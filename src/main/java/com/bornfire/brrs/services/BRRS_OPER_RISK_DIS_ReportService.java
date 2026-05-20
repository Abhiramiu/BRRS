




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

public class BRRS_OPER_RISK_DIS_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_OPER_RISK_DIS_ReportService.class);
	
	
	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

  
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;
	

// =====================================================
// SUMAMRY REPO
// =====================================================


	public List<OPER_RISK_DIS_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_OPER_RISK_DIS_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new OPER_RISK_DIS_Summary_RowMapper()
    );
}
	
// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================



public List<Object[]> getOPER_RISK_DIS_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_OPER_RISK_DIS_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<OPER_RISK_DIS_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_OPER_RISK_DIS_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new OPER_RISK_DIS_Archival_Summary_RowMapper()
    );
}

public List<OPER_RISK_DIS_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_OPER_RISK_DIS_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new OPER_RISK_DIS_Archival_Summary_RowMapper()
    );
}

// =====================================================
// DETAIL REPO
// =====================================================	


public List<OPER_RISK_DIS_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_OPER_RISK_DIS_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new OPER_RISK_DIS_Detail_RowMapper()
    );
}

public List<OPER_RISK_DIS_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_OPER_RISK_DIS_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, offset, limit},
            new OPER_RISK_DIS_Detail_RowMapper()
    );
}

public int getDataCount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_OPER_RISK_DIS_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(sql, new Object[]{reportDate}, Integer.class);
}

public List<OPER_RISK_DIS_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel, String reportAddlCriteria1, Date reportDate) {

    String sql = "SELECT * FROM BRRS_OPER_RISK_DIS_DETAILTABLE " +
                 "WHERE REPORT_LABEL = ? " +
                 "AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate},
            new OPER_RISK_DIS_Detail_RowMapper()
    );
}

public OPER_RISK_DIS_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_OPER_RISK_DIS_DETAILTABLE WHERE ACCT_NUMBER = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{acctNumber},
            new OPER_RISK_DIS_Detail_RowMapper()
    );
}


// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================

public List<OPER_RISK_DIS_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate, String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_OPER_RISK_DIS_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, dataEntryVersion},
            new OPER_RISK_DIS_Archival_Detail_RowMapper()
    );
}


public List<OPER_RISK_DIS_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_OPER_RISK_DIS_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_LABEL = ? " +
                 "AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ? " +
                 "AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{
                    reportLabel,
                    reportAddlCriteria1,
                    reportDate,
                    dataEntryVersion
            },
            new OPER_RISK_DIS_Archival_Detail_RowMapper()
    );
}

// =====================================================
// SUMAMRY ENTITY 
// =====================================================


public class OPER_RISK_DIS_Summary_RowMapper implements RowMapper<OPER_RISK_DIS_Summary_Entity> {

    @Override
    public OPER_RISK_DIS_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        OPER_RISK_DIS_Summary_Entity obj = new OPER_RISK_DIS_Summary_Entity();

// =========================
// R3
// =========================
obj.setR3_qua_name(rs.getString("r3_qua_name"));
obj.setR3_product(rs.getString("r3_product"));
obj.setR3_amt(rs.getBigDecimal("r3_amt"));

// =========================
// R4
// =========================
obj.setR4_qua_name(rs.getString("r4_qua_name"));
obj.setR4_product(rs.getString("r4_product"));
obj.setR4_amt(rs.getBigDecimal("r4_amt"));

// =========================
// R9
// =========================
obj.setR9_qua_name(rs.getString("r9_qua_name"));
obj.setR9_product(rs.getString("r9_product"));
obj.setR9_amt(rs.getBigDecimal("r9_amt"));

// =========================
// R10
// =========================
obj.setR10_product(rs.getString("r10_product"));
obj.setR10_amt(rs.getBigDecimal("r10_amt"));

// =========================
// R11
// =========================
obj.setR11_qua_name(rs.getString("r11_qua_name"));
obj.setR11_product(rs.getString("r11_product"));
obj.setR11_amt(rs.getBigDecimal("r11_amt"));

// =========================
// R12
// =========================
obj.setR12_product(rs.getString("r12_product"));
obj.setR12_amt(rs.getBigDecimal("r12_amt"));

// =========================
// R18
// =========================
obj.setR18_tot_remu_cur_yr(rs.getString("r18_tot_remu_cur_yr"));
obj.setR18_unrestricted(rs.getBigDecimal("r18_unrestricted"));
obj.setR18_deferred(rs.getBigDecimal("r18_deferred"));

// =========================
// R19
// =========================
obj.setR19_tot_remu_cur_yr(rs.getString("r19_tot_remu_cur_yr"));
obj.setR19_unrestricted(rs.getBigDecimal("r19_unrestricted"));
obj.setR19_deferred(rs.getBigDecimal("r19_deferred"));

// =========================
// R20
// =========================
obj.setR20_tot_remu_cur_yr(rs.getString("r20_tot_remu_cur_yr"));
obj.setR20_unrestricted(rs.getBigDecimal("r20_unrestricted"));
obj.setR20_deferred(rs.getBigDecimal("r20_deferred"));

// =========================
// R21
// =========================
obj.setR21_tot_remu_cur_yr(rs.getString("r21_tot_remu_cur_yr"));
obj.setR21_unrestricted(rs.getBigDecimal("r21_unrestricted"));
obj.setR21_deferred(rs.getBigDecimal("r21_deferred"));

// =========================
// R22
// =========================
obj.setR22_tot_remu_cur_yr(rs.getString("r22_tot_remu_cur_yr"));
obj.setR22_unrestricted(rs.getBigDecimal("r22_unrestricted"));
obj.setR22_deferred(rs.getBigDecimal("r22_deferred"));

// =========================
// R23
// =========================
obj.setR23_tot_remu_cur_yr(rs.getString("r23_tot_remu_cur_yr"));
obj.setR23_unrestricted(rs.getBigDecimal("r23_unrestricted"));
obj.setR23_deferred(rs.getBigDecimal("r23_deferred"));

// =========================
// R24
// =========================
obj.setR24_tot_remu_cur_yr(rs.getString("r24_tot_remu_cur_yr"));
obj.setR24_unrestricted(rs.getBigDecimal("r24_unrestricted"));
obj.setR24_deferred(rs.getBigDecimal("r24_deferred"));

// =========================
// R25
// =========================
obj.setR25_tot_remu_cur_yr(rs.getString("r25_tot_remu_cur_yr"));
obj.setR25_unrestricted(rs.getBigDecimal("r25_unrestricted"));
obj.setR25_deferred(rs.getBigDecimal("r25_deferred"));
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


public class OPER_RISK_DIS_Summary_Entity {
	
private String	r3_qua_name ;
	private String	r3_product;
	private BigDecimal	r3_amt;

	private String	r4_qua_name ;
	private String	r4_product;
	private BigDecimal	r4_amt;

	private String	r9_qua_name ;
	private String	r9_product;
	private BigDecimal	r9_amt;

	private String	r10_product;
	private BigDecimal	r10_amt;

	private String	r11_qua_name ;
	private String	r11_product;
	private BigDecimal	r11_amt;

	private String	r12_product;
	private BigDecimal	r12_amt;

	private String	r18_tot_remu_cur_yr;
	private BigDecimal	r18_unrestricted;
	private BigDecimal	r18_deferred;
	private String	r19_tot_remu_cur_yr;
	private BigDecimal	r19_unrestricted;
	private BigDecimal	r19_deferred;
	private String	r20_tot_remu_cur_yr;
	private BigDecimal	r20_unrestricted;
	private BigDecimal	r20_deferred;
	private String	r21_tot_remu_cur_yr;
	private BigDecimal	r21_unrestricted;
	private BigDecimal	r21_deferred;
	private String	r22_tot_remu_cur_yr;
	private BigDecimal	r22_unrestricted;
	private BigDecimal	r22_deferred;
	private String	r23_tot_remu_cur_yr;
	private BigDecimal	r23_unrestricted;
	private BigDecimal	r23_deferred;
	private String	r24_tot_remu_cur_yr;
	private BigDecimal	r24_unrestricted;
	private BigDecimal	r24_deferred;
	private String	r25_tot_remu_cur_yr;
	private BigDecimal	r25_unrestricted;
	private BigDecimal	r25_deferred;

	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	
	
	private Date	report_date;
	private BigDecimal	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
public String getR3_qua_name() {
		return r3_qua_name;
	}
	public void setR3_qua_name(String r3_qua_name) {
		this.r3_qua_name = r3_qua_name;
	}
	public String getR3_product() {
		return r3_product;
	}
	public void setR3_product(String r3_product) {
		this.r3_product = r3_product;
	}
	public BigDecimal getR3_amt() {
		return r3_amt;
	}
	public void setR3_amt(BigDecimal r3_amt) {
		this.r3_amt = r3_amt;
	}
	public String getR4_qua_name() {
		return r4_qua_name;
	}
	public void setR4_qua_name(String r4_qua_name) {
		this.r4_qua_name = r4_qua_name;
	}
	public String getR4_product() {
		return r4_product;
	}
	public void setR4_product(String r4_product) {
		this.r4_product = r4_product;
	}
	public BigDecimal getR4_amt() {
		return r4_amt;
	}
	public void setR4_amt(BigDecimal r4_amt) {
		this.r4_amt = r4_amt;
	}
	public String getR9_qua_name() {
		return r9_qua_name;
	}
	public void setR9_qua_name(String r9_qua_name) {
		this.r9_qua_name = r9_qua_name;
	}
	public String getR9_product() {
		return r9_product;
	}
	public void setR9_product(String r9_product) {
		this.r9_product = r9_product;
	}
	public BigDecimal getR9_amt() {
		return r9_amt;
	}
	public void setR9_amt(BigDecimal r9_amt) {
		this.r9_amt = r9_amt;
	}
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_amt() {
		return r10_amt;
	}
	public void setR10_amt(BigDecimal r10_amt) {
		this.r10_amt = r10_amt;
	}
	public String getR11_qua_name() {
		return r11_qua_name;
	}
	public void setR11_qua_name(String r11_qua_name) {
		this.r11_qua_name = r11_qua_name;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_amt() {
		return r11_amt;
	}
	public void setR11_amt(BigDecimal r11_amt) {
		this.r11_amt = r11_amt;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_amt() {
		return r12_amt;
	}
	public void setR12_amt(BigDecimal r12_amt) {
		this.r12_amt = r12_amt;
	}
	public String getR18_tot_remu_cur_yr() {
		return r18_tot_remu_cur_yr;
	}
	public void setR18_tot_remu_cur_yr(String r18_tot_remu_cur_yr) {
		this.r18_tot_remu_cur_yr = r18_tot_remu_cur_yr;
	}
	public BigDecimal getR18_unrestricted() {
		return r18_unrestricted;
	}
	public void setR18_unrestricted(BigDecimal r18_unrestricted) {
		this.r18_unrestricted = r18_unrestricted;
	}
	public BigDecimal getR18_deferred() {
		return r18_deferred;
	}
	public void setR18_deferred(BigDecimal r18_deferred) {
		this.r18_deferred = r18_deferred;
	}
	public String getR19_tot_remu_cur_yr() {
		return r19_tot_remu_cur_yr;
	}
	public void setR19_tot_remu_cur_yr(String r19_tot_remu_cur_yr) {
		this.r19_tot_remu_cur_yr = r19_tot_remu_cur_yr;
	}
	public BigDecimal getR19_unrestricted() {
		return r19_unrestricted;
	}
	public void setR19_unrestricted(BigDecimal r19_unrestricted) {
		this.r19_unrestricted = r19_unrestricted;
	}
	public BigDecimal getR19_deferred() {
		return r19_deferred;
	}
	public void setR19_deferred(BigDecimal r19_deferred) {
		this.r19_deferred = r19_deferred;
	}
	public String getR20_tot_remu_cur_yr() {
		return r20_tot_remu_cur_yr;
	}
	public void setR20_tot_remu_cur_yr(String r20_tot_remu_cur_yr) {
		this.r20_tot_remu_cur_yr = r20_tot_remu_cur_yr;
	}
	public BigDecimal getR20_unrestricted() {
		return r20_unrestricted;
	}
	public void setR20_unrestricted(BigDecimal r20_unrestricted) {
		this.r20_unrestricted = r20_unrestricted;
	}
	public BigDecimal getR20_deferred() {
		return r20_deferred;
	}
	public void setR20_deferred(BigDecimal r20_deferred) {
		this.r20_deferred = r20_deferred;
	}
	public String getR21_tot_remu_cur_yr() {
		return r21_tot_remu_cur_yr;
	}
	public void setR21_tot_remu_cur_yr(String r21_tot_remu_cur_yr) {
		this.r21_tot_remu_cur_yr = r21_tot_remu_cur_yr;
	}
	public BigDecimal getR21_unrestricted() {
		return r21_unrestricted;
	}
	public void setR21_unrestricted(BigDecimal r21_unrestricted) {
		this.r21_unrestricted = r21_unrestricted;
	}
	public BigDecimal getR21_deferred() {
		return r21_deferred;
	}
	public void setR21_deferred(BigDecimal r21_deferred) {
		this.r21_deferred = r21_deferred;
	}
	public String getR22_tot_remu_cur_yr() {
		return r22_tot_remu_cur_yr;
	}
	public void setR22_tot_remu_cur_yr(String r22_tot_remu_cur_yr) {
		this.r22_tot_remu_cur_yr = r22_tot_remu_cur_yr;
	}
	public BigDecimal getR22_unrestricted() {
		return r22_unrestricted;
	}
	public void setR22_unrestricted(BigDecimal r22_unrestricted) {
		this.r22_unrestricted = r22_unrestricted;
	}
	public BigDecimal getR22_deferred() {
		return r22_deferred;
	}
	public void setR22_deferred(BigDecimal r22_deferred) {
		this.r22_deferred = r22_deferred;
	}
	public String getR23_tot_remu_cur_yr() {
		return r23_tot_remu_cur_yr;
	}
	public void setR23_tot_remu_cur_yr(String r23_tot_remu_cur_yr) {
		this.r23_tot_remu_cur_yr = r23_tot_remu_cur_yr;
	}
	public BigDecimal getR23_unrestricted() {
		return r23_unrestricted;
	}
	public void setR23_unrestricted(BigDecimal r23_unrestricted) {
		this.r23_unrestricted = r23_unrestricted;
	}
	public BigDecimal getR23_deferred() {
		return r23_deferred;
	}
	public void setR23_deferred(BigDecimal r23_deferred) {
		this.r23_deferred = r23_deferred;
	}
	public String getR24_tot_remu_cur_yr() {
		return r24_tot_remu_cur_yr;
	}
	public void setR24_tot_remu_cur_yr(String r24_tot_remu_cur_yr) {
		this.r24_tot_remu_cur_yr = r24_tot_remu_cur_yr;
	}
	public BigDecimal getR24_unrestricted() {
		return r24_unrestricted;
	}
	public void setR24_unrestricted(BigDecimal r24_unrestricted) {
		this.r24_unrestricted = r24_unrestricted;
	}
	public BigDecimal getR24_deferred() {
		return r24_deferred;
	}
	public void setR24_deferred(BigDecimal r24_deferred) {
		this.r24_deferred = r24_deferred;
	}
	public String getR25_tot_remu_cur_yr() {
		return r25_tot_remu_cur_yr;
	}
	public void setR25_tot_remu_cur_yr(String r25_tot_remu_cur_yr) {
		this.r25_tot_remu_cur_yr = r25_tot_remu_cur_yr;
	}
	public BigDecimal getR25_unrestricted() {
		return r25_unrestricted;
	}
	public void setR25_unrestricted(BigDecimal r25_unrestricted) {
		this.r25_unrestricted = r25_unrestricted;
	}
	public BigDecimal getR25_deferred() {
		return r25_deferred;
	}
	public void setR25_deferred(BigDecimal r25_deferred) {
		this.r25_deferred = r25_deferred;
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

		

// =====================================================
// ARCHIVAL  SUMAMRY ENTITY 
// =====================================================


public class OPER_RISK_DIS_Archival_Summary_RowMapper
        implements RowMapper<OPER_RISK_DIS_Archival_Summary_Entity> {

    @Override
    public OPER_RISK_DIS_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        OPER_RISK_DIS_Archival_Summary_Entity obj = new OPER_RISK_DIS_Archival_Summary_Entity();

 // =========================
// R3
// =========================
obj.setR3_qua_name(rs.getString("r3_qua_name"));
obj.setR3_product(rs.getString("r3_product"));
obj.setR3_amt(rs.getBigDecimal("r3_amt"));

// =========================
// R4
// =========================
obj.setR4_qua_name(rs.getString("r4_qua_name"));
obj.setR4_product(rs.getString("r4_product"));
obj.setR4_amt(rs.getBigDecimal("r4_amt"));

// =========================
// R9
// =========================
obj.setR9_qua_name(rs.getString("r9_qua_name"));
obj.setR9_product(rs.getString("r9_product"));
obj.setR9_amt(rs.getBigDecimal("r9_amt"));

// =========================
// R10
// =========================
obj.setR10_product(rs.getString("r10_product"));
obj.setR10_amt(rs.getBigDecimal("r10_amt"));

// =========================
// R11
// =========================
obj.setR11_qua_name(rs.getString("r11_qua_name"));
obj.setR11_product(rs.getString("r11_product"));
obj.setR11_amt(rs.getBigDecimal("r11_amt"));

// =========================
// R12
// =========================
obj.setR12_product(rs.getString("r12_product"));
obj.setR12_amt(rs.getBigDecimal("r12_amt"));

// =========================
// R18
// =========================
obj.setR18_tot_remu_cur_yr(rs.getString("r18_tot_remu_cur_yr"));
obj.setR18_unrestricted(rs.getBigDecimal("r18_unrestricted"));
obj.setR18_deferred(rs.getBigDecimal("r18_deferred"));

// =========================
// R19
// =========================
obj.setR19_tot_remu_cur_yr(rs.getString("r19_tot_remu_cur_yr"));
obj.setR19_unrestricted(rs.getBigDecimal("r19_unrestricted"));
obj.setR19_deferred(rs.getBigDecimal("r19_deferred"));

// =========================
// R20
// =========================
obj.setR20_tot_remu_cur_yr(rs.getString("r20_tot_remu_cur_yr"));
obj.setR20_unrestricted(rs.getBigDecimal("r20_unrestricted"));
obj.setR20_deferred(rs.getBigDecimal("r20_deferred"));

// =========================
// R21
// =========================
obj.setR21_tot_remu_cur_yr(rs.getString("r21_tot_remu_cur_yr"));
obj.setR21_unrestricted(rs.getBigDecimal("r21_unrestricted"));
obj.setR21_deferred(rs.getBigDecimal("r21_deferred"));

// =========================
// R22
// =========================
obj.setR22_tot_remu_cur_yr(rs.getString("r22_tot_remu_cur_yr"));
obj.setR22_unrestricted(rs.getBigDecimal("r22_unrestricted"));
obj.setR22_deferred(rs.getBigDecimal("r22_deferred"));

// =========================
// R23
// =========================
obj.setR23_tot_remu_cur_yr(rs.getString("r23_tot_remu_cur_yr"));
obj.setR23_unrestricted(rs.getBigDecimal("r23_unrestricted"));
obj.setR23_deferred(rs.getBigDecimal("r23_deferred"));

// =========================
// R24
// =========================
obj.setR24_tot_remu_cur_yr(rs.getString("r24_tot_remu_cur_yr"));
obj.setR24_unrestricted(rs.getBigDecimal("r24_unrestricted"));
obj.setR24_deferred(rs.getBigDecimal("r24_deferred"));

// =========================
// R25
// =========================
obj.setR25_tot_remu_cur_yr(rs.getString("r25_tot_remu_cur_yr"));
obj.setR25_unrestricted(rs.getBigDecimal("r25_unrestricted"));
obj.setR25_deferred(rs.getBigDecimal("r25_deferred"));

        // =========================
        // COMMON FIELDS
        // =========================
        obj.setReport_date(rs.getDate("report_date"));
        obj.setReport_version(rs.getBigDecimal("report_version"));
        obj.setReportResubDate(rs.getDate("report_resubdate"));

        obj.setReport_frequency(rs.getString("report_frequency"));
        obj.setReport_code(rs.getString("report_code"));
        obj.setReport_desc(rs.getString("report_desc"));

        obj.setEntity_flg(rs.getString("entity_flg"));
        obj.setModify_flg(rs.getString("modify_flg"));
        obj.setDel_flg(rs.getString("del_flg"));

        return obj;
    }
}


public class OPER_RISK_DIS_Archival_Summary_Entity {
	
	

		
	
		private String	r3_qua_name ;
	private String	r3_product;
	private BigDecimal	r3_amt;

	private String	r4_qua_name ;
	private String	r4_product;
	private BigDecimal	r4_amt;

	private String	r9_qua_name ;
	private String	r9_product;
	private BigDecimal	r9_amt;

	private String	r10_product;
	private BigDecimal	r10_amt;

	private String	r11_qua_name ;
	private String	r11_product;
	private BigDecimal	r11_amt;

	private String	r12_product;
	private BigDecimal	r12_amt;

	private String	r18_tot_remu_cur_yr;
	private BigDecimal	r18_unrestricted;
	private BigDecimal	r18_deferred;
	private String	r19_tot_remu_cur_yr;
	private BigDecimal	r19_unrestricted;
	private BigDecimal	r19_deferred;
	private String	r20_tot_remu_cur_yr;
	private BigDecimal	r20_unrestricted;
	private BigDecimal	r20_deferred;
	private String	r21_tot_remu_cur_yr;
	private BigDecimal	r21_unrestricted;
	private BigDecimal	r21_deferred;
	private String	r22_tot_remu_cur_yr;
	private BigDecimal	r22_unrestricted;
	private BigDecimal	r22_deferred;
	private String	r23_tot_remu_cur_yr;
	private BigDecimal	r23_unrestricted;
	private BigDecimal	r23_deferred;
	private String	r24_tot_remu_cur_yr;
	private BigDecimal	r24_unrestricted;
	private BigDecimal	r24_deferred;
	private String	r25_tot_remu_cur_yr;
	private BigDecimal	r25_unrestricted;
	private BigDecimal	r25_deferred;
	               
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
	
	public String getR3_qua_name() {
		return r3_qua_name;
	}
	public void setR3_qua_name(String r3_qua_name) {
		this.r3_qua_name = r3_qua_name;
	}
	public String getR3_product() {
		return r3_product;
	}
	public void setR3_product(String r3_product) {
		this.r3_product = r3_product;
	}
	public BigDecimal getR3_amt() {
		return r3_amt;
	}
	public void setR3_amt(BigDecimal r3_amt) {
		this.r3_amt = r3_amt;
	}
	public String getR4_qua_name() {
		return r4_qua_name;
	}
	public void setR4_qua_name(String r4_qua_name) {
		this.r4_qua_name = r4_qua_name;
	}
	public String getR4_product() {
		return r4_product;
	}
	public void setR4_product(String r4_product) {
		this.r4_product = r4_product;
	}
	public BigDecimal getR4_amt() {
		return r4_amt;
	}
	public void setR4_amt(BigDecimal r4_amt) {
		this.r4_amt = r4_amt;
	}
	public String getR9_qua_name() {
		return r9_qua_name;
	}
	public void setR9_qua_name(String r9_qua_name) {
		this.r9_qua_name = r9_qua_name;
	}
	public String getR9_product() {
		return r9_product;
	}
	public void setR9_product(String r9_product) {
		this.r9_product = r9_product;
	}
	public BigDecimal getR9_amt() {
		return r9_amt;
	}
	public void setR9_amt(BigDecimal r9_amt) {
		this.r9_amt = r9_amt;
	}
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_amt() {
		return r10_amt;
	}
	public void setR10_amt(BigDecimal r10_amt) {
		this.r10_amt = r10_amt;
	}
	public String getR11_qua_name() {
		return r11_qua_name;
	}
	public void setR11_qua_name(String r11_qua_name) {
		this.r11_qua_name = r11_qua_name;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_amt() {
		return r11_amt;
	}
	public void setR11_amt(BigDecimal r11_amt) {
		this.r11_amt = r11_amt;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_amt() {
		return r12_amt;
	}
	public void setR12_amt(BigDecimal r12_amt) {
		this.r12_amt = r12_amt;
	}
	public String getR18_tot_remu_cur_yr() {
		return r18_tot_remu_cur_yr;
	}
	public void setR18_tot_remu_cur_yr(String r18_tot_remu_cur_yr) {
		this.r18_tot_remu_cur_yr = r18_tot_remu_cur_yr;
	}
	public BigDecimal getR18_unrestricted() {
		return r18_unrestricted;
	}
	public void setR18_unrestricted(BigDecimal r18_unrestricted) {
		this.r18_unrestricted = r18_unrestricted;
	}
	public BigDecimal getR18_deferred() {
		return r18_deferred;
	}
	public void setR18_deferred(BigDecimal r18_deferred) {
		this.r18_deferred = r18_deferred;
	}
	public String getR19_tot_remu_cur_yr() {
		return r19_tot_remu_cur_yr;
	}
	public void setR19_tot_remu_cur_yr(String r19_tot_remu_cur_yr) {
		this.r19_tot_remu_cur_yr = r19_tot_remu_cur_yr;
	}
	public BigDecimal getR19_unrestricted() {
		return r19_unrestricted;
	}
	public void setR19_unrestricted(BigDecimal r19_unrestricted) {
		this.r19_unrestricted = r19_unrestricted;
	}
	public BigDecimal getR19_deferred() {
		return r19_deferred;
	}
	public void setR19_deferred(BigDecimal r19_deferred) {
		this.r19_deferred = r19_deferred;
	}
	public String getR20_tot_remu_cur_yr() {
		return r20_tot_remu_cur_yr;
	}
	public void setR20_tot_remu_cur_yr(String r20_tot_remu_cur_yr) {
		this.r20_tot_remu_cur_yr = r20_tot_remu_cur_yr;
	}
	public BigDecimal getR20_unrestricted() {
		return r20_unrestricted;
	}
	public void setR20_unrestricted(BigDecimal r20_unrestricted) {
		this.r20_unrestricted = r20_unrestricted;
	}
	public BigDecimal getR20_deferred() {
		return r20_deferred;
	}
	public void setR20_deferred(BigDecimal r20_deferred) {
		this.r20_deferred = r20_deferred;
	}
	public String getR21_tot_remu_cur_yr() {
		return r21_tot_remu_cur_yr;
	}
	public void setR21_tot_remu_cur_yr(String r21_tot_remu_cur_yr) {
		this.r21_tot_remu_cur_yr = r21_tot_remu_cur_yr;
	}
	public BigDecimal getR21_unrestricted() {
		return r21_unrestricted;
	}
	public void setR21_unrestricted(BigDecimal r21_unrestricted) {
		this.r21_unrestricted = r21_unrestricted;
	}
	public BigDecimal getR21_deferred() {
		return r21_deferred;
	}
	public void setR21_deferred(BigDecimal r21_deferred) {
		this.r21_deferred = r21_deferred;
	}
	public String getR22_tot_remu_cur_yr() {
		return r22_tot_remu_cur_yr;
	}
	public void setR22_tot_remu_cur_yr(String r22_tot_remu_cur_yr) {
		this.r22_tot_remu_cur_yr = r22_tot_remu_cur_yr;
	}
	public BigDecimal getR22_unrestricted() {
		return r22_unrestricted;
	}
	public void setR22_unrestricted(BigDecimal r22_unrestricted) {
		this.r22_unrestricted = r22_unrestricted;
	}
	public BigDecimal getR22_deferred() {
		return r22_deferred;
	}
	public void setR22_deferred(BigDecimal r22_deferred) {
		this.r22_deferred = r22_deferred;
	}
	public String getR23_tot_remu_cur_yr() {
		return r23_tot_remu_cur_yr;
	}
	public void setR23_tot_remu_cur_yr(String r23_tot_remu_cur_yr) {
		this.r23_tot_remu_cur_yr = r23_tot_remu_cur_yr;
	}
	public BigDecimal getR23_unrestricted() {
		return r23_unrestricted;
	}
	public void setR23_unrestricted(BigDecimal r23_unrestricted) {
		this.r23_unrestricted = r23_unrestricted;
	}
	public BigDecimal getR23_deferred() {
		return r23_deferred;
	}
	public void setR23_deferred(BigDecimal r23_deferred) {
		this.r23_deferred = r23_deferred;
	}
	public String getR24_tot_remu_cur_yr() {
		return r24_tot_remu_cur_yr;
	}
	public void setR24_tot_remu_cur_yr(String r24_tot_remu_cur_yr) {
		this.r24_tot_remu_cur_yr = r24_tot_remu_cur_yr;
	}
	public BigDecimal getR24_unrestricted() {
		return r24_unrestricted;
	}
	public void setR24_unrestricted(BigDecimal r24_unrestricted) {
		this.r24_unrestricted = r24_unrestricted;
	}
	public BigDecimal getR24_deferred() {
		return r24_deferred;
	}
	public void setR24_deferred(BigDecimal r24_deferred) {
		this.r24_deferred = r24_deferred;
	}
	public String getR25_tot_remu_cur_yr() {
		return r25_tot_remu_cur_yr;
	}
	public void setR25_tot_remu_cur_yr(String r25_tot_remu_cur_yr) {
		this.r25_tot_remu_cur_yr = r25_tot_remu_cur_yr;
	}
	public BigDecimal getR25_unrestricted() {
		return r25_unrestricted;
	}
	public void setR25_unrestricted(BigDecimal r25_unrestricted) {
		this.r25_unrestricted = r25_unrestricted;
	}
	public BigDecimal getR25_deferred() {
		return r25_deferred;
	}
	public void setR25_deferred(BigDecimal r25_deferred) {
		this.r25_deferred = r25_deferred;
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
	
	public Date getReportResubDate() {
		return reportResubDate;
	}
	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
	}
	
}
	


// =====================================================
// DETAIL ENTITY  OPER_RISK_DIS
// =====================================================	

public class OPER_RISK_DIS_Detail_RowMapper implements RowMapper<OPER_RISK_DIS_Detail_Entity> {

    @Override
    public OPER_RISK_DIS_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        OPER_RISK_DIS_Detail_Entity obj = new OPER_RISK_DIS_Detail_Entity();

        // =========================
        // BASIC DETAILS
        // =========================
        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));
        obj.setDataType(rs.getString("DATA_TYPE"));

        // =========================
        // REPORT DETAILS
        // =========================
        obj.setReportName(rs.getString("REPORT_NAME"));
        obj.setReportLabel(rs.getString("REPORT_LABEL"));
        obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

        // =========================
        // AMOUNT
        // =========================
        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

        // =========================
        // DATE FIELDS
        // =========================
        obj.setReportDate(rs.getDate("REPORT_DATE"));
        obj.setCreateTime(rs.getDate("CREATE_TIME"));
        obj.setModifyTime(rs.getDate("MODIFY_TIME"));
        obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

        // =========================
        // USER INFO
        // =========================
        obj.setCreateUser(rs.getString("CREATE_USER"));
        obj.setModifyUser(rs.getString("MODIFY_USER"));
        obj.setVerifyUser(rs.getString("VERIFY_USER"));

        // =========================
        // FLAGS (char)
        // =========================
        obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');
        obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');
        obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

        return obj;
    }
}

public class OPER_RISK_DIS_Detail_Entity {

   
	
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
  private String reportAddlCriteria_1;
  
 
  
  @Column(name = "REPORT_REMARKS")
  private String reportRemarks;
  


  @Column(name = "MODIFICATION_REMARKS")
  private String modificationRemarks;

  @Column(name = "DATA_ENTRY_VERSION")
  private String dataEntryVersion;

  @Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
  private BigDecimal acctBalanceInpula;


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

public String getReportAddlCriteria_1() {
	return reportAddlCriteria_1;
}

public void setReportAddlCriteria_1(String reportAddlCriteria_1) {
	this.reportAddlCriteria_1 = reportAddlCriteria_1;
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


}

			  

// =====================================================
// ARCHIVAL  DETAIL ENTITY 
// =====================================================


public class OPER_RISK_DIS_Archival_Detail_RowMapper 
        implements RowMapper<OPER_RISK_DIS_Archival_Detail_Entity> {

    @Override
    public OPER_RISK_DIS_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        OPER_RISK_DIS_Archival_Detail_Entity obj = new OPER_RISK_DIS_Archival_Detail_Entity();

        // =========================
        // BASIC DETAILS
        // =========================
        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));
        obj.setDataType(rs.getString("DATA_TYPE"));

        // =========================
        // REPORT DETAILS
        // =========================
        obj.setReportName(rs.getString("REPORT_NAME"));
        obj.setReportLabel(rs.getString("REPORT_LABEL"));
        obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

        // =========================
        // AMOUNT
        // =========================
        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

        // =========================
        // DATE FIELDS
        // =========================
        obj.setReportDate(rs.getDate("REPORT_DATE"));
        obj.setCreateTime(rs.getDate("CREATE_TIME"));
        obj.setModifyTime(rs.getDate("MODIFY_TIME"));
        obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

        // =========================
        // USER INFO
        // =========================
        obj.setCreateUser(rs.getString("CREATE_USER"));
        obj.setModifyUser(rs.getString("MODIFY_USER"));
        obj.setVerifyUser(rs.getString("VERIFY_USER"));

        // =========================
        // FLAGS (char)
        // =========================
        obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');
        obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');
        obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

        return obj;
    }
}

public class OPER_RISK_DIS_Archival_Detail_Entity {

   
	
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
  private String reportAddlCriteria_1;
  
 
  
  @Column(name = "REPORT_REMARKS")
  private String reportRemarks;
  


  @Column(name = "MODIFICATION_REMARKS")
  private String modificationRemarks;

  @Column(name = "DATA_ENTRY_VERSION")
  private String dataEntryVersion;

  @Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
  private BigDecimal acctBalanceInpula;


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

public String getReportAddlCriteria_1() {
	return reportAddlCriteria_1;
}

public void setReportAddlCriteria_1(String reportAddlCriteria_1) {
	this.reportAddlCriteria_1 = reportAddlCriteria_1;
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


}


//=====================================================
// MODEL AND VIEW METHOD summary OPER_RISK_DIS
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
		 		 	 public ModelAndView getOPER_RISK_DISView(

	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("OPER_RISK_DIS View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    // =====================================================
	    // ARCHIVAL MODE
	    // =====================================================

	    if ("ARCHIVAL".equals(type) && version != null) {

	        List<OPER_RISK_DIS_Archival_Summary_Entity> T1Master = new ArrayList<>();
	    

	        try {
	            Date dt = dateformat.parse(todate);
	            
	        
	            // ============================
	            // SUMMARY ARCHIVAL
	            // ============================
	            T1Master = getDataByDateListArchival(dt, version);

	            System.out.println("Archival Summary size = " + T1Master.size());

	            

	            mv.addObject("report_date", dateformat.format(dt));

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        mv.addObject("reportsummary", T1Master);
	       
	    }
	    // =====================================================
	    // NORMAL MODE
	    // =====================================================

	    else {

	        List<OPER_RISK_DIS_Summary_Entity> T1Master = new ArrayList<>();
	       

	        try {
	            Date dt = dateformat.parse(todate);

	            // SUMMARY NORMAL
	            T1Master = getSummaryDataByDate(dt);

	            System.out.println("Summary size = " + T1Master.size());


	            mv.addObject("report_date", dateformat.format(dt));

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        mv.addObject("reportsummary", T1Master);
	      
	    }

	    // =====================================================
	    // VIEW SETTINGS
	    // =====================================================

	    mv.setViewName("BRRS/OPER_RISK_DIS");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getOPER_RISK_DIScurrentDtl(
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

	            List<OPER_RISK_DIS_Archival_Detail_Entity> archivalDetailList;

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
	                		getarchivaldetaildatabydateList(
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

	            List<OPER_RISK_DIS_Detail_Entity> currentDetailList;

	            if (reportLabel != null && reportAddlCriteria1 != null) {

	                currentDetailList =
	                       getdetailDataByRowIdAndColumnId(
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

	    mv.setViewName("BRRS/OPER_RISK_DIS");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
	
//=====================================================
// MODEL AND VIEW METHOD ARCHIVAL VIEW
//=====================================================

public List<Object[]> getOPER_RISK_DISArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<OPER_RISK_DIS_Archival_Summary_Entity> repoData =
						getarchivaldatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (OPER_RISK_DIS_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					OPER_RISK_DIS_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  OPER_RISK_DIS  Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}
//=====================================================
// UPDATE REPORT
//=====================================================

//=====================================================
// VIEW AND EDIT
//=====================================================

public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/OPER_RISK_DIS"); 

		if (acctNo != null) {
			OPER_RISK_DIS_Detail_Entity oper_risk_disEntity = findByDetailAcctnumber(acctNo);
			if (oper_risk_disEntity != null && oper_risk_disEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(oper_risk_disEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("oper_risk_disData", oper_risk_disEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}
	
//=====================================================
// UPDATEDETAIL
//=====================================================

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("acctBalanceInpula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			OPER_RISK_DIS_Detail_Entity existing = findByDetailAcctnumber(acctNo);
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

			 if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {
		            BigDecimal newacctBalanceInpula = new BigDecimal(acctBalanceInpula);
		            if (existing.getAcctBalanceInpula()  == null ||
		                existing.getAcctBalanceInpula().compareTo(newacctBalanceInpula) != 0) {
		            	 existing.setAcctBalanceInpula(newacctBalanceInpula);
		                isChanged = true;
		                logger.info("Balance updated to {}", newacctBalanceInpula);
		            }
		        }
			 
			 
			
		        
			if (isChanged) {
				  String sql =
    "UPDATE BRRS_OPER_RISK_DIS_DETAILTABLE " +
    "SET ACCT_NAME = ?, " +
    "ACCT_BALANCE_IN_PULA = ?, " +
   
    "WHERE ACCT_NUMBER = ?";

		           jdbcTemplate.update(
    sql,
    existing.getAcctName(),
    existing.getAcctBalanceInpula(),
  
    existing.getAcctNumber()
);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_OPER_RISK_DIS_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_OPER_RISK_DIS_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating OPER_RISK_DIS  record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
//=====================================================
// DETAIL EXCEL 
//=====================================================


	public byte[] getOPER_RISK_DISDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  OPER_RISK_DIS  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getOPER_RISK_DISDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("OPER_RISK_DIS Details ");

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
//Header row
String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABEL", "REPORT ADDL CRITERIA1", "REPORT_DATE" };


				XSSFRow headerRow = sheet.createRow(0);
				for (int i = 0; i < headers.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(headers[i]);

				
				if (i == 3 ) {  // ACCT BALANCE 
				    cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
				    cell.setCellStyle(headerStyle);
				}

					sheet.setColumnWidth(i, 5000);
				}

				// Get data
				Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
				List<OPER_RISK_DIS_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (OPER_RISK_DIS_Detail_Entity item : reportData) { 
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
					logger.info("No data found for OPER_RISK_DIS — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating OPER_RISK_DIS Excel", e);
				return new byte[0];
			}
		}
	
	
//=====================================================
// ARCHIVAL DETAIL EXCEL 
//=====================================================

	public byte[] getOPER_RISK_DISDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for OPER_RISK_DIS ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("OPER_RISK_DIS Detail NEW");

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
					String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE",  "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

				XSSFRow headerRow = sheet.createRow(0);
				for (int i = 0; i < headers.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(headers[i]);

						if (i == 3 ) {  // MONTHLY_INT (3) and CREDIT_EQUIVALENT (4) nd DEBIT_EQUIVALENT(5)
				    cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
				    cell.setCellStyle(headerStyle);
				}

					sheet.setColumnWidth(i, 5000);
				}

	// Get data
				Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
				List<OPER_RISK_DIS_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (OPER_RISK_DIS_Archival_Detail_Entity item : reportData) {
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
					logger.info("No data found for OPER_RISK_DIS — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating OPER_RISK_DIS NEW Excel", e);
				return new byte[0];
			}
		}
		
		
//=====================================================
// Summary EXCEL 
//=====================================================

	public byte[] getOPER_RISK_DISExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.OPER_RISK_DIS");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelOPER_RISK_DISARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<OPER_RISK_DIS_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		
		
			System.out.println("DATA SIZE IS : "+dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  OPER_RISK_DIS report. Returning empty result.");
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
						OPER_RISK_DIS_Summary_Entity record = dataList.get(i);
					
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

		       // R3
					row = sheet.getRow(2);
					Cell  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR3_amt() != null ? record.getR3_amt().doubleValue() : 0);

					// R4
					row = sheet.getRow(3);
					  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR4_amt() != null ? record.getR4_amt().doubleValue() : 0);
					 
					// R9
					row = sheet.getRow(8);
					  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR9_amt() != null ? record.getR9_amt().doubleValue() : 0);
					
					// R10
					row = sheet.getRow(9);
					  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR10_amt() != null ? record.getR10_amt().doubleValue() : 0);
					
					
					// R11
					row = sheet.getRow(10);
					  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR11_amt() != null ? record.getR11_amt().doubleValue() : 0);
					
					
					// R12
					row = sheet.getRow(11);
					  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR12_amt() != null ? record.getR12_amt().doubleValue() : 0);
					
					
					// R18 B
					row = sheet.getRow(17);
					Cell  cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR18_unrestricted() != null ? record.getR18_unrestricted().doubleValue() : 0);



					// R18 C
					row = sheet.getRow(17);
					  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR18_deferred() != null ? record.getR18_deferred().doubleValue() : 0);
					
					
					// R19 B
					row = sheet.getRow(18);
				 cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR19_unrestricted() != null ? record.getR19_unrestricted().doubleValue() : 0);

					// R19 C
					row = sheet.getRow(18);
					 cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR19_deferred() != null ? record.getR19_deferred().doubleValue() : 0);
					
					
					
					
					// R20 B
					row = sheet.getRow(19);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR20_unrestricted() != null ? record.getR20_unrestricted().doubleValue() : 0);

					// R20 C
					row = sheet.getRow(19);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR20_deferred() != null ? record.getR20_deferred().doubleValue() : 0);

					
					
					// R21 B
					row = sheet.getRow(20);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR21_unrestricted() != null ? record.getR21_unrestricted().doubleValue() : 0);

					// R21 C
					row = sheet.getRow(20);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR21_deferred() != null ? record.getR21_deferred().doubleValue() : 0);

					
					
					// R22 B
					row = sheet.getRow(21);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR22_unrestricted() != null ? record.getR22_unrestricted().doubleValue() : 0);

					// R22 C
					row = sheet.getRow(21);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR22_deferred() != null ? record.getR22_deferred().doubleValue() : 0);

					
					
					// R23 B
					row = sheet.getRow(22);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR23_unrestricted() != null ? record.getR23_unrestricted().doubleValue() : 0);

					// R23 C
					row = sheet.getRow(22);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR23_deferred() != null ? record.getR23_deferred().doubleValue() : 0);

					
					
					// R24 B
					row = sheet.getRow(23);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR24_unrestricted() != null ? record.getR24_unrestricted().doubleValue() : 0);

					// R24 C
					row = sheet.getRow(23);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR24_deferred() != null ? record.getR24_deferred().doubleValue() : 0);

					
					// R25 B
					row = sheet.getRow(24);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR25_unrestricted() != null ? record.getR25_unrestricted().doubleValue() : 0);

					// R25 C
					row = sheet.getRow(24);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR25_deferred() != null ? record.getR25_deferred().doubleValue() : 0);

					
				
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




//=====================================================
//ARCHIVAL SUMMARY EXCEL 
//=====================================================



				public byte[] getExcelOPER_RISK_DISARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {	

			}

			List<OPER_RISK_DIS_Archival_Summary_Entity> dataList = 
					getDataByDateListArchival(dateformat.parse(todate), version);
		
	    

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for OPER_RISK_DIS new report. Returning empty result.");
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
						OPER_RISK_DIS_Archival_Summary_Entity record = dataList.get(i);
					    
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


	
					// R3
					row = sheet.getRow(2);
					Cell  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR3_amt() != null ? record.getR3_amt().doubleValue() : 0);

					// R4
					row = sheet.getRow(3);
					  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR4_amt() != null ? record.getR4_amt().doubleValue() : 0);
					 
					// R9
					row = sheet.getRow(8);
					  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR9_amt() != null ? record.getR9_amt().doubleValue() : 0);
					
					// R10
					row = sheet.getRow(9);
					  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR10_amt() != null ? record.getR10_amt().doubleValue() : 0);
					
					
					// R11
					row = sheet.getRow(10);
					  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR11_amt() != null ? record.getR11_amt().doubleValue() : 0);
					
					
					// R12
					row = sheet.getRow(11);
					  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR12_amt() != null ? record.getR12_amt().doubleValue() : 0);
					
					
					// R18 B
					row = sheet.getRow(17);
					Cell  cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR18_unrestricted() != null ? record.getR18_unrestricted().doubleValue() : 0);



					// R18 C
					row = sheet.getRow(17);
					  cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR18_deferred() != null ? record.getR18_deferred().doubleValue() : 0);
					
					
					// R19 B
					row = sheet.getRow(18);
				 cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR19_unrestricted() != null ? record.getR19_unrestricted().doubleValue() : 0);

					// R19 C
					row = sheet.getRow(18);
					 cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR19_deferred() != null ? record.getR19_deferred().doubleValue() : 0);
					
					
					
					
					// R20 B
					row = sheet.getRow(19);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR20_unrestricted() != null ? record.getR20_unrestricted().doubleValue() : 0);

					// R20 C
					row = sheet.getRow(19);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR20_deferred() != null ? record.getR20_deferred().doubleValue() : 0);

					
					
					// R21 B
					row = sheet.getRow(20);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR21_unrestricted() != null ? record.getR21_unrestricted().doubleValue() : 0);

					// R21 C
					row = sheet.getRow(20);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR21_deferred() != null ? record.getR21_deferred().doubleValue() : 0);

					
					
					// R22 B
					row = sheet.getRow(21);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR22_unrestricted() != null ? record.getR22_unrestricted().doubleValue() : 0);

					// R22 C
					row = sheet.getRow(21);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR22_deferred() != null ? record.getR22_deferred().doubleValue() : 0);

					
					
					// R23 B
					row = sheet.getRow(22);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR23_unrestricted() != null ? record.getR23_unrestricted().doubleValue() : 0);

					// R23 C
					row = sheet.getRow(22);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR23_deferred() != null ? record.getR23_deferred().doubleValue() : 0);

					
					
					// R24 B
					row = sheet.getRow(23);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR24_unrestricted() != null ? record.getR24_unrestricted().doubleValue() : 0);

					// R24 C
					row = sheet.getRow(23);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR24_deferred() != null ? record.getR24_deferred().doubleValue() : 0);

					
					// R25 B
					row = sheet.getRow(24);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					cellB.setCellValue(record.getR25_unrestricted() != null ? record.getR25_unrestricted().doubleValue() : 0);

					// R25 C
					row = sheet.getRow(24);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR25_deferred() != null ? record.getR25_deferred().doubleValue() : 0);
					


				
						
						
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
