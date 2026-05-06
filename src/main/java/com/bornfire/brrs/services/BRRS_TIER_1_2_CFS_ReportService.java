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

public class BRRS_TIER_1_2_CFS_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_TIER_1_2_CFS_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

  
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;
	
	
	
	// Fetch data by report date - TIER 1 & 2 CFS Summary
public List<TIER_1_2_CFS_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_TIER_1_2_CFS_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new TIER_1_2_CFS_RowMapper()   // make sure this RowMapper exists
    );
}


// ARCHIVAL 

public List<Object[]> getTIER_1_2_CFS_Archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_TIER_1_2_CFS_ARCHIVALTABLE_SUMMARY ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<TIER_1_2_CFS_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_TIER_1_2_CFS_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new TIER_1_2_CFS_Archival_RowMapper()
    );
}


public List<TIER_1_2_CFS_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_TIER_1_2_CFS_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new TIER_1_2_CFS_Archival_RowMapper()
    );
}


// DETAIL


public List<TIER_1_2_CFS_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_TIER_1_2_CFS_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new TIER_1_2_CFS_Detail_RowMapper()
    );
}

public List<TIER_1_2_CFS_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_TIER_1_2_CFS_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, offset, limit},
            new TIER_1_2_CFS_Detail_RowMapper()
    );
}

public int getDataCount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_TIER_1_2_CFS_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            Integer.class
    );
}

public List<TIER_1_2_CFS_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate) {

    String sql = "SELECT * FROM BRRS_TIER_1_2_CFS_DETAILTABLE " +
                 "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate},
            new TIER_1_2_CFS_Detail_RowMapper()
    );
}

public TIER_1_2_CFS_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_TIER_1_2_CFS_DETAILTABLE WHERE ACCT_NUMBER = ?";

    List<TIER_1_2_CFS_Detail_Entity> list = jdbcTemplate.query(
            sql,
            new Object[]{acctNumber},
            new TIER_1_2_CFS_Detail_RowMapper()
    );

    return list.isEmpty() ? null : list.get(0);
}

// ARCHIVAL DETAIL 

public List<TIER_1_2_CFS_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_TIER_1_2_CFS_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, dataEntryVersion},
            new TIER_1_2_CFS_Archival_Detail_RowMapper()
    );
}

public List<TIER_1_2_CFS_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_TIER_1_2_CFS_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_LABEL = ? " +
                 "AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ? " +
                 "AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate, dataEntryVersion},
            new TIER_1_2_CFS_Archival_Detail_RowMapper()
    );
}

// SUMAMRY ENTITY CLASS


public class TIER_1_2_CFS_RowMapper
        implements RowMapper<TIER_1_2_CFS_Summary_Entity> {

    @Override
    public TIER_1_2_CFS_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        TIER_1_2_CFS_Summary_Entity obj = new TIER_1_2_CFS_Summary_Entity();

        // =========================
        // COMMON FIELDS
        // =========================
        obj.setReport_date(rs.getDate("REPORT_DATE"));
        obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
        obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
        obj.setReport_code(rs.getString("REPORT_CODE"));
        obj.setReport_desc(rs.getString("REPORT_DESC"));
        obj.setEntity_flg(rs.getString("ENTITY_FLG"));
        obj.setModify_flg(rs.getString("MODIFY_FLG"));
        obj.setDel_flg(rs.getString("DEL_FLG"));

        // =========================
        // R10 → R20
        // =========================
        obj.setR10_VALUE_IN_I_COLUMN(rs.getBigDecimal("R10_VALUE_IN_I_COLUMN"));
        obj.setR10_BWP_AMOUNT(rs.getBigDecimal("R10_BWP_AMOUNT"));

        obj.setR11_VALUE_IN_I_COLUMN(rs.getBigDecimal("R11_VALUE_IN_I_COLUMN"));
        obj.setR11_BWP_AMOUNT(rs.getBigDecimal("R11_BWP_AMOUNT"));

        obj.setR12_VALUE_IN_I_COLUMN(rs.getBigDecimal("R12_VALUE_IN_I_COLUMN"));
        obj.setR12_BWP_AMOUNT(rs.getBigDecimal("R12_BWP_AMOUNT"));

        obj.setR13_VALUE_IN_I_COLUMN(rs.getBigDecimal("R13_VALUE_IN_I_COLUMN"));
        obj.setR13_BWP_AMOUNT(rs.getBigDecimal("R13_BWP_AMOUNT"));

        obj.setR14_VALUE_IN_I_COLUMN(rs.getBigDecimal("R14_VALUE_IN_I_COLUMN"));
        obj.setR14_BWP_AMOUNT(rs.getBigDecimal("R14_BWP_AMOUNT"));

        obj.setR15_VALUE_IN_I_COLUMN(rs.getBigDecimal("R15_VALUE_IN_I_COLUMN"));
        obj.setR15_BWP_AMOUNT(rs.getBigDecimal("R15_BWP_AMOUNT"));

        obj.setR16_VALUE_IN_I_COLUMN(rs.getBigDecimal("R16_VALUE_IN_I_COLUMN"));
        obj.setR16_BWP_AMOUNT(rs.getBigDecimal("R16_BWP_AMOUNT"));

        obj.setR17_VALUE_IN_I_COLUMN(rs.getBigDecimal("R17_VALUE_IN_I_COLUMN"));
        obj.setR17_BWP_AMOUNT(rs.getBigDecimal("R17_BWP_AMOUNT"));

        obj.setR18_VALUE_IN_I_COLUMN(rs.getBigDecimal("R18_VALUE_IN_I_COLUMN"));
        obj.setR18_BWP_AMOUNT(rs.getBigDecimal("R18_BWP_AMOUNT"));

        obj.setR19_VALUE_IN_I_COLUMN(rs.getBigDecimal("R19_VALUE_IN_I_COLUMN"));
        obj.setR19_BWP_AMOUNT(rs.getBigDecimal("R19_BWP_AMOUNT"));

        obj.setR20_VALUE_IN_I_COLUMN(rs.getBigDecimal("R20_VALUE_IN_I_COLUMN"));
        obj.setR20_BWP_AMOUNT(rs.getBigDecimal("R20_BWP_AMOUNT"));

        // =========================
        // R21 → R30
        // =========================
        obj.setR21_VALUE_IN_I_COLUMN(rs.getBigDecimal("R21_VALUE_IN_I_COLUMN"));
        obj.setR21_BWP_AMOUNT(rs.getBigDecimal("R21_BWP_AMOUNT"));

        obj.setR22_VALUE_IN_I_COLUMN(rs.getBigDecimal("R22_VALUE_IN_I_COLUMN"));
        obj.setR22_BWP_AMOUNT(rs.getBigDecimal("R22_BWP_AMOUNT"));

        obj.setR23_VALUE_IN_I_COLUMN(rs.getBigDecimal("R23_VALUE_IN_I_COLUMN"));
        obj.setR23_BWP_AMOUNT(rs.getBigDecimal("R23_BWP_AMOUNT"));

        obj.setR24_VALUE_IN_I_COLUMN(rs.getBigDecimal("R24_VALUE_IN_I_COLUMN"));
        obj.setR24_BWP_AMOUNT(rs.getBigDecimal("R24_BWP_AMOUNT"));

        obj.setR25_VALUE_IN_I_COLUMN(rs.getBigDecimal("R25_VALUE_IN_I_COLUMN"));
        obj.setR25_BWP_AMOUNT(rs.getBigDecimal("R25_BWP_AMOUNT"));

        obj.setR26_VALUE_IN_I_COLUMN(rs.getBigDecimal("R26_VALUE_IN_I_COLUMN"));
        obj.setR26_BWP_AMOUNT(rs.getBigDecimal("R26_BWP_AMOUNT"));

        obj.setR27_VALUE_IN_I_COLUMN(rs.getBigDecimal("R27_VALUE_IN_I_COLUMN"));
        obj.setR27_BWP_AMOUNT(rs.getBigDecimal("R27_BWP_AMOUNT"));

        obj.setR28_VALUE_IN_I_COLUMN(rs.getBigDecimal("R28_VALUE_IN_I_COLUMN"));
        obj.setR28_BWP_AMOUNT(rs.getBigDecimal("R28_BWP_AMOUNT"));

        obj.setR29_VALUE_IN_I_COLUMN(rs.getBigDecimal("R29_VALUE_IN_I_COLUMN"));
        obj.setR29_BWP_AMOUNT(rs.getBigDecimal("R29_BWP_AMOUNT"));

        obj.setR30_VALUE_IN_I_COLUMN(rs.getBigDecimal("R30_VALUE_IN_I_COLUMN"));
        obj.setR30_BWP_AMOUNT(rs.getBigDecimal("R30_BWP_AMOUNT"));

        // =========================
        // R31 → R37
        // =========================
        obj.setR31_VALUE_IN_I_COLUMN(rs.getBigDecimal("R31_VALUE_IN_I_COLUMN"));
        obj.setR31_BWP_AMOUNT(rs.getBigDecimal("R31_BWP_AMOUNT"));

        obj.setR32_VALUE_IN_I_COLUMN(rs.getBigDecimal("R32_VALUE_IN_I_COLUMN"));
        obj.setR32_BWP_AMOUNT(rs.getBigDecimal("R32_BWP_AMOUNT"));

        obj.setR33_VALUE_IN_I_COLUMN(rs.getBigDecimal("R33_VALUE_IN_I_COLUMN"));
        obj.setR33_BWP_AMOUNT(rs.getBigDecimal("R33_BWP_AMOUNT"));

        obj.setR34_VALUE_IN_I_COLUMN(rs.getBigDecimal("R34_VALUE_IN_I_COLUMN"));
        obj.setR34_BWP_AMOUNT(rs.getBigDecimal("R34_BWP_AMOUNT"));

        obj.setR35_VALUE_IN_I_COLUMN(rs.getBigDecimal("R35_VALUE_IN_I_COLUMN"));
        obj.setR35_BWP_AMOUNT(rs.getBigDecimal("R35_BWP_AMOUNT"));

        obj.setR36_VALUE_IN_I_COLUMN(rs.getBigDecimal("R36_VALUE_IN_I_COLUMN"));
        obj.setR36_BWP_AMOUNT(rs.getBigDecimal("R36_BWP_AMOUNT"));

        obj.setR37_VALUE_IN_I_COLUMN(rs.getBigDecimal("R37_VALUE_IN_I_COLUMN"));
        obj.setR37_BWP_AMOUNT(rs.getBigDecimal("R37_BWP_AMOUNT"));

        // =========================
        // R41 → R48
        // =========================
        obj.setR41_RISK_ASSETS(rs.getBigDecimal("R41_RISK_ASSETS"));
        obj.setR42_RISK_ASSETS(rs.getBigDecimal("R42_RISK_ASSETS"));
        obj.setR43_RISK_ASSETS(rs.getBigDecimal("R43_RISK_ASSETS"));
        obj.setR44_RISK_ASSETS(rs.getBigDecimal("R44_RISK_ASSETS"));
        obj.setR46_PERCENTAGE_CF_TO_RWA(rs.getBigDecimal("R46_PERCENTAGE_CF_TO_RWA"));
        obj.setR47_PERCENTAGE_TIER1(rs.getBigDecimal("R47_PERCENTAGE_TIER1"));
        obj.setR48_PERCENTAGE_TIER2(rs.getBigDecimal("R48_PERCENTAGE_TIER2"));

        // =========================
        // R52 → R54
        // =========================
        obj.setR52_DOMESTIC_RISK_ASSETS(rs.getBigDecimal("R52_DOMESTIC_RISK_ASSETS"));
        obj.setR52_FOREIGN_RISK_ASSETS(rs.getBigDecimal("R52_FOREIGN_RISK_ASSETS"));
        obj.setR52_TOTAL_RISK_ASSETS(rs.getBigDecimal("R52_TOTAL_RISK_ASSETS"));

        obj.setR53_DOMESTIC_RISK_ASSETS(rs.getBigDecimal("R53_DOMESTIC_RISK_ASSETS"));
        obj.setR53_FOREIGN_RISK_ASSETS(rs.getBigDecimal("R53_FOREIGN_RISK_ASSETS"));
        obj.setR53_TOTAL_RISK_ASSETS(rs.getBigDecimal("R53_TOTAL_RISK_ASSETS"));

        obj.setR54_DOMESTIC_RISK_ASSETS(rs.getBigDecimal("R54_DOMESTIC_RISK_ASSETS"));
        obj.setR54_FOREIGN_RISK_ASSETS(rs.getBigDecimal("R54_FOREIGN_RISK_ASSETS"));
        obj.setR54_TOTAL_RISK_ASSETS(rs.getBigDecimal("R54_TOTAL_RISK_ASSETS"));

        return obj;
    }
}


public class TIER_1_2_CFS_Summary_Entity {


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

private BigDecimal R10_VALUE_IN_I_COLUMN;
private BigDecimal R10_BWP_AMOUNT;
private BigDecimal R11_VALUE_IN_I_COLUMN;
private BigDecimal R11_BWP_AMOUNT;
private BigDecimal R12_VALUE_IN_I_COLUMN;
private BigDecimal R12_BWP_AMOUNT;
private BigDecimal R13_VALUE_IN_I_COLUMN;
private BigDecimal R13_BWP_AMOUNT;
private BigDecimal R14_VALUE_IN_I_COLUMN;
private BigDecimal R14_BWP_AMOUNT;
private BigDecimal R15_VALUE_IN_I_COLUMN;
private BigDecimal R15_BWP_AMOUNT;
private BigDecimal R16_VALUE_IN_I_COLUMN;
private BigDecimal R16_BWP_AMOUNT;
private BigDecimal R17_VALUE_IN_I_COLUMN;
private BigDecimal R17_BWP_AMOUNT;
private BigDecimal R18_VALUE_IN_I_COLUMN;
private BigDecimal R18_BWP_AMOUNT;
private BigDecimal R19_VALUE_IN_I_COLUMN;
private BigDecimal R19_BWP_AMOUNT;
private BigDecimal R20_VALUE_IN_I_COLUMN;
private BigDecimal R20_BWP_AMOUNT;
private BigDecimal R21_VALUE_IN_I_COLUMN;
private BigDecimal R21_BWP_AMOUNT;
private BigDecimal R22_VALUE_IN_I_COLUMN;
private BigDecimal R22_BWP_AMOUNT;
private BigDecimal R23_VALUE_IN_I_COLUMN;
private BigDecimal R23_BWP_AMOUNT;
private BigDecimal R24_VALUE_IN_I_COLUMN;
private BigDecimal R24_BWP_AMOUNT;
private BigDecimal R25_VALUE_IN_I_COLUMN;
private BigDecimal R25_BWP_AMOUNT;
private BigDecimal R26_VALUE_IN_I_COLUMN;
private BigDecimal R26_BWP_AMOUNT;
private BigDecimal R27_VALUE_IN_I_COLUMN;
private BigDecimal R27_BWP_AMOUNT;
private BigDecimal R28_VALUE_IN_I_COLUMN;
private BigDecimal R28_BWP_AMOUNT;
private BigDecimal R29_VALUE_IN_I_COLUMN;
private BigDecimal R29_BWP_AMOUNT;
private BigDecimal R30_VALUE_IN_I_COLUMN;
private BigDecimal R30_BWP_AMOUNT;
private BigDecimal R31_VALUE_IN_I_COLUMN;
private BigDecimal R31_BWP_AMOUNT;
private BigDecimal R32_VALUE_IN_I_COLUMN;
private BigDecimal R32_BWP_AMOUNT;
private BigDecimal R33_VALUE_IN_I_COLUMN;
private BigDecimal R33_BWP_AMOUNT;
private BigDecimal R34_VALUE_IN_I_COLUMN;
private BigDecimal R34_BWP_AMOUNT;
private BigDecimal R35_VALUE_IN_I_COLUMN;
private BigDecimal R35_BWP_AMOUNT;
private BigDecimal R36_VALUE_IN_I_COLUMN;
private BigDecimal R36_BWP_AMOUNT;
private BigDecimal R37_VALUE_IN_I_COLUMN;
private BigDecimal R37_BWP_AMOUNT;
private BigDecimal R41_RISK_ASSETS;
private BigDecimal R42_RISK_ASSETS;
private BigDecimal R43_RISK_ASSETS;
private BigDecimal R44_RISK_ASSETS;
private BigDecimal R46_PERCENTAGE_CF_TO_RWA;
private BigDecimal R47_PERCENTAGE_TIER1;
private BigDecimal R48_PERCENTAGE_TIER2;
private BigDecimal R52_DOMESTIC_RISK_ASSETS;
private BigDecimal R52_FOREIGN_RISK_ASSETS;
private BigDecimal R52_TOTAL_RISK_ASSETS;
private BigDecimal R53_DOMESTIC_RISK_ASSETS;
private BigDecimal R53_FOREIGN_RISK_ASSETS;
private BigDecimal R53_TOTAL_RISK_ASSETS;
private BigDecimal R54_DOMESTIC_RISK_ASSETS;
private BigDecimal R54_FOREIGN_RISK_ASSETS;
private BigDecimal R54_TOTAL_RISK_ASSETS;



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



public BigDecimal getR10_VALUE_IN_I_COLUMN() {
	return R10_VALUE_IN_I_COLUMN;
}



public void setR10_VALUE_IN_I_COLUMN(BigDecimal r10_VALUE_IN_I_COLUMN) {
	R10_VALUE_IN_I_COLUMN = r10_VALUE_IN_I_COLUMN;
}



public BigDecimal getR10_BWP_AMOUNT() {
	return R10_BWP_AMOUNT;
}



public void setR10_BWP_AMOUNT(BigDecimal r10_BWP_AMOUNT) {
	R10_BWP_AMOUNT = r10_BWP_AMOUNT;
}



public BigDecimal getR11_VALUE_IN_I_COLUMN() {
	return R11_VALUE_IN_I_COLUMN;
}



public void setR11_VALUE_IN_I_COLUMN(BigDecimal r11_VALUE_IN_I_COLUMN) {
	R11_VALUE_IN_I_COLUMN = r11_VALUE_IN_I_COLUMN;
}



public BigDecimal getR11_BWP_AMOUNT() {
	return R11_BWP_AMOUNT;
}



public void setR11_BWP_AMOUNT(BigDecimal r11_BWP_AMOUNT) {
	R11_BWP_AMOUNT = r11_BWP_AMOUNT;
}



public BigDecimal getR12_VALUE_IN_I_COLUMN() {
	return R12_VALUE_IN_I_COLUMN;
}



public void setR12_VALUE_IN_I_COLUMN(BigDecimal r12_VALUE_IN_I_COLUMN) {
	R12_VALUE_IN_I_COLUMN = r12_VALUE_IN_I_COLUMN;
}



public BigDecimal getR12_BWP_AMOUNT() {
	return R12_BWP_AMOUNT;
}



public void setR12_BWP_AMOUNT(BigDecimal r12_BWP_AMOUNT) {
	R12_BWP_AMOUNT = r12_BWP_AMOUNT;
}



public BigDecimal getR13_VALUE_IN_I_COLUMN() {
	return R13_VALUE_IN_I_COLUMN;
}



public void setR13_VALUE_IN_I_COLUMN(BigDecimal r13_VALUE_IN_I_COLUMN) {
	R13_VALUE_IN_I_COLUMN = r13_VALUE_IN_I_COLUMN;
}



public BigDecimal getR13_BWP_AMOUNT() {
	return R13_BWP_AMOUNT;
}



public void setR13_BWP_AMOUNT(BigDecimal r13_BWP_AMOUNT) {
	R13_BWP_AMOUNT = r13_BWP_AMOUNT;
}



public BigDecimal getR14_VALUE_IN_I_COLUMN() {
	return R14_VALUE_IN_I_COLUMN;
}



public void setR14_VALUE_IN_I_COLUMN(BigDecimal r14_VALUE_IN_I_COLUMN) {
	R14_VALUE_IN_I_COLUMN = r14_VALUE_IN_I_COLUMN;
}



public BigDecimal getR14_BWP_AMOUNT() {
	return R14_BWP_AMOUNT;
}



public void setR14_BWP_AMOUNT(BigDecimal r14_BWP_AMOUNT) {
	R14_BWP_AMOUNT = r14_BWP_AMOUNT;
}



public BigDecimal getR15_VALUE_IN_I_COLUMN() {
	return R15_VALUE_IN_I_COLUMN;
}



public void setR15_VALUE_IN_I_COLUMN(BigDecimal r15_VALUE_IN_I_COLUMN) {
	R15_VALUE_IN_I_COLUMN = r15_VALUE_IN_I_COLUMN;
}



public BigDecimal getR15_BWP_AMOUNT() {
	return R15_BWP_AMOUNT;
}



public void setR15_BWP_AMOUNT(BigDecimal r15_BWP_AMOUNT) {
	R15_BWP_AMOUNT = r15_BWP_AMOUNT;
}



public BigDecimal getR16_VALUE_IN_I_COLUMN() {
	return R16_VALUE_IN_I_COLUMN;
}



public void setR16_VALUE_IN_I_COLUMN(BigDecimal r16_VALUE_IN_I_COLUMN) {
	R16_VALUE_IN_I_COLUMN = r16_VALUE_IN_I_COLUMN;
}



public BigDecimal getR16_BWP_AMOUNT() {
	return R16_BWP_AMOUNT;
}



public void setR16_BWP_AMOUNT(BigDecimal r16_BWP_AMOUNT) {
	R16_BWP_AMOUNT = r16_BWP_AMOUNT;
}



public BigDecimal getR17_VALUE_IN_I_COLUMN() {
	return R17_VALUE_IN_I_COLUMN;
}



public void setR17_VALUE_IN_I_COLUMN(BigDecimal r17_VALUE_IN_I_COLUMN) {
	R17_VALUE_IN_I_COLUMN = r17_VALUE_IN_I_COLUMN;
}



public BigDecimal getR17_BWP_AMOUNT() {
	return R17_BWP_AMOUNT;
}



public void setR17_BWP_AMOUNT(BigDecimal r17_BWP_AMOUNT) {
	R17_BWP_AMOUNT = r17_BWP_AMOUNT;
}



public BigDecimal getR18_VALUE_IN_I_COLUMN() {
	return R18_VALUE_IN_I_COLUMN;
}



public void setR18_VALUE_IN_I_COLUMN(BigDecimal r18_VALUE_IN_I_COLUMN) {
	R18_VALUE_IN_I_COLUMN = r18_VALUE_IN_I_COLUMN;
}



public BigDecimal getR18_BWP_AMOUNT() {
	return R18_BWP_AMOUNT;
}



public void setR18_BWP_AMOUNT(BigDecimal r18_BWP_AMOUNT) {
	R18_BWP_AMOUNT = r18_BWP_AMOUNT;
}



public BigDecimal getR19_VALUE_IN_I_COLUMN() {
	return R19_VALUE_IN_I_COLUMN;
}



public void setR19_VALUE_IN_I_COLUMN(BigDecimal r19_VALUE_IN_I_COLUMN) {
	R19_VALUE_IN_I_COLUMN = r19_VALUE_IN_I_COLUMN;
}



public BigDecimal getR19_BWP_AMOUNT() {
	return R19_BWP_AMOUNT;
}



public void setR19_BWP_AMOUNT(BigDecimal r19_BWP_AMOUNT) {
	R19_BWP_AMOUNT = r19_BWP_AMOUNT;
}



public BigDecimal getR20_VALUE_IN_I_COLUMN() {
	return R20_VALUE_IN_I_COLUMN;
}



public void setR20_VALUE_IN_I_COLUMN(BigDecimal r20_VALUE_IN_I_COLUMN) {
	R20_VALUE_IN_I_COLUMN = r20_VALUE_IN_I_COLUMN;
}



public BigDecimal getR20_BWP_AMOUNT() {
	return R20_BWP_AMOUNT;
}



public void setR20_BWP_AMOUNT(BigDecimal r20_BWP_AMOUNT) {
	R20_BWP_AMOUNT = r20_BWP_AMOUNT;
}



public BigDecimal getR21_VALUE_IN_I_COLUMN() {
	return R21_VALUE_IN_I_COLUMN;
}



public void setR21_VALUE_IN_I_COLUMN(BigDecimal r21_VALUE_IN_I_COLUMN) {
	R21_VALUE_IN_I_COLUMN = r21_VALUE_IN_I_COLUMN;
}



public BigDecimal getR21_BWP_AMOUNT() {
	return R21_BWP_AMOUNT;
}



public void setR21_BWP_AMOUNT(BigDecimal r21_BWP_AMOUNT) {
	R21_BWP_AMOUNT = r21_BWP_AMOUNT;
}



public BigDecimal getR22_VALUE_IN_I_COLUMN() {
	return R22_VALUE_IN_I_COLUMN;
}



public void setR22_VALUE_IN_I_COLUMN(BigDecimal r22_VALUE_IN_I_COLUMN) {
	R22_VALUE_IN_I_COLUMN = r22_VALUE_IN_I_COLUMN;
}



public BigDecimal getR22_BWP_AMOUNT() {
	return R22_BWP_AMOUNT;
}



public void setR22_BWP_AMOUNT(BigDecimal r22_BWP_AMOUNT) {
	R22_BWP_AMOUNT = r22_BWP_AMOUNT;
}



public BigDecimal getR23_VALUE_IN_I_COLUMN() {
	return R23_VALUE_IN_I_COLUMN;
}



public void setR23_VALUE_IN_I_COLUMN(BigDecimal r23_VALUE_IN_I_COLUMN) {
	R23_VALUE_IN_I_COLUMN = r23_VALUE_IN_I_COLUMN;
}



public BigDecimal getR23_BWP_AMOUNT() {
	return R23_BWP_AMOUNT;
}



public void setR23_BWP_AMOUNT(BigDecimal r23_BWP_AMOUNT) {
	R23_BWP_AMOUNT = r23_BWP_AMOUNT;
}



public BigDecimal getR24_VALUE_IN_I_COLUMN() {
	return R24_VALUE_IN_I_COLUMN;
}



public void setR24_VALUE_IN_I_COLUMN(BigDecimal r24_VALUE_IN_I_COLUMN) {
	R24_VALUE_IN_I_COLUMN = r24_VALUE_IN_I_COLUMN;
}



public BigDecimal getR24_BWP_AMOUNT() {
	return R24_BWP_AMOUNT;
}



public void setR24_BWP_AMOUNT(BigDecimal r24_BWP_AMOUNT) {
	R24_BWP_AMOUNT = r24_BWP_AMOUNT;
}



public BigDecimal getR25_VALUE_IN_I_COLUMN() {
	return R25_VALUE_IN_I_COLUMN;
}



public void setR25_VALUE_IN_I_COLUMN(BigDecimal r25_VALUE_IN_I_COLUMN) {
	R25_VALUE_IN_I_COLUMN = r25_VALUE_IN_I_COLUMN;
}



public BigDecimal getR25_BWP_AMOUNT() {
	return R25_BWP_AMOUNT;
}



public void setR25_BWP_AMOUNT(BigDecimal r25_BWP_AMOUNT) {
	R25_BWP_AMOUNT = r25_BWP_AMOUNT;
}



public BigDecimal getR26_VALUE_IN_I_COLUMN() {
	return R26_VALUE_IN_I_COLUMN;
}



public void setR26_VALUE_IN_I_COLUMN(BigDecimal r26_VALUE_IN_I_COLUMN) {
	R26_VALUE_IN_I_COLUMN = r26_VALUE_IN_I_COLUMN;
}



public BigDecimal getR26_BWP_AMOUNT() {
	return R26_BWP_AMOUNT;
}



public void setR26_BWP_AMOUNT(BigDecimal r26_BWP_AMOUNT) {
	R26_BWP_AMOUNT = r26_BWP_AMOUNT;
}



public BigDecimal getR27_VALUE_IN_I_COLUMN() {
	return R27_VALUE_IN_I_COLUMN;
}



public void setR27_VALUE_IN_I_COLUMN(BigDecimal r27_VALUE_IN_I_COLUMN) {
	R27_VALUE_IN_I_COLUMN = r27_VALUE_IN_I_COLUMN;
}



public BigDecimal getR27_BWP_AMOUNT() {
	return R27_BWP_AMOUNT;
}



public void setR27_BWP_AMOUNT(BigDecimal r27_BWP_AMOUNT) {
	R27_BWP_AMOUNT = r27_BWP_AMOUNT;
}



public BigDecimal getR28_VALUE_IN_I_COLUMN() {
	return R28_VALUE_IN_I_COLUMN;
}



public void setR28_VALUE_IN_I_COLUMN(BigDecimal r28_VALUE_IN_I_COLUMN) {
	R28_VALUE_IN_I_COLUMN = r28_VALUE_IN_I_COLUMN;
}



public BigDecimal getR28_BWP_AMOUNT() {
	return R28_BWP_AMOUNT;
}



public void setR28_BWP_AMOUNT(BigDecimal r28_BWP_AMOUNT) {
	R28_BWP_AMOUNT = r28_BWP_AMOUNT;
}



public BigDecimal getR29_VALUE_IN_I_COLUMN() {
	return R29_VALUE_IN_I_COLUMN;
}



public void setR29_VALUE_IN_I_COLUMN(BigDecimal r29_VALUE_IN_I_COLUMN) {
	R29_VALUE_IN_I_COLUMN = r29_VALUE_IN_I_COLUMN;
}



public BigDecimal getR29_BWP_AMOUNT() {
	return R29_BWP_AMOUNT;
}



public void setR29_BWP_AMOUNT(BigDecimal r29_BWP_AMOUNT) {
	R29_BWP_AMOUNT = r29_BWP_AMOUNT;
}



public BigDecimal getR30_VALUE_IN_I_COLUMN() {
	return R30_VALUE_IN_I_COLUMN;
}



public void setR30_VALUE_IN_I_COLUMN(BigDecimal r30_VALUE_IN_I_COLUMN) {
	R30_VALUE_IN_I_COLUMN = r30_VALUE_IN_I_COLUMN;
}



public BigDecimal getR30_BWP_AMOUNT() {
	return R30_BWP_AMOUNT;
}



public void setR30_BWP_AMOUNT(BigDecimal r30_BWP_AMOUNT) {
	R30_BWP_AMOUNT = r30_BWP_AMOUNT;
}



public BigDecimal getR31_VALUE_IN_I_COLUMN() {
	return R31_VALUE_IN_I_COLUMN;
}



public void setR31_VALUE_IN_I_COLUMN(BigDecimal r31_VALUE_IN_I_COLUMN) {
	R31_VALUE_IN_I_COLUMN = r31_VALUE_IN_I_COLUMN;
}



public BigDecimal getR31_BWP_AMOUNT() {
	return R31_BWP_AMOUNT;
}



public void setR31_BWP_AMOUNT(BigDecimal r31_BWP_AMOUNT) {
	R31_BWP_AMOUNT = r31_BWP_AMOUNT;
}



public BigDecimal getR32_VALUE_IN_I_COLUMN() {
	return R32_VALUE_IN_I_COLUMN;
}



public void setR32_VALUE_IN_I_COLUMN(BigDecimal r32_VALUE_IN_I_COLUMN) {
	R32_VALUE_IN_I_COLUMN = r32_VALUE_IN_I_COLUMN;
}



public BigDecimal getR32_BWP_AMOUNT() {
	return R32_BWP_AMOUNT;
}



public void setR32_BWP_AMOUNT(BigDecimal r32_BWP_AMOUNT) {
	R32_BWP_AMOUNT = r32_BWP_AMOUNT;
}



public BigDecimal getR33_VALUE_IN_I_COLUMN() {
	return R33_VALUE_IN_I_COLUMN;
}



public void setR33_VALUE_IN_I_COLUMN(BigDecimal r33_VALUE_IN_I_COLUMN) {
	R33_VALUE_IN_I_COLUMN = r33_VALUE_IN_I_COLUMN;
}



public BigDecimal getR33_BWP_AMOUNT() {
	return R33_BWP_AMOUNT;
}



public void setR33_BWP_AMOUNT(BigDecimal r33_BWP_AMOUNT) {
	R33_BWP_AMOUNT = r33_BWP_AMOUNT;
}



public BigDecimal getR34_VALUE_IN_I_COLUMN() {
	return R34_VALUE_IN_I_COLUMN;
}



public void setR34_VALUE_IN_I_COLUMN(BigDecimal r34_VALUE_IN_I_COLUMN) {
	R34_VALUE_IN_I_COLUMN = r34_VALUE_IN_I_COLUMN;
}



public BigDecimal getR34_BWP_AMOUNT() {
	return R34_BWP_AMOUNT;
}



public void setR34_BWP_AMOUNT(BigDecimal r34_BWP_AMOUNT) {
	R34_BWP_AMOUNT = r34_BWP_AMOUNT;
}



public BigDecimal getR35_VALUE_IN_I_COLUMN() {
	return R35_VALUE_IN_I_COLUMN;
}



public void setR35_VALUE_IN_I_COLUMN(BigDecimal r35_VALUE_IN_I_COLUMN) {
	R35_VALUE_IN_I_COLUMN = r35_VALUE_IN_I_COLUMN;
}



public BigDecimal getR35_BWP_AMOUNT() {
	return R35_BWP_AMOUNT;
}



public void setR35_BWP_AMOUNT(BigDecimal r35_BWP_AMOUNT) {
	R35_BWP_AMOUNT = r35_BWP_AMOUNT;
}



public BigDecimal getR36_VALUE_IN_I_COLUMN() {
	return R36_VALUE_IN_I_COLUMN;
}



public void setR36_VALUE_IN_I_COLUMN(BigDecimal r36_VALUE_IN_I_COLUMN) {
	R36_VALUE_IN_I_COLUMN = r36_VALUE_IN_I_COLUMN;
}



public BigDecimal getR36_BWP_AMOUNT() {
	return R36_BWP_AMOUNT;
}



public void setR36_BWP_AMOUNT(BigDecimal r36_BWP_AMOUNT) {
	R36_BWP_AMOUNT = r36_BWP_AMOUNT;
}



public BigDecimal getR37_VALUE_IN_I_COLUMN() {
	return R37_VALUE_IN_I_COLUMN;
}



public void setR37_VALUE_IN_I_COLUMN(BigDecimal r37_VALUE_IN_I_COLUMN) {
	R37_VALUE_IN_I_COLUMN = r37_VALUE_IN_I_COLUMN;
}



public BigDecimal getR37_BWP_AMOUNT() {
	return R37_BWP_AMOUNT;
}



public void setR37_BWP_AMOUNT(BigDecimal r37_BWP_AMOUNT) {
	R37_BWP_AMOUNT = r37_BWP_AMOUNT;
}



public BigDecimal getR41_RISK_ASSETS() {
	return R41_RISK_ASSETS;
}



public void setR41_RISK_ASSETS(BigDecimal r41_RISK_ASSETS) {
	R41_RISK_ASSETS = r41_RISK_ASSETS;
}



public BigDecimal getR42_RISK_ASSETS() {
	return R42_RISK_ASSETS;
}



public void setR42_RISK_ASSETS(BigDecimal r42_RISK_ASSETS) {
	R42_RISK_ASSETS = r42_RISK_ASSETS;
}



public BigDecimal getR43_RISK_ASSETS() {
	return R43_RISK_ASSETS;
}



public void setR43_RISK_ASSETS(BigDecimal r43_RISK_ASSETS) {
	R43_RISK_ASSETS = r43_RISK_ASSETS;
}



public BigDecimal getR44_RISK_ASSETS() {
	return R44_RISK_ASSETS;
}



public void setR44_RISK_ASSETS(BigDecimal r44_RISK_ASSETS) {
	R44_RISK_ASSETS = r44_RISK_ASSETS;
}



public BigDecimal getR46_PERCENTAGE_CF_TO_RWA() {
	return R46_PERCENTAGE_CF_TO_RWA;
}



public void setR46_PERCENTAGE_CF_TO_RWA(BigDecimal r46_PERCENTAGE_CF_TO_RWA) {
	R46_PERCENTAGE_CF_TO_RWA = r46_PERCENTAGE_CF_TO_RWA;
}



public BigDecimal getR47_PERCENTAGE_TIER1() {
	return R47_PERCENTAGE_TIER1;
}



public void setR47_PERCENTAGE_TIER1(BigDecimal r47_PERCENTAGE_TIER1) {
	R47_PERCENTAGE_TIER1 = r47_PERCENTAGE_TIER1;
}



public BigDecimal getR48_PERCENTAGE_TIER2() {
	return R48_PERCENTAGE_TIER2;
}



public void setR48_PERCENTAGE_TIER2(BigDecimal r48_PERCENTAGE_TIER2) {
	R48_PERCENTAGE_TIER2 = r48_PERCENTAGE_TIER2;
}



public BigDecimal getR52_DOMESTIC_RISK_ASSETS() {
	return R52_DOMESTIC_RISK_ASSETS;
}



public void setR52_DOMESTIC_RISK_ASSETS(BigDecimal r52_DOMESTIC_RISK_ASSETS) {
	R52_DOMESTIC_RISK_ASSETS = r52_DOMESTIC_RISK_ASSETS;
}



public BigDecimal getR52_FOREIGN_RISK_ASSETS() {
	return R52_FOREIGN_RISK_ASSETS;
}



public void setR52_FOREIGN_RISK_ASSETS(BigDecimal r52_FOREIGN_RISK_ASSETS) {
	R52_FOREIGN_RISK_ASSETS = r52_FOREIGN_RISK_ASSETS;
}



public BigDecimal getR52_TOTAL_RISK_ASSETS() {
	return R52_TOTAL_RISK_ASSETS;
}



public void setR52_TOTAL_RISK_ASSETS(BigDecimal r52_TOTAL_RISK_ASSETS) {
	R52_TOTAL_RISK_ASSETS = r52_TOTAL_RISK_ASSETS;
}



public BigDecimal getR53_DOMESTIC_RISK_ASSETS() {
	return R53_DOMESTIC_RISK_ASSETS;
}



public void setR53_DOMESTIC_RISK_ASSETS(BigDecimal r53_DOMESTIC_RISK_ASSETS) {
	R53_DOMESTIC_RISK_ASSETS = r53_DOMESTIC_RISK_ASSETS;
}



public BigDecimal getR53_FOREIGN_RISK_ASSETS() {
	return R53_FOREIGN_RISK_ASSETS;
}



public void setR53_FOREIGN_RISK_ASSETS(BigDecimal r53_FOREIGN_RISK_ASSETS) {
	R53_FOREIGN_RISK_ASSETS = r53_FOREIGN_RISK_ASSETS;
}



public BigDecimal getR53_TOTAL_RISK_ASSETS() {
	return R53_TOTAL_RISK_ASSETS;
}



public void setR53_TOTAL_RISK_ASSETS(BigDecimal r53_TOTAL_RISK_ASSETS) {
	R53_TOTAL_RISK_ASSETS = r53_TOTAL_RISK_ASSETS;
}



public BigDecimal getR54_DOMESTIC_RISK_ASSETS() {
	return R54_DOMESTIC_RISK_ASSETS;
}



public void setR54_DOMESTIC_RISK_ASSETS(BigDecimal r54_DOMESTIC_RISK_ASSETS) {
	R54_DOMESTIC_RISK_ASSETS = r54_DOMESTIC_RISK_ASSETS;
}



public BigDecimal getR54_FOREIGN_RISK_ASSETS() {
	return R54_FOREIGN_RISK_ASSETS;
}



public void setR54_FOREIGN_RISK_ASSETS(BigDecimal r54_FOREIGN_RISK_ASSETS) {
	R54_FOREIGN_RISK_ASSETS = r54_FOREIGN_RISK_ASSETS;
}



public BigDecimal getR54_TOTAL_RISK_ASSETS() {
	return R54_TOTAL_RISK_ASSETS;
}



public void setR54_TOTAL_RISK_ASSETS(BigDecimal r54_TOTAL_RISK_ASSETS) {
	R54_TOTAL_RISK_ASSETS = r54_TOTAL_RISK_ASSETS;
}



}

// ARCHIVAL SUMMARY 

public class TIER_1_2_CFS_Archival_RowMapper
        implements RowMapper<TIER_1_2_CFS_Archival_Summary_Entity> {

    @Override
    public TIER_1_2_CFS_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        TIER_1_2_CFS_Archival_Summary_Entity obj =
                new TIER_1_2_CFS_Archival_Summary_Entity();

        // =========================
        // COMMON FIELDS
        // =========================
        obj.setReport_date(rs.getDate("REPORT_DATE"));
        obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
        obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
        obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
        obj.setReport_code(rs.getString("REPORT_CODE"));
        obj.setReport_desc(rs.getString("REPORT_DESC"));
        obj.setEntity_flg(rs.getString("ENTITY_FLG"));
        obj.setModify_flg(rs.getString("MODIFY_FLG"));
        obj.setDel_flg(rs.getString("DEL_FLG"));

        // =========================
        // R10 → R20
        // =========================
        obj.setR10_VALUE_IN_I_COLUMN(rs.getBigDecimal("R10_VALUE_IN_I_COLUMN"));
        obj.setR10_BWP_AMOUNT(rs.getBigDecimal("R10_BWP_AMOUNT"));

        obj.setR11_VALUE_IN_I_COLUMN(rs.getBigDecimal("R11_VALUE_IN_I_COLUMN"));
        obj.setR11_BWP_AMOUNT(rs.getBigDecimal("R11_BWP_AMOUNT"));

        obj.setR12_VALUE_IN_I_COLUMN(rs.getBigDecimal("R12_VALUE_IN_I_COLUMN"));
        obj.setR12_BWP_AMOUNT(rs.getBigDecimal("R12_BWP_AMOUNT"));

        obj.setR13_VALUE_IN_I_COLUMN(rs.getBigDecimal("R13_VALUE_IN_I_COLUMN"));
        obj.setR13_BWP_AMOUNT(rs.getBigDecimal("R13_BWP_AMOUNT"));

        obj.setR14_VALUE_IN_I_COLUMN(rs.getBigDecimal("R14_VALUE_IN_I_COLUMN"));
        obj.setR14_BWP_AMOUNT(rs.getBigDecimal("R14_BWP_AMOUNT"));

        obj.setR15_VALUE_IN_I_COLUMN(rs.getBigDecimal("R15_VALUE_IN_I_COLUMN"));
        obj.setR15_BWP_AMOUNT(rs.getBigDecimal("R15_BWP_AMOUNT"));

        obj.setR16_VALUE_IN_I_COLUMN(rs.getBigDecimal("R16_VALUE_IN_I_COLUMN"));
        obj.setR16_BWP_AMOUNT(rs.getBigDecimal("R16_BWP_AMOUNT"));

        obj.setR17_VALUE_IN_I_COLUMN(rs.getBigDecimal("R17_VALUE_IN_I_COLUMN"));
        obj.setR17_BWP_AMOUNT(rs.getBigDecimal("R17_BWP_AMOUNT"));

        obj.setR18_VALUE_IN_I_COLUMN(rs.getBigDecimal("R18_VALUE_IN_I_COLUMN"));
        obj.setR18_BWP_AMOUNT(rs.getBigDecimal("R18_BWP_AMOUNT"));

        obj.setR19_VALUE_IN_I_COLUMN(rs.getBigDecimal("R19_VALUE_IN_I_COLUMN"));
        obj.setR19_BWP_AMOUNT(rs.getBigDecimal("R19_BWP_AMOUNT"));

        obj.setR20_VALUE_IN_I_COLUMN(rs.getBigDecimal("R20_VALUE_IN_I_COLUMN"));
        obj.setR20_BWP_AMOUNT(rs.getBigDecimal("R20_BWP_AMOUNT"));

        // =========================
        // R21 → R30
        // =========================
        obj.setR21_VALUE_IN_I_COLUMN(rs.getBigDecimal("R21_VALUE_IN_I_COLUMN"));
        obj.setR21_BWP_AMOUNT(rs.getBigDecimal("R21_BWP_AMOUNT"));

        obj.setR22_VALUE_IN_I_COLUMN(rs.getBigDecimal("R22_VALUE_IN_I_COLUMN"));
        obj.setR22_BWP_AMOUNT(rs.getBigDecimal("R22_BWP_AMOUNT"));

        obj.setR23_VALUE_IN_I_COLUMN(rs.getBigDecimal("R23_VALUE_IN_I_COLUMN"));
        obj.setR23_BWP_AMOUNT(rs.getBigDecimal("R23_BWP_AMOUNT"));

        obj.setR24_VALUE_IN_I_COLUMN(rs.getBigDecimal("R24_VALUE_IN_I_COLUMN"));
        obj.setR24_BWP_AMOUNT(rs.getBigDecimal("R24_BWP_AMOUNT"));

        obj.setR25_VALUE_IN_I_COLUMN(rs.getBigDecimal("R25_VALUE_IN_I_COLUMN"));
        obj.setR25_BWP_AMOUNT(rs.getBigDecimal("R25_BWP_AMOUNT"));

        obj.setR26_VALUE_IN_I_COLUMN(rs.getBigDecimal("R26_VALUE_IN_I_COLUMN"));
        obj.setR26_BWP_AMOUNT(rs.getBigDecimal("R26_BWP_AMOUNT"));

        obj.setR27_VALUE_IN_I_COLUMN(rs.getBigDecimal("R27_VALUE_IN_I_COLUMN"));
        obj.setR27_BWP_AMOUNT(rs.getBigDecimal("R27_BWP_AMOUNT"));

        obj.setR28_VALUE_IN_I_COLUMN(rs.getBigDecimal("R28_VALUE_IN_I_COLUMN"));
        obj.setR28_BWP_AMOUNT(rs.getBigDecimal("R28_BWP_AMOUNT"));

        obj.setR29_VALUE_IN_I_COLUMN(rs.getBigDecimal("R29_VALUE_IN_I_COLUMN"));
        obj.setR29_BWP_AMOUNT(rs.getBigDecimal("R29_BWP_AMOUNT"));

        obj.setR30_VALUE_IN_I_COLUMN(rs.getBigDecimal("R30_VALUE_IN_I_COLUMN"));
        obj.setR30_BWP_AMOUNT(rs.getBigDecimal("R30_BWP_AMOUNT"));

        // =========================
        // R31 → R37
        // =========================
        obj.setR31_VALUE_IN_I_COLUMN(rs.getBigDecimal("R31_VALUE_IN_I_COLUMN"));
        obj.setR31_BWP_AMOUNT(rs.getBigDecimal("R31_BWP_AMOUNT"));

        obj.setR32_VALUE_IN_I_COLUMN(rs.getBigDecimal("R32_VALUE_IN_I_COLUMN"));
        obj.setR32_BWP_AMOUNT(rs.getBigDecimal("R32_BWP_AMOUNT"));

        obj.setR33_VALUE_IN_I_COLUMN(rs.getBigDecimal("R33_VALUE_IN_I_COLUMN"));
        obj.setR33_BWP_AMOUNT(rs.getBigDecimal("R33_BWP_AMOUNT"));

        obj.setR34_VALUE_IN_I_COLUMN(rs.getBigDecimal("R34_VALUE_IN_I_COLUMN"));
        obj.setR34_BWP_AMOUNT(rs.getBigDecimal("R34_BWP_AMOUNT"));

        obj.setR35_VALUE_IN_I_COLUMN(rs.getBigDecimal("R35_VALUE_IN_I_COLUMN"));
        obj.setR35_BWP_AMOUNT(rs.getBigDecimal("R35_BWP_AMOUNT"));

        obj.setR36_VALUE_IN_I_COLUMN(rs.getBigDecimal("R36_VALUE_IN_I_COLUMN"));
        obj.setR36_BWP_AMOUNT(rs.getBigDecimal("R36_BWP_AMOUNT"));

        obj.setR37_VALUE_IN_I_COLUMN(rs.getBigDecimal("R37_VALUE_IN_I_COLUMN"));
        obj.setR37_BWP_AMOUNT(rs.getBigDecimal("R37_BWP_AMOUNT"));

        // =========================
        // R41 → R48
        // =========================
        obj.setR41_RISK_ASSETS(rs.getBigDecimal("R41_RISK_ASSETS"));
        obj.setR42_RISK_ASSETS(rs.getBigDecimal("R42_RISK_ASSETS"));
        obj.setR43_RISK_ASSETS(rs.getBigDecimal("R43_RISK_ASSETS"));
        obj.setR44_RISK_ASSETS(rs.getBigDecimal("R44_RISK_ASSETS"));
        obj.setR46_PERCENTAGE_CF_TO_RWA(rs.getBigDecimal("R46_PERCENTAGE_CF_TO_RWA"));
        obj.setR47_PERCENTAGE_TIER1(rs.getBigDecimal("R47_PERCENTAGE_TIER1"));
        obj.setR48_PERCENTAGE_TIER2(rs.getBigDecimal("R48_PERCENTAGE_TIER2"));

        // =========================
        // R52 → R54
        // =========================
        obj.setR52_DOMESTIC_RISK_ASSETS(rs.getBigDecimal("R52_DOMESTIC_RISK_ASSETS"));
        obj.setR52_FOREIGN_RISK_ASSETS(rs.getBigDecimal("R52_FOREIGN_RISK_ASSETS"));
        obj.setR52_TOTAL_RISK_ASSETS(rs.getBigDecimal("R52_TOTAL_RISK_ASSETS"));

        obj.setR53_DOMESTIC_RISK_ASSETS(rs.getBigDecimal("R53_DOMESTIC_RISK_ASSETS"));
        obj.setR53_FOREIGN_RISK_ASSETS(rs.getBigDecimal("R53_FOREIGN_RISK_ASSETS"));
        obj.setR53_TOTAL_RISK_ASSETS(rs.getBigDecimal("R53_TOTAL_RISK_ASSETS"));

        obj.setR54_DOMESTIC_RISK_ASSETS(rs.getBigDecimal("R54_DOMESTIC_RISK_ASSETS"));
        obj.setR54_FOREIGN_RISK_ASSETS(rs.getBigDecimal("R54_FOREIGN_RISK_ASSETS"));
        obj.setR54_TOTAL_RISK_ASSETS(rs.getBigDecimal("R54_TOTAL_RISK_ASSETS"));

        return obj;
    }
}


public class TIER_1_2_CFS_Archival_Summary_Entity {

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

private BigDecimal R10_VALUE_IN_I_COLUMN;
private BigDecimal R10_BWP_AMOUNT;
private BigDecimal R11_VALUE_IN_I_COLUMN;
private BigDecimal R11_BWP_AMOUNT;
private BigDecimal R12_VALUE_IN_I_COLUMN;
private BigDecimal R12_BWP_AMOUNT;
private BigDecimal R13_VALUE_IN_I_COLUMN;
private BigDecimal R13_BWP_AMOUNT;
private BigDecimal R14_VALUE_IN_I_COLUMN;
private BigDecimal R14_BWP_AMOUNT;
private BigDecimal R15_VALUE_IN_I_COLUMN;
private BigDecimal R15_BWP_AMOUNT;
private BigDecimal R16_VALUE_IN_I_COLUMN;
private BigDecimal R16_BWP_AMOUNT;
private BigDecimal R17_VALUE_IN_I_COLUMN;
private BigDecimal R17_BWP_AMOUNT;
private BigDecimal R18_VALUE_IN_I_COLUMN;
private BigDecimal R18_BWP_AMOUNT;
private BigDecimal R19_VALUE_IN_I_COLUMN;
private BigDecimal R19_BWP_AMOUNT;
private BigDecimal R20_VALUE_IN_I_COLUMN;
private BigDecimal R20_BWP_AMOUNT;
private BigDecimal R21_VALUE_IN_I_COLUMN;
private BigDecimal R21_BWP_AMOUNT;
private BigDecimal R22_VALUE_IN_I_COLUMN;
private BigDecimal R22_BWP_AMOUNT;
private BigDecimal R23_VALUE_IN_I_COLUMN;
private BigDecimal R23_BWP_AMOUNT;
private BigDecimal R24_VALUE_IN_I_COLUMN;
private BigDecimal R24_BWP_AMOUNT;
private BigDecimal R25_VALUE_IN_I_COLUMN;
private BigDecimal R25_BWP_AMOUNT;
private BigDecimal R26_VALUE_IN_I_COLUMN;
private BigDecimal R26_BWP_AMOUNT;
private BigDecimal R27_VALUE_IN_I_COLUMN;
private BigDecimal R27_BWP_AMOUNT;
private BigDecimal R28_VALUE_IN_I_COLUMN;
private BigDecimal R28_BWP_AMOUNT;
private BigDecimal R29_VALUE_IN_I_COLUMN;
private BigDecimal R29_BWP_AMOUNT;
private BigDecimal R30_VALUE_IN_I_COLUMN;
private BigDecimal R30_BWP_AMOUNT;
private BigDecimal R31_VALUE_IN_I_COLUMN;
private BigDecimal R31_BWP_AMOUNT;
private BigDecimal R32_VALUE_IN_I_COLUMN;
private BigDecimal R32_BWP_AMOUNT;
private BigDecimal R33_VALUE_IN_I_COLUMN;
private BigDecimal R33_BWP_AMOUNT;
private BigDecimal R34_VALUE_IN_I_COLUMN;
private BigDecimal R34_BWP_AMOUNT;
private BigDecimal R35_VALUE_IN_I_COLUMN;
private BigDecimal R35_BWP_AMOUNT;
private BigDecimal R36_VALUE_IN_I_COLUMN;
private BigDecimal R36_BWP_AMOUNT;
private BigDecimal R37_VALUE_IN_I_COLUMN;
private BigDecimal R37_BWP_AMOUNT;
private BigDecimal R41_RISK_ASSETS;
private BigDecimal R42_RISK_ASSETS;
private BigDecimal R43_RISK_ASSETS;
private BigDecimal R44_RISK_ASSETS;
private BigDecimal R46_PERCENTAGE_CF_TO_RWA;
private BigDecimal R47_PERCENTAGE_TIER1;
private BigDecimal R48_PERCENTAGE_TIER2;
private BigDecimal R52_DOMESTIC_RISK_ASSETS;
private BigDecimal R52_FOREIGN_RISK_ASSETS;
private BigDecimal R52_TOTAL_RISK_ASSETS;
private BigDecimal R53_DOMESTIC_RISK_ASSETS;
private BigDecimal R53_FOREIGN_RISK_ASSETS;
private BigDecimal R53_TOTAL_RISK_ASSETS;
private BigDecimal R54_DOMESTIC_RISK_ASSETS;
private BigDecimal R54_FOREIGN_RISK_ASSETS;
private BigDecimal R54_TOTAL_RISK_ASSETS;



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



public BigDecimal getR10_VALUE_IN_I_COLUMN() {
	return R10_VALUE_IN_I_COLUMN;
}



public void setR10_VALUE_IN_I_COLUMN(BigDecimal r10_VALUE_IN_I_COLUMN) {
	R10_VALUE_IN_I_COLUMN = r10_VALUE_IN_I_COLUMN;
}



public BigDecimal getR10_BWP_AMOUNT() {
	return R10_BWP_AMOUNT;
}



public void setR10_BWP_AMOUNT(BigDecimal r10_BWP_AMOUNT) {
	R10_BWP_AMOUNT = r10_BWP_AMOUNT;
}



public BigDecimal getR11_VALUE_IN_I_COLUMN() {
	return R11_VALUE_IN_I_COLUMN;
}



public void setR11_VALUE_IN_I_COLUMN(BigDecimal r11_VALUE_IN_I_COLUMN) {
	R11_VALUE_IN_I_COLUMN = r11_VALUE_IN_I_COLUMN;
}



public BigDecimal getR11_BWP_AMOUNT() {
	return R11_BWP_AMOUNT;
}



public void setR11_BWP_AMOUNT(BigDecimal r11_BWP_AMOUNT) {
	R11_BWP_AMOUNT = r11_BWP_AMOUNT;
}



public BigDecimal getR12_VALUE_IN_I_COLUMN() {
	return R12_VALUE_IN_I_COLUMN;
}



public void setR12_VALUE_IN_I_COLUMN(BigDecimal r12_VALUE_IN_I_COLUMN) {
	R12_VALUE_IN_I_COLUMN = r12_VALUE_IN_I_COLUMN;
}



public BigDecimal getR12_BWP_AMOUNT() {
	return R12_BWP_AMOUNT;
}



public void setR12_BWP_AMOUNT(BigDecimal r12_BWP_AMOUNT) {
	R12_BWP_AMOUNT = r12_BWP_AMOUNT;
}



public BigDecimal getR13_VALUE_IN_I_COLUMN() {
	return R13_VALUE_IN_I_COLUMN;
}



public void setR13_VALUE_IN_I_COLUMN(BigDecimal r13_VALUE_IN_I_COLUMN) {
	R13_VALUE_IN_I_COLUMN = r13_VALUE_IN_I_COLUMN;
}



public BigDecimal getR13_BWP_AMOUNT() {
	return R13_BWP_AMOUNT;
}



public void setR13_BWP_AMOUNT(BigDecimal r13_BWP_AMOUNT) {
	R13_BWP_AMOUNT = r13_BWP_AMOUNT;
}



public BigDecimal getR14_VALUE_IN_I_COLUMN() {
	return R14_VALUE_IN_I_COLUMN;
}



public void setR14_VALUE_IN_I_COLUMN(BigDecimal r14_VALUE_IN_I_COLUMN) {
	R14_VALUE_IN_I_COLUMN = r14_VALUE_IN_I_COLUMN;
}



public BigDecimal getR14_BWP_AMOUNT() {
	return R14_BWP_AMOUNT;
}



public void setR14_BWP_AMOUNT(BigDecimal r14_BWP_AMOUNT) {
	R14_BWP_AMOUNT = r14_BWP_AMOUNT;
}



public BigDecimal getR15_VALUE_IN_I_COLUMN() {
	return R15_VALUE_IN_I_COLUMN;
}



public void setR15_VALUE_IN_I_COLUMN(BigDecimal r15_VALUE_IN_I_COLUMN) {
	R15_VALUE_IN_I_COLUMN = r15_VALUE_IN_I_COLUMN;
}



public BigDecimal getR15_BWP_AMOUNT() {
	return R15_BWP_AMOUNT;
}



public void setR15_BWP_AMOUNT(BigDecimal r15_BWP_AMOUNT) {
	R15_BWP_AMOUNT = r15_BWP_AMOUNT;
}



public BigDecimal getR16_VALUE_IN_I_COLUMN() {
	return R16_VALUE_IN_I_COLUMN;
}



public void setR16_VALUE_IN_I_COLUMN(BigDecimal r16_VALUE_IN_I_COLUMN) {
	R16_VALUE_IN_I_COLUMN = r16_VALUE_IN_I_COLUMN;
}



public BigDecimal getR16_BWP_AMOUNT() {
	return R16_BWP_AMOUNT;
}



public void setR16_BWP_AMOUNT(BigDecimal r16_BWP_AMOUNT) {
	R16_BWP_AMOUNT = r16_BWP_AMOUNT;
}



public BigDecimal getR17_VALUE_IN_I_COLUMN() {
	return R17_VALUE_IN_I_COLUMN;
}



public void setR17_VALUE_IN_I_COLUMN(BigDecimal r17_VALUE_IN_I_COLUMN) {
	R17_VALUE_IN_I_COLUMN = r17_VALUE_IN_I_COLUMN;
}



public BigDecimal getR17_BWP_AMOUNT() {
	return R17_BWP_AMOUNT;
}



public void setR17_BWP_AMOUNT(BigDecimal r17_BWP_AMOUNT) {
	R17_BWP_AMOUNT = r17_BWP_AMOUNT;
}



public BigDecimal getR18_VALUE_IN_I_COLUMN() {
	return R18_VALUE_IN_I_COLUMN;
}



public void setR18_VALUE_IN_I_COLUMN(BigDecimal r18_VALUE_IN_I_COLUMN) {
	R18_VALUE_IN_I_COLUMN = r18_VALUE_IN_I_COLUMN;
}



public BigDecimal getR18_BWP_AMOUNT() {
	return R18_BWP_AMOUNT;
}



public void setR18_BWP_AMOUNT(BigDecimal r18_BWP_AMOUNT) {
	R18_BWP_AMOUNT = r18_BWP_AMOUNT;
}



public BigDecimal getR19_VALUE_IN_I_COLUMN() {
	return R19_VALUE_IN_I_COLUMN;
}



public void setR19_VALUE_IN_I_COLUMN(BigDecimal r19_VALUE_IN_I_COLUMN) {
	R19_VALUE_IN_I_COLUMN = r19_VALUE_IN_I_COLUMN;
}



public BigDecimal getR19_BWP_AMOUNT() {
	return R19_BWP_AMOUNT;
}



public void setR19_BWP_AMOUNT(BigDecimal r19_BWP_AMOUNT) {
	R19_BWP_AMOUNT = r19_BWP_AMOUNT;
}



public BigDecimal getR20_VALUE_IN_I_COLUMN() {
	return R20_VALUE_IN_I_COLUMN;
}



public void setR20_VALUE_IN_I_COLUMN(BigDecimal r20_VALUE_IN_I_COLUMN) {
	R20_VALUE_IN_I_COLUMN = r20_VALUE_IN_I_COLUMN;
}



public BigDecimal getR20_BWP_AMOUNT() {
	return R20_BWP_AMOUNT;
}



public void setR20_BWP_AMOUNT(BigDecimal r20_BWP_AMOUNT) {
	R20_BWP_AMOUNT = r20_BWP_AMOUNT;
}



public BigDecimal getR21_VALUE_IN_I_COLUMN() {
	return R21_VALUE_IN_I_COLUMN;
}



public void setR21_VALUE_IN_I_COLUMN(BigDecimal r21_VALUE_IN_I_COLUMN) {
	R21_VALUE_IN_I_COLUMN = r21_VALUE_IN_I_COLUMN;
}



public BigDecimal getR21_BWP_AMOUNT() {
	return R21_BWP_AMOUNT;
}



public void setR21_BWP_AMOUNT(BigDecimal r21_BWP_AMOUNT) {
	R21_BWP_AMOUNT = r21_BWP_AMOUNT;
}



public BigDecimal getR22_VALUE_IN_I_COLUMN() {
	return R22_VALUE_IN_I_COLUMN;
}



public void setR22_VALUE_IN_I_COLUMN(BigDecimal r22_VALUE_IN_I_COLUMN) {
	R22_VALUE_IN_I_COLUMN = r22_VALUE_IN_I_COLUMN;
}



public BigDecimal getR22_BWP_AMOUNT() {
	return R22_BWP_AMOUNT;
}



public void setR22_BWP_AMOUNT(BigDecimal r22_BWP_AMOUNT) {
	R22_BWP_AMOUNT = r22_BWP_AMOUNT;
}



public BigDecimal getR23_VALUE_IN_I_COLUMN() {
	return R23_VALUE_IN_I_COLUMN;
}



public void setR23_VALUE_IN_I_COLUMN(BigDecimal r23_VALUE_IN_I_COLUMN) {
	R23_VALUE_IN_I_COLUMN = r23_VALUE_IN_I_COLUMN;
}



public BigDecimal getR23_BWP_AMOUNT() {
	return R23_BWP_AMOUNT;
}



public void setR23_BWP_AMOUNT(BigDecimal r23_BWP_AMOUNT) {
	R23_BWP_AMOUNT = r23_BWP_AMOUNT;
}



public BigDecimal getR24_VALUE_IN_I_COLUMN() {
	return R24_VALUE_IN_I_COLUMN;
}



public void setR24_VALUE_IN_I_COLUMN(BigDecimal r24_VALUE_IN_I_COLUMN) {
	R24_VALUE_IN_I_COLUMN = r24_VALUE_IN_I_COLUMN;
}



public BigDecimal getR24_BWP_AMOUNT() {
	return R24_BWP_AMOUNT;
}



public void setR24_BWP_AMOUNT(BigDecimal r24_BWP_AMOUNT) {
	R24_BWP_AMOUNT = r24_BWP_AMOUNT;
}



public BigDecimal getR25_VALUE_IN_I_COLUMN() {
	return R25_VALUE_IN_I_COLUMN;
}



public void setR25_VALUE_IN_I_COLUMN(BigDecimal r25_VALUE_IN_I_COLUMN) {
	R25_VALUE_IN_I_COLUMN = r25_VALUE_IN_I_COLUMN;
}



public BigDecimal getR25_BWP_AMOUNT() {
	return R25_BWP_AMOUNT;
}



public void setR25_BWP_AMOUNT(BigDecimal r25_BWP_AMOUNT) {
	R25_BWP_AMOUNT = r25_BWP_AMOUNT;
}



public BigDecimal getR26_VALUE_IN_I_COLUMN() {
	return R26_VALUE_IN_I_COLUMN;
}



public void setR26_VALUE_IN_I_COLUMN(BigDecimal r26_VALUE_IN_I_COLUMN) {
	R26_VALUE_IN_I_COLUMN = r26_VALUE_IN_I_COLUMN;
}



public BigDecimal getR26_BWP_AMOUNT() {
	return R26_BWP_AMOUNT;
}



public void setR26_BWP_AMOUNT(BigDecimal r26_BWP_AMOUNT) {
	R26_BWP_AMOUNT = r26_BWP_AMOUNT;
}



public BigDecimal getR27_VALUE_IN_I_COLUMN() {
	return R27_VALUE_IN_I_COLUMN;
}



public void setR27_VALUE_IN_I_COLUMN(BigDecimal r27_VALUE_IN_I_COLUMN) {
	R27_VALUE_IN_I_COLUMN = r27_VALUE_IN_I_COLUMN;
}



public BigDecimal getR27_BWP_AMOUNT() {
	return R27_BWP_AMOUNT;
}



public void setR27_BWP_AMOUNT(BigDecimal r27_BWP_AMOUNT) {
	R27_BWP_AMOUNT = r27_BWP_AMOUNT;
}



public BigDecimal getR28_VALUE_IN_I_COLUMN() {
	return R28_VALUE_IN_I_COLUMN;
}



public void setR28_VALUE_IN_I_COLUMN(BigDecimal r28_VALUE_IN_I_COLUMN) {
	R28_VALUE_IN_I_COLUMN = r28_VALUE_IN_I_COLUMN;
}



public BigDecimal getR28_BWP_AMOUNT() {
	return R28_BWP_AMOUNT;
}



public void setR28_BWP_AMOUNT(BigDecimal r28_BWP_AMOUNT) {
	R28_BWP_AMOUNT = r28_BWP_AMOUNT;
}



public BigDecimal getR29_VALUE_IN_I_COLUMN() {
	return R29_VALUE_IN_I_COLUMN;
}



public void setR29_VALUE_IN_I_COLUMN(BigDecimal r29_VALUE_IN_I_COLUMN) {
	R29_VALUE_IN_I_COLUMN = r29_VALUE_IN_I_COLUMN;
}



public BigDecimal getR29_BWP_AMOUNT() {
	return R29_BWP_AMOUNT;
}



public void setR29_BWP_AMOUNT(BigDecimal r29_BWP_AMOUNT) {
	R29_BWP_AMOUNT = r29_BWP_AMOUNT;
}



public BigDecimal getR30_VALUE_IN_I_COLUMN() {
	return R30_VALUE_IN_I_COLUMN;
}



public void setR30_VALUE_IN_I_COLUMN(BigDecimal r30_VALUE_IN_I_COLUMN) {
	R30_VALUE_IN_I_COLUMN = r30_VALUE_IN_I_COLUMN;
}



public BigDecimal getR30_BWP_AMOUNT() {
	return R30_BWP_AMOUNT;
}



public void setR30_BWP_AMOUNT(BigDecimal r30_BWP_AMOUNT) {
	R30_BWP_AMOUNT = r30_BWP_AMOUNT;
}



public BigDecimal getR31_VALUE_IN_I_COLUMN() {
	return R31_VALUE_IN_I_COLUMN;
}



public void setR31_VALUE_IN_I_COLUMN(BigDecimal r31_VALUE_IN_I_COLUMN) {
	R31_VALUE_IN_I_COLUMN = r31_VALUE_IN_I_COLUMN;
}



public BigDecimal getR31_BWP_AMOUNT() {
	return R31_BWP_AMOUNT;
}



public void setR31_BWP_AMOUNT(BigDecimal r31_BWP_AMOUNT) {
	R31_BWP_AMOUNT = r31_BWP_AMOUNT;
}



public BigDecimal getR32_VALUE_IN_I_COLUMN() {
	return R32_VALUE_IN_I_COLUMN;
}



public void setR32_VALUE_IN_I_COLUMN(BigDecimal r32_VALUE_IN_I_COLUMN) {
	R32_VALUE_IN_I_COLUMN = r32_VALUE_IN_I_COLUMN;
}



public BigDecimal getR32_BWP_AMOUNT() {
	return R32_BWP_AMOUNT;
}



public void setR32_BWP_AMOUNT(BigDecimal r32_BWP_AMOUNT) {
	R32_BWP_AMOUNT = r32_BWP_AMOUNT;
}



public BigDecimal getR33_VALUE_IN_I_COLUMN() {
	return R33_VALUE_IN_I_COLUMN;
}



public void setR33_VALUE_IN_I_COLUMN(BigDecimal r33_VALUE_IN_I_COLUMN) {
	R33_VALUE_IN_I_COLUMN = r33_VALUE_IN_I_COLUMN;
}



public BigDecimal getR33_BWP_AMOUNT() {
	return R33_BWP_AMOUNT;
}



public void setR33_BWP_AMOUNT(BigDecimal r33_BWP_AMOUNT) {
	R33_BWP_AMOUNT = r33_BWP_AMOUNT;
}



public BigDecimal getR34_VALUE_IN_I_COLUMN() {
	return R34_VALUE_IN_I_COLUMN;
}



public void setR34_VALUE_IN_I_COLUMN(BigDecimal r34_VALUE_IN_I_COLUMN) {
	R34_VALUE_IN_I_COLUMN = r34_VALUE_IN_I_COLUMN;
}



public BigDecimal getR34_BWP_AMOUNT() {
	return R34_BWP_AMOUNT;
}



public void setR34_BWP_AMOUNT(BigDecimal r34_BWP_AMOUNT) {
	R34_BWP_AMOUNT = r34_BWP_AMOUNT;
}



public BigDecimal getR35_VALUE_IN_I_COLUMN() {
	return R35_VALUE_IN_I_COLUMN;
}



public void setR35_VALUE_IN_I_COLUMN(BigDecimal r35_VALUE_IN_I_COLUMN) {
	R35_VALUE_IN_I_COLUMN = r35_VALUE_IN_I_COLUMN;
}



public BigDecimal getR35_BWP_AMOUNT() {
	return R35_BWP_AMOUNT;
}



public void setR35_BWP_AMOUNT(BigDecimal r35_BWP_AMOUNT) {
	R35_BWP_AMOUNT = r35_BWP_AMOUNT;
}



public BigDecimal getR36_VALUE_IN_I_COLUMN() {
	return R36_VALUE_IN_I_COLUMN;
}



public void setR36_VALUE_IN_I_COLUMN(BigDecimal r36_VALUE_IN_I_COLUMN) {
	R36_VALUE_IN_I_COLUMN = r36_VALUE_IN_I_COLUMN;
}



public BigDecimal getR36_BWP_AMOUNT() {
	return R36_BWP_AMOUNT;
}



public void setR36_BWP_AMOUNT(BigDecimal r36_BWP_AMOUNT) {
	R36_BWP_AMOUNT = r36_BWP_AMOUNT;
}



public BigDecimal getR37_VALUE_IN_I_COLUMN() {
	return R37_VALUE_IN_I_COLUMN;
}



public void setR37_VALUE_IN_I_COLUMN(BigDecimal r37_VALUE_IN_I_COLUMN) {
	R37_VALUE_IN_I_COLUMN = r37_VALUE_IN_I_COLUMN;
}



public BigDecimal getR37_BWP_AMOUNT() {
	return R37_BWP_AMOUNT;
}



public void setR37_BWP_AMOUNT(BigDecimal r37_BWP_AMOUNT) {
	R37_BWP_AMOUNT = r37_BWP_AMOUNT;
}



public BigDecimal getR41_RISK_ASSETS() {
	return R41_RISK_ASSETS;
}



public void setR41_RISK_ASSETS(BigDecimal r41_RISK_ASSETS) {
	R41_RISK_ASSETS = r41_RISK_ASSETS;
}



public BigDecimal getR42_RISK_ASSETS() {
	return R42_RISK_ASSETS;
}



public void setR42_RISK_ASSETS(BigDecimal r42_RISK_ASSETS) {
	R42_RISK_ASSETS = r42_RISK_ASSETS;
}



public BigDecimal getR43_RISK_ASSETS() {
	return R43_RISK_ASSETS;
}



public void setR43_RISK_ASSETS(BigDecimal r43_RISK_ASSETS) {
	R43_RISK_ASSETS = r43_RISK_ASSETS;
}



public BigDecimal getR44_RISK_ASSETS() {
	return R44_RISK_ASSETS;
}



public void setR44_RISK_ASSETS(BigDecimal r44_RISK_ASSETS) {
	R44_RISK_ASSETS = r44_RISK_ASSETS;
}



public BigDecimal getR46_PERCENTAGE_CF_TO_RWA() {
	return R46_PERCENTAGE_CF_TO_RWA;
}



public void setR46_PERCENTAGE_CF_TO_RWA(BigDecimal r46_PERCENTAGE_CF_TO_RWA) {
	R46_PERCENTAGE_CF_TO_RWA = r46_PERCENTAGE_CF_TO_RWA;
}



public BigDecimal getR47_PERCENTAGE_TIER1() {
	return R47_PERCENTAGE_TIER1;
}



public void setR47_PERCENTAGE_TIER1(BigDecimal r47_PERCENTAGE_TIER1) {
	R47_PERCENTAGE_TIER1 = r47_PERCENTAGE_TIER1;
}



public BigDecimal getR48_PERCENTAGE_TIER2() {
	return R48_PERCENTAGE_TIER2;
}



public void setR48_PERCENTAGE_TIER2(BigDecimal r48_PERCENTAGE_TIER2) {
	R48_PERCENTAGE_TIER2 = r48_PERCENTAGE_TIER2;
}



public BigDecimal getR52_DOMESTIC_RISK_ASSETS() {
	return R52_DOMESTIC_RISK_ASSETS;
}



public void setR52_DOMESTIC_RISK_ASSETS(BigDecimal r52_DOMESTIC_RISK_ASSETS) {
	R52_DOMESTIC_RISK_ASSETS = r52_DOMESTIC_RISK_ASSETS;
}



public BigDecimal getR52_FOREIGN_RISK_ASSETS() {
	return R52_FOREIGN_RISK_ASSETS;
}



public void setR52_FOREIGN_RISK_ASSETS(BigDecimal r52_FOREIGN_RISK_ASSETS) {
	R52_FOREIGN_RISK_ASSETS = r52_FOREIGN_RISK_ASSETS;
}



public BigDecimal getR52_TOTAL_RISK_ASSETS() {
	return R52_TOTAL_RISK_ASSETS;
}



public void setR52_TOTAL_RISK_ASSETS(BigDecimal r52_TOTAL_RISK_ASSETS) {
	R52_TOTAL_RISK_ASSETS = r52_TOTAL_RISK_ASSETS;
}



public BigDecimal getR53_DOMESTIC_RISK_ASSETS() {
	return R53_DOMESTIC_RISK_ASSETS;
}



public void setR53_DOMESTIC_RISK_ASSETS(BigDecimal r53_DOMESTIC_RISK_ASSETS) {
	R53_DOMESTIC_RISK_ASSETS = r53_DOMESTIC_RISK_ASSETS;
}



public BigDecimal getR53_FOREIGN_RISK_ASSETS() {
	return R53_FOREIGN_RISK_ASSETS;
}



public void setR53_FOREIGN_RISK_ASSETS(BigDecimal r53_FOREIGN_RISK_ASSETS) {
	R53_FOREIGN_RISK_ASSETS = r53_FOREIGN_RISK_ASSETS;
}



public BigDecimal getR53_TOTAL_RISK_ASSETS() {
	return R53_TOTAL_RISK_ASSETS;
}



public void setR53_TOTAL_RISK_ASSETS(BigDecimal r53_TOTAL_RISK_ASSETS) {
	R53_TOTAL_RISK_ASSETS = r53_TOTAL_RISK_ASSETS;
}



public BigDecimal getR54_DOMESTIC_RISK_ASSETS() {
	return R54_DOMESTIC_RISK_ASSETS;
}



public void setR54_DOMESTIC_RISK_ASSETS(BigDecimal r54_DOMESTIC_RISK_ASSETS) {
	R54_DOMESTIC_RISK_ASSETS = r54_DOMESTIC_RISK_ASSETS;
}



public BigDecimal getR54_FOREIGN_RISK_ASSETS() {
	return R54_FOREIGN_RISK_ASSETS;
}



public void setR54_FOREIGN_RISK_ASSETS(BigDecimal r54_FOREIGN_RISK_ASSETS) {
	R54_FOREIGN_RISK_ASSETS = r54_FOREIGN_RISK_ASSETS;
}



public BigDecimal getR54_TOTAL_RISK_ASSETS() {
	return R54_TOTAL_RISK_ASSETS;
}



public void setR54_TOTAL_RISK_ASSETS(BigDecimal r54_TOTAL_RISK_ASSETS) {
	R54_TOTAL_RISK_ASSETS = r54_TOTAL_RISK_ASSETS;
}




public Date getReportResubDate() {
	return reportResubDate;
}



public void setReportResubDate(Date reportResubDate) {
	this.reportResubDate = reportResubDate;
}


}

// DETAIL ENTITY CLASS 


public class TIER_1_2_CFS_Detail_RowMapper
        implements RowMapper<TIER_1_2_CFS_Detail_Entity> {

    @Override
    public TIER_1_2_CFS_Detail_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        TIER_1_2_CFS_Detail_Entity obj = new TIER_1_2_CFS_Detail_Entity();

        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));
        obj.setDataType(rs.getString("DATA_TYPE"));
        obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));

        //  DB column is REPORT_LABLE (as per your entity)
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

        // char fields (handle null safely)
        obj.setEntityFlg(rs.getString("ENTITY_FLG"));
        obj.setModifyFlg(rs.getString("MODIFY_FLG"));
        obj.setDelFlg(rs.getString("DEL_FLG"));

        return obj;
    }
}

public class TIER_1_2_CFS_Detail_Entity {
	
	   @Column(name = "CUST_ID")
	   private String custId;
	   @Id
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
	   @Column(name = "ENTITY_FLG", length = 1)
	   private String entityFlg;

	   @Column(name = "MODIFY_FLG", length = 1)
	   private String modifyFlg;

	   @Column(name = "DEL_FLG", length = 1)
	   private String delFlg;
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




	public String getEntityFlg() {
		return entityFlg;
	}




	public void setEntityFlg(String entityFlg) {
		this.entityFlg = entityFlg;
	}




	public String getModifyFlg() {
		return modifyFlg;
	}




	public void setModifyFlg(String modifyFlg) {
		this.modifyFlg = modifyFlg;
	}




	public String getDelFlg() {
		return delFlg;
	}




	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

}


// ARCHIVAL DETAIL 

public class TIER_1_2_CFS_Archival_Detail_RowMapper
        implements RowMapper<TIER_1_2_CFS_Archival_Detail_Entity> {

    @Override
    public TIER_1_2_CFS_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        TIER_1_2_CFS_Archival_Detail_Entity obj =
                new TIER_1_2_CFS_Archival_Detail_Entity();

        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));
        obj.setDataType(rs.getString("DATA_TYPE"));
        obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));

        //  keep DB column as-is
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
		
 // char fields (handle null safely)
        obj.setEntityFlg(rs.getString("ENTITY_FLG"));
        obj.setModifyFlg(rs.getString("MODIFY_FLG"));
        obj.setDelFlg(rs.getString("DEL_FLG"));
        return obj;
    }
}

public class TIER_1_2_CFS_Archival_Detail_Entity {
	
	   @Column(name = "CUST_ID")
	   private String custId;
	   @Id
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
	   @Column(name = "ENTITY_FLG", length = 1)
	   private String entityFlg;

	   @Column(name = "MODIFY_FLG", length = 1)
	   private String modifyFlg;

	   @Column(name = "DEL_FLG", length = 1)
	   private String delFlg;
	  
	   
	   
	   
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




	public String getEntityFlg() {
		return entityFlg;
	}




	public void setEntityFlg(String entityFlg) {
		this.entityFlg = entityFlg;
	}




	public String getModifyFlg() {
		return modifyFlg;
	}




	public void setModifyFlg(String modifyFlg) {
		this.modifyFlg = modifyFlg;
	}




	public String getDelFlg() {
		return delFlg;
	}




	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}


	
}

//=====================================================
// MODEL AND VIEW METHOD summary
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
		 		 	 public ModelAndView getTIER_1_2_CFSView(

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

	        List<TIER_1_2_CFS_Archival_Summary_Entity> T1Master = new ArrayList<>();
	    

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

	        List<TIER_1_2_CFS_Summary_Entity> T1Master = new ArrayList<>();
	       

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

	    mv.setViewName("BRRS/TIER_1_2_CFS");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
	//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getTIER_1_2_CFScurrentDtl(
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

	            List<TIER_1_2_CFS_Archival_Detail_Entity> archivalDetailList;

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

	            List<TIER_1_2_CFS_Detail_Entity> currentDetailList;

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

	    mv.setViewName("BRRS/TIER_1_2_CFS");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
	
	//=====================================================
// MODEL AND VIEW METHOD ARCHIVAL VIEW
//=====================================================

public List<Object[]> getTIER_1_2_CFSArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<TIER_1_2_CFS_Archival_Summary_Entity> repoData =
						getarchivaldatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (TIER_1_2_CFS_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					TIER_1_2_CFS_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  TIER_1_2_CFS  Archival data: " + e.getMessage());
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
		ModelAndView mv = new ModelAndView("BRRS/TIER_1_2_CFS"); 

		if (acctNo != null) {
			TIER_1_2_CFS_Detail_Entity TIER_1_2_CFSEntity = findByDetailAcctnumber(acctNo);
			if (TIER_1_2_CFSEntity != null && TIER_1_2_CFSEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(TIER_1_2_CFSEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("d_taxData", TIER_1_2_CFSEntity);
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

			TIER_1_2_CFS_Detail_Entity existing = findByDetailAcctnumber(acctNo);
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
							logger.info("Transaction committed — calling BRRS_TIER_1_2_CFS_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_TIER_1_2_CFS_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating TIER_1_2_CFS record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
	
	//=====================================================
// DETAIL EXCEL 
//=====================================================


	public byte[] getTIER_1_2_CFSDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  TIER_1_2_CFS  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getTIER_1_2_CFSDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("TIER_1_2_CFS Details ");

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
				String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE", "REPORT ADDL CRITERIA1",
"REPORT_DATE"
};

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
				List<TIER_1_2_CFS_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (TIER_1_2_CFS_Detail_Entity item : reportData) { 
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
						 if (j != 3  ) {
							row.getCell(j).setCellStyle(dataStyle);
						}
						}
					}
				} else {
					logger.info("No data found for TIER_1_2_CFS — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating TIER_1_2_CFS Excel", e);
				return new byte[0];
			}
		}
		





//=====================================================
// ARCHIVAL DETAIL EXCEL 
//=====================================================

	public byte[] getTIER_1_2_CFSDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for DTAX ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("TIER_1_2_CFS Detail NEW");

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
					String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
				List<TIER_1_2_CFS_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (TIER_1_2_CFS_Archival_Detail_Entity item : reportData) {
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
						 if (j != 3 ) {
							row.getCell(j).setCellStyle(dataStyle);
						}
						}
					}
				} else {
					logger.info("No data found for TIER_1_2_CFS — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating TIER_1_2_CFS NEW Excel", e);
				return new byte[0];
			}
		}
		
		
//=====================================================
// Summary EXCEL 
//=====================================================

	public byte[] getTIER_1_2_CFSExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.DTAX");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelTIER_1_2_CFSARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<TIER_1_2_CFS_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		
		
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

						int startRow = 9;
						
				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						TIER_1_2_CFS_Summary_Entity record = dataList.get(i);
					
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

Cell cell1 = row.createCell(8);
if (record.getR10_VALUE_IN_I_COLUMN() != null) {
    cell1.setCellValue(record.getR10_VALUE_IN_I_COLUMN().doubleValue());
    cell1.setCellStyle(numberStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}

Cell cell2 = row.createCell(9);
if (record.getR10_BWP_AMOUNT() != null) {
    cell2.setCellValue(record.getR10_BWP_AMOUNT().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

row = sheet.getRow(10);
 cell1 = row.createCell(8);
if (record.getR11_VALUE_IN_I_COLUMN() != null) {
    cell1.setCellValue(record.getR11_VALUE_IN_I_COLUMN().doubleValue());
    cell1.setCellStyle(numberStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR11_BWP_AMOUNT() != null) {
   cell2.setCellValue(record.getR11_BWP_AMOUNT().doubleValue());
   cell2.setCellStyle(numberStyle);
} else {
   cell2.setCellValue("");
   cell2.setCellStyle(textStyle);
}
row = sheet.getRow(11);
cell1 = row.createCell(8);
if (record.getR12_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR12_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR12_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR12_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(12);
cell1 = row.createCell(8);
if (record.getR13_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR13_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}

row = sheet.getRow(15);
cell1 = row.createCell(8);
if (record.getR16_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR16_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR16_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR16_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(16);
cell1 = row.createCell(8);
if (record.getR17_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR17_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR17_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR17_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(17);
cell1 = row.createCell(8);
if (record.getR18_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR18_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR18_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR18_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(18);
cell1 = row.createCell(8);
if (record.getR19_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR19_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR19_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR19_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(19);
cell1 = row.createCell(8);
if (record.getR20_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR20_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
row = sheet.getRow(25);
cell1 = row.createCell(8);
if (record.getR26_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR26_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR26_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR26_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(26);
cell1 = row.createCell(8);
if (record.getR27_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR27_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR27_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR27_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(27);
cell1 = row.createCell(8);
if (record.getR28_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR28_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR28_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR28_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(28);
cell1 = row.createCell(8);
if (record.getR29_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR29_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR29_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR29_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(29);
cell1 = row.createCell(8);
if (record.getR30_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR30_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR30_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR30_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(30);
cell1 = row.createCell(8);
if (record.getR31_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR31_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR31_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR31_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(31);
cell1 = row.createCell(8);
if (record.getR32_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR32_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR32_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR32_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(32);
cell1 = row.createCell(8);
if (record.getR33_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR33_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR33_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR33_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(51);
cell1 = row.createCell(6);
if (record.getR52_DOMESTIC_RISK_ASSETS() != null) {
   cell1.setCellValue(record.getR52_DOMESTIC_RISK_ASSETS().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(7);
if (record.getR52_FOREIGN_RISK_ASSETS() != null) {
  cell2.setCellValue(record.getR52_FOREIGN_RISK_ASSETS().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(52);
cell1 = row.createCell(6);
if (record.getR53_DOMESTIC_RISK_ASSETS() != null) {
   cell1.setCellValue(record.getR53_DOMESTIC_RISK_ASSETS().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(7);
if (record.getR53_FOREIGN_RISK_ASSETS() != null) {
  cell2.setCellValue(record.getR53_FOREIGN_RISK_ASSETS().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
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



				public byte[] getExcelTIER_1_2_CFSARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {

			}

			List<TIER_1_2_CFS_Archival_Summary_Entity> dataList = 
					getDataByDateListArchival(dateformat.parse(todate), version);
		
	    

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for TIER_1_2_CFS new report. Returning empty result.");
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
						TIER_1_2_CFS_Archival_Summary_Entity record = dataList.get(i);
					    
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

		
Cell cell1 = row.createCell(8);
if (record.getR10_VALUE_IN_I_COLUMN() != null) {
    cell1.setCellValue(record.getR10_VALUE_IN_I_COLUMN().doubleValue());
    cell1.setCellStyle(numberStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}

Cell cell2 = row.createCell(9);
if (record.getR10_BWP_AMOUNT() != null) {
    cell2.setCellValue(record.getR10_BWP_AMOUNT().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

row = sheet.getRow(10);
 cell1 = row.createCell(8);
if (record.getR11_VALUE_IN_I_COLUMN() != null) {
    cell1.setCellValue(record.getR11_VALUE_IN_I_COLUMN().doubleValue());
    cell1.setCellStyle(numberStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR11_BWP_AMOUNT() != null) {
   cell2.setCellValue(record.getR11_BWP_AMOUNT().doubleValue());
   cell2.setCellStyle(numberStyle);
} else {
   cell2.setCellValue("");
   cell2.setCellStyle(textStyle);
}
row = sheet.getRow(11);
cell1 = row.createCell(8);
if (record.getR12_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR12_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR12_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR12_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(12);
cell1 = row.createCell(8);
if (record.getR13_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR13_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}

row = sheet.getRow(15);
cell1 = row.createCell(8);
if (record.getR16_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR16_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR16_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR16_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(16);
cell1 = row.createCell(8);
if (record.getR17_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR17_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR17_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR17_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(17);
cell1 = row.createCell(8);
if (record.getR18_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR18_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR18_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR18_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(18);
cell1 = row.createCell(8);
if (record.getR19_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR19_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR19_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR19_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(19);
cell1 = row.createCell(8);
if (record.getR20_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR20_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
row = sheet.getRow(25);
cell1 = row.createCell(8);
if (record.getR26_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR26_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR26_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR26_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(26);
cell1 = row.createCell(8);
if (record.getR27_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR27_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR27_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR27_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(27);
cell1 = row.createCell(8);
if (record.getR28_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR28_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR28_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR28_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(28);
cell1 = row.createCell(8);
if (record.getR29_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR29_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR29_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR29_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(29);
cell1 = row.createCell(8);
if (record.getR30_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR30_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR30_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR30_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(30);
cell1 = row.createCell(8);
if (record.getR31_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR31_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR31_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR31_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(31);
cell1 = row.createCell(8);
if (record.getR32_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR32_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR32_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR32_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(32);
cell1 = row.createCell(8);
if (record.getR33_VALUE_IN_I_COLUMN() != null) {
   cell1.setCellValue(record.getR33_VALUE_IN_I_COLUMN().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(9);
if (record.getR33_BWP_AMOUNT() != null) {
  cell2.setCellValue(record.getR33_BWP_AMOUNT().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(51);
cell1 = row.createCell(6);
if (record.getR52_DOMESTIC_RISK_ASSETS() != null) {
   cell1.setCellValue(record.getR52_DOMESTIC_RISK_ASSETS().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(7);
if (record.getR52_FOREIGN_RISK_ASSETS() != null) {
  cell2.setCellValue(record.getR52_FOREIGN_RISK_ASSETS().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}
row = sheet.getRow(52);
cell1 = row.createCell(6);
if (record.getR53_DOMESTIC_RISK_ASSETS() != null) {
   cell1.setCellValue(record.getR53_DOMESTIC_RISK_ASSETS().doubleValue());
   cell1.setCellStyle(numberStyle);
} else {
   cell1.setCellValue("");
   cell1.setCellStyle(textStyle);
}
cell2 = row.createCell(7);
if (record.getR53_FOREIGN_RISK_ASSETS() != null) {
  cell2.setCellValue(record.getR53_FOREIGN_RISK_ASSETS().doubleValue());
  cell2.setCellStyle(numberStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
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