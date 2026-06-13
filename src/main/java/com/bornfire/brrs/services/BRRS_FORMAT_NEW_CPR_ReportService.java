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

public class BRRS_FORMAT_NEW_CPR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_FORMAT_NEW_CPR_ReportService.class);
	
	
	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	AuditService auditService;

  
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;
	

// =====================================================
// SUMAMRY REPO
// =====================================================


	public List<FORMAT_NEW_CPR_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_FORMAT_NEW_CPR_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new FORMAT_NEW_CPR_Summary_RowMapper()
    );
}
	
// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================



public List<Object[]> getFORMAT_NEW_CPR_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_FORMAT_NEW_CPR_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<FORMAT_NEW_CPR_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_FORMAT_NEW_CPR_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new FORMAT_NEW_CPR_Archival_Summary_RowMapper()
    );
}

public List<FORMAT_NEW_CPR_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_FORMAT_NEW_CPR_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new FORMAT_NEW_CPR_Archival_Summary_RowMapper()
    );
}

// =====================================================
// DETAIL REPO
// =====================================================	


public List<FORMAT_NEW_CPR_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_FORMAT_NEW_CPR_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new FORMAT_NEW_CPR_Detail_RowMapper()
    );
}

public List<FORMAT_NEW_CPR_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_FORMAT_NEW_CPR_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, offset, limit},
            new FORMAT_NEW_CPR_Detail_RowMapper()
    );
}

public int getDataCount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_FORMAT_NEW_CPR_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(sql, new Object[]{reportDate}, Integer.class);
}

public List<FORMAT_NEW_CPR_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel, String reportAddlCriteria1, Date reportDate) {

    String sql = "SELECT * FROM BRRS_FORMAT_NEW_CPR_DETAILTABLE " +
                 "WHERE REPORT_LABEL = ? " +
                 "AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate},
            new FORMAT_NEW_CPR_Detail_RowMapper()
    );
}

public FORMAT_NEW_CPR_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_FORMAT_NEW_CPR_DETAILTABLE WHERE ACCT_NUMBER = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{acctNumber},
            new FORMAT_NEW_CPR_Detail_RowMapper()
    );
}


// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================

public List<FORMAT_NEW_CPR_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate, String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_FORMAT_NEW_CPR_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, dataEntryVersion},
            new FORMAT_NEW_CPR_Archival_Detail_RowMapper()
    );
}


public List<FORMAT_NEW_CPR_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_FORMAT_NEW_CPR_ARCHIVALTABLE_DETAIL " +
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
            new FORMAT_NEW_CPR_Archival_Detail_RowMapper()
    );
}

// =====================================================
// SUMAMRY ENTITY 
// =====================================================


public class FORMAT_NEW_CPR_Summary_RowMapper implements RowMapper<FORMAT_NEW_CPR_Summary_Entity> {

    @Override
    public FORMAT_NEW_CPR_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        FORMAT_NEW_CPR_Summary_Entity obj = new FORMAT_NEW_CPR_Summary_Entity();

// =========================
// R5
// =========================
obj.setR5_LINE_NO(rs.getString("R5_LINE_NO"));
obj.setR5_PARAMETERS(rs.getString("R5_PARAMETERS"));
obj.setR5_AMOUNT(rs.getString("R5_AMOUNT"));

// =========================
// R6
// =========================
obj.setR6_LINE_NO(rs.getString("R6_LINE_NO"));
obj.setR6_PARAMETERS(rs.getString("R6_PARAMETERS"));
obj.setR6_AMOUNT(rs.getString("R6_AMOUNT"));

// =========================
// R7
// =========================
obj.setR7_LINE_NO(rs.getString("R7_LINE_NO"));
obj.setR7_PARAMETERS(rs.getString("R7_PARAMETERS"));
obj.setR7_AMOUNT(rs.getBigDecimal("R7_AMOUNT"));

// =========================
// R8
// =========================
obj.setR8_LINE_NO(rs.getString("R8_LINE_NO"));
obj.setR8_PARAMETERS(rs.getString("R8_PARAMETERS"));
obj.setR8_AMOUNT(rs.getBigDecimal("R8_AMOUNT"));

// =========================
// R9
// =========================
obj.setR9_LINE_NO(rs.getString("R9_LINE_NO"));
obj.setR9_PARAMETERS(rs.getString("R9_PARAMETERS"));
obj.setR9_AMOUNT(rs.getBigDecimal("R9_AMOUNT"));

// =========================
// R10
// =========================
obj.setR10_LINE_NO(rs.getString("R10_LINE_NO"));
obj.setR10_PARAMETERS(rs.getString("R10_PARAMETERS"));
obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));

// =========================
// R11
// =========================
obj.setR11_LINE_NO(rs.getString("R11_LINE_NO"));
obj.setR11_PARAMETERS(rs.getString("R11_PARAMETERS"));
obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));

// =========================
// R12
// =========================
obj.setR12_LINE_NO(rs.getString("R12_LINE_NO"));
obj.setR12_PARAMETERS(rs.getString("R12_PARAMETERS"));
obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));

// =========================
// R13
// =========================
obj.setR13_LINE_NO(rs.getString("R13_LINE_NO"));
obj.setR13_PARAMETERS(rs.getString("R13_PARAMETERS"));
obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));

// =========================
// R14
// =========================
obj.setR14_LINE_NO(rs.getString("R14_LINE_NO"));
obj.setR14_PARAMETERS(rs.getString("R14_PARAMETERS"));
obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));

// =========================
// R15
// =========================
obj.setR15_LINE_NO(rs.getString("R15_LINE_NO"));
obj.setR15_PARAMETERS(rs.getString("R15_PARAMETERS"));
obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));

// =========================
// R16
// =========================
obj.setR16_LINE_NO(rs.getString("R16_LINE_NO"));
obj.setR16_PARAMETERS(rs.getString("R16_PARAMETERS"));
obj.setR16_AMOUNT(rs.getBigDecimal("R16_AMOUNT"));

// =========================
// R17
// =========================
obj.setR17_LINE_NO(rs.getString("R17_LINE_NO"));
obj.setR17_PARAMETERS(rs.getString("R17_PARAMETERS"));
obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));

// =========================
// R18
// =========================
obj.setR18_LINE_NO(rs.getString("R18_LINE_NO"));
obj.setR18_PARAMETERS(rs.getString("R18_PARAMETERS"));
obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));

// =========================
// R19
// =========================
obj.setR19_LINE_NO(rs.getString("R19_LINE_NO"));
obj.setR19_PARAMETERS(rs.getString("R19_PARAMETERS"));
obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));

// =========================
// R20
// =========================
obj.setR20_LINE_NO(rs.getString("R20_LINE_NO"));
obj.setR20_PARAMETERS(rs.getString("R20_PARAMETERS"));
obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));


// =========================
// R21
// =========================
obj.setR21_LINE_NO(rs.getString("R21_LINE_NO"));
obj.setR21_PARAMETERS(rs.getString("R21_PARAMETERS"));
obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));

// =========================
// R22
// =========================
obj.setR22_LINE_NO(rs.getString("R22_LINE_NO"));
obj.setR22_PARAMETERS(rs.getString("R22_PARAMETERS"));
obj.setR22_AMOUNT(rs.getBigDecimal("R22_AMOUNT"));

// =========================
// R23
// =========================
obj.setR23_LINE_NO(rs.getString("R23_LINE_NO"));
obj.setR23_PARAMETERS(rs.getString("R23_PARAMETERS"));
obj.setR23_AMOUNT(rs.getBigDecimal("R23_AMOUNT"));

// =========================
// R24
// =========================
obj.setR24_LINE_NO(rs.getString("R24_LINE_NO"));
obj.setR24_PARAMETERS(rs.getString("R24_PARAMETERS"));
obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));

// =========================
// R25
// =========================
obj.setR25_LINE_NO(rs.getString("R25_LINE_NO"));
obj.setR25_PARAMETERS(rs.getString("R25_PARAMETERS"));
obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));

// =========================
// R26
// =========================
obj.setR26_LINE_NO(rs.getString("R26_LINE_NO"));
obj.setR26_PARAMETERS(rs.getString("R26_PARAMETERS"));
obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));

// =========================
// R27
// =========================
obj.setR27_LINE_NO(rs.getString("R27_LINE_NO"));
obj.setR27_PARAMETERS(rs.getString("R27_PARAMETERS"));
obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));

// =========================
// R28
// =========================
obj.setR28_LINE_NO(rs.getString("R28_LINE_NO"));
obj.setR28_PARAMETERS(rs.getString("R28_PARAMETERS"));
obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));

// =========================
// R29
// =========================
obj.setR29_LINE_NO(rs.getString("R29_LINE_NO"));
obj.setR29_PARAMETERS(rs.getString("R29_PARAMETERS"));
obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));

// =========================
// R30
// =========================
obj.setR30_LINE_NO(rs.getString("R30_LINE_NO"));
obj.setR30_PARAMETERS(rs.getString("R30_PARAMETERS"));
obj.setR30_AMOUNT(rs.getBigDecimal("R30_AMOUNT"));

// =========================
// R31
// =========================
obj.setR31_LINE_NO(rs.getString("R31_LINE_NO"));
obj.setR31_PARAMETERS(rs.getString("R31_PARAMETERS"));
obj.setR31_AMOUNT(rs.getBigDecimal("R31_AMOUNT"));

// =========================
// R32
// =========================
obj.setR32_LINE_NO(rs.getString("R32_LINE_NO"));
obj.setR32_PARAMETERS(rs.getString("R32_PARAMETERS"));
obj.setR32_AMOUNT(rs.getBigDecimal("R32_AMOUNT"));

// =========================
// R33
// =========================
obj.setR33_LINE_NO(rs.getString("R33_LINE_NO"));
obj.setR33_PARAMETERS(rs.getString("R33_PARAMETERS"));
obj.setR33_AMOUNT(rs.getBigDecimal("R33_AMOUNT"));

// =========================
// R34
// =========================
obj.setR34_LINE_NO(rs.getString("R34_LINE_NO"));
obj.setR34_PARAMETERS(rs.getString("R34_PARAMETERS"));
obj.setR34_AMOUNT(rs.getBigDecimal("R34_AMOUNT"));

// =========================
// R35
// =========================
obj.setR35_LINE_NO(rs.getString("R35_LINE_NO"));
obj.setR35_PARAMETERS(rs.getString("R35_PARAMETERS"));
obj.setR35_AMOUNT(rs.getBigDecimal("R35_AMOUNT"));

// =========================
// R36
// =========================
obj.setR36_LINE_NO(rs.getString("R36_LINE_NO"));
obj.setR36_PARAMETERS(rs.getString("R36_PARAMETERS"));
obj.setR36_AMOUNT(rs.getBigDecimal("R36_AMOUNT"));

// =========================
// R37
// =========================
obj.setR37_LINE_NO(rs.getString("R37_LINE_NO"));
obj.setR37_PARAMETERS(rs.getString("R37_PARAMETERS"));
obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));

// =========================
// R38
// =========================
obj.setR38_LINE_NO(rs.getString("R38_LINE_NO"));
obj.setR38_PARAMETERS(rs.getString("R38_PARAMETERS"));
obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));

// =========================
// R39
// =========================
obj.setR39_LINE_NO(rs.getString("R39_LINE_NO"));
obj.setR39_PARAMETERS(rs.getString("R39_PARAMETERS"));
obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));

// =========================
// R40
// =========================
obj.setR40_LINE_NO(rs.getString("R40_LINE_NO"));
obj.setR40_PARAMETERS(rs.getString("R40_PARAMETERS"));
obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));

// =========================
// R41
// =========================
obj.setR41_LINE_NO(rs.getString("R41_LINE_NO"));
obj.setR41_PARAMETERS(rs.getString("R41_PARAMETERS"));
obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));

// =========================
// R42
// =========================
obj.setR42_LINE_NO(rs.getString("R42_LINE_NO"));
obj.setR42_PARAMETERS(rs.getString("R42_PARAMETERS"));
obj.setR42_AMOUNT(rs.getBigDecimal("R42_AMOUNT"));

// =========================
// R43
// =========================
obj.setR43_LINE_NO(rs.getString("R43_LINE_NO"));
obj.setR43_PARAMETERS(rs.getString("R43_PARAMETERS"));
obj.setR43_AMOUNT(rs.getBigDecimal("R43_AMOUNT"));

// =========================
// R44
// =========================
obj.setR44_LINE_NO(rs.getString("R44_LINE_NO"));
obj.setR44_PARAMETERS(rs.getString("R44_PARAMETERS"));
obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));

// =========================
// R45
// =========================
obj.setR45_LINE_NO(rs.getString("R45_LINE_NO"));
obj.setR45_PARAMETERS(rs.getString("R45_PARAMETERS"));
obj.setR45_AMOUNT(rs.getBigDecimal("R45_AMOUNT"));

// =========================
// R46
// =========================
obj.setR46_LINE_NO(rs.getString("R46_LINE_NO"));
obj.setR46_PARAMETERS(rs.getString("R46_PARAMETERS"));
obj.setR46_AMOUNT(rs.getBigDecimal("R46_AMOUNT"));

// =========================
// R47
// =========================
obj.setR47_LINE_NO(rs.getString("R47_LINE_NO"));
obj.setR47_PARAMETERS(rs.getString("R47_PARAMETERS"));
obj.setR47_AMOUNT(rs.getBigDecimal("R47_AMOUNT"));

// =========================
// R48
// =========================
obj.setR48_LINE_NO(rs.getString("R48_LINE_NO"));
obj.setR48_PARAMETERS(rs.getString("R48_PARAMETERS"));
obj.setR48_AMOUNT(rs.getBigDecimal("R48_AMOUNT"));

// =========================
// R49
// =========================
obj.setR49_LINE_NO(rs.getString("R49_LINE_NO"));
obj.setR49_PARAMETERS(rs.getString("R49_PARAMETERS"));
obj.setR49_AMOUNT(rs.getBigDecimal("R49_AMOUNT"));

// =========================
// R50
// =========================
obj.setR50_LINE_NO(rs.getString("R50_LINE_NO"));
obj.setR50_PARAMETERS(rs.getString("R50_PARAMETERS"));
obj.setR50_AMOUNT(rs.getBigDecimal("R50_AMOUNT"));



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


public class FORMAT_NEW_CPR_Summary_Entity {
	
//-------- R5 --------
private String R5_LINE_NO;
private String R5_PARAMETERS;
private String R5_AMOUNT;

// -------- R6 --------
private String R6_LINE_NO;
private String R6_PARAMETERS;
private String R6_AMOUNT;

// -------- R7 --------
private String R7_LINE_NO;
private String R7_PARAMETERS;
private BigDecimal R7_AMOUNT;

// -------- R8 --------
private String R8_LINE_NO;
private String R8_PARAMETERS;
private BigDecimal R8_AMOUNT;

// -------- R9 --------
private String R9_LINE_NO;
private String R9_PARAMETERS;
private BigDecimal R9_AMOUNT;

// -------- R10 --------
private String R10_LINE_NO;
private String R10_PARAMETERS;
private BigDecimal R10_AMOUNT;

// -------- R11 --------
private String R11_LINE_NO;
private String R11_PARAMETERS;
private BigDecimal R11_AMOUNT;

// -------- R12 --------
private String R12_LINE_NO;
private String R12_PARAMETERS;
private BigDecimal R12_AMOUNT;

// -------- R13 --------
private String R13_LINE_NO;
private String R13_PARAMETERS;
private BigDecimal R13_AMOUNT;

// -------- R14 --------
private String R14_LINE_NO;
private String R14_PARAMETERS;
private BigDecimal R14_AMOUNT;

// -------- R15 --------
private String R15_LINE_NO;
private String R15_PARAMETERS;
private BigDecimal R15_AMOUNT;

// -------- R16 --------
private String R16_LINE_NO;
private String R16_PARAMETERS;
private BigDecimal R16_AMOUNT;

// -------- R17 --------
private String R17_LINE_NO;
private String R17_PARAMETERS;
private BigDecimal R17_AMOUNT;

// -------- R18 --------
private String R18_LINE_NO;
private String R18_PARAMETERS;
private BigDecimal R18_AMOUNT;

// -------- R19 --------
private String R19_LINE_NO;
private String R19_PARAMETERS;
private BigDecimal R19_AMOUNT;

// -------- R20 --------
private String R20_LINE_NO;
private String R20_PARAMETERS;
private BigDecimal R20_AMOUNT;

// -------- R21 --------
private String R21_LINE_NO;
private String R21_PARAMETERS;
private BigDecimal R21_AMOUNT;

// -------- R22 --------
private String R22_LINE_NO;
private String R22_PARAMETERS;
private BigDecimal R22_AMOUNT;

// -------- R23 --------
private String R23_LINE_NO;
private String R23_PARAMETERS;
private BigDecimal R23_AMOUNT;

// -------- R24 --------
private String R24_LINE_NO;
private String R24_PARAMETERS;
private BigDecimal R24_AMOUNT;

// -------- R25 --------
private String R25_LINE_NO;
private String R25_PARAMETERS;
private BigDecimal R25_AMOUNT;

// -------- R26 --------
private String R26_LINE_NO;
private String R26_PARAMETERS;
private BigDecimal R26_AMOUNT;

// -------- R27 --------
private String R27_LINE_NO;
private String R27_PARAMETERS;
private BigDecimal R27_AMOUNT;

// -------- R28 --------
private String R28_LINE_NO;
private String R28_PARAMETERS;
private BigDecimal R28_AMOUNT;

// -------- R29 --------
private String R29_LINE_NO;
private String R29_PARAMETERS;
private BigDecimal R29_AMOUNT;

// -------- R30 --------
private String R30_LINE_NO;
private String R30_PARAMETERS;
private BigDecimal R30_AMOUNT;

// -------- R31 --------
private String R31_LINE_NO;
private String R31_PARAMETERS;
private BigDecimal R31_AMOUNT;

// -------- R32 --------
private String R32_LINE_NO;
private String R32_PARAMETERS;
private BigDecimal R32_AMOUNT;

// -------- R33 --------
private String R33_LINE_NO;
private String R33_PARAMETERS;
private BigDecimal R33_AMOUNT;

// -------- R34 --------
private String R34_LINE_NO;
private String R34_PARAMETERS;
private BigDecimal R34_AMOUNT;

// -------- R35 --------
private String R35_LINE_NO;
private String R35_PARAMETERS;
private BigDecimal R35_AMOUNT;

// -------- R36 --------
private String R36_LINE_NO;
private String R36_PARAMETERS;
private BigDecimal R36_AMOUNT;

// -------- R37 --------
private String R37_LINE_NO;
private String R37_PARAMETERS;
private BigDecimal R37_AMOUNT;

// -------- R38 --------
private String R38_LINE_NO;
private String R38_PARAMETERS;
private BigDecimal R38_AMOUNT;

// -------- R39 --------
private String R39_LINE_NO;
private String R39_PARAMETERS;
private BigDecimal R39_AMOUNT;

// -------- R40 --------
private String R40_LINE_NO;
private String R40_PARAMETERS;
private BigDecimal R40_AMOUNT;

// -------- R41 --------
private String R41_LINE_NO;
private String R41_PARAMETERS;
private BigDecimal R41_AMOUNT;

// -------- R42 --------
private String R42_LINE_NO;
private String R42_PARAMETERS;
private BigDecimal R42_AMOUNT;

// -------- R43 --------
private String R43_LINE_NO;
private String R43_PARAMETERS;
private BigDecimal R43_AMOUNT;

// -------- R44 --------
private String R44_LINE_NO;
private String R44_PARAMETERS;
private BigDecimal R44_AMOUNT;

// -------- R45 --------
private String R45_LINE_NO;
private String R45_PARAMETERS;
private BigDecimal R45_AMOUNT;

// -------- R46 --------
private String R46_LINE_NO;
private String R46_PARAMETERS;
private BigDecimal R46_AMOUNT;

// -------- R47 --------
private String R47_LINE_NO;
private String R47_PARAMETERS;
private BigDecimal R47_AMOUNT;

// -------- R48 --------
private String R48_LINE_NO;
private String R48_PARAMETERS;
private BigDecimal R48_AMOUNT;

// -------- R49 --------
private String R49_LINE_NO;
private String R49_PARAMETERS;
private BigDecimal R49_AMOUNT;

// -------- R50 --------
private String R50_LINE_NO;
private String R50_PARAMETERS;
private BigDecimal R50_AMOUNT;

	
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
	
	
public String getR5_LINE_NO() {
	return R5_LINE_NO;
}
public void setR5_LINE_NO(String r5_LINE_NO) {
	R5_LINE_NO = r5_LINE_NO;
}
public String getR5_PARAMETERS() {
	return R5_PARAMETERS;
}
public void setR5_PARAMETERS(String r5_PARAMETERS) {
	R5_PARAMETERS = r5_PARAMETERS;
}
public String getR5_AMOUNT() {
	return R5_AMOUNT;
}
public void setR5_AMOUNT(String r5_AMOUNT) {
	R5_AMOUNT = r5_AMOUNT;
}
public String getR6_LINE_NO() {
	return R6_LINE_NO;
}
public void setR6_LINE_NO(String r6_LINE_NO) {
	R6_LINE_NO = r6_LINE_NO;
}
public String getR6_PARAMETERS() {
	return R6_PARAMETERS;
}
public void setR6_PARAMETERS(String r6_PARAMETERS) {
	R6_PARAMETERS = r6_PARAMETERS;
}
public String getR6_AMOUNT() {
	return R6_AMOUNT;
}
public void setR6_AMOUNT(String r6_AMOUNT) {
	R6_AMOUNT = r6_AMOUNT;
}
public String getR7_LINE_NO() {
	return R7_LINE_NO;
}
public void setR7_LINE_NO(String r7_LINE_NO) {
	R7_LINE_NO = r7_LINE_NO;
}
public String getR7_PARAMETERS() {
	return R7_PARAMETERS;
}
public void setR7_PARAMETERS(String r7_PARAMETERS) {
	R7_PARAMETERS = r7_PARAMETERS;
}
public BigDecimal getR7_AMOUNT() {
	return R7_AMOUNT;
}
public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
	R7_AMOUNT = r7_AMOUNT;
}
public String getR8_LINE_NO() {
	return R8_LINE_NO;
}
public void setR8_LINE_NO(String r8_LINE_NO) {
	R8_LINE_NO = r8_LINE_NO;
}
public String getR8_PARAMETERS() {
	return R8_PARAMETERS;
}
public void setR8_PARAMETERS(String r8_PARAMETERS) {
	R8_PARAMETERS = r8_PARAMETERS;
}
public BigDecimal getR8_AMOUNT() {
	return R8_AMOUNT;
}
public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
	R8_AMOUNT = r8_AMOUNT;
}
public String getR9_LINE_NO() {
	return R9_LINE_NO;
}
public void setR9_LINE_NO(String r9_LINE_NO) {
	R9_LINE_NO = r9_LINE_NO;
}
public String getR9_PARAMETERS() {
	return R9_PARAMETERS;
}
public void setR9_PARAMETERS(String r9_PARAMETERS) {
	R9_PARAMETERS = r9_PARAMETERS;
}
public BigDecimal getR9_AMOUNT() {
	return R9_AMOUNT;
}
public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
	R9_AMOUNT = r9_AMOUNT;
}
public String getR10_LINE_NO() {
	return R10_LINE_NO;
}
public void setR10_LINE_NO(String r10_LINE_NO) {
	R10_LINE_NO = r10_LINE_NO;
}
public String getR10_PARAMETERS() {
	return R10_PARAMETERS;
}
public void setR10_PARAMETERS(String r10_PARAMETERS) {
	R10_PARAMETERS = r10_PARAMETERS;
}
public BigDecimal getR10_AMOUNT() {
	return R10_AMOUNT;
}
public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
	R10_AMOUNT = r10_AMOUNT;
}
public String getR11_LINE_NO() {
	return R11_LINE_NO;
}
public void setR11_LINE_NO(String r11_LINE_NO) {
	R11_LINE_NO = r11_LINE_NO;
}
public String getR11_PARAMETERS() {
	return R11_PARAMETERS;
}
public void setR11_PARAMETERS(String r11_PARAMETERS) {
	R11_PARAMETERS = r11_PARAMETERS;
}
public BigDecimal getR11_AMOUNT() {
	return R11_AMOUNT;
}
public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
	R11_AMOUNT = r11_AMOUNT;
}
public String getR12_LINE_NO() {
	return R12_LINE_NO;
}
public void setR12_LINE_NO(String r12_LINE_NO) {
	R12_LINE_NO = r12_LINE_NO;
}
public String getR12_PARAMETERS() {
	return R12_PARAMETERS;
}
public void setR12_PARAMETERS(String r12_PARAMETERS) {
	R12_PARAMETERS = r12_PARAMETERS;
}
public BigDecimal getR12_AMOUNT() {
	return R12_AMOUNT;
}
public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
	R12_AMOUNT = r12_AMOUNT;
}
public String getR13_LINE_NO() {
	return R13_LINE_NO;
}
public void setR13_LINE_NO(String r13_LINE_NO) {
	R13_LINE_NO = r13_LINE_NO;
}
public String getR13_PARAMETERS() {
	return R13_PARAMETERS;
}
public void setR13_PARAMETERS(String r13_PARAMETERS) {
	R13_PARAMETERS = r13_PARAMETERS;
}
public BigDecimal getR13_AMOUNT() {
	return R13_AMOUNT;
}
public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
	R13_AMOUNT = r13_AMOUNT;
}
public String getR14_LINE_NO() {
	return R14_LINE_NO;
}
public void setR14_LINE_NO(String r14_LINE_NO) {
	R14_LINE_NO = r14_LINE_NO;
}
public String getR14_PARAMETERS() {
	return R14_PARAMETERS;
}
public void setR14_PARAMETERS(String r14_PARAMETERS) {
	R14_PARAMETERS = r14_PARAMETERS;
}
public BigDecimal getR14_AMOUNT() {
	return R14_AMOUNT;
}
public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
	R14_AMOUNT = r14_AMOUNT;
}
public String getR15_LINE_NO() {
	return R15_LINE_NO;
}
public void setR15_LINE_NO(String r15_LINE_NO) {
	R15_LINE_NO = r15_LINE_NO;
}
public String getR15_PARAMETERS() {
	return R15_PARAMETERS;
}
public void setR15_PARAMETERS(String r15_PARAMETERS) {
	R15_PARAMETERS = r15_PARAMETERS;
}
public BigDecimal getR15_AMOUNT() {
	return R15_AMOUNT;
}
public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
	R15_AMOUNT = r15_AMOUNT;
}
public String getR16_LINE_NO() {
	return R16_LINE_NO;
}
public void setR16_LINE_NO(String r16_LINE_NO) {
	R16_LINE_NO = r16_LINE_NO;
}
public String getR16_PARAMETERS() {
	return R16_PARAMETERS;
}
public void setR16_PARAMETERS(String r16_PARAMETERS) {
	R16_PARAMETERS = r16_PARAMETERS;
}
public BigDecimal getR16_AMOUNT() {
	return R16_AMOUNT;
}
public void setR16_AMOUNT(BigDecimal r16_AMOUNT) {
	R16_AMOUNT = r16_AMOUNT;
}
public String getR17_LINE_NO() {
	return R17_LINE_NO;
}
public void setR17_LINE_NO(String r17_LINE_NO) {
	R17_LINE_NO = r17_LINE_NO;
}
public String getR17_PARAMETERS() {
	return R17_PARAMETERS;
}
public void setR17_PARAMETERS(String r17_PARAMETERS) {
	R17_PARAMETERS = r17_PARAMETERS;
}
public BigDecimal getR17_AMOUNT() {
	return R17_AMOUNT;
}
public void setR17_AMOUNT(BigDecimal r17_AMOUNT) {
	R17_AMOUNT = r17_AMOUNT;
}
public String getR18_LINE_NO() {
	return R18_LINE_NO;
}
public void setR18_LINE_NO(String r18_LINE_NO) {
	R18_LINE_NO = r18_LINE_NO;
}
public String getR18_PARAMETERS() {
	return R18_PARAMETERS;
}
public void setR18_PARAMETERS(String r18_PARAMETERS) {
	R18_PARAMETERS = r18_PARAMETERS;
}
public BigDecimal getR18_AMOUNT() {
	return R18_AMOUNT;
}
public void setR18_AMOUNT(BigDecimal r18_AMOUNT) {
	R18_AMOUNT = r18_AMOUNT;
}
public String getR19_LINE_NO() {
	return R19_LINE_NO;
}
public void setR19_LINE_NO(String r19_LINE_NO) {
	R19_LINE_NO = r19_LINE_NO;
}
public String getR19_PARAMETERS() {
	return R19_PARAMETERS;
}
public void setR19_PARAMETERS(String r19_PARAMETERS) {
	R19_PARAMETERS = r19_PARAMETERS;
}
public BigDecimal getR19_AMOUNT() {
	return R19_AMOUNT;
}
public void setR19_AMOUNT(BigDecimal r19_AMOUNT) {
	R19_AMOUNT = r19_AMOUNT;
}
public String getR20_LINE_NO() {
	return R20_LINE_NO;
}
public void setR20_LINE_NO(String r20_LINE_NO) {
	R20_LINE_NO = r20_LINE_NO;
}
public String getR20_PARAMETERS() {
	return R20_PARAMETERS;
}
public void setR20_PARAMETERS(String r20_PARAMETERS) {
	R20_PARAMETERS = r20_PARAMETERS;
}
public BigDecimal getR20_AMOUNT() {
	return R20_AMOUNT;
}
public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
	R20_AMOUNT = r20_AMOUNT;
}
public String getR21_LINE_NO() {
	return R21_LINE_NO;
}
public void setR21_LINE_NO(String r21_LINE_NO) {
	R21_LINE_NO = r21_LINE_NO;
}
public String getR21_PARAMETERS() {
	return R21_PARAMETERS;
}
public void setR21_PARAMETERS(String r21_PARAMETERS) {
	R21_PARAMETERS = r21_PARAMETERS;
}
public BigDecimal getR21_AMOUNT() {
	return R21_AMOUNT;
}
public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
	R21_AMOUNT = r21_AMOUNT;
}
public String getR22_LINE_NO() {
	return R22_LINE_NO;
}
public void setR22_LINE_NO(String r22_LINE_NO) {
	R22_LINE_NO = r22_LINE_NO;
}
public String getR22_PARAMETERS() {
	return R22_PARAMETERS;
}
public void setR22_PARAMETERS(String r22_PARAMETERS) {
	R22_PARAMETERS = r22_PARAMETERS;
}
public BigDecimal getR22_AMOUNT() {
	return R22_AMOUNT;
}
public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
	R22_AMOUNT = r22_AMOUNT;
}
public String getR23_LINE_NO() {
	return R23_LINE_NO;
}
public void setR23_LINE_NO(String r23_LINE_NO) {
	R23_LINE_NO = r23_LINE_NO;
}
public String getR23_PARAMETERS() {
	return R23_PARAMETERS;
}
public void setR23_PARAMETERS(String r23_PARAMETERS) {
	R23_PARAMETERS = r23_PARAMETERS;
}
public BigDecimal getR23_AMOUNT() {
	return R23_AMOUNT;
}
public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
	R23_AMOUNT = r23_AMOUNT;
}
public String getR24_LINE_NO() {
	return R24_LINE_NO;
}
public void setR24_LINE_NO(String r24_LINE_NO) {
	R24_LINE_NO = r24_LINE_NO;
}
public String getR24_PARAMETERS() {
	return R24_PARAMETERS;
}
public void setR24_PARAMETERS(String r24_PARAMETERS) {
	R24_PARAMETERS = r24_PARAMETERS;
}
public BigDecimal getR24_AMOUNT() {
	return R24_AMOUNT;
}
public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
	R24_AMOUNT = r24_AMOUNT;
}
public String getR25_LINE_NO() {
	return R25_LINE_NO;
}
public void setR25_LINE_NO(String r25_LINE_NO) {
	R25_LINE_NO = r25_LINE_NO;
}
public String getR25_PARAMETERS() {
	return R25_PARAMETERS;
}
public void setR25_PARAMETERS(String r25_PARAMETERS) {
	R25_PARAMETERS = r25_PARAMETERS;
}
public BigDecimal getR25_AMOUNT() {
	return R25_AMOUNT;
}
public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
	R25_AMOUNT = r25_AMOUNT;
}
public String getR26_LINE_NO() {
	return R26_LINE_NO;
}
public void setR26_LINE_NO(String r26_LINE_NO) {
	R26_LINE_NO = r26_LINE_NO;
}
public String getR26_PARAMETERS() {
	return R26_PARAMETERS;
}
public void setR26_PARAMETERS(String r26_PARAMETERS) {
	R26_PARAMETERS = r26_PARAMETERS;
}
public BigDecimal getR26_AMOUNT() {
	return R26_AMOUNT;
}
public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
	R26_AMOUNT = r26_AMOUNT;
}
public String getR27_LINE_NO() {
	return R27_LINE_NO;
}
public void setR27_LINE_NO(String r27_LINE_NO) {
	R27_LINE_NO = r27_LINE_NO;
}
public String getR27_PARAMETERS() {
	return R27_PARAMETERS;
}
public void setR27_PARAMETERS(String r27_PARAMETERS) {
	R27_PARAMETERS = r27_PARAMETERS;
}
public BigDecimal getR27_AMOUNT() {
	return R27_AMOUNT;
}
public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
	R27_AMOUNT = r27_AMOUNT;
}
public String getR28_LINE_NO() {
	return R28_LINE_NO;
}
public void setR28_LINE_NO(String r28_LINE_NO) {
	R28_LINE_NO = r28_LINE_NO;
}
public String getR28_PARAMETERS() {
	return R28_PARAMETERS;
}
public void setR28_PARAMETERS(String r28_PARAMETERS) {
	R28_PARAMETERS = r28_PARAMETERS;
}
public BigDecimal getR28_AMOUNT() {
	return R28_AMOUNT;
}
public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
	R28_AMOUNT = r28_AMOUNT;
}
public String getR29_LINE_NO() {
	return R29_LINE_NO;
}
public void setR29_LINE_NO(String r29_LINE_NO) {
	R29_LINE_NO = r29_LINE_NO;
}
public String getR29_PARAMETERS() {
	return R29_PARAMETERS;
}
public void setR29_PARAMETERS(String r29_PARAMETERS) {
	R29_PARAMETERS = r29_PARAMETERS;
}
public BigDecimal getR29_AMOUNT() {
	return R29_AMOUNT;
}
public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
	R29_AMOUNT = r29_AMOUNT;
}
public String getR30_LINE_NO() {
	return R30_LINE_NO;
}
public void setR30_LINE_NO(String r30_LINE_NO) {
	R30_LINE_NO = r30_LINE_NO;
}
public String getR30_PARAMETERS() {
	return R30_PARAMETERS;
}
public void setR30_PARAMETERS(String r30_PARAMETERS) {
	R30_PARAMETERS = r30_PARAMETERS;
}
public BigDecimal getR30_AMOUNT() {
	return R30_AMOUNT;
}
public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
	R30_AMOUNT = r30_AMOUNT;
}
public String getR31_LINE_NO() {
	return R31_LINE_NO;
}
public void setR31_LINE_NO(String r31_LINE_NO) {
	R31_LINE_NO = r31_LINE_NO;
}
public String getR31_PARAMETERS() {
	return R31_PARAMETERS;
}
public void setR31_PARAMETERS(String r31_PARAMETERS) {
	R31_PARAMETERS = r31_PARAMETERS;
}
public BigDecimal getR31_AMOUNT() {
	return R31_AMOUNT;
}
public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
	R31_AMOUNT = r31_AMOUNT;
}
public String getR32_LINE_NO() {
	return R32_LINE_NO;
}
public void setR32_LINE_NO(String r32_LINE_NO) {
	R32_LINE_NO = r32_LINE_NO;
}
public String getR32_PARAMETERS() {
	return R32_PARAMETERS;
}
public void setR32_PARAMETERS(String r32_PARAMETERS) {
	R32_PARAMETERS = r32_PARAMETERS;
}
public BigDecimal getR32_AMOUNT() {
	return R32_AMOUNT;
}
public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
	R32_AMOUNT = r32_AMOUNT;
}
public String getR33_LINE_NO() {
	return R33_LINE_NO;
}
public void setR33_LINE_NO(String r33_LINE_NO) {
	R33_LINE_NO = r33_LINE_NO;
}
public String getR33_PARAMETERS() {
	return R33_PARAMETERS;
}
public void setR33_PARAMETERS(String r33_PARAMETERS) {
	R33_PARAMETERS = r33_PARAMETERS;
}
public BigDecimal getR33_AMOUNT() {
	return R33_AMOUNT;
}
public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
	R33_AMOUNT = r33_AMOUNT;
}
public String getR34_LINE_NO() {
	return R34_LINE_NO;
}
public void setR34_LINE_NO(String r34_LINE_NO) {
	R34_LINE_NO = r34_LINE_NO;
}
public String getR34_PARAMETERS() {
	return R34_PARAMETERS;
}
public void setR34_PARAMETERS(String r34_PARAMETERS) {
	R34_PARAMETERS = r34_PARAMETERS;
}
public BigDecimal getR34_AMOUNT() {
	return R34_AMOUNT;
}
public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
	R34_AMOUNT = r34_AMOUNT;
}
public String getR35_LINE_NO() {
	return R35_LINE_NO;
}
public void setR35_LINE_NO(String r35_LINE_NO) {
	R35_LINE_NO = r35_LINE_NO;
}
public String getR35_PARAMETERS() {
	return R35_PARAMETERS;
}
public void setR35_PARAMETERS(String r35_PARAMETERS) {
	R35_PARAMETERS = r35_PARAMETERS;
}
public BigDecimal getR35_AMOUNT() {
	return R35_AMOUNT;
}
public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
	R35_AMOUNT = r35_AMOUNT;
}
public String getR36_LINE_NO() {
	return R36_LINE_NO;
}
public void setR36_LINE_NO(String r36_LINE_NO) {
	R36_LINE_NO = r36_LINE_NO;
}
public String getR36_PARAMETERS() {
	return R36_PARAMETERS;
}
public void setR36_PARAMETERS(String r36_PARAMETERS) {
	R36_PARAMETERS = r36_PARAMETERS;
}
public BigDecimal getR36_AMOUNT() {
	return R36_AMOUNT;
}
public void setR36_AMOUNT(BigDecimal r36_AMOUNT) {
	R36_AMOUNT = r36_AMOUNT;
}
public String getR37_LINE_NO() {
	return R37_LINE_NO;
}
public void setR37_LINE_NO(String r37_LINE_NO) {
	R37_LINE_NO = r37_LINE_NO;
}
public String getR37_PARAMETERS() {
	return R37_PARAMETERS;
}
public void setR37_PARAMETERS(String r37_PARAMETERS) {
	R37_PARAMETERS = r37_PARAMETERS;
}
public BigDecimal getR37_AMOUNT() {
	return R37_AMOUNT;
}
public void setR37_AMOUNT(BigDecimal r37_AMOUNT) {
	R37_AMOUNT = r37_AMOUNT;
}
public String getR38_LINE_NO() {
	return R38_LINE_NO;
}
public void setR38_LINE_NO(String r38_LINE_NO) {
	R38_LINE_NO = r38_LINE_NO;
}
public String getR38_PARAMETERS() {
	return R38_PARAMETERS;
}
public void setR38_PARAMETERS(String r38_PARAMETERS) {
	R38_PARAMETERS = r38_PARAMETERS;
}
public BigDecimal getR38_AMOUNT() {
	return R38_AMOUNT;
}
public void setR38_AMOUNT(BigDecimal r38_AMOUNT) {
	R38_AMOUNT = r38_AMOUNT;
}
public String getR39_LINE_NO() {
	return R39_LINE_NO;
}
public void setR39_LINE_NO(String r39_LINE_NO) {
	R39_LINE_NO = r39_LINE_NO;
}
public String getR39_PARAMETERS() {
	return R39_PARAMETERS;
}
public void setR39_PARAMETERS(String r39_PARAMETERS) {
	R39_PARAMETERS = r39_PARAMETERS;
}
public BigDecimal getR39_AMOUNT() {
	return R39_AMOUNT;
}
public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
	R39_AMOUNT = r39_AMOUNT;
}
public String getR40_LINE_NO() {
	return R40_LINE_NO;
}
public void setR40_LINE_NO(String r40_LINE_NO) {
	R40_LINE_NO = r40_LINE_NO;
}
public String getR40_PARAMETERS() {
	return R40_PARAMETERS;
}
public void setR40_PARAMETERS(String r40_PARAMETERS) {
	R40_PARAMETERS = r40_PARAMETERS;
}
public BigDecimal getR40_AMOUNT() {
	return R40_AMOUNT;
}
public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
	R40_AMOUNT = r40_AMOUNT;
}
public String getR41_LINE_NO() {
	return R41_LINE_NO;
}
public void setR41_LINE_NO(String r41_LINE_NO) {
	R41_LINE_NO = r41_LINE_NO;
}
public String getR41_PARAMETERS() {
	return R41_PARAMETERS;
}
public void setR41_PARAMETERS(String r41_PARAMETERS) {
	R41_PARAMETERS = r41_PARAMETERS;
}
public BigDecimal getR41_AMOUNT() {
	return R41_AMOUNT;
}
public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
	R41_AMOUNT = r41_AMOUNT;
}
public String getR42_LINE_NO() {
	return R42_LINE_NO;
}
public void setR42_LINE_NO(String r42_LINE_NO) {
	R42_LINE_NO = r42_LINE_NO;
}
public String getR42_PARAMETERS() {
	return R42_PARAMETERS;
}
public void setR42_PARAMETERS(String r42_PARAMETERS) {
	R42_PARAMETERS = r42_PARAMETERS;
}
public BigDecimal getR42_AMOUNT() {
	return R42_AMOUNT;
}
public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
	R42_AMOUNT = r42_AMOUNT;
}
public String getR43_LINE_NO() {
	return R43_LINE_NO;
}
public void setR43_LINE_NO(String r43_LINE_NO) {
	R43_LINE_NO = r43_LINE_NO;
}
public String getR43_PARAMETERS() {
	return R43_PARAMETERS;
}
public void setR43_PARAMETERS(String r43_PARAMETERS) {
	R43_PARAMETERS = r43_PARAMETERS;
}
public BigDecimal getR43_AMOUNT() {
	return R43_AMOUNT;
}
public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
	R43_AMOUNT = r43_AMOUNT;
}
public String getR44_LINE_NO() {
	return R44_LINE_NO;
}
public void setR44_LINE_NO(String r44_LINE_NO) {
	R44_LINE_NO = r44_LINE_NO;
}
public String getR44_PARAMETERS() {
	return R44_PARAMETERS;
}
public void setR44_PARAMETERS(String r44_PARAMETERS) {
	R44_PARAMETERS = r44_PARAMETERS;
}
public BigDecimal getR44_AMOUNT() {
	return R44_AMOUNT;
}
public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
	R44_AMOUNT = r44_AMOUNT;
}
public String getR45_LINE_NO() {
	return R45_LINE_NO;
}
public void setR45_LINE_NO(String r45_LINE_NO) {
	R45_LINE_NO = r45_LINE_NO;
}
public String getR45_PARAMETERS() {
	return R45_PARAMETERS;
}
public void setR45_PARAMETERS(String r45_PARAMETERS) {
	R45_PARAMETERS = r45_PARAMETERS;
}
public BigDecimal getR45_AMOUNT() {
	return R45_AMOUNT;
}
public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
	R45_AMOUNT = r45_AMOUNT;
}
public String getR46_LINE_NO() {
	return R46_LINE_NO;
}
public void setR46_LINE_NO(String r46_LINE_NO) {
	R46_LINE_NO = r46_LINE_NO;
}
public String getR46_PARAMETERS() {
	return R46_PARAMETERS;
}
public void setR46_PARAMETERS(String r46_PARAMETERS) {
	R46_PARAMETERS = r46_PARAMETERS;
}
public BigDecimal getR46_AMOUNT() {
	return R46_AMOUNT;
}
public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
	R46_AMOUNT = r46_AMOUNT;
}
public String getR47_LINE_NO() {
	return R47_LINE_NO;
}
public void setR47_LINE_NO(String r47_LINE_NO) {
	R47_LINE_NO = r47_LINE_NO;
}
public String getR47_PARAMETERS() {
	return R47_PARAMETERS;
}
public void setR47_PARAMETERS(String r47_PARAMETERS) {
	R47_PARAMETERS = r47_PARAMETERS;
}
public BigDecimal getR47_AMOUNT() {
	return R47_AMOUNT;
}
public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
	R47_AMOUNT = r47_AMOUNT;
}
public String getR48_LINE_NO() {
	return R48_LINE_NO;
}
public void setR48_LINE_NO(String r48_LINE_NO) {
	R48_LINE_NO = r48_LINE_NO;
}
public String getR48_PARAMETERS() {
	return R48_PARAMETERS;
}
public void setR48_PARAMETERS(String r48_PARAMETERS) {
	R48_PARAMETERS = r48_PARAMETERS;
}
public BigDecimal getR48_AMOUNT() {
	return R48_AMOUNT;
}
public void setR48_AMOUNT(BigDecimal r48_AMOUNT) {
	R48_AMOUNT = r48_AMOUNT;
}
public String getR49_LINE_NO() {
	return R49_LINE_NO;
}
public void setR49_LINE_NO(String r49_LINE_NO) {
	R49_LINE_NO = r49_LINE_NO;
}
public String getR49_PARAMETERS() {
	return R49_PARAMETERS;
}
public void setR49_PARAMETERS(String r49_PARAMETERS) {
	R49_PARAMETERS = r49_PARAMETERS;
}
public BigDecimal getR49_AMOUNT() {
	return R49_AMOUNT;
}
public void setR49_AMOUNT(BigDecimal r49_AMOUNT) {
	R49_AMOUNT = r49_AMOUNT;
}
public String getR50_LINE_NO() {
	return R50_LINE_NO;
}
public void setR50_LINE_NO(String r50_LINE_NO) {
	R50_LINE_NO = r50_LINE_NO;
}
public String getR50_PARAMETERS() {
	return R50_PARAMETERS;
}
public void setR50_PARAMETERS(String r50_PARAMETERS) {
	R50_PARAMETERS = r50_PARAMETERS;
}
public BigDecimal getR50_AMOUNT() {
	return R50_AMOUNT;
}
public void setR50_AMOUNT(BigDecimal r50_AMOUNT) {
	R50_AMOUNT = r50_AMOUNT;
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


public class FORMAT_NEW_CPR_Archival_Summary_RowMapper
        implements RowMapper<FORMAT_NEW_CPR_Archival_Summary_Entity> {

    @Override
    public FORMAT_NEW_CPR_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        FORMAT_NEW_CPR_Archival_Summary_Entity obj = new FORMAT_NEW_CPR_Archival_Summary_Entity();

// =========================
// R5
// =========================
obj.setR5_LINE_NO(rs.getString("R5_LINE_NO"));
obj.setR5_PARAMETERS(rs.getString("R5_PARAMETERS"));
obj.setR5_AMOUNT(rs.getString("R5_AMOUNT"));

// =========================
// R6
// =========================
obj.setR6_LINE_NO(rs.getString("R6_LINE_NO"));
obj.setR6_PARAMETERS(rs.getString("R6_PARAMETERS"));
obj.setR6_AMOUNT(rs.getString("R6_AMOUNT"));

// =========================
// R7
// =========================
obj.setR7_LINE_NO(rs.getString("R7_LINE_NO"));
obj.setR7_PARAMETERS(rs.getString("R7_PARAMETERS"));
obj.setR7_AMOUNT(rs.getBigDecimal("R7_AMOUNT"));

// =========================
// R8
// =========================
obj.setR8_LINE_NO(rs.getString("R8_LINE_NO"));
obj.setR8_PARAMETERS(rs.getString("R8_PARAMETERS"));
obj.setR8_AMOUNT(rs.getBigDecimal("R8_AMOUNT"));

// =========================
// R9
// =========================
obj.setR9_LINE_NO(rs.getString("R9_LINE_NO"));
obj.setR9_PARAMETERS(rs.getString("R9_PARAMETERS"));
obj.setR9_AMOUNT(rs.getBigDecimal("R9_AMOUNT"));

// =========================
// R10
// =========================
obj.setR10_LINE_NO(rs.getString("R10_LINE_NO"));
obj.setR10_PARAMETERS(rs.getString("R10_PARAMETERS"));
obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));

// =========================
// R11
// =========================
obj.setR11_LINE_NO(rs.getString("R11_LINE_NO"));
obj.setR11_PARAMETERS(rs.getString("R11_PARAMETERS"));
obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));

// =========================
// R12
// =========================
obj.setR12_LINE_NO(rs.getString("R12_LINE_NO"));
obj.setR12_PARAMETERS(rs.getString("R12_PARAMETERS"));
obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));

// =========================
// R13
// =========================
obj.setR13_LINE_NO(rs.getString("R13_LINE_NO"));
obj.setR13_PARAMETERS(rs.getString("R13_PARAMETERS"));
obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));

// =========================
// R14
// =========================
obj.setR14_LINE_NO(rs.getString("R14_LINE_NO"));
obj.setR14_PARAMETERS(rs.getString("R14_PARAMETERS"));
obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));

// =========================
// R15
// =========================
obj.setR15_LINE_NO(rs.getString("R15_LINE_NO"));
obj.setR15_PARAMETERS(rs.getString("R15_PARAMETERS"));
obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));

// =========================
// R16
// =========================
obj.setR16_LINE_NO(rs.getString("R16_LINE_NO"));
obj.setR16_PARAMETERS(rs.getString("R16_PARAMETERS"));
obj.setR16_AMOUNT(rs.getBigDecimal("R16_AMOUNT"));

// =========================
// R17
// =========================
obj.setR17_LINE_NO(rs.getString("R17_LINE_NO"));
obj.setR17_PARAMETERS(rs.getString("R17_PARAMETERS"));
obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));

// =========================
// R18
// =========================
obj.setR18_LINE_NO(rs.getString("R18_LINE_NO"));
obj.setR18_PARAMETERS(rs.getString("R18_PARAMETERS"));
obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));

// =========================
// R19
// =========================
obj.setR19_LINE_NO(rs.getString("R19_LINE_NO"));
obj.setR19_PARAMETERS(rs.getString("R19_PARAMETERS"));
obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));

// =========================
// R20
// =========================
obj.setR20_LINE_NO(rs.getString("R20_LINE_NO"));
obj.setR20_PARAMETERS(rs.getString("R20_PARAMETERS"));
obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));


// =========================
// R21
// =========================
obj.setR21_LINE_NO(rs.getString("R21_LINE_NO"));
obj.setR21_PARAMETERS(rs.getString("R21_PARAMETERS"));
obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));

// =========================
// R22
// =========================
obj.setR22_LINE_NO(rs.getString("R22_LINE_NO"));
obj.setR22_PARAMETERS(rs.getString("R22_PARAMETERS"));
obj.setR22_AMOUNT(rs.getBigDecimal("R22_AMOUNT"));

// =========================
// R23
// =========================
obj.setR23_LINE_NO(rs.getString("R23_LINE_NO"));
obj.setR23_PARAMETERS(rs.getString("R23_PARAMETERS"));
obj.setR23_AMOUNT(rs.getBigDecimal("R23_AMOUNT"));

// =========================
// R24
// =========================
obj.setR24_LINE_NO(rs.getString("R24_LINE_NO"));
obj.setR24_PARAMETERS(rs.getString("R24_PARAMETERS"));
obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));

// =========================
// R25
// =========================
obj.setR25_LINE_NO(rs.getString("R25_LINE_NO"));
obj.setR25_PARAMETERS(rs.getString("R25_PARAMETERS"));
obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));

// =========================
// R26
// =========================
obj.setR26_LINE_NO(rs.getString("R26_LINE_NO"));
obj.setR26_PARAMETERS(rs.getString("R26_PARAMETERS"));
obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));

// =========================
// R27
// =========================
obj.setR27_LINE_NO(rs.getString("R27_LINE_NO"));
obj.setR27_PARAMETERS(rs.getString("R27_PARAMETERS"));
obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));

// =========================
// R28
// =========================
obj.setR28_LINE_NO(rs.getString("R28_LINE_NO"));
obj.setR28_PARAMETERS(rs.getString("R28_PARAMETERS"));
obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));

// =========================
// R29
// =========================
obj.setR29_LINE_NO(rs.getString("R29_LINE_NO"));
obj.setR29_PARAMETERS(rs.getString("R29_PARAMETERS"));
obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));

// =========================
// R30
// =========================
obj.setR30_LINE_NO(rs.getString("R30_LINE_NO"));
obj.setR30_PARAMETERS(rs.getString("R30_PARAMETERS"));
obj.setR30_AMOUNT(rs.getBigDecimal("R30_AMOUNT"));

// =========================
// R31
// =========================
obj.setR31_LINE_NO(rs.getString("R31_LINE_NO"));
obj.setR31_PARAMETERS(rs.getString("R31_PARAMETERS"));
obj.setR31_AMOUNT(rs.getBigDecimal("R31_AMOUNT"));

// =========================
// R32
// =========================
obj.setR32_LINE_NO(rs.getString("R32_LINE_NO"));
obj.setR32_PARAMETERS(rs.getString("R32_PARAMETERS"));
obj.setR32_AMOUNT(rs.getBigDecimal("R32_AMOUNT"));

// =========================
// R33
// =========================
obj.setR33_LINE_NO(rs.getString("R33_LINE_NO"));
obj.setR33_PARAMETERS(rs.getString("R33_PARAMETERS"));
obj.setR33_AMOUNT(rs.getBigDecimal("R33_AMOUNT"));

// =========================
// R34
// =========================
obj.setR34_LINE_NO(rs.getString("R34_LINE_NO"));
obj.setR34_PARAMETERS(rs.getString("R34_PARAMETERS"));
obj.setR34_AMOUNT(rs.getBigDecimal("R34_AMOUNT"));

// =========================
// R35
// =========================
obj.setR35_LINE_NO(rs.getString("R35_LINE_NO"));
obj.setR35_PARAMETERS(rs.getString("R35_PARAMETERS"));
obj.setR35_AMOUNT(rs.getBigDecimal("R35_AMOUNT"));

// =========================
// R36
// =========================
obj.setR36_LINE_NO(rs.getString("R36_LINE_NO"));
obj.setR36_PARAMETERS(rs.getString("R36_PARAMETERS"));
obj.setR36_AMOUNT(rs.getBigDecimal("R36_AMOUNT"));

// =========================
// R37
// =========================
obj.setR37_LINE_NO(rs.getString("R37_LINE_NO"));
obj.setR37_PARAMETERS(rs.getString("R37_PARAMETERS"));
obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));

// =========================
// R38
// =========================
obj.setR38_LINE_NO(rs.getString("R38_LINE_NO"));
obj.setR38_PARAMETERS(rs.getString("R38_PARAMETERS"));
obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));

// =========================
// R39
// =========================
obj.setR39_LINE_NO(rs.getString("R39_LINE_NO"));
obj.setR39_PARAMETERS(rs.getString("R39_PARAMETERS"));
obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));

// =========================
// R40
// =========================
obj.setR40_LINE_NO(rs.getString("R40_LINE_NO"));
obj.setR40_PARAMETERS(rs.getString("R40_PARAMETERS"));
obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));

// =========================
// R41
// =========================
obj.setR41_LINE_NO(rs.getString("R41_LINE_NO"));
obj.setR41_PARAMETERS(rs.getString("R41_PARAMETERS"));
obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));

// =========================
// R42
// =========================
obj.setR42_LINE_NO(rs.getString("R42_LINE_NO"));
obj.setR42_PARAMETERS(rs.getString("R42_PARAMETERS"));
obj.setR42_AMOUNT(rs.getBigDecimal("R42_AMOUNT"));

// =========================
// R43
// =========================
obj.setR43_LINE_NO(rs.getString("R43_LINE_NO"));
obj.setR43_PARAMETERS(rs.getString("R43_PARAMETERS"));
obj.setR43_AMOUNT(rs.getBigDecimal("R43_AMOUNT"));

// =========================
// R44
// =========================
obj.setR44_LINE_NO(rs.getString("R44_LINE_NO"));
obj.setR44_PARAMETERS(rs.getString("R44_PARAMETERS"));
obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));

// =========================
// R45
// =========================
obj.setR45_LINE_NO(rs.getString("R45_LINE_NO"));
obj.setR45_PARAMETERS(rs.getString("R45_PARAMETERS"));
obj.setR45_AMOUNT(rs.getBigDecimal("R45_AMOUNT"));

// =========================
// R46
// =========================
obj.setR46_LINE_NO(rs.getString("R46_LINE_NO"));
obj.setR46_PARAMETERS(rs.getString("R46_PARAMETERS"));
obj.setR46_AMOUNT(rs.getBigDecimal("R46_AMOUNT"));

// =========================
// R47
// =========================
obj.setR47_LINE_NO(rs.getString("R47_LINE_NO"));
obj.setR47_PARAMETERS(rs.getString("R47_PARAMETERS"));
obj.setR47_AMOUNT(rs.getBigDecimal("R47_AMOUNT"));

// =========================
// R48
// =========================
obj.setR48_LINE_NO(rs.getString("R48_LINE_NO"));
obj.setR48_PARAMETERS(rs.getString("R48_PARAMETERS"));
obj.setR48_AMOUNT(rs.getBigDecimal("R48_AMOUNT"));

// =========================
// R49
// =========================
obj.setR49_LINE_NO(rs.getString("R49_LINE_NO"));
obj.setR49_PARAMETERS(rs.getString("R49_PARAMETERS"));
obj.setR49_AMOUNT(rs.getBigDecimal("R49_AMOUNT"));

// =========================
// R50
// =========================
obj.setR50_LINE_NO(rs.getString("R50_LINE_NO"));
obj.setR50_PARAMETERS(rs.getString("R50_PARAMETERS"));
obj.setR50_AMOUNT(rs.getBigDecimal("R50_AMOUNT"));


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


public class FORMAT_NEW_CPR_Archival_Summary_Entity {
	
	

		
	
//-------- R5 --------
private String R5_LINE_NO;
private String R5_PARAMETERS;
private String R5_AMOUNT;

//-------- R6 --------
private String R6_LINE_NO;
private String R6_PARAMETERS;
private String R6_AMOUNT;

//-------- R7 --------
private String R7_LINE_NO;
private String R7_PARAMETERS;
private BigDecimal R7_AMOUNT;

//-------- R8 --------
private String R8_LINE_NO;
private String R8_PARAMETERS;
private BigDecimal R8_AMOUNT;

//-------- R9 --------
private String R9_LINE_NO;
private String R9_PARAMETERS;
private BigDecimal R9_AMOUNT;

//-------- R10 --------
private String R10_LINE_NO;
private String R10_PARAMETERS;
private BigDecimal R10_AMOUNT;

//-------- R11 --------
private String R11_LINE_NO;
private String R11_PARAMETERS;
private BigDecimal R11_AMOUNT;

//-------- R12 --------
private String R12_LINE_NO;
private String R12_PARAMETERS;
private BigDecimal R12_AMOUNT;

//-------- R13 --------
private String R13_LINE_NO;
private String R13_PARAMETERS;
private BigDecimal R13_AMOUNT;

//-------- R14 --------
private String R14_LINE_NO;
private String R14_PARAMETERS;
private BigDecimal R14_AMOUNT;

//-------- R15 --------
private String R15_LINE_NO;
private String R15_PARAMETERS;
private BigDecimal R15_AMOUNT;

//-------- R16 --------
private String R16_LINE_NO;
private String R16_PARAMETERS;
private BigDecimal R16_AMOUNT;

//-------- R17 --------
private String R17_LINE_NO;
private String R17_PARAMETERS;
private BigDecimal R17_AMOUNT;

//-------- R18 --------
private String R18_LINE_NO;
private String R18_PARAMETERS;
private BigDecimal R18_AMOUNT;

//-------- R19 --------
private String R19_LINE_NO;
private String R19_PARAMETERS;
private BigDecimal R19_AMOUNT;

//-------- R20 --------
private String R20_LINE_NO;
private String R20_PARAMETERS;
private BigDecimal R20_AMOUNT;

//-------- R21 --------
private String R21_LINE_NO;
private String R21_PARAMETERS;
private BigDecimal R21_AMOUNT;

//-------- R22 --------
private String R22_LINE_NO;
private String R22_PARAMETERS;
private BigDecimal R22_AMOUNT;

//-------- R23 --------
private String R23_LINE_NO;
private String R23_PARAMETERS;
private BigDecimal R23_AMOUNT;

//-------- R24 --------
private String R24_LINE_NO;
private String R24_PARAMETERS;
private BigDecimal R24_AMOUNT;

//-------- R25 --------
private String R25_LINE_NO;
private String R25_PARAMETERS;
private BigDecimal R25_AMOUNT;

//-------- R26 --------
private String R26_LINE_NO;
private String R26_PARAMETERS;
private BigDecimal R26_AMOUNT;

//-------- R27 --------
private String R27_LINE_NO;
private String R27_PARAMETERS;
private BigDecimal R27_AMOUNT;

//-------- R28 --------
private String R28_LINE_NO;
private String R28_PARAMETERS;
private BigDecimal R28_AMOUNT;

//-------- R29 --------
private String R29_LINE_NO;
private String R29_PARAMETERS;
private BigDecimal R29_AMOUNT;

//-------- R30 --------
private String R30_LINE_NO;
private String R30_PARAMETERS;
private BigDecimal R30_AMOUNT;

//-------- R31 --------
private String R31_LINE_NO;
private String R31_PARAMETERS;
private BigDecimal R31_AMOUNT;

//-------- R32 --------
private String R32_LINE_NO;
private String R32_PARAMETERS;
private BigDecimal R32_AMOUNT;

//-------- R33 --------
private String R33_LINE_NO;
private String R33_PARAMETERS;
private BigDecimal R33_AMOUNT;

//-------- R34 --------
private String R34_LINE_NO;
private String R34_PARAMETERS;
private BigDecimal R34_AMOUNT;

//-------- R35 --------
private String R35_LINE_NO;
private String R35_PARAMETERS;
private BigDecimal R35_AMOUNT;

//-------- R36 --------
private String R36_LINE_NO;
private String R36_PARAMETERS;
private BigDecimal R36_AMOUNT;

//-------- R37 --------
private String R37_LINE_NO;
private String R37_PARAMETERS;
private BigDecimal R37_AMOUNT;

//-------- R38 --------
private String R38_LINE_NO;
private String R38_PARAMETERS;
private BigDecimal R38_AMOUNT;

//-------- R39 --------
private String R39_LINE_NO;
private String R39_PARAMETERS;
private BigDecimal R39_AMOUNT;

//-------- R40 --------
private String R40_LINE_NO;
private String R40_PARAMETERS;
private BigDecimal R40_AMOUNT;

//-------- R41 --------
private String R41_LINE_NO;
private String R41_PARAMETERS;
private BigDecimal R41_AMOUNT;

//-------- R42 --------
private String R42_LINE_NO;
private String R42_PARAMETERS;
private BigDecimal R42_AMOUNT;

//-------- R43 --------
private String R43_LINE_NO;
private String R43_PARAMETERS;
private BigDecimal R43_AMOUNT;

//-------- R44 --------
private String R44_LINE_NO;
private String R44_PARAMETERS;
private BigDecimal R44_AMOUNT;

//-------- R45 --------
private String R45_LINE_NO;
private String R45_PARAMETERS;
private BigDecimal R45_AMOUNT;

//-------- R46 --------
private String R46_LINE_NO;
private String R46_PARAMETERS;
private BigDecimal R46_AMOUNT;

//-------- R47 --------
private String R47_LINE_NO;
private String R47_PARAMETERS;
private BigDecimal R47_AMOUNT;

//-------- R48 --------
private String R48_LINE_NO;
private String R48_PARAMETERS;
private BigDecimal R48_AMOUNT;

//-------- R49 --------
private String R49_LINE_NO;
private String R49_PARAMETERS;
private BigDecimal R49_AMOUNT;

//-------- R50 --------
private String R50_LINE_NO;
private String R50_PARAMETERS;
private BigDecimal R50_AMOUNT;
	               
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
	
	public String getR5_LINE_NO() {
	return R5_LINE_NO;
}
public void setR5_LINE_NO(String r5_LINE_NO) {
	R5_LINE_NO = r5_LINE_NO;
}
public String getR5_PARAMETERS() {
	return R5_PARAMETERS;
}
public void setR5_PARAMETERS(String r5_PARAMETERS) {
	R5_PARAMETERS = r5_PARAMETERS;
}
public String getR5_AMOUNT() {
	return R5_AMOUNT;
}
public void setR5_AMOUNT(String r5_AMOUNT) {
	R5_AMOUNT = r5_AMOUNT;
}
public String getR6_LINE_NO() {
	return R6_LINE_NO;
}
public void setR6_LINE_NO(String r6_LINE_NO) {
	R6_LINE_NO = r6_LINE_NO;
}
public String getR6_PARAMETERS() {
	return R6_PARAMETERS;
}
public void setR6_PARAMETERS(String r6_PARAMETERS) {
	R6_PARAMETERS = r6_PARAMETERS;
}
public String getR6_AMOUNT() {
	return R6_AMOUNT;
}
public void setR6_AMOUNT(String r6_AMOUNT) {
	R6_AMOUNT = r6_AMOUNT;
}
public String getR7_LINE_NO() {
	return R7_LINE_NO;
}
public void setR7_LINE_NO(String r7_LINE_NO) {
	R7_LINE_NO = r7_LINE_NO;
}
public String getR7_PARAMETERS() {
	return R7_PARAMETERS;
}
public void setR7_PARAMETERS(String r7_PARAMETERS) {
	R7_PARAMETERS = r7_PARAMETERS;
}
public BigDecimal getR7_AMOUNT() {
	return R7_AMOUNT;
}
public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
	R7_AMOUNT = r7_AMOUNT;
}
public String getR8_LINE_NO() {
	return R8_LINE_NO;
}
public void setR8_LINE_NO(String r8_LINE_NO) {
	R8_LINE_NO = r8_LINE_NO;
}
public String getR8_PARAMETERS() {
	return R8_PARAMETERS;
}
public void setR8_PARAMETERS(String r8_PARAMETERS) {
	R8_PARAMETERS = r8_PARAMETERS;
}
public BigDecimal getR8_AMOUNT() {
	return R8_AMOUNT;
}
public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
	R8_AMOUNT = r8_AMOUNT;
}
public String getR9_LINE_NO() {
	return R9_LINE_NO;
}
public void setR9_LINE_NO(String r9_LINE_NO) {
	R9_LINE_NO = r9_LINE_NO;
}
public String getR9_PARAMETERS() {
	return R9_PARAMETERS;
}
public void setR9_PARAMETERS(String r9_PARAMETERS) {
	R9_PARAMETERS = r9_PARAMETERS;
}
public BigDecimal getR9_AMOUNT() {
	return R9_AMOUNT;
}
public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
	R9_AMOUNT = r9_AMOUNT;
}
public String getR10_LINE_NO() {
	return R10_LINE_NO;
}
public void setR10_LINE_NO(String r10_LINE_NO) {
	R10_LINE_NO = r10_LINE_NO;
}
public String getR10_PARAMETERS() {
	return R10_PARAMETERS;
}
public void setR10_PARAMETERS(String r10_PARAMETERS) {
	R10_PARAMETERS = r10_PARAMETERS;
}
public BigDecimal getR10_AMOUNT() {
	return R10_AMOUNT;
}
public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
	R10_AMOUNT = r10_AMOUNT;
}
public String getR11_LINE_NO() {
	return R11_LINE_NO;
}
public void setR11_LINE_NO(String r11_LINE_NO) {
	R11_LINE_NO = r11_LINE_NO;
}
public String getR11_PARAMETERS() {
	return R11_PARAMETERS;
}
public void setR11_PARAMETERS(String r11_PARAMETERS) {
	R11_PARAMETERS = r11_PARAMETERS;
}
public BigDecimal getR11_AMOUNT() {
	return R11_AMOUNT;
}
public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
	R11_AMOUNT = r11_AMOUNT;
}
public String getR12_LINE_NO() {
	return R12_LINE_NO;
}
public void setR12_LINE_NO(String r12_LINE_NO) {
	R12_LINE_NO = r12_LINE_NO;
}
public String getR12_PARAMETERS() {
	return R12_PARAMETERS;
}
public void setR12_PARAMETERS(String r12_PARAMETERS) {
	R12_PARAMETERS = r12_PARAMETERS;
}
public BigDecimal getR12_AMOUNT() {
	return R12_AMOUNT;
}
public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
	R12_AMOUNT = r12_AMOUNT;
}
public String getR13_LINE_NO() {
	return R13_LINE_NO;
}
public void setR13_LINE_NO(String r13_LINE_NO) {
	R13_LINE_NO = r13_LINE_NO;
}
public String getR13_PARAMETERS() {
	return R13_PARAMETERS;
}
public void setR13_PARAMETERS(String r13_PARAMETERS) {
	R13_PARAMETERS = r13_PARAMETERS;
}
public BigDecimal getR13_AMOUNT() {
	return R13_AMOUNT;
}
public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
	R13_AMOUNT = r13_AMOUNT;
}
public String getR14_LINE_NO() {
	return R14_LINE_NO;
}
public void setR14_LINE_NO(String r14_LINE_NO) {
	R14_LINE_NO = r14_LINE_NO;
}
public String getR14_PARAMETERS() {
	return R14_PARAMETERS;
}
public void setR14_PARAMETERS(String r14_PARAMETERS) {
	R14_PARAMETERS = r14_PARAMETERS;
}
public BigDecimal getR14_AMOUNT() {
	return R14_AMOUNT;
}
public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
	R14_AMOUNT = r14_AMOUNT;
}
public String getR15_LINE_NO() {
	return R15_LINE_NO;
}
public void setR15_LINE_NO(String r15_LINE_NO) {
	R15_LINE_NO = r15_LINE_NO;
}
public String getR15_PARAMETERS() {
	return R15_PARAMETERS;
}
public void setR15_PARAMETERS(String r15_PARAMETERS) {
	R15_PARAMETERS = r15_PARAMETERS;
}
public BigDecimal getR15_AMOUNT() {
	return R15_AMOUNT;
}
public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
	R15_AMOUNT = r15_AMOUNT;
}
public String getR16_LINE_NO() {
	return R16_LINE_NO;
}
public void setR16_LINE_NO(String r16_LINE_NO) {
	R16_LINE_NO = r16_LINE_NO;
}
public String getR16_PARAMETERS() {
	return R16_PARAMETERS;
}
public void setR16_PARAMETERS(String r16_PARAMETERS) {
	R16_PARAMETERS = r16_PARAMETERS;
}
public BigDecimal getR16_AMOUNT() {
	return R16_AMOUNT;
}
public void setR16_AMOUNT(BigDecimal r16_AMOUNT) {
	R16_AMOUNT = r16_AMOUNT;
}
public String getR17_LINE_NO() {
	return R17_LINE_NO;
}
public void setR17_LINE_NO(String r17_LINE_NO) {
	R17_LINE_NO = r17_LINE_NO;
}
public String getR17_PARAMETERS() {
	return R17_PARAMETERS;
}
public void setR17_PARAMETERS(String r17_PARAMETERS) {
	R17_PARAMETERS = r17_PARAMETERS;
}
public BigDecimal getR17_AMOUNT() {
	return R17_AMOUNT;
}
public void setR17_AMOUNT(BigDecimal r17_AMOUNT) {
	R17_AMOUNT = r17_AMOUNT;
}
public String getR18_LINE_NO() {
	return R18_LINE_NO;
}
public void setR18_LINE_NO(String r18_LINE_NO) {
	R18_LINE_NO = r18_LINE_NO;
}
public String getR18_PARAMETERS() {
	return R18_PARAMETERS;
}
public void setR18_PARAMETERS(String r18_PARAMETERS) {
	R18_PARAMETERS = r18_PARAMETERS;
}
public BigDecimal getR18_AMOUNT() {
	return R18_AMOUNT;
}
public void setR18_AMOUNT(BigDecimal r18_AMOUNT) {
	R18_AMOUNT = r18_AMOUNT;
}
public String getR19_LINE_NO() {
	return R19_LINE_NO;
}
public void setR19_LINE_NO(String r19_LINE_NO) {
	R19_LINE_NO = r19_LINE_NO;
}
public String getR19_PARAMETERS() {
	return R19_PARAMETERS;
}
public void setR19_PARAMETERS(String r19_PARAMETERS) {
	R19_PARAMETERS = r19_PARAMETERS;
}
public BigDecimal getR19_AMOUNT() {
	return R19_AMOUNT;
}
public void setR19_AMOUNT(BigDecimal r19_AMOUNT) {
	R19_AMOUNT = r19_AMOUNT;
}
public String getR20_LINE_NO() {
	return R20_LINE_NO;
}
public void setR20_LINE_NO(String r20_LINE_NO) {
	R20_LINE_NO = r20_LINE_NO;
}
public String getR20_PARAMETERS() {
	return R20_PARAMETERS;
}
public void setR20_PARAMETERS(String r20_PARAMETERS) {
	R20_PARAMETERS = r20_PARAMETERS;
}
public BigDecimal getR20_AMOUNT() {
	return R20_AMOUNT;
}
public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
	R20_AMOUNT = r20_AMOUNT;
}
public String getR21_LINE_NO() {
	return R21_LINE_NO;
}
public void setR21_LINE_NO(String r21_LINE_NO) {
	R21_LINE_NO = r21_LINE_NO;
}
public String getR21_PARAMETERS() {
	return R21_PARAMETERS;
}
public void setR21_PARAMETERS(String r21_PARAMETERS) {
	R21_PARAMETERS = r21_PARAMETERS;
}
public BigDecimal getR21_AMOUNT() {
	return R21_AMOUNT;
}
public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
	R21_AMOUNT = r21_AMOUNT;
}
public String getR22_LINE_NO() {
	return R22_LINE_NO;
}
public void setR22_LINE_NO(String r22_LINE_NO) {
	R22_LINE_NO = r22_LINE_NO;
}
public String getR22_PARAMETERS() {
	return R22_PARAMETERS;
}
public void setR22_PARAMETERS(String r22_PARAMETERS) {
	R22_PARAMETERS = r22_PARAMETERS;
}
public BigDecimal getR22_AMOUNT() {
	return R22_AMOUNT;
}
public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
	R22_AMOUNT = r22_AMOUNT;
}
public String getR23_LINE_NO() {
	return R23_LINE_NO;
}
public void setR23_LINE_NO(String r23_LINE_NO) {
	R23_LINE_NO = r23_LINE_NO;
}
public String getR23_PARAMETERS() {
	return R23_PARAMETERS;
}
public void setR23_PARAMETERS(String r23_PARAMETERS) {
	R23_PARAMETERS = r23_PARAMETERS;
}
public BigDecimal getR23_AMOUNT() {
	return R23_AMOUNT;
}
public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
	R23_AMOUNT = r23_AMOUNT;
}
public String getR24_LINE_NO() {
	return R24_LINE_NO;
}
public void setR24_LINE_NO(String r24_LINE_NO) {
	R24_LINE_NO = r24_LINE_NO;
}
public String getR24_PARAMETERS() {
	return R24_PARAMETERS;
}
public void setR24_PARAMETERS(String r24_PARAMETERS) {
	R24_PARAMETERS = r24_PARAMETERS;
}
public BigDecimal getR24_AMOUNT() {
	return R24_AMOUNT;
}
public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
	R24_AMOUNT = r24_AMOUNT;
}
public String getR25_LINE_NO() {
	return R25_LINE_NO;
}
public void setR25_LINE_NO(String r25_LINE_NO) {
	R25_LINE_NO = r25_LINE_NO;
}
public String getR25_PARAMETERS() {
	return R25_PARAMETERS;
}
public void setR25_PARAMETERS(String r25_PARAMETERS) {
	R25_PARAMETERS = r25_PARAMETERS;
}
public BigDecimal getR25_AMOUNT() {
	return R25_AMOUNT;
}
public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
	R25_AMOUNT = r25_AMOUNT;
}
public String getR26_LINE_NO() {
	return R26_LINE_NO;
}
public void setR26_LINE_NO(String r26_LINE_NO) {
	R26_LINE_NO = r26_LINE_NO;
}
public String getR26_PARAMETERS() {
	return R26_PARAMETERS;
}
public void setR26_PARAMETERS(String r26_PARAMETERS) {
	R26_PARAMETERS = r26_PARAMETERS;
}
public BigDecimal getR26_AMOUNT() {
	return R26_AMOUNT;
}
public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
	R26_AMOUNT = r26_AMOUNT;
}
public String getR27_LINE_NO() {
	return R27_LINE_NO;
}
public void setR27_LINE_NO(String r27_LINE_NO) {
	R27_LINE_NO = r27_LINE_NO;
}
public String getR27_PARAMETERS() {
	return R27_PARAMETERS;
}
public void setR27_PARAMETERS(String r27_PARAMETERS) {
	R27_PARAMETERS = r27_PARAMETERS;
}
public BigDecimal getR27_AMOUNT() {
	return R27_AMOUNT;
}
public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
	R27_AMOUNT = r27_AMOUNT;
}
public String getR28_LINE_NO() {
	return R28_LINE_NO;
}
public void setR28_LINE_NO(String r28_LINE_NO) {
	R28_LINE_NO = r28_LINE_NO;
}
public String getR28_PARAMETERS() {
	return R28_PARAMETERS;
}
public void setR28_PARAMETERS(String r28_PARAMETERS) {
	R28_PARAMETERS = r28_PARAMETERS;
}
public BigDecimal getR28_AMOUNT() {
	return R28_AMOUNT;
}
public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
	R28_AMOUNT = r28_AMOUNT;
}
public String getR29_LINE_NO() {
	return R29_LINE_NO;
}
public void setR29_LINE_NO(String r29_LINE_NO) {
	R29_LINE_NO = r29_LINE_NO;
}
public String getR29_PARAMETERS() {
	return R29_PARAMETERS;
}
public void setR29_PARAMETERS(String r29_PARAMETERS) {
	R29_PARAMETERS = r29_PARAMETERS;
}
public BigDecimal getR29_AMOUNT() {
	return R29_AMOUNT;
}
public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
	R29_AMOUNT = r29_AMOUNT;
}
public String getR30_LINE_NO() {
	return R30_LINE_NO;
}
public void setR30_LINE_NO(String r30_LINE_NO) {
	R30_LINE_NO = r30_LINE_NO;
}
public String getR30_PARAMETERS() {
	return R30_PARAMETERS;
}
public void setR30_PARAMETERS(String r30_PARAMETERS) {
	R30_PARAMETERS = r30_PARAMETERS;
}
public BigDecimal getR30_AMOUNT() {
	return R30_AMOUNT;
}
public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
	R30_AMOUNT = r30_AMOUNT;
}
public String getR31_LINE_NO() {
	return R31_LINE_NO;
}
public void setR31_LINE_NO(String r31_LINE_NO) {
	R31_LINE_NO = r31_LINE_NO;
}
public String getR31_PARAMETERS() {
	return R31_PARAMETERS;
}
public void setR31_PARAMETERS(String r31_PARAMETERS) {
	R31_PARAMETERS = r31_PARAMETERS;
}
public BigDecimal getR31_AMOUNT() {
	return R31_AMOUNT;
}
public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
	R31_AMOUNT = r31_AMOUNT;
}
public String getR32_LINE_NO() {
	return R32_LINE_NO;
}
public void setR32_LINE_NO(String r32_LINE_NO) {
	R32_LINE_NO = r32_LINE_NO;
}
public String getR32_PARAMETERS() {
	return R32_PARAMETERS;
}
public void setR32_PARAMETERS(String r32_PARAMETERS) {
	R32_PARAMETERS = r32_PARAMETERS;
}
public BigDecimal getR32_AMOUNT() {
	return R32_AMOUNT;
}
public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
	R32_AMOUNT = r32_AMOUNT;
}
public String getR33_LINE_NO() {
	return R33_LINE_NO;
}
public void setR33_LINE_NO(String r33_LINE_NO) {
	R33_LINE_NO = r33_LINE_NO;
}
public String getR33_PARAMETERS() {
	return R33_PARAMETERS;
}
public void setR33_PARAMETERS(String r33_PARAMETERS) {
	R33_PARAMETERS = r33_PARAMETERS;
}
public BigDecimal getR33_AMOUNT() {
	return R33_AMOUNT;
}
public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
	R33_AMOUNT = r33_AMOUNT;
}
public String getR34_LINE_NO() {
	return R34_LINE_NO;
}
public void setR34_LINE_NO(String r34_LINE_NO) {
	R34_LINE_NO = r34_LINE_NO;
}
public String getR34_PARAMETERS() {
	return R34_PARAMETERS;
}
public void setR34_PARAMETERS(String r34_PARAMETERS) {
	R34_PARAMETERS = r34_PARAMETERS;
}
public BigDecimal getR34_AMOUNT() {
	return R34_AMOUNT;
}
public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
	R34_AMOUNT = r34_AMOUNT;
}
public String getR35_LINE_NO() {
	return R35_LINE_NO;
}
public void setR35_LINE_NO(String r35_LINE_NO) {
	R35_LINE_NO = r35_LINE_NO;
}
public String getR35_PARAMETERS() {
	return R35_PARAMETERS;
}
public void setR35_PARAMETERS(String r35_PARAMETERS) {
	R35_PARAMETERS = r35_PARAMETERS;
}
public BigDecimal getR35_AMOUNT() {
	return R35_AMOUNT;
}
public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
	R35_AMOUNT = r35_AMOUNT;
}
public String getR36_LINE_NO() {
	return R36_LINE_NO;
}
public void setR36_LINE_NO(String r36_LINE_NO) {
	R36_LINE_NO = r36_LINE_NO;
}
public String getR36_PARAMETERS() {
	return R36_PARAMETERS;
}
public void setR36_PARAMETERS(String r36_PARAMETERS) {
	R36_PARAMETERS = r36_PARAMETERS;
}
public BigDecimal getR36_AMOUNT() {
	return R36_AMOUNT;
}
public void setR36_AMOUNT(BigDecimal r36_AMOUNT) {
	R36_AMOUNT = r36_AMOUNT;
}
public String getR37_LINE_NO() {
	return R37_LINE_NO;
}
public void setR37_LINE_NO(String r37_LINE_NO) {
	R37_LINE_NO = r37_LINE_NO;
}
public String getR37_PARAMETERS() {
	return R37_PARAMETERS;
}
public void setR37_PARAMETERS(String r37_PARAMETERS) {
	R37_PARAMETERS = r37_PARAMETERS;
}
public BigDecimal getR37_AMOUNT() {
	return R37_AMOUNT;
}
public void setR37_AMOUNT(BigDecimal r37_AMOUNT) {
	R37_AMOUNT = r37_AMOUNT;
}
public String getR38_LINE_NO() {
	return R38_LINE_NO;
}
public void setR38_LINE_NO(String r38_LINE_NO) {
	R38_LINE_NO = r38_LINE_NO;
}
public String getR38_PARAMETERS() {
	return R38_PARAMETERS;
}
public void setR38_PARAMETERS(String r38_PARAMETERS) {
	R38_PARAMETERS = r38_PARAMETERS;
}
public BigDecimal getR38_AMOUNT() {
	return R38_AMOUNT;
}
public void setR38_AMOUNT(BigDecimal r38_AMOUNT) {
	R38_AMOUNT = r38_AMOUNT;
}
public String getR39_LINE_NO() {
	return R39_LINE_NO;
}
public void setR39_LINE_NO(String r39_LINE_NO) {
	R39_LINE_NO = r39_LINE_NO;
}
public String getR39_PARAMETERS() {
	return R39_PARAMETERS;
}
public void setR39_PARAMETERS(String r39_PARAMETERS) {
	R39_PARAMETERS = r39_PARAMETERS;
}
public BigDecimal getR39_AMOUNT() {
	return R39_AMOUNT;
}
public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
	R39_AMOUNT = r39_AMOUNT;
}
public String getR40_LINE_NO() {
	return R40_LINE_NO;
}
public void setR40_LINE_NO(String r40_LINE_NO) {
	R40_LINE_NO = r40_LINE_NO;
}
public String getR40_PARAMETERS() {
	return R40_PARAMETERS;
}
public void setR40_PARAMETERS(String r40_PARAMETERS) {
	R40_PARAMETERS = r40_PARAMETERS;
}
public BigDecimal getR40_AMOUNT() {
	return R40_AMOUNT;
}
public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
	R40_AMOUNT = r40_AMOUNT;
}
public String getR41_LINE_NO() {
	return R41_LINE_NO;
}
public void setR41_LINE_NO(String r41_LINE_NO) {
	R41_LINE_NO = r41_LINE_NO;
}
public String getR41_PARAMETERS() {
	return R41_PARAMETERS;
}
public void setR41_PARAMETERS(String r41_PARAMETERS) {
	R41_PARAMETERS = r41_PARAMETERS;
}
public BigDecimal getR41_AMOUNT() {
	return R41_AMOUNT;
}
public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
	R41_AMOUNT = r41_AMOUNT;
}
public String getR42_LINE_NO() {
	return R42_LINE_NO;
}
public void setR42_LINE_NO(String r42_LINE_NO) {
	R42_LINE_NO = r42_LINE_NO;
}
public String getR42_PARAMETERS() {
	return R42_PARAMETERS;
}
public void setR42_PARAMETERS(String r42_PARAMETERS) {
	R42_PARAMETERS = r42_PARAMETERS;
}
public BigDecimal getR42_AMOUNT() {
	return R42_AMOUNT;
}
public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
	R42_AMOUNT = r42_AMOUNT;
}
public String getR43_LINE_NO() {
	return R43_LINE_NO;
}
public void setR43_LINE_NO(String r43_LINE_NO) {
	R43_LINE_NO = r43_LINE_NO;
}
public String getR43_PARAMETERS() {
	return R43_PARAMETERS;
}
public void setR43_PARAMETERS(String r43_PARAMETERS) {
	R43_PARAMETERS = r43_PARAMETERS;
}
public BigDecimal getR43_AMOUNT() {
	return R43_AMOUNT;
}
public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
	R43_AMOUNT = r43_AMOUNT;
}
public String getR44_LINE_NO() {
	return R44_LINE_NO;
}
public void setR44_LINE_NO(String r44_LINE_NO) {
	R44_LINE_NO = r44_LINE_NO;
}
public String getR44_PARAMETERS() {
	return R44_PARAMETERS;
}
public void setR44_PARAMETERS(String r44_PARAMETERS) {
	R44_PARAMETERS = r44_PARAMETERS;
}
public BigDecimal getR44_AMOUNT() {
	return R44_AMOUNT;
}
public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
	R44_AMOUNT = r44_AMOUNT;
}
public String getR45_LINE_NO() {
	return R45_LINE_NO;
}
public void setR45_LINE_NO(String r45_LINE_NO) {
	R45_LINE_NO = r45_LINE_NO;
}
public String getR45_PARAMETERS() {
	return R45_PARAMETERS;
}
public void setR45_PARAMETERS(String r45_PARAMETERS) {
	R45_PARAMETERS = r45_PARAMETERS;
}
public BigDecimal getR45_AMOUNT() {
	return R45_AMOUNT;
}
public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
	R45_AMOUNT = r45_AMOUNT;
}
public String getR46_LINE_NO() {
	return R46_LINE_NO;
}
public void setR46_LINE_NO(String r46_LINE_NO) {
	R46_LINE_NO = r46_LINE_NO;
}
public String getR46_PARAMETERS() {
	return R46_PARAMETERS;
}
public void setR46_PARAMETERS(String r46_PARAMETERS) {
	R46_PARAMETERS = r46_PARAMETERS;
}
public BigDecimal getR46_AMOUNT() {
	return R46_AMOUNT;
}
public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
	R46_AMOUNT = r46_AMOUNT;
}
public String getR47_LINE_NO() {
	return R47_LINE_NO;
}
public void setR47_LINE_NO(String r47_LINE_NO) {
	R47_LINE_NO = r47_LINE_NO;
}
public String getR47_PARAMETERS() {
	return R47_PARAMETERS;
}
public void setR47_PARAMETERS(String r47_PARAMETERS) {
	R47_PARAMETERS = r47_PARAMETERS;
}
public BigDecimal getR47_AMOUNT() {
	return R47_AMOUNT;
}
public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
	R47_AMOUNT = r47_AMOUNT;
}
public String getR48_LINE_NO() {
	return R48_LINE_NO;
}
public void setR48_LINE_NO(String r48_LINE_NO) {
	R48_LINE_NO = r48_LINE_NO;
}
public String getR48_PARAMETERS() {
	return R48_PARAMETERS;
}
public void setR48_PARAMETERS(String r48_PARAMETERS) {
	R48_PARAMETERS = r48_PARAMETERS;
}
public BigDecimal getR48_AMOUNT() {
	return R48_AMOUNT;
}
public void setR48_AMOUNT(BigDecimal r48_AMOUNT) {
	R48_AMOUNT = r48_AMOUNT;
}
public String getR49_LINE_NO() {
	return R49_LINE_NO;
}
public void setR49_LINE_NO(String r49_LINE_NO) {
	R49_LINE_NO = r49_LINE_NO;
}
public String getR49_PARAMETERS() {
	return R49_PARAMETERS;
}
public void setR49_PARAMETERS(String r49_PARAMETERS) {
	R49_PARAMETERS = r49_PARAMETERS;
}
public BigDecimal getR49_AMOUNT() {
	return R49_AMOUNT;
}
public void setR49_AMOUNT(BigDecimal r49_AMOUNT) {
	R49_AMOUNT = r49_AMOUNT;
}
public String getR50_LINE_NO() {
	return R50_LINE_NO;
}
public void setR50_LINE_NO(String r50_LINE_NO) {
	R50_LINE_NO = r50_LINE_NO;
}
public String getR50_PARAMETERS() {
	return R50_PARAMETERS;
}
public void setR50_PARAMETERS(String r50_PARAMETERS) {
	R50_PARAMETERS = r50_PARAMETERS;
}
public BigDecimal getR50_AMOUNT() {
	return R50_AMOUNT;
}
public void setR50_AMOUNT(BigDecimal r50_AMOUNT) {
	R50_AMOUNT = r50_AMOUNT;
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
// DETAIL ENTITY  FORMAT_NEW_CPR
// =====================================================	

public class FORMAT_NEW_CPR_Detail_RowMapper implements RowMapper<FORMAT_NEW_CPR_Detail_Entity> {

    @Override
    public FORMAT_NEW_CPR_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        FORMAT_NEW_CPR_Detail_Entity obj = new FORMAT_NEW_CPR_Detail_Entity();

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

public class FORMAT_NEW_CPR_Detail_Entity {

   
	
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


public class FORMAT_NEW_CPR_Archival_Detail_RowMapper 
        implements RowMapper<FORMAT_NEW_CPR_Archival_Detail_Entity> {

    @Override
    public FORMAT_NEW_CPR_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        FORMAT_NEW_CPR_Archival_Detail_Entity obj = new FORMAT_NEW_CPR_Archival_Detail_Entity();

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

public class FORMAT_NEW_CPR_Archival_Detail_Entity {

   
	
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
// MODEL AND VIEW METHOD summary FORMAT_NEW_CPR
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
		 		 	 public ModelAndView getFORMAT_NEW_CPRView(

	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("FORMAT_NEW_CPR View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    // =====================================================
	    // ARCHIVAL MODE
	    // =====================================================

	    if ("ARCHIVAL".equals(type) && version != null) {

	        List<FORMAT_NEW_CPR_Archival_Summary_Entity> T1Master = new ArrayList<>();
	    

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

	        List<FORMAT_NEW_CPR_Summary_Entity> T1Master = new ArrayList<>();
	       

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

	    mv.setViewName("BRRS/FORMAT_NEW_CPR");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getFORMAT_NEW_CPRcurrentDtl(
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

	            List<FORMAT_NEW_CPR_Archival_Detail_Entity> archivalDetailList;

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

	            List<FORMAT_NEW_CPR_Detail_Entity> currentDetailList;

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

	    mv.setViewName("BRRS/FORMAT_NEW_CPR");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
	
//=====================================================
// MODEL AND VIEW METHOD ARCHIVAL VIEW
//=====================================================

public List<Object[]> getFORMAT_NEW_CPRArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<FORMAT_NEW_CPR_Archival_Summary_Entity> repoData =
						getarchivaldatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (FORMAT_NEW_CPR_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					FORMAT_NEW_CPR_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  FORMAT_NEW_CPR  Archival data: " + e.getMessage());
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
		ModelAndView mv = new ModelAndView("BRRS/FORMAT_NEW_CPR"); 

		if (acctNo != null) {
			FORMAT_NEW_CPR_Detail_Entity fsiEntity = findByDetailAcctnumber(acctNo);
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

			FORMAT_NEW_CPR_Detail_Entity existing = findByDetailAcctnumber(acctNo);
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
    "UPDATE BRRS_FORMAT_NEW_CPR_DETAILTABLE " +
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
							logger.info("Transaction committed — calling BRRS_FORMAT_NEW_CPR_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_FORMAT_NEW_CPR_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating FORMAT_NEW_CPR  record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
//=====================================================
// DETAIL EXCEL 
//=====================================================


	public byte[] getFORMAT_NEW_CPRDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  FORMAT_NEW_CPR  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getFORMAT_NEW_CPRDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("FORMAT_NEW_CPR Details ");

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
				List<FORMAT_NEW_CPR_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (FORMAT_NEW_CPR_Detail_Entity item : reportData) { 
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
					logger.info("No data found for FORMAT_NEW_CPR — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating FORMAT_NEW_CPR Excel", e);
				return new byte[0];
			}
		}
	
	
//=====================================================
// ARCHIVAL DETAIL EXCEL 
//=====================================================

	public byte[] getFORMAT_NEW_CPRDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for FORMAT_NEW_CPR ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("FORMAT_NEW_CPR Detail NEW");

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
				List<FORMAT_NEW_CPR_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (FORMAT_NEW_CPR_Archival_Detail_Entity item : reportData) {
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
					logger.info("No data found for FORMAT_NEW_CPR — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating FORMAT_NEW_CPR NEW Excel", e);
				return new byte[0];
			}
		}
		
		
//=====================================================
// Summary EXCEL 
//=====================================================

	public byte[] getFORMAT_NEW_CPRExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.FORMAT_NEW_CPR");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelFORMAT_NEW_CPRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<FORMAT_NEW_CPR_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		
		
			System.out.println("DATA SIZE IS : "+dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  FORMAT_NEW_CPR report. Returning empty result.");
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
						FORMAT_NEW_CPR_Summary_Entity record = dataList.get(i);
					
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

		       Cell cell2 = row.createCell(2);
if (record.getR8_AMOUNT() != null) {
   cell2.setCellValue(record.getR8_AMOUNT().doubleValue());
   cell2.setCellStyle(numberStyle);
} else {
   cell2.setCellValue("");
   cell2.setCellStyle(textStyle);
}

//R9
row = sheet.getRow(8);
cell2 = row.createCell(2);
if (record.getR9_AMOUNT() != null) {
 cell2.setCellValue(record.getR9_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R10
row = sheet.getRow(9);
cell2 = row.createCell(2);
if (record.getR10_AMOUNT() != null) {
 cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R11
row = sheet.getRow(10);
cell2 = row.createCell(2);
if (record.getR11_AMOUNT() != null) {
 cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R12
row = sheet.getRow(11);
cell2 = row.createCell(2);
if (record.getR12_AMOUNT() != null) {
 cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R13
row = sheet.getRow(12);
cell2 = row.createCell(2);
if (record.getR13_AMOUNT() != null) {
 cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R14
row = sheet.getRow(13);
cell2 = row.createCell(2);
if (record.getR14_AMOUNT() != null) {
 cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R15
row = sheet.getRow(14);
cell2 = row.createCell(2);
if (record.getR15_AMOUNT() != null) {
 cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R16
row = sheet.getRow(15);
cell2 = row.createCell(2);
if (record.getR16_AMOUNT() != null) {
 cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R17
row = sheet.getRow(16);
cell2 = row.createCell(2);
if (record.getR17_AMOUNT() != null) {
 cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R18
row = sheet.getRow(17);
cell2 = row.createCell(2);
if (record.getR18_AMOUNT() != null) {
 cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R19
row = sheet.getRow(18);
cell2 = row.createCell(2);
if (record.getR19_AMOUNT() != null) {
 cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R20
row = sheet.getRow(19);
cell2 = row.createCell(2);
if (record.getR20_AMOUNT() != null) {
 cell2.setCellValue(record.getR20_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R21
row = sheet.getRow(20);
cell2 = row.createCell(2);
if (record.getR21_AMOUNT() != null) {
 cell2.setCellValue(record.getR21_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R22
row = sheet.getRow(21);
cell2 = row.createCell(2);
if (record.getR22_AMOUNT() != null) {
 cell2.setCellValue(record.getR22_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R23
row = sheet.getRow(22);
cell2 = row.createCell(2);
if (record.getR23_AMOUNT() != null) {
 cell2.setCellValue(record.getR23_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R24
row = sheet.getRow(23);
cell2 = row.createCell(2);
if (record.getR24_AMOUNT() != null) {
 cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R25
row = sheet.getRow(24);
cell2 = row.createCell(2);
if (record.getR25_AMOUNT() != null) {
 cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R26
row = sheet.getRow(25);
cell2 = row.createCell(2);
if (record.getR26_AMOUNT() != null) {
 cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R27
row = sheet.getRow(26);
cell2 = row.createCell(2);
if (record.getR27_AMOUNT() != null) {
 cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R28
row = sheet.getRow(27);
cell2 = row.createCell(2);
if (record.getR28_AMOUNT() != null) {
 cell2.setCellValue(record.getR28_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R29
row = sheet.getRow(28);
cell2 = row.createCell(2);
if (record.getR29_AMOUNT() != null) {
 cell2.setCellValue(record.getR29_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R30
row = sheet.getRow(29);
cell2 = row.createCell(2);
if (record.getR30_AMOUNT() != null) {
 cell2.setCellValue(record.getR30_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R31
row = sheet.getRow(30);
cell2 = row.createCell(2);
if (record.getR31_AMOUNT() != null) {
 cell2.setCellValue(record.getR31_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R32
row = sheet.getRow(31);
cell2 = row.createCell(2);
if (record.getR32_AMOUNT() != null) {
 cell2.setCellValue(record.getR32_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R33
row = sheet.getRow(32);
cell2 = row.createCell(2);
if (record.getR33_AMOUNT() != null) {
 cell2.setCellValue(record.getR33_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R34
row = sheet.getRow(33);
cell2 = row.createCell(2);
if (record.getR34_AMOUNT() != null) {
 cell2.setCellValue(record.getR34_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R35
row = sheet.getRow(34);
cell2 = row.createCell(2);
if (record.getR35_AMOUNT() != null) {
 cell2.setCellValue(record.getR35_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R36
row = sheet.getRow(35);
cell2 = row.createCell(2);
if (record.getR36_AMOUNT() != null) {
 cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R37
row = sheet.getRow(36);
cell2 = row.createCell(2);
if (record.getR37_AMOUNT() != null) {
 cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R38
row = sheet.getRow(37);
cell2 = row.createCell(2);
if (record.getR38_AMOUNT() != null) {
 cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R39
row = sheet.getRow(38);
cell2 = row.createCell(2);
if (record.getR39_AMOUNT() != null) {
 cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R40
row = sheet.getRow(39);
cell2 = row.createCell(2);
if (record.getR40_AMOUNT() != null) {
 cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R41
row = sheet.getRow(40);
cell2 = row.createCell(2);
if (record.getR41_AMOUNT() != null) {
 cell2.setCellValue(record.getR41_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R42
row = sheet.getRow(41);
cell2 = row.createCell(2);
if (record.getR42_AMOUNT() != null) {
 cell2.setCellValue(record.getR42_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R43
row = sheet.getRow(42);
cell2 = row.createCell(2);
if (record.getR43_AMOUNT() != null) {
 cell2.setCellValue(record.getR43_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R44
row = sheet.getRow(43);
cell2 = row.createCell(2);
if (record.getR44_AMOUNT() != null) {
 cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}


//R46
row = sheet.getRow(45);
cell2 = row.createCell(2);
if (record.getR46_AMOUNT() != null) {
 cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R47
row = sheet.getRow(46);
cell2 = row.createCell(2);
if (record.getR47_AMOUNT() != null) {
 cell2.setCellValue(record.getR47_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//R48
row = sheet.getRow(47);
cell2 = row.createCell(2);
if (record.getR48_AMOUNT() != null) {
 cell2.setCellValue(record.getR48_AMOUNT().doubleValue());
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
				
				// audit service summary format

				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
											if (attrs != null) {
												HttpServletRequest request = attrs.getRequest();
												String userid = (String) request.getSession().getAttribute("USERID");
												auditService.createBusinessAudit(userid, "DOWNLOAD", "FORMAT_NEW_CPR  SUMMARY", null, "BRRS_FORMAT_NEW_CPR_SUMMARYTABLE");
											}

				return out.toByteArray();
			}

		}




//=====================================================
//ARCHIVAL SUMMARY EXCEL 
//=====================================================



				public byte[] getExcelFORMAT_NEW_CPRARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {	

			}

			List<FORMAT_NEW_CPR_Archival_Summary_Entity> dataList = 
					getDataByDateListArchival(dateformat.parse(todate), version);
		
	    

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for FORMAT_NEW_CPR new report. Returning empty result.");
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
						FORMAT_NEW_CPR_Archival_Summary_Entity record1 = dataList.get(i);
					    
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


	
					Cell cell2 = row.createCell(2);
				if (record1.getR8_AMOUNT() != null) {
				   cell2.setCellValue(record1.getR8_AMOUNT().doubleValue());
				   cell2.setCellStyle(numberStyle);
				} else {
				   cell2.setCellValue("");
				   cell2.setCellStyle(textStyle);
				}

				//R9
				row = sheet.getRow(8);
				cell2 = row.createCell(2);
				if (record1.getR9_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR9_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R10
				row = sheet.getRow(9);
				cell2 = row.createCell(2);
				if (record1.getR10_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR10_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R11
				row = sheet.getRow(10);
				cell2 = row.createCell(2);
				if (record1.getR11_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR11_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R12
				row = sheet.getRow(11);
				cell2 = row.createCell(2);
				if (record1.getR12_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR12_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R13
				row = sheet.getRow(12);
				cell2 = row.createCell(2);
				if (record1.getR13_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR13_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R14
				row = sheet.getRow(13);
				cell2 = row.createCell(2);
				if (record1.getR14_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR14_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R15
				row = sheet.getRow(14);
				cell2 = row.createCell(2);
				if (record1.getR15_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR15_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R16
				row = sheet.getRow(15);
				cell2 = row.createCell(2);
				if (record1.getR16_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR16_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R17
				row = sheet.getRow(16);
				cell2 = row.createCell(2);
				if (record1.getR17_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR17_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R18
				row = sheet.getRow(17);
				cell2 = row.createCell(2);
				if (record1.getR18_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR18_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R19
				row = sheet.getRow(18);
				cell2 = row.createCell(2);
				if (record1.getR19_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR19_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R20
				row = sheet.getRow(19);
				cell2 = row.createCell(2);
				if (record1.getR20_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR20_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R21
				row = sheet.getRow(20);
				cell2 = row.createCell(2);
				if (record1.getR21_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR21_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R22
				row = sheet.getRow(21);
				cell2 = row.createCell(2);
				if (record1.getR22_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR22_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R23
				row = sheet.getRow(22);
				cell2 = row.createCell(2);
				if (record1.getR23_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR23_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R24
				row = sheet.getRow(23);
				cell2 = row.createCell(2);
				if (record1.getR24_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR24_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R25
				row = sheet.getRow(24);
				cell2 = row.createCell(2);
				if (record1.getR25_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR25_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R26
				row = sheet.getRow(25);
				cell2 = row.createCell(2);
				if (record1.getR26_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR26_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R27
				row = sheet.getRow(26);
				cell2 = row.createCell(2);
				if (record1.getR27_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR27_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R28
				row = sheet.getRow(27);
				cell2 = row.createCell(2);
				if (record1.getR28_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR28_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R29
				row = sheet.getRow(28);
				cell2 = row.createCell(2);
				if (record1.getR29_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR29_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R30
				row = sheet.getRow(29);
				cell2 = row.createCell(2);
				if (record1.getR30_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR30_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R31
				row = sheet.getRow(30);
				cell2 = row.createCell(2);
				if (record1.getR31_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR31_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R32
				row = sheet.getRow(31);
				cell2 = row.createCell(2);
				if (record1.getR32_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR32_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R33
				row = sheet.getRow(32);
				cell2 = row.createCell(2);
				if (record1.getR33_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR33_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R34
				row = sheet.getRow(33);
				cell2 = row.createCell(2);
				if (record1.getR34_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR34_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R35
				row = sheet.getRow(34);
				cell2 = row.createCell(2);
				if (record1.getR35_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR35_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R36
				row = sheet.getRow(35);
				cell2 = row.createCell(2);
				if (record1.getR36_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR36_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R37
				row = sheet.getRow(36);
				cell2 = row.createCell(2);
				if (record1.getR37_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR37_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R38
				row = sheet.getRow(37);
				cell2 = row.createCell(2);
				if (record1.getR38_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR38_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R39
				row = sheet.getRow(38);
				cell2 = row.createCell(2);
				if (record1.getR39_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR39_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R40
				row = sheet.getRow(39);
				cell2 = row.createCell(2);
				if (record1.getR40_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR40_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R41
				row = sheet.getRow(40);
				cell2 = row.createCell(2);
				if (record1.getR41_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR41_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R42
				row = sheet.getRow(41);
				cell2 = row.createCell(2);
				if (record1.getR42_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR42_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R43
				row = sheet.getRow(42);
				cell2 = row.createCell(2);
				if (record1.getR43_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR43_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R44
				row = sheet.getRow(43);
				cell2 = row.createCell(2);
				if (record1.getR44_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR44_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}


				//R46
				row = sheet.getRow(45);
				cell2 = row.createCell(2);
				if (record1.getR46_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR46_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R47
				row = sheet.getRow(46);
				cell2 = row.createCell(2);
				if (record1.getR47_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR47_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				//R48
				row = sheet.getRow(47);
				cell2 = row.createCell(2);
				if (record1.getR48_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR48_AMOUNT().doubleValue());
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
				
				// audit service archival summary format

ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs != null) {
			HttpServletRequest request = attrs.getRequest();
			String userid = (String) request.getSession().getAttribute("USERID");
			auditService.createBusinessAudit(userid, "DOWNLOAD", "FORMAT_NEW_CPR ARCHIVAL SUMMARY", null, "BRRS_FORMAT_NEW_CPR_ARCHIVALTABLE_SUMMARY");
		}

				return out.toByteArray();
			}

		}
		
		
		
	}