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

public class BRRS_AML_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_AML_ReportService.class);
	
	
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


	public List<AML_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_AML_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new AML_Summary_RowMapper()
    );
}
	
// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================



public List<Object[]> getAML_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_AML_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<AML_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_AML_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new AML_Archival_Summary_RowMapper()
    );
}

public List<AML_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_AML_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new AML_Archival_Summary_RowMapper()
    );
}

// =====================================================
// DETAIL REPO
// =====================================================	


public List<AML_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_AML_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new AML_Detail_RowMapper()
    );
}

public List<AML_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_AML_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, offset, limit},
            new AML_Detail_RowMapper()
    );
}

public int getDataCount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_AML_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(sql, new Object[]{reportDate}, Integer.class);
}

public List<AML_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel, String reportAddlCriteria1, Date reportDate) {

    String sql = "SELECT * FROM BRRS_AML_DETAILTABLE " +
                 "WHERE REPORT_LABEL = ? " +
                 "AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate},
            new AML_Detail_RowMapper()
    );
}

public AML_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_AML_DETAILTABLE WHERE ACCT_NUMBER = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{acctNumber},
            new AML_Detail_RowMapper()
    );
}


// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================

public List<AML_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate, String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_AML_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, dataEntryVersion},
            new AML_Archival_Detail_RowMapper()
    );
}


public List<AML_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_AML_ARCHIVALTABLE_DETAIL " +
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
            new AML_Archival_Detail_RowMapper()
    );
}

// =====================================================
// SUMAMRY ENTITY 
// =====================================================


public class AML_Summary_RowMapper implements RowMapper<AML_Summary_Entity> {

    @Override
    public AML_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        AML_Summary_Entity obj = new AML_Summary_Entity();

// =========================
// R11
// =========================
obj.setR11_cust_base_deposit(rs.getString("r11_cust_base_deposit"));
obj.setR11_cust_base_no_of_acct(rs.getBigDecimal("r11_cust_base_no_of_acct"));
obj.setR11_cust_base_tot_dep(rs.getBigDecimal("r11_cust_base_tot_dep"));

// =========================
// R12
// =========================
obj.setR12_cust_base_deposit(rs.getString("r12_cust_base_deposit"));
obj.setR12_cust_base_no_of_acct(rs.getBigDecimal("r12_cust_base_no_of_acct"));
obj.setR12_cust_base_tot_dep(rs.getBigDecimal("r12_cust_base_tot_dep"));

// =========================
// R13
// =========================
obj.setR13_cust_base_deposit(rs.getString("r13_cust_base_deposit"));
obj.setR13_cust_base_no_of_acct(rs.getBigDecimal("r13_cust_base_no_of_acct"));
obj.setR13_cust_base_tot_dep(rs.getBigDecimal("r13_cust_base_tot_dep"));

// =========================
// R14
// =========================
obj.setR14_cust_base_deposit(rs.getString("r14_cust_base_deposit"));
obj.setR14_cust_base_no_of_acct(rs.getBigDecimal("r14_cust_base_no_of_acct"));
obj.setR14_cust_base_tot_dep(rs.getBigDecimal("r14_cust_base_tot_dep"));

// =========================
// R15
// =========================
obj.setR15_cust_base_deposit(rs.getString("r15_cust_base_deposit"));
obj.setR15_cust_base_no_of_acct(rs.getBigDecimal("r15_cust_base_no_of_acct"));
obj.setR15_cust_base_tot_dep(rs.getBigDecimal("r15_cust_base_tot_dep"));


// =========================
// R21
// =========================
obj.setR21_cust_risk_pro_deposit(rs.getString("r21_cust_risk_pro_deposit"));
obj.setR21_cust_risk_pro_num_of_cust(rs.getBigDecimal("r21_cust_risk_pro_num_of_cust"));
obj.setR21_cust_risk_pro_value(rs.getBigDecimal("r21_cust_risk_pro_value"));

// =========================
// R22
// =========================
obj.setR22_cust_risk_pro_deposit(rs.getString("r22_cust_risk_pro_deposit"));
obj.setR22_cust_risk_pro_num_of_cust(rs.getBigDecimal("r22_cust_risk_pro_num_of_cust"));
obj.setR22_cust_risk_pro_value(rs.getBigDecimal("r22_cust_risk_pro_value"));

// =========================
// R23
// =========================
obj.setR23_cust_risk_pro_deposit(rs.getString("r23_cust_risk_pro_deposit"));
obj.setR23_cust_risk_pro_num_of_cust(rs.getBigDecimal("r23_cust_risk_pro_num_of_cust"));
obj.setR23_cust_risk_pro_value(rs.getBigDecimal("r23_cust_risk_pro_value"));

// =========================
// R24
// =========================
obj.setR24_cust_risk_pro_deposit(rs.getString("r24_cust_risk_pro_deposit"));
obj.setR24_cust_risk_pro_num_of_cust(rs.getBigDecimal("r24_cust_risk_pro_num_of_cust"));
obj.setR24_cust_risk_pro_value(rs.getBigDecimal("r24_cust_risk_pro_value"));


// =========================
// R30
// =========================
obj.setR30_b2_cust_deposit(rs.getString("r30_b2_cust_deposit"));
obj.setR30_b2_low_risk_no_cust(rs.getBigDecimal("r30_b2_low_risk_no_cust"));
obj.setR30_b2_low_risk_deposit(rs.getBigDecimal("r30_b2_low_risk_deposit"));
obj.setR30_b2_medi_risk_no_cust(rs.getBigDecimal("r30_b2_medi_risk_no_cust"));
obj.setR30_b2_medi_risk_deposit(rs.getBigDecimal("r30_b2_medi_risk_deposit"));
obj.setR30_b2_high_risk_no_cust(rs.getBigDecimal("r30_b2_high_risk_no_cust"));
obj.setR30_b2_high_risk_deposit(rs.getBigDecimal("r30_b2_high_risk_deposit"));
obj.setR30_b2_tot_no_cust(rs.getBigDecimal("r30_b2_tot_no_cust"));
obj.setR30_b2_tot_deposit(rs.getBigDecimal("r30_b2_tot_deposit"));

// =========================
// R31
// =========================
obj.setR31_b2_cust_deposit(rs.getString("r31_b2_cust_deposit"));
obj.setR31_b2_low_risk_no_cust(rs.getBigDecimal("r31_b2_low_risk_no_cust"));
obj.setR31_b2_low_risk_deposit(rs.getBigDecimal("r31_b2_low_risk_deposit"));
obj.setR31_b2_medi_risk_no_cust(rs.getBigDecimal("r31_b2_medi_risk_no_cust"));
obj.setR31_b2_medi_risk_deposit(rs.getBigDecimal("r31_b2_medi_risk_deposit"));
obj.setR31_b2_high_risk_no_cust(rs.getBigDecimal("r31_b2_high_risk_no_cust"));
obj.setR31_b2_high_risk_deposit(rs.getBigDecimal("r31_b2_high_risk_deposit"));
obj.setR31_b2_tot_no_cust(rs.getBigDecimal("r31_b2_tot_no_cust"));
obj.setR31_b2_tot_deposit(rs.getBigDecimal("r31_b2_tot_deposit"));

// =========================
// R32
// =========================
obj.setR32_b2_cust_deposit(rs.getString("r32_b2_cust_deposit"));
obj.setR32_b2_low_risk_no_cust(rs.getBigDecimal("r32_b2_low_risk_no_cust"));
obj.setR32_b2_low_risk_deposit(rs.getBigDecimal("r32_b2_low_risk_deposit"));
obj.setR32_b2_medi_risk_no_cust(rs.getBigDecimal("r32_b2_medi_risk_no_cust"));
obj.setR32_b2_medi_risk_deposit(rs.getBigDecimal("r32_b2_medi_risk_deposit"));
obj.setR32_b2_high_risk_no_cust(rs.getBigDecimal("r32_b2_high_risk_no_cust"));
obj.setR32_b2_high_risk_deposit(rs.getBigDecimal("r32_b2_high_risk_deposit"));
obj.setR32_b2_tot_no_cust(rs.getBigDecimal("r32_b2_tot_no_cust"));
obj.setR32_b2_tot_deposit(rs.getBigDecimal("r32_b2_tot_deposit"));

// =========================
// R33
// =========================
obj.setR33_b2_cust_deposit(rs.getString("r33_b2_cust_deposit"));
obj.setR33_b2_low_risk_no_cust(rs.getBigDecimal("r33_b2_low_risk_no_cust"));
obj.setR33_b2_low_risk_deposit(rs.getBigDecimal("r33_b2_low_risk_deposit"));
obj.setR33_b2_medi_risk_no_cust(rs.getBigDecimal("r33_b2_medi_risk_no_cust"));
obj.setR33_b2_medi_risk_deposit(rs.getBigDecimal("r33_b2_medi_risk_deposit"));
obj.setR33_b2_high_risk_no_cust(rs.getBigDecimal("r33_b2_high_risk_no_cust"));
obj.setR33_b2_high_risk_deposit(rs.getBigDecimal("r33_b2_high_risk_deposit"));
obj.setR33_b2_tot_no_cust(rs.getBigDecimal("r33_b2_tot_no_cust"));
obj.setR33_b2_tot_deposit(rs.getBigDecimal("r33_b2_tot_deposit"));


// =========================
// R39
// =========================
obj.setR39_cust_base_cust_deposit(rs.getString("r39_cust_base_cust_deposit"));
obj.setR39_cust_base_no_cust(rs.getBigDecimal("r39_cust_base_no_cust"));
obj.setR39_cust_base_deposits(rs.getBigDecimal("r39_cust_base_deposits"));

// =========================
// R40
// =========================
obj.setR40_cust_base_cust_deposit(rs.getString("r40_cust_base_cust_deposit"));
obj.setR40_cust_base_no_cust(rs.getBigDecimal("r40_cust_base_no_cust"));
obj.setR40_cust_base_deposits(rs.getBigDecimal("r40_cust_base_deposits"));

// =========================
// R41
// =========================
obj.setR41_cust_base_cust_deposit(rs.getString("r41_cust_base_cust_deposit"));
obj.setR41_cust_base_no_cust(rs.getBigDecimal("r41_cust_base_no_cust"));
obj.setR41_cust_base_deposits(rs.getBigDecimal("r41_cust_base_deposits"));


// =========================
// R50
// =========================
obj.setR50_brkdown_typ_of_cust(rs.getString("r50_brkdown_typ_of_cust"));
obj.setR50_brkdown_num_of_cust(rs.getBigDecimal("r50_brkdown_num_of_cust"));
obj.setR50_brkdown_tot_depo(rs.getBigDecimal("r50_brkdown_tot_depo"));

// =========================
// R51
// =========================
obj.setR51_brkdown_typ_of_cust(rs.getString("r51_brkdown_typ_of_cust"));
obj.setR51_brkdown_num_of_cust(rs.getBigDecimal("r51_brkdown_num_of_cust"));
obj.setR51_brkdown_tot_depo(rs.getBigDecimal("r51_brkdown_tot_depo"));

// =========================
// R52
// =========================
obj.setR52_brkdown_typ_of_cust(rs.getString("r52_brkdown_typ_of_cust"));
obj.setR52_brkdown_num_of_cust(rs.getBigDecimal("r52_brkdown_num_of_cust"));
obj.setR52_brkdown_tot_depo(rs.getBigDecimal("r52_brkdown_tot_depo"));

// =========================
// R53
// =========================
obj.setR53_brkdown_typ_of_cust(rs.getString("r53_brkdown_typ_of_cust"));
obj.setR53_brkdown_num_of_cust(rs.getBigDecimal("r53_brkdown_num_of_cust"));
obj.setR53_brkdown_tot_depo(rs.getBigDecimal("r53_brkdown_tot_depo"));

// =========================
// R54
// =========================
obj.setR54_brkdown_typ_of_cust(rs.getString("r54_brkdown_typ_of_cust"));
obj.setR54_brkdown_num_of_cust(rs.getBigDecimal("r54_brkdown_num_of_cust"));
obj.setR54_brkdown_tot_depo(rs.getBigDecimal("r54_brkdown_tot_depo"));

// =========================
// R55
// =========================
obj.setR55_brkdown_typ_of_cust(rs.getString("r55_brkdown_typ_of_cust"));
obj.setR55_brkdown_num_of_cust(rs.getBigDecimal("r55_brkdown_num_of_cust"));
obj.setR55_brkdown_tot_depo(rs.getBigDecimal("r55_brkdown_tot_depo"));

// =========================
// R56
// =========================
obj.setR56_brkdown_typ_of_cust(rs.getString("r56_brkdown_typ_of_cust"));
obj.setR56_brkdown_num_of_cust(rs.getBigDecimal("r56_brkdown_num_of_cust"));
obj.setR56_brkdown_tot_depo(rs.getBigDecimal("r56_brkdown_tot_depo"));

// =========================
// R57
// =========================
obj.setR57_brkdown_typ_of_cust(rs.getString("r57_brkdown_typ_of_cust"));
obj.setR57_brkdown_num_of_cust(rs.getBigDecimal("r57_brkdown_num_of_cust"));
obj.setR57_brkdown_tot_depo(rs.getBigDecimal("r57_brkdown_tot_depo"));

// =========================
// R58
// =========================
obj.setR58_brkdown_typ_of_cust(rs.getString("r58_brkdown_typ_of_cust"));
obj.setR58_brkdown_num_of_cust(rs.getBigDecimal("r58_brkdown_num_of_cust"));
obj.setR58_brkdown_tot_depo(rs.getBigDecimal("r58_brkdown_tot_depo"));

// =========================
// R59
// =========================
obj.setR59_brkdown_typ_of_cust(rs.getString("r59_brkdown_typ_of_cust"));
obj.setR59_brkdown_num_of_cust(rs.getBigDecimal("r59_brkdown_num_of_cust"));
obj.setR59_brkdown_tot_depo(rs.getBigDecimal("r59_brkdown_tot_depo"));

// =========================
// R60
// =========================
obj.setR60_brkdown_typ_of_cust(rs.getString("r60_brkdown_typ_of_cust"));
obj.setR60_brkdown_num_of_cust(rs.getBigDecimal("r60_brkdown_num_of_cust"));
obj.setR60_brkdown_tot_depo(rs.getBigDecimal("r60_brkdown_tot_depo"));


// =========================
// R61
// =========================
obj.setR61_brkdown_typ_of_cust(rs.getString("r61_brkdown_typ_of_cust"));
obj.setR61_brkdown_num_of_cust(rs.getBigDecimal("r61_brkdown_num_of_cust"));
obj.setR61_brkdown_tot_depo(rs.getBigDecimal("r61_brkdown_tot_depo"));

// =========================
// R62
// =========================
obj.setR62_brkdown_typ_of_cust(rs.getString("r62_brkdown_typ_of_cust"));
obj.setR62_brkdown_num_of_cust(rs.getBigDecimal("r62_brkdown_num_of_cust"));
obj.setR62_brkdown_tot_depo(rs.getBigDecimal("r62_brkdown_tot_depo"));

// =========================
// R63
// =========================
obj.setR63_brkdown_typ_of_cust(rs.getString("r63_brkdown_typ_of_cust"));
obj.setR63_brkdown_num_of_cust(rs.getBigDecimal("r63_brkdown_num_of_cust"));
obj.setR63_brkdown_tot_depo(rs.getBigDecimal("r63_brkdown_tot_depo"));

// =========================
// R64
// =========================
obj.setR64_brkdown_typ_of_cust(rs.getString("r64_brkdown_typ_of_cust"));
obj.setR64_brkdown_num_of_cust(rs.getBigDecimal("r64_brkdown_num_of_cust"));
obj.setR64_brkdown_tot_depo(rs.getBigDecimal("r64_brkdown_tot_depo"));

// =========================
// R65
// =========================
obj.setR65_brkdown_typ_of_cust(rs.getString("r65_brkdown_typ_of_cust"));
obj.setR65_brkdown_num_of_cust(rs.getBigDecimal("r65_brkdown_num_of_cust"));
obj.setR65_brkdown_tot_depo(rs.getBigDecimal("r65_brkdown_tot_depo"));

// =========================
// R66
// =========================
obj.setR66_brkdown_typ_of_cust(rs.getString("r66_brkdown_typ_of_cust"));
obj.setR66_brkdown_num_of_cust(rs.getBigDecimal("r66_brkdown_num_of_cust"));
obj.setR66_brkdown_tot_depo(rs.getBigDecimal("r66_brkdown_tot_depo"));

// =========================
// R67
// =========================
obj.setR67_brkdown_typ_of_cust(rs.getString("r67_brkdown_typ_of_cust"));
obj.setR67_brkdown_num_of_cust(rs.getBigDecimal("r67_brkdown_num_of_cust"));
obj.setR67_brkdown_tot_depo(rs.getBigDecimal("r67_brkdown_tot_depo"));

// =========================
// R68
// =========================
obj.setR68_brkdown_typ_of_cust(rs.getString("r68_brkdown_typ_of_cust"));
obj.setR68_brkdown_num_of_cust(rs.getBigDecimal("r68_brkdown_num_of_cust"));
obj.setR68_brkdown_tot_depo(rs.getBigDecimal("r68_brkdown_tot_depo"));

// =========================
// R69
// =========================
obj.setR69_brkdown_typ_of_cust(rs.getString("r69_brkdown_typ_of_cust"));
obj.setR69_brkdown_num_of_cust(rs.getBigDecimal("r69_brkdown_num_of_cust"));
obj.setR69_brkdown_tot_depo(rs.getBigDecimal("r69_brkdown_tot_depo"));

// =========================
// R70
// =========================
obj.setR70_brkdown_typ_of_cust(rs.getString("r70_brkdown_typ_of_cust"));
obj.setR70_brkdown_num_of_cust(rs.getBigDecimal("r70_brkdown_num_of_cust"));
obj.setR70_brkdown_tot_depo(rs.getBigDecimal("r70_brkdown_tot_depo"));

// =========================
// R71
// =========================
obj.setR71_brkdown_typ_of_cust(rs.getString("r71_brkdown_typ_of_cust"));
obj.setR71_brkdown_num_of_cust(rs.getBigDecimal("r71_brkdown_num_of_cust"));
obj.setR71_brkdown_tot_depo(rs.getBigDecimal("r71_brkdown_tot_depo"));

// =========================
// R72
// =========================
obj.setR72_brkdown_typ_of_cust(rs.getString("r72_brkdown_typ_of_cust"));
obj.setR72_brkdown_num_of_cust(rs.getBigDecimal("r72_brkdown_num_of_cust"));
obj.setR72_brkdown_tot_depo(rs.getBigDecimal("r72_brkdown_tot_depo"));

// =========================
// R73
// =========================
obj.setR73_brkdown_typ_of_cust(rs.getString("r73_brkdown_typ_of_cust"));
obj.setR73_brkdown_num_of_cust(rs.getBigDecimal("r73_brkdown_num_of_cust"));
obj.setR73_brkdown_tot_depo(rs.getBigDecimal("r73_brkdown_tot_depo"));

// =========================
// R74
// =========================
obj.setR74_brkdown_typ_of_cust(rs.getString("r74_brkdown_typ_of_cust"));
obj.setR74_brkdown_num_of_cust(rs.getBigDecimal("r74_brkdown_num_of_cust"));
obj.setR74_brkdown_tot_depo(rs.getBigDecimal("r74_brkdown_tot_depo"));

// =========================
// R75
// =========================
obj.setR75_brkdown_typ_of_cust(rs.getString("r75_brkdown_typ_of_cust"));
obj.setR75_brkdown_num_of_cust(rs.getBigDecimal("r75_brkdown_num_of_cust"));
obj.setR75_brkdown_tot_depo(rs.getBigDecimal("r75_brkdown_tot_depo"));

// =========================
// R82
// =========================
obj.setR82_e1_tot_no_cust(rs.getBigDecimal("r82_e1_tot_no_cust"));
obj.setR82_e1_loan_on_bal_expo(rs.getBigDecimal("r82_e1_loan_on_bal_expo"));
obj.setR82_e1_deposit(rs.getBigDecimal("r82_e1_deposit"));
obj.setR82_e1_funds_behalf_cust(rs.getBigDecimal("r82_e1_funds_behalf_cust"));
obj.setR82_e1_turnover(rs.getBigDecimal("r82_e1_turnover"));

// =========================
// R83
// =========================
obj.setR83_e1_tot_no_cust(rs.getBigDecimal("r83_e1_tot_no_cust"));
obj.setR83_e1_loan_on_bal_expo(rs.getBigDecimal("r83_e1_loan_on_bal_expo"));
obj.setR83_e1_deposit(rs.getBigDecimal("r83_e1_deposit"));
obj.setR83_e1_funds_behalf_cust(rs.getBigDecimal("r83_e1_funds_behalf_cust"));
obj.setR83_e1_turnover(rs.getBigDecimal("r83_e1_turnover"));

// =========================
// R89
// =========================
obj.setR89_e2_tot_no_cust(rs.getBigDecimal("r89_e2_tot_no_cust"));
obj.setR89_e2_loans_bal_expo(rs.getBigDecimal("r89_e2_loans_bal_expo"));
obj.setR89_e2_deposit(rs.getBigDecimal("r89_e2_deposit"));
obj.setR89_e2_funds_behalf_cust(rs.getBigDecimal("r89_e2_funds_behalf_cust"));
obj.setR89_e2_turnover(rs.getBigDecimal("r89_e2_turnover"));

// =========================
// R90
// =========================
obj.setR90_e2_tot_no_cust(rs.getBigDecimal("r90_e2_tot_no_cust"));
obj.setR90_e2_loans_bal_expo(rs.getBigDecimal("r90_e2_loans_bal_expo"));
obj.setR90_e2_deposit(rs.getBigDecimal("r90_e2_deposit"));
obj.setR90_e2_funds_behalf_cust(rs.getBigDecimal("r90_e2_funds_behalf_cust"));
obj.setR90_e2_turnover(rs.getBigDecimal("r90_e2_turnover"));

// =========================
// R96
// =========================
obj.setR96_e3_tot_no_cust(rs.getBigDecimal("r96_e3_tot_no_cust"));
obj.setR96_e3_loans_bal_expo(rs.getBigDecimal("r96_e3_loans_bal_expo"));
obj.setR96_e3_deposit(rs.getBigDecimal("r96_e3_deposit"));
obj.setR96_e3_funds_behalf_cust(rs.getBigDecimal("r96_e3_funds_behalf_cust"));
obj.setR96_e3_turnover(rs.getBigDecimal("r96_e3_turnover"));

// =========================
// R97
// =========================
obj.setR97_e3_tot_no_cust(rs.getBigDecimal("r97_e3_tot_no_cust"));
obj.setR97_e3_loans_bal_expo(rs.getBigDecimal("r97_e3_loans_bal_expo"));
obj.setR97_e3_deposit(rs.getBigDecimal("r97_e3_deposit"));
obj.setR97_e3_funds_behalf_cust(rs.getBigDecimal("r97_e3_funds_behalf_cust"));
obj.setR97_e3_turnover(rs.getBigDecimal("r97_e3_turnover"));


// =========================
// R104
// =========================
obj.setR104_f_num_of_cust(rs.getBigDecimal("r104_f_num_of_cust"));
obj.setR104_f_loans_bal_expo(rs.getBigDecimal("r104_f_loans_bal_expo"));
obj.setR104_f_deposit(rs.getBigDecimal("r104_f_deposit"));
obj.setR104_f_funds_behalf_cust(rs.getBigDecimal("r104_f_funds_behalf_cust"));
obj.setR104_f_turnover(rs.getBigDecimal("r104_f_turnover"));

// =========================
// R105
// =========================
obj.setR105_f_num_of_cust(rs.getBigDecimal("r105_f_num_of_cust"));
obj.setR105_f_loans_bal_expo(rs.getBigDecimal("r105_f_loans_bal_expo"));
obj.setR105_f_deposit(rs.getBigDecimal("r105_f_deposit"));
obj.setR105_f_funds_behalf_cust(rs.getBigDecimal("r105_f_funds_behalf_cust"));
obj.setR105_f_turnover(rs.getBigDecimal("r105_f_turnover"));


// =========================
// R111
// =========================
obj.setR111_g1_pay_mech(rs.getString("r111_g1_pay_mech"));
obj.setR111_g1_pay_mechanisum(rs.getString("r111_g1_pay_mechanisum"));
obj.setR111_g1_num_trans(rs.getBigDecimal("r111_g1_num_trans"));
obj.setR111_g1_val_trans(rs.getBigDecimal("r111_g1_val_trans"));

// =========================
// R112
// =========================
obj.setR112_g1_pay_mech(rs.getString("r112_g1_pay_mech"));
obj.setR112_g1_pay_mechanisum(rs.getString("r112_g1_pay_mechanisum"));
obj.setR112_g1_num_trans(rs.getBigDecimal("r112_g1_num_trans"));
obj.setR112_g1_val_trans(rs.getBigDecimal("r112_g1_val_trans"));

// =========================
// R113
// =========================
obj.setR113_g1_pay_mech(rs.getString("r113_g1_pay_mech"));
obj.setR113_g1_pay_mechanisum(rs.getString("r113_g1_pay_mechanisum"));
obj.setR113_g1_num_trans(rs.getBigDecimal("r113_g1_num_trans"));
obj.setR113_g1_val_trans(rs.getBigDecimal("r113_g1_val_trans"));

// =========================
// R114
// =========================
obj.setR114_g1_pay_mech(rs.getString("r114_g1_pay_mech"));
obj.setR114_g1_pay_mechanisum(rs.getString("r114_g1_pay_mechanisum"));
obj.setR114_g1_num_trans(rs.getBigDecimal("r114_g1_num_trans"));
obj.setR114_g1_val_trans(rs.getBigDecimal("r114_g1_val_trans"));

// =========================
// R115
// =========================
obj.setR115_g1_pay_mech(rs.getString("r115_g1_pay_mech"));
obj.setR115_g1_pay_mechanisum(rs.getString("r115_g1_pay_mechanisum"));
obj.setR115_g1_num_trans(rs.getBigDecimal("r115_g1_num_trans"));
obj.setR115_g1_val_trans(rs.getBigDecimal("r115_g1_val_trans"));

// =========================
// R116
// =========================
obj.setR116_g1_pay_mech(rs.getString("r116_g1_pay_mech"));
obj.setR116_g1_pay_mechanisum(rs.getString("r116_g1_pay_mechanisum"));
obj.setR116_g1_num_trans(rs.getBigDecimal("r116_g1_num_trans"));
obj.setR116_g1_val_trans(rs.getBigDecimal("r116_g1_val_trans"));

// =========================
// R117
// =========================
obj.setR117_g1_pay_mech(rs.getString("r117_g1_pay_mech"));
obj.setR117_g1_pay_mechanisum(rs.getString("r117_g1_pay_mechanisum"));
obj.setR117_g1_num_trans(rs.getBigDecimal("r117_g1_num_trans"));
obj.setR117_g1_val_trans(rs.getBigDecimal("r117_g1_val_trans"));

// =========================
// R118
// =========================
obj.setR118_g1_pay_mech(rs.getString("r118_g1_pay_mech"));
obj.setR118_g1_pay_mechanisum(rs.getString("r118_g1_pay_mechanisum"));
obj.setR118_g1_num_trans(rs.getBigDecimal("r118_g1_num_trans"));
obj.setR118_g1_val_trans(rs.getBigDecimal("r118_g1_val_trans"));

// =========================
// R119
// =========================
obj.setR119_g1_pay_mech(rs.getString("r119_g1_pay_mech"));
obj.setR119_g1_pay_mechanisum(rs.getString("r119_g1_pay_mechanisum"));
obj.setR119_g1_num_trans(rs.getBigDecimal("r119_g1_num_trans"));
obj.setR119_g1_val_trans(rs.getBigDecimal("r119_g1_val_trans"));

// =========================
// R120
// =========================
obj.setR120_g1_pay_mech(rs.getString("r120_g1_pay_mech"));
obj.setR120_g1_pay_mechanisum(rs.getString("r120_g1_pay_mechanisum"));
obj.setR120_g1_num_trans(rs.getBigDecimal("r120_g1_num_trans"));
obj.setR120_g1_val_trans(rs.getBigDecimal("r120_g1_val_trans"));


// =========================
// R121
// =========================
obj.setR121_g1_pay_mech(rs.getString("r121_g1_pay_mech"));
obj.setR121_g1_pay_mechanisum(rs.getString("r121_g1_pay_mechanisum"));
obj.setR121_g1_num_trans(rs.getBigDecimal("r121_g1_num_trans"));
obj.setR121_g1_val_trans(rs.getBigDecimal("r121_g1_val_trans"));

// =========================
// R122
// =========================
obj.setR122_g1_pay_mech(rs.getString("r122_g1_pay_mech"));
obj.setR122_g1_pay_mechanisum(rs.getString("r122_g1_pay_mechanisum"));
obj.setR122_g1_num_trans(rs.getBigDecimal("r122_g1_num_trans"));
obj.setR122_g1_val_trans(rs.getBigDecimal("r122_g1_val_trans"));

// =========================
// R123
// =========================
obj.setR123_g1_pay_mech(rs.getString("r123_g1_pay_mech"));
obj.setR123_g1_pay_mechanisum(rs.getString("r123_g1_pay_mechanisum"));
obj.setR123_g1_num_trans(rs.getBigDecimal("r123_g1_num_trans"));
obj.setR123_g1_val_trans(rs.getBigDecimal("r123_g1_val_trans"));

// =========================
// R124
// =========================
obj.setR124_g1_pay_mech(rs.getString("r124_g1_pay_mech"));
obj.setR124_g1_pay_mechanisum(rs.getString("r124_g1_pay_mechanisum"));
obj.setR124_g1_num_trans(rs.getBigDecimal("r124_g1_num_trans"));
obj.setR124_g1_val_trans(rs.getBigDecimal("r124_g1_val_trans"));

// =========================
// R125
// =========================
obj.setR125_g1_pay_mech(rs.getString("r125_g1_pay_mech"));
obj.setR125_g1_pay_mechanisum(rs.getString("r125_g1_pay_mechanisum"));
obj.setR125_g1_num_trans(rs.getBigDecimal("r125_g1_num_trans"));
obj.setR125_g1_val_trans(rs.getBigDecimal("r125_g1_val_trans"));

// =========================
// R126
// =========================
obj.setR126_g1_pay_mech(rs.getString("r126_g1_pay_mech"));
obj.setR126_g1_pay_mechanisum(rs.getString("r126_g1_pay_mechanisum"));
obj.setR126_g1_num_trans(rs.getBigDecimal("r126_g1_num_trans"));
obj.setR126_g1_val_trans(rs.getBigDecimal("r126_g1_val_trans"));

// =========================
// R127
// =========================
obj.setR127_g1_pay_mech(rs.getString("r127_g1_pay_mech"));
obj.setR127_g1_pay_mechanisum(rs.getString("r127_g1_pay_mechanisum"));
obj.setR127_g1_num_trans(rs.getBigDecimal("r127_g1_num_trans"));
obj.setR127_g1_val_trans(rs.getBigDecimal("r127_g1_val_trans"));

// =========================
// R128
// =========================
obj.setR128_g1_pay_mech(rs.getString("r128_g1_pay_mech"));
obj.setR128_g1_pay_mechanisum(rs.getString("r128_g1_pay_mechanisum"));
obj.setR128_g1_num_trans(rs.getBigDecimal("r128_g1_num_trans"));
obj.setR128_g1_val_trans(rs.getBigDecimal("r128_g1_val_trans"));

// =========================
// R135
// =========================
obj.setR135_g2_foreign_exchange(rs.getString("r135_g2_foreign_exchange"));
obj.setR135_g2_fore_exchange(rs.getString("r135_g2_fore_exchange"));
obj.setR135_g2_val_transac(rs.getBigDecimal("r135_g2_val_transac"));

// =========================
// R136
// =========================
obj.setR136_g2_fore_exchange(rs.getString("r136_g2_fore_exchange"));
obj.setR136_g2_val_transac(rs.getBigDecimal("r136_g2_val_transac"));

// =========================
// R138
// =========================
obj.setR138_g2_foreign_exchange(rs.getString("r138_g2_foreign_exchange"));
obj.setR138_g2_fore_exchange(rs.getString("r138_g2_fore_exchange"));
obj.setR138_g2_val_transac(rs.getBigDecimal("r138_g2_val_transac"));

// =========================
// R139
// =========================
obj.setR139_g2_fore_exchange(rs.getString("r139_g2_fore_exchange"));
obj.setR139_g2_val_transac(rs.getBigDecimal("r139_g2_val_transac"));

// =========================
// R144
// =========================
obj.setR144_h_types(rs.getString("r144_h_types"));
obj.setR144_h_amount(rs.getBigDecimal("r144_h_amount"));

// =========================
// R145
// =========================
obj.setR145_h_types(rs.getString("r145_h_types"));
obj.setR145_h_amount(rs.getBigDecimal("r145_h_amount"));

// =========================
// R146
// =========================
obj.setR146_h_types(rs.getString("r146_h_types"));
obj.setR146_h_amount(rs.getBigDecimal("r146_h_amount"));

// =========================
// R147
// =========================
obj.setR147_h_types(rs.getString("r147_h_types"));
obj.setR147_h_amount(rs.getBigDecimal("r147_h_amount"));

// =========================
// R148
// =========================
obj.setR148_h_types(rs.getString("r148_h_types"));
obj.setR148_h_amount(rs.getBigDecimal("r148_h_amount"));

// =========================
// R153
// =========================
obj.setR153_i_product_serv(rs.getString("r153_i_product_serv"));
obj.setR153_i_no_cust(rs.getBigDecimal("r153_i_no_cust"));
obj.setR153_i_outs_bal(rs.getBigDecimal("r153_i_outs_bal"));
obj.setR153_i_turnover(rs.getBigDecimal("r153_i_turnover"));

// =========================
// R154
// =========================
obj.setR154_i_product_serv(rs.getString("r154_i_product_serv"));
obj.setR154_i_no_cust(rs.getBigDecimal("r154_i_no_cust"));
obj.setR154_i_outs_bal(rs.getBigDecimal("r154_i_outs_bal"));
obj.setR154_i_turnover(rs.getBigDecimal("r154_i_turnover"));

// =========================
// R155
// =========================
obj.setR155_i_product_serv(rs.getString("r155_i_product_serv"));
obj.setR155_i_no_cust(rs.getBigDecimal("r155_i_no_cust"));
obj.setR155_i_outs_bal(rs.getBigDecimal("r155_i_outs_bal"));
obj.setR155_i_turnover(rs.getBigDecimal("r155_i_turnover"));

// =========================
// R161
// =========================
obj.setR161_j_trade_finc_prod(rs.getString("r161_j_trade_finc_prod"));
obj.setR161_j_num_of_cust(rs.getBigDecimal("r161_j_num_of_cust"));
obj.setR161_j_commitment_at_jun(rs.getBigDecimal("r161_j_commitment_at_jun"));

// =========================
// R162
// =========================
obj.setR162_j_trade_finc_prod(rs.getString("r162_j_trade_finc_prod"));
obj.setR162_j_num_of_cust(rs.getBigDecimal("r162_j_num_of_cust"));
obj.setR162_j_commitment_at_jun(rs.getBigDecimal("r162_j_commitment_at_jun"));

// =========================
// R163
// =========================
obj.setR163_j_trade_finc_prod(rs.getString("r163_j_trade_finc_prod"));
obj.setR163_j_num_of_cust(rs.getBigDecimal("r163_j_num_of_cust"));
obj.setR163_j_commitment_at_jun(rs.getBigDecimal("r163_j_commitment_at_jun"));

// =========================
// R164
// =========================
obj.setR164_j_trade_finc_prod(rs.getString("r164_j_trade_finc_prod"));
obj.setR164_j_num_of_cust(rs.getBigDecimal("r164_j_num_of_cust"));
obj.setR164_j_commitment_at_jun(rs.getBigDecimal("r164_j_commitment_at_jun"));

// =========================
// R170
// =========================
obj.setR170_k_pay_mechanism(rs.getString("r170_k_pay_mechanism"));
obj.setR170_k_pay_mech(rs.getString("r170_k_pay_mech"));
obj.setR170_k_num_of_trans(rs.getBigDecimal("r170_k_num_of_trans"));
obj.setR170_k_value_of_trans(rs.getBigDecimal("r170_k_value_of_trans"));

// =========================
// R171
// =========================
obj.setR171_k_pay_mech(rs.getString("r171_k_pay_mech"));
obj.setR171_k_num_of_trans(rs.getBigDecimal("r171_k_num_of_trans"));
obj.setR171_k_value_of_trans(rs.getBigDecimal("r171_k_value_of_trans"));

// =========================
// R172
// =========================
obj.setR172_k_pay_mechanism(rs.getString("r172_k_pay_mechanism"));
obj.setR172_k_pay_mech(rs.getString("r172_k_pay_mech"));
obj.setR172_k_num_of_trans(rs.getBigDecimal("r172_k_num_of_trans"));
obj.setR172_k_value_of_trans(rs.getBigDecimal("r172_k_value_of_trans"));

// =========================
// R179
// =========================
obj.setR179_l_transac_report(rs.getString("r179_l_transac_report"));
obj.setR179_l_num_of_transac(rs.getBigDecimal("r179_l_num_of_transac"));

// =========================
// R180
// =========================
obj.setR180_l_transac_report(rs.getString("r180_l_transac_report"));
obj.setR180_l_num_of_transac(rs.getBigDecimal("r180_l_num_of_transac"));

// =========================
// R181
// =========================
obj.setR181_l_transac_report(rs.getString("r181_l_transac_report"));
obj.setR181_l_num_of_transac(rs.getBigDecimal("r181_l_num_of_transac"));

// =========================
// R187
// =========================
obj.setR187_m_transac_life(rs.getString("r187_m_transac_life"));
obj.setR187_m_num_of_transac(rs.getBigDecimal("r187_m_num_of_transac"));
obj.setR187_m_val_of_transac(rs.getBigDecimal("r187_m_val_of_transac"));

// =========================
// R192
// =========================
obj.setR192_n_transac_life(rs.getString("r192_n_transac_life"));
obj.setR192_n_num_of_transac(rs.getBigDecimal("r192_n_num_of_transac"));
obj.setR192_n_val_of_transac(rs.getBigDecimal("r192_n_val_of_transac"));

// =========================
// R196
// =========================
obj.setR196_o_transac_life(rs.getString("r196_o_transac_life"));
obj.setR196_o_num_of_transac(rs.getBigDecimal("r196_o_num_of_transac"));
obj.setR196_o_val_of_transac(rs.getBigDecimal("r196_o_val_of_transac"));

// =========================
// R201
// =========================
obj.setR201_p_transac_life(rs.getString("r201_p_transac_life"));
obj.setR201_p_num_of_transac(rs.getBigDecimal("r201_p_num_of_transac"));
obj.setR201_p_val_of_transac(rs.getBigDecimal("r201_p_val_of_transac"));



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


public class AML_Summary_Entity {
	
  private String r11_cust_base_deposit;
    private BigDecimal r11_cust_base_no_of_acct;
    private BigDecimal r11_cust_base_tot_dep;
    private String r12_cust_base_deposit;
    private BigDecimal r12_cust_base_no_of_acct;
    private BigDecimal r12_cust_base_tot_dep;
    private String r13_cust_base_deposit;
    private BigDecimal r13_cust_base_no_of_acct;
    private BigDecimal r13_cust_base_tot_dep;
    private String r14_cust_base_deposit;
    private BigDecimal r14_cust_base_no_of_acct;
    private BigDecimal r14_cust_base_tot_dep;
    private String r15_cust_base_deposit;
    private BigDecimal r15_cust_base_no_of_acct;
    private BigDecimal r15_cust_base_tot_dep;
    private String r21_cust_risk_pro_deposit;
    private BigDecimal r21_cust_risk_pro_num_of_cust;
    private BigDecimal r21_cust_risk_pro_value;
    private String r22_cust_risk_pro_deposit;
    private BigDecimal r22_cust_risk_pro_num_of_cust;
    private BigDecimal r22_cust_risk_pro_value;
    private String r23_cust_risk_pro_deposit;
    private BigDecimal r23_cust_risk_pro_num_of_cust;
    private BigDecimal r23_cust_risk_pro_value;
    private String r24_cust_risk_pro_deposit;
    private BigDecimal r24_cust_risk_pro_num_of_cust;
    private BigDecimal r24_cust_risk_pro_value;
    private String r30_b2_cust_deposit;
    private BigDecimal r30_b2_low_risk_no_cust;
    private BigDecimal r30_b2_low_risk_deposit;
    private BigDecimal r30_b2_medi_risk_no_cust;
    private BigDecimal r30_b2_medi_risk_deposit;
    private BigDecimal r30_b2_high_risk_no_cust;
    private BigDecimal r30_b2_high_risk_deposit;
    private BigDecimal r30_b2_tot_no_cust;
    private BigDecimal r30_b2_tot_deposit;
    private String r31_b2_cust_deposit;
    private BigDecimal r31_b2_low_risk_no_cust;
    private BigDecimal r31_b2_low_risk_deposit;
    private BigDecimal r31_b2_medi_risk_no_cust;
    private BigDecimal r31_b2_medi_risk_deposit;
    private BigDecimal r31_b2_high_risk_no_cust;
    private BigDecimal r31_b2_high_risk_deposit;
    private BigDecimal r31_b2_tot_no_cust;
    private BigDecimal r31_b2_tot_deposit;
    private String r32_b2_cust_deposit;
    private BigDecimal r32_b2_low_risk_no_cust;
    private BigDecimal r32_b2_low_risk_deposit;
    private BigDecimal r32_b2_medi_risk_no_cust;
    private BigDecimal r32_b2_medi_risk_deposit;
    private BigDecimal r32_b2_high_risk_no_cust;
    private BigDecimal r32_b2_high_risk_deposit;
    private BigDecimal r32_b2_tot_no_cust;
    private BigDecimal r32_b2_tot_deposit;
    private String r33_b2_cust_deposit;
    private BigDecimal r33_b2_low_risk_no_cust;
    private BigDecimal r33_b2_low_risk_deposit;
    private BigDecimal r33_b2_medi_risk_no_cust;
    private BigDecimal r33_b2_medi_risk_deposit;
    private BigDecimal r33_b2_high_risk_no_cust;
    private BigDecimal r33_b2_high_risk_deposit;
    private BigDecimal r33_b2_tot_no_cust;
    private BigDecimal r33_b2_tot_deposit;
    private String r39_cust_base_cust_deposit;
    private BigDecimal r39_cust_base_no_cust;
    private BigDecimal r39_cust_base_deposits;
    private String r40_cust_base_cust_deposit;
    private BigDecimal r40_cust_base_no_cust;
    private BigDecimal r40_cust_base_deposits;
    private String r41_cust_base_cust_deposit;
    private BigDecimal r41_cust_base_no_cust;
    private BigDecimal r41_cust_base_deposits;
    private String r50_brkdown_typ_of_cust;
    private BigDecimal r50_brkdown_num_of_cust;
    private BigDecimal r50_brkdown_tot_depo;
    private String r51_brkdown_typ_of_cust;
    private BigDecimal r51_brkdown_num_of_cust;
    private BigDecimal r51_brkdown_tot_depo;
    private String r52_brkdown_typ_of_cust;
    private BigDecimal r52_brkdown_num_of_cust;
    private BigDecimal r52_brkdown_tot_depo;
    private String r53_brkdown_typ_of_cust;
    private BigDecimal r53_brkdown_num_of_cust;
    private BigDecimal r53_brkdown_tot_depo;
    private String r54_brkdown_typ_of_cust;
    private BigDecimal r54_brkdown_num_of_cust;
    private BigDecimal r54_brkdown_tot_depo;
    private String r55_brkdown_typ_of_cust;
    private BigDecimal r55_brkdown_num_of_cust;
    private BigDecimal r55_brkdown_tot_depo;
    private String r56_brkdown_typ_of_cust;
    private BigDecimal r56_brkdown_num_of_cust;
    private BigDecimal r56_brkdown_tot_depo;
    private String r57_brkdown_typ_of_cust;
    private BigDecimal r57_brkdown_num_of_cust;
    private BigDecimal r57_brkdown_tot_depo;
    private String r58_brkdown_typ_of_cust;
    private BigDecimal r58_brkdown_num_of_cust;
    private BigDecimal r58_brkdown_tot_depo;
    private String r59_brkdown_typ_of_cust;
    private BigDecimal r59_brkdown_num_of_cust;
    private BigDecimal r59_brkdown_tot_depo;
    private String r60_brkdown_typ_of_cust;
    private BigDecimal r60_brkdown_num_of_cust;
    private BigDecimal r60_brkdown_tot_depo;
    private String r61_brkdown_typ_of_cust;
    private BigDecimal r61_brkdown_num_of_cust;
    private BigDecimal r61_brkdown_tot_depo;
    private String r62_brkdown_typ_of_cust;
    private BigDecimal r62_brkdown_num_of_cust;
    private BigDecimal r62_brkdown_tot_depo;
    private String r63_brkdown_typ_of_cust;
    private BigDecimal r63_brkdown_num_of_cust;
    private BigDecimal r63_brkdown_tot_depo;
    private String r64_brkdown_typ_of_cust;
    private BigDecimal r64_brkdown_num_of_cust;
    private BigDecimal r64_brkdown_tot_depo;
    private String r65_brkdown_typ_of_cust;
    private BigDecimal r65_brkdown_num_of_cust;
    private BigDecimal r65_brkdown_tot_depo;
    private String r66_brkdown_typ_of_cust;
    private BigDecimal r66_brkdown_num_of_cust;
    private BigDecimal r66_brkdown_tot_depo;
    private String r67_brkdown_typ_of_cust;
    private BigDecimal r67_brkdown_num_of_cust;
    private BigDecimal r67_brkdown_tot_depo;
    private String r68_brkdown_typ_of_cust;
    private BigDecimal r68_brkdown_num_of_cust;
    private BigDecimal r68_brkdown_tot_depo;
    private String r69_brkdown_typ_of_cust;
    private BigDecimal r69_brkdown_num_of_cust;
    private BigDecimal r69_brkdown_tot_depo;
    private String r70_brkdown_typ_of_cust;
    private BigDecimal r70_brkdown_num_of_cust;
    private BigDecimal r70_brkdown_tot_depo;
    private String r71_brkdown_typ_of_cust;
    private BigDecimal r71_brkdown_num_of_cust;
    private BigDecimal r71_brkdown_tot_depo;
    private String r72_brkdown_typ_of_cust;
    private BigDecimal r72_brkdown_num_of_cust;
    private BigDecimal r72_brkdown_tot_depo;
    private String r73_brkdown_typ_of_cust;
    private BigDecimal r73_brkdown_num_of_cust;
    private BigDecimal r73_brkdown_tot_depo;
    private String r74_brkdown_typ_of_cust;
    private BigDecimal r74_brkdown_num_of_cust;
    private BigDecimal r74_brkdown_tot_depo;
    private String r75_brkdown_typ_of_cust;
    private BigDecimal r75_brkdown_num_of_cust;
    private BigDecimal r75_brkdown_tot_depo;
    private BigDecimal r82_e1_tot_no_cust;
    private BigDecimal r82_e1_loan_on_bal_expo;
    private BigDecimal r82_e1_deposit;
    private BigDecimal r82_e1_funds_behalf_cust;
    private BigDecimal r82_e1_turnover;
    private BigDecimal r83_e1_tot_no_cust;
    private BigDecimal r83_e1_loan_on_bal_expo;
    private BigDecimal r83_e1_deposit;
    private BigDecimal r83_e1_funds_behalf_cust;
    private BigDecimal r83_e1_turnover;
    private BigDecimal r89_e2_tot_no_cust;
    private BigDecimal r89_e2_loans_bal_expo;
    private BigDecimal r89_e2_deposit;
    private BigDecimal r89_e2_funds_behalf_cust;
    private BigDecimal r89_e2_turnover;
    private BigDecimal r90_e2_tot_no_cust;
    private BigDecimal r90_e2_loans_bal_expo;
    private BigDecimal r90_e2_deposit;
    private BigDecimal r90_e2_funds_behalf_cust;
    private BigDecimal r90_e2_turnover;
    private BigDecimal r96_e3_tot_no_cust;
    private BigDecimal r96_e3_loans_bal_expo;
    private BigDecimal r96_e3_deposit;
    private BigDecimal r96_e3_funds_behalf_cust;
    private BigDecimal r96_e3_turnover;
    private BigDecimal r97_e3_tot_no_cust;
    private BigDecimal r97_e3_loans_bal_expo;
    private BigDecimal r97_e3_deposit;
    private BigDecimal r97_e3_funds_behalf_cust;
    private BigDecimal r97_e3_turnover;
    private BigDecimal r104_f_num_of_cust;
    private BigDecimal r104_f_loans_bal_expo;
    private BigDecimal r104_f_deposit;
    private BigDecimal r104_f_funds_behalf_cust;
    private BigDecimal r104_f_turnover;
    private BigDecimal r105_f_num_of_cust;
    private BigDecimal r105_f_loans_bal_expo;
    private BigDecimal r105_f_deposit;
    private BigDecimal r105_f_funds_behalf_cust;
    private BigDecimal r105_f_turnover;
    private String r111_g1_pay_mech;
    private String r111_g1_pay_mechanisum;
    private BigDecimal r111_g1_num_trans;
    private BigDecimal r111_g1_val_trans;
    private String r112_g1_pay_mech;
    private String r112_g1_pay_mechanisum;
    private BigDecimal r112_g1_num_trans;
    private BigDecimal r112_g1_val_trans;
    private String r113_g1_pay_mech;
    private String r113_g1_pay_mechanisum;
    private BigDecimal r113_g1_num_trans;
    private BigDecimal r113_g1_val_trans;
    private String r114_g1_pay_mech;
    private String r114_g1_pay_mechanisum;
    private BigDecimal r114_g1_num_trans;
    private BigDecimal r114_g1_val_trans;
    private String r115_g1_pay_mech;
    private String r115_g1_pay_mechanisum;
    private BigDecimal r115_g1_num_trans;
    private BigDecimal r115_g1_val_trans;
    private String r116_g1_pay_mech;
    private String r116_g1_pay_mechanisum;
    private BigDecimal r116_g1_num_trans;
    private BigDecimal r116_g1_val_trans;
    private String r117_g1_pay_mech;
    private String r117_g1_pay_mechanisum;
    private BigDecimal r117_g1_num_trans;
    private BigDecimal r117_g1_val_trans;
    private String r118_g1_pay_mech;
    private String r118_g1_pay_mechanisum;
    private BigDecimal r118_g1_num_trans;
    private BigDecimal r118_g1_val_trans;
    private String r119_g1_pay_mech;
    private String r119_g1_pay_mechanisum;
    private BigDecimal r119_g1_num_trans;
    private BigDecimal r119_g1_val_trans;
    private String r120_g1_pay_mech;
    private String r120_g1_pay_mechanisum;
    private BigDecimal r120_g1_num_trans;
    private BigDecimal r120_g1_val_trans;
    private String r121_g1_pay_mech;
    private String r121_g1_pay_mechanisum;
    private BigDecimal r121_g1_num_trans;
    private BigDecimal r121_g1_val_trans;
    private String r122_g1_pay_mech;
    private String r122_g1_pay_mechanisum;
    private BigDecimal r122_g1_num_trans;
    private BigDecimal r122_g1_val_trans;
    private String r123_g1_pay_mech;
    private String r123_g1_pay_mechanisum;
    private BigDecimal r123_g1_num_trans;
    private BigDecimal r123_g1_val_trans;
    private String r124_g1_pay_mech;
    private String r124_g1_pay_mechanisum;
    private BigDecimal r124_g1_num_trans;
    private BigDecimal r124_g1_val_trans;
    private String r125_g1_pay_mech;
    private String r125_g1_pay_mechanisum;
    private BigDecimal r125_g1_num_trans;
    private BigDecimal r125_g1_val_trans;
    private String r126_g1_pay_mech;
    private String r126_g1_pay_mechanisum;
    private BigDecimal r126_g1_num_trans;
    private BigDecimal r126_g1_val_trans;
    private String r127_g1_pay_mech;
    private String r127_g1_pay_mechanisum;
    private BigDecimal r127_g1_num_trans;
    private BigDecimal r127_g1_val_trans;
    private String r128_g1_pay_mech;
    private String r128_g1_pay_mechanisum;
    private BigDecimal r128_g1_num_trans;
    private BigDecimal r128_g1_val_trans;

    private String r135_g2_foreign_exchange;
    private String r135_g2_fore_exchange;
    private BigDecimal r135_g2_val_transac;

    private String r136_g2_fore_exchange;
    private BigDecimal r136_g2_val_transac;

    private String r138_g2_foreign_exchange;
    private String r138_g2_fore_exchange;
    private BigDecimal r138_g2_val_transac;

    private String r139_g2_fore_exchange;
    private BigDecimal r139_g2_val_transac;
    
    private String r144_h_types;
    private BigDecimal r144_h_amount;
    private String r145_h_types;
    private BigDecimal r145_h_amount;
    private String r146_h_types;
    private BigDecimal r146_h_amount;
    private String r147_h_types;
    private BigDecimal r147_h_amount;
    private String r148_h_types;
    private BigDecimal r148_h_amount;
    private String r153_i_product_serv;
    private BigDecimal r153_i_no_cust;
    private BigDecimal r153_i_outs_bal;
    private BigDecimal r153_i_turnover;
    private String r154_i_product_serv;
    private BigDecimal r154_i_no_cust;
    private BigDecimal r154_i_outs_bal;
    private BigDecimal r154_i_turnover;
    private String r155_i_product_serv;
    private BigDecimal r155_i_no_cust;
    private BigDecimal r155_i_outs_bal;
    private BigDecimal r155_i_turnover;
    private String r161_j_trade_finc_prod;
    private BigDecimal r161_j_num_of_cust;
    private BigDecimal r161_j_commitment_at_jun;
    private String r162_j_trade_finc_prod;
    private BigDecimal r162_j_num_of_cust;
    private BigDecimal r162_j_commitment_at_jun;
    private String r163_j_trade_finc_prod;
    private BigDecimal r163_j_num_of_cust;
    private BigDecimal r163_j_commitment_at_jun;
    private String r164_j_trade_finc_prod;
    private BigDecimal r164_j_num_of_cust;
    private BigDecimal r164_j_commitment_at_jun;
    private String r170_k_pay_mechanism;
    private String r170_k_pay_mech;
    private BigDecimal r170_k_num_of_trans;
    private BigDecimal r170_k_value_of_trans;
    private String r171_k_pay_mech;
    private BigDecimal r171_k_num_of_trans;
    private BigDecimal r171_k_value_of_trans;
    private String r172_k_pay_mechanism;
    private String r172_k_pay_mech;
    private BigDecimal r172_k_num_of_trans;
    private BigDecimal r172_k_value_of_trans;
    private String r179_l_transac_report;
    private BigDecimal r179_l_num_of_transac;
    private String r180_l_transac_report;
    private BigDecimal r180_l_num_of_transac;
    private String r181_l_transac_report;
    private BigDecimal r181_l_num_of_transac;
    private String r187_m_transac_life;
    private BigDecimal r187_m_num_of_transac;
    private BigDecimal r187_m_val_of_transac;
    private String r192_n_transac_life;
    private BigDecimal r192_n_num_of_transac;
    private BigDecimal r192_n_val_of_transac;
    private String r196_o_transac_life;
    private BigDecimal r196_o_num_of_transac;
    private BigDecimal r196_o_val_of_transac;
    private String r201_p_transac_life;
    private BigDecimal r201_p_num_of_transac;
    private BigDecimal r201_p_val_of_transac;
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

    public String getR11_cust_base_deposit() {
        return r11_cust_base_deposit;
    }

    public void setR11_cust_base_deposit(String r11_cust_base_deposit) {
        this.r11_cust_base_deposit = r11_cust_base_deposit;
    }

    public BigDecimal getR11_cust_base_no_of_acct() {
        return r11_cust_base_no_of_acct;
    }

    public void setR11_cust_base_no_of_acct(BigDecimal r11_cust_base_no_of_acct) {
        this.r11_cust_base_no_of_acct = r11_cust_base_no_of_acct;
    }

    public BigDecimal getR11_cust_base_tot_dep() {
        return r11_cust_base_tot_dep;
    }

    public void setR11_cust_base_tot_dep(BigDecimal r11_cust_base_tot_dep) {
        this.r11_cust_base_tot_dep = r11_cust_base_tot_dep;
    }

    public String getR12_cust_base_deposit() {
        return r12_cust_base_deposit;
    }

    public void setR12_cust_base_deposit(String r12_cust_base_deposit) {
        this.r12_cust_base_deposit = r12_cust_base_deposit;
    }

    public BigDecimal getR12_cust_base_no_of_acct() {
        return r12_cust_base_no_of_acct;
    }

    public void setR12_cust_base_no_of_acct(BigDecimal r12_cust_base_no_of_acct) {
        this.r12_cust_base_no_of_acct = r12_cust_base_no_of_acct;
    }

    public BigDecimal getR12_cust_base_tot_dep() {
        return r12_cust_base_tot_dep;
    }

    public void setR12_cust_base_tot_dep(BigDecimal r12_cust_base_tot_dep) {
        this.r12_cust_base_tot_dep = r12_cust_base_tot_dep;
    }

    public String getR13_cust_base_deposit() {
        return r13_cust_base_deposit;
    }

    public void setR13_cust_base_deposit(String r13_cust_base_deposit) {
        this.r13_cust_base_deposit = r13_cust_base_deposit;
    }

    public BigDecimal getR13_cust_base_no_of_acct() {
        return r13_cust_base_no_of_acct;
    }

    public void setR13_cust_base_no_of_acct(BigDecimal r13_cust_base_no_of_acct) {
        this.r13_cust_base_no_of_acct = r13_cust_base_no_of_acct;
    }

    public BigDecimal getR13_cust_base_tot_dep() {
        return r13_cust_base_tot_dep;
    }

    public void setR13_cust_base_tot_dep(BigDecimal r13_cust_base_tot_dep) {
        this.r13_cust_base_tot_dep = r13_cust_base_tot_dep;
    }

    public String getR14_cust_base_deposit() {
        return r14_cust_base_deposit;
    }

    public void setR14_cust_base_deposit(String r14_cust_base_deposit) {
        this.r14_cust_base_deposit = r14_cust_base_deposit;
    }

    public BigDecimal getR14_cust_base_no_of_acct() {
        return r14_cust_base_no_of_acct;
    }

    public void setR14_cust_base_no_of_acct(BigDecimal r14_cust_base_no_of_acct) {
        this.r14_cust_base_no_of_acct = r14_cust_base_no_of_acct;
    }

    public BigDecimal getR14_cust_base_tot_dep() {
        return r14_cust_base_tot_dep;
    }

    public void setR14_cust_base_tot_dep(BigDecimal r14_cust_base_tot_dep) {
        this.r14_cust_base_tot_dep = r14_cust_base_tot_dep;
    }

    public String getR15_cust_base_deposit() {
        return r15_cust_base_deposit;
    }

    public void setR15_cust_base_deposit(String r15_cust_base_deposit) {
        this.r15_cust_base_deposit = r15_cust_base_deposit;
    }

    public BigDecimal getR15_cust_base_no_of_acct() {
        return r15_cust_base_no_of_acct;
    }

    public void setR15_cust_base_no_of_acct(BigDecimal r15_cust_base_no_of_acct) {
        this.r15_cust_base_no_of_acct = r15_cust_base_no_of_acct;
    }

    public BigDecimal getR15_cust_base_tot_dep() {
        return r15_cust_base_tot_dep;
    }

    public void setR15_cust_base_tot_dep(BigDecimal r15_cust_base_tot_dep) {
        this.r15_cust_base_tot_dep = r15_cust_base_tot_dep;
    }

    public String getR21_cust_risk_pro_deposit() {
        return r21_cust_risk_pro_deposit;
    }

    public void setR21_cust_risk_pro_deposit(String r21_cust_risk_pro_deposit) {
        this.r21_cust_risk_pro_deposit = r21_cust_risk_pro_deposit;
    }

    public BigDecimal getR21_cust_risk_pro_num_of_cust() {
        return r21_cust_risk_pro_num_of_cust;
    }

    public void setR21_cust_risk_pro_num_of_cust(BigDecimal r21_cust_risk_pro_num_of_cust) {
        this.r21_cust_risk_pro_num_of_cust = r21_cust_risk_pro_num_of_cust;
    }

    public BigDecimal getR21_cust_risk_pro_value() {
        return r21_cust_risk_pro_value;
    }

    public void setR21_cust_risk_pro_value(BigDecimal r21_cust_risk_pro_value) {
        this.r21_cust_risk_pro_value = r21_cust_risk_pro_value;
    }

    public String getR22_cust_risk_pro_deposit() {
        return r22_cust_risk_pro_deposit;
    }

    public void setR22_cust_risk_pro_deposit(String r22_cust_risk_pro_deposit) {
        this.r22_cust_risk_pro_deposit = r22_cust_risk_pro_deposit;
    }

    public BigDecimal getR22_cust_risk_pro_num_of_cust() {
        return r22_cust_risk_pro_num_of_cust;
    }

    public void setR22_cust_risk_pro_num_of_cust(BigDecimal r22_cust_risk_pro_num_of_cust) {
        this.r22_cust_risk_pro_num_of_cust = r22_cust_risk_pro_num_of_cust;
    }

    public BigDecimal getR22_cust_risk_pro_value() {
        return r22_cust_risk_pro_value;
    }

    public void setR22_cust_risk_pro_value(BigDecimal r22_cust_risk_pro_value) {
        this.r22_cust_risk_pro_value = r22_cust_risk_pro_value;
    }

    public String getR23_cust_risk_pro_deposit() {
        return r23_cust_risk_pro_deposit;
    }

    public void setR23_cust_risk_pro_deposit(String r23_cust_risk_pro_deposit) {
        this.r23_cust_risk_pro_deposit = r23_cust_risk_pro_deposit;
    }

    public BigDecimal getR23_cust_risk_pro_num_of_cust() {
        return r23_cust_risk_pro_num_of_cust;
    }

    public void setR23_cust_risk_pro_num_of_cust(BigDecimal r23_cust_risk_pro_num_of_cust) {
        this.r23_cust_risk_pro_num_of_cust = r23_cust_risk_pro_num_of_cust;
    }

    public BigDecimal getR23_cust_risk_pro_value() {
        return r23_cust_risk_pro_value;
    }

    public void setR23_cust_risk_pro_value(BigDecimal r23_cust_risk_pro_value) {
        this.r23_cust_risk_pro_value = r23_cust_risk_pro_value;
    }

    public String getR24_cust_risk_pro_deposit() {
        return r24_cust_risk_pro_deposit;
    }

    public void setR24_cust_risk_pro_deposit(String r24_cust_risk_pro_deposit) {
        this.r24_cust_risk_pro_deposit = r24_cust_risk_pro_deposit;
    }

    public BigDecimal getR24_cust_risk_pro_num_of_cust() {
        return r24_cust_risk_pro_num_of_cust;
    }

    public void setR24_cust_risk_pro_num_of_cust(BigDecimal r24_cust_risk_pro_num_of_cust) {
        this.r24_cust_risk_pro_num_of_cust = r24_cust_risk_pro_num_of_cust;
    }

    public BigDecimal getR24_cust_risk_pro_value() {
        return r24_cust_risk_pro_value;
    }

    public void setR24_cust_risk_pro_value(BigDecimal r24_cust_risk_pro_value) {
        this.r24_cust_risk_pro_value = r24_cust_risk_pro_value;
    }

    public String getR30_b2_cust_deposit() {
        return r30_b2_cust_deposit;
    }

    public void setR30_b2_cust_deposit(String r30_b2_cust_deposit) {
        this.r30_b2_cust_deposit = r30_b2_cust_deposit;
    }

    public BigDecimal getR30_b2_low_risk_no_cust() {
        return r30_b2_low_risk_no_cust;
    }

    public void setR30_b2_low_risk_no_cust(BigDecimal r30_b2_low_risk_no_cust) {
        this.r30_b2_low_risk_no_cust = r30_b2_low_risk_no_cust;
    }

    public BigDecimal getR30_b2_low_risk_deposit() {
        return r30_b2_low_risk_deposit;
    }

    public void setR30_b2_low_risk_deposit(BigDecimal r30_b2_low_risk_deposit) {
        this.r30_b2_low_risk_deposit = r30_b2_low_risk_deposit;
    }

    public BigDecimal getR30_b2_medi_risk_no_cust() {
        return r30_b2_medi_risk_no_cust;
    }

    public void setR30_b2_medi_risk_no_cust(BigDecimal r30_b2_medi_risk_no_cust) {
        this.r30_b2_medi_risk_no_cust = r30_b2_medi_risk_no_cust;
    }

    public BigDecimal getR30_b2_medi_risk_deposit() {
        return r30_b2_medi_risk_deposit;
    }

    public void setR30_b2_medi_risk_deposit(BigDecimal r30_b2_medi_risk_deposit) {
        this.r30_b2_medi_risk_deposit = r30_b2_medi_risk_deposit;
    }

    public BigDecimal getR30_b2_high_risk_no_cust() {
        return r30_b2_high_risk_no_cust;
    }

    public void setR30_b2_high_risk_no_cust(BigDecimal r30_b2_high_risk_no_cust) {
        this.r30_b2_high_risk_no_cust = r30_b2_high_risk_no_cust;
    }

    public BigDecimal getR30_b2_high_risk_deposit() {
        return r30_b2_high_risk_deposit;
    }

    public void setR30_b2_high_risk_deposit(BigDecimal r30_b2_high_risk_deposit) {
        this.r30_b2_high_risk_deposit = r30_b2_high_risk_deposit;
    }

    public BigDecimal getR30_b2_tot_no_cust() {
        return r30_b2_tot_no_cust;
    }

    public void setR30_b2_tot_no_cust(BigDecimal r30_b2_tot_no_cust) {
        this.r30_b2_tot_no_cust = r30_b2_tot_no_cust;
    }

    public BigDecimal getR30_b2_tot_deposit() {
        return r30_b2_tot_deposit;
    }

    public void setR30_b2_tot_deposit(BigDecimal r30_b2_tot_deposit) {
        this.r30_b2_tot_deposit = r30_b2_tot_deposit;
    }

    public String getR31_b2_cust_deposit() {
        return r31_b2_cust_deposit;
    }

    public void setR31_b2_cust_deposit(String r31_b2_cust_deposit) {
        this.r31_b2_cust_deposit = r31_b2_cust_deposit;
    }

    public BigDecimal getR31_b2_low_risk_no_cust() {
        return r31_b2_low_risk_no_cust;
    }

    public void setR31_b2_low_risk_no_cust(BigDecimal r31_b2_low_risk_no_cust) {
        this.r31_b2_low_risk_no_cust = r31_b2_low_risk_no_cust;
    }

    public BigDecimal getR31_b2_low_risk_deposit() {
        return r31_b2_low_risk_deposit;
    }

    public void setR31_b2_low_risk_deposit(BigDecimal r31_b2_low_risk_deposit) {
        this.r31_b2_low_risk_deposit = r31_b2_low_risk_deposit;
    }

    public BigDecimal getR31_b2_medi_risk_no_cust() {
        return r31_b2_medi_risk_no_cust;
    }

    public void setR31_b2_medi_risk_no_cust(BigDecimal r31_b2_medi_risk_no_cust) {
        this.r31_b2_medi_risk_no_cust = r31_b2_medi_risk_no_cust;
    }

    public BigDecimal getR31_b2_medi_risk_deposit() {
        return r31_b2_medi_risk_deposit;
    }

    public void setR31_b2_medi_risk_deposit(BigDecimal r31_b2_medi_risk_deposit) {
        this.r31_b2_medi_risk_deposit = r31_b2_medi_risk_deposit;
    }

    public BigDecimal getR31_b2_high_risk_no_cust() {
        return r31_b2_high_risk_no_cust;
    }

    public void setR31_b2_high_risk_no_cust(BigDecimal r31_b2_high_risk_no_cust) {
        this.r31_b2_high_risk_no_cust = r31_b2_high_risk_no_cust;
    }

    public BigDecimal getR31_b2_high_risk_deposit() {
        return r31_b2_high_risk_deposit;
    }

    public void setR31_b2_high_risk_deposit(BigDecimal r31_b2_high_risk_deposit) {
        this.r31_b2_high_risk_deposit = r31_b2_high_risk_deposit;
    }

    public BigDecimal getR31_b2_tot_no_cust() {
        return r31_b2_tot_no_cust;
    }

    public void setR31_b2_tot_no_cust(BigDecimal r31_b2_tot_no_cust) {
        this.r31_b2_tot_no_cust = r31_b2_tot_no_cust;
    }

    public BigDecimal getR31_b2_tot_deposit() {
        return r31_b2_tot_deposit;
    }

    public void setR31_b2_tot_deposit(BigDecimal r31_b2_tot_deposit) {
        this.r31_b2_tot_deposit = r31_b2_tot_deposit;
    }

    public String getR32_b2_cust_deposit() {
        return r32_b2_cust_deposit;
    }

    public void setR32_b2_cust_deposit(String r32_b2_cust_deposit) {
        this.r32_b2_cust_deposit = r32_b2_cust_deposit;
    }

    public BigDecimal getR32_b2_low_risk_no_cust() {
        return r32_b2_low_risk_no_cust;
    }

    public void setR32_b2_low_risk_no_cust(BigDecimal r32_b2_low_risk_no_cust) {
        this.r32_b2_low_risk_no_cust = r32_b2_low_risk_no_cust;
    }

    public BigDecimal getR32_b2_low_risk_deposit() {
        return r32_b2_low_risk_deposit;
    }

    public void setR32_b2_low_risk_deposit(BigDecimal r32_b2_low_risk_deposit) {
        this.r32_b2_low_risk_deposit = r32_b2_low_risk_deposit;
    }

    public BigDecimal getR32_b2_medi_risk_no_cust() {
        return r32_b2_medi_risk_no_cust;
    }

    public void setR32_b2_medi_risk_no_cust(BigDecimal r32_b2_medi_risk_no_cust) {
        this.r32_b2_medi_risk_no_cust = r32_b2_medi_risk_no_cust;
    }

    public BigDecimal getR32_b2_medi_risk_deposit() {
        return r32_b2_medi_risk_deposit;
    }

    public void setR32_b2_medi_risk_deposit(BigDecimal r32_b2_medi_risk_deposit) {
        this.r32_b2_medi_risk_deposit = r32_b2_medi_risk_deposit;
    }

    public BigDecimal getR32_b2_high_risk_no_cust() {
        return r32_b2_high_risk_no_cust;
    }

    public void setR32_b2_high_risk_no_cust(BigDecimal r32_b2_high_risk_no_cust) {
        this.r32_b2_high_risk_no_cust = r32_b2_high_risk_no_cust;
    }

    public BigDecimal getR32_b2_high_risk_deposit() {
        return r32_b2_high_risk_deposit;
    }

    public void setR32_b2_high_risk_deposit(BigDecimal r32_b2_high_risk_deposit) {
        this.r32_b2_high_risk_deposit = r32_b2_high_risk_deposit;
    }

    public BigDecimal getR32_b2_tot_no_cust() {
        return r32_b2_tot_no_cust;
    }

    public void setR32_b2_tot_no_cust(BigDecimal r32_b2_tot_no_cust) {
        this.r32_b2_tot_no_cust = r32_b2_tot_no_cust;
    }

    public BigDecimal getR32_b2_tot_deposit() {
        return r32_b2_tot_deposit;
    }

    public void setR32_b2_tot_deposit(BigDecimal r32_b2_tot_deposit) {
        this.r32_b2_tot_deposit = r32_b2_tot_deposit;
    }

    public String getR33_b2_cust_deposit() {
        return r33_b2_cust_deposit;
    }

    public void setR33_b2_cust_deposit(String r33_b2_cust_deposit) {
        this.r33_b2_cust_deposit = r33_b2_cust_deposit;
    }

    public BigDecimal getR33_b2_low_risk_no_cust() {
        return r33_b2_low_risk_no_cust;
    }

    public void setR33_b2_low_risk_no_cust(BigDecimal r33_b2_low_risk_no_cust) {
        this.r33_b2_low_risk_no_cust = r33_b2_low_risk_no_cust;
    }

    public BigDecimal getR33_b2_low_risk_deposit() {
        return r33_b2_low_risk_deposit;
    }

    public void setR33_b2_low_risk_deposit(BigDecimal r33_b2_low_risk_deposit) {
        this.r33_b2_low_risk_deposit = r33_b2_low_risk_deposit;
    }

    public BigDecimal getR33_b2_medi_risk_no_cust() {
        return r33_b2_medi_risk_no_cust;
    }

    public void setR33_b2_medi_risk_no_cust(BigDecimal r33_b2_medi_risk_no_cust) {
        this.r33_b2_medi_risk_no_cust = r33_b2_medi_risk_no_cust;
    }

    public BigDecimal getR33_b2_medi_risk_deposit() {
        return r33_b2_medi_risk_deposit;
    }

    public void setR33_b2_medi_risk_deposit(BigDecimal r33_b2_medi_risk_deposit) {
        this.r33_b2_medi_risk_deposit = r33_b2_medi_risk_deposit;
    }

    public BigDecimal getR33_b2_high_risk_no_cust() {
        return r33_b2_high_risk_no_cust;
    }

    public void setR33_b2_high_risk_no_cust(BigDecimal r33_b2_high_risk_no_cust) {
        this.r33_b2_high_risk_no_cust = r33_b2_high_risk_no_cust;
    }

    public BigDecimal getR33_b2_high_risk_deposit() {
        return r33_b2_high_risk_deposit;
    }

    public void setR33_b2_high_risk_deposit(BigDecimal r33_b2_high_risk_deposit) {
        this.r33_b2_high_risk_deposit = r33_b2_high_risk_deposit;
    }

    public BigDecimal getR33_b2_tot_no_cust() {
        return r33_b2_tot_no_cust;
    }

    public void setR33_b2_tot_no_cust(BigDecimal r33_b2_tot_no_cust) {
        this.r33_b2_tot_no_cust = r33_b2_tot_no_cust;
    }

    public BigDecimal getR33_b2_tot_deposit() {
        return r33_b2_tot_deposit;
    }

    public void setR33_b2_tot_deposit(BigDecimal r33_b2_tot_deposit) {
        this.r33_b2_tot_deposit = r33_b2_tot_deposit;
    }

    public String getR39_cust_base_cust_deposit() {
        return r39_cust_base_cust_deposit;
    }

    public void setR39_cust_base_cust_deposit(String r39_cust_base_cust_deposit) {
        this.r39_cust_base_cust_deposit = r39_cust_base_cust_deposit;
    }

    public BigDecimal getR39_cust_base_no_cust() {
        return r39_cust_base_no_cust;
    }

    public void setR39_cust_base_no_cust(BigDecimal r39_cust_base_no_cust) {
        this.r39_cust_base_no_cust = r39_cust_base_no_cust;
    }

    public BigDecimal getR39_cust_base_deposits() {
        return r39_cust_base_deposits;
    }

    public void setR39_cust_base_deposits(BigDecimal r39_cust_base_deposits) {
        this.r39_cust_base_deposits = r39_cust_base_deposits;
    }

    public String getR40_cust_base_cust_deposit() {
        return r40_cust_base_cust_deposit;
    }

    public void setR40_cust_base_cust_deposit(String r40_cust_base_cust_deposit) {
        this.r40_cust_base_cust_deposit = r40_cust_base_cust_deposit;
    }

    public BigDecimal getR40_cust_base_no_cust() {
        return r40_cust_base_no_cust;
    }

    public void setR40_cust_base_no_cust(BigDecimal r40_cust_base_no_cust) {
        this.r40_cust_base_no_cust = r40_cust_base_no_cust;
    }

    public BigDecimal getR40_cust_base_deposits() {
        return r40_cust_base_deposits;
    }

    public void setR40_cust_base_deposits(BigDecimal r40_cust_base_deposits) {
        this.r40_cust_base_deposits = r40_cust_base_deposits;
    }

    public String getR41_cust_base_cust_deposit() {
        return r41_cust_base_cust_deposit;
    }

    public void setR41_cust_base_cust_deposit(String r41_cust_base_cust_deposit) {
        this.r41_cust_base_cust_deposit = r41_cust_base_cust_deposit;
    }

    public BigDecimal getR41_cust_base_no_cust() {
        return r41_cust_base_no_cust;
    }

    public void setR41_cust_base_no_cust(BigDecimal r41_cust_base_no_cust) {
        this.r41_cust_base_no_cust = r41_cust_base_no_cust;
    }

    public BigDecimal getR41_cust_base_deposits() {
        return r41_cust_base_deposits;
    }

    public void setR41_cust_base_deposits(BigDecimal r41_cust_base_deposits) {
        this.r41_cust_base_deposits = r41_cust_base_deposits;
    }

    public String getR50_brkdown_typ_of_cust() {
        return r50_brkdown_typ_of_cust;
    }

    public void setR50_brkdown_typ_of_cust(String r50_brkdown_typ_of_cust) {
        this.r50_brkdown_typ_of_cust = r50_brkdown_typ_of_cust;
    }

    public BigDecimal getR50_brkdown_num_of_cust() {
        return r50_brkdown_num_of_cust;
    }

    public void setR50_brkdown_num_of_cust(BigDecimal r50_brkdown_num_of_cust) {
        this.r50_brkdown_num_of_cust = r50_brkdown_num_of_cust;
    }

    public BigDecimal getR50_brkdown_tot_depo() {
        return r50_brkdown_tot_depo;
    }

    public void setR50_brkdown_tot_depo(BigDecimal r50_brkdown_tot_depo) {
        this.r50_brkdown_tot_depo = r50_brkdown_tot_depo;
    }

    public String getR51_brkdown_typ_of_cust() {
        return r51_brkdown_typ_of_cust;
    }

    public void setR51_brkdown_typ_of_cust(String r51_brkdown_typ_of_cust) {
        this.r51_brkdown_typ_of_cust = r51_brkdown_typ_of_cust;
    }

    public BigDecimal getR51_brkdown_num_of_cust() {
        return r51_brkdown_num_of_cust;
    }

    public void setR51_brkdown_num_of_cust(BigDecimal r51_brkdown_num_of_cust) {
        this.r51_brkdown_num_of_cust = r51_brkdown_num_of_cust;
    }

    public BigDecimal getR51_brkdown_tot_depo() {
        return r51_brkdown_tot_depo;
    }

    public void setR51_brkdown_tot_depo(BigDecimal r51_brkdown_tot_depo) {
        this.r51_brkdown_tot_depo = r51_brkdown_tot_depo;
    }

    public String getR52_brkdown_typ_of_cust() {
        return r52_brkdown_typ_of_cust;
    }

    public void setR52_brkdown_typ_of_cust(String r52_brkdown_typ_of_cust) {
        this.r52_brkdown_typ_of_cust = r52_brkdown_typ_of_cust;
    }

    public BigDecimal getR52_brkdown_num_of_cust() {
        return r52_brkdown_num_of_cust;
    }

    public void setR52_brkdown_num_of_cust(BigDecimal r52_brkdown_num_of_cust) {
        this.r52_brkdown_num_of_cust = r52_brkdown_num_of_cust;
    }

    public BigDecimal getR52_brkdown_tot_depo() {
        return r52_brkdown_tot_depo;
    }

    public void setR52_brkdown_tot_depo(BigDecimal r52_brkdown_tot_depo) {
        this.r52_brkdown_tot_depo = r52_brkdown_tot_depo;
    }

    public String getR53_brkdown_typ_of_cust() {
        return r53_brkdown_typ_of_cust;
    }

    public void setR53_brkdown_typ_of_cust(String r53_brkdown_typ_of_cust) {
        this.r53_brkdown_typ_of_cust = r53_brkdown_typ_of_cust;
    }

    public BigDecimal getR53_brkdown_num_of_cust() {
        return r53_brkdown_num_of_cust;
    }

    public void setR53_brkdown_num_of_cust(BigDecimal r53_brkdown_num_of_cust) {
        this.r53_brkdown_num_of_cust = r53_brkdown_num_of_cust;
    }

    public BigDecimal getR53_brkdown_tot_depo() {
        return r53_brkdown_tot_depo;
    }

    public void setR53_brkdown_tot_depo(BigDecimal r53_brkdown_tot_depo) {
        this.r53_brkdown_tot_depo = r53_brkdown_tot_depo;
    }

    public String getR54_brkdown_typ_of_cust() {
        return r54_brkdown_typ_of_cust;
    }

    public void setR54_brkdown_typ_of_cust(String r54_brkdown_typ_of_cust) {
        this.r54_brkdown_typ_of_cust = r54_brkdown_typ_of_cust;
    }

    public BigDecimal getR54_brkdown_num_of_cust() {
        return r54_brkdown_num_of_cust;
    }

    public void setR54_brkdown_num_of_cust(BigDecimal r54_brkdown_num_of_cust) {
        this.r54_brkdown_num_of_cust = r54_brkdown_num_of_cust;
    }

    public BigDecimal getR54_brkdown_tot_depo() {
        return r54_brkdown_tot_depo;
    }

    public void setR54_brkdown_tot_depo(BigDecimal r54_brkdown_tot_depo) {
        this.r54_brkdown_tot_depo = r54_brkdown_tot_depo;
    }

    public String getR55_brkdown_typ_of_cust() {
        return r55_brkdown_typ_of_cust;
    }

    public void setR55_brkdown_typ_of_cust(String r55_brkdown_typ_of_cust) {
        this.r55_brkdown_typ_of_cust = r55_brkdown_typ_of_cust;
    }

    public BigDecimal getR55_brkdown_num_of_cust() {
        return r55_brkdown_num_of_cust;
    }

    public void setR55_brkdown_num_of_cust(BigDecimal r55_brkdown_num_of_cust) {
        this.r55_brkdown_num_of_cust = r55_brkdown_num_of_cust;
    }

    public BigDecimal getR55_brkdown_tot_depo() {
        return r55_brkdown_tot_depo;
    }

    public void setR55_brkdown_tot_depo(BigDecimal r55_brkdown_tot_depo) {
        this.r55_brkdown_tot_depo = r55_brkdown_tot_depo;
    }

    public String getR56_brkdown_typ_of_cust() {
        return r56_brkdown_typ_of_cust;
    }

    public void setR56_brkdown_typ_of_cust(String r56_brkdown_typ_of_cust) {
        this.r56_brkdown_typ_of_cust = r56_brkdown_typ_of_cust;
    }

    public BigDecimal getR56_brkdown_num_of_cust() {
        return r56_brkdown_num_of_cust;
    }

    public void setR56_brkdown_num_of_cust(BigDecimal r56_brkdown_num_of_cust) {
        this.r56_brkdown_num_of_cust = r56_brkdown_num_of_cust;
    }

    public BigDecimal getR56_brkdown_tot_depo() {
        return r56_brkdown_tot_depo;
    }

    public void setR56_brkdown_tot_depo(BigDecimal r56_brkdown_tot_depo) {
        this.r56_brkdown_tot_depo = r56_brkdown_tot_depo;
    }

    public String getR57_brkdown_typ_of_cust() {
        return r57_brkdown_typ_of_cust;
    }

    public void setR57_brkdown_typ_of_cust(String r57_brkdown_typ_of_cust) {
        this.r57_brkdown_typ_of_cust = r57_brkdown_typ_of_cust;
    }

    public BigDecimal getR57_brkdown_num_of_cust() {
        return r57_brkdown_num_of_cust;
    }

    public void setR57_brkdown_num_of_cust(BigDecimal r57_brkdown_num_of_cust) {
        this.r57_brkdown_num_of_cust = r57_brkdown_num_of_cust;
    }

    public BigDecimal getR57_brkdown_tot_depo() {
        return r57_brkdown_tot_depo;
    }

    public void setR57_brkdown_tot_depo(BigDecimal r57_brkdown_tot_depo) {
        this.r57_brkdown_tot_depo = r57_brkdown_tot_depo;
    }

    public String getR58_brkdown_typ_of_cust() {
        return r58_brkdown_typ_of_cust;
    }

    public void setR58_brkdown_typ_of_cust(String r58_brkdown_typ_of_cust) {
        this.r58_brkdown_typ_of_cust = r58_brkdown_typ_of_cust;
    }

    public BigDecimal getR58_brkdown_num_of_cust() {
        return r58_brkdown_num_of_cust;
    }

    public void setR58_brkdown_num_of_cust(BigDecimal r58_brkdown_num_of_cust) {
        this.r58_brkdown_num_of_cust = r58_brkdown_num_of_cust;
    }

    public BigDecimal getR58_brkdown_tot_depo() {
        return r58_brkdown_tot_depo;
    }

    public void setR58_brkdown_tot_depo(BigDecimal r58_brkdown_tot_depo) {
        this.r58_brkdown_tot_depo = r58_brkdown_tot_depo;
    }

    public String getR59_brkdown_typ_of_cust() {
        return r59_brkdown_typ_of_cust;
    }

    public void setR59_brkdown_typ_of_cust(String r59_brkdown_typ_of_cust) {
        this.r59_brkdown_typ_of_cust = r59_brkdown_typ_of_cust;
    }

    public BigDecimal getR59_brkdown_num_of_cust() {
        return r59_brkdown_num_of_cust;
    }

    public void setR59_brkdown_num_of_cust(BigDecimal r59_brkdown_num_of_cust) {
        this.r59_brkdown_num_of_cust = r59_brkdown_num_of_cust;
    }

    public BigDecimal getR59_brkdown_tot_depo() {
        return r59_brkdown_tot_depo;
    }

    public void setR59_brkdown_tot_depo(BigDecimal r59_brkdown_tot_depo) {
        this.r59_brkdown_tot_depo = r59_brkdown_tot_depo;
    }

    public String getR60_brkdown_typ_of_cust() {
        return r60_brkdown_typ_of_cust;
    }

    public void setR60_brkdown_typ_of_cust(String r60_brkdown_typ_of_cust) {
        this.r60_brkdown_typ_of_cust = r60_brkdown_typ_of_cust;
    }

    public BigDecimal getR60_brkdown_num_of_cust() {
        return r60_brkdown_num_of_cust;
    }

    public void setR60_brkdown_num_of_cust(BigDecimal r60_brkdown_num_of_cust) {
        this.r60_brkdown_num_of_cust = r60_brkdown_num_of_cust;
    }

    public BigDecimal getR60_brkdown_tot_depo() {
        return r60_brkdown_tot_depo;
    }

    public void setR60_brkdown_tot_depo(BigDecimal r60_brkdown_tot_depo) {
        this.r60_brkdown_tot_depo = r60_brkdown_tot_depo;
    }

    public String getR61_brkdown_typ_of_cust() {
        return r61_brkdown_typ_of_cust;
    }

    public void setR61_brkdown_typ_of_cust(String r61_brkdown_typ_of_cust) {
        this.r61_brkdown_typ_of_cust = r61_brkdown_typ_of_cust;
    }

    public BigDecimal getR61_brkdown_num_of_cust() {
        return r61_brkdown_num_of_cust;
    }

    public void setR61_brkdown_num_of_cust(BigDecimal r61_brkdown_num_of_cust) {
        this.r61_brkdown_num_of_cust = r61_brkdown_num_of_cust;
    }

    public BigDecimal getR61_brkdown_tot_depo() {
        return r61_brkdown_tot_depo;
    }

    public void setR61_brkdown_tot_depo(BigDecimal r61_brkdown_tot_depo) {
        this.r61_brkdown_tot_depo = r61_brkdown_tot_depo;
    }

    public String getR62_brkdown_typ_of_cust() {
        return r62_brkdown_typ_of_cust;
    }

    public void setR62_brkdown_typ_of_cust(String r62_brkdown_typ_of_cust) {
        this.r62_brkdown_typ_of_cust = r62_brkdown_typ_of_cust;
    }

    public BigDecimal getR62_brkdown_num_of_cust() {
        return r62_brkdown_num_of_cust;
    }

    public void setR62_brkdown_num_of_cust(BigDecimal r62_brkdown_num_of_cust) {
        this.r62_brkdown_num_of_cust = r62_brkdown_num_of_cust;
    }

    public BigDecimal getR62_brkdown_tot_depo() {
        return r62_brkdown_tot_depo;
    }

    public void setR62_brkdown_tot_depo(BigDecimal r62_brkdown_tot_depo) {
        this.r62_brkdown_tot_depo = r62_brkdown_tot_depo;
    }

    public String getR63_brkdown_typ_of_cust() {
        return r63_brkdown_typ_of_cust;
    }

    public void setR63_brkdown_typ_of_cust(String r63_brkdown_typ_of_cust) {
        this.r63_brkdown_typ_of_cust = r63_brkdown_typ_of_cust;
    }

    public BigDecimal getR63_brkdown_num_of_cust() {
        return r63_brkdown_num_of_cust;
    }

    public void setR63_brkdown_num_of_cust(BigDecimal r63_brkdown_num_of_cust) {
        this.r63_brkdown_num_of_cust = r63_brkdown_num_of_cust;
    }

    public BigDecimal getR63_brkdown_tot_depo() {
        return r63_brkdown_tot_depo;
    }

    public void setR63_brkdown_tot_depo(BigDecimal r63_brkdown_tot_depo) {
        this.r63_brkdown_tot_depo = r63_brkdown_tot_depo;
    }

    public String getR64_brkdown_typ_of_cust() {
        return r64_brkdown_typ_of_cust;
    }

    public void setR64_brkdown_typ_of_cust(String r64_brkdown_typ_of_cust) {
        this.r64_brkdown_typ_of_cust = r64_brkdown_typ_of_cust;
    }

    public BigDecimal getR64_brkdown_num_of_cust() {
        return r64_brkdown_num_of_cust;
    }

    public void setR64_brkdown_num_of_cust(BigDecimal r64_brkdown_num_of_cust) {
        this.r64_brkdown_num_of_cust = r64_brkdown_num_of_cust;
    }

    public BigDecimal getR64_brkdown_tot_depo() {
        return r64_brkdown_tot_depo;
    }

    public void setR64_brkdown_tot_depo(BigDecimal r64_brkdown_tot_depo) {
        this.r64_brkdown_tot_depo = r64_brkdown_tot_depo;
    }

    public String getR65_brkdown_typ_of_cust() {
        return r65_brkdown_typ_of_cust;
    }

    public void setR65_brkdown_typ_of_cust(String r65_brkdown_typ_of_cust) {
        this.r65_brkdown_typ_of_cust = r65_brkdown_typ_of_cust;
    }

    public BigDecimal getR65_brkdown_num_of_cust() {
        return r65_brkdown_num_of_cust;
    }

    public void setR65_brkdown_num_of_cust(BigDecimal r65_brkdown_num_of_cust) {
        this.r65_brkdown_num_of_cust = r65_brkdown_num_of_cust;
    }

    public BigDecimal getR65_brkdown_tot_depo() {
        return r65_brkdown_tot_depo;
    }

    public void setR65_brkdown_tot_depo(BigDecimal r65_brkdown_tot_depo) {
        this.r65_brkdown_tot_depo = r65_brkdown_tot_depo;
    }

    public String getR66_brkdown_typ_of_cust() {
        return r66_brkdown_typ_of_cust;
    }

    public void setR66_brkdown_typ_of_cust(String r66_brkdown_typ_of_cust) {
        this.r66_brkdown_typ_of_cust = r66_brkdown_typ_of_cust;
    }

    public BigDecimal getR66_brkdown_num_of_cust() {
        return r66_brkdown_num_of_cust;
    }

    public void setR66_brkdown_num_of_cust(BigDecimal r66_brkdown_num_of_cust) {
        this.r66_brkdown_num_of_cust = r66_brkdown_num_of_cust;
    }

    public BigDecimal getR66_brkdown_tot_depo() {
        return r66_brkdown_tot_depo;
    }

    public void setR66_brkdown_tot_depo(BigDecimal r66_brkdown_tot_depo) {
        this.r66_brkdown_tot_depo = r66_brkdown_tot_depo;
    }

    public String getR67_brkdown_typ_of_cust() {
        return r67_brkdown_typ_of_cust;
    }

    public void setR67_brkdown_typ_of_cust(String r67_brkdown_typ_of_cust) {
        this.r67_brkdown_typ_of_cust = r67_brkdown_typ_of_cust;
    }

    public BigDecimal getR67_brkdown_num_of_cust() {
        return r67_brkdown_num_of_cust;
    }

    public void setR67_brkdown_num_of_cust(BigDecimal r67_brkdown_num_of_cust) {
        this.r67_brkdown_num_of_cust = r67_brkdown_num_of_cust;
    }

    public BigDecimal getR67_brkdown_tot_depo() {
        return r67_brkdown_tot_depo;
    }

    public void setR67_brkdown_tot_depo(BigDecimal r67_brkdown_tot_depo) {
        this.r67_brkdown_tot_depo = r67_brkdown_tot_depo;
    }

    public String getR68_brkdown_typ_of_cust() {
        return r68_brkdown_typ_of_cust;
    }

    public void setR68_brkdown_typ_of_cust(String r68_brkdown_typ_of_cust) {
        this.r68_brkdown_typ_of_cust = r68_brkdown_typ_of_cust;
    }

    public BigDecimal getR68_brkdown_num_of_cust() {
        return r68_brkdown_num_of_cust;
    }

    public void setR68_brkdown_num_of_cust(BigDecimal r68_brkdown_num_of_cust) {
        this.r68_brkdown_num_of_cust = r68_brkdown_num_of_cust;
    }

    public BigDecimal getR68_brkdown_tot_depo() {
        return r68_brkdown_tot_depo;
    }

    public void setR68_brkdown_tot_depo(BigDecimal r68_brkdown_tot_depo) {
        this.r68_brkdown_tot_depo = r68_brkdown_tot_depo;
    }

    public String getR69_brkdown_typ_of_cust() {
        return r69_brkdown_typ_of_cust;
    }

    public void setR69_brkdown_typ_of_cust(String r69_brkdown_typ_of_cust) {
        this.r69_brkdown_typ_of_cust = r69_brkdown_typ_of_cust;
    }

    public BigDecimal getR69_brkdown_num_of_cust() {
        return r69_brkdown_num_of_cust;
    }

    public void setR69_brkdown_num_of_cust(BigDecimal r69_brkdown_num_of_cust) {
        this.r69_brkdown_num_of_cust = r69_brkdown_num_of_cust;
    }

    public BigDecimal getR69_brkdown_tot_depo() {
        return r69_brkdown_tot_depo;
    }

    public void setR69_brkdown_tot_depo(BigDecimal r69_brkdown_tot_depo) {
        this.r69_brkdown_tot_depo = r69_brkdown_tot_depo;
    }

    public String getR70_brkdown_typ_of_cust() {
        return r70_brkdown_typ_of_cust;
    }

    public void setR70_brkdown_typ_of_cust(String r70_brkdown_typ_of_cust) {
        this.r70_brkdown_typ_of_cust = r70_brkdown_typ_of_cust;
    }

    public BigDecimal getR70_brkdown_num_of_cust() {
        return r70_brkdown_num_of_cust;
    }

    public void setR70_brkdown_num_of_cust(BigDecimal r70_brkdown_num_of_cust) {
        this.r70_brkdown_num_of_cust = r70_brkdown_num_of_cust;
    }

    public BigDecimal getR70_brkdown_tot_depo() {
        return r70_brkdown_tot_depo;
    }

    public void setR70_brkdown_tot_depo(BigDecimal r70_brkdown_tot_depo) {
        this.r70_brkdown_tot_depo = r70_brkdown_tot_depo;
    }

    public String getR71_brkdown_typ_of_cust() {
        return r71_brkdown_typ_of_cust;
    }

    public void setR71_brkdown_typ_of_cust(String r71_brkdown_typ_of_cust) {
        this.r71_brkdown_typ_of_cust = r71_brkdown_typ_of_cust;
    }

    public BigDecimal getR71_brkdown_num_of_cust() {
        return r71_brkdown_num_of_cust;
    }

    public void setR71_brkdown_num_of_cust(BigDecimal r71_brkdown_num_of_cust) {
        this.r71_brkdown_num_of_cust = r71_brkdown_num_of_cust;
    }

    public BigDecimal getR71_brkdown_tot_depo() {
        return r71_brkdown_tot_depo;
    }

    public void setR71_brkdown_tot_depo(BigDecimal r71_brkdown_tot_depo) {
        this.r71_brkdown_tot_depo = r71_brkdown_tot_depo;
    }

    public String getR72_brkdown_typ_of_cust() {
        return r72_brkdown_typ_of_cust;
    }

    public void setR72_brkdown_typ_of_cust(String r72_brkdown_typ_of_cust) {
        this.r72_brkdown_typ_of_cust = r72_brkdown_typ_of_cust;
    }

    public BigDecimal getR72_brkdown_num_of_cust() {
        return r72_brkdown_num_of_cust;
    }

    public void setR72_brkdown_num_of_cust(BigDecimal r72_brkdown_num_of_cust) {
        this.r72_brkdown_num_of_cust = r72_brkdown_num_of_cust;
    }

    public BigDecimal getR72_brkdown_tot_depo() {
        return r72_brkdown_tot_depo;
    }

    public void setR72_brkdown_tot_depo(BigDecimal r72_brkdown_tot_depo) {
        this.r72_brkdown_tot_depo = r72_brkdown_tot_depo;
    }

    public String getR73_brkdown_typ_of_cust() {
        return r73_brkdown_typ_of_cust;
    }

    public void setR73_brkdown_typ_of_cust(String r73_brkdown_typ_of_cust) {
        this.r73_brkdown_typ_of_cust = r73_brkdown_typ_of_cust;
    }

    public BigDecimal getR73_brkdown_num_of_cust() {
        return r73_brkdown_num_of_cust;
    }

    public void setR73_brkdown_num_of_cust(BigDecimal r73_brkdown_num_of_cust) {
        this.r73_brkdown_num_of_cust = r73_brkdown_num_of_cust;
    }

    public BigDecimal getR73_brkdown_tot_depo() {
        return r73_brkdown_tot_depo;
    }

    public void setR73_brkdown_tot_depo(BigDecimal r73_brkdown_tot_depo) {
        this.r73_brkdown_tot_depo = r73_brkdown_tot_depo;
    }

    public String getR74_brkdown_typ_of_cust() {
        return r74_brkdown_typ_of_cust;
    }

    public void setR74_brkdown_typ_of_cust(String r74_brkdown_typ_of_cust) {
        this.r74_brkdown_typ_of_cust = r74_brkdown_typ_of_cust;
    }

    public BigDecimal getR74_brkdown_num_of_cust() {
        return r74_brkdown_num_of_cust;
    }

    public void setR74_brkdown_num_of_cust(BigDecimal r74_brkdown_num_of_cust) {
        this.r74_brkdown_num_of_cust = r74_brkdown_num_of_cust;
    }

    public BigDecimal getR74_brkdown_tot_depo() {
        return r74_brkdown_tot_depo;
    }

    public void setR74_brkdown_tot_depo(BigDecimal r74_brkdown_tot_depo) {
        this.r74_brkdown_tot_depo = r74_brkdown_tot_depo;
    }

    public String getR75_brkdown_typ_of_cust() {
        return r75_brkdown_typ_of_cust;
    }

    public void setR75_brkdown_typ_of_cust(String r75_brkdown_typ_of_cust) {
        this.r75_brkdown_typ_of_cust = r75_brkdown_typ_of_cust;
    }

    public BigDecimal getR75_brkdown_num_of_cust() {
        return r75_brkdown_num_of_cust;
    }

    public void setR75_brkdown_num_of_cust(BigDecimal r75_brkdown_num_of_cust) {
        this.r75_brkdown_num_of_cust = r75_brkdown_num_of_cust;
    }

    public BigDecimal getR75_brkdown_tot_depo() {
        return r75_brkdown_tot_depo;
    }

    public void setR75_brkdown_tot_depo(BigDecimal r75_brkdown_tot_depo) {
        this.r75_brkdown_tot_depo = r75_brkdown_tot_depo;
    }

    public BigDecimal getR82_e1_tot_no_cust() {
        return r82_e1_tot_no_cust;
    }

    public void setR82_e1_tot_no_cust(BigDecimal r82_e1_tot_no_cust) {
        this.r82_e1_tot_no_cust = r82_e1_tot_no_cust;
    }

    public BigDecimal getR82_e1_loan_on_bal_expo() {
        return r82_e1_loan_on_bal_expo;
    }

    public void setR82_e1_loan_on_bal_expo(BigDecimal r82_e1_loan_on_bal_expo) {
        this.r82_e1_loan_on_bal_expo = r82_e1_loan_on_bal_expo;
    }

    public BigDecimal getR82_e1_deposit() {
        return r82_e1_deposit;
    }

    public void setR82_e1_deposit(BigDecimal r82_e1_deposit) {
        this.r82_e1_deposit = r82_e1_deposit;
    }

    public BigDecimal getR82_e1_funds_behalf_cust() {
        return r82_e1_funds_behalf_cust;
    }

    public void setR82_e1_funds_behalf_cust(BigDecimal r82_e1_funds_behalf_cust) {
        this.r82_e1_funds_behalf_cust = r82_e1_funds_behalf_cust;
    }

    public BigDecimal getR82_e1_turnover() {
        return r82_e1_turnover;
    }

    public void setR82_e1_turnover(BigDecimal r82_e1_turnover) {
        this.r82_e1_turnover = r82_e1_turnover;
    }

    public BigDecimal getR83_e1_tot_no_cust() {
        return r83_e1_tot_no_cust;
    }

    public void setR83_e1_tot_no_cust(BigDecimal r83_e1_tot_no_cust) {
        this.r83_e1_tot_no_cust = r83_e1_tot_no_cust;
    }

    public BigDecimal getR83_e1_loan_on_bal_expo() {
        return r83_e1_loan_on_bal_expo;
    }

    public void setR83_e1_loan_on_bal_expo(BigDecimal r83_e1_loan_on_bal_expo) {
        this.r83_e1_loan_on_bal_expo = r83_e1_loan_on_bal_expo;
    }

    public BigDecimal getR83_e1_deposit() {
        return r83_e1_deposit;
    }

    public void setR83_e1_deposit(BigDecimal r83_e1_deposit) {
        this.r83_e1_deposit = r83_e1_deposit;
    }

    public BigDecimal getR83_e1_funds_behalf_cust() {
        return r83_e1_funds_behalf_cust;
    }

    public void setR83_e1_funds_behalf_cust(BigDecimal r83_e1_funds_behalf_cust) {
        this.r83_e1_funds_behalf_cust = r83_e1_funds_behalf_cust;
    }

    public BigDecimal getR83_e1_turnover() {
        return r83_e1_turnover;
    }

    public void setR83_e1_turnover(BigDecimal r83_e1_turnover) {
        this.r83_e1_turnover = r83_e1_turnover;
    }

    public BigDecimal getR89_e2_tot_no_cust() {
        return r89_e2_tot_no_cust;
    }

    public void setR89_e2_tot_no_cust(BigDecimal r89_e2_tot_no_cust) {
        this.r89_e2_tot_no_cust = r89_e2_tot_no_cust;
    }

    public BigDecimal getR89_e2_loans_bal_expo() {
        return r89_e2_loans_bal_expo;
    }

    public void setR89_e2_loans_bal_expo(BigDecimal r89_e2_loans_bal_expo) {
        this.r89_e2_loans_bal_expo = r89_e2_loans_bal_expo;
    }

    public BigDecimal getR89_e2_deposit() {
        return r89_e2_deposit;
    }

    public void setR89_e2_deposit(BigDecimal r89_e2_deposit) {
        this.r89_e2_deposit = r89_e2_deposit;
    }

    public BigDecimal getR89_e2_funds_behalf_cust() {
        return r89_e2_funds_behalf_cust;
    }

    public void setR89_e2_funds_behalf_cust(BigDecimal r89_e2_funds_behalf_cust) {
        this.r89_e2_funds_behalf_cust = r89_e2_funds_behalf_cust;
    }

    public BigDecimal getR89_e2_turnover() {
        return r89_e2_turnover;
    }

    public void setR89_e2_turnover(BigDecimal r89_e2_turnover) {
        this.r89_e2_turnover = r89_e2_turnover;
    }

    public BigDecimal getR90_e2_tot_no_cust() {
        return r90_e2_tot_no_cust;
    }

    public void setR90_e2_tot_no_cust(BigDecimal r90_e2_tot_no_cust) {
        this.r90_e2_tot_no_cust = r90_e2_tot_no_cust;
    }

    public BigDecimal getR90_e2_loans_bal_expo() {
        return r90_e2_loans_bal_expo;
    }

    public void setR90_e2_loans_bal_expo(BigDecimal r90_e2_loans_bal_expo) {
        this.r90_e2_loans_bal_expo = r90_e2_loans_bal_expo;
    }

    public BigDecimal getR90_e2_deposit() {
        return r90_e2_deposit;
    }

    public void setR90_e2_deposit(BigDecimal r90_e2_deposit) {
        this.r90_e2_deposit = r90_e2_deposit;
    }

    public BigDecimal getR90_e2_funds_behalf_cust() {
        return r90_e2_funds_behalf_cust;
    }

    public void setR90_e2_funds_behalf_cust(BigDecimal r90_e2_funds_behalf_cust) {
        this.r90_e2_funds_behalf_cust = r90_e2_funds_behalf_cust;
    }

    public BigDecimal getR90_e2_turnover() {
        return r90_e2_turnover;
    }

    public void setR90_e2_turnover(BigDecimal r90_e2_turnover) {
        this.r90_e2_turnover = r90_e2_turnover;
    }

    public BigDecimal getR96_e3_tot_no_cust() {
        return r96_e3_tot_no_cust;
    }

    public void setR96_e3_tot_no_cust(BigDecimal r96_e3_tot_no_cust) {
        this.r96_e3_tot_no_cust = r96_e3_tot_no_cust;
    }

    public BigDecimal getR96_e3_loans_bal_expo() {
        return r96_e3_loans_bal_expo;
    }

    public void setR96_e3_loans_bal_expo(BigDecimal r96_e3_loans_bal_expo) {
        this.r96_e3_loans_bal_expo = r96_e3_loans_bal_expo;
    }

    public BigDecimal getR96_e3_deposit() {
        return r96_e3_deposit;
    }

    public void setR96_e3_deposit(BigDecimal r96_e3_deposit) {
        this.r96_e3_deposit = r96_e3_deposit;
    }

    public BigDecimal getR96_e3_funds_behalf_cust() {
        return r96_e3_funds_behalf_cust;
    }

    public void setR96_e3_funds_behalf_cust(BigDecimal r96_e3_funds_behalf_cust) {
        this.r96_e3_funds_behalf_cust = r96_e3_funds_behalf_cust;
    }

    public BigDecimal getR96_e3_turnover() {
        return r96_e3_turnover;
    }

    public void setR96_e3_turnover(BigDecimal r96_e3_turnover) {
        this.r96_e3_turnover = r96_e3_turnover;
    }

    public BigDecimal getR97_e3_tot_no_cust() {
        return r97_e3_tot_no_cust;
    }

    public void setR97_e3_tot_no_cust(BigDecimal r97_e3_tot_no_cust) {
        this.r97_e3_tot_no_cust = r97_e3_tot_no_cust;
    }

    public BigDecimal getR97_e3_loans_bal_expo() {
        return r97_e3_loans_bal_expo;
    }

    public void setR97_e3_loans_bal_expo(BigDecimal r97_e3_loans_bal_expo) {
        this.r97_e3_loans_bal_expo = r97_e3_loans_bal_expo;
    }

    public BigDecimal getR97_e3_deposit() {
        return r97_e3_deposit;
    }

    public void setR97_e3_deposit(BigDecimal r97_e3_deposit) {
        this.r97_e3_deposit = r97_e3_deposit;
    }

    public BigDecimal getR97_e3_funds_behalf_cust() {
        return r97_e3_funds_behalf_cust;
    }

    public void setR97_e3_funds_behalf_cust(BigDecimal r97_e3_funds_behalf_cust) {
        this.r97_e3_funds_behalf_cust = r97_e3_funds_behalf_cust;
    }

    public BigDecimal getR97_e3_turnover() {
        return r97_e3_turnover;
    }

    public void setR97_e3_turnover(BigDecimal r97_e3_turnover) {
        this.r97_e3_turnover = r97_e3_turnover;
    }

    public BigDecimal getR104_f_num_of_cust() {
        return r104_f_num_of_cust;
    }

    public void setR104_f_num_of_cust(BigDecimal r104_f_num_of_cust) {
        this.r104_f_num_of_cust = r104_f_num_of_cust;
    }

    public BigDecimal getR104_f_loans_bal_expo() {
        return r104_f_loans_bal_expo;
    }

    public void setR104_f_loans_bal_expo(BigDecimal r104_f_loans_bal_expo) {
        this.r104_f_loans_bal_expo = r104_f_loans_bal_expo;
    }

    public BigDecimal getR104_f_deposit() {
        return r104_f_deposit;
    }

    public void setR104_f_deposit(BigDecimal r104_f_deposit) {
        this.r104_f_deposit = r104_f_deposit;
    }

    public BigDecimal getR104_f_funds_behalf_cust() {
        return r104_f_funds_behalf_cust;
    }

    public void setR104_f_funds_behalf_cust(BigDecimal r104_f_funds_behalf_cust) {
        this.r104_f_funds_behalf_cust = r104_f_funds_behalf_cust;
    }

    public BigDecimal getR104_f_turnover() {
        return r104_f_turnover;
    }

    public void setR104_f_turnover(BigDecimal r104_f_turnover) {
        this.r104_f_turnover = r104_f_turnover;
    }

    public BigDecimal getR105_f_num_of_cust() {
        return r105_f_num_of_cust;
    }

    public void setR105_f_num_of_cust(BigDecimal r105_f_num_of_cust) {
        this.r105_f_num_of_cust = r105_f_num_of_cust;
    }

    public BigDecimal getR105_f_loans_bal_expo() {
        return r105_f_loans_bal_expo;
    }

    public void setR105_f_loans_bal_expo(BigDecimal r105_f_loans_bal_expo) {
        this.r105_f_loans_bal_expo = r105_f_loans_bal_expo;
    }

    public BigDecimal getR105_f_deposit() {
        return r105_f_deposit;
    }

    public void setR105_f_deposit(BigDecimal r105_f_deposit) {
        this.r105_f_deposit = r105_f_deposit;
    }

    public BigDecimal getR105_f_funds_behalf_cust() {
        return r105_f_funds_behalf_cust;
    }

    public void setR105_f_funds_behalf_cust(BigDecimal r105_f_funds_behalf_cust) {
        this.r105_f_funds_behalf_cust = r105_f_funds_behalf_cust;
    }

    public BigDecimal getR105_f_turnover() {
        return r105_f_turnover;
    }

    public void setR105_f_turnover(BigDecimal r105_f_turnover) {
        this.r105_f_turnover = r105_f_turnover;
    }

    public String getR111_g1_pay_mech() {
        return r111_g1_pay_mech;
    }

    public void setR111_g1_pay_mech(String r111_g1_pay_mech) {
        this.r111_g1_pay_mech = r111_g1_pay_mech;
    }

    public String getR111_g1_pay_mechanisum() {
        return r111_g1_pay_mechanisum;
    }

    public void setR111_g1_pay_mechanisum(String r111_g1_pay_mechanisum) {
        this.r111_g1_pay_mechanisum = r111_g1_pay_mechanisum;
    }

    public BigDecimal getR111_g1_num_trans() {
        return r111_g1_num_trans;
    }

    public void setR111_g1_num_trans(BigDecimal r111_g1_num_trans) {
        this.r111_g1_num_trans = r111_g1_num_trans;
    }

    public BigDecimal getR111_g1_val_trans() {
        return r111_g1_val_trans;
    }

    public void setR111_g1_val_trans(BigDecimal r111_g1_val_trans) {
        this.r111_g1_val_trans = r111_g1_val_trans;
    }

    public String getR112_g1_pay_mech() {
        return r112_g1_pay_mech;
    }

    public void setR112_g1_pay_mech(String r112_g1_pay_mech) {
        this.r112_g1_pay_mech = r112_g1_pay_mech;
    }

    public String getR112_g1_pay_mechanisum() {
        return r112_g1_pay_mechanisum;
    }

    public void setR112_g1_pay_mechanisum(String r112_g1_pay_mechanisum) {
        this.r112_g1_pay_mechanisum = r112_g1_pay_mechanisum;
    }

    public BigDecimal getR112_g1_num_trans() {
        return r112_g1_num_trans;
    }

    public void setR112_g1_num_trans(BigDecimal r112_g1_num_trans) {
        this.r112_g1_num_trans = r112_g1_num_trans;
    }

    public BigDecimal getR112_g1_val_trans() {
        return r112_g1_val_trans;
    }

    public void setR112_g1_val_trans(BigDecimal r112_g1_val_trans) {
        this.r112_g1_val_trans = r112_g1_val_trans;
    }

    public String getR113_g1_pay_mech() {
        return r113_g1_pay_mech;
    }

    public void setR113_g1_pay_mech(String r113_g1_pay_mech) {
        this.r113_g1_pay_mech = r113_g1_pay_mech;
    }

    public String getR113_g1_pay_mechanisum() {
        return r113_g1_pay_mechanisum;
    }

    public void setR113_g1_pay_mechanisum(String r113_g1_pay_mechanisum) {
        this.r113_g1_pay_mechanisum = r113_g1_pay_mechanisum;
    }

    public BigDecimal getR113_g1_num_trans() {
        return r113_g1_num_trans;
    }

    public void setR113_g1_num_trans(BigDecimal r113_g1_num_trans) {
        this.r113_g1_num_trans = r113_g1_num_trans;
    }

    public BigDecimal getR113_g1_val_trans() {
        return r113_g1_val_trans;
    }

    public void setR113_g1_val_trans(BigDecimal r113_g1_val_trans) {
        this.r113_g1_val_trans = r113_g1_val_trans;
    }

    public String getR114_g1_pay_mech() {
        return r114_g1_pay_mech;
    }

    public void setR114_g1_pay_mech(String r114_g1_pay_mech) {
        this.r114_g1_pay_mech = r114_g1_pay_mech;
    }

    public String getR114_g1_pay_mechanisum() {
        return r114_g1_pay_mechanisum;
    }

    public void setR114_g1_pay_mechanisum(String r114_g1_pay_mechanisum) {
        this.r114_g1_pay_mechanisum = r114_g1_pay_mechanisum;
    }

    public BigDecimal getR114_g1_num_trans() {
        return r114_g1_num_trans;
    }

    public void setR114_g1_num_trans(BigDecimal r114_g1_num_trans) {
        this.r114_g1_num_trans = r114_g1_num_trans;
    }

    public BigDecimal getR114_g1_val_trans() {
        return r114_g1_val_trans;
    }

    public void setR114_g1_val_trans(BigDecimal r114_g1_val_trans) {
        this.r114_g1_val_trans = r114_g1_val_trans;
    }

    public String getR115_g1_pay_mech() {
        return r115_g1_pay_mech;
    }

    public void setR115_g1_pay_mech(String r115_g1_pay_mech) {
        this.r115_g1_pay_mech = r115_g1_pay_mech;
    }

    public String getR115_g1_pay_mechanisum() {
        return r115_g1_pay_mechanisum;
    }

    public void setR115_g1_pay_mechanisum(String r115_g1_pay_mechanisum) {
        this.r115_g1_pay_mechanisum = r115_g1_pay_mechanisum;
    }

    public BigDecimal getR115_g1_num_trans() {
        return r115_g1_num_trans;
    }

    public void setR115_g1_num_trans(BigDecimal r115_g1_num_trans) {
        this.r115_g1_num_trans = r115_g1_num_trans;
    }

    public BigDecimal getR115_g1_val_trans() {
        return r115_g1_val_trans;
    }

    public void setR115_g1_val_trans(BigDecimal r115_g1_val_trans) {
        this.r115_g1_val_trans = r115_g1_val_trans;
    }

    public String getR116_g1_pay_mech() {
        return r116_g1_pay_mech;
    }

    public void setR116_g1_pay_mech(String r116_g1_pay_mech) {
        this.r116_g1_pay_mech = r116_g1_pay_mech;
    }

    public String getR116_g1_pay_mechanisum() {
        return r116_g1_pay_mechanisum;
    }

    public void setR116_g1_pay_mechanisum(String r116_g1_pay_mechanisum) {
        this.r116_g1_pay_mechanisum = r116_g1_pay_mechanisum;
    }

    public BigDecimal getR116_g1_num_trans() {
        return r116_g1_num_trans;
    }

    public void setR116_g1_num_trans(BigDecimal r116_g1_num_trans) {
        this.r116_g1_num_trans = r116_g1_num_trans;
    }

    public BigDecimal getR116_g1_val_trans() {
        return r116_g1_val_trans;
    }

    public void setR116_g1_val_trans(BigDecimal r116_g1_val_trans) {
        this.r116_g1_val_trans = r116_g1_val_trans;
    }

    public String getR117_g1_pay_mech() {
        return r117_g1_pay_mech;
    }

    public void setR117_g1_pay_mech(String r117_g1_pay_mech) {
        this.r117_g1_pay_mech = r117_g1_pay_mech;
    }

    public String getR117_g1_pay_mechanisum() {
        return r117_g1_pay_mechanisum;
    }

    public void setR117_g1_pay_mechanisum(String r117_g1_pay_mechanisum) {
        this.r117_g1_pay_mechanisum = r117_g1_pay_mechanisum;
    }

    public BigDecimal getR117_g1_num_trans() {
        return r117_g1_num_trans;
    }

    public void setR117_g1_num_trans(BigDecimal r117_g1_num_trans) {
        this.r117_g1_num_trans = r117_g1_num_trans;
    }

    public BigDecimal getR117_g1_val_trans() {
        return r117_g1_val_trans;
    }

    public void setR117_g1_val_trans(BigDecimal r117_g1_val_trans) {
        this.r117_g1_val_trans = r117_g1_val_trans;
    }

    public String getR118_g1_pay_mech() {
        return r118_g1_pay_mech;
    }

    public void setR118_g1_pay_mech(String r118_g1_pay_mech) {
        this.r118_g1_pay_mech = r118_g1_pay_mech;
    }

    public String getR118_g1_pay_mechanisum() {
        return r118_g1_pay_mechanisum;
    }

    public void setR118_g1_pay_mechanisum(String r118_g1_pay_mechanisum) {
        this.r118_g1_pay_mechanisum = r118_g1_pay_mechanisum;
    }

    public BigDecimal getR118_g1_num_trans() {
        return r118_g1_num_trans;
    }

    public void setR118_g1_num_trans(BigDecimal r118_g1_num_trans) {
        this.r118_g1_num_trans = r118_g1_num_trans;
    }

    public BigDecimal getR118_g1_val_trans() {
        return r118_g1_val_trans;
    }

    public void setR118_g1_val_trans(BigDecimal r118_g1_val_trans) {
        this.r118_g1_val_trans = r118_g1_val_trans;
    }

    public String getR119_g1_pay_mech() {
        return r119_g1_pay_mech;
    }

    public void setR119_g1_pay_mech(String r119_g1_pay_mech) {
        this.r119_g1_pay_mech = r119_g1_pay_mech;
    }

    public String getR119_g1_pay_mechanisum() {
        return r119_g1_pay_mechanisum;
    }

    public void setR119_g1_pay_mechanisum(String r119_g1_pay_mechanisum) {
        this.r119_g1_pay_mechanisum = r119_g1_pay_mechanisum;
    }

    public BigDecimal getR119_g1_num_trans() {
        return r119_g1_num_trans;
    }

    public void setR119_g1_num_trans(BigDecimal r119_g1_num_trans) {
        this.r119_g1_num_trans = r119_g1_num_trans;
    }

    public BigDecimal getR119_g1_val_trans() {
        return r119_g1_val_trans;
    }

    public void setR119_g1_val_trans(BigDecimal r119_g1_val_trans) {
        this.r119_g1_val_trans = r119_g1_val_trans;
    }

    public String getR120_g1_pay_mech() {
        return r120_g1_pay_mech;
    }

    public void setR120_g1_pay_mech(String r120_g1_pay_mech) {
        this.r120_g1_pay_mech = r120_g1_pay_mech;
    }

    public String getR120_g1_pay_mechanisum() {
        return r120_g1_pay_mechanisum;
    }

    public void setR120_g1_pay_mechanisum(String r120_g1_pay_mechanisum) {
        this.r120_g1_pay_mechanisum = r120_g1_pay_mechanisum;
    }

    public BigDecimal getR120_g1_num_trans() {
        return r120_g1_num_trans;
    }

    public void setR120_g1_num_trans(BigDecimal r120_g1_num_trans) {
        this.r120_g1_num_trans = r120_g1_num_trans;
    }

    public BigDecimal getR120_g1_val_trans() {
        return r120_g1_val_trans;
    }

    public void setR120_g1_val_trans(BigDecimal r120_g1_val_trans) {
        this.r120_g1_val_trans = r120_g1_val_trans;
    }

    public String getR121_g1_pay_mech() {
        return r121_g1_pay_mech;
    }

    public void setR121_g1_pay_mech(String r121_g1_pay_mech) {
        this.r121_g1_pay_mech = r121_g1_pay_mech;
    }

    public String getR121_g1_pay_mechanisum() {
        return r121_g1_pay_mechanisum;
    }

    public void setR121_g1_pay_mechanisum(String r121_g1_pay_mechanisum) {
        this.r121_g1_pay_mechanisum = r121_g1_pay_mechanisum;
    }

    public BigDecimal getR121_g1_num_trans() {
        return r121_g1_num_trans;
    }

    public void setR121_g1_num_trans(BigDecimal r121_g1_num_trans) {
        this.r121_g1_num_trans = r121_g1_num_trans;
    }

    public BigDecimal getR121_g1_val_trans() {
        return r121_g1_val_trans;
    }

    public void setR121_g1_val_trans(BigDecimal r121_g1_val_trans) {
        this.r121_g1_val_trans = r121_g1_val_trans;
    }

    public String getR122_g1_pay_mech() {
        return r122_g1_pay_mech;
    }

    public void setR122_g1_pay_mech(String r122_g1_pay_mech) {
        this.r122_g1_pay_mech = r122_g1_pay_mech;
    }

    public String getR122_g1_pay_mechanisum() {
        return r122_g1_pay_mechanisum;
    }

    public void setR122_g1_pay_mechanisum(String r122_g1_pay_mechanisum) {
        this.r122_g1_pay_mechanisum = r122_g1_pay_mechanisum;
    }

    public BigDecimal getR122_g1_num_trans() {
        return r122_g1_num_trans;
    }

    public void setR122_g1_num_trans(BigDecimal r122_g1_num_trans) {
        this.r122_g1_num_trans = r122_g1_num_trans;
    }

    public BigDecimal getR122_g1_val_trans() {
        return r122_g1_val_trans;
    }

    public void setR122_g1_val_trans(BigDecimal r122_g1_val_trans) {
        this.r122_g1_val_trans = r122_g1_val_trans;
    }

    public String getR123_g1_pay_mech() {
        return r123_g1_pay_mech;
    }

    public void setR123_g1_pay_mech(String r123_g1_pay_mech) {
        this.r123_g1_pay_mech = r123_g1_pay_mech;
    }

    public String getR123_g1_pay_mechanisum() {
        return r123_g1_pay_mechanisum;
    }

    public void setR123_g1_pay_mechanisum(String r123_g1_pay_mechanisum) {
        this.r123_g1_pay_mechanisum = r123_g1_pay_mechanisum;
    }

    public BigDecimal getR123_g1_num_trans() {
        return r123_g1_num_trans;
    }

    public void setR123_g1_num_trans(BigDecimal r123_g1_num_trans) {
        this.r123_g1_num_trans = r123_g1_num_trans;
    }

    public BigDecimal getR123_g1_val_trans() {
        return r123_g1_val_trans;
    }

    public void setR123_g1_val_trans(BigDecimal r123_g1_val_trans) {
        this.r123_g1_val_trans = r123_g1_val_trans;
    }

    public String getR124_g1_pay_mech() {
        return r124_g1_pay_mech;
    }

    public void setR124_g1_pay_mech(String r124_g1_pay_mech) {
        this.r124_g1_pay_mech = r124_g1_pay_mech;
    }

    public String getR124_g1_pay_mechanisum() {
        return r124_g1_pay_mechanisum;
    }

    public void setR124_g1_pay_mechanisum(String r124_g1_pay_mechanisum) {
        this.r124_g1_pay_mechanisum = r124_g1_pay_mechanisum;
    }

    public BigDecimal getR124_g1_num_trans() {
        return r124_g1_num_trans;
    }

    public void setR124_g1_num_trans(BigDecimal r124_g1_num_trans) {
        this.r124_g1_num_trans = r124_g1_num_trans;
    }

    public BigDecimal getR124_g1_val_trans() {
        return r124_g1_val_trans;
    }

    public void setR124_g1_val_trans(BigDecimal r124_g1_val_trans) {
        this.r124_g1_val_trans = r124_g1_val_trans;
    }

    public String getR125_g1_pay_mech() {
        return r125_g1_pay_mech;
    }

    public void setR125_g1_pay_mech(String r125_g1_pay_mech) {
        this.r125_g1_pay_mech = r125_g1_pay_mech;
    }

    public String getR125_g1_pay_mechanisum() {
        return r125_g1_pay_mechanisum;
    }

    public void setR125_g1_pay_mechanisum(String r125_g1_pay_mechanisum) {
        this.r125_g1_pay_mechanisum = r125_g1_pay_mechanisum;
    }

    public BigDecimal getR125_g1_num_trans() {
        return r125_g1_num_trans;
    }

    public void setR125_g1_num_trans(BigDecimal r125_g1_num_trans) {
        this.r125_g1_num_trans = r125_g1_num_trans;
    }

    public BigDecimal getR125_g1_val_trans() {
        return r125_g1_val_trans;
    }

    public void setR125_g1_val_trans(BigDecimal r125_g1_val_trans) {
        this.r125_g1_val_trans = r125_g1_val_trans;
    }

    public String getR126_g1_pay_mech() {
        return r126_g1_pay_mech;
    }

    public void setR126_g1_pay_mech(String r126_g1_pay_mech) {
        this.r126_g1_pay_mech = r126_g1_pay_mech;
    }

    public String getR126_g1_pay_mechanisum() {
        return r126_g1_pay_mechanisum;
    }

    public void setR126_g1_pay_mechanisum(String r126_g1_pay_mechanisum) {
        this.r126_g1_pay_mechanisum = r126_g1_pay_mechanisum;
    }

    public BigDecimal getR126_g1_num_trans() {
        return r126_g1_num_trans;
    }

    public void setR126_g1_num_trans(BigDecimal r126_g1_num_trans) {
        this.r126_g1_num_trans = r126_g1_num_trans;
    }

    public BigDecimal getR126_g1_val_trans() {
        return r126_g1_val_trans;
    }

    public void setR126_g1_val_trans(BigDecimal r126_g1_val_trans) {
        this.r126_g1_val_trans = r126_g1_val_trans;
    }

    public String getR127_g1_pay_mech() {
        return r127_g1_pay_mech;
    }

    public void setR127_g1_pay_mech(String r127_g1_pay_mech) {
        this.r127_g1_pay_mech = r127_g1_pay_mech;
    }

    public String getR127_g1_pay_mechanisum() {
        return r127_g1_pay_mechanisum;
    }

    public void setR127_g1_pay_mechanisum(String r127_g1_pay_mechanisum) {
        this.r127_g1_pay_mechanisum = r127_g1_pay_mechanisum;
    }

    public BigDecimal getR127_g1_num_trans() {
        return r127_g1_num_trans;
    }

    public void setR127_g1_num_trans(BigDecimal r127_g1_num_trans) {
        this.r127_g1_num_trans = r127_g1_num_trans;
    }

    public BigDecimal getR127_g1_val_trans() {
        return r127_g1_val_trans;
    }

    public void setR127_g1_val_trans(BigDecimal r127_g1_val_trans) {
        this.r127_g1_val_trans = r127_g1_val_trans;
    }

    public String getR128_g1_pay_mech() {
        return r128_g1_pay_mech;
    }

    public void setR128_g1_pay_mech(String r128_g1_pay_mech) {
        this.r128_g1_pay_mech = r128_g1_pay_mech;
    }

    public String getR128_g1_pay_mechanisum() {
        return r128_g1_pay_mechanisum;
    }

    public void setR128_g1_pay_mechanisum(String r128_g1_pay_mechanisum) {
        this.r128_g1_pay_mechanisum = r128_g1_pay_mechanisum;
    }

    public BigDecimal getR128_g1_num_trans() {
        return r128_g1_num_trans;
    }

    public void setR128_g1_num_trans(BigDecimal r128_g1_num_trans) {
        this.r128_g1_num_trans = r128_g1_num_trans;
    }

    public BigDecimal getR128_g1_val_trans() {
        return r128_g1_val_trans;
    }

    public void setR128_g1_val_trans(BigDecimal r128_g1_val_trans) {
        this.r128_g1_val_trans = r128_g1_val_trans;
    }

    public String getR135_g2_foreign_exchange() {
        return r135_g2_foreign_exchange;
    }

    public void setR135_g2_foreign_exchange(String r135_g2_foreign_exchange) {
        this.r135_g2_foreign_exchange = r135_g2_foreign_exchange;
    }

    public String getR135_g2_fore_exchange() {
        return r135_g2_fore_exchange;
    }

    public void setR135_g2_fore_exchange(String r135_g2_fore_exchange) {
        this.r135_g2_fore_exchange = r135_g2_fore_exchange;
    }

    public BigDecimal getR135_g2_val_transac() {
        return r135_g2_val_transac;
    }

    public void setR135_g2_val_transac(BigDecimal r135_g2_val_transac) {
        this.r135_g2_val_transac = r135_g2_val_transac;
    }

    public String getR136_g2_fore_exchange() {
        return r136_g2_fore_exchange;
    }

    public void setR136_g2_fore_exchange(String r136_g2_fore_exchange) {
        this.r136_g2_fore_exchange = r136_g2_fore_exchange;
    }

    public BigDecimal getR136_g2_val_transac() {
        return r136_g2_val_transac;
    }

    public void setR136_g2_val_transac(BigDecimal r136_g2_val_transac) {
        this.r136_g2_val_transac = r136_g2_val_transac;
    }

    public String getR138_g2_foreign_exchange() {
        return r138_g2_foreign_exchange;
    }

    public void setR138_g2_foreign_exchange(String r138_g2_foreign_exchange) {
        this.r138_g2_foreign_exchange = r138_g2_foreign_exchange;
    }

    public String getR138_g2_fore_exchange() {
        return r138_g2_fore_exchange;
    }

    public void setR138_g2_fore_exchange(String r138_g2_fore_exchange) {
        this.r138_g2_fore_exchange = r138_g2_fore_exchange;
    }

    public BigDecimal getR138_g2_val_transac() {
        return r138_g2_val_transac;
    }

    public void setR138_g2_val_transac(BigDecimal r138_g2_val_transac) {
        this.r138_g2_val_transac = r138_g2_val_transac;
    }

    public String getR139_g2_fore_exchange() {
        return r139_g2_fore_exchange;
    }

    public void setR139_g2_fore_exchange(String r139_g2_fore_exchange) {
        this.r139_g2_fore_exchange = r139_g2_fore_exchange;
    }

    public BigDecimal getR139_g2_val_transac() {
        return r139_g2_val_transac;
    }

    public void setR139_g2_val_transac(BigDecimal r139_g2_val_transac) {
        this.r139_g2_val_transac = r139_g2_val_transac;
    }

    public String getR144_h_types() {
        return r144_h_types;
    }

    public void setR144_h_types(String r144_h_types) {
        this.r144_h_types = r144_h_types;
    }

    public BigDecimal getR144_h_amount() {
        return r144_h_amount;
    }

    public void setR144_h_amount(BigDecimal r144_h_amount) {
        this.r144_h_amount = r144_h_amount;
    }

    public String getR145_h_types() {
        return r145_h_types;
    }

    public void setR145_h_types(String r145_h_types) {
        this.r145_h_types = r145_h_types;
    }

    public BigDecimal getR145_h_amount() {
        return r145_h_amount;
    }

    public void setR145_h_amount(BigDecimal r145_h_amount) {
        this.r145_h_amount = r145_h_amount;
    }

    public String getR146_h_types() {
        return r146_h_types;
    }

    public void setR146_h_types(String r146_h_types) {
        this.r146_h_types = r146_h_types;
    }

    public BigDecimal getR146_h_amount() {
        return r146_h_amount;
    }

    public void setR146_h_amount(BigDecimal r146_h_amount) {
        this.r146_h_amount = r146_h_amount;
    }

    public String getR147_h_types() {
        return r147_h_types;
    }

    public void setR147_h_types(String r147_h_types) {
        this.r147_h_types = r147_h_types;
    }

    public BigDecimal getR147_h_amount() {
        return r147_h_amount;
    }

    public void setR147_h_amount(BigDecimal r147_h_amount) {
        this.r147_h_amount = r147_h_amount;
    }

    public String getR148_h_types() {
        return r148_h_types;
    }

    public void setR148_h_types(String r148_h_types) {
        this.r148_h_types = r148_h_types;
    }

    public BigDecimal getR148_h_amount() {
        return r148_h_amount;
    }

    public void setR148_h_amount(BigDecimal r148_h_amount) {
        this.r148_h_amount = r148_h_amount;
    }

    public String getR153_i_product_serv() {
        return r153_i_product_serv;
    }

    public void setR153_i_product_serv(String r153_i_product_serv) {
        this.r153_i_product_serv = r153_i_product_serv;
    }

    public BigDecimal getR153_i_no_cust() {
        return r153_i_no_cust;
    }

    public void setR153_i_no_cust(BigDecimal r153_i_no_cust) {
        this.r153_i_no_cust = r153_i_no_cust;
    }

    public BigDecimal getR153_i_outs_bal() {
        return r153_i_outs_bal;
    }

    public void setR153_i_outs_bal(BigDecimal r153_i_outs_bal) {
        this.r153_i_outs_bal = r153_i_outs_bal;
    }

    public BigDecimal getR153_i_turnover() {
        return r153_i_turnover;
    }

    public void setR153_i_turnover(BigDecimal r153_i_turnover) {
        this.r153_i_turnover = r153_i_turnover;
    }

    public String getR154_i_product_serv() {
        return r154_i_product_serv;
    }

    public void setR154_i_product_serv(String r154_i_product_serv) {
        this.r154_i_product_serv = r154_i_product_serv;
    }

    public BigDecimal getR154_i_no_cust() {
        return r154_i_no_cust;
    }

    public void setR154_i_no_cust(BigDecimal r154_i_no_cust) {
        this.r154_i_no_cust = r154_i_no_cust;
    }

    public BigDecimal getR154_i_outs_bal() {
        return r154_i_outs_bal;
    }

    public void setR154_i_outs_bal(BigDecimal r154_i_outs_bal) {
        this.r154_i_outs_bal = r154_i_outs_bal;
    }

    public BigDecimal getR154_i_turnover() {
        return r154_i_turnover;
    }

    public void setR154_i_turnover(BigDecimal r154_i_turnover) {
        this.r154_i_turnover = r154_i_turnover;
    }

    public String getR155_i_product_serv() {
        return r155_i_product_serv;
    }

    public void setR155_i_product_serv(String r155_i_product_serv) {
        this.r155_i_product_serv = r155_i_product_serv;
    }

    public BigDecimal getR155_i_no_cust() {
        return r155_i_no_cust;
    }

    public void setR155_i_no_cust(BigDecimal r155_i_no_cust) {
        this.r155_i_no_cust = r155_i_no_cust;
    }

    public BigDecimal getR155_i_outs_bal() {
        return r155_i_outs_bal;
    }

    public void setR155_i_outs_bal(BigDecimal r155_i_outs_bal) {
        this.r155_i_outs_bal = r155_i_outs_bal;
    }

    public BigDecimal getR155_i_turnover() {
        return r155_i_turnover;
    }

    public void setR155_i_turnover(BigDecimal r155_i_turnover) {
        this.r155_i_turnover = r155_i_turnover;
    }

    public String getR161_j_trade_finc_prod() {
        return r161_j_trade_finc_prod;
    }

    public void setR161_j_trade_finc_prod(String r161_j_trade_finc_prod) {
        this.r161_j_trade_finc_prod = r161_j_trade_finc_prod;
    }

    public BigDecimal getR161_j_num_of_cust() {
        return r161_j_num_of_cust;
    }

    public void setR161_j_num_of_cust(BigDecimal r161_j_num_of_cust) {
        this.r161_j_num_of_cust = r161_j_num_of_cust;
    }

    public BigDecimal getR161_j_commitment_at_jun() {
        return r161_j_commitment_at_jun;
    }

    public void setR161_j_commitment_at_jun(BigDecimal r161_j_commitment_at_jun) {
        this.r161_j_commitment_at_jun = r161_j_commitment_at_jun;
    }

    public String getR162_j_trade_finc_prod() {
        return r162_j_trade_finc_prod;
    }

    public void setR162_j_trade_finc_prod(String r162_j_trade_finc_prod) {
        this.r162_j_trade_finc_prod = r162_j_trade_finc_prod;
    }

    public BigDecimal getR162_j_num_of_cust() {
        return r162_j_num_of_cust;
    }

    public void setR162_j_num_of_cust(BigDecimal r162_j_num_of_cust) {
        this.r162_j_num_of_cust = r162_j_num_of_cust;
    }

    public BigDecimal getR162_j_commitment_at_jun() {
        return r162_j_commitment_at_jun;
    }

    public void setR162_j_commitment_at_jun(BigDecimal r162_j_commitment_at_jun) {
        this.r162_j_commitment_at_jun = r162_j_commitment_at_jun;
    }

    public String getR163_j_trade_finc_prod() {
        return r163_j_trade_finc_prod;
    }

    public void setR163_j_trade_finc_prod(String r163_j_trade_finc_prod) {
        this.r163_j_trade_finc_prod = r163_j_trade_finc_prod;
    }

    public BigDecimal getR163_j_num_of_cust() {
        return r163_j_num_of_cust;
    }

    public void setR163_j_num_of_cust(BigDecimal r163_j_num_of_cust) {
        this.r163_j_num_of_cust = r163_j_num_of_cust;
    }

    public BigDecimal getR163_j_commitment_at_jun() {
        return r163_j_commitment_at_jun;
    }

    public void setR163_j_commitment_at_jun(BigDecimal r163_j_commitment_at_jun) {
        this.r163_j_commitment_at_jun = r163_j_commitment_at_jun;
    }

    public String getR164_j_trade_finc_prod() {
        return r164_j_trade_finc_prod;
    }

    public void setR164_j_trade_finc_prod(String r164_j_trade_finc_prod) {
        this.r164_j_trade_finc_prod = r164_j_trade_finc_prod;
    }

    public BigDecimal getR164_j_num_of_cust() {
        return r164_j_num_of_cust;
    }

    public void setR164_j_num_of_cust(BigDecimal r164_j_num_of_cust) {
        this.r164_j_num_of_cust = r164_j_num_of_cust;
    }

    public BigDecimal getR164_j_commitment_at_jun() {
        return r164_j_commitment_at_jun;
    }

    public void setR164_j_commitment_at_jun(BigDecimal r164_j_commitment_at_jun) {
        this.r164_j_commitment_at_jun = r164_j_commitment_at_jun;
    }

    public String getR170_k_pay_mechanism() {
        return r170_k_pay_mechanism;
    }

    public void setR170_k_pay_mechanism(String r170_k_pay_mechanism) {
        this.r170_k_pay_mechanism = r170_k_pay_mechanism;
    }

    public String getR170_k_pay_mech() {
        return r170_k_pay_mech;
    }

    public void setR170_k_pay_mech(String r170_k_pay_mech) {
        this.r170_k_pay_mech = r170_k_pay_mech;
    }

    public BigDecimal getR170_k_num_of_trans() {
        return r170_k_num_of_trans;
    }

    public void setR170_k_num_of_trans(BigDecimal r170_k_num_of_trans) {
        this.r170_k_num_of_trans = r170_k_num_of_trans;
    }

    public BigDecimal getR170_k_value_of_trans() {
        return r170_k_value_of_trans;
    }

    public void setR170_k_value_of_trans(BigDecimal r170_k_value_of_trans) {
        this.r170_k_value_of_trans = r170_k_value_of_trans;
    }

    public String getR171_k_pay_mech() {
        return r171_k_pay_mech;
    }

    public void setR171_k_pay_mech(String r171_k_pay_mech) {
        this.r171_k_pay_mech = r171_k_pay_mech;
    }

    public BigDecimal getR171_k_num_of_trans() {
        return r171_k_num_of_trans;
    }

    public void setR171_k_num_of_trans(BigDecimal r171_k_num_of_trans) {
        this.r171_k_num_of_trans = r171_k_num_of_trans;
    }

    public BigDecimal getR171_k_value_of_trans() {
        return r171_k_value_of_trans;
    }

    public void setR171_k_value_of_trans(BigDecimal r171_k_value_of_trans) {
        this.r171_k_value_of_trans = r171_k_value_of_trans;
    }

    public String getR172_k_pay_mechanism() {
        return r172_k_pay_mechanism;
    }

    public void setR172_k_pay_mechanism(String r172_k_pay_mechanism) {
        this.r172_k_pay_mechanism = r172_k_pay_mechanism;
    }

    public String getR172_k_pay_mech() {
        return r172_k_pay_mech;
    }

    public void setR172_k_pay_mech(String r172_k_pay_mech) {
        this.r172_k_pay_mech = r172_k_pay_mech;
    }

    public BigDecimal getR172_k_num_of_trans() {
        return r172_k_num_of_trans;
    }

    public void setR172_k_num_of_trans(BigDecimal r172_k_num_of_trans) {
        this.r172_k_num_of_trans = r172_k_num_of_trans;
    }

    public BigDecimal getR172_k_value_of_trans() {
        return r172_k_value_of_trans;
    }

    public void setR172_k_value_of_trans(BigDecimal r172_k_value_of_trans) {
        this.r172_k_value_of_trans = r172_k_value_of_trans;
    }

    public String getR179_l_transac_report() {
        return r179_l_transac_report;
    }

    public void setR179_l_transac_report(String r179_l_transac_report) {
        this.r179_l_transac_report = r179_l_transac_report;
    }

    public BigDecimal getR179_l_num_of_transac() {
        return r179_l_num_of_transac;
    }

    public void setR179_l_num_of_transac(BigDecimal r179_l_num_of_transac) {
        this.r179_l_num_of_transac = r179_l_num_of_transac;
    }

    public String getR180_l_transac_report() {
        return r180_l_transac_report;
    }

    public void setR180_l_transac_report(String r180_l_transac_report) {
        this.r180_l_transac_report = r180_l_transac_report;
    }

    public BigDecimal getR180_l_num_of_transac() {
        return r180_l_num_of_transac;
    }

    public void setR180_l_num_of_transac(BigDecimal r180_l_num_of_transac) {
        this.r180_l_num_of_transac = r180_l_num_of_transac;
    }

    public String getR181_l_transac_report() {
        return r181_l_transac_report;
    }

    public void setR181_l_transac_report(String r181_l_transac_report) {
        this.r181_l_transac_report = r181_l_transac_report;
    }

    public BigDecimal getR181_l_num_of_transac() {
        return r181_l_num_of_transac;
    }

    public void setR181_l_num_of_transac(BigDecimal r181_l_num_of_transac) {
        this.r181_l_num_of_transac = r181_l_num_of_transac;
    }

    public String getR187_m_transac_life() {
        return r187_m_transac_life;
    }

    public void setR187_m_transac_life(String r187_m_transac_life) {
        this.r187_m_transac_life = r187_m_transac_life;
    }

    public BigDecimal getR187_m_num_of_transac() {
        return r187_m_num_of_transac;
    }

    public void setR187_m_num_of_transac(BigDecimal r187_m_num_of_transac) {
        this.r187_m_num_of_transac = r187_m_num_of_transac;
    }

    public BigDecimal getR187_m_val_of_transac() {
        return r187_m_val_of_transac;
    }

    public void setR187_m_val_of_transac(BigDecimal r187_m_val_of_transac) {
        this.r187_m_val_of_transac = r187_m_val_of_transac;
    }

    public String getR192_n_transac_life() {
        return r192_n_transac_life;
    }

    public void setR192_n_transac_life(String r192_n_transac_life) {
        this.r192_n_transac_life = r192_n_transac_life;
    }

    public BigDecimal getR192_n_num_of_transac() {
        return r192_n_num_of_transac;
    }

    public void setR192_n_num_of_transac(BigDecimal r192_n_num_of_transac) {
        this.r192_n_num_of_transac = r192_n_num_of_transac;
    }

    public BigDecimal getR192_n_val_of_transac() {
        return r192_n_val_of_transac;
    }

    public void setR192_n_val_of_transac(BigDecimal r192_n_val_of_transac) {
        this.r192_n_val_of_transac = r192_n_val_of_transac;
    }

    public String getR196_o_transac_life() {
        return r196_o_transac_life;
    }

    public void setR196_o_transac_life(String r196_o_transac_life) {
        this.r196_o_transac_life = r196_o_transac_life;
    }

    public BigDecimal getR196_o_num_of_transac() {
        return r196_o_num_of_transac;
    }

    public void setR196_o_num_of_transac(BigDecimal r196_o_num_of_transac) {
        this.r196_o_num_of_transac = r196_o_num_of_transac;
    }

    public BigDecimal getR196_o_val_of_transac() {
        return r196_o_val_of_transac;
    }

    public void setR196_o_val_of_transac(BigDecimal r196_o_val_of_transac) {
        this.r196_o_val_of_transac = r196_o_val_of_transac;
    }

    public String getR201_p_transac_life() {
        return r201_p_transac_life;
    }

    public void setR201_p_transac_life(String r201_p_transac_life) {
        this.r201_p_transac_life = r201_p_transac_life;
    }

    public BigDecimal getR201_p_num_of_transac() {
        return r201_p_num_of_transac;
    }

    public void setR201_p_num_of_transac(BigDecimal r201_p_num_of_transac) {
        this.r201_p_num_of_transac = r201_p_num_of_transac;
    }

    public BigDecimal getR201_p_val_of_transac() {
        return r201_p_val_of_transac;
    }

    public void setR201_p_val_of_transac(BigDecimal r201_p_val_of_transac) {
        this.r201_p_val_of_transac = r201_p_val_of_transac;
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


public class AML_Archival_Summary_RowMapper
        implements RowMapper<AML_Archival_Summary_Entity> {

    @Override
    public AML_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        AML_Archival_Summary_Entity obj = new AML_Archival_Summary_Entity();

// =========================
// R11
// =========================
obj.setR11_cust_base_deposit(rs.getString("r11_cust_base_deposit"));
obj.setR11_cust_base_no_of_acct(rs.getBigDecimal("r11_cust_base_no_of_acct"));
obj.setR11_cust_base_tot_dep(rs.getBigDecimal("r11_cust_base_tot_dep"));

// =========================
// R12
// =========================
obj.setR12_cust_base_deposit(rs.getString("r12_cust_base_deposit"));
obj.setR12_cust_base_no_of_acct(rs.getBigDecimal("r12_cust_base_no_of_acct"));
obj.setR12_cust_base_tot_dep(rs.getBigDecimal("r12_cust_base_tot_dep"));

// =========================
// R13
// =========================
obj.setR13_cust_base_deposit(rs.getString("r13_cust_base_deposit"));
obj.setR13_cust_base_no_of_acct(rs.getBigDecimal("r13_cust_base_no_of_acct"));
obj.setR13_cust_base_tot_dep(rs.getBigDecimal("r13_cust_base_tot_dep"));

// =========================
// R14
// =========================
obj.setR14_cust_base_deposit(rs.getString("r14_cust_base_deposit"));
obj.setR14_cust_base_no_of_acct(rs.getBigDecimal("r14_cust_base_no_of_acct"));
obj.setR14_cust_base_tot_dep(rs.getBigDecimal("r14_cust_base_tot_dep"));

// =========================
// R15
// =========================
obj.setR15_cust_base_deposit(rs.getString("r15_cust_base_deposit"));
obj.setR15_cust_base_no_of_acct(rs.getBigDecimal("r15_cust_base_no_of_acct"));
obj.setR15_cust_base_tot_dep(rs.getBigDecimal("r15_cust_base_tot_dep"));


// =========================
// R21
// =========================
obj.setR21_cust_risk_pro_deposit(rs.getString("r21_cust_risk_pro_deposit"));
obj.setR21_cust_risk_pro_num_of_cust(rs.getBigDecimal("r21_cust_risk_pro_num_of_cust"));
obj.setR21_cust_risk_pro_value(rs.getBigDecimal("r21_cust_risk_pro_value"));

// =========================
// R22
// =========================
obj.setR22_cust_risk_pro_deposit(rs.getString("r22_cust_risk_pro_deposit"));
obj.setR22_cust_risk_pro_num_of_cust(rs.getBigDecimal("r22_cust_risk_pro_num_of_cust"));
obj.setR22_cust_risk_pro_value(rs.getBigDecimal("r22_cust_risk_pro_value"));

// =========================
// R23
// =========================
obj.setR23_cust_risk_pro_deposit(rs.getString("r23_cust_risk_pro_deposit"));
obj.setR23_cust_risk_pro_num_of_cust(rs.getBigDecimal("r23_cust_risk_pro_num_of_cust"));
obj.setR23_cust_risk_pro_value(rs.getBigDecimal("r23_cust_risk_pro_value"));

// =========================
// R24
// =========================
obj.setR24_cust_risk_pro_deposit(rs.getString("r24_cust_risk_pro_deposit"));
obj.setR24_cust_risk_pro_num_of_cust(rs.getBigDecimal("r24_cust_risk_pro_num_of_cust"));
obj.setR24_cust_risk_pro_value(rs.getBigDecimal("r24_cust_risk_pro_value"));


// =========================
// R30
// =========================
obj.setR30_b2_cust_deposit(rs.getString("r30_b2_cust_deposit"));
obj.setR30_b2_low_risk_no_cust(rs.getBigDecimal("r30_b2_low_risk_no_cust"));
obj.setR30_b2_low_risk_deposit(rs.getBigDecimal("r30_b2_low_risk_deposit"));
obj.setR30_b2_medi_risk_no_cust(rs.getBigDecimal("r30_b2_medi_risk_no_cust"));
obj.setR30_b2_medi_risk_deposit(rs.getBigDecimal("r30_b2_medi_risk_deposit"));
obj.setR30_b2_high_risk_no_cust(rs.getBigDecimal("r30_b2_high_risk_no_cust"));
obj.setR30_b2_high_risk_deposit(rs.getBigDecimal("r30_b2_high_risk_deposit"));
obj.setR30_b2_tot_no_cust(rs.getBigDecimal("r30_b2_tot_no_cust"));
obj.setR30_b2_tot_deposit(rs.getBigDecimal("r30_b2_tot_deposit"));

// =========================
// R31
// =========================
obj.setR31_b2_cust_deposit(rs.getString("r31_b2_cust_deposit"));
obj.setR31_b2_low_risk_no_cust(rs.getBigDecimal("r31_b2_low_risk_no_cust"));
obj.setR31_b2_low_risk_deposit(rs.getBigDecimal("r31_b2_low_risk_deposit"));
obj.setR31_b2_medi_risk_no_cust(rs.getBigDecimal("r31_b2_medi_risk_no_cust"));
obj.setR31_b2_medi_risk_deposit(rs.getBigDecimal("r31_b2_medi_risk_deposit"));
obj.setR31_b2_high_risk_no_cust(rs.getBigDecimal("r31_b2_high_risk_no_cust"));
obj.setR31_b2_high_risk_deposit(rs.getBigDecimal("r31_b2_high_risk_deposit"));
obj.setR31_b2_tot_no_cust(rs.getBigDecimal("r31_b2_tot_no_cust"));
obj.setR31_b2_tot_deposit(rs.getBigDecimal("r31_b2_tot_deposit"));

// =========================
// R32
// =========================
obj.setR32_b2_cust_deposit(rs.getString("r32_b2_cust_deposit"));
obj.setR32_b2_low_risk_no_cust(rs.getBigDecimal("r32_b2_low_risk_no_cust"));
obj.setR32_b2_low_risk_deposit(rs.getBigDecimal("r32_b2_low_risk_deposit"));
obj.setR32_b2_medi_risk_no_cust(rs.getBigDecimal("r32_b2_medi_risk_no_cust"));
obj.setR32_b2_medi_risk_deposit(rs.getBigDecimal("r32_b2_medi_risk_deposit"));
obj.setR32_b2_high_risk_no_cust(rs.getBigDecimal("r32_b2_high_risk_no_cust"));
obj.setR32_b2_high_risk_deposit(rs.getBigDecimal("r32_b2_high_risk_deposit"));
obj.setR32_b2_tot_no_cust(rs.getBigDecimal("r32_b2_tot_no_cust"));
obj.setR32_b2_tot_deposit(rs.getBigDecimal("r32_b2_tot_deposit"));

// =========================
// R33
// =========================
obj.setR33_b2_cust_deposit(rs.getString("r33_b2_cust_deposit"));
obj.setR33_b2_low_risk_no_cust(rs.getBigDecimal("r33_b2_low_risk_no_cust"));
obj.setR33_b2_low_risk_deposit(rs.getBigDecimal("r33_b2_low_risk_deposit"));
obj.setR33_b2_medi_risk_no_cust(rs.getBigDecimal("r33_b2_medi_risk_no_cust"));
obj.setR33_b2_medi_risk_deposit(rs.getBigDecimal("r33_b2_medi_risk_deposit"));
obj.setR33_b2_high_risk_no_cust(rs.getBigDecimal("r33_b2_high_risk_no_cust"));
obj.setR33_b2_high_risk_deposit(rs.getBigDecimal("r33_b2_high_risk_deposit"));
obj.setR33_b2_tot_no_cust(rs.getBigDecimal("r33_b2_tot_no_cust"));
obj.setR33_b2_tot_deposit(rs.getBigDecimal("r33_b2_tot_deposit"));


// =========================
// R39
// =========================
obj.setR39_cust_base_cust_deposit(rs.getString("r39_cust_base_cust_deposit"));
obj.setR39_cust_base_no_cust(rs.getBigDecimal("r39_cust_base_no_cust"));
obj.setR39_cust_base_deposits(rs.getBigDecimal("r39_cust_base_deposits"));

// =========================
// R40
// =========================
obj.setR40_cust_base_cust_deposit(rs.getString("r40_cust_base_cust_deposit"));
obj.setR40_cust_base_no_cust(rs.getBigDecimal("r40_cust_base_no_cust"));
obj.setR40_cust_base_deposits(rs.getBigDecimal("r40_cust_base_deposits"));

// =========================
// R41
// =========================
obj.setR41_cust_base_cust_deposit(rs.getString("r41_cust_base_cust_deposit"));
obj.setR41_cust_base_no_cust(rs.getBigDecimal("r41_cust_base_no_cust"));
obj.setR41_cust_base_deposits(rs.getBigDecimal("r41_cust_base_deposits"));


// =========================
// R50
// =========================
obj.setR50_brkdown_typ_of_cust(rs.getString("r50_brkdown_typ_of_cust"));
obj.setR50_brkdown_num_of_cust(rs.getBigDecimal("r50_brkdown_num_of_cust"));
obj.setR50_brkdown_tot_depo(rs.getBigDecimal("r50_brkdown_tot_depo"));

// =========================
// R51
// =========================
obj.setR51_brkdown_typ_of_cust(rs.getString("r51_brkdown_typ_of_cust"));
obj.setR51_brkdown_num_of_cust(rs.getBigDecimal("r51_brkdown_num_of_cust"));
obj.setR51_brkdown_tot_depo(rs.getBigDecimal("r51_brkdown_tot_depo"));

// =========================
// R52
// =========================
obj.setR52_brkdown_typ_of_cust(rs.getString("r52_brkdown_typ_of_cust"));
obj.setR52_brkdown_num_of_cust(rs.getBigDecimal("r52_brkdown_num_of_cust"));
obj.setR52_brkdown_tot_depo(rs.getBigDecimal("r52_brkdown_tot_depo"));

// =========================
// R53
// =========================
obj.setR53_brkdown_typ_of_cust(rs.getString("r53_brkdown_typ_of_cust"));
obj.setR53_brkdown_num_of_cust(rs.getBigDecimal("r53_brkdown_num_of_cust"));
obj.setR53_brkdown_tot_depo(rs.getBigDecimal("r53_brkdown_tot_depo"));

// =========================
// R54
// =========================
obj.setR54_brkdown_typ_of_cust(rs.getString("r54_brkdown_typ_of_cust"));
obj.setR54_brkdown_num_of_cust(rs.getBigDecimal("r54_brkdown_num_of_cust"));
obj.setR54_brkdown_tot_depo(rs.getBigDecimal("r54_brkdown_tot_depo"));

// =========================
// R55
// =========================
obj.setR55_brkdown_typ_of_cust(rs.getString("r55_brkdown_typ_of_cust"));
obj.setR55_brkdown_num_of_cust(rs.getBigDecimal("r55_brkdown_num_of_cust"));
obj.setR55_brkdown_tot_depo(rs.getBigDecimal("r55_brkdown_tot_depo"));

// =========================
// R56
// =========================
obj.setR56_brkdown_typ_of_cust(rs.getString("r56_brkdown_typ_of_cust"));
obj.setR56_brkdown_num_of_cust(rs.getBigDecimal("r56_brkdown_num_of_cust"));
obj.setR56_brkdown_tot_depo(rs.getBigDecimal("r56_brkdown_tot_depo"));

// =========================
// R57
// =========================
obj.setR57_brkdown_typ_of_cust(rs.getString("r57_brkdown_typ_of_cust"));
obj.setR57_brkdown_num_of_cust(rs.getBigDecimal("r57_brkdown_num_of_cust"));
obj.setR57_brkdown_tot_depo(rs.getBigDecimal("r57_brkdown_tot_depo"));

// =========================
// R58
// =========================
obj.setR58_brkdown_typ_of_cust(rs.getString("r58_brkdown_typ_of_cust"));
obj.setR58_brkdown_num_of_cust(rs.getBigDecimal("r58_brkdown_num_of_cust"));
obj.setR58_brkdown_tot_depo(rs.getBigDecimal("r58_brkdown_tot_depo"));

// =========================
// R59
// =========================
obj.setR59_brkdown_typ_of_cust(rs.getString("r59_brkdown_typ_of_cust"));
obj.setR59_brkdown_num_of_cust(rs.getBigDecimal("r59_brkdown_num_of_cust"));
obj.setR59_brkdown_tot_depo(rs.getBigDecimal("r59_brkdown_tot_depo"));

// =========================
// R60
// =========================
obj.setR60_brkdown_typ_of_cust(rs.getString("r60_brkdown_typ_of_cust"));
obj.setR60_brkdown_num_of_cust(rs.getBigDecimal("r60_brkdown_num_of_cust"));
obj.setR60_brkdown_tot_depo(rs.getBigDecimal("r60_brkdown_tot_depo"));


// =========================
// R61
// =========================
obj.setR61_brkdown_typ_of_cust(rs.getString("r61_brkdown_typ_of_cust"));
obj.setR61_brkdown_num_of_cust(rs.getBigDecimal("r61_brkdown_num_of_cust"));
obj.setR61_brkdown_tot_depo(rs.getBigDecimal("r61_brkdown_tot_depo"));

// =========================
// R62
// =========================
obj.setR62_brkdown_typ_of_cust(rs.getString("r62_brkdown_typ_of_cust"));
obj.setR62_brkdown_num_of_cust(rs.getBigDecimal("r62_brkdown_num_of_cust"));
obj.setR62_brkdown_tot_depo(rs.getBigDecimal("r62_brkdown_tot_depo"));

// =========================
// R63
// =========================
obj.setR63_brkdown_typ_of_cust(rs.getString("r63_brkdown_typ_of_cust"));
obj.setR63_brkdown_num_of_cust(rs.getBigDecimal("r63_brkdown_num_of_cust"));
obj.setR63_brkdown_tot_depo(rs.getBigDecimal("r63_brkdown_tot_depo"));

// =========================
// R64
// =========================
obj.setR64_brkdown_typ_of_cust(rs.getString("r64_brkdown_typ_of_cust"));
obj.setR64_brkdown_num_of_cust(rs.getBigDecimal("r64_brkdown_num_of_cust"));
obj.setR64_brkdown_tot_depo(rs.getBigDecimal("r64_brkdown_tot_depo"));

// =========================
// R65
// =========================
obj.setR65_brkdown_typ_of_cust(rs.getString("r65_brkdown_typ_of_cust"));
obj.setR65_brkdown_num_of_cust(rs.getBigDecimal("r65_brkdown_num_of_cust"));
obj.setR65_brkdown_tot_depo(rs.getBigDecimal("r65_brkdown_tot_depo"));

// =========================
// R66
// =========================
obj.setR66_brkdown_typ_of_cust(rs.getString("r66_brkdown_typ_of_cust"));
obj.setR66_brkdown_num_of_cust(rs.getBigDecimal("r66_brkdown_num_of_cust"));
obj.setR66_brkdown_tot_depo(rs.getBigDecimal("r66_brkdown_tot_depo"));

// =========================
// R67
// =========================
obj.setR67_brkdown_typ_of_cust(rs.getString("r67_brkdown_typ_of_cust"));
obj.setR67_brkdown_num_of_cust(rs.getBigDecimal("r67_brkdown_num_of_cust"));
obj.setR67_brkdown_tot_depo(rs.getBigDecimal("r67_brkdown_tot_depo"));

// =========================
// R68
// =========================
obj.setR68_brkdown_typ_of_cust(rs.getString("r68_brkdown_typ_of_cust"));
obj.setR68_brkdown_num_of_cust(rs.getBigDecimal("r68_brkdown_num_of_cust"));
obj.setR68_brkdown_tot_depo(rs.getBigDecimal("r68_brkdown_tot_depo"));

// =========================
// R69
// =========================
obj.setR69_brkdown_typ_of_cust(rs.getString("r69_brkdown_typ_of_cust"));
obj.setR69_brkdown_num_of_cust(rs.getBigDecimal("r69_brkdown_num_of_cust"));
obj.setR69_brkdown_tot_depo(rs.getBigDecimal("r69_brkdown_tot_depo"));

// =========================
// R70
// =========================
obj.setR70_brkdown_typ_of_cust(rs.getString("r70_brkdown_typ_of_cust"));
obj.setR70_brkdown_num_of_cust(rs.getBigDecimal("r70_brkdown_num_of_cust"));
obj.setR70_brkdown_tot_depo(rs.getBigDecimal("r70_brkdown_tot_depo"));

// =========================
// R71
// =========================
obj.setR71_brkdown_typ_of_cust(rs.getString("r71_brkdown_typ_of_cust"));
obj.setR71_brkdown_num_of_cust(rs.getBigDecimal("r71_brkdown_num_of_cust"));
obj.setR71_brkdown_tot_depo(rs.getBigDecimal("r71_brkdown_tot_depo"));

// =========================
// R72
// =========================
obj.setR72_brkdown_typ_of_cust(rs.getString("r72_brkdown_typ_of_cust"));
obj.setR72_brkdown_num_of_cust(rs.getBigDecimal("r72_brkdown_num_of_cust"));
obj.setR72_brkdown_tot_depo(rs.getBigDecimal("r72_brkdown_tot_depo"));

// =========================
// R73
// =========================
obj.setR73_brkdown_typ_of_cust(rs.getString("r73_brkdown_typ_of_cust"));
obj.setR73_brkdown_num_of_cust(rs.getBigDecimal("r73_brkdown_num_of_cust"));
obj.setR73_brkdown_tot_depo(rs.getBigDecimal("r73_brkdown_tot_depo"));

// =========================
// R74
// =========================
obj.setR74_brkdown_typ_of_cust(rs.getString("r74_brkdown_typ_of_cust"));
obj.setR74_brkdown_num_of_cust(rs.getBigDecimal("r74_brkdown_num_of_cust"));
obj.setR74_brkdown_tot_depo(rs.getBigDecimal("r74_brkdown_tot_depo"));

// =========================
// R75
// =========================
obj.setR75_brkdown_typ_of_cust(rs.getString("r75_brkdown_typ_of_cust"));
obj.setR75_brkdown_num_of_cust(rs.getBigDecimal("r75_brkdown_num_of_cust"));
obj.setR75_brkdown_tot_depo(rs.getBigDecimal("r75_brkdown_tot_depo"));

// =========================
// R82
// =========================
obj.setR82_e1_tot_no_cust(rs.getBigDecimal("r82_e1_tot_no_cust"));
obj.setR82_e1_loan_on_bal_expo(rs.getBigDecimal("r82_e1_loan_on_bal_expo"));
obj.setR82_e1_deposit(rs.getBigDecimal("r82_e1_deposit"));
obj.setR82_e1_funds_behalf_cust(rs.getBigDecimal("r82_e1_funds_behalf_cust"));
obj.setR82_e1_turnover(rs.getBigDecimal("r82_e1_turnover"));

// =========================
// R83
// =========================
obj.setR83_e1_tot_no_cust(rs.getBigDecimal("r83_e1_tot_no_cust"));
obj.setR83_e1_loan_on_bal_expo(rs.getBigDecimal("r83_e1_loan_on_bal_expo"));
obj.setR83_e1_deposit(rs.getBigDecimal("r83_e1_deposit"));
obj.setR83_e1_funds_behalf_cust(rs.getBigDecimal("r83_e1_funds_behalf_cust"));
obj.setR83_e1_turnover(rs.getBigDecimal("r83_e1_turnover"));

// =========================
// R89
// =========================
obj.setR89_e2_tot_no_cust(rs.getBigDecimal("r89_e2_tot_no_cust"));
obj.setR89_e2_loans_bal_expo(rs.getBigDecimal("r89_e2_loans_bal_expo"));
obj.setR89_e2_deposit(rs.getBigDecimal("r89_e2_deposit"));
obj.setR89_e2_funds_behalf_cust(rs.getBigDecimal("r89_e2_funds_behalf_cust"));
obj.setR89_e2_turnover(rs.getBigDecimal("r89_e2_turnover"));

// =========================
// R90
// =========================
obj.setR90_e2_tot_no_cust(rs.getBigDecimal("r90_e2_tot_no_cust"));
obj.setR90_e2_loans_bal_expo(rs.getBigDecimal("r90_e2_loans_bal_expo"));
obj.setR90_e2_deposit(rs.getBigDecimal("r90_e2_deposit"));
obj.setR90_e2_funds_behalf_cust(rs.getBigDecimal("r90_e2_funds_behalf_cust"));
obj.setR90_e2_turnover(rs.getBigDecimal("r90_e2_turnover"));

// =========================
// R96
// =========================
obj.setR96_e3_tot_no_cust(rs.getBigDecimal("r96_e3_tot_no_cust"));
obj.setR96_e3_loans_bal_expo(rs.getBigDecimal("r96_e3_loans_bal_expo"));
obj.setR96_e3_deposit(rs.getBigDecimal("r96_e3_deposit"));
obj.setR96_e3_funds_behalf_cust(rs.getBigDecimal("r96_e3_funds_behalf_cust"));
obj.setR96_e3_turnover(rs.getBigDecimal("r96_e3_turnover"));

// =========================
// R97
// =========================
obj.setR97_e3_tot_no_cust(rs.getBigDecimal("r97_e3_tot_no_cust"));
obj.setR97_e3_loans_bal_expo(rs.getBigDecimal("r97_e3_loans_bal_expo"));
obj.setR97_e3_deposit(rs.getBigDecimal("r97_e3_deposit"));
obj.setR97_e3_funds_behalf_cust(rs.getBigDecimal("r97_e3_funds_behalf_cust"));
obj.setR97_e3_turnover(rs.getBigDecimal("r97_e3_turnover"));


// =========================
// R104
// =========================
obj.setR104_f_num_of_cust(rs.getBigDecimal("r104_f_num_of_cust"));
obj.setR104_f_loans_bal_expo(rs.getBigDecimal("r104_f_loans_bal_expo"));
obj.setR104_f_deposit(rs.getBigDecimal("r104_f_deposit"));
obj.setR104_f_funds_behalf_cust(rs.getBigDecimal("r104_f_funds_behalf_cust"));
obj.setR104_f_turnover(rs.getBigDecimal("r104_f_turnover"));

// =========================
// R105
// =========================
obj.setR105_f_num_of_cust(rs.getBigDecimal("r105_f_num_of_cust"));
obj.setR105_f_loans_bal_expo(rs.getBigDecimal("r105_f_loans_bal_expo"));
obj.setR105_f_deposit(rs.getBigDecimal("r105_f_deposit"));
obj.setR105_f_funds_behalf_cust(rs.getBigDecimal("r105_f_funds_behalf_cust"));
obj.setR105_f_turnover(rs.getBigDecimal("r105_f_turnover"));


// =========================
// R111
// =========================
obj.setR111_g1_pay_mech(rs.getString("r111_g1_pay_mech"));
obj.setR111_g1_pay_mechanisum(rs.getString("r111_g1_pay_mechanisum"));
obj.setR111_g1_num_trans(rs.getBigDecimal("r111_g1_num_trans"));
obj.setR111_g1_val_trans(rs.getBigDecimal("r111_g1_val_trans"));

// =========================
// R112
// =========================
obj.setR112_g1_pay_mech(rs.getString("r112_g1_pay_mech"));
obj.setR112_g1_pay_mechanisum(rs.getString("r112_g1_pay_mechanisum"));
obj.setR112_g1_num_trans(rs.getBigDecimal("r112_g1_num_trans"));
obj.setR112_g1_val_trans(rs.getBigDecimal("r112_g1_val_trans"));

// =========================
// R113
// =========================
obj.setR113_g1_pay_mech(rs.getString("r113_g1_pay_mech"));
obj.setR113_g1_pay_mechanisum(rs.getString("r113_g1_pay_mechanisum"));
obj.setR113_g1_num_trans(rs.getBigDecimal("r113_g1_num_trans"));
obj.setR113_g1_val_trans(rs.getBigDecimal("r113_g1_val_trans"));

// =========================
// R114
// =========================
obj.setR114_g1_pay_mech(rs.getString("r114_g1_pay_mech"));
obj.setR114_g1_pay_mechanisum(rs.getString("r114_g1_pay_mechanisum"));
obj.setR114_g1_num_trans(rs.getBigDecimal("r114_g1_num_trans"));
obj.setR114_g1_val_trans(rs.getBigDecimal("r114_g1_val_trans"));

// =========================
// R115
// =========================
obj.setR115_g1_pay_mech(rs.getString("r115_g1_pay_mech"));
obj.setR115_g1_pay_mechanisum(rs.getString("r115_g1_pay_mechanisum"));
obj.setR115_g1_num_trans(rs.getBigDecimal("r115_g1_num_trans"));
obj.setR115_g1_val_trans(rs.getBigDecimal("r115_g1_val_trans"));

// =========================
// R116
// =========================
obj.setR116_g1_pay_mech(rs.getString("r116_g1_pay_mech"));
obj.setR116_g1_pay_mechanisum(rs.getString("r116_g1_pay_mechanisum"));
obj.setR116_g1_num_trans(rs.getBigDecimal("r116_g1_num_trans"));
obj.setR116_g1_val_trans(rs.getBigDecimal("r116_g1_val_trans"));

// =========================
// R117
// =========================
obj.setR117_g1_pay_mech(rs.getString("r117_g1_pay_mech"));
obj.setR117_g1_pay_mechanisum(rs.getString("r117_g1_pay_mechanisum"));
obj.setR117_g1_num_trans(rs.getBigDecimal("r117_g1_num_trans"));
obj.setR117_g1_val_trans(rs.getBigDecimal("r117_g1_val_trans"));

// =========================
// R118
// =========================
obj.setR118_g1_pay_mech(rs.getString("r118_g1_pay_mech"));
obj.setR118_g1_pay_mechanisum(rs.getString("r118_g1_pay_mechanisum"));
obj.setR118_g1_num_trans(rs.getBigDecimal("r118_g1_num_trans"));
obj.setR118_g1_val_trans(rs.getBigDecimal("r118_g1_val_trans"));

// =========================
// R119
// =========================
obj.setR119_g1_pay_mech(rs.getString("r119_g1_pay_mech"));
obj.setR119_g1_pay_mechanisum(rs.getString("r119_g1_pay_mechanisum"));
obj.setR119_g1_num_trans(rs.getBigDecimal("r119_g1_num_trans"));
obj.setR119_g1_val_trans(rs.getBigDecimal("r119_g1_val_trans"));

// =========================
// R120
// =========================
obj.setR120_g1_pay_mech(rs.getString("r120_g1_pay_mech"));
obj.setR120_g1_pay_mechanisum(rs.getString("r120_g1_pay_mechanisum"));
obj.setR120_g1_num_trans(rs.getBigDecimal("r120_g1_num_trans"));
obj.setR120_g1_val_trans(rs.getBigDecimal("r120_g1_val_trans"));


// =========================
// R121
// =========================
obj.setR121_g1_pay_mech(rs.getString("r121_g1_pay_mech"));
obj.setR121_g1_pay_mechanisum(rs.getString("r121_g1_pay_mechanisum"));
obj.setR121_g1_num_trans(rs.getBigDecimal("r121_g1_num_trans"));
obj.setR121_g1_val_trans(rs.getBigDecimal("r121_g1_val_trans"));

// =========================
// R122
// =========================
obj.setR122_g1_pay_mech(rs.getString("r122_g1_pay_mech"));
obj.setR122_g1_pay_mechanisum(rs.getString("r122_g1_pay_mechanisum"));
obj.setR122_g1_num_trans(rs.getBigDecimal("r122_g1_num_trans"));
obj.setR122_g1_val_trans(rs.getBigDecimal("r122_g1_val_trans"));

// =========================
// R123
// =========================
obj.setR123_g1_pay_mech(rs.getString("r123_g1_pay_mech"));
obj.setR123_g1_pay_mechanisum(rs.getString("r123_g1_pay_mechanisum"));
obj.setR123_g1_num_trans(rs.getBigDecimal("r123_g1_num_trans"));
obj.setR123_g1_val_trans(rs.getBigDecimal("r123_g1_val_trans"));

// =========================
// R124
// =========================
obj.setR124_g1_pay_mech(rs.getString("r124_g1_pay_mech"));
obj.setR124_g1_pay_mechanisum(rs.getString("r124_g1_pay_mechanisum"));
obj.setR124_g1_num_trans(rs.getBigDecimal("r124_g1_num_trans"));
obj.setR124_g1_val_trans(rs.getBigDecimal("r124_g1_val_trans"));

// =========================
// R125
// =========================
obj.setR125_g1_pay_mech(rs.getString("r125_g1_pay_mech"));
obj.setR125_g1_pay_mechanisum(rs.getString("r125_g1_pay_mechanisum"));
obj.setR125_g1_num_trans(rs.getBigDecimal("r125_g1_num_trans"));
obj.setR125_g1_val_trans(rs.getBigDecimal("r125_g1_val_trans"));

// =========================
// R126
// =========================
obj.setR126_g1_pay_mech(rs.getString("r126_g1_pay_mech"));
obj.setR126_g1_pay_mechanisum(rs.getString("r126_g1_pay_mechanisum"));
obj.setR126_g1_num_trans(rs.getBigDecimal("r126_g1_num_trans"));
obj.setR126_g1_val_trans(rs.getBigDecimal("r126_g1_val_trans"));

// =========================
// R127
// =========================
obj.setR127_g1_pay_mech(rs.getString("r127_g1_pay_mech"));
obj.setR127_g1_pay_mechanisum(rs.getString("r127_g1_pay_mechanisum"));
obj.setR127_g1_num_trans(rs.getBigDecimal("r127_g1_num_trans"));
obj.setR127_g1_val_trans(rs.getBigDecimal("r127_g1_val_trans"));

// =========================
// R128
// =========================
obj.setR128_g1_pay_mech(rs.getString("r128_g1_pay_mech"));
obj.setR128_g1_pay_mechanisum(rs.getString("r128_g1_pay_mechanisum"));
obj.setR128_g1_num_trans(rs.getBigDecimal("r128_g1_num_trans"));
obj.setR128_g1_val_trans(rs.getBigDecimal("r128_g1_val_trans"));

// =========================
// R135
// =========================
obj.setR135_g2_foreign_exchange(rs.getString("r135_g2_foreign_exchange"));
obj.setR135_g2_fore_exchange(rs.getString("r135_g2_fore_exchange"));
obj.setR135_g2_val_transac(rs.getBigDecimal("r135_g2_val_transac"));

// =========================
// R136
// =========================
obj.setR136_g2_fore_exchange(rs.getString("r136_g2_fore_exchange"));
obj.setR136_g2_val_transac(rs.getBigDecimal("r136_g2_val_transac"));

// =========================
// R138
// =========================
obj.setR138_g2_foreign_exchange(rs.getString("r138_g2_foreign_exchange"));
obj.setR138_g2_fore_exchange(rs.getString("r138_g2_fore_exchange"));
obj.setR138_g2_val_transac(rs.getBigDecimal("r138_g2_val_transac"));

// =========================
// R139
// =========================
obj.setR139_g2_fore_exchange(rs.getString("r139_g2_fore_exchange"));
obj.setR139_g2_val_transac(rs.getBigDecimal("r139_g2_val_transac"));

// =========================
// R144
// =========================
obj.setR144_h_types(rs.getString("r144_h_types"));
obj.setR144_h_amount(rs.getBigDecimal("r144_h_amount"));

// =========================
// R145
// =========================
obj.setR145_h_types(rs.getString("r145_h_types"));
obj.setR145_h_amount(rs.getBigDecimal("r145_h_amount"));

// =========================
// R146
// =========================
obj.setR146_h_types(rs.getString("r146_h_types"));
obj.setR146_h_amount(rs.getBigDecimal("r146_h_amount"));

// =========================
// R147
// =========================
obj.setR147_h_types(rs.getString("r147_h_types"));
obj.setR147_h_amount(rs.getBigDecimal("r147_h_amount"));

// =========================
// R148
// =========================
obj.setR148_h_types(rs.getString("r148_h_types"));
obj.setR148_h_amount(rs.getBigDecimal("r148_h_amount"));

// =========================
// R153
// =========================
obj.setR153_i_product_serv(rs.getString("r153_i_product_serv"));
obj.setR153_i_no_cust(rs.getBigDecimal("r153_i_no_cust"));
obj.setR153_i_outs_bal(rs.getBigDecimal("r153_i_outs_bal"));
obj.setR153_i_turnover(rs.getBigDecimal("r153_i_turnover"));

// =========================
// R154
// =========================
obj.setR154_i_product_serv(rs.getString("r154_i_product_serv"));
obj.setR154_i_no_cust(rs.getBigDecimal("r154_i_no_cust"));
obj.setR154_i_outs_bal(rs.getBigDecimal("r154_i_outs_bal"));
obj.setR154_i_turnover(rs.getBigDecimal("r154_i_turnover"));

// =========================
// R155
// =========================
obj.setR155_i_product_serv(rs.getString("r155_i_product_serv"));
obj.setR155_i_no_cust(rs.getBigDecimal("r155_i_no_cust"));
obj.setR155_i_outs_bal(rs.getBigDecimal("r155_i_outs_bal"));
obj.setR155_i_turnover(rs.getBigDecimal("r155_i_turnover"));

// =========================
// R161
// =========================
obj.setR161_j_trade_finc_prod(rs.getString("r161_j_trade_finc_prod"));
obj.setR161_j_num_of_cust(rs.getBigDecimal("r161_j_num_of_cust"));
obj.setR161_j_commitment_at_jun(rs.getBigDecimal("r161_j_commitment_at_jun"));

// =========================
// R162
// =========================
obj.setR162_j_trade_finc_prod(rs.getString("r162_j_trade_finc_prod"));
obj.setR162_j_num_of_cust(rs.getBigDecimal("r162_j_num_of_cust"));
obj.setR162_j_commitment_at_jun(rs.getBigDecimal("r162_j_commitment_at_jun"));

// =========================
// R163
// =========================
obj.setR163_j_trade_finc_prod(rs.getString("r163_j_trade_finc_prod"));
obj.setR163_j_num_of_cust(rs.getBigDecimal("r163_j_num_of_cust"));
obj.setR163_j_commitment_at_jun(rs.getBigDecimal("r163_j_commitment_at_jun"));

// =========================
// R164
// =========================
obj.setR164_j_trade_finc_prod(rs.getString("r164_j_trade_finc_prod"));
obj.setR164_j_num_of_cust(rs.getBigDecimal("r164_j_num_of_cust"));
obj.setR164_j_commitment_at_jun(rs.getBigDecimal("r164_j_commitment_at_jun"));

// =========================
// R170
// =========================
obj.setR170_k_pay_mechanism(rs.getString("r170_k_pay_mechanism"));
obj.setR170_k_pay_mech(rs.getString("r170_k_pay_mech"));
obj.setR170_k_num_of_trans(rs.getBigDecimal("r170_k_num_of_trans"));
obj.setR170_k_value_of_trans(rs.getBigDecimal("r170_k_value_of_trans"));

// =========================
// R171
// =========================
obj.setR171_k_pay_mech(rs.getString("r171_k_pay_mech"));
obj.setR171_k_num_of_trans(rs.getBigDecimal("r171_k_num_of_trans"));
obj.setR171_k_value_of_trans(rs.getBigDecimal("r171_k_value_of_trans"));

// =========================
// R172
// =========================
obj.setR172_k_pay_mechanism(rs.getString("r172_k_pay_mechanism"));
obj.setR172_k_pay_mech(rs.getString("r172_k_pay_mech"));
obj.setR172_k_num_of_trans(rs.getBigDecimal("r172_k_num_of_trans"));
obj.setR172_k_value_of_trans(rs.getBigDecimal("r172_k_value_of_trans"));

// =========================
// R179
// =========================
obj.setR179_l_transac_report(rs.getString("r179_l_transac_report"));
obj.setR179_l_num_of_transac(rs.getBigDecimal("r179_l_num_of_transac"));

// =========================
// R180
// =========================
obj.setR180_l_transac_report(rs.getString("r180_l_transac_report"));
obj.setR180_l_num_of_transac(rs.getBigDecimal("r180_l_num_of_transac"));

// =========================
// R181
// =========================
obj.setR181_l_transac_report(rs.getString("r181_l_transac_report"));
obj.setR181_l_num_of_transac(rs.getBigDecimal("r181_l_num_of_transac"));

// =========================
// R187
// =========================
obj.setR187_m_transac_life(rs.getString("r187_m_transac_life"));
obj.setR187_m_num_of_transac(rs.getBigDecimal("r187_m_num_of_transac"));
obj.setR187_m_val_of_transac(rs.getBigDecimal("r187_m_val_of_transac"));

// =========================
// R192
// =========================
obj.setR192_n_transac_life(rs.getString("r192_n_transac_life"));
obj.setR192_n_num_of_transac(rs.getBigDecimal("r192_n_num_of_transac"));
obj.setR192_n_val_of_transac(rs.getBigDecimal("r192_n_val_of_transac"));

// =========================
// R196
// =========================
obj.setR196_o_transac_life(rs.getString("r196_o_transac_life"));
obj.setR196_o_num_of_transac(rs.getBigDecimal("r196_o_num_of_transac"));
obj.setR196_o_val_of_transac(rs.getBigDecimal("r196_o_val_of_transac"));

// =========================
// R201
// =========================
obj.setR201_p_transac_life(rs.getString("r201_p_transac_life"));
obj.setR201_p_num_of_transac(rs.getBigDecimal("r201_p_num_of_transac"));
obj.setR201_p_val_of_transac(rs.getBigDecimal("r201_p_val_of_transac"));


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


public class AML_Archival_Summary_Entity {
	
     private String	r11_cust_base_deposit;
private BigDecimal	r11_cust_base_no_of_acct;
private BigDecimal	r11_cust_base_tot_dep;
private String	r12_cust_base_deposit;
private BigDecimal	r12_cust_base_no_of_acct;
private BigDecimal	r12_cust_base_tot_dep;
private String	r13_cust_base_deposit;
private BigDecimal	r13_cust_base_no_of_acct;
private BigDecimal	r13_cust_base_tot_dep;
private String	r14_cust_base_deposit;
private BigDecimal	r14_cust_base_no_of_acct;
private BigDecimal	r14_cust_base_tot_dep;
private String	r15_cust_base_deposit;
private BigDecimal	r15_cust_base_no_of_acct;
private BigDecimal	r15_cust_base_tot_dep;
private String	r21_cust_risk_pro_deposit;
private BigDecimal	r21_cust_risk_pro_num_of_cust;
private BigDecimal	r21_cust_risk_pro_value;
private String	r22_cust_risk_pro_deposit;
private BigDecimal	r22_cust_risk_pro_num_of_cust;
private BigDecimal	r22_cust_risk_pro_value;
private String	r23_cust_risk_pro_deposit;
private BigDecimal	r23_cust_risk_pro_num_of_cust;
private BigDecimal	r23_cust_risk_pro_value;
private String	r24_cust_risk_pro_deposit;
private BigDecimal	r24_cust_risk_pro_num_of_cust;
private BigDecimal	r24_cust_risk_pro_value;
private String	r30_b2_cust_deposit;
private BigDecimal	r30_b2_low_risk_no_cust;
private BigDecimal	r30_b2_low_risk_deposit;
private BigDecimal	r30_b2_medi_risk_no_cust;
private BigDecimal	r30_b2_medi_risk_deposit;
private BigDecimal	r30_b2_high_risk_no_cust;
private BigDecimal	r30_b2_high_risk_deposit;
private BigDecimal	r30_b2_tot_no_cust;
private BigDecimal	r30_b2_tot_deposit;
private String	r31_b2_cust_deposit;
private BigDecimal	r31_b2_low_risk_no_cust;
private BigDecimal	r31_b2_low_risk_deposit;
private BigDecimal	r31_b2_medi_risk_no_cust;
private BigDecimal	r31_b2_medi_risk_deposit;
private BigDecimal	r31_b2_high_risk_no_cust;
private BigDecimal	r31_b2_high_risk_deposit;
private BigDecimal	r31_b2_tot_no_cust;
private BigDecimal	r31_b2_tot_deposit;
private String	r32_b2_cust_deposit;
private BigDecimal	r32_b2_low_risk_no_cust;
private BigDecimal	r32_b2_low_risk_deposit;
private BigDecimal	r32_b2_medi_risk_no_cust;
private BigDecimal	r32_b2_medi_risk_deposit;
private BigDecimal	r32_b2_high_risk_no_cust;
private BigDecimal	r32_b2_high_risk_deposit;
private BigDecimal	r32_b2_tot_no_cust;
private BigDecimal	r32_b2_tot_deposit;
private String	r33_b2_cust_deposit;
private BigDecimal	r33_b2_low_risk_no_cust;
private BigDecimal	r33_b2_low_risk_deposit;
private BigDecimal	r33_b2_medi_risk_no_cust;
private BigDecimal	r33_b2_medi_risk_deposit;
private BigDecimal	r33_b2_high_risk_no_cust;
private BigDecimal	r33_b2_high_risk_deposit;
private BigDecimal	r33_b2_tot_no_cust;
private BigDecimal	r33_b2_tot_deposit;
private String	r39_cust_base_cust_deposit;
private BigDecimal	r39_cust_base_no_cust;
private BigDecimal	r39_cust_base_deposits;
private String	r40_cust_base_cust_deposit;
private BigDecimal	r40_cust_base_no_cust;
private BigDecimal	r40_cust_base_deposits;
private String	r41_cust_base_cust_deposit;
private BigDecimal	r41_cust_base_no_cust;
private BigDecimal	r41_cust_base_deposits;
private String	r50_brkdown_typ_of_cust;
private BigDecimal	r50_brkdown_num_of_cust;
private BigDecimal	r50_brkdown_tot_depo;
private String	r51_brkdown_typ_of_cust;
private BigDecimal	r51_brkdown_num_of_cust;
private BigDecimal	r51_brkdown_tot_depo;
private String	r52_brkdown_typ_of_cust;
private BigDecimal	r52_brkdown_num_of_cust;
private BigDecimal	r52_brkdown_tot_depo;
private String	r53_brkdown_typ_of_cust;
private BigDecimal	r53_brkdown_num_of_cust;
private BigDecimal	r53_brkdown_tot_depo;
private String	r54_brkdown_typ_of_cust;
private BigDecimal	r54_brkdown_num_of_cust;
private BigDecimal	r54_brkdown_tot_depo;
private String	r55_brkdown_typ_of_cust;
private BigDecimal	r55_brkdown_num_of_cust;
private BigDecimal	r55_brkdown_tot_depo;
private String	r56_brkdown_typ_of_cust;
private BigDecimal	r56_brkdown_num_of_cust;
private BigDecimal	r56_brkdown_tot_depo;
private String	r57_brkdown_typ_of_cust;
private BigDecimal	r57_brkdown_num_of_cust;
private BigDecimal	r57_brkdown_tot_depo;
private String	r58_brkdown_typ_of_cust;
private BigDecimal	r58_brkdown_num_of_cust;
private BigDecimal	r58_brkdown_tot_depo;
private String	r59_brkdown_typ_of_cust;
private BigDecimal	r59_brkdown_num_of_cust;
private BigDecimal	r59_brkdown_tot_depo;
private String	r60_brkdown_typ_of_cust;
private BigDecimal	r60_brkdown_num_of_cust;
private BigDecimal	r60_brkdown_tot_depo;
private String	r61_brkdown_typ_of_cust;
private BigDecimal	r61_brkdown_num_of_cust;
private BigDecimal	r61_brkdown_tot_depo;
private String	r62_brkdown_typ_of_cust;
private BigDecimal	r62_brkdown_num_of_cust;
private BigDecimal	r62_brkdown_tot_depo;
private String	r63_brkdown_typ_of_cust;
private BigDecimal	r63_brkdown_num_of_cust;
private BigDecimal	r63_brkdown_tot_depo;
private String	r64_brkdown_typ_of_cust;
private BigDecimal	r64_brkdown_num_of_cust;
private BigDecimal	r64_brkdown_tot_depo;
private String	r65_brkdown_typ_of_cust;
private BigDecimal	r65_brkdown_num_of_cust;
private BigDecimal	r65_brkdown_tot_depo;
private String	r66_brkdown_typ_of_cust;
private BigDecimal	r66_brkdown_num_of_cust;
private BigDecimal	r66_brkdown_tot_depo;
private String	r67_brkdown_typ_of_cust;
private BigDecimal	r67_brkdown_num_of_cust;
private BigDecimal	r67_brkdown_tot_depo;
private String	r68_brkdown_typ_of_cust;
private BigDecimal	r68_brkdown_num_of_cust;
private BigDecimal	r68_brkdown_tot_depo;
private String	r69_brkdown_typ_of_cust;
private BigDecimal	r69_brkdown_num_of_cust;
private BigDecimal	r69_brkdown_tot_depo;
private String	r70_brkdown_typ_of_cust;
private BigDecimal	r70_brkdown_num_of_cust;
private BigDecimal	r70_brkdown_tot_depo;
private String	r71_brkdown_typ_of_cust;
private BigDecimal	r71_brkdown_num_of_cust;
private BigDecimal	r71_brkdown_tot_depo;
private String	r72_brkdown_typ_of_cust;
private BigDecimal	r72_brkdown_num_of_cust;
private BigDecimal	r72_brkdown_tot_depo;
private String	r73_brkdown_typ_of_cust;
private BigDecimal	r73_brkdown_num_of_cust;
private BigDecimal	r73_brkdown_tot_depo;
private String	r74_brkdown_typ_of_cust;
private BigDecimal	r74_brkdown_num_of_cust;
private BigDecimal	r74_brkdown_tot_depo;
private String	r75_brkdown_typ_of_cust;
private BigDecimal	r75_brkdown_num_of_cust;
private BigDecimal	r75_brkdown_tot_depo;
private BigDecimal	r82_e1_tot_no_cust;
private BigDecimal	r82_e1_loan_on_bal_expo;
private BigDecimal	r82_e1_deposit;
private BigDecimal	r82_e1_funds_behalf_cust;
private BigDecimal	r82_e1_turnover;
private BigDecimal	r83_e1_tot_no_cust;
private BigDecimal	r83_e1_loan_on_bal_expo;
private BigDecimal	r83_e1_deposit;
private BigDecimal	r83_e1_funds_behalf_cust;
private BigDecimal	r83_e1_turnover;
private BigDecimal	r89_e2_tot_no_cust;
private BigDecimal	r89_e2_loans_bal_expo;
private BigDecimal	r89_e2_deposit;
private BigDecimal	r89_e2_funds_behalf_cust;
private BigDecimal	r89_e2_turnover;
private BigDecimal	r90_e2_tot_no_cust;
private BigDecimal	r90_e2_loans_bal_expo;
private BigDecimal	r90_e2_deposit;
private BigDecimal	r90_e2_funds_behalf_cust;
private BigDecimal	r90_e2_turnover;
private BigDecimal	r96_e3_tot_no_cust;
private BigDecimal	r96_e3_loans_bal_expo;
private BigDecimal	r96_e3_deposit;
private BigDecimal	r96_e3_funds_behalf_cust;
private BigDecimal	r96_e3_turnover;
private BigDecimal	r97_e3_tot_no_cust;
private BigDecimal	r97_e3_loans_bal_expo;
private BigDecimal	r97_e3_deposit;
private BigDecimal	r97_e3_funds_behalf_cust;
private BigDecimal	r97_e3_turnover;
private BigDecimal	r104_f_num_of_cust;
private BigDecimal	r104_f_loans_bal_expo;
private BigDecimal	r104_f_deposit;
private BigDecimal	r104_f_funds_behalf_cust;
private BigDecimal	r104_f_turnover;
private BigDecimal	r105_f_num_of_cust;
private BigDecimal	r105_f_loans_bal_expo;
private BigDecimal	r105_f_deposit;
private BigDecimal	r105_f_funds_behalf_cust;
private BigDecimal	r105_f_turnover;
private String	r111_g1_pay_mech;
private String	r111_g1_pay_mechanisum;
private BigDecimal	r111_g1_num_trans;
private BigDecimal	r111_g1_val_trans;
private String	r112_g1_pay_mech;
private String	r112_g1_pay_mechanisum;
private BigDecimal	r112_g1_num_trans;
private BigDecimal	r112_g1_val_trans;
private String	r113_g1_pay_mech;
private String	r113_g1_pay_mechanisum;
private BigDecimal	r113_g1_num_trans;
private BigDecimal	r113_g1_val_trans;
private String	r114_g1_pay_mech;
private String	r114_g1_pay_mechanisum;
private BigDecimal	r114_g1_num_trans;
private BigDecimal	r114_g1_val_trans;
private String	r115_g1_pay_mech;
private String	r115_g1_pay_mechanisum;
private BigDecimal	r115_g1_num_trans;
private BigDecimal	r115_g1_val_trans;
private String	r116_g1_pay_mech;
private String	r116_g1_pay_mechanisum;
private BigDecimal	r116_g1_num_trans;
private BigDecimal	r116_g1_val_trans;
private String	r117_g1_pay_mech;
private String	r117_g1_pay_mechanisum;
private BigDecimal	r117_g1_num_trans;
private BigDecimal	r117_g1_val_trans;
private String	r118_g1_pay_mech;
private String	r118_g1_pay_mechanisum;
private BigDecimal	r118_g1_num_trans;
private BigDecimal	r118_g1_val_trans;
private String	r119_g1_pay_mech;
private String	r119_g1_pay_mechanisum;
private BigDecimal	r119_g1_num_trans;
private BigDecimal	r119_g1_val_trans;
private String	r120_g1_pay_mech;
private String	r120_g1_pay_mechanisum;
private BigDecimal	r120_g1_num_trans;
private BigDecimal	r120_g1_val_trans;
private String	r121_g1_pay_mech;
private String	r121_g1_pay_mechanisum;
private BigDecimal	r121_g1_num_trans;
private BigDecimal	r121_g1_val_trans;
private String	r122_g1_pay_mech;
private String	r122_g1_pay_mechanisum;
private BigDecimal	r122_g1_num_trans;
private BigDecimal	r122_g1_val_trans;
private String	r123_g1_pay_mech;
private String	r123_g1_pay_mechanisum;
private BigDecimal	r123_g1_num_trans;
private BigDecimal	r123_g1_val_trans;
private String	r124_g1_pay_mech;
private String	r124_g1_pay_mechanisum;
private BigDecimal	r124_g1_num_trans;
private BigDecimal	r124_g1_val_trans;
private String	r125_g1_pay_mech;
private String	r125_g1_pay_mechanisum;
private BigDecimal	r125_g1_num_trans;
private BigDecimal	r125_g1_val_trans;
private String	r126_g1_pay_mech;
private String	r126_g1_pay_mechanisum;
private BigDecimal	r126_g1_num_trans;
private BigDecimal	r126_g1_val_trans;
private String	r127_g1_pay_mech;
private String	r127_g1_pay_mechanisum;
private BigDecimal	r127_g1_num_trans;
private BigDecimal	r127_g1_val_trans;
private String	r128_g1_pay_mech;
private String	r128_g1_pay_mechanisum;
private BigDecimal	r128_g1_num_trans;
private BigDecimal	r128_g1_val_trans;
private String	r135_g2_foreign_exchange;
private String	r135_g2_fore_exchange;
private BigDecimal	r135_g2_val_transac;
private String	r136_g2_fore_exchange;
private BigDecimal	r136_g2_val_transac;
private String	r138_g2_foreign_exchange;
private String	r138_g2_fore_exchange;
private BigDecimal	r138_g2_val_transac;
private String	r139_g2_fore_exchange;
private BigDecimal	r139_g2_val_transac;
private String	r144_h_types;
private BigDecimal	r144_h_amount;
private String	r145_h_types;
private BigDecimal	r145_h_amount;
private String	r146_h_types;
private BigDecimal	r146_h_amount;
private String	r147_h_types;
private BigDecimal	r147_h_amount;
private String	r148_h_types;
private BigDecimal	r148_h_amount;
private String	r153_i_product_serv;
private BigDecimal	r153_i_no_cust;
private BigDecimal	r153_i_outs_bal;
private BigDecimal	r153_i_turnover;
private String	r154_i_product_serv;
private BigDecimal	r154_i_no_cust;
private BigDecimal	r154_i_outs_bal;
private BigDecimal	r154_i_turnover;
private String	r155_i_product_serv;
private BigDecimal	r155_i_no_cust;
private BigDecimal	r155_i_outs_bal;
private BigDecimal	r155_i_turnover;
private String	r161_j_trade_finc_prod;
private BigDecimal	r161_j_num_of_cust;
private BigDecimal	r161_j_commitment_at_jun;
private String	r162_j_trade_finc_prod;
private BigDecimal	r162_j_num_of_cust;
private BigDecimal	r162_j_commitment_at_jun;
private String	r163_j_trade_finc_prod;
private BigDecimal	r163_j_num_of_cust;
private BigDecimal	r163_j_commitment_at_jun;
private String	r164_j_trade_finc_prod;
private BigDecimal	r164_j_num_of_cust;
private BigDecimal	r164_j_commitment_at_jun;
private String	r170_k_pay_mechanism;
private String	r170_k_pay_mech;
private BigDecimal	r170_k_num_of_trans;
private BigDecimal	r170_k_value_of_trans;
private String	r171_k_pay_mech;
private BigDecimal	r171_k_num_of_trans;
private BigDecimal	r171_k_value_of_trans;
private String	r172_k_pay_mechanism;
private String	r172_k_pay_mech;
private BigDecimal	r172_k_num_of_trans;
private BigDecimal	r172_k_value_of_trans;
private String	r179_l_transac_report;
private BigDecimal	r179_l_num_of_transac;
private String	r180_l_transac_report;
private BigDecimal	r180_l_num_of_transac;
private String	r181_l_transac_report;
private BigDecimal	r181_l_num_of_transac;
private String	r187_m_transac_life;
private BigDecimal	r187_m_num_of_transac;
private BigDecimal	r187_m_val_of_transac;
private String	r192_n_transac_life;
private BigDecimal	r192_n_num_of_transac;
private BigDecimal	r192_n_val_of_transac;
private String	r196_o_transac_life;
private BigDecimal	r196_o_num_of_transac;
private BigDecimal	r196_o_val_of_transac;
private String	r201_p_transac_life;
private BigDecimal	r201_p_num_of_transac;
private BigDecimal	r201_p_val_of_transac;
	               
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
	
	public String getR11_cust_base_deposit() {
    return r11_cust_base_deposit;
}
public void setR11_cust_base_deposit(String r11_cust_base_deposit) {
    this.r11_cust_base_deposit = r11_cust_base_deposit;
}
public BigDecimal getR11_cust_base_no_of_acct() {
    return r11_cust_base_no_of_acct;
}
public void setR11_cust_base_no_of_acct(BigDecimal r11_cust_base_no_of_acct) {
    this.r11_cust_base_no_of_acct = r11_cust_base_no_of_acct;
}
public BigDecimal getR11_cust_base_tot_dep() {
    return r11_cust_base_tot_dep;
}
public void setR11_cust_base_tot_dep(BigDecimal r11_cust_base_tot_dep) {
    this.r11_cust_base_tot_dep = r11_cust_base_tot_dep;
}
public String getR12_cust_base_deposit() {
    return r12_cust_base_deposit;
}
public void setR12_cust_base_deposit(String r12_cust_base_deposit) {
    this.r12_cust_base_deposit = r12_cust_base_deposit;
}
public BigDecimal getR12_cust_base_no_of_acct() {
    return r12_cust_base_no_of_acct;
}
public void setR12_cust_base_no_of_acct(BigDecimal r12_cust_base_no_of_acct) {
    this.r12_cust_base_no_of_acct = r12_cust_base_no_of_acct;
}
public BigDecimal getR12_cust_base_tot_dep() {
    return r12_cust_base_tot_dep;
}
public void setR12_cust_base_tot_dep(BigDecimal r12_cust_base_tot_dep) {
    this.r12_cust_base_tot_dep = r12_cust_base_tot_dep;
}
public String getR13_cust_base_deposit() {
    return r13_cust_base_deposit;
}
public void setR13_cust_base_deposit(String r13_cust_base_deposit) {
    this.r13_cust_base_deposit = r13_cust_base_deposit;
}
public BigDecimal getR13_cust_base_no_of_acct() {
    return r13_cust_base_no_of_acct;
}
public void setR13_cust_base_no_of_acct(BigDecimal r13_cust_base_no_of_acct) {
    this.r13_cust_base_no_of_acct = r13_cust_base_no_of_acct;
}
public BigDecimal getR13_cust_base_tot_dep() {
    return r13_cust_base_tot_dep;
}
public void setR13_cust_base_tot_dep(BigDecimal r13_cust_base_tot_dep) {
    this.r13_cust_base_tot_dep = r13_cust_base_tot_dep;
}
public String getR14_cust_base_deposit() {
    return r14_cust_base_deposit;
}
public void setR14_cust_base_deposit(String r14_cust_base_deposit) {
    this.r14_cust_base_deposit = r14_cust_base_deposit;
}
public BigDecimal getR14_cust_base_no_of_acct() {
    return r14_cust_base_no_of_acct;
}
public void setR14_cust_base_no_of_acct(BigDecimal r14_cust_base_no_of_acct) {
    this.r14_cust_base_no_of_acct = r14_cust_base_no_of_acct;
}
public BigDecimal getR14_cust_base_tot_dep() {
    return r14_cust_base_tot_dep;
}
public void setR14_cust_base_tot_dep(BigDecimal r14_cust_base_tot_dep) {
    this.r14_cust_base_tot_dep = r14_cust_base_tot_dep;
}
public String getR15_cust_base_deposit() {
    return r15_cust_base_deposit;
}
public void setR15_cust_base_deposit(String r15_cust_base_deposit) {
    this.r15_cust_base_deposit = r15_cust_base_deposit;
}
public BigDecimal getR15_cust_base_no_of_acct() {
    return r15_cust_base_no_of_acct;
}
public void setR15_cust_base_no_of_acct(BigDecimal r15_cust_base_no_of_acct) {
    this.r15_cust_base_no_of_acct = r15_cust_base_no_of_acct;
}
public BigDecimal getR15_cust_base_tot_dep() {
    return r15_cust_base_tot_dep;
}
public void setR15_cust_base_tot_dep(BigDecimal r15_cust_base_tot_dep) {
    this.r15_cust_base_tot_dep = r15_cust_base_tot_dep;
}
public String getR21_cust_risk_pro_deposit() {
    return r21_cust_risk_pro_deposit;
}
public void setR21_cust_risk_pro_deposit(String r21_cust_risk_pro_deposit) {
    this.r21_cust_risk_pro_deposit = r21_cust_risk_pro_deposit;
}
public BigDecimal getR21_cust_risk_pro_num_of_cust() {
    return r21_cust_risk_pro_num_of_cust;
}
public void setR21_cust_risk_pro_num_of_cust(BigDecimal r21_cust_risk_pro_num_of_cust) {
    this.r21_cust_risk_pro_num_of_cust = r21_cust_risk_pro_num_of_cust;
}
public BigDecimal getR21_cust_risk_pro_value() {
    return r21_cust_risk_pro_value;
}
public void setR21_cust_risk_pro_value(BigDecimal r21_cust_risk_pro_value) {
    this.r21_cust_risk_pro_value = r21_cust_risk_pro_value;
}
public String getR22_cust_risk_pro_deposit() {
    return r22_cust_risk_pro_deposit;
}
public void setR22_cust_risk_pro_deposit(String r22_cust_risk_pro_deposit) {
    this.r22_cust_risk_pro_deposit = r22_cust_risk_pro_deposit;
}
public BigDecimal getR22_cust_risk_pro_num_of_cust() {
    return r22_cust_risk_pro_num_of_cust;
}
public void setR22_cust_risk_pro_num_of_cust(BigDecimal r22_cust_risk_pro_num_of_cust) {
    this.r22_cust_risk_pro_num_of_cust = r22_cust_risk_pro_num_of_cust;
}
public BigDecimal getR22_cust_risk_pro_value() {
    return r22_cust_risk_pro_value;
}
public void setR22_cust_risk_pro_value(BigDecimal r22_cust_risk_pro_value) {
    this.r22_cust_risk_pro_value = r22_cust_risk_pro_value;
}
public String getR23_cust_risk_pro_deposit() {
    return r23_cust_risk_pro_deposit;
}
public void setR23_cust_risk_pro_deposit(String r23_cust_risk_pro_deposit) {
    this.r23_cust_risk_pro_deposit = r23_cust_risk_pro_deposit;
}
public BigDecimal getR23_cust_risk_pro_num_of_cust() {
    return r23_cust_risk_pro_num_of_cust;
}
public void setR23_cust_risk_pro_num_of_cust(BigDecimal r23_cust_risk_pro_num_of_cust) {
    this.r23_cust_risk_pro_num_of_cust = r23_cust_risk_pro_num_of_cust;
}
public BigDecimal getR23_cust_risk_pro_value() {
    return r23_cust_risk_pro_value;
}
public void setR23_cust_risk_pro_value(BigDecimal r23_cust_risk_pro_value) {
    this.r23_cust_risk_pro_value = r23_cust_risk_pro_value;
}
public String getR24_cust_risk_pro_deposit() {
    return r24_cust_risk_pro_deposit;
}
public void setR24_cust_risk_pro_deposit(String r24_cust_risk_pro_deposit) {
    this.r24_cust_risk_pro_deposit = r24_cust_risk_pro_deposit;
}
public BigDecimal getR24_cust_risk_pro_num_of_cust() {
    return r24_cust_risk_pro_num_of_cust;
}
public void setR24_cust_risk_pro_num_of_cust(BigDecimal r24_cust_risk_pro_num_of_cust) {
    this.r24_cust_risk_pro_num_of_cust = r24_cust_risk_pro_num_of_cust;
}
public BigDecimal getR24_cust_risk_pro_value() {
    return r24_cust_risk_pro_value;
}
public void setR24_cust_risk_pro_value(BigDecimal r24_cust_risk_pro_value) {
    this.r24_cust_risk_pro_value = r24_cust_risk_pro_value;
}
public String getR30_b2_cust_deposit() {
    return r30_b2_cust_deposit;
}
public void setR30_b2_cust_deposit(String r30_b2_cust_deposit) {
    this.r30_b2_cust_deposit = r30_b2_cust_deposit;
}
public BigDecimal getR30_b2_low_risk_no_cust() {
    return r30_b2_low_risk_no_cust;
}
public void setR30_b2_low_risk_no_cust(BigDecimal r30_b2_low_risk_no_cust) {
    this.r30_b2_low_risk_no_cust = r30_b2_low_risk_no_cust;
}
public BigDecimal getR30_b2_low_risk_deposit() {
    return r30_b2_low_risk_deposit;
}
public void setR30_b2_low_risk_deposit(BigDecimal r30_b2_low_risk_deposit) {
    this.r30_b2_low_risk_deposit = r30_b2_low_risk_deposit;
}
public BigDecimal getR30_b2_medi_risk_no_cust() {
    return r30_b2_medi_risk_no_cust;
}
public void setR30_b2_medi_risk_no_cust(BigDecimal r30_b2_medi_risk_no_cust) {
    this.r30_b2_medi_risk_no_cust = r30_b2_medi_risk_no_cust;
}
public BigDecimal getR30_b2_medi_risk_deposit() {
    return r30_b2_medi_risk_deposit;
}
public void setR30_b2_medi_risk_deposit(BigDecimal r30_b2_medi_risk_deposit) {
    this.r30_b2_medi_risk_deposit = r30_b2_medi_risk_deposit;
}
public BigDecimal getR30_b2_high_risk_no_cust() {
    return r30_b2_high_risk_no_cust;
}
public void setR30_b2_high_risk_no_cust(BigDecimal r30_b2_high_risk_no_cust) {
    this.r30_b2_high_risk_no_cust = r30_b2_high_risk_no_cust;
}
public BigDecimal getR30_b2_high_risk_deposit() {
    return r30_b2_high_risk_deposit;
}
public void setR30_b2_high_risk_deposit(BigDecimal r30_b2_high_risk_deposit) {
    this.r30_b2_high_risk_deposit = r30_b2_high_risk_deposit;
}
public BigDecimal getR30_b2_tot_no_cust() {
    return r30_b2_tot_no_cust;
}
public void setR30_b2_tot_no_cust(BigDecimal r30_b2_tot_no_cust) {
    this.r30_b2_tot_no_cust = r30_b2_tot_no_cust;
}
public BigDecimal getR30_b2_tot_deposit() {
    return r30_b2_tot_deposit;
}
public void setR30_b2_tot_deposit(BigDecimal r30_b2_tot_deposit) {
    this.r30_b2_tot_deposit = r30_b2_tot_deposit;
}
public String getR31_b2_cust_deposit() {
    return r31_b2_cust_deposit;
}
public void setR31_b2_cust_deposit(String r31_b2_cust_deposit) {
    this.r31_b2_cust_deposit = r31_b2_cust_deposit;
}
public BigDecimal getR31_b2_low_risk_no_cust() {
    return r31_b2_low_risk_no_cust;
}
public void setR31_b2_low_risk_no_cust(BigDecimal r31_b2_low_risk_no_cust) {
    this.r31_b2_low_risk_no_cust = r31_b2_low_risk_no_cust;
}
public BigDecimal getR31_b2_low_risk_deposit() {
    return r31_b2_low_risk_deposit;
}
public void setR31_b2_low_risk_deposit(BigDecimal r31_b2_low_risk_deposit) {
    this.r31_b2_low_risk_deposit = r31_b2_low_risk_deposit;
}
public BigDecimal getR31_b2_medi_risk_no_cust() {
    return r31_b2_medi_risk_no_cust;
}
public void setR31_b2_medi_risk_no_cust(BigDecimal r31_b2_medi_risk_no_cust) {
    this.r31_b2_medi_risk_no_cust = r31_b2_medi_risk_no_cust;
}
public BigDecimal getR31_b2_medi_risk_deposit() {
    return r31_b2_medi_risk_deposit;
}
public void setR31_b2_medi_risk_deposit(BigDecimal r31_b2_medi_risk_deposit) {
    this.r31_b2_medi_risk_deposit = r31_b2_medi_risk_deposit;
}
public BigDecimal getR31_b2_high_risk_no_cust() {
    return r31_b2_high_risk_no_cust;
}
public void setR31_b2_high_risk_no_cust(BigDecimal r31_b2_high_risk_no_cust) {
    this.r31_b2_high_risk_no_cust = r31_b2_high_risk_no_cust;
}
public BigDecimal getR31_b2_high_risk_deposit() {
    return r31_b2_high_risk_deposit;
}
public void setR31_b2_high_risk_deposit(BigDecimal r31_b2_high_risk_deposit) {
    this.r31_b2_high_risk_deposit = r31_b2_high_risk_deposit;
}
public BigDecimal getR31_b2_tot_no_cust() {
    return r31_b2_tot_no_cust;
}
public void setR31_b2_tot_no_cust(BigDecimal r31_b2_tot_no_cust) {
    this.r31_b2_tot_no_cust = r31_b2_tot_no_cust;
}
public BigDecimal getR31_b2_tot_deposit() {
    return r31_b2_tot_deposit;
}
public void setR31_b2_tot_deposit(BigDecimal r31_b2_tot_deposit) {
    this.r31_b2_tot_deposit = r31_b2_tot_deposit;
}
public String getR32_b2_cust_deposit() {
    return r32_b2_cust_deposit;
}
public void setR32_b2_cust_deposit(String r32_b2_cust_deposit) {
    this.r32_b2_cust_deposit = r32_b2_cust_deposit;
}
public BigDecimal getR32_b2_low_risk_no_cust() {
    return r32_b2_low_risk_no_cust;
}
public void setR32_b2_low_risk_no_cust(BigDecimal r32_b2_low_risk_no_cust) {
    this.r32_b2_low_risk_no_cust = r32_b2_low_risk_no_cust;
}
public BigDecimal getR32_b2_low_risk_deposit() {
    return r32_b2_low_risk_deposit;
}
public void setR32_b2_low_risk_deposit(BigDecimal r32_b2_low_risk_deposit) {
    this.r32_b2_low_risk_deposit = r32_b2_low_risk_deposit;
}
public BigDecimal getR32_b2_medi_risk_no_cust() {
    return r32_b2_medi_risk_no_cust;
}
public void setR32_b2_medi_risk_no_cust(BigDecimal r32_b2_medi_risk_no_cust) {
    this.r32_b2_medi_risk_no_cust = r32_b2_medi_risk_no_cust;
}
public BigDecimal getR32_b2_medi_risk_deposit() {
    return r32_b2_medi_risk_deposit;
}
public void setR32_b2_medi_risk_deposit(BigDecimal r32_b2_medi_risk_deposit) {
    this.r32_b2_medi_risk_deposit = r32_b2_medi_risk_deposit;
}
public BigDecimal getR32_b2_high_risk_no_cust() {
    return r32_b2_high_risk_no_cust;
}
public void setR32_b2_high_risk_no_cust(BigDecimal r32_b2_high_risk_no_cust) {
    this.r32_b2_high_risk_no_cust = r32_b2_high_risk_no_cust;
}
public BigDecimal getR32_b2_high_risk_deposit() {
    return r32_b2_high_risk_deposit;
}
public void setR32_b2_high_risk_deposit(BigDecimal r32_b2_high_risk_deposit) {
    this.r32_b2_high_risk_deposit = r32_b2_high_risk_deposit;
}
public BigDecimal getR32_b2_tot_no_cust() {
    return r32_b2_tot_no_cust;
}
public void setR32_b2_tot_no_cust(BigDecimal r32_b2_tot_no_cust) {
    this.r32_b2_tot_no_cust = r32_b2_tot_no_cust;
}
public BigDecimal getR32_b2_tot_deposit() {
    return r32_b2_tot_deposit;
}
public void setR32_b2_tot_deposit(BigDecimal r32_b2_tot_deposit) {
    this.r32_b2_tot_deposit = r32_b2_tot_deposit;
}
public String getR33_b2_cust_deposit() {
    return r33_b2_cust_deposit;
}
public void setR33_b2_cust_deposit(String r33_b2_cust_deposit) {
    this.r33_b2_cust_deposit = r33_b2_cust_deposit;
}
public BigDecimal getR33_b2_low_risk_no_cust() {
    return r33_b2_low_risk_no_cust;
}
public void setR33_b2_low_risk_no_cust(BigDecimal r33_b2_low_risk_no_cust) {
    this.r33_b2_low_risk_no_cust = r33_b2_low_risk_no_cust;
}
public BigDecimal getR33_b2_low_risk_deposit() {
    return r33_b2_low_risk_deposit;
}
public void setR33_b2_low_risk_deposit(BigDecimal r33_b2_low_risk_deposit) {
    this.r33_b2_low_risk_deposit = r33_b2_low_risk_deposit;
}
public BigDecimal getR33_b2_medi_risk_no_cust() {
    return r33_b2_medi_risk_no_cust;
}
public void setR33_b2_medi_risk_no_cust(BigDecimal r33_b2_medi_risk_no_cust) {
    this.r33_b2_medi_risk_no_cust = r33_b2_medi_risk_no_cust;
}
public BigDecimal getR33_b2_medi_risk_deposit() {
    return r33_b2_medi_risk_deposit;
}
public void setR33_b2_medi_risk_deposit(BigDecimal r33_b2_medi_risk_deposit) {
    this.r33_b2_medi_risk_deposit = r33_b2_medi_risk_deposit;
}
public BigDecimal getR33_b2_high_risk_no_cust() {
    return r33_b2_high_risk_no_cust;
}
public void setR33_b2_high_risk_no_cust(BigDecimal r33_b2_high_risk_no_cust) {
    this.r33_b2_high_risk_no_cust = r33_b2_high_risk_no_cust;
}
public BigDecimal getR33_b2_high_risk_deposit() {
    return r33_b2_high_risk_deposit;
}
public void setR33_b2_high_risk_deposit(BigDecimal r33_b2_high_risk_deposit) {
    this.r33_b2_high_risk_deposit = r33_b2_high_risk_deposit;
}
public BigDecimal getR33_b2_tot_no_cust() {
    return r33_b2_tot_no_cust;
}
public void setR33_b2_tot_no_cust(BigDecimal r33_b2_tot_no_cust) {
    this.r33_b2_tot_no_cust = r33_b2_tot_no_cust;
}
public BigDecimal getR33_b2_tot_deposit() {
    return r33_b2_tot_deposit;
}
public void setR33_b2_tot_deposit(BigDecimal r33_b2_tot_deposit) {
    this.r33_b2_tot_deposit = r33_b2_tot_deposit;
}
public String getR39_cust_base_cust_deposit() {
    return r39_cust_base_cust_deposit;
}
public void setR39_cust_base_cust_deposit(String r39_cust_base_cust_deposit) {
    this.r39_cust_base_cust_deposit = r39_cust_base_cust_deposit;
}
public BigDecimal getR39_cust_base_no_cust() {
    return r39_cust_base_no_cust;
}
public void setR39_cust_base_no_cust(BigDecimal r39_cust_base_no_cust) {
    this.r39_cust_base_no_cust = r39_cust_base_no_cust;
}
public BigDecimal getR39_cust_base_deposits() {
    return r39_cust_base_deposits;
}
public void setR39_cust_base_deposits(BigDecimal r39_cust_base_deposits) {
    this.r39_cust_base_deposits = r39_cust_base_deposits;
}
public String getR40_cust_base_cust_deposit() {
    return r40_cust_base_cust_deposit;
}
public void setR40_cust_base_cust_deposit(String r40_cust_base_cust_deposit) {
    this.r40_cust_base_cust_deposit = r40_cust_base_cust_deposit;
}
public BigDecimal getR40_cust_base_no_cust() {
    return r40_cust_base_no_cust;
}
public void setR40_cust_base_no_cust(BigDecimal r40_cust_base_no_cust) {
    this.r40_cust_base_no_cust = r40_cust_base_no_cust;
}
public BigDecimal getR40_cust_base_deposits() {
    return r40_cust_base_deposits;
}
public void setR40_cust_base_deposits(BigDecimal r40_cust_base_deposits) {
    this.r40_cust_base_deposits = r40_cust_base_deposits;
}
public String getR41_cust_base_cust_deposit() {
    return r41_cust_base_cust_deposit;
}
public void setR41_cust_base_cust_deposit(String r41_cust_base_cust_deposit) {
    this.r41_cust_base_cust_deposit = r41_cust_base_cust_deposit;
}
public BigDecimal getR41_cust_base_no_cust() {
    return r41_cust_base_no_cust;
}
public void setR41_cust_base_no_cust(BigDecimal r41_cust_base_no_cust) {
    this.r41_cust_base_no_cust = r41_cust_base_no_cust;
}
public BigDecimal getR41_cust_base_deposits() {
    return r41_cust_base_deposits;
}
public void setR41_cust_base_deposits(BigDecimal r41_cust_base_deposits) {
    this.r41_cust_base_deposits = r41_cust_base_deposits;
}
public String getR50_brkdown_typ_of_cust() {
    return r50_brkdown_typ_of_cust;
}
public void setR50_brkdown_typ_of_cust(String r50_brkdown_typ_of_cust) {
    this.r50_brkdown_typ_of_cust = r50_brkdown_typ_of_cust;
}
public BigDecimal getR50_brkdown_num_of_cust() {
    return r50_brkdown_num_of_cust;
}
public void setR50_brkdown_num_of_cust(BigDecimal r50_brkdown_num_of_cust) {
    this.r50_brkdown_num_of_cust = r50_brkdown_num_of_cust;
}
public BigDecimal getR50_brkdown_tot_depo() {
    return r50_brkdown_tot_depo;
}
public void setR50_brkdown_tot_depo(BigDecimal r50_brkdown_tot_depo) {
    this.r50_brkdown_tot_depo = r50_brkdown_tot_depo;
}
public String getR51_brkdown_typ_of_cust() {
    return r51_brkdown_typ_of_cust;
}
public void setR51_brkdown_typ_of_cust(String r51_brkdown_typ_of_cust) {
    this.r51_brkdown_typ_of_cust = r51_brkdown_typ_of_cust;
}
public BigDecimal getR51_brkdown_num_of_cust() {
    return r51_brkdown_num_of_cust;
}
public void setR51_brkdown_num_of_cust(BigDecimal r51_brkdown_num_of_cust) {
    this.r51_brkdown_num_of_cust = r51_brkdown_num_of_cust;
}
public BigDecimal getR51_brkdown_tot_depo() {
    return r51_brkdown_tot_depo;
}
public void setR51_brkdown_tot_depo(BigDecimal r51_brkdown_tot_depo) {
    this.r51_brkdown_tot_depo = r51_brkdown_tot_depo;
}
public String getR52_brkdown_typ_of_cust() {
    return r52_brkdown_typ_of_cust;
}
public void setR52_brkdown_typ_of_cust(String r52_brkdown_typ_of_cust) {
    this.r52_brkdown_typ_of_cust = r52_brkdown_typ_of_cust;
}
public BigDecimal getR52_brkdown_num_of_cust() {
    return r52_brkdown_num_of_cust;
}
public void setR52_brkdown_num_of_cust(BigDecimal r52_brkdown_num_of_cust) {
    this.r52_brkdown_num_of_cust = r52_brkdown_num_of_cust;
}
public BigDecimal getR52_brkdown_tot_depo() {
    return r52_brkdown_tot_depo;
}
public void setR52_brkdown_tot_depo(BigDecimal r52_brkdown_tot_depo) {
    this.r52_brkdown_tot_depo = r52_brkdown_tot_depo;
}
public String getR53_brkdown_typ_of_cust() {
    return r53_brkdown_typ_of_cust;
}
public void setR53_brkdown_typ_of_cust(String r53_brkdown_typ_of_cust) {
    this.r53_brkdown_typ_of_cust = r53_brkdown_typ_of_cust;
}
public BigDecimal getR53_brkdown_num_of_cust() {
    return r53_brkdown_num_of_cust;
}
public void setR53_brkdown_num_of_cust(BigDecimal r53_brkdown_num_of_cust) {
    this.r53_brkdown_num_of_cust = r53_brkdown_num_of_cust;
}
public BigDecimal getR53_brkdown_tot_depo() {
    return r53_brkdown_tot_depo;
}
public void setR53_brkdown_tot_depo(BigDecimal r53_brkdown_tot_depo) {
    this.r53_brkdown_tot_depo = r53_brkdown_tot_depo;
}
public String getR54_brkdown_typ_of_cust() {
    return r54_brkdown_typ_of_cust;
}
public void setR54_brkdown_typ_of_cust(String r54_brkdown_typ_of_cust) {
    this.r54_brkdown_typ_of_cust = r54_brkdown_typ_of_cust;
}
public BigDecimal getR54_brkdown_num_of_cust() {
    return r54_brkdown_num_of_cust;
}
public void setR54_brkdown_num_of_cust(BigDecimal r54_brkdown_num_of_cust) {
    this.r54_brkdown_num_of_cust = r54_brkdown_num_of_cust;
}
public BigDecimal getR54_brkdown_tot_depo() {
    return r54_brkdown_tot_depo;
}
public void setR54_brkdown_tot_depo(BigDecimal r54_brkdown_tot_depo) {
    this.r54_brkdown_tot_depo = r54_brkdown_tot_depo;
}
public String getR55_brkdown_typ_of_cust() {
    return r55_brkdown_typ_of_cust;
}
public void setR55_brkdown_typ_of_cust(String r55_brkdown_typ_of_cust) {
    this.r55_brkdown_typ_of_cust = r55_brkdown_typ_of_cust;
}
public BigDecimal getR55_brkdown_num_of_cust() {
    return r55_brkdown_num_of_cust;
}
public void setR55_brkdown_num_of_cust(BigDecimal r55_brkdown_num_of_cust) {
    this.r55_brkdown_num_of_cust = r55_brkdown_num_of_cust;
}
public BigDecimal getR55_brkdown_tot_depo() {
    return r55_brkdown_tot_depo;
}
public void setR55_brkdown_tot_depo(BigDecimal r55_brkdown_tot_depo) {
    this.r55_brkdown_tot_depo = r55_brkdown_tot_depo;
}
public String getR56_brkdown_typ_of_cust() {
    return r56_brkdown_typ_of_cust;
}
public void setR56_brkdown_typ_of_cust(String r56_brkdown_typ_of_cust) {
    this.r56_brkdown_typ_of_cust = r56_brkdown_typ_of_cust;
}
public BigDecimal getR56_brkdown_num_of_cust() {
    return r56_brkdown_num_of_cust;
}
public void setR56_brkdown_num_of_cust(BigDecimal r56_brkdown_num_of_cust) {
    this.r56_brkdown_num_of_cust = r56_brkdown_num_of_cust;
}
public BigDecimal getR56_brkdown_tot_depo() {
    return r56_brkdown_tot_depo;
}
public void setR56_brkdown_tot_depo(BigDecimal r56_brkdown_tot_depo) {
    this.r56_brkdown_tot_depo = r56_brkdown_tot_depo;
}
public String getR57_brkdown_typ_of_cust() {
    return r57_brkdown_typ_of_cust;
}
public void setR57_brkdown_typ_of_cust(String r57_brkdown_typ_of_cust) {
    this.r57_brkdown_typ_of_cust = r57_brkdown_typ_of_cust;
}
public BigDecimal getR57_brkdown_num_of_cust() {
    return r57_brkdown_num_of_cust;
}
public void setR57_brkdown_num_of_cust(BigDecimal r57_brkdown_num_of_cust) {
    this.r57_brkdown_num_of_cust = r57_brkdown_num_of_cust;
}
public BigDecimal getR57_brkdown_tot_depo() {
    return r57_brkdown_tot_depo;
}
public void setR57_brkdown_tot_depo(BigDecimal r57_brkdown_tot_depo) {
    this.r57_brkdown_tot_depo = r57_brkdown_tot_depo;
}
public String getR58_brkdown_typ_of_cust() {
    return r58_brkdown_typ_of_cust;
}
public void setR58_brkdown_typ_of_cust(String r58_brkdown_typ_of_cust) {
    this.r58_brkdown_typ_of_cust = r58_brkdown_typ_of_cust;
}
public BigDecimal getR58_brkdown_num_of_cust() {
    return r58_brkdown_num_of_cust;
}
public void setR58_brkdown_num_of_cust(BigDecimal r58_brkdown_num_of_cust) {
    this.r58_brkdown_num_of_cust = r58_brkdown_num_of_cust;
}
public BigDecimal getR58_brkdown_tot_depo() {
    return r58_brkdown_tot_depo;
}
public void setR58_brkdown_tot_depo(BigDecimal r58_brkdown_tot_depo) {
    this.r58_brkdown_tot_depo = r58_brkdown_tot_depo;
}
public String getR59_brkdown_typ_of_cust() {
    return r59_brkdown_typ_of_cust;
}
public void setR59_brkdown_typ_of_cust(String r59_brkdown_typ_of_cust) {
    this.r59_brkdown_typ_of_cust = r59_brkdown_typ_of_cust;
}
public BigDecimal getR59_brkdown_num_of_cust() {
    return r59_brkdown_num_of_cust;
}
public void setR59_brkdown_num_of_cust(BigDecimal r59_brkdown_num_of_cust) {
    this.r59_brkdown_num_of_cust = r59_brkdown_num_of_cust;
}
public BigDecimal getR59_brkdown_tot_depo() {
    return r59_brkdown_tot_depo;
}
public void setR59_brkdown_tot_depo(BigDecimal r59_brkdown_tot_depo) {
    this.r59_brkdown_tot_depo = r59_brkdown_tot_depo;
}
public String getR60_brkdown_typ_of_cust() {
    return r60_brkdown_typ_of_cust;
}
public void setR60_brkdown_typ_of_cust(String r60_brkdown_typ_of_cust) {
    this.r60_brkdown_typ_of_cust = r60_brkdown_typ_of_cust;
}
public BigDecimal getR60_brkdown_num_of_cust() {
    return r60_brkdown_num_of_cust;
}
public void setR60_brkdown_num_of_cust(BigDecimal r60_brkdown_num_of_cust) {
    this.r60_brkdown_num_of_cust = r60_brkdown_num_of_cust;
}
public BigDecimal getR60_brkdown_tot_depo() {
    return r60_brkdown_tot_depo;
}
public void setR60_brkdown_tot_depo(BigDecimal r60_brkdown_tot_depo) {
    this.r60_brkdown_tot_depo = r60_brkdown_tot_depo;
}
public String getR61_brkdown_typ_of_cust() {
    return r61_brkdown_typ_of_cust;
}
public void setR61_brkdown_typ_of_cust(String r61_brkdown_typ_of_cust) {
    this.r61_brkdown_typ_of_cust = r61_brkdown_typ_of_cust;
}
public BigDecimal getR61_brkdown_num_of_cust() {
    return r61_brkdown_num_of_cust;
}
public void setR61_brkdown_num_of_cust(BigDecimal r61_brkdown_num_of_cust) {
    this.r61_brkdown_num_of_cust = r61_brkdown_num_of_cust;
}
public BigDecimal getR61_brkdown_tot_depo() {
    return r61_brkdown_tot_depo;
}
public void setR61_brkdown_tot_depo(BigDecimal r61_brkdown_tot_depo) {
    this.r61_brkdown_tot_depo = r61_brkdown_tot_depo;
}
public String getR62_brkdown_typ_of_cust() {
    return r62_brkdown_typ_of_cust;
}
public void setR62_brkdown_typ_of_cust(String r62_brkdown_typ_of_cust) {
    this.r62_brkdown_typ_of_cust = r62_brkdown_typ_of_cust;
}
public BigDecimal getR62_brkdown_num_of_cust() {
    return r62_brkdown_num_of_cust;
}
public void setR62_brkdown_num_of_cust(BigDecimal r62_brkdown_num_of_cust) {
    this.r62_brkdown_num_of_cust = r62_brkdown_num_of_cust;
}
public BigDecimal getR62_brkdown_tot_depo() {
    return r62_brkdown_tot_depo;
}
public void setR62_brkdown_tot_depo(BigDecimal r62_brkdown_tot_depo) {
    this.r62_brkdown_tot_depo = r62_brkdown_tot_depo;
}
public String getR63_brkdown_typ_of_cust() {
    return r63_brkdown_typ_of_cust;
}
public void setR63_brkdown_typ_of_cust(String r63_brkdown_typ_of_cust) {
    this.r63_brkdown_typ_of_cust = r63_brkdown_typ_of_cust;
}
public BigDecimal getR63_brkdown_num_of_cust() {
    return r63_brkdown_num_of_cust;
}
public void setR63_brkdown_num_of_cust(BigDecimal r63_brkdown_num_of_cust) {
    this.r63_brkdown_num_of_cust = r63_brkdown_num_of_cust;
}
public BigDecimal getR63_brkdown_tot_depo() {
    return r63_brkdown_tot_depo;
}
public void setR63_brkdown_tot_depo(BigDecimal r63_brkdown_tot_depo) {
    this.r63_brkdown_tot_depo = r63_brkdown_tot_depo;
}
public String getR64_brkdown_typ_of_cust() {
    return r64_brkdown_typ_of_cust;
}
public void setR64_brkdown_typ_of_cust(String r64_brkdown_typ_of_cust) {
    this.r64_brkdown_typ_of_cust = r64_brkdown_typ_of_cust;
}
public BigDecimal getR64_brkdown_num_of_cust() {
    return r64_brkdown_num_of_cust;
}
public void setR64_brkdown_num_of_cust(BigDecimal r64_brkdown_num_of_cust) {
    this.r64_brkdown_num_of_cust = r64_brkdown_num_of_cust;
}
public BigDecimal getR64_brkdown_tot_depo() {
    return r64_brkdown_tot_depo;
}
public void setR64_brkdown_tot_depo(BigDecimal r64_brkdown_tot_depo) {
    this.r64_brkdown_tot_depo = r64_brkdown_tot_depo;
}
public String getR65_brkdown_typ_of_cust() {
    return r65_brkdown_typ_of_cust;
}
public void setR65_brkdown_typ_of_cust(String r65_brkdown_typ_of_cust) {
    this.r65_brkdown_typ_of_cust = r65_brkdown_typ_of_cust;
}
public BigDecimal getR65_brkdown_num_of_cust() {
    return r65_brkdown_num_of_cust;
}
public void setR65_brkdown_num_of_cust(BigDecimal r65_brkdown_num_of_cust) {
    this.r65_brkdown_num_of_cust = r65_brkdown_num_of_cust;
}
public BigDecimal getR65_brkdown_tot_depo() {
    return r65_brkdown_tot_depo;
}
public void setR65_brkdown_tot_depo(BigDecimal r65_brkdown_tot_depo) {
    this.r65_brkdown_tot_depo = r65_brkdown_tot_depo;
}
public String getR66_brkdown_typ_of_cust() {
    return r66_brkdown_typ_of_cust;
}
public void setR66_brkdown_typ_of_cust(String r66_brkdown_typ_of_cust) {
    this.r66_brkdown_typ_of_cust = r66_brkdown_typ_of_cust;
}
public BigDecimal getR66_brkdown_num_of_cust() {
    return r66_brkdown_num_of_cust;
}
public void setR66_brkdown_num_of_cust(BigDecimal r66_brkdown_num_of_cust) {
    this.r66_brkdown_num_of_cust = r66_brkdown_num_of_cust;
}
public BigDecimal getR66_brkdown_tot_depo() {
    return r66_brkdown_tot_depo;
}
public void setR66_brkdown_tot_depo(BigDecimal r66_brkdown_tot_depo) {
    this.r66_brkdown_tot_depo = r66_brkdown_tot_depo;
}
public String getR67_brkdown_typ_of_cust() {
    return r67_brkdown_typ_of_cust;
}
public void setR67_brkdown_typ_of_cust(String r67_brkdown_typ_of_cust) {
    this.r67_brkdown_typ_of_cust = r67_brkdown_typ_of_cust;
}
public BigDecimal getR67_brkdown_num_of_cust() {
    return r67_brkdown_num_of_cust;
}
public void setR67_brkdown_num_of_cust(BigDecimal r67_brkdown_num_of_cust) {
    this.r67_brkdown_num_of_cust = r67_brkdown_num_of_cust;
}
public BigDecimal getR67_brkdown_tot_depo() {
    return r67_brkdown_tot_depo;
}
public void setR67_brkdown_tot_depo(BigDecimal r67_brkdown_tot_depo) {
    this.r67_brkdown_tot_depo = r67_brkdown_tot_depo;
}
public String getR68_brkdown_typ_of_cust() {
    return r68_brkdown_typ_of_cust;
}
public void setR68_brkdown_typ_of_cust(String r68_brkdown_typ_of_cust) {
    this.r68_brkdown_typ_of_cust = r68_brkdown_typ_of_cust;
}
public BigDecimal getR68_brkdown_num_of_cust() {
    return r68_brkdown_num_of_cust;
}
public void setR68_brkdown_num_of_cust(BigDecimal r68_brkdown_num_of_cust) {
    this.r68_brkdown_num_of_cust = r68_brkdown_num_of_cust;
}
public BigDecimal getR68_brkdown_tot_depo() {
    return r68_brkdown_tot_depo;
}
public void setR68_brkdown_tot_depo(BigDecimal r68_brkdown_tot_depo) {
    this.r68_brkdown_tot_depo = r68_brkdown_tot_depo;
}
public String getR69_brkdown_typ_of_cust() {
    return r69_brkdown_typ_of_cust;
}
public void setR69_brkdown_typ_of_cust(String r69_brkdown_typ_of_cust) {
    this.r69_brkdown_typ_of_cust = r69_brkdown_typ_of_cust;
}
public BigDecimal getR69_brkdown_num_of_cust() {
    return r69_brkdown_num_of_cust;
}
public void setR69_brkdown_num_of_cust(BigDecimal r69_brkdown_num_of_cust) {
    this.r69_brkdown_num_of_cust = r69_brkdown_num_of_cust;
}
public BigDecimal getR69_brkdown_tot_depo() {
    return r69_brkdown_tot_depo;
}
public void setR69_brkdown_tot_depo(BigDecimal r69_brkdown_tot_depo) {
    this.r69_brkdown_tot_depo = r69_brkdown_tot_depo;
}
public String getR70_brkdown_typ_of_cust() {
    return r70_brkdown_typ_of_cust;
}
public void setR70_brkdown_typ_of_cust(String r70_brkdown_typ_of_cust) {
    this.r70_brkdown_typ_of_cust = r70_brkdown_typ_of_cust;
}
public BigDecimal getR70_brkdown_num_of_cust() {
    return r70_brkdown_num_of_cust;
}
public void setR70_brkdown_num_of_cust(BigDecimal r70_brkdown_num_of_cust) {
    this.r70_brkdown_num_of_cust = r70_brkdown_num_of_cust;
}
public BigDecimal getR70_brkdown_tot_depo() {
    return r70_brkdown_tot_depo;
}
public void setR70_brkdown_tot_depo(BigDecimal r70_brkdown_tot_depo) {
    this.r70_brkdown_tot_depo = r70_brkdown_tot_depo;
}
public String getR71_brkdown_typ_of_cust() {
    return r71_brkdown_typ_of_cust;
}
public void setR71_brkdown_typ_of_cust(String r71_brkdown_typ_of_cust) {
    this.r71_brkdown_typ_of_cust = r71_brkdown_typ_of_cust;
}
public BigDecimal getR71_brkdown_num_of_cust() {
    return r71_brkdown_num_of_cust;
}
public void setR71_brkdown_num_of_cust(BigDecimal r71_brkdown_num_of_cust) {
    this.r71_brkdown_num_of_cust = r71_brkdown_num_of_cust;
}
public BigDecimal getR71_brkdown_tot_depo() {
    return r71_brkdown_tot_depo;
}
public void setR71_brkdown_tot_depo(BigDecimal r71_brkdown_tot_depo) {
    this.r71_brkdown_tot_depo = r71_brkdown_tot_depo;
}
public String getR72_brkdown_typ_of_cust() {
    return r72_brkdown_typ_of_cust;
}
public void setR72_brkdown_typ_of_cust(String r72_brkdown_typ_of_cust) {
    this.r72_brkdown_typ_of_cust = r72_brkdown_typ_of_cust;
}
public BigDecimal getR72_brkdown_num_of_cust() {
    return r72_brkdown_num_of_cust;
}
public void setR72_brkdown_num_of_cust(BigDecimal r72_brkdown_num_of_cust) {
    this.r72_brkdown_num_of_cust = r72_brkdown_num_of_cust;
}
public BigDecimal getR72_brkdown_tot_depo() {
    return r72_brkdown_tot_depo;
}
public void setR72_brkdown_tot_depo(BigDecimal r72_brkdown_tot_depo) {
    this.r72_brkdown_tot_depo = r72_brkdown_tot_depo;
}
public String getR73_brkdown_typ_of_cust() {
    return r73_brkdown_typ_of_cust;
}
public void setR73_brkdown_typ_of_cust(String r73_brkdown_typ_of_cust) {
    this.r73_brkdown_typ_of_cust = r73_brkdown_typ_of_cust;
}
public BigDecimal getR73_brkdown_num_of_cust() {
    return r73_brkdown_num_of_cust;
}
public void setR73_brkdown_num_of_cust(BigDecimal r73_brkdown_num_of_cust) {
    this.r73_brkdown_num_of_cust = r73_brkdown_num_of_cust;
}
public BigDecimal getR73_brkdown_tot_depo() {
    return r73_brkdown_tot_depo;
}
public void setR73_brkdown_tot_depo(BigDecimal r73_brkdown_tot_depo) {
    this.r73_brkdown_tot_depo = r73_brkdown_tot_depo;
}
public String getR74_brkdown_typ_of_cust() {
    return r74_brkdown_typ_of_cust;
}
public void setR74_brkdown_typ_of_cust(String r74_brkdown_typ_of_cust) {
    this.r74_brkdown_typ_of_cust = r74_brkdown_typ_of_cust;
}
public BigDecimal getR74_brkdown_num_of_cust() {
    return r74_brkdown_num_of_cust;
}
public void setR74_brkdown_num_of_cust(BigDecimal r74_brkdown_num_of_cust) {
    this.r74_brkdown_num_of_cust = r74_brkdown_num_of_cust;
}
public BigDecimal getR74_brkdown_tot_depo() {
    return r74_brkdown_tot_depo;
}
public void setR74_brkdown_tot_depo(BigDecimal r74_brkdown_tot_depo) {
    this.r74_brkdown_tot_depo = r74_brkdown_tot_depo;
}
public String getR75_brkdown_typ_of_cust() {
    return r75_brkdown_typ_of_cust;
}
public void setR75_brkdown_typ_of_cust(String r75_brkdown_typ_of_cust) {
    this.r75_brkdown_typ_of_cust = r75_brkdown_typ_of_cust;
}
public BigDecimal getR75_brkdown_num_of_cust() {
    return r75_brkdown_num_of_cust;
}
public void setR75_brkdown_num_of_cust(BigDecimal r75_brkdown_num_of_cust) {
    this.r75_brkdown_num_of_cust = r75_brkdown_num_of_cust;
}
public BigDecimal getR75_brkdown_tot_depo() {
    return r75_brkdown_tot_depo;
}
public void setR75_brkdown_tot_depo(BigDecimal r75_brkdown_tot_depo) {
    this.r75_brkdown_tot_depo = r75_brkdown_tot_depo;
}
public BigDecimal getR82_e1_tot_no_cust() {
    return r82_e1_tot_no_cust;
}
public void setR82_e1_tot_no_cust(BigDecimal r82_e1_tot_no_cust) {
    this.r82_e1_tot_no_cust = r82_e1_tot_no_cust;
}
public BigDecimal getR82_e1_loan_on_bal_expo() {
    return r82_e1_loan_on_bal_expo;
}
public void setR82_e1_loan_on_bal_expo(BigDecimal r82_e1_loan_on_bal_expo) {
    this.r82_e1_loan_on_bal_expo = r82_e1_loan_on_bal_expo;
}
public BigDecimal getR82_e1_deposit() {
    return r82_e1_deposit;
}
public void setR82_e1_deposit(BigDecimal r82_e1_deposit) {
    this.r82_e1_deposit = r82_e1_deposit;
}
public BigDecimal getR82_e1_funds_behalf_cust() {
    return r82_e1_funds_behalf_cust;
}
public void setR82_e1_funds_behalf_cust(BigDecimal r82_e1_funds_behalf_cust) {
    this.r82_e1_funds_behalf_cust = r82_e1_funds_behalf_cust;
}
public BigDecimal getR82_e1_turnover() {
    return r82_e1_turnover;
}
public void setR82_e1_turnover(BigDecimal r82_e1_turnover) {
    this.r82_e1_turnover = r82_e1_turnover;
}
public BigDecimal getR83_e1_tot_no_cust() {
    return r83_e1_tot_no_cust;
}
public void setR83_e1_tot_no_cust(BigDecimal r83_e1_tot_no_cust) {
    this.r83_e1_tot_no_cust = r83_e1_tot_no_cust;
}
public BigDecimal getR83_e1_loan_on_bal_expo() {
    return r83_e1_loan_on_bal_expo;
}
public void setR83_e1_loan_on_bal_expo(BigDecimal r83_e1_loan_on_bal_expo) {
    this.r83_e1_loan_on_bal_expo = r83_e1_loan_on_bal_expo;
}
public BigDecimal getR83_e1_deposit() {
    return r83_e1_deposit;
}
public void setR83_e1_deposit(BigDecimal r83_e1_deposit) {
    this.r83_e1_deposit = r83_e1_deposit;
}
public BigDecimal getR83_e1_funds_behalf_cust() {
    return r83_e1_funds_behalf_cust;
}
public void setR83_e1_funds_behalf_cust(BigDecimal r83_e1_funds_behalf_cust) {
    this.r83_e1_funds_behalf_cust = r83_e1_funds_behalf_cust;
}
public BigDecimal getR83_e1_turnover() {
    return r83_e1_turnover;
}
public void setR83_e1_turnover(BigDecimal r83_e1_turnover) {
    this.r83_e1_turnover = r83_e1_turnover;
}
public BigDecimal getR89_e2_tot_no_cust() {
    return r89_e2_tot_no_cust;
}
public void setR89_e2_tot_no_cust(BigDecimal r89_e2_tot_no_cust) {
    this.r89_e2_tot_no_cust = r89_e2_tot_no_cust;
}
public BigDecimal getR89_e2_loans_bal_expo() {
    return r89_e2_loans_bal_expo;
}
public void setR89_e2_loans_bal_expo(BigDecimal r89_e2_loans_bal_expo) {
    this.r89_e2_loans_bal_expo = r89_e2_loans_bal_expo;
}
public BigDecimal getR89_e2_deposit() {
    return r89_e2_deposit;
}
public void setR89_e2_deposit(BigDecimal r89_e2_deposit) {
    this.r89_e2_deposit = r89_e2_deposit;
}
public BigDecimal getR89_e2_funds_behalf_cust() {
    return r89_e2_funds_behalf_cust;
}
public void setR89_e2_funds_behalf_cust(BigDecimal r89_e2_funds_behalf_cust) {
    this.r89_e2_funds_behalf_cust = r89_e2_funds_behalf_cust;
}
public BigDecimal getR89_e2_turnover() {
    return r89_e2_turnover;
}
public void setR89_e2_turnover(BigDecimal r89_e2_turnover) {
    this.r89_e2_turnover = r89_e2_turnover;
}
public BigDecimal getR90_e2_tot_no_cust() {
    return r90_e2_tot_no_cust;
}
public void setR90_e2_tot_no_cust(BigDecimal r90_e2_tot_no_cust) {
    this.r90_e2_tot_no_cust = r90_e2_tot_no_cust;
}
public BigDecimal getR90_e2_loans_bal_expo() {
    return r90_e2_loans_bal_expo;
}
public void setR90_e2_loans_bal_expo(BigDecimal r90_e2_loans_bal_expo) {
    this.r90_e2_loans_bal_expo = r90_e2_loans_bal_expo;
}
public BigDecimal getR90_e2_deposit() {
    return r90_e2_deposit;
}
public void setR90_e2_deposit(BigDecimal r90_e2_deposit) {
    this.r90_e2_deposit = r90_e2_deposit;
}
public BigDecimal getR90_e2_funds_behalf_cust() {
    return r90_e2_funds_behalf_cust;
}
public void setR90_e2_funds_behalf_cust(BigDecimal r90_e2_funds_behalf_cust) {
    this.r90_e2_funds_behalf_cust = r90_e2_funds_behalf_cust;
}
public BigDecimal getR90_e2_turnover() {
    return r90_e2_turnover;
}
public void setR90_e2_turnover(BigDecimal r90_e2_turnover) {
    this.r90_e2_turnover = r90_e2_turnover;
}
public BigDecimal getR96_e3_tot_no_cust() {
    return r96_e3_tot_no_cust;
}
public void setR96_e3_tot_no_cust(BigDecimal r96_e3_tot_no_cust) {
    this.r96_e3_tot_no_cust = r96_e3_tot_no_cust;
}
public BigDecimal getR96_e3_loans_bal_expo() {
    return r96_e3_loans_bal_expo;
}
public void setR96_e3_loans_bal_expo(BigDecimal r96_e3_loans_bal_expo) {
    this.r96_e3_loans_bal_expo = r96_e3_loans_bal_expo;
}
public BigDecimal getR96_e3_deposit() {
    return r96_e3_deposit;
}
public void setR96_e3_deposit(BigDecimal r96_e3_deposit) {
    this.r96_e3_deposit = r96_e3_deposit;
}
public BigDecimal getR96_e3_funds_behalf_cust() {
    return r96_e3_funds_behalf_cust;
}
public void setR96_e3_funds_behalf_cust(BigDecimal r96_e3_funds_behalf_cust) {
    this.r96_e3_funds_behalf_cust = r96_e3_funds_behalf_cust;
}
public BigDecimal getR96_e3_turnover() {
    return r96_e3_turnover;
}
public void setR96_e3_turnover(BigDecimal r96_e3_turnover) {
    this.r96_e3_turnover = r96_e3_turnover;
}
public BigDecimal getR97_e3_tot_no_cust() {
    return r97_e3_tot_no_cust;
}
public void setR97_e3_tot_no_cust(BigDecimal r97_e3_tot_no_cust) {
    this.r97_e3_tot_no_cust = r97_e3_tot_no_cust;
}
public BigDecimal getR97_e3_loans_bal_expo() {
    return r97_e3_loans_bal_expo;
}
public void setR97_e3_loans_bal_expo(BigDecimal r97_e3_loans_bal_expo) {
    this.r97_e3_loans_bal_expo = r97_e3_loans_bal_expo;
}
public BigDecimal getR97_e3_deposit() {
    return r97_e3_deposit;
}
public void setR97_e3_deposit(BigDecimal r97_e3_deposit) {
    this.r97_e3_deposit = r97_e3_deposit;
}
public BigDecimal getR97_e3_funds_behalf_cust() {
    return r97_e3_funds_behalf_cust;
}
public void setR97_e3_funds_behalf_cust(BigDecimal r97_e3_funds_behalf_cust) {
    this.r97_e3_funds_behalf_cust = r97_e3_funds_behalf_cust;
}
public BigDecimal getR97_e3_turnover() {
    return r97_e3_turnover;
}
public void setR97_e3_turnover(BigDecimal r97_e3_turnover) {
    this.r97_e3_turnover = r97_e3_turnover;
}
public BigDecimal getR104_f_num_of_cust() {
    return r104_f_num_of_cust;
}
public void setR104_f_num_of_cust(BigDecimal r104_f_num_of_cust) {
    this.r104_f_num_of_cust = r104_f_num_of_cust;
}
public BigDecimal getR104_f_loans_bal_expo() {
    return r104_f_loans_bal_expo;
}
public void setR104_f_loans_bal_expo(BigDecimal r104_f_loans_bal_expo) {
    this.r104_f_loans_bal_expo = r104_f_loans_bal_expo;
}
public BigDecimal getR104_f_deposit() {
    return r104_f_deposit;
}
public void setR104_f_deposit(BigDecimal r104_f_deposit) {
    this.r104_f_deposit = r104_f_deposit;
}
public BigDecimal getR104_f_funds_behalf_cust() {
    return r104_f_funds_behalf_cust;
}
public void setR104_f_funds_behalf_cust(BigDecimal r104_f_funds_behalf_cust) {
    this.r104_f_funds_behalf_cust = r104_f_funds_behalf_cust;
}
public BigDecimal getR104_f_turnover() {
    return r104_f_turnover;
}
public void setR104_f_turnover(BigDecimal r104_f_turnover) {
    this.r104_f_turnover = r104_f_turnover;
}
public BigDecimal getR105_f_num_of_cust() {
    return r105_f_num_of_cust;
}
public void setR105_f_num_of_cust(BigDecimal r105_f_num_of_cust) {
    this.r105_f_num_of_cust = r105_f_num_of_cust;
}
public BigDecimal getR105_f_loans_bal_expo() {
    return r105_f_loans_bal_expo;
}
public void setR105_f_loans_bal_expo(BigDecimal r105_f_loans_bal_expo) {
    this.r105_f_loans_bal_expo = r105_f_loans_bal_expo;
}
public BigDecimal getR105_f_deposit() {
    return r105_f_deposit;
}
public void setR105_f_deposit(BigDecimal r105_f_deposit) {
    this.r105_f_deposit = r105_f_deposit;
}
public BigDecimal getR105_f_funds_behalf_cust() {
    return r105_f_funds_behalf_cust;
}
public void setR105_f_funds_behalf_cust(BigDecimal r105_f_funds_behalf_cust) {
    this.r105_f_funds_behalf_cust = r105_f_funds_behalf_cust;
}
public BigDecimal getR105_f_turnover() {
    return r105_f_turnover;
}
public void setR105_f_turnover(BigDecimal r105_f_turnover) {
    this.r105_f_turnover = r105_f_turnover;
}
public String getR111_g1_pay_mech() {
    return r111_g1_pay_mech;
}
public void setR111_g1_pay_mech(String r111_g1_pay_mech) {
    this.r111_g1_pay_mech = r111_g1_pay_mech;
}
public String getR111_g1_pay_mechanisum() {
    return r111_g1_pay_mechanisum;
}
public void setR111_g1_pay_mechanisum(String r111_g1_pay_mechanisum) {
    this.r111_g1_pay_mechanisum = r111_g1_pay_mechanisum;
}
public BigDecimal getR111_g1_num_trans() {
    return r111_g1_num_trans;
}
public void setR111_g1_num_trans(BigDecimal r111_g1_num_trans) {
    this.r111_g1_num_trans = r111_g1_num_trans;
}
public BigDecimal getR111_g1_val_trans() {
    return r111_g1_val_trans;
}
public void setR111_g1_val_trans(BigDecimal r111_g1_val_trans) {
    this.r111_g1_val_trans = r111_g1_val_trans;
}
public String getR112_g1_pay_mech() {
    return r112_g1_pay_mech;
}
public void setR112_g1_pay_mech(String r112_g1_pay_mech) {
    this.r112_g1_pay_mech = r112_g1_pay_mech;
}
public String getR112_g1_pay_mechanisum() {
    return r112_g1_pay_mechanisum;
}
public void setR112_g1_pay_mechanisum(String r112_g1_pay_mechanisum) {
    this.r112_g1_pay_mechanisum = r112_g1_pay_mechanisum;
}
public BigDecimal getR112_g1_num_trans() {
    return r112_g1_num_trans;
}
public void setR112_g1_num_trans(BigDecimal r112_g1_num_trans) {
    this.r112_g1_num_trans = r112_g1_num_trans;
}
public BigDecimal getR112_g1_val_trans() {
    return r112_g1_val_trans;
}
public void setR112_g1_val_trans(BigDecimal r112_g1_val_trans) {
    this.r112_g1_val_trans = r112_g1_val_trans;
}
public String getR113_g1_pay_mech() {
    return r113_g1_pay_mech;
}
public void setR113_g1_pay_mech(String r113_g1_pay_mech) {
    this.r113_g1_pay_mech = r113_g1_pay_mech;
}
public String getR113_g1_pay_mechanisum() {
    return r113_g1_pay_mechanisum;
}
public void setR113_g1_pay_mechanisum(String r113_g1_pay_mechanisum) {
    this.r113_g1_pay_mechanisum = r113_g1_pay_mechanisum;
}
public BigDecimal getR113_g1_num_trans() {
    return r113_g1_num_trans;
}
public void setR113_g1_num_trans(BigDecimal r113_g1_num_trans) {
    this.r113_g1_num_trans = r113_g1_num_trans;
}
public BigDecimal getR113_g1_val_trans() {
    return r113_g1_val_trans;
}
public void setR113_g1_val_trans(BigDecimal r113_g1_val_trans) {
    this.r113_g1_val_trans = r113_g1_val_trans;
}
public String getR114_g1_pay_mech() {
    return r114_g1_pay_mech;
}
public void setR114_g1_pay_mech(String r114_g1_pay_mech) {
    this.r114_g1_pay_mech = r114_g1_pay_mech;
}
public String getR114_g1_pay_mechanisum() {
    return r114_g1_pay_mechanisum;
}
public void setR114_g1_pay_mechanisum(String r114_g1_pay_mechanisum) {
    this.r114_g1_pay_mechanisum = r114_g1_pay_mechanisum;
}
public BigDecimal getR114_g1_num_trans() {
    return r114_g1_num_trans;
}
public void setR114_g1_num_trans(BigDecimal r114_g1_num_trans) {
    this.r114_g1_num_trans = r114_g1_num_trans;
}
public BigDecimal getR114_g1_val_trans() {
    return r114_g1_val_trans;
}
public void setR114_g1_val_trans(BigDecimal r114_g1_val_trans) {
    this.r114_g1_val_trans = r114_g1_val_trans;
}
public String getR115_g1_pay_mech() {
    return r115_g1_pay_mech;
}
public void setR115_g1_pay_mech(String r115_g1_pay_mech) {
    this.r115_g1_pay_mech = r115_g1_pay_mech;
}
public String getR115_g1_pay_mechanisum() {
    return r115_g1_pay_mechanisum;
}
public void setR115_g1_pay_mechanisum(String r115_g1_pay_mechanisum) {
    this.r115_g1_pay_mechanisum = r115_g1_pay_mechanisum;
}
public BigDecimal getR115_g1_num_trans() {
    return r115_g1_num_trans;
}
public void setR115_g1_num_trans(BigDecimal r115_g1_num_trans) {
    this.r115_g1_num_trans = r115_g1_num_trans;
}
public BigDecimal getR115_g1_val_trans() {
    return r115_g1_val_trans;
}
public void setR115_g1_val_trans(BigDecimal r115_g1_val_trans) {
    this.r115_g1_val_trans = r115_g1_val_trans;
}
public String getR116_g1_pay_mech() {
    return r116_g1_pay_mech;
}
public void setR116_g1_pay_mech(String r116_g1_pay_mech) {
    this.r116_g1_pay_mech = r116_g1_pay_mech;
}
public String getR116_g1_pay_mechanisum() {
    return r116_g1_pay_mechanisum;
}
public void setR116_g1_pay_mechanisum(String r116_g1_pay_mechanisum) {
    this.r116_g1_pay_mechanisum = r116_g1_pay_mechanisum;
}
public BigDecimal getR116_g1_num_trans() {
    return r116_g1_num_trans;
}
public void setR116_g1_num_trans(BigDecimal r116_g1_num_trans) {
    this.r116_g1_num_trans = r116_g1_num_trans;
}
public BigDecimal getR116_g1_val_trans() {
    return r116_g1_val_trans;
}
public void setR116_g1_val_trans(BigDecimal r116_g1_val_trans) {
    this.r116_g1_val_trans = r116_g1_val_trans;
}
public String getR117_g1_pay_mech() {
    return r117_g1_pay_mech;
}
public void setR117_g1_pay_mech(String r117_g1_pay_mech) {
    this.r117_g1_pay_mech = r117_g1_pay_mech;
}
public String getR117_g1_pay_mechanisum() {
    return r117_g1_pay_mechanisum;
}
public void setR117_g1_pay_mechanisum(String r117_g1_pay_mechanisum) {
    this.r117_g1_pay_mechanisum = r117_g1_pay_mechanisum;
}
public BigDecimal getR117_g1_num_trans() {
    return r117_g1_num_trans;
}
public void setR117_g1_num_trans(BigDecimal r117_g1_num_trans) {
    this.r117_g1_num_trans = r117_g1_num_trans;
}
public BigDecimal getR117_g1_val_trans() {
    return r117_g1_val_trans;
}
public void setR117_g1_val_trans(BigDecimal r117_g1_val_trans) {
    this.r117_g1_val_trans = r117_g1_val_trans;
}
public String getR118_g1_pay_mech() {
    return r118_g1_pay_mech;
}
public void setR118_g1_pay_mech(String r118_g1_pay_mech) {
    this.r118_g1_pay_mech = r118_g1_pay_mech;
}
public String getR118_g1_pay_mechanisum() {
    return r118_g1_pay_mechanisum;
}
public void setR118_g1_pay_mechanisum(String r118_g1_pay_mechanisum) {
    this.r118_g1_pay_mechanisum = r118_g1_pay_mechanisum;
}
public BigDecimal getR118_g1_num_trans() {
    return r118_g1_num_trans;
}
public void setR118_g1_num_trans(BigDecimal r118_g1_num_trans) {
    this.r118_g1_num_trans = r118_g1_num_trans;
}
public BigDecimal getR118_g1_val_trans() {
    return r118_g1_val_trans;
}
public void setR118_g1_val_trans(BigDecimal r118_g1_val_trans) {
    this.r118_g1_val_trans = r118_g1_val_trans;
}
public String getR119_g1_pay_mech() {
    return r119_g1_pay_mech;
}
public void setR119_g1_pay_mech(String r119_g1_pay_mech) {
    this.r119_g1_pay_mech = r119_g1_pay_mech;
}
public String getR119_g1_pay_mechanisum() {
    return r119_g1_pay_mechanisum;
}
public void setR119_g1_pay_mechanisum(String r119_g1_pay_mechanisum) {
    this.r119_g1_pay_mechanisum = r119_g1_pay_mechanisum;
}
public BigDecimal getR119_g1_num_trans() {
    return r119_g1_num_trans;
}
public void setR119_g1_num_trans(BigDecimal r119_g1_num_trans) {
    this.r119_g1_num_trans = r119_g1_num_trans;
}
public BigDecimal getR119_g1_val_trans() {
    return r119_g1_val_trans;
}
public void setR119_g1_val_trans(BigDecimal r119_g1_val_trans) {
    this.r119_g1_val_trans = r119_g1_val_trans;
}
public String getR120_g1_pay_mech() {
    return r120_g1_pay_mech;
}
public void setR120_g1_pay_mech(String r120_g1_pay_mech) {
    this.r120_g1_pay_mech = r120_g1_pay_mech;
}
public String getR120_g1_pay_mechanisum() {
    return r120_g1_pay_mechanisum;
}
public void setR120_g1_pay_mechanisum(String r120_g1_pay_mechanisum) {
    this.r120_g1_pay_mechanisum = r120_g1_pay_mechanisum;
}
public BigDecimal getR120_g1_num_trans() {
    return r120_g1_num_trans;
}
public void setR120_g1_num_trans(BigDecimal r120_g1_num_trans) {
    this.r120_g1_num_trans = r120_g1_num_trans;
}
public BigDecimal getR120_g1_val_trans() {
    return r120_g1_val_trans;
}
public void setR120_g1_val_trans(BigDecimal r120_g1_val_trans) {
    this.r120_g1_val_trans = r120_g1_val_trans;
}
public String getR121_g1_pay_mech() {
    return r121_g1_pay_mech;
}
public void setR121_g1_pay_mech(String r121_g1_pay_mech) {
    this.r121_g1_pay_mech = r121_g1_pay_mech;
}
public String getR121_g1_pay_mechanisum() {
    return r121_g1_pay_mechanisum;
}
public void setR121_g1_pay_mechanisum(String r121_g1_pay_mechanisum) {
    this.r121_g1_pay_mechanisum = r121_g1_pay_mechanisum;
}
public BigDecimal getR121_g1_num_trans() {
    return r121_g1_num_trans;
}
public void setR121_g1_num_trans(BigDecimal r121_g1_num_trans) {
    this.r121_g1_num_trans = r121_g1_num_trans;
}
public BigDecimal getR121_g1_val_trans() {
    return r121_g1_val_trans;
}
public void setR121_g1_val_trans(BigDecimal r121_g1_val_trans) {
    this.r121_g1_val_trans = r121_g1_val_trans;
}
public String getR122_g1_pay_mech() {
    return r122_g1_pay_mech;
}
public void setR122_g1_pay_mech(String r122_g1_pay_mech) {
    this.r122_g1_pay_mech = r122_g1_pay_mech;
}
public String getR122_g1_pay_mechanisum() {
    return r122_g1_pay_mechanisum;
}
public void setR122_g1_pay_mechanisum(String r122_g1_pay_mechanisum) {
    this.r122_g1_pay_mechanisum = r122_g1_pay_mechanisum;
}
public BigDecimal getR122_g1_num_trans() {
    return r122_g1_num_trans;
}
public void setR122_g1_num_trans(BigDecimal r122_g1_num_trans) {
    this.r122_g1_num_trans = r122_g1_num_trans;
}
public BigDecimal getR122_g1_val_trans() {
    return r122_g1_val_trans;
}
public void setR122_g1_val_trans(BigDecimal r122_g1_val_trans) {
    this.r122_g1_val_trans = r122_g1_val_trans;
}
public String getR123_g1_pay_mech() {
    return r123_g1_pay_mech;
}
public void setR123_g1_pay_mech(String r123_g1_pay_mech) {
    this.r123_g1_pay_mech = r123_g1_pay_mech;
}
public String getR123_g1_pay_mechanisum() {
    return r123_g1_pay_mechanisum;
}
public void setR123_g1_pay_mechanisum(String r123_g1_pay_mechanisum) {
    this.r123_g1_pay_mechanisum = r123_g1_pay_mechanisum;
}
public BigDecimal getR123_g1_num_trans() {
    return r123_g1_num_trans;
}
public void setR123_g1_num_trans(BigDecimal r123_g1_num_trans) {
    this.r123_g1_num_trans = r123_g1_num_trans;
}
public BigDecimal getR123_g1_val_trans() {
    return r123_g1_val_trans;
}
public void setR123_g1_val_trans(BigDecimal r123_g1_val_trans) {
    this.r123_g1_val_trans = r123_g1_val_trans;
}
public String getR124_g1_pay_mech() {
    return r124_g1_pay_mech;
}
public void setR124_g1_pay_mech(String r124_g1_pay_mech) {
    this.r124_g1_pay_mech = r124_g1_pay_mech;
}
public String getR124_g1_pay_mechanisum() {
    return r124_g1_pay_mechanisum;
}
public void setR124_g1_pay_mechanisum(String r124_g1_pay_mechanisum) {
    this.r124_g1_pay_mechanisum = r124_g1_pay_mechanisum;
}
public BigDecimal getR124_g1_num_trans() {
    return r124_g1_num_trans;
}
public void setR124_g1_num_trans(BigDecimal r124_g1_num_trans) {
    this.r124_g1_num_trans = r124_g1_num_trans;
}
public BigDecimal getR124_g1_val_trans() {
    return r124_g1_val_trans;
}
public void setR124_g1_val_trans(BigDecimal r124_g1_val_trans) {
    this.r124_g1_val_trans = r124_g1_val_trans;
}
public String getR125_g1_pay_mech() {
    return r125_g1_pay_mech;
}
public void setR125_g1_pay_mech(String r125_g1_pay_mech) {
    this.r125_g1_pay_mech = r125_g1_pay_mech;
}
public String getR125_g1_pay_mechanisum() {
    return r125_g1_pay_mechanisum;
}
public void setR125_g1_pay_mechanisum(String r125_g1_pay_mechanisum) {
    this.r125_g1_pay_mechanisum = r125_g1_pay_mechanisum;
}
public BigDecimal getR125_g1_num_trans() {
    return r125_g1_num_trans;
}
public void setR125_g1_num_trans(BigDecimal r125_g1_num_trans) {
    this.r125_g1_num_trans = r125_g1_num_trans;
}
public BigDecimal getR125_g1_val_trans() {
    return r125_g1_val_trans;
}
public void setR125_g1_val_trans(BigDecimal r125_g1_val_trans) {
    this.r125_g1_val_trans = r125_g1_val_trans;
}
public String getR126_g1_pay_mech() {
    return r126_g1_pay_mech;
}
public void setR126_g1_pay_mech(String r126_g1_pay_mech) {
    this.r126_g1_pay_mech = r126_g1_pay_mech;
}
public String getR126_g1_pay_mechanisum() {
    return r126_g1_pay_mechanisum;
}
public void setR126_g1_pay_mechanisum(String r126_g1_pay_mechanisum) {
    this.r126_g1_pay_mechanisum = r126_g1_pay_mechanisum;
}
public BigDecimal getR126_g1_num_trans() {
    return r126_g1_num_trans;
}
public void setR126_g1_num_trans(BigDecimal r126_g1_num_trans) {
    this.r126_g1_num_trans = r126_g1_num_trans;
}
public BigDecimal getR126_g1_val_trans() {
    return r126_g1_val_trans;
}
public void setR126_g1_val_trans(BigDecimal r126_g1_val_trans) {
    this.r126_g1_val_trans = r126_g1_val_trans;
}
public String getR127_g1_pay_mech() {
    return r127_g1_pay_mech;
}
public void setR127_g1_pay_mech(String r127_g1_pay_mech) {
    this.r127_g1_pay_mech = r127_g1_pay_mech;
}
public String getR127_g1_pay_mechanisum() {
    return r127_g1_pay_mechanisum;
}
public void setR127_g1_pay_mechanisum(String r127_g1_pay_mechanisum) {
    this.r127_g1_pay_mechanisum = r127_g1_pay_mechanisum;
}
public BigDecimal getR127_g1_num_trans() {
    return r127_g1_num_trans;
}
public void setR127_g1_num_trans(BigDecimal r127_g1_num_trans) {
    this.r127_g1_num_trans = r127_g1_num_trans;
}
public BigDecimal getR127_g1_val_trans() {
    return r127_g1_val_trans;
}
public void setR127_g1_val_trans(BigDecimal r127_g1_val_trans) {
    this.r127_g1_val_trans = r127_g1_val_trans;
}
public String getR128_g1_pay_mech() {
    return r128_g1_pay_mech;
}
public void setR128_g1_pay_mech(String r128_g1_pay_mech) {
    this.r128_g1_pay_mech = r128_g1_pay_mech;
}
public String getR128_g1_pay_mechanisum() {
    return r128_g1_pay_mechanisum;
}
public void setR128_g1_pay_mechanisum(String r128_g1_pay_mechanisum) {
    this.r128_g1_pay_mechanisum = r128_g1_pay_mechanisum;
}
public BigDecimal getR128_g1_num_trans() {
    return r128_g1_num_trans;
}
public void setR128_g1_num_trans(BigDecimal r128_g1_num_trans) {
    this.r128_g1_num_trans = r128_g1_num_trans;
}
public BigDecimal getR128_g1_val_trans() {
    return r128_g1_val_trans;
}
public void setR128_g1_val_trans(BigDecimal r128_g1_val_trans) {
    this.r128_g1_val_trans = r128_g1_val_trans;
}
public String getR135_g2_foreign_exchange() {
    return r135_g2_foreign_exchange;
}
public void setR135_g2_foreign_exchange(String r135_g2_foreign_exchange) {
    this.r135_g2_foreign_exchange = r135_g2_foreign_exchange;
}
public String getR135_g2_fore_exchange() {
    return r135_g2_fore_exchange;
}
public void setR135_g2_fore_exchange(String r135_g2_fore_exchange) {
    this.r135_g2_fore_exchange = r135_g2_fore_exchange;
}
public BigDecimal getR135_g2_val_transac() {
    return r135_g2_val_transac;
}
public void setR135_g2_val_transac(BigDecimal r135_g2_val_transac) {
    this.r135_g2_val_transac = r135_g2_val_transac;
}
public String getR136_g2_fore_exchange() {
    return r136_g2_fore_exchange;
}
public void setR136_g2_fore_exchange(String r136_g2_fore_exchange) {
    this.r136_g2_fore_exchange = r136_g2_fore_exchange;
}
public BigDecimal getR136_g2_val_transac() {
    return r136_g2_val_transac;
}
public void setR136_g2_val_transac(BigDecimal r136_g2_val_transac) {
    this.r136_g2_val_transac = r136_g2_val_transac;
}
public String getR138_g2_foreign_exchange() {
    return r138_g2_foreign_exchange;
}
public void setR138_g2_foreign_exchange(String r138_g2_foreign_exchange) {
    this.r138_g2_foreign_exchange = r138_g2_foreign_exchange;
}
public String getR138_g2_fore_exchange() {
    return r138_g2_fore_exchange;
}
public void setR138_g2_fore_exchange(String r138_g2_fore_exchange) {
    this.r138_g2_fore_exchange = r138_g2_fore_exchange;
}
public BigDecimal getR138_g2_val_transac() {
    return r138_g2_val_transac;
}
public void setR138_g2_val_transac(BigDecimal r138_g2_val_transac) {
    this.r138_g2_val_transac = r138_g2_val_transac;
}
public String getR139_g2_fore_exchange() {
    return r139_g2_fore_exchange;
}
public void setR139_g2_fore_exchange(String r139_g2_fore_exchange) {
    this.r139_g2_fore_exchange = r139_g2_fore_exchange;
}
public BigDecimal getR139_g2_val_transac() {
    return r139_g2_val_transac;
}
public void setR139_g2_val_transac(BigDecimal r139_g2_val_transac) {
    this.r139_g2_val_transac = r139_g2_val_transac;
}
public String getR144_h_types() {
    return r144_h_types;
}
public void setR144_h_types(String r144_h_types) {
    this.r144_h_types = r144_h_types;
}
public BigDecimal getR144_h_amount() {
    return r144_h_amount;
}
public void setR144_h_amount(BigDecimal r144_h_amount) {
    this.r144_h_amount = r144_h_amount;
}
public String getR145_h_types() {
    return r145_h_types;
}
public void setR145_h_types(String r145_h_types) {
    this.r145_h_types = r145_h_types;
}
public BigDecimal getR145_h_amount() {
    return r145_h_amount;
}
public void setR145_h_amount(BigDecimal r145_h_amount) {
    this.r145_h_amount = r145_h_amount;
}
public String getR146_h_types() {
    return r146_h_types;
}
public void setR146_h_types(String r146_h_types) {
    this.r146_h_types = r146_h_types;
}
public BigDecimal getR146_h_amount() {
    return r146_h_amount;
}
public void setR146_h_amount(BigDecimal r146_h_amount) {
    this.r146_h_amount = r146_h_amount;
}
public String getR147_h_types() {
    return r147_h_types;
}
public void setR147_h_types(String r147_h_types) {
    this.r147_h_types = r147_h_types;
}
public BigDecimal getR147_h_amount() {
    return r147_h_amount;
}
public void setR147_h_amount(BigDecimal r147_h_amount) {
    this.r147_h_amount = r147_h_amount;
}
public String getR148_h_types() {
    return r148_h_types;
}
public void setR148_h_types(String r148_h_types) {
    this.r148_h_types = r148_h_types;
}
public BigDecimal getR148_h_amount() {
    return r148_h_amount;
}
public void setR148_h_amount(BigDecimal r148_h_amount) {
    this.r148_h_amount = r148_h_amount;
}
public String getR153_i_product_serv() {
    return r153_i_product_serv;
}
public void setR153_i_product_serv(String r153_i_product_serv) {
    this.r153_i_product_serv = r153_i_product_serv;
}
public BigDecimal getR153_i_no_cust() {
    return r153_i_no_cust;
}
public void setR153_i_no_cust(BigDecimal r153_i_no_cust) {
    this.r153_i_no_cust = r153_i_no_cust;
}
public BigDecimal getR153_i_outs_bal() {
    return r153_i_outs_bal;
}
public void setR153_i_outs_bal(BigDecimal r153_i_outs_bal) {
    this.r153_i_outs_bal = r153_i_outs_bal;
}
public BigDecimal getR153_i_turnover() {
    return r153_i_turnover;
}
public void setR153_i_turnover(BigDecimal r153_i_turnover) {
    this.r153_i_turnover = r153_i_turnover;
}
public String getR154_i_product_serv() {
    return r154_i_product_serv;
}
public void setR154_i_product_serv(String r154_i_product_serv) {
    this.r154_i_product_serv = r154_i_product_serv;
}
public BigDecimal getR154_i_no_cust() {
    return r154_i_no_cust;
}
public void setR154_i_no_cust(BigDecimal r154_i_no_cust) {
    this.r154_i_no_cust = r154_i_no_cust;
}
public BigDecimal getR154_i_outs_bal() {
    return r154_i_outs_bal;
}
public void setR154_i_outs_bal(BigDecimal r154_i_outs_bal) {
    this.r154_i_outs_bal = r154_i_outs_bal;
}
public BigDecimal getR154_i_turnover() {
    return r154_i_turnover;
}
public void setR154_i_turnover(BigDecimal r154_i_turnover) {
    this.r154_i_turnover = r154_i_turnover;
}
public String getR155_i_product_serv() {
    return r155_i_product_serv;
}
public void setR155_i_product_serv(String r155_i_product_serv) {
    this.r155_i_product_serv = r155_i_product_serv;
}
public BigDecimal getR155_i_no_cust() {
    return r155_i_no_cust;
}
public void setR155_i_no_cust(BigDecimal r155_i_no_cust) {
    this.r155_i_no_cust = r155_i_no_cust;
}
public BigDecimal getR155_i_outs_bal() {
    return r155_i_outs_bal;
}
public void setR155_i_outs_bal(BigDecimal r155_i_outs_bal) {
    this.r155_i_outs_bal = r155_i_outs_bal;
}
public BigDecimal getR155_i_turnover() {
    return r155_i_turnover;
}
public void setR155_i_turnover(BigDecimal r155_i_turnover) {
    this.r155_i_turnover = r155_i_turnover;
}
public String getR161_j_trade_finc_prod() {
    return r161_j_trade_finc_prod;
}
public void setR161_j_trade_finc_prod(String r161_j_trade_finc_prod) {
    this.r161_j_trade_finc_prod = r161_j_trade_finc_prod;
}
public BigDecimal getR161_j_num_of_cust() {
    return r161_j_num_of_cust;
}
public void setR161_j_num_of_cust(BigDecimal r161_j_num_of_cust) {
    this.r161_j_num_of_cust = r161_j_num_of_cust;
}
public BigDecimal getR161_j_commitment_at_jun() {
    return r161_j_commitment_at_jun;
}
public void setR161_j_commitment_at_jun(BigDecimal r161_j_commitment_at_jun) {
    this.r161_j_commitment_at_jun = r161_j_commitment_at_jun;
}
public String getR162_j_trade_finc_prod() {
    return r162_j_trade_finc_prod;
}
public void setR162_j_trade_finc_prod(String r162_j_trade_finc_prod) {
    this.r162_j_trade_finc_prod = r162_j_trade_finc_prod;
}
public BigDecimal getR162_j_num_of_cust() {
    return r162_j_num_of_cust;
}
public void setR162_j_num_of_cust(BigDecimal r162_j_num_of_cust) {
    this.r162_j_num_of_cust = r162_j_num_of_cust;
}
public BigDecimal getR162_j_commitment_at_jun() {
    return r162_j_commitment_at_jun;
}
public void setR162_j_commitment_at_jun(BigDecimal r162_j_commitment_at_jun) {
    this.r162_j_commitment_at_jun = r162_j_commitment_at_jun;
}
public String getR163_j_trade_finc_prod() {
    return r163_j_trade_finc_prod;
}
public void setR163_j_trade_finc_prod(String r163_j_trade_finc_prod) {
    this.r163_j_trade_finc_prod = r163_j_trade_finc_prod;
}
public BigDecimal getR163_j_num_of_cust() {
    return r163_j_num_of_cust;
}
public void setR163_j_num_of_cust(BigDecimal r163_j_num_of_cust) {
    this.r163_j_num_of_cust = r163_j_num_of_cust;
}
public BigDecimal getR163_j_commitment_at_jun() {
    return r163_j_commitment_at_jun;
}
public void setR163_j_commitment_at_jun(BigDecimal r163_j_commitment_at_jun) {
    this.r163_j_commitment_at_jun = r163_j_commitment_at_jun;
}
public String getR164_j_trade_finc_prod() {
    return r164_j_trade_finc_prod;
}
public void setR164_j_trade_finc_prod(String r164_j_trade_finc_prod) {
    this.r164_j_trade_finc_prod = r164_j_trade_finc_prod;
}
public BigDecimal getR164_j_num_of_cust() {
    return r164_j_num_of_cust;
}
public void setR164_j_num_of_cust(BigDecimal r164_j_num_of_cust) {
    this.r164_j_num_of_cust = r164_j_num_of_cust;
}
public BigDecimal getR164_j_commitment_at_jun() {
    return r164_j_commitment_at_jun;
}
public void setR164_j_commitment_at_jun(BigDecimal r164_j_commitment_at_jun) {
    this.r164_j_commitment_at_jun = r164_j_commitment_at_jun;
}
public String getR170_k_pay_mechanism() {
    return r170_k_pay_mechanism;
}
public void setR170_k_pay_mechanism(String r170_k_pay_mechanism) {
    this.r170_k_pay_mechanism = r170_k_pay_mechanism;
}
public String getR170_k_pay_mech() {
    return r170_k_pay_mech;
}
public void setR170_k_pay_mech(String r170_k_pay_mech) {
    this.r170_k_pay_mech = r170_k_pay_mech;
}
public BigDecimal getR170_k_num_of_trans() {
    return r170_k_num_of_trans;
}
public void setR170_k_num_of_trans(BigDecimal r170_k_num_of_trans) {
    this.r170_k_num_of_trans = r170_k_num_of_trans;
}
public BigDecimal getR170_k_value_of_trans() {
    return r170_k_value_of_trans;
}
public void setR170_k_value_of_trans(BigDecimal r170_k_value_of_trans) {
    this.r170_k_value_of_trans = r170_k_value_of_trans;
}
public String getR171_k_pay_mech() {
    return r171_k_pay_mech;
}
public void setR171_k_pay_mech(String r171_k_pay_mech) {
    this.r171_k_pay_mech = r171_k_pay_mech;
}
public BigDecimal getR171_k_num_of_trans() {
    return r171_k_num_of_trans;
}
public void setR171_k_num_of_trans(BigDecimal r171_k_num_of_trans) {
    this.r171_k_num_of_trans = r171_k_num_of_trans;
}
public BigDecimal getR171_k_value_of_trans() {
    return r171_k_value_of_trans;
}
public void setR171_k_value_of_trans(BigDecimal r171_k_value_of_trans) {
    this.r171_k_value_of_trans = r171_k_value_of_trans;
}
public String getR172_k_pay_mechanism() {
    return r172_k_pay_mechanism;
}
public void setR172_k_pay_mechanism(String r172_k_pay_mechanism) {
    this.r172_k_pay_mechanism = r172_k_pay_mechanism;
}
public String getR172_k_pay_mech() {
    return r172_k_pay_mech;
}
public void setR172_k_pay_mech(String r172_k_pay_mech) {
    this.r172_k_pay_mech = r172_k_pay_mech;
}
public BigDecimal getR172_k_num_of_trans() {
    return r172_k_num_of_trans;
}
public void setR172_k_num_of_trans(BigDecimal r172_k_num_of_trans) {
    this.r172_k_num_of_trans = r172_k_num_of_trans;
}
public BigDecimal getR172_k_value_of_trans() {
    return r172_k_value_of_trans;
}
public void setR172_k_value_of_trans(BigDecimal r172_k_value_of_trans) {
    this.r172_k_value_of_trans = r172_k_value_of_trans;
}
public String getR179_l_transac_report() {
    return r179_l_transac_report;
}
public void setR179_l_transac_report(String r179_l_transac_report) {
    this.r179_l_transac_report = r179_l_transac_report;
}
public BigDecimal getR179_l_num_of_transac() {
    return r179_l_num_of_transac;
}
public void setR179_l_num_of_transac(BigDecimal r179_l_num_of_transac) {
    this.r179_l_num_of_transac = r179_l_num_of_transac;
}
public String getR180_l_transac_report() {
    return r180_l_transac_report;
}
public void setR180_l_transac_report(String r180_l_transac_report) {
    this.r180_l_transac_report = r180_l_transac_report;
}
public BigDecimal getR180_l_num_of_transac() {
    return r180_l_num_of_transac;
}
public void setR180_l_num_of_transac(BigDecimal r180_l_num_of_transac) {
    this.r180_l_num_of_transac = r180_l_num_of_transac;
}
public String getR181_l_transac_report() {
    return r181_l_transac_report;
}
public void setR181_l_transac_report(String r181_l_transac_report) {
    this.r181_l_transac_report = r181_l_transac_report;
}
public BigDecimal getR181_l_num_of_transac() {
    return r181_l_num_of_transac;
}
public void setR181_l_num_of_transac(BigDecimal r181_l_num_of_transac) {
    this.r181_l_num_of_transac = r181_l_num_of_transac;
}
public String getR187_m_transac_life() {
    return r187_m_transac_life;
}
public void setR187_m_transac_life(String r187_m_transac_life) {
    this.r187_m_transac_life = r187_m_transac_life;
}
public BigDecimal getR187_m_num_of_transac() {
    return r187_m_num_of_transac;
}
public void setR187_m_num_of_transac(BigDecimal r187_m_num_of_transac) {
    this.r187_m_num_of_transac = r187_m_num_of_transac;
}
public BigDecimal getR187_m_val_of_transac() {
    return r187_m_val_of_transac;
}
public void setR187_m_val_of_transac(BigDecimal r187_m_val_of_transac) {
    this.r187_m_val_of_transac = r187_m_val_of_transac;
}
public String getR192_n_transac_life() {
    return r192_n_transac_life;
}
public void setR192_n_transac_life(String r192_n_transac_life) {
    this.r192_n_transac_life = r192_n_transac_life;
}
public BigDecimal getR192_n_num_of_transac() {
    return r192_n_num_of_transac;
}
public void setR192_n_num_of_transac(BigDecimal r192_n_num_of_transac) {
    this.r192_n_num_of_transac = r192_n_num_of_transac;
}
public BigDecimal getR192_n_val_of_transac() {
    return r192_n_val_of_transac;
}
public void setR192_n_val_of_transac(BigDecimal r192_n_val_of_transac) {
    this.r192_n_val_of_transac = r192_n_val_of_transac;
}
public String getR196_o_transac_life() {
    return r196_o_transac_life;
}
public void setR196_o_transac_life(String r196_o_transac_life) {
    this.r196_o_transac_life = r196_o_transac_life;
}
public BigDecimal getR196_o_num_of_transac() {
    return r196_o_num_of_transac;
}
public void setR196_o_num_of_transac(BigDecimal r196_o_num_of_transac) {
    this.r196_o_num_of_transac = r196_o_num_of_transac;
}
public BigDecimal getR196_o_val_of_transac() {
    return r196_o_val_of_transac;
}
public void setR196_o_val_of_transac(BigDecimal r196_o_val_of_transac) {
    this.r196_o_val_of_transac = r196_o_val_of_transac;
}
public String getR201_p_transac_life() {
    return r201_p_transac_life;
}
public void setR201_p_transac_life(String r201_p_transac_life) {
    this.r201_p_transac_life = r201_p_transac_life;
}
public BigDecimal getR201_p_num_of_transac() {
    return r201_p_num_of_transac;
}
public void setR201_p_num_of_transac(BigDecimal r201_p_num_of_transac) {
    this.r201_p_num_of_transac = r201_p_num_of_transac;
}
public BigDecimal getR201_p_val_of_transac() {
    return r201_p_val_of_transac;
}
public void setR201_p_val_of_transac(BigDecimal r201_p_val_of_transac) {
    this.r201_p_val_of_transac = r201_p_val_of_transac;
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
// DETAIL ENTITY  AML
// =====================================================	

public class AML_Detail_RowMapper implements RowMapper<AML_Detail_Entity> {

    @Override
    public AML_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        AML_Detail_Entity obj = new AML_Detail_Entity();

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

public class AML_Detail_Entity {

   
	
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


public class AML_Archival_Detail_RowMapper 
        implements RowMapper<AML_Archival_Detail_Entity> {

    @Override
    public AML_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        AML_Archival_Detail_Entity obj = new AML_Archival_Detail_Entity();

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

public class AML_Archival_Detail_Entity {

   
	
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
// MODEL AND VIEW METHOD summary AML
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
		 		 	 public ModelAndView getAMLView(

	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("AML View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    // =====================================================
	    // ARCHIVAL MODE
	    // =====================================================

	    if ("ARCHIVAL".equals(type) && version != null) {

	        List<AML_Archival_Summary_Entity> T1Master = new ArrayList<>();
	    

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

	        List<AML_Summary_Entity> T1Master = new ArrayList<>();
	       

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

	    mv.setViewName("BRRS/AML");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getAMLcurrentDtl(
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

	            List<AML_Archival_Detail_Entity> archivalDetailList;

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

	            List<AML_Detail_Entity> currentDetailList;

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

	    mv.setViewName("BRRS/AML");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
	
//=====================================================
// MODEL AND VIEW METHOD ARCHIVAL VIEW
//=====================================================

public List<Object[]> getAMLArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<AML_Archival_Summary_Entity> repoData =
						getarchivaldatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (AML_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					AML_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  AML  Archival data: " + e.getMessage());
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
		ModelAndView mv = new ModelAndView("BRRS/AML"); 

		if (acctNo != null) {
			AML_Detail_Entity amlEntity = findByDetailAcctnumber(acctNo);
			if (amlEntity != null && amlEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(amlEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("AMLData", amlEntity);
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

			AML_Detail_Entity existing = findByDetailAcctnumber(acctNo);
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
    "UPDATE BRRS_AML_DETAILTABLE " +
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
							logger.info("Transaction committed — calling BRRS_AML_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_AML_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating AML  record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
//=====================================================
// DETAIL EXCEL 
//=====================================================


	public byte[] getAMLDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  AML  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getAMLDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("AML Details ");

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
				List<AML_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (AML_Detail_Entity item : reportData) { 
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
					logger.info("No data found for AML — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating AML Excel", e);
				return new byte[0];
			}
		}
	
	
//=====================================================
// ARCHIVAL DETAIL EXCEL 
//=====================================================

	public byte[] getAMLDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for AML ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("AML Detail NEW");

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
				List<AML_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (AML_Archival_Detail_Entity item : reportData) {
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
					logger.info("No data found for AML — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating AML NEW Excel", e);
				return new byte[0];
			}
		}
		
		
//=====================================================
// Summary EXCEL 
//=====================================================

	public byte[] getAMLExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.AML");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelAMLARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<AML_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		
		
			System.out.println("DATA SIZE IS : "+dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  AML report. Returning empty result.");
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

						int startRow =10;
						
				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						AML_Summary_Entity record = dataList.get(i);
					
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						
						     // A TABLE

                    Cell cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR11_cust_base_no_of_acct() != null) {
                        cellC.setCellValue(record.getR11_cust_base_no_of_acct().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    Cell cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(2);
                    if (record.getR11_cust_base_tot_dep() != null) {
                        cellD.setCellValue(record.getR11_cust_base_tot_dep().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 12
                    row = sheet.getRow(11);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR12_cust_base_no_of_acct() != null) {
                        cellC.setCellValue(record.getR12_cust_base_no_of_acct().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR12_cust_base_tot_dep() != null) {
                        cellD.setCellValue(record.getR12_cust_base_tot_dep().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 13
                    row = sheet.getRow(12);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR13_cust_base_no_of_acct() != null) {
                        cellC.setCellValue(record.getR13_cust_base_no_of_acct().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR13_cust_base_tot_dep() != null) {
                        cellD.setCellValue(record.getR13_cust_base_tot_dep().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 14
                    row = sheet.getRow(13);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR14_cust_base_no_of_acct() != null) {
                        cellC.setCellValue(record.getR14_cust_base_no_of_acct().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR14_cust_base_tot_dep() != null) {
                        cellD.setCellValue(record.getR14_cust_base_tot_dep().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // B1 TABLE

                    // ROW 21
                    row = sheet.getRow(20);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR21_cust_risk_pro_num_of_cust() != null) {
                        cellC.setCellValue(record.getR21_cust_risk_pro_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR21_cust_risk_pro_value() != null) {
                        cellD.setCellValue(record.getR21_cust_risk_pro_value().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 22
                    row = sheet.getRow(21);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR22_cust_risk_pro_num_of_cust() != null) {
                        cellC.setCellValue(record.getR22_cust_risk_pro_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR22_cust_risk_pro_value() != null) {
                        cellD.setCellValue(record.getR22_cust_risk_pro_value().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 23
                    row = sheet.getRow(22);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR23_cust_risk_pro_num_of_cust() != null) {
                        cellC.setCellValue(record.getR23_cust_risk_pro_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR23_cust_risk_pro_value() != null) {
                        cellD.setCellValue(record.getR23_cust_risk_pro_value().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // B2 TABLE

                    // ROW 30
                    row = sheet.getRow(29);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR30_b2_low_risk_no_cust() != null) {
                        cellC.setCellValue(record.getR30_b2_low_risk_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR30_b2_low_risk_deposit() != null) {
                        cellD.setCellValue(record.getR30_b2_low_risk_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    Cell cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR30_b2_medi_risk_no_cust() != null) {
                        cellE.setCellValue(record.getR30_b2_medi_risk_no_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    Cell cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR30_b2_medi_risk_deposit() != null) {
                        cellF.setCellValue(record.getR30_b2_medi_risk_deposit().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    Cell cellG = row.getCell(6);
                    if (cellG == null)
                        cellG = row.createCell(6);
                    if (record.getR30_b2_high_risk_no_cust() != null) {
                        cellG.setCellValue(record.getR30_b2_high_risk_no_cust().doubleValue());
                    } else {
                        cellG.setCellValue(0);
                    }

                    Cell cellH = row.getCell(7);
                    if (cellH == null)
                        cellH = row.createCell(7);
                    if (record.getR30_b2_high_risk_deposit() != null) {
                        cellH.setCellValue(record.getR30_b2_high_risk_deposit().doubleValue());
                    } else {
                        cellH.setCellValue(0);
                    }

                    // ROW 31
                    row = sheet.getRow(30);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR31_b2_low_risk_no_cust() != null) {
                        cellC.setCellValue(record.getR31_b2_low_risk_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR31_b2_low_risk_deposit() != null) {
                        cellD.setCellValue(record.getR31_b2_low_risk_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR31_b2_medi_risk_no_cust() != null) {
                        cellE.setCellValue(record.getR31_b2_medi_risk_no_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR31_b2_medi_risk_deposit() != null) {
                        cellF.setCellValue(record.getR31_b2_medi_risk_deposit().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    cellG = row.getCell(6);
                    if (cellG == null)
                        cellG = row.createCell(6);
                    if (record.getR31_b2_high_risk_no_cust() != null) {
                        cellG.setCellValue(record.getR31_b2_high_risk_no_cust().doubleValue());
                    } else {
                        cellG.setCellValue(0);
                    }

                    cellH = row.getCell(7);
                    if (cellH == null)
                        cellH = row.createCell(7);
                    if (record.getR31_b2_high_risk_deposit() != null) {
                        cellH.setCellValue(record.getR31_b2_high_risk_deposit().doubleValue());
                    } else {
                        cellH.setCellValue(0);
                    }

                    // ROW 32
                    row = sheet.getRow(31);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR32_b2_low_risk_no_cust() != null) {
                        cellC.setCellValue(record.getR32_b2_low_risk_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR32_b2_low_risk_deposit() != null) {
                        cellD.setCellValue(record.getR32_b2_low_risk_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // C TABLE 39 AND 40

                    // ROW 39
                    row = sheet.getRow(38);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR39_cust_base_no_cust() != null) {
                        cellC.setCellValue(record.getR39_cust_base_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR39_cust_base_deposits() != null) {
                        cellD.setCellValue(record.getR39_cust_base_deposits().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 40
                    row = sheet.getRow(39);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR40_cust_base_no_cust() != null) {
                        cellC.setCellValue(record.getR40_cust_base_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR40_cust_base_deposits() != null) {
                        cellD.setCellValue(record.getR40_cust_base_deposits().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // D TABLE 39 AND 40 D AND E COLUMN

                    // ROW 51
                    row = sheet.getRow(50);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR51_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR51_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR51_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR51_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 52
                    row = sheet.getRow(51);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR52_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR52_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR52_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR52_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 53
                    row = sheet.getRow(52);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR53_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR53_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR53_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR53_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 54
                    row = sheet.getRow(53);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR54_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR54_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR54_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR54_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 57
                    row = sheet.getRow(56);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR57_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR57_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR57_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR57_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 58
                    row = sheet.getRow(57);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR58_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR58_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR58_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR58_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 59
                    row = sheet.getRow(58);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR59_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR59_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR59_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR59_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 60
                    row = sheet.getRow(59);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR60_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR60_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR60_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR60_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 61
                    row = sheet.getRow(60);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR61_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR61_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR61_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR61_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 62
                    row = sheet.getRow(61);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR62_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR62_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR62_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR62_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 63
                    row = sheet.getRow(62);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR63_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR63_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR63_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR63_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 64
                    row = sheet.getRow(63);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR64_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR64_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR64_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR64_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 65
                    row = sheet.getRow(64);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR65_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR65_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR65_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR65_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 66
                    row = sheet.getRow(65);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR66_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR66_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR66_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR66_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 67
                    row = sheet.getRow(66);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR67_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR67_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR67_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR67_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 68
                    row = sheet.getRow(67);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR68_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR68_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR68_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR68_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 69
                    row = sheet.getRow(68);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR69_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR69_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR69_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR69_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 72
                    row = sheet.getRow(71);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR72_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR72_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR72_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR72_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 73
                    row = sheet.getRow(72);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR73_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR73_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR73_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR73_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 74
                    row = sheet.getRow(73);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR74_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR74_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR74_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR74_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // E1 TABLE

                    // ROW 82
                    row = sheet.getRow(81);

                    Cell cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR82_e1_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR82_e1_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(1);
                    if (record.getR82_e1_loan_on_bal_expo() != null) {
                        cellC.setCellValue(record.getR82_e1_loan_on_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR82_e1_deposit() != null) {
                        cellD.setCellValue(record.getR82_e1_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR82_e1_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR82_e1_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR82_e1_turnover() != null) {
                        cellF.setCellValue(record.getR82_e1_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 83
                    row = sheet.getRow(82);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR83_e1_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR83_e1_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR83_e1_loan_on_bal_expo() != null) {
                        cellC.setCellValue(record.getR83_e1_loan_on_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR83_e1_deposit() != null) {
                        cellD.setCellValue(record.getR83_e1_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR83_e1_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR83_e1_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR83_e1_turnover() != null) {
                        cellF.setCellValue(record.getR83_e1_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // E2 TABLE

                    // ROW 89
                    row = sheet.getRow(88);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR89_e2_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR89_e2_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR89_e2_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR89_e2_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR89_e2_deposit() != null) {
                        cellD.setCellValue(record.getR89_e2_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR89_e2_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR89_e2_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR89_e2_turnover() != null) {
                        cellF.setCellValue(record.getR89_e2_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 90
                    row = sheet.getRow(89);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR90_e2_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR90_e2_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR90_e2_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR90_e2_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR90_e2_deposit() != null) {
                        cellD.setCellValue(record.getR90_e2_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR90_e2_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR90_e2_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR90_e2_turnover() != null) {
                        cellF.setCellValue(record.getR90_e2_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // E3 TABLE

                    // ROW 96
                    row = sheet.getRow(95);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR96_e3_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR96_e3_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR96_e3_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR96_e3_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR96_e3_deposit() != null) {
                        cellD.setCellValue(record.getR96_e3_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR96_e3_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR96_e3_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR96_e3_turnover() != null) {
                        cellF.setCellValue(record.getR96_e3_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 97
                    row = sheet.getRow(96);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR97_e3_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR97_e3_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR97_e3_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR97_e3_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR97_e3_deposit() != null) {
                        cellD.setCellValue(record.getR97_e3_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR97_e3_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR97_e3_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR97_e3_turnover() != null) {
                        cellF.setCellValue(record.getR97_e3_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // F TABLE

                    // ROW 104
                    row = sheet.getRow(103);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR104_f_num_of_cust() != null) {
                        cellB.setCellValue(record.getR104_f_num_of_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR104_f_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR104_f_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR104_f_deposit() != null) {
                        cellD.setCellValue(record.getR104_f_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR104_f_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR104_f_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR104_f_turnover() != null) {
                        cellF.setCellValue(record.getR104_f_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 105
                    row = sheet.getRow(104);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR105_f_num_of_cust() != null) {
                        cellB.setCellValue(record.getR105_f_num_of_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR105_f_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR105_f_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR105_f_deposit() != null) {
                        cellD.setCellValue(record.getR105_f_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR105_f_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR105_f_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR105_f_turnover() != null) {
                        cellF.setCellValue(record.getR105_f_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // G1 TABLE

                    // ROW 111
                    row = sheet.getRow(110);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR111_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR111_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR111_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR111_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 112
                    row = sheet.getRow(111);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR112_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR112_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR112_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR112_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 114
                    row = sheet.getRow(113);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR114_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR114_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR114_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR114_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 115
                    row = sheet.getRow(114);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR115_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR115_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR115_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR115_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 117
                    row = sheet.getRow(116);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR117_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR117_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR117_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR117_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 118
                    row = sheet.getRow(117);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR118_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR118_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR118_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR118_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 120
                    row = sheet.getRow(119);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR120_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR120_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR120_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR120_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 121
                    row = sheet.getRow(120);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR121_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR121_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR121_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR121_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 123
                    row = sheet.getRow(122);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR123_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR123_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR123_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR123_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 124
                    row = sheet.getRow(123);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR124_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR124_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR124_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR124_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 126
                    row = sheet.getRow(125);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR126_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR126_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR126_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR126_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 127
                    row = sheet.getRow(126);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR127_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR127_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR127_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR127_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 128
                    row = sheet.getRow(127);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR128_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR128_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR128_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR128_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // G2 TABLE

                    // ROW 135
                    row = sheet.getRow(134);

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR135_g2_val_transac() != null) {
                        cellE.setCellValue(record.getR135_g2_val_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 136
                    row = sheet.getRow(135);

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR136_g2_val_transac() != null) {
                        cellE.setCellValue(record.getR136_g2_val_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 138
                    row = sheet.getRow(137);

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR138_g2_val_transac() != null) {
                        cellE.setCellValue(record.getR138_g2_val_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 139
                    row = sheet.getRow(138);

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR139_g2_val_transac() != null) {
                        cellE.setCellValue(record.getR139_g2_val_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // H TABLE

                    // ROW 144
                    row = sheet.getRow(143);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR144_h_amount() != null) {
                        cellF.setCellValue(record.getR144_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 145
                    row = sheet.getRow(144);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR145_h_amount() != null) {
                        cellF.setCellValue(record.getR145_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 146
                    row = sheet.getRow(145);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR146_h_amount() != null) {
                        cellF.setCellValue(record.getR146_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 147
                    row = sheet.getRow(146);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR147_h_amount() != null) {
                        cellF.setCellValue(record.getR147_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 148
                    row = sheet.getRow(147);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR148_h_amount() != null) {
                        cellF.setCellValue(record.getR148_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // I TABLE

                    // ROW 153
                    row = sheet.getRow(152);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR153_i_no_cust() != null) {
                        cellC.setCellValue(record.getR153_i_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Outstanding Balance
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR153_i_outs_bal() != null) {
                        cellD.setCellValue(record.getR153_i_outs_bal().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Turnover
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR153_i_turnover() != null) {
                        cellE.setCellValue(record.getR153_i_turnover().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 154
                    row = sheet.getRow(153);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR154_i_no_cust() != null) {
                        cellC.setCellValue(record.getR154_i_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Outstanding Balance
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR154_i_outs_bal() != null) {
                        cellD.setCellValue(record.getR154_i_outs_bal().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Turnover
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR154_i_turnover() != null) {
                        cellE.setCellValue(record.getR154_i_turnover().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 155
                    row = sheet.getRow(154);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR155_i_no_cust() != null) {
                        cellC.setCellValue(record.getR155_i_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Outstanding Balance
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR155_i_outs_bal() != null) {
                        cellD.setCellValue(record.getR155_i_outs_bal().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Turnover
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR155_i_turnover() != null) {
                        cellE.setCellValue(record.getR155_i_turnover().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // J TABLE

                    // ROW 161
                    row = sheet.getRow(160);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR161_j_num_of_cust() != null) {
                        cellC.setCellValue(record.getR161_j_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Commitment at June
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR161_j_commitment_at_jun() != null) {
                        cellD.setCellValue(record.getR161_j_commitment_at_jun().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 162
                    row = sheet.getRow(161);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR162_j_num_of_cust() != null) {
                        cellC.setCellValue(record.getR162_j_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Commitment at June
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR162_j_commitment_at_jun() != null) {
                        cellD.setCellValue(record.getR162_j_commitment_at_jun().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    /// ROW 163
                    row = sheet.getRow(162);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR163_j_num_of_cust() != null) {
                        cellC.setCellValue(record.getR163_j_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Commitment at June
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR163_j_commitment_at_jun() != null) {
                        cellD.setCellValue(record.getR163_j_commitment_at_jun().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }
                    // K TABLE

                    // ROW 170
                    row = sheet.getRow(169);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR170_k_num_of_trans() != null) {
                        cellE.setCellValue(record.getR170_k_num_of_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR170_k_value_of_trans() != null) {
                        cellF.setCellValue(record.getR170_k_value_of_trans().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 171
                    row = sheet.getRow(170);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR171_k_num_of_trans() != null) {
                        cellE.setCellValue(record.getR171_k_num_of_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR171_k_value_of_trans() != null) {
                        cellF.setCellValue(record.getR171_k_value_of_trans().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 172
                    row = sheet.getRow(171);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR172_k_num_of_trans() != null) {
                        cellE.setCellValue(record.getR172_k_num_of_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR172_k_value_of_trans() != null) {
                        cellF.setCellValue(record.getR172_k_value_of_trans().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // L TABLE

                    // ROW 179
                    row = sheet.getRow(178);

                    // Column F – Number of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR179_l_num_of_transac() != null) {
                        cellF.setCellValue(record.getR179_l_num_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 180
                    row = sheet.getRow(179);

                    // Column F – Number of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR180_l_num_of_transac() != null) {
                        cellF.setCellValue(record.getR180_l_num_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 181
                    row = sheet.getRow(180);

                    // Column F – Number of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR181_l_num_of_transac() != null) {
                        cellF.setCellValue(record.getR181_l_num_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // M TABLE

                    // ROW 187
                    row = sheet.getRow(186);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR187_m_num_of_transac() != null) {
                        cellE.setCellValue(record.getR187_m_num_of_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR187_m_val_of_transac() != null) {
                        cellF.setCellValue(record.getR187_m_val_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // N TABLE

                    // ROW 192
                    row = sheet.getRow(191);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR192_n_num_of_transac() != null) {
                        cellE.setCellValue(record.getR192_n_num_of_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR192_n_val_of_transac() != null) {
                        cellF.setCellValue(record.getR192_n_val_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // O TABLE

                    // ROW 196
                    row = sheet.getRow(195);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR196_o_num_of_transac() != null) {
                        cellE.setCellValue(record.getR196_o_num_of_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR196_o_val_of_transac() != null) {
                        cellF.setCellValue(record.getR196_o_val_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 201
                    row = sheet.getRow(200);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR201_p_num_of_transac() != null) {
                        cellE.setCellValue(record.getR201_p_num_of_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR201_p_val_of_transac() != null) {
                        cellF.setCellValue(record.getR201_p_val_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
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
												auditService.createBusinessAudit(userid, "DOWNLOAD", "AML  SUMMARY", null, "BRRS_AML_SUMMARYTABLE");
											}

				return out.toByteArray();
			}

		}




//=====================================================
//ARCHIVAL SUMMARY EXCEL 
//=====================================================



				public byte[] getExcelAMLARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {	

			}

			List<AML_Archival_Summary_Entity> dataList = 
					getDataByDateListArchival(dateformat.parse(todate), version);
		
	    

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for AML new report. Returning empty result.");
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
						AML_Archival_Summary_Entity record = dataList.get(i);
					    
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


	     // A TABLE

                    Cell cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR11_cust_base_no_of_acct() != null) {
                        cellC.setCellValue(record.getR11_cust_base_no_of_acct().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    Cell cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(2);
                    if (record.getR11_cust_base_tot_dep() != null) {
                        cellD.setCellValue(record.getR11_cust_base_tot_dep().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 12
                    row = sheet.getRow(11);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR12_cust_base_no_of_acct() != null) {
                        cellC.setCellValue(record.getR12_cust_base_no_of_acct().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR12_cust_base_tot_dep() != null) {
                        cellD.setCellValue(record.getR12_cust_base_tot_dep().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 13
                    row = sheet.getRow(12);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR13_cust_base_no_of_acct() != null) {
                        cellC.setCellValue(record.getR13_cust_base_no_of_acct().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR13_cust_base_tot_dep() != null) {
                        cellD.setCellValue(record.getR13_cust_base_tot_dep().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 14
                    row = sheet.getRow(13);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR14_cust_base_no_of_acct() != null) {
                        cellC.setCellValue(record.getR14_cust_base_no_of_acct().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR14_cust_base_tot_dep() != null) {
                        cellD.setCellValue(record.getR14_cust_base_tot_dep().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // B1 TABLE

                    // ROW 21
                    row = sheet.getRow(20);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR21_cust_risk_pro_num_of_cust() != null) {
                        cellC.setCellValue(record.getR21_cust_risk_pro_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR21_cust_risk_pro_value() != null) {
                        cellD.setCellValue(record.getR21_cust_risk_pro_value().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 22
                    row = sheet.getRow(21);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR22_cust_risk_pro_num_of_cust() != null) {
                        cellC.setCellValue(record.getR22_cust_risk_pro_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR22_cust_risk_pro_value() != null) {
                        cellD.setCellValue(record.getR22_cust_risk_pro_value().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 23
                    row = sheet.getRow(22);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR23_cust_risk_pro_num_of_cust() != null) {
                        cellC.setCellValue(record.getR23_cust_risk_pro_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR23_cust_risk_pro_value() != null) {
                        cellD.setCellValue(record.getR23_cust_risk_pro_value().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // B2 TABLE

                    // ROW 30
                    row = sheet.getRow(29);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR30_b2_low_risk_no_cust() != null) {
                        cellC.setCellValue(record.getR30_b2_low_risk_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR30_b2_low_risk_deposit() != null) {
                        cellD.setCellValue(record.getR30_b2_low_risk_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    Cell cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR30_b2_medi_risk_no_cust() != null) {
                        cellE.setCellValue(record.getR30_b2_medi_risk_no_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    Cell cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR30_b2_medi_risk_deposit() != null) {
                        cellF.setCellValue(record.getR30_b2_medi_risk_deposit().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    Cell cellG = row.getCell(6);
                    if (cellG == null)
                        cellG = row.createCell(6);
                    if (record.getR30_b2_high_risk_no_cust() != null) {
                        cellG.setCellValue(record.getR30_b2_high_risk_no_cust().doubleValue());
                    } else {
                        cellG.setCellValue(0);
                    }

                    Cell cellH = row.getCell(7);
                    if (cellH == null)
                        cellH = row.createCell(7);
                    if (record.getR30_b2_high_risk_deposit() != null) {
                        cellH.setCellValue(record.getR30_b2_high_risk_deposit().doubleValue());
                    } else {
                        cellH.setCellValue(0);
                    }

                    // ROW 31
                    row = sheet.getRow(30);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR31_b2_low_risk_no_cust() != null) {
                        cellC.setCellValue(record.getR31_b2_low_risk_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR31_b2_low_risk_deposit() != null) {
                        cellD.setCellValue(record.getR31_b2_low_risk_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR31_b2_medi_risk_no_cust() != null) {
                        cellE.setCellValue(record.getR31_b2_medi_risk_no_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR31_b2_medi_risk_deposit() != null) {
                        cellF.setCellValue(record.getR31_b2_medi_risk_deposit().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    cellG = row.getCell(6);
                    if (cellG == null)
                        cellG = row.createCell(6);
                    if (record.getR31_b2_high_risk_no_cust() != null) {
                        cellG.setCellValue(record.getR31_b2_high_risk_no_cust().doubleValue());
                    } else {
                        cellG.setCellValue(0);
                    }

                    cellH = row.getCell(7);
                    if (cellH == null)
                        cellH = row.createCell(7);
                    if (record.getR31_b2_high_risk_deposit() != null) {
                        cellH.setCellValue(record.getR31_b2_high_risk_deposit().doubleValue());
                    } else {
                        cellH.setCellValue(0);
                    }

                    // ROW 32
                    row = sheet.getRow(31);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR32_b2_low_risk_no_cust() != null) {
                        cellC.setCellValue(record.getR32_b2_low_risk_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR32_b2_low_risk_deposit() != null) {
                        cellD.setCellValue(record.getR32_b2_low_risk_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // C TABLE 39 AND 40

                    // ROW 39
                    row = sheet.getRow(38);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR39_cust_base_no_cust() != null) {
                        cellC.setCellValue(record.getR39_cust_base_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR39_cust_base_deposits() != null) {
                        cellD.setCellValue(record.getR39_cust_base_deposits().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 40
                    row = sheet.getRow(39);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR40_cust_base_no_cust() != null) {
                        cellC.setCellValue(record.getR40_cust_base_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR40_cust_base_deposits() != null) {
                        cellD.setCellValue(record.getR40_cust_base_deposits().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // D TABLE 39 AND 40 D AND E COLUMN

                    // ROW 51
                    row = sheet.getRow(50);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR51_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR51_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR51_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR51_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 52
                    row = sheet.getRow(51);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR52_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR52_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR52_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR52_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 53
                    row = sheet.getRow(52);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR53_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR53_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR53_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR53_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 54
                    row = sheet.getRow(53);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR54_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR54_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR54_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR54_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 57
                    row = sheet.getRow(56);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR57_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR57_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR57_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR57_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 58
                    row = sheet.getRow(57);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR58_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR58_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR58_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR58_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 59
                    row = sheet.getRow(58);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR59_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR59_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR59_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR59_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 60
                    row = sheet.getRow(59);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR60_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR60_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR60_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR60_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 61
                    row = sheet.getRow(60);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR61_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR61_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR61_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR61_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 62
                    row = sheet.getRow(61);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR62_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR62_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR62_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR62_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 63
                    row = sheet.getRow(62);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR63_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR63_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR63_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR63_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 64
                    row = sheet.getRow(63);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR64_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR64_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR64_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR64_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 65
                    row = sheet.getRow(64);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR65_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR65_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR65_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR65_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 66
                    row = sheet.getRow(65);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR66_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR66_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR66_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR66_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 67
                    row = sheet.getRow(66);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR67_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR67_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR67_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR67_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 68
                    row = sheet.getRow(67);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR68_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR68_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR68_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR68_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 69
                    row = sheet.getRow(68);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR69_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR69_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR69_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR69_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 72
                    row = sheet.getRow(71);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR72_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR72_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR72_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR72_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 73
                    row = sheet.getRow(72);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR73_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR73_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR73_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR73_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 74
                    row = sheet.getRow(73);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR74_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR74_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR74_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR74_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // E1 TABLE

                    // ROW 82
                    row = sheet.getRow(81);

                    Cell cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR82_e1_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR82_e1_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(1);
                    if (record.getR82_e1_loan_on_bal_expo() != null) {
                        cellC.setCellValue(record.getR82_e1_loan_on_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR82_e1_deposit() != null) {
                        cellD.setCellValue(record.getR82_e1_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR82_e1_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR82_e1_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR82_e1_turnover() != null) {
                        cellF.setCellValue(record.getR82_e1_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 83
                    row = sheet.getRow(82);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR83_e1_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR83_e1_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR83_e1_loan_on_bal_expo() != null) {
                        cellC.setCellValue(record.getR83_e1_loan_on_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR83_e1_deposit() != null) {
                        cellD.setCellValue(record.getR83_e1_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR83_e1_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR83_e1_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR83_e1_turnover() != null) {
                        cellF.setCellValue(record.getR83_e1_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // E2 TABLE

                    // ROW 89
                    row = sheet.getRow(88);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR89_e2_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR89_e2_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR89_e2_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR89_e2_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR89_e2_deposit() != null) {
                        cellD.setCellValue(record.getR89_e2_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR89_e2_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR89_e2_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR89_e2_turnover() != null) {
                        cellF.setCellValue(record.getR89_e2_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 90
                    row = sheet.getRow(89);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR90_e2_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR90_e2_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR90_e2_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR90_e2_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR90_e2_deposit() != null) {
                        cellD.setCellValue(record.getR90_e2_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR90_e2_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR90_e2_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR90_e2_turnover() != null) {
                        cellF.setCellValue(record.getR90_e2_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // E3 TABLE

                    // ROW 96
                    row = sheet.getRow(95);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR96_e3_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR96_e3_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR96_e3_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR96_e3_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR96_e3_deposit() != null) {
                        cellD.setCellValue(record.getR96_e3_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR96_e3_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR96_e3_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR96_e3_turnover() != null) {
                        cellF.setCellValue(record.getR96_e3_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 97
                    row = sheet.getRow(96);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR97_e3_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR97_e3_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR97_e3_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR97_e3_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR97_e3_deposit() != null) {
                        cellD.setCellValue(record.getR97_e3_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR97_e3_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR97_e3_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR97_e3_turnover() != null) {
                        cellF.setCellValue(record.getR97_e3_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // F TABLE

                    // ROW 104
                    row = sheet.getRow(103);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR104_f_num_of_cust() != null) {
                        cellB.setCellValue(record.getR104_f_num_of_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR104_f_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR104_f_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR104_f_deposit() != null) {
                        cellD.setCellValue(record.getR104_f_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR104_f_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR104_f_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR104_f_turnover() != null) {
                        cellF.setCellValue(record.getR104_f_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 105
                    row = sheet.getRow(104);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR105_f_num_of_cust() != null) {
                        cellB.setCellValue(record.getR105_f_num_of_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR105_f_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR105_f_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR105_f_deposit() != null) {
                        cellD.setCellValue(record.getR105_f_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR105_f_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR105_f_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR105_f_turnover() != null) {
                        cellF.setCellValue(record.getR105_f_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // G1 TABLE

                    // ROW 111
                    row = sheet.getRow(110);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR111_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR111_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR111_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR111_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 112
                    row = sheet.getRow(111);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR112_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR112_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR112_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR112_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 114
                    row = sheet.getRow(113);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR114_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR114_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR114_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR114_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 115
                    row = sheet.getRow(114);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR115_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR115_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR115_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR115_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 117
                    row = sheet.getRow(116);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR117_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR117_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR117_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR117_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 118
                    row = sheet.getRow(117);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR118_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR118_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR118_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR118_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 120
                    row = sheet.getRow(119);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR120_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR120_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR120_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR120_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 121
                    row = sheet.getRow(120);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR121_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR121_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR121_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR121_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 123
                    row = sheet.getRow(122);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR123_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR123_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR123_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR123_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 124
                    row = sheet.getRow(123);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR124_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR124_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR124_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR124_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 126
                    row = sheet.getRow(125);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR126_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR126_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR126_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR126_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 127
                    row = sheet.getRow(126);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR127_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR127_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR127_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR127_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 128
                    row = sheet.getRow(127);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR128_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR128_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR128_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR128_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // G2 TABLE

                    // ROW 135
                    row = sheet.getRow(134);

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR135_g2_val_transac() != null) {
                        cellE.setCellValue(record.getR135_g2_val_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 136
                    row = sheet.getRow(135);

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR136_g2_val_transac() != null) {
                        cellE.setCellValue(record.getR136_g2_val_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 138
                    row = sheet.getRow(137);

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR138_g2_val_transac() != null) {
                        cellE.setCellValue(record.getR138_g2_val_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 139
                    row = sheet.getRow(138);

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR139_g2_val_transac() != null) {
                        cellE.setCellValue(record.getR139_g2_val_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // H TABLE

                    // ROW 144
                    row = sheet.getRow(143);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR144_h_amount() != null) {
                        cellF.setCellValue(record.getR144_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 145
                    row = sheet.getRow(144);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR145_h_amount() != null) {
                        cellF.setCellValue(record.getR145_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 146
                    row = sheet.getRow(145);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR146_h_amount() != null) {
                        cellF.setCellValue(record.getR146_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 147
                    row = sheet.getRow(146);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR147_h_amount() != null) {
                        cellF.setCellValue(record.getR147_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 148
                    row = sheet.getRow(147);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR148_h_amount() != null) {
                        cellF.setCellValue(record.getR148_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // I TABLE

                    // ROW 153
                    row = sheet.getRow(152);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR153_i_no_cust() != null) {
                        cellC.setCellValue(record.getR153_i_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Outstanding Balance
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR153_i_outs_bal() != null) {
                        cellD.setCellValue(record.getR153_i_outs_bal().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Turnover
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR153_i_turnover() != null) {
                        cellE.setCellValue(record.getR153_i_turnover().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 154
                    row = sheet.getRow(153);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR154_i_no_cust() != null) {
                        cellC.setCellValue(record.getR154_i_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Outstanding Balance
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR154_i_outs_bal() != null) {
                        cellD.setCellValue(record.getR154_i_outs_bal().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Turnover
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR154_i_turnover() != null) {
                        cellE.setCellValue(record.getR154_i_turnover().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 155
                    row = sheet.getRow(154);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR155_i_no_cust() != null) {
                        cellC.setCellValue(record.getR155_i_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Outstanding Balance
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR155_i_outs_bal() != null) {
                        cellD.setCellValue(record.getR155_i_outs_bal().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Turnover
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR155_i_turnover() != null) {
                        cellE.setCellValue(record.getR155_i_turnover().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // J TABLE

                    // ROW 161
                    row = sheet.getRow(160);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR161_j_num_of_cust() != null) {
                        cellC.setCellValue(record.getR161_j_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Commitment at June
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR161_j_commitment_at_jun() != null) {
                        cellD.setCellValue(record.getR161_j_commitment_at_jun().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 162
                    row = sheet.getRow(161);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR162_j_num_of_cust() != null) {
                        cellC.setCellValue(record.getR162_j_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Commitment at June
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR162_j_commitment_at_jun() != null) {
                        cellD.setCellValue(record.getR162_j_commitment_at_jun().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    /// ROW 163
                    row = sheet.getRow(162);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR163_j_num_of_cust() != null) {
                        cellC.setCellValue(record.getR163_j_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Commitment at June
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR163_j_commitment_at_jun() != null) {
                        cellD.setCellValue(record.getR163_j_commitment_at_jun().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }
                    // K TABLE

                    // ROW 170
                    row = sheet.getRow(169);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR170_k_num_of_trans() != null) {
                        cellE.setCellValue(record.getR170_k_num_of_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR170_k_value_of_trans() != null) {
                        cellF.setCellValue(record.getR170_k_value_of_trans().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 171
                    row = sheet.getRow(170);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR171_k_num_of_trans() != null) {
                        cellE.setCellValue(record.getR171_k_num_of_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR171_k_value_of_trans() != null) {
                        cellF.setCellValue(record.getR171_k_value_of_trans().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 172
                    row = sheet.getRow(171);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR172_k_num_of_trans() != null) {
                        cellE.setCellValue(record.getR172_k_num_of_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR172_k_value_of_trans() != null) {
                        cellF.setCellValue(record.getR172_k_value_of_trans().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // L TABLE

                    // ROW 179
                    row = sheet.getRow(178);

                    // Column F – Number of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR179_l_num_of_transac() != null) {
                        cellF.setCellValue(record.getR179_l_num_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 180
                    row = sheet.getRow(179);

                    // Column F – Number of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR180_l_num_of_transac() != null) {
                        cellF.setCellValue(record.getR180_l_num_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 181
                    row = sheet.getRow(180);

                    // Column F – Number of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR181_l_num_of_transac() != null) {
                        cellF.setCellValue(record.getR181_l_num_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // M TABLE

                    // ROW 187
                    row = sheet.getRow(186);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR187_m_num_of_transac() != null) {
                        cellE.setCellValue(record.getR187_m_num_of_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR187_m_val_of_transac() != null) {
                        cellF.setCellValue(record.getR187_m_val_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // N TABLE

                    // ROW 192
                    row = sheet.getRow(191);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR192_n_num_of_transac() != null) {
                        cellE.setCellValue(record.getR192_n_num_of_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR192_n_val_of_transac() != null) {
                        cellF.setCellValue(record.getR192_n_val_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // O TABLE

                    // ROW 196
                    row = sheet.getRow(195);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR196_o_num_of_transac() != null) {
                        cellE.setCellValue(record.getR196_o_num_of_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR196_o_val_of_transac() != null) {
                        cellF.setCellValue(record.getR196_o_val_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 201
                    row = sheet.getRow(200);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR201_p_num_of_transac() != null) {
                        cellE.setCellValue(record.getR201_p_num_of_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR201_p_val_of_transac() != null) {
                        cellF.setCellValue(record.getR201_p_val_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
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
			auditService.createBusinessAudit(userid, "DOWNLOAD", "AML ARCHIVAL SUMMARY", null, "BRRS_AML_ARCHIVALTABLE_SUMMARY");
		}

				return out.toByteArray();
			}

		}
		
		
		
	}