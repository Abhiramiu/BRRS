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

public class BRRS_EXPOSURES_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_EXPOSURES_ReportService.class);
	
	
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


	public List<EXPOSURES_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_EXPOSURES_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new EXPOSURES_Summary_RowMapper()
    );
}
	
// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================



public List<Object[]> getEXPOSURES_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_EXPOSURES_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<EXPOSURES_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_EXPOSURES_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new EXPOSURES_Archival_Summary_RowMapper()
    );
}

public List<EXPOSURES_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_EXPOSURES_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new EXPOSURES_Archival_Summary_RowMapper()
    );
}

// =====================================================
// DETAIL REPO
// =====================================================	


public List<EXPOSURES_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_EXPOSURES_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new EXPOSURES_Detail_RowMapper()
    );
}

public List<EXPOSURES_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_EXPOSURES_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, offset, limit},
            new EXPOSURES_Detail_RowMapper()
    );
}

public int getDataCount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_EXPOSURES_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(sql, new Object[]{reportDate}, Integer.class);
}

public List<EXPOSURES_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel, String reportAddlCriteria1, Date reportDate) {

    String sql = "SELECT * FROM BRRS_EXPOSURES_DETAILTABLE " +
                 "WHERE REPORT_LABEL = ? " +
                 "AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate},
            new EXPOSURES_Detail_RowMapper()
    );
}

public EXPOSURES_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_EXPOSURES_DETAILTABLE WHERE ACCT_NUMBER = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{acctNumber},
            new EXPOSURES_Detail_RowMapper()
    );
}


// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================

public List<EXPOSURES_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate, String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_EXPOSURES_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, dataEntryVersion},
            new EXPOSURES_Archival_Detail_RowMapper()
    );
}


public List<EXPOSURES_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_EXPOSURES_ARCHIVALTABLE_DETAIL " +
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
            new EXPOSURES_Archival_Detail_RowMapper()
    );
}

// =====================================================
// SUMAMRY ENTITY 
// =====================================================


public class EXPOSURES_Summary_RowMapper implements RowMapper<EXPOSURES_Summary_Entity> {

    @Override
    public EXPOSURES_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        EXPOSURES_Summary_Entity obj = new EXPOSURES_Summary_Entity();


// =========================
// R4
// =========================
obj.setR4_AMOUNT(rs.getBigDecimal("R4_AMOUNT"));

// =========================
// R5
// =========================
obj.setR5_LINE_NO(rs.getString("R5_LINE_NO"));
obj.setR5_BORROWER(rs.getString("R5_BORROWER"));
obj.setR5_AMOUNT(rs.getString("R5_AMOUNT"));
obj.setR5_CAPITAL_FUNDS(rs.getString("R5_CAPITAL_FUNDS"));


// =========================
// R6
// =========================
obj.setR6_AMOUNT(rs.getBigDecimal("R6_AMOUNT"));
obj.setR6_CAPITAL_FUNDS(rs.getBigDecimal("R6_CAPITAL_FUNDS"));

// =========================
// R7
// =========================
obj.setR7_AMOUNT(rs.getBigDecimal("R7_AMOUNT"));
obj.setR7_CAPITAL_FUNDS(rs.getBigDecimal("R7_CAPITAL_FUNDS"));

// =========================
// R8
// =========================
obj.setR8_AMOUNT(rs.getBigDecimal("R8_AMOUNT"));
obj.setR8_CAPITAL_FUNDS(rs.getBigDecimal("R8_CAPITAL_FUNDS"));

// =========================
// R9
// =========================
obj.setR9_AMOUNT(rs.getBigDecimal("R9_AMOUNT"));
obj.setR9_CAPITAL_FUNDS(rs.getBigDecimal("R9_CAPITAL_FUNDS"));

// =========================
// R10
// =========================
obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));
obj.setR10_CAPITAL_FUNDS(rs.getBigDecimal("R10_CAPITAL_FUNDS"));

// =========================
// R11
// =========================
obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));
obj.setR11_CAPITAL_FUNDS(rs.getBigDecimal("R11_CAPITAL_FUNDS"));

// =========================
// R12
// =========================
obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));
obj.setR12_CAPITAL_FUNDS(rs.getBigDecimal("R12_CAPITAL_FUNDS"));

// =========================
// R13
// =========================
obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));
obj.setR13_CAPITAL_FUNDS(rs.getBigDecimal("R13_CAPITAL_FUNDS"));

// =========================
// R14
// =========================
obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));
obj.setR14_CAPITAL_FUNDS(rs.getBigDecimal("R14_CAPITAL_FUNDS"));

// =========================
// R15
// =========================
obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));
obj.setR15_CAPITAL_FUNDS(rs.getBigDecimal("R15_CAPITAL_FUNDS"));

// =========================
// R16
// =========================
obj.setR16_AMOUNT(rs.getBigDecimal("R16_AMOUNT"));
obj.setR16_CAPITAL_FUNDS(rs.getBigDecimal("R16_CAPITAL_FUNDS"));

// =========================
// R17
// =========================
obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));
obj.setR17_CAPITAL_FUNDS(rs.getBigDecimal("R17_CAPITAL_FUNDS"));

// =========================
// R18
// =========================
obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));
obj.setR18_CAPITAL_FUNDS(rs.getBigDecimal("R18_CAPITAL_FUNDS"));

// =========================
// R19
// =========================
obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));
obj.setR19_CAPITAL_FUNDS(rs.getBigDecimal("R19_CAPITAL_FUNDS"));

// =========================
// R20
// =========================
obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));
obj.setR20_CAPITAL_FUNDS(rs.getBigDecimal("R20_CAPITAL_FUNDS"));


// =========================
// R21
// =========================
obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));
obj.setR21_CAPITAL_FUNDS(rs.getBigDecimal("R21_CAPITAL_FUNDS"));

// =========================
// R22
// =========================
obj.setR22_AMOUNT(rs.getBigDecimal("R22_AMOUNT"));
obj.setR22_CAPITAL_FUNDS(rs.getBigDecimal("R22_CAPITAL_FUNDS"));

// =========================
// R23
// =========================
obj.setR23_AMOUNT(rs.getBigDecimal("R23_AMOUNT"));
obj.setR23_CAPITAL_FUNDS(rs.getBigDecimal("R23_CAPITAL_FUNDS"));

// =========================
// R24
// =========================
obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));
obj.setR24_CAPITAL_FUNDS(rs.getBigDecimal("R24_CAPITAL_FUNDS"));

// =========================
// R25
// =========================
obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));
obj.setR25_CAPITAL_FUNDS(rs.getBigDecimal("R25_CAPITAL_FUNDS"));

// =========================
// R26
// =========================
obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));
obj.setR26_CAPITAL_FUNDS(rs.getBigDecimal("R26_CAPITAL_FUNDS"));

// =========================
// R30
// =========================
obj.setR30_LINE_NO(rs.getBigDecimal("R30_LINE_NO"));
obj.setR30_BORROWER(rs.getBigDecimal("R30_BORROWER"));
obj.setR30_AMOUNT(rs.getBigDecimal("R30_AMOUNT"));
obj.setR30_CAPITAL_FUNDS(rs.getBigDecimal("R30_CAPITAL_FUNDS"));


// =========================
// R31
// =========================
obj.setR31_AMOUNT(rs.getBigDecimal("R31_AMOUNT"));
obj.setR31_CAPITAL_FUNDS(rs.getBigDecimal("R31_CAPITAL_FUNDS"));

// =========================
// R32
// =========================
obj.setR32_AMOUNT(rs.getBigDecimal("R32_AMOUNT"));
obj.setR32_CAPITAL_FUNDS(rs.getBigDecimal("R32_CAPITAL_FUNDS"));

// =========================
// R33
// =========================
obj.setR33_AMOUNT(rs.getBigDecimal("R33_AMOUNT"));
obj.setR33_CAPITAL_FUNDS(rs.getBigDecimal("R33_CAPITAL_FUNDS"));

// =========================
// R34
// =========================
obj.setR34_AMOUNT(rs.getBigDecimal("R34_AMOUNT"));
obj.setR34_CAPITAL_FUNDS(rs.getBigDecimal("R34_CAPITAL_FUNDS"));

// =========================
// R35
// =========================
obj.setR35_AMOUNT(rs.getBigDecimal("R35_AMOUNT"));
obj.setR35_CAPITAL_FUNDS(rs.getBigDecimal("R35_CAPITAL_FUNDS"));

// =========================
// R36
// =========================
obj.setR36_AMOUNT(rs.getBigDecimal("R36_AMOUNT"));
obj.setR36_CAPITAL_FUNDS(rs.getBigDecimal("R36_CAPITAL_FUNDS"));

// =========================
// R37
// =========================
obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));
obj.setR37_CAPITAL_FUNDS(rs.getBigDecimal("R37_CAPITAL_FUNDS"));

// =========================
// R38
// =========================
obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));
obj.setR38_CAPITAL_FUNDS(rs.getBigDecimal("R38_CAPITAL_FUNDS"));

// =========================
// R39
// =========================
obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));
obj.setR39_CAPITAL_FUNDS(rs.getBigDecimal("R39_CAPITAL_FUNDS"));

// =========================
// R40
// =========================
obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));
obj.setR40_CAPITAL_FUNDS(rs.getBigDecimal("R40_CAPITAL_FUNDS"));


// =========================
// R41
// =========================
obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));
obj.setR41_CAPITAL_FUNDS(rs.getBigDecimal("R41_CAPITAL_FUNDS"));

// =========================
// R42
// =========================
obj.setR42_AMOUNT(rs.getBigDecimal("R42_AMOUNT"));
obj.setR42_CAPITAL_FUNDS(rs.getBigDecimal("R42_CAPITAL_FUNDS"));

// =========================
// R43
// =========================
obj.setR43_AMOUNT(rs.getBigDecimal("R43_AMOUNT"));
obj.setR43_CAPITAL_FUNDS(rs.getBigDecimal("R43_CAPITAL_FUNDS"));

// =========================
// R44
// =========================
obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));
obj.setR44_CAPITAL_FUNDS(rs.getBigDecimal("R44_CAPITAL_FUNDS"));

// =========================
// R45
// =========================
obj.setR45_AMOUNT(rs.getBigDecimal("R45_AMOUNT"));
obj.setR45_CAPITAL_FUNDS(rs.getBigDecimal("R45_CAPITAL_FUNDS"));

// =========================
// R46
// =========================
obj.setR46_AMOUNT(rs.getBigDecimal("R46_AMOUNT"));
obj.setR46_CAPITAL_FUNDS(rs.getBigDecimal("R46_CAPITAL_FUNDS"));

// =========================
// R47
// =========================
obj.setR47_AMOUNT(rs.getBigDecimal("R47_AMOUNT"));
obj.setR47_CAPITAL_FUNDS(rs.getBigDecimal("R47_CAPITAL_FUNDS"));

// =========================
// R48
// =========================
obj.setR48_AMOUNT(rs.getBigDecimal("R48_AMOUNT"));
obj.setR48_CAPITAL_FUNDS(rs.getBigDecimal("R48_CAPITAL_FUNDS"));

// =========================
// R49
// =========================
obj.setR49_AMOUNT(rs.getBigDecimal("R49_AMOUNT"));
obj.setR49_CAPITAL_FUNDS(rs.getBigDecimal("R49_CAPITAL_FUNDS"));

// =========================
// R50
// =========================
obj.setR50_AMOUNT(rs.getBigDecimal("R50_AMOUNT"));
obj.setR50_CAPITAL_FUNDS(rs.getBigDecimal("R50_CAPITAL_FUNDS"));


// =========================
// R51
// =========================
obj.setR51_AMOUNT(rs.getBigDecimal("R51_AMOUNT"));
obj.setR51_CAPITAL_FUNDS(rs.getBigDecimal("R51_CAPITAL_FUNDS"));

// =========================
// R55
// =========================
obj.setR55_LINE_NO(rs.getString("R55_LINE_NO"));
obj.setR55_BORROWER(rs.getString("R55_BORROWER"));
obj.setR55_AMOUNT(rs.getString("R55_AMOUNT"));

// =========================
// R56
// =========================
obj.setR56_LINE_NO(rs.getString("R56_LINE_NO"));
obj.setR56_BORROWER(rs.getString("R56_BORROWER"));
obj.setR56_AMOUNT(rs.getString("R56_AMOUNT"));

// =========================
// R57
// =========================
obj.setR57_AMOUNT(rs.getBigDecimal("R57_AMOUNT"));

// =========================
// R58
// =========================
obj.setR58_AMOUNT(rs.getBigDecimal("R58_AMOUNT"));

// =========================
// R59
// =========================
obj.setR59_AMOUNT(rs.getBigDecimal("R59_AMOUNT"));

// =========================
// R60
// =========================
obj.setR60_AMOUNT(rs.getBigDecimal("R60_AMOUNT"));

// =========================
// R61
// =========================
obj.setR61_AMOUNT(rs.getBigDecimal("R61_AMOUNT"));

// =========================
// R62
// =========================
obj.setR62_AMOUNT(rs.getBigDecimal("R62_AMOUNT"));

// =========================
// R63
// =========================
obj.setR63_AMOUNT(rs.getBigDecimal("R63_AMOUNT"));

// =========================
// R64
// =========================
obj.setR64_AMOUNT(rs.getBigDecimal("R64_AMOUNT"));




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


public class EXPOSURES_Summary_Entity {
	
private BigDecimal R4_AMOUNT;

private String R5_LINE_NO;
private String R5_BORROWER;
private String R5_AMOUNT;
private String R5_CAPITAL_FUNDS;

private BigDecimal R6_AMOUNT;
private BigDecimal R6_CAPITAL_FUNDS;

private BigDecimal R7_AMOUNT;
private BigDecimal R7_CAPITAL_FUNDS;

private BigDecimal R8_AMOUNT;
private BigDecimal R8_CAPITAL_FUNDS;

private BigDecimal R9_AMOUNT;
private BigDecimal R9_CAPITAL_FUNDS;

private BigDecimal R10_AMOUNT;
private BigDecimal R10_CAPITAL_FUNDS;

private BigDecimal R11_AMOUNT;
private BigDecimal R11_CAPITAL_FUNDS;

private BigDecimal R12_AMOUNT;
private BigDecimal R12_CAPITAL_FUNDS;

private BigDecimal R13_AMOUNT;
private BigDecimal R13_CAPITAL_FUNDS;

private BigDecimal R14_AMOUNT;
private BigDecimal R14_CAPITAL_FUNDS;

private BigDecimal R15_AMOUNT;
private BigDecimal R15_CAPITAL_FUNDS;

private BigDecimal R16_AMOUNT;
private BigDecimal R16_CAPITAL_FUNDS;

private BigDecimal R17_AMOUNT;
private BigDecimal R17_CAPITAL_FUNDS;

private BigDecimal R18_AMOUNT;
private BigDecimal R18_CAPITAL_FUNDS;

private BigDecimal R19_AMOUNT;
private BigDecimal R19_CAPITAL_FUNDS;

private BigDecimal R20_AMOUNT;
private BigDecimal R20_CAPITAL_FUNDS;

private BigDecimal R21_AMOUNT;
private BigDecimal R21_CAPITAL_FUNDS;

private BigDecimal R22_AMOUNT;
private BigDecimal R22_CAPITAL_FUNDS;

private BigDecimal R23_AMOUNT;
private BigDecimal R23_CAPITAL_FUNDS;

private BigDecimal R24_AMOUNT;
private BigDecimal R24_CAPITAL_FUNDS;

private BigDecimal R25_AMOUNT;
private BigDecimal R25_CAPITAL_FUNDS;

private BigDecimal R26_AMOUNT;
private BigDecimal R26_CAPITAL_FUNDS;

private BigDecimal R30_LINE_NO;
private BigDecimal R30_BORROWER;
private BigDecimal R30_AMOUNT;
private BigDecimal R30_CAPITAL_FUNDS;

private BigDecimal R31_AMOUNT;
private BigDecimal R31_CAPITAL_FUNDS;

private BigDecimal R32_AMOUNT;
private BigDecimal R32_CAPITAL_FUNDS;

private BigDecimal R33_AMOUNT;
private BigDecimal R33_CAPITAL_FUNDS;

private BigDecimal R34_AMOUNT;
private BigDecimal R34_CAPITAL_FUNDS;

private BigDecimal R35_AMOUNT;
private BigDecimal R35_CAPITAL_FUNDS;

private BigDecimal R36_AMOUNT;
private BigDecimal R36_CAPITAL_FUNDS;

private BigDecimal R37_AMOUNT;
private BigDecimal R37_CAPITAL_FUNDS;

private BigDecimal R38_AMOUNT;
private BigDecimal R38_CAPITAL_FUNDS;

private BigDecimal R39_AMOUNT;
private BigDecimal R39_CAPITAL_FUNDS;

private BigDecimal R40_AMOUNT;
private BigDecimal R40_CAPITAL_FUNDS;

private BigDecimal R41_AMOUNT;
private BigDecimal R41_CAPITAL_FUNDS;

private BigDecimal R42_AMOUNT;
private BigDecimal R42_CAPITAL_FUNDS;

private BigDecimal R43_AMOUNT;
private BigDecimal R43_CAPITAL_FUNDS;

private BigDecimal R44_AMOUNT;
private BigDecimal R44_CAPITAL_FUNDS;

private BigDecimal R45_AMOUNT;
private BigDecimal R45_CAPITAL_FUNDS;

private BigDecimal R46_AMOUNT;
private BigDecimal R46_CAPITAL_FUNDS;

private BigDecimal R47_AMOUNT;
private BigDecimal R47_CAPITAL_FUNDS;

private BigDecimal R48_AMOUNT;
private BigDecimal R48_CAPITAL_FUNDS;

private BigDecimal R49_AMOUNT;
private BigDecimal R49_CAPITAL_FUNDS;

private BigDecimal R50_AMOUNT;
private BigDecimal R50_CAPITAL_FUNDS;

private BigDecimal R51_AMOUNT;
private BigDecimal R51_CAPITAL_FUNDS;

private String R55_LINE_NO;
private String R55_BORROWER;
private String R55_AMOUNT;

private String R56_LINE_NO;
private String R56_BORROWER;
private String R56_AMOUNT;

private BigDecimal R57_AMOUNT;
private BigDecimal R58_AMOUNT;
private BigDecimal R59_AMOUNT;
private BigDecimal R60_AMOUNT;
private BigDecimal R61_AMOUNT;
private BigDecimal R62_AMOUNT;
private BigDecimal R63_AMOUNT;
private BigDecimal R64_AMOUNT;

	
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
	
	
	
public BigDecimal getR4_AMOUNT() {
	return R4_AMOUNT;
}
public void setR4_AMOUNT(BigDecimal r4_AMOUNT) {
	R4_AMOUNT = r4_AMOUNT;
}
public String getR5_LINE_NO() {
	return R5_LINE_NO;
}
public void setR5_LINE_NO(String r5_LINE_NO) {
	R5_LINE_NO = r5_LINE_NO;
}
public String getR5_BORROWER() {
	return R5_BORROWER;
}
public void setR5_BORROWER(String r5_BORROWER) {
	R5_BORROWER = r5_BORROWER;
}
public String getR5_AMOUNT() {
	return R5_AMOUNT;
}
public void setR5_AMOUNT(String r5_AMOUNT) {
	R5_AMOUNT = r5_AMOUNT;
}
public String getR5_CAPITAL_FUNDS() {
	return R5_CAPITAL_FUNDS;
}
public void setR5_CAPITAL_FUNDS(String r5_CAPITAL_FUNDS) {
	R5_CAPITAL_FUNDS = r5_CAPITAL_FUNDS;
}
public BigDecimal getR6_AMOUNT() {
	return R6_AMOUNT;
}
public void setR6_AMOUNT(BigDecimal r6_AMOUNT) {
	R6_AMOUNT = r6_AMOUNT;
}
public BigDecimal getR6_CAPITAL_FUNDS() {
	return R6_CAPITAL_FUNDS;
}
public void setR6_CAPITAL_FUNDS(BigDecimal r6_CAPITAL_FUNDS) {
	R6_CAPITAL_FUNDS = r6_CAPITAL_FUNDS;
}
public BigDecimal getR7_AMOUNT() {
	return R7_AMOUNT;
}
public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
	R7_AMOUNT = r7_AMOUNT;
}
public BigDecimal getR7_CAPITAL_FUNDS() {
	return R7_CAPITAL_FUNDS;
}
public void setR7_CAPITAL_FUNDS(BigDecimal r7_CAPITAL_FUNDS) {
	R7_CAPITAL_FUNDS = r7_CAPITAL_FUNDS;
}
public BigDecimal getR8_AMOUNT() {
	return R8_AMOUNT;
}
public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
	R8_AMOUNT = r8_AMOUNT;
}
public BigDecimal getR8_CAPITAL_FUNDS() {
	return R8_CAPITAL_FUNDS;
}
public void setR8_CAPITAL_FUNDS(BigDecimal r8_CAPITAL_FUNDS) {
	R8_CAPITAL_FUNDS = r8_CAPITAL_FUNDS;
}
public BigDecimal getR9_AMOUNT() {
	return R9_AMOUNT;
}
public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
	R9_AMOUNT = r9_AMOUNT;
}
public BigDecimal getR9_CAPITAL_FUNDS() {
	return R9_CAPITAL_FUNDS;
}
public void setR9_CAPITAL_FUNDS(BigDecimal r9_CAPITAL_FUNDS) {
	R9_CAPITAL_FUNDS = r9_CAPITAL_FUNDS;
}
public BigDecimal getR10_AMOUNT() {
	return R10_AMOUNT;
}
public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
	R10_AMOUNT = r10_AMOUNT;
}
public BigDecimal getR10_CAPITAL_FUNDS() {
	return R10_CAPITAL_FUNDS;
}
public void setR10_CAPITAL_FUNDS(BigDecimal r10_CAPITAL_FUNDS) {
	R10_CAPITAL_FUNDS = r10_CAPITAL_FUNDS;
}
public BigDecimal getR11_AMOUNT() {
	return R11_AMOUNT;
}
public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
	R11_AMOUNT = r11_AMOUNT;
}
public BigDecimal getR11_CAPITAL_FUNDS() {
	return R11_CAPITAL_FUNDS;
}
public void setR11_CAPITAL_FUNDS(BigDecimal r11_CAPITAL_FUNDS) {
	R11_CAPITAL_FUNDS = r11_CAPITAL_FUNDS;
}
public BigDecimal getR12_AMOUNT() {
	return R12_AMOUNT;
}
public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
	R12_AMOUNT = r12_AMOUNT;
}
public BigDecimal getR12_CAPITAL_FUNDS() {
	return R12_CAPITAL_FUNDS;
}
public void setR12_CAPITAL_FUNDS(BigDecimal r12_CAPITAL_FUNDS) {
	R12_CAPITAL_FUNDS = r12_CAPITAL_FUNDS;
}
public BigDecimal getR13_AMOUNT() {
	return R13_AMOUNT;
}
public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
	R13_AMOUNT = r13_AMOUNT;
}
public BigDecimal getR13_CAPITAL_FUNDS() {
	return R13_CAPITAL_FUNDS;
}
public void setR13_CAPITAL_FUNDS(BigDecimal r13_CAPITAL_FUNDS) {
	R13_CAPITAL_FUNDS = r13_CAPITAL_FUNDS;
}
public BigDecimal getR14_AMOUNT() {
	return R14_AMOUNT;
}
public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
	R14_AMOUNT = r14_AMOUNT;
}
public BigDecimal getR14_CAPITAL_FUNDS() {
	return R14_CAPITAL_FUNDS;
}
public void setR14_CAPITAL_FUNDS(BigDecimal r14_CAPITAL_FUNDS) {
	R14_CAPITAL_FUNDS = r14_CAPITAL_FUNDS;
}
public BigDecimal getR15_AMOUNT() {
	return R15_AMOUNT;
}
public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
	R15_AMOUNT = r15_AMOUNT;
}
public BigDecimal getR15_CAPITAL_FUNDS() {
	return R15_CAPITAL_FUNDS;
}
public void setR15_CAPITAL_FUNDS(BigDecimal r15_CAPITAL_FUNDS) {
	R15_CAPITAL_FUNDS = r15_CAPITAL_FUNDS;
}
public BigDecimal getR16_AMOUNT() {
	return R16_AMOUNT;
}
public void setR16_AMOUNT(BigDecimal r16_AMOUNT) {
	R16_AMOUNT = r16_AMOUNT;
}
public BigDecimal getR16_CAPITAL_FUNDS() {
	return R16_CAPITAL_FUNDS;
}
public void setR16_CAPITAL_FUNDS(BigDecimal r16_CAPITAL_FUNDS) {
	R16_CAPITAL_FUNDS = r16_CAPITAL_FUNDS;
}
public BigDecimal getR17_AMOUNT() {
	return R17_AMOUNT;
}
public void setR17_AMOUNT(BigDecimal r17_AMOUNT) {
	R17_AMOUNT = r17_AMOUNT;
}
public BigDecimal getR17_CAPITAL_FUNDS() {
	return R17_CAPITAL_FUNDS;
}
public void setR17_CAPITAL_FUNDS(BigDecimal r17_CAPITAL_FUNDS) {
	R17_CAPITAL_FUNDS = r17_CAPITAL_FUNDS;
}
public BigDecimal getR18_AMOUNT() {
	return R18_AMOUNT;
}
public void setR18_AMOUNT(BigDecimal r18_AMOUNT) {
	R18_AMOUNT = r18_AMOUNT;
}
public BigDecimal getR18_CAPITAL_FUNDS() {
	return R18_CAPITAL_FUNDS;
}
public void setR18_CAPITAL_FUNDS(BigDecimal r18_CAPITAL_FUNDS) {
	R18_CAPITAL_FUNDS = r18_CAPITAL_FUNDS;
}
public BigDecimal getR19_AMOUNT() {
	return R19_AMOUNT;
}
public void setR19_AMOUNT(BigDecimal r19_AMOUNT) {
	R19_AMOUNT = r19_AMOUNT;
}
public BigDecimal getR19_CAPITAL_FUNDS() {
	return R19_CAPITAL_FUNDS;
}
public void setR19_CAPITAL_FUNDS(BigDecimal r19_CAPITAL_FUNDS) {
	R19_CAPITAL_FUNDS = r19_CAPITAL_FUNDS;
}
public BigDecimal getR20_AMOUNT() {
	return R20_AMOUNT;
}
public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
	R20_AMOUNT = r20_AMOUNT;
}
public BigDecimal getR20_CAPITAL_FUNDS() {
	return R20_CAPITAL_FUNDS;
}
public void setR20_CAPITAL_FUNDS(BigDecimal r20_CAPITAL_FUNDS) {
	R20_CAPITAL_FUNDS = r20_CAPITAL_FUNDS;
}
public BigDecimal getR21_AMOUNT() {
	return R21_AMOUNT;
}
public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
	R21_AMOUNT = r21_AMOUNT;
}
public BigDecimal getR21_CAPITAL_FUNDS() {
	return R21_CAPITAL_FUNDS;
}
public void setR21_CAPITAL_FUNDS(BigDecimal r21_CAPITAL_FUNDS) {
	R21_CAPITAL_FUNDS = r21_CAPITAL_FUNDS;
}
public BigDecimal getR22_AMOUNT() {
	return R22_AMOUNT;
}
public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
	R22_AMOUNT = r22_AMOUNT;
}
public BigDecimal getR22_CAPITAL_FUNDS() {
	return R22_CAPITAL_FUNDS;
}
public void setR22_CAPITAL_FUNDS(BigDecimal r22_CAPITAL_FUNDS) {
	R22_CAPITAL_FUNDS = r22_CAPITAL_FUNDS;
}
public BigDecimal getR23_AMOUNT() {
	return R23_AMOUNT;
}
public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
	R23_AMOUNT = r23_AMOUNT;
}
public BigDecimal getR23_CAPITAL_FUNDS() {
	return R23_CAPITAL_FUNDS;
}
public void setR23_CAPITAL_FUNDS(BigDecimal r23_CAPITAL_FUNDS) {
	R23_CAPITAL_FUNDS = r23_CAPITAL_FUNDS;
}
public BigDecimal getR24_AMOUNT() {
	return R24_AMOUNT;
}
public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
	R24_AMOUNT = r24_AMOUNT;
}
public BigDecimal getR24_CAPITAL_FUNDS() {
	return R24_CAPITAL_FUNDS;
}
public void setR24_CAPITAL_FUNDS(BigDecimal r24_CAPITAL_FUNDS) {
	R24_CAPITAL_FUNDS = r24_CAPITAL_FUNDS;
}
public BigDecimal getR25_AMOUNT() {
	return R25_AMOUNT;
}
public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
	R25_AMOUNT = r25_AMOUNT;
}
public BigDecimal getR25_CAPITAL_FUNDS() {
	return R25_CAPITAL_FUNDS;
}
public void setR25_CAPITAL_FUNDS(BigDecimal r25_CAPITAL_FUNDS) {
	R25_CAPITAL_FUNDS = r25_CAPITAL_FUNDS;
}
public BigDecimal getR26_AMOUNT() {
	return R26_AMOUNT;
}
public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
	R26_AMOUNT = r26_AMOUNT;
}
public BigDecimal getR26_CAPITAL_FUNDS() {
	return R26_CAPITAL_FUNDS;
}
public void setR26_CAPITAL_FUNDS(BigDecimal r26_CAPITAL_FUNDS) {
	R26_CAPITAL_FUNDS = r26_CAPITAL_FUNDS;
}
public BigDecimal getR30_LINE_NO() {
	return R30_LINE_NO;
}
public void setR30_LINE_NO(BigDecimal r30_LINE_NO) {
	R30_LINE_NO = r30_LINE_NO;
}
public BigDecimal getR30_BORROWER() {
	return R30_BORROWER;
}
public void setR30_BORROWER(BigDecimal r30_BORROWER) {
	R30_BORROWER = r30_BORROWER;
}
public BigDecimal getR30_AMOUNT() {
	return R30_AMOUNT;
}
public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
	R30_AMOUNT = r30_AMOUNT;
}
public BigDecimal getR30_CAPITAL_FUNDS() {
	return R30_CAPITAL_FUNDS;
}
public void setR30_CAPITAL_FUNDS(BigDecimal r30_CAPITAL_FUNDS) {
	R30_CAPITAL_FUNDS = r30_CAPITAL_FUNDS;
}
public BigDecimal getR31_AMOUNT() {
	return R31_AMOUNT;
}
public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
	R31_AMOUNT = r31_AMOUNT;
}
public BigDecimal getR31_CAPITAL_FUNDS() {
	return R31_CAPITAL_FUNDS;
}
public void setR31_CAPITAL_FUNDS(BigDecimal r31_CAPITAL_FUNDS) {
	R31_CAPITAL_FUNDS = r31_CAPITAL_FUNDS;
}
public BigDecimal getR32_AMOUNT() {
	return R32_AMOUNT;
}
public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
	R32_AMOUNT = r32_AMOUNT;
}
public BigDecimal getR32_CAPITAL_FUNDS() {
	return R32_CAPITAL_FUNDS;
}
public void setR32_CAPITAL_FUNDS(BigDecimal r32_CAPITAL_FUNDS) {
	R32_CAPITAL_FUNDS = r32_CAPITAL_FUNDS;
}
public BigDecimal getR33_AMOUNT() {
	return R33_AMOUNT;
}
public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
	R33_AMOUNT = r33_AMOUNT;
}
public BigDecimal getR33_CAPITAL_FUNDS() {
	return R33_CAPITAL_FUNDS;
}
public void setR33_CAPITAL_FUNDS(BigDecimal r33_CAPITAL_FUNDS) {
	R33_CAPITAL_FUNDS = r33_CAPITAL_FUNDS;
}
public BigDecimal getR34_AMOUNT() {
	return R34_AMOUNT;
}
public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
	R34_AMOUNT = r34_AMOUNT;
}
public BigDecimal getR34_CAPITAL_FUNDS() {
	return R34_CAPITAL_FUNDS;
}
public void setR34_CAPITAL_FUNDS(BigDecimal r34_CAPITAL_FUNDS) {
	R34_CAPITAL_FUNDS = r34_CAPITAL_FUNDS;
}
public BigDecimal getR35_AMOUNT() {
	return R35_AMOUNT;
}
public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
	R35_AMOUNT = r35_AMOUNT;
}
public BigDecimal getR35_CAPITAL_FUNDS() {
	return R35_CAPITAL_FUNDS;
}
public void setR35_CAPITAL_FUNDS(BigDecimal r35_CAPITAL_FUNDS) {
	R35_CAPITAL_FUNDS = r35_CAPITAL_FUNDS;
}
public BigDecimal getR36_AMOUNT() {
	return R36_AMOUNT;
}
public void setR36_AMOUNT(BigDecimal r36_AMOUNT) {
	R36_AMOUNT = r36_AMOUNT;
}
public BigDecimal getR36_CAPITAL_FUNDS() {
	return R36_CAPITAL_FUNDS;
}
public void setR36_CAPITAL_FUNDS(BigDecimal r36_CAPITAL_FUNDS) {
	R36_CAPITAL_FUNDS = r36_CAPITAL_FUNDS;
}
public BigDecimal getR37_AMOUNT() {
	return R37_AMOUNT;
}
public void setR37_AMOUNT(BigDecimal r37_AMOUNT) {
	R37_AMOUNT = r37_AMOUNT;
}
public BigDecimal getR37_CAPITAL_FUNDS() {
	return R37_CAPITAL_FUNDS;
}
public void setR37_CAPITAL_FUNDS(BigDecimal r37_CAPITAL_FUNDS) {
	R37_CAPITAL_FUNDS = r37_CAPITAL_FUNDS;
}
public BigDecimal getR38_AMOUNT() {
	return R38_AMOUNT;
}
public void setR38_AMOUNT(BigDecimal r38_AMOUNT) {
	R38_AMOUNT = r38_AMOUNT;
}
public BigDecimal getR38_CAPITAL_FUNDS() {
	return R38_CAPITAL_FUNDS;
}
public void setR38_CAPITAL_FUNDS(BigDecimal r38_CAPITAL_FUNDS) {
	R38_CAPITAL_FUNDS = r38_CAPITAL_FUNDS;
}
public BigDecimal getR39_AMOUNT() {
	return R39_AMOUNT;
}
public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
	R39_AMOUNT = r39_AMOUNT;
}
public BigDecimal getR39_CAPITAL_FUNDS() {
	return R39_CAPITAL_FUNDS;
}
public void setR39_CAPITAL_FUNDS(BigDecimal r39_CAPITAL_FUNDS) {
	R39_CAPITAL_FUNDS = r39_CAPITAL_FUNDS;
}
public BigDecimal getR40_AMOUNT() {
	return R40_AMOUNT;
}
public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
	R40_AMOUNT = r40_AMOUNT;
}
public BigDecimal getR40_CAPITAL_FUNDS() {
	return R40_CAPITAL_FUNDS;
}
public void setR40_CAPITAL_FUNDS(BigDecimal r40_CAPITAL_FUNDS) {
	R40_CAPITAL_FUNDS = r40_CAPITAL_FUNDS;
}
public BigDecimal getR41_AMOUNT() {
	return R41_AMOUNT;
}
public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
	R41_AMOUNT = r41_AMOUNT;
}
public BigDecimal getR41_CAPITAL_FUNDS() {
	return R41_CAPITAL_FUNDS;
}
public void setR41_CAPITAL_FUNDS(BigDecimal r41_CAPITAL_FUNDS) {
	R41_CAPITAL_FUNDS = r41_CAPITAL_FUNDS;
}
public BigDecimal getR42_AMOUNT() {
	return R42_AMOUNT;
}
public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
	R42_AMOUNT = r42_AMOUNT;
}
public BigDecimal getR42_CAPITAL_FUNDS() {
	return R42_CAPITAL_FUNDS;
}
public void setR42_CAPITAL_FUNDS(BigDecimal r42_CAPITAL_FUNDS) {
	R42_CAPITAL_FUNDS = r42_CAPITAL_FUNDS;
}
public BigDecimal getR43_AMOUNT() {
	return R43_AMOUNT;
}
public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
	R43_AMOUNT = r43_AMOUNT;
}
public BigDecimal getR43_CAPITAL_FUNDS() {
	return R43_CAPITAL_FUNDS;
}
public void setR43_CAPITAL_FUNDS(BigDecimal r43_CAPITAL_FUNDS) {
	R43_CAPITAL_FUNDS = r43_CAPITAL_FUNDS;
}
public BigDecimal getR44_AMOUNT() {
	return R44_AMOUNT;
}
public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
	R44_AMOUNT = r44_AMOUNT;
}
public BigDecimal getR44_CAPITAL_FUNDS() {
	return R44_CAPITAL_FUNDS;
}
public void setR44_CAPITAL_FUNDS(BigDecimal r44_CAPITAL_FUNDS) {
	R44_CAPITAL_FUNDS = r44_CAPITAL_FUNDS;
}
public BigDecimal getR45_AMOUNT() {
	return R45_AMOUNT;
}
public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
	R45_AMOUNT = r45_AMOUNT;
}
public BigDecimal getR45_CAPITAL_FUNDS() {
	return R45_CAPITAL_FUNDS;
}
public void setR45_CAPITAL_FUNDS(BigDecimal r45_CAPITAL_FUNDS) {
	R45_CAPITAL_FUNDS = r45_CAPITAL_FUNDS;
}
public BigDecimal getR46_AMOUNT() {
	return R46_AMOUNT;
}
public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
	R46_AMOUNT = r46_AMOUNT;
}
public BigDecimal getR46_CAPITAL_FUNDS() {
	return R46_CAPITAL_FUNDS;
}
public void setR46_CAPITAL_FUNDS(BigDecimal r46_CAPITAL_FUNDS) {
	R46_CAPITAL_FUNDS = r46_CAPITAL_FUNDS;
}
public BigDecimal getR47_AMOUNT() {
	return R47_AMOUNT;
}
public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
	R47_AMOUNT = r47_AMOUNT;
}
public BigDecimal getR47_CAPITAL_FUNDS() {
	return R47_CAPITAL_FUNDS;
}
public void setR47_CAPITAL_FUNDS(BigDecimal r47_CAPITAL_FUNDS) {
	R47_CAPITAL_FUNDS = r47_CAPITAL_FUNDS;
}
public BigDecimal getR48_AMOUNT() {
	return R48_AMOUNT;
}
public void setR48_AMOUNT(BigDecimal r48_AMOUNT) {
	R48_AMOUNT = r48_AMOUNT;
}
public BigDecimal getR48_CAPITAL_FUNDS() {
	return R48_CAPITAL_FUNDS;
}
public void setR48_CAPITAL_FUNDS(BigDecimal r48_CAPITAL_FUNDS) {
	R48_CAPITAL_FUNDS = r48_CAPITAL_FUNDS;
}
public BigDecimal getR49_AMOUNT() {
	return R49_AMOUNT;
}
public void setR49_AMOUNT(BigDecimal r49_AMOUNT) {
	R49_AMOUNT = r49_AMOUNT;
}
public BigDecimal getR49_CAPITAL_FUNDS() {
	return R49_CAPITAL_FUNDS;
}
public void setR49_CAPITAL_FUNDS(BigDecimal r49_CAPITAL_FUNDS) {
	R49_CAPITAL_FUNDS = r49_CAPITAL_FUNDS;
}
public BigDecimal getR50_AMOUNT() {
	return R50_AMOUNT;
}
public void setR50_AMOUNT(BigDecimal r50_AMOUNT) {
	R50_AMOUNT = r50_AMOUNT;
}
public BigDecimal getR50_CAPITAL_FUNDS() {
	return R50_CAPITAL_FUNDS;
}
public void setR50_CAPITAL_FUNDS(BigDecimal r50_CAPITAL_FUNDS) {
	R50_CAPITAL_FUNDS = r50_CAPITAL_FUNDS;
}
public BigDecimal getR51_AMOUNT() {
	return R51_AMOUNT;
}
public void setR51_AMOUNT(BigDecimal r51_AMOUNT) {
	R51_AMOUNT = r51_AMOUNT;
}
public BigDecimal getR51_CAPITAL_FUNDS() {
	return R51_CAPITAL_FUNDS;
}
public void setR51_CAPITAL_FUNDS(BigDecimal r51_CAPITAL_FUNDS) {
	R51_CAPITAL_FUNDS = r51_CAPITAL_FUNDS;
}
public String getR55_LINE_NO() {
	return R55_LINE_NO;
}
public void setR55_LINE_NO(String r55_LINE_NO) {
	R55_LINE_NO = r55_LINE_NO;
}
public String getR55_BORROWER() {
	return R55_BORROWER;
}
public void setR55_BORROWER(String r55_BORROWER) {
	R55_BORROWER = r55_BORROWER;
}
public String getR55_AMOUNT() {
	return R55_AMOUNT;
}
public void setR55_AMOUNT(String r55_AMOUNT) {
	R55_AMOUNT = r55_AMOUNT;
}
public String getR56_LINE_NO() {
	return R56_LINE_NO;
}
public void setR56_LINE_NO(String r56_LINE_NO) {
	R56_LINE_NO = r56_LINE_NO;
}
public String getR56_BORROWER() {
	return R56_BORROWER;
}
public void setR56_BORROWER(String r56_BORROWER) {
	R56_BORROWER = r56_BORROWER;
}
public String getR56_AMOUNT() {
	return R56_AMOUNT;
}
public void setR56_AMOUNT(String r56_AMOUNT) {
	R56_AMOUNT = r56_AMOUNT;
}
public BigDecimal getR57_AMOUNT() {
	return R57_AMOUNT;
}
public void setR57_AMOUNT(BigDecimal r57_AMOUNT) {
	R57_AMOUNT = r57_AMOUNT;
}
public BigDecimal getR58_AMOUNT() {
	return R58_AMOUNT;
}
public void setR58_AMOUNT(BigDecimal r58_AMOUNT) {
	R58_AMOUNT = r58_AMOUNT;
}
public BigDecimal getR59_AMOUNT() {
	return R59_AMOUNT;
}
public void setR59_AMOUNT(BigDecimal r59_AMOUNT) {
	R59_AMOUNT = r59_AMOUNT;
}
public BigDecimal getR60_AMOUNT() {
	return R60_AMOUNT;
}
public void setR60_AMOUNT(BigDecimal r60_AMOUNT) {
	R60_AMOUNT = r60_AMOUNT;
}
public BigDecimal getR61_AMOUNT() {
	return R61_AMOUNT;
}
public void setR61_AMOUNT(BigDecimal r61_AMOUNT) {
	R61_AMOUNT = r61_AMOUNT;
}
public BigDecimal getR62_AMOUNT() {
	return R62_AMOUNT;
}
public void setR62_AMOUNT(BigDecimal r62_AMOUNT) {
	R62_AMOUNT = r62_AMOUNT;
}
public BigDecimal getR63_AMOUNT() {
	return R63_AMOUNT;
}
public void setR63_AMOUNT(BigDecimal r63_AMOUNT) {
	R63_AMOUNT = r63_AMOUNT;
}
public BigDecimal getR64_AMOUNT() {
	return R64_AMOUNT;
}
public void setR64_AMOUNT(BigDecimal r64_AMOUNT) {
	R64_AMOUNT = r64_AMOUNT;
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


public class EXPOSURES_Archival_Summary_RowMapper
        implements RowMapper<EXPOSURES_Archival_Summary_Entity> {

    @Override
    public EXPOSURES_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        EXPOSURES_Archival_Summary_Entity obj = new EXPOSURES_Archival_Summary_Entity();

// =========================
// R4
// =========================
obj.setR4_AMOUNT(rs.getBigDecimal("R4_AMOUNT"));

// =========================
// R5
// =========================
obj.setR5_LINE_NO(rs.getString("R5_LINE_NO"));
obj.setR5_BORROWER(rs.getString("R5_BORROWER"));
obj.setR5_AMOUNT(rs.getString("R5_AMOUNT"));
obj.setR5_CAPITAL_FUNDS(rs.getString("R5_CAPITAL_FUNDS"));


// =========================
// R6
// =========================
obj.setR6_AMOUNT(rs.getBigDecimal("R6_AMOUNT"));
obj.setR6_CAPITAL_FUNDS(rs.getBigDecimal("R6_CAPITAL_FUNDS"));

// =========================
// R7
// =========================
obj.setR7_AMOUNT(rs.getBigDecimal("R7_AMOUNT"));
obj.setR7_CAPITAL_FUNDS(rs.getBigDecimal("R7_CAPITAL_FUNDS"));

// =========================
// R8
// =========================
obj.setR8_AMOUNT(rs.getBigDecimal("R8_AMOUNT"));
obj.setR8_CAPITAL_FUNDS(rs.getBigDecimal("R8_CAPITAL_FUNDS"));

// =========================
// R9
// =========================
obj.setR9_AMOUNT(rs.getBigDecimal("R9_AMOUNT"));
obj.setR9_CAPITAL_FUNDS(rs.getBigDecimal("R9_CAPITAL_FUNDS"));

// =========================
// R10
// =========================
obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));
obj.setR10_CAPITAL_FUNDS(rs.getBigDecimal("R10_CAPITAL_FUNDS"));

// =========================
// R11
// =========================
obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));
obj.setR11_CAPITAL_FUNDS(rs.getBigDecimal("R11_CAPITAL_FUNDS"));

// =========================
// R12
// =========================
obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));
obj.setR12_CAPITAL_FUNDS(rs.getBigDecimal("R12_CAPITAL_FUNDS"));

// =========================
// R13
// =========================
obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));
obj.setR13_CAPITAL_FUNDS(rs.getBigDecimal("R13_CAPITAL_FUNDS"));

// =========================
// R14
// =========================
obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));
obj.setR14_CAPITAL_FUNDS(rs.getBigDecimal("R14_CAPITAL_FUNDS"));

// =========================
// R15
// =========================
obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));
obj.setR15_CAPITAL_FUNDS(rs.getBigDecimal("R15_CAPITAL_FUNDS"));

// =========================
// R16
// =========================
obj.setR16_AMOUNT(rs.getBigDecimal("R16_AMOUNT"));
obj.setR16_CAPITAL_FUNDS(rs.getBigDecimal("R16_CAPITAL_FUNDS"));

// =========================
// R17
// =========================
obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));
obj.setR17_CAPITAL_FUNDS(rs.getBigDecimal("R17_CAPITAL_FUNDS"));

// =========================
// R18
// =========================
obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));
obj.setR18_CAPITAL_FUNDS(rs.getBigDecimal("R18_CAPITAL_FUNDS"));

// =========================
// R19
// =========================
obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));
obj.setR19_CAPITAL_FUNDS(rs.getBigDecimal("R19_CAPITAL_FUNDS"));

// =========================
// R20
// =========================
obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));
obj.setR20_CAPITAL_FUNDS(rs.getBigDecimal("R20_CAPITAL_FUNDS"));


// =========================
// R21
// =========================
obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));
obj.setR21_CAPITAL_FUNDS(rs.getBigDecimal("R21_CAPITAL_FUNDS"));

// =========================
// R22
// =========================
obj.setR22_AMOUNT(rs.getBigDecimal("R22_AMOUNT"));
obj.setR22_CAPITAL_FUNDS(rs.getBigDecimal("R22_CAPITAL_FUNDS"));

// =========================
// R23
// =========================
obj.setR23_AMOUNT(rs.getBigDecimal("R23_AMOUNT"));
obj.setR23_CAPITAL_FUNDS(rs.getBigDecimal("R23_CAPITAL_FUNDS"));

// =========================
// R24
// =========================
obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));
obj.setR24_CAPITAL_FUNDS(rs.getBigDecimal("R24_CAPITAL_FUNDS"));

// =========================
// R25
// =========================
obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));
obj.setR25_CAPITAL_FUNDS(rs.getBigDecimal("R25_CAPITAL_FUNDS"));

// =========================
// R26
// =========================
obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));
obj.setR26_CAPITAL_FUNDS(rs.getBigDecimal("R26_CAPITAL_FUNDS"));

// =========================
// R30
// =========================
obj.setR30_LINE_NO(rs.getBigDecimal("R30_LINE_NO"));
obj.setR30_BORROWER(rs.getBigDecimal("R30_BORROWER"));
obj.setR30_AMOUNT(rs.getBigDecimal("R30_AMOUNT"));
obj.setR30_CAPITAL_FUNDS(rs.getBigDecimal("R30_CAPITAL_FUNDS"));


// =========================
// R31
// =========================
obj.setR31_AMOUNT(rs.getBigDecimal("R31_AMOUNT"));
obj.setR31_CAPITAL_FUNDS(rs.getBigDecimal("R31_CAPITAL_FUNDS"));

// =========================
// R32
// =========================
obj.setR32_AMOUNT(rs.getBigDecimal("R32_AMOUNT"));
obj.setR32_CAPITAL_FUNDS(rs.getBigDecimal("R32_CAPITAL_FUNDS"));

// =========================
// R33
// =========================
obj.setR33_AMOUNT(rs.getBigDecimal("R33_AMOUNT"));
obj.setR33_CAPITAL_FUNDS(rs.getBigDecimal("R33_CAPITAL_FUNDS"));

// =========================
// R34
// =========================
obj.setR34_AMOUNT(rs.getBigDecimal("R34_AMOUNT"));
obj.setR34_CAPITAL_FUNDS(rs.getBigDecimal("R34_CAPITAL_FUNDS"));

// =========================
// R35
// =========================
obj.setR35_AMOUNT(rs.getBigDecimal("R35_AMOUNT"));
obj.setR35_CAPITAL_FUNDS(rs.getBigDecimal("R35_CAPITAL_FUNDS"));

// =========================
// R36
// =========================
obj.setR36_AMOUNT(rs.getBigDecimal("R36_AMOUNT"));
obj.setR36_CAPITAL_FUNDS(rs.getBigDecimal("R36_CAPITAL_FUNDS"));

// =========================
// R37
// =========================
obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));
obj.setR37_CAPITAL_FUNDS(rs.getBigDecimal("R37_CAPITAL_FUNDS"));

// =========================
// R38
// =========================
obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));
obj.setR38_CAPITAL_FUNDS(rs.getBigDecimal("R38_CAPITAL_FUNDS"));

// =========================
// R39
// =========================
obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));
obj.setR39_CAPITAL_FUNDS(rs.getBigDecimal("R39_CAPITAL_FUNDS"));

// =========================
// R40
// =========================
obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));
obj.setR40_CAPITAL_FUNDS(rs.getBigDecimal("R40_CAPITAL_FUNDS"));


// =========================
// R41
// =========================
obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));
obj.setR41_CAPITAL_FUNDS(rs.getBigDecimal("R41_CAPITAL_FUNDS"));

// =========================
// R42
// =========================
obj.setR42_AMOUNT(rs.getBigDecimal("R42_AMOUNT"));
obj.setR42_CAPITAL_FUNDS(rs.getBigDecimal("R42_CAPITAL_FUNDS"));

// =========================
// R43
// =========================
obj.setR43_AMOUNT(rs.getBigDecimal("R43_AMOUNT"));
obj.setR43_CAPITAL_FUNDS(rs.getBigDecimal("R43_CAPITAL_FUNDS"));

// =========================
// R44
// =========================
obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));
obj.setR44_CAPITAL_FUNDS(rs.getBigDecimal("R44_CAPITAL_FUNDS"));

// =========================
// R45
// =========================
obj.setR45_AMOUNT(rs.getBigDecimal("R45_AMOUNT"));
obj.setR45_CAPITAL_FUNDS(rs.getBigDecimal("R45_CAPITAL_FUNDS"));

// =========================
// R46
// =========================
obj.setR46_AMOUNT(rs.getBigDecimal("R46_AMOUNT"));
obj.setR46_CAPITAL_FUNDS(rs.getBigDecimal("R46_CAPITAL_FUNDS"));

// =========================
// R47
// =========================
obj.setR47_AMOUNT(rs.getBigDecimal("R47_AMOUNT"));
obj.setR47_CAPITAL_FUNDS(rs.getBigDecimal("R47_CAPITAL_FUNDS"));

// =========================
// R48
// =========================
obj.setR48_AMOUNT(rs.getBigDecimal("R48_AMOUNT"));
obj.setR48_CAPITAL_FUNDS(rs.getBigDecimal("R48_CAPITAL_FUNDS"));

// =========================
// R49
// =========================
obj.setR49_AMOUNT(rs.getBigDecimal("R49_AMOUNT"));
obj.setR49_CAPITAL_FUNDS(rs.getBigDecimal("R49_CAPITAL_FUNDS"));

// =========================
// R50
// =========================
obj.setR50_AMOUNT(rs.getBigDecimal("R50_AMOUNT"));
obj.setR50_CAPITAL_FUNDS(rs.getBigDecimal("R50_CAPITAL_FUNDS"));


// =========================
// R51
// =========================
obj.setR51_AMOUNT(rs.getBigDecimal("R51_AMOUNT"));
obj.setR51_CAPITAL_FUNDS(rs.getBigDecimal("R51_CAPITAL_FUNDS"));

// =========================
// R55
// =========================
obj.setR55_LINE_NO(rs.getString("R55_LINE_NO"));
obj.setR55_BORROWER(rs.getString("R55_BORROWER"));
obj.setR55_AMOUNT(rs.getString("R55_AMOUNT"));

// =========================
// R56
// =========================
obj.setR56_LINE_NO(rs.getString("R56_LINE_NO"));
obj.setR56_BORROWER(rs.getString("R56_BORROWER"));
obj.setR56_AMOUNT(rs.getString("R56_AMOUNT"));

// =========================
// R57
// =========================
obj.setR57_AMOUNT(rs.getBigDecimal("R57_AMOUNT"));

// =========================
// R58
// =========================
obj.setR58_AMOUNT(rs.getBigDecimal("R58_AMOUNT"));

// =========================
// R59
// =========================
obj.setR59_AMOUNT(rs.getBigDecimal("R59_AMOUNT"));

// =========================
// R60
// =========================
obj.setR60_AMOUNT(rs.getBigDecimal("R60_AMOUNT"));

// =========================
// R61
// =========================
obj.setR61_AMOUNT(rs.getBigDecimal("R61_AMOUNT"));

// =========================
// R62
// =========================
obj.setR62_AMOUNT(rs.getBigDecimal("R62_AMOUNT"));

// =========================
// R63
// =========================
obj.setR63_AMOUNT(rs.getBigDecimal("R63_AMOUNT"));

// =========================
// R64
// =========================
obj.setR64_AMOUNT(rs.getBigDecimal("R64_AMOUNT"));

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


public class EXPOSURES_Archival_Summary_Entity {
	
	
private BigDecimal R4_AMOUNT;

private String R5_LINE_NO;
private String R5_BORROWER;
private String R5_AMOUNT;
private String R5_CAPITAL_FUNDS;

private BigDecimal R6_AMOUNT;
private BigDecimal R6_CAPITAL_FUNDS;

private BigDecimal R7_AMOUNT;
private BigDecimal R7_CAPITAL_FUNDS;

private BigDecimal R8_AMOUNT;
private BigDecimal R8_CAPITAL_FUNDS;

private BigDecimal R9_AMOUNT;
private BigDecimal R9_CAPITAL_FUNDS;

private BigDecimal R10_AMOUNT;
private BigDecimal R10_CAPITAL_FUNDS;

private BigDecimal R11_AMOUNT;
private BigDecimal R11_CAPITAL_FUNDS;

private BigDecimal R12_AMOUNT;
private BigDecimal R12_CAPITAL_FUNDS;

private BigDecimal R13_AMOUNT;
private BigDecimal R13_CAPITAL_FUNDS;

private BigDecimal R14_AMOUNT;
private BigDecimal R14_CAPITAL_FUNDS;

private BigDecimal R15_AMOUNT;
private BigDecimal R15_CAPITAL_FUNDS;

private BigDecimal R16_AMOUNT;
private BigDecimal R16_CAPITAL_FUNDS;

private BigDecimal R17_AMOUNT;
private BigDecimal R17_CAPITAL_FUNDS;

private BigDecimal R18_AMOUNT;
private BigDecimal R18_CAPITAL_FUNDS;

private BigDecimal R19_AMOUNT;
private BigDecimal R19_CAPITAL_FUNDS;

private BigDecimal R20_AMOUNT;
private BigDecimal R20_CAPITAL_FUNDS;

private BigDecimal R21_AMOUNT;
private BigDecimal R21_CAPITAL_FUNDS;

private BigDecimal R22_AMOUNT;
private BigDecimal R22_CAPITAL_FUNDS;

private BigDecimal R23_AMOUNT;
private BigDecimal R23_CAPITAL_FUNDS;

private BigDecimal R24_AMOUNT;
private BigDecimal R24_CAPITAL_FUNDS;

private BigDecimal R25_AMOUNT;
private BigDecimal R25_CAPITAL_FUNDS;

private BigDecimal R26_AMOUNT;
private BigDecimal R26_CAPITAL_FUNDS;

private BigDecimal R30_LINE_NO;
private BigDecimal R30_BORROWER;
private BigDecimal R30_AMOUNT;
private BigDecimal R30_CAPITAL_FUNDS;

private BigDecimal R31_AMOUNT;
private BigDecimal R31_CAPITAL_FUNDS;

private BigDecimal R32_AMOUNT;
private BigDecimal R32_CAPITAL_FUNDS;

private BigDecimal R33_AMOUNT;
private BigDecimal R33_CAPITAL_FUNDS;

private BigDecimal R34_AMOUNT;
private BigDecimal R34_CAPITAL_FUNDS;

private BigDecimal R35_AMOUNT;
private BigDecimal R35_CAPITAL_FUNDS;

private BigDecimal R36_AMOUNT;
private BigDecimal R36_CAPITAL_FUNDS;

private BigDecimal R37_AMOUNT;
private BigDecimal R37_CAPITAL_FUNDS;

private BigDecimal R38_AMOUNT;
private BigDecimal R38_CAPITAL_FUNDS;

private BigDecimal R39_AMOUNT;
private BigDecimal R39_CAPITAL_FUNDS;

private BigDecimal R40_AMOUNT;
private BigDecimal R40_CAPITAL_FUNDS;

private BigDecimal R41_AMOUNT;
private BigDecimal R41_CAPITAL_FUNDS;

private BigDecimal R42_AMOUNT;
private BigDecimal R42_CAPITAL_FUNDS;

private BigDecimal R43_AMOUNT;
private BigDecimal R43_CAPITAL_FUNDS;

private BigDecimal R44_AMOUNT;
private BigDecimal R44_CAPITAL_FUNDS;

private BigDecimal R45_AMOUNT;
private BigDecimal R45_CAPITAL_FUNDS;

private BigDecimal R46_AMOUNT;
private BigDecimal R46_CAPITAL_FUNDS;

private BigDecimal R47_AMOUNT;
private BigDecimal R47_CAPITAL_FUNDS;

private BigDecimal R48_AMOUNT;
private BigDecimal R48_CAPITAL_FUNDS;

private BigDecimal R49_AMOUNT;
private BigDecimal R49_CAPITAL_FUNDS;

private BigDecimal R50_AMOUNT;
private BigDecimal R50_CAPITAL_FUNDS;

private BigDecimal R51_AMOUNT;
private BigDecimal R51_CAPITAL_FUNDS;

private String R55_LINE_NO;
private String R55_BORROWER;
private String R55_AMOUNT;

private String R56_LINE_NO;
private String R56_BORROWER;
private String R56_AMOUNT;

private BigDecimal R57_AMOUNT;
private BigDecimal R58_AMOUNT;
private BigDecimal R59_AMOUNT;
private BigDecimal R60_AMOUNT;
private BigDecimal R61_AMOUNT;
private BigDecimal R62_AMOUNT;
private BigDecimal R63_AMOUNT;
private BigDecimal R64_AMOUNT;
	               
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
	
	public BigDecimal getR4_AMOUNT() {
	return R4_AMOUNT;
}
public void setR4_AMOUNT(BigDecimal r4_AMOUNT) {
	R4_AMOUNT = r4_AMOUNT;
}
public String getR5_LINE_NO() {
	return R5_LINE_NO;
}
public void setR5_LINE_NO(String r5_LINE_NO) {
	R5_LINE_NO = r5_LINE_NO;
}
public String getR5_BORROWER() {
	return R5_BORROWER;
}
public void setR5_BORROWER(String r5_BORROWER) {
	R5_BORROWER = r5_BORROWER;
}
public String getR5_AMOUNT() {
	return R5_AMOUNT;
}
public void setR5_AMOUNT(String r5_AMOUNT) {
	R5_AMOUNT = r5_AMOUNT;
}
public String getR5_CAPITAL_FUNDS() {
	return R5_CAPITAL_FUNDS;
}
public void setR5_CAPITAL_FUNDS(String r5_CAPITAL_FUNDS) {
	R5_CAPITAL_FUNDS = r5_CAPITAL_FUNDS;
}
public BigDecimal getR6_AMOUNT() {
	return R6_AMOUNT;
}
public void setR6_AMOUNT(BigDecimal r6_AMOUNT) {
	R6_AMOUNT = r6_AMOUNT;
}
public BigDecimal getR6_CAPITAL_FUNDS() {
	return R6_CAPITAL_FUNDS;
}
public void setR6_CAPITAL_FUNDS(BigDecimal r6_CAPITAL_FUNDS) {
	R6_CAPITAL_FUNDS = r6_CAPITAL_FUNDS;
}
public BigDecimal getR7_AMOUNT() {
	return R7_AMOUNT;
}
public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
	R7_AMOUNT = r7_AMOUNT;
}
public BigDecimal getR7_CAPITAL_FUNDS() {
	return R7_CAPITAL_FUNDS;
}
public void setR7_CAPITAL_FUNDS(BigDecimal r7_CAPITAL_FUNDS) {
	R7_CAPITAL_FUNDS = r7_CAPITAL_FUNDS;
}
public BigDecimal getR8_AMOUNT() {
	return R8_AMOUNT;
}
public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
	R8_AMOUNT = r8_AMOUNT;
}
public BigDecimal getR8_CAPITAL_FUNDS() {
	return R8_CAPITAL_FUNDS;
}
public void setR8_CAPITAL_FUNDS(BigDecimal r8_CAPITAL_FUNDS) {
	R8_CAPITAL_FUNDS = r8_CAPITAL_FUNDS;
}
public BigDecimal getR9_AMOUNT() {
	return R9_AMOUNT;
}
public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
	R9_AMOUNT = r9_AMOUNT;
}
public BigDecimal getR9_CAPITAL_FUNDS() {
	return R9_CAPITAL_FUNDS;
}
public void setR9_CAPITAL_FUNDS(BigDecimal r9_CAPITAL_FUNDS) {
	R9_CAPITAL_FUNDS = r9_CAPITAL_FUNDS;
}
public BigDecimal getR10_AMOUNT() {
	return R10_AMOUNT;
}
public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
	R10_AMOUNT = r10_AMOUNT;
}
public BigDecimal getR10_CAPITAL_FUNDS() {
	return R10_CAPITAL_FUNDS;
}
public void setR10_CAPITAL_FUNDS(BigDecimal r10_CAPITAL_FUNDS) {
	R10_CAPITAL_FUNDS = r10_CAPITAL_FUNDS;
}
public BigDecimal getR11_AMOUNT() {
	return R11_AMOUNT;
}
public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
	R11_AMOUNT = r11_AMOUNT;
}
public BigDecimal getR11_CAPITAL_FUNDS() {
	return R11_CAPITAL_FUNDS;
}
public void setR11_CAPITAL_FUNDS(BigDecimal r11_CAPITAL_FUNDS) {
	R11_CAPITAL_FUNDS = r11_CAPITAL_FUNDS;
}
public BigDecimal getR12_AMOUNT() {
	return R12_AMOUNT;
}
public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
	R12_AMOUNT = r12_AMOUNT;
}
public BigDecimal getR12_CAPITAL_FUNDS() {
	return R12_CAPITAL_FUNDS;
}
public void setR12_CAPITAL_FUNDS(BigDecimal r12_CAPITAL_FUNDS) {
	R12_CAPITAL_FUNDS = r12_CAPITAL_FUNDS;
}
public BigDecimal getR13_AMOUNT() {
	return R13_AMOUNT;
}
public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
	R13_AMOUNT = r13_AMOUNT;
}
public BigDecimal getR13_CAPITAL_FUNDS() {
	return R13_CAPITAL_FUNDS;
}
public void setR13_CAPITAL_FUNDS(BigDecimal r13_CAPITAL_FUNDS) {
	R13_CAPITAL_FUNDS = r13_CAPITAL_FUNDS;
}
public BigDecimal getR14_AMOUNT() {
	return R14_AMOUNT;
}
public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
	R14_AMOUNT = r14_AMOUNT;
}
public BigDecimal getR14_CAPITAL_FUNDS() {
	return R14_CAPITAL_FUNDS;
}
public void setR14_CAPITAL_FUNDS(BigDecimal r14_CAPITAL_FUNDS) {
	R14_CAPITAL_FUNDS = r14_CAPITAL_FUNDS;
}
public BigDecimal getR15_AMOUNT() {
	return R15_AMOUNT;
}
public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
	R15_AMOUNT = r15_AMOUNT;
}
public BigDecimal getR15_CAPITAL_FUNDS() {
	return R15_CAPITAL_FUNDS;
}
public void setR15_CAPITAL_FUNDS(BigDecimal r15_CAPITAL_FUNDS) {
	R15_CAPITAL_FUNDS = r15_CAPITAL_FUNDS;
}
public BigDecimal getR16_AMOUNT() {
	return R16_AMOUNT;
}
public void setR16_AMOUNT(BigDecimal r16_AMOUNT) {
	R16_AMOUNT = r16_AMOUNT;
}
public BigDecimal getR16_CAPITAL_FUNDS() {
	return R16_CAPITAL_FUNDS;
}
public void setR16_CAPITAL_FUNDS(BigDecimal r16_CAPITAL_FUNDS) {
	R16_CAPITAL_FUNDS = r16_CAPITAL_FUNDS;
}
public BigDecimal getR17_AMOUNT() {
	return R17_AMOUNT;
}
public void setR17_AMOUNT(BigDecimal r17_AMOUNT) {
	R17_AMOUNT = r17_AMOUNT;
}
public BigDecimal getR17_CAPITAL_FUNDS() {
	return R17_CAPITAL_FUNDS;
}
public void setR17_CAPITAL_FUNDS(BigDecimal r17_CAPITAL_FUNDS) {
	R17_CAPITAL_FUNDS = r17_CAPITAL_FUNDS;
}
public BigDecimal getR18_AMOUNT() {
	return R18_AMOUNT;
}
public void setR18_AMOUNT(BigDecimal r18_AMOUNT) {
	R18_AMOUNT = r18_AMOUNT;
}
public BigDecimal getR18_CAPITAL_FUNDS() {
	return R18_CAPITAL_FUNDS;
}
public void setR18_CAPITAL_FUNDS(BigDecimal r18_CAPITAL_FUNDS) {
	R18_CAPITAL_FUNDS = r18_CAPITAL_FUNDS;
}
public BigDecimal getR19_AMOUNT() {
	return R19_AMOUNT;
}
public void setR19_AMOUNT(BigDecimal r19_AMOUNT) {
	R19_AMOUNT = r19_AMOUNT;
}
public BigDecimal getR19_CAPITAL_FUNDS() {
	return R19_CAPITAL_FUNDS;
}
public void setR19_CAPITAL_FUNDS(BigDecimal r19_CAPITAL_FUNDS) {
	R19_CAPITAL_FUNDS = r19_CAPITAL_FUNDS;
}
public BigDecimal getR20_AMOUNT() {
	return R20_AMOUNT;
}
public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
	R20_AMOUNT = r20_AMOUNT;
}
public BigDecimal getR20_CAPITAL_FUNDS() {
	return R20_CAPITAL_FUNDS;
}
public void setR20_CAPITAL_FUNDS(BigDecimal r20_CAPITAL_FUNDS) {
	R20_CAPITAL_FUNDS = r20_CAPITAL_FUNDS;
}
public BigDecimal getR21_AMOUNT() {
	return R21_AMOUNT;
}
public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
	R21_AMOUNT = r21_AMOUNT;
}
public BigDecimal getR21_CAPITAL_FUNDS() {
	return R21_CAPITAL_FUNDS;
}
public void setR21_CAPITAL_FUNDS(BigDecimal r21_CAPITAL_FUNDS) {
	R21_CAPITAL_FUNDS = r21_CAPITAL_FUNDS;
}
public BigDecimal getR22_AMOUNT() {
	return R22_AMOUNT;
}
public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
	R22_AMOUNT = r22_AMOUNT;
}
public BigDecimal getR22_CAPITAL_FUNDS() {
	return R22_CAPITAL_FUNDS;
}
public void setR22_CAPITAL_FUNDS(BigDecimal r22_CAPITAL_FUNDS) {
	R22_CAPITAL_FUNDS = r22_CAPITAL_FUNDS;
}
public BigDecimal getR23_AMOUNT() {
	return R23_AMOUNT;
}
public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
	R23_AMOUNT = r23_AMOUNT;
}
public BigDecimal getR23_CAPITAL_FUNDS() {
	return R23_CAPITAL_FUNDS;
}
public void setR23_CAPITAL_FUNDS(BigDecimal r23_CAPITAL_FUNDS) {
	R23_CAPITAL_FUNDS = r23_CAPITAL_FUNDS;
}
public BigDecimal getR24_AMOUNT() {
	return R24_AMOUNT;
}
public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
	R24_AMOUNT = r24_AMOUNT;
}
public BigDecimal getR24_CAPITAL_FUNDS() {
	return R24_CAPITAL_FUNDS;
}
public void setR24_CAPITAL_FUNDS(BigDecimal r24_CAPITAL_FUNDS) {
	R24_CAPITAL_FUNDS = r24_CAPITAL_FUNDS;
}
public BigDecimal getR25_AMOUNT() {
	return R25_AMOUNT;
}
public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
	R25_AMOUNT = r25_AMOUNT;
}
public BigDecimal getR25_CAPITAL_FUNDS() {
	return R25_CAPITAL_FUNDS;
}
public void setR25_CAPITAL_FUNDS(BigDecimal r25_CAPITAL_FUNDS) {
	R25_CAPITAL_FUNDS = r25_CAPITAL_FUNDS;
}
public BigDecimal getR26_AMOUNT() {
	return R26_AMOUNT;
}
public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
	R26_AMOUNT = r26_AMOUNT;
}
public BigDecimal getR26_CAPITAL_FUNDS() {
	return R26_CAPITAL_FUNDS;
}
public void setR26_CAPITAL_FUNDS(BigDecimal r26_CAPITAL_FUNDS) {
	R26_CAPITAL_FUNDS = r26_CAPITAL_FUNDS;
}
public BigDecimal getR30_LINE_NO() {
	return R30_LINE_NO;
}
public void setR30_LINE_NO(BigDecimal r30_LINE_NO) {
	R30_LINE_NO = r30_LINE_NO;
}
public BigDecimal getR30_BORROWER() {
	return R30_BORROWER;
}
public void setR30_BORROWER(BigDecimal r30_BORROWER) {
	R30_BORROWER = r30_BORROWER;
}
public BigDecimal getR30_AMOUNT() {
	return R30_AMOUNT;
}
public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
	R30_AMOUNT = r30_AMOUNT;
}
public BigDecimal getR30_CAPITAL_FUNDS() {
	return R30_CAPITAL_FUNDS;
}
public void setR30_CAPITAL_FUNDS(BigDecimal r30_CAPITAL_FUNDS) {
	R30_CAPITAL_FUNDS = r30_CAPITAL_FUNDS;
}
public BigDecimal getR31_AMOUNT() {
	return R31_AMOUNT;
}
public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
	R31_AMOUNT = r31_AMOUNT;
}
public BigDecimal getR31_CAPITAL_FUNDS() {
	return R31_CAPITAL_FUNDS;
}
public void setR31_CAPITAL_FUNDS(BigDecimal r31_CAPITAL_FUNDS) {
	R31_CAPITAL_FUNDS = r31_CAPITAL_FUNDS;
}
public BigDecimal getR32_AMOUNT() {
	return R32_AMOUNT;
}
public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
	R32_AMOUNT = r32_AMOUNT;
}
public BigDecimal getR32_CAPITAL_FUNDS() {
	return R32_CAPITAL_FUNDS;
}
public void setR32_CAPITAL_FUNDS(BigDecimal r32_CAPITAL_FUNDS) {
	R32_CAPITAL_FUNDS = r32_CAPITAL_FUNDS;
}
public BigDecimal getR33_AMOUNT() {
	return R33_AMOUNT;
}
public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
	R33_AMOUNT = r33_AMOUNT;
}
public BigDecimal getR33_CAPITAL_FUNDS() {
	return R33_CAPITAL_FUNDS;
}
public void setR33_CAPITAL_FUNDS(BigDecimal r33_CAPITAL_FUNDS) {
	R33_CAPITAL_FUNDS = r33_CAPITAL_FUNDS;
}
public BigDecimal getR34_AMOUNT() {
	return R34_AMOUNT;
}
public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
	R34_AMOUNT = r34_AMOUNT;
}
public BigDecimal getR34_CAPITAL_FUNDS() {
	return R34_CAPITAL_FUNDS;
}
public void setR34_CAPITAL_FUNDS(BigDecimal r34_CAPITAL_FUNDS) {
	R34_CAPITAL_FUNDS = r34_CAPITAL_FUNDS;
}
public BigDecimal getR35_AMOUNT() {
	return R35_AMOUNT;
}
public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
	R35_AMOUNT = r35_AMOUNT;
}
public BigDecimal getR35_CAPITAL_FUNDS() {
	return R35_CAPITAL_FUNDS;
}
public void setR35_CAPITAL_FUNDS(BigDecimal r35_CAPITAL_FUNDS) {
	R35_CAPITAL_FUNDS = r35_CAPITAL_FUNDS;
}
public BigDecimal getR36_AMOUNT() {
	return R36_AMOUNT;
}
public void setR36_AMOUNT(BigDecimal r36_AMOUNT) {
	R36_AMOUNT = r36_AMOUNT;
}
public BigDecimal getR36_CAPITAL_FUNDS() {
	return R36_CAPITAL_FUNDS;
}
public void setR36_CAPITAL_FUNDS(BigDecimal r36_CAPITAL_FUNDS) {
	R36_CAPITAL_FUNDS = r36_CAPITAL_FUNDS;
}
public BigDecimal getR37_AMOUNT() {
	return R37_AMOUNT;
}
public void setR37_AMOUNT(BigDecimal r37_AMOUNT) {
	R37_AMOUNT = r37_AMOUNT;
}
public BigDecimal getR37_CAPITAL_FUNDS() {
	return R37_CAPITAL_FUNDS;
}
public void setR37_CAPITAL_FUNDS(BigDecimal r37_CAPITAL_FUNDS) {
	R37_CAPITAL_FUNDS = r37_CAPITAL_FUNDS;
}
public BigDecimal getR38_AMOUNT() {
	return R38_AMOUNT;
}
public void setR38_AMOUNT(BigDecimal r38_AMOUNT) {
	R38_AMOUNT = r38_AMOUNT;
}
public BigDecimal getR38_CAPITAL_FUNDS() {
	return R38_CAPITAL_FUNDS;
}
public void setR38_CAPITAL_FUNDS(BigDecimal r38_CAPITAL_FUNDS) {
	R38_CAPITAL_FUNDS = r38_CAPITAL_FUNDS;
}
public BigDecimal getR39_AMOUNT() {
	return R39_AMOUNT;
}
public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
	R39_AMOUNT = r39_AMOUNT;
}
public BigDecimal getR39_CAPITAL_FUNDS() {
	return R39_CAPITAL_FUNDS;
}
public void setR39_CAPITAL_FUNDS(BigDecimal r39_CAPITAL_FUNDS) {
	R39_CAPITAL_FUNDS = r39_CAPITAL_FUNDS;
}
public BigDecimal getR40_AMOUNT() {
	return R40_AMOUNT;
}
public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
	R40_AMOUNT = r40_AMOUNT;
}
public BigDecimal getR40_CAPITAL_FUNDS() {
	return R40_CAPITAL_FUNDS;
}
public void setR40_CAPITAL_FUNDS(BigDecimal r40_CAPITAL_FUNDS) {
	R40_CAPITAL_FUNDS = r40_CAPITAL_FUNDS;
}
public BigDecimal getR41_AMOUNT() {
	return R41_AMOUNT;
}
public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
	R41_AMOUNT = r41_AMOUNT;
}
public BigDecimal getR41_CAPITAL_FUNDS() {
	return R41_CAPITAL_FUNDS;
}
public void setR41_CAPITAL_FUNDS(BigDecimal r41_CAPITAL_FUNDS) {
	R41_CAPITAL_FUNDS = r41_CAPITAL_FUNDS;
}
public BigDecimal getR42_AMOUNT() {
	return R42_AMOUNT;
}
public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
	R42_AMOUNT = r42_AMOUNT;
}
public BigDecimal getR42_CAPITAL_FUNDS() {
	return R42_CAPITAL_FUNDS;
}
public void setR42_CAPITAL_FUNDS(BigDecimal r42_CAPITAL_FUNDS) {
	R42_CAPITAL_FUNDS = r42_CAPITAL_FUNDS;
}
public BigDecimal getR43_AMOUNT() {
	return R43_AMOUNT;
}
public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
	R43_AMOUNT = r43_AMOUNT;
}
public BigDecimal getR43_CAPITAL_FUNDS() {
	return R43_CAPITAL_FUNDS;
}
public void setR43_CAPITAL_FUNDS(BigDecimal r43_CAPITAL_FUNDS) {
	R43_CAPITAL_FUNDS = r43_CAPITAL_FUNDS;
}
public BigDecimal getR44_AMOUNT() {
	return R44_AMOUNT;
}
public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
	R44_AMOUNT = r44_AMOUNT;
}
public BigDecimal getR44_CAPITAL_FUNDS() {
	return R44_CAPITAL_FUNDS;
}
public void setR44_CAPITAL_FUNDS(BigDecimal r44_CAPITAL_FUNDS) {
	R44_CAPITAL_FUNDS = r44_CAPITAL_FUNDS;
}
public BigDecimal getR45_AMOUNT() {
	return R45_AMOUNT;
}
public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
	R45_AMOUNT = r45_AMOUNT;
}
public BigDecimal getR45_CAPITAL_FUNDS() {
	return R45_CAPITAL_FUNDS;
}
public void setR45_CAPITAL_FUNDS(BigDecimal r45_CAPITAL_FUNDS) {
	R45_CAPITAL_FUNDS = r45_CAPITAL_FUNDS;
}
public BigDecimal getR46_AMOUNT() {
	return R46_AMOUNT;
}
public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
	R46_AMOUNT = r46_AMOUNT;
}
public BigDecimal getR46_CAPITAL_FUNDS() {
	return R46_CAPITAL_FUNDS;
}
public void setR46_CAPITAL_FUNDS(BigDecimal r46_CAPITAL_FUNDS) {
	R46_CAPITAL_FUNDS = r46_CAPITAL_FUNDS;
}
public BigDecimal getR47_AMOUNT() {
	return R47_AMOUNT;
}
public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
	R47_AMOUNT = r47_AMOUNT;
}
public BigDecimal getR47_CAPITAL_FUNDS() {
	return R47_CAPITAL_FUNDS;
}
public void setR47_CAPITAL_FUNDS(BigDecimal r47_CAPITAL_FUNDS) {
	R47_CAPITAL_FUNDS = r47_CAPITAL_FUNDS;
}
public BigDecimal getR48_AMOUNT() {
	return R48_AMOUNT;
}
public void setR48_AMOUNT(BigDecimal r48_AMOUNT) {
	R48_AMOUNT = r48_AMOUNT;
}
public BigDecimal getR48_CAPITAL_FUNDS() {
	return R48_CAPITAL_FUNDS;
}
public void setR48_CAPITAL_FUNDS(BigDecimal r48_CAPITAL_FUNDS) {
	R48_CAPITAL_FUNDS = r48_CAPITAL_FUNDS;
}
public BigDecimal getR49_AMOUNT() {
	return R49_AMOUNT;
}
public void setR49_AMOUNT(BigDecimal r49_AMOUNT) {
	R49_AMOUNT = r49_AMOUNT;
}
public BigDecimal getR49_CAPITAL_FUNDS() {
	return R49_CAPITAL_FUNDS;
}
public void setR49_CAPITAL_FUNDS(BigDecimal r49_CAPITAL_FUNDS) {
	R49_CAPITAL_FUNDS = r49_CAPITAL_FUNDS;
}
public BigDecimal getR50_AMOUNT() {
	return R50_AMOUNT;
}
public void setR50_AMOUNT(BigDecimal r50_AMOUNT) {
	R50_AMOUNT = r50_AMOUNT;
}
public BigDecimal getR50_CAPITAL_FUNDS() {
	return R50_CAPITAL_FUNDS;
}
public void setR50_CAPITAL_FUNDS(BigDecimal r50_CAPITAL_FUNDS) {
	R50_CAPITAL_FUNDS = r50_CAPITAL_FUNDS;
}
public BigDecimal getR51_AMOUNT() {
	return R51_AMOUNT;
}
public void setR51_AMOUNT(BigDecimal r51_AMOUNT) {
	R51_AMOUNT = r51_AMOUNT;
}
public BigDecimal getR51_CAPITAL_FUNDS() {
	return R51_CAPITAL_FUNDS;
}
public void setR51_CAPITAL_FUNDS(BigDecimal r51_CAPITAL_FUNDS) {
	R51_CAPITAL_FUNDS = r51_CAPITAL_FUNDS;
}
public String getR55_LINE_NO() {
	return R55_LINE_NO;
}
public void setR55_LINE_NO(String r55_LINE_NO) {
	R55_LINE_NO = r55_LINE_NO;
}
public String getR55_BORROWER() {
	return R55_BORROWER;
}
public void setR55_BORROWER(String r55_BORROWER) {
	R55_BORROWER = r55_BORROWER;
}
public String getR55_AMOUNT() {
	return R55_AMOUNT;
}
public void setR55_AMOUNT(String r55_AMOUNT) {
	R55_AMOUNT = r55_AMOUNT;
}
public String getR56_LINE_NO() {
	return R56_LINE_NO;
}
public void setR56_LINE_NO(String r56_LINE_NO) {
	R56_LINE_NO = r56_LINE_NO;
}
public String getR56_BORROWER() {
	return R56_BORROWER;
}
public void setR56_BORROWER(String r56_BORROWER) {
	R56_BORROWER = r56_BORROWER;
}
public String getR56_AMOUNT() {
	return R56_AMOUNT;
}
public void setR56_AMOUNT(String r56_AMOUNT) {
	R56_AMOUNT = r56_AMOUNT;
}
public BigDecimal getR57_AMOUNT() {
	return R57_AMOUNT;
}
public void setR57_AMOUNT(BigDecimal r57_AMOUNT) {
	R57_AMOUNT = r57_AMOUNT;
}
public BigDecimal getR58_AMOUNT() {
	return R58_AMOUNT;
}
public void setR58_AMOUNT(BigDecimal r58_AMOUNT) {
	R58_AMOUNT = r58_AMOUNT;
}
public BigDecimal getR59_AMOUNT() {
	return R59_AMOUNT;
}
public void setR59_AMOUNT(BigDecimal r59_AMOUNT) {
	R59_AMOUNT = r59_AMOUNT;
}
public BigDecimal getR60_AMOUNT() {
	return R60_AMOUNT;
}
public void setR60_AMOUNT(BigDecimal r60_AMOUNT) {
	R60_AMOUNT = r60_AMOUNT;
}
public BigDecimal getR61_AMOUNT() {
	return R61_AMOUNT;
}
public void setR61_AMOUNT(BigDecimal r61_AMOUNT) {
	R61_AMOUNT = r61_AMOUNT;
}
public BigDecimal getR62_AMOUNT() {
	return R62_AMOUNT;
}
public void setR62_AMOUNT(BigDecimal r62_AMOUNT) {
	R62_AMOUNT = r62_AMOUNT;
}
public BigDecimal getR63_AMOUNT() {
	return R63_AMOUNT;
}
public void setR63_AMOUNT(BigDecimal r63_AMOUNT) {
	R63_AMOUNT = r63_AMOUNT;
}
public BigDecimal getR64_AMOUNT() {
	return R64_AMOUNT;
}
public void setR64_AMOUNT(BigDecimal r64_AMOUNT) {
	R64_AMOUNT = r64_AMOUNT;
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
// DETAIL ENTITY  EXPOSURES
// =====================================================	

public class EXPOSURES_Detail_RowMapper implements RowMapper<EXPOSURES_Detail_Entity> {

    @Override
    public EXPOSURES_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        EXPOSURES_Detail_Entity obj = new EXPOSURES_Detail_Entity();

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

public class EXPOSURES_Detail_Entity {

   
	
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


public class EXPOSURES_Archival_Detail_RowMapper 
        implements RowMapper<EXPOSURES_Archival_Detail_Entity> {

    @Override
    public EXPOSURES_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        EXPOSURES_Archival_Detail_Entity obj = new EXPOSURES_Archival_Detail_Entity();

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

public class EXPOSURES_Archival_Detail_Entity {

   
	
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
// MODEL AND VIEW METHOD summary EXPOSURES
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
		 		 	 public ModelAndView getEXPOSURESView(

	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("EXPOSURES View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    // =====================================================
	    // ARCHIVAL MODE
	    // =====================================================

	    if ("ARCHIVAL".equals(type) && version != null) {

	        List<EXPOSURES_Archival_Summary_Entity> T1Master = new ArrayList<>();
	    

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

	        List<EXPOSURES_Summary_Entity> T1Master = new ArrayList<>();
	       

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

	    mv.setViewName("BRRS/EXPOSURES");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getEXPOSUREScurrentDtl(
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

	            List<EXPOSURES_Archival_Detail_Entity> archivalDetailList;

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

	            List<EXPOSURES_Detail_Entity> currentDetailList;

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

	    mv.setViewName("BRRS/EXPOSURES");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
	
//=====================================================
// MODEL AND VIEW METHOD ARCHIVAL VIEW
//=====================================================

public List<Object[]> getEXPOSURESArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<EXPOSURES_Archival_Summary_Entity> repoData =
						getarchivaldatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (EXPOSURES_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					EXPOSURES_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  EXPOSURES  Archival data: " + e.getMessage());
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
		ModelAndView mv = new ModelAndView("BRRS/EXPOSURES"); 

		if (acctNo != null) {
			EXPOSURES_Detail_Entity fsiEntity = findByDetailAcctnumber(acctNo);
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

			EXPOSURES_Detail_Entity existing = findByDetailAcctnumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			 // Create old copy for audit comparison
			EXPOSURES_Detail_Entity oldcopy = new EXPOSURES_Detail_Entity();
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
    "UPDATE BRRS_EXPOSURES_DETAILTABLE " +
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
		                    "EXPOSURES Detail Screen",
		                    "BRRS_EXPOSURES_DETAILTABLE"
		            ); 
		           
				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_EXPOSURES_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_EXPOSURES_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating EXPOSURES  record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
//=====================================================
// DETAIL EXCEL 
//=====================================================


	public byte[] getEXPOSURESDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  EXPOSURES  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getEXPOSURESDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("EXPOSURES Details ");

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
				List<EXPOSURES_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (EXPOSURES_Detail_Entity item : reportData) { 
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
					logger.info("No data found for EXPOSURES — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating EXPOSURES Excel", e);
				return new byte[0];
			}
		}
	
	
//=====================================================
// ARCHIVAL DETAIL EXCEL 
//=====================================================

	public byte[] getEXPOSURESDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for EXPOSURES ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("EXPOSURES Detail NEW");

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
				List<EXPOSURES_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (EXPOSURES_Archival_Detail_Entity item : reportData) {
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
					logger.info("No data found for EXPOSURES — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating EXPOSURES NEW Excel", e);
				return new byte[0];
			}
		}
		
		
//=====================================================
// Summary EXCEL 
//=====================================================

	public byte[] getEXPOSURESExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.EXPOSURES");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelEXPOSURESARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<EXPOSURES_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		
		
			System.out.println("DATA SIZE IS : "+dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  EXPOSURES report. Returning empty result.");
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

						int startRow = 3;
						
				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						EXPOSURES_Summary_Entity record = dataList.get(i);
					
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

		  Cell cell2 = row.createCell(2);
if (record.getR4_AMOUNT() != null) {
 cell2.setCellValue(record.getR4_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

row = sheet.getRow(5);
cell2 = row.createCell(2);
if (record.getR6_AMOUNT() != null) {
 cell2.setCellValue(record.getR6_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

Cell cell3 = row.createCell(3);
if (record.getR6_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR6_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}

//===== R7 =====
row = sheet.getRow(6);
cell2 = row.createCell(2);
if (record.getR7_AMOUNT() != null) {
 cell2.setCellValue(record.getR7_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR7_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR7_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R8 =====
row = sheet.getRow(7);
cell2 = row.createCell(2);
if (record.getR8_AMOUNT() != null) {
 cell2.setCellValue(record.getR8_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR8_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR8_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R9 =====
row = sheet.getRow(8);
cell2 = row.createCell(2);
if (record.getR9_AMOUNT() != null) {
 cell2.setCellValue(record.getR9_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR9_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR9_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R10 =====
row = sheet.getRow(9);
cell2 = row.createCell(2);
if (record.getR10_AMOUNT() != null) {
 cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR10_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR10_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R11 =====
row = sheet.getRow(10);
cell2 = row.createCell(2);
if (record.getR11_AMOUNT() != null) {
 cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR11_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR11_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R12 =====
row = sheet.getRow(11);
cell2 = row.createCell(2);
if (record.getR12_AMOUNT() != null) {
 cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR12_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR12_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R13 =====
row = sheet.getRow(12);
cell2 = row.createCell(2);
if (record.getR13_AMOUNT() != null) {
 cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR13_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR13_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R14 =====
row = sheet.getRow(13);
cell2 = row.createCell(2);
if (record.getR14_AMOUNT() != null) {
 cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR14_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR14_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R15 =====
row = sheet.getRow(14);
cell2 = row.createCell(2);
if (record.getR15_AMOUNT() != null) {
 cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR15_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR15_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R16 =====
row = sheet.getRow(15);
cell2 = row.createCell(2);
if (record.getR16_AMOUNT() != null) {
 cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR16_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR16_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R17 =====
row = sheet.getRow(16);
cell2 = row.createCell(2);
if (record.getR17_AMOUNT() != null) {
 cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR17_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR17_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R18 =====
row = sheet.getRow(17);
cell2 = row.createCell(2);
if (record.getR18_AMOUNT() != null) {
 cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR18_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR18_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R19 =====
row = sheet.getRow(18);
cell2 = row.createCell(2);
if (record.getR19_AMOUNT() != null) {
 cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR19_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR19_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R20 =====
row = sheet.getRow(19);
cell2 = row.createCell(2);
if (record.getR20_AMOUNT() != null) {
 cell2.setCellValue(record.getR20_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR20_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR20_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R21 =====
row = sheet.getRow(20);
cell2 = row.createCell(2);
if (record.getR21_AMOUNT() != null) {
 cell2.setCellValue(record.getR21_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR21_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR21_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R22 =====
row = sheet.getRow(21);
cell2 = row.createCell(2);
if (record.getR22_AMOUNT() != null) {
 cell2.setCellValue(record.getR22_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR22_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR22_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R23 =====
row = sheet.getRow(22);
cell2 = row.createCell(2);
if (record.getR23_AMOUNT() != null) {
 cell2.setCellValue(record.getR23_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR23_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR23_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R24 =====
row = sheet.getRow(23);
cell2 = row.createCell(2);
if (record.getR24_AMOUNT() != null) {
 cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR24_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR24_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R25 =====
row = sheet.getRow(24);
cell2 = row.createCell(2);
if (record.getR25_AMOUNT() != null) {
 cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR25_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR25_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R31 =====
row = sheet.getRow(30);
cell2 = row.createCell(2);
if (record.getR31_AMOUNT() != null) {
 cell2.setCellValue(record.getR31_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR31_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR31_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R32 =====
row = sheet.getRow(31);
cell2 = row.createCell(2);
if (record.getR32_AMOUNT() != null) {
 cell2.setCellValue(record.getR32_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR32_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR32_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R33 =====
row = sheet.getRow(32);
cell2 = row.createCell(2);
if (record.getR33_AMOUNT() != null) {
 cell2.setCellValue(record.getR33_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR33_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR33_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R34 =====
row = sheet.getRow(33);
cell2 = row.createCell(2);
if (record.getR34_AMOUNT() != null) {
 cell2.setCellValue(record.getR34_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR34_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR34_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R35 =====
row = sheet.getRow(34);
cell2 = row.createCell(2);
if (record.getR35_AMOUNT() != null) {
 cell2.setCellValue(record.getR35_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR35_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR35_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R36 =====
row = sheet.getRow(35);
cell2 = row.createCell(2);
if (record.getR36_AMOUNT() != null) {
 cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR36_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR36_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R37 =====
row = sheet.getRow(36);
cell2 = row.createCell(2);
if (record.getR37_AMOUNT() != null) {
 cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR37_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR37_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R38 =====
row = sheet.getRow(37);
cell2 = row.createCell(2);
if (record.getR38_AMOUNT() != null) {
 cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR38_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR38_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R39 =====
row = sheet.getRow(38);
cell2 = row.createCell(2);
if (record.getR39_AMOUNT() != null) {
 cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR39_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR39_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R40 =====
row = sheet.getRow(39);
cell2 = row.createCell(2);
if (record.getR40_AMOUNT() != null) {
 cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR40_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR40_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R41 =====
row = sheet.getRow(40);
cell2 = row.createCell(2);
if (record.getR41_AMOUNT() != null) {
 cell2.setCellValue(record.getR41_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR41_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR41_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R42 =====
row = sheet.getRow(41);
cell2 = row.createCell(2);
if (record.getR42_AMOUNT() != null) {
 cell2.setCellValue(record.getR42_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR42_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR42_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R43 =====
row = sheet.getRow(42);
cell2 = row.createCell(2);
if (record.getR43_AMOUNT() != null) {
 cell2.setCellValue(record.getR43_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR43_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR43_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R44 =====
row = sheet.getRow(43);
cell2 = row.createCell(2);
if (record.getR44_AMOUNT() != null) {
 cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR44_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR44_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R45 =====
row = sheet.getRow(44);
cell2 = row.createCell(2);
if (record.getR45_AMOUNT() != null) {
 cell2.setCellValue(record.getR45_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR45_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR45_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R46 =====
row = sheet.getRow(45);
cell2 = row.createCell(2);
if (record.getR46_AMOUNT() != null) {
 cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR46_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR46_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R47 =====
row = sheet.getRow(46);
cell2 = row.createCell(2);
if (record.getR47_AMOUNT() != null) {
 cell2.setCellValue(record.getR47_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR47_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR47_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R48 =====
row = sheet.getRow(47);
cell2 = row.createCell(2);
if (record.getR48_AMOUNT() != null) {
 cell2.setCellValue(record.getR48_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR48_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR48_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R49 =====
row = sheet.getRow(48);
cell2 = row.createCell(2);
if (record.getR49_AMOUNT() != null) {
 cell2.setCellValue(record.getR49_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR49_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR49_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R50 =====
row = sheet.getRow(49);
cell2 = row.createCell(2);
if (record.getR50_AMOUNT() != null) {
 cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR50_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR50_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}

//===== R58 =====
row = sheet.getRow(57);
cell3 = row.createCell(3);
if (record.getR58_AMOUNT() != null) {
 cell3.setCellValue(record.getR58_AMOUNT().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R59 =====
row = sheet.getRow(58);
cell3 = row.createCell(3);
if (record.getR59_AMOUNT() != null) {
 cell3.setCellValue(record.getR59_AMOUNT().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R60 =====
row = sheet.getRow(59);
cell3 = row.createCell(3);
if (record.getR60_AMOUNT() != null) {
 cell3.setCellValue(record.getR60_AMOUNT().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R61 =====
row = sheet.getRow(61);
cell3 = row.createCell(3);
if (record.getR62_AMOUNT() != null) {
 cell3.setCellValue(record.getR62_AMOUNT().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
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
												auditService.createBusinessAudit(userid, "DOWNLOAD", "EXPOSURES  SUMMARY", null, "BRRS_EXPOSURES_SUMMARYTABLE");
											}

				return out.toByteArray();
			}

		}




//=====================================================
//ARCHIVAL SUMMARY EXCEL 
//=====================================================



				public byte[] getExcelEXPOSURESARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {	

			}

			List<EXPOSURES_Archival_Summary_Entity> dataList = 
					getDataByDateListArchival(dateformat.parse(todate), version);
		
	    

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for EXPOSURES new report. Returning empty result.");
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

				int startRow = 3;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						EXPOSURES_Archival_Summary_Entity record1 = dataList.get(i);
					    
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


	
						Cell cell2 = row.createCell(2);
				if (record1.getR4_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR4_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				row = sheet.getRow(5);
				cell2 = row.createCell(2);
				if (record1.getR6_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR6_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				Cell cell3 = row.createCell(3);
				if (record1.getR6_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR6_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}

				//===== R7 =====
				row = sheet.getRow(6);
				cell2 = row.createCell(2);
				if (record1.getR7_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR7_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR7_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR7_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R8 =====
				row = sheet.getRow(7);
				cell2 = row.createCell(2);
				if (record1.getR8_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR8_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR8_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR8_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R9 =====
				row = sheet.getRow(8);
				cell2 = row.createCell(2);
				if (record1.getR9_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR9_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR9_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR9_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R10 =====
				row = sheet.getRow(9);
				cell2 = row.createCell(2);
				if (record1.getR10_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR10_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR10_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR10_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R11 =====
				row = sheet.getRow(10);
				cell2 = row.createCell(2);
				if (record1.getR11_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR11_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR11_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR11_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R12 =====
				row = sheet.getRow(11);
				cell2 = row.createCell(2);
				if (record1.getR12_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR12_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR12_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR12_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R13 =====
				row = sheet.getRow(12);
				cell2 = row.createCell(2);
				if (record1.getR13_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR13_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR13_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR13_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R14 =====
				row = sheet.getRow(13);
				cell2 = row.createCell(2);
				if (record1.getR14_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR14_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR14_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR14_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R15 =====
				row = sheet.getRow(14);
				cell2 = row.createCell(2);
				if (record1.getR15_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR15_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR15_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR15_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R16 =====
				row = sheet.getRow(15);
				cell2 = row.createCell(2);
				if (record1.getR16_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR16_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR16_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR16_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R17 =====
				row = sheet.getRow(16);
				cell2 = row.createCell(2);
				if (record1.getR17_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR17_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR17_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR17_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R18 =====
				row = sheet.getRow(17);
				cell2 = row.createCell(2);
				if (record1.getR18_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR18_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR18_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR18_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R19 =====
				row = sheet.getRow(18);
				cell2 = row.createCell(2);
				if (record1.getR19_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR19_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR19_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR19_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R20 =====
				row = sheet.getRow(19);
				cell2 = row.createCell(2);
				if (record1.getR20_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR20_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR20_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR20_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R21 =====
				row = sheet.getRow(20);
				cell2 = row.createCell(2);
				if (record1.getR21_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR21_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR21_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR21_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R22 =====
				row = sheet.getRow(21);
				cell2 = row.createCell(2);
				if (record1.getR22_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR22_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR22_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR22_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R23 =====
				row = sheet.getRow(22);
				cell2 = row.createCell(2);
				if (record1.getR23_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR23_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR23_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR23_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R24 =====
				row = sheet.getRow(23);
				cell2 = row.createCell(2);
				if (record1.getR24_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR24_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR24_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR24_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R25 =====
				row = sheet.getRow(24);
				cell2 = row.createCell(2);
				if (record1.getR25_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR25_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR25_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR25_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R31 =====
				row = sheet.getRow(30);
				cell2 = row.createCell(2);
				if (record1.getR31_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR31_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR31_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR31_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R32 =====
				row = sheet.getRow(31);
				cell2 = row.createCell(2);
				if (record1.getR32_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR32_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR32_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR32_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R33 =====
				row = sheet.getRow(32);
				cell2 = row.createCell(2);
				if (record1.getR33_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR33_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR33_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR33_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R34 =====
				row = sheet.getRow(33);
				cell2 = row.createCell(2);
				if (record1.getR34_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR34_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR34_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR34_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R35 =====
				row = sheet.getRow(34);
				cell2 = row.createCell(2);
				if (record1.getR35_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR35_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR35_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR35_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R36 =====
				row = sheet.getRow(35);
				cell2 = row.createCell(2);
				if (record1.getR36_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR36_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR36_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR36_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R37 =====
				row = sheet.getRow(36);
				cell2 = row.createCell(2);
				if (record1.getR37_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR37_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR37_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR37_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R38 =====
				row = sheet.getRow(37);
				cell2 = row.createCell(2);
				if (record1.getR38_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR38_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR38_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR38_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R39 =====
				row = sheet.getRow(38);
				cell2 = row.createCell(2);
				if (record1.getR39_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR39_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR39_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR39_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R40 =====
				row = sheet.getRow(39);
				cell2 = row.createCell(2);
				if (record1.getR40_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR40_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR40_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR40_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R41 =====
				row = sheet.getRow(40);
				cell2 = row.createCell(2);
				if (record1.getR41_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR41_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR41_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR41_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R42 =====
				row = sheet.getRow(41);
				cell2 = row.createCell(2);
				if (record1.getR42_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR42_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR42_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR42_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R43 =====
				row = sheet.getRow(42);
				cell2 = row.createCell(2);
				if (record1.getR43_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR43_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR43_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR43_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R44 =====
				row = sheet.getRow(43);
				cell2 = row.createCell(2);
				if (record1.getR44_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR44_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR44_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR44_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R45 =====
				row = sheet.getRow(44);
				cell2 = row.createCell(2);
				if (record1.getR45_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR45_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR45_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR45_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R46 =====
				row = sheet.getRow(45);
				cell2 = row.createCell(2);
				if (record1.getR46_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR46_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR46_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR46_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R47 =====
				row = sheet.getRow(46);
				cell2 = row.createCell(2);
				if (record1.getR47_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR47_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR47_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR47_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R48 =====
				row = sheet.getRow(47);
				cell2 = row.createCell(2);
				if (record1.getR48_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR48_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR48_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR48_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R49 =====
				row = sheet.getRow(48);
				cell2 = row.createCell(2);
				if (record1.getR49_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR49_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR49_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR49_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R50 =====
				row = sheet.getRow(49);
				cell2 = row.createCell(2);
				if (record1.getR50_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR50_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR50_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR50_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}

				//===== R58 =====
				row = sheet.getRow(57);
				cell3 = row.createCell(3);
				if (record1.getR58_AMOUNT() != null) {
				 cell3.setCellValue(record1.getR58_AMOUNT().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R59 =====
				row = sheet.getRow(58);
				cell3 = row.createCell(3);
				if (record1.getR59_AMOUNT() != null) {
				 cell3.setCellValue(record1.getR59_AMOUNT().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R60 =====
				row = sheet.getRow(59);
				cell3 = row.createCell(3);
				if (record1.getR60_AMOUNT() != null) {
				 cell3.setCellValue(record1.getR60_AMOUNT().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R61 =====
				row = sheet.getRow(61);
				cell3 = row.createCell(3);
				if (record1.getR62_AMOUNT() != null) {
				 cell3.setCellValue(record1.getR62_AMOUNT().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
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
			auditService.createBusinessAudit(userid, "DOWNLOAD", "EXPOSURES ARCHIVAL SUMMARY", null, "BRRS_EXPOSURES_ARCHIVALTABLE_SUMMARY");
		}
				

				return out.toByteArray();
			}

		}
		
		
		
	}