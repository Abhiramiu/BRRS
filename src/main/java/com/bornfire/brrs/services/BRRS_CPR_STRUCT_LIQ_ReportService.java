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
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;


@Service

public class BRRS_CPR_STRUCT_LIQ_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_CPR_STRUCT_LIQ_ReportService.class);
	
	
	@Autowired
	private Environment env;
	
	@Autowired
	AuditService auditService;

	@Autowired
	SessionFactory sessionFactory;

  
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;
	

// =====================================================
// SUMAMRY REPO
// =====================================================


	public List<CPR_STRUCT_LIQ_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_CPR_STRUCT_LIQ_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new CPR_STRUCT_LIQ_Summary_RowMapper()
    );
}
	
// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================



public List<Object[]> getCPR_STRUCT_LIQ_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_CPR_STRUCT_LIQ_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<CPR_STRUCT_LIQ_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_CPR_STRUCT_LIQ_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new CPR_STRUCT_LIQ_Archival_Summary_RowMapper()
    );
}

public List<CPR_STRUCT_LIQ_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_CPR_STRUCT_LIQ_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new CPR_STRUCT_LIQ_Archival_Summary_RowMapper()
    );
}

// =====================================================
// DETAIL REPO
// =====================================================	


public List<CPR_STRUCT_LIQ_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_CPR_STRUCT_LIQ_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new CPR_STRUCT_LIQ_Detail_RowMapper()
    );
}

public List<CPR_STRUCT_LIQ_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_CPR_STRUCT_LIQ_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, offset, limit},
            new CPR_STRUCT_LIQ_Detail_RowMapper()
    );
}

public int getDataCount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_CPR_STRUCT_LIQ_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(sql, new Object[]{reportDate}, Integer.class);
}

public List<CPR_STRUCT_LIQ_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel, String reportAddlCriteria1, Date reportDate) {

    String sql = "SELECT * FROM BRRS_CPR_STRUCT_LIQ_DETAILTABLE " +
                 "WHERE REPORT_LABEL = ? " +
                 "AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate},
            new CPR_STRUCT_LIQ_Detail_RowMapper()
    );
}

public CPR_STRUCT_LIQ_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_CPR_STRUCT_LIQ_DETAILTABLE WHERE ACCT_NUMBER = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{acctNumber},
            new CPR_STRUCT_LIQ_Detail_RowMapper()
    );
}


// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================

public List<CPR_STRUCT_LIQ_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate, String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_CPR_STRUCT_LIQ_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, dataEntryVersion},
            new CPR_STRUCT_LIQ_Archival_Detail_RowMapper()
    );
}


public List<CPR_STRUCT_LIQ_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_CPR_STRUCT_LIQ_ARCHIVALTABLE_DETAIL " +
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
            new CPR_STRUCT_LIQ_Archival_Detail_RowMapper()
    );
}

// =====================================================
// SUMAMRY ENTITY 
// =====================================================


public class CPR_STRUCT_LIQ_Summary_RowMapper implements RowMapper<CPR_STRUCT_LIQ_Summary_Entity> {

    @Override
    public CPR_STRUCT_LIQ_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        CPR_STRUCT_LIQ_Summary_Entity obj = new CPR_STRUCT_LIQ_Summary_Entity();

// =========================
// R8
// =========================
obj.setR8_1_DAY(rs.getBigDecimal("R8_1_DAY"));
obj.setR8_2TO7_DAYS(rs.getBigDecimal("R8_2TO7_DAYS"));
obj.setR8_8TO14_DAYS(rs.getBigDecimal("R8_8TO14_DAYS"));
obj.setR8_15TO30_DAYS(rs.getBigDecimal("R8_15TO30_DAYS"));
obj.setR8_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R8_31DAYS_UPTO_2MONTHS"));
obj.setR8_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R8_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR8_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R8_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR8_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R8_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR8_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R8_OVER_1YEAR_UPTO_3YEARS"));
obj.setR8_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R8_OVER_3YEARS_UPTO_5YEARS"));
obj.setR8_OVER_5YEARS(rs.getBigDecimal("R8_OVER_5YEARS"));
obj.setR8_TOTAL(rs.getBigDecimal("R8_TOTAL"));

// =========================
// R9
// =========================
obj.setR9_1_DAY(rs.getBigDecimal("R9_1_DAY"));
obj.setR9_2TO7_DAYS(rs.getBigDecimal("R9_2TO7_DAYS"));
obj.setR9_8TO14_DAYS(rs.getBigDecimal("R9_8TO14_DAYS"));
obj.setR9_15TO30_DAYS(rs.getBigDecimal("R9_15TO30_DAYS"));
obj.setR9_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R9_31DAYS_UPTO_2MONTHS"));
obj.setR9_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R9_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR9_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R9_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR9_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R9_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR9_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R9_OVER_1YEAR_UPTO_3YEARS"));
obj.setR9_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R9_OVER_3YEARS_UPTO_5YEARS"));
obj.setR9_OVER_5YEARS(rs.getBigDecimal("R9_OVER_5YEARS"));
obj.setR9_TOTAL(rs.getBigDecimal("R9_TOTAL"));

// =========================
// R10
// =========================
obj.setR10_1_DAY(rs.getBigDecimal("R10_1_DAY"));
obj.setR10_2TO7_DAYS(rs.getBigDecimal("R10_2TO7_DAYS"));
obj.setR10_8TO14_DAYS(rs.getBigDecimal("R10_8TO14_DAYS"));
obj.setR10_15TO30_DAYS(rs.getBigDecimal("R10_15TO30_DAYS"));
obj.setR10_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R10_31DAYS_UPTO_2MONTHS"));
obj.setR10_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R10_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR10_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R10_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR10_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R10_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR10_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R10_OVER_1YEAR_UPTO_3YEARS"));
obj.setR10_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R10_OVER_3YEARS_UPTO_5YEARS"));
obj.setR10_OVER_5YEARS(rs.getBigDecimal("R10_OVER_5YEARS"));
obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

// =========================
// R11
// =========================
obj.setR11_1_DAY(rs.getBigDecimal("R11_1_DAY"));
obj.setR11_2TO7_DAYS(rs.getBigDecimal("R11_2TO7_DAYS"));
obj.setR11_8TO14_DAYS(rs.getBigDecimal("R11_8TO14_DAYS"));
obj.setR11_15TO30_DAYS(rs.getBigDecimal("R11_15TO30_DAYS"));
obj.setR11_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R11_31DAYS_UPTO_2MONTHS"));
obj.setR11_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R11_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR11_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R11_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR11_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R11_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR11_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R11_OVER_1YEAR_UPTO_3YEARS"));
obj.setR11_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R11_OVER_3YEARS_UPTO_5YEARS"));
obj.setR11_OVER_5YEARS(rs.getBigDecimal("R11_OVER_5YEARS"));
obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

// =========================
// R12
// =========================
obj.setR12_1_DAY(rs.getBigDecimal("R12_1_DAY"));
obj.setR12_2TO7_DAYS(rs.getBigDecimal("R12_2TO7_DAYS"));
obj.setR12_8TO14_DAYS(rs.getBigDecimal("R12_8TO14_DAYS"));
obj.setR12_15TO30_DAYS(rs.getBigDecimal("R12_15TO30_DAYS"));
obj.setR12_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R12_31DAYS_UPTO_2MONTHS"));
obj.setR12_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R12_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR12_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R12_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR12_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R12_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR12_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R12_OVER_1YEAR_UPTO_3YEARS"));
obj.setR12_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R12_OVER_3YEARS_UPTO_5YEARS"));
obj.setR12_OVER_5YEARS(rs.getBigDecimal("R12_OVER_5YEARS"));
obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));


// =========================
// R13
// =========================
obj.setR13_1_DAY(rs.getBigDecimal("R13_1_DAY"));
obj.setR13_2TO7_DAYS(rs.getBigDecimal("R13_2TO7_DAYS"));
obj.setR13_8TO14_DAYS(rs.getBigDecimal("R13_8TO14_DAYS"));
obj.setR13_15TO30_DAYS(rs.getBigDecimal("R13_15TO30_DAYS"));
obj.setR13_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R13_31DAYS_UPTO_2MONTHS"));
obj.setR13_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R13_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR13_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R13_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR13_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R13_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR13_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R13_OVER_1YEAR_UPTO_3YEARS"));
obj.setR13_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R13_OVER_3YEARS_UPTO_5YEARS"));
obj.setR13_OVER_5YEARS(rs.getBigDecimal("R13_OVER_5YEARS"));
obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

// =========================
// R14
// =========================
obj.setR14_1_DAY(rs.getBigDecimal("R14_1_DAY"));
obj.setR14_2TO7_DAYS(rs.getBigDecimal("R14_2TO7_DAYS"));
obj.setR14_8TO14_DAYS(rs.getBigDecimal("R14_8TO14_DAYS"));
obj.setR14_15TO30_DAYS(rs.getBigDecimal("R14_15TO30_DAYS"));
obj.setR14_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R14_31DAYS_UPTO_2MONTHS"));
obj.setR14_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R14_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR14_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R14_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR14_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R14_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR14_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R14_OVER_1YEAR_UPTO_3YEARS"));
obj.setR14_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R14_OVER_3YEARS_UPTO_5YEARS"));
obj.setR14_OVER_5YEARS(rs.getBigDecimal("R14_OVER_5YEARS"));
obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

// =========================
// R15
// =========================
obj.setR15_1_DAY(rs.getBigDecimal("R15_1_DAY"));
obj.setR15_2TO7_DAYS(rs.getBigDecimal("R15_2TO7_DAYS"));
obj.setR15_8TO14_DAYS(rs.getBigDecimal("R15_8TO14_DAYS"));
obj.setR15_15TO30_DAYS(rs.getBigDecimal("R15_15TO30_DAYS"));
obj.setR15_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R15_31DAYS_UPTO_2MONTHS"));
obj.setR15_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R15_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR15_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R15_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR15_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R15_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR15_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R15_OVER_1YEAR_UPTO_3YEARS"));
obj.setR15_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R15_OVER_3YEARS_UPTO_5YEARS"));
obj.setR15_OVER_5YEARS(rs.getBigDecimal("R15_OVER_5YEARS"));
obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));


// =========================
// R16
// =========================
obj.setR16_1_DAY(rs.getBigDecimal("R16_1_DAY"));
obj.setR16_2TO7_DAYS(rs.getBigDecimal("R16_2TO7_DAYS"));
obj.setR16_8TO14_DAYS(rs.getBigDecimal("R16_8TO14_DAYS"));
obj.setR16_15TO30_DAYS(rs.getBigDecimal("R16_15TO30_DAYS"));
obj.setR16_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R16_31DAYS_UPTO_2MONTHS"));
obj.setR16_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R16_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR16_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R16_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR16_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R16_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR16_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R16_OVER_1YEAR_UPTO_3YEARS"));
obj.setR16_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R16_OVER_3YEARS_UPTO_5YEARS"));
obj.setR16_OVER_5YEARS(rs.getBigDecimal("R16_OVER_5YEARS"));
obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

// =========================
// R17
// =========================
obj.setR17_1_DAY(rs.getBigDecimal("R17_1_DAY"));
obj.setR17_2TO7_DAYS(rs.getBigDecimal("R17_2TO7_DAYS"));
obj.setR17_8TO14_DAYS(rs.getBigDecimal("R17_8TO14_DAYS"));
obj.setR17_15TO30_DAYS(rs.getBigDecimal("R17_15TO30_DAYS"));
obj.setR17_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R17_31DAYS_UPTO_2MONTHS"));
obj.setR17_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R17_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR17_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R17_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR17_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R17_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR17_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R17_OVER_1YEAR_UPTO_3YEARS"));
obj.setR17_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R17_OVER_3YEARS_UPTO_5YEARS"));
obj.setR17_OVER_5YEARS(rs.getBigDecimal("R17_OVER_5YEARS"));
obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

// =========================
// R18
// =========================
obj.setR18_1_DAY(rs.getBigDecimal("R18_1_DAY"));
obj.setR18_2TO7_DAYS(rs.getBigDecimal("R18_2TO7_DAYS"));
obj.setR18_8TO14_DAYS(rs.getBigDecimal("R18_8TO14_DAYS"));
obj.setR18_15TO30_DAYS(rs.getBigDecimal("R18_15TO30_DAYS"));
obj.setR18_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R18_31DAYS_UPTO_2MONTHS"));
obj.setR18_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R18_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR18_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R18_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR18_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R18_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR18_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R18_OVER_1YEAR_UPTO_3YEARS"));
obj.setR18_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R18_OVER_3YEARS_UPTO_5YEARS"));
obj.setR18_OVER_5YEARS(rs.getBigDecimal("R18_OVER_5YEARS"));
obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

// =========================
// R19
// =========================
obj.setR19_1_DAY(rs.getBigDecimal("R19_1_DAY"));
obj.setR19_2TO7_DAYS(rs.getBigDecimal("R19_2TO7_DAYS"));
obj.setR19_8TO14_DAYS(rs.getBigDecimal("R19_8TO14_DAYS"));
obj.setR19_15TO30_DAYS(rs.getBigDecimal("R19_15TO30_DAYS"));
obj.setR19_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R19_31DAYS_UPTO_2MONTHS"));
obj.setR19_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R19_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR19_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R19_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR19_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R19_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR19_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R19_OVER_1YEAR_UPTO_3YEARS"));
obj.setR19_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R19_OVER_3YEARS_UPTO_5YEARS"));
obj.setR19_OVER_5YEARS(rs.getBigDecimal("R19_OVER_5YEARS"));
obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

// =========================
// R20
// =========================
obj.setR20_1_DAY(rs.getBigDecimal("R20_1_DAY"));
obj.setR20_2TO7_DAYS(rs.getBigDecimal("R20_2TO7_DAYS"));
obj.setR20_8TO14_DAYS(rs.getBigDecimal("R20_8TO14_DAYS"));
obj.setR20_15TO30_DAYS(rs.getBigDecimal("R20_15TO30_DAYS"));
obj.setR20_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R20_31DAYS_UPTO_2MONTHS"));
obj.setR20_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R20_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR20_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R20_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR20_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R20_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR20_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R20_OVER_1YEAR_UPTO_3YEARS"));
obj.setR20_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R20_OVER_3YEARS_UPTO_5YEARS"));
obj.setR20_OVER_5YEARS(rs.getBigDecimal("R20_OVER_5YEARS"));
obj.setR20_TOTAL(rs.getBigDecimal("R20_TOTAL"));



// =========================
// R21
// =========================
obj.setR21_1_DAY(rs.getBigDecimal("R21_1_DAY"));
obj.setR21_2TO7_DAYS(rs.getBigDecimal("R21_2TO7_DAYS"));
obj.setR21_8TO14_DAYS(rs.getBigDecimal("R21_8TO14_DAYS"));
obj.setR21_15TO30_DAYS(rs.getBigDecimal("R21_15TO30_DAYS"));
obj.setR21_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R21_31DAYS_UPTO_2MONTHS"));
obj.setR21_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R21_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR21_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R21_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR21_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R21_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR21_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R21_OVER_1YEAR_UPTO_3YEARS"));
obj.setR21_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R21_OVER_3YEARS_UPTO_5YEARS"));
obj.setR21_OVER_5YEARS(rs.getBigDecimal("R21_OVER_5YEARS"));
obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));


// =========================
// R22
// =========================
obj.setR22_1_DAY(rs.getBigDecimal("R22_1_DAY"));
obj.setR22_2TO7_DAYS(rs.getBigDecimal("R22_2TO7_DAYS"));
obj.setR22_8TO14_DAYS(rs.getBigDecimal("R22_8TO14_DAYS"));
obj.setR22_15TO30_DAYS(rs.getBigDecimal("R22_15TO30_DAYS"));
obj.setR22_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R22_31DAYS_UPTO_2MONTHS"));
obj.setR22_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R22_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR22_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R22_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR22_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R22_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR22_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R22_OVER_1YEAR_UPTO_3YEARS"));
obj.setR22_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R22_OVER_3YEARS_UPTO_5YEARS"));
obj.setR22_OVER_5YEARS(rs.getBigDecimal("R22_OVER_5YEARS"));
obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

// =========================
// R23
// =========================
obj.setR23_1_DAY(rs.getBigDecimal("R23_1_DAY"));
obj.setR23_2TO7_DAYS(rs.getBigDecimal("R23_2TO7_DAYS"));
obj.setR23_8TO14_DAYS(rs.getBigDecimal("R23_8TO14_DAYS"));
obj.setR23_15TO30_DAYS(rs.getBigDecimal("R23_15TO30_DAYS"));
obj.setR23_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R23_31DAYS_UPTO_2MONTHS"));
obj.setR23_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R23_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR23_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R23_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR23_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R23_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR23_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R23_OVER_1YEAR_UPTO_3YEARS"));
obj.setR23_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R23_OVER_3YEARS_UPTO_5YEARS"));
obj.setR23_OVER_5YEARS(rs.getBigDecimal("R23_OVER_5YEARS"));
obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));


// =========================
// R24
// =========================
obj.setR24_1_DAY(rs.getBigDecimal("R24_1_DAY"));
obj.setR24_2TO7_DAYS(rs.getBigDecimal("R24_2TO7_DAYS"));
obj.setR24_8TO14_DAYS(rs.getBigDecimal("R24_8TO14_DAYS"));
obj.setR24_15TO30_DAYS(rs.getBigDecimal("R24_15TO30_DAYS"));
obj.setR24_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R24_31DAYS_UPTO_2MONTHS"));
obj.setR24_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R24_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR24_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R24_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR24_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R24_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR24_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R24_OVER_1YEAR_UPTO_3YEARS"));
obj.setR24_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R24_OVER_3YEARS_UPTO_5YEARS"));
obj.setR24_OVER_5YEARS(rs.getBigDecimal("R24_OVER_5YEARS"));
obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

// =========================
// R25
// =========================
obj.setR25_1_DAY(rs.getBigDecimal("R25_1_DAY"));
obj.setR25_2TO7_DAYS(rs.getBigDecimal("R25_2TO7_DAYS"));
obj.setR25_8TO14_DAYS(rs.getBigDecimal("R25_8TO14_DAYS"));
obj.setR25_15TO30_DAYS(rs.getBigDecimal("R25_15TO30_DAYS"));
obj.setR25_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R25_31DAYS_UPTO_2MONTHS"));
obj.setR25_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R25_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR25_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R25_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR25_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R25_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR25_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R25_OVER_1YEAR_UPTO_3YEARS"));
obj.setR25_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R25_OVER_3YEARS_UPTO_5YEARS"));
obj.setR25_OVER_5YEARS(rs.getBigDecimal("R25_OVER_5YEARS"));
obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));


// =========================
// R26
// =========================
obj.setR26_1_DAY(rs.getBigDecimal("R26_1_DAY"));
obj.setR26_2TO7_DAYS(rs.getBigDecimal("R26_2TO7_DAYS"));
obj.setR26_8TO14_DAYS(rs.getBigDecimal("R26_8TO14_DAYS"));
obj.setR26_15TO30_DAYS(rs.getBigDecimal("R26_15TO30_DAYS"));
obj.setR26_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R26_31DAYS_UPTO_2MONTHS"));
obj.setR26_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R26_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR26_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R26_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR26_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R26_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR26_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R26_OVER_1YEAR_UPTO_3YEARS"));
obj.setR26_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R26_OVER_3YEARS_UPTO_5YEARS"));
obj.setR26_OVER_5YEARS(rs.getBigDecimal("R26_OVER_5YEARS"));
obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

// =========================
// R27
// =========================
obj.setR27_1_DAY(rs.getBigDecimal("R27_1_DAY"));
obj.setR27_2TO7_DAYS(rs.getBigDecimal("R27_2TO7_DAYS"));
obj.setR27_8TO14_DAYS(rs.getBigDecimal("R27_8TO14_DAYS"));
obj.setR27_15TO30_DAYS(rs.getBigDecimal("R27_15TO30_DAYS"));
obj.setR27_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R27_31DAYS_UPTO_2MONTHS"));
obj.setR27_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R27_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR27_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R27_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR27_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R27_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR27_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R27_OVER_1YEAR_UPTO_3YEARS"));
obj.setR27_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R27_OVER_3YEARS_UPTO_5YEARS"));
obj.setR27_OVER_5YEARS(rs.getBigDecimal("R27_OVER_5YEARS"));
obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));

// =========================
// R28
// =========================
obj.setR28_1_DAY(rs.getBigDecimal("R28_1_DAY"));
obj.setR28_2TO7_DAYS(rs.getBigDecimal("R28_2TO7_DAYS"));
obj.setR28_8TO14_DAYS(rs.getBigDecimal("R28_8TO14_DAYS"));
obj.setR28_15TO30_DAYS(rs.getBigDecimal("R28_15TO30_DAYS"));
obj.setR28_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R28_31DAYS_UPTO_2MONTHS"));
obj.setR28_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R28_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR28_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R28_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR28_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R28_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR28_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R28_OVER_1YEAR_UPTO_3YEARS"));
obj.setR28_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R28_OVER_3YEARS_UPTO_5YEARS"));
obj.setR28_OVER_5YEARS(rs.getBigDecimal("R28_OVER_5YEARS"));
obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));

// =========================
// R29
// =========================
obj.setR29_1_DAY(rs.getBigDecimal("R29_1_DAY"));
obj.setR29_2TO7_DAYS(rs.getBigDecimal("R29_2TO7_DAYS"));
obj.setR29_8TO14_DAYS(rs.getBigDecimal("R29_8TO14_DAYS"));
obj.setR29_15TO30_DAYS(rs.getBigDecimal("R29_15TO30_DAYS"));
obj.setR29_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R29_31DAYS_UPTO_2MONTHS"));
obj.setR29_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R29_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR29_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R29_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR29_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R29_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR29_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R29_OVER_1YEAR_UPTO_3YEARS"));
obj.setR29_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R29_OVER_3YEARS_UPTO_5YEARS"));
obj.setR29_OVER_5YEARS(rs.getBigDecimal("R29_OVER_5YEARS"));
obj.setR29_TOTAL(rs.getBigDecimal("R29_TOTAL"));

// =========================
// R30
// =========================
obj.setR30_1_DAY(rs.getBigDecimal("R30_1_DAY"));
obj.setR30_2TO7_DAYS(rs.getBigDecimal("R30_2TO7_DAYS"));
obj.setR30_8TO14_DAYS(rs.getBigDecimal("R30_8TO14_DAYS"));
obj.setR30_15TO30_DAYS(rs.getBigDecimal("R30_15TO30_DAYS"));
obj.setR30_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R30_31DAYS_UPTO_2MONTHS"));
obj.setR30_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R30_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR30_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R30_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR30_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R30_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR30_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R30_OVER_1YEAR_UPTO_3YEARS"));
obj.setR30_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R30_OVER_3YEARS_UPTO_5YEARS"));
obj.setR30_OVER_5YEARS(rs.getBigDecimal("R30_OVER_5YEARS"));
obj.setR30_TOTAL(rs.getBigDecimal("R30_TOTAL"));

// =========================
// R31
// =========================
obj.setR31_1_DAY(rs.getBigDecimal("R31_1_DAY"));
obj.setR31_2TO7_DAYS(rs.getBigDecimal("R31_2TO7_DAYS"));
obj.setR31_8TO14_DAYS(rs.getBigDecimal("R31_8TO14_DAYS"));
obj.setR31_15TO30_DAYS(rs.getBigDecimal("R31_15TO30_DAYS"));
obj.setR31_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R31_31DAYS_UPTO_2MONTHS"));
obj.setR31_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R31_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR31_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R31_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR31_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R31_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR31_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R31_OVER_1YEAR_UPTO_3YEARS"));
obj.setR31_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R31_OVER_3YEARS_UPTO_5YEARS"));
obj.setR31_OVER_5YEARS(rs.getBigDecimal("R31_OVER_5YEARS"));
obj.setR31_TOTAL(rs.getBigDecimal("R31_TOTAL"));

// =========================
// R32
// =========================
obj.setR32_1_DAY(rs.getBigDecimal("R32_1_DAY"));
obj.setR32_2TO7_DAYS(rs.getBigDecimal("R32_2TO7_DAYS"));
obj.setR32_8TO14_DAYS(rs.getBigDecimal("R32_8TO14_DAYS"));
obj.setR32_15TO30_DAYS(rs.getBigDecimal("R32_15TO30_DAYS"));
obj.setR32_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R32_31DAYS_UPTO_2MONTHS"));
obj.setR32_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R32_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR32_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R32_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR32_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R32_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR32_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R32_OVER_1YEAR_UPTO_3YEARS"));
obj.setR32_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R32_OVER_3YEARS_UPTO_5YEARS"));
obj.setR32_OVER_5YEARS(rs.getBigDecimal("R32_OVER_5YEARS"));
obj.setR32_TOTAL(rs.getBigDecimal("R32_TOTAL"));

// =========================
// R33
// =========================
obj.setR33_1_DAY(rs.getBigDecimal("R33_1_DAY"));
obj.setR33_2TO7_DAYS(rs.getBigDecimal("R33_2TO7_DAYS"));
obj.setR33_8TO14_DAYS(rs.getBigDecimal("R33_8TO14_DAYS"));
obj.setR33_15TO30_DAYS(rs.getBigDecimal("R33_15TO30_DAYS"));
obj.setR33_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R33_31DAYS_UPTO_2MONTHS"));
obj.setR33_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R33_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR33_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R33_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR33_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R33_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR33_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R33_OVER_1YEAR_UPTO_3YEARS"));
obj.setR33_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R33_OVER_3YEARS_UPTO_5YEARS"));
obj.setR33_OVER_5YEARS(rs.getBigDecimal("R33_OVER_5YEARS"));
obj.setR33_TOTAL(rs.getBigDecimal("R33_TOTAL"));

// =========================
// R34
// =========================
obj.setR34_1_DAY(rs.getBigDecimal("R34_1_DAY"));
obj.setR34_2TO7_DAYS(rs.getBigDecimal("R34_2TO7_DAYS"));
obj.setR34_8TO14_DAYS(rs.getBigDecimal("R34_8TO14_DAYS"));
obj.setR34_15TO30_DAYS(rs.getBigDecimal("R34_15TO30_DAYS"));
obj.setR34_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R34_31DAYS_UPTO_2MONTHS"));
obj.setR34_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R34_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR34_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R34_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR34_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R34_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR34_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R34_OVER_1YEAR_UPTO_3YEARS"));
obj.setR34_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R34_OVER_3YEARS_UPTO_5YEARS"));
obj.setR34_OVER_5YEARS(rs.getBigDecimal("R34_OVER_5YEARS"));
obj.setR34_TOTAL(rs.getBigDecimal("R34_TOTAL"));

// =========================
// R35
// =========================
obj.setR35_1_DAY(rs.getBigDecimal("R35_1_DAY"));
obj.setR35_2TO7_DAYS(rs.getBigDecimal("R35_2TO7_DAYS"));
obj.setR35_8TO14_DAYS(rs.getBigDecimal("R35_8TO14_DAYS"));
obj.setR35_15TO30_DAYS(rs.getBigDecimal("R35_15TO30_DAYS"));
obj.setR35_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R35_31DAYS_UPTO_2MONTHS"));
obj.setR35_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R35_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR35_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R35_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR35_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R35_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR35_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R35_OVER_1YEAR_UPTO_3YEARS"));
obj.setR35_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R35_OVER_3YEARS_UPTO_5YEARS"));
obj.setR35_OVER_5YEARS(rs.getBigDecimal("R35_OVER_5YEARS"));
obj.setR35_TOTAL(rs.getBigDecimal("R35_TOTAL"));


// =========================
// R36
// =========================
obj.setR36_1_DAY(rs.getBigDecimal("R36_1_DAY"));
obj.setR36_2TO7_DAYS(rs.getBigDecimal("R36_2TO7_DAYS"));
obj.setR36_8TO14_DAYS(rs.getBigDecimal("R36_8TO14_DAYS"));
obj.setR36_15TO30_DAYS(rs.getBigDecimal("R36_15TO30_DAYS"));
obj.setR36_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R36_31DAYS_UPTO_2MONTHS"));
obj.setR36_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R36_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR36_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R36_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR36_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R36_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR36_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R36_OVER_1YEAR_UPTO_3YEARS"));
obj.setR36_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R36_OVER_3YEARS_UPTO_5YEARS"));
obj.setR36_OVER_5YEARS(rs.getBigDecimal("R36_OVER_5YEARS"));
obj.setR36_TOTAL(rs.getBigDecimal("R36_TOTAL"));

// =========================
// R37
// =========================
obj.setR37_1_DAY(rs.getBigDecimal("R37_1_DAY"));
obj.setR37_2TO7_DAYS(rs.getBigDecimal("R37_2TO7_DAYS"));
obj.setR37_8TO14_DAYS(rs.getBigDecimal("R37_8TO14_DAYS"));
obj.setR37_15TO30_DAYS(rs.getBigDecimal("R37_15TO30_DAYS"));
obj.setR37_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R37_31DAYS_UPTO_2MONTHS"));
obj.setR37_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R37_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR37_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R37_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR37_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R37_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR37_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R37_OVER_1YEAR_UPTO_3YEARS"));
obj.setR37_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R37_OVER_3YEARS_UPTO_5YEARS"));
obj.setR37_OVER_5YEARS(rs.getBigDecimal("R37_OVER_5YEARS"));
obj.setR37_TOTAL(rs.getBigDecimal("R37_TOTAL"));

// =========================
// R38
// =========================
obj.setR38_1_DAY(rs.getBigDecimal("R38_1_DAY"));
obj.setR38_2TO7_DAYS(rs.getBigDecimal("R38_2TO7_DAYS"));
obj.setR38_8TO14_DAYS(rs.getBigDecimal("R38_8TO14_DAYS"));
obj.setR38_15TO30_DAYS(rs.getBigDecimal("R38_15TO30_DAYS"));
obj.setR38_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R38_31DAYS_UPTO_2MONTHS"));
obj.setR38_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R38_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR38_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R38_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR38_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R38_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR38_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R38_OVER_1YEAR_UPTO_3YEARS"));
obj.setR38_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R38_OVER_3YEARS_UPTO_5YEARS"));
obj.setR38_OVER_5YEARS(rs.getBigDecimal("R38_OVER_5YEARS"));
obj.setR38_TOTAL(rs.getBigDecimal("R38_TOTAL"));

// =========================
// R39
// =========================
obj.setR39_1_DAY(rs.getBigDecimal("R39_1_DAY"));
obj.setR39_2TO7_DAYS(rs.getBigDecimal("R39_2TO7_DAYS"));
obj.setR39_8TO14_DAYS(rs.getBigDecimal("R39_8TO14_DAYS"));
obj.setR39_15TO30_DAYS(rs.getBigDecimal("R39_15TO30_DAYS"));
obj.setR39_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R39_31DAYS_UPTO_2MONTHS"));
obj.setR39_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R39_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR39_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R39_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR39_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R39_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR39_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R39_OVER_1YEAR_UPTO_3YEARS"));
obj.setR39_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R39_OVER_3YEARS_UPTO_5YEARS"));
obj.setR39_OVER_5YEARS(rs.getBigDecimal("R39_OVER_5YEARS"));
obj.setR39_TOTAL(rs.getBigDecimal("R39_TOTAL"));

// =========================
// R40
// =========================
obj.setR40_1_DAY(rs.getBigDecimal("R40_1_DAY"));
obj.setR40_2TO7_DAYS(rs.getBigDecimal("R40_2TO7_DAYS"));
obj.setR40_8TO14_DAYS(rs.getBigDecimal("R40_8TO14_DAYS"));
obj.setR40_15TO30_DAYS(rs.getBigDecimal("R40_15TO30_DAYS"));
obj.setR40_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R40_31DAYS_UPTO_2MONTHS"));
obj.setR40_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R40_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR40_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R40_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR40_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R40_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR40_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R40_OVER_1YEAR_UPTO_3YEARS"));
obj.setR40_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R40_OVER_3YEARS_UPTO_5YEARS"));
obj.setR40_OVER_5YEARS(rs.getBigDecimal("R40_OVER_5YEARS"));
obj.setR40_TOTAL(rs.getBigDecimal("R40_TOTAL"));

// =========================
// R41
// =========================
obj.setR41_1_DAY(rs.getBigDecimal("R41_1_DAY"));
obj.setR41_2TO7_DAYS(rs.getBigDecimal("R41_2TO7_DAYS"));
obj.setR41_8TO14_DAYS(rs.getBigDecimal("R41_8TO14_DAYS"));
obj.setR41_15TO30_DAYS(rs.getBigDecimal("R41_15TO30_DAYS"));
obj.setR41_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R41_31DAYS_UPTO_2MONTHS"));
obj.setR41_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R41_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR41_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R41_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR41_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R41_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR41_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R41_OVER_1YEAR_UPTO_3YEARS"));
obj.setR41_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R41_OVER_3YEARS_UPTO_5YEARS"));
obj.setR41_OVER_5YEARS(rs.getBigDecimal("R41_OVER_5YEARS"));
obj.setR41_TOTAL(rs.getBigDecimal("R41_TOTAL"));

// =========================
// R42
// =========================
obj.setR42_1_DAY(rs.getBigDecimal("R42_1_DAY"));
obj.setR42_2TO7_DAYS(rs.getBigDecimal("R42_2TO7_DAYS"));
obj.setR42_8TO14_DAYS(rs.getBigDecimal("R42_8TO14_DAYS"));
obj.setR42_15TO30_DAYS(rs.getBigDecimal("R42_15TO30_DAYS"));
obj.setR42_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R42_31DAYS_UPTO_2MONTHS"));
obj.setR42_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R42_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR42_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R42_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR42_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R42_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR42_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R42_OVER_1YEAR_UPTO_3YEARS"));
obj.setR42_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R42_OVER_3YEARS_UPTO_5YEARS"));
obj.setR42_OVER_5YEARS(rs.getBigDecimal("R42_OVER_5YEARS"));
obj.setR42_TOTAL(rs.getBigDecimal("R42_TOTAL"));

// =========================
// R43
// =========================
obj.setR43_1_DAY(rs.getBigDecimal("R43_1_DAY"));
obj.setR43_2TO7_DAYS(rs.getBigDecimal("R43_2TO7_DAYS"));
obj.setR43_8TO14_DAYS(rs.getBigDecimal("R43_8TO14_DAYS"));
obj.setR43_15TO30_DAYS(rs.getBigDecimal("R43_15TO30_DAYS"));
obj.setR43_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R43_31DAYS_UPTO_2MONTHS"));
obj.setR43_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R43_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR43_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R43_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR43_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R43_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR43_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R43_OVER_1YEAR_UPTO_3YEARS"));
obj.setR43_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R43_OVER_3YEARS_UPTO_5YEARS"));
obj.setR43_OVER_5YEARS(rs.getBigDecimal("R43_OVER_5YEARS"));
obj.setR43_TOTAL(rs.getBigDecimal("R43_TOTAL"));

// =========================
// R44
// =========================
obj.setR44_1_DAY(rs.getBigDecimal("R44_1_DAY"));
obj.setR44_2TO7_DAYS(rs.getBigDecimal("R44_2TO7_DAYS"));
obj.setR44_8TO14_DAYS(rs.getBigDecimal("R44_8TO14_DAYS"));
obj.setR44_15TO30_DAYS(rs.getBigDecimal("R44_15TO30_DAYS"));
obj.setR44_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R44_31DAYS_UPTO_2MONTHS"));
obj.setR44_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R44_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR44_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R44_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR44_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R44_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR44_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R44_OVER_1YEAR_UPTO_3YEARS"));
obj.setR44_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R44_OVER_3YEARS_UPTO_5YEARS"));
obj.setR44_OVER_5YEARS(rs.getBigDecimal("R44_OVER_5YEARS"));
obj.setR44_TOTAL(rs.getBigDecimal("R44_TOTAL"));

// =========================
// R45
// =========================
obj.setR45_1_DAY(rs.getBigDecimal("R45_1_DAY"));
obj.setR45_2TO7_DAYS(rs.getBigDecimal("R45_2TO7_DAYS"));
obj.setR45_8TO14_DAYS(rs.getBigDecimal("R45_8TO14_DAYS"));
obj.setR45_15TO30_DAYS(rs.getBigDecimal("R45_15TO30_DAYS"));
obj.setR45_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R45_31DAYS_UPTO_2MONTHS"));
obj.setR45_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R45_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR45_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R45_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR45_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R45_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR45_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R45_OVER_1YEAR_UPTO_3YEARS"));
obj.setR45_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R45_OVER_3YEARS_UPTO_5YEARS"));
obj.setR45_OVER_5YEARS(rs.getBigDecimal("R45_OVER_5YEARS"));
obj.setR45_TOTAL(rs.getBigDecimal("R45_TOTAL"));

// =========================
// R46
// =========================
obj.setR46_1_DAY(rs.getBigDecimal("R46_1_DAY"));
obj.setR46_2TO7_DAYS(rs.getBigDecimal("R46_2TO7_DAYS"));
obj.setR46_8TO14_DAYS(rs.getBigDecimal("R46_8TO14_DAYS"));
obj.setR46_15TO30_DAYS(rs.getBigDecimal("R46_15TO30_DAYS"));
obj.setR46_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R46_31DAYS_UPTO_2MONTHS"));
obj.setR46_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R46_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR46_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R46_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR46_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R46_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR46_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R46_OVER_1YEAR_UPTO_3YEARS"));
obj.setR46_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R46_OVER_3YEARS_UPTO_5YEARS"));
obj.setR46_OVER_5YEARS(rs.getBigDecimal("R46_OVER_5YEARS"));
obj.setR46_TOTAL(rs.getBigDecimal("R46_TOTAL"));

// =========================
// R47
// =========================
obj.setR47_1_DAY(rs.getBigDecimal("R47_1_DAY"));
obj.setR47_2TO7_DAYS(rs.getBigDecimal("R47_2TO7_DAYS"));
obj.setR47_8TO14_DAYS(rs.getBigDecimal("R47_8TO14_DAYS"));
obj.setR47_15TO30_DAYS(rs.getBigDecimal("R47_15TO30_DAYS"));
obj.setR47_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R47_31DAYS_UPTO_2MONTHS"));
obj.setR47_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R47_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR47_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R47_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR47_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R47_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR47_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R47_OVER_1YEAR_UPTO_3YEARS"));
obj.setR47_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R47_OVER_3YEARS_UPTO_5YEARS"));
obj.setR47_OVER_5YEARS(rs.getBigDecimal("R47_OVER_5YEARS"));
obj.setR47_TOTAL(rs.getBigDecimal("R47_TOTAL"));

// =========================
// R48
// =========================
obj.setR48_1_DAY(rs.getBigDecimal("R48_1_DAY"));
obj.setR48_2TO7_DAYS(rs.getBigDecimal("R48_2TO7_DAYS"));
obj.setR48_8TO14_DAYS(rs.getBigDecimal("R48_8TO14_DAYS"));
obj.setR48_15TO30_DAYS(rs.getBigDecimal("R48_15TO30_DAYS"));
obj.setR48_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R48_31DAYS_UPTO_2MONTHS"));
obj.setR48_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R48_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR48_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R48_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR48_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R48_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR48_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R48_OVER_1YEAR_UPTO_3YEARS"));
obj.setR48_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R48_OVER_3YEARS_UPTO_5YEARS"));
obj.setR48_OVER_5YEARS(rs.getBigDecimal("R48_OVER_5YEARS"));
obj.setR48_TOTAL(rs.getBigDecimal("R48_TOTAL"));

// =========================
// R49
// =========================
obj.setR49_1_DAY(rs.getBigDecimal("R49_1_DAY"));
obj.setR49_2TO7_DAYS(rs.getBigDecimal("R49_2TO7_DAYS"));
obj.setR49_8TO14_DAYS(rs.getBigDecimal("R49_8TO14_DAYS"));
obj.setR49_15TO30_DAYS(rs.getBigDecimal("R49_15TO30_DAYS"));
obj.setR49_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R49_31DAYS_UPTO_2MONTHS"));
obj.setR49_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R49_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR49_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R49_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR49_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R49_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR49_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R49_OVER_1YEAR_UPTO_3YEARS"));
obj.setR49_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R49_OVER_3YEARS_UPTO_5YEARS"));
obj.setR49_OVER_5YEARS(rs.getBigDecimal("R49_OVER_5YEARS"));
obj.setR49_TOTAL(rs.getBigDecimal("R49_TOTAL"));

// =========================
// R50
// =========================
obj.setR50_1_DAY(rs.getBigDecimal("R50_1_DAY"));
obj.setR50_2TO7_DAYS(rs.getBigDecimal("R50_2TO7_DAYS"));
obj.setR50_8TO14_DAYS(rs.getBigDecimal("R50_8TO14_DAYS"));
obj.setR50_15TO30_DAYS(rs.getBigDecimal("R50_15TO30_DAYS"));
obj.setR50_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R50_31DAYS_UPTO_2MONTHS"));
obj.setR50_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R50_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR50_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R50_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR50_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R50_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR50_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R50_OVER_1YEAR_UPTO_3YEARS"));
obj.setR50_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R50_OVER_3YEARS_UPTO_5YEARS"));
obj.setR50_OVER_5YEARS(rs.getBigDecimal("R50_OVER_5YEARS"));
obj.setR50_TOTAL(rs.getBigDecimal("R50_TOTAL"));


// =========================
// R51
// =========================
obj.setR51_1_DAY(rs.getBigDecimal("R51_1_DAY"));
obj.setR51_2TO7_DAYS(rs.getBigDecimal("R51_2TO7_DAYS"));
obj.setR51_8TO14_DAYS(rs.getBigDecimal("R51_8TO14_DAYS"));
obj.setR51_15TO30_DAYS(rs.getBigDecimal("R51_15TO30_DAYS"));
obj.setR51_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R51_31DAYS_UPTO_2MONTHS"));
obj.setR51_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R51_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR51_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R51_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR51_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R51_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR51_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R51_OVER_1YEAR_UPTO_3YEARS"));
obj.setR51_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R51_OVER_3YEARS_UPTO_5YEARS"));
obj.setR51_OVER_5YEARS(rs.getBigDecimal("R51_OVER_5YEARS"));
obj.setR51_TOTAL(rs.getBigDecimal("R51_TOTAL"));

// =========================
// R52
// =========================
obj.setR52_1_DAY(rs.getBigDecimal("R52_1_DAY"));
obj.setR52_2TO7_DAYS(rs.getBigDecimal("R52_2TO7_DAYS"));
obj.setR52_8TO14_DAYS(rs.getBigDecimal("R52_8TO14_DAYS"));
obj.setR52_15TO30_DAYS(rs.getBigDecimal("R52_15TO30_DAYS"));
obj.setR52_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R52_31DAYS_UPTO_2MONTHS"));
obj.setR52_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R52_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR52_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R52_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR52_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R52_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR52_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R52_OVER_1YEAR_UPTO_3YEARS"));
obj.setR52_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R52_OVER_3YEARS_UPTO_5YEARS"));
obj.setR52_OVER_5YEARS(rs.getBigDecimal("R52_OVER_5YEARS"));
obj.setR52_TOTAL(rs.getBigDecimal("R52_TOTAL"));

// =========================
// R53
// =========================
obj.setR53_1_DAY(rs.getBigDecimal("R53_1_DAY"));
obj.setR53_2TO7_DAYS(rs.getBigDecimal("R53_2TO7_DAYS"));
obj.setR53_8TO14_DAYS(rs.getBigDecimal("R53_8TO14_DAYS"));
obj.setR53_15TO30_DAYS(rs.getBigDecimal("R53_15TO30_DAYS"));
obj.setR53_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R53_31DAYS_UPTO_2MONTHS"));
obj.setR53_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R53_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR53_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R53_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR53_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R53_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR53_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R53_OVER_1YEAR_UPTO_3YEARS"));
obj.setR53_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R53_OVER_3YEARS_UPTO_5YEARS"));
obj.setR53_OVER_5YEARS(rs.getBigDecimal("R53_OVER_5YEARS"));
obj.setR53_TOTAL(rs.getBigDecimal("R53_TOTAL"));

// =========================
// R54
// =========================
obj.setR54_1_DAY(rs.getBigDecimal("R54_1_DAY"));
obj.setR54_2TO7_DAYS(rs.getBigDecimal("R54_2TO7_DAYS"));
obj.setR54_8TO14_DAYS(rs.getBigDecimal("R54_8TO14_DAYS"));
obj.setR54_15TO30_DAYS(rs.getBigDecimal("R54_15TO30_DAYS"));
obj.setR54_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R54_31DAYS_UPTO_2MONTHS"));
obj.setR54_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R54_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR54_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R54_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR54_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R54_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR54_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R54_OVER_1YEAR_UPTO_3YEARS"));
obj.setR54_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R54_OVER_3YEARS_UPTO_5YEARS"));
obj.setR54_OVER_5YEARS(rs.getBigDecimal("R54_OVER_5YEARS"));
obj.setR54_TOTAL(rs.getBigDecimal("R54_TOTAL"));

// =========================
// R55
// =========================
obj.setR55_1_DAY(rs.getBigDecimal("R55_1_DAY"));
obj.setR55_2TO7_DAYS(rs.getBigDecimal("R55_2TO7_DAYS"));
obj.setR55_8TO14_DAYS(rs.getBigDecimal("R55_8TO14_DAYS"));
obj.setR55_15TO30_DAYS(rs.getBigDecimal("R55_15TO30_DAYS"));
obj.setR55_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R55_31DAYS_UPTO_2MONTHS"));
obj.setR55_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R55_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR55_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R55_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR55_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R55_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR55_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R55_OVER_1YEAR_UPTO_3YEARS"));
obj.setR55_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R55_OVER_3YEARS_UPTO_5YEARS"));
obj.setR55_OVER_5YEARS(rs.getBigDecimal("R55_OVER_5YEARS"));
obj.setR55_TOTAL(rs.getBigDecimal("R55_TOTAL"));


// =========================
// R56
// =========================
obj.setR56_1_DAY(rs.getBigDecimal("R56_1_DAY"));
obj.setR56_2TO7_DAYS(rs.getBigDecimal("R56_2TO7_DAYS"));
obj.setR56_8TO14_DAYS(rs.getBigDecimal("R56_8TO14_DAYS"));
obj.setR56_15TO30_DAYS(rs.getBigDecimal("R56_15TO30_DAYS"));
obj.setR56_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R56_31DAYS_UPTO_2MONTHS"));
obj.setR56_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R56_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR56_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R56_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR56_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R56_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR56_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R56_OVER_1YEAR_UPTO_3YEARS"));
obj.setR56_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R56_OVER_3YEARS_UPTO_5YEARS"));
obj.setR56_OVER_5YEARS(rs.getBigDecimal("R56_OVER_5YEARS"));
obj.setR56_TOTAL(rs.getBigDecimal("R56_TOTAL"));

// =========================
// R57
// =========================
obj.setR57_1_DAY(rs.getBigDecimal("R57_1_DAY"));
obj.setR57_2TO7_DAYS(rs.getBigDecimal("R57_2TO7_DAYS"));
obj.setR57_8TO14_DAYS(rs.getBigDecimal("R57_8TO14_DAYS"));
obj.setR57_15TO30_DAYS(rs.getBigDecimal("R57_15TO30_DAYS"));
obj.setR57_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R57_31DAYS_UPTO_2MONTHS"));
obj.setR57_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R57_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR57_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R57_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR57_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R57_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR57_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R57_OVER_1YEAR_UPTO_3YEARS"));
obj.setR57_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R57_OVER_3YEARS_UPTO_5YEARS"));
obj.setR57_OVER_5YEARS(rs.getBigDecimal("R57_OVER_5YEARS"));
obj.setR57_TOTAL(rs.getBigDecimal("R57_TOTAL"));

// =========================
// R58
// =========================
obj.setR58_1_DAY(rs.getBigDecimal("R58_1_DAY"));
obj.setR58_2TO7_DAYS(rs.getBigDecimal("R58_2TO7_DAYS"));
obj.setR58_8TO14_DAYS(rs.getBigDecimal("R58_8TO14_DAYS"));
obj.setR58_15TO30_DAYS(rs.getBigDecimal("R58_15TO30_DAYS"));
obj.setR58_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R58_31DAYS_UPTO_2MONTHS"));
obj.setR58_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R58_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR58_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R58_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR58_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R58_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR58_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R58_OVER_1YEAR_UPTO_3YEARS"));
obj.setR58_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R58_OVER_3YEARS_UPTO_5YEARS"));
obj.setR58_OVER_5YEARS(rs.getBigDecimal("R58_OVER_5YEARS"));
obj.setR58_TOTAL(rs.getBigDecimal("R58_TOTAL"));

// =========================
// R59
// =========================
obj.setR59_1_DAY(rs.getBigDecimal("R59_1_DAY"));
obj.setR59_2TO7_DAYS(rs.getBigDecimal("R59_2TO7_DAYS"));
obj.setR59_8TO14_DAYS(rs.getBigDecimal("R59_8TO14_DAYS"));
obj.setR59_15TO30_DAYS(rs.getBigDecimal("R59_15TO30_DAYS"));
obj.setR59_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R59_31DAYS_UPTO_2MONTHS"));
obj.setR59_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R59_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR59_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R59_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR59_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R59_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR59_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R59_OVER_1YEAR_UPTO_3YEARS"));
obj.setR59_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R59_OVER_3YEARS_UPTO_5YEARS"));
obj.setR59_OVER_5YEARS(rs.getBigDecimal("R59_OVER_5YEARS"));
obj.setR59_TOTAL(rs.getBigDecimal("R59_TOTAL"));

// =========================
// R60
// =========================
obj.setR60_1_DAY(rs.getBigDecimal("R60_1_DAY"));
obj.setR60_2TO7_DAYS(rs.getBigDecimal("R60_2TO7_DAYS"));
obj.setR60_8TO14_DAYS(rs.getBigDecimal("R60_8TO14_DAYS"));
obj.setR60_15TO30_DAYS(rs.getBigDecimal("R60_15TO30_DAYS"));
obj.setR60_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R60_31DAYS_UPTO_2MONTHS"));
obj.setR60_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R60_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR60_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R60_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR60_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R60_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR60_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R60_OVER_1YEAR_UPTO_3YEARS"));
obj.setR60_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R60_OVER_3YEARS_UPTO_5YEARS"));
obj.setR60_OVER_5YEARS(rs.getBigDecimal("R60_OVER_5YEARS"));
obj.setR60_TOTAL(rs.getBigDecimal("R60_TOTAL"));


// =========================
// R61
// =========================
obj.setR61_1_DAY(rs.getBigDecimal("R61_1_DAY"));
obj.setR61_2TO7_DAYS(rs.getBigDecimal("R61_2TO7_DAYS"));
obj.setR61_8TO14_DAYS(rs.getBigDecimal("R61_8TO14_DAYS"));
obj.setR61_15TO30_DAYS(rs.getBigDecimal("R61_15TO30_DAYS"));
obj.setR61_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R61_31DAYS_UPTO_2MONTHS"));
obj.setR61_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R61_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR61_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R61_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR61_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R61_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR61_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R61_OVER_1YEAR_UPTO_3YEARS"));
obj.setR61_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R61_OVER_3YEARS_UPTO_5YEARS"));
obj.setR61_OVER_5YEARS(rs.getBigDecimal("R61_OVER_5YEARS"));
obj.setR61_TOTAL(rs.getBigDecimal("R61_TOTAL"));

// =========================
// R62
// =========================
obj.setR62_1_DAY(rs.getBigDecimal("R62_1_DAY"));
obj.setR62_2TO7_DAYS(rs.getBigDecimal("R62_2TO7_DAYS"));
obj.setR62_8TO14_DAYS(rs.getBigDecimal("R62_8TO14_DAYS"));
obj.setR62_15TO30_DAYS(rs.getBigDecimal("R62_15TO30_DAYS"));
obj.setR62_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R62_31DAYS_UPTO_2MONTHS"));
obj.setR62_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R62_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR62_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R62_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR62_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R62_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR62_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R62_OVER_1YEAR_UPTO_3YEARS"));
obj.setR62_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R62_OVER_3YEARS_UPTO_5YEARS"));
obj.setR62_OVER_5YEARS(rs.getBigDecimal("R62_OVER_5YEARS"));
obj.setR62_TOTAL(rs.getBigDecimal("R62_TOTAL"));

// =========================
// R63
// =========================
obj.setR63_1_DAY(rs.getBigDecimal("R63_1_DAY"));
obj.setR63_2TO7_DAYS(rs.getBigDecimal("R63_2TO7_DAYS"));
obj.setR63_8TO14_DAYS(rs.getBigDecimal("R63_8TO14_DAYS"));
obj.setR63_15TO30_DAYS(rs.getBigDecimal("R63_15TO30_DAYS"));
obj.setR63_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R63_31DAYS_UPTO_2MONTHS"));
obj.setR63_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R63_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR63_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R63_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR63_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R63_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR63_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R63_OVER_1YEAR_UPTO_3YEARS"));
obj.setR63_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R63_OVER_3YEARS_UPTO_5YEARS"));
obj.setR63_OVER_5YEARS(rs.getBigDecimal("R63_OVER_5YEARS"));
obj.setR63_TOTAL(rs.getBigDecimal("R63_TOTAL"));

// =========================
// R64
// =========================
obj.setR64_1_DAY(rs.getBigDecimal("R64_1_DAY"));
obj.setR64_2TO7_DAYS(rs.getBigDecimal("R64_2TO7_DAYS"));
obj.setR64_8TO14_DAYS(rs.getBigDecimal("R64_8TO14_DAYS"));
obj.setR64_15TO30_DAYS(rs.getBigDecimal("R64_15TO30_DAYS"));
obj.setR64_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R64_31DAYS_UPTO_2MONTHS"));
obj.setR64_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R64_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR64_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R64_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR64_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R64_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR64_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R64_OVER_1YEAR_UPTO_3YEARS"));
obj.setR64_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R64_OVER_3YEARS_UPTO_5YEARS"));
obj.setR64_OVER_5YEARS(rs.getBigDecimal("R64_OVER_5YEARS"));
obj.setR64_TOTAL(rs.getBigDecimal("R64_TOTAL"));

// =========================
// R65
// =========================
obj.setR65_1_DAY(rs.getBigDecimal("R65_1_DAY"));
obj.setR65_2TO7_DAYS(rs.getBigDecimal("R65_2TO7_DAYS"));
obj.setR65_8TO14_DAYS(rs.getBigDecimal("R65_8TO14_DAYS"));
obj.setR65_15TO30_DAYS(rs.getBigDecimal("R65_15TO30_DAYS"));
obj.setR65_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R65_31DAYS_UPTO_2MONTHS"));
obj.setR65_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R65_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR65_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R65_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR65_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R65_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR65_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R65_OVER_1YEAR_UPTO_3YEARS"));
obj.setR65_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R65_OVER_3YEARS_UPTO_5YEARS"));
obj.setR65_OVER_5YEARS(rs.getBigDecimal("R65_OVER_5YEARS"));
obj.setR65_TOTAL(rs.getBigDecimal("R65_TOTAL"));


// =========================
// R66
// =========================
obj.setR66_1_DAY(rs.getBigDecimal("R66_1_DAY"));
obj.setR66_2TO7_DAYS(rs.getBigDecimal("R66_2TO7_DAYS"));
obj.setR66_8TO14_DAYS(rs.getBigDecimal("R66_8TO14_DAYS"));
obj.setR66_15TO30_DAYS(rs.getBigDecimal("R66_15TO30_DAYS"));
obj.setR66_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R66_31DAYS_UPTO_2MONTHS"));
obj.setR66_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R66_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR66_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R66_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR66_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R66_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR66_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R66_OVER_1YEAR_UPTO_3YEARS"));
obj.setR66_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R66_OVER_3YEARS_UPTO_5YEARS"));
obj.setR66_OVER_5YEARS(rs.getBigDecimal("R66_OVER_5YEARS"));
obj.setR66_TOTAL(rs.getBigDecimal("R66_TOTAL"));

// =========================
// R67
// =========================
obj.setR67_1_DAY(rs.getBigDecimal("R67_1_DAY"));
obj.setR67_2TO7_DAYS(rs.getBigDecimal("R67_2TO7_DAYS"));
obj.setR67_8TO14_DAYS(rs.getBigDecimal("R67_8TO14_DAYS"));
obj.setR67_15TO30_DAYS(rs.getBigDecimal("R67_15TO30_DAYS"));
obj.setR67_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R67_31DAYS_UPTO_2MONTHS"));
obj.setR67_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R67_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR67_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R67_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR67_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R67_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR67_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R67_OVER_1YEAR_UPTO_3YEARS"));
obj.setR67_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R67_OVER_3YEARS_UPTO_5YEARS"));
obj.setR67_OVER_5YEARS(rs.getBigDecimal("R67_OVER_5YEARS"));
obj.setR67_TOTAL(rs.getBigDecimal("R67_TOTAL"));

// =========================
// R68
// =========================
obj.setR68_1_DAY(rs.getBigDecimal("R68_1_DAY"));
obj.setR68_2TO7_DAYS(rs.getBigDecimal("R68_2TO7_DAYS"));
obj.setR68_8TO14_DAYS(rs.getBigDecimal("R68_8TO14_DAYS"));
obj.setR68_15TO30_DAYS(rs.getBigDecimal("R68_15TO30_DAYS"));
obj.setR68_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R68_31DAYS_UPTO_2MONTHS"));
obj.setR68_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R68_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR68_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R68_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR68_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R68_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR68_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R68_OVER_1YEAR_UPTO_3YEARS"));
obj.setR68_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R68_OVER_3YEARS_UPTO_5YEARS"));
obj.setR68_OVER_5YEARS(rs.getBigDecimal("R68_OVER_5YEARS"));
obj.setR68_TOTAL(rs.getBigDecimal("R68_TOTAL"));

// =========================
// R69
// =========================
obj.setR69_1_DAY(rs.getBigDecimal("R69_1_DAY"));
obj.setR69_2TO7_DAYS(rs.getBigDecimal("R69_2TO7_DAYS"));
obj.setR69_8TO14_DAYS(rs.getBigDecimal("R69_8TO14_DAYS"));
obj.setR69_15TO30_DAYS(rs.getBigDecimal("R69_15TO30_DAYS"));
obj.setR69_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R69_31DAYS_UPTO_2MONTHS"));
obj.setR69_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R69_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR69_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R69_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR69_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R69_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR69_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R69_OVER_1YEAR_UPTO_3YEARS"));
obj.setR69_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R69_OVER_3YEARS_UPTO_5YEARS"));
obj.setR69_OVER_5YEARS(rs.getBigDecimal("R69_OVER_5YEARS"));
obj.setR69_TOTAL(rs.getBigDecimal("R69_TOTAL"));

// =========================
// R70
// =========================
obj.setR70_1_DAY(rs.getBigDecimal("R70_1_DAY"));
obj.setR70_2TO7_DAYS(rs.getBigDecimal("R70_2TO7_DAYS"));
obj.setR70_8TO14_DAYS(rs.getBigDecimal("R70_8TO14_DAYS"));
obj.setR70_15TO30_DAYS(rs.getBigDecimal("R70_15TO30_DAYS"));
obj.setR70_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R70_31DAYS_UPTO_2MONTHS"));
obj.setR70_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R70_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR70_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R70_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR70_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R70_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR70_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R70_OVER_1YEAR_UPTO_3YEARS"));
obj.setR70_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R70_OVER_3YEARS_UPTO_5YEARS"));
obj.setR70_OVER_5YEARS(rs.getBigDecimal("R70_OVER_5YEARS"));
obj.setR70_TOTAL(rs.getBigDecimal("R70_TOTAL"));

// =========================
// R71
// =========================
obj.setR71_1_DAY(rs.getBigDecimal("R71_1_DAY"));
obj.setR71_2TO7_DAYS(rs.getBigDecimal("R71_2TO7_DAYS"));
obj.setR71_8TO14_DAYS(rs.getBigDecimal("R71_8TO14_DAYS"));
obj.setR71_15TO30_DAYS(rs.getBigDecimal("R71_15TO30_DAYS"));
obj.setR71_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R71_31DAYS_UPTO_2MONTHS"));
obj.setR71_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R71_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR71_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R71_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR71_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R71_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR71_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R71_OVER_1YEAR_UPTO_3YEARS"));
obj.setR71_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R71_OVER_3YEARS_UPTO_5YEARS"));
obj.setR71_OVER_5YEARS(rs.getBigDecimal("R71_OVER_5YEARS"));
obj.setR71_TOTAL(rs.getBigDecimal("R71_TOTAL"));

// =========================
// R72
// =========================
obj.setR72_1_DAY(rs.getBigDecimal("R72_1_DAY"));
obj.setR72_2TO7_DAYS(rs.getBigDecimal("R72_2TO7_DAYS"));
obj.setR72_8TO14_DAYS(rs.getBigDecimal("R72_8TO14_DAYS"));
obj.setR72_15TO30_DAYS(rs.getBigDecimal("R72_15TO30_DAYS"));
obj.setR72_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R72_31DAYS_UPTO_2MONTHS"));
obj.setR72_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R72_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR72_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R72_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR72_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R72_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR72_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R72_OVER_1YEAR_UPTO_3YEARS"));
obj.setR72_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R72_OVER_3YEARS_UPTO_5YEARS"));
obj.setR72_OVER_5YEARS(rs.getBigDecimal("R72_OVER_5YEARS"));
obj.setR72_TOTAL(rs.getBigDecimal("R72_TOTAL"));

// =========================
// R73
// =========================
obj.setR73_1_DAY(rs.getBigDecimal("R73_1_DAY"));
obj.setR73_2TO7_DAYS(rs.getBigDecimal("R73_2TO7_DAYS"));
obj.setR73_8TO14_DAYS(rs.getBigDecimal("R73_8TO14_DAYS"));
obj.setR73_15TO30_DAYS(rs.getBigDecimal("R73_15TO30_DAYS"));
obj.setR73_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R73_31DAYS_UPTO_2MONTHS"));
obj.setR73_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R73_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR73_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R73_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR73_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R73_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR73_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R73_OVER_1YEAR_UPTO_3YEARS"));
obj.setR73_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R73_OVER_3YEARS_UPTO_5YEARS"));
obj.setR73_OVER_5YEARS(rs.getBigDecimal("R73_OVER_5YEARS"));
obj.setR73_TOTAL(rs.getBigDecimal("R73_TOTAL"));

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


public class CPR_STRUCT_LIQ_Summary_Entity {
	
 private BigDecimal R8_1_DAY;
private BigDecimal R8_2TO7_DAYS;
private BigDecimal R8_8TO14_DAYS;
private BigDecimal R8_15TO30_DAYS;
private BigDecimal R8_31DAYS_UPTO_2MONTHS;
private BigDecimal R8_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R8_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R8_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R8_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R8_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R8_OVER_5YEARS;
private BigDecimal R8_TOTAL;
private BigDecimal R9_1_DAY;
private BigDecimal R9_2TO7_DAYS;
private BigDecimal R9_8TO14_DAYS;
private BigDecimal R9_15TO30_DAYS;
private BigDecimal R9_31DAYS_UPTO_2MONTHS;
private BigDecimal R9_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R9_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R9_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R9_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R9_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R9_OVER_5YEARS;
private BigDecimal R9_TOTAL;
private BigDecimal R10_1_DAY;
private BigDecimal R10_2TO7_DAYS;
private BigDecimal R10_8TO14_DAYS;
private BigDecimal R10_15TO30_DAYS;
private BigDecimal R10_31DAYS_UPTO_2MONTHS;
private BigDecimal R10_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R10_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R10_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R10_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R10_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R10_OVER_5YEARS;
private BigDecimal R10_TOTAL;
private BigDecimal R11_1_DAY;
private BigDecimal R11_2TO7_DAYS;
private BigDecimal R11_8TO14_DAYS;
private BigDecimal R11_15TO30_DAYS;
private BigDecimal R11_31DAYS_UPTO_2MONTHS;
private BigDecimal R11_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R11_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R11_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R11_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R11_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R11_OVER_5YEARS;
private BigDecimal R11_TOTAL;
private BigDecimal R12_1_DAY;
private BigDecimal R12_2TO7_DAYS;
private BigDecimal R12_8TO14_DAYS;
private BigDecimal R12_15TO30_DAYS;
private BigDecimal R12_31DAYS_UPTO_2MONTHS;
private BigDecimal R12_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R12_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R12_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R12_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R12_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R12_OVER_5YEARS;
private BigDecimal R12_TOTAL;
private BigDecimal R13_1_DAY;
private BigDecimal R13_2TO7_DAYS;
private BigDecimal R13_8TO14_DAYS;
private BigDecimal R13_15TO30_DAYS;
private BigDecimal R13_31DAYS_UPTO_2MONTHS;
private BigDecimal R13_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R13_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R13_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R13_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R13_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R13_OVER_5YEARS;
private BigDecimal R13_TOTAL;
private BigDecimal R14_1_DAY;
private BigDecimal R14_2TO7_DAYS;
private BigDecimal R14_8TO14_DAYS;
private BigDecimal R14_15TO30_DAYS;
private BigDecimal R14_31DAYS_UPTO_2MONTHS;
private BigDecimal R14_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R14_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R14_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R14_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R14_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R14_OVER_5YEARS;
private BigDecimal R14_TOTAL;
private BigDecimal R15_1_DAY;
private BigDecimal R15_2TO7_DAYS;
private BigDecimal R15_8TO14_DAYS;
private BigDecimal R15_15TO30_DAYS;
private BigDecimal R15_31DAYS_UPTO_2MONTHS;
private BigDecimal R15_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R15_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R15_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R15_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R15_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R15_OVER_5YEARS;
private BigDecimal R15_TOTAL;
private BigDecimal R16_1_DAY;
private BigDecimal R16_2TO7_DAYS;
private BigDecimal R16_8TO14_DAYS;
private BigDecimal R16_15TO30_DAYS;
private BigDecimal R16_31DAYS_UPTO_2MONTHS;
private BigDecimal R16_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R16_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R16_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R16_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R16_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R16_OVER_5YEARS;
private BigDecimal R16_TOTAL;
private BigDecimal R17_1_DAY;
private BigDecimal R17_2TO7_DAYS;
private BigDecimal R17_8TO14_DAYS;
private BigDecimal R17_15TO30_DAYS;
private BigDecimal R17_31DAYS_UPTO_2MONTHS;
private BigDecimal R17_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R17_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R17_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R17_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R17_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R17_OVER_5YEARS;
private BigDecimal R17_TOTAL;
private BigDecimal R18_1_DAY;
private BigDecimal R18_2TO7_DAYS;
private BigDecimal R18_8TO14_DAYS;
private BigDecimal R18_15TO30_DAYS;
private BigDecimal R18_31DAYS_UPTO_2MONTHS;
private BigDecimal R18_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R18_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R18_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R18_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R18_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R18_OVER_5YEARS;
private BigDecimal R18_TOTAL;
private BigDecimal R19_1_DAY;
private BigDecimal R19_2TO7_DAYS;
private BigDecimal R19_8TO14_DAYS;
private BigDecimal R19_15TO30_DAYS;
private BigDecimal R19_31DAYS_UPTO_2MONTHS;
private BigDecimal R19_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R19_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R19_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R19_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R19_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R19_OVER_5YEARS;
private BigDecimal R19_TOTAL;
private BigDecimal R20_1_DAY;
private BigDecimal R20_2TO7_DAYS;
private BigDecimal R20_8TO14_DAYS;
private BigDecimal R20_15TO30_DAYS;
private BigDecimal R20_31DAYS_UPTO_2MONTHS;
private BigDecimal R20_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R20_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R20_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R20_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R20_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R20_OVER_5YEARS;
private BigDecimal R20_TOTAL;
private BigDecimal R21_1_DAY;
private BigDecimal R21_2TO7_DAYS;
private BigDecimal R21_8TO14_DAYS;
private BigDecimal R21_15TO30_DAYS;
private BigDecimal R21_31DAYS_UPTO_2MONTHS;
private BigDecimal R21_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R21_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R21_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R21_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R21_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R21_OVER_5YEARS;
private BigDecimal R21_TOTAL;
private BigDecimal R22_1_DAY;
private BigDecimal R22_2TO7_DAYS;
private BigDecimal R22_8TO14_DAYS;
private BigDecimal R22_15TO30_DAYS;
private BigDecimal R22_31DAYS_UPTO_2MONTHS;
private BigDecimal R22_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R22_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R22_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R22_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R22_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R22_OVER_5YEARS;
private BigDecimal R22_TOTAL;
private BigDecimal R23_1_DAY;
private BigDecimal R23_2TO7_DAYS;
private BigDecimal R23_8TO14_DAYS;
private BigDecimal R23_15TO30_DAYS;
private BigDecimal R23_31DAYS_UPTO_2MONTHS;
private BigDecimal R23_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R23_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R23_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R23_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R23_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R23_OVER_5YEARS;
private BigDecimal R23_TOTAL;
private BigDecimal R24_1_DAY;
private BigDecimal R24_2TO7_DAYS;
private BigDecimal R24_8TO14_DAYS;
private BigDecimal R24_15TO30_DAYS;
private BigDecimal R24_31DAYS_UPTO_2MONTHS;
private BigDecimal R24_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R24_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R24_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R24_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R24_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R24_OVER_5YEARS;
private BigDecimal R24_TOTAL;
private BigDecimal R25_1_DAY;
private BigDecimal R25_2TO7_DAYS;
private BigDecimal R25_8TO14_DAYS;
private BigDecimal R25_15TO30_DAYS;
private BigDecimal R25_31DAYS_UPTO_2MONTHS;
private BigDecimal R25_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R25_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R25_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R25_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R25_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R25_OVER_5YEARS;
private BigDecimal R25_TOTAL;
private BigDecimal R26_1_DAY;
private BigDecimal R26_2TO7_DAYS;
private BigDecimal R26_8TO14_DAYS;
private BigDecimal R26_15TO30_DAYS;
private BigDecimal R26_31DAYS_UPTO_2MONTHS;
private BigDecimal R26_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R26_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R26_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R26_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R26_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R26_OVER_5YEARS;
private BigDecimal R26_TOTAL;
private BigDecimal R27_1_DAY;
private BigDecimal R27_2TO7_DAYS;
private BigDecimal R27_8TO14_DAYS;
private BigDecimal R27_15TO30_DAYS;
private BigDecimal R27_31DAYS_UPTO_2MONTHS;
private BigDecimal R27_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R27_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R27_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R27_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R27_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R27_OVER_5YEARS;
private BigDecimal R27_TOTAL;
private BigDecimal R28_1_DAY;
private BigDecimal R28_2TO7_DAYS;
private BigDecimal R28_8TO14_DAYS;
private BigDecimal R28_15TO30_DAYS;
private BigDecimal R28_31DAYS_UPTO_2MONTHS;
private BigDecimal R28_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R28_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R28_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R28_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R28_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R28_OVER_5YEARS;
private BigDecimal R28_TOTAL;
private BigDecimal R29_1_DAY;
private BigDecimal R29_2TO7_DAYS;
private BigDecimal R29_8TO14_DAYS;
private BigDecimal R29_15TO30_DAYS;
private BigDecimal R29_31DAYS_UPTO_2MONTHS;
private BigDecimal R29_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R29_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R29_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R29_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R29_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R29_OVER_5YEARS;
private BigDecimal R29_TOTAL;
private BigDecimal R30_1_DAY;
private BigDecimal R30_2TO7_DAYS;
private BigDecimal R30_8TO14_DAYS;
private BigDecimal R30_15TO30_DAYS;
private BigDecimal R30_31DAYS_UPTO_2MONTHS;
private BigDecimal R30_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R30_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R30_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R30_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R30_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R30_OVER_5YEARS;
private BigDecimal R30_TOTAL;
private BigDecimal R31_1_DAY;
private BigDecimal R31_2TO7_DAYS;
private BigDecimal R31_8TO14_DAYS;
private BigDecimal R31_15TO30_DAYS;
private BigDecimal R31_31DAYS_UPTO_2MONTHS;
private BigDecimal R31_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R31_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R31_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R31_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R31_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R31_OVER_5YEARS;
private BigDecimal R31_TOTAL;
private BigDecimal R32_1_DAY;
private BigDecimal R32_2TO7_DAYS;
private BigDecimal R32_8TO14_DAYS;
private BigDecimal R32_15TO30_DAYS;
private BigDecimal R32_31DAYS_UPTO_2MONTHS;
private BigDecimal R32_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R32_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R32_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R32_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R32_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R32_OVER_5YEARS;
private BigDecimal R32_TOTAL;
private BigDecimal R33_1_DAY;
private BigDecimal R33_2TO7_DAYS;
private BigDecimal R33_8TO14_DAYS;
private BigDecimal R33_15TO30_DAYS;
private BigDecimal R33_31DAYS_UPTO_2MONTHS;
private BigDecimal R33_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R33_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R33_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R33_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R33_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R33_OVER_5YEARS;
private BigDecimal R33_TOTAL;
private BigDecimal R34_1_DAY;
private BigDecimal R34_2TO7_DAYS;
private BigDecimal R34_8TO14_DAYS;
private BigDecimal R34_15TO30_DAYS;
private BigDecimal R34_31DAYS_UPTO_2MONTHS;
private BigDecimal R34_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R34_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R34_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R34_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R34_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R34_OVER_5YEARS;
private BigDecimal R34_TOTAL;
private BigDecimal R35_1_DAY;
private BigDecimal R35_2TO7_DAYS;
private BigDecimal R35_8TO14_DAYS;
private BigDecimal R35_15TO30_DAYS;
private BigDecimal R35_31DAYS_UPTO_2MONTHS;
private BigDecimal R35_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R35_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R35_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R35_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R35_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R35_OVER_5YEARS;
private BigDecimal R35_TOTAL;
private BigDecimal R36_1_DAY;
private BigDecimal R36_2TO7_DAYS;
private BigDecimal R36_8TO14_DAYS;
private BigDecimal R36_15TO30_DAYS;
private BigDecimal R36_31DAYS_UPTO_2MONTHS;
private BigDecimal R36_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R36_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R36_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R36_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R36_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R36_OVER_5YEARS;
private BigDecimal R36_TOTAL;
private BigDecimal R37_1_DAY;
private BigDecimal R37_2TO7_DAYS;
private BigDecimal R37_8TO14_DAYS;
private BigDecimal R37_15TO30_DAYS;
private BigDecimal R37_31DAYS_UPTO_2MONTHS;
private BigDecimal R37_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R37_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R37_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R37_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R37_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R37_OVER_5YEARS;
private BigDecimal R37_TOTAL;
private BigDecimal R38_1_DAY;
private BigDecimal R38_2TO7_DAYS;
private BigDecimal R38_8TO14_DAYS;
private BigDecimal R38_15TO30_DAYS;
private BigDecimal R38_31DAYS_UPTO_2MONTHS;
private BigDecimal R38_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R38_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R38_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R38_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R38_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R38_OVER_5YEARS;
private BigDecimal R38_TOTAL;
private BigDecimal R39_1_DAY;
private BigDecimal R39_2TO7_DAYS;
private BigDecimal R39_8TO14_DAYS;
private BigDecimal R39_15TO30_DAYS;
private BigDecimal R39_31DAYS_UPTO_2MONTHS;
private BigDecimal R39_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R39_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R39_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R39_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R39_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R39_OVER_5YEARS;
private BigDecimal R39_TOTAL;
private BigDecimal R40_1_DAY;
private BigDecimal R40_2TO7_DAYS;
private BigDecimal R40_8TO14_DAYS;
private BigDecimal R40_15TO30_DAYS;
private BigDecimal R40_31DAYS_UPTO_2MONTHS;
private BigDecimal R40_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R40_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R40_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R40_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R40_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R40_OVER_5YEARS;
private BigDecimal R40_TOTAL;
private BigDecimal R41_1_DAY;
private BigDecimal R41_2TO7_DAYS;
private BigDecimal R41_8TO14_DAYS;
private BigDecimal R41_15TO30_DAYS;
private BigDecimal R41_31DAYS_UPTO_2MONTHS;
private BigDecimal R41_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R41_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R41_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R41_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R41_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R41_OVER_5YEARS;
private BigDecimal R41_TOTAL;
private BigDecimal R42_1_DAY;
private BigDecimal R42_2TO7_DAYS;
private BigDecimal R42_8TO14_DAYS;
private BigDecimal R42_15TO30_DAYS;
private BigDecimal R42_31DAYS_UPTO_2MONTHS;
private BigDecimal R42_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R42_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R42_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R42_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R42_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R42_OVER_5YEARS;
private BigDecimal R42_TOTAL;
private BigDecimal R43_1_DAY;
private BigDecimal R43_2TO7_DAYS;
private BigDecimal R43_8TO14_DAYS;
private BigDecimal R43_15TO30_DAYS;
private BigDecimal R43_31DAYS_UPTO_2MONTHS;
private BigDecimal R43_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R43_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R43_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R43_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R43_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R43_OVER_5YEARS;
private BigDecimal R43_TOTAL;
private BigDecimal R44_1_DAY;
private BigDecimal R44_2TO7_DAYS;
private BigDecimal R44_8TO14_DAYS;
private BigDecimal R44_15TO30_DAYS;
private BigDecimal R44_31DAYS_UPTO_2MONTHS;
private BigDecimal R44_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R44_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R44_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R44_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R44_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R44_OVER_5YEARS;
private BigDecimal R44_TOTAL;
private BigDecimal R45_1_DAY;
private BigDecimal R45_2TO7_DAYS;
private BigDecimal R45_8TO14_DAYS;
private BigDecimal R45_15TO30_DAYS;
private BigDecimal R45_31DAYS_UPTO_2MONTHS;
private BigDecimal R45_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R45_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R45_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R45_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R45_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R45_OVER_5YEARS;
private BigDecimal R45_TOTAL;
private BigDecimal R46_1_DAY;
private BigDecimal R46_2TO7_DAYS;
private BigDecimal R46_8TO14_DAYS;
private BigDecimal R46_15TO30_DAYS;
private BigDecimal R46_31DAYS_UPTO_2MONTHS;
private BigDecimal R46_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R46_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R46_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R46_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R46_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R46_OVER_5YEARS;
private BigDecimal R46_TOTAL;
private BigDecimal R47_1_DAY;
private BigDecimal R47_2TO7_DAYS;
private BigDecimal R47_8TO14_DAYS;
private BigDecimal R47_15TO30_DAYS;
private BigDecimal R47_31DAYS_UPTO_2MONTHS;
private BigDecimal R47_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R47_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R47_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R47_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R47_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R47_OVER_5YEARS;
private BigDecimal R47_TOTAL;
private BigDecimal R48_1_DAY;
private BigDecimal R48_2TO7_DAYS;
private BigDecimal R48_8TO14_DAYS;
private BigDecimal R48_15TO30_DAYS;
private BigDecimal R48_31DAYS_UPTO_2MONTHS;
private BigDecimal R48_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R48_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R48_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R48_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R48_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R48_OVER_5YEARS;
private BigDecimal R48_TOTAL;
private BigDecimal R49_1_DAY;
private BigDecimal R49_2TO7_DAYS;
private BigDecimal R49_8TO14_DAYS;
private BigDecimal R49_15TO30_DAYS;
private BigDecimal R49_31DAYS_UPTO_2MONTHS;
private BigDecimal R49_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R49_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R49_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R49_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R49_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R49_OVER_5YEARS;
private BigDecimal R49_TOTAL;
private BigDecimal R50_1_DAY;
private BigDecimal R50_2TO7_DAYS;
private BigDecimal R50_8TO14_DAYS;
private BigDecimal R50_15TO30_DAYS;
private BigDecimal R50_31DAYS_UPTO_2MONTHS;
private BigDecimal R50_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R50_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R50_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R50_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R50_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R50_OVER_5YEARS;
private BigDecimal R50_TOTAL;
private BigDecimal R51_1_DAY;
private BigDecimal R51_2TO7_DAYS;
private BigDecimal R51_8TO14_DAYS;
private BigDecimal R51_15TO30_DAYS;
private BigDecimal R51_31DAYS_UPTO_2MONTHS;
private BigDecimal R51_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R51_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R51_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R51_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R51_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R51_OVER_5YEARS;
private BigDecimal R51_TOTAL;
private BigDecimal R52_1_DAY;
private BigDecimal R52_2TO7_DAYS;
private BigDecimal R52_8TO14_DAYS;
private BigDecimal R52_15TO30_DAYS;
private BigDecimal R52_31DAYS_UPTO_2MONTHS;
private BigDecimal R52_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R52_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R52_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R52_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R52_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R52_OVER_5YEARS;
private BigDecimal R52_TOTAL;
private BigDecimal R53_1_DAY;
private BigDecimal R53_2TO7_DAYS;
private BigDecimal R53_8TO14_DAYS;
private BigDecimal R53_15TO30_DAYS;
private BigDecimal R53_31DAYS_UPTO_2MONTHS;
private BigDecimal R53_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R53_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R53_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R53_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R53_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R53_OVER_5YEARS;
private BigDecimal R53_TOTAL;
private BigDecimal R54_1_DAY;
private BigDecimal R54_2TO7_DAYS;
private BigDecimal R54_8TO14_DAYS;
private BigDecimal R54_15TO30_DAYS;
private BigDecimal R54_31DAYS_UPTO_2MONTHS;
private BigDecimal R54_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R54_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R54_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R54_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R54_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R54_OVER_5YEARS;
private BigDecimal R54_TOTAL;
private BigDecimal R55_1_DAY;
private BigDecimal R55_2TO7_DAYS;
private BigDecimal R55_8TO14_DAYS;
private BigDecimal R55_15TO30_DAYS;
private BigDecimal R55_31DAYS_UPTO_2MONTHS;
private BigDecimal R55_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R55_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R55_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R55_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R55_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R55_OVER_5YEARS;
private BigDecimal R55_TOTAL;
private BigDecimal R56_1_DAY;
private BigDecimal R56_2TO7_DAYS;
private BigDecimal R56_8TO14_DAYS;
private BigDecimal R56_15TO30_DAYS;
private BigDecimal R56_31DAYS_UPTO_2MONTHS;
private BigDecimal R56_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R56_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R56_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R56_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R56_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R56_OVER_5YEARS;
private BigDecimal R56_TOTAL;
private BigDecimal R57_1_DAY;
private BigDecimal R57_2TO7_DAYS;
private BigDecimal R57_8TO14_DAYS;
private BigDecimal R57_15TO30_DAYS;
private BigDecimal R57_31DAYS_UPTO_2MONTHS;
private BigDecimal R57_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R57_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R57_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R57_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R57_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R57_OVER_5YEARS;
private BigDecimal R57_TOTAL;
private BigDecimal R58_1_DAY;
private BigDecimal R58_2TO7_DAYS;
private BigDecimal R58_8TO14_DAYS;
private BigDecimal R58_15TO30_DAYS;
private BigDecimal R58_31DAYS_UPTO_2MONTHS;
private BigDecimal R58_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R58_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R58_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R58_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R58_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R58_OVER_5YEARS;
private BigDecimal R58_TOTAL;
private BigDecimal R59_1_DAY;
private BigDecimal R59_2TO7_DAYS;
private BigDecimal R59_8TO14_DAYS;
private BigDecimal R59_15TO30_DAYS;
private BigDecimal R59_31DAYS_UPTO_2MONTHS;
private BigDecimal R59_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R59_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R59_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R59_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R59_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R59_OVER_5YEARS;
private BigDecimal R59_TOTAL;
private BigDecimal R60_1_DAY;
private BigDecimal R60_2TO7_DAYS;
private BigDecimal R60_8TO14_DAYS;
private BigDecimal R60_15TO30_DAYS;
private BigDecimal R60_31DAYS_UPTO_2MONTHS;
private BigDecimal R60_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R60_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R60_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R60_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R60_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R60_OVER_5YEARS;
private BigDecimal R60_TOTAL;
private BigDecimal R61_1_DAY;
private BigDecimal R61_2TO7_DAYS;
private BigDecimal R61_8TO14_DAYS;
private BigDecimal R61_15TO30_DAYS;
private BigDecimal R61_31DAYS_UPTO_2MONTHS;
private BigDecimal R61_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R61_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R61_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R61_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R61_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R61_OVER_5YEARS;
private BigDecimal R61_TOTAL;
private BigDecimal R62_1_DAY;
private BigDecimal R62_2TO7_DAYS;
private BigDecimal R62_8TO14_DAYS;
private BigDecimal R62_15TO30_DAYS;
private BigDecimal R62_31DAYS_UPTO_2MONTHS;
private BigDecimal R62_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R62_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R62_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R62_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R62_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R62_OVER_5YEARS;
private BigDecimal R62_TOTAL;
private BigDecimal R63_1_DAY;
private BigDecimal R63_2TO7_DAYS;
private BigDecimal R63_8TO14_DAYS;
private BigDecimal R63_15TO30_DAYS;
private BigDecimal R63_31DAYS_UPTO_2MONTHS;
private BigDecimal R63_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R63_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R63_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R63_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R63_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R63_OVER_5YEARS;
private BigDecimal R63_TOTAL;
private BigDecimal R64_1_DAY;
private BigDecimal R64_2TO7_DAYS;
private BigDecimal R64_8TO14_DAYS;
private BigDecimal R64_15TO30_DAYS;
private BigDecimal R64_31DAYS_UPTO_2MONTHS;
private BigDecimal R64_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R64_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R64_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R64_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R64_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R64_OVER_5YEARS;
private BigDecimal R64_TOTAL;
private BigDecimal R65_1_DAY;
private BigDecimal R65_2TO7_DAYS;
private BigDecimal R65_8TO14_DAYS;
private BigDecimal R65_15TO30_DAYS;
private BigDecimal R65_31DAYS_UPTO_2MONTHS;
private BigDecimal R65_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R65_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R65_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R65_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R65_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R65_OVER_5YEARS;
private BigDecimal R65_TOTAL;
private BigDecimal R66_1_DAY;
private BigDecimal R66_2TO7_DAYS;
private BigDecimal R66_8TO14_DAYS;
private BigDecimal R66_15TO30_DAYS;
private BigDecimal R66_31DAYS_UPTO_2MONTHS;
private BigDecimal R66_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R66_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R66_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R66_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R66_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R66_OVER_5YEARS;
private BigDecimal R66_TOTAL;
private BigDecimal R67_1_DAY;
private BigDecimal R67_2TO7_DAYS;
private BigDecimal R67_8TO14_DAYS;
private BigDecimal R67_15TO30_DAYS;
private BigDecimal R67_31DAYS_UPTO_2MONTHS;
private BigDecimal R67_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R67_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R67_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R67_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R67_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R67_OVER_5YEARS;
private BigDecimal R67_TOTAL;
private BigDecimal R68_1_DAY;
private BigDecimal R68_2TO7_DAYS;
private BigDecimal R68_8TO14_DAYS;
private BigDecimal R68_15TO30_DAYS;
private BigDecimal R68_31DAYS_UPTO_2MONTHS;
private BigDecimal R68_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R68_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R68_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R68_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R68_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R68_OVER_5YEARS;
private BigDecimal R68_TOTAL;
private BigDecimal R69_1_DAY;
private BigDecimal R69_2TO7_DAYS;
private BigDecimal R69_8TO14_DAYS;
private BigDecimal R69_15TO30_DAYS;
private BigDecimal R69_31DAYS_UPTO_2MONTHS;
private BigDecimal R69_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R69_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R69_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R69_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R69_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R69_OVER_5YEARS;
private BigDecimal R69_TOTAL;
private BigDecimal R70_1_DAY;
private BigDecimal R70_2TO7_DAYS;
private BigDecimal R70_8TO14_DAYS;
private BigDecimal R70_15TO30_DAYS;
private BigDecimal R70_31DAYS_UPTO_2MONTHS;
private BigDecimal R70_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R70_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R70_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R70_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R70_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R70_OVER_5YEARS;
private BigDecimal R70_TOTAL;
private BigDecimal R71_1_DAY;
private BigDecimal R71_2TO7_DAYS;
private BigDecimal R71_8TO14_DAYS;
private BigDecimal R71_15TO30_DAYS;
private BigDecimal R71_31DAYS_UPTO_2MONTHS;
private BigDecimal R71_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R71_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R71_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R71_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R71_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R71_OVER_5YEARS;
private BigDecimal R71_TOTAL;
private BigDecimal R72_1_DAY;
private BigDecimal R72_2TO7_DAYS;
private BigDecimal R72_8TO14_DAYS;
private BigDecimal R72_15TO30_DAYS;
private BigDecimal R72_31DAYS_UPTO_2MONTHS;
private BigDecimal R72_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R72_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R72_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R72_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R72_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R72_OVER_5YEARS;
private BigDecimal R72_TOTAL;
private BigDecimal R73_1_DAY;
private BigDecimal R73_2TO7_DAYS;
private BigDecimal R73_8TO14_DAYS;
private BigDecimal R73_15TO30_DAYS;
private BigDecimal R73_31DAYS_UPTO_2MONTHS;
private BigDecimal R73_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R73_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R73_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R73_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R73_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R73_OVER_5YEARS;
private BigDecimal R73_TOTAL;

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
	public BigDecimal getR8_1_DAY() {
		return R8_1_DAY;
	}
	public void setR8_1_DAY(BigDecimal r8_1_DAY) {
		R8_1_DAY = r8_1_DAY;
	}
	public BigDecimal getR8_2TO7_DAYS() {
		return R8_2TO7_DAYS;
	}
	public void setR8_2TO7_DAYS(BigDecimal r8_2to7_DAYS) {
		R8_2TO7_DAYS = r8_2to7_DAYS;
	}
	public BigDecimal getR8_8TO14_DAYS() {
		return R8_8TO14_DAYS;
	}
	public void setR8_8TO14_DAYS(BigDecimal r8_8to14_DAYS) {
		R8_8TO14_DAYS = r8_8to14_DAYS;
	}
	public BigDecimal getR8_15TO30_DAYS() {
		return R8_15TO30_DAYS;
	}
	public void setR8_15TO30_DAYS(BigDecimal r8_15to30_DAYS) {
		R8_15TO30_DAYS = r8_15to30_DAYS;
	}
	public BigDecimal getR8_31DAYS_UPTO_2MONTHS() {
		return R8_31DAYS_UPTO_2MONTHS;
	}
	public void setR8_31DAYS_UPTO_2MONTHS(BigDecimal r8_31days_UPTO_2MONTHS) {
		R8_31DAYS_UPTO_2MONTHS = r8_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR8_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R8_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR8_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r8_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R8_MORETHAN_2MONTHS_UPTO_3MONHTS = r8_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR8_OVER_3MONTHS_UPTO_6MONTHS() {
		return R8_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR8_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r8_OVER_3MONTHS_UPTO_6MONTHS) {
		R8_OVER_3MONTHS_UPTO_6MONTHS = r8_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR8_OVER_6MONTHS_UPTO_1YEAR() {
		return R8_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR8_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r8_OVER_6MONTHS_UPTO_1YEAR) {
		R8_OVER_6MONTHS_UPTO_1YEAR = r8_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR8_OVER_1YEAR_UPTO_3YEARS() {
		return R8_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR8_OVER_1YEAR_UPTO_3YEARS(BigDecimal r8_OVER_1YEAR_UPTO_3YEARS) {
		R8_OVER_1YEAR_UPTO_3YEARS = r8_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR8_OVER_3YEARS_UPTO_5YEARS() {
		return R8_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR8_OVER_3YEARS_UPTO_5YEARS(BigDecimal r8_OVER_3YEARS_UPTO_5YEARS) {
		R8_OVER_3YEARS_UPTO_5YEARS = r8_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR8_OVER_5YEARS() {
		return R8_OVER_5YEARS;
	}
	public void setR8_OVER_5YEARS(BigDecimal r8_OVER_5YEARS) {
		R8_OVER_5YEARS = r8_OVER_5YEARS;
	}
	public BigDecimal getR8_TOTAL() {
		return R8_TOTAL;
	}
	public void setR8_TOTAL(BigDecimal r8_TOTAL) {
		R8_TOTAL = r8_TOTAL;
	}
	public BigDecimal getR9_1_DAY() {
		return R9_1_DAY;
	}
	public void setR9_1_DAY(BigDecimal r9_1_DAY) {
		R9_1_DAY = r9_1_DAY;
	}
	public BigDecimal getR9_2TO7_DAYS() {
		return R9_2TO7_DAYS;
	}
	public void setR9_2TO7_DAYS(BigDecimal r9_2to7_DAYS) {
		R9_2TO7_DAYS = r9_2to7_DAYS;
	}
	public BigDecimal getR9_8TO14_DAYS() {
		return R9_8TO14_DAYS;
	}
	public void setR9_8TO14_DAYS(BigDecimal r9_8to14_DAYS) {
		R9_8TO14_DAYS = r9_8to14_DAYS;
	}
	public BigDecimal getR9_15TO30_DAYS() {
		return R9_15TO30_DAYS;
	}
	public void setR9_15TO30_DAYS(BigDecimal r9_15to30_DAYS) {
		R9_15TO30_DAYS = r9_15to30_DAYS;
	}
	public BigDecimal getR9_31DAYS_UPTO_2MONTHS() {
		return R9_31DAYS_UPTO_2MONTHS;
	}
	public void setR9_31DAYS_UPTO_2MONTHS(BigDecimal r9_31days_UPTO_2MONTHS) {
		R9_31DAYS_UPTO_2MONTHS = r9_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR9_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R9_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR9_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r9_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R9_MORETHAN_2MONTHS_UPTO_3MONHTS = r9_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR9_OVER_3MONTHS_UPTO_6MONTHS() {
		return R9_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR9_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r9_OVER_3MONTHS_UPTO_6MONTHS) {
		R9_OVER_3MONTHS_UPTO_6MONTHS = r9_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR9_OVER_6MONTHS_UPTO_1YEAR() {
		return R9_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR9_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r9_OVER_6MONTHS_UPTO_1YEAR) {
		R9_OVER_6MONTHS_UPTO_1YEAR = r9_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR9_OVER_1YEAR_UPTO_3YEARS() {
		return R9_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR9_OVER_1YEAR_UPTO_3YEARS(BigDecimal r9_OVER_1YEAR_UPTO_3YEARS) {
		R9_OVER_1YEAR_UPTO_3YEARS = r9_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR9_OVER_3YEARS_UPTO_5YEARS() {
		return R9_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR9_OVER_3YEARS_UPTO_5YEARS(BigDecimal r9_OVER_3YEARS_UPTO_5YEARS) {
		R9_OVER_3YEARS_UPTO_5YEARS = r9_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR9_OVER_5YEARS() {
		return R9_OVER_5YEARS;
	}
	public void setR9_OVER_5YEARS(BigDecimal r9_OVER_5YEARS) {
		R9_OVER_5YEARS = r9_OVER_5YEARS;
	}
	public BigDecimal getR9_TOTAL() {
		return R9_TOTAL;
	}
	public void setR9_TOTAL(BigDecimal r9_TOTAL) {
		R9_TOTAL = r9_TOTAL;
	}
	public BigDecimal getR10_1_DAY() {
		return R10_1_DAY;
	}
	public void setR10_1_DAY(BigDecimal r10_1_DAY) {
		R10_1_DAY = r10_1_DAY;
	}
	public BigDecimal getR10_2TO7_DAYS() {
		return R10_2TO7_DAYS;
	}
	public void setR10_2TO7_DAYS(BigDecimal r10_2to7_DAYS) {
		R10_2TO7_DAYS = r10_2to7_DAYS;
	}
	public BigDecimal getR10_8TO14_DAYS() {
		return R10_8TO14_DAYS;
	}
	public void setR10_8TO14_DAYS(BigDecimal r10_8to14_DAYS) {
		R10_8TO14_DAYS = r10_8to14_DAYS;
	}
	public BigDecimal getR10_15TO30_DAYS() {
		return R10_15TO30_DAYS;
	}
	public void setR10_15TO30_DAYS(BigDecimal r10_15to30_DAYS) {
		R10_15TO30_DAYS = r10_15to30_DAYS;
	}
	public BigDecimal getR10_31DAYS_UPTO_2MONTHS() {
		return R10_31DAYS_UPTO_2MONTHS;
	}
	public void setR10_31DAYS_UPTO_2MONTHS(BigDecimal r10_31days_UPTO_2MONTHS) {
		R10_31DAYS_UPTO_2MONTHS = r10_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR10_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R10_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR10_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r10_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R10_MORETHAN_2MONTHS_UPTO_3MONHTS = r10_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR10_OVER_3MONTHS_UPTO_6MONTHS() {
		return R10_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR10_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r10_OVER_3MONTHS_UPTO_6MONTHS) {
		R10_OVER_3MONTHS_UPTO_6MONTHS = r10_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR10_OVER_6MONTHS_UPTO_1YEAR() {
		return R10_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR10_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r10_OVER_6MONTHS_UPTO_1YEAR) {
		R10_OVER_6MONTHS_UPTO_1YEAR = r10_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR10_OVER_1YEAR_UPTO_3YEARS() {
		return R10_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR10_OVER_1YEAR_UPTO_3YEARS(BigDecimal r10_OVER_1YEAR_UPTO_3YEARS) {
		R10_OVER_1YEAR_UPTO_3YEARS = r10_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR10_OVER_3YEARS_UPTO_5YEARS() {
		return R10_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR10_OVER_3YEARS_UPTO_5YEARS(BigDecimal r10_OVER_3YEARS_UPTO_5YEARS) {
		R10_OVER_3YEARS_UPTO_5YEARS = r10_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR10_OVER_5YEARS() {
		return R10_OVER_5YEARS;
	}
	public void setR10_OVER_5YEARS(BigDecimal r10_OVER_5YEARS) {
		R10_OVER_5YEARS = r10_OVER_5YEARS;
	}
	public BigDecimal getR10_TOTAL() {
		return R10_TOTAL;
	}
	public void setR10_TOTAL(BigDecimal r10_TOTAL) {
		R10_TOTAL = r10_TOTAL;
	}
	public BigDecimal getR11_1_DAY() {
		return R11_1_DAY;
	}
	public void setR11_1_DAY(BigDecimal r11_1_DAY) {
		R11_1_DAY = r11_1_DAY;
	}
	public BigDecimal getR11_2TO7_DAYS() {
		return R11_2TO7_DAYS;
	}
	public void setR11_2TO7_DAYS(BigDecimal r11_2to7_DAYS) {
		R11_2TO7_DAYS = r11_2to7_DAYS;
	}
	public BigDecimal getR11_8TO14_DAYS() {
		return R11_8TO14_DAYS;
	}
	public void setR11_8TO14_DAYS(BigDecimal r11_8to14_DAYS) {
		R11_8TO14_DAYS = r11_8to14_DAYS;
	}
	public BigDecimal getR11_15TO30_DAYS() {
		return R11_15TO30_DAYS;
	}
	public void setR11_15TO30_DAYS(BigDecimal r11_15to30_DAYS) {
		R11_15TO30_DAYS = r11_15to30_DAYS;
	}
	public BigDecimal getR11_31DAYS_UPTO_2MONTHS() {
		return R11_31DAYS_UPTO_2MONTHS;
	}
	public void setR11_31DAYS_UPTO_2MONTHS(BigDecimal r11_31days_UPTO_2MONTHS) {
		R11_31DAYS_UPTO_2MONTHS = r11_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR11_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R11_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR11_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r11_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R11_MORETHAN_2MONTHS_UPTO_3MONHTS = r11_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR11_OVER_3MONTHS_UPTO_6MONTHS() {
		return R11_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR11_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r11_OVER_3MONTHS_UPTO_6MONTHS) {
		R11_OVER_3MONTHS_UPTO_6MONTHS = r11_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR11_OVER_6MONTHS_UPTO_1YEAR() {
		return R11_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR11_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r11_OVER_6MONTHS_UPTO_1YEAR) {
		R11_OVER_6MONTHS_UPTO_1YEAR = r11_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR11_OVER_1YEAR_UPTO_3YEARS() {
		return R11_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR11_OVER_1YEAR_UPTO_3YEARS(BigDecimal r11_OVER_1YEAR_UPTO_3YEARS) {
		R11_OVER_1YEAR_UPTO_3YEARS = r11_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR11_OVER_3YEARS_UPTO_5YEARS() {
		return R11_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR11_OVER_3YEARS_UPTO_5YEARS(BigDecimal r11_OVER_3YEARS_UPTO_5YEARS) {
		R11_OVER_3YEARS_UPTO_5YEARS = r11_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR11_OVER_5YEARS() {
		return R11_OVER_5YEARS;
	}
	public void setR11_OVER_5YEARS(BigDecimal r11_OVER_5YEARS) {
		R11_OVER_5YEARS = r11_OVER_5YEARS;
	}
	public BigDecimal getR11_TOTAL() {
		return R11_TOTAL;
	}
	public void setR11_TOTAL(BigDecimal r11_TOTAL) {
		R11_TOTAL = r11_TOTAL;
	}
	public BigDecimal getR12_1_DAY() {
		return R12_1_DAY;
	}
	public void setR12_1_DAY(BigDecimal r12_1_DAY) {
		R12_1_DAY = r12_1_DAY;
	}
	public BigDecimal getR12_2TO7_DAYS() {
		return R12_2TO7_DAYS;
	}
	public void setR12_2TO7_DAYS(BigDecimal r12_2to7_DAYS) {
		R12_2TO7_DAYS = r12_2to7_DAYS;
	}
	public BigDecimal getR12_8TO14_DAYS() {
		return R12_8TO14_DAYS;
	}
	public void setR12_8TO14_DAYS(BigDecimal r12_8to14_DAYS) {
		R12_8TO14_DAYS = r12_8to14_DAYS;
	}
	public BigDecimal getR12_15TO30_DAYS() {
		return R12_15TO30_DAYS;
	}
	public void setR12_15TO30_DAYS(BigDecimal r12_15to30_DAYS) {
		R12_15TO30_DAYS = r12_15to30_DAYS;
	}
	public BigDecimal getR12_31DAYS_UPTO_2MONTHS() {
		return R12_31DAYS_UPTO_2MONTHS;
	}
	public void setR12_31DAYS_UPTO_2MONTHS(BigDecimal r12_31days_UPTO_2MONTHS) {
		R12_31DAYS_UPTO_2MONTHS = r12_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR12_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R12_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR12_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r12_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R12_MORETHAN_2MONTHS_UPTO_3MONHTS = r12_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR12_OVER_3MONTHS_UPTO_6MONTHS() {
		return R12_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR12_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r12_OVER_3MONTHS_UPTO_6MONTHS) {
		R12_OVER_3MONTHS_UPTO_6MONTHS = r12_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR12_OVER_6MONTHS_UPTO_1YEAR() {
		return R12_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR12_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r12_OVER_6MONTHS_UPTO_1YEAR) {
		R12_OVER_6MONTHS_UPTO_1YEAR = r12_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR12_OVER_1YEAR_UPTO_3YEARS() {
		return R12_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR12_OVER_1YEAR_UPTO_3YEARS(BigDecimal r12_OVER_1YEAR_UPTO_3YEARS) {
		R12_OVER_1YEAR_UPTO_3YEARS = r12_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR12_OVER_3YEARS_UPTO_5YEARS() {
		return R12_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR12_OVER_3YEARS_UPTO_5YEARS(BigDecimal r12_OVER_3YEARS_UPTO_5YEARS) {
		R12_OVER_3YEARS_UPTO_5YEARS = r12_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR12_OVER_5YEARS() {
		return R12_OVER_5YEARS;
	}
	public void setR12_OVER_5YEARS(BigDecimal r12_OVER_5YEARS) {
		R12_OVER_5YEARS = r12_OVER_5YEARS;
	}
	public BigDecimal getR12_TOTAL() {
		return R12_TOTAL;
	}
	public void setR12_TOTAL(BigDecimal r12_TOTAL) {
		R12_TOTAL = r12_TOTAL;
	}
	public BigDecimal getR13_1_DAY() {
		return R13_1_DAY;
	}
	public void setR13_1_DAY(BigDecimal r13_1_DAY) {
		R13_1_DAY = r13_1_DAY;
	}
	public BigDecimal getR13_2TO7_DAYS() {
		return R13_2TO7_DAYS;
	}
	public void setR13_2TO7_DAYS(BigDecimal r13_2to7_DAYS) {
		R13_2TO7_DAYS = r13_2to7_DAYS;
	}
	public BigDecimal getR13_8TO14_DAYS() {
		return R13_8TO14_DAYS;
	}
	public void setR13_8TO14_DAYS(BigDecimal r13_8to14_DAYS) {
		R13_8TO14_DAYS = r13_8to14_DAYS;
	}
	public BigDecimal getR13_15TO30_DAYS() {
		return R13_15TO30_DAYS;
	}
	public void setR13_15TO30_DAYS(BigDecimal r13_15to30_DAYS) {
		R13_15TO30_DAYS = r13_15to30_DAYS;
	}
	public BigDecimal getR13_31DAYS_UPTO_2MONTHS() {
		return R13_31DAYS_UPTO_2MONTHS;
	}
	public void setR13_31DAYS_UPTO_2MONTHS(BigDecimal r13_31days_UPTO_2MONTHS) {
		R13_31DAYS_UPTO_2MONTHS = r13_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR13_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R13_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR13_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r13_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R13_MORETHAN_2MONTHS_UPTO_3MONHTS = r13_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR13_OVER_3MONTHS_UPTO_6MONTHS() {
		return R13_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR13_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r13_OVER_3MONTHS_UPTO_6MONTHS) {
		R13_OVER_3MONTHS_UPTO_6MONTHS = r13_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR13_OVER_6MONTHS_UPTO_1YEAR() {
		return R13_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR13_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r13_OVER_6MONTHS_UPTO_1YEAR) {
		R13_OVER_6MONTHS_UPTO_1YEAR = r13_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR13_OVER_1YEAR_UPTO_3YEARS() {
		return R13_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR13_OVER_1YEAR_UPTO_3YEARS(BigDecimal r13_OVER_1YEAR_UPTO_3YEARS) {
		R13_OVER_1YEAR_UPTO_3YEARS = r13_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR13_OVER_3YEARS_UPTO_5YEARS() {
		return R13_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR13_OVER_3YEARS_UPTO_5YEARS(BigDecimal r13_OVER_3YEARS_UPTO_5YEARS) {
		R13_OVER_3YEARS_UPTO_5YEARS = r13_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR13_OVER_5YEARS() {
		return R13_OVER_5YEARS;
	}
	public void setR13_OVER_5YEARS(BigDecimal r13_OVER_5YEARS) {
		R13_OVER_5YEARS = r13_OVER_5YEARS;
	}
	public BigDecimal getR13_TOTAL() {
		return R13_TOTAL;
	}
	public void setR13_TOTAL(BigDecimal r13_TOTAL) {
		R13_TOTAL = r13_TOTAL;
	}
	public BigDecimal getR14_1_DAY() {
		return R14_1_DAY;
	}
	public void setR14_1_DAY(BigDecimal r14_1_DAY) {
		R14_1_DAY = r14_1_DAY;
	}
	public BigDecimal getR14_2TO7_DAYS() {
		return R14_2TO7_DAYS;
	}
	public void setR14_2TO7_DAYS(BigDecimal r14_2to7_DAYS) {
		R14_2TO7_DAYS = r14_2to7_DAYS;
	}
	public BigDecimal getR14_8TO14_DAYS() {
		return R14_8TO14_DAYS;
	}
	public void setR14_8TO14_DAYS(BigDecimal r14_8to14_DAYS) {
		R14_8TO14_DAYS = r14_8to14_DAYS;
	}
	public BigDecimal getR14_15TO30_DAYS() {
		return R14_15TO30_DAYS;
	}
	public void setR14_15TO30_DAYS(BigDecimal r14_15to30_DAYS) {
		R14_15TO30_DAYS = r14_15to30_DAYS;
	}
	public BigDecimal getR14_31DAYS_UPTO_2MONTHS() {
		return R14_31DAYS_UPTO_2MONTHS;
	}
	public void setR14_31DAYS_UPTO_2MONTHS(BigDecimal r14_31days_UPTO_2MONTHS) {
		R14_31DAYS_UPTO_2MONTHS = r14_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR14_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R14_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR14_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r14_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R14_MORETHAN_2MONTHS_UPTO_3MONHTS = r14_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR14_OVER_3MONTHS_UPTO_6MONTHS() {
		return R14_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR14_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r14_OVER_3MONTHS_UPTO_6MONTHS) {
		R14_OVER_3MONTHS_UPTO_6MONTHS = r14_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR14_OVER_6MONTHS_UPTO_1YEAR() {
		return R14_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR14_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r14_OVER_6MONTHS_UPTO_1YEAR) {
		R14_OVER_6MONTHS_UPTO_1YEAR = r14_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR14_OVER_1YEAR_UPTO_3YEARS() {
		return R14_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR14_OVER_1YEAR_UPTO_3YEARS(BigDecimal r14_OVER_1YEAR_UPTO_3YEARS) {
		R14_OVER_1YEAR_UPTO_3YEARS = r14_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR14_OVER_3YEARS_UPTO_5YEARS() {
		return R14_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR14_OVER_3YEARS_UPTO_5YEARS(BigDecimal r14_OVER_3YEARS_UPTO_5YEARS) {
		R14_OVER_3YEARS_UPTO_5YEARS = r14_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR14_OVER_5YEARS() {
		return R14_OVER_5YEARS;
	}
	public void setR14_OVER_5YEARS(BigDecimal r14_OVER_5YEARS) {
		R14_OVER_5YEARS = r14_OVER_5YEARS;
	}
	public BigDecimal getR14_TOTAL() {
		return R14_TOTAL;
	}
	public void setR14_TOTAL(BigDecimal r14_TOTAL) {
		R14_TOTAL = r14_TOTAL;
	}
	public BigDecimal getR15_1_DAY() {
		return R15_1_DAY;
	}
	public void setR15_1_DAY(BigDecimal r15_1_DAY) {
		R15_1_DAY = r15_1_DAY;
	}
	public BigDecimal getR15_2TO7_DAYS() {
		return R15_2TO7_DAYS;
	}
	public void setR15_2TO7_DAYS(BigDecimal r15_2to7_DAYS) {
		R15_2TO7_DAYS = r15_2to7_DAYS;
	}
	public BigDecimal getR15_8TO14_DAYS() {
		return R15_8TO14_DAYS;
	}
	public void setR15_8TO14_DAYS(BigDecimal r15_8to14_DAYS) {
		R15_8TO14_DAYS = r15_8to14_DAYS;
	}
	public BigDecimal getR15_15TO30_DAYS() {
		return R15_15TO30_DAYS;
	}
	public void setR15_15TO30_DAYS(BigDecimal r15_15to30_DAYS) {
		R15_15TO30_DAYS = r15_15to30_DAYS;
	}
	public BigDecimal getR15_31DAYS_UPTO_2MONTHS() {
		return R15_31DAYS_UPTO_2MONTHS;
	}
	public void setR15_31DAYS_UPTO_2MONTHS(BigDecimal r15_31days_UPTO_2MONTHS) {
		R15_31DAYS_UPTO_2MONTHS = r15_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR15_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R15_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR15_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r15_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R15_MORETHAN_2MONTHS_UPTO_3MONHTS = r15_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR15_OVER_3MONTHS_UPTO_6MONTHS() {
		return R15_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR15_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r15_OVER_3MONTHS_UPTO_6MONTHS) {
		R15_OVER_3MONTHS_UPTO_6MONTHS = r15_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR15_OVER_6MONTHS_UPTO_1YEAR() {
		return R15_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR15_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r15_OVER_6MONTHS_UPTO_1YEAR) {
		R15_OVER_6MONTHS_UPTO_1YEAR = r15_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR15_OVER_1YEAR_UPTO_3YEARS() {
		return R15_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR15_OVER_1YEAR_UPTO_3YEARS(BigDecimal r15_OVER_1YEAR_UPTO_3YEARS) {
		R15_OVER_1YEAR_UPTO_3YEARS = r15_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR15_OVER_3YEARS_UPTO_5YEARS() {
		return R15_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR15_OVER_3YEARS_UPTO_5YEARS(BigDecimal r15_OVER_3YEARS_UPTO_5YEARS) {
		R15_OVER_3YEARS_UPTO_5YEARS = r15_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR15_OVER_5YEARS() {
		return R15_OVER_5YEARS;
	}
	public void setR15_OVER_5YEARS(BigDecimal r15_OVER_5YEARS) {
		R15_OVER_5YEARS = r15_OVER_5YEARS;
	}
	public BigDecimal getR15_TOTAL() {
		return R15_TOTAL;
	}
	public void setR15_TOTAL(BigDecimal r15_TOTAL) {
		R15_TOTAL = r15_TOTAL;
	}
	public BigDecimal getR16_1_DAY() {
		return R16_1_DAY;
	}
	public void setR16_1_DAY(BigDecimal r16_1_DAY) {
		R16_1_DAY = r16_1_DAY;
	}
	public BigDecimal getR16_2TO7_DAYS() {
		return R16_2TO7_DAYS;
	}
	public void setR16_2TO7_DAYS(BigDecimal r16_2to7_DAYS) {
		R16_2TO7_DAYS = r16_2to7_DAYS;
	}
	public BigDecimal getR16_8TO14_DAYS() {
		return R16_8TO14_DAYS;
	}
	public void setR16_8TO14_DAYS(BigDecimal r16_8to14_DAYS) {
		R16_8TO14_DAYS = r16_8to14_DAYS;
	}
	public BigDecimal getR16_15TO30_DAYS() {
		return R16_15TO30_DAYS;
	}
	public void setR16_15TO30_DAYS(BigDecimal r16_15to30_DAYS) {
		R16_15TO30_DAYS = r16_15to30_DAYS;
	}
	public BigDecimal getR16_31DAYS_UPTO_2MONTHS() {
		return R16_31DAYS_UPTO_2MONTHS;
	}
	public void setR16_31DAYS_UPTO_2MONTHS(BigDecimal r16_31days_UPTO_2MONTHS) {
		R16_31DAYS_UPTO_2MONTHS = r16_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR16_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R16_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR16_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r16_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R16_MORETHAN_2MONTHS_UPTO_3MONHTS = r16_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR16_OVER_3MONTHS_UPTO_6MONTHS() {
		return R16_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR16_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r16_OVER_3MONTHS_UPTO_6MONTHS) {
		R16_OVER_3MONTHS_UPTO_6MONTHS = r16_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR16_OVER_6MONTHS_UPTO_1YEAR() {
		return R16_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR16_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r16_OVER_6MONTHS_UPTO_1YEAR) {
		R16_OVER_6MONTHS_UPTO_1YEAR = r16_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR16_OVER_1YEAR_UPTO_3YEARS() {
		return R16_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR16_OVER_1YEAR_UPTO_3YEARS(BigDecimal r16_OVER_1YEAR_UPTO_3YEARS) {
		R16_OVER_1YEAR_UPTO_3YEARS = r16_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR16_OVER_3YEARS_UPTO_5YEARS() {
		return R16_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR16_OVER_3YEARS_UPTO_5YEARS(BigDecimal r16_OVER_3YEARS_UPTO_5YEARS) {
		R16_OVER_3YEARS_UPTO_5YEARS = r16_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR16_OVER_5YEARS() {
		return R16_OVER_5YEARS;
	}
	public void setR16_OVER_5YEARS(BigDecimal r16_OVER_5YEARS) {
		R16_OVER_5YEARS = r16_OVER_5YEARS;
	}
	public BigDecimal getR16_TOTAL() {
		return R16_TOTAL;
	}
	public void setR16_TOTAL(BigDecimal r16_TOTAL) {
		R16_TOTAL = r16_TOTAL;
	}
	public BigDecimal getR17_1_DAY() {
		return R17_1_DAY;
	}
	public void setR17_1_DAY(BigDecimal r17_1_DAY) {
		R17_1_DAY = r17_1_DAY;
	}
	public BigDecimal getR17_2TO7_DAYS() {
		return R17_2TO7_DAYS;
	}
	public void setR17_2TO7_DAYS(BigDecimal r17_2to7_DAYS) {
		R17_2TO7_DAYS = r17_2to7_DAYS;
	}
	public BigDecimal getR17_8TO14_DAYS() {
		return R17_8TO14_DAYS;
	}
	public void setR17_8TO14_DAYS(BigDecimal r17_8to14_DAYS) {
		R17_8TO14_DAYS = r17_8to14_DAYS;
	}
	public BigDecimal getR17_15TO30_DAYS() {
		return R17_15TO30_DAYS;
	}
	public void setR17_15TO30_DAYS(BigDecimal r17_15to30_DAYS) {
		R17_15TO30_DAYS = r17_15to30_DAYS;
	}
	public BigDecimal getR17_31DAYS_UPTO_2MONTHS() {
		return R17_31DAYS_UPTO_2MONTHS;
	}
	public void setR17_31DAYS_UPTO_2MONTHS(BigDecimal r17_31days_UPTO_2MONTHS) {
		R17_31DAYS_UPTO_2MONTHS = r17_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR17_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R17_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR17_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r17_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R17_MORETHAN_2MONTHS_UPTO_3MONHTS = r17_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR17_OVER_3MONTHS_UPTO_6MONTHS() {
		return R17_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR17_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r17_OVER_3MONTHS_UPTO_6MONTHS) {
		R17_OVER_3MONTHS_UPTO_6MONTHS = r17_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR17_OVER_6MONTHS_UPTO_1YEAR() {
		return R17_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR17_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r17_OVER_6MONTHS_UPTO_1YEAR) {
		R17_OVER_6MONTHS_UPTO_1YEAR = r17_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR17_OVER_1YEAR_UPTO_3YEARS() {
		return R17_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR17_OVER_1YEAR_UPTO_3YEARS(BigDecimal r17_OVER_1YEAR_UPTO_3YEARS) {
		R17_OVER_1YEAR_UPTO_3YEARS = r17_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR17_OVER_3YEARS_UPTO_5YEARS() {
		return R17_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR17_OVER_3YEARS_UPTO_5YEARS(BigDecimal r17_OVER_3YEARS_UPTO_5YEARS) {
		R17_OVER_3YEARS_UPTO_5YEARS = r17_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR17_OVER_5YEARS() {
		return R17_OVER_5YEARS;
	}
	public void setR17_OVER_5YEARS(BigDecimal r17_OVER_5YEARS) {
		R17_OVER_5YEARS = r17_OVER_5YEARS;
	}
	public BigDecimal getR17_TOTAL() {
		return R17_TOTAL;
	}
	public void setR17_TOTAL(BigDecimal r17_TOTAL) {
		R17_TOTAL = r17_TOTAL;
	}
	public BigDecimal getR18_1_DAY() {
		return R18_1_DAY;
	}
	public void setR18_1_DAY(BigDecimal r18_1_DAY) {
		R18_1_DAY = r18_1_DAY;
	}
	public BigDecimal getR18_2TO7_DAYS() {
		return R18_2TO7_DAYS;
	}
	public void setR18_2TO7_DAYS(BigDecimal r18_2to7_DAYS) {
		R18_2TO7_DAYS = r18_2to7_DAYS;
	}
	public BigDecimal getR18_8TO14_DAYS() {
		return R18_8TO14_DAYS;
	}
	public void setR18_8TO14_DAYS(BigDecimal r18_8to14_DAYS) {
		R18_8TO14_DAYS = r18_8to14_DAYS;
	}
	public BigDecimal getR18_15TO30_DAYS() {
		return R18_15TO30_DAYS;
	}
	public void setR18_15TO30_DAYS(BigDecimal r18_15to30_DAYS) {
		R18_15TO30_DAYS = r18_15to30_DAYS;
	}
	public BigDecimal getR18_31DAYS_UPTO_2MONTHS() {
		return R18_31DAYS_UPTO_2MONTHS;
	}
	public void setR18_31DAYS_UPTO_2MONTHS(BigDecimal r18_31days_UPTO_2MONTHS) {
		R18_31DAYS_UPTO_2MONTHS = r18_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR18_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R18_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR18_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r18_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R18_MORETHAN_2MONTHS_UPTO_3MONHTS = r18_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR18_OVER_3MONTHS_UPTO_6MONTHS() {
		return R18_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR18_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r18_OVER_3MONTHS_UPTO_6MONTHS) {
		R18_OVER_3MONTHS_UPTO_6MONTHS = r18_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR18_OVER_6MONTHS_UPTO_1YEAR() {
		return R18_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR18_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r18_OVER_6MONTHS_UPTO_1YEAR) {
		R18_OVER_6MONTHS_UPTO_1YEAR = r18_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR18_OVER_1YEAR_UPTO_3YEARS() {
		return R18_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR18_OVER_1YEAR_UPTO_3YEARS(BigDecimal r18_OVER_1YEAR_UPTO_3YEARS) {
		R18_OVER_1YEAR_UPTO_3YEARS = r18_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR18_OVER_3YEARS_UPTO_5YEARS() {
		return R18_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR18_OVER_3YEARS_UPTO_5YEARS(BigDecimal r18_OVER_3YEARS_UPTO_5YEARS) {
		R18_OVER_3YEARS_UPTO_5YEARS = r18_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR18_OVER_5YEARS() {
		return R18_OVER_5YEARS;
	}
	public void setR18_OVER_5YEARS(BigDecimal r18_OVER_5YEARS) {
		R18_OVER_5YEARS = r18_OVER_5YEARS;
	}
	public BigDecimal getR18_TOTAL() {
		return R18_TOTAL;
	}
	public void setR18_TOTAL(BigDecimal r18_TOTAL) {
		R18_TOTAL = r18_TOTAL;
	}
	public BigDecimal getR19_1_DAY() {
		return R19_1_DAY;
	}
	public void setR19_1_DAY(BigDecimal r19_1_DAY) {
		R19_1_DAY = r19_1_DAY;
	}
	public BigDecimal getR19_2TO7_DAYS() {
		return R19_2TO7_DAYS;
	}
	public void setR19_2TO7_DAYS(BigDecimal r19_2to7_DAYS) {
		R19_2TO7_DAYS = r19_2to7_DAYS;
	}
	public BigDecimal getR19_8TO14_DAYS() {
		return R19_8TO14_DAYS;
	}
	public void setR19_8TO14_DAYS(BigDecimal r19_8to14_DAYS) {
		R19_8TO14_DAYS = r19_8to14_DAYS;
	}
	public BigDecimal getR19_15TO30_DAYS() {
		return R19_15TO30_DAYS;
	}
	public void setR19_15TO30_DAYS(BigDecimal r19_15to30_DAYS) {
		R19_15TO30_DAYS = r19_15to30_DAYS;
	}
	public BigDecimal getR19_31DAYS_UPTO_2MONTHS() {
		return R19_31DAYS_UPTO_2MONTHS;
	}
	public void setR19_31DAYS_UPTO_2MONTHS(BigDecimal r19_31days_UPTO_2MONTHS) {
		R19_31DAYS_UPTO_2MONTHS = r19_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR19_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R19_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR19_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r19_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R19_MORETHAN_2MONTHS_UPTO_3MONHTS = r19_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR19_OVER_3MONTHS_UPTO_6MONTHS() {
		return R19_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR19_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r19_OVER_3MONTHS_UPTO_6MONTHS) {
		R19_OVER_3MONTHS_UPTO_6MONTHS = r19_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR19_OVER_6MONTHS_UPTO_1YEAR() {
		return R19_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR19_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r19_OVER_6MONTHS_UPTO_1YEAR) {
		R19_OVER_6MONTHS_UPTO_1YEAR = r19_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR19_OVER_1YEAR_UPTO_3YEARS() {
		return R19_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR19_OVER_1YEAR_UPTO_3YEARS(BigDecimal r19_OVER_1YEAR_UPTO_3YEARS) {
		R19_OVER_1YEAR_UPTO_3YEARS = r19_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR19_OVER_3YEARS_UPTO_5YEARS() {
		return R19_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR19_OVER_3YEARS_UPTO_5YEARS(BigDecimal r19_OVER_3YEARS_UPTO_5YEARS) {
		R19_OVER_3YEARS_UPTO_5YEARS = r19_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR19_OVER_5YEARS() {
		return R19_OVER_5YEARS;
	}
	public void setR19_OVER_5YEARS(BigDecimal r19_OVER_5YEARS) {
		R19_OVER_5YEARS = r19_OVER_5YEARS;
	}
	public BigDecimal getR19_TOTAL() {
		return R19_TOTAL;
	}
	public void setR19_TOTAL(BigDecimal r19_TOTAL) {
		R19_TOTAL = r19_TOTAL;
	}
	public BigDecimal getR20_1_DAY() {
		return R20_1_DAY;
	}
	public void setR20_1_DAY(BigDecimal r20_1_DAY) {
		R20_1_DAY = r20_1_DAY;
	}
	public BigDecimal getR20_2TO7_DAYS() {
		return R20_2TO7_DAYS;
	}
	public void setR20_2TO7_DAYS(BigDecimal r20_2to7_DAYS) {
		R20_2TO7_DAYS = r20_2to7_DAYS;
	}
	public BigDecimal getR20_8TO14_DAYS() {
		return R20_8TO14_DAYS;
	}
	public void setR20_8TO14_DAYS(BigDecimal r20_8to14_DAYS) {
		R20_8TO14_DAYS = r20_8to14_DAYS;
	}
	public BigDecimal getR20_15TO30_DAYS() {
		return R20_15TO30_DAYS;
	}
	public void setR20_15TO30_DAYS(BigDecimal r20_15to30_DAYS) {
		R20_15TO30_DAYS = r20_15to30_DAYS;
	}
	public BigDecimal getR20_31DAYS_UPTO_2MONTHS() {
		return R20_31DAYS_UPTO_2MONTHS;
	}
	public void setR20_31DAYS_UPTO_2MONTHS(BigDecimal r20_31days_UPTO_2MONTHS) {
		R20_31DAYS_UPTO_2MONTHS = r20_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR20_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R20_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR20_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r20_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R20_MORETHAN_2MONTHS_UPTO_3MONHTS = r20_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR20_OVER_3MONTHS_UPTO_6MONTHS() {
		return R20_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR20_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r20_OVER_3MONTHS_UPTO_6MONTHS) {
		R20_OVER_3MONTHS_UPTO_6MONTHS = r20_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR20_OVER_6MONTHS_UPTO_1YEAR() {
		return R20_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR20_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r20_OVER_6MONTHS_UPTO_1YEAR) {
		R20_OVER_6MONTHS_UPTO_1YEAR = r20_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR20_OVER_1YEAR_UPTO_3YEARS() {
		return R20_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR20_OVER_1YEAR_UPTO_3YEARS(BigDecimal r20_OVER_1YEAR_UPTO_3YEARS) {
		R20_OVER_1YEAR_UPTO_3YEARS = r20_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR20_OVER_3YEARS_UPTO_5YEARS() {
		return R20_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR20_OVER_3YEARS_UPTO_5YEARS(BigDecimal r20_OVER_3YEARS_UPTO_5YEARS) {
		R20_OVER_3YEARS_UPTO_5YEARS = r20_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR20_OVER_5YEARS() {
		return R20_OVER_5YEARS;
	}
	public void setR20_OVER_5YEARS(BigDecimal r20_OVER_5YEARS) {
		R20_OVER_5YEARS = r20_OVER_5YEARS;
	}
	public BigDecimal getR20_TOTAL() {
		return R20_TOTAL;
	}
	public void setR20_TOTAL(BigDecimal r20_TOTAL) {
		R20_TOTAL = r20_TOTAL;
	}
	public BigDecimal getR21_1_DAY() {
		return R21_1_DAY;
	}
	public void setR21_1_DAY(BigDecimal r21_1_DAY) {
		R21_1_DAY = r21_1_DAY;
	}
	public BigDecimal getR21_2TO7_DAYS() {
		return R21_2TO7_DAYS;
	}
	public void setR21_2TO7_DAYS(BigDecimal r21_2to7_DAYS) {
		R21_2TO7_DAYS = r21_2to7_DAYS;
	}
	public BigDecimal getR21_8TO14_DAYS() {
		return R21_8TO14_DAYS;
	}
	public void setR21_8TO14_DAYS(BigDecimal r21_8to14_DAYS) {
		R21_8TO14_DAYS = r21_8to14_DAYS;
	}
	public BigDecimal getR21_15TO30_DAYS() {
		return R21_15TO30_DAYS;
	}
	public void setR21_15TO30_DAYS(BigDecimal r21_15to30_DAYS) {
		R21_15TO30_DAYS = r21_15to30_DAYS;
	}
	public BigDecimal getR21_31DAYS_UPTO_2MONTHS() {
		return R21_31DAYS_UPTO_2MONTHS;
	}
	public void setR21_31DAYS_UPTO_2MONTHS(BigDecimal r21_31days_UPTO_2MONTHS) {
		R21_31DAYS_UPTO_2MONTHS = r21_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR21_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R21_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR21_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r21_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R21_MORETHAN_2MONTHS_UPTO_3MONHTS = r21_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR21_OVER_3MONTHS_UPTO_6MONTHS() {
		return R21_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR21_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r21_OVER_3MONTHS_UPTO_6MONTHS) {
		R21_OVER_3MONTHS_UPTO_6MONTHS = r21_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR21_OVER_6MONTHS_UPTO_1YEAR() {
		return R21_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR21_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r21_OVER_6MONTHS_UPTO_1YEAR) {
		R21_OVER_6MONTHS_UPTO_1YEAR = r21_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR21_OVER_1YEAR_UPTO_3YEARS() {
		return R21_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR21_OVER_1YEAR_UPTO_3YEARS(BigDecimal r21_OVER_1YEAR_UPTO_3YEARS) {
		R21_OVER_1YEAR_UPTO_3YEARS = r21_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR21_OVER_3YEARS_UPTO_5YEARS() {
		return R21_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR21_OVER_3YEARS_UPTO_5YEARS(BigDecimal r21_OVER_3YEARS_UPTO_5YEARS) {
		R21_OVER_3YEARS_UPTO_5YEARS = r21_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR21_OVER_5YEARS() {
		return R21_OVER_5YEARS;
	}
	public void setR21_OVER_5YEARS(BigDecimal r21_OVER_5YEARS) {
		R21_OVER_5YEARS = r21_OVER_5YEARS;
	}
	public BigDecimal getR21_TOTAL() {
		return R21_TOTAL;
	}
	public void setR21_TOTAL(BigDecimal r21_TOTAL) {
		R21_TOTAL = r21_TOTAL;
	}
	public BigDecimal getR22_1_DAY() {
		return R22_1_DAY;
	}
	public void setR22_1_DAY(BigDecimal r22_1_DAY) {
		R22_1_DAY = r22_1_DAY;
	}
	public BigDecimal getR22_2TO7_DAYS() {
		return R22_2TO7_DAYS;
	}
	public void setR22_2TO7_DAYS(BigDecimal r22_2to7_DAYS) {
		R22_2TO7_DAYS = r22_2to7_DAYS;
	}
	public BigDecimal getR22_8TO14_DAYS() {
		return R22_8TO14_DAYS;
	}
	public void setR22_8TO14_DAYS(BigDecimal r22_8to14_DAYS) {
		R22_8TO14_DAYS = r22_8to14_DAYS;
	}
	public BigDecimal getR22_15TO30_DAYS() {
		return R22_15TO30_DAYS;
	}
	public void setR22_15TO30_DAYS(BigDecimal r22_15to30_DAYS) {
		R22_15TO30_DAYS = r22_15to30_DAYS;
	}
	public BigDecimal getR22_31DAYS_UPTO_2MONTHS() {
		return R22_31DAYS_UPTO_2MONTHS;
	}
	public void setR22_31DAYS_UPTO_2MONTHS(BigDecimal r22_31days_UPTO_2MONTHS) {
		R22_31DAYS_UPTO_2MONTHS = r22_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR22_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R22_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR22_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r22_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R22_MORETHAN_2MONTHS_UPTO_3MONHTS = r22_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR22_OVER_3MONTHS_UPTO_6MONTHS() {
		return R22_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR22_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r22_OVER_3MONTHS_UPTO_6MONTHS) {
		R22_OVER_3MONTHS_UPTO_6MONTHS = r22_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR22_OVER_6MONTHS_UPTO_1YEAR() {
		return R22_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR22_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r22_OVER_6MONTHS_UPTO_1YEAR) {
		R22_OVER_6MONTHS_UPTO_1YEAR = r22_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR22_OVER_1YEAR_UPTO_3YEARS() {
		return R22_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR22_OVER_1YEAR_UPTO_3YEARS(BigDecimal r22_OVER_1YEAR_UPTO_3YEARS) {
		R22_OVER_1YEAR_UPTO_3YEARS = r22_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR22_OVER_3YEARS_UPTO_5YEARS() {
		return R22_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR22_OVER_3YEARS_UPTO_5YEARS(BigDecimal r22_OVER_3YEARS_UPTO_5YEARS) {
		R22_OVER_3YEARS_UPTO_5YEARS = r22_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR22_OVER_5YEARS() {
		return R22_OVER_5YEARS;
	}
	public void setR22_OVER_5YEARS(BigDecimal r22_OVER_5YEARS) {
		R22_OVER_5YEARS = r22_OVER_5YEARS;
	}
	public BigDecimal getR22_TOTAL() {
		return R22_TOTAL;
	}
	public void setR22_TOTAL(BigDecimal r22_TOTAL) {
		R22_TOTAL = r22_TOTAL;
	}
	public BigDecimal getR23_1_DAY() {
		return R23_1_DAY;
	}
	public void setR23_1_DAY(BigDecimal r23_1_DAY) {
		R23_1_DAY = r23_1_DAY;
	}
	public BigDecimal getR23_2TO7_DAYS() {
		return R23_2TO7_DAYS;
	}
	public void setR23_2TO7_DAYS(BigDecimal r23_2to7_DAYS) {
		R23_2TO7_DAYS = r23_2to7_DAYS;
	}
	public BigDecimal getR23_8TO14_DAYS() {
		return R23_8TO14_DAYS;
	}
	public void setR23_8TO14_DAYS(BigDecimal r23_8to14_DAYS) {
		R23_8TO14_DAYS = r23_8to14_DAYS;
	}
	public BigDecimal getR23_15TO30_DAYS() {
		return R23_15TO30_DAYS;
	}
	public void setR23_15TO30_DAYS(BigDecimal r23_15to30_DAYS) {
		R23_15TO30_DAYS = r23_15to30_DAYS;
	}
	public BigDecimal getR23_31DAYS_UPTO_2MONTHS() {
		return R23_31DAYS_UPTO_2MONTHS;
	}
	public void setR23_31DAYS_UPTO_2MONTHS(BigDecimal r23_31days_UPTO_2MONTHS) {
		R23_31DAYS_UPTO_2MONTHS = r23_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR23_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R23_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR23_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r23_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R23_MORETHAN_2MONTHS_UPTO_3MONHTS = r23_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR23_OVER_3MONTHS_UPTO_6MONTHS() {
		return R23_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR23_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r23_OVER_3MONTHS_UPTO_6MONTHS) {
		R23_OVER_3MONTHS_UPTO_6MONTHS = r23_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR23_OVER_6MONTHS_UPTO_1YEAR() {
		return R23_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR23_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r23_OVER_6MONTHS_UPTO_1YEAR) {
		R23_OVER_6MONTHS_UPTO_1YEAR = r23_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR23_OVER_1YEAR_UPTO_3YEARS() {
		return R23_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR23_OVER_1YEAR_UPTO_3YEARS(BigDecimal r23_OVER_1YEAR_UPTO_3YEARS) {
		R23_OVER_1YEAR_UPTO_3YEARS = r23_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR23_OVER_3YEARS_UPTO_5YEARS() {
		return R23_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR23_OVER_3YEARS_UPTO_5YEARS(BigDecimal r23_OVER_3YEARS_UPTO_5YEARS) {
		R23_OVER_3YEARS_UPTO_5YEARS = r23_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR23_OVER_5YEARS() {
		return R23_OVER_5YEARS;
	}
	public void setR23_OVER_5YEARS(BigDecimal r23_OVER_5YEARS) {
		R23_OVER_5YEARS = r23_OVER_5YEARS;
	}
	public BigDecimal getR23_TOTAL() {
		return R23_TOTAL;
	}
	public void setR23_TOTAL(BigDecimal r23_TOTAL) {
		R23_TOTAL = r23_TOTAL;
	}
	public BigDecimal getR24_1_DAY() {
		return R24_1_DAY;
	}
	public void setR24_1_DAY(BigDecimal r24_1_DAY) {
		R24_1_DAY = r24_1_DAY;
	}
	public BigDecimal getR24_2TO7_DAYS() {
		return R24_2TO7_DAYS;
	}
	public void setR24_2TO7_DAYS(BigDecimal r24_2to7_DAYS) {
		R24_2TO7_DAYS = r24_2to7_DAYS;
	}
	public BigDecimal getR24_8TO14_DAYS() {
		return R24_8TO14_DAYS;
	}
	public void setR24_8TO14_DAYS(BigDecimal r24_8to14_DAYS) {
		R24_8TO14_DAYS = r24_8to14_DAYS;
	}
	public BigDecimal getR24_15TO30_DAYS() {
		return R24_15TO30_DAYS;
	}
	public void setR24_15TO30_DAYS(BigDecimal r24_15to30_DAYS) {
		R24_15TO30_DAYS = r24_15to30_DAYS;
	}
	public BigDecimal getR24_31DAYS_UPTO_2MONTHS() {
		return R24_31DAYS_UPTO_2MONTHS;
	}
	public void setR24_31DAYS_UPTO_2MONTHS(BigDecimal r24_31days_UPTO_2MONTHS) {
		R24_31DAYS_UPTO_2MONTHS = r24_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR24_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R24_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR24_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r24_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R24_MORETHAN_2MONTHS_UPTO_3MONHTS = r24_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR24_OVER_3MONTHS_UPTO_6MONTHS() {
		return R24_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR24_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r24_OVER_3MONTHS_UPTO_6MONTHS) {
		R24_OVER_3MONTHS_UPTO_6MONTHS = r24_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR24_OVER_6MONTHS_UPTO_1YEAR() {
		return R24_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR24_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r24_OVER_6MONTHS_UPTO_1YEAR) {
		R24_OVER_6MONTHS_UPTO_1YEAR = r24_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR24_OVER_1YEAR_UPTO_3YEARS() {
		return R24_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR24_OVER_1YEAR_UPTO_3YEARS(BigDecimal r24_OVER_1YEAR_UPTO_3YEARS) {
		R24_OVER_1YEAR_UPTO_3YEARS = r24_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR24_OVER_3YEARS_UPTO_5YEARS() {
		return R24_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR24_OVER_3YEARS_UPTO_5YEARS(BigDecimal r24_OVER_3YEARS_UPTO_5YEARS) {
		R24_OVER_3YEARS_UPTO_5YEARS = r24_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR24_OVER_5YEARS() {
		return R24_OVER_5YEARS;
	}
	public void setR24_OVER_5YEARS(BigDecimal r24_OVER_5YEARS) {
		R24_OVER_5YEARS = r24_OVER_5YEARS;
	}
	public BigDecimal getR24_TOTAL() {
		return R24_TOTAL;
	}
	public void setR24_TOTAL(BigDecimal r24_TOTAL) {
		R24_TOTAL = r24_TOTAL;
	}
	public BigDecimal getR25_1_DAY() {
		return R25_1_DAY;
	}
	public void setR25_1_DAY(BigDecimal r25_1_DAY) {
		R25_1_DAY = r25_1_DAY;
	}
	public BigDecimal getR25_2TO7_DAYS() {
		return R25_2TO7_DAYS;
	}
	public void setR25_2TO7_DAYS(BigDecimal r25_2to7_DAYS) {
		R25_2TO7_DAYS = r25_2to7_DAYS;
	}
	public BigDecimal getR25_8TO14_DAYS() {
		return R25_8TO14_DAYS;
	}
	public void setR25_8TO14_DAYS(BigDecimal r25_8to14_DAYS) {
		R25_8TO14_DAYS = r25_8to14_DAYS;
	}
	public BigDecimal getR25_15TO30_DAYS() {
		return R25_15TO30_DAYS;
	}
	public void setR25_15TO30_DAYS(BigDecimal r25_15to30_DAYS) {
		R25_15TO30_DAYS = r25_15to30_DAYS;
	}
	public BigDecimal getR25_31DAYS_UPTO_2MONTHS() {
		return R25_31DAYS_UPTO_2MONTHS;
	}
	public void setR25_31DAYS_UPTO_2MONTHS(BigDecimal r25_31days_UPTO_2MONTHS) {
		R25_31DAYS_UPTO_2MONTHS = r25_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR25_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R25_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR25_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r25_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R25_MORETHAN_2MONTHS_UPTO_3MONHTS = r25_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR25_OVER_3MONTHS_UPTO_6MONTHS() {
		return R25_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR25_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r25_OVER_3MONTHS_UPTO_6MONTHS) {
		R25_OVER_3MONTHS_UPTO_6MONTHS = r25_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR25_OVER_6MONTHS_UPTO_1YEAR() {
		return R25_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR25_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r25_OVER_6MONTHS_UPTO_1YEAR) {
		R25_OVER_6MONTHS_UPTO_1YEAR = r25_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR25_OVER_1YEAR_UPTO_3YEARS() {
		return R25_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR25_OVER_1YEAR_UPTO_3YEARS(BigDecimal r25_OVER_1YEAR_UPTO_3YEARS) {
		R25_OVER_1YEAR_UPTO_3YEARS = r25_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR25_OVER_3YEARS_UPTO_5YEARS() {
		return R25_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR25_OVER_3YEARS_UPTO_5YEARS(BigDecimal r25_OVER_3YEARS_UPTO_5YEARS) {
		R25_OVER_3YEARS_UPTO_5YEARS = r25_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR25_OVER_5YEARS() {
		return R25_OVER_5YEARS;
	}
	public void setR25_OVER_5YEARS(BigDecimal r25_OVER_5YEARS) {
		R25_OVER_5YEARS = r25_OVER_5YEARS;
	}
	public BigDecimal getR25_TOTAL() {
		return R25_TOTAL;
	}
	public void setR25_TOTAL(BigDecimal r25_TOTAL) {
		R25_TOTAL = r25_TOTAL;
	}
	public BigDecimal getR26_1_DAY() {
		return R26_1_DAY;
	}
	public void setR26_1_DAY(BigDecimal r26_1_DAY) {
		R26_1_DAY = r26_1_DAY;
	}
	public BigDecimal getR26_2TO7_DAYS() {
		return R26_2TO7_DAYS;
	}
	public void setR26_2TO7_DAYS(BigDecimal r26_2to7_DAYS) {
		R26_2TO7_DAYS = r26_2to7_DAYS;
	}
	public BigDecimal getR26_8TO14_DAYS() {
		return R26_8TO14_DAYS;
	}
	public void setR26_8TO14_DAYS(BigDecimal r26_8to14_DAYS) {
		R26_8TO14_DAYS = r26_8to14_DAYS;
	}
	public BigDecimal getR26_15TO30_DAYS() {
		return R26_15TO30_DAYS;
	}
	public void setR26_15TO30_DAYS(BigDecimal r26_15to30_DAYS) {
		R26_15TO30_DAYS = r26_15to30_DAYS;
	}
	public BigDecimal getR26_31DAYS_UPTO_2MONTHS() {
		return R26_31DAYS_UPTO_2MONTHS;
	}
	public void setR26_31DAYS_UPTO_2MONTHS(BigDecimal r26_31days_UPTO_2MONTHS) {
		R26_31DAYS_UPTO_2MONTHS = r26_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR26_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R26_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR26_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r26_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R26_MORETHAN_2MONTHS_UPTO_3MONHTS = r26_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR26_OVER_3MONTHS_UPTO_6MONTHS() {
		return R26_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR26_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r26_OVER_3MONTHS_UPTO_6MONTHS) {
		R26_OVER_3MONTHS_UPTO_6MONTHS = r26_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR26_OVER_6MONTHS_UPTO_1YEAR() {
		return R26_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR26_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r26_OVER_6MONTHS_UPTO_1YEAR) {
		R26_OVER_6MONTHS_UPTO_1YEAR = r26_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR26_OVER_1YEAR_UPTO_3YEARS() {
		return R26_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR26_OVER_1YEAR_UPTO_3YEARS(BigDecimal r26_OVER_1YEAR_UPTO_3YEARS) {
		R26_OVER_1YEAR_UPTO_3YEARS = r26_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR26_OVER_3YEARS_UPTO_5YEARS() {
		return R26_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR26_OVER_3YEARS_UPTO_5YEARS(BigDecimal r26_OVER_3YEARS_UPTO_5YEARS) {
		R26_OVER_3YEARS_UPTO_5YEARS = r26_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR26_OVER_5YEARS() {
		return R26_OVER_5YEARS;
	}
	public void setR26_OVER_5YEARS(BigDecimal r26_OVER_5YEARS) {
		R26_OVER_5YEARS = r26_OVER_5YEARS;
	}
	public BigDecimal getR26_TOTAL() {
		return R26_TOTAL;
	}
	public void setR26_TOTAL(BigDecimal r26_TOTAL) {
		R26_TOTAL = r26_TOTAL;
	}
	public BigDecimal getR27_1_DAY() {
		return R27_1_DAY;
	}
	public void setR27_1_DAY(BigDecimal r27_1_DAY) {
		R27_1_DAY = r27_1_DAY;
	}
	public BigDecimal getR27_2TO7_DAYS() {
		return R27_2TO7_DAYS;
	}
	public void setR27_2TO7_DAYS(BigDecimal r27_2to7_DAYS) {
		R27_2TO7_DAYS = r27_2to7_DAYS;
	}
	public BigDecimal getR27_8TO14_DAYS() {
		return R27_8TO14_DAYS;
	}
	public void setR27_8TO14_DAYS(BigDecimal r27_8to14_DAYS) {
		R27_8TO14_DAYS = r27_8to14_DAYS;
	}
	public BigDecimal getR27_15TO30_DAYS() {
		return R27_15TO30_DAYS;
	}
	public void setR27_15TO30_DAYS(BigDecimal r27_15to30_DAYS) {
		R27_15TO30_DAYS = r27_15to30_DAYS;
	}
	public BigDecimal getR27_31DAYS_UPTO_2MONTHS() {
		return R27_31DAYS_UPTO_2MONTHS;
	}
	public void setR27_31DAYS_UPTO_2MONTHS(BigDecimal r27_31days_UPTO_2MONTHS) {
		R27_31DAYS_UPTO_2MONTHS = r27_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR27_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R27_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR27_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r27_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R27_MORETHAN_2MONTHS_UPTO_3MONHTS = r27_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR27_OVER_3MONTHS_UPTO_6MONTHS() {
		return R27_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR27_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r27_OVER_3MONTHS_UPTO_6MONTHS) {
		R27_OVER_3MONTHS_UPTO_6MONTHS = r27_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR27_OVER_6MONTHS_UPTO_1YEAR() {
		return R27_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR27_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r27_OVER_6MONTHS_UPTO_1YEAR) {
		R27_OVER_6MONTHS_UPTO_1YEAR = r27_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR27_OVER_1YEAR_UPTO_3YEARS() {
		return R27_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR27_OVER_1YEAR_UPTO_3YEARS(BigDecimal r27_OVER_1YEAR_UPTO_3YEARS) {
		R27_OVER_1YEAR_UPTO_3YEARS = r27_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR27_OVER_3YEARS_UPTO_5YEARS() {
		return R27_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR27_OVER_3YEARS_UPTO_5YEARS(BigDecimal r27_OVER_3YEARS_UPTO_5YEARS) {
		R27_OVER_3YEARS_UPTO_5YEARS = r27_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR27_OVER_5YEARS() {
		return R27_OVER_5YEARS;
	}
	public void setR27_OVER_5YEARS(BigDecimal r27_OVER_5YEARS) {
		R27_OVER_5YEARS = r27_OVER_5YEARS;
	}
	public BigDecimal getR27_TOTAL() {
		return R27_TOTAL;
	}
	public void setR27_TOTAL(BigDecimal r27_TOTAL) {
		R27_TOTAL = r27_TOTAL;
	}
	public BigDecimal getR28_1_DAY() {
		return R28_1_DAY;
	}
	public void setR28_1_DAY(BigDecimal r28_1_DAY) {
		R28_1_DAY = r28_1_DAY;
	}
	public BigDecimal getR28_2TO7_DAYS() {
		return R28_2TO7_DAYS;
	}
	public void setR28_2TO7_DAYS(BigDecimal r28_2to7_DAYS) {
		R28_2TO7_DAYS = r28_2to7_DAYS;
	}
	public BigDecimal getR28_8TO14_DAYS() {
		return R28_8TO14_DAYS;
	}
	public void setR28_8TO14_DAYS(BigDecimal r28_8to14_DAYS) {
		R28_8TO14_DAYS = r28_8to14_DAYS;
	}
	public BigDecimal getR28_15TO30_DAYS() {
		return R28_15TO30_DAYS;
	}
	public void setR28_15TO30_DAYS(BigDecimal r28_15to30_DAYS) {
		R28_15TO30_DAYS = r28_15to30_DAYS;
	}
	public BigDecimal getR28_31DAYS_UPTO_2MONTHS() {
		return R28_31DAYS_UPTO_2MONTHS;
	}
	public void setR28_31DAYS_UPTO_2MONTHS(BigDecimal r28_31days_UPTO_2MONTHS) {
		R28_31DAYS_UPTO_2MONTHS = r28_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR28_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R28_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR28_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r28_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R28_MORETHAN_2MONTHS_UPTO_3MONHTS = r28_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR28_OVER_3MONTHS_UPTO_6MONTHS() {
		return R28_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR28_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r28_OVER_3MONTHS_UPTO_6MONTHS) {
		R28_OVER_3MONTHS_UPTO_6MONTHS = r28_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR28_OVER_6MONTHS_UPTO_1YEAR() {
		return R28_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR28_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r28_OVER_6MONTHS_UPTO_1YEAR) {
		R28_OVER_6MONTHS_UPTO_1YEAR = r28_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR28_OVER_1YEAR_UPTO_3YEARS() {
		return R28_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR28_OVER_1YEAR_UPTO_3YEARS(BigDecimal r28_OVER_1YEAR_UPTO_3YEARS) {
		R28_OVER_1YEAR_UPTO_3YEARS = r28_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR28_OVER_3YEARS_UPTO_5YEARS() {
		return R28_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR28_OVER_3YEARS_UPTO_5YEARS(BigDecimal r28_OVER_3YEARS_UPTO_5YEARS) {
		R28_OVER_3YEARS_UPTO_5YEARS = r28_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR28_OVER_5YEARS() {
		return R28_OVER_5YEARS;
	}
	public void setR28_OVER_5YEARS(BigDecimal r28_OVER_5YEARS) {
		R28_OVER_5YEARS = r28_OVER_5YEARS;
	}
	public BigDecimal getR28_TOTAL() {
		return R28_TOTAL;
	}
	public void setR28_TOTAL(BigDecimal r28_TOTAL) {
		R28_TOTAL = r28_TOTAL;
	}
	public BigDecimal getR29_1_DAY() {
		return R29_1_DAY;
	}
	public void setR29_1_DAY(BigDecimal r29_1_DAY) {
		R29_1_DAY = r29_1_DAY;
	}
	public BigDecimal getR29_2TO7_DAYS() {
		return R29_2TO7_DAYS;
	}
	public void setR29_2TO7_DAYS(BigDecimal r29_2to7_DAYS) {
		R29_2TO7_DAYS = r29_2to7_DAYS;
	}
	public BigDecimal getR29_8TO14_DAYS() {
		return R29_8TO14_DAYS;
	}
	public void setR29_8TO14_DAYS(BigDecimal r29_8to14_DAYS) {
		R29_8TO14_DAYS = r29_8to14_DAYS;
	}
	public BigDecimal getR29_15TO30_DAYS() {
		return R29_15TO30_DAYS;
	}
	public void setR29_15TO30_DAYS(BigDecimal r29_15to30_DAYS) {
		R29_15TO30_DAYS = r29_15to30_DAYS;
	}
	public BigDecimal getR29_31DAYS_UPTO_2MONTHS() {
		return R29_31DAYS_UPTO_2MONTHS;
	}
	public void setR29_31DAYS_UPTO_2MONTHS(BigDecimal r29_31days_UPTO_2MONTHS) {
		R29_31DAYS_UPTO_2MONTHS = r29_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR29_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R29_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR29_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r29_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R29_MORETHAN_2MONTHS_UPTO_3MONHTS = r29_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR29_OVER_3MONTHS_UPTO_6MONTHS() {
		return R29_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR29_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r29_OVER_3MONTHS_UPTO_6MONTHS) {
		R29_OVER_3MONTHS_UPTO_6MONTHS = r29_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR29_OVER_6MONTHS_UPTO_1YEAR() {
		return R29_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR29_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r29_OVER_6MONTHS_UPTO_1YEAR) {
		R29_OVER_6MONTHS_UPTO_1YEAR = r29_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR29_OVER_1YEAR_UPTO_3YEARS() {
		return R29_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR29_OVER_1YEAR_UPTO_3YEARS(BigDecimal r29_OVER_1YEAR_UPTO_3YEARS) {
		R29_OVER_1YEAR_UPTO_3YEARS = r29_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR29_OVER_3YEARS_UPTO_5YEARS() {
		return R29_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR29_OVER_3YEARS_UPTO_5YEARS(BigDecimal r29_OVER_3YEARS_UPTO_5YEARS) {
		R29_OVER_3YEARS_UPTO_5YEARS = r29_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR29_OVER_5YEARS() {
		return R29_OVER_5YEARS;
	}
	public void setR29_OVER_5YEARS(BigDecimal r29_OVER_5YEARS) {
		R29_OVER_5YEARS = r29_OVER_5YEARS;
	}
	public BigDecimal getR29_TOTAL() {
		return R29_TOTAL;
	}
	public void setR29_TOTAL(BigDecimal r29_TOTAL) {
		R29_TOTAL = r29_TOTAL;
	}
	public BigDecimal getR30_1_DAY() {
		return R30_1_DAY;
	}
	public void setR30_1_DAY(BigDecimal r30_1_DAY) {
		R30_1_DAY = r30_1_DAY;
	}
	public BigDecimal getR30_2TO7_DAYS() {
		return R30_2TO7_DAYS;
	}
	public void setR30_2TO7_DAYS(BigDecimal r30_2to7_DAYS) {
		R30_2TO7_DAYS = r30_2to7_DAYS;
	}
	public BigDecimal getR30_8TO14_DAYS() {
		return R30_8TO14_DAYS;
	}
	public void setR30_8TO14_DAYS(BigDecimal r30_8to14_DAYS) {
		R30_8TO14_DAYS = r30_8to14_DAYS;
	}
	public BigDecimal getR30_15TO30_DAYS() {
		return R30_15TO30_DAYS;
	}
	public void setR30_15TO30_DAYS(BigDecimal r30_15to30_DAYS) {
		R30_15TO30_DAYS = r30_15to30_DAYS;
	}
	public BigDecimal getR30_31DAYS_UPTO_2MONTHS() {
		return R30_31DAYS_UPTO_2MONTHS;
	}
	public void setR30_31DAYS_UPTO_2MONTHS(BigDecimal r30_31days_UPTO_2MONTHS) {
		R30_31DAYS_UPTO_2MONTHS = r30_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR30_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R30_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR30_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r30_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R30_MORETHAN_2MONTHS_UPTO_3MONHTS = r30_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR30_OVER_3MONTHS_UPTO_6MONTHS() {
		return R30_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR30_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r30_OVER_3MONTHS_UPTO_6MONTHS) {
		R30_OVER_3MONTHS_UPTO_6MONTHS = r30_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR30_OVER_6MONTHS_UPTO_1YEAR() {
		return R30_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR30_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r30_OVER_6MONTHS_UPTO_1YEAR) {
		R30_OVER_6MONTHS_UPTO_1YEAR = r30_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR30_OVER_1YEAR_UPTO_3YEARS() {
		return R30_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR30_OVER_1YEAR_UPTO_3YEARS(BigDecimal r30_OVER_1YEAR_UPTO_3YEARS) {
		R30_OVER_1YEAR_UPTO_3YEARS = r30_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR30_OVER_3YEARS_UPTO_5YEARS() {
		return R30_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR30_OVER_3YEARS_UPTO_5YEARS(BigDecimal r30_OVER_3YEARS_UPTO_5YEARS) {
		R30_OVER_3YEARS_UPTO_5YEARS = r30_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR30_OVER_5YEARS() {
		return R30_OVER_5YEARS;
	}
	public void setR30_OVER_5YEARS(BigDecimal r30_OVER_5YEARS) {
		R30_OVER_5YEARS = r30_OVER_5YEARS;
	}
	public BigDecimal getR30_TOTAL() {
		return R30_TOTAL;
	}
	public void setR30_TOTAL(BigDecimal r30_TOTAL) {
		R30_TOTAL = r30_TOTAL;
	}
	public BigDecimal getR31_1_DAY() {
		return R31_1_DAY;
	}
	public void setR31_1_DAY(BigDecimal r31_1_DAY) {
		R31_1_DAY = r31_1_DAY;
	}
	public BigDecimal getR31_2TO7_DAYS() {
		return R31_2TO7_DAYS;
	}
	public void setR31_2TO7_DAYS(BigDecimal r31_2to7_DAYS) {
		R31_2TO7_DAYS = r31_2to7_DAYS;
	}
	public BigDecimal getR31_8TO14_DAYS() {
		return R31_8TO14_DAYS;
	}
	public void setR31_8TO14_DAYS(BigDecimal r31_8to14_DAYS) {
		R31_8TO14_DAYS = r31_8to14_DAYS;
	}
	public BigDecimal getR31_15TO30_DAYS() {
		return R31_15TO30_DAYS;
	}
	public void setR31_15TO30_DAYS(BigDecimal r31_15to30_DAYS) {
		R31_15TO30_DAYS = r31_15to30_DAYS;
	}
	public BigDecimal getR31_31DAYS_UPTO_2MONTHS() {
		return R31_31DAYS_UPTO_2MONTHS;
	}
	public void setR31_31DAYS_UPTO_2MONTHS(BigDecimal r31_31days_UPTO_2MONTHS) {
		R31_31DAYS_UPTO_2MONTHS = r31_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR31_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R31_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR31_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r31_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R31_MORETHAN_2MONTHS_UPTO_3MONHTS = r31_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR31_OVER_3MONTHS_UPTO_6MONTHS() {
		return R31_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR31_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r31_OVER_3MONTHS_UPTO_6MONTHS) {
		R31_OVER_3MONTHS_UPTO_6MONTHS = r31_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR31_OVER_6MONTHS_UPTO_1YEAR() {
		return R31_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR31_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r31_OVER_6MONTHS_UPTO_1YEAR) {
		R31_OVER_6MONTHS_UPTO_1YEAR = r31_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR31_OVER_1YEAR_UPTO_3YEARS() {
		return R31_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR31_OVER_1YEAR_UPTO_3YEARS(BigDecimal r31_OVER_1YEAR_UPTO_3YEARS) {
		R31_OVER_1YEAR_UPTO_3YEARS = r31_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR31_OVER_3YEARS_UPTO_5YEARS() {
		return R31_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR31_OVER_3YEARS_UPTO_5YEARS(BigDecimal r31_OVER_3YEARS_UPTO_5YEARS) {
		R31_OVER_3YEARS_UPTO_5YEARS = r31_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR31_OVER_5YEARS() {
		return R31_OVER_5YEARS;
	}
	public void setR31_OVER_5YEARS(BigDecimal r31_OVER_5YEARS) {
		R31_OVER_5YEARS = r31_OVER_5YEARS;
	}
	public BigDecimal getR31_TOTAL() {
		return R31_TOTAL;
	}
	public void setR31_TOTAL(BigDecimal r31_TOTAL) {
		R31_TOTAL = r31_TOTAL;
	}
	public BigDecimal getR32_1_DAY() {
		return R32_1_DAY;
	}
	public void setR32_1_DAY(BigDecimal r32_1_DAY) {
		R32_1_DAY = r32_1_DAY;
	}
	public BigDecimal getR32_2TO7_DAYS() {
		return R32_2TO7_DAYS;
	}
	public void setR32_2TO7_DAYS(BigDecimal r32_2to7_DAYS) {
		R32_2TO7_DAYS = r32_2to7_DAYS;
	}
	public BigDecimal getR32_8TO14_DAYS() {
		return R32_8TO14_DAYS;
	}
	public void setR32_8TO14_DAYS(BigDecimal r32_8to14_DAYS) {
		R32_8TO14_DAYS = r32_8to14_DAYS;
	}
	public BigDecimal getR32_15TO30_DAYS() {
		return R32_15TO30_DAYS;
	}
	public void setR32_15TO30_DAYS(BigDecimal r32_15to30_DAYS) {
		R32_15TO30_DAYS = r32_15to30_DAYS;
	}
	public BigDecimal getR32_31DAYS_UPTO_2MONTHS() {
		return R32_31DAYS_UPTO_2MONTHS;
	}
	public void setR32_31DAYS_UPTO_2MONTHS(BigDecimal r32_31days_UPTO_2MONTHS) {
		R32_31DAYS_UPTO_2MONTHS = r32_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR32_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R32_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR32_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r32_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R32_MORETHAN_2MONTHS_UPTO_3MONHTS = r32_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR32_OVER_3MONTHS_UPTO_6MONTHS() {
		return R32_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR32_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r32_OVER_3MONTHS_UPTO_6MONTHS) {
		R32_OVER_3MONTHS_UPTO_6MONTHS = r32_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR32_OVER_6MONTHS_UPTO_1YEAR() {
		return R32_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR32_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r32_OVER_6MONTHS_UPTO_1YEAR) {
		R32_OVER_6MONTHS_UPTO_1YEAR = r32_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR32_OVER_1YEAR_UPTO_3YEARS() {
		return R32_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR32_OVER_1YEAR_UPTO_3YEARS(BigDecimal r32_OVER_1YEAR_UPTO_3YEARS) {
		R32_OVER_1YEAR_UPTO_3YEARS = r32_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR32_OVER_3YEARS_UPTO_5YEARS() {
		return R32_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR32_OVER_3YEARS_UPTO_5YEARS(BigDecimal r32_OVER_3YEARS_UPTO_5YEARS) {
		R32_OVER_3YEARS_UPTO_5YEARS = r32_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR32_OVER_5YEARS() {
		return R32_OVER_5YEARS;
	}
	public void setR32_OVER_5YEARS(BigDecimal r32_OVER_5YEARS) {
		R32_OVER_5YEARS = r32_OVER_5YEARS;
	}
	public BigDecimal getR32_TOTAL() {
		return R32_TOTAL;
	}
	public void setR32_TOTAL(BigDecimal r32_TOTAL) {
		R32_TOTAL = r32_TOTAL;
	}
	public BigDecimal getR33_1_DAY() {
		return R33_1_DAY;
	}
	public void setR33_1_DAY(BigDecimal r33_1_DAY) {
		R33_1_DAY = r33_1_DAY;
	}
	public BigDecimal getR33_2TO7_DAYS() {
		return R33_2TO7_DAYS;
	}
	public void setR33_2TO7_DAYS(BigDecimal r33_2to7_DAYS) {
		R33_2TO7_DAYS = r33_2to7_DAYS;
	}
	public BigDecimal getR33_8TO14_DAYS() {
		return R33_8TO14_DAYS;
	}
	public void setR33_8TO14_DAYS(BigDecimal r33_8to14_DAYS) {
		R33_8TO14_DAYS = r33_8to14_DAYS;
	}
	public BigDecimal getR33_15TO30_DAYS() {
		return R33_15TO30_DAYS;
	}
	public void setR33_15TO30_DAYS(BigDecimal r33_15to30_DAYS) {
		R33_15TO30_DAYS = r33_15to30_DAYS;
	}
	public BigDecimal getR33_31DAYS_UPTO_2MONTHS() {
		return R33_31DAYS_UPTO_2MONTHS;
	}
	public void setR33_31DAYS_UPTO_2MONTHS(BigDecimal r33_31days_UPTO_2MONTHS) {
		R33_31DAYS_UPTO_2MONTHS = r33_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR33_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R33_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR33_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r33_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R33_MORETHAN_2MONTHS_UPTO_3MONHTS = r33_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR33_OVER_3MONTHS_UPTO_6MONTHS() {
		return R33_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR33_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r33_OVER_3MONTHS_UPTO_6MONTHS) {
		R33_OVER_3MONTHS_UPTO_6MONTHS = r33_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR33_OVER_6MONTHS_UPTO_1YEAR() {
		return R33_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR33_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r33_OVER_6MONTHS_UPTO_1YEAR) {
		R33_OVER_6MONTHS_UPTO_1YEAR = r33_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR33_OVER_1YEAR_UPTO_3YEARS() {
		return R33_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR33_OVER_1YEAR_UPTO_3YEARS(BigDecimal r33_OVER_1YEAR_UPTO_3YEARS) {
		R33_OVER_1YEAR_UPTO_3YEARS = r33_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR33_OVER_3YEARS_UPTO_5YEARS() {
		return R33_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR33_OVER_3YEARS_UPTO_5YEARS(BigDecimal r33_OVER_3YEARS_UPTO_5YEARS) {
		R33_OVER_3YEARS_UPTO_5YEARS = r33_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR33_OVER_5YEARS() {
		return R33_OVER_5YEARS;
	}
	public void setR33_OVER_5YEARS(BigDecimal r33_OVER_5YEARS) {
		R33_OVER_5YEARS = r33_OVER_5YEARS;
	}
	public BigDecimal getR33_TOTAL() {
		return R33_TOTAL;
	}
	public void setR33_TOTAL(BigDecimal r33_TOTAL) {
		R33_TOTAL = r33_TOTAL;
	}
	public BigDecimal getR34_1_DAY() {
		return R34_1_DAY;
	}
	public void setR34_1_DAY(BigDecimal r34_1_DAY) {
		R34_1_DAY = r34_1_DAY;
	}
	public BigDecimal getR34_2TO7_DAYS() {
		return R34_2TO7_DAYS;
	}
	public void setR34_2TO7_DAYS(BigDecimal r34_2to7_DAYS) {
		R34_2TO7_DAYS = r34_2to7_DAYS;
	}
	public BigDecimal getR34_8TO14_DAYS() {
		return R34_8TO14_DAYS;
	}
	public void setR34_8TO14_DAYS(BigDecimal r34_8to14_DAYS) {
		R34_8TO14_DAYS = r34_8to14_DAYS;
	}
	public BigDecimal getR34_15TO30_DAYS() {
		return R34_15TO30_DAYS;
	}
	public void setR34_15TO30_DAYS(BigDecimal r34_15to30_DAYS) {
		R34_15TO30_DAYS = r34_15to30_DAYS;
	}
	public BigDecimal getR34_31DAYS_UPTO_2MONTHS() {
		return R34_31DAYS_UPTO_2MONTHS;
	}
	public void setR34_31DAYS_UPTO_2MONTHS(BigDecimal r34_31days_UPTO_2MONTHS) {
		R34_31DAYS_UPTO_2MONTHS = r34_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR34_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R34_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR34_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r34_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R34_MORETHAN_2MONTHS_UPTO_3MONHTS = r34_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR34_OVER_3MONTHS_UPTO_6MONTHS() {
		return R34_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR34_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r34_OVER_3MONTHS_UPTO_6MONTHS) {
		R34_OVER_3MONTHS_UPTO_6MONTHS = r34_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR34_OVER_6MONTHS_UPTO_1YEAR() {
		return R34_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR34_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r34_OVER_6MONTHS_UPTO_1YEAR) {
		R34_OVER_6MONTHS_UPTO_1YEAR = r34_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR34_OVER_1YEAR_UPTO_3YEARS() {
		return R34_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR34_OVER_1YEAR_UPTO_3YEARS(BigDecimal r34_OVER_1YEAR_UPTO_3YEARS) {
		R34_OVER_1YEAR_UPTO_3YEARS = r34_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR34_OVER_3YEARS_UPTO_5YEARS() {
		return R34_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR34_OVER_3YEARS_UPTO_5YEARS(BigDecimal r34_OVER_3YEARS_UPTO_5YEARS) {
		R34_OVER_3YEARS_UPTO_5YEARS = r34_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR34_OVER_5YEARS() {
		return R34_OVER_5YEARS;
	}
	public void setR34_OVER_5YEARS(BigDecimal r34_OVER_5YEARS) {
		R34_OVER_5YEARS = r34_OVER_5YEARS;
	}
	public BigDecimal getR34_TOTAL() {
		return R34_TOTAL;
	}
	public void setR34_TOTAL(BigDecimal r34_TOTAL) {
		R34_TOTAL = r34_TOTAL;
	}
	public BigDecimal getR35_1_DAY() {
		return R35_1_DAY;
	}
	public void setR35_1_DAY(BigDecimal r35_1_DAY) {
		R35_1_DAY = r35_1_DAY;
	}
	public BigDecimal getR35_2TO7_DAYS() {
		return R35_2TO7_DAYS;
	}
	public void setR35_2TO7_DAYS(BigDecimal r35_2to7_DAYS) {
		R35_2TO7_DAYS = r35_2to7_DAYS;
	}
	public BigDecimal getR35_8TO14_DAYS() {
		return R35_8TO14_DAYS;
	}
	public void setR35_8TO14_DAYS(BigDecimal r35_8to14_DAYS) {
		R35_8TO14_DAYS = r35_8to14_DAYS;
	}
	public BigDecimal getR35_15TO30_DAYS() {
		return R35_15TO30_DAYS;
	}
	public void setR35_15TO30_DAYS(BigDecimal r35_15to30_DAYS) {
		R35_15TO30_DAYS = r35_15to30_DAYS;
	}
	public BigDecimal getR35_31DAYS_UPTO_2MONTHS() {
		return R35_31DAYS_UPTO_2MONTHS;
	}
	public void setR35_31DAYS_UPTO_2MONTHS(BigDecimal r35_31days_UPTO_2MONTHS) {
		R35_31DAYS_UPTO_2MONTHS = r35_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR35_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R35_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR35_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r35_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R35_MORETHAN_2MONTHS_UPTO_3MONHTS = r35_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR35_OVER_3MONTHS_UPTO_6MONTHS() {
		return R35_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR35_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r35_OVER_3MONTHS_UPTO_6MONTHS) {
		R35_OVER_3MONTHS_UPTO_6MONTHS = r35_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR35_OVER_6MONTHS_UPTO_1YEAR() {
		return R35_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR35_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r35_OVER_6MONTHS_UPTO_1YEAR) {
		R35_OVER_6MONTHS_UPTO_1YEAR = r35_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR35_OVER_1YEAR_UPTO_3YEARS() {
		return R35_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR35_OVER_1YEAR_UPTO_3YEARS(BigDecimal r35_OVER_1YEAR_UPTO_3YEARS) {
		R35_OVER_1YEAR_UPTO_3YEARS = r35_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR35_OVER_3YEARS_UPTO_5YEARS() {
		return R35_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR35_OVER_3YEARS_UPTO_5YEARS(BigDecimal r35_OVER_3YEARS_UPTO_5YEARS) {
		R35_OVER_3YEARS_UPTO_5YEARS = r35_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR35_OVER_5YEARS() {
		return R35_OVER_5YEARS;
	}
	public void setR35_OVER_5YEARS(BigDecimal r35_OVER_5YEARS) {
		R35_OVER_5YEARS = r35_OVER_5YEARS;
	}
	public BigDecimal getR35_TOTAL() {
		return R35_TOTAL;
	}
	public void setR35_TOTAL(BigDecimal r35_TOTAL) {
		R35_TOTAL = r35_TOTAL;
	}
	public BigDecimal getR36_1_DAY() {
		return R36_1_DAY;
	}
	public void setR36_1_DAY(BigDecimal r36_1_DAY) {
		R36_1_DAY = r36_1_DAY;
	}
	public BigDecimal getR36_2TO7_DAYS() {
		return R36_2TO7_DAYS;
	}
	public void setR36_2TO7_DAYS(BigDecimal r36_2to7_DAYS) {
		R36_2TO7_DAYS = r36_2to7_DAYS;
	}
	public BigDecimal getR36_8TO14_DAYS() {
		return R36_8TO14_DAYS;
	}
	public void setR36_8TO14_DAYS(BigDecimal r36_8to14_DAYS) {
		R36_8TO14_DAYS = r36_8to14_DAYS;
	}
	public BigDecimal getR36_15TO30_DAYS() {
		return R36_15TO30_DAYS;
	}
	public void setR36_15TO30_DAYS(BigDecimal r36_15to30_DAYS) {
		R36_15TO30_DAYS = r36_15to30_DAYS;
	}
	public BigDecimal getR36_31DAYS_UPTO_2MONTHS() {
		return R36_31DAYS_UPTO_2MONTHS;
	}
	public void setR36_31DAYS_UPTO_2MONTHS(BigDecimal r36_31days_UPTO_2MONTHS) {
		R36_31DAYS_UPTO_2MONTHS = r36_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR36_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R36_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR36_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r36_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R36_MORETHAN_2MONTHS_UPTO_3MONHTS = r36_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR36_OVER_3MONTHS_UPTO_6MONTHS() {
		return R36_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR36_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r36_OVER_3MONTHS_UPTO_6MONTHS) {
		R36_OVER_3MONTHS_UPTO_6MONTHS = r36_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR36_OVER_6MONTHS_UPTO_1YEAR() {
		return R36_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR36_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r36_OVER_6MONTHS_UPTO_1YEAR) {
		R36_OVER_6MONTHS_UPTO_1YEAR = r36_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR36_OVER_1YEAR_UPTO_3YEARS() {
		return R36_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR36_OVER_1YEAR_UPTO_3YEARS(BigDecimal r36_OVER_1YEAR_UPTO_3YEARS) {
		R36_OVER_1YEAR_UPTO_3YEARS = r36_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR36_OVER_3YEARS_UPTO_5YEARS() {
		return R36_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR36_OVER_3YEARS_UPTO_5YEARS(BigDecimal r36_OVER_3YEARS_UPTO_5YEARS) {
		R36_OVER_3YEARS_UPTO_5YEARS = r36_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR36_OVER_5YEARS() {
		return R36_OVER_5YEARS;
	}
	public void setR36_OVER_5YEARS(BigDecimal r36_OVER_5YEARS) {
		R36_OVER_5YEARS = r36_OVER_5YEARS;
	}
	public BigDecimal getR36_TOTAL() {
		return R36_TOTAL;
	}
	public void setR36_TOTAL(BigDecimal r36_TOTAL) {
		R36_TOTAL = r36_TOTAL;
	}
	public BigDecimal getR37_1_DAY() {
		return R37_1_DAY;
	}
	public void setR37_1_DAY(BigDecimal r37_1_DAY) {
		R37_1_DAY = r37_1_DAY;
	}
	public BigDecimal getR37_2TO7_DAYS() {
		return R37_2TO7_DAYS;
	}
	public void setR37_2TO7_DAYS(BigDecimal r37_2to7_DAYS) {
		R37_2TO7_DAYS = r37_2to7_DAYS;
	}
	public BigDecimal getR37_8TO14_DAYS() {
		return R37_8TO14_DAYS;
	}
	public void setR37_8TO14_DAYS(BigDecimal r37_8to14_DAYS) {
		R37_8TO14_DAYS = r37_8to14_DAYS;
	}
	public BigDecimal getR37_15TO30_DAYS() {
		return R37_15TO30_DAYS;
	}
	public void setR37_15TO30_DAYS(BigDecimal r37_15to30_DAYS) {
		R37_15TO30_DAYS = r37_15to30_DAYS;
	}
	public BigDecimal getR37_31DAYS_UPTO_2MONTHS() {
		return R37_31DAYS_UPTO_2MONTHS;
	}
	public void setR37_31DAYS_UPTO_2MONTHS(BigDecimal r37_31days_UPTO_2MONTHS) {
		R37_31DAYS_UPTO_2MONTHS = r37_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR37_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R37_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR37_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r37_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R37_MORETHAN_2MONTHS_UPTO_3MONHTS = r37_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR37_OVER_3MONTHS_UPTO_6MONTHS() {
		return R37_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR37_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r37_OVER_3MONTHS_UPTO_6MONTHS) {
		R37_OVER_3MONTHS_UPTO_6MONTHS = r37_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR37_OVER_6MONTHS_UPTO_1YEAR() {
		return R37_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR37_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r37_OVER_6MONTHS_UPTO_1YEAR) {
		R37_OVER_6MONTHS_UPTO_1YEAR = r37_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR37_OVER_1YEAR_UPTO_3YEARS() {
		return R37_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR37_OVER_1YEAR_UPTO_3YEARS(BigDecimal r37_OVER_1YEAR_UPTO_3YEARS) {
		R37_OVER_1YEAR_UPTO_3YEARS = r37_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR37_OVER_3YEARS_UPTO_5YEARS() {
		return R37_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR37_OVER_3YEARS_UPTO_5YEARS(BigDecimal r37_OVER_3YEARS_UPTO_5YEARS) {
		R37_OVER_3YEARS_UPTO_5YEARS = r37_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR37_OVER_5YEARS() {
		return R37_OVER_5YEARS;
	}
	public void setR37_OVER_5YEARS(BigDecimal r37_OVER_5YEARS) {
		R37_OVER_5YEARS = r37_OVER_5YEARS;
	}
	public BigDecimal getR37_TOTAL() {
		return R37_TOTAL;
	}
	public void setR37_TOTAL(BigDecimal r37_TOTAL) {
		R37_TOTAL = r37_TOTAL;
	}
	public BigDecimal getR38_1_DAY() {
		return R38_1_DAY;
	}
	public void setR38_1_DAY(BigDecimal r38_1_DAY) {
		R38_1_DAY = r38_1_DAY;
	}
	public BigDecimal getR38_2TO7_DAYS() {
		return R38_2TO7_DAYS;
	}
	public void setR38_2TO7_DAYS(BigDecimal r38_2to7_DAYS) {
		R38_2TO7_DAYS = r38_2to7_DAYS;
	}
	public BigDecimal getR38_8TO14_DAYS() {
		return R38_8TO14_DAYS;
	}
	public void setR38_8TO14_DAYS(BigDecimal r38_8to14_DAYS) {
		R38_8TO14_DAYS = r38_8to14_DAYS;
	}
	public BigDecimal getR38_15TO30_DAYS() {
		return R38_15TO30_DAYS;
	}
	public void setR38_15TO30_DAYS(BigDecimal r38_15to30_DAYS) {
		R38_15TO30_DAYS = r38_15to30_DAYS;
	}
	public BigDecimal getR38_31DAYS_UPTO_2MONTHS() {
		return R38_31DAYS_UPTO_2MONTHS;
	}
	public void setR38_31DAYS_UPTO_2MONTHS(BigDecimal r38_31days_UPTO_2MONTHS) {
		R38_31DAYS_UPTO_2MONTHS = r38_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR38_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R38_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR38_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r38_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R38_MORETHAN_2MONTHS_UPTO_3MONHTS = r38_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR38_OVER_3MONTHS_UPTO_6MONTHS() {
		return R38_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR38_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r38_OVER_3MONTHS_UPTO_6MONTHS) {
		R38_OVER_3MONTHS_UPTO_6MONTHS = r38_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR38_OVER_6MONTHS_UPTO_1YEAR() {
		return R38_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR38_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r38_OVER_6MONTHS_UPTO_1YEAR) {
		R38_OVER_6MONTHS_UPTO_1YEAR = r38_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR38_OVER_1YEAR_UPTO_3YEARS() {
		return R38_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR38_OVER_1YEAR_UPTO_3YEARS(BigDecimal r38_OVER_1YEAR_UPTO_3YEARS) {
		R38_OVER_1YEAR_UPTO_3YEARS = r38_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR38_OVER_3YEARS_UPTO_5YEARS() {
		return R38_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR38_OVER_3YEARS_UPTO_5YEARS(BigDecimal r38_OVER_3YEARS_UPTO_5YEARS) {
		R38_OVER_3YEARS_UPTO_5YEARS = r38_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR38_OVER_5YEARS() {
		return R38_OVER_5YEARS;
	}
	public void setR38_OVER_5YEARS(BigDecimal r38_OVER_5YEARS) {
		R38_OVER_5YEARS = r38_OVER_5YEARS;
	}
	public BigDecimal getR38_TOTAL() {
		return R38_TOTAL;
	}
	public void setR38_TOTAL(BigDecimal r38_TOTAL) {
		R38_TOTAL = r38_TOTAL;
	}
	public BigDecimal getR39_1_DAY() {
		return R39_1_DAY;
	}
	public void setR39_1_DAY(BigDecimal r39_1_DAY) {
		R39_1_DAY = r39_1_DAY;
	}
	public BigDecimal getR39_2TO7_DAYS() {
		return R39_2TO7_DAYS;
	}
	public void setR39_2TO7_DAYS(BigDecimal r39_2to7_DAYS) {
		R39_2TO7_DAYS = r39_2to7_DAYS;
	}
	public BigDecimal getR39_8TO14_DAYS() {
		return R39_8TO14_DAYS;
	}
	public void setR39_8TO14_DAYS(BigDecimal r39_8to14_DAYS) {
		R39_8TO14_DAYS = r39_8to14_DAYS;
	}
	public BigDecimal getR39_15TO30_DAYS() {
		return R39_15TO30_DAYS;
	}
	public void setR39_15TO30_DAYS(BigDecimal r39_15to30_DAYS) {
		R39_15TO30_DAYS = r39_15to30_DAYS;
	}
	public BigDecimal getR39_31DAYS_UPTO_2MONTHS() {
		return R39_31DAYS_UPTO_2MONTHS;
	}
	public void setR39_31DAYS_UPTO_2MONTHS(BigDecimal r39_31days_UPTO_2MONTHS) {
		R39_31DAYS_UPTO_2MONTHS = r39_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR39_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R39_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR39_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r39_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R39_MORETHAN_2MONTHS_UPTO_3MONHTS = r39_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR39_OVER_3MONTHS_UPTO_6MONTHS() {
		return R39_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR39_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r39_OVER_3MONTHS_UPTO_6MONTHS) {
		R39_OVER_3MONTHS_UPTO_6MONTHS = r39_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR39_OVER_6MONTHS_UPTO_1YEAR() {
		return R39_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR39_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r39_OVER_6MONTHS_UPTO_1YEAR) {
		R39_OVER_6MONTHS_UPTO_1YEAR = r39_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR39_OVER_1YEAR_UPTO_3YEARS() {
		return R39_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR39_OVER_1YEAR_UPTO_3YEARS(BigDecimal r39_OVER_1YEAR_UPTO_3YEARS) {
		R39_OVER_1YEAR_UPTO_3YEARS = r39_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR39_OVER_3YEARS_UPTO_5YEARS() {
		return R39_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR39_OVER_3YEARS_UPTO_5YEARS(BigDecimal r39_OVER_3YEARS_UPTO_5YEARS) {
		R39_OVER_3YEARS_UPTO_5YEARS = r39_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR39_OVER_5YEARS() {
		return R39_OVER_5YEARS;
	}
	public void setR39_OVER_5YEARS(BigDecimal r39_OVER_5YEARS) {
		R39_OVER_5YEARS = r39_OVER_5YEARS;
	}
	public BigDecimal getR39_TOTAL() {
		return R39_TOTAL;
	}
	public void setR39_TOTAL(BigDecimal r39_TOTAL) {
		R39_TOTAL = r39_TOTAL;
	}
	public BigDecimal getR40_1_DAY() {
		return R40_1_DAY;
	}
	public void setR40_1_DAY(BigDecimal r40_1_DAY) {
		R40_1_DAY = r40_1_DAY;
	}
	public BigDecimal getR40_2TO7_DAYS() {
		return R40_2TO7_DAYS;
	}
	public void setR40_2TO7_DAYS(BigDecimal r40_2to7_DAYS) {
		R40_2TO7_DAYS = r40_2to7_DAYS;
	}
	public BigDecimal getR40_8TO14_DAYS() {
		return R40_8TO14_DAYS;
	}
	public void setR40_8TO14_DAYS(BigDecimal r40_8to14_DAYS) {
		R40_8TO14_DAYS = r40_8to14_DAYS;
	}
	public BigDecimal getR40_15TO30_DAYS() {
		return R40_15TO30_DAYS;
	}
	public void setR40_15TO30_DAYS(BigDecimal r40_15to30_DAYS) {
		R40_15TO30_DAYS = r40_15to30_DAYS;
	}
	public BigDecimal getR40_31DAYS_UPTO_2MONTHS() {
		return R40_31DAYS_UPTO_2MONTHS;
	}
	public void setR40_31DAYS_UPTO_2MONTHS(BigDecimal r40_31days_UPTO_2MONTHS) {
		R40_31DAYS_UPTO_2MONTHS = r40_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR40_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R40_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR40_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r40_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R40_MORETHAN_2MONTHS_UPTO_3MONHTS = r40_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR40_OVER_3MONTHS_UPTO_6MONTHS() {
		return R40_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR40_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r40_OVER_3MONTHS_UPTO_6MONTHS) {
		R40_OVER_3MONTHS_UPTO_6MONTHS = r40_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR40_OVER_6MONTHS_UPTO_1YEAR() {
		return R40_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR40_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r40_OVER_6MONTHS_UPTO_1YEAR) {
		R40_OVER_6MONTHS_UPTO_1YEAR = r40_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR40_OVER_1YEAR_UPTO_3YEARS() {
		return R40_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR40_OVER_1YEAR_UPTO_3YEARS(BigDecimal r40_OVER_1YEAR_UPTO_3YEARS) {
		R40_OVER_1YEAR_UPTO_3YEARS = r40_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR40_OVER_3YEARS_UPTO_5YEARS() {
		return R40_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR40_OVER_3YEARS_UPTO_5YEARS(BigDecimal r40_OVER_3YEARS_UPTO_5YEARS) {
		R40_OVER_3YEARS_UPTO_5YEARS = r40_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR40_OVER_5YEARS() {
		return R40_OVER_5YEARS;
	}
	public void setR40_OVER_5YEARS(BigDecimal r40_OVER_5YEARS) {
		R40_OVER_5YEARS = r40_OVER_5YEARS;
	}
	public BigDecimal getR40_TOTAL() {
		return R40_TOTAL;
	}
	public void setR40_TOTAL(BigDecimal r40_TOTAL) {
		R40_TOTAL = r40_TOTAL;
	}
	public BigDecimal getR41_1_DAY() {
		return R41_1_DAY;
	}
	public void setR41_1_DAY(BigDecimal r41_1_DAY) {
		R41_1_DAY = r41_1_DAY;
	}
	public BigDecimal getR41_2TO7_DAYS() {
		return R41_2TO7_DAYS;
	}
	public void setR41_2TO7_DAYS(BigDecimal r41_2to7_DAYS) {
		R41_2TO7_DAYS = r41_2to7_DAYS;
	}
	public BigDecimal getR41_8TO14_DAYS() {
		return R41_8TO14_DAYS;
	}
	public void setR41_8TO14_DAYS(BigDecimal r41_8to14_DAYS) {
		R41_8TO14_DAYS = r41_8to14_DAYS;
	}
	public BigDecimal getR41_15TO30_DAYS() {
		return R41_15TO30_DAYS;
	}
	public void setR41_15TO30_DAYS(BigDecimal r41_15to30_DAYS) {
		R41_15TO30_DAYS = r41_15to30_DAYS;
	}
	public BigDecimal getR41_31DAYS_UPTO_2MONTHS() {
		return R41_31DAYS_UPTO_2MONTHS;
	}
	public void setR41_31DAYS_UPTO_2MONTHS(BigDecimal r41_31days_UPTO_2MONTHS) {
		R41_31DAYS_UPTO_2MONTHS = r41_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR41_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R41_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR41_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r41_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R41_MORETHAN_2MONTHS_UPTO_3MONHTS = r41_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR41_OVER_3MONTHS_UPTO_6MONTHS() {
		return R41_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR41_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r41_OVER_3MONTHS_UPTO_6MONTHS) {
		R41_OVER_3MONTHS_UPTO_6MONTHS = r41_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR41_OVER_6MONTHS_UPTO_1YEAR() {
		return R41_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR41_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r41_OVER_6MONTHS_UPTO_1YEAR) {
		R41_OVER_6MONTHS_UPTO_1YEAR = r41_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR41_OVER_1YEAR_UPTO_3YEARS() {
		return R41_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR41_OVER_1YEAR_UPTO_3YEARS(BigDecimal r41_OVER_1YEAR_UPTO_3YEARS) {
		R41_OVER_1YEAR_UPTO_3YEARS = r41_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR41_OVER_3YEARS_UPTO_5YEARS() {
		return R41_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR41_OVER_3YEARS_UPTO_5YEARS(BigDecimal r41_OVER_3YEARS_UPTO_5YEARS) {
		R41_OVER_3YEARS_UPTO_5YEARS = r41_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR41_OVER_5YEARS() {
		return R41_OVER_5YEARS;
	}
	public void setR41_OVER_5YEARS(BigDecimal r41_OVER_5YEARS) {
		R41_OVER_5YEARS = r41_OVER_5YEARS;
	}
	public BigDecimal getR41_TOTAL() {
		return R41_TOTAL;
	}
	public void setR41_TOTAL(BigDecimal r41_TOTAL) {
		R41_TOTAL = r41_TOTAL;
	}
	public BigDecimal getR42_1_DAY() {
		return R42_1_DAY;
	}
	public void setR42_1_DAY(BigDecimal r42_1_DAY) {
		R42_1_DAY = r42_1_DAY;
	}
	public BigDecimal getR42_2TO7_DAYS() {
		return R42_2TO7_DAYS;
	}
	public void setR42_2TO7_DAYS(BigDecimal r42_2to7_DAYS) {
		R42_2TO7_DAYS = r42_2to7_DAYS;
	}
	public BigDecimal getR42_8TO14_DAYS() {
		return R42_8TO14_DAYS;
	}
	public void setR42_8TO14_DAYS(BigDecimal r42_8to14_DAYS) {
		R42_8TO14_DAYS = r42_8to14_DAYS;
	}
	public BigDecimal getR42_15TO30_DAYS() {
		return R42_15TO30_DAYS;
	}
	public void setR42_15TO30_DAYS(BigDecimal r42_15to30_DAYS) {
		R42_15TO30_DAYS = r42_15to30_DAYS;
	}
	public BigDecimal getR42_31DAYS_UPTO_2MONTHS() {
		return R42_31DAYS_UPTO_2MONTHS;
	}
	public void setR42_31DAYS_UPTO_2MONTHS(BigDecimal r42_31days_UPTO_2MONTHS) {
		R42_31DAYS_UPTO_2MONTHS = r42_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR42_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R42_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR42_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r42_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R42_MORETHAN_2MONTHS_UPTO_3MONHTS = r42_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR42_OVER_3MONTHS_UPTO_6MONTHS() {
		return R42_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR42_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r42_OVER_3MONTHS_UPTO_6MONTHS) {
		R42_OVER_3MONTHS_UPTO_6MONTHS = r42_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR42_OVER_6MONTHS_UPTO_1YEAR() {
		return R42_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR42_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r42_OVER_6MONTHS_UPTO_1YEAR) {
		R42_OVER_6MONTHS_UPTO_1YEAR = r42_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR42_OVER_1YEAR_UPTO_3YEARS() {
		return R42_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR42_OVER_1YEAR_UPTO_3YEARS(BigDecimal r42_OVER_1YEAR_UPTO_3YEARS) {
		R42_OVER_1YEAR_UPTO_3YEARS = r42_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR42_OVER_3YEARS_UPTO_5YEARS() {
		return R42_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR42_OVER_3YEARS_UPTO_5YEARS(BigDecimal r42_OVER_3YEARS_UPTO_5YEARS) {
		R42_OVER_3YEARS_UPTO_5YEARS = r42_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR42_OVER_5YEARS() {
		return R42_OVER_5YEARS;
	}
	public void setR42_OVER_5YEARS(BigDecimal r42_OVER_5YEARS) {
		R42_OVER_5YEARS = r42_OVER_5YEARS;
	}
	public BigDecimal getR42_TOTAL() {
		return R42_TOTAL;
	}
	public void setR42_TOTAL(BigDecimal r42_TOTAL) {
		R42_TOTAL = r42_TOTAL;
	}
	public BigDecimal getR43_1_DAY() {
		return R43_1_DAY;
	}
	public void setR43_1_DAY(BigDecimal r43_1_DAY) {
		R43_1_DAY = r43_1_DAY;
	}
	public BigDecimal getR43_2TO7_DAYS() {
		return R43_2TO7_DAYS;
	}
	public void setR43_2TO7_DAYS(BigDecimal r43_2to7_DAYS) {
		R43_2TO7_DAYS = r43_2to7_DAYS;
	}
	public BigDecimal getR43_8TO14_DAYS() {
		return R43_8TO14_DAYS;
	}
	public void setR43_8TO14_DAYS(BigDecimal r43_8to14_DAYS) {
		R43_8TO14_DAYS = r43_8to14_DAYS;
	}
	public BigDecimal getR43_15TO30_DAYS() {
		return R43_15TO30_DAYS;
	}
	public void setR43_15TO30_DAYS(BigDecimal r43_15to30_DAYS) {
		R43_15TO30_DAYS = r43_15to30_DAYS;
	}
	public BigDecimal getR43_31DAYS_UPTO_2MONTHS() {
		return R43_31DAYS_UPTO_2MONTHS;
	}
	public void setR43_31DAYS_UPTO_2MONTHS(BigDecimal r43_31days_UPTO_2MONTHS) {
		R43_31DAYS_UPTO_2MONTHS = r43_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR43_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R43_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR43_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r43_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R43_MORETHAN_2MONTHS_UPTO_3MONHTS = r43_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR43_OVER_3MONTHS_UPTO_6MONTHS() {
		return R43_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR43_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r43_OVER_3MONTHS_UPTO_6MONTHS) {
		R43_OVER_3MONTHS_UPTO_6MONTHS = r43_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR43_OVER_6MONTHS_UPTO_1YEAR() {
		return R43_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR43_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r43_OVER_6MONTHS_UPTO_1YEAR) {
		R43_OVER_6MONTHS_UPTO_1YEAR = r43_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR43_OVER_1YEAR_UPTO_3YEARS() {
		return R43_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR43_OVER_1YEAR_UPTO_3YEARS(BigDecimal r43_OVER_1YEAR_UPTO_3YEARS) {
		R43_OVER_1YEAR_UPTO_3YEARS = r43_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR43_OVER_3YEARS_UPTO_5YEARS() {
		return R43_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR43_OVER_3YEARS_UPTO_5YEARS(BigDecimal r43_OVER_3YEARS_UPTO_5YEARS) {
		R43_OVER_3YEARS_UPTO_5YEARS = r43_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR43_OVER_5YEARS() {
		return R43_OVER_5YEARS;
	}
	public void setR43_OVER_5YEARS(BigDecimal r43_OVER_5YEARS) {
		R43_OVER_5YEARS = r43_OVER_5YEARS;
	}
	public BigDecimal getR43_TOTAL() {
		return R43_TOTAL;
	}
	public void setR43_TOTAL(BigDecimal r43_TOTAL) {
		R43_TOTAL = r43_TOTAL;
	}
	public BigDecimal getR44_1_DAY() {
		return R44_1_DAY;
	}
	public void setR44_1_DAY(BigDecimal r44_1_DAY) {
		R44_1_DAY = r44_1_DAY;
	}
	public BigDecimal getR44_2TO7_DAYS() {
		return R44_2TO7_DAYS;
	}
	public void setR44_2TO7_DAYS(BigDecimal r44_2to7_DAYS) {
		R44_2TO7_DAYS = r44_2to7_DAYS;
	}
	public BigDecimal getR44_8TO14_DAYS() {
		return R44_8TO14_DAYS;
	}
	public void setR44_8TO14_DAYS(BigDecimal r44_8to14_DAYS) {
		R44_8TO14_DAYS = r44_8to14_DAYS;
	}
	public BigDecimal getR44_15TO30_DAYS() {
		return R44_15TO30_DAYS;
	}
	public void setR44_15TO30_DAYS(BigDecimal r44_15to30_DAYS) {
		R44_15TO30_DAYS = r44_15to30_DAYS;
	}
	public BigDecimal getR44_31DAYS_UPTO_2MONTHS() {
		return R44_31DAYS_UPTO_2MONTHS;
	}
	public void setR44_31DAYS_UPTO_2MONTHS(BigDecimal r44_31days_UPTO_2MONTHS) {
		R44_31DAYS_UPTO_2MONTHS = r44_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR44_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R44_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR44_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r44_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R44_MORETHAN_2MONTHS_UPTO_3MONHTS = r44_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR44_OVER_3MONTHS_UPTO_6MONTHS() {
		return R44_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR44_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r44_OVER_3MONTHS_UPTO_6MONTHS) {
		R44_OVER_3MONTHS_UPTO_6MONTHS = r44_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR44_OVER_6MONTHS_UPTO_1YEAR() {
		return R44_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR44_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r44_OVER_6MONTHS_UPTO_1YEAR) {
		R44_OVER_6MONTHS_UPTO_1YEAR = r44_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR44_OVER_1YEAR_UPTO_3YEARS() {
		return R44_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR44_OVER_1YEAR_UPTO_3YEARS(BigDecimal r44_OVER_1YEAR_UPTO_3YEARS) {
		R44_OVER_1YEAR_UPTO_3YEARS = r44_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR44_OVER_3YEARS_UPTO_5YEARS() {
		return R44_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR44_OVER_3YEARS_UPTO_5YEARS(BigDecimal r44_OVER_3YEARS_UPTO_5YEARS) {
		R44_OVER_3YEARS_UPTO_5YEARS = r44_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR44_OVER_5YEARS() {
		return R44_OVER_5YEARS;
	}
	public void setR44_OVER_5YEARS(BigDecimal r44_OVER_5YEARS) {
		R44_OVER_5YEARS = r44_OVER_5YEARS;
	}
	public BigDecimal getR44_TOTAL() {
		return R44_TOTAL;
	}
	public void setR44_TOTAL(BigDecimal r44_TOTAL) {
		R44_TOTAL = r44_TOTAL;
	}
	public BigDecimal getR45_1_DAY() {
		return R45_1_DAY;
	}
	public void setR45_1_DAY(BigDecimal r45_1_DAY) {
		R45_1_DAY = r45_1_DAY;
	}
	public BigDecimal getR45_2TO7_DAYS() {
		return R45_2TO7_DAYS;
	}
	public void setR45_2TO7_DAYS(BigDecimal r45_2to7_DAYS) {
		R45_2TO7_DAYS = r45_2to7_DAYS;
	}
	public BigDecimal getR45_8TO14_DAYS() {
		return R45_8TO14_DAYS;
	}
	public void setR45_8TO14_DAYS(BigDecimal r45_8to14_DAYS) {
		R45_8TO14_DAYS = r45_8to14_DAYS;
	}
	public BigDecimal getR45_15TO30_DAYS() {
		return R45_15TO30_DAYS;
	}
	public void setR45_15TO30_DAYS(BigDecimal r45_15to30_DAYS) {
		R45_15TO30_DAYS = r45_15to30_DAYS;
	}
	public BigDecimal getR45_31DAYS_UPTO_2MONTHS() {
		return R45_31DAYS_UPTO_2MONTHS;
	}
	public void setR45_31DAYS_UPTO_2MONTHS(BigDecimal r45_31days_UPTO_2MONTHS) {
		R45_31DAYS_UPTO_2MONTHS = r45_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR45_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R45_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR45_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r45_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R45_MORETHAN_2MONTHS_UPTO_3MONHTS = r45_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR45_OVER_3MONTHS_UPTO_6MONTHS() {
		return R45_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR45_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r45_OVER_3MONTHS_UPTO_6MONTHS) {
		R45_OVER_3MONTHS_UPTO_6MONTHS = r45_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR45_OVER_6MONTHS_UPTO_1YEAR() {
		return R45_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR45_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r45_OVER_6MONTHS_UPTO_1YEAR) {
		R45_OVER_6MONTHS_UPTO_1YEAR = r45_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR45_OVER_1YEAR_UPTO_3YEARS() {
		return R45_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR45_OVER_1YEAR_UPTO_3YEARS(BigDecimal r45_OVER_1YEAR_UPTO_3YEARS) {
		R45_OVER_1YEAR_UPTO_3YEARS = r45_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR45_OVER_3YEARS_UPTO_5YEARS() {
		return R45_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR45_OVER_3YEARS_UPTO_5YEARS(BigDecimal r45_OVER_3YEARS_UPTO_5YEARS) {
		R45_OVER_3YEARS_UPTO_5YEARS = r45_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR45_OVER_5YEARS() {
		return R45_OVER_5YEARS;
	}
	public void setR45_OVER_5YEARS(BigDecimal r45_OVER_5YEARS) {
		R45_OVER_5YEARS = r45_OVER_5YEARS;
	}
	public BigDecimal getR45_TOTAL() {
		return R45_TOTAL;
	}
	public void setR45_TOTAL(BigDecimal r45_TOTAL) {
		R45_TOTAL = r45_TOTAL;
	}
	public BigDecimal getR46_1_DAY() {
		return R46_1_DAY;
	}
	public void setR46_1_DAY(BigDecimal r46_1_DAY) {
		R46_1_DAY = r46_1_DAY;
	}
	public BigDecimal getR46_2TO7_DAYS() {
		return R46_2TO7_DAYS;
	}
	public void setR46_2TO7_DAYS(BigDecimal r46_2to7_DAYS) {
		R46_2TO7_DAYS = r46_2to7_DAYS;
	}
	public BigDecimal getR46_8TO14_DAYS() {
		return R46_8TO14_DAYS;
	}
	public void setR46_8TO14_DAYS(BigDecimal r46_8to14_DAYS) {
		R46_8TO14_DAYS = r46_8to14_DAYS;
	}
	public BigDecimal getR46_15TO30_DAYS() {
		return R46_15TO30_DAYS;
	}
	public void setR46_15TO30_DAYS(BigDecimal r46_15to30_DAYS) {
		R46_15TO30_DAYS = r46_15to30_DAYS;
	}
	public BigDecimal getR46_31DAYS_UPTO_2MONTHS() {
		return R46_31DAYS_UPTO_2MONTHS;
	}
	public void setR46_31DAYS_UPTO_2MONTHS(BigDecimal r46_31days_UPTO_2MONTHS) {
		R46_31DAYS_UPTO_2MONTHS = r46_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR46_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R46_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR46_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r46_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R46_MORETHAN_2MONTHS_UPTO_3MONHTS = r46_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR46_OVER_3MONTHS_UPTO_6MONTHS() {
		return R46_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR46_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r46_OVER_3MONTHS_UPTO_6MONTHS) {
		R46_OVER_3MONTHS_UPTO_6MONTHS = r46_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR46_OVER_6MONTHS_UPTO_1YEAR() {
		return R46_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR46_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r46_OVER_6MONTHS_UPTO_1YEAR) {
		R46_OVER_6MONTHS_UPTO_1YEAR = r46_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR46_OVER_1YEAR_UPTO_3YEARS() {
		return R46_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR46_OVER_1YEAR_UPTO_3YEARS(BigDecimal r46_OVER_1YEAR_UPTO_3YEARS) {
		R46_OVER_1YEAR_UPTO_3YEARS = r46_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR46_OVER_3YEARS_UPTO_5YEARS() {
		return R46_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR46_OVER_3YEARS_UPTO_5YEARS(BigDecimal r46_OVER_3YEARS_UPTO_5YEARS) {
		R46_OVER_3YEARS_UPTO_5YEARS = r46_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR46_OVER_5YEARS() {
		return R46_OVER_5YEARS;
	}
	public void setR46_OVER_5YEARS(BigDecimal r46_OVER_5YEARS) {
		R46_OVER_5YEARS = r46_OVER_5YEARS;
	}
	public BigDecimal getR46_TOTAL() {
		return R46_TOTAL;
	}
	public void setR46_TOTAL(BigDecimal r46_TOTAL) {
		R46_TOTAL = r46_TOTAL;
	}
	public BigDecimal getR47_1_DAY() {
		return R47_1_DAY;
	}
	public void setR47_1_DAY(BigDecimal r47_1_DAY) {
		R47_1_DAY = r47_1_DAY;
	}
	public BigDecimal getR47_2TO7_DAYS() {
		return R47_2TO7_DAYS;
	}
	public void setR47_2TO7_DAYS(BigDecimal r47_2to7_DAYS) {
		R47_2TO7_DAYS = r47_2to7_DAYS;
	}
	public BigDecimal getR47_8TO14_DAYS() {
		return R47_8TO14_DAYS;
	}
	public void setR47_8TO14_DAYS(BigDecimal r47_8to14_DAYS) {
		R47_8TO14_DAYS = r47_8to14_DAYS;
	}
	public BigDecimal getR47_15TO30_DAYS() {
		return R47_15TO30_DAYS;
	}
	public void setR47_15TO30_DAYS(BigDecimal r47_15to30_DAYS) {
		R47_15TO30_DAYS = r47_15to30_DAYS;
	}
	public BigDecimal getR47_31DAYS_UPTO_2MONTHS() {
		return R47_31DAYS_UPTO_2MONTHS;
	}
	public void setR47_31DAYS_UPTO_2MONTHS(BigDecimal r47_31days_UPTO_2MONTHS) {
		R47_31DAYS_UPTO_2MONTHS = r47_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR47_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R47_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR47_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r47_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R47_MORETHAN_2MONTHS_UPTO_3MONHTS = r47_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR47_OVER_3MONTHS_UPTO_6MONTHS() {
		return R47_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR47_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r47_OVER_3MONTHS_UPTO_6MONTHS) {
		R47_OVER_3MONTHS_UPTO_6MONTHS = r47_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR47_OVER_6MONTHS_UPTO_1YEAR() {
		return R47_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR47_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r47_OVER_6MONTHS_UPTO_1YEAR) {
		R47_OVER_6MONTHS_UPTO_1YEAR = r47_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR47_OVER_1YEAR_UPTO_3YEARS() {
		return R47_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR47_OVER_1YEAR_UPTO_3YEARS(BigDecimal r47_OVER_1YEAR_UPTO_3YEARS) {
		R47_OVER_1YEAR_UPTO_3YEARS = r47_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR47_OVER_3YEARS_UPTO_5YEARS() {
		return R47_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR47_OVER_3YEARS_UPTO_5YEARS(BigDecimal r47_OVER_3YEARS_UPTO_5YEARS) {
		R47_OVER_3YEARS_UPTO_5YEARS = r47_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR47_OVER_5YEARS() {
		return R47_OVER_5YEARS;
	}
	public void setR47_OVER_5YEARS(BigDecimal r47_OVER_5YEARS) {
		R47_OVER_5YEARS = r47_OVER_5YEARS;
	}
	public BigDecimal getR47_TOTAL() {
		return R47_TOTAL;
	}
	public void setR47_TOTAL(BigDecimal r47_TOTAL) {
		R47_TOTAL = r47_TOTAL;
	}
	public BigDecimal getR48_1_DAY() {
		return R48_1_DAY;
	}
	public void setR48_1_DAY(BigDecimal r48_1_DAY) {
		R48_1_DAY = r48_1_DAY;
	}
	public BigDecimal getR48_2TO7_DAYS() {
		return R48_2TO7_DAYS;
	}
	public void setR48_2TO7_DAYS(BigDecimal r48_2to7_DAYS) {
		R48_2TO7_DAYS = r48_2to7_DAYS;
	}
	public BigDecimal getR48_8TO14_DAYS() {
		return R48_8TO14_DAYS;
	}
	public void setR48_8TO14_DAYS(BigDecimal r48_8to14_DAYS) {
		R48_8TO14_DAYS = r48_8to14_DAYS;
	}
	public BigDecimal getR48_15TO30_DAYS() {
		return R48_15TO30_DAYS;
	}
	public void setR48_15TO30_DAYS(BigDecimal r48_15to30_DAYS) {
		R48_15TO30_DAYS = r48_15to30_DAYS;
	}
	public BigDecimal getR48_31DAYS_UPTO_2MONTHS() {
		return R48_31DAYS_UPTO_2MONTHS;
	}
	public void setR48_31DAYS_UPTO_2MONTHS(BigDecimal r48_31days_UPTO_2MONTHS) {
		R48_31DAYS_UPTO_2MONTHS = r48_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR48_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R48_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR48_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r48_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R48_MORETHAN_2MONTHS_UPTO_3MONHTS = r48_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR48_OVER_3MONTHS_UPTO_6MONTHS() {
		return R48_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR48_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r48_OVER_3MONTHS_UPTO_6MONTHS) {
		R48_OVER_3MONTHS_UPTO_6MONTHS = r48_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR48_OVER_6MONTHS_UPTO_1YEAR() {
		return R48_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR48_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r48_OVER_6MONTHS_UPTO_1YEAR) {
		R48_OVER_6MONTHS_UPTO_1YEAR = r48_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR48_OVER_1YEAR_UPTO_3YEARS() {
		return R48_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR48_OVER_1YEAR_UPTO_3YEARS(BigDecimal r48_OVER_1YEAR_UPTO_3YEARS) {
		R48_OVER_1YEAR_UPTO_3YEARS = r48_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR48_OVER_3YEARS_UPTO_5YEARS() {
		return R48_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR48_OVER_3YEARS_UPTO_5YEARS(BigDecimal r48_OVER_3YEARS_UPTO_5YEARS) {
		R48_OVER_3YEARS_UPTO_5YEARS = r48_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR48_OVER_5YEARS() {
		return R48_OVER_5YEARS;
	}
	public void setR48_OVER_5YEARS(BigDecimal r48_OVER_5YEARS) {
		R48_OVER_5YEARS = r48_OVER_5YEARS;
	}
	public BigDecimal getR48_TOTAL() {
		return R48_TOTAL;
	}
	public void setR48_TOTAL(BigDecimal r48_TOTAL) {
		R48_TOTAL = r48_TOTAL;
	}
	public BigDecimal getR49_1_DAY() {
		return R49_1_DAY;
	}
	public void setR49_1_DAY(BigDecimal r49_1_DAY) {
		R49_1_DAY = r49_1_DAY;
	}
	public BigDecimal getR49_2TO7_DAYS() {
		return R49_2TO7_DAYS;
	}
	public void setR49_2TO7_DAYS(BigDecimal r49_2to7_DAYS) {
		R49_2TO7_DAYS = r49_2to7_DAYS;
	}
	public BigDecimal getR49_8TO14_DAYS() {
		return R49_8TO14_DAYS;
	}
	public void setR49_8TO14_DAYS(BigDecimal r49_8to14_DAYS) {
		R49_8TO14_DAYS = r49_8to14_DAYS;
	}
	public BigDecimal getR49_15TO30_DAYS() {
		return R49_15TO30_DAYS;
	}
	public void setR49_15TO30_DAYS(BigDecimal r49_15to30_DAYS) {
		R49_15TO30_DAYS = r49_15to30_DAYS;
	}
	public BigDecimal getR49_31DAYS_UPTO_2MONTHS() {
		return R49_31DAYS_UPTO_2MONTHS;
	}
	public void setR49_31DAYS_UPTO_2MONTHS(BigDecimal r49_31days_UPTO_2MONTHS) {
		R49_31DAYS_UPTO_2MONTHS = r49_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR49_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R49_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR49_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r49_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R49_MORETHAN_2MONTHS_UPTO_3MONHTS = r49_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR49_OVER_3MONTHS_UPTO_6MONTHS() {
		return R49_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR49_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r49_OVER_3MONTHS_UPTO_6MONTHS) {
		R49_OVER_3MONTHS_UPTO_6MONTHS = r49_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR49_OVER_6MONTHS_UPTO_1YEAR() {
		return R49_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR49_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r49_OVER_6MONTHS_UPTO_1YEAR) {
		R49_OVER_6MONTHS_UPTO_1YEAR = r49_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR49_OVER_1YEAR_UPTO_3YEARS() {
		return R49_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR49_OVER_1YEAR_UPTO_3YEARS(BigDecimal r49_OVER_1YEAR_UPTO_3YEARS) {
		R49_OVER_1YEAR_UPTO_3YEARS = r49_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR49_OVER_3YEARS_UPTO_5YEARS() {
		return R49_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR49_OVER_3YEARS_UPTO_5YEARS(BigDecimal r49_OVER_3YEARS_UPTO_5YEARS) {
		R49_OVER_3YEARS_UPTO_5YEARS = r49_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR49_OVER_5YEARS() {
		return R49_OVER_5YEARS;
	}
	public void setR49_OVER_5YEARS(BigDecimal r49_OVER_5YEARS) {
		R49_OVER_5YEARS = r49_OVER_5YEARS;
	}
	public BigDecimal getR49_TOTAL() {
		return R49_TOTAL;
	}
	public void setR49_TOTAL(BigDecimal r49_TOTAL) {
		R49_TOTAL = r49_TOTAL;
	}
	public BigDecimal getR50_1_DAY() {
		return R50_1_DAY;
	}
	public void setR50_1_DAY(BigDecimal r50_1_DAY) {
		R50_1_DAY = r50_1_DAY;
	}
	public BigDecimal getR50_2TO7_DAYS() {
		return R50_2TO7_DAYS;
	}
	public void setR50_2TO7_DAYS(BigDecimal r50_2to7_DAYS) {
		R50_2TO7_DAYS = r50_2to7_DAYS;
	}
	public BigDecimal getR50_8TO14_DAYS() {
		return R50_8TO14_DAYS;
	}
	public void setR50_8TO14_DAYS(BigDecimal r50_8to14_DAYS) {
		R50_8TO14_DAYS = r50_8to14_DAYS;
	}
	public BigDecimal getR50_15TO30_DAYS() {
		return R50_15TO30_DAYS;
	}
	public void setR50_15TO30_DAYS(BigDecimal r50_15to30_DAYS) {
		R50_15TO30_DAYS = r50_15to30_DAYS;
	}
	public BigDecimal getR50_31DAYS_UPTO_2MONTHS() {
		return R50_31DAYS_UPTO_2MONTHS;
	}
	public void setR50_31DAYS_UPTO_2MONTHS(BigDecimal r50_31days_UPTO_2MONTHS) {
		R50_31DAYS_UPTO_2MONTHS = r50_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR50_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R50_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR50_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r50_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R50_MORETHAN_2MONTHS_UPTO_3MONHTS = r50_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR50_OVER_3MONTHS_UPTO_6MONTHS() {
		return R50_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR50_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r50_OVER_3MONTHS_UPTO_6MONTHS) {
		R50_OVER_3MONTHS_UPTO_6MONTHS = r50_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR50_OVER_6MONTHS_UPTO_1YEAR() {
		return R50_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR50_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r50_OVER_6MONTHS_UPTO_1YEAR) {
		R50_OVER_6MONTHS_UPTO_1YEAR = r50_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR50_OVER_1YEAR_UPTO_3YEARS() {
		return R50_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR50_OVER_1YEAR_UPTO_3YEARS(BigDecimal r50_OVER_1YEAR_UPTO_3YEARS) {
		R50_OVER_1YEAR_UPTO_3YEARS = r50_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR50_OVER_3YEARS_UPTO_5YEARS() {
		return R50_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR50_OVER_3YEARS_UPTO_5YEARS(BigDecimal r50_OVER_3YEARS_UPTO_5YEARS) {
		R50_OVER_3YEARS_UPTO_5YEARS = r50_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR50_OVER_5YEARS() {
		return R50_OVER_5YEARS;
	}
	public void setR50_OVER_5YEARS(BigDecimal r50_OVER_5YEARS) {
		R50_OVER_5YEARS = r50_OVER_5YEARS;
	}
	public BigDecimal getR50_TOTAL() {
		return R50_TOTAL;
	}
	public void setR50_TOTAL(BigDecimal r50_TOTAL) {
		R50_TOTAL = r50_TOTAL;
	}
	public BigDecimal getR51_1_DAY() {
		return R51_1_DAY;
	}
	public void setR51_1_DAY(BigDecimal r51_1_DAY) {
		R51_1_DAY = r51_1_DAY;
	}
	public BigDecimal getR51_2TO7_DAYS() {
		return R51_2TO7_DAYS;
	}
	public void setR51_2TO7_DAYS(BigDecimal r51_2to7_DAYS) {
		R51_2TO7_DAYS = r51_2to7_DAYS;
	}
	public BigDecimal getR51_8TO14_DAYS() {
		return R51_8TO14_DAYS;
	}
	public void setR51_8TO14_DAYS(BigDecimal r51_8to14_DAYS) {
		R51_8TO14_DAYS = r51_8to14_DAYS;
	}
	public BigDecimal getR51_15TO30_DAYS() {
		return R51_15TO30_DAYS;
	}
	public void setR51_15TO30_DAYS(BigDecimal r51_15to30_DAYS) {
		R51_15TO30_DAYS = r51_15to30_DAYS;
	}
	public BigDecimal getR51_31DAYS_UPTO_2MONTHS() {
		return R51_31DAYS_UPTO_2MONTHS;
	}
	public void setR51_31DAYS_UPTO_2MONTHS(BigDecimal r51_31days_UPTO_2MONTHS) {
		R51_31DAYS_UPTO_2MONTHS = r51_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR51_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R51_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR51_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r51_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R51_MORETHAN_2MONTHS_UPTO_3MONHTS = r51_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR51_OVER_3MONTHS_UPTO_6MONTHS() {
		return R51_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR51_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r51_OVER_3MONTHS_UPTO_6MONTHS) {
		R51_OVER_3MONTHS_UPTO_6MONTHS = r51_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR51_OVER_6MONTHS_UPTO_1YEAR() {
		return R51_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR51_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r51_OVER_6MONTHS_UPTO_1YEAR) {
		R51_OVER_6MONTHS_UPTO_1YEAR = r51_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR51_OVER_1YEAR_UPTO_3YEARS() {
		return R51_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR51_OVER_1YEAR_UPTO_3YEARS(BigDecimal r51_OVER_1YEAR_UPTO_3YEARS) {
		R51_OVER_1YEAR_UPTO_3YEARS = r51_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR51_OVER_3YEARS_UPTO_5YEARS() {
		return R51_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR51_OVER_3YEARS_UPTO_5YEARS(BigDecimal r51_OVER_3YEARS_UPTO_5YEARS) {
		R51_OVER_3YEARS_UPTO_5YEARS = r51_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR51_OVER_5YEARS() {
		return R51_OVER_5YEARS;
	}
	public void setR51_OVER_5YEARS(BigDecimal r51_OVER_5YEARS) {
		R51_OVER_5YEARS = r51_OVER_5YEARS;
	}
	public BigDecimal getR51_TOTAL() {
		return R51_TOTAL;
	}
	public void setR51_TOTAL(BigDecimal r51_TOTAL) {
		R51_TOTAL = r51_TOTAL;
	}
	public BigDecimal getR52_1_DAY() {
		return R52_1_DAY;
	}
	public void setR52_1_DAY(BigDecimal r52_1_DAY) {
		R52_1_DAY = r52_1_DAY;
	}
	public BigDecimal getR52_2TO7_DAYS() {
		return R52_2TO7_DAYS;
	}
	public void setR52_2TO7_DAYS(BigDecimal r52_2to7_DAYS) {
		R52_2TO7_DAYS = r52_2to7_DAYS;
	}
	public BigDecimal getR52_8TO14_DAYS() {
		return R52_8TO14_DAYS;
	}
	public void setR52_8TO14_DAYS(BigDecimal r52_8to14_DAYS) {
		R52_8TO14_DAYS = r52_8to14_DAYS;
	}
	public BigDecimal getR52_15TO30_DAYS() {
		return R52_15TO30_DAYS;
	}
	public void setR52_15TO30_DAYS(BigDecimal r52_15to30_DAYS) {
		R52_15TO30_DAYS = r52_15to30_DAYS;
	}
	public BigDecimal getR52_31DAYS_UPTO_2MONTHS() {
		return R52_31DAYS_UPTO_2MONTHS;
	}
	public void setR52_31DAYS_UPTO_2MONTHS(BigDecimal r52_31days_UPTO_2MONTHS) {
		R52_31DAYS_UPTO_2MONTHS = r52_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR52_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R52_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR52_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r52_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R52_MORETHAN_2MONTHS_UPTO_3MONHTS = r52_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR52_OVER_3MONTHS_UPTO_6MONTHS() {
		return R52_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR52_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r52_OVER_3MONTHS_UPTO_6MONTHS) {
		R52_OVER_3MONTHS_UPTO_6MONTHS = r52_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR52_OVER_6MONTHS_UPTO_1YEAR() {
		return R52_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR52_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r52_OVER_6MONTHS_UPTO_1YEAR) {
		R52_OVER_6MONTHS_UPTO_1YEAR = r52_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR52_OVER_1YEAR_UPTO_3YEARS() {
		return R52_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR52_OVER_1YEAR_UPTO_3YEARS(BigDecimal r52_OVER_1YEAR_UPTO_3YEARS) {
		R52_OVER_1YEAR_UPTO_3YEARS = r52_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR52_OVER_3YEARS_UPTO_5YEARS() {
		return R52_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR52_OVER_3YEARS_UPTO_5YEARS(BigDecimal r52_OVER_3YEARS_UPTO_5YEARS) {
		R52_OVER_3YEARS_UPTO_5YEARS = r52_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR52_OVER_5YEARS() {
		return R52_OVER_5YEARS;
	}
	public void setR52_OVER_5YEARS(BigDecimal r52_OVER_5YEARS) {
		R52_OVER_5YEARS = r52_OVER_5YEARS;
	}
	public BigDecimal getR52_TOTAL() {
		return R52_TOTAL;
	}
	public void setR52_TOTAL(BigDecimal r52_TOTAL) {
		R52_TOTAL = r52_TOTAL;
	}
	public BigDecimal getR53_1_DAY() {
		return R53_1_DAY;
	}
	public void setR53_1_DAY(BigDecimal r53_1_DAY) {
		R53_1_DAY = r53_1_DAY;
	}
	public BigDecimal getR53_2TO7_DAYS() {
		return R53_2TO7_DAYS;
	}
	public void setR53_2TO7_DAYS(BigDecimal r53_2to7_DAYS) {
		R53_2TO7_DAYS = r53_2to7_DAYS;
	}
	public BigDecimal getR53_8TO14_DAYS() {
		return R53_8TO14_DAYS;
	}
	public void setR53_8TO14_DAYS(BigDecimal r53_8to14_DAYS) {
		R53_8TO14_DAYS = r53_8to14_DAYS;
	}
	public BigDecimal getR53_15TO30_DAYS() {
		return R53_15TO30_DAYS;
	}
	public void setR53_15TO30_DAYS(BigDecimal r53_15to30_DAYS) {
		R53_15TO30_DAYS = r53_15to30_DAYS;
	}
	public BigDecimal getR53_31DAYS_UPTO_2MONTHS() {
		return R53_31DAYS_UPTO_2MONTHS;
	}
	public void setR53_31DAYS_UPTO_2MONTHS(BigDecimal r53_31days_UPTO_2MONTHS) {
		R53_31DAYS_UPTO_2MONTHS = r53_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR53_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R53_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR53_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r53_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R53_MORETHAN_2MONTHS_UPTO_3MONHTS = r53_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR53_OVER_3MONTHS_UPTO_6MONTHS() {
		return R53_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR53_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r53_OVER_3MONTHS_UPTO_6MONTHS) {
		R53_OVER_3MONTHS_UPTO_6MONTHS = r53_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR53_OVER_6MONTHS_UPTO_1YEAR() {
		return R53_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR53_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r53_OVER_6MONTHS_UPTO_1YEAR) {
		R53_OVER_6MONTHS_UPTO_1YEAR = r53_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR53_OVER_1YEAR_UPTO_3YEARS() {
		return R53_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR53_OVER_1YEAR_UPTO_3YEARS(BigDecimal r53_OVER_1YEAR_UPTO_3YEARS) {
		R53_OVER_1YEAR_UPTO_3YEARS = r53_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR53_OVER_3YEARS_UPTO_5YEARS() {
		return R53_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR53_OVER_3YEARS_UPTO_5YEARS(BigDecimal r53_OVER_3YEARS_UPTO_5YEARS) {
		R53_OVER_3YEARS_UPTO_5YEARS = r53_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR53_OVER_5YEARS() {
		return R53_OVER_5YEARS;
	}
	public void setR53_OVER_5YEARS(BigDecimal r53_OVER_5YEARS) {
		R53_OVER_5YEARS = r53_OVER_5YEARS;
	}
	public BigDecimal getR53_TOTAL() {
		return R53_TOTAL;
	}
	public void setR53_TOTAL(BigDecimal r53_TOTAL) {
		R53_TOTAL = r53_TOTAL;
	}
	public BigDecimal getR54_1_DAY() {
		return R54_1_DAY;
	}
	public void setR54_1_DAY(BigDecimal r54_1_DAY) {
		R54_1_DAY = r54_1_DAY;
	}
	public BigDecimal getR54_2TO7_DAYS() {
		return R54_2TO7_DAYS;
	}
	public void setR54_2TO7_DAYS(BigDecimal r54_2to7_DAYS) {
		R54_2TO7_DAYS = r54_2to7_DAYS;
	}
	public BigDecimal getR54_8TO14_DAYS() {
		return R54_8TO14_DAYS;
	}
	public void setR54_8TO14_DAYS(BigDecimal r54_8to14_DAYS) {
		R54_8TO14_DAYS = r54_8to14_DAYS;
	}
	public BigDecimal getR54_15TO30_DAYS() {
		return R54_15TO30_DAYS;
	}
	public void setR54_15TO30_DAYS(BigDecimal r54_15to30_DAYS) {
		R54_15TO30_DAYS = r54_15to30_DAYS;
	}
	public BigDecimal getR54_31DAYS_UPTO_2MONTHS() {
		return R54_31DAYS_UPTO_2MONTHS;
	}
	public void setR54_31DAYS_UPTO_2MONTHS(BigDecimal r54_31days_UPTO_2MONTHS) {
		R54_31DAYS_UPTO_2MONTHS = r54_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR54_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R54_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR54_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r54_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R54_MORETHAN_2MONTHS_UPTO_3MONHTS = r54_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR54_OVER_3MONTHS_UPTO_6MONTHS() {
		return R54_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR54_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r54_OVER_3MONTHS_UPTO_6MONTHS) {
		R54_OVER_3MONTHS_UPTO_6MONTHS = r54_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR54_OVER_6MONTHS_UPTO_1YEAR() {
		return R54_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR54_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r54_OVER_6MONTHS_UPTO_1YEAR) {
		R54_OVER_6MONTHS_UPTO_1YEAR = r54_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR54_OVER_1YEAR_UPTO_3YEARS() {
		return R54_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR54_OVER_1YEAR_UPTO_3YEARS(BigDecimal r54_OVER_1YEAR_UPTO_3YEARS) {
		R54_OVER_1YEAR_UPTO_3YEARS = r54_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR54_OVER_3YEARS_UPTO_5YEARS() {
		return R54_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR54_OVER_3YEARS_UPTO_5YEARS(BigDecimal r54_OVER_3YEARS_UPTO_5YEARS) {
		R54_OVER_3YEARS_UPTO_5YEARS = r54_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR54_OVER_5YEARS() {
		return R54_OVER_5YEARS;
	}
	public void setR54_OVER_5YEARS(BigDecimal r54_OVER_5YEARS) {
		R54_OVER_5YEARS = r54_OVER_5YEARS;
	}
	public BigDecimal getR54_TOTAL() {
		return R54_TOTAL;
	}
	public void setR54_TOTAL(BigDecimal r54_TOTAL) {
		R54_TOTAL = r54_TOTAL;
	}
	public BigDecimal getR55_1_DAY() {
		return R55_1_DAY;
	}
	public void setR55_1_DAY(BigDecimal r55_1_DAY) {
		R55_1_DAY = r55_1_DAY;
	}
	public BigDecimal getR55_2TO7_DAYS() {
		return R55_2TO7_DAYS;
	}
	public void setR55_2TO7_DAYS(BigDecimal r55_2to7_DAYS) {
		R55_2TO7_DAYS = r55_2to7_DAYS;
	}
	public BigDecimal getR55_8TO14_DAYS() {
		return R55_8TO14_DAYS;
	}
	public void setR55_8TO14_DAYS(BigDecimal r55_8to14_DAYS) {
		R55_8TO14_DAYS = r55_8to14_DAYS;
	}
	public BigDecimal getR55_15TO30_DAYS() {
		return R55_15TO30_DAYS;
	}
	public void setR55_15TO30_DAYS(BigDecimal r55_15to30_DAYS) {
		R55_15TO30_DAYS = r55_15to30_DAYS;
	}
	public BigDecimal getR55_31DAYS_UPTO_2MONTHS() {
		return R55_31DAYS_UPTO_2MONTHS;
	}
	public void setR55_31DAYS_UPTO_2MONTHS(BigDecimal r55_31days_UPTO_2MONTHS) {
		R55_31DAYS_UPTO_2MONTHS = r55_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR55_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R55_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR55_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r55_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R55_MORETHAN_2MONTHS_UPTO_3MONHTS = r55_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR55_OVER_3MONTHS_UPTO_6MONTHS() {
		return R55_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR55_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r55_OVER_3MONTHS_UPTO_6MONTHS) {
		R55_OVER_3MONTHS_UPTO_6MONTHS = r55_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR55_OVER_6MONTHS_UPTO_1YEAR() {
		return R55_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR55_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r55_OVER_6MONTHS_UPTO_1YEAR) {
		R55_OVER_6MONTHS_UPTO_1YEAR = r55_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR55_OVER_1YEAR_UPTO_3YEARS() {
		return R55_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR55_OVER_1YEAR_UPTO_3YEARS(BigDecimal r55_OVER_1YEAR_UPTO_3YEARS) {
		R55_OVER_1YEAR_UPTO_3YEARS = r55_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR55_OVER_3YEARS_UPTO_5YEARS() {
		return R55_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR55_OVER_3YEARS_UPTO_5YEARS(BigDecimal r55_OVER_3YEARS_UPTO_5YEARS) {
		R55_OVER_3YEARS_UPTO_5YEARS = r55_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR55_OVER_5YEARS() {
		return R55_OVER_5YEARS;
	}
	public void setR55_OVER_5YEARS(BigDecimal r55_OVER_5YEARS) {
		R55_OVER_5YEARS = r55_OVER_5YEARS;
	}
	public BigDecimal getR55_TOTAL() {
		return R55_TOTAL;
	}
	public void setR55_TOTAL(BigDecimal r55_TOTAL) {
		R55_TOTAL = r55_TOTAL;
	}
	public BigDecimal getR56_1_DAY() {
		return R56_1_DAY;
	}
	public void setR56_1_DAY(BigDecimal r56_1_DAY) {
		R56_1_DAY = r56_1_DAY;
	}
	public BigDecimal getR56_2TO7_DAYS() {
		return R56_2TO7_DAYS;
	}
	public void setR56_2TO7_DAYS(BigDecimal r56_2to7_DAYS) {
		R56_2TO7_DAYS = r56_2to7_DAYS;
	}
	public BigDecimal getR56_8TO14_DAYS() {
		return R56_8TO14_DAYS;
	}
	public void setR56_8TO14_DAYS(BigDecimal r56_8to14_DAYS) {
		R56_8TO14_DAYS = r56_8to14_DAYS;
	}
	public BigDecimal getR56_15TO30_DAYS() {
		return R56_15TO30_DAYS;
	}
	public void setR56_15TO30_DAYS(BigDecimal r56_15to30_DAYS) {
		R56_15TO30_DAYS = r56_15to30_DAYS;
	}
	public BigDecimal getR56_31DAYS_UPTO_2MONTHS() {
		return R56_31DAYS_UPTO_2MONTHS;
	}
	public void setR56_31DAYS_UPTO_2MONTHS(BigDecimal r56_31days_UPTO_2MONTHS) {
		R56_31DAYS_UPTO_2MONTHS = r56_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR56_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R56_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR56_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r56_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R56_MORETHAN_2MONTHS_UPTO_3MONHTS = r56_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR56_OVER_3MONTHS_UPTO_6MONTHS() {
		return R56_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR56_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r56_OVER_3MONTHS_UPTO_6MONTHS) {
		R56_OVER_3MONTHS_UPTO_6MONTHS = r56_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR56_OVER_6MONTHS_UPTO_1YEAR() {
		return R56_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR56_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r56_OVER_6MONTHS_UPTO_1YEAR) {
		R56_OVER_6MONTHS_UPTO_1YEAR = r56_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR56_OVER_1YEAR_UPTO_3YEARS() {
		return R56_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR56_OVER_1YEAR_UPTO_3YEARS(BigDecimal r56_OVER_1YEAR_UPTO_3YEARS) {
		R56_OVER_1YEAR_UPTO_3YEARS = r56_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR56_OVER_3YEARS_UPTO_5YEARS() {
		return R56_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR56_OVER_3YEARS_UPTO_5YEARS(BigDecimal r56_OVER_3YEARS_UPTO_5YEARS) {
		R56_OVER_3YEARS_UPTO_5YEARS = r56_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR56_OVER_5YEARS() {
		return R56_OVER_5YEARS;
	}
	public void setR56_OVER_5YEARS(BigDecimal r56_OVER_5YEARS) {
		R56_OVER_5YEARS = r56_OVER_5YEARS;
	}
	public BigDecimal getR56_TOTAL() {
		return R56_TOTAL;
	}
	public void setR56_TOTAL(BigDecimal r56_TOTAL) {
		R56_TOTAL = r56_TOTAL;
	}
	public BigDecimal getR57_1_DAY() {
		return R57_1_DAY;
	}
	public void setR57_1_DAY(BigDecimal r57_1_DAY) {
		R57_1_DAY = r57_1_DAY;
	}
	public BigDecimal getR57_2TO7_DAYS() {
		return R57_2TO7_DAYS;
	}
	public void setR57_2TO7_DAYS(BigDecimal r57_2to7_DAYS) {
		R57_2TO7_DAYS = r57_2to7_DAYS;
	}
	public BigDecimal getR57_8TO14_DAYS() {
		return R57_8TO14_DAYS;
	}
	public void setR57_8TO14_DAYS(BigDecimal r57_8to14_DAYS) {
		R57_8TO14_DAYS = r57_8to14_DAYS;
	}
	public BigDecimal getR57_15TO30_DAYS() {
		return R57_15TO30_DAYS;
	}
	public void setR57_15TO30_DAYS(BigDecimal r57_15to30_DAYS) {
		R57_15TO30_DAYS = r57_15to30_DAYS;
	}
	public BigDecimal getR57_31DAYS_UPTO_2MONTHS() {
		return R57_31DAYS_UPTO_2MONTHS;
	}
	public void setR57_31DAYS_UPTO_2MONTHS(BigDecimal r57_31days_UPTO_2MONTHS) {
		R57_31DAYS_UPTO_2MONTHS = r57_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR57_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R57_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR57_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r57_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R57_MORETHAN_2MONTHS_UPTO_3MONHTS = r57_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR57_OVER_3MONTHS_UPTO_6MONTHS() {
		return R57_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR57_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r57_OVER_3MONTHS_UPTO_6MONTHS) {
		R57_OVER_3MONTHS_UPTO_6MONTHS = r57_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR57_OVER_6MONTHS_UPTO_1YEAR() {
		return R57_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR57_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r57_OVER_6MONTHS_UPTO_1YEAR) {
		R57_OVER_6MONTHS_UPTO_1YEAR = r57_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR57_OVER_1YEAR_UPTO_3YEARS() {
		return R57_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR57_OVER_1YEAR_UPTO_3YEARS(BigDecimal r57_OVER_1YEAR_UPTO_3YEARS) {
		R57_OVER_1YEAR_UPTO_3YEARS = r57_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR57_OVER_3YEARS_UPTO_5YEARS() {
		return R57_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR57_OVER_3YEARS_UPTO_5YEARS(BigDecimal r57_OVER_3YEARS_UPTO_5YEARS) {
		R57_OVER_3YEARS_UPTO_5YEARS = r57_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR57_OVER_5YEARS() {
		return R57_OVER_5YEARS;
	}
	public void setR57_OVER_5YEARS(BigDecimal r57_OVER_5YEARS) {
		R57_OVER_5YEARS = r57_OVER_5YEARS;
	}
	public BigDecimal getR57_TOTAL() {
		return R57_TOTAL;
	}
	public void setR57_TOTAL(BigDecimal r57_TOTAL) {
		R57_TOTAL = r57_TOTAL;
	}
	public BigDecimal getR58_1_DAY() {
		return R58_1_DAY;
	}
	public void setR58_1_DAY(BigDecimal r58_1_DAY) {
		R58_1_DAY = r58_1_DAY;
	}
	public BigDecimal getR58_2TO7_DAYS() {
		return R58_2TO7_DAYS;
	}
	public void setR58_2TO7_DAYS(BigDecimal r58_2to7_DAYS) {
		R58_2TO7_DAYS = r58_2to7_DAYS;
	}
	public BigDecimal getR58_8TO14_DAYS() {
		return R58_8TO14_DAYS;
	}
	public void setR58_8TO14_DAYS(BigDecimal r58_8to14_DAYS) {
		R58_8TO14_DAYS = r58_8to14_DAYS;
	}
	public BigDecimal getR58_15TO30_DAYS() {
		return R58_15TO30_DAYS;
	}
	public void setR58_15TO30_DAYS(BigDecimal r58_15to30_DAYS) {
		R58_15TO30_DAYS = r58_15to30_DAYS;
	}
	public BigDecimal getR58_31DAYS_UPTO_2MONTHS() {
		return R58_31DAYS_UPTO_2MONTHS;
	}
	public void setR58_31DAYS_UPTO_2MONTHS(BigDecimal r58_31days_UPTO_2MONTHS) {
		R58_31DAYS_UPTO_2MONTHS = r58_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR58_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R58_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR58_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r58_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R58_MORETHAN_2MONTHS_UPTO_3MONHTS = r58_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR58_OVER_3MONTHS_UPTO_6MONTHS() {
		return R58_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR58_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r58_OVER_3MONTHS_UPTO_6MONTHS) {
		R58_OVER_3MONTHS_UPTO_6MONTHS = r58_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR58_OVER_6MONTHS_UPTO_1YEAR() {
		return R58_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR58_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r58_OVER_6MONTHS_UPTO_1YEAR) {
		R58_OVER_6MONTHS_UPTO_1YEAR = r58_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR58_OVER_1YEAR_UPTO_3YEARS() {
		return R58_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR58_OVER_1YEAR_UPTO_3YEARS(BigDecimal r58_OVER_1YEAR_UPTO_3YEARS) {
		R58_OVER_1YEAR_UPTO_3YEARS = r58_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR58_OVER_3YEARS_UPTO_5YEARS() {
		return R58_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR58_OVER_3YEARS_UPTO_5YEARS(BigDecimal r58_OVER_3YEARS_UPTO_5YEARS) {
		R58_OVER_3YEARS_UPTO_5YEARS = r58_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR58_OVER_5YEARS() {
		return R58_OVER_5YEARS;
	}
	public void setR58_OVER_5YEARS(BigDecimal r58_OVER_5YEARS) {
		R58_OVER_5YEARS = r58_OVER_5YEARS;
	}
	public BigDecimal getR58_TOTAL() {
		return R58_TOTAL;
	}
	public void setR58_TOTAL(BigDecimal r58_TOTAL) {
		R58_TOTAL = r58_TOTAL;
	}
	public BigDecimal getR59_1_DAY() {
		return R59_1_DAY;
	}
	public void setR59_1_DAY(BigDecimal r59_1_DAY) {
		R59_1_DAY = r59_1_DAY;
	}
	public BigDecimal getR59_2TO7_DAYS() {
		return R59_2TO7_DAYS;
	}
	public void setR59_2TO7_DAYS(BigDecimal r59_2to7_DAYS) {
		R59_2TO7_DAYS = r59_2to7_DAYS;
	}
	public BigDecimal getR59_8TO14_DAYS() {
		return R59_8TO14_DAYS;
	}
	public void setR59_8TO14_DAYS(BigDecimal r59_8to14_DAYS) {
		R59_8TO14_DAYS = r59_8to14_DAYS;
	}
	public BigDecimal getR59_15TO30_DAYS() {
		return R59_15TO30_DAYS;
	}
	public void setR59_15TO30_DAYS(BigDecimal r59_15to30_DAYS) {
		R59_15TO30_DAYS = r59_15to30_DAYS;
	}
	public BigDecimal getR59_31DAYS_UPTO_2MONTHS() {
		return R59_31DAYS_UPTO_2MONTHS;
	}
	public void setR59_31DAYS_UPTO_2MONTHS(BigDecimal r59_31days_UPTO_2MONTHS) {
		R59_31DAYS_UPTO_2MONTHS = r59_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR59_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R59_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR59_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r59_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R59_MORETHAN_2MONTHS_UPTO_3MONHTS = r59_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR59_OVER_3MONTHS_UPTO_6MONTHS() {
		return R59_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR59_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r59_OVER_3MONTHS_UPTO_6MONTHS) {
		R59_OVER_3MONTHS_UPTO_6MONTHS = r59_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR59_OVER_6MONTHS_UPTO_1YEAR() {
		return R59_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR59_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r59_OVER_6MONTHS_UPTO_1YEAR) {
		R59_OVER_6MONTHS_UPTO_1YEAR = r59_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR59_OVER_1YEAR_UPTO_3YEARS() {
		return R59_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR59_OVER_1YEAR_UPTO_3YEARS(BigDecimal r59_OVER_1YEAR_UPTO_3YEARS) {
		R59_OVER_1YEAR_UPTO_3YEARS = r59_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR59_OVER_3YEARS_UPTO_5YEARS() {
		return R59_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR59_OVER_3YEARS_UPTO_5YEARS(BigDecimal r59_OVER_3YEARS_UPTO_5YEARS) {
		R59_OVER_3YEARS_UPTO_5YEARS = r59_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR59_OVER_5YEARS() {
		return R59_OVER_5YEARS;
	}
	public void setR59_OVER_5YEARS(BigDecimal r59_OVER_5YEARS) {
		R59_OVER_5YEARS = r59_OVER_5YEARS;
	}
	public BigDecimal getR59_TOTAL() {
		return R59_TOTAL;
	}
	public void setR59_TOTAL(BigDecimal r59_TOTAL) {
		R59_TOTAL = r59_TOTAL;
	}
	public BigDecimal getR60_1_DAY() {
		return R60_1_DAY;
	}
	public void setR60_1_DAY(BigDecimal r60_1_DAY) {
		R60_1_DAY = r60_1_DAY;
	}
	public BigDecimal getR60_2TO7_DAYS() {
		return R60_2TO7_DAYS;
	}
	public void setR60_2TO7_DAYS(BigDecimal r60_2to7_DAYS) {
		R60_2TO7_DAYS = r60_2to7_DAYS;
	}
	public BigDecimal getR60_8TO14_DAYS() {
		return R60_8TO14_DAYS;
	}
	public void setR60_8TO14_DAYS(BigDecimal r60_8to14_DAYS) {
		R60_8TO14_DAYS = r60_8to14_DAYS;
	}
	public BigDecimal getR60_15TO30_DAYS() {
		return R60_15TO30_DAYS;
	}
	public void setR60_15TO30_DAYS(BigDecimal r60_15to30_DAYS) {
		R60_15TO30_DAYS = r60_15to30_DAYS;
	}
	public BigDecimal getR60_31DAYS_UPTO_2MONTHS() {
		return R60_31DAYS_UPTO_2MONTHS;
	}
	public void setR60_31DAYS_UPTO_2MONTHS(BigDecimal r60_31days_UPTO_2MONTHS) {
		R60_31DAYS_UPTO_2MONTHS = r60_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR60_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R60_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR60_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r60_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R60_MORETHAN_2MONTHS_UPTO_3MONHTS = r60_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR60_OVER_3MONTHS_UPTO_6MONTHS() {
		return R60_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR60_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r60_OVER_3MONTHS_UPTO_6MONTHS) {
		R60_OVER_3MONTHS_UPTO_6MONTHS = r60_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR60_OVER_6MONTHS_UPTO_1YEAR() {
		return R60_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR60_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r60_OVER_6MONTHS_UPTO_1YEAR) {
		R60_OVER_6MONTHS_UPTO_1YEAR = r60_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR60_OVER_1YEAR_UPTO_3YEARS() {
		return R60_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR60_OVER_1YEAR_UPTO_3YEARS(BigDecimal r60_OVER_1YEAR_UPTO_3YEARS) {
		R60_OVER_1YEAR_UPTO_3YEARS = r60_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR60_OVER_3YEARS_UPTO_5YEARS() {
		return R60_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR60_OVER_3YEARS_UPTO_5YEARS(BigDecimal r60_OVER_3YEARS_UPTO_5YEARS) {
		R60_OVER_3YEARS_UPTO_5YEARS = r60_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR60_OVER_5YEARS() {
		return R60_OVER_5YEARS;
	}
	public void setR60_OVER_5YEARS(BigDecimal r60_OVER_5YEARS) {
		R60_OVER_5YEARS = r60_OVER_5YEARS;
	}
	public BigDecimal getR60_TOTAL() {
		return R60_TOTAL;
	}
	public void setR60_TOTAL(BigDecimal r60_TOTAL) {
		R60_TOTAL = r60_TOTAL;
	}
	public BigDecimal getR61_1_DAY() {
		return R61_1_DAY;
	}
	public void setR61_1_DAY(BigDecimal r61_1_DAY) {
		R61_1_DAY = r61_1_DAY;
	}
	public BigDecimal getR61_2TO7_DAYS() {
		return R61_2TO7_DAYS;
	}
	public void setR61_2TO7_DAYS(BigDecimal r61_2to7_DAYS) {
		R61_2TO7_DAYS = r61_2to7_DAYS;
	}
	public BigDecimal getR61_8TO14_DAYS() {
		return R61_8TO14_DAYS;
	}
	public void setR61_8TO14_DAYS(BigDecimal r61_8to14_DAYS) {
		R61_8TO14_DAYS = r61_8to14_DAYS;
	}
	public BigDecimal getR61_15TO30_DAYS() {
		return R61_15TO30_DAYS;
	}
	public void setR61_15TO30_DAYS(BigDecimal r61_15to30_DAYS) {
		R61_15TO30_DAYS = r61_15to30_DAYS;
	}
	public BigDecimal getR61_31DAYS_UPTO_2MONTHS() {
		return R61_31DAYS_UPTO_2MONTHS;
	}
	public void setR61_31DAYS_UPTO_2MONTHS(BigDecimal r61_31days_UPTO_2MONTHS) {
		R61_31DAYS_UPTO_2MONTHS = r61_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR61_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R61_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR61_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r61_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R61_MORETHAN_2MONTHS_UPTO_3MONHTS = r61_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR61_OVER_3MONTHS_UPTO_6MONTHS() {
		return R61_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR61_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r61_OVER_3MONTHS_UPTO_6MONTHS) {
		R61_OVER_3MONTHS_UPTO_6MONTHS = r61_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR61_OVER_6MONTHS_UPTO_1YEAR() {
		return R61_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR61_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r61_OVER_6MONTHS_UPTO_1YEAR) {
		R61_OVER_6MONTHS_UPTO_1YEAR = r61_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR61_OVER_1YEAR_UPTO_3YEARS() {
		return R61_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR61_OVER_1YEAR_UPTO_3YEARS(BigDecimal r61_OVER_1YEAR_UPTO_3YEARS) {
		R61_OVER_1YEAR_UPTO_3YEARS = r61_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR61_OVER_3YEARS_UPTO_5YEARS() {
		return R61_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR61_OVER_3YEARS_UPTO_5YEARS(BigDecimal r61_OVER_3YEARS_UPTO_5YEARS) {
		R61_OVER_3YEARS_UPTO_5YEARS = r61_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR61_OVER_5YEARS() {
		return R61_OVER_5YEARS;
	}
	public void setR61_OVER_5YEARS(BigDecimal r61_OVER_5YEARS) {
		R61_OVER_5YEARS = r61_OVER_5YEARS;
	}
	public BigDecimal getR61_TOTAL() {
		return R61_TOTAL;
	}
	public void setR61_TOTAL(BigDecimal r61_TOTAL) {
		R61_TOTAL = r61_TOTAL;
	}
	public BigDecimal getR62_1_DAY() {
		return R62_1_DAY;
	}
	public void setR62_1_DAY(BigDecimal r62_1_DAY) {
		R62_1_DAY = r62_1_DAY;
	}
	public BigDecimal getR62_2TO7_DAYS() {
		return R62_2TO7_DAYS;
	}
	public void setR62_2TO7_DAYS(BigDecimal r62_2to7_DAYS) {
		R62_2TO7_DAYS = r62_2to7_DAYS;
	}
	public BigDecimal getR62_8TO14_DAYS() {
		return R62_8TO14_DAYS;
	}
	public void setR62_8TO14_DAYS(BigDecimal r62_8to14_DAYS) {
		R62_8TO14_DAYS = r62_8to14_DAYS;
	}
	public BigDecimal getR62_15TO30_DAYS() {
		return R62_15TO30_DAYS;
	}
	public void setR62_15TO30_DAYS(BigDecimal r62_15to30_DAYS) {
		R62_15TO30_DAYS = r62_15to30_DAYS;
	}
	public BigDecimal getR62_31DAYS_UPTO_2MONTHS() {
		return R62_31DAYS_UPTO_2MONTHS;
	}
	public void setR62_31DAYS_UPTO_2MONTHS(BigDecimal r62_31days_UPTO_2MONTHS) {
		R62_31DAYS_UPTO_2MONTHS = r62_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR62_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R62_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR62_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r62_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R62_MORETHAN_2MONTHS_UPTO_3MONHTS = r62_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR62_OVER_3MONTHS_UPTO_6MONTHS() {
		return R62_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR62_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r62_OVER_3MONTHS_UPTO_6MONTHS) {
		R62_OVER_3MONTHS_UPTO_6MONTHS = r62_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR62_OVER_6MONTHS_UPTO_1YEAR() {
		return R62_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR62_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r62_OVER_6MONTHS_UPTO_1YEAR) {
		R62_OVER_6MONTHS_UPTO_1YEAR = r62_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR62_OVER_1YEAR_UPTO_3YEARS() {
		return R62_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR62_OVER_1YEAR_UPTO_3YEARS(BigDecimal r62_OVER_1YEAR_UPTO_3YEARS) {
		R62_OVER_1YEAR_UPTO_3YEARS = r62_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR62_OVER_3YEARS_UPTO_5YEARS() {
		return R62_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR62_OVER_3YEARS_UPTO_5YEARS(BigDecimal r62_OVER_3YEARS_UPTO_5YEARS) {
		R62_OVER_3YEARS_UPTO_5YEARS = r62_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR62_OVER_5YEARS() {
		return R62_OVER_5YEARS;
	}
	public void setR62_OVER_5YEARS(BigDecimal r62_OVER_5YEARS) {
		R62_OVER_5YEARS = r62_OVER_5YEARS;
	}
	public BigDecimal getR62_TOTAL() {
		return R62_TOTAL;
	}
	public void setR62_TOTAL(BigDecimal r62_TOTAL) {
		R62_TOTAL = r62_TOTAL;
	}
	public BigDecimal getR63_1_DAY() {
		return R63_1_DAY;
	}
	public void setR63_1_DAY(BigDecimal r63_1_DAY) {
		R63_1_DAY = r63_1_DAY;
	}
	public BigDecimal getR63_2TO7_DAYS() {
		return R63_2TO7_DAYS;
	}
	public void setR63_2TO7_DAYS(BigDecimal r63_2to7_DAYS) {
		R63_2TO7_DAYS = r63_2to7_DAYS;
	}
	public BigDecimal getR63_8TO14_DAYS() {
		return R63_8TO14_DAYS;
	}
	public void setR63_8TO14_DAYS(BigDecimal r63_8to14_DAYS) {
		R63_8TO14_DAYS = r63_8to14_DAYS;
	}
	public BigDecimal getR63_15TO30_DAYS() {
		return R63_15TO30_DAYS;
	}
	public void setR63_15TO30_DAYS(BigDecimal r63_15to30_DAYS) {
		R63_15TO30_DAYS = r63_15to30_DAYS;
	}
	public BigDecimal getR63_31DAYS_UPTO_2MONTHS() {
		return R63_31DAYS_UPTO_2MONTHS;
	}
	public void setR63_31DAYS_UPTO_2MONTHS(BigDecimal r63_31days_UPTO_2MONTHS) {
		R63_31DAYS_UPTO_2MONTHS = r63_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR63_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R63_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR63_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r63_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R63_MORETHAN_2MONTHS_UPTO_3MONHTS = r63_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR63_OVER_3MONTHS_UPTO_6MONTHS() {
		return R63_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR63_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r63_OVER_3MONTHS_UPTO_6MONTHS) {
		R63_OVER_3MONTHS_UPTO_6MONTHS = r63_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR63_OVER_6MONTHS_UPTO_1YEAR() {
		return R63_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR63_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r63_OVER_6MONTHS_UPTO_1YEAR) {
		R63_OVER_6MONTHS_UPTO_1YEAR = r63_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR63_OVER_1YEAR_UPTO_3YEARS() {
		return R63_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR63_OVER_1YEAR_UPTO_3YEARS(BigDecimal r63_OVER_1YEAR_UPTO_3YEARS) {
		R63_OVER_1YEAR_UPTO_3YEARS = r63_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR63_OVER_3YEARS_UPTO_5YEARS() {
		return R63_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR63_OVER_3YEARS_UPTO_5YEARS(BigDecimal r63_OVER_3YEARS_UPTO_5YEARS) {
		R63_OVER_3YEARS_UPTO_5YEARS = r63_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR63_OVER_5YEARS() {
		return R63_OVER_5YEARS;
	}
	public void setR63_OVER_5YEARS(BigDecimal r63_OVER_5YEARS) {
		R63_OVER_5YEARS = r63_OVER_5YEARS;
	}
	public BigDecimal getR63_TOTAL() {
		return R63_TOTAL;
	}
	public void setR63_TOTAL(BigDecimal r63_TOTAL) {
		R63_TOTAL = r63_TOTAL;
	}
	public BigDecimal getR64_1_DAY() {
		return R64_1_DAY;
	}
	public void setR64_1_DAY(BigDecimal r64_1_DAY) {
		R64_1_DAY = r64_1_DAY;
	}
	public BigDecimal getR64_2TO7_DAYS() {
		return R64_2TO7_DAYS;
	}
	public void setR64_2TO7_DAYS(BigDecimal r64_2to7_DAYS) {
		R64_2TO7_DAYS = r64_2to7_DAYS;
	}
	public BigDecimal getR64_8TO14_DAYS() {
		return R64_8TO14_DAYS;
	}
	public void setR64_8TO14_DAYS(BigDecimal r64_8to14_DAYS) {
		R64_8TO14_DAYS = r64_8to14_DAYS;
	}
	public BigDecimal getR64_15TO30_DAYS() {
		return R64_15TO30_DAYS;
	}
	public void setR64_15TO30_DAYS(BigDecimal r64_15to30_DAYS) {
		R64_15TO30_DAYS = r64_15to30_DAYS;
	}
	public BigDecimal getR64_31DAYS_UPTO_2MONTHS() {
		return R64_31DAYS_UPTO_2MONTHS;
	}
	public void setR64_31DAYS_UPTO_2MONTHS(BigDecimal r64_31days_UPTO_2MONTHS) {
		R64_31DAYS_UPTO_2MONTHS = r64_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR64_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R64_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR64_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r64_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R64_MORETHAN_2MONTHS_UPTO_3MONHTS = r64_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR64_OVER_3MONTHS_UPTO_6MONTHS() {
		return R64_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR64_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r64_OVER_3MONTHS_UPTO_6MONTHS) {
		R64_OVER_3MONTHS_UPTO_6MONTHS = r64_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR64_OVER_6MONTHS_UPTO_1YEAR() {
		return R64_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR64_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r64_OVER_6MONTHS_UPTO_1YEAR) {
		R64_OVER_6MONTHS_UPTO_1YEAR = r64_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR64_OVER_1YEAR_UPTO_3YEARS() {
		return R64_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR64_OVER_1YEAR_UPTO_3YEARS(BigDecimal r64_OVER_1YEAR_UPTO_3YEARS) {
		R64_OVER_1YEAR_UPTO_3YEARS = r64_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR64_OVER_3YEARS_UPTO_5YEARS() {
		return R64_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR64_OVER_3YEARS_UPTO_5YEARS(BigDecimal r64_OVER_3YEARS_UPTO_5YEARS) {
		R64_OVER_3YEARS_UPTO_5YEARS = r64_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR64_OVER_5YEARS() {
		return R64_OVER_5YEARS;
	}
	public void setR64_OVER_5YEARS(BigDecimal r64_OVER_5YEARS) {
		R64_OVER_5YEARS = r64_OVER_5YEARS;
	}
	public BigDecimal getR64_TOTAL() {
		return R64_TOTAL;
	}
	public void setR64_TOTAL(BigDecimal r64_TOTAL) {
		R64_TOTAL = r64_TOTAL;
	}
	public BigDecimal getR65_1_DAY() {
		return R65_1_DAY;
	}
	public void setR65_1_DAY(BigDecimal r65_1_DAY) {
		R65_1_DAY = r65_1_DAY;
	}
	public BigDecimal getR65_2TO7_DAYS() {
		return R65_2TO7_DAYS;
	}
	public void setR65_2TO7_DAYS(BigDecimal r65_2to7_DAYS) {
		R65_2TO7_DAYS = r65_2to7_DAYS;
	}
	public BigDecimal getR65_8TO14_DAYS() {
		return R65_8TO14_DAYS;
	}
	public void setR65_8TO14_DAYS(BigDecimal r65_8to14_DAYS) {
		R65_8TO14_DAYS = r65_8to14_DAYS;
	}
	public BigDecimal getR65_15TO30_DAYS() {
		return R65_15TO30_DAYS;
	}
	public void setR65_15TO30_DAYS(BigDecimal r65_15to30_DAYS) {
		R65_15TO30_DAYS = r65_15to30_DAYS;
	}
	public BigDecimal getR65_31DAYS_UPTO_2MONTHS() {
		return R65_31DAYS_UPTO_2MONTHS;
	}
	public void setR65_31DAYS_UPTO_2MONTHS(BigDecimal r65_31days_UPTO_2MONTHS) {
		R65_31DAYS_UPTO_2MONTHS = r65_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR65_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R65_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR65_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r65_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R65_MORETHAN_2MONTHS_UPTO_3MONHTS = r65_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR65_OVER_3MONTHS_UPTO_6MONTHS() {
		return R65_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR65_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r65_OVER_3MONTHS_UPTO_6MONTHS) {
		R65_OVER_3MONTHS_UPTO_6MONTHS = r65_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR65_OVER_6MONTHS_UPTO_1YEAR() {
		return R65_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR65_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r65_OVER_6MONTHS_UPTO_1YEAR) {
		R65_OVER_6MONTHS_UPTO_1YEAR = r65_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR65_OVER_1YEAR_UPTO_3YEARS() {
		return R65_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR65_OVER_1YEAR_UPTO_3YEARS(BigDecimal r65_OVER_1YEAR_UPTO_3YEARS) {
		R65_OVER_1YEAR_UPTO_3YEARS = r65_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR65_OVER_3YEARS_UPTO_5YEARS() {
		return R65_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR65_OVER_3YEARS_UPTO_5YEARS(BigDecimal r65_OVER_3YEARS_UPTO_5YEARS) {
		R65_OVER_3YEARS_UPTO_5YEARS = r65_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR65_OVER_5YEARS() {
		return R65_OVER_5YEARS;
	}
	public void setR65_OVER_5YEARS(BigDecimal r65_OVER_5YEARS) {
		R65_OVER_5YEARS = r65_OVER_5YEARS;
	}
	public BigDecimal getR65_TOTAL() {
		return R65_TOTAL;
	}
	public void setR65_TOTAL(BigDecimal r65_TOTAL) {
		R65_TOTAL = r65_TOTAL;
	}
	public BigDecimal getR66_1_DAY() {
		return R66_1_DAY;
	}
	public void setR66_1_DAY(BigDecimal r66_1_DAY) {
		R66_1_DAY = r66_1_DAY;
	}
	public BigDecimal getR66_2TO7_DAYS() {
		return R66_2TO7_DAYS;
	}
	public void setR66_2TO7_DAYS(BigDecimal r66_2to7_DAYS) {
		R66_2TO7_DAYS = r66_2to7_DAYS;
	}
	public BigDecimal getR66_8TO14_DAYS() {
		return R66_8TO14_DAYS;
	}
	public void setR66_8TO14_DAYS(BigDecimal r66_8to14_DAYS) {
		R66_8TO14_DAYS = r66_8to14_DAYS;
	}
	public BigDecimal getR66_15TO30_DAYS() {
		return R66_15TO30_DAYS;
	}
	public void setR66_15TO30_DAYS(BigDecimal r66_15to30_DAYS) {
		R66_15TO30_DAYS = r66_15to30_DAYS;
	}
	public BigDecimal getR66_31DAYS_UPTO_2MONTHS() {
		return R66_31DAYS_UPTO_2MONTHS;
	}
	public void setR66_31DAYS_UPTO_2MONTHS(BigDecimal r66_31days_UPTO_2MONTHS) {
		R66_31DAYS_UPTO_2MONTHS = r66_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR66_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R66_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR66_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r66_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R66_MORETHAN_2MONTHS_UPTO_3MONHTS = r66_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR66_OVER_3MONTHS_UPTO_6MONTHS() {
		return R66_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR66_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r66_OVER_3MONTHS_UPTO_6MONTHS) {
		R66_OVER_3MONTHS_UPTO_6MONTHS = r66_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR66_OVER_6MONTHS_UPTO_1YEAR() {
		return R66_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR66_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r66_OVER_6MONTHS_UPTO_1YEAR) {
		R66_OVER_6MONTHS_UPTO_1YEAR = r66_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR66_OVER_1YEAR_UPTO_3YEARS() {
		return R66_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR66_OVER_1YEAR_UPTO_3YEARS(BigDecimal r66_OVER_1YEAR_UPTO_3YEARS) {
		R66_OVER_1YEAR_UPTO_3YEARS = r66_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR66_OVER_3YEARS_UPTO_5YEARS() {
		return R66_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR66_OVER_3YEARS_UPTO_5YEARS(BigDecimal r66_OVER_3YEARS_UPTO_5YEARS) {
		R66_OVER_3YEARS_UPTO_5YEARS = r66_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR66_OVER_5YEARS() {
		return R66_OVER_5YEARS;
	}
	public void setR66_OVER_5YEARS(BigDecimal r66_OVER_5YEARS) {
		R66_OVER_5YEARS = r66_OVER_5YEARS;
	}
	public BigDecimal getR66_TOTAL() {
		return R66_TOTAL;
	}
	public void setR66_TOTAL(BigDecimal r66_TOTAL) {
		R66_TOTAL = r66_TOTAL;
	}
	public BigDecimal getR67_1_DAY() {
		return R67_1_DAY;
	}
	public void setR67_1_DAY(BigDecimal r67_1_DAY) {
		R67_1_DAY = r67_1_DAY;
	}
	public BigDecimal getR67_2TO7_DAYS() {
		return R67_2TO7_DAYS;
	}
	public void setR67_2TO7_DAYS(BigDecimal r67_2to7_DAYS) {
		R67_2TO7_DAYS = r67_2to7_DAYS;
	}
	public BigDecimal getR67_8TO14_DAYS() {
		return R67_8TO14_DAYS;
	}
	public void setR67_8TO14_DAYS(BigDecimal r67_8to14_DAYS) {
		R67_8TO14_DAYS = r67_8to14_DAYS;
	}
	public BigDecimal getR67_15TO30_DAYS() {
		return R67_15TO30_DAYS;
	}
	public void setR67_15TO30_DAYS(BigDecimal r67_15to30_DAYS) {
		R67_15TO30_DAYS = r67_15to30_DAYS;
	}
	public BigDecimal getR67_31DAYS_UPTO_2MONTHS() {
		return R67_31DAYS_UPTO_2MONTHS;
	}
	public void setR67_31DAYS_UPTO_2MONTHS(BigDecimal r67_31days_UPTO_2MONTHS) {
		R67_31DAYS_UPTO_2MONTHS = r67_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR67_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R67_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR67_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r67_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R67_MORETHAN_2MONTHS_UPTO_3MONHTS = r67_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR67_OVER_3MONTHS_UPTO_6MONTHS() {
		return R67_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR67_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r67_OVER_3MONTHS_UPTO_6MONTHS) {
		R67_OVER_3MONTHS_UPTO_6MONTHS = r67_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR67_OVER_6MONTHS_UPTO_1YEAR() {
		return R67_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR67_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r67_OVER_6MONTHS_UPTO_1YEAR) {
		R67_OVER_6MONTHS_UPTO_1YEAR = r67_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR67_OVER_1YEAR_UPTO_3YEARS() {
		return R67_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR67_OVER_1YEAR_UPTO_3YEARS(BigDecimal r67_OVER_1YEAR_UPTO_3YEARS) {
		R67_OVER_1YEAR_UPTO_3YEARS = r67_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR67_OVER_3YEARS_UPTO_5YEARS() {
		return R67_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR67_OVER_3YEARS_UPTO_5YEARS(BigDecimal r67_OVER_3YEARS_UPTO_5YEARS) {
		R67_OVER_3YEARS_UPTO_5YEARS = r67_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR67_OVER_5YEARS() {
		return R67_OVER_5YEARS;
	}
	public void setR67_OVER_5YEARS(BigDecimal r67_OVER_5YEARS) {
		R67_OVER_5YEARS = r67_OVER_5YEARS;
	}
	public BigDecimal getR67_TOTAL() {
		return R67_TOTAL;
	}
	public void setR67_TOTAL(BigDecimal r67_TOTAL) {
		R67_TOTAL = r67_TOTAL;
	}
	public BigDecimal getR68_1_DAY() {
		return R68_1_DAY;
	}
	public void setR68_1_DAY(BigDecimal r68_1_DAY) {
		R68_1_DAY = r68_1_DAY;
	}
	public BigDecimal getR68_2TO7_DAYS() {
		return R68_2TO7_DAYS;
	}
	public void setR68_2TO7_DAYS(BigDecimal r68_2to7_DAYS) {
		R68_2TO7_DAYS = r68_2to7_DAYS;
	}
	public BigDecimal getR68_8TO14_DAYS() {
		return R68_8TO14_DAYS;
	}
	public void setR68_8TO14_DAYS(BigDecimal r68_8to14_DAYS) {
		R68_8TO14_DAYS = r68_8to14_DAYS;
	}
	public BigDecimal getR68_15TO30_DAYS() {
		return R68_15TO30_DAYS;
	}
	public void setR68_15TO30_DAYS(BigDecimal r68_15to30_DAYS) {
		R68_15TO30_DAYS = r68_15to30_DAYS;
	}
	public BigDecimal getR68_31DAYS_UPTO_2MONTHS() {
		return R68_31DAYS_UPTO_2MONTHS;
	}
	public void setR68_31DAYS_UPTO_2MONTHS(BigDecimal r68_31days_UPTO_2MONTHS) {
		R68_31DAYS_UPTO_2MONTHS = r68_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR68_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R68_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR68_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r68_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R68_MORETHAN_2MONTHS_UPTO_3MONHTS = r68_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR68_OVER_3MONTHS_UPTO_6MONTHS() {
		return R68_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR68_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r68_OVER_3MONTHS_UPTO_6MONTHS) {
		R68_OVER_3MONTHS_UPTO_6MONTHS = r68_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR68_OVER_6MONTHS_UPTO_1YEAR() {
		return R68_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR68_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r68_OVER_6MONTHS_UPTO_1YEAR) {
		R68_OVER_6MONTHS_UPTO_1YEAR = r68_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR68_OVER_1YEAR_UPTO_3YEARS() {
		return R68_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR68_OVER_1YEAR_UPTO_3YEARS(BigDecimal r68_OVER_1YEAR_UPTO_3YEARS) {
		R68_OVER_1YEAR_UPTO_3YEARS = r68_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR68_OVER_3YEARS_UPTO_5YEARS() {
		return R68_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR68_OVER_3YEARS_UPTO_5YEARS(BigDecimal r68_OVER_3YEARS_UPTO_5YEARS) {
		R68_OVER_3YEARS_UPTO_5YEARS = r68_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR68_OVER_5YEARS() {
		return R68_OVER_5YEARS;
	}
	public void setR68_OVER_5YEARS(BigDecimal r68_OVER_5YEARS) {
		R68_OVER_5YEARS = r68_OVER_5YEARS;
	}
	public BigDecimal getR68_TOTAL() {
		return R68_TOTAL;
	}
	public void setR68_TOTAL(BigDecimal r68_TOTAL) {
		R68_TOTAL = r68_TOTAL;
	}
	public BigDecimal getR69_1_DAY() {
		return R69_1_DAY;
	}
	public void setR69_1_DAY(BigDecimal r69_1_DAY) {
		R69_1_DAY = r69_1_DAY;
	}
	public BigDecimal getR69_2TO7_DAYS() {
		return R69_2TO7_DAYS;
	}
	public void setR69_2TO7_DAYS(BigDecimal r69_2to7_DAYS) {
		R69_2TO7_DAYS = r69_2to7_DAYS;
	}
	public BigDecimal getR69_8TO14_DAYS() {
		return R69_8TO14_DAYS;
	}
	public void setR69_8TO14_DAYS(BigDecimal r69_8to14_DAYS) {
		R69_8TO14_DAYS = r69_8to14_DAYS;
	}
	public BigDecimal getR69_15TO30_DAYS() {
		return R69_15TO30_DAYS;
	}
	public void setR69_15TO30_DAYS(BigDecimal r69_15to30_DAYS) {
		R69_15TO30_DAYS = r69_15to30_DAYS;
	}
	public BigDecimal getR69_31DAYS_UPTO_2MONTHS() {
		return R69_31DAYS_UPTO_2MONTHS;
	}
	public void setR69_31DAYS_UPTO_2MONTHS(BigDecimal r69_31days_UPTO_2MONTHS) {
		R69_31DAYS_UPTO_2MONTHS = r69_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR69_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R69_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR69_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r69_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R69_MORETHAN_2MONTHS_UPTO_3MONHTS = r69_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR69_OVER_3MONTHS_UPTO_6MONTHS() {
		return R69_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR69_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r69_OVER_3MONTHS_UPTO_6MONTHS) {
		R69_OVER_3MONTHS_UPTO_6MONTHS = r69_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR69_OVER_6MONTHS_UPTO_1YEAR() {
		return R69_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR69_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r69_OVER_6MONTHS_UPTO_1YEAR) {
		R69_OVER_6MONTHS_UPTO_1YEAR = r69_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR69_OVER_1YEAR_UPTO_3YEARS() {
		return R69_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR69_OVER_1YEAR_UPTO_3YEARS(BigDecimal r69_OVER_1YEAR_UPTO_3YEARS) {
		R69_OVER_1YEAR_UPTO_3YEARS = r69_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR69_OVER_3YEARS_UPTO_5YEARS() {
		return R69_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR69_OVER_3YEARS_UPTO_5YEARS(BigDecimal r69_OVER_3YEARS_UPTO_5YEARS) {
		R69_OVER_3YEARS_UPTO_5YEARS = r69_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR69_OVER_5YEARS() {
		return R69_OVER_5YEARS;
	}
	public void setR69_OVER_5YEARS(BigDecimal r69_OVER_5YEARS) {
		R69_OVER_5YEARS = r69_OVER_5YEARS;
	}
	public BigDecimal getR69_TOTAL() {
		return R69_TOTAL;
	}
	public void setR69_TOTAL(BigDecimal r69_TOTAL) {
		R69_TOTAL = r69_TOTAL;
	}
	public BigDecimal getR70_1_DAY() {
		return R70_1_DAY;
	}
	public void setR70_1_DAY(BigDecimal r70_1_DAY) {
		R70_1_DAY = r70_1_DAY;
	}
	public BigDecimal getR70_2TO7_DAYS() {
		return R70_2TO7_DAYS;
	}
	public void setR70_2TO7_DAYS(BigDecimal r70_2to7_DAYS) {
		R70_2TO7_DAYS = r70_2to7_DAYS;
	}
	public BigDecimal getR70_8TO14_DAYS() {
		return R70_8TO14_DAYS;
	}
	public void setR70_8TO14_DAYS(BigDecimal r70_8to14_DAYS) {
		R70_8TO14_DAYS = r70_8to14_DAYS;
	}
	public BigDecimal getR70_15TO30_DAYS() {
		return R70_15TO30_DAYS;
	}
	public void setR70_15TO30_DAYS(BigDecimal r70_15to30_DAYS) {
		R70_15TO30_DAYS = r70_15to30_DAYS;
	}
	public BigDecimal getR70_31DAYS_UPTO_2MONTHS() {
		return R70_31DAYS_UPTO_2MONTHS;
	}
	public void setR70_31DAYS_UPTO_2MONTHS(BigDecimal r70_31days_UPTO_2MONTHS) {
		R70_31DAYS_UPTO_2MONTHS = r70_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR70_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R70_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR70_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r70_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R70_MORETHAN_2MONTHS_UPTO_3MONHTS = r70_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR70_OVER_3MONTHS_UPTO_6MONTHS() {
		return R70_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR70_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r70_OVER_3MONTHS_UPTO_6MONTHS) {
		R70_OVER_3MONTHS_UPTO_6MONTHS = r70_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR70_OVER_6MONTHS_UPTO_1YEAR() {
		return R70_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR70_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r70_OVER_6MONTHS_UPTO_1YEAR) {
		R70_OVER_6MONTHS_UPTO_1YEAR = r70_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR70_OVER_1YEAR_UPTO_3YEARS() {
		return R70_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR70_OVER_1YEAR_UPTO_3YEARS(BigDecimal r70_OVER_1YEAR_UPTO_3YEARS) {
		R70_OVER_1YEAR_UPTO_3YEARS = r70_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR70_OVER_3YEARS_UPTO_5YEARS() {
		return R70_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR70_OVER_3YEARS_UPTO_5YEARS(BigDecimal r70_OVER_3YEARS_UPTO_5YEARS) {
		R70_OVER_3YEARS_UPTO_5YEARS = r70_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR70_OVER_5YEARS() {
		return R70_OVER_5YEARS;
	}
	public void setR70_OVER_5YEARS(BigDecimal r70_OVER_5YEARS) {
		R70_OVER_5YEARS = r70_OVER_5YEARS;
	}
	public BigDecimal getR70_TOTAL() {
		return R70_TOTAL;
	}
	public void setR70_TOTAL(BigDecimal r70_TOTAL) {
		R70_TOTAL = r70_TOTAL;
	}
	public BigDecimal getR71_1_DAY() {
		return R71_1_DAY;
	}
	public void setR71_1_DAY(BigDecimal r71_1_DAY) {
		R71_1_DAY = r71_1_DAY;
	}
	public BigDecimal getR71_2TO7_DAYS() {
		return R71_2TO7_DAYS;
	}
	public void setR71_2TO7_DAYS(BigDecimal r71_2to7_DAYS) {
		R71_2TO7_DAYS = r71_2to7_DAYS;
	}
	public BigDecimal getR71_8TO14_DAYS() {
		return R71_8TO14_DAYS;
	}
	public void setR71_8TO14_DAYS(BigDecimal r71_8to14_DAYS) {
		R71_8TO14_DAYS = r71_8to14_DAYS;
	}
	public BigDecimal getR71_15TO30_DAYS() {
		return R71_15TO30_DAYS;
	}
	public void setR71_15TO30_DAYS(BigDecimal r71_15to30_DAYS) {
		R71_15TO30_DAYS = r71_15to30_DAYS;
	}
	public BigDecimal getR71_31DAYS_UPTO_2MONTHS() {
		return R71_31DAYS_UPTO_2MONTHS;
	}
	public void setR71_31DAYS_UPTO_2MONTHS(BigDecimal r71_31days_UPTO_2MONTHS) {
		R71_31DAYS_UPTO_2MONTHS = r71_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR71_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R71_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR71_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r71_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R71_MORETHAN_2MONTHS_UPTO_3MONHTS = r71_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR71_OVER_3MONTHS_UPTO_6MONTHS() {
		return R71_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR71_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r71_OVER_3MONTHS_UPTO_6MONTHS) {
		R71_OVER_3MONTHS_UPTO_6MONTHS = r71_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR71_OVER_6MONTHS_UPTO_1YEAR() {
		return R71_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR71_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r71_OVER_6MONTHS_UPTO_1YEAR) {
		R71_OVER_6MONTHS_UPTO_1YEAR = r71_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR71_OVER_1YEAR_UPTO_3YEARS() {
		return R71_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR71_OVER_1YEAR_UPTO_3YEARS(BigDecimal r71_OVER_1YEAR_UPTO_3YEARS) {
		R71_OVER_1YEAR_UPTO_3YEARS = r71_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR71_OVER_3YEARS_UPTO_5YEARS() {
		return R71_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR71_OVER_3YEARS_UPTO_5YEARS(BigDecimal r71_OVER_3YEARS_UPTO_5YEARS) {
		R71_OVER_3YEARS_UPTO_5YEARS = r71_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR71_OVER_5YEARS() {
		return R71_OVER_5YEARS;
	}
	public void setR71_OVER_5YEARS(BigDecimal r71_OVER_5YEARS) {
		R71_OVER_5YEARS = r71_OVER_5YEARS;
	}
	public BigDecimal getR71_TOTAL() {
		return R71_TOTAL;
	}
	public void setR71_TOTAL(BigDecimal r71_TOTAL) {
		R71_TOTAL = r71_TOTAL;
	}
	public BigDecimal getR72_1_DAY() {
		return R72_1_DAY;
	}
	public void setR72_1_DAY(BigDecimal r72_1_DAY) {
		R72_1_DAY = r72_1_DAY;
	}
	public BigDecimal getR72_2TO7_DAYS() {
		return R72_2TO7_DAYS;
	}
	public void setR72_2TO7_DAYS(BigDecimal r72_2to7_DAYS) {
		R72_2TO7_DAYS = r72_2to7_DAYS;
	}
	public BigDecimal getR72_8TO14_DAYS() {
		return R72_8TO14_DAYS;
	}
	public void setR72_8TO14_DAYS(BigDecimal r72_8to14_DAYS) {
		R72_8TO14_DAYS = r72_8to14_DAYS;
	}
	public BigDecimal getR72_15TO30_DAYS() {
		return R72_15TO30_DAYS;
	}
	public void setR72_15TO30_DAYS(BigDecimal r72_15to30_DAYS) {
		R72_15TO30_DAYS = r72_15to30_DAYS;
	}
	public BigDecimal getR72_31DAYS_UPTO_2MONTHS() {
		return R72_31DAYS_UPTO_2MONTHS;
	}
	public void setR72_31DAYS_UPTO_2MONTHS(BigDecimal r72_31days_UPTO_2MONTHS) {
		R72_31DAYS_UPTO_2MONTHS = r72_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR72_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R72_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR72_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r72_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R72_MORETHAN_2MONTHS_UPTO_3MONHTS = r72_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR72_OVER_3MONTHS_UPTO_6MONTHS() {
		return R72_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR72_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r72_OVER_3MONTHS_UPTO_6MONTHS) {
		R72_OVER_3MONTHS_UPTO_6MONTHS = r72_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR72_OVER_6MONTHS_UPTO_1YEAR() {
		return R72_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR72_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r72_OVER_6MONTHS_UPTO_1YEAR) {
		R72_OVER_6MONTHS_UPTO_1YEAR = r72_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR72_OVER_1YEAR_UPTO_3YEARS() {
		return R72_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR72_OVER_1YEAR_UPTO_3YEARS(BigDecimal r72_OVER_1YEAR_UPTO_3YEARS) {
		R72_OVER_1YEAR_UPTO_3YEARS = r72_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR72_OVER_3YEARS_UPTO_5YEARS() {
		return R72_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR72_OVER_3YEARS_UPTO_5YEARS(BigDecimal r72_OVER_3YEARS_UPTO_5YEARS) {
		R72_OVER_3YEARS_UPTO_5YEARS = r72_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR72_OVER_5YEARS() {
		return R72_OVER_5YEARS;
	}
	public void setR72_OVER_5YEARS(BigDecimal r72_OVER_5YEARS) {
		R72_OVER_5YEARS = r72_OVER_5YEARS;
	}
	public BigDecimal getR72_TOTAL() {
		return R72_TOTAL;
	}
	public void setR72_TOTAL(BigDecimal r72_TOTAL) {
		R72_TOTAL = r72_TOTAL;
	}
	public BigDecimal getR73_1_DAY() {
		return R73_1_DAY;
	}
	public void setR73_1_DAY(BigDecimal r73_1_DAY) {
		R73_1_DAY = r73_1_DAY;
	}
	public BigDecimal getR73_2TO7_DAYS() {
		return R73_2TO7_DAYS;
	}
	public void setR73_2TO7_DAYS(BigDecimal r73_2to7_DAYS) {
		R73_2TO7_DAYS = r73_2to7_DAYS;
	}
	public BigDecimal getR73_8TO14_DAYS() {
		return R73_8TO14_DAYS;
	}
	public void setR73_8TO14_DAYS(BigDecimal r73_8to14_DAYS) {
		R73_8TO14_DAYS = r73_8to14_DAYS;
	}
	public BigDecimal getR73_15TO30_DAYS() {
		return R73_15TO30_DAYS;
	}
	public void setR73_15TO30_DAYS(BigDecimal r73_15to30_DAYS) {
		R73_15TO30_DAYS = r73_15to30_DAYS;
	}
	public BigDecimal getR73_31DAYS_UPTO_2MONTHS() {
		return R73_31DAYS_UPTO_2MONTHS;
	}
	public void setR73_31DAYS_UPTO_2MONTHS(BigDecimal r73_31days_UPTO_2MONTHS) {
		R73_31DAYS_UPTO_2MONTHS = r73_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR73_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R73_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR73_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r73_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R73_MORETHAN_2MONTHS_UPTO_3MONHTS = r73_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR73_OVER_3MONTHS_UPTO_6MONTHS() {
		return R73_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR73_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r73_OVER_3MONTHS_UPTO_6MONTHS) {
		R73_OVER_3MONTHS_UPTO_6MONTHS = r73_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR73_OVER_6MONTHS_UPTO_1YEAR() {
		return R73_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR73_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r73_OVER_6MONTHS_UPTO_1YEAR) {
		R73_OVER_6MONTHS_UPTO_1YEAR = r73_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR73_OVER_1YEAR_UPTO_3YEARS() {
		return R73_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR73_OVER_1YEAR_UPTO_3YEARS(BigDecimal r73_OVER_1YEAR_UPTO_3YEARS) {
		R73_OVER_1YEAR_UPTO_3YEARS = r73_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR73_OVER_3YEARS_UPTO_5YEARS() {
		return R73_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR73_OVER_3YEARS_UPTO_5YEARS(BigDecimal r73_OVER_3YEARS_UPTO_5YEARS) {
		R73_OVER_3YEARS_UPTO_5YEARS = r73_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR73_OVER_5YEARS() {
		return R73_OVER_5YEARS;
	}
	public void setR73_OVER_5YEARS(BigDecimal r73_OVER_5YEARS) {
		R73_OVER_5YEARS = r73_OVER_5YEARS;
	}
	public BigDecimal getR73_TOTAL() {
		return R73_TOTAL;
	}
	public void setR73_TOTAL(BigDecimal r73_TOTAL) {
		R73_TOTAL = r73_TOTAL;
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


public class CPR_STRUCT_LIQ_Archival_Summary_RowMapper
        implements RowMapper<CPR_STRUCT_LIQ_Archival_Summary_Entity> {

    @Override
    public CPR_STRUCT_LIQ_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        CPR_STRUCT_LIQ_Archival_Summary_Entity obj = new CPR_STRUCT_LIQ_Archival_Summary_Entity();
// =========================
// R8
// =========================
obj.setR8_1_DAY(rs.getBigDecimal("R8_1_DAY"));
obj.setR8_2TO7_DAYS(rs.getBigDecimal("R8_2TO7_DAYS"));
obj.setR8_8TO14_DAYS(rs.getBigDecimal("R8_8TO14_DAYS"));
obj.setR8_15TO30_DAYS(rs.getBigDecimal("R8_15TO30_DAYS"));
obj.setR8_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R8_31DAYS_UPTO_2MONTHS"));
obj.setR8_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R8_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR8_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R8_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR8_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R8_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR8_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R8_OVER_1YEAR_UPTO_3YEARS"));
obj.setR8_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R8_OVER_3YEARS_UPTO_5YEARS"));
obj.setR8_OVER_5YEARS(rs.getBigDecimal("R8_OVER_5YEARS"));
obj.setR8_TOTAL(rs.getBigDecimal("R8_TOTAL"));

// =========================
// R9
// =========================
obj.setR9_1_DAY(rs.getBigDecimal("R9_1_DAY"));
obj.setR9_2TO7_DAYS(rs.getBigDecimal("R9_2TO7_DAYS"));
obj.setR9_8TO14_DAYS(rs.getBigDecimal("R9_8TO14_DAYS"));
obj.setR9_15TO30_DAYS(rs.getBigDecimal("R9_15TO30_DAYS"));
obj.setR9_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R9_31DAYS_UPTO_2MONTHS"));
obj.setR9_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R9_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR9_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R9_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR9_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R9_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR9_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R9_OVER_1YEAR_UPTO_3YEARS"));
obj.setR9_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R9_OVER_3YEARS_UPTO_5YEARS"));
obj.setR9_OVER_5YEARS(rs.getBigDecimal("R9_OVER_5YEARS"));
obj.setR9_TOTAL(rs.getBigDecimal("R9_TOTAL"));

// =========================
// R10
// =========================
obj.setR10_1_DAY(rs.getBigDecimal("R10_1_DAY"));
obj.setR10_2TO7_DAYS(rs.getBigDecimal("R10_2TO7_DAYS"));
obj.setR10_8TO14_DAYS(rs.getBigDecimal("R10_8TO14_DAYS"));
obj.setR10_15TO30_DAYS(rs.getBigDecimal("R10_15TO30_DAYS"));
obj.setR10_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R10_31DAYS_UPTO_2MONTHS"));
obj.setR10_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R10_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR10_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R10_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR10_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R10_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR10_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R10_OVER_1YEAR_UPTO_3YEARS"));
obj.setR10_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R10_OVER_3YEARS_UPTO_5YEARS"));
obj.setR10_OVER_5YEARS(rs.getBigDecimal("R10_OVER_5YEARS"));
obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

// =========================
// R11
// =========================
obj.setR11_1_DAY(rs.getBigDecimal("R11_1_DAY"));
obj.setR11_2TO7_DAYS(rs.getBigDecimal("R11_2TO7_DAYS"));
obj.setR11_8TO14_DAYS(rs.getBigDecimal("R11_8TO14_DAYS"));
obj.setR11_15TO30_DAYS(rs.getBigDecimal("R11_15TO30_DAYS"));
obj.setR11_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R11_31DAYS_UPTO_2MONTHS"));
obj.setR11_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R11_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR11_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R11_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR11_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R11_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR11_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R11_OVER_1YEAR_UPTO_3YEARS"));
obj.setR11_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R11_OVER_3YEARS_UPTO_5YEARS"));
obj.setR11_OVER_5YEARS(rs.getBigDecimal("R11_OVER_5YEARS"));
obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

// =========================
// R12
// =========================
obj.setR12_1_DAY(rs.getBigDecimal("R12_1_DAY"));
obj.setR12_2TO7_DAYS(rs.getBigDecimal("R12_2TO7_DAYS"));
obj.setR12_8TO14_DAYS(rs.getBigDecimal("R12_8TO14_DAYS"));
obj.setR12_15TO30_DAYS(rs.getBigDecimal("R12_15TO30_DAYS"));
obj.setR12_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R12_31DAYS_UPTO_2MONTHS"));
obj.setR12_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R12_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR12_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R12_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR12_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R12_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR12_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R12_OVER_1YEAR_UPTO_3YEARS"));
obj.setR12_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R12_OVER_3YEARS_UPTO_5YEARS"));
obj.setR12_OVER_5YEARS(rs.getBigDecimal("R12_OVER_5YEARS"));
obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));


// =========================
// R13
// =========================
obj.setR13_1_DAY(rs.getBigDecimal("R13_1_DAY"));
obj.setR13_2TO7_DAYS(rs.getBigDecimal("R13_2TO7_DAYS"));
obj.setR13_8TO14_DAYS(rs.getBigDecimal("R13_8TO14_DAYS"));
obj.setR13_15TO30_DAYS(rs.getBigDecimal("R13_15TO30_DAYS"));
obj.setR13_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R13_31DAYS_UPTO_2MONTHS"));
obj.setR13_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R13_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR13_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R13_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR13_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R13_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR13_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R13_OVER_1YEAR_UPTO_3YEARS"));
obj.setR13_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R13_OVER_3YEARS_UPTO_5YEARS"));
obj.setR13_OVER_5YEARS(rs.getBigDecimal("R13_OVER_5YEARS"));
obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

// =========================
// R14
// =========================
obj.setR14_1_DAY(rs.getBigDecimal("R14_1_DAY"));
obj.setR14_2TO7_DAYS(rs.getBigDecimal("R14_2TO7_DAYS"));
obj.setR14_8TO14_DAYS(rs.getBigDecimal("R14_8TO14_DAYS"));
obj.setR14_15TO30_DAYS(rs.getBigDecimal("R14_15TO30_DAYS"));
obj.setR14_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R14_31DAYS_UPTO_2MONTHS"));
obj.setR14_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R14_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR14_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R14_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR14_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R14_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR14_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R14_OVER_1YEAR_UPTO_3YEARS"));
obj.setR14_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R14_OVER_3YEARS_UPTO_5YEARS"));
obj.setR14_OVER_5YEARS(rs.getBigDecimal("R14_OVER_5YEARS"));
obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

// =========================
// R15
// =========================
obj.setR15_1_DAY(rs.getBigDecimal("R15_1_DAY"));
obj.setR15_2TO7_DAYS(rs.getBigDecimal("R15_2TO7_DAYS"));
obj.setR15_8TO14_DAYS(rs.getBigDecimal("R15_8TO14_DAYS"));
obj.setR15_15TO30_DAYS(rs.getBigDecimal("R15_15TO30_DAYS"));
obj.setR15_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R15_31DAYS_UPTO_2MONTHS"));
obj.setR15_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R15_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR15_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R15_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR15_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R15_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR15_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R15_OVER_1YEAR_UPTO_3YEARS"));
obj.setR15_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R15_OVER_3YEARS_UPTO_5YEARS"));
obj.setR15_OVER_5YEARS(rs.getBigDecimal("R15_OVER_5YEARS"));
obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));


// =========================
// R16
// =========================
obj.setR16_1_DAY(rs.getBigDecimal("R16_1_DAY"));
obj.setR16_2TO7_DAYS(rs.getBigDecimal("R16_2TO7_DAYS"));
obj.setR16_8TO14_DAYS(rs.getBigDecimal("R16_8TO14_DAYS"));
obj.setR16_15TO30_DAYS(rs.getBigDecimal("R16_15TO30_DAYS"));
obj.setR16_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R16_31DAYS_UPTO_2MONTHS"));
obj.setR16_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R16_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR16_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R16_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR16_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R16_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR16_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R16_OVER_1YEAR_UPTO_3YEARS"));
obj.setR16_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R16_OVER_3YEARS_UPTO_5YEARS"));
obj.setR16_OVER_5YEARS(rs.getBigDecimal("R16_OVER_5YEARS"));
obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

// =========================
// R17
// =========================
obj.setR17_1_DAY(rs.getBigDecimal("R17_1_DAY"));
obj.setR17_2TO7_DAYS(rs.getBigDecimal("R17_2TO7_DAYS"));
obj.setR17_8TO14_DAYS(rs.getBigDecimal("R17_8TO14_DAYS"));
obj.setR17_15TO30_DAYS(rs.getBigDecimal("R17_15TO30_DAYS"));
obj.setR17_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R17_31DAYS_UPTO_2MONTHS"));
obj.setR17_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R17_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR17_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R17_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR17_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R17_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR17_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R17_OVER_1YEAR_UPTO_3YEARS"));
obj.setR17_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R17_OVER_3YEARS_UPTO_5YEARS"));
obj.setR17_OVER_5YEARS(rs.getBigDecimal("R17_OVER_5YEARS"));
obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

// =========================
// R18
// =========================
obj.setR18_1_DAY(rs.getBigDecimal("R18_1_DAY"));
obj.setR18_2TO7_DAYS(rs.getBigDecimal("R18_2TO7_DAYS"));
obj.setR18_8TO14_DAYS(rs.getBigDecimal("R18_8TO14_DAYS"));
obj.setR18_15TO30_DAYS(rs.getBigDecimal("R18_15TO30_DAYS"));
obj.setR18_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R18_31DAYS_UPTO_2MONTHS"));
obj.setR18_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R18_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR18_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R18_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR18_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R18_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR18_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R18_OVER_1YEAR_UPTO_3YEARS"));
obj.setR18_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R18_OVER_3YEARS_UPTO_5YEARS"));
obj.setR18_OVER_5YEARS(rs.getBigDecimal("R18_OVER_5YEARS"));
obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

// =========================
// R19
// =========================
obj.setR19_1_DAY(rs.getBigDecimal("R19_1_DAY"));
obj.setR19_2TO7_DAYS(rs.getBigDecimal("R19_2TO7_DAYS"));
obj.setR19_8TO14_DAYS(rs.getBigDecimal("R19_8TO14_DAYS"));
obj.setR19_15TO30_DAYS(rs.getBigDecimal("R19_15TO30_DAYS"));
obj.setR19_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R19_31DAYS_UPTO_2MONTHS"));
obj.setR19_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R19_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR19_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R19_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR19_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R19_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR19_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R19_OVER_1YEAR_UPTO_3YEARS"));
obj.setR19_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R19_OVER_3YEARS_UPTO_5YEARS"));
obj.setR19_OVER_5YEARS(rs.getBigDecimal("R19_OVER_5YEARS"));
obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

// =========================
// R20
// =========================
obj.setR20_1_DAY(rs.getBigDecimal("R20_1_DAY"));
obj.setR20_2TO7_DAYS(rs.getBigDecimal("R20_2TO7_DAYS"));
obj.setR20_8TO14_DAYS(rs.getBigDecimal("R20_8TO14_DAYS"));
obj.setR20_15TO30_DAYS(rs.getBigDecimal("R20_15TO30_DAYS"));
obj.setR20_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R20_31DAYS_UPTO_2MONTHS"));
obj.setR20_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R20_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR20_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R20_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR20_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R20_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR20_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R20_OVER_1YEAR_UPTO_3YEARS"));
obj.setR20_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R20_OVER_3YEARS_UPTO_5YEARS"));
obj.setR20_OVER_5YEARS(rs.getBigDecimal("R20_OVER_5YEARS"));
obj.setR20_TOTAL(rs.getBigDecimal("R20_TOTAL"));



// =========================
// R21
// =========================
obj.setR21_1_DAY(rs.getBigDecimal("R21_1_DAY"));
obj.setR21_2TO7_DAYS(rs.getBigDecimal("R21_2TO7_DAYS"));
obj.setR21_8TO14_DAYS(rs.getBigDecimal("R21_8TO14_DAYS"));
obj.setR21_15TO30_DAYS(rs.getBigDecimal("R21_15TO30_DAYS"));
obj.setR21_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R21_31DAYS_UPTO_2MONTHS"));
obj.setR21_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R21_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR21_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R21_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR21_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R21_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR21_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R21_OVER_1YEAR_UPTO_3YEARS"));
obj.setR21_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R21_OVER_3YEARS_UPTO_5YEARS"));
obj.setR21_OVER_5YEARS(rs.getBigDecimal("R21_OVER_5YEARS"));
obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));


// =========================
// R22
// =========================
obj.setR22_1_DAY(rs.getBigDecimal("R22_1_DAY"));
obj.setR22_2TO7_DAYS(rs.getBigDecimal("R22_2TO7_DAYS"));
obj.setR22_8TO14_DAYS(rs.getBigDecimal("R22_8TO14_DAYS"));
obj.setR22_15TO30_DAYS(rs.getBigDecimal("R22_15TO30_DAYS"));
obj.setR22_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R22_31DAYS_UPTO_2MONTHS"));
obj.setR22_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R22_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR22_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R22_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR22_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R22_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR22_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R22_OVER_1YEAR_UPTO_3YEARS"));
obj.setR22_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R22_OVER_3YEARS_UPTO_5YEARS"));
obj.setR22_OVER_5YEARS(rs.getBigDecimal("R22_OVER_5YEARS"));
obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

// =========================
// R23
// =========================
obj.setR23_1_DAY(rs.getBigDecimal("R23_1_DAY"));
obj.setR23_2TO7_DAYS(rs.getBigDecimal("R23_2TO7_DAYS"));
obj.setR23_8TO14_DAYS(rs.getBigDecimal("R23_8TO14_DAYS"));
obj.setR23_15TO30_DAYS(rs.getBigDecimal("R23_15TO30_DAYS"));
obj.setR23_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R23_31DAYS_UPTO_2MONTHS"));
obj.setR23_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R23_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR23_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R23_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR23_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R23_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR23_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R23_OVER_1YEAR_UPTO_3YEARS"));
obj.setR23_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R23_OVER_3YEARS_UPTO_5YEARS"));
obj.setR23_OVER_5YEARS(rs.getBigDecimal("R23_OVER_5YEARS"));
obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));


// =========================
// R24
// =========================
obj.setR24_1_DAY(rs.getBigDecimal("R24_1_DAY"));
obj.setR24_2TO7_DAYS(rs.getBigDecimal("R24_2TO7_DAYS"));
obj.setR24_8TO14_DAYS(rs.getBigDecimal("R24_8TO14_DAYS"));
obj.setR24_15TO30_DAYS(rs.getBigDecimal("R24_15TO30_DAYS"));
obj.setR24_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R24_31DAYS_UPTO_2MONTHS"));
obj.setR24_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R24_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR24_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R24_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR24_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R24_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR24_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R24_OVER_1YEAR_UPTO_3YEARS"));
obj.setR24_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R24_OVER_3YEARS_UPTO_5YEARS"));
obj.setR24_OVER_5YEARS(rs.getBigDecimal("R24_OVER_5YEARS"));
obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

// =========================
// R25
// =========================
obj.setR25_1_DAY(rs.getBigDecimal("R25_1_DAY"));
obj.setR25_2TO7_DAYS(rs.getBigDecimal("R25_2TO7_DAYS"));
obj.setR25_8TO14_DAYS(rs.getBigDecimal("R25_8TO14_DAYS"));
obj.setR25_15TO30_DAYS(rs.getBigDecimal("R25_15TO30_DAYS"));
obj.setR25_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R25_31DAYS_UPTO_2MONTHS"));
obj.setR25_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R25_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR25_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R25_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR25_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R25_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR25_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R25_OVER_1YEAR_UPTO_3YEARS"));
obj.setR25_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R25_OVER_3YEARS_UPTO_5YEARS"));
obj.setR25_OVER_5YEARS(rs.getBigDecimal("R25_OVER_5YEARS"));
obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));


// =========================
// R26
// =========================
obj.setR26_1_DAY(rs.getBigDecimal("R26_1_DAY"));
obj.setR26_2TO7_DAYS(rs.getBigDecimal("R26_2TO7_DAYS"));
obj.setR26_8TO14_DAYS(rs.getBigDecimal("R26_8TO14_DAYS"));
obj.setR26_15TO30_DAYS(rs.getBigDecimal("R26_15TO30_DAYS"));
obj.setR26_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R26_31DAYS_UPTO_2MONTHS"));
obj.setR26_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R26_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR26_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R26_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR26_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R26_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR26_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R26_OVER_1YEAR_UPTO_3YEARS"));
obj.setR26_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R26_OVER_3YEARS_UPTO_5YEARS"));
obj.setR26_OVER_5YEARS(rs.getBigDecimal("R26_OVER_5YEARS"));
obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

// =========================
// R27
// =========================
obj.setR27_1_DAY(rs.getBigDecimal("R27_1_DAY"));
obj.setR27_2TO7_DAYS(rs.getBigDecimal("R27_2TO7_DAYS"));
obj.setR27_8TO14_DAYS(rs.getBigDecimal("R27_8TO14_DAYS"));
obj.setR27_15TO30_DAYS(rs.getBigDecimal("R27_15TO30_DAYS"));
obj.setR27_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R27_31DAYS_UPTO_2MONTHS"));
obj.setR27_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R27_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR27_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R27_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR27_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R27_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR27_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R27_OVER_1YEAR_UPTO_3YEARS"));
obj.setR27_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R27_OVER_3YEARS_UPTO_5YEARS"));
obj.setR27_OVER_5YEARS(rs.getBigDecimal("R27_OVER_5YEARS"));
obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));

// =========================
// R28
// =========================
obj.setR28_1_DAY(rs.getBigDecimal("R28_1_DAY"));
obj.setR28_2TO7_DAYS(rs.getBigDecimal("R28_2TO7_DAYS"));
obj.setR28_8TO14_DAYS(rs.getBigDecimal("R28_8TO14_DAYS"));
obj.setR28_15TO30_DAYS(rs.getBigDecimal("R28_15TO30_DAYS"));
obj.setR28_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R28_31DAYS_UPTO_2MONTHS"));
obj.setR28_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R28_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR28_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R28_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR28_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R28_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR28_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R28_OVER_1YEAR_UPTO_3YEARS"));
obj.setR28_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R28_OVER_3YEARS_UPTO_5YEARS"));
obj.setR28_OVER_5YEARS(rs.getBigDecimal("R28_OVER_5YEARS"));
obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));

// =========================
// R29
// =========================
obj.setR29_1_DAY(rs.getBigDecimal("R29_1_DAY"));
obj.setR29_2TO7_DAYS(rs.getBigDecimal("R29_2TO7_DAYS"));
obj.setR29_8TO14_DAYS(rs.getBigDecimal("R29_8TO14_DAYS"));
obj.setR29_15TO30_DAYS(rs.getBigDecimal("R29_15TO30_DAYS"));
obj.setR29_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R29_31DAYS_UPTO_2MONTHS"));
obj.setR29_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R29_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR29_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R29_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR29_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R29_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR29_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R29_OVER_1YEAR_UPTO_3YEARS"));
obj.setR29_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R29_OVER_3YEARS_UPTO_5YEARS"));
obj.setR29_OVER_5YEARS(rs.getBigDecimal("R29_OVER_5YEARS"));
obj.setR29_TOTAL(rs.getBigDecimal("R29_TOTAL"));

// =========================
// R30
// =========================
obj.setR30_1_DAY(rs.getBigDecimal("R30_1_DAY"));
obj.setR30_2TO7_DAYS(rs.getBigDecimal("R30_2TO7_DAYS"));
obj.setR30_8TO14_DAYS(rs.getBigDecimal("R30_8TO14_DAYS"));
obj.setR30_15TO30_DAYS(rs.getBigDecimal("R30_15TO30_DAYS"));
obj.setR30_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R30_31DAYS_UPTO_2MONTHS"));
obj.setR30_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R30_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR30_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R30_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR30_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R30_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR30_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R30_OVER_1YEAR_UPTO_3YEARS"));
obj.setR30_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R30_OVER_3YEARS_UPTO_5YEARS"));
obj.setR30_OVER_5YEARS(rs.getBigDecimal("R30_OVER_5YEARS"));
obj.setR30_TOTAL(rs.getBigDecimal("R30_TOTAL"));

// =========================
// R31
// =========================
obj.setR31_1_DAY(rs.getBigDecimal("R31_1_DAY"));
obj.setR31_2TO7_DAYS(rs.getBigDecimal("R31_2TO7_DAYS"));
obj.setR31_8TO14_DAYS(rs.getBigDecimal("R31_8TO14_DAYS"));
obj.setR31_15TO30_DAYS(rs.getBigDecimal("R31_15TO30_DAYS"));
obj.setR31_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R31_31DAYS_UPTO_2MONTHS"));
obj.setR31_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R31_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR31_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R31_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR31_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R31_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR31_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R31_OVER_1YEAR_UPTO_3YEARS"));
obj.setR31_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R31_OVER_3YEARS_UPTO_5YEARS"));
obj.setR31_OVER_5YEARS(rs.getBigDecimal("R31_OVER_5YEARS"));
obj.setR31_TOTAL(rs.getBigDecimal("R31_TOTAL"));

// =========================
// R32
// =========================
obj.setR32_1_DAY(rs.getBigDecimal("R32_1_DAY"));
obj.setR32_2TO7_DAYS(rs.getBigDecimal("R32_2TO7_DAYS"));
obj.setR32_8TO14_DAYS(rs.getBigDecimal("R32_8TO14_DAYS"));
obj.setR32_15TO30_DAYS(rs.getBigDecimal("R32_15TO30_DAYS"));
obj.setR32_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R32_31DAYS_UPTO_2MONTHS"));
obj.setR32_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R32_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR32_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R32_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR32_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R32_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR32_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R32_OVER_1YEAR_UPTO_3YEARS"));
obj.setR32_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R32_OVER_3YEARS_UPTO_5YEARS"));
obj.setR32_OVER_5YEARS(rs.getBigDecimal("R32_OVER_5YEARS"));
obj.setR32_TOTAL(rs.getBigDecimal("R32_TOTAL"));

// =========================
// R33
// =========================
obj.setR33_1_DAY(rs.getBigDecimal("R33_1_DAY"));
obj.setR33_2TO7_DAYS(rs.getBigDecimal("R33_2TO7_DAYS"));
obj.setR33_8TO14_DAYS(rs.getBigDecimal("R33_8TO14_DAYS"));
obj.setR33_15TO30_DAYS(rs.getBigDecimal("R33_15TO30_DAYS"));
obj.setR33_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R33_31DAYS_UPTO_2MONTHS"));
obj.setR33_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R33_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR33_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R33_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR33_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R33_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR33_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R33_OVER_1YEAR_UPTO_3YEARS"));
obj.setR33_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R33_OVER_3YEARS_UPTO_5YEARS"));
obj.setR33_OVER_5YEARS(rs.getBigDecimal("R33_OVER_5YEARS"));
obj.setR33_TOTAL(rs.getBigDecimal("R33_TOTAL"));

// =========================
// R34
// =========================
obj.setR34_1_DAY(rs.getBigDecimal("R34_1_DAY"));
obj.setR34_2TO7_DAYS(rs.getBigDecimal("R34_2TO7_DAYS"));
obj.setR34_8TO14_DAYS(rs.getBigDecimal("R34_8TO14_DAYS"));
obj.setR34_15TO30_DAYS(rs.getBigDecimal("R34_15TO30_DAYS"));
obj.setR34_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R34_31DAYS_UPTO_2MONTHS"));
obj.setR34_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R34_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR34_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R34_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR34_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R34_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR34_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R34_OVER_1YEAR_UPTO_3YEARS"));
obj.setR34_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R34_OVER_3YEARS_UPTO_5YEARS"));
obj.setR34_OVER_5YEARS(rs.getBigDecimal("R34_OVER_5YEARS"));
obj.setR34_TOTAL(rs.getBigDecimal("R34_TOTAL"));

// =========================
// R35
// =========================
obj.setR35_1_DAY(rs.getBigDecimal("R35_1_DAY"));
obj.setR35_2TO7_DAYS(rs.getBigDecimal("R35_2TO7_DAYS"));
obj.setR35_8TO14_DAYS(rs.getBigDecimal("R35_8TO14_DAYS"));
obj.setR35_15TO30_DAYS(rs.getBigDecimal("R35_15TO30_DAYS"));
obj.setR35_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R35_31DAYS_UPTO_2MONTHS"));
obj.setR35_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R35_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR35_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R35_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR35_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R35_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR35_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R35_OVER_1YEAR_UPTO_3YEARS"));
obj.setR35_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R35_OVER_3YEARS_UPTO_5YEARS"));
obj.setR35_OVER_5YEARS(rs.getBigDecimal("R35_OVER_5YEARS"));
obj.setR35_TOTAL(rs.getBigDecimal("R35_TOTAL"));


// =========================
// R36
// =========================
obj.setR36_1_DAY(rs.getBigDecimal("R36_1_DAY"));
obj.setR36_2TO7_DAYS(rs.getBigDecimal("R36_2TO7_DAYS"));
obj.setR36_8TO14_DAYS(rs.getBigDecimal("R36_8TO14_DAYS"));
obj.setR36_15TO30_DAYS(rs.getBigDecimal("R36_15TO30_DAYS"));
obj.setR36_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R36_31DAYS_UPTO_2MONTHS"));
obj.setR36_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R36_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR36_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R36_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR36_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R36_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR36_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R36_OVER_1YEAR_UPTO_3YEARS"));
obj.setR36_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R36_OVER_3YEARS_UPTO_5YEARS"));
obj.setR36_OVER_5YEARS(rs.getBigDecimal("R36_OVER_5YEARS"));
obj.setR36_TOTAL(rs.getBigDecimal("R36_TOTAL"));

// =========================
// R37
// =========================
obj.setR37_1_DAY(rs.getBigDecimal("R37_1_DAY"));
obj.setR37_2TO7_DAYS(rs.getBigDecimal("R37_2TO7_DAYS"));
obj.setR37_8TO14_DAYS(rs.getBigDecimal("R37_8TO14_DAYS"));
obj.setR37_15TO30_DAYS(rs.getBigDecimal("R37_15TO30_DAYS"));
obj.setR37_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R37_31DAYS_UPTO_2MONTHS"));
obj.setR37_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R37_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR37_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R37_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR37_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R37_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR37_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R37_OVER_1YEAR_UPTO_3YEARS"));
obj.setR37_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R37_OVER_3YEARS_UPTO_5YEARS"));
obj.setR37_OVER_5YEARS(rs.getBigDecimal("R37_OVER_5YEARS"));
obj.setR37_TOTAL(rs.getBigDecimal("R37_TOTAL"));

// =========================
// R38
// =========================
obj.setR38_1_DAY(rs.getBigDecimal("R38_1_DAY"));
obj.setR38_2TO7_DAYS(rs.getBigDecimal("R38_2TO7_DAYS"));
obj.setR38_8TO14_DAYS(rs.getBigDecimal("R38_8TO14_DAYS"));
obj.setR38_15TO30_DAYS(rs.getBigDecimal("R38_15TO30_DAYS"));
obj.setR38_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R38_31DAYS_UPTO_2MONTHS"));
obj.setR38_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R38_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR38_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R38_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR38_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R38_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR38_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R38_OVER_1YEAR_UPTO_3YEARS"));
obj.setR38_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R38_OVER_3YEARS_UPTO_5YEARS"));
obj.setR38_OVER_5YEARS(rs.getBigDecimal("R38_OVER_5YEARS"));
obj.setR38_TOTAL(rs.getBigDecimal("R38_TOTAL"));

// =========================
// R39
// =========================
obj.setR39_1_DAY(rs.getBigDecimal("R39_1_DAY"));
obj.setR39_2TO7_DAYS(rs.getBigDecimal("R39_2TO7_DAYS"));
obj.setR39_8TO14_DAYS(rs.getBigDecimal("R39_8TO14_DAYS"));
obj.setR39_15TO30_DAYS(rs.getBigDecimal("R39_15TO30_DAYS"));
obj.setR39_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R39_31DAYS_UPTO_2MONTHS"));
obj.setR39_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R39_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR39_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R39_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR39_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R39_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR39_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R39_OVER_1YEAR_UPTO_3YEARS"));
obj.setR39_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R39_OVER_3YEARS_UPTO_5YEARS"));
obj.setR39_OVER_5YEARS(rs.getBigDecimal("R39_OVER_5YEARS"));
obj.setR39_TOTAL(rs.getBigDecimal("R39_TOTAL"));

// =========================
// R40
// =========================
obj.setR40_1_DAY(rs.getBigDecimal("R40_1_DAY"));
obj.setR40_2TO7_DAYS(rs.getBigDecimal("R40_2TO7_DAYS"));
obj.setR40_8TO14_DAYS(rs.getBigDecimal("R40_8TO14_DAYS"));
obj.setR40_15TO30_DAYS(rs.getBigDecimal("R40_15TO30_DAYS"));
obj.setR40_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R40_31DAYS_UPTO_2MONTHS"));
obj.setR40_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R40_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR40_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R40_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR40_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R40_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR40_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R40_OVER_1YEAR_UPTO_3YEARS"));
obj.setR40_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R40_OVER_3YEARS_UPTO_5YEARS"));
obj.setR40_OVER_5YEARS(rs.getBigDecimal("R40_OVER_5YEARS"));
obj.setR40_TOTAL(rs.getBigDecimal("R40_TOTAL"));

// =========================
// R41
// =========================
obj.setR41_1_DAY(rs.getBigDecimal("R41_1_DAY"));
obj.setR41_2TO7_DAYS(rs.getBigDecimal("R41_2TO7_DAYS"));
obj.setR41_8TO14_DAYS(rs.getBigDecimal("R41_8TO14_DAYS"));
obj.setR41_15TO30_DAYS(rs.getBigDecimal("R41_15TO30_DAYS"));
obj.setR41_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R41_31DAYS_UPTO_2MONTHS"));
obj.setR41_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R41_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR41_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R41_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR41_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R41_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR41_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R41_OVER_1YEAR_UPTO_3YEARS"));
obj.setR41_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R41_OVER_3YEARS_UPTO_5YEARS"));
obj.setR41_OVER_5YEARS(rs.getBigDecimal("R41_OVER_5YEARS"));
obj.setR41_TOTAL(rs.getBigDecimal("R41_TOTAL"));

// =========================
// R42
// =========================
obj.setR42_1_DAY(rs.getBigDecimal("R42_1_DAY"));
obj.setR42_2TO7_DAYS(rs.getBigDecimal("R42_2TO7_DAYS"));
obj.setR42_8TO14_DAYS(rs.getBigDecimal("R42_8TO14_DAYS"));
obj.setR42_15TO30_DAYS(rs.getBigDecimal("R42_15TO30_DAYS"));
obj.setR42_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R42_31DAYS_UPTO_2MONTHS"));
obj.setR42_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R42_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR42_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R42_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR42_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R42_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR42_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R42_OVER_1YEAR_UPTO_3YEARS"));
obj.setR42_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R42_OVER_3YEARS_UPTO_5YEARS"));
obj.setR42_OVER_5YEARS(rs.getBigDecimal("R42_OVER_5YEARS"));
obj.setR42_TOTAL(rs.getBigDecimal("R42_TOTAL"));

// =========================
// R43
// =========================
obj.setR43_1_DAY(rs.getBigDecimal("R43_1_DAY"));
obj.setR43_2TO7_DAYS(rs.getBigDecimal("R43_2TO7_DAYS"));
obj.setR43_8TO14_DAYS(rs.getBigDecimal("R43_8TO14_DAYS"));
obj.setR43_15TO30_DAYS(rs.getBigDecimal("R43_15TO30_DAYS"));
obj.setR43_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R43_31DAYS_UPTO_2MONTHS"));
obj.setR43_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R43_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR43_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R43_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR43_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R43_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR43_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R43_OVER_1YEAR_UPTO_3YEARS"));
obj.setR43_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R43_OVER_3YEARS_UPTO_5YEARS"));
obj.setR43_OVER_5YEARS(rs.getBigDecimal("R43_OVER_5YEARS"));
obj.setR43_TOTAL(rs.getBigDecimal("R43_TOTAL"));

// =========================
// R44
// =========================
obj.setR44_1_DAY(rs.getBigDecimal("R44_1_DAY"));
obj.setR44_2TO7_DAYS(rs.getBigDecimal("R44_2TO7_DAYS"));
obj.setR44_8TO14_DAYS(rs.getBigDecimal("R44_8TO14_DAYS"));
obj.setR44_15TO30_DAYS(rs.getBigDecimal("R44_15TO30_DAYS"));
obj.setR44_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R44_31DAYS_UPTO_2MONTHS"));
obj.setR44_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R44_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR44_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R44_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR44_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R44_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR44_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R44_OVER_1YEAR_UPTO_3YEARS"));
obj.setR44_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R44_OVER_3YEARS_UPTO_5YEARS"));
obj.setR44_OVER_5YEARS(rs.getBigDecimal("R44_OVER_5YEARS"));
obj.setR44_TOTAL(rs.getBigDecimal("R44_TOTAL"));

// =========================
// R45
// =========================
obj.setR45_1_DAY(rs.getBigDecimal("R45_1_DAY"));
obj.setR45_2TO7_DAYS(rs.getBigDecimal("R45_2TO7_DAYS"));
obj.setR45_8TO14_DAYS(rs.getBigDecimal("R45_8TO14_DAYS"));
obj.setR45_15TO30_DAYS(rs.getBigDecimal("R45_15TO30_DAYS"));
obj.setR45_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R45_31DAYS_UPTO_2MONTHS"));
obj.setR45_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R45_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR45_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R45_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR45_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R45_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR45_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R45_OVER_1YEAR_UPTO_3YEARS"));
obj.setR45_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R45_OVER_3YEARS_UPTO_5YEARS"));
obj.setR45_OVER_5YEARS(rs.getBigDecimal("R45_OVER_5YEARS"));
obj.setR45_TOTAL(rs.getBigDecimal("R45_TOTAL"));

// =========================
// R46
// =========================
obj.setR46_1_DAY(rs.getBigDecimal("R46_1_DAY"));
obj.setR46_2TO7_DAYS(rs.getBigDecimal("R46_2TO7_DAYS"));
obj.setR46_8TO14_DAYS(rs.getBigDecimal("R46_8TO14_DAYS"));
obj.setR46_15TO30_DAYS(rs.getBigDecimal("R46_15TO30_DAYS"));
obj.setR46_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R46_31DAYS_UPTO_2MONTHS"));
obj.setR46_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R46_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR46_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R46_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR46_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R46_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR46_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R46_OVER_1YEAR_UPTO_3YEARS"));
obj.setR46_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R46_OVER_3YEARS_UPTO_5YEARS"));
obj.setR46_OVER_5YEARS(rs.getBigDecimal("R46_OVER_5YEARS"));
obj.setR46_TOTAL(rs.getBigDecimal("R46_TOTAL"));

// =========================
// R47
// =========================
obj.setR47_1_DAY(rs.getBigDecimal("R47_1_DAY"));
obj.setR47_2TO7_DAYS(rs.getBigDecimal("R47_2TO7_DAYS"));
obj.setR47_8TO14_DAYS(rs.getBigDecimal("R47_8TO14_DAYS"));
obj.setR47_15TO30_DAYS(rs.getBigDecimal("R47_15TO30_DAYS"));
obj.setR47_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R47_31DAYS_UPTO_2MONTHS"));
obj.setR47_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R47_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR47_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R47_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR47_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R47_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR47_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R47_OVER_1YEAR_UPTO_3YEARS"));
obj.setR47_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R47_OVER_3YEARS_UPTO_5YEARS"));
obj.setR47_OVER_5YEARS(rs.getBigDecimal("R47_OVER_5YEARS"));
obj.setR47_TOTAL(rs.getBigDecimal("R47_TOTAL"));

// =========================
// R48
// =========================
obj.setR48_1_DAY(rs.getBigDecimal("R48_1_DAY"));
obj.setR48_2TO7_DAYS(rs.getBigDecimal("R48_2TO7_DAYS"));
obj.setR48_8TO14_DAYS(rs.getBigDecimal("R48_8TO14_DAYS"));
obj.setR48_15TO30_DAYS(rs.getBigDecimal("R48_15TO30_DAYS"));
obj.setR48_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R48_31DAYS_UPTO_2MONTHS"));
obj.setR48_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R48_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR48_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R48_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR48_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R48_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR48_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R48_OVER_1YEAR_UPTO_3YEARS"));
obj.setR48_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R48_OVER_3YEARS_UPTO_5YEARS"));
obj.setR48_OVER_5YEARS(rs.getBigDecimal("R48_OVER_5YEARS"));
obj.setR48_TOTAL(rs.getBigDecimal("R48_TOTAL"));

// =========================
// R49
// =========================
obj.setR49_1_DAY(rs.getBigDecimal("R49_1_DAY"));
obj.setR49_2TO7_DAYS(rs.getBigDecimal("R49_2TO7_DAYS"));
obj.setR49_8TO14_DAYS(rs.getBigDecimal("R49_8TO14_DAYS"));
obj.setR49_15TO30_DAYS(rs.getBigDecimal("R49_15TO30_DAYS"));
obj.setR49_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R49_31DAYS_UPTO_2MONTHS"));
obj.setR49_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R49_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR49_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R49_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR49_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R49_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR49_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R49_OVER_1YEAR_UPTO_3YEARS"));
obj.setR49_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R49_OVER_3YEARS_UPTO_5YEARS"));
obj.setR49_OVER_5YEARS(rs.getBigDecimal("R49_OVER_5YEARS"));
obj.setR49_TOTAL(rs.getBigDecimal("R49_TOTAL"));

// =========================
// R50
// =========================
obj.setR50_1_DAY(rs.getBigDecimal("R50_1_DAY"));
obj.setR50_2TO7_DAYS(rs.getBigDecimal("R50_2TO7_DAYS"));
obj.setR50_8TO14_DAYS(rs.getBigDecimal("R50_8TO14_DAYS"));
obj.setR50_15TO30_DAYS(rs.getBigDecimal("R50_15TO30_DAYS"));
obj.setR50_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R50_31DAYS_UPTO_2MONTHS"));
obj.setR50_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R50_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR50_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R50_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR50_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R50_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR50_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R50_OVER_1YEAR_UPTO_3YEARS"));
obj.setR50_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R50_OVER_3YEARS_UPTO_5YEARS"));
obj.setR50_OVER_5YEARS(rs.getBigDecimal("R50_OVER_5YEARS"));
obj.setR50_TOTAL(rs.getBigDecimal("R50_TOTAL"));


// =========================
// R51
// =========================
obj.setR51_1_DAY(rs.getBigDecimal("R51_1_DAY"));
obj.setR51_2TO7_DAYS(rs.getBigDecimal("R51_2TO7_DAYS"));
obj.setR51_8TO14_DAYS(rs.getBigDecimal("R51_8TO14_DAYS"));
obj.setR51_15TO30_DAYS(rs.getBigDecimal("R51_15TO30_DAYS"));
obj.setR51_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R51_31DAYS_UPTO_2MONTHS"));
obj.setR51_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R51_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR51_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R51_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR51_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R51_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR51_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R51_OVER_1YEAR_UPTO_3YEARS"));
obj.setR51_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R51_OVER_3YEARS_UPTO_5YEARS"));
obj.setR51_OVER_5YEARS(rs.getBigDecimal("R51_OVER_5YEARS"));
obj.setR51_TOTAL(rs.getBigDecimal("R51_TOTAL"));

// =========================
// R52
// =========================
obj.setR52_1_DAY(rs.getBigDecimal("R52_1_DAY"));
obj.setR52_2TO7_DAYS(rs.getBigDecimal("R52_2TO7_DAYS"));
obj.setR52_8TO14_DAYS(rs.getBigDecimal("R52_8TO14_DAYS"));
obj.setR52_15TO30_DAYS(rs.getBigDecimal("R52_15TO30_DAYS"));
obj.setR52_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R52_31DAYS_UPTO_2MONTHS"));
obj.setR52_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R52_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR52_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R52_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR52_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R52_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR52_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R52_OVER_1YEAR_UPTO_3YEARS"));
obj.setR52_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R52_OVER_3YEARS_UPTO_5YEARS"));
obj.setR52_OVER_5YEARS(rs.getBigDecimal("R52_OVER_5YEARS"));
obj.setR52_TOTAL(rs.getBigDecimal("R52_TOTAL"));

// =========================
// R53
// =========================
obj.setR53_1_DAY(rs.getBigDecimal("R53_1_DAY"));
obj.setR53_2TO7_DAYS(rs.getBigDecimal("R53_2TO7_DAYS"));
obj.setR53_8TO14_DAYS(rs.getBigDecimal("R53_8TO14_DAYS"));
obj.setR53_15TO30_DAYS(rs.getBigDecimal("R53_15TO30_DAYS"));
obj.setR53_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R53_31DAYS_UPTO_2MONTHS"));
obj.setR53_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R53_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR53_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R53_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR53_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R53_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR53_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R53_OVER_1YEAR_UPTO_3YEARS"));
obj.setR53_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R53_OVER_3YEARS_UPTO_5YEARS"));
obj.setR53_OVER_5YEARS(rs.getBigDecimal("R53_OVER_5YEARS"));
obj.setR53_TOTAL(rs.getBigDecimal("R53_TOTAL"));

// =========================
// R54
// =========================
obj.setR54_1_DAY(rs.getBigDecimal("R54_1_DAY"));
obj.setR54_2TO7_DAYS(rs.getBigDecimal("R54_2TO7_DAYS"));
obj.setR54_8TO14_DAYS(rs.getBigDecimal("R54_8TO14_DAYS"));
obj.setR54_15TO30_DAYS(rs.getBigDecimal("R54_15TO30_DAYS"));
obj.setR54_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R54_31DAYS_UPTO_2MONTHS"));
obj.setR54_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R54_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR54_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R54_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR54_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R54_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR54_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R54_OVER_1YEAR_UPTO_3YEARS"));
obj.setR54_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R54_OVER_3YEARS_UPTO_5YEARS"));
obj.setR54_OVER_5YEARS(rs.getBigDecimal("R54_OVER_5YEARS"));
obj.setR54_TOTAL(rs.getBigDecimal("R54_TOTAL"));

// =========================
// R55
// =========================
obj.setR55_1_DAY(rs.getBigDecimal("R55_1_DAY"));
obj.setR55_2TO7_DAYS(rs.getBigDecimal("R55_2TO7_DAYS"));
obj.setR55_8TO14_DAYS(rs.getBigDecimal("R55_8TO14_DAYS"));
obj.setR55_15TO30_DAYS(rs.getBigDecimal("R55_15TO30_DAYS"));
obj.setR55_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R55_31DAYS_UPTO_2MONTHS"));
obj.setR55_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R55_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR55_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R55_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR55_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R55_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR55_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R55_OVER_1YEAR_UPTO_3YEARS"));
obj.setR55_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R55_OVER_3YEARS_UPTO_5YEARS"));
obj.setR55_OVER_5YEARS(rs.getBigDecimal("R55_OVER_5YEARS"));
obj.setR55_TOTAL(rs.getBigDecimal("R55_TOTAL"));


// =========================
// R56
// =========================
obj.setR56_1_DAY(rs.getBigDecimal("R56_1_DAY"));
obj.setR56_2TO7_DAYS(rs.getBigDecimal("R56_2TO7_DAYS"));
obj.setR56_8TO14_DAYS(rs.getBigDecimal("R56_8TO14_DAYS"));
obj.setR56_15TO30_DAYS(rs.getBigDecimal("R56_15TO30_DAYS"));
obj.setR56_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R56_31DAYS_UPTO_2MONTHS"));
obj.setR56_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R56_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR56_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R56_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR56_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R56_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR56_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R56_OVER_1YEAR_UPTO_3YEARS"));
obj.setR56_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R56_OVER_3YEARS_UPTO_5YEARS"));
obj.setR56_OVER_5YEARS(rs.getBigDecimal("R56_OVER_5YEARS"));
obj.setR56_TOTAL(rs.getBigDecimal("R56_TOTAL"));

// =========================
// R57
// =========================
obj.setR57_1_DAY(rs.getBigDecimal("R57_1_DAY"));
obj.setR57_2TO7_DAYS(rs.getBigDecimal("R57_2TO7_DAYS"));
obj.setR57_8TO14_DAYS(rs.getBigDecimal("R57_8TO14_DAYS"));
obj.setR57_15TO30_DAYS(rs.getBigDecimal("R57_15TO30_DAYS"));
obj.setR57_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R57_31DAYS_UPTO_2MONTHS"));
obj.setR57_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R57_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR57_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R57_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR57_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R57_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR57_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R57_OVER_1YEAR_UPTO_3YEARS"));
obj.setR57_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R57_OVER_3YEARS_UPTO_5YEARS"));
obj.setR57_OVER_5YEARS(rs.getBigDecimal("R57_OVER_5YEARS"));
obj.setR57_TOTAL(rs.getBigDecimal("R57_TOTAL"));

// =========================
// R58
// =========================
obj.setR58_1_DAY(rs.getBigDecimal("R58_1_DAY"));
obj.setR58_2TO7_DAYS(rs.getBigDecimal("R58_2TO7_DAYS"));
obj.setR58_8TO14_DAYS(rs.getBigDecimal("R58_8TO14_DAYS"));
obj.setR58_15TO30_DAYS(rs.getBigDecimal("R58_15TO30_DAYS"));
obj.setR58_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R58_31DAYS_UPTO_2MONTHS"));
obj.setR58_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R58_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR58_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R58_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR58_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R58_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR58_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R58_OVER_1YEAR_UPTO_3YEARS"));
obj.setR58_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R58_OVER_3YEARS_UPTO_5YEARS"));
obj.setR58_OVER_5YEARS(rs.getBigDecimal("R58_OVER_5YEARS"));
obj.setR58_TOTAL(rs.getBigDecimal("R58_TOTAL"));

// =========================
// R59
// =========================
obj.setR59_1_DAY(rs.getBigDecimal("R59_1_DAY"));
obj.setR59_2TO7_DAYS(rs.getBigDecimal("R59_2TO7_DAYS"));
obj.setR59_8TO14_DAYS(rs.getBigDecimal("R59_8TO14_DAYS"));
obj.setR59_15TO30_DAYS(rs.getBigDecimal("R59_15TO30_DAYS"));
obj.setR59_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R59_31DAYS_UPTO_2MONTHS"));
obj.setR59_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R59_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR59_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R59_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR59_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R59_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR59_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R59_OVER_1YEAR_UPTO_3YEARS"));
obj.setR59_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R59_OVER_3YEARS_UPTO_5YEARS"));
obj.setR59_OVER_5YEARS(rs.getBigDecimal("R59_OVER_5YEARS"));
obj.setR59_TOTAL(rs.getBigDecimal("R59_TOTAL"));

// =========================
// R60
// =========================
obj.setR60_1_DAY(rs.getBigDecimal("R60_1_DAY"));
obj.setR60_2TO7_DAYS(rs.getBigDecimal("R60_2TO7_DAYS"));
obj.setR60_8TO14_DAYS(rs.getBigDecimal("R60_8TO14_DAYS"));
obj.setR60_15TO30_DAYS(rs.getBigDecimal("R60_15TO30_DAYS"));
obj.setR60_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R60_31DAYS_UPTO_2MONTHS"));
obj.setR60_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R60_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR60_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R60_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR60_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R60_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR60_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R60_OVER_1YEAR_UPTO_3YEARS"));
obj.setR60_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R60_OVER_3YEARS_UPTO_5YEARS"));
obj.setR60_OVER_5YEARS(rs.getBigDecimal("R60_OVER_5YEARS"));
obj.setR60_TOTAL(rs.getBigDecimal("R60_TOTAL"));


// =========================
// R61
// =========================
obj.setR61_1_DAY(rs.getBigDecimal("R61_1_DAY"));
obj.setR61_2TO7_DAYS(rs.getBigDecimal("R61_2TO7_DAYS"));
obj.setR61_8TO14_DAYS(rs.getBigDecimal("R61_8TO14_DAYS"));
obj.setR61_15TO30_DAYS(rs.getBigDecimal("R61_15TO30_DAYS"));
obj.setR61_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R61_31DAYS_UPTO_2MONTHS"));
obj.setR61_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R61_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR61_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R61_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR61_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R61_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR61_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R61_OVER_1YEAR_UPTO_3YEARS"));
obj.setR61_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R61_OVER_3YEARS_UPTO_5YEARS"));
obj.setR61_OVER_5YEARS(rs.getBigDecimal("R61_OVER_5YEARS"));
obj.setR61_TOTAL(rs.getBigDecimal("R61_TOTAL"));

// =========================
// R62
// =========================
obj.setR62_1_DAY(rs.getBigDecimal("R62_1_DAY"));
obj.setR62_2TO7_DAYS(rs.getBigDecimal("R62_2TO7_DAYS"));
obj.setR62_8TO14_DAYS(rs.getBigDecimal("R62_8TO14_DAYS"));
obj.setR62_15TO30_DAYS(rs.getBigDecimal("R62_15TO30_DAYS"));
obj.setR62_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R62_31DAYS_UPTO_2MONTHS"));
obj.setR62_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R62_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR62_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R62_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR62_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R62_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR62_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R62_OVER_1YEAR_UPTO_3YEARS"));
obj.setR62_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R62_OVER_3YEARS_UPTO_5YEARS"));
obj.setR62_OVER_5YEARS(rs.getBigDecimal("R62_OVER_5YEARS"));
obj.setR62_TOTAL(rs.getBigDecimal("R62_TOTAL"));

// =========================
// R63
// =========================
obj.setR63_1_DAY(rs.getBigDecimal("R63_1_DAY"));
obj.setR63_2TO7_DAYS(rs.getBigDecimal("R63_2TO7_DAYS"));
obj.setR63_8TO14_DAYS(rs.getBigDecimal("R63_8TO14_DAYS"));
obj.setR63_15TO30_DAYS(rs.getBigDecimal("R63_15TO30_DAYS"));
obj.setR63_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R63_31DAYS_UPTO_2MONTHS"));
obj.setR63_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R63_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR63_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R63_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR63_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R63_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR63_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R63_OVER_1YEAR_UPTO_3YEARS"));
obj.setR63_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R63_OVER_3YEARS_UPTO_5YEARS"));
obj.setR63_OVER_5YEARS(rs.getBigDecimal("R63_OVER_5YEARS"));
obj.setR63_TOTAL(rs.getBigDecimal("R63_TOTAL"));

// =========================
// R64
// =========================
obj.setR64_1_DAY(rs.getBigDecimal("R64_1_DAY"));
obj.setR64_2TO7_DAYS(rs.getBigDecimal("R64_2TO7_DAYS"));
obj.setR64_8TO14_DAYS(rs.getBigDecimal("R64_8TO14_DAYS"));
obj.setR64_15TO30_DAYS(rs.getBigDecimal("R64_15TO30_DAYS"));
obj.setR64_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R64_31DAYS_UPTO_2MONTHS"));
obj.setR64_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R64_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR64_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R64_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR64_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R64_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR64_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R64_OVER_1YEAR_UPTO_3YEARS"));
obj.setR64_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R64_OVER_3YEARS_UPTO_5YEARS"));
obj.setR64_OVER_5YEARS(rs.getBigDecimal("R64_OVER_5YEARS"));
obj.setR64_TOTAL(rs.getBigDecimal("R64_TOTAL"));

// =========================
// R65
// =========================
obj.setR65_1_DAY(rs.getBigDecimal("R65_1_DAY"));
obj.setR65_2TO7_DAYS(rs.getBigDecimal("R65_2TO7_DAYS"));
obj.setR65_8TO14_DAYS(rs.getBigDecimal("R65_8TO14_DAYS"));
obj.setR65_15TO30_DAYS(rs.getBigDecimal("R65_15TO30_DAYS"));
obj.setR65_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R65_31DAYS_UPTO_2MONTHS"));
obj.setR65_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R65_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR65_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R65_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR65_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R65_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR65_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R65_OVER_1YEAR_UPTO_3YEARS"));
obj.setR65_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R65_OVER_3YEARS_UPTO_5YEARS"));
obj.setR65_OVER_5YEARS(rs.getBigDecimal("R65_OVER_5YEARS"));
obj.setR65_TOTAL(rs.getBigDecimal("R65_TOTAL"));


// =========================
// R66
// =========================
obj.setR66_1_DAY(rs.getBigDecimal("R66_1_DAY"));
obj.setR66_2TO7_DAYS(rs.getBigDecimal("R66_2TO7_DAYS"));
obj.setR66_8TO14_DAYS(rs.getBigDecimal("R66_8TO14_DAYS"));
obj.setR66_15TO30_DAYS(rs.getBigDecimal("R66_15TO30_DAYS"));
obj.setR66_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R66_31DAYS_UPTO_2MONTHS"));
obj.setR66_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R66_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR66_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R66_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR66_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R66_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR66_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R66_OVER_1YEAR_UPTO_3YEARS"));
obj.setR66_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R66_OVER_3YEARS_UPTO_5YEARS"));
obj.setR66_OVER_5YEARS(rs.getBigDecimal("R66_OVER_5YEARS"));
obj.setR66_TOTAL(rs.getBigDecimal("R66_TOTAL"));

// =========================
// R67
// =========================
obj.setR67_1_DAY(rs.getBigDecimal("R67_1_DAY"));
obj.setR67_2TO7_DAYS(rs.getBigDecimal("R67_2TO7_DAYS"));
obj.setR67_8TO14_DAYS(rs.getBigDecimal("R67_8TO14_DAYS"));
obj.setR67_15TO30_DAYS(rs.getBigDecimal("R67_15TO30_DAYS"));
obj.setR67_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R67_31DAYS_UPTO_2MONTHS"));
obj.setR67_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R67_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR67_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R67_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR67_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R67_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR67_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R67_OVER_1YEAR_UPTO_3YEARS"));
obj.setR67_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R67_OVER_3YEARS_UPTO_5YEARS"));
obj.setR67_OVER_5YEARS(rs.getBigDecimal("R67_OVER_5YEARS"));
obj.setR67_TOTAL(rs.getBigDecimal("R67_TOTAL"));

// =========================
// R68
// =========================
obj.setR68_1_DAY(rs.getBigDecimal("R68_1_DAY"));
obj.setR68_2TO7_DAYS(rs.getBigDecimal("R68_2TO7_DAYS"));
obj.setR68_8TO14_DAYS(rs.getBigDecimal("R68_8TO14_DAYS"));
obj.setR68_15TO30_DAYS(rs.getBigDecimal("R68_15TO30_DAYS"));
obj.setR68_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R68_31DAYS_UPTO_2MONTHS"));
obj.setR68_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R68_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR68_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R68_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR68_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R68_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR68_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R68_OVER_1YEAR_UPTO_3YEARS"));
obj.setR68_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R68_OVER_3YEARS_UPTO_5YEARS"));
obj.setR68_OVER_5YEARS(rs.getBigDecimal("R68_OVER_5YEARS"));
obj.setR68_TOTAL(rs.getBigDecimal("R68_TOTAL"));

// =========================
// R69
// =========================
obj.setR69_1_DAY(rs.getBigDecimal("R69_1_DAY"));
obj.setR69_2TO7_DAYS(rs.getBigDecimal("R69_2TO7_DAYS"));
obj.setR69_8TO14_DAYS(rs.getBigDecimal("R69_8TO14_DAYS"));
obj.setR69_15TO30_DAYS(rs.getBigDecimal("R69_15TO30_DAYS"));
obj.setR69_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R69_31DAYS_UPTO_2MONTHS"));
obj.setR69_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R69_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR69_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R69_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR69_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R69_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR69_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R69_OVER_1YEAR_UPTO_3YEARS"));
obj.setR69_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R69_OVER_3YEARS_UPTO_5YEARS"));
obj.setR69_OVER_5YEARS(rs.getBigDecimal("R69_OVER_5YEARS"));
obj.setR69_TOTAL(rs.getBigDecimal("R69_TOTAL"));

// =========================
// R70
// =========================
obj.setR70_1_DAY(rs.getBigDecimal("R70_1_DAY"));
obj.setR70_2TO7_DAYS(rs.getBigDecimal("R70_2TO7_DAYS"));
obj.setR70_8TO14_DAYS(rs.getBigDecimal("R70_8TO14_DAYS"));
obj.setR70_15TO30_DAYS(rs.getBigDecimal("R70_15TO30_DAYS"));
obj.setR70_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R70_31DAYS_UPTO_2MONTHS"));
obj.setR70_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R70_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR70_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R70_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR70_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R70_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR70_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R70_OVER_1YEAR_UPTO_3YEARS"));
obj.setR70_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R70_OVER_3YEARS_UPTO_5YEARS"));
obj.setR70_OVER_5YEARS(rs.getBigDecimal("R70_OVER_5YEARS"));
obj.setR70_TOTAL(rs.getBigDecimal("R70_TOTAL"));

// =========================
// R71
// =========================
obj.setR71_1_DAY(rs.getBigDecimal("R71_1_DAY"));
obj.setR71_2TO7_DAYS(rs.getBigDecimal("R71_2TO7_DAYS"));
obj.setR71_8TO14_DAYS(rs.getBigDecimal("R71_8TO14_DAYS"));
obj.setR71_15TO30_DAYS(rs.getBigDecimal("R71_15TO30_DAYS"));
obj.setR71_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R71_31DAYS_UPTO_2MONTHS"));
obj.setR71_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R71_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR71_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R71_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR71_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R71_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR71_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R71_OVER_1YEAR_UPTO_3YEARS"));
obj.setR71_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R71_OVER_3YEARS_UPTO_5YEARS"));
obj.setR71_OVER_5YEARS(rs.getBigDecimal("R71_OVER_5YEARS"));
obj.setR71_TOTAL(rs.getBigDecimal("R71_TOTAL"));

// =========================
// R72
// =========================
obj.setR72_1_DAY(rs.getBigDecimal("R72_1_DAY"));
obj.setR72_2TO7_DAYS(rs.getBigDecimal("R72_2TO7_DAYS"));
obj.setR72_8TO14_DAYS(rs.getBigDecimal("R72_8TO14_DAYS"));
obj.setR72_15TO30_DAYS(rs.getBigDecimal("R72_15TO30_DAYS"));
obj.setR72_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R72_31DAYS_UPTO_2MONTHS"));
obj.setR72_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R72_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR72_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R72_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR72_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R72_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR72_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R72_OVER_1YEAR_UPTO_3YEARS"));
obj.setR72_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R72_OVER_3YEARS_UPTO_5YEARS"));
obj.setR72_OVER_5YEARS(rs.getBigDecimal("R72_OVER_5YEARS"));
obj.setR72_TOTAL(rs.getBigDecimal("R72_TOTAL"));

// =========================
// R73
// =========================
obj.setR73_1_DAY(rs.getBigDecimal("R73_1_DAY"));
obj.setR73_2TO7_DAYS(rs.getBigDecimal("R73_2TO7_DAYS"));
obj.setR73_8TO14_DAYS(rs.getBigDecimal("R73_8TO14_DAYS"));
obj.setR73_15TO30_DAYS(rs.getBigDecimal("R73_15TO30_DAYS"));
obj.setR73_31DAYS_UPTO_2MONTHS(rs.getBigDecimal("R73_31DAYS_UPTO_2MONTHS"));
obj.setR73_MORETHAN_2MONTHS_UPTO_3MONHTS(rs.getBigDecimal("R73_MORETHAN_2MONTHS_UPTO_3MONHTS"));
obj.setR73_OVER_3MONTHS_UPTO_6MONTHS(rs.getBigDecimal("R73_OVER_3MONTHS_UPTO_6MONTHS"));
obj.setR73_OVER_6MONTHS_UPTO_1YEAR(rs.getBigDecimal("R73_OVER_6MONTHS_UPTO_1YEAR"));
obj.setR73_OVER_1YEAR_UPTO_3YEARS(rs.getBigDecimal("R73_OVER_1YEAR_UPTO_3YEARS"));
obj.setR73_OVER_3YEARS_UPTO_5YEARS(rs.getBigDecimal("R73_OVER_3YEARS_UPTO_5YEARS"));
obj.setR73_OVER_5YEARS(rs.getBigDecimal("R73_OVER_5YEARS"));
obj.setR73_TOTAL(rs.getBigDecimal("R73_TOTAL"));


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


public class CPR_STRUCT_LIQ_Archival_Summary_Entity {
	
 private BigDecimal R8_1_DAY;
private BigDecimal R8_2TO7_DAYS;
private BigDecimal R8_8TO14_DAYS;
private BigDecimal R8_15TO30_DAYS;
private BigDecimal R8_31DAYS_UPTO_2MONTHS;
private BigDecimal R8_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R8_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R8_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R8_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R8_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R8_OVER_5YEARS;
private BigDecimal R8_TOTAL;
private BigDecimal R9_1_DAY;
private BigDecimal R9_2TO7_DAYS;
private BigDecimal R9_8TO14_DAYS;
private BigDecimal R9_15TO30_DAYS;
private BigDecimal R9_31DAYS_UPTO_2MONTHS;
private BigDecimal R9_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R9_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R9_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R9_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R9_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R9_OVER_5YEARS;
private BigDecimal R9_TOTAL;
private BigDecimal R10_1_DAY;
private BigDecimal R10_2TO7_DAYS;
private BigDecimal R10_8TO14_DAYS;
private BigDecimal R10_15TO30_DAYS;
private BigDecimal R10_31DAYS_UPTO_2MONTHS;
private BigDecimal R10_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R10_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R10_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R10_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R10_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R10_OVER_5YEARS;
private BigDecimal R10_TOTAL;
private BigDecimal R11_1_DAY;
private BigDecimal R11_2TO7_DAYS;
private BigDecimal R11_8TO14_DAYS;
private BigDecimal R11_15TO30_DAYS;
private BigDecimal R11_31DAYS_UPTO_2MONTHS;
private BigDecimal R11_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R11_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R11_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R11_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R11_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R11_OVER_5YEARS;
private BigDecimal R11_TOTAL;
private BigDecimal R12_1_DAY;
private BigDecimal R12_2TO7_DAYS;
private BigDecimal R12_8TO14_DAYS;
private BigDecimal R12_15TO30_DAYS;
private BigDecimal R12_31DAYS_UPTO_2MONTHS;
private BigDecimal R12_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R12_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R12_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R12_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R12_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R12_OVER_5YEARS;
private BigDecimal R12_TOTAL;
private BigDecimal R13_1_DAY;
private BigDecimal R13_2TO7_DAYS;
private BigDecimal R13_8TO14_DAYS;
private BigDecimal R13_15TO30_DAYS;
private BigDecimal R13_31DAYS_UPTO_2MONTHS;
private BigDecimal R13_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R13_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R13_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R13_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R13_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R13_OVER_5YEARS;
private BigDecimal R13_TOTAL;
private BigDecimal R14_1_DAY;
private BigDecimal R14_2TO7_DAYS;
private BigDecimal R14_8TO14_DAYS;
private BigDecimal R14_15TO30_DAYS;
private BigDecimal R14_31DAYS_UPTO_2MONTHS;
private BigDecimal R14_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R14_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R14_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R14_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R14_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R14_OVER_5YEARS;
private BigDecimal R14_TOTAL;
private BigDecimal R15_1_DAY;
private BigDecimal R15_2TO7_DAYS;
private BigDecimal R15_8TO14_DAYS;
private BigDecimal R15_15TO30_DAYS;
private BigDecimal R15_31DAYS_UPTO_2MONTHS;
private BigDecimal R15_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R15_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R15_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R15_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R15_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R15_OVER_5YEARS;
private BigDecimal R15_TOTAL;
private BigDecimal R16_1_DAY;
private BigDecimal R16_2TO7_DAYS;
private BigDecimal R16_8TO14_DAYS;
private BigDecimal R16_15TO30_DAYS;
private BigDecimal R16_31DAYS_UPTO_2MONTHS;
private BigDecimal R16_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R16_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R16_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R16_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R16_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R16_OVER_5YEARS;
private BigDecimal R16_TOTAL;
private BigDecimal R17_1_DAY;
private BigDecimal R17_2TO7_DAYS;
private BigDecimal R17_8TO14_DAYS;
private BigDecimal R17_15TO30_DAYS;
private BigDecimal R17_31DAYS_UPTO_2MONTHS;
private BigDecimal R17_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R17_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R17_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R17_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R17_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R17_OVER_5YEARS;
private BigDecimal R17_TOTAL;
private BigDecimal R18_1_DAY;
private BigDecimal R18_2TO7_DAYS;
private BigDecimal R18_8TO14_DAYS;
private BigDecimal R18_15TO30_DAYS;
private BigDecimal R18_31DAYS_UPTO_2MONTHS;
private BigDecimal R18_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R18_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R18_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R18_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R18_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R18_OVER_5YEARS;
private BigDecimal R18_TOTAL;
private BigDecimal R19_1_DAY;
private BigDecimal R19_2TO7_DAYS;
private BigDecimal R19_8TO14_DAYS;
private BigDecimal R19_15TO30_DAYS;
private BigDecimal R19_31DAYS_UPTO_2MONTHS;
private BigDecimal R19_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R19_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R19_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R19_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R19_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R19_OVER_5YEARS;
private BigDecimal R19_TOTAL;
private BigDecimal R20_1_DAY;
private BigDecimal R20_2TO7_DAYS;
private BigDecimal R20_8TO14_DAYS;
private BigDecimal R20_15TO30_DAYS;
private BigDecimal R20_31DAYS_UPTO_2MONTHS;
private BigDecimal R20_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R20_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R20_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R20_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R20_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R20_OVER_5YEARS;
private BigDecimal R20_TOTAL;
private BigDecimal R21_1_DAY;
private BigDecimal R21_2TO7_DAYS;
private BigDecimal R21_8TO14_DAYS;
private BigDecimal R21_15TO30_DAYS;
private BigDecimal R21_31DAYS_UPTO_2MONTHS;
private BigDecimal R21_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R21_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R21_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R21_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R21_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R21_OVER_5YEARS;
private BigDecimal R21_TOTAL;
private BigDecimal R22_1_DAY;
private BigDecimal R22_2TO7_DAYS;
private BigDecimal R22_8TO14_DAYS;
private BigDecimal R22_15TO30_DAYS;
private BigDecimal R22_31DAYS_UPTO_2MONTHS;
private BigDecimal R22_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R22_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R22_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R22_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R22_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R22_OVER_5YEARS;
private BigDecimal R22_TOTAL;
private BigDecimal R23_1_DAY;
private BigDecimal R23_2TO7_DAYS;
private BigDecimal R23_8TO14_DAYS;
private BigDecimal R23_15TO30_DAYS;
private BigDecimal R23_31DAYS_UPTO_2MONTHS;
private BigDecimal R23_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R23_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R23_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R23_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R23_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R23_OVER_5YEARS;
private BigDecimal R23_TOTAL;
private BigDecimal R24_1_DAY;
private BigDecimal R24_2TO7_DAYS;
private BigDecimal R24_8TO14_DAYS;
private BigDecimal R24_15TO30_DAYS;
private BigDecimal R24_31DAYS_UPTO_2MONTHS;
private BigDecimal R24_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R24_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R24_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R24_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R24_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R24_OVER_5YEARS;
private BigDecimal R24_TOTAL;
private BigDecimal R25_1_DAY;
private BigDecimal R25_2TO7_DAYS;
private BigDecimal R25_8TO14_DAYS;
private BigDecimal R25_15TO30_DAYS;
private BigDecimal R25_31DAYS_UPTO_2MONTHS;
private BigDecimal R25_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R25_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R25_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R25_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R25_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R25_OVER_5YEARS;
private BigDecimal R25_TOTAL;
private BigDecimal R26_1_DAY;
private BigDecimal R26_2TO7_DAYS;
private BigDecimal R26_8TO14_DAYS;
private BigDecimal R26_15TO30_DAYS;
private BigDecimal R26_31DAYS_UPTO_2MONTHS;
private BigDecimal R26_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R26_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R26_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R26_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R26_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R26_OVER_5YEARS;
private BigDecimal R26_TOTAL;
private BigDecimal R27_1_DAY;
private BigDecimal R27_2TO7_DAYS;
private BigDecimal R27_8TO14_DAYS;
private BigDecimal R27_15TO30_DAYS;
private BigDecimal R27_31DAYS_UPTO_2MONTHS;
private BigDecimal R27_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R27_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R27_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R27_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R27_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R27_OVER_5YEARS;
private BigDecimal R27_TOTAL;
private BigDecimal R28_1_DAY;
private BigDecimal R28_2TO7_DAYS;
private BigDecimal R28_8TO14_DAYS;
private BigDecimal R28_15TO30_DAYS;
private BigDecimal R28_31DAYS_UPTO_2MONTHS;
private BigDecimal R28_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R28_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R28_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R28_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R28_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R28_OVER_5YEARS;
private BigDecimal R28_TOTAL;
private BigDecimal R29_1_DAY;
private BigDecimal R29_2TO7_DAYS;
private BigDecimal R29_8TO14_DAYS;
private BigDecimal R29_15TO30_DAYS;
private BigDecimal R29_31DAYS_UPTO_2MONTHS;
private BigDecimal R29_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R29_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R29_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R29_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R29_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R29_OVER_5YEARS;
private BigDecimal R29_TOTAL;
private BigDecimal R30_1_DAY;
private BigDecimal R30_2TO7_DAYS;
private BigDecimal R30_8TO14_DAYS;
private BigDecimal R30_15TO30_DAYS;
private BigDecimal R30_31DAYS_UPTO_2MONTHS;
private BigDecimal R30_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R30_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R30_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R30_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R30_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R30_OVER_5YEARS;
private BigDecimal R30_TOTAL;
private BigDecimal R31_1_DAY;
private BigDecimal R31_2TO7_DAYS;
private BigDecimal R31_8TO14_DAYS;
private BigDecimal R31_15TO30_DAYS;
private BigDecimal R31_31DAYS_UPTO_2MONTHS;
private BigDecimal R31_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R31_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R31_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R31_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R31_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R31_OVER_5YEARS;
private BigDecimal R31_TOTAL;
private BigDecimal R32_1_DAY;
private BigDecimal R32_2TO7_DAYS;
private BigDecimal R32_8TO14_DAYS;
private BigDecimal R32_15TO30_DAYS;
private BigDecimal R32_31DAYS_UPTO_2MONTHS;
private BigDecimal R32_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R32_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R32_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R32_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R32_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R32_OVER_5YEARS;
private BigDecimal R32_TOTAL;
private BigDecimal R33_1_DAY;
private BigDecimal R33_2TO7_DAYS;
private BigDecimal R33_8TO14_DAYS;
private BigDecimal R33_15TO30_DAYS;
private BigDecimal R33_31DAYS_UPTO_2MONTHS;
private BigDecimal R33_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R33_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R33_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R33_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R33_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R33_OVER_5YEARS;
private BigDecimal R33_TOTAL;
private BigDecimal R34_1_DAY;
private BigDecimal R34_2TO7_DAYS;
private BigDecimal R34_8TO14_DAYS;
private BigDecimal R34_15TO30_DAYS;
private BigDecimal R34_31DAYS_UPTO_2MONTHS;
private BigDecimal R34_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R34_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R34_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R34_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R34_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R34_OVER_5YEARS;
private BigDecimal R34_TOTAL;
private BigDecimal R35_1_DAY;
private BigDecimal R35_2TO7_DAYS;
private BigDecimal R35_8TO14_DAYS;
private BigDecimal R35_15TO30_DAYS;
private BigDecimal R35_31DAYS_UPTO_2MONTHS;
private BigDecimal R35_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R35_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R35_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R35_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R35_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R35_OVER_5YEARS;
private BigDecimal R35_TOTAL;
private BigDecimal R36_1_DAY;
private BigDecimal R36_2TO7_DAYS;
private BigDecimal R36_8TO14_DAYS;
private BigDecimal R36_15TO30_DAYS;
private BigDecimal R36_31DAYS_UPTO_2MONTHS;
private BigDecimal R36_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R36_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R36_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R36_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R36_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R36_OVER_5YEARS;
private BigDecimal R36_TOTAL;
private BigDecimal R37_1_DAY;
private BigDecimal R37_2TO7_DAYS;
private BigDecimal R37_8TO14_DAYS;
private BigDecimal R37_15TO30_DAYS;
private BigDecimal R37_31DAYS_UPTO_2MONTHS;
private BigDecimal R37_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R37_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R37_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R37_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R37_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R37_OVER_5YEARS;
private BigDecimal R37_TOTAL;
private BigDecimal R38_1_DAY;
private BigDecimal R38_2TO7_DAYS;
private BigDecimal R38_8TO14_DAYS;
private BigDecimal R38_15TO30_DAYS;
private BigDecimal R38_31DAYS_UPTO_2MONTHS;
private BigDecimal R38_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R38_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R38_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R38_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R38_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R38_OVER_5YEARS;
private BigDecimal R38_TOTAL;
private BigDecimal R39_1_DAY;
private BigDecimal R39_2TO7_DAYS;
private BigDecimal R39_8TO14_DAYS;
private BigDecimal R39_15TO30_DAYS;
private BigDecimal R39_31DAYS_UPTO_2MONTHS;
private BigDecimal R39_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R39_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R39_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R39_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R39_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R39_OVER_5YEARS;
private BigDecimal R39_TOTAL;
private BigDecimal R40_1_DAY;
private BigDecimal R40_2TO7_DAYS;
private BigDecimal R40_8TO14_DAYS;
private BigDecimal R40_15TO30_DAYS;
private BigDecimal R40_31DAYS_UPTO_2MONTHS;
private BigDecimal R40_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R40_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R40_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R40_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R40_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R40_OVER_5YEARS;
private BigDecimal R40_TOTAL;
private BigDecimal R41_1_DAY;
private BigDecimal R41_2TO7_DAYS;
private BigDecimal R41_8TO14_DAYS;
private BigDecimal R41_15TO30_DAYS;
private BigDecimal R41_31DAYS_UPTO_2MONTHS;
private BigDecimal R41_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R41_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R41_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R41_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R41_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R41_OVER_5YEARS;
private BigDecimal R41_TOTAL;
private BigDecimal R42_1_DAY;
private BigDecimal R42_2TO7_DAYS;
private BigDecimal R42_8TO14_DAYS;
private BigDecimal R42_15TO30_DAYS;
private BigDecimal R42_31DAYS_UPTO_2MONTHS;
private BigDecimal R42_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R42_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R42_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R42_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R42_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R42_OVER_5YEARS;
private BigDecimal R42_TOTAL;
private BigDecimal R43_1_DAY;
private BigDecimal R43_2TO7_DAYS;
private BigDecimal R43_8TO14_DAYS;
private BigDecimal R43_15TO30_DAYS;
private BigDecimal R43_31DAYS_UPTO_2MONTHS;
private BigDecimal R43_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R43_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R43_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R43_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R43_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R43_OVER_5YEARS;
private BigDecimal R43_TOTAL;
private BigDecimal R44_1_DAY;
private BigDecimal R44_2TO7_DAYS;
private BigDecimal R44_8TO14_DAYS;
private BigDecimal R44_15TO30_DAYS;
private BigDecimal R44_31DAYS_UPTO_2MONTHS;
private BigDecimal R44_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R44_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R44_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R44_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R44_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R44_OVER_5YEARS;
private BigDecimal R44_TOTAL;
private BigDecimal R45_1_DAY;
private BigDecimal R45_2TO7_DAYS;
private BigDecimal R45_8TO14_DAYS;
private BigDecimal R45_15TO30_DAYS;
private BigDecimal R45_31DAYS_UPTO_2MONTHS;
private BigDecimal R45_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R45_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R45_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R45_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R45_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R45_OVER_5YEARS;
private BigDecimal R45_TOTAL;
private BigDecimal R46_1_DAY;
private BigDecimal R46_2TO7_DAYS;
private BigDecimal R46_8TO14_DAYS;
private BigDecimal R46_15TO30_DAYS;
private BigDecimal R46_31DAYS_UPTO_2MONTHS;
private BigDecimal R46_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R46_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R46_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R46_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R46_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R46_OVER_5YEARS;
private BigDecimal R46_TOTAL;
private BigDecimal R47_1_DAY;
private BigDecimal R47_2TO7_DAYS;
private BigDecimal R47_8TO14_DAYS;
private BigDecimal R47_15TO30_DAYS;
private BigDecimal R47_31DAYS_UPTO_2MONTHS;
private BigDecimal R47_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R47_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R47_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R47_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R47_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R47_OVER_5YEARS;
private BigDecimal R47_TOTAL;
private BigDecimal R48_1_DAY;
private BigDecimal R48_2TO7_DAYS;
private BigDecimal R48_8TO14_DAYS;
private BigDecimal R48_15TO30_DAYS;
private BigDecimal R48_31DAYS_UPTO_2MONTHS;
private BigDecimal R48_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R48_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R48_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R48_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R48_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R48_OVER_5YEARS;
private BigDecimal R48_TOTAL;
private BigDecimal R49_1_DAY;
private BigDecimal R49_2TO7_DAYS;
private BigDecimal R49_8TO14_DAYS;
private BigDecimal R49_15TO30_DAYS;
private BigDecimal R49_31DAYS_UPTO_2MONTHS;
private BigDecimal R49_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R49_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R49_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R49_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R49_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R49_OVER_5YEARS;
private BigDecimal R49_TOTAL;
private BigDecimal R50_1_DAY;
private BigDecimal R50_2TO7_DAYS;
private BigDecimal R50_8TO14_DAYS;
private BigDecimal R50_15TO30_DAYS;
private BigDecimal R50_31DAYS_UPTO_2MONTHS;
private BigDecimal R50_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R50_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R50_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R50_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R50_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R50_OVER_5YEARS;
private BigDecimal R50_TOTAL;
private BigDecimal R51_1_DAY;
private BigDecimal R51_2TO7_DAYS;
private BigDecimal R51_8TO14_DAYS;
private BigDecimal R51_15TO30_DAYS;
private BigDecimal R51_31DAYS_UPTO_2MONTHS;
private BigDecimal R51_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R51_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R51_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R51_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R51_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R51_OVER_5YEARS;
private BigDecimal R51_TOTAL;
private BigDecimal R52_1_DAY;
private BigDecimal R52_2TO7_DAYS;
private BigDecimal R52_8TO14_DAYS;
private BigDecimal R52_15TO30_DAYS;
private BigDecimal R52_31DAYS_UPTO_2MONTHS;
private BigDecimal R52_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R52_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R52_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R52_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R52_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R52_OVER_5YEARS;
private BigDecimal R52_TOTAL;
private BigDecimal R53_1_DAY;
private BigDecimal R53_2TO7_DAYS;
private BigDecimal R53_8TO14_DAYS;
private BigDecimal R53_15TO30_DAYS;
private BigDecimal R53_31DAYS_UPTO_2MONTHS;
private BigDecimal R53_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R53_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R53_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R53_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R53_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R53_OVER_5YEARS;
private BigDecimal R53_TOTAL;
private BigDecimal R54_1_DAY;
private BigDecimal R54_2TO7_DAYS;
private BigDecimal R54_8TO14_DAYS;
private BigDecimal R54_15TO30_DAYS;
private BigDecimal R54_31DAYS_UPTO_2MONTHS;
private BigDecimal R54_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R54_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R54_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R54_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R54_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R54_OVER_5YEARS;
private BigDecimal R54_TOTAL;
private BigDecimal R55_1_DAY;
private BigDecimal R55_2TO7_DAYS;
private BigDecimal R55_8TO14_DAYS;
private BigDecimal R55_15TO30_DAYS;
private BigDecimal R55_31DAYS_UPTO_2MONTHS;
private BigDecimal R55_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R55_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R55_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R55_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R55_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R55_OVER_5YEARS;
private BigDecimal R55_TOTAL;
private BigDecimal R56_1_DAY;
private BigDecimal R56_2TO7_DAYS;
private BigDecimal R56_8TO14_DAYS;
private BigDecimal R56_15TO30_DAYS;
private BigDecimal R56_31DAYS_UPTO_2MONTHS;
private BigDecimal R56_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R56_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R56_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R56_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R56_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R56_OVER_5YEARS;
private BigDecimal R56_TOTAL;
private BigDecimal R57_1_DAY;
private BigDecimal R57_2TO7_DAYS;
private BigDecimal R57_8TO14_DAYS;
private BigDecimal R57_15TO30_DAYS;
private BigDecimal R57_31DAYS_UPTO_2MONTHS;
private BigDecimal R57_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R57_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R57_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R57_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R57_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R57_OVER_5YEARS;
private BigDecimal R57_TOTAL;
private BigDecimal R58_1_DAY;
private BigDecimal R58_2TO7_DAYS;
private BigDecimal R58_8TO14_DAYS;
private BigDecimal R58_15TO30_DAYS;
private BigDecimal R58_31DAYS_UPTO_2MONTHS;
private BigDecimal R58_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R58_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R58_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R58_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R58_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R58_OVER_5YEARS;
private BigDecimal R58_TOTAL;
private BigDecimal R59_1_DAY;
private BigDecimal R59_2TO7_DAYS;
private BigDecimal R59_8TO14_DAYS;
private BigDecimal R59_15TO30_DAYS;
private BigDecimal R59_31DAYS_UPTO_2MONTHS;
private BigDecimal R59_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R59_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R59_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R59_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R59_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R59_OVER_5YEARS;
private BigDecimal R59_TOTAL;
private BigDecimal R60_1_DAY;
private BigDecimal R60_2TO7_DAYS;
private BigDecimal R60_8TO14_DAYS;
private BigDecimal R60_15TO30_DAYS;
private BigDecimal R60_31DAYS_UPTO_2MONTHS;
private BigDecimal R60_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R60_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R60_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R60_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R60_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R60_OVER_5YEARS;
private BigDecimal R60_TOTAL;
private BigDecimal R61_1_DAY;
private BigDecimal R61_2TO7_DAYS;
private BigDecimal R61_8TO14_DAYS;
private BigDecimal R61_15TO30_DAYS;
private BigDecimal R61_31DAYS_UPTO_2MONTHS;
private BigDecimal R61_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R61_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R61_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R61_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R61_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R61_OVER_5YEARS;
private BigDecimal R61_TOTAL;
private BigDecimal R62_1_DAY;
private BigDecimal R62_2TO7_DAYS;
private BigDecimal R62_8TO14_DAYS;
private BigDecimal R62_15TO30_DAYS;
private BigDecimal R62_31DAYS_UPTO_2MONTHS;
private BigDecimal R62_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R62_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R62_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R62_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R62_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R62_OVER_5YEARS;
private BigDecimal R62_TOTAL;
private BigDecimal R63_1_DAY;
private BigDecimal R63_2TO7_DAYS;
private BigDecimal R63_8TO14_DAYS;
private BigDecimal R63_15TO30_DAYS;
private BigDecimal R63_31DAYS_UPTO_2MONTHS;
private BigDecimal R63_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R63_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R63_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R63_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R63_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R63_OVER_5YEARS;
private BigDecimal R63_TOTAL;
private BigDecimal R64_1_DAY;
private BigDecimal R64_2TO7_DAYS;
private BigDecimal R64_8TO14_DAYS;
private BigDecimal R64_15TO30_DAYS;
private BigDecimal R64_31DAYS_UPTO_2MONTHS;
private BigDecimal R64_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R64_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R64_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R64_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R64_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R64_OVER_5YEARS;
private BigDecimal R64_TOTAL;
private BigDecimal R65_1_DAY;
private BigDecimal R65_2TO7_DAYS;
private BigDecimal R65_8TO14_DAYS;
private BigDecimal R65_15TO30_DAYS;
private BigDecimal R65_31DAYS_UPTO_2MONTHS;
private BigDecimal R65_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R65_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R65_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R65_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R65_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R65_OVER_5YEARS;
private BigDecimal R65_TOTAL;
private BigDecimal R66_1_DAY;
private BigDecimal R66_2TO7_DAYS;
private BigDecimal R66_8TO14_DAYS;
private BigDecimal R66_15TO30_DAYS;
private BigDecimal R66_31DAYS_UPTO_2MONTHS;
private BigDecimal R66_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R66_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R66_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R66_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R66_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R66_OVER_5YEARS;
private BigDecimal R66_TOTAL;
private BigDecimal R67_1_DAY;
private BigDecimal R67_2TO7_DAYS;
private BigDecimal R67_8TO14_DAYS;
private BigDecimal R67_15TO30_DAYS;
private BigDecimal R67_31DAYS_UPTO_2MONTHS;
private BigDecimal R67_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R67_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R67_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R67_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R67_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R67_OVER_5YEARS;
private BigDecimal R67_TOTAL;
private BigDecimal R68_1_DAY;
private BigDecimal R68_2TO7_DAYS;
private BigDecimal R68_8TO14_DAYS;
private BigDecimal R68_15TO30_DAYS;
private BigDecimal R68_31DAYS_UPTO_2MONTHS;
private BigDecimal R68_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R68_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R68_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R68_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R68_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R68_OVER_5YEARS;
private BigDecimal R68_TOTAL;
private BigDecimal R69_1_DAY;
private BigDecimal R69_2TO7_DAYS;
private BigDecimal R69_8TO14_DAYS;
private BigDecimal R69_15TO30_DAYS;
private BigDecimal R69_31DAYS_UPTO_2MONTHS;
private BigDecimal R69_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R69_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R69_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R69_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R69_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R69_OVER_5YEARS;
private BigDecimal R69_TOTAL;
private BigDecimal R70_1_DAY;
private BigDecimal R70_2TO7_DAYS;
private BigDecimal R70_8TO14_DAYS;
private BigDecimal R70_15TO30_DAYS;
private BigDecimal R70_31DAYS_UPTO_2MONTHS;
private BigDecimal R70_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R70_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R70_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R70_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R70_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R70_OVER_5YEARS;
private BigDecimal R70_TOTAL;
private BigDecimal R71_1_DAY;
private BigDecimal R71_2TO7_DAYS;
private BigDecimal R71_8TO14_DAYS;
private BigDecimal R71_15TO30_DAYS;
private BigDecimal R71_31DAYS_UPTO_2MONTHS;
private BigDecimal R71_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R71_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R71_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R71_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R71_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R71_OVER_5YEARS;
private BigDecimal R71_TOTAL;
private BigDecimal R72_1_DAY;
private BigDecimal R72_2TO7_DAYS;
private BigDecimal R72_8TO14_DAYS;
private BigDecimal R72_15TO30_DAYS;
private BigDecimal R72_31DAYS_UPTO_2MONTHS;
private BigDecimal R72_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R72_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R72_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R72_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R72_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R72_OVER_5YEARS;
private BigDecimal R72_TOTAL;
private BigDecimal R73_1_DAY;
private BigDecimal R73_2TO7_DAYS;
private BigDecimal R73_8TO14_DAYS;
private BigDecimal R73_15TO30_DAYS;
private BigDecimal R73_31DAYS_UPTO_2MONTHS;
private BigDecimal R73_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R73_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R73_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R73_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R73_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R73_OVER_5YEARS;
private BigDecimal R73_TOTAL;
	               
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
	public BigDecimal getR8_1_DAY() {
		return R8_1_DAY;
	}
	public void setR8_1_DAY(BigDecimal r8_1_DAY) {
		R8_1_DAY = r8_1_DAY;
	}
	public BigDecimal getR8_2TO7_DAYS() {
		return R8_2TO7_DAYS;
	}
	public void setR8_2TO7_DAYS(BigDecimal r8_2to7_DAYS) {
		R8_2TO7_DAYS = r8_2to7_DAYS;
	}
	public BigDecimal getR8_8TO14_DAYS() {
		return R8_8TO14_DAYS;
	}
	public void setR8_8TO14_DAYS(BigDecimal r8_8to14_DAYS) {
		R8_8TO14_DAYS = r8_8to14_DAYS;
	}
	public BigDecimal getR8_15TO30_DAYS() {
		return R8_15TO30_DAYS;
	}
	public void setR8_15TO30_DAYS(BigDecimal r8_15to30_DAYS) {
		R8_15TO30_DAYS = r8_15to30_DAYS;
	}
	public BigDecimal getR8_31DAYS_UPTO_2MONTHS() {
		return R8_31DAYS_UPTO_2MONTHS;
	}
	public void setR8_31DAYS_UPTO_2MONTHS(BigDecimal r8_31days_UPTO_2MONTHS) {
		R8_31DAYS_UPTO_2MONTHS = r8_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR8_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R8_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR8_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r8_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R8_MORETHAN_2MONTHS_UPTO_3MONHTS = r8_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR8_OVER_3MONTHS_UPTO_6MONTHS() {
		return R8_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR8_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r8_OVER_3MONTHS_UPTO_6MONTHS) {
		R8_OVER_3MONTHS_UPTO_6MONTHS = r8_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR8_OVER_6MONTHS_UPTO_1YEAR() {
		return R8_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR8_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r8_OVER_6MONTHS_UPTO_1YEAR) {
		R8_OVER_6MONTHS_UPTO_1YEAR = r8_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR8_OVER_1YEAR_UPTO_3YEARS() {
		return R8_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR8_OVER_1YEAR_UPTO_3YEARS(BigDecimal r8_OVER_1YEAR_UPTO_3YEARS) {
		R8_OVER_1YEAR_UPTO_3YEARS = r8_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR8_OVER_3YEARS_UPTO_5YEARS() {
		return R8_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR8_OVER_3YEARS_UPTO_5YEARS(BigDecimal r8_OVER_3YEARS_UPTO_5YEARS) {
		R8_OVER_3YEARS_UPTO_5YEARS = r8_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR8_OVER_5YEARS() {
		return R8_OVER_5YEARS;
	}
	public void setR8_OVER_5YEARS(BigDecimal r8_OVER_5YEARS) {
		R8_OVER_5YEARS = r8_OVER_5YEARS;
	}
	public BigDecimal getR8_TOTAL() {
		return R8_TOTAL;
	}
	public void setR8_TOTAL(BigDecimal r8_TOTAL) {
		R8_TOTAL = r8_TOTAL;
	}
	public BigDecimal getR9_1_DAY() {
		return R9_1_DAY;
	}
	public void setR9_1_DAY(BigDecimal r9_1_DAY) {
		R9_1_DAY = r9_1_DAY;
	}
	public BigDecimal getR9_2TO7_DAYS() {
		return R9_2TO7_DAYS;
	}
	public void setR9_2TO7_DAYS(BigDecimal r9_2to7_DAYS) {
		R9_2TO7_DAYS = r9_2to7_DAYS;
	}
	public BigDecimal getR9_8TO14_DAYS() {
		return R9_8TO14_DAYS;
	}
	public void setR9_8TO14_DAYS(BigDecimal r9_8to14_DAYS) {
		R9_8TO14_DAYS = r9_8to14_DAYS;
	}
	public BigDecimal getR9_15TO30_DAYS() {
		return R9_15TO30_DAYS;
	}
	public void setR9_15TO30_DAYS(BigDecimal r9_15to30_DAYS) {
		R9_15TO30_DAYS = r9_15to30_DAYS;
	}
	public BigDecimal getR9_31DAYS_UPTO_2MONTHS() {
		return R9_31DAYS_UPTO_2MONTHS;
	}
	public void setR9_31DAYS_UPTO_2MONTHS(BigDecimal r9_31days_UPTO_2MONTHS) {
		R9_31DAYS_UPTO_2MONTHS = r9_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR9_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R9_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR9_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r9_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R9_MORETHAN_2MONTHS_UPTO_3MONHTS = r9_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR9_OVER_3MONTHS_UPTO_6MONTHS() {
		return R9_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR9_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r9_OVER_3MONTHS_UPTO_6MONTHS) {
		R9_OVER_3MONTHS_UPTO_6MONTHS = r9_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR9_OVER_6MONTHS_UPTO_1YEAR() {
		return R9_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR9_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r9_OVER_6MONTHS_UPTO_1YEAR) {
		R9_OVER_6MONTHS_UPTO_1YEAR = r9_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR9_OVER_1YEAR_UPTO_3YEARS() {
		return R9_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR9_OVER_1YEAR_UPTO_3YEARS(BigDecimal r9_OVER_1YEAR_UPTO_3YEARS) {
		R9_OVER_1YEAR_UPTO_3YEARS = r9_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR9_OVER_3YEARS_UPTO_5YEARS() {
		return R9_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR9_OVER_3YEARS_UPTO_5YEARS(BigDecimal r9_OVER_3YEARS_UPTO_5YEARS) {
		R9_OVER_3YEARS_UPTO_5YEARS = r9_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR9_OVER_5YEARS() {
		return R9_OVER_5YEARS;
	}
	public void setR9_OVER_5YEARS(BigDecimal r9_OVER_5YEARS) {
		R9_OVER_5YEARS = r9_OVER_5YEARS;
	}
	public BigDecimal getR9_TOTAL() {
		return R9_TOTAL;
	}
	public void setR9_TOTAL(BigDecimal r9_TOTAL) {
		R9_TOTAL = r9_TOTAL;
	}
	public BigDecimal getR10_1_DAY() {
		return R10_1_DAY;
	}
	public void setR10_1_DAY(BigDecimal r10_1_DAY) {
		R10_1_DAY = r10_1_DAY;
	}
	public BigDecimal getR10_2TO7_DAYS() {
		return R10_2TO7_DAYS;
	}
	public void setR10_2TO7_DAYS(BigDecimal r10_2to7_DAYS) {
		R10_2TO7_DAYS = r10_2to7_DAYS;
	}
	public BigDecimal getR10_8TO14_DAYS() {
		return R10_8TO14_DAYS;
	}
	public void setR10_8TO14_DAYS(BigDecimal r10_8to14_DAYS) {
		R10_8TO14_DAYS = r10_8to14_DAYS;
	}
	public BigDecimal getR10_15TO30_DAYS() {
		return R10_15TO30_DAYS;
	}
	public void setR10_15TO30_DAYS(BigDecimal r10_15to30_DAYS) {
		R10_15TO30_DAYS = r10_15to30_DAYS;
	}
	public BigDecimal getR10_31DAYS_UPTO_2MONTHS() {
		return R10_31DAYS_UPTO_2MONTHS;
	}
	public void setR10_31DAYS_UPTO_2MONTHS(BigDecimal r10_31days_UPTO_2MONTHS) {
		R10_31DAYS_UPTO_2MONTHS = r10_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR10_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R10_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR10_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r10_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R10_MORETHAN_2MONTHS_UPTO_3MONHTS = r10_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR10_OVER_3MONTHS_UPTO_6MONTHS() {
		return R10_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR10_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r10_OVER_3MONTHS_UPTO_6MONTHS) {
		R10_OVER_3MONTHS_UPTO_6MONTHS = r10_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR10_OVER_6MONTHS_UPTO_1YEAR() {
		return R10_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR10_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r10_OVER_6MONTHS_UPTO_1YEAR) {
		R10_OVER_6MONTHS_UPTO_1YEAR = r10_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR10_OVER_1YEAR_UPTO_3YEARS() {
		return R10_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR10_OVER_1YEAR_UPTO_3YEARS(BigDecimal r10_OVER_1YEAR_UPTO_3YEARS) {
		R10_OVER_1YEAR_UPTO_3YEARS = r10_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR10_OVER_3YEARS_UPTO_5YEARS() {
		return R10_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR10_OVER_3YEARS_UPTO_5YEARS(BigDecimal r10_OVER_3YEARS_UPTO_5YEARS) {
		R10_OVER_3YEARS_UPTO_5YEARS = r10_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR10_OVER_5YEARS() {
		return R10_OVER_5YEARS;
	}
	public void setR10_OVER_5YEARS(BigDecimal r10_OVER_5YEARS) {
		R10_OVER_5YEARS = r10_OVER_5YEARS;
	}
	public BigDecimal getR10_TOTAL() {
		return R10_TOTAL;
	}
	public void setR10_TOTAL(BigDecimal r10_TOTAL) {
		R10_TOTAL = r10_TOTAL;
	}
	public BigDecimal getR11_1_DAY() {
		return R11_1_DAY;
	}
	public void setR11_1_DAY(BigDecimal r11_1_DAY) {
		R11_1_DAY = r11_1_DAY;
	}
	public BigDecimal getR11_2TO7_DAYS() {
		return R11_2TO7_DAYS;
	}
	public void setR11_2TO7_DAYS(BigDecimal r11_2to7_DAYS) {
		R11_2TO7_DAYS = r11_2to7_DAYS;
	}
	public BigDecimal getR11_8TO14_DAYS() {
		return R11_8TO14_DAYS;
	}
	public void setR11_8TO14_DAYS(BigDecimal r11_8to14_DAYS) {
		R11_8TO14_DAYS = r11_8to14_DAYS;
	}
	public BigDecimal getR11_15TO30_DAYS() {
		return R11_15TO30_DAYS;
	}
	public void setR11_15TO30_DAYS(BigDecimal r11_15to30_DAYS) {
		R11_15TO30_DAYS = r11_15to30_DAYS;
	}
	public BigDecimal getR11_31DAYS_UPTO_2MONTHS() {
		return R11_31DAYS_UPTO_2MONTHS;
	}
	public void setR11_31DAYS_UPTO_2MONTHS(BigDecimal r11_31days_UPTO_2MONTHS) {
		R11_31DAYS_UPTO_2MONTHS = r11_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR11_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R11_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR11_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r11_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R11_MORETHAN_2MONTHS_UPTO_3MONHTS = r11_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR11_OVER_3MONTHS_UPTO_6MONTHS() {
		return R11_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR11_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r11_OVER_3MONTHS_UPTO_6MONTHS) {
		R11_OVER_3MONTHS_UPTO_6MONTHS = r11_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR11_OVER_6MONTHS_UPTO_1YEAR() {
		return R11_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR11_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r11_OVER_6MONTHS_UPTO_1YEAR) {
		R11_OVER_6MONTHS_UPTO_1YEAR = r11_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR11_OVER_1YEAR_UPTO_3YEARS() {
		return R11_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR11_OVER_1YEAR_UPTO_3YEARS(BigDecimal r11_OVER_1YEAR_UPTO_3YEARS) {
		R11_OVER_1YEAR_UPTO_3YEARS = r11_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR11_OVER_3YEARS_UPTO_5YEARS() {
		return R11_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR11_OVER_3YEARS_UPTO_5YEARS(BigDecimal r11_OVER_3YEARS_UPTO_5YEARS) {
		R11_OVER_3YEARS_UPTO_5YEARS = r11_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR11_OVER_5YEARS() {
		return R11_OVER_5YEARS;
	}
	public void setR11_OVER_5YEARS(BigDecimal r11_OVER_5YEARS) {
		R11_OVER_5YEARS = r11_OVER_5YEARS;
	}
	public BigDecimal getR11_TOTAL() {
		return R11_TOTAL;
	}
	public void setR11_TOTAL(BigDecimal r11_TOTAL) {
		R11_TOTAL = r11_TOTAL;
	}
	public BigDecimal getR12_1_DAY() {
		return R12_1_DAY;
	}
	public void setR12_1_DAY(BigDecimal r12_1_DAY) {
		R12_1_DAY = r12_1_DAY;
	}
	public BigDecimal getR12_2TO7_DAYS() {
		return R12_2TO7_DAYS;
	}
	public void setR12_2TO7_DAYS(BigDecimal r12_2to7_DAYS) {
		R12_2TO7_DAYS = r12_2to7_DAYS;
	}
	public BigDecimal getR12_8TO14_DAYS() {
		return R12_8TO14_DAYS;
	}
	public void setR12_8TO14_DAYS(BigDecimal r12_8to14_DAYS) {
		R12_8TO14_DAYS = r12_8to14_DAYS;
	}
	public BigDecimal getR12_15TO30_DAYS() {
		return R12_15TO30_DAYS;
	}
	public void setR12_15TO30_DAYS(BigDecimal r12_15to30_DAYS) {
		R12_15TO30_DAYS = r12_15to30_DAYS;
	}
	public BigDecimal getR12_31DAYS_UPTO_2MONTHS() {
		return R12_31DAYS_UPTO_2MONTHS;
	}
	public void setR12_31DAYS_UPTO_2MONTHS(BigDecimal r12_31days_UPTO_2MONTHS) {
		R12_31DAYS_UPTO_2MONTHS = r12_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR12_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R12_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR12_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r12_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R12_MORETHAN_2MONTHS_UPTO_3MONHTS = r12_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR12_OVER_3MONTHS_UPTO_6MONTHS() {
		return R12_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR12_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r12_OVER_3MONTHS_UPTO_6MONTHS) {
		R12_OVER_3MONTHS_UPTO_6MONTHS = r12_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR12_OVER_6MONTHS_UPTO_1YEAR() {
		return R12_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR12_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r12_OVER_6MONTHS_UPTO_1YEAR) {
		R12_OVER_6MONTHS_UPTO_1YEAR = r12_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR12_OVER_1YEAR_UPTO_3YEARS() {
		return R12_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR12_OVER_1YEAR_UPTO_3YEARS(BigDecimal r12_OVER_1YEAR_UPTO_3YEARS) {
		R12_OVER_1YEAR_UPTO_3YEARS = r12_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR12_OVER_3YEARS_UPTO_5YEARS() {
		return R12_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR12_OVER_3YEARS_UPTO_5YEARS(BigDecimal r12_OVER_3YEARS_UPTO_5YEARS) {
		R12_OVER_3YEARS_UPTO_5YEARS = r12_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR12_OVER_5YEARS() {
		return R12_OVER_5YEARS;
	}
	public void setR12_OVER_5YEARS(BigDecimal r12_OVER_5YEARS) {
		R12_OVER_5YEARS = r12_OVER_5YEARS;
	}
	public BigDecimal getR12_TOTAL() {
		return R12_TOTAL;
	}
	public void setR12_TOTAL(BigDecimal r12_TOTAL) {
		R12_TOTAL = r12_TOTAL;
	}
	public BigDecimal getR13_1_DAY() {
		return R13_1_DAY;
	}
	public void setR13_1_DAY(BigDecimal r13_1_DAY) {
		R13_1_DAY = r13_1_DAY;
	}
	public BigDecimal getR13_2TO7_DAYS() {
		return R13_2TO7_DAYS;
	}
	public void setR13_2TO7_DAYS(BigDecimal r13_2to7_DAYS) {
		R13_2TO7_DAYS = r13_2to7_DAYS;
	}
	public BigDecimal getR13_8TO14_DAYS() {
		return R13_8TO14_DAYS;
	}
	public void setR13_8TO14_DAYS(BigDecimal r13_8to14_DAYS) {
		R13_8TO14_DAYS = r13_8to14_DAYS;
	}
	public BigDecimal getR13_15TO30_DAYS() {
		return R13_15TO30_DAYS;
	}
	public void setR13_15TO30_DAYS(BigDecimal r13_15to30_DAYS) {
		R13_15TO30_DAYS = r13_15to30_DAYS;
	}
	public BigDecimal getR13_31DAYS_UPTO_2MONTHS() {
		return R13_31DAYS_UPTO_2MONTHS;
	}
	public void setR13_31DAYS_UPTO_2MONTHS(BigDecimal r13_31days_UPTO_2MONTHS) {
		R13_31DAYS_UPTO_2MONTHS = r13_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR13_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R13_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR13_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r13_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R13_MORETHAN_2MONTHS_UPTO_3MONHTS = r13_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR13_OVER_3MONTHS_UPTO_6MONTHS() {
		return R13_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR13_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r13_OVER_3MONTHS_UPTO_6MONTHS) {
		R13_OVER_3MONTHS_UPTO_6MONTHS = r13_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR13_OVER_6MONTHS_UPTO_1YEAR() {
		return R13_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR13_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r13_OVER_6MONTHS_UPTO_1YEAR) {
		R13_OVER_6MONTHS_UPTO_1YEAR = r13_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR13_OVER_1YEAR_UPTO_3YEARS() {
		return R13_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR13_OVER_1YEAR_UPTO_3YEARS(BigDecimal r13_OVER_1YEAR_UPTO_3YEARS) {
		R13_OVER_1YEAR_UPTO_3YEARS = r13_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR13_OVER_3YEARS_UPTO_5YEARS() {
		return R13_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR13_OVER_3YEARS_UPTO_5YEARS(BigDecimal r13_OVER_3YEARS_UPTO_5YEARS) {
		R13_OVER_3YEARS_UPTO_5YEARS = r13_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR13_OVER_5YEARS() {
		return R13_OVER_5YEARS;
	}
	public void setR13_OVER_5YEARS(BigDecimal r13_OVER_5YEARS) {
		R13_OVER_5YEARS = r13_OVER_5YEARS;
	}
	public BigDecimal getR13_TOTAL() {
		return R13_TOTAL;
	}
	public void setR13_TOTAL(BigDecimal r13_TOTAL) {
		R13_TOTAL = r13_TOTAL;
	}
	public BigDecimal getR14_1_DAY() {
		return R14_1_DAY;
	}
	public void setR14_1_DAY(BigDecimal r14_1_DAY) {
		R14_1_DAY = r14_1_DAY;
	}
	public BigDecimal getR14_2TO7_DAYS() {
		return R14_2TO7_DAYS;
	}
	public void setR14_2TO7_DAYS(BigDecimal r14_2to7_DAYS) {
		R14_2TO7_DAYS = r14_2to7_DAYS;
	}
	public BigDecimal getR14_8TO14_DAYS() {
		return R14_8TO14_DAYS;
	}
	public void setR14_8TO14_DAYS(BigDecimal r14_8to14_DAYS) {
		R14_8TO14_DAYS = r14_8to14_DAYS;
	}
	public BigDecimal getR14_15TO30_DAYS() {
		return R14_15TO30_DAYS;
	}
	public void setR14_15TO30_DAYS(BigDecimal r14_15to30_DAYS) {
		R14_15TO30_DAYS = r14_15to30_DAYS;
	}
	public BigDecimal getR14_31DAYS_UPTO_2MONTHS() {
		return R14_31DAYS_UPTO_2MONTHS;
	}
	public void setR14_31DAYS_UPTO_2MONTHS(BigDecimal r14_31days_UPTO_2MONTHS) {
		R14_31DAYS_UPTO_2MONTHS = r14_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR14_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R14_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR14_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r14_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R14_MORETHAN_2MONTHS_UPTO_3MONHTS = r14_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR14_OVER_3MONTHS_UPTO_6MONTHS() {
		return R14_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR14_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r14_OVER_3MONTHS_UPTO_6MONTHS) {
		R14_OVER_3MONTHS_UPTO_6MONTHS = r14_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR14_OVER_6MONTHS_UPTO_1YEAR() {
		return R14_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR14_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r14_OVER_6MONTHS_UPTO_1YEAR) {
		R14_OVER_6MONTHS_UPTO_1YEAR = r14_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR14_OVER_1YEAR_UPTO_3YEARS() {
		return R14_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR14_OVER_1YEAR_UPTO_3YEARS(BigDecimal r14_OVER_1YEAR_UPTO_3YEARS) {
		R14_OVER_1YEAR_UPTO_3YEARS = r14_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR14_OVER_3YEARS_UPTO_5YEARS() {
		return R14_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR14_OVER_3YEARS_UPTO_5YEARS(BigDecimal r14_OVER_3YEARS_UPTO_5YEARS) {
		R14_OVER_3YEARS_UPTO_5YEARS = r14_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR14_OVER_5YEARS() {
		return R14_OVER_5YEARS;
	}
	public void setR14_OVER_5YEARS(BigDecimal r14_OVER_5YEARS) {
		R14_OVER_5YEARS = r14_OVER_5YEARS;
	}
	public BigDecimal getR14_TOTAL() {
		return R14_TOTAL;
	}
	public void setR14_TOTAL(BigDecimal r14_TOTAL) {
		R14_TOTAL = r14_TOTAL;
	}
	public BigDecimal getR15_1_DAY() {
		return R15_1_DAY;
	}
	public void setR15_1_DAY(BigDecimal r15_1_DAY) {
		R15_1_DAY = r15_1_DAY;
	}
	public BigDecimal getR15_2TO7_DAYS() {
		return R15_2TO7_DAYS;
	}
	public void setR15_2TO7_DAYS(BigDecimal r15_2to7_DAYS) {
		R15_2TO7_DAYS = r15_2to7_DAYS;
	}
	public BigDecimal getR15_8TO14_DAYS() {
		return R15_8TO14_DAYS;
	}
	public void setR15_8TO14_DAYS(BigDecimal r15_8to14_DAYS) {
		R15_8TO14_DAYS = r15_8to14_DAYS;
	}
	public BigDecimal getR15_15TO30_DAYS() {
		return R15_15TO30_DAYS;
	}
	public void setR15_15TO30_DAYS(BigDecimal r15_15to30_DAYS) {
		R15_15TO30_DAYS = r15_15to30_DAYS;
	}
	public BigDecimal getR15_31DAYS_UPTO_2MONTHS() {
		return R15_31DAYS_UPTO_2MONTHS;
	}
	public void setR15_31DAYS_UPTO_2MONTHS(BigDecimal r15_31days_UPTO_2MONTHS) {
		R15_31DAYS_UPTO_2MONTHS = r15_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR15_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R15_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR15_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r15_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R15_MORETHAN_2MONTHS_UPTO_3MONHTS = r15_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR15_OVER_3MONTHS_UPTO_6MONTHS() {
		return R15_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR15_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r15_OVER_3MONTHS_UPTO_6MONTHS) {
		R15_OVER_3MONTHS_UPTO_6MONTHS = r15_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR15_OVER_6MONTHS_UPTO_1YEAR() {
		return R15_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR15_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r15_OVER_6MONTHS_UPTO_1YEAR) {
		R15_OVER_6MONTHS_UPTO_1YEAR = r15_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR15_OVER_1YEAR_UPTO_3YEARS() {
		return R15_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR15_OVER_1YEAR_UPTO_3YEARS(BigDecimal r15_OVER_1YEAR_UPTO_3YEARS) {
		R15_OVER_1YEAR_UPTO_3YEARS = r15_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR15_OVER_3YEARS_UPTO_5YEARS() {
		return R15_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR15_OVER_3YEARS_UPTO_5YEARS(BigDecimal r15_OVER_3YEARS_UPTO_5YEARS) {
		R15_OVER_3YEARS_UPTO_5YEARS = r15_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR15_OVER_5YEARS() {
		return R15_OVER_5YEARS;
	}
	public void setR15_OVER_5YEARS(BigDecimal r15_OVER_5YEARS) {
		R15_OVER_5YEARS = r15_OVER_5YEARS;
	}
	public BigDecimal getR15_TOTAL() {
		return R15_TOTAL;
	}
	public void setR15_TOTAL(BigDecimal r15_TOTAL) {
		R15_TOTAL = r15_TOTAL;
	}
	public BigDecimal getR16_1_DAY() {
		return R16_1_DAY;
	}
	public void setR16_1_DAY(BigDecimal r16_1_DAY) {
		R16_1_DAY = r16_1_DAY;
	}
	public BigDecimal getR16_2TO7_DAYS() {
		return R16_2TO7_DAYS;
	}
	public void setR16_2TO7_DAYS(BigDecimal r16_2to7_DAYS) {
		R16_2TO7_DAYS = r16_2to7_DAYS;
	}
	public BigDecimal getR16_8TO14_DAYS() {
		return R16_8TO14_DAYS;
	}
	public void setR16_8TO14_DAYS(BigDecimal r16_8to14_DAYS) {
		R16_8TO14_DAYS = r16_8to14_DAYS;
	}
	public BigDecimal getR16_15TO30_DAYS() {
		return R16_15TO30_DAYS;
	}
	public void setR16_15TO30_DAYS(BigDecimal r16_15to30_DAYS) {
		R16_15TO30_DAYS = r16_15to30_DAYS;
	}
	public BigDecimal getR16_31DAYS_UPTO_2MONTHS() {
		return R16_31DAYS_UPTO_2MONTHS;
	}
	public void setR16_31DAYS_UPTO_2MONTHS(BigDecimal r16_31days_UPTO_2MONTHS) {
		R16_31DAYS_UPTO_2MONTHS = r16_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR16_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R16_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR16_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r16_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R16_MORETHAN_2MONTHS_UPTO_3MONHTS = r16_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR16_OVER_3MONTHS_UPTO_6MONTHS() {
		return R16_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR16_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r16_OVER_3MONTHS_UPTO_6MONTHS) {
		R16_OVER_3MONTHS_UPTO_6MONTHS = r16_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR16_OVER_6MONTHS_UPTO_1YEAR() {
		return R16_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR16_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r16_OVER_6MONTHS_UPTO_1YEAR) {
		R16_OVER_6MONTHS_UPTO_1YEAR = r16_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR16_OVER_1YEAR_UPTO_3YEARS() {
		return R16_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR16_OVER_1YEAR_UPTO_3YEARS(BigDecimal r16_OVER_1YEAR_UPTO_3YEARS) {
		R16_OVER_1YEAR_UPTO_3YEARS = r16_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR16_OVER_3YEARS_UPTO_5YEARS() {
		return R16_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR16_OVER_3YEARS_UPTO_5YEARS(BigDecimal r16_OVER_3YEARS_UPTO_5YEARS) {
		R16_OVER_3YEARS_UPTO_5YEARS = r16_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR16_OVER_5YEARS() {
		return R16_OVER_5YEARS;
	}
	public void setR16_OVER_5YEARS(BigDecimal r16_OVER_5YEARS) {
		R16_OVER_5YEARS = r16_OVER_5YEARS;
	}
	public BigDecimal getR16_TOTAL() {
		return R16_TOTAL;
	}
	public void setR16_TOTAL(BigDecimal r16_TOTAL) {
		R16_TOTAL = r16_TOTAL;
	}
	public BigDecimal getR17_1_DAY() {
		return R17_1_DAY;
	}
	public void setR17_1_DAY(BigDecimal r17_1_DAY) {
		R17_1_DAY = r17_1_DAY;
	}
	public BigDecimal getR17_2TO7_DAYS() {
		return R17_2TO7_DAYS;
	}
	public void setR17_2TO7_DAYS(BigDecimal r17_2to7_DAYS) {
		R17_2TO7_DAYS = r17_2to7_DAYS;
	}
	public BigDecimal getR17_8TO14_DAYS() {
		return R17_8TO14_DAYS;
	}
	public void setR17_8TO14_DAYS(BigDecimal r17_8to14_DAYS) {
		R17_8TO14_DAYS = r17_8to14_DAYS;
	}
	public BigDecimal getR17_15TO30_DAYS() {
		return R17_15TO30_DAYS;
	}
	public void setR17_15TO30_DAYS(BigDecimal r17_15to30_DAYS) {
		R17_15TO30_DAYS = r17_15to30_DAYS;
	}
	public BigDecimal getR17_31DAYS_UPTO_2MONTHS() {
		return R17_31DAYS_UPTO_2MONTHS;
	}
	public void setR17_31DAYS_UPTO_2MONTHS(BigDecimal r17_31days_UPTO_2MONTHS) {
		R17_31DAYS_UPTO_2MONTHS = r17_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR17_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R17_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR17_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r17_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R17_MORETHAN_2MONTHS_UPTO_3MONHTS = r17_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR17_OVER_3MONTHS_UPTO_6MONTHS() {
		return R17_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR17_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r17_OVER_3MONTHS_UPTO_6MONTHS) {
		R17_OVER_3MONTHS_UPTO_6MONTHS = r17_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR17_OVER_6MONTHS_UPTO_1YEAR() {
		return R17_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR17_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r17_OVER_6MONTHS_UPTO_1YEAR) {
		R17_OVER_6MONTHS_UPTO_1YEAR = r17_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR17_OVER_1YEAR_UPTO_3YEARS() {
		return R17_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR17_OVER_1YEAR_UPTO_3YEARS(BigDecimal r17_OVER_1YEAR_UPTO_3YEARS) {
		R17_OVER_1YEAR_UPTO_3YEARS = r17_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR17_OVER_3YEARS_UPTO_5YEARS() {
		return R17_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR17_OVER_3YEARS_UPTO_5YEARS(BigDecimal r17_OVER_3YEARS_UPTO_5YEARS) {
		R17_OVER_3YEARS_UPTO_5YEARS = r17_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR17_OVER_5YEARS() {
		return R17_OVER_5YEARS;
	}
	public void setR17_OVER_5YEARS(BigDecimal r17_OVER_5YEARS) {
		R17_OVER_5YEARS = r17_OVER_5YEARS;
	}
	public BigDecimal getR17_TOTAL() {
		return R17_TOTAL;
	}
	public void setR17_TOTAL(BigDecimal r17_TOTAL) {
		R17_TOTAL = r17_TOTAL;
	}
	public BigDecimal getR18_1_DAY() {
		return R18_1_DAY;
	}
	public void setR18_1_DAY(BigDecimal r18_1_DAY) {
		R18_1_DAY = r18_1_DAY;
	}
	public BigDecimal getR18_2TO7_DAYS() {
		return R18_2TO7_DAYS;
	}
	public void setR18_2TO7_DAYS(BigDecimal r18_2to7_DAYS) {
		R18_2TO7_DAYS = r18_2to7_DAYS;
	}
	public BigDecimal getR18_8TO14_DAYS() {
		return R18_8TO14_DAYS;
	}
	public void setR18_8TO14_DAYS(BigDecimal r18_8to14_DAYS) {
		R18_8TO14_DAYS = r18_8to14_DAYS;
	}
	public BigDecimal getR18_15TO30_DAYS() {
		return R18_15TO30_DAYS;
	}
	public void setR18_15TO30_DAYS(BigDecimal r18_15to30_DAYS) {
		R18_15TO30_DAYS = r18_15to30_DAYS;
	}
	public BigDecimal getR18_31DAYS_UPTO_2MONTHS() {
		return R18_31DAYS_UPTO_2MONTHS;
	}
	public void setR18_31DAYS_UPTO_2MONTHS(BigDecimal r18_31days_UPTO_2MONTHS) {
		R18_31DAYS_UPTO_2MONTHS = r18_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR18_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R18_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR18_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r18_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R18_MORETHAN_2MONTHS_UPTO_3MONHTS = r18_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR18_OVER_3MONTHS_UPTO_6MONTHS() {
		return R18_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR18_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r18_OVER_3MONTHS_UPTO_6MONTHS) {
		R18_OVER_3MONTHS_UPTO_6MONTHS = r18_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR18_OVER_6MONTHS_UPTO_1YEAR() {
		return R18_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR18_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r18_OVER_6MONTHS_UPTO_1YEAR) {
		R18_OVER_6MONTHS_UPTO_1YEAR = r18_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR18_OVER_1YEAR_UPTO_3YEARS() {
		return R18_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR18_OVER_1YEAR_UPTO_3YEARS(BigDecimal r18_OVER_1YEAR_UPTO_3YEARS) {
		R18_OVER_1YEAR_UPTO_3YEARS = r18_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR18_OVER_3YEARS_UPTO_5YEARS() {
		return R18_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR18_OVER_3YEARS_UPTO_5YEARS(BigDecimal r18_OVER_3YEARS_UPTO_5YEARS) {
		R18_OVER_3YEARS_UPTO_5YEARS = r18_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR18_OVER_5YEARS() {
		return R18_OVER_5YEARS;
	}
	public void setR18_OVER_5YEARS(BigDecimal r18_OVER_5YEARS) {
		R18_OVER_5YEARS = r18_OVER_5YEARS;
	}
	public BigDecimal getR18_TOTAL() {
		return R18_TOTAL;
	}
	public void setR18_TOTAL(BigDecimal r18_TOTAL) {
		R18_TOTAL = r18_TOTAL;
	}
	public BigDecimal getR19_1_DAY() {
		return R19_1_DAY;
	}
	public void setR19_1_DAY(BigDecimal r19_1_DAY) {
		R19_1_DAY = r19_1_DAY;
	}
	public BigDecimal getR19_2TO7_DAYS() {
		return R19_2TO7_DAYS;
	}
	public void setR19_2TO7_DAYS(BigDecimal r19_2to7_DAYS) {
		R19_2TO7_DAYS = r19_2to7_DAYS;
	}
	public BigDecimal getR19_8TO14_DAYS() {
		return R19_8TO14_DAYS;
	}
	public void setR19_8TO14_DAYS(BigDecimal r19_8to14_DAYS) {
		R19_8TO14_DAYS = r19_8to14_DAYS;
	}
	public BigDecimal getR19_15TO30_DAYS() {
		return R19_15TO30_DAYS;
	}
	public void setR19_15TO30_DAYS(BigDecimal r19_15to30_DAYS) {
		R19_15TO30_DAYS = r19_15to30_DAYS;
	}
	public BigDecimal getR19_31DAYS_UPTO_2MONTHS() {
		return R19_31DAYS_UPTO_2MONTHS;
	}
	public void setR19_31DAYS_UPTO_2MONTHS(BigDecimal r19_31days_UPTO_2MONTHS) {
		R19_31DAYS_UPTO_2MONTHS = r19_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR19_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R19_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR19_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r19_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R19_MORETHAN_2MONTHS_UPTO_3MONHTS = r19_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR19_OVER_3MONTHS_UPTO_6MONTHS() {
		return R19_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR19_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r19_OVER_3MONTHS_UPTO_6MONTHS) {
		R19_OVER_3MONTHS_UPTO_6MONTHS = r19_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR19_OVER_6MONTHS_UPTO_1YEAR() {
		return R19_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR19_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r19_OVER_6MONTHS_UPTO_1YEAR) {
		R19_OVER_6MONTHS_UPTO_1YEAR = r19_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR19_OVER_1YEAR_UPTO_3YEARS() {
		return R19_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR19_OVER_1YEAR_UPTO_3YEARS(BigDecimal r19_OVER_1YEAR_UPTO_3YEARS) {
		R19_OVER_1YEAR_UPTO_3YEARS = r19_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR19_OVER_3YEARS_UPTO_5YEARS() {
		return R19_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR19_OVER_3YEARS_UPTO_5YEARS(BigDecimal r19_OVER_3YEARS_UPTO_5YEARS) {
		R19_OVER_3YEARS_UPTO_5YEARS = r19_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR19_OVER_5YEARS() {
		return R19_OVER_5YEARS;
	}
	public void setR19_OVER_5YEARS(BigDecimal r19_OVER_5YEARS) {
		R19_OVER_5YEARS = r19_OVER_5YEARS;
	}
	public BigDecimal getR19_TOTAL() {
		return R19_TOTAL;
	}
	public void setR19_TOTAL(BigDecimal r19_TOTAL) {
		R19_TOTAL = r19_TOTAL;
	}
	public BigDecimal getR20_1_DAY() {
		return R20_1_DAY;
	}
	public void setR20_1_DAY(BigDecimal r20_1_DAY) {
		R20_1_DAY = r20_1_DAY;
	}
	public BigDecimal getR20_2TO7_DAYS() {
		return R20_2TO7_DAYS;
	}
	public void setR20_2TO7_DAYS(BigDecimal r20_2to7_DAYS) {
		R20_2TO7_DAYS = r20_2to7_DAYS;
	}
	public BigDecimal getR20_8TO14_DAYS() {
		return R20_8TO14_DAYS;
	}
	public void setR20_8TO14_DAYS(BigDecimal r20_8to14_DAYS) {
		R20_8TO14_DAYS = r20_8to14_DAYS;
	}
	public BigDecimal getR20_15TO30_DAYS() {
		return R20_15TO30_DAYS;
	}
	public void setR20_15TO30_DAYS(BigDecimal r20_15to30_DAYS) {
		R20_15TO30_DAYS = r20_15to30_DAYS;
	}
	public BigDecimal getR20_31DAYS_UPTO_2MONTHS() {
		return R20_31DAYS_UPTO_2MONTHS;
	}
	public void setR20_31DAYS_UPTO_2MONTHS(BigDecimal r20_31days_UPTO_2MONTHS) {
		R20_31DAYS_UPTO_2MONTHS = r20_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR20_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R20_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR20_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r20_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R20_MORETHAN_2MONTHS_UPTO_3MONHTS = r20_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR20_OVER_3MONTHS_UPTO_6MONTHS() {
		return R20_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR20_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r20_OVER_3MONTHS_UPTO_6MONTHS) {
		R20_OVER_3MONTHS_UPTO_6MONTHS = r20_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR20_OVER_6MONTHS_UPTO_1YEAR() {
		return R20_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR20_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r20_OVER_6MONTHS_UPTO_1YEAR) {
		R20_OVER_6MONTHS_UPTO_1YEAR = r20_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR20_OVER_1YEAR_UPTO_3YEARS() {
		return R20_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR20_OVER_1YEAR_UPTO_3YEARS(BigDecimal r20_OVER_1YEAR_UPTO_3YEARS) {
		R20_OVER_1YEAR_UPTO_3YEARS = r20_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR20_OVER_3YEARS_UPTO_5YEARS() {
		return R20_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR20_OVER_3YEARS_UPTO_5YEARS(BigDecimal r20_OVER_3YEARS_UPTO_5YEARS) {
		R20_OVER_3YEARS_UPTO_5YEARS = r20_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR20_OVER_5YEARS() {
		return R20_OVER_5YEARS;
	}
	public void setR20_OVER_5YEARS(BigDecimal r20_OVER_5YEARS) {
		R20_OVER_5YEARS = r20_OVER_5YEARS;
	}
	public BigDecimal getR20_TOTAL() {
		return R20_TOTAL;
	}
	public void setR20_TOTAL(BigDecimal r20_TOTAL) {
		R20_TOTAL = r20_TOTAL;
	}
	public BigDecimal getR21_1_DAY() {
		return R21_1_DAY;
	}
	public void setR21_1_DAY(BigDecimal r21_1_DAY) {
		R21_1_DAY = r21_1_DAY;
	}
	public BigDecimal getR21_2TO7_DAYS() {
		return R21_2TO7_DAYS;
	}
	public void setR21_2TO7_DAYS(BigDecimal r21_2to7_DAYS) {
		R21_2TO7_DAYS = r21_2to7_DAYS;
	}
	public BigDecimal getR21_8TO14_DAYS() {
		return R21_8TO14_DAYS;
	}
	public void setR21_8TO14_DAYS(BigDecimal r21_8to14_DAYS) {
		R21_8TO14_DAYS = r21_8to14_DAYS;
	}
	public BigDecimal getR21_15TO30_DAYS() {
		return R21_15TO30_DAYS;
	}
	public void setR21_15TO30_DAYS(BigDecimal r21_15to30_DAYS) {
		R21_15TO30_DAYS = r21_15to30_DAYS;
	}
	public BigDecimal getR21_31DAYS_UPTO_2MONTHS() {
		return R21_31DAYS_UPTO_2MONTHS;
	}
	public void setR21_31DAYS_UPTO_2MONTHS(BigDecimal r21_31days_UPTO_2MONTHS) {
		R21_31DAYS_UPTO_2MONTHS = r21_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR21_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R21_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR21_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r21_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R21_MORETHAN_2MONTHS_UPTO_3MONHTS = r21_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR21_OVER_3MONTHS_UPTO_6MONTHS() {
		return R21_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR21_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r21_OVER_3MONTHS_UPTO_6MONTHS) {
		R21_OVER_3MONTHS_UPTO_6MONTHS = r21_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR21_OVER_6MONTHS_UPTO_1YEAR() {
		return R21_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR21_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r21_OVER_6MONTHS_UPTO_1YEAR) {
		R21_OVER_6MONTHS_UPTO_1YEAR = r21_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR21_OVER_1YEAR_UPTO_3YEARS() {
		return R21_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR21_OVER_1YEAR_UPTO_3YEARS(BigDecimal r21_OVER_1YEAR_UPTO_3YEARS) {
		R21_OVER_1YEAR_UPTO_3YEARS = r21_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR21_OVER_3YEARS_UPTO_5YEARS() {
		return R21_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR21_OVER_3YEARS_UPTO_5YEARS(BigDecimal r21_OVER_3YEARS_UPTO_5YEARS) {
		R21_OVER_3YEARS_UPTO_5YEARS = r21_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR21_OVER_5YEARS() {
		return R21_OVER_5YEARS;
	}
	public void setR21_OVER_5YEARS(BigDecimal r21_OVER_5YEARS) {
		R21_OVER_5YEARS = r21_OVER_5YEARS;
	}
	public BigDecimal getR21_TOTAL() {
		return R21_TOTAL;
	}
	public void setR21_TOTAL(BigDecimal r21_TOTAL) {
		R21_TOTAL = r21_TOTAL;
	}
	public BigDecimal getR22_1_DAY() {
		return R22_1_DAY;
	}
	public void setR22_1_DAY(BigDecimal r22_1_DAY) {
		R22_1_DAY = r22_1_DAY;
	}
	public BigDecimal getR22_2TO7_DAYS() {
		return R22_2TO7_DAYS;
	}
	public void setR22_2TO7_DAYS(BigDecimal r22_2to7_DAYS) {
		R22_2TO7_DAYS = r22_2to7_DAYS;
	}
	public BigDecimal getR22_8TO14_DAYS() {
		return R22_8TO14_DAYS;
	}
	public void setR22_8TO14_DAYS(BigDecimal r22_8to14_DAYS) {
		R22_8TO14_DAYS = r22_8to14_DAYS;
	}
	public BigDecimal getR22_15TO30_DAYS() {
		return R22_15TO30_DAYS;
	}
	public void setR22_15TO30_DAYS(BigDecimal r22_15to30_DAYS) {
		R22_15TO30_DAYS = r22_15to30_DAYS;
	}
	public BigDecimal getR22_31DAYS_UPTO_2MONTHS() {
		return R22_31DAYS_UPTO_2MONTHS;
	}
	public void setR22_31DAYS_UPTO_2MONTHS(BigDecimal r22_31days_UPTO_2MONTHS) {
		R22_31DAYS_UPTO_2MONTHS = r22_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR22_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R22_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR22_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r22_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R22_MORETHAN_2MONTHS_UPTO_3MONHTS = r22_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR22_OVER_3MONTHS_UPTO_6MONTHS() {
		return R22_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR22_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r22_OVER_3MONTHS_UPTO_6MONTHS) {
		R22_OVER_3MONTHS_UPTO_6MONTHS = r22_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR22_OVER_6MONTHS_UPTO_1YEAR() {
		return R22_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR22_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r22_OVER_6MONTHS_UPTO_1YEAR) {
		R22_OVER_6MONTHS_UPTO_1YEAR = r22_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR22_OVER_1YEAR_UPTO_3YEARS() {
		return R22_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR22_OVER_1YEAR_UPTO_3YEARS(BigDecimal r22_OVER_1YEAR_UPTO_3YEARS) {
		R22_OVER_1YEAR_UPTO_3YEARS = r22_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR22_OVER_3YEARS_UPTO_5YEARS() {
		return R22_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR22_OVER_3YEARS_UPTO_5YEARS(BigDecimal r22_OVER_3YEARS_UPTO_5YEARS) {
		R22_OVER_3YEARS_UPTO_5YEARS = r22_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR22_OVER_5YEARS() {
		return R22_OVER_5YEARS;
	}
	public void setR22_OVER_5YEARS(BigDecimal r22_OVER_5YEARS) {
		R22_OVER_5YEARS = r22_OVER_5YEARS;
	}
	public BigDecimal getR22_TOTAL() {
		return R22_TOTAL;
	}
	public void setR22_TOTAL(BigDecimal r22_TOTAL) {
		R22_TOTAL = r22_TOTAL;
	}
	public BigDecimal getR23_1_DAY() {
		return R23_1_DAY;
	}
	public void setR23_1_DAY(BigDecimal r23_1_DAY) {
		R23_1_DAY = r23_1_DAY;
	}
	public BigDecimal getR23_2TO7_DAYS() {
		return R23_2TO7_DAYS;
	}
	public void setR23_2TO7_DAYS(BigDecimal r23_2to7_DAYS) {
		R23_2TO7_DAYS = r23_2to7_DAYS;
	}
	public BigDecimal getR23_8TO14_DAYS() {
		return R23_8TO14_DAYS;
	}
	public void setR23_8TO14_DAYS(BigDecimal r23_8to14_DAYS) {
		R23_8TO14_DAYS = r23_8to14_DAYS;
	}
	public BigDecimal getR23_15TO30_DAYS() {
		return R23_15TO30_DAYS;
	}
	public void setR23_15TO30_DAYS(BigDecimal r23_15to30_DAYS) {
		R23_15TO30_DAYS = r23_15to30_DAYS;
	}
	public BigDecimal getR23_31DAYS_UPTO_2MONTHS() {
		return R23_31DAYS_UPTO_2MONTHS;
	}
	public void setR23_31DAYS_UPTO_2MONTHS(BigDecimal r23_31days_UPTO_2MONTHS) {
		R23_31DAYS_UPTO_2MONTHS = r23_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR23_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R23_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR23_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r23_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R23_MORETHAN_2MONTHS_UPTO_3MONHTS = r23_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR23_OVER_3MONTHS_UPTO_6MONTHS() {
		return R23_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR23_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r23_OVER_3MONTHS_UPTO_6MONTHS) {
		R23_OVER_3MONTHS_UPTO_6MONTHS = r23_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR23_OVER_6MONTHS_UPTO_1YEAR() {
		return R23_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR23_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r23_OVER_6MONTHS_UPTO_1YEAR) {
		R23_OVER_6MONTHS_UPTO_1YEAR = r23_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR23_OVER_1YEAR_UPTO_3YEARS() {
		return R23_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR23_OVER_1YEAR_UPTO_3YEARS(BigDecimal r23_OVER_1YEAR_UPTO_3YEARS) {
		R23_OVER_1YEAR_UPTO_3YEARS = r23_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR23_OVER_3YEARS_UPTO_5YEARS() {
		return R23_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR23_OVER_3YEARS_UPTO_5YEARS(BigDecimal r23_OVER_3YEARS_UPTO_5YEARS) {
		R23_OVER_3YEARS_UPTO_5YEARS = r23_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR23_OVER_5YEARS() {
		return R23_OVER_5YEARS;
	}
	public void setR23_OVER_5YEARS(BigDecimal r23_OVER_5YEARS) {
		R23_OVER_5YEARS = r23_OVER_5YEARS;
	}
	public BigDecimal getR23_TOTAL() {
		return R23_TOTAL;
	}
	public void setR23_TOTAL(BigDecimal r23_TOTAL) {
		R23_TOTAL = r23_TOTAL;
	}
	public BigDecimal getR24_1_DAY() {
		return R24_1_DAY;
	}
	public void setR24_1_DAY(BigDecimal r24_1_DAY) {
		R24_1_DAY = r24_1_DAY;
	}
	public BigDecimal getR24_2TO7_DAYS() {
		return R24_2TO7_DAYS;
	}
	public void setR24_2TO7_DAYS(BigDecimal r24_2to7_DAYS) {
		R24_2TO7_DAYS = r24_2to7_DAYS;
	}
	public BigDecimal getR24_8TO14_DAYS() {
		return R24_8TO14_DAYS;
	}
	public void setR24_8TO14_DAYS(BigDecimal r24_8to14_DAYS) {
		R24_8TO14_DAYS = r24_8to14_DAYS;
	}
	public BigDecimal getR24_15TO30_DAYS() {
		return R24_15TO30_DAYS;
	}
	public void setR24_15TO30_DAYS(BigDecimal r24_15to30_DAYS) {
		R24_15TO30_DAYS = r24_15to30_DAYS;
	}
	public BigDecimal getR24_31DAYS_UPTO_2MONTHS() {
		return R24_31DAYS_UPTO_2MONTHS;
	}
	public void setR24_31DAYS_UPTO_2MONTHS(BigDecimal r24_31days_UPTO_2MONTHS) {
		R24_31DAYS_UPTO_2MONTHS = r24_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR24_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R24_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR24_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r24_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R24_MORETHAN_2MONTHS_UPTO_3MONHTS = r24_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR24_OVER_3MONTHS_UPTO_6MONTHS() {
		return R24_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR24_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r24_OVER_3MONTHS_UPTO_6MONTHS) {
		R24_OVER_3MONTHS_UPTO_6MONTHS = r24_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR24_OVER_6MONTHS_UPTO_1YEAR() {
		return R24_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR24_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r24_OVER_6MONTHS_UPTO_1YEAR) {
		R24_OVER_6MONTHS_UPTO_1YEAR = r24_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR24_OVER_1YEAR_UPTO_3YEARS() {
		return R24_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR24_OVER_1YEAR_UPTO_3YEARS(BigDecimal r24_OVER_1YEAR_UPTO_3YEARS) {
		R24_OVER_1YEAR_UPTO_3YEARS = r24_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR24_OVER_3YEARS_UPTO_5YEARS() {
		return R24_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR24_OVER_3YEARS_UPTO_5YEARS(BigDecimal r24_OVER_3YEARS_UPTO_5YEARS) {
		R24_OVER_3YEARS_UPTO_5YEARS = r24_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR24_OVER_5YEARS() {
		return R24_OVER_5YEARS;
	}
	public void setR24_OVER_5YEARS(BigDecimal r24_OVER_5YEARS) {
		R24_OVER_5YEARS = r24_OVER_5YEARS;
	}
	public BigDecimal getR24_TOTAL() {
		return R24_TOTAL;
	}
	public void setR24_TOTAL(BigDecimal r24_TOTAL) {
		R24_TOTAL = r24_TOTAL;
	}
	public BigDecimal getR25_1_DAY() {
		return R25_1_DAY;
	}
	public void setR25_1_DAY(BigDecimal r25_1_DAY) {
		R25_1_DAY = r25_1_DAY;
	}
	public BigDecimal getR25_2TO7_DAYS() {
		return R25_2TO7_DAYS;
	}
	public void setR25_2TO7_DAYS(BigDecimal r25_2to7_DAYS) {
		R25_2TO7_DAYS = r25_2to7_DAYS;
	}
	public BigDecimal getR25_8TO14_DAYS() {
		return R25_8TO14_DAYS;
	}
	public void setR25_8TO14_DAYS(BigDecimal r25_8to14_DAYS) {
		R25_8TO14_DAYS = r25_8to14_DAYS;
	}
	public BigDecimal getR25_15TO30_DAYS() {
		return R25_15TO30_DAYS;
	}
	public void setR25_15TO30_DAYS(BigDecimal r25_15to30_DAYS) {
		R25_15TO30_DAYS = r25_15to30_DAYS;
	}
	public BigDecimal getR25_31DAYS_UPTO_2MONTHS() {
		return R25_31DAYS_UPTO_2MONTHS;
	}
	public void setR25_31DAYS_UPTO_2MONTHS(BigDecimal r25_31days_UPTO_2MONTHS) {
		R25_31DAYS_UPTO_2MONTHS = r25_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR25_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R25_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR25_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r25_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R25_MORETHAN_2MONTHS_UPTO_3MONHTS = r25_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR25_OVER_3MONTHS_UPTO_6MONTHS() {
		return R25_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR25_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r25_OVER_3MONTHS_UPTO_6MONTHS) {
		R25_OVER_3MONTHS_UPTO_6MONTHS = r25_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR25_OVER_6MONTHS_UPTO_1YEAR() {
		return R25_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR25_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r25_OVER_6MONTHS_UPTO_1YEAR) {
		R25_OVER_6MONTHS_UPTO_1YEAR = r25_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR25_OVER_1YEAR_UPTO_3YEARS() {
		return R25_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR25_OVER_1YEAR_UPTO_3YEARS(BigDecimal r25_OVER_1YEAR_UPTO_3YEARS) {
		R25_OVER_1YEAR_UPTO_3YEARS = r25_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR25_OVER_3YEARS_UPTO_5YEARS() {
		return R25_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR25_OVER_3YEARS_UPTO_5YEARS(BigDecimal r25_OVER_3YEARS_UPTO_5YEARS) {
		R25_OVER_3YEARS_UPTO_5YEARS = r25_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR25_OVER_5YEARS() {
		return R25_OVER_5YEARS;
	}
	public void setR25_OVER_5YEARS(BigDecimal r25_OVER_5YEARS) {
		R25_OVER_5YEARS = r25_OVER_5YEARS;
	}
	public BigDecimal getR25_TOTAL() {
		return R25_TOTAL;
	}
	public void setR25_TOTAL(BigDecimal r25_TOTAL) {
		R25_TOTAL = r25_TOTAL;
	}
	public BigDecimal getR26_1_DAY() {
		return R26_1_DAY;
	}
	public void setR26_1_DAY(BigDecimal r26_1_DAY) {
		R26_1_DAY = r26_1_DAY;
	}
	public BigDecimal getR26_2TO7_DAYS() {
		return R26_2TO7_DAYS;
	}
	public void setR26_2TO7_DAYS(BigDecimal r26_2to7_DAYS) {
		R26_2TO7_DAYS = r26_2to7_DAYS;
	}
	public BigDecimal getR26_8TO14_DAYS() {
		return R26_8TO14_DAYS;
	}
	public void setR26_8TO14_DAYS(BigDecimal r26_8to14_DAYS) {
		R26_8TO14_DAYS = r26_8to14_DAYS;
	}
	public BigDecimal getR26_15TO30_DAYS() {
		return R26_15TO30_DAYS;
	}
	public void setR26_15TO30_DAYS(BigDecimal r26_15to30_DAYS) {
		R26_15TO30_DAYS = r26_15to30_DAYS;
	}
	public BigDecimal getR26_31DAYS_UPTO_2MONTHS() {
		return R26_31DAYS_UPTO_2MONTHS;
	}
	public void setR26_31DAYS_UPTO_2MONTHS(BigDecimal r26_31days_UPTO_2MONTHS) {
		R26_31DAYS_UPTO_2MONTHS = r26_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR26_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R26_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR26_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r26_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R26_MORETHAN_2MONTHS_UPTO_3MONHTS = r26_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR26_OVER_3MONTHS_UPTO_6MONTHS() {
		return R26_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR26_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r26_OVER_3MONTHS_UPTO_6MONTHS) {
		R26_OVER_3MONTHS_UPTO_6MONTHS = r26_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR26_OVER_6MONTHS_UPTO_1YEAR() {
		return R26_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR26_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r26_OVER_6MONTHS_UPTO_1YEAR) {
		R26_OVER_6MONTHS_UPTO_1YEAR = r26_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR26_OVER_1YEAR_UPTO_3YEARS() {
		return R26_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR26_OVER_1YEAR_UPTO_3YEARS(BigDecimal r26_OVER_1YEAR_UPTO_3YEARS) {
		R26_OVER_1YEAR_UPTO_3YEARS = r26_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR26_OVER_3YEARS_UPTO_5YEARS() {
		return R26_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR26_OVER_3YEARS_UPTO_5YEARS(BigDecimal r26_OVER_3YEARS_UPTO_5YEARS) {
		R26_OVER_3YEARS_UPTO_5YEARS = r26_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR26_OVER_5YEARS() {
		return R26_OVER_5YEARS;
	}
	public void setR26_OVER_5YEARS(BigDecimal r26_OVER_5YEARS) {
		R26_OVER_5YEARS = r26_OVER_5YEARS;
	}
	public BigDecimal getR26_TOTAL() {
		return R26_TOTAL;
	}
	public void setR26_TOTAL(BigDecimal r26_TOTAL) {
		R26_TOTAL = r26_TOTAL;
	}
	public BigDecimal getR27_1_DAY() {
		return R27_1_DAY;
	}
	public void setR27_1_DAY(BigDecimal r27_1_DAY) {
		R27_1_DAY = r27_1_DAY;
	}
	public BigDecimal getR27_2TO7_DAYS() {
		return R27_2TO7_DAYS;
	}
	public void setR27_2TO7_DAYS(BigDecimal r27_2to7_DAYS) {
		R27_2TO7_DAYS = r27_2to7_DAYS;
	}
	public BigDecimal getR27_8TO14_DAYS() {
		return R27_8TO14_DAYS;
	}
	public void setR27_8TO14_DAYS(BigDecimal r27_8to14_DAYS) {
		R27_8TO14_DAYS = r27_8to14_DAYS;
	}
	public BigDecimal getR27_15TO30_DAYS() {
		return R27_15TO30_DAYS;
	}
	public void setR27_15TO30_DAYS(BigDecimal r27_15to30_DAYS) {
		R27_15TO30_DAYS = r27_15to30_DAYS;
	}
	public BigDecimal getR27_31DAYS_UPTO_2MONTHS() {
		return R27_31DAYS_UPTO_2MONTHS;
	}
	public void setR27_31DAYS_UPTO_2MONTHS(BigDecimal r27_31days_UPTO_2MONTHS) {
		R27_31DAYS_UPTO_2MONTHS = r27_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR27_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R27_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR27_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r27_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R27_MORETHAN_2MONTHS_UPTO_3MONHTS = r27_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR27_OVER_3MONTHS_UPTO_6MONTHS() {
		return R27_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR27_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r27_OVER_3MONTHS_UPTO_6MONTHS) {
		R27_OVER_3MONTHS_UPTO_6MONTHS = r27_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR27_OVER_6MONTHS_UPTO_1YEAR() {
		return R27_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR27_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r27_OVER_6MONTHS_UPTO_1YEAR) {
		R27_OVER_6MONTHS_UPTO_1YEAR = r27_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR27_OVER_1YEAR_UPTO_3YEARS() {
		return R27_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR27_OVER_1YEAR_UPTO_3YEARS(BigDecimal r27_OVER_1YEAR_UPTO_3YEARS) {
		R27_OVER_1YEAR_UPTO_3YEARS = r27_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR27_OVER_3YEARS_UPTO_5YEARS() {
		return R27_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR27_OVER_3YEARS_UPTO_5YEARS(BigDecimal r27_OVER_3YEARS_UPTO_5YEARS) {
		R27_OVER_3YEARS_UPTO_5YEARS = r27_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR27_OVER_5YEARS() {
		return R27_OVER_5YEARS;
	}
	public void setR27_OVER_5YEARS(BigDecimal r27_OVER_5YEARS) {
		R27_OVER_5YEARS = r27_OVER_5YEARS;
	}
	public BigDecimal getR27_TOTAL() {
		return R27_TOTAL;
	}
	public void setR27_TOTAL(BigDecimal r27_TOTAL) {
		R27_TOTAL = r27_TOTAL;
	}
	public BigDecimal getR28_1_DAY() {
		return R28_1_DAY;
	}
	public void setR28_1_DAY(BigDecimal r28_1_DAY) {
		R28_1_DAY = r28_1_DAY;
	}
	public BigDecimal getR28_2TO7_DAYS() {
		return R28_2TO7_DAYS;
	}
	public void setR28_2TO7_DAYS(BigDecimal r28_2to7_DAYS) {
		R28_2TO7_DAYS = r28_2to7_DAYS;
	}
	public BigDecimal getR28_8TO14_DAYS() {
		return R28_8TO14_DAYS;
	}
	public void setR28_8TO14_DAYS(BigDecimal r28_8to14_DAYS) {
		R28_8TO14_DAYS = r28_8to14_DAYS;
	}
	public BigDecimal getR28_15TO30_DAYS() {
		return R28_15TO30_DAYS;
	}
	public void setR28_15TO30_DAYS(BigDecimal r28_15to30_DAYS) {
		R28_15TO30_DAYS = r28_15to30_DAYS;
	}
	public BigDecimal getR28_31DAYS_UPTO_2MONTHS() {
		return R28_31DAYS_UPTO_2MONTHS;
	}
	public void setR28_31DAYS_UPTO_2MONTHS(BigDecimal r28_31days_UPTO_2MONTHS) {
		R28_31DAYS_UPTO_2MONTHS = r28_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR28_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R28_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR28_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r28_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R28_MORETHAN_2MONTHS_UPTO_3MONHTS = r28_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR28_OVER_3MONTHS_UPTO_6MONTHS() {
		return R28_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR28_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r28_OVER_3MONTHS_UPTO_6MONTHS) {
		R28_OVER_3MONTHS_UPTO_6MONTHS = r28_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR28_OVER_6MONTHS_UPTO_1YEAR() {
		return R28_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR28_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r28_OVER_6MONTHS_UPTO_1YEAR) {
		R28_OVER_6MONTHS_UPTO_1YEAR = r28_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR28_OVER_1YEAR_UPTO_3YEARS() {
		return R28_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR28_OVER_1YEAR_UPTO_3YEARS(BigDecimal r28_OVER_1YEAR_UPTO_3YEARS) {
		R28_OVER_1YEAR_UPTO_3YEARS = r28_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR28_OVER_3YEARS_UPTO_5YEARS() {
		return R28_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR28_OVER_3YEARS_UPTO_5YEARS(BigDecimal r28_OVER_3YEARS_UPTO_5YEARS) {
		R28_OVER_3YEARS_UPTO_5YEARS = r28_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR28_OVER_5YEARS() {
		return R28_OVER_5YEARS;
	}
	public void setR28_OVER_5YEARS(BigDecimal r28_OVER_5YEARS) {
		R28_OVER_5YEARS = r28_OVER_5YEARS;
	}
	public BigDecimal getR28_TOTAL() {
		return R28_TOTAL;
	}
	public void setR28_TOTAL(BigDecimal r28_TOTAL) {
		R28_TOTAL = r28_TOTAL;
	}
	public BigDecimal getR29_1_DAY() {
		return R29_1_DAY;
	}
	public void setR29_1_DAY(BigDecimal r29_1_DAY) {
		R29_1_DAY = r29_1_DAY;
	}
	public BigDecimal getR29_2TO7_DAYS() {
		return R29_2TO7_DAYS;
	}
	public void setR29_2TO7_DAYS(BigDecimal r29_2to7_DAYS) {
		R29_2TO7_DAYS = r29_2to7_DAYS;
	}
	public BigDecimal getR29_8TO14_DAYS() {
		return R29_8TO14_DAYS;
	}
	public void setR29_8TO14_DAYS(BigDecimal r29_8to14_DAYS) {
		R29_8TO14_DAYS = r29_8to14_DAYS;
	}
	public BigDecimal getR29_15TO30_DAYS() {
		return R29_15TO30_DAYS;
	}
	public void setR29_15TO30_DAYS(BigDecimal r29_15to30_DAYS) {
		R29_15TO30_DAYS = r29_15to30_DAYS;
	}
	public BigDecimal getR29_31DAYS_UPTO_2MONTHS() {
		return R29_31DAYS_UPTO_2MONTHS;
	}
	public void setR29_31DAYS_UPTO_2MONTHS(BigDecimal r29_31days_UPTO_2MONTHS) {
		R29_31DAYS_UPTO_2MONTHS = r29_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR29_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R29_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR29_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r29_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R29_MORETHAN_2MONTHS_UPTO_3MONHTS = r29_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR29_OVER_3MONTHS_UPTO_6MONTHS() {
		return R29_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR29_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r29_OVER_3MONTHS_UPTO_6MONTHS) {
		R29_OVER_3MONTHS_UPTO_6MONTHS = r29_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR29_OVER_6MONTHS_UPTO_1YEAR() {
		return R29_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR29_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r29_OVER_6MONTHS_UPTO_1YEAR) {
		R29_OVER_6MONTHS_UPTO_1YEAR = r29_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR29_OVER_1YEAR_UPTO_3YEARS() {
		return R29_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR29_OVER_1YEAR_UPTO_3YEARS(BigDecimal r29_OVER_1YEAR_UPTO_3YEARS) {
		R29_OVER_1YEAR_UPTO_3YEARS = r29_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR29_OVER_3YEARS_UPTO_5YEARS() {
		return R29_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR29_OVER_3YEARS_UPTO_5YEARS(BigDecimal r29_OVER_3YEARS_UPTO_5YEARS) {
		R29_OVER_3YEARS_UPTO_5YEARS = r29_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR29_OVER_5YEARS() {
		return R29_OVER_5YEARS;
	}
	public void setR29_OVER_5YEARS(BigDecimal r29_OVER_5YEARS) {
		R29_OVER_5YEARS = r29_OVER_5YEARS;
	}
	public BigDecimal getR29_TOTAL() {
		return R29_TOTAL;
	}
	public void setR29_TOTAL(BigDecimal r29_TOTAL) {
		R29_TOTAL = r29_TOTAL;
	}
	public BigDecimal getR30_1_DAY() {
		return R30_1_DAY;
	}
	public void setR30_1_DAY(BigDecimal r30_1_DAY) {
		R30_1_DAY = r30_1_DAY;
	}
	public BigDecimal getR30_2TO7_DAYS() {
		return R30_2TO7_DAYS;
	}
	public void setR30_2TO7_DAYS(BigDecimal r30_2to7_DAYS) {
		R30_2TO7_DAYS = r30_2to7_DAYS;
	}
	public BigDecimal getR30_8TO14_DAYS() {
		return R30_8TO14_DAYS;
	}
	public void setR30_8TO14_DAYS(BigDecimal r30_8to14_DAYS) {
		R30_8TO14_DAYS = r30_8to14_DAYS;
	}
	public BigDecimal getR30_15TO30_DAYS() {
		return R30_15TO30_DAYS;
	}
	public void setR30_15TO30_DAYS(BigDecimal r30_15to30_DAYS) {
		R30_15TO30_DAYS = r30_15to30_DAYS;
	}
	public BigDecimal getR30_31DAYS_UPTO_2MONTHS() {
		return R30_31DAYS_UPTO_2MONTHS;
	}
	public void setR30_31DAYS_UPTO_2MONTHS(BigDecimal r30_31days_UPTO_2MONTHS) {
		R30_31DAYS_UPTO_2MONTHS = r30_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR30_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R30_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR30_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r30_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R30_MORETHAN_2MONTHS_UPTO_3MONHTS = r30_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR30_OVER_3MONTHS_UPTO_6MONTHS() {
		return R30_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR30_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r30_OVER_3MONTHS_UPTO_6MONTHS) {
		R30_OVER_3MONTHS_UPTO_6MONTHS = r30_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR30_OVER_6MONTHS_UPTO_1YEAR() {
		return R30_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR30_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r30_OVER_6MONTHS_UPTO_1YEAR) {
		R30_OVER_6MONTHS_UPTO_1YEAR = r30_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR30_OVER_1YEAR_UPTO_3YEARS() {
		return R30_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR30_OVER_1YEAR_UPTO_3YEARS(BigDecimal r30_OVER_1YEAR_UPTO_3YEARS) {
		R30_OVER_1YEAR_UPTO_3YEARS = r30_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR30_OVER_3YEARS_UPTO_5YEARS() {
		return R30_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR30_OVER_3YEARS_UPTO_5YEARS(BigDecimal r30_OVER_3YEARS_UPTO_5YEARS) {
		R30_OVER_3YEARS_UPTO_5YEARS = r30_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR30_OVER_5YEARS() {
		return R30_OVER_5YEARS;
	}
	public void setR30_OVER_5YEARS(BigDecimal r30_OVER_5YEARS) {
		R30_OVER_5YEARS = r30_OVER_5YEARS;
	}
	public BigDecimal getR30_TOTAL() {
		return R30_TOTAL;
	}
	public void setR30_TOTAL(BigDecimal r30_TOTAL) {
		R30_TOTAL = r30_TOTAL;
	}
	public BigDecimal getR31_1_DAY() {
		return R31_1_DAY;
	}
	public void setR31_1_DAY(BigDecimal r31_1_DAY) {
		R31_1_DAY = r31_1_DAY;
	}
	public BigDecimal getR31_2TO7_DAYS() {
		return R31_2TO7_DAYS;
	}
	public void setR31_2TO7_DAYS(BigDecimal r31_2to7_DAYS) {
		R31_2TO7_DAYS = r31_2to7_DAYS;
	}
	public BigDecimal getR31_8TO14_DAYS() {
		return R31_8TO14_DAYS;
	}
	public void setR31_8TO14_DAYS(BigDecimal r31_8to14_DAYS) {
		R31_8TO14_DAYS = r31_8to14_DAYS;
	}
	public BigDecimal getR31_15TO30_DAYS() {
		return R31_15TO30_DAYS;
	}
	public void setR31_15TO30_DAYS(BigDecimal r31_15to30_DAYS) {
		R31_15TO30_DAYS = r31_15to30_DAYS;
	}
	public BigDecimal getR31_31DAYS_UPTO_2MONTHS() {
		return R31_31DAYS_UPTO_2MONTHS;
	}
	public void setR31_31DAYS_UPTO_2MONTHS(BigDecimal r31_31days_UPTO_2MONTHS) {
		R31_31DAYS_UPTO_2MONTHS = r31_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR31_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R31_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR31_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r31_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R31_MORETHAN_2MONTHS_UPTO_3MONHTS = r31_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR31_OVER_3MONTHS_UPTO_6MONTHS() {
		return R31_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR31_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r31_OVER_3MONTHS_UPTO_6MONTHS) {
		R31_OVER_3MONTHS_UPTO_6MONTHS = r31_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR31_OVER_6MONTHS_UPTO_1YEAR() {
		return R31_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR31_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r31_OVER_6MONTHS_UPTO_1YEAR) {
		R31_OVER_6MONTHS_UPTO_1YEAR = r31_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR31_OVER_1YEAR_UPTO_3YEARS() {
		return R31_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR31_OVER_1YEAR_UPTO_3YEARS(BigDecimal r31_OVER_1YEAR_UPTO_3YEARS) {
		R31_OVER_1YEAR_UPTO_3YEARS = r31_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR31_OVER_3YEARS_UPTO_5YEARS() {
		return R31_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR31_OVER_3YEARS_UPTO_5YEARS(BigDecimal r31_OVER_3YEARS_UPTO_5YEARS) {
		R31_OVER_3YEARS_UPTO_5YEARS = r31_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR31_OVER_5YEARS() {
		return R31_OVER_5YEARS;
	}
	public void setR31_OVER_5YEARS(BigDecimal r31_OVER_5YEARS) {
		R31_OVER_5YEARS = r31_OVER_5YEARS;
	}
	public BigDecimal getR31_TOTAL() {
		return R31_TOTAL;
	}
	public void setR31_TOTAL(BigDecimal r31_TOTAL) {
		R31_TOTAL = r31_TOTAL;
	}
	public BigDecimal getR32_1_DAY() {
		return R32_1_DAY;
	}
	public void setR32_1_DAY(BigDecimal r32_1_DAY) {
		R32_1_DAY = r32_1_DAY;
	}
	public BigDecimal getR32_2TO7_DAYS() {
		return R32_2TO7_DAYS;
	}
	public void setR32_2TO7_DAYS(BigDecimal r32_2to7_DAYS) {
		R32_2TO7_DAYS = r32_2to7_DAYS;
	}
	public BigDecimal getR32_8TO14_DAYS() {
		return R32_8TO14_DAYS;
	}
	public void setR32_8TO14_DAYS(BigDecimal r32_8to14_DAYS) {
		R32_8TO14_DAYS = r32_8to14_DAYS;
	}
	public BigDecimal getR32_15TO30_DAYS() {
		return R32_15TO30_DAYS;
	}
	public void setR32_15TO30_DAYS(BigDecimal r32_15to30_DAYS) {
		R32_15TO30_DAYS = r32_15to30_DAYS;
	}
	public BigDecimal getR32_31DAYS_UPTO_2MONTHS() {
		return R32_31DAYS_UPTO_2MONTHS;
	}
	public void setR32_31DAYS_UPTO_2MONTHS(BigDecimal r32_31days_UPTO_2MONTHS) {
		R32_31DAYS_UPTO_2MONTHS = r32_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR32_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R32_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR32_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r32_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R32_MORETHAN_2MONTHS_UPTO_3MONHTS = r32_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR32_OVER_3MONTHS_UPTO_6MONTHS() {
		return R32_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR32_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r32_OVER_3MONTHS_UPTO_6MONTHS) {
		R32_OVER_3MONTHS_UPTO_6MONTHS = r32_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR32_OVER_6MONTHS_UPTO_1YEAR() {
		return R32_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR32_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r32_OVER_6MONTHS_UPTO_1YEAR) {
		R32_OVER_6MONTHS_UPTO_1YEAR = r32_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR32_OVER_1YEAR_UPTO_3YEARS() {
		return R32_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR32_OVER_1YEAR_UPTO_3YEARS(BigDecimal r32_OVER_1YEAR_UPTO_3YEARS) {
		R32_OVER_1YEAR_UPTO_3YEARS = r32_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR32_OVER_3YEARS_UPTO_5YEARS() {
		return R32_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR32_OVER_3YEARS_UPTO_5YEARS(BigDecimal r32_OVER_3YEARS_UPTO_5YEARS) {
		R32_OVER_3YEARS_UPTO_5YEARS = r32_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR32_OVER_5YEARS() {
		return R32_OVER_5YEARS;
	}
	public void setR32_OVER_5YEARS(BigDecimal r32_OVER_5YEARS) {
		R32_OVER_5YEARS = r32_OVER_5YEARS;
	}
	public BigDecimal getR32_TOTAL() {
		return R32_TOTAL;
	}
	public void setR32_TOTAL(BigDecimal r32_TOTAL) {
		R32_TOTAL = r32_TOTAL;
	}
	public BigDecimal getR33_1_DAY() {
		return R33_1_DAY;
	}
	public void setR33_1_DAY(BigDecimal r33_1_DAY) {
		R33_1_DAY = r33_1_DAY;
	}
	public BigDecimal getR33_2TO7_DAYS() {
		return R33_2TO7_DAYS;
	}
	public void setR33_2TO7_DAYS(BigDecimal r33_2to7_DAYS) {
		R33_2TO7_DAYS = r33_2to7_DAYS;
	}
	public BigDecimal getR33_8TO14_DAYS() {
		return R33_8TO14_DAYS;
	}
	public void setR33_8TO14_DAYS(BigDecimal r33_8to14_DAYS) {
		R33_8TO14_DAYS = r33_8to14_DAYS;
	}
	public BigDecimal getR33_15TO30_DAYS() {
		return R33_15TO30_DAYS;
	}
	public void setR33_15TO30_DAYS(BigDecimal r33_15to30_DAYS) {
		R33_15TO30_DAYS = r33_15to30_DAYS;
	}
	public BigDecimal getR33_31DAYS_UPTO_2MONTHS() {
		return R33_31DAYS_UPTO_2MONTHS;
	}
	public void setR33_31DAYS_UPTO_2MONTHS(BigDecimal r33_31days_UPTO_2MONTHS) {
		R33_31DAYS_UPTO_2MONTHS = r33_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR33_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R33_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR33_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r33_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R33_MORETHAN_2MONTHS_UPTO_3MONHTS = r33_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR33_OVER_3MONTHS_UPTO_6MONTHS() {
		return R33_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR33_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r33_OVER_3MONTHS_UPTO_6MONTHS) {
		R33_OVER_3MONTHS_UPTO_6MONTHS = r33_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR33_OVER_6MONTHS_UPTO_1YEAR() {
		return R33_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR33_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r33_OVER_6MONTHS_UPTO_1YEAR) {
		R33_OVER_6MONTHS_UPTO_1YEAR = r33_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR33_OVER_1YEAR_UPTO_3YEARS() {
		return R33_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR33_OVER_1YEAR_UPTO_3YEARS(BigDecimal r33_OVER_1YEAR_UPTO_3YEARS) {
		R33_OVER_1YEAR_UPTO_3YEARS = r33_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR33_OVER_3YEARS_UPTO_5YEARS() {
		return R33_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR33_OVER_3YEARS_UPTO_5YEARS(BigDecimal r33_OVER_3YEARS_UPTO_5YEARS) {
		R33_OVER_3YEARS_UPTO_5YEARS = r33_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR33_OVER_5YEARS() {
		return R33_OVER_5YEARS;
	}
	public void setR33_OVER_5YEARS(BigDecimal r33_OVER_5YEARS) {
		R33_OVER_5YEARS = r33_OVER_5YEARS;
	}
	public BigDecimal getR33_TOTAL() {
		return R33_TOTAL;
	}
	public void setR33_TOTAL(BigDecimal r33_TOTAL) {
		R33_TOTAL = r33_TOTAL;
	}
	public BigDecimal getR34_1_DAY() {
		return R34_1_DAY;
	}
	public void setR34_1_DAY(BigDecimal r34_1_DAY) {
		R34_1_DAY = r34_1_DAY;
	}
	public BigDecimal getR34_2TO7_DAYS() {
		return R34_2TO7_DAYS;
	}
	public void setR34_2TO7_DAYS(BigDecimal r34_2to7_DAYS) {
		R34_2TO7_DAYS = r34_2to7_DAYS;
	}
	public BigDecimal getR34_8TO14_DAYS() {
		return R34_8TO14_DAYS;
	}
	public void setR34_8TO14_DAYS(BigDecimal r34_8to14_DAYS) {
		R34_8TO14_DAYS = r34_8to14_DAYS;
	}
	public BigDecimal getR34_15TO30_DAYS() {
		return R34_15TO30_DAYS;
	}
	public void setR34_15TO30_DAYS(BigDecimal r34_15to30_DAYS) {
		R34_15TO30_DAYS = r34_15to30_DAYS;
	}
	public BigDecimal getR34_31DAYS_UPTO_2MONTHS() {
		return R34_31DAYS_UPTO_2MONTHS;
	}
	public void setR34_31DAYS_UPTO_2MONTHS(BigDecimal r34_31days_UPTO_2MONTHS) {
		R34_31DAYS_UPTO_2MONTHS = r34_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR34_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R34_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR34_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r34_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R34_MORETHAN_2MONTHS_UPTO_3MONHTS = r34_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR34_OVER_3MONTHS_UPTO_6MONTHS() {
		return R34_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR34_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r34_OVER_3MONTHS_UPTO_6MONTHS) {
		R34_OVER_3MONTHS_UPTO_6MONTHS = r34_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR34_OVER_6MONTHS_UPTO_1YEAR() {
		return R34_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR34_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r34_OVER_6MONTHS_UPTO_1YEAR) {
		R34_OVER_6MONTHS_UPTO_1YEAR = r34_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR34_OVER_1YEAR_UPTO_3YEARS() {
		return R34_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR34_OVER_1YEAR_UPTO_3YEARS(BigDecimal r34_OVER_1YEAR_UPTO_3YEARS) {
		R34_OVER_1YEAR_UPTO_3YEARS = r34_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR34_OVER_3YEARS_UPTO_5YEARS() {
		return R34_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR34_OVER_3YEARS_UPTO_5YEARS(BigDecimal r34_OVER_3YEARS_UPTO_5YEARS) {
		R34_OVER_3YEARS_UPTO_5YEARS = r34_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR34_OVER_5YEARS() {
		return R34_OVER_5YEARS;
	}
	public void setR34_OVER_5YEARS(BigDecimal r34_OVER_5YEARS) {
		R34_OVER_5YEARS = r34_OVER_5YEARS;
	}
	public BigDecimal getR34_TOTAL() {
		return R34_TOTAL;
	}
	public void setR34_TOTAL(BigDecimal r34_TOTAL) {
		R34_TOTAL = r34_TOTAL;
	}
	public BigDecimal getR35_1_DAY() {
		return R35_1_DAY;
	}
	public void setR35_1_DAY(BigDecimal r35_1_DAY) {
		R35_1_DAY = r35_1_DAY;
	}
	public BigDecimal getR35_2TO7_DAYS() {
		return R35_2TO7_DAYS;
	}
	public void setR35_2TO7_DAYS(BigDecimal r35_2to7_DAYS) {
		R35_2TO7_DAYS = r35_2to7_DAYS;
	}
	public BigDecimal getR35_8TO14_DAYS() {
		return R35_8TO14_DAYS;
	}
	public void setR35_8TO14_DAYS(BigDecimal r35_8to14_DAYS) {
		R35_8TO14_DAYS = r35_8to14_DAYS;
	}
	public BigDecimal getR35_15TO30_DAYS() {
		return R35_15TO30_DAYS;
	}
	public void setR35_15TO30_DAYS(BigDecimal r35_15to30_DAYS) {
		R35_15TO30_DAYS = r35_15to30_DAYS;
	}
	public BigDecimal getR35_31DAYS_UPTO_2MONTHS() {
		return R35_31DAYS_UPTO_2MONTHS;
	}
	public void setR35_31DAYS_UPTO_2MONTHS(BigDecimal r35_31days_UPTO_2MONTHS) {
		R35_31DAYS_UPTO_2MONTHS = r35_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR35_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R35_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR35_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r35_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R35_MORETHAN_2MONTHS_UPTO_3MONHTS = r35_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR35_OVER_3MONTHS_UPTO_6MONTHS() {
		return R35_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR35_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r35_OVER_3MONTHS_UPTO_6MONTHS) {
		R35_OVER_3MONTHS_UPTO_6MONTHS = r35_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR35_OVER_6MONTHS_UPTO_1YEAR() {
		return R35_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR35_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r35_OVER_6MONTHS_UPTO_1YEAR) {
		R35_OVER_6MONTHS_UPTO_1YEAR = r35_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR35_OVER_1YEAR_UPTO_3YEARS() {
		return R35_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR35_OVER_1YEAR_UPTO_3YEARS(BigDecimal r35_OVER_1YEAR_UPTO_3YEARS) {
		R35_OVER_1YEAR_UPTO_3YEARS = r35_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR35_OVER_3YEARS_UPTO_5YEARS() {
		return R35_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR35_OVER_3YEARS_UPTO_5YEARS(BigDecimal r35_OVER_3YEARS_UPTO_5YEARS) {
		R35_OVER_3YEARS_UPTO_5YEARS = r35_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR35_OVER_5YEARS() {
		return R35_OVER_5YEARS;
	}
	public void setR35_OVER_5YEARS(BigDecimal r35_OVER_5YEARS) {
		R35_OVER_5YEARS = r35_OVER_5YEARS;
	}
	public BigDecimal getR35_TOTAL() {
		return R35_TOTAL;
	}
	public void setR35_TOTAL(BigDecimal r35_TOTAL) {
		R35_TOTAL = r35_TOTAL;
	}
	public BigDecimal getR36_1_DAY() {
		return R36_1_DAY;
	}
	public void setR36_1_DAY(BigDecimal r36_1_DAY) {
		R36_1_DAY = r36_1_DAY;
	}
	public BigDecimal getR36_2TO7_DAYS() {
		return R36_2TO7_DAYS;
	}
	public void setR36_2TO7_DAYS(BigDecimal r36_2to7_DAYS) {
		R36_2TO7_DAYS = r36_2to7_DAYS;
	}
	public BigDecimal getR36_8TO14_DAYS() {
		return R36_8TO14_DAYS;
	}
	public void setR36_8TO14_DAYS(BigDecimal r36_8to14_DAYS) {
		R36_8TO14_DAYS = r36_8to14_DAYS;
	}
	public BigDecimal getR36_15TO30_DAYS() {
		return R36_15TO30_DAYS;
	}
	public void setR36_15TO30_DAYS(BigDecimal r36_15to30_DAYS) {
		R36_15TO30_DAYS = r36_15to30_DAYS;
	}
	public BigDecimal getR36_31DAYS_UPTO_2MONTHS() {
		return R36_31DAYS_UPTO_2MONTHS;
	}
	public void setR36_31DAYS_UPTO_2MONTHS(BigDecimal r36_31days_UPTO_2MONTHS) {
		R36_31DAYS_UPTO_2MONTHS = r36_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR36_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R36_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR36_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r36_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R36_MORETHAN_2MONTHS_UPTO_3MONHTS = r36_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR36_OVER_3MONTHS_UPTO_6MONTHS() {
		return R36_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR36_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r36_OVER_3MONTHS_UPTO_6MONTHS) {
		R36_OVER_3MONTHS_UPTO_6MONTHS = r36_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR36_OVER_6MONTHS_UPTO_1YEAR() {
		return R36_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR36_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r36_OVER_6MONTHS_UPTO_1YEAR) {
		R36_OVER_6MONTHS_UPTO_1YEAR = r36_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR36_OVER_1YEAR_UPTO_3YEARS() {
		return R36_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR36_OVER_1YEAR_UPTO_3YEARS(BigDecimal r36_OVER_1YEAR_UPTO_3YEARS) {
		R36_OVER_1YEAR_UPTO_3YEARS = r36_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR36_OVER_3YEARS_UPTO_5YEARS() {
		return R36_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR36_OVER_3YEARS_UPTO_5YEARS(BigDecimal r36_OVER_3YEARS_UPTO_5YEARS) {
		R36_OVER_3YEARS_UPTO_5YEARS = r36_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR36_OVER_5YEARS() {
		return R36_OVER_5YEARS;
	}
	public void setR36_OVER_5YEARS(BigDecimal r36_OVER_5YEARS) {
		R36_OVER_5YEARS = r36_OVER_5YEARS;
	}
	public BigDecimal getR36_TOTAL() {
		return R36_TOTAL;
	}
	public void setR36_TOTAL(BigDecimal r36_TOTAL) {
		R36_TOTAL = r36_TOTAL;
	}
	public BigDecimal getR37_1_DAY() {
		return R37_1_DAY;
	}
	public void setR37_1_DAY(BigDecimal r37_1_DAY) {
		R37_1_DAY = r37_1_DAY;
	}
	public BigDecimal getR37_2TO7_DAYS() {
		return R37_2TO7_DAYS;
	}
	public void setR37_2TO7_DAYS(BigDecimal r37_2to7_DAYS) {
		R37_2TO7_DAYS = r37_2to7_DAYS;
	}
	public BigDecimal getR37_8TO14_DAYS() {
		return R37_8TO14_DAYS;
	}
	public void setR37_8TO14_DAYS(BigDecimal r37_8to14_DAYS) {
		R37_8TO14_DAYS = r37_8to14_DAYS;
	}
	public BigDecimal getR37_15TO30_DAYS() {
		return R37_15TO30_DAYS;
	}
	public void setR37_15TO30_DAYS(BigDecimal r37_15to30_DAYS) {
		R37_15TO30_DAYS = r37_15to30_DAYS;
	}
	public BigDecimal getR37_31DAYS_UPTO_2MONTHS() {
		return R37_31DAYS_UPTO_2MONTHS;
	}
	public void setR37_31DAYS_UPTO_2MONTHS(BigDecimal r37_31days_UPTO_2MONTHS) {
		R37_31DAYS_UPTO_2MONTHS = r37_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR37_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R37_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR37_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r37_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R37_MORETHAN_2MONTHS_UPTO_3MONHTS = r37_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR37_OVER_3MONTHS_UPTO_6MONTHS() {
		return R37_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR37_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r37_OVER_3MONTHS_UPTO_6MONTHS) {
		R37_OVER_3MONTHS_UPTO_6MONTHS = r37_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR37_OVER_6MONTHS_UPTO_1YEAR() {
		return R37_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR37_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r37_OVER_6MONTHS_UPTO_1YEAR) {
		R37_OVER_6MONTHS_UPTO_1YEAR = r37_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR37_OVER_1YEAR_UPTO_3YEARS() {
		return R37_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR37_OVER_1YEAR_UPTO_3YEARS(BigDecimal r37_OVER_1YEAR_UPTO_3YEARS) {
		R37_OVER_1YEAR_UPTO_3YEARS = r37_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR37_OVER_3YEARS_UPTO_5YEARS() {
		return R37_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR37_OVER_3YEARS_UPTO_5YEARS(BigDecimal r37_OVER_3YEARS_UPTO_5YEARS) {
		R37_OVER_3YEARS_UPTO_5YEARS = r37_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR37_OVER_5YEARS() {
		return R37_OVER_5YEARS;
	}
	public void setR37_OVER_5YEARS(BigDecimal r37_OVER_5YEARS) {
		R37_OVER_5YEARS = r37_OVER_5YEARS;
	}
	public BigDecimal getR37_TOTAL() {
		return R37_TOTAL;
	}
	public void setR37_TOTAL(BigDecimal r37_TOTAL) {
		R37_TOTAL = r37_TOTAL;
	}
	public BigDecimal getR38_1_DAY() {
		return R38_1_DAY;
	}
	public void setR38_1_DAY(BigDecimal r38_1_DAY) {
		R38_1_DAY = r38_1_DAY;
	}
	public BigDecimal getR38_2TO7_DAYS() {
		return R38_2TO7_DAYS;
	}
	public void setR38_2TO7_DAYS(BigDecimal r38_2to7_DAYS) {
		R38_2TO7_DAYS = r38_2to7_DAYS;
	}
	public BigDecimal getR38_8TO14_DAYS() {
		return R38_8TO14_DAYS;
	}
	public void setR38_8TO14_DAYS(BigDecimal r38_8to14_DAYS) {
		R38_8TO14_DAYS = r38_8to14_DAYS;
	}
	public BigDecimal getR38_15TO30_DAYS() {
		return R38_15TO30_DAYS;
	}
	public void setR38_15TO30_DAYS(BigDecimal r38_15to30_DAYS) {
		R38_15TO30_DAYS = r38_15to30_DAYS;
	}
	public BigDecimal getR38_31DAYS_UPTO_2MONTHS() {
		return R38_31DAYS_UPTO_2MONTHS;
	}
	public void setR38_31DAYS_UPTO_2MONTHS(BigDecimal r38_31days_UPTO_2MONTHS) {
		R38_31DAYS_UPTO_2MONTHS = r38_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR38_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R38_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR38_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r38_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R38_MORETHAN_2MONTHS_UPTO_3MONHTS = r38_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR38_OVER_3MONTHS_UPTO_6MONTHS() {
		return R38_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR38_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r38_OVER_3MONTHS_UPTO_6MONTHS) {
		R38_OVER_3MONTHS_UPTO_6MONTHS = r38_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR38_OVER_6MONTHS_UPTO_1YEAR() {
		return R38_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR38_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r38_OVER_6MONTHS_UPTO_1YEAR) {
		R38_OVER_6MONTHS_UPTO_1YEAR = r38_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR38_OVER_1YEAR_UPTO_3YEARS() {
		return R38_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR38_OVER_1YEAR_UPTO_3YEARS(BigDecimal r38_OVER_1YEAR_UPTO_3YEARS) {
		R38_OVER_1YEAR_UPTO_3YEARS = r38_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR38_OVER_3YEARS_UPTO_5YEARS() {
		return R38_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR38_OVER_3YEARS_UPTO_5YEARS(BigDecimal r38_OVER_3YEARS_UPTO_5YEARS) {
		R38_OVER_3YEARS_UPTO_5YEARS = r38_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR38_OVER_5YEARS() {
		return R38_OVER_5YEARS;
	}
	public void setR38_OVER_5YEARS(BigDecimal r38_OVER_5YEARS) {
		R38_OVER_5YEARS = r38_OVER_5YEARS;
	}
	public BigDecimal getR38_TOTAL() {
		return R38_TOTAL;
	}
	public void setR38_TOTAL(BigDecimal r38_TOTAL) {
		R38_TOTAL = r38_TOTAL;
	}
	public BigDecimal getR39_1_DAY() {
		return R39_1_DAY;
	}
	public void setR39_1_DAY(BigDecimal r39_1_DAY) {
		R39_1_DAY = r39_1_DAY;
	}
	public BigDecimal getR39_2TO7_DAYS() {
		return R39_2TO7_DAYS;
	}
	public void setR39_2TO7_DAYS(BigDecimal r39_2to7_DAYS) {
		R39_2TO7_DAYS = r39_2to7_DAYS;
	}
	public BigDecimal getR39_8TO14_DAYS() {
		return R39_8TO14_DAYS;
	}
	public void setR39_8TO14_DAYS(BigDecimal r39_8to14_DAYS) {
		R39_8TO14_DAYS = r39_8to14_DAYS;
	}
	public BigDecimal getR39_15TO30_DAYS() {
		return R39_15TO30_DAYS;
	}
	public void setR39_15TO30_DAYS(BigDecimal r39_15to30_DAYS) {
		R39_15TO30_DAYS = r39_15to30_DAYS;
	}
	public BigDecimal getR39_31DAYS_UPTO_2MONTHS() {
		return R39_31DAYS_UPTO_2MONTHS;
	}
	public void setR39_31DAYS_UPTO_2MONTHS(BigDecimal r39_31days_UPTO_2MONTHS) {
		R39_31DAYS_UPTO_2MONTHS = r39_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR39_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R39_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR39_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r39_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R39_MORETHAN_2MONTHS_UPTO_3MONHTS = r39_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR39_OVER_3MONTHS_UPTO_6MONTHS() {
		return R39_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR39_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r39_OVER_3MONTHS_UPTO_6MONTHS) {
		R39_OVER_3MONTHS_UPTO_6MONTHS = r39_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR39_OVER_6MONTHS_UPTO_1YEAR() {
		return R39_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR39_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r39_OVER_6MONTHS_UPTO_1YEAR) {
		R39_OVER_6MONTHS_UPTO_1YEAR = r39_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR39_OVER_1YEAR_UPTO_3YEARS() {
		return R39_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR39_OVER_1YEAR_UPTO_3YEARS(BigDecimal r39_OVER_1YEAR_UPTO_3YEARS) {
		R39_OVER_1YEAR_UPTO_3YEARS = r39_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR39_OVER_3YEARS_UPTO_5YEARS() {
		return R39_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR39_OVER_3YEARS_UPTO_5YEARS(BigDecimal r39_OVER_3YEARS_UPTO_5YEARS) {
		R39_OVER_3YEARS_UPTO_5YEARS = r39_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR39_OVER_5YEARS() {
		return R39_OVER_5YEARS;
	}
	public void setR39_OVER_5YEARS(BigDecimal r39_OVER_5YEARS) {
		R39_OVER_5YEARS = r39_OVER_5YEARS;
	}
	public BigDecimal getR39_TOTAL() {
		return R39_TOTAL;
	}
	public void setR39_TOTAL(BigDecimal r39_TOTAL) {
		R39_TOTAL = r39_TOTAL;
	}
	public BigDecimal getR40_1_DAY() {
		return R40_1_DAY;
	}
	public void setR40_1_DAY(BigDecimal r40_1_DAY) {
		R40_1_DAY = r40_1_DAY;
	}
	public BigDecimal getR40_2TO7_DAYS() {
		return R40_2TO7_DAYS;
	}
	public void setR40_2TO7_DAYS(BigDecimal r40_2to7_DAYS) {
		R40_2TO7_DAYS = r40_2to7_DAYS;
	}
	public BigDecimal getR40_8TO14_DAYS() {
		return R40_8TO14_DAYS;
	}
	public void setR40_8TO14_DAYS(BigDecimal r40_8to14_DAYS) {
		R40_8TO14_DAYS = r40_8to14_DAYS;
	}
	public BigDecimal getR40_15TO30_DAYS() {
		return R40_15TO30_DAYS;
	}
	public void setR40_15TO30_DAYS(BigDecimal r40_15to30_DAYS) {
		R40_15TO30_DAYS = r40_15to30_DAYS;
	}
	public BigDecimal getR40_31DAYS_UPTO_2MONTHS() {
		return R40_31DAYS_UPTO_2MONTHS;
	}
	public void setR40_31DAYS_UPTO_2MONTHS(BigDecimal r40_31days_UPTO_2MONTHS) {
		R40_31DAYS_UPTO_2MONTHS = r40_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR40_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R40_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR40_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r40_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R40_MORETHAN_2MONTHS_UPTO_3MONHTS = r40_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR40_OVER_3MONTHS_UPTO_6MONTHS() {
		return R40_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR40_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r40_OVER_3MONTHS_UPTO_6MONTHS) {
		R40_OVER_3MONTHS_UPTO_6MONTHS = r40_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR40_OVER_6MONTHS_UPTO_1YEAR() {
		return R40_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR40_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r40_OVER_6MONTHS_UPTO_1YEAR) {
		R40_OVER_6MONTHS_UPTO_1YEAR = r40_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR40_OVER_1YEAR_UPTO_3YEARS() {
		return R40_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR40_OVER_1YEAR_UPTO_3YEARS(BigDecimal r40_OVER_1YEAR_UPTO_3YEARS) {
		R40_OVER_1YEAR_UPTO_3YEARS = r40_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR40_OVER_3YEARS_UPTO_5YEARS() {
		return R40_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR40_OVER_3YEARS_UPTO_5YEARS(BigDecimal r40_OVER_3YEARS_UPTO_5YEARS) {
		R40_OVER_3YEARS_UPTO_5YEARS = r40_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR40_OVER_5YEARS() {
		return R40_OVER_5YEARS;
	}
	public void setR40_OVER_5YEARS(BigDecimal r40_OVER_5YEARS) {
		R40_OVER_5YEARS = r40_OVER_5YEARS;
	}
	public BigDecimal getR40_TOTAL() {
		return R40_TOTAL;
	}
	public void setR40_TOTAL(BigDecimal r40_TOTAL) {
		R40_TOTAL = r40_TOTAL;
	}
	public BigDecimal getR41_1_DAY() {
		return R41_1_DAY;
	}
	public void setR41_1_DAY(BigDecimal r41_1_DAY) {
		R41_1_DAY = r41_1_DAY;
	}
	public BigDecimal getR41_2TO7_DAYS() {
		return R41_2TO7_DAYS;
	}
	public void setR41_2TO7_DAYS(BigDecimal r41_2to7_DAYS) {
		R41_2TO7_DAYS = r41_2to7_DAYS;
	}
	public BigDecimal getR41_8TO14_DAYS() {
		return R41_8TO14_DAYS;
	}
	public void setR41_8TO14_DAYS(BigDecimal r41_8to14_DAYS) {
		R41_8TO14_DAYS = r41_8to14_DAYS;
	}
	public BigDecimal getR41_15TO30_DAYS() {
		return R41_15TO30_DAYS;
	}
	public void setR41_15TO30_DAYS(BigDecimal r41_15to30_DAYS) {
		R41_15TO30_DAYS = r41_15to30_DAYS;
	}
	public BigDecimal getR41_31DAYS_UPTO_2MONTHS() {
		return R41_31DAYS_UPTO_2MONTHS;
	}
	public void setR41_31DAYS_UPTO_2MONTHS(BigDecimal r41_31days_UPTO_2MONTHS) {
		R41_31DAYS_UPTO_2MONTHS = r41_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR41_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R41_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR41_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r41_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R41_MORETHAN_2MONTHS_UPTO_3MONHTS = r41_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR41_OVER_3MONTHS_UPTO_6MONTHS() {
		return R41_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR41_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r41_OVER_3MONTHS_UPTO_6MONTHS) {
		R41_OVER_3MONTHS_UPTO_6MONTHS = r41_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR41_OVER_6MONTHS_UPTO_1YEAR() {
		return R41_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR41_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r41_OVER_6MONTHS_UPTO_1YEAR) {
		R41_OVER_6MONTHS_UPTO_1YEAR = r41_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR41_OVER_1YEAR_UPTO_3YEARS() {
		return R41_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR41_OVER_1YEAR_UPTO_3YEARS(BigDecimal r41_OVER_1YEAR_UPTO_3YEARS) {
		R41_OVER_1YEAR_UPTO_3YEARS = r41_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR41_OVER_3YEARS_UPTO_5YEARS() {
		return R41_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR41_OVER_3YEARS_UPTO_5YEARS(BigDecimal r41_OVER_3YEARS_UPTO_5YEARS) {
		R41_OVER_3YEARS_UPTO_5YEARS = r41_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR41_OVER_5YEARS() {
		return R41_OVER_5YEARS;
	}
	public void setR41_OVER_5YEARS(BigDecimal r41_OVER_5YEARS) {
		R41_OVER_5YEARS = r41_OVER_5YEARS;
	}
	public BigDecimal getR41_TOTAL() {
		return R41_TOTAL;
	}
	public void setR41_TOTAL(BigDecimal r41_TOTAL) {
		R41_TOTAL = r41_TOTAL;
	}
	public BigDecimal getR42_1_DAY() {
		return R42_1_DAY;
	}
	public void setR42_1_DAY(BigDecimal r42_1_DAY) {
		R42_1_DAY = r42_1_DAY;
	}
	public BigDecimal getR42_2TO7_DAYS() {
		return R42_2TO7_DAYS;
	}
	public void setR42_2TO7_DAYS(BigDecimal r42_2to7_DAYS) {
		R42_2TO7_DAYS = r42_2to7_DAYS;
	}
	public BigDecimal getR42_8TO14_DAYS() {
		return R42_8TO14_DAYS;
	}
	public void setR42_8TO14_DAYS(BigDecimal r42_8to14_DAYS) {
		R42_8TO14_DAYS = r42_8to14_DAYS;
	}
	public BigDecimal getR42_15TO30_DAYS() {
		return R42_15TO30_DAYS;
	}
	public void setR42_15TO30_DAYS(BigDecimal r42_15to30_DAYS) {
		R42_15TO30_DAYS = r42_15to30_DAYS;
	}
	public BigDecimal getR42_31DAYS_UPTO_2MONTHS() {
		return R42_31DAYS_UPTO_2MONTHS;
	}
	public void setR42_31DAYS_UPTO_2MONTHS(BigDecimal r42_31days_UPTO_2MONTHS) {
		R42_31DAYS_UPTO_2MONTHS = r42_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR42_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R42_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR42_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r42_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R42_MORETHAN_2MONTHS_UPTO_3MONHTS = r42_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR42_OVER_3MONTHS_UPTO_6MONTHS() {
		return R42_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR42_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r42_OVER_3MONTHS_UPTO_6MONTHS) {
		R42_OVER_3MONTHS_UPTO_6MONTHS = r42_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR42_OVER_6MONTHS_UPTO_1YEAR() {
		return R42_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR42_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r42_OVER_6MONTHS_UPTO_1YEAR) {
		R42_OVER_6MONTHS_UPTO_1YEAR = r42_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR42_OVER_1YEAR_UPTO_3YEARS() {
		return R42_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR42_OVER_1YEAR_UPTO_3YEARS(BigDecimal r42_OVER_1YEAR_UPTO_3YEARS) {
		R42_OVER_1YEAR_UPTO_3YEARS = r42_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR42_OVER_3YEARS_UPTO_5YEARS() {
		return R42_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR42_OVER_3YEARS_UPTO_5YEARS(BigDecimal r42_OVER_3YEARS_UPTO_5YEARS) {
		R42_OVER_3YEARS_UPTO_5YEARS = r42_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR42_OVER_5YEARS() {
		return R42_OVER_5YEARS;
	}
	public void setR42_OVER_5YEARS(BigDecimal r42_OVER_5YEARS) {
		R42_OVER_5YEARS = r42_OVER_5YEARS;
	}
	public BigDecimal getR42_TOTAL() {
		return R42_TOTAL;
	}
	public void setR42_TOTAL(BigDecimal r42_TOTAL) {
		R42_TOTAL = r42_TOTAL;
	}
	public BigDecimal getR43_1_DAY() {
		return R43_1_DAY;
	}
	public void setR43_1_DAY(BigDecimal r43_1_DAY) {
		R43_1_DAY = r43_1_DAY;
	}
	public BigDecimal getR43_2TO7_DAYS() {
		return R43_2TO7_DAYS;
	}
	public void setR43_2TO7_DAYS(BigDecimal r43_2to7_DAYS) {
		R43_2TO7_DAYS = r43_2to7_DAYS;
	}
	public BigDecimal getR43_8TO14_DAYS() {
		return R43_8TO14_DAYS;
	}
	public void setR43_8TO14_DAYS(BigDecimal r43_8to14_DAYS) {
		R43_8TO14_DAYS = r43_8to14_DAYS;
	}
	public BigDecimal getR43_15TO30_DAYS() {
		return R43_15TO30_DAYS;
	}
	public void setR43_15TO30_DAYS(BigDecimal r43_15to30_DAYS) {
		R43_15TO30_DAYS = r43_15to30_DAYS;
	}
	public BigDecimal getR43_31DAYS_UPTO_2MONTHS() {
		return R43_31DAYS_UPTO_2MONTHS;
	}
	public void setR43_31DAYS_UPTO_2MONTHS(BigDecimal r43_31days_UPTO_2MONTHS) {
		R43_31DAYS_UPTO_2MONTHS = r43_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR43_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R43_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR43_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r43_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R43_MORETHAN_2MONTHS_UPTO_3MONHTS = r43_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR43_OVER_3MONTHS_UPTO_6MONTHS() {
		return R43_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR43_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r43_OVER_3MONTHS_UPTO_6MONTHS) {
		R43_OVER_3MONTHS_UPTO_6MONTHS = r43_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR43_OVER_6MONTHS_UPTO_1YEAR() {
		return R43_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR43_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r43_OVER_6MONTHS_UPTO_1YEAR) {
		R43_OVER_6MONTHS_UPTO_1YEAR = r43_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR43_OVER_1YEAR_UPTO_3YEARS() {
		return R43_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR43_OVER_1YEAR_UPTO_3YEARS(BigDecimal r43_OVER_1YEAR_UPTO_3YEARS) {
		R43_OVER_1YEAR_UPTO_3YEARS = r43_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR43_OVER_3YEARS_UPTO_5YEARS() {
		return R43_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR43_OVER_3YEARS_UPTO_5YEARS(BigDecimal r43_OVER_3YEARS_UPTO_5YEARS) {
		R43_OVER_3YEARS_UPTO_5YEARS = r43_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR43_OVER_5YEARS() {
		return R43_OVER_5YEARS;
	}
	public void setR43_OVER_5YEARS(BigDecimal r43_OVER_5YEARS) {
		R43_OVER_5YEARS = r43_OVER_5YEARS;
	}
	public BigDecimal getR43_TOTAL() {
		return R43_TOTAL;
	}
	public void setR43_TOTAL(BigDecimal r43_TOTAL) {
		R43_TOTAL = r43_TOTAL;
	}
	public BigDecimal getR44_1_DAY() {
		return R44_1_DAY;
	}
	public void setR44_1_DAY(BigDecimal r44_1_DAY) {
		R44_1_DAY = r44_1_DAY;
	}
	public BigDecimal getR44_2TO7_DAYS() {
		return R44_2TO7_DAYS;
	}
	public void setR44_2TO7_DAYS(BigDecimal r44_2to7_DAYS) {
		R44_2TO7_DAYS = r44_2to7_DAYS;
	}
	public BigDecimal getR44_8TO14_DAYS() {
		return R44_8TO14_DAYS;
	}
	public void setR44_8TO14_DAYS(BigDecimal r44_8to14_DAYS) {
		R44_8TO14_DAYS = r44_8to14_DAYS;
	}
	public BigDecimal getR44_15TO30_DAYS() {
		return R44_15TO30_DAYS;
	}
	public void setR44_15TO30_DAYS(BigDecimal r44_15to30_DAYS) {
		R44_15TO30_DAYS = r44_15to30_DAYS;
	}
	public BigDecimal getR44_31DAYS_UPTO_2MONTHS() {
		return R44_31DAYS_UPTO_2MONTHS;
	}
	public void setR44_31DAYS_UPTO_2MONTHS(BigDecimal r44_31days_UPTO_2MONTHS) {
		R44_31DAYS_UPTO_2MONTHS = r44_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR44_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R44_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR44_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r44_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R44_MORETHAN_2MONTHS_UPTO_3MONHTS = r44_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR44_OVER_3MONTHS_UPTO_6MONTHS() {
		return R44_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR44_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r44_OVER_3MONTHS_UPTO_6MONTHS) {
		R44_OVER_3MONTHS_UPTO_6MONTHS = r44_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR44_OVER_6MONTHS_UPTO_1YEAR() {
		return R44_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR44_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r44_OVER_6MONTHS_UPTO_1YEAR) {
		R44_OVER_6MONTHS_UPTO_1YEAR = r44_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR44_OVER_1YEAR_UPTO_3YEARS() {
		return R44_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR44_OVER_1YEAR_UPTO_3YEARS(BigDecimal r44_OVER_1YEAR_UPTO_3YEARS) {
		R44_OVER_1YEAR_UPTO_3YEARS = r44_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR44_OVER_3YEARS_UPTO_5YEARS() {
		return R44_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR44_OVER_3YEARS_UPTO_5YEARS(BigDecimal r44_OVER_3YEARS_UPTO_5YEARS) {
		R44_OVER_3YEARS_UPTO_5YEARS = r44_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR44_OVER_5YEARS() {
		return R44_OVER_5YEARS;
	}
	public void setR44_OVER_5YEARS(BigDecimal r44_OVER_5YEARS) {
		R44_OVER_5YEARS = r44_OVER_5YEARS;
	}
	public BigDecimal getR44_TOTAL() {
		return R44_TOTAL;
	}
	public void setR44_TOTAL(BigDecimal r44_TOTAL) {
		R44_TOTAL = r44_TOTAL;
	}
	public BigDecimal getR45_1_DAY() {
		return R45_1_DAY;
	}
	public void setR45_1_DAY(BigDecimal r45_1_DAY) {
		R45_1_DAY = r45_1_DAY;
	}
	public BigDecimal getR45_2TO7_DAYS() {
		return R45_2TO7_DAYS;
	}
	public void setR45_2TO7_DAYS(BigDecimal r45_2to7_DAYS) {
		R45_2TO7_DAYS = r45_2to7_DAYS;
	}
	public BigDecimal getR45_8TO14_DAYS() {
		return R45_8TO14_DAYS;
	}
	public void setR45_8TO14_DAYS(BigDecimal r45_8to14_DAYS) {
		R45_8TO14_DAYS = r45_8to14_DAYS;
	}
	public BigDecimal getR45_15TO30_DAYS() {
		return R45_15TO30_DAYS;
	}
	public void setR45_15TO30_DAYS(BigDecimal r45_15to30_DAYS) {
		R45_15TO30_DAYS = r45_15to30_DAYS;
	}
	public BigDecimal getR45_31DAYS_UPTO_2MONTHS() {
		return R45_31DAYS_UPTO_2MONTHS;
	}
	public void setR45_31DAYS_UPTO_2MONTHS(BigDecimal r45_31days_UPTO_2MONTHS) {
		R45_31DAYS_UPTO_2MONTHS = r45_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR45_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R45_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR45_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r45_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R45_MORETHAN_2MONTHS_UPTO_3MONHTS = r45_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR45_OVER_3MONTHS_UPTO_6MONTHS() {
		return R45_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR45_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r45_OVER_3MONTHS_UPTO_6MONTHS) {
		R45_OVER_3MONTHS_UPTO_6MONTHS = r45_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR45_OVER_6MONTHS_UPTO_1YEAR() {
		return R45_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR45_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r45_OVER_6MONTHS_UPTO_1YEAR) {
		R45_OVER_6MONTHS_UPTO_1YEAR = r45_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR45_OVER_1YEAR_UPTO_3YEARS() {
		return R45_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR45_OVER_1YEAR_UPTO_3YEARS(BigDecimal r45_OVER_1YEAR_UPTO_3YEARS) {
		R45_OVER_1YEAR_UPTO_3YEARS = r45_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR45_OVER_3YEARS_UPTO_5YEARS() {
		return R45_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR45_OVER_3YEARS_UPTO_5YEARS(BigDecimal r45_OVER_3YEARS_UPTO_5YEARS) {
		R45_OVER_3YEARS_UPTO_5YEARS = r45_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR45_OVER_5YEARS() {
		return R45_OVER_5YEARS;
	}
	public void setR45_OVER_5YEARS(BigDecimal r45_OVER_5YEARS) {
		R45_OVER_5YEARS = r45_OVER_5YEARS;
	}
	public BigDecimal getR45_TOTAL() {
		return R45_TOTAL;
	}
	public void setR45_TOTAL(BigDecimal r45_TOTAL) {
		R45_TOTAL = r45_TOTAL;
	}
	public BigDecimal getR46_1_DAY() {
		return R46_1_DAY;
	}
	public void setR46_1_DAY(BigDecimal r46_1_DAY) {
		R46_1_DAY = r46_1_DAY;
	}
	public BigDecimal getR46_2TO7_DAYS() {
		return R46_2TO7_DAYS;
	}
	public void setR46_2TO7_DAYS(BigDecimal r46_2to7_DAYS) {
		R46_2TO7_DAYS = r46_2to7_DAYS;
	}
	public BigDecimal getR46_8TO14_DAYS() {
		return R46_8TO14_DAYS;
	}
	public void setR46_8TO14_DAYS(BigDecimal r46_8to14_DAYS) {
		R46_8TO14_DAYS = r46_8to14_DAYS;
	}
	public BigDecimal getR46_15TO30_DAYS() {
		return R46_15TO30_DAYS;
	}
	public void setR46_15TO30_DAYS(BigDecimal r46_15to30_DAYS) {
		R46_15TO30_DAYS = r46_15to30_DAYS;
	}
	public BigDecimal getR46_31DAYS_UPTO_2MONTHS() {
		return R46_31DAYS_UPTO_2MONTHS;
	}
	public void setR46_31DAYS_UPTO_2MONTHS(BigDecimal r46_31days_UPTO_2MONTHS) {
		R46_31DAYS_UPTO_2MONTHS = r46_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR46_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R46_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR46_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r46_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R46_MORETHAN_2MONTHS_UPTO_3MONHTS = r46_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR46_OVER_3MONTHS_UPTO_6MONTHS() {
		return R46_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR46_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r46_OVER_3MONTHS_UPTO_6MONTHS) {
		R46_OVER_3MONTHS_UPTO_6MONTHS = r46_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR46_OVER_6MONTHS_UPTO_1YEAR() {
		return R46_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR46_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r46_OVER_6MONTHS_UPTO_1YEAR) {
		R46_OVER_6MONTHS_UPTO_1YEAR = r46_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR46_OVER_1YEAR_UPTO_3YEARS() {
		return R46_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR46_OVER_1YEAR_UPTO_3YEARS(BigDecimal r46_OVER_1YEAR_UPTO_3YEARS) {
		R46_OVER_1YEAR_UPTO_3YEARS = r46_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR46_OVER_3YEARS_UPTO_5YEARS() {
		return R46_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR46_OVER_3YEARS_UPTO_5YEARS(BigDecimal r46_OVER_3YEARS_UPTO_5YEARS) {
		R46_OVER_3YEARS_UPTO_5YEARS = r46_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR46_OVER_5YEARS() {
		return R46_OVER_5YEARS;
	}
	public void setR46_OVER_5YEARS(BigDecimal r46_OVER_5YEARS) {
		R46_OVER_5YEARS = r46_OVER_5YEARS;
	}
	public BigDecimal getR46_TOTAL() {
		return R46_TOTAL;
	}
	public void setR46_TOTAL(BigDecimal r46_TOTAL) {
		R46_TOTAL = r46_TOTAL;
	}
	public BigDecimal getR47_1_DAY() {
		return R47_1_DAY;
	}
	public void setR47_1_DAY(BigDecimal r47_1_DAY) {
		R47_1_DAY = r47_1_DAY;
	}
	public BigDecimal getR47_2TO7_DAYS() {
		return R47_2TO7_DAYS;
	}
	public void setR47_2TO7_DAYS(BigDecimal r47_2to7_DAYS) {
		R47_2TO7_DAYS = r47_2to7_DAYS;
	}
	public BigDecimal getR47_8TO14_DAYS() {
		return R47_8TO14_DAYS;
	}
	public void setR47_8TO14_DAYS(BigDecimal r47_8to14_DAYS) {
		R47_8TO14_DAYS = r47_8to14_DAYS;
	}
	public BigDecimal getR47_15TO30_DAYS() {
		return R47_15TO30_DAYS;
	}
	public void setR47_15TO30_DAYS(BigDecimal r47_15to30_DAYS) {
		R47_15TO30_DAYS = r47_15to30_DAYS;
	}
	public BigDecimal getR47_31DAYS_UPTO_2MONTHS() {
		return R47_31DAYS_UPTO_2MONTHS;
	}
	public void setR47_31DAYS_UPTO_2MONTHS(BigDecimal r47_31days_UPTO_2MONTHS) {
		R47_31DAYS_UPTO_2MONTHS = r47_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR47_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R47_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR47_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r47_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R47_MORETHAN_2MONTHS_UPTO_3MONHTS = r47_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR47_OVER_3MONTHS_UPTO_6MONTHS() {
		return R47_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR47_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r47_OVER_3MONTHS_UPTO_6MONTHS) {
		R47_OVER_3MONTHS_UPTO_6MONTHS = r47_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR47_OVER_6MONTHS_UPTO_1YEAR() {
		return R47_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR47_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r47_OVER_6MONTHS_UPTO_1YEAR) {
		R47_OVER_6MONTHS_UPTO_1YEAR = r47_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR47_OVER_1YEAR_UPTO_3YEARS() {
		return R47_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR47_OVER_1YEAR_UPTO_3YEARS(BigDecimal r47_OVER_1YEAR_UPTO_3YEARS) {
		R47_OVER_1YEAR_UPTO_3YEARS = r47_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR47_OVER_3YEARS_UPTO_5YEARS() {
		return R47_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR47_OVER_3YEARS_UPTO_5YEARS(BigDecimal r47_OVER_3YEARS_UPTO_5YEARS) {
		R47_OVER_3YEARS_UPTO_5YEARS = r47_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR47_OVER_5YEARS() {
		return R47_OVER_5YEARS;
	}
	public void setR47_OVER_5YEARS(BigDecimal r47_OVER_5YEARS) {
		R47_OVER_5YEARS = r47_OVER_5YEARS;
	}
	public BigDecimal getR47_TOTAL() {
		return R47_TOTAL;
	}
	public void setR47_TOTAL(BigDecimal r47_TOTAL) {
		R47_TOTAL = r47_TOTAL;
	}
	public BigDecimal getR48_1_DAY() {
		return R48_1_DAY;
	}
	public void setR48_1_DAY(BigDecimal r48_1_DAY) {
		R48_1_DAY = r48_1_DAY;
	}
	public BigDecimal getR48_2TO7_DAYS() {
		return R48_2TO7_DAYS;
	}
	public void setR48_2TO7_DAYS(BigDecimal r48_2to7_DAYS) {
		R48_2TO7_DAYS = r48_2to7_DAYS;
	}
	public BigDecimal getR48_8TO14_DAYS() {
		return R48_8TO14_DAYS;
	}
	public void setR48_8TO14_DAYS(BigDecimal r48_8to14_DAYS) {
		R48_8TO14_DAYS = r48_8to14_DAYS;
	}
	public BigDecimal getR48_15TO30_DAYS() {
		return R48_15TO30_DAYS;
	}
	public void setR48_15TO30_DAYS(BigDecimal r48_15to30_DAYS) {
		R48_15TO30_DAYS = r48_15to30_DAYS;
	}
	public BigDecimal getR48_31DAYS_UPTO_2MONTHS() {
		return R48_31DAYS_UPTO_2MONTHS;
	}
	public void setR48_31DAYS_UPTO_2MONTHS(BigDecimal r48_31days_UPTO_2MONTHS) {
		R48_31DAYS_UPTO_2MONTHS = r48_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR48_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R48_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR48_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r48_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R48_MORETHAN_2MONTHS_UPTO_3MONHTS = r48_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR48_OVER_3MONTHS_UPTO_6MONTHS() {
		return R48_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR48_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r48_OVER_3MONTHS_UPTO_6MONTHS) {
		R48_OVER_3MONTHS_UPTO_6MONTHS = r48_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR48_OVER_6MONTHS_UPTO_1YEAR() {
		return R48_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR48_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r48_OVER_6MONTHS_UPTO_1YEAR) {
		R48_OVER_6MONTHS_UPTO_1YEAR = r48_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR48_OVER_1YEAR_UPTO_3YEARS() {
		return R48_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR48_OVER_1YEAR_UPTO_3YEARS(BigDecimal r48_OVER_1YEAR_UPTO_3YEARS) {
		R48_OVER_1YEAR_UPTO_3YEARS = r48_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR48_OVER_3YEARS_UPTO_5YEARS() {
		return R48_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR48_OVER_3YEARS_UPTO_5YEARS(BigDecimal r48_OVER_3YEARS_UPTO_5YEARS) {
		R48_OVER_3YEARS_UPTO_5YEARS = r48_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR48_OVER_5YEARS() {
		return R48_OVER_5YEARS;
	}
	public void setR48_OVER_5YEARS(BigDecimal r48_OVER_5YEARS) {
		R48_OVER_5YEARS = r48_OVER_5YEARS;
	}
	public BigDecimal getR48_TOTAL() {
		return R48_TOTAL;
	}
	public void setR48_TOTAL(BigDecimal r48_TOTAL) {
		R48_TOTAL = r48_TOTAL;
	}
	public BigDecimal getR49_1_DAY() {
		return R49_1_DAY;
	}
	public void setR49_1_DAY(BigDecimal r49_1_DAY) {
		R49_1_DAY = r49_1_DAY;
	}
	public BigDecimal getR49_2TO7_DAYS() {
		return R49_2TO7_DAYS;
	}
	public void setR49_2TO7_DAYS(BigDecimal r49_2to7_DAYS) {
		R49_2TO7_DAYS = r49_2to7_DAYS;
	}
	public BigDecimal getR49_8TO14_DAYS() {
		return R49_8TO14_DAYS;
	}
	public void setR49_8TO14_DAYS(BigDecimal r49_8to14_DAYS) {
		R49_8TO14_DAYS = r49_8to14_DAYS;
	}
	public BigDecimal getR49_15TO30_DAYS() {
		return R49_15TO30_DAYS;
	}
	public void setR49_15TO30_DAYS(BigDecimal r49_15to30_DAYS) {
		R49_15TO30_DAYS = r49_15to30_DAYS;
	}
	public BigDecimal getR49_31DAYS_UPTO_2MONTHS() {
		return R49_31DAYS_UPTO_2MONTHS;
	}
	public void setR49_31DAYS_UPTO_2MONTHS(BigDecimal r49_31days_UPTO_2MONTHS) {
		R49_31DAYS_UPTO_2MONTHS = r49_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR49_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R49_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR49_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r49_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R49_MORETHAN_2MONTHS_UPTO_3MONHTS = r49_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR49_OVER_3MONTHS_UPTO_6MONTHS() {
		return R49_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR49_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r49_OVER_3MONTHS_UPTO_6MONTHS) {
		R49_OVER_3MONTHS_UPTO_6MONTHS = r49_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR49_OVER_6MONTHS_UPTO_1YEAR() {
		return R49_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR49_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r49_OVER_6MONTHS_UPTO_1YEAR) {
		R49_OVER_6MONTHS_UPTO_1YEAR = r49_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR49_OVER_1YEAR_UPTO_3YEARS() {
		return R49_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR49_OVER_1YEAR_UPTO_3YEARS(BigDecimal r49_OVER_1YEAR_UPTO_3YEARS) {
		R49_OVER_1YEAR_UPTO_3YEARS = r49_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR49_OVER_3YEARS_UPTO_5YEARS() {
		return R49_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR49_OVER_3YEARS_UPTO_5YEARS(BigDecimal r49_OVER_3YEARS_UPTO_5YEARS) {
		R49_OVER_3YEARS_UPTO_5YEARS = r49_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR49_OVER_5YEARS() {
		return R49_OVER_5YEARS;
	}
	public void setR49_OVER_5YEARS(BigDecimal r49_OVER_5YEARS) {
		R49_OVER_5YEARS = r49_OVER_5YEARS;
	}
	public BigDecimal getR49_TOTAL() {
		return R49_TOTAL;
	}
	public void setR49_TOTAL(BigDecimal r49_TOTAL) {
		R49_TOTAL = r49_TOTAL;
	}
	public BigDecimal getR50_1_DAY() {
		return R50_1_DAY;
	}
	public void setR50_1_DAY(BigDecimal r50_1_DAY) {
		R50_1_DAY = r50_1_DAY;
	}
	public BigDecimal getR50_2TO7_DAYS() {
		return R50_2TO7_DAYS;
	}
	public void setR50_2TO7_DAYS(BigDecimal r50_2to7_DAYS) {
		R50_2TO7_DAYS = r50_2to7_DAYS;
	}
	public BigDecimal getR50_8TO14_DAYS() {
		return R50_8TO14_DAYS;
	}
	public void setR50_8TO14_DAYS(BigDecimal r50_8to14_DAYS) {
		R50_8TO14_DAYS = r50_8to14_DAYS;
	}
	public BigDecimal getR50_15TO30_DAYS() {
		return R50_15TO30_DAYS;
	}
	public void setR50_15TO30_DAYS(BigDecimal r50_15to30_DAYS) {
		R50_15TO30_DAYS = r50_15to30_DAYS;
	}
	public BigDecimal getR50_31DAYS_UPTO_2MONTHS() {
		return R50_31DAYS_UPTO_2MONTHS;
	}
	public void setR50_31DAYS_UPTO_2MONTHS(BigDecimal r50_31days_UPTO_2MONTHS) {
		R50_31DAYS_UPTO_2MONTHS = r50_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR50_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R50_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR50_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r50_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R50_MORETHAN_2MONTHS_UPTO_3MONHTS = r50_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR50_OVER_3MONTHS_UPTO_6MONTHS() {
		return R50_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR50_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r50_OVER_3MONTHS_UPTO_6MONTHS) {
		R50_OVER_3MONTHS_UPTO_6MONTHS = r50_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR50_OVER_6MONTHS_UPTO_1YEAR() {
		return R50_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR50_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r50_OVER_6MONTHS_UPTO_1YEAR) {
		R50_OVER_6MONTHS_UPTO_1YEAR = r50_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR50_OVER_1YEAR_UPTO_3YEARS() {
		return R50_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR50_OVER_1YEAR_UPTO_3YEARS(BigDecimal r50_OVER_1YEAR_UPTO_3YEARS) {
		R50_OVER_1YEAR_UPTO_3YEARS = r50_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR50_OVER_3YEARS_UPTO_5YEARS() {
		return R50_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR50_OVER_3YEARS_UPTO_5YEARS(BigDecimal r50_OVER_3YEARS_UPTO_5YEARS) {
		R50_OVER_3YEARS_UPTO_5YEARS = r50_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR50_OVER_5YEARS() {
		return R50_OVER_5YEARS;
	}
	public void setR50_OVER_5YEARS(BigDecimal r50_OVER_5YEARS) {
		R50_OVER_5YEARS = r50_OVER_5YEARS;
	}
	public BigDecimal getR50_TOTAL() {
		return R50_TOTAL;
	}
	public void setR50_TOTAL(BigDecimal r50_TOTAL) {
		R50_TOTAL = r50_TOTAL;
	}
	public BigDecimal getR51_1_DAY() {
		return R51_1_DAY;
	}
	public void setR51_1_DAY(BigDecimal r51_1_DAY) {
		R51_1_DAY = r51_1_DAY;
	}
	public BigDecimal getR51_2TO7_DAYS() {
		return R51_2TO7_DAYS;
	}
	public void setR51_2TO7_DAYS(BigDecimal r51_2to7_DAYS) {
		R51_2TO7_DAYS = r51_2to7_DAYS;
	}
	public BigDecimal getR51_8TO14_DAYS() {
		return R51_8TO14_DAYS;
	}
	public void setR51_8TO14_DAYS(BigDecimal r51_8to14_DAYS) {
		R51_8TO14_DAYS = r51_8to14_DAYS;
	}
	public BigDecimal getR51_15TO30_DAYS() {
		return R51_15TO30_DAYS;
	}
	public void setR51_15TO30_DAYS(BigDecimal r51_15to30_DAYS) {
		R51_15TO30_DAYS = r51_15to30_DAYS;
	}
	public BigDecimal getR51_31DAYS_UPTO_2MONTHS() {
		return R51_31DAYS_UPTO_2MONTHS;
	}
	public void setR51_31DAYS_UPTO_2MONTHS(BigDecimal r51_31days_UPTO_2MONTHS) {
		R51_31DAYS_UPTO_2MONTHS = r51_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR51_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R51_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR51_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r51_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R51_MORETHAN_2MONTHS_UPTO_3MONHTS = r51_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR51_OVER_3MONTHS_UPTO_6MONTHS() {
		return R51_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR51_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r51_OVER_3MONTHS_UPTO_6MONTHS) {
		R51_OVER_3MONTHS_UPTO_6MONTHS = r51_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR51_OVER_6MONTHS_UPTO_1YEAR() {
		return R51_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR51_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r51_OVER_6MONTHS_UPTO_1YEAR) {
		R51_OVER_6MONTHS_UPTO_1YEAR = r51_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR51_OVER_1YEAR_UPTO_3YEARS() {
		return R51_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR51_OVER_1YEAR_UPTO_3YEARS(BigDecimal r51_OVER_1YEAR_UPTO_3YEARS) {
		R51_OVER_1YEAR_UPTO_3YEARS = r51_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR51_OVER_3YEARS_UPTO_5YEARS() {
		return R51_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR51_OVER_3YEARS_UPTO_5YEARS(BigDecimal r51_OVER_3YEARS_UPTO_5YEARS) {
		R51_OVER_3YEARS_UPTO_5YEARS = r51_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR51_OVER_5YEARS() {
		return R51_OVER_5YEARS;
	}
	public void setR51_OVER_5YEARS(BigDecimal r51_OVER_5YEARS) {
		R51_OVER_5YEARS = r51_OVER_5YEARS;
	}
	public BigDecimal getR51_TOTAL() {
		return R51_TOTAL;
	}
	public void setR51_TOTAL(BigDecimal r51_TOTAL) {
		R51_TOTAL = r51_TOTAL;
	}
	public BigDecimal getR52_1_DAY() {
		return R52_1_DAY;
	}
	public void setR52_1_DAY(BigDecimal r52_1_DAY) {
		R52_1_DAY = r52_1_DAY;
	}
	public BigDecimal getR52_2TO7_DAYS() {
		return R52_2TO7_DAYS;
	}
	public void setR52_2TO7_DAYS(BigDecimal r52_2to7_DAYS) {
		R52_2TO7_DAYS = r52_2to7_DAYS;
	}
	public BigDecimal getR52_8TO14_DAYS() {
		return R52_8TO14_DAYS;
	}
	public void setR52_8TO14_DAYS(BigDecimal r52_8to14_DAYS) {
		R52_8TO14_DAYS = r52_8to14_DAYS;
	}
	public BigDecimal getR52_15TO30_DAYS() {
		return R52_15TO30_DAYS;
	}
	public void setR52_15TO30_DAYS(BigDecimal r52_15to30_DAYS) {
		R52_15TO30_DAYS = r52_15to30_DAYS;
	}
	public BigDecimal getR52_31DAYS_UPTO_2MONTHS() {
		return R52_31DAYS_UPTO_2MONTHS;
	}
	public void setR52_31DAYS_UPTO_2MONTHS(BigDecimal r52_31days_UPTO_2MONTHS) {
		R52_31DAYS_UPTO_2MONTHS = r52_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR52_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R52_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR52_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r52_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R52_MORETHAN_2MONTHS_UPTO_3MONHTS = r52_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR52_OVER_3MONTHS_UPTO_6MONTHS() {
		return R52_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR52_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r52_OVER_3MONTHS_UPTO_6MONTHS) {
		R52_OVER_3MONTHS_UPTO_6MONTHS = r52_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR52_OVER_6MONTHS_UPTO_1YEAR() {
		return R52_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR52_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r52_OVER_6MONTHS_UPTO_1YEAR) {
		R52_OVER_6MONTHS_UPTO_1YEAR = r52_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR52_OVER_1YEAR_UPTO_3YEARS() {
		return R52_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR52_OVER_1YEAR_UPTO_3YEARS(BigDecimal r52_OVER_1YEAR_UPTO_3YEARS) {
		R52_OVER_1YEAR_UPTO_3YEARS = r52_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR52_OVER_3YEARS_UPTO_5YEARS() {
		return R52_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR52_OVER_3YEARS_UPTO_5YEARS(BigDecimal r52_OVER_3YEARS_UPTO_5YEARS) {
		R52_OVER_3YEARS_UPTO_5YEARS = r52_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR52_OVER_5YEARS() {
		return R52_OVER_5YEARS;
	}
	public void setR52_OVER_5YEARS(BigDecimal r52_OVER_5YEARS) {
		R52_OVER_5YEARS = r52_OVER_5YEARS;
	}
	public BigDecimal getR52_TOTAL() {
		return R52_TOTAL;
	}
	public void setR52_TOTAL(BigDecimal r52_TOTAL) {
		R52_TOTAL = r52_TOTAL;
	}
	public BigDecimal getR53_1_DAY() {
		return R53_1_DAY;
	}
	public void setR53_1_DAY(BigDecimal r53_1_DAY) {
		R53_1_DAY = r53_1_DAY;
	}
	public BigDecimal getR53_2TO7_DAYS() {
		return R53_2TO7_DAYS;
	}
	public void setR53_2TO7_DAYS(BigDecimal r53_2to7_DAYS) {
		R53_2TO7_DAYS = r53_2to7_DAYS;
	}
	public BigDecimal getR53_8TO14_DAYS() {
		return R53_8TO14_DAYS;
	}
	public void setR53_8TO14_DAYS(BigDecimal r53_8to14_DAYS) {
		R53_8TO14_DAYS = r53_8to14_DAYS;
	}
	public BigDecimal getR53_15TO30_DAYS() {
		return R53_15TO30_DAYS;
	}
	public void setR53_15TO30_DAYS(BigDecimal r53_15to30_DAYS) {
		R53_15TO30_DAYS = r53_15to30_DAYS;
	}
	public BigDecimal getR53_31DAYS_UPTO_2MONTHS() {
		return R53_31DAYS_UPTO_2MONTHS;
	}
	public void setR53_31DAYS_UPTO_2MONTHS(BigDecimal r53_31days_UPTO_2MONTHS) {
		R53_31DAYS_UPTO_2MONTHS = r53_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR53_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R53_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR53_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r53_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R53_MORETHAN_2MONTHS_UPTO_3MONHTS = r53_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR53_OVER_3MONTHS_UPTO_6MONTHS() {
		return R53_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR53_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r53_OVER_3MONTHS_UPTO_6MONTHS) {
		R53_OVER_3MONTHS_UPTO_6MONTHS = r53_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR53_OVER_6MONTHS_UPTO_1YEAR() {
		return R53_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR53_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r53_OVER_6MONTHS_UPTO_1YEAR) {
		R53_OVER_6MONTHS_UPTO_1YEAR = r53_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR53_OVER_1YEAR_UPTO_3YEARS() {
		return R53_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR53_OVER_1YEAR_UPTO_3YEARS(BigDecimal r53_OVER_1YEAR_UPTO_3YEARS) {
		R53_OVER_1YEAR_UPTO_3YEARS = r53_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR53_OVER_3YEARS_UPTO_5YEARS() {
		return R53_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR53_OVER_3YEARS_UPTO_5YEARS(BigDecimal r53_OVER_3YEARS_UPTO_5YEARS) {
		R53_OVER_3YEARS_UPTO_5YEARS = r53_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR53_OVER_5YEARS() {
		return R53_OVER_5YEARS;
	}
	public void setR53_OVER_5YEARS(BigDecimal r53_OVER_5YEARS) {
		R53_OVER_5YEARS = r53_OVER_5YEARS;
	}
	public BigDecimal getR53_TOTAL() {
		return R53_TOTAL;
	}
	public void setR53_TOTAL(BigDecimal r53_TOTAL) {
		R53_TOTAL = r53_TOTAL;
	}
	public BigDecimal getR54_1_DAY() {
		return R54_1_DAY;
	}
	public void setR54_1_DAY(BigDecimal r54_1_DAY) {
		R54_1_DAY = r54_1_DAY;
	}
	public BigDecimal getR54_2TO7_DAYS() {
		return R54_2TO7_DAYS;
	}
	public void setR54_2TO7_DAYS(BigDecimal r54_2to7_DAYS) {
		R54_2TO7_DAYS = r54_2to7_DAYS;
	}
	public BigDecimal getR54_8TO14_DAYS() {
		return R54_8TO14_DAYS;
	}
	public void setR54_8TO14_DAYS(BigDecimal r54_8to14_DAYS) {
		R54_8TO14_DAYS = r54_8to14_DAYS;
	}
	public BigDecimal getR54_15TO30_DAYS() {
		return R54_15TO30_DAYS;
	}
	public void setR54_15TO30_DAYS(BigDecimal r54_15to30_DAYS) {
		R54_15TO30_DAYS = r54_15to30_DAYS;
	}
	public BigDecimal getR54_31DAYS_UPTO_2MONTHS() {
		return R54_31DAYS_UPTO_2MONTHS;
	}
	public void setR54_31DAYS_UPTO_2MONTHS(BigDecimal r54_31days_UPTO_2MONTHS) {
		R54_31DAYS_UPTO_2MONTHS = r54_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR54_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R54_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR54_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r54_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R54_MORETHAN_2MONTHS_UPTO_3MONHTS = r54_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR54_OVER_3MONTHS_UPTO_6MONTHS() {
		return R54_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR54_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r54_OVER_3MONTHS_UPTO_6MONTHS) {
		R54_OVER_3MONTHS_UPTO_6MONTHS = r54_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR54_OVER_6MONTHS_UPTO_1YEAR() {
		return R54_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR54_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r54_OVER_6MONTHS_UPTO_1YEAR) {
		R54_OVER_6MONTHS_UPTO_1YEAR = r54_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR54_OVER_1YEAR_UPTO_3YEARS() {
		return R54_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR54_OVER_1YEAR_UPTO_3YEARS(BigDecimal r54_OVER_1YEAR_UPTO_3YEARS) {
		R54_OVER_1YEAR_UPTO_3YEARS = r54_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR54_OVER_3YEARS_UPTO_5YEARS() {
		return R54_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR54_OVER_3YEARS_UPTO_5YEARS(BigDecimal r54_OVER_3YEARS_UPTO_5YEARS) {
		R54_OVER_3YEARS_UPTO_5YEARS = r54_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR54_OVER_5YEARS() {
		return R54_OVER_5YEARS;
	}
	public void setR54_OVER_5YEARS(BigDecimal r54_OVER_5YEARS) {
		R54_OVER_5YEARS = r54_OVER_5YEARS;
	}
	public BigDecimal getR54_TOTAL() {
		return R54_TOTAL;
	}
	public void setR54_TOTAL(BigDecimal r54_TOTAL) {
		R54_TOTAL = r54_TOTAL;
	}
	public BigDecimal getR55_1_DAY() {
		return R55_1_DAY;
	}
	public void setR55_1_DAY(BigDecimal r55_1_DAY) {
		R55_1_DAY = r55_1_DAY;
	}
	public BigDecimal getR55_2TO7_DAYS() {
		return R55_2TO7_DAYS;
	}
	public void setR55_2TO7_DAYS(BigDecimal r55_2to7_DAYS) {
		R55_2TO7_DAYS = r55_2to7_DAYS;
	}
	public BigDecimal getR55_8TO14_DAYS() {
		return R55_8TO14_DAYS;
	}
	public void setR55_8TO14_DAYS(BigDecimal r55_8to14_DAYS) {
		R55_8TO14_DAYS = r55_8to14_DAYS;
	}
	public BigDecimal getR55_15TO30_DAYS() {
		return R55_15TO30_DAYS;
	}
	public void setR55_15TO30_DAYS(BigDecimal r55_15to30_DAYS) {
		R55_15TO30_DAYS = r55_15to30_DAYS;
	}
	public BigDecimal getR55_31DAYS_UPTO_2MONTHS() {
		return R55_31DAYS_UPTO_2MONTHS;
	}
	public void setR55_31DAYS_UPTO_2MONTHS(BigDecimal r55_31days_UPTO_2MONTHS) {
		R55_31DAYS_UPTO_2MONTHS = r55_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR55_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R55_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR55_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r55_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R55_MORETHAN_2MONTHS_UPTO_3MONHTS = r55_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR55_OVER_3MONTHS_UPTO_6MONTHS() {
		return R55_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR55_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r55_OVER_3MONTHS_UPTO_6MONTHS) {
		R55_OVER_3MONTHS_UPTO_6MONTHS = r55_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR55_OVER_6MONTHS_UPTO_1YEAR() {
		return R55_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR55_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r55_OVER_6MONTHS_UPTO_1YEAR) {
		R55_OVER_6MONTHS_UPTO_1YEAR = r55_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR55_OVER_1YEAR_UPTO_3YEARS() {
		return R55_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR55_OVER_1YEAR_UPTO_3YEARS(BigDecimal r55_OVER_1YEAR_UPTO_3YEARS) {
		R55_OVER_1YEAR_UPTO_3YEARS = r55_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR55_OVER_3YEARS_UPTO_5YEARS() {
		return R55_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR55_OVER_3YEARS_UPTO_5YEARS(BigDecimal r55_OVER_3YEARS_UPTO_5YEARS) {
		R55_OVER_3YEARS_UPTO_5YEARS = r55_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR55_OVER_5YEARS() {
		return R55_OVER_5YEARS;
	}
	public void setR55_OVER_5YEARS(BigDecimal r55_OVER_5YEARS) {
		R55_OVER_5YEARS = r55_OVER_5YEARS;
	}
	public BigDecimal getR55_TOTAL() {
		return R55_TOTAL;
	}
	public void setR55_TOTAL(BigDecimal r55_TOTAL) {
		R55_TOTAL = r55_TOTAL;
	}
	public BigDecimal getR56_1_DAY() {
		return R56_1_DAY;
	}
	public void setR56_1_DAY(BigDecimal r56_1_DAY) {
		R56_1_DAY = r56_1_DAY;
	}
	public BigDecimal getR56_2TO7_DAYS() {
		return R56_2TO7_DAYS;
	}
	public void setR56_2TO7_DAYS(BigDecimal r56_2to7_DAYS) {
		R56_2TO7_DAYS = r56_2to7_DAYS;
	}
	public BigDecimal getR56_8TO14_DAYS() {
		return R56_8TO14_DAYS;
	}
	public void setR56_8TO14_DAYS(BigDecimal r56_8to14_DAYS) {
		R56_8TO14_DAYS = r56_8to14_DAYS;
	}
	public BigDecimal getR56_15TO30_DAYS() {
		return R56_15TO30_DAYS;
	}
	public void setR56_15TO30_DAYS(BigDecimal r56_15to30_DAYS) {
		R56_15TO30_DAYS = r56_15to30_DAYS;
	}
	public BigDecimal getR56_31DAYS_UPTO_2MONTHS() {
		return R56_31DAYS_UPTO_2MONTHS;
	}
	public void setR56_31DAYS_UPTO_2MONTHS(BigDecimal r56_31days_UPTO_2MONTHS) {
		R56_31DAYS_UPTO_2MONTHS = r56_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR56_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R56_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR56_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r56_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R56_MORETHAN_2MONTHS_UPTO_3MONHTS = r56_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR56_OVER_3MONTHS_UPTO_6MONTHS() {
		return R56_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR56_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r56_OVER_3MONTHS_UPTO_6MONTHS) {
		R56_OVER_3MONTHS_UPTO_6MONTHS = r56_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR56_OVER_6MONTHS_UPTO_1YEAR() {
		return R56_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR56_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r56_OVER_6MONTHS_UPTO_1YEAR) {
		R56_OVER_6MONTHS_UPTO_1YEAR = r56_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR56_OVER_1YEAR_UPTO_3YEARS() {
		return R56_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR56_OVER_1YEAR_UPTO_3YEARS(BigDecimal r56_OVER_1YEAR_UPTO_3YEARS) {
		R56_OVER_1YEAR_UPTO_3YEARS = r56_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR56_OVER_3YEARS_UPTO_5YEARS() {
		return R56_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR56_OVER_3YEARS_UPTO_5YEARS(BigDecimal r56_OVER_3YEARS_UPTO_5YEARS) {
		R56_OVER_3YEARS_UPTO_5YEARS = r56_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR56_OVER_5YEARS() {
		return R56_OVER_5YEARS;
	}
	public void setR56_OVER_5YEARS(BigDecimal r56_OVER_5YEARS) {
		R56_OVER_5YEARS = r56_OVER_5YEARS;
	}
	public BigDecimal getR56_TOTAL() {
		return R56_TOTAL;
	}
	public void setR56_TOTAL(BigDecimal r56_TOTAL) {
		R56_TOTAL = r56_TOTAL;
	}
	public BigDecimal getR57_1_DAY() {
		return R57_1_DAY;
	}
	public void setR57_1_DAY(BigDecimal r57_1_DAY) {
		R57_1_DAY = r57_1_DAY;
	}
	public BigDecimal getR57_2TO7_DAYS() {
		return R57_2TO7_DAYS;
	}
	public void setR57_2TO7_DAYS(BigDecimal r57_2to7_DAYS) {
		R57_2TO7_DAYS = r57_2to7_DAYS;
	}
	public BigDecimal getR57_8TO14_DAYS() {
		return R57_8TO14_DAYS;
	}
	public void setR57_8TO14_DAYS(BigDecimal r57_8to14_DAYS) {
		R57_8TO14_DAYS = r57_8to14_DAYS;
	}
	public BigDecimal getR57_15TO30_DAYS() {
		return R57_15TO30_DAYS;
	}
	public void setR57_15TO30_DAYS(BigDecimal r57_15to30_DAYS) {
		R57_15TO30_DAYS = r57_15to30_DAYS;
	}
	public BigDecimal getR57_31DAYS_UPTO_2MONTHS() {
		return R57_31DAYS_UPTO_2MONTHS;
	}
	public void setR57_31DAYS_UPTO_2MONTHS(BigDecimal r57_31days_UPTO_2MONTHS) {
		R57_31DAYS_UPTO_2MONTHS = r57_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR57_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R57_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR57_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r57_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R57_MORETHAN_2MONTHS_UPTO_3MONHTS = r57_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR57_OVER_3MONTHS_UPTO_6MONTHS() {
		return R57_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR57_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r57_OVER_3MONTHS_UPTO_6MONTHS) {
		R57_OVER_3MONTHS_UPTO_6MONTHS = r57_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR57_OVER_6MONTHS_UPTO_1YEAR() {
		return R57_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR57_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r57_OVER_6MONTHS_UPTO_1YEAR) {
		R57_OVER_6MONTHS_UPTO_1YEAR = r57_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR57_OVER_1YEAR_UPTO_3YEARS() {
		return R57_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR57_OVER_1YEAR_UPTO_3YEARS(BigDecimal r57_OVER_1YEAR_UPTO_3YEARS) {
		R57_OVER_1YEAR_UPTO_3YEARS = r57_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR57_OVER_3YEARS_UPTO_5YEARS() {
		return R57_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR57_OVER_3YEARS_UPTO_5YEARS(BigDecimal r57_OVER_3YEARS_UPTO_5YEARS) {
		R57_OVER_3YEARS_UPTO_5YEARS = r57_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR57_OVER_5YEARS() {
		return R57_OVER_5YEARS;
	}
	public void setR57_OVER_5YEARS(BigDecimal r57_OVER_5YEARS) {
		R57_OVER_5YEARS = r57_OVER_5YEARS;
	}
	public BigDecimal getR57_TOTAL() {
		return R57_TOTAL;
	}
	public void setR57_TOTAL(BigDecimal r57_TOTAL) {
		R57_TOTAL = r57_TOTAL;
	}
	public BigDecimal getR58_1_DAY() {
		return R58_1_DAY;
	}
	public void setR58_1_DAY(BigDecimal r58_1_DAY) {
		R58_1_DAY = r58_1_DAY;
	}
	public BigDecimal getR58_2TO7_DAYS() {
		return R58_2TO7_DAYS;
	}
	public void setR58_2TO7_DAYS(BigDecimal r58_2to7_DAYS) {
		R58_2TO7_DAYS = r58_2to7_DAYS;
	}
	public BigDecimal getR58_8TO14_DAYS() {
		return R58_8TO14_DAYS;
	}
	public void setR58_8TO14_DAYS(BigDecimal r58_8to14_DAYS) {
		R58_8TO14_DAYS = r58_8to14_DAYS;
	}
	public BigDecimal getR58_15TO30_DAYS() {
		return R58_15TO30_DAYS;
	}
	public void setR58_15TO30_DAYS(BigDecimal r58_15to30_DAYS) {
		R58_15TO30_DAYS = r58_15to30_DAYS;
	}
	public BigDecimal getR58_31DAYS_UPTO_2MONTHS() {
		return R58_31DAYS_UPTO_2MONTHS;
	}
	public void setR58_31DAYS_UPTO_2MONTHS(BigDecimal r58_31days_UPTO_2MONTHS) {
		R58_31DAYS_UPTO_2MONTHS = r58_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR58_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R58_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR58_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r58_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R58_MORETHAN_2MONTHS_UPTO_3MONHTS = r58_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR58_OVER_3MONTHS_UPTO_6MONTHS() {
		return R58_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR58_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r58_OVER_3MONTHS_UPTO_6MONTHS) {
		R58_OVER_3MONTHS_UPTO_6MONTHS = r58_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR58_OVER_6MONTHS_UPTO_1YEAR() {
		return R58_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR58_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r58_OVER_6MONTHS_UPTO_1YEAR) {
		R58_OVER_6MONTHS_UPTO_1YEAR = r58_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR58_OVER_1YEAR_UPTO_3YEARS() {
		return R58_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR58_OVER_1YEAR_UPTO_3YEARS(BigDecimal r58_OVER_1YEAR_UPTO_3YEARS) {
		R58_OVER_1YEAR_UPTO_3YEARS = r58_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR58_OVER_3YEARS_UPTO_5YEARS() {
		return R58_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR58_OVER_3YEARS_UPTO_5YEARS(BigDecimal r58_OVER_3YEARS_UPTO_5YEARS) {
		R58_OVER_3YEARS_UPTO_5YEARS = r58_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR58_OVER_5YEARS() {
		return R58_OVER_5YEARS;
	}
	public void setR58_OVER_5YEARS(BigDecimal r58_OVER_5YEARS) {
		R58_OVER_5YEARS = r58_OVER_5YEARS;
	}
	public BigDecimal getR58_TOTAL() {
		return R58_TOTAL;
	}
	public void setR58_TOTAL(BigDecimal r58_TOTAL) {
		R58_TOTAL = r58_TOTAL;
	}
	public BigDecimal getR59_1_DAY() {
		return R59_1_DAY;
	}
	public void setR59_1_DAY(BigDecimal r59_1_DAY) {
		R59_1_DAY = r59_1_DAY;
	}
	public BigDecimal getR59_2TO7_DAYS() {
		return R59_2TO7_DAYS;
	}
	public void setR59_2TO7_DAYS(BigDecimal r59_2to7_DAYS) {
		R59_2TO7_DAYS = r59_2to7_DAYS;
	}
	public BigDecimal getR59_8TO14_DAYS() {
		return R59_8TO14_DAYS;
	}
	public void setR59_8TO14_DAYS(BigDecimal r59_8to14_DAYS) {
		R59_8TO14_DAYS = r59_8to14_DAYS;
	}
	public BigDecimal getR59_15TO30_DAYS() {
		return R59_15TO30_DAYS;
	}
	public void setR59_15TO30_DAYS(BigDecimal r59_15to30_DAYS) {
		R59_15TO30_DAYS = r59_15to30_DAYS;
	}
	public BigDecimal getR59_31DAYS_UPTO_2MONTHS() {
		return R59_31DAYS_UPTO_2MONTHS;
	}
	public void setR59_31DAYS_UPTO_2MONTHS(BigDecimal r59_31days_UPTO_2MONTHS) {
		R59_31DAYS_UPTO_2MONTHS = r59_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR59_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R59_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR59_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r59_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R59_MORETHAN_2MONTHS_UPTO_3MONHTS = r59_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR59_OVER_3MONTHS_UPTO_6MONTHS() {
		return R59_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR59_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r59_OVER_3MONTHS_UPTO_6MONTHS) {
		R59_OVER_3MONTHS_UPTO_6MONTHS = r59_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR59_OVER_6MONTHS_UPTO_1YEAR() {
		return R59_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR59_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r59_OVER_6MONTHS_UPTO_1YEAR) {
		R59_OVER_6MONTHS_UPTO_1YEAR = r59_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR59_OVER_1YEAR_UPTO_3YEARS() {
		return R59_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR59_OVER_1YEAR_UPTO_3YEARS(BigDecimal r59_OVER_1YEAR_UPTO_3YEARS) {
		R59_OVER_1YEAR_UPTO_3YEARS = r59_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR59_OVER_3YEARS_UPTO_5YEARS() {
		return R59_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR59_OVER_3YEARS_UPTO_5YEARS(BigDecimal r59_OVER_3YEARS_UPTO_5YEARS) {
		R59_OVER_3YEARS_UPTO_5YEARS = r59_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR59_OVER_5YEARS() {
		return R59_OVER_5YEARS;
	}
	public void setR59_OVER_5YEARS(BigDecimal r59_OVER_5YEARS) {
		R59_OVER_5YEARS = r59_OVER_5YEARS;
	}
	public BigDecimal getR59_TOTAL() {
		return R59_TOTAL;
	}
	public void setR59_TOTAL(BigDecimal r59_TOTAL) {
		R59_TOTAL = r59_TOTAL;
	}
	public BigDecimal getR60_1_DAY() {
		return R60_1_DAY;
	}
	public void setR60_1_DAY(BigDecimal r60_1_DAY) {
		R60_1_DAY = r60_1_DAY;
	}
	public BigDecimal getR60_2TO7_DAYS() {
		return R60_2TO7_DAYS;
	}
	public void setR60_2TO7_DAYS(BigDecimal r60_2to7_DAYS) {
		R60_2TO7_DAYS = r60_2to7_DAYS;
	}
	public BigDecimal getR60_8TO14_DAYS() {
		return R60_8TO14_DAYS;
	}
	public void setR60_8TO14_DAYS(BigDecimal r60_8to14_DAYS) {
		R60_8TO14_DAYS = r60_8to14_DAYS;
	}
	public BigDecimal getR60_15TO30_DAYS() {
		return R60_15TO30_DAYS;
	}
	public void setR60_15TO30_DAYS(BigDecimal r60_15to30_DAYS) {
		R60_15TO30_DAYS = r60_15to30_DAYS;
	}
	public BigDecimal getR60_31DAYS_UPTO_2MONTHS() {
		return R60_31DAYS_UPTO_2MONTHS;
	}
	public void setR60_31DAYS_UPTO_2MONTHS(BigDecimal r60_31days_UPTO_2MONTHS) {
		R60_31DAYS_UPTO_2MONTHS = r60_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR60_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R60_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR60_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r60_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R60_MORETHAN_2MONTHS_UPTO_3MONHTS = r60_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR60_OVER_3MONTHS_UPTO_6MONTHS() {
		return R60_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR60_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r60_OVER_3MONTHS_UPTO_6MONTHS) {
		R60_OVER_3MONTHS_UPTO_6MONTHS = r60_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR60_OVER_6MONTHS_UPTO_1YEAR() {
		return R60_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR60_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r60_OVER_6MONTHS_UPTO_1YEAR) {
		R60_OVER_6MONTHS_UPTO_1YEAR = r60_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR60_OVER_1YEAR_UPTO_3YEARS() {
		return R60_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR60_OVER_1YEAR_UPTO_3YEARS(BigDecimal r60_OVER_1YEAR_UPTO_3YEARS) {
		R60_OVER_1YEAR_UPTO_3YEARS = r60_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR60_OVER_3YEARS_UPTO_5YEARS() {
		return R60_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR60_OVER_3YEARS_UPTO_5YEARS(BigDecimal r60_OVER_3YEARS_UPTO_5YEARS) {
		R60_OVER_3YEARS_UPTO_5YEARS = r60_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR60_OVER_5YEARS() {
		return R60_OVER_5YEARS;
	}
	public void setR60_OVER_5YEARS(BigDecimal r60_OVER_5YEARS) {
		R60_OVER_5YEARS = r60_OVER_5YEARS;
	}
	public BigDecimal getR60_TOTAL() {
		return R60_TOTAL;
	}
	public void setR60_TOTAL(BigDecimal r60_TOTAL) {
		R60_TOTAL = r60_TOTAL;
	}
	public BigDecimal getR61_1_DAY() {
		return R61_1_DAY;
	}
	public void setR61_1_DAY(BigDecimal r61_1_DAY) {
		R61_1_DAY = r61_1_DAY;
	}
	public BigDecimal getR61_2TO7_DAYS() {
		return R61_2TO7_DAYS;
	}
	public void setR61_2TO7_DAYS(BigDecimal r61_2to7_DAYS) {
		R61_2TO7_DAYS = r61_2to7_DAYS;
	}
	public BigDecimal getR61_8TO14_DAYS() {
		return R61_8TO14_DAYS;
	}
	public void setR61_8TO14_DAYS(BigDecimal r61_8to14_DAYS) {
		R61_8TO14_DAYS = r61_8to14_DAYS;
	}
	public BigDecimal getR61_15TO30_DAYS() {
		return R61_15TO30_DAYS;
	}
	public void setR61_15TO30_DAYS(BigDecimal r61_15to30_DAYS) {
		R61_15TO30_DAYS = r61_15to30_DAYS;
	}
	public BigDecimal getR61_31DAYS_UPTO_2MONTHS() {
		return R61_31DAYS_UPTO_2MONTHS;
	}
	public void setR61_31DAYS_UPTO_2MONTHS(BigDecimal r61_31days_UPTO_2MONTHS) {
		R61_31DAYS_UPTO_2MONTHS = r61_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR61_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R61_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR61_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r61_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R61_MORETHAN_2MONTHS_UPTO_3MONHTS = r61_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR61_OVER_3MONTHS_UPTO_6MONTHS() {
		return R61_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR61_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r61_OVER_3MONTHS_UPTO_6MONTHS) {
		R61_OVER_3MONTHS_UPTO_6MONTHS = r61_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR61_OVER_6MONTHS_UPTO_1YEAR() {
		return R61_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR61_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r61_OVER_6MONTHS_UPTO_1YEAR) {
		R61_OVER_6MONTHS_UPTO_1YEAR = r61_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR61_OVER_1YEAR_UPTO_3YEARS() {
		return R61_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR61_OVER_1YEAR_UPTO_3YEARS(BigDecimal r61_OVER_1YEAR_UPTO_3YEARS) {
		R61_OVER_1YEAR_UPTO_3YEARS = r61_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR61_OVER_3YEARS_UPTO_5YEARS() {
		return R61_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR61_OVER_3YEARS_UPTO_5YEARS(BigDecimal r61_OVER_3YEARS_UPTO_5YEARS) {
		R61_OVER_3YEARS_UPTO_5YEARS = r61_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR61_OVER_5YEARS() {
		return R61_OVER_5YEARS;
	}
	public void setR61_OVER_5YEARS(BigDecimal r61_OVER_5YEARS) {
		R61_OVER_5YEARS = r61_OVER_5YEARS;
	}
	public BigDecimal getR61_TOTAL() {
		return R61_TOTAL;
	}
	public void setR61_TOTAL(BigDecimal r61_TOTAL) {
		R61_TOTAL = r61_TOTAL;
	}
	public BigDecimal getR62_1_DAY() {
		return R62_1_DAY;
	}
	public void setR62_1_DAY(BigDecimal r62_1_DAY) {
		R62_1_DAY = r62_1_DAY;
	}
	public BigDecimal getR62_2TO7_DAYS() {
		return R62_2TO7_DAYS;
	}
	public void setR62_2TO7_DAYS(BigDecimal r62_2to7_DAYS) {
		R62_2TO7_DAYS = r62_2to7_DAYS;
	}
	public BigDecimal getR62_8TO14_DAYS() {
		return R62_8TO14_DAYS;
	}
	public void setR62_8TO14_DAYS(BigDecimal r62_8to14_DAYS) {
		R62_8TO14_DAYS = r62_8to14_DAYS;
	}
	public BigDecimal getR62_15TO30_DAYS() {
		return R62_15TO30_DAYS;
	}
	public void setR62_15TO30_DAYS(BigDecimal r62_15to30_DAYS) {
		R62_15TO30_DAYS = r62_15to30_DAYS;
	}
	public BigDecimal getR62_31DAYS_UPTO_2MONTHS() {
		return R62_31DAYS_UPTO_2MONTHS;
	}
	public void setR62_31DAYS_UPTO_2MONTHS(BigDecimal r62_31days_UPTO_2MONTHS) {
		R62_31DAYS_UPTO_2MONTHS = r62_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR62_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R62_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR62_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r62_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R62_MORETHAN_2MONTHS_UPTO_3MONHTS = r62_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR62_OVER_3MONTHS_UPTO_6MONTHS() {
		return R62_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR62_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r62_OVER_3MONTHS_UPTO_6MONTHS) {
		R62_OVER_3MONTHS_UPTO_6MONTHS = r62_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR62_OVER_6MONTHS_UPTO_1YEAR() {
		return R62_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR62_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r62_OVER_6MONTHS_UPTO_1YEAR) {
		R62_OVER_6MONTHS_UPTO_1YEAR = r62_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR62_OVER_1YEAR_UPTO_3YEARS() {
		return R62_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR62_OVER_1YEAR_UPTO_3YEARS(BigDecimal r62_OVER_1YEAR_UPTO_3YEARS) {
		R62_OVER_1YEAR_UPTO_3YEARS = r62_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR62_OVER_3YEARS_UPTO_5YEARS() {
		return R62_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR62_OVER_3YEARS_UPTO_5YEARS(BigDecimal r62_OVER_3YEARS_UPTO_5YEARS) {
		R62_OVER_3YEARS_UPTO_5YEARS = r62_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR62_OVER_5YEARS() {
		return R62_OVER_5YEARS;
	}
	public void setR62_OVER_5YEARS(BigDecimal r62_OVER_5YEARS) {
		R62_OVER_5YEARS = r62_OVER_5YEARS;
	}
	public BigDecimal getR62_TOTAL() {
		return R62_TOTAL;
	}
	public void setR62_TOTAL(BigDecimal r62_TOTAL) {
		R62_TOTAL = r62_TOTAL;
	}
	public BigDecimal getR63_1_DAY() {
		return R63_1_DAY;
	}
	public void setR63_1_DAY(BigDecimal r63_1_DAY) {
		R63_1_DAY = r63_1_DAY;
	}
	public BigDecimal getR63_2TO7_DAYS() {
		return R63_2TO7_DAYS;
	}
	public void setR63_2TO7_DAYS(BigDecimal r63_2to7_DAYS) {
		R63_2TO7_DAYS = r63_2to7_DAYS;
	}
	public BigDecimal getR63_8TO14_DAYS() {
		return R63_8TO14_DAYS;
	}
	public void setR63_8TO14_DAYS(BigDecimal r63_8to14_DAYS) {
		R63_8TO14_DAYS = r63_8to14_DAYS;
	}
	public BigDecimal getR63_15TO30_DAYS() {
		return R63_15TO30_DAYS;
	}
	public void setR63_15TO30_DAYS(BigDecimal r63_15to30_DAYS) {
		R63_15TO30_DAYS = r63_15to30_DAYS;
	}
	public BigDecimal getR63_31DAYS_UPTO_2MONTHS() {
		return R63_31DAYS_UPTO_2MONTHS;
	}
	public void setR63_31DAYS_UPTO_2MONTHS(BigDecimal r63_31days_UPTO_2MONTHS) {
		R63_31DAYS_UPTO_2MONTHS = r63_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR63_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R63_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR63_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r63_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R63_MORETHAN_2MONTHS_UPTO_3MONHTS = r63_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR63_OVER_3MONTHS_UPTO_6MONTHS() {
		return R63_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR63_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r63_OVER_3MONTHS_UPTO_6MONTHS) {
		R63_OVER_3MONTHS_UPTO_6MONTHS = r63_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR63_OVER_6MONTHS_UPTO_1YEAR() {
		return R63_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR63_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r63_OVER_6MONTHS_UPTO_1YEAR) {
		R63_OVER_6MONTHS_UPTO_1YEAR = r63_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR63_OVER_1YEAR_UPTO_3YEARS() {
		return R63_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR63_OVER_1YEAR_UPTO_3YEARS(BigDecimal r63_OVER_1YEAR_UPTO_3YEARS) {
		R63_OVER_1YEAR_UPTO_3YEARS = r63_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR63_OVER_3YEARS_UPTO_5YEARS() {
		return R63_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR63_OVER_3YEARS_UPTO_5YEARS(BigDecimal r63_OVER_3YEARS_UPTO_5YEARS) {
		R63_OVER_3YEARS_UPTO_5YEARS = r63_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR63_OVER_5YEARS() {
		return R63_OVER_5YEARS;
	}
	public void setR63_OVER_5YEARS(BigDecimal r63_OVER_5YEARS) {
		R63_OVER_5YEARS = r63_OVER_5YEARS;
	}
	public BigDecimal getR63_TOTAL() {
		return R63_TOTAL;
	}
	public void setR63_TOTAL(BigDecimal r63_TOTAL) {
		R63_TOTAL = r63_TOTAL;
	}
	public BigDecimal getR64_1_DAY() {
		return R64_1_DAY;
	}
	public void setR64_1_DAY(BigDecimal r64_1_DAY) {
		R64_1_DAY = r64_1_DAY;
	}
	public BigDecimal getR64_2TO7_DAYS() {
		return R64_2TO7_DAYS;
	}
	public void setR64_2TO7_DAYS(BigDecimal r64_2to7_DAYS) {
		R64_2TO7_DAYS = r64_2to7_DAYS;
	}
	public BigDecimal getR64_8TO14_DAYS() {
		return R64_8TO14_DAYS;
	}
	public void setR64_8TO14_DAYS(BigDecimal r64_8to14_DAYS) {
		R64_8TO14_DAYS = r64_8to14_DAYS;
	}
	public BigDecimal getR64_15TO30_DAYS() {
		return R64_15TO30_DAYS;
	}
	public void setR64_15TO30_DAYS(BigDecimal r64_15to30_DAYS) {
		R64_15TO30_DAYS = r64_15to30_DAYS;
	}
	public BigDecimal getR64_31DAYS_UPTO_2MONTHS() {
		return R64_31DAYS_UPTO_2MONTHS;
	}
	public void setR64_31DAYS_UPTO_2MONTHS(BigDecimal r64_31days_UPTO_2MONTHS) {
		R64_31DAYS_UPTO_2MONTHS = r64_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR64_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R64_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR64_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r64_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R64_MORETHAN_2MONTHS_UPTO_3MONHTS = r64_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR64_OVER_3MONTHS_UPTO_6MONTHS() {
		return R64_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR64_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r64_OVER_3MONTHS_UPTO_6MONTHS) {
		R64_OVER_3MONTHS_UPTO_6MONTHS = r64_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR64_OVER_6MONTHS_UPTO_1YEAR() {
		return R64_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR64_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r64_OVER_6MONTHS_UPTO_1YEAR) {
		R64_OVER_6MONTHS_UPTO_1YEAR = r64_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR64_OVER_1YEAR_UPTO_3YEARS() {
		return R64_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR64_OVER_1YEAR_UPTO_3YEARS(BigDecimal r64_OVER_1YEAR_UPTO_3YEARS) {
		R64_OVER_1YEAR_UPTO_3YEARS = r64_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR64_OVER_3YEARS_UPTO_5YEARS() {
		return R64_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR64_OVER_3YEARS_UPTO_5YEARS(BigDecimal r64_OVER_3YEARS_UPTO_5YEARS) {
		R64_OVER_3YEARS_UPTO_5YEARS = r64_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR64_OVER_5YEARS() {
		return R64_OVER_5YEARS;
	}
	public void setR64_OVER_5YEARS(BigDecimal r64_OVER_5YEARS) {
		R64_OVER_5YEARS = r64_OVER_5YEARS;
	}
	public BigDecimal getR64_TOTAL() {
		return R64_TOTAL;
	}
	public void setR64_TOTAL(BigDecimal r64_TOTAL) {
		R64_TOTAL = r64_TOTAL;
	}
	public BigDecimal getR65_1_DAY() {
		return R65_1_DAY;
	}
	public void setR65_1_DAY(BigDecimal r65_1_DAY) {
		R65_1_DAY = r65_1_DAY;
	}
	public BigDecimal getR65_2TO7_DAYS() {
		return R65_2TO7_DAYS;
	}
	public void setR65_2TO7_DAYS(BigDecimal r65_2to7_DAYS) {
		R65_2TO7_DAYS = r65_2to7_DAYS;
	}
	public BigDecimal getR65_8TO14_DAYS() {
		return R65_8TO14_DAYS;
	}
	public void setR65_8TO14_DAYS(BigDecimal r65_8to14_DAYS) {
		R65_8TO14_DAYS = r65_8to14_DAYS;
	}
	public BigDecimal getR65_15TO30_DAYS() {
		return R65_15TO30_DAYS;
	}
	public void setR65_15TO30_DAYS(BigDecimal r65_15to30_DAYS) {
		R65_15TO30_DAYS = r65_15to30_DAYS;
	}
	public BigDecimal getR65_31DAYS_UPTO_2MONTHS() {
		return R65_31DAYS_UPTO_2MONTHS;
	}
	public void setR65_31DAYS_UPTO_2MONTHS(BigDecimal r65_31days_UPTO_2MONTHS) {
		R65_31DAYS_UPTO_2MONTHS = r65_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR65_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R65_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR65_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r65_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R65_MORETHAN_2MONTHS_UPTO_3MONHTS = r65_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR65_OVER_3MONTHS_UPTO_6MONTHS() {
		return R65_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR65_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r65_OVER_3MONTHS_UPTO_6MONTHS) {
		R65_OVER_3MONTHS_UPTO_6MONTHS = r65_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR65_OVER_6MONTHS_UPTO_1YEAR() {
		return R65_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR65_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r65_OVER_6MONTHS_UPTO_1YEAR) {
		R65_OVER_6MONTHS_UPTO_1YEAR = r65_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR65_OVER_1YEAR_UPTO_3YEARS() {
		return R65_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR65_OVER_1YEAR_UPTO_3YEARS(BigDecimal r65_OVER_1YEAR_UPTO_3YEARS) {
		R65_OVER_1YEAR_UPTO_3YEARS = r65_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR65_OVER_3YEARS_UPTO_5YEARS() {
		return R65_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR65_OVER_3YEARS_UPTO_5YEARS(BigDecimal r65_OVER_3YEARS_UPTO_5YEARS) {
		R65_OVER_3YEARS_UPTO_5YEARS = r65_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR65_OVER_5YEARS() {
		return R65_OVER_5YEARS;
	}
	public void setR65_OVER_5YEARS(BigDecimal r65_OVER_5YEARS) {
		R65_OVER_5YEARS = r65_OVER_5YEARS;
	}
	public BigDecimal getR65_TOTAL() {
		return R65_TOTAL;
	}
	public void setR65_TOTAL(BigDecimal r65_TOTAL) {
		R65_TOTAL = r65_TOTAL;
	}
	public BigDecimal getR66_1_DAY() {
		return R66_1_DAY;
	}
	public void setR66_1_DAY(BigDecimal r66_1_DAY) {
		R66_1_DAY = r66_1_DAY;
	}
	public BigDecimal getR66_2TO7_DAYS() {
		return R66_2TO7_DAYS;
	}
	public void setR66_2TO7_DAYS(BigDecimal r66_2to7_DAYS) {
		R66_2TO7_DAYS = r66_2to7_DAYS;
	}
	public BigDecimal getR66_8TO14_DAYS() {
		return R66_8TO14_DAYS;
	}
	public void setR66_8TO14_DAYS(BigDecimal r66_8to14_DAYS) {
		R66_8TO14_DAYS = r66_8to14_DAYS;
	}
	public BigDecimal getR66_15TO30_DAYS() {
		return R66_15TO30_DAYS;
	}
	public void setR66_15TO30_DAYS(BigDecimal r66_15to30_DAYS) {
		R66_15TO30_DAYS = r66_15to30_DAYS;
	}
	public BigDecimal getR66_31DAYS_UPTO_2MONTHS() {
		return R66_31DAYS_UPTO_2MONTHS;
	}
	public void setR66_31DAYS_UPTO_2MONTHS(BigDecimal r66_31days_UPTO_2MONTHS) {
		R66_31DAYS_UPTO_2MONTHS = r66_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR66_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R66_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR66_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r66_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R66_MORETHAN_2MONTHS_UPTO_3MONHTS = r66_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR66_OVER_3MONTHS_UPTO_6MONTHS() {
		return R66_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR66_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r66_OVER_3MONTHS_UPTO_6MONTHS) {
		R66_OVER_3MONTHS_UPTO_6MONTHS = r66_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR66_OVER_6MONTHS_UPTO_1YEAR() {
		return R66_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR66_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r66_OVER_6MONTHS_UPTO_1YEAR) {
		R66_OVER_6MONTHS_UPTO_1YEAR = r66_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR66_OVER_1YEAR_UPTO_3YEARS() {
		return R66_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR66_OVER_1YEAR_UPTO_3YEARS(BigDecimal r66_OVER_1YEAR_UPTO_3YEARS) {
		R66_OVER_1YEAR_UPTO_3YEARS = r66_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR66_OVER_3YEARS_UPTO_5YEARS() {
		return R66_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR66_OVER_3YEARS_UPTO_5YEARS(BigDecimal r66_OVER_3YEARS_UPTO_5YEARS) {
		R66_OVER_3YEARS_UPTO_5YEARS = r66_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR66_OVER_5YEARS() {
		return R66_OVER_5YEARS;
	}
	public void setR66_OVER_5YEARS(BigDecimal r66_OVER_5YEARS) {
		R66_OVER_5YEARS = r66_OVER_5YEARS;
	}
	public BigDecimal getR66_TOTAL() {
		return R66_TOTAL;
	}
	public void setR66_TOTAL(BigDecimal r66_TOTAL) {
		R66_TOTAL = r66_TOTAL;
	}
	public BigDecimal getR67_1_DAY() {
		return R67_1_DAY;
	}
	public void setR67_1_DAY(BigDecimal r67_1_DAY) {
		R67_1_DAY = r67_1_DAY;
	}
	public BigDecimal getR67_2TO7_DAYS() {
		return R67_2TO7_DAYS;
	}
	public void setR67_2TO7_DAYS(BigDecimal r67_2to7_DAYS) {
		R67_2TO7_DAYS = r67_2to7_DAYS;
	}
	public BigDecimal getR67_8TO14_DAYS() {
		return R67_8TO14_DAYS;
	}
	public void setR67_8TO14_DAYS(BigDecimal r67_8to14_DAYS) {
		R67_8TO14_DAYS = r67_8to14_DAYS;
	}
	public BigDecimal getR67_15TO30_DAYS() {
		return R67_15TO30_DAYS;
	}
	public void setR67_15TO30_DAYS(BigDecimal r67_15to30_DAYS) {
		R67_15TO30_DAYS = r67_15to30_DAYS;
	}
	public BigDecimal getR67_31DAYS_UPTO_2MONTHS() {
		return R67_31DAYS_UPTO_2MONTHS;
	}
	public void setR67_31DAYS_UPTO_2MONTHS(BigDecimal r67_31days_UPTO_2MONTHS) {
		R67_31DAYS_UPTO_2MONTHS = r67_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR67_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R67_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR67_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r67_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R67_MORETHAN_2MONTHS_UPTO_3MONHTS = r67_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR67_OVER_3MONTHS_UPTO_6MONTHS() {
		return R67_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR67_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r67_OVER_3MONTHS_UPTO_6MONTHS) {
		R67_OVER_3MONTHS_UPTO_6MONTHS = r67_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR67_OVER_6MONTHS_UPTO_1YEAR() {
		return R67_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR67_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r67_OVER_6MONTHS_UPTO_1YEAR) {
		R67_OVER_6MONTHS_UPTO_1YEAR = r67_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR67_OVER_1YEAR_UPTO_3YEARS() {
		return R67_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR67_OVER_1YEAR_UPTO_3YEARS(BigDecimal r67_OVER_1YEAR_UPTO_3YEARS) {
		R67_OVER_1YEAR_UPTO_3YEARS = r67_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR67_OVER_3YEARS_UPTO_5YEARS() {
		return R67_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR67_OVER_3YEARS_UPTO_5YEARS(BigDecimal r67_OVER_3YEARS_UPTO_5YEARS) {
		R67_OVER_3YEARS_UPTO_5YEARS = r67_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR67_OVER_5YEARS() {
		return R67_OVER_5YEARS;
	}
	public void setR67_OVER_5YEARS(BigDecimal r67_OVER_5YEARS) {
		R67_OVER_5YEARS = r67_OVER_5YEARS;
	}
	public BigDecimal getR67_TOTAL() {
		return R67_TOTAL;
	}
	public void setR67_TOTAL(BigDecimal r67_TOTAL) {
		R67_TOTAL = r67_TOTAL;
	}
	public BigDecimal getR68_1_DAY() {
		return R68_1_DAY;
	}
	public void setR68_1_DAY(BigDecimal r68_1_DAY) {
		R68_1_DAY = r68_1_DAY;
	}
	public BigDecimal getR68_2TO7_DAYS() {
		return R68_2TO7_DAYS;
	}
	public void setR68_2TO7_DAYS(BigDecimal r68_2to7_DAYS) {
		R68_2TO7_DAYS = r68_2to7_DAYS;
	}
	public BigDecimal getR68_8TO14_DAYS() {
		return R68_8TO14_DAYS;
	}
	public void setR68_8TO14_DAYS(BigDecimal r68_8to14_DAYS) {
		R68_8TO14_DAYS = r68_8to14_DAYS;
	}
	public BigDecimal getR68_15TO30_DAYS() {
		return R68_15TO30_DAYS;
	}
	public void setR68_15TO30_DAYS(BigDecimal r68_15to30_DAYS) {
		R68_15TO30_DAYS = r68_15to30_DAYS;
	}
	public BigDecimal getR68_31DAYS_UPTO_2MONTHS() {
		return R68_31DAYS_UPTO_2MONTHS;
	}
	public void setR68_31DAYS_UPTO_2MONTHS(BigDecimal r68_31days_UPTO_2MONTHS) {
		R68_31DAYS_UPTO_2MONTHS = r68_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR68_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R68_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR68_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r68_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R68_MORETHAN_2MONTHS_UPTO_3MONHTS = r68_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR68_OVER_3MONTHS_UPTO_6MONTHS() {
		return R68_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR68_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r68_OVER_3MONTHS_UPTO_6MONTHS) {
		R68_OVER_3MONTHS_UPTO_6MONTHS = r68_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR68_OVER_6MONTHS_UPTO_1YEAR() {
		return R68_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR68_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r68_OVER_6MONTHS_UPTO_1YEAR) {
		R68_OVER_6MONTHS_UPTO_1YEAR = r68_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR68_OVER_1YEAR_UPTO_3YEARS() {
		return R68_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR68_OVER_1YEAR_UPTO_3YEARS(BigDecimal r68_OVER_1YEAR_UPTO_3YEARS) {
		R68_OVER_1YEAR_UPTO_3YEARS = r68_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR68_OVER_3YEARS_UPTO_5YEARS() {
		return R68_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR68_OVER_3YEARS_UPTO_5YEARS(BigDecimal r68_OVER_3YEARS_UPTO_5YEARS) {
		R68_OVER_3YEARS_UPTO_5YEARS = r68_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR68_OVER_5YEARS() {
		return R68_OVER_5YEARS;
	}
	public void setR68_OVER_5YEARS(BigDecimal r68_OVER_5YEARS) {
		R68_OVER_5YEARS = r68_OVER_5YEARS;
	}
	public BigDecimal getR68_TOTAL() {
		return R68_TOTAL;
	}
	public void setR68_TOTAL(BigDecimal r68_TOTAL) {
		R68_TOTAL = r68_TOTAL;
	}
	public BigDecimal getR69_1_DAY() {
		return R69_1_DAY;
	}
	public void setR69_1_DAY(BigDecimal r69_1_DAY) {
		R69_1_DAY = r69_1_DAY;
	}
	public BigDecimal getR69_2TO7_DAYS() {
		return R69_2TO7_DAYS;
	}
	public void setR69_2TO7_DAYS(BigDecimal r69_2to7_DAYS) {
		R69_2TO7_DAYS = r69_2to7_DAYS;
	}
	public BigDecimal getR69_8TO14_DAYS() {
		return R69_8TO14_DAYS;
	}
	public void setR69_8TO14_DAYS(BigDecimal r69_8to14_DAYS) {
		R69_8TO14_DAYS = r69_8to14_DAYS;
	}
	public BigDecimal getR69_15TO30_DAYS() {
		return R69_15TO30_DAYS;
	}
	public void setR69_15TO30_DAYS(BigDecimal r69_15to30_DAYS) {
		R69_15TO30_DAYS = r69_15to30_DAYS;
	}
	public BigDecimal getR69_31DAYS_UPTO_2MONTHS() {
		return R69_31DAYS_UPTO_2MONTHS;
	}
	public void setR69_31DAYS_UPTO_2MONTHS(BigDecimal r69_31days_UPTO_2MONTHS) {
		R69_31DAYS_UPTO_2MONTHS = r69_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR69_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R69_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR69_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r69_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R69_MORETHAN_2MONTHS_UPTO_3MONHTS = r69_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR69_OVER_3MONTHS_UPTO_6MONTHS() {
		return R69_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR69_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r69_OVER_3MONTHS_UPTO_6MONTHS) {
		R69_OVER_3MONTHS_UPTO_6MONTHS = r69_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR69_OVER_6MONTHS_UPTO_1YEAR() {
		return R69_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR69_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r69_OVER_6MONTHS_UPTO_1YEAR) {
		R69_OVER_6MONTHS_UPTO_1YEAR = r69_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR69_OVER_1YEAR_UPTO_3YEARS() {
		return R69_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR69_OVER_1YEAR_UPTO_3YEARS(BigDecimal r69_OVER_1YEAR_UPTO_3YEARS) {
		R69_OVER_1YEAR_UPTO_3YEARS = r69_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR69_OVER_3YEARS_UPTO_5YEARS() {
		return R69_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR69_OVER_3YEARS_UPTO_5YEARS(BigDecimal r69_OVER_3YEARS_UPTO_5YEARS) {
		R69_OVER_3YEARS_UPTO_5YEARS = r69_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR69_OVER_5YEARS() {
		return R69_OVER_5YEARS;
	}
	public void setR69_OVER_5YEARS(BigDecimal r69_OVER_5YEARS) {
		R69_OVER_5YEARS = r69_OVER_5YEARS;
	}
	public BigDecimal getR69_TOTAL() {
		return R69_TOTAL;
	}
	public void setR69_TOTAL(BigDecimal r69_TOTAL) {
		R69_TOTAL = r69_TOTAL;
	}
	public BigDecimal getR70_1_DAY() {
		return R70_1_DAY;
	}
	public void setR70_1_DAY(BigDecimal r70_1_DAY) {
		R70_1_DAY = r70_1_DAY;
	}
	public BigDecimal getR70_2TO7_DAYS() {
		return R70_2TO7_DAYS;
	}
	public void setR70_2TO7_DAYS(BigDecimal r70_2to7_DAYS) {
		R70_2TO7_DAYS = r70_2to7_DAYS;
	}
	public BigDecimal getR70_8TO14_DAYS() {
		return R70_8TO14_DAYS;
	}
	public void setR70_8TO14_DAYS(BigDecimal r70_8to14_DAYS) {
		R70_8TO14_DAYS = r70_8to14_DAYS;
	}
	public BigDecimal getR70_15TO30_DAYS() {
		return R70_15TO30_DAYS;
	}
	public void setR70_15TO30_DAYS(BigDecimal r70_15to30_DAYS) {
		R70_15TO30_DAYS = r70_15to30_DAYS;
	}
	public BigDecimal getR70_31DAYS_UPTO_2MONTHS() {
		return R70_31DAYS_UPTO_2MONTHS;
	}
	public void setR70_31DAYS_UPTO_2MONTHS(BigDecimal r70_31days_UPTO_2MONTHS) {
		R70_31DAYS_UPTO_2MONTHS = r70_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR70_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R70_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR70_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r70_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R70_MORETHAN_2MONTHS_UPTO_3MONHTS = r70_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR70_OVER_3MONTHS_UPTO_6MONTHS() {
		return R70_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR70_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r70_OVER_3MONTHS_UPTO_6MONTHS) {
		R70_OVER_3MONTHS_UPTO_6MONTHS = r70_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR70_OVER_6MONTHS_UPTO_1YEAR() {
		return R70_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR70_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r70_OVER_6MONTHS_UPTO_1YEAR) {
		R70_OVER_6MONTHS_UPTO_1YEAR = r70_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR70_OVER_1YEAR_UPTO_3YEARS() {
		return R70_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR70_OVER_1YEAR_UPTO_3YEARS(BigDecimal r70_OVER_1YEAR_UPTO_3YEARS) {
		R70_OVER_1YEAR_UPTO_3YEARS = r70_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR70_OVER_3YEARS_UPTO_5YEARS() {
		return R70_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR70_OVER_3YEARS_UPTO_5YEARS(BigDecimal r70_OVER_3YEARS_UPTO_5YEARS) {
		R70_OVER_3YEARS_UPTO_5YEARS = r70_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR70_OVER_5YEARS() {
		return R70_OVER_5YEARS;
	}
	public void setR70_OVER_5YEARS(BigDecimal r70_OVER_5YEARS) {
		R70_OVER_5YEARS = r70_OVER_5YEARS;
	}
	public BigDecimal getR70_TOTAL() {
		return R70_TOTAL;
	}
	public void setR70_TOTAL(BigDecimal r70_TOTAL) {
		R70_TOTAL = r70_TOTAL;
	}
	public BigDecimal getR71_1_DAY() {
		return R71_1_DAY;
	}
	public void setR71_1_DAY(BigDecimal r71_1_DAY) {
		R71_1_DAY = r71_1_DAY;
	}
	public BigDecimal getR71_2TO7_DAYS() {
		return R71_2TO7_DAYS;
	}
	public void setR71_2TO7_DAYS(BigDecimal r71_2to7_DAYS) {
		R71_2TO7_DAYS = r71_2to7_DAYS;
	}
	public BigDecimal getR71_8TO14_DAYS() {
		return R71_8TO14_DAYS;
	}
	public void setR71_8TO14_DAYS(BigDecimal r71_8to14_DAYS) {
		R71_8TO14_DAYS = r71_8to14_DAYS;
	}
	public BigDecimal getR71_15TO30_DAYS() {
		return R71_15TO30_DAYS;
	}
	public void setR71_15TO30_DAYS(BigDecimal r71_15to30_DAYS) {
		R71_15TO30_DAYS = r71_15to30_DAYS;
	}
	public BigDecimal getR71_31DAYS_UPTO_2MONTHS() {
		return R71_31DAYS_UPTO_2MONTHS;
	}
	public void setR71_31DAYS_UPTO_2MONTHS(BigDecimal r71_31days_UPTO_2MONTHS) {
		R71_31DAYS_UPTO_2MONTHS = r71_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR71_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R71_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR71_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r71_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R71_MORETHAN_2MONTHS_UPTO_3MONHTS = r71_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR71_OVER_3MONTHS_UPTO_6MONTHS() {
		return R71_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR71_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r71_OVER_3MONTHS_UPTO_6MONTHS) {
		R71_OVER_3MONTHS_UPTO_6MONTHS = r71_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR71_OVER_6MONTHS_UPTO_1YEAR() {
		return R71_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR71_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r71_OVER_6MONTHS_UPTO_1YEAR) {
		R71_OVER_6MONTHS_UPTO_1YEAR = r71_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR71_OVER_1YEAR_UPTO_3YEARS() {
		return R71_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR71_OVER_1YEAR_UPTO_3YEARS(BigDecimal r71_OVER_1YEAR_UPTO_3YEARS) {
		R71_OVER_1YEAR_UPTO_3YEARS = r71_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR71_OVER_3YEARS_UPTO_5YEARS() {
		return R71_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR71_OVER_3YEARS_UPTO_5YEARS(BigDecimal r71_OVER_3YEARS_UPTO_5YEARS) {
		R71_OVER_3YEARS_UPTO_5YEARS = r71_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR71_OVER_5YEARS() {
		return R71_OVER_5YEARS;
	}
	public void setR71_OVER_5YEARS(BigDecimal r71_OVER_5YEARS) {
		R71_OVER_5YEARS = r71_OVER_5YEARS;
	}
	public BigDecimal getR71_TOTAL() {
		return R71_TOTAL;
	}
	public void setR71_TOTAL(BigDecimal r71_TOTAL) {
		R71_TOTAL = r71_TOTAL;
	}
	public BigDecimal getR72_1_DAY() {
		return R72_1_DAY;
	}
	public void setR72_1_DAY(BigDecimal r72_1_DAY) {
		R72_1_DAY = r72_1_DAY;
	}
	public BigDecimal getR72_2TO7_DAYS() {
		return R72_2TO7_DAYS;
	}
	public void setR72_2TO7_DAYS(BigDecimal r72_2to7_DAYS) {
		R72_2TO7_DAYS = r72_2to7_DAYS;
	}
	public BigDecimal getR72_8TO14_DAYS() {
		return R72_8TO14_DAYS;
	}
	public void setR72_8TO14_DAYS(BigDecimal r72_8to14_DAYS) {
		R72_8TO14_DAYS = r72_8to14_DAYS;
	}
	public BigDecimal getR72_15TO30_DAYS() {
		return R72_15TO30_DAYS;
	}
	public void setR72_15TO30_DAYS(BigDecimal r72_15to30_DAYS) {
		R72_15TO30_DAYS = r72_15to30_DAYS;
	}
	public BigDecimal getR72_31DAYS_UPTO_2MONTHS() {
		return R72_31DAYS_UPTO_2MONTHS;
	}
	public void setR72_31DAYS_UPTO_2MONTHS(BigDecimal r72_31days_UPTO_2MONTHS) {
		R72_31DAYS_UPTO_2MONTHS = r72_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR72_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R72_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR72_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r72_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R72_MORETHAN_2MONTHS_UPTO_3MONHTS = r72_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR72_OVER_3MONTHS_UPTO_6MONTHS() {
		return R72_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR72_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r72_OVER_3MONTHS_UPTO_6MONTHS) {
		R72_OVER_3MONTHS_UPTO_6MONTHS = r72_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR72_OVER_6MONTHS_UPTO_1YEAR() {
		return R72_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR72_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r72_OVER_6MONTHS_UPTO_1YEAR) {
		R72_OVER_6MONTHS_UPTO_1YEAR = r72_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR72_OVER_1YEAR_UPTO_3YEARS() {
		return R72_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR72_OVER_1YEAR_UPTO_3YEARS(BigDecimal r72_OVER_1YEAR_UPTO_3YEARS) {
		R72_OVER_1YEAR_UPTO_3YEARS = r72_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR72_OVER_3YEARS_UPTO_5YEARS() {
		return R72_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR72_OVER_3YEARS_UPTO_5YEARS(BigDecimal r72_OVER_3YEARS_UPTO_5YEARS) {
		R72_OVER_3YEARS_UPTO_5YEARS = r72_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR72_OVER_5YEARS() {
		return R72_OVER_5YEARS;
	}
	public void setR72_OVER_5YEARS(BigDecimal r72_OVER_5YEARS) {
		R72_OVER_5YEARS = r72_OVER_5YEARS;
	}
	public BigDecimal getR72_TOTAL() {
		return R72_TOTAL;
	}
	public void setR72_TOTAL(BigDecimal r72_TOTAL) {
		R72_TOTAL = r72_TOTAL;
	}
	public BigDecimal getR73_1_DAY() {
		return R73_1_DAY;
	}
	public void setR73_1_DAY(BigDecimal r73_1_DAY) {
		R73_1_DAY = r73_1_DAY;
	}
	public BigDecimal getR73_2TO7_DAYS() {
		return R73_2TO7_DAYS;
	}
	public void setR73_2TO7_DAYS(BigDecimal r73_2to7_DAYS) {
		R73_2TO7_DAYS = r73_2to7_DAYS;
	}
	public BigDecimal getR73_8TO14_DAYS() {
		return R73_8TO14_DAYS;
	}
	public void setR73_8TO14_DAYS(BigDecimal r73_8to14_DAYS) {
		R73_8TO14_DAYS = r73_8to14_DAYS;
	}
	public BigDecimal getR73_15TO30_DAYS() {
		return R73_15TO30_DAYS;
	}
	public void setR73_15TO30_DAYS(BigDecimal r73_15to30_DAYS) {
		R73_15TO30_DAYS = r73_15to30_DAYS;
	}
	public BigDecimal getR73_31DAYS_UPTO_2MONTHS() {
		return R73_31DAYS_UPTO_2MONTHS;
	}
	public void setR73_31DAYS_UPTO_2MONTHS(BigDecimal r73_31days_UPTO_2MONTHS) {
		R73_31DAYS_UPTO_2MONTHS = r73_31days_UPTO_2MONTHS;
	}
	public BigDecimal getR73_MORETHAN_2MONTHS_UPTO_3MONHTS() {
		return R73_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public void setR73_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r73_MORETHAN_2MONTHS_UPTO_3MONHTS) {
		R73_MORETHAN_2MONTHS_UPTO_3MONHTS = r73_MORETHAN_2MONTHS_UPTO_3MONHTS;
	}
	public BigDecimal getR73_OVER_3MONTHS_UPTO_6MONTHS() {
		return R73_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public void setR73_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r73_OVER_3MONTHS_UPTO_6MONTHS) {
		R73_OVER_3MONTHS_UPTO_6MONTHS = r73_OVER_3MONTHS_UPTO_6MONTHS;
	}
	public BigDecimal getR73_OVER_6MONTHS_UPTO_1YEAR() {
		return R73_OVER_6MONTHS_UPTO_1YEAR;
	}
	public void setR73_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r73_OVER_6MONTHS_UPTO_1YEAR) {
		R73_OVER_6MONTHS_UPTO_1YEAR = r73_OVER_6MONTHS_UPTO_1YEAR;
	}
	public BigDecimal getR73_OVER_1YEAR_UPTO_3YEARS() {
		return R73_OVER_1YEAR_UPTO_3YEARS;
	}
	public void setR73_OVER_1YEAR_UPTO_3YEARS(BigDecimal r73_OVER_1YEAR_UPTO_3YEARS) {
		R73_OVER_1YEAR_UPTO_3YEARS = r73_OVER_1YEAR_UPTO_3YEARS;
	}
	public BigDecimal getR73_OVER_3YEARS_UPTO_5YEARS() {
		return R73_OVER_3YEARS_UPTO_5YEARS;
	}
	public void setR73_OVER_3YEARS_UPTO_5YEARS(BigDecimal r73_OVER_3YEARS_UPTO_5YEARS) {
		R73_OVER_3YEARS_UPTO_5YEARS = r73_OVER_3YEARS_UPTO_5YEARS;
	}
	public BigDecimal getR73_OVER_5YEARS() {
		return R73_OVER_5YEARS;
	}
	public void setR73_OVER_5YEARS(BigDecimal r73_OVER_5YEARS) {
		R73_OVER_5YEARS = r73_OVER_5YEARS;
	}
	public BigDecimal getR73_TOTAL() {
		return R73_TOTAL;
	}
	public void setR73_TOTAL(BigDecimal r73_TOTAL) {
		R73_TOTAL = r73_TOTAL;
	}
	
	
	
}
	


// =====================================================
// DETAIL ENTITY  CPR_STRUCT_LIQ
// =====================================================	

public class CPR_STRUCT_LIQ_Detail_RowMapper implements RowMapper<CPR_STRUCT_LIQ_Detail_Entity> {

    @Override
    public CPR_STRUCT_LIQ_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        CPR_STRUCT_LIQ_Detail_Entity obj = new CPR_STRUCT_LIQ_Detail_Entity();

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

public class CPR_STRUCT_LIQ_Detail_Entity {

   
	
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


public class CPR_STRUCT_LIQ_Archival_Detail_RowMapper 
        implements RowMapper<CPR_STRUCT_LIQ_Archival_Detail_Entity> {

    @Override
    public CPR_STRUCT_LIQ_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        CPR_STRUCT_LIQ_Archival_Detail_Entity obj = new CPR_STRUCT_LIQ_Archival_Detail_Entity();

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

public class CPR_STRUCT_LIQ_Archival_Detail_Entity {

   
	
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
// MODEL AND VIEW METHOD summary CPR_STRUCT_LIQ
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
		 		 	 public ModelAndView getCPR_STRUCT_LIQView(

	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("CPR_STRUCT_LIQ View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    // =====================================================
	    // ARCHIVAL MODE
	    // =====================================================

	    if ("ARCHIVAL".equals(type) && version != null) {

	        List<CPR_STRUCT_LIQ_Archival_Summary_Entity> T1Master = new ArrayList<>();
	    

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

	        List<CPR_STRUCT_LIQ_Summary_Entity> T1Master = new ArrayList<>();
	       

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

	    mv.setViewName("BRRS/CPR_STRUCT_LIQ");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getCPR_STRUCT_LIQcurrentDtl(
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

	            List<CPR_STRUCT_LIQ_Archival_Detail_Entity> archivalDetailList;

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

	            List<CPR_STRUCT_LIQ_Detail_Entity> currentDetailList;

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

	    mv.setViewName("BRRS/CPR_STRUCT_LIQ");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
	
//=====================================================
// MODEL AND VIEW METHOD ARCHIVAL VIEW
//=====================================================

public List<Object[]> getCPR_STRUCT_LIQArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<CPR_STRUCT_LIQ_Archival_Summary_Entity> repoData =
						getarchivaldatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (CPR_STRUCT_LIQ_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					CPR_STRUCT_LIQ_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  CPR_STRUCT_LIQ  Archival data: " + e.getMessage());
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
		ModelAndView mv = new ModelAndView("BRRS/CPR_STRUCT_LIQ"); 

		if (acctNo != null) {
			CPR_STRUCT_LIQ_Detail_Entity fsiEntity = findByDetailAcctnumber(acctNo);
			if (fsiEntity != null && fsiEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(fsiEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", fsiEntity);
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

			CPR_STRUCT_LIQ_Detail_Entity existing = findByDetailAcctnumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}
			
			 // Create old copy for audit comparison
			CPR_STRUCT_LIQ_Detail_Entity oldcopy = new CPR_STRUCT_LIQ_Detail_Entity();
	        BeanUtils.copyProperties(existing, oldcopy);

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
    "UPDATE BRRS_CPR_STRUCT_LIQ_DETAILTABLE " +
    "SET ACCT_NAME = ?, " +
    "ACCT_BALANCE_IN_PULA = ?, " +
   
    "WHERE ACCT_NUMBER = ?";

		           jdbcTemplate.update(
    sql,
    existing.getAcctName(),
    existing.getAcctBalanceInpula(),
  
    existing.getAcctNumber()
);
		           
		           // Audit comparison
		            auditService.compareEntitiesmanual(
		                    oldcopy,
		                    existing,
		                    acctNo,
		                    "CPR_STRUCT_LIQ Detail Screen",
		                    "BRRS_CPR_STRUCT_LIQ_DETAILTABLE"
		            );

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_CPR_STRUCT_LIQ_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_CPR_STRUCT_LIQ_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating CPR_STRUCT_LIQ  record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
//=====================================================
// DETAIL EXCEL 
//=====================================================


	public byte[] getCPR_STRUCT_LIQDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  CPR_STRUCT_LIQ  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getCPR_STRUCT_LIQDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("CPR_STRUCT_LIQ Details ");

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
				List<CPR_STRUCT_LIQ_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (CPR_STRUCT_LIQ_Detail_Entity item : reportData) { 
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
					logger.info("No data found for CPR_STRUCT_LIQ — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating CPR_STRUCT_LIQ Excel", e);
				return new byte[0];
			}
		}
	
	
//=====================================================
// ARCHIVAL DETAIL EXCEL 
//=====================================================

	public byte[] getCPR_STRUCT_LIQDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for CPR_STRUCT_LIQ ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("CPR_STRUCT_LIQ Detail NEW");

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
				List<CPR_STRUCT_LIQ_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (CPR_STRUCT_LIQ_Archival_Detail_Entity item : reportData) {
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
					logger.info("No data found for CPR_STRUCT_LIQ — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating CPR_STRUCT_LIQ NEW Excel", e);
				return new byte[0];
			}
		}
		
		
//=====================================================
// Summary EXCEL 
//=====================================================

	public byte[] getCPR_STRUCT_LIQExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.CPR_STRUCT_LIQ");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelCPR_STRUCT_LIQARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<CPR_STRUCT_LIQ_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		
		
			System.out.println("DATA SIZE IS : "+dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  CPR_STRUCT_LIQ report. Returning empty result.");
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

						int startRow = 7;
						
				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						CPR_STRUCT_LIQ_Summary_Entity record = dataList.get(i);
					
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						
					Cell cell2 = row.createCell(2);
if (record.getR8_1_DAY() != null) {
   cell2.setCellValue(record.getR8_1_DAY().doubleValue());
   cell2.setCellStyle(numberStyle);
} else {
   cell2.setCellValue("");
   cell2.setCellStyle(textStyle);
}

Cell cell3 = row.createCell(3);
if (record.getR8_2TO7_DAYS() != null) {
   cell3.setCellValue(record.getR8_2TO7_DAYS().doubleValue());
   cell3.setCellStyle(numberStyle);
} else {
   cell3.setCellValue("");
   cell3.setCellStyle(textStyle);
}

Cell cell4 = row.createCell(4);
if (record.getR8_8TO14_DAYS() != null) {
   cell4.setCellValue(record.getR8_8TO14_DAYS().doubleValue());
   cell4.setCellStyle(numberStyle);
} else {
   cell4.setCellValue("");
   cell4.setCellStyle(textStyle);
}

Cell cell5 = row.createCell(5);
if (record.getR8_15TO30_DAYS() != null) {
   cell5.setCellValue(record.getR8_15TO30_DAYS().doubleValue());
   cell5.setCellStyle(numberStyle);
} else {
   cell5.setCellValue("");
   cell5.setCellStyle(textStyle);
}

Cell cell6 = row.createCell(6);
if (record.getR8_31DAYS_UPTO_2MONTHS() != null) {
   cell6.setCellValue(record.getR8_31DAYS_UPTO_2MONTHS().doubleValue());
   cell6.setCellStyle(numberStyle);
} else {
   cell6.setCellValue("");
   cell6.setCellStyle(textStyle);
}

Cell cell7 = row.createCell(7);
if (record.getR8_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
   cell7.setCellValue(record.getR8_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
   cell7.setCellStyle(numberStyle);
} else {
   cell7.setCellValue("");
   cell7.setCellStyle(textStyle);
}

Cell cell8 = row.createCell(8);
if (record.getR8_OVER_3MONTHS_UPTO_6MONTHS() != null) {
   cell8.setCellValue(record.getR8_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
   cell8.setCellStyle(numberStyle);
} else {
   cell8.setCellValue("");
   cell8.setCellStyle(textStyle);
}

Cell cell9 = row.createCell(9);
if (record.getR8_OVER_6MONTHS_UPTO_1YEAR() != null) {
   cell9.setCellValue(record.getR8_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
   cell9.setCellStyle(numberStyle);
} else {
   cell9.setCellValue("");
   cell9.setCellStyle(textStyle);
}

Cell cell10 = row.createCell(10);
if (record.getR8_OVER_1YEAR_UPTO_3YEARS() != null) {
   cell10.setCellValue(record.getR8_OVER_1YEAR_UPTO_3YEARS().doubleValue());
   cell10.setCellStyle(numberStyle);
} else {
   cell10.setCellValue("");
   cell10.setCellStyle(textStyle);
}

Cell cell11 = row.createCell(11);
if (record.getR8_OVER_3YEARS_UPTO_5YEARS() != null) {
   cell11.setCellValue(record.getR8_OVER_3YEARS_UPTO_5YEARS().doubleValue());
   cell11.setCellStyle(numberStyle);
} else {
   cell11.setCellValue("");
   cell2.setCellStyle(textStyle);
}

Cell cell12 = row.createCell(12);
if (record.getR8_OVER_3YEARS_UPTO_5YEARS() != null) {
   cell12.setCellValue(record.getR8_OVER_3YEARS_UPTO_5YEARS().doubleValue());
   cell12.setCellStyle(numberStyle);
} else {
   cell12.setCellValue("");
   cell12.setCellStyle(textStyle);
}

//-------- R9 --------
row = sheet.getRow(8);
cell2 = row.createCell(2);
if (record.getR9_1_DAY() != null) {
cell2.setCellValue(record.getR9_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR9_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR9_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR9_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR9_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR9_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR9_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR9_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR9_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR9_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR9_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR9_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR9_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR9_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR9_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR9_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR9_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR9_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR9_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR9_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR9_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R10 --------
row = sheet.getRow(9);
cell2 = row.createCell(2);
if (record.getR10_1_DAY() != null) {
cell2.setCellValue(record.getR10_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR10_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR10_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR10_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR10_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR10_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR10_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR10_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR10_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR10_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR10_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR10_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR10_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR10_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR10_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR10_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR10_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR10_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR10_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR10_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR10_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R11 --------
row = sheet.getRow(10);
cell2 = row.createCell(2);
if (record.getR11_1_DAY() != null) {
cell2.setCellValue(record.getR11_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR11_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR11_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR11_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR11_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR11_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR11_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR11_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR11_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR11_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR11_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR11_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR11_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR11_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR11_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR11_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR11_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR11_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR11_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR11_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR11_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R12 --------
row = sheet.getRow(11);
cell2 = row.createCell(2);
if (record.getR12_1_DAY() != null) {
cell2.setCellValue(record.getR12_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR12_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR12_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR12_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR12_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR12_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR12_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR12_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR12_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR12_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR12_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR12_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR12_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR12_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR12_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR12_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR12_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR12_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR12_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR12_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR12_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R13 --------
row = sheet.getRow(12);
cell2 = row.createCell(2);
if (record.getR13_1_DAY() != null) {
cell2.setCellValue(record.getR13_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR13_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR13_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR13_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR13_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR13_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR13_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR13_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR13_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR13_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR13_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR13_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR13_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR13_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR13_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR13_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR13_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR13_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR13_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR13_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR13_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R14 --------
row = sheet.getRow(13);
cell2 = row.createCell(2);
if (record.getR14_1_DAY() != null) {
cell2.setCellValue(record.getR14_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR14_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR14_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR14_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR14_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR14_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR14_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR14_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR14_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR14_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR14_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR14_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR14_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR14_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR14_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR14_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR14_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR14_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR14_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR14_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR14_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R15 --------
row = sheet.getRow(14);
cell2 = row.createCell(2);
if (record.getR15_1_DAY() != null) {
cell2.setCellValue(record.getR15_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR15_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR15_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR15_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR15_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR15_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR15_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR15_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR15_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR15_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR15_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR15_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR15_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR15_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR15_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR15_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR15_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR15_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR15_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR15_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR15_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R16 --------
row = sheet.getRow(15);
cell2 = row.createCell(2);
if (record.getR16_1_DAY() != null) {
cell2.setCellValue(record.getR16_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR16_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR16_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR16_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR16_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR16_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR16_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR16_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR16_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR16_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR16_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR16_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR16_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR16_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR16_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR16_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR16_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR16_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR16_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR16_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR16_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R17 --------
row = sheet.getRow(16);
cell2 = row.createCell(2);
if (record.getR17_1_DAY() != null) {
cell2.setCellValue(record.getR17_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR17_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR17_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR17_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR17_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR17_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR17_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR17_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR17_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR17_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR17_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR17_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR17_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR17_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR17_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR17_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR17_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR17_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR17_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR17_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR17_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R18 --------
row = sheet.getRow(17);
cell2 = row.createCell(2);
if (record.getR18_1_DAY() != null) {
cell2.setCellValue(record.getR18_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR18_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR18_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR18_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR18_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR18_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR18_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR18_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR18_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR18_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR18_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR18_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR18_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR18_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR18_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR18_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR18_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR18_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR18_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR18_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR18_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R19 --------
row = sheet.getRow(18);
cell2 = row.createCell(2);
if (record.getR19_1_DAY() != null) {
cell2.setCellValue(record.getR19_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR19_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR19_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR19_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR19_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR19_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR19_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR19_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR19_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR19_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR19_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR19_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR19_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR19_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR19_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR19_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR19_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR19_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR19_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR19_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR19_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R20 --------
row = sheet.getRow(19);
cell2 = row.createCell(2);
if (record.getR20_1_DAY() != null) {
cell2.setCellValue(record.getR20_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR20_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR20_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR20_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR20_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR20_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR20_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR20_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR20_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR20_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR20_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR20_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR20_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR20_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR20_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR20_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR20_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR20_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR20_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR20_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR20_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R21 --------
row = sheet.getRow(20);
cell2 = row.createCell(2);
if (record.getR21_1_DAY() != null) {
cell2.setCellValue(record.getR21_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR21_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR21_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR21_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR21_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR21_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR21_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR21_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR21_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR21_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR21_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR21_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR21_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR21_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR21_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR21_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR21_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR21_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR21_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR21_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR21_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R22 --------
row = sheet.getRow(21);
cell2 = row.createCell(2);
if (record.getR22_1_DAY() != null) {
cell2.setCellValue(record.getR22_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR22_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR22_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR22_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR22_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR22_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR22_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR22_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR22_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR22_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR22_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR22_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR22_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR22_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR22_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR22_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR22_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR22_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR22_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR22_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR22_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R23 --------
row = sheet.getRow(22);
cell2 = row.createCell(2);
if (record.getR23_1_DAY() != null) {
cell2.setCellValue(record.getR23_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR23_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR23_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR23_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR23_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR23_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR23_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR23_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR23_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR23_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR23_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR23_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR23_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR23_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR23_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR23_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR23_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR23_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR23_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR23_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR23_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R24 --------
row = sheet.getRow(23);
cell2 = row.createCell(2);
if (record.getR24_1_DAY() != null) {
cell2.setCellValue(record.getR24_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR24_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR24_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR24_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR24_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR24_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR24_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR24_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR24_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR24_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR24_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR24_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR24_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR24_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR24_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR24_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR24_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR24_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR24_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR24_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR24_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R25 --------
row = sheet.getRow(24);
cell2 = row.createCell(2);
if (record.getR25_1_DAY() != null) {
cell2.setCellValue(record.getR25_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR25_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR25_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR25_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR25_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR25_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR25_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR25_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR25_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR25_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR25_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR25_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR25_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR25_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR25_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR25_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR25_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR25_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR25_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR25_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR25_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R26 --------
row = sheet.getRow(25);
cell2 = row.createCell(2);
if (record.getR26_1_DAY() != null) {
cell2.setCellValue(record.getR26_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR26_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR26_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR26_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR26_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR26_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR26_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR26_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR26_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR26_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR26_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR26_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR26_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR26_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR26_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR26_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR26_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR26_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR26_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR26_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR26_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R27 --------
row = sheet.getRow(26);
cell2 = row.createCell(2);
if (record.getR27_1_DAY() != null) {
cell2.setCellValue(record.getR27_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR27_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR27_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR27_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR27_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR27_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR27_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR27_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR27_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR27_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR27_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR27_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR27_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR27_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR27_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR27_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR27_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR27_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR27_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR27_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR27_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R28 --------
row = sheet.getRow(27);
cell2 = row.createCell(2);
if (record.getR28_1_DAY() != null) {
cell2.setCellValue(record.getR28_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR28_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR28_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR28_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR28_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR28_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR28_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR28_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR28_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR28_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR28_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR28_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR28_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR28_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR28_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR28_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR28_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR28_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR28_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR28_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR28_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R29 --------
row = sheet.getRow(28);
cell2 = row.createCell(2);
if (record.getR29_1_DAY() != null) {
cell2.setCellValue(record.getR29_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR29_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR29_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR29_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR29_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR29_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR29_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR29_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR29_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR29_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR29_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR29_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR29_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR29_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR29_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR29_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR29_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR29_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR29_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR29_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR29_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R30 --------
row = sheet.getRow(29);
cell2 = row.createCell(2);
if (record.getR30_1_DAY() != null) {
cell2.setCellValue(record.getR30_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR30_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR30_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR30_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR30_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR30_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR30_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR30_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR30_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR30_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR30_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR30_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR30_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR30_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR30_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR30_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR30_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR30_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR30_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR30_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR30_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R31 --------
row = sheet.getRow(30);
cell2 = row.createCell(2);
if (record.getR31_1_DAY() != null) {
cell2.setCellValue(record.getR31_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR31_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR31_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR31_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR31_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR31_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR31_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR31_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR31_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR31_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR31_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR31_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR31_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR31_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR31_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR31_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR31_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR31_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR31_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR31_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR31_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R32 --------
row = sheet.getRow(31);
cell2 = row.createCell(2);
if (record.getR32_1_DAY() != null) {
cell2.setCellValue(record.getR32_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR32_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR32_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR32_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR32_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR32_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR32_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR32_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR32_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR32_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR32_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR32_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR32_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR32_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR32_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR32_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR32_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR32_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR32_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR32_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR32_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R33 --------
row = sheet.getRow(32);
cell2 = row.createCell(2);
if (record.getR33_1_DAY() != null) {
cell2.setCellValue(record.getR33_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR33_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR33_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR33_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR33_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR33_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR33_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR33_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR33_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR33_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR33_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR33_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR33_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR33_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR33_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR33_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR33_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR33_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR33_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR33_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR33_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R34 --------
row = sheet.getRow(33);
cell2 = row.createCell(2);
if (record.getR34_1_DAY() != null) {
cell2.setCellValue(record.getR34_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR34_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR34_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR34_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR34_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR34_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR34_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR34_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR34_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR34_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR34_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR34_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR34_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR34_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR34_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR34_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR34_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR34_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR34_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR34_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR34_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R35 --------
row = sheet.getRow(34);
cell2 = row.createCell(2);
if (record.getR35_1_DAY() != null) {
cell2.setCellValue(record.getR35_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR35_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR35_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR35_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR35_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR35_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR35_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR35_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR35_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR35_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR35_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR35_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR35_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR35_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR35_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR35_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR35_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR35_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR35_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR35_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR35_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R36 --------
row = sheet.getRow(35);
cell2 = row.createCell(2);
if (record.getR36_1_DAY() != null) {
cell2.setCellValue(record.getR36_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR36_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR36_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR36_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR36_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR36_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR36_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR36_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR36_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR36_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR36_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR36_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR36_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR36_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR36_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR36_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR36_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR36_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR36_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR36_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR36_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}

//-------- R46 --------
row = sheet.getRow(45);
cell2 = row.createCell(2);
if (record.getR46_1_DAY() != null) {
cell2.setCellValue(record.getR46_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR46_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR46_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR46_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR46_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR46_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR46_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR46_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR46_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR46_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR46_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR46_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR46_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR46_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR46_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR46_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR46_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR46_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR46_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR46_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR46_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R47 --------
row = sheet.getRow(46);
cell2 = row.createCell(2);
if (record.getR47_1_DAY() != null) {
cell2.setCellValue(record.getR47_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR47_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR47_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR47_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR47_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR47_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR47_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR47_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR47_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR47_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR47_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR47_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR47_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR47_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR47_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR47_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR47_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR47_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR47_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR47_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR47_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R48 --------
row = sheet.getRow(47);
cell2 = row.createCell(2);
if (record.getR48_1_DAY() != null) {
cell2.setCellValue(record.getR48_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR48_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR48_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR48_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR48_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR48_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR48_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR48_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR48_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR48_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR48_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR48_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR48_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR48_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR48_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR48_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR48_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR48_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR48_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR48_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR48_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R49 --------
row = sheet.getRow(48);
cell2 = row.createCell(2);
if (record.getR49_1_DAY() != null) {
cell2.setCellValue(record.getR49_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR49_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR49_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR49_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR49_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR49_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR49_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR49_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR49_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR49_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR49_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR49_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR49_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR49_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR49_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR49_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR49_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR49_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR49_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR49_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR49_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R50 --------
row = sheet.getRow(49);
cell2 = row.createCell(2);
if (record.getR50_1_DAY() != null) {
cell2.setCellValue(record.getR50_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR50_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR50_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR50_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR50_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR50_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR50_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR50_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR50_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR50_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR50_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR50_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR50_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR50_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR50_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR50_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR50_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR50_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR50_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR50_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR50_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R51 --------
row = sheet.getRow(50);
cell2 = row.createCell(2);
if (record.getR51_1_DAY() != null) {
cell2.setCellValue(record.getR51_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR51_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR51_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR51_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR51_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR51_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR51_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR51_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR51_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR51_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR51_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR51_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR51_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR51_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR51_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR51_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR51_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR51_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR51_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR51_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR51_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R52 --------
row = sheet.getRow(51);
cell2 = row.createCell(2);
if (record.getR52_1_DAY() != null) {
cell2.setCellValue(record.getR52_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR52_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR52_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR52_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR52_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR52_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR52_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR52_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR52_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR52_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR52_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR52_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR52_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR52_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR52_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR52_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR52_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR52_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR52_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR52_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR52_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R53 --------
row = sheet.getRow(52);
cell2 = row.createCell(2);
if (record.getR53_1_DAY() != null) {
cell2.setCellValue(record.getR53_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR53_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR53_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR53_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR53_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR53_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR53_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR53_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR53_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR53_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR53_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR53_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR53_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR53_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR53_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR53_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR53_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR53_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR53_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR53_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR53_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R54 --------
row = sheet.getRow(53);
cell2 = row.createCell(2);
if (record.getR54_1_DAY() != null) {
cell2.setCellValue(record.getR54_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR54_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR54_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR54_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR54_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR54_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR54_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR54_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR54_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR54_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR54_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR54_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR54_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR54_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR54_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR54_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR54_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR54_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR54_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR54_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR54_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R55 --------
row = sheet.getRow(54);
cell2 = row.createCell(2);
if (record.getR55_1_DAY() != null) {
cell2.setCellValue(record.getR55_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR55_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR55_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR55_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR55_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR55_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR55_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR55_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR55_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR55_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR55_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR55_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR55_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR55_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR55_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR55_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR55_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR55_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR55_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR55_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR55_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R56 --------
row = sheet.getRow(55);
cell2 = row.createCell(2);
if (record.getR56_1_DAY() != null) {
cell2.setCellValue(record.getR56_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR56_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR56_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR56_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR56_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR56_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR56_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR56_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR56_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR56_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR56_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR56_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR56_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR56_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR56_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR56_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR56_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR56_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR56_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR56_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR56_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R57 --------
row = sheet.getRow(56);
cell2 = row.createCell(2);
if (record.getR57_1_DAY() != null) {
cell2.setCellValue(record.getR57_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR57_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR57_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR57_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR57_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR57_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR57_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR57_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR57_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR57_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR57_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR57_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR57_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR57_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR57_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR57_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR57_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR57_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR57_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR57_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR57_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R58 --------
row = sheet.getRow(57);
cell2 = row.createCell(2);
if (record.getR58_1_DAY() != null) {
cell2.setCellValue(record.getR58_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR58_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR58_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR58_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR58_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR58_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR58_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR58_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR58_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR58_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR58_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR58_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR58_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR58_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR58_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR58_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR58_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR58_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR58_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR58_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR58_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R59 --------
row = sheet.getRow(58);
cell2 = row.createCell(2);
if (record.getR59_1_DAY() != null) {
cell2.setCellValue(record.getR59_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR59_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR59_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR59_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR59_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR59_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR59_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR59_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR59_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR59_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR59_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR59_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR59_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR59_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR59_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR59_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR59_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR59_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR59_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR59_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR59_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R60 --------
row = sheet.getRow(59);
cell2 = row.createCell(2);
if (record.getR60_1_DAY() != null) {
cell2.setCellValue(record.getR60_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR60_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR60_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR60_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR60_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR60_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR60_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR60_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR60_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR60_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR60_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR60_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR60_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR60_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR60_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR60_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR60_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR60_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR60_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR60_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR60_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R61 --------
row = sheet.getRow(60);
cell2 = row.createCell(2);
if (record.getR61_1_DAY() != null) {
cell2.setCellValue(record.getR61_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR61_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR61_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR61_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR61_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR61_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR61_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR61_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR61_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR61_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR61_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR61_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR61_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR61_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR61_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR61_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR61_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR61_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR61_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR61_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR61_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R62 --------
row = sheet.getRow(61);
cell2 = row.createCell(2);
if (record.getR62_1_DAY() != null) {
cell2.setCellValue(record.getR62_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR62_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR62_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR62_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR62_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR62_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR62_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR62_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR62_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR62_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR62_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR62_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR62_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR62_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR62_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR62_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR62_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR62_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR62_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR62_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR62_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R63 --------
row = sheet.getRow(62);
cell2 = row.createCell(2);
if (record.getR63_1_DAY() != null) {
cell2.setCellValue(record.getR63_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR63_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR63_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR63_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR63_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR63_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR63_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR63_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR63_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR63_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR63_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR63_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR63_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR63_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR63_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR63_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR63_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR63_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR63_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR63_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR63_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R64 --------
row = sheet.getRow(63);
cell2 = row.createCell(2);
if (record.getR64_1_DAY() != null) {
cell2.setCellValue(record.getR64_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR64_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR64_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR64_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR64_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR64_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR64_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR64_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR64_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR64_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR64_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR64_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR64_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR64_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR64_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR64_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR64_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR64_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR64_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR64_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR64_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R65 --------
row = sheet.getRow(64);
cell2 = row.createCell(2);
if (record.getR65_1_DAY() != null) {
cell2.setCellValue(record.getR65_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR65_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR65_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR65_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR65_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR65_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR65_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR65_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR65_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR65_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR65_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR65_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR65_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR65_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR65_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR65_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR65_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR65_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR65_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR65_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR65_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R66 --------
row = sheet.getRow(65);
cell2 = row.createCell(2);
if (record.getR66_1_DAY() != null) {
cell2.setCellValue(record.getR66_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR66_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR66_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR66_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR66_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR66_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR66_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR66_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR66_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR66_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR66_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR66_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR66_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR66_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR66_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR66_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR66_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR66_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR66_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR66_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR66_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R67 --------
row = sheet.getRow(66);
cell2 = row.createCell(2);
if (record.getR67_1_DAY() != null) {
cell2.setCellValue(record.getR67_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR67_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR67_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR67_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR67_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR67_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR67_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR67_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR67_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR67_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR67_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR67_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR67_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR67_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR67_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR67_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR67_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR67_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR67_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR67_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR67_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R68 --------
row = sheet.getRow(67);
cell2 = row.createCell(2);
if (record.getR68_1_DAY() != null) {
cell2.setCellValue(record.getR68_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR68_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR68_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR68_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR68_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR68_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR68_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR68_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR68_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR68_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR68_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR68_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR68_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR68_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR68_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR68_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR68_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR68_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR68_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR68_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR68_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R69 --------
row = sheet.getRow(68);
cell2 = row.createCell(2);
if (record.getR69_1_DAY() != null) {
cell2.setCellValue(record.getR69_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR69_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR69_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR69_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR69_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR69_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR69_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR69_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR69_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR69_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR69_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR69_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR69_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR69_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR69_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR69_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR69_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR69_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR69_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR69_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR69_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R70 --------
row = sheet.getRow(69);
cell2 = row.createCell(2);
if (record.getR70_1_DAY() != null) {
cell2.setCellValue(record.getR70_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR70_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR70_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR70_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR70_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR70_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR70_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR70_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR70_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR70_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR70_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR70_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR70_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR70_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR70_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR70_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR70_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR70_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR70_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR70_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR70_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R71 --------
row = sheet.getRow(70);
cell2 = row.createCell(2);
if (record.getR71_1_DAY() != null) {
cell2.setCellValue(record.getR71_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR71_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR71_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR71_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR71_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR71_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR71_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR71_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR71_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR71_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR71_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR71_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR71_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR71_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR71_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR71_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR71_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR71_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR71_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR71_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR71_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R72 --------
row = sheet.getRow(71);
cell2 = row.createCell(2);
if (record.getR72_1_DAY() != null) {
cell2.setCellValue(record.getR72_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR72_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR72_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR72_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR72_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR72_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR72_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR72_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR72_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR72_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR72_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR72_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR72_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR72_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR72_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR72_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR72_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR72_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR72_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR72_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR72_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}
		      
				
					}
					
				workbook.setForceFormulaRecalculation(true);

					
				} else {

				}

	// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
				
				// audit service summary format

				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
											if (attrs != null) {
												HttpServletRequest request = attrs.getRequest();
												String userid = (String) request.getSession().getAttribute("USERID");
												auditService.createBusinessAudit(userid, "DOWNLOAD", "CPR_STRUCT_LIQ  SUMMARY", null, "BRRS_CPR_STRUCT_LIQ_SUMMARYTABLE");
											}

				return out.toByteArray();
			}

		}




//=====================================================
//ARCHIVAL SUMMARY EXCEL 
//=====================================================



				public byte[] getExcelCPR_STRUCT_LIQARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {	

			}

			List<CPR_STRUCT_LIQ_Archival_Summary_Entity> dataList = 
					getDataByDateListArchival(dateformat.parse(todate), version);
		
	    

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for CPR_STRUCT_LIQ new report. Returning empty result.");
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

				int startRow = 7;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						CPR_STRUCT_LIQ_Archival_Summary_Entity record = dataList.get(i);
					    
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


	    Cell cell2 = row.createCell(2);
if (record.getR8_1_DAY() != null) {
   cell2.setCellValue(record.getR8_1_DAY().doubleValue());
   cell2.setCellStyle(numberStyle);
} else {
   cell2.setCellValue("");
   cell2.setCellStyle(textStyle);
}

Cell cell3 = row.createCell(3);
if (record.getR8_2TO7_DAYS() != null) {
   cell3.setCellValue(record.getR8_2TO7_DAYS().doubleValue());
   cell3.setCellStyle(numberStyle);
} else {
   cell3.setCellValue("");
   cell3.setCellStyle(textStyle);
}

Cell cell4 = row.createCell(4);
if (record.getR8_8TO14_DAYS() != null) {
   cell4.setCellValue(record.getR8_8TO14_DAYS().doubleValue());
   cell4.setCellStyle(numberStyle);
} else {
   cell4.setCellValue("");
   cell4.setCellStyle(textStyle);
}

Cell cell5 = row.createCell(5);
if (record.getR8_15TO30_DAYS() != null) {
   cell5.setCellValue(record.getR8_15TO30_DAYS().doubleValue());
   cell5.setCellStyle(numberStyle);
} else {
   cell5.setCellValue("");
   cell5.setCellStyle(textStyle);
}

Cell cell6 = row.createCell(6);
if (record.getR8_31DAYS_UPTO_2MONTHS() != null) {
   cell6.setCellValue(record.getR8_31DAYS_UPTO_2MONTHS().doubleValue());
   cell6.setCellStyle(numberStyle);
} else {
   cell6.setCellValue("");
   cell6.setCellStyle(textStyle);
}

Cell cell7 = row.createCell(7);
if (record.getR8_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
   cell7.setCellValue(record.getR8_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
   cell7.setCellStyle(numberStyle);
} else {
   cell7.setCellValue("");
   cell7.setCellStyle(textStyle);
}

Cell cell8 = row.createCell(8);
if (record.getR8_OVER_3MONTHS_UPTO_6MONTHS() != null) {
   cell8.setCellValue(record.getR8_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
   cell8.setCellStyle(numberStyle);
} else {
   cell8.setCellValue("");
   cell8.setCellStyle(textStyle);
}

Cell cell9 = row.createCell(9);
if (record.getR8_OVER_6MONTHS_UPTO_1YEAR() != null) {
   cell9.setCellValue(record.getR8_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
   cell9.setCellStyle(numberStyle);
} else {
   cell9.setCellValue("");
   cell9.setCellStyle(textStyle);
}

Cell cell10 = row.createCell(10);
if (record.getR8_OVER_1YEAR_UPTO_3YEARS() != null) {
   cell10.setCellValue(record.getR8_OVER_1YEAR_UPTO_3YEARS().doubleValue());
   cell10.setCellStyle(numberStyle);
} else {
   cell10.setCellValue("");
   cell10.setCellStyle(textStyle);
}

Cell cell11 = row.createCell(11);
if (record.getR8_OVER_3YEARS_UPTO_5YEARS() != null) {
   cell11.setCellValue(record.getR8_OVER_3YEARS_UPTO_5YEARS().doubleValue());
   cell11.setCellStyle(numberStyle);
} else {
   cell11.setCellValue("");
   cell2.setCellStyle(textStyle);
}

Cell cell12 = row.createCell(12);
if (record.getR8_OVER_3YEARS_UPTO_5YEARS() != null) {
   cell12.setCellValue(record.getR8_OVER_3YEARS_UPTO_5YEARS().doubleValue());
   cell12.setCellStyle(numberStyle);
} else {
   cell12.setCellValue("");
   cell12.setCellStyle(textStyle);
}

//-------- R9 --------
row = sheet.getRow(8);
cell2 = row.createCell(2);
if (record.getR9_1_DAY() != null) {
cell2.setCellValue(record.getR9_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR9_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR9_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR9_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR9_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR9_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR9_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR9_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR9_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR9_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR9_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR9_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR9_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR9_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR9_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR9_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR9_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR9_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR9_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR9_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR9_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R10 --------
row = sheet.getRow(9);
cell2 = row.createCell(2);
if (record.getR10_1_DAY() != null) {
cell2.setCellValue(record.getR10_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR10_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR10_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR10_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR10_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR10_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR10_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR10_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR10_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR10_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR10_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR10_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR10_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR10_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR10_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR10_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR10_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR10_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR10_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR10_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR10_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R11 --------
row = sheet.getRow(10);
cell2 = row.createCell(2);
if (record.getR11_1_DAY() != null) {
cell2.setCellValue(record.getR11_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR11_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR11_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR11_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR11_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR11_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR11_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR11_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR11_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR11_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR11_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR11_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR11_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR11_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR11_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR11_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR11_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR11_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR11_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR11_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR11_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R12 --------
row = sheet.getRow(11);
cell2 = row.createCell(2);
if (record.getR12_1_DAY() != null) {
cell2.setCellValue(record.getR12_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR12_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR12_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR12_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR12_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR12_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR12_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR12_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR12_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR12_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR12_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR12_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR12_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR12_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR12_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR12_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR12_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR12_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR12_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR12_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR12_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R13 --------
row = sheet.getRow(12);
cell2 = row.createCell(2);
if (record.getR13_1_DAY() != null) {
cell2.setCellValue(record.getR13_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR13_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR13_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR13_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR13_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR13_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR13_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR13_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR13_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR13_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR13_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR13_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR13_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR13_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR13_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR13_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR13_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR13_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR13_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR13_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR13_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R14 --------
row = sheet.getRow(13);
cell2 = row.createCell(2);
if (record.getR14_1_DAY() != null) {
cell2.setCellValue(record.getR14_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR14_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR14_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR14_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR14_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR14_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR14_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR14_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR14_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR14_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR14_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR14_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR14_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR14_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR14_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR14_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR14_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR14_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR14_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR14_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR14_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R15 --------
row = sheet.getRow(14);
cell2 = row.createCell(2);
if (record.getR15_1_DAY() != null) {
cell2.setCellValue(record.getR15_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR15_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR15_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR15_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR15_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR15_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR15_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR15_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR15_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR15_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR15_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR15_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR15_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR15_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR15_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR15_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR15_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR15_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR15_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR15_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR15_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R16 --------
row = sheet.getRow(15);
cell2 = row.createCell(2);
if (record.getR16_1_DAY() != null) {
cell2.setCellValue(record.getR16_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR16_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR16_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR16_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR16_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR16_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR16_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR16_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR16_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR16_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR16_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR16_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR16_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR16_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR16_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR16_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR16_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR16_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR16_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR16_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR16_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R17 --------
row = sheet.getRow(16);
cell2 = row.createCell(2);
if (record.getR17_1_DAY() != null) {
cell2.setCellValue(record.getR17_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR17_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR17_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR17_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR17_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR17_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR17_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR17_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR17_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR17_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR17_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR17_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR17_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR17_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR17_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR17_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR17_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR17_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR17_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR17_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR17_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R18 --------
row = sheet.getRow(17);
cell2 = row.createCell(2);
if (record.getR18_1_DAY() != null) {
cell2.setCellValue(record.getR18_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR18_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR18_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR18_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR18_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR18_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR18_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR18_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR18_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR18_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR18_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR18_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR18_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR18_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR18_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR18_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR18_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR18_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR18_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR18_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR18_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R19 --------
row = sheet.getRow(18);
cell2 = row.createCell(2);
if (record.getR19_1_DAY() != null) {
cell2.setCellValue(record.getR19_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR19_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR19_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR19_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR19_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR19_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR19_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR19_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR19_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR19_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR19_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR19_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR19_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR19_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR19_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR19_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR19_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR19_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR19_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR19_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR19_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R20 --------
row = sheet.getRow(19);
cell2 = row.createCell(2);
if (record.getR20_1_DAY() != null) {
cell2.setCellValue(record.getR20_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR20_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR20_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR20_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR20_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR20_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR20_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR20_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR20_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR20_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR20_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR20_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR20_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR20_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR20_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR20_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR20_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR20_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR20_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR20_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR20_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R21 --------
row = sheet.getRow(20);
cell2 = row.createCell(2);
if (record.getR21_1_DAY() != null) {
cell2.setCellValue(record.getR21_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR21_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR21_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR21_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR21_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR21_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR21_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR21_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR21_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR21_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR21_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR21_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR21_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR21_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR21_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR21_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR21_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR21_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR21_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR21_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR21_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R22 --------
row = sheet.getRow(21);
cell2 = row.createCell(2);
if (record.getR22_1_DAY() != null) {
cell2.setCellValue(record.getR22_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR22_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR22_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR22_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR22_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR22_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR22_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR22_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR22_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR22_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR22_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR22_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR22_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR22_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR22_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR22_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR22_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR22_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR22_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR22_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR22_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R23 --------
row = sheet.getRow(22);
cell2 = row.createCell(2);
if (record.getR23_1_DAY() != null) {
cell2.setCellValue(record.getR23_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR23_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR23_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR23_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR23_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR23_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR23_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR23_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR23_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR23_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR23_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR23_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR23_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR23_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR23_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR23_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR23_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR23_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR23_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR23_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR23_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R24 --------
row = sheet.getRow(23);
cell2 = row.createCell(2);
if (record.getR24_1_DAY() != null) {
cell2.setCellValue(record.getR24_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR24_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR24_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR24_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR24_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR24_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR24_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR24_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR24_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR24_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR24_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR24_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR24_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR24_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR24_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR24_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR24_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR24_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR24_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR24_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR24_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R25 --------
row = sheet.getRow(24);
cell2 = row.createCell(2);
if (record.getR25_1_DAY() != null) {
cell2.setCellValue(record.getR25_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR25_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR25_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR25_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR25_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR25_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR25_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR25_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR25_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR25_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR25_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR25_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR25_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR25_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR25_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR25_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR25_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR25_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR25_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR25_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR25_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R26 --------
row = sheet.getRow(25);
cell2 = row.createCell(2);
if (record.getR26_1_DAY() != null) {
cell2.setCellValue(record.getR26_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR26_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR26_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR26_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR26_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR26_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR26_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR26_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR26_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR26_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR26_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR26_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR26_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR26_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR26_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR26_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR26_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR26_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR26_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR26_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR26_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R27 --------
row = sheet.getRow(26);
cell2 = row.createCell(2);
if (record.getR27_1_DAY() != null) {
cell2.setCellValue(record.getR27_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR27_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR27_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR27_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR27_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR27_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR27_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR27_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR27_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR27_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR27_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR27_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR27_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR27_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR27_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR27_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR27_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR27_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR27_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR27_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR27_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R28 --------
row = sheet.getRow(27);
cell2 = row.createCell(2);
if (record.getR28_1_DAY() != null) {
cell2.setCellValue(record.getR28_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR28_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR28_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR28_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR28_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR28_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR28_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR28_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR28_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR28_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR28_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR28_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR28_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR28_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR28_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR28_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR28_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR28_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR28_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR28_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR28_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R29 --------
row = sheet.getRow(28);
cell2 = row.createCell(2);
if (record.getR29_1_DAY() != null) {
cell2.setCellValue(record.getR29_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR29_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR29_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR29_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR29_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR29_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR29_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR29_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR29_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR29_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR29_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR29_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR29_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR29_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR29_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR29_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR29_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR29_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR29_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR29_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR29_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R30 --------
row = sheet.getRow(29);
cell2 = row.createCell(2);
if (record.getR30_1_DAY() != null) {
cell2.setCellValue(record.getR30_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR30_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR30_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR30_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR30_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR30_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR30_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR30_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR30_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR30_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR30_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR30_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR30_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR30_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR30_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR30_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR30_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR30_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR30_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR30_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR30_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R31 --------
row = sheet.getRow(30);
cell2 = row.createCell(2);
if (record.getR31_1_DAY() != null) {
cell2.setCellValue(record.getR31_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR31_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR31_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR31_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR31_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR31_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR31_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR31_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR31_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR31_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR31_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR31_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR31_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR31_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR31_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR31_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR31_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR31_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR31_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR31_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR31_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R32 --------
row = sheet.getRow(31);
cell2 = row.createCell(2);
if (record.getR32_1_DAY() != null) {
cell2.setCellValue(record.getR32_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR32_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR32_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR32_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR32_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR32_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR32_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR32_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR32_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR32_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR32_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR32_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR32_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR32_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR32_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR32_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR32_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR32_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR32_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR32_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR32_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R33 --------
row = sheet.getRow(32);
cell2 = row.createCell(2);
if (record.getR33_1_DAY() != null) {
cell2.setCellValue(record.getR33_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR33_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR33_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR33_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR33_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR33_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR33_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR33_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR33_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR33_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR33_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR33_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR33_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR33_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR33_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR33_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR33_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR33_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR33_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR33_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR33_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R34 --------
row = sheet.getRow(33);
cell2 = row.createCell(2);
if (record.getR34_1_DAY() != null) {
cell2.setCellValue(record.getR34_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR34_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR34_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR34_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR34_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR34_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR34_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR34_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR34_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR34_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR34_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR34_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR34_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR34_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR34_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR34_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR34_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR34_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR34_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR34_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR34_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R35 --------
row = sheet.getRow(34);
cell2 = row.createCell(2);
if (record.getR35_1_DAY() != null) {
cell2.setCellValue(record.getR35_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR35_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR35_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR35_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR35_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR35_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR35_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR35_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR35_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR35_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR35_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR35_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR35_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR35_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR35_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR35_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR35_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR35_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR35_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR35_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR35_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R36 --------
row = sheet.getRow(35);
cell2 = row.createCell(2);
if (record.getR36_1_DAY() != null) {
cell2.setCellValue(record.getR36_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR36_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR36_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR36_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR36_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR36_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR36_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR36_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR36_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR36_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR36_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR36_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR36_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR36_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR36_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR36_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR36_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR36_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR36_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR36_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR36_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}

//-------- R46 --------
row = sheet.getRow(45);
cell2 = row.createCell(2);
if (record.getR46_1_DAY() != null) {
cell2.setCellValue(record.getR46_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR46_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR46_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR46_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR46_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR46_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR46_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR46_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR46_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR46_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR46_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR46_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR46_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR46_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR46_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR46_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR46_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR46_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR46_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR46_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR46_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R47 --------
row = sheet.getRow(46);
cell2 = row.createCell(2);
if (record.getR47_1_DAY() != null) {
cell2.setCellValue(record.getR47_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR47_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR47_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR47_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR47_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR47_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR47_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR47_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR47_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR47_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR47_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR47_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR47_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR47_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR47_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR47_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR47_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR47_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR47_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR47_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR47_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R48 --------
row = sheet.getRow(47);
cell2 = row.createCell(2);
if (record.getR48_1_DAY() != null) {
cell2.setCellValue(record.getR48_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR48_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR48_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR48_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR48_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR48_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR48_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR48_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR48_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR48_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR48_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR48_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR48_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR48_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR48_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR48_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR48_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR48_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR48_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR48_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR48_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R49 --------
row = sheet.getRow(48);
cell2 = row.createCell(2);
if (record.getR49_1_DAY() != null) {
cell2.setCellValue(record.getR49_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR49_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR49_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR49_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR49_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR49_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR49_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR49_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR49_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR49_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR49_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR49_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR49_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR49_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR49_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR49_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR49_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR49_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR49_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR49_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR49_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R50 --------
row = sheet.getRow(49);
cell2 = row.createCell(2);
if (record.getR50_1_DAY() != null) {
cell2.setCellValue(record.getR50_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR50_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR50_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR50_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR50_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR50_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR50_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR50_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR50_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR50_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR50_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR50_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR50_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR50_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR50_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR50_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR50_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR50_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR50_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR50_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR50_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R51 --------
row = sheet.getRow(50);
cell2 = row.createCell(2);
if (record.getR51_1_DAY() != null) {
cell2.setCellValue(record.getR51_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR51_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR51_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR51_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR51_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR51_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR51_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR51_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR51_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR51_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR51_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR51_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR51_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR51_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR51_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR51_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR51_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR51_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR51_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR51_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR51_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R52 --------
row = sheet.getRow(51);
cell2 = row.createCell(2);
if (record.getR52_1_DAY() != null) {
cell2.setCellValue(record.getR52_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR52_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR52_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR52_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR52_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR52_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR52_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR52_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR52_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR52_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR52_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR52_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR52_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR52_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR52_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR52_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR52_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR52_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR52_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR52_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR52_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R53 --------
row = sheet.getRow(52);
cell2 = row.createCell(2);
if (record.getR53_1_DAY() != null) {
cell2.setCellValue(record.getR53_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR53_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR53_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR53_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR53_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR53_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR53_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR53_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR53_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR53_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR53_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR53_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR53_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR53_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR53_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR53_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR53_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR53_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR53_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR53_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR53_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R54 --------
row = sheet.getRow(53);
cell2 = row.createCell(2);
if (record.getR54_1_DAY() != null) {
cell2.setCellValue(record.getR54_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR54_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR54_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR54_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR54_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR54_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR54_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR54_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR54_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR54_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR54_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR54_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR54_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR54_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR54_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR54_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR54_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR54_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR54_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR54_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR54_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R55 --------
row = sheet.getRow(54);
cell2 = row.createCell(2);
if (record.getR55_1_DAY() != null) {
cell2.setCellValue(record.getR55_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR55_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR55_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR55_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR55_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR55_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR55_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR55_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR55_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR55_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR55_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR55_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR55_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR55_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR55_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR55_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR55_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR55_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR55_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR55_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR55_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R56 --------
row = sheet.getRow(55);
cell2 = row.createCell(2);
if (record.getR56_1_DAY() != null) {
cell2.setCellValue(record.getR56_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR56_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR56_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR56_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR56_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR56_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR56_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR56_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR56_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR56_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR56_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR56_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR56_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR56_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR56_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR56_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR56_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR56_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR56_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR56_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR56_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R57 --------
row = sheet.getRow(56);
cell2 = row.createCell(2);
if (record.getR57_1_DAY() != null) {
cell2.setCellValue(record.getR57_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR57_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR57_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR57_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR57_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR57_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR57_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR57_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR57_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR57_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR57_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR57_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR57_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR57_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR57_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR57_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR57_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR57_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR57_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR57_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR57_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R58 --------
row = sheet.getRow(57);
cell2 = row.createCell(2);
if (record.getR58_1_DAY() != null) {
cell2.setCellValue(record.getR58_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR58_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR58_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR58_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR58_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR58_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR58_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR58_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR58_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR58_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR58_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR58_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR58_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR58_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR58_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR58_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR58_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR58_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR58_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR58_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR58_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R59 --------
row = sheet.getRow(58);
cell2 = row.createCell(2);
if (record.getR59_1_DAY() != null) {
cell2.setCellValue(record.getR59_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR59_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR59_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR59_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR59_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR59_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR59_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR59_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR59_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR59_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR59_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR59_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR59_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR59_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR59_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR59_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR59_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR59_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR59_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR59_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR59_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R60 --------
row = sheet.getRow(59);
cell2 = row.createCell(2);
if (record.getR60_1_DAY() != null) {
cell2.setCellValue(record.getR60_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR60_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR60_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR60_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR60_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR60_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR60_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR60_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR60_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR60_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR60_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR60_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR60_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR60_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR60_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR60_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR60_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR60_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR60_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR60_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR60_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R61 --------
row = sheet.getRow(60);
cell2 = row.createCell(2);
if (record.getR61_1_DAY() != null) {
cell2.setCellValue(record.getR61_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR61_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR61_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR61_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR61_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR61_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR61_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR61_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR61_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR61_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR61_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR61_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR61_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR61_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR61_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR61_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR61_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR61_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR61_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR61_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR61_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R62 --------
row = sheet.getRow(61);
cell2 = row.createCell(2);
if (record.getR62_1_DAY() != null) {
cell2.setCellValue(record.getR62_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR62_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR62_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR62_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR62_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR62_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR62_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR62_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR62_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR62_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR62_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR62_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR62_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR62_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR62_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR62_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR62_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR62_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR62_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR62_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR62_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R63 --------
row = sheet.getRow(62);
cell2 = row.createCell(2);
if (record.getR63_1_DAY() != null) {
cell2.setCellValue(record.getR63_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR63_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR63_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR63_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR63_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR63_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR63_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR63_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR63_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR63_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR63_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR63_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR63_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR63_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR63_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR63_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR63_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR63_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR63_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR63_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR63_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R64 --------
row = sheet.getRow(63);
cell2 = row.createCell(2);
if (record.getR64_1_DAY() != null) {
cell2.setCellValue(record.getR64_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR64_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR64_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR64_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR64_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR64_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR64_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR64_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR64_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR64_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR64_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR64_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR64_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR64_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR64_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR64_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR64_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR64_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR64_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR64_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR64_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R65 --------
row = sheet.getRow(64);
cell2 = row.createCell(2);
if (record.getR65_1_DAY() != null) {
cell2.setCellValue(record.getR65_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR65_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR65_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR65_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR65_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR65_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR65_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR65_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR65_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR65_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR65_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR65_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR65_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR65_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR65_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR65_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR65_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR65_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR65_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR65_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR65_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R66 --------
row = sheet.getRow(65);
cell2 = row.createCell(2);
if (record.getR66_1_DAY() != null) {
cell2.setCellValue(record.getR66_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR66_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR66_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR66_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR66_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR66_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR66_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR66_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR66_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR66_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR66_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR66_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR66_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR66_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR66_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR66_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR66_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR66_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR66_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR66_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR66_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R67 --------
row = sheet.getRow(66);
cell2 = row.createCell(2);
if (record.getR67_1_DAY() != null) {
cell2.setCellValue(record.getR67_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR67_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR67_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR67_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR67_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR67_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR67_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR67_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR67_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR67_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR67_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR67_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR67_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR67_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR67_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR67_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR67_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR67_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR67_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR67_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR67_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R68 --------
row = sheet.getRow(67);
cell2 = row.createCell(2);
if (record.getR68_1_DAY() != null) {
cell2.setCellValue(record.getR68_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR68_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR68_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR68_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR68_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR68_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR68_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR68_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR68_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR68_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR68_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR68_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR68_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR68_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR68_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR68_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR68_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR68_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR68_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR68_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR68_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R69 --------
row = sheet.getRow(68);
cell2 = row.createCell(2);
if (record.getR69_1_DAY() != null) {
cell2.setCellValue(record.getR69_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR69_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR69_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR69_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR69_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR69_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR69_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR69_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR69_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR69_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR69_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR69_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR69_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR69_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR69_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR69_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR69_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR69_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR69_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR69_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR69_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R70 --------
row = sheet.getRow(69);
cell2 = row.createCell(2);
if (record.getR70_1_DAY() != null) {
cell2.setCellValue(record.getR70_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR70_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR70_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR70_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR70_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR70_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR70_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR70_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR70_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR70_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR70_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR70_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR70_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR70_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR70_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR70_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR70_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR70_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR70_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR70_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR70_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R71 --------
row = sheet.getRow(70);
cell2 = row.createCell(2);
if (record.getR71_1_DAY() != null) {
cell2.setCellValue(record.getR71_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR71_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR71_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR71_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR71_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR71_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR71_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR71_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR71_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR71_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR71_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR71_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR71_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR71_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR71_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR71_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR71_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR71_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR71_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR71_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR71_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R72 --------
row = sheet.getRow(71);
cell2 = row.createCell(2);
if (record.getR72_1_DAY() != null) {
cell2.setCellValue(record.getR72_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR72_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR72_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR72_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR72_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR72_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR72_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR72_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR72_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR72_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR72_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR72_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR72_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR72_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR72_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR72_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR72_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR72_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR72_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR72_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR72_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}
						
						
					}
                  workbook.setForceFormulaRecalculation(true);
					
				} else {

				}

	// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
				
				
				// audit service archival summary format

				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
									if (attrs != null) {
										HttpServletRequest request = attrs.getRequest();
										String userid = (String) request.getSession().getAttribute("USERID");
										auditService.createBusinessAudit(userid, "DOWNLOAD", "CPR_STRUCT_LIQ ARCHIVAL SUMMARY", null, "BRRS_CPR_STRUCT_LIQ_ARCHIVALTABLE_SUMMARY");
									}

				return out.toByteArray();
			}

		}
		
		
		
	}