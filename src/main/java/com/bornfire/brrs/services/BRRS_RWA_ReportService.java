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

public class BRRS_RWA_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_RWA_ReportService.class);
	
	
	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

  
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;
	
	
	// Fetch data by report date - RWA Summary
public List<RWA_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_RWA_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new RWA_Summary_RowMapper()   // make sure this exists
    );
}

// ARCHIVAL 

public List<Object[]> getRWA_Archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_RWA_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<RWA_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_RWA_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new RWA_Archival_RowMapper()
    );
}


public List<RWA_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_RWA_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new RWA_Archival_RowMapper()
    );
}

// DETAIL

public List<RWA_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_RWA_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new RWA_Detail_RowMapper()
    );
}

public List<RWA_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_RWA_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, offset, limit},
            new RWA_Detail_RowMapper()
    );
}

public int getDataCount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_RWA_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            Integer.class
    );
}

public List<RWA_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate) {

    String sql = "SELECT * FROM BRRS_RWA_DETAILTABLE " +
                 "WHERE REPORT_LABLE = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate},
            new RWA_Detail_RowMapper()
    );
}

public RWA_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_RWA_DETAILTABLE WHERE ACCT_NUMBER = ?";

    List<RWA_Detail_Entity> list = jdbcTemplate.query(
            sql,
            new Object[]{acctNumber},
            new RWA_Detail_RowMapper()
    );

    return list.isEmpty() ? null : list.get(0);
}

//ARCHIVAL DETAIL 

public List<RWA_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_RWA_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, dataEntryVersion},
            new RWA_Archival_Detail_RowMapper()
    );
}


public List<RWA_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_RWA_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_LABLE = ? " +   // keep if DB has typo
                 "AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ? " +
                 "AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate, dataEntryVersion},
            new RWA_Archival_Detail_RowMapper()
    );
}


// SUMAMRY ENTITY CLASS


public class RWA_Summary_RowMapper implements RowMapper<RWA_Summary_Entity> {

    @Override
    public RWA_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        RWA_Summary_Entity obj = new RWA_Summary_Entity();

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
        // R8 → R15
        // =========================
        obj.setR8_BOOK_VALUE(rs.getBigDecimal("R8_BOOK_VALUE"));
        obj.setR8_MARGINS(rs.getBigDecimal("R8_MARGINS"));
        obj.setR8_BOOK_VALUE_NET(rs.getBigDecimal("R8_BOOK_VALUE_NET"));
        obj.setR8_RW(rs.getBigDecimal("R8_RW"));
        obj.setR8_RISK_VALUE(rs.getBigDecimal("R8_RISK_VALUE"));

        obj.setR9_BOOK_VALUE(rs.getBigDecimal("R9_BOOK_VALUE"));
        obj.setR9_MARGINS(rs.getBigDecimal("R9_MARGINS"));
        obj.setR9_BOOK_VALUE_NET(rs.getBigDecimal("R9_BOOK_VALUE_NET"));
        obj.setR9_RW(rs.getBigDecimal("R9_RW"));
        obj.setR9_RISK_VALUE(rs.getBigDecimal("R9_RISK_VALUE"));

        obj.setR10_BOOK_VALUE(rs.getBigDecimal("R10_BOOK_VALUE"));
        obj.setR10_MARGINS(rs.getBigDecimal("R10_MARGINS"));
        obj.setR10_BOOK_VALUE_NET(rs.getBigDecimal("R10_BOOK_VALUE_NET"));
        obj.setR10_RW(rs.getBigDecimal("R10_RW"));
        obj.setR10_RISK_VALUE(rs.getBigDecimal("R10_RISK_VALUE"));

        obj.setR11_BOOK_VALUE(rs.getBigDecimal("R11_BOOK_VALUE"));
        obj.setR11_MARGINS(rs.getBigDecimal("R11_MARGINS"));
        obj.setR11_BOOK_VALUE_NET(rs.getBigDecimal("R11_BOOK_VALUE_NET"));
        obj.setR11_RW(rs.getBigDecimal("R11_RW"));
        obj.setR11_RISK_VALUE(rs.getBigDecimal("R11_RISK_VALUE"));

        obj.setR12_BOOK_VALUE(rs.getBigDecimal("R12_BOOK_VALUE"));
        obj.setR12_MARGINS(rs.getBigDecimal("R12_MARGINS"));
        obj.setR12_BOOK_VALUE_NET(rs.getBigDecimal("R12_BOOK_VALUE_NET"));
        obj.setR12_RW(rs.getBigDecimal("R12_RW"));
        obj.setR12_RISK_VALUE(rs.getBigDecimal("R12_RISK_VALUE"));

        obj.setR13_BOOK_VALUE(rs.getBigDecimal("R13_BOOK_VALUE"));
        obj.setR13_MARGINS(rs.getBigDecimal("R13_MARGINS"));
        obj.setR13_BOOK_VALUE_NET(rs.getBigDecimal("R13_BOOK_VALUE_NET"));
        obj.setR13_RW(rs.getBigDecimal("R13_RW"));
        obj.setR13_RISK_VALUE(rs.getBigDecimal("R13_RISK_VALUE"));

        obj.setR14_BOOK_VALUE(rs.getBigDecimal("R14_BOOK_VALUE"));
        obj.setR14_MARGINS(rs.getBigDecimal("R14_MARGINS"));
        obj.setR14_BOOK_VALUE_NET(rs.getBigDecimal("R14_BOOK_VALUE_NET"));
        obj.setR14_RW(rs.getBigDecimal("R14_RW"));
        obj.setR14_RISK_VALUE(rs.getBigDecimal("R14_RISK_VALUE"));

        obj.setR15_BOOK_VALUE(rs.getBigDecimal("R15_BOOK_VALUE"));
        obj.setR15_MARGINS(rs.getBigDecimal("R15_MARGINS"));
        obj.setR15_BOOK_VALUE_NET(rs.getBigDecimal("R15_BOOK_VALUE_NET"));
        obj.setR15_RW(rs.getBigDecimal("R15_RW"));
        obj.setR15_RISK_VALUE(rs.getBigDecimal("R15_RISK_VALUE"));

// =========================
// R16
// =========================
obj.setR16_BOOK_VALUE(rs.getBigDecimal("R16_BOOK_VALUE"));
obj.setR16_MARGINS(rs.getBigDecimal("R16_MARGINS"));
obj.setR16_BOOK_VALUE_NET(rs.getBigDecimal("R16_BOOK_VALUE_NET"));
obj.setR16_RW(rs.getBigDecimal("R16_RW"));
obj.setR16_RISK_VALUE(rs.getBigDecimal("R16_RISK_VALUE"));


// =========================
// R17
// =========================
obj.setR17_BOOK_VALUE(rs.getBigDecimal("R17_BOOK_VALUE"));
obj.setR17_MARGINS(rs.getBigDecimal("R17_MARGINS"));
obj.setR17_BOOK_VALUE_NET(rs.getBigDecimal("R17_BOOK_VALUE_NET"));
obj.setR17_RW(rs.getBigDecimal("R17_RW"));
obj.setR17_RISK_VALUE(rs.getBigDecimal("R17_RISK_VALUE"));

// =========================
// R18
// =========================
obj.setR18_BOOK_VALUE(rs.getBigDecimal("R18_BOOK_VALUE"));
obj.setR18_MARGINS(rs.getBigDecimal("R18_MARGINS"));
obj.setR18_BOOK_VALUE_NET(rs.getBigDecimal("R18_BOOK_VALUE_NET"));
obj.setR18_RW(rs.getBigDecimal("R18_RW"));
obj.setR18_RISK_VALUE(rs.getBigDecimal("R18_RISK_VALUE"));

// =========================
// R19
// =========================
obj.setR19_BOOK_VALUE(rs.getBigDecimal("R19_BOOK_VALUE"));
obj.setR19_MARGINS(rs.getBigDecimal("R19_MARGINS"));
obj.setR19_BOOK_VALUE_NET(rs.getBigDecimal("R19_BOOK_VALUE_NET"));
obj.setR19_RW(rs.getBigDecimal("R19_RW"));
obj.setR19_RISK_VALUE(rs.getBigDecimal("R19_RISK_VALUE"));

// =========================
// R20
// =========================
obj.setR20_BOOK_VALUE(rs.getBigDecimal("R20_BOOK_VALUE"));
obj.setR20_MARGINS(rs.getBigDecimal("R20_MARGINS"));
obj.setR20_BOOK_VALUE_NET(rs.getBigDecimal("R20_BOOK_VALUE_NET"));
obj.setR20_RW(rs.getBigDecimal("R20_RW"));
obj.setR20_RISK_VALUE(rs.getBigDecimal("R20_RISK_VALUE"));

// =========================
// R21 → R30
// =========================
obj.setR21_BOOK_VALUE(rs.getBigDecimal("R21_BOOK_VALUE"));
obj.setR21_MARGINS(rs.getBigDecimal("R21_MARGINS"));
obj.setR21_BOOK_VALUE_NET(rs.getBigDecimal("R21_BOOK_VALUE_NET"));
obj.setR21_RW(rs.getBigDecimal("R21_RW"));
obj.setR21_RISK_VALUE(rs.getBigDecimal("R21_RISK_VALUE"));

obj.setR22_BOOK_VALUE(rs.getBigDecimal("R22_BOOK_VALUE"));
obj.setR22_MARGINS(rs.getBigDecimal("R22_MARGINS"));
obj.setR22_BOOK_VALUE_NET(rs.getBigDecimal("R22_BOOK_VALUE_NET"));
obj.setR22_RW(rs.getBigDecimal("R22_RW"));
obj.setR22_RISK_VALUE(rs.getBigDecimal("R22_RISK_VALUE"));

obj.setR23_BOOK_VALUE(rs.getBigDecimal("R23_BOOK_VALUE"));
obj.setR23_MARGINS(rs.getBigDecimal("R23_MARGINS"));
obj.setR23_BOOK_VALUE_NET(rs.getBigDecimal("R23_BOOK_VALUE_NET"));
obj.setR23_RW(rs.getBigDecimal("R23_RW"));
obj.setR23_RISK_VALUE(rs.getBigDecimal("R23_RISK_VALUE"));

obj.setR24_BOOK_VALUE(rs.getBigDecimal("R24_BOOK_VALUE"));
obj.setR24_MARGINS(rs.getBigDecimal("R24_MARGINS"));
obj.setR24_BOOK_VALUE_NET(rs.getBigDecimal("R24_BOOK_VALUE_NET"));
obj.setR24_RW(rs.getBigDecimal("R24_RW"));
obj.setR24_RISK_VALUE(rs.getBigDecimal("R24_RISK_VALUE"));

obj.setR25_BOOK_VALUE(rs.getBigDecimal("R25_BOOK_VALUE"));
obj.setR25_MARGINS(rs.getBigDecimal("R25_MARGINS"));
obj.setR25_BOOK_VALUE_NET(rs.getBigDecimal("R25_BOOK_VALUE_NET"));
obj.setR25_RW(rs.getBigDecimal("R25_RW"));
obj.setR25_RISK_VALUE(rs.getBigDecimal("R25_RISK_VALUE"));

obj.setR26_BOOK_VALUE(rs.getBigDecimal("R26_BOOK_VALUE"));
obj.setR26_MARGINS(rs.getBigDecimal("R26_MARGINS"));
obj.setR26_BOOK_VALUE_NET(rs.getBigDecimal("R26_BOOK_VALUE_NET"));
obj.setR26_RW(rs.getBigDecimal("R26_RW"));
obj.setR26_RISK_VALUE(rs.getBigDecimal("R26_RISK_VALUE"));

obj.setR27_BOOK_VALUE(rs.getBigDecimal("R27_BOOK_VALUE"));
obj.setR27_MARGINS(rs.getBigDecimal("R27_MARGINS"));
obj.setR27_BOOK_VALUE_NET(rs.getBigDecimal("R27_BOOK_VALUE_NET"));
obj.setR27_RW(rs.getBigDecimal("R27_RW"));
obj.setR27_RISK_VALUE(rs.getBigDecimal("R27_RISK_VALUE"));

obj.setR28_BOOK_VALUE(rs.getBigDecimal("R28_BOOK_VALUE"));
obj.setR28_MARGINS(rs.getBigDecimal("R28_MARGINS"));
obj.setR28_BOOK_VALUE_NET(rs.getBigDecimal("R28_BOOK_VALUE_NET"));
obj.setR28_RW(rs.getBigDecimal("R28_RW"));
obj.setR28_RISK_VALUE(rs.getBigDecimal("R28_RISK_VALUE"));

obj.setR29_BOOK_VALUE(rs.getBigDecimal("R29_BOOK_VALUE"));
obj.setR29_MARGINS(rs.getBigDecimal("R29_MARGINS"));
obj.setR29_BOOK_VALUE_NET(rs.getBigDecimal("R29_BOOK_VALUE_NET"));
obj.setR29_RW(rs.getBigDecimal("R29_RW"));
obj.setR29_RISK_VALUE(rs.getBigDecimal("R29_RISK_VALUE"));

obj.setR30_BOOK_VALUE(rs.getBigDecimal("R30_BOOK_VALUE"));
obj.setR30_MARGINS(rs.getBigDecimal("R30_MARGINS"));
obj.setR30_BOOK_VALUE_NET(rs.getBigDecimal("R30_BOOK_VALUE_NET"));
obj.setR30_RW(rs.getBigDecimal("R30_RW"));
obj.setR30_RISK_VALUE(rs.getBigDecimal("R30_RISK_VALUE"));


// =========================
// R31 → R40
// =========================
obj.setR31_BOOK_VALUE(rs.getBigDecimal("R31_BOOK_VALUE"));
obj.setR31_MARGINS(rs.getBigDecimal("R31_MARGINS"));
obj.setR31_BOOK_VALUE_NET(rs.getBigDecimal("R31_BOOK_VALUE_NET"));
obj.setR31_RW(rs.getBigDecimal("R31_RW"));
obj.setR31_RISK_VALUE(rs.getBigDecimal("R31_RISK_VALUE"));

obj.setR32_BOOK_VALUE(rs.getBigDecimal("R32_BOOK_VALUE"));
obj.setR32_MARGINS(rs.getBigDecimal("R32_MARGINS"));
obj.setR32_BOOK_VALUE_NET(rs.getBigDecimal("R32_BOOK_VALUE_NET"));
obj.setR32_RW(rs.getBigDecimal("R32_RW"));
obj.setR32_RISK_VALUE(rs.getBigDecimal("R32_RISK_VALUE"));

obj.setR33_BOOK_VALUE(rs.getBigDecimal("R33_BOOK_VALUE"));
obj.setR33_MARGINS(rs.getBigDecimal("R33_MARGINS"));
obj.setR33_BOOK_VALUE_NET(rs.getBigDecimal("R33_BOOK_VALUE_NET"));
obj.setR33_RW(rs.getBigDecimal("R33_RW"));
obj.setR33_RISK_VALUE(rs.getBigDecimal("R33_RISK_VALUE"));

obj.setR34_BOOK_VALUE(rs.getBigDecimal("R34_BOOK_VALUE"));
obj.setR34_MARGINS(rs.getBigDecimal("R34_MARGINS"));
obj.setR34_BOOK_VALUE_NET(rs.getBigDecimal("R34_BOOK_VALUE_NET"));
obj.setR34_RW(rs.getBigDecimal("R34_RW"));
obj.setR34_RISK_VALUE(rs.getBigDecimal("R34_RISK_VALUE"));

obj.setR35_BOOK_VALUE(rs.getBigDecimal("R35_BOOK_VALUE"));
obj.setR35_MARGINS(rs.getBigDecimal("R35_MARGINS"));
obj.setR35_BOOK_VALUE_NET(rs.getBigDecimal("R35_BOOK_VALUE_NET"));
obj.setR35_RW(rs.getBigDecimal("R35_RW"));
obj.setR35_RISK_VALUE(rs.getBigDecimal("R35_RISK_VALUE"));

obj.setR36_BOOK_VALUE(rs.getBigDecimal("R36_BOOK_VALUE"));
obj.setR36_MARGINS(rs.getBigDecimal("R36_MARGINS"));
obj.setR36_BOOK_VALUE_NET(rs.getBigDecimal("R36_BOOK_VALUE_NET"));
obj.setR36_RW(rs.getBigDecimal("R36_RW"));
obj.setR36_RISK_VALUE(rs.getBigDecimal("R36_RISK_VALUE"));

obj.setR37_BOOK_VALUE(rs.getBigDecimal("R37_BOOK_VALUE"));
obj.setR37_MARGINS(rs.getBigDecimal("R37_MARGINS"));
obj.setR37_BOOK_VALUE_NET(rs.getBigDecimal("R37_BOOK_VALUE_NET"));
obj.setR37_RW(rs.getBigDecimal("R37_RW"));
obj.setR37_RISK_VALUE(rs.getBigDecimal("R37_RISK_VALUE"));

obj.setR38_BOOK_VALUE(rs.getBigDecimal("R38_BOOK_VALUE"));
obj.setR38_MARGINS(rs.getBigDecimal("R38_MARGINS"));
obj.setR38_BOOK_VALUE_NET(rs.getBigDecimal("R38_BOOK_VALUE_NET"));
obj.setR38_RW(rs.getBigDecimal("R38_RW"));
obj.setR38_RISK_VALUE(rs.getBigDecimal("R38_RISK_VALUE"));

obj.setR39_BOOK_VALUE(rs.getBigDecimal("R39_BOOK_VALUE"));
obj.setR39_MARGINS(rs.getBigDecimal("R39_MARGINS"));
obj.setR39_BOOK_VALUE_NET(rs.getBigDecimal("R39_BOOK_VALUE_NET"));
obj.setR39_RW(rs.getBigDecimal("R39_RW"));
obj.setR39_RISK_VALUE(rs.getBigDecimal("R39_RISK_VALUE"));

obj.setR40_BOOK_VALUE(rs.getBigDecimal("R40_BOOK_VALUE"));
obj.setR40_MARGINS(rs.getBigDecimal("R40_MARGINS"));
obj.setR40_BOOK_VALUE_NET(rs.getBigDecimal("R40_BOOK_VALUE_NET"));
obj.setR40_RW(rs.getBigDecimal("R40_RW"));
obj.setR40_RISK_VALUE(rs.getBigDecimal("R40_RISK_VALUE"));

// =========================
// R41 → R48
// =========================
obj.setR41_BOOK_VALUE(rs.getBigDecimal("R41_BOOK_VALUE"));
obj.setR41_MARGINS(rs.getBigDecimal("R41_MARGINS"));
obj.setR41_BOOK_VALUE_NET(rs.getBigDecimal("R41_BOOK_VALUE_NET"));
obj.setR41_RW(rs.getBigDecimal("R41_RW"));
obj.setR41_RISK_VALUE(rs.getBigDecimal("R41_RISK_VALUE"));

obj.setR42_BOOK_VALUE(rs.getBigDecimal("R42_BOOK_VALUE"));
obj.setR42_MARGINS(rs.getBigDecimal("R42_MARGINS"));
obj.setR42_BOOK_VALUE_NET(rs.getBigDecimal("R42_BOOK_VALUE_NET"));
obj.setR42_RW(rs.getBigDecimal("R42_RW"));
obj.setR42_RISK_VALUE(rs.getBigDecimal("R42_RISK_VALUE"));

obj.setR43_BOOK_VALUE(rs.getBigDecimal("R43_BOOK_VALUE"));
obj.setR43_MARGINS(rs.getBigDecimal("R43_MARGINS"));
obj.setR43_BOOK_VALUE_NET(rs.getBigDecimal("R43_BOOK_VALUE_NET"));
obj.setR43_RW(rs.getBigDecimal("R43_RW"));
obj.setR43_RISK_VALUE(rs.getBigDecimal("R43_RISK_VALUE"));

obj.setR44_BOOK_VALUE(rs.getBigDecimal("R44_BOOK_VALUE"));
obj.setR44_MARGINS(rs.getBigDecimal("R44_MARGINS"));
obj.setR44_BOOK_VALUE_NET(rs.getBigDecimal("R44_BOOK_VALUE_NET"));
obj.setR44_RW(rs.getBigDecimal("R44_RW"));
obj.setR44_RISK_VALUE(rs.getBigDecimal("R44_RISK_VALUE"));

obj.setR45_BOOK_VALUE(rs.getBigDecimal("R45_BOOK_VALUE"));
obj.setR45_MARGINS(rs.getBigDecimal("R45_MARGINS"));
obj.setR45_BOOK_VALUE_NET(rs.getBigDecimal("R45_BOOK_VALUE_NET"));
obj.setR45_RW(rs.getBigDecimal("R45_RW"));
obj.setR45_RISK_VALUE(rs.getBigDecimal("R45_RISK_VALUE"));

obj.setR46_BOOK_VALUE(rs.getBigDecimal("R46_BOOK_VALUE"));
obj.setR46_MARGINS(rs.getBigDecimal("R46_MARGINS"));
obj.setR46_BOOK_VALUE_NET(rs.getBigDecimal("R46_BOOK_VALUE_NET"));
obj.setR46_RW(rs.getBigDecimal("R46_RW"));
obj.setR46_RISK_VALUE(rs.getBigDecimal("R46_RISK_VALUE"));

obj.setR48_BOOK_VALUE(rs.getBigDecimal("R48_BOOK_VALUE"));
obj.setR48_MARGINS(rs.getBigDecimal("R48_MARGINS"));
obj.setR48_BOOK_VALUE_NET(rs.getBigDecimal("R48_BOOK_VALUE_NET"));
obj.setR48_RW(rs.getBigDecimal("R48_RW"));
obj.setR48_RISK_VALUE(rs.getBigDecimal("R48_RISK_VALUE"));


// =========================
// R61, R63 → R70
// =========================
obj.setR61_BOOK_VALUE(rs.getBigDecimal("R61_BOOK_VALUE"));
obj.setR61_MARGINS(rs.getBigDecimal("R61_MARGINS"));
obj.setR61_BOOK_VALUE_NET(rs.getBigDecimal("R61_BOOK_VALUE_NET"));
obj.setR61_RW(rs.getBigDecimal("R61_RW"));
obj.setR61_RISK_VALUE(rs.getBigDecimal("R61_RISK_VALUE"));

obj.setR63_BOOK_VALUE(rs.getBigDecimal("R63_BOOK_VALUE"));
obj.setR63_MARGINS(rs.getBigDecimal("R63_MARGINS"));
obj.setR63_BOOK_VALUE_NET(rs.getBigDecimal("R63_BOOK_VALUE_NET"));
obj.setR63_RW(rs.getBigDecimal("R63_RW"));
obj.setR63_RISK_VALUE(rs.getBigDecimal("R63_RISK_VALUE"));

obj.setR64_BOOK_VALUE(rs.getBigDecimal("R64_BOOK_VALUE"));
obj.setR64_MARGINS(rs.getBigDecimal("R64_MARGINS"));
obj.setR64_BOOK_VALUE_NET(rs.getBigDecimal("R64_BOOK_VALUE_NET"));
obj.setR64_RW(rs.getBigDecimal("R64_RW"));
obj.setR64_RISK_VALUE(rs.getBigDecimal("R64_RISK_VALUE"));

obj.setR65_BOOK_VALUE(rs.getBigDecimal("R65_BOOK_VALUE"));
obj.setR65_MARGINS(rs.getBigDecimal("R65_MARGINS"));
obj.setR65_BOOK_VALUE_NET(rs.getBigDecimal("R65_BOOK_VALUE_NET"));
obj.setR65_RW(rs.getBigDecimal("R65_RW"));
obj.setR65_RISK_VALUE(rs.getBigDecimal("R65_RISK_VALUE"));

obj.setR66_BOOK_VALUE(rs.getBigDecimal("R66_BOOK_VALUE"));
obj.setR66_MARGINS(rs.getBigDecimal("R66_MARGINS"));
obj.setR66_BOOK_VALUE_NET(rs.getBigDecimal("R66_BOOK_VALUE_NET"));
obj.setR66_RW(rs.getBigDecimal("R66_RW"));
obj.setR66_RISK_VALUE(rs.getBigDecimal("R66_RISK_VALUE"));

obj.setR67_BOOK_VALUE(rs.getBigDecimal("R67_BOOK_VALUE"));
obj.setR67_MARGINS(rs.getBigDecimal("R67_MARGINS"));
obj.setR67_BOOK_VALUE_NET(rs.getBigDecimal("R67_BOOK_VALUE_NET"));
obj.setR67_RW(rs.getBigDecimal("R67_RW"));
obj.setR67_RISK_VALUE(rs.getBigDecimal("R67_RISK_VALUE"));

obj.setR68_BOOK_VALUE(rs.getBigDecimal("R68_BOOK_VALUE"));
obj.setR68_MARGINS(rs.getBigDecimal("R68_MARGINS"));
obj.setR68_BOOK_VALUE_NET(rs.getBigDecimal("R68_BOOK_VALUE_NET"));
obj.setR68_RW(rs.getBigDecimal("R68_RW"));
obj.setR68_RISK_VALUE(rs.getBigDecimal("R68_RISK_VALUE"));

obj.setR69_BOOK_VALUE(rs.getBigDecimal("R69_BOOK_VALUE"));
obj.setR69_MARGINS(rs.getBigDecimal("R69_MARGINS"));
obj.setR69_BOOK_VALUE_NET(rs.getBigDecimal("R69_BOOK_VALUE_NET"));
obj.setR69_RW(rs.getBigDecimal("R69_RW"));
obj.setR69_RISK_VALUE(rs.getBigDecimal("R69_RISK_VALUE"));

obj.setR70_BOOK_VALUE(rs.getBigDecimal("R70_BOOK_VALUE"));
obj.setR70_MARGINS(rs.getBigDecimal("R70_MARGINS"));
obj.setR70_BOOK_VALUE_NET(rs.getBigDecimal("R70_BOOK_VALUE_NET"));
obj.setR70_RW(rs.getBigDecimal("R70_RW"));
obj.setR70_RISK_VALUE(rs.getBigDecimal("R70_RISK_VALUE"));


// =========================
// R81, R82, R97 → R110
// =========================
obj.setR81_BOOK_VALUE(rs.getBigDecimal("R81_BOOK_VALUE"));
obj.setR81_MARGINS(rs.getBigDecimal("R81_MARGINS"));
obj.setR81_BOOK_VALUE_NET(rs.getBigDecimal("R81_BOOK_VALUE_NET"));
obj.setR81_RW(rs.getBigDecimal("R81_RW"));
obj.setR81_RISK_VALUE(rs.getBigDecimal("R81_RISK_VALUE"));

obj.setR82_BOOK_VALUE(rs.getBigDecimal("R82_BOOK_VALUE"));
obj.setR82_MARGINS(rs.getBigDecimal("R82_MARGINS"));
obj.setR82_BOOK_VALUE_NET(rs.getBigDecimal("R82_BOOK_VALUE_NET"));
obj.setR82_RW(rs.getBigDecimal("R82_RW"));
obj.setR82_RISK_VALUE(rs.getBigDecimal("R82_RISK_VALUE"));

obj.setR97_BOOK_VALUE(rs.getBigDecimal("R97_BOOK_VALUE"));
obj.setR97_MARGINS(rs.getBigDecimal("R97_MARGINS"));
obj.setR97_BOOK_VALUE_NET(rs.getBigDecimal("R97_BOOK_VALUE_NET"));
obj.setR97_RW(rs.getBigDecimal("R97_RW"));
obj.setR97_RISK_VALUE(rs.getBigDecimal("R97_RISK_VALUE"));

obj.setR98_BOOK_VALUE(rs.getBigDecimal("R98_BOOK_VALUE"));
obj.setR98_MARGINS(rs.getBigDecimal("R98_MARGINS"));
obj.setR98_BOOK_VALUE_NET(rs.getBigDecimal("R98_BOOK_VALUE_NET"));
obj.setR98_RW(rs.getBigDecimal("R98_RW"));
obj.setR98_RISK_VALUE(rs.getBigDecimal("R98_RISK_VALUE"));

obj.setR99_BOOK_VALUE(rs.getBigDecimal("R99_BOOK_VALUE"));
obj.setR99_MARGINS(rs.getBigDecimal("R99_MARGINS"));
obj.setR99_BOOK_VALUE_NET(rs.getBigDecimal("R99_BOOK_VALUE_NET"));
obj.setR99_RW(rs.getBigDecimal("R99_RW"));
obj.setR99_RISK_VALUE(rs.getBigDecimal("R99_RISK_VALUE"));

obj.setR100_BOOK_VALUE(rs.getBigDecimal("R100_BOOK_VALUE"));
obj.setR100_MARGINS(rs.getBigDecimal("R100_MARGINS"));
obj.setR100_BOOK_VALUE_NET(rs.getBigDecimal("R100_BOOK_VALUE_NET"));
obj.setR100_RW(rs.getBigDecimal("R100_RW"));
obj.setR100_RISK_VALUE(rs.getBigDecimal("R100_RISK_VALUE"));

obj.setR101_BOOK_VALUE(rs.getBigDecimal("R101_BOOK_VALUE"));
obj.setR101_MARGINS(rs.getBigDecimal("R101_MARGINS"));
obj.setR101_BOOK_VALUE_NET(rs.getBigDecimal("R101_BOOK_VALUE_NET"));
obj.setR101_RW(rs.getBigDecimal("R101_RW"));
obj.setR101_RISK_VALUE(rs.getBigDecimal("R101_RISK_VALUE"));

obj.setR102_BOOK_VALUE(rs.getBigDecimal("R102_BOOK_VALUE"));
obj.setR102_MARGINS(rs.getBigDecimal("R102_MARGINS"));
obj.setR102_BOOK_VALUE_NET(rs.getBigDecimal("R102_BOOK_VALUE_NET"));
obj.setR102_RW(rs.getBigDecimal("R102_RW"));
obj.setR102_RISK_VALUE(rs.getBigDecimal("R102_RISK_VALUE"));

obj.setR103_BOOK_VALUE(rs.getBigDecimal("R103_BOOK_VALUE"));
obj.setR103_MARGINS(rs.getBigDecimal("R103_MARGINS"));
obj.setR103_BOOK_VALUE_NET(rs.getBigDecimal("R103_BOOK_VALUE_NET"));
obj.setR103_RW(rs.getBigDecimal("R103_RW"));
obj.setR103_RISK_VALUE(rs.getBigDecimal("R103_RISK_VALUE"));

obj.setR104_BOOK_VALUE(rs.getBigDecimal("R104_BOOK_VALUE"));
obj.setR104_MARGINS(rs.getBigDecimal("R104_MARGINS"));
obj.setR104_BOOK_VALUE_NET(rs.getBigDecimal("R104_BOOK_VALUE_NET"));
obj.setR104_RW(rs.getBigDecimal("R104_RW"));
obj.setR104_RISK_VALUE(rs.getBigDecimal("R104_RISK_VALUE"));

obj.setR105_BOOK_VALUE(rs.getBigDecimal("R105_BOOK_VALUE"));
obj.setR105_MARGINS(rs.getBigDecimal("R105_MARGINS"));
obj.setR105_BOOK_VALUE_NET(rs.getBigDecimal("R105_BOOK_VALUE_NET"));
obj.setR105_RW(rs.getBigDecimal("R105_RW"));
obj.setR105_RISK_VALUE(rs.getBigDecimal("R105_RISK_VALUE"));

obj.setR106_BOOK_VALUE(rs.getBigDecimal("R106_BOOK_VALUE"));
obj.setR106_MARGINS(rs.getBigDecimal("R106_MARGINS"));
obj.setR106_BOOK_VALUE_NET(rs.getBigDecimal("R106_BOOK_VALUE_NET"));
obj.setR106_RW(rs.getBigDecimal("R106_RW"));
obj.setR106_RISK_VALUE(rs.getBigDecimal("R106_RISK_VALUE"));

obj.setR107_BOOK_VALUE(rs.getBigDecimal("R107_BOOK_VALUE"));
obj.setR107_MARGINS(rs.getBigDecimal("R107_MARGINS"));
obj.setR107_BOOK_VALUE_NET(rs.getBigDecimal("R107_BOOK_VALUE_NET"));
obj.setR107_RW(rs.getBigDecimal("R107_RW"));
obj.setR107_RISK_VALUE(rs.getBigDecimal("R107_RISK_VALUE"));

obj.setR108_BOOK_VALUE(rs.getBigDecimal("R108_BOOK_VALUE"));
obj.setR108_MARGINS(rs.getBigDecimal("R108_MARGINS"));
obj.setR108_BOOK_VALUE_NET(rs.getBigDecimal("R108_BOOK_VALUE_NET"));
obj.setR108_RW(rs.getBigDecimal("R108_RW"));
obj.setR108_RISK_VALUE(rs.getBigDecimal("R108_RISK_VALUE"));

obj.setR109_BOOK_VALUE(rs.getBigDecimal("R109_BOOK_VALUE"));
obj.setR109_MARGINS(rs.getBigDecimal("R109_MARGINS"));
obj.setR109_BOOK_VALUE_NET(rs.getBigDecimal("R109_BOOK_VALUE_NET"));
obj.setR109_RW(rs.getBigDecimal("R109_RW"));
obj.setR109_RISK_VALUE(rs.getBigDecimal("R109_RISK_VALUE"));

obj.setR110_BOOK_VALUE(rs.getBigDecimal("R110_BOOK_VALUE"));
obj.setR110_MARGINS(rs.getBigDecimal("R110_MARGINS"));
obj.setR110_BOOK_VALUE_NET(rs.getBigDecimal("R110_BOOK_VALUE_NET"));
obj.setR110_RW(rs.getBigDecimal("R110_RW"));
obj.setR110_RISK_VALUE(rs.getBigDecimal("R110_RISK_VALUE"));


// =========================
// R111 → R120
// =========================
obj.setR111_BOOK_VALUE(rs.getBigDecimal("R111_BOOK_VALUE"));
obj.setR111_MARGINS(rs.getBigDecimal("R111_MARGINS"));
obj.setR111_BOOK_VALUE_NET(rs.getBigDecimal("R111_BOOK_VALUE_NET"));
obj.setR111_RW(rs.getBigDecimal("R111_RW"));
obj.setR111_RISK_VALUE(rs.getBigDecimal("R111_RISK_VALUE"));

obj.setR112_BOOK_VALUE(rs.getBigDecimal("R112_BOOK_VALUE"));
obj.setR112_MARGINS(rs.getBigDecimal("R112_MARGINS"));
obj.setR112_BOOK_VALUE_NET(rs.getBigDecimal("R112_BOOK_VALUE_NET"));
obj.setR112_RW(rs.getBigDecimal("R112_RW"));
obj.setR112_RISK_VALUE(rs.getBigDecimal("R112_RISK_VALUE"));

obj.setR113_BOOK_VALUE(rs.getBigDecimal("R113_BOOK_VALUE"));
obj.setR113_MARGINS(rs.getBigDecimal("R113_MARGINS"));
obj.setR113_BOOK_VALUE_NET(rs.getBigDecimal("R113_BOOK_VALUE_NET"));
obj.setR113_RW(rs.getBigDecimal("R113_RW"));
obj.setR113_RISK_VALUE(rs.getBigDecimal("R113_RISK_VALUE"));

obj.setR114_BOOK_VALUE(rs.getBigDecimal("R114_BOOK_VALUE"));
obj.setR114_MARGINS(rs.getBigDecimal("R114_MARGINS"));
obj.setR114_BOOK_VALUE_NET(rs.getBigDecimal("R114_BOOK_VALUE_NET"));
obj.setR114_RW(rs.getBigDecimal("R114_RW"));
obj.setR114_RISK_VALUE(rs.getBigDecimal("R114_RISK_VALUE"));

obj.setR115_BOOK_VALUE(rs.getBigDecimal("R115_BOOK_VALUE"));
obj.setR115_MARGINS(rs.getBigDecimal("R115_MARGINS"));
obj.setR115_BOOK_VALUE_NET(rs.getBigDecimal("R115_BOOK_VALUE_NET"));
obj.setR115_RW(rs.getBigDecimal("R115_RW"));
obj.setR115_RISK_VALUE(rs.getBigDecimal("R115_RISK_VALUE"));

obj.setR116_BOOK_VALUE(rs.getBigDecimal("R116_BOOK_VALUE"));
obj.setR116_MARGINS(rs.getBigDecimal("R116_MARGINS"));
obj.setR116_BOOK_VALUE_NET(rs.getBigDecimal("R116_BOOK_VALUE_NET"));
obj.setR116_RW(rs.getBigDecimal("R116_RW"));
obj.setR116_RISK_VALUE(rs.getBigDecimal("R116_RISK_VALUE"));

obj.setR117_BOOK_VALUE(rs.getBigDecimal("R117_BOOK_VALUE"));
obj.setR117_MARGINS(rs.getBigDecimal("R117_MARGINS"));
obj.setR117_BOOK_VALUE_NET(rs.getBigDecimal("R117_BOOK_VALUE_NET"));
obj.setR117_RW(rs.getBigDecimal("R117_RW"));
obj.setR117_RISK_VALUE(rs.getBigDecimal("R117_RISK_VALUE"));

obj.setR118_BOOK_VALUE(rs.getBigDecimal("R118_BOOK_VALUE"));
obj.setR118_MARGINS(rs.getBigDecimal("R118_MARGINS"));
obj.setR118_BOOK_VALUE_NET(rs.getBigDecimal("R118_BOOK_VALUE_NET"));
obj.setR118_RW(rs.getBigDecimal("R118_RW"));
obj.setR118_RISK_VALUE(rs.getBigDecimal("R118_RISK_VALUE"));

obj.setR119_BOOK_VALUE(rs.getBigDecimal("R119_BOOK_VALUE"));
obj.setR119_MARGINS(rs.getBigDecimal("R119_MARGINS"));
obj.setR119_BOOK_VALUE_NET(rs.getBigDecimal("R119_BOOK_VALUE_NET"));
obj.setR119_RW(rs.getBigDecimal("R119_RW"));
obj.setR119_RISK_VALUE(rs.getBigDecimal("R119_RISK_VALUE"));

obj.setR120_BOOK_VALUE(rs.getBigDecimal("R120_BOOK_VALUE"));
obj.setR120_MARGINS(rs.getBigDecimal("R120_MARGINS"));
obj.setR120_BOOK_VALUE_NET(rs.getBigDecimal("R120_BOOK_VALUE_NET"));
obj.setR120_RW(rs.getBigDecimal("R120_RW"));
obj.setR120_RISK_VALUE(rs.getBigDecimal("R120_RISK_VALUE"));


// =========================
// R121 → R130
// =========================
obj.setR121_BOOK_VALUE(rs.getBigDecimal("R121_BOOK_VALUE"));
obj.setR121_MARGINS(rs.getBigDecimal("R121_MARGINS"));
obj.setR121_BOOK_VALUE_NET(rs.getBigDecimal("R121_BOOK_VALUE_NET"));
obj.setR121_RW(rs.getBigDecimal("R121_RW"));
obj.setR121_RISK_VALUE(rs.getBigDecimal("R121_RISK_VALUE"));

obj.setR122_BOOK_VALUE(rs.getBigDecimal("R122_BOOK_VALUE"));
obj.setR122_MARGINS(rs.getBigDecimal("R122_MARGINS"));
obj.setR122_BOOK_VALUE_NET(rs.getBigDecimal("R122_BOOK_VALUE_NET"));
obj.setR122_RW(rs.getBigDecimal("R122_RW"));
obj.setR122_RISK_VALUE(rs.getBigDecimal("R122_RISK_VALUE"));

obj.setR123_BOOK_VALUE(rs.getBigDecimal("R123_BOOK_VALUE"));
obj.setR123_MARGINS(rs.getBigDecimal("R123_MARGINS"));
obj.setR123_BOOK_VALUE_NET(rs.getBigDecimal("R123_BOOK_VALUE_NET"));
obj.setR123_RW(rs.getBigDecimal("R123_RW"));
obj.setR123_RISK_VALUE(rs.getBigDecimal("R123_RISK_VALUE"));

obj.setR124_BOOK_VALUE(rs.getBigDecimal("R124_BOOK_VALUE"));
obj.setR124_MARGINS(rs.getBigDecimal("R124_MARGINS"));
obj.setR124_BOOK_VALUE_NET(rs.getBigDecimal("R124_BOOK_VALUE_NET"));
obj.setR124_RW(rs.getBigDecimal("R124_RW"));
obj.setR124_RISK_VALUE(rs.getBigDecimal("R124_RISK_VALUE"));

obj.setR125_BOOK_VALUE(rs.getBigDecimal("R125_BOOK_VALUE"));
obj.setR125_MARGINS(rs.getBigDecimal("R125_MARGINS"));
obj.setR125_BOOK_VALUE_NET(rs.getBigDecimal("R125_BOOK_VALUE_NET"));
obj.setR125_RW(rs.getBigDecimal("R125_RW"));
obj.setR125_RISK_VALUE(rs.getBigDecimal("R125_RISK_VALUE"));

obj.setR126_BOOK_VALUE(rs.getBigDecimal("R126_BOOK_VALUE"));
obj.setR126_MARGINS(rs.getBigDecimal("R126_MARGINS"));
obj.setR126_BOOK_VALUE_NET(rs.getBigDecimal("R126_BOOK_VALUE_NET"));
obj.setR126_RW(rs.getBigDecimal("R126_RW"));
obj.setR126_RISK_VALUE(rs.getBigDecimal("R126_RISK_VALUE"));

obj.setR127_BOOK_VALUE(rs.getBigDecimal("R127_BOOK_VALUE"));
obj.setR127_MARGINS(rs.getBigDecimal("R127_MARGINS"));
obj.setR127_BOOK_VALUE_NET(rs.getBigDecimal("R127_BOOK_VALUE_NET"));
obj.setR127_RW(rs.getBigDecimal("R127_RW"));
obj.setR127_RISK_VALUE(rs.getBigDecimal("R127_RISK_VALUE"));

obj.setR128_BOOK_VALUE(rs.getBigDecimal("R128_BOOK_VALUE"));
obj.setR128_MARGINS(rs.getBigDecimal("R128_MARGINS"));
obj.setR128_BOOK_VALUE_NET(rs.getBigDecimal("R128_BOOK_VALUE_NET"));
obj.setR128_RW(rs.getBigDecimal("R128_RW"));
obj.setR128_RISK_VALUE(rs.getBigDecimal("R128_RISK_VALUE"));

obj.setR129_BOOK_VALUE(rs.getBigDecimal("R129_BOOK_VALUE"));
obj.setR129_MARGINS(rs.getBigDecimal("R129_MARGINS"));
obj.setR129_BOOK_VALUE_NET(rs.getBigDecimal("R129_BOOK_VALUE_NET"));
obj.setR129_RW(rs.getBigDecimal("R129_RW"));
obj.setR129_RISK_VALUE(rs.getBigDecimal("R129_RISK_VALUE"));

obj.setR130_BOOK_VALUE(rs.getBigDecimal("R130_BOOK_VALUE"));
obj.setR130_MARGINS(rs.getBigDecimal("R130_MARGINS"));
obj.setR130_BOOK_VALUE_NET(rs.getBigDecimal("R130_BOOK_VALUE_NET"));
obj.setR130_RW(rs.getBigDecimal("R130_RW"));
obj.setR130_RISK_VALUE(rs.getBigDecimal("R130_RISK_VALUE"));


        
        return obj;
    }
}


public class RWA_Summary_Entity {


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
	
	private BigDecimal R8_BOOK_VALUE;
	private BigDecimal R8_MARGINS;
	private BigDecimal R8_BOOK_VALUE_NET;
	private BigDecimal R8_RW;
	private BigDecimal R8_RISK_VALUE;

	private BigDecimal R9_BOOK_VALUE;
	private BigDecimal R9_MARGINS;
	private BigDecimal R9_BOOK_VALUE_NET;
	private BigDecimal R9_RW;
	private BigDecimal R9_RISK_VALUE;

	private BigDecimal R10_BOOK_VALUE;
	private BigDecimal R10_MARGINS;
	private BigDecimal R10_BOOK_VALUE_NET;
	private BigDecimal R10_RW;
	private BigDecimal R10_RISK_VALUE;

	private BigDecimal R11_BOOK_VALUE;
	private BigDecimal R11_MARGINS;
	private BigDecimal R11_BOOK_VALUE_NET;
	private BigDecimal R11_RW;
	private BigDecimal R11_RISK_VALUE;

	private BigDecimal R12_BOOK_VALUE;
	private BigDecimal R12_MARGINS;
	private BigDecimal R12_BOOK_VALUE_NET;
	private BigDecimal R12_RW;
	private BigDecimal R12_RISK_VALUE;

	private BigDecimal R13_BOOK_VALUE;
	private BigDecimal R13_MARGINS;
	private BigDecimal R13_BOOK_VALUE_NET;
	private BigDecimal R13_RW;
	private BigDecimal R13_RISK_VALUE;

	private BigDecimal R14_BOOK_VALUE;
	private BigDecimal R14_MARGINS;
	private BigDecimal R14_BOOK_VALUE_NET;
	private BigDecimal R14_RW;
	private BigDecimal R14_RISK_VALUE;

	private BigDecimal R15_BOOK_VALUE;
	private BigDecimal R15_MARGINS;
	private BigDecimal R15_BOOK_VALUE_NET;
	private BigDecimal R15_RW;
	private BigDecimal R15_RISK_VALUE;

	private BigDecimal R16_BOOK_VALUE;
	private BigDecimal R16_MARGINS;
	private BigDecimal R16_BOOK_VALUE_NET;
	private BigDecimal R16_RW;
	private BigDecimal R16_RISK_VALUE;

	private BigDecimal R17_BOOK_VALUE;
	private BigDecimal R17_MARGINS;
	private BigDecimal R17_BOOK_VALUE_NET;
	private BigDecimal R17_RW;
	private BigDecimal R17_RISK_VALUE;

	private BigDecimal R18_BOOK_VALUE;
	private BigDecimal R18_MARGINS;
	private BigDecimal R18_BOOK_VALUE_NET;
	private BigDecimal R18_RW;
	private BigDecimal R18_RISK_VALUE;

	private BigDecimal R19_BOOK_VALUE;
	private BigDecimal R19_MARGINS;
	private BigDecimal R19_BOOK_VALUE_NET;
	private BigDecimal R19_RW;
	private BigDecimal R19_RISK_VALUE;

	private BigDecimal R20_BOOK_VALUE;
	private BigDecimal R20_MARGINS;
	private BigDecimal R20_BOOK_VALUE_NET;
	private BigDecimal R20_RW;
	private BigDecimal R20_RISK_VALUE;

	private BigDecimal R21_BOOK_VALUE;
	private BigDecimal R21_MARGINS;
	private BigDecimal R21_BOOK_VALUE_NET;
	private BigDecimal R21_RW;
	private BigDecimal R21_RISK_VALUE;

	private BigDecimal R22_BOOK_VALUE;
	private BigDecimal R22_MARGINS;
	private BigDecimal R22_BOOK_VALUE_NET;
	private BigDecimal R22_RW;
	private BigDecimal R22_RISK_VALUE;

	private BigDecimal R23_BOOK_VALUE;
	private BigDecimal R23_MARGINS;
	private BigDecimal R23_BOOK_VALUE_NET;
	private BigDecimal R23_RW;
	private BigDecimal R23_RISK_VALUE;

	private BigDecimal R24_BOOK_VALUE;
	private BigDecimal R24_MARGINS;
	private BigDecimal R24_BOOK_VALUE_NET;
	private BigDecimal R24_RW;
	private BigDecimal R24_RISK_VALUE;

	private BigDecimal R25_BOOK_VALUE;
	private BigDecimal R25_MARGINS;
	private BigDecimal R25_BOOK_VALUE_NET;
	private BigDecimal R25_RW;
	private BigDecimal R25_RISK_VALUE;

	private BigDecimal R26_BOOK_VALUE;
	private BigDecimal R26_MARGINS;
	private BigDecimal R26_BOOK_VALUE_NET;
	private BigDecimal R26_RW;
	private BigDecimal R26_RISK_VALUE;

	private BigDecimal R27_BOOK_VALUE;
	private BigDecimal R27_MARGINS;
	private BigDecimal R27_BOOK_VALUE_NET;
	private BigDecimal R27_RW;
	private BigDecimal R27_RISK_VALUE;

	private BigDecimal R28_BOOK_VALUE;
	private BigDecimal R28_MARGINS;
	private BigDecimal R28_BOOK_VALUE_NET;
	private BigDecimal R28_RW;
	private BigDecimal R28_RISK_VALUE;

	private BigDecimal R29_BOOK_VALUE;
	private BigDecimal R29_MARGINS;
	private BigDecimal R29_BOOK_VALUE_NET;
	private BigDecimal R29_RW;
	private BigDecimal R29_RISK_VALUE;

	private BigDecimal R30_BOOK_VALUE;
	private BigDecimal R30_MARGINS;
	private BigDecimal R30_BOOK_VALUE_NET;
	private BigDecimal R30_RW;
	private BigDecimal R30_RISK_VALUE;

	private BigDecimal R31_BOOK_VALUE;
	private BigDecimal R31_MARGINS;
	private BigDecimal R31_BOOK_VALUE_NET;
	private BigDecimal R31_RW;
	private BigDecimal R31_RISK_VALUE;

	private BigDecimal R32_BOOK_VALUE;
	private BigDecimal R32_MARGINS;
	private BigDecimal R32_BOOK_VALUE_NET;
	private BigDecimal R32_RW;
	private BigDecimal R32_RISK_VALUE;

	private BigDecimal R33_BOOK_VALUE;
	private BigDecimal R33_MARGINS;
	private BigDecimal R33_BOOK_VALUE_NET;
	private BigDecimal R33_RW;
	private BigDecimal R33_RISK_VALUE;

	private BigDecimal R34_BOOK_VALUE;
	private BigDecimal R34_MARGINS;
	private BigDecimal R34_BOOK_VALUE_NET;
	private BigDecimal R34_RW;
	private BigDecimal R34_RISK_VALUE;

	private BigDecimal R35_BOOK_VALUE;
	private BigDecimal R35_MARGINS;
	private BigDecimal R35_BOOK_VALUE_NET;
	private BigDecimal R35_RW;
	private BigDecimal R35_RISK_VALUE;

	private BigDecimal R36_BOOK_VALUE;
	private BigDecimal R36_MARGINS;
	private BigDecimal R36_BOOK_VALUE_NET;
	private BigDecimal R36_RW;
	private BigDecimal R36_RISK_VALUE;

	private BigDecimal R37_BOOK_VALUE;
	private BigDecimal R37_MARGINS;
	private BigDecimal R37_BOOK_VALUE_NET;
	private BigDecimal R37_RW;
	private BigDecimal R37_RISK_VALUE;

	private BigDecimal R38_BOOK_VALUE;
	private BigDecimal R38_MARGINS;
	private BigDecimal R38_BOOK_VALUE_NET;
	private BigDecimal R38_RW;
	private BigDecimal R38_RISK_VALUE;

	private BigDecimal R39_BOOK_VALUE;
	private BigDecimal R39_MARGINS;
	private BigDecimal R39_BOOK_VALUE_NET;
	private BigDecimal R39_RW;
	private BigDecimal R39_RISK_VALUE;

	private BigDecimal R40_BOOK_VALUE;
	private BigDecimal R40_MARGINS;
	private BigDecimal R40_BOOK_VALUE_NET;
	private BigDecimal R40_RW;
	private BigDecimal R40_RISK_VALUE;

	private BigDecimal R41_BOOK_VALUE;
	private BigDecimal R41_MARGINS;
	private BigDecimal R41_BOOK_VALUE_NET;
	private BigDecimal R41_RW;
	private BigDecimal R41_RISK_VALUE;

	private BigDecimal R42_BOOK_VALUE;
	private BigDecimal R42_MARGINS;
	private BigDecimal R42_BOOK_VALUE_NET;
	private BigDecimal R42_RW;
	private BigDecimal R42_RISK_VALUE;

	private BigDecimal R43_BOOK_VALUE;
	private BigDecimal R43_MARGINS;
	private BigDecimal R43_BOOK_VALUE_NET;
	private BigDecimal R43_RW;
	private BigDecimal R43_RISK_VALUE;

	private BigDecimal R44_BOOK_VALUE;
	private BigDecimal R44_MARGINS;
	private BigDecimal R44_BOOK_VALUE_NET;
	private BigDecimal R44_RW;
	private BigDecimal R44_RISK_VALUE;

	private BigDecimal R45_BOOK_VALUE;
	private BigDecimal R45_MARGINS;
	private BigDecimal R45_BOOK_VALUE_NET;
	private BigDecimal R45_RW;
	private BigDecimal R45_RISK_VALUE;

	private BigDecimal R46_BOOK_VALUE;
	private BigDecimal R46_MARGINS;
	private BigDecimal R46_BOOK_VALUE_NET;
	private BigDecimal R46_RW;
	private BigDecimal R46_RISK_VALUE;

	private BigDecimal R48_BOOK_VALUE;
	private BigDecimal R48_MARGINS;
	private BigDecimal R48_BOOK_VALUE_NET;
	private BigDecimal R48_RW;
	private BigDecimal R48_RISK_VALUE;

	private BigDecimal R61_BOOK_VALUE;
	private BigDecimal R61_MARGINS;
	private BigDecimal R61_BOOK_VALUE_NET;
	private BigDecimal R61_RW;
	private BigDecimal R61_RISK_VALUE;

	private BigDecimal R63_BOOK_VALUE;
	private BigDecimal R63_MARGINS;
	private BigDecimal R63_BOOK_VALUE_NET;
	private BigDecimal R63_RW;
	private BigDecimal R63_RISK_VALUE;

	private BigDecimal R64_BOOK_VALUE;
	private BigDecimal R64_MARGINS;
	private BigDecimal R64_BOOK_VALUE_NET;
	private BigDecimal R64_RW;
	private BigDecimal R64_RISK_VALUE;

	private BigDecimal R65_BOOK_VALUE;
	private BigDecimal R65_MARGINS;
	private BigDecimal R65_BOOK_VALUE_NET;
	private BigDecimal R65_RW;
	private BigDecimal R65_RISK_VALUE;

	private BigDecimal R66_BOOK_VALUE;
	private BigDecimal R66_MARGINS;
	private BigDecimal R66_BOOK_VALUE_NET;
	private BigDecimal R66_RW;
	private BigDecimal R66_RISK_VALUE;

	private BigDecimal R67_BOOK_VALUE;
	private BigDecimal R67_MARGINS;
	private BigDecimal R67_BOOK_VALUE_NET;
	private BigDecimal R67_RW;
	private BigDecimal R67_RISK_VALUE;

	private BigDecimal R68_BOOK_VALUE;
	private BigDecimal R68_MARGINS;
	private BigDecimal R68_BOOK_VALUE_NET;
	private BigDecimal R68_RW;
	private BigDecimal R68_RISK_VALUE;

	private BigDecimal R69_BOOK_VALUE;
	private BigDecimal R69_MARGINS;
	private BigDecimal R69_BOOK_VALUE_NET;
	private BigDecimal R69_RW;
	private BigDecimal R69_RISK_VALUE;

	private BigDecimal R70_BOOK_VALUE;
	private BigDecimal R70_MARGINS;
	private BigDecimal R70_BOOK_VALUE_NET;
	private BigDecimal R70_RW;
	private BigDecimal R70_RISK_VALUE;

	private BigDecimal R71_BOOK_VALUE;
	private BigDecimal R71_MARGINS;
	private BigDecimal R71_BOOK_VALUE_NET;
	private BigDecimal R71_RW;
	private BigDecimal R71_RISK_VALUE;

	private BigDecimal R72_BOOK_VALUE;
	private BigDecimal R72_MARGINS;
	private BigDecimal R72_BOOK_VALUE_NET;
	private BigDecimal R72_RW;
	private BigDecimal R72_RISK_VALUE;

	private BigDecimal R73_BOOK_VALUE;
	private BigDecimal R73_MARGINS;
	private BigDecimal R73_BOOK_VALUE_NET;
	private BigDecimal R73_RW;
	private BigDecimal R73_RISK_VALUE;

	private BigDecimal R74_BOOK_VALUE;
	private BigDecimal R74_MARGINS;
	private BigDecimal R74_BOOK_VALUE_NET;
	private BigDecimal R74_RW;
	private BigDecimal R74_RISK_VALUE;

	private BigDecimal R75_BOOK_VALUE;
	private BigDecimal R75_MARGINS;
	private BigDecimal R75_BOOK_VALUE_NET;
	private BigDecimal R75_RW;
	private BigDecimal R75_RISK_VALUE;

	private BigDecimal R76_BOOK_VALUE;
	private BigDecimal R76_MARGINS;
	private BigDecimal R76_BOOK_VALUE_NET;
	private BigDecimal R76_RW;
	private BigDecimal R76_RISK_VALUE;

	private BigDecimal R77_BOOK_VALUE;
	private BigDecimal R77_MARGINS;
	private BigDecimal R77_BOOK_VALUE_NET;
	private BigDecimal R77_RW;
	private BigDecimal R77_RISK_VALUE;

	private BigDecimal R78_BOOK_VALUE;
	private BigDecimal R78_MARGINS;
	private BigDecimal R78_BOOK_VALUE_NET;
	private BigDecimal R78_RW;
	private BigDecimal R78_RISK_VALUE;

	private BigDecimal R79_BOOK_VALUE;
	private BigDecimal R79_MARGINS;
	private BigDecimal R79_BOOK_VALUE_NET;
	private BigDecimal R79_RW;
	private BigDecimal R79_RISK_VALUE;

	private BigDecimal R80_BOOK_VALUE;
	private BigDecimal R80_MARGINS;
	private BigDecimal R80_BOOK_VALUE_NET;
	private BigDecimal R80_RW;
	private BigDecimal R80_RISK_VALUE;

	private BigDecimal R81_BOOK_VALUE;
	private BigDecimal R81_MARGINS;
	private BigDecimal R81_BOOK_VALUE_NET;
	private BigDecimal R81_RW;
	private BigDecimal R81_RISK_VALUE;

	private BigDecimal R82_BOOK_VALUE;
	private BigDecimal R82_MARGINS;
	private BigDecimal R82_BOOK_VALUE_NET;
	private BigDecimal R82_RW;
	private BigDecimal R82_RISK_VALUE;

	private BigDecimal R97_BOOK_VALUE;
	private BigDecimal R97_MARGINS;
	private BigDecimal R97_BOOK_VALUE_NET;
	private BigDecimal R97_RW;
	private BigDecimal R97_RISK_VALUE;

	private BigDecimal R98_BOOK_VALUE;
	private BigDecimal R98_MARGINS;
	private BigDecimal R98_BOOK_VALUE_NET;
	private BigDecimal R98_RW;
	private BigDecimal R98_RISK_VALUE;

	private BigDecimal R99_BOOK_VALUE;
	private BigDecimal R99_MARGINS;
	private BigDecimal R99_BOOK_VALUE_NET;
	private BigDecimal R99_RW;
	private BigDecimal R99_RISK_VALUE;

	private BigDecimal R100_BOOK_VALUE;
	private BigDecimal R100_MARGINS;
	private BigDecimal R100_BOOK_VALUE_NET;
	private BigDecimal R100_RW;
	private BigDecimal R100_RISK_VALUE;

	private BigDecimal R101_BOOK_VALUE;
	private BigDecimal R101_MARGINS;
	private BigDecimal R101_BOOK_VALUE_NET;
	private BigDecimal R101_RW;
	private BigDecimal R101_RISK_VALUE;

	private BigDecimal R102_BOOK_VALUE;
	private BigDecimal R102_MARGINS;
	private BigDecimal R102_BOOK_VALUE_NET;
	private BigDecimal R102_RW;
	private BigDecimal R102_RISK_VALUE;

	private BigDecimal R103_BOOK_VALUE;
	private BigDecimal R103_MARGINS;
	private BigDecimal R103_BOOK_VALUE_NET;
	private BigDecimal R103_RW;
	private BigDecimal R103_RISK_VALUE;

	private BigDecimal R104_BOOK_VALUE;
	private BigDecimal R104_MARGINS;
	private BigDecimal R104_BOOK_VALUE_NET;
	private BigDecimal R104_RW;
	private BigDecimal R104_RISK_VALUE;

	private BigDecimal R105_BOOK_VALUE;
	private BigDecimal R105_MARGINS;
	private BigDecimal R105_BOOK_VALUE_NET;
	private BigDecimal R105_RW;
	private BigDecimal R105_RISK_VALUE;

	private BigDecimal R106_BOOK_VALUE;
	private BigDecimal R106_MARGINS;
	private BigDecimal R106_BOOK_VALUE_NET;
	private BigDecimal R106_RW;
	private BigDecimal R106_RISK_VALUE;

	private BigDecimal R107_BOOK_VALUE;
	private BigDecimal R107_MARGINS;
	private BigDecimal R107_BOOK_VALUE_NET;
	private BigDecimal R107_RW;
	private BigDecimal R107_RISK_VALUE;

	private BigDecimal R108_BOOK_VALUE;
	private BigDecimal R108_MARGINS;
	private BigDecimal R108_BOOK_VALUE_NET;
	private BigDecimal R108_RW;
	private BigDecimal R108_RISK_VALUE;

	private BigDecimal R109_BOOK_VALUE;
	private BigDecimal R109_MARGINS;
	private BigDecimal R109_BOOK_VALUE_NET;
	private BigDecimal R109_RW;
	private BigDecimal R109_RISK_VALUE;

	private BigDecimal R110_BOOK_VALUE;
	private BigDecimal R110_MARGINS;
	private BigDecimal R110_BOOK_VALUE_NET;
	private BigDecimal R110_RW;
	private BigDecimal R110_RISK_VALUE;

	private BigDecimal R111_BOOK_VALUE;
	private BigDecimal R111_MARGINS;
	private BigDecimal R111_BOOK_VALUE_NET;
	private BigDecimal R111_RW;
	private BigDecimal R111_RISK_VALUE;

	private BigDecimal R112_BOOK_VALUE;
	private BigDecimal R112_MARGINS;
	private BigDecimal R112_BOOK_VALUE_NET;
	private BigDecimal R112_RW;
	private BigDecimal R112_RISK_VALUE;

	private BigDecimal R113_BOOK_VALUE;
	private BigDecimal R113_MARGINS;
	private BigDecimal R113_BOOK_VALUE_NET;
	private BigDecimal R113_RW;
	private BigDecimal R113_RISK_VALUE;

	private BigDecimal R114_BOOK_VALUE;
	private BigDecimal R114_MARGINS;
	private BigDecimal R114_BOOK_VALUE_NET;
	private BigDecimal R114_RW;
	private BigDecimal R114_RISK_VALUE;

	private BigDecimal R115_BOOK_VALUE;
	private BigDecimal R115_MARGINS;
	private BigDecimal R115_BOOK_VALUE_NET;
	private BigDecimal R115_RW;
	private BigDecimal R115_RISK_VALUE;

	private BigDecimal R116_BOOK_VALUE;
	private BigDecimal R116_MARGINS;
	private BigDecimal R116_BOOK_VALUE_NET;
	private BigDecimal R116_RW;
	private BigDecimal R116_RISK_VALUE;

	private BigDecimal R117_BOOK_VALUE;
	private BigDecimal R117_MARGINS;
	private BigDecimal R117_BOOK_VALUE_NET;
	private BigDecimal R117_RW;
	private BigDecimal R117_RISK_VALUE;

	private BigDecimal R118_BOOK_VALUE;
	private BigDecimal R118_MARGINS;
	private BigDecimal R118_BOOK_VALUE_NET;
	private BigDecimal R118_RW;
	private BigDecimal R118_RISK_VALUE;

	private BigDecimal R119_BOOK_VALUE;
	private BigDecimal R119_MARGINS;
	private BigDecimal R119_BOOK_VALUE_NET;
	private BigDecimal R119_RW;
	private BigDecimal R119_RISK_VALUE;

	private BigDecimal R120_BOOK_VALUE;
	private BigDecimal R120_MARGINS;
	private BigDecimal R120_BOOK_VALUE_NET;
	private BigDecimal R120_RW;
	private BigDecimal R120_RISK_VALUE;

	private BigDecimal R121_BOOK_VALUE;
	private BigDecimal R121_MARGINS;
	private BigDecimal R121_BOOK_VALUE_NET;
	private BigDecimal R121_RW;
	private BigDecimal R121_RISK_VALUE;

	private BigDecimal R122_BOOK_VALUE;
	private BigDecimal R122_MARGINS;
	private BigDecimal R122_BOOK_VALUE_NET;
	private BigDecimal R122_RW;
	private BigDecimal R122_RISK_VALUE;

	private BigDecimal R123_BOOK_VALUE;
	private BigDecimal R123_MARGINS;
	private BigDecimal R123_BOOK_VALUE_NET;
	private BigDecimal R123_RW;
	private BigDecimal R123_RISK_VALUE;

	private BigDecimal R124_BOOK_VALUE;
	private BigDecimal R124_MARGINS;
	private BigDecimal R124_BOOK_VALUE_NET;
	private BigDecimal R124_RW;
	private BigDecimal R124_RISK_VALUE;

	private BigDecimal R125_BOOK_VALUE;
	private BigDecimal R125_MARGINS;
	private BigDecimal R125_BOOK_VALUE_NET;
	private BigDecimal R125_RW;
	private BigDecimal R125_RISK_VALUE;

	private BigDecimal R126_BOOK_VALUE;
	private BigDecimal R126_MARGINS;
	private BigDecimal R126_BOOK_VALUE_NET;
	private BigDecimal R126_RW;
	private BigDecimal R126_RISK_VALUE;

	private BigDecimal R127_BOOK_VALUE;
	private BigDecimal R127_MARGINS;
	private BigDecimal R127_BOOK_VALUE_NET;
	private BigDecimal R127_RW;
	private BigDecimal R127_RISK_VALUE;

	private BigDecimal R128_BOOK_VALUE;
	private BigDecimal R128_MARGINS;
	private BigDecimal R128_BOOK_VALUE_NET;
	private BigDecimal R128_RW;
	private BigDecimal R128_RISK_VALUE;

	private BigDecimal R129_BOOK_VALUE;
	private BigDecimal R129_MARGINS;
	private BigDecimal R129_BOOK_VALUE_NET;
	private BigDecimal R129_RW;
	private BigDecimal R129_RISK_VALUE;

	private BigDecimal R130_BOOK_VALUE;
	private BigDecimal R130_MARGINS;
	private BigDecimal R130_BOOK_VALUE_NET;
	private BigDecimal R130_RW;
	private BigDecimal R130_RISK_VALUE;
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
	public BigDecimal getR8_BOOK_VALUE() {
		return R8_BOOK_VALUE;
	}
	public void setR8_BOOK_VALUE(BigDecimal r8_BOOK_VALUE) {
		R8_BOOK_VALUE = r8_BOOK_VALUE;
	}
	public BigDecimal getR8_MARGINS() {
		return R8_MARGINS;
	}
	public void setR8_MARGINS(BigDecimal r8_MARGINS) {
		R8_MARGINS = r8_MARGINS;
	}
	public BigDecimal getR8_BOOK_VALUE_NET() {
		return R8_BOOK_VALUE_NET;
	}
	public void setR8_BOOK_VALUE_NET(BigDecimal r8_BOOK_VALUE_NET) {
		R8_BOOK_VALUE_NET = r8_BOOK_VALUE_NET;
	}
	public BigDecimal getR8_RW() {
		return R8_RW;
	}
	public void setR8_RW(BigDecimal r8_RW) {
		R8_RW = r8_RW;
	}
	public BigDecimal getR8_RISK_VALUE() {
		return R8_RISK_VALUE;
	}
	public void setR8_RISK_VALUE(BigDecimal r8_RISK_VALUE) {
		R8_RISK_VALUE = r8_RISK_VALUE;
	}
	public BigDecimal getR9_BOOK_VALUE() {
		return R9_BOOK_VALUE;
	}
	public void setR9_BOOK_VALUE(BigDecimal r9_BOOK_VALUE) {
		R9_BOOK_VALUE = r9_BOOK_VALUE;
	}
	public BigDecimal getR9_MARGINS() {
		return R9_MARGINS;
	}
	public void setR9_MARGINS(BigDecimal r9_MARGINS) {
		R9_MARGINS = r9_MARGINS;
	}
	public BigDecimal getR9_BOOK_VALUE_NET() {
		return R9_BOOK_VALUE_NET;
	}
	public void setR9_BOOK_VALUE_NET(BigDecimal r9_BOOK_VALUE_NET) {
		R9_BOOK_VALUE_NET = r9_BOOK_VALUE_NET;
	}
	public BigDecimal getR9_RW() {
		return R9_RW;
	}
	public void setR9_RW(BigDecimal r9_RW) {
		R9_RW = r9_RW;
	}
	public BigDecimal getR9_RISK_VALUE() {
		return R9_RISK_VALUE;
	}
	public void setR9_RISK_VALUE(BigDecimal r9_RISK_VALUE) {
		R9_RISK_VALUE = r9_RISK_VALUE;
	}
	public BigDecimal getR10_BOOK_VALUE() {
		return R10_BOOK_VALUE;
	}
	public void setR10_BOOK_VALUE(BigDecimal r10_BOOK_VALUE) {
		R10_BOOK_VALUE = r10_BOOK_VALUE;
	}
	public BigDecimal getR10_MARGINS() {
		return R10_MARGINS;
	}
	public void setR10_MARGINS(BigDecimal r10_MARGINS) {
		R10_MARGINS = r10_MARGINS;
	}
	public BigDecimal getR10_BOOK_VALUE_NET() {
		return R10_BOOK_VALUE_NET;
	}
	public void setR10_BOOK_VALUE_NET(BigDecimal r10_BOOK_VALUE_NET) {
		R10_BOOK_VALUE_NET = r10_BOOK_VALUE_NET;
	}
	public BigDecimal getR10_RW() {
		return R10_RW;
	}
	public void setR10_RW(BigDecimal r10_RW) {
		R10_RW = r10_RW;
	}
	public BigDecimal getR10_RISK_VALUE() {
		return R10_RISK_VALUE;
	}
	public void setR10_RISK_VALUE(BigDecimal r10_RISK_VALUE) {
		R10_RISK_VALUE = r10_RISK_VALUE;
	}
	public BigDecimal getR11_BOOK_VALUE() {
		return R11_BOOK_VALUE;
	}
	public void setR11_BOOK_VALUE(BigDecimal r11_BOOK_VALUE) {
		R11_BOOK_VALUE = r11_BOOK_VALUE;
	}
	public BigDecimal getR11_MARGINS() {
		return R11_MARGINS;
	}
	public void setR11_MARGINS(BigDecimal r11_MARGINS) {
		R11_MARGINS = r11_MARGINS;
	}
	public BigDecimal getR11_BOOK_VALUE_NET() {
		return R11_BOOK_VALUE_NET;
	}
	public void setR11_BOOK_VALUE_NET(BigDecimal r11_BOOK_VALUE_NET) {
		R11_BOOK_VALUE_NET = r11_BOOK_VALUE_NET;
	}
	public BigDecimal getR11_RW() {
		return R11_RW;
	}
	public void setR11_RW(BigDecimal r11_RW) {
		R11_RW = r11_RW;
	}
	public BigDecimal getR11_RISK_VALUE() {
		return R11_RISK_VALUE;
	}
	public void setR11_RISK_VALUE(BigDecimal r11_RISK_VALUE) {
		R11_RISK_VALUE = r11_RISK_VALUE;
	}
	public BigDecimal getR12_BOOK_VALUE() {
		return R12_BOOK_VALUE;
	}
	public void setR12_BOOK_VALUE(BigDecimal r12_BOOK_VALUE) {
		R12_BOOK_VALUE = r12_BOOK_VALUE;
	}
	public BigDecimal getR12_MARGINS() {
		return R12_MARGINS;
	}
	public void setR12_MARGINS(BigDecimal r12_MARGINS) {
		R12_MARGINS = r12_MARGINS;
	}
	public BigDecimal getR12_BOOK_VALUE_NET() {
		return R12_BOOK_VALUE_NET;
	}
	public void setR12_BOOK_VALUE_NET(BigDecimal r12_BOOK_VALUE_NET) {
		R12_BOOK_VALUE_NET = r12_BOOK_VALUE_NET;
	}
	public BigDecimal getR12_RW() {
		return R12_RW;
	}
	public void setR12_RW(BigDecimal r12_RW) {
		R12_RW = r12_RW;
	}
	public BigDecimal getR12_RISK_VALUE() {
		return R12_RISK_VALUE;
	}
	public void setR12_RISK_VALUE(BigDecimal r12_RISK_VALUE) {
		R12_RISK_VALUE = r12_RISK_VALUE;
	}
	public BigDecimal getR13_BOOK_VALUE() {
		return R13_BOOK_VALUE;
	}
	public void setR13_BOOK_VALUE(BigDecimal r13_BOOK_VALUE) {
		R13_BOOK_VALUE = r13_BOOK_VALUE;
	}
	public BigDecimal getR13_MARGINS() {
		return R13_MARGINS;
	}
	public void setR13_MARGINS(BigDecimal r13_MARGINS) {
		R13_MARGINS = r13_MARGINS;
	}
	public BigDecimal getR13_BOOK_VALUE_NET() {
		return R13_BOOK_VALUE_NET;
	}
	public void setR13_BOOK_VALUE_NET(BigDecimal r13_BOOK_VALUE_NET) {
		R13_BOOK_VALUE_NET = r13_BOOK_VALUE_NET;
	}
	public BigDecimal getR13_RW() {
		return R13_RW;
	}
	public void setR13_RW(BigDecimal r13_RW) {
		R13_RW = r13_RW;
	}
	public BigDecimal getR13_RISK_VALUE() {
		return R13_RISK_VALUE;
	}
	public void setR13_RISK_VALUE(BigDecimal r13_RISK_VALUE) {
		R13_RISK_VALUE = r13_RISK_VALUE;
	}
	public BigDecimal getR14_BOOK_VALUE() {
		return R14_BOOK_VALUE;
	}
	public void setR14_BOOK_VALUE(BigDecimal r14_BOOK_VALUE) {
		R14_BOOK_VALUE = r14_BOOK_VALUE;
	}
	public BigDecimal getR14_MARGINS() {
		return R14_MARGINS;
	}
	public void setR14_MARGINS(BigDecimal r14_MARGINS) {
		R14_MARGINS = r14_MARGINS;
	}
	public BigDecimal getR14_BOOK_VALUE_NET() {
		return R14_BOOK_VALUE_NET;
	}
	public void setR14_BOOK_VALUE_NET(BigDecimal r14_BOOK_VALUE_NET) {
		R14_BOOK_VALUE_NET = r14_BOOK_VALUE_NET;
	}
	public BigDecimal getR14_RW() {
		return R14_RW;
	}
	public void setR14_RW(BigDecimal r14_RW) {
		R14_RW = r14_RW;
	}
	public BigDecimal getR14_RISK_VALUE() {
		return R14_RISK_VALUE;
	}
	public void setR14_RISK_VALUE(BigDecimal r14_RISK_VALUE) {
		R14_RISK_VALUE = r14_RISK_VALUE;
	}
	public BigDecimal getR15_BOOK_VALUE() {
		return R15_BOOK_VALUE;
	}
	public void setR15_BOOK_VALUE(BigDecimal r15_BOOK_VALUE) {
		R15_BOOK_VALUE = r15_BOOK_VALUE;
	}
	public BigDecimal getR15_MARGINS() {
		return R15_MARGINS;
	}
	public void setR15_MARGINS(BigDecimal r15_MARGINS) {
		R15_MARGINS = r15_MARGINS;
	}
	public BigDecimal getR15_BOOK_VALUE_NET() {
		return R15_BOOK_VALUE_NET;
	}
	public void setR15_BOOK_VALUE_NET(BigDecimal r15_BOOK_VALUE_NET) {
		R15_BOOK_VALUE_NET = r15_BOOK_VALUE_NET;
	}
	public BigDecimal getR15_RW() {
		return R15_RW;
	}
	public void setR15_RW(BigDecimal r15_RW) {
		R15_RW = r15_RW;
	}
	public BigDecimal getR15_RISK_VALUE() {
		return R15_RISK_VALUE;
	}
	public void setR15_RISK_VALUE(BigDecimal r15_RISK_VALUE) {
		R15_RISK_VALUE = r15_RISK_VALUE;
	}
	public BigDecimal getR16_BOOK_VALUE() {
		return R16_BOOK_VALUE;
	}
	public void setR16_BOOK_VALUE(BigDecimal r16_BOOK_VALUE) {
		R16_BOOK_VALUE = r16_BOOK_VALUE;
	}
	public BigDecimal getR16_MARGINS() {
		return R16_MARGINS;
	}
	public void setR16_MARGINS(BigDecimal r16_MARGINS) {
		R16_MARGINS = r16_MARGINS;
	}
	public BigDecimal getR16_BOOK_VALUE_NET() {
		return R16_BOOK_VALUE_NET;
	}
	public void setR16_BOOK_VALUE_NET(BigDecimal r16_BOOK_VALUE_NET) {
		R16_BOOK_VALUE_NET = r16_BOOK_VALUE_NET;
	}
	public BigDecimal getR16_RW() {
		return R16_RW;
	}
	public void setR16_RW(BigDecimal r16_RW) {
		R16_RW = r16_RW;
	}
	public BigDecimal getR16_RISK_VALUE() {
		return R16_RISK_VALUE;
	}
	public void setR16_RISK_VALUE(BigDecimal r16_RISK_VALUE) {
		R16_RISK_VALUE = r16_RISK_VALUE;
	}
	public BigDecimal getR17_BOOK_VALUE() {
		return R17_BOOK_VALUE;
	}
	public void setR17_BOOK_VALUE(BigDecimal r17_BOOK_VALUE) {
		R17_BOOK_VALUE = r17_BOOK_VALUE;
	}
	public BigDecimal getR17_MARGINS() {
		return R17_MARGINS;
	}
	public void setR17_MARGINS(BigDecimal r17_MARGINS) {
		R17_MARGINS = r17_MARGINS;
	}
	public BigDecimal getR17_BOOK_VALUE_NET() {
		return R17_BOOK_VALUE_NET;
	}
	public void setR17_BOOK_VALUE_NET(BigDecimal r17_BOOK_VALUE_NET) {
		R17_BOOK_VALUE_NET = r17_BOOK_VALUE_NET;
	}
	public BigDecimal getR17_RW() {
		return R17_RW;
	}
	public void setR17_RW(BigDecimal r17_RW) {
		R17_RW = r17_RW;
	}
	public BigDecimal getR17_RISK_VALUE() {
		return R17_RISK_VALUE;
	}
	public void setR17_RISK_VALUE(BigDecimal r17_RISK_VALUE) {
		R17_RISK_VALUE = r17_RISK_VALUE;
	}
	public BigDecimal getR18_BOOK_VALUE() {
		return R18_BOOK_VALUE;
	}
	public void setR18_BOOK_VALUE(BigDecimal r18_BOOK_VALUE) {
		R18_BOOK_VALUE = r18_BOOK_VALUE;
	}
	public BigDecimal getR18_MARGINS() {
		return R18_MARGINS;
	}
	public void setR18_MARGINS(BigDecimal r18_MARGINS) {
		R18_MARGINS = r18_MARGINS;
	}
	public BigDecimal getR18_BOOK_VALUE_NET() {
		return R18_BOOK_VALUE_NET;
	}
	public void setR18_BOOK_VALUE_NET(BigDecimal r18_BOOK_VALUE_NET) {
		R18_BOOK_VALUE_NET = r18_BOOK_VALUE_NET;
	}
	public BigDecimal getR18_RW() {
		return R18_RW;
	}
	public void setR18_RW(BigDecimal r18_RW) {
		R18_RW = r18_RW;
	}
	public BigDecimal getR18_RISK_VALUE() {
		return R18_RISK_VALUE;
	}
	public void setR18_RISK_VALUE(BigDecimal r18_RISK_VALUE) {
		R18_RISK_VALUE = r18_RISK_VALUE;
	}
	public BigDecimal getR19_BOOK_VALUE() {
		return R19_BOOK_VALUE;
	}
	public void setR19_BOOK_VALUE(BigDecimal r19_BOOK_VALUE) {
		R19_BOOK_VALUE = r19_BOOK_VALUE;
	}
	public BigDecimal getR19_MARGINS() {
		return R19_MARGINS;
	}
	public void setR19_MARGINS(BigDecimal r19_MARGINS) {
		R19_MARGINS = r19_MARGINS;
	}
	public BigDecimal getR19_BOOK_VALUE_NET() {
		return R19_BOOK_VALUE_NET;
	}
	public void setR19_BOOK_VALUE_NET(BigDecimal r19_BOOK_VALUE_NET) {
		R19_BOOK_VALUE_NET = r19_BOOK_VALUE_NET;
	}
	public BigDecimal getR19_RW() {
		return R19_RW;
	}
	public void setR19_RW(BigDecimal r19_RW) {
		R19_RW = r19_RW;
	}
	public BigDecimal getR19_RISK_VALUE() {
		return R19_RISK_VALUE;
	}
	public void setR19_RISK_VALUE(BigDecimal r19_RISK_VALUE) {
		R19_RISK_VALUE = r19_RISK_VALUE;
	}
	public BigDecimal getR20_BOOK_VALUE() {
		return R20_BOOK_VALUE;
	}
	public void setR20_BOOK_VALUE(BigDecimal r20_BOOK_VALUE) {
		R20_BOOK_VALUE = r20_BOOK_VALUE;
	}
	public BigDecimal getR20_MARGINS() {
		return R20_MARGINS;
	}
	public void setR20_MARGINS(BigDecimal r20_MARGINS) {
		R20_MARGINS = r20_MARGINS;
	}
	public BigDecimal getR20_BOOK_VALUE_NET() {
		return R20_BOOK_VALUE_NET;
	}
	public void setR20_BOOK_VALUE_NET(BigDecimal r20_BOOK_VALUE_NET) {
		R20_BOOK_VALUE_NET = r20_BOOK_VALUE_NET;
	}
	public BigDecimal getR20_RW() {
		return R20_RW;
	}
	public void setR20_RW(BigDecimal r20_RW) {
		R20_RW = r20_RW;
	}
	public BigDecimal getR20_RISK_VALUE() {
		return R20_RISK_VALUE;
	}
	public void setR20_RISK_VALUE(BigDecimal r20_RISK_VALUE) {
		R20_RISK_VALUE = r20_RISK_VALUE;
	}
	public BigDecimal getR21_BOOK_VALUE() {
		return R21_BOOK_VALUE;
	}
	public void setR21_BOOK_VALUE(BigDecimal r21_BOOK_VALUE) {
		R21_BOOK_VALUE = r21_BOOK_VALUE;
	}
	public BigDecimal getR21_MARGINS() {
		return R21_MARGINS;
	}
	public void setR21_MARGINS(BigDecimal r21_MARGINS) {
		R21_MARGINS = r21_MARGINS;
	}
	public BigDecimal getR21_BOOK_VALUE_NET() {
		return R21_BOOK_VALUE_NET;
	}
	public void setR21_BOOK_VALUE_NET(BigDecimal r21_BOOK_VALUE_NET) {
		R21_BOOK_VALUE_NET = r21_BOOK_VALUE_NET;
	}
	public BigDecimal getR21_RW() {
		return R21_RW;
	}
	public void setR21_RW(BigDecimal r21_RW) {
		R21_RW = r21_RW;
	}
	public BigDecimal getR21_RISK_VALUE() {
		return R21_RISK_VALUE;
	}
	public void setR21_RISK_VALUE(BigDecimal r21_RISK_VALUE) {
		R21_RISK_VALUE = r21_RISK_VALUE;
	}
	public BigDecimal getR22_BOOK_VALUE() {
		return R22_BOOK_VALUE;
	}
	public void setR22_BOOK_VALUE(BigDecimal r22_BOOK_VALUE) {
		R22_BOOK_VALUE = r22_BOOK_VALUE;
	}
	public BigDecimal getR22_MARGINS() {
		return R22_MARGINS;
	}
	public void setR22_MARGINS(BigDecimal r22_MARGINS) {
		R22_MARGINS = r22_MARGINS;
	}
	public BigDecimal getR22_BOOK_VALUE_NET() {
		return R22_BOOK_VALUE_NET;
	}
	public void setR22_BOOK_VALUE_NET(BigDecimal r22_BOOK_VALUE_NET) {
		R22_BOOK_VALUE_NET = r22_BOOK_VALUE_NET;
	}
	public BigDecimal getR22_RW() {
		return R22_RW;
	}
	public void setR22_RW(BigDecimal r22_RW) {
		R22_RW = r22_RW;
	}
	public BigDecimal getR22_RISK_VALUE() {
		return R22_RISK_VALUE;
	}
	public void setR22_RISK_VALUE(BigDecimal r22_RISK_VALUE) {
		R22_RISK_VALUE = r22_RISK_VALUE;
	}
	public BigDecimal getR23_BOOK_VALUE() {
		return R23_BOOK_VALUE;
	}
	public void setR23_BOOK_VALUE(BigDecimal r23_BOOK_VALUE) {
		R23_BOOK_VALUE = r23_BOOK_VALUE;
	}
	public BigDecimal getR23_MARGINS() {
		return R23_MARGINS;
	}
	public void setR23_MARGINS(BigDecimal r23_MARGINS) {
		R23_MARGINS = r23_MARGINS;
	}
	public BigDecimal getR23_BOOK_VALUE_NET() {
		return R23_BOOK_VALUE_NET;
	}
	public void setR23_BOOK_VALUE_NET(BigDecimal r23_BOOK_VALUE_NET) {
		R23_BOOK_VALUE_NET = r23_BOOK_VALUE_NET;
	}
	public BigDecimal getR23_RW() {
		return R23_RW;
	}
	public void setR23_RW(BigDecimal r23_RW) {
		R23_RW = r23_RW;
	}
	public BigDecimal getR23_RISK_VALUE() {
		return R23_RISK_VALUE;
	}
	public void setR23_RISK_VALUE(BigDecimal r23_RISK_VALUE) {
		R23_RISK_VALUE = r23_RISK_VALUE;
	}
	public BigDecimal getR24_BOOK_VALUE() {
		return R24_BOOK_VALUE;
	}
	public void setR24_BOOK_VALUE(BigDecimal r24_BOOK_VALUE) {
		R24_BOOK_VALUE = r24_BOOK_VALUE;
	}
	public BigDecimal getR24_MARGINS() {
		return R24_MARGINS;
	}
	public void setR24_MARGINS(BigDecimal r24_MARGINS) {
		R24_MARGINS = r24_MARGINS;
	}
	public BigDecimal getR24_BOOK_VALUE_NET() {
		return R24_BOOK_VALUE_NET;
	}
	public void setR24_BOOK_VALUE_NET(BigDecimal r24_BOOK_VALUE_NET) {
		R24_BOOK_VALUE_NET = r24_BOOK_VALUE_NET;
	}
	public BigDecimal getR24_RW() {
		return R24_RW;
	}
	public void setR24_RW(BigDecimal r24_RW) {
		R24_RW = r24_RW;
	}
	public BigDecimal getR24_RISK_VALUE() {
		return R24_RISK_VALUE;
	}
	public void setR24_RISK_VALUE(BigDecimal r24_RISK_VALUE) {
		R24_RISK_VALUE = r24_RISK_VALUE;
	}
	public BigDecimal getR25_BOOK_VALUE() {
		return R25_BOOK_VALUE;
	}
	public void setR25_BOOK_VALUE(BigDecimal r25_BOOK_VALUE) {
		R25_BOOK_VALUE = r25_BOOK_VALUE;
	}
	public BigDecimal getR25_MARGINS() {
		return R25_MARGINS;
	}
	public void setR25_MARGINS(BigDecimal r25_MARGINS) {
		R25_MARGINS = r25_MARGINS;
	}
	public BigDecimal getR25_BOOK_VALUE_NET() {
		return R25_BOOK_VALUE_NET;
	}
	public void setR25_BOOK_VALUE_NET(BigDecimal r25_BOOK_VALUE_NET) {
		R25_BOOK_VALUE_NET = r25_BOOK_VALUE_NET;
	}
	public BigDecimal getR25_RW() {
		return R25_RW;
	}
	public void setR25_RW(BigDecimal r25_RW) {
		R25_RW = r25_RW;
	}
	public BigDecimal getR25_RISK_VALUE() {
		return R25_RISK_VALUE;
	}
	public void setR25_RISK_VALUE(BigDecimal r25_RISK_VALUE) {
		R25_RISK_VALUE = r25_RISK_VALUE;
	}
	public BigDecimal getR26_BOOK_VALUE() {
		return R26_BOOK_VALUE;
	}
	public void setR26_BOOK_VALUE(BigDecimal r26_BOOK_VALUE) {
		R26_BOOK_VALUE = r26_BOOK_VALUE;
	}
	public BigDecimal getR26_MARGINS() {
		return R26_MARGINS;
	}
	public void setR26_MARGINS(BigDecimal r26_MARGINS) {
		R26_MARGINS = r26_MARGINS;
	}
	public BigDecimal getR26_BOOK_VALUE_NET() {
		return R26_BOOK_VALUE_NET;
	}
	public void setR26_BOOK_VALUE_NET(BigDecimal r26_BOOK_VALUE_NET) {
		R26_BOOK_VALUE_NET = r26_BOOK_VALUE_NET;
	}
	public BigDecimal getR26_RW() {
		return R26_RW;
	}
	public void setR26_RW(BigDecimal r26_RW) {
		R26_RW = r26_RW;
	}
	public BigDecimal getR26_RISK_VALUE() {
		return R26_RISK_VALUE;
	}
	public void setR26_RISK_VALUE(BigDecimal r26_RISK_VALUE) {
		R26_RISK_VALUE = r26_RISK_VALUE;
	}
	public BigDecimal getR27_BOOK_VALUE() {
		return R27_BOOK_VALUE;
	}
	public void setR27_BOOK_VALUE(BigDecimal r27_BOOK_VALUE) {
		R27_BOOK_VALUE = r27_BOOK_VALUE;
	}
	public BigDecimal getR27_MARGINS() {
		return R27_MARGINS;
	}
	public void setR27_MARGINS(BigDecimal r27_MARGINS) {
		R27_MARGINS = r27_MARGINS;
	}
	public BigDecimal getR27_BOOK_VALUE_NET() {
		return R27_BOOK_VALUE_NET;
	}
	public void setR27_BOOK_VALUE_NET(BigDecimal r27_BOOK_VALUE_NET) {
		R27_BOOK_VALUE_NET = r27_BOOK_VALUE_NET;
	}
	public BigDecimal getR27_RW() {
		return R27_RW;
	}
	public void setR27_RW(BigDecimal r27_RW) {
		R27_RW = r27_RW;
	}
	public BigDecimal getR27_RISK_VALUE() {
		return R27_RISK_VALUE;
	}
	public void setR27_RISK_VALUE(BigDecimal r27_RISK_VALUE) {
		R27_RISK_VALUE = r27_RISK_VALUE;
	}
	public BigDecimal getR28_BOOK_VALUE() {
		return R28_BOOK_VALUE;
	}
	public void setR28_BOOK_VALUE(BigDecimal r28_BOOK_VALUE) {
		R28_BOOK_VALUE = r28_BOOK_VALUE;
	}
	public BigDecimal getR28_MARGINS() {
		return R28_MARGINS;
	}
	public void setR28_MARGINS(BigDecimal r28_MARGINS) {
		R28_MARGINS = r28_MARGINS;
	}
	public BigDecimal getR28_BOOK_VALUE_NET() {
		return R28_BOOK_VALUE_NET;
	}
	public void setR28_BOOK_VALUE_NET(BigDecimal r28_BOOK_VALUE_NET) {
		R28_BOOK_VALUE_NET = r28_BOOK_VALUE_NET;
	}
	public BigDecimal getR28_RW() {
		return R28_RW;
	}
	public void setR28_RW(BigDecimal r28_RW) {
		R28_RW = r28_RW;
	}
	public BigDecimal getR28_RISK_VALUE() {
		return R28_RISK_VALUE;
	}
	public void setR28_RISK_VALUE(BigDecimal r28_RISK_VALUE) {
		R28_RISK_VALUE = r28_RISK_VALUE;
	}
	public BigDecimal getR29_BOOK_VALUE() {
		return R29_BOOK_VALUE;
	}
	public void setR29_BOOK_VALUE(BigDecimal r29_BOOK_VALUE) {
		R29_BOOK_VALUE = r29_BOOK_VALUE;
	}
	public BigDecimal getR29_MARGINS() {
		return R29_MARGINS;
	}
	public void setR29_MARGINS(BigDecimal r29_MARGINS) {
		R29_MARGINS = r29_MARGINS;
	}
	public BigDecimal getR29_BOOK_VALUE_NET() {
		return R29_BOOK_VALUE_NET;
	}
	public void setR29_BOOK_VALUE_NET(BigDecimal r29_BOOK_VALUE_NET) {
		R29_BOOK_VALUE_NET = r29_BOOK_VALUE_NET;
	}
	public BigDecimal getR29_RW() {
		return R29_RW;
	}
	public void setR29_RW(BigDecimal r29_RW) {
		R29_RW = r29_RW;
	}
	public BigDecimal getR29_RISK_VALUE() {
		return R29_RISK_VALUE;
	}
	public void setR29_RISK_VALUE(BigDecimal r29_RISK_VALUE) {
		R29_RISK_VALUE = r29_RISK_VALUE;
	}
	public BigDecimal getR30_BOOK_VALUE() {
		return R30_BOOK_VALUE;
	}
	public void setR30_BOOK_VALUE(BigDecimal r30_BOOK_VALUE) {
		R30_BOOK_VALUE = r30_BOOK_VALUE;
	}
	public BigDecimal getR30_MARGINS() {
		return R30_MARGINS;
	}
	public void setR30_MARGINS(BigDecimal r30_MARGINS) {
		R30_MARGINS = r30_MARGINS;
	}
	public BigDecimal getR30_BOOK_VALUE_NET() {
		return R30_BOOK_VALUE_NET;
	}
	public void setR30_BOOK_VALUE_NET(BigDecimal r30_BOOK_VALUE_NET) {
		R30_BOOK_VALUE_NET = r30_BOOK_VALUE_NET;
	}
	public BigDecimal getR30_RW() {
		return R30_RW;
	}
	public void setR30_RW(BigDecimal r30_RW) {
		R30_RW = r30_RW;
	}
	public BigDecimal getR30_RISK_VALUE() {
		return R30_RISK_VALUE;
	}
	public void setR30_RISK_VALUE(BigDecimal r30_RISK_VALUE) {
		R30_RISK_VALUE = r30_RISK_VALUE;
	}
	public BigDecimal getR31_BOOK_VALUE() {
		return R31_BOOK_VALUE;
	}
	public void setR31_BOOK_VALUE(BigDecimal r31_BOOK_VALUE) {
		R31_BOOK_VALUE = r31_BOOK_VALUE;
	}
	public BigDecimal getR31_MARGINS() {
		return R31_MARGINS;
	}
	public void setR31_MARGINS(BigDecimal r31_MARGINS) {
		R31_MARGINS = r31_MARGINS;
	}
	public BigDecimal getR31_BOOK_VALUE_NET() {
		return R31_BOOK_VALUE_NET;
	}
	public void setR31_BOOK_VALUE_NET(BigDecimal r31_BOOK_VALUE_NET) {
		R31_BOOK_VALUE_NET = r31_BOOK_VALUE_NET;
	}
	public BigDecimal getR31_RW() {
		return R31_RW;
	}
	public void setR31_RW(BigDecimal r31_RW) {
		R31_RW = r31_RW;
	}
	public BigDecimal getR31_RISK_VALUE() {
		return R31_RISK_VALUE;
	}
	public void setR31_RISK_VALUE(BigDecimal r31_RISK_VALUE) {
		R31_RISK_VALUE = r31_RISK_VALUE;
	}
	public BigDecimal getR32_BOOK_VALUE() {
		return R32_BOOK_VALUE;
	}
	public void setR32_BOOK_VALUE(BigDecimal r32_BOOK_VALUE) {
		R32_BOOK_VALUE = r32_BOOK_VALUE;
	}
	public BigDecimal getR32_MARGINS() {
		return R32_MARGINS;
	}
	public void setR32_MARGINS(BigDecimal r32_MARGINS) {
		R32_MARGINS = r32_MARGINS;
	}
	public BigDecimal getR32_BOOK_VALUE_NET() {
		return R32_BOOK_VALUE_NET;
	}
	public void setR32_BOOK_VALUE_NET(BigDecimal r32_BOOK_VALUE_NET) {
		R32_BOOK_VALUE_NET = r32_BOOK_VALUE_NET;
	}
	public BigDecimal getR32_RW() {
		return R32_RW;
	}
	public void setR32_RW(BigDecimal r32_RW) {
		R32_RW = r32_RW;
	}
	public BigDecimal getR32_RISK_VALUE() {
		return R32_RISK_VALUE;
	}
	public void setR32_RISK_VALUE(BigDecimal r32_RISK_VALUE) {
		R32_RISK_VALUE = r32_RISK_VALUE;
	}
	public BigDecimal getR33_BOOK_VALUE() {
		return R33_BOOK_VALUE;
	}
	public void setR33_BOOK_VALUE(BigDecimal r33_BOOK_VALUE) {
		R33_BOOK_VALUE = r33_BOOK_VALUE;
	}
	public BigDecimal getR33_MARGINS() {
		return R33_MARGINS;
	}
	public void setR33_MARGINS(BigDecimal r33_MARGINS) {
		R33_MARGINS = r33_MARGINS;
	}
	public BigDecimal getR33_BOOK_VALUE_NET() {
		return R33_BOOK_VALUE_NET;
	}
	public void setR33_BOOK_VALUE_NET(BigDecimal r33_BOOK_VALUE_NET) {
		R33_BOOK_VALUE_NET = r33_BOOK_VALUE_NET;
	}
	public BigDecimal getR33_RW() {
		return R33_RW;
	}
	public void setR33_RW(BigDecimal r33_RW) {
		R33_RW = r33_RW;
	}
	public BigDecimal getR33_RISK_VALUE() {
		return R33_RISK_VALUE;
	}
	public void setR33_RISK_VALUE(BigDecimal r33_RISK_VALUE) {
		R33_RISK_VALUE = r33_RISK_VALUE;
	}
	public BigDecimal getR34_BOOK_VALUE() {
		return R34_BOOK_VALUE;
	}
	public void setR34_BOOK_VALUE(BigDecimal r34_BOOK_VALUE) {
		R34_BOOK_VALUE = r34_BOOK_VALUE;
	}
	public BigDecimal getR34_MARGINS() {
		return R34_MARGINS;
	}
	public void setR34_MARGINS(BigDecimal r34_MARGINS) {
		R34_MARGINS = r34_MARGINS;
	}
	public BigDecimal getR34_BOOK_VALUE_NET() {
		return R34_BOOK_VALUE_NET;
	}
	public void setR34_BOOK_VALUE_NET(BigDecimal r34_BOOK_VALUE_NET) {
		R34_BOOK_VALUE_NET = r34_BOOK_VALUE_NET;
	}
	public BigDecimal getR34_RW() {
		return R34_RW;
	}
	public void setR34_RW(BigDecimal r34_RW) {
		R34_RW = r34_RW;
	}
	public BigDecimal getR34_RISK_VALUE() {
		return R34_RISK_VALUE;
	}
	public void setR34_RISK_VALUE(BigDecimal r34_RISK_VALUE) {
		R34_RISK_VALUE = r34_RISK_VALUE;
	}
	public BigDecimal getR35_BOOK_VALUE() {
		return R35_BOOK_VALUE;
	}
	public void setR35_BOOK_VALUE(BigDecimal r35_BOOK_VALUE) {
		R35_BOOK_VALUE = r35_BOOK_VALUE;
	}
	public BigDecimal getR35_MARGINS() {
		return R35_MARGINS;
	}
	public void setR35_MARGINS(BigDecimal r35_MARGINS) {
		R35_MARGINS = r35_MARGINS;
	}
	public BigDecimal getR35_BOOK_VALUE_NET() {
		return R35_BOOK_VALUE_NET;
	}
	public void setR35_BOOK_VALUE_NET(BigDecimal r35_BOOK_VALUE_NET) {
		R35_BOOK_VALUE_NET = r35_BOOK_VALUE_NET;
	}
	public BigDecimal getR35_RW() {
		return R35_RW;
	}
	public void setR35_RW(BigDecimal r35_RW) {
		R35_RW = r35_RW;
	}
	public BigDecimal getR35_RISK_VALUE() {
		return R35_RISK_VALUE;
	}
	public void setR35_RISK_VALUE(BigDecimal r35_RISK_VALUE) {
		R35_RISK_VALUE = r35_RISK_VALUE;
	}
	public BigDecimal getR36_BOOK_VALUE() {
		return R36_BOOK_VALUE;
	}
	public void setR36_BOOK_VALUE(BigDecimal r36_BOOK_VALUE) {
		R36_BOOK_VALUE = r36_BOOK_VALUE;
	}
	public BigDecimal getR36_MARGINS() {
		return R36_MARGINS;
	}
	public void setR36_MARGINS(BigDecimal r36_MARGINS) {
		R36_MARGINS = r36_MARGINS;
	}
	public BigDecimal getR36_BOOK_VALUE_NET() {
		return R36_BOOK_VALUE_NET;
	}
	public void setR36_BOOK_VALUE_NET(BigDecimal r36_BOOK_VALUE_NET) {
		R36_BOOK_VALUE_NET = r36_BOOK_VALUE_NET;
	}
	public BigDecimal getR36_RW() {
		return R36_RW;
	}
	public void setR36_RW(BigDecimal r36_RW) {
		R36_RW = r36_RW;
	}
	public BigDecimal getR36_RISK_VALUE() {
		return R36_RISK_VALUE;
	}
	public void setR36_RISK_VALUE(BigDecimal r36_RISK_VALUE) {
		R36_RISK_VALUE = r36_RISK_VALUE;
	}
	public BigDecimal getR37_BOOK_VALUE() {
		return R37_BOOK_VALUE;
	}
	public void setR37_BOOK_VALUE(BigDecimal r37_BOOK_VALUE) {
		R37_BOOK_VALUE = r37_BOOK_VALUE;
	}
	public BigDecimal getR37_MARGINS() {
		return R37_MARGINS;
	}
	public void setR37_MARGINS(BigDecimal r37_MARGINS) {
		R37_MARGINS = r37_MARGINS;
	}
	public BigDecimal getR37_BOOK_VALUE_NET() {
		return R37_BOOK_VALUE_NET;
	}
	public void setR37_BOOK_VALUE_NET(BigDecimal r37_BOOK_VALUE_NET) {
		R37_BOOK_VALUE_NET = r37_BOOK_VALUE_NET;
	}
	public BigDecimal getR37_RW() {
		return R37_RW;
	}
	public void setR37_RW(BigDecimal r37_RW) {
		R37_RW = r37_RW;
	}
	public BigDecimal getR37_RISK_VALUE() {
		return R37_RISK_VALUE;
	}
	public void setR37_RISK_VALUE(BigDecimal r37_RISK_VALUE) {
		R37_RISK_VALUE = r37_RISK_VALUE;
	}
	public BigDecimal getR38_BOOK_VALUE() {
		return R38_BOOK_VALUE;
	}
	public void setR38_BOOK_VALUE(BigDecimal r38_BOOK_VALUE) {
		R38_BOOK_VALUE = r38_BOOK_VALUE;
	}
	public BigDecimal getR38_MARGINS() {
		return R38_MARGINS;
	}
	public void setR38_MARGINS(BigDecimal r38_MARGINS) {
		R38_MARGINS = r38_MARGINS;
	}
	public BigDecimal getR38_BOOK_VALUE_NET() {
		return R38_BOOK_VALUE_NET;
	}
	public void setR38_BOOK_VALUE_NET(BigDecimal r38_BOOK_VALUE_NET) {
		R38_BOOK_VALUE_NET = r38_BOOK_VALUE_NET;
	}
	public BigDecimal getR38_RW() {
		return R38_RW;
	}
	public void setR38_RW(BigDecimal r38_RW) {
		R38_RW = r38_RW;
	}
	public BigDecimal getR38_RISK_VALUE() {
		return R38_RISK_VALUE;
	}
	public void setR38_RISK_VALUE(BigDecimal r38_RISK_VALUE) {
		R38_RISK_VALUE = r38_RISK_VALUE;
	}
	public BigDecimal getR39_BOOK_VALUE() {
		return R39_BOOK_VALUE;
	}
	public void setR39_BOOK_VALUE(BigDecimal r39_BOOK_VALUE) {
		R39_BOOK_VALUE = r39_BOOK_VALUE;
	}
	public BigDecimal getR39_MARGINS() {
		return R39_MARGINS;
	}
	public void setR39_MARGINS(BigDecimal r39_MARGINS) {
		R39_MARGINS = r39_MARGINS;
	}
	public BigDecimal getR39_BOOK_VALUE_NET() {
		return R39_BOOK_VALUE_NET;
	}
	public void setR39_BOOK_VALUE_NET(BigDecimal r39_BOOK_VALUE_NET) {
		R39_BOOK_VALUE_NET = r39_BOOK_VALUE_NET;
	}
	public BigDecimal getR39_RW() {
		return R39_RW;
	}
	public void setR39_RW(BigDecimal r39_RW) {
		R39_RW = r39_RW;
	}
	public BigDecimal getR39_RISK_VALUE() {
		return R39_RISK_VALUE;
	}
	public void setR39_RISK_VALUE(BigDecimal r39_RISK_VALUE) {
		R39_RISK_VALUE = r39_RISK_VALUE;
	}
	public BigDecimal getR40_BOOK_VALUE() {
		return R40_BOOK_VALUE;
	}
	public void setR40_BOOK_VALUE(BigDecimal r40_BOOK_VALUE) {
		R40_BOOK_VALUE = r40_BOOK_VALUE;
	}
	public BigDecimal getR40_MARGINS() {
		return R40_MARGINS;
	}
	public void setR40_MARGINS(BigDecimal r40_MARGINS) {
		R40_MARGINS = r40_MARGINS;
	}
	public BigDecimal getR40_BOOK_VALUE_NET() {
		return R40_BOOK_VALUE_NET;
	}
	public void setR40_BOOK_VALUE_NET(BigDecimal r40_BOOK_VALUE_NET) {
		R40_BOOK_VALUE_NET = r40_BOOK_VALUE_NET;
	}
	public BigDecimal getR40_RW() {
		return R40_RW;
	}
	public void setR40_RW(BigDecimal r40_RW) {
		R40_RW = r40_RW;
	}
	public BigDecimal getR40_RISK_VALUE() {
		return R40_RISK_VALUE;
	}
	public void setR40_RISK_VALUE(BigDecimal r40_RISK_VALUE) {
		R40_RISK_VALUE = r40_RISK_VALUE;
	}
	public BigDecimal getR41_BOOK_VALUE() {
		return R41_BOOK_VALUE;
	}
	public void setR41_BOOK_VALUE(BigDecimal r41_BOOK_VALUE) {
		R41_BOOK_VALUE = r41_BOOK_VALUE;
	}
	public BigDecimal getR41_MARGINS() {
		return R41_MARGINS;
	}
	public void setR41_MARGINS(BigDecimal r41_MARGINS) {
		R41_MARGINS = r41_MARGINS;
	}
	public BigDecimal getR41_BOOK_VALUE_NET() {
		return R41_BOOK_VALUE_NET;
	}
	public void setR41_BOOK_VALUE_NET(BigDecimal r41_BOOK_VALUE_NET) {
		R41_BOOK_VALUE_NET = r41_BOOK_VALUE_NET;
	}
	public BigDecimal getR41_RW() {
		return R41_RW;
	}
	public void setR41_RW(BigDecimal r41_RW) {
		R41_RW = r41_RW;
	}
	public BigDecimal getR41_RISK_VALUE() {
		return R41_RISK_VALUE;
	}
	public void setR41_RISK_VALUE(BigDecimal r41_RISK_VALUE) {
		R41_RISK_VALUE = r41_RISK_VALUE;
	}
	public BigDecimal getR42_BOOK_VALUE() {
		return R42_BOOK_VALUE;
	}
	public void setR42_BOOK_VALUE(BigDecimal r42_BOOK_VALUE) {
		R42_BOOK_VALUE = r42_BOOK_VALUE;
	}
	public BigDecimal getR42_MARGINS() {
		return R42_MARGINS;
	}
	public void setR42_MARGINS(BigDecimal r42_MARGINS) {
		R42_MARGINS = r42_MARGINS;
	}
	public BigDecimal getR42_BOOK_VALUE_NET() {
		return R42_BOOK_VALUE_NET;
	}
	public void setR42_BOOK_VALUE_NET(BigDecimal r42_BOOK_VALUE_NET) {
		R42_BOOK_VALUE_NET = r42_BOOK_VALUE_NET;
	}
	public BigDecimal getR42_RW() {
		return R42_RW;
	}
	public void setR42_RW(BigDecimal r42_RW) {
		R42_RW = r42_RW;
	}
	public BigDecimal getR42_RISK_VALUE() {
		return R42_RISK_VALUE;
	}
	public void setR42_RISK_VALUE(BigDecimal r42_RISK_VALUE) {
		R42_RISK_VALUE = r42_RISK_VALUE;
	}
	public BigDecimal getR43_BOOK_VALUE() {
		return R43_BOOK_VALUE;
	}
	public void setR43_BOOK_VALUE(BigDecimal r43_BOOK_VALUE) {
		R43_BOOK_VALUE = r43_BOOK_VALUE;
	}
	public BigDecimal getR43_MARGINS() {
		return R43_MARGINS;
	}
	public void setR43_MARGINS(BigDecimal r43_MARGINS) {
		R43_MARGINS = r43_MARGINS;
	}
	public BigDecimal getR43_BOOK_VALUE_NET() {
		return R43_BOOK_VALUE_NET;
	}
	public void setR43_BOOK_VALUE_NET(BigDecimal r43_BOOK_VALUE_NET) {
		R43_BOOK_VALUE_NET = r43_BOOK_VALUE_NET;
	}
	public BigDecimal getR43_RW() {
		return R43_RW;
	}
	public void setR43_RW(BigDecimal r43_RW) {
		R43_RW = r43_RW;
	}
	public BigDecimal getR43_RISK_VALUE() {
		return R43_RISK_VALUE;
	}
	public void setR43_RISK_VALUE(BigDecimal r43_RISK_VALUE) {
		R43_RISK_VALUE = r43_RISK_VALUE;
	}
	public BigDecimal getR44_BOOK_VALUE() {
		return R44_BOOK_VALUE;
	}
	public void setR44_BOOK_VALUE(BigDecimal r44_BOOK_VALUE) {
		R44_BOOK_VALUE = r44_BOOK_VALUE;
	}
	public BigDecimal getR44_MARGINS() {
		return R44_MARGINS;
	}
	public void setR44_MARGINS(BigDecimal r44_MARGINS) {
		R44_MARGINS = r44_MARGINS;
	}
	public BigDecimal getR44_BOOK_VALUE_NET() {
		return R44_BOOK_VALUE_NET;
	}
	public void setR44_BOOK_VALUE_NET(BigDecimal r44_BOOK_VALUE_NET) {
		R44_BOOK_VALUE_NET = r44_BOOK_VALUE_NET;
	}
	public BigDecimal getR44_RW() {
		return R44_RW;
	}
	public void setR44_RW(BigDecimal r44_RW) {
		R44_RW = r44_RW;
	}
	public BigDecimal getR44_RISK_VALUE() {
		return R44_RISK_VALUE;
	}
	public void setR44_RISK_VALUE(BigDecimal r44_RISK_VALUE) {
		R44_RISK_VALUE = r44_RISK_VALUE;
	}
	public BigDecimal getR45_BOOK_VALUE() {
		return R45_BOOK_VALUE;
	}
	public void setR45_BOOK_VALUE(BigDecimal r45_BOOK_VALUE) {
		R45_BOOK_VALUE = r45_BOOK_VALUE;
	}
	public BigDecimal getR45_MARGINS() {
		return R45_MARGINS;
	}
	public void setR45_MARGINS(BigDecimal r45_MARGINS) {
		R45_MARGINS = r45_MARGINS;
	}
	public BigDecimal getR45_BOOK_VALUE_NET() {
		return R45_BOOK_VALUE_NET;
	}
	public void setR45_BOOK_VALUE_NET(BigDecimal r45_BOOK_VALUE_NET) {
		R45_BOOK_VALUE_NET = r45_BOOK_VALUE_NET;
	}
	public BigDecimal getR45_RW() {
		return R45_RW;
	}
	public void setR45_RW(BigDecimal r45_RW) {
		R45_RW = r45_RW;
	}
	public BigDecimal getR45_RISK_VALUE() {
		return R45_RISK_VALUE;
	}
	public void setR45_RISK_VALUE(BigDecimal r45_RISK_VALUE) {
		R45_RISK_VALUE = r45_RISK_VALUE;
	}
	public BigDecimal getR46_BOOK_VALUE() {
		return R46_BOOK_VALUE;
	}
	public void setR46_BOOK_VALUE(BigDecimal r46_BOOK_VALUE) {
		R46_BOOK_VALUE = r46_BOOK_VALUE;
	}
	public BigDecimal getR46_MARGINS() {
		return R46_MARGINS;
	}
	public void setR46_MARGINS(BigDecimal r46_MARGINS) {
		R46_MARGINS = r46_MARGINS;
	}
	public BigDecimal getR46_BOOK_VALUE_NET() {
		return R46_BOOK_VALUE_NET;
	}
	public void setR46_BOOK_VALUE_NET(BigDecimal r46_BOOK_VALUE_NET) {
		R46_BOOK_VALUE_NET = r46_BOOK_VALUE_NET;
	}
	public BigDecimal getR46_RW() {
		return R46_RW;
	}
	public void setR46_RW(BigDecimal r46_RW) {
		R46_RW = r46_RW;
	}
	public BigDecimal getR46_RISK_VALUE() {
		return R46_RISK_VALUE;
	}
	public void setR46_RISK_VALUE(BigDecimal r46_RISK_VALUE) {
		R46_RISK_VALUE = r46_RISK_VALUE;
	}
	public BigDecimal getR48_BOOK_VALUE() {
		return R48_BOOK_VALUE;
	}
	public void setR48_BOOK_VALUE(BigDecimal r48_BOOK_VALUE) {
		R48_BOOK_VALUE = r48_BOOK_VALUE;
	}
	public BigDecimal getR48_MARGINS() {
		return R48_MARGINS;
	}
	public void setR48_MARGINS(BigDecimal r48_MARGINS) {
		R48_MARGINS = r48_MARGINS;
	}
	public BigDecimal getR48_BOOK_VALUE_NET() {
		return R48_BOOK_VALUE_NET;
	}
	public void setR48_BOOK_VALUE_NET(BigDecimal r48_BOOK_VALUE_NET) {
		R48_BOOK_VALUE_NET = r48_BOOK_VALUE_NET;
	}
	public BigDecimal getR48_RW() {
		return R48_RW;
	}
	public void setR48_RW(BigDecimal r48_RW) {
		R48_RW = r48_RW;
	}
	public BigDecimal getR48_RISK_VALUE() {
		return R48_RISK_VALUE;
	}
	public void setR48_RISK_VALUE(BigDecimal r48_RISK_VALUE) {
		R48_RISK_VALUE = r48_RISK_VALUE;
	}
	public BigDecimal getR61_BOOK_VALUE() {
		return R61_BOOK_VALUE;
	}
	public void setR61_BOOK_VALUE(BigDecimal r61_BOOK_VALUE) {
		R61_BOOK_VALUE = r61_BOOK_VALUE;
	}
	public BigDecimal getR61_MARGINS() {
		return R61_MARGINS;
	}
	public void setR61_MARGINS(BigDecimal r61_MARGINS) {
		R61_MARGINS = r61_MARGINS;
	}
	public BigDecimal getR61_BOOK_VALUE_NET() {
		return R61_BOOK_VALUE_NET;
	}
	public void setR61_BOOK_VALUE_NET(BigDecimal r61_BOOK_VALUE_NET) {
		R61_BOOK_VALUE_NET = r61_BOOK_VALUE_NET;
	}
	public BigDecimal getR61_RW() {
		return R61_RW;
	}
	public void setR61_RW(BigDecimal r61_RW) {
		R61_RW = r61_RW;
	}
	public BigDecimal getR61_RISK_VALUE() {
		return R61_RISK_VALUE;
	}
	public void setR61_RISK_VALUE(BigDecimal r61_RISK_VALUE) {
		R61_RISK_VALUE = r61_RISK_VALUE;
	}
	public BigDecimal getR63_BOOK_VALUE() {
		return R63_BOOK_VALUE;
	}
	public void setR63_BOOK_VALUE(BigDecimal r63_BOOK_VALUE) {
		R63_BOOK_VALUE = r63_BOOK_VALUE;
	}
	public BigDecimal getR63_MARGINS() {
		return R63_MARGINS;
	}
	public void setR63_MARGINS(BigDecimal r63_MARGINS) {
		R63_MARGINS = r63_MARGINS;
	}
	public BigDecimal getR63_BOOK_VALUE_NET() {
		return R63_BOOK_VALUE_NET;
	}
	public void setR63_BOOK_VALUE_NET(BigDecimal r63_BOOK_VALUE_NET) {
		R63_BOOK_VALUE_NET = r63_BOOK_VALUE_NET;
	}
	public BigDecimal getR63_RW() {
		return R63_RW;
	}
	public void setR63_RW(BigDecimal r63_RW) {
		R63_RW = r63_RW;
	}
	public BigDecimal getR63_RISK_VALUE() {
		return R63_RISK_VALUE;
	}
	public void setR63_RISK_VALUE(BigDecimal r63_RISK_VALUE) {
		R63_RISK_VALUE = r63_RISK_VALUE;
	}
	public BigDecimal getR64_BOOK_VALUE() {
		return R64_BOOK_VALUE;
	}
	public void setR64_BOOK_VALUE(BigDecimal r64_BOOK_VALUE) {
		R64_BOOK_VALUE = r64_BOOK_VALUE;
	}
	public BigDecimal getR64_MARGINS() {
		return R64_MARGINS;
	}
	public void setR64_MARGINS(BigDecimal r64_MARGINS) {
		R64_MARGINS = r64_MARGINS;
	}
	public BigDecimal getR64_BOOK_VALUE_NET() {
		return R64_BOOK_VALUE_NET;
	}
	public void setR64_BOOK_VALUE_NET(BigDecimal r64_BOOK_VALUE_NET) {
		R64_BOOK_VALUE_NET = r64_BOOK_VALUE_NET;
	}
	public BigDecimal getR64_RW() {
		return R64_RW;
	}
	public void setR64_RW(BigDecimal r64_RW) {
		R64_RW = r64_RW;
	}
	public BigDecimal getR64_RISK_VALUE() {
		return R64_RISK_VALUE;
	}
	public void setR64_RISK_VALUE(BigDecimal r64_RISK_VALUE) {
		R64_RISK_VALUE = r64_RISK_VALUE;
	}
	public BigDecimal getR65_BOOK_VALUE() {
		return R65_BOOK_VALUE;
	}
	public void setR65_BOOK_VALUE(BigDecimal r65_BOOK_VALUE) {
		R65_BOOK_VALUE = r65_BOOK_VALUE;
	}
	public BigDecimal getR65_MARGINS() {
		return R65_MARGINS;
	}
	public void setR65_MARGINS(BigDecimal r65_MARGINS) {
		R65_MARGINS = r65_MARGINS;
	}
	public BigDecimal getR65_BOOK_VALUE_NET() {
		return R65_BOOK_VALUE_NET;
	}
	public void setR65_BOOK_VALUE_NET(BigDecimal r65_BOOK_VALUE_NET) {
		R65_BOOK_VALUE_NET = r65_BOOK_VALUE_NET;
	}
	public BigDecimal getR65_RW() {
		return R65_RW;
	}
	public void setR65_RW(BigDecimal r65_RW) {
		R65_RW = r65_RW;
	}
	public BigDecimal getR65_RISK_VALUE() {
		return R65_RISK_VALUE;
	}
	public void setR65_RISK_VALUE(BigDecimal r65_RISK_VALUE) {
		R65_RISK_VALUE = r65_RISK_VALUE;
	}
	public BigDecimal getR66_BOOK_VALUE() {
		return R66_BOOK_VALUE;
	}
	public void setR66_BOOK_VALUE(BigDecimal r66_BOOK_VALUE) {
		R66_BOOK_VALUE = r66_BOOK_VALUE;
	}
	public BigDecimal getR66_MARGINS() {
		return R66_MARGINS;
	}
	public void setR66_MARGINS(BigDecimal r66_MARGINS) {
		R66_MARGINS = r66_MARGINS;
	}
	public BigDecimal getR66_BOOK_VALUE_NET() {
		return R66_BOOK_VALUE_NET;
	}
	public void setR66_BOOK_VALUE_NET(BigDecimal r66_BOOK_VALUE_NET) {
		R66_BOOK_VALUE_NET = r66_BOOK_VALUE_NET;
	}
	public BigDecimal getR66_RW() {
		return R66_RW;
	}
	public void setR66_RW(BigDecimal r66_RW) {
		R66_RW = r66_RW;
	}
	public BigDecimal getR66_RISK_VALUE() {
		return R66_RISK_VALUE;
	}
	public void setR66_RISK_VALUE(BigDecimal r66_RISK_VALUE) {
		R66_RISK_VALUE = r66_RISK_VALUE;
	}
	public BigDecimal getR67_BOOK_VALUE() {
		return R67_BOOK_VALUE;
	}
	public void setR67_BOOK_VALUE(BigDecimal r67_BOOK_VALUE) {
		R67_BOOK_VALUE = r67_BOOK_VALUE;
	}
	public BigDecimal getR67_MARGINS() {
		return R67_MARGINS;
	}
	public void setR67_MARGINS(BigDecimal r67_MARGINS) {
		R67_MARGINS = r67_MARGINS;
	}
	public BigDecimal getR67_BOOK_VALUE_NET() {
		return R67_BOOK_VALUE_NET;
	}
	public void setR67_BOOK_VALUE_NET(BigDecimal r67_BOOK_VALUE_NET) {
		R67_BOOK_VALUE_NET = r67_BOOK_VALUE_NET;
	}
	public BigDecimal getR67_RW() {
		return R67_RW;
	}
	public void setR67_RW(BigDecimal r67_RW) {
		R67_RW = r67_RW;
	}
	public BigDecimal getR67_RISK_VALUE() {
		return R67_RISK_VALUE;
	}
	public void setR67_RISK_VALUE(BigDecimal r67_RISK_VALUE) {
		R67_RISK_VALUE = r67_RISK_VALUE;
	}
	public BigDecimal getR68_BOOK_VALUE() {
		return R68_BOOK_VALUE;
	}
	public void setR68_BOOK_VALUE(BigDecimal r68_BOOK_VALUE) {
		R68_BOOK_VALUE = r68_BOOK_VALUE;
	}
	public BigDecimal getR68_MARGINS() {
		return R68_MARGINS;
	}
	public void setR68_MARGINS(BigDecimal r68_MARGINS) {
		R68_MARGINS = r68_MARGINS;
	}
	public BigDecimal getR68_BOOK_VALUE_NET() {
		return R68_BOOK_VALUE_NET;
	}
	public void setR68_BOOK_VALUE_NET(BigDecimal r68_BOOK_VALUE_NET) {
		R68_BOOK_VALUE_NET = r68_BOOK_VALUE_NET;
	}
	public BigDecimal getR68_RW() {
		return R68_RW;
	}
	public void setR68_RW(BigDecimal r68_RW) {
		R68_RW = r68_RW;
	}
	public BigDecimal getR68_RISK_VALUE() {
		return R68_RISK_VALUE;
	}
	public void setR68_RISK_VALUE(BigDecimal r68_RISK_VALUE) {
		R68_RISK_VALUE = r68_RISK_VALUE;
	}
	public BigDecimal getR69_BOOK_VALUE() {
		return R69_BOOK_VALUE;
	}
	public void setR69_BOOK_VALUE(BigDecimal r69_BOOK_VALUE) {
		R69_BOOK_VALUE = r69_BOOK_VALUE;
	}
	public BigDecimal getR69_MARGINS() {
		return R69_MARGINS;
	}
	public void setR69_MARGINS(BigDecimal r69_MARGINS) {
		R69_MARGINS = r69_MARGINS;
	}
	public BigDecimal getR69_BOOK_VALUE_NET() {
		return R69_BOOK_VALUE_NET;
	}
	public void setR69_BOOK_VALUE_NET(BigDecimal r69_BOOK_VALUE_NET) {
		R69_BOOK_VALUE_NET = r69_BOOK_VALUE_NET;
	}
	public BigDecimal getR69_RW() {
		return R69_RW;
	}
	public void setR69_RW(BigDecimal r69_RW) {
		R69_RW = r69_RW;
	}
	public BigDecimal getR69_RISK_VALUE() {
		return R69_RISK_VALUE;
	}
	public void setR69_RISK_VALUE(BigDecimal r69_RISK_VALUE) {
		R69_RISK_VALUE = r69_RISK_VALUE;
	}
	public BigDecimal getR70_BOOK_VALUE() {
		return R70_BOOK_VALUE;
	}
	public void setR70_BOOK_VALUE(BigDecimal r70_BOOK_VALUE) {
		R70_BOOK_VALUE = r70_BOOK_VALUE;
	}
	public BigDecimal getR70_MARGINS() {
		return R70_MARGINS;
	}
	public void setR70_MARGINS(BigDecimal r70_MARGINS) {
		R70_MARGINS = r70_MARGINS;
	}
	public BigDecimal getR70_BOOK_VALUE_NET() {
		return R70_BOOK_VALUE_NET;
	}
	public void setR70_BOOK_VALUE_NET(BigDecimal r70_BOOK_VALUE_NET) {
		R70_BOOK_VALUE_NET = r70_BOOK_VALUE_NET;
	}
	public BigDecimal getR70_RW() {
		return R70_RW;
	}
	public void setR70_RW(BigDecimal r70_RW) {
		R70_RW = r70_RW;
	}
	public BigDecimal getR70_RISK_VALUE() {
		return R70_RISK_VALUE;
	}
	public void setR70_RISK_VALUE(BigDecimal r70_RISK_VALUE) {
		R70_RISK_VALUE = r70_RISK_VALUE;
	}
	public BigDecimal getR71_BOOK_VALUE() {
		return R71_BOOK_VALUE;
	}
	public void setR71_BOOK_VALUE(BigDecimal r71_BOOK_VALUE) {
		R71_BOOK_VALUE = r71_BOOK_VALUE;
	}
	public BigDecimal getR71_MARGINS() {
		return R71_MARGINS;
	}
	public void setR71_MARGINS(BigDecimal r71_MARGINS) {
		R71_MARGINS = r71_MARGINS;
	}
	public BigDecimal getR71_BOOK_VALUE_NET() {
		return R71_BOOK_VALUE_NET;
	}
	public void setR71_BOOK_VALUE_NET(BigDecimal r71_BOOK_VALUE_NET) {
		R71_BOOK_VALUE_NET = r71_BOOK_VALUE_NET;
	}
	public BigDecimal getR71_RW() {
		return R71_RW;
	}
	public void setR71_RW(BigDecimal r71_RW) {
		R71_RW = r71_RW;
	}
	public BigDecimal getR71_RISK_VALUE() {
		return R71_RISK_VALUE;
	}
	public void setR71_RISK_VALUE(BigDecimal r71_RISK_VALUE) {
		R71_RISK_VALUE = r71_RISK_VALUE;
	}
	public BigDecimal getR72_BOOK_VALUE() {
		return R72_BOOK_VALUE;
	}
	public void setR72_BOOK_VALUE(BigDecimal r72_BOOK_VALUE) {
		R72_BOOK_VALUE = r72_BOOK_VALUE;
	}
	public BigDecimal getR72_MARGINS() {
		return R72_MARGINS;
	}
	public void setR72_MARGINS(BigDecimal r72_MARGINS) {
		R72_MARGINS = r72_MARGINS;
	}
	public BigDecimal getR72_BOOK_VALUE_NET() {
		return R72_BOOK_VALUE_NET;
	}
	public void setR72_BOOK_VALUE_NET(BigDecimal r72_BOOK_VALUE_NET) {
		R72_BOOK_VALUE_NET = r72_BOOK_VALUE_NET;
	}
	public BigDecimal getR72_RW() {
		return R72_RW;
	}
	public void setR72_RW(BigDecimal r72_RW) {
		R72_RW = r72_RW;
	}
	public BigDecimal getR72_RISK_VALUE() {
		return R72_RISK_VALUE;
	}
	public void setR72_RISK_VALUE(BigDecimal r72_RISK_VALUE) {
		R72_RISK_VALUE = r72_RISK_VALUE;
	}
	public BigDecimal getR73_BOOK_VALUE() {
		return R73_BOOK_VALUE;
	}
	public void setR73_BOOK_VALUE(BigDecimal r73_BOOK_VALUE) {
		R73_BOOK_VALUE = r73_BOOK_VALUE;
	}
	public BigDecimal getR73_MARGINS() {
		return R73_MARGINS;
	}
	public void setR73_MARGINS(BigDecimal r73_MARGINS) {
		R73_MARGINS = r73_MARGINS;
	}
	public BigDecimal getR73_BOOK_VALUE_NET() {
		return R73_BOOK_VALUE_NET;
	}
	public void setR73_BOOK_VALUE_NET(BigDecimal r73_BOOK_VALUE_NET) {
		R73_BOOK_VALUE_NET = r73_BOOK_VALUE_NET;
	}
	public BigDecimal getR73_RW() {
		return R73_RW;
	}
	public void setR73_RW(BigDecimal r73_RW) {
		R73_RW = r73_RW;
	}
	public BigDecimal getR73_RISK_VALUE() {
		return R73_RISK_VALUE;
	}
	public void setR73_RISK_VALUE(BigDecimal r73_RISK_VALUE) {
		R73_RISK_VALUE = r73_RISK_VALUE;
	}
	public BigDecimal getR74_BOOK_VALUE() {
		return R74_BOOK_VALUE;
	}
	public void setR74_BOOK_VALUE(BigDecimal r74_BOOK_VALUE) {
		R74_BOOK_VALUE = r74_BOOK_VALUE;
	}
	public BigDecimal getR74_MARGINS() {
		return R74_MARGINS;
	}
	public void setR74_MARGINS(BigDecimal r74_MARGINS) {
		R74_MARGINS = r74_MARGINS;
	}
	public BigDecimal getR74_BOOK_VALUE_NET() {
		return R74_BOOK_VALUE_NET;
	}
	public void setR74_BOOK_VALUE_NET(BigDecimal r74_BOOK_VALUE_NET) {
		R74_BOOK_VALUE_NET = r74_BOOK_VALUE_NET;
	}
	public BigDecimal getR74_RW() {
		return R74_RW;
	}
	public void setR74_RW(BigDecimal r74_RW) {
		R74_RW = r74_RW;
	}
	public BigDecimal getR74_RISK_VALUE() {
		return R74_RISK_VALUE;
	}
	public void setR74_RISK_VALUE(BigDecimal r74_RISK_VALUE) {
		R74_RISK_VALUE = r74_RISK_VALUE;
	}
	public BigDecimal getR75_BOOK_VALUE() {
		return R75_BOOK_VALUE;
	}
	public void setR75_BOOK_VALUE(BigDecimal r75_BOOK_VALUE) {
		R75_BOOK_VALUE = r75_BOOK_VALUE;
	}
	public BigDecimal getR75_MARGINS() {
		return R75_MARGINS;
	}
	public void setR75_MARGINS(BigDecimal r75_MARGINS) {
		R75_MARGINS = r75_MARGINS;
	}
	public BigDecimal getR75_BOOK_VALUE_NET() {
		return R75_BOOK_VALUE_NET;
	}
	public void setR75_BOOK_VALUE_NET(BigDecimal r75_BOOK_VALUE_NET) {
		R75_BOOK_VALUE_NET = r75_BOOK_VALUE_NET;
	}
	public BigDecimal getR75_RW() {
		return R75_RW;
	}
	public void setR75_RW(BigDecimal r75_RW) {
		R75_RW = r75_RW;
	}
	public BigDecimal getR75_RISK_VALUE() {
		return R75_RISK_VALUE;
	}
	public void setR75_RISK_VALUE(BigDecimal r75_RISK_VALUE) {
		R75_RISK_VALUE = r75_RISK_VALUE;
	}
	public BigDecimal getR76_BOOK_VALUE() {
		return R76_BOOK_VALUE;
	}
	public void setR76_BOOK_VALUE(BigDecimal r76_BOOK_VALUE) {
		R76_BOOK_VALUE = r76_BOOK_VALUE;
	}
	public BigDecimal getR76_MARGINS() {
		return R76_MARGINS;
	}
	public void setR76_MARGINS(BigDecimal r76_MARGINS) {
		R76_MARGINS = r76_MARGINS;
	}
	public BigDecimal getR76_BOOK_VALUE_NET() {
		return R76_BOOK_VALUE_NET;
	}
	public void setR76_BOOK_VALUE_NET(BigDecimal r76_BOOK_VALUE_NET) {
		R76_BOOK_VALUE_NET = r76_BOOK_VALUE_NET;
	}
	public BigDecimal getR76_RW() {
		return R76_RW;
	}
	public void setR76_RW(BigDecimal r76_RW) {
		R76_RW = r76_RW;
	}
	public BigDecimal getR76_RISK_VALUE() {
		return R76_RISK_VALUE;
	}
	public void setR76_RISK_VALUE(BigDecimal r76_RISK_VALUE) {
		R76_RISK_VALUE = r76_RISK_VALUE;
	}
	public BigDecimal getR77_BOOK_VALUE() {
		return R77_BOOK_VALUE;
	}
	public void setR77_BOOK_VALUE(BigDecimal r77_BOOK_VALUE) {
		R77_BOOK_VALUE = r77_BOOK_VALUE;
	}
	public BigDecimal getR77_MARGINS() {
		return R77_MARGINS;
	}
	public void setR77_MARGINS(BigDecimal r77_MARGINS) {
		R77_MARGINS = r77_MARGINS;
	}
	public BigDecimal getR77_BOOK_VALUE_NET() {
		return R77_BOOK_VALUE_NET;
	}
	public void setR77_BOOK_VALUE_NET(BigDecimal r77_BOOK_VALUE_NET) {
		R77_BOOK_VALUE_NET = r77_BOOK_VALUE_NET;
	}
	public BigDecimal getR77_RW() {
		return R77_RW;
	}
	public void setR77_RW(BigDecimal r77_RW) {
		R77_RW = r77_RW;
	}
	public BigDecimal getR77_RISK_VALUE() {
		return R77_RISK_VALUE;
	}
	public void setR77_RISK_VALUE(BigDecimal r77_RISK_VALUE) {
		R77_RISK_VALUE = r77_RISK_VALUE;
	}
	public BigDecimal getR78_BOOK_VALUE() {
		return R78_BOOK_VALUE;
	}
	public void setR78_BOOK_VALUE(BigDecimal r78_BOOK_VALUE) {
		R78_BOOK_VALUE = r78_BOOK_VALUE;
	}
	public BigDecimal getR78_MARGINS() {
		return R78_MARGINS;
	}
	public void setR78_MARGINS(BigDecimal r78_MARGINS) {
		R78_MARGINS = r78_MARGINS;
	}
	public BigDecimal getR78_BOOK_VALUE_NET() {
		return R78_BOOK_VALUE_NET;
	}
	public void setR78_BOOK_VALUE_NET(BigDecimal r78_BOOK_VALUE_NET) {
		R78_BOOK_VALUE_NET = r78_BOOK_VALUE_NET;
	}
	public BigDecimal getR78_RW() {
		return R78_RW;
	}
	public void setR78_RW(BigDecimal r78_RW) {
		R78_RW = r78_RW;
	}
	public BigDecimal getR78_RISK_VALUE() {
		return R78_RISK_VALUE;
	}
	public void setR78_RISK_VALUE(BigDecimal r78_RISK_VALUE) {
		R78_RISK_VALUE = r78_RISK_VALUE;
	}
	public BigDecimal getR79_BOOK_VALUE() {
		return R79_BOOK_VALUE;
	}
	public void setR79_BOOK_VALUE(BigDecimal r79_BOOK_VALUE) {
		R79_BOOK_VALUE = r79_BOOK_VALUE;
	}
	public BigDecimal getR79_MARGINS() {
		return R79_MARGINS;
	}
	public void setR79_MARGINS(BigDecimal r79_MARGINS) {
		R79_MARGINS = r79_MARGINS;
	}
	public BigDecimal getR79_BOOK_VALUE_NET() {
		return R79_BOOK_VALUE_NET;
	}
	public void setR79_BOOK_VALUE_NET(BigDecimal r79_BOOK_VALUE_NET) {
		R79_BOOK_VALUE_NET = r79_BOOK_VALUE_NET;
	}
	public BigDecimal getR79_RW() {
		return R79_RW;
	}
	public void setR79_RW(BigDecimal r79_RW) {
		R79_RW = r79_RW;
	}
	public BigDecimal getR79_RISK_VALUE() {
		return R79_RISK_VALUE;
	}
	public void setR79_RISK_VALUE(BigDecimal r79_RISK_VALUE) {
		R79_RISK_VALUE = r79_RISK_VALUE;
	}
	public BigDecimal getR80_BOOK_VALUE() {
		return R80_BOOK_VALUE;
	}
	public void setR80_BOOK_VALUE(BigDecimal r80_BOOK_VALUE) {
		R80_BOOK_VALUE = r80_BOOK_VALUE;
	}
	public BigDecimal getR80_MARGINS() {
		return R80_MARGINS;
	}
	public void setR80_MARGINS(BigDecimal r80_MARGINS) {
		R80_MARGINS = r80_MARGINS;
	}
	public BigDecimal getR80_BOOK_VALUE_NET() {
		return R80_BOOK_VALUE_NET;
	}
	public void setR80_BOOK_VALUE_NET(BigDecimal r80_BOOK_VALUE_NET) {
		R80_BOOK_VALUE_NET = r80_BOOK_VALUE_NET;
	}
	public BigDecimal getR80_RW() {
		return R80_RW;
	}
	public void setR80_RW(BigDecimal r80_RW) {
		R80_RW = r80_RW;
	}
	public BigDecimal getR80_RISK_VALUE() {
		return R80_RISK_VALUE;
	}
	public void setR80_RISK_VALUE(BigDecimal r80_RISK_VALUE) {
		R80_RISK_VALUE = r80_RISK_VALUE;
	}
	public BigDecimal getR81_BOOK_VALUE() {
		return R81_BOOK_VALUE;
	}
	public void setR81_BOOK_VALUE(BigDecimal r81_BOOK_VALUE) {
		R81_BOOK_VALUE = r81_BOOK_VALUE;
	}
	public BigDecimal getR81_MARGINS() {
		return R81_MARGINS;
	}
	public void setR81_MARGINS(BigDecimal r81_MARGINS) {
		R81_MARGINS = r81_MARGINS;
	}
	public BigDecimal getR81_BOOK_VALUE_NET() {
		return R81_BOOK_VALUE_NET;
	}
	public void setR81_BOOK_VALUE_NET(BigDecimal r81_BOOK_VALUE_NET) {
		R81_BOOK_VALUE_NET = r81_BOOK_VALUE_NET;
	}
	public BigDecimal getR81_RW() {
		return R81_RW;
	}
	public void setR81_RW(BigDecimal r81_RW) {
		R81_RW = r81_RW;
	}
	public BigDecimal getR81_RISK_VALUE() {
		return R81_RISK_VALUE;
	}
	public void setR81_RISK_VALUE(BigDecimal r81_RISK_VALUE) {
		R81_RISK_VALUE = r81_RISK_VALUE;
	}
	public BigDecimal getR82_BOOK_VALUE() {
		return R82_BOOK_VALUE;
	}
	public void setR82_BOOK_VALUE(BigDecimal r82_BOOK_VALUE) {
		R82_BOOK_VALUE = r82_BOOK_VALUE;
	}
	public BigDecimal getR82_MARGINS() {
		return R82_MARGINS;
	}
	public void setR82_MARGINS(BigDecimal r82_MARGINS) {
		R82_MARGINS = r82_MARGINS;
	}
	public BigDecimal getR82_BOOK_VALUE_NET() {
		return R82_BOOK_VALUE_NET;
	}
	public void setR82_BOOK_VALUE_NET(BigDecimal r82_BOOK_VALUE_NET) {
		R82_BOOK_VALUE_NET = r82_BOOK_VALUE_NET;
	}
	public BigDecimal getR82_RW() {
		return R82_RW;
	}
	public void setR82_RW(BigDecimal r82_RW) {
		R82_RW = r82_RW;
	}
	public BigDecimal getR82_RISK_VALUE() {
		return R82_RISK_VALUE;
	}
	public void setR82_RISK_VALUE(BigDecimal r82_RISK_VALUE) {
		R82_RISK_VALUE = r82_RISK_VALUE;
	}
	public BigDecimal getR97_BOOK_VALUE() {
		return R97_BOOK_VALUE;
	}
	public void setR97_BOOK_VALUE(BigDecimal r97_BOOK_VALUE) {
		R97_BOOK_VALUE = r97_BOOK_VALUE;
	}
	public BigDecimal getR97_MARGINS() {
		return R97_MARGINS;
	}
	public void setR97_MARGINS(BigDecimal r97_MARGINS) {
		R97_MARGINS = r97_MARGINS;
	}
	public BigDecimal getR97_BOOK_VALUE_NET() {
		return R97_BOOK_VALUE_NET;
	}
	public void setR97_BOOK_VALUE_NET(BigDecimal r97_BOOK_VALUE_NET) {
		R97_BOOK_VALUE_NET = r97_BOOK_VALUE_NET;
	}
	public BigDecimal getR97_RW() {
		return R97_RW;
	}
	public void setR97_RW(BigDecimal r97_RW) {
		R97_RW = r97_RW;
	}
	public BigDecimal getR97_RISK_VALUE() {
		return R97_RISK_VALUE;
	}
	public void setR97_RISK_VALUE(BigDecimal r97_RISK_VALUE) {
		R97_RISK_VALUE = r97_RISK_VALUE;
	}
	public BigDecimal getR98_BOOK_VALUE() {
		return R98_BOOK_VALUE;
	}
	public void setR98_BOOK_VALUE(BigDecimal r98_BOOK_VALUE) {
		R98_BOOK_VALUE = r98_BOOK_VALUE;
	}
	public BigDecimal getR98_MARGINS() {
		return R98_MARGINS;
	}
	public void setR98_MARGINS(BigDecimal r98_MARGINS) {
		R98_MARGINS = r98_MARGINS;
	}
	public BigDecimal getR98_BOOK_VALUE_NET() {
		return R98_BOOK_VALUE_NET;
	}
	public void setR98_BOOK_VALUE_NET(BigDecimal r98_BOOK_VALUE_NET) {
		R98_BOOK_VALUE_NET = r98_BOOK_VALUE_NET;
	}
	public BigDecimal getR98_RW() {
		return R98_RW;
	}
	public void setR98_RW(BigDecimal r98_RW) {
		R98_RW = r98_RW;
	}
	public BigDecimal getR98_RISK_VALUE() {
		return R98_RISK_VALUE;
	}
	public void setR98_RISK_VALUE(BigDecimal r98_RISK_VALUE) {
		R98_RISK_VALUE = r98_RISK_VALUE;
	}
	public BigDecimal getR99_BOOK_VALUE() {
		return R99_BOOK_VALUE;
	}
	public void setR99_BOOK_VALUE(BigDecimal r99_BOOK_VALUE) {
		R99_BOOK_VALUE = r99_BOOK_VALUE;
	}
	public BigDecimal getR99_MARGINS() {
		return R99_MARGINS;
	}
	public void setR99_MARGINS(BigDecimal r99_MARGINS) {
		R99_MARGINS = r99_MARGINS;
	}
	public BigDecimal getR99_BOOK_VALUE_NET() {
		return R99_BOOK_VALUE_NET;
	}
	public void setR99_BOOK_VALUE_NET(BigDecimal r99_BOOK_VALUE_NET) {
		R99_BOOK_VALUE_NET = r99_BOOK_VALUE_NET;
	}
	public BigDecimal getR99_RW() {
		return R99_RW;
	}
	public void setR99_RW(BigDecimal r99_RW) {
		R99_RW = r99_RW;
	}
	public BigDecimal getR99_RISK_VALUE() {
		return R99_RISK_VALUE;
	}
	public void setR99_RISK_VALUE(BigDecimal r99_RISK_VALUE) {
		R99_RISK_VALUE = r99_RISK_VALUE;
	}
	public BigDecimal getR100_BOOK_VALUE() {
		return R100_BOOK_VALUE;
	}
	public void setR100_BOOK_VALUE(BigDecimal r100_BOOK_VALUE) {
		R100_BOOK_VALUE = r100_BOOK_VALUE;
	}
	public BigDecimal getR100_MARGINS() {
		return R100_MARGINS;
	}
	public void setR100_MARGINS(BigDecimal r100_MARGINS) {
		R100_MARGINS = r100_MARGINS;
	}
	public BigDecimal getR100_BOOK_VALUE_NET() {
		return R100_BOOK_VALUE_NET;
	}
	public void setR100_BOOK_VALUE_NET(BigDecimal r100_BOOK_VALUE_NET) {
		R100_BOOK_VALUE_NET = r100_BOOK_VALUE_NET;
	}
	public BigDecimal getR100_RW() {
		return R100_RW;
	}
	public void setR100_RW(BigDecimal r100_RW) {
		R100_RW = r100_RW;
	}
	public BigDecimal getR100_RISK_VALUE() {
		return R100_RISK_VALUE;
	}
	public void setR100_RISK_VALUE(BigDecimal r100_RISK_VALUE) {
		R100_RISK_VALUE = r100_RISK_VALUE;
	}
	public BigDecimal getR101_BOOK_VALUE() {
		return R101_BOOK_VALUE;
	}
	public void setR101_BOOK_VALUE(BigDecimal r101_BOOK_VALUE) {
		R101_BOOK_VALUE = r101_BOOK_VALUE;
	}
	public BigDecimal getR101_MARGINS() {
		return R101_MARGINS;
	}
	public void setR101_MARGINS(BigDecimal r101_MARGINS) {
		R101_MARGINS = r101_MARGINS;
	}
	public BigDecimal getR101_BOOK_VALUE_NET() {
		return R101_BOOK_VALUE_NET;
	}
	public void setR101_BOOK_VALUE_NET(BigDecimal r101_BOOK_VALUE_NET) {
		R101_BOOK_VALUE_NET = r101_BOOK_VALUE_NET;
	}
	public BigDecimal getR101_RW() {
		return R101_RW;
	}
	public void setR101_RW(BigDecimal r101_RW) {
		R101_RW = r101_RW;
	}
	public BigDecimal getR101_RISK_VALUE() {
		return R101_RISK_VALUE;
	}
	public void setR101_RISK_VALUE(BigDecimal r101_RISK_VALUE) {
		R101_RISK_VALUE = r101_RISK_VALUE;
	}
	public BigDecimal getR102_BOOK_VALUE() {
		return R102_BOOK_VALUE;
	}
	public void setR102_BOOK_VALUE(BigDecimal r102_BOOK_VALUE) {
		R102_BOOK_VALUE = r102_BOOK_VALUE;
	}
	public BigDecimal getR102_MARGINS() {
		return R102_MARGINS;
	}
	public void setR102_MARGINS(BigDecimal r102_MARGINS) {
		R102_MARGINS = r102_MARGINS;
	}
	public BigDecimal getR102_BOOK_VALUE_NET() {
		return R102_BOOK_VALUE_NET;
	}
	public void setR102_BOOK_VALUE_NET(BigDecimal r102_BOOK_VALUE_NET) {
		R102_BOOK_VALUE_NET = r102_BOOK_VALUE_NET;
	}
	public BigDecimal getR102_RW() {
		return R102_RW;
	}
	public void setR102_RW(BigDecimal r102_RW) {
		R102_RW = r102_RW;
	}
	public BigDecimal getR102_RISK_VALUE() {
		return R102_RISK_VALUE;
	}
	public void setR102_RISK_VALUE(BigDecimal r102_RISK_VALUE) {
		R102_RISK_VALUE = r102_RISK_VALUE;
	}
	public BigDecimal getR103_BOOK_VALUE() {
		return R103_BOOK_VALUE;
	}
	public void setR103_BOOK_VALUE(BigDecimal r103_BOOK_VALUE) {
		R103_BOOK_VALUE = r103_BOOK_VALUE;
	}
	public BigDecimal getR103_MARGINS() {
		return R103_MARGINS;
	}
	public void setR103_MARGINS(BigDecimal r103_MARGINS) {
		R103_MARGINS = r103_MARGINS;
	}
	public BigDecimal getR103_BOOK_VALUE_NET() {
		return R103_BOOK_VALUE_NET;
	}
	public void setR103_BOOK_VALUE_NET(BigDecimal r103_BOOK_VALUE_NET) {
		R103_BOOK_VALUE_NET = r103_BOOK_VALUE_NET;
	}
	public BigDecimal getR103_RW() {
		return R103_RW;
	}
	public void setR103_RW(BigDecimal r103_RW) {
		R103_RW = r103_RW;
	}
	public BigDecimal getR103_RISK_VALUE() {
		return R103_RISK_VALUE;
	}
	public void setR103_RISK_VALUE(BigDecimal r103_RISK_VALUE) {
		R103_RISK_VALUE = r103_RISK_VALUE;
	}
	public BigDecimal getR104_BOOK_VALUE() {
		return R104_BOOK_VALUE;
	}
	public void setR104_BOOK_VALUE(BigDecimal r104_BOOK_VALUE) {
		R104_BOOK_VALUE = r104_BOOK_VALUE;
	}
	public BigDecimal getR104_MARGINS() {
		return R104_MARGINS;
	}
	public void setR104_MARGINS(BigDecimal r104_MARGINS) {
		R104_MARGINS = r104_MARGINS;
	}
	public BigDecimal getR104_BOOK_VALUE_NET() {
		return R104_BOOK_VALUE_NET;
	}
	public void setR104_BOOK_VALUE_NET(BigDecimal r104_BOOK_VALUE_NET) {
		R104_BOOK_VALUE_NET = r104_BOOK_VALUE_NET;
	}
	public BigDecimal getR104_RW() {
		return R104_RW;
	}
	public void setR104_RW(BigDecimal r104_RW) {
		R104_RW = r104_RW;
	}
	public BigDecimal getR104_RISK_VALUE() {
		return R104_RISK_VALUE;
	}
	public void setR104_RISK_VALUE(BigDecimal r104_RISK_VALUE) {
		R104_RISK_VALUE = r104_RISK_VALUE;
	}
	public BigDecimal getR105_BOOK_VALUE() {
		return R105_BOOK_VALUE;
	}
	public void setR105_BOOK_VALUE(BigDecimal r105_BOOK_VALUE) {
		R105_BOOK_VALUE = r105_BOOK_VALUE;
	}
	public BigDecimal getR105_MARGINS() {
		return R105_MARGINS;
	}
	public void setR105_MARGINS(BigDecimal r105_MARGINS) {
		R105_MARGINS = r105_MARGINS;
	}
	public BigDecimal getR105_BOOK_VALUE_NET() {
		return R105_BOOK_VALUE_NET;
	}
	public void setR105_BOOK_VALUE_NET(BigDecimal r105_BOOK_VALUE_NET) {
		R105_BOOK_VALUE_NET = r105_BOOK_VALUE_NET;
	}
	public BigDecimal getR105_RW() {
		return R105_RW;
	}
	public void setR105_RW(BigDecimal r105_RW) {
		R105_RW = r105_RW;
	}
	public BigDecimal getR105_RISK_VALUE() {
		return R105_RISK_VALUE;
	}
	public void setR105_RISK_VALUE(BigDecimal r105_RISK_VALUE) {
		R105_RISK_VALUE = r105_RISK_VALUE;
	}
	public BigDecimal getR106_BOOK_VALUE() {
		return R106_BOOK_VALUE;
	}
	public void setR106_BOOK_VALUE(BigDecimal r106_BOOK_VALUE) {
		R106_BOOK_VALUE = r106_BOOK_VALUE;
	}
	public BigDecimal getR106_MARGINS() {
		return R106_MARGINS;
	}
	public void setR106_MARGINS(BigDecimal r106_MARGINS) {
		R106_MARGINS = r106_MARGINS;
	}
	public BigDecimal getR106_BOOK_VALUE_NET() {
		return R106_BOOK_VALUE_NET;
	}
	public void setR106_BOOK_VALUE_NET(BigDecimal r106_BOOK_VALUE_NET) {
		R106_BOOK_VALUE_NET = r106_BOOK_VALUE_NET;
	}
	public BigDecimal getR106_RW() {
		return R106_RW;
	}
	public void setR106_RW(BigDecimal r106_RW) {
		R106_RW = r106_RW;
	}
	public BigDecimal getR106_RISK_VALUE() {
		return R106_RISK_VALUE;
	}
	public void setR106_RISK_VALUE(BigDecimal r106_RISK_VALUE) {
		R106_RISK_VALUE = r106_RISK_VALUE;
	}
	public BigDecimal getR107_BOOK_VALUE() {
		return R107_BOOK_VALUE;
	}
	public void setR107_BOOK_VALUE(BigDecimal r107_BOOK_VALUE) {
		R107_BOOK_VALUE = r107_BOOK_VALUE;
	}
	public BigDecimal getR107_MARGINS() {
		return R107_MARGINS;
	}
	public void setR107_MARGINS(BigDecimal r107_MARGINS) {
		R107_MARGINS = r107_MARGINS;
	}
	public BigDecimal getR107_BOOK_VALUE_NET() {
		return R107_BOOK_VALUE_NET;
	}
	public void setR107_BOOK_VALUE_NET(BigDecimal r107_BOOK_VALUE_NET) {
		R107_BOOK_VALUE_NET = r107_BOOK_VALUE_NET;
	}
	public BigDecimal getR107_RW() {
		return R107_RW;
	}
	public void setR107_RW(BigDecimal r107_RW) {
		R107_RW = r107_RW;
	}
	public BigDecimal getR107_RISK_VALUE() {
		return R107_RISK_VALUE;
	}
	public void setR107_RISK_VALUE(BigDecimal r107_RISK_VALUE) {
		R107_RISK_VALUE = r107_RISK_VALUE;
	}
	public BigDecimal getR108_BOOK_VALUE() {
		return R108_BOOK_VALUE;
	}
	public void setR108_BOOK_VALUE(BigDecimal r108_BOOK_VALUE) {
		R108_BOOK_VALUE = r108_BOOK_VALUE;
	}
	public BigDecimal getR108_MARGINS() {
		return R108_MARGINS;
	}
	public void setR108_MARGINS(BigDecimal r108_MARGINS) {
		R108_MARGINS = r108_MARGINS;
	}
	public BigDecimal getR108_BOOK_VALUE_NET() {
		return R108_BOOK_VALUE_NET;
	}
	public void setR108_BOOK_VALUE_NET(BigDecimal r108_BOOK_VALUE_NET) {
		R108_BOOK_VALUE_NET = r108_BOOK_VALUE_NET;
	}
	public BigDecimal getR108_RW() {
		return R108_RW;
	}
	public void setR108_RW(BigDecimal r108_RW) {
		R108_RW = r108_RW;
	}
	public BigDecimal getR108_RISK_VALUE() {
		return R108_RISK_VALUE;
	}
	public void setR108_RISK_VALUE(BigDecimal r108_RISK_VALUE) {
		R108_RISK_VALUE = r108_RISK_VALUE;
	}
	public BigDecimal getR109_BOOK_VALUE() {
		return R109_BOOK_VALUE;
	}
	public void setR109_BOOK_VALUE(BigDecimal r109_BOOK_VALUE) {
		R109_BOOK_VALUE = r109_BOOK_VALUE;
	}
	public BigDecimal getR109_MARGINS() {
		return R109_MARGINS;
	}
	public void setR109_MARGINS(BigDecimal r109_MARGINS) {
		R109_MARGINS = r109_MARGINS;
	}
	public BigDecimal getR109_BOOK_VALUE_NET() {
		return R109_BOOK_VALUE_NET;
	}
	public void setR109_BOOK_VALUE_NET(BigDecimal r109_BOOK_VALUE_NET) {
		R109_BOOK_VALUE_NET = r109_BOOK_VALUE_NET;
	}
	public BigDecimal getR109_RW() {
		return R109_RW;
	}
	public void setR109_RW(BigDecimal r109_RW) {
		R109_RW = r109_RW;
	}
	public BigDecimal getR109_RISK_VALUE() {
		return R109_RISK_VALUE;
	}
	public void setR109_RISK_VALUE(BigDecimal r109_RISK_VALUE) {
		R109_RISK_VALUE = r109_RISK_VALUE;
	}
	public BigDecimal getR110_BOOK_VALUE() {
		return R110_BOOK_VALUE;
	}
	public void setR110_BOOK_VALUE(BigDecimal r110_BOOK_VALUE) {
		R110_BOOK_VALUE = r110_BOOK_VALUE;
	}
	public BigDecimal getR110_MARGINS() {
		return R110_MARGINS;
	}
	public void setR110_MARGINS(BigDecimal r110_MARGINS) {
		R110_MARGINS = r110_MARGINS;
	}
	public BigDecimal getR110_BOOK_VALUE_NET() {
		return R110_BOOK_VALUE_NET;
	}
	public void setR110_BOOK_VALUE_NET(BigDecimal r110_BOOK_VALUE_NET) {
		R110_BOOK_VALUE_NET = r110_BOOK_VALUE_NET;
	}
	public BigDecimal getR110_RW() {
		return R110_RW;
	}
	public void setR110_RW(BigDecimal r110_RW) {
		R110_RW = r110_RW;
	}
	public BigDecimal getR110_RISK_VALUE() {
		return R110_RISK_VALUE;
	}
	public void setR110_RISK_VALUE(BigDecimal r110_RISK_VALUE) {
		R110_RISK_VALUE = r110_RISK_VALUE;
	}
	public BigDecimal getR111_BOOK_VALUE() {
		return R111_BOOK_VALUE;
	}
	public void setR111_BOOK_VALUE(BigDecimal r111_BOOK_VALUE) {
		R111_BOOK_VALUE = r111_BOOK_VALUE;
	}
	public BigDecimal getR111_MARGINS() {
		return R111_MARGINS;
	}
	public void setR111_MARGINS(BigDecimal r111_MARGINS) {
		R111_MARGINS = r111_MARGINS;
	}
	public BigDecimal getR111_BOOK_VALUE_NET() {
		return R111_BOOK_VALUE_NET;
	}
	public void setR111_BOOK_VALUE_NET(BigDecimal r111_BOOK_VALUE_NET) {
		R111_BOOK_VALUE_NET = r111_BOOK_VALUE_NET;
	}
	public BigDecimal getR111_RW() {
		return R111_RW;
	}
	public void setR111_RW(BigDecimal r111_RW) {
		R111_RW = r111_RW;
	}
	public BigDecimal getR111_RISK_VALUE() {
		return R111_RISK_VALUE;
	}
	public void setR111_RISK_VALUE(BigDecimal r111_RISK_VALUE) {
		R111_RISK_VALUE = r111_RISK_VALUE;
	}
	public BigDecimal getR112_BOOK_VALUE() {
		return R112_BOOK_VALUE;
	}
	public void setR112_BOOK_VALUE(BigDecimal r112_BOOK_VALUE) {
		R112_BOOK_VALUE = r112_BOOK_VALUE;
	}
	public BigDecimal getR112_MARGINS() {
		return R112_MARGINS;
	}
	public void setR112_MARGINS(BigDecimal r112_MARGINS) {
		R112_MARGINS = r112_MARGINS;
	}
	public BigDecimal getR112_BOOK_VALUE_NET() {
		return R112_BOOK_VALUE_NET;
	}
	public void setR112_BOOK_VALUE_NET(BigDecimal r112_BOOK_VALUE_NET) {
		R112_BOOK_VALUE_NET = r112_BOOK_VALUE_NET;
	}
	public BigDecimal getR112_RW() {
		return R112_RW;
	}
	public void setR112_RW(BigDecimal r112_RW) {
		R112_RW = r112_RW;
	}
	public BigDecimal getR112_RISK_VALUE() {
		return R112_RISK_VALUE;
	}
	public void setR112_RISK_VALUE(BigDecimal r112_RISK_VALUE) {
		R112_RISK_VALUE = r112_RISK_VALUE;
	}
	public BigDecimal getR113_BOOK_VALUE() {
		return R113_BOOK_VALUE;
	}
	public void setR113_BOOK_VALUE(BigDecimal r113_BOOK_VALUE) {
		R113_BOOK_VALUE = r113_BOOK_VALUE;
	}
	public BigDecimal getR113_MARGINS() {
		return R113_MARGINS;
	}
	public void setR113_MARGINS(BigDecimal r113_MARGINS) {
		R113_MARGINS = r113_MARGINS;
	}
	public BigDecimal getR113_BOOK_VALUE_NET() {
		return R113_BOOK_VALUE_NET;
	}
	public void setR113_BOOK_VALUE_NET(BigDecimal r113_BOOK_VALUE_NET) {
		R113_BOOK_VALUE_NET = r113_BOOK_VALUE_NET;
	}
	public BigDecimal getR113_RW() {
		return R113_RW;
	}
	public void setR113_RW(BigDecimal r113_RW) {
		R113_RW = r113_RW;
	}
	public BigDecimal getR113_RISK_VALUE() {
		return R113_RISK_VALUE;
	}
	public void setR113_RISK_VALUE(BigDecimal r113_RISK_VALUE) {
		R113_RISK_VALUE = r113_RISK_VALUE;
	}
	public BigDecimal getR114_BOOK_VALUE() {
		return R114_BOOK_VALUE;
	}
	public void setR114_BOOK_VALUE(BigDecimal r114_BOOK_VALUE) {
		R114_BOOK_VALUE = r114_BOOK_VALUE;
	}
	public BigDecimal getR114_MARGINS() {
		return R114_MARGINS;
	}
	public void setR114_MARGINS(BigDecimal r114_MARGINS) {
		R114_MARGINS = r114_MARGINS;
	}
	public BigDecimal getR114_BOOK_VALUE_NET() {
		return R114_BOOK_VALUE_NET;
	}
	public void setR114_BOOK_VALUE_NET(BigDecimal r114_BOOK_VALUE_NET) {
		R114_BOOK_VALUE_NET = r114_BOOK_VALUE_NET;
	}
	public BigDecimal getR114_RW() {
		return R114_RW;
	}
	public void setR114_RW(BigDecimal r114_RW) {
		R114_RW = r114_RW;
	}
	public BigDecimal getR114_RISK_VALUE() {
		return R114_RISK_VALUE;
	}
	public void setR114_RISK_VALUE(BigDecimal r114_RISK_VALUE) {
		R114_RISK_VALUE = r114_RISK_VALUE;
	}
	public BigDecimal getR115_BOOK_VALUE() {
		return R115_BOOK_VALUE;
	}
	public void setR115_BOOK_VALUE(BigDecimal r115_BOOK_VALUE) {
		R115_BOOK_VALUE = r115_BOOK_VALUE;
	}
	public BigDecimal getR115_MARGINS() {
		return R115_MARGINS;
	}
	public void setR115_MARGINS(BigDecimal r115_MARGINS) {
		R115_MARGINS = r115_MARGINS;
	}
	public BigDecimal getR115_BOOK_VALUE_NET() {
		return R115_BOOK_VALUE_NET;
	}
	public void setR115_BOOK_VALUE_NET(BigDecimal r115_BOOK_VALUE_NET) {
		R115_BOOK_VALUE_NET = r115_BOOK_VALUE_NET;
	}
	public BigDecimal getR115_RW() {
		return R115_RW;
	}
	public void setR115_RW(BigDecimal r115_RW) {
		R115_RW = r115_RW;
	}
	public BigDecimal getR115_RISK_VALUE() {
		return R115_RISK_VALUE;
	}
	public void setR115_RISK_VALUE(BigDecimal r115_RISK_VALUE) {
		R115_RISK_VALUE = r115_RISK_VALUE;
	}
	public BigDecimal getR116_BOOK_VALUE() {
		return R116_BOOK_VALUE;
	}
	public void setR116_BOOK_VALUE(BigDecimal r116_BOOK_VALUE) {
		R116_BOOK_VALUE = r116_BOOK_VALUE;
	}
	public BigDecimal getR116_MARGINS() {
		return R116_MARGINS;
	}
	public void setR116_MARGINS(BigDecimal r116_MARGINS) {
		R116_MARGINS = r116_MARGINS;
	}
	public BigDecimal getR116_BOOK_VALUE_NET() {
		return R116_BOOK_VALUE_NET;
	}
	public void setR116_BOOK_VALUE_NET(BigDecimal r116_BOOK_VALUE_NET) {
		R116_BOOK_VALUE_NET = r116_BOOK_VALUE_NET;
	}
	public BigDecimal getR116_RW() {
		return R116_RW;
	}
	public void setR116_RW(BigDecimal r116_RW) {
		R116_RW = r116_RW;
	}
	public BigDecimal getR116_RISK_VALUE() {
		return R116_RISK_VALUE;
	}
	public void setR116_RISK_VALUE(BigDecimal r116_RISK_VALUE) {
		R116_RISK_VALUE = r116_RISK_VALUE;
	}
	public BigDecimal getR117_BOOK_VALUE() {
		return R117_BOOK_VALUE;
	}
	public void setR117_BOOK_VALUE(BigDecimal r117_BOOK_VALUE) {
		R117_BOOK_VALUE = r117_BOOK_VALUE;
	}
	public BigDecimal getR117_MARGINS() {
		return R117_MARGINS;
	}
	public void setR117_MARGINS(BigDecimal r117_MARGINS) {
		R117_MARGINS = r117_MARGINS;
	}
	public BigDecimal getR117_BOOK_VALUE_NET() {
		return R117_BOOK_VALUE_NET;
	}
	public void setR117_BOOK_VALUE_NET(BigDecimal r117_BOOK_VALUE_NET) {
		R117_BOOK_VALUE_NET = r117_BOOK_VALUE_NET;
	}
	public BigDecimal getR117_RW() {
		return R117_RW;
	}
	public void setR117_RW(BigDecimal r117_RW) {
		R117_RW = r117_RW;
	}
	public BigDecimal getR117_RISK_VALUE() {
		return R117_RISK_VALUE;
	}
	public void setR117_RISK_VALUE(BigDecimal r117_RISK_VALUE) {
		R117_RISK_VALUE = r117_RISK_VALUE;
	}
	public BigDecimal getR118_BOOK_VALUE() {
		return R118_BOOK_VALUE;
	}
	public void setR118_BOOK_VALUE(BigDecimal r118_BOOK_VALUE) {
		R118_BOOK_VALUE = r118_BOOK_VALUE;
	}
	public BigDecimal getR118_MARGINS() {
		return R118_MARGINS;
	}
	public void setR118_MARGINS(BigDecimal r118_MARGINS) {
		R118_MARGINS = r118_MARGINS;
	}
	public BigDecimal getR118_BOOK_VALUE_NET() {
		return R118_BOOK_VALUE_NET;
	}
	public void setR118_BOOK_VALUE_NET(BigDecimal r118_BOOK_VALUE_NET) {
		R118_BOOK_VALUE_NET = r118_BOOK_VALUE_NET;
	}
	public BigDecimal getR118_RW() {
		return R118_RW;
	}
	public void setR118_RW(BigDecimal r118_RW) {
		R118_RW = r118_RW;
	}
	public BigDecimal getR118_RISK_VALUE() {
		return R118_RISK_VALUE;
	}
	public void setR118_RISK_VALUE(BigDecimal r118_RISK_VALUE) {
		R118_RISK_VALUE = r118_RISK_VALUE;
	}
	public BigDecimal getR119_BOOK_VALUE() {
		return R119_BOOK_VALUE;
	}
	public void setR119_BOOK_VALUE(BigDecimal r119_BOOK_VALUE) {
		R119_BOOK_VALUE = r119_BOOK_VALUE;
	}
	public BigDecimal getR119_MARGINS() {
		return R119_MARGINS;
	}
	public void setR119_MARGINS(BigDecimal r119_MARGINS) {
		R119_MARGINS = r119_MARGINS;
	}
	public BigDecimal getR119_BOOK_VALUE_NET() {
		return R119_BOOK_VALUE_NET;
	}
	public void setR119_BOOK_VALUE_NET(BigDecimal r119_BOOK_VALUE_NET) {
		R119_BOOK_VALUE_NET = r119_BOOK_VALUE_NET;
	}
	public BigDecimal getR119_RW() {
		return R119_RW;
	}
	public void setR119_RW(BigDecimal r119_RW) {
		R119_RW = r119_RW;
	}
	public BigDecimal getR119_RISK_VALUE() {
		return R119_RISK_VALUE;
	}
	public void setR119_RISK_VALUE(BigDecimal r119_RISK_VALUE) {
		R119_RISK_VALUE = r119_RISK_VALUE;
	}
	public BigDecimal getR120_BOOK_VALUE() {
		return R120_BOOK_VALUE;
	}
	public void setR120_BOOK_VALUE(BigDecimal r120_BOOK_VALUE) {
		R120_BOOK_VALUE = r120_BOOK_VALUE;
	}
	public BigDecimal getR120_MARGINS() {
		return R120_MARGINS;
	}
	public void setR120_MARGINS(BigDecimal r120_MARGINS) {
		R120_MARGINS = r120_MARGINS;
	}
	public BigDecimal getR120_BOOK_VALUE_NET() {
		return R120_BOOK_VALUE_NET;
	}
	public void setR120_BOOK_VALUE_NET(BigDecimal r120_BOOK_VALUE_NET) {
		R120_BOOK_VALUE_NET = r120_BOOK_VALUE_NET;
	}
	public BigDecimal getR120_RW() {
		return R120_RW;
	}
	public void setR120_RW(BigDecimal r120_RW) {
		R120_RW = r120_RW;
	}
	public BigDecimal getR120_RISK_VALUE() {
		return R120_RISK_VALUE;
	}
	public void setR120_RISK_VALUE(BigDecimal r120_RISK_VALUE) {
		R120_RISK_VALUE = r120_RISK_VALUE;
	}
	public BigDecimal getR121_BOOK_VALUE() {
		return R121_BOOK_VALUE;
	}
	public void setR121_BOOK_VALUE(BigDecimal r121_BOOK_VALUE) {
		R121_BOOK_VALUE = r121_BOOK_VALUE;
	}
	public BigDecimal getR121_MARGINS() {
		return R121_MARGINS;
	}
	public void setR121_MARGINS(BigDecimal r121_MARGINS) {
		R121_MARGINS = r121_MARGINS;
	}
	public BigDecimal getR121_BOOK_VALUE_NET() {
		return R121_BOOK_VALUE_NET;
	}
	public void setR121_BOOK_VALUE_NET(BigDecimal r121_BOOK_VALUE_NET) {
		R121_BOOK_VALUE_NET = r121_BOOK_VALUE_NET;
	}
	public BigDecimal getR121_RW() {
		return R121_RW;
	}
	public void setR121_RW(BigDecimal r121_RW) {
		R121_RW = r121_RW;
	}
	public BigDecimal getR121_RISK_VALUE() {
		return R121_RISK_VALUE;
	}
	public void setR121_RISK_VALUE(BigDecimal r121_RISK_VALUE) {
		R121_RISK_VALUE = r121_RISK_VALUE;
	}
	public BigDecimal getR122_BOOK_VALUE() {
		return R122_BOOK_VALUE;
	}
	public void setR122_BOOK_VALUE(BigDecimal r122_BOOK_VALUE) {
		R122_BOOK_VALUE = r122_BOOK_VALUE;
	}
	public BigDecimal getR122_MARGINS() {
		return R122_MARGINS;
	}
	public void setR122_MARGINS(BigDecimal r122_MARGINS) {
		R122_MARGINS = r122_MARGINS;
	}
	public BigDecimal getR122_BOOK_VALUE_NET() {
		return R122_BOOK_VALUE_NET;
	}
	public void setR122_BOOK_VALUE_NET(BigDecimal r122_BOOK_VALUE_NET) {
		R122_BOOK_VALUE_NET = r122_BOOK_VALUE_NET;
	}
	public BigDecimal getR122_RW() {
		return R122_RW;
	}
	public void setR122_RW(BigDecimal r122_RW) {
		R122_RW = r122_RW;
	}
	public BigDecimal getR122_RISK_VALUE() {
		return R122_RISK_VALUE;
	}
	public void setR122_RISK_VALUE(BigDecimal r122_RISK_VALUE) {
		R122_RISK_VALUE = r122_RISK_VALUE;
	}
	public BigDecimal getR123_BOOK_VALUE() {
		return R123_BOOK_VALUE;
	}
	public void setR123_BOOK_VALUE(BigDecimal r123_BOOK_VALUE) {
		R123_BOOK_VALUE = r123_BOOK_VALUE;
	}
	public BigDecimal getR123_MARGINS() {
		return R123_MARGINS;
	}
	public void setR123_MARGINS(BigDecimal r123_MARGINS) {
		R123_MARGINS = r123_MARGINS;
	}
	public BigDecimal getR123_BOOK_VALUE_NET() {
		return R123_BOOK_VALUE_NET;
	}
	public void setR123_BOOK_VALUE_NET(BigDecimal r123_BOOK_VALUE_NET) {
		R123_BOOK_VALUE_NET = r123_BOOK_VALUE_NET;
	}
	public BigDecimal getR123_RW() {
		return R123_RW;
	}
	public void setR123_RW(BigDecimal r123_RW) {
		R123_RW = r123_RW;
	}
	public BigDecimal getR123_RISK_VALUE() {
		return R123_RISK_VALUE;
	}
	public void setR123_RISK_VALUE(BigDecimal r123_RISK_VALUE) {
		R123_RISK_VALUE = r123_RISK_VALUE;
	}
	public BigDecimal getR124_BOOK_VALUE() {
		return R124_BOOK_VALUE;
	}
	public void setR124_BOOK_VALUE(BigDecimal r124_BOOK_VALUE) {
		R124_BOOK_VALUE = r124_BOOK_VALUE;
	}
	public BigDecimal getR124_MARGINS() {
		return R124_MARGINS;
	}
	public void setR124_MARGINS(BigDecimal r124_MARGINS) {
		R124_MARGINS = r124_MARGINS;
	}
	public BigDecimal getR124_BOOK_VALUE_NET() {
		return R124_BOOK_VALUE_NET;
	}
	public void setR124_BOOK_VALUE_NET(BigDecimal r124_BOOK_VALUE_NET) {
		R124_BOOK_VALUE_NET = r124_BOOK_VALUE_NET;
	}
	public BigDecimal getR124_RW() {
		return R124_RW;
	}
	public void setR124_RW(BigDecimal r124_RW) {
		R124_RW = r124_RW;
	}
	public BigDecimal getR124_RISK_VALUE() {
		return R124_RISK_VALUE;
	}
	public void setR124_RISK_VALUE(BigDecimal r124_RISK_VALUE) {
		R124_RISK_VALUE = r124_RISK_VALUE;
	}
	public BigDecimal getR125_BOOK_VALUE() {
		return R125_BOOK_VALUE;
	}
	public void setR125_BOOK_VALUE(BigDecimal r125_BOOK_VALUE) {
		R125_BOOK_VALUE = r125_BOOK_VALUE;
	}
	public BigDecimal getR125_MARGINS() {
		return R125_MARGINS;
	}
	public void setR125_MARGINS(BigDecimal r125_MARGINS) {
		R125_MARGINS = r125_MARGINS;
	}
	public BigDecimal getR125_BOOK_VALUE_NET() {
		return R125_BOOK_VALUE_NET;
	}
	public void setR125_BOOK_VALUE_NET(BigDecimal r125_BOOK_VALUE_NET) {
		R125_BOOK_VALUE_NET = r125_BOOK_VALUE_NET;
	}
	public BigDecimal getR125_RW() {
		return R125_RW;
	}
	public void setR125_RW(BigDecimal r125_RW) {
		R125_RW = r125_RW;
	}
	public BigDecimal getR125_RISK_VALUE() {
		return R125_RISK_VALUE;
	}
	public void setR125_RISK_VALUE(BigDecimal r125_RISK_VALUE) {
		R125_RISK_VALUE = r125_RISK_VALUE;
	}
	public BigDecimal getR126_BOOK_VALUE() {
		return R126_BOOK_VALUE;
	}
	public void setR126_BOOK_VALUE(BigDecimal r126_BOOK_VALUE) {
		R126_BOOK_VALUE = r126_BOOK_VALUE;
	}
	public BigDecimal getR126_MARGINS() {
		return R126_MARGINS;
	}
	public void setR126_MARGINS(BigDecimal r126_MARGINS) {
		R126_MARGINS = r126_MARGINS;
	}
	public BigDecimal getR126_BOOK_VALUE_NET() {
		return R126_BOOK_VALUE_NET;
	}
	public void setR126_BOOK_VALUE_NET(BigDecimal r126_BOOK_VALUE_NET) {
		R126_BOOK_VALUE_NET = r126_BOOK_VALUE_NET;
	}
	public BigDecimal getR126_RW() {
		return R126_RW;
	}
	public void setR126_RW(BigDecimal r126_RW) {
		R126_RW = r126_RW;
	}
	public BigDecimal getR126_RISK_VALUE() {
		return R126_RISK_VALUE;
	}
	public void setR126_RISK_VALUE(BigDecimal r126_RISK_VALUE) {
		R126_RISK_VALUE = r126_RISK_VALUE;
	}
	public BigDecimal getR127_BOOK_VALUE() {
		return R127_BOOK_VALUE;
	}
	public void setR127_BOOK_VALUE(BigDecimal r127_BOOK_VALUE) {
		R127_BOOK_VALUE = r127_BOOK_VALUE;
	}
	public BigDecimal getR127_MARGINS() {
		return R127_MARGINS;
	}
	public void setR127_MARGINS(BigDecimal r127_MARGINS) {
		R127_MARGINS = r127_MARGINS;
	}
	public BigDecimal getR127_BOOK_VALUE_NET() {
		return R127_BOOK_VALUE_NET;
	}
	public void setR127_BOOK_VALUE_NET(BigDecimal r127_BOOK_VALUE_NET) {
		R127_BOOK_VALUE_NET = r127_BOOK_VALUE_NET;
	}
	public BigDecimal getR127_RW() {
		return R127_RW;
	}
	public void setR127_RW(BigDecimal r127_RW) {
		R127_RW = r127_RW;
	}
	public BigDecimal getR127_RISK_VALUE() {
		return R127_RISK_VALUE;
	}
	public void setR127_RISK_VALUE(BigDecimal r127_RISK_VALUE) {
		R127_RISK_VALUE = r127_RISK_VALUE;
	}
	public BigDecimal getR128_BOOK_VALUE() {
		return R128_BOOK_VALUE;
	}
	public void setR128_BOOK_VALUE(BigDecimal r128_BOOK_VALUE) {
		R128_BOOK_VALUE = r128_BOOK_VALUE;
	}
	public BigDecimal getR128_MARGINS() {
		return R128_MARGINS;
	}
	public void setR128_MARGINS(BigDecimal r128_MARGINS) {
		R128_MARGINS = r128_MARGINS;
	}
	public BigDecimal getR128_BOOK_VALUE_NET() {
		return R128_BOOK_VALUE_NET;
	}
	public void setR128_BOOK_VALUE_NET(BigDecimal r128_BOOK_VALUE_NET) {
		R128_BOOK_VALUE_NET = r128_BOOK_VALUE_NET;
	}
	public BigDecimal getR128_RW() {
		return R128_RW;
	}
	public void setR128_RW(BigDecimal r128_RW) {
		R128_RW = r128_RW;
	}
	public BigDecimal getR128_RISK_VALUE() {
		return R128_RISK_VALUE;
	}
	public void setR128_RISK_VALUE(BigDecimal r128_RISK_VALUE) {
		R128_RISK_VALUE = r128_RISK_VALUE;
	}
	public BigDecimal getR129_BOOK_VALUE() {
		return R129_BOOK_VALUE;
	}
	public void setR129_BOOK_VALUE(BigDecimal r129_BOOK_VALUE) {
		R129_BOOK_VALUE = r129_BOOK_VALUE;
	}
	public BigDecimal getR129_MARGINS() {
		return R129_MARGINS;
	}
	public void setR129_MARGINS(BigDecimal r129_MARGINS) {
		R129_MARGINS = r129_MARGINS;
	}
	public BigDecimal getR129_BOOK_VALUE_NET() {
		return R129_BOOK_VALUE_NET;
	}
	public void setR129_BOOK_VALUE_NET(BigDecimal r129_BOOK_VALUE_NET) {
		R129_BOOK_VALUE_NET = r129_BOOK_VALUE_NET;
	}
	public BigDecimal getR129_RW() {
		return R129_RW;
	}
	public void setR129_RW(BigDecimal r129_RW) {
		R129_RW = r129_RW;
	}
	public BigDecimal getR129_RISK_VALUE() {
		return R129_RISK_VALUE;
	}
	public void setR129_RISK_VALUE(BigDecimal r129_RISK_VALUE) {
		R129_RISK_VALUE = r129_RISK_VALUE;
	}
	public BigDecimal getR130_BOOK_VALUE() {
		return R130_BOOK_VALUE;
	}
	public void setR130_BOOK_VALUE(BigDecimal r130_BOOK_VALUE) {
		R130_BOOK_VALUE = r130_BOOK_VALUE;
	}
	public BigDecimal getR130_MARGINS() {
		return R130_MARGINS;
	}
	public void setR130_MARGINS(BigDecimal r130_MARGINS) {
		R130_MARGINS = r130_MARGINS;
	}
	public BigDecimal getR130_BOOK_VALUE_NET() {
		return R130_BOOK_VALUE_NET;
	}
	public void setR130_BOOK_VALUE_NET(BigDecimal r130_BOOK_VALUE_NET) {
		R130_BOOK_VALUE_NET = r130_BOOK_VALUE_NET;
	}
	public BigDecimal getR130_RW() {
		return R130_RW;
	}
	public void setR130_RW(BigDecimal r130_RW) {
		R130_RW = r130_RW;
	}
	public BigDecimal getR130_RISK_VALUE() {
		return R130_RISK_VALUE;
	}
	public void setR130_RISK_VALUE(BigDecimal r130_RISK_VALUE) {
		R130_RISK_VALUE = r130_RISK_VALUE;
	}
	

}

//  archival summary 


public class RWA_Archival_RowMapper
        implements RowMapper<RWA_Archival_Summary_Entity> {

    @Override
    public RWA_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        RWA_Archival_Summary_Entity obj = new RWA_Archival_Summary_Entity();

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
        // R8 → R20
        // =========================
        obj.setR8_BOOK_VALUE(rs.getBigDecimal("R8_BOOK_VALUE"));
        obj.setR8_MARGINS(rs.getBigDecimal("R8_MARGINS"));
        obj.setR8_BOOK_VALUE_NET(rs.getBigDecimal("R8_BOOK_VALUE_NET"));
        obj.setR8_RW(rs.getBigDecimal("R8_RW"));
        obj.setR8_RISK_VALUE(rs.getBigDecimal("R8_RISK_VALUE"));

        obj.setR9_BOOK_VALUE(rs.getBigDecimal("R9_BOOK_VALUE"));
        obj.setR9_MARGINS(rs.getBigDecimal("R9_MARGINS"));
        obj.setR9_BOOK_VALUE_NET(rs.getBigDecimal("R9_BOOK_VALUE_NET"));
        obj.setR9_RW(rs.getBigDecimal("R9_RW"));
        obj.setR9_RISK_VALUE(rs.getBigDecimal("R9_RISK_VALUE"));

        obj.setR10_BOOK_VALUE(rs.getBigDecimal("R10_BOOK_VALUE"));
        obj.setR10_MARGINS(rs.getBigDecimal("R10_MARGINS"));
        obj.setR10_BOOK_VALUE_NET(rs.getBigDecimal("R10_BOOK_VALUE_NET"));
        obj.setR10_RW(rs.getBigDecimal("R10_RW"));
        obj.setR10_RISK_VALUE(rs.getBigDecimal("R10_RISK_VALUE"));

        obj.setR11_BOOK_VALUE(rs.getBigDecimal("R11_BOOK_VALUE"));
        obj.setR11_MARGINS(rs.getBigDecimal("R11_MARGINS"));
        obj.setR11_BOOK_VALUE_NET(rs.getBigDecimal("R11_BOOK_VALUE_NET"));
        obj.setR11_RW(rs.getBigDecimal("R11_RW"));
        obj.setR11_RISK_VALUE(rs.getBigDecimal("R11_RISK_VALUE"));

        obj.setR12_BOOK_VALUE(rs.getBigDecimal("R12_BOOK_VALUE"));
        obj.setR12_MARGINS(rs.getBigDecimal("R12_MARGINS"));
        obj.setR12_BOOK_VALUE_NET(rs.getBigDecimal("R12_BOOK_VALUE_NET"));
        obj.setR12_RW(rs.getBigDecimal("R12_RW"));
        obj.setR12_RISK_VALUE(rs.getBigDecimal("R12_RISK_VALUE"));

        obj.setR13_BOOK_VALUE(rs.getBigDecimal("R13_BOOK_VALUE"));
        obj.setR13_MARGINS(rs.getBigDecimal("R13_MARGINS"));
        obj.setR13_BOOK_VALUE_NET(rs.getBigDecimal("R13_BOOK_VALUE_NET"));
        obj.setR13_RW(rs.getBigDecimal("R13_RW"));
        obj.setR13_RISK_VALUE(rs.getBigDecimal("R13_RISK_VALUE"));

        obj.setR14_BOOK_VALUE(rs.getBigDecimal("R14_BOOK_VALUE"));
        obj.setR14_MARGINS(rs.getBigDecimal("R14_MARGINS"));
        obj.setR14_BOOK_VALUE_NET(rs.getBigDecimal("R14_BOOK_VALUE_NET"));
        obj.setR14_RW(rs.getBigDecimal("R14_RW"));
        obj.setR14_RISK_VALUE(rs.getBigDecimal("R14_RISK_VALUE"));

        obj.setR15_BOOK_VALUE(rs.getBigDecimal("R15_BOOK_VALUE"));
        obj.setR15_MARGINS(rs.getBigDecimal("R15_MARGINS"));
        obj.setR15_BOOK_VALUE_NET(rs.getBigDecimal("R15_BOOK_VALUE_NET"));
        obj.setR15_RW(rs.getBigDecimal("R15_RW"));
        obj.setR15_RISK_VALUE(rs.getBigDecimal("R15_RISK_VALUE"));

        obj.setR16_BOOK_VALUE(rs.getBigDecimal("R16_BOOK_VALUE"));
        obj.setR16_MARGINS(rs.getBigDecimal("R16_MARGINS"));
        obj.setR16_BOOK_VALUE_NET(rs.getBigDecimal("R16_BOOK_VALUE_NET"));
        obj.setR16_RW(rs.getBigDecimal("R16_RW"));
        obj.setR16_RISK_VALUE(rs.getBigDecimal("R16_RISK_VALUE"));

        obj.setR17_BOOK_VALUE(rs.getBigDecimal("R17_BOOK_VALUE"));
        obj.setR17_MARGINS(rs.getBigDecimal("R17_MARGINS"));
        obj.setR17_BOOK_VALUE_NET(rs.getBigDecimal("R17_BOOK_VALUE_NET"));
        obj.setR17_RW(rs.getBigDecimal("R17_RW"));
        obj.setR17_RISK_VALUE(rs.getBigDecimal("R17_RISK_VALUE"));

        obj.setR18_BOOK_VALUE(rs.getBigDecimal("R18_BOOK_VALUE"));
        obj.setR18_MARGINS(rs.getBigDecimal("R18_MARGINS"));
        obj.setR18_BOOK_VALUE_NET(rs.getBigDecimal("R18_BOOK_VALUE_NET"));
        obj.setR18_RW(rs.getBigDecimal("R18_RW"));
        obj.setR18_RISK_VALUE(rs.getBigDecimal("R18_RISK_VALUE"));

        obj.setR19_BOOK_VALUE(rs.getBigDecimal("R19_BOOK_VALUE"));
        obj.setR19_MARGINS(rs.getBigDecimal("R19_MARGINS"));
        obj.setR19_BOOK_VALUE_NET(rs.getBigDecimal("R19_BOOK_VALUE_NET"));
        obj.setR19_RW(rs.getBigDecimal("R19_RW"));
        obj.setR19_RISK_VALUE(rs.getBigDecimal("R19_RISK_VALUE"));

        obj.setR20_BOOK_VALUE(rs.getBigDecimal("R20_BOOK_VALUE"));
        obj.setR20_MARGINS(rs.getBigDecimal("R20_MARGINS"));
        obj.setR20_BOOK_VALUE_NET(rs.getBigDecimal("R20_BOOK_VALUE_NET"));
        obj.setR20_RW(rs.getBigDecimal("R20_RW"));
        obj.setR20_RISK_VALUE(rs.getBigDecimal("R20_RISK_VALUE"));
		
		
// =========================
// R21 → R30
// =========================
obj.setR21_BOOK_VALUE(rs.getBigDecimal("R21_BOOK_VALUE"));
obj.setR21_MARGINS(rs.getBigDecimal("R21_MARGINS"));
obj.setR21_BOOK_VALUE_NET(rs.getBigDecimal("R21_BOOK_VALUE_NET"));
obj.setR21_RW(rs.getBigDecimal("R21_RW"));
obj.setR21_RISK_VALUE(rs.getBigDecimal("R21_RISK_VALUE"));

obj.setR22_BOOK_VALUE(rs.getBigDecimal("R22_BOOK_VALUE"));
obj.setR22_MARGINS(rs.getBigDecimal("R22_MARGINS"));
obj.setR22_BOOK_VALUE_NET(rs.getBigDecimal("R22_BOOK_VALUE_NET"));
obj.setR22_RW(rs.getBigDecimal("R22_RW"));
obj.setR22_RISK_VALUE(rs.getBigDecimal("R22_RISK_VALUE"));

obj.setR23_BOOK_VALUE(rs.getBigDecimal("R23_BOOK_VALUE"));
obj.setR23_MARGINS(rs.getBigDecimal("R23_MARGINS"));
obj.setR23_BOOK_VALUE_NET(rs.getBigDecimal("R23_BOOK_VALUE_NET"));
obj.setR23_RW(rs.getBigDecimal("R23_RW"));
obj.setR23_RISK_VALUE(rs.getBigDecimal("R23_RISK_VALUE"));

obj.setR24_BOOK_VALUE(rs.getBigDecimal("R24_BOOK_VALUE"));
obj.setR24_MARGINS(rs.getBigDecimal("R24_MARGINS"));
obj.setR24_BOOK_VALUE_NET(rs.getBigDecimal("R24_BOOK_VALUE_NET"));
obj.setR24_RW(rs.getBigDecimal("R24_RW"));
obj.setR24_RISK_VALUE(rs.getBigDecimal("R24_RISK_VALUE"));

obj.setR25_BOOK_VALUE(rs.getBigDecimal("R25_BOOK_VALUE"));
obj.setR25_MARGINS(rs.getBigDecimal("R25_MARGINS"));
obj.setR25_BOOK_VALUE_NET(rs.getBigDecimal("R25_BOOK_VALUE_NET"));
obj.setR25_RW(rs.getBigDecimal("R25_RW"));
obj.setR25_RISK_VALUE(rs.getBigDecimal("R25_RISK_VALUE"));

obj.setR26_BOOK_VALUE(rs.getBigDecimal("R26_BOOK_VALUE"));
obj.setR26_MARGINS(rs.getBigDecimal("R26_MARGINS"));
obj.setR26_BOOK_VALUE_NET(rs.getBigDecimal("R26_BOOK_VALUE_NET"));
obj.setR26_RW(rs.getBigDecimal("R26_RW"));
obj.setR26_RISK_VALUE(rs.getBigDecimal("R26_RISK_VALUE"));

obj.setR27_BOOK_VALUE(rs.getBigDecimal("R27_BOOK_VALUE"));
obj.setR27_MARGINS(rs.getBigDecimal("R27_MARGINS"));
obj.setR27_BOOK_VALUE_NET(rs.getBigDecimal("R27_BOOK_VALUE_NET"));
obj.setR27_RW(rs.getBigDecimal("R27_RW"));
obj.setR27_RISK_VALUE(rs.getBigDecimal("R27_RISK_VALUE"));

obj.setR28_BOOK_VALUE(rs.getBigDecimal("R28_BOOK_VALUE"));
obj.setR28_MARGINS(rs.getBigDecimal("R28_MARGINS"));
obj.setR28_BOOK_VALUE_NET(rs.getBigDecimal("R28_BOOK_VALUE_NET"));
obj.setR28_RW(rs.getBigDecimal("R28_RW"));
obj.setR28_RISK_VALUE(rs.getBigDecimal("R28_RISK_VALUE"));

obj.setR29_BOOK_VALUE(rs.getBigDecimal("R29_BOOK_VALUE"));
obj.setR29_MARGINS(rs.getBigDecimal("R29_MARGINS"));
obj.setR29_BOOK_VALUE_NET(rs.getBigDecimal("R29_BOOK_VALUE_NET"));
obj.setR29_RW(rs.getBigDecimal("R29_RW"));
obj.setR29_RISK_VALUE(rs.getBigDecimal("R29_RISK_VALUE"));

obj.setR30_BOOK_VALUE(rs.getBigDecimal("R30_BOOK_VALUE"));
obj.setR30_MARGINS(rs.getBigDecimal("R30_MARGINS"));
obj.setR30_BOOK_VALUE_NET(rs.getBigDecimal("R30_BOOK_VALUE_NET"));
obj.setR30_RW(rs.getBigDecimal("R30_RW"));
obj.setR30_RISK_VALUE(rs.getBigDecimal("R30_RISK_VALUE"));


// =========================
// R31 → R40
// =========================
obj.setR31_BOOK_VALUE(rs.getBigDecimal("R31_BOOK_VALUE"));
obj.setR31_MARGINS(rs.getBigDecimal("R31_MARGINS"));
obj.setR31_BOOK_VALUE_NET(rs.getBigDecimal("R31_BOOK_VALUE_NET"));
obj.setR31_RW(rs.getBigDecimal("R31_RW"));
obj.setR31_RISK_VALUE(rs.getBigDecimal("R31_RISK_VALUE"));

obj.setR32_BOOK_VALUE(rs.getBigDecimal("R32_BOOK_VALUE"));
obj.setR32_MARGINS(rs.getBigDecimal("R32_MARGINS"));
obj.setR32_BOOK_VALUE_NET(rs.getBigDecimal("R32_BOOK_VALUE_NET"));
obj.setR32_RW(rs.getBigDecimal("R32_RW"));
obj.setR32_RISK_VALUE(rs.getBigDecimal("R32_RISK_VALUE"));

obj.setR33_BOOK_VALUE(rs.getBigDecimal("R33_BOOK_VALUE"));
obj.setR33_MARGINS(rs.getBigDecimal("R33_MARGINS"));
obj.setR33_BOOK_VALUE_NET(rs.getBigDecimal("R33_BOOK_VALUE_NET"));
obj.setR33_RW(rs.getBigDecimal("R33_RW"));
obj.setR33_RISK_VALUE(rs.getBigDecimal("R33_RISK_VALUE"));

obj.setR34_BOOK_VALUE(rs.getBigDecimal("R34_BOOK_VALUE"));
obj.setR34_MARGINS(rs.getBigDecimal("R34_MARGINS"));
obj.setR34_BOOK_VALUE_NET(rs.getBigDecimal("R34_BOOK_VALUE_NET"));
obj.setR34_RW(rs.getBigDecimal("R34_RW"));
obj.setR34_RISK_VALUE(rs.getBigDecimal("R34_RISK_VALUE"));

obj.setR35_BOOK_VALUE(rs.getBigDecimal("R35_BOOK_VALUE"));
obj.setR35_MARGINS(rs.getBigDecimal("R35_MARGINS"));
obj.setR35_BOOK_VALUE_NET(rs.getBigDecimal("R35_BOOK_VALUE_NET"));
obj.setR35_RW(rs.getBigDecimal("R35_RW"));
obj.setR35_RISK_VALUE(rs.getBigDecimal("R35_RISK_VALUE"));

obj.setR36_BOOK_VALUE(rs.getBigDecimal("R36_BOOK_VALUE"));
obj.setR36_MARGINS(rs.getBigDecimal("R36_MARGINS"));
obj.setR36_BOOK_VALUE_NET(rs.getBigDecimal("R36_BOOK_VALUE_NET"));
obj.setR36_RW(rs.getBigDecimal("R36_RW"));
obj.setR36_RISK_VALUE(rs.getBigDecimal("R36_RISK_VALUE"));

obj.setR37_BOOK_VALUE(rs.getBigDecimal("R37_BOOK_VALUE"));
obj.setR37_MARGINS(rs.getBigDecimal("R37_MARGINS"));
obj.setR37_BOOK_VALUE_NET(rs.getBigDecimal("R37_BOOK_VALUE_NET"));
obj.setR37_RW(rs.getBigDecimal("R37_RW"));
obj.setR37_RISK_VALUE(rs.getBigDecimal("R37_RISK_VALUE"));

obj.setR38_BOOK_VALUE(rs.getBigDecimal("R38_BOOK_VALUE"));
obj.setR38_MARGINS(rs.getBigDecimal("R38_MARGINS"));
obj.setR38_BOOK_VALUE_NET(rs.getBigDecimal("R38_BOOK_VALUE_NET"));
obj.setR38_RW(rs.getBigDecimal("R38_RW"));
obj.setR38_RISK_VALUE(rs.getBigDecimal("R38_RISK_VALUE"));

obj.setR39_BOOK_VALUE(rs.getBigDecimal("R39_BOOK_VALUE"));
obj.setR39_MARGINS(rs.getBigDecimal("R39_MARGINS"));
obj.setR39_BOOK_VALUE_NET(rs.getBigDecimal("R39_BOOK_VALUE_NET"));
obj.setR39_RW(rs.getBigDecimal("R39_RW"));
obj.setR39_RISK_VALUE(rs.getBigDecimal("R39_RISK_VALUE"));

obj.setR40_BOOK_VALUE(rs.getBigDecimal("R40_BOOK_VALUE"));
obj.setR40_MARGINS(rs.getBigDecimal("R40_MARGINS"));
obj.setR40_BOOK_VALUE_NET(rs.getBigDecimal("R40_BOOK_VALUE_NET"));
obj.setR40_RW(rs.getBigDecimal("R40_RW"));
obj.setR40_RISK_VALUE(rs.getBigDecimal("R40_RISK_VALUE"));

// =========================
// R41 → R48
// =========================
obj.setR41_BOOK_VALUE(rs.getBigDecimal("R41_BOOK_VALUE"));
obj.setR41_MARGINS(rs.getBigDecimal("R41_MARGINS"));
obj.setR41_BOOK_VALUE_NET(rs.getBigDecimal("R41_BOOK_VALUE_NET"));
obj.setR41_RW(rs.getBigDecimal("R41_RW"));
obj.setR41_RISK_VALUE(rs.getBigDecimal("R41_RISK_VALUE"));

obj.setR42_BOOK_VALUE(rs.getBigDecimal("R42_BOOK_VALUE"));
obj.setR42_MARGINS(rs.getBigDecimal("R42_MARGINS"));
obj.setR42_BOOK_VALUE_NET(rs.getBigDecimal("R42_BOOK_VALUE_NET"));
obj.setR42_RW(rs.getBigDecimal("R42_RW"));
obj.setR42_RISK_VALUE(rs.getBigDecimal("R42_RISK_VALUE"));

obj.setR43_BOOK_VALUE(rs.getBigDecimal("R43_BOOK_VALUE"));
obj.setR43_MARGINS(rs.getBigDecimal("R43_MARGINS"));
obj.setR43_BOOK_VALUE_NET(rs.getBigDecimal("R43_BOOK_VALUE_NET"));
obj.setR43_RW(rs.getBigDecimal("R43_RW"));
obj.setR43_RISK_VALUE(rs.getBigDecimal("R43_RISK_VALUE"));

obj.setR44_BOOK_VALUE(rs.getBigDecimal("R44_BOOK_VALUE"));
obj.setR44_MARGINS(rs.getBigDecimal("R44_MARGINS"));
obj.setR44_BOOK_VALUE_NET(rs.getBigDecimal("R44_BOOK_VALUE_NET"));
obj.setR44_RW(rs.getBigDecimal("R44_RW"));
obj.setR44_RISK_VALUE(rs.getBigDecimal("R44_RISK_VALUE"));

obj.setR45_BOOK_VALUE(rs.getBigDecimal("R45_BOOK_VALUE"));
obj.setR45_MARGINS(rs.getBigDecimal("R45_MARGINS"));
obj.setR45_BOOK_VALUE_NET(rs.getBigDecimal("R45_BOOK_VALUE_NET"));
obj.setR45_RW(rs.getBigDecimal("R45_RW"));
obj.setR45_RISK_VALUE(rs.getBigDecimal("R45_RISK_VALUE"));

obj.setR46_BOOK_VALUE(rs.getBigDecimal("R46_BOOK_VALUE"));
obj.setR46_MARGINS(rs.getBigDecimal("R46_MARGINS"));
obj.setR46_BOOK_VALUE_NET(rs.getBigDecimal("R46_BOOK_VALUE_NET"));
obj.setR46_RW(rs.getBigDecimal("R46_RW"));
obj.setR46_RISK_VALUE(rs.getBigDecimal("R46_RISK_VALUE"));

obj.setR48_BOOK_VALUE(rs.getBigDecimal("R48_BOOK_VALUE"));
obj.setR48_MARGINS(rs.getBigDecimal("R48_MARGINS"));
obj.setR48_BOOK_VALUE_NET(rs.getBigDecimal("R48_BOOK_VALUE_NET"));
obj.setR48_RW(rs.getBigDecimal("R48_RW"));
obj.setR48_RISK_VALUE(rs.getBigDecimal("R48_RISK_VALUE"));


// =========================
// R61, R63 → R70
// =========================
obj.setR61_BOOK_VALUE(rs.getBigDecimal("R61_BOOK_VALUE"));
obj.setR61_MARGINS(rs.getBigDecimal("R61_MARGINS"));
obj.setR61_BOOK_VALUE_NET(rs.getBigDecimal("R61_BOOK_VALUE_NET"));
obj.setR61_RW(rs.getBigDecimal("R61_RW"));
obj.setR61_RISK_VALUE(rs.getBigDecimal("R61_RISK_VALUE"));

obj.setR63_BOOK_VALUE(rs.getBigDecimal("R63_BOOK_VALUE"));
obj.setR63_MARGINS(rs.getBigDecimal("R63_MARGINS"));
obj.setR63_BOOK_VALUE_NET(rs.getBigDecimal("R63_BOOK_VALUE_NET"));
obj.setR63_RW(rs.getBigDecimal("R63_RW"));
obj.setR63_RISK_VALUE(rs.getBigDecimal("R63_RISK_VALUE"));

obj.setR64_BOOK_VALUE(rs.getBigDecimal("R64_BOOK_VALUE"));
obj.setR64_MARGINS(rs.getBigDecimal("R64_MARGINS"));
obj.setR64_BOOK_VALUE_NET(rs.getBigDecimal("R64_BOOK_VALUE_NET"));
obj.setR64_RW(rs.getBigDecimal("R64_RW"));
obj.setR64_RISK_VALUE(rs.getBigDecimal("R64_RISK_VALUE"));

obj.setR65_BOOK_VALUE(rs.getBigDecimal("R65_BOOK_VALUE"));
obj.setR65_MARGINS(rs.getBigDecimal("R65_MARGINS"));
obj.setR65_BOOK_VALUE_NET(rs.getBigDecimal("R65_BOOK_VALUE_NET"));
obj.setR65_RW(rs.getBigDecimal("R65_RW"));
obj.setR65_RISK_VALUE(rs.getBigDecimal("R65_RISK_VALUE"));

obj.setR66_BOOK_VALUE(rs.getBigDecimal("R66_BOOK_VALUE"));
obj.setR66_MARGINS(rs.getBigDecimal("R66_MARGINS"));
obj.setR66_BOOK_VALUE_NET(rs.getBigDecimal("R66_BOOK_VALUE_NET"));
obj.setR66_RW(rs.getBigDecimal("R66_RW"));
obj.setR66_RISK_VALUE(rs.getBigDecimal("R66_RISK_VALUE"));

obj.setR67_BOOK_VALUE(rs.getBigDecimal("R67_BOOK_VALUE"));
obj.setR67_MARGINS(rs.getBigDecimal("R67_MARGINS"));
obj.setR67_BOOK_VALUE_NET(rs.getBigDecimal("R67_BOOK_VALUE_NET"));
obj.setR67_RW(rs.getBigDecimal("R67_RW"));
obj.setR67_RISK_VALUE(rs.getBigDecimal("R67_RISK_VALUE"));

obj.setR68_BOOK_VALUE(rs.getBigDecimal("R68_BOOK_VALUE"));
obj.setR68_MARGINS(rs.getBigDecimal("R68_MARGINS"));
obj.setR68_BOOK_VALUE_NET(rs.getBigDecimal("R68_BOOK_VALUE_NET"));
obj.setR68_RW(rs.getBigDecimal("R68_RW"));
obj.setR68_RISK_VALUE(rs.getBigDecimal("R68_RISK_VALUE"));

obj.setR69_BOOK_VALUE(rs.getBigDecimal("R69_BOOK_VALUE"));
obj.setR69_MARGINS(rs.getBigDecimal("R69_MARGINS"));
obj.setR69_BOOK_VALUE_NET(rs.getBigDecimal("R69_BOOK_VALUE_NET"));
obj.setR69_RW(rs.getBigDecimal("R69_RW"));
obj.setR69_RISK_VALUE(rs.getBigDecimal("R69_RISK_VALUE"));

obj.setR70_BOOK_VALUE(rs.getBigDecimal("R70_BOOK_VALUE"));
obj.setR70_MARGINS(rs.getBigDecimal("R70_MARGINS"));
obj.setR70_BOOK_VALUE_NET(rs.getBigDecimal("R70_BOOK_VALUE_NET"));
obj.setR70_RW(rs.getBigDecimal("R70_RW"));
obj.setR70_RISK_VALUE(rs.getBigDecimal("R70_RISK_VALUE"));


// =========================
// R81, R82, R97 → R110
// =========================
obj.setR81_BOOK_VALUE(rs.getBigDecimal("R81_BOOK_VALUE"));
obj.setR81_MARGINS(rs.getBigDecimal("R81_MARGINS"));
obj.setR81_BOOK_VALUE_NET(rs.getBigDecimal("R81_BOOK_VALUE_NET"));
obj.setR81_RW(rs.getBigDecimal("R81_RW"));
obj.setR81_RISK_VALUE(rs.getBigDecimal("R81_RISK_VALUE"));

obj.setR82_BOOK_VALUE(rs.getBigDecimal("R82_BOOK_VALUE"));
obj.setR82_MARGINS(rs.getBigDecimal("R82_MARGINS"));
obj.setR82_BOOK_VALUE_NET(rs.getBigDecimal("R82_BOOK_VALUE_NET"));
obj.setR82_RW(rs.getBigDecimal("R82_RW"));
obj.setR82_RISK_VALUE(rs.getBigDecimal("R82_RISK_VALUE"));

obj.setR97_BOOK_VALUE(rs.getBigDecimal("R97_BOOK_VALUE"));
obj.setR97_MARGINS(rs.getBigDecimal("R97_MARGINS"));
obj.setR97_BOOK_VALUE_NET(rs.getBigDecimal("R97_BOOK_VALUE_NET"));
obj.setR97_RW(rs.getBigDecimal("R97_RW"));
obj.setR97_RISK_VALUE(rs.getBigDecimal("R97_RISK_VALUE"));

obj.setR98_BOOK_VALUE(rs.getBigDecimal("R98_BOOK_VALUE"));
obj.setR98_MARGINS(rs.getBigDecimal("R98_MARGINS"));
obj.setR98_BOOK_VALUE_NET(rs.getBigDecimal("R98_BOOK_VALUE_NET"));
obj.setR98_RW(rs.getBigDecimal("R98_RW"));
obj.setR98_RISK_VALUE(rs.getBigDecimal("R98_RISK_VALUE"));

obj.setR99_BOOK_VALUE(rs.getBigDecimal("R99_BOOK_VALUE"));
obj.setR99_MARGINS(rs.getBigDecimal("R99_MARGINS"));
obj.setR99_BOOK_VALUE_NET(rs.getBigDecimal("R99_BOOK_VALUE_NET"));
obj.setR99_RW(rs.getBigDecimal("R99_RW"));
obj.setR99_RISK_VALUE(rs.getBigDecimal("R99_RISK_VALUE"));

obj.setR100_BOOK_VALUE(rs.getBigDecimal("R100_BOOK_VALUE"));
obj.setR100_MARGINS(rs.getBigDecimal("R100_MARGINS"));
obj.setR100_BOOK_VALUE_NET(rs.getBigDecimal("R100_BOOK_VALUE_NET"));
obj.setR100_RW(rs.getBigDecimal("R100_RW"));
obj.setR100_RISK_VALUE(rs.getBigDecimal("R100_RISK_VALUE"));

obj.setR101_BOOK_VALUE(rs.getBigDecimal("R101_BOOK_VALUE"));
obj.setR101_MARGINS(rs.getBigDecimal("R101_MARGINS"));
obj.setR101_BOOK_VALUE_NET(rs.getBigDecimal("R101_BOOK_VALUE_NET"));
obj.setR101_RW(rs.getBigDecimal("R101_RW"));
obj.setR101_RISK_VALUE(rs.getBigDecimal("R101_RISK_VALUE"));

obj.setR102_BOOK_VALUE(rs.getBigDecimal("R102_BOOK_VALUE"));
obj.setR102_MARGINS(rs.getBigDecimal("R102_MARGINS"));
obj.setR102_BOOK_VALUE_NET(rs.getBigDecimal("R102_BOOK_VALUE_NET"));
obj.setR102_RW(rs.getBigDecimal("R102_RW"));
obj.setR102_RISK_VALUE(rs.getBigDecimal("R102_RISK_VALUE"));

obj.setR103_BOOK_VALUE(rs.getBigDecimal("R103_BOOK_VALUE"));
obj.setR103_MARGINS(rs.getBigDecimal("R103_MARGINS"));
obj.setR103_BOOK_VALUE_NET(rs.getBigDecimal("R103_BOOK_VALUE_NET"));
obj.setR103_RW(rs.getBigDecimal("R103_RW"));
obj.setR103_RISK_VALUE(rs.getBigDecimal("R103_RISK_VALUE"));

obj.setR104_BOOK_VALUE(rs.getBigDecimal("R104_BOOK_VALUE"));
obj.setR104_MARGINS(rs.getBigDecimal("R104_MARGINS"));
obj.setR104_BOOK_VALUE_NET(rs.getBigDecimal("R104_BOOK_VALUE_NET"));
obj.setR104_RW(rs.getBigDecimal("R104_RW"));
obj.setR104_RISK_VALUE(rs.getBigDecimal("R104_RISK_VALUE"));

obj.setR105_BOOK_VALUE(rs.getBigDecimal("R105_BOOK_VALUE"));
obj.setR105_MARGINS(rs.getBigDecimal("R105_MARGINS"));
obj.setR105_BOOK_VALUE_NET(rs.getBigDecimal("R105_BOOK_VALUE_NET"));
obj.setR105_RW(rs.getBigDecimal("R105_RW"));
obj.setR105_RISK_VALUE(rs.getBigDecimal("R105_RISK_VALUE"));

obj.setR106_BOOK_VALUE(rs.getBigDecimal("R106_BOOK_VALUE"));
obj.setR106_MARGINS(rs.getBigDecimal("R106_MARGINS"));
obj.setR106_BOOK_VALUE_NET(rs.getBigDecimal("R106_BOOK_VALUE_NET"));
obj.setR106_RW(rs.getBigDecimal("R106_RW"));
obj.setR106_RISK_VALUE(rs.getBigDecimal("R106_RISK_VALUE"));

obj.setR107_BOOK_VALUE(rs.getBigDecimal("R107_BOOK_VALUE"));
obj.setR107_MARGINS(rs.getBigDecimal("R107_MARGINS"));
obj.setR107_BOOK_VALUE_NET(rs.getBigDecimal("R107_BOOK_VALUE_NET"));
obj.setR107_RW(rs.getBigDecimal("R107_RW"));
obj.setR107_RISK_VALUE(rs.getBigDecimal("R107_RISK_VALUE"));

obj.setR108_BOOK_VALUE(rs.getBigDecimal("R108_BOOK_VALUE"));
obj.setR108_MARGINS(rs.getBigDecimal("R108_MARGINS"));
obj.setR108_BOOK_VALUE_NET(rs.getBigDecimal("R108_BOOK_VALUE_NET"));
obj.setR108_RW(rs.getBigDecimal("R108_RW"));
obj.setR108_RISK_VALUE(rs.getBigDecimal("R108_RISK_VALUE"));

obj.setR109_BOOK_VALUE(rs.getBigDecimal("R109_BOOK_VALUE"));
obj.setR109_MARGINS(rs.getBigDecimal("R109_MARGINS"));
obj.setR109_BOOK_VALUE_NET(rs.getBigDecimal("R109_BOOK_VALUE_NET"));
obj.setR109_RW(rs.getBigDecimal("R109_RW"));
obj.setR109_RISK_VALUE(rs.getBigDecimal("R109_RISK_VALUE"));

obj.setR110_BOOK_VALUE(rs.getBigDecimal("R110_BOOK_VALUE"));
obj.setR110_MARGINS(rs.getBigDecimal("R110_MARGINS"));
obj.setR110_BOOK_VALUE_NET(rs.getBigDecimal("R110_BOOK_VALUE_NET"));
obj.setR110_RW(rs.getBigDecimal("R110_RW"));
obj.setR110_RISK_VALUE(rs.getBigDecimal("R110_RISK_VALUE"));


// =========================
// R111 → R120
// =========================
obj.setR111_BOOK_VALUE(rs.getBigDecimal("R111_BOOK_VALUE"));
obj.setR111_MARGINS(rs.getBigDecimal("R111_MARGINS"));
obj.setR111_BOOK_VALUE_NET(rs.getBigDecimal("R111_BOOK_VALUE_NET"));
obj.setR111_RW(rs.getBigDecimal("R111_RW"));
obj.setR111_RISK_VALUE(rs.getBigDecimal("R111_RISK_VALUE"));

obj.setR112_BOOK_VALUE(rs.getBigDecimal("R112_BOOK_VALUE"));
obj.setR112_MARGINS(rs.getBigDecimal("R112_MARGINS"));
obj.setR112_BOOK_VALUE_NET(rs.getBigDecimal("R112_BOOK_VALUE_NET"));
obj.setR112_RW(rs.getBigDecimal("R112_RW"));
obj.setR112_RISK_VALUE(rs.getBigDecimal("R112_RISK_VALUE"));

obj.setR113_BOOK_VALUE(rs.getBigDecimal("R113_BOOK_VALUE"));
obj.setR113_MARGINS(rs.getBigDecimal("R113_MARGINS"));
obj.setR113_BOOK_VALUE_NET(rs.getBigDecimal("R113_BOOK_VALUE_NET"));
obj.setR113_RW(rs.getBigDecimal("R113_RW"));
obj.setR113_RISK_VALUE(rs.getBigDecimal("R113_RISK_VALUE"));

obj.setR114_BOOK_VALUE(rs.getBigDecimal("R114_BOOK_VALUE"));
obj.setR114_MARGINS(rs.getBigDecimal("R114_MARGINS"));
obj.setR114_BOOK_VALUE_NET(rs.getBigDecimal("R114_BOOK_VALUE_NET"));
obj.setR114_RW(rs.getBigDecimal("R114_RW"));
obj.setR114_RISK_VALUE(rs.getBigDecimal("R114_RISK_VALUE"));

obj.setR115_BOOK_VALUE(rs.getBigDecimal("R115_BOOK_VALUE"));
obj.setR115_MARGINS(rs.getBigDecimal("R115_MARGINS"));
obj.setR115_BOOK_VALUE_NET(rs.getBigDecimal("R115_BOOK_VALUE_NET"));
obj.setR115_RW(rs.getBigDecimal("R115_RW"));
obj.setR115_RISK_VALUE(rs.getBigDecimal("R115_RISK_VALUE"));

obj.setR116_BOOK_VALUE(rs.getBigDecimal("R116_BOOK_VALUE"));
obj.setR116_MARGINS(rs.getBigDecimal("R116_MARGINS"));
obj.setR116_BOOK_VALUE_NET(rs.getBigDecimal("R116_BOOK_VALUE_NET"));
obj.setR116_RW(rs.getBigDecimal("R116_RW"));
obj.setR116_RISK_VALUE(rs.getBigDecimal("R116_RISK_VALUE"));

obj.setR117_BOOK_VALUE(rs.getBigDecimal("R117_BOOK_VALUE"));
obj.setR117_MARGINS(rs.getBigDecimal("R117_MARGINS"));
obj.setR117_BOOK_VALUE_NET(rs.getBigDecimal("R117_BOOK_VALUE_NET"));
obj.setR117_RW(rs.getBigDecimal("R117_RW"));
obj.setR117_RISK_VALUE(rs.getBigDecimal("R117_RISK_VALUE"));

obj.setR118_BOOK_VALUE(rs.getBigDecimal("R118_BOOK_VALUE"));
obj.setR118_MARGINS(rs.getBigDecimal("R118_MARGINS"));
obj.setR118_BOOK_VALUE_NET(rs.getBigDecimal("R118_BOOK_VALUE_NET"));
obj.setR118_RW(rs.getBigDecimal("R118_RW"));
obj.setR118_RISK_VALUE(rs.getBigDecimal("R118_RISK_VALUE"));

obj.setR119_BOOK_VALUE(rs.getBigDecimal("R119_BOOK_VALUE"));
obj.setR119_MARGINS(rs.getBigDecimal("R119_MARGINS"));
obj.setR119_BOOK_VALUE_NET(rs.getBigDecimal("R119_BOOK_VALUE_NET"));
obj.setR119_RW(rs.getBigDecimal("R119_RW"));
obj.setR119_RISK_VALUE(rs.getBigDecimal("R119_RISK_VALUE"));

obj.setR120_BOOK_VALUE(rs.getBigDecimal("R120_BOOK_VALUE"));
obj.setR120_MARGINS(rs.getBigDecimal("R120_MARGINS"));
obj.setR120_BOOK_VALUE_NET(rs.getBigDecimal("R120_BOOK_VALUE_NET"));
obj.setR120_RW(rs.getBigDecimal("R120_RW"));
obj.setR120_RISK_VALUE(rs.getBigDecimal("R120_RISK_VALUE"));


// =========================
// R121 → R130
// =========================
obj.setR121_BOOK_VALUE(rs.getBigDecimal("R121_BOOK_VALUE"));
obj.setR121_MARGINS(rs.getBigDecimal("R121_MARGINS"));
obj.setR121_BOOK_VALUE_NET(rs.getBigDecimal("R121_BOOK_VALUE_NET"));
obj.setR121_RW(rs.getBigDecimal("R121_RW"));
obj.setR121_RISK_VALUE(rs.getBigDecimal("R121_RISK_VALUE"));

obj.setR122_BOOK_VALUE(rs.getBigDecimal("R122_BOOK_VALUE"));
obj.setR122_MARGINS(rs.getBigDecimal("R122_MARGINS"));
obj.setR122_BOOK_VALUE_NET(rs.getBigDecimal("R122_BOOK_VALUE_NET"));
obj.setR122_RW(rs.getBigDecimal("R122_RW"));
obj.setR122_RISK_VALUE(rs.getBigDecimal("R122_RISK_VALUE"));

obj.setR123_BOOK_VALUE(rs.getBigDecimal("R123_BOOK_VALUE"));
obj.setR123_MARGINS(rs.getBigDecimal("R123_MARGINS"));
obj.setR123_BOOK_VALUE_NET(rs.getBigDecimal("R123_BOOK_VALUE_NET"));
obj.setR123_RW(rs.getBigDecimal("R123_RW"));
obj.setR123_RISK_VALUE(rs.getBigDecimal("R123_RISK_VALUE"));

obj.setR124_BOOK_VALUE(rs.getBigDecimal("R124_BOOK_VALUE"));
obj.setR124_MARGINS(rs.getBigDecimal("R124_MARGINS"));
obj.setR124_BOOK_VALUE_NET(rs.getBigDecimal("R124_BOOK_VALUE_NET"));
obj.setR124_RW(rs.getBigDecimal("R124_RW"));
obj.setR124_RISK_VALUE(rs.getBigDecimal("R124_RISK_VALUE"));

obj.setR125_BOOK_VALUE(rs.getBigDecimal("R125_BOOK_VALUE"));
obj.setR125_MARGINS(rs.getBigDecimal("R125_MARGINS"));
obj.setR125_BOOK_VALUE_NET(rs.getBigDecimal("R125_BOOK_VALUE_NET"));
obj.setR125_RW(rs.getBigDecimal("R125_RW"));
obj.setR125_RISK_VALUE(rs.getBigDecimal("R125_RISK_VALUE"));

obj.setR126_BOOK_VALUE(rs.getBigDecimal("R126_BOOK_VALUE"));
obj.setR126_MARGINS(rs.getBigDecimal("R126_MARGINS"));
obj.setR126_BOOK_VALUE_NET(rs.getBigDecimal("R126_BOOK_VALUE_NET"));
obj.setR126_RW(rs.getBigDecimal("R126_RW"));
obj.setR126_RISK_VALUE(rs.getBigDecimal("R126_RISK_VALUE"));

obj.setR127_BOOK_VALUE(rs.getBigDecimal("R127_BOOK_VALUE"));
obj.setR127_MARGINS(rs.getBigDecimal("R127_MARGINS"));
obj.setR127_BOOK_VALUE_NET(rs.getBigDecimal("R127_BOOK_VALUE_NET"));
obj.setR127_RW(rs.getBigDecimal("R127_RW"));
obj.setR127_RISK_VALUE(rs.getBigDecimal("R127_RISK_VALUE"));

obj.setR128_BOOK_VALUE(rs.getBigDecimal("R128_BOOK_VALUE"));
obj.setR128_MARGINS(rs.getBigDecimal("R128_MARGINS"));
obj.setR128_BOOK_VALUE_NET(rs.getBigDecimal("R128_BOOK_VALUE_NET"));
obj.setR128_RW(rs.getBigDecimal("R128_RW"));
obj.setR128_RISK_VALUE(rs.getBigDecimal("R128_RISK_VALUE"));

obj.setR129_BOOK_VALUE(rs.getBigDecimal("R129_BOOK_VALUE"));
obj.setR129_MARGINS(rs.getBigDecimal("R129_MARGINS"));
obj.setR129_BOOK_VALUE_NET(rs.getBigDecimal("R129_BOOK_VALUE_NET"));
obj.setR129_RW(rs.getBigDecimal("R129_RW"));
obj.setR129_RISK_VALUE(rs.getBigDecimal("R129_RISK_VALUE"));

obj.setR130_BOOK_VALUE(rs.getBigDecimal("R130_BOOK_VALUE"));
obj.setR130_MARGINS(rs.getBigDecimal("R130_MARGINS"));
obj.setR130_BOOK_VALUE_NET(rs.getBigDecimal("R130_BOOK_VALUE_NET"));
obj.setR130_RW(rs.getBigDecimal("R130_RW"));
obj.setR130_RISK_VALUE(rs.getBigDecimal("R130_RISK_VALUE"));

       

        return obj;
    }
}


public class RWA_Archival_Summary_Entity {


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

private BigDecimal R8_BOOK_VALUE;
private BigDecimal R8_MARGINS;
private BigDecimal R8_BOOK_VALUE_NET;
private BigDecimal R8_RW;
private BigDecimal R8_RISK_VALUE;

private BigDecimal R9_BOOK_VALUE;
private BigDecimal R9_MARGINS;
private BigDecimal R9_BOOK_VALUE_NET;
private BigDecimal R9_RW;
private BigDecimal R9_RISK_VALUE;

private BigDecimal R10_BOOK_VALUE;
private BigDecimal R10_MARGINS;
private BigDecimal R10_BOOK_VALUE_NET;
private BigDecimal R10_RW;
private BigDecimal R10_RISK_VALUE;

private BigDecimal R11_BOOK_VALUE;
private BigDecimal R11_MARGINS;
private BigDecimal R11_BOOK_VALUE_NET;
private BigDecimal R11_RW;
private BigDecimal R11_RISK_VALUE;

private BigDecimal R12_BOOK_VALUE;
private BigDecimal R12_MARGINS;
private BigDecimal R12_BOOK_VALUE_NET;
private BigDecimal R12_RW;
private BigDecimal R12_RISK_VALUE;

private BigDecimal R13_BOOK_VALUE;
private BigDecimal R13_MARGINS;
private BigDecimal R13_BOOK_VALUE_NET;
private BigDecimal R13_RW;
private BigDecimal R13_RISK_VALUE;

private BigDecimal R14_BOOK_VALUE;
private BigDecimal R14_MARGINS;
private BigDecimal R14_BOOK_VALUE_NET;
private BigDecimal R14_RW;
private BigDecimal R14_RISK_VALUE;

private BigDecimal R15_BOOK_VALUE;
private BigDecimal R15_MARGINS;
private BigDecimal R15_BOOK_VALUE_NET;
private BigDecimal R15_RW;
private BigDecimal R15_RISK_VALUE;

private BigDecimal R16_BOOK_VALUE;
private BigDecimal R16_MARGINS;
private BigDecimal R16_BOOK_VALUE_NET;
private BigDecimal R16_RW;
private BigDecimal R16_RISK_VALUE;

private BigDecimal R17_BOOK_VALUE;
private BigDecimal R17_MARGINS;
private BigDecimal R17_BOOK_VALUE_NET;
private BigDecimal R17_RW;
private BigDecimal R17_RISK_VALUE;

private BigDecimal R18_BOOK_VALUE;
private BigDecimal R18_MARGINS;
private BigDecimal R18_BOOK_VALUE_NET;
private BigDecimal R18_RW;
private BigDecimal R18_RISK_VALUE;

private BigDecimal R19_BOOK_VALUE;
private BigDecimal R19_MARGINS;
private BigDecimal R19_BOOK_VALUE_NET;
private BigDecimal R19_RW;
private BigDecimal R19_RISK_VALUE;

private BigDecimal R20_BOOK_VALUE;
private BigDecimal R20_MARGINS;
private BigDecimal R20_BOOK_VALUE_NET;
private BigDecimal R20_RW;
private BigDecimal R20_RISK_VALUE;

private BigDecimal R21_BOOK_VALUE;
private BigDecimal R21_MARGINS;
private BigDecimal R21_BOOK_VALUE_NET;
private BigDecimal R21_RW;
private BigDecimal R21_RISK_VALUE;

private BigDecimal R22_BOOK_VALUE;
private BigDecimal R22_MARGINS;
private BigDecimal R22_BOOK_VALUE_NET;
private BigDecimal R22_RW;
private BigDecimal R22_RISK_VALUE;

private BigDecimal R23_BOOK_VALUE;
private BigDecimal R23_MARGINS;
private BigDecimal R23_BOOK_VALUE_NET;
private BigDecimal R23_RW;
private BigDecimal R23_RISK_VALUE;

private BigDecimal R24_BOOK_VALUE;
private BigDecimal R24_MARGINS;
private BigDecimal R24_BOOK_VALUE_NET;
private BigDecimal R24_RW;
private BigDecimal R24_RISK_VALUE;

private BigDecimal R25_BOOK_VALUE;
private BigDecimal R25_MARGINS;
private BigDecimal R25_BOOK_VALUE_NET;
private BigDecimal R25_RW;
private BigDecimal R25_RISK_VALUE;

private BigDecimal R26_BOOK_VALUE;
private BigDecimal R26_MARGINS;
private BigDecimal R26_BOOK_VALUE_NET;
private BigDecimal R26_RW;
private BigDecimal R26_RISK_VALUE;

private BigDecimal R27_BOOK_VALUE;
private BigDecimal R27_MARGINS;
private BigDecimal R27_BOOK_VALUE_NET;
private BigDecimal R27_RW;
private BigDecimal R27_RISK_VALUE;

private BigDecimal R28_BOOK_VALUE;
private BigDecimal R28_MARGINS;
private BigDecimal R28_BOOK_VALUE_NET;
private BigDecimal R28_RW;
private BigDecimal R28_RISK_VALUE;

private BigDecimal R29_BOOK_VALUE;
private BigDecimal R29_MARGINS;
private BigDecimal R29_BOOK_VALUE_NET;
private BigDecimal R29_RW;
private BigDecimal R29_RISK_VALUE;

private BigDecimal R30_BOOK_VALUE;
private BigDecimal R30_MARGINS;
private BigDecimal R30_BOOK_VALUE_NET;
private BigDecimal R30_RW;
private BigDecimal R30_RISK_VALUE;

private BigDecimal R31_BOOK_VALUE;
private BigDecimal R31_MARGINS;
private BigDecimal R31_BOOK_VALUE_NET;
private BigDecimal R31_RW;
private BigDecimal R31_RISK_VALUE;

private BigDecimal R32_BOOK_VALUE;
private BigDecimal R32_MARGINS;
private BigDecimal R32_BOOK_VALUE_NET;
private BigDecimal R32_RW;
private BigDecimal R32_RISK_VALUE;

private BigDecimal R33_BOOK_VALUE;
private BigDecimal R33_MARGINS;
private BigDecimal R33_BOOK_VALUE_NET;
private BigDecimal R33_RW;
private BigDecimal R33_RISK_VALUE;

private BigDecimal R34_BOOK_VALUE;
private BigDecimal R34_MARGINS;
private BigDecimal R34_BOOK_VALUE_NET;
private BigDecimal R34_RW;
private BigDecimal R34_RISK_VALUE;

private BigDecimal R35_BOOK_VALUE;
private BigDecimal R35_MARGINS;
private BigDecimal R35_BOOK_VALUE_NET;
private BigDecimal R35_RW;
private BigDecimal R35_RISK_VALUE;

private BigDecimal R36_BOOK_VALUE;
private BigDecimal R36_MARGINS;
private BigDecimal R36_BOOK_VALUE_NET;
private BigDecimal R36_RW;
private BigDecimal R36_RISK_VALUE;

private BigDecimal R37_BOOK_VALUE;
private BigDecimal R37_MARGINS;
private BigDecimal R37_BOOK_VALUE_NET;
private BigDecimal R37_RW;
private BigDecimal R37_RISK_VALUE;

private BigDecimal R38_BOOK_VALUE;
private BigDecimal R38_MARGINS;
private BigDecimal R38_BOOK_VALUE_NET;
private BigDecimal R38_RW;
private BigDecimal R38_RISK_VALUE;

private BigDecimal R39_BOOK_VALUE;
private BigDecimal R39_MARGINS;
private BigDecimal R39_BOOK_VALUE_NET;
private BigDecimal R39_RW;
private BigDecimal R39_RISK_VALUE;

private BigDecimal R40_BOOK_VALUE;
private BigDecimal R40_MARGINS;
private BigDecimal R40_BOOK_VALUE_NET;
private BigDecimal R40_RW;
private BigDecimal R40_RISK_VALUE;

private BigDecimal R41_BOOK_VALUE;
private BigDecimal R41_MARGINS;
private BigDecimal R41_BOOK_VALUE_NET;
private BigDecimal R41_RW;
private BigDecimal R41_RISK_VALUE;

private BigDecimal R42_BOOK_VALUE;
private BigDecimal R42_MARGINS;
private BigDecimal R42_BOOK_VALUE_NET;
private BigDecimal R42_RW;
private BigDecimal R42_RISK_VALUE;

private BigDecimal R43_BOOK_VALUE;
private BigDecimal R43_MARGINS;
private BigDecimal R43_BOOK_VALUE_NET;
private BigDecimal R43_RW;
private BigDecimal R43_RISK_VALUE;

private BigDecimal R44_BOOK_VALUE;
private BigDecimal R44_MARGINS;
private BigDecimal R44_BOOK_VALUE_NET;
private BigDecimal R44_RW;
private BigDecimal R44_RISK_VALUE;

private BigDecimal R45_BOOK_VALUE;
private BigDecimal R45_MARGINS;
private BigDecimal R45_BOOK_VALUE_NET;
private BigDecimal R45_RW;
private BigDecimal R45_RISK_VALUE;

private BigDecimal R46_BOOK_VALUE;
private BigDecimal R46_MARGINS;
private BigDecimal R46_BOOK_VALUE_NET;
private BigDecimal R46_RW;
private BigDecimal R46_RISK_VALUE;

private BigDecimal R48_BOOK_VALUE;
private BigDecimal R48_MARGINS;
private BigDecimal R48_BOOK_VALUE_NET;
private BigDecimal R48_RW;
private BigDecimal R48_RISK_VALUE;

private BigDecimal R61_BOOK_VALUE;
private BigDecimal R61_MARGINS;
private BigDecimal R61_BOOK_VALUE_NET;
private BigDecimal R61_RW;
private BigDecimal R61_RISK_VALUE;

private BigDecimal R63_BOOK_VALUE;
private BigDecimal R63_MARGINS;
private BigDecimal R63_BOOK_VALUE_NET;
private BigDecimal R63_RW;
private BigDecimal R63_RISK_VALUE;

private BigDecimal R64_BOOK_VALUE;
private BigDecimal R64_MARGINS;
private BigDecimal R64_BOOK_VALUE_NET;
private BigDecimal R64_RW;
private BigDecimal R64_RISK_VALUE;

private BigDecimal R65_BOOK_VALUE;
private BigDecimal R65_MARGINS;
private BigDecimal R65_BOOK_VALUE_NET;
private BigDecimal R65_RW;
private BigDecimal R65_RISK_VALUE;

private BigDecimal R66_BOOK_VALUE;
private BigDecimal R66_MARGINS;
private BigDecimal R66_BOOK_VALUE_NET;
private BigDecimal R66_RW;
private BigDecimal R66_RISK_VALUE;

private BigDecimal R67_BOOK_VALUE;
private BigDecimal R67_MARGINS;
private BigDecimal R67_BOOK_VALUE_NET;
private BigDecimal R67_RW;
private BigDecimal R67_RISK_VALUE;

private BigDecimal R68_BOOK_VALUE;
private BigDecimal R68_MARGINS;
private BigDecimal R68_BOOK_VALUE_NET;
private BigDecimal R68_RW;
private BigDecimal R68_RISK_VALUE;

private BigDecimal R69_BOOK_VALUE;
private BigDecimal R69_MARGINS;
private BigDecimal R69_BOOK_VALUE_NET;
private BigDecimal R69_RW;
private BigDecimal R69_RISK_VALUE;

private BigDecimal R70_BOOK_VALUE;
private BigDecimal R70_MARGINS;
private BigDecimal R70_BOOK_VALUE_NET;
private BigDecimal R70_RW;
private BigDecimal R70_RISK_VALUE;

private BigDecimal R71_BOOK_VALUE;
private BigDecimal R71_MARGINS;
private BigDecimal R71_BOOK_VALUE_NET;
private BigDecimal R71_RW;
private BigDecimal R71_RISK_VALUE;

private BigDecimal R72_BOOK_VALUE;
private BigDecimal R72_MARGINS;
private BigDecimal R72_BOOK_VALUE_NET;
private BigDecimal R72_RW;
private BigDecimal R72_RISK_VALUE;

private BigDecimal R73_BOOK_VALUE;
private BigDecimal R73_MARGINS;
private BigDecimal R73_BOOK_VALUE_NET;
private BigDecimal R73_RW;
private BigDecimal R73_RISK_VALUE;

private BigDecimal R74_BOOK_VALUE;
private BigDecimal R74_MARGINS;
private BigDecimal R74_BOOK_VALUE_NET;
private BigDecimal R74_RW;
private BigDecimal R74_RISK_VALUE;

private BigDecimal R75_BOOK_VALUE;
private BigDecimal R75_MARGINS;
private BigDecimal R75_BOOK_VALUE_NET;
private BigDecimal R75_RW;
private BigDecimal R75_RISK_VALUE;

private BigDecimal R76_BOOK_VALUE;
private BigDecimal R76_MARGINS;
private BigDecimal R76_BOOK_VALUE_NET;
private BigDecimal R76_RW;
private BigDecimal R76_RISK_VALUE;

private BigDecimal R77_BOOK_VALUE;
private BigDecimal R77_MARGINS;
private BigDecimal R77_BOOK_VALUE_NET;
private BigDecimal R77_RW;
private BigDecimal R77_RISK_VALUE;

private BigDecimal R78_BOOK_VALUE;
private BigDecimal R78_MARGINS;
private BigDecimal R78_BOOK_VALUE_NET;
private BigDecimal R78_RW;
private BigDecimal R78_RISK_VALUE;

private BigDecimal R79_BOOK_VALUE;
private BigDecimal R79_MARGINS;
private BigDecimal R79_BOOK_VALUE_NET;
private BigDecimal R79_RW;
private BigDecimal R79_RISK_VALUE;

private BigDecimal R80_BOOK_VALUE;
private BigDecimal R80_MARGINS;
private BigDecimal R80_BOOK_VALUE_NET;
private BigDecimal R80_RW;
private BigDecimal R80_RISK_VALUE;

private BigDecimal R81_BOOK_VALUE;
private BigDecimal R81_MARGINS;
private BigDecimal R81_BOOK_VALUE_NET;
private BigDecimal R81_RW;
private BigDecimal R81_RISK_VALUE;

private BigDecimal R82_BOOK_VALUE;
private BigDecimal R82_MARGINS;
private BigDecimal R82_BOOK_VALUE_NET;
private BigDecimal R82_RW;
private BigDecimal R82_RISK_VALUE;

private BigDecimal R97_BOOK_VALUE;
private BigDecimal R97_MARGINS;
private BigDecimal R97_BOOK_VALUE_NET;
private BigDecimal R97_RW;
private BigDecimal R97_RISK_VALUE;

private BigDecimal R98_BOOK_VALUE;
private BigDecimal R98_MARGINS;
private BigDecimal R98_BOOK_VALUE_NET;
private BigDecimal R98_RW;
private BigDecimal R98_RISK_VALUE;

private BigDecimal R99_BOOK_VALUE;
private BigDecimal R99_MARGINS;
private BigDecimal R99_BOOK_VALUE_NET;
private BigDecimal R99_RW;
private BigDecimal R99_RISK_VALUE;

private BigDecimal R100_BOOK_VALUE;
private BigDecimal R100_MARGINS;
private BigDecimal R100_BOOK_VALUE_NET;
private BigDecimal R100_RW;
private BigDecimal R100_RISK_VALUE;

private BigDecimal R101_BOOK_VALUE;
private BigDecimal R101_MARGINS;
private BigDecimal R101_BOOK_VALUE_NET;
private BigDecimal R101_RW;
private BigDecimal R101_RISK_VALUE;

private BigDecimal R102_BOOK_VALUE;
private BigDecimal R102_MARGINS;
private BigDecimal R102_BOOK_VALUE_NET;
private BigDecimal R102_RW;
private BigDecimal R102_RISK_VALUE;

private BigDecimal R103_BOOK_VALUE;
private BigDecimal R103_MARGINS;
private BigDecimal R103_BOOK_VALUE_NET;
private BigDecimal R103_RW;
private BigDecimal R103_RISK_VALUE;

private BigDecimal R104_BOOK_VALUE;
private BigDecimal R104_MARGINS;
private BigDecimal R104_BOOK_VALUE_NET;
private BigDecimal R104_RW;
private BigDecimal R104_RISK_VALUE;

private BigDecimal R105_BOOK_VALUE;
private BigDecimal R105_MARGINS;
private BigDecimal R105_BOOK_VALUE_NET;
private BigDecimal R105_RW;
private BigDecimal R105_RISK_VALUE;

private BigDecimal R106_BOOK_VALUE;
private BigDecimal R106_MARGINS;
private BigDecimal R106_BOOK_VALUE_NET;
private BigDecimal R106_RW;
private BigDecimal R106_RISK_VALUE;

private BigDecimal R107_BOOK_VALUE;
private BigDecimal R107_MARGINS;
private BigDecimal R107_BOOK_VALUE_NET;
private BigDecimal R107_RW;
private BigDecimal R107_RISK_VALUE;

private BigDecimal R108_BOOK_VALUE;
private BigDecimal R108_MARGINS;
private BigDecimal R108_BOOK_VALUE_NET;
private BigDecimal R108_RW;
private BigDecimal R108_RISK_VALUE;

private BigDecimal R109_BOOK_VALUE;
private BigDecimal R109_MARGINS;
private BigDecimal R109_BOOK_VALUE_NET;
private BigDecimal R109_RW;
private BigDecimal R109_RISK_VALUE;

private BigDecimal R110_BOOK_VALUE;
private BigDecimal R110_MARGINS;
private BigDecimal R110_BOOK_VALUE_NET;
private BigDecimal R110_RW;
private BigDecimal R110_RISK_VALUE;

private BigDecimal R111_BOOK_VALUE;
private BigDecimal R111_MARGINS;
private BigDecimal R111_BOOK_VALUE_NET;
private BigDecimal R111_RW;
private BigDecimal R111_RISK_VALUE;

private BigDecimal R112_BOOK_VALUE;
private BigDecimal R112_MARGINS;
private BigDecimal R112_BOOK_VALUE_NET;
private BigDecimal R112_RW;
private BigDecimal R112_RISK_VALUE;

private BigDecimal R113_BOOK_VALUE;
private BigDecimal R113_MARGINS;
private BigDecimal R113_BOOK_VALUE_NET;
private BigDecimal R113_RW;
private BigDecimal R113_RISK_VALUE;

private BigDecimal R114_BOOK_VALUE;
private BigDecimal R114_MARGINS;
private BigDecimal R114_BOOK_VALUE_NET;
private BigDecimal R114_RW;
private BigDecimal R114_RISK_VALUE;

private BigDecimal R115_BOOK_VALUE;
private BigDecimal R115_MARGINS;
private BigDecimal R115_BOOK_VALUE_NET;
private BigDecimal R115_RW;
private BigDecimal R115_RISK_VALUE;

private BigDecimal R116_BOOK_VALUE;
private BigDecimal R116_MARGINS;
private BigDecimal R116_BOOK_VALUE_NET;
private BigDecimal R116_RW;
private BigDecimal R116_RISK_VALUE;

private BigDecimal R117_BOOK_VALUE;
private BigDecimal R117_MARGINS;
private BigDecimal R117_BOOK_VALUE_NET;
private BigDecimal R117_RW;
private BigDecimal R117_RISK_VALUE;

private BigDecimal R118_BOOK_VALUE;
private BigDecimal R118_MARGINS;
private BigDecimal R118_BOOK_VALUE_NET;
private BigDecimal R118_RW;
private BigDecimal R118_RISK_VALUE;

private BigDecimal R119_BOOK_VALUE;
private BigDecimal R119_MARGINS;
private BigDecimal R119_BOOK_VALUE_NET;
private BigDecimal R119_RW;
private BigDecimal R119_RISK_VALUE;

private BigDecimal R120_BOOK_VALUE;
private BigDecimal R120_MARGINS;
private BigDecimal R120_BOOK_VALUE_NET;
private BigDecimal R120_RW;
private BigDecimal R120_RISK_VALUE;

private BigDecimal R121_BOOK_VALUE;
private BigDecimal R121_MARGINS;
private BigDecimal R121_BOOK_VALUE_NET;
private BigDecimal R121_RW;
private BigDecimal R121_RISK_VALUE;

private BigDecimal R122_BOOK_VALUE;
private BigDecimal R122_MARGINS;
private BigDecimal R122_BOOK_VALUE_NET;
private BigDecimal R122_RW;
private BigDecimal R122_RISK_VALUE;

private BigDecimal R123_BOOK_VALUE;
private BigDecimal R123_MARGINS;
private BigDecimal R123_BOOK_VALUE_NET;
private BigDecimal R123_RW;
private BigDecimal R123_RISK_VALUE;

private BigDecimal R124_BOOK_VALUE;
private BigDecimal R124_MARGINS;
private BigDecimal R124_BOOK_VALUE_NET;
private BigDecimal R124_RW;
private BigDecimal R124_RISK_VALUE;

private BigDecimal R125_BOOK_VALUE;
private BigDecimal R125_MARGINS;
private BigDecimal R125_BOOK_VALUE_NET;
private BigDecimal R125_RW;
private BigDecimal R125_RISK_VALUE;

private BigDecimal R126_BOOK_VALUE;
private BigDecimal R126_MARGINS;
private BigDecimal R126_BOOK_VALUE_NET;
private BigDecimal R126_RW;
private BigDecimal R126_RISK_VALUE;

private BigDecimal R127_BOOK_VALUE;
private BigDecimal R127_MARGINS;
private BigDecimal R127_BOOK_VALUE_NET;
private BigDecimal R127_RW;
private BigDecimal R127_RISK_VALUE;

private BigDecimal R128_BOOK_VALUE;
private BigDecimal R128_MARGINS;
private BigDecimal R128_BOOK_VALUE_NET;
private BigDecimal R128_RW;
private BigDecimal R128_RISK_VALUE;

private BigDecimal R129_BOOK_VALUE;
private BigDecimal R129_MARGINS;
private BigDecimal R129_BOOK_VALUE_NET;
private BigDecimal R129_RW;
private BigDecimal R129_RISK_VALUE;

private BigDecimal R130_BOOK_VALUE;
private BigDecimal R130_MARGINS;
private BigDecimal R130_BOOK_VALUE_NET;
private BigDecimal R130_RW;
private BigDecimal R130_RISK_VALUE;

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
public BigDecimal getR8_BOOK_VALUE() {
	return R8_BOOK_VALUE;
}
public void setR8_BOOK_VALUE(BigDecimal r8_BOOK_VALUE) {
	R8_BOOK_VALUE = r8_BOOK_VALUE;
}
public BigDecimal getR8_MARGINS() {
	return R8_MARGINS;
}
public void setR8_MARGINS(BigDecimal r8_MARGINS) {
	R8_MARGINS = r8_MARGINS;
}
public BigDecimal getR8_BOOK_VALUE_NET() {
	return R8_BOOK_VALUE_NET;
}
public void setR8_BOOK_VALUE_NET(BigDecimal r8_BOOK_VALUE_NET) {
	R8_BOOK_VALUE_NET = r8_BOOK_VALUE_NET;
}
public BigDecimal getR8_RW() {
	return R8_RW;
}
public void setR8_RW(BigDecimal r8_RW) {
	R8_RW = r8_RW;
}
public BigDecimal getR8_RISK_VALUE() {
	return R8_RISK_VALUE;
}
public void setR8_RISK_VALUE(BigDecimal r8_RISK_VALUE) {
	R8_RISK_VALUE = r8_RISK_VALUE;
}
public BigDecimal getR9_BOOK_VALUE() {
	return R9_BOOK_VALUE;
}
public void setR9_BOOK_VALUE(BigDecimal r9_BOOK_VALUE) {
	R9_BOOK_VALUE = r9_BOOK_VALUE;
}
public BigDecimal getR9_MARGINS() {
	return R9_MARGINS;
}
public void setR9_MARGINS(BigDecimal r9_MARGINS) {
	R9_MARGINS = r9_MARGINS;
}
public BigDecimal getR9_BOOK_VALUE_NET() {
	return R9_BOOK_VALUE_NET;
}
public void setR9_BOOK_VALUE_NET(BigDecimal r9_BOOK_VALUE_NET) {
	R9_BOOK_VALUE_NET = r9_BOOK_VALUE_NET;
}
public BigDecimal getR9_RW() {
	return R9_RW;
}
public void setR9_RW(BigDecimal r9_RW) {
	R9_RW = r9_RW;
}
public BigDecimal getR9_RISK_VALUE() {
	return R9_RISK_VALUE;
}
public void setR9_RISK_VALUE(BigDecimal r9_RISK_VALUE) {
	R9_RISK_VALUE = r9_RISK_VALUE;
}
public BigDecimal getR10_BOOK_VALUE() {
	return R10_BOOK_VALUE;
}
public void setR10_BOOK_VALUE(BigDecimal r10_BOOK_VALUE) {
	R10_BOOK_VALUE = r10_BOOK_VALUE;
}
public BigDecimal getR10_MARGINS() {
	return R10_MARGINS;
}
public void setR10_MARGINS(BigDecimal r10_MARGINS) {
	R10_MARGINS = r10_MARGINS;
}
public BigDecimal getR10_BOOK_VALUE_NET() {
	return R10_BOOK_VALUE_NET;
}
public void setR10_BOOK_VALUE_NET(BigDecimal r10_BOOK_VALUE_NET) {
	R10_BOOK_VALUE_NET = r10_BOOK_VALUE_NET;
}
public BigDecimal getR10_RW() {
	return R10_RW;
}
public void setR10_RW(BigDecimal r10_RW) {
	R10_RW = r10_RW;
}
public BigDecimal getR10_RISK_VALUE() {
	return R10_RISK_VALUE;
}
public void setR10_RISK_VALUE(BigDecimal r10_RISK_VALUE) {
	R10_RISK_VALUE = r10_RISK_VALUE;
}
public BigDecimal getR11_BOOK_VALUE() {
	return R11_BOOK_VALUE;
}
public void setR11_BOOK_VALUE(BigDecimal r11_BOOK_VALUE) {
	R11_BOOK_VALUE = r11_BOOK_VALUE;
}
public BigDecimal getR11_MARGINS() {
	return R11_MARGINS;
}
public void setR11_MARGINS(BigDecimal r11_MARGINS) {
	R11_MARGINS = r11_MARGINS;
}
public BigDecimal getR11_BOOK_VALUE_NET() {
	return R11_BOOK_VALUE_NET;
}
public void setR11_BOOK_VALUE_NET(BigDecimal r11_BOOK_VALUE_NET) {
	R11_BOOK_VALUE_NET = r11_BOOK_VALUE_NET;
}
public BigDecimal getR11_RW() {
	return R11_RW;
}
public void setR11_RW(BigDecimal r11_RW) {
	R11_RW = r11_RW;
}
public BigDecimal getR11_RISK_VALUE() {
	return R11_RISK_VALUE;
}
public void setR11_RISK_VALUE(BigDecimal r11_RISK_VALUE) {
	R11_RISK_VALUE = r11_RISK_VALUE;
}
public BigDecimal getR12_BOOK_VALUE() {
	return R12_BOOK_VALUE;
}
public void setR12_BOOK_VALUE(BigDecimal r12_BOOK_VALUE) {
	R12_BOOK_VALUE = r12_BOOK_VALUE;
}
public BigDecimal getR12_MARGINS() {
	return R12_MARGINS;
}
public void setR12_MARGINS(BigDecimal r12_MARGINS) {
	R12_MARGINS = r12_MARGINS;
}
public BigDecimal getR12_BOOK_VALUE_NET() {
	return R12_BOOK_VALUE_NET;
}
public void setR12_BOOK_VALUE_NET(BigDecimal r12_BOOK_VALUE_NET) {
	R12_BOOK_VALUE_NET = r12_BOOK_VALUE_NET;
}
public BigDecimal getR12_RW() {
	return R12_RW;
}
public void setR12_RW(BigDecimal r12_RW) {
	R12_RW = r12_RW;
}
public BigDecimal getR12_RISK_VALUE() {
	return R12_RISK_VALUE;
}
public void setR12_RISK_VALUE(BigDecimal r12_RISK_VALUE) {
	R12_RISK_VALUE = r12_RISK_VALUE;
}
public BigDecimal getR13_BOOK_VALUE() {
	return R13_BOOK_VALUE;
}
public void setR13_BOOK_VALUE(BigDecimal r13_BOOK_VALUE) {
	R13_BOOK_VALUE = r13_BOOK_VALUE;
}
public BigDecimal getR13_MARGINS() {
	return R13_MARGINS;
}
public void setR13_MARGINS(BigDecimal r13_MARGINS) {
	R13_MARGINS = r13_MARGINS;
}
public BigDecimal getR13_BOOK_VALUE_NET() {
	return R13_BOOK_VALUE_NET;
}
public void setR13_BOOK_VALUE_NET(BigDecimal r13_BOOK_VALUE_NET) {
	R13_BOOK_VALUE_NET = r13_BOOK_VALUE_NET;
}
public BigDecimal getR13_RW() {
	return R13_RW;
}
public void setR13_RW(BigDecimal r13_RW) {
	R13_RW = r13_RW;
}
public BigDecimal getR13_RISK_VALUE() {
	return R13_RISK_VALUE;
}
public void setR13_RISK_VALUE(BigDecimal r13_RISK_VALUE) {
	R13_RISK_VALUE = r13_RISK_VALUE;
}
public BigDecimal getR14_BOOK_VALUE() {
	return R14_BOOK_VALUE;
}
public void setR14_BOOK_VALUE(BigDecimal r14_BOOK_VALUE) {
	R14_BOOK_VALUE = r14_BOOK_VALUE;
}
public BigDecimal getR14_MARGINS() {
	return R14_MARGINS;
}
public void setR14_MARGINS(BigDecimal r14_MARGINS) {
	R14_MARGINS = r14_MARGINS;
}
public BigDecimal getR14_BOOK_VALUE_NET() {
	return R14_BOOK_VALUE_NET;
}
public void setR14_BOOK_VALUE_NET(BigDecimal r14_BOOK_VALUE_NET) {
	R14_BOOK_VALUE_NET = r14_BOOK_VALUE_NET;
}
public BigDecimal getR14_RW() {
	return R14_RW;
}
public void setR14_RW(BigDecimal r14_RW) {
	R14_RW = r14_RW;
}
public BigDecimal getR14_RISK_VALUE() {
	return R14_RISK_VALUE;
}
public void setR14_RISK_VALUE(BigDecimal r14_RISK_VALUE) {
	R14_RISK_VALUE = r14_RISK_VALUE;
}
public BigDecimal getR15_BOOK_VALUE() {
	return R15_BOOK_VALUE;
}
public void setR15_BOOK_VALUE(BigDecimal r15_BOOK_VALUE) {
	R15_BOOK_VALUE = r15_BOOK_VALUE;
}
public BigDecimal getR15_MARGINS() {
	return R15_MARGINS;
}
public void setR15_MARGINS(BigDecimal r15_MARGINS) {
	R15_MARGINS = r15_MARGINS;
}
public BigDecimal getR15_BOOK_VALUE_NET() {
	return R15_BOOK_VALUE_NET;
}
public void setR15_BOOK_VALUE_NET(BigDecimal r15_BOOK_VALUE_NET) {
	R15_BOOK_VALUE_NET = r15_BOOK_VALUE_NET;
}
public BigDecimal getR15_RW() {
	return R15_RW;
}
public void setR15_RW(BigDecimal r15_RW) {
	R15_RW = r15_RW;
}
public BigDecimal getR15_RISK_VALUE() {
	return R15_RISK_VALUE;
}
public void setR15_RISK_VALUE(BigDecimal r15_RISK_VALUE) {
	R15_RISK_VALUE = r15_RISK_VALUE;
}
public BigDecimal getR16_BOOK_VALUE() {
	return R16_BOOK_VALUE;
}
public void setR16_BOOK_VALUE(BigDecimal r16_BOOK_VALUE) {
	R16_BOOK_VALUE = r16_BOOK_VALUE;
}
public BigDecimal getR16_MARGINS() {
	return R16_MARGINS;
}
public void setR16_MARGINS(BigDecimal r16_MARGINS) {
	R16_MARGINS = r16_MARGINS;
}
public BigDecimal getR16_BOOK_VALUE_NET() {
	return R16_BOOK_VALUE_NET;
}
public void setR16_BOOK_VALUE_NET(BigDecimal r16_BOOK_VALUE_NET) {
	R16_BOOK_VALUE_NET = r16_BOOK_VALUE_NET;
}
public BigDecimal getR16_RW() {
	return R16_RW;
}
public void setR16_RW(BigDecimal r16_RW) {
	R16_RW = r16_RW;
}
public BigDecimal getR16_RISK_VALUE() {
	return R16_RISK_VALUE;
}
public void setR16_RISK_VALUE(BigDecimal r16_RISK_VALUE) {
	R16_RISK_VALUE = r16_RISK_VALUE;
}
public BigDecimal getR17_BOOK_VALUE() {
	return R17_BOOK_VALUE;
}
public void setR17_BOOK_VALUE(BigDecimal r17_BOOK_VALUE) {
	R17_BOOK_VALUE = r17_BOOK_VALUE;
}
public BigDecimal getR17_MARGINS() {
	return R17_MARGINS;
}
public void setR17_MARGINS(BigDecimal r17_MARGINS) {
	R17_MARGINS = r17_MARGINS;
}
public BigDecimal getR17_BOOK_VALUE_NET() {
	return R17_BOOK_VALUE_NET;
}
public void setR17_BOOK_VALUE_NET(BigDecimal r17_BOOK_VALUE_NET) {
	R17_BOOK_VALUE_NET = r17_BOOK_VALUE_NET;
}
public BigDecimal getR17_RW() {
	return R17_RW;
}
public void setR17_RW(BigDecimal r17_RW) {
	R17_RW = r17_RW;
}
public BigDecimal getR17_RISK_VALUE() {
	return R17_RISK_VALUE;
}
public void setR17_RISK_VALUE(BigDecimal r17_RISK_VALUE) {
	R17_RISK_VALUE = r17_RISK_VALUE;
}
public BigDecimal getR18_BOOK_VALUE() {
	return R18_BOOK_VALUE;
}
public void setR18_BOOK_VALUE(BigDecimal r18_BOOK_VALUE) {
	R18_BOOK_VALUE = r18_BOOK_VALUE;
}
public BigDecimal getR18_MARGINS() {
	return R18_MARGINS;
}
public void setR18_MARGINS(BigDecimal r18_MARGINS) {
	R18_MARGINS = r18_MARGINS;
}
public BigDecimal getR18_BOOK_VALUE_NET() {
	return R18_BOOK_VALUE_NET;
}
public void setR18_BOOK_VALUE_NET(BigDecimal r18_BOOK_VALUE_NET) {
	R18_BOOK_VALUE_NET = r18_BOOK_VALUE_NET;
}
public BigDecimal getR18_RW() {
	return R18_RW;
}
public void setR18_RW(BigDecimal r18_RW) {
	R18_RW = r18_RW;
}
public BigDecimal getR18_RISK_VALUE() {
	return R18_RISK_VALUE;
}
public void setR18_RISK_VALUE(BigDecimal r18_RISK_VALUE) {
	R18_RISK_VALUE = r18_RISK_VALUE;
}
public BigDecimal getR19_BOOK_VALUE() {
	return R19_BOOK_VALUE;
}
public void setR19_BOOK_VALUE(BigDecimal r19_BOOK_VALUE) {
	R19_BOOK_VALUE = r19_BOOK_VALUE;
}
public BigDecimal getR19_MARGINS() {
	return R19_MARGINS;
}
public void setR19_MARGINS(BigDecimal r19_MARGINS) {
	R19_MARGINS = r19_MARGINS;
}
public BigDecimal getR19_BOOK_VALUE_NET() {
	return R19_BOOK_VALUE_NET;
}
public void setR19_BOOK_VALUE_NET(BigDecimal r19_BOOK_VALUE_NET) {
	R19_BOOK_VALUE_NET = r19_BOOK_VALUE_NET;
}
public BigDecimal getR19_RW() {
	return R19_RW;
}
public void setR19_RW(BigDecimal r19_RW) {
	R19_RW = r19_RW;
}
public BigDecimal getR19_RISK_VALUE() {
	return R19_RISK_VALUE;
}
public void setR19_RISK_VALUE(BigDecimal r19_RISK_VALUE) {
	R19_RISK_VALUE = r19_RISK_VALUE;
}
public BigDecimal getR20_BOOK_VALUE() {
	return R20_BOOK_VALUE;
}
public void setR20_BOOK_VALUE(BigDecimal r20_BOOK_VALUE) {
	R20_BOOK_VALUE = r20_BOOK_VALUE;
}
public BigDecimal getR20_MARGINS() {
	return R20_MARGINS;
}
public void setR20_MARGINS(BigDecimal r20_MARGINS) {
	R20_MARGINS = r20_MARGINS;
}
public BigDecimal getR20_BOOK_VALUE_NET() {
	return R20_BOOK_VALUE_NET;
}
public void setR20_BOOK_VALUE_NET(BigDecimal r20_BOOK_VALUE_NET) {
	R20_BOOK_VALUE_NET = r20_BOOK_VALUE_NET;
}
public BigDecimal getR20_RW() {
	return R20_RW;
}
public void setR20_RW(BigDecimal r20_RW) {
	R20_RW = r20_RW;
}
public BigDecimal getR20_RISK_VALUE() {
	return R20_RISK_VALUE;
}
public void setR20_RISK_VALUE(BigDecimal r20_RISK_VALUE) {
	R20_RISK_VALUE = r20_RISK_VALUE;
}
public BigDecimal getR21_BOOK_VALUE() {
	return R21_BOOK_VALUE;
}
public void setR21_BOOK_VALUE(BigDecimal r21_BOOK_VALUE) {
	R21_BOOK_VALUE = r21_BOOK_VALUE;
}
public BigDecimal getR21_MARGINS() {
	return R21_MARGINS;
}
public void setR21_MARGINS(BigDecimal r21_MARGINS) {
	R21_MARGINS = r21_MARGINS;
}
public BigDecimal getR21_BOOK_VALUE_NET() {
	return R21_BOOK_VALUE_NET;
}
public void setR21_BOOK_VALUE_NET(BigDecimal r21_BOOK_VALUE_NET) {
	R21_BOOK_VALUE_NET = r21_BOOK_VALUE_NET;
}
public BigDecimal getR21_RW() {
	return R21_RW;
}
public void setR21_RW(BigDecimal r21_RW) {
	R21_RW = r21_RW;
}
public BigDecimal getR21_RISK_VALUE() {
	return R21_RISK_VALUE;
}
public void setR21_RISK_VALUE(BigDecimal r21_RISK_VALUE) {
	R21_RISK_VALUE = r21_RISK_VALUE;
}
public BigDecimal getR22_BOOK_VALUE() {
	return R22_BOOK_VALUE;
}
public void setR22_BOOK_VALUE(BigDecimal r22_BOOK_VALUE) {
	R22_BOOK_VALUE = r22_BOOK_VALUE;
}
public BigDecimal getR22_MARGINS() {
	return R22_MARGINS;
}
public void setR22_MARGINS(BigDecimal r22_MARGINS) {
	R22_MARGINS = r22_MARGINS;
}
public BigDecimal getR22_BOOK_VALUE_NET() {
	return R22_BOOK_VALUE_NET;
}
public void setR22_BOOK_VALUE_NET(BigDecimal r22_BOOK_VALUE_NET) {
	R22_BOOK_VALUE_NET = r22_BOOK_VALUE_NET;
}
public BigDecimal getR22_RW() {
	return R22_RW;
}
public void setR22_RW(BigDecimal r22_RW) {
	R22_RW = r22_RW;
}
public BigDecimal getR22_RISK_VALUE() {
	return R22_RISK_VALUE;
}
public void setR22_RISK_VALUE(BigDecimal r22_RISK_VALUE) {
	R22_RISK_VALUE = r22_RISK_VALUE;
}
public BigDecimal getR23_BOOK_VALUE() {
	return R23_BOOK_VALUE;
}
public void setR23_BOOK_VALUE(BigDecimal r23_BOOK_VALUE) {
	R23_BOOK_VALUE = r23_BOOK_VALUE;
}
public BigDecimal getR23_MARGINS() {
	return R23_MARGINS;
}
public void setR23_MARGINS(BigDecimal r23_MARGINS) {
	R23_MARGINS = r23_MARGINS;
}
public BigDecimal getR23_BOOK_VALUE_NET() {
	return R23_BOOK_VALUE_NET;
}
public void setR23_BOOK_VALUE_NET(BigDecimal r23_BOOK_VALUE_NET) {
	R23_BOOK_VALUE_NET = r23_BOOK_VALUE_NET;
}
public BigDecimal getR23_RW() {
	return R23_RW;
}
public void setR23_RW(BigDecimal r23_RW) {
	R23_RW = r23_RW;
}
public BigDecimal getR23_RISK_VALUE() {
	return R23_RISK_VALUE;
}
public void setR23_RISK_VALUE(BigDecimal r23_RISK_VALUE) {
	R23_RISK_VALUE = r23_RISK_VALUE;
}
public BigDecimal getR24_BOOK_VALUE() {
	return R24_BOOK_VALUE;
}
public void setR24_BOOK_VALUE(BigDecimal r24_BOOK_VALUE) {
	R24_BOOK_VALUE = r24_BOOK_VALUE;
}
public BigDecimal getR24_MARGINS() {
	return R24_MARGINS;
}
public void setR24_MARGINS(BigDecimal r24_MARGINS) {
	R24_MARGINS = r24_MARGINS;
}
public BigDecimal getR24_BOOK_VALUE_NET() {
	return R24_BOOK_VALUE_NET;
}
public void setR24_BOOK_VALUE_NET(BigDecimal r24_BOOK_VALUE_NET) {
	R24_BOOK_VALUE_NET = r24_BOOK_VALUE_NET;
}
public BigDecimal getR24_RW() {
	return R24_RW;
}
public void setR24_RW(BigDecimal r24_RW) {
	R24_RW = r24_RW;
}
public BigDecimal getR24_RISK_VALUE() {
	return R24_RISK_VALUE;
}
public void setR24_RISK_VALUE(BigDecimal r24_RISK_VALUE) {
	R24_RISK_VALUE = r24_RISK_VALUE;
}
public BigDecimal getR25_BOOK_VALUE() {
	return R25_BOOK_VALUE;
}
public void setR25_BOOK_VALUE(BigDecimal r25_BOOK_VALUE) {
	R25_BOOK_VALUE = r25_BOOK_VALUE;
}
public BigDecimal getR25_MARGINS() {
	return R25_MARGINS;
}
public void setR25_MARGINS(BigDecimal r25_MARGINS) {
	R25_MARGINS = r25_MARGINS;
}
public BigDecimal getR25_BOOK_VALUE_NET() {
	return R25_BOOK_VALUE_NET;
}
public void setR25_BOOK_VALUE_NET(BigDecimal r25_BOOK_VALUE_NET) {
	R25_BOOK_VALUE_NET = r25_BOOK_VALUE_NET;
}
public BigDecimal getR25_RW() {
	return R25_RW;
}
public void setR25_RW(BigDecimal r25_RW) {
	R25_RW = r25_RW;
}
public BigDecimal getR25_RISK_VALUE() {
	return R25_RISK_VALUE;
}
public void setR25_RISK_VALUE(BigDecimal r25_RISK_VALUE) {
	R25_RISK_VALUE = r25_RISK_VALUE;
}
public BigDecimal getR26_BOOK_VALUE() {
	return R26_BOOK_VALUE;
}
public void setR26_BOOK_VALUE(BigDecimal r26_BOOK_VALUE) {
	R26_BOOK_VALUE = r26_BOOK_VALUE;
}
public BigDecimal getR26_MARGINS() {
	return R26_MARGINS;
}
public void setR26_MARGINS(BigDecimal r26_MARGINS) {
	R26_MARGINS = r26_MARGINS;
}
public BigDecimal getR26_BOOK_VALUE_NET() {
	return R26_BOOK_VALUE_NET;
}
public void setR26_BOOK_VALUE_NET(BigDecimal r26_BOOK_VALUE_NET) {
	R26_BOOK_VALUE_NET = r26_BOOK_VALUE_NET;
}
public BigDecimal getR26_RW() {
	return R26_RW;
}
public void setR26_RW(BigDecimal r26_RW) {
	R26_RW = r26_RW;
}
public BigDecimal getR26_RISK_VALUE() {
	return R26_RISK_VALUE;
}
public void setR26_RISK_VALUE(BigDecimal r26_RISK_VALUE) {
	R26_RISK_VALUE = r26_RISK_VALUE;
}
public BigDecimal getR27_BOOK_VALUE() {
	return R27_BOOK_VALUE;
}
public void setR27_BOOK_VALUE(BigDecimal r27_BOOK_VALUE) {
	R27_BOOK_VALUE = r27_BOOK_VALUE;
}
public BigDecimal getR27_MARGINS() {
	return R27_MARGINS;
}
public void setR27_MARGINS(BigDecimal r27_MARGINS) {
	R27_MARGINS = r27_MARGINS;
}
public BigDecimal getR27_BOOK_VALUE_NET() {
	return R27_BOOK_VALUE_NET;
}
public void setR27_BOOK_VALUE_NET(BigDecimal r27_BOOK_VALUE_NET) {
	R27_BOOK_VALUE_NET = r27_BOOK_VALUE_NET;
}
public BigDecimal getR27_RW() {
	return R27_RW;
}
public void setR27_RW(BigDecimal r27_RW) {
	R27_RW = r27_RW;
}
public BigDecimal getR27_RISK_VALUE() {
	return R27_RISK_VALUE;
}
public void setR27_RISK_VALUE(BigDecimal r27_RISK_VALUE) {
	R27_RISK_VALUE = r27_RISK_VALUE;
}
public BigDecimal getR28_BOOK_VALUE() {
	return R28_BOOK_VALUE;
}
public void setR28_BOOK_VALUE(BigDecimal r28_BOOK_VALUE) {
	R28_BOOK_VALUE = r28_BOOK_VALUE;
}
public BigDecimal getR28_MARGINS() {
	return R28_MARGINS;
}
public void setR28_MARGINS(BigDecimal r28_MARGINS) {
	R28_MARGINS = r28_MARGINS;
}
public BigDecimal getR28_BOOK_VALUE_NET() {
	return R28_BOOK_VALUE_NET;
}
public void setR28_BOOK_VALUE_NET(BigDecimal r28_BOOK_VALUE_NET) {
	R28_BOOK_VALUE_NET = r28_BOOK_VALUE_NET;
}
public BigDecimal getR28_RW() {
	return R28_RW;
}
public void setR28_RW(BigDecimal r28_RW) {
	R28_RW = r28_RW;
}
public BigDecimal getR28_RISK_VALUE() {
	return R28_RISK_VALUE;
}
public void setR28_RISK_VALUE(BigDecimal r28_RISK_VALUE) {
	R28_RISK_VALUE = r28_RISK_VALUE;
}
public BigDecimal getR29_BOOK_VALUE() {
	return R29_BOOK_VALUE;
}
public void setR29_BOOK_VALUE(BigDecimal r29_BOOK_VALUE) {
	R29_BOOK_VALUE = r29_BOOK_VALUE;
}
public BigDecimal getR29_MARGINS() {
	return R29_MARGINS;
}
public void setR29_MARGINS(BigDecimal r29_MARGINS) {
	R29_MARGINS = r29_MARGINS;
}
public BigDecimal getR29_BOOK_VALUE_NET() {
	return R29_BOOK_VALUE_NET;
}
public void setR29_BOOK_VALUE_NET(BigDecimal r29_BOOK_VALUE_NET) {
	R29_BOOK_VALUE_NET = r29_BOOK_VALUE_NET;
}
public BigDecimal getR29_RW() {
	return R29_RW;
}
public void setR29_RW(BigDecimal r29_RW) {
	R29_RW = r29_RW;
}
public BigDecimal getR29_RISK_VALUE() {
	return R29_RISK_VALUE;
}
public void setR29_RISK_VALUE(BigDecimal r29_RISK_VALUE) {
	R29_RISK_VALUE = r29_RISK_VALUE;
}
public BigDecimal getR30_BOOK_VALUE() {
	return R30_BOOK_VALUE;
}
public void setR30_BOOK_VALUE(BigDecimal r30_BOOK_VALUE) {
	R30_BOOK_VALUE = r30_BOOK_VALUE;
}
public BigDecimal getR30_MARGINS() {
	return R30_MARGINS;
}
public void setR30_MARGINS(BigDecimal r30_MARGINS) {
	R30_MARGINS = r30_MARGINS;
}
public BigDecimal getR30_BOOK_VALUE_NET() {
	return R30_BOOK_VALUE_NET;
}
public void setR30_BOOK_VALUE_NET(BigDecimal r30_BOOK_VALUE_NET) {
	R30_BOOK_VALUE_NET = r30_BOOK_VALUE_NET;
}
public BigDecimal getR30_RW() {
	return R30_RW;
}
public void setR30_RW(BigDecimal r30_RW) {
	R30_RW = r30_RW;
}
public BigDecimal getR30_RISK_VALUE() {
	return R30_RISK_VALUE;
}
public void setR30_RISK_VALUE(BigDecimal r30_RISK_VALUE) {
	R30_RISK_VALUE = r30_RISK_VALUE;
}
public BigDecimal getR31_BOOK_VALUE() {
	return R31_BOOK_VALUE;
}
public void setR31_BOOK_VALUE(BigDecimal r31_BOOK_VALUE) {
	R31_BOOK_VALUE = r31_BOOK_VALUE;
}
public BigDecimal getR31_MARGINS() {
	return R31_MARGINS;
}
public void setR31_MARGINS(BigDecimal r31_MARGINS) {
	R31_MARGINS = r31_MARGINS;
}
public BigDecimal getR31_BOOK_VALUE_NET() {
	return R31_BOOK_VALUE_NET;
}
public void setR31_BOOK_VALUE_NET(BigDecimal r31_BOOK_VALUE_NET) {
	R31_BOOK_VALUE_NET = r31_BOOK_VALUE_NET;
}
public BigDecimal getR31_RW() {
	return R31_RW;
}
public void setR31_RW(BigDecimal r31_RW) {
	R31_RW = r31_RW;
}
public BigDecimal getR31_RISK_VALUE() {
	return R31_RISK_VALUE;
}
public void setR31_RISK_VALUE(BigDecimal r31_RISK_VALUE) {
	R31_RISK_VALUE = r31_RISK_VALUE;
}
public BigDecimal getR32_BOOK_VALUE() {
	return R32_BOOK_VALUE;
}
public void setR32_BOOK_VALUE(BigDecimal r32_BOOK_VALUE) {
	R32_BOOK_VALUE = r32_BOOK_VALUE;
}
public BigDecimal getR32_MARGINS() {
	return R32_MARGINS;
}
public void setR32_MARGINS(BigDecimal r32_MARGINS) {
	R32_MARGINS = r32_MARGINS;
}
public BigDecimal getR32_BOOK_VALUE_NET() {
	return R32_BOOK_VALUE_NET;
}
public void setR32_BOOK_VALUE_NET(BigDecimal r32_BOOK_VALUE_NET) {
	R32_BOOK_VALUE_NET = r32_BOOK_VALUE_NET;
}
public BigDecimal getR32_RW() {
	return R32_RW;
}
public void setR32_RW(BigDecimal r32_RW) {
	R32_RW = r32_RW;
}
public BigDecimal getR32_RISK_VALUE() {
	return R32_RISK_VALUE;
}
public void setR32_RISK_VALUE(BigDecimal r32_RISK_VALUE) {
	R32_RISK_VALUE = r32_RISK_VALUE;
}
public BigDecimal getR33_BOOK_VALUE() {
	return R33_BOOK_VALUE;
}
public void setR33_BOOK_VALUE(BigDecimal r33_BOOK_VALUE) {
	R33_BOOK_VALUE = r33_BOOK_VALUE;
}
public BigDecimal getR33_MARGINS() {
	return R33_MARGINS;
}
public void setR33_MARGINS(BigDecimal r33_MARGINS) {
	R33_MARGINS = r33_MARGINS;
}
public BigDecimal getR33_BOOK_VALUE_NET() {
	return R33_BOOK_VALUE_NET;
}
public void setR33_BOOK_VALUE_NET(BigDecimal r33_BOOK_VALUE_NET) {
	R33_BOOK_VALUE_NET = r33_BOOK_VALUE_NET;
}
public BigDecimal getR33_RW() {
	return R33_RW;
}
public void setR33_RW(BigDecimal r33_RW) {
	R33_RW = r33_RW;
}
public BigDecimal getR33_RISK_VALUE() {
	return R33_RISK_VALUE;
}
public void setR33_RISK_VALUE(BigDecimal r33_RISK_VALUE) {
	R33_RISK_VALUE = r33_RISK_VALUE;
}
public BigDecimal getR34_BOOK_VALUE() {
	return R34_BOOK_VALUE;
}
public void setR34_BOOK_VALUE(BigDecimal r34_BOOK_VALUE) {
	R34_BOOK_VALUE = r34_BOOK_VALUE;
}
public BigDecimal getR34_MARGINS() {
	return R34_MARGINS;
}
public void setR34_MARGINS(BigDecimal r34_MARGINS) {
	R34_MARGINS = r34_MARGINS;
}
public BigDecimal getR34_BOOK_VALUE_NET() {
	return R34_BOOK_VALUE_NET;
}
public void setR34_BOOK_VALUE_NET(BigDecimal r34_BOOK_VALUE_NET) {
	R34_BOOK_VALUE_NET = r34_BOOK_VALUE_NET;
}
public BigDecimal getR34_RW() {
	return R34_RW;
}
public void setR34_RW(BigDecimal r34_RW) {
	R34_RW = r34_RW;
}
public BigDecimal getR34_RISK_VALUE() {
	return R34_RISK_VALUE;
}
public void setR34_RISK_VALUE(BigDecimal r34_RISK_VALUE) {
	R34_RISK_VALUE = r34_RISK_VALUE;
}
public BigDecimal getR35_BOOK_VALUE() {
	return R35_BOOK_VALUE;
}
public void setR35_BOOK_VALUE(BigDecimal r35_BOOK_VALUE) {
	R35_BOOK_VALUE = r35_BOOK_VALUE;
}
public BigDecimal getR35_MARGINS() {
	return R35_MARGINS;
}
public void setR35_MARGINS(BigDecimal r35_MARGINS) {
	R35_MARGINS = r35_MARGINS;
}
public BigDecimal getR35_BOOK_VALUE_NET() {
	return R35_BOOK_VALUE_NET;
}
public void setR35_BOOK_VALUE_NET(BigDecimal r35_BOOK_VALUE_NET) {
	R35_BOOK_VALUE_NET = r35_BOOK_VALUE_NET;
}
public BigDecimal getR35_RW() {
	return R35_RW;
}
public void setR35_RW(BigDecimal r35_RW) {
	R35_RW = r35_RW;
}
public BigDecimal getR35_RISK_VALUE() {
	return R35_RISK_VALUE;
}
public void setR35_RISK_VALUE(BigDecimal r35_RISK_VALUE) {
	R35_RISK_VALUE = r35_RISK_VALUE;
}
public BigDecimal getR36_BOOK_VALUE() {
	return R36_BOOK_VALUE;
}
public void setR36_BOOK_VALUE(BigDecimal r36_BOOK_VALUE) {
	R36_BOOK_VALUE = r36_BOOK_VALUE;
}
public BigDecimal getR36_MARGINS() {
	return R36_MARGINS;
}
public void setR36_MARGINS(BigDecimal r36_MARGINS) {
	R36_MARGINS = r36_MARGINS;
}
public BigDecimal getR36_BOOK_VALUE_NET() {
	return R36_BOOK_VALUE_NET;
}
public void setR36_BOOK_VALUE_NET(BigDecimal r36_BOOK_VALUE_NET) {
	R36_BOOK_VALUE_NET = r36_BOOK_VALUE_NET;
}
public BigDecimal getR36_RW() {
	return R36_RW;
}
public void setR36_RW(BigDecimal r36_RW) {
	R36_RW = r36_RW;
}
public BigDecimal getR36_RISK_VALUE() {
	return R36_RISK_VALUE;
}
public void setR36_RISK_VALUE(BigDecimal r36_RISK_VALUE) {
	R36_RISK_VALUE = r36_RISK_VALUE;
}
public BigDecimal getR37_BOOK_VALUE() {
	return R37_BOOK_VALUE;
}
public void setR37_BOOK_VALUE(BigDecimal r37_BOOK_VALUE) {
	R37_BOOK_VALUE = r37_BOOK_VALUE;
}
public BigDecimal getR37_MARGINS() {
	return R37_MARGINS;
}
public void setR37_MARGINS(BigDecimal r37_MARGINS) {
	R37_MARGINS = r37_MARGINS;
}
public BigDecimal getR37_BOOK_VALUE_NET() {
	return R37_BOOK_VALUE_NET;
}
public void setR37_BOOK_VALUE_NET(BigDecimal r37_BOOK_VALUE_NET) {
	R37_BOOK_VALUE_NET = r37_BOOK_VALUE_NET;
}
public BigDecimal getR37_RW() {
	return R37_RW;
}
public void setR37_RW(BigDecimal r37_RW) {
	R37_RW = r37_RW;
}
public BigDecimal getR37_RISK_VALUE() {
	return R37_RISK_VALUE;
}
public void setR37_RISK_VALUE(BigDecimal r37_RISK_VALUE) {
	R37_RISK_VALUE = r37_RISK_VALUE;
}
public BigDecimal getR38_BOOK_VALUE() {
	return R38_BOOK_VALUE;
}
public void setR38_BOOK_VALUE(BigDecimal r38_BOOK_VALUE) {
	R38_BOOK_VALUE = r38_BOOK_VALUE;
}
public BigDecimal getR38_MARGINS() {
	return R38_MARGINS;
}
public void setR38_MARGINS(BigDecimal r38_MARGINS) {
	R38_MARGINS = r38_MARGINS;
}
public BigDecimal getR38_BOOK_VALUE_NET() {
	return R38_BOOK_VALUE_NET;
}
public void setR38_BOOK_VALUE_NET(BigDecimal r38_BOOK_VALUE_NET) {
	R38_BOOK_VALUE_NET = r38_BOOK_VALUE_NET;
}
public BigDecimal getR38_RW() {
	return R38_RW;
}
public void setR38_RW(BigDecimal r38_RW) {
	R38_RW = r38_RW;
}
public BigDecimal getR38_RISK_VALUE() {
	return R38_RISK_VALUE;
}
public void setR38_RISK_VALUE(BigDecimal r38_RISK_VALUE) {
	R38_RISK_VALUE = r38_RISK_VALUE;
}
public BigDecimal getR39_BOOK_VALUE() {
	return R39_BOOK_VALUE;
}
public void setR39_BOOK_VALUE(BigDecimal r39_BOOK_VALUE) {
	R39_BOOK_VALUE = r39_BOOK_VALUE;
}
public BigDecimal getR39_MARGINS() {
	return R39_MARGINS;
}
public void setR39_MARGINS(BigDecimal r39_MARGINS) {
	R39_MARGINS = r39_MARGINS;
}
public BigDecimal getR39_BOOK_VALUE_NET() {
	return R39_BOOK_VALUE_NET;
}
public void setR39_BOOK_VALUE_NET(BigDecimal r39_BOOK_VALUE_NET) {
	R39_BOOK_VALUE_NET = r39_BOOK_VALUE_NET;
}
public BigDecimal getR39_RW() {
	return R39_RW;
}
public void setR39_RW(BigDecimal r39_RW) {
	R39_RW = r39_RW;
}
public BigDecimal getR39_RISK_VALUE() {
	return R39_RISK_VALUE;
}
public void setR39_RISK_VALUE(BigDecimal r39_RISK_VALUE) {
	R39_RISK_VALUE = r39_RISK_VALUE;
}
public BigDecimal getR40_BOOK_VALUE() {
	return R40_BOOK_VALUE;
}
public void setR40_BOOK_VALUE(BigDecimal r40_BOOK_VALUE) {
	R40_BOOK_VALUE = r40_BOOK_VALUE;
}
public BigDecimal getR40_MARGINS() {
	return R40_MARGINS;
}
public void setR40_MARGINS(BigDecimal r40_MARGINS) {
	R40_MARGINS = r40_MARGINS;
}
public BigDecimal getR40_BOOK_VALUE_NET() {
	return R40_BOOK_VALUE_NET;
}
public void setR40_BOOK_VALUE_NET(BigDecimal r40_BOOK_VALUE_NET) {
	R40_BOOK_VALUE_NET = r40_BOOK_VALUE_NET;
}
public BigDecimal getR40_RW() {
	return R40_RW;
}
public void setR40_RW(BigDecimal r40_RW) {
	R40_RW = r40_RW;
}
public BigDecimal getR40_RISK_VALUE() {
	return R40_RISK_VALUE;
}
public void setR40_RISK_VALUE(BigDecimal r40_RISK_VALUE) {
	R40_RISK_VALUE = r40_RISK_VALUE;
}
public BigDecimal getR41_BOOK_VALUE() {
	return R41_BOOK_VALUE;
}
public void setR41_BOOK_VALUE(BigDecimal r41_BOOK_VALUE) {
	R41_BOOK_VALUE = r41_BOOK_VALUE;
}
public BigDecimal getR41_MARGINS() {
	return R41_MARGINS;
}
public void setR41_MARGINS(BigDecimal r41_MARGINS) {
	R41_MARGINS = r41_MARGINS;
}
public BigDecimal getR41_BOOK_VALUE_NET() {
	return R41_BOOK_VALUE_NET;
}
public void setR41_BOOK_VALUE_NET(BigDecimal r41_BOOK_VALUE_NET) {
	R41_BOOK_VALUE_NET = r41_BOOK_VALUE_NET;
}
public BigDecimal getR41_RW() {
	return R41_RW;
}
public void setR41_RW(BigDecimal r41_RW) {
	R41_RW = r41_RW;
}
public BigDecimal getR41_RISK_VALUE() {
	return R41_RISK_VALUE;
}
public void setR41_RISK_VALUE(BigDecimal r41_RISK_VALUE) {
	R41_RISK_VALUE = r41_RISK_VALUE;
}
public BigDecimal getR42_BOOK_VALUE() {
	return R42_BOOK_VALUE;
}
public void setR42_BOOK_VALUE(BigDecimal r42_BOOK_VALUE) {
	R42_BOOK_VALUE = r42_BOOK_VALUE;
}
public BigDecimal getR42_MARGINS() {
	return R42_MARGINS;
}
public void setR42_MARGINS(BigDecimal r42_MARGINS) {
	R42_MARGINS = r42_MARGINS;
}
public BigDecimal getR42_BOOK_VALUE_NET() {
	return R42_BOOK_VALUE_NET;
}
public void setR42_BOOK_VALUE_NET(BigDecimal r42_BOOK_VALUE_NET) {
	R42_BOOK_VALUE_NET = r42_BOOK_VALUE_NET;
}
public BigDecimal getR42_RW() {
	return R42_RW;
}
public void setR42_RW(BigDecimal r42_RW) {
	R42_RW = r42_RW;
}
public BigDecimal getR42_RISK_VALUE() {
	return R42_RISK_VALUE;
}
public void setR42_RISK_VALUE(BigDecimal r42_RISK_VALUE) {
	R42_RISK_VALUE = r42_RISK_VALUE;
}
public BigDecimal getR43_BOOK_VALUE() {
	return R43_BOOK_VALUE;
}
public void setR43_BOOK_VALUE(BigDecimal r43_BOOK_VALUE) {
	R43_BOOK_VALUE = r43_BOOK_VALUE;
}
public BigDecimal getR43_MARGINS() {
	return R43_MARGINS;
}
public void setR43_MARGINS(BigDecimal r43_MARGINS) {
	R43_MARGINS = r43_MARGINS;
}
public BigDecimal getR43_BOOK_VALUE_NET() {
	return R43_BOOK_VALUE_NET;
}
public void setR43_BOOK_VALUE_NET(BigDecimal r43_BOOK_VALUE_NET) {
	R43_BOOK_VALUE_NET = r43_BOOK_VALUE_NET;
}
public BigDecimal getR43_RW() {
	return R43_RW;
}
public void setR43_RW(BigDecimal r43_RW) {
	R43_RW = r43_RW;
}
public BigDecimal getR43_RISK_VALUE() {
	return R43_RISK_VALUE;
}
public void setR43_RISK_VALUE(BigDecimal r43_RISK_VALUE) {
	R43_RISK_VALUE = r43_RISK_VALUE;
}
public BigDecimal getR44_BOOK_VALUE() {
	return R44_BOOK_VALUE;
}
public void setR44_BOOK_VALUE(BigDecimal r44_BOOK_VALUE) {
	R44_BOOK_VALUE = r44_BOOK_VALUE;
}
public BigDecimal getR44_MARGINS() {
	return R44_MARGINS;
}
public void setR44_MARGINS(BigDecimal r44_MARGINS) {
	R44_MARGINS = r44_MARGINS;
}
public BigDecimal getR44_BOOK_VALUE_NET() {
	return R44_BOOK_VALUE_NET;
}
public void setR44_BOOK_VALUE_NET(BigDecimal r44_BOOK_VALUE_NET) {
	R44_BOOK_VALUE_NET = r44_BOOK_VALUE_NET;
}
public BigDecimal getR44_RW() {
	return R44_RW;
}
public void setR44_RW(BigDecimal r44_RW) {
	R44_RW = r44_RW;
}
public BigDecimal getR44_RISK_VALUE() {
	return R44_RISK_VALUE;
}
public void setR44_RISK_VALUE(BigDecimal r44_RISK_VALUE) {
	R44_RISK_VALUE = r44_RISK_VALUE;
}
public BigDecimal getR45_BOOK_VALUE() {
	return R45_BOOK_VALUE;
}
public void setR45_BOOK_VALUE(BigDecimal r45_BOOK_VALUE) {
	R45_BOOK_VALUE = r45_BOOK_VALUE;
}
public BigDecimal getR45_MARGINS() {
	return R45_MARGINS;
}
public void setR45_MARGINS(BigDecimal r45_MARGINS) {
	R45_MARGINS = r45_MARGINS;
}
public BigDecimal getR45_BOOK_VALUE_NET() {
	return R45_BOOK_VALUE_NET;
}
public void setR45_BOOK_VALUE_NET(BigDecimal r45_BOOK_VALUE_NET) {
	R45_BOOK_VALUE_NET = r45_BOOK_VALUE_NET;
}
public BigDecimal getR45_RW() {
	return R45_RW;
}
public void setR45_RW(BigDecimal r45_RW) {
	R45_RW = r45_RW;
}
public BigDecimal getR45_RISK_VALUE() {
	return R45_RISK_VALUE;
}
public void setR45_RISK_VALUE(BigDecimal r45_RISK_VALUE) {
	R45_RISK_VALUE = r45_RISK_VALUE;
}
public BigDecimal getR46_BOOK_VALUE() {
	return R46_BOOK_VALUE;
}
public void setR46_BOOK_VALUE(BigDecimal r46_BOOK_VALUE) {
	R46_BOOK_VALUE = r46_BOOK_VALUE;
}
public BigDecimal getR46_MARGINS() {
	return R46_MARGINS;
}
public void setR46_MARGINS(BigDecimal r46_MARGINS) {
	R46_MARGINS = r46_MARGINS;
}
public BigDecimal getR46_BOOK_VALUE_NET() {
	return R46_BOOK_VALUE_NET;
}
public void setR46_BOOK_VALUE_NET(BigDecimal r46_BOOK_VALUE_NET) {
	R46_BOOK_VALUE_NET = r46_BOOK_VALUE_NET;
}
public BigDecimal getR46_RW() {
	return R46_RW;
}
public void setR46_RW(BigDecimal r46_RW) {
	R46_RW = r46_RW;
}
public BigDecimal getR46_RISK_VALUE() {
	return R46_RISK_VALUE;
}
public void setR46_RISK_VALUE(BigDecimal r46_RISK_VALUE) {
	R46_RISK_VALUE = r46_RISK_VALUE;
}
public BigDecimal getR48_BOOK_VALUE() {
	return R48_BOOK_VALUE;
}
public void setR48_BOOK_VALUE(BigDecimal r48_BOOK_VALUE) {
	R48_BOOK_VALUE = r48_BOOK_VALUE;
}
public BigDecimal getR48_MARGINS() {
	return R48_MARGINS;
}
public void setR48_MARGINS(BigDecimal r48_MARGINS) {
	R48_MARGINS = r48_MARGINS;
}
public BigDecimal getR48_BOOK_VALUE_NET() {
	return R48_BOOK_VALUE_NET;
}
public void setR48_BOOK_VALUE_NET(BigDecimal r48_BOOK_VALUE_NET) {
	R48_BOOK_VALUE_NET = r48_BOOK_VALUE_NET;
}
public BigDecimal getR48_RW() {
	return R48_RW;
}
public void setR48_RW(BigDecimal r48_RW) {
	R48_RW = r48_RW;
}
public BigDecimal getR48_RISK_VALUE() {
	return R48_RISK_VALUE;
}
public void setR48_RISK_VALUE(BigDecimal r48_RISK_VALUE) {
	R48_RISK_VALUE = r48_RISK_VALUE;
}
public BigDecimal getR61_BOOK_VALUE() {
	return R61_BOOK_VALUE;
}
public void setR61_BOOK_VALUE(BigDecimal r61_BOOK_VALUE) {
	R61_BOOK_VALUE = r61_BOOK_VALUE;
}
public BigDecimal getR61_MARGINS() {
	return R61_MARGINS;
}
public void setR61_MARGINS(BigDecimal r61_MARGINS) {
	R61_MARGINS = r61_MARGINS;
}
public BigDecimal getR61_BOOK_VALUE_NET() {
	return R61_BOOK_VALUE_NET;
}
public void setR61_BOOK_VALUE_NET(BigDecimal r61_BOOK_VALUE_NET) {
	R61_BOOK_VALUE_NET = r61_BOOK_VALUE_NET;
}
public BigDecimal getR61_RW() {
	return R61_RW;
}
public void setR61_RW(BigDecimal r61_RW) {
	R61_RW = r61_RW;
}
public BigDecimal getR61_RISK_VALUE() {
	return R61_RISK_VALUE;
}
public void setR61_RISK_VALUE(BigDecimal r61_RISK_VALUE) {
	R61_RISK_VALUE = r61_RISK_VALUE;
}
public BigDecimal getR63_BOOK_VALUE() {
	return R63_BOOK_VALUE;
}
public void setR63_BOOK_VALUE(BigDecimal r63_BOOK_VALUE) {
	R63_BOOK_VALUE = r63_BOOK_VALUE;
}
public BigDecimal getR63_MARGINS() {
	return R63_MARGINS;
}
public void setR63_MARGINS(BigDecimal r63_MARGINS) {
	R63_MARGINS = r63_MARGINS;
}
public BigDecimal getR63_BOOK_VALUE_NET() {
	return R63_BOOK_VALUE_NET;
}
public void setR63_BOOK_VALUE_NET(BigDecimal r63_BOOK_VALUE_NET) {
	R63_BOOK_VALUE_NET = r63_BOOK_VALUE_NET;
}
public BigDecimal getR63_RW() {
	return R63_RW;
}
public void setR63_RW(BigDecimal r63_RW) {
	R63_RW = r63_RW;
}
public BigDecimal getR63_RISK_VALUE() {
	return R63_RISK_VALUE;
}
public void setR63_RISK_VALUE(BigDecimal r63_RISK_VALUE) {
	R63_RISK_VALUE = r63_RISK_VALUE;
}
public BigDecimal getR64_BOOK_VALUE() {
	return R64_BOOK_VALUE;
}
public void setR64_BOOK_VALUE(BigDecimal r64_BOOK_VALUE) {
	R64_BOOK_VALUE = r64_BOOK_VALUE;
}
public BigDecimal getR64_MARGINS() {
	return R64_MARGINS;
}
public void setR64_MARGINS(BigDecimal r64_MARGINS) {
	R64_MARGINS = r64_MARGINS;
}
public BigDecimal getR64_BOOK_VALUE_NET() {
	return R64_BOOK_VALUE_NET;
}
public void setR64_BOOK_VALUE_NET(BigDecimal r64_BOOK_VALUE_NET) {
	R64_BOOK_VALUE_NET = r64_BOOK_VALUE_NET;
}
public BigDecimal getR64_RW() {
	return R64_RW;
}
public void setR64_RW(BigDecimal r64_RW) {
	R64_RW = r64_RW;
}
public BigDecimal getR64_RISK_VALUE() {
	return R64_RISK_VALUE;
}
public void setR64_RISK_VALUE(BigDecimal r64_RISK_VALUE) {
	R64_RISK_VALUE = r64_RISK_VALUE;
}
public BigDecimal getR65_BOOK_VALUE() {
	return R65_BOOK_VALUE;
}
public void setR65_BOOK_VALUE(BigDecimal r65_BOOK_VALUE) {
	R65_BOOK_VALUE = r65_BOOK_VALUE;
}
public BigDecimal getR65_MARGINS() {
	return R65_MARGINS;
}
public void setR65_MARGINS(BigDecimal r65_MARGINS) {
	R65_MARGINS = r65_MARGINS;
}
public BigDecimal getR65_BOOK_VALUE_NET() {
	return R65_BOOK_VALUE_NET;
}
public void setR65_BOOK_VALUE_NET(BigDecimal r65_BOOK_VALUE_NET) {
	R65_BOOK_VALUE_NET = r65_BOOK_VALUE_NET;
}
public BigDecimal getR65_RW() {
	return R65_RW;
}
public void setR65_RW(BigDecimal r65_RW) {
	R65_RW = r65_RW;
}
public BigDecimal getR65_RISK_VALUE() {
	return R65_RISK_VALUE;
}
public void setR65_RISK_VALUE(BigDecimal r65_RISK_VALUE) {
	R65_RISK_VALUE = r65_RISK_VALUE;
}
public BigDecimal getR66_BOOK_VALUE() {
	return R66_BOOK_VALUE;
}
public void setR66_BOOK_VALUE(BigDecimal r66_BOOK_VALUE) {
	R66_BOOK_VALUE = r66_BOOK_VALUE;
}
public BigDecimal getR66_MARGINS() {
	return R66_MARGINS;
}
public void setR66_MARGINS(BigDecimal r66_MARGINS) {
	R66_MARGINS = r66_MARGINS;
}
public BigDecimal getR66_BOOK_VALUE_NET() {
	return R66_BOOK_VALUE_NET;
}
public void setR66_BOOK_VALUE_NET(BigDecimal r66_BOOK_VALUE_NET) {
	R66_BOOK_VALUE_NET = r66_BOOK_VALUE_NET;
}
public BigDecimal getR66_RW() {
	return R66_RW;
}
public void setR66_RW(BigDecimal r66_RW) {
	R66_RW = r66_RW;
}
public BigDecimal getR66_RISK_VALUE() {
	return R66_RISK_VALUE;
}
public void setR66_RISK_VALUE(BigDecimal r66_RISK_VALUE) {
	R66_RISK_VALUE = r66_RISK_VALUE;
}
public BigDecimal getR67_BOOK_VALUE() {
	return R67_BOOK_VALUE;
}
public void setR67_BOOK_VALUE(BigDecimal r67_BOOK_VALUE) {
	R67_BOOK_VALUE = r67_BOOK_VALUE;
}
public BigDecimal getR67_MARGINS() {
	return R67_MARGINS;
}
public void setR67_MARGINS(BigDecimal r67_MARGINS) {
	R67_MARGINS = r67_MARGINS;
}
public BigDecimal getR67_BOOK_VALUE_NET() {
	return R67_BOOK_VALUE_NET;
}
public void setR67_BOOK_VALUE_NET(BigDecimal r67_BOOK_VALUE_NET) {
	R67_BOOK_VALUE_NET = r67_BOOK_VALUE_NET;
}
public BigDecimal getR67_RW() {
	return R67_RW;
}
public void setR67_RW(BigDecimal r67_RW) {
	R67_RW = r67_RW;
}
public BigDecimal getR67_RISK_VALUE() {
	return R67_RISK_VALUE;
}
public void setR67_RISK_VALUE(BigDecimal r67_RISK_VALUE) {
	R67_RISK_VALUE = r67_RISK_VALUE;
}
public BigDecimal getR68_BOOK_VALUE() {
	return R68_BOOK_VALUE;
}
public void setR68_BOOK_VALUE(BigDecimal r68_BOOK_VALUE) {
	R68_BOOK_VALUE = r68_BOOK_VALUE;
}
public BigDecimal getR68_MARGINS() {
	return R68_MARGINS;
}
public void setR68_MARGINS(BigDecimal r68_MARGINS) {
	R68_MARGINS = r68_MARGINS;
}
public BigDecimal getR68_BOOK_VALUE_NET() {
	return R68_BOOK_VALUE_NET;
}
public void setR68_BOOK_VALUE_NET(BigDecimal r68_BOOK_VALUE_NET) {
	R68_BOOK_VALUE_NET = r68_BOOK_VALUE_NET;
}
public BigDecimal getR68_RW() {
	return R68_RW;
}
public void setR68_RW(BigDecimal r68_RW) {
	R68_RW = r68_RW;
}
public BigDecimal getR68_RISK_VALUE() {
	return R68_RISK_VALUE;
}
public void setR68_RISK_VALUE(BigDecimal r68_RISK_VALUE) {
	R68_RISK_VALUE = r68_RISK_VALUE;
}
public BigDecimal getR69_BOOK_VALUE() {
	return R69_BOOK_VALUE;
}
public void setR69_BOOK_VALUE(BigDecimal r69_BOOK_VALUE) {
	R69_BOOK_VALUE = r69_BOOK_VALUE;
}
public BigDecimal getR69_MARGINS() {
	return R69_MARGINS;
}
public void setR69_MARGINS(BigDecimal r69_MARGINS) {
	R69_MARGINS = r69_MARGINS;
}
public BigDecimal getR69_BOOK_VALUE_NET() {
	return R69_BOOK_VALUE_NET;
}
public void setR69_BOOK_VALUE_NET(BigDecimal r69_BOOK_VALUE_NET) {
	R69_BOOK_VALUE_NET = r69_BOOK_VALUE_NET;
}
public BigDecimal getR69_RW() {
	return R69_RW;
}
public void setR69_RW(BigDecimal r69_RW) {
	R69_RW = r69_RW;
}
public BigDecimal getR69_RISK_VALUE() {
	return R69_RISK_VALUE;
}
public void setR69_RISK_VALUE(BigDecimal r69_RISK_VALUE) {
	R69_RISK_VALUE = r69_RISK_VALUE;
}
public BigDecimal getR70_BOOK_VALUE() {
	return R70_BOOK_VALUE;
}
public void setR70_BOOK_VALUE(BigDecimal r70_BOOK_VALUE) {
	R70_BOOK_VALUE = r70_BOOK_VALUE;
}
public BigDecimal getR70_MARGINS() {
	return R70_MARGINS;
}
public void setR70_MARGINS(BigDecimal r70_MARGINS) {
	R70_MARGINS = r70_MARGINS;
}
public BigDecimal getR70_BOOK_VALUE_NET() {
	return R70_BOOK_VALUE_NET;
}
public void setR70_BOOK_VALUE_NET(BigDecimal r70_BOOK_VALUE_NET) {
	R70_BOOK_VALUE_NET = r70_BOOK_VALUE_NET;
}
public BigDecimal getR70_RW() {
	return R70_RW;
}
public void setR70_RW(BigDecimal r70_RW) {
	R70_RW = r70_RW;
}
public BigDecimal getR70_RISK_VALUE() {
	return R70_RISK_VALUE;
}
public void setR70_RISK_VALUE(BigDecimal r70_RISK_VALUE) {
	R70_RISK_VALUE = r70_RISK_VALUE;
}
public BigDecimal getR71_BOOK_VALUE() {
	return R71_BOOK_VALUE;
}
public void setR71_BOOK_VALUE(BigDecimal r71_BOOK_VALUE) {
	R71_BOOK_VALUE = r71_BOOK_VALUE;
}
public BigDecimal getR71_MARGINS() {
	return R71_MARGINS;
}
public void setR71_MARGINS(BigDecimal r71_MARGINS) {
	R71_MARGINS = r71_MARGINS;
}
public BigDecimal getR71_BOOK_VALUE_NET() {
	return R71_BOOK_VALUE_NET;
}
public void setR71_BOOK_VALUE_NET(BigDecimal r71_BOOK_VALUE_NET) {
	R71_BOOK_VALUE_NET = r71_BOOK_VALUE_NET;
}
public BigDecimal getR71_RW() {
	return R71_RW;
}
public void setR71_RW(BigDecimal r71_RW) {
	R71_RW = r71_RW;
}
public BigDecimal getR71_RISK_VALUE() {
	return R71_RISK_VALUE;
}
public void setR71_RISK_VALUE(BigDecimal r71_RISK_VALUE) {
	R71_RISK_VALUE = r71_RISK_VALUE;
}
public BigDecimal getR72_BOOK_VALUE() {
	return R72_BOOK_VALUE;
}
public void setR72_BOOK_VALUE(BigDecimal r72_BOOK_VALUE) {
	R72_BOOK_VALUE = r72_BOOK_VALUE;
}
public BigDecimal getR72_MARGINS() {
	return R72_MARGINS;
}
public void setR72_MARGINS(BigDecimal r72_MARGINS) {
	R72_MARGINS = r72_MARGINS;
}
public BigDecimal getR72_BOOK_VALUE_NET() {
	return R72_BOOK_VALUE_NET;
}
public void setR72_BOOK_VALUE_NET(BigDecimal r72_BOOK_VALUE_NET) {
	R72_BOOK_VALUE_NET = r72_BOOK_VALUE_NET;
}
public BigDecimal getR72_RW() {
	return R72_RW;
}
public void setR72_RW(BigDecimal r72_RW) {
	R72_RW = r72_RW;
}
public BigDecimal getR72_RISK_VALUE() {
	return R72_RISK_VALUE;
}
public void setR72_RISK_VALUE(BigDecimal r72_RISK_VALUE) {
	R72_RISK_VALUE = r72_RISK_VALUE;
}
public BigDecimal getR73_BOOK_VALUE() {
	return R73_BOOK_VALUE;
}
public void setR73_BOOK_VALUE(BigDecimal r73_BOOK_VALUE) {
	R73_BOOK_VALUE = r73_BOOK_VALUE;
}
public BigDecimal getR73_MARGINS() {
	return R73_MARGINS;
}
public void setR73_MARGINS(BigDecimal r73_MARGINS) {
	R73_MARGINS = r73_MARGINS;
}
public BigDecimal getR73_BOOK_VALUE_NET() {
	return R73_BOOK_VALUE_NET;
}
public void setR73_BOOK_VALUE_NET(BigDecimal r73_BOOK_VALUE_NET) {
	R73_BOOK_VALUE_NET = r73_BOOK_VALUE_NET;
}
public BigDecimal getR73_RW() {
	return R73_RW;
}
public void setR73_RW(BigDecimal r73_RW) {
	R73_RW = r73_RW;
}
public BigDecimal getR73_RISK_VALUE() {
	return R73_RISK_VALUE;
}
public void setR73_RISK_VALUE(BigDecimal r73_RISK_VALUE) {
	R73_RISK_VALUE = r73_RISK_VALUE;
}
public BigDecimal getR74_BOOK_VALUE() {
	return R74_BOOK_VALUE;
}
public void setR74_BOOK_VALUE(BigDecimal r74_BOOK_VALUE) {
	R74_BOOK_VALUE = r74_BOOK_VALUE;
}
public BigDecimal getR74_MARGINS() {
	return R74_MARGINS;
}
public void setR74_MARGINS(BigDecimal r74_MARGINS) {
	R74_MARGINS = r74_MARGINS;
}
public BigDecimal getR74_BOOK_VALUE_NET() {
	return R74_BOOK_VALUE_NET;
}
public void setR74_BOOK_VALUE_NET(BigDecimal r74_BOOK_VALUE_NET) {
	R74_BOOK_VALUE_NET = r74_BOOK_VALUE_NET;
}
public BigDecimal getR74_RW() {
	return R74_RW;
}
public void setR74_RW(BigDecimal r74_RW) {
	R74_RW = r74_RW;
}
public BigDecimal getR74_RISK_VALUE() {
	return R74_RISK_VALUE;
}
public void setR74_RISK_VALUE(BigDecimal r74_RISK_VALUE) {
	R74_RISK_VALUE = r74_RISK_VALUE;
}
public BigDecimal getR75_BOOK_VALUE() {
	return R75_BOOK_VALUE;
}
public void setR75_BOOK_VALUE(BigDecimal r75_BOOK_VALUE) {
	R75_BOOK_VALUE = r75_BOOK_VALUE;
}
public BigDecimal getR75_MARGINS() {
	return R75_MARGINS;
}
public void setR75_MARGINS(BigDecimal r75_MARGINS) {
	R75_MARGINS = r75_MARGINS;
}
public BigDecimal getR75_BOOK_VALUE_NET() {
	return R75_BOOK_VALUE_NET;
}
public void setR75_BOOK_VALUE_NET(BigDecimal r75_BOOK_VALUE_NET) {
	R75_BOOK_VALUE_NET = r75_BOOK_VALUE_NET;
}
public BigDecimal getR75_RW() {
	return R75_RW;
}
public void setR75_RW(BigDecimal r75_RW) {
	R75_RW = r75_RW;
}
public BigDecimal getR75_RISK_VALUE() {
	return R75_RISK_VALUE;
}
public void setR75_RISK_VALUE(BigDecimal r75_RISK_VALUE) {
	R75_RISK_VALUE = r75_RISK_VALUE;
}
public BigDecimal getR76_BOOK_VALUE() {
	return R76_BOOK_VALUE;
}
public void setR76_BOOK_VALUE(BigDecimal r76_BOOK_VALUE) {
	R76_BOOK_VALUE = r76_BOOK_VALUE;
}
public BigDecimal getR76_MARGINS() {
	return R76_MARGINS;
}
public void setR76_MARGINS(BigDecimal r76_MARGINS) {
	R76_MARGINS = r76_MARGINS;
}
public BigDecimal getR76_BOOK_VALUE_NET() {
	return R76_BOOK_VALUE_NET;
}
public void setR76_BOOK_VALUE_NET(BigDecimal r76_BOOK_VALUE_NET) {
	R76_BOOK_VALUE_NET = r76_BOOK_VALUE_NET;
}
public BigDecimal getR76_RW() {
	return R76_RW;
}
public void setR76_RW(BigDecimal r76_RW) {
	R76_RW = r76_RW;
}
public BigDecimal getR76_RISK_VALUE() {
	return R76_RISK_VALUE;
}
public void setR76_RISK_VALUE(BigDecimal r76_RISK_VALUE) {
	R76_RISK_VALUE = r76_RISK_VALUE;
}
public BigDecimal getR77_BOOK_VALUE() {
	return R77_BOOK_VALUE;
}
public void setR77_BOOK_VALUE(BigDecimal r77_BOOK_VALUE) {
	R77_BOOK_VALUE = r77_BOOK_VALUE;
}
public BigDecimal getR77_MARGINS() {
	return R77_MARGINS;
}
public void setR77_MARGINS(BigDecimal r77_MARGINS) {
	R77_MARGINS = r77_MARGINS;
}
public BigDecimal getR77_BOOK_VALUE_NET() {
	return R77_BOOK_VALUE_NET;
}
public void setR77_BOOK_VALUE_NET(BigDecimal r77_BOOK_VALUE_NET) {
	R77_BOOK_VALUE_NET = r77_BOOK_VALUE_NET;
}
public BigDecimal getR77_RW() {
	return R77_RW;
}
public void setR77_RW(BigDecimal r77_RW) {
	R77_RW = r77_RW;
}
public BigDecimal getR77_RISK_VALUE() {
	return R77_RISK_VALUE;
}
public void setR77_RISK_VALUE(BigDecimal r77_RISK_VALUE) {
	R77_RISK_VALUE = r77_RISK_VALUE;
}
public BigDecimal getR78_BOOK_VALUE() {
	return R78_BOOK_VALUE;
}
public void setR78_BOOK_VALUE(BigDecimal r78_BOOK_VALUE) {
	R78_BOOK_VALUE = r78_BOOK_VALUE;
}
public BigDecimal getR78_MARGINS() {
	return R78_MARGINS;
}
public void setR78_MARGINS(BigDecimal r78_MARGINS) {
	R78_MARGINS = r78_MARGINS;
}
public BigDecimal getR78_BOOK_VALUE_NET() {
	return R78_BOOK_VALUE_NET;
}
public void setR78_BOOK_VALUE_NET(BigDecimal r78_BOOK_VALUE_NET) {
	R78_BOOK_VALUE_NET = r78_BOOK_VALUE_NET;
}
public BigDecimal getR78_RW() {
	return R78_RW;
}
public void setR78_RW(BigDecimal r78_RW) {
	R78_RW = r78_RW;
}
public BigDecimal getR78_RISK_VALUE() {
	return R78_RISK_VALUE;
}
public void setR78_RISK_VALUE(BigDecimal r78_RISK_VALUE) {
	R78_RISK_VALUE = r78_RISK_VALUE;
}
public BigDecimal getR79_BOOK_VALUE() {
	return R79_BOOK_VALUE;
}
public void setR79_BOOK_VALUE(BigDecimal r79_BOOK_VALUE) {
	R79_BOOK_VALUE = r79_BOOK_VALUE;
}
public BigDecimal getR79_MARGINS() {
	return R79_MARGINS;
}
public void setR79_MARGINS(BigDecimal r79_MARGINS) {
	R79_MARGINS = r79_MARGINS;
}
public BigDecimal getR79_BOOK_VALUE_NET() {
	return R79_BOOK_VALUE_NET;
}
public void setR79_BOOK_VALUE_NET(BigDecimal r79_BOOK_VALUE_NET) {
	R79_BOOK_VALUE_NET = r79_BOOK_VALUE_NET;
}
public BigDecimal getR79_RW() {
	return R79_RW;
}
public void setR79_RW(BigDecimal r79_RW) {
	R79_RW = r79_RW;
}
public BigDecimal getR79_RISK_VALUE() {
	return R79_RISK_VALUE;
}
public void setR79_RISK_VALUE(BigDecimal r79_RISK_VALUE) {
	R79_RISK_VALUE = r79_RISK_VALUE;
}
public BigDecimal getR80_BOOK_VALUE() {
	return R80_BOOK_VALUE;
}
public void setR80_BOOK_VALUE(BigDecimal r80_BOOK_VALUE) {
	R80_BOOK_VALUE = r80_BOOK_VALUE;
}
public BigDecimal getR80_MARGINS() {
	return R80_MARGINS;
}
public void setR80_MARGINS(BigDecimal r80_MARGINS) {
	R80_MARGINS = r80_MARGINS;
}
public BigDecimal getR80_BOOK_VALUE_NET() {
	return R80_BOOK_VALUE_NET;
}
public void setR80_BOOK_VALUE_NET(BigDecimal r80_BOOK_VALUE_NET) {
	R80_BOOK_VALUE_NET = r80_BOOK_VALUE_NET;
}
public BigDecimal getR80_RW() {
	return R80_RW;
}
public void setR80_RW(BigDecimal r80_RW) {
	R80_RW = r80_RW;
}
public BigDecimal getR80_RISK_VALUE() {
	return R80_RISK_VALUE;
}
public void setR80_RISK_VALUE(BigDecimal r80_RISK_VALUE) {
	R80_RISK_VALUE = r80_RISK_VALUE;
}
public BigDecimal getR81_BOOK_VALUE() {
	return R81_BOOK_VALUE;
}
public void setR81_BOOK_VALUE(BigDecimal r81_BOOK_VALUE) {
	R81_BOOK_VALUE = r81_BOOK_VALUE;
}
public BigDecimal getR81_MARGINS() {
	return R81_MARGINS;
}
public void setR81_MARGINS(BigDecimal r81_MARGINS) {
	R81_MARGINS = r81_MARGINS;
}
public BigDecimal getR81_BOOK_VALUE_NET() {
	return R81_BOOK_VALUE_NET;
}
public void setR81_BOOK_VALUE_NET(BigDecimal r81_BOOK_VALUE_NET) {
	R81_BOOK_VALUE_NET = r81_BOOK_VALUE_NET;
}
public BigDecimal getR81_RW() {
	return R81_RW;
}
public void setR81_RW(BigDecimal r81_RW) {
	R81_RW = r81_RW;
}
public BigDecimal getR81_RISK_VALUE() {
	return R81_RISK_VALUE;
}
public void setR81_RISK_VALUE(BigDecimal r81_RISK_VALUE) {
	R81_RISK_VALUE = r81_RISK_VALUE;
}
public BigDecimal getR82_BOOK_VALUE() {
	return R82_BOOK_VALUE;
}
public void setR82_BOOK_VALUE(BigDecimal r82_BOOK_VALUE) {
	R82_BOOK_VALUE = r82_BOOK_VALUE;
}
public BigDecimal getR82_MARGINS() {
	return R82_MARGINS;
}
public void setR82_MARGINS(BigDecimal r82_MARGINS) {
	R82_MARGINS = r82_MARGINS;
}
public BigDecimal getR82_BOOK_VALUE_NET() {
	return R82_BOOK_VALUE_NET;
}
public void setR82_BOOK_VALUE_NET(BigDecimal r82_BOOK_VALUE_NET) {
	R82_BOOK_VALUE_NET = r82_BOOK_VALUE_NET;
}
public BigDecimal getR82_RW() {
	return R82_RW;
}
public void setR82_RW(BigDecimal r82_RW) {
	R82_RW = r82_RW;
}
public BigDecimal getR82_RISK_VALUE() {
	return R82_RISK_VALUE;
}
public void setR82_RISK_VALUE(BigDecimal r82_RISK_VALUE) {
	R82_RISK_VALUE = r82_RISK_VALUE;
}
public BigDecimal getR97_BOOK_VALUE() {
	return R97_BOOK_VALUE;
}
public void setR97_BOOK_VALUE(BigDecimal r97_BOOK_VALUE) {
	R97_BOOK_VALUE = r97_BOOK_VALUE;
}
public BigDecimal getR97_MARGINS() {
	return R97_MARGINS;
}
public void setR97_MARGINS(BigDecimal r97_MARGINS) {
	R97_MARGINS = r97_MARGINS;
}
public BigDecimal getR97_BOOK_VALUE_NET() {
	return R97_BOOK_VALUE_NET;
}
public void setR97_BOOK_VALUE_NET(BigDecimal r97_BOOK_VALUE_NET) {
	R97_BOOK_VALUE_NET = r97_BOOK_VALUE_NET;
}
public BigDecimal getR97_RW() {
	return R97_RW;
}
public void setR97_RW(BigDecimal r97_RW) {
	R97_RW = r97_RW;
}
public BigDecimal getR97_RISK_VALUE() {
	return R97_RISK_VALUE;
}
public void setR97_RISK_VALUE(BigDecimal r97_RISK_VALUE) {
	R97_RISK_VALUE = r97_RISK_VALUE;
}
public BigDecimal getR98_BOOK_VALUE() {
	return R98_BOOK_VALUE;
}
public void setR98_BOOK_VALUE(BigDecimal r98_BOOK_VALUE) {
	R98_BOOK_VALUE = r98_BOOK_VALUE;
}
public BigDecimal getR98_MARGINS() {
	return R98_MARGINS;
}
public void setR98_MARGINS(BigDecimal r98_MARGINS) {
	R98_MARGINS = r98_MARGINS;
}
public BigDecimal getR98_BOOK_VALUE_NET() {
	return R98_BOOK_VALUE_NET;
}
public void setR98_BOOK_VALUE_NET(BigDecimal r98_BOOK_VALUE_NET) {
	R98_BOOK_VALUE_NET = r98_BOOK_VALUE_NET;
}
public BigDecimal getR98_RW() {
	return R98_RW;
}
public void setR98_RW(BigDecimal r98_RW) {
	R98_RW = r98_RW;
}
public BigDecimal getR98_RISK_VALUE() {
	return R98_RISK_VALUE;
}
public void setR98_RISK_VALUE(BigDecimal r98_RISK_VALUE) {
	R98_RISK_VALUE = r98_RISK_VALUE;
}
public BigDecimal getR99_BOOK_VALUE() {
	return R99_BOOK_VALUE;
}
public void setR99_BOOK_VALUE(BigDecimal r99_BOOK_VALUE) {
	R99_BOOK_VALUE = r99_BOOK_VALUE;
}
public BigDecimal getR99_MARGINS() {
	return R99_MARGINS;
}
public void setR99_MARGINS(BigDecimal r99_MARGINS) {
	R99_MARGINS = r99_MARGINS;
}
public BigDecimal getR99_BOOK_VALUE_NET() {
	return R99_BOOK_VALUE_NET;
}
public void setR99_BOOK_VALUE_NET(BigDecimal r99_BOOK_VALUE_NET) {
	R99_BOOK_VALUE_NET = r99_BOOK_VALUE_NET;
}
public BigDecimal getR99_RW() {
	return R99_RW;
}
public void setR99_RW(BigDecimal r99_RW) {
	R99_RW = r99_RW;
}
public BigDecimal getR99_RISK_VALUE() {
	return R99_RISK_VALUE;
}
public void setR99_RISK_VALUE(BigDecimal r99_RISK_VALUE) {
	R99_RISK_VALUE = r99_RISK_VALUE;
}
public BigDecimal getR100_BOOK_VALUE() {
	return R100_BOOK_VALUE;
}
public void setR100_BOOK_VALUE(BigDecimal r100_BOOK_VALUE) {
	R100_BOOK_VALUE = r100_BOOK_VALUE;
}
public BigDecimal getR100_MARGINS() {
	return R100_MARGINS;
}
public void setR100_MARGINS(BigDecimal r100_MARGINS) {
	R100_MARGINS = r100_MARGINS;
}
public BigDecimal getR100_BOOK_VALUE_NET() {
	return R100_BOOK_VALUE_NET;
}
public void setR100_BOOK_VALUE_NET(BigDecimal r100_BOOK_VALUE_NET) {
	R100_BOOK_VALUE_NET = r100_BOOK_VALUE_NET;
}
public BigDecimal getR100_RW() {
	return R100_RW;
}
public void setR100_RW(BigDecimal r100_RW) {
	R100_RW = r100_RW;
}
public BigDecimal getR100_RISK_VALUE() {
	return R100_RISK_VALUE;
}
public void setR100_RISK_VALUE(BigDecimal r100_RISK_VALUE) {
	R100_RISK_VALUE = r100_RISK_VALUE;
}
public BigDecimal getR101_BOOK_VALUE() {
	return R101_BOOK_VALUE;
}
public void setR101_BOOK_VALUE(BigDecimal r101_BOOK_VALUE) {
	R101_BOOK_VALUE = r101_BOOK_VALUE;
}
public BigDecimal getR101_MARGINS() {
	return R101_MARGINS;
}
public void setR101_MARGINS(BigDecimal r101_MARGINS) {
	R101_MARGINS = r101_MARGINS;
}
public BigDecimal getR101_BOOK_VALUE_NET() {
	return R101_BOOK_VALUE_NET;
}
public void setR101_BOOK_VALUE_NET(BigDecimal r101_BOOK_VALUE_NET) {
	R101_BOOK_VALUE_NET = r101_BOOK_VALUE_NET;
}
public BigDecimal getR101_RW() {
	return R101_RW;
}
public void setR101_RW(BigDecimal r101_RW) {
	R101_RW = r101_RW;
}
public BigDecimal getR101_RISK_VALUE() {
	return R101_RISK_VALUE;
}
public void setR101_RISK_VALUE(BigDecimal r101_RISK_VALUE) {
	R101_RISK_VALUE = r101_RISK_VALUE;
}
public BigDecimal getR102_BOOK_VALUE() {
	return R102_BOOK_VALUE;
}
public void setR102_BOOK_VALUE(BigDecimal r102_BOOK_VALUE) {
	R102_BOOK_VALUE = r102_BOOK_VALUE;
}
public BigDecimal getR102_MARGINS() {
	return R102_MARGINS;
}
public void setR102_MARGINS(BigDecimal r102_MARGINS) {
	R102_MARGINS = r102_MARGINS;
}
public BigDecimal getR102_BOOK_VALUE_NET() {
	return R102_BOOK_VALUE_NET;
}
public void setR102_BOOK_VALUE_NET(BigDecimal r102_BOOK_VALUE_NET) {
	R102_BOOK_VALUE_NET = r102_BOOK_VALUE_NET;
}
public BigDecimal getR102_RW() {
	return R102_RW;
}
public void setR102_RW(BigDecimal r102_RW) {
	R102_RW = r102_RW;
}
public BigDecimal getR102_RISK_VALUE() {
	return R102_RISK_VALUE;
}
public void setR102_RISK_VALUE(BigDecimal r102_RISK_VALUE) {
	R102_RISK_VALUE = r102_RISK_VALUE;
}
public BigDecimal getR103_BOOK_VALUE() {
	return R103_BOOK_VALUE;
}
public void setR103_BOOK_VALUE(BigDecimal r103_BOOK_VALUE) {
	R103_BOOK_VALUE = r103_BOOK_VALUE;
}
public BigDecimal getR103_MARGINS() {
	return R103_MARGINS;
}
public void setR103_MARGINS(BigDecimal r103_MARGINS) {
	R103_MARGINS = r103_MARGINS;
}
public BigDecimal getR103_BOOK_VALUE_NET() {
	return R103_BOOK_VALUE_NET;
}
public void setR103_BOOK_VALUE_NET(BigDecimal r103_BOOK_VALUE_NET) {
	R103_BOOK_VALUE_NET = r103_BOOK_VALUE_NET;
}
public BigDecimal getR103_RW() {
	return R103_RW;
}
public void setR103_RW(BigDecimal r103_RW) {
	R103_RW = r103_RW;
}
public BigDecimal getR103_RISK_VALUE() {
	return R103_RISK_VALUE;
}
public void setR103_RISK_VALUE(BigDecimal r103_RISK_VALUE) {
	R103_RISK_VALUE = r103_RISK_VALUE;
}
public BigDecimal getR104_BOOK_VALUE() {
	return R104_BOOK_VALUE;
}
public void setR104_BOOK_VALUE(BigDecimal r104_BOOK_VALUE) {
	R104_BOOK_VALUE = r104_BOOK_VALUE;
}
public BigDecimal getR104_MARGINS() {
	return R104_MARGINS;
}
public void setR104_MARGINS(BigDecimal r104_MARGINS) {
	R104_MARGINS = r104_MARGINS;
}
public BigDecimal getR104_BOOK_VALUE_NET() {
	return R104_BOOK_VALUE_NET;
}
public void setR104_BOOK_VALUE_NET(BigDecimal r104_BOOK_VALUE_NET) {
	R104_BOOK_VALUE_NET = r104_BOOK_VALUE_NET;
}
public BigDecimal getR104_RW() {
	return R104_RW;
}
public void setR104_RW(BigDecimal r104_RW) {
	R104_RW = r104_RW;
}
public BigDecimal getR104_RISK_VALUE() {
	return R104_RISK_VALUE;
}
public void setR104_RISK_VALUE(BigDecimal r104_RISK_VALUE) {
	R104_RISK_VALUE = r104_RISK_VALUE;
}
public BigDecimal getR105_BOOK_VALUE() {
	return R105_BOOK_VALUE;
}
public void setR105_BOOK_VALUE(BigDecimal r105_BOOK_VALUE) {
	R105_BOOK_VALUE = r105_BOOK_VALUE;
}
public BigDecimal getR105_MARGINS() {
	return R105_MARGINS;
}
public void setR105_MARGINS(BigDecimal r105_MARGINS) {
	R105_MARGINS = r105_MARGINS;
}
public BigDecimal getR105_BOOK_VALUE_NET() {
	return R105_BOOK_VALUE_NET;
}
public void setR105_BOOK_VALUE_NET(BigDecimal r105_BOOK_VALUE_NET) {
	R105_BOOK_VALUE_NET = r105_BOOK_VALUE_NET;
}
public BigDecimal getR105_RW() {
	return R105_RW;
}
public void setR105_RW(BigDecimal r105_RW) {
	R105_RW = r105_RW;
}
public BigDecimal getR105_RISK_VALUE() {
	return R105_RISK_VALUE;
}
public void setR105_RISK_VALUE(BigDecimal r105_RISK_VALUE) {
	R105_RISK_VALUE = r105_RISK_VALUE;
}
public BigDecimal getR106_BOOK_VALUE() {
	return R106_BOOK_VALUE;
}
public void setR106_BOOK_VALUE(BigDecimal r106_BOOK_VALUE) {
	R106_BOOK_VALUE = r106_BOOK_VALUE;
}
public BigDecimal getR106_MARGINS() {
	return R106_MARGINS;
}
public void setR106_MARGINS(BigDecimal r106_MARGINS) {
	R106_MARGINS = r106_MARGINS;
}
public BigDecimal getR106_BOOK_VALUE_NET() {
	return R106_BOOK_VALUE_NET;
}
public void setR106_BOOK_VALUE_NET(BigDecimal r106_BOOK_VALUE_NET) {
	R106_BOOK_VALUE_NET = r106_BOOK_VALUE_NET;
}
public BigDecimal getR106_RW() {
	return R106_RW;
}
public void setR106_RW(BigDecimal r106_RW) {
	R106_RW = r106_RW;
}
public BigDecimal getR106_RISK_VALUE() {
	return R106_RISK_VALUE;
}
public void setR106_RISK_VALUE(BigDecimal r106_RISK_VALUE) {
	R106_RISK_VALUE = r106_RISK_VALUE;
}
public BigDecimal getR107_BOOK_VALUE() {
	return R107_BOOK_VALUE;
}
public void setR107_BOOK_VALUE(BigDecimal r107_BOOK_VALUE) {
	R107_BOOK_VALUE = r107_BOOK_VALUE;
}
public BigDecimal getR107_MARGINS() {
	return R107_MARGINS;
}
public void setR107_MARGINS(BigDecimal r107_MARGINS) {
	R107_MARGINS = r107_MARGINS;
}
public BigDecimal getR107_BOOK_VALUE_NET() {
	return R107_BOOK_VALUE_NET;
}
public void setR107_BOOK_VALUE_NET(BigDecimal r107_BOOK_VALUE_NET) {
	R107_BOOK_VALUE_NET = r107_BOOK_VALUE_NET;
}
public BigDecimal getR107_RW() {
	return R107_RW;
}
public void setR107_RW(BigDecimal r107_RW) {
	R107_RW = r107_RW;
}
public BigDecimal getR107_RISK_VALUE() {
	return R107_RISK_VALUE;
}
public void setR107_RISK_VALUE(BigDecimal r107_RISK_VALUE) {
	R107_RISK_VALUE = r107_RISK_VALUE;
}
public BigDecimal getR108_BOOK_VALUE() {
	return R108_BOOK_VALUE;
}
public void setR108_BOOK_VALUE(BigDecimal r108_BOOK_VALUE) {
	R108_BOOK_VALUE = r108_BOOK_VALUE;
}
public BigDecimal getR108_MARGINS() {
	return R108_MARGINS;
}
public void setR108_MARGINS(BigDecimal r108_MARGINS) {
	R108_MARGINS = r108_MARGINS;
}
public BigDecimal getR108_BOOK_VALUE_NET() {
	return R108_BOOK_VALUE_NET;
}
public void setR108_BOOK_VALUE_NET(BigDecimal r108_BOOK_VALUE_NET) {
	R108_BOOK_VALUE_NET = r108_BOOK_VALUE_NET;
}
public BigDecimal getR108_RW() {
	return R108_RW;
}
public void setR108_RW(BigDecimal r108_RW) {
	R108_RW = r108_RW;
}
public BigDecimal getR108_RISK_VALUE() {
	return R108_RISK_VALUE;
}
public void setR108_RISK_VALUE(BigDecimal r108_RISK_VALUE) {
	R108_RISK_VALUE = r108_RISK_VALUE;
}
public BigDecimal getR109_BOOK_VALUE() {
	return R109_BOOK_VALUE;
}
public void setR109_BOOK_VALUE(BigDecimal r109_BOOK_VALUE) {
	R109_BOOK_VALUE = r109_BOOK_VALUE;
}
public BigDecimal getR109_MARGINS() {
	return R109_MARGINS;
}
public void setR109_MARGINS(BigDecimal r109_MARGINS) {
	R109_MARGINS = r109_MARGINS;
}
public BigDecimal getR109_BOOK_VALUE_NET() {
	return R109_BOOK_VALUE_NET;
}
public void setR109_BOOK_VALUE_NET(BigDecimal r109_BOOK_VALUE_NET) {
	R109_BOOK_VALUE_NET = r109_BOOK_VALUE_NET;
}
public BigDecimal getR109_RW() {
	return R109_RW;
}
public void setR109_RW(BigDecimal r109_RW) {
	R109_RW = r109_RW;
}
public BigDecimal getR109_RISK_VALUE() {
	return R109_RISK_VALUE;
}
public void setR109_RISK_VALUE(BigDecimal r109_RISK_VALUE) {
	R109_RISK_VALUE = r109_RISK_VALUE;
}
public BigDecimal getR110_BOOK_VALUE() {
	return R110_BOOK_VALUE;
}
public void setR110_BOOK_VALUE(BigDecimal r110_BOOK_VALUE) {
	R110_BOOK_VALUE = r110_BOOK_VALUE;
}
public BigDecimal getR110_MARGINS() {
	return R110_MARGINS;
}
public void setR110_MARGINS(BigDecimal r110_MARGINS) {
	R110_MARGINS = r110_MARGINS;
}
public BigDecimal getR110_BOOK_VALUE_NET() {
	return R110_BOOK_VALUE_NET;
}
public void setR110_BOOK_VALUE_NET(BigDecimal r110_BOOK_VALUE_NET) {
	R110_BOOK_VALUE_NET = r110_BOOK_VALUE_NET;
}
public BigDecimal getR110_RW() {
	return R110_RW;
}
public void setR110_RW(BigDecimal r110_RW) {
	R110_RW = r110_RW;
}
public BigDecimal getR110_RISK_VALUE() {
	return R110_RISK_VALUE;
}
public void setR110_RISK_VALUE(BigDecimal r110_RISK_VALUE) {
	R110_RISK_VALUE = r110_RISK_VALUE;
}
public BigDecimal getR111_BOOK_VALUE() {
	return R111_BOOK_VALUE;
}
public void setR111_BOOK_VALUE(BigDecimal r111_BOOK_VALUE) {
	R111_BOOK_VALUE = r111_BOOK_VALUE;
}
public BigDecimal getR111_MARGINS() {
	return R111_MARGINS;
}
public void setR111_MARGINS(BigDecimal r111_MARGINS) {
	R111_MARGINS = r111_MARGINS;
}
public BigDecimal getR111_BOOK_VALUE_NET() {
	return R111_BOOK_VALUE_NET;
}
public void setR111_BOOK_VALUE_NET(BigDecimal r111_BOOK_VALUE_NET) {
	R111_BOOK_VALUE_NET = r111_BOOK_VALUE_NET;
}
public BigDecimal getR111_RW() {
	return R111_RW;
}
public void setR111_RW(BigDecimal r111_RW) {
	R111_RW = r111_RW;
}
public BigDecimal getR111_RISK_VALUE() {
	return R111_RISK_VALUE;
}
public void setR111_RISK_VALUE(BigDecimal r111_RISK_VALUE) {
	R111_RISK_VALUE = r111_RISK_VALUE;
}
public BigDecimal getR112_BOOK_VALUE() {
	return R112_BOOK_VALUE;
}
public void setR112_BOOK_VALUE(BigDecimal r112_BOOK_VALUE) {
	R112_BOOK_VALUE = r112_BOOK_VALUE;
}
public BigDecimal getR112_MARGINS() {
	return R112_MARGINS;
}
public void setR112_MARGINS(BigDecimal r112_MARGINS) {
	R112_MARGINS = r112_MARGINS;
}
public BigDecimal getR112_BOOK_VALUE_NET() {
	return R112_BOOK_VALUE_NET;
}
public void setR112_BOOK_VALUE_NET(BigDecimal r112_BOOK_VALUE_NET) {
	R112_BOOK_VALUE_NET = r112_BOOK_VALUE_NET;
}
public BigDecimal getR112_RW() {
	return R112_RW;
}
public void setR112_RW(BigDecimal r112_RW) {
	R112_RW = r112_RW;
}
public BigDecimal getR112_RISK_VALUE() {
	return R112_RISK_VALUE;
}
public void setR112_RISK_VALUE(BigDecimal r112_RISK_VALUE) {
	R112_RISK_VALUE = r112_RISK_VALUE;
}
public BigDecimal getR113_BOOK_VALUE() {
	return R113_BOOK_VALUE;
}
public void setR113_BOOK_VALUE(BigDecimal r113_BOOK_VALUE) {
	R113_BOOK_VALUE = r113_BOOK_VALUE;
}
public BigDecimal getR113_MARGINS() {
	return R113_MARGINS;
}
public void setR113_MARGINS(BigDecimal r113_MARGINS) {
	R113_MARGINS = r113_MARGINS;
}
public BigDecimal getR113_BOOK_VALUE_NET() {
	return R113_BOOK_VALUE_NET;
}
public void setR113_BOOK_VALUE_NET(BigDecimal r113_BOOK_VALUE_NET) {
	R113_BOOK_VALUE_NET = r113_BOOK_VALUE_NET;
}
public BigDecimal getR113_RW() {
	return R113_RW;
}
public void setR113_RW(BigDecimal r113_RW) {
	R113_RW = r113_RW;
}
public BigDecimal getR113_RISK_VALUE() {
	return R113_RISK_VALUE;
}
public void setR113_RISK_VALUE(BigDecimal r113_RISK_VALUE) {
	R113_RISK_VALUE = r113_RISK_VALUE;
}
public BigDecimal getR114_BOOK_VALUE() {
	return R114_BOOK_VALUE;
}
public void setR114_BOOK_VALUE(BigDecimal r114_BOOK_VALUE) {
	R114_BOOK_VALUE = r114_BOOK_VALUE;
}
public BigDecimal getR114_MARGINS() {
	return R114_MARGINS;
}
public void setR114_MARGINS(BigDecimal r114_MARGINS) {
	R114_MARGINS = r114_MARGINS;
}
public BigDecimal getR114_BOOK_VALUE_NET() {
	return R114_BOOK_VALUE_NET;
}
public void setR114_BOOK_VALUE_NET(BigDecimal r114_BOOK_VALUE_NET) {
	R114_BOOK_VALUE_NET = r114_BOOK_VALUE_NET;
}
public BigDecimal getR114_RW() {
	return R114_RW;
}
public void setR114_RW(BigDecimal r114_RW) {
	R114_RW = r114_RW;
}
public BigDecimal getR114_RISK_VALUE() {
	return R114_RISK_VALUE;
}
public void setR114_RISK_VALUE(BigDecimal r114_RISK_VALUE) {
	R114_RISK_VALUE = r114_RISK_VALUE;
}
public BigDecimal getR115_BOOK_VALUE() {
	return R115_BOOK_VALUE;
}
public void setR115_BOOK_VALUE(BigDecimal r115_BOOK_VALUE) {
	R115_BOOK_VALUE = r115_BOOK_VALUE;
}
public BigDecimal getR115_MARGINS() {
	return R115_MARGINS;
}
public void setR115_MARGINS(BigDecimal r115_MARGINS) {
	R115_MARGINS = r115_MARGINS;
}
public BigDecimal getR115_BOOK_VALUE_NET() {
	return R115_BOOK_VALUE_NET;
}
public void setR115_BOOK_VALUE_NET(BigDecimal r115_BOOK_VALUE_NET) {
	R115_BOOK_VALUE_NET = r115_BOOK_VALUE_NET;
}
public BigDecimal getR115_RW() {
	return R115_RW;
}
public void setR115_RW(BigDecimal r115_RW) {
	R115_RW = r115_RW;
}
public BigDecimal getR115_RISK_VALUE() {
	return R115_RISK_VALUE;
}
public void setR115_RISK_VALUE(BigDecimal r115_RISK_VALUE) {
	R115_RISK_VALUE = r115_RISK_VALUE;
}
public BigDecimal getR116_BOOK_VALUE() {
	return R116_BOOK_VALUE;
}
public void setR116_BOOK_VALUE(BigDecimal r116_BOOK_VALUE) {
	R116_BOOK_VALUE = r116_BOOK_VALUE;
}
public BigDecimal getR116_MARGINS() {
	return R116_MARGINS;
}
public void setR116_MARGINS(BigDecimal r116_MARGINS) {
	R116_MARGINS = r116_MARGINS;
}
public BigDecimal getR116_BOOK_VALUE_NET() {
	return R116_BOOK_VALUE_NET;
}
public void setR116_BOOK_VALUE_NET(BigDecimal r116_BOOK_VALUE_NET) {
	R116_BOOK_VALUE_NET = r116_BOOK_VALUE_NET;
}
public BigDecimal getR116_RW() {
	return R116_RW;
}
public void setR116_RW(BigDecimal r116_RW) {
	R116_RW = r116_RW;
}
public BigDecimal getR116_RISK_VALUE() {
	return R116_RISK_VALUE;
}
public void setR116_RISK_VALUE(BigDecimal r116_RISK_VALUE) {
	R116_RISK_VALUE = r116_RISK_VALUE;
}
public BigDecimal getR117_BOOK_VALUE() {
	return R117_BOOK_VALUE;
}
public void setR117_BOOK_VALUE(BigDecimal r117_BOOK_VALUE) {
	R117_BOOK_VALUE = r117_BOOK_VALUE;
}
public BigDecimal getR117_MARGINS() {
	return R117_MARGINS;
}
public void setR117_MARGINS(BigDecimal r117_MARGINS) {
	R117_MARGINS = r117_MARGINS;
}
public BigDecimal getR117_BOOK_VALUE_NET() {
	return R117_BOOK_VALUE_NET;
}
public void setR117_BOOK_VALUE_NET(BigDecimal r117_BOOK_VALUE_NET) {
	R117_BOOK_VALUE_NET = r117_BOOK_VALUE_NET;
}
public BigDecimal getR117_RW() {
	return R117_RW;
}
public void setR117_RW(BigDecimal r117_RW) {
	R117_RW = r117_RW;
}
public BigDecimal getR117_RISK_VALUE() {
	return R117_RISK_VALUE;
}
public void setR117_RISK_VALUE(BigDecimal r117_RISK_VALUE) {
	R117_RISK_VALUE = r117_RISK_VALUE;
}
public BigDecimal getR118_BOOK_VALUE() {
	return R118_BOOK_VALUE;
}
public void setR118_BOOK_VALUE(BigDecimal r118_BOOK_VALUE) {
	R118_BOOK_VALUE = r118_BOOK_VALUE;
}
public BigDecimal getR118_MARGINS() {
	return R118_MARGINS;
}
public void setR118_MARGINS(BigDecimal r118_MARGINS) {
	R118_MARGINS = r118_MARGINS;
}
public BigDecimal getR118_BOOK_VALUE_NET() {
	return R118_BOOK_VALUE_NET;
}
public void setR118_BOOK_VALUE_NET(BigDecimal r118_BOOK_VALUE_NET) {
	R118_BOOK_VALUE_NET = r118_BOOK_VALUE_NET;
}
public BigDecimal getR118_RW() {
	return R118_RW;
}
public void setR118_RW(BigDecimal r118_RW) {
	R118_RW = r118_RW;
}
public BigDecimal getR118_RISK_VALUE() {
	return R118_RISK_VALUE;
}
public void setR118_RISK_VALUE(BigDecimal r118_RISK_VALUE) {
	R118_RISK_VALUE = r118_RISK_VALUE;
}
public BigDecimal getR119_BOOK_VALUE() {
	return R119_BOOK_VALUE;
}
public void setR119_BOOK_VALUE(BigDecimal r119_BOOK_VALUE) {
	R119_BOOK_VALUE = r119_BOOK_VALUE;
}
public BigDecimal getR119_MARGINS() {
	return R119_MARGINS;
}
public void setR119_MARGINS(BigDecimal r119_MARGINS) {
	R119_MARGINS = r119_MARGINS;
}
public BigDecimal getR119_BOOK_VALUE_NET() {
	return R119_BOOK_VALUE_NET;
}
public void setR119_BOOK_VALUE_NET(BigDecimal r119_BOOK_VALUE_NET) {
	R119_BOOK_VALUE_NET = r119_BOOK_VALUE_NET;
}
public BigDecimal getR119_RW() {
	return R119_RW;
}
public void setR119_RW(BigDecimal r119_RW) {
	R119_RW = r119_RW;
}
public BigDecimal getR119_RISK_VALUE() {
	return R119_RISK_VALUE;
}
public void setR119_RISK_VALUE(BigDecimal r119_RISK_VALUE) {
	R119_RISK_VALUE = r119_RISK_VALUE;
}
public BigDecimal getR120_BOOK_VALUE() {
	return R120_BOOK_VALUE;
}
public void setR120_BOOK_VALUE(BigDecimal r120_BOOK_VALUE) {
	R120_BOOK_VALUE = r120_BOOK_VALUE;
}
public BigDecimal getR120_MARGINS() {
	return R120_MARGINS;
}
public void setR120_MARGINS(BigDecimal r120_MARGINS) {
	R120_MARGINS = r120_MARGINS;
}
public BigDecimal getR120_BOOK_VALUE_NET() {
	return R120_BOOK_VALUE_NET;
}
public void setR120_BOOK_VALUE_NET(BigDecimal r120_BOOK_VALUE_NET) {
	R120_BOOK_VALUE_NET = r120_BOOK_VALUE_NET;
}
public BigDecimal getR120_RW() {
	return R120_RW;
}
public void setR120_RW(BigDecimal r120_RW) {
	R120_RW = r120_RW;
}
public BigDecimal getR120_RISK_VALUE() {
	return R120_RISK_VALUE;
}
public void setR120_RISK_VALUE(BigDecimal r120_RISK_VALUE) {
	R120_RISK_VALUE = r120_RISK_VALUE;
}
public BigDecimal getR121_BOOK_VALUE() {
	return R121_BOOK_VALUE;
}
public void setR121_BOOK_VALUE(BigDecimal r121_BOOK_VALUE) {
	R121_BOOK_VALUE = r121_BOOK_VALUE;
}
public BigDecimal getR121_MARGINS() {
	return R121_MARGINS;
}
public void setR121_MARGINS(BigDecimal r121_MARGINS) {
	R121_MARGINS = r121_MARGINS;
}
public BigDecimal getR121_BOOK_VALUE_NET() {
	return R121_BOOK_VALUE_NET;
}
public void setR121_BOOK_VALUE_NET(BigDecimal r121_BOOK_VALUE_NET) {
	R121_BOOK_VALUE_NET = r121_BOOK_VALUE_NET;
}
public BigDecimal getR121_RW() {
	return R121_RW;
}
public void setR121_RW(BigDecimal r121_RW) {
	R121_RW = r121_RW;
}
public BigDecimal getR121_RISK_VALUE() {
	return R121_RISK_VALUE;
}
public void setR121_RISK_VALUE(BigDecimal r121_RISK_VALUE) {
	R121_RISK_VALUE = r121_RISK_VALUE;
}
public BigDecimal getR122_BOOK_VALUE() {
	return R122_BOOK_VALUE;
}
public void setR122_BOOK_VALUE(BigDecimal r122_BOOK_VALUE) {
	R122_BOOK_VALUE = r122_BOOK_VALUE;
}
public BigDecimal getR122_MARGINS() {
	return R122_MARGINS;
}
public void setR122_MARGINS(BigDecimal r122_MARGINS) {
	R122_MARGINS = r122_MARGINS;
}
public BigDecimal getR122_BOOK_VALUE_NET() {
	return R122_BOOK_VALUE_NET;
}
public void setR122_BOOK_VALUE_NET(BigDecimal r122_BOOK_VALUE_NET) {
	R122_BOOK_VALUE_NET = r122_BOOK_VALUE_NET;
}
public BigDecimal getR122_RW() {
	return R122_RW;
}
public void setR122_RW(BigDecimal r122_RW) {
	R122_RW = r122_RW;
}
public BigDecimal getR122_RISK_VALUE() {
	return R122_RISK_VALUE;
}
public void setR122_RISK_VALUE(BigDecimal r122_RISK_VALUE) {
	R122_RISK_VALUE = r122_RISK_VALUE;
}
public BigDecimal getR123_BOOK_VALUE() {
	return R123_BOOK_VALUE;
}
public void setR123_BOOK_VALUE(BigDecimal r123_BOOK_VALUE) {
	R123_BOOK_VALUE = r123_BOOK_VALUE;
}
public BigDecimal getR123_MARGINS() {
	return R123_MARGINS;
}
public void setR123_MARGINS(BigDecimal r123_MARGINS) {
	R123_MARGINS = r123_MARGINS;
}
public BigDecimal getR123_BOOK_VALUE_NET() {
	return R123_BOOK_VALUE_NET;
}
public void setR123_BOOK_VALUE_NET(BigDecimal r123_BOOK_VALUE_NET) {
	R123_BOOK_VALUE_NET = r123_BOOK_VALUE_NET;
}
public BigDecimal getR123_RW() {
	return R123_RW;
}
public void setR123_RW(BigDecimal r123_RW) {
	R123_RW = r123_RW;
}
public BigDecimal getR123_RISK_VALUE() {
	return R123_RISK_VALUE;
}
public void setR123_RISK_VALUE(BigDecimal r123_RISK_VALUE) {
	R123_RISK_VALUE = r123_RISK_VALUE;
}
public BigDecimal getR124_BOOK_VALUE() {
	return R124_BOOK_VALUE;
}
public void setR124_BOOK_VALUE(BigDecimal r124_BOOK_VALUE) {
	R124_BOOK_VALUE = r124_BOOK_VALUE;
}
public BigDecimal getR124_MARGINS() {
	return R124_MARGINS;
}
public void setR124_MARGINS(BigDecimal r124_MARGINS) {
	R124_MARGINS = r124_MARGINS;
}
public BigDecimal getR124_BOOK_VALUE_NET() {
	return R124_BOOK_VALUE_NET;
}
public void setR124_BOOK_VALUE_NET(BigDecimal r124_BOOK_VALUE_NET) {
	R124_BOOK_VALUE_NET = r124_BOOK_VALUE_NET;
}
public BigDecimal getR124_RW() {
	return R124_RW;
}
public void setR124_RW(BigDecimal r124_RW) {
	R124_RW = r124_RW;
}
public BigDecimal getR124_RISK_VALUE() {
	return R124_RISK_VALUE;
}
public void setR124_RISK_VALUE(BigDecimal r124_RISK_VALUE) {
	R124_RISK_VALUE = r124_RISK_VALUE;
}
public BigDecimal getR125_BOOK_VALUE() {
	return R125_BOOK_VALUE;
}
public void setR125_BOOK_VALUE(BigDecimal r125_BOOK_VALUE) {
	R125_BOOK_VALUE = r125_BOOK_VALUE;
}
public BigDecimal getR125_MARGINS() {
	return R125_MARGINS;
}
public void setR125_MARGINS(BigDecimal r125_MARGINS) {
	R125_MARGINS = r125_MARGINS;
}
public BigDecimal getR125_BOOK_VALUE_NET() {
	return R125_BOOK_VALUE_NET;
}
public void setR125_BOOK_VALUE_NET(BigDecimal r125_BOOK_VALUE_NET) {
	R125_BOOK_VALUE_NET = r125_BOOK_VALUE_NET;
}
public BigDecimal getR125_RW() {
	return R125_RW;
}
public void setR125_RW(BigDecimal r125_RW) {
	R125_RW = r125_RW;
}
public BigDecimal getR125_RISK_VALUE() {
	return R125_RISK_VALUE;
}
public void setR125_RISK_VALUE(BigDecimal r125_RISK_VALUE) {
	R125_RISK_VALUE = r125_RISK_VALUE;
}
public BigDecimal getR126_BOOK_VALUE() {
	return R126_BOOK_VALUE;
}
public void setR126_BOOK_VALUE(BigDecimal r126_BOOK_VALUE) {
	R126_BOOK_VALUE = r126_BOOK_VALUE;
}
public BigDecimal getR126_MARGINS() {
	return R126_MARGINS;
}
public void setR126_MARGINS(BigDecimal r126_MARGINS) {
	R126_MARGINS = r126_MARGINS;
}
public BigDecimal getR126_BOOK_VALUE_NET() {
	return R126_BOOK_VALUE_NET;
}
public void setR126_BOOK_VALUE_NET(BigDecimal r126_BOOK_VALUE_NET) {
	R126_BOOK_VALUE_NET = r126_BOOK_VALUE_NET;
}
public BigDecimal getR126_RW() {
	return R126_RW;
}
public void setR126_RW(BigDecimal r126_RW) {
	R126_RW = r126_RW;
}
public BigDecimal getR126_RISK_VALUE() {
	return R126_RISK_VALUE;
}
public void setR126_RISK_VALUE(BigDecimal r126_RISK_VALUE) {
	R126_RISK_VALUE = r126_RISK_VALUE;
}
public BigDecimal getR127_BOOK_VALUE() {
	return R127_BOOK_VALUE;
}
public void setR127_BOOK_VALUE(BigDecimal r127_BOOK_VALUE) {
	R127_BOOK_VALUE = r127_BOOK_VALUE;
}
public BigDecimal getR127_MARGINS() {
	return R127_MARGINS;
}
public void setR127_MARGINS(BigDecimal r127_MARGINS) {
	R127_MARGINS = r127_MARGINS;
}
public BigDecimal getR127_BOOK_VALUE_NET() {
	return R127_BOOK_VALUE_NET;
}
public void setR127_BOOK_VALUE_NET(BigDecimal r127_BOOK_VALUE_NET) {
	R127_BOOK_VALUE_NET = r127_BOOK_VALUE_NET;
}
public BigDecimal getR127_RW() {
	return R127_RW;
}
public void setR127_RW(BigDecimal r127_RW) {
	R127_RW = r127_RW;
}
public BigDecimal getR127_RISK_VALUE() {
	return R127_RISK_VALUE;
}
public void setR127_RISK_VALUE(BigDecimal r127_RISK_VALUE) {
	R127_RISK_VALUE = r127_RISK_VALUE;
}
public BigDecimal getR128_BOOK_VALUE() {
	return R128_BOOK_VALUE;
}
public void setR128_BOOK_VALUE(BigDecimal r128_BOOK_VALUE) {
	R128_BOOK_VALUE = r128_BOOK_VALUE;
}
public BigDecimal getR128_MARGINS() {
	return R128_MARGINS;
}
public void setR128_MARGINS(BigDecimal r128_MARGINS) {
	R128_MARGINS = r128_MARGINS;
}
public BigDecimal getR128_BOOK_VALUE_NET() {
	return R128_BOOK_VALUE_NET;
}
public void setR128_BOOK_VALUE_NET(BigDecimal r128_BOOK_VALUE_NET) {
	R128_BOOK_VALUE_NET = r128_BOOK_VALUE_NET;
}
public BigDecimal getR128_RW() {
	return R128_RW;
}
public void setR128_RW(BigDecimal r128_RW) {
	R128_RW = r128_RW;
}
public BigDecimal getR128_RISK_VALUE() {
	return R128_RISK_VALUE;
}
public void setR128_RISK_VALUE(BigDecimal r128_RISK_VALUE) {
	R128_RISK_VALUE = r128_RISK_VALUE;
}
public BigDecimal getR129_BOOK_VALUE() {
	return R129_BOOK_VALUE;
}
public void setR129_BOOK_VALUE(BigDecimal r129_BOOK_VALUE) {
	R129_BOOK_VALUE = r129_BOOK_VALUE;
}
public BigDecimal getR129_MARGINS() {
	return R129_MARGINS;
}
public void setR129_MARGINS(BigDecimal r129_MARGINS) {
	R129_MARGINS = r129_MARGINS;
}
public BigDecimal getR129_BOOK_VALUE_NET() {
	return R129_BOOK_VALUE_NET;
}
public void setR129_BOOK_VALUE_NET(BigDecimal r129_BOOK_VALUE_NET) {
	R129_BOOK_VALUE_NET = r129_BOOK_VALUE_NET;
}
public BigDecimal getR129_RW() {
	return R129_RW;
}
public void setR129_RW(BigDecimal r129_RW) {
	R129_RW = r129_RW;
}
public BigDecimal getR129_RISK_VALUE() {
	return R129_RISK_VALUE;
}
public void setR129_RISK_VALUE(BigDecimal r129_RISK_VALUE) {
	R129_RISK_VALUE = r129_RISK_VALUE;
}
public BigDecimal getR130_BOOK_VALUE() {
	return R130_BOOK_VALUE;
}
public void setR130_BOOK_VALUE(BigDecimal r130_BOOK_VALUE) {
	R130_BOOK_VALUE = r130_BOOK_VALUE;
}
public BigDecimal getR130_MARGINS() {
	return R130_MARGINS;
}
public void setR130_MARGINS(BigDecimal r130_MARGINS) {
	R130_MARGINS = r130_MARGINS;
}
public BigDecimal getR130_BOOK_VALUE_NET() {
	return R130_BOOK_VALUE_NET;
}
public void setR130_BOOK_VALUE_NET(BigDecimal r130_BOOK_VALUE_NET) {
	R130_BOOK_VALUE_NET = r130_BOOK_VALUE_NET;
}
public BigDecimal getR130_RW() {
	return R130_RW;
}
public void setR130_RW(BigDecimal r130_RW) {
	R130_RW = r130_RW;
}
public BigDecimal getR130_RISK_VALUE() {
	return R130_RISK_VALUE;
}
public void setR130_RISK_VALUE(BigDecimal r130_RISK_VALUE) {
	R130_RISK_VALUE = r130_RISK_VALUE;
}


public Date getReportResubDate() {
	return reportResubDate;
}
public void setReportResubDate(Date reportResubDate) {
	this.reportResubDate = reportResubDate;
}

}

// DETAIL 

public class RWA_Detail_RowMapper implements RowMapper<RWA_Detail_Entity> {

    @Override
    public RWA_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        RWA_Detail_Entity obj = new RWA_Detail_Entity();

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
        obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
        obj.setReportLable(rs.getString("REPORT_LABLE"));
        obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
        obj.setReportLable1(rs.getString("REPORT_LABLE_1"));

        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

        // =========================
        // AMOUNT
        // =========================
        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

        // =========================
        // DATE DETAILS
        // =========================
        obj.setReportDate(rs.getDate("REPORT_DATE"));
        obj.setCreateTime(rs.getDate("CREATE_TIME"));
        obj.setModifyTime(rs.getDate("MODIFY_TIME"));
        obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

        // =========================
        // USER DETAILS
        // =========================
        obj.setReportName(rs.getString("REPORT_NAME"));
        obj.setCreateUser(rs.getString("CREATE_USER"));
        obj.setModifyUser(rs.getString("MODIFY_USER"));
        obj.setVerifyUser(rs.getString("VERIFY_USER"));
		obj.setStaff(rs.getString("STAFF"));
        obj.setLabod(rs.getString("LABOD"));

        // =========================
        // FLAGS
        // =========================
        obj.setEntityFlg(rs.getString("ENTITY_FLG"));
        obj.setModifyFlg(rs.getString("MODIFY_FLG"));
        obj.setDelFlg(rs.getString("DEL_FLG"));

        return obj;
    }
}

public class RWA_Detail_Entity {
	
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
	   @Column(name = "REPORT_ADDL_CRITERIA_2")
	   private String reportAddlCriteria2;
	  
	   @Column(name = "REPORT_LABLE_1")
	   private String reportLable1;
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
	   
	   @Column(name = "STAFF")
private String staff;

@Column(name = "LABOD")
private String labod;

	   
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
	

	public String getReportAddlCriteria2() {
		return reportAddlCriteria2;
	}

	public void setReportAddlCriteria2(String reportAddlCriteria2) {
		this.reportAddlCriteria2 = reportAddlCriteria2;
	}

	public String getReportLable1() {
		return reportLable1;
	}

	public void setReportLable1(String reportLable1) {
		this.reportLable1 = reportLable1;
	}
	
	public String getStaff() {
    return staff;
}

public void setStaff(String staff) {
    this.staff = staff;
}

public String getLabod() {
    return labod;
}

public void setLabod(String labod) {
    this.labod = labod;
}
	

	
}

//---ARCHIVAL DETAIL 

public class RWA_Archival_Detail_RowMapper implements RowMapper<RWA_Archival_Detail_Entity> {

    @Override
    public RWA_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        RWA_Archival_Detail_Entity obj = new RWA_Archival_Detail_Entity();

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
        obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
        obj.setReportLable(rs.getString("REPORT_LABLE"));
        obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
        obj.setReportLable1(rs.getString("REPORT_LABLE_1"));
        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

        // =========================
        // NEW COLUMNS (ADDED)
        // =========================
        obj.setStaff(rs.getString("STAFF"));
        obj.setLabod(rs.getString("LABOD"));

        // =========================
        // AMOUNT
        // =========================
        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

        // =========================
        // DATE DETAILS
        // =========================
        obj.setReportDate(rs.getDate("REPORT_DATE"));
        obj.setCreateTime(rs.getDate("CREATE_TIME"));
        obj.setModifyTime(rs.getDate("MODIFY_TIME"));
        obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

        // =========================
        // USER DETAILS
        // =========================
        obj.setCreateUser(rs.getString("CREATE_USER"));
        obj.setModifyUser(rs.getString("MODIFY_USER"));
        obj.setVerifyUser(rs.getString("VERIFY_USER"));
        obj.setReportName(rs.getString("REPORT_NAME"));

        // =========================
        // FLAGS
        // =========================
        obj.setEntityFlg(rs.getString("ENTITY_FLG"));
        obj.setModifyFlg(rs.getString("MODIFY_FLG"));
        obj.setDelFlg(rs.getString("DEL_FLG"));

        return obj;
    }
}

public class RWA_Archival_Detail_Entity {
	
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
	   
	   @Column(name = "REPORT_ADDL_CRITERIA_2")
	   private String reportAddlCriteria2;
	  
	   @Column(name = "REPORT_LABLE_1")
	   private String reportLable1;
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
	   
	   // NEW FIELDS
@Column(name = "STAFF")
private String staff;

@Column(name = "LABOD")
private String labod;

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

	
	public String getReportAddlCriteria2() {
		return reportAddlCriteria2;
	}

	public void setReportAddlCriteria2(String reportAddlCriteria2) {
		this.reportAddlCriteria2 = reportAddlCriteria2;
	}

	public String getReportLable1() {
		return reportLable1;
	}

	public void setReportLable1(String reportLable1) {
		this.reportLable1 = reportLable1;
	}

	public String getStaff() {
    return staff;
}

public void setStaff(String staff) {
    this.staff = staff;
}

public String getLabod() {
    return labod;
}

public void setLabod(String labod) {
    this.labod = labod;
}

}

//=====================================================
// MODEL AND VIEW METHOD summary
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
		 		 	 public ModelAndView getRWAView(

	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("RWA View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    // =====================================================
	    // ARCHIVAL MODE
	    // =====================================================

	    if ("ARCHIVAL".equals(type) && version != null) {

	        List<RWA_Archival_Summary_Entity> T1Master = new ArrayList<>();
	    

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

	        List<RWA_Summary_Entity> T1Master = new ArrayList<>();
	       

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

	    mv.setViewName("BRRS/RWA");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	

	//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getRWAcurrentDtl(
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

	            List<RWA_Archival_Detail_Entity> archivalDetailList;

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

	            List<RWA_Detail_Entity> currentDetailList;

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

	    mv.setViewName("BRRS/RWA");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
	

//=====================================================
// MODEL AND VIEW METHOD ARCHIVAL VIEW
//=====================================================

public List<Object[]> getRWAArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<RWA_Archival_Summary_Entity> repoData =
						getarchivaldatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (RWA_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					RWA_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  RWA  Archival data: " + e.getMessage());
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
		ModelAndView mv = new ModelAndView("BRRS/RWA"); 

		if (acctNo != null) {
			RWA_Detail_Entity RWAEntity = findByDetailAcctnumber(acctNo);
			if (RWAEntity != null && RWAEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(RWAEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", RWAEntity);
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

			RWA_Detail_Entity existing = findByDetailAcctnumber(acctNo);
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
    "UPDATE BRRS_RWA_DETAILTABLE " +
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
							logger.info("Transaction committed — calling BRRS_RWA_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_RWA_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating RWA record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
//=====================================================
// DETAIL EXCEL 
//=====================================================


	public byte[] getRWADetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  RWA  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getRWADetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("RWA Details ");

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
String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT LABLE1", "REPORT ADDL CRITERIA2",
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
				List<RWA_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (RWA_Detail_Entity item : reportData) { 
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

		row.createCell(4).setCellValue(item.getReportLable());
		row.createCell(5).setCellValue(item.getReportAddlCriteria1());
		row.createCell(6).setCellValue(item.getReportLable1());
		row.createCell(7).setCellValue(item.getReportAddlCriteria2());
		row.createCell(8)
				.setCellValue(item.getReportDate() != null
						? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
						: "");

		// Apply data style for all other cells
		for (int j = 0; j < 9; j++) {
			if (j != 3) {
				row.getCell(j).setCellStyle(dataStyle);
			}
						}
					}
				} else {
					logger.info("No data found for RWA — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating RWA Excel", e);
				return new byte[0];
			}
		}
		
		
//=====================================================
// ARCHIVAL DETAIL EXCEL 
//=====================================================

	public byte[] getRWADetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for RWA ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("RWA Detail NEW");

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
					String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE", "REPORT ADDL CRITERIA", "REPORT LABLE1", "REPORT ADDL CRITERIA2","REPORT_DATE"
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
				List<RWA_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (RWA_Archival_Detail_Entity item : reportData) {
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
row.createCell(6).setCellValue(item.getReportLable1());
row.createCell(7).setCellValue(item.getReportAddlCriteria2());
row.createCell(8).setCellValue(
item.getReportDate() != null ?
new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : ""
);

// Apply data style for all other cells
					for (int j = 0; j < 9; j++) {
						 if (j != 3 ) {
							row.getCell(j).setCellStyle(dataStyle);
						}
						}
					}
				} else {
					logger.info("No data found for RWA — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating RWA NEW Excel", e);
				return new byte[0];
			}
		}
		
		
//=====================================================
// Summary EXCEL 
//=====================================================

	public byte[] getRWAExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.RWA");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelRWAARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<RWA_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		
		
			System.out.println("DATA SIZE IS : "+dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  RWA report. Returning empty result.");
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
						RWA_Summary_Entity record = dataList.get(i);
					
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						
						Cell cell2 = row.createCell(2);
if (record.getR8_BOOK_VALUE() != null) {
cell2.setCellValue(record.getR8_BOOK_VALUE().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

Cell cell3 = row.createCell(3);
if (record.getR8_MARGINS() != null) {
cell3.setCellValue(record.getR8_MARGINS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

Cell cell5 = row.createCell(5);
if (record.getR8_RW() != null) {
cell5.setCellValue(record.getR8_RW().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}


row = sheet.getRow(8);
/* ===================== R9 ===================== */
cell2 = row.createCell(2);
if (record.getR9_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR9_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR9_MARGINS() != null) {
    cell3.setCellValue(record.getR9_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR9_RW() != null) {
    cell5.setCellValue(record.getR9_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(9);
/* ===================== R10 ===================== */
cell2 = row.createCell(2);
if (record.getR10_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR10_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR10_MARGINS() != null) {
    cell3.setCellValue(record.getR10_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR10_RW() != null) {
    cell5.setCellValue(record.getR10_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(10);
/* ===================== R11 ===================== */
cell2 = row.createCell(2);
if (record.getR11_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR11_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR11_MARGINS() != null) {
    cell3.setCellValue(record.getR11_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR11_RW() != null) {
    cell5.setCellValue(record.getR11_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(11);
/* ===================== R12 ===================== */
cell2 = row.createCell(2);
if (record.getR12_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR12_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR12_MARGINS() != null) {
    cell3.setCellValue(record.getR12_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR12_RW() != null) {
    cell5.setCellValue(record.getR12_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(12);
/* ===================== R13 ===================== */
cell2 = row.createCell(2);
if (record.getR13_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR13_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR13_MARGINS() != null) {
    cell3.setCellValue(record.getR13_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR13_RW() != null) {
    cell5.setCellValue(record.getR13_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(13);
/* ===================== R14 ===================== */
cell2 = row.createCell(2);
if (record.getR14_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR14_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR14_MARGINS() != null) {
    cell3.setCellValue(record.getR14_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR14_RW() != null) {
    cell5.setCellValue(record.getR14_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(14);
/* ===================== R15 ===================== */
cell2 = row.createCell(2);
if (record.getR15_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR15_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR15_MARGINS() != null) {
    cell3.setCellValue(record.getR15_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR15_RW() != null) {
    cell5.setCellValue(record.getR15_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(15);
/* ===================== R16 ===================== */
cell2 = row.createCell(2);
if (record.getR16_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR16_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR16_MARGINS() != null) {
    cell3.setCellValue(record.getR16_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR16_RW() != null) {
    cell5.setCellValue(record.getR16_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(16);
/* ===================== R17 ===================== */
cell2 = row.createCell(2);
if (record.getR17_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR17_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR17_MARGINS() != null) {
    cell3.setCellValue(record.getR17_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR17_RW() != null) {
    cell5.setCellValue(record.getR17_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(17);
/* ===================== R18 ===================== */
cell2 = row.createCell(2);
if (record.getR18_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR18_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR18_MARGINS() != null) {
    cell3.setCellValue(record.getR18_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR18_RW() != null) {
    cell5.setCellValue(record.getR18_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(18);
/* ===================== R19 ===================== */
cell2 = row.createCell(2);
if (record.getR19_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR19_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR19_MARGINS() != null) {
    cell3.setCellValue(record.getR19_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR19_RW() != null) {
    cell5.setCellValue(record.getR19_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(19);
/* ===================== R20 ===================== */
cell2 = row.createCell(2);
if (record.getR20_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR20_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR20_MARGINS() != null) {
    cell3.setCellValue(record.getR20_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR20_RW() != null) {
    cell5.setCellValue(record.getR20_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(20);
/* ===================== R21 ===================== */
cell2 = row.createCell(2);
if (record.getR21_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR21_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR21_MARGINS() != null) {
    cell3.setCellValue(record.getR21_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR21_RW() != null) {
    cell5.setCellValue(record.getR21_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(21);
/* ===================== R22 ===================== */
cell2 = row.createCell(2);
if (record.getR22_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR22_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR22_MARGINS() != null) {
    cell3.setCellValue(record.getR22_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR22_RW() != null) {
    cell5.setCellValue(record.getR22_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(22);
/* ===================== R23 ===================== */
cell2 = row.createCell(2);
if (record.getR23_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR23_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR23_MARGINS() != null) {
    cell3.setCellValue(record.getR23_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR23_RW() != null) {
    cell5.setCellValue(record.getR23_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(23);
/* ===================== R24 ===================== */
cell2 = row.createCell(2);
if (record.getR24_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR24_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR24_MARGINS() != null) {
    cell3.setCellValue(record.getR24_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR24_RW() != null) {
    cell5.setCellValue(record.getR24_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(24);
/* ===================== R25 ===================== */
cell2 = row.createCell(2);
if (record.getR25_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR25_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR25_MARGINS() != null) {
    cell3.setCellValue(record.getR25_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR25_RW() != null) {
    cell5.setCellValue(record.getR25_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(25);
/* ===================== R26 ===================== */
cell2 = row.createCell(2);
if (record.getR26_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR26_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR26_MARGINS() != null) {
    cell3.setCellValue(record.getR26_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR26_RW() != null) {
    cell5.setCellValue(record.getR26_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(26);
/* ===================== R27 ===================== */
cell2 = row.createCell(2);
if (record.getR27_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR27_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR27_MARGINS() != null) {
    cell3.setCellValue(record.getR27_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR27_RW() != null) {
    cell5.setCellValue(record.getR27_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(27);
/* ===================== R28 ===================== */
cell2 = row.createCell(2);
if (record.getR28_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR28_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR28_MARGINS() != null) {
    cell3.setCellValue(record.getR28_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR28_RW() != null) {
    cell5.setCellValue(record.getR28_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(28);
/* ===================== R29 ===================== */
cell2 = row.createCell(2);
if (record.getR29_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR29_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR29_MARGINS() != null) {
    cell3.setCellValue(record.getR29_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR29_RW() != null) {
    cell5.setCellValue(record.getR29_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(29);
/* ===================== R30 ===================== */
cell2 = row.createCell(2);
if (record.getR30_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR30_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR30_MARGINS() != null) {
    cell3.setCellValue(record.getR30_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR30_RW() != null) {
    cell5.setCellValue(record.getR30_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(30);
/* ===================== R31 ===================== */
cell2 = row.createCell(2);
if (record.getR31_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR31_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR31_MARGINS() != null) {
    cell3.setCellValue(record.getR31_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR31_RW() != null) {
    cell5.setCellValue(record.getR31_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(31);
/* ===================== R32 ===================== */
cell2 = row.createCell(2);
if (record.getR32_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR32_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR32_MARGINS() != null) {
    cell3.setCellValue(record.getR32_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR32_RW() != null) {
    cell5.setCellValue(record.getR32_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(32);
/* ===================== R33 ===================== */
cell2 = row.createCell(2);
if (record.getR33_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR33_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR33_MARGINS() != null) {
    cell3.setCellValue(record.getR33_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR33_RW() != null) {
    cell5.setCellValue(record.getR33_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(33);
/* ===================== R34 ===================== */
cell2 = row.createCell(2);
if (record.getR34_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR34_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR34_MARGINS() != null) {
    cell3.setCellValue(record.getR34_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR34_RW() != null) {
    cell5.setCellValue(record.getR34_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(34);
/* ===================== R35 ===================== */
cell2 = row.createCell(2);
if (record.getR35_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR35_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR35_MARGINS() != null) {
    cell3.setCellValue(record.getR35_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR35_RW() != null) {
    cell5.setCellValue(record.getR35_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(35);
/* ===================== R36 ===================== */
cell2 = row.createCell(2);
if (record.getR36_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR36_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR36_MARGINS() != null) {
    cell3.setCellValue(record.getR36_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR36_RW() != null) {
    cell5.setCellValue(record.getR36_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(36);
/* ===================== R37 ===================== */
cell2 = row.createCell(2);
if (record.getR37_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR37_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR37_MARGINS() != null) {
    cell3.setCellValue(record.getR37_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR37_RW() != null) {
    cell5.setCellValue(record.getR37_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(37);
/* ===================== R38 ===================== */
cell2 = row.createCell(2);
if (record.getR38_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR38_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR38_MARGINS() != null) {
    cell3.setCellValue(record.getR38_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR38_RW() != null) {
    cell5.setCellValue(record.getR38_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(38);
/* ===================== R39 ===================== */
cell2 = row.createCell(2);
if (record.getR39_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR39_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR39_MARGINS() != null) {
    cell3.setCellValue(record.getR39_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR39_RW() != null) {
    cell5.setCellValue(record.getR39_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(39);
/* ===================== R40 ===================== */
cell2 = row.createCell(2);
if (record.getR40_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR40_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR40_MARGINS() != null) {
    cell3.setCellValue(record.getR40_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR40_RW() != null) {
    cell5.setCellValue(record.getR40_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(40);
/* ===================== R41 ===================== */
cell2 = row.createCell(2);
if (record.getR41_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR41_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR41_MARGINS() != null) {
    cell3.setCellValue(record.getR41_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR41_RW() != null) {
    cell5.setCellValue(record.getR41_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(41);
/* ===================== R42 ===================== */
cell2 = row.createCell(2);
if (record.getR42_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR42_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR42_MARGINS() != null) {
    cell3.setCellValue(record.getR42_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR42_RW() != null) {
    cell5.setCellValue(record.getR42_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(42);
/* ===================== R43 ===================== */
cell2 = row.createCell(2);
if (record.getR43_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR43_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR43_MARGINS() != null) {
    cell3.setCellValue(record.getR43_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR43_RW() != null) {
    cell5.setCellValue(record.getR43_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(43);
/* ===================== R44 ===================== */
cell2 = row.createCell(2);
if (record.getR44_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR44_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR44_MARGINS() != null) {
    cell3.setCellValue(record.getR44_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR44_RW() != null) {
    cell5.setCellValue(record.getR44_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(44);
/* ===================== R45 ===================== */
cell2 = row.createCell(2);
if (record.getR45_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR45_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR45_MARGINS() != null) {
    cell3.setCellValue(record.getR45_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR45_RW() != null) {
    cell5.setCellValue(record.getR45_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(45);
/* ===================== R46 ===================== */
cell2 = row.createCell(2);
if (record.getR46_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR46_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR46_MARGINS() != null) {
    cell3.setCellValue(record.getR46_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR46_RW() != null) {
    cell5.setCellValue(record.getR46_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}


row = sheet.getRow(47);
/* ===================== R48 ===================== */
cell2 = row.createCell(2);
if (record.getR48_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR48_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR48_MARGINS() != null) {
    cell3.setCellValue(record.getR48_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR48_RW() != null) {
    cell5.setCellValue(record.getR48_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}


row = sheet.getRow(60);
/* ===================== R61 ===================== */
cell2 = row.createCell(2);
if (record.getR61_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR61_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR61_MARGINS() != null) {
    cell3.setCellValue(record.getR61_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR61_RW() != null) {
    cell5.setCellValue(record.getR61_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}


row = sheet.getRow(62);
/* ===================== R63 ===================== */
cell2 = row.createCell(2);
if (record.getR63_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR63_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR63_MARGINS() != null) {
    cell3.setCellValue(record.getR63_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR63_RW() != null) {
    cell5.setCellValue(record.getR63_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(63);
/* ===================== R64 ===================== */
cell2 = row.createCell(2);
if (record.getR64_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR64_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR64_MARGINS() != null) {
    cell3.setCellValue(record.getR64_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR64_RW() != null) {
    cell5.setCellValue(record.getR64_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(64);
/* ===================== R65 ===================== */
cell2 = row.createCell(2);
if (record.getR65_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR65_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR65_MARGINS() != null) {
    cell3.setCellValue(record.getR65_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR65_RW() != null) {
    cell5.setCellValue(record.getR65_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(65);
/* ===================== R66 ===================== */
cell2 = row.createCell(2);
if (record.getR66_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR66_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR66_MARGINS() != null) {
    cell3.setCellValue(record.getR66_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR66_RW() != null) {
    cell5.setCellValue(record.getR66_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(66);
/* ===================== R67 ===================== */
cell2 = row.createCell(2);
if (record.getR67_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR67_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR67_MARGINS() != null) {
    cell3.setCellValue(record.getR67_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR67_RW() != null) {
    cell5.setCellValue(record.getR67_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(67);
/* ===================== R68 ===================== */
cell2 = row.createCell(2);
if (record.getR68_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR68_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR68_MARGINS() != null) {
    cell3.setCellValue(record.getR68_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR68_RW() != null) {
    cell5.setCellValue(record.getR68_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(68);
/* ===================== R69 ===================== */
cell2 = row.createCell(2);
if (record.getR69_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR69_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR69_MARGINS() != null) {
    cell3.setCellValue(record.getR69_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR69_RW() != null) {
    cell5.setCellValue(record.getR69_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(69);
/* ===================== R70 ===================== */
cell2 = row.createCell(2);
if (record.getR70_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR70_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR70_MARGINS() != null) {
    cell3.setCellValue(record.getR70_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR70_RW() != null) {
    cell5.setCellValue(record.getR70_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(70);
/* ===================== R71 ===================== */
cell2 = row.createCell(2);
if (record.getR71_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR71_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR71_MARGINS() != null) {
    cell3.setCellValue(record.getR71_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR71_RW() != null) {
    cell5.setCellValue(record.getR71_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(71);
/* ===================== R72 ===================== */
cell2 = row.createCell(2);
if (record.getR72_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR72_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR72_MARGINS() != null) {
    cell3.setCellValue(record.getR72_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR72_RW() != null) {
    cell5.setCellValue(record.getR72_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(72);
/* ===================== R73 ===================== */
cell2 = row.createCell(2);
if (record.getR73_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR73_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR73_MARGINS() != null) {
    cell3.setCellValue(record.getR73_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR73_RW() != null) {
    cell5.setCellValue(record.getR73_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(73);
/* ===================== R74 ===================== */
cell2 = row.createCell(2);
if (record.getR74_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR74_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR74_MARGINS() != null) {
    cell3.setCellValue(record.getR74_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR74_RW() != null) {
    cell5.setCellValue(record.getR74_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(74);
/* ===================== R75 ===================== */
cell2 = row.createCell(2);
if (record.getR75_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR75_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR75_MARGINS() != null) {
    cell3.setCellValue(record.getR75_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR75_RW() != null) {
    cell5.setCellValue(record.getR75_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(75);
/* ===================== R76 ===================== */
cell2 = row.createCell(2);
if (record.getR76_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR76_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR76_MARGINS() != null) {
    cell3.setCellValue(record.getR76_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR76_RW() != null) {
    cell5.setCellValue(record.getR76_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(76);
/* ===================== R77 ===================== */
cell2 = row.createCell(2);
if (record.getR77_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR77_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR77_MARGINS() != null) {
    cell3.setCellValue(record.getR77_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR77_RW() != null) {
    cell5.setCellValue(record.getR77_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(77);
/* ===================== R78 ===================== */
cell2 = row.createCell(2);
if (record.getR78_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR78_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR78_MARGINS() != null) {
    cell3.setCellValue(record.getR78_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR78_RW() != null) {
    cell5.setCellValue(record.getR78_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(78);
/* ===================== R79 ===================== */
cell2 = row.createCell(2);
if (record.getR79_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR79_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR79_MARGINS() != null) {
    cell3.setCellValue(record.getR79_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR79_RW() != null) {
    cell5.setCellValue(record.getR79_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(79);
/* ===================== R80 ===================== */
cell2 = row.createCell(2);
if (record.getR80_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR80_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR80_MARGINS() != null) {
    cell3.setCellValue(record.getR80_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR80_RW() != null) {
    cell5.setCellValue(record.getR80_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(80);
/* ===================== R81 ===================== */
cell2 = row.createCell(2);
if (record.getR81_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR81_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR81_MARGINS() != null) {
    cell3.setCellValue(record.getR81_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR81_RW() != null) {
    cell5.setCellValue(record.getR81_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(81);
/* ===================== R82 ===================== */
cell2 = row.createCell(2);
if (record.getR82_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR82_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR82_MARGINS() != null) {
    cell3.setCellValue(record.getR82_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR82_RW() != null) {
    cell5.setCellValue(record.getR82_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}


row = sheet.getRow(96);
/* ===================== R97 ===================== */
cell2 = row.createCell(2);
if (record.getR97_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR97_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR97_MARGINS() != null) {
    cell3.setCellValue(record.getR97_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR97_RW() != null) {
    cell5.setCellValue(record.getR97_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(97);
/* ===================== R98 ===================== */
cell2 = row.createCell(2);
if (record.getR98_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR98_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR98_MARGINS() != null) {
    cell3.setCellValue(record.getR98_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR98_RW() != null) {
    cell5.setCellValue(record.getR98_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(98);
/* ===================== R99 ===================== */
cell2 = row.createCell(2);
if (record.getR99_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR99_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR99_MARGINS() != null) {
    cell3.setCellValue(record.getR99_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR99_RW() != null) {
    cell5.setCellValue(record.getR99_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(99);
/* ===================== R100 ===================== */
cell2 = row.createCell(2);
if (record.getR100_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR100_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR100_MARGINS() != null) {
    cell3.setCellValue(record.getR100_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR100_RW() != null) {
    cell5.setCellValue(record.getR100_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(100);
/* ===================== R101 ===================== */
cell2 = row.createCell(2);
if (record.getR101_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR101_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR101_MARGINS() != null) {
    cell3.setCellValue(record.getR101_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR101_RW() != null) {
    cell5.setCellValue(record.getR101_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(101);
/* ===================== R102 ===================== */
cell2 = row.createCell(2);
if (record.getR102_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR102_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR102_MARGINS() != null) {
    cell3.setCellValue(record.getR102_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR102_RW() != null) {
    cell5.setCellValue(record.getR102_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(102);
/* ===================== R103 ===================== */
cell2 = row.createCell(2);
if (record.getR103_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR103_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR103_MARGINS() != null) {
    cell3.setCellValue(record.getR103_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR103_RW() != null) {
    cell5.setCellValue(record.getR103_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(103);
/* ===================== R104 ===================== */
cell2 = row.createCell(2);
if (record.getR104_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR104_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR104_MARGINS() != null) {
    cell3.setCellValue(record.getR104_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR104_RW() != null) {
    cell5.setCellValue(record.getR104_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(104);
/* ===================== R105 ===================== */
cell2 = row.createCell(2);
if (record.getR105_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR105_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR105_MARGINS() != null) {
    cell3.setCellValue(record.getR105_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR105_RW() != null) {
    cell5.setCellValue(record.getR105_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(105);
/* ===================== R106 ===================== */
cell2 = row.createCell(2);
if (record.getR106_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR106_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR106_MARGINS() != null) {
    cell3.setCellValue(record.getR106_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR106_RW() != null) {
    cell5.setCellValue(record.getR106_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(106);
/* ===================== R107 ===================== */
cell2 = row.createCell(2);
if (record.getR107_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR107_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR107_MARGINS() != null) {
    cell3.setCellValue(record.getR107_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR107_RW() != null) {
    cell5.setCellValue(record.getR107_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(107);
/* ===================== R108 ===================== */
cell2 = row.createCell(2);
if (record.getR108_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR108_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR108_MARGINS() != null) {
    cell3.setCellValue(record.getR108_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR108_RW() != null) {
    cell5.setCellValue(record.getR108_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(108);
/* ===================== R109 ===================== */
cell2 = row.createCell(2);
if (record.getR109_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR109_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR109_MARGINS() != null) {
    cell3.setCellValue(record.getR109_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR109_RW() != null) {
    cell5.setCellValue(record.getR109_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(109);
/* ===================== R110 ===================== */
cell2 = row.createCell(2);
if (record.getR110_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR110_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR110_MARGINS() != null) {
    cell3.setCellValue(record.getR110_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR110_RW() != null) {
    cell5.setCellValue(record.getR110_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(110);
/* ===================== R111 ===================== */
cell2 = row.createCell(2);
if (record.getR111_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR111_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR111_MARGINS() != null) {
    cell3.setCellValue(record.getR111_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR111_RW() != null) {
    cell5.setCellValue(record.getR111_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(111);
/* ===================== R112 ===================== */
cell2 = row.createCell(2);
if (record.getR112_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR112_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR112_MARGINS() != null) {
    cell3.setCellValue(record.getR112_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR112_RW() != null) {
    cell5.setCellValue(record.getR112_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(112);
/* ===================== R113 ===================== */
cell2 = row.createCell(2);
if (record.getR113_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR113_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR113_MARGINS() != null) {
    cell3.setCellValue(record.getR113_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR113_RW() != null) {
    cell5.setCellValue(record.getR113_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(113);
/* ===================== R114 ===================== */
cell2 = row.createCell(2);
if (record.getR114_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR114_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR114_MARGINS() != null) {
    cell3.setCellValue(record.getR114_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR114_RW() != null) {
    cell5.setCellValue(record.getR114_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(114);
/* ===================== R115 ===================== */
cell2 = row.createCell(2);
if (record.getR115_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR115_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR115_MARGINS() != null) {
    cell3.setCellValue(record.getR115_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR115_RW() != null) {
    cell5.setCellValue(record.getR115_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(115);
/* ===================== R116 ===================== */
cell2 = row.createCell(2);
if (record.getR116_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR116_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR116_MARGINS() != null) {
    cell3.setCellValue(record.getR116_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR116_RW() != null) {
    cell5.setCellValue(record.getR116_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(116);
/* ===================== R117 ===================== */
cell2 = row.createCell(2);
if (record.getR117_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR117_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR117_MARGINS() != null) {
    cell3.setCellValue(record.getR117_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR117_RW() != null) {
    cell5.setCellValue(record.getR117_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(117);
/* ===================== R118 ===================== */
cell2 = row.createCell(2);
if (record.getR118_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR118_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR118_MARGINS() != null) {
    cell3.setCellValue(record.getR118_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR118_RW() != null) {
    cell5.setCellValue(record.getR118_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(118);
/* ===================== R119 ===================== */
cell2 = row.createCell(2);
if (record.getR119_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR119_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR119_MARGINS() != null) {
    cell3.setCellValue(record.getR119_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR119_RW() != null) {
    cell5.setCellValue(record.getR119_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(119);
/* ===================== R120 ===================== */
cell2 = row.createCell(2);
if (record.getR120_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR120_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR120_MARGINS() != null) {
    cell3.setCellValue(record.getR120_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR120_RW() != null) {
    cell5.setCellValue(record.getR120_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(120);
/* ===================== R121 ===================== */
cell2 = row.createCell(2);
if (record.getR121_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR121_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR121_MARGINS() != null) {
    cell3.setCellValue(record.getR121_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR121_RW() != null) {
    cell5.setCellValue(record.getR121_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(121);
/* ===================== R122 ===================== */
cell2 = row.createCell(2);
if (record.getR122_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR122_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR122_MARGINS() != null) {
    cell3.setCellValue(record.getR122_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR122_RW() != null) {
    cell5.setCellValue(record.getR122_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(122);
/* ===================== R123 ===================== */
cell2 = row.createCell(2);
if (record.getR123_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR123_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR123_MARGINS() != null) {
    cell3.setCellValue(record.getR123_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR123_RW() != null) {
    cell5.setCellValue(record.getR123_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(123);
/* ===================== R124 ===================== */
cell2 = row.createCell(2);
if (record.getR124_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR124_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR124_MARGINS() != null) {
    cell3.setCellValue(record.getR124_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR124_RW() != null) {
    cell5.setCellValue(record.getR124_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(124);
/* ===================== R125 ===================== */
cell2 = row.createCell(2);
if (record.getR125_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR125_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR125_MARGINS() != null) {
    cell3.setCellValue(record.getR125_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR125_RW() != null) {
    cell5.setCellValue(record.getR125_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(125);
/* ===================== R126 ===================== */
cell2 = row.createCell(2);
if (record.getR126_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR126_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR126_MARGINS() != null) {
    cell3.setCellValue(record.getR126_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR126_RW() != null) {
    cell5.setCellValue(record.getR126_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(126);
/* ===================== R127 ===================== */
cell2 = row.createCell(2);
if (record.getR127_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR127_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR127_MARGINS() != null) {
    cell3.setCellValue(record.getR127_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR127_RW() != null) {
    cell5.setCellValue(record.getR127_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(127);
/* ===================== R128 ===================== */
cell2 = row.createCell(2);
if (record.getR128_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR128_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR128_MARGINS() != null) {
    cell3.setCellValue(record.getR128_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR128_RW() != null) {
    cell5.setCellValue(record.getR128_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(128);
/* ===================== R129 ===================== */
cell2 = row.createCell(2);
if (record.getR129_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR129_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR129_MARGINS() != null) {
    cell3.setCellValue(record.getR129_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR129_RW() != null) {
    cell5.setCellValue(record.getR129_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(129);
/* ===================== R130 ===================== */
cell2 = row.createCell(2);
if (record.getR130_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR130_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR130_MARGINS() != null) {
    cell3.setCellValue(record.getR130_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR130_RW() != null) {
    cell5.setCellValue(record.getR130_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
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



				public byte[] getExcelRWAARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {

			}

			List<RWA_Archival_Summary_Entity> dataList = 
					getDataByDateListArchival(dateformat.parse(todate), version);
		
	    

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for RWA new report. Returning empty result.");
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
						RWA_Archival_Summary_Entity record1 = dataList.get(i);
					    
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

		Cell cell2 = row.createCell(2);
				if (record1.getR8_BOOK_VALUE() != null) {
				cell2.setCellValue(record1.getR8_BOOK_VALUE().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				Cell cell3 = row.createCell(3);
				if (record1.getR8_MARGINS() != null) {
				cell3.setCellValue(record1.getR8_MARGINS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				Cell cell5 = row.createCell(3);
				if (record1.getR8_RW() != null) {
				cell5.setCellValue(record1.getR8_RW().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(8);
				/* ===================== R9 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR9_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR9_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR9_MARGINS() != null) {
				    cell3.setCellValue(record1.getR9_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR9_RW() != null) {
				    cell5.setCellValue(record1.getR9_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(9);
				/* ===================== R10 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR10_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR10_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR10_MARGINS() != null) {
				    cell3.setCellValue(record1.getR10_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR10_RW() != null) {
				    cell5.setCellValue(record1.getR10_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(10);
				/* ===================== R11 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR11_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR11_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR11_MARGINS() != null) {
				    cell3.setCellValue(record1.getR11_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR11_RW() != null) {
				    cell5.setCellValue(record1.getR11_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(11);
				/* ===================== R12 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR12_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR12_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR12_MARGINS() != null) {
				    cell3.setCellValue(record1.getR12_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR12_RW() != null) {
				    cell5.setCellValue(record1.getR12_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(12);
				/* ===================== R13 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR13_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR13_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR13_MARGINS() != null) {
				    cell3.setCellValue(record1.getR13_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR13_RW() != null) {
				    cell5.setCellValue(record1.getR13_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(13);
				/* ===================== R14 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR14_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR14_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR14_MARGINS() != null) {
				    cell3.setCellValue(record1.getR14_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR14_RW() != null) {
				    cell5.setCellValue(record1.getR14_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(14);
				/* ===================== R15 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR15_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR15_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR15_MARGINS() != null) {
				    cell3.setCellValue(record1.getR15_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR15_RW() != null) {
				    cell5.setCellValue(record1.getR15_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(15);
				/* ===================== R16 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR16_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR16_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR16_MARGINS() != null) {
				    cell3.setCellValue(record1.getR16_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR16_RW() != null) {
				    cell5.setCellValue(record1.getR16_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(16);
				/* ===================== R17 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR17_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR17_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR17_MARGINS() != null) {
				    cell3.setCellValue(record1.getR17_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR17_RW() != null) {
				    cell5.setCellValue(record1.getR17_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(17);
				/* ===================== R18 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR18_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR18_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR18_MARGINS() != null) {
				    cell3.setCellValue(record1.getR18_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR18_RW() != null) {
				    cell5.setCellValue(record1.getR18_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(18);
				/* ===================== R19 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR19_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR19_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR19_MARGINS() != null) {
				    cell3.setCellValue(record1.getR19_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR19_RW() != null) {
				    cell5.setCellValue(record1.getR19_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(19);
				/* ===================== R20 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR20_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR20_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR20_MARGINS() != null) {
				    cell3.setCellValue(record1.getR20_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR20_RW() != null) {
				    cell5.setCellValue(record1.getR20_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(20);
				/* ===================== R21 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR21_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR21_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR21_MARGINS() != null) {
				    cell3.setCellValue(record1.getR21_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR21_RW() != null) {
				    cell5.setCellValue(record1.getR21_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(21);
				/* ===================== R22 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR22_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR22_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR22_MARGINS() != null) {
				    cell3.setCellValue(record1.getR22_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR22_RW() != null) {
				    cell5.setCellValue(record1.getR22_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(22);
				/* ===================== R23 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR23_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR23_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR23_MARGINS() != null) {
				    cell3.setCellValue(record1.getR23_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR23_RW() != null) {
				    cell5.setCellValue(record1.getR23_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(23);
				/* ===================== R24 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR24_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR24_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR24_MARGINS() != null) {
				    cell3.setCellValue(record1.getR24_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR24_RW() != null) {
				    cell5.setCellValue(record1.getR24_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(24);
				/* ===================== R25 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR25_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR25_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR25_MARGINS() != null) {
				    cell3.setCellValue(record1.getR25_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR25_RW() != null) {
				    cell5.setCellValue(record1.getR25_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(25);
				/* ===================== R26 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR26_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR26_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR26_MARGINS() != null) {
				    cell3.setCellValue(record1.getR26_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR26_RW() != null) {
				    cell5.setCellValue(record1.getR26_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(26);
				/* ===================== R27 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR27_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR27_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR27_MARGINS() != null) {
				    cell3.setCellValue(record1.getR27_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR27_RW() != null) {
				    cell5.setCellValue(record1.getR27_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(27);
				/* ===================== R28 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR28_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR28_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR28_MARGINS() != null) {
				    cell3.setCellValue(record1.getR28_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR28_RW() != null) {
				    cell5.setCellValue(record1.getR28_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(28);
				/* ===================== R29 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR29_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR29_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR29_MARGINS() != null) {
				    cell3.setCellValue(record1.getR29_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR29_RW() != null) {
				    cell5.setCellValue(record1.getR29_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(29);
				/* ===================== R30 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR30_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR30_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR30_MARGINS() != null) {
				    cell3.setCellValue(record1.getR30_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR30_RW() != null) {
				    cell5.setCellValue(record1.getR30_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(30);
				/* ===================== R31 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR31_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR31_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR31_MARGINS() != null) {
				    cell3.setCellValue(record1.getR31_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR31_RW() != null) {
				    cell5.setCellValue(record1.getR31_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(31);
				/* ===================== R32 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR32_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR32_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR32_MARGINS() != null) {
				    cell3.setCellValue(record1.getR32_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR32_RW() != null) {
				    cell5.setCellValue(record1.getR32_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(32);
				/* ===================== R33 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR33_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR33_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR33_MARGINS() != null) {
				    cell3.setCellValue(record1.getR33_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR33_RW() != null) {
				    cell5.setCellValue(record1.getR33_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(33);
				/* ===================== R34 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR34_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR34_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR34_MARGINS() != null) {
				    cell3.setCellValue(record1.getR34_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR34_RW() != null) {
				    cell5.setCellValue(record1.getR34_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(34);
				/* ===================== R35 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR35_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR35_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR35_MARGINS() != null) {
				    cell3.setCellValue(record1.getR35_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR35_RW() != null) {
				    cell5.setCellValue(record1.getR35_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(35);
				/* ===================== R36 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR36_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR36_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR36_MARGINS() != null) {
				    cell3.setCellValue(record1.getR36_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR36_RW() != null) {
				    cell5.setCellValue(record1.getR36_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(36);
				/* ===================== R37 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR37_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR37_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR37_MARGINS() != null) {
				    cell3.setCellValue(record1.getR37_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR37_RW() != null) {
				    cell5.setCellValue(record1.getR37_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(37);
				/* ===================== R38 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR38_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR38_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR38_MARGINS() != null) {
				    cell3.setCellValue(record1.getR38_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR38_RW() != null) {
				    cell5.setCellValue(record1.getR38_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(38);
				/* ===================== R39 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR39_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR39_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR39_MARGINS() != null) {
				    cell3.setCellValue(record1.getR39_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR39_RW() != null) {
				    cell5.setCellValue(record1.getR39_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(39);
				/* ===================== R40 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR40_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR40_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR40_MARGINS() != null) {
				    cell3.setCellValue(record1.getR40_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR40_RW() != null) {
				    cell5.setCellValue(record1.getR40_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(40);
				/* ===================== R41 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR41_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR41_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR41_MARGINS() != null) {
				    cell3.setCellValue(record1.getR41_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR41_RW() != null) {
				    cell5.setCellValue(record1.getR41_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(41);
				/* ===================== R42 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR42_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR42_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR42_MARGINS() != null) {
				    cell3.setCellValue(record1.getR42_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR42_RW() != null) {
				    cell5.setCellValue(record1.getR42_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(42);
				/* ===================== R43 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR43_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR43_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR43_MARGINS() != null) {
				    cell3.setCellValue(record1.getR43_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR43_RW() != null) {
				    cell5.setCellValue(record1.getR43_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(43);
				/* ===================== R44 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR44_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR44_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR44_MARGINS() != null) {
				    cell3.setCellValue(record1.getR44_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR44_RW() != null) {
				    cell5.setCellValue(record1.getR44_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(44);
				/* ===================== R45 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR45_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR45_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR45_MARGINS() != null) {
				    cell3.setCellValue(record1.getR45_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR45_RW() != null) {
				    cell5.setCellValue(record1.getR45_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(45);
				/* ===================== R46 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR46_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR46_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR46_MARGINS() != null) {
				    cell3.setCellValue(record1.getR46_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR46_RW() != null) {
				    cell5.setCellValue(record1.getR46_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}


				row = sheet.getRow(47);
				/* ===================== R48 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR48_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR48_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR48_MARGINS() != null) {
				    cell3.setCellValue(record1.getR48_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR48_RW() != null) {
				    cell5.setCellValue(record1.getR48_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}


				row = sheet.getRow(60);
				/* ===================== R61 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR61_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR61_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR61_MARGINS() != null) {
				    cell3.setCellValue(record1.getR61_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR61_RW() != null) {
				    cell5.setCellValue(record1.getR61_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}


				row = sheet.getRow(62);
				/* ===================== R63 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR63_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR63_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR63_MARGINS() != null) {
				    cell3.setCellValue(record1.getR63_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR63_RW() != null) {
				    cell5.setCellValue(record1.getR63_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(63);
				/* ===================== R64 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR64_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR64_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR64_MARGINS() != null) {
				    cell3.setCellValue(record1.getR64_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR64_RW() != null) {
				    cell5.setCellValue(record1.getR64_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(64);
				/* ===================== R65 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR65_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR65_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR65_MARGINS() != null) {
				    cell3.setCellValue(record1.getR65_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR65_RW() != null) {
				    cell5.setCellValue(record1.getR65_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(65);
				/* ===================== R66 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR66_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR66_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR66_MARGINS() != null) {
				    cell3.setCellValue(record1.getR66_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR66_RW() != null) {
				    cell5.setCellValue(record1.getR66_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(66);
				/* ===================== R67 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR67_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR67_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR67_MARGINS() != null) {
				    cell3.setCellValue(record1.getR67_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR67_RW() != null) {
				    cell5.setCellValue(record1.getR67_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(67);
				/* ===================== R68 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR68_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR68_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR68_MARGINS() != null) {
				    cell3.setCellValue(record1.getR68_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR68_RW() != null) {
				    cell5.setCellValue(record1.getR68_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(68);
				/* ===================== R69 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR69_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR69_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR69_MARGINS() != null) {
				    cell3.setCellValue(record1.getR69_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR69_RW() != null) {
				    cell5.setCellValue(record1.getR69_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(69);
				/* ===================== R70 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR70_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR70_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR70_MARGINS() != null) {
				    cell3.setCellValue(record1.getR70_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR70_RW() != null) {
				    cell5.setCellValue(record1.getR70_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(70);
				/* ===================== R71 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR71_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR71_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR71_MARGINS() != null) {
				    cell3.setCellValue(record1.getR71_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR71_RW() != null) {
				    cell5.setCellValue(record1.getR71_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(71);
				/* ===================== R72 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR72_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR72_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR72_MARGINS() != null) {
				    cell3.setCellValue(record1.getR72_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR72_RW() != null) {
				    cell5.setCellValue(record1.getR72_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(72);
				/* ===================== R73 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR73_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR73_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR73_MARGINS() != null) {
				    cell3.setCellValue(record1.getR73_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR73_RW() != null) {
				    cell5.setCellValue(record1.getR73_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(73);
				/* ===================== R74 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR74_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR74_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR74_MARGINS() != null) {
				    cell3.setCellValue(record1.getR74_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR74_RW() != null) {
				    cell5.setCellValue(record1.getR74_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(74);
				/* ===================== R75 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR75_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR75_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR75_MARGINS() != null) {
				    cell3.setCellValue(record1.getR75_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR75_RW() != null) {
				    cell5.setCellValue(record1.getR75_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(75);
				/* ===================== R76 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR76_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR76_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR76_MARGINS() != null) {
				    cell3.setCellValue(record1.getR76_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR76_RW() != null) {
				    cell5.setCellValue(record1.getR76_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(76);
				/* ===================== R77 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR77_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR77_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR77_MARGINS() != null) {
				    cell3.setCellValue(record1.getR77_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR77_RW() != null) {
				    cell5.setCellValue(record1.getR77_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(77);
				/* ===================== R78 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR78_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR78_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR78_MARGINS() != null) {
				    cell3.setCellValue(record1.getR78_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR78_RW() != null) {
				    cell5.setCellValue(record1.getR78_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(78);
				/* ===================== R79 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR79_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR79_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR79_MARGINS() != null) {
				    cell3.setCellValue(record1.getR79_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR79_RW() != null) {
				    cell5.setCellValue(record1.getR79_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(79);
				/* ===================== R80 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR80_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR80_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR80_MARGINS() != null) {
				    cell3.setCellValue(record1.getR80_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR80_RW() != null) {
				    cell5.setCellValue(record1.getR80_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(80);
				/* ===================== R81 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR81_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR81_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR81_MARGINS() != null) {
				    cell3.setCellValue(record1.getR81_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR81_RW() != null) {
				    cell5.setCellValue(record1.getR81_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(81);
				/* ===================== R82 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR82_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR82_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR82_MARGINS() != null) {
				    cell3.setCellValue(record1.getR82_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR82_RW() != null) {
				    cell5.setCellValue(record1.getR82_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}


				row = sheet.getRow(96);
				/* ===================== R97 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR97_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR97_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR97_MARGINS() != null) {
				    cell3.setCellValue(record1.getR97_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR97_RW() != null) {
				    cell5.setCellValue(record1.getR97_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(97);
				/* ===================== R98 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR98_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR98_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR98_MARGINS() != null) {
				    cell3.setCellValue(record1.getR98_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR98_RW() != null) {
				    cell5.setCellValue(record1.getR98_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(98);
				/* ===================== R99 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR99_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR99_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR99_MARGINS() != null) {
				    cell3.setCellValue(record1.getR99_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR99_RW() != null) {
				    cell5.setCellValue(record1.getR99_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(99);
				/* ===================== R100 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR100_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR100_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR100_MARGINS() != null) {
				    cell3.setCellValue(record1.getR100_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR100_RW() != null) {
				    cell5.setCellValue(record1.getR100_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(100);
				/* ===================== R101 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR101_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR101_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR101_MARGINS() != null) {
				    cell3.setCellValue(record1.getR101_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR101_RW() != null) {
				    cell5.setCellValue(record1.getR101_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(101);
				/* ===================== R102 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR102_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR102_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR102_MARGINS() != null) {
				    cell3.setCellValue(record1.getR102_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR102_RW() != null) {
				    cell5.setCellValue(record1.getR102_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(102);
				/* ===================== R103 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR103_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR103_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR103_MARGINS() != null) {
				    cell3.setCellValue(record1.getR103_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR103_RW() != null) {
				    cell5.setCellValue(record1.getR103_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(103);
				/* ===================== R104 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR104_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR104_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR104_MARGINS() != null) {
				    cell3.setCellValue(record1.getR104_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR104_RW() != null) {
				    cell5.setCellValue(record1.getR104_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(104);
				/* ===================== R105 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR105_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR105_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR105_MARGINS() != null) {
				    cell3.setCellValue(record1.getR105_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR105_RW() != null) {
				    cell5.setCellValue(record1.getR105_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(105);
				/* ===================== R106 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR106_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR106_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR106_MARGINS() != null) {
				    cell3.setCellValue(record1.getR106_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR106_RW() != null) {
				    cell5.setCellValue(record1.getR106_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(106);
				/* ===================== R107 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR107_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR107_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR107_MARGINS() != null) {
				    cell3.setCellValue(record1.getR107_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR107_RW() != null) {
				    cell5.setCellValue(record1.getR107_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(107);
				/* ===================== R108 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR108_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR108_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR108_MARGINS() != null) {
				    cell3.setCellValue(record1.getR108_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR108_RW() != null) {
				    cell5.setCellValue(record1.getR108_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(108);
				/* ===================== R109 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR109_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR109_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR109_MARGINS() != null) {
				    cell3.setCellValue(record1.getR109_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR109_RW() != null) {
				    cell5.setCellValue(record1.getR109_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(109);
				/* ===================== R110 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR110_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR110_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR110_MARGINS() != null) {
				    cell3.setCellValue(record1.getR110_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR110_RW() != null) {
				    cell5.setCellValue(record1.getR110_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(110);
				/* ===================== R111 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR111_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR111_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR111_MARGINS() != null) {
				    cell3.setCellValue(record1.getR111_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR111_RW() != null) {
				    cell5.setCellValue(record1.getR111_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(111);
				/* ===================== R112 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR112_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR112_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR112_MARGINS() != null) {
				    cell3.setCellValue(record1.getR112_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR112_RW() != null) {
				    cell5.setCellValue(record1.getR112_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(112);
				/* ===================== R113 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR113_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR113_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR113_MARGINS() != null) {
				    cell3.setCellValue(record1.getR113_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR113_RW() != null) {
				    cell5.setCellValue(record1.getR113_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(113);
				/* ===================== R114 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR114_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR114_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR114_MARGINS() != null) {
				    cell3.setCellValue(record1.getR114_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR114_RW() != null) {
				    cell5.setCellValue(record1.getR114_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(114);
				/* ===================== R115 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR115_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR115_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR115_MARGINS() != null) {
				    cell3.setCellValue(record1.getR115_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR115_RW() != null) {
				    cell5.setCellValue(record1.getR115_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(115);
				/* ===================== R116 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR116_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR116_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR116_MARGINS() != null) {
				    cell3.setCellValue(record1.getR116_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR116_RW() != null) {
				    cell5.setCellValue(record1.getR116_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(116);
				/* ===================== R117 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR117_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR117_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR117_MARGINS() != null) {
				    cell3.setCellValue(record1.getR117_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR117_RW() != null) {
				    cell5.setCellValue(record1.getR117_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(117);
				/* ===================== R118 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR118_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR118_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR118_MARGINS() != null) {
				    cell3.setCellValue(record1.getR118_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR118_RW() != null) {
				    cell5.setCellValue(record1.getR118_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(118);
				/* ===================== R119 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR119_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR119_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR119_MARGINS() != null) {
				    cell3.setCellValue(record1.getR119_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR119_RW() != null) {
				    cell5.setCellValue(record1.getR119_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(119);
				/* ===================== R120 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR120_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR120_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR120_MARGINS() != null) {
				    cell3.setCellValue(record1.getR120_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR120_RW() != null) {
				    cell5.setCellValue(record1.getR120_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(120);
				/* ===================== R121 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR121_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR121_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR121_MARGINS() != null) {
				    cell3.setCellValue(record1.getR121_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR121_RW() != null) {
				    cell5.setCellValue(record1.getR121_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(121);
				/* ===================== R122 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR122_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR122_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR122_MARGINS() != null) {
				    cell3.setCellValue(record1.getR122_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR122_RW() != null) {
				    cell5.setCellValue(record1.getR122_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(122);
				/* ===================== R123 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR123_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR123_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR123_MARGINS() != null) {
				    cell3.setCellValue(record1.getR123_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR123_RW() != null) {
				    cell5.setCellValue(record1.getR123_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(123);
				/* ===================== R124 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR124_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR124_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR124_MARGINS() != null) {
				    cell3.setCellValue(record1.getR124_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR124_RW() != null) {
				    cell5.setCellValue(record1.getR124_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(124);
				/* ===================== R125 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR125_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR125_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR125_MARGINS() != null) {
				    cell3.setCellValue(record1.getR125_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR125_RW() != null) {
				    cell5.setCellValue(record1.getR125_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(125);
				/* ===================== R126 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR126_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR126_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR126_MARGINS() != null) {
				    cell3.setCellValue(record1.getR126_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR126_RW() != null) {
				    cell5.setCellValue(record1.getR126_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(126);
				/* ===================== R127 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR127_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR127_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR127_MARGINS() != null) {
				    cell3.setCellValue(record1.getR127_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR127_RW() != null) {
				    cell5.setCellValue(record1.getR127_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(127);
				/* ===================== R128 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR128_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR128_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR128_MARGINS() != null) {
				    cell3.setCellValue(record1.getR128_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR128_RW() != null) {
				    cell5.setCellValue(record1.getR128_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(128);
				/* ===================== R129 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR129_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR129_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR129_MARGINS() != null) {
				    cell3.setCellValue(record1.getR129_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR129_RW() != null) {
				    cell5.setCellValue(record1.getR129_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(129);
				/* ===================== R130 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR130_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR130_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR130_MARGINS() != null) {
				    cell3.setCellValue(record1.getR130_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR130_RW() != null) {
				    cell5.setCellValue(record1.getR130_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
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