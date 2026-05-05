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

public class BRRS_DEFERRED_TAX_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_DEFERRED_TAX_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

  
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;
	
	
	
	// Fetch data by report date - Deferred Tax Summary
	
public List<DEFERRED_TAX_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_DEFERRED_TAX_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new DEFERRED_TAX_RowMapper()   // make sure this RowMapper exists
    );
}

// ARCHIVAL 

public List<Object[]> get_DTAXArchival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_DEFERRED_TAX_ARCHIVALTABLE_SUMMARY ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<DEFERRED_TAX_Archival_Summary_Entity> getDataByDateListArchival(Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_DEFERRED_TAX_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new DEFERRED_TAX_Archival_RowMapper()
    );
}

public List<DEFERRED_TAX_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_DEFERRED_TAX_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new DEFERRED_TAX_Archival_RowMapper()
    );
}

// DETAIL 

public List<DEFERRED_TAX_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_DEFERRED_TAX_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new DEFERRED_TAX_Detail_RowMapper()
    );
}

public List<DEFERRED_TAX_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_DEFERRED_TAX_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, offset, limit},
            new DEFERRED_TAX_Detail_RowMapper()
    );
}

public int getDataCount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_DEFERRED_TAX_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            Integer.class
    );
}


public List<DEFERRED_TAX_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate) {

    String sql = "SELECT * FROM BRRS_DEFERRED_TAX_DETAILTABLE " +
                 "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate},
            new DEFERRED_TAX_Detail_RowMapper()
    );
}

public DEFERRED_TAX_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_DEFERRED_TAX_DETAILTABLE WHERE ACCT_NUMBER = ?";

    List<DEFERRED_TAX_Detail_Entity> list = jdbcTemplate.query(
            sql,
            new Object[]{acctNumber},
            new DEFERRED_TAX_Detail_RowMapper()
    );

    return list.isEmpty() ? null : list.get(0);
}

// ARCHIVAL DETAIL 

public List<DEFERRED_TAX_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_DEFERRED_TAX_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, dataEntryVersion},
            new DEFERRED_TAX_Archival_Detail_RowMapper()
    );
}


public List<DEFERRED_TAX_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_DEFERRED_TAX_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_LABEL = ? " +
                 "AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ? " +
                 "AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate, dataEntryVersion},
            new DEFERRED_TAX_Archival_Detail_RowMapper()
    );
}

// SUMAMRY ENTITY CLASS

public class DEFERRED_TAX_RowMapper 
        implements RowMapper<DEFERRED_TAX_Summary_Entity> {

    @Override
    public DEFERRED_TAX_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        DEFERRED_TAX_Summary_Entity obj = new DEFERRED_TAX_Summary_Entity();

        // =========================
        // R11
        // =========================
        obj.setR11_product(rs.getString("r11_product"));
        obj.setR11_31_asse_lc(rs.getBigDecimal("r11_31_asse_lc"));
        obj.setR11_31_asse_inr(rs.getBigDecimal("r11_31_asse_inr"));
        obj.setR11_31_liab_lc(rs.getBigDecimal("r11_31_liab_lc"));
        obj.setR11_31_liab_inr(rs.getBigDecimal("r11_31_liab_inr"));
        obj.setR11_30_asse_lc(rs.getBigDecimal("r11_30_asse_lc"));
        obj.setR11_30_asse_inr(rs.getBigDecimal("r11_30_asse_inr"));
        obj.setR11_30_liab_lc(rs.getBigDecimal("r11_30_liab_lc"));
        obj.setR11_30_liab_inr(rs.getBigDecimal("r11_30_liab_inr"));

        // =========================
        // R12
        // =========================
        obj.setR12_product(rs.getString("r12_product"));
        obj.setR12_31_asse_lc(rs.getBigDecimal("r12_31_asse_lc"));
        obj.setR12_31_asse_inr(rs.getBigDecimal("r12_31_asse_inr"));
        obj.setR12_31_liab_lc(rs.getBigDecimal("r12_31_liab_lc"));
        obj.setR12_31_liab_inr(rs.getBigDecimal("r12_31_liab_inr"));
        obj.setR12_30_asse_lc(rs.getBigDecimal("r12_30_asse_lc"));
        obj.setR12_30_asse_inr(rs.getBigDecimal("r12_30_asse_inr"));
        obj.setR12_30_liab_lc(rs.getBigDecimal("r12_30_liab_lc"));
        obj.setR12_30_liab_inr(rs.getBigDecimal("r12_30_liab_inr"));

        // =========================
        // R13
        // =========================
        obj.setR13_product(rs.getString("r13_product"));
        obj.setR13_31_asse_lc(rs.getBigDecimal("r13_31_asse_lc"));
        obj.setR13_31_asse_inr(rs.getBigDecimal("r13_31_asse_inr"));
        obj.setR13_31_liab_lc(rs.getBigDecimal("r13_31_liab_lc"));
        obj.setR13_31_liab_inr(rs.getBigDecimal("r13_31_liab_inr"));
        obj.setR13_30_asse_lc(rs.getBigDecimal("r13_30_asse_lc"));
        obj.setR13_30_asse_inr(rs.getBigDecimal("r13_30_asse_inr"));
        obj.setR13_30_liab_lc(rs.getBigDecimal("r13_30_liab_lc"));
        obj.setR13_30_liab_inr(rs.getBigDecimal("r13_30_liab_inr"));

        // =========================
        // R14
        // =========================
        obj.setR14_product(rs.getString("r14_product"));
        obj.setR14_31_asse_lc(rs.getBigDecimal("r14_31_asse_lc"));
        obj.setR14_31_asse_inr(rs.getBigDecimal("r14_31_asse_inr"));
        obj.setR14_31_liab_lc(rs.getBigDecimal("r14_31_liab_lc"));
        obj.setR14_31_liab_inr(rs.getBigDecimal("r14_31_liab_inr"));
        obj.setR14_30_asse_lc(rs.getBigDecimal("r14_30_asse_lc"));
        obj.setR14_30_asse_inr(rs.getBigDecimal("r14_30_asse_inr"));
        obj.setR14_30_liab_lc(rs.getBigDecimal("r14_30_liab_lc"));
        obj.setR14_30_liab_inr(rs.getBigDecimal("r14_30_liab_inr"));

        // =========================
        // R15
        // =========================
        obj.setR15_product(rs.getString("r15_product"));
        obj.setR15_31_asse_lc(rs.getBigDecimal("r15_31_asse_lc"));
        obj.setR15_31_asse_inr(rs.getBigDecimal("r15_31_asse_inr"));
        obj.setR15_31_liab_lc(rs.getBigDecimal("r15_31_liab_lc"));
        obj.setR15_31_liab_inr(rs.getBigDecimal("r15_31_liab_inr"));
        obj.setR15_30_asse_lc(rs.getBigDecimal("r15_30_asse_lc"));
        obj.setR15_30_asse_inr(rs.getBigDecimal("r15_30_asse_inr"));
        obj.setR15_30_liab_lc(rs.getBigDecimal("r15_30_liab_lc"));
        obj.setR15_30_liab_inr(rs.getBigDecimal("r15_30_liab_inr"));

// =========================
// R16
// =========================
obj.setR16_product(rs.getString("r16_product"));
obj.setR16_31_asse_lc(rs.getBigDecimal("r16_31_asse_lc"));
obj.setR16_31_asse_inr(rs.getBigDecimal("r16_31_asse_inr"));
obj.setR16_31_liab_lc(rs.getBigDecimal("r16_31_liab_lc"));
obj.setR16_31_liab_inr(rs.getBigDecimal("r16_31_liab_inr"));
obj.setR16_30_asse_lc(rs.getBigDecimal("r16_30_asse_lc"));
obj.setR16_30_asse_inr(rs.getBigDecimal("r16_30_asse_inr"));
obj.setR16_30_liab_lc(rs.getBigDecimal("r16_30_liab_lc"));
obj.setR16_30_liab_inr(rs.getBigDecimal("r16_30_liab_inr"));


// =========================
// R17
// =========================
obj.setR17_product(rs.getString("r17_product"));
obj.setR17_31_asse_lc(rs.getBigDecimal("r17_31_asse_lc"));
obj.setR17_31_asse_inr(rs.getBigDecimal("r17_31_asse_inr"));
obj.setR17_31_liab_lc(rs.getBigDecimal("r17_31_liab_lc"));
obj.setR17_31_liab_inr(rs.getBigDecimal("r17_31_liab_inr"));
obj.setR17_30_asse_lc(rs.getBigDecimal("r17_30_asse_lc"));
obj.setR17_30_asse_inr(rs.getBigDecimal("r17_30_asse_inr"));
obj.setR17_30_liab_lc(rs.getBigDecimal("r17_30_liab_lc"));
obj.setR17_30_liab_inr(rs.getBigDecimal("r17_30_liab_inr"));


// =========================
// R18
// =========================
obj.setR18_product(rs.getString("r18_product"));
obj.setR18_31_asse_lc(rs.getBigDecimal("r18_31_asse_lc"));
obj.setR18_31_asse_inr(rs.getBigDecimal("r18_31_asse_inr"));
obj.setR18_31_liab_lc(rs.getBigDecimal("r18_31_liab_lc"));
obj.setR18_31_liab_inr(rs.getBigDecimal("r18_31_liab_inr"));
obj.setR18_30_asse_lc(rs.getBigDecimal("r18_30_asse_lc"));
obj.setR18_30_asse_inr(rs.getBigDecimal("r18_30_asse_inr"));
obj.setR18_30_liab_lc(rs.getBigDecimal("r18_30_liab_lc"));
obj.setR18_30_liab_inr(rs.getBigDecimal("r18_30_liab_inr"));


// =========================
// R19
// =========================
obj.setR19_product(rs.getString("r19_product"));
obj.setR19_31_asse_lc(rs.getBigDecimal("r19_31_asse_lc"));
obj.setR19_31_asse_inr(rs.getBigDecimal("r19_31_asse_inr"));
obj.setR19_31_liab_lc(rs.getBigDecimal("r19_31_liab_lc"));
obj.setR19_31_liab_inr(rs.getBigDecimal("r19_31_liab_inr"));
obj.setR19_30_asse_lc(rs.getBigDecimal("r19_30_asse_lc"));
obj.setR19_30_asse_inr(rs.getBigDecimal("r19_30_asse_inr"));
obj.setR19_30_liab_lc(rs.getBigDecimal("r19_30_liab_lc"));
obj.setR19_30_liab_inr(rs.getBigDecimal("r19_30_liab_inr"));


// =========================
// R20
// =========================
obj.setR20_product(rs.getString("r20_product"));
obj.setR20_31_asse_lc(rs.getBigDecimal("r20_31_asse_lc"));
obj.setR20_31_asse_inr(rs.getBigDecimal("r20_31_asse_inr"));
obj.setR20_31_liab_lc(rs.getBigDecimal("r20_31_liab_lc"));
obj.setR20_31_liab_inr(rs.getBigDecimal("r20_31_liab_inr"));
obj.setR20_30_asse_lc(rs.getBigDecimal("r20_30_asse_lc"));
obj.setR20_30_asse_inr(rs.getBigDecimal("r20_30_asse_inr"));
obj.setR20_30_liab_lc(rs.getBigDecimal("r20_30_liab_lc"));
obj.setR20_30_liab_inr(rs.getBigDecimal("r20_30_liab_inr"));

// =========================
// R21
// =========================
obj.setR21_product(rs.getString("r21_product"));
obj.setR21_31_asse_lc(rs.getBigDecimal("r21_31_asse_lc"));
obj.setR21_31_asse_inr(rs.getBigDecimal("r21_31_asse_inr"));
obj.setR21_31_liab_lc(rs.getBigDecimal("r21_31_liab_lc"));
obj.setR21_31_liab_inr(rs.getBigDecimal("r21_31_liab_inr"));
obj.setR21_30_asse_lc(rs.getBigDecimal("r21_30_asse_lc"));
obj.setR21_30_asse_inr(rs.getBigDecimal("r21_30_asse_inr"));
obj.setR21_30_liab_lc(rs.getBigDecimal("r21_30_liab_lc"));
obj.setR21_30_liab_inr(rs.getBigDecimal("r21_30_liab_inr"));

// =========================
// R22
// =========================
obj.setR22_product(rs.getString("r22_product"));
obj.setR22_31_asse_lc(rs.getBigDecimal("r22_31_asse_lc"));
obj.setR22_31_asse_inr(rs.getBigDecimal("r22_31_asse_inr"));
obj.setR22_31_liab_lc(rs.getBigDecimal("r22_31_liab_lc"));
obj.setR22_31_liab_inr(rs.getBigDecimal("r22_31_liab_inr"));
obj.setR22_30_asse_lc(rs.getBigDecimal("r22_30_asse_lc"));
obj.setR22_30_asse_inr(rs.getBigDecimal("r22_30_asse_inr"));
obj.setR22_30_liab_lc(rs.getBigDecimal("r22_30_liab_lc"));
obj.setR22_30_liab_inr(rs.getBigDecimal("r22_30_liab_inr"));

// =========================
// R23
// =========================
obj.setR23_product(rs.getString("r23_product"));
obj.setR23_31_asse_lc(rs.getBigDecimal("r23_31_asse_lc"));
obj.setR23_31_asse_inr(rs.getBigDecimal("r23_31_asse_inr"));
obj.setR23_31_liab_lc(rs.getBigDecimal("r23_31_liab_lc"));
obj.setR23_31_liab_inr(rs.getBigDecimal("r23_31_liab_inr"));
obj.setR23_30_asse_lc(rs.getBigDecimal("r23_30_asse_lc"));
obj.setR23_30_asse_inr(rs.getBigDecimal("r23_30_asse_inr"));
obj.setR23_30_liab_lc(rs.getBigDecimal("r23_30_liab_lc"));
obj.setR23_30_liab_inr(rs.getBigDecimal("r23_30_liab_inr"));

// =========================
// R24
// =========================
obj.setR24_product(rs.getString("r24_product"));
obj.setR24_31_asse_lc(rs.getBigDecimal("r24_31_asse_lc"));
obj.setR24_31_asse_inr(rs.getBigDecimal("r24_31_asse_inr"));
obj.setR24_31_liab_lc(rs.getBigDecimal("r24_31_liab_lc"));
obj.setR24_31_liab_inr(rs.getBigDecimal("r24_31_liab_inr"));
obj.setR24_30_asse_lc(rs.getBigDecimal("r24_30_asse_lc"));
obj.setR24_30_asse_inr(rs.getBigDecimal("r24_30_asse_inr"));
obj.setR24_30_liab_lc(rs.getBigDecimal("r24_30_liab_lc"));
obj.setR24_30_liab_inr(rs.getBigDecimal("r24_30_liab_inr"));

// =========================
// R25
// =========================
obj.setR25_product(rs.getString("r25_product"));
obj.setR25_31_asse_lc(rs.getBigDecimal("r25_31_asse_lc"));
obj.setR25_31_asse_inr(rs.getBigDecimal("r25_31_asse_inr"));
obj.setR25_31_liab_lc(rs.getBigDecimal("r25_31_liab_lc"));
obj.setR25_31_liab_inr(rs.getBigDecimal("r25_31_liab_inr"));
obj.setR25_30_asse_lc(rs.getBigDecimal("r25_30_asse_lc"));
obj.setR25_30_asse_inr(rs.getBigDecimal("r25_30_asse_inr"));
obj.setR25_30_liab_lc(rs.getBigDecimal("r25_30_liab_lc"));
obj.setR25_30_liab_inr(rs.getBigDecimal("r25_30_liab_inr"));

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

public class DEFERRED_TAX_Summary_Entity {
	
	

	private String	r11_product;
	private BigDecimal	r11_31_asse_lc;
	private BigDecimal	r11_31_asse_inr;
	private BigDecimal	r11_31_liab_lc;
	private BigDecimal	r11_31_liab_inr;
	private BigDecimal	r11_30_asse_lc;
	private BigDecimal	r11_30_asse_inr;
	private BigDecimal	r11_30_liab_lc;
	private BigDecimal	r11_30_liab_inr;
	private String	r12_product;
	private BigDecimal	r12_31_asse_lc;
	private BigDecimal	r12_31_asse_inr;
	private BigDecimal	r12_31_liab_lc;
	private BigDecimal	r12_31_liab_inr;
	private BigDecimal	r12_30_asse_lc;
	private BigDecimal	r12_30_asse_inr;
	private BigDecimal	r12_30_liab_lc;
	private BigDecimal	r12_30_liab_inr;
	private String	r13_product;
	private BigDecimal	r13_31_asse_lc;
	private BigDecimal	r13_31_asse_inr;
	private BigDecimal	r13_31_liab_lc;
	private BigDecimal	r13_31_liab_inr;
	private BigDecimal	r13_30_asse_lc;
	private BigDecimal	r13_30_asse_inr;
	private BigDecimal	r13_30_liab_lc;
	private BigDecimal	r13_30_liab_inr;
	private String	r14_product;
	private BigDecimal	r14_31_asse_lc;
	private BigDecimal	r14_31_asse_inr;
	private BigDecimal	r14_31_liab_lc;
	private BigDecimal	r14_31_liab_inr;
	private BigDecimal	r14_30_asse_lc;
	private BigDecimal	r14_30_asse_inr;
	private BigDecimal	r14_30_liab_lc;
	private BigDecimal	r14_30_liab_inr;
	private String	r15_product;
	private BigDecimal	r15_31_asse_lc;
	private BigDecimal	r15_31_asse_inr;
	private BigDecimal	r15_31_liab_lc;
	private BigDecimal	r15_31_liab_inr;
	private BigDecimal	r15_30_asse_lc;
	private BigDecimal	r15_30_asse_inr;
	private BigDecimal	r15_30_liab_lc;
	private BigDecimal	r15_30_liab_inr;
	private String	r16_product;
	private BigDecimal	r16_31_asse_lc;
	private BigDecimal	r16_31_asse_inr;
	private BigDecimal	r16_31_liab_lc;
	private BigDecimal	r16_31_liab_inr;
	private BigDecimal	r16_30_asse_lc;
	private BigDecimal	r16_30_asse_inr;
	private BigDecimal	r16_30_liab_lc;
	private BigDecimal	r16_30_liab_inr;
	private String	r17_product;
	private BigDecimal	r17_31_asse_lc;
	private BigDecimal	r17_31_asse_inr;
	private BigDecimal	r17_31_liab_lc;
	private BigDecimal	r17_31_liab_inr;
	private BigDecimal	r17_30_asse_lc;
	private BigDecimal	r17_30_asse_inr;
	private BigDecimal	r17_30_liab_lc;
	private BigDecimal	r17_30_liab_inr;
	private String	r18_product;
	private BigDecimal	r18_31_asse_lc;
	private BigDecimal	r18_31_asse_inr;
	private BigDecimal	r18_31_liab_lc;
	private BigDecimal	r18_31_liab_inr;
	private BigDecimal	r18_30_asse_lc;
	private BigDecimal	r18_30_asse_inr;
	private BigDecimal	r18_30_liab_lc;
	private BigDecimal	r18_30_liab_inr;
	private String	r19_product;
	private BigDecimal	r19_31_asse_lc;
	private BigDecimal	r19_31_asse_inr;
	private BigDecimal	r19_31_liab_lc;
	private BigDecimal	r19_31_liab_inr;
	private BigDecimal	r19_30_asse_lc;
	private BigDecimal	r19_30_asse_inr;
	private BigDecimal	r19_30_liab_lc;
	private BigDecimal	r19_30_liab_inr;
	private String	r20_product;
	private BigDecimal	r20_31_asse_lc;
	private BigDecimal	r20_31_asse_inr;
	private BigDecimal	r20_31_liab_lc;
	private BigDecimal	r20_31_liab_inr;
	private BigDecimal	r20_30_asse_lc;
	private BigDecimal	r20_30_asse_inr;
	private BigDecimal	r20_30_liab_lc;
	private BigDecimal	r20_30_liab_inr;
	private String	r21_product;
	private BigDecimal	r21_31_asse_lc;
	private BigDecimal	r21_31_asse_inr;
	private BigDecimal	r21_31_liab_lc;
	private BigDecimal	r21_31_liab_inr;
	private BigDecimal	r21_30_asse_lc;
	private BigDecimal	r21_30_asse_inr;
	private BigDecimal	r21_30_liab_lc;
	private BigDecimal	r21_30_liab_inr;
	private String	r22_product;
	private BigDecimal	r22_31_asse_lc;
	private BigDecimal	r22_31_asse_inr;
	private BigDecimal	r22_31_liab_lc;
	private BigDecimal	r22_31_liab_inr;
	private BigDecimal	r22_30_asse_lc;
	private BigDecimal	r22_30_asse_inr;
	private BigDecimal	r22_30_liab_lc;
	private BigDecimal	r22_30_liab_inr;
	private String	r23_product;
	private BigDecimal	r23_31_asse_lc;
	private BigDecimal	r23_31_asse_inr;
	private BigDecimal	r23_31_liab_lc;
	private BigDecimal	r23_31_liab_inr;
	private BigDecimal	r23_30_asse_lc;
	private BigDecimal	r23_30_asse_inr;
	private BigDecimal	r23_30_liab_lc;
	private BigDecimal	r23_30_liab_inr;
	private String	r24_product;
	private BigDecimal	r24_31_asse_lc;
	private BigDecimal	r24_31_asse_inr;
	private BigDecimal	r24_31_liab_lc;
	private BigDecimal	r24_31_liab_inr;
	private BigDecimal	r24_30_asse_lc;
	private BigDecimal	r24_30_asse_inr;
	private BigDecimal	r24_30_liab_lc;
	private BigDecimal	r24_30_liab_inr;
	private String	r25_product;
	private BigDecimal	r25_31_asse_lc;
	private BigDecimal	r25_31_asse_inr;
	private BigDecimal	r25_31_liab_lc;
	private BigDecimal	r25_31_liab_inr;
	private BigDecimal	r25_30_asse_lc;
	private BigDecimal	r25_30_asse_inr;
	private BigDecimal	r25_30_liab_lc;
	private BigDecimal	r25_30_liab_inr;

	
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
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_31_asse_lc() {
		return r11_31_asse_lc;
	}
	public void setR11_31_asse_lc(BigDecimal r11_31_asse_lc) {
		this.r11_31_asse_lc = r11_31_asse_lc;
	}
	public BigDecimal getR11_31_asse_inr() {
		return r11_31_asse_inr;
	}
	public void setR11_31_asse_inr(BigDecimal r11_31_asse_inr) {
		this.r11_31_asse_inr = r11_31_asse_inr;
	}
	public BigDecimal getR11_31_liab_lc() {
		return r11_31_liab_lc;
	}
	public void setR11_31_liab_lc(BigDecimal r11_31_liab_lc) {
		this.r11_31_liab_lc = r11_31_liab_lc;
	}
	public BigDecimal getR11_31_liab_inr() {
		return r11_31_liab_inr;
	}
	public void setR11_31_liab_inr(BigDecimal r11_31_liab_inr) {
		this.r11_31_liab_inr = r11_31_liab_inr;
	}
	public BigDecimal getR11_30_asse_lc() {
		return r11_30_asse_lc;
	}
	public void setR11_30_asse_lc(BigDecimal r11_30_asse_lc) {
		this.r11_30_asse_lc = r11_30_asse_lc;
	}
	public BigDecimal getR11_30_asse_inr() {
		return r11_30_asse_inr;
	}
	public void setR11_30_asse_inr(BigDecimal r11_30_asse_inr) {
		this.r11_30_asse_inr = r11_30_asse_inr;
	}
	public BigDecimal getR11_30_liab_lc() {
		return r11_30_liab_lc;
	}
	public void setR11_30_liab_lc(BigDecimal r11_30_liab_lc) {
		this.r11_30_liab_lc = r11_30_liab_lc;
	}
	public BigDecimal getR11_30_liab_inr() {
		return r11_30_liab_inr;
	}
	public void setR11_30_liab_inr(BigDecimal r11_30_liab_inr) {
		this.r11_30_liab_inr = r11_30_liab_inr;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_31_asse_lc() {
		return r12_31_asse_lc;
	}
	public void setR12_31_asse_lc(BigDecimal r12_31_asse_lc) {
		this.r12_31_asse_lc = r12_31_asse_lc;
	}
	public BigDecimal getR12_31_asse_inr() {
		return r12_31_asse_inr;
	}
	public void setR12_31_asse_inr(BigDecimal r12_31_asse_inr) {
		this.r12_31_asse_inr = r12_31_asse_inr;
	}
	public BigDecimal getR12_31_liab_lc() {
		return r12_31_liab_lc;
	}
	public void setR12_31_liab_lc(BigDecimal r12_31_liab_lc) {
		this.r12_31_liab_lc = r12_31_liab_lc;
	}
	public BigDecimal getR12_31_liab_inr() {
		return r12_31_liab_inr;
	}
	public void setR12_31_liab_inr(BigDecimal r12_31_liab_inr) {
		this.r12_31_liab_inr = r12_31_liab_inr;
	}
	public BigDecimal getR12_30_asse_lc() {
		return r12_30_asse_lc;
	}
	public void setR12_30_asse_lc(BigDecimal r12_30_asse_lc) {
		this.r12_30_asse_lc = r12_30_asse_lc;
	}
	public BigDecimal getR12_30_asse_inr() {
		return r12_30_asse_inr;
	}
	public void setR12_30_asse_inr(BigDecimal r12_30_asse_inr) {
		this.r12_30_asse_inr = r12_30_asse_inr;
	}
	public BigDecimal getR12_30_liab_lc() {
		return r12_30_liab_lc;
	}
	public void setR12_30_liab_lc(BigDecimal r12_30_liab_lc) {
		this.r12_30_liab_lc = r12_30_liab_lc;
	}
	public BigDecimal getR12_30_liab_inr() {
		return r12_30_liab_inr;
	}
	public void setR12_30_liab_inr(BigDecimal r12_30_liab_inr) {
		this.r12_30_liab_inr = r12_30_liab_inr;
	}
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_31_asse_lc() {
		return r13_31_asse_lc;
	}
	public void setR13_31_asse_lc(BigDecimal r13_31_asse_lc) {
		this.r13_31_asse_lc = r13_31_asse_lc;
	}
	public BigDecimal getR13_31_asse_inr() {
		return r13_31_asse_inr;
	}
	public void setR13_31_asse_inr(BigDecimal r13_31_asse_inr) {
		this.r13_31_asse_inr = r13_31_asse_inr;
	}
	public BigDecimal getR13_31_liab_lc() {
		return r13_31_liab_lc;
	}
	public void setR13_31_liab_lc(BigDecimal r13_31_liab_lc) {
		this.r13_31_liab_lc = r13_31_liab_lc;
	}
	public BigDecimal getR13_31_liab_inr() {
		return r13_31_liab_inr;
	}
	public void setR13_31_liab_inr(BigDecimal r13_31_liab_inr) {
		this.r13_31_liab_inr = r13_31_liab_inr;
	}
	public BigDecimal getR13_30_asse_lc() {
		return r13_30_asse_lc;
	}
	public void setR13_30_asse_lc(BigDecimal r13_30_asse_lc) {
		this.r13_30_asse_lc = r13_30_asse_lc;
	}
	public BigDecimal getR13_30_asse_inr() {
		return r13_30_asse_inr;
	}
	public void setR13_30_asse_inr(BigDecimal r13_30_asse_inr) {
		this.r13_30_asse_inr = r13_30_asse_inr;
	}
	public BigDecimal getR13_30_liab_lc() {
		return r13_30_liab_lc;
	}
	public void setR13_30_liab_lc(BigDecimal r13_30_liab_lc) {
		this.r13_30_liab_lc = r13_30_liab_lc;
	}
	public BigDecimal getR13_30_liab_inr() {
		return r13_30_liab_inr;
	}
	public void setR13_30_liab_inr(BigDecimal r13_30_liab_inr) {
		this.r13_30_liab_inr = r13_30_liab_inr;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_31_asse_lc() {
		return r14_31_asse_lc;
	}
	public void setR14_31_asse_lc(BigDecimal r14_31_asse_lc) {
		this.r14_31_asse_lc = r14_31_asse_lc;
	}
	public BigDecimal getR14_31_asse_inr() {
		return r14_31_asse_inr;
	}
	public void setR14_31_asse_inr(BigDecimal r14_31_asse_inr) {
		this.r14_31_asse_inr = r14_31_asse_inr;
	}
	public BigDecimal getR14_31_liab_lc() {
		return r14_31_liab_lc;
	}
	public void setR14_31_liab_lc(BigDecimal r14_31_liab_lc) {
		this.r14_31_liab_lc = r14_31_liab_lc;
	}
	public BigDecimal getR14_31_liab_inr() {
		return r14_31_liab_inr;
	}
	public void setR14_31_liab_inr(BigDecimal r14_31_liab_inr) {
		this.r14_31_liab_inr = r14_31_liab_inr;
	}
	public BigDecimal getR14_30_asse_lc() {
		return r14_30_asse_lc;
	}
	public void setR14_30_asse_lc(BigDecimal r14_30_asse_lc) {
		this.r14_30_asse_lc = r14_30_asse_lc;
	}
	public BigDecimal getR14_30_asse_inr() {
		return r14_30_asse_inr;
	}
	public void setR14_30_asse_inr(BigDecimal r14_30_asse_inr) {
		this.r14_30_asse_inr = r14_30_asse_inr;
	}
	public BigDecimal getR14_30_liab_lc() {
		return r14_30_liab_lc;
	}
	public void setR14_30_liab_lc(BigDecimal r14_30_liab_lc) {
		this.r14_30_liab_lc = r14_30_liab_lc;
	}
	public BigDecimal getR14_30_liab_inr() {
		return r14_30_liab_inr;
	}
	public void setR14_30_liab_inr(BigDecimal r14_30_liab_inr) {
		this.r14_30_liab_inr = r14_30_liab_inr;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_31_asse_lc() {
		return r15_31_asse_lc;
	}
	public void setR15_31_asse_lc(BigDecimal r15_31_asse_lc) {
		this.r15_31_asse_lc = r15_31_asse_lc;
	}
	public BigDecimal getR15_31_asse_inr() {
		return r15_31_asse_inr;
	}
	public void setR15_31_asse_inr(BigDecimal r15_31_asse_inr) {
		this.r15_31_asse_inr = r15_31_asse_inr;
	}
	public BigDecimal getR15_31_liab_lc() {
		return r15_31_liab_lc;
	}
	public void setR15_31_liab_lc(BigDecimal r15_31_liab_lc) {
		this.r15_31_liab_lc = r15_31_liab_lc;
	}
	public BigDecimal getR15_31_liab_inr() {
		return r15_31_liab_inr;
	}
	public void setR15_31_liab_inr(BigDecimal r15_31_liab_inr) {
		this.r15_31_liab_inr = r15_31_liab_inr;
	}
	public BigDecimal getR15_30_asse_lc() {
		return r15_30_asse_lc;
	}
	public void setR15_30_asse_lc(BigDecimal r15_30_asse_lc) {
		this.r15_30_asse_lc = r15_30_asse_lc;
	}
	public BigDecimal getR15_30_asse_inr() {
		return r15_30_asse_inr;
	}
	public void setR15_30_asse_inr(BigDecimal r15_30_asse_inr) {
		this.r15_30_asse_inr = r15_30_asse_inr;
	}
	public BigDecimal getR15_30_liab_lc() {
		return r15_30_liab_lc;
	}
	public void setR15_30_liab_lc(BigDecimal r15_30_liab_lc) {
		this.r15_30_liab_lc = r15_30_liab_lc;
	}
	public BigDecimal getR15_30_liab_inr() {
		return r15_30_liab_inr;
	}
	public void setR15_30_liab_inr(BigDecimal r15_30_liab_inr) {
		this.r15_30_liab_inr = r15_30_liab_inr;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_31_asse_lc() {
		return r16_31_asse_lc;
	}
	public void setR16_31_asse_lc(BigDecimal r16_31_asse_lc) {
		this.r16_31_asse_lc = r16_31_asse_lc;
	}
	public BigDecimal getR16_31_asse_inr() {
		return r16_31_asse_inr;
	}
	public void setR16_31_asse_inr(BigDecimal r16_31_asse_inr) {
		this.r16_31_asse_inr = r16_31_asse_inr;
	}
	public BigDecimal getR16_31_liab_lc() {
		return r16_31_liab_lc;
	}
	public void setR16_31_liab_lc(BigDecimal r16_31_liab_lc) {
		this.r16_31_liab_lc = r16_31_liab_lc;
	}
	public BigDecimal getR16_31_liab_inr() {
		return r16_31_liab_inr;
	}
	public void setR16_31_liab_inr(BigDecimal r16_31_liab_inr) {
		this.r16_31_liab_inr = r16_31_liab_inr;
	}
	public BigDecimal getR16_30_asse_lc() {
		return r16_30_asse_lc;
	}
	public void setR16_30_asse_lc(BigDecimal r16_30_asse_lc) {
		this.r16_30_asse_lc = r16_30_asse_lc;
	}
	public BigDecimal getR16_30_asse_inr() {
		return r16_30_asse_inr;
	}
	public void setR16_30_asse_inr(BigDecimal r16_30_asse_inr) {
		this.r16_30_asse_inr = r16_30_asse_inr;
	}
	public BigDecimal getR16_30_liab_lc() {
		return r16_30_liab_lc;
	}
	public void setR16_30_liab_lc(BigDecimal r16_30_liab_lc) {
		this.r16_30_liab_lc = r16_30_liab_lc;
	}
	public BigDecimal getR16_30_liab_inr() {
		return r16_30_liab_inr;
	}
	public void setR16_30_liab_inr(BigDecimal r16_30_liab_inr) {
		this.r16_30_liab_inr = r16_30_liab_inr;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_31_asse_lc() {
		return r17_31_asse_lc;
	}
	public void setR17_31_asse_lc(BigDecimal r17_31_asse_lc) {
		this.r17_31_asse_lc = r17_31_asse_lc;
	}
	public BigDecimal getR17_31_asse_inr() {
		return r17_31_asse_inr;
	}
	public void setR17_31_asse_inr(BigDecimal r17_31_asse_inr) {
		this.r17_31_asse_inr = r17_31_asse_inr;
	}
	public BigDecimal getR17_31_liab_lc() {
		return r17_31_liab_lc;
	}
	public void setR17_31_liab_lc(BigDecimal r17_31_liab_lc) {
		this.r17_31_liab_lc = r17_31_liab_lc;
	}
	public BigDecimal getR17_31_liab_inr() {
		return r17_31_liab_inr;
	}
	public void setR17_31_liab_inr(BigDecimal r17_31_liab_inr) {
		this.r17_31_liab_inr = r17_31_liab_inr;
	}
	public BigDecimal getR17_30_asse_lc() {
		return r17_30_asse_lc;
	}
	public void setR17_30_asse_lc(BigDecimal r17_30_asse_lc) {
		this.r17_30_asse_lc = r17_30_asse_lc;
	}
	public BigDecimal getR17_30_asse_inr() {
		return r17_30_asse_inr;
	}
	public void setR17_30_asse_inr(BigDecimal r17_30_asse_inr) {
		this.r17_30_asse_inr = r17_30_asse_inr;
	}
	public BigDecimal getR17_30_liab_lc() {
		return r17_30_liab_lc;
	}
	public void setR17_30_liab_lc(BigDecimal r17_30_liab_lc) {
		this.r17_30_liab_lc = r17_30_liab_lc;
	}
	public BigDecimal getR17_30_liab_inr() {
		return r17_30_liab_inr;
	}
	public void setR17_30_liab_inr(BigDecimal r17_30_liab_inr) {
		this.r17_30_liab_inr = r17_30_liab_inr;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_31_asse_lc() {
		return r18_31_asse_lc;
	}
	public void setR18_31_asse_lc(BigDecimal r18_31_asse_lc) {
		this.r18_31_asse_lc = r18_31_asse_lc;
	}
	public BigDecimal getR18_31_asse_inr() {
		return r18_31_asse_inr;
	}
	public void setR18_31_asse_inr(BigDecimal r18_31_asse_inr) {
		this.r18_31_asse_inr = r18_31_asse_inr;
	}
	public BigDecimal getR18_31_liab_lc() {
		return r18_31_liab_lc;
	}
	public void setR18_31_liab_lc(BigDecimal r18_31_liab_lc) {
		this.r18_31_liab_lc = r18_31_liab_lc;
	}
	public BigDecimal getR18_31_liab_inr() {
		return r18_31_liab_inr;
	}
	public void setR18_31_liab_inr(BigDecimal r18_31_liab_inr) {
		this.r18_31_liab_inr = r18_31_liab_inr;
	}
	public BigDecimal getR18_30_asse_lc() {
		return r18_30_asse_lc;
	}
	public void setR18_30_asse_lc(BigDecimal r18_30_asse_lc) {
		this.r18_30_asse_lc = r18_30_asse_lc;
	}
	public BigDecimal getR18_30_asse_inr() {
		return r18_30_asse_inr;
	}
	public void setR18_30_asse_inr(BigDecimal r18_30_asse_inr) {
		this.r18_30_asse_inr = r18_30_asse_inr;
	}
	public BigDecimal getR18_30_liab_lc() {
		return r18_30_liab_lc;
	}
	public void setR18_30_liab_lc(BigDecimal r18_30_liab_lc) {
		this.r18_30_liab_lc = r18_30_liab_lc;
	}
	public BigDecimal getR18_30_liab_inr() {
		return r18_30_liab_inr;
	}
	public void setR18_30_liab_inr(BigDecimal r18_30_liab_inr) {
		this.r18_30_liab_inr = r18_30_liab_inr;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_31_asse_lc() {
		return r19_31_asse_lc;
	}
	public void setR19_31_asse_lc(BigDecimal r19_31_asse_lc) {
		this.r19_31_asse_lc = r19_31_asse_lc;
	}
	public BigDecimal getR19_31_asse_inr() {
		return r19_31_asse_inr;
	}
	public void setR19_31_asse_inr(BigDecimal r19_31_asse_inr) {
		this.r19_31_asse_inr = r19_31_asse_inr;
	}
	public BigDecimal getR19_31_liab_lc() {
		return r19_31_liab_lc;
	}
	public void setR19_31_liab_lc(BigDecimal r19_31_liab_lc) {
		this.r19_31_liab_lc = r19_31_liab_lc;
	}
	public BigDecimal getR19_31_liab_inr() {
		return r19_31_liab_inr;
	}
	public void setR19_31_liab_inr(BigDecimal r19_31_liab_inr) {
		this.r19_31_liab_inr = r19_31_liab_inr;
	}
	public BigDecimal getR19_30_asse_lc() {
		return r19_30_asse_lc;
	}
	public void setR19_30_asse_lc(BigDecimal r19_30_asse_lc) {
		this.r19_30_asse_lc = r19_30_asse_lc;
	}
	public BigDecimal getR19_30_asse_inr() {
		return r19_30_asse_inr;
	}
	public void setR19_30_asse_inr(BigDecimal r19_30_asse_inr) {
		this.r19_30_asse_inr = r19_30_asse_inr;
	}
	public BigDecimal getR19_30_liab_lc() {
		return r19_30_liab_lc;
	}
	public void setR19_30_liab_lc(BigDecimal r19_30_liab_lc) {
		this.r19_30_liab_lc = r19_30_liab_lc;
	}
	public BigDecimal getR19_30_liab_inr() {
		return r19_30_liab_inr;
	}
	public void setR19_30_liab_inr(BigDecimal r19_30_liab_inr) {
		this.r19_30_liab_inr = r19_30_liab_inr;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_31_asse_lc() {
		return r20_31_asse_lc;
	}
	public void setR20_31_asse_lc(BigDecimal r20_31_asse_lc) {
		this.r20_31_asse_lc = r20_31_asse_lc;
	}
	public BigDecimal getR20_31_asse_inr() {
		return r20_31_asse_inr;
	}
	public void setR20_31_asse_inr(BigDecimal r20_31_asse_inr) {
		this.r20_31_asse_inr = r20_31_asse_inr;
	}
	public BigDecimal getR20_31_liab_lc() {
		return r20_31_liab_lc;
	}
	public void setR20_31_liab_lc(BigDecimal r20_31_liab_lc) {
		this.r20_31_liab_lc = r20_31_liab_lc;
	}
	public BigDecimal getR20_31_liab_inr() {
		return r20_31_liab_inr;
	}
	public void setR20_31_liab_inr(BigDecimal r20_31_liab_inr) {
		this.r20_31_liab_inr = r20_31_liab_inr;
	}
	public BigDecimal getR20_30_asse_lc() {
		return r20_30_asse_lc;
	}
	public void setR20_30_asse_lc(BigDecimal r20_30_asse_lc) {
		this.r20_30_asse_lc = r20_30_asse_lc;
	}
	public BigDecimal getR20_30_asse_inr() {
		return r20_30_asse_inr;
	}
	public void setR20_30_asse_inr(BigDecimal r20_30_asse_inr) {
		this.r20_30_asse_inr = r20_30_asse_inr;
	}
	public BigDecimal getR20_30_liab_lc() {
		return r20_30_liab_lc;
	}
	public void setR20_30_liab_lc(BigDecimal r20_30_liab_lc) {
		this.r20_30_liab_lc = r20_30_liab_lc;
	}
	public BigDecimal getR20_30_liab_inr() {
		return r20_30_liab_inr;
	}
	public void setR20_30_liab_inr(BigDecimal r20_30_liab_inr) {
		this.r20_30_liab_inr = r20_30_liab_inr;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_31_asse_lc() {
		return r21_31_asse_lc;
	}
	public void setR21_31_asse_lc(BigDecimal r21_31_asse_lc) {
		this.r21_31_asse_lc = r21_31_asse_lc;
	}
	public BigDecimal getR21_31_asse_inr() {
		return r21_31_asse_inr;
	}
	public void setR21_31_asse_inr(BigDecimal r21_31_asse_inr) {
		this.r21_31_asse_inr = r21_31_asse_inr;
	}
	public BigDecimal getR21_31_liab_lc() {
		return r21_31_liab_lc;
	}
	public void setR21_31_liab_lc(BigDecimal r21_31_liab_lc) {
		this.r21_31_liab_lc = r21_31_liab_lc;
	}
	public BigDecimal getR21_31_liab_inr() {
		return r21_31_liab_inr;
	}
	public void setR21_31_liab_inr(BigDecimal r21_31_liab_inr) {
		this.r21_31_liab_inr = r21_31_liab_inr;
	}
	public BigDecimal getR21_30_asse_lc() {
		return r21_30_asse_lc;
	}
	public void setR21_30_asse_lc(BigDecimal r21_30_asse_lc) {
		this.r21_30_asse_lc = r21_30_asse_lc;
	}
	public BigDecimal getR21_30_asse_inr() {
		return r21_30_asse_inr;
	}
	public void setR21_30_asse_inr(BigDecimal r21_30_asse_inr) {
		this.r21_30_asse_inr = r21_30_asse_inr;
	}
	public BigDecimal getR21_30_liab_lc() {
		return r21_30_liab_lc;
	}
	public void setR21_30_liab_lc(BigDecimal r21_30_liab_lc) {
		this.r21_30_liab_lc = r21_30_liab_lc;
	}
	public BigDecimal getR21_30_liab_inr() {
		return r21_30_liab_inr;
	}
	public void setR21_30_liab_inr(BigDecimal r21_30_liab_inr) {
		this.r21_30_liab_inr = r21_30_liab_inr;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_31_asse_lc() {
		return r22_31_asse_lc;
	}
	public void setR22_31_asse_lc(BigDecimal r22_31_asse_lc) {
		this.r22_31_asse_lc = r22_31_asse_lc;
	}
	public BigDecimal getR22_31_asse_inr() {
		return r22_31_asse_inr;
	}
	public void setR22_31_asse_inr(BigDecimal r22_31_asse_inr) {
		this.r22_31_asse_inr = r22_31_asse_inr;
	}
	public BigDecimal getR22_31_liab_lc() {
		return r22_31_liab_lc;
	}
	public void setR22_31_liab_lc(BigDecimal r22_31_liab_lc) {
		this.r22_31_liab_lc = r22_31_liab_lc;
	}
	public BigDecimal getR22_31_liab_inr() {
		return r22_31_liab_inr;
	}
	public void setR22_31_liab_inr(BigDecimal r22_31_liab_inr) {
		this.r22_31_liab_inr = r22_31_liab_inr;
	}
	public BigDecimal getR22_30_asse_lc() {
		return r22_30_asse_lc;
	}
	public void setR22_30_asse_lc(BigDecimal r22_30_asse_lc) {
		this.r22_30_asse_lc = r22_30_asse_lc;
	}
	public BigDecimal getR22_30_asse_inr() {
		return r22_30_asse_inr;
	}
	public void setR22_30_asse_inr(BigDecimal r22_30_asse_inr) {
		this.r22_30_asse_inr = r22_30_asse_inr;
	}
	public BigDecimal getR22_30_liab_lc() {
		return r22_30_liab_lc;
	}
	public void setR22_30_liab_lc(BigDecimal r22_30_liab_lc) {
		this.r22_30_liab_lc = r22_30_liab_lc;
	}
	public BigDecimal getR22_30_liab_inr() {
		return r22_30_liab_inr;
	}
	public void setR22_30_liab_inr(BigDecimal r22_30_liab_inr) {
		this.r22_30_liab_inr = r22_30_liab_inr;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_31_asse_lc() {
		return r23_31_asse_lc;
	}
	public void setR23_31_asse_lc(BigDecimal r23_31_asse_lc) {
		this.r23_31_asse_lc = r23_31_asse_lc;
	}
	public BigDecimal getR23_31_asse_inr() {
		return r23_31_asse_inr;
	}
	public void setR23_31_asse_inr(BigDecimal r23_31_asse_inr) {
		this.r23_31_asse_inr = r23_31_asse_inr;
	}
	public BigDecimal getR23_31_liab_lc() {
		return r23_31_liab_lc;
	}
	public void setR23_31_liab_lc(BigDecimal r23_31_liab_lc) {
		this.r23_31_liab_lc = r23_31_liab_lc;
	}
	public BigDecimal getR23_31_liab_inr() {
		return r23_31_liab_inr;
	}
	public void setR23_31_liab_inr(BigDecimal r23_31_liab_inr) {
		this.r23_31_liab_inr = r23_31_liab_inr;
	}
	public BigDecimal getR23_30_asse_lc() {
		return r23_30_asse_lc;
	}
	public void setR23_30_asse_lc(BigDecimal r23_30_asse_lc) {
		this.r23_30_asse_lc = r23_30_asse_lc;
	}
	public BigDecimal getR23_30_asse_inr() {
		return r23_30_asse_inr;
	}
	public void setR23_30_asse_inr(BigDecimal r23_30_asse_inr) {
		this.r23_30_asse_inr = r23_30_asse_inr;
	}
	public BigDecimal getR23_30_liab_lc() {
		return r23_30_liab_lc;
	}
	public void setR23_30_liab_lc(BigDecimal r23_30_liab_lc) {
		this.r23_30_liab_lc = r23_30_liab_lc;
	}
	public BigDecimal getR23_30_liab_inr() {
		return r23_30_liab_inr;
	}
	public void setR23_30_liab_inr(BigDecimal r23_30_liab_inr) {
		this.r23_30_liab_inr = r23_30_liab_inr;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_31_asse_lc() {
		return r24_31_asse_lc;
	}
	public void setR24_31_asse_lc(BigDecimal r24_31_asse_lc) {
		this.r24_31_asse_lc = r24_31_asse_lc;
	}
	public BigDecimal getR24_31_asse_inr() {
		return r24_31_asse_inr;
	}
	public void setR24_31_asse_inr(BigDecimal r24_31_asse_inr) {
		this.r24_31_asse_inr = r24_31_asse_inr;
	}
	public BigDecimal getR24_31_liab_lc() {
		return r24_31_liab_lc;
	}
	public void setR24_31_liab_lc(BigDecimal r24_31_liab_lc) {
		this.r24_31_liab_lc = r24_31_liab_lc;
	}
	public BigDecimal getR24_31_liab_inr() {
		return r24_31_liab_inr;
	}
	public void setR24_31_liab_inr(BigDecimal r24_31_liab_inr) {
		this.r24_31_liab_inr = r24_31_liab_inr;
	}
	public BigDecimal getR24_30_asse_lc() {
		return r24_30_asse_lc;
	}
	public void setR24_30_asse_lc(BigDecimal r24_30_asse_lc) {
		this.r24_30_asse_lc = r24_30_asse_lc;
	}
	public BigDecimal getR24_30_asse_inr() {
		return r24_30_asse_inr;
	}
	public void setR24_30_asse_inr(BigDecimal r24_30_asse_inr) {
		this.r24_30_asse_inr = r24_30_asse_inr;
	}
	public BigDecimal getR24_30_liab_lc() {
		return r24_30_liab_lc;
	}
	public void setR24_30_liab_lc(BigDecimal r24_30_liab_lc) {
		this.r24_30_liab_lc = r24_30_liab_lc;
	}
	public BigDecimal getR24_30_liab_inr() {
		return r24_30_liab_inr;
	}
	public void setR24_30_liab_inr(BigDecimal r24_30_liab_inr) {
		this.r24_30_liab_inr = r24_30_liab_inr;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_31_asse_lc() {
		return r25_31_asse_lc;
	}
	public void setR25_31_asse_lc(BigDecimal r25_31_asse_lc) {
		this.r25_31_asse_lc = r25_31_asse_lc;
	}
	public BigDecimal getR25_31_asse_inr() {
		return r25_31_asse_inr;
	}
	public void setR25_31_asse_inr(BigDecimal r25_31_asse_inr) {
		this.r25_31_asse_inr = r25_31_asse_inr;
	}
	public BigDecimal getR25_31_liab_lc() {
		return r25_31_liab_lc;
	}
	public void setR25_31_liab_lc(BigDecimal r25_31_liab_lc) {
		this.r25_31_liab_lc = r25_31_liab_lc;
	}
	public BigDecimal getR25_31_liab_inr() {
		return r25_31_liab_inr;
	}
	public void setR25_31_liab_inr(BigDecimal r25_31_liab_inr) {
		this.r25_31_liab_inr = r25_31_liab_inr;
	}
	public BigDecimal getR25_30_asse_lc() {
		return r25_30_asse_lc;
	}
	public void setR25_30_asse_lc(BigDecimal r25_30_asse_lc) {
		this.r25_30_asse_lc = r25_30_asse_lc;
	}
	public BigDecimal getR25_30_asse_inr() {
		return r25_30_asse_inr;
	}
	public void setR25_30_asse_inr(BigDecimal r25_30_asse_inr) {
		this.r25_30_asse_inr = r25_30_asse_inr;
	}
	public BigDecimal getR25_30_liab_lc() {
		return r25_30_liab_lc;
	}
	public void setR25_30_liab_lc(BigDecimal r25_30_liab_lc) {
		this.r25_30_liab_lc = r25_30_liab_lc;
	}
	public BigDecimal getR25_30_liab_inr() {
		return r25_30_liab_inr;
	}
	public void setR25_30_liab_inr(BigDecimal r25_30_liab_inr) {
		this.r25_30_liab_inr = r25_30_liab_inr;
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

// ARCHIVAL SUMMARY  ENTITY CLASS

public class DEFERRED_TAX_Archival_RowMapper
        implements RowMapper<DEFERRED_TAX_Archival_Summary_Entity> {

    @Override
    public DEFERRED_TAX_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        DEFERRED_TAX_Archival_Summary_Entity obj = new DEFERRED_TAX_Archival_Summary_Entity();

        // =========================
        // R11 → R25
        // =========================

        // R11
        obj.setR11_product(rs.getString("r11_product"));
        obj.setR11_31_asse_lc(rs.getBigDecimal("r11_31_asse_lc"));
        obj.setR11_31_asse_inr(rs.getBigDecimal("r11_31_asse_inr"));
        obj.setR11_31_liab_lc(rs.getBigDecimal("r11_31_liab_lc"));
        obj.setR11_31_liab_inr(rs.getBigDecimal("r11_31_liab_inr"));
        obj.setR11_30_asse_lc(rs.getBigDecimal("r11_30_asse_lc"));
        obj.setR11_30_asse_inr(rs.getBigDecimal("r11_30_asse_inr"));
        obj.setR11_30_liab_lc(rs.getBigDecimal("r11_30_liab_lc"));
        obj.setR11_30_liab_inr(rs.getBigDecimal("r11_30_liab_inr"));

        // R12
        obj.setR12_product(rs.getString("r12_product"));
        obj.setR12_31_asse_lc(rs.getBigDecimal("r12_31_asse_lc"));
        obj.setR12_31_asse_inr(rs.getBigDecimal("r12_31_asse_inr"));
        obj.setR12_31_liab_lc(rs.getBigDecimal("r12_31_liab_lc"));
        obj.setR12_31_liab_inr(rs.getBigDecimal("r12_31_liab_inr"));
        obj.setR12_30_asse_lc(rs.getBigDecimal("r12_30_asse_lc"));
        obj.setR12_30_asse_inr(rs.getBigDecimal("r12_30_asse_inr"));
        obj.setR12_30_liab_lc(rs.getBigDecimal("r12_30_liab_lc"));
        obj.setR12_30_liab_inr(rs.getBigDecimal("r12_30_liab_inr"));

        // R13
        obj.setR13_product(rs.getString("r13_product"));
        obj.setR13_31_asse_lc(rs.getBigDecimal("r13_31_asse_lc"));
        obj.setR13_31_asse_inr(rs.getBigDecimal("r13_31_asse_inr"));
        obj.setR13_31_liab_lc(rs.getBigDecimal("r13_31_liab_lc"));
        obj.setR13_31_liab_inr(rs.getBigDecimal("r13_31_liab_inr"));
        obj.setR13_30_asse_lc(rs.getBigDecimal("r13_30_asse_lc"));
        obj.setR13_30_asse_inr(rs.getBigDecimal("r13_30_asse_inr"));
        obj.setR13_30_liab_lc(rs.getBigDecimal("r13_30_liab_lc"));
        obj.setR13_30_liab_inr(rs.getBigDecimal("r13_30_liab_inr"));

        // R14
        obj.setR14_product(rs.getString("r14_product"));
        obj.setR14_31_asse_lc(rs.getBigDecimal("r14_31_asse_lc"));
        obj.setR14_31_asse_inr(rs.getBigDecimal("r14_31_asse_inr"));
        obj.setR14_31_liab_lc(rs.getBigDecimal("r14_31_liab_lc"));
        obj.setR14_31_liab_inr(rs.getBigDecimal("r14_31_liab_inr"));
        obj.setR14_30_asse_lc(rs.getBigDecimal("r14_30_asse_lc"));
        obj.setR14_30_asse_inr(rs.getBigDecimal("r14_30_asse_inr"));
        obj.setR14_30_liab_lc(rs.getBigDecimal("r14_30_liab_lc"));
        obj.setR14_30_liab_inr(rs.getBigDecimal("r14_30_liab_inr"));

        // R15
        obj.setR15_product(rs.getString("r15_product"));
        obj.setR15_31_asse_lc(rs.getBigDecimal("r15_31_asse_lc"));
        obj.setR15_31_asse_inr(rs.getBigDecimal("r15_31_asse_inr"));
        obj.setR15_31_liab_lc(rs.getBigDecimal("r15_31_liab_lc"));
        obj.setR15_31_liab_inr(rs.getBigDecimal("r15_31_liab_inr"));
        obj.setR15_30_asse_lc(rs.getBigDecimal("r15_30_asse_lc"));
        obj.setR15_30_asse_inr(rs.getBigDecimal("r15_30_asse_inr"));
        obj.setR15_30_liab_lc(rs.getBigDecimal("r15_30_liab_lc"));
        obj.setR15_30_liab_inr(rs.getBigDecimal("r15_30_liab_inr"));
		
		
// =========================
// R16
// =========================
obj.setR16_product(rs.getString("r16_product"));
obj.setR16_31_asse_lc(rs.getBigDecimal("r16_31_asse_lc"));
obj.setR16_31_asse_inr(rs.getBigDecimal("r16_31_asse_inr"));
obj.setR16_31_liab_lc(rs.getBigDecimal("r16_31_liab_lc"));
obj.setR16_31_liab_inr(rs.getBigDecimal("r16_31_liab_inr"));
obj.setR16_30_asse_lc(rs.getBigDecimal("r16_30_asse_lc"));
obj.setR16_30_asse_inr(rs.getBigDecimal("r16_30_asse_inr"));
obj.setR16_30_liab_lc(rs.getBigDecimal("r16_30_liab_lc"));
obj.setR16_30_liab_inr(rs.getBigDecimal("r16_30_liab_inr"));


// =========================
// R17
// =========================
obj.setR17_product(rs.getString("r17_product"));
obj.setR17_31_asse_lc(rs.getBigDecimal("r17_31_asse_lc"));
obj.setR17_31_asse_inr(rs.getBigDecimal("r17_31_asse_inr"));
obj.setR17_31_liab_lc(rs.getBigDecimal("r17_31_liab_lc"));
obj.setR17_31_liab_inr(rs.getBigDecimal("r17_31_liab_inr"));
obj.setR17_30_asse_lc(rs.getBigDecimal("r17_30_asse_lc"));
obj.setR17_30_asse_inr(rs.getBigDecimal("r17_30_asse_inr"));
obj.setR17_30_liab_lc(rs.getBigDecimal("r17_30_liab_lc"));
obj.setR17_30_liab_inr(rs.getBigDecimal("r17_30_liab_inr"));


// =========================
// R18
// =========================
obj.setR18_product(rs.getString("r18_product"));
obj.setR18_31_asse_lc(rs.getBigDecimal("r18_31_asse_lc"));
obj.setR18_31_asse_inr(rs.getBigDecimal("r18_31_asse_inr"));
obj.setR18_31_liab_lc(rs.getBigDecimal("r18_31_liab_lc"));
obj.setR18_31_liab_inr(rs.getBigDecimal("r18_31_liab_inr"));
obj.setR18_30_asse_lc(rs.getBigDecimal("r18_30_asse_lc"));
obj.setR18_30_asse_inr(rs.getBigDecimal("r18_30_asse_inr"));
obj.setR18_30_liab_lc(rs.getBigDecimal("r18_30_liab_lc"));
obj.setR18_30_liab_inr(rs.getBigDecimal("r18_30_liab_inr"));


// =========================
// R19
// =========================
obj.setR19_product(rs.getString("r19_product"));
obj.setR19_31_asse_lc(rs.getBigDecimal("r19_31_asse_lc"));
obj.setR19_31_asse_inr(rs.getBigDecimal("r19_31_asse_inr"));
obj.setR19_31_liab_lc(rs.getBigDecimal("r19_31_liab_lc"));
obj.setR19_31_liab_inr(rs.getBigDecimal("r19_31_liab_inr"));
obj.setR19_30_asse_lc(rs.getBigDecimal("r19_30_asse_lc"));
obj.setR19_30_asse_inr(rs.getBigDecimal("r19_30_asse_inr"));
obj.setR19_30_liab_lc(rs.getBigDecimal("r19_30_liab_lc"));
obj.setR19_30_liab_inr(rs.getBigDecimal("r19_30_liab_inr"));


// =========================
// R20
// =========================
obj.setR20_product(rs.getString("r20_product"));
obj.setR20_31_asse_lc(rs.getBigDecimal("r20_31_asse_lc"));
obj.setR20_31_asse_inr(rs.getBigDecimal("r20_31_asse_inr"));
obj.setR20_31_liab_lc(rs.getBigDecimal("r20_31_liab_lc"));
obj.setR20_31_liab_inr(rs.getBigDecimal("r20_31_liab_inr"));
obj.setR20_30_asse_lc(rs.getBigDecimal("r20_30_asse_lc"));
obj.setR20_30_asse_inr(rs.getBigDecimal("r20_30_asse_inr"));
obj.setR20_30_liab_lc(rs.getBigDecimal("r20_30_liab_lc"));
obj.setR20_30_liab_inr(rs.getBigDecimal("r20_30_liab_inr"));

// =========================
// R21
// =========================
obj.setR21_product(rs.getString("r21_product"));
obj.setR21_31_asse_lc(rs.getBigDecimal("r21_31_asse_lc"));
obj.setR21_31_asse_inr(rs.getBigDecimal("r21_31_asse_inr"));
obj.setR21_31_liab_lc(rs.getBigDecimal("r21_31_liab_lc"));
obj.setR21_31_liab_inr(rs.getBigDecimal("r21_31_liab_inr"));
obj.setR21_30_asse_lc(rs.getBigDecimal("r21_30_asse_lc"));
obj.setR21_30_asse_inr(rs.getBigDecimal("r21_30_asse_inr"));
obj.setR21_30_liab_lc(rs.getBigDecimal("r21_30_liab_lc"));
obj.setR21_30_liab_inr(rs.getBigDecimal("r21_30_liab_inr"));

// =========================
// R22
// =========================
obj.setR22_product(rs.getString("r22_product"));
obj.setR22_31_asse_lc(rs.getBigDecimal("r22_31_asse_lc"));
obj.setR22_31_asse_inr(rs.getBigDecimal("r22_31_asse_inr"));
obj.setR22_31_liab_lc(rs.getBigDecimal("r22_31_liab_lc"));
obj.setR22_31_liab_inr(rs.getBigDecimal("r22_31_liab_inr"));
obj.setR22_30_asse_lc(rs.getBigDecimal("r22_30_asse_lc"));
obj.setR22_30_asse_inr(rs.getBigDecimal("r22_30_asse_inr"));
obj.setR22_30_liab_lc(rs.getBigDecimal("r22_30_liab_lc"));
obj.setR22_30_liab_inr(rs.getBigDecimal("r22_30_liab_inr"));

// =========================
// R23
// =========================
obj.setR23_product(rs.getString("r23_product"));
obj.setR23_31_asse_lc(rs.getBigDecimal("r23_31_asse_lc"));
obj.setR23_31_asse_inr(rs.getBigDecimal("r23_31_asse_inr"));
obj.setR23_31_liab_lc(rs.getBigDecimal("r23_31_liab_lc"));
obj.setR23_31_liab_inr(rs.getBigDecimal("r23_31_liab_inr"));
obj.setR23_30_asse_lc(rs.getBigDecimal("r23_30_asse_lc"));
obj.setR23_30_asse_inr(rs.getBigDecimal("r23_30_asse_inr"));
obj.setR23_30_liab_lc(rs.getBigDecimal("r23_30_liab_lc"));
obj.setR23_30_liab_inr(rs.getBigDecimal("r23_30_liab_inr"));

// =========================
// R24
// =========================
obj.setR24_product(rs.getString("r24_product"));
obj.setR24_31_asse_lc(rs.getBigDecimal("r24_31_asse_lc"));
obj.setR24_31_asse_inr(rs.getBigDecimal("r24_31_asse_inr"));
obj.setR24_31_liab_lc(rs.getBigDecimal("r24_31_liab_lc"));
obj.setR24_31_liab_inr(rs.getBigDecimal("r24_31_liab_inr"));
obj.setR24_30_asse_lc(rs.getBigDecimal("r24_30_asse_lc"));
obj.setR24_30_asse_inr(rs.getBigDecimal("r24_30_asse_inr"));
obj.setR24_30_liab_lc(rs.getBigDecimal("r24_30_liab_lc"));
obj.setR24_30_liab_inr(rs.getBigDecimal("r24_30_liab_inr"));

// =========================
// R25
// =========================
obj.setR25_product(rs.getString("r25_product"));
obj.setR25_31_asse_lc(rs.getBigDecimal("r25_31_asse_lc"));
obj.setR25_31_asse_inr(rs.getBigDecimal("r25_31_asse_inr"));
obj.setR25_31_liab_lc(rs.getBigDecimal("r25_31_liab_lc"));
obj.setR25_31_liab_inr(rs.getBigDecimal("r25_31_liab_inr"));
obj.setR25_30_asse_lc(rs.getBigDecimal("r25_30_asse_lc"));
obj.setR25_30_asse_inr(rs.getBigDecimal("r25_30_asse_inr"));
obj.setR25_30_liab_lc(rs.getBigDecimal("r25_30_liab_lc"));
obj.setR25_30_liab_inr(rs.getBigDecimal("r25_30_liab_inr"));

       

        // =========================
        // COMMON FIELDS
        // =========================
        obj.setReport_date(rs.getDate("report_date"));
        obj.setReport_version(rs.getBigDecimal("report_version"));
        obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
        obj.setReport_frequency(rs.getString("report_frequency"));
        obj.setReport_code(rs.getString("report_code"));
        obj.setReport_desc(rs.getString("report_desc"));
        obj.setEntity_flg(rs.getString("entity_flg"));
        obj.setModify_flg(rs.getString("modify_flg"));
        obj.setDel_flg(rs.getString("del_flg"));

        return obj;
    }
}

public class DEFERRED_TAX_Archival_Summary_Entity {
	
	

	private String	r11_product;
	private BigDecimal	r11_31_asse_lc;
	private BigDecimal	r11_31_asse_inr;
	private BigDecimal	r11_31_liab_lc;
	private BigDecimal	r11_31_liab_inr;
	private BigDecimal	r11_30_asse_lc;
	private BigDecimal	r11_30_asse_inr;
	private BigDecimal	r11_30_liab_lc;
	private BigDecimal	r11_30_liab_inr;
	private String	r12_product;
	private BigDecimal	r12_31_asse_lc;
	private BigDecimal	r12_31_asse_inr;
	private BigDecimal	r12_31_liab_lc;
	private BigDecimal	r12_31_liab_inr;
	private BigDecimal	r12_30_asse_lc;
	private BigDecimal	r12_30_asse_inr;
	private BigDecimal	r12_30_liab_lc;
	private BigDecimal	r12_30_liab_inr;
	private String	r13_product;
	private BigDecimal	r13_31_asse_lc;
	private BigDecimal	r13_31_asse_inr;
	private BigDecimal	r13_31_liab_lc;
	private BigDecimal	r13_31_liab_inr;
	private BigDecimal	r13_30_asse_lc;
	private BigDecimal	r13_30_asse_inr;
	private BigDecimal	r13_30_liab_lc;
	private BigDecimal	r13_30_liab_inr;
	private String	r14_product;
	private BigDecimal	r14_31_asse_lc;
	private BigDecimal	r14_31_asse_inr;
	private BigDecimal	r14_31_liab_lc;
	private BigDecimal	r14_31_liab_inr;
	private BigDecimal	r14_30_asse_lc;
	private BigDecimal	r14_30_asse_inr;
	private BigDecimal	r14_30_liab_lc;
	private BigDecimal	r14_30_liab_inr;
	private String	r15_product;
	private BigDecimal	r15_31_asse_lc;
	private BigDecimal	r15_31_asse_inr;
	private BigDecimal	r15_31_liab_lc;
	private BigDecimal	r15_31_liab_inr;
	private BigDecimal	r15_30_asse_lc;
	private BigDecimal	r15_30_asse_inr;
	private BigDecimal	r15_30_liab_lc;
	private BigDecimal	r15_30_liab_inr;
	private String	r16_product;
	private BigDecimal	r16_31_asse_lc;
	private BigDecimal	r16_31_asse_inr;
	private BigDecimal	r16_31_liab_lc;
	private BigDecimal	r16_31_liab_inr;
	private BigDecimal	r16_30_asse_lc;
	private BigDecimal	r16_30_asse_inr;
	private BigDecimal	r16_30_liab_lc;
	private BigDecimal	r16_30_liab_inr;
	private String	r17_product;
	private BigDecimal	r17_31_asse_lc;
	private BigDecimal	r17_31_asse_inr;
	private BigDecimal	r17_31_liab_lc;
	private BigDecimal	r17_31_liab_inr;
	private BigDecimal	r17_30_asse_lc;
	private BigDecimal	r17_30_asse_inr;
	private BigDecimal	r17_30_liab_lc;
	private BigDecimal	r17_30_liab_inr;
	private String	r18_product;
	private BigDecimal	r18_31_asse_lc;
	private BigDecimal	r18_31_asse_inr;
	private BigDecimal	r18_31_liab_lc;
	private BigDecimal	r18_31_liab_inr;
	private BigDecimal	r18_30_asse_lc;
	private BigDecimal	r18_30_asse_inr;
	private BigDecimal	r18_30_liab_lc;
	private BigDecimal	r18_30_liab_inr;
	private String	r19_product;
	private BigDecimal	r19_31_asse_lc;
	private BigDecimal	r19_31_asse_inr;
	private BigDecimal	r19_31_liab_lc;
	private BigDecimal	r19_31_liab_inr;
	private BigDecimal	r19_30_asse_lc;
	private BigDecimal	r19_30_asse_inr;
	private BigDecimal	r19_30_liab_lc;
	private BigDecimal	r19_30_liab_inr;
	private String	r20_product;
	private BigDecimal	r20_31_asse_lc;
	private BigDecimal	r20_31_asse_inr;
	private BigDecimal	r20_31_liab_lc;
	private BigDecimal	r20_31_liab_inr;
	private BigDecimal	r20_30_asse_lc;
	private BigDecimal	r20_30_asse_inr;
	private BigDecimal	r20_30_liab_lc;
	private BigDecimal	r20_30_liab_inr;
	private String	r21_product;
	private BigDecimal	r21_31_asse_lc;
	private BigDecimal	r21_31_asse_inr;
	private BigDecimal	r21_31_liab_lc;
	private BigDecimal	r21_31_liab_inr;
	private BigDecimal	r21_30_asse_lc;
	private BigDecimal	r21_30_asse_inr;
	private BigDecimal	r21_30_liab_lc;
	private BigDecimal	r21_30_liab_inr;
	private String	r22_product;
	private BigDecimal	r22_31_asse_lc;
	private BigDecimal	r22_31_asse_inr;
	private BigDecimal	r22_31_liab_lc;
	private BigDecimal	r22_31_liab_inr;
	private BigDecimal	r22_30_asse_lc;
	private BigDecimal	r22_30_asse_inr;
	private BigDecimal	r22_30_liab_lc;
	private BigDecimal	r22_30_liab_inr;
	private String	r23_product;
	private BigDecimal	r23_31_asse_lc;
	private BigDecimal	r23_31_asse_inr;
	private BigDecimal	r23_31_liab_lc;
	private BigDecimal	r23_31_liab_inr;
	private BigDecimal	r23_30_asse_lc;
	private BigDecimal	r23_30_asse_inr;
	private BigDecimal	r23_30_liab_lc;
	private BigDecimal	r23_30_liab_inr;
	private String	r24_product;
	private BigDecimal	r24_31_asse_lc;
	private BigDecimal	r24_31_asse_inr;
	private BigDecimal	r24_31_liab_lc;
	private BigDecimal	r24_31_liab_inr;
	private BigDecimal	r24_30_asse_lc;
	private BigDecimal	r24_30_asse_inr;
	private BigDecimal	r24_30_liab_lc;
	private BigDecimal	r24_30_liab_inr;
	private String	r25_product;
	private BigDecimal	r25_31_asse_lc;
	private BigDecimal	r25_31_asse_inr;
	private BigDecimal	r25_31_liab_lc;
	private BigDecimal	r25_31_liab_inr;
	private BigDecimal	r25_30_asse_lc;
	private BigDecimal	r25_30_asse_inr;
	private BigDecimal	r25_30_liab_lc;
	private BigDecimal	r25_30_liab_inr;

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
	
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_31_asse_lc() {
		return r11_31_asse_lc;
	}
	public void setR11_31_asse_lc(BigDecimal r11_31_asse_lc) {
		this.r11_31_asse_lc = r11_31_asse_lc;
	}
	public BigDecimal getR11_31_asse_inr() {
		return r11_31_asse_inr;
	}
	public void setR11_31_asse_inr(BigDecimal r11_31_asse_inr) {
		this.r11_31_asse_inr = r11_31_asse_inr;
	}
	public BigDecimal getR11_31_liab_lc() {
		return r11_31_liab_lc;
	}
	public void setR11_31_liab_lc(BigDecimal r11_31_liab_lc) {
		this.r11_31_liab_lc = r11_31_liab_lc;
	}
	public BigDecimal getR11_31_liab_inr() {
		return r11_31_liab_inr;
	}
	public void setR11_31_liab_inr(BigDecimal r11_31_liab_inr) {
		this.r11_31_liab_inr = r11_31_liab_inr;
	}
	public BigDecimal getR11_30_asse_lc() {
		return r11_30_asse_lc;
	}
	public void setR11_30_asse_lc(BigDecimal r11_30_asse_lc) {
		this.r11_30_asse_lc = r11_30_asse_lc;
	}
	public BigDecimal getR11_30_asse_inr() {
		return r11_30_asse_inr;
	}
	public void setR11_30_asse_inr(BigDecimal r11_30_asse_inr) {
		this.r11_30_asse_inr = r11_30_asse_inr;
	}
	public BigDecimal getR11_30_liab_lc() {
		return r11_30_liab_lc;
	}
	public void setR11_30_liab_lc(BigDecimal r11_30_liab_lc) {
		this.r11_30_liab_lc = r11_30_liab_lc;
	}
	public BigDecimal getR11_30_liab_inr() {
		return r11_30_liab_inr;
	}
	public void setR11_30_liab_inr(BigDecimal r11_30_liab_inr) {
		this.r11_30_liab_inr = r11_30_liab_inr;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_31_asse_lc() {
		return r12_31_asse_lc;
	}
	public void setR12_31_asse_lc(BigDecimal r12_31_asse_lc) {
		this.r12_31_asse_lc = r12_31_asse_lc;
	}
	public BigDecimal getR12_31_asse_inr() {
		return r12_31_asse_inr;
	}
	public void setR12_31_asse_inr(BigDecimal r12_31_asse_inr) {
		this.r12_31_asse_inr = r12_31_asse_inr;
	}
	public BigDecimal getR12_31_liab_lc() {
		return r12_31_liab_lc;
	}
	public void setR12_31_liab_lc(BigDecimal r12_31_liab_lc) {
		this.r12_31_liab_lc = r12_31_liab_lc;
	}
	public BigDecimal getR12_31_liab_inr() {
		return r12_31_liab_inr;
	}
	public void setR12_31_liab_inr(BigDecimal r12_31_liab_inr) {
		this.r12_31_liab_inr = r12_31_liab_inr;
	}
	public BigDecimal getR12_30_asse_lc() {
		return r12_30_asse_lc;
	}
	public void setR12_30_asse_lc(BigDecimal r12_30_asse_lc) {
		this.r12_30_asse_lc = r12_30_asse_lc;
	}
	public BigDecimal getR12_30_asse_inr() {
		return r12_30_asse_inr;
	}
	public void setR12_30_asse_inr(BigDecimal r12_30_asse_inr) {
		this.r12_30_asse_inr = r12_30_asse_inr;
	}
	public BigDecimal getR12_30_liab_lc() {
		return r12_30_liab_lc;
	}
	public void setR12_30_liab_lc(BigDecimal r12_30_liab_lc) {
		this.r12_30_liab_lc = r12_30_liab_lc;
	}
	public BigDecimal getR12_30_liab_inr() {
		return r12_30_liab_inr;
	}
	public void setR12_30_liab_inr(BigDecimal r12_30_liab_inr) {
		this.r12_30_liab_inr = r12_30_liab_inr;
	}
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_31_asse_lc() {
		return r13_31_asse_lc;
	}
	public void setR13_31_asse_lc(BigDecimal r13_31_asse_lc) {
		this.r13_31_asse_lc = r13_31_asse_lc;
	}
	public BigDecimal getR13_31_asse_inr() {
		return r13_31_asse_inr;
	}
	public void setR13_31_asse_inr(BigDecimal r13_31_asse_inr) {
		this.r13_31_asse_inr = r13_31_asse_inr;
	}
	public BigDecimal getR13_31_liab_lc() {
		return r13_31_liab_lc;
	}
	public void setR13_31_liab_lc(BigDecimal r13_31_liab_lc) {
		this.r13_31_liab_lc = r13_31_liab_lc;
	}
	public BigDecimal getR13_31_liab_inr() {
		return r13_31_liab_inr;
	}
	public void setR13_31_liab_inr(BigDecimal r13_31_liab_inr) {
		this.r13_31_liab_inr = r13_31_liab_inr;
	}
	public BigDecimal getR13_30_asse_lc() {
		return r13_30_asse_lc;
	}
	public void setR13_30_asse_lc(BigDecimal r13_30_asse_lc) {
		this.r13_30_asse_lc = r13_30_asse_lc;
	}
	public BigDecimal getR13_30_asse_inr() {
		return r13_30_asse_inr;
	}
	public void setR13_30_asse_inr(BigDecimal r13_30_asse_inr) {
		this.r13_30_asse_inr = r13_30_asse_inr;
	}
	public BigDecimal getR13_30_liab_lc() {
		return r13_30_liab_lc;
	}
	public void setR13_30_liab_lc(BigDecimal r13_30_liab_lc) {
		this.r13_30_liab_lc = r13_30_liab_lc;
	}
	public BigDecimal getR13_30_liab_inr() {
		return r13_30_liab_inr;
	}
	public void setR13_30_liab_inr(BigDecimal r13_30_liab_inr) {
		this.r13_30_liab_inr = r13_30_liab_inr;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_31_asse_lc() {
		return r14_31_asse_lc;
	}
	public void setR14_31_asse_lc(BigDecimal r14_31_asse_lc) {
		this.r14_31_asse_lc = r14_31_asse_lc;
	}
	public BigDecimal getR14_31_asse_inr() {
		return r14_31_asse_inr;
	}
	public void setR14_31_asse_inr(BigDecimal r14_31_asse_inr) {
		this.r14_31_asse_inr = r14_31_asse_inr;
	}
	public BigDecimal getR14_31_liab_lc() {
		return r14_31_liab_lc;
	}
	public void setR14_31_liab_lc(BigDecimal r14_31_liab_lc) {
		this.r14_31_liab_lc = r14_31_liab_lc;
	}
	public BigDecimal getR14_31_liab_inr() {
		return r14_31_liab_inr;
	}
	public void setR14_31_liab_inr(BigDecimal r14_31_liab_inr) {
		this.r14_31_liab_inr = r14_31_liab_inr;
	}
	public BigDecimal getR14_30_asse_lc() {
		return r14_30_asse_lc;
	}
	public void setR14_30_asse_lc(BigDecimal r14_30_asse_lc) {
		this.r14_30_asse_lc = r14_30_asse_lc;
	}
	public BigDecimal getR14_30_asse_inr() {
		return r14_30_asse_inr;
	}
	public void setR14_30_asse_inr(BigDecimal r14_30_asse_inr) {
		this.r14_30_asse_inr = r14_30_asse_inr;
	}
	public BigDecimal getR14_30_liab_lc() {
		return r14_30_liab_lc;
	}
	public void setR14_30_liab_lc(BigDecimal r14_30_liab_lc) {
		this.r14_30_liab_lc = r14_30_liab_lc;
	}
	public BigDecimal getR14_30_liab_inr() {
		return r14_30_liab_inr;
	}
	public void setR14_30_liab_inr(BigDecimal r14_30_liab_inr) {
		this.r14_30_liab_inr = r14_30_liab_inr;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_31_asse_lc() {
		return r15_31_asse_lc;
	}
	public void setR15_31_asse_lc(BigDecimal r15_31_asse_lc) {
		this.r15_31_asse_lc = r15_31_asse_lc;
	}
	public BigDecimal getR15_31_asse_inr() {
		return r15_31_asse_inr;
	}
	public void setR15_31_asse_inr(BigDecimal r15_31_asse_inr) {
		this.r15_31_asse_inr = r15_31_asse_inr;
	}
	public BigDecimal getR15_31_liab_lc() {
		return r15_31_liab_lc;
	}
	public void setR15_31_liab_lc(BigDecimal r15_31_liab_lc) {
		this.r15_31_liab_lc = r15_31_liab_lc;
	}
	public BigDecimal getR15_31_liab_inr() {
		return r15_31_liab_inr;
	}
	public void setR15_31_liab_inr(BigDecimal r15_31_liab_inr) {
		this.r15_31_liab_inr = r15_31_liab_inr;
	}
	public BigDecimal getR15_30_asse_lc() {
		return r15_30_asse_lc;
	}
	public void setR15_30_asse_lc(BigDecimal r15_30_asse_lc) {
		this.r15_30_asse_lc = r15_30_asse_lc;
	}
	public BigDecimal getR15_30_asse_inr() {
		return r15_30_asse_inr;
	}
	public void setR15_30_asse_inr(BigDecimal r15_30_asse_inr) {
		this.r15_30_asse_inr = r15_30_asse_inr;
	}
	public BigDecimal getR15_30_liab_lc() {
		return r15_30_liab_lc;
	}
	public void setR15_30_liab_lc(BigDecimal r15_30_liab_lc) {
		this.r15_30_liab_lc = r15_30_liab_lc;
	}
	public BigDecimal getR15_30_liab_inr() {
		return r15_30_liab_inr;
	}
	public void setR15_30_liab_inr(BigDecimal r15_30_liab_inr) {
		this.r15_30_liab_inr = r15_30_liab_inr;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_31_asse_lc() {
		return r16_31_asse_lc;
	}
	public void setR16_31_asse_lc(BigDecimal r16_31_asse_lc) {
		this.r16_31_asse_lc = r16_31_asse_lc;
	}
	public BigDecimal getR16_31_asse_inr() {
		return r16_31_asse_inr;
	}
	public void setR16_31_asse_inr(BigDecimal r16_31_asse_inr) {
		this.r16_31_asse_inr = r16_31_asse_inr;
	}
	public BigDecimal getR16_31_liab_lc() {
		return r16_31_liab_lc;
	}
	public void setR16_31_liab_lc(BigDecimal r16_31_liab_lc) {
		this.r16_31_liab_lc = r16_31_liab_lc;
	}
	public BigDecimal getR16_31_liab_inr() {
		return r16_31_liab_inr;
	}
	public void setR16_31_liab_inr(BigDecimal r16_31_liab_inr) {
		this.r16_31_liab_inr = r16_31_liab_inr;
	}
	public BigDecimal getR16_30_asse_lc() {
		return r16_30_asse_lc;
	}
	public void setR16_30_asse_lc(BigDecimal r16_30_asse_lc) {
		this.r16_30_asse_lc = r16_30_asse_lc;
	}
	public BigDecimal getR16_30_asse_inr() {
		return r16_30_asse_inr;
	}
	public void setR16_30_asse_inr(BigDecimal r16_30_asse_inr) {
		this.r16_30_asse_inr = r16_30_asse_inr;
	}
	public BigDecimal getR16_30_liab_lc() {
		return r16_30_liab_lc;
	}
	public void setR16_30_liab_lc(BigDecimal r16_30_liab_lc) {
		this.r16_30_liab_lc = r16_30_liab_lc;
	}
	public BigDecimal getR16_30_liab_inr() {
		return r16_30_liab_inr;
	}
	public void setR16_30_liab_inr(BigDecimal r16_30_liab_inr) {
		this.r16_30_liab_inr = r16_30_liab_inr;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_31_asse_lc() {
		return r17_31_asse_lc;
	}
	public void setR17_31_asse_lc(BigDecimal r17_31_asse_lc) {
		this.r17_31_asse_lc = r17_31_asse_lc;
	}
	public BigDecimal getR17_31_asse_inr() {
		return r17_31_asse_inr;
	}
	public void setR17_31_asse_inr(BigDecimal r17_31_asse_inr) {
		this.r17_31_asse_inr = r17_31_asse_inr;
	}
	public BigDecimal getR17_31_liab_lc() {
		return r17_31_liab_lc;
	}
	public void setR17_31_liab_lc(BigDecimal r17_31_liab_lc) {
		this.r17_31_liab_lc = r17_31_liab_lc;
	}
	public BigDecimal getR17_31_liab_inr() {
		return r17_31_liab_inr;
	}
	public void setR17_31_liab_inr(BigDecimal r17_31_liab_inr) {
		this.r17_31_liab_inr = r17_31_liab_inr;
	}
	public BigDecimal getR17_30_asse_lc() {
		return r17_30_asse_lc;
	}
	public void setR17_30_asse_lc(BigDecimal r17_30_asse_lc) {
		this.r17_30_asse_lc = r17_30_asse_lc;
	}
	public BigDecimal getR17_30_asse_inr() {
		return r17_30_asse_inr;
	}
	public void setR17_30_asse_inr(BigDecimal r17_30_asse_inr) {
		this.r17_30_asse_inr = r17_30_asse_inr;
	}
	public BigDecimal getR17_30_liab_lc() {
		return r17_30_liab_lc;
	}
	public void setR17_30_liab_lc(BigDecimal r17_30_liab_lc) {
		this.r17_30_liab_lc = r17_30_liab_lc;
	}
	public BigDecimal getR17_30_liab_inr() {
		return r17_30_liab_inr;
	}
	public void setR17_30_liab_inr(BigDecimal r17_30_liab_inr) {
		this.r17_30_liab_inr = r17_30_liab_inr;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_31_asse_lc() {
		return r18_31_asse_lc;
	}
	public void setR18_31_asse_lc(BigDecimal r18_31_asse_lc) {
		this.r18_31_asse_lc = r18_31_asse_lc;
	}
	public BigDecimal getR18_31_asse_inr() {
		return r18_31_asse_inr;
	}
	public void setR18_31_asse_inr(BigDecimal r18_31_asse_inr) {
		this.r18_31_asse_inr = r18_31_asse_inr;
	}
	public BigDecimal getR18_31_liab_lc() {
		return r18_31_liab_lc;
	}
	public void setR18_31_liab_lc(BigDecimal r18_31_liab_lc) {
		this.r18_31_liab_lc = r18_31_liab_lc;
	}
	public BigDecimal getR18_31_liab_inr() {
		return r18_31_liab_inr;
	}
	public void setR18_31_liab_inr(BigDecimal r18_31_liab_inr) {
		this.r18_31_liab_inr = r18_31_liab_inr;
	}
	public BigDecimal getR18_30_asse_lc() {
		return r18_30_asse_lc;
	}
	public void setR18_30_asse_lc(BigDecimal r18_30_asse_lc) {
		this.r18_30_asse_lc = r18_30_asse_lc;
	}
	public BigDecimal getR18_30_asse_inr() {
		return r18_30_asse_inr;
	}
	public void setR18_30_asse_inr(BigDecimal r18_30_asse_inr) {
		this.r18_30_asse_inr = r18_30_asse_inr;
	}
	public BigDecimal getR18_30_liab_lc() {
		return r18_30_liab_lc;
	}
	public void setR18_30_liab_lc(BigDecimal r18_30_liab_lc) {
		this.r18_30_liab_lc = r18_30_liab_lc;
	}
	public BigDecimal getR18_30_liab_inr() {
		return r18_30_liab_inr;
	}
	public void setR18_30_liab_inr(BigDecimal r18_30_liab_inr) {
		this.r18_30_liab_inr = r18_30_liab_inr;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_31_asse_lc() {
		return r19_31_asse_lc;
	}
	public void setR19_31_asse_lc(BigDecimal r19_31_asse_lc) {
		this.r19_31_asse_lc = r19_31_asse_lc;
	}
	public BigDecimal getR19_31_asse_inr() {
		return r19_31_asse_inr;
	}
	public void setR19_31_asse_inr(BigDecimal r19_31_asse_inr) {
		this.r19_31_asse_inr = r19_31_asse_inr;
	}
	public BigDecimal getR19_31_liab_lc() {
		return r19_31_liab_lc;
	}
	public void setR19_31_liab_lc(BigDecimal r19_31_liab_lc) {
		this.r19_31_liab_lc = r19_31_liab_lc;
	}
	public BigDecimal getR19_31_liab_inr() {
		return r19_31_liab_inr;
	}
	public void setR19_31_liab_inr(BigDecimal r19_31_liab_inr) {
		this.r19_31_liab_inr = r19_31_liab_inr;
	}
	public BigDecimal getR19_30_asse_lc() {
		return r19_30_asse_lc;
	}
	public void setR19_30_asse_lc(BigDecimal r19_30_asse_lc) {
		this.r19_30_asse_lc = r19_30_asse_lc;
	}
	public BigDecimal getR19_30_asse_inr() {
		return r19_30_asse_inr;
	}
	public void setR19_30_asse_inr(BigDecimal r19_30_asse_inr) {
		this.r19_30_asse_inr = r19_30_asse_inr;
	}
	public BigDecimal getR19_30_liab_lc() {
		return r19_30_liab_lc;
	}
	public void setR19_30_liab_lc(BigDecimal r19_30_liab_lc) {
		this.r19_30_liab_lc = r19_30_liab_lc;
	}
	public BigDecimal getR19_30_liab_inr() {
		return r19_30_liab_inr;
	}
	public void setR19_30_liab_inr(BigDecimal r19_30_liab_inr) {
		this.r19_30_liab_inr = r19_30_liab_inr;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_31_asse_lc() {
		return r20_31_asse_lc;
	}
	public void setR20_31_asse_lc(BigDecimal r20_31_asse_lc) {
		this.r20_31_asse_lc = r20_31_asse_lc;
	}
	public BigDecimal getR20_31_asse_inr() {
		return r20_31_asse_inr;
	}
	public void setR20_31_asse_inr(BigDecimal r20_31_asse_inr) {
		this.r20_31_asse_inr = r20_31_asse_inr;
	}
	public BigDecimal getR20_31_liab_lc() {
		return r20_31_liab_lc;
	}
	public void setR20_31_liab_lc(BigDecimal r20_31_liab_lc) {
		this.r20_31_liab_lc = r20_31_liab_lc;
	}
	public BigDecimal getR20_31_liab_inr() {
		return r20_31_liab_inr;
	}
	public void setR20_31_liab_inr(BigDecimal r20_31_liab_inr) {
		this.r20_31_liab_inr = r20_31_liab_inr;
	}
	public BigDecimal getR20_30_asse_lc() {
		return r20_30_asse_lc;
	}
	public void setR20_30_asse_lc(BigDecimal r20_30_asse_lc) {
		this.r20_30_asse_lc = r20_30_asse_lc;
	}
	public BigDecimal getR20_30_asse_inr() {
		return r20_30_asse_inr;
	}
	public void setR20_30_asse_inr(BigDecimal r20_30_asse_inr) {
		this.r20_30_asse_inr = r20_30_asse_inr;
	}
	public BigDecimal getR20_30_liab_lc() {
		return r20_30_liab_lc;
	}
	public void setR20_30_liab_lc(BigDecimal r20_30_liab_lc) {
		this.r20_30_liab_lc = r20_30_liab_lc;
	}
	public BigDecimal getR20_30_liab_inr() {
		return r20_30_liab_inr;
	}
	public void setR20_30_liab_inr(BigDecimal r20_30_liab_inr) {
		this.r20_30_liab_inr = r20_30_liab_inr;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_31_asse_lc() {
		return r21_31_asse_lc;
	}
	public void setR21_31_asse_lc(BigDecimal r21_31_asse_lc) {
		this.r21_31_asse_lc = r21_31_asse_lc;
	}
	public BigDecimal getR21_31_asse_inr() {
		return r21_31_asse_inr;
	}
	public void setR21_31_asse_inr(BigDecimal r21_31_asse_inr) {
		this.r21_31_asse_inr = r21_31_asse_inr;
	}
	public BigDecimal getR21_31_liab_lc() {
		return r21_31_liab_lc;
	}
	public void setR21_31_liab_lc(BigDecimal r21_31_liab_lc) {
		this.r21_31_liab_lc = r21_31_liab_lc;
	}
	public BigDecimal getR21_31_liab_inr() {
		return r21_31_liab_inr;
	}
	public void setR21_31_liab_inr(BigDecimal r21_31_liab_inr) {
		this.r21_31_liab_inr = r21_31_liab_inr;
	}
	public BigDecimal getR21_30_asse_lc() {
		return r21_30_asse_lc;
	}
	public void setR21_30_asse_lc(BigDecimal r21_30_asse_lc) {
		this.r21_30_asse_lc = r21_30_asse_lc;
	}
	public BigDecimal getR21_30_asse_inr() {
		return r21_30_asse_inr;
	}
	public void setR21_30_asse_inr(BigDecimal r21_30_asse_inr) {
		this.r21_30_asse_inr = r21_30_asse_inr;
	}
	public BigDecimal getR21_30_liab_lc() {
		return r21_30_liab_lc;
	}
	public void setR21_30_liab_lc(BigDecimal r21_30_liab_lc) {
		this.r21_30_liab_lc = r21_30_liab_lc;
	}
	public BigDecimal getR21_30_liab_inr() {
		return r21_30_liab_inr;
	}
	public void setR21_30_liab_inr(BigDecimal r21_30_liab_inr) {
		this.r21_30_liab_inr = r21_30_liab_inr;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_31_asse_lc() {
		return r22_31_asse_lc;
	}
	public void setR22_31_asse_lc(BigDecimal r22_31_asse_lc) {
		this.r22_31_asse_lc = r22_31_asse_lc;
	}
	public BigDecimal getR22_31_asse_inr() {
		return r22_31_asse_inr;
	}
	public void setR22_31_asse_inr(BigDecimal r22_31_asse_inr) {
		this.r22_31_asse_inr = r22_31_asse_inr;
	}
	public BigDecimal getR22_31_liab_lc() {
		return r22_31_liab_lc;
	}
	public void setR22_31_liab_lc(BigDecimal r22_31_liab_lc) {
		this.r22_31_liab_lc = r22_31_liab_lc;
	}
	public BigDecimal getR22_31_liab_inr() {
		return r22_31_liab_inr;
	}
	public void setR22_31_liab_inr(BigDecimal r22_31_liab_inr) {
		this.r22_31_liab_inr = r22_31_liab_inr;
	}
	public BigDecimal getR22_30_asse_lc() {
		return r22_30_asse_lc;
	}
	public void setR22_30_asse_lc(BigDecimal r22_30_asse_lc) {
		this.r22_30_asse_lc = r22_30_asse_lc;
	}
	public BigDecimal getR22_30_asse_inr() {
		return r22_30_asse_inr;
	}
	public void setR22_30_asse_inr(BigDecimal r22_30_asse_inr) {
		this.r22_30_asse_inr = r22_30_asse_inr;
	}
	public BigDecimal getR22_30_liab_lc() {
		return r22_30_liab_lc;
	}
	public void setR22_30_liab_lc(BigDecimal r22_30_liab_lc) {
		this.r22_30_liab_lc = r22_30_liab_lc;
	}
	public BigDecimal getR22_30_liab_inr() {
		return r22_30_liab_inr;
	}
	public void setR22_30_liab_inr(BigDecimal r22_30_liab_inr) {
		this.r22_30_liab_inr = r22_30_liab_inr;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_31_asse_lc() {
		return r23_31_asse_lc;
	}
	public void setR23_31_asse_lc(BigDecimal r23_31_asse_lc) {
		this.r23_31_asse_lc = r23_31_asse_lc;
	}
	public BigDecimal getR23_31_asse_inr() {
		return r23_31_asse_inr;
	}
	public void setR23_31_asse_inr(BigDecimal r23_31_asse_inr) {
		this.r23_31_asse_inr = r23_31_asse_inr;
	}
	public BigDecimal getR23_31_liab_lc() {
		return r23_31_liab_lc;
	}
	public void setR23_31_liab_lc(BigDecimal r23_31_liab_lc) {
		this.r23_31_liab_lc = r23_31_liab_lc;
	}
	public BigDecimal getR23_31_liab_inr() {
		return r23_31_liab_inr;
	}
	public void setR23_31_liab_inr(BigDecimal r23_31_liab_inr) {
		this.r23_31_liab_inr = r23_31_liab_inr;
	}
	public BigDecimal getR23_30_asse_lc() {
		return r23_30_asse_lc;
	}
	public void setR23_30_asse_lc(BigDecimal r23_30_asse_lc) {
		this.r23_30_asse_lc = r23_30_asse_lc;
	}
	public BigDecimal getR23_30_asse_inr() {
		return r23_30_asse_inr;
	}
	public void setR23_30_asse_inr(BigDecimal r23_30_asse_inr) {
		this.r23_30_asse_inr = r23_30_asse_inr;
	}
	public BigDecimal getR23_30_liab_lc() {
		return r23_30_liab_lc;
	}
	public void setR23_30_liab_lc(BigDecimal r23_30_liab_lc) {
		this.r23_30_liab_lc = r23_30_liab_lc;
	}
	public BigDecimal getR23_30_liab_inr() {
		return r23_30_liab_inr;
	}
	public void setR23_30_liab_inr(BigDecimal r23_30_liab_inr) {
		this.r23_30_liab_inr = r23_30_liab_inr;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_31_asse_lc() {
		return r24_31_asse_lc;
	}
	public void setR24_31_asse_lc(BigDecimal r24_31_asse_lc) {
		this.r24_31_asse_lc = r24_31_asse_lc;
	}
	public BigDecimal getR24_31_asse_inr() {
		return r24_31_asse_inr;
	}
	public void setR24_31_asse_inr(BigDecimal r24_31_asse_inr) {
		this.r24_31_asse_inr = r24_31_asse_inr;
	}
	public BigDecimal getR24_31_liab_lc() {
		return r24_31_liab_lc;
	}
	public void setR24_31_liab_lc(BigDecimal r24_31_liab_lc) {
		this.r24_31_liab_lc = r24_31_liab_lc;
	}
	public BigDecimal getR24_31_liab_inr() {
		return r24_31_liab_inr;
	}
	public void setR24_31_liab_inr(BigDecimal r24_31_liab_inr) {
		this.r24_31_liab_inr = r24_31_liab_inr;
	}
	public BigDecimal getR24_30_asse_lc() {
		return r24_30_asse_lc;
	}
	public void setR24_30_asse_lc(BigDecimal r24_30_asse_lc) {
		this.r24_30_asse_lc = r24_30_asse_lc;
	}
	public BigDecimal getR24_30_asse_inr() {
		return r24_30_asse_inr;
	}
	public void setR24_30_asse_inr(BigDecimal r24_30_asse_inr) {
		this.r24_30_asse_inr = r24_30_asse_inr;
	}
	public BigDecimal getR24_30_liab_lc() {
		return r24_30_liab_lc;
	}
	public void setR24_30_liab_lc(BigDecimal r24_30_liab_lc) {
		this.r24_30_liab_lc = r24_30_liab_lc;
	}
	public BigDecimal getR24_30_liab_inr() {
		return r24_30_liab_inr;
	}
	public void setR24_30_liab_inr(BigDecimal r24_30_liab_inr) {
		this.r24_30_liab_inr = r24_30_liab_inr;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_31_asse_lc() {
		return r25_31_asse_lc;
	}
	public void setR25_31_asse_lc(BigDecimal r25_31_asse_lc) {
		this.r25_31_asse_lc = r25_31_asse_lc;
	}
	public BigDecimal getR25_31_asse_inr() {
		return r25_31_asse_inr;
	}
	public void setR25_31_asse_inr(BigDecimal r25_31_asse_inr) {
		this.r25_31_asse_inr = r25_31_asse_inr;
	}
	public BigDecimal getR25_31_liab_lc() {
		return r25_31_liab_lc;
	}
	public void setR25_31_liab_lc(BigDecimal r25_31_liab_lc) {
		this.r25_31_liab_lc = r25_31_liab_lc;
	}
	public BigDecimal getR25_31_liab_inr() {
		return r25_31_liab_inr;
	}
	public void setR25_31_liab_inr(BigDecimal r25_31_liab_inr) {
		this.r25_31_liab_inr = r25_31_liab_inr;
	}
	public BigDecimal getR25_30_asse_lc() {
		return r25_30_asse_lc;
	}
	public void setR25_30_asse_lc(BigDecimal r25_30_asse_lc) {
		this.r25_30_asse_lc = r25_30_asse_lc;
	}
	public BigDecimal getR25_30_asse_inr() {
		return r25_30_asse_inr;
	}
	public void setR25_30_asse_inr(BigDecimal r25_30_asse_inr) {
		this.r25_30_asse_inr = r25_30_asse_inr;
	}
	public BigDecimal getR25_30_liab_lc() {
		return r25_30_liab_lc;
	}
	public void setR25_30_liab_lc(BigDecimal r25_30_liab_lc) {
		this.r25_30_liab_lc = r25_30_liab_lc;
	}
	public BigDecimal getR25_30_liab_inr() {
		return r25_30_liab_inr;
	}
	public void setR25_30_liab_inr(BigDecimal r25_30_liab_inr) {
		this.r25_30_liab_inr = r25_30_liab_inr;
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


//  DETAIL ENTITY CLASS

public class DEFERRED_TAX_Detail_RowMapper
        implements RowMapper<DEFERRED_TAX_Detail_Entity> {

    @Override
    public DEFERRED_TAX_Detail_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        DEFERRED_TAX_Detail_Entity obj = new DEFERRED_TAX_Detail_Entity();

        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));
        obj.setDataType(rs.getString("DATA_TYPE"));
        obj.setReportName(rs.getString("REPORT_NAME"));
        obj.setReportLabel(rs.getString("REPORT_LABEL"));
        obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
        obj.setReportAddlCriteria_2(rs.getString("REPORT_ADDL_CRITERIA_2"));
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

        // char fields (handle null safely)
        obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');
        obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');
        obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

        return obj;
    }
}


public class DEFERRED_TAX_Detail_Entity {

   
	
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
  
   @Column(name = "REPORT_ADDL_CRITERIA_2")
  private String reportAddlCriteria_2;
  
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

public String getReportAddlCriteria_2() {
	return reportAddlCriteria_2;
}

public void setReportAddlCriteria_2(String reportAddlCriteria_2) {
	this.reportAddlCriteria_2 = reportAddlCriteria_2;
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

// ARCHIVAL DETAIL ENTITY CLASS 

public class DEFERRED_TAX_Archival_Detail_RowMapper
        implements RowMapper<DEFERRED_TAX_Archival_Detail_Entity> {

    @Override
    public DEFERRED_TAX_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        DEFERRED_TAX_Archival_Detail_Entity obj = new DEFERRED_TAX_Archival_Detail_Entity();

        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));
        obj.setDataType(rs.getString("DATA_TYPE"));
        obj.setReportName(rs.getString("REPORT_NAME"));
        obj.setReportLabel(rs.getString("REPORT_LABEL"));
        obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
        obj.setReportAddlCriteria_2(rs.getString("REPORT_ADDL_CRITERIA_2"));
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

        // char fields (safe handling)
        obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');
        obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');
        obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

        return obj;
    }
}

public class DEFERRED_TAX_Archival_Detail_Entity {

   
	
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
  
   @Column(name = "REPORT_ADDL_CRITERIA_2")
  private String reportAddlCriteria_2;
  
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

public String getReportAddlCriteria_2() {
	return reportAddlCriteria_2;
}

public void setReportAddlCriteria_2(String reportAddlCriteria_2) {
	this.reportAddlCriteria_2 = reportAddlCriteria_2;
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
// MODEL AND VIEW METHOD summary
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
		 		 	 public ModelAndView getDTAXView(

	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("DTAX View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    // =====================================================
	    // ARCHIVAL MODE
	    // =====================================================

	    if ("ARCHIVAL".equals(type) && version != null) {

	        List<DEFERRED_TAX_Archival_Summary_Entity> T1Master = new ArrayList<>();
	    

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

	        List<DEFERRED_TAX_Summary_Entity> T1Master = new ArrayList<>();
	       

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

	    mv.setViewName("BRRS/DTAX");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getDTAXcurrentDtl(
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

	            List<DEFERRED_TAX_Archival_Detail_Entity> archivalDetailList;

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

	            List<DEFERRED_TAX_Detail_Entity> currentDetailList;

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

	    mv.setViewName("BRRS/DTAX");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
	
//=====================================================
// MODEL AND VIEW METHOD ARCHIVAL VIEW
//=====================================================

public List<Object[]> getDTAXArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<DEFERRED_TAX_Archival_Summary_Entity> repoData =
						getarchivaldatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (DEFERRED_TAX_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					DEFERRED_TAX_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  DTAX  Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}
		
//=====================================================
// UPDATE REPORT
//=====================================================

public void updateReport(
		        DEFERRED_TAX_Summary_Entity updatedEntity) {

		    System.out.println("Came to DTAX Manual Update");
		    System.out.println("Report Date: " + updatedEntity.getReport_date());

		 	int[] rows = { };

		String[] fields = { " "};
		    try {

		      for (int i : rows) {
				for (String field : fields) {

					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;

		                try {

		                    Method getter =
		                            DEFERRED_TAX_Summary_Entity.class
		                                    .getMethod(getterName);

		                    Object value =
		                            getter.invoke(updatedEntity);

		                    // Skip null values
		                    if (value == null) continue;

		                    // Column name in DB
		                    String columnName =
		                    		"R" + i + "_" + field;

		                    String sql =
		                            "UPDATE BRRS_FORMAT_II_SUMMARYTABLE " +
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

		        System.out.println("DTAX Manual Update Completed");

		    } catch (Exception e) {
		        throw new RuntimeException(
		                "Error while updating DTAX Manual fields", e);
		    }
		}
		
//=====================================================
// VIEW AND EDIT
//=====================================================

public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/DTAX"); 

		if (acctNo != null) {
			DEFERRED_TAX_Detail_Entity dtaxEntity = findByDetailAcctnumber(acctNo);
			if (dtaxEntity != null && dtaxEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(dtaxEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("d_taxData", dtaxEntity);
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

			DEFERRED_TAX_Detail_Entity existing = findByDetailAcctnumber(acctNo);
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
    "UPDATE DEFERRED_TAX_Detail_Entity " +
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
							logger.info("Transaction committed — calling BRRS_DEFERRED_TAX_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_DEFERRED_TAX_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating DTAX record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
//=====================================================
// DETAIL EXCEL 
//=====================================================


	public byte[] getDTAXDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  DTAX  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getDTAXDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("DTAX' Details ");

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
					String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
				List<DEFERRED_TAX_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (DEFERRED_TAX_Detail_Entity item : reportData) { 
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
						 if (j != 3  ) {
							row.getCell(j).setCellStyle(dataStyle);
						}
						}
					}
				} else {
					logger.info("No data found for DATX — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating DTAX Excel", e);
				return new byte[0];
			}
		}
		





//=====================================================
// ARCHIVAL DETAIL EXCEL 
//=====================================================

	public byte[] getDTAXDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for DTAX ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("DTAX Detail NEW");

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
					String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
				List<DEFERRED_TAX_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (DEFERRED_TAX_Archival_Detail_Entity item : reportData) {
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
						 if (j != 3 ) {
							row.getCell(j).setCellStyle(dataStyle);
						}
						}
					}
				} else {
					logger.info("No data found for DTAX — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating DTAX NEW Excel", e);
				return new byte[0];
			}
		}


//=====================================================
// Summary EXCEL 
//=====================================================

	public byte[] getDTAXExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.DTAX");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelDTAXARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<DEFERRED_TAX_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		
		
			System.out.println("DATA SIZE IS : "+dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  DATX report. Returning empty result.");
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

						int startRow = 10;
						
				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						DEFERRED_TAX_Summary_Entity record = dataList.get(i);
					
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


					Cell cellB , cellC ,cellD , cellE , cellF , cellG , cellH, cellI;
//ROW 11
                    //COLUMN B 
					  
                    cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR11_31_asse_lc() != null) {
					    cellB.setCellValue(record.getR11_31_asse_lc().doubleValue());
					} else {
					    cellB.setCellValue(0);
					}

                    
                  //COLUMN C 

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR11_31_asse_inr() != null) {
					    cellC.setCellValue(record.getR11_31_asse_inr().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}
					
					
                  //COLUMN D 
					
					
					cellD = row.getCell(3);
					if (cellD == null) cellD= row.createCell(3);
					if (record.getR11_31_liab_lc() != null) {
					    cellD.setCellValue(record.getR11_31_liab_lc().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}
					
					
                  //COLUMN E
					
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR11_31_liab_inr() != null) {
					    cellE.setCellValue(record.getR11_31_liab_inr().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}
					
					
                  //COLUMN F 
					
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR11_30_asse_lc() != null) {
					    cellF.setCellValue(record.getR11_30_asse_lc().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}
					
					
                  //COLUMN G 
					
					
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR11_30_asse_inr() != null) {
					    cellG.setCellValue(record.getR11_30_asse_inr().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					
					
					
                  //COLUMN H
					
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR11_30_liab_lc() != null) {
					    cellH.setCellValue(record.getR11_30_liab_lc().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}
					
					
                  //COLUMN I 
					
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR11_30_liab_inr() != null) {
					    cellI.setCellValue(record.getR11_30_liab_inr().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					
					
					// ROW 12

row = sheet.getRow(11);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR12_31_asse_lc() != null) {
cellB.setCellValue(record.getR12_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR12_31_asse_inr() != null) {
cellC.setCellValue(record.getR12_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR12_31_liab_lc() != null) {
cellD.setCellValue(record.getR12_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR12_31_liab_inr() != null) {
cellE.setCellValue(record.getR12_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR12_30_asse_lc() != null) {
cellF.setCellValue(record.getR12_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR12_30_asse_inr() != null) {
cellG.setCellValue(record.getR12_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR12_30_liab_lc() != null) {
cellH.setCellValue(record.getR12_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR12_30_liab_inr() != null) {
cellI.setCellValue(record.getR12_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 13

row = sheet.getRow(12);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR13_31_asse_lc() != null) {
cellB.setCellValue(record.getR13_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR13_31_asse_inr() != null) {
cellC.setCellValue(record.getR13_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR13_31_liab_lc() != null) {
cellD.setCellValue(record.getR13_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR13_31_liab_inr() != null) {
cellE.setCellValue(record.getR13_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR13_30_asse_lc() != null) {
cellF.setCellValue(record.getR13_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR13_30_asse_inr() != null) {
cellG.setCellValue(record.getR13_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR13_30_liab_lc() != null) {
cellH.setCellValue(record.getR13_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR13_30_liab_inr() != null) {
cellI.setCellValue(record.getR13_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 14

row = sheet.getRow(13);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR14_31_asse_lc() != null) {
cellB.setCellValue(record.getR14_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR14_31_asse_inr() != null) {
cellC.setCellValue(record.getR14_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR14_31_liab_lc() != null) {
cellD.setCellValue(record.getR14_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR14_31_liab_inr() != null) {
cellE.setCellValue(record.getR14_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR14_30_asse_lc() != null) {
cellF.setCellValue(record.getR14_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR14_30_asse_inr() != null) {
cellG.setCellValue(record.getR14_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR14_30_liab_lc() != null) {
cellH.setCellValue(record.getR14_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR14_30_liab_inr() != null) {
cellI.setCellValue(record.getR14_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}

//ROW 15

row = sheet.getRow(14);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR15_31_asse_lc() != null) {
cellB.setCellValue(record.getR15_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR15_31_asse_inr() != null) {
cellC.setCellValue(record.getR15_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR15_31_liab_lc() != null) {
cellD.setCellValue(record.getR15_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR15_31_liab_inr() != null) {
cellE.setCellValue(record.getR15_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR15_30_asse_lc() != null) {
cellF.setCellValue(record.getR15_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR15_30_asse_inr() != null) {
cellG.setCellValue(record.getR15_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR15_30_liab_lc() != null) {
cellH.setCellValue(record.getR15_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR15_30_liab_inr() != null) {
cellI.setCellValue(record.getR15_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 16

row = sheet.getRow(15);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR16_31_asse_lc() != null) {
cellB.setCellValue(record.getR16_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR16_31_asse_inr() != null) {
cellC.setCellValue(record.getR16_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR16_31_liab_lc() != null) {
cellD.setCellValue(record.getR16_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR16_31_liab_inr() != null) {
cellE.setCellValue(record.getR16_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR16_30_asse_lc() != null) {
cellF.setCellValue(record.getR16_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR16_30_asse_inr() != null) {
cellG.setCellValue(record.getR16_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR16_30_liab_lc() != null) {
cellH.setCellValue(record.getR16_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR16_30_liab_inr() != null) {
cellI.setCellValue(record.getR16_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}



//ROW 17

row = sheet.getRow(16);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR17_31_asse_lc() != null) {
cellB.setCellValue(record.getR17_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR17_31_asse_inr() != null) {
cellC.setCellValue(record.getR17_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR17_31_liab_lc() != null) {
cellD.setCellValue(record.getR17_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR17_31_liab_inr() != null) {
cellE.setCellValue(record.getR17_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR17_30_asse_lc() != null) {
cellF.setCellValue(record.getR17_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR17_30_asse_inr() != null) {
cellG.setCellValue(record.getR17_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR17_30_liab_lc() != null) {
cellH.setCellValue(record.getR17_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR17_30_liab_inr() != null) {
cellI.setCellValue(record.getR17_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 18

row = sheet.getRow(17);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR18_31_asse_lc() != null) {
cellB.setCellValue(record.getR18_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR18_31_asse_inr() != null) {
cellC.setCellValue(record.getR18_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR18_31_liab_lc() != null) {
cellD.setCellValue(record.getR18_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR18_31_liab_inr() != null) {
cellE.setCellValue(record.getR18_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR18_30_asse_lc() != null) {
cellF.setCellValue(record.getR18_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR18_30_asse_inr() != null) {
cellG.setCellValue(record.getR18_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR18_30_liab_lc() != null) {
cellH.setCellValue(record.getR18_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR18_30_liab_inr() != null) {
cellI.setCellValue(record.getR18_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}



//ROW 19

row = sheet.getRow(18);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR19_31_asse_lc() != null) {
cellB.setCellValue(record.getR19_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR19_31_asse_inr() != null) {
cellC.setCellValue(record.getR19_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR19_31_liab_lc() != null) {
cellD.setCellValue(record.getR19_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR19_31_liab_inr() != null) {
cellE.setCellValue(record.getR19_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR19_30_asse_lc() != null) {
cellF.setCellValue(record.getR19_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR19_30_asse_inr() != null) {
cellG.setCellValue(record.getR19_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR19_30_liab_lc() != null) {
cellH.setCellValue(record.getR19_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR19_30_liab_inr() != null) {
cellI.setCellValue(record.getR19_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 20

row = sheet.getRow(19);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR20_31_asse_lc() != null) {
cellB.setCellValue(record.getR20_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR20_31_asse_inr() != null) {
cellC.setCellValue(record.getR20_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR20_31_liab_lc() != null) {
cellD.setCellValue(record.getR20_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR20_31_liab_inr() != null) {
cellE.setCellValue(record.getR20_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR20_30_asse_lc() != null) {
cellF.setCellValue(record.getR20_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR20_30_asse_inr() != null) {
cellG.setCellValue(record.getR20_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR20_30_liab_lc() != null) {
cellH.setCellValue(record.getR20_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR20_30_liab_inr() != null) {
cellI.setCellValue(record.getR20_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 21

row = sheet.getRow(20);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR21_31_asse_lc() != null) {
cellB.setCellValue(record.getR21_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR21_31_asse_inr() != null) {
cellC.setCellValue(record.getR21_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR21_31_liab_lc() != null) {
cellD.setCellValue(record.getR21_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR21_31_liab_inr() != null) {
cellE.setCellValue(record.getR21_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR21_30_asse_lc() != null) {
cellF.setCellValue(record.getR21_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR21_30_asse_inr() != null) {
cellG.setCellValue(record.getR21_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR21_30_liab_lc() != null) {
cellH.setCellValue(record.getR21_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR21_30_liab_inr() != null) {
cellI.setCellValue(record.getR21_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}



//ROW 22

row = sheet.getRow(21);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR22_31_asse_lc() != null) {
cellB.setCellValue(record.getR22_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR22_31_asse_inr() != null) {
cellC.setCellValue(record.getR22_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR22_31_liab_lc() != null) {
cellD.setCellValue(record.getR22_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR22_31_liab_inr() != null) {
cellE.setCellValue(record.getR22_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR22_30_asse_lc() != null) {
cellF.setCellValue(record.getR22_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR22_30_asse_inr() != null) {
cellG.setCellValue(record.getR22_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR22_30_liab_lc() != null) {
cellH.setCellValue(record.getR22_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR22_30_liab_inr() != null) {
cellI.setCellValue(record.getR22_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 23

row = sheet.getRow(22);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR23_31_asse_lc() != null) {
cellB.setCellValue(record.getR23_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR23_31_asse_inr() != null) {
cellC.setCellValue(record.getR23_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR23_31_liab_lc() != null) {
cellD.setCellValue(record.getR23_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR23_31_liab_inr() != null) {
cellE.setCellValue(record.getR23_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR23_30_asse_lc() != null) {
cellF.setCellValue(record.getR23_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR23_30_asse_inr() != null) {
cellG.setCellValue(record.getR23_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR23_30_liab_lc() != null) {
cellH.setCellValue(record.getR23_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR23_30_liab_inr() != null) {
cellI.setCellValue(record.getR23_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 24

row = sheet.getRow(23);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR24_31_asse_lc() != null) {
cellB.setCellValue(record.getR24_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR24_31_asse_inr() != null) {
cellC.setCellValue(record.getR24_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR24_31_liab_lc() != null) {
cellD.setCellValue(record.getR24_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR24_31_liab_inr() != null) {
cellE.setCellValue(record.getR24_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR24_30_asse_lc() != null) {
cellF.setCellValue(record.getR24_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR24_30_asse_inr() != null) {
cellG.setCellValue(record.getR24_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR24_30_liab_lc() != null) {
cellH.setCellValue(record.getR24_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR24_30_liab_inr() != null) {
cellI.setCellValue(record.getR24_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 25

row = sheet.getRow(24);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR25_31_asse_lc() != null) {
cellB.setCellValue(record.getR25_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR25_31_asse_inr() != null) {
cellC.setCellValue(record.getR25_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR25_31_liab_lc() != null) {
cellD.setCellValue(record.getR25_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR25_31_liab_inr() != null) {
cellE.setCellValue(record.getR25_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR25_30_asse_lc() != null) {
cellF.setCellValue(record.getR25_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR25_30_asse_inr() != null) {
cellG.setCellValue(record.getR25_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR25_30_liab_lc() != null) {
cellH.setCellValue(record.getR25_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR25_30_liab_inr() != null) {
cellI.setCellValue(record.getR25_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}
					
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



				public byte[] getExcelDTAXARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {

			}

			List<DEFERRED_TAX_Archival_Summary_Entity> dataList = 
					getDataByDateListArchival(dateformat.parse(todate), version);
		
	    

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for DTAX new report. Returning empty result.");
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

				int startRow = 10;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						DEFERRED_TAX_Archival_Summary_Entity record = dataList.get(i);
					    
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

		Cell cellB , cellC ,cellD , cellE , cellF , cellG , cellH, cellI;
					//ROW 11
					                    //COLUMN B 
										  
					                    cellB = row.getCell(1);
										if (cellB == null) cellB = row.createCell(1);
										if (record.getR11_31_asse_lc() != null) {
										    cellB.setCellValue(record.getR11_31_asse_lc().doubleValue());
										} else {
										    cellB.setCellValue(0);
										}

					                    
					                  //COLUMN C 

										cellC = row.getCell(2);
										if (cellC == null) cellC = row.createCell(2);
										if (record.getR11_31_asse_inr() != null) {
										    cellC.setCellValue(record.getR11_31_asse_inr().doubleValue());
										} else {
										    cellC.setCellValue(0);
										}
										
										
					                  //COLUMN D 
										
										
										cellD = row.getCell(3);
										if (cellD == null) cellD= row.createCell(3);
										if (record.getR11_31_liab_lc() != null) {
										    cellD.setCellValue(record.getR11_31_liab_lc().doubleValue());
										} else {
										    cellD.setCellValue(0);
										}
										
										
					                  //COLUMN E
										
										cellE = row.getCell(4);
										if (cellE == null) cellE = row.createCell(4);
										if (record.getR11_31_liab_inr() != null) {
										    cellE.setCellValue(record.getR11_31_liab_inr().doubleValue());
										} else {
										    cellE.setCellValue(0);
										}
										
										
					                  //COLUMN F 
										
										cellF = row.getCell(5);
										if (cellF == null) cellF = row.createCell(5);
										if (record.getR11_30_asse_lc() != null) {
										    cellF.setCellValue(record.getR11_30_asse_lc().doubleValue());
										} else {
										    cellF.setCellValue(0);
										}
										
										
					                  //COLUMN G 
										
										
										cellG = row.getCell(6);
										if (cellG == null) cellG = row.createCell(6);
										if (record.getR11_30_asse_inr() != null) {
										    cellG.setCellValue(record.getR11_30_asse_inr().doubleValue());
										} else {
										    cellG.setCellValue(0);
										}
										
										
										
					                  //COLUMN H
										
										cellH = row.getCell(7);
										if (cellH == null) cellH = row.createCell(7);
										if (record.getR11_30_liab_lc() != null) {
										    cellH.setCellValue(record.getR11_30_liab_lc().doubleValue());
										} else {
										    cellH.setCellValue(0);
										}
										
										
					                  //COLUMN I 
										
										cellI = row.getCell(8);
										if (cellI == null) cellI = row.createCell(8);
										if (record.getR11_30_liab_inr() != null) {
										    cellI.setCellValue(record.getR11_30_liab_inr().doubleValue());
										} else {
										    cellI.setCellValue(0);
										}
										
										
										// ROW 12

					row = sheet.getRow(11);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR12_31_asse_lc() != null) {
					cellB.setCellValue(record.getR12_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR12_31_asse_inr() != null) {
					cellC.setCellValue(record.getR12_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR12_31_liab_lc() != null) {
					cellD.setCellValue(record.getR12_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR12_31_liab_inr() != null) {
					cellE.setCellValue(record.getR12_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR12_30_asse_lc() != null) {
					cellF.setCellValue(record.getR12_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR12_30_asse_inr() != null) {
					cellG.setCellValue(record.getR12_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR12_30_liab_lc() != null) {
					cellH.setCellValue(record.getR12_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR12_30_liab_inr() != null) {
					cellI.setCellValue(record.getR12_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 13

					row = sheet.getRow(12);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR13_31_asse_lc() != null) {
					cellB.setCellValue(record.getR13_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR13_31_asse_inr() != null) {
					cellC.setCellValue(record.getR13_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR13_31_liab_lc() != null) {
					cellD.setCellValue(record.getR13_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR13_31_liab_inr() != null) {
					cellE.setCellValue(record.getR13_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR13_30_asse_lc() != null) {
					cellF.setCellValue(record.getR13_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR13_30_asse_inr() != null) {
					cellG.setCellValue(record.getR13_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR13_30_liab_lc() != null) {
					cellH.setCellValue(record.getR13_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR13_30_liab_inr() != null) {
					cellI.setCellValue(record.getR13_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 14

					row = sheet.getRow(13);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR14_31_asse_lc() != null) {
					cellB.setCellValue(record.getR14_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR14_31_asse_inr() != null) {
					cellC.setCellValue(record.getR14_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR14_31_liab_lc() != null) {
					cellD.setCellValue(record.getR14_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR14_31_liab_inr() != null) {
					cellE.setCellValue(record.getR14_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR14_30_asse_lc() != null) {
					cellF.setCellValue(record.getR14_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR14_30_asse_inr() != null) {
					cellG.setCellValue(record.getR14_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR14_30_liab_lc() != null) {
					cellH.setCellValue(record.getR14_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR14_30_liab_inr() != null) {
					cellI.setCellValue(record.getR14_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}

					//ROW 15

					row = sheet.getRow(14);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR15_31_asse_lc() != null) {
					cellB.setCellValue(record.getR15_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR15_31_asse_inr() != null) {
					cellC.setCellValue(record.getR15_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR15_31_liab_lc() != null) {
					cellD.setCellValue(record.getR15_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR15_31_liab_inr() != null) {
					cellE.setCellValue(record.getR15_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR15_30_asse_lc() != null) {
					cellF.setCellValue(record.getR15_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR15_30_asse_inr() != null) {
					cellG.setCellValue(record.getR15_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR15_30_liab_lc() != null) {
					cellH.setCellValue(record.getR15_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR15_30_liab_inr() != null) {
					cellI.setCellValue(record.getR15_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 16

					row = sheet.getRow(15);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR16_31_asse_lc() != null) {
					cellB.setCellValue(record.getR16_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR16_31_asse_inr() != null) {
					cellC.setCellValue(record.getR16_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR16_31_liab_lc() != null) {
					cellD.setCellValue(record.getR16_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR16_31_liab_inr() != null) {
					cellE.setCellValue(record.getR16_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR16_30_asse_lc() != null) {
					cellF.setCellValue(record.getR16_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR16_30_asse_inr() != null) {
					cellG.setCellValue(record.getR16_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR16_30_liab_lc() != null) {
					cellH.setCellValue(record.getR16_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR16_30_liab_inr() != null) {
					cellI.setCellValue(record.getR16_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}



					//ROW 17

					row = sheet.getRow(16);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR17_31_asse_lc() != null) {
					cellB.setCellValue(record.getR17_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR17_31_asse_inr() != null) {
					cellC.setCellValue(record.getR17_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR17_31_liab_lc() != null) {
					cellD.setCellValue(record.getR17_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR17_31_liab_inr() != null) {
					cellE.setCellValue(record.getR17_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR17_30_asse_lc() != null) {
					cellF.setCellValue(record.getR17_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR17_30_asse_inr() != null) {
					cellG.setCellValue(record.getR17_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR17_30_liab_lc() != null) {
					cellH.setCellValue(record.getR17_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR17_30_liab_inr() != null) {
					cellI.setCellValue(record.getR17_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 18

					row = sheet.getRow(17);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR18_31_asse_lc() != null) {
					cellB.setCellValue(record.getR18_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR18_31_asse_inr() != null) {
					cellC.setCellValue(record.getR18_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR18_31_liab_lc() != null) {
					cellD.setCellValue(record.getR18_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR18_31_liab_inr() != null) {
					cellE.setCellValue(record.getR18_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR18_30_asse_lc() != null) {
					cellF.setCellValue(record.getR18_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR18_30_asse_inr() != null) {
					cellG.setCellValue(record.getR18_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR18_30_liab_lc() != null) {
					cellH.setCellValue(record.getR18_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR18_30_liab_inr() != null) {
					cellI.setCellValue(record.getR18_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}



					//ROW 19

					row = sheet.getRow(18);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR19_31_asse_lc() != null) {
					cellB.setCellValue(record.getR19_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR19_31_asse_inr() != null) {
					cellC.setCellValue(record.getR19_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR19_31_liab_lc() != null) {
					cellD.setCellValue(record.getR19_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR19_31_liab_inr() != null) {
					cellE.setCellValue(record.getR19_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR19_30_asse_lc() != null) {
					cellF.setCellValue(record.getR19_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR19_30_asse_inr() != null) {
					cellG.setCellValue(record.getR19_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR19_30_liab_lc() != null) {
					cellH.setCellValue(record.getR19_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR19_30_liab_inr() != null) {
					cellI.setCellValue(record.getR19_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 20

					row = sheet.getRow(19);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR20_31_asse_lc() != null) {
					cellB.setCellValue(record.getR20_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR20_31_asse_inr() != null) {
					cellC.setCellValue(record.getR20_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR20_31_liab_lc() != null) {
					cellD.setCellValue(record.getR20_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR20_31_liab_inr() != null) {
					cellE.setCellValue(record.getR20_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR20_30_asse_lc() != null) {
					cellF.setCellValue(record.getR20_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR20_30_asse_inr() != null) {
					cellG.setCellValue(record.getR20_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR20_30_liab_lc() != null) {
					cellH.setCellValue(record.getR20_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR20_30_liab_inr() != null) {
					cellI.setCellValue(record.getR20_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 21

					row = sheet.getRow(20);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR21_31_asse_lc() != null) {
					cellB.setCellValue(record.getR21_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR21_31_asse_inr() != null) {
					cellC.setCellValue(record.getR21_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR21_31_liab_lc() != null) {
					cellD.setCellValue(record.getR21_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR21_31_liab_inr() != null) {
					cellE.setCellValue(record.getR21_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR21_30_asse_lc() != null) {
					cellF.setCellValue(record.getR21_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR21_30_asse_inr() != null) {
					cellG.setCellValue(record.getR21_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR21_30_liab_lc() != null) {
					cellH.setCellValue(record.getR21_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR21_30_liab_inr() != null) {
					cellI.setCellValue(record.getR21_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}



					//ROW 22

					row = sheet.getRow(21);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR22_31_asse_lc() != null) {
					cellB.setCellValue(record.getR22_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR22_31_asse_inr() != null) {
					cellC.setCellValue(record.getR22_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR22_31_liab_lc() != null) {
					cellD.setCellValue(record.getR22_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR22_31_liab_inr() != null) {
					cellE.setCellValue(record.getR22_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR22_30_asse_lc() != null) {
					cellF.setCellValue(record.getR22_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR22_30_asse_inr() != null) {
					cellG.setCellValue(record.getR22_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR22_30_liab_lc() != null) {
					cellH.setCellValue(record.getR22_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR22_30_liab_inr() != null) {
					cellI.setCellValue(record.getR22_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 23

					row = sheet.getRow(22);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR23_31_asse_lc() != null) {
					cellB.setCellValue(record.getR23_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR23_31_asse_inr() != null) {
					cellC.setCellValue(record.getR23_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR23_31_liab_lc() != null) {
					cellD.setCellValue(record.getR23_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR23_31_liab_inr() != null) {
					cellE.setCellValue(record.getR23_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR23_30_asse_lc() != null) {
					cellF.setCellValue(record.getR23_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR23_30_asse_inr() != null) {
					cellG.setCellValue(record.getR23_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR23_30_liab_lc() != null) {
					cellH.setCellValue(record.getR23_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR23_30_liab_inr() != null) {
					cellI.setCellValue(record.getR23_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 24

					row = sheet.getRow(23);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR24_31_asse_lc() != null) {
					cellB.setCellValue(record.getR24_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR24_31_asse_inr() != null) {
					cellC.setCellValue(record.getR24_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR24_31_liab_lc() != null) {
					cellD.setCellValue(record.getR24_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR24_31_liab_inr() != null) {
					cellE.setCellValue(record.getR24_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR24_30_asse_lc() != null) {
					cellF.setCellValue(record.getR24_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR24_30_asse_inr() != null) {
					cellG.setCellValue(record.getR24_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR24_30_liab_lc() != null) {
					cellH.setCellValue(record.getR24_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR24_30_liab_inr() != null) {
					cellI.setCellValue(record.getR24_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 25

					row = sheet.getRow(24);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR25_31_asse_lc() != null) {
					cellB.setCellValue(record.getR25_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR25_31_asse_inr() != null) {
					cellC.setCellValue(record.getR25_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR25_31_liab_lc() != null) {
					cellD.setCellValue(record.getR25_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR25_31_liab_inr() != null) {
					cellE.setCellValue(record.getR25_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR25_30_asse_lc() != null) {
					cellF.setCellValue(record.getR25_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR25_30_asse_inr() != null) {
					cellG.setCellValue(record.getR25_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR25_30_liab_lc() != null) {
					cellH.setCellValue(record.getR25_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR25_30_liab_inr() != null) {
					cellI.setCellValue(record.getR25_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


						
						
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