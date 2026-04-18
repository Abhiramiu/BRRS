package com.bornfire.brrs.services;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;



@Service
@Transactional
public class BRRS_SCH_17_New_Service {

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
    
 // =========================================================
 // GET ALL ARCHIVAL REPORT DATE + VERSION (JDBC)
 // =========================================================

 public List<Object[]> getSCH_17archival() {

     String sql =
         "SELECT REPORT_DATE, REPORT_VERSION " +
         "FROM BRRS_SCH_17_ARCHIVALTABLE_SUMMARY " +
         "ORDER BY REPORT_VERSION";

     return jdbcTemplate.query(sql,
         (rs, rowNum) -> new Object[] {
             rs.getDate("REPORT_DATE"),
             rs.getBigDecimal("REPORT_VERSION")
         }
     );
 }
 
//=========================================================
//GET ARCHIVAL FULL DATA BY DATE + VERSION (JDBC)
//=========================================================

public List<SCH_17_Archival_Summary_Entity1> getdatabydateListarchival(
      Date reportDate,
      BigDecimal reportVersion) {

  String sql =
      "SELECT * FROM BRRS_SCH_17_ARCHIVALTABLE_SUMMARY " +
      "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

  return jdbcTemplate.query(
          sql,
          new Object[]{reportDate, reportVersion},
          new SCH17ArchivalRowMapper()
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
    @Id
    @Temporal(TemporalType.DATE)
    private Date report_date;

    private BigDecimal report_version;
    private String report_frequency;
    private String report_code;
    private String report_desc;
    private String entity_flg;
    private String modify_flg;
    private String del_flg;
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
	public SCH_17_Archival_Summary_Entity1() {
		super();
		// TODO Auto-generated constructor stub
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
    @Id
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date report_date;

    private BigDecimal report_version;
    private String report_frequency;
    private String report_code;
    private String report_desc;
    private String entity_flg;
    private String modify_flg;
    private String del_flg;

    // ================= GETTERS & SETTERS =================

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

  //=====================================================
 // MODEL AND VIEW METHOD
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


}