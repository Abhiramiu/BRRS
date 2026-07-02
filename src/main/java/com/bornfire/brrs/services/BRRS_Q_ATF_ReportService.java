//=======================================================================================Q_ATF
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
import java.text.ParseException;
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
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;
import com.bornfire.brrs.services.BRRS_Common_Disclosure_ReportService.CommonDisclosureDetailRowMapper;
import com.bornfire.brrs.services.BRRS_Common_Disclosure_ReportService.Common_Disclosure_Detail_Entity;


@Service

public class BRRS_Q_ATF_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_ATF_ReportService.class);
	
	
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


	public List<Q_ATF_Summary_Entity> getDataByDateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_Q_ATF_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new Q_ATF_Summary_RowMapper()
    );
}
	
// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================



public List<Object[]> getQ_ATF_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_Q_ATF_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<Q_ATF_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_Q_ATF_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new Q_ATF_Archival_Summary_RowMapper()
    );
}

public List<Q_ATF_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_Q_ATF_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new Q_ATF_Archival_Summary_RowMapper()
    );
}

// =====================================================
// DETAIL REPO
// =====================================================	


public List<Q_ATF_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_Q_ATF_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new Q_ATF_Detail_RowMapper()
    );
}

public List<Q_ATF_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_Q_ATF_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, offset, limit},
            new Q_ATF_Detail_RowMapper()
    );
}

public int getDataCount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_Q_ATF_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(sql, new Object[]{reportDate}, Integer.class);
}

public List<Q_ATF_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel, String reportAddlCriteria1, Date reportDate) {

    String sql = "SELECT * FROM BRRS_Q_ATF_DETAILTABLE " +
                 "WHERE REPORT_LABEL = ? " +
                 "AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate},
            new Q_ATF_Detail_RowMapper()
    );
}

public Q_ATF_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_Q_ATF_DETAILTABLE WHERE ACCT_NUMBER = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{acctNumber},
            new Q_ATF_Detail_RowMapper()
    );
}


// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================

public List<Q_ATF_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate, String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_Q_ATF_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, dataEntryVersion},
            new Q_ATF_Archival_Detail_RowMapper()
    );
}


public List<Q_ATF_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_Q_ATF_ARCHIVALTABLE_DETAIL " +
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
            new Q_ATF_Archival_Detail_RowMapper()
    );
}
//=====================================================
//resub 
//=====================================================

public Q_ATF_Detail_Entity findBySno(String sno) {

	String sql = "SELECT * FROM BRRS_Q_ATF_DETAILTABLE WHERE SNO = ?";

	return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new Q_ATF_Detail_RowMapper());
}

public Q_ATF_Detail_Entity findBySnoArch(String sno) {

	String sql = "SELECT * FROM BRRS_Q_ATF_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

	return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new Q_ATF_Detail_RowMapper());
}

public String getishighestversion(Date REPORT_DATE,
		BigDecimal REPORT_VERSION) {
	String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest " +
             "FROM BRRS_Q_ATF_ARCHIVALTABLE_SUMMARY " +
             "WHERE REPORT_DATE = ?";
	return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

}
// =====================================================
// SUMAMRY ENTITY 
// =====================================================


public class Q_ATF_Summary_RowMapper implements RowMapper<Q_ATF_Summary_Entity> {

    @Override
    public Q_ATF_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        Q_ATF_Summary_Entity obj = new Q_ATF_Summary_Entity();

// =========================
// R11 VALUES
// =========================
obj.setR11_num_by_inst_sec(rs.getString("r11_num_by_inst_sec"));
obj.setR11_num_depo(rs.getBigDecimal("r11_num_depo"));
obj.setR11_num_depo_acc(rs.getBigDecimal("r11_num_depo_acc"));
obj.setR11_num_borrowers(rs.getBigDecimal("r11_num_borrowers"));
obj.setR11_num_loan_acc(rs.getBigDecimal("r11_num_loan_acc"));
        
// =========================
// R12 VALUES
// =========================
obj.setR12_num_by_inst_sec(rs.getString("r12_num_by_inst_sec"));
obj.setR12_num_depo(rs.getBigDecimal("r12_num_depo"));
obj.setR12_num_depo_acc(rs.getBigDecimal("r12_num_depo_acc"));
obj.setR12_num_borrowers(rs.getBigDecimal("r12_num_borrowers"));
obj.setR12_num_loan_acc(rs.getBigDecimal("r12_num_loan_acc"));

// =========================
// R13 VALUES
// =========================
obj.setR13_num_by_inst_sec(rs.getString("r13_num_by_inst_sec"));
obj.setR13_num_depo(rs.getBigDecimal("r13_num_depo"));
obj.setR13_num_depo_acc(rs.getBigDecimal("r13_num_depo_acc"));
obj.setR13_num_borrowers(rs.getBigDecimal("r13_num_borrowers"));
obj.setR13_num_loan_acc(rs.getBigDecimal("r13_num_loan_acc"));

// =========================
// R14 VALUES
// =========================
obj.setR14_num_by_inst_sec(rs.getString("r14_num_by_inst_sec"));
obj.setR14_num_depo(rs.getBigDecimal("r14_num_depo"));
obj.setR14_num_depo_acc(rs.getBigDecimal("r14_num_depo_acc"));
obj.setR14_num_borrowers(rs.getBigDecimal("r14_num_borrowers"));
obj.setR14_num_loan_acc(rs.getBigDecimal("r14_num_loan_acc"));

// =========================
// R15 VALUES
// =========================
obj.setR15_num_by_inst_sec(rs.getString("r15_num_by_inst_sec"));
obj.setR15_num_depo(rs.getBigDecimal("r15_num_depo"));
obj.setR15_num_depo_acc(rs.getBigDecimal("r15_num_depo_acc"));
obj.setR15_num_borrowers(rs.getBigDecimal("r15_num_borrowers"));
obj.setR15_num_loan_acc(rs.getBigDecimal("r15_num_loan_acc"));

// =========================
// R16 VALUES
// =========================
obj.setR16_num_by_inst_sec(rs.getString("r16_num_by_inst_sec"));
obj.setR16_num_depo(rs.getBigDecimal("r16_num_depo"));
obj.setR16_num_depo_acc(rs.getBigDecimal("r16_num_depo_acc"));
obj.setR16_num_borrowers(rs.getBigDecimal("r16_num_borrowers"));
obj.setR16_num_loan_acc(rs.getBigDecimal("r16_num_loan_acc"));

// =========================
// R17 VALUES
// =========================
obj.setR17_num_by_inst_sec(rs.getString("r17_num_by_inst_sec"));
obj.setR17_num_depo(rs.getBigDecimal("r17_num_depo"));
obj.setR17_num_depo_acc(rs.getBigDecimal("r17_num_depo_acc"));
obj.setR17_num_borrowers(rs.getBigDecimal("r17_num_borrowers"));
obj.setR17_num_loan_acc(rs.getBigDecimal("r17_num_loan_acc"));

// =========================
// R18 VALUES
// =========================
obj.setR18_num_by_inst_sec(rs.getString("r18_num_by_inst_sec"));
obj.setR18_num_depo(rs.getBigDecimal("r18_num_depo"));
obj.setR18_num_depo_acc(rs.getBigDecimal("r18_num_depo_acc"));
obj.setR18_num_borrowers(rs.getBigDecimal("r18_num_borrowers"));
obj.setR18_num_loan_acc(rs.getBigDecimal("r18_num_loan_acc"));

// =========================
// R19 VALUES
// =========================
obj.setR19_num_by_inst_sec(rs.getString("r19_num_by_inst_sec"));
obj.setR19_num_depo(rs.getBigDecimal("r19_num_depo"));
obj.setR19_num_depo_acc(rs.getBigDecimal("r19_num_depo_acc"));
obj.setR19_num_borrowers(rs.getBigDecimal("r19_num_borrowers"));
obj.setR19_num_loan_acc(rs.getBigDecimal("r19_num_loan_acc"));

// =========================
// R20 VALUES
// =========================
obj.setR20_num_by_inst_sec(rs.getString("r20_num_by_inst_sec"));
obj.setR20_num_depo(rs.getBigDecimal("r20_num_depo"));
obj.setR20_num_depo_acc(rs.getBigDecimal("r20_num_depo_acc"));
obj.setR20_num_borrowers(rs.getBigDecimal("r20_num_borrowers"));
obj.setR20_num_loan_acc(rs.getBigDecimal("r20_num_loan_acc"));

// =========================
// R21 VALUES
// =========================
obj.setR21_num_by_inst_sec(rs.getString("r21_num_by_inst_sec"));
obj.setR21_num_depo(rs.getBigDecimal("r21_num_depo"));
obj.setR21_num_depo_acc(rs.getBigDecimal("r21_num_depo_acc"));
obj.setR21_num_borrowers(rs.getBigDecimal("r21_num_borrowers"));
obj.setR21_num_loan_acc(rs.getBigDecimal("r21_num_loan_acc"));

// =========================
// R22 VALUES
// =========================
obj.setR22_num_by_inst_sec(rs.getString("r22_num_by_inst_sec"));
obj.setR22_num_depo(rs.getBigDecimal("r22_num_depo"));
obj.setR22_num_depo_acc(rs.getBigDecimal("r22_num_depo_acc"));
obj.setR22_num_borrowers(rs.getBigDecimal("r22_num_borrowers"));
obj.setR22_num_loan_acc(rs.getBigDecimal("r22_num_loan_acc"));

// =========================
// R23 VALUES
// =========================
obj.setR23_num_by_inst_sec(rs.getString("r23_num_by_inst_sec"));
obj.setR23_num_depo(rs.getBigDecimal("r23_num_depo"));
obj.setR23_num_depo_acc(rs.getBigDecimal("r23_num_depo_acc"));
obj.setR23_num_borrowers(rs.getBigDecimal("r23_num_borrowers"));
obj.setR23_num_loan_acc(rs.getBigDecimal("r23_num_loan_acc"));

// =========================
// R24 VALUES
// =========================
obj.setR24_num_by_inst_sec(rs.getString("r24_num_by_inst_sec"));
obj.setR24_num_depo(rs.getBigDecimal("r24_num_depo"));
obj.setR24_num_depo_acc(rs.getBigDecimal("r24_num_depo_acc"));
obj.setR24_num_borrowers(rs.getBigDecimal("r24_num_borrowers"));
obj.setR24_num_loan_acc(rs.getBigDecimal("r24_num_loan_acc"));

// =========================
// R25 VALUES
// =========================
obj.setR25_num_by_inst_sec(rs.getString("r25_num_by_inst_sec"));
obj.setR25_num_depo(rs.getBigDecimal("r25_num_depo"));
obj.setR25_num_depo_acc(rs.getBigDecimal("r25_num_depo_acc"));
obj.setR25_num_borrowers(rs.getBigDecimal("r25_num_borrowers"));
obj.setR25_num_loan_acc(rs.getBigDecimal("r25_num_loan_acc"));

// =========================
// R26 VALUES
// =========================
obj.setR26_num_by_inst_sec(rs.getString("r26_num_by_inst_sec"));
obj.setR26_num_depo(rs.getBigDecimal("r26_num_depo"));
obj.setR26_num_depo_acc(rs.getBigDecimal("r26_num_depo_acc"));
obj.setR26_num_borrowers(rs.getBigDecimal("r26_num_borrowers"));
obj.setR26_num_loan_acc(rs.getBigDecimal("r26_num_loan_acc"));

// =========================
// R27 VALUES
// =========================
obj.setR27_num_by_inst_sec(rs.getString("r27_num_by_inst_sec"));
obj.setR27_num_depo(rs.getBigDecimal("r27_num_depo"));
obj.setR27_num_depo_acc(rs.getBigDecimal("r27_num_depo_acc"));
obj.setR27_num_borrowers(rs.getBigDecimal("r27_num_borrowers"));
obj.setR27_num_loan_acc(rs.getBigDecimal("r27_num_loan_acc"));

// =========================
// R28 VALUES
// =========================
obj.setR28_num_by_inst_sec(rs.getString("r28_num_by_inst_sec"));
obj.setR28_num_depo(rs.getBigDecimal("r28_num_depo"));
obj.setR28_num_depo_acc(rs.getBigDecimal("r28_num_depo_acc"));
obj.setR28_num_borrowers(rs.getBigDecimal("r28_num_borrowers"));
obj.setR28_num_loan_acc(rs.getBigDecimal("r28_num_loan_acc"));

// =========================
// R29 VALUES
// =========================
obj.setR29_num_by_inst_sec(rs.getString("r29_num_by_inst_sec"));
obj.setR29_num_depo(rs.getBigDecimal("r29_num_depo"));
obj.setR29_num_depo_acc(rs.getBigDecimal("r29_num_depo_acc"));
obj.setR29_num_borrowers(rs.getBigDecimal("r29_num_borrowers"));
obj.setR29_num_loan_acc(rs.getBigDecimal("r29_num_loan_acc"));

// =========================
// R30 VALUES
// =========================
obj.setR30_num_by_inst_sec(rs.getString("r30_num_by_inst_sec"));
obj.setR30_num_depo(rs.getBigDecimal("r30_num_depo"));
obj.setR30_num_depo_acc(rs.getBigDecimal("r30_num_depo_acc"));
obj.setR30_num_borrowers(rs.getBigDecimal("r30_num_borrowers"));
obj.setR30_num_loan_acc(rs.getBigDecimal("r30_num_loan_acc"));

// =========================
// R31 VALUES
// =========================
obj.setR31_num_by_inst_sec(rs.getString("r31_num_by_inst_sec"));
obj.setR31_num_depo(rs.getBigDecimal("r31_num_depo"));
obj.setR31_num_depo_acc(rs.getBigDecimal("r31_num_depo_acc"));
obj.setR31_num_borrowers(rs.getBigDecimal("r31_num_borrowers"));
obj.setR31_num_loan_acc(rs.getBigDecimal("r31_num_loan_acc"));

// =========================
// R32 VALUES
// =========================
obj.setR32_num_by_inst_sec(rs.getString("r32_num_by_inst_sec"));
obj.setR32_num_depo(rs.getBigDecimal("r32_num_depo"));
obj.setR32_num_depo_acc(rs.getBigDecimal("r32_num_depo_acc"));
obj.setR32_num_borrowers(rs.getBigDecimal("r32_num_borrowers"));
obj.setR32_num_loan_acc(rs.getBigDecimal("r32_num_loan_acc"));

// =========================
// R33 VALUES
// =========================
obj.setR33_num_by_inst_sec(rs.getString("r33_num_by_inst_sec"));
obj.setR33_num_depo(rs.getBigDecimal("r33_num_depo"));
obj.setR33_num_depo_acc(rs.getBigDecimal("r33_num_depo_acc"));
obj.setR33_num_borrowers(rs.getBigDecimal("r33_num_borrowers"));
obj.setR33_num_loan_acc(rs.getBigDecimal("r33_num_loan_acc"));

// =========================
// R34 VALUES
// =========================
obj.setR34_num_by_inst_sec(rs.getString("r34_num_by_inst_sec"));
obj.setR34_num_depo(rs.getBigDecimal("r34_num_depo"));
obj.setR34_num_depo_acc(rs.getBigDecimal("r34_num_depo_acc"));
obj.setR34_num_borrowers(rs.getBigDecimal("r34_num_borrowers"));
obj.setR34_num_loan_acc(rs.getBigDecimal("r34_num_loan_acc"));

// =========================
// R35 VALUES
// =========================
obj.setR35_num_by_inst_sec(rs.getString("r35_num_by_inst_sec"));
obj.setR35_num_depo(rs.getBigDecimal("r35_num_depo"));
obj.setR35_num_depo_acc(rs.getBigDecimal("r35_num_depo_acc"));
obj.setR35_num_borrowers(rs.getBigDecimal("r35_num_borrowers"));
obj.setR35_num_loan_acc(rs.getBigDecimal("r35_num_loan_acc"));

// =========================
// R36 VALUES
// =========================
obj.setR36_num_by_inst_sec(rs.getString("r36_num_by_inst_sec"));
obj.setR36_num_depo(rs.getBigDecimal("r36_num_depo"));
obj.setR36_num_depo_acc(rs.getBigDecimal("r36_num_depo_acc"));
obj.setR36_num_borrowers(rs.getBigDecimal("r36_num_borrowers"));
obj.setR36_num_loan_acc(rs.getBigDecimal("r36_num_loan_acc"));

// =========================
// R37 VALUES
// =========================
obj.setR37_num_by_inst_sec(rs.getString("r37_num_by_inst_sec"));
obj.setR37_num_depo(rs.getBigDecimal("r37_num_depo"));
obj.setR37_num_depo_acc(rs.getBigDecimal("r37_num_depo_acc"));
obj.setR37_num_borrowers(rs.getBigDecimal("r37_num_borrowers"));
obj.setR37_num_loan_acc(rs.getBigDecimal("r37_num_loan_acc"));

// =========================
// R38 VALUES
// =========================
obj.setR38_num_by_inst_sec(rs.getString("r38_num_by_inst_sec"));
obj.setR38_num_depo(rs.getBigDecimal("r38_num_depo"));
obj.setR38_num_depo_acc(rs.getBigDecimal("r38_num_depo_acc"));
obj.setR38_num_borrowers(rs.getBigDecimal("r38_num_borrowers"));
obj.setR38_num_loan_acc(rs.getBigDecimal("r38_num_loan_acc"));

// =========================
// R39 VALUES
// =========================
obj.setR39_num_by_inst_sec(rs.getString("r39_num_by_inst_sec"));
obj.setR39_num_depo(rs.getBigDecimal("r39_num_depo"));
obj.setR39_num_depo_acc(rs.getBigDecimal("r39_num_depo_acc"));
obj.setR39_num_borrowers(rs.getBigDecimal("r39_num_borrowers"));
obj.setR39_num_loan_acc(rs.getBigDecimal("r39_num_loan_acc"));

// =========================
// R40 VALUES
// =========================
obj.setR40_num_by_inst_sec(rs.getString("r40_num_by_inst_sec"));
obj.setR40_num_depo(rs.getBigDecimal("r40_num_depo"));
obj.setR40_num_depo_acc(rs.getBigDecimal("r40_num_depo_acc"));
obj.setR40_num_borrowers(rs.getBigDecimal("r40_num_borrowers"));
obj.setR40_num_loan_acc(rs.getBigDecimal("r40_num_loan_acc"));

// =========================
// R41 VALUES
// =========================
obj.setR41_num_by_inst_sec(rs.getString("r41_num_by_inst_sec"));
obj.setR41_num_depo(rs.getBigDecimal("r41_num_depo"));
obj.setR41_num_depo_acc(rs.getBigDecimal("r41_num_depo_acc"));
obj.setR41_num_borrowers(rs.getBigDecimal("r41_num_borrowers"));
obj.setR41_num_loan_acc(rs.getBigDecimal("r41_num_loan_acc"));

// =========================
// R42 VALUES
// =========================
obj.setR42_num_by_inst_sec(rs.getString("r42_num_by_inst_sec"));
obj.setR42_num_depo(rs.getBigDecimal("r42_num_depo"));
obj.setR42_num_depo_acc(rs.getBigDecimal("r42_num_depo_acc"));
obj.setR42_num_borrowers(rs.getBigDecimal("r42_num_borrowers"));
obj.setR42_num_loan_acc(rs.getBigDecimal("r42_num_loan_acc"));

// =========================
// R43 VALUES
// =========================
obj.setR43_num_by_inst_sec(rs.getString("r43_num_by_inst_sec"));
obj.setR43_num_depo(rs.getBigDecimal("r43_num_depo"));
obj.setR43_num_depo_acc(rs.getBigDecimal("r43_num_depo_acc"));
obj.setR43_num_borrowers(rs.getBigDecimal("r43_num_borrowers"));
obj.setR43_num_loan_acc(rs.getBigDecimal("r43_num_loan_acc"));

// =========================
// R44 VALUES
// =========================
obj.setR44_num_by_inst_sec(rs.getString("r44_num_by_inst_sec"));
obj.setR44_num_depo(rs.getBigDecimal("r44_num_depo"));
obj.setR44_num_depo_acc(rs.getBigDecimal("r44_num_depo_acc"));
obj.setR44_num_borrowers(rs.getBigDecimal("r44_num_borrowers"));
obj.setR44_num_loan_acc(rs.getBigDecimal("r44_num_loan_acc"));

// =========================
// R45 VALUES
// =========================
obj.setR45_num_by_inst_sec(rs.getString("r45_num_by_inst_sec"));
obj.setR45_num_depo(rs.getBigDecimal("r45_num_depo"));
obj.setR45_num_depo_acc(rs.getBigDecimal("r45_num_depo_acc"));
obj.setR45_num_borrowers(rs.getBigDecimal("r45_num_borrowers"));
obj.setR45_num_loan_acc(rs.getBigDecimal("r45_num_loan_acc"));

// =========================
// R46 VALUES
// =========================
obj.setR46_num_by_inst_sec(rs.getString("r46_num_by_inst_sec"));
obj.setR46_num_depo(rs.getBigDecimal("r46_num_depo"));
obj.setR46_num_depo_acc(rs.getBigDecimal("r46_num_depo_acc"));
obj.setR46_num_borrowers(rs.getBigDecimal("r46_num_borrowers"));
obj.setR46_num_loan_acc(rs.getBigDecimal("r46_num_loan_acc"));

// =========================
// R47 VALUES
// =========================
obj.setR47_num_by_inst_sec(rs.getString("r47_num_by_inst_sec"));
obj.setR47_num_depo(rs.getBigDecimal("r47_num_depo"));
obj.setR47_num_depo_acc(rs.getBigDecimal("r47_num_depo_acc"));
obj.setR47_num_borrowers(rs.getBigDecimal("r47_num_borrowers"));
obj.setR47_num_loan_acc(rs.getBigDecimal("r47_num_loan_acc"));

// =========================
// R48 VALUES
// =========================
obj.setR48_num_by_inst_sec(rs.getString("r48_num_by_inst_sec"));
obj.setR48_num_depo(rs.getBigDecimal("r48_num_depo"));
obj.setR48_num_depo_acc(rs.getBigDecimal("r48_num_depo_acc"));
obj.setR48_num_borrowers(rs.getBigDecimal("r48_num_borrowers"));
obj.setR48_num_loan_acc(rs.getBigDecimal("r48_num_loan_acc"));

// =========================
// R49 VALUES
// =========================
obj.setR49_num_by_inst_sec(rs.getString("r49_num_by_inst_sec"));
obj.setR49_num_depo(rs.getBigDecimal("r49_num_depo"));
obj.setR49_num_depo_acc(rs.getBigDecimal("r49_num_depo_acc"));
obj.setR49_num_borrowers(rs.getBigDecimal("r49_num_borrowers"));
obj.setR49_num_loan_acc(rs.getBigDecimal("r49_num_loan_acc"));

// =========================
// R50 VALUES
// =========================
obj.setR50_num_by_inst_sec(rs.getString("r50_num_by_inst_sec"));
obj.setR50_num_depo(rs.getBigDecimal("r50_num_depo"));
obj.setR50_num_depo_acc(rs.getBigDecimal("r50_num_depo_acc"));
obj.setR50_num_borrowers(rs.getBigDecimal("r50_num_borrowers"));
obj.setR50_num_loan_acc(rs.getBigDecimal("r50_num_loan_acc"));

// =========================
// R51 VALUES
// =========================
obj.setR51_num_by_inst_sec(rs.getString("r51_num_by_inst_sec"));
obj.setR51_num_depo(rs.getBigDecimal("r51_num_depo"));
obj.setR51_num_depo_acc(rs.getBigDecimal("r51_num_depo_acc"));
obj.setR51_num_borrowers(rs.getBigDecimal("r51_num_borrowers"));
obj.setR51_num_loan_acc(rs.getBigDecimal("r51_num_loan_acc"));

// =========================
// R52 VALUES
// =========================
obj.setR52_num_by_inst_sec(rs.getString("r52_num_by_inst_sec"));
obj.setR52_num_depo(rs.getBigDecimal("r52_num_depo"));
obj.setR52_num_depo_acc(rs.getBigDecimal("r52_num_depo_acc"));
obj.setR52_num_borrowers(rs.getBigDecimal("r52_num_borrowers"));
obj.setR52_num_loan_acc(rs.getBigDecimal("r52_num_loan_acc"));

// =========================
// R53 VALUES
// =========================
obj.setR53_num_by_inst_sec(rs.getString("r53_num_by_inst_sec"));
obj.setR53_num_depo(rs.getBigDecimal("r53_num_depo"));
obj.setR53_num_depo_acc(rs.getBigDecimal("r53_num_depo_acc"));
obj.setR53_num_borrowers(rs.getBigDecimal("r53_num_borrowers"));
obj.setR53_num_loan_acc(rs.getBigDecimal("r53_num_loan_acc"));

// =========================
// R54 VALUES
// =========================
obj.setR54_num_by_inst_sec(rs.getString("r54_num_by_inst_sec"));
obj.setR54_num_depo(rs.getBigDecimal("r54_num_depo"));
obj.setR54_num_depo_acc(rs.getBigDecimal("r54_num_depo_acc"));
obj.setR54_num_borrowers(rs.getBigDecimal("r54_num_borrowers"));
obj.setR54_num_loan_acc(rs.getBigDecimal("r54_num_loan_acc"));

// =========================
// R55 VALUES
// =========================
obj.setR55_num_by_inst_sec(rs.getString("r55_num_by_inst_sec"));
obj.setR55_num_depo(rs.getBigDecimal("r55_num_depo"));
obj.setR55_num_depo_acc(rs.getBigDecimal("r55_num_depo_acc"));
obj.setR55_num_borrowers(rs.getBigDecimal("r55_num_borrowers"));
obj.setR55_num_loan_acc(rs.getBigDecimal("r55_num_loan_acc"));

// =========================
// R56 VALUES
// =========================
obj.setR56_num_by_inst_sec(rs.getString("r56_num_by_inst_sec"));
obj.setR56_num_depo(rs.getBigDecimal("r56_num_depo"));
obj.setR56_num_depo_acc(rs.getBigDecimal("r56_num_depo_acc"));
obj.setR56_num_borrowers(rs.getBigDecimal("r56_num_borrowers"));
obj.setR56_num_loan_acc(rs.getBigDecimal("r56_num_loan_acc"));

// =========================
// R57 VALUES
// =========================
obj.setR57_num_by_inst_sec(rs.getString("r57_num_by_inst_sec"));
obj.setR57_num_depo(rs.getBigDecimal("r57_num_depo"));
obj.setR57_num_depo_acc(rs.getBigDecimal("r57_num_depo_acc"));
obj.setR57_num_borrowers(rs.getBigDecimal("r57_num_borrowers"));
obj.setR57_num_loan_acc(rs.getBigDecimal("r57_num_loan_acc"));

// =========================
// R58 VALUES
// =========================
obj.setR58_num_by_inst_sec(rs.getString("r58_num_by_inst_sec"));
obj.setR58_num_depo(rs.getBigDecimal("r58_num_depo"));
obj.setR58_num_depo_acc(rs.getBigDecimal("r58_num_depo_acc"));
obj.setR58_num_borrowers(rs.getBigDecimal("r58_num_borrowers"));
obj.setR58_num_loan_acc(rs.getBigDecimal("r58_num_loan_acc"));

// =========================
// R59 VALUES
// =========================
obj.setR59_num_by_inst_sec(rs.getString("r59_num_by_inst_sec"));
obj.setR59_num_depo(rs.getBigDecimal("r59_num_depo"));
obj.setR59_num_depo_acc(rs.getBigDecimal("r59_num_depo_acc"));
obj.setR59_num_borrowers(rs.getBigDecimal("r59_num_borrowers"));
obj.setR59_num_loan_acc(rs.getBigDecimal("r59_num_loan_acc"));

// =========================
// R60 VALUES
// =========================
obj.setR60_num_by_inst_sec(rs.getString("r60_num_by_inst_sec"));
obj.setR60_num_depo(rs.getBigDecimal("r60_num_depo"));
obj.setR60_num_depo_acc(rs.getBigDecimal("r60_num_depo_acc"));
obj.setR60_num_borrowers(rs.getBigDecimal("r60_num_borrowers"));
obj.setR60_num_loan_acc(rs.getBigDecimal("r60_num_loan_acc"));

// =========================
// R61 VALUES
// =========================
obj.setR61_num_by_inst_sec(rs.getString("r61_num_by_inst_sec"));
obj.setR61_num_depo(rs.getBigDecimal("r61_num_depo"));
obj.setR61_num_depo_acc(rs.getBigDecimal("r61_num_depo_acc"));
obj.setR61_num_borrowers(rs.getBigDecimal("r61_num_borrowers"));
obj.setR61_num_loan_acc(rs.getBigDecimal("r61_num_loan_acc"));

// =========================
// R62 VALUES
// =========================
obj.setR62_num_by_inst_sec(rs.getString("r62_num_by_inst_sec"));
obj.setR62_num_depo(rs.getBigDecimal("r62_num_depo"));
obj.setR62_num_depo_acc(rs.getBigDecimal("r62_num_depo_acc"));
obj.setR62_num_borrowers(rs.getBigDecimal("r62_num_borrowers"));
obj.setR62_num_loan_acc(rs.getBigDecimal("r62_num_loan_acc"));

// =========================
// R63 VALUES
// =========================
obj.setR63_num_by_inst_sec(rs.getString("r63_num_by_inst_sec"));
obj.setR63_num_depo(rs.getBigDecimal("r63_num_depo"));
obj.setR63_num_depo_acc(rs.getBigDecimal("r63_num_depo_acc"));
obj.setR63_num_borrowers(rs.getBigDecimal("r63_num_borrowers"));
obj.setR63_num_loan_acc(rs.getBigDecimal("r63_num_loan_acc"));

// =========================
// R64 VALUES
// =========================
obj.setR64_num_by_inst_sec(rs.getString("r64_num_by_inst_sec"));
obj.setR64_num_depo(rs.getBigDecimal("r64_num_depo"));
obj.setR64_num_depo_acc(rs.getBigDecimal("r64_num_depo_acc"));
obj.setR64_num_borrowers(rs.getBigDecimal("r64_num_borrowers"));
obj.setR64_num_loan_acc(rs.getBigDecimal("r64_num_loan_acc"));		
		
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


public class Q_ATF_Summary_Entity {
	
	
	private String	r11_num_by_inst_sec;
	private BigDecimal	r11_num_depo;
	private BigDecimal	r11_num_depo_acc;
	private BigDecimal	r11_num_borrowers;
	private BigDecimal	r11_num_loan_acc;
	private String	r12_num_by_inst_sec;
	private BigDecimal	r12_num_depo;
	private BigDecimal	r12_num_depo_acc;
	private BigDecimal	r12_num_borrowers;
	private BigDecimal	r12_num_loan_acc;
	private String	r13_num_by_inst_sec;
	private BigDecimal	r13_num_depo;
	private BigDecimal	r13_num_depo_acc;
	private BigDecimal	r13_num_borrowers;
	private BigDecimal	r13_num_loan_acc;
	private String	r14_num_by_inst_sec;
	private BigDecimal	r14_num_depo;
	private BigDecimal	r14_num_depo_acc;
	private BigDecimal	r14_num_borrowers;
	private BigDecimal	r14_num_loan_acc;
	private String	r15_num_by_inst_sec;
	private BigDecimal	r15_num_depo;
	private BigDecimal	r15_num_depo_acc;
	private BigDecimal	r15_num_borrowers;
	private BigDecimal	r15_num_loan_acc;
	private String	r16_num_by_inst_sec;
	private BigDecimal	r16_num_depo;
	private BigDecimal	r16_num_depo_acc;
	private BigDecimal	r16_num_borrowers;
	private BigDecimal	r16_num_loan_acc;
	private String	r17_num_by_inst_sec;
	private BigDecimal	r17_num_depo;
	private BigDecimal	r17_num_depo_acc;
	private BigDecimal	r17_num_borrowers;
	private BigDecimal	r17_num_loan_acc;
	private String	r18_num_by_inst_sec;
	private BigDecimal	r18_num_depo;
	private BigDecimal	r18_num_depo_acc;
	private BigDecimal	r18_num_borrowers;
	private BigDecimal	r18_num_loan_acc;
	private String	r19_num_by_inst_sec;
	private BigDecimal	r19_num_depo;
	private BigDecimal	r19_num_depo_acc;
	private BigDecimal	r19_num_borrowers;
	private BigDecimal	r19_num_loan_acc;
	private String	r20_num_by_inst_sec;
	private BigDecimal	r20_num_depo;
	private BigDecimal	r20_num_depo_acc;
	private BigDecimal	r20_num_borrowers;
	private BigDecimal	r20_num_loan_acc;
	private String	r21_num_by_inst_sec;
	private BigDecimal	r21_num_depo;
	private BigDecimal	r21_num_depo_acc;
	private BigDecimal	r21_num_borrowers;
	private BigDecimal	r21_num_loan_acc;
	private String	r22_num_by_inst_sec;
	private BigDecimal	r22_num_depo;
	private BigDecimal	r22_num_depo_acc;
	private BigDecimal	r22_num_borrowers;
	private BigDecimal	r22_num_loan_acc;
	private String	r23_num_by_inst_sec;
	private BigDecimal	r23_num_depo;
	private BigDecimal	r23_num_depo_acc;
	private BigDecimal	r23_num_borrowers;
	private BigDecimal	r23_num_loan_acc;
	private String	r24_num_by_inst_sec;
	private BigDecimal	r24_num_depo;
	private BigDecimal	r24_num_depo_acc;
	private BigDecimal	r24_num_borrowers;
	private BigDecimal	r24_num_loan_acc;
	private String	r25_num_by_inst_sec;
	private BigDecimal	r25_num_depo;
	private BigDecimal	r25_num_depo_acc;
	private BigDecimal	r25_num_borrowers;
	private BigDecimal	r25_num_loan_acc;
	private String	r26_num_by_inst_sec;
	private BigDecimal	r26_num_depo;
	private BigDecimal	r26_num_depo_acc;
	private BigDecimal	r26_num_borrowers;
	private BigDecimal	r26_num_loan_acc;
	private String	r27_num_by_inst_sec;
	private BigDecimal	r27_num_depo;
	private BigDecimal	r27_num_depo_acc;
	private BigDecimal	r27_num_borrowers;
	private BigDecimal	r27_num_loan_acc;
	private String	r28_num_by_inst_sec;
	private BigDecimal	r28_num_depo;
	private BigDecimal	r28_num_depo_acc;
	private BigDecimal	r28_num_borrowers;
	private BigDecimal	r28_num_loan_acc;
	private String	r29_num_by_inst_sec;
	private BigDecimal	r29_num_depo;
	private BigDecimal	r29_num_depo_acc;
	private BigDecimal	r29_num_borrowers;
	private BigDecimal	r29_num_loan_acc;
	private String	r30_num_by_inst_sec;
	private BigDecimal	r30_num_depo;
	private BigDecimal	r30_num_depo_acc;
	private BigDecimal	r30_num_borrowers;
	private BigDecimal	r30_num_loan_acc;
	private String	r31_num_by_inst_sec;
	private BigDecimal	r31_num_depo;
	private BigDecimal	r31_num_depo_acc;
	private BigDecimal	r31_num_borrowers;
	private BigDecimal	r31_num_loan_acc;
	private String	r32_num_by_inst_sec;
	private BigDecimal	r32_num_depo;
	private BigDecimal	r32_num_depo_acc;
	private BigDecimal	r32_num_borrowers;
	private BigDecimal	r32_num_loan_acc;
	private String	r33_num_by_inst_sec;
	private BigDecimal	r33_num_depo;
	private BigDecimal	r33_num_depo_acc;
	private BigDecimal	r33_num_borrowers;
	private BigDecimal	r33_num_loan_acc;
	private String	r34_num_by_inst_sec;
	private BigDecimal	r34_num_depo;
	private BigDecimal	r34_num_depo_acc;
	private BigDecimal	r34_num_borrowers;
	private BigDecimal	r34_num_loan_acc;
	private String	r35_num_by_inst_sec;
	private BigDecimal	r35_num_depo;
	private BigDecimal	r35_num_depo_acc;
	private BigDecimal	r35_num_borrowers;
	private BigDecimal	r35_num_loan_acc;
	private String	r36_num_by_inst_sec;
	private BigDecimal	r36_num_depo;
	private BigDecimal	r36_num_depo_acc;
	private BigDecimal	r36_num_borrowers;
	private BigDecimal	r36_num_loan_acc;
	private String	r37_num_by_inst_sec;
	private BigDecimal	r37_num_depo;
	private BigDecimal	r37_num_depo_acc;
	private BigDecimal	r37_num_borrowers;
	private BigDecimal	r37_num_loan_acc;
	private String	r38_num_by_inst_sec;
	private BigDecimal	r38_num_depo;
	private BigDecimal	r38_num_depo_acc;
	private BigDecimal	r38_num_borrowers;
	private BigDecimal	r38_num_loan_acc;
	private String	r39_num_by_inst_sec;
	private BigDecimal	r39_num_depo;
	private BigDecimal	r39_num_depo_acc;
	private BigDecimal	r39_num_borrowers;
	private BigDecimal	r39_num_loan_acc;
	private String	r40_num_by_inst_sec;
	private BigDecimal	r40_num_depo;
	private BigDecimal	r40_num_depo_acc;
	private BigDecimal	r40_num_borrowers;
	private BigDecimal	r40_num_loan_acc;
	private String	r41_num_by_inst_sec;
	private BigDecimal	r41_num_depo;
	private BigDecimal	r41_num_depo_acc;
	private BigDecimal	r41_num_borrowers;
	private BigDecimal	r41_num_loan_acc;
	private String	r42_num_by_inst_sec;
	private BigDecimal	r42_num_depo;
	private BigDecimal	r42_num_depo_acc;
	private BigDecimal	r42_num_borrowers;
	private BigDecimal	r42_num_loan_acc;
	private String	r43_num_by_inst_sec;
	private BigDecimal	r43_num_depo;
	private BigDecimal	r43_num_depo_acc;
	private BigDecimal	r43_num_borrowers;
	private BigDecimal	r43_num_loan_acc;
	private String	r44_num_by_inst_sec;
	private BigDecimal	r44_num_depo;
	private BigDecimal	r44_num_depo_acc;
	private BigDecimal	r44_num_borrowers;
	private BigDecimal	r44_num_loan_acc;
	private String	r45_num_by_inst_sec;
	private BigDecimal	r45_num_depo;
	private BigDecimal	r45_num_depo_acc;
	private BigDecimal	r45_num_borrowers;
	private BigDecimal	r45_num_loan_acc;
	private String	r46_num_by_inst_sec;
	private BigDecimal	r46_num_depo;
	private BigDecimal	r46_num_depo_acc;
	private BigDecimal	r46_num_borrowers;
	private BigDecimal	r46_num_loan_acc;
	private String	r47_num_by_inst_sec;
	private BigDecimal	r47_num_depo;
	private BigDecimal	r47_num_depo_acc;
	private BigDecimal	r47_num_borrowers;
	private BigDecimal	r47_num_loan_acc;
	private String	r48_num_by_inst_sec;
	private BigDecimal	r48_num_depo;
	private BigDecimal	r48_num_depo_acc;
	private BigDecimal	r48_num_borrowers;
	private BigDecimal	r48_num_loan_acc;
	private String	r49_num_by_inst_sec;
	private BigDecimal	r49_num_depo;
	private BigDecimal	r49_num_depo_acc;
	private BigDecimal	r49_num_borrowers;
	private BigDecimal	r49_num_loan_acc;
	private String	r50_num_by_inst_sec;
	private BigDecimal	r50_num_depo;
	private BigDecimal	r50_num_depo_acc;
	private BigDecimal	r50_num_borrowers;
	private BigDecimal	r50_num_loan_acc;
	private String	r51_num_by_inst_sec;
	private BigDecimal	r51_num_depo;
	private BigDecimal	r51_num_depo_acc;
	private BigDecimal	r51_num_borrowers;
	private BigDecimal	r51_num_loan_acc;
	private String	r52_num_by_inst_sec;
	private BigDecimal	r52_num_depo;
	private BigDecimal	r52_num_depo_acc;
	private BigDecimal	r52_num_borrowers;
	private BigDecimal	r52_num_loan_acc;
	private String	r53_num_by_inst_sec;
	private BigDecimal	r53_num_depo;
	private BigDecimal	r53_num_depo_acc;
	private BigDecimal	r53_num_borrowers;
	private BigDecimal	r53_num_loan_acc;
	private String	r54_num_by_inst_sec;
	private BigDecimal	r54_num_depo;
	private BigDecimal	r54_num_depo_acc;
	private BigDecimal	r54_num_borrowers;
	private BigDecimal	r54_num_loan_acc;
	private String	r55_num_by_inst_sec;
	private BigDecimal	r55_num_depo;
	private BigDecimal	r55_num_depo_acc;
	private BigDecimal	r55_num_borrowers;
	private BigDecimal	r55_num_loan_acc;
	private String	r56_num_by_inst_sec;
	private BigDecimal	r56_num_depo;
	private BigDecimal	r56_num_depo_acc;
	private BigDecimal	r56_num_borrowers;
	private BigDecimal	r56_num_loan_acc;
	private String	r57_num_by_inst_sec;
	private BigDecimal	r57_num_depo;
	private BigDecimal	r57_num_depo_acc;
	private BigDecimal	r57_num_borrowers;
	private BigDecimal	r57_num_loan_acc;
	private String	r58_num_by_inst_sec;
	private BigDecimal	r58_num_depo;
	private BigDecimal	r58_num_depo_acc;
	private BigDecimal	r58_num_borrowers;
	private BigDecimal	r58_num_loan_acc;
	private String	r59_num_by_inst_sec;
	private BigDecimal	r59_num_depo;
	private BigDecimal	r59_num_depo_acc;
	private BigDecimal	r59_num_borrowers;
	private BigDecimal	r59_num_loan_acc;
	private String	r60_num_by_inst_sec;
	private BigDecimal	r60_num_depo;
	private BigDecimal	r60_num_depo_acc;
	private BigDecimal	r60_num_borrowers;
	private BigDecimal	r60_num_loan_acc;
	private String	r61_num_by_inst_sec;
	private BigDecimal	r61_num_depo;
	private BigDecimal	r61_num_depo_acc;
	private BigDecimal	r61_num_borrowers;
	private BigDecimal	r61_num_loan_acc;
	private String	r62_num_by_inst_sec;
	private BigDecimal	r62_num_depo;
	private BigDecimal	r62_num_depo_acc;
	private BigDecimal	r62_num_borrowers;
	private BigDecimal	r62_num_loan_acc;
	private String	r63_num_by_inst_sec;
	private BigDecimal	r63_num_depo;
	private BigDecimal	r63_num_depo_acc;
	private BigDecimal	r63_num_borrowers;
	private BigDecimal	r63_num_loan_acc;
	private String	r64_num_by_inst_sec;
	private BigDecimal	r64_num_depo;
	private BigDecimal	r64_num_depo_acc;
	private BigDecimal	r64_num_borrowers;
	private BigDecimal	r64_num_loan_acc;
	               
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
	

public String getR11_num_by_inst_sec() {
		return r11_num_by_inst_sec;
	}
	public void setR11_num_by_inst_sec(String r11_num_by_inst_sec) {
		this.r11_num_by_inst_sec = r11_num_by_inst_sec;
	}
	public BigDecimal getR11_num_depo() {
		return r11_num_depo;
	}
	public void setR11_num_depo(BigDecimal r11_num_depo) {
		this.r11_num_depo = r11_num_depo;
	}
	public BigDecimal getR11_num_depo_acc() {
		return r11_num_depo_acc;
	}
	public void setR11_num_depo_acc(BigDecimal r11_num_depo_acc) {
		this.r11_num_depo_acc = r11_num_depo_acc;
	}
	public BigDecimal getR11_num_borrowers() {
		return r11_num_borrowers;
	}
	public void setR11_num_borrowers(BigDecimal r11_num_borrowers) {
		this.r11_num_borrowers = r11_num_borrowers;
	}
	public BigDecimal getR11_num_loan_acc() {
		return r11_num_loan_acc;
	}
	public void setR11_num_loan_acc(BigDecimal r11_num_loan_acc) {
		this.r11_num_loan_acc = r11_num_loan_acc;
	}
	public String getR12_num_by_inst_sec() {
		return r12_num_by_inst_sec;
	}
	public void setR12_num_by_inst_sec(String r12_num_by_inst_sec) {
		this.r12_num_by_inst_sec = r12_num_by_inst_sec;
	}
	public BigDecimal getR12_num_depo() {
		return r12_num_depo;
	}
	public void setR12_num_depo(BigDecimal r12_num_depo) {
		this.r12_num_depo = r12_num_depo;
	}
	public BigDecimal getR12_num_depo_acc() {
		return r12_num_depo_acc;
	}
	public void setR12_num_depo_acc(BigDecimal r12_num_depo_acc) {
		this.r12_num_depo_acc = r12_num_depo_acc;
	}
	public BigDecimal getR12_num_borrowers() {
		return r12_num_borrowers;
	}
	public void setR12_num_borrowers(BigDecimal r12_num_borrowers) {
		this.r12_num_borrowers = r12_num_borrowers;
	}
	public BigDecimal getR12_num_loan_acc() {
		return r12_num_loan_acc;
	}
	public void setR12_num_loan_acc(BigDecimal r12_num_loan_acc) {
		this.r12_num_loan_acc = r12_num_loan_acc;
	}
	public String getR13_num_by_inst_sec() {
		return r13_num_by_inst_sec;
	}
	public void setR13_num_by_inst_sec(String r13_num_by_inst_sec) {
		this.r13_num_by_inst_sec = r13_num_by_inst_sec;
	}
	public BigDecimal getR13_num_depo() {
		return r13_num_depo;
	}
	public void setR13_num_depo(BigDecimal r13_num_depo) {
		this.r13_num_depo = r13_num_depo;
	}
	public BigDecimal getR13_num_depo_acc() {
		return r13_num_depo_acc;
	}
	public void setR13_num_depo_acc(BigDecimal r13_num_depo_acc) {
		this.r13_num_depo_acc = r13_num_depo_acc;
	}
	public BigDecimal getR13_num_borrowers() {
		return r13_num_borrowers;
	}
	public void setR13_num_borrowers(BigDecimal r13_num_borrowers) {
		this.r13_num_borrowers = r13_num_borrowers;
	}
	public BigDecimal getR13_num_loan_acc() {
		return r13_num_loan_acc;
	}
	public void setR13_num_loan_acc(BigDecimal r13_num_loan_acc) {
		this.r13_num_loan_acc = r13_num_loan_acc;
	}
	public String getR14_num_by_inst_sec() {
		return r14_num_by_inst_sec;
	}
	public void setR14_num_by_inst_sec(String r14_num_by_inst_sec) {
		this.r14_num_by_inst_sec = r14_num_by_inst_sec;
	}
	public BigDecimal getR14_num_depo() {
		return r14_num_depo;
	}
	public void setR14_num_depo(BigDecimal r14_num_depo) {
		this.r14_num_depo = r14_num_depo;
	}
	public BigDecimal getR14_num_depo_acc() {
		return r14_num_depo_acc;
	}
	public void setR14_num_depo_acc(BigDecimal r14_num_depo_acc) {
		this.r14_num_depo_acc = r14_num_depo_acc;
	}
	public BigDecimal getR14_num_borrowers() {
		return r14_num_borrowers;
	}
	public void setR14_num_borrowers(BigDecimal r14_num_borrowers) {
		this.r14_num_borrowers = r14_num_borrowers;
	}
	public BigDecimal getR14_num_loan_acc() {
		return r14_num_loan_acc;
	}
	public void setR14_num_loan_acc(BigDecimal r14_num_loan_acc) {
		this.r14_num_loan_acc = r14_num_loan_acc;
	}
	public String getR15_num_by_inst_sec() {
		return r15_num_by_inst_sec;
	}
	public void setR15_num_by_inst_sec(String r15_num_by_inst_sec) {
		this.r15_num_by_inst_sec = r15_num_by_inst_sec;
	}
	public BigDecimal getR15_num_depo() {
		return r15_num_depo;
	}
	public void setR15_num_depo(BigDecimal r15_num_depo) {
		this.r15_num_depo = r15_num_depo;
	}
	public BigDecimal getR15_num_depo_acc() {
		return r15_num_depo_acc;
	}
	public void setR15_num_depo_acc(BigDecimal r15_num_depo_acc) {
		this.r15_num_depo_acc = r15_num_depo_acc;
	}
	public BigDecimal getR15_num_borrowers() {
		return r15_num_borrowers;
	}
	public void setR15_num_borrowers(BigDecimal r15_num_borrowers) {
		this.r15_num_borrowers = r15_num_borrowers;
	}
	public BigDecimal getR15_num_loan_acc() {
		return r15_num_loan_acc;
	}
	public void setR15_num_loan_acc(BigDecimal r15_num_loan_acc) {
		this.r15_num_loan_acc = r15_num_loan_acc;
	}
	public String getR16_num_by_inst_sec() {
		return r16_num_by_inst_sec;
	}
	public void setR16_num_by_inst_sec(String r16_num_by_inst_sec) {
		this.r16_num_by_inst_sec = r16_num_by_inst_sec;
	}
	public BigDecimal getR16_num_depo() {
		return r16_num_depo;
	}
	public void setR16_num_depo(BigDecimal r16_num_depo) {
		this.r16_num_depo = r16_num_depo;
	}
	public BigDecimal getR16_num_depo_acc() {
		return r16_num_depo_acc;
	}
	public void setR16_num_depo_acc(BigDecimal r16_num_depo_acc) {
		this.r16_num_depo_acc = r16_num_depo_acc;
	}
	public BigDecimal getR16_num_borrowers() {
		return r16_num_borrowers;
	}
	public void setR16_num_borrowers(BigDecimal r16_num_borrowers) {
		this.r16_num_borrowers = r16_num_borrowers;
	}
	public BigDecimal getR16_num_loan_acc() {
		return r16_num_loan_acc;
	}
	public void setR16_num_loan_acc(BigDecimal r16_num_loan_acc) {
		this.r16_num_loan_acc = r16_num_loan_acc;
	}
	public String getR17_num_by_inst_sec() {
		return r17_num_by_inst_sec;
	}
	public void setR17_num_by_inst_sec(String r17_num_by_inst_sec) {
		this.r17_num_by_inst_sec = r17_num_by_inst_sec;
	}
	public BigDecimal getR17_num_depo() {
		return r17_num_depo;
	}
	public void setR17_num_depo(BigDecimal r17_num_depo) {
		this.r17_num_depo = r17_num_depo;
	}
	public BigDecimal getR17_num_depo_acc() {
		return r17_num_depo_acc;
	}
	public void setR17_num_depo_acc(BigDecimal r17_num_depo_acc) {
		this.r17_num_depo_acc = r17_num_depo_acc;
	}
	public BigDecimal getR17_num_borrowers() {
		return r17_num_borrowers;
	}
	public void setR17_num_borrowers(BigDecimal r17_num_borrowers) {
		this.r17_num_borrowers = r17_num_borrowers;
	}
	public BigDecimal getR17_num_loan_acc() {
		return r17_num_loan_acc;
	}
	public void setR17_num_loan_acc(BigDecimal r17_num_loan_acc) {
		this.r17_num_loan_acc = r17_num_loan_acc;
	}
	public String getR18_num_by_inst_sec() {
		return r18_num_by_inst_sec;
	}
	public void setR18_num_by_inst_sec(String r18_num_by_inst_sec) {
		this.r18_num_by_inst_sec = r18_num_by_inst_sec;
	}
	public BigDecimal getR18_num_depo() {
		return r18_num_depo;
	}
	public void setR18_num_depo(BigDecimal r18_num_depo) {
		this.r18_num_depo = r18_num_depo;
	}
	public BigDecimal getR18_num_depo_acc() {
		return r18_num_depo_acc;
	}
	public void setR18_num_depo_acc(BigDecimal r18_num_depo_acc) {
		this.r18_num_depo_acc = r18_num_depo_acc;
	}
	public BigDecimal getR18_num_borrowers() {
		return r18_num_borrowers;
	}
	public void setR18_num_borrowers(BigDecimal r18_num_borrowers) {
		this.r18_num_borrowers = r18_num_borrowers;
	}
	public BigDecimal getR18_num_loan_acc() {
		return r18_num_loan_acc;
	}
	public void setR18_num_loan_acc(BigDecimal r18_num_loan_acc) {
		this.r18_num_loan_acc = r18_num_loan_acc;
	}
	public String getR19_num_by_inst_sec() {
		return r19_num_by_inst_sec;
	}
	public void setR19_num_by_inst_sec(String r19_num_by_inst_sec) {
		this.r19_num_by_inst_sec = r19_num_by_inst_sec;
	}
	public BigDecimal getR19_num_depo() {
		return r19_num_depo;
	}
	public void setR19_num_depo(BigDecimal r19_num_depo) {
		this.r19_num_depo = r19_num_depo;
	}
	public BigDecimal getR19_num_depo_acc() {
		return r19_num_depo_acc;
	}
	public void setR19_num_depo_acc(BigDecimal r19_num_depo_acc) {
		this.r19_num_depo_acc = r19_num_depo_acc;
	}
	public BigDecimal getR19_num_borrowers() {
		return r19_num_borrowers;
	}
	public void setR19_num_borrowers(BigDecimal r19_num_borrowers) {
		this.r19_num_borrowers = r19_num_borrowers;
	}
	public BigDecimal getR19_num_loan_acc() {
		return r19_num_loan_acc;
	}
	public void setR19_num_loan_acc(BigDecimal r19_num_loan_acc) {
		this.r19_num_loan_acc = r19_num_loan_acc;
	}
	public String getR20_num_by_inst_sec() {
		return r20_num_by_inst_sec;
	}
	public void setR20_num_by_inst_sec(String r20_num_by_inst_sec) {
		this.r20_num_by_inst_sec = r20_num_by_inst_sec;
	}
	public BigDecimal getR20_num_depo() {
		return r20_num_depo;
	}
	public void setR20_num_depo(BigDecimal r20_num_depo) {
		this.r20_num_depo = r20_num_depo;
	}
	public BigDecimal getR20_num_depo_acc() {
		return r20_num_depo_acc;
	}
	public void setR20_num_depo_acc(BigDecimal r20_num_depo_acc) {
		this.r20_num_depo_acc = r20_num_depo_acc;
	}
	public BigDecimal getR20_num_borrowers() {
		return r20_num_borrowers;
	}
	public void setR20_num_borrowers(BigDecimal r20_num_borrowers) {
		this.r20_num_borrowers = r20_num_borrowers;
	}
	public BigDecimal getR20_num_loan_acc() {
		return r20_num_loan_acc;
	}
	public void setR20_num_loan_acc(BigDecimal r20_num_loan_acc) {
		this.r20_num_loan_acc = r20_num_loan_acc;
	}
	public String getR21_num_by_inst_sec() {
		return r21_num_by_inst_sec;
	}
	public void setR21_num_by_inst_sec(String r21_num_by_inst_sec) {
		this.r21_num_by_inst_sec = r21_num_by_inst_sec;
	}
	public BigDecimal getR21_num_depo() {
		return r21_num_depo;
	}
	public void setR21_num_depo(BigDecimal r21_num_depo) {
		this.r21_num_depo = r21_num_depo;
	}
	public BigDecimal getR21_num_depo_acc() {
		return r21_num_depo_acc;
	}
	public void setR21_num_depo_acc(BigDecimal r21_num_depo_acc) {
		this.r21_num_depo_acc = r21_num_depo_acc;
	}
	public BigDecimal getR21_num_borrowers() {
		return r21_num_borrowers;
	}
	public void setR21_num_borrowers(BigDecimal r21_num_borrowers) {
		this.r21_num_borrowers = r21_num_borrowers;
	}
	public BigDecimal getR21_num_loan_acc() {
		return r21_num_loan_acc;
	}
	public void setR21_num_loan_acc(BigDecimal r21_num_loan_acc) {
		this.r21_num_loan_acc = r21_num_loan_acc;
	}
	public String getR22_num_by_inst_sec() {
		return r22_num_by_inst_sec;
	}
	public void setR22_num_by_inst_sec(String r22_num_by_inst_sec) {
		this.r22_num_by_inst_sec = r22_num_by_inst_sec;
	}
	public BigDecimal getR22_num_depo() {
		return r22_num_depo;
	}
	public void setR22_num_depo(BigDecimal r22_num_depo) {
		this.r22_num_depo = r22_num_depo;
	}
	public BigDecimal getR22_num_depo_acc() {
		return r22_num_depo_acc;
	}
	public void setR22_num_depo_acc(BigDecimal r22_num_depo_acc) {
		this.r22_num_depo_acc = r22_num_depo_acc;
	}
	public BigDecimal getR22_num_borrowers() {
		return r22_num_borrowers;
	}
	public void setR22_num_borrowers(BigDecimal r22_num_borrowers) {
		this.r22_num_borrowers = r22_num_borrowers;
	}
	public BigDecimal getR22_num_loan_acc() {
		return r22_num_loan_acc;
	}
	public void setR22_num_loan_acc(BigDecimal r22_num_loan_acc) {
		this.r22_num_loan_acc = r22_num_loan_acc;
	}
	public String getR23_num_by_inst_sec() {
		return r23_num_by_inst_sec;
	}
	public void setR23_num_by_inst_sec(String r23_num_by_inst_sec) {
		this.r23_num_by_inst_sec = r23_num_by_inst_sec;
	}
	public BigDecimal getR23_num_depo() {
		return r23_num_depo;
	}
	public void setR23_num_depo(BigDecimal r23_num_depo) {
		this.r23_num_depo = r23_num_depo;
	}
	public BigDecimal getR23_num_depo_acc() {
		return r23_num_depo_acc;
	}
	public void setR23_num_depo_acc(BigDecimal r23_num_depo_acc) {
		this.r23_num_depo_acc = r23_num_depo_acc;
	}
	public BigDecimal getR23_num_borrowers() {
		return r23_num_borrowers;
	}
	public void setR23_num_borrowers(BigDecimal r23_num_borrowers) {
		this.r23_num_borrowers = r23_num_borrowers;
	}
	public BigDecimal getR23_num_loan_acc() {
		return r23_num_loan_acc;
	}
	public void setR23_num_loan_acc(BigDecimal r23_num_loan_acc) {
		this.r23_num_loan_acc = r23_num_loan_acc;
	}
	public String getR24_num_by_inst_sec() {
		return r24_num_by_inst_sec;
	}
	public void setR24_num_by_inst_sec(String r24_num_by_inst_sec) {
		this.r24_num_by_inst_sec = r24_num_by_inst_sec;
	}
	public BigDecimal getR24_num_depo() {
		return r24_num_depo;
	}
	public void setR24_num_depo(BigDecimal r24_num_depo) {
		this.r24_num_depo = r24_num_depo;
	}
	public BigDecimal getR24_num_depo_acc() {
		return r24_num_depo_acc;
	}
	public void setR24_num_depo_acc(BigDecimal r24_num_depo_acc) {
		this.r24_num_depo_acc = r24_num_depo_acc;
	}
	public BigDecimal getR24_num_borrowers() {
		return r24_num_borrowers;
	}
	public void setR24_num_borrowers(BigDecimal r24_num_borrowers) {
		this.r24_num_borrowers = r24_num_borrowers;
	}
	public BigDecimal getR24_num_loan_acc() {
		return r24_num_loan_acc;
	}
	public void setR24_num_loan_acc(BigDecimal r24_num_loan_acc) {
		this.r24_num_loan_acc = r24_num_loan_acc;
	}
	public String getR25_num_by_inst_sec() {
		return r25_num_by_inst_sec;
	}
	public void setR25_num_by_inst_sec(String r25_num_by_inst_sec) {
		this.r25_num_by_inst_sec = r25_num_by_inst_sec;
	}
	public BigDecimal getR25_num_depo() {
		return r25_num_depo;
	}
	public void setR25_num_depo(BigDecimal r25_num_depo) {
		this.r25_num_depo = r25_num_depo;
	}
	public BigDecimal getR25_num_depo_acc() {
		return r25_num_depo_acc;
	}
	public void setR25_num_depo_acc(BigDecimal r25_num_depo_acc) {
		this.r25_num_depo_acc = r25_num_depo_acc;
	}
	public BigDecimal getR25_num_borrowers() {
		return r25_num_borrowers;
	}
	public void setR25_num_borrowers(BigDecimal r25_num_borrowers) {
		this.r25_num_borrowers = r25_num_borrowers;
	}
	public BigDecimal getR25_num_loan_acc() {
		return r25_num_loan_acc;
	}
	public void setR25_num_loan_acc(BigDecimal r25_num_loan_acc) {
		this.r25_num_loan_acc = r25_num_loan_acc;
	}
	public String getR26_num_by_inst_sec() {
		return r26_num_by_inst_sec;
	}
	public void setR26_num_by_inst_sec(String r26_num_by_inst_sec) {
		this.r26_num_by_inst_sec = r26_num_by_inst_sec;
	}
	public BigDecimal getR26_num_depo() {
		return r26_num_depo;
	}
	public void setR26_num_depo(BigDecimal r26_num_depo) {
		this.r26_num_depo = r26_num_depo;
	}
	public BigDecimal getR26_num_depo_acc() {
		return r26_num_depo_acc;
	}
	public void setR26_num_depo_acc(BigDecimal r26_num_depo_acc) {
		this.r26_num_depo_acc = r26_num_depo_acc;
	}
	public BigDecimal getR26_num_borrowers() {
		return r26_num_borrowers;
	}
	public void setR26_num_borrowers(BigDecimal r26_num_borrowers) {
		this.r26_num_borrowers = r26_num_borrowers;
	}
	public BigDecimal getR26_num_loan_acc() {
		return r26_num_loan_acc;
	}
	public void setR26_num_loan_acc(BigDecimal r26_num_loan_acc) {
		this.r26_num_loan_acc = r26_num_loan_acc;
	}
	public String getR27_num_by_inst_sec() {
		return r27_num_by_inst_sec;
	}
	public void setR27_num_by_inst_sec(String r27_num_by_inst_sec) {
		this.r27_num_by_inst_sec = r27_num_by_inst_sec;
	}
	public BigDecimal getR27_num_depo() {
		return r27_num_depo;
	}
	public void setR27_num_depo(BigDecimal r27_num_depo) {
		this.r27_num_depo = r27_num_depo;
	}
	public BigDecimal getR27_num_depo_acc() {
		return r27_num_depo_acc;
	}
	public void setR27_num_depo_acc(BigDecimal r27_num_depo_acc) {
		this.r27_num_depo_acc = r27_num_depo_acc;
	}
	public BigDecimal getR27_num_borrowers() {
		return r27_num_borrowers;
	}
	public void setR27_num_borrowers(BigDecimal r27_num_borrowers) {
		this.r27_num_borrowers = r27_num_borrowers;
	}
	public BigDecimal getR27_num_loan_acc() {
		return r27_num_loan_acc;
	}
	public void setR27_num_loan_acc(BigDecimal r27_num_loan_acc) {
		this.r27_num_loan_acc = r27_num_loan_acc;
	}
	public String getR28_num_by_inst_sec() {
		return r28_num_by_inst_sec;
	}
	public void setR28_num_by_inst_sec(String r28_num_by_inst_sec) {
		this.r28_num_by_inst_sec = r28_num_by_inst_sec;
	}
	public BigDecimal getR28_num_depo() {
		return r28_num_depo;
	}
	public void setR28_num_depo(BigDecimal r28_num_depo) {
		this.r28_num_depo = r28_num_depo;
	}
	public BigDecimal getR28_num_depo_acc() {
		return r28_num_depo_acc;
	}
	public void setR28_num_depo_acc(BigDecimal r28_num_depo_acc) {
		this.r28_num_depo_acc = r28_num_depo_acc;
	}
	public BigDecimal getR28_num_borrowers() {
		return r28_num_borrowers;
	}
	public void setR28_num_borrowers(BigDecimal r28_num_borrowers) {
		this.r28_num_borrowers = r28_num_borrowers;
	}
	public BigDecimal getR28_num_loan_acc() {
		return r28_num_loan_acc;
	}
	public void setR28_num_loan_acc(BigDecimal r28_num_loan_acc) {
		this.r28_num_loan_acc = r28_num_loan_acc;
	}
	public String getR29_num_by_inst_sec() {
		return r29_num_by_inst_sec;
	}
	public void setR29_num_by_inst_sec(String r29_num_by_inst_sec) {
		this.r29_num_by_inst_sec = r29_num_by_inst_sec;
	}
	public BigDecimal getR29_num_depo() {
		return r29_num_depo;
	}
	public void setR29_num_depo(BigDecimal r29_num_depo) {
		this.r29_num_depo = r29_num_depo;
	}
	public BigDecimal getR29_num_depo_acc() {
		return r29_num_depo_acc;
	}
	public void setR29_num_depo_acc(BigDecimal r29_num_depo_acc) {
		this.r29_num_depo_acc = r29_num_depo_acc;
	}
	public BigDecimal getR29_num_borrowers() {
		return r29_num_borrowers;
	}
	public void setR29_num_borrowers(BigDecimal r29_num_borrowers) {
		this.r29_num_borrowers = r29_num_borrowers;
	}
	public BigDecimal getR29_num_loan_acc() {
		return r29_num_loan_acc;
	}
	public void setR29_num_loan_acc(BigDecimal r29_num_loan_acc) {
		this.r29_num_loan_acc = r29_num_loan_acc;
	}
	public String getR30_num_by_inst_sec() {
		return r30_num_by_inst_sec;
	}
	public void setR30_num_by_inst_sec(String r30_num_by_inst_sec) {
		this.r30_num_by_inst_sec = r30_num_by_inst_sec;
	}
	public BigDecimal getR30_num_depo() {
		return r30_num_depo;
	}
	public void setR30_num_depo(BigDecimal r30_num_depo) {
		this.r30_num_depo = r30_num_depo;
	}
	public BigDecimal getR30_num_depo_acc() {
		return r30_num_depo_acc;
	}
	public void setR30_num_depo_acc(BigDecimal r30_num_depo_acc) {
		this.r30_num_depo_acc = r30_num_depo_acc;
	}
	public BigDecimal getR30_num_borrowers() {
		return r30_num_borrowers;
	}
	public void setR30_num_borrowers(BigDecimal r30_num_borrowers) {
		this.r30_num_borrowers = r30_num_borrowers;
	}
	public BigDecimal getR30_num_loan_acc() {
		return r30_num_loan_acc;
	}
	public void setR30_num_loan_acc(BigDecimal r30_num_loan_acc) {
		this.r30_num_loan_acc = r30_num_loan_acc;
	}
	public String getR31_num_by_inst_sec() {
		return r31_num_by_inst_sec;
	}
	public void setR31_num_by_inst_sec(String r31_num_by_inst_sec) {
		this.r31_num_by_inst_sec = r31_num_by_inst_sec;
	}
	public BigDecimal getR31_num_depo() {
		return r31_num_depo;
	}
	public void setR31_num_depo(BigDecimal r31_num_depo) {
		this.r31_num_depo = r31_num_depo;
	}
	public BigDecimal getR31_num_depo_acc() {
		return r31_num_depo_acc;
	}
	public void setR31_num_depo_acc(BigDecimal r31_num_depo_acc) {
		this.r31_num_depo_acc = r31_num_depo_acc;
	}
	public BigDecimal getR31_num_borrowers() {
		return r31_num_borrowers;
	}
	public void setR31_num_borrowers(BigDecimal r31_num_borrowers) {
		this.r31_num_borrowers = r31_num_borrowers;
	}
	public BigDecimal getR31_num_loan_acc() {
		return r31_num_loan_acc;
	}
	public void setR31_num_loan_acc(BigDecimal r31_num_loan_acc) {
		this.r31_num_loan_acc = r31_num_loan_acc;
	}
	public String getR32_num_by_inst_sec() {
		return r32_num_by_inst_sec;
	}
	public void setR32_num_by_inst_sec(String r32_num_by_inst_sec) {
		this.r32_num_by_inst_sec = r32_num_by_inst_sec;
	}
	public BigDecimal getR32_num_depo() {
		return r32_num_depo;
	}
	public void setR32_num_depo(BigDecimal r32_num_depo) {
		this.r32_num_depo = r32_num_depo;
	}
	public BigDecimal getR32_num_depo_acc() {
		return r32_num_depo_acc;
	}
	public void setR32_num_depo_acc(BigDecimal r32_num_depo_acc) {
		this.r32_num_depo_acc = r32_num_depo_acc;
	}
	public BigDecimal getR32_num_borrowers() {
		return r32_num_borrowers;
	}
	public void setR32_num_borrowers(BigDecimal r32_num_borrowers) {
		this.r32_num_borrowers = r32_num_borrowers;
	}
	public BigDecimal getR32_num_loan_acc() {
		return r32_num_loan_acc;
	}
	public void setR32_num_loan_acc(BigDecimal r32_num_loan_acc) {
		this.r32_num_loan_acc = r32_num_loan_acc;
	}
	public String getR33_num_by_inst_sec() {
		return r33_num_by_inst_sec;
	}
	public void setR33_num_by_inst_sec(String r33_num_by_inst_sec) {
		this.r33_num_by_inst_sec = r33_num_by_inst_sec;
	}
	public BigDecimal getR33_num_depo() {
		return r33_num_depo;
	}
	public void setR33_num_depo(BigDecimal r33_num_depo) {
		this.r33_num_depo = r33_num_depo;
	}
	public BigDecimal getR33_num_depo_acc() {
		return r33_num_depo_acc;
	}
	public void setR33_num_depo_acc(BigDecimal r33_num_depo_acc) {
		this.r33_num_depo_acc = r33_num_depo_acc;
	}
	public BigDecimal getR33_num_borrowers() {
		return r33_num_borrowers;
	}
	public void setR33_num_borrowers(BigDecimal r33_num_borrowers) {
		this.r33_num_borrowers = r33_num_borrowers;
	}
	public BigDecimal getR33_num_loan_acc() {
		return r33_num_loan_acc;
	}
	public void setR33_num_loan_acc(BigDecimal r33_num_loan_acc) {
		this.r33_num_loan_acc = r33_num_loan_acc;
	}
	public String getR34_num_by_inst_sec() {
		return r34_num_by_inst_sec;
	}
	public void setR34_num_by_inst_sec(String r34_num_by_inst_sec) {
		this.r34_num_by_inst_sec = r34_num_by_inst_sec;
	}
	public BigDecimal getR34_num_depo() {
		return r34_num_depo;
	}
	public void setR34_num_depo(BigDecimal r34_num_depo) {
		this.r34_num_depo = r34_num_depo;
	}
	public BigDecimal getR34_num_depo_acc() {
		return r34_num_depo_acc;
	}
	public void setR34_num_depo_acc(BigDecimal r34_num_depo_acc) {
		this.r34_num_depo_acc = r34_num_depo_acc;
	}
	public BigDecimal getR34_num_borrowers() {
		return r34_num_borrowers;
	}
	public void setR34_num_borrowers(BigDecimal r34_num_borrowers) {
		this.r34_num_borrowers = r34_num_borrowers;
	}
	public BigDecimal getR34_num_loan_acc() {
		return r34_num_loan_acc;
	}
	public void setR34_num_loan_acc(BigDecimal r34_num_loan_acc) {
		this.r34_num_loan_acc = r34_num_loan_acc;
	}
	public String getR35_num_by_inst_sec() {
		return r35_num_by_inst_sec;
	}
	public void setR35_num_by_inst_sec(String r35_num_by_inst_sec) {
		this.r35_num_by_inst_sec = r35_num_by_inst_sec;
	}
	public BigDecimal getR35_num_depo() {
		return r35_num_depo;
	}
	public void setR35_num_depo(BigDecimal r35_num_depo) {
		this.r35_num_depo = r35_num_depo;
	}
	public BigDecimal getR35_num_depo_acc() {
		return r35_num_depo_acc;
	}
	public void setR35_num_depo_acc(BigDecimal r35_num_depo_acc) {
		this.r35_num_depo_acc = r35_num_depo_acc;
	}
	public BigDecimal getR35_num_borrowers() {
		return r35_num_borrowers;
	}
	public void setR35_num_borrowers(BigDecimal r35_num_borrowers) {
		this.r35_num_borrowers = r35_num_borrowers;
	}
	public BigDecimal getR35_num_loan_acc() {
		return r35_num_loan_acc;
	}
	public void setR35_num_loan_acc(BigDecimal r35_num_loan_acc) {
		this.r35_num_loan_acc = r35_num_loan_acc;
	}
	public String getR36_num_by_inst_sec() {
		return r36_num_by_inst_sec;
	}
	public void setR36_num_by_inst_sec(String r36_num_by_inst_sec) {
		this.r36_num_by_inst_sec = r36_num_by_inst_sec;
	}
	public BigDecimal getR36_num_depo() {
		return r36_num_depo;
	}
	public void setR36_num_depo(BigDecimal r36_num_depo) {
		this.r36_num_depo = r36_num_depo;
	}
	public BigDecimal getR36_num_depo_acc() {
		return r36_num_depo_acc;
	}
	public void setR36_num_depo_acc(BigDecimal r36_num_depo_acc) {
		this.r36_num_depo_acc = r36_num_depo_acc;
	}
	public BigDecimal getR36_num_borrowers() {
		return r36_num_borrowers;
	}
	public void setR36_num_borrowers(BigDecimal r36_num_borrowers) {
		this.r36_num_borrowers = r36_num_borrowers;
	}
	public BigDecimal getR36_num_loan_acc() {
		return r36_num_loan_acc;
	}
	public void setR36_num_loan_acc(BigDecimal r36_num_loan_acc) {
		this.r36_num_loan_acc = r36_num_loan_acc;
	}
	public String getR37_num_by_inst_sec() {
		return r37_num_by_inst_sec;
	}
	public void setR37_num_by_inst_sec(String r37_num_by_inst_sec) {
		this.r37_num_by_inst_sec = r37_num_by_inst_sec;
	}
	public BigDecimal getR37_num_depo() {
		return r37_num_depo;
	}
	public void setR37_num_depo(BigDecimal r37_num_depo) {
		this.r37_num_depo = r37_num_depo;
	}
	public BigDecimal getR37_num_depo_acc() {
		return r37_num_depo_acc;
	}
	public void setR37_num_depo_acc(BigDecimal r37_num_depo_acc) {
		this.r37_num_depo_acc = r37_num_depo_acc;
	}
	public BigDecimal getR37_num_borrowers() {
		return r37_num_borrowers;
	}
	public void setR37_num_borrowers(BigDecimal r37_num_borrowers) {
		this.r37_num_borrowers = r37_num_borrowers;
	}
	public BigDecimal getR37_num_loan_acc() {
		return r37_num_loan_acc;
	}
	public void setR37_num_loan_acc(BigDecimal r37_num_loan_acc) {
		this.r37_num_loan_acc = r37_num_loan_acc;
	}
	public String getR38_num_by_inst_sec() {
		return r38_num_by_inst_sec;
	}
	public void setR38_num_by_inst_sec(String r38_num_by_inst_sec) {
		this.r38_num_by_inst_sec = r38_num_by_inst_sec;
	}
	public BigDecimal getR38_num_depo() {
		return r38_num_depo;
	}
	public void setR38_num_depo(BigDecimal r38_num_depo) {
		this.r38_num_depo = r38_num_depo;
	}
	public BigDecimal getR38_num_depo_acc() {
		return r38_num_depo_acc;
	}
	public void setR38_num_depo_acc(BigDecimal r38_num_depo_acc) {
		this.r38_num_depo_acc = r38_num_depo_acc;
	}
	public BigDecimal getR38_num_borrowers() {
		return r38_num_borrowers;
	}
	public void setR38_num_borrowers(BigDecimal r38_num_borrowers) {
		this.r38_num_borrowers = r38_num_borrowers;
	}
	public BigDecimal getR38_num_loan_acc() {
		return r38_num_loan_acc;
	}
	public void setR38_num_loan_acc(BigDecimal r38_num_loan_acc) {
		this.r38_num_loan_acc = r38_num_loan_acc;
	}
	public String getR39_num_by_inst_sec() {
		return r39_num_by_inst_sec;
	}
	public void setR39_num_by_inst_sec(String r39_num_by_inst_sec) {
		this.r39_num_by_inst_sec = r39_num_by_inst_sec;
	}
	public BigDecimal getR39_num_depo() {
		return r39_num_depo;
	}
	public void setR39_num_depo(BigDecimal r39_num_depo) {
		this.r39_num_depo = r39_num_depo;
	}
	public BigDecimal getR39_num_depo_acc() {
		return r39_num_depo_acc;
	}
	public void setR39_num_depo_acc(BigDecimal r39_num_depo_acc) {
		this.r39_num_depo_acc = r39_num_depo_acc;
	}
	public BigDecimal getR39_num_borrowers() {
		return r39_num_borrowers;
	}
	public void setR39_num_borrowers(BigDecimal r39_num_borrowers) {
		this.r39_num_borrowers = r39_num_borrowers;
	}
	public BigDecimal getR39_num_loan_acc() {
		return r39_num_loan_acc;
	}
	public void setR39_num_loan_acc(BigDecimal r39_num_loan_acc) {
		this.r39_num_loan_acc = r39_num_loan_acc;
	}
	public String getR40_num_by_inst_sec() {
		return r40_num_by_inst_sec;
	}
	public void setR40_num_by_inst_sec(String r40_num_by_inst_sec) {
		this.r40_num_by_inst_sec = r40_num_by_inst_sec;
	}
	public BigDecimal getR40_num_depo() {
		return r40_num_depo;
	}
	public void setR40_num_depo(BigDecimal r40_num_depo) {
		this.r40_num_depo = r40_num_depo;
	}
	public BigDecimal getR40_num_depo_acc() {
		return r40_num_depo_acc;
	}
	public void setR40_num_depo_acc(BigDecimal r40_num_depo_acc) {
		this.r40_num_depo_acc = r40_num_depo_acc;
	}
	public BigDecimal getR40_num_borrowers() {
		return r40_num_borrowers;
	}
	public void setR40_num_borrowers(BigDecimal r40_num_borrowers) {
		this.r40_num_borrowers = r40_num_borrowers;
	}
	public BigDecimal getR40_num_loan_acc() {
		return r40_num_loan_acc;
	}
	public void setR40_num_loan_acc(BigDecimal r40_num_loan_acc) {
		this.r40_num_loan_acc = r40_num_loan_acc;
	}
	public String getR41_num_by_inst_sec() {
		return r41_num_by_inst_sec;
	}
	public void setR41_num_by_inst_sec(String r41_num_by_inst_sec) {
		this.r41_num_by_inst_sec = r41_num_by_inst_sec;
	}
	public BigDecimal getR41_num_depo() {
		return r41_num_depo;
	}
	public void setR41_num_depo(BigDecimal r41_num_depo) {
		this.r41_num_depo = r41_num_depo;
	}
	public BigDecimal getR41_num_depo_acc() {
		return r41_num_depo_acc;
	}
	public void setR41_num_depo_acc(BigDecimal r41_num_depo_acc) {
		this.r41_num_depo_acc = r41_num_depo_acc;
	}
	public BigDecimal getR41_num_borrowers() {
		return r41_num_borrowers;
	}
	public void setR41_num_borrowers(BigDecimal r41_num_borrowers) {
		this.r41_num_borrowers = r41_num_borrowers;
	}
	public BigDecimal getR41_num_loan_acc() {
		return r41_num_loan_acc;
	}
	public void setR41_num_loan_acc(BigDecimal r41_num_loan_acc) {
		this.r41_num_loan_acc = r41_num_loan_acc;
	}
	public String getR42_num_by_inst_sec() {
		return r42_num_by_inst_sec;
	}
	public void setR42_num_by_inst_sec(String r42_num_by_inst_sec) {
		this.r42_num_by_inst_sec = r42_num_by_inst_sec;
	}
	public BigDecimal getR42_num_depo() {
		return r42_num_depo;
	}
	public void setR42_num_depo(BigDecimal r42_num_depo) {
		this.r42_num_depo = r42_num_depo;
	}
	public BigDecimal getR42_num_depo_acc() {
		return r42_num_depo_acc;
	}
	public void setR42_num_depo_acc(BigDecimal r42_num_depo_acc) {
		this.r42_num_depo_acc = r42_num_depo_acc;
	}
	public BigDecimal getR42_num_borrowers() {
		return r42_num_borrowers;
	}
	public void setR42_num_borrowers(BigDecimal r42_num_borrowers) {
		this.r42_num_borrowers = r42_num_borrowers;
	}
	public BigDecimal getR42_num_loan_acc() {
		return r42_num_loan_acc;
	}
	public void setR42_num_loan_acc(BigDecimal r42_num_loan_acc) {
		this.r42_num_loan_acc = r42_num_loan_acc;
	}
	public String getR43_num_by_inst_sec() {
		return r43_num_by_inst_sec;
	}
	public void setR43_num_by_inst_sec(String r43_num_by_inst_sec) {
		this.r43_num_by_inst_sec = r43_num_by_inst_sec;
	}
	public BigDecimal getR43_num_depo() {
		return r43_num_depo;
	}
	public void setR43_num_depo(BigDecimal r43_num_depo) {
		this.r43_num_depo = r43_num_depo;
	}
	public BigDecimal getR43_num_depo_acc() {
		return r43_num_depo_acc;
	}
	public void setR43_num_depo_acc(BigDecimal r43_num_depo_acc) {
		this.r43_num_depo_acc = r43_num_depo_acc;
	}
	public BigDecimal getR43_num_borrowers() {
		return r43_num_borrowers;
	}
	public void setR43_num_borrowers(BigDecimal r43_num_borrowers) {
		this.r43_num_borrowers = r43_num_borrowers;
	}
	public BigDecimal getR43_num_loan_acc() {
		return r43_num_loan_acc;
	}
	public void setR43_num_loan_acc(BigDecimal r43_num_loan_acc) {
		this.r43_num_loan_acc = r43_num_loan_acc;
	}
	public String getR44_num_by_inst_sec() {
		return r44_num_by_inst_sec;
	}
	public void setR44_num_by_inst_sec(String r44_num_by_inst_sec) {
		this.r44_num_by_inst_sec = r44_num_by_inst_sec;
	}
	public BigDecimal getR44_num_depo() {
		return r44_num_depo;
	}
	public void setR44_num_depo(BigDecimal r44_num_depo) {
		this.r44_num_depo = r44_num_depo;
	}
	public BigDecimal getR44_num_depo_acc() {
		return r44_num_depo_acc;
	}
	public void setR44_num_depo_acc(BigDecimal r44_num_depo_acc) {
		this.r44_num_depo_acc = r44_num_depo_acc;
	}
	public BigDecimal getR44_num_borrowers() {
		return r44_num_borrowers;
	}
	public void setR44_num_borrowers(BigDecimal r44_num_borrowers) {
		this.r44_num_borrowers = r44_num_borrowers;
	}
	public BigDecimal getR44_num_loan_acc() {
		return r44_num_loan_acc;
	}
	public void setR44_num_loan_acc(BigDecimal r44_num_loan_acc) {
		this.r44_num_loan_acc = r44_num_loan_acc;
	}
	public String getR45_num_by_inst_sec() {
		return r45_num_by_inst_sec;
	}
	public void setR45_num_by_inst_sec(String r45_num_by_inst_sec) {
		this.r45_num_by_inst_sec = r45_num_by_inst_sec;
	}
	public BigDecimal getR45_num_depo() {
		return r45_num_depo;
	}
	public void setR45_num_depo(BigDecimal r45_num_depo) {
		this.r45_num_depo = r45_num_depo;
	}
	public BigDecimal getR45_num_depo_acc() {
		return r45_num_depo_acc;
	}
	public void setR45_num_depo_acc(BigDecimal r45_num_depo_acc) {
		this.r45_num_depo_acc = r45_num_depo_acc;
	}
	public BigDecimal getR45_num_borrowers() {
		return r45_num_borrowers;
	}
	public void setR45_num_borrowers(BigDecimal r45_num_borrowers) {
		this.r45_num_borrowers = r45_num_borrowers;
	}
	public BigDecimal getR45_num_loan_acc() {
		return r45_num_loan_acc;
	}
	public void setR45_num_loan_acc(BigDecimal r45_num_loan_acc) {
		this.r45_num_loan_acc = r45_num_loan_acc;
	}
	public String getR46_num_by_inst_sec() {
		return r46_num_by_inst_sec;
	}
	public void setR46_num_by_inst_sec(String r46_num_by_inst_sec) {
		this.r46_num_by_inst_sec = r46_num_by_inst_sec;
	}
	public BigDecimal getR46_num_depo() {
		return r46_num_depo;
	}
	public void setR46_num_depo(BigDecimal r46_num_depo) {
		this.r46_num_depo = r46_num_depo;
	}
	public BigDecimal getR46_num_depo_acc() {
		return r46_num_depo_acc;
	}
	public void setR46_num_depo_acc(BigDecimal r46_num_depo_acc) {
		this.r46_num_depo_acc = r46_num_depo_acc;
	}
	public BigDecimal getR46_num_borrowers() {
		return r46_num_borrowers;
	}
	public void setR46_num_borrowers(BigDecimal r46_num_borrowers) {
		this.r46_num_borrowers = r46_num_borrowers;
	}
	public BigDecimal getR46_num_loan_acc() {
		return r46_num_loan_acc;
	}
	public void setR46_num_loan_acc(BigDecimal r46_num_loan_acc) {
		this.r46_num_loan_acc = r46_num_loan_acc;
	}
	public String getR47_num_by_inst_sec() {
		return r47_num_by_inst_sec;
	}
	public void setR47_num_by_inst_sec(String r47_num_by_inst_sec) {
		this.r47_num_by_inst_sec = r47_num_by_inst_sec;
	}
	public BigDecimal getR47_num_depo() {
		return r47_num_depo;
	}
	public void setR47_num_depo(BigDecimal r47_num_depo) {
		this.r47_num_depo = r47_num_depo;
	}
	public BigDecimal getR47_num_depo_acc() {
		return r47_num_depo_acc;
	}
	public void setR47_num_depo_acc(BigDecimal r47_num_depo_acc) {
		this.r47_num_depo_acc = r47_num_depo_acc;
	}
	public BigDecimal getR47_num_borrowers() {
		return r47_num_borrowers;
	}
	public void setR47_num_borrowers(BigDecimal r47_num_borrowers) {
		this.r47_num_borrowers = r47_num_borrowers;
	}
	public BigDecimal getR47_num_loan_acc() {
		return r47_num_loan_acc;
	}
	public void setR47_num_loan_acc(BigDecimal r47_num_loan_acc) {
		this.r47_num_loan_acc = r47_num_loan_acc;
	}
	public String getR48_num_by_inst_sec() {
		return r48_num_by_inst_sec;
	}
	public void setR48_num_by_inst_sec(String r48_num_by_inst_sec) {
		this.r48_num_by_inst_sec = r48_num_by_inst_sec;
	}
	public BigDecimal getR48_num_depo() {
		return r48_num_depo;
	}
	public void setR48_num_depo(BigDecimal r48_num_depo) {
		this.r48_num_depo = r48_num_depo;
	}
	public BigDecimal getR48_num_depo_acc() {
		return r48_num_depo_acc;
	}
	public void setR48_num_depo_acc(BigDecimal r48_num_depo_acc) {
		this.r48_num_depo_acc = r48_num_depo_acc;
	}
	public BigDecimal getR48_num_borrowers() {
		return r48_num_borrowers;
	}
	public void setR48_num_borrowers(BigDecimal r48_num_borrowers) {
		this.r48_num_borrowers = r48_num_borrowers;
	}
	public BigDecimal getR48_num_loan_acc() {
		return r48_num_loan_acc;
	}
	public void setR48_num_loan_acc(BigDecimal r48_num_loan_acc) {
		this.r48_num_loan_acc = r48_num_loan_acc;
	}
	public String getR49_num_by_inst_sec() {
		return r49_num_by_inst_sec;
	}
	public void setR49_num_by_inst_sec(String r49_num_by_inst_sec) {
		this.r49_num_by_inst_sec = r49_num_by_inst_sec;
	}
	public BigDecimal getR49_num_depo() {
		return r49_num_depo;
	}
	public void setR49_num_depo(BigDecimal r49_num_depo) {
		this.r49_num_depo = r49_num_depo;
	}
	public BigDecimal getR49_num_depo_acc() {
		return r49_num_depo_acc;
	}
	public void setR49_num_depo_acc(BigDecimal r49_num_depo_acc) {
		this.r49_num_depo_acc = r49_num_depo_acc;
	}
	public BigDecimal getR49_num_borrowers() {
		return r49_num_borrowers;
	}
	public void setR49_num_borrowers(BigDecimal r49_num_borrowers) {
		this.r49_num_borrowers = r49_num_borrowers;
	}
	public BigDecimal getR49_num_loan_acc() {
		return r49_num_loan_acc;
	}
	public void setR49_num_loan_acc(BigDecimal r49_num_loan_acc) {
		this.r49_num_loan_acc = r49_num_loan_acc;
	}
	public String getR50_num_by_inst_sec() {
		return r50_num_by_inst_sec;
	}
	public void setR50_num_by_inst_sec(String r50_num_by_inst_sec) {
		this.r50_num_by_inst_sec = r50_num_by_inst_sec;
	}
	public BigDecimal getR50_num_depo() {
		return r50_num_depo;
	}
	public void setR50_num_depo(BigDecimal r50_num_depo) {
		this.r50_num_depo = r50_num_depo;
	}
	public BigDecimal getR50_num_depo_acc() {
		return r50_num_depo_acc;
	}
	public void setR50_num_depo_acc(BigDecimal r50_num_depo_acc) {
		this.r50_num_depo_acc = r50_num_depo_acc;
	}
	public BigDecimal getR50_num_borrowers() {
		return r50_num_borrowers;
	}
	public void setR50_num_borrowers(BigDecimal r50_num_borrowers) {
		this.r50_num_borrowers = r50_num_borrowers;
	}
	public BigDecimal getR50_num_loan_acc() {
		return r50_num_loan_acc;
	}
	public void setR50_num_loan_acc(BigDecimal r50_num_loan_acc) {
		this.r50_num_loan_acc = r50_num_loan_acc;
	}
	public String getR51_num_by_inst_sec() {
		return r51_num_by_inst_sec;
	}
	public void setR51_num_by_inst_sec(String r51_num_by_inst_sec) {
		this.r51_num_by_inst_sec = r51_num_by_inst_sec;
	}
	public BigDecimal getR51_num_depo() {
		return r51_num_depo;
	}
	public void setR51_num_depo(BigDecimal r51_num_depo) {
		this.r51_num_depo = r51_num_depo;
	}
	public BigDecimal getR51_num_depo_acc() {
		return r51_num_depo_acc;
	}
	public void setR51_num_depo_acc(BigDecimal r51_num_depo_acc) {
		this.r51_num_depo_acc = r51_num_depo_acc;
	}
	public BigDecimal getR51_num_borrowers() {
		return r51_num_borrowers;
	}
	public void setR51_num_borrowers(BigDecimal r51_num_borrowers) {
		this.r51_num_borrowers = r51_num_borrowers;
	}
	public BigDecimal getR51_num_loan_acc() {
		return r51_num_loan_acc;
	}
	public void setR51_num_loan_acc(BigDecimal r51_num_loan_acc) {
		this.r51_num_loan_acc = r51_num_loan_acc;
	}
	public String getR52_num_by_inst_sec() {
		return r52_num_by_inst_sec;
	}
	public void setR52_num_by_inst_sec(String r52_num_by_inst_sec) {
		this.r52_num_by_inst_sec = r52_num_by_inst_sec;
	}
	public BigDecimal getR52_num_depo() {
		return r52_num_depo;
	}
	public void setR52_num_depo(BigDecimal r52_num_depo) {
		this.r52_num_depo = r52_num_depo;
	}
	public BigDecimal getR52_num_depo_acc() {
		return r52_num_depo_acc;
	}
	public void setR52_num_depo_acc(BigDecimal r52_num_depo_acc) {
		this.r52_num_depo_acc = r52_num_depo_acc;
	}
	public BigDecimal getR52_num_borrowers() {
		return r52_num_borrowers;
	}
	public void setR52_num_borrowers(BigDecimal r52_num_borrowers) {
		this.r52_num_borrowers = r52_num_borrowers;
	}
	public BigDecimal getR52_num_loan_acc() {
		return r52_num_loan_acc;
	}
	public void setR52_num_loan_acc(BigDecimal r52_num_loan_acc) {
		this.r52_num_loan_acc = r52_num_loan_acc;
	}
	public String getR53_num_by_inst_sec() {
		return r53_num_by_inst_sec;
	}
	public void setR53_num_by_inst_sec(String r53_num_by_inst_sec) {
		this.r53_num_by_inst_sec = r53_num_by_inst_sec;
	}
	public BigDecimal getR53_num_depo() {
		return r53_num_depo;
	}
	public void setR53_num_depo(BigDecimal r53_num_depo) {
		this.r53_num_depo = r53_num_depo;
	}
	public BigDecimal getR53_num_depo_acc() {
		return r53_num_depo_acc;
	}
	public void setR53_num_depo_acc(BigDecimal r53_num_depo_acc) {
		this.r53_num_depo_acc = r53_num_depo_acc;
	}
	public BigDecimal getR53_num_borrowers() {
		return r53_num_borrowers;
	}
	public void setR53_num_borrowers(BigDecimal r53_num_borrowers) {
		this.r53_num_borrowers = r53_num_borrowers;
	}
	public BigDecimal getR53_num_loan_acc() {
		return r53_num_loan_acc;
	}
	public void setR53_num_loan_acc(BigDecimal r53_num_loan_acc) {
		this.r53_num_loan_acc = r53_num_loan_acc;
	}
	public String getR54_num_by_inst_sec() {
		return r54_num_by_inst_sec;
	}
	public void setR54_num_by_inst_sec(String r54_num_by_inst_sec) {
		this.r54_num_by_inst_sec = r54_num_by_inst_sec;
	}
	public BigDecimal getR54_num_depo() {
		return r54_num_depo;
	}
	public void setR54_num_depo(BigDecimal r54_num_depo) {
		this.r54_num_depo = r54_num_depo;
	}
	public BigDecimal getR54_num_depo_acc() {
		return r54_num_depo_acc;
	}
	public void setR54_num_depo_acc(BigDecimal r54_num_depo_acc) {
		this.r54_num_depo_acc = r54_num_depo_acc;
	}
	public BigDecimal getR54_num_borrowers() {
		return r54_num_borrowers;
	}
	public void setR54_num_borrowers(BigDecimal r54_num_borrowers) {
		this.r54_num_borrowers = r54_num_borrowers;
	}
	public BigDecimal getR54_num_loan_acc() {
		return r54_num_loan_acc;
	}
	public void setR54_num_loan_acc(BigDecimal r54_num_loan_acc) {
		this.r54_num_loan_acc = r54_num_loan_acc;
	}
	public String getR55_num_by_inst_sec() {
		return r55_num_by_inst_sec;
	}
	public void setR55_num_by_inst_sec(String r55_num_by_inst_sec) {
		this.r55_num_by_inst_sec = r55_num_by_inst_sec;
	}
	public BigDecimal getR55_num_depo() {
		return r55_num_depo;
	}
	public void setR55_num_depo(BigDecimal r55_num_depo) {
		this.r55_num_depo = r55_num_depo;
	}
	public BigDecimal getR55_num_depo_acc() {
		return r55_num_depo_acc;
	}
	public void setR55_num_depo_acc(BigDecimal r55_num_depo_acc) {
		this.r55_num_depo_acc = r55_num_depo_acc;
	}
	public BigDecimal getR55_num_borrowers() {
		return r55_num_borrowers;
	}
	public void setR55_num_borrowers(BigDecimal r55_num_borrowers) {
		this.r55_num_borrowers = r55_num_borrowers;
	}
	public BigDecimal getR55_num_loan_acc() {
		return r55_num_loan_acc;
	}
	public void setR55_num_loan_acc(BigDecimal r55_num_loan_acc) {
		this.r55_num_loan_acc = r55_num_loan_acc;
	}
	public String getR56_num_by_inst_sec() {
		return r56_num_by_inst_sec;
	}
	public void setR56_num_by_inst_sec(String r56_num_by_inst_sec) {
		this.r56_num_by_inst_sec = r56_num_by_inst_sec;
	}
	public BigDecimal getR56_num_depo() {
		return r56_num_depo;
	}
	public void setR56_num_depo(BigDecimal r56_num_depo) {
		this.r56_num_depo = r56_num_depo;
	}
	public BigDecimal getR56_num_depo_acc() {
		return r56_num_depo_acc;
	}
	public void setR56_num_depo_acc(BigDecimal r56_num_depo_acc) {
		this.r56_num_depo_acc = r56_num_depo_acc;
	}
	public BigDecimal getR56_num_borrowers() {
		return r56_num_borrowers;
	}
	public void setR56_num_borrowers(BigDecimal r56_num_borrowers) {
		this.r56_num_borrowers = r56_num_borrowers;
	}
	public BigDecimal getR56_num_loan_acc() {
		return r56_num_loan_acc;
	}
	public void setR56_num_loan_acc(BigDecimal r56_num_loan_acc) {
		this.r56_num_loan_acc = r56_num_loan_acc;
	}
	public String getR57_num_by_inst_sec() {
		return r57_num_by_inst_sec;
	}
	public void setR57_num_by_inst_sec(String r57_num_by_inst_sec) {
		this.r57_num_by_inst_sec = r57_num_by_inst_sec;
	}
	public BigDecimal getR57_num_depo() {
		return r57_num_depo;
	}
	public void setR57_num_depo(BigDecimal r57_num_depo) {
		this.r57_num_depo = r57_num_depo;
	}
	public BigDecimal getR57_num_depo_acc() {
		return r57_num_depo_acc;
	}
	public void setR57_num_depo_acc(BigDecimal r57_num_depo_acc) {
		this.r57_num_depo_acc = r57_num_depo_acc;
	}
	public BigDecimal getR57_num_borrowers() {
		return r57_num_borrowers;
	}
	public void setR57_num_borrowers(BigDecimal r57_num_borrowers) {
		this.r57_num_borrowers = r57_num_borrowers;
	}
	public BigDecimal getR57_num_loan_acc() {
		return r57_num_loan_acc;
	}
	public void setR57_num_loan_acc(BigDecimal r57_num_loan_acc) {
		this.r57_num_loan_acc = r57_num_loan_acc;
	}
	public String getR58_num_by_inst_sec() {
		return r58_num_by_inst_sec;
	}
	public void setR58_num_by_inst_sec(String r58_num_by_inst_sec) {
		this.r58_num_by_inst_sec = r58_num_by_inst_sec;
	}
	public BigDecimal getR58_num_depo() {
		return r58_num_depo;
	}
	public void setR58_num_depo(BigDecimal r58_num_depo) {
		this.r58_num_depo = r58_num_depo;
	}
	public BigDecimal getR58_num_depo_acc() {
		return r58_num_depo_acc;
	}
	public void setR58_num_depo_acc(BigDecimal r58_num_depo_acc) {
		this.r58_num_depo_acc = r58_num_depo_acc;
	}
	public BigDecimal getR58_num_borrowers() {
		return r58_num_borrowers;
	}
	public void setR58_num_borrowers(BigDecimal r58_num_borrowers) {
		this.r58_num_borrowers = r58_num_borrowers;
	}
	public BigDecimal getR58_num_loan_acc() {
		return r58_num_loan_acc;
	}
	public void setR58_num_loan_acc(BigDecimal r58_num_loan_acc) {
		this.r58_num_loan_acc = r58_num_loan_acc;
	}
	public String getR59_num_by_inst_sec() {
		return r59_num_by_inst_sec;
	}
	public void setR59_num_by_inst_sec(String r59_num_by_inst_sec) {
		this.r59_num_by_inst_sec = r59_num_by_inst_sec;
	}
	public BigDecimal getR59_num_depo() {
		return r59_num_depo;
	}
	public void setR59_num_depo(BigDecimal r59_num_depo) {
		this.r59_num_depo = r59_num_depo;
	}
	public BigDecimal getR59_num_depo_acc() {
		return r59_num_depo_acc;
	}
	public void setR59_num_depo_acc(BigDecimal r59_num_depo_acc) {
		this.r59_num_depo_acc = r59_num_depo_acc;
	}
	public BigDecimal getR59_num_borrowers() {
		return r59_num_borrowers;
	}
	public void setR59_num_borrowers(BigDecimal r59_num_borrowers) {
		this.r59_num_borrowers = r59_num_borrowers;
	}
	public BigDecimal getR59_num_loan_acc() {
		return r59_num_loan_acc;
	}
	public void setR59_num_loan_acc(BigDecimal r59_num_loan_acc) {
		this.r59_num_loan_acc = r59_num_loan_acc;
	}
	public String getR60_num_by_inst_sec() {
		return r60_num_by_inst_sec;
	}
	public void setR60_num_by_inst_sec(String r60_num_by_inst_sec) {
		this.r60_num_by_inst_sec = r60_num_by_inst_sec;
	}
	public BigDecimal getR60_num_depo() {
		return r60_num_depo;
	}
	public void setR60_num_depo(BigDecimal r60_num_depo) {
		this.r60_num_depo = r60_num_depo;
	}
	public BigDecimal getR60_num_depo_acc() {
		return r60_num_depo_acc;
	}
	public void setR60_num_depo_acc(BigDecimal r60_num_depo_acc) {
		this.r60_num_depo_acc = r60_num_depo_acc;
	}
	public BigDecimal getR60_num_borrowers() {
		return r60_num_borrowers;
	}
	public void setR60_num_borrowers(BigDecimal r60_num_borrowers) {
		this.r60_num_borrowers = r60_num_borrowers;
	}
	public BigDecimal getR60_num_loan_acc() {
		return r60_num_loan_acc;
	}
	public void setR60_num_loan_acc(BigDecimal r60_num_loan_acc) {
		this.r60_num_loan_acc = r60_num_loan_acc;
	}
	public String getR61_num_by_inst_sec() {
		return r61_num_by_inst_sec;
	}
	public void setR61_num_by_inst_sec(String r61_num_by_inst_sec) {
		this.r61_num_by_inst_sec = r61_num_by_inst_sec;
	}
	public BigDecimal getR61_num_depo() {
		return r61_num_depo;
	}
	public void setR61_num_depo(BigDecimal r61_num_depo) {
		this.r61_num_depo = r61_num_depo;
	}
	public BigDecimal getR61_num_depo_acc() {
		return r61_num_depo_acc;
	}
	public void setR61_num_depo_acc(BigDecimal r61_num_depo_acc) {
		this.r61_num_depo_acc = r61_num_depo_acc;
	}
	public BigDecimal getR61_num_borrowers() {
		return r61_num_borrowers;
	}
	public void setR61_num_borrowers(BigDecimal r61_num_borrowers) {
		this.r61_num_borrowers = r61_num_borrowers;
	}
	public BigDecimal getR61_num_loan_acc() {
		return r61_num_loan_acc;
	}
	public void setR61_num_loan_acc(BigDecimal r61_num_loan_acc) {
		this.r61_num_loan_acc = r61_num_loan_acc;
	}
	public String getR62_num_by_inst_sec() {
		return r62_num_by_inst_sec;
	}
	public void setR62_num_by_inst_sec(String r62_num_by_inst_sec) {
		this.r62_num_by_inst_sec = r62_num_by_inst_sec;
	}
	public BigDecimal getR62_num_depo() {
		return r62_num_depo;
	}
	public void setR62_num_depo(BigDecimal r62_num_depo) {
		this.r62_num_depo = r62_num_depo;
	}
	public BigDecimal getR62_num_depo_acc() {
		return r62_num_depo_acc;
	}
	public void setR62_num_depo_acc(BigDecimal r62_num_depo_acc) {
		this.r62_num_depo_acc = r62_num_depo_acc;
	}
	public BigDecimal getR62_num_borrowers() {
		return r62_num_borrowers;
	}
	public void setR62_num_borrowers(BigDecimal r62_num_borrowers) {
		this.r62_num_borrowers = r62_num_borrowers;
	}
	public BigDecimal getR62_num_loan_acc() {
		return r62_num_loan_acc;
	}
	public void setR62_num_loan_acc(BigDecimal r62_num_loan_acc) {
		this.r62_num_loan_acc = r62_num_loan_acc;
	}
	public String getR63_num_by_inst_sec() {
		return r63_num_by_inst_sec;
	}
	public void setR63_num_by_inst_sec(String r63_num_by_inst_sec) {
		this.r63_num_by_inst_sec = r63_num_by_inst_sec;
	}
	public BigDecimal getR63_num_depo() {
		return r63_num_depo;
	}
	public void setR63_num_depo(BigDecimal r63_num_depo) {
		this.r63_num_depo = r63_num_depo;
	}
	public BigDecimal getR63_num_depo_acc() {
		return r63_num_depo_acc;
	}
	public void setR63_num_depo_acc(BigDecimal r63_num_depo_acc) {
		this.r63_num_depo_acc = r63_num_depo_acc;
	}
	public BigDecimal getR63_num_borrowers() {
		return r63_num_borrowers;
	}
	public void setR63_num_borrowers(BigDecimal r63_num_borrowers) {
		this.r63_num_borrowers = r63_num_borrowers;
	}
	public BigDecimal getR63_num_loan_acc() {
		return r63_num_loan_acc;
	}
	public void setR63_num_loan_acc(BigDecimal r63_num_loan_acc) {
		this.r63_num_loan_acc = r63_num_loan_acc;
	}
	public String getR64_num_by_inst_sec() {
		return r64_num_by_inst_sec;
	}
	public void setR64_num_by_inst_sec(String r64_num_by_inst_sec) {
		this.r64_num_by_inst_sec = r64_num_by_inst_sec;
	}
	public BigDecimal getR64_num_depo() {
		return r64_num_depo;
	}
	public void setR64_num_depo(BigDecimal r64_num_depo) {
		this.r64_num_depo = r64_num_depo;
	}
	public BigDecimal getR64_num_depo_acc() {
		return r64_num_depo_acc;
	}
	public void setR64_num_depo_acc(BigDecimal r64_num_depo_acc) {
		this.r64_num_depo_acc = r64_num_depo_acc;
	}
	public BigDecimal getR64_num_borrowers() {
		return r64_num_borrowers;
	}
	public void setR64_num_borrowers(BigDecimal r64_num_borrowers) {
		this.r64_num_borrowers = r64_num_borrowers;
	}
	public BigDecimal getR64_num_loan_acc() {
		return r64_num_loan_acc;
	}
	public void setR64_num_loan_acc(BigDecimal r64_num_loan_acc) {
		this.r64_num_loan_acc = r64_num_loan_acc;
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


public class Q_ATF_Archival_Summary_RowMapper
        implements RowMapper<Q_ATF_Archival_Summary_Entity> {

    @Override
    public Q_ATF_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        Q_ATF_Archival_Summary_Entity obj = new Q_ATF_Archival_Summary_Entity();
// =========================
// R11 VALUES
// =========================
obj.setR11_num_by_inst_sec(rs.getString("r11_num_by_inst_sec"));
obj.setR11_num_depo(rs.getBigDecimal("r11_num_depo"));
obj.setR11_num_depo_acc(rs.getBigDecimal("r11_num_depo_acc"));
obj.setR11_num_borrowers(rs.getBigDecimal("r11_num_borrowers"));
obj.setR11_num_loan_acc(rs.getBigDecimal("r11_num_loan_acc"));
        
// =========================
// R12 VALUES
// =========================
obj.setR12_num_by_inst_sec(rs.getString("r12_num_by_inst_sec"));
obj.setR12_num_depo(rs.getBigDecimal("r12_num_depo"));
obj.setR12_num_depo_acc(rs.getBigDecimal("r12_num_depo_acc"));
obj.setR12_num_borrowers(rs.getBigDecimal("r12_num_borrowers"));
obj.setR12_num_loan_acc(rs.getBigDecimal("r12_num_loan_acc"));

// =========================
// R13 VALUES
// =========================
obj.setR13_num_by_inst_sec(rs.getString("r13_num_by_inst_sec"));
obj.setR13_num_depo(rs.getBigDecimal("r13_num_depo"));
obj.setR13_num_depo_acc(rs.getBigDecimal("r13_num_depo_acc"));
obj.setR13_num_borrowers(rs.getBigDecimal("r13_num_borrowers"));
obj.setR13_num_loan_acc(rs.getBigDecimal("r13_num_loan_acc"));

// =========================
// R14 VALUES
// =========================
obj.setR14_num_by_inst_sec(rs.getString("r14_num_by_inst_sec"));
obj.setR14_num_depo(rs.getBigDecimal("r14_num_depo"));
obj.setR14_num_depo_acc(rs.getBigDecimal("r14_num_depo_acc"));
obj.setR14_num_borrowers(rs.getBigDecimal("r14_num_borrowers"));
obj.setR14_num_loan_acc(rs.getBigDecimal("r14_num_loan_acc"));

// =========================
// R15 VALUES
// =========================
obj.setR15_num_by_inst_sec(rs.getString("r15_num_by_inst_sec"));
obj.setR15_num_depo(rs.getBigDecimal("r15_num_depo"));
obj.setR15_num_depo_acc(rs.getBigDecimal("r15_num_depo_acc"));
obj.setR15_num_borrowers(rs.getBigDecimal("r15_num_borrowers"));
obj.setR15_num_loan_acc(rs.getBigDecimal("r15_num_loan_acc"));

// =========================
// R16 VALUES
// =========================
obj.setR16_num_by_inst_sec(rs.getString("r16_num_by_inst_sec"));
obj.setR16_num_depo(rs.getBigDecimal("r16_num_depo"));
obj.setR16_num_depo_acc(rs.getBigDecimal("r16_num_depo_acc"));
obj.setR16_num_borrowers(rs.getBigDecimal("r16_num_borrowers"));
obj.setR16_num_loan_acc(rs.getBigDecimal("r16_num_loan_acc"));

// =========================
// R17 VALUES
// =========================
obj.setR17_num_by_inst_sec(rs.getString("r17_num_by_inst_sec"));
obj.setR17_num_depo(rs.getBigDecimal("r17_num_depo"));
obj.setR17_num_depo_acc(rs.getBigDecimal("r17_num_depo_acc"));
obj.setR17_num_borrowers(rs.getBigDecimal("r17_num_borrowers"));
obj.setR17_num_loan_acc(rs.getBigDecimal("r17_num_loan_acc"));

// =========================
// R18 VALUES
// =========================
obj.setR18_num_by_inst_sec(rs.getString("r18_num_by_inst_sec"));
obj.setR18_num_depo(rs.getBigDecimal("r18_num_depo"));
obj.setR18_num_depo_acc(rs.getBigDecimal("r18_num_depo_acc"));
obj.setR18_num_borrowers(rs.getBigDecimal("r18_num_borrowers"));
obj.setR18_num_loan_acc(rs.getBigDecimal("r18_num_loan_acc"));

// =========================
// R19 VALUES
// =========================
obj.setR19_num_by_inst_sec(rs.getString("r19_num_by_inst_sec"));
obj.setR19_num_depo(rs.getBigDecimal("r19_num_depo"));
obj.setR19_num_depo_acc(rs.getBigDecimal("r19_num_depo_acc"));
obj.setR19_num_borrowers(rs.getBigDecimal("r19_num_borrowers"));
obj.setR19_num_loan_acc(rs.getBigDecimal("r19_num_loan_acc"));

// =========================
// R20 VALUES
// =========================
obj.setR20_num_by_inst_sec(rs.getString("r20_num_by_inst_sec"));
obj.setR20_num_depo(rs.getBigDecimal("r20_num_depo"));
obj.setR20_num_depo_acc(rs.getBigDecimal("r20_num_depo_acc"));
obj.setR20_num_borrowers(rs.getBigDecimal("r20_num_borrowers"));
obj.setR20_num_loan_acc(rs.getBigDecimal("r20_num_loan_acc"));

// =========================
// R21 VALUES
// =========================
obj.setR21_num_by_inst_sec(rs.getString("r21_num_by_inst_sec"));
obj.setR21_num_depo(rs.getBigDecimal("r21_num_depo"));
obj.setR21_num_depo_acc(rs.getBigDecimal("r21_num_depo_acc"));
obj.setR21_num_borrowers(rs.getBigDecimal("r21_num_borrowers"));
obj.setR21_num_loan_acc(rs.getBigDecimal("r21_num_loan_acc"));

// =========================
// R22 VALUES
// =========================
obj.setR22_num_by_inst_sec(rs.getString("r22_num_by_inst_sec"));
obj.setR22_num_depo(rs.getBigDecimal("r22_num_depo"));
obj.setR22_num_depo_acc(rs.getBigDecimal("r22_num_depo_acc"));
obj.setR22_num_borrowers(rs.getBigDecimal("r22_num_borrowers"));
obj.setR22_num_loan_acc(rs.getBigDecimal("r22_num_loan_acc"));

// =========================
// R23 VALUES
// =========================
obj.setR23_num_by_inst_sec(rs.getString("r23_num_by_inst_sec"));
obj.setR23_num_depo(rs.getBigDecimal("r23_num_depo"));
obj.setR23_num_depo_acc(rs.getBigDecimal("r23_num_depo_acc"));
obj.setR23_num_borrowers(rs.getBigDecimal("r23_num_borrowers"));
obj.setR23_num_loan_acc(rs.getBigDecimal("r23_num_loan_acc"));

// =========================
// R24 VALUES
// =========================
obj.setR24_num_by_inst_sec(rs.getString("r24_num_by_inst_sec"));
obj.setR24_num_depo(rs.getBigDecimal("r24_num_depo"));
obj.setR24_num_depo_acc(rs.getBigDecimal("r24_num_depo_acc"));
obj.setR24_num_borrowers(rs.getBigDecimal("r24_num_borrowers"));
obj.setR24_num_loan_acc(rs.getBigDecimal("r24_num_loan_acc"));

// =========================
// R25 VALUES
// =========================
obj.setR25_num_by_inst_sec(rs.getString("r25_num_by_inst_sec"));
obj.setR25_num_depo(rs.getBigDecimal("r25_num_depo"));
obj.setR25_num_depo_acc(rs.getBigDecimal("r25_num_depo_acc"));
obj.setR25_num_borrowers(rs.getBigDecimal("r25_num_borrowers"));
obj.setR25_num_loan_acc(rs.getBigDecimal("r25_num_loan_acc"));

// =========================
// R26 VALUES
// =========================
obj.setR26_num_by_inst_sec(rs.getString("r26_num_by_inst_sec"));
obj.setR26_num_depo(rs.getBigDecimal("r26_num_depo"));
obj.setR26_num_depo_acc(rs.getBigDecimal("r26_num_depo_acc"));
obj.setR26_num_borrowers(rs.getBigDecimal("r26_num_borrowers"));
obj.setR26_num_loan_acc(rs.getBigDecimal("r26_num_loan_acc"));

// =========================
// R27 VALUES
// =========================
obj.setR27_num_by_inst_sec(rs.getString("r27_num_by_inst_sec"));
obj.setR27_num_depo(rs.getBigDecimal("r27_num_depo"));
obj.setR27_num_depo_acc(rs.getBigDecimal("r27_num_depo_acc"));
obj.setR27_num_borrowers(rs.getBigDecimal("r27_num_borrowers"));
obj.setR27_num_loan_acc(rs.getBigDecimal("r27_num_loan_acc"));

// =========================
// R28 VALUES
// =========================
obj.setR28_num_by_inst_sec(rs.getString("r28_num_by_inst_sec"));
obj.setR28_num_depo(rs.getBigDecimal("r28_num_depo"));
obj.setR28_num_depo_acc(rs.getBigDecimal("r28_num_depo_acc"));
obj.setR28_num_borrowers(rs.getBigDecimal("r28_num_borrowers"));
obj.setR28_num_loan_acc(rs.getBigDecimal("r28_num_loan_acc"));

// =========================
// R29 VALUES
// =========================
obj.setR29_num_by_inst_sec(rs.getString("r29_num_by_inst_sec"));
obj.setR29_num_depo(rs.getBigDecimal("r29_num_depo"));
obj.setR29_num_depo_acc(rs.getBigDecimal("r29_num_depo_acc"));
obj.setR29_num_borrowers(rs.getBigDecimal("r29_num_borrowers"));
obj.setR29_num_loan_acc(rs.getBigDecimal("r29_num_loan_acc"));

// =========================
// R30 VALUES
// =========================
obj.setR30_num_by_inst_sec(rs.getString("r30_num_by_inst_sec"));
obj.setR30_num_depo(rs.getBigDecimal("r30_num_depo"));
obj.setR30_num_depo_acc(rs.getBigDecimal("r30_num_depo_acc"));
obj.setR30_num_borrowers(rs.getBigDecimal("r30_num_borrowers"));
obj.setR30_num_loan_acc(rs.getBigDecimal("r30_num_loan_acc"));

// =========================
// R31 VALUES
// =========================
obj.setR31_num_by_inst_sec(rs.getString("r31_num_by_inst_sec"));
obj.setR31_num_depo(rs.getBigDecimal("r31_num_depo"));
obj.setR31_num_depo_acc(rs.getBigDecimal("r31_num_depo_acc"));
obj.setR31_num_borrowers(rs.getBigDecimal("r31_num_borrowers"));
obj.setR31_num_loan_acc(rs.getBigDecimal("r31_num_loan_acc"));

// =========================
// R32 VALUES
// =========================
obj.setR32_num_by_inst_sec(rs.getString("r32_num_by_inst_sec"));
obj.setR32_num_depo(rs.getBigDecimal("r32_num_depo"));
obj.setR32_num_depo_acc(rs.getBigDecimal("r32_num_depo_acc"));
obj.setR32_num_borrowers(rs.getBigDecimal("r32_num_borrowers"));
obj.setR32_num_loan_acc(rs.getBigDecimal("r32_num_loan_acc"));

// =========================
// R33 VALUES
// =========================
obj.setR33_num_by_inst_sec(rs.getString("r33_num_by_inst_sec"));
obj.setR33_num_depo(rs.getBigDecimal("r33_num_depo"));
obj.setR33_num_depo_acc(rs.getBigDecimal("r33_num_depo_acc"));
obj.setR33_num_borrowers(rs.getBigDecimal("r33_num_borrowers"));
obj.setR33_num_loan_acc(rs.getBigDecimal("r33_num_loan_acc"));

// =========================
// R34 VALUES
// =========================
obj.setR34_num_by_inst_sec(rs.getString("r34_num_by_inst_sec"));
obj.setR34_num_depo(rs.getBigDecimal("r34_num_depo"));
obj.setR34_num_depo_acc(rs.getBigDecimal("r34_num_depo_acc"));
obj.setR34_num_borrowers(rs.getBigDecimal("r34_num_borrowers"));
obj.setR34_num_loan_acc(rs.getBigDecimal("r34_num_loan_acc"));

// =========================
// R35 VALUES
// =========================
obj.setR35_num_by_inst_sec(rs.getString("r35_num_by_inst_sec"));
obj.setR35_num_depo(rs.getBigDecimal("r35_num_depo"));
obj.setR35_num_depo_acc(rs.getBigDecimal("r35_num_depo_acc"));
obj.setR35_num_borrowers(rs.getBigDecimal("r35_num_borrowers"));
obj.setR35_num_loan_acc(rs.getBigDecimal("r35_num_loan_acc"));

// =========================
// R36 VALUES
// =========================
obj.setR36_num_by_inst_sec(rs.getString("r36_num_by_inst_sec"));
obj.setR36_num_depo(rs.getBigDecimal("r36_num_depo"));
obj.setR36_num_depo_acc(rs.getBigDecimal("r36_num_depo_acc"));
obj.setR36_num_borrowers(rs.getBigDecimal("r36_num_borrowers"));
obj.setR36_num_loan_acc(rs.getBigDecimal("r36_num_loan_acc"));

// =========================
// R37 VALUES
// =========================
obj.setR37_num_by_inst_sec(rs.getString("r37_num_by_inst_sec"));
obj.setR37_num_depo(rs.getBigDecimal("r37_num_depo"));
obj.setR37_num_depo_acc(rs.getBigDecimal("r37_num_depo_acc"));
obj.setR37_num_borrowers(rs.getBigDecimal("r37_num_borrowers"));
obj.setR37_num_loan_acc(rs.getBigDecimal("r37_num_loan_acc"));

// =========================
// R38 VALUES
// =========================
obj.setR38_num_by_inst_sec(rs.getString("r38_num_by_inst_sec"));
obj.setR38_num_depo(rs.getBigDecimal("r38_num_depo"));
obj.setR38_num_depo_acc(rs.getBigDecimal("r38_num_depo_acc"));
obj.setR38_num_borrowers(rs.getBigDecimal("r38_num_borrowers"));
obj.setR38_num_loan_acc(rs.getBigDecimal("r38_num_loan_acc"));

// =========================
// R39 VALUES
// =========================
obj.setR39_num_by_inst_sec(rs.getString("r39_num_by_inst_sec"));
obj.setR39_num_depo(rs.getBigDecimal("r39_num_depo"));
obj.setR39_num_depo_acc(rs.getBigDecimal("r39_num_depo_acc"));
obj.setR39_num_borrowers(rs.getBigDecimal("r39_num_borrowers"));
obj.setR39_num_loan_acc(rs.getBigDecimal("r39_num_loan_acc"));

// =========================
// R40 VALUES
// =========================
obj.setR40_num_by_inst_sec(rs.getString("r40_num_by_inst_sec"));
obj.setR40_num_depo(rs.getBigDecimal("r40_num_depo"));
obj.setR40_num_depo_acc(rs.getBigDecimal("r40_num_depo_acc"));
obj.setR40_num_borrowers(rs.getBigDecimal("r40_num_borrowers"));
obj.setR40_num_loan_acc(rs.getBigDecimal("r40_num_loan_acc"));

// =========================
// R41 VALUES
// =========================
obj.setR41_num_by_inst_sec(rs.getString("r41_num_by_inst_sec"));
obj.setR41_num_depo(rs.getBigDecimal("r41_num_depo"));
obj.setR41_num_depo_acc(rs.getBigDecimal("r41_num_depo_acc"));
obj.setR41_num_borrowers(rs.getBigDecimal("r41_num_borrowers"));
obj.setR41_num_loan_acc(rs.getBigDecimal("r41_num_loan_acc"));

// =========================
// R42 VALUES
// =========================
obj.setR42_num_by_inst_sec(rs.getString("r42_num_by_inst_sec"));
obj.setR42_num_depo(rs.getBigDecimal("r42_num_depo"));
obj.setR42_num_depo_acc(rs.getBigDecimal("r42_num_depo_acc"));
obj.setR42_num_borrowers(rs.getBigDecimal("r42_num_borrowers"));
obj.setR42_num_loan_acc(rs.getBigDecimal("r42_num_loan_acc"));

// =========================
// R43 VALUES
// =========================
obj.setR43_num_by_inst_sec(rs.getString("r43_num_by_inst_sec"));
obj.setR43_num_depo(rs.getBigDecimal("r43_num_depo"));
obj.setR43_num_depo_acc(rs.getBigDecimal("r43_num_depo_acc"));
obj.setR43_num_borrowers(rs.getBigDecimal("r43_num_borrowers"));
obj.setR43_num_loan_acc(rs.getBigDecimal("r43_num_loan_acc"));

// =========================
// R44 VALUES
// =========================
obj.setR44_num_by_inst_sec(rs.getString("r44_num_by_inst_sec"));
obj.setR44_num_depo(rs.getBigDecimal("r44_num_depo"));
obj.setR44_num_depo_acc(rs.getBigDecimal("r44_num_depo_acc"));
obj.setR44_num_borrowers(rs.getBigDecimal("r44_num_borrowers"));
obj.setR44_num_loan_acc(rs.getBigDecimal("r44_num_loan_acc"));

// =========================
// R45 VALUES
// =========================
obj.setR45_num_by_inst_sec(rs.getString("r45_num_by_inst_sec"));
obj.setR45_num_depo(rs.getBigDecimal("r45_num_depo"));
obj.setR45_num_depo_acc(rs.getBigDecimal("r45_num_depo_acc"));
obj.setR45_num_borrowers(rs.getBigDecimal("r45_num_borrowers"));
obj.setR45_num_loan_acc(rs.getBigDecimal("r45_num_loan_acc"));

// =========================
// R46 VALUES
// =========================
obj.setR46_num_by_inst_sec(rs.getString("r46_num_by_inst_sec"));
obj.setR46_num_depo(rs.getBigDecimal("r46_num_depo"));
obj.setR46_num_depo_acc(rs.getBigDecimal("r46_num_depo_acc"));
obj.setR46_num_borrowers(rs.getBigDecimal("r46_num_borrowers"));
obj.setR46_num_loan_acc(rs.getBigDecimal("r46_num_loan_acc"));

// =========================
// R47 VALUES
// =========================
obj.setR47_num_by_inst_sec(rs.getString("r47_num_by_inst_sec"));
obj.setR47_num_depo(rs.getBigDecimal("r47_num_depo"));
obj.setR47_num_depo_acc(rs.getBigDecimal("r47_num_depo_acc"));
obj.setR47_num_borrowers(rs.getBigDecimal("r47_num_borrowers"));
obj.setR47_num_loan_acc(rs.getBigDecimal("r47_num_loan_acc"));

// =========================
// R48 VALUES
// =========================
obj.setR48_num_by_inst_sec(rs.getString("r48_num_by_inst_sec"));
obj.setR48_num_depo(rs.getBigDecimal("r48_num_depo"));
obj.setR48_num_depo_acc(rs.getBigDecimal("r48_num_depo_acc"));
obj.setR48_num_borrowers(rs.getBigDecimal("r48_num_borrowers"));
obj.setR48_num_loan_acc(rs.getBigDecimal("r48_num_loan_acc"));

// =========================
// R49 VALUES
// =========================
obj.setR49_num_by_inst_sec(rs.getString("r49_num_by_inst_sec"));
obj.setR49_num_depo(rs.getBigDecimal("r49_num_depo"));
obj.setR49_num_depo_acc(rs.getBigDecimal("r49_num_depo_acc"));
obj.setR49_num_borrowers(rs.getBigDecimal("r49_num_borrowers"));
obj.setR49_num_loan_acc(rs.getBigDecimal("r49_num_loan_acc"));

// =========================
// R50 VALUES
// =========================
obj.setR50_num_by_inst_sec(rs.getString("r50_num_by_inst_sec"));
obj.setR50_num_depo(rs.getBigDecimal("r50_num_depo"));
obj.setR50_num_depo_acc(rs.getBigDecimal("r50_num_depo_acc"));
obj.setR50_num_borrowers(rs.getBigDecimal("r50_num_borrowers"));
obj.setR50_num_loan_acc(rs.getBigDecimal("r50_num_loan_acc"));

// =========================
// R51 VALUES
// =========================
obj.setR51_num_by_inst_sec(rs.getString("r51_num_by_inst_sec"));
obj.setR51_num_depo(rs.getBigDecimal("r51_num_depo"));
obj.setR51_num_depo_acc(rs.getBigDecimal("r51_num_depo_acc"));
obj.setR51_num_borrowers(rs.getBigDecimal("r51_num_borrowers"));
obj.setR51_num_loan_acc(rs.getBigDecimal("r51_num_loan_acc"));

// =========================
// R52 VALUES
// =========================
obj.setR52_num_by_inst_sec(rs.getString("r52_num_by_inst_sec"));
obj.setR52_num_depo(rs.getBigDecimal("r52_num_depo"));
obj.setR52_num_depo_acc(rs.getBigDecimal("r52_num_depo_acc"));
obj.setR52_num_borrowers(rs.getBigDecimal("r52_num_borrowers"));
obj.setR52_num_loan_acc(rs.getBigDecimal("r52_num_loan_acc"));

// =========================
// R53 VALUES
// =========================
obj.setR53_num_by_inst_sec(rs.getString("r53_num_by_inst_sec"));
obj.setR53_num_depo(rs.getBigDecimal("r53_num_depo"));
obj.setR53_num_depo_acc(rs.getBigDecimal("r53_num_depo_acc"));
obj.setR53_num_borrowers(rs.getBigDecimal("r53_num_borrowers"));
obj.setR53_num_loan_acc(rs.getBigDecimal("r53_num_loan_acc"));

// =========================
// R54 VALUES
// =========================
obj.setR54_num_by_inst_sec(rs.getString("r54_num_by_inst_sec"));
obj.setR54_num_depo(rs.getBigDecimal("r54_num_depo"));
obj.setR54_num_depo_acc(rs.getBigDecimal("r54_num_depo_acc"));
obj.setR54_num_borrowers(rs.getBigDecimal("r54_num_borrowers"));
obj.setR54_num_loan_acc(rs.getBigDecimal("r54_num_loan_acc"));

// =========================
// R55 VALUES
// =========================
obj.setR55_num_by_inst_sec(rs.getString("r55_num_by_inst_sec"));
obj.setR55_num_depo(rs.getBigDecimal("r55_num_depo"));
obj.setR55_num_depo_acc(rs.getBigDecimal("r55_num_depo_acc"));
obj.setR55_num_borrowers(rs.getBigDecimal("r55_num_borrowers"));
obj.setR55_num_loan_acc(rs.getBigDecimal("r55_num_loan_acc"));

// =========================
// R56 VALUES
// =========================
obj.setR56_num_by_inst_sec(rs.getString("r56_num_by_inst_sec"));
obj.setR56_num_depo(rs.getBigDecimal("r56_num_depo"));
obj.setR56_num_depo_acc(rs.getBigDecimal("r56_num_depo_acc"));
obj.setR56_num_borrowers(rs.getBigDecimal("r56_num_borrowers"));
obj.setR56_num_loan_acc(rs.getBigDecimal("r56_num_loan_acc"));

// =========================
// R57 VALUES
// =========================
obj.setR57_num_by_inst_sec(rs.getString("r57_num_by_inst_sec"));
obj.setR57_num_depo(rs.getBigDecimal("r57_num_depo"));
obj.setR57_num_depo_acc(rs.getBigDecimal("r57_num_depo_acc"));
obj.setR57_num_borrowers(rs.getBigDecimal("r57_num_borrowers"));
obj.setR57_num_loan_acc(rs.getBigDecimal("r57_num_loan_acc"));

// =========================
// R58 VALUES
// =========================
obj.setR58_num_by_inst_sec(rs.getString("r58_num_by_inst_sec"));
obj.setR58_num_depo(rs.getBigDecimal("r58_num_depo"));
obj.setR58_num_depo_acc(rs.getBigDecimal("r58_num_depo_acc"));
obj.setR58_num_borrowers(rs.getBigDecimal("r58_num_borrowers"));
obj.setR58_num_loan_acc(rs.getBigDecimal("r58_num_loan_acc"));

// =========================
// R59 VALUES
// =========================
obj.setR59_num_by_inst_sec(rs.getString("r59_num_by_inst_sec"));
obj.setR59_num_depo(rs.getBigDecimal("r59_num_depo"));
obj.setR59_num_depo_acc(rs.getBigDecimal("r59_num_depo_acc"));
obj.setR59_num_borrowers(rs.getBigDecimal("r59_num_borrowers"));
obj.setR59_num_loan_acc(rs.getBigDecimal("r59_num_loan_acc"));

// =========================
// R60 VALUES
// =========================
obj.setR60_num_by_inst_sec(rs.getString("r60_num_by_inst_sec"));
obj.setR60_num_depo(rs.getBigDecimal("r60_num_depo"));
obj.setR60_num_depo_acc(rs.getBigDecimal("r60_num_depo_acc"));
obj.setR60_num_borrowers(rs.getBigDecimal("r60_num_borrowers"));
obj.setR60_num_loan_acc(rs.getBigDecimal("r60_num_loan_acc"));

// =========================
// R61 VALUES
// =========================
obj.setR61_num_by_inst_sec(rs.getString("r61_num_by_inst_sec"));
obj.setR61_num_depo(rs.getBigDecimal("r61_num_depo"));
obj.setR61_num_depo_acc(rs.getBigDecimal("r61_num_depo_acc"));
obj.setR61_num_borrowers(rs.getBigDecimal("r61_num_borrowers"));
obj.setR61_num_loan_acc(rs.getBigDecimal("r61_num_loan_acc"));

// =========================
// R62 VALUES
// =========================
obj.setR62_num_by_inst_sec(rs.getString("r62_num_by_inst_sec"));
obj.setR62_num_depo(rs.getBigDecimal("r62_num_depo"));
obj.setR62_num_depo_acc(rs.getBigDecimal("r62_num_depo_acc"));
obj.setR62_num_borrowers(rs.getBigDecimal("r62_num_borrowers"));
obj.setR62_num_loan_acc(rs.getBigDecimal("r62_num_loan_acc"));

// =========================
// R63 VALUES
// =========================
obj.setR63_num_by_inst_sec(rs.getString("r63_num_by_inst_sec"));
obj.setR63_num_depo(rs.getBigDecimal("r63_num_depo"));
obj.setR63_num_depo_acc(rs.getBigDecimal("r63_num_depo_acc"));
obj.setR63_num_borrowers(rs.getBigDecimal("r63_num_borrowers"));
obj.setR63_num_loan_acc(rs.getBigDecimal("r63_num_loan_acc"));

// =========================
// R64 VALUES
// =========================
obj.setR64_num_by_inst_sec(rs.getString("r64_num_by_inst_sec"));
obj.setR64_num_depo(rs.getBigDecimal("r64_num_depo"));
obj.setR64_num_depo_acc(rs.getBigDecimal("r64_num_depo_acc"));
obj.setR64_num_borrowers(rs.getBigDecimal("r64_num_borrowers"));
obj.setR64_num_loan_acc(rs.getBigDecimal("r64_num_loan_acc"));	


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


public class Q_ATF_Archival_Summary_Entity {
	
	

		
	
		private String	r11_num_by_inst_sec;
	private BigDecimal	r11_num_depo;
	private BigDecimal	r11_num_depo_acc;
	private BigDecimal	r11_num_borrowers;
	private BigDecimal	r11_num_loan_acc;
	private String	r12_num_by_inst_sec;
	private BigDecimal	r12_num_depo;
	private BigDecimal	r12_num_depo_acc;
	private BigDecimal	r12_num_borrowers;
	private BigDecimal	r12_num_loan_acc;
	private String	r13_num_by_inst_sec;
	private BigDecimal	r13_num_depo;
	private BigDecimal	r13_num_depo_acc;
	private BigDecimal	r13_num_borrowers;
	private BigDecimal	r13_num_loan_acc;
	private String	r14_num_by_inst_sec;
	private BigDecimal	r14_num_depo;
	private BigDecimal	r14_num_depo_acc;
	private BigDecimal	r14_num_borrowers;
	private BigDecimal	r14_num_loan_acc;
	private String	r15_num_by_inst_sec;
	private BigDecimal	r15_num_depo;
	private BigDecimal	r15_num_depo_acc;
	private BigDecimal	r15_num_borrowers;
	private BigDecimal	r15_num_loan_acc;
	private String	r16_num_by_inst_sec;
	private BigDecimal	r16_num_depo;
	private BigDecimal	r16_num_depo_acc;
	private BigDecimal	r16_num_borrowers;
	private BigDecimal	r16_num_loan_acc;
	private String	r17_num_by_inst_sec;
	private BigDecimal	r17_num_depo;
	private BigDecimal	r17_num_depo_acc;
	private BigDecimal	r17_num_borrowers;
	private BigDecimal	r17_num_loan_acc;
	private String	r18_num_by_inst_sec;
	private BigDecimal	r18_num_depo;
	private BigDecimal	r18_num_depo_acc;
	private BigDecimal	r18_num_borrowers;
	private BigDecimal	r18_num_loan_acc;
	private String	r19_num_by_inst_sec;
	private BigDecimal	r19_num_depo;
	private BigDecimal	r19_num_depo_acc;
	private BigDecimal	r19_num_borrowers;
	private BigDecimal	r19_num_loan_acc;
	private String	r20_num_by_inst_sec;
	private BigDecimal	r20_num_depo;
	private BigDecimal	r20_num_depo_acc;
	private BigDecimal	r20_num_borrowers;
	private BigDecimal	r20_num_loan_acc;
	private String	r21_num_by_inst_sec;
	private BigDecimal	r21_num_depo;
	private BigDecimal	r21_num_depo_acc;
	private BigDecimal	r21_num_borrowers;
	private BigDecimal	r21_num_loan_acc;
	private String	r22_num_by_inst_sec;
	private BigDecimal	r22_num_depo;
	private BigDecimal	r22_num_depo_acc;
	private BigDecimal	r22_num_borrowers;
	private BigDecimal	r22_num_loan_acc;
	private String	r23_num_by_inst_sec;
	private BigDecimal	r23_num_depo;
	private BigDecimal	r23_num_depo_acc;
	private BigDecimal	r23_num_borrowers;
	private BigDecimal	r23_num_loan_acc;
	private String	r24_num_by_inst_sec;
	private BigDecimal	r24_num_depo;
	private BigDecimal	r24_num_depo_acc;
	private BigDecimal	r24_num_borrowers;
	private BigDecimal	r24_num_loan_acc;
	private String	r25_num_by_inst_sec;
	private BigDecimal	r25_num_depo;
	private BigDecimal	r25_num_depo_acc;
	private BigDecimal	r25_num_borrowers;
	private BigDecimal	r25_num_loan_acc;
	private String	r26_num_by_inst_sec;
	private BigDecimal	r26_num_depo;
	private BigDecimal	r26_num_depo_acc;
	private BigDecimal	r26_num_borrowers;
	private BigDecimal	r26_num_loan_acc;
	private String	r27_num_by_inst_sec;
	private BigDecimal	r27_num_depo;
	private BigDecimal	r27_num_depo_acc;
	private BigDecimal	r27_num_borrowers;
	private BigDecimal	r27_num_loan_acc;
	private String	r28_num_by_inst_sec;
	private BigDecimal	r28_num_depo;
	private BigDecimal	r28_num_depo_acc;
	private BigDecimal	r28_num_borrowers;
	private BigDecimal	r28_num_loan_acc;
	private String	r29_num_by_inst_sec;
	private BigDecimal	r29_num_depo;
	private BigDecimal	r29_num_depo_acc;
	private BigDecimal	r29_num_borrowers;
	private BigDecimal	r29_num_loan_acc;
	private String	r30_num_by_inst_sec;
	private BigDecimal	r30_num_depo;
	private BigDecimal	r30_num_depo_acc;
	private BigDecimal	r30_num_borrowers;
	private BigDecimal	r30_num_loan_acc;
	private String	r31_num_by_inst_sec;
	private BigDecimal	r31_num_depo;
	private BigDecimal	r31_num_depo_acc;
	private BigDecimal	r31_num_borrowers;
	private BigDecimal	r31_num_loan_acc;
	private String	r32_num_by_inst_sec;
	private BigDecimal	r32_num_depo;
	private BigDecimal	r32_num_depo_acc;
	private BigDecimal	r32_num_borrowers;
	private BigDecimal	r32_num_loan_acc;
	private String	r33_num_by_inst_sec;
	private BigDecimal	r33_num_depo;
	private BigDecimal	r33_num_depo_acc;
	private BigDecimal	r33_num_borrowers;
	private BigDecimal	r33_num_loan_acc;
	private String	r34_num_by_inst_sec;
	private BigDecimal	r34_num_depo;
	private BigDecimal	r34_num_depo_acc;
	private BigDecimal	r34_num_borrowers;
	private BigDecimal	r34_num_loan_acc;
	private String	r35_num_by_inst_sec;
	private BigDecimal	r35_num_depo;
	private BigDecimal	r35_num_depo_acc;
	private BigDecimal	r35_num_borrowers;
	private BigDecimal	r35_num_loan_acc;
	private String	r36_num_by_inst_sec;
	private BigDecimal	r36_num_depo;
	private BigDecimal	r36_num_depo_acc;
	private BigDecimal	r36_num_borrowers;
	private BigDecimal	r36_num_loan_acc;
	private String	r37_num_by_inst_sec;
	private BigDecimal	r37_num_depo;
	private BigDecimal	r37_num_depo_acc;
	private BigDecimal	r37_num_borrowers;
	private BigDecimal	r37_num_loan_acc;
	private String	r38_num_by_inst_sec;
	private BigDecimal	r38_num_depo;
	private BigDecimal	r38_num_depo_acc;
	private BigDecimal	r38_num_borrowers;
	private BigDecimal	r38_num_loan_acc;
	private String	r39_num_by_inst_sec;
	private BigDecimal	r39_num_depo;
	private BigDecimal	r39_num_depo_acc;
	private BigDecimal	r39_num_borrowers;
	private BigDecimal	r39_num_loan_acc;
	private String	r40_num_by_inst_sec;
	private BigDecimal	r40_num_depo;
	private BigDecimal	r40_num_depo_acc;
	private BigDecimal	r40_num_borrowers;
	private BigDecimal	r40_num_loan_acc;
	private String	r41_num_by_inst_sec;
	private BigDecimal	r41_num_depo;
	private BigDecimal	r41_num_depo_acc;
	private BigDecimal	r41_num_borrowers;
	private BigDecimal	r41_num_loan_acc;
	private String	r42_num_by_inst_sec;
	private BigDecimal	r42_num_depo;
	private BigDecimal	r42_num_depo_acc;
	private BigDecimal	r42_num_borrowers;
	private BigDecimal	r42_num_loan_acc;
	private String	r43_num_by_inst_sec;
	private BigDecimal	r43_num_depo;
	private BigDecimal	r43_num_depo_acc;
	private BigDecimal	r43_num_borrowers;
	private BigDecimal	r43_num_loan_acc;
	private String	r44_num_by_inst_sec;
	private BigDecimal	r44_num_depo;
	private BigDecimal	r44_num_depo_acc;
	private BigDecimal	r44_num_borrowers;
	private BigDecimal	r44_num_loan_acc;
	private String	r45_num_by_inst_sec;
	private BigDecimal	r45_num_depo;
	private BigDecimal	r45_num_depo_acc;
	private BigDecimal	r45_num_borrowers;
	private BigDecimal	r45_num_loan_acc;
	private String	r46_num_by_inst_sec;
	private BigDecimal	r46_num_depo;
	private BigDecimal	r46_num_depo_acc;
	private BigDecimal	r46_num_borrowers;
	private BigDecimal	r46_num_loan_acc;
	private String	r47_num_by_inst_sec;
	private BigDecimal	r47_num_depo;
	private BigDecimal	r47_num_depo_acc;
	private BigDecimal	r47_num_borrowers;
	private BigDecimal	r47_num_loan_acc;
	private String	r48_num_by_inst_sec;
	private BigDecimal	r48_num_depo;
	private BigDecimal	r48_num_depo_acc;
	private BigDecimal	r48_num_borrowers;
	private BigDecimal	r48_num_loan_acc;
	private String	r49_num_by_inst_sec;
	private BigDecimal	r49_num_depo;
	private BigDecimal	r49_num_depo_acc;
	private BigDecimal	r49_num_borrowers;
	private BigDecimal	r49_num_loan_acc;
	private String	r50_num_by_inst_sec;
	private BigDecimal	r50_num_depo;
	private BigDecimal	r50_num_depo_acc;
	private BigDecimal	r50_num_borrowers;
	private BigDecimal	r50_num_loan_acc;
	private String	r51_num_by_inst_sec;
	private BigDecimal	r51_num_depo;
	private BigDecimal	r51_num_depo_acc;
	private BigDecimal	r51_num_borrowers;
	private BigDecimal	r51_num_loan_acc;
	private String	r52_num_by_inst_sec;
	private BigDecimal	r52_num_depo;
	private BigDecimal	r52_num_depo_acc;
	private BigDecimal	r52_num_borrowers;
	private BigDecimal	r52_num_loan_acc;
	private String	r53_num_by_inst_sec;
	private BigDecimal	r53_num_depo;
	private BigDecimal	r53_num_depo_acc;
	private BigDecimal	r53_num_borrowers;
	private BigDecimal	r53_num_loan_acc;
	private String	r54_num_by_inst_sec;
	private BigDecimal	r54_num_depo;
	private BigDecimal	r54_num_depo_acc;
	private BigDecimal	r54_num_borrowers;
	private BigDecimal	r54_num_loan_acc;
	private String	r55_num_by_inst_sec;
	private BigDecimal	r55_num_depo;
	private BigDecimal	r55_num_depo_acc;
	private BigDecimal	r55_num_borrowers;
	private BigDecimal	r55_num_loan_acc;
	private String	r56_num_by_inst_sec;
	private BigDecimal	r56_num_depo;
	private BigDecimal	r56_num_depo_acc;
	private BigDecimal	r56_num_borrowers;
	private BigDecimal	r56_num_loan_acc;
	private String	r57_num_by_inst_sec;
	private BigDecimal	r57_num_depo;
	private BigDecimal	r57_num_depo_acc;
	private BigDecimal	r57_num_borrowers;
	private BigDecimal	r57_num_loan_acc;
	private String	r58_num_by_inst_sec;
	private BigDecimal	r58_num_depo;
	private BigDecimal	r58_num_depo_acc;
	private BigDecimal	r58_num_borrowers;
	private BigDecimal	r58_num_loan_acc;
	private String	r59_num_by_inst_sec;
	private BigDecimal	r59_num_depo;
	private BigDecimal	r59_num_depo_acc;
	private BigDecimal	r59_num_borrowers;
	private BigDecimal	r59_num_loan_acc;
	private String	r60_num_by_inst_sec;
	private BigDecimal	r60_num_depo;
	private BigDecimal	r60_num_depo_acc;
	private BigDecimal	r60_num_borrowers;
	private BigDecimal	r60_num_loan_acc;
	private String	r61_num_by_inst_sec;
	private BigDecimal	r61_num_depo;
	private BigDecimal	r61_num_depo_acc;
	private BigDecimal	r61_num_borrowers;
	private BigDecimal	r61_num_loan_acc;
	private String	r62_num_by_inst_sec;
	private BigDecimal	r62_num_depo;
	private BigDecimal	r62_num_depo_acc;
	private BigDecimal	r62_num_borrowers;
	private BigDecimal	r62_num_loan_acc;
	private String	r63_num_by_inst_sec;
	private BigDecimal	r63_num_depo;
	private BigDecimal	r63_num_depo_acc;
	private BigDecimal	r63_num_borrowers;
	private BigDecimal	r63_num_loan_acc;
	private String	r64_num_by_inst_sec;
	private BigDecimal	r64_num_depo;
	private BigDecimal	r64_num_depo_acc;
	private BigDecimal	r64_num_borrowers;
	private BigDecimal	r64_num_loan_acc;
	               
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



public String getR11_num_by_inst_sec() {
		return r11_num_by_inst_sec;
	}
	public void setR11_num_by_inst_sec(String r11_num_by_inst_sec) {
		this.r11_num_by_inst_sec = r11_num_by_inst_sec;
	}
	public BigDecimal getR11_num_depo() {
		return r11_num_depo;
	}
	public void setR11_num_depo(BigDecimal r11_num_depo) {
		this.r11_num_depo = r11_num_depo;
	}
	public BigDecimal getR11_num_depo_acc() {
		return r11_num_depo_acc;
	}
	public void setR11_num_depo_acc(BigDecimal r11_num_depo_acc) {
		this.r11_num_depo_acc = r11_num_depo_acc;
	}
	public BigDecimal getR11_num_borrowers() {
		return r11_num_borrowers;
	}
	public void setR11_num_borrowers(BigDecimal r11_num_borrowers) {
		this.r11_num_borrowers = r11_num_borrowers;
	}
	public BigDecimal getR11_num_loan_acc() {
		return r11_num_loan_acc;
	}
	public void setR11_num_loan_acc(BigDecimal r11_num_loan_acc) {
		this.r11_num_loan_acc = r11_num_loan_acc;
	}
	public String getR12_num_by_inst_sec() {
		return r12_num_by_inst_sec;
	}
	public void setR12_num_by_inst_sec(String r12_num_by_inst_sec) {
		this.r12_num_by_inst_sec = r12_num_by_inst_sec;
	}
	public BigDecimal getR12_num_depo() {
		return r12_num_depo;
	}
	public void setR12_num_depo(BigDecimal r12_num_depo) {
		this.r12_num_depo = r12_num_depo;
	}
	public BigDecimal getR12_num_depo_acc() {
		return r12_num_depo_acc;
	}
	public void setR12_num_depo_acc(BigDecimal r12_num_depo_acc) {
		this.r12_num_depo_acc = r12_num_depo_acc;
	}
	public BigDecimal getR12_num_borrowers() {
		return r12_num_borrowers;
	}
	public void setR12_num_borrowers(BigDecimal r12_num_borrowers) {
		this.r12_num_borrowers = r12_num_borrowers;
	}
	public BigDecimal getR12_num_loan_acc() {
		return r12_num_loan_acc;
	}
	public void setR12_num_loan_acc(BigDecimal r12_num_loan_acc) {
		this.r12_num_loan_acc = r12_num_loan_acc;
	}
	public String getR13_num_by_inst_sec() {
		return r13_num_by_inst_sec;
	}
	public void setR13_num_by_inst_sec(String r13_num_by_inst_sec) {
		this.r13_num_by_inst_sec = r13_num_by_inst_sec;
	}
	public BigDecimal getR13_num_depo() {
		return r13_num_depo;
	}
	public void setR13_num_depo(BigDecimal r13_num_depo) {
		this.r13_num_depo = r13_num_depo;
	}
	public BigDecimal getR13_num_depo_acc() {
		return r13_num_depo_acc;
	}
	public void setR13_num_depo_acc(BigDecimal r13_num_depo_acc) {
		this.r13_num_depo_acc = r13_num_depo_acc;
	}
	public BigDecimal getR13_num_borrowers() {
		return r13_num_borrowers;
	}
	public void setR13_num_borrowers(BigDecimal r13_num_borrowers) {
		this.r13_num_borrowers = r13_num_borrowers;
	}
	public BigDecimal getR13_num_loan_acc() {
		return r13_num_loan_acc;
	}
	public void setR13_num_loan_acc(BigDecimal r13_num_loan_acc) {
		this.r13_num_loan_acc = r13_num_loan_acc;
	}
	public String getR14_num_by_inst_sec() {
		return r14_num_by_inst_sec;
	}
	public void setR14_num_by_inst_sec(String r14_num_by_inst_sec) {
		this.r14_num_by_inst_sec = r14_num_by_inst_sec;
	}
	public BigDecimal getR14_num_depo() {
		return r14_num_depo;
	}
	public void setR14_num_depo(BigDecimal r14_num_depo) {
		this.r14_num_depo = r14_num_depo;
	}
	public BigDecimal getR14_num_depo_acc() {
		return r14_num_depo_acc;
	}
	public void setR14_num_depo_acc(BigDecimal r14_num_depo_acc) {
		this.r14_num_depo_acc = r14_num_depo_acc;
	}
	public BigDecimal getR14_num_borrowers() {
		return r14_num_borrowers;
	}
	public void setR14_num_borrowers(BigDecimal r14_num_borrowers) {
		this.r14_num_borrowers = r14_num_borrowers;
	}
	public BigDecimal getR14_num_loan_acc() {
		return r14_num_loan_acc;
	}
	public void setR14_num_loan_acc(BigDecimal r14_num_loan_acc) {
		this.r14_num_loan_acc = r14_num_loan_acc;
	}
	public String getR15_num_by_inst_sec() {
		return r15_num_by_inst_sec;
	}
	public void setR15_num_by_inst_sec(String r15_num_by_inst_sec) {
		this.r15_num_by_inst_sec = r15_num_by_inst_sec;
	}
	public BigDecimal getR15_num_depo() {
		return r15_num_depo;
	}
	public void setR15_num_depo(BigDecimal r15_num_depo) {
		this.r15_num_depo = r15_num_depo;
	}
	public BigDecimal getR15_num_depo_acc() {
		return r15_num_depo_acc;
	}
	public void setR15_num_depo_acc(BigDecimal r15_num_depo_acc) {
		this.r15_num_depo_acc = r15_num_depo_acc;
	}
	public BigDecimal getR15_num_borrowers() {
		return r15_num_borrowers;
	}
	public void setR15_num_borrowers(BigDecimal r15_num_borrowers) {
		this.r15_num_borrowers = r15_num_borrowers;
	}
	public BigDecimal getR15_num_loan_acc() {
		return r15_num_loan_acc;
	}
	public void setR15_num_loan_acc(BigDecimal r15_num_loan_acc) {
		this.r15_num_loan_acc = r15_num_loan_acc;
	}
	public String getR16_num_by_inst_sec() {
		return r16_num_by_inst_sec;
	}
	public void setR16_num_by_inst_sec(String r16_num_by_inst_sec) {
		this.r16_num_by_inst_sec = r16_num_by_inst_sec;
	}
	public BigDecimal getR16_num_depo() {
		return r16_num_depo;
	}
	public void setR16_num_depo(BigDecimal r16_num_depo) {
		this.r16_num_depo = r16_num_depo;
	}
	public BigDecimal getR16_num_depo_acc() {
		return r16_num_depo_acc;
	}
	public void setR16_num_depo_acc(BigDecimal r16_num_depo_acc) {
		this.r16_num_depo_acc = r16_num_depo_acc;
	}
	public BigDecimal getR16_num_borrowers() {
		return r16_num_borrowers;
	}
	public void setR16_num_borrowers(BigDecimal r16_num_borrowers) {
		this.r16_num_borrowers = r16_num_borrowers;
	}
	public BigDecimal getR16_num_loan_acc() {
		return r16_num_loan_acc;
	}
	public void setR16_num_loan_acc(BigDecimal r16_num_loan_acc) {
		this.r16_num_loan_acc = r16_num_loan_acc;
	}
	public String getR17_num_by_inst_sec() {
		return r17_num_by_inst_sec;
	}
	public void setR17_num_by_inst_sec(String r17_num_by_inst_sec) {
		this.r17_num_by_inst_sec = r17_num_by_inst_sec;
	}
	public BigDecimal getR17_num_depo() {
		return r17_num_depo;
	}
	public void setR17_num_depo(BigDecimal r17_num_depo) {
		this.r17_num_depo = r17_num_depo;
	}
	public BigDecimal getR17_num_depo_acc() {
		return r17_num_depo_acc;
	}
	public void setR17_num_depo_acc(BigDecimal r17_num_depo_acc) {
		this.r17_num_depo_acc = r17_num_depo_acc;
	}
	public BigDecimal getR17_num_borrowers() {
		return r17_num_borrowers;
	}
	public void setR17_num_borrowers(BigDecimal r17_num_borrowers) {
		this.r17_num_borrowers = r17_num_borrowers;
	}
	public BigDecimal getR17_num_loan_acc() {
		return r17_num_loan_acc;
	}
	public void setR17_num_loan_acc(BigDecimal r17_num_loan_acc) {
		this.r17_num_loan_acc = r17_num_loan_acc;
	}
	public String getR18_num_by_inst_sec() {
		return r18_num_by_inst_sec;
	}
	public void setR18_num_by_inst_sec(String r18_num_by_inst_sec) {
		this.r18_num_by_inst_sec = r18_num_by_inst_sec;
	}
	public BigDecimal getR18_num_depo() {
		return r18_num_depo;
	}
	public void setR18_num_depo(BigDecimal r18_num_depo) {
		this.r18_num_depo = r18_num_depo;
	}
	public BigDecimal getR18_num_depo_acc() {
		return r18_num_depo_acc;
	}
	public void setR18_num_depo_acc(BigDecimal r18_num_depo_acc) {
		this.r18_num_depo_acc = r18_num_depo_acc;
	}
	public BigDecimal getR18_num_borrowers() {
		return r18_num_borrowers;
	}
	public void setR18_num_borrowers(BigDecimal r18_num_borrowers) {
		this.r18_num_borrowers = r18_num_borrowers;
	}
	public BigDecimal getR18_num_loan_acc() {
		return r18_num_loan_acc;
	}
	public void setR18_num_loan_acc(BigDecimal r18_num_loan_acc) {
		this.r18_num_loan_acc = r18_num_loan_acc;
	}
	public String getR19_num_by_inst_sec() {
		return r19_num_by_inst_sec;
	}
	public void setR19_num_by_inst_sec(String r19_num_by_inst_sec) {
		this.r19_num_by_inst_sec = r19_num_by_inst_sec;
	}
	public BigDecimal getR19_num_depo() {
		return r19_num_depo;
	}
	public void setR19_num_depo(BigDecimal r19_num_depo) {
		this.r19_num_depo = r19_num_depo;
	}
	public BigDecimal getR19_num_depo_acc() {
		return r19_num_depo_acc;
	}
	public void setR19_num_depo_acc(BigDecimal r19_num_depo_acc) {
		this.r19_num_depo_acc = r19_num_depo_acc;
	}
	public BigDecimal getR19_num_borrowers() {
		return r19_num_borrowers;
	}
	public void setR19_num_borrowers(BigDecimal r19_num_borrowers) {
		this.r19_num_borrowers = r19_num_borrowers;
	}
	public BigDecimal getR19_num_loan_acc() {
		return r19_num_loan_acc;
	}
	public void setR19_num_loan_acc(BigDecimal r19_num_loan_acc) {
		this.r19_num_loan_acc = r19_num_loan_acc;
	}
	public String getR20_num_by_inst_sec() {
		return r20_num_by_inst_sec;
	}
	public void setR20_num_by_inst_sec(String r20_num_by_inst_sec) {
		this.r20_num_by_inst_sec = r20_num_by_inst_sec;
	}
	public BigDecimal getR20_num_depo() {
		return r20_num_depo;
	}
	public void setR20_num_depo(BigDecimal r20_num_depo) {
		this.r20_num_depo = r20_num_depo;
	}
	public BigDecimal getR20_num_depo_acc() {
		return r20_num_depo_acc;
	}
	public void setR20_num_depo_acc(BigDecimal r20_num_depo_acc) {
		this.r20_num_depo_acc = r20_num_depo_acc;
	}
	public BigDecimal getR20_num_borrowers() {
		return r20_num_borrowers;
	}
	public void setR20_num_borrowers(BigDecimal r20_num_borrowers) {
		this.r20_num_borrowers = r20_num_borrowers;
	}
	public BigDecimal getR20_num_loan_acc() {
		return r20_num_loan_acc;
	}
	public void setR20_num_loan_acc(BigDecimal r20_num_loan_acc) {
		this.r20_num_loan_acc = r20_num_loan_acc;
	}
	public String getR21_num_by_inst_sec() {
		return r21_num_by_inst_sec;
	}
	public void setR21_num_by_inst_sec(String r21_num_by_inst_sec) {
		this.r21_num_by_inst_sec = r21_num_by_inst_sec;
	}
	public BigDecimal getR21_num_depo() {
		return r21_num_depo;
	}
	public void setR21_num_depo(BigDecimal r21_num_depo) {
		this.r21_num_depo = r21_num_depo;
	}
	public BigDecimal getR21_num_depo_acc() {
		return r21_num_depo_acc;
	}
	public void setR21_num_depo_acc(BigDecimal r21_num_depo_acc) {
		this.r21_num_depo_acc = r21_num_depo_acc;
	}
	public BigDecimal getR21_num_borrowers() {
		return r21_num_borrowers;
	}
	public void setR21_num_borrowers(BigDecimal r21_num_borrowers) {
		this.r21_num_borrowers = r21_num_borrowers;
	}
	public BigDecimal getR21_num_loan_acc() {
		return r21_num_loan_acc;
	}
	public void setR21_num_loan_acc(BigDecimal r21_num_loan_acc) {
		this.r21_num_loan_acc = r21_num_loan_acc;
	}
	public String getR22_num_by_inst_sec() {
		return r22_num_by_inst_sec;
	}
	public void setR22_num_by_inst_sec(String r22_num_by_inst_sec) {
		this.r22_num_by_inst_sec = r22_num_by_inst_sec;
	}
	public BigDecimal getR22_num_depo() {
		return r22_num_depo;
	}
	public void setR22_num_depo(BigDecimal r22_num_depo) {
		this.r22_num_depo = r22_num_depo;
	}
	public BigDecimal getR22_num_depo_acc() {
		return r22_num_depo_acc;
	}
	public void setR22_num_depo_acc(BigDecimal r22_num_depo_acc) {
		this.r22_num_depo_acc = r22_num_depo_acc;
	}
	public BigDecimal getR22_num_borrowers() {
		return r22_num_borrowers;
	}
	public void setR22_num_borrowers(BigDecimal r22_num_borrowers) {
		this.r22_num_borrowers = r22_num_borrowers;
	}
	public BigDecimal getR22_num_loan_acc() {
		return r22_num_loan_acc;
	}
	public void setR22_num_loan_acc(BigDecimal r22_num_loan_acc) {
		this.r22_num_loan_acc = r22_num_loan_acc;
	}
	public String getR23_num_by_inst_sec() {
		return r23_num_by_inst_sec;
	}
	public void setR23_num_by_inst_sec(String r23_num_by_inst_sec) {
		this.r23_num_by_inst_sec = r23_num_by_inst_sec;
	}
	public BigDecimal getR23_num_depo() {
		return r23_num_depo;
	}
	public void setR23_num_depo(BigDecimal r23_num_depo) {
		this.r23_num_depo = r23_num_depo;
	}
	public BigDecimal getR23_num_depo_acc() {
		return r23_num_depo_acc;
	}
	public void setR23_num_depo_acc(BigDecimal r23_num_depo_acc) {
		this.r23_num_depo_acc = r23_num_depo_acc;
	}
	public BigDecimal getR23_num_borrowers() {
		return r23_num_borrowers;
	}
	public void setR23_num_borrowers(BigDecimal r23_num_borrowers) {
		this.r23_num_borrowers = r23_num_borrowers;
	}
	public BigDecimal getR23_num_loan_acc() {
		return r23_num_loan_acc;
	}
	public void setR23_num_loan_acc(BigDecimal r23_num_loan_acc) {
		this.r23_num_loan_acc = r23_num_loan_acc;
	}
	public String getR24_num_by_inst_sec() {
		return r24_num_by_inst_sec;
	}
	public void setR24_num_by_inst_sec(String r24_num_by_inst_sec) {
		this.r24_num_by_inst_sec = r24_num_by_inst_sec;
	}
	public BigDecimal getR24_num_depo() {
		return r24_num_depo;
	}
	public void setR24_num_depo(BigDecimal r24_num_depo) {
		this.r24_num_depo = r24_num_depo;
	}
	public BigDecimal getR24_num_depo_acc() {
		return r24_num_depo_acc;
	}
	public void setR24_num_depo_acc(BigDecimal r24_num_depo_acc) {
		this.r24_num_depo_acc = r24_num_depo_acc;
	}
	public BigDecimal getR24_num_borrowers() {
		return r24_num_borrowers;
	}
	public void setR24_num_borrowers(BigDecimal r24_num_borrowers) {
		this.r24_num_borrowers = r24_num_borrowers;
	}
	public BigDecimal getR24_num_loan_acc() {
		return r24_num_loan_acc;
	}
	public void setR24_num_loan_acc(BigDecimal r24_num_loan_acc) {
		this.r24_num_loan_acc = r24_num_loan_acc;
	}
	public String getR25_num_by_inst_sec() {
		return r25_num_by_inst_sec;
	}
	public void setR25_num_by_inst_sec(String r25_num_by_inst_sec) {
		this.r25_num_by_inst_sec = r25_num_by_inst_sec;
	}
	public BigDecimal getR25_num_depo() {
		return r25_num_depo;
	}
	public void setR25_num_depo(BigDecimal r25_num_depo) {
		this.r25_num_depo = r25_num_depo;
	}
	public BigDecimal getR25_num_depo_acc() {
		return r25_num_depo_acc;
	}
	public void setR25_num_depo_acc(BigDecimal r25_num_depo_acc) {
		this.r25_num_depo_acc = r25_num_depo_acc;
	}
	public BigDecimal getR25_num_borrowers() {
		return r25_num_borrowers;
	}
	public void setR25_num_borrowers(BigDecimal r25_num_borrowers) {
		this.r25_num_borrowers = r25_num_borrowers;
	}
	public BigDecimal getR25_num_loan_acc() {
		return r25_num_loan_acc;
	}
	public void setR25_num_loan_acc(BigDecimal r25_num_loan_acc) {
		this.r25_num_loan_acc = r25_num_loan_acc;
	}
	public String getR26_num_by_inst_sec() {
		return r26_num_by_inst_sec;
	}
	public void setR26_num_by_inst_sec(String r26_num_by_inst_sec) {
		this.r26_num_by_inst_sec = r26_num_by_inst_sec;
	}
	public BigDecimal getR26_num_depo() {
		return r26_num_depo;
	}
	public void setR26_num_depo(BigDecimal r26_num_depo) {
		this.r26_num_depo = r26_num_depo;
	}
	public BigDecimal getR26_num_depo_acc() {
		return r26_num_depo_acc;
	}
	public void setR26_num_depo_acc(BigDecimal r26_num_depo_acc) {
		this.r26_num_depo_acc = r26_num_depo_acc;
	}
	public BigDecimal getR26_num_borrowers() {
		return r26_num_borrowers;
	}
	public void setR26_num_borrowers(BigDecimal r26_num_borrowers) {
		this.r26_num_borrowers = r26_num_borrowers;
	}
	public BigDecimal getR26_num_loan_acc() {
		return r26_num_loan_acc;
	}
	public void setR26_num_loan_acc(BigDecimal r26_num_loan_acc) {
		this.r26_num_loan_acc = r26_num_loan_acc;
	}
	public String getR27_num_by_inst_sec() {
		return r27_num_by_inst_sec;
	}
	public void setR27_num_by_inst_sec(String r27_num_by_inst_sec) {
		this.r27_num_by_inst_sec = r27_num_by_inst_sec;
	}
	public BigDecimal getR27_num_depo() {
		return r27_num_depo;
	}
	public void setR27_num_depo(BigDecimal r27_num_depo) {
		this.r27_num_depo = r27_num_depo;
	}
	public BigDecimal getR27_num_depo_acc() {
		return r27_num_depo_acc;
	}
	public void setR27_num_depo_acc(BigDecimal r27_num_depo_acc) {
		this.r27_num_depo_acc = r27_num_depo_acc;
	}
	public BigDecimal getR27_num_borrowers() {
		return r27_num_borrowers;
	}
	public void setR27_num_borrowers(BigDecimal r27_num_borrowers) {
		this.r27_num_borrowers = r27_num_borrowers;
	}
	public BigDecimal getR27_num_loan_acc() {
		return r27_num_loan_acc;
	}
	public void setR27_num_loan_acc(BigDecimal r27_num_loan_acc) {
		this.r27_num_loan_acc = r27_num_loan_acc;
	}
	public String getR28_num_by_inst_sec() {
		return r28_num_by_inst_sec;
	}
	public void setR28_num_by_inst_sec(String r28_num_by_inst_sec) {
		this.r28_num_by_inst_sec = r28_num_by_inst_sec;
	}
	public BigDecimal getR28_num_depo() {
		return r28_num_depo;
	}
	public void setR28_num_depo(BigDecimal r28_num_depo) {
		this.r28_num_depo = r28_num_depo;
	}
	public BigDecimal getR28_num_depo_acc() {
		return r28_num_depo_acc;
	}
	public void setR28_num_depo_acc(BigDecimal r28_num_depo_acc) {
		this.r28_num_depo_acc = r28_num_depo_acc;
	}
	public BigDecimal getR28_num_borrowers() {
		return r28_num_borrowers;
	}
	public void setR28_num_borrowers(BigDecimal r28_num_borrowers) {
		this.r28_num_borrowers = r28_num_borrowers;
	}
	public BigDecimal getR28_num_loan_acc() {
		return r28_num_loan_acc;
	}
	public void setR28_num_loan_acc(BigDecimal r28_num_loan_acc) {
		this.r28_num_loan_acc = r28_num_loan_acc;
	}
	public String getR29_num_by_inst_sec() {
		return r29_num_by_inst_sec;
	}
	public void setR29_num_by_inst_sec(String r29_num_by_inst_sec) {
		this.r29_num_by_inst_sec = r29_num_by_inst_sec;
	}
	public BigDecimal getR29_num_depo() {
		return r29_num_depo;
	}
	public void setR29_num_depo(BigDecimal r29_num_depo) {
		this.r29_num_depo = r29_num_depo;
	}
	public BigDecimal getR29_num_depo_acc() {
		return r29_num_depo_acc;
	}
	public void setR29_num_depo_acc(BigDecimal r29_num_depo_acc) {
		this.r29_num_depo_acc = r29_num_depo_acc;
	}
	public BigDecimal getR29_num_borrowers() {
		return r29_num_borrowers;
	}
	public void setR29_num_borrowers(BigDecimal r29_num_borrowers) {
		this.r29_num_borrowers = r29_num_borrowers;
	}
	public BigDecimal getR29_num_loan_acc() {
		return r29_num_loan_acc;
	}
	public void setR29_num_loan_acc(BigDecimal r29_num_loan_acc) {
		this.r29_num_loan_acc = r29_num_loan_acc;
	}
	public String getR30_num_by_inst_sec() {
		return r30_num_by_inst_sec;
	}
	public void setR30_num_by_inst_sec(String r30_num_by_inst_sec) {
		this.r30_num_by_inst_sec = r30_num_by_inst_sec;
	}
	public BigDecimal getR30_num_depo() {
		return r30_num_depo;
	}
	public void setR30_num_depo(BigDecimal r30_num_depo) {
		this.r30_num_depo = r30_num_depo;
	}
	public BigDecimal getR30_num_depo_acc() {
		return r30_num_depo_acc;
	}
	public void setR30_num_depo_acc(BigDecimal r30_num_depo_acc) {
		this.r30_num_depo_acc = r30_num_depo_acc;
	}
	public BigDecimal getR30_num_borrowers() {
		return r30_num_borrowers;
	}
	public void setR30_num_borrowers(BigDecimal r30_num_borrowers) {
		this.r30_num_borrowers = r30_num_borrowers;
	}
	public BigDecimal getR30_num_loan_acc() {
		return r30_num_loan_acc;
	}
	public void setR30_num_loan_acc(BigDecimal r30_num_loan_acc) {
		this.r30_num_loan_acc = r30_num_loan_acc;
	}
	public String getR31_num_by_inst_sec() {
		return r31_num_by_inst_sec;
	}
	public void setR31_num_by_inst_sec(String r31_num_by_inst_sec) {
		this.r31_num_by_inst_sec = r31_num_by_inst_sec;
	}
	public BigDecimal getR31_num_depo() {
		return r31_num_depo;
	}
	public void setR31_num_depo(BigDecimal r31_num_depo) {
		this.r31_num_depo = r31_num_depo;
	}
	public BigDecimal getR31_num_depo_acc() {
		return r31_num_depo_acc;
	}
	public void setR31_num_depo_acc(BigDecimal r31_num_depo_acc) {
		this.r31_num_depo_acc = r31_num_depo_acc;
	}
	public BigDecimal getR31_num_borrowers() {
		return r31_num_borrowers;
	}
	public void setR31_num_borrowers(BigDecimal r31_num_borrowers) {
		this.r31_num_borrowers = r31_num_borrowers;
	}
	public BigDecimal getR31_num_loan_acc() {
		return r31_num_loan_acc;
	}
	public void setR31_num_loan_acc(BigDecimal r31_num_loan_acc) {
		this.r31_num_loan_acc = r31_num_loan_acc;
	}
	public String getR32_num_by_inst_sec() {
		return r32_num_by_inst_sec;
	}
	public void setR32_num_by_inst_sec(String r32_num_by_inst_sec) {
		this.r32_num_by_inst_sec = r32_num_by_inst_sec;
	}
	public BigDecimal getR32_num_depo() {
		return r32_num_depo;
	}
	public void setR32_num_depo(BigDecimal r32_num_depo) {
		this.r32_num_depo = r32_num_depo;
	}
	public BigDecimal getR32_num_depo_acc() {
		return r32_num_depo_acc;
	}
	public void setR32_num_depo_acc(BigDecimal r32_num_depo_acc) {
		this.r32_num_depo_acc = r32_num_depo_acc;
	}
	public BigDecimal getR32_num_borrowers() {
		return r32_num_borrowers;
	}
	public void setR32_num_borrowers(BigDecimal r32_num_borrowers) {
		this.r32_num_borrowers = r32_num_borrowers;
	}
	public BigDecimal getR32_num_loan_acc() {
		return r32_num_loan_acc;
	}
	public void setR32_num_loan_acc(BigDecimal r32_num_loan_acc) {
		this.r32_num_loan_acc = r32_num_loan_acc;
	}
	public String getR33_num_by_inst_sec() {
		return r33_num_by_inst_sec;
	}
	public void setR33_num_by_inst_sec(String r33_num_by_inst_sec) {
		this.r33_num_by_inst_sec = r33_num_by_inst_sec;
	}
	public BigDecimal getR33_num_depo() {
		return r33_num_depo;
	}
	public void setR33_num_depo(BigDecimal r33_num_depo) {
		this.r33_num_depo = r33_num_depo;
	}
	public BigDecimal getR33_num_depo_acc() {
		return r33_num_depo_acc;
	}
	public void setR33_num_depo_acc(BigDecimal r33_num_depo_acc) {
		this.r33_num_depo_acc = r33_num_depo_acc;
	}
	public BigDecimal getR33_num_borrowers() {
		return r33_num_borrowers;
	}
	public void setR33_num_borrowers(BigDecimal r33_num_borrowers) {
		this.r33_num_borrowers = r33_num_borrowers;
	}
	public BigDecimal getR33_num_loan_acc() {
		return r33_num_loan_acc;
	}
	public void setR33_num_loan_acc(BigDecimal r33_num_loan_acc) {
		this.r33_num_loan_acc = r33_num_loan_acc;
	}
	public String getR34_num_by_inst_sec() {
		return r34_num_by_inst_sec;
	}
	public void setR34_num_by_inst_sec(String r34_num_by_inst_sec) {
		this.r34_num_by_inst_sec = r34_num_by_inst_sec;
	}
	public BigDecimal getR34_num_depo() {
		return r34_num_depo;
	}
	public void setR34_num_depo(BigDecimal r34_num_depo) {
		this.r34_num_depo = r34_num_depo;
	}
	public BigDecimal getR34_num_depo_acc() {
		return r34_num_depo_acc;
	}
	public void setR34_num_depo_acc(BigDecimal r34_num_depo_acc) {
		this.r34_num_depo_acc = r34_num_depo_acc;
	}
	public BigDecimal getR34_num_borrowers() {
		return r34_num_borrowers;
	}
	public void setR34_num_borrowers(BigDecimal r34_num_borrowers) {
		this.r34_num_borrowers = r34_num_borrowers;
	}
	public BigDecimal getR34_num_loan_acc() {
		return r34_num_loan_acc;
	}
	public void setR34_num_loan_acc(BigDecimal r34_num_loan_acc) {
		this.r34_num_loan_acc = r34_num_loan_acc;
	}
	public String getR35_num_by_inst_sec() {
		return r35_num_by_inst_sec;
	}
	public void setR35_num_by_inst_sec(String r35_num_by_inst_sec) {
		this.r35_num_by_inst_sec = r35_num_by_inst_sec;
	}
	public BigDecimal getR35_num_depo() {
		return r35_num_depo;
	}
	public void setR35_num_depo(BigDecimal r35_num_depo) {
		this.r35_num_depo = r35_num_depo;
	}
	public BigDecimal getR35_num_depo_acc() {
		return r35_num_depo_acc;
	}
	public void setR35_num_depo_acc(BigDecimal r35_num_depo_acc) {
		this.r35_num_depo_acc = r35_num_depo_acc;
	}
	public BigDecimal getR35_num_borrowers() {
		return r35_num_borrowers;
	}
	public void setR35_num_borrowers(BigDecimal r35_num_borrowers) {
		this.r35_num_borrowers = r35_num_borrowers;
	}
	public BigDecimal getR35_num_loan_acc() {
		return r35_num_loan_acc;
	}
	public void setR35_num_loan_acc(BigDecimal r35_num_loan_acc) {
		this.r35_num_loan_acc = r35_num_loan_acc;
	}
	public String getR36_num_by_inst_sec() {
		return r36_num_by_inst_sec;
	}
	public void setR36_num_by_inst_sec(String r36_num_by_inst_sec) {
		this.r36_num_by_inst_sec = r36_num_by_inst_sec;
	}
	public BigDecimal getR36_num_depo() {
		return r36_num_depo;
	}
	public void setR36_num_depo(BigDecimal r36_num_depo) {
		this.r36_num_depo = r36_num_depo;
	}
	public BigDecimal getR36_num_depo_acc() {
		return r36_num_depo_acc;
	}
	public void setR36_num_depo_acc(BigDecimal r36_num_depo_acc) {
		this.r36_num_depo_acc = r36_num_depo_acc;
	}
	public BigDecimal getR36_num_borrowers() {
		return r36_num_borrowers;
	}
	public void setR36_num_borrowers(BigDecimal r36_num_borrowers) {
		this.r36_num_borrowers = r36_num_borrowers;
	}
	public BigDecimal getR36_num_loan_acc() {
		return r36_num_loan_acc;
	}
	public void setR36_num_loan_acc(BigDecimal r36_num_loan_acc) {
		this.r36_num_loan_acc = r36_num_loan_acc;
	}
	public String getR37_num_by_inst_sec() {
		return r37_num_by_inst_sec;
	}
	public void setR37_num_by_inst_sec(String r37_num_by_inst_sec) {
		this.r37_num_by_inst_sec = r37_num_by_inst_sec;
	}
	public BigDecimal getR37_num_depo() {
		return r37_num_depo;
	}
	public void setR37_num_depo(BigDecimal r37_num_depo) {
		this.r37_num_depo = r37_num_depo;
	}
	public BigDecimal getR37_num_depo_acc() {
		return r37_num_depo_acc;
	}
	public void setR37_num_depo_acc(BigDecimal r37_num_depo_acc) {
		this.r37_num_depo_acc = r37_num_depo_acc;
	}
	public BigDecimal getR37_num_borrowers() {
		return r37_num_borrowers;
	}
	public void setR37_num_borrowers(BigDecimal r37_num_borrowers) {
		this.r37_num_borrowers = r37_num_borrowers;
	}
	public BigDecimal getR37_num_loan_acc() {
		return r37_num_loan_acc;
	}
	public void setR37_num_loan_acc(BigDecimal r37_num_loan_acc) {
		this.r37_num_loan_acc = r37_num_loan_acc;
	}
	public String getR38_num_by_inst_sec() {
		return r38_num_by_inst_sec;
	}
	public void setR38_num_by_inst_sec(String r38_num_by_inst_sec) {
		this.r38_num_by_inst_sec = r38_num_by_inst_sec;
	}
	public BigDecimal getR38_num_depo() {
		return r38_num_depo;
	}
	public void setR38_num_depo(BigDecimal r38_num_depo) {
		this.r38_num_depo = r38_num_depo;
	}
	public BigDecimal getR38_num_depo_acc() {
		return r38_num_depo_acc;
	}
	public void setR38_num_depo_acc(BigDecimal r38_num_depo_acc) {
		this.r38_num_depo_acc = r38_num_depo_acc;
	}
	public BigDecimal getR38_num_borrowers() {
		return r38_num_borrowers;
	}
	public void setR38_num_borrowers(BigDecimal r38_num_borrowers) {
		this.r38_num_borrowers = r38_num_borrowers;
	}
	public BigDecimal getR38_num_loan_acc() {
		return r38_num_loan_acc;
	}
	public void setR38_num_loan_acc(BigDecimal r38_num_loan_acc) {
		this.r38_num_loan_acc = r38_num_loan_acc;
	}
	public String getR39_num_by_inst_sec() {
		return r39_num_by_inst_sec;
	}
	public void setR39_num_by_inst_sec(String r39_num_by_inst_sec) {
		this.r39_num_by_inst_sec = r39_num_by_inst_sec;
	}
	public BigDecimal getR39_num_depo() {
		return r39_num_depo;
	}
	public void setR39_num_depo(BigDecimal r39_num_depo) {
		this.r39_num_depo = r39_num_depo;
	}
	public BigDecimal getR39_num_depo_acc() {
		return r39_num_depo_acc;
	}
	public void setR39_num_depo_acc(BigDecimal r39_num_depo_acc) {
		this.r39_num_depo_acc = r39_num_depo_acc;
	}
	public BigDecimal getR39_num_borrowers() {
		return r39_num_borrowers;
	}
	public void setR39_num_borrowers(BigDecimal r39_num_borrowers) {
		this.r39_num_borrowers = r39_num_borrowers;
	}
	public BigDecimal getR39_num_loan_acc() {
		return r39_num_loan_acc;
	}
	public void setR39_num_loan_acc(BigDecimal r39_num_loan_acc) {
		this.r39_num_loan_acc = r39_num_loan_acc;
	}
	public String getR40_num_by_inst_sec() {
		return r40_num_by_inst_sec;
	}
	public void setR40_num_by_inst_sec(String r40_num_by_inst_sec) {
		this.r40_num_by_inst_sec = r40_num_by_inst_sec;
	}
	public BigDecimal getR40_num_depo() {
		return r40_num_depo;
	}
	public void setR40_num_depo(BigDecimal r40_num_depo) {
		this.r40_num_depo = r40_num_depo;
	}
	public BigDecimal getR40_num_depo_acc() {
		return r40_num_depo_acc;
	}
	public void setR40_num_depo_acc(BigDecimal r40_num_depo_acc) {
		this.r40_num_depo_acc = r40_num_depo_acc;
	}
	public BigDecimal getR40_num_borrowers() {
		return r40_num_borrowers;
	}
	public void setR40_num_borrowers(BigDecimal r40_num_borrowers) {
		this.r40_num_borrowers = r40_num_borrowers;
	}
	public BigDecimal getR40_num_loan_acc() {
		return r40_num_loan_acc;
	}
	public void setR40_num_loan_acc(BigDecimal r40_num_loan_acc) {
		this.r40_num_loan_acc = r40_num_loan_acc;
	}
	public String getR41_num_by_inst_sec() {
		return r41_num_by_inst_sec;
	}
	public void setR41_num_by_inst_sec(String r41_num_by_inst_sec) {
		this.r41_num_by_inst_sec = r41_num_by_inst_sec;
	}
	public BigDecimal getR41_num_depo() {
		return r41_num_depo;
	}
	public void setR41_num_depo(BigDecimal r41_num_depo) {
		this.r41_num_depo = r41_num_depo;
	}
	public BigDecimal getR41_num_depo_acc() {
		return r41_num_depo_acc;
	}
	public void setR41_num_depo_acc(BigDecimal r41_num_depo_acc) {
		this.r41_num_depo_acc = r41_num_depo_acc;
	}
	public BigDecimal getR41_num_borrowers() {
		return r41_num_borrowers;
	}
	public void setR41_num_borrowers(BigDecimal r41_num_borrowers) {
		this.r41_num_borrowers = r41_num_borrowers;
	}
	public BigDecimal getR41_num_loan_acc() {
		return r41_num_loan_acc;
	}
	public void setR41_num_loan_acc(BigDecimal r41_num_loan_acc) {
		this.r41_num_loan_acc = r41_num_loan_acc;
	}
	public String getR42_num_by_inst_sec() {
		return r42_num_by_inst_sec;
	}
	public void setR42_num_by_inst_sec(String r42_num_by_inst_sec) {
		this.r42_num_by_inst_sec = r42_num_by_inst_sec;
	}
	public BigDecimal getR42_num_depo() {
		return r42_num_depo;
	}
	public void setR42_num_depo(BigDecimal r42_num_depo) {
		this.r42_num_depo = r42_num_depo;
	}
	public BigDecimal getR42_num_depo_acc() {
		return r42_num_depo_acc;
	}
	public void setR42_num_depo_acc(BigDecimal r42_num_depo_acc) {
		this.r42_num_depo_acc = r42_num_depo_acc;
	}
	public BigDecimal getR42_num_borrowers() {
		return r42_num_borrowers;
	}
	public void setR42_num_borrowers(BigDecimal r42_num_borrowers) {
		this.r42_num_borrowers = r42_num_borrowers;
	}
	public BigDecimal getR42_num_loan_acc() {
		return r42_num_loan_acc;
	}
	public void setR42_num_loan_acc(BigDecimal r42_num_loan_acc) {
		this.r42_num_loan_acc = r42_num_loan_acc;
	}
	public String getR43_num_by_inst_sec() {
		return r43_num_by_inst_sec;
	}
	public void setR43_num_by_inst_sec(String r43_num_by_inst_sec) {
		this.r43_num_by_inst_sec = r43_num_by_inst_sec;
	}
	public BigDecimal getR43_num_depo() {
		return r43_num_depo;
	}
	public void setR43_num_depo(BigDecimal r43_num_depo) {
		this.r43_num_depo = r43_num_depo;
	}
	public BigDecimal getR43_num_depo_acc() {
		return r43_num_depo_acc;
	}
	public void setR43_num_depo_acc(BigDecimal r43_num_depo_acc) {
		this.r43_num_depo_acc = r43_num_depo_acc;
	}
	public BigDecimal getR43_num_borrowers() {
		return r43_num_borrowers;
	}
	public void setR43_num_borrowers(BigDecimal r43_num_borrowers) {
		this.r43_num_borrowers = r43_num_borrowers;
	}
	public BigDecimal getR43_num_loan_acc() {
		return r43_num_loan_acc;
	}
	public void setR43_num_loan_acc(BigDecimal r43_num_loan_acc) {
		this.r43_num_loan_acc = r43_num_loan_acc;
	}
	public String getR44_num_by_inst_sec() {
		return r44_num_by_inst_sec;
	}
	public void setR44_num_by_inst_sec(String r44_num_by_inst_sec) {
		this.r44_num_by_inst_sec = r44_num_by_inst_sec;
	}
	public BigDecimal getR44_num_depo() {
		return r44_num_depo;
	}
	public void setR44_num_depo(BigDecimal r44_num_depo) {
		this.r44_num_depo = r44_num_depo;
	}
	public BigDecimal getR44_num_depo_acc() {
		return r44_num_depo_acc;
	}
	public void setR44_num_depo_acc(BigDecimal r44_num_depo_acc) {
		this.r44_num_depo_acc = r44_num_depo_acc;
	}
	public BigDecimal getR44_num_borrowers() {
		return r44_num_borrowers;
	}
	public void setR44_num_borrowers(BigDecimal r44_num_borrowers) {
		this.r44_num_borrowers = r44_num_borrowers;
	}
	public BigDecimal getR44_num_loan_acc() {
		return r44_num_loan_acc;
	}
	public void setR44_num_loan_acc(BigDecimal r44_num_loan_acc) {
		this.r44_num_loan_acc = r44_num_loan_acc;
	}
	public String getR45_num_by_inst_sec() {
		return r45_num_by_inst_sec;
	}
	public void setR45_num_by_inst_sec(String r45_num_by_inst_sec) {
		this.r45_num_by_inst_sec = r45_num_by_inst_sec;
	}
	public BigDecimal getR45_num_depo() {
		return r45_num_depo;
	}
	public void setR45_num_depo(BigDecimal r45_num_depo) {
		this.r45_num_depo = r45_num_depo;
	}
	public BigDecimal getR45_num_depo_acc() {
		return r45_num_depo_acc;
	}
	public void setR45_num_depo_acc(BigDecimal r45_num_depo_acc) {
		this.r45_num_depo_acc = r45_num_depo_acc;
	}
	public BigDecimal getR45_num_borrowers() {
		return r45_num_borrowers;
	}
	public void setR45_num_borrowers(BigDecimal r45_num_borrowers) {
		this.r45_num_borrowers = r45_num_borrowers;
	}
	public BigDecimal getR45_num_loan_acc() {
		return r45_num_loan_acc;
	}
	public void setR45_num_loan_acc(BigDecimal r45_num_loan_acc) {
		this.r45_num_loan_acc = r45_num_loan_acc;
	}
	public String getR46_num_by_inst_sec() {
		return r46_num_by_inst_sec;
	}
	public void setR46_num_by_inst_sec(String r46_num_by_inst_sec) {
		this.r46_num_by_inst_sec = r46_num_by_inst_sec;
	}
	public BigDecimal getR46_num_depo() {
		return r46_num_depo;
	}
	public void setR46_num_depo(BigDecimal r46_num_depo) {
		this.r46_num_depo = r46_num_depo;
	}
	public BigDecimal getR46_num_depo_acc() {
		return r46_num_depo_acc;
	}
	public void setR46_num_depo_acc(BigDecimal r46_num_depo_acc) {
		this.r46_num_depo_acc = r46_num_depo_acc;
	}
	public BigDecimal getR46_num_borrowers() {
		return r46_num_borrowers;
	}
	public void setR46_num_borrowers(BigDecimal r46_num_borrowers) {
		this.r46_num_borrowers = r46_num_borrowers;
	}
	public BigDecimal getR46_num_loan_acc() {
		return r46_num_loan_acc;
	}
	public void setR46_num_loan_acc(BigDecimal r46_num_loan_acc) {
		this.r46_num_loan_acc = r46_num_loan_acc;
	}
	public String getR47_num_by_inst_sec() {
		return r47_num_by_inst_sec;
	}
	public void setR47_num_by_inst_sec(String r47_num_by_inst_sec) {
		this.r47_num_by_inst_sec = r47_num_by_inst_sec;
	}
	public BigDecimal getR47_num_depo() {
		return r47_num_depo;
	}
	public void setR47_num_depo(BigDecimal r47_num_depo) {
		this.r47_num_depo = r47_num_depo;
	}
	public BigDecimal getR47_num_depo_acc() {
		return r47_num_depo_acc;
	}
	public void setR47_num_depo_acc(BigDecimal r47_num_depo_acc) {
		this.r47_num_depo_acc = r47_num_depo_acc;
	}
	public BigDecimal getR47_num_borrowers() {
		return r47_num_borrowers;
	}
	public void setR47_num_borrowers(BigDecimal r47_num_borrowers) {
		this.r47_num_borrowers = r47_num_borrowers;
	}
	public BigDecimal getR47_num_loan_acc() {
		return r47_num_loan_acc;
	}
	public void setR47_num_loan_acc(BigDecimal r47_num_loan_acc) {
		this.r47_num_loan_acc = r47_num_loan_acc;
	}
	public String getR48_num_by_inst_sec() {
		return r48_num_by_inst_sec;
	}
	public void setR48_num_by_inst_sec(String r48_num_by_inst_sec) {
		this.r48_num_by_inst_sec = r48_num_by_inst_sec;
	}
	public BigDecimal getR48_num_depo() {
		return r48_num_depo;
	}
	public void setR48_num_depo(BigDecimal r48_num_depo) {
		this.r48_num_depo = r48_num_depo;
	}
	public BigDecimal getR48_num_depo_acc() {
		return r48_num_depo_acc;
	}
	public void setR48_num_depo_acc(BigDecimal r48_num_depo_acc) {
		this.r48_num_depo_acc = r48_num_depo_acc;
	}
	public BigDecimal getR48_num_borrowers() {
		return r48_num_borrowers;
	}
	public void setR48_num_borrowers(BigDecimal r48_num_borrowers) {
		this.r48_num_borrowers = r48_num_borrowers;
	}
	public BigDecimal getR48_num_loan_acc() {
		return r48_num_loan_acc;
	}
	public void setR48_num_loan_acc(BigDecimal r48_num_loan_acc) {
		this.r48_num_loan_acc = r48_num_loan_acc;
	}
	public String getR49_num_by_inst_sec() {
		return r49_num_by_inst_sec;
	}
	public void setR49_num_by_inst_sec(String r49_num_by_inst_sec) {
		this.r49_num_by_inst_sec = r49_num_by_inst_sec;
	}
	public BigDecimal getR49_num_depo() {
		return r49_num_depo;
	}
	public void setR49_num_depo(BigDecimal r49_num_depo) {
		this.r49_num_depo = r49_num_depo;
	}
	public BigDecimal getR49_num_depo_acc() {
		return r49_num_depo_acc;
	}
	public void setR49_num_depo_acc(BigDecimal r49_num_depo_acc) {
		this.r49_num_depo_acc = r49_num_depo_acc;
	}
	public BigDecimal getR49_num_borrowers() {
		return r49_num_borrowers;
	}
	public void setR49_num_borrowers(BigDecimal r49_num_borrowers) {
		this.r49_num_borrowers = r49_num_borrowers;
	}
	public BigDecimal getR49_num_loan_acc() {
		return r49_num_loan_acc;
	}
	public void setR49_num_loan_acc(BigDecimal r49_num_loan_acc) {
		this.r49_num_loan_acc = r49_num_loan_acc;
	}
	public String getR50_num_by_inst_sec() {
		return r50_num_by_inst_sec;
	}
	public void setR50_num_by_inst_sec(String r50_num_by_inst_sec) {
		this.r50_num_by_inst_sec = r50_num_by_inst_sec;
	}
	public BigDecimal getR50_num_depo() {
		return r50_num_depo;
	}
	public void setR50_num_depo(BigDecimal r50_num_depo) {
		this.r50_num_depo = r50_num_depo;
	}
	public BigDecimal getR50_num_depo_acc() {
		return r50_num_depo_acc;
	}
	public void setR50_num_depo_acc(BigDecimal r50_num_depo_acc) {
		this.r50_num_depo_acc = r50_num_depo_acc;
	}
	public BigDecimal getR50_num_borrowers() {
		return r50_num_borrowers;
	}
	public void setR50_num_borrowers(BigDecimal r50_num_borrowers) {
		this.r50_num_borrowers = r50_num_borrowers;
	}
	public BigDecimal getR50_num_loan_acc() {
		return r50_num_loan_acc;
	}
	public void setR50_num_loan_acc(BigDecimal r50_num_loan_acc) {
		this.r50_num_loan_acc = r50_num_loan_acc;
	}
	public String getR51_num_by_inst_sec() {
		return r51_num_by_inst_sec;
	}
	public void setR51_num_by_inst_sec(String r51_num_by_inst_sec) {
		this.r51_num_by_inst_sec = r51_num_by_inst_sec;
	}
	public BigDecimal getR51_num_depo() {
		return r51_num_depo;
	}
	public void setR51_num_depo(BigDecimal r51_num_depo) {
		this.r51_num_depo = r51_num_depo;
	}
	public BigDecimal getR51_num_depo_acc() {
		return r51_num_depo_acc;
	}
	public void setR51_num_depo_acc(BigDecimal r51_num_depo_acc) {
		this.r51_num_depo_acc = r51_num_depo_acc;
	}
	public BigDecimal getR51_num_borrowers() {
		return r51_num_borrowers;
	}
	public void setR51_num_borrowers(BigDecimal r51_num_borrowers) {
		this.r51_num_borrowers = r51_num_borrowers;
	}
	public BigDecimal getR51_num_loan_acc() {
		return r51_num_loan_acc;
	}
	public void setR51_num_loan_acc(BigDecimal r51_num_loan_acc) {
		this.r51_num_loan_acc = r51_num_loan_acc;
	}
	public String getR52_num_by_inst_sec() {
		return r52_num_by_inst_sec;
	}
	public void setR52_num_by_inst_sec(String r52_num_by_inst_sec) {
		this.r52_num_by_inst_sec = r52_num_by_inst_sec;
	}
	public BigDecimal getR52_num_depo() {
		return r52_num_depo;
	}
	public void setR52_num_depo(BigDecimal r52_num_depo) {
		this.r52_num_depo = r52_num_depo;
	}
	public BigDecimal getR52_num_depo_acc() {
		return r52_num_depo_acc;
	}
	public void setR52_num_depo_acc(BigDecimal r52_num_depo_acc) {
		this.r52_num_depo_acc = r52_num_depo_acc;
	}
	public BigDecimal getR52_num_borrowers() {
		return r52_num_borrowers;
	}
	public void setR52_num_borrowers(BigDecimal r52_num_borrowers) {
		this.r52_num_borrowers = r52_num_borrowers;
	}
	public BigDecimal getR52_num_loan_acc() {
		return r52_num_loan_acc;
	}
	public void setR52_num_loan_acc(BigDecimal r52_num_loan_acc) {
		this.r52_num_loan_acc = r52_num_loan_acc;
	}
	public String getR53_num_by_inst_sec() {
		return r53_num_by_inst_sec;
	}
	public void setR53_num_by_inst_sec(String r53_num_by_inst_sec) {
		this.r53_num_by_inst_sec = r53_num_by_inst_sec;
	}
	public BigDecimal getR53_num_depo() {
		return r53_num_depo;
	}
	public void setR53_num_depo(BigDecimal r53_num_depo) {
		this.r53_num_depo = r53_num_depo;
	}
	public BigDecimal getR53_num_depo_acc() {
		return r53_num_depo_acc;
	}
	public void setR53_num_depo_acc(BigDecimal r53_num_depo_acc) {
		this.r53_num_depo_acc = r53_num_depo_acc;
	}
	public BigDecimal getR53_num_borrowers() {
		return r53_num_borrowers;
	}
	public void setR53_num_borrowers(BigDecimal r53_num_borrowers) {
		this.r53_num_borrowers = r53_num_borrowers;
	}
	public BigDecimal getR53_num_loan_acc() {
		return r53_num_loan_acc;
	}
	public void setR53_num_loan_acc(BigDecimal r53_num_loan_acc) {
		this.r53_num_loan_acc = r53_num_loan_acc;
	}
	public String getR54_num_by_inst_sec() {
		return r54_num_by_inst_sec;
	}
	public void setR54_num_by_inst_sec(String r54_num_by_inst_sec) {
		this.r54_num_by_inst_sec = r54_num_by_inst_sec;
	}
	public BigDecimal getR54_num_depo() {
		return r54_num_depo;
	}
	public void setR54_num_depo(BigDecimal r54_num_depo) {
		this.r54_num_depo = r54_num_depo;
	}
	public BigDecimal getR54_num_depo_acc() {
		return r54_num_depo_acc;
	}
	public void setR54_num_depo_acc(BigDecimal r54_num_depo_acc) {
		this.r54_num_depo_acc = r54_num_depo_acc;
	}
	public BigDecimal getR54_num_borrowers() {
		return r54_num_borrowers;
	}
	public void setR54_num_borrowers(BigDecimal r54_num_borrowers) {
		this.r54_num_borrowers = r54_num_borrowers;
	}
	public BigDecimal getR54_num_loan_acc() {
		return r54_num_loan_acc;
	}
	public void setR54_num_loan_acc(BigDecimal r54_num_loan_acc) {
		this.r54_num_loan_acc = r54_num_loan_acc;
	}
	public String getR55_num_by_inst_sec() {
		return r55_num_by_inst_sec;
	}
	public void setR55_num_by_inst_sec(String r55_num_by_inst_sec) {
		this.r55_num_by_inst_sec = r55_num_by_inst_sec;
	}
	public BigDecimal getR55_num_depo() {
		return r55_num_depo;
	}
	public void setR55_num_depo(BigDecimal r55_num_depo) {
		this.r55_num_depo = r55_num_depo;
	}
	public BigDecimal getR55_num_depo_acc() {
		return r55_num_depo_acc;
	}
	public void setR55_num_depo_acc(BigDecimal r55_num_depo_acc) {
		this.r55_num_depo_acc = r55_num_depo_acc;
	}
	public BigDecimal getR55_num_borrowers() {
		return r55_num_borrowers;
	}
	public void setR55_num_borrowers(BigDecimal r55_num_borrowers) {
		this.r55_num_borrowers = r55_num_borrowers;
	}
	public BigDecimal getR55_num_loan_acc() {
		return r55_num_loan_acc;
	}
	public void setR55_num_loan_acc(BigDecimal r55_num_loan_acc) {
		this.r55_num_loan_acc = r55_num_loan_acc;
	}
	public String getR56_num_by_inst_sec() {
		return r56_num_by_inst_sec;
	}
	public void setR56_num_by_inst_sec(String r56_num_by_inst_sec) {
		this.r56_num_by_inst_sec = r56_num_by_inst_sec;
	}
	public BigDecimal getR56_num_depo() {
		return r56_num_depo;
	}
	public void setR56_num_depo(BigDecimal r56_num_depo) {
		this.r56_num_depo = r56_num_depo;
	}
	public BigDecimal getR56_num_depo_acc() {
		return r56_num_depo_acc;
	}
	public void setR56_num_depo_acc(BigDecimal r56_num_depo_acc) {
		this.r56_num_depo_acc = r56_num_depo_acc;
	}
	public BigDecimal getR56_num_borrowers() {
		return r56_num_borrowers;
	}
	public void setR56_num_borrowers(BigDecimal r56_num_borrowers) {
		this.r56_num_borrowers = r56_num_borrowers;
	}
	public BigDecimal getR56_num_loan_acc() {
		return r56_num_loan_acc;
	}
	public void setR56_num_loan_acc(BigDecimal r56_num_loan_acc) {
		this.r56_num_loan_acc = r56_num_loan_acc;
	}
	public String getR57_num_by_inst_sec() {
		return r57_num_by_inst_sec;
	}
	public void setR57_num_by_inst_sec(String r57_num_by_inst_sec) {
		this.r57_num_by_inst_sec = r57_num_by_inst_sec;
	}
	public BigDecimal getR57_num_depo() {
		return r57_num_depo;
	}
	public void setR57_num_depo(BigDecimal r57_num_depo) {
		this.r57_num_depo = r57_num_depo;
	}
	public BigDecimal getR57_num_depo_acc() {
		return r57_num_depo_acc;
	}
	public void setR57_num_depo_acc(BigDecimal r57_num_depo_acc) {
		this.r57_num_depo_acc = r57_num_depo_acc;
	}
	public BigDecimal getR57_num_borrowers() {
		return r57_num_borrowers;
	}
	public void setR57_num_borrowers(BigDecimal r57_num_borrowers) {
		this.r57_num_borrowers = r57_num_borrowers;
	}
	public BigDecimal getR57_num_loan_acc() {
		return r57_num_loan_acc;
	}
	public void setR57_num_loan_acc(BigDecimal r57_num_loan_acc) {
		this.r57_num_loan_acc = r57_num_loan_acc;
	}
	public String getR58_num_by_inst_sec() {
		return r58_num_by_inst_sec;
	}
	public void setR58_num_by_inst_sec(String r58_num_by_inst_sec) {
		this.r58_num_by_inst_sec = r58_num_by_inst_sec;
	}
	public BigDecimal getR58_num_depo() {
		return r58_num_depo;
	}
	public void setR58_num_depo(BigDecimal r58_num_depo) {
		this.r58_num_depo = r58_num_depo;
	}
	public BigDecimal getR58_num_depo_acc() {
		return r58_num_depo_acc;
	}
	public void setR58_num_depo_acc(BigDecimal r58_num_depo_acc) {
		this.r58_num_depo_acc = r58_num_depo_acc;
	}
	public BigDecimal getR58_num_borrowers() {
		return r58_num_borrowers;
	}
	public void setR58_num_borrowers(BigDecimal r58_num_borrowers) {
		this.r58_num_borrowers = r58_num_borrowers;
	}
	public BigDecimal getR58_num_loan_acc() {
		return r58_num_loan_acc;
	}
	public void setR58_num_loan_acc(BigDecimal r58_num_loan_acc) {
		this.r58_num_loan_acc = r58_num_loan_acc;
	}
	public String getR59_num_by_inst_sec() {
		return r59_num_by_inst_sec;
	}
	public void setR59_num_by_inst_sec(String r59_num_by_inst_sec) {
		this.r59_num_by_inst_sec = r59_num_by_inst_sec;
	}
	public BigDecimal getR59_num_depo() {
		return r59_num_depo;
	}
	public void setR59_num_depo(BigDecimal r59_num_depo) {
		this.r59_num_depo = r59_num_depo;
	}
	public BigDecimal getR59_num_depo_acc() {
		return r59_num_depo_acc;
	}
	public void setR59_num_depo_acc(BigDecimal r59_num_depo_acc) {
		this.r59_num_depo_acc = r59_num_depo_acc;
	}
	public BigDecimal getR59_num_borrowers() {
		return r59_num_borrowers;
	}
	public void setR59_num_borrowers(BigDecimal r59_num_borrowers) {
		this.r59_num_borrowers = r59_num_borrowers;
	}
	public BigDecimal getR59_num_loan_acc() {
		return r59_num_loan_acc;
	}
	public void setR59_num_loan_acc(BigDecimal r59_num_loan_acc) {
		this.r59_num_loan_acc = r59_num_loan_acc;
	}
	public String getR60_num_by_inst_sec() {
		return r60_num_by_inst_sec;
	}
	public void setR60_num_by_inst_sec(String r60_num_by_inst_sec) {
		this.r60_num_by_inst_sec = r60_num_by_inst_sec;
	}
	public BigDecimal getR60_num_depo() {
		return r60_num_depo;
	}
	public void setR60_num_depo(BigDecimal r60_num_depo) {
		this.r60_num_depo = r60_num_depo;
	}
	public BigDecimal getR60_num_depo_acc() {
		return r60_num_depo_acc;
	}
	public void setR60_num_depo_acc(BigDecimal r60_num_depo_acc) {
		this.r60_num_depo_acc = r60_num_depo_acc;
	}
	public BigDecimal getR60_num_borrowers() {
		return r60_num_borrowers;
	}
	public void setR60_num_borrowers(BigDecimal r60_num_borrowers) {
		this.r60_num_borrowers = r60_num_borrowers;
	}
	public BigDecimal getR60_num_loan_acc() {
		return r60_num_loan_acc;
	}
	public void setR60_num_loan_acc(BigDecimal r60_num_loan_acc) {
		this.r60_num_loan_acc = r60_num_loan_acc;
	}
	public String getR61_num_by_inst_sec() {
		return r61_num_by_inst_sec;
	}
	public void setR61_num_by_inst_sec(String r61_num_by_inst_sec) {
		this.r61_num_by_inst_sec = r61_num_by_inst_sec;
	}
	public BigDecimal getR61_num_depo() {
		return r61_num_depo;
	}
	public void setR61_num_depo(BigDecimal r61_num_depo) {
		this.r61_num_depo = r61_num_depo;
	}
	public BigDecimal getR61_num_depo_acc() {
		return r61_num_depo_acc;
	}
	public void setR61_num_depo_acc(BigDecimal r61_num_depo_acc) {
		this.r61_num_depo_acc = r61_num_depo_acc;
	}
	public BigDecimal getR61_num_borrowers() {
		return r61_num_borrowers;
	}
	public void setR61_num_borrowers(BigDecimal r61_num_borrowers) {
		this.r61_num_borrowers = r61_num_borrowers;
	}
	public BigDecimal getR61_num_loan_acc() {
		return r61_num_loan_acc;
	}
	public void setR61_num_loan_acc(BigDecimal r61_num_loan_acc) {
		this.r61_num_loan_acc = r61_num_loan_acc;
	}
	public String getR62_num_by_inst_sec() {
		return r62_num_by_inst_sec;
	}
	public void setR62_num_by_inst_sec(String r62_num_by_inst_sec) {
		this.r62_num_by_inst_sec = r62_num_by_inst_sec;
	}
	public BigDecimal getR62_num_depo() {
		return r62_num_depo;
	}
	public void setR62_num_depo(BigDecimal r62_num_depo) {
		this.r62_num_depo = r62_num_depo;
	}
	public BigDecimal getR62_num_depo_acc() {
		return r62_num_depo_acc;
	}
	public void setR62_num_depo_acc(BigDecimal r62_num_depo_acc) {
		this.r62_num_depo_acc = r62_num_depo_acc;
	}
	public BigDecimal getR62_num_borrowers() {
		return r62_num_borrowers;
	}
	public void setR62_num_borrowers(BigDecimal r62_num_borrowers) {
		this.r62_num_borrowers = r62_num_borrowers;
	}
	public BigDecimal getR62_num_loan_acc() {
		return r62_num_loan_acc;
	}
	public void setR62_num_loan_acc(BigDecimal r62_num_loan_acc) {
		this.r62_num_loan_acc = r62_num_loan_acc;
	}
	public String getR63_num_by_inst_sec() {
		return r63_num_by_inst_sec;
	}
	public void setR63_num_by_inst_sec(String r63_num_by_inst_sec) {
		this.r63_num_by_inst_sec = r63_num_by_inst_sec;
	}
	public BigDecimal getR63_num_depo() {
		return r63_num_depo;
	}
	public void setR63_num_depo(BigDecimal r63_num_depo) {
		this.r63_num_depo = r63_num_depo;
	}
	public BigDecimal getR63_num_depo_acc() {
		return r63_num_depo_acc;
	}
	public void setR63_num_depo_acc(BigDecimal r63_num_depo_acc) {
		this.r63_num_depo_acc = r63_num_depo_acc;
	}
	public BigDecimal getR63_num_borrowers() {
		return r63_num_borrowers;
	}
	public void setR63_num_borrowers(BigDecimal r63_num_borrowers) {
		this.r63_num_borrowers = r63_num_borrowers;
	}
	public BigDecimal getR63_num_loan_acc() {
		return r63_num_loan_acc;
	}
	public void setR63_num_loan_acc(BigDecimal r63_num_loan_acc) {
		this.r63_num_loan_acc = r63_num_loan_acc;
	}
	public String getR64_num_by_inst_sec() {
		return r64_num_by_inst_sec;
	}
	public void setR64_num_by_inst_sec(String r64_num_by_inst_sec) {
		this.r64_num_by_inst_sec = r64_num_by_inst_sec;
	}
	public BigDecimal getR64_num_depo() {
		return r64_num_depo;
	}
	public void setR64_num_depo(BigDecimal r64_num_depo) {
		this.r64_num_depo = r64_num_depo;
	}
	public BigDecimal getR64_num_depo_acc() {
		return r64_num_depo_acc;
	}
	public void setR64_num_depo_acc(BigDecimal r64_num_depo_acc) {
		this.r64_num_depo_acc = r64_num_depo_acc;
	}
	public BigDecimal getR64_num_borrowers() {
		return r64_num_borrowers;
	}
	public void setR64_num_borrowers(BigDecimal r64_num_borrowers) {
		this.r64_num_borrowers = r64_num_borrowers;
	}
	public BigDecimal getR64_num_loan_acc() {
		return r64_num_loan_acc;
	}
	public void setR64_num_loan_acc(BigDecimal r64_num_loan_acc) {
		this.r64_num_loan_acc = r64_num_loan_acc;
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
// DETAIL ENTITY 
// =====================================================	

public class Q_ATF_Detail_RowMapper implements RowMapper<Q_ATF_Detail_Entity> {

    @Override
    public Q_ATF_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        Q_ATF_Detail_Entity obj = new Q_ATF_Detail_Entity();

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
		 obj.setReportAddlCriteria_2(rs.getString("REPORT_ADDL_CRITERIA_2"));
		  obj.setReportAddlCriteria_3(rs.getString("REPORT_ADDL_CRITERIA_3"));
		   obj.setReportAddlCriteria_4(rs.getString("REPORT_ADDL_CRITERIA_4"));
        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

        // =========================
        // AMOUNT
        // =========================
        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
		obj.setSanctionLimit(rs.getString("SANCTION_LIMIT"));

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

public class Q_ATF_Detail_Entity {

	private Long sno;
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

  @Column(name = "REPORT_ADDL_CRITERIA_3")
  private String reportAddlCriteria_3;
  
  @Column(name = "REPORT_ADDL_CRITERIA_4")
  private String reportAddlCriteria_4;
  
  @Column(name = "REPORT_REMARKS")
  private String reportRemarks;
  
  @Column(name = "SANCTION_LIMIT")
  private String sanctionLimit;

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
  
  public Long getSno() {
		return sno;
	}

	public void setSno(Long sno) {
		this.sno = sno;
	}
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

public String getReportAddlCriteria_3() {
	return reportAddlCriteria_3;
}

public void setReportAddlCriteria_3(String reportAddlCriteria_3) {
	this.reportAddlCriteria_3 = reportAddlCriteria_3;
}

public String getReportAddlCriteria_4() {
	return reportAddlCriteria_4;
}

public void setReportAddlCriteria_4(String reportAddlCriteria_4) {
	this.reportAddlCriteria_4 = reportAddlCriteria_4;
}

public String getReportRemarks() {
	return reportRemarks;
}

public void setReportRemarks(String reportRemarks) {
	this.reportRemarks = reportRemarks;
}

public String getSanctionLimit() {
	return sanctionLimit;
}

public void setSanctionLimit(String sanctionLimit) {
	this.sanctionLimit = sanctionLimit;
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


public class Q_ATF_Archival_Detail_RowMapper 
        implements RowMapper<Q_ATF_Archival_Detail_Entity> {

    @Override
    public Q_ATF_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        Q_ATF_Archival_Detail_Entity obj = new Q_ATF_Archival_Detail_Entity();

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
		obj.setSanctionLimit(rs.getString("SANCTION_LIMIT"));

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

public class Q_ATF_Archival_Detail_Entity {
	
	private Long sno;
	
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

	  @Column(name = "REPORT_ADDL_CRITERIA_3")
	  private String reportAddlCriteria_3;
	  
	  @Column(name = "REPORT_ADDL_CRITERIA_4")
	  private String reportAddlCriteria_4;
	  
	  @Column(name = "REPORT_REMARKS")
	  private String reportRemarks;
	  
	  @Column(name = "SANCTION_LIMIT")
	  private String sanctionLimit;

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
	

	  public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}
	
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

public String getReportAddlCriteria_3() {
	return reportAddlCriteria_3;
}

public void setReportAddlCriteria_3(String reportAddlCriteria_3) {
	this.reportAddlCriteria_3 = reportAddlCriteria_3;
}

public String getReportAddlCriteria_4() {
	return reportAddlCriteria_4;
}

public void setReportAddlCriteria_4(String reportAddlCriteria_4) {
	this.reportAddlCriteria_4 = reportAddlCriteria_4;
}

public String getReportRemarks() {
	return reportRemarks;
}

public void setReportRemarks(String reportRemarks) {
	this.reportRemarks = reportRemarks;
}

public String getSanctionLimit() {
	return sanctionLimit;
}

public void setSanctionLimit(String sanctionLimit) {
	this.sanctionLimit = sanctionLimit;
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
@Autowired
UserProfileRep userProfileRep;

//=====================================================
// MODEL AND VIEW METHOD summary Q_ATF
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
 public ModelAndView getQ_ATFView(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,     // kept but not used
	        Pageable pageable,
	        String type,
	        BigDecimal version,HttpServletRequest req1,Model md) {

	    ModelAndView mv = new ModelAndView();
	    
	    String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);


	    System.out.println("Q_ATF View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	 // =====================================================
	 // ARCHIVAL + RESUB MODE
	 // =====================================================

	 if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

	     List<Q_ATF_Archival_Summary_Entity> T1Master = new ArrayList<>();

	     try {

	         Date dt = dateformat.parse(todate);

	         // SUMMARY ARCHIVAL
	         T1Master = getDataByDateListArchival(dt, version);

	         System.out.println(type + " Summary size = " + T1Master.size());

	         mv.addObject("report_date", dateformat.format(dt));

	         System.out.println("getishighestversion(dt, version) : "
	                 + getishighestversion(dt, version));

	         mv.addObject("allowdetail", getishighestversion(dt, version));

	     } catch (Exception e) {
	         e.printStackTrace();
	     }

	     mv.addObject("reportsummary", T1Master);
	 }
	    // =====================================================
	    // NORMAL MODE
	    // =====================================================

	    else {

	        List<Q_ATF_Summary_Entity> T1Master = new ArrayList<>();
	       

	        try {
	            Date dt = dateformat.parse(todate);

	            // SUMMARY NORMAL
	            T1Master = getDataByDateList(dt);

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

	    mv.setViewName("BRRS/Q_ATF");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getQ_ATFcurrentDtl(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String filter,
	        String type,
	        String version,HttpServletRequest req1,Model md) {

	    ModelAndView mv = new ModelAndView();
	    
	    String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);


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
	     // ARCHIVAL / RESUB MODE
	     // =====================================================
	     if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

	         System.out.println(type + " DETAIL MODE");

	         List<Q_ATF_Archival_Detail_Entity> detailList;

	         if (reportLabel != null && reportAddlCriteria1 != null) {

	             detailList = GetArchivalDataByRowIdAndColumnId(
	                     reportLabel,
	                     reportAddlCriteria1,
	                     parsedDate,
	                     version);

	         } else {

	             detailList = getarchivaldetaildatabydateList(
	                     parsedDate,
	                     version);
	         }

	         mv.addObject("reportdetails", detailList);
	         mv.addObject("reportmaster12", detailList);

	         System.out.println(type + " DETAIL COUNT: " + detailList.size());
	     }
	        // =====================================================
	        // CURRENT MODE
	        // =====================================================
	        else {

	            List<Q_ATF_Detail_Entity> currentDetailList;

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

	    mv.setViewName("BRRS/Q_ATF");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
	
//=====================================================
// MODEL AND VIEW METHOD ARCHIVAL VIEW
//=====================================================

public List<Object[]> getQ_ATFArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<Q_ATF_Archival_Summary_Entity> repoData =
						getarchivaldatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (Q_ATF_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					Q_ATF_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  Q_ATF  Archival data: " + e.getMessage());
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

public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {

    ModelAndView mv = new ModelAndView("BRRS/Q_ATF");

    System.out.println("SNO : " + SNO);
    System.out.println("Type : " + type);

    if (SNO != null) {

        if ("RESUB".equals(type)) {

            System.out.println("Inside RESUB FETCH");

            Q_ATF_Detail_Entity qatfEntity = findBySnoArch(SNO);

            if (qatfEntity != null && qatfEntity.getReportDate() != null) {
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
                        .format(qatfEntity.getReportDate());
                mv.addObject("asondate", formattedDate);
            }

            mv.addObject("qatfData", qatfEntity);

        } else {

            Q_ATF_Detail_Entity qatfEntity = findBySno(SNO);

            if (qatfEntity != null && qatfEntity.getReportDate() != null) {
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
                        .format(qatfEntity.getReportDate());
                mv.addObject("asondate", formattedDate);
            }

            mv.addObject("qatfData", qatfEntity);
        }
    }

    mv.addObject("type", type);
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

        String Sno = request.getParameter("sno");
        String acctBalanceInpula = request.getParameter("acctBalanceInpula");
        String acctName = request.getParameter("acctName");
        String reportDateStr = request.getParameter("reportDate");

        System.out.println("SNO : " + Sno);

        String type = request.getParameter("type");
        String entry = (request.getParameter("entry") != null)
                ? request.getParameter("entry")
                : "YES";

        Q_ATF_Detail_Entity existing = null;

        System.out.println("Type : " + type);

        if ("RESUB".equals(type)) {
            existing = findBySnoArch(Sno);
        } else {
            existing = findBySno(Sno);
        }

        Q_ATF_Detail_Entity oldcopy = new Q_ATF_Detail_Entity();
        BeanUtils.copyProperties(existing, oldcopy);

        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Record not found for update.");
        }

        boolean isChanged = false;

        // Account Name
        if (acctName != null && !acctName.isEmpty()) {

            if (existing.getAcctName() == null ||
                    !existing.getAcctName().equals(acctName)) {

                existing.setAcctName(acctName);
                isChanged = true;
            }
        }

        // Account Balance
        if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {

            BigDecimal newBalance = new BigDecimal(acctBalanceInpula);

            if (existing.getAcctBalanceInpula() == null ||
                    existing.getAcctBalanceInpula().compareTo(newBalance) != 0) {

                existing.setAcctBalanceInpula(newBalance);
                isChanged = true;
            }
        }

        if (isChanged) {

            String sql;

            if ("RESUB".equals(type)) {

                sql = "UPDATE BRRS_Q_ATF_ARCHIVALTABLE_DETAIL "
                        + "SET ACCT_NAME = ?, "
                        + "ACCT_BALANCE_IN_PULA = ? "
                        + "WHERE SNO = ?";

            } else {

                sql = "UPDATE BRRS_Q_ATF_DETAILTABLE "
                        + "SET ACCT_NAME = ?, "
                        + "ACCT_BALANCE_IN_PULA = ? "
                        + "WHERE SNO = ?";
            }

            jdbcTemplate.update(
                    sql,
                    existing.getAcctName(),
                    existing.getAcctBalanceInpula(),
                    Sno
            );

            if ("RESUB".equals(type)) {
                auditService.compareEntitiesmanual(
                        oldcopy,
                        existing,
                        Sno,
                        "Q ATF Archival Screen",
                        "BRRS_Q_ATF_ARCHIVALTABLE_DETAIL");
            } else {
                auditService.compareEntitiesmanual(
                        oldcopy,
                        existing,
                        Sno,
                        "Q ATF Screen",
                        "BRRS_Q_ATF_DETAILTABLE");
            }

            System.out.println("Record updated successfully.");

            Run_Q_ATF_Procudure(reportDateStr, type, entry);

            if ("RESUB".equals(type) && "NO".equals(entry)) {
                return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
            }

            return ResponseEntity.ok("Record updated successfully!");
        }

        return ResponseEntity.ok("No changes were made.");

    } catch (Exception e) {

        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating record: " + e.getMessage());
    }
}

@Transactional
public ResponseEntity<?> callregenprocedure(HttpServletRequest request) {
    try {

        Run_Q_ATF_Procudure(
                request.getParameter("reportDate"),
                request.getParameter("type"),
                request.getParameter("entry"));

        return ResponseEntity.ok("Resubmitted successfully!");

    } catch (Exception e) {

        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating record: " + e.getMessage());
    }
}


private void Run_Q_ATF_Procudure(String reportDateStr, String type, String entry) {
    
    String formattedDate;
    try {
        formattedDate = new SimpleDateFormat("dd-MM-yyyy")
                .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));
    } catch (Exception e) {
        System.out.println("Error parsing date. Post-commit logic aborted.");
        e.printStackTrace();
        return;
    }

    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
        
        @Override
        public void afterCommit() {
            try {
                boolean isResubNoEntry = "RESUB".equals(type) && "NO".equals(entry);
                boolean shouldExecuteProcedure = !"RESUB".equals(type) || isResubNoEntry;

                if (isResubNoEntry) {
                    String bdsql = "DELETE FROM BRRS_Q_ATF_DETAILTABLE WHERE REPORT_DATE = ?";
                    int rowsDeleted = jdbcTemplate.update(bdsql, formattedDate);
                    System.out.println("Successfully deleted before executing procedure " + rowsDeleted + " rows.");

                    String sqltransfer = "INSERT INTO BRRS_Q_ATF_DETAILTABLE "
                            + "SELECT * FROM BRRS_Q_ATF_ARCHIVALTABLE_DETAIL "
                            + "WHERE REPORT_DATE = ?";
                    int rowsInserted = jdbcTemplate.update(sqltransfer, formattedDate);
                    System.out.println("Successfully transferred " + rowsInserted + " rows.");
                }

                if (shouldExecuteProcedure) {
                    jdbcTemplate.update("BEGIN BRRS_Q_ATF_SUMMARY_PROCEDURE(?); END;", formattedDate);
                    System.out.println("Procedure executed");
                }

                if (isResubNoEntry) {
                    String adsql = "DELETE FROM BRRS_Q_ATF_DETAILTABLE WHERE REPORT_DATE = ?";
                    int rowsDeleted = jdbcTemplate.update(adsql, formattedDate);
                    System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

                    String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_Q_ATF_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
                    Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, formattedDate);
                    int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

                    String finalsql = "INSERT INTO BRRS_Q_ATF_ARCHIVALTABLE_SUMMARY "
                            + "SELECT * FROM BRRS_Q_ATF_SUMMARYTABLE "
                            + "WHERE REPORT_DATE = ?";
                    
                    int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, formattedDate);
                    System.out.println("Successfully transferred " + rowsInsertedSum + " rows.");

                    String adsumsql = "DELETE FROM BRRS_Q_ATF_SUMMARYTABLE WHERE REPORT_DATE = ?";
                    int rowsDeletedSum = jdbcTemplate.update(adsumsql, formattedDate);
                    System.out.println("Deleted from summary " + rowsDeletedSum + " rows after transfering.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
}
	
//=====================================================
// DETAIL EXCEL 
//=====================================================


	public byte[] getQ_ATFDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  Q_ATF  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getQ_ATFDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("Q_ATF Details ");

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
	                "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE",
	                "REPORT LABEL", "REPORT ADDL CRITERIA1", "REPORT ADDL CRITERIA2","REPORT DATE"
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
				List<Q_ATF_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);
				
				 // ===== DATE FORMATTER =====
		        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (Q_ATF_Detail_Entity item : reportData) { 
						XSSFRow row = sheet.createRow(rowIndex++);

 row.createCell(0).setCellValue(
	                        item.getCustId() != null ? item.getCustId() : "");
	                row.createCell(1).setCellValue(
	                        item.getAcctNumber() != null ? item.getAcctNumber() : "");
	                row.createCell(2).setCellValue(
	                        item.getAcctName() != null ? item.getAcctName() : "");

	                Cell balanceCell = row.createCell(3);
	                balanceCell.setCellValue(
	                        item.getAcctBalanceInpula() != null
	                                ? item.getAcctBalanceInpula().doubleValue()
	                                : 0);
	                balanceCell.setCellStyle(balanceStyle);

	                row.createCell(4).setCellValue(
	                        item.getReportLabel() != null ? item.getReportLabel() : "");
	                row.createCell(5).setCellValue(
	                        item.getReportAddlCriteria_1() != null ? item.getReportAddlCriteria_1() : "");
	                row.createCell(6).setCellValue(
	                        item.getReportAddlCriteria_2() != null ? item.getReportAddlCriteria_2() : "");
	                row.createCell(7).setCellValue(
	                        item.getReportDate() != null ? sdf.format(item.getReportDate()) : "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
						}
					}
				} else {
					logger.info("No data found for Q_ATF — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating Q_ATF Excel", e);
				return new byte[0];
			}
		}
	
	
//=====================================================
// ARCHIVAL DETAIL EXCEL 
//=====================================================

	public byte[] getQ_ATFDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for Q_ATF ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("Q_ATF Detail NEW");

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
	                "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE",
	                "REPORT LABEL", "REPORT ADDL CRITERIA1","REPORT ADDL CRITERIA2", "REPORT DATE"
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
				List<Q_ATF_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);
				
				 // ===== DATE FORMATTER =====
		        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (Q_ATF_Archival_Detail_Entity item : reportData) {
						XSSFRow row = sheet.createRow(rowIndex++);

					  row.createCell(0).setCellValue(
	                        item.getCustId() != null ? item.getCustId() : "");
	                row.createCell(1).setCellValue(
	                        item.getAcctNumber() != null ? item.getAcctNumber() : "");
	                row.createCell(2).setCellValue(
	                        item.getAcctName() != null ? item.getAcctName() : "");

	                Cell balanceCell = row.createCell(3);
	                balanceCell.setCellValue(
	                        item.getAcctBalanceInpula() != null
	                                ? item.getAcctBalanceInpula().doubleValue()
	                                : 0.000);
	                balanceCell.setCellStyle(balanceStyle);

	                row.createCell(4).setCellValue(
	                        item.getReportLabel() != null ? item.getReportLabel() : "");
	                row.createCell(5).setCellValue(
	                        item.getReportAddlCriteria_1() != null ? item.getReportAddlCriteria_1() : "");
	                row.createCell(6).setCellValue(
	                        item.getReportAddlCriteria_2() != null ? item.getReportAddlCriteria_2() : "");
	                row.createCell(7).setCellValue(
	                        item.getReportDate() != null ? sdf.format(item.getReportDate()) : "");


// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
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
				logger.error("Error generating RWA NEW Excel", e);
				return new byte[0];
			}
		}
		
		
//=====================================================
// Summary EXCEL 
//=====================================================


// Normal format Excel

			public byte[] getBRRS_Q_ATFExcel(String filename, String reportId, String fromdate, String todate, String currency,
					String dtltype, String type, String format, BigDecimal version) throws Exception {
				logger.info("Service: Starting Excel generation process in memory.");

				System.out.println("======= VIEW SCREEN =======");
				System.out.println("TYPE      : " + type);
				System.out.println("FORMAT      : " + format);
				System.out.println("DTLTYPE   : " + dtltype);
				System.out.println("DATE      : " + dateformat.parse(todate));
				System.out.println("VERSION   : " + version);
				System.out.println("==========================");
				
				
				// ARCHIVAL check
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					try {
						// Redirecting to Archival
						return getExcelQ_ATFARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 
//				
//				else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//					logger.info("Service: Generating RESUB report for version {}", version);
//
//					try {
//						// ✅ Redirecting to Resub Excel
//						return BRRS_Q_ATFResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
//
//					} catch (ParseException e) {
//						logger.error("Invalid report date format: {}", fromdate, e);
//						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//					}
//				}
//				
				else {

					if ("email".equalsIgnoreCase(format) && version == null) {
						logger.info("Got format as Email");
						logger.info("Service: Generating Email report for version {}", version);
						return BRRS_Q_ATFEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} else {

						// Fetch data

						List<Q_ATF_Summary_Entity> dataList = getDataByDateList(dateformat.parse(todate));

						if (dataList.isEmpty()) {
							logger.warn("Service: No data found for BRRS_Q_ATF report. Returning empty result.");
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
							throw new SecurityException("Template file exists but is not readable (check permissions): "
									+ templatePath.toAbsolutePath());
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
									Q_ATF_Summary_Entity record = dataList.get(i);
									System.out.println("rownumber=" + startRow + i);
									Row row = sheet.getRow(startRow + i);
									if (row == null) {
										row = sheet.createRow(startRow + i);
									}
									
									//row7
									// Column B
									Cell cellBdate = row.createCell(1);
									if (record.getReport_date() != null) {
										cellBdate.setCellValue(record.getReport_date());
										cellBdate.setCellStyle(dateStyle);
									} else {
										cellBdate.setCellValue("");
										cellBdate.setCellStyle(textStyle);
									}
									
									
									
									//ROW 12
									row = sheet.getRow(11);
									
		// row12
							
							
							
							// Column 2 - _num_depo
							Cell cellB = row.createCell(1);
							if (record.getR12_num_depo() != null) {
							    cellB.setCellValue(record.getR12_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}
							
							// Column 3 - _num_depo_acc
							Cell cellC = row.createCell(2);
							if (record.getR12_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR12_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}
							
							// Column 4 - _num_borrowers
							Cell cellD = row.createCell(3);
							if (record.getR12_num_borrowers() != null) {
							    cellD.setCellValue(record.getR12_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// Column 5 - _num_loan_acc
							Cell cellE = row.createCell(4);
							if (record.getR12_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR12_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
					
							
							
							// ======================= R13 =======================
							// row13
							row = sheet.getRow(12);
							
							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR13_num_depo() != null) {
							    cellB.setCellValue(record.getR13_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR13_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR13_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR13_num_borrowers() != null) {
							    cellD.setCellValue(record.getR13_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR13_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR13_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R14 =======================
							// row14
							row = sheet.getRow(13);
							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR14_num_depo() != null) {
							    cellB.setCellValue(record.getR14_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR14_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR14_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR14_num_borrowers() != null) {
							    cellD.setCellValue(record.getR14_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR14_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR14_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// ======================= R16 =======================

							// row16
							row = sheet.getRow(15);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR16_num_depo() != null) {
							    cellB.setCellValue(record.getR16_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR16_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR16_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR16_num_borrowers() != null) {
							    cellD.setCellValue(record.getR16_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR16_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR16_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R17 =======================

							// row17
							row = sheet.getRow(16);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR17_num_depo() != null) {
							    cellB.setCellValue(record.getR17_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR17_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR17_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR17_num_borrowers() != null) {
							    cellD.setCellValue(record.getR17_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR17_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR17_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R18 =======================

							// row18
							row = sheet.getRow(17);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR18_num_depo() != null) {
							    cellB.setCellValue(record.getR18_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR18_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR18_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR18_num_borrowers() != null) {
							    cellD.setCellValue(record.getR18_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR18_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR18_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R19 =======================

							// row19
							row = sheet.getRow(18);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR19_num_depo() != null) {
							    cellB.setCellValue(record.getR19_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR19_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR19_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR19_num_borrowers() != null) {
							    cellD.setCellValue(record.getR19_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR19_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR19_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R20 =======================

							// row20
							row = sheet.getRow(19);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR20_num_depo() != null) {
							    cellB.setCellValue(record.getR20_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR20_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR20_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR20_num_borrowers() != null) {
							    cellD.setCellValue(record.getR20_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR20_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR20_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R21 =======================

							// row21
							row = sheet.getRow(20);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR21_num_depo() != null) {
							    cellB.setCellValue(record.getR21_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR21_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR21_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR21_num_borrowers() != null) {
							    cellD.setCellValue(record.getR21_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR21_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR21_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R22 =======================

							// row22
							row = sheet.getRow(21);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR22_num_depo() != null) {
							    cellB.setCellValue(record.getR22_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR22_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR22_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR22_num_borrowers() != null) {
							    cellD.setCellValue(record.getR22_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR22_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR22_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R23 =======================

							// row23
							row = sheet.getRow(22);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR23_num_depo() != null) {
							    cellB.setCellValue(record.getR23_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR23_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR23_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR23_num_borrowers() != null) {
							    cellD.setCellValue(record.getR23_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR23_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR23_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R24 =======================

							// row24
							row = sheet.getRow(23);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR24_num_depo() != null) {
							    cellB.setCellValue(record.getR24_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR24_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR24_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR24_num_borrowers() != null) {
							    cellD.setCellValue(record.getR24_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR24_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR24_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R25 =======================

							// row25
							row = sheet.getRow(24);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR25_num_depo() != null) {
							    cellB.setCellValue(record.getR25_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR25_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR25_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR25_num_borrowers() != null) {
							    cellD.setCellValue(record.getR25_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR25_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR25_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R26 =======================

							// row26
							row = sheet.getRow(25);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR26_num_depo() != null) {
							    cellB.setCellValue(record.getR26_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR26_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR26_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR26_num_borrowers() != null) {
							    cellD.setCellValue(record.getR26_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR26_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR26_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R27 =======================

							// row27
							row = sheet.getRow(26);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR27_num_depo() != null) {
							    cellB.setCellValue(record.getR27_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR27_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR27_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR27_num_borrowers() != null) {
							    cellD.setCellValue(record.getR27_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR27_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR27_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R28 =======================

							// row28
							row = sheet.getRow(27);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR28_num_depo() != null) {
							    cellB.setCellValue(record.getR28_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR28_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR28_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR28_num_borrowers() != null) {
							    cellD.setCellValue(record.getR28_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR28_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR28_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// ======================= R30 =======================

							// row30
							row = sheet.getRow(29);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR30_num_depo() != null) {
							    cellB.setCellValue(record.getR30_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR30_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR30_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR30_num_borrowers() != null) {
							    cellD.setCellValue(record.getR30_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR30_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR30_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R31 =======================

							// row31
							row = sheet.getRow(30);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR31_num_depo() != null) {
							    cellB.setCellValue(record.getR31_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR31_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR31_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR31_num_borrowers() != null) {
							    cellD.setCellValue(record.getR31_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR31_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR31_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R32 =======================

							// row32
							row = sheet.getRow(31);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR32_num_depo() != null) {
							    cellB.setCellValue(record.getR32_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR32_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR32_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR32_num_borrowers() != null) {
							    cellD.setCellValue(record.getR32_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR32_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR32_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R33 =======================

							// row33
							row = sheet.getRow(32);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR33_num_depo() != null) {
							    cellB.setCellValue(record.getR33_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR33_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR33_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR33_num_borrowers() != null) {
							    cellD.setCellValue(record.getR33_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR33_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR33_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R34 =======================

							// row34
							row = sheet.getRow(33);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR34_num_depo() != null) {
							    cellB.setCellValue(record.getR34_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR34_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR34_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR34_num_borrowers() != null) {
							    cellD.setCellValue(record.getR34_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR34_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR34_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R35 =======================

							// row35
							row = sheet.getRow(34);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR35_num_depo() != null) {
							    cellB.setCellValue(record.getR35_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR35_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR35_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR35_num_borrowers() != null) {
							    cellD.setCellValue(record.getR35_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR35_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR35_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R36 =======================

							// row36
							row = sheet.getRow(35);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR36_num_depo() != null) {
							    cellB.setCellValue(record.getR36_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR36_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR36_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR36_num_borrowers() != null) {
							    cellD.setCellValue(record.getR36_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR36_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR36_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R37 =======================

							// row37
							row = sheet.getRow(36);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR37_num_depo() != null) {
							    cellB.setCellValue(record.getR37_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR37_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR37_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR37_num_borrowers() != null) {
							    cellD.setCellValue(record.getR37_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR37_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR37_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



						
							// ======================= R39 =======================

							// row39
							row = sheet.getRow(38);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR39_num_depo() != null) {
							    cellB.setCellValue(record.getR39_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR39_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR39_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR39_num_borrowers() != null) {
							    cellD.setCellValue(record.getR39_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR39_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR39_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R40 =======================

							// row40
							row = sheet.getRow(39);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR40_num_depo() != null) {
							    cellB.setCellValue(record.getR40_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR40_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR40_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR40_num_borrowers() != null) {
							    cellD.setCellValue(record.getR40_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR40_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR40_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// ======================= R42 =======================

							// row42
							row = sheet.getRow(41);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR42_num_depo() != null) {
							    cellB.setCellValue(record.getR42_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR42_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR42_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR42_num_borrowers() != null) {
							    cellD.setCellValue(record.getR42_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR42_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR42_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R43 =======================

							// row43
							row = sheet.getRow(42);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR43_num_depo() != null) {
							    cellB.setCellValue(record.getR43_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR43_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR43_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR43_num_borrowers() != null) {
							    cellD.setCellValue(record.getR43_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR43_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR43_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							// ======================= R45 =======================

							// row45
							row = sheet.getRow(44);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR45_num_depo() != null) {
							    cellB.setCellValue(record.getR45_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR45_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR45_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR45_num_borrowers() != null) {
							    cellD.setCellValue(record.getR45_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR45_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR45_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R46 =======================

							// row46
							row = sheet.getRow(45);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR46_num_depo() != null) {
							    cellB.setCellValue(record.getR46_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR46_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR46_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR46_num_borrowers() != null) {
							    cellD.setCellValue(record.getR46_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR46_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR46_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R47 =======================

							// row47
							row = sheet.getRow(46);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR47_num_depo() != null) {
							    cellB.setCellValue(record.getR47_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR47_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR47_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR47_num_borrowers() != null) {
							    cellD.setCellValue(record.getR47_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR47_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR47_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R48 =======================

							// row48
							row = sheet.getRow(47);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR48_num_depo() != null) {
							    cellB.setCellValue(record.getR48_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR48_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR48_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR48_num_borrowers() != null) {
							    cellD.setCellValue(record.getR48_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR48_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR48_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
							// ======================= R50 =======================

							// row50
							row = sheet.getRow(49);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR50_num_depo() != null) {
							    cellB.setCellValue(record.getR50_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR50_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR50_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR50_num_borrowers() != null) {
							    cellD.setCellValue(record.getR50_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR50_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR50_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R51 =======================

							// row51
							row = sheet.getRow(50);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR51_num_depo() != null) {
							    cellB.setCellValue(record.getR51_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR51_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR51_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR51_num_borrowers() != null) {
							    cellD.setCellValue(record.getR51_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR51_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR51_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R52 =======================

							// row52
							row = sheet.getRow(51);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR52_num_depo() != null) {
							    cellB.setCellValue(record.getR52_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR52_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR52_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR52_num_borrowers() != null) {
							    cellD.setCellValue(record.getR52_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR52_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR52_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// ======================= R54 =======================

							// row54
							row = sheet.getRow(53);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR54_num_depo() != null) {
							    cellB.setCellValue(record.getR54_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR54_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR54_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR54_num_borrowers() != null) {
							    cellD.setCellValue(record.getR54_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR54_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR54_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R55 =======================

							// row55
							row = sheet.getRow(54);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR55_num_depo() != null) {
							    cellB.setCellValue(record.getR55_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR55_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR55_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR55_num_borrowers() != null) {
							    cellD.setCellValue(record.getR55_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR55_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR55_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R56 =======================

							// row56
							row = sheet.getRow(55);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR56_num_depo() != null) {
							    cellB.setCellValue(record.getR56_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR56_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR56_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR56_num_borrowers() != null) {
							    cellD.setCellValue(record.getR56_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR56_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR56_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							// ======================= R58 =======================

							// row58
							row = sheet.getRow(57);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR58_num_depo() != null) {
							    cellB.setCellValue(record.getR58_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR58_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR58_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR58_num_borrowers() != null) {
							    cellD.setCellValue(record.getR58_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR58_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR58_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R59 =======================

							// row59
							row = sheet.getRow(58);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR59_num_depo() != null) {
							    cellB.setCellValue(record.getR59_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR59_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR59_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR59_num_borrowers() != null) {
							    cellD.setCellValue(record.getR59_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR59_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR59_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R60 =======================

							// row60
							row = sheet.getRow(59);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR60_num_depo() != null) {
							    cellB.setCellValue(record.getR60_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR60_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR60_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR60_num_borrowers() != null) {
							    cellD.setCellValue(record.getR60_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR60_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR60_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R61 =======================

							// row61
							row = sheet.getRow(60);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR61_num_depo() != null) {
							    cellB.setCellValue(record.getR61_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR61_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR61_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR61_num_borrowers() != null) {
							    cellD.setCellValue(record.getR61_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR61_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR61_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R62 =======================

							// row62
							row = sheet.getRow(61);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR62_num_depo() != null) {
							    cellB.setCellValue(record.getR62_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR62_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR62_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR62_num_borrowers() != null) {
							    cellD.setCellValue(record.getR62_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR62_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR62_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R63 =======================

							// row63
							row = sheet.getRow(62);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR63_num_depo() != null) {
							    cellB.setCellValue(record.getR63_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR63_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR63_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR63_num_borrowers() != null) {
							    cellD.setCellValue(record.getR63_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR63_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR63_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
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
															auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_ATF SUMMARY", null, "BRRS_Q_ATF_SUMMARYTABLE");
														}

							return out.toByteArray();
						}	
					}
				}
			}

			// Normal Email Excel
			public byte[] BRRS_Q_ATFEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Email Excel generation process in memory.");
				
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_Q_ATFARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				}
//				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//					logger.info("Service: Generating RESUB report for version {}", version);
//
//					try {
//						// ✅ Redirecting to Resub Excel
//						return BRRS_Q_ATFEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
//
//					} catch (ParseException e) {
//						logger.error("Invalid report date format: {}", fromdate, e);
//						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//					}
//				} 
				else {
				List<Q_ATF_Summary_Entity> dataList = getDataByDateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_Q_ATF report. Returning empty result.");
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
							Q_ATF_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							
							//row4
							// Column B
							Cell cellBdate = row.createCell(1);
							if (record.getReport_date() != null) {
								cellBdate.setCellValue(record.getReport_date());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}
							
							
							
							//ROW 10
							row = sheet.getRow(9);
							

		                  // row10 central government new
							
							// Column 2 - _num_depo
							Cell cellB = row.createCell(1);
							if (record.getR12_num_depo() != null) {
							    cellB.setCellValue(record.getR12_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}
							
							// Column 3 - _num_depo_acc
							Cell cellC = row.createCell(2);
							if (record.getR12_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR12_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}
							
							// Column 4 - _num_borrowers
							Cell cellD = row.createCell(3);
							if (record.getR12_num_borrowers() != null) {
							    cellD.setCellValue(record.getR12_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// Column 5 - _num_loan_acc
							Cell cellE = row.createCell(4);
							if (record.getR12_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR12_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// ======================= R13 local government r11=======================
							// row13
							row = sheet.getRow(10);
							
							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR13_num_depo() != null) {
							    cellB.setCellValue(record.getR13_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR13_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR13_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR13_num_borrowers() != null) {
							    cellD.setCellValue(record.getR13_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR13_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR13_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

					   
					   // ======================= R14 (iii)  Public Non-Financial Corporations r12=======================
					   
							// row14
							row = sheet.getRow(11);
							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR14_num_depo() != null) {
							    cellB.setCellValue(record.getR14_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR14_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR14_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR14_num_borrowers() != null) {
							    cellD.setCellValue(record.getR14_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR14_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR14_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R16 agriculture r14=======================

							// row16
							row = sheet.getRow(13);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR16_num_depo() != null) {
							    cellB.setCellValue(record.getR16_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR16_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR16_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR16_num_borrowers() != null) {
							    cellD.setCellValue(record.getR16_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR16_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR16_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

		// ======================= R17          b) Mining and Quarying r15=======================

							// row17
							row = sheet.getRow(14);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR17_num_depo() != null) {
							    cellB.setCellValue(record.getR17_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR17_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR17_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR17_num_borrowers() != null) {
							    cellD.setCellValue(record.getR17_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR17_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR17_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							// ======================= R18  c) Manufacturing R16=======================

							// row18
							row = sheet.getRow(15);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR18_num_depo() != null) {
							    cellB.setCellValue(record.getR18_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR18_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR18_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR18_num_borrowers() != null) {
							    cellD.setCellValue(record.getR18_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR18_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR18_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


		// ======================= R19 Construction R17=======================

							// row19
							row = sheet.getRow(16);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR19_num_depo() != null) {
							    cellB.setCellValue(record.getR19_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR19_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR19_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR19_num_borrowers() != null) {
							    cellD.setCellValue(record.getR19_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR19_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR19_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



			// ======================= R20          e) Commercial real estate R18=======================

							// row20
							row = sheet.getRow(17);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR20_num_depo() != null) {
							    cellB.setCellValue(record.getR20_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR20_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR20_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR20_num_borrowers() != null) {
							    cellD.setCellValue(record.getR20_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR20_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR20_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
								// ======================= R21 Electricity R19=======================

							// row21
							row = sheet.getRow(18);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR21_num_depo() != null) {
							    cellB.setCellValue(record.getR21_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR21_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR21_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR21_num_borrowers() != null) {
							    cellD.setCellValue(record.getR21_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR21_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR21_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							// ======================= R22 Water R20=======================

							// row22
							row = sheet.getRow(19);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR22_num_depo() != null) {
							    cellB.setCellValue(record.getR22_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR22_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR22_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR22_num_borrowers() != null) {
							    cellD.setCellValue(record.getR22_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR22_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR22_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
							// ======================= R23  h) Telecommunication and Post R21=======================

							// row23
							row = sheet.getRow(20);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR23_num_depo() != null) {
							    cellB.setCellValue(record.getR23_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR23_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR23_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR23_num_borrowers() != null) {
							    cellD.setCellValue(record.getR23_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR23_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR23_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

			// ======================= R24 i) Tourism and hotels R22=======================

							// row24
							row = sheet.getRow(21);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR24_num_depo() != null) {
							    cellB.setCellValue(record.getR24_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR24_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR24_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR24_num_borrowers() != null) {
							    cellD.setCellValue(record.getR24_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR24_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR24_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							
							// ======================= R25   j)Transport and storage R23=======================

							// row25
							row = sheet.getRow(22);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR25_num_depo() != null) {
							    cellB.setCellValue(record.getR25_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR25_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR25_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR25_num_borrowers() != null) {
							    cellD.setCellValue(record.getR25_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR25_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR25_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R26 k) Trade, restaurants and bars R24=======================

							// row26
							row = sheet.getRow(23);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR26_num_depo() != null) {
							    cellB.setCellValue(record.getR26_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR26_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR26_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR26_num_borrowers() != null) {
							    cellD.setCellValue(record.getR26_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR26_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR26_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
								// ======================= R27   l) Business services R25=======================

							// row27
							row = sheet.getRow(24);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR27_num_depo() != null) {
							    cellB.setCellValue(record.getR27_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR27_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR27_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR27_num_borrowers() != null) {
							    cellD.setCellValue(record.getR27_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR27_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR27_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
								// ======================= R28  m) Other community, social and personal services R26=======================

							// row28
							row = sheet.getRow(25);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR28_num_depo() != null) {
							    cellB.setCellValue(record.getR28_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR28_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR28_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR28_num_borrowers() != null) {
							    cellD.setCellValue(record.getR28_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR28_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR28_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// ======================= R30           a) Residential property (owner occupied)  R28 =======================

							// row30
							row = sheet.getRow(27);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR30_num_depo() != null) {
							    cellB.setCellValue(record.getR30_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR30_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR30_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR30_num_borrowers() != null) {
							    cellD.setCellValue(record.getR30_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR30_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR30_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							
							// ======================= R31         b) Residential property (rented) R29=======================

							// row31
							row = sheet.getRow(28);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR31_num_depo() != null) {
							    cellB.setCellValue(record.getR31_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR31_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR31_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR31_num_borrowers() != null) {
							    cellD.setCellValue(record.getR31_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR31_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR31_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
								// ======================= R32 c) Personal Loans R30 =======================

							// row32
							row = sheet.getRow(29);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR32_num_depo() != null) {
							    cellB.setCellValue(record.getR32_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR32_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR32_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR32_num_borrowers() != null) {
							    cellD.setCellValue(record.getR32_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR32_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR32_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							// ======================= R33 d) Motor vehicle R31=======================

							// row33
							row = sheet.getRow(30);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR33_num_depo() != null) {
							    cellB.setCellValue(record.getR33_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR33_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR33_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR33_num_borrowers() != null) {
							    cellD.setCellValue(record.getR33_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR33_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR33_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
								// ======================= R34         e) Household goods R32=======================

							// row34
							row = sheet.getRow(31);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR34_num_depo() != null) {
							    cellB.setCellValue(record.getR34_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR34_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR34_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR34_num_borrowers() != null) {
							    cellD.setCellValue(record.getR34_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR34_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR34_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
								// ======================= R35         f) Credit card loans R33=======================

							// row35
							row = sheet.getRow(32);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR35_num_depo() != null) {
							    cellB.setCellValue(record.getR35_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR35_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR35_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR35_num_borrowers() != null) {
							    cellD.setCellValue(record.getR35_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR35_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR35_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R36 g) Other* R34=======================

							// row36
							row = sheet.getRow(33);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR36_num_depo() != null) {
							    cellB.setCellValue(record.getR36_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR36_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR36_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR36_num_borrowers() != null) {
							    cellD.setCellValue(record.getR36_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR36_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR36_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
							// ======================= R37    (vi)  Non-Profit Institutions Serving Households R35======================

							// row37
							row = sheet.getRow(34);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR37_num_depo() != null) {
							    cellB.setCellValue(record.getR37_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR37_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR37_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR37_num_borrowers() != null) {
							    cellD.setCellValue(record.getR37_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR37_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR37_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
							// ======================= R39  a) Other Non-Financial Corporations R37 =======================

							// row39
							row = sheet.getRow(36);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR39_num_depo() != null) {
							    cellB.setCellValue(record.getR39_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR39_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR39_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR39_num_borrowers() != null) {
							    cellD.setCellValue(record.getR39_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR39_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR39_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
							// ======================= R40   b) Households R38=======================

							// row40
							row = sheet.getRow(37);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR40_num_depo() != null) {
							    cellB.setCellValue(record.getR40_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR40_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR40_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR40_num_borrowers() != null) {
							    cellD.setCellValue(record.getR40_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR40_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR40_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							
								// ======================= R42    (i)    Central Bank R40=======================

							// row42
							row = sheet.getRow(39);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR42_num_depo() != null) {
							    cellB.setCellValue(record.getR42_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR42_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR42_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR42_num_borrowers() != null) {
							    cellD.setCellValue(record.getR42_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR42_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR42_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
								// ======================= R43    (ii)   Commercial Banks R41 =======================

							// row43
							row = sheet.getRow(40);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR43_num_depo() != null) {
							    cellB.setCellValue(record.getR43_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR43_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR43_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR43_num_borrowers() != null) {
							    cellD.setCellValue(record.getR43_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR43_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR43_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// ======================= R45  a) Botswana Savings Bank (BSB) R43=======================

							// row45
							row = sheet.getRow(42);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR45_num_depo() != null) {
							    cellB.setCellValue(record.getR45_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR45_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR45_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR45_num_borrowers() != null) {
							    cellD.setCellValue(record.getR45_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR45_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR45_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R46         b) Botswana Building Society (BBS) R44=======================

							// row46
							row = sheet.getRow(43);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR46_num_depo() != null) {
							    cellB.setCellValue(record.getR46_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR46_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR46_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR46_num_borrowers() != null) {
							    cellD.setCellValue(record.getR46_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR46_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR46_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


								// ======================= R47         c) Domestic Money Market Unit Trusts R45=======================

							// row47
							row = sheet.getRow(44);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR47_num_depo() != null) {
							    cellB.setCellValue(record.getR47_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR47_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR47_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR47_num_borrowers() != null) {
							    cellD.setCellValue(record.getR47_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR47_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR47_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							
								// ======================= R48         d) Other (specify)*R46 =======================

							// row48
							row = sheet.getRow(45);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR48_num_depo() != null) {
							    cellB.setCellValue(record.getR48_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR48_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR48_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR48_num_borrowers() != null) {
							    cellD.setCellValue(record.getR48_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR48_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR48_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R50         a) Insurance Companies R48 =======================

							// row50
							row = sheet.getRow(47);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR50_num_depo() != null) {
							    cellB.setCellValue(record.getR50_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR50_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR50_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR50_num_borrowers() != null) {
							    cellD.setCellValue(record.getR50_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR50_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR50_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R51         b) Pension Funds R49 =======================

							// row51
							row = sheet.getRow(48);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR51_num_depo() != null) {
							    cellB.setCellValue(record.getR51_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR51_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR51_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR51_num_borrowers() != null) {
							    cellD.setCellValue(record.getR51_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR51_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR51_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// ======================= R58 1.Asset managersR51 =======================

							// row58
							row = sheet.getRow(50);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR58_num_depo() != null) {
							    cellB.setCellValue(record.getR58_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR58_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR58_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR58_num_borrowers() != null) {
							    cellD.setCellValue(record.getR58_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR58_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR58_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
									// ======================= R54 2.Finance companies R52=======================

							// row54
							row = sheet.getRow(51);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR54_num_depo() != null) {
							    cellB.setCellValue(record.getR54_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR54_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR54_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR54_num_borrowers() != null) {
							    cellD.setCellValue(record.getR54_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR54_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR54_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

			// ======================= R55    3.Medical Aid Schemes R53=======================

							// row55
							row = sheet.getRow(52);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR55_num_depo() != null) {
							    cellB.setCellValue(record.getR55_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR55_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR55_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR55_num_borrowers() != null) {
							    cellD.setCellValue(record.getR55_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR55_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR55_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
							// ======================= R56 4.Public sector financial intermediaries R54=======================

							// row56
							row = sheet.getRow(53);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR56_num_depo() != null) {
							    cellB.setCellValue(record.getR56_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR56_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR56_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR56_num_borrowers() != null) {
							    cellD.setCellValue(record.getR56_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR56_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR56_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
		// ======================= R52 5.SACCOs R55=======================

							// row52
							row = sheet.getRow(54);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR52_num_depo() != null) {
							    cellB.setCellValue(record.getR52_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR52_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR52_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR52_num_borrowers() != null) {
							    cellD.setCellValue(record.getR52_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR52_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR52_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


		// ======================= R59 1. Insurance brokers R57 =======================

							// row59
							row = sheet.getRow(56);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR59_num_depo() != null) {
							    cellB.setCellValue(record.getR59_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR59_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR59_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR59_num_borrowers() != null) {
							    cellD.setCellValue(record.getR59_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR59_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR59_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							// ======================= R60  2. Fund administrators R58=======================

							// row60
							row = sheet.getRow(57);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR60_num_depo() != null) {
							    cellB.setCellValue(record.getR60_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR60_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR60_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR60_num_borrowers() != null) {
							    cellD.setCellValue(record.getR60_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR60_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR60_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// ======================= R61 3. Bureau de change R59=======================

							// row61
							row = sheet.getRow(58);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR61_num_depo() != null) {
							    cellB.setCellValue(record.getR61_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR61_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR61_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR61_num_borrowers() != null) {
							    cellD.setCellValue(record.getR61_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR61_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR61_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
			// ======================= R62   4. Other (specify)* R60=======================

							// row62
							row = sheet.getRow(59);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR62_num_depo() != null) {
							    cellB.setCellValue(record.getR62_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR62_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR62_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR62_num_borrowers() != null) {
							    cellD.setCellValue(record.getR62_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR62_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR62_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R63      (v)  Non-residents R61 =======================

							// row63
							row = sheet.getRow(60);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR63_num_depo() != null) {
							    cellB.setCellValue(record.getR63_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR63_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR63_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR63_num_borrowers() != null) {
							    cellD.setCellValue(record.getR63_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR63_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR63_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							

						}
						workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					
					
					// audit service summary email

					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
										if (attrs != null) {
											HttpServletRequest request = attrs.getRequest();
											String userid = (String) request.getSession().getAttribute("USERID");
											auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_ATF EMAIL SUMMARY", null, "BRRS_Q_ATF_SUMMARYTABLE");
										}

					return out.toByteArray();
				}
				}
			}
			
			
			
			// Archival format excel
			public byte[] getExcelQ_ATFARCHIVAL(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory in Archival.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_Q_ATFARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 

				List<Q_ATF_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for Q_ATF report. Returning empty result.");
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
							Q_ATF_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
							//row7
							// Column B
							Cell cellBdate = row.createCell(1);
							if (record.getReport_date() != null) {
								cellBdate.setCellValue(record.getReport_date());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}
							
							
							
							//ROW 12
							row = sheet.getRow(11);

		// row12
							
							
							
							// Column 2 - _num_depo
							Cell cellB = row.createCell(1);
							if (record.getR12_num_depo() != null) {
							    cellB.setCellValue(record.getR12_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}
							
							// Column 3 - _num_depo_acc
							Cell cellC = row.createCell(2);
							if (record.getR12_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR12_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}
							
							// Column 4 - _num_borrowers
							Cell cellD = row.createCell(3);
							if (record.getR12_num_borrowers() != null) {
							    cellD.setCellValue(record.getR12_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// Column 5 - _num_loan_acc
							Cell cellE = row.createCell(4);
							if (record.getR12_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR12_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
					
							
							
							// ======================= R13 =======================
							// row13
							row = sheet.getRow(12);
							
							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR13_num_depo() != null) {
							    cellB.setCellValue(record.getR13_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR13_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR13_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR13_num_borrowers() != null) {
							    cellD.setCellValue(record.getR13_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR13_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR13_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R14 =======================
							// row14
							row = sheet.getRow(13);
							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR14_num_depo() != null) {
							    cellB.setCellValue(record.getR14_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR14_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR14_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR14_num_borrowers() != null) {
							    cellD.setCellValue(record.getR14_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR14_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR14_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// ======================= R16 =======================

							// row16
							row = sheet.getRow(15);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR16_num_depo() != null) {
							    cellB.setCellValue(record.getR16_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR16_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR16_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR16_num_borrowers() != null) {
							    cellD.setCellValue(record.getR16_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR16_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR16_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R17 =======================

							// row17
							row = sheet.getRow(16);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR17_num_depo() != null) {
							    cellB.setCellValue(record.getR17_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR17_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR17_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR17_num_borrowers() != null) {
							    cellD.setCellValue(record.getR17_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR17_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR17_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R18 =======================

							// row18
							row = sheet.getRow(17);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR18_num_depo() != null) {
							    cellB.setCellValue(record.getR18_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR18_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR18_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR18_num_borrowers() != null) {
							    cellD.setCellValue(record.getR18_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR18_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR18_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R19 =======================

							// row19
							row = sheet.getRow(18);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR19_num_depo() != null) {
							    cellB.setCellValue(record.getR19_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR19_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR19_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR19_num_borrowers() != null) {
							    cellD.setCellValue(record.getR19_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR19_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR19_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R20 =======================

							// row20
							row = sheet.getRow(19);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR20_num_depo() != null) {
							    cellB.setCellValue(record.getR20_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR20_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR20_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR20_num_borrowers() != null) {
							    cellD.setCellValue(record.getR20_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR20_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR20_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R21 =======================

							// row21
							row = sheet.getRow(20);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR21_num_depo() != null) {
							    cellB.setCellValue(record.getR21_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR21_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR21_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR21_num_borrowers() != null) {
							    cellD.setCellValue(record.getR21_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR21_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR21_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R22 =======================

							// row22
							row = sheet.getRow(21);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR22_num_depo() != null) {
							    cellB.setCellValue(record.getR22_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR22_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR22_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR22_num_borrowers() != null) {
							    cellD.setCellValue(record.getR22_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR22_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR22_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R23 =======================

							// row23
							row = sheet.getRow(22);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR23_num_depo() != null) {
							    cellB.setCellValue(record.getR23_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR23_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR23_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR23_num_borrowers() != null) {
							    cellD.setCellValue(record.getR23_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR23_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR23_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R24 =======================

							// row24
							row = sheet.getRow(23);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR24_num_depo() != null) {
							    cellB.setCellValue(record.getR24_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR24_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR24_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR24_num_borrowers() != null) {
							    cellD.setCellValue(record.getR24_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR24_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR24_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R25 =======================

							// row25
							row = sheet.getRow(24);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR25_num_depo() != null) {
							    cellB.setCellValue(record.getR25_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR25_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR25_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR25_num_borrowers() != null) {
							    cellD.setCellValue(record.getR25_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR25_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR25_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R26 =======================

							// row26
							row = sheet.getRow(25);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR26_num_depo() != null) {
							    cellB.setCellValue(record.getR26_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR26_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR26_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR26_num_borrowers() != null) {
							    cellD.setCellValue(record.getR26_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR26_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR26_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R27 =======================

							// row27
							row = sheet.getRow(26);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR27_num_depo() != null) {
							    cellB.setCellValue(record.getR27_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR27_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR27_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR27_num_borrowers() != null) {
							    cellD.setCellValue(record.getR27_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR27_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR27_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R28 =======================

							// row28
							row = sheet.getRow(27);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR28_num_depo() != null) {
							    cellB.setCellValue(record.getR28_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR28_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR28_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR28_num_borrowers() != null) {
							    cellD.setCellValue(record.getR28_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR28_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR28_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// ======================= R30 =======================

							// row30
							row = sheet.getRow(29);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR30_num_depo() != null) {
							    cellB.setCellValue(record.getR30_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR30_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR30_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR30_num_borrowers() != null) {
							    cellD.setCellValue(record.getR30_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR30_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR30_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R31 =======================

							// row31
							row = sheet.getRow(30);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR31_num_depo() != null) {
							    cellB.setCellValue(record.getR31_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR31_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR31_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR31_num_borrowers() != null) {
							    cellD.setCellValue(record.getR31_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR31_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR31_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R32 =======================

							// row32
							row = sheet.getRow(31);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR32_num_depo() != null) {
							    cellB.setCellValue(record.getR32_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR32_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR32_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR32_num_borrowers() != null) {
							    cellD.setCellValue(record.getR32_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR32_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR32_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R33 =======================

							// row33
							row = sheet.getRow(32);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR33_num_depo() != null) {
							    cellB.setCellValue(record.getR33_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR33_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR33_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR33_num_borrowers() != null) {
							    cellD.setCellValue(record.getR33_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR33_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR33_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R34 =======================

							// row34
							row = sheet.getRow(33);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR34_num_depo() != null) {
							    cellB.setCellValue(record.getR34_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR34_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR34_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR34_num_borrowers() != null) {
							    cellD.setCellValue(record.getR34_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR34_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR34_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R35 =======================

							// row35
							row = sheet.getRow(34);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR35_num_depo() != null) {
							    cellB.setCellValue(record.getR35_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR35_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR35_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR35_num_borrowers() != null) {
							    cellD.setCellValue(record.getR35_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR35_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR35_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R36 =======================

							// row36
							row = sheet.getRow(35);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR36_num_depo() != null) {
							    cellB.setCellValue(record.getR36_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR36_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR36_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR36_num_borrowers() != null) {
							    cellD.setCellValue(record.getR36_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR36_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR36_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R37 =======================

							// row37
							row = sheet.getRow(36);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR37_num_depo() != null) {
							    cellB.setCellValue(record.getR37_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR37_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR37_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR37_num_borrowers() != null) {
							    cellD.setCellValue(record.getR37_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR37_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR37_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



						
							// ======================= R39 =======================

							// row39
							row = sheet.getRow(38);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR39_num_depo() != null) {
							    cellB.setCellValue(record.getR39_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR39_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR39_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR39_num_borrowers() != null) {
							    cellD.setCellValue(record.getR39_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR39_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR39_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R40 =======================

							// row40
							row = sheet.getRow(39);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR40_num_depo() != null) {
							    cellB.setCellValue(record.getR40_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR40_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR40_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR40_num_borrowers() != null) {
							    cellD.setCellValue(record.getR40_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR40_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR40_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// ======================= R42 =======================

							// row42
							row = sheet.getRow(41);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR42_num_depo() != null) {
							    cellB.setCellValue(record.getR42_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR42_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR42_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR42_num_borrowers() != null) {
							    cellD.setCellValue(record.getR42_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR42_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR42_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R43 =======================

							// row43
							row = sheet.getRow(42);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR43_num_depo() != null) {
							    cellB.setCellValue(record.getR43_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR43_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR43_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR43_num_borrowers() != null) {
							    cellD.setCellValue(record.getR43_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR43_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR43_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							// ======================= R45 =======================

							// row45
							row = sheet.getRow(44);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR45_num_depo() != null) {
							    cellB.setCellValue(record.getR45_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR45_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR45_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR45_num_borrowers() != null) {
							    cellD.setCellValue(record.getR45_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR45_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR45_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R46 =======================

							// row46
							row = sheet.getRow(45);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR46_num_depo() != null) {
							    cellB.setCellValue(record.getR46_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR46_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR46_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR46_num_borrowers() != null) {
							    cellD.setCellValue(record.getR46_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR46_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR46_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R47 =======================

							// row47
							row = sheet.getRow(46);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR47_num_depo() != null) {
							    cellB.setCellValue(record.getR47_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR47_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR47_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR47_num_borrowers() != null) {
							    cellD.setCellValue(record.getR47_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR47_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR47_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R48 =======================

							// row48
							row = sheet.getRow(47);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR48_num_depo() != null) {
							    cellB.setCellValue(record.getR48_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR48_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR48_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR48_num_borrowers() != null) {
							    cellD.setCellValue(record.getR48_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR48_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR48_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
							// ======================= R50 =======================

							// row50
							row = sheet.getRow(49);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR50_num_depo() != null) {
							    cellB.setCellValue(record.getR50_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR50_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR50_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR50_num_borrowers() != null) {
							    cellD.setCellValue(record.getR50_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR50_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR50_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R51 =======================

							// row51
							row = sheet.getRow(50);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR51_num_depo() != null) {
							    cellB.setCellValue(record.getR51_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR51_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR51_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR51_num_borrowers() != null) {
							    cellD.setCellValue(record.getR51_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR51_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR51_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R52 =======================

							// row52
							row = sheet.getRow(51);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR52_num_depo() != null) {
							    cellB.setCellValue(record.getR52_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR52_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR52_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR52_num_borrowers() != null) {
							    cellD.setCellValue(record.getR52_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR52_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR52_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// ======================= R54 =======================

							// row54
							row = sheet.getRow(53);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR54_num_depo() != null) {
							    cellB.setCellValue(record.getR54_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR54_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR54_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR54_num_borrowers() != null) {
							    cellD.setCellValue(record.getR54_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR54_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR54_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R55 =======================

							// row55
							row = sheet.getRow(54);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR55_num_depo() != null) {
							    cellB.setCellValue(record.getR55_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR55_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR55_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR55_num_borrowers() != null) {
							    cellD.setCellValue(record.getR55_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR55_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR55_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R56 =======================

							// row56
							row = sheet.getRow(55);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR56_num_depo() != null) {
							    cellB.setCellValue(record.getR56_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR56_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR56_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR56_num_borrowers() != null) {
							    cellD.setCellValue(record.getR56_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR56_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR56_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							// ======================= R58 =======================

							// row58
							row = sheet.getRow(57);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR58_num_depo() != null) {
							    cellB.setCellValue(record.getR58_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR58_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR58_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR58_num_borrowers() != null) {
							    cellD.setCellValue(record.getR58_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR58_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR58_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R59 =======================

							// row59
							row = sheet.getRow(58);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR59_num_depo() != null) {
							    cellB.setCellValue(record.getR59_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR59_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR59_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR59_num_borrowers() != null) {
							    cellD.setCellValue(record.getR59_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR59_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR59_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R60 =======================

							// row60
							row = sheet.getRow(59);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR60_num_depo() != null) {
							    cellB.setCellValue(record.getR60_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR60_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR60_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR60_num_borrowers() != null) {
							    cellD.setCellValue(record.getR60_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR60_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR60_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R61 =======================

							// row61
							row = sheet.getRow(60);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR61_num_depo() != null) {
							    cellB.setCellValue(record.getR61_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR61_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR61_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR61_num_borrowers() != null) {
							    cellD.setCellValue(record.getR61_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR61_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR61_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R62 =======================

							// row62
							row = sheet.getRow(61);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR62_num_depo() != null) {
							    cellB.setCellValue(record.getR62_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR62_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR62_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR62_num_borrowers() != null) {
							    cellD.setCellValue(record.getR62_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR62_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR62_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							// ======================= R63 =======================

							// row63
							row = sheet.getRow(62);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR63_num_depo() != null) {
							    cellB.setCellValue(record.getR63_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR63_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR63_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR63_num_borrowers() != null) {
							    cellD.setCellValue(record.getR63_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR63_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR63_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
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
											auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_ATF ARCHIVAL SUMMARY", null, "BRRS_Q_ATF_ARCHIVALTABLE_SUMMARY");
										}

					return out.toByteArray();
				}

			}
			

			// Archival Email Excel
			public byte[] BRRS_Q_ATFARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Archival Email Excel generation process in memory.");

				List<Q_ATF_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_Q_ATF report. Returning empty result.");
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
							Q_ATF_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
							//row4
							// Column B
							Cell cellBdate = row.createCell(1);
							if (record.getReport_date() != null) {
								cellBdate.setCellValue(record.getReport_date());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}
							
							
							
							//ROW 10
							row = sheet.getRow(9);


		                  // row10 central government new
							
							// Column 2 - _num_depo
							Cell cellB = row.createCell(1);
							if (record.getR12_num_depo() != null) {
							    cellB.setCellValue(record.getR12_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}
							
							// Column 3 - _num_depo_acc
							Cell cellC = row.createCell(2);
							if (record.getR12_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR12_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}
							
							// Column 4 - _num_borrowers
							Cell cellD = row.createCell(3);
							if (record.getR12_num_borrowers() != null) {
							    cellD.setCellValue(record.getR12_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// Column 5 - _num_loan_acc
							Cell cellE = row.createCell(4);
							if (record.getR12_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR12_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// ======================= R13 local government r11=======================
							// row13
							row = sheet.getRow(10);
							
							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR13_num_depo() != null) {
							    cellB.setCellValue(record.getR13_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR13_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR13_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR13_num_borrowers() != null) {
							    cellD.setCellValue(record.getR13_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR13_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR13_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

					   
					   // ======================= R14 (iii)  Public Non-Financial Corporations r12=======================
					   
							// row14
							row = sheet.getRow(11);
							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR14_num_depo() != null) {
							    cellB.setCellValue(record.getR14_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR14_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR14_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR14_num_borrowers() != null) {
							    cellD.setCellValue(record.getR14_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR14_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR14_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R16 agriculture r14=======================

							// row16
							row = sheet.getRow(13);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR16_num_depo() != null) {
							    cellB.setCellValue(record.getR16_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR16_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR16_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR16_num_borrowers() != null) {
							    cellD.setCellValue(record.getR16_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR16_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR16_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

		// ======================= R17          b) Mining and Quarying r15=======================

							// row17
							row = sheet.getRow(14);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR17_num_depo() != null) {
							    cellB.setCellValue(record.getR17_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR17_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR17_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR17_num_borrowers() != null) {
							    cellD.setCellValue(record.getR17_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR17_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR17_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							// ======================= R18  c) Manufacturing R16=======================

							// row18
							row = sheet.getRow(15);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR18_num_depo() != null) {
							    cellB.setCellValue(record.getR18_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR18_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR18_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR18_num_borrowers() != null) {
							    cellD.setCellValue(record.getR18_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR18_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR18_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


		// ======================= R19 Construction R17=======================

							// row19
							row = sheet.getRow(16);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR19_num_depo() != null) {
							    cellB.setCellValue(record.getR19_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR19_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR19_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR19_num_borrowers() != null) {
							    cellD.setCellValue(record.getR19_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR19_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR19_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



			// ======================= R20          e) Commercial real estate R18=======================

							// row20
							row = sheet.getRow(17);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR20_num_depo() != null) {
							    cellB.setCellValue(record.getR20_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR20_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR20_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR20_num_borrowers() != null) {
							    cellD.setCellValue(record.getR20_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR20_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR20_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
								// ======================= R21 Electricity R19=======================

							// row21
							row = sheet.getRow(18);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR21_num_depo() != null) {
							    cellB.setCellValue(record.getR21_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR21_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR21_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR21_num_borrowers() != null) {
							    cellD.setCellValue(record.getR21_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR21_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR21_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							// ======================= R22 Water R20=======================

							// row22
							row = sheet.getRow(19);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR22_num_depo() != null) {
							    cellB.setCellValue(record.getR22_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR22_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR22_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR22_num_borrowers() != null) {
							    cellD.setCellValue(record.getR22_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR22_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR22_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
							// ======================= R23  h) Telecommunication and Post R21=======================

							// row23
							row = sheet.getRow(20);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR23_num_depo() != null) {
							    cellB.setCellValue(record.getR23_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR23_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR23_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR23_num_borrowers() != null) {
							    cellD.setCellValue(record.getR23_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR23_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR23_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

			// ======================= R24 i) Tourism and hotels R22=======================

							// row24
							row = sheet.getRow(21);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR24_num_depo() != null) {
							    cellB.setCellValue(record.getR24_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR24_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR24_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR24_num_borrowers() != null) {
							    cellD.setCellValue(record.getR24_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR24_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR24_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							
							// ======================= R25   j)Transport and storage R23=======================

							// row25
							row = sheet.getRow(22);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR25_num_depo() != null) {
							    cellB.setCellValue(record.getR25_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR25_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR25_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR25_num_borrowers() != null) {
							    cellD.setCellValue(record.getR25_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR25_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR25_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R26 k) Trade, restaurants and bars R24=======================

							// row26
							row = sheet.getRow(23);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR26_num_depo() != null) {
							    cellB.setCellValue(record.getR26_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR26_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR26_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR26_num_borrowers() != null) {
							    cellD.setCellValue(record.getR26_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR26_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR26_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
								// ======================= R27   l) Business services R25=======================

							// row27
							row = sheet.getRow(24);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR27_num_depo() != null) {
							    cellB.setCellValue(record.getR27_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR27_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR27_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR27_num_borrowers() != null) {
							    cellD.setCellValue(record.getR27_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR27_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR27_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
								// ======================= R28  m) Other community, social and personal services R26=======================

							// row28
							row = sheet.getRow(25);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR28_num_depo() != null) {
							    cellB.setCellValue(record.getR28_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR28_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR28_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR28_num_borrowers() != null) {
							    cellD.setCellValue(record.getR28_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR28_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR28_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// ======================= R30           a) Residential property (owner occupied)  R28 =======================

							// row30
							row = sheet.getRow(27);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR30_num_depo() != null) {
							    cellB.setCellValue(record.getR30_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR30_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR30_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR30_num_borrowers() != null) {
							    cellD.setCellValue(record.getR30_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR30_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR30_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							
							// ======================= R31         b) Residential property (rented) R29=======================

							// row31
							row = sheet.getRow(28);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR31_num_depo() != null) {
							    cellB.setCellValue(record.getR31_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR31_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR31_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR31_num_borrowers() != null) {
							    cellD.setCellValue(record.getR31_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR31_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR31_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
								// ======================= R32 c) Personal Loans R30 =======================

							// row32
							row = sheet.getRow(29);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR32_num_depo() != null) {
							    cellB.setCellValue(record.getR32_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR32_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR32_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR32_num_borrowers() != null) {
							    cellD.setCellValue(record.getR32_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR32_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR32_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							// ======================= R33 d) Motor vehicle R31=======================

							// row33
							row = sheet.getRow(30);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR33_num_depo() != null) {
							    cellB.setCellValue(record.getR33_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR33_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR33_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR33_num_borrowers() != null) {
							    cellD.setCellValue(record.getR33_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR33_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR33_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
								// ======================= R34         e) Household goods R32=======================

							// row34
							row = sheet.getRow(31);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR34_num_depo() != null) {
							    cellB.setCellValue(record.getR34_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR34_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR34_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR34_num_borrowers() != null) {
							    cellD.setCellValue(record.getR34_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR34_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR34_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
								// ======================= R35         f) Credit card loans R33=======================

							// row35
							row = sheet.getRow(32);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR35_num_depo() != null) {
							    cellB.setCellValue(record.getR35_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR35_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR35_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR35_num_borrowers() != null) {
							    cellD.setCellValue(record.getR35_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR35_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR35_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R36 g) Other* R34=======================

							// row36
							row = sheet.getRow(33);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR36_num_depo() != null) {
							    cellB.setCellValue(record.getR36_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR36_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR36_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR36_num_borrowers() != null) {
							    cellD.setCellValue(record.getR36_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR36_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR36_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
							// ======================= R37    (vi)  Non-Profit Institutions Serving Households R35======================

							// row37
							row = sheet.getRow(34);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR37_num_depo() != null) {
							    cellB.setCellValue(record.getR37_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR37_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR37_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR37_num_borrowers() != null) {
							    cellD.setCellValue(record.getR37_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR37_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR37_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
							// ======================= R39  a) Other Non-Financial Corporations R37 =======================

							// row39
							row = sheet.getRow(36);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR39_num_depo() != null) {
							    cellB.setCellValue(record.getR39_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR39_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR39_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR39_num_borrowers() != null) {
							    cellD.setCellValue(record.getR39_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR39_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR39_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
							// ======================= R40   b) Households R38=======================

							// row40
							row = sheet.getRow(37);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR40_num_depo() != null) {
							    cellB.setCellValue(record.getR40_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR40_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR40_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR40_num_borrowers() != null) {
							    cellD.setCellValue(record.getR40_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR40_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR40_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}



							
								// ======================= R42    (i)    Central Bank R40=======================

							// row42
							row = sheet.getRow(39);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR42_num_depo() != null) {
							    cellB.setCellValue(record.getR42_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR42_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR42_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR42_num_borrowers() != null) {
							    cellD.setCellValue(record.getR42_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR42_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR42_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
								// ======================= R43    (ii)   Commercial Banks R41 =======================

							// row43
							row = sheet.getRow(40);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR43_num_depo() != null) {
							    cellB.setCellValue(record.getR43_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR43_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR43_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR43_num_borrowers() != null) {
							    cellD.setCellValue(record.getR43_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR43_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR43_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// ======================= R45  a) Botswana Savings Bank (BSB) R43=======================

							// row45
							row = sheet.getRow(42);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR45_num_depo() != null) {
							    cellB.setCellValue(record.getR45_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR45_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR45_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR45_num_borrowers() != null) {
							    cellD.setCellValue(record.getR45_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR45_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR45_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R46         b) Botswana Building Society (BBS) R44=======================

							// row46
							row = sheet.getRow(43);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR46_num_depo() != null) {
							    cellB.setCellValue(record.getR46_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR46_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR46_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR46_num_borrowers() != null) {
							    cellD.setCellValue(record.getR46_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR46_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR46_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


								// ======================= R47         c) Domestic Money Market Unit Trusts R45=======================

							// row47
							row = sheet.getRow(44);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR47_num_depo() != null) {
							    cellB.setCellValue(record.getR47_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR47_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR47_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR47_num_borrowers() != null) {
							    cellD.setCellValue(record.getR47_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR47_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR47_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							
								// ======================= R48         d) Other (specify)*R46 =======================

							// row48
							row = sheet.getRow(45);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR48_num_depo() != null) {
							    cellB.setCellValue(record.getR48_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR48_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR48_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR48_num_borrowers() != null) {
							    cellD.setCellValue(record.getR48_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR48_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR48_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R50         a) Insurance Companies R48 =======================

							// row50
							row = sheet.getRow(47);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR50_num_depo() != null) {
							    cellB.setCellValue(record.getR50_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR50_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR50_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR50_num_borrowers() != null) {
							    cellD.setCellValue(record.getR50_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR50_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR50_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R51         b) Pension Funds R49 =======================

							// row51
							row = sheet.getRow(48);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR51_num_depo() != null) {
							    cellB.setCellValue(record.getR51_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR51_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR51_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR51_num_borrowers() != null) {
							    cellD.setCellValue(record.getR51_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR51_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR51_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// ======================= R58 1.Asset managersR51 =======================

							// row58
							row = sheet.getRow(50);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR58_num_depo() != null) {
							    cellB.setCellValue(record.getR58_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR58_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR58_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR58_num_borrowers() != null) {
							    cellD.setCellValue(record.getR58_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR58_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR58_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
									// ======================= R54 2.Finance companies R52=======================

							// row54
							row = sheet.getRow(51);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR54_num_depo() != null) {
							    cellB.setCellValue(record.getR54_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR54_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR54_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR54_num_borrowers() != null) {
							    cellD.setCellValue(record.getR54_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR54_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR54_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

			// ======================= R55    3.Medical Aid Schemes R53=======================

							// row55
							row = sheet.getRow(52);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR55_num_depo() != null) {
							    cellB.setCellValue(record.getR55_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR55_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR55_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR55_num_borrowers() != null) {
							    cellD.setCellValue(record.getR55_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR55_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR55_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							
							// ======================= R56 4.Public sector financial intermediaries R54=======================

							// row56
							row = sheet.getRow(53);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR56_num_depo() != null) {
							    cellB.setCellValue(record.getR56_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR56_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR56_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR56_num_borrowers() != null) {
							    cellD.setCellValue(record.getR56_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR56_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR56_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
		// ======================= R52 5.SACCOs R55=======================

							// row52
							row = sheet.getRow(54);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR52_num_depo() != null) {
							    cellB.setCellValue(record.getR52_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR52_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR52_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR52_num_borrowers() != null) {
							    cellD.setCellValue(record.getR52_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR52_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR52_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


		// ======================= R59 1. Insurance brokers R57 =======================

							// row59
							row = sheet.getRow(56);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR59_num_depo() != null) {
							    cellB.setCellValue(record.getR59_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR59_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR59_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR59_num_borrowers() != null) {
							    cellD.setCellValue(record.getR59_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR59_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR59_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}


							
							// ======================= R60  2. Fund administrators R58=======================

							// row60
							row = sheet.getRow(57);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR60_num_depo() != null) {
							    cellB.setCellValue(record.getR60_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR60_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR60_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR60_num_borrowers() != null) {
							    cellD.setCellValue(record.getR60_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR60_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR60_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// ======================= R61 3. Bureau de change R59=======================

							// row61
							row = sheet.getRow(58);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR61_num_depo() != null) {
							    cellB.setCellValue(record.getR61_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR61_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR61_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR61_num_borrowers() != null) {
							    cellD.setCellValue(record.getR61_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR61_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR61_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
			// ======================= R62   4. Other (specify)* R60=======================

							// row62
							row = sheet.getRow(59);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR62_num_depo() != null) {
							    cellB.setCellValue(record.getR62_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR62_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR62_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR62_num_borrowers() != null) {
							    cellD.setCellValue(record.getR62_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR62_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR62_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							
							// ======================= R63      (v)  Non-residents R61 =======================

							// row63
							row = sheet.getRow(60);

							// Column 2 - _num_depo
							cellB = row.createCell(1);
							if (record.getR63_num_depo() != null) {
							    cellB.setCellValue(record.getR63_num_depo().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// Column 3 - _num_depo_acc
							cellC = row.createCell(2);
							if (record.getR63_num_depo_acc() != null) {
							    cellC.setCellValue(record.getR63_num_depo_acc().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// Column 4 - _num_borrowers
							cellD = row.createCell(3);
							if (record.getR63_num_borrowers() != null) {
							    cellD.setCellValue(record.getR63_num_borrowers().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// Column 5 - _num_loan_acc
							cellE = row.createCell(4);
							if (record.getR63_num_loan_acc() != null) {
							    cellE.setCellValue(record.getR63_num_loan_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							

						}
						workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					
					// audit service archival summary email


					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
									if (attrs != null) {
										HttpServletRequest request = attrs.getRequest();
										String userid = (String) request.getSession().getAttribute("USERID");
										auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_ATF EMAIL ARCHIVAL SUMMARY", null, "BRRS_Q_ATF_ARCHIVALTABLE_SUMMARY");
									}

					return out.toByteArray();
				}
			}
			
			//=====================================================
			// RESUB VIEW 
			//=====================================================

			public List<Object[]> getQ_ATFResub() {

			    List<Object[]> resubList = new ArrayList<>();

			    try {

			        List<Q_ATF_Archival_Summary_Entity> repoData =
			                getarchivaldatabydateListWithVersion();

			        if (repoData != null && !repoData.isEmpty()) {

			            for (Q_ATF_Archival_Summary_Entity entity : repoData) {

			                Object[] row = new Object[] {
			                        entity.getReport_date(),
			                        entity.getReport_version(),
			                        entity.getReportResubDate()
			                };

			                resubList.add(row);
			            }

			            System.out.println("Fetched " + resubList.size() + " resub records");

			            Q_ATF_Archival_Summary_Entity first = repoData.get(0);

			            System.out.println("Latest resub version : "
			                    + first.getReport_version());

			        } else {

			            System.out.println("No resub data found.");
			        }

			    } catch (Exception e) {

			        System.err.println("Error fetching Q_ATF Resub data : "
			                + e.getMessage());

			        e.printStackTrace();
			    }

			    return resubList;
			}
		
		
		
	}