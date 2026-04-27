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

import com.bornfire.brrs.entities.NSFR_Summary_Entity_old;
import com.bornfire.brrs.services.BRRS_SCH_17_New_Service.SCH_17_Archival_Detail_Entity1;
import com.bornfire.brrs.services.BRRS_SCH_17_New_Service.SCH_17_Detail_Entity1;
import com.bornfire.brrs.services.BRRS_SCH_17_New_Service.SCH_17_PK;


/*import com.bornfire.brrs.entities.ADISB2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.ADISB2_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_NSFR_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_NSFR_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_NSFR_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_NSFR_Summary_Repo;
import com.bornfire.brrs.entities.NSFR_Archival_Detail_Entity;
import com.bornfire.brrs.entities.NSFR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.NSFR_Detail_Entity;
import com.bornfire.brrs.entities.NSFR_Summary_Entity;
import com.bornfire.brrs.services.BRRS_SCH_17_New_Service.SCH17RowMapper;
import com.bornfire.brrs.services.BRRS_SCH_17_New_Service.SCH_17_Summary_Entity1;
*/
@Service
@Transactional
public class BRRS_NSFR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_NSFR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	 // =====================================================
    // ENTITY MANAGER (Acts like Repository)
    // =====================================================

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Fetch data by report date
    public List<NSFR_Summary_Entity> getDataByDate(Date reportDate) {

        String sql = "SELECT * FROM BRRS_NSFR_SUMMARYTABLE WHERE REPORT_DATE = ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{reportDate},
                new NSFRRowMapper()
        );
    }

    // =========================================================
    // GET REPORT_DATE + REPORT_VERSION
    // =========================================================

    public List<Object[]> getNSFR_Newarchival() {

        String sql =
            "SELECT REPORT_DATE, REPORT_VERSION " +
            "FROM BRRS_NFSR_ARCHIVALTABLE_SUMMARY " +
            "ORDER BY REPORT_VERSION";

        return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[] {
                rs.getDate("REPORT_DATE"),
                rs.getBigDecimal("REPORT_VERSION")
            }
        );
    }
    
   //=========================================================
    		//GET ARCHIVAL FULL DATA BY DATE + VERSION
    		//=========================================================
    		public List<NSFR_Archival_Summary_Entity>
    		getdatabydateListarchival(
    		      Date reportDate,
    		      BigDecimal reportVersion) {

    		  String sql =
    		      "SELECT * FROM BRRS_NFSR_ARCHIVALTABLE_SUMMARY " +
    		      "WHERE REPORT_DATE = ? " +
    		      "AND REPORT_VERSION = ?";

    		  return jdbcTemplate.query(
    		          sql,
    		          new Object[]{
    		                  reportDate,
    		                  reportVersion
    		          },
    		          new NFSRArchivalRowMapper()
    		  );
    		}
    		
    		//=========================================================
    		//GET ALL WITH VERSION
    		//=========================================================

    		public List<NSFR_Archival_Summary_Entity>
    		getdatabydateListWithVersion() {

    		 String sql =
    		     "SELECT * FROM BRRS_NFSR_ARCHIVALTABLE_SUMMARY " +
    		     "WHERE REPORT_VERSION IS NOT NULL " +
    		     "ORDER BY REPORT_VERSION ASC";

    		 return jdbcTemplate.query(
    		         sql,
    		         new NFSRArchivalRowMapper()
    		 );
    		}

    		//=========================================================
    		//GET MAX VERSION BY DATE
    		//=========================================================

    		public BigDecimal findMaxVersion(Date reportDate) {

    		 String sql =
    		     "SELECT MAX(REPORT_VERSION) " +
    		     "FROM BRRS_NFSR_ARCHIVALTABLE_SUMMARY " +
    		     "WHERE REPORT_DATE = ?";

    		 return jdbcTemplate.queryForObject(
    		         sql,
    		         new Object[]{reportDate},
    		         BigDecimal.class
    		 );
    		}
    
    		// =========================================================
    		// 1. BY DATE + LABEL + CRITERIA
    		// =========================================================
    		public List<NSFR_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(
    		        Date reportDate,
    		        String reportLabel,
    		        String reportAddlCriteria1) {

    		    String sql =
    		        "SELECT * FROM BRRS_NFSR_DETAILTABLE " +
    		        "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

    		    return jdbcTemplate.query(
    		            sql,
    		            new Object[]{reportDate, reportLabel, reportAddlCriteria1},
    		            new NSFRDetailRowMapper()
    		    );
    		}

    		
    		// =========================================================
    		// 2. GET ALL (BY DATE - simple)
    		// =========================================================
    		public List<NSFR_Detail_Entity> getDetaildatabydateList(Date reportdate) {

    		    String sql = "SELECT * FROM BRRS_NFSR_DETAILTABLE WHERE REPORT_DATE = ?";

    		    return jdbcTemplate.query(
    		            sql,
    		            new Object[]{reportdate},
    		            new NSFRDetailRowMapper()
    		    );
    		}
    	
    		// =========================================================
    		// 3. PAGINATION
    		// =========================================================
    		public List<NSFR_Detail_Entity> getDetaildatabydateList(
    		        Date reportdate, int offset, int limit) {

    		    String sql =
    		        "SELECT * FROM BRRS_NSFR_DETAILTABLE " +
    		        "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    		    return jdbcTemplate.query(
    		            sql,
    		            new Object[]{reportdate, offset, limit},
    		            new NSFRDetailRowMapper()
    		    );
    		}

    		// =========================================================
    		// 4. COUNT
    		// =========================================================
    		public int getDetaildatacount(Date reportdate) {

    		    String sql =
    		        "SELECT COUNT(*) FROM BRRS_NSFR_DETAILTABLE WHERE REPORT_DATE = ?";

    		    return jdbcTemplate.queryForObject(
    		            sql,
    		            new Object[]{reportdate},
    		            Integer.class
    		    );
    		}

    		// =========================================================
    		// 5. BY LABEL + CRITERIA
    		// =========================================================
    		public List<NSFR_Detail_Entity> GetDetailDataByRowIdAndColumnId(
    		        String reportLabel,
    		        String reportAddlCriteria1,
    		        Date reportdate) {

    		    String sql =
    		        "SELECT * FROM BRRS_NSFR_DETAILTABLE " +
    		        "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

    		    return jdbcTemplate.query(
    		            sql,
    		            new Object[]{reportLabel, reportAddlCriteria1, reportdate},
    		            new NSFRDetailRowMapper()
    		    );
    		}
    		
    		// =========================================================
    		// 6. BY ACCOUNT NUMBER
    		// =========================================================
    		public NSFR_Detail_Entity findByAcctnumber(String acct_number) {

    		    String sql =
    		        "SELECT * FROM BRRS_NSFR_DETAILTABLE WHERE ACCT_NUMBER = ?";

    		    return jdbcTemplate.queryForObject(
    		            sql,
    		            new Object[]{acct_number},
    		            new NSFRDetailRowMapper()
    		    );
    		}
    		
    		// =========================================================
    		// 7. BY SNO
    		// =========================================================
    		public NSFR_Detail_Entity findBySno(String sno) {

    		    String sql =
    		        "SELECT * FROM BRRS_NSFR_DETAILTABLE WHERE SNO = ?";

    		    return jdbcTemplate.queryForObject(
    		            sql,
    		            new Object[]{sno},
    		            new NSFRDetailRowMapper()
    		    );
    		}

    		// =========================================================
    		// 1. GET BY DATE + VERSION
    		// =========================================================
    		public List<NSFR_Archival_Detail_Entity> getArchivalDetaildatabydateList(
    		        Date reportdate,
    		        String dataEntryVersion) {

    		    String sql =
    		        "SELECT * FROM BRRS_NSFR_ARCHIVALTABLE_DETAIL " +
    		        "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    		    return jdbcTemplate.query(
    		            sql,
    		            new Object[]{reportdate, dataEntryVersion},
    		            new NSFRArchivalDetailRowMapper()
    		    );
    		}
    		
    		// =========================================================
    		// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION
    		// =========================================================
    		public List<NSFR_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
    		        String reportLabel,
    		        String reportAddlCriteria1,
    		        Date reportdate,
    		        String dataEntryVersion) {

    		    String sql =
    		        "SELECT * FROM BRRS_NSFR_ARCHIVALTABLE_DETAIL " +
    		        "WHERE REPORT_LABEL = ? " +
    		        "AND REPORT_ADDL_CRITERIA_1 = ? " +
    		        "AND REPORT_DATE = ? " +
    		        "AND DATA_ENTRY_VERSION = ?";

    		    return jdbcTemplate.query(
    		            sql,
    		            new Object[]{reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion},
    		            new NSFRArchivalDetailRowMapper()
    		    );
    		}


    		   // =========================================================
    	    // ROW MAPPER
    	    // =========================================================
    		

    		class NSFRRowMapper implements RowMapper<NSFR_Summary_Entity> {

    		    @Override
    		    public NSFR_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

    		        NSFR_Summary_Entity obj = new NSFR_Summary_Entity();

    		        try {

    		           
    		            // =========================
    		            // R12 to R66 (Dynamic Mapping)
    		            // =========================
    		            for (int i = 12; i <= 66; i++) {

    		                obj.getClass()
    		                   .getMethod("setR" + i + "_FACTOR_BOB", BigDecimal.class)
    		                   .invoke(obj, rs.getBigDecimal("R" + i + "_FACTOR_BOB"));

    		                obj.getClass()
    		                   .getMethod("setR" + i + "_TOTAL_AMOUNT_BOB", BigDecimal.class)
    		                   .invoke(obj, rs.getBigDecimal("R" + i + "_TOTAL_AMOUNT_BOB"));

    		                obj.getClass()
    		                   .getMethod("setR" + i + "_WITH_FACTOR_APPLIED_BOB", BigDecimal.class)
    		                   .invoke(obj, rs.getBigDecimal("R" + i + "_WITH_FACTOR_APPLIED_BOB"));
    		            }

    		        } catch (Exception e) {
    		            throw new SQLException("Error mapping NSFR dynamic fields", e);
    		        }

    		        
    		        // =========================
    		        // COMMON FIELDS (FIXED)
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
    		
    	    
    	    public static class NSFR_Summary_Entity {
    	    	private BigDecimal R12_FACTOR_BOB;
    	    	private BigDecimal R12_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R12_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R13_FACTOR_BOB;
    	    	private BigDecimal R13_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R13_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R14_FACTOR_BOB;
    	    	private BigDecimal R14_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R14_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R15_FACTOR_BOB;
    	    	private BigDecimal R15_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R15_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R16_FACTOR_BOB;
    	    	private BigDecimal R16_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R16_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R17_FACTOR_BOB;
    	    	private BigDecimal R17_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R17_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R18_FACTOR_BOB;
    	    	private BigDecimal R18_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R18_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R19_FACTOR_BOB;
    	    	private BigDecimal R19_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R19_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R20_FACTOR_BOB;
    	    	private BigDecimal R20_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R20_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R21_FACTOR_BOB;
    	    	private BigDecimal R21_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R21_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R22_FACTOR_BOB;
    	    	private BigDecimal R22_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R22_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R23_FACTOR_BOB;
    	    	private BigDecimal R23_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R23_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R24_FACTOR_BOB;
    	    	private BigDecimal R24_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R24_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R25_FACTOR_BOB;
    	    	private BigDecimal R25_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R25_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R26_FACTOR_BOB;
    	    	private BigDecimal R26_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R26_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R27_FACTOR_BOB;
    	    	private BigDecimal R27_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R27_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R28_FACTOR_BOB;
    	    	private BigDecimal R28_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R28_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R29_FACTOR_BOB;
    	    	private BigDecimal R29_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R29_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R30_FACTOR_BOB;
    	    	private BigDecimal R30_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R30_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R31_FACTOR_BOB;
    	    	private BigDecimal R31_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R31_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R32_FACTOR_BOB;
    	    	private BigDecimal R32_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R32_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R33_FACTOR_BOB;
    	    	private BigDecimal R33_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R33_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R34_FACTOR_BOB;
    	    	private BigDecimal R34_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R34_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R35_FACTOR_BOB;
    	    	private BigDecimal R35_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R35_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R36_FACTOR_BOB;
    	    	private BigDecimal R36_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R36_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R37_FACTOR_BOB;
    	    	private BigDecimal R37_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R37_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R38_FACTOR_BOB;
    	    	private BigDecimal R38_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R38_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R39_FACTOR_BOB;
    	    	private BigDecimal R39_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R39_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R40_FACTOR_BOB;
    	    	private BigDecimal R40_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R40_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R41_FACTOR_BOB;
    	    	private BigDecimal R41_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R41_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R42_FACTOR_BOB;
    	    	private BigDecimal R42_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R42_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R43_FACTOR_BOB;
    	    	private BigDecimal R43_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R43_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R44_FACTOR_BOB;
    	    	private BigDecimal R44_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R44_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R45_FACTOR_BOB;
    	    	private BigDecimal R45_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R45_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R46_FACTOR_BOB;
    	    	private BigDecimal R46_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R46_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R47_FACTOR_BOB;
    	    	private BigDecimal R47_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R47_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R48_FACTOR_BOB;
    	    	private BigDecimal R48_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R48_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R49_FACTOR_BOB;
    	    	private BigDecimal R49_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R49_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R50_FACTOR_BOB;
    	    	private BigDecimal R50_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R50_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R51_FACTOR_BOB;
    	    	private BigDecimal R51_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R51_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R52_FACTOR_BOB;
    	    	private BigDecimal R52_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R52_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R53_FACTOR_BOB;
    	    	private BigDecimal R53_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R53_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R54_FACTOR_BOB;
    	    	private BigDecimal R54_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R54_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R55_FACTOR_BOB;
    	    	private BigDecimal R55_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R55_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R56_FACTOR_BOB;
    	    	private BigDecimal R56_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R56_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R57_FACTOR_BOB;
    	    	private BigDecimal R57_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R57_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R58_FACTOR_BOB;
    	    	private BigDecimal R58_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R58_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R59_FACTOR_BOB;
    	    	private BigDecimal R59_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R59_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R60_FACTOR_BOB;
    	    	private BigDecimal R60_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R60_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R61_FACTOR_BOB;
    	    	private BigDecimal R61_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R61_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R62_FACTOR_BOB;
    	    	private BigDecimal R62_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R62_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R63_FACTOR_BOB;
    	    	private BigDecimal R63_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R63_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R64_FACTOR_BOB;
    	    	private BigDecimal R64_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R64_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R65_FACTOR_BOB;
    	    	private BigDecimal R65_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R65_WITH_FACTOR_APPLIED_BOB;
    	    	private BigDecimal R66_FACTOR_BOB;
    	    	private BigDecimal R66_TOTAL_AMOUNT_BOB;
    	    	private BigDecimal R66_WITH_FACTOR_APPLIED_BOB;
    	    	
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


    	  
    	    
     // =====================================================
        // GETTERS & SETTERS (FULL)
        // =====================================================

    	 // -------- R12 --------
    	    public BigDecimal getR12_FACTOR_BOB() { return R12_FACTOR_BOB; }
    	    public void setR12_FACTOR_BOB(BigDecimal v) { this.R12_FACTOR_BOB = v; }

    	    public BigDecimal getR12_TOTAL_AMOUNT_BOB() { return R12_TOTAL_AMOUNT_BOB; }
    	    public void setR12_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R12_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR12_WITH_FACTOR_APPLIED_BOB() { return R12_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR12_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R12_WITH_FACTOR_APPLIED_BOB = v; }
    	    
    	    
    	 // -------- R13 --------
    	    public BigDecimal getR13_FACTOR_BOB() { return R13_FACTOR_BOB; }
    	    public void setR13_FACTOR_BOB(BigDecimal v) { this.R13_FACTOR_BOB = v; }

    	    public BigDecimal getR13_TOTAL_AMOUNT_BOB() { return R13_TOTAL_AMOUNT_BOB; }
    	    public void setR13_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R13_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR13_WITH_FACTOR_APPLIED_BOB() { return R13_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR13_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R13_WITH_FACTOR_APPLIED_BOB = v; }
    	    
    	    
    	 // -------- R14 --------
    	    public BigDecimal getR14_FACTOR_BOB() { return R14_FACTOR_BOB; }
    	    public void setR14_FACTOR_BOB(BigDecimal v) { this.R14_FACTOR_BOB = v; }

    	    public BigDecimal getR14_TOTAL_AMOUNT_BOB() { return R14_TOTAL_AMOUNT_BOB; }
    	    public void setR14_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R14_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR14_WITH_FACTOR_APPLIED_BOB() { return R14_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR14_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R14_WITH_FACTOR_APPLIED_BOB = v; }
    	    
    	 // -------- R15 --------
    	    public BigDecimal getR15_FACTOR_BOB() { return R15_FACTOR_BOB; }
    	    public void setR15_FACTOR_BOB(BigDecimal v) { this.R15_FACTOR_BOB = v; }

    	    public BigDecimal getR15_TOTAL_AMOUNT_BOB() { return R15_TOTAL_AMOUNT_BOB; }
    	    public void setR15_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R15_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR15_WITH_FACTOR_APPLIED_BOB() { return R15_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR15_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R15_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R16 --------
    	    public BigDecimal getR16_FACTOR_BOB() { return R16_FACTOR_BOB; }
    	    public void setR16_FACTOR_BOB(BigDecimal v) { this.R16_FACTOR_BOB = v; }

    	    public BigDecimal getR16_TOTAL_AMOUNT_BOB() { return R16_TOTAL_AMOUNT_BOB; }
    	    public void setR16_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R16_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR16_WITH_FACTOR_APPLIED_BOB() { return R16_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR16_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R16_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R17 --------
    	    public BigDecimal getR17_FACTOR_BOB() { return R17_FACTOR_BOB; }
    	    public void setR17_FACTOR_BOB(BigDecimal v) { this.R17_FACTOR_BOB = v; }

    	    public BigDecimal getR17_TOTAL_AMOUNT_BOB() { return R17_TOTAL_AMOUNT_BOB; }
    	    public void setR17_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R17_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR17_WITH_FACTOR_APPLIED_BOB() { return R17_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR17_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R17_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R18 --------
    	    public BigDecimal getR18_FACTOR_BOB() { return R18_FACTOR_BOB; }
    	    public void setR18_FACTOR_BOB(BigDecimal v) { this.R18_FACTOR_BOB = v; }

    	    public BigDecimal getR18_TOTAL_AMOUNT_BOB() { return R18_TOTAL_AMOUNT_BOB; }
    	    public void setR18_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R18_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR18_WITH_FACTOR_APPLIED_BOB() { return R18_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR18_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R18_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R19 --------
    	    public BigDecimal getR19_FACTOR_BOB() { return R19_FACTOR_BOB; }
    	    public void setR19_FACTOR_BOB(BigDecimal v) { this.R19_FACTOR_BOB = v; }

    	    public BigDecimal getR19_TOTAL_AMOUNT_BOB() { return R19_TOTAL_AMOUNT_BOB; }
    	    public void setR19_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R19_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR19_WITH_FACTOR_APPLIED_BOB() { return R19_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR19_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R19_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R20 --------
    	    public BigDecimal getR20_FACTOR_BOB() { return R20_FACTOR_BOB; }
    	    public void setR20_FACTOR_BOB(BigDecimal v) { this.R20_FACTOR_BOB = v; }

    	    public BigDecimal getR20_TOTAL_AMOUNT_BOB() { return R20_TOTAL_AMOUNT_BOB; }
    	    public void setR20_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R20_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR20_WITH_FACTOR_APPLIED_BOB() { return R20_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR20_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R20_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R21 --------
    	    public BigDecimal getR21_FACTOR_BOB() { return R21_FACTOR_BOB; }
    	    public void setR21_FACTOR_BOB(BigDecimal v) { this.R21_FACTOR_BOB = v; }

    	    public BigDecimal getR21_TOTAL_AMOUNT_BOB() { return R21_TOTAL_AMOUNT_BOB; }
    	    public void setR21_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R21_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR21_WITH_FACTOR_APPLIED_BOB() { return R21_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR21_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R21_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R22 --------
    	    public BigDecimal getR22_FACTOR_BOB() { return R22_FACTOR_BOB; }
    	    public void setR22_FACTOR_BOB(BigDecimal v) { this.R22_FACTOR_BOB = v; }

    	    public BigDecimal getR22_TOTAL_AMOUNT_BOB() { return R22_TOTAL_AMOUNT_BOB; }
    	    public void setR22_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R22_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR22_WITH_FACTOR_APPLIED_BOB() { return R22_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR22_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R22_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R23 --------
    	    public BigDecimal getR23_FACTOR_BOB() { return R23_FACTOR_BOB; }
    	    public void setR23_FACTOR_BOB(BigDecimal v) { this.R23_FACTOR_BOB = v; }

    	    public BigDecimal getR23_TOTAL_AMOUNT_BOB() { return R23_TOTAL_AMOUNT_BOB; }
    	    public void setR23_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R23_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR23_WITH_FACTOR_APPLIED_BOB() { return R23_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR23_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R23_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R24 --------
    	    public BigDecimal getR24_FACTOR_BOB() { return R24_FACTOR_BOB; }
    	    public void setR24_FACTOR_BOB(BigDecimal v) { this.R24_FACTOR_BOB = v; }

    	    public BigDecimal getR24_TOTAL_AMOUNT_BOB() { return R24_TOTAL_AMOUNT_BOB; }
    	    public void setR24_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R24_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR24_WITH_FACTOR_APPLIED_BOB() { return R24_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR24_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R24_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R25 --------
    	    public BigDecimal getR25_FACTOR_BOB() { return R25_FACTOR_BOB; }
    	    public void setR25_FACTOR_BOB(BigDecimal v) { this.R25_FACTOR_BOB = v; }

    	    public BigDecimal getR25_TOTAL_AMOUNT_BOB() { return R25_TOTAL_AMOUNT_BOB; }
    	    public void setR25_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R25_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR25_WITH_FACTOR_APPLIED_BOB() { return R25_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR25_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R25_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R26 --------
    	    public BigDecimal getR26_FACTOR_BOB() { return R26_FACTOR_BOB; }
    	    public void setR26_FACTOR_BOB(BigDecimal v) { this.R26_FACTOR_BOB = v; }

    	    public BigDecimal getR26_TOTAL_AMOUNT_BOB() { return R26_TOTAL_AMOUNT_BOB; }
    	    public void setR26_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R26_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR26_WITH_FACTOR_APPLIED_BOB() { return R26_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR26_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R26_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R27 --------
    	    public BigDecimal getR27_FACTOR_BOB() { return R27_FACTOR_BOB; }
    	    public void setR27_FACTOR_BOB(BigDecimal v) { this.R27_FACTOR_BOB = v; }

    	    public BigDecimal getR27_TOTAL_AMOUNT_BOB() { return R27_TOTAL_AMOUNT_BOB; }
    	    public void setR27_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R27_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR27_WITH_FACTOR_APPLIED_BOB() { return R27_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR27_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R27_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R28 --------
    	    public BigDecimal getR28_FACTOR_BOB() { return R28_FACTOR_BOB; }
    	    public void setR28_FACTOR_BOB(BigDecimal v) { this.R28_FACTOR_BOB = v; }

    	    public BigDecimal getR28_TOTAL_AMOUNT_BOB() { return R28_TOTAL_AMOUNT_BOB; }
    	    public void setR28_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R28_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR28_WITH_FACTOR_APPLIED_BOB() { return R28_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR28_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R28_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R29 --------
    	    public BigDecimal getR29_FACTOR_BOB() { return R29_FACTOR_BOB; }
    	    public void setR29_FACTOR_BOB(BigDecimal v) { this.R29_FACTOR_BOB = v; }

    	    public BigDecimal getR29_TOTAL_AMOUNT_BOB() { return R29_TOTAL_AMOUNT_BOB; }
    	    public void setR29_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R29_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR29_WITH_FACTOR_APPLIED_BOB() { return R29_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR29_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R29_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R30 --------
    	    public BigDecimal getR30_FACTOR_BOB() { return R30_FACTOR_BOB; }
    	    public void setR30_FACTOR_BOB(BigDecimal v) { this.R30_FACTOR_BOB = v; }

    	    public BigDecimal getR30_TOTAL_AMOUNT_BOB() { return R30_TOTAL_AMOUNT_BOB; }
    	    public void setR30_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R30_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR30_WITH_FACTOR_APPLIED_BOB() { return R30_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR30_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R30_WITH_FACTOR_APPLIED_BOB = v; }
    	    
    	    	 // -------- R31 --------
    	    public BigDecimal getR31_FACTOR_BOB() { return R31_FACTOR_BOB; }
    	    public void setR31_FACTOR_BOB(BigDecimal v) { this.R31_FACTOR_BOB = v; }

    	    public BigDecimal getR31_TOTAL_AMOUNT_BOB() { return R31_TOTAL_AMOUNT_BOB; }
    	    public void setR31_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R31_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR31_WITH_FACTOR_APPLIED_BOB() { return R31_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR31_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R31_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R32 --------
    	    public BigDecimal getR32_FACTOR_BOB() { return R32_FACTOR_BOB; }
    	    public void setR32_FACTOR_BOB(BigDecimal v) { this.R32_FACTOR_BOB = v; }

    	    public BigDecimal getR32_TOTAL_AMOUNT_BOB() { return R32_TOTAL_AMOUNT_BOB; }
    	    public void setR32_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R32_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR32_WITH_FACTOR_APPLIED_BOB() { return R32_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR32_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R32_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R33 --------
    	    public BigDecimal getR33_FACTOR_BOB() { return R33_FACTOR_BOB; }
    	    public void setR33_FACTOR_BOB(BigDecimal v) { this.R33_FACTOR_BOB = v; }

    	    public BigDecimal getR33_TOTAL_AMOUNT_BOB() { return R33_TOTAL_AMOUNT_BOB; }
    	    public void setR33_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R33_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR33_WITH_FACTOR_APPLIED_BOB() { return R33_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR33_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R33_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R34 --------
    	    public BigDecimal getR34_FACTOR_BOB() { return R34_FACTOR_BOB; }
    	    public void setR34_FACTOR_BOB(BigDecimal v) { this.R34_FACTOR_BOB = v; }

    	    public BigDecimal getR34_TOTAL_AMOUNT_BOB() { return R34_TOTAL_AMOUNT_BOB; }
    	    public void setR34_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R34_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR34_WITH_FACTOR_APPLIED_BOB() { return R34_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR34_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R34_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R35 --------
    	    public BigDecimal getR35_FACTOR_BOB() { return R35_FACTOR_BOB; }
    	    public void setR35_FACTOR_BOB(BigDecimal v) { this.R35_FACTOR_BOB = v; }

    	    public BigDecimal getR35_TOTAL_AMOUNT_BOB() { return R35_TOTAL_AMOUNT_BOB; }
    	    public void setR35_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R35_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR35_WITH_FACTOR_APPLIED_BOB() { return R35_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR35_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R35_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R36 --------
    	    public BigDecimal getR36_FACTOR_BOB() { return R36_FACTOR_BOB; }
    	    public void setR36_FACTOR_BOB(BigDecimal v) { this.R36_FACTOR_BOB = v; }

    	    public BigDecimal getR36_TOTAL_AMOUNT_BOB() { return R36_TOTAL_AMOUNT_BOB; }
    	    public void setR36_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R36_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR36_WITH_FACTOR_APPLIED_BOB() { return R36_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR36_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R36_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R37 --------
    	    public BigDecimal getR37_FACTOR_BOB() { return R37_FACTOR_BOB; }
    	    public void setR37_FACTOR_BOB(BigDecimal v) { this.R37_FACTOR_BOB = v; }

    	    public BigDecimal getR37_TOTAL_AMOUNT_BOB() { return R37_TOTAL_AMOUNT_BOB; }
    	    public void setR37_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R37_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR37_WITH_FACTOR_APPLIED_BOB() { return R37_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR37_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R37_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R38 --------
    	    public BigDecimal getR38_FACTOR_BOB() { return R38_FACTOR_BOB; }
    	    public void setR38_FACTOR_BOB(BigDecimal v) { this.R38_FACTOR_BOB = v; }

    	    public BigDecimal getR38_TOTAL_AMOUNT_BOB() { return R38_TOTAL_AMOUNT_BOB; }
    	    public void setR38_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R38_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR38_WITH_FACTOR_APPLIED_BOB() { return R38_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR38_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R38_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R39 --------
    	    public BigDecimal getR39_FACTOR_BOB() { return R39_FACTOR_BOB; }
    	    public void setR39_FACTOR_BOB(BigDecimal v) { this.R39_FACTOR_BOB = v; }

    	    public BigDecimal getR39_TOTAL_AMOUNT_BOB() { return R39_TOTAL_AMOUNT_BOB; }
    	    public void setR39_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R39_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR39_WITH_FACTOR_APPLIED_BOB() { return R39_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR39_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R39_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R40 --------
    	    public BigDecimal getR40_FACTOR_BOB() { return R40_FACTOR_BOB; }
    	    public void setR40_FACTOR_BOB(BigDecimal v) { this.R40_FACTOR_BOB = v; }

    	    public BigDecimal getR40_TOTAL_AMOUNT_BOB() { return R40_TOTAL_AMOUNT_BOB; }
    	    public void setR40_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R40_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR40_WITH_FACTOR_APPLIED_BOB() { return R40_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR40_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R40_WITH_FACTOR_APPLIED_BOB = v; }
    	    
    	 // -------- R41 --------
    	    public BigDecimal getR41_FACTOR_BOB() { return R41_FACTOR_BOB; }
    	    public void setR41_FACTOR_BOB(BigDecimal v) { this.R41_FACTOR_BOB = v; }

    	    public BigDecimal getR41_TOTAL_AMOUNT_BOB() { return R41_TOTAL_AMOUNT_BOB; }
    	    public void setR41_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R41_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR41_WITH_FACTOR_APPLIED_BOB() { return R41_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR41_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R41_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R42 --------
    	    public BigDecimal getR42_FACTOR_BOB() { return R42_FACTOR_BOB; }
    	    public void setR42_FACTOR_BOB(BigDecimal v) { this.R42_FACTOR_BOB = v; }

    	    public BigDecimal getR42_TOTAL_AMOUNT_BOB() { return R42_TOTAL_AMOUNT_BOB; }
    	    public void setR42_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R42_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR42_WITH_FACTOR_APPLIED_BOB() { return R42_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR42_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R42_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R43 --------
    	    public BigDecimal getR43_FACTOR_BOB() { return R43_FACTOR_BOB; }
    	    public void setR43_FACTOR_BOB(BigDecimal v) { this.R43_FACTOR_BOB = v; }

    	    public BigDecimal getR43_TOTAL_AMOUNT_BOB() { return R43_TOTAL_AMOUNT_BOB; }
    	    public void setR43_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R43_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR43_WITH_FACTOR_APPLIED_BOB() { return R43_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR43_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R43_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R44 --------
    	    public BigDecimal getR44_FACTOR_BOB() { return R44_FACTOR_BOB; }
    	    public void setR44_FACTOR_BOB(BigDecimal v) { this.R44_FACTOR_BOB = v; }

    	    public BigDecimal getR44_TOTAL_AMOUNT_BOB() { return R44_TOTAL_AMOUNT_BOB; }
    	    public void setR44_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R44_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR44_WITH_FACTOR_APPLIED_BOB() { return R44_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR44_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R44_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R45 --------
    	    public BigDecimal getR45_FACTOR_BOB() { return R45_FACTOR_BOB; }
    	    public void setR45_FACTOR_BOB(BigDecimal v) { this.R45_FACTOR_BOB = v; }

    	    public BigDecimal getR45_TOTAL_AMOUNT_BOB() { return R45_TOTAL_AMOUNT_BOB; }
    	    public void setR45_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R45_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR45_WITH_FACTOR_APPLIED_BOB() { return R45_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR45_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R45_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R46 --------
    	    public BigDecimal getR46_FACTOR_BOB() { return R46_FACTOR_BOB; }
    	    public void setR46_FACTOR_BOB(BigDecimal v) { this.R46_FACTOR_BOB = v; }

    	    public BigDecimal getR46_TOTAL_AMOUNT_BOB() { return R46_TOTAL_AMOUNT_BOB; }
    	    public void setR46_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R46_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR46_WITH_FACTOR_APPLIED_BOB() { return R46_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR46_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R46_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R47 --------
    	    public BigDecimal getR47_FACTOR_BOB() { return R47_FACTOR_BOB; }
    	    public void setR47_FACTOR_BOB(BigDecimal v) { this.R47_FACTOR_BOB = v; }

    	    public BigDecimal getR47_TOTAL_AMOUNT_BOB() { return R47_TOTAL_AMOUNT_BOB; }
    	    public void setR47_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R47_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR47_WITH_FACTOR_APPLIED_BOB() { return R47_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR47_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R47_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R48 --------
    	    public BigDecimal getR48_FACTOR_BOB() { return R48_FACTOR_BOB; }
    	    public void setR48_FACTOR_BOB(BigDecimal v) { this.R48_FACTOR_BOB = v; }

    	    public BigDecimal getR48_TOTAL_AMOUNT_BOB() { return R48_TOTAL_AMOUNT_BOB; }
    	    public void setR48_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R48_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR48_WITH_FACTOR_APPLIED_BOB() { return R48_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR48_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R48_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R49 --------
    	    public BigDecimal getR49_FACTOR_BOB() { return R49_FACTOR_BOB; }
    	    public void setR49_FACTOR_BOB(BigDecimal v) { this.R49_FACTOR_BOB = v; }

    	    public BigDecimal getR49_TOTAL_AMOUNT_BOB() { return R49_TOTAL_AMOUNT_BOB; }
    	    public void setR49_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R49_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR49_WITH_FACTOR_APPLIED_BOB() { return R49_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR49_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R49_WITH_FACTOR_APPLIED_BOB = v; }

    	    // -------- R50 --------
    	    public BigDecimal getR50_FACTOR_BOB() { return R50_FACTOR_BOB; }
    	    public void setR50_FACTOR_BOB(BigDecimal v) { this.R50_FACTOR_BOB = v; }

    	    public BigDecimal getR50_TOTAL_AMOUNT_BOB() { return R50_TOTAL_AMOUNT_BOB; }
    	    public void setR50_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R50_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR50_WITH_FACTOR_APPLIED_BOB() { return R50_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR50_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R50_WITH_FACTOR_APPLIED_BOB = v; }
    	    
    	 // -------- R51 --------
    	    public BigDecimal getR51_FACTOR_BOB() { return R51_FACTOR_BOB; }
    	    public void setR51_FACTOR_BOB(BigDecimal v) { this.R51_FACTOR_BOB = v; }

    	    public BigDecimal getR51_TOTAL_AMOUNT_BOB() { return R51_TOTAL_AMOUNT_BOB; }
    	    public void setR51_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R51_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR51_WITH_FACTOR_APPLIED_BOB() { return R51_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR51_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R51_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R52 --------
    	    public BigDecimal getR52_FACTOR_BOB() { return R52_FACTOR_BOB; }
    	    public void setR52_FACTOR_BOB(BigDecimal v) { this.R52_FACTOR_BOB = v; }

    	    public BigDecimal getR52_TOTAL_AMOUNT_BOB() { return R52_TOTAL_AMOUNT_BOB; }
    	    public void setR52_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R52_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR52_WITH_FACTOR_APPLIED_BOB() { return R52_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR52_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R52_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R53 --------
    	    public BigDecimal getR53_FACTOR_BOB() { return R53_FACTOR_BOB; }
    	    public void setR53_FACTOR_BOB(BigDecimal v) { this.R53_FACTOR_BOB = v; }

    	    public BigDecimal getR53_TOTAL_AMOUNT_BOB() { return R53_TOTAL_AMOUNT_BOB; }
    	    public void setR53_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R53_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR53_WITH_FACTOR_APPLIED_BOB() { return R53_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR53_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R53_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R54 --------
    	    public BigDecimal getR54_FACTOR_BOB() { return R54_FACTOR_BOB; }
    	    public void setR54_FACTOR_BOB(BigDecimal v) { this.R54_FACTOR_BOB = v; }

    	    public BigDecimal getR54_TOTAL_AMOUNT_BOB() { return R54_TOTAL_AMOUNT_BOB; }
    	    public void setR54_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R54_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR54_WITH_FACTOR_APPLIED_BOB() { return R54_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR54_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R54_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R55 --------
    	    public BigDecimal getR55_FACTOR_BOB() { return R55_FACTOR_BOB; }
    	    public void setR55_FACTOR_BOB(BigDecimal v) { this.R55_FACTOR_BOB = v; }

    	    public BigDecimal getR55_TOTAL_AMOUNT_BOB() { return R55_TOTAL_AMOUNT_BOB; }
    	    public void setR55_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R55_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR55_WITH_FACTOR_APPLIED_BOB() { return R55_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR55_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R55_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R56 --------
    	    public BigDecimal getR56_FACTOR_BOB() { return R56_FACTOR_BOB; }
    	    public void setR56_FACTOR_BOB(BigDecimal v) { this.R56_FACTOR_BOB = v; }

    	    public BigDecimal getR56_TOTAL_AMOUNT_BOB() { return R56_TOTAL_AMOUNT_BOB; }
    	    public void setR56_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R56_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR56_WITH_FACTOR_APPLIED_BOB() { return R56_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR56_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R56_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R57 --------
    	    public BigDecimal getR57_FACTOR_BOB() { return R57_FACTOR_BOB; }
    	    public void setR57_FACTOR_BOB(BigDecimal v) { this.R57_FACTOR_BOB = v; }

    	    public BigDecimal getR57_TOTAL_AMOUNT_BOB() { return R57_TOTAL_AMOUNT_BOB; }
    	    public void setR57_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R57_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR57_WITH_FACTOR_APPLIED_BOB() { return R57_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR57_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R57_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R58 --------
    	    public BigDecimal getR58_FACTOR_BOB() { return R58_FACTOR_BOB; }
    	    public void setR58_FACTOR_BOB(BigDecimal v) { this.R58_FACTOR_BOB = v; }

    	    public BigDecimal getR58_TOTAL_AMOUNT_BOB() { return R58_TOTAL_AMOUNT_BOB; }
    	    public void setR58_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R58_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR58_WITH_FACTOR_APPLIED_BOB() { return R58_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR58_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R58_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R59 --------
    	    public BigDecimal getR59_FACTOR_BOB() { return R59_FACTOR_BOB; }
    	    public void setR59_FACTOR_BOB(BigDecimal v) { this.R59_FACTOR_BOB = v; }

    	    public BigDecimal getR59_TOTAL_AMOUNT_BOB() { return R59_TOTAL_AMOUNT_BOB; }
    	    public void setR59_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R59_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR59_WITH_FACTOR_APPLIED_BOB() { return R59_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR59_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R59_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R60 --------
    	    public BigDecimal getR60_FACTOR_BOB() { return R60_FACTOR_BOB; }
    	    public void setR60_FACTOR_BOB(BigDecimal v) { this.R60_FACTOR_BOB = v; }

    	    public BigDecimal getR60_TOTAL_AMOUNT_BOB() { return R60_TOTAL_AMOUNT_BOB; }
    	    public void setR60_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R60_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR60_WITH_FACTOR_APPLIED_BOB() { return R60_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR60_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R60_WITH_FACTOR_APPLIED_BOB = v; }
    	    
    	 // -------- R61 --------
    	    public BigDecimal getR61_FACTOR_BOB() { return R61_FACTOR_BOB; }
    	    public void setR61_FACTOR_BOB(BigDecimal v) { this.R61_FACTOR_BOB = v; }

    	    public BigDecimal getR61_TOTAL_AMOUNT_BOB() { return R61_TOTAL_AMOUNT_BOB; }
    	    public void setR61_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R61_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR61_WITH_FACTOR_APPLIED_BOB() { return R61_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR61_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R61_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R62 --------
    	    public BigDecimal getR62_FACTOR_BOB() { return R62_FACTOR_BOB; }
    	    public void setR62_FACTOR_BOB(BigDecimal v) { this.R62_FACTOR_BOB = v; }

    	    public BigDecimal getR62_TOTAL_AMOUNT_BOB() { return R62_TOTAL_AMOUNT_BOB; }
    	    public void setR62_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R62_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR62_WITH_FACTOR_APPLIED_BOB() { return R62_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR62_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R62_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R63 --------
    	    public BigDecimal getR63_FACTOR_BOB() { return R63_FACTOR_BOB; }
    	    public void setR63_FACTOR_BOB(BigDecimal v) { this.R63_FACTOR_BOB = v; }

    	    public BigDecimal getR63_TOTAL_AMOUNT_BOB() { return R63_TOTAL_AMOUNT_BOB; }
    	    public void setR63_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R63_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR63_WITH_FACTOR_APPLIED_BOB() { return R63_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR63_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R63_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R64 --------
    	    public BigDecimal getR64_FACTOR_BOB() { return R64_FACTOR_BOB; }
    	    public void setR64_FACTOR_BOB(BigDecimal v) { this.R64_FACTOR_BOB = v; }

    	    public BigDecimal getR64_TOTAL_AMOUNT_BOB() { return R64_TOTAL_AMOUNT_BOB; }
    	    public void setR64_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R64_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR64_WITH_FACTOR_APPLIED_BOB() { return R64_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR64_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R64_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R65 --------
    	    public BigDecimal getR65_FACTOR_BOB() { return R65_FACTOR_BOB; }
    	    public void setR65_FACTOR_BOB(BigDecimal v) { this.R65_FACTOR_BOB = v; }

    	    public BigDecimal getR65_TOTAL_AMOUNT_BOB() { return R65_TOTAL_AMOUNT_BOB; }
    	    public void setR65_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R65_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR65_WITH_FACTOR_APPLIED_BOB() { return R65_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR65_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R65_WITH_FACTOR_APPLIED_BOB = v; }


    	    // -------- R66 --------
    	    public BigDecimal getR66_FACTOR_BOB() { return R66_FACTOR_BOB; }
    	    public void setR66_FACTOR_BOB(BigDecimal v) { this.R66_FACTOR_BOB = v; }

    	    public BigDecimal getR66_TOTAL_AMOUNT_BOB() { return R66_TOTAL_AMOUNT_BOB; }
    	    public void setR66_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R66_TOTAL_AMOUNT_BOB = v; }

    	    public BigDecimal getR66_WITH_FACTOR_APPLIED_BOB() { return R66_WITH_FACTOR_APPLIED_BOB; }
    	    public void setR66_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R66_WITH_FACTOR_APPLIED_BOB = v; }
    	    
    	    
    	    public Date getReport_date() { return report_date; }
    	    public void setReport_date(Date v) { this.report_date = v; }

    	    public BigDecimal getReport_version() { return report_version; }
    	    public void setReport_version(BigDecimal v) { this.report_version = v; }

    	    public String getReport_frequency() { return report_frequency; }
    	    public void setReport_frequency(String v) { this.report_frequency = v; }

    	    public String getReport_code() { return report_code; }
    	    public void setReport_code(String v) { this.report_code = v; }

    	    public String getReport_desc() { return report_desc; }
    	    public void setReport_desc(String v) { this.report_desc = v; }

    	    public String getEntity_flg() { return entity_flg; }
    	    public void setEntity_flg(String v) { this.entity_flg = v; }

    	    public String getModify_flg() { return modify_flg; }
    	    public void setModify_flg(String v) { this.modify_flg = v; }

    	    public String getDel_flg() { return del_flg; }
    	    public void setDel_flg(String v) { this.del_flg = v; }
    	}
    	    
    	  //=========================================================
    	  //ARCHIVAL ROW MAPPER
    	  //=========================================================

    	  class NFSRArchivalRowMapper implements RowMapper<NSFR_Archival_Summary_Entity> {

    	   @Override
    	   public NSFR_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
    	           throws SQLException {

    		   NSFR_Archival_Summary_Entity obj =
    	               new NSFR_Archival_Summary_Entity();    
    	    
    		   
    		   try {

		           
		            // =========================
		            // R12 to R66 (Dynamic Mapping)
		            // =========================
		            for (int i = 12; i <= 66; i++) {

		                obj.getClass()
		                   .getMethod("setR" + i + "_FACTOR_BOB", BigDecimal.class)
		                   .invoke(obj, rs.getBigDecimal("R" + i + "_FACTOR_BOB"));

		                obj.getClass()
		                   .getMethod("setR" + i + "_TOTAL_AMOUNT_BOB", BigDecimal.class)
		                   .invoke(obj, rs.getBigDecimal("R" + i + "_TOTAL_AMOUNT_BOB"));

		                obj.getClass()
		                   .getMethod("setR" + i + "_WITH_FACTOR_APPLIED_BOB", BigDecimal.class)
		                   .invoke(obj, rs.getBigDecimal("R" + i + "_WITH_FACTOR_APPLIED_BOB"));
		            }

		        } catch (Exception e) {
		            throw new SQLException("Error mapping NSFR dynamic fields", e);
		        }

		        
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
    	  
    	//==============================
    	// COMPOSITE KEY CLASS INSIDE SERVICE
    	// ==============================
    	public static class NSFR_PK implements Serializable {

    	    private Date report_date;
    	    private BigDecimal report_version;

    	    public NSFR_PK() {}

    	    public NSFR_PK(Date report_date, BigDecimal report_version) {
    	        this.report_date = report_date;
    	        this.report_version = report_version;
    	    }

    	    @Override
    	    public boolean equals(Object o) {
    	        if (this == o) return true;
    	        if (!(o instanceof NSFR_PK)) return false;
    	        NSFR_PK that = (NSFR_PK) o;
    	        return Objects.equals(report_date, that.report_date) &&
    	               Objects.equals(report_version, that.report_version);
    	    }

    	    @Override
    	    public int hashCode() {
    	        return Objects.hash(report_date, report_version);
    	    }

    	    public Date getReport_date() { return report_date; }
    	    public void setReport_date(Date report_date) { this.report_date = report_date; }

    	    public BigDecimal getReport_version() { return report_version; }
    	    public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
    	}
	
    @IdClass(NSFR_PK.class)
	    public static class NSFR_Archival_Summary_Entity {
	    	private BigDecimal R12_FACTOR_BOB;
	    	private BigDecimal R12_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R12_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R13_FACTOR_BOB;
	    	private BigDecimal R13_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R13_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R14_FACTOR_BOB;
	    	private BigDecimal R14_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R14_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R15_FACTOR_BOB;
	    	private BigDecimal R15_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R15_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R16_FACTOR_BOB;
	    	private BigDecimal R16_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R16_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R17_FACTOR_BOB;
	    	private BigDecimal R17_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R17_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R18_FACTOR_BOB;
	    	private BigDecimal R18_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R18_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R19_FACTOR_BOB;
	    	private BigDecimal R19_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R19_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R20_FACTOR_BOB;
	    	private BigDecimal R20_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R20_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R21_FACTOR_BOB;
	    	private BigDecimal R21_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R21_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R22_FACTOR_BOB;
	    	private BigDecimal R22_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R22_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R23_FACTOR_BOB;
	    	private BigDecimal R23_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R23_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R24_FACTOR_BOB;
	    	private BigDecimal R24_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R24_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R25_FACTOR_BOB;
	    	private BigDecimal R25_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R25_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R26_FACTOR_BOB;
	    	private BigDecimal R26_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R26_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R27_FACTOR_BOB;
	    	private BigDecimal R27_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R27_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R28_FACTOR_BOB;
	    	private BigDecimal R28_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R28_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R29_FACTOR_BOB;
	    	private BigDecimal R29_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R29_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R30_FACTOR_BOB;
	    	private BigDecimal R30_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R30_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R31_FACTOR_BOB;
	    	private BigDecimal R31_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R31_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R32_FACTOR_BOB;
	    	private BigDecimal R32_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R32_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R33_FACTOR_BOB;
	    	private BigDecimal R33_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R33_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R34_FACTOR_BOB;
	    	private BigDecimal R34_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R34_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R35_FACTOR_BOB;
	    	private BigDecimal R35_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R35_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R36_FACTOR_BOB;
	    	private BigDecimal R36_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R36_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R37_FACTOR_BOB;
	    	private BigDecimal R37_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R37_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R38_FACTOR_BOB;
	    	private BigDecimal R38_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R38_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R39_FACTOR_BOB;
	    	private BigDecimal R39_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R39_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R40_FACTOR_BOB;
	    	private BigDecimal R40_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R40_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R41_FACTOR_BOB;
	    	private BigDecimal R41_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R41_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R42_FACTOR_BOB;
	    	private BigDecimal R42_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R42_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R43_FACTOR_BOB;
	    	private BigDecimal R43_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R43_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R44_FACTOR_BOB;
	    	private BigDecimal R44_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R44_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R45_FACTOR_BOB;
	    	private BigDecimal R45_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R45_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R46_FACTOR_BOB;
	    	private BigDecimal R46_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R46_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R47_FACTOR_BOB;
	    	private BigDecimal R47_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R47_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R48_FACTOR_BOB;
	    	private BigDecimal R48_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R48_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R49_FACTOR_BOB;
	    	private BigDecimal R49_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R49_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R50_FACTOR_BOB;
	    	private BigDecimal R50_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R50_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R51_FACTOR_BOB;
	    	private BigDecimal R51_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R51_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R52_FACTOR_BOB;
	    	private BigDecimal R52_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R52_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R53_FACTOR_BOB;
	    	private BigDecimal R53_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R53_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R54_FACTOR_BOB;
	    	private BigDecimal R54_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R54_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R55_FACTOR_BOB;
	    	private BigDecimal R55_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R55_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R56_FACTOR_BOB;
	    	private BigDecimal R56_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R56_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R57_FACTOR_BOB;
	    	private BigDecimal R57_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R57_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R58_FACTOR_BOB;
	    	private BigDecimal R58_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R58_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R59_FACTOR_BOB;
	    	private BigDecimal R59_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R59_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R60_FACTOR_BOB;
	    	private BigDecimal R60_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R60_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R61_FACTOR_BOB;
	    	private BigDecimal R61_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R61_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R62_FACTOR_BOB;
	    	private BigDecimal R62_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R62_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R63_FACTOR_BOB;
	    	private BigDecimal R63_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R63_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R64_FACTOR_BOB;
	    	private BigDecimal R64_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R64_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R65_FACTOR_BOB;
	    	private BigDecimal R65_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R65_WITH_FACTOR_APPLIED_BOB;
	    	private BigDecimal R66_FACTOR_BOB;
	    	private BigDecimal R66_TOTAL_AMOUNT_BOB;
	    	private BigDecimal R66_WITH_FACTOR_APPLIED_BOB;
	    	
	    	// ================= COMMON =================
	    	@Temporal(TemporalType.DATE)
	       	@DateTimeFormat(pattern = "dd/MM/yyyy")
	       	@Id
	       		
	       	private Date	report_date;
	       	 @Column(name = "REPORT_VERSION")
	       	 @Id
	       	private BigDecimal	report_version;
	       	@Column(name = "REPORT_RESUBDATE")

	           private Date reportResubDate;
	       	private String	report_frequency;
	       	private String	report_code;
	       	private String	report_desc;
	       	private String	entity_flg;
	       	private String	modify_flg;
	       	private String	del_flg;

	  
	    
// =====================================================
   // GETTERS & SETTERS (FULL)
   // =====================================================

	 // -------- R12 --------
	    public BigDecimal getR12_FACTOR_BOB() { return R12_FACTOR_BOB; }
	    public void setR12_FACTOR_BOB(BigDecimal v) { this.R12_FACTOR_BOB = v; }

	    public BigDecimal getR12_TOTAL_AMOUNT_BOB() { return R12_TOTAL_AMOUNT_BOB; }
	    public void setR12_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R12_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR12_WITH_FACTOR_APPLIED_BOB() { return R12_WITH_FACTOR_APPLIED_BOB; }
	    public void setR12_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R12_WITH_FACTOR_APPLIED_BOB = v; }
	    
	    
	 // -------- R13 --------
	    public BigDecimal getR13_FACTOR_BOB() { return R13_FACTOR_BOB; }
	    public void setR13_FACTOR_BOB(BigDecimal v) { this.R13_FACTOR_BOB = v; }

	    public BigDecimal getR13_TOTAL_AMOUNT_BOB() { return R13_TOTAL_AMOUNT_BOB; }
	    public void setR13_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R13_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR13_WITH_FACTOR_APPLIED_BOB() { return R13_WITH_FACTOR_APPLIED_BOB; }
	    public void setR13_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R13_WITH_FACTOR_APPLIED_BOB = v; }
	    
	    
	 // -------- R14 --------
	    public BigDecimal getR14_FACTOR_BOB() { return R14_FACTOR_BOB; }
	    public void setR14_FACTOR_BOB(BigDecimal v) { this.R14_FACTOR_BOB = v; }

	    public BigDecimal getR14_TOTAL_AMOUNT_BOB() { return R14_TOTAL_AMOUNT_BOB; }
	    public void setR14_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R14_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR14_WITH_FACTOR_APPLIED_BOB() { return R14_WITH_FACTOR_APPLIED_BOB; }
	    public void setR14_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R14_WITH_FACTOR_APPLIED_BOB = v; }
	    
	 // -------- R15 --------
	    public BigDecimal getR15_FACTOR_BOB() { return R15_FACTOR_BOB; }
	    public void setR15_FACTOR_BOB(BigDecimal v) { this.R15_FACTOR_BOB = v; }

	    public BigDecimal getR15_TOTAL_AMOUNT_BOB() { return R15_TOTAL_AMOUNT_BOB; }
	    public void setR15_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R15_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR15_WITH_FACTOR_APPLIED_BOB() { return R15_WITH_FACTOR_APPLIED_BOB; }
	    public void setR15_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R15_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R16 --------
	    public BigDecimal getR16_FACTOR_BOB() { return R16_FACTOR_BOB; }
	    public void setR16_FACTOR_BOB(BigDecimal v) { this.R16_FACTOR_BOB = v; }

	    public BigDecimal getR16_TOTAL_AMOUNT_BOB() { return R16_TOTAL_AMOUNT_BOB; }
	    public void setR16_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R16_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR16_WITH_FACTOR_APPLIED_BOB() { return R16_WITH_FACTOR_APPLIED_BOB; }
	    public void setR16_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R16_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R17 --------
	    public BigDecimal getR17_FACTOR_BOB() { return R17_FACTOR_BOB; }
	    public void setR17_FACTOR_BOB(BigDecimal v) { this.R17_FACTOR_BOB = v; }

	    public BigDecimal getR17_TOTAL_AMOUNT_BOB() { return R17_TOTAL_AMOUNT_BOB; }
	    public void setR17_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R17_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR17_WITH_FACTOR_APPLIED_BOB() { return R17_WITH_FACTOR_APPLIED_BOB; }
	    public void setR17_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R17_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R18 --------
	    public BigDecimal getR18_FACTOR_BOB() { return R18_FACTOR_BOB; }
	    public void setR18_FACTOR_BOB(BigDecimal v) { this.R18_FACTOR_BOB = v; }

	    public BigDecimal getR18_TOTAL_AMOUNT_BOB() { return R18_TOTAL_AMOUNT_BOB; }
	    public void setR18_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R18_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR18_WITH_FACTOR_APPLIED_BOB() { return R18_WITH_FACTOR_APPLIED_BOB; }
	    public void setR18_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R18_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R19 --------
	    public BigDecimal getR19_FACTOR_BOB() { return R19_FACTOR_BOB; }
	    public void setR19_FACTOR_BOB(BigDecimal v) { this.R19_FACTOR_BOB = v; }

	    public BigDecimal getR19_TOTAL_AMOUNT_BOB() { return R19_TOTAL_AMOUNT_BOB; }
	    public void setR19_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R19_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR19_WITH_FACTOR_APPLIED_BOB() { return R19_WITH_FACTOR_APPLIED_BOB; }
	    public void setR19_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R19_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R20 --------
	    public BigDecimal getR20_FACTOR_BOB() { return R20_FACTOR_BOB; }
	    public void setR20_FACTOR_BOB(BigDecimal v) { this.R20_FACTOR_BOB = v; }

	    public BigDecimal getR20_TOTAL_AMOUNT_BOB() { return R20_TOTAL_AMOUNT_BOB; }
	    public void setR20_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R20_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR20_WITH_FACTOR_APPLIED_BOB() { return R20_WITH_FACTOR_APPLIED_BOB; }
	    public void setR20_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R20_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R21 --------
	    public BigDecimal getR21_FACTOR_BOB() { return R21_FACTOR_BOB; }
	    public void setR21_FACTOR_BOB(BigDecimal v) { this.R21_FACTOR_BOB = v; }

	    public BigDecimal getR21_TOTAL_AMOUNT_BOB() { return R21_TOTAL_AMOUNT_BOB; }
	    public void setR21_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R21_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR21_WITH_FACTOR_APPLIED_BOB() { return R21_WITH_FACTOR_APPLIED_BOB; }
	    public void setR21_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R21_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R22 --------
	    public BigDecimal getR22_FACTOR_BOB() { return R22_FACTOR_BOB; }
	    public void setR22_FACTOR_BOB(BigDecimal v) { this.R22_FACTOR_BOB = v; }

	    public BigDecimal getR22_TOTAL_AMOUNT_BOB() { return R22_TOTAL_AMOUNT_BOB; }
	    public void setR22_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R22_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR22_WITH_FACTOR_APPLIED_BOB() { return R22_WITH_FACTOR_APPLIED_BOB; }
	    public void setR22_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R22_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R23 --------
	    public BigDecimal getR23_FACTOR_BOB() { return R23_FACTOR_BOB; }
	    public void setR23_FACTOR_BOB(BigDecimal v) { this.R23_FACTOR_BOB = v; }

	    public BigDecimal getR23_TOTAL_AMOUNT_BOB() { return R23_TOTAL_AMOUNT_BOB; }
	    public void setR23_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R23_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR23_WITH_FACTOR_APPLIED_BOB() { return R23_WITH_FACTOR_APPLIED_BOB; }
	    public void setR23_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R23_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R24 --------
	    public BigDecimal getR24_FACTOR_BOB() { return R24_FACTOR_BOB; }
	    public void setR24_FACTOR_BOB(BigDecimal v) { this.R24_FACTOR_BOB = v; }

	    public BigDecimal getR24_TOTAL_AMOUNT_BOB() { return R24_TOTAL_AMOUNT_BOB; }
	    public void setR24_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R24_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR24_WITH_FACTOR_APPLIED_BOB() { return R24_WITH_FACTOR_APPLIED_BOB; }
	    public void setR24_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R24_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R25 --------
	    public BigDecimal getR25_FACTOR_BOB() { return R25_FACTOR_BOB; }
	    public void setR25_FACTOR_BOB(BigDecimal v) { this.R25_FACTOR_BOB = v; }

	    public BigDecimal getR25_TOTAL_AMOUNT_BOB() { return R25_TOTAL_AMOUNT_BOB; }
	    public void setR25_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R25_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR25_WITH_FACTOR_APPLIED_BOB() { return R25_WITH_FACTOR_APPLIED_BOB; }
	    public void setR25_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R25_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R26 --------
	    public BigDecimal getR26_FACTOR_BOB() { return R26_FACTOR_BOB; }
	    public void setR26_FACTOR_BOB(BigDecimal v) { this.R26_FACTOR_BOB = v; }

	    public BigDecimal getR26_TOTAL_AMOUNT_BOB() { return R26_TOTAL_AMOUNT_BOB; }
	    public void setR26_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R26_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR26_WITH_FACTOR_APPLIED_BOB() { return R26_WITH_FACTOR_APPLIED_BOB; }
	    public void setR26_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R26_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R27 --------
	    public BigDecimal getR27_FACTOR_BOB() { return R27_FACTOR_BOB; }
	    public void setR27_FACTOR_BOB(BigDecimal v) { this.R27_FACTOR_BOB = v; }

	    public BigDecimal getR27_TOTAL_AMOUNT_BOB() { return R27_TOTAL_AMOUNT_BOB; }
	    public void setR27_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R27_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR27_WITH_FACTOR_APPLIED_BOB() { return R27_WITH_FACTOR_APPLIED_BOB; }
	    public void setR27_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R27_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R28 --------
	    public BigDecimal getR28_FACTOR_BOB() { return R28_FACTOR_BOB; }
	    public void setR28_FACTOR_BOB(BigDecimal v) { this.R28_FACTOR_BOB = v; }

	    public BigDecimal getR28_TOTAL_AMOUNT_BOB() { return R28_TOTAL_AMOUNT_BOB; }
	    public void setR28_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R28_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR28_WITH_FACTOR_APPLIED_BOB() { return R28_WITH_FACTOR_APPLIED_BOB; }
	    public void setR28_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R28_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R29 --------
	    public BigDecimal getR29_FACTOR_BOB() { return R29_FACTOR_BOB; }
	    public void setR29_FACTOR_BOB(BigDecimal v) { this.R29_FACTOR_BOB = v; }

	    public BigDecimal getR29_TOTAL_AMOUNT_BOB() { return R29_TOTAL_AMOUNT_BOB; }
	    public void setR29_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R29_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR29_WITH_FACTOR_APPLIED_BOB() { return R29_WITH_FACTOR_APPLIED_BOB; }
	    public void setR29_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R29_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R30 --------
	    public BigDecimal getR30_FACTOR_BOB() { return R30_FACTOR_BOB; }
	    public void setR30_FACTOR_BOB(BigDecimal v) { this.R30_FACTOR_BOB = v; }

	    public BigDecimal getR30_TOTAL_AMOUNT_BOB() { return R30_TOTAL_AMOUNT_BOB; }
	    public void setR30_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R30_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR30_WITH_FACTOR_APPLIED_BOB() { return R30_WITH_FACTOR_APPLIED_BOB; }
	    public void setR30_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R30_WITH_FACTOR_APPLIED_BOB = v; }
	    
	    	 // -------- R31 --------
	    public BigDecimal getR31_FACTOR_BOB() { return R31_FACTOR_BOB; }
	    public void setR31_FACTOR_BOB(BigDecimal v) { this.R31_FACTOR_BOB = v; }

	    public BigDecimal getR31_TOTAL_AMOUNT_BOB() { return R31_TOTAL_AMOUNT_BOB; }
	    public void setR31_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R31_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR31_WITH_FACTOR_APPLIED_BOB() { return R31_WITH_FACTOR_APPLIED_BOB; }
	    public void setR31_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R31_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R32 --------
	    public BigDecimal getR32_FACTOR_BOB() { return R32_FACTOR_BOB; }
	    public void setR32_FACTOR_BOB(BigDecimal v) { this.R32_FACTOR_BOB = v; }

	    public BigDecimal getR32_TOTAL_AMOUNT_BOB() { return R32_TOTAL_AMOUNT_BOB; }
	    public void setR32_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R32_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR32_WITH_FACTOR_APPLIED_BOB() { return R32_WITH_FACTOR_APPLIED_BOB; }
	    public void setR32_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R32_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R33 --------
	    public BigDecimal getR33_FACTOR_BOB() { return R33_FACTOR_BOB; }
	    public void setR33_FACTOR_BOB(BigDecimal v) { this.R33_FACTOR_BOB = v; }

	    public BigDecimal getR33_TOTAL_AMOUNT_BOB() { return R33_TOTAL_AMOUNT_BOB; }
	    public void setR33_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R33_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR33_WITH_FACTOR_APPLIED_BOB() { return R33_WITH_FACTOR_APPLIED_BOB; }
	    public void setR33_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R33_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R34 --------
	    public BigDecimal getR34_FACTOR_BOB() { return R34_FACTOR_BOB; }
	    public void setR34_FACTOR_BOB(BigDecimal v) { this.R34_FACTOR_BOB = v; }

	    public BigDecimal getR34_TOTAL_AMOUNT_BOB() { return R34_TOTAL_AMOUNT_BOB; }
	    public void setR34_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R34_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR34_WITH_FACTOR_APPLIED_BOB() { return R34_WITH_FACTOR_APPLIED_BOB; }
	    public void setR34_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R34_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R35 --------
	    public BigDecimal getR35_FACTOR_BOB() { return R35_FACTOR_BOB; }
	    public void setR35_FACTOR_BOB(BigDecimal v) { this.R35_FACTOR_BOB = v; }

	    public BigDecimal getR35_TOTAL_AMOUNT_BOB() { return R35_TOTAL_AMOUNT_BOB; }
	    public void setR35_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R35_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR35_WITH_FACTOR_APPLIED_BOB() { return R35_WITH_FACTOR_APPLIED_BOB; }
	    public void setR35_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R35_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R36 --------
	    public BigDecimal getR36_FACTOR_BOB() { return R36_FACTOR_BOB; }
	    public void setR36_FACTOR_BOB(BigDecimal v) { this.R36_FACTOR_BOB = v; }

	    public BigDecimal getR36_TOTAL_AMOUNT_BOB() { return R36_TOTAL_AMOUNT_BOB; }
	    public void setR36_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R36_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR36_WITH_FACTOR_APPLIED_BOB() { return R36_WITH_FACTOR_APPLIED_BOB; }
	    public void setR36_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R36_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R37 --------
	    public BigDecimal getR37_FACTOR_BOB() { return R37_FACTOR_BOB; }
	    public void setR37_FACTOR_BOB(BigDecimal v) { this.R37_FACTOR_BOB = v; }

	    public BigDecimal getR37_TOTAL_AMOUNT_BOB() { return R37_TOTAL_AMOUNT_BOB; }
	    public void setR37_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R37_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR37_WITH_FACTOR_APPLIED_BOB() { return R37_WITH_FACTOR_APPLIED_BOB; }
	    public void setR37_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R37_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R38 --------
	    public BigDecimal getR38_FACTOR_BOB() { return R38_FACTOR_BOB; }
	    public void setR38_FACTOR_BOB(BigDecimal v) { this.R38_FACTOR_BOB = v; }

	    public BigDecimal getR38_TOTAL_AMOUNT_BOB() { return R38_TOTAL_AMOUNT_BOB; }
	    public void setR38_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R38_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR38_WITH_FACTOR_APPLIED_BOB() { return R38_WITH_FACTOR_APPLIED_BOB; }
	    public void setR38_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R38_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R39 --------
	    public BigDecimal getR39_FACTOR_BOB() { return R39_FACTOR_BOB; }
	    public void setR39_FACTOR_BOB(BigDecimal v) { this.R39_FACTOR_BOB = v; }

	    public BigDecimal getR39_TOTAL_AMOUNT_BOB() { return R39_TOTAL_AMOUNT_BOB; }
	    public void setR39_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R39_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR39_WITH_FACTOR_APPLIED_BOB() { return R39_WITH_FACTOR_APPLIED_BOB; }
	    public void setR39_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R39_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R40 --------
	    public BigDecimal getR40_FACTOR_BOB() { return R40_FACTOR_BOB; }
	    public void setR40_FACTOR_BOB(BigDecimal v) { this.R40_FACTOR_BOB = v; }

	    public BigDecimal getR40_TOTAL_AMOUNT_BOB() { return R40_TOTAL_AMOUNT_BOB; }
	    public void setR40_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R40_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR40_WITH_FACTOR_APPLIED_BOB() { return R40_WITH_FACTOR_APPLIED_BOB; }
	    public void setR40_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R40_WITH_FACTOR_APPLIED_BOB = v; }
	    
	 // -------- R41 --------
	    public BigDecimal getR41_FACTOR_BOB() { return R41_FACTOR_BOB; }
	    public void setR41_FACTOR_BOB(BigDecimal v) { this.R41_FACTOR_BOB = v; }

	    public BigDecimal getR41_TOTAL_AMOUNT_BOB() { return R41_TOTAL_AMOUNT_BOB; }
	    public void setR41_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R41_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR41_WITH_FACTOR_APPLIED_BOB() { return R41_WITH_FACTOR_APPLIED_BOB; }
	    public void setR41_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R41_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R42 --------
	    public BigDecimal getR42_FACTOR_BOB() { return R42_FACTOR_BOB; }
	    public void setR42_FACTOR_BOB(BigDecimal v) { this.R42_FACTOR_BOB = v; }

	    public BigDecimal getR42_TOTAL_AMOUNT_BOB() { return R42_TOTAL_AMOUNT_BOB; }
	    public void setR42_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R42_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR42_WITH_FACTOR_APPLIED_BOB() { return R42_WITH_FACTOR_APPLIED_BOB; }
	    public void setR42_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R42_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R43 --------
	    public BigDecimal getR43_FACTOR_BOB() { return R43_FACTOR_BOB; }
	    public void setR43_FACTOR_BOB(BigDecimal v) { this.R43_FACTOR_BOB = v; }

	    public BigDecimal getR43_TOTAL_AMOUNT_BOB() { return R43_TOTAL_AMOUNT_BOB; }
	    public void setR43_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R43_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR43_WITH_FACTOR_APPLIED_BOB() { return R43_WITH_FACTOR_APPLIED_BOB; }
	    public void setR43_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R43_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R44 --------
	    public BigDecimal getR44_FACTOR_BOB() { return R44_FACTOR_BOB; }
	    public void setR44_FACTOR_BOB(BigDecimal v) { this.R44_FACTOR_BOB = v; }

	    public BigDecimal getR44_TOTAL_AMOUNT_BOB() { return R44_TOTAL_AMOUNT_BOB; }
	    public void setR44_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R44_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR44_WITH_FACTOR_APPLIED_BOB() { return R44_WITH_FACTOR_APPLIED_BOB; }
	    public void setR44_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R44_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R45 --------
	    public BigDecimal getR45_FACTOR_BOB() { return R45_FACTOR_BOB; }
	    public void setR45_FACTOR_BOB(BigDecimal v) { this.R45_FACTOR_BOB = v; }

	    public BigDecimal getR45_TOTAL_AMOUNT_BOB() { return R45_TOTAL_AMOUNT_BOB; }
	    public void setR45_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R45_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR45_WITH_FACTOR_APPLIED_BOB() { return R45_WITH_FACTOR_APPLIED_BOB; }
	    public void setR45_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R45_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R46 --------
	    public BigDecimal getR46_FACTOR_BOB() { return R46_FACTOR_BOB; }
	    public void setR46_FACTOR_BOB(BigDecimal v) { this.R46_FACTOR_BOB = v; }

	    public BigDecimal getR46_TOTAL_AMOUNT_BOB() { return R46_TOTAL_AMOUNT_BOB; }
	    public void setR46_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R46_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR46_WITH_FACTOR_APPLIED_BOB() { return R46_WITH_FACTOR_APPLIED_BOB; }
	    public void setR46_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R46_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R47 --------
	    public BigDecimal getR47_FACTOR_BOB() { return R47_FACTOR_BOB; }
	    public void setR47_FACTOR_BOB(BigDecimal v) { this.R47_FACTOR_BOB = v; }

	    public BigDecimal getR47_TOTAL_AMOUNT_BOB() { return R47_TOTAL_AMOUNT_BOB; }
	    public void setR47_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R47_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR47_WITH_FACTOR_APPLIED_BOB() { return R47_WITH_FACTOR_APPLIED_BOB; }
	    public void setR47_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R47_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R48 --------
	    public BigDecimal getR48_FACTOR_BOB() { return R48_FACTOR_BOB; }
	    public void setR48_FACTOR_BOB(BigDecimal v) { this.R48_FACTOR_BOB = v; }

	    public BigDecimal getR48_TOTAL_AMOUNT_BOB() { return R48_TOTAL_AMOUNT_BOB; }
	    public void setR48_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R48_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR48_WITH_FACTOR_APPLIED_BOB() { return R48_WITH_FACTOR_APPLIED_BOB; }
	    public void setR48_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R48_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R49 --------
	    public BigDecimal getR49_FACTOR_BOB() { return R49_FACTOR_BOB; }
	    public void setR49_FACTOR_BOB(BigDecimal v) { this.R49_FACTOR_BOB = v; }

	    public BigDecimal getR49_TOTAL_AMOUNT_BOB() { return R49_TOTAL_AMOUNT_BOB; }
	    public void setR49_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R49_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR49_WITH_FACTOR_APPLIED_BOB() { return R49_WITH_FACTOR_APPLIED_BOB; }
	    public void setR49_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R49_WITH_FACTOR_APPLIED_BOB = v; }

	    // -------- R50 --------
	    public BigDecimal getR50_FACTOR_BOB() { return R50_FACTOR_BOB; }
	    public void setR50_FACTOR_BOB(BigDecimal v) { this.R50_FACTOR_BOB = v; }

	    public BigDecimal getR50_TOTAL_AMOUNT_BOB() { return R50_TOTAL_AMOUNT_BOB; }
	    public void setR50_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R50_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR50_WITH_FACTOR_APPLIED_BOB() { return R50_WITH_FACTOR_APPLIED_BOB; }
	    public void setR50_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R50_WITH_FACTOR_APPLIED_BOB = v; }
	    
	 // -------- R51 --------
	    public BigDecimal getR51_FACTOR_BOB() { return R51_FACTOR_BOB; }
	    public void setR51_FACTOR_BOB(BigDecimal v) { this.R51_FACTOR_BOB = v; }

	    public BigDecimal getR51_TOTAL_AMOUNT_BOB() { return R51_TOTAL_AMOUNT_BOB; }
	    public void setR51_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R51_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR51_WITH_FACTOR_APPLIED_BOB() { return R51_WITH_FACTOR_APPLIED_BOB; }
	    public void setR51_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R51_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R52 --------
	    public BigDecimal getR52_FACTOR_BOB() { return R52_FACTOR_BOB; }
	    public void setR52_FACTOR_BOB(BigDecimal v) { this.R52_FACTOR_BOB = v; }

	    public BigDecimal getR52_TOTAL_AMOUNT_BOB() { return R52_TOTAL_AMOUNT_BOB; }
	    public void setR52_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R52_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR52_WITH_FACTOR_APPLIED_BOB() { return R52_WITH_FACTOR_APPLIED_BOB; }
	    public void setR52_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R52_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R53 --------
	    public BigDecimal getR53_FACTOR_BOB() { return R53_FACTOR_BOB; }
	    public void setR53_FACTOR_BOB(BigDecimal v) { this.R53_FACTOR_BOB = v; }

	    public BigDecimal getR53_TOTAL_AMOUNT_BOB() { return R53_TOTAL_AMOUNT_BOB; }
	    public void setR53_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R53_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR53_WITH_FACTOR_APPLIED_BOB() { return R53_WITH_FACTOR_APPLIED_BOB; }
	    public void setR53_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R53_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R54 --------
	    public BigDecimal getR54_FACTOR_BOB() { return R54_FACTOR_BOB; }
	    public void setR54_FACTOR_BOB(BigDecimal v) { this.R54_FACTOR_BOB = v; }

	    public BigDecimal getR54_TOTAL_AMOUNT_BOB() { return R54_TOTAL_AMOUNT_BOB; }
	    public void setR54_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R54_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR54_WITH_FACTOR_APPLIED_BOB() { return R54_WITH_FACTOR_APPLIED_BOB; }
	    public void setR54_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R54_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R55 --------
	    public BigDecimal getR55_FACTOR_BOB() { return R55_FACTOR_BOB; }
	    public void setR55_FACTOR_BOB(BigDecimal v) { this.R55_FACTOR_BOB = v; }

	    public BigDecimal getR55_TOTAL_AMOUNT_BOB() { return R55_TOTAL_AMOUNT_BOB; }
	    public void setR55_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R55_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR55_WITH_FACTOR_APPLIED_BOB() { return R55_WITH_FACTOR_APPLIED_BOB; }
	    public void setR55_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R55_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R56 --------
	    public BigDecimal getR56_FACTOR_BOB() { return R56_FACTOR_BOB; }
	    public void setR56_FACTOR_BOB(BigDecimal v) { this.R56_FACTOR_BOB = v; }

	    public BigDecimal getR56_TOTAL_AMOUNT_BOB() { return R56_TOTAL_AMOUNT_BOB; }
	    public void setR56_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R56_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR56_WITH_FACTOR_APPLIED_BOB() { return R56_WITH_FACTOR_APPLIED_BOB; }
	    public void setR56_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R56_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R57 --------
	    public BigDecimal getR57_FACTOR_BOB() { return R57_FACTOR_BOB; }
	    public void setR57_FACTOR_BOB(BigDecimal v) { this.R57_FACTOR_BOB = v; }

	    public BigDecimal getR57_TOTAL_AMOUNT_BOB() { return R57_TOTAL_AMOUNT_BOB; }
	    public void setR57_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R57_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR57_WITH_FACTOR_APPLIED_BOB() { return R57_WITH_FACTOR_APPLIED_BOB; }
	    public void setR57_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R57_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R58 --------
	    public BigDecimal getR58_FACTOR_BOB() { return R58_FACTOR_BOB; }
	    public void setR58_FACTOR_BOB(BigDecimal v) { this.R58_FACTOR_BOB = v; }

	    public BigDecimal getR58_TOTAL_AMOUNT_BOB() { return R58_TOTAL_AMOUNT_BOB; }
	    public void setR58_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R58_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR58_WITH_FACTOR_APPLIED_BOB() { return R58_WITH_FACTOR_APPLIED_BOB; }
	    public void setR58_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R58_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R59 --------
	    public BigDecimal getR59_FACTOR_BOB() { return R59_FACTOR_BOB; }
	    public void setR59_FACTOR_BOB(BigDecimal v) { this.R59_FACTOR_BOB = v; }

	    public BigDecimal getR59_TOTAL_AMOUNT_BOB() { return R59_TOTAL_AMOUNT_BOB; }
	    public void setR59_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R59_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR59_WITH_FACTOR_APPLIED_BOB() { return R59_WITH_FACTOR_APPLIED_BOB; }
	    public void setR59_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R59_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R60 --------
	    public BigDecimal getR60_FACTOR_BOB() { return R60_FACTOR_BOB; }
	    public void setR60_FACTOR_BOB(BigDecimal v) { this.R60_FACTOR_BOB = v; }

	    public BigDecimal getR60_TOTAL_AMOUNT_BOB() { return R60_TOTAL_AMOUNT_BOB; }
	    public void setR60_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R60_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR60_WITH_FACTOR_APPLIED_BOB() { return R60_WITH_FACTOR_APPLIED_BOB; }
	    public void setR60_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R60_WITH_FACTOR_APPLIED_BOB = v; }
	    
	 // -------- R61 --------
	    public BigDecimal getR61_FACTOR_BOB() { return R61_FACTOR_BOB; }
	    public void setR61_FACTOR_BOB(BigDecimal v) { this.R61_FACTOR_BOB = v; }

	    public BigDecimal getR61_TOTAL_AMOUNT_BOB() { return R61_TOTAL_AMOUNT_BOB; }
	    public void setR61_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R61_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR61_WITH_FACTOR_APPLIED_BOB() { return R61_WITH_FACTOR_APPLIED_BOB; }
	    public void setR61_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R61_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R62 --------
	    public BigDecimal getR62_FACTOR_BOB() { return R62_FACTOR_BOB; }
	    public void setR62_FACTOR_BOB(BigDecimal v) { this.R62_FACTOR_BOB = v; }

	    public BigDecimal getR62_TOTAL_AMOUNT_BOB() { return R62_TOTAL_AMOUNT_BOB; }
	    public void setR62_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R62_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR62_WITH_FACTOR_APPLIED_BOB() { return R62_WITH_FACTOR_APPLIED_BOB; }
	    public void setR62_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R62_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R63 --------
	    public BigDecimal getR63_FACTOR_BOB() { return R63_FACTOR_BOB; }
	    public void setR63_FACTOR_BOB(BigDecimal v) { this.R63_FACTOR_BOB = v; }

	    public BigDecimal getR63_TOTAL_AMOUNT_BOB() { return R63_TOTAL_AMOUNT_BOB; }
	    public void setR63_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R63_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR63_WITH_FACTOR_APPLIED_BOB() { return R63_WITH_FACTOR_APPLIED_BOB; }
	    public void setR63_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R63_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R64 --------
	    public BigDecimal getR64_FACTOR_BOB() { return R64_FACTOR_BOB; }
	    public void setR64_FACTOR_BOB(BigDecimal v) { this.R64_FACTOR_BOB = v; }

	    public BigDecimal getR64_TOTAL_AMOUNT_BOB() { return R64_TOTAL_AMOUNT_BOB; }
	    public void setR64_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R64_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR64_WITH_FACTOR_APPLIED_BOB() { return R64_WITH_FACTOR_APPLIED_BOB; }
	    public void setR64_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R64_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R65 --------
	    public BigDecimal getR65_FACTOR_BOB() { return R65_FACTOR_BOB; }
	    public void setR65_FACTOR_BOB(BigDecimal v) { this.R65_FACTOR_BOB = v; }

	    public BigDecimal getR65_TOTAL_AMOUNT_BOB() { return R65_TOTAL_AMOUNT_BOB; }
	    public void setR65_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R65_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR65_WITH_FACTOR_APPLIED_BOB() { return R65_WITH_FACTOR_APPLIED_BOB; }
	    public void setR65_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R65_WITH_FACTOR_APPLIED_BOB = v; }


	    // -------- R66 --------
	    public BigDecimal getR66_FACTOR_BOB() { return R66_FACTOR_BOB; }
	    public void setR66_FACTOR_BOB(BigDecimal v) { this.R66_FACTOR_BOB = v; }

	    public BigDecimal getR66_TOTAL_AMOUNT_BOB() { return R66_TOTAL_AMOUNT_BOB; }
	    public void setR66_TOTAL_AMOUNT_BOB(BigDecimal v) { this.R66_TOTAL_AMOUNT_BOB = v; }

	    public BigDecimal getR66_WITH_FACTOR_APPLIED_BOB() { return R66_WITH_FACTOR_APPLIED_BOB; }
	    public void setR66_WITH_FACTOR_APPLIED_BOB(BigDecimal v) { this.R66_WITH_FACTOR_APPLIED_BOB = v; }
	    
	    
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
	    

    public class NSFR_Detail_Entity {

        private Long sno;
        private String custId;
        private String acctNumber;
        private String acctName;
        private String dataType;
        private String reportName;
        private String reportLabel;
        private String reportAddlCriteria_1;
        private String reportRemarks;
        private String modificationRemarks;
        private String dataEntryVersion;

        private BigDecimal acctBalanceInpula;

        private Date reportDate;

        private String createUser;
        private Date createTime;

        private String modifyUser;
        private Date modifyTime;

        private String verifyUser;
        private Date verifyTime;

        private String entityFlg;
        private String modifyFlg;
        private String delFlg;

        // ================= GETTERS & SETTERS =================

        public Long getSno() { return sno; }
        public void setSno(Long sno) { this.sno = sno; }

        public String getCustId() { return custId; }
        public void setCustId(String custId) { this.custId = custId; }

        public String getAcctNumber() { return acctNumber; }
        public void setAcctNumber(String acctNumber) { this.acctNumber = acctNumber; }

        public String getAcctName() { return acctName; }
        public void setAcctName(String acctName) { this.acctName = acctName; }

        public String getDataType() { return dataType; }
        public void setDataType(String dataType) { this.dataType = dataType; }

        public String getReportName() { return reportName; }
        public void setReportName(String reportName) { this.reportName = reportName; }

        public String getReportLabel() { return reportLabel; }
        public void setReportLabel(String reportLabel) { this.reportLabel = reportLabel; }

        public String getReportAddlCriteria_1() { return reportAddlCriteria_1; }
        public void setReportAddlCriteria_1(String reportAddlCriteria_1) { this.reportAddlCriteria_1 = reportAddlCriteria_1; }

        public String getReportRemarks() { return reportRemarks; }
        public void setReportRemarks(String reportRemarks) { this.reportRemarks = reportRemarks; }

        public String getModificationRemarks() { return modificationRemarks; }
        public void setModificationRemarks(String modificationRemarks) { this.modificationRemarks = modificationRemarks; }

        public String getDataEntryVersion() { return dataEntryVersion; }
        public void setDataEntryVersion(String dataEntryVersion) { this.dataEntryVersion = dataEntryVersion; }

        public BigDecimal getAcctBalanceInpula() { return acctBalanceInpula; }
        public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) { this.acctBalanceInpula = acctBalanceInpula; }

        public Date getReportDate() { return reportDate; }
        public void setReportDate(Date reportDate) { this.reportDate = reportDate; }

        public String getCreateUser() { return createUser; }
        public void setCreateUser(String createUser) { this.createUser = createUser; }

        public Date getCreateTime() { return createTime; }
        public void setCreateTime(Date createTime) { this.createTime = createTime; }

        public String getModifyUser() { return modifyUser; }
        public void setModifyUser(String modifyUser) { this.modifyUser = modifyUser; }

        public Date getModifyTime() { return modifyTime; }
        public void setModifyTime(Date modifyTime) { this.modifyTime = modifyTime; }

        public String getVerifyUser() { return verifyUser; }
        public void setVerifyUser(String verifyUser) { this.verifyUser = verifyUser; }

        public Date getVerifyTime() { return verifyTime; }
        public void setVerifyTime(Date verifyTime) { this.verifyTime = verifyTime; }

        public String getEntityFlg() { return entityFlg; }
        public void setEntityFlg(String entityFlg) { this.entityFlg = entityFlg; }

        public String getModifyFlg() { return modifyFlg; }
        public void setModifyFlg(String modifyFlg) { this.modifyFlg = modifyFlg; }

        public String getDelFlg() { return delFlg; }
        public void setDelFlg(String delFlg) { this.delFlg = delFlg; }
    }



class NSFRDetailRowMapper implements RowMapper<NSFR_Detail_Entity> {

    @Override
    public NSFR_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

    	NSFR_Detail_Entity obj = new NSFR_Detail_Entity();

        obj.setSno(rs.getLong("SNO"));
        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));

        obj.setDataType(rs.getString("DATA_TYPE"));
        obj.setReportName(rs.getString("REPORT_NAME"));
        obj.setReportLabel(rs.getString("REPORT_LABEL"));
        obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));

        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

        obj.setReportDate(rs.getDate("REPORT_DATE"));

        obj.setCreateUser(rs.getString("CREATE_USER"));
        obj.setCreateTime(rs.getDate("CREATE_TIME"));

        obj.setModifyUser(rs.getString("MODIFY_USER"));
        obj.setModifyTime(rs.getDate("MODIFY_TIME"));

        obj.setVerifyUser(rs.getString("VERIFY_USER"));
        obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

        obj.setEntityFlg(rs.getString("ENTITY_FLG"));
        obj.setModifyFlg(rs.getString("MODIFY_FLG"));
        obj.setDelFlg(rs.getString("DEL_FLG"));

        return obj;
    }
}

class NSFRArchivalDetailRowMapper implements RowMapper<NSFR_Archival_Detail_Entity> {

    @Override
    public NSFR_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

       NSFR_Archival_Detail_Entity obj = new NSFR_Archival_Detail_Entity();

        obj.setSno(rs.getLong("SNO"));
        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));
        obj.setDataType(rs.getString("DATA_TYPE"));
        obj.setReportName(rs.getString("REPORT_NAME"));
        obj.setReportLabel(rs.getString("REPORT_LABEL"));
        obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
        obj.setReportDate(rs.getDate("REPORT_DATE"));

        obj.setCreateUser(rs.getString("CREATE_USER"));
        obj.setCreateTime(rs.getDate("CREATE_TIME"));
        obj.setModifyUser(rs.getString("MODIFY_USER"));
        obj.setModifyTime(rs.getDate("MODIFY_TIME"));
        obj.setVerifyUser(rs.getString("VERIFY_USER"));
        obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

        obj.setEntityFlg(
            rs.getString("ENTITY_FLG") != null
                ? rs.getString("ENTITY_FLG").charAt(0)
                : null);

        obj.setModifyFlg(
            rs.getString("MODIFY_FLG") != null
                ? rs.getString("MODIFY_FLG").charAt(0)
                : null);

        obj.setDelFlg(
            rs.getString("DEL_FLG") != null
                ? rs.getString("DEL_FLG").charAt(0)
                : null);

        return obj;
    }
}


public class NSFR_Archival_Detail_Entity {

    private Long sno;

    private String custId;
    private String acctNumber;
    private String acctName;
    private String dataType;
    private String reportName;

    private String reportLabel;
    private String reportAddlCriteria_1;
    private String reportRemarks;
    private String modificationRemarks;

    private String dataEntryVersion;

    private BigDecimal acctBalanceInpula;

    private Date reportDate;

    private String createUser;
    private Date createTime;

    private String modifyUser;
    private Date modifyTime;

    private String verifyUser;
    private Date verifyTime;

    private Character entityFlg;
    private Character modifyFlg;
    private Character delFlg;

    // ================= GETTERS & SETTERS =================

    public Long getSno() { return sno; }
    public void setSno(Long sno) { this.sno = sno; }

    public String getCustId() { return custId; }
    public void setCustId(String custId) { this.custId = custId; }

    public String getAcctNumber() { return acctNumber; }
    public void setAcctNumber(String acctNumber) { this.acctNumber = acctNumber; }

    public String getAcctName() { return acctName; }
    public void setAcctName(String acctName) { this.acctName = acctName; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }

    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }

    public String getReportLabel() { return reportLabel; }
    public void setReportLabel(String reportLabel) { this.reportLabel = reportLabel; }

    public String getReportAddlCriteria_1() { return reportAddlCriteria_1; }
    public void setReportAddlCriteria_1(String v) { this.reportAddlCriteria_1 = v; }

    public String getReportRemarks() { return reportRemarks; }
    public void setReportRemarks(String v) { this.reportRemarks = v; }

    public String getModificationRemarks() { return modificationRemarks; }
    public void setModificationRemarks(String v) { this.modificationRemarks = v; }

    public String getDataEntryVersion() { return dataEntryVersion; }
    public void setDataEntryVersion(String v) { this.dataEntryVersion = v; }

    public BigDecimal getAcctBalanceInpula() { return acctBalanceInpula; }
    public void setAcctBalanceInpula(BigDecimal v) { this.acctBalanceInpula = v; }

    public Date getReportDate() { return reportDate; }
    public void setReportDate(Date v) { this.reportDate = v; }

    public String getCreateUser() { return createUser; }
    public void setCreateUser(String v) { this.createUser = v; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date v) { this.createTime = v; }

    public String getModifyUser() { return modifyUser; }
    public void setModifyUser(String v) { this.modifyUser = v; }

    public Date getModifyTime() { return modifyTime; }
    public void setModifyTime(Date v) { this.modifyTime = v; }

    public String getVerifyUser() { return verifyUser; }
    public void setVerifyUser(String v) { this.verifyUser = v; }

    public Date getVerifyTime() { return verifyTime; }
    public void setVerifyTime(Date v) { this.verifyTime = v; }

    public Character getEntityFlg() { return entityFlg; }
    public void setEntityFlg(Character v) { this.entityFlg = v; }

    public Character getModifyFlg() { return modifyFlg; }
    public void setModifyFlg(Character v) { this.modifyFlg = v; }

    public Character getDelFlg() { return delFlg; }
    public void setDelFlg(Character v) { this.delFlg = v; }
}
	   
    		   
    		   
		   
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getNSFRView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type,  BigDecimal version) {
		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
//		int pageSize = pageable.getPageSize();
//		int currentPage = pageable.getPageNumber();
//		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<NSFR_Archival_Summary_Entity> T1Master = new ArrayList();
//			List<NSFR_Manual_Archival_Summary_Entity> T2Master = new ArrayList<NSFR_Manual_Archival_Summary_Entity>();

			try {
				 Date dt = dateformat.parse(todate);

				T1Master = getdatabydateListarchival(dt, version);
			 System.out.println("Archival Summary size = " + T1Master.size());
			 } catch (Exception e) {
		            e.printStackTrace();
		        }

			mv.addObject("reportsummary", T1Master);
         
		} else {
			List<NSFR_Summary_Entity> T1Master = new ArrayList<NSFR_Summary_Entity>();
					try {
						 Date dt = dateformat.parse(todate);

				
				 T1Master = getDataByDate(dt);
				

					 } catch (Exception e) {
				            e.printStackTrace();
				        }
					
			mv.addObject("reportsummary", T1Master);
           
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/NSFR");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}



	public ModelAndView getNSFRcurrentDtl(String reportId, String fromdate, String todate, String currency,
			  String dtltype, Pageable pageable, String Filter, String type, String version) {

	int pageSize = pageable != null ? pageable.getPageSize() : 10;
	int currentPage = pageable != null ? pageable.getPageNumber() : 0;
	int totalPages = 0;

	ModelAndView mv = new ModelAndView();
//	Session hs = sessionFactory.getCurrentSession();

	try {
		Date parsedDate = null;
		if (todate != null && !todate.isEmpty()) {
			parsedDate = dateformat.parse(todate);
		}

		 String reportLabel = null;
	        String reportAddlCriteria1 = null;
	        
		// ✅ Split filter string into rowId & columnId
		if (Filter != null && Filter.contains(",")) {
			String[] parts = Filter.split(",");
			if (parts.length >= 2) {
				 reportLabel = parts[0];
	                reportAddlCriteria1 = parts[1];
			}
		}
	
		if ("ARCHIVAL".equals(type) && version != null) {
			// 🔹 Archival branch
			List<NSFR_Archival_Detail_Entity> archivalDetailList;
			if (reportLabel != null && reportAddlCriteria1 != null) {
				 archivalDetailList =
	                      GetArchivalDataByRowIdAndColumnId(
	                                reportLabel,
	                                reportAddlCriteria1,
	                                parsedDate,
	                                version
	                        );
				 } else {
					  archivalDetailList =
		                       getArchivalDetaildatabydateList(
		                                parsedDate,
		                                version
		                        );
		            }
			

			mv.addObject("reportdetails", archivalDetailList);
			mv.addObject("reportmaster12", archivalDetailList);
			System.out.println("ARCHIVAL COUNT: " + (archivalDetailList != null ? archivalDetailList.size() : 0));

		} else {
			// 🔹 Current branch
			List<NSFR_Detail_Entity> currentDetailList;
			 if (reportLabel != null && reportAddlCriteria1 != null) {

				currentDetailList =   GetDetailDataByRowIdAndColumnId(
                        reportLabel,
                        reportAddlCriteria1,
                        parsedDate
                );
			} else {
				currentDetailList =
	                       getDetaildatabydateList(parsedDate);
				
				
			}

			mv.addObject("reportdetails", currentDetailList);
			mv.addObject("reportmaster12", currentDetailList);
			System.out.println("LISTCOUNT: " + (currentDetailList != null ? currentDetailList.size() : 0));
		}

	} catch (Exception e) {
		e.printStackTrace();
		mv.addObject("errorMessage", "Invalid date format: " + todate);
	} 

	// ✅ Common attributes
	mv.setViewName("BRRS/NSFR");
	mv.addObject("displaymode", "Details");
	mv.addObject("currentPage", currentPage);
	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	mv.addObject("reportsflag", "reportsflag");
	mv.addObject("menu", reportId);

	return mv;
}

	
//	public void updateReport(NSFR_Manual_Summary_Entity updatedEntity) {
//	    System.out.println("Came to services1");
//	    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//	    NSFR_Manual_Summary_Entity existing = BRRS_NSFR_Manual_Summary_Repo.findById(updatedEntity.getReport_date())
//	            .orElseThrow(() -> new RuntimeException(
//	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));
//
//	    try {
//	        // ✅ Loop for fields
//	        int[] Rows = {23,25,26,30,34,35,42,43,44,46};
//	        for (int i : Rows) {
//	            String prefix = "R" + i + "_";
//	            String[] fields = {"total_no_of_acct", "total_value"};
//
//	            for (String field : fields) {
//	                try {
//	                    String getterName = "get" + prefix + field;
//	                    String setterName = "set" + prefix + field;
//
//	                    Method getter = NSFR_Manual_Summary_Entity.class.getMethod(getterName);
//	                    Method setter = NSFR_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//	                    Object newValue = getter.invoke(updatedEntity);
//	                    setter.invoke(existing, newValue);
//
//	                } catch (NoSuchMethodException e) {
//	                    // Skip missing getter/setter gracefully
//	                    continue;
//	                }
//	            }
//	        }
//
//
//	        // ✅ Save after all updates
//	        BRRS_NSFR_Manual_Summary_Repo.save(existing);
//
//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//	}

	
	
	
	public byte[] getNSFRDetailExcel(String filename, String fromdate, String todate, String currency,
			   String dtltype, String type, String version) {
try {
logger.info("Generating Excel for NSFR Details...");
System.out.println("came to Detail download service");


if (type.equals("ARCHIVAL") & version != null) {
byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
version);
return ARCHIVALreport;
}

XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("NSFRDetail");

//Common border style
BorderStyle border = BorderStyle.THIN;

//Header style (left aligned)
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

//Right-aligned header style for ACCT BALANCE
CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

//Default data style (left aligned)
CellStyle dataStyle = workbook.createCellStyle();
dataStyle.setAlignment(HorizontalAlignment.LEFT);
dataStyle.setBorderTop(border);
dataStyle.setBorderBottom(border);
dataStyle.setBorderLeft(border);
dataStyle.setBorderRight(border);

//ACCT BALANCE style (right aligned with thousand separator)
CellStyle balanceStyle = workbook.createCellStyle();
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
balanceStyle.setBorderTop(border);
balanceStyle.setBorderBottom(border);
balanceStyle.setBorderLeft(border);
balanceStyle.setBorderRight(border);






//Header row
String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE", "REPORT ADDL CRITERIA1",
"REPORT_DATE"
};

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

//Get data
Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
List<NSFR_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (NSFR_Detail_Entity item : reportData) {
XSSFRow row = sheet.createRow(rowIndex++);

row.createCell(0).setCellValue(item.getCustId());
row.createCell(1).setCellValue(item.getAcctNumber());
row.createCell(2).setCellValue(item.getAcctName());

//ACCT BALANCE (right aligned, 3 decimal places)
Cell balanceCell = row.createCell(3);
if (item.getAcctBalanceInpula() != null) {
balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
} else {
balanceCell.setCellValue(0);
}
balanceCell.setCellStyle(balanceStyle);

		row.createCell(4).setCellValue(item.getReportLabel());
		row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
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
	logger.info("No data found for NSFR — only header will be written.");
}

//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating NSFR Excel", e);
return new byte[0];
}
}

	public byte[] getNSFRExcel(String filename, String reportId, String fromdate, String todate, String currency,
			 String dtltype, String type, BigDecimal version) throws Exception {
logger.info("Service: Starting Excel generation process in memory.");
logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

//Convert string to Date
Date reportDate = dateformat.parse(todate);

//ARCHIVAL check
if ("ARCHIVAL".equalsIgnoreCase(type)   && version != null
&& version.compareTo(BigDecimal.ZERO) >= 0) {
logger.info("Service: Generating ARCHIVAL report for version {}", version);
return getExcelNSFRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}
/*
 * //RESUB check else if ("RESUB".equalsIgnoreCase(type) && version != null &&
 * !version.trim().isEmpty()) {
 * logger.info("Service: Generating RESUB report for version {}", version);
 */





//Default (LIVE) case
List<NSFR_Summary_Entity> dataList1 = getDataByDate(dateformat.parse(todate));

System.out.println("DATA SIZE IS : "+dataList1.size());
if (dataList1.isEmpty()) {
	logger.warn("Service: No data found for  NFSR report. Returning empty result.");
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
font.setFontHeightInPoints((short)8); // size 8
font.setFontName("Arial");    

CellStyle numberStyle = workbook.createCellStyle();
//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
numberStyle.setBorderBottom(BorderStyle.THIN);
numberStyle.setBorderTop(BorderStyle.THIN);
numberStyle.setBorderLeft(BorderStyle.THIN);
numberStyle.setBorderRight(BorderStyle.THIN);
numberStyle.setFont(font);
//--- End of Style Definitions ---

int startRow = 11;

if (!dataList1.isEmpty()) {
for (int i = 0; i < dataList1.size(); i++) {

NSFR_Summary_Entity record = dataList1.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}


Cell cell1 = row.createCell(4);
if (record.getR12_TOTAL_AMOUNT_BOB() != null) {
    cell1.setCellValue(record.getR12_TOTAL_AMOUNT_BOB().doubleValue());
    cell1.setCellStyle(numberStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}

row = sheet.getRow(12);
 cell1 = row.createCell(4);
if (record.getR13_TOTAL_AMOUNT_BOB() != null) {
    cell1.setCellValue(record.getR13_TOTAL_AMOUNT_BOB().doubleValue());
    cell1.setCellStyle(numberStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}

//R13
row = sheet.getRow(12);
cell1 = row.createCell(4);
if (record.getR13_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR13_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R14
row = sheet.getRow(13);
cell1 = row.createCell(4);
if (record.getR14_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR14_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R15
row = sheet.getRow(14);
cell1 = row.createCell(4);
if (record.getR15_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR15_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R16
row = sheet.getRow(15);
cell1 = row.createCell(4);
if (record.getR16_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR16_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R17
row = sheet.getRow(16);
cell1 = row.createCell(4);
if (record.getR17_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR17_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R18
row = sheet.getRow(17);
cell1 = row.createCell(4);
if (record.getR18_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR18_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R19
row = sheet.getRow(18);
cell1 = row.createCell(4);
if (record.getR19_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR19_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R20
row = sheet.getRow(19);
cell1 = row.createCell(4);
if (record.getR20_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR20_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R21
row = sheet.getRow(20);
cell1 = row.createCell(4);
if (record.getR21_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR21_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R22
row = sheet.getRow(21);
cell1 = row.createCell(4);
if (record.getR22_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR22_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R26
row = sheet.getRow(25);
cell1 = row.createCell(4);
if (record.getR26_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR26_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R27
row = sheet.getRow(26);
cell1 = row.createCell(4);
if (record.getR27_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR27_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R28
row = sheet.getRow(27);
cell1 = row.createCell(4);
if (record.getR28_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR28_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R29
row = sheet.getRow(28);
cell1 = row.createCell(4);
if (record.getR29_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR29_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R30
row = sheet.getRow(29);
cell1 = row.createCell(4);
if (record.getR30_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR30_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R31
row = sheet.getRow(30);
cell1 = row.createCell(4);
if (record.getR31_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR31_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R32
row = sheet.getRow(31);
cell1 = row.createCell(4);
if (record.getR32_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR32_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R33
row = sheet.getRow(32);
cell1 = row.createCell(4);
if (record.getR33_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR33_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R34
row = sheet.getRow(33);
cell1 = row.createCell(4);
if (record.getR34_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR34_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R35
row = sheet.getRow(34);
cell1 = row.createCell(4);
if (record.getR35_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR35_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R36
row = sheet.getRow(35);
cell1 = row.createCell(4);
if (record.getR36_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR36_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R37
row = sheet.getRow(36);
cell1 = row.createCell(4);
if (record.getR37_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR37_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R38
row = sheet.getRow(37);
cell1 = row.createCell(4);
if (record.getR38_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR38_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R39
row = sheet.getRow(38);
cell1 = row.createCell(4);
if (record.getR39_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR39_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R40
row = sheet.getRow(39);
cell1 = row.createCell(4);
if (record.getR40_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR40_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R41
row = sheet.getRow(40);
cell1 = row.createCell(4);
if (record.getR41_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR41_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R42
row = sheet.getRow(41);
cell1 = row.createCell(4);
if (record.getR42_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR42_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R43
row = sheet.getRow(42);
cell1 = row.createCell(4);
if (record.getR43_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR43_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R44
row = sheet.getRow(43);
cell1 = row.createCell(4);
if (record.getR44_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR44_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R45
row = sheet.getRow(44);
cell1 = row.createCell(4);
if (record.getR45_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR45_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R46
row = sheet.getRow(45);
cell1 = row.createCell(4);
if (record.getR46_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR46_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R47
row = sheet.getRow(46);
cell1 = row.createCell(4);
if (record.getR47_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR47_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R49
row = sheet.getRow(48);
cell1 = row.createCell(4);
if (record.getR49_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR49_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R50
row = sheet.getRow(49);
cell1 = row.createCell(4);
if (record.getR50_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR50_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R51
row = sheet.getRow(50);
cell1 = row.createCell(4);
if (record.getR51_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR51_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R52
row = sheet.getRow(51);
cell1 = row.createCell(4);
if (record.getR52_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR52_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R53
row = sheet.getRow(52);
cell1 = row.createCell(4);
if (record.getR53_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR53_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R54
row = sheet.getRow(53);
cell1 = row.createCell(4);
if (record.getR54_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR54_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R55
row = sheet.getRow(54);
cell1 = row.createCell(4);
if (record.getR55_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR55_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R56
row = sheet.getRow(55);
cell1 = row.createCell(4);
if (record.getR56_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR56_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R57
row = sheet.getRow(56);
cell1 = row.createCell(4);
if (record.getR57_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR57_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R58
row = sheet.getRow(57);
cell1 = row.createCell(4);
if (record.getR58_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR58_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R59
row = sheet.getRow(58);
cell1 = row.createCell(4);
if (record.getR59_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR59_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R60
row = sheet.getRow(59);
cell1 = row.createCell(4);
if (record.getR60_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR60_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
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

	public List<Object[]> getNSFRArchival() {

	    List<Object[]> archivalList = new ArrayList<>();

	    try {

	        // Fetch data from repo
	        List<NSFR_Archival_Summary_Entity> repoData =
	        		getdatabydateListWithVersion();

	        if (repoData != null && !repoData.isEmpty()) {

	            for (NSFR_Archival_Summary_Entity entity : repoData) {

	                Object[] row = new Object[] {
	                        entity.getReport_date(),
	                        entity.getReport_version(),
	                        entity.getReport_frequency(),
	                        entity.getReport_code(),
	                        entity.getReport_desc()
	                        // 👉 add more fields if needed
	                };

	                archivalList.add(row);
	            }

	            System.out.println("Fetched " + archivalList.size() + " NSFR archival records");

	            NSFR_Archival_Summary_Entity first = repoData.get(0);
	            System.out.println("Latest NSFR version: " + first.getReport_version());

	        } else {
	            System.out.println("No NSFR archival data found.");
	        }

	    } catch (Exception e) {
	        System.err.println("Error fetching NSFR Archival data: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return archivalList;
	}
	
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) {
try {
logger.info("Generating Excel for BRRS_NSFR ARCHIVAL Details...");
System.out.println("came to Detail download service");
if (type.equals("ARCHIVAL") & version != null) {

}
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("NSFRDetail");

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


// Header row
String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE", "REPORT ADDL CRITERIA", "REPORT_DATE"
};

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
List<NSFR_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate,version);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (NSFR_Archival_Detail_Entity item : reportData) {
XSSFRow row = sheet.createRow(rowIndex++);

row.createCell(0).setCellValue(item.getCustId());
row.createCell(1).setCellValue(item.getAcctNumber());
row.createCell(2).setCellValue(item.getAcctName());

// ACCT BALANCE (right aligned, 3 decimal places with comma separator)
Cell balanceCell = row.createCell(3);

if (item.getAcctBalanceInpula() != null) {
balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
} else {
balanceCell.setCellValue(0);
}

/*
 * // Create style with thousand separator and decimal point DataFormat format =
 * workbook.createDataFormat();
 * 
 * // Format: 1,234,567 balanceStyle.setDataFormat(format.getFormat("#,##0"));
 * 
 * // Right alignment (optional)
 * balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
 * 
 * balanceCell.setCellStyle(balanceStyle);
 */

row.createCell(4).setCellValue(item.getReportLabel());
row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
row.createCell(6).setCellValue(
item.getReportDate() != null ?
new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : ""
);

// Apply data style for all other cells
for (int j = 0; j < 7; j++) {
if (j != 3) {
row.getCell(j).setCellStyle(dataStyle);
}
}
}
} else {
logger.info("No data found for NSFR — only header will be written.");
}
// Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating NSFR Excel", e);
return new byte[0];
}
}

	public byte[] getExcelNSFRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<NSFR_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate), version);
		

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for ADISB1 report. Returning empty result.");
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
			// --- End of Style Definitions --
			int startRow = 11;
			
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

				NSFR_Archival_Summary_Entity record = dataList.get(i);
				System.out.println("rownumber=" + startRow + i);
				Row row = sheet.getRow(startRow + i);
				if (row == null) {
				row = sheet.createRow(startRow + i);
				}
				

Cell cell1 = row.createCell(4);
if (record.getR12_TOTAL_AMOUNT_BOB() != null) {
    cell1.setCellValue(record.getR12_TOTAL_AMOUNT_BOB().doubleValue());
    cell1.setCellStyle(numberStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}

row = sheet.getRow(12);
 cell1 = row.createCell(4);
if (record.getR13_TOTAL_AMOUNT_BOB() != null) {
    cell1.setCellValue(record.getR13_TOTAL_AMOUNT_BOB().doubleValue());
    cell1.setCellStyle(numberStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}

//R13
row = sheet.getRow(12);
cell1 = row.createCell(4);
if (record.getR13_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR13_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R14
row = sheet.getRow(13);
cell1 = row.createCell(4);
if (record.getR14_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR14_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R15
row = sheet.getRow(14);
cell1 = row.createCell(4);
if (record.getR15_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR15_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R16
row = sheet.getRow(15);
cell1 = row.createCell(4);
if (record.getR16_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR16_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R17
row = sheet.getRow(16);
cell1 = row.createCell(4);
if (record.getR17_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR17_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R18
row = sheet.getRow(17);
cell1 = row.createCell(4);
if (record.getR18_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR18_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R19
row = sheet.getRow(18);
cell1 = row.createCell(4);
if (record.getR19_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR19_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R20
row = sheet.getRow(19);
cell1 = row.createCell(4);
if (record.getR20_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR20_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R21
row = sheet.getRow(20);
cell1 = row.createCell(4);
if (record.getR21_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR21_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R22
row = sheet.getRow(21);
cell1 = row.createCell(4);
if (record.getR22_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR22_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R26
row = sheet.getRow(25);
cell1 = row.createCell(4);
if (record.getR26_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR26_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R27
row = sheet.getRow(26);
cell1 = row.createCell(4);
if (record.getR27_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR27_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R28
row = sheet.getRow(27);
cell1 = row.createCell(4);
if (record.getR28_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR28_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R29
row = sheet.getRow(28);
cell1 = row.createCell(4);
if (record.getR29_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR29_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R30
row = sheet.getRow(29);
cell1 = row.createCell(4);
if (record.getR30_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR30_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R31
row = sheet.getRow(30);
cell1 = row.createCell(4);
if (record.getR31_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR31_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R32
row = sheet.getRow(31);
cell1 = row.createCell(4);
if (record.getR32_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR32_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R33
row = sheet.getRow(32);
cell1 = row.createCell(4);
if (record.getR33_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR33_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R34
row = sheet.getRow(33);
cell1 = row.createCell(4);
if (record.getR34_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR34_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R35
row = sheet.getRow(34);
cell1 = row.createCell(4);
if (record.getR35_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR35_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R36
row = sheet.getRow(35);
cell1 = row.createCell(4);
if (record.getR36_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR36_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R37
row = sheet.getRow(36);
cell1 = row.createCell(4);
if (record.getR37_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR37_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R38
row = sheet.getRow(37);
cell1 = row.createCell(4);
if (record.getR38_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR38_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R39
row = sheet.getRow(38);
cell1 = row.createCell(4);
if (record.getR39_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR39_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R40
row = sheet.getRow(39);
cell1 = row.createCell(4);
if (record.getR40_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR40_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R41
row = sheet.getRow(40);
cell1 = row.createCell(4);
if (record.getR41_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR41_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R42
row = sheet.getRow(41);
cell1 = row.createCell(4);
if (record.getR42_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR42_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R43
row = sheet.getRow(42);
cell1 = row.createCell(4);
if (record.getR43_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR43_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R44
row = sheet.getRow(43);
cell1 = row.createCell(4);
if (record.getR44_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR44_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R45
row = sheet.getRow(44);
cell1 = row.createCell(4);
if (record.getR45_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR45_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R46
row = sheet.getRow(45);
cell1 = row.createCell(4);
if (record.getR46_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR46_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R47
row = sheet.getRow(46);
cell1 = row.createCell(4);
if (record.getR47_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR47_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R49
row = sheet.getRow(48);
cell1 = row.createCell(4);
if (record.getR49_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR49_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R50
row = sheet.getRow(49);
cell1 = row.createCell(4);
if (record.getR50_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR50_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R51
row = sheet.getRow(50);
cell1 = row.createCell(4);
if (record.getR51_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR51_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R52
row = sheet.getRow(51);
cell1 = row.createCell(4);
if (record.getR52_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR52_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R53
row = sheet.getRow(52);
cell1 = row.createCell(4);
if (record.getR53_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR53_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R54
row = sheet.getRow(53);
cell1 = row.createCell(4);
if (record.getR54_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR54_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R55
row = sheet.getRow(54);
cell1 = row.createCell(4);
if (record.getR55_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR55_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R56
row = sheet.getRow(55);
cell1 = row.createCell(4);
if (record.getR56_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR56_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R57
row = sheet.getRow(56);
cell1 = row.createCell(4);
if (record.getR57_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR57_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R58
row = sheet.getRow(57);
cell1 = row.createCell(4);
if (record.getR58_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR58_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R59
row = sheet.getRow(58);
cell1 = row.createCell(4);
if (record.getR59_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR59_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//R60
row = sheet.getRow(59);
cell1 = row.createCell(4);
if (record.getR60_TOTAL_AMOUNT_BOB() != null) {
 cell1.setCellValue(record.getR60_TOTAL_AMOUNT_BOB().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
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



	public ModelAndView getViewOrEditPage(String SNO, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/NSFR"); // ✅ match the report name
	    System.out.println("Hello");
	    if (SNO != null) {
	    	NSFR_Detail_Entity NSFREntity = findBySno(SNO);
	        if (NSFREntity != null && NSFREntity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(NSFREntity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", NSFREntity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}





	public ModelAndView updateDetailEdit(String SNO, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/NSFR"); // ✅ match the report name

	    if (SNO != null) {
	        NSFR_Detail_Entity nsfrEntity =  findBySno(SNO);
	        if (nsfrEntity != null && nsfrEntity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(nsfrEntity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	            System.out.println(formattedDate);
	        }
	        mv.addObject("Data", nsfrEntity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
	    try {
	        String acctNo = request.getParameter("acctNumber");
	        String Sno = request.getParameter("sno");
	        String provisionStr = request.getParameter("acctBalanceInpula");
	        String acctName = request.getParameter("acctName");
	        String reportDateStr = request.getParameter("reportDate");

	        logger.info("Received update for ACCT_NO: {}", acctNo);

	        NSFR_Detail_Entity existing =  findBySno(Sno);
	        if (existing == null) {
	            logger.warn("No record found for ACCT_NO: {}", acctNo);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
	        }

	        boolean isChanged = false;

	        if (acctName != null && !acctName.isEmpty()) {
	            if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
	                existing.setAcctName(acctName);
	                isChanged = true;
	                logger.info("Account name updated to {}", acctName);
	            }
	        }

	        if (provisionStr != null && !provisionStr.isEmpty()) {
	            BigDecimal newProvision = new BigDecimal(provisionStr);
	            if (existing.getAcctBalanceInpula() == null ||
	                existing.getAcctBalanceInpula().compareTo(newProvision) != 0) {
	                existing.setAcctBalanceInpula(newProvision);
	                isChanged = true;
	                logger.info("Balance updated to {}", newProvision);
	            }
	        }
	        
	        

	        /*if (isChanged) {
	        	BRRS_NSFR_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);
*/
	        if (isChanged) {

	            String sql =
	                "UPDATE BRRS_NSFR_DETAILTABLE " +
	                "SET ACCT_NAME = ?, " +
	                "ACCT_BALANCE_IN_PULA = ? " +
	                "WHERE SNO = ?";

	            jdbcTemplate.update(
	                    sql,
	                    existing.getAcctName(),
	                    existing.getAcctBalanceInpula(),
	                    existing.getSno()
	            );

	            System.out.println(
	                    "Record updated using JDBC");
	        
	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_NSFR_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_NSFR_SUMMARY_PROCEDURE(?); END;", formattedDate);
	                        logger.info("Procedure executed successfully after commit.");
	                    } catch (Exception e) {
	                        logger.error("Error executing procedure after commit", e);
	                    }
	                }
	            });

	            return ResponseEntity.ok("Record updated successfully!");
	        } else {
	            logger.info("No changes detected for ACCT_NO: {}", acctNo);
	            return ResponseEntity.ok("No changes were made.");
	        }

	    } catch (Exception e) {
	        logger.error("Error updating NSFR record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}
	
	
	
	
}