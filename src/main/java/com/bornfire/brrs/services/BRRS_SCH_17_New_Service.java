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
public class BRRS_SCH_17_New_Service {
	
	private static final Logger logger = LoggerFactory.getLogger(BRRS_SCH_17_New_Service.class);
	
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
    public List<SCH_17_Summary_Entity1> getDataByDate(Date reportDate) {

        String sql = "SELECT * FROM BRRS_SCH_17_SUMMARYTABLE WHERE REPORT_DATE = ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{reportDate},
                new SCH17RowMapper()
        );
    }
    
    public List<SCH_17_Manual_Summary_Entity1> getManualDataByDate(Date reportDate) {

        String sql =
            "SELECT * FROM BRRS_SCH_17_MANUAL_SUMMARYTABLE WHERE REPORT_DATE = ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{reportDate},
                new SCH17ManualRowMapper()
        );
    }
    
// // =========================================================
// // GET ALL ARCHIVAL REPORT DATE + VERSION (JDBC)
// // =========================================================
//
//    public List<Object[]> getSCH_17_Newarchival() {
//
//        String sql =
//            "SELECT REPORT_DATE, REPORT_VERSION " +
//            "FROM BRRS_SCH_17_ARCHIVALTABLE_SUMMARY " +
//            "ORDER BY REPORT_VERSION";
//
//        return jdbcTemplate.query(sql,
//            (rs, rowNum) -> new Object[] {
//                rs.getDate("REPORT_DATE"),
//                rs.getBigDecimal("REPORT_VERSION")
//            }
//        );
//    }
// 
////=========================================================
////GET ARCHIVAL FULL DATA BY DATE + VERSION (JDBC)
////=========================================================
//
//public List<SCH_17_Archival_Summary_Entity1> getdatabydateListarchival(
//      Date reportDate,
//      BigDecimal reportVersion) {
//
//  String sql =
//      "SELECT * FROM BRRS_SCH_17_ARCHIVALTABLE_SUMMARY " +
//      "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
//
//  return jdbcTemplate.query(
//          sql,
//          new Object[]{reportDate, reportVersion},
//          new SCH17ArchivalRowMapper()
//  );
//}
    
    
 // =========================================================
 // GET REPORT_DATE + REPORT_VERSION
 // =========================================================

 public List<Object[]> getSCH_17_Newarchival() {

     String sql =
         "SELECT REPORT_DATE, REPORT_VERSION " +
         "FROM BRRS_SCH_17_ARCHIVALTABLE_SUMMARY " +
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
public List<SCH_17_Archival_Summary_Entity1>
getdatabydateListarchival(
      Date reportDate,
      BigDecimal reportVersion) {

  String sql =
      "SELECT * FROM BRRS_SCH_17_ARCHIVALTABLE_SUMMARY " +
      "WHERE REPORT_DATE = ? " +
      "AND REPORT_VERSION = ?";

  return jdbcTemplate.query(
          sql,
          new Object[]{
                  reportDate,
                  reportVersion
          },
          new SCH17ArchivalRowMapper()
  );
}
//=========================================================
//GET ALL WITH VERSION
//=========================================================

public List<SCH_17_Archival_Summary_Entity1>
getdatabydateListWithVersion() {

 String sql =
     "SELECT * FROM BRRS_SCH_17_ARCHIVALTABLE_SUMMARY " +
     "WHERE REPORT_VERSION IS NOT NULL " +
     "ORDER BY REPORT_VERSION ASC";

 return jdbcTemplate.query(
         sql,
         new SCH17ArchivalRowMapper()
 );
}


//=========================================================
//GET MAX VERSION BY DATE
//=========================================================

public BigDecimal findMaxVersion(Date reportDate) {

 String sql =
     "SELECT MAX(REPORT_VERSION) " +
     "FROM BRRS_SCH_17_ARCHIVALTABLE_SUMMARY " +
     "WHERE REPORT_DATE = ?";

 return jdbcTemplate.queryForObject(
         sql,
         new Object[]{reportDate},
         BigDecimal.class
 );
}

public List<Object[]> getSCH_17ManualArchivalList() {

    String sql =
        "SELECT REPORT_DATE, REPORT_VERSION " +
        "FROM BRRS_SCH_17_MANUAL_ARCHIVALTABLE_SUMMARY " +
        "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(sql,
        (rs, rowNum) -> new Object[]{
            rs.getDate("REPORT_DATE"),
            rs.getBigDecimal("REPORT_VERSION")
        }
    );
}

public List<SCH_17_Archival_Manual_Summary_Entity1> getManualArchivalByDate(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql =
        "SELECT * FROM BRRS_SCH_17_MANUAL_ARCHIVALTABLE_SUMMARY " +
        "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new SCH17ManualArchivalRowMapper()
    );
}
    

// =========================================================
// 1. BY DATE + LABEL + CRITERIA
// =========================================================
public List<SCH_17_Detail_Entity1> findByDetailReportDateAndLabelAndCriteria(
        Date reportDate,
        String reportLabel,
        String reportAddlCriteria1) {

    String sql =
        "SELECT * FROM BRRS_SCH_17_DETAILTABLE " +
        "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportLabel, reportAddlCriteria1},
            new SCH17DetailRowMapper()
    );
}

// =========================================================
// 2. GET ALL (BY DATE - simple)
// =========================================================
public List<SCH_17_Detail_Entity1> getDetaildatabydateList(Date reportdate) {

    String sql = "SELECT * FROM BRRS_SCH_17_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportdate},
            new SCH17DetailRowMapper()
    );
}

// =========================================================
// 3. PAGINATION
// =========================================================
public List<SCH_17_Detail_Entity1> getDetaildatabydateList(
        Date reportdate, int offset, int limit) {

    String sql =
        "SELECT * FROM BRRS_SCH_17_DETAILTABLE " +
        "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportdate, offset, limit},
            new SCH17DetailRowMapper()
    );
}

// =========================================================
// 4. COUNT
// =========================================================
public int getDetaildatacount(Date reportdate) {

    String sql =
        "SELECT COUNT(*) FROM BRRS_SCH_17_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportdate},
            Integer.class
    );
}

// =========================================================
// 5. BY LABEL + CRITERIA
// =========================================================
public List<SCH_17_Detail_Entity1> GetDetailDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportdate) {

    String sql =
        "SELECT * FROM BRRS_SCH_17_DETAILTABLE " +
        "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportdate},
            new SCH17DetailRowMapper()
    );
}

// =========================================================
// 6. BY ACCOUNT NUMBER
// =========================================================
public SCH_17_Detail_Entity1 findByAcctnumber(String acct_number) {

    String sql =
        "SELECT * FROM BRRS_SCH_17_DETAILTABLE WHERE ACCT_NUMBER = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{acct_number},
            new SCH17DetailRowMapper()
    );
}

// =========================================================
// 7. BY SNO
// =========================================================
public SCH_17_Detail_Entity1 findBySno(String sno) {

    String sql =
        "SELECT * FROM BRRS_SCH_17_DETAILTABLE WHERE SNO = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{sno},
            new SCH17DetailRowMapper()
    );
}


// =========================================================
// 1. GET BY DATE + VERSION
// =========================================================
public List<SCH_17_Archival_Detail_Entity1> getArchivalDetaildatabydateList(
        Date reportdate,
        String dataEntryVersion) {

    String sql =
        "SELECT * FROM BRRS_SCH_17_ARCHIVALTABLE_DETAIL " +
        "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportdate, dataEntryVersion},
            new SCH17ArchivalDetailRowMapper()
    );
}

// =========================================================
// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION
// =========================================================
public List<SCH_17_Archival_Detail_Entity1> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportdate,
        String dataEntryVersion) {

    String sql =
        "SELECT * FROM BRRS_SCH_17_ARCHIVALTABLE_DETAIL " +
        "WHERE REPORT_LABEL = ? " +
        "AND REPORT_ADDL_CRITERIA_1 = ? " +
        "AND REPORT_DATE = ? " +
        "AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion},
            new SCH17ArchivalDetailRowMapper()
    );
}

    // =========================================================
    // ROW MAPPER
    // =========================================================
    class SCH17RowMapper implements RowMapper<SCH_17_Summary_Entity1> {

        @Override	
        public SCH_17_Summary_Entity1 mapRow(ResultSet rs, int rowNum)
                throws SQLException {

        	SCH_17_Summary_Entity1 obj = new SCH_17_Summary_Entity1();

            // =========================
            // R9
            // =========================
            obj.setR9_product(rs.getString("r9_product"));
            obj.setR9_31_3_25_amt(rs.getBigDecimal("r9_31_3_25_amt"));
            obj.setR9_30_9_25_amt(rs.getBigDecimal("r9_30_9_25_amt"));

            // =========================
            // R10
            // =========================
            obj.setR10_product(rs.getString("r10_product"));
            obj.setR10_31_3_25_amt(rs.getBigDecimal("r10_31_3_25_amt"));
            obj.setR10_30_9_25_amt(rs.getBigDecimal("r10_30_9_25_amt"));

            // =========================
            // R11
            // =========================
            obj.setR11_product(rs.getString("r11_product"));
            obj.setR11_31_3_25_amt(rs.getBigDecimal("r11_31_3_25_amt"));
            obj.setR11_30_9_25_amt(rs.getBigDecimal("r11_30_9_25_amt"));

            // =========================
            // R12
            // =========================
            obj.setR12_product(rs.getString("r12_product"));
            obj.setR12_31_3_25_amt(rs.getBigDecimal("r12_31_3_25_amt"));
            obj.setR12_30_9_25_amt(rs.getBigDecimal("r12_30_9_25_amt"));

            // =========================
            // R13
            // =========================
            obj.setR13_product(rs.getString("r13_product"));
            obj.setR13_31_3_25_amt(rs.getBigDecimal("r13_31_3_25_amt"));
            obj.setR13_30_9_25_amt(rs.getBigDecimal("r13_30_9_25_amt"));

            // =========================
            // R14
            // =========================
            obj.setR14_product(rs.getString("r14_product"));
            obj.setR14_31_3_25_amt(rs.getBigDecimal("r14_31_3_25_amt"));
            obj.setR14_30_9_25_amt(rs.getBigDecimal("r14_30_9_25_amt"));

            // =========================
            // R15
            // =========================
            obj.setR15_product(rs.getString("r15_product"));
            obj.setR15_31_3_25_amt(rs.getBigDecimal("r15_31_3_25_amt"));
            obj.setR15_30_9_25_amt(rs.getBigDecimal("r15_30_9_25_amt"));

            // =========================
            // R16
            // =========================
            obj.setR16_product(rs.getString("r16_product"));
            obj.setR16_31_3_25_amt(rs.getBigDecimal("r16_31_3_25_amt"));
            obj.setR16_30_9_25_amt(rs.getBigDecimal("r16_30_9_25_amt"));

            // =========================
            // R17
            // =========================
            obj.setR17_product(rs.getString("r17_product"));
            obj.setR17_31_3_25_amt(rs.getBigDecimal("r17_31_3_25_amt"));
            obj.setR17_30_9_25_amt(rs.getBigDecimal("r17_30_9_25_amt"));

            // =========================
            // R18
            // =========================
            obj.setR18_product(rs.getString("r18_product"));
            obj.setR18_31_3_25_amt(rs.getBigDecimal("r18_31_3_25_amt"));
            obj.setR18_30_9_25_amt(rs.getBigDecimal("r18_30_9_25_amt"));

            // =========================
            // R19
            // =========================
            obj.setR19_product(rs.getString("r19_product"));
            obj.setR19_31_3_25_amt(rs.getBigDecimal("r19_31_3_25_amt"));
            obj.setR19_30_9_25_amt(rs.getBigDecimal("r19_30_9_25_amt"));

            // =========================
            // R20
            // =========================
            obj.setR20_product(rs.getString("r20_product"));
            obj.setR20_31_3_25_amt(rs.getBigDecimal("r20_31_3_25_amt"));
            obj.setR20_30_9_25_amt(rs.getBigDecimal("r20_30_9_25_amt"));

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



public static class SCH_17_Summary_Entity1 {

    // ================= R9 =================
    private String r9_product;
    private BigDecimal r9_31_3_25_amt;
    private BigDecimal r9_30_9_25_amt;

    // ================= R10 =================
    private String r10_product;
    private BigDecimal r10_31_3_25_amt;
    private BigDecimal r10_30_9_25_amt;

    // ================= R11 =================
    private String r11_product;
    private BigDecimal r11_31_3_25_amt;
    private BigDecimal r11_30_9_25_amt;

    // ================= R12 =================
    private String r12_product;
    private BigDecimal r12_31_3_25_amt;
    private BigDecimal r12_30_9_25_amt;

    // ================= R13 =================
    private String r13_product;
    private BigDecimal r13_31_3_25_amt;
    private BigDecimal r13_30_9_25_amt;

    // ================= R14 =================
    private String r14_product;
    private BigDecimal r14_31_3_25_amt;
    private BigDecimal r14_30_9_25_amt;

    // ================= R15 =================
    private String r15_product;
    private BigDecimal r15_31_3_25_amt;
    private BigDecimal r15_30_9_25_amt;

    // ================= R16 =================
    private String r16_product;
    private BigDecimal r16_31_3_25_amt;
    private BigDecimal r16_30_9_25_amt;

    // ================= R17 =================
    private String r17_product;
    private BigDecimal r17_31_3_25_amt;
    private BigDecimal r17_30_9_25_amt;

    // ================= R18 =================
    private String r18_product;
    private BigDecimal r18_31_3_25_amt;
    private BigDecimal r18_30_9_25_amt;

    // ================= R19 =================
    private String r19_product;
    private BigDecimal r19_31_3_25_amt;
    private BigDecimal r19_30_9_25_amt;

    // ================= R20 =================
    private String r20_product;
    private BigDecimal r20_31_3_25_amt;
    private BigDecimal r20_30_9_25_amt;

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

    // -------- R9 --------
    public String getR9_product() { return r9_product; }
    public void setR9_product(String v) { this.r9_product = v; }

    public BigDecimal getR9_31_3_25_amt() { return r9_31_3_25_amt; }
    public void setR9_31_3_25_amt(BigDecimal v) { this.r9_31_3_25_amt = v; }

    public BigDecimal getR9_30_9_25_amt() { return r9_30_9_25_amt; }
    public void setR9_30_9_25_amt(BigDecimal v) { this.r9_30_9_25_amt = v; }

    // -------- R10 --------
    public String getR10_product() { return r10_product; }
    public void setR10_product(String v) { this.r10_product = v; }

    public BigDecimal getR10_31_3_25_amt() { return r10_31_3_25_amt; }
    public void setR10_31_3_25_amt(BigDecimal v) { this.r10_31_3_25_amt = v; }

    public BigDecimal getR10_30_9_25_amt() { return r10_30_9_25_amt; }
    public void setR10_30_9_25_amt(BigDecimal v) { this.r10_30_9_25_amt = v; }

    // -------- R11 --------
    public String getR11_product() { return r11_product; }
    public void setR11_product(String v) { this.r11_product = v; }

    public BigDecimal getR11_31_3_25_amt() { return r11_31_3_25_amt; }
    public void setR11_31_3_25_amt(BigDecimal v) { this.r11_31_3_25_amt = v; }

    public BigDecimal getR11_30_9_25_amt() { return r11_30_9_25_amt; }
    public void setR11_30_9_25_amt(BigDecimal v) { this.r11_30_9_25_amt = v; }

    // -------- R12 --------
    public String getR12_product() { return r12_product; }
    public void setR12_product(String v) { this.r12_product = v; }

    public BigDecimal getR12_31_3_25_amt() { return r12_31_3_25_amt; }
    public void setR12_31_3_25_amt(BigDecimal v) { this.r12_31_3_25_amt = v; }

    public BigDecimal getR12_30_9_25_amt() { return r12_30_9_25_amt; }
    public void setR12_30_9_25_amt(BigDecimal v) { this.r12_30_9_25_amt = v; }

    // -------- R13 --------
    public String getR13_product() { return r13_product; }
    public void setR13_product(String v) { this.r13_product = v; }

    public BigDecimal getR13_31_3_25_amt() { return r13_31_3_25_amt; }
    public void setR13_31_3_25_amt(BigDecimal v) { this.r13_31_3_25_amt = v; }

    public BigDecimal getR13_30_9_25_amt() { return r13_30_9_25_amt; }
    public void setR13_30_9_25_amt(BigDecimal v) { this.r13_30_9_25_amt = v; }

    // -------- R14 --------
    public String getR14_product() { return r14_product; }
    public void setR14_product(String v) { this.r14_product = v; }

    public BigDecimal getR14_31_3_25_amt() { return r14_31_3_25_amt; }
    public void setR14_31_3_25_amt(BigDecimal v) { this.r14_31_3_25_amt = v; }

    public BigDecimal getR14_30_9_25_amt() { return r14_30_9_25_amt; }
    public void setR14_30_9_25_amt(BigDecimal v) { this.r14_30_9_25_amt = v; }

    // -------- R15 --------
    public String getR15_product() { return r15_product; }
    public void setR15_product(String v) { this.r15_product = v; }

    public BigDecimal getR15_31_3_25_amt() { return r15_31_3_25_amt; }
    public void setR15_31_3_25_amt(BigDecimal v) { this.r15_31_3_25_amt = v; }

    public BigDecimal getR15_30_9_25_amt() { return r15_30_9_25_amt; }
    public void setR15_30_9_25_amt(BigDecimal v) { this.r15_30_9_25_amt = v; }

    // -------- R16 --------
    public String getR16_product() { return r16_product; }
    public void setR16_product(String v) { this.r16_product = v; }

    public BigDecimal getR16_31_3_25_amt() { return r16_31_3_25_amt; }
    public void setR16_31_3_25_amt(BigDecimal v) { this.r16_31_3_25_amt = v; }

    public BigDecimal getR16_30_9_25_amt() { return r16_30_9_25_amt; }
    public void setR16_30_9_25_amt(BigDecimal v) { this.r16_30_9_25_amt = v; }

    // -------- R17 --------
    public String getR17_product() { return r17_product; }
    public void setR17_product(String v) { this.r17_product = v; }

    public BigDecimal getR17_31_3_25_amt() { return r17_31_3_25_amt; }
    public void setR17_31_3_25_amt(BigDecimal v) { this.r17_31_3_25_amt = v; }

    public BigDecimal getR17_30_9_25_amt() { return r17_30_9_25_amt; }
    public void setR17_30_9_25_amt(BigDecimal v) { this.r17_30_9_25_amt = v; }

    // -------- R18 --------
    public String getR18_product() { return r18_product; }
    public void setR18_product(String v) { this.r18_product = v; }

    public BigDecimal getR18_31_3_25_amt() { return r18_31_3_25_amt; }
    public void setR18_31_3_25_amt(BigDecimal v) { this.r18_31_3_25_amt = v; }

    public BigDecimal getR18_30_9_25_amt() { return r18_30_9_25_amt; }
    public void setR18_30_9_25_amt(BigDecimal v) { this.r18_30_9_25_amt = v; }

    // -------- R19 --------
    public String getR19_product() { return r19_product; }
    public void setR19_product(String v) { this.r19_product = v; }

    public BigDecimal getR19_31_3_25_amt() { return r19_31_3_25_amt; }
    public void setR19_31_3_25_amt(BigDecimal v) { this.r19_31_3_25_amt = v; }

    public BigDecimal getR19_30_9_25_amt() { return r19_30_9_25_amt; }
    public void setR19_30_9_25_amt(BigDecimal v) { this.r19_30_9_25_amt = v; }

    // -------- R20 --------
    public String getR20_product() { return r20_product; }
    public void setR20_product(String v) { this.r20_product = v; }

    public BigDecimal getR20_31_3_25_amt() { return r20_31_3_25_amt; }
    public void setR20_31_3_25_amt(BigDecimal v) { this.r20_31_3_25_amt = v; }

    public BigDecimal getR20_30_9_25_amt() { return r20_30_9_25_amt; }
    public void setR20_30_9_25_amt(BigDecimal v) { this.r20_30_9_25_amt = v; }

    // -------- COMMON --------
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



class SCH17ManualRowMapper implements RowMapper<SCH_17_Manual_Summary_Entity1> {

    @Override
    public SCH_17_Manual_Summary_Entity1 mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        SCH_17_Manual_Summary_Entity1 obj =
                new SCH_17_Manual_Summary_Entity1();

        // ================= R10 =================
        obj.setR10_product(rs.getString("r10_product"));
        obj.setR10_31_3_25_amt(rs.getBigDecimal("r10_31_3_25_amt"));
        obj.setR10_30_9_25_amt(rs.getBigDecimal("r10_30_9_25_amt"));

        // ================= R11 =================
        obj.setR11_product(rs.getString("r11_product"));
        obj.setR11_31_3_25_amt(rs.getBigDecimal("r11_31_3_25_amt"));
        obj.setR11_30_9_25_amt(rs.getBigDecimal("r11_30_9_25_amt"));

        // ================= R12 =================
        obj.setR12_product(rs.getString("r12_product"));
        obj.setR12_31_3_25_amt(rs.getBigDecimal("r12_31_3_25_amt"));
        obj.setR12_30_9_25_amt(rs.getBigDecimal("r12_30_9_25_amt"));

        // ================= R14 =================
        obj.setR14_product(rs.getString("r14_product"));
        obj.setR14_31_3_25_amt(rs.getBigDecimal("r14_31_3_25_amt"));
        obj.setR14_30_9_25_amt(rs.getBigDecimal("r14_30_9_25_amt"));

        // ================= R15 =================
        obj.setR15_product(rs.getString("r15_product"));
        obj.setR15_31_3_25_amt(rs.getBigDecimal("r15_31_3_25_amt"));
        obj.setR15_30_9_25_amt(rs.getBigDecimal("r15_30_9_25_amt"));

        // ================= R16 =================
        obj.setR16_product(rs.getString("r16_product"));
        obj.setR16_31_3_25_amt(rs.getBigDecimal("r16_31_3_25_amt"));
        obj.setR16_30_9_25_amt(rs.getBigDecimal("r16_30_9_25_amt"));

        // ================= R17 =================
        obj.setR17_product(rs.getString("r17_product"));
        obj.setR17_31_3_25_amt(rs.getBigDecimal("r17_31_3_25_amt"));
        obj.setR17_30_9_25_amt(rs.getBigDecimal("r17_30_9_25_amt"));

        // ================= R18 =================
        obj.setR18_product(rs.getString("r18_product"));
        obj.setR18_31_3_25_amt(rs.getBigDecimal("r18_31_3_25_amt"));
        obj.setR18_30_9_25_amt(rs.getBigDecimal("r18_30_9_25_amt"));

        // ================= R19 =================
        obj.setR19_product(rs.getString("r19_product"));
        obj.setR19_31_3_25_amt(rs.getBigDecimal("r19_31_3_25_amt"));
        obj.setR19_30_9_25_amt(rs.getBigDecimal("r19_30_9_25_amt"));

        // ================= R20 =================
        obj.setR20_product(rs.getString("r20_product"));
        obj.setR20_31_3_25_amt(rs.getBigDecimal("r20_31_3_25_amt"));
        obj.setR20_30_9_25_amt(rs.getBigDecimal("r20_30_9_25_amt"));

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


public static class SCH_17_Manual_Summary_Entity1 {

    // ================= R10 =================
    private String r10_product;
    private BigDecimal r10_31_3_25_amt;
    private BigDecimal r10_30_9_25_amt;

    // ================= R11 =================
    private String r11_product;
    private BigDecimal r11_31_3_25_amt;
    private BigDecimal r11_30_9_25_amt;

    // ================= R12 =================
    private String r12_product;
    private BigDecimal r12_31_3_25_amt;
    private BigDecimal r12_30_9_25_amt;

    // ================= R14 =================
    private String r14_product;
    private BigDecimal r14_31_3_25_amt;
    private BigDecimal r14_30_9_25_amt;

    // ================= R15 =================
    private String r15_product;
    private BigDecimal r15_31_3_25_amt;
    private BigDecimal r15_30_9_25_amt;

    // ================= R16 =================
    private String r16_product;
    private BigDecimal r16_31_3_25_amt;
    private BigDecimal r16_30_9_25_amt;

    // ================= R17 =================
    private String r17_product;
    private BigDecimal r17_31_3_25_amt;
    private BigDecimal r17_30_9_25_amt;

    // ================= R18 =================
    private String r18_product;
    private BigDecimal r18_31_3_25_amt;
    private BigDecimal r18_30_9_25_amt;

    // ================= R19 =================
    private String r19_product;
    private BigDecimal r19_31_3_25_amt;
    private BigDecimal r19_30_9_25_amt;

    // ================= R20 =================
    private String r20_product;
    private BigDecimal r20_31_3_25_amt;
    private BigDecimal r20_30_9_25_amt;

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

    // ================= GETTERS & SETTERS =================

    public String getR10_product() { return r10_product; }
    public void setR10_product(String v) { this.r10_product = v; }

    public BigDecimal getR10_31_3_25_amt() { return r10_31_3_25_amt; }
    public void setR10_31_3_25_amt(BigDecimal v) { this.r10_31_3_25_amt = v; }

    public BigDecimal getR10_30_9_25_amt() { return r10_30_9_25_amt; }
    public void setR10_30_9_25_amt(BigDecimal v) { this.r10_30_9_25_amt = v; }

 // ================= R11 =================
    public String getR11_product() { return r11_product; }
    public void setR11_product(String v) { this.r11_product = v; }

    public BigDecimal getR11_31_3_25_amt() { return r11_31_3_25_amt; }
    public void setR11_31_3_25_amt(BigDecimal v) { this.r11_31_3_25_amt = v; }

    public BigDecimal getR11_30_9_25_amt() { return r11_30_9_25_amt; }
    public void setR11_30_9_25_amt(BigDecimal v) { this.r11_30_9_25_amt = v; }
    
 // ================= R12 =================
    public String getR12_product() { return r12_product; }
    public void setR12_product(String v) { this.r12_product = v; }

    public BigDecimal getR12_31_3_25_amt() { return r12_31_3_25_amt; }
    public void setR12_31_3_25_amt(BigDecimal v) { this.r12_31_3_25_amt = v; }

    public BigDecimal getR12_30_9_25_amt() { return r12_30_9_25_amt; }
    public void setR12_30_9_25_amt(BigDecimal v) { this.r12_30_9_25_amt = v; }
    
 // ================= R14 =================
    public String getR14_product() { return r14_product; }
    public void setR14_product(String v) { this.r14_product = v; }

    public BigDecimal getR14_31_3_25_amt() { return r14_31_3_25_amt; }
    public void setR14_31_3_25_amt(BigDecimal v) { this.r14_31_3_25_amt = v; }

    public BigDecimal getR14_30_9_25_amt() { return r14_30_9_25_amt; }
    public void setR14_30_9_25_amt(BigDecimal v) { this.r14_30_9_25_amt = v; }
    
 // ================= R15 =================
    public String getR15_product() { return r15_product; }
    public void setR15_product(String v) { this.r15_product = v; }

    public BigDecimal getR15_31_3_25_amt() { return r15_31_3_25_amt; }
    public void setR15_31_3_25_amt(BigDecimal v) { this.r15_31_3_25_amt = v; }

    public BigDecimal getR15_30_9_25_amt() { return r15_30_9_25_amt; }
    public void setR15_30_9_25_amt(BigDecimal v) { this.r15_30_9_25_amt = v; }
    
 // ================= R16 =================
    public String getR16_product() { return r16_product; }
    public void setR16_product(String v) { this.r16_product = v; }

    public BigDecimal getR16_31_3_25_amt() { return r16_31_3_25_amt; }
    public void setR16_31_3_25_amt(BigDecimal v) { this.r16_31_3_25_amt = v; }

    public BigDecimal getR16_30_9_25_amt() { return r16_30_9_25_amt; }
    public void setR16_30_9_25_amt(BigDecimal v) { this.r16_30_9_25_amt = v; }
    
 // ================= R17 =================
    public String getR17_product() { return r17_product; }
    public void setR17_product(String v) { this.r17_product = v; }

    public BigDecimal getR17_31_3_25_amt() { return r17_31_3_25_amt; }
    public void setR17_31_3_25_amt(BigDecimal v) { this.r17_31_3_25_amt = v; }

    public BigDecimal getR17_30_9_25_amt() { return r17_30_9_25_amt; }
    public void setR17_30_9_25_amt(BigDecimal v) { this.r17_30_9_25_amt = v; }
    
 // ================= R18 =================
    public String getR18_product() { return r18_product; }
    public void setR18_product(String v) { this.r18_product = v; }

    public BigDecimal getR18_31_3_25_amt() { return r18_31_3_25_amt; }
    public void setR18_31_3_25_amt(BigDecimal v) { this.r18_31_3_25_amt = v; }

    public BigDecimal getR18_30_9_25_amt() { return r18_30_9_25_amt; }
    public void setR18_30_9_25_amt(BigDecimal v) { this.r18_30_9_25_amt = v; }
    
 // ================= R19 =================
    public String getR19_product() { return r19_product; }
    public void setR19_product(String v) { this.r19_product = v; }

    public BigDecimal getR19_31_3_25_amt() { return r19_31_3_25_amt; }
    public void setR19_31_3_25_amt(BigDecimal v) { this.r19_31_3_25_amt = v; }

    public BigDecimal getR19_30_9_25_amt() { return r19_30_9_25_amt; }
    public void setR19_30_9_25_amt(BigDecimal v) { this.r19_30_9_25_amt = v; }
   
 // ================= R20 =================
    public String getR20_product() { return r20_product; }
    public void setR20_product(String v) { this.r20_product = v; }

    public BigDecimal getR20_31_3_25_amt() { return r20_31_3_25_amt; }
    public void setR20_31_3_25_amt(BigDecimal v) { this.r20_31_3_25_amt = v; }

    public BigDecimal getR20_30_9_25_amt() { return r20_30_9_25_amt; }
    public void setR20_30_9_25_amt(BigDecimal v) { this.r20_30_9_25_amt = v; }
    
    

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

class SCH17ArchivalRowMapper implements RowMapper<SCH_17_Archival_Summary_Entity1> {

 @Override
 public SCH_17_Archival_Summary_Entity1 mapRow(ResultSet rs, int rowNum)
         throws SQLException {

     SCH_17_Archival_Summary_Entity1 obj =
             new SCH_17_Archival_Summary_Entity1();

     // ================= R9 =================
     obj.setR9_product(rs.getString("r9_product"));
     obj.setR9_31_3_25_amt(rs.getBigDecimal("r9_31_3_25_amt"));
     obj.setR9_30_9_25_amt(rs.getBigDecimal("r9_30_9_25_amt"));

     // ================= R10 =================
     obj.setR10_product(rs.getString("r10_product"));
     obj.setR10_31_3_25_amt(rs.getBigDecimal("r10_31_3_25_amt"));
     obj.setR10_30_9_25_amt(rs.getBigDecimal("r10_30_9_25_amt"));

     // ================= R11 =================
     obj.setR11_product(rs.getString("r11_product"));
     obj.setR11_31_3_25_amt(rs.getBigDecimal("r11_31_3_25_amt"));
     obj.setR11_30_9_25_amt(rs.getBigDecimal("r11_30_9_25_amt"));

     // ================= R12 =================
     obj.setR12_product(rs.getString("r12_product"));
     obj.setR12_31_3_25_amt(rs.getBigDecimal("r12_31_3_25_amt"));
     obj.setR12_30_9_25_amt(rs.getBigDecimal("r12_30_9_25_amt"));

     // ================= R13 =================
     obj.setR13_product(rs.getString("r13_product"));
     obj.setR13_31_3_25_amt(rs.getBigDecimal("r13_31_3_25_amt"));
     obj.setR13_30_9_25_amt(rs.getBigDecimal("r13_30_9_25_amt"));

     // ================= R14 =================
     obj.setR14_product(rs.getString("r14_product"));
     obj.setR14_31_3_25_amt(rs.getBigDecimal("r14_31_3_25_amt"));
     obj.setR14_30_9_25_amt(rs.getBigDecimal("r14_30_9_25_amt"));

     // ================= R15 =================
     obj.setR15_product(rs.getString("r15_product"));
     obj.setR15_31_3_25_amt(rs.getBigDecimal("r15_31_3_25_amt"));
     obj.setR15_30_9_25_amt(rs.getBigDecimal("r15_30_9_25_amt"));

     // ================= R16 =================
     obj.setR16_product(rs.getString("r16_product"));
     obj.setR16_31_3_25_amt(rs.getBigDecimal("r16_31_3_25_amt"));
     obj.setR16_30_9_25_amt(rs.getBigDecimal("r16_30_9_25_amt"));

     // ================= R17 =================
     obj.setR17_product(rs.getString("r17_product"));
     obj.setR17_31_3_25_amt(rs.getBigDecimal("r17_31_3_25_amt"));
     obj.setR17_30_9_25_amt(rs.getBigDecimal("r17_30_9_25_amt"));

     // ================= R18 =================
     obj.setR18_product(rs.getString("r18_product"));
     obj.setR18_31_3_25_amt(rs.getBigDecimal("r18_31_3_25_amt"));
     obj.setR18_30_9_25_amt(rs.getBigDecimal("r18_30_9_25_amt"));

     // ================= R19 =================
     obj.setR19_product(rs.getString("r19_product"));
     obj.setR19_31_3_25_amt(rs.getBigDecimal("r19_31_3_25_amt"));
     obj.setR19_30_9_25_amt(rs.getBigDecimal("r19_30_9_25_amt"));

     // ================= R20 =================
     obj.setR20_product(rs.getString("r20_product"));
     obj.setR20_31_3_25_amt(rs.getBigDecimal("r20_31_3_25_amt"));
     obj.setR20_30_9_25_amt(rs.getBigDecimal("r20_30_9_25_amt"));

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



@IdClass(SCH_17_PK.class)
public class SCH_17_Archival_Summary_Entity1 {

    // ================= R9 =================
    private String r9_product;
    private BigDecimal r9_31_3_25_amt;
    private BigDecimal r9_30_9_25_amt;

    // ================= R10 =================
    private String r10_product;
    private BigDecimal r10_31_3_25_amt;
    private BigDecimal r10_30_9_25_amt;

    // ================= R11 =================
    private String r11_product;
    private BigDecimal r11_31_3_25_amt;
    private BigDecimal r11_30_9_25_amt;

    // ================= R12 =================
    private String r12_product;
    private BigDecimal r12_31_3_25_amt;
    private BigDecimal r12_30_9_25_amt;

    // ================= R13 =================
    private String r13_product;
    private BigDecimal r13_31_3_25_amt;
    private BigDecimal r13_30_9_25_amt;

    // ================= R14 =================
    private String r14_product;
    private BigDecimal r14_31_3_25_amt;
    private BigDecimal r14_30_9_25_amt;

    // ================= R15 =================
    private String r15_product;
    private BigDecimal r15_31_3_25_amt;
    private BigDecimal r15_30_9_25_amt;

    // ================= R16 =================
    private String r16_product;
    private BigDecimal r16_31_3_25_amt;
    private BigDecimal r16_30_9_25_amt;

    // ================= R17 =================
    private String r17_product;
    private BigDecimal r17_31_3_25_amt;
    private BigDecimal r17_30_9_25_amt;

    // ================= R18 =================
    private String r18_product;
    private BigDecimal r18_31_3_25_amt;
    private BigDecimal r18_30_9_25_amt;

    // ================= R19 =================
    private String r19_product;
    private BigDecimal r19_31_3_25_amt;
    private BigDecimal r19_30_9_25_amt;

    // ================= R20 =================
    private String r20_product;
    private BigDecimal r20_31_3_25_amt;
    private BigDecimal r20_30_9_25_amt;

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
	public String getR9_product() {
		return r9_product;
	}
	public void setR9_product(String r9_product) {
		this.r9_product = r9_product;
	}
	public BigDecimal getR9_31_3_25_amt() {
		return r9_31_3_25_amt;
	}
	public void setR9_31_3_25_amt(BigDecimal r9_31_3_25_amt) {
		this.r9_31_3_25_amt = r9_31_3_25_amt;
	}
	public BigDecimal getR9_30_9_25_amt() {
		return r9_30_9_25_amt;
	}
	public void setR9_30_9_25_amt(BigDecimal r9_30_9_25_amt) {
		this.r9_30_9_25_amt = r9_30_9_25_amt;
	}
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_31_3_25_amt() {
		return r10_31_3_25_amt;
	}
	public void setR10_31_3_25_amt(BigDecimal r10_31_3_25_amt) {
		this.r10_31_3_25_amt = r10_31_3_25_amt;
	}
	public BigDecimal getR10_30_9_25_amt() {
		return r10_30_9_25_amt;
	}
	public void setR10_30_9_25_amt(BigDecimal r10_30_9_25_amt) {
		this.r10_30_9_25_amt = r10_30_9_25_amt;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_31_3_25_amt() {
		return r11_31_3_25_amt;
	}
	public void setR11_31_3_25_amt(BigDecimal r11_31_3_25_amt) {
		this.r11_31_3_25_amt = r11_31_3_25_amt;
	}
	public BigDecimal getR11_30_9_25_amt() {
		return r11_30_9_25_amt;
	}
	public void setR11_30_9_25_amt(BigDecimal r11_30_9_25_amt) {
		this.r11_30_9_25_amt = r11_30_9_25_amt;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_31_3_25_amt() {
		return r12_31_3_25_amt;
	}
	public void setR12_31_3_25_amt(BigDecimal r12_31_3_25_amt) {
		this.r12_31_3_25_amt = r12_31_3_25_amt;
	}
	public BigDecimal getR12_30_9_25_amt() {
		return r12_30_9_25_amt;
	}
	public void setR12_30_9_25_amt(BigDecimal r12_30_9_25_amt) {
		this.r12_30_9_25_amt = r12_30_9_25_amt;
	}
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_31_3_25_amt() {
		return r13_31_3_25_amt;
	}
	public void setR13_31_3_25_amt(BigDecimal r13_31_3_25_amt) {
		this.r13_31_3_25_amt = r13_31_3_25_amt;
	}
	public BigDecimal getR13_30_9_25_amt() {
		return r13_30_9_25_amt;
	}
	public void setR13_30_9_25_amt(BigDecimal r13_30_9_25_amt) {
		this.r13_30_9_25_amt = r13_30_9_25_amt;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_31_3_25_amt() {
		return r14_31_3_25_amt;
	}
	public void setR14_31_3_25_amt(BigDecimal r14_31_3_25_amt) {
		this.r14_31_3_25_amt = r14_31_3_25_amt;
	}
	public BigDecimal getR14_30_9_25_amt() {
		return r14_30_9_25_amt;
	}
	public void setR14_30_9_25_amt(BigDecimal r14_30_9_25_amt) {
		this.r14_30_9_25_amt = r14_30_9_25_amt;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_31_3_25_amt() {
		return r15_31_3_25_amt;
	}
	public void setR15_31_3_25_amt(BigDecimal r15_31_3_25_amt) {
		this.r15_31_3_25_amt = r15_31_3_25_amt;
	}
	public BigDecimal getR15_30_9_25_amt() {
		return r15_30_9_25_amt;
	}
	public void setR15_30_9_25_amt(BigDecimal r15_30_9_25_amt) {
		this.r15_30_9_25_amt = r15_30_9_25_amt;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_31_3_25_amt() {
		return r16_31_3_25_amt;
	}
	public void setR16_31_3_25_amt(BigDecimal r16_31_3_25_amt) {
		this.r16_31_3_25_amt = r16_31_3_25_amt;
	}
	public BigDecimal getR16_30_9_25_amt() {
		return r16_30_9_25_amt;
	}
	public void setR16_30_9_25_amt(BigDecimal r16_30_9_25_amt) {
		this.r16_30_9_25_amt = r16_30_9_25_amt;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_31_3_25_amt() {
		return r17_31_3_25_amt;
	}
	public void setR17_31_3_25_amt(BigDecimal r17_31_3_25_amt) {
		this.r17_31_3_25_amt = r17_31_3_25_amt;
	}
	public BigDecimal getR17_30_9_25_amt() {
		return r17_30_9_25_amt;
	}
	public void setR17_30_9_25_amt(BigDecimal r17_30_9_25_amt) {
		this.r17_30_9_25_amt = r17_30_9_25_amt;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_31_3_25_amt() {
		return r18_31_3_25_amt;
	}
	public void setR18_31_3_25_amt(BigDecimal r18_31_3_25_amt) {
		this.r18_31_3_25_amt = r18_31_3_25_amt;
	}
	public BigDecimal getR18_30_9_25_amt() {
		return r18_30_9_25_amt;
	}
	public void setR18_30_9_25_amt(BigDecimal r18_30_9_25_amt) {
		this.r18_30_9_25_amt = r18_30_9_25_amt;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_31_3_25_amt() {
		return r19_31_3_25_amt;
	}
	public void setR19_31_3_25_amt(BigDecimal r19_31_3_25_amt) {
		this.r19_31_3_25_amt = r19_31_3_25_amt;
	}
	public BigDecimal getR19_30_9_25_amt() {
		return r19_30_9_25_amt;
	}
	public void setR19_30_9_25_amt(BigDecimal r19_30_9_25_amt) {
		this.r19_30_9_25_amt = r19_30_9_25_amt;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_31_3_25_amt() {
		return r20_31_3_25_amt;
	}
	public void setR20_31_3_25_amt(BigDecimal r20_31_3_25_amt) {
		this.r20_31_3_25_amt = r20_31_3_25_amt;
	}
	public BigDecimal getR20_30_9_25_amt() {
		return r20_30_9_25_amt;
	}
	public void setR20_30_9_25_amt(BigDecimal r20_30_9_25_amt) {
		this.r20_30_9_25_amt = r20_30_9_25_amt;
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


class SCH17ManualArchivalRowMapper
implements RowMapper<SCH_17_Archival_Manual_Summary_Entity1> {

@Override
public SCH_17_Archival_Manual_Summary_Entity1 mapRow(ResultSet rs, int rowNum)
    throws SQLException {

SCH_17_Archival_Manual_Summary_Entity1 obj =
        new SCH_17_Archival_Manual_Summary_Entity1();

// ================= R10 =================
obj.setR10_product(rs.getString("r10_product"));
obj.setR10_31_3_25_amt(rs.getBigDecimal("r10_31_3_25_amt"));
obj.setR10_30_9_25_amt(rs.getBigDecimal("r10_30_9_25_amt"));

// ================= R11 =================
obj.setR11_product(rs.getString("r11_product"));
obj.setR11_31_3_25_amt(rs.getBigDecimal("r11_31_3_25_amt"));
obj.setR11_30_9_25_amt(rs.getBigDecimal("r11_30_9_25_amt"));

// ================= R12 =================
obj.setR12_product(rs.getString("r12_product"));
obj.setR12_31_3_25_amt(rs.getBigDecimal("r12_31_3_25_amt"));
obj.setR12_30_9_25_amt(rs.getBigDecimal("r12_30_9_25_amt"));

// ================= R14 =================
obj.setR14_product(rs.getString("r14_product"));
obj.setR14_31_3_25_amt(rs.getBigDecimal("r14_31_3_25_amt"));
obj.setR14_30_9_25_amt(rs.getBigDecimal("r14_30_9_25_amt"));

// ================= R15 =================
obj.setR15_product(rs.getString("r15_product"));
obj.setR15_31_3_25_amt(rs.getBigDecimal("r15_31_3_25_amt"));
obj.setR15_30_9_25_amt(rs.getBigDecimal("r15_30_9_25_amt"));

// ================= R16 =================
obj.setR16_product(rs.getString("r16_product"));
obj.setR16_31_3_25_amt(rs.getBigDecimal("r16_31_3_25_amt"));
obj.setR16_30_9_25_amt(rs.getBigDecimal("r16_30_9_25_amt"));

// ================= R17 =================
obj.setR17_product(rs.getString("r17_product"));
obj.setR17_31_3_25_amt(rs.getBigDecimal("r17_31_3_25_amt"));
obj.setR17_30_9_25_amt(rs.getBigDecimal("r17_30_9_25_amt"));

// ================= R18 =================
obj.setR18_product(rs.getString("r18_product"));
obj.setR18_31_3_25_amt(rs.getBigDecimal("r18_31_3_25_amt"));
obj.setR18_30_9_25_amt(rs.getBigDecimal("r18_30_9_25_amt"));

// ================= R19 =================
obj.setR19_product(rs.getString("r19_product"));
obj.setR19_31_3_25_amt(rs.getBigDecimal("r19_31_3_25_amt"));
obj.setR19_30_9_25_amt(rs.getBigDecimal("r19_30_9_25_amt"));

// ================= R20 =================
obj.setR20_product(rs.getString("r20_product"));
obj.setR20_31_3_25_amt(rs.getBigDecimal("r20_31_3_25_amt"));
obj.setR20_30_9_25_amt(rs.getBigDecimal("r20_30_9_25_amt"));

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

//==============================
// COMPOSITE KEY CLASS INSIDE SERVICE
// ==============================
public static class SCH_17_PK implements Serializable {

    private Date report_date;
    private BigDecimal report_version;

    public SCH_17_PK() {}

    public SCH_17_PK(Date report_date, BigDecimal report_version) {
        this.report_date = report_date;
        this.report_version = report_version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SCH_17_PK)) return false;
        SCH_17_PK that = (SCH_17_PK) o;
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
@IdClass(SCH_17_PK.class)
public class SCH_17_Archival_Manual_Summary_Entity1 {

    // ================= R10 =================
    private String r10_product;
    private BigDecimal r10_31_3_25_amt;
    private BigDecimal r10_30_9_25_amt;

    // ================= R11 =================
    private String r11_product;
    private BigDecimal r11_31_3_25_amt;
    private BigDecimal r11_30_9_25_amt;

    // ================= R12 =================
    private String r12_product;
    private BigDecimal r12_31_3_25_amt;
    private BigDecimal r12_30_9_25_amt;

    // ================= R14 =================
    private String r14_product;
    private BigDecimal r14_31_3_25_amt;
    private BigDecimal r14_30_9_25_amt;

    // ================= R15 =================
    private String r15_product;
    private BigDecimal r15_31_3_25_amt;
    private BigDecimal r15_30_9_25_amt;

    // ================= R16 =================
    private String r16_product;
    private BigDecimal r16_31_3_25_amt;
    private BigDecimal r16_30_9_25_amt;

    // ================= R17 =================
    private String r17_product;
    private BigDecimal r17_31_3_25_amt;
    private BigDecimal r17_30_9_25_amt;

    // ================= R18 =================
    private String r18_product;
    private BigDecimal r18_31_3_25_amt;
    private BigDecimal r18_30_9_25_amt;

    // ================= R19 =================
    private String r19_product;
    private BigDecimal r19_31_3_25_amt;
    private BigDecimal r19_30_9_25_amt;

    // ================= R20 =================
    private String r20_product;
    private BigDecimal r20_31_3_25_amt;
    private BigDecimal r20_30_9_25_amt;

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
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_31_3_25_amt() {
		return r10_31_3_25_amt;
	}
	public void setR10_31_3_25_amt(BigDecimal r10_31_3_25_amt) {
		this.r10_31_3_25_amt = r10_31_3_25_amt;
	}
	public BigDecimal getR10_30_9_25_amt() {
		return r10_30_9_25_amt;
	}
	public void setR10_30_9_25_amt(BigDecimal r10_30_9_25_amt) {
		this.r10_30_9_25_amt = r10_30_9_25_amt;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_31_3_25_amt() {
		return r11_31_3_25_amt;
	}
	public void setR11_31_3_25_amt(BigDecimal r11_31_3_25_amt) {
		this.r11_31_3_25_amt = r11_31_3_25_amt;
	}
	public BigDecimal getR11_30_9_25_amt() {
		return r11_30_9_25_amt;
	}
	public void setR11_30_9_25_amt(BigDecimal r11_30_9_25_amt) {
		this.r11_30_9_25_amt = r11_30_9_25_amt;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_31_3_25_amt() {
		return r12_31_3_25_amt;
	}
	public void setR12_31_3_25_amt(BigDecimal r12_31_3_25_amt) {
		this.r12_31_3_25_amt = r12_31_3_25_amt;
	}
	public BigDecimal getR12_30_9_25_amt() {
		return r12_30_9_25_amt;
	}
	public void setR12_30_9_25_amt(BigDecimal r12_30_9_25_amt) {
		this.r12_30_9_25_amt = r12_30_9_25_amt;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_31_3_25_amt() {
		return r14_31_3_25_amt;
	}
	public void setR14_31_3_25_amt(BigDecimal r14_31_3_25_amt) {
		this.r14_31_3_25_amt = r14_31_3_25_amt;
	}
	public BigDecimal getR14_30_9_25_amt() {
		return r14_30_9_25_amt;
	}
	public void setR14_30_9_25_amt(BigDecimal r14_30_9_25_amt) {
		this.r14_30_9_25_amt = r14_30_9_25_amt;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_31_3_25_amt() {
		return r15_31_3_25_amt;
	}
	public void setR15_31_3_25_amt(BigDecimal r15_31_3_25_amt) {
		this.r15_31_3_25_amt = r15_31_3_25_amt;
	}
	public BigDecimal getR15_30_9_25_amt() {
		return r15_30_9_25_amt;
	}
	public void setR15_30_9_25_amt(BigDecimal r15_30_9_25_amt) {
		this.r15_30_9_25_amt = r15_30_9_25_amt;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_31_3_25_amt() {
		return r16_31_3_25_amt;
	}
	public void setR16_31_3_25_amt(BigDecimal r16_31_3_25_amt) {
		this.r16_31_3_25_amt = r16_31_3_25_amt;
	}
	public BigDecimal getR16_30_9_25_amt() {
		return r16_30_9_25_amt;
	}
	public void setR16_30_9_25_amt(BigDecimal r16_30_9_25_amt) {
		this.r16_30_9_25_amt = r16_30_9_25_amt;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_31_3_25_amt() {
		return r17_31_3_25_amt;
	}
	public void setR17_31_3_25_amt(BigDecimal r17_31_3_25_amt) {
		this.r17_31_3_25_amt = r17_31_3_25_amt;
	}
	public BigDecimal getR17_30_9_25_amt() {
		return r17_30_9_25_amt;
	}
	public void setR17_30_9_25_amt(BigDecimal r17_30_9_25_amt) {
		this.r17_30_9_25_amt = r17_30_9_25_amt;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_31_3_25_amt() {
		return r18_31_3_25_amt;
	}
	public void setR18_31_3_25_amt(BigDecimal r18_31_3_25_amt) {
		this.r18_31_3_25_amt = r18_31_3_25_amt;
	}
	public BigDecimal getR18_30_9_25_amt() {
		return r18_30_9_25_amt;
	}
	public void setR18_30_9_25_amt(BigDecimal r18_30_9_25_amt) {
		this.r18_30_9_25_amt = r18_30_9_25_amt;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_31_3_25_amt() {
		return r19_31_3_25_amt;
	}
	public void setR19_31_3_25_amt(BigDecimal r19_31_3_25_amt) {
		this.r19_31_3_25_amt = r19_31_3_25_amt;
	}
	public BigDecimal getR19_30_9_25_amt() {
		return r19_30_9_25_amt;
	}
	public void setR19_30_9_25_amt(BigDecimal r19_30_9_25_amt) {
		this.r19_30_9_25_amt = r19_30_9_25_amt;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_31_3_25_amt() {
		return r20_31_3_25_amt;
	}
	public void setR20_31_3_25_amt(BigDecimal r20_31_3_25_amt) {
		this.r20_31_3_25_amt = r20_31_3_25_amt;
	}
	public BigDecimal getR20_30_9_25_amt() {
		return r20_30_9_25_amt;
	}
	public void setR20_30_9_25_amt(BigDecimal r20_30_9_25_amt) {
		this.r20_30_9_25_amt = r20_30_9_25_amt;
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




public class SCH_17_Detail_Entity1 {

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



class SCH17DetailRowMapper implements RowMapper<SCH_17_Detail_Entity1> {

    @Override
    public SCH_17_Detail_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

    	SCH_17_Detail_Entity1 obj = new SCH_17_Detail_Entity1();

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


class SCH17ArchivalDetailRowMapper implements RowMapper<SCH_17_Archival_Detail_Entity1> {

    @Override
    public SCH_17_Archival_Detail_Entity1 mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        SCH_17_Archival_Detail_Entity1 obj = new SCH_17_Archival_Detail_Entity1();

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


public class SCH_17_Archival_Detail_Entity1 {

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

  //=====================================================
 // MODEL AND VIEW METHOD summary
 //=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");


 public ModelAndView getSCH_17_NewView(

	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("SCH_17_New View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    // =====================================================
	    // ARCHIVAL MODE
	    // =====================================================

	    if ("ARCHIVAL".equals(type) && version != null) {

	        List<SCH_17_Archival_Summary_Entity1> T1Master = new ArrayList<>();
	        List<SCH_17_Archival_Manual_Summary_Entity1> T2Master = new ArrayList<>();

	        try {
	            Date dt = dateformat.parse(todate);
	            
	        
	            // ============================
	            // SUMMARY ARCHIVAL
	            // ============================
	            T1Master = getdatabydateListarchival(dt, version);

	            System.out.println("Archival Summary size = " + T1Master.size());

	            // ============================
	            // MANUAL ARCHIVAL
	            // ============================
	            T2Master = getManualArchivalByDate(dt, version);

	            System.out.println("Archival Manual size = " + T2Master.size());

	            mv.addObject("report_date", dateformat.format(dt));

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        mv.addObject("reportsummary", T1Master);
	        mv.addObject("reportsummary1", T2Master);
	    }
	    // =====================================================
	    // NORMAL MODE
	    // =====================================================

	    else {

	        List<SCH_17_Summary_Entity1> T1Master = new ArrayList<>();
	        List<SCH_17_Manual_Summary_Entity1> T2Master = new ArrayList<>();

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

	    // =====================================================
	    // VIEW SETTINGS
	    // =====================================================

	    mv.setViewName("BRRS/SCH_17_New");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
 
 
 //=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getSCH_17_NewcurrentDtl(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String filter,
	        String type,
	        String version) {

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

	        // =====================================================
	        // ARCHIVAL MODE
	        // =====================================================
	        if ("ARCHIVAL".equals(type) && version != null) {

	            System.out.println("ARCHIVAL DETAIL MODE");

	            List<SCH_17_Archival_Detail_Entity1> archivalDetailList;

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

	            System.out.println("ARCHIVAL DETAIL COUNT: " + archivalDetailList.size());

	        }

	        // =====================================================
	        // CURRENT MODE
	        // =====================================================
	        else {

	            List<SCH_17_Detail_Entity1> currentDetailList;

	            if (reportLabel != null && reportAddlCriteria1 != null) {

	                currentDetailList =
	                       GetDetailDataByRowIdAndColumnId(
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

	            System.out.println("CURRENT DETAIL COUNT: " + currentDetailList.size());
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        mv.addObject("errorMessage", e.getMessage());
	    }

	    mv.setViewName("BRRS/SCH_17_New");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
 

//Archival View
		public List<Object[]> getSCH_17_newArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<SCH_17_Archival_Summary_Entity1> repoData =
						getdatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (SCH_17_Archival_Summary_Entity1 entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					SCH_17_Archival_Summary_Entity1 first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  SCH_17  Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}
	
		
		public void updateSCH17NewReport(
		        SCH_17_Manual_Summary_Entity1 updatedEntity) {

		    System.out.println("Came to SCH_17 Manual Update");
		    System.out.println("Report Date: " + updatedEntity.getReport_date());

		    // Allowed rows
		    int[] rows = {10, 11, 12, 14, 15, 16, 18, 19};

		    try {

		        // Loop rows
		        for (int r : rows) {

		            // Two amount columns
		            String[] cols = {"31_3_25_amt", "30_9_25_amt"};

		            for (String col : cols) {

		                String getterName =
		                        "getR" + r + "_" + col;

		                try {

		                    Method getter =
		                            SCH_17_Manual_Summary_Entity1.class
		                                    .getMethod(getterName);

		                    Object value =
		                            getter.invoke(updatedEntity);

		                    // Skip null values
		                    if (value == null) continue;

		                    // Column name in DB
		                    String columnName =
		                            "R" + r + "_" + col;

		                    String sql =
		                            "UPDATE BRRS_SCH_17_MANUAL_SUMMARYTABLE " +
		                            "SET " + columnName + " = ? " +
		                            "WHERE REPORT_DATE = ?";

		                    jdbcTemplate.update(
		                            sql,
		                            value,
		                            updatedEntity.getReport_date()
		                    );

		                } catch (NoSuchMethodException e) {
		                    // Skip if method not exists
		                    continue;
		                }
		            }
		        }

		        System.out.println("SCH_17 Manual Update Completed");

		    } catch (Exception e) {
		        throw new RuntimeException(
		                "Error while updating SCH_17 Manual fields", e);
		    }
		}
		
		public ModelAndView getViewOrEditPage(String SNO, String formMode) {
			ModelAndView mv = new ModelAndView("BRRS/SCH_17_New"); 

			System.out.println("sno is : "+ SNO);
			if (SNO != null) {
				SCH_17_Detail_Entity1 sch_17Entity = findBySno(SNO);
				if (sch_17Entity != null && sch_17Entity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(sch_17Entity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("sch_17_newData", sch_17Entity);
			}

			mv.addObject("displaymode", "edit");
			mv.addObject("formmode", formMode != null ? formMode : "edit");
			return mv;
		}
		
		@Transactional
		public ResponseEntity<?> updateDetailEdit(
		        HttpServletRequest request) {

		    try {

		        String acctNo =
		                request.getParameter("acctNumber");

		        String Sno =
		                request.getParameter("sno");

		        String acctBalanceInpula =
		                request.getParameter("acctBalanceInpula");

		        String acctName =
		                request.getParameter("acctName");

		        String reportDateStr =
		                request.getParameter("reportDate");

		        System.out.println("Sno is : " + Sno);

		        // Load Existing Record
		        SCH_17_Detail_Entity1 existing =
		                findBySno(Sno);

		        if (existing == null) {

		            return ResponseEntity
		                    .status(HttpStatus.NOT_FOUND)
		                    .body("Record not found for update.");
		        }

		        boolean isChanged = false;

		        // Update Name
		        if (acctName != null &&
		            !acctName.isEmpty()) {

		            if (existing.getAcctName() == null ||
		                !existing.getAcctName()
		                        .equals(acctName)) {

		                existing.setAcctName(acctName);

		                isChanged = true;
		            }
		        }

		        // Update Balance
		        if (acctBalanceInpula != null &&
		            !acctBalanceInpula.isEmpty()) {

		            BigDecimal newBalance =
		                    new BigDecimal(acctBalanceInpula);

		            if (existing.getAcctBalanceInpula() == null ||
		                existing.getAcctBalanceInpula()
		                        .compareTo(newBalance) != 0) {

		                existing.setAcctBalanceInpula(
		                        newBalance);

		                isChanged = true;
		            }
		        }

		        // Save using JDBC
		        if (isChanged) {

		            String sql =
		                "UPDATE BRRS_SCH_17_DETAILTABLE " +
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

		            // Format Date
		            String formattedDate =
		                new SimpleDateFormat("dd-MM-yyyy")
		                .format(
		                    new SimpleDateFormat("yyyy-MM-dd")
		                    .parse(reportDateStr)
		                );

		            // Call Procedure After Commit
		            TransactionSynchronizationManager
		                .registerSynchronization(
		                    new TransactionSynchronizationAdapter() {

		                @Override
		                public void afterCommit() {

		                    try {

		                        jdbcTemplate.update(
		                            "BEGIN BRRS_SCH_17_SUMMARY_PROCEDURE(?); END;",
		                            formattedDate
		                        );

		                        System.out.println(
		                            "Procedure executed");

		                    } catch (Exception e) {

		                        e.printStackTrace();
		                    }
		                }
		            });

		            return ResponseEntity
		                    .ok("Record updated successfully!");
		        }

		        else {

		            return ResponseEntity
		                    .ok("No changes were made.");
		        }

		    }

		    catch (Exception e) {

		        e.printStackTrace();

		        return ResponseEntity
		                .status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body("Error updating record: "
		                        + e.getMessage());
		    }
		}
		
		
		public byte[] getSCH_17_NewDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  SCH_17NEW Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getSCH_17DetailNewExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("SCH_17 Details New");

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
				String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABEL", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
				List<SCH_17_Detail_Entity1> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (SCH_17_Detail_Entity1 item : reportData) { 
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
					logger.info("No data found for SCH_17 — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating SCH_17 Excel", e);
				return new byte[0];
			}
		}
		
		public byte[] getSCH_17DetailNewExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for SCH_17NEW ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("SCH_17 Detail NEW");

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
				String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE",  "REPORT LABEL", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
				List<SCH_17_Archival_Detail_Entity1> reportData = getArchivalDetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (SCH_17_Archival_Detail_Entity1 item : reportData) {
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
					logger.info("No data found for SCH_17NEW — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating SCH_17 NEW Excel", e);
				return new byte[0];
			}
		}
		
		public byte[] getSCH_17_NewExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.sch17");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelSCH_17_NewARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<SCH_17_Summary_Entity1> dataList = getDataByDate(dateformat.parse(todate));
			List<SCH_17_Manual_Summary_Entity1> dataList1 = getManualDataByDate(dateformat.parse(todate));
		
			System.out.println("DATA SIZE IS : "+dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  SCH_17new report. Returning empty result.");
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

				int startRow = 8;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						SCH_17_Summary_Entity1 record = dataList.get(i);
					SCH_17_Manual_Summary_Entity1 record1 = dataList1.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

	  
						

				
						// Column C
						Cell cellC = row.createCell(2);
						if (record.getR9_31_3_25_amt() != null) {
						    cellC.setCellValue(record.getR9_31_3_25_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue(0);   // IMPORTANT
						    cellC.setCellStyle(numberStyle);
						}

						// Column D
						Cell cellD = row.createCell(3);
						if (record.getR9_30_9_25_amt() != null) {
						    cellD.setCellValue(record.getR9_30_9_25_amt().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue(0);   // IMPORTANT
						    cellD.setCellStyle(numberStyle);
						}


						// R10
						row = sheet.getRow(9);
					   cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR10_31_3_25_amt() != null ? record1.getR10_31_3_25_amt().doubleValue() : 0);
						
						// R10
					
					   cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR10_30_9_25_amt() != null ? record1.getR10_30_9_25_amt().doubleValue() : 0);


					
						// R11
						row = sheet.getRow(10);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR11_31_3_25_amt() != null ? record1.getR11_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR11_30_9_25_amt() != null ? record1.getR11_30_9_25_amt().doubleValue() : 0);


						// R12
						row = sheet.getRow(11);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR12_31_3_25_amt() != null ? record1.getR12_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR12_30_9_25_amt() != null ? record1.getR12_30_9_25_amt().doubleValue() : 0);


						// R13
						row = sheet.getRow(12);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record.getR13_31_3_25_amt() != null ? record.getR13_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record.getR13_30_9_25_amt() != null ? record.getR13_30_9_25_amt().doubleValue() : 0);


						// R14
						row = sheet.getRow(13);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR14_31_3_25_amt() != null ? record1.getR14_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR14_30_9_25_amt() != null ? record1.getR14_30_9_25_amt().doubleValue() : 0);


						// R15
						row = sheet.getRow(14);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR15_31_3_25_amt() != null ? record1.getR15_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR15_30_9_25_amt() != null ? record1.getR15_30_9_25_amt().doubleValue() : 0);


						// R16
						row = sheet.getRow(15);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR16_31_3_25_amt() != null ? record1.getR16_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR16_30_9_25_amt() != null ? record1.getR16_30_9_25_amt().doubleValue() : 0);

						// R17
						row = sheet.getRow(16);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record.getR17_31_3_25_amt() != null ? record.getR17_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record.getR17_30_9_25_amt() != null ? record.getR17_30_9_25_amt().doubleValue() : 0);
					

						// R18
						row = sheet.getRow(17);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR18_31_3_25_amt() != null ? record1.getR18_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR18_30_9_25_amt() != null ? record1.getR18_30_9_25_amt().doubleValue() : 0);


						// R19
						row = sheet.getRow(18);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR19_31_3_25_amt() != null ? record1.getR19_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR19_30_9_25_amt() != null ? record1.getR19_30_9_25_amt().doubleValue() : 0);
						
						
						// R20
						row = sheet.getRow(19);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record.getR20_31_3_25_amt() != null ? record.getR20_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record.getR20_30_9_25_amt() != null ? record.getR20_30_9_25_amt().doubleValue() : 0);
						
					
					
					}
					
					/* workbook.getCreationHelper().createFormulaEvaluator().evaluateAll(); */

					
				} else {

				}

	// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

				return out.toByteArray();
			}

		}
		
		
		public byte[] getExcelSCH_17_NewARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {

			}

			List<SCH_17_Archival_Summary_Entity1> dataList = 
					getdatabydateListarchival(dateformat.parse(todate), version);
		
	       List<SCH_17_Archival_Manual_Summary_Entity1> dataList1 = 
					getManualArchivalByDate(dateformat.parse(todate), version);
			

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for SCH_17 new report. Returning empty result.");
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

				int startRow = 8;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						SCH_17_Archival_Summary_Entity1 record = dataList.get(i);
					    SCH_17_Archival_Manual_Summary_Entity1 record1 = dataList1.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


							// Column C
						Cell cellC = row.createCell(2);
						if (record.getR9_31_3_25_amt() != null) {
						    cellC.setCellValue(record.getR9_31_3_25_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue(0);   // IMPORTANT
						    cellC.setCellStyle(numberStyle);
						}

						// Column D
						Cell cellD = row.createCell(3);
						if (record.getR9_30_9_25_amt() != null) {
						    cellD.setCellValue(record.getR9_30_9_25_amt().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue(0);   // IMPORTANT
						    cellD.setCellStyle(numberStyle);
						}


						// R10
						row = sheet.getRow(9);
					   cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR10_31_3_25_amt() != null ? record1.getR10_31_3_25_amt().doubleValue() : 0);
						
						// R10
					
					   cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR10_30_9_25_amt() != null ? record1.getR10_30_9_25_amt().doubleValue() : 0);


					
						// R11
						row = sheet.getRow(10);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR11_31_3_25_amt() != null ? record1.getR11_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR11_30_9_25_amt() != null ? record1.getR11_30_9_25_amt().doubleValue() : 0);


						// R12
						row = sheet.getRow(11);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR12_31_3_25_amt() != null ? record1.getR12_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR12_30_9_25_amt() != null ? record1.getR12_30_9_25_amt().doubleValue() : 0);


						// R13
						row = sheet.getRow(12);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record.getR13_31_3_25_amt() != null ? record.getR13_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record.getR13_30_9_25_amt() != null ? record.getR13_30_9_25_amt().doubleValue() : 0);


						// R14
						row = sheet.getRow(13);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR14_31_3_25_amt() != null ? record1.getR14_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR14_30_9_25_amt() != null ? record1.getR14_30_9_25_amt().doubleValue() : 0);


						// R15
						row = sheet.getRow(14);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR15_31_3_25_amt() != null ? record1.getR15_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR15_30_9_25_amt() != null ? record1.getR15_30_9_25_amt().doubleValue() : 0);


						// R16
						row = sheet.getRow(15);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR16_31_3_25_amt() != null ? record1.getR16_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR16_30_9_25_amt() != null ? record1.getR16_30_9_25_amt().doubleValue() : 0);

						// R17
						row = sheet.getRow(16);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record.getR17_31_3_25_amt() != null ? record.getR17_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record.getR17_30_9_25_amt() != null ? record.getR17_30_9_25_amt().doubleValue() : 0);
					

						// R18
						row = sheet.getRow(17);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR18_31_3_25_amt() != null ? record1.getR18_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR18_30_9_25_amt() != null ? record1.getR18_30_9_25_amt().doubleValue() : 0);


						// R19
						row = sheet.getRow(18);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR19_31_3_25_amt() != null ? record1.getR19_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR19_30_9_25_amt() != null ? record1.getR19_30_9_25_amt().doubleValue() : 0);
						
						
						// R20
						row = sheet.getRow(19);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						cellC.setCellValue(record1.getR20_31_3_25_amt() != null ? record1.getR20_31_3_25_amt().doubleValue() : 0);

						cellD = row.getCell(3);
						if (cellD == null) cellD = row.createCell(3);
						cellD.setCellValue(record1.getR20_30_9_25_amt() != null ? record1.getR20_30_9_25_amt().doubleValue() : 0);
						
						
					}

					
				} else {

				}

	// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

				return out.toByteArray();
			}

		}
		
		

}