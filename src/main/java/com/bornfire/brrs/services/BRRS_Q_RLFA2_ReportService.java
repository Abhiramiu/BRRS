//========================Q_RLFA2 MANUAL REPORT 

package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
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
import java.util.Map;

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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;
import org.springframework.beans.BeanUtils;


@Service

public class BRRS_Q_RLFA2_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_RLFA2_ReportService.class);
	
	
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
	
	 @Autowired
    UserProfileRep userProfileRep;
	

// =====================================================
// SUMAMRY REPO
// =====================================================


	public List<Q_RLFA2_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_Q_RLFA2_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new Q_RLFA2_Summary_RowMapper()
    );
}
	
	public Q_RLFA2_Summary_Entity findByReportDate(Date reportDate) {

	    String sql =
	            "SELECT * FROM BRRS_Q_RLFA2_SUMMARYTABLE " +
	            "WHERE REPORT_DATE = ?";

	    return jdbcTemplate.queryForObject(
	            sql,
	            new Object[]{reportDate},
	            new Q_RLFA2_Summary_RowMapper()
	    );
	}
// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================



public List<Object[]> get_Q_RLFA2_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_Q_RLFA2_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<Q_RLFA2_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_Q_RLFA2_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new Q_RLFA2_Archival_Summary_RowMapper()
    );
}

public List<Q_RLFA2_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_Q_RLFA2_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new Q_RLFA2_Archival_Summary_RowMapper()
    );
}

public BigDecimal findMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_Q_RLFA2_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            BigDecimal.class
    );
}
 
// =====================================================
// DETAIL REPO
// =====================================================	


public List<Q_RLFA2_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_Q_RLFA2_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new Q_RLFA2_Detail_RowMapper()
    );
}



// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================


public List<Map<String, Object>> getQ_RLFA2_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_Q_RLFA2_ARCHIVALTABLE_DETAIL " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.queryForList(sql);
}


public List<Q_RLFA2_Archival_Detail_Entity> getDetaildatabydateListarchival(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * " +
                 "FROM BRRS_Q_RLFA2_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? " +
                 "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportDate, reportVersion },
            new Q_RLFA2_Archival_Detail_RowMapper()
    );
}

public BigDecimal findArchivalDetailMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) " +
                 "FROM BRRS_Q_RLFA2_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[] { reportDate },
            BigDecimal.class
    );
}

public Q_RLFA2_Archival_Detail_Entity getArchivalListWithVersion() {

    String sql = "SELECT * " +
                 "FROM BRRS_Q_RLFA2_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC " +
                 "FETCH FIRST 1 ROWS ONLY";

    return jdbcTemplate.queryForObject(
            sql,
            new Q_RLFA2_Archival_Detail_RowMapper()
    );
}

// =====================================================
// RESUB SUMMARY
// =====================================================

public List<Q_RLFA2_RESUB_Summary_Entity> getResubSummarydatabydateListarchival(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * " +
                 "FROM BRRS_Q_RLFA2_RESUB_SUMMARYTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new Q_RLFA2_RESUB_Summary_RowMapper()
    );
}


public BigDecimal findResubSummaryMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) " +
                 "FROM BRRS_Q_RLFA2_RESUB_SUMMARYTABLE " +
                 "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            BigDecimal.class
    );
}

public List<Map<String, Object>> getQ_RLFA2_Archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_Q_RLFA2_RESUB_SUMMARYTABLE " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.queryForList(sql);
}

public Q_RLFA2_RESUB_Summary_Entity getResubSummarydatabydateListWithVersion() {

    String sql = "SELECT * " +
                 "FROM BRRS_Q_RLFA2_RESUB_SUMMARYTABLE " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC " +
                 "FETCH FIRST 1 ROWS ONLY";

    return jdbcTemplate.queryForObject(
            sql,
            new Q_RLFA2_RESUB_Summary_RowMapper()
    );
}



// =====================================================
// RESUB DETAIL
// =====================================================


public List<Map<String, Object>> get_Q_RLFA2_Archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_Q_RLFA2_RESUB_DETAILTABLE " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.queryForList(sql);
}

public List<Q_RLFA2_RESUB_Detail_Entity> getResubDetaildatabydateList(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * " +
                 "FROM BRRS_Q_RLFA2_RESUB_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportDate, reportVersion },
            new Q_RLFA2_RESUB_Detail_RowMapper()
    );
}

public BigDecimal findResubDetailMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) " +
                 "FROM BRRS_Q_RLFA2_RESUB_DETAILTABLE " +
                 "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[] { reportDate },
            BigDecimal.class
    );
}

public Q_RLFA2_RESUB_Detail_Entity getdResubDetailDatabydateListWithVersion() {

    String sql = "SELECT * " +
                 "FROM BRRS_Q_RLFA2_RESUB_DETAILTABLE " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC " +
                 "FETCH FIRST 1 ROWS ONLY";

    return jdbcTemplate.queryForObject(
            sql,
            new Q_RLFA2_RESUB_Detail_RowMapper()
    );
}

// =====================================================
// SUMAMRY ENTITY & ROW MAPPER 
// =====================================================


public class Q_RLFA2_Summary_RowMapper implements RowMapper<Q_RLFA2_Summary_Entity> {

    @Override
    public Q_RLFA2_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        Q_RLFA2_Summary_Entity obj = new Q_RLFA2_Summary_Entity();

// =========================
// R10
// =========================
obj.setR10_sche_fore_ass(rs.getString("r10_sche_fore_ass"));
obj.setR10_orig_amt(rs.getBigDecimal("r10_orig_amt"));
obj.setR10_fore_amt(rs.getBigDecimal("r10_fore_amt"));
obj.setR10_no_of_acc(rs.getBigDecimal("r10_no_of_acc"));


// =========================
// R11
// =========================
obj.setR11_sche_fore_ass(rs.getString("r11_sche_fore_ass"));
obj.setR11_orig_amt(rs.getBigDecimal("r11_orig_amt"));
obj.setR11_fore_amt(rs.getBigDecimal("r11_fore_amt"));
obj.setR11_no_of_acc(rs.getBigDecimal("r11_no_of_acc"));

// =========================
// R12
// =========================
obj.setR12_sche_fore_ass(rs.getString("r12_sche_fore_ass"));
obj.setR12_orig_amt(rs.getBigDecimal("r12_orig_amt"));
obj.setR12_fore_amt(rs.getBigDecimal("r12_fore_amt"));
obj.setR12_no_of_acc(rs.getBigDecimal("r12_no_of_acc"));

// =========================
// R13
// =========================
obj.setR13_sche_fore_ass(rs.getString("r13_sche_fore_ass"));
obj.setR13_orig_amt(rs.getBigDecimal("r13_orig_amt"));
obj.setR13_fore_amt(rs.getBigDecimal("r13_fore_amt"));
obj.setR13_no_of_acc(rs.getBigDecimal("r13_no_of_acc"));

// =========================
// R14
// =========================
obj.setR14_sche_fore_ass(rs.getString("r14_sche_fore_ass"));
obj.setR14_orig_amt(rs.getBigDecimal("r14_orig_amt"));
obj.setR14_fore_amt(rs.getBigDecimal("r14_fore_amt"));
obj.setR14_no_of_acc(rs.getBigDecimal("r14_no_of_acc"));

// =========================
// R15
// =========================
obj.setR15_sche_fore_ass(rs.getString("r15_sche_fore_ass"));
obj.setR15_orig_amt(rs.getBigDecimal("r15_orig_amt"));
obj.setR15_fore_amt(rs.getBigDecimal("r15_fore_amt"));
obj.setR15_no_of_acc(rs.getBigDecimal("r15_no_of_acc"));

// =========================
// R16
// =========================
obj.setR16_sche_fore_ass(rs.getString("r16_sche_fore_ass"));
obj.setR16_orig_amt(rs.getBigDecimal("r16_orig_amt"));
obj.setR16_fore_amt(rs.getBigDecimal("r16_fore_amt"));
obj.setR16_no_of_acc(rs.getBigDecimal("r16_no_of_acc"));

// =========================
// R17
// =========================
obj.setR17_sche_fore_ass(rs.getString("r17_sche_fore_ass"));
obj.setR17_orig_amt(rs.getBigDecimal("r17_orig_amt"));
obj.setR17_fore_amt(rs.getBigDecimal("r17_fore_amt"));
obj.setR17_no_of_acc(rs.getBigDecimal("r17_no_of_acc"));

// =========================
// R18
// =========================
obj.setR18_sche_fore_ass(rs.getString("r18_sche_fore_ass"));
obj.setR18_orig_amt(rs.getBigDecimal("r18_orig_amt"));
obj.setR18_fore_amt(rs.getBigDecimal("r18_fore_amt"));
obj.setR18_no_of_acc(rs.getBigDecimal("r18_no_of_acc"));

// =========================
// R19
// =========================
obj.setR19_sche_fore_ass(rs.getString("r19_sche_fore_ass"));
obj.setR19_orig_amt(rs.getBigDecimal("r19_orig_amt"));
obj.setR19_fore_amt(rs.getBigDecimal("r19_fore_amt"));
obj.setR19_no_of_acc(rs.getBigDecimal("r19_no_of_acc"));

// =========================
// R20
// =========================
obj.setR20_sche_fore_ass(rs.getString("r20_sche_fore_ass"));
obj.setR20_orig_amt(rs.getBigDecimal("r20_orig_amt"));
obj.setR20_fore_amt(rs.getBigDecimal("r20_fore_amt"));
obj.setR20_no_of_acc(rs.getBigDecimal("r20_no_of_acc"));

// =========================
// R21
// =========================
obj.setR21_sche_fore_ass(rs.getString("r21_sche_fore_ass"));
obj.setR21_orig_amt(rs.getBigDecimal("r21_orig_amt"));
obj.setR21_fore_amt(rs.getBigDecimal("r21_fore_amt"));
obj.setR21_no_of_acc(rs.getBigDecimal("r21_no_of_acc"));

// =========================
// R22
// =========================
obj.setR22_sche_fore_ass(rs.getString("r22_sche_fore_ass"));
obj.setR22_orig_amt(rs.getBigDecimal("r22_orig_amt"));
obj.setR22_fore_amt(rs.getBigDecimal("r22_fore_amt"));
obj.setR22_no_of_acc(rs.getBigDecimal("r22_no_of_acc"));

// =========================
// R23
// =========================
obj.setR23_sche_fore_ass(rs.getString("r23_sche_fore_ass"));
obj.setR23_orig_amt(rs.getBigDecimal("r23_orig_amt"));
obj.setR23_fore_amt(rs.getBigDecimal("r23_fore_amt"));
obj.setR23_no_of_acc(rs.getBigDecimal("r23_no_of_acc"));

// =========================
// R24
// =========================
obj.setR24_sche_fore_ass(rs.getString("r24_sche_fore_ass"));
obj.setR24_orig_amt(rs.getBigDecimal("r24_orig_amt"));
obj.setR24_fore_amt(rs.getBigDecimal("r24_fore_amt"));
obj.setR24_no_of_acc(rs.getBigDecimal("r24_no_of_acc"));

// =========================
// R25
// =========================
obj.setR25_sche_fore_ass(rs.getString("r25_sche_fore_ass"));
obj.setR25_orig_amt(rs.getBigDecimal("r25_orig_amt"));
obj.setR25_fore_amt(rs.getBigDecimal("r25_fore_amt"));
obj.setR25_no_of_acc(rs.getBigDecimal("r25_no_of_acc"));

// =========================
// R26
// =========================
obj.setR26_sche_fore_ass(rs.getString("r26_sche_fore_ass"));
obj.setR26_orig_amt(rs.getBigDecimal("r26_orig_amt"));
obj.setR26_fore_amt(rs.getBigDecimal("r26_fore_amt"));
obj.setR26_no_of_acc(rs.getBigDecimal("r26_no_of_acc"));

// =========================
// R27
// =========================
obj.setR27_sche_fore_ass(rs.getString("r27_sche_fore_ass"));
obj.setR27_orig_amt(rs.getBigDecimal("r27_orig_amt"));
obj.setR27_fore_amt(rs.getBigDecimal("r27_fore_amt"));
obj.setR27_no_of_acc(rs.getBigDecimal("r27_no_of_acc"));

// =========================
// R28
// =========================
obj.setR28_sche_fore_ass(rs.getString("r28_sche_fore_ass"));
obj.setR28_orig_amt(rs.getBigDecimal("r28_orig_amt"));
obj.setR28_fore_amt(rs.getBigDecimal("r28_fore_amt"));
obj.setR28_no_of_acc(rs.getBigDecimal("r28_no_of_acc"));

// =========================
// R29
// =========================
obj.setR29_sche_fore_ass(rs.getString("r29_sche_fore_ass"));
obj.setR29_orig_amt(rs.getBigDecimal("r29_orig_amt"));
obj.setR29_fore_amt(rs.getBigDecimal("r29_fore_amt"));
obj.setR29_no_of_acc(rs.getBigDecimal("r29_no_of_acc"));

// =========================
// R30
// =========================
obj.setR30_sche_fore_ass(rs.getString("r30_sche_fore_ass"));
obj.setR30_orig_amt(rs.getBigDecimal("r30_orig_amt"));
obj.setR30_fore_amt(rs.getBigDecimal("r30_fore_amt"));
obj.setR30_no_of_acc(rs.getBigDecimal("r30_no_of_acc"));


// =========================
// R31
// =========================
obj.setR31_sche_fore_ass(rs.getString("r31_sche_fore_ass"));
obj.setR31_orig_amt(rs.getBigDecimal("r31_orig_amt"));
obj.setR31_fore_amt(rs.getBigDecimal("r31_fore_amt"));
obj.setR31_no_of_acc(rs.getBigDecimal("r31_no_of_acc"));

// =========================
// R32
// =========================
obj.setR32_sche_fore_ass(rs.getString("r32_sche_fore_ass"));
obj.setR32_orig_amt(rs.getBigDecimal("r32_orig_amt"));
obj.setR32_fore_amt(rs.getBigDecimal("r32_fore_amt"));
obj.setR32_no_of_acc(rs.getBigDecimal("r32_no_of_acc"));

// =========================
// R33
// =========================
obj.setR33_sche_fore_ass(rs.getString("r33_sche_fore_ass"));
obj.setR33_orig_amt(rs.getBigDecimal("r33_orig_amt"));
obj.setR33_fore_amt(rs.getBigDecimal("r33_fore_amt"));
obj.setR33_no_of_acc(rs.getBigDecimal("r33_no_of_acc"));

// =========================
// R34
// =========================
obj.setR34_sche_fore_ass(rs.getString("r34_sche_fore_ass"));
obj.setR34_orig_amt(rs.getBigDecimal("r34_orig_amt"));
obj.setR34_fore_amt(rs.getBigDecimal("r34_fore_amt"));
obj.setR34_no_of_acc(rs.getBigDecimal("r34_no_of_acc"));

// =========================
// R35
// =========================
obj.setR35_sche_fore_ass(rs.getString("r35_sche_fore_ass"));
obj.setR35_orig_amt(rs.getBigDecimal("r35_orig_amt"));
obj.setR35_fore_amt(rs.getBigDecimal("r35_fore_amt"));
obj.setR35_no_of_acc(rs.getBigDecimal("r35_no_of_acc"));

// =========================
// R36
// =========================
obj.setR36_sche_fore_ass(rs.getString("r36_sche_fore_ass"));
obj.setR36_orig_amt(rs.getBigDecimal("r36_orig_amt"));
obj.setR36_fore_amt(rs.getBigDecimal("r36_fore_amt"));
obj.setR36_no_of_acc(rs.getBigDecimal("r36_no_of_acc"));

// =========================
// R37
// =========================
obj.setR37_sche_fore_ass(rs.getString("r37_sche_fore_ass"));
obj.setR37_orig_amt(rs.getBigDecimal("r37_orig_amt"));
obj.setR37_fore_amt(rs.getBigDecimal("r37_fore_amt"));
obj.setR37_no_of_acc(rs.getBigDecimal("r37_no_of_acc"));

// =========================
// R38
// =========================
obj.setR38_sche_fore_ass(rs.getString("r38_sche_fore_ass"));
obj.setR38_orig_amt(rs.getBigDecimal("r38_orig_amt"));
obj.setR38_fore_amt(rs.getBigDecimal("r38_fore_amt"));
obj.setR38_no_of_acc(rs.getBigDecimal("r38_no_of_acc"));

// =========================
// R39
// =========================
obj.setR39_sche_fore_ass(rs.getString("r39_sche_fore_ass"));
obj.setR39_orig_amt(rs.getBigDecimal("r39_orig_amt"));
obj.setR39_fore_amt(rs.getBigDecimal("r39_fore_amt"));
obj.setR39_no_of_acc(rs.getBigDecimal("r39_no_of_acc"));

// =========================
// R40
// =========================
obj.setR40_sche_fore_ass(rs.getString("r40_sche_fore_ass"));
obj.setR40_orig_amt(rs.getBigDecimal("r40_orig_amt"));
obj.setR40_fore_amt(rs.getBigDecimal("r40_fore_amt"));
obj.setR40_no_of_acc(rs.getBigDecimal("r40_no_of_acc"));

// =========================
// R41
// =========================
obj.setR41_sche_fore_ass(rs.getString("r41_sche_fore_ass"));
obj.setR41_orig_amt(rs.getBigDecimal("r41_orig_amt"));
obj.setR41_fore_amt(rs.getBigDecimal("r41_fore_amt"));
obj.setR41_no_of_acc(rs.getBigDecimal("r41_no_of_acc"));

// =========================
// R42
// =========================
obj.setR42_sche_fore_ass(rs.getString("r42_sche_fore_ass"));
obj.setR42_orig_amt(rs.getBigDecimal("r42_orig_amt"));
obj.setR42_fore_amt(rs.getBigDecimal("r42_fore_amt"));
obj.setR42_no_of_acc(rs.getBigDecimal("r42_no_of_acc"));

// =========================
// R43
// =========================
obj.setR43_sche_fore_ass(rs.getString("r43_sche_fore_ass"));
obj.setR43_orig_amt(rs.getBigDecimal("r43_orig_amt"));
obj.setR43_fore_amt(rs.getBigDecimal("r43_fore_amt"));
obj.setR43_no_of_acc(rs.getBigDecimal("r43_no_of_acc"));

// =========================
// R44
// =========================
obj.setR44_sche_fore_ass(rs.getString("r44_sche_fore_ass"));
obj.setR44_orig_amt(rs.getBigDecimal("r44_orig_amt"));
obj.setR44_fore_amt(rs.getBigDecimal("r44_fore_amt"));
obj.setR44_no_of_acc(rs.getBigDecimal("r44_no_of_acc"));

// =========================
// R45
// =========================
obj.setR45_sche_fore_ass(rs.getString("r45_sche_fore_ass"));
obj.setR45_orig_amt(rs.getBigDecimal("r45_orig_amt"));
obj.setR45_fore_amt(rs.getBigDecimal("r45_fore_amt"));
obj.setR45_no_of_acc(rs.getBigDecimal("r45_no_of_acc"));

// =========================
// R46
// =========================
obj.setR46_sche_fore_ass(rs.getString("r46_sche_fore_ass"));
obj.setR46_orig_amt(rs.getBigDecimal("r46_orig_amt"));
obj.setR46_fore_amt(rs.getBigDecimal("r46_fore_amt"));
obj.setR46_no_of_acc(rs.getBigDecimal("r46_no_of_acc"));

// =========================
// R47
// =========================
obj.setR47_sche_fore_ass(rs.getString("r47_sche_fore_ass"));
obj.setR47_orig_amt(rs.getBigDecimal("r47_orig_amt"));
obj.setR47_fore_amt(rs.getBigDecimal("r47_fore_amt"));
obj.setR47_no_of_acc(rs.getBigDecimal("r47_no_of_acc"));

// =========================
// R48
// =========================
obj.setR48_sche_fore_ass(rs.getString("r48_sche_fore_ass"));
obj.setR48_orig_amt(rs.getBigDecimal("r48_orig_amt"));
obj.setR48_fore_amt(rs.getBigDecimal("r48_fore_amt"));
obj.setR48_no_of_acc(rs.getBigDecimal("r48_no_of_acc"));

// =========================
// R49
// =========================
obj.setR49_sche_fore_ass(rs.getString("r49_sche_fore_ass"));
obj.setR49_orig_amt(rs.getBigDecimal("r49_orig_amt"));
obj.setR49_fore_amt(rs.getBigDecimal("r49_fore_amt"));
obj.setR49_no_of_acc(rs.getBigDecimal("r49_no_of_acc"));

// =========================
// R50
// =========================
obj.setR50_sche_fore_ass(rs.getString("r50_sche_fore_ass"));
obj.setR50_orig_amt(rs.getBigDecimal("r50_orig_amt"));
obj.setR50_fore_amt(rs.getBigDecimal("r50_fore_amt"));
obj.setR50_no_of_acc(rs.getBigDecimal("r50_no_of_acc"));

// =========================
// R51
// =========================
obj.setR51_sche_fore_ass(rs.getString("r51_sche_fore_ass"));
obj.setR51_orig_amt(rs.getBigDecimal("r51_orig_amt"));
obj.setR51_fore_amt(rs.getBigDecimal("r51_fore_amt"));
obj.setR51_no_of_acc(rs.getBigDecimal("r51_no_of_acc"));

// =========================
// R52
// =========================
obj.setR52_sche_fore_ass(rs.getString("r52_sche_fore_ass"));
obj.setR52_orig_amt(rs.getBigDecimal("r52_orig_amt"));
obj.setR52_fore_amt(rs.getBigDecimal("r52_fore_amt"));
obj.setR52_no_of_acc(rs.getBigDecimal("r52_no_of_acc"));

// =========================
// R53
// =========================
obj.setR53_sche_fore_ass(rs.getString("r53_sche_fore_ass"));
obj.setR53_orig_amt(rs.getBigDecimal("r53_orig_amt"));
obj.setR53_fore_amt(rs.getBigDecimal("r53_fore_amt"));
obj.setR53_no_of_acc(rs.getBigDecimal("r53_no_of_acc"));

// =========================
// R54
// =========================
obj.setR54_sche_fore_ass(rs.getString("r54_sche_fore_ass"));
obj.setR54_orig_amt(rs.getBigDecimal("r54_orig_amt"));
obj.setR54_fore_amt(rs.getBigDecimal("r54_fore_amt"));
obj.setR54_no_of_acc(rs.getBigDecimal("r54_no_of_acc"));

// =========================
// R55
// =========================
obj.setR55_sche_fore_ass(rs.getString("r55_sche_fore_ass"));
obj.setR55_orig_amt(rs.getBigDecimal("r55_orig_amt"));
obj.setR55_fore_amt(rs.getBigDecimal("r55_fore_amt"));
obj.setR55_no_of_acc(rs.getBigDecimal("r55_no_of_acc"));

// =========================
// R56
// =========================
obj.setR56_sche_fore_ass(rs.getString("r56_sche_fore_ass"));
obj.setR56_orig_amt(rs.getBigDecimal("r56_orig_amt"));
obj.setR56_fore_amt(rs.getBigDecimal("r56_fore_amt"));
obj.setR56_no_of_acc(rs.getBigDecimal("r56_no_of_acc"));

// =========================
// R57
// =========================
obj.setR57_sche_fore_ass(rs.getString("r57_sche_fore_ass"));
obj.setR57_orig_amt(rs.getBigDecimal("r57_orig_amt"));
obj.setR57_fore_amt(rs.getBigDecimal("r57_fore_amt"));
obj.setR57_no_of_acc(rs.getBigDecimal("r57_no_of_acc"));

// =========================
// R58
// =========================
obj.setR58_sche_fore_ass(rs.getString("r58_sche_fore_ass"));
obj.setR58_orig_amt(rs.getBigDecimal("r58_orig_amt"));
obj.setR58_fore_amt(rs.getBigDecimal("r58_fore_amt"));
obj.setR58_no_of_acc(rs.getBigDecimal("r58_no_of_acc"));

// =========================
// R59
// =========================
obj.setR59_sche_fore_ass(rs.getString("r59_sche_fore_ass"));
obj.setR59_orig_amt(rs.getBigDecimal("r59_orig_amt"));
obj.setR59_fore_amt(rs.getBigDecimal("r59_fore_amt"));
obj.setR59_no_of_acc(rs.getBigDecimal("r59_no_of_acc"));

// =========================
// R60
// =========================
obj.setR60_sche_fore_ass(rs.getString("r60_sche_fore_ass"));
obj.setR60_orig_amt(rs.getBigDecimal("r60_orig_amt"));
obj.setR60_fore_amt(rs.getBigDecimal("r60_fore_amt"));
obj.setR60_no_of_acc(rs.getBigDecimal("r60_no_of_acc"));

// =========================
// R61
// =========================
obj.setR61_sche_fore_ass(rs.getString("r61_sche_fore_ass"));
obj.setR61_orig_amt(rs.getBigDecimal("r61_orig_amt"));
obj.setR61_fore_amt(rs.getBigDecimal("r61_fore_amt"));
obj.setR61_no_of_acc(rs.getBigDecimal("r61_no_of_acc"));

// =========================
// R62
// =========================
obj.setR62_sche_fore_ass(rs.getString("r62_sche_fore_ass"));
obj.setR62_orig_amt(rs.getBigDecimal("r62_orig_amt"));
obj.setR62_fore_amt(rs.getBigDecimal("r62_fore_amt"));
obj.setR62_no_of_acc(rs.getBigDecimal("r62_no_of_acc"));

// =========================
// R63
// =========================
obj.setR63_sche_fore_ass(rs.getString("r63_sche_fore_ass"));
obj.setR63_orig_amt(rs.getBigDecimal("r63_orig_amt"));
obj.setR63_fore_amt(rs.getBigDecimal("r63_fore_amt"));
obj.setR63_no_of_acc(rs.getBigDecimal("r63_no_of_acc"));

// =========================
// R64
// =========================
obj.setR64_sche_fore_ass(rs.getString("r64_sche_fore_ass"));
obj.setR64_orig_amt(rs.getBigDecimal("r64_orig_amt"));
obj.setR64_fore_amt(rs.getBigDecimal("r64_fore_amt"));
obj.setR64_no_of_acc(rs.getBigDecimal("r64_no_of_acc"));

// =========================
// R65
// =========================
obj.setR65_sche_fore_ass(rs.getString("r65_sche_fore_ass"));
obj.setR65_orig_amt(rs.getBigDecimal("r65_orig_amt"));
obj.setR65_fore_amt(rs.getBigDecimal("r65_fore_amt"));
obj.setR65_no_of_acc(rs.getBigDecimal("r65_no_of_acc"));

// =========================
// R66
// =========================
obj.setR66_sche_fore_ass(rs.getString("r66_sche_fore_ass"));
obj.setR66_orig_amt(rs.getBigDecimal("r66_orig_amt"));
obj.setR66_fore_amt(rs.getBigDecimal("r66_fore_amt"));
obj.setR66_no_of_acc(rs.getBigDecimal("r66_no_of_acc"));

// =========================
// R67
// =========================
obj.setR67_sche_fore_ass(rs.getString("r67_sche_fore_ass"));
obj.setR67_orig_amt(rs.getBigDecimal("r67_orig_amt"));
obj.setR67_fore_amt(rs.getBigDecimal("r67_fore_amt"));
obj.setR67_no_of_acc(rs.getBigDecimal("r67_no_of_acc"));


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


public class Q_RLFA2_Summary_Entity {
	
	private String	r10_sche_fore_ass;
	private BigDecimal	r10_orig_amt;
	private BigDecimal	r10_fore_amt;
	private BigDecimal	r10_no_of_acc;
	
	private String	r11_sche_fore_ass;
	private BigDecimal	r11_orig_amt;
	private BigDecimal	r11_fore_amt;
	private BigDecimal	r11_no_of_acc;
	
	private String	r12_sche_fore_ass;
	private BigDecimal	r12_orig_amt;
	private BigDecimal	r12_fore_amt;
	private BigDecimal	r12_no_of_acc;
	
	private String	r13_sche_fore_ass;
	private BigDecimal	r13_orig_amt;
	private BigDecimal	r13_fore_amt;
	private BigDecimal	r13_no_of_acc;
	
	private String	r14_sche_fore_ass;
	private BigDecimal	r14_orig_amt;
	private BigDecimal	r14_fore_amt;
	private BigDecimal	r14_no_of_acc;
	
	private String	r15_sche_fore_ass;
	private BigDecimal	r15_orig_amt;
	private BigDecimal	r15_fore_amt;
	private BigDecimal	r15_no_of_acc;
	
	private String	r16_sche_fore_ass;
	private BigDecimal	r16_orig_amt;
	private BigDecimal	r16_fore_amt;
	private BigDecimal	r16_no_of_acc;
	
	private String	r17_sche_fore_ass;
	private BigDecimal	r17_orig_amt;
	private BigDecimal	r17_fore_amt;
	private BigDecimal	r17_no_of_acc;
	
	private String	r18_sche_fore_ass;
	private BigDecimal	r18_orig_amt;
	private BigDecimal	r18_fore_amt;
	private BigDecimal	r18_no_of_acc;
	
	private String	r19_sche_fore_ass;
	private BigDecimal	r19_orig_amt;
	private BigDecimal	r19_fore_amt;
	private BigDecimal	r19_no_of_acc;
	
	private String	r20_sche_fore_ass;
	private BigDecimal	r20_orig_amt;
	private BigDecimal	r20_fore_amt;
	private BigDecimal	r20_no_of_acc;
	
	private String	r21_sche_fore_ass;
	private BigDecimal	r21_orig_amt;
	private BigDecimal	r21_fore_amt;
	private BigDecimal	r21_no_of_acc;
	
	private String	r22_sche_fore_ass;
	private BigDecimal	r22_orig_amt;
	private BigDecimal	r22_fore_amt;
	private BigDecimal	r22_no_of_acc;
	
	private String	r23_sche_fore_ass;
	private BigDecimal	r23_orig_amt;
	private BigDecimal	r23_fore_amt;
	private BigDecimal	r23_no_of_acc;
	
	private String	r24_sche_fore_ass;
	private BigDecimal	r24_orig_amt;
	private BigDecimal	r24_fore_amt;
	private BigDecimal	r24_no_of_acc;
	
	private String	r25_sche_fore_ass;
	private BigDecimal	r25_orig_amt;
	private BigDecimal	r25_fore_amt;
	private BigDecimal	r25_no_of_acc;
	
	private String	r26_sche_fore_ass;
	private BigDecimal	r26_orig_amt;
	private BigDecimal	r26_fore_amt;
	private BigDecimal	r26_no_of_acc;
	
	private String	r27_sche_fore_ass;
	private BigDecimal	r27_orig_amt;
	private BigDecimal	r27_fore_amt;
	private BigDecimal	r27_no_of_acc;
	
	private String	r28_sche_fore_ass;
	private BigDecimal	r28_orig_amt;
	private BigDecimal	r28_fore_amt;
	private BigDecimal	r28_no_of_acc;
	
	private String	r29_sche_fore_ass;
	private BigDecimal	r29_orig_amt;
	private BigDecimal	r29_fore_amt;
	private BigDecimal	r29_no_of_acc;
	
	private String	r30_sche_fore_ass;
	private BigDecimal	r30_orig_amt;
	private BigDecimal	r30_fore_amt;
	private BigDecimal	r30_no_of_acc;
	
	private String	r31_sche_fore_ass;
	private BigDecimal	r31_orig_amt;
	private BigDecimal	r31_fore_amt;
	private BigDecimal	r31_no_of_acc;
	
	private String	r32_sche_fore_ass;
	private BigDecimal	r32_orig_amt;
	private BigDecimal	r32_fore_amt;
	private BigDecimal	r32_no_of_acc;
	
	private String	r33_sche_fore_ass;
	private BigDecimal	r33_orig_amt;
	private BigDecimal	r33_fore_amt;
	private BigDecimal	r33_no_of_acc;
	
	private String	r34_sche_fore_ass;
	private BigDecimal	r34_orig_amt;
	private BigDecimal	r34_fore_amt;
	private BigDecimal	r34_no_of_acc;
	
	private String	r35_sche_fore_ass;
	private BigDecimal	r35_orig_amt;
	private BigDecimal	r35_fore_amt;
	private BigDecimal	r35_no_of_acc;
	
	private String	r36_sche_fore_ass;
	private BigDecimal	r36_orig_amt;
	private BigDecimal	r36_fore_amt;
	private BigDecimal	r36_no_of_acc;
	
	private String	r37_sche_fore_ass;
	private BigDecimal	r37_orig_amt;
	private BigDecimal	r37_fore_amt;
	private BigDecimal	r37_no_of_acc;
	
	private String	r38_sche_fore_ass;
	private BigDecimal	r38_orig_amt;
	private BigDecimal	r38_fore_amt;
	private BigDecimal	r38_no_of_acc;
	
	private String	r39_sche_fore_ass;
	private BigDecimal	r39_orig_amt;
	private BigDecimal	r39_fore_amt;
	private BigDecimal	r39_no_of_acc;
	
	private String	r40_sche_fore_ass;
	private BigDecimal	r40_orig_amt;
	private BigDecimal	r40_fore_amt;
	private BigDecimal	r40_no_of_acc;
	
	private String	r41_sche_fore_ass;
	private BigDecimal	r41_orig_amt;
	private BigDecimal	r41_fore_amt;
	private BigDecimal	r41_no_of_acc;
	
	private String	r42_sche_fore_ass;
	private BigDecimal	r42_orig_amt;
	private BigDecimal	r42_fore_amt;
	private BigDecimal	r42_no_of_acc;
	
	private String	r43_sche_fore_ass;
	private BigDecimal	r43_orig_amt;
	private BigDecimal	r43_fore_amt;
	private BigDecimal	r43_no_of_acc;
	
	private String	r44_sche_fore_ass;
	private BigDecimal	r44_orig_amt;
	private BigDecimal	r44_fore_amt;
	private BigDecimal	r44_no_of_acc;
	
	private String	r45_sche_fore_ass;
	private BigDecimal	r45_orig_amt;
	private BigDecimal	r45_fore_amt;
	private BigDecimal	r45_no_of_acc;
	
	private String	r46_sche_fore_ass;
	private BigDecimal	r46_orig_amt;
	private BigDecimal	r46_fore_amt;
	private BigDecimal	r46_no_of_acc;
	
	private String	r47_sche_fore_ass;
	private BigDecimal	r47_orig_amt;
	private BigDecimal	r47_fore_amt;
	private BigDecimal	r47_no_of_acc;
	
	private String	r48_sche_fore_ass;
	private BigDecimal	r48_orig_amt;
	private BigDecimal	r48_fore_amt;
	private BigDecimal	r48_no_of_acc;
	
	private String	r49_sche_fore_ass;
	private BigDecimal	r49_orig_amt;
	private BigDecimal	r49_fore_amt;
	private BigDecimal	r49_no_of_acc;
	
	private String	r50_sche_fore_ass;
	private BigDecimal	r50_orig_amt;
	private BigDecimal	r50_fore_amt;
	private BigDecimal	r50_no_of_acc;
	
	private String	r51_sche_fore_ass;
	private BigDecimal	r51_orig_amt;
	private BigDecimal	r51_fore_amt;
	private BigDecimal	r51_no_of_acc;
	
	private String	r52_sche_fore_ass;
	private BigDecimal	r52_orig_amt;
	private BigDecimal	r52_fore_amt;
	private BigDecimal	r52_no_of_acc;
	
	private String	r53_sche_fore_ass;
	private BigDecimal	r53_orig_amt;
	private BigDecimal	r53_fore_amt;
	private BigDecimal	r53_no_of_acc;
	
	private String	r54_sche_fore_ass;
	private BigDecimal	r54_orig_amt;
	private BigDecimal	r54_fore_amt;
	private BigDecimal	r54_no_of_acc;
	
	private String	r55_sche_fore_ass;
	private BigDecimal	r55_orig_amt;
	private BigDecimal	r55_fore_amt;
	private BigDecimal	r55_no_of_acc;
	
	private String	r56_sche_fore_ass;
	private BigDecimal	r56_orig_amt;
	private BigDecimal	r56_fore_amt;
	private BigDecimal	r56_no_of_acc;
	
	private String	r57_sche_fore_ass;
	private BigDecimal	r57_orig_amt;
	private BigDecimal	r57_fore_amt;
	private BigDecimal	r57_no_of_acc;
	
	private String	r58_sche_fore_ass;
	private BigDecimal	r58_orig_amt;
	private BigDecimal	r58_fore_amt;
	private BigDecimal	r58_no_of_acc;
	
	private String	r59_sche_fore_ass;
	private BigDecimal	r59_orig_amt;
	private BigDecimal	r59_fore_amt;
	private BigDecimal	r59_no_of_acc;
	
	private String	r60_sche_fore_ass;
	private BigDecimal	r60_orig_amt;
	private BigDecimal	r60_fore_amt;
	private BigDecimal	r60_no_of_acc;
	
	private String	r61_sche_fore_ass;
	private BigDecimal	r61_orig_amt;
	private BigDecimal	r61_fore_amt;
	private BigDecimal	r61_no_of_acc;
	
	private String	r62_sche_fore_ass;
	private BigDecimal	r62_orig_amt;
	private BigDecimal	r62_fore_amt;
	private BigDecimal	r62_no_of_acc;
	
	private String	r63_sche_fore_ass;
	private BigDecimal	r63_orig_amt;
	private BigDecimal	r63_fore_amt;
	private BigDecimal	r63_no_of_acc;
	
	// R64
	private String     r64_sche_fore_ass;
	private BigDecimal r64_orig_amt;
	private BigDecimal r64_fore_amt;
	private BigDecimal r64_no_of_acc;

	// R65
	private String     r65_sche_fore_ass;
	private BigDecimal r65_orig_amt;
	private BigDecimal r65_fore_amt;
	private BigDecimal r65_no_of_acc;

	// R66
	private String     r66_sche_fore_ass;
	private BigDecimal r66_orig_amt;
	private BigDecimal r66_fore_amt;
	private BigDecimal r66_no_of_acc;

	// R67
	private String     r67_sche_fore_ass;
	private BigDecimal r67_orig_amt;
	private BigDecimal r67_fore_amt;
	private BigDecimal r67_no_of_acc;

	

	
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
	
	

public String getR10_sche_fore_ass() {
		return r10_sche_fore_ass;
	}
	public void setR10_sche_fore_ass(String r10_sche_fore_ass) {
		this.r10_sche_fore_ass = r10_sche_fore_ass;
	}
	public BigDecimal getR10_orig_amt() {
		return r10_orig_amt;
	}
	public void setR10_orig_amt(BigDecimal r10_orig_amt) {
		this.r10_orig_amt = r10_orig_amt;
	}
	public BigDecimal getR10_fore_amt() {
		return r10_fore_amt;
	}
	public void setR10_fore_amt(BigDecimal r10_fore_amt) {
		this.r10_fore_amt = r10_fore_amt;
	}
	public BigDecimal getR10_no_of_acc() {
		return r10_no_of_acc;
	}
	public void setR10_no_of_acc(BigDecimal r10_no_of_acc) {
		this.r10_no_of_acc = r10_no_of_acc;
	}
	public String getR11_sche_fore_ass() {
		return r11_sche_fore_ass;
	}
	public void setR11_sche_fore_ass(String r11_sche_fore_ass) {
		this.r11_sche_fore_ass = r11_sche_fore_ass;
	}
	public BigDecimal getR11_orig_amt() {
		return r11_orig_amt;
	}
	public void setR11_orig_amt(BigDecimal r11_orig_amt) {
		this.r11_orig_amt = r11_orig_amt;
	}
	public BigDecimal getR11_fore_amt() {
		return r11_fore_amt;
	}
	public void setR11_fore_amt(BigDecimal r11_fore_amt) {
		this.r11_fore_amt = r11_fore_amt;
	}
	public BigDecimal getR11_no_of_acc() {
		return r11_no_of_acc;
	}
	public void setR11_no_of_acc(BigDecimal r11_no_of_acc) {
		this.r11_no_of_acc = r11_no_of_acc;
	}
	public String getR12_sche_fore_ass() {
		return r12_sche_fore_ass;
	}
	public void setR12_sche_fore_ass(String r12_sche_fore_ass) {
		this.r12_sche_fore_ass = r12_sche_fore_ass;
	}
	public BigDecimal getR12_orig_amt() {
		return r12_orig_amt;
	}
	public void setR12_orig_amt(BigDecimal r12_orig_amt) {
		this.r12_orig_amt = r12_orig_amt;
	}
	public BigDecimal getR12_fore_amt() {
		return r12_fore_amt;
	}
	public void setR12_fore_amt(BigDecimal r12_fore_amt) {
		this.r12_fore_amt = r12_fore_amt;
	}
	public BigDecimal getR12_no_of_acc() {
		return r12_no_of_acc;
	}
	public void setR12_no_of_acc(BigDecimal r12_no_of_acc) {
		this.r12_no_of_acc = r12_no_of_acc;
	}
	public String getR13_sche_fore_ass() {
		return r13_sche_fore_ass;
	}
	public void setR13_sche_fore_ass(String r13_sche_fore_ass) {
		this.r13_sche_fore_ass = r13_sche_fore_ass;
	}
	public BigDecimal getR13_orig_amt() {
		return r13_orig_amt;
	}
	public void setR13_orig_amt(BigDecimal r13_orig_amt) {
		this.r13_orig_amt = r13_orig_amt;
	}
	public BigDecimal getR13_fore_amt() {
		return r13_fore_amt;
	}
	public void setR13_fore_amt(BigDecimal r13_fore_amt) {
		this.r13_fore_amt = r13_fore_amt;
	}
	public BigDecimal getR13_no_of_acc() {
		return r13_no_of_acc;
	}
	public void setR13_no_of_acc(BigDecimal r13_no_of_acc) {
		this.r13_no_of_acc = r13_no_of_acc;
	}
	public String getR14_sche_fore_ass() {
		return r14_sche_fore_ass;
	}
	public void setR14_sche_fore_ass(String r14_sche_fore_ass) {
		this.r14_sche_fore_ass = r14_sche_fore_ass;
	}
	public BigDecimal getR14_orig_amt() {
		return r14_orig_amt;
	}
	public void setR14_orig_amt(BigDecimal r14_orig_amt) {
		this.r14_orig_amt = r14_orig_amt;
	}
	public BigDecimal getR14_fore_amt() {
		return r14_fore_amt;
	}
	public void setR14_fore_amt(BigDecimal r14_fore_amt) {
		this.r14_fore_amt = r14_fore_amt;
	}
	public BigDecimal getR14_no_of_acc() {
		return r14_no_of_acc;
	}
	public void setR14_no_of_acc(BigDecimal r14_no_of_acc) {
		this.r14_no_of_acc = r14_no_of_acc;
	}
	public String getR15_sche_fore_ass() {
		return r15_sche_fore_ass;
	}
	public void setR15_sche_fore_ass(String r15_sche_fore_ass) {
		this.r15_sche_fore_ass = r15_sche_fore_ass;
	}
	public BigDecimal getR15_orig_amt() {
		return r15_orig_amt;
	}
	public void setR15_orig_amt(BigDecimal r15_orig_amt) {
		this.r15_orig_amt = r15_orig_amt;
	}
	public BigDecimal getR15_fore_amt() {
		return r15_fore_amt;
	}
	public void setR15_fore_amt(BigDecimal r15_fore_amt) {
		this.r15_fore_amt = r15_fore_amt;
	}
	public BigDecimal getR15_no_of_acc() {
		return r15_no_of_acc;
	}
	public void setR15_no_of_acc(BigDecimal r15_no_of_acc) {
		this.r15_no_of_acc = r15_no_of_acc;
	}
	public String getR16_sche_fore_ass() {
		return r16_sche_fore_ass;
	}
	public void setR16_sche_fore_ass(String r16_sche_fore_ass) {
		this.r16_sche_fore_ass = r16_sche_fore_ass;
	}
	public BigDecimal getR16_orig_amt() {
		return r16_orig_amt;
	}
	public void setR16_orig_amt(BigDecimal r16_orig_amt) {
		this.r16_orig_amt = r16_orig_amt;
	}
	public BigDecimal getR16_fore_amt() {
		return r16_fore_amt;
	}
	public void setR16_fore_amt(BigDecimal r16_fore_amt) {
		this.r16_fore_amt = r16_fore_amt;
	}
	public BigDecimal getR16_no_of_acc() {
		return r16_no_of_acc;
	}
	public void setR16_no_of_acc(BigDecimal r16_no_of_acc) {
		this.r16_no_of_acc = r16_no_of_acc;
	}
	public String getR17_sche_fore_ass() {
		return r17_sche_fore_ass;
	}
	public void setR17_sche_fore_ass(String r17_sche_fore_ass) {
		this.r17_sche_fore_ass = r17_sche_fore_ass;
	}
	public BigDecimal getR17_orig_amt() {
		return r17_orig_amt;
	}
	public void setR17_orig_amt(BigDecimal r17_orig_amt) {
		this.r17_orig_amt = r17_orig_amt;
	}
	public BigDecimal getR17_fore_amt() {
		return r17_fore_amt;
	}
	public void setR17_fore_amt(BigDecimal r17_fore_amt) {
		this.r17_fore_amt = r17_fore_amt;
	}
	public BigDecimal getR17_no_of_acc() {
		return r17_no_of_acc;
	}
	public void setR17_no_of_acc(BigDecimal r17_no_of_acc) {
		this.r17_no_of_acc = r17_no_of_acc;
	}
	public String getR18_sche_fore_ass() {
		return r18_sche_fore_ass;
	}
	public void setR18_sche_fore_ass(String r18_sche_fore_ass) {
		this.r18_sche_fore_ass = r18_sche_fore_ass;
	}
	public BigDecimal getR18_orig_amt() {
		return r18_orig_amt;
	}
	public void setR18_orig_amt(BigDecimal r18_orig_amt) {
		this.r18_orig_amt = r18_orig_amt;
	}
	public BigDecimal getR18_fore_amt() {
		return r18_fore_amt;
	}
	public void setR18_fore_amt(BigDecimal r18_fore_amt) {
		this.r18_fore_amt = r18_fore_amt;
	}
	public BigDecimal getR18_no_of_acc() {
		return r18_no_of_acc;
	}
	public void setR18_no_of_acc(BigDecimal r18_no_of_acc) {
		this.r18_no_of_acc = r18_no_of_acc;
	}
	public String getR19_sche_fore_ass() {
		return r19_sche_fore_ass;
	}
	public void setR19_sche_fore_ass(String r19_sche_fore_ass) {
		this.r19_sche_fore_ass = r19_sche_fore_ass;
	}
	public BigDecimal getR19_orig_amt() {
		return r19_orig_amt;
	}
	public void setR19_orig_amt(BigDecimal r19_orig_amt) {
		this.r19_orig_amt = r19_orig_amt;
	}
	public BigDecimal getR19_fore_amt() {
		return r19_fore_amt;
	}
	public void setR19_fore_amt(BigDecimal r19_fore_amt) {
		this.r19_fore_amt = r19_fore_amt;
	}
	public BigDecimal getR19_no_of_acc() {
		return r19_no_of_acc;
	}
	public void setR19_no_of_acc(BigDecimal r19_no_of_acc) {
		this.r19_no_of_acc = r19_no_of_acc;
	}
	public String getR20_sche_fore_ass() {
		return r20_sche_fore_ass;
	}
	public void setR20_sche_fore_ass(String r20_sche_fore_ass) {
		this.r20_sche_fore_ass = r20_sche_fore_ass;
	}
	public BigDecimal getR20_orig_amt() {
		return r20_orig_amt;
	}
	public void setR20_orig_amt(BigDecimal r20_orig_amt) {
		this.r20_orig_amt = r20_orig_amt;
	}
	public BigDecimal getR20_fore_amt() {
		return r20_fore_amt;
	}
	public void setR20_fore_amt(BigDecimal r20_fore_amt) {
		this.r20_fore_amt = r20_fore_amt;
	}
	public BigDecimal getR20_no_of_acc() {
		return r20_no_of_acc;
	}
	public void setR20_no_of_acc(BigDecimal r20_no_of_acc) {
		this.r20_no_of_acc = r20_no_of_acc;
	}
	public String getR21_sche_fore_ass() {
		return r21_sche_fore_ass;
	}
	public void setR21_sche_fore_ass(String r21_sche_fore_ass) {
		this.r21_sche_fore_ass = r21_sche_fore_ass;
	}
	public BigDecimal getR21_orig_amt() {
		return r21_orig_amt;
	}
	public void setR21_orig_amt(BigDecimal r21_orig_amt) {
		this.r21_orig_amt = r21_orig_amt;
	}
	public BigDecimal getR21_fore_amt() {
		return r21_fore_amt;
	}
	public void setR21_fore_amt(BigDecimal r21_fore_amt) {
		this.r21_fore_amt = r21_fore_amt;
	}
	public BigDecimal getR21_no_of_acc() {
		return r21_no_of_acc;
	}
	public void setR21_no_of_acc(BigDecimal r21_no_of_acc) {
		this.r21_no_of_acc = r21_no_of_acc;
	}
	public String getR22_sche_fore_ass() {
		return r22_sche_fore_ass;
	}
	public void setR22_sche_fore_ass(String r22_sche_fore_ass) {
		this.r22_sche_fore_ass = r22_sche_fore_ass;
	}
	public BigDecimal getR22_orig_amt() {
		return r22_orig_amt;
	}
	public void setR22_orig_amt(BigDecimal r22_orig_amt) {
		this.r22_orig_amt = r22_orig_amt;
	}
	public BigDecimal getR22_fore_amt() {
		return r22_fore_amt;
	}
	public void setR22_fore_amt(BigDecimal r22_fore_amt) {
		this.r22_fore_amt = r22_fore_amt;
	}
	public BigDecimal getR22_no_of_acc() {
		return r22_no_of_acc;
	}
	public void setR22_no_of_acc(BigDecimal r22_no_of_acc) {
		this.r22_no_of_acc = r22_no_of_acc;
	}
	public String getR23_sche_fore_ass() {
		return r23_sche_fore_ass;
	}
	public void setR23_sche_fore_ass(String r23_sche_fore_ass) {
		this.r23_sche_fore_ass = r23_sche_fore_ass;
	}
	public BigDecimal getR23_orig_amt() {
		return r23_orig_amt;
	}
	public void setR23_orig_amt(BigDecimal r23_orig_amt) {
		this.r23_orig_amt = r23_orig_amt;
	}
	public BigDecimal getR23_fore_amt() {
		return r23_fore_amt;
	}
	public void setR23_fore_amt(BigDecimal r23_fore_amt) {
		this.r23_fore_amt = r23_fore_amt;
	}
	public BigDecimal getR23_no_of_acc() {
		return r23_no_of_acc;
	}
	public void setR23_no_of_acc(BigDecimal r23_no_of_acc) {
		this.r23_no_of_acc = r23_no_of_acc;
	}
	public String getR24_sche_fore_ass() {
		return r24_sche_fore_ass;
	}
	public void setR24_sche_fore_ass(String r24_sche_fore_ass) {
		this.r24_sche_fore_ass = r24_sche_fore_ass;
	}
	public BigDecimal getR24_orig_amt() {
		return r24_orig_amt;
	}
	public void setR24_orig_amt(BigDecimal r24_orig_amt) {
		this.r24_orig_amt = r24_orig_amt;
	}
	public BigDecimal getR24_fore_amt() {
		return r24_fore_amt;
	}
	public void setR24_fore_amt(BigDecimal r24_fore_amt) {
		this.r24_fore_amt = r24_fore_amt;
	}
	public BigDecimal getR24_no_of_acc() {
		return r24_no_of_acc;
	}
	public void setR24_no_of_acc(BigDecimal r24_no_of_acc) {
		this.r24_no_of_acc = r24_no_of_acc;
	}
	public String getR25_sche_fore_ass() {
		return r25_sche_fore_ass;
	}
	public void setR25_sche_fore_ass(String r25_sche_fore_ass) {
		this.r25_sche_fore_ass = r25_sche_fore_ass;
	}
	public BigDecimal getR25_orig_amt() {
		return r25_orig_amt;
	}
	public void setR25_orig_amt(BigDecimal r25_orig_amt) {
		this.r25_orig_amt = r25_orig_amt;
	}
	public BigDecimal getR25_fore_amt() {
		return r25_fore_amt;
	}
	public void setR25_fore_amt(BigDecimal r25_fore_amt) {
		this.r25_fore_amt = r25_fore_amt;
	}
	public BigDecimal getR25_no_of_acc() {
		return r25_no_of_acc;
	}
	public void setR25_no_of_acc(BigDecimal r25_no_of_acc) {
		this.r25_no_of_acc = r25_no_of_acc;
	}
	public String getR26_sche_fore_ass() {
		return r26_sche_fore_ass;
	}
	public void setR26_sche_fore_ass(String r26_sche_fore_ass) {
		this.r26_sche_fore_ass = r26_sche_fore_ass;
	}
	public BigDecimal getR26_orig_amt() {
		return r26_orig_amt;
	}
	public void setR26_orig_amt(BigDecimal r26_orig_amt) {
		this.r26_orig_amt = r26_orig_amt;
	}
	public BigDecimal getR26_fore_amt() {
		return r26_fore_amt;
	}
	public void setR26_fore_amt(BigDecimal r26_fore_amt) {
		this.r26_fore_amt = r26_fore_amt;
	}
	public BigDecimal getR26_no_of_acc() {
		return r26_no_of_acc;
	}
	public void setR26_no_of_acc(BigDecimal r26_no_of_acc) {
		this.r26_no_of_acc = r26_no_of_acc;
	}
	public String getR27_sche_fore_ass() {
		return r27_sche_fore_ass;
	}
	public void setR27_sche_fore_ass(String r27_sche_fore_ass) {
		this.r27_sche_fore_ass = r27_sche_fore_ass;
	}
	public BigDecimal getR27_orig_amt() {
		return r27_orig_amt;
	}
	public void setR27_orig_amt(BigDecimal r27_orig_amt) {
		this.r27_orig_amt = r27_orig_amt;
	}
	public BigDecimal getR27_fore_amt() {
		return r27_fore_amt;
	}
	public void setR27_fore_amt(BigDecimal r27_fore_amt) {
		this.r27_fore_amt = r27_fore_amt;
	}
	public BigDecimal getR27_no_of_acc() {
		return r27_no_of_acc;
	}
	public void setR27_no_of_acc(BigDecimal r27_no_of_acc) {
		this.r27_no_of_acc = r27_no_of_acc;
	}
	public String getR28_sche_fore_ass() {
		return r28_sche_fore_ass;
	}
	public void setR28_sche_fore_ass(String r28_sche_fore_ass) {
		this.r28_sche_fore_ass = r28_sche_fore_ass;
	}
	public BigDecimal getR28_orig_amt() {
		return r28_orig_amt;
	}
	public void setR28_orig_amt(BigDecimal r28_orig_amt) {
		this.r28_orig_amt = r28_orig_amt;
	}
	public BigDecimal getR28_fore_amt() {
		return r28_fore_amt;
	}
	public void setR28_fore_amt(BigDecimal r28_fore_amt) {
		this.r28_fore_amt = r28_fore_amt;
	}
	public BigDecimal getR28_no_of_acc() {
		return r28_no_of_acc;
	}
	public void setR28_no_of_acc(BigDecimal r28_no_of_acc) {
		this.r28_no_of_acc = r28_no_of_acc;
	}
	public String getR29_sche_fore_ass() {
		return r29_sche_fore_ass;
	}
	public void setR29_sche_fore_ass(String r29_sche_fore_ass) {
		this.r29_sche_fore_ass = r29_sche_fore_ass;
	}
	public BigDecimal getR29_orig_amt() {
		return r29_orig_amt;
	}
	public void setR29_orig_amt(BigDecimal r29_orig_amt) {
		this.r29_orig_amt = r29_orig_amt;
	}
	public BigDecimal getR29_fore_amt() {
		return r29_fore_amt;
	}
	public void setR29_fore_amt(BigDecimal r29_fore_amt) {
		this.r29_fore_amt = r29_fore_amt;
	}
	public BigDecimal getR29_no_of_acc() {
		return r29_no_of_acc;
	}
	public void setR29_no_of_acc(BigDecimal r29_no_of_acc) {
		this.r29_no_of_acc = r29_no_of_acc;
	}
	public String getR30_sche_fore_ass() {
		return r30_sche_fore_ass;
	}
	public void setR30_sche_fore_ass(String r30_sche_fore_ass) {
		this.r30_sche_fore_ass = r30_sche_fore_ass;
	}
	public BigDecimal getR30_orig_amt() {
		return r30_orig_amt;
	}
	public void setR30_orig_amt(BigDecimal r30_orig_amt) {
		this.r30_orig_amt = r30_orig_amt;
	}
	public BigDecimal getR30_fore_amt() {
		return r30_fore_amt;
	}
	public void setR30_fore_amt(BigDecimal r30_fore_amt) {
		this.r30_fore_amt = r30_fore_amt;
	}
	public BigDecimal getR30_no_of_acc() {
		return r30_no_of_acc;
	}
	public void setR30_no_of_acc(BigDecimal r30_no_of_acc) {
		this.r30_no_of_acc = r30_no_of_acc;
	}
	public String getR31_sche_fore_ass() {
		return r31_sche_fore_ass;
	}
	public void setR31_sche_fore_ass(String r31_sche_fore_ass) {
		this.r31_sche_fore_ass = r31_sche_fore_ass;
	}
	public BigDecimal getR31_orig_amt() {
		return r31_orig_amt;
	}
	public void setR31_orig_amt(BigDecimal r31_orig_amt) {
		this.r31_orig_amt = r31_orig_amt;
	}
	public BigDecimal getR31_fore_amt() {
		return r31_fore_amt;
	}
	public void setR31_fore_amt(BigDecimal r31_fore_amt) {
		this.r31_fore_amt = r31_fore_amt;
	}
	public BigDecimal getR31_no_of_acc() {
		return r31_no_of_acc;
	}
	public void setR31_no_of_acc(BigDecimal r31_no_of_acc) {
		this.r31_no_of_acc = r31_no_of_acc;
	}
	public String getR32_sche_fore_ass() {
		return r32_sche_fore_ass;
	}
	public void setR32_sche_fore_ass(String r32_sche_fore_ass) {
		this.r32_sche_fore_ass = r32_sche_fore_ass;
	}
	public BigDecimal getR32_orig_amt() {
		return r32_orig_amt;
	}
	public void setR32_orig_amt(BigDecimal r32_orig_amt) {
		this.r32_orig_amt = r32_orig_amt;
	}
	public BigDecimal getR32_fore_amt() {
		return r32_fore_amt;
	}
	public void setR32_fore_amt(BigDecimal r32_fore_amt) {
		this.r32_fore_amt = r32_fore_amt;
	}
	public BigDecimal getR32_no_of_acc() {
		return r32_no_of_acc;
	}
	public void setR32_no_of_acc(BigDecimal r32_no_of_acc) {
		this.r32_no_of_acc = r32_no_of_acc;
	}
	public String getR33_sche_fore_ass() {
		return r33_sche_fore_ass;
	}
	public void setR33_sche_fore_ass(String r33_sche_fore_ass) {
		this.r33_sche_fore_ass = r33_sche_fore_ass;
	}
	public BigDecimal getR33_orig_amt() {
		return r33_orig_amt;
	}
	public void setR33_orig_amt(BigDecimal r33_orig_amt) {
		this.r33_orig_amt = r33_orig_amt;
	}
	public BigDecimal getR33_fore_amt() {
		return r33_fore_amt;
	}
	public void setR33_fore_amt(BigDecimal r33_fore_amt) {
		this.r33_fore_amt = r33_fore_amt;
	}
	public BigDecimal getR33_no_of_acc() {
		return r33_no_of_acc;
	}
	public void setR33_no_of_acc(BigDecimal r33_no_of_acc) {
		this.r33_no_of_acc = r33_no_of_acc;
	}
	public String getR34_sche_fore_ass() {
		return r34_sche_fore_ass;
	}
	public void setR34_sche_fore_ass(String r34_sche_fore_ass) {
		this.r34_sche_fore_ass = r34_sche_fore_ass;
	}
	public BigDecimal getR34_orig_amt() {
		return r34_orig_amt;
	}
	public void setR34_orig_amt(BigDecimal r34_orig_amt) {
		this.r34_orig_amt = r34_orig_amt;
	}
	public BigDecimal getR34_fore_amt() {
		return r34_fore_amt;
	}
	public void setR34_fore_amt(BigDecimal r34_fore_amt) {
		this.r34_fore_amt = r34_fore_amt;
	}
	public BigDecimal getR34_no_of_acc() {
		return r34_no_of_acc;
	}
	public void setR34_no_of_acc(BigDecimal r34_no_of_acc) {
		this.r34_no_of_acc = r34_no_of_acc;
	}
	public String getR35_sche_fore_ass() {
		return r35_sche_fore_ass;
	}
	public void setR35_sche_fore_ass(String r35_sche_fore_ass) {
		this.r35_sche_fore_ass = r35_sche_fore_ass;
	}
	public BigDecimal getR35_orig_amt() {
		return r35_orig_amt;
	}
	public void setR35_orig_amt(BigDecimal r35_orig_amt) {
		this.r35_orig_amt = r35_orig_amt;
	}
	public BigDecimal getR35_fore_amt() {
		return r35_fore_amt;
	}
	public void setR35_fore_amt(BigDecimal r35_fore_amt) {
		this.r35_fore_amt = r35_fore_amt;
	}
	public BigDecimal getR35_no_of_acc() {
		return r35_no_of_acc;
	}
	public void setR35_no_of_acc(BigDecimal r35_no_of_acc) {
		this.r35_no_of_acc = r35_no_of_acc;
	}
	public String getR36_sche_fore_ass() {
		return r36_sche_fore_ass;
	}
	public void setR36_sche_fore_ass(String r36_sche_fore_ass) {
		this.r36_sche_fore_ass = r36_sche_fore_ass;
	}
	public BigDecimal getR36_orig_amt() {
		return r36_orig_amt;
	}
	public void setR36_orig_amt(BigDecimal r36_orig_amt) {
		this.r36_orig_amt = r36_orig_amt;
	}
	public BigDecimal getR36_fore_amt() {
		return r36_fore_amt;
	}
	public void setR36_fore_amt(BigDecimal r36_fore_amt) {
		this.r36_fore_amt = r36_fore_amt;
	}
	public BigDecimal getR36_no_of_acc() {
		return r36_no_of_acc;
	}
	public void setR36_no_of_acc(BigDecimal r36_no_of_acc) {
		this.r36_no_of_acc = r36_no_of_acc;
	}
	public String getR37_sche_fore_ass() {
		return r37_sche_fore_ass;
	}
	public void setR37_sche_fore_ass(String r37_sche_fore_ass) {
		this.r37_sche_fore_ass = r37_sche_fore_ass;
	}
	public BigDecimal getR37_orig_amt() {
		return r37_orig_amt;
	}
	public void setR37_orig_amt(BigDecimal r37_orig_amt) {
		this.r37_orig_amt = r37_orig_amt;
	}
	public BigDecimal getR37_fore_amt() {
		return r37_fore_amt;
	}
	public void setR37_fore_amt(BigDecimal r37_fore_amt) {
		this.r37_fore_amt = r37_fore_amt;
	}
	public BigDecimal getR37_no_of_acc() {
		return r37_no_of_acc;
	}
	public void setR37_no_of_acc(BigDecimal r37_no_of_acc) {
		this.r37_no_of_acc = r37_no_of_acc;
	}
	public String getR38_sche_fore_ass() {
		return r38_sche_fore_ass;
	}
	public void setR38_sche_fore_ass(String r38_sche_fore_ass) {
		this.r38_sche_fore_ass = r38_sche_fore_ass;
	}
	public BigDecimal getR38_orig_amt() {
		return r38_orig_amt;
	}
	public void setR38_orig_amt(BigDecimal r38_orig_amt) {
		this.r38_orig_amt = r38_orig_amt;
	}
	public BigDecimal getR38_fore_amt() {
		return r38_fore_amt;
	}
	public void setR38_fore_amt(BigDecimal r38_fore_amt) {
		this.r38_fore_amt = r38_fore_amt;
	}
	public BigDecimal getR38_no_of_acc() {
		return r38_no_of_acc;
	}
	public void setR38_no_of_acc(BigDecimal r38_no_of_acc) {
		this.r38_no_of_acc = r38_no_of_acc;
	}
	public String getR39_sche_fore_ass() {
		return r39_sche_fore_ass;
	}
	public void setR39_sche_fore_ass(String r39_sche_fore_ass) {
		this.r39_sche_fore_ass = r39_sche_fore_ass;
	}
	public BigDecimal getR39_orig_amt() {
		return r39_orig_amt;
	}
	public void setR39_orig_amt(BigDecimal r39_orig_amt) {
		this.r39_orig_amt = r39_orig_amt;
	}
	public BigDecimal getR39_fore_amt() {
		return r39_fore_amt;
	}
	public void setR39_fore_amt(BigDecimal r39_fore_amt) {
		this.r39_fore_amt = r39_fore_amt;
	}
	public BigDecimal getR39_no_of_acc() {
		return r39_no_of_acc;
	}
	public void setR39_no_of_acc(BigDecimal r39_no_of_acc) {
		this.r39_no_of_acc = r39_no_of_acc;
	}
	public String getR40_sche_fore_ass() {
		return r40_sche_fore_ass;
	}
	public void setR40_sche_fore_ass(String r40_sche_fore_ass) {
		this.r40_sche_fore_ass = r40_sche_fore_ass;
	}
	public BigDecimal getR40_orig_amt() {
		return r40_orig_amt;
	}
	public void setR40_orig_amt(BigDecimal r40_orig_amt) {
		this.r40_orig_amt = r40_orig_amt;
	}
	public BigDecimal getR40_fore_amt() {
		return r40_fore_amt;
	}
	public void setR40_fore_amt(BigDecimal r40_fore_amt) {
		this.r40_fore_amt = r40_fore_amt;
	}
	public BigDecimal getR40_no_of_acc() {
		return r40_no_of_acc;
	}
	public void setR40_no_of_acc(BigDecimal r40_no_of_acc) {
		this.r40_no_of_acc = r40_no_of_acc;
	}
	public String getR41_sche_fore_ass() {
		return r41_sche_fore_ass;
	}
	public void setR41_sche_fore_ass(String r41_sche_fore_ass) {
		this.r41_sche_fore_ass = r41_sche_fore_ass;
	}
	public BigDecimal getR41_orig_amt() {
		return r41_orig_amt;
	}
	public void setR41_orig_amt(BigDecimal r41_orig_amt) {
		this.r41_orig_amt = r41_orig_amt;
	}
	public BigDecimal getR41_fore_amt() {
		return r41_fore_amt;
	}
	public void setR41_fore_amt(BigDecimal r41_fore_amt) {
		this.r41_fore_amt = r41_fore_amt;
	}
	public BigDecimal getR41_no_of_acc() {
		return r41_no_of_acc;
	}
	public void setR41_no_of_acc(BigDecimal r41_no_of_acc) {
		this.r41_no_of_acc = r41_no_of_acc;
	}
	public String getR42_sche_fore_ass() {
		return r42_sche_fore_ass;
	}
	public void setR42_sche_fore_ass(String r42_sche_fore_ass) {
		this.r42_sche_fore_ass = r42_sche_fore_ass;
	}
	public BigDecimal getR42_orig_amt() {
		return r42_orig_amt;
	}
	public void setR42_orig_amt(BigDecimal r42_orig_amt) {
		this.r42_orig_amt = r42_orig_amt;
	}
	public BigDecimal getR42_fore_amt() {
		return r42_fore_amt;
	}
	public void setR42_fore_amt(BigDecimal r42_fore_amt) {
		this.r42_fore_amt = r42_fore_amt;
	}
	public BigDecimal getR42_no_of_acc() {
		return r42_no_of_acc;
	}
	public void setR42_no_of_acc(BigDecimal r42_no_of_acc) {
		this.r42_no_of_acc = r42_no_of_acc;
	}
	public String getR43_sche_fore_ass() {
		return r43_sche_fore_ass;
	}
	public void setR43_sche_fore_ass(String r43_sche_fore_ass) {
		this.r43_sche_fore_ass = r43_sche_fore_ass;
	}
	public BigDecimal getR43_orig_amt() {
		return r43_orig_amt;
	}
	public void setR43_orig_amt(BigDecimal r43_orig_amt) {
		this.r43_orig_amt = r43_orig_amt;
	}
	public BigDecimal getR43_fore_amt() {
		return r43_fore_amt;
	}
	public void setR43_fore_amt(BigDecimal r43_fore_amt) {
		this.r43_fore_amt = r43_fore_amt;
	}
	public BigDecimal getR43_no_of_acc() {
		return r43_no_of_acc;
	}
	public void setR43_no_of_acc(BigDecimal r43_no_of_acc) {
		this.r43_no_of_acc = r43_no_of_acc;
	}
	public String getR44_sche_fore_ass() {
		return r44_sche_fore_ass;
	}
	public void setR44_sche_fore_ass(String r44_sche_fore_ass) {
		this.r44_sche_fore_ass = r44_sche_fore_ass;
	}
	public BigDecimal getR44_orig_amt() {
		return r44_orig_amt;
	}
	public void setR44_orig_amt(BigDecimal r44_orig_amt) {
		this.r44_orig_amt = r44_orig_amt;
	}
	public BigDecimal getR44_fore_amt() {
		return r44_fore_amt;
	}
	public void setR44_fore_amt(BigDecimal r44_fore_amt) {
		this.r44_fore_amt = r44_fore_amt;
	}
	public BigDecimal getR44_no_of_acc() {
		return r44_no_of_acc;
	}
	public void setR44_no_of_acc(BigDecimal r44_no_of_acc) {
		this.r44_no_of_acc = r44_no_of_acc;
	}
	public String getR45_sche_fore_ass() {
		return r45_sche_fore_ass;
	}
	public void setR45_sche_fore_ass(String r45_sche_fore_ass) {
		this.r45_sche_fore_ass = r45_sche_fore_ass;
	}
	public BigDecimal getR45_orig_amt() {
		return r45_orig_amt;
	}
	public void setR45_orig_amt(BigDecimal r45_orig_amt) {
		this.r45_orig_amt = r45_orig_amt;
	}
	public BigDecimal getR45_fore_amt() {
		return r45_fore_amt;
	}
	public void setR45_fore_amt(BigDecimal r45_fore_amt) {
		this.r45_fore_amt = r45_fore_amt;
	}
	public BigDecimal getR45_no_of_acc() {
		return r45_no_of_acc;
	}
	public void setR45_no_of_acc(BigDecimal r45_no_of_acc) {
		this.r45_no_of_acc = r45_no_of_acc;
	}
	public String getR46_sche_fore_ass() {
		return r46_sche_fore_ass;
	}
	public void setR46_sche_fore_ass(String r46_sche_fore_ass) {
		this.r46_sche_fore_ass = r46_sche_fore_ass;
	}
	public BigDecimal getR46_orig_amt() {
		return r46_orig_amt;
	}
	public void setR46_orig_amt(BigDecimal r46_orig_amt) {
		this.r46_orig_amt = r46_orig_amt;
	}
	public BigDecimal getR46_fore_amt() {
		return r46_fore_amt;
	}
	public void setR46_fore_amt(BigDecimal r46_fore_amt) {
		this.r46_fore_amt = r46_fore_amt;
	}
	public BigDecimal getR46_no_of_acc() {
		return r46_no_of_acc;
	}
	public void setR46_no_of_acc(BigDecimal r46_no_of_acc) {
		this.r46_no_of_acc = r46_no_of_acc;
	}
	public String getR47_sche_fore_ass() {
		return r47_sche_fore_ass;
	}
	public void setR47_sche_fore_ass(String r47_sche_fore_ass) {
		this.r47_sche_fore_ass = r47_sche_fore_ass;
	}
	public BigDecimal getR47_orig_amt() {
		return r47_orig_amt;
	}
	public void setR47_orig_amt(BigDecimal r47_orig_amt) {
		this.r47_orig_amt = r47_orig_amt;
	}
	public BigDecimal getR47_fore_amt() {
		return r47_fore_amt;
	}
	public void setR47_fore_amt(BigDecimal r47_fore_amt) {
		this.r47_fore_amt = r47_fore_amt;
	}
	public BigDecimal getR47_no_of_acc() {
		return r47_no_of_acc;
	}
	public void setR47_no_of_acc(BigDecimal r47_no_of_acc) {
		this.r47_no_of_acc = r47_no_of_acc;
	}
	public String getR48_sche_fore_ass() {
		return r48_sche_fore_ass;
	}
	public void setR48_sche_fore_ass(String r48_sche_fore_ass) {
		this.r48_sche_fore_ass = r48_sche_fore_ass;
	}
	public BigDecimal getR48_orig_amt() {
		return r48_orig_amt;
	}
	public void setR48_orig_amt(BigDecimal r48_orig_amt) {
		this.r48_orig_amt = r48_orig_amt;
	}
	public BigDecimal getR48_fore_amt() {
		return r48_fore_amt;
	}
	public void setR48_fore_amt(BigDecimal r48_fore_amt) {
		this.r48_fore_amt = r48_fore_amt;
	}
	public BigDecimal getR48_no_of_acc() {
		return r48_no_of_acc;
	}
	public void setR48_no_of_acc(BigDecimal r48_no_of_acc) {
		this.r48_no_of_acc = r48_no_of_acc;
	}
	public String getR49_sche_fore_ass() {
		return r49_sche_fore_ass;
	}
	public void setR49_sche_fore_ass(String r49_sche_fore_ass) {
		this.r49_sche_fore_ass = r49_sche_fore_ass;
	}
	public BigDecimal getR49_orig_amt() {
		return r49_orig_amt;
	}
	public void setR49_orig_amt(BigDecimal r49_orig_amt) {
		this.r49_orig_amt = r49_orig_amt;
	}
	public BigDecimal getR49_fore_amt() {
		return r49_fore_amt;
	}
	public void setR49_fore_amt(BigDecimal r49_fore_amt) {
		this.r49_fore_amt = r49_fore_amt;
	}
	public BigDecimal getR49_no_of_acc() {
		return r49_no_of_acc;
	}
	public void setR49_no_of_acc(BigDecimal r49_no_of_acc) {
		this.r49_no_of_acc = r49_no_of_acc;
	}
	public String getR50_sche_fore_ass() {
		return r50_sche_fore_ass;
	}
	public void setR50_sche_fore_ass(String r50_sche_fore_ass) {
		this.r50_sche_fore_ass = r50_sche_fore_ass;
	}
	public BigDecimal getR50_orig_amt() {
		return r50_orig_amt;
	}
	public void setR50_orig_amt(BigDecimal r50_orig_amt) {
		this.r50_orig_amt = r50_orig_amt;
	}
	public BigDecimal getR50_fore_amt() {
		return r50_fore_amt;
	}
	public void setR50_fore_amt(BigDecimal r50_fore_amt) {
		this.r50_fore_amt = r50_fore_amt;
	}
	public BigDecimal getR50_no_of_acc() {
		return r50_no_of_acc;
	}
	public void setR50_no_of_acc(BigDecimal r50_no_of_acc) {
		this.r50_no_of_acc = r50_no_of_acc;
	}
	public String getR51_sche_fore_ass() {
		return r51_sche_fore_ass;
	}
	public void setR51_sche_fore_ass(String r51_sche_fore_ass) {
		this.r51_sche_fore_ass = r51_sche_fore_ass;
	}
	public BigDecimal getR51_orig_amt() {
		return r51_orig_amt;
	}
	public void setR51_orig_amt(BigDecimal r51_orig_amt) {
		this.r51_orig_amt = r51_orig_amt;
	}
	public BigDecimal getR51_fore_amt() {
		return r51_fore_amt;
	}
	public void setR51_fore_amt(BigDecimal r51_fore_amt) {
		this.r51_fore_amt = r51_fore_amt;
	}
	public BigDecimal getR51_no_of_acc() {
		return r51_no_of_acc;
	}
	public void setR51_no_of_acc(BigDecimal r51_no_of_acc) {
		this.r51_no_of_acc = r51_no_of_acc;
	}
	public String getR52_sche_fore_ass() {
		return r52_sche_fore_ass;
	}
	public void setR52_sche_fore_ass(String r52_sche_fore_ass) {
		this.r52_sche_fore_ass = r52_sche_fore_ass;
	}
	public BigDecimal getR52_orig_amt() {
		return r52_orig_amt;
	}
	public void setR52_orig_amt(BigDecimal r52_orig_amt) {
		this.r52_orig_amt = r52_orig_amt;
	}
	public BigDecimal getR52_fore_amt() {
		return r52_fore_amt;
	}
	public void setR52_fore_amt(BigDecimal r52_fore_amt) {
		this.r52_fore_amt = r52_fore_amt;
	}
	public BigDecimal getR52_no_of_acc() {
		return r52_no_of_acc;
	}
	public void setR52_no_of_acc(BigDecimal r52_no_of_acc) {
		this.r52_no_of_acc = r52_no_of_acc;
	}
	public String getR53_sche_fore_ass() {
		return r53_sche_fore_ass;
	}
	public void setR53_sche_fore_ass(String r53_sche_fore_ass) {
		this.r53_sche_fore_ass = r53_sche_fore_ass;
	}
	public BigDecimal getR53_orig_amt() {
		return r53_orig_amt;
	}
	public void setR53_orig_amt(BigDecimal r53_orig_amt) {
		this.r53_orig_amt = r53_orig_amt;
	}
	public BigDecimal getR53_fore_amt() {
		return r53_fore_amt;
	}
	public void setR53_fore_amt(BigDecimal r53_fore_amt) {
		this.r53_fore_amt = r53_fore_amt;
	}
	public BigDecimal getR53_no_of_acc() {
		return r53_no_of_acc;
	}
	public void setR53_no_of_acc(BigDecimal r53_no_of_acc) {
		this.r53_no_of_acc = r53_no_of_acc;
	}
	public String getR54_sche_fore_ass() {
		return r54_sche_fore_ass;
	}
	public void setR54_sche_fore_ass(String r54_sche_fore_ass) {
		this.r54_sche_fore_ass = r54_sche_fore_ass;
	}
	public BigDecimal getR54_orig_amt() {
		return r54_orig_amt;
	}
	public void setR54_orig_amt(BigDecimal r54_orig_amt) {
		this.r54_orig_amt = r54_orig_amt;
	}
	public BigDecimal getR54_fore_amt() {
		return r54_fore_amt;
	}
	public void setR54_fore_amt(BigDecimal r54_fore_amt) {
		this.r54_fore_amt = r54_fore_amt;
	}
	public BigDecimal getR54_no_of_acc() {
		return r54_no_of_acc;
	}
	public void setR54_no_of_acc(BigDecimal r54_no_of_acc) {
		this.r54_no_of_acc = r54_no_of_acc;
	}
	public String getR55_sche_fore_ass() {
		return r55_sche_fore_ass;
	}
	public void setR55_sche_fore_ass(String r55_sche_fore_ass) {
		this.r55_sche_fore_ass = r55_sche_fore_ass;
	}
	public BigDecimal getR55_orig_amt() {
		return r55_orig_amt;
	}
	public void setR55_orig_amt(BigDecimal r55_orig_amt) {
		this.r55_orig_amt = r55_orig_amt;
	}
	public BigDecimal getR55_fore_amt() {
		return r55_fore_amt;
	}
	public void setR55_fore_amt(BigDecimal r55_fore_amt) {
		this.r55_fore_amt = r55_fore_amt;
	}
	public BigDecimal getR55_no_of_acc() {
		return r55_no_of_acc;
	}
	public void setR55_no_of_acc(BigDecimal r55_no_of_acc) {
		this.r55_no_of_acc = r55_no_of_acc;
	}
	public String getR56_sche_fore_ass() {
		return r56_sche_fore_ass;
	}
	public void setR56_sche_fore_ass(String r56_sche_fore_ass) {
		this.r56_sche_fore_ass = r56_sche_fore_ass;
	}
	public BigDecimal getR56_orig_amt() {
		return r56_orig_amt;
	}
	public void setR56_orig_amt(BigDecimal r56_orig_amt) {
		this.r56_orig_amt = r56_orig_amt;
	}
	public BigDecimal getR56_fore_amt() {
		return r56_fore_amt;
	}
	public void setR56_fore_amt(BigDecimal r56_fore_amt) {
		this.r56_fore_amt = r56_fore_amt;
	}
	public BigDecimal getR56_no_of_acc() {
		return r56_no_of_acc;
	}
	public void setR56_no_of_acc(BigDecimal r56_no_of_acc) {
		this.r56_no_of_acc = r56_no_of_acc;
	}
	public String getR57_sche_fore_ass() {
		return r57_sche_fore_ass;
	}
	public void setR57_sche_fore_ass(String r57_sche_fore_ass) {
		this.r57_sche_fore_ass = r57_sche_fore_ass;
	}
	public BigDecimal getR57_orig_amt() {
		return r57_orig_amt;
	}
	public void setR57_orig_amt(BigDecimal r57_orig_amt) {
		this.r57_orig_amt = r57_orig_amt;
	}
	public BigDecimal getR57_fore_amt() {
		return r57_fore_amt;
	}
	public void setR57_fore_amt(BigDecimal r57_fore_amt) {
		this.r57_fore_amt = r57_fore_amt;
	}
	public BigDecimal getR57_no_of_acc() {
		return r57_no_of_acc;
	}
	public void setR57_no_of_acc(BigDecimal r57_no_of_acc) {
		this.r57_no_of_acc = r57_no_of_acc;
	}
	public String getR58_sche_fore_ass() {
		return r58_sche_fore_ass;
	}
	public void setR58_sche_fore_ass(String r58_sche_fore_ass) {
		this.r58_sche_fore_ass = r58_sche_fore_ass;
	}
	public BigDecimal getR58_orig_amt() {
		return r58_orig_amt;
	}
	public void setR58_orig_amt(BigDecimal r58_orig_amt) {
		this.r58_orig_amt = r58_orig_amt;
	}
	public BigDecimal getR58_fore_amt() {
		return r58_fore_amt;
	}
	public void setR58_fore_amt(BigDecimal r58_fore_amt) {
		this.r58_fore_amt = r58_fore_amt;
	}
	public BigDecimal getR58_no_of_acc() {
		return r58_no_of_acc;
	}
	public void setR58_no_of_acc(BigDecimal r58_no_of_acc) {
		this.r58_no_of_acc = r58_no_of_acc;
	}
	public String getR59_sche_fore_ass() {
		return r59_sche_fore_ass;
	}
	public void setR59_sche_fore_ass(String r59_sche_fore_ass) {
		this.r59_sche_fore_ass = r59_sche_fore_ass;
	}
	public BigDecimal getR59_orig_amt() {
		return r59_orig_amt;
	}
	public void setR59_orig_amt(BigDecimal r59_orig_amt) {
		this.r59_orig_amt = r59_orig_amt;
	}
	public BigDecimal getR59_fore_amt() {
		return r59_fore_amt;
	}
	public void setR59_fore_amt(BigDecimal r59_fore_amt) {
		this.r59_fore_amt = r59_fore_amt;
	}
	public BigDecimal getR59_no_of_acc() {
		return r59_no_of_acc;
	}
	public void setR59_no_of_acc(BigDecimal r59_no_of_acc) {
		this.r59_no_of_acc = r59_no_of_acc;
	}
	public String getR60_sche_fore_ass() {
		return r60_sche_fore_ass;
	}
	public void setR60_sche_fore_ass(String r60_sche_fore_ass) {
		this.r60_sche_fore_ass = r60_sche_fore_ass;
	}
	public BigDecimal getR60_orig_amt() {
		return r60_orig_amt;
	}
	public void setR60_orig_amt(BigDecimal r60_orig_amt) {
		this.r60_orig_amt = r60_orig_amt;
	}
	public BigDecimal getR60_fore_amt() {
		return r60_fore_amt;
	}
	public void setR60_fore_amt(BigDecimal r60_fore_amt) {
		this.r60_fore_amt = r60_fore_amt;
	}
	public BigDecimal getR60_no_of_acc() {
		return r60_no_of_acc;
	}
	public void setR60_no_of_acc(BigDecimal r60_no_of_acc) {
		this.r60_no_of_acc = r60_no_of_acc;
	}
	public String getR61_sche_fore_ass() {
		return r61_sche_fore_ass;
	}
	public void setR61_sche_fore_ass(String r61_sche_fore_ass) {
		this.r61_sche_fore_ass = r61_sche_fore_ass;
	}
	public BigDecimal getR61_orig_amt() {
		return r61_orig_amt;
	}
	public void setR61_orig_amt(BigDecimal r61_orig_amt) {
		this.r61_orig_amt = r61_orig_amt;
	}
	public BigDecimal getR61_fore_amt() {
		return r61_fore_amt;
	}
	public void setR61_fore_amt(BigDecimal r61_fore_amt) {
		this.r61_fore_amt = r61_fore_amt;
	}
	public BigDecimal getR61_no_of_acc() {
		return r61_no_of_acc;
	}
	public void setR61_no_of_acc(BigDecimal r61_no_of_acc) {
		this.r61_no_of_acc = r61_no_of_acc;
	}
	public String getR62_sche_fore_ass() {
		return r62_sche_fore_ass;
	}
	public void setR62_sche_fore_ass(String r62_sche_fore_ass) {
		this.r62_sche_fore_ass = r62_sche_fore_ass;
	}
	public BigDecimal getR62_orig_amt() {
		return r62_orig_amt;
	}
	public void setR62_orig_amt(BigDecimal r62_orig_amt) {
		this.r62_orig_amt = r62_orig_amt;
	}
	public BigDecimal getR62_fore_amt() {
		return r62_fore_amt;
	}
	public void setR62_fore_amt(BigDecimal r62_fore_amt) {
		this.r62_fore_amt = r62_fore_amt;
	}
	public BigDecimal getR62_no_of_acc() {
		return r62_no_of_acc;
	}
	public void setR62_no_of_acc(BigDecimal r62_no_of_acc) {
		this.r62_no_of_acc = r62_no_of_acc;
	}
	public String getR63_sche_fore_ass() {
		return r63_sche_fore_ass;
	}
	public void setR63_sche_fore_ass(String r63_sche_fore_ass) {
		this.r63_sche_fore_ass = r63_sche_fore_ass;
	}
	public BigDecimal getR63_orig_amt() {
		return r63_orig_amt;
	}
	public void setR63_orig_amt(BigDecimal r63_orig_amt) {
		this.r63_orig_amt = r63_orig_amt;
	}
	public BigDecimal getR63_fore_amt() {
		return r63_fore_amt;
	}
	public void setR63_fore_amt(BigDecimal r63_fore_amt) {
		this.r63_fore_amt = r63_fore_amt;
	}
	public BigDecimal getR63_no_of_acc() {
		return r63_no_of_acc;
	}
	public void setR63_no_of_acc(BigDecimal r63_no_of_acc) {
		this.r63_no_of_acc = r63_no_of_acc;
	}
	public String getR64_sche_fore_ass() {
		return r64_sche_fore_ass;
	}
	public void setR64_sche_fore_ass(String r64_sche_fore_ass) {
		this.r64_sche_fore_ass = r64_sche_fore_ass;
	}
	public BigDecimal getR64_orig_amt() {
		return r64_orig_amt;
	}
	public void setR64_orig_amt(BigDecimal r64_orig_amt) {
		this.r64_orig_amt = r64_orig_amt;
	}
	public BigDecimal getR64_fore_amt() {
		return r64_fore_amt;
	}
	public void setR64_fore_amt(BigDecimal r64_fore_amt) {
		this.r64_fore_amt = r64_fore_amt;
	}
	public BigDecimal getR64_no_of_acc() {
		return r64_no_of_acc;
	}
	public void setR64_no_of_acc(BigDecimal r64_no_of_acc) {
		this.r64_no_of_acc = r64_no_of_acc;
	}
	public String getR65_sche_fore_ass() {
		return r65_sche_fore_ass;
	}
	public void setR65_sche_fore_ass(String r65_sche_fore_ass) {
		this.r65_sche_fore_ass = r65_sche_fore_ass;
	}
	public BigDecimal getR65_orig_amt() {
		return r65_orig_amt;
	}
	public void setR65_orig_amt(BigDecimal r65_orig_amt) {
		this.r65_orig_amt = r65_orig_amt;
	}
	public BigDecimal getR65_fore_amt() {
		return r65_fore_amt;
	}
	public void setR65_fore_amt(BigDecimal r65_fore_amt) {
		this.r65_fore_amt = r65_fore_amt;
	}
	public BigDecimal getR65_no_of_acc() {
		return r65_no_of_acc;
	}
	public void setR65_no_of_acc(BigDecimal r65_no_of_acc) {
		this.r65_no_of_acc = r65_no_of_acc;
	}
	public String getR66_sche_fore_ass() {
		return r66_sche_fore_ass;
	}
	public void setR66_sche_fore_ass(String r66_sche_fore_ass) {
		this.r66_sche_fore_ass = r66_sche_fore_ass;
	}
	public BigDecimal getR66_orig_amt() {
		return r66_orig_amt;
	}
	public void setR66_orig_amt(BigDecimal r66_orig_amt) {
		this.r66_orig_amt = r66_orig_amt;
	}
	public BigDecimal getR66_fore_amt() {
		return r66_fore_amt;
	}
	public void setR66_fore_amt(BigDecimal r66_fore_amt) {
		this.r66_fore_amt = r66_fore_amt;
	}
	public BigDecimal getR66_no_of_acc() {
		return r66_no_of_acc;
	}
	public void setR66_no_of_acc(BigDecimal r66_no_of_acc) {
		this.r66_no_of_acc = r66_no_of_acc;
	}
	public String getR67_sche_fore_ass() {
		return r67_sche_fore_ass;
	}
	public void setR67_sche_fore_ass(String r67_sche_fore_ass) {
		this.r67_sche_fore_ass = r67_sche_fore_ass;
	}
	public BigDecimal getR67_orig_amt() {
		return r67_orig_amt;
	}
	public void setR67_orig_amt(BigDecimal r67_orig_amt) {
		this.r67_orig_amt = r67_orig_amt;
	}
	public BigDecimal getR67_fore_amt() {
		return r67_fore_amt;
	}
	public void setR67_fore_amt(BigDecimal r67_fore_amt) {
		this.r67_fore_amt = r67_fore_amt;
	}
	public BigDecimal getR67_no_of_acc() {
		return r67_no_of_acc;
	}
	public void setR67_no_of_acc(BigDecimal r67_no_of_acc) {
		this.r67_no_of_acc = r67_no_of_acc;
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


public class Q_RLFA2_Archival_Summary_RowMapper
        implements RowMapper<Q_RLFA2_Archival_Summary_Entity> {

    @Override
    public Q_RLFA2_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        Q_RLFA2_Archival_Summary_Entity obj = new Q_RLFA2_Archival_Summary_Entity();

// =========================
// R10
// =========================
obj.setR10_sche_fore_ass(rs.getString("r10_sche_fore_ass"));
obj.setR10_orig_amt(rs.getBigDecimal("r10_orig_amt"));
obj.setR10_fore_amt(rs.getBigDecimal("r10_fore_amt"));
obj.setR10_no_of_acc(rs.getBigDecimal("r10_no_of_acc"));


// =========================
// R11
// =========================
obj.setR11_sche_fore_ass(rs.getString("r11_sche_fore_ass"));
obj.setR11_orig_amt(rs.getBigDecimal("r11_orig_amt"));
obj.setR11_fore_amt(rs.getBigDecimal("r11_fore_amt"));
obj.setR11_no_of_acc(rs.getBigDecimal("r11_no_of_acc"));

// =========================
// R12
// =========================
obj.setR12_sche_fore_ass(rs.getString("r12_sche_fore_ass"));
obj.setR12_orig_amt(rs.getBigDecimal("r12_orig_amt"));
obj.setR12_fore_amt(rs.getBigDecimal("r12_fore_amt"));
obj.setR12_no_of_acc(rs.getBigDecimal("r12_no_of_acc"));

// =========================
// R13
// =========================
obj.setR13_sche_fore_ass(rs.getString("r13_sche_fore_ass"));
obj.setR13_orig_amt(rs.getBigDecimal("r13_orig_amt"));
obj.setR13_fore_amt(rs.getBigDecimal("r13_fore_amt"));
obj.setR13_no_of_acc(rs.getBigDecimal("r13_no_of_acc"));

// =========================
// R14
// =========================
obj.setR14_sche_fore_ass(rs.getString("r14_sche_fore_ass"));
obj.setR14_orig_amt(rs.getBigDecimal("r14_orig_amt"));
obj.setR14_fore_amt(rs.getBigDecimal("r14_fore_amt"));
obj.setR14_no_of_acc(rs.getBigDecimal("r14_no_of_acc"));

// =========================
// R15
// =========================
obj.setR15_sche_fore_ass(rs.getString("r15_sche_fore_ass"));
obj.setR15_orig_amt(rs.getBigDecimal("r15_orig_amt"));
obj.setR15_fore_amt(rs.getBigDecimal("r15_fore_amt"));
obj.setR15_no_of_acc(rs.getBigDecimal("r15_no_of_acc"));

// =========================
// R16
// =========================
obj.setR16_sche_fore_ass(rs.getString("r16_sche_fore_ass"));
obj.setR16_orig_amt(rs.getBigDecimal("r16_orig_amt"));
obj.setR16_fore_amt(rs.getBigDecimal("r16_fore_amt"));
obj.setR16_no_of_acc(rs.getBigDecimal("r16_no_of_acc"));

// =========================
// R17
// =========================
obj.setR17_sche_fore_ass(rs.getString("r17_sche_fore_ass"));
obj.setR17_orig_amt(rs.getBigDecimal("r17_orig_amt"));
obj.setR17_fore_amt(rs.getBigDecimal("r17_fore_amt"));
obj.setR17_no_of_acc(rs.getBigDecimal("r17_no_of_acc"));

// =========================
// R18
// =========================
obj.setR18_sche_fore_ass(rs.getString("r18_sche_fore_ass"));
obj.setR18_orig_amt(rs.getBigDecimal("r18_orig_amt"));
obj.setR18_fore_amt(rs.getBigDecimal("r18_fore_amt"));
obj.setR18_no_of_acc(rs.getBigDecimal("r18_no_of_acc"));

// =========================
// R19
// =========================
obj.setR19_sche_fore_ass(rs.getString("r19_sche_fore_ass"));
obj.setR19_orig_amt(rs.getBigDecimal("r19_orig_amt"));
obj.setR19_fore_amt(rs.getBigDecimal("r19_fore_amt"));
obj.setR19_no_of_acc(rs.getBigDecimal("r19_no_of_acc"));

// =========================
// R20
// =========================
obj.setR20_sche_fore_ass(rs.getString("r20_sche_fore_ass"));
obj.setR20_orig_amt(rs.getBigDecimal("r20_orig_amt"));
obj.setR20_fore_amt(rs.getBigDecimal("r20_fore_amt"));
obj.setR20_no_of_acc(rs.getBigDecimal("r20_no_of_acc"));

// =========================
// R21
// =========================
obj.setR21_sche_fore_ass(rs.getString("r21_sche_fore_ass"));
obj.setR21_orig_amt(rs.getBigDecimal("r21_orig_amt"));
obj.setR21_fore_amt(rs.getBigDecimal("r21_fore_amt"));
obj.setR21_no_of_acc(rs.getBigDecimal("r21_no_of_acc"));

// =========================
// R22
// =========================
obj.setR22_sche_fore_ass(rs.getString("r22_sche_fore_ass"));
obj.setR22_orig_amt(rs.getBigDecimal("r22_orig_amt"));
obj.setR22_fore_amt(rs.getBigDecimal("r22_fore_amt"));
obj.setR22_no_of_acc(rs.getBigDecimal("r22_no_of_acc"));

// =========================
// R23
// =========================
obj.setR23_sche_fore_ass(rs.getString("r23_sche_fore_ass"));
obj.setR23_orig_amt(rs.getBigDecimal("r23_orig_amt"));
obj.setR23_fore_amt(rs.getBigDecimal("r23_fore_amt"));
obj.setR23_no_of_acc(rs.getBigDecimal("r23_no_of_acc"));

// =========================
// R24
// =========================
obj.setR24_sche_fore_ass(rs.getString("r24_sche_fore_ass"));
obj.setR24_orig_amt(rs.getBigDecimal("r24_orig_amt"));
obj.setR24_fore_amt(rs.getBigDecimal("r24_fore_amt"));
obj.setR24_no_of_acc(rs.getBigDecimal("r24_no_of_acc"));

// =========================
// R25
// =========================
obj.setR25_sche_fore_ass(rs.getString("r25_sche_fore_ass"));
obj.setR25_orig_amt(rs.getBigDecimal("r25_orig_amt"));
obj.setR25_fore_amt(rs.getBigDecimal("r25_fore_amt"));
obj.setR25_no_of_acc(rs.getBigDecimal("r25_no_of_acc"));

// =========================
// R26
// =========================
obj.setR26_sche_fore_ass(rs.getString("r26_sche_fore_ass"));
obj.setR26_orig_amt(rs.getBigDecimal("r26_orig_amt"));
obj.setR26_fore_amt(rs.getBigDecimal("r26_fore_amt"));
obj.setR26_no_of_acc(rs.getBigDecimal("r26_no_of_acc"));

// =========================
// R27
// =========================
obj.setR27_sche_fore_ass(rs.getString("r27_sche_fore_ass"));
obj.setR27_orig_amt(rs.getBigDecimal("r27_orig_amt"));
obj.setR27_fore_amt(rs.getBigDecimal("r27_fore_amt"));
obj.setR27_no_of_acc(rs.getBigDecimal("r27_no_of_acc"));

// =========================
// R28
// =========================
obj.setR28_sche_fore_ass(rs.getString("r28_sche_fore_ass"));
obj.setR28_orig_amt(rs.getBigDecimal("r28_orig_amt"));
obj.setR28_fore_amt(rs.getBigDecimal("r28_fore_amt"));
obj.setR28_no_of_acc(rs.getBigDecimal("r28_no_of_acc"));

// =========================
// R29
// =========================
obj.setR29_sche_fore_ass(rs.getString("r29_sche_fore_ass"));
obj.setR29_orig_amt(rs.getBigDecimal("r29_orig_amt"));
obj.setR29_fore_amt(rs.getBigDecimal("r29_fore_amt"));
obj.setR29_no_of_acc(rs.getBigDecimal("r29_no_of_acc"));

// =========================
// R30
// =========================
obj.setR30_sche_fore_ass(rs.getString("r30_sche_fore_ass"));
obj.setR30_orig_amt(rs.getBigDecimal("r30_orig_amt"));
obj.setR30_fore_amt(rs.getBigDecimal("r30_fore_amt"));
obj.setR30_no_of_acc(rs.getBigDecimal("r30_no_of_acc"));


// =========================
// R31
// =========================
obj.setR31_sche_fore_ass(rs.getString("r31_sche_fore_ass"));
obj.setR31_orig_amt(rs.getBigDecimal("r31_orig_amt"));
obj.setR31_fore_amt(rs.getBigDecimal("r31_fore_amt"));
obj.setR31_no_of_acc(rs.getBigDecimal("r31_no_of_acc"));

// =========================
// R32
// =========================
obj.setR32_sche_fore_ass(rs.getString("r32_sche_fore_ass"));
obj.setR32_orig_amt(rs.getBigDecimal("r32_orig_amt"));
obj.setR32_fore_amt(rs.getBigDecimal("r32_fore_amt"));
obj.setR32_no_of_acc(rs.getBigDecimal("r32_no_of_acc"));

// =========================
// R33
// =========================
obj.setR33_sche_fore_ass(rs.getString("r33_sche_fore_ass"));
obj.setR33_orig_amt(rs.getBigDecimal("r33_orig_amt"));
obj.setR33_fore_amt(rs.getBigDecimal("r33_fore_amt"));
obj.setR33_no_of_acc(rs.getBigDecimal("r33_no_of_acc"));

// =========================
// R34
// =========================
obj.setR34_sche_fore_ass(rs.getString("r34_sche_fore_ass"));
obj.setR34_orig_amt(rs.getBigDecimal("r34_orig_amt"));
obj.setR34_fore_amt(rs.getBigDecimal("r34_fore_amt"));
obj.setR34_no_of_acc(rs.getBigDecimal("r34_no_of_acc"));

// =========================
// R35
// =========================
obj.setR35_sche_fore_ass(rs.getString("r35_sche_fore_ass"));
obj.setR35_orig_amt(rs.getBigDecimal("r35_orig_amt"));
obj.setR35_fore_amt(rs.getBigDecimal("r35_fore_amt"));
obj.setR35_no_of_acc(rs.getBigDecimal("r35_no_of_acc"));

// =========================
// R36
// =========================
obj.setR36_sche_fore_ass(rs.getString("r36_sche_fore_ass"));
obj.setR36_orig_amt(rs.getBigDecimal("r36_orig_amt"));
obj.setR36_fore_amt(rs.getBigDecimal("r36_fore_amt"));
obj.setR36_no_of_acc(rs.getBigDecimal("r36_no_of_acc"));

// =========================
// R37
// =========================
obj.setR37_sche_fore_ass(rs.getString("r37_sche_fore_ass"));
obj.setR37_orig_amt(rs.getBigDecimal("r37_orig_amt"));
obj.setR37_fore_amt(rs.getBigDecimal("r37_fore_amt"));
obj.setR37_no_of_acc(rs.getBigDecimal("r37_no_of_acc"));

// =========================
// R38
// =========================
obj.setR38_sche_fore_ass(rs.getString("r38_sche_fore_ass"));
obj.setR38_orig_amt(rs.getBigDecimal("r38_orig_amt"));
obj.setR38_fore_amt(rs.getBigDecimal("r38_fore_amt"));
obj.setR38_no_of_acc(rs.getBigDecimal("r38_no_of_acc"));

// =========================
// R39
// =========================
obj.setR39_sche_fore_ass(rs.getString("r39_sche_fore_ass"));
obj.setR39_orig_amt(rs.getBigDecimal("r39_orig_amt"));
obj.setR39_fore_amt(rs.getBigDecimal("r39_fore_amt"));
obj.setR39_no_of_acc(rs.getBigDecimal("r39_no_of_acc"));

// =========================
// R40
// =========================
obj.setR40_sche_fore_ass(rs.getString("r40_sche_fore_ass"));
obj.setR40_orig_amt(rs.getBigDecimal("r40_orig_amt"));
obj.setR40_fore_amt(rs.getBigDecimal("r40_fore_amt"));
obj.setR40_no_of_acc(rs.getBigDecimal("r40_no_of_acc"));

// =========================
// R41
// =========================
obj.setR41_sche_fore_ass(rs.getString("r41_sche_fore_ass"));
obj.setR41_orig_amt(rs.getBigDecimal("r41_orig_amt"));
obj.setR41_fore_amt(rs.getBigDecimal("r41_fore_amt"));
obj.setR41_no_of_acc(rs.getBigDecimal("r41_no_of_acc"));

// =========================
// R42
// =========================
obj.setR42_sche_fore_ass(rs.getString("r42_sche_fore_ass"));
obj.setR42_orig_amt(rs.getBigDecimal("r42_orig_amt"));
obj.setR42_fore_amt(rs.getBigDecimal("r42_fore_amt"));
obj.setR42_no_of_acc(rs.getBigDecimal("r42_no_of_acc"));

// =========================
// R43
// =========================
obj.setR43_sche_fore_ass(rs.getString("r43_sche_fore_ass"));
obj.setR43_orig_amt(rs.getBigDecimal("r43_orig_amt"));
obj.setR43_fore_amt(rs.getBigDecimal("r43_fore_amt"));
obj.setR43_no_of_acc(rs.getBigDecimal("r43_no_of_acc"));

// =========================
// R44
// =========================
obj.setR44_sche_fore_ass(rs.getString("r44_sche_fore_ass"));
obj.setR44_orig_amt(rs.getBigDecimal("r44_orig_amt"));
obj.setR44_fore_amt(rs.getBigDecimal("r44_fore_amt"));
obj.setR44_no_of_acc(rs.getBigDecimal("r44_no_of_acc"));

// =========================
// R45
// =========================
obj.setR45_sche_fore_ass(rs.getString("r45_sche_fore_ass"));
obj.setR45_orig_amt(rs.getBigDecimal("r45_orig_amt"));
obj.setR45_fore_amt(rs.getBigDecimal("r45_fore_amt"));
obj.setR45_no_of_acc(rs.getBigDecimal("r45_no_of_acc"));

// =========================
// R46
// =========================
obj.setR46_sche_fore_ass(rs.getString("r46_sche_fore_ass"));
obj.setR46_orig_amt(rs.getBigDecimal("r46_orig_amt"));
obj.setR46_fore_amt(rs.getBigDecimal("r46_fore_amt"));
obj.setR46_no_of_acc(rs.getBigDecimal("r46_no_of_acc"));

// =========================
// R47
// =========================
obj.setR47_sche_fore_ass(rs.getString("r47_sche_fore_ass"));
obj.setR47_orig_amt(rs.getBigDecimal("r47_orig_amt"));
obj.setR47_fore_amt(rs.getBigDecimal("r47_fore_amt"));
obj.setR47_no_of_acc(rs.getBigDecimal("r47_no_of_acc"));

// =========================
// R48
// =========================
obj.setR48_sche_fore_ass(rs.getString("r48_sche_fore_ass"));
obj.setR48_orig_amt(rs.getBigDecimal("r48_orig_amt"));
obj.setR48_fore_amt(rs.getBigDecimal("r48_fore_amt"));
obj.setR48_no_of_acc(rs.getBigDecimal("r48_no_of_acc"));

// =========================
// R49
// =========================
obj.setR49_sche_fore_ass(rs.getString("r49_sche_fore_ass"));
obj.setR49_orig_amt(rs.getBigDecimal("r49_orig_amt"));
obj.setR49_fore_amt(rs.getBigDecimal("r49_fore_amt"));
obj.setR49_no_of_acc(rs.getBigDecimal("r49_no_of_acc"));

// =========================
// R50
// =========================
obj.setR50_sche_fore_ass(rs.getString("r50_sche_fore_ass"));
obj.setR50_orig_amt(rs.getBigDecimal("r50_orig_amt"));
obj.setR50_fore_amt(rs.getBigDecimal("r50_fore_amt"));
obj.setR50_no_of_acc(rs.getBigDecimal("r50_no_of_acc"));

// =========================
// R51
// =========================
obj.setR51_sche_fore_ass(rs.getString("r51_sche_fore_ass"));
obj.setR51_orig_amt(rs.getBigDecimal("r51_orig_amt"));
obj.setR51_fore_amt(rs.getBigDecimal("r51_fore_amt"));
obj.setR51_no_of_acc(rs.getBigDecimal("r51_no_of_acc"));

// =========================
// R52
// =========================
obj.setR52_sche_fore_ass(rs.getString("r52_sche_fore_ass"));
obj.setR52_orig_amt(rs.getBigDecimal("r52_orig_amt"));
obj.setR52_fore_amt(rs.getBigDecimal("r52_fore_amt"));
obj.setR52_no_of_acc(rs.getBigDecimal("r52_no_of_acc"));

// =========================
// R53
// =========================
obj.setR53_sche_fore_ass(rs.getString("r53_sche_fore_ass"));
obj.setR53_orig_amt(rs.getBigDecimal("r53_orig_amt"));
obj.setR53_fore_amt(rs.getBigDecimal("r53_fore_amt"));
obj.setR53_no_of_acc(rs.getBigDecimal("r53_no_of_acc"));

// =========================
// R54
// =========================
obj.setR54_sche_fore_ass(rs.getString("r54_sche_fore_ass"));
obj.setR54_orig_amt(rs.getBigDecimal("r54_orig_amt"));
obj.setR54_fore_amt(rs.getBigDecimal("r54_fore_amt"));
obj.setR54_no_of_acc(rs.getBigDecimal("r54_no_of_acc"));

// =========================
// R55
// =========================
obj.setR55_sche_fore_ass(rs.getString("r55_sche_fore_ass"));
obj.setR55_orig_amt(rs.getBigDecimal("r55_orig_amt"));
obj.setR55_fore_amt(rs.getBigDecimal("r55_fore_amt"));
obj.setR55_no_of_acc(rs.getBigDecimal("r55_no_of_acc"));

// =========================
// R56
// =========================
obj.setR56_sche_fore_ass(rs.getString("r56_sche_fore_ass"));
obj.setR56_orig_amt(rs.getBigDecimal("r56_orig_amt"));
obj.setR56_fore_amt(rs.getBigDecimal("r56_fore_amt"));
obj.setR56_no_of_acc(rs.getBigDecimal("r56_no_of_acc"));

// =========================
// R57
// =========================
obj.setR57_sche_fore_ass(rs.getString("r57_sche_fore_ass"));
obj.setR57_orig_amt(rs.getBigDecimal("r57_orig_amt"));
obj.setR57_fore_amt(rs.getBigDecimal("r57_fore_amt"));
obj.setR57_no_of_acc(rs.getBigDecimal("r57_no_of_acc"));

// =========================
// R58
// =========================
obj.setR58_sche_fore_ass(rs.getString("r58_sche_fore_ass"));
obj.setR58_orig_amt(rs.getBigDecimal("r58_orig_amt"));
obj.setR58_fore_amt(rs.getBigDecimal("r58_fore_amt"));
obj.setR58_no_of_acc(rs.getBigDecimal("r58_no_of_acc"));

// =========================
// R59
// =========================
obj.setR59_sche_fore_ass(rs.getString("r59_sche_fore_ass"));
obj.setR59_orig_amt(rs.getBigDecimal("r59_orig_amt"));
obj.setR59_fore_amt(rs.getBigDecimal("r59_fore_amt"));
obj.setR59_no_of_acc(rs.getBigDecimal("r59_no_of_acc"));

// =========================
// R60
// =========================
obj.setR60_sche_fore_ass(rs.getString("r60_sche_fore_ass"));
obj.setR60_orig_amt(rs.getBigDecimal("r60_orig_amt"));
obj.setR60_fore_amt(rs.getBigDecimal("r60_fore_amt"));
obj.setR60_no_of_acc(rs.getBigDecimal("r60_no_of_acc"));

// =========================
// R61
// =========================
obj.setR61_sche_fore_ass(rs.getString("r61_sche_fore_ass"));
obj.setR61_orig_amt(rs.getBigDecimal("r61_orig_amt"));
obj.setR61_fore_amt(rs.getBigDecimal("r61_fore_amt"));
obj.setR61_no_of_acc(rs.getBigDecimal("r61_no_of_acc"));

// =========================
// R62
// =========================
obj.setR62_sche_fore_ass(rs.getString("r62_sche_fore_ass"));
obj.setR62_orig_amt(rs.getBigDecimal("r62_orig_amt"));
obj.setR62_fore_amt(rs.getBigDecimal("r62_fore_amt"));
obj.setR62_no_of_acc(rs.getBigDecimal("r62_no_of_acc"));

// =========================
// R63
// =========================
obj.setR63_sche_fore_ass(rs.getString("r63_sche_fore_ass"));
obj.setR63_orig_amt(rs.getBigDecimal("r63_orig_amt"));
obj.setR63_fore_amt(rs.getBigDecimal("r63_fore_amt"));
obj.setR63_no_of_acc(rs.getBigDecimal("r63_no_of_acc"));

// =========================
// R64
// =========================
obj.setR64_sche_fore_ass(rs.getString("r64_sche_fore_ass"));
obj.setR64_orig_amt(rs.getBigDecimal("r64_orig_amt"));
obj.setR64_fore_amt(rs.getBigDecimal("r64_fore_amt"));
obj.setR64_no_of_acc(rs.getBigDecimal("r64_no_of_acc"));

// =========================
// R65
// =========================
obj.setR65_sche_fore_ass(rs.getString("r65_sche_fore_ass"));
obj.setR65_orig_amt(rs.getBigDecimal("r65_orig_amt"));
obj.setR65_fore_amt(rs.getBigDecimal("r65_fore_amt"));
obj.setR65_no_of_acc(rs.getBigDecimal("r65_no_of_acc"));

// =========================
// R66
// =========================
obj.setR66_sche_fore_ass(rs.getString("r66_sche_fore_ass"));
obj.setR66_orig_amt(rs.getBigDecimal("r66_orig_amt"));
obj.setR66_fore_amt(rs.getBigDecimal("r66_fore_amt"));
obj.setR66_no_of_acc(rs.getBigDecimal("r66_no_of_acc"));

// =========================
// R67
// =========================
obj.setR67_sche_fore_ass(rs.getString("r67_sche_fore_ass"));
obj.setR67_orig_amt(rs.getBigDecimal("r67_orig_amt"));
obj.setR67_fore_amt(rs.getBigDecimal("r67_fore_amt"));
obj.setR67_no_of_acc(rs.getBigDecimal("r67_no_of_acc"));

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


public class Q_RLFA2_Archival_Summary_Entity {
	
	
	private String	r10_sche_fore_ass;
	private BigDecimal	r10_orig_amt;
	private BigDecimal	r10_fore_amt;
	private BigDecimal	r10_no_of_acc;
	
	private String	r11_sche_fore_ass;
	private BigDecimal	r11_orig_amt;
	private BigDecimal	r11_fore_amt;
	private BigDecimal	r11_no_of_acc;
	
	private String	r12_sche_fore_ass;
	private BigDecimal	r12_orig_amt;
	private BigDecimal	r12_fore_amt;
	private BigDecimal	r12_no_of_acc;
	
	private String	r13_sche_fore_ass;
	private BigDecimal	r13_orig_amt;
	private BigDecimal	r13_fore_amt;
	private BigDecimal	r13_no_of_acc;
	
	private String	r14_sche_fore_ass;
	private BigDecimal	r14_orig_amt;
	private BigDecimal	r14_fore_amt;
	private BigDecimal	r14_no_of_acc;
	
	private String	r15_sche_fore_ass;
	private BigDecimal	r15_orig_amt;
	private BigDecimal	r15_fore_amt;
	private BigDecimal	r15_no_of_acc;
	
	private String	r16_sche_fore_ass;
	private BigDecimal	r16_orig_amt;
	private BigDecimal	r16_fore_amt;
	private BigDecimal	r16_no_of_acc;
	
	private String	r17_sche_fore_ass;
	private BigDecimal	r17_orig_amt;
	private BigDecimal	r17_fore_amt;
	private BigDecimal	r17_no_of_acc;
	
	private String	r18_sche_fore_ass;
	private BigDecimal	r18_orig_amt;
	private BigDecimal	r18_fore_amt;
	private BigDecimal	r18_no_of_acc;
	
	private String	r19_sche_fore_ass;
	private BigDecimal	r19_orig_amt;
	private BigDecimal	r19_fore_amt;
	private BigDecimal	r19_no_of_acc;
	
	private String	r20_sche_fore_ass;
	private BigDecimal	r20_orig_amt;
	private BigDecimal	r20_fore_amt;
	private BigDecimal	r20_no_of_acc;
	
	private String	r21_sche_fore_ass;
	private BigDecimal	r21_orig_amt;
	private BigDecimal	r21_fore_amt;
	private BigDecimal	r21_no_of_acc;
	
	private String	r22_sche_fore_ass;
	private BigDecimal	r22_orig_amt;
	private BigDecimal	r22_fore_amt;
	private BigDecimal	r22_no_of_acc;
	
	private String	r23_sche_fore_ass;
	private BigDecimal	r23_orig_amt;
	private BigDecimal	r23_fore_amt;
	private BigDecimal	r23_no_of_acc;
	
	private String	r24_sche_fore_ass;
	private BigDecimal	r24_orig_amt;
	private BigDecimal	r24_fore_amt;
	private BigDecimal	r24_no_of_acc;
	
	private String	r25_sche_fore_ass;
	private BigDecimal	r25_orig_amt;
	private BigDecimal	r25_fore_amt;
	private BigDecimal	r25_no_of_acc;
	
	private String	r26_sche_fore_ass;
	private BigDecimal	r26_orig_amt;
	private BigDecimal	r26_fore_amt;
	private BigDecimal	r26_no_of_acc;
	
	private String	r27_sche_fore_ass;
	private BigDecimal	r27_orig_amt;
	private BigDecimal	r27_fore_amt;
	private BigDecimal	r27_no_of_acc;
	
	private String	r28_sche_fore_ass;
	private BigDecimal	r28_orig_amt;
	private BigDecimal	r28_fore_amt;
	private BigDecimal	r28_no_of_acc;
	
	private String	r29_sche_fore_ass;
	private BigDecimal	r29_orig_amt;
	private BigDecimal	r29_fore_amt;
	private BigDecimal	r29_no_of_acc;
	
	private String	r30_sche_fore_ass;
	private BigDecimal	r30_orig_amt;
	private BigDecimal	r30_fore_amt;
	private BigDecimal	r30_no_of_acc;
	
	private String	r31_sche_fore_ass;
	private BigDecimal	r31_orig_amt;
	private BigDecimal	r31_fore_amt;
	private BigDecimal	r31_no_of_acc;
	
	private String	r32_sche_fore_ass;
	private BigDecimal	r32_orig_amt;
	private BigDecimal	r32_fore_amt;
	private BigDecimal	r32_no_of_acc;
	
	private String	r33_sche_fore_ass;
	private BigDecimal	r33_orig_amt;
	private BigDecimal	r33_fore_amt;
	private BigDecimal	r33_no_of_acc;
	
	private String	r34_sche_fore_ass;
	private BigDecimal	r34_orig_amt;
	private BigDecimal	r34_fore_amt;
	private BigDecimal	r34_no_of_acc;
	
	private String	r35_sche_fore_ass;
	private BigDecimal	r35_orig_amt;
	private BigDecimal	r35_fore_amt;
	private BigDecimal	r35_no_of_acc;
	
	private String	r36_sche_fore_ass;
	private BigDecimal	r36_orig_amt;
	private BigDecimal	r36_fore_amt;
	private BigDecimal	r36_no_of_acc;
	
	private String	r37_sche_fore_ass;
	private BigDecimal	r37_orig_amt;
	private BigDecimal	r37_fore_amt;
	private BigDecimal	r37_no_of_acc;
	
	private String	r38_sche_fore_ass;
	private BigDecimal	r38_orig_amt;
	private BigDecimal	r38_fore_amt;
	private BigDecimal	r38_no_of_acc;
	
	private String	r39_sche_fore_ass;
	private BigDecimal	r39_orig_amt;
	private BigDecimal	r39_fore_amt;
	private BigDecimal	r39_no_of_acc;
	
	private String	r40_sche_fore_ass;
	private BigDecimal	r40_orig_amt;
	private BigDecimal	r40_fore_amt;
	private BigDecimal	r40_no_of_acc;
	
	private String	r41_sche_fore_ass;
	private BigDecimal	r41_orig_amt;
	private BigDecimal	r41_fore_amt;
	private BigDecimal	r41_no_of_acc;
	
	private String	r42_sche_fore_ass;
	private BigDecimal	r42_orig_amt;
	private BigDecimal	r42_fore_amt;
	private BigDecimal	r42_no_of_acc;
	
	private String	r43_sche_fore_ass;
	private BigDecimal	r43_orig_amt;
	private BigDecimal	r43_fore_amt;
	private BigDecimal	r43_no_of_acc;
	
	private String	r44_sche_fore_ass;
	private BigDecimal	r44_orig_amt;
	private BigDecimal	r44_fore_amt;
	private BigDecimal	r44_no_of_acc;
	
	private String	r45_sche_fore_ass;
	private BigDecimal	r45_orig_amt;
	private BigDecimal	r45_fore_amt;
	private BigDecimal	r45_no_of_acc;
	
	private String	r46_sche_fore_ass;
	private BigDecimal	r46_orig_amt;
	private BigDecimal	r46_fore_amt;
	private BigDecimal	r46_no_of_acc;
	
	private String	r47_sche_fore_ass;
	private BigDecimal	r47_orig_amt;
	private BigDecimal	r47_fore_amt;
	private BigDecimal	r47_no_of_acc;
	
	private String	r48_sche_fore_ass;
	private BigDecimal	r48_orig_amt;
	private BigDecimal	r48_fore_amt;
	private BigDecimal	r48_no_of_acc;
	
	private String	r49_sche_fore_ass;
	private BigDecimal	r49_orig_amt;
	private BigDecimal	r49_fore_amt;
	private BigDecimal	r49_no_of_acc;
	
	private String	r50_sche_fore_ass;
	private BigDecimal	r50_orig_amt;
	private BigDecimal	r50_fore_amt;
	private BigDecimal	r50_no_of_acc;
	
	private String	r51_sche_fore_ass;
	private BigDecimal	r51_orig_amt;
	private BigDecimal	r51_fore_amt;
	private BigDecimal	r51_no_of_acc;
	
	private String	r52_sche_fore_ass;
	private BigDecimal	r52_orig_amt;
	private BigDecimal	r52_fore_amt;
	private BigDecimal	r52_no_of_acc;
	
	private String	r53_sche_fore_ass;
	private BigDecimal	r53_orig_amt;
	private BigDecimal	r53_fore_amt;
	private BigDecimal	r53_no_of_acc;
	
	private String	r54_sche_fore_ass;
	private BigDecimal	r54_orig_amt;
	private BigDecimal	r54_fore_amt;
	private BigDecimal	r54_no_of_acc;
	
	private String	r55_sche_fore_ass;
	private BigDecimal	r55_orig_amt;
	private BigDecimal	r55_fore_amt;
	private BigDecimal	r55_no_of_acc;
	
	private String	r56_sche_fore_ass;
	private BigDecimal	r56_orig_amt;
	private BigDecimal	r56_fore_amt;
	private BigDecimal	r56_no_of_acc;
	
	private String	r57_sche_fore_ass;
	private BigDecimal	r57_orig_amt;
	private BigDecimal	r57_fore_amt;
	private BigDecimal	r57_no_of_acc;
	
	private String	r58_sche_fore_ass;
	private BigDecimal	r58_orig_amt;
	private BigDecimal	r58_fore_amt;
	private BigDecimal	r58_no_of_acc;
	
	private String	r59_sche_fore_ass;
	private BigDecimal	r59_orig_amt;
	private BigDecimal	r59_fore_amt;
	private BigDecimal	r59_no_of_acc;
	
	private String	r60_sche_fore_ass;
	private BigDecimal	r60_orig_amt;
	private BigDecimal	r60_fore_amt;
	private BigDecimal	r60_no_of_acc;
	
	private String	r61_sche_fore_ass;
	private BigDecimal	r61_orig_amt;
	private BigDecimal	r61_fore_amt;
	private BigDecimal	r61_no_of_acc;
	
	private String	r62_sche_fore_ass;
	private BigDecimal	r62_orig_amt;
	private BigDecimal	r62_fore_amt;
	private BigDecimal	r62_no_of_acc;
	
	private String	r63_sche_fore_ass;
	private BigDecimal	r63_orig_amt;
	private BigDecimal	r63_fore_amt;
	private BigDecimal	r63_no_of_acc;
	
	// R64
	private String     r64_sche_fore_ass;
	private BigDecimal r64_orig_amt;
	private BigDecimal r64_fore_amt;
	private BigDecimal r64_no_of_acc;

	// R65
	private String     r65_sche_fore_ass;
	private BigDecimal r65_orig_amt;
	private BigDecimal r65_fore_amt;
	private BigDecimal r65_no_of_acc;

	// R66
	private String     r66_sche_fore_ass;
	private BigDecimal r66_orig_amt;
	private BigDecimal r66_fore_amt;
	private BigDecimal r66_no_of_acc;

	// R67
	private String     r67_sche_fore_ass;
	private BigDecimal r67_orig_amt;
	private BigDecimal r67_fore_amt;
	private BigDecimal r67_no_of_acc;

	               
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
	
public String getR10_sche_fore_ass() {
		return r10_sche_fore_ass;
	}
	public void setR10_sche_fore_ass(String r10_sche_fore_ass) {
		this.r10_sche_fore_ass = r10_sche_fore_ass;
	}
	public BigDecimal getR10_orig_amt() {
		return r10_orig_amt;
	}
	public void setR10_orig_amt(BigDecimal r10_orig_amt) {
		this.r10_orig_amt = r10_orig_amt;
	}
	public BigDecimal getR10_fore_amt() {
		return r10_fore_amt;
	}
	public void setR10_fore_amt(BigDecimal r10_fore_amt) {
		this.r10_fore_amt = r10_fore_amt;
	}
	public BigDecimal getR10_no_of_acc() {
		return r10_no_of_acc;
	}
	public void setR10_no_of_acc(BigDecimal r10_no_of_acc) {
		this.r10_no_of_acc = r10_no_of_acc;
	}
	public String getR11_sche_fore_ass() {
		return r11_sche_fore_ass;
	}
	public void setR11_sche_fore_ass(String r11_sche_fore_ass) {
		this.r11_sche_fore_ass = r11_sche_fore_ass;
	}
	public BigDecimal getR11_orig_amt() {
		return r11_orig_amt;
	}
	public void setR11_orig_amt(BigDecimal r11_orig_amt) {
		this.r11_orig_amt = r11_orig_amt;
	}
	public BigDecimal getR11_fore_amt() {
		return r11_fore_amt;
	}
	public void setR11_fore_amt(BigDecimal r11_fore_amt) {
		this.r11_fore_amt = r11_fore_amt;
	}
	public BigDecimal getR11_no_of_acc() {
		return r11_no_of_acc;
	}
	public void setR11_no_of_acc(BigDecimal r11_no_of_acc) {
		this.r11_no_of_acc = r11_no_of_acc;
	}
	public String getR12_sche_fore_ass() {
		return r12_sche_fore_ass;
	}
	public void setR12_sche_fore_ass(String r12_sche_fore_ass) {
		this.r12_sche_fore_ass = r12_sche_fore_ass;
	}
	public BigDecimal getR12_orig_amt() {
		return r12_orig_amt;
	}
	public void setR12_orig_amt(BigDecimal r12_orig_amt) {
		this.r12_orig_amt = r12_orig_amt;
	}
	public BigDecimal getR12_fore_amt() {
		return r12_fore_amt;
	}
	public void setR12_fore_amt(BigDecimal r12_fore_amt) {
		this.r12_fore_amt = r12_fore_amt;
	}
	public BigDecimal getR12_no_of_acc() {
		return r12_no_of_acc;
	}
	public void setR12_no_of_acc(BigDecimal r12_no_of_acc) {
		this.r12_no_of_acc = r12_no_of_acc;
	}
	public String getR13_sche_fore_ass() {
		return r13_sche_fore_ass;
	}
	public void setR13_sche_fore_ass(String r13_sche_fore_ass) {
		this.r13_sche_fore_ass = r13_sche_fore_ass;
	}
	public BigDecimal getR13_orig_amt() {
		return r13_orig_amt;
	}
	public void setR13_orig_amt(BigDecimal r13_orig_amt) {
		this.r13_orig_amt = r13_orig_amt;
	}
	public BigDecimal getR13_fore_amt() {
		return r13_fore_amt;
	}
	public void setR13_fore_amt(BigDecimal r13_fore_amt) {
		this.r13_fore_amt = r13_fore_amt;
	}
	public BigDecimal getR13_no_of_acc() {
		return r13_no_of_acc;
	}
	public void setR13_no_of_acc(BigDecimal r13_no_of_acc) {
		this.r13_no_of_acc = r13_no_of_acc;
	}
	public String getR14_sche_fore_ass() {
		return r14_sche_fore_ass;
	}
	public void setR14_sche_fore_ass(String r14_sche_fore_ass) {
		this.r14_sche_fore_ass = r14_sche_fore_ass;
	}
	public BigDecimal getR14_orig_amt() {
		return r14_orig_amt;
	}
	public void setR14_orig_amt(BigDecimal r14_orig_amt) {
		this.r14_orig_amt = r14_orig_amt;
	}
	public BigDecimal getR14_fore_amt() {
		return r14_fore_amt;
	}
	public void setR14_fore_amt(BigDecimal r14_fore_amt) {
		this.r14_fore_amt = r14_fore_amt;
	}
	public BigDecimal getR14_no_of_acc() {
		return r14_no_of_acc;
	}
	public void setR14_no_of_acc(BigDecimal r14_no_of_acc) {
		this.r14_no_of_acc = r14_no_of_acc;
	}
	public String getR15_sche_fore_ass() {
		return r15_sche_fore_ass;
	}
	public void setR15_sche_fore_ass(String r15_sche_fore_ass) {
		this.r15_sche_fore_ass = r15_sche_fore_ass;
	}
	public BigDecimal getR15_orig_amt() {
		return r15_orig_amt;
	}
	public void setR15_orig_amt(BigDecimal r15_orig_amt) {
		this.r15_orig_amt = r15_orig_amt;
	}
	public BigDecimal getR15_fore_amt() {
		return r15_fore_amt;
	}
	public void setR15_fore_amt(BigDecimal r15_fore_amt) {
		this.r15_fore_amt = r15_fore_amt;
	}
	public BigDecimal getR15_no_of_acc() {
		return r15_no_of_acc;
	}
	public void setR15_no_of_acc(BigDecimal r15_no_of_acc) {
		this.r15_no_of_acc = r15_no_of_acc;
	}
	public String getR16_sche_fore_ass() {
		return r16_sche_fore_ass;
	}
	public void setR16_sche_fore_ass(String r16_sche_fore_ass) {
		this.r16_sche_fore_ass = r16_sche_fore_ass;
	}
	public BigDecimal getR16_orig_amt() {
		return r16_orig_amt;
	}
	public void setR16_orig_amt(BigDecimal r16_orig_amt) {
		this.r16_orig_amt = r16_orig_amt;
	}
	public BigDecimal getR16_fore_amt() {
		return r16_fore_amt;
	}
	public void setR16_fore_amt(BigDecimal r16_fore_amt) {
		this.r16_fore_amt = r16_fore_amt;
	}
	public BigDecimal getR16_no_of_acc() {
		return r16_no_of_acc;
	}
	public void setR16_no_of_acc(BigDecimal r16_no_of_acc) {
		this.r16_no_of_acc = r16_no_of_acc;
	}
	public String getR17_sche_fore_ass() {
		return r17_sche_fore_ass;
	}
	public void setR17_sche_fore_ass(String r17_sche_fore_ass) {
		this.r17_sche_fore_ass = r17_sche_fore_ass;
	}
	public BigDecimal getR17_orig_amt() {
		return r17_orig_amt;
	}
	public void setR17_orig_amt(BigDecimal r17_orig_amt) {
		this.r17_orig_amt = r17_orig_amt;
	}
	public BigDecimal getR17_fore_amt() {
		return r17_fore_amt;
	}
	public void setR17_fore_amt(BigDecimal r17_fore_amt) {
		this.r17_fore_amt = r17_fore_amt;
	}
	public BigDecimal getR17_no_of_acc() {
		return r17_no_of_acc;
	}
	public void setR17_no_of_acc(BigDecimal r17_no_of_acc) {
		this.r17_no_of_acc = r17_no_of_acc;
	}
	public String getR18_sche_fore_ass() {
		return r18_sche_fore_ass;
	}
	public void setR18_sche_fore_ass(String r18_sche_fore_ass) {
		this.r18_sche_fore_ass = r18_sche_fore_ass;
	}
	public BigDecimal getR18_orig_amt() {
		return r18_orig_amt;
	}
	public void setR18_orig_amt(BigDecimal r18_orig_amt) {
		this.r18_orig_amt = r18_orig_amt;
	}
	public BigDecimal getR18_fore_amt() {
		return r18_fore_amt;
	}
	public void setR18_fore_amt(BigDecimal r18_fore_amt) {
		this.r18_fore_amt = r18_fore_amt;
	}
	public BigDecimal getR18_no_of_acc() {
		return r18_no_of_acc;
	}
	public void setR18_no_of_acc(BigDecimal r18_no_of_acc) {
		this.r18_no_of_acc = r18_no_of_acc;
	}
	public String getR19_sche_fore_ass() {
		return r19_sche_fore_ass;
	}
	public void setR19_sche_fore_ass(String r19_sche_fore_ass) {
		this.r19_sche_fore_ass = r19_sche_fore_ass;
	}
	public BigDecimal getR19_orig_amt() {
		return r19_orig_amt;
	}
	public void setR19_orig_amt(BigDecimal r19_orig_amt) {
		this.r19_orig_amt = r19_orig_amt;
	}
	public BigDecimal getR19_fore_amt() {
		return r19_fore_amt;
	}
	public void setR19_fore_amt(BigDecimal r19_fore_amt) {
		this.r19_fore_amt = r19_fore_amt;
	}
	public BigDecimal getR19_no_of_acc() {
		return r19_no_of_acc;
	}
	public void setR19_no_of_acc(BigDecimal r19_no_of_acc) {
		this.r19_no_of_acc = r19_no_of_acc;
	}
	public String getR20_sche_fore_ass() {
		return r20_sche_fore_ass;
	}
	public void setR20_sche_fore_ass(String r20_sche_fore_ass) {
		this.r20_sche_fore_ass = r20_sche_fore_ass;
	}
	public BigDecimal getR20_orig_amt() {
		return r20_orig_amt;
	}
	public void setR20_orig_amt(BigDecimal r20_orig_amt) {
		this.r20_orig_amt = r20_orig_amt;
	}
	public BigDecimal getR20_fore_amt() {
		return r20_fore_amt;
	}
	public void setR20_fore_amt(BigDecimal r20_fore_amt) {
		this.r20_fore_amt = r20_fore_amt;
	}
	public BigDecimal getR20_no_of_acc() {
		return r20_no_of_acc;
	}
	public void setR20_no_of_acc(BigDecimal r20_no_of_acc) {
		this.r20_no_of_acc = r20_no_of_acc;
	}
	public String getR21_sche_fore_ass() {
		return r21_sche_fore_ass;
	}
	public void setR21_sche_fore_ass(String r21_sche_fore_ass) {
		this.r21_sche_fore_ass = r21_sche_fore_ass;
	}
	public BigDecimal getR21_orig_amt() {
		return r21_orig_amt;
	}
	public void setR21_orig_amt(BigDecimal r21_orig_amt) {
		this.r21_orig_amt = r21_orig_amt;
	}
	public BigDecimal getR21_fore_amt() {
		return r21_fore_amt;
	}
	public void setR21_fore_amt(BigDecimal r21_fore_amt) {
		this.r21_fore_amt = r21_fore_amt;
	}
	public BigDecimal getR21_no_of_acc() {
		return r21_no_of_acc;
	}
	public void setR21_no_of_acc(BigDecimal r21_no_of_acc) {
		this.r21_no_of_acc = r21_no_of_acc;
	}
	public String getR22_sche_fore_ass() {
		return r22_sche_fore_ass;
	}
	public void setR22_sche_fore_ass(String r22_sche_fore_ass) {
		this.r22_sche_fore_ass = r22_sche_fore_ass;
	}
	public BigDecimal getR22_orig_amt() {
		return r22_orig_amt;
	}
	public void setR22_orig_amt(BigDecimal r22_orig_amt) {
		this.r22_orig_amt = r22_orig_amt;
	}
	public BigDecimal getR22_fore_amt() {
		return r22_fore_amt;
	}
	public void setR22_fore_amt(BigDecimal r22_fore_amt) {
		this.r22_fore_amt = r22_fore_amt;
	}
	public BigDecimal getR22_no_of_acc() {
		return r22_no_of_acc;
	}
	public void setR22_no_of_acc(BigDecimal r22_no_of_acc) {
		this.r22_no_of_acc = r22_no_of_acc;
	}
	public String getR23_sche_fore_ass() {
		return r23_sche_fore_ass;
	}
	public void setR23_sche_fore_ass(String r23_sche_fore_ass) {
		this.r23_sche_fore_ass = r23_sche_fore_ass;
	}
	public BigDecimal getR23_orig_amt() {
		return r23_orig_amt;
	}
	public void setR23_orig_amt(BigDecimal r23_orig_amt) {
		this.r23_orig_amt = r23_orig_amt;
	}
	public BigDecimal getR23_fore_amt() {
		return r23_fore_amt;
	}
	public void setR23_fore_amt(BigDecimal r23_fore_amt) {
		this.r23_fore_amt = r23_fore_amt;
	}
	public BigDecimal getR23_no_of_acc() {
		return r23_no_of_acc;
	}
	public void setR23_no_of_acc(BigDecimal r23_no_of_acc) {
		this.r23_no_of_acc = r23_no_of_acc;
	}
	public String getR24_sche_fore_ass() {
		return r24_sche_fore_ass;
	}
	public void setR24_sche_fore_ass(String r24_sche_fore_ass) {
		this.r24_sche_fore_ass = r24_sche_fore_ass;
	}
	public BigDecimal getR24_orig_amt() {
		return r24_orig_amt;
	}
	public void setR24_orig_amt(BigDecimal r24_orig_amt) {
		this.r24_orig_amt = r24_orig_amt;
	}
	public BigDecimal getR24_fore_amt() {
		return r24_fore_amt;
	}
	public void setR24_fore_amt(BigDecimal r24_fore_amt) {
		this.r24_fore_amt = r24_fore_amt;
	}
	public BigDecimal getR24_no_of_acc() {
		return r24_no_of_acc;
	}
	public void setR24_no_of_acc(BigDecimal r24_no_of_acc) {
		this.r24_no_of_acc = r24_no_of_acc;
	}
	public String getR25_sche_fore_ass() {
		return r25_sche_fore_ass;
	}
	public void setR25_sche_fore_ass(String r25_sche_fore_ass) {
		this.r25_sche_fore_ass = r25_sche_fore_ass;
	}
	public BigDecimal getR25_orig_amt() {
		return r25_orig_amt;
	}
	public void setR25_orig_amt(BigDecimal r25_orig_amt) {
		this.r25_orig_amt = r25_orig_amt;
	}
	public BigDecimal getR25_fore_amt() {
		return r25_fore_amt;
	}
	public void setR25_fore_amt(BigDecimal r25_fore_amt) {
		this.r25_fore_amt = r25_fore_amt;
	}
	public BigDecimal getR25_no_of_acc() {
		return r25_no_of_acc;
	}
	public void setR25_no_of_acc(BigDecimal r25_no_of_acc) {
		this.r25_no_of_acc = r25_no_of_acc;
	}
	public String getR26_sche_fore_ass() {
		return r26_sche_fore_ass;
	}
	public void setR26_sche_fore_ass(String r26_sche_fore_ass) {
		this.r26_sche_fore_ass = r26_sche_fore_ass;
	}
	public BigDecimal getR26_orig_amt() {
		return r26_orig_amt;
	}
	public void setR26_orig_amt(BigDecimal r26_orig_amt) {
		this.r26_orig_amt = r26_orig_amt;
	}
	public BigDecimal getR26_fore_amt() {
		return r26_fore_amt;
	}
	public void setR26_fore_amt(BigDecimal r26_fore_amt) {
		this.r26_fore_amt = r26_fore_amt;
	}
	public BigDecimal getR26_no_of_acc() {
		return r26_no_of_acc;
	}
	public void setR26_no_of_acc(BigDecimal r26_no_of_acc) {
		this.r26_no_of_acc = r26_no_of_acc;
	}
	public String getR27_sche_fore_ass() {
		return r27_sche_fore_ass;
	}
	public void setR27_sche_fore_ass(String r27_sche_fore_ass) {
		this.r27_sche_fore_ass = r27_sche_fore_ass;
	}
	public BigDecimal getR27_orig_amt() {
		return r27_orig_amt;
	}
	public void setR27_orig_amt(BigDecimal r27_orig_amt) {
		this.r27_orig_amt = r27_orig_amt;
	}
	public BigDecimal getR27_fore_amt() {
		return r27_fore_amt;
	}
	public void setR27_fore_amt(BigDecimal r27_fore_amt) {
		this.r27_fore_amt = r27_fore_amt;
	}
	public BigDecimal getR27_no_of_acc() {
		return r27_no_of_acc;
	}
	public void setR27_no_of_acc(BigDecimal r27_no_of_acc) {
		this.r27_no_of_acc = r27_no_of_acc;
	}
	public String getR28_sche_fore_ass() {
		return r28_sche_fore_ass;
	}
	public void setR28_sche_fore_ass(String r28_sche_fore_ass) {
		this.r28_sche_fore_ass = r28_sche_fore_ass;
	}
	public BigDecimal getR28_orig_amt() {
		return r28_orig_amt;
	}
	public void setR28_orig_amt(BigDecimal r28_orig_amt) {
		this.r28_orig_amt = r28_orig_amt;
	}
	public BigDecimal getR28_fore_amt() {
		return r28_fore_amt;
	}
	public void setR28_fore_amt(BigDecimal r28_fore_amt) {
		this.r28_fore_amt = r28_fore_amt;
	}
	public BigDecimal getR28_no_of_acc() {
		return r28_no_of_acc;
	}
	public void setR28_no_of_acc(BigDecimal r28_no_of_acc) {
		this.r28_no_of_acc = r28_no_of_acc;
	}
	public String getR29_sche_fore_ass() {
		return r29_sche_fore_ass;
	}
	public void setR29_sche_fore_ass(String r29_sche_fore_ass) {
		this.r29_sche_fore_ass = r29_sche_fore_ass;
	}
	public BigDecimal getR29_orig_amt() {
		return r29_orig_amt;
	}
	public void setR29_orig_amt(BigDecimal r29_orig_amt) {
		this.r29_orig_amt = r29_orig_amt;
	}
	public BigDecimal getR29_fore_amt() {
		return r29_fore_amt;
	}
	public void setR29_fore_amt(BigDecimal r29_fore_amt) {
		this.r29_fore_amt = r29_fore_amt;
	}
	public BigDecimal getR29_no_of_acc() {
		return r29_no_of_acc;
	}
	public void setR29_no_of_acc(BigDecimal r29_no_of_acc) {
		this.r29_no_of_acc = r29_no_of_acc;
	}
	public String getR30_sche_fore_ass() {
		return r30_sche_fore_ass;
	}
	public void setR30_sche_fore_ass(String r30_sche_fore_ass) {
		this.r30_sche_fore_ass = r30_sche_fore_ass;
	}
	public BigDecimal getR30_orig_amt() {
		return r30_orig_amt;
	}
	public void setR30_orig_amt(BigDecimal r30_orig_amt) {
		this.r30_orig_amt = r30_orig_amt;
	}
	public BigDecimal getR30_fore_amt() {
		return r30_fore_amt;
	}
	public void setR30_fore_amt(BigDecimal r30_fore_amt) {
		this.r30_fore_amt = r30_fore_amt;
	}
	public BigDecimal getR30_no_of_acc() {
		return r30_no_of_acc;
	}
	public void setR30_no_of_acc(BigDecimal r30_no_of_acc) {
		this.r30_no_of_acc = r30_no_of_acc;
	}
	public String getR31_sche_fore_ass() {
		return r31_sche_fore_ass;
	}
	public void setR31_sche_fore_ass(String r31_sche_fore_ass) {
		this.r31_sche_fore_ass = r31_sche_fore_ass;
	}
	public BigDecimal getR31_orig_amt() {
		return r31_orig_amt;
	}
	public void setR31_orig_amt(BigDecimal r31_orig_amt) {
		this.r31_orig_amt = r31_orig_amt;
	}
	public BigDecimal getR31_fore_amt() {
		return r31_fore_amt;
	}
	public void setR31_fore_amt(BigDecimal r31_fore_amt) {
		this.r31_fore_amt = r31_fore_amt;
	}
	public BigDecimal getR31_no_of_acc() {
		return r31_no_of_acc;
	}
	public void setR31_no_of_acc(BigDecimal r31_no_of_acc) {
		this.r31_no_of_acc = r31_no_of_acc;
	}
	public String getR32_sche_fore_ass() {
		return r32_sche_fore_ass;
	}
	public void setR32_sche_fore_ass(String r32_sche_fore_ass) {
		this.r32_sche_fore_ass = r32_sche_fore_ass;
	}
	public BigDecimal getR32_orig_amt() {
		return r32_orig_amt;
	}
	public void setR32_orig_amt(BigDecimal r32_orig_amt) {
		this.r32_orig_amt = r32_orig_amt;
	}
	public BigDecimal getR32_fore_amt() {
		return r32_fore_amt;
	}
	public void setR32_fore_amt(BigDecimal r32_fore_amt) {
		this.r32_fore_amt = r32_fore_amt;
	}
	public BigDecimal getR32_no_of_acc() {
		return r32_no_of_acc;
	}
	public void setR32_no_of_acc(BigDecimal r32_no_of_acc) {
		this.r32_no_of_acc = r32_no_of_acc;
	}
	public String getR33_sche_fore_ass() {
		return r33_sche_fore_ass;
	}
	public void setR33_sche_fore_ass(String r33_sche_fore_ass) {
		this.r33_sche_fore_ass = r33_sche_fore_ass;
	}
	public BigDecimal getR33_orig_amt() {
		return r33_orig_amt;
	}
	public void setR33_orig_amt(BigDecimal r33_orig_amt) {
		this.r33_orig_amt = r33_orig_amt;
	}
	public BigDecimal getR33_fore_amt() {
		return r33_fore_amt;
	}
	public void setR33_fore_amt(BigDecimal r33_fore_amt) {
		this.r33_fore_amt = r33_fore_amt;
	}
	public BigDecimal getR33_no_of_acc() {
		return r33_no_of_acc;
	}
	public void setR33_no_of_acc(BigDecimal r33_no_of_acc) {
		this.r33_no_of_acc = r33_no_of_acc;
	}
	public String getR34_sche_fore_ass() {
		return r34_sche_fore_ass;
	}
	public void setR34_sche_fore_ass(String r34_sche_fore_ass) {
		this.r34_sche_fore_ass = r34_sche_fore_ass;
	}
	public BigDecimal getR34_orig_amt() {
		return r34_orig_amt;
	}
	public void setR34_orig_amt(BigDecimal r34_orig_amt) {
		this.r34_orig_amt = r34_orig_amt;
	}
	public BigDecimal getR34_fore_amt() {
		return r34_fore_amt;
	}
	public void setR34_fore_amt(BigDecimal r34_fore_amt) {
		this.r34_fore_amt = r34_fore_amt;
	}
	public BigDecimal getR34_no_of_acc() {
		return r34_no_of_acc;
	}
	public void setR34_no_of_acc(BigDecimal r34_no_of_acc) {
		this.r34_no_of_acc = r34_no_of_acc;
	}
	public String getR35_sche_fore_ass() {
		return r35_sche_fore_ass;
	}
	public void setR35_sche_fore_ass(String r35_sche_fore_ass) {
		this.r35_sche_fore_ass = r35_sche_fore_ass;
	}
	public BigDecimal getR35_orig_amt() {
		return r35_orig_amt;
	}
	public void setR35_orig_amt(BigDecimal r35_orig_amt) {
		this.r35_orig_amt = r35_orig_amt;
	}
	public BigDecimal getR35_fore_amt() {
		return r35_fore_amt;
	}
	public void setR35_fore_amt(BigDecimal r35_fore_amt) {
		this.r35_fore_amt = r35_fore_amt;
	}
	public BigDecimal getR35_no_of_acc() {
		return r35_no_of_acc;
	}
	public void setR35_no_of_acc(BigDecimal r35_no_of_acc) {
		this.r35_no_of_acc = r35_no_of_acc;
	}
	public String getR36_sche_fore_ass() {
		return r36_sche_fore_ass;
	}
	public void setR36_sche_fore_ass(String r36_sche_fore_ass) {
		this.r36_sche_fore_ass = r36_sche_fore_ass;
	}
	public BigDecimal getR36_orig_amt() {
		return r36_orig_amt;
	}
	public void setR36_orig_amt(BigDecimal r36_orig_amt) {
		this.r36_orig_amt = r36_orig_amt;
	}
	public BigDecimal getR36_fore_amt() {
		return r36_fore_amt;
	}
	public void setR36_fore_amt(BigDecimal r36_fore_amt) {
		this.r36_fore_amt = r36_fore_amt;
	}
	public BigDecimal getR36_no_of_acc() {
		return r36_no_of_acc;
	}
	public void setR36_no_of_acc(BigDecimal r36_no_of_acc) {
		this.r36_no_of_acc = r36_no_of_acc;
	}
	public String getR37_sche_fore_ass() {
		return r37_sche_fore_ass;
	}
	public void setR37_sche_fore_ass(String r37_sche_fore_ass) {
		this.r37_sche_fore_ass = r37_sche_fore_ass;
	}
	public BigDecimal getR37_orig_amt() {
		return r37_orig_amt;
	}
	public void setR37_orig_amt(BigDecimal r37_orig_amt) {
		this.r37_orig_amt = r37_orig_amt;
	}
	public BigDecimal getR37_fore_amt() {
		return r37_fore_amt;
	}
	public void setR37_fore_amt(BigDecimal r37_fore_amt) {
		this.r37_fore_amt = r37_fore_amt;
	}
	public BigDecimal getR37_no_of_acc() {
		return r37_no_of_acc;
	}
	public void setR37_no_of_acc(BigDecimal r37_no_of_acc) {
		this.r37_no_of_acc = r37_no_of_acc;
	}
	public String getR38_sche_fore_ass() {
		return r38_sche_fore_ass;
	}
	public void setR38_sche_fore_ass(String r38_sche_fore_ass) {
		this.r38_sche_fore_ass = r38_sche_fore_ass;
	}
	public BigDecimal getR38_orig_amt() {
		return r38_orig_amt;
	}
	public void setR38_orig_amt(BigDecimal r38_orig_amt) {
		this.r38_orig_amt = r38_orig_amt;
	}
	public BigDecimal getR38_fore_amt() {
		return r38_fore_amt;
	}
	public void setR38_fore_amt(BigDecimal r38_fore_amt) {
		this.r38_fore_amt = r38_fore_amt;
	}
	public BigDecimal getR38_no_of_acc() {
		return r38_no_of_acc;
	}
	public void setR38_no_of_acc(BigDecimal r38_no_of_acc) {
		this.r38_no_of_acc = r38_no_of_acc;
	}
	public String getR39_sche_fore_ass() {
		return r39_sche_fore_ass;
	}
	public void setR39_sche_fore_ass(String r39_sche_fore_ass) {
		this.r39_sche_fore_ass = r39_sche_fore_ass;
	}
	public BigDecimal getR39_orig_amt() {
		return r39_orig_amt;
	}
	public void setR39_orig_amt(BigDecimal r39_orig_amt) {
		this.r39_orig_amt = r39_orig_amt;
	}
	public BigDecimal getR39_fore_amt() {
		return r39_fore_amt;
	}
	public void setR39_fore_amt(BigDecimal r39_fore_amt) {
		this.r39_fore_amt = r39_fore_amt;
	}
	public BigDecimal getR39_no_of_acc() {
		return r39_no_of_acc;
	}
	public void setR39_no_of_acc(BigDecimal r39_no_of_acc) {
		this.r39_no_of_acc = r39_no_of_acc;
	}
	public String getR40_sche_fore_ass() {
		return r40_sche_fore_ass;
	}
	public void setR40_sche_fore_ass(String r40_sche_fore_ass) {
		this.r40_sche_fore_ass = r40_sche_fore_ass;
	}
	public BigDecimal getR40_orig_amt() {
		return r40_orig_amt;
	}
	public void setR40_orig_amt(BigDecimal r40_orig_amt) {
		this.r40_orig_amt = r40_orig_amt;
	}
	public BigDecimal getR40_fore_amt() {
		return r40_fore_amt;
	}
	public void setR40_fore_amt(BigDecimal r40_fore_amt) {
		this.r40_fore_amt = r40_fore_amt;
	}
	public BigDecimal getR40_no_of_acc() {
		return r40_no_of_acc;
	}
	public void setR40_no_of_acc(BigDecimal r40_no_of_acc) {
		this.r40_no_of_acc = r40_no_of_acc;
	}
	public String getR41_sche_fore_ass() {
		return r41_sche_fore_ass;
	}
	public void setR41_sche_fore_ass(String r41_sche_fore_ass) {
		this.r41_sche_fore_ass = r41_sche_fore_ass;
	}
	public BigDecimal getR41_orig_amt() {
		return r41_orig_amt;
	}
	public void setR41_orig_amt(BigDecimal r41_orig_amt) {
		this.r41_orig_amt = r41_orig_amt;
	}
	public BigDecimal getR41_fore_amt() {
		return r41_fore_amt;
	}
	public void setR41_fore_amt(BigDecimal r41_fore_amt) {
		this.r41_fore_amt = r41_fore_amt;
	}
	public BigDecimal getR41_no_of_acc() {
		return r41_no_of_acc;
	}
	public void setR41_no_of_acc(BigDecimal r41_no_of_acc) {
		this.r41_no_of_acc = r41_no_of_acc;
	}
	public String getR42_sche_fore_ass() {
		return r42_sche_fore_ass;
	}
	public void setR42_sche_fore_ass(String r42_sche_fore_ass) {
		this.r42_sche_fore_ass = r42_sche_fore_ass;
	}
	public BigDecimal getR42_orig_amt() {
		return r42_orig_amt;
	}
	public void setR42_orig_amt(BigDecimal r42_orig_amt) {
		this.r42_orig_amt = r42_orig_amt;
	}
	public BigDecimal getR42_fore_amt() {
		return r42_fore_amt;
	}
	public void setR42_fore_amt(BigDecimal r42_fore_amt) {
		this.r42_fore_amt = r42_fore_amt;
	}
	public BigDecimal getR42_no_of_acc() {
		return r42_no_of_acc;
	}
	public void setR42_no_of_acc(BigDecimal r42_no_of_acc) {
		this.r42_no_of_acc = r42_no_of_acc;
	}
	public String getR43_sche_fore_ass() {
		return r43_sche_fore_ass;
	}
	public void setR43_sche_fore_ass(String r43_sche_fore_ass) {
		this.r43_sche_fore_ass = r43_sche_fore_ass;
	}
	public BigDecimal getR43_orig_amt() {
		return r43_orig_amt;
	}
	public void setR43_orig_amt(BigDecimal r43_orig_amt) {
		this.r43_orig_amt = r43_orig_amt;
	}
	public BigDecimal getR43_fore_amt() {
		return r43_fore_amt;
	}
	public void setR43_fore_amt(BigDecimal r43_fore_amt) {
		this.r43_fore_amt = r43_fore_amt;
	}
	public BigDecimal getR43_no_of_acc() {
		return r43_no_of_acc;
	}
	public void setR43_no_of_acc(BigDecimal r43_no_of_acc) {
		this.r43_no_of_acc = r43_no_of_acc;
	}
	public String getR44_sche_fore_ass() {
		return r44_sche_fore_ass;
	}
	public void setR44_sche_fore_ass(String r44_sche_fore_ass) {
		this.r44_sche_fore_ass = r44_sche_fore_ass;
	}
	public BigDecimal getR44_orig_amt() {
		return r44_orig_amt;
	}
	public void setR44_orig_amt(BigDecimal r44_orig_amt) {
		this.r44_orig_amt = r44_orig_amt;
	}
	public BigDecimal getR44_fore_amt() {
		return r44_fore_amt;
	}
	public void setR44_fore_amt(BigDecimal r44_fore_amt) {
		this.r44_fore_amt = r44_fore_amt;
	}
	public BigDecimal getR44_no_of_acc() {
		return r44_no_of_acc;
	}
	public void setR44_no_of_acc(BigDecimal r44_no_of_acc) {
		this.r44_no_of_acc = r44_no_of_acc;
	}
	public String getR45_sche_fore_ass() {
		return r45_sche_fore_ass;
	}
	public void setR45_sche_fore_ass(String r45_sche_fore_ass) {
		this.r45_sche_fore_ass = r45_sche_fore_ass;
	}
	public BigDecimal getR45_orig_amt() {
		return r45_orig_amt;
	}
	public void setR45_orig_amt(BigDecimal r45_orig_amt) {
		this.r45_orig_amt = r45_orig_amt;
	}
	public BigDecimal getR45_fore_amt() {
		return r45_fore_amt;
	}
	public void setR45_fore_amt(BigDecimal r45_fore_amt) {
		this.r45_fore_amt = r45_fore_amt;
	}
	public BigDecimal getR45_no_of_acc() {
		return r45_no_of_acc;
	}
	public void setR45_no_of_acc(BigDecimal r45_no_of_acc) {
		this.r45_no_of_acc = r45_no_of_acc;
	}
	public String getR46_sche_fore_ass() {
		return r46_sche_fore_ass;
	}
	public void setR46_sche_fore_ass(String r46_sche_fore_ass) {
		this.r46_sche_fore_ass = r46_sche_fore_ass;
	}
	public BigDecimal getR46_orig_amt() {
		return r46_orig_amt;
	}
	public void setR46_orig_amt(BigDecimal r46_orig_amt) {
		this.r46_orig_amt = r46_orig_amt;
	}
	public BigDecimal getR46_fore_amt() {
		return r46_fore_amt;
	}
	public void setR46_fore_amt(BigDecimal r46_fore_amt) {
		this.r46_fore_amt = r46_fore_amt;
	}
	public BigDecimal getR46_no_of_acc() {
		return r46_no_of_acc;
	}
	public void setR46_no_of_acc(BigDecimal r46_no_of_acc) {
		this.r46_no_of_acc = r46_no_of_acc;
	}
	public String getR47_sche_fore_ass() {
		return r47_sche_fore_ass;
	}
	public void setR47_sche_fore_ass(String r47_sche_fore_ass) {
		this.r47_sche_fore_ass = r47_sche_fore_ass;
	}
	public BigDecimal getR47_orig_amt() {
		return r47_orig_amt;
	}
	public void setR47_orig_amt(BigDecimal r47_orig_amt) {
		this.r47_orig_amt = r47_orig_amt;
	}
	public BigDecimal getR47_fore_amt() {
		return r47_fore_amt;
	}
	public void setR47_fore_amt(BigDecimal r47_fore_amt) {
		this.r47_fore_amt = r47_fore_amt;
	}
	public BigDecimal getR47_no_of_acc() {
		return r47_no_of_acc;
	}
	public void setR47_no_of_acc(BigDecimal r47_no_of_acc) {
		this.r47_no_of_acc = r47_no_of_acc;
	}
	public String getR48_sche_fore_ass() {
		return r48_sche_fore_ass;
	}
	public void setR48_sche_fore_ass(String r48_sche_fore_ass) {
		this.r48_sche_fore_ass = r48_sche_fore_ass;
	}
	public BigDecimal getR48_orig_amt() {
		return r48_orig_amt;
	}
	public void setR48_orig_amt(BigDecimal r48_orig_amt) {
		this.r48_orig_amt = r48_orig_amt;
	}
	public BigDecimal getR48_fore_amt() {
		return r48_fore_amt;
	}
	public void setR48_fore_amt(BigDecimal r48_fore_amt) {
		this.r48_fore_amt = r48_fore_amt;
	}
	public BigDecimal getR48_no_of_acc() {
		return r48_no_of_acc;
	}
	public void setR48_no_of_acc(BigDecimal r48_no_of_acc) {
		this.r48_no_of_acc = r48_no_of_acc;
	}
	public String getR49_sche_fore_ass() {
		return r49_sche_fore_ass;
	}
	public void setR49_sche_fore_ass(String r49_sche_fore_ass) {
		this.r49_sche_fore_ass = r49_sche_fore_ass;
	}
	public BigDecimal getR49_orig_amt() {
		return r49_orig_amt;
	}
	public void setR49_orig_amt(BigDecimal r49_orig_amt) {
		this.r49_orig_amt = r49_orig_amt;
	}
	public BigDecimal getR49_fore_amt() {
		return r49_fore_amt;
	}
	public void setR49_fore_amt(BigDecimal r49_fore_amt) {
		this.r49_fore_amt = r49_fore_amt;
	}
	public BigDecimal getR49_no_of_acc() {
		return r49_no_of_acc;
	}
	public void setR49_no_of_acc(BigDecimal r49_no_of_acc) {
		this.r49_no_of_acc = r49_no_of_acc;
	}
	public String getR50_sche_fore_ass() {
		return r50_sche_fore_ass;
	}
	public void setR50_sche_fore_ass(String r50_sche_fore_ass) {
		this.r50_sche_fore_ass = r50_sche_fore_ass;
	}
	public BigDecimal getR50_orig_amt() {
		return r50_orig_amt;
	}
	public void setR50_orig_amt(BigDecimal r50_orig_amt) {
		this.r50_orig_amt = r50_orig_amt;
	}
	public BigDecimal getR50_fore_amt() {
		return r50_fore_amt;
	}
	public void setR50_fore_amt(BigDecimal r50_fore_amt) {
		this.r50_fore_amt = r50_fore_amt;
	}
	public BigDecimal getR50_no_of_acc() {
		return r50_no_of_acc;
	}
	public void setR50_no_of_acc(BigDecimal r50_no_of_acc) {
		this.r50_no_of_acc = r50_no_of_acc;
	}
	public String getR51_sche_fore_ass() {
		return r51_sche_fore_ass;
	}
	public void setR51_sche_fore_ass(String r51_sche_fore_ass) {
		this.r51_sche_fore_ass = r51_sche_fore_ass;
	}
	public BigDecimal getR51_orig_amt() {
		return r51_orig_amt;
	}
	public void setR51_orig_amt(BigDecimal r51_orig_amt) {
		this.r51_orig_amt = r51_orig_amt;
	}
	public BigDecimal getR51_fore_amt() {
		return r51_fore_amt;
	}
	public void setR51_fore_amt(BigDecimal r51_fore_amt) {
		this.r51_fore_amt = r51_fore_amt;
	}
	public BigDecimal getR51_no_of_acc() {
		return r51_no_of_acc;
	}
	public void setR51_no_of_acc(BigDecimal r51_no_of_acc) {
		this.r51_no_of_acc = r51_no_of_acc;
	}
	public String getR52_sche_fore_ass() {
		return r52_sche_fore_ass;
	}
	public void setR52_sche_fore_ass(String r52_sche_fore_ass) {
		this.r52_sche_fore_ass = r52_sche_fore_ass;
	}
	public BigDecimal getR52_orig_amt() {
		return r52_orig_amt;
	}
	public void setR52_orig_amt(BigDecimal r52_orig_amt) {
		this.r52_orig_amt = r52_orig_amt;
	}
	public BigDecimal getR52_fore_amt() {
		return r52_fore_amt;
	}
	public void setR52_fore_amt(BigDecimal r52_fore_amt) {
		this.r52_fore_amt = r52_fore_amt;
	}
	public BigDecimal getR52_no_of_acc() {
		return r52_no_of_acc;
	}
	public void setR52_no_of_acc(BigDecimal r52_no_of_acc) {
		this.r52_no_of_acc = r52_no_of_acc;
	}
	public String getR53_sche_fore_ass() {
		return r53_sche_fore_ass;
	}
	public void setR53_sche_fore_ass(String r53_sche_fore_ass) {
		this.r53_sche_fore_ass = r53_sche_fore_ass;
	}
	public BigDecimal getR53_orig_amt() {
		return r53_orig_amt;
	}
	public void setR53_orig_amt(BigDecimal r53_orig_amt) {
		this.r53_orig_amt = r53_orig_amt;
	}
	public BigDecimal getR53_fore_amt() {
		return r53_fore_amt;
	}
	public void setR53_fore_amt(BigDecimal r53_fore_amt) {
		this.r53_fore_amt = r53_fore_amt;
	}
	public BigDecimal getR53_no_of_acc() {
		return r53_no_of_acc;
	}
	public void setR53_no_of_acc(BigDecimal r53_no_of_acc) {
		this.r53_no_of_acc = r53_no_of_acc;
	}
	public String getR54_sche_fore_ass() {
		return r54_sche_fore_ass;
	}
	public void setR54_sche_fore_ass(String r54_sche_fore_ass) {
		this.r54_sche_fore_ass = r54_sche_fore_ass;
	}
	public BigDecimal getR54_orig_amt() {
		return r54_orig_amt;
	}
	public void setR54_orig_amt(BigDecimal r54_orig_amt) {
		this.r54_orig_amt = r54_orig_amt;
	}
	public BigDecimal getR54_fore_amt() {
		return r54_fore_amt;
	}
	public void setR54_fore_amt(BigDecimal r54_fore_amt) {
		this.r54_fore_amt = r54_fore_amt;
	}
	public BigDecimal getR54_no_of_acc() {
		return r54_no_of_acc;
	}
	public void setR54_no_of_acc(BigDecimal r54_no_of_acc) {
		this.r54_no_of_acc = r54_no_of_acc;
	}
	public String getR55_sche_fore_ass() {
		return r55_sche_fore_ass;
	}
	public void setR55_sche_fore_ass(String r55_sche_fore_ass) {
		this.r55_sche_fore_ass = r55_sche_fore_ass;
	}
	public BigDecimal getR55_orig_amt() {
		return r55_orig_amt;
	}
	public void setR55_orig_amt(BigDecimal r55_orig_amt) {
		this.r55_orig_amt = r55_orig_amt;
	}
	public BigDecimal getR55_fore_amt() {
		return r55_fore_amt;
	}
	public void setR55_fore_amt(BigDecimal r55_fore_amt) {
		this.r55_fore_amt = r55_fore_amt;
	}
	public BigDecimal getR55_no_of_acc() {
		return r55_no_of_acc;
	}
	public void setR55_no_of_acc(BigDecimal r55_no_of_acc) {
		this.r55_no_of_acc = r55_no_of_acc;
	}
	public String getR56_sche_fore_ass() {
		return r56_sche_fore_ass;
	}
	public void setR56_sche_fore_ass(String r56_sche_fore_ass) {
		this.r56_sche_fore_ass = r56_sche_fore_ass;
	}
	public BigDecimal getR56_orig_amt() {
		return r56_orig_amt;
	}
	public void setR56_orig_amt(BigDecimal r56_orig_amt) {
		this.r56_orig_amt = r56_orig_amt;
	}
	public BigDecimal getR56_fore_amt() {
		return r56_fore_amt;
	}
	public void setR56_fore_amt(BigDecimal r56_fore_amt) {
		this.r56_fore_amt = r56_fore_amt;
	}
	public BigDecimal getR56_no_of_acc() {
		return r56_no_of_acc;
	}
	public void setR56_no_of_acc(BigDecimal r56_no_of_acc) {
		this.r56_no_of_acc = r56_no_of_acc;
	}
	public String getR57_sche_fore_ass() {
		return r57_sche_fore_ass;
	}
	public void setR57_sche_fore_ass(String r57_sche_fore_ass) {
		this.r57_sche_fore_ass = r57_sche_fore_ass;
	}
	public BigDecimal getR57_orig_amt() {
		return r57_orig_amt;
	}
	public void setR57_orig_amt(BigDecimal r57_orig_amt) {
		this.r57_orig_amt = r57_orig_amt;
	}
	public BigDecimal getR57_fore_amt() {
		return r57_fore_amt;
	}
	public void setR57_fore_amt(BigDecimal r57_fore_amt) {
		this.r57_fore_amt = r57_fore_amt;
	}
	public BigDecimal getR57_no_of_acc() {
		return r57_no_of_acc;
	}
	public void setR57_no_of_acc(BigDecimal r57_no_of_acc) {
		this.r57_no_of_acc = r57_no_of_acc;
	}
	public String getR58_sche_fore_ass() {
		return r58_sche_fore_ass;
	}
	public void setR58_sche_fore_ass(String r58_sche_fore_ass) {
		this.r58_sche_fore_ass = r58_sche_fore_ass;
	}
	public BigDecimal getR58_orig_amt() {
		return r58_orig_amt;
	}
	public void setR58_orig_amt(BigDecimal r58_orig_amt) {
		this.r58_orig_amt = r58_orig_amt;
	}
	public BigDecimal getR58_fore_amt() {
		return r58_fore_amt;
	}
	public void setR58_fore_amt(BigDecimal r58_fore_amt) {
		this.r58_fore_amt = r58_fore_amt;
	}
	public BigDecimal getR58_no_of_acc() {
		return r58_no_of_acc;
	}
	public void setR58_no_of_acc(BigDecimal r58_no_of_acc) {
		this.r58_no_of_acc = r58_no_of_acc;
	}
	public String getR59_sche_fore_ass() {
		return r59_sche_fore_ass;
	}
	public void setR59_sche_fore_ass(String r59_sche_fore_ass) {
		this.r59_sche_fore_ass = r59_sche_fore_ass;
	}
	public BigDecimal getR59_orig_amt() {
		return r59_orig_amt;
	}
	public void setR59_orig_amt(BigDecimal r59_orig_amt) {
		this.r59_orig_amt = r59_orig_amt;
	}
	public BigDecimal getR59_fore_amt() {
		return r59_fore_amt;
	}
	public void setR59_fore_amt(BigDecimal r59_fore_amt) {
		this.r59_fore_amt = r59_fore_amt;
	}
	public BigDecimal getR59_no_of_acc() {
		return r59_no_of_acc;
	}
	public void setR59_no_of_acc(BigDecimal r59_no_of_acc) {
		this.r59_no_of_acc = r59_no_of_acc;
	}
	public String getR60_sche_fore_ass() {
		return r60_sche_fore_ass;
	}
	public void setR60_sche_fore_ass(String r60_sche_fore_ass) {
		this.r60_sche_fore_ass = r60_sche_fore_ass;
	}
	public BigDecimal getR60_orig_amt() {
		return r60_orig_amt;
	}
	public void setR60_orig_amt(BigDecimal r60_orig_amt) {
		this.r60_orig_amt = r60_orig_amt;
	}
	public BigDecimal getR60_fore_amt() {
		return r60_fore_amt;
	}
	public void setR60_fore_amt(BigDecimal r60_fore_amt) {
		this.r60_fore_amt = r60_fore_amt;
	}
	public BigDecimal getR60_no_of_acc() {
		return r60_no_of_acc;
	}
	public void setR60_no_of_acc(BigDecimal r60_no_of_acc) {
		this.r60_no_of_acc = r60_no_of_acc;
	}
	public String getR61_sche_fore_ass() {
		return r61_sche_fore_ass;
	}
	public void setR61_sche_fore_ass(String r61_sche_fore_ass) {
		this.r61_sche_fore_ass = r61_sche_fore_ass;
	}
	public BigDecimal getR61_orig_amt() {
		return r61_orig_amt;
	}
	public void setR61_orig_amt(BigDecimal r61_orig_amt) {
		this.r61_orig_amt = r61_orig_amt;
	}
	public BigDecimal getR61_fore_amt() {
		return r61_fore_amt;
	}
	public void setR61_fore_amt(BigDecimal r61_fore_amt) {
		this.r61_fore_amt = r61_fore_amt;
	}
	public BigDecimal getR61_no_of_acc() {
		return r61_no_of_acc;
	}
	public void setR61_no_of_acc(BigDecimal r61_no_of_acc) {
		this.r61_no_of_acc = r61_no_of_acc;
	}
	public String getR62_sche_fore_ass() {
		return r62_sche_fore_ass;
	}
	public void setR62_sche_fore_ass(String r62_sche_fore_ass) {
		this.r62_sche_fore_ass = r62_sche_fore_ass;
	}
	public BigDecimal getR62_orig_amt() {
		return r62_orig_amt;
	}
	public void setR62_orig_amt(BigDecimal r62_orig_amt) {
		this.r62_orig_amt = r62_orig_amt;
	}
	public BigDecimal getR62_fore_amt() {
		return r62_fore_amt;
	}
	public void setR62_fore_amt(BigDecimal r62_fore_amt) {
		this.r62_fore_amt = r62_fore_amt;
	}
	public BigDecimal getR62_no_of_acc() {
		return r62_no_of_acc;
	}
	public void setR62_no_of_acc(BigDecimal r62_no_of_acc) {
		this.r62_no_of_acc = r62_no_of_acc;
	}
	public String getR63_sche_fore_ass() {
		return r63_sche_fore_ass;
	}
	public void setR63_sche_fore_ass(String r63_sche_fore_ass) {
		this.r63_sche_fore_ass = r63_sche_fore_ass;
	}
	public BigDecimal getR63_orig_amt() {
		return r63_orig_amt;
	}
	public void setR63_orig_amt(BigDecimal r63_orig_amt) {
		this.r63_orig_amt = r63_orig_amt;
	}
	public BigDecimal getR63_fore_amt() {
		return r63_fore_amt;
	}
	public void setR63_fore_amt(BigDecimal r63_fore_amt) {
		this.r63_fore_amt = r63_fore_amt;
	}
	public BigDecimal getR63_no_of_acc() {
		return r63_no_of_acc;
	}
	public void setR63_no_of_acc(BigDecimal r63_no_of_acc) {
		this.r63_no_of_acc = r63_no_of_acc;
	}
	public String getR64_sche_fore_ass() {
		return r64_sche_fore_ass;
	}
	public void setR64_sche_fore_ass(String r64_sche_fore_ass) {
		this.r64_sche_fore_ass = r64_sche_fore_ass;
	}
	public BigDecimal getR64_orig_amt() {
		return r64_orig_amt;
	}
	public void setR64_orig_amt(BigDecimal r64_orig_amt) {
		this.r64_orig_amt = r64_orig_amt;
	}
	public BigDecimal getR64_fore_amt() {
		return r64_fore_amt;
	}
	public void setR64_fore_amt(BigDecimal r64_fore_amt) {
		this.r64_fore_amt = r64_fore_amt;
	}
	public BigDecimal getR64_no_of_acc() {
		return r64_no_of_acc;
	}
	public void setR64_no_of_acc(BigDecimal r64_no_of_acc) {
		this.r64_no_of_acc = r64_no_of_acc;
	}
	public String getR65_sche_fore_ass() {
		return r65_sche_fore_ass;
	}
	public void setR65_sche_fore_ass(String r65_sche_fore_ass) {
		this.r65_sche_fore_ass = r65_sche_fore_ass;
	}
	public BigDecimal getR65_orig_amt() {
		return r65_orig_amt;
	}
	public void setR65_orig_amt(BigDecimal r65_orig_amt) {
		this.r65_orig_amt = r65_orig_amt;
	}
	public BigDecimal getR65_fore_amt() {
		return r65_fore_amt;
	}
	public void setR65_fore_amt(BigDecimal r65_fore_amt) {
		this.r65_fore_amt = r65_fore_amt;
	}
	public BigDecimal getR65_no_of_acc() {
		return r65_no_of_acc;
	}
	public void setR65_no_of_acc(BigDecimal r65_no_of_acc) {
		this.r65_no_of_acc = r65_no_of_acc;
	}
	public String getR66_sche_fore_ass() {
		return r66_sche_fore_ass;
	}
	public void setR66_sche_fore_ass(String r66_sche_fore_ass) {
		this.r66_sche_fore_ass = r66_sche_fore_ass;
	}
	public BigDecimal getR66_orig_amt() {
		return r66_orig_amt;
	}
	public void setR66_orig_amt(BigDecimal r66_orig_amt) {
		this.r66_orig_amt = r66_orig_amt;
	}
	public BigDecimal getR66_fore_amt() {
		return r66_fore_amt;
	}
	public void setR66_fore_amt(BigDecimal r66_fore_amt) {
		this.r66_fore_amt = r66_fore_amt;
	}
	public BigDecimal getR66_no_of_acc() {
		return r66_no_of_acc;
	}
	public void setR66_no_of_acc(BigDecimal r66_no_of_acc) {
		this.r66_no_of_acc = r66_no_of_acc;
	}
	public String getR67_sche_fore_ass() {
		return r67_sche_fore_ass;
	}
	public void setR67_sche_fore_ass(String r67_sche_fore_ass) {
		this.r67_sche_fore_ass = r67_sche_fore_ass;
	}
	public BigDecimal getR67_orig_amt() {
		return r67_orig_amt;
	}
	public void setR67_orig_amt(BigDecimal r67_orig_amt) {
		this.r67_orig_amt = r67_orig_amt;
	}
	public BigDecimal getR67_fore_amt() {
		return r67_fore_amt;
	}
	public void setR67_fore_amt(BigDecimal r67_fore_amt) {
		this.r67_fore_amt = r67_fore_amt;
	}
	public BigDecimal getR67_no_of_acc() {
		return r67_no_of_acc;
	}
	public void setR67_no_of_acc(BigDecimal r67_no_of_acc) {
		this.r67_no_of_acc = r67_no_of_acc;
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
// DETAIL ENTITY  Q_RLFA2
// =====================================================	

public class Q_RLFA2_Detail_RowMapper implements RowMapper<Q_RLFA2_Detail_Entity> {

    @Override
    public Q_RLFA2_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        Q_RLFA2_Detail_Entity obj = new Q_RLFA2_Detail_Entity();

// =========================
// R10
// =========================
obj.setR10_sche_fore_ass(rs.getString("r10_sche_fore_ass"));
obj.setR10_orig_amt(rs.getBigDecimal("r10_orig_amt"));
obj.setR10_fore_amt(rs.getBigDecimal("r10_fore_amt"));
obj.setR10_no_of_acc(rs.getBigDecimal("r10_no_of_acc"));


// =========================
// R11
// =========================
obj.setR11_sche_fore_ass(rs.getString("r11_sche_fore_ass"));
obj.setR11_orig_amt(rs.getBigDecimal("r11_orig_amt"));
obj.setR11_fore_amt(rs.getBigDecimal("r11_fore_amt"));
obj.setR11_no_of_acc(rs.getBigDecimal("r11_no_of_acc"));

// =========================
// R12
// =========================
obj.setR12_sche_fore_ass(rs.getString("r12_sche_fore_ass"));
obj.setR12_orig_amt(rs.getBigDecimal("r12_orig_amt"));
obj.setR12_fore_amt(rs.getBigDecimal("r12_fore_amt"));
obj.setR12_no_of_acc(rs.getBigDecimal("r12_no_of_acc"));

// =========================
// R13
// =========================
obj.setR13_sche_fore_ass(rs.getString("r13_sche_fore_ass"));
obj.setR13_orig_amt(rs.getBigDecimal("r13_orig_amt"));
obj.setR13_fore_amt(rs.getBigDecimal("r13_fore_amt"));
obj.setR13_no_of_acc(rs.getBigDecimal("r13_no_of_acc"));

// =========================
// R14
// =========================
obj.setR14_sche_fore_ass(rs.getString("r14_sche_fore_ass"));
obj.setR14_orig_amt(rs.getBigDecimal("r14_orig_amt"));
obj.setR14_fore_amt(rs.getBigDecimal("r14_fore_amt"));
obj.setR14_no_of_acc(rs.getBigDecimal("r14_no_of_acc"));

// =========================
// R15
// =========================
obj.setR15_sche_fore_ass(rs.getString("r15_sche_fore_ass"));
obj.setR15_orig_amt(rs.getBigDecimal("r15_orig_amt"));
obj.setR15_fore_amt(rs.getBigDecimal("r15_fore_amt"));
obj.setR15_no_of_acc(rs.getBigDecimal("r15_no_of_acc"));

// =========================
// R16
// =========================
obj.setR16_sche_fore_ass(rs.getString("r16_sche_fore_ass"));
obj.setR16_orig_amt(rs.getBigDecimal("r16_orig_amt"));
obj.setR16_fore_amt(rs.getBigDecimal("r16_fore_amt"));
obj.setR16_no_of_acc(rs.getBigDecimal("r16_no_of_acc"));

// =========================
// R17
// =========================
obj.setR17_sche_fore_ass(rs.getString("r17_sche_fore_ass"));
obj.setR17_orig_amt(rs.getBigDecimal("r17_orig_amt"));
obj.setR17_fore_amt(rs.getBigDecimal("r17_fore_amt"));
obj.setR17_no_of_acc(rs.getBigDecimal("r17_no_of_acc"));

// =========================
// R18
// =========================
obj.setR18_sche_fore_ass(rs.getString("r18_sche_fore_ass"));
obj.setR18_orig_amt(rs.getBigDecimal("r18_orig_amt"));
obj.setR18_fore_amt(rs.getBigDecimal("r18_fore_amt"));
obj.setR18_no_of_acc(rs.getBigDecimal("r18_no_of_acc"));

// =========================
// R19
// =========================
obj.setR19_sche_fore_ass(rs.getString("r19_sche_fore_ass"));
obj.setR19_orig_amt(rs.getBigDecimal("r19_orig_amt"));
obj.setR19_fore_amt(rs.getBigDecimal("r19_fore_amt"));
obj.setR19_no_of_acc(rs.getBigDecimal("r19_no_of_acc"));

// =========================
// R20
// =========================
obj.setR20_sche_fore_ass(rs.getString("r20_sche_fore_ass"));
obj.setR20_orig_amt(rs.getBigDecimal("r20_orig_amt"));
obj.setR20_fore_amt(rs.getBigDecimal("r20_fore_amt"));
obj.setR20_no_of_acc(rs.getBigDecimal("r20_no_of_acc"));

// =========================
// R21
// =========================
obj.setR21_sche_fore_ass(rs.getString("r21_sche_fore_ass"));
obj.setR21_orig_amt(rs.getBigDecimal("r21_orig_amt"));
obj.setR21_fore_amt(rs.getBigDecimal("r21_fore_amt"));
obj.setR21_no_of_acc(rs.getBigDecimal("r21_no_of_acc"));

// =========================
// R22
// =========================
obj.setR22_sche_fore_ass(rs.getString("r22_sche_fore_ass"));
obj.setR22_orig_amt(rs.getBigDecimal("r22_orig_amt"));
obj.setR22_fore_amt(rs.getBigDecimal("r22_fore_amt"));
obj.setR22_no_of_acc(rs.getBigDecimal("r22_no_of_acc"));

// =========================
// R23
// =========================
obj.setR23_sche_fore_ass(rs.getString("r23_sche_fore_ass"));
obj.setR23_orig_amt(rs.getBigDecimal("r23_orig_amt"));
obj.setR23_fore_amt(rs.getBigDecimal("r23_fore_amt"));
obj.setR23_no_of_acc(rs.getBigDecimal("r23_no_of_acc"));

// =========================
// R24
// =========================
obj.setR24_sche_fore_ass(rs.getString("r24_sche_fore_ass"));
obj.setR24_orig_amt(rs.getBigDecimal("r24_orig_amt"));
obj.setR24_fore_amt(rs.getBigDecimal("r24_fore_amt"));
obj.setR24_no_of_acc(rs.getBigDecimal("r24_no_of_acc"));

// =========================
// R25
// =========================
obj.setR25_sche_fore_ass(rs.getString("r25_sche_fore_ass"));
obj.setR25_orig_amt(rs.getBigDecimal("r25_orig_amt"));
obj.setR25_fore_amt(rs.getBigDecimal("r25_fore_amt"));
obj.setR25_no_of_acc(rs.getBigDecimal("r25_no_of_acc"));

// =========================
// R26
// =========================
obj.setR26_sche_fore_ass(rs.getString("r26_sche_fore_ass"));
obj.setR26_orig_amt(rs.getBigDecimal("r26_orig_amt"));
obj.setR26_fore_amt(rs.getBigDecimal("r26_fore_amt"));
obj.setR26_no_of_acc(rs.getBigDecimal("r26_no_of_acc"));

// =========================
// R27
// =========================
obj.setR27_sche_fore_ass(rs.getString("r27_sche_fore_ass"));
obj.setR27_orig_amt(rs.getBigDecimal("r27_orig_amt"));
obj.setR27_fore_amt(rs.getBigDecimal("r27_fore_amt"));
obj.setR27_no_of_acc(rs.getBigDecimal("r27_no_of_acc"));

// =========================
// R28
// =========================
obj.setR28_sche_fore_ass(rs.getString("r28_sche_fore_ass"));
obj.setR28_orig_amt(rs.getBigDecimal("r28_orig_amt"));
obj.setR28_fore_amt(rs.getBigDecimal("r28_fore_amt"));
obj.setR28_no_of_acc(rs.getBigDecimal("r28_no_of_acc"));

// =========================
// R29
// =========================
obj.setR29_sche_fore_ass(rs.getString("r29_sche_fore_ass"));
obj.setR29_orig_amt(rs.getBigDecimal("r29_orig_amt"));
obj.setR29_fore_amt(rs.getBigDecimal("r29_fore_amt"));
obj.setR29_no_of_acc(rs.getBigDecimal("r29_no_of_acc"));

// =========================
// R30
// =========================
obj.setR30_sche_fore_ass(rs.getString("r30_sche_fore_ass"));
obj.setR30_orig_amt(rs.getBigDecimal("r30_orig_amt"));
obj.setR30_fore_amt(rs.getBigDecimal("r30_fore_amt"));
obj.setR30_no_of_acc(rs.getBigDecimal("r30_no_of_acc"));


// =========================
// R31
// =========================
obj.setR31_sche_fore_ass(rs.getString("r31_sche_fore_ass"));
obj.setR31_orig_amt(rs.getBigDecimal("r31_orig_amt"));
obj.setR31_fore_amt(rs.getBigDecimal("r31_fore_amt"));
obj.setR31_no_of_acc(rs.getBigDecimal("r31_no_of_acc"));

// =========================
// R32
// =========================
obj.setR32_sche_fore_ass(rs.getString("r32_sche_fore_ass"));
obj.setR32_orig_amt(rs.getBigDecimal("r32_orig_amt"));
obj.setR32_fore_amt(rs.getBigDecimal("r32_fore_amt"));
obj.setR32_no_of_acc(rs.getBigDecimal("r32_no_of_acc"));

// =========================
// R33
// =========================
obj.setR33_sche_fore_ass(rs.getString("r33_sche_fore_ass"));
obj.setR33_orig_amt(rs.getBigDecimal("r33_orig_amt"));
obj.setR33_fore_amt(rs.getBigDecimal("r33_fore_amt"));
obj.setR33_no_of_acc(rs.getBigDecimal("r33_no_of_acc"));

// =========================
// R34
// =========================
obj.setR34_sche_fore_ass(rs.getString("r34_sche_fore_ass"));
obj.setR34_orig_amt(rs.getBigDecimal("r34_orig_amt"));
obj.setR34_fore_amt(rs.getBigDecimal("r34_fore_amt"));
obj.setR34_no_of_acc(rs.getBigDecimal("r34_no_of_acc"));

// =========================
// R35
// =========================
obj.setR35_sche_fore_ass(rs.getString("r35_sche_fore_ass"));
obj.setR35_orig_amt(rs.getBigDecimal("r35_orig_amt"));
obj.setR35_fore_amt(rs.getBigDecimal("r35_fore_amt"));
obj.setR35_no_of_acc(rs.getBigDecimal("r35_no_of_acc"));

// =========================
// R36
// =========================
obj.setR36_sche_fore_ass(rs.getString("r36_sche_fore_ass"));
obj.setR36_orig_amt(rs.getBigDecimal("r36_orig_amt"));
obj.setR36_fore_amt(rs.getBigDecimal("r36_fore_amt"));
obj.setR36_no_of_acc(rs.getBigDecimal("r36_no_of_acc"));

// =========================
// R37
// =========================
obj.setR37_sche_fore_ass(rs.getString("r37_sche_fore_ass"));
obj.setR37_orig_amt(rs.getBigDecimal("r37_orig_amt"));
obj.setR37_fore_amt(rs.getBigDecimal("r37_fore_amt"));
obj.setR37_no_of_acc(rs.getBigDecimal("r37_no_of_acc"));

// =========================
// R38
// =========================
obj.setR38_sche_fore_ass(rs.getString("r38_sche_fore_ass"));
obj.setR38_orig_amt(rs.getBigDecimal("r38_orig_amt"));
obj.setR38_fore_amt(rs.getBigDecimal("r38_fore_amt"));
obj.setR38_no_of_acc(rs.getBigDecimal("r38_no_of_acc"));

// =========================
// R39
// =========================
obj.setR39_sche_fore_ass(rs.getString("r39_sche_fore_ass"));
obj.setR39_orig_amt(rs.getBigDecimal("r39_orig_amt"));
obj.setR39_fore_amt(rs.getBigDecimal("r39_fore_amt"));
obj.setR39_no_of_acc(rs.getBigDecimal("r39_no_of_acc"));

// =========================
// R40
// =========================
obj.setR40_sche_fore_ass(rs.getString("r40_sche_fore_ass"));
obj.setR40_orig_amt(rs.getBigDecimal("r40_orig_amt"));
obj.setR40_fore_amt(rs.getBigDecimal("r40_fore_amt"));
obj.setR40_no_of_acc(rs.getBigDecimal("r40_no_of_acc"));

// =========================
// R41
// =========================
obj.setR41_sche_fore_ass(rs.getString("r41_sche_fore_ass"));
obj.setR41_orig_amt(rs.getBigDecimal("r41_orig_amt"));
obj.setR41_fore_amt(rs.getBigDecimal("r41_fore_amt"));
obj.setR41_no_of_acc(rs.getBigDecimal("r41_no_of_acc"));

// =========================
// R42
// =========================
obj.setR42_sche_fore_ass(rs.getString("r42_sche_fore_ass"));
obj.setR42_orig_amt(rs.getBigDecimal("r42_orig_amt"));
obj.setR42_fore_amt(rs.getBigDecimal("r42_fore_amt"));
obj.setR42_no_of_acc(rs.getBigDecimal("r42_no_of_acc"));

// =========================
// R43
// =========================
obj.setR43_sche_fore_ass(rs.getString("r43_sche_fore_ass"));
obj.setR43_orig_amt(rs.getBigDecimal("r43_orig_amt"));
obj.setR43_fore_amt(rs.getBigDecimal("r43_fore_amt"));
obj.setR43_no_of_acc(rs.getBigDecimal("r43_no_of_acc"));

// =========================
// R44
// =========================
obj.setR44_sche_fore_ass(rs.getString("r44_sche_fore_ass"));
obj.setR44_orig_amt(rs.getBigDecimal("r44_orig_amt"));
obj.setR44_fore_amt(rs.getBigDecimal("r44_fore_amt"));
obj.setR44_no_of_acc(rs.getBigDecimal("r44_no_of_acc"));

// =========================
// R45
// =========================
obj.setR45_sche_fore_ass(rs.getString("r45_sche_fore_ass"));
obj.setR45_orig_amt(rs.getBigDecimal("r45_orig_amt"));
obj.setR45_fore_amt(rs.getBigDecimal("r45_fore_amt"));
obj.setR45_no_of_acc(rs.getBigDecimal("r45_no_of_acc"));

// =========================
// R46
// =========================
obj.setR46_sche_fore_ass(rs.getString("r46_sche_fore_ass"));
obj.setR46_orig_amt(rs.getBigDecimal("r46_orig_amt"));
obj.setR46_fore_amt(rs.getBigDecimal("r46_fore_amt"));
obj.setR46_no_of_acc(rs.getBigDecimal("r46_no_of_acc"));

// =========================
// R47
// =========================
obj.setR47_sche_fore_ass(rs.getString("r47_sche_fore_ass"));
obj.setR47_orig_amt(rs.getBigDecimal("r47_orig_amt"));
obj.setR47_fore_amt(rs.getBigDecimal("r47_fore_amt"));
obj.setR47_no_of_acc(rs.getBigDecimal("r47_no_of_acc"));

// =========================
// R48
// =========================
obj.setR48_sche_fore_ass(rs.getString("r48_sche_fore_ass"));
obj.setR48_orig_amt(rs.getBigDecimal("r48_orig_amt"));
obj.setR48_fore_amt(rs.getBigDecimal("r48_fore_amt"));
obj.setR48_no_of_acc(rs.getBigDecimal("r48_no_of_acc"));

// =========================
// R49
// =========================
obj.setR49_sche_fore_ass(rs.getString("r49_sche_fore_ass"));
obj.setR49_orig_amt(rs.getBigDecimal("r49_orig_amt"));
obj.setR49_fore_amt(rs.getBigDecimal("r49_fore_amt"));
obj.setR49_no_of_acc(rs.getBigDecimal("r49_no_of_acc"));

// =========================
// R50
// =========================
obj.setR50_sche_fore_ass(rs.getString("r50_sche_fore_ass"));
obj.setR50_orig_amt(rs.getBigDecimal("r50_orig_amt"));
obj.setR50_fore_amt(rs.getBigDecimal("r50_fore_amt"));
obj.setR50_no_of_acc(rs.getBigDecimal("r50_no_of_acc"));

// =========================
// R51
// =========================
obj.setR51_sche_fore_ass(rs.getString("r51_sche_fore_ass"));
obj.setR51_orig_amt(rs.getBigDecimal("r51_orig_amt"));
obj.setR51_fore_amt(rs.getBigDecimal("r51_fore_amt"));
obj.setR51_no_of_acc(rs.getBigDecimal("r51_no_of_acc"));

// =========================
// R52
// =========================
obj.setR52_sche_fore_ass(rs.getString("r52_sche_fore_ass"));
obj.setR52_orig_amt(rs.getBigDecimal("r52_orig_amt"));
obj.setR52_fore_amt(rs.getBigDecimal("r52_fore_amt"));
obj.setR52_no_of_acc(rs.getBigDecimal("r52_no_of_acc"));

// =========================
// R53
// =========================
obj.setR53_sche_fore_ass(rs.getString("r53_sche_fore_ass"));
obj.setR53_orig_amt(rs.getBigDecimal("r53_orig_amt"));
obj.setR53_fore_amt(rs.getBigDecimal("r53_fore_amt"));
obj.setR53_no_of_acc(rs.getBigDecimal("r53_no_of_acc"));

// =========================
// R54
// =========================
obj.setR54_sche_fore_ass(rs.getString("r54_sche_fore_ass"));
obj.setR54_orig_amt(rs.getBigDecimal("r54_orig_amt"));
obj.setR54_fore_amt(rs.getBigDecimal("r54_fore_amt"));
obj.setR54_no_of_acc(rs.getBigDecimal("r54_no_of_acc"));

// =========================
// R55
// =========================
obj.setR55_sche_fore_ass(rs.getString("r55_sche_fore_ass"));
obj.setR55_orig_amt(rs.getBigDecimal("r55_orig_amt"));
obj.setR55_fore_amt(rs.getBigDecimal("r55_fore_amt"));
obj.setR55_no_of_acc(rs.getBigDecimal("r55_no_of_acc"));

// =========================
// R56
// =========================
obj.setR56_sche_fore_ass(rs.getString("r56_sche_fore_ass"));
obj.setR56_orig_amt(rs.getBigDecimal("r56_orig_amt"));
obj.setR56_fore_amt(rs.getBigDecimal("r56_fore_amt"));
obj.setR56_no_of_acc(rs.getBigDecimal("r56_no_of_acc"));

// =========================
// R57
// =========================
obj.setR57_sche_fore_ass(rs.getString("r57_sche_fore_ass"));
obj.setR57_orig_amt(rs.getBigDecimal("r57_orig_amt"));
obj.setR57_fore_amt(rs.getBigDecimal("r57_fore_amt"));
obj.setR57_no_of_acc(rs.getBigDecimal("r57_no_of_acc"));

// =========================
// R58
// =========================
obj.setR58_sche_fore_ass(rs.getString("r58_sche_fore_ass"));
obj.setR58_orig_amt(rs.getBigDecimal("r58_orig_amt"));
obj.setR58_fore_amt(rs.getBigDecimal("r58_fore_amt"));
obj.setR58_no_of_acc(rs.getBigDecimal("r58_no_of_acc"));

// =========================
// R59
// =========================
obj.setR59_sche_fore_ass(rs.getString("r59_sche_fore_ass"));
obj.setR59_orig_amt(rs.getBigDecimal("r59_orig_amt"));
obj.setR59_fore_amt(rs.getBigDecimal("r59_fore_amt"));
obj.setR59_no_of_acc(rs.getBigDecimal("r59_no_of_acc"));

// =========================
// R60
// =========================
obj.setR60_sche_fore_ass(rs.getString("r60_sche_fore_ass"));
obj.setR60_orig_amt(rs.getBigDecimal("r60_orig_amt"));
obj.setR60_fore_amt(rs.getBigDecimal("r60_fore_amt"));
obj.setR60_no_of_acc(rs.getBigDecimal("r60_no_of_acc"));

// =========================
// R61
// =========================
obj.setR61_sche_fore_ass(rs.getString("r61_sche_fore_ass"));
obj.setR61_orig_amt(rs.getBigDecimal("r61_orig_amt"));
obj.setR61_fore_amt(rs.getBigDecimal("r61_fore_amt"));
obj.setR61_no_of_acc(rs.getBigDecimal("r61_no_of_acc"));

// =========================
// R62
// =========================
obj.setR62_sche_fore_ass(rs.getString("r62_sche_fore_ass"));
obj.setR62_orig_amt(rs.getBigDecimal("r62_orig_amt"));
obj.setR62_fore_amt(rs.getBigDecimal("r62_fore_amt"));
obj.setR62_no_of_acc(rs.getBigDecimal("r62_no_of_acc"));

// =========================
// R63
// =========================
obj.setR63_sche_fore_ass(rs.getString("r63_sche_fore_ass"));
obj.setR63_orig_amt(rs.getBigDecimal("r63_orig_amt"));
obj.setR63_fore_amt(rs.getBigDecimal("r63_fore_amt"));
obj.setR63_no_of_acc(rs.getBigDecimal("r63_no_of_acc"));

// =========================
// R64
// =========================
obj.setR64_sche_fore_ass(rs.getString("r64_sche_fore_ass"));
obj.setR64_orig_amt(rs.getBigDecimal("r64_orig_amt"));
obj.setR64_fore_amt(rs.getBigDecimal("r64_fore_amt"));
obj.setR64_no_of_acc(rs.getBigDecimal("r64_no_of_acc"));

// =========================
// R65
// =========================
obj.setR65_sche_fore_ass(rs.getString("r65_sche_fore_ass"));
obj.setR65_orig_amt(rs.getBigDecimal("r65_orig_amt"));
obj.setR65_fore_amt(rs.getBigDecimal("r65_fore_amt"));
obj.setR65_no_of_acc(rs.getBigDecimal("r65_no_of_acc"));

// =========================
// R66
// =========================
obj.setR66_sche_fore_ass(rs.getString("r66_sche_fore_ass"));
obj.setR66_orig_amt(rs.getBigDecimal("r66_orig_amt"));
obj.setR66_fore_amt(rs.getBigDecimal("r66_fore_amt"));
obj.setR66_no_of_acc(rs.getBigDecimal("r66_no_of_acc"));

// =========================
// R67
// =========================
obj.setR67_sche_fore_ass(rs.getString("r67_sche_fore_ass"));
obj.setR67_orig_amt(rs.getBigDecimal("r67_orig_amt"));
obj.setR67_fore_amt(rs.getBigDecimal("r67_fore_amt"));
obj.setR67_no_of_acc(rs.getBigDecimal("r67_no_of_acc"));


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

public class Q_RLFA2_Detail_Entity {

   
	private String	r10_sche_fore_ass;
	private BigDecimal	r10_orig_amt;
	private BigDecimal	r10_fore_amt;
	private BigDecimal	r10_no_of_acc;
	
	private String	r11_sche_fore_ass;
	private BigDecimal	r11_orig_amt;
	private BigDecimal	r11_fore_amt;
	private BigDecimal	r11_no_of_acc;
	
	private String	r12_sche_fore_ass;
	private BigDecimal	r12_orig_amt;
	private BigDecimal	r12_fore_amt;
	private BigDecimal	r12_no_of_acc;
	
	private String	r13_sche_fore_ass;
	private BigDecimal	r13_orig_amt;
	private BigDecimal	r13_fore_amt;
	private BigDecimal	r13_no_of_acc;
	
	private String	r14_sche_fore_ass;
	private BigDecimal	r14_orig_amt;
	private BigDecimal	r14_fore_amt;
	private BigDecimal	r14_no_of_acc;
	
	private String	r15_sche_fore_ass;
	private BigDecimal	r15_orig_amt;
	private BigDecimal	r15_fore_amt;
	private BigDecimal	r15_no_of_acc;
	
	private String	r16_sche_fore_ass;
	private BigDecimal	r16_orig_amt;
	private BigDecimal	r16_fore_amt;
	private BigDecimal	r16_no_of_acc;
	
	private String	r17_sche_fore_ass;
	private BigDecimal	r17_orig_amt;
	private BigDecimal	r17_fore_amt;
	private BigDecimal	r17_no_of_acc;
	
	private String	r18_sche_fore_ass;
	private BigDecimal	r18_orig_amt;
	private BigDecimal	r18_fore_amt;
	private BigDecimal	r18_no_of_acc;
	
	private String	r19_sche_fore_ass;
	private BigDecimal	r19_orig_amt;
	private BigDecimal	r19_fore_amt;
	private BigDecimal	r19_no_of_acc;
	
	private String	r20_sche_fore_ass;
	private BigDecimal	r20_orig_amt;
	private BigDecimal	r20_fore_amt;
	private BigDecimal	r20_no_of_acc;
	
	private String	r21_sche_fore_ass;
	private BigDecimal	r21_orig_amt;
	private BigDecimal	r21_fore_amt;
	private BigDecimal	r21_no_of_acc;
	
	private String	r22_sche_fore_ass;
	private BigDecimal	r22_orig_amt;
	private BigDecimal	r22_fore_amt;
	private BigDecimal	r22_no_of_acc;
	
	private String	r23_sche_fore_ass;
	private BigDecimal	r23_orig_amt;
	private BigDecimal	r23_fore_amt;
	private BigDecimal	r23_no_of_acc;
	
	private String	r24_sche_fore_ass;
	private BigDecimal	r24_orig_amt;
	private BigDecimal	r24_fore_amt;
	private BigDecimal	r24_no_of_acc;
	
	private String	r25_sche_fore_ass;
	private BigDecimal	r25_orig_amt;
	private BigDecimal	r25_fore_amt;
	private BigDecimal	r25_no_of_acc;
	
	private String	r26_sche_fore_ass;
	private BigDecimal	r26_orig_amt;
	private BigDecimal	r26_fore_amt;
	private BigDecimal	r26_no_of_acc;
	
	private String	r27_sche_fore_ass;
	private BigDecimal	r27_orig_amt;
	private BigDecimal	r27_fore_amt;
	private BigDecimal	r27_no_of_acc;
	
	private String	r28_sche_fore_ass;
	private BigDecimal	r28_orig_amt;
	private BigDecimal	r28_fore_amt;
	private BigDecimal	r28_no_of_acc;
	
	private String	r29_sche_fore_ass;
	private BigDecimal	r29_orig_amt;
	private BigDecimal	r29_fore_amt;
	private BigDecimal	r29_no_of_acc;
	
	private String	r30_sche_fore_ass;
	private BigDecimal	r30_orig_amt;
	private BigDecimal	r30_fore_amt;
	private BigDecimal	r30_no_of_acc;
	
	private String	r31_sche_fore_ass;
	private BigDecimal	r31_orig_amt;
	private BigDecimal	r31_fore_amt;
	private BigDecimal	r31_no_of_acc;
	
	private String	r32_sche_fore_ass;
	private BigDecimal	r32_orig_amt;
	private BigDecimal	r32_fore_amt;
	private BigDecimal	r32_no_of_acc;
	
	private String	r33_sche_fore_ass;
	private BigDecimal	r33_orig_amt;
	private BigDecimal	r33_fore_amt;
	private BigDecimal	r33_no_of_acc;
	
	private String	r34_sche_fore_ass;
	private BigDecimal	r34_orig_amt;
	private BigDecimal	r34_fore_amt;
	private BigDecimal	r34_no_of_acc;
	
	private String	r35_sche_fore_ass;
	private BigDecimal	r35_orig_amt;
	private BigDecimal	r35_fore_amt;
	private BigDecimal	r35_no_of_acc;
	
	private String	r36_sche_fore_ass;
	private BigDecimal	r36_orig_amt;
	private BigDecimal	r36_fore_amt;
	private BigDecimal	r36_no_of_acc;
	
	private String	r37_sche_fore_ass;
	private BigDecimal	r37_orig_amt;
	private BigDecimal	r37_fore_amt;
	private BigDecimal	r37_no_of_acc;
	
	private String	r38_sche_fore_ass;
	private BigDecimal	r38_orig_amt;
	private BigDecimal	r38_fore_amt;
	private BigDecimal	r38_no_of_acc;
	
	private String	r39_sche_fore_ass;
	private BigDecimal	r39_orig_amt;
	private BigDecimal	r39_fore_amt;
	private BigDecimal	r39_no_of_acc;
	
	private String	r40_sche_fore_ass;
	private BigDecimal	r40_orig_amt;
	private BigDecimal	r40_fore_amt;
	private BigDecimal	r40_no_of_acc;
	
	private String	r41_sche_fore_ass;
	private BigDecimal	r41_orig_amt;
	private BigDecimal	r41_fore_amt;
	private BigDecimal	r41_no_of_acc;
	
	private String	r42_sche_fore_ass;
	private BigDecimal	r42_orig_amt;
	private BigDecimal	r42_fore_amt;
	private BigDecimal	r42_no_of_acc;
	
	private String	r43_sche_fore_ass;
	private BigDecimal	r43_orig_amt;
	private BigDecimal	r43_fore_amt;
	private BigDecimal	r43_no_of_acc;
	
	private String	r44_sche_fore_ass;
	private BigDecimal	r44_orig_amt;
	private BigDecimal	r44_fore_amt;
	private BigDecimal	r44_no_of_acc;
	
	private String	r45_sche_fore_ass;
	private BigDecimal	r45_orig_amt;
	private BigDecimal	r45_fore_amt;
	private BigDecimal	r45_no_of_acc;
	
	private String	r46_sche_fore_ass;
	private BigDecimal	r46_orig_amt;
	private BigDecimal	r46_fore_amt;
	private BigDecimal	r46_no_of_acc;
	
	private String	r47_sche_fore_ass;
	private BigDecimal	r47_orig_amt;
	private BigDecimal	r47_fore_amt;
	private BigDecimal	r47_no_of_acc;
	
	private String	r48_sche_fore_ass;
	private BigDecimal	r48_orig_amt;
	private BigDecimal	r48_fore_amt;
	private BigDecimal	r48_no_of_acc;
	
	private String	r49_sche_fore_ass;
	private BigDecimal	r49_orig_amt;
	private BigDecimal	r49_fore_amt;
	private BigDecimal	r49_no_of_acc;
	
	private String	r50_sche_fore_ass;
	private BigDecimal	r50_orig_amt;
	private BigDecimal	r50_fore_amt;
	private BigDecimal	r50_no_of_acc;
	
	private String	r51_sche_fore_ass;
	private BigDecimal	r51_orig_amt;
	private BigDecimal	r51_fore_amt;
	private BigDecimal	r51_no_of_acc;
	
	private String	r52_sche_fore_ass;
	private BigDecimal	r52_orig_amt;
	private BigDecimal	r52_fore_amt;
	private BigDecimal	r52_no_of_acc;
	
	private String	r53_sche_fore_ass;
	private BigDecimal	r53_orig_amt;
	private BigDecimal	r53_fore_amt;
	private BigDecimal	r53_no_of_acc;
	
	private String	r54_sche_fore_ass;
	private BigDecimal	r54_orig_amt;
	private BigDecimal	r54_fore_amt;
	private BigDecimal	r54_no_of_acc;
	
	private String	r55_sche_fore_ass;
	private BigDecimal	r55_orig_amt;
	private BigDecimal	r55_fore_amt;
	private BigDecimal	r55_no_of_acc;
	
	private String	r56_sche_fore_ass;
	private BigDecimal	r56_orig_amt;
	private BigDecimal	r56_fore_amt;
	private BigDecimal	r56_no_of_acc;
	
	private String	r57_sche_fore_ass;
	private BigDecimal	r57_orig_amt;
	private BigDecimal	r57_fore_amt;
	private BigDecimal	r57_no_of_acc;
	
	private String	r58_sche_fore_ass;
	private BigDecimal	r58_orig_amt;
	private BigDecimal	r58_fore_amt;
	private BigDecimal	r58_no_of_acc;
	
	private String	r59_sche_fore_ass;
	private BigDecimal	r59_orig_amt;
	private BigDecimal	r59_fore_amt;
	private BigDecimal	r59_no_of_acc;
	
	private String	r60_sche_fore_ass;
	private BigDecimal	r60_orig_amt;
	private BigDecimal	r60_fore_amt;
	private BigDecimal	r60_no_of_acc;
	
	private String	r61_sche_fore_ass;
	private BigDecimal	r61_orig_amt;
	private BigDecimal	r61_fore_amt;
	private BigDecimal	r61_no_of_acc;
	
	private String	r62_sche_fore_ass;
	private BigDecimal	r62_orig_amt;
	private BigDecimal	r62_fore_amt;
	private BigDecimal	r62_no_of_acc;
	
	private String	r63_sche_fore_ass;
	private BigDecimal	r63_orig_amt;
	private BigDecimal	r63_fore_amt;
	private BigDecimal	r63_no_of_acc;
	
	// R64
	private String     r64_sche_fore_ass;
	private BigDecimal r64_orig_amt;
	private BigDecimal r64_fore_amt;
	private BigDecimal r64_no_of_acc;

	// R65
	private String     r65_sche_fore_ass;
	private BigDecimal r65_orig_amt;
	private BigDecimal r65_fore_amt;
	private BigDecimal r65_no_of_acc;

	// R66
	private String     r66_sche_fore_ass;
	private BigDecimal r66_orig_amt;
	private BigDecimal r66_fore_amt;
	private BigDecimal r66_no_of_acc;

	// R67
	private String     r67_sche_fore_ass;
	private BigDecimal r67_orig_amt;
	private BigDecimal r67_fore_amt;
	private BigDecimal r67_no_of_acc;

	
	
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
	
	
	
public String getR10_sche_fore_ass() {
		return r10_sche_fore_ass;
	}
	public void setR10_sche_fore_ass(String r10_sche_fore_ass) {
		this.r10_sche_fore_ass = r10_sche_fore_ass;
	}
	public BigDecimal getR10_orig_amt() {
		return r10_orig_amt;
	}
	public void setR10_orig_amt(BigDecimal r10_orig_amt) {
		this.r10_orig_amt = r10_orig_amt;
	}
	public BigDecimal getR10_fore_amt() {
		return r10_fore_amt;
	}
	public void setR10_fore_amt(BigDecimal r10_fore_amt) {
		this.r10_fore_amt = r10_fore_amt;
	}
	public BigDecimal getR10_no_of_acc() {
		return r10_no_of_acc;
	}
	public void setR10_no_of_acc(BigDecimal r10_no_of_acc) {
		this.r10_no_of_acc = r10_no_of_acc;
	}
	public String getR11_sche_fore_ass() {
		return r11_sche_fore_ass;
	}
	public void setR11_sche_fore_ass(String r11_sche_fore_ass) {
		this.r11_sche_fore_ass = r11_sche_fore_ass;
	}
	public BigDecimal getR11_orig_amt() {
		return r11_orig_amt;
	}
	public void setR11_orig_amt(BigDecimal r11_orig_amt) {
		this.r11_orig_amt = r11_orig_amt;
	}
	public BigDecimal getR11_fore_amt() {
		return r11_fore_amt;
	}
	public void setR11_fore_amt(BigDecimal r11_fore_amt) {
		this.r11_fore_amt = r11_fore_amt;
	}
	public BigDecimal getR11_no_of_acc() {
		return r11_no_of_acc;
	}
	public void setR11_no_of_acc(BigDecimal r11_no_of_acc) {
		this.r11_no_of_acc = r11_no_of_acc;
	}
	public String getR12_sche_fore_ass() {
		return r12_sche_fore_ass;
	}
	public void setR12_sche_fore_ass(String r12_sche_fore_ass) {
		this.r12_sche_fore_ass = r12_sche_fore_ass;
	}
	public BigDecimal getR12_orig_amt() {
		return r12_orig_amt;
	}
	public void setR12_orig_amt(BigDecimal r12_orig_amt) {
		this.r12_orig_amt = r12_orig_amt;
	}
	public BigDecimal getR12_fore_amt() {
		return r12_fore_amt;
	}
	public void setR12_fore_amt(BigDecimal r12_fore_amt) {
		this.r12_fore_amt = r12_fore_amt;
	}
	public BigDecimal getR12_no_of_acc() {
		return r12_no_of_acc;
	}
	public void setR12_no_of_acc(BigDecimal r12_no_of_acc) {
		this.r12_no_of_acc = r12_no_of_acc;
	}
	public String getR13_sche_fore_ass() {
		return r13_sche_fore_ass;
	}
	public void setR13_sche_fore_ass(String r13_sche_fore_ass) {
		this.r13_sche_fore_ass = r13_sche_fore_ass;
	}
	public BigDecimal getR13_orig_amt() {
		return r13_orig_amt;
	}
	public void setR13_orig_amt(BigDecimal r13_orig_amt) {
		this.r13_orig_amt = r13_orig_amt;
	}
	public BigDecimal getR13_fore_amt() {
		return r13_fore_amt;
	}
	public void setR13_fore_amt(BigDecimal r13_fore_amt) {
		this.r13_fore_amt = r13_fore_amt;
	}
	public BigDecimal getR13_no_of_acc() {
		return r13_no_of_acc;
	}
	public void setR13_no_of_acc(BigDecimal r13_no_of_acc) {
		this.r13_no_of_acc = r13_no_of_acc;
	}
	public String getR14_sche_fore_ass() {
		return r14_sche_fore_ass;
	}
	public void setR14_sche_fore_ass(String r14_sche_fore_ass) {
		this.r14_sche_fore_ass = r14_sche_fore_ass;
	}
	public BigDecimal getR14_orig_amt() {
		return r14_orig_amt;
	}
	public void setR14_orig_amt(BigDecimal r14_orig_amt) {
		this.r14_orig_amt = r14_orig_amt;
	}
	public BigDecimal getR14_fore_amt() {
		return r14_fore_amt;
	}
	public void setR14_fore_amt(BigDecimal r14_fore_amt) {
		this.r14_fore_amt = r14_fore_amt;
	}
	public BigDecimal getR14_no_of_acc() {
		return r14_no_of_acc;
	}
	public void setR14_no_of_acc(BigDecimal r14_no_of_acc) {
		this.r14_no_of_acc = r14_no_of_acc;
	}
	public String getR15_sche_fore_ass() {
		return r15_sche_fore_ass;
	}
	public void setR15_sche_fore_ass(String r15_sche_fore_ass) {
		this.r15_sche_fore_ass = r15_sche_fore_ass;
	}
	public BigDecimal getR15_orig_amt() {
		return r15_orig_amt;
	}
	public void setR15_orig_amt(BigDecimal r15_orig_amt) {
		this.r15_orig_amt = r15_orig_amt;
	}
	public BigDecimal getR15_fore_amt() {
		return r15_fore_amt;
	}
	public void setR15_fore_amt(BigDecimal r15_fore_amt) {
		this.r15_fore_amt = r15_fore_amt;
	}
	public BigDecimal getR15_no_of_acc() {
		return r15_no_of_acc;
	}
	public void setR15_no_of_acc(BigDecimal r15_no_of_acc) {
		this.r15_no_of_acc = r15_no_of_acc;
	}
	public String getR16_sche_fore_ass() {
		return r16_sche_fore_ass;
	}
	public void setR16_sche_fore_ass(String r16_sche_fore_ass) {
		this.r16_sche_fore_ass = r16_sche_fore_ass;
	}
	public BigDecimal getR16_orig_amt() {
		return r16_orig_amt;
	}
	public void setR16_orig_amt(BigDecimal r16_orig_amt) {
		this.r16_orig_amt = r16_orig_amt;
	}
	public BigDecimal getR16_fore_amt() {
		return r16_fore_amt;
	}
	public void setR16_fore_amt(BigDecimal r16_fore_amt) {
		this.r16_fore_amt = r16_fore_amt;
	}
	public BigDecimal getR16_no_of_acc() {
		return r16_no_of_acc;
	}
	public void setR16_no_of_acc(BigDecimal r16_no_of_acc) {
		this.r16_no_of_acc = r16_no_of_acc;
	}
	public String getR17_sche_fore_ass() {
		return r17_sche_fore_ass;
	}
	public void setR17_sche_fore_ass(String r17_sche_fore_ass) {
		this.r17_sche_fore_ass = r17_sche_fore_ass;
	}
	public BigDecimal getR17_orig_amt() {
		return r17_orig_amt;
	}
	public void setR17_orig_amt(BigDecimal r17_orig_amt) {
		this.r17_orig_amt = r17_orig_amt;
	}
	public BigDecimal getR17_fore_amt() {
		return r17_fore_amt;
	}
	public void setR17_fore_amt(BigDecimal r17_fore_amt) {
		this.r17_fore_amt = r17_fore_amt;
	}
	public BigDecimal getR17_no_of_acc() {
		return r17_no_of_acc;
	}
	public void setR17_no_of_acc(BigDecimal r17_no_of_acc) {
		this.r17_no_of_acc = r17_no_of_acc;
	}
	public String getR18_sche_fore_ass() {
		return r18_sche_fore_ass;
	}
	public void setR18_sche_fore_ass(String r18_sche_fore_ass) {
		this.r18_sche_fore_ass = r18_sche_fore_ass;
	}
	public BigDecimal getR18_orig_amt() {
		return r18_orig_amt;
	}
	public void setR18_orig_amt(BigDecimal r18_orig_amt) {
		this.r18_orig_amt = r18_orig_amt;
	}
	public BigDecimal getR18_fore_amt() {
		return r18_fore_amt;
	}
	public void setR18_fore_amt(BigDecimal r18_fore_amt) {
		this.r18_fore_amt = r18_fore_amt;
	}
	public BigDecimal getR18_no_of_acc() {
		return r18_no_of_acc;
	}
	public void setR18_no_of_acc(BigDecimal r18_no_of_acc) {
		this.r18_no_of_acc = r18_no_of_acc;
	}
	public String getR19_sche_fore_ass() {
		return r19_sche_fore_ass;
	}
	public void setR19_sche_fore_ass(String r19_sche_fore_ass) {
		this.r19_sche_fore_ass = r19_sche_fore_ass;
	}
	public BigDecimal getR19_orig_amt() {
		return r19_orig_amt;
	}
	public void setR19_orig_amt(BigDecimal r19_orig_amt) {
		this.r19_orig_amt = r19_orig_amt;
	}
	public BigDecimal getR19_fore_amt() {
		return r19_fore_amt;
	}
	public void setR19_fore_amt(BigDecimal r19_fore_amt) {
		this.r19_fore_amt = r19_fore_amt;
	}
	public BigDecimal getR19_no_of_acc() {
		return r19_no_of_acc;
	}
	public void setR19_no_of_acc(BigDecimal r19_no_of_acc) {
		this.r19_no_of_acc = r19_no_of_acc;
	}
	public String getR20_sche_fore_ass() {
		return r20_sche_fore_ass;
	}
	public void setR20_sche_fore_ass(String r20_sche_fore_ass) {
		this.r20_sche_fore_ass = r20_sche_fore_ass;
	}
	public BigDecimal getR20_orig_amt() {
		return r20_orig_amt;
	}
	public void setR20_orig_amt(BigDecimal r20_orig_amt) {
		this.r20_orig_amt = r20_orig_amt;
	}
	public BigDecimal getR20_fore_amt() {
		return r20_fore_amt;
	}
	public void setR20_fore_amt(BigDecimal r20_fore_amt) {
		this.r20_fore_amt = r20_fore_amt;
	}
	public BigDecimal getR20_no_of_acc() {
		return r20_no_of_acc;
	}
	public void setR20_no_of_acc(BigDecimal r20_no_of_acc) {
		this.r20_no_of_acc = r20_no_of_acc;
	}
	public String getR21_sche_fore_ass() {
		return r21_sche_fore_ass;
	}
	public void setR21_sche_fore_ass(String r21_sche_fore_ass) {
		this.r21_sche_fore_ass = r21_sche_fore_ass;
	}
	public BigDecimal getR21_orig_amt() {
		return r21_orig_amt;
	}
	public void setR21_orig_amt(BigDecimal r21_orig_amt) {
		this.r21_orig_amt = r21_orig_amt;
	}
	public BigDecimal getR21_fore_amt() {
		return r21_fore_amt;
	}
	public void setR21_fore_amt(BigDecimal r21_fore_amt) {
		this.r21_fore_amt = r21_fore_amt;
	}
	public BigDecimal getR21_no_of_acc() {
		return r21_no_of_acc;
	}
	public void setR21_no_of_acc(BigDecimal r21_no_of_acc) {
		this.r21_no_of_acc = r21_no_of_acc;
	}
	public String getR22_sche_fore_ass() {
		return r22_sche_fore_ass;
	}
	public void setR22_sche_fore_ass(String r22_sche_fore_ass) {
		this.r22_sche_fore_ass = r22_sche_fore_ass;
	}
	public BigDecimal getR22_orig_amt() {
		return r22_orig_amt;
	}
	public void setR22_orig_amt(BigDecimal r22_orig_amt) {
		this.r22_orig_amt = r22_orig_amt;
	}
	public BigDecimal getR22_fore_amt() {
		return r22_fore_amt;
	}
	public void setR22_fore_amt(BigDecimal r22_fore_amt) {
		this.r22_fore_amt = r22_fore_amt;
	}
	public BigDecimal getR22_no_of_acc() {
		return r22_no_of_acc;
	}
	public void setR22_no_of_acc(BigDecimal r22_no_of_acc) {
		this.r22_no_of_acc = r22_no_of_acc;
	}
	public String getR23_sche_fore_ass() {
		return r23_sche_fore_ass;
	}
	public void setR23_sche_fore_ass(String r23_sche_fore_ass) {
		this.r23_sche_fore_ass = r23_sche_fore_ass;
	}
	public BigDecimal getR23_orig_amt() {
		return r23_orig_amt;
	}
	public void setR23_orig_amt(BigDecimal r23_orig_amt) {
		this.r23_orig_amt = r23_orig_amt;
	}
	public BigDecimal getR23_fore_amt() {
		return r23_fore_amt;
	}
	public void setR23_fore_amt(BigDecimal r23_fore_amt) {
		this.r23_fore_amt = r23_fore_amt;
	}
	public BigDecimal getR23_no_of_acc() {
		return r23_no_of_acc;
	}
	public void setR23_no_of_acc(BigDecimal r23_no_of_acc) {
		this.r23_no_of_acc = r23_no_of_acc;
	}
	public String getR24_sche_fore_ass() {
		return r24_sche_fore_ass;
	}
	public void setR24_sche_fore_ass(String r24_sche_fore_ass) {
		this.r24_sche_fore_ass = r24_sche_fore_ass;
	}
	public BigDecimal getR24_orig_amt() {
		return r24_orig_amt;
	}
	public void setR24_orig_amt(BigDecimal r24_orig_amt) {
		this.r24_orig_amt = r24_orig_amt;
	}
	public BigDecimal getR24_fore_amt() {
		return r24_fore_amt;
	}
	public void setR24_fore_amt(BigDecimal r24_fore_amt) {
		this.r24_fore_amt = r24_fore_amt;
	}
	public BigDecimal getR24_no_of_acc() {
		return r24_no_of_acc;
	}
	public void setR24_no_of_acc(BigDecimal r24_no_of_acc) {
		this.r24_no_of_acc = r24_no_of_acc;
	}
	public String getR25_sche_fore_ass() {
		return r25_sche_fore_ass;
	}
	public void setR25_sche_fore_ass(String r25_sche_fore_ass) {
		this.r25_sche_fore_ass = r25_sche_fore_ass;
	}
	public BigDecimal getR25_orig_amt() {
		return r25_orig_amt;
	}
	public void setR25_orig_amt(BigDecimal r25_orig_amt) {
		this.r25_orig_amt = r25_orig_amt;
	}
	public BigDecimal getR25_fore_amt() {
		return r25_fore_amt;
	}
	public void setR25_fore_amt(BigDecimal r25_fore_amt) {
		this.r25_fore_amt = r25_fore_amt;
	}
	public BigDecimal getR25_no_of_acc() {
		return r25_no_of_acc;
	}
	public void setR25_no_of_acc(BigDecimal r25_no_of_acc) {
		this.r25_no_of_acc = r25_no_of_acc;
	}
	public String getR26_sche_fore_ass() {
		return r26_sche_fore_ass;
	}
	public void setR26_sche_fore_ass(String r26_sche_fore_ass) {
		this.r26_sche_fore_ass = r26_sche_fore_ass;
	}
	public BigDecimal getR26_orig_amt() {
		return r26_orig_amt;
	}
	public void setR26_orig_amt(BigDecimal r26_orig_amt) {
		this.r26_orig_amt = r26_orig_amt;
	}
	public BigDecimal getR26_fore_amt() {
		return r26_fore_amt;
	}
	public void setR26_fore_amt(BigDecimal r26_fore_amt) {
		this.r26_fore_amt = r26_fore_amt;
	}
	public BigDecimal getR26_no_of_acc() {
		return r26_no_of_acc;
	}
	public void setR26_no_of_acc(BigDecimal r26_no_of_acc) {
		this.r26_no_of_acc = r26_no_of_acc;
	}
	public String getR27_sche_fore_ass() {
		return r27_sche_fore_ass;
	}
	public void setR27_sche_fore_ass(String r27_sche_fore_ass) {
		this.r27_sche_fore_ass = r27_sche_fore_ass;
	}
	public BigDecimal getR27_orig_amt() {
		return r27_orig_amt;
	}
	public void setR27_orig_amt(BigDecimal r27_orig_amt) {
		this.r27_orig_amt = r27_orig_amt;
	}
	public BigDecimal getR27_fore_amt() {
		return r27_fore_amt;
	}
	public void setR27_fore_amt(BigDecimal r27_fore_amt) {
		this.r27_fore_amt = r27_fore_amt;
	}
	public BigDecimal getR27_no_of_acc() {
		return r27_no_of_acc;
	}
	public void setR27_no_of_acc(BigDecimal r27_no_of_acc) {
		this.r27_no_of_acc = r27_no_of_acc;
	}
	public String getR28_sche_fore_ass() {
		return r28_sche_fore_ass;
	}
	public void setR28_sche_fore_ass(String r28_sche_fore_ass) {
		this.r28_sche_fore_ass = r28_sche_fore_ass;
	}
	public BigDecimal getR28_orig_amt() {
		return r28_orig_amt;
	}
	public void setR28_orig_amt(BigDecimal r28_orig_amt) {
		this.r28_orig_amt = r28_orig_amt;
	}
	public BigDecimal getR28_fore_amt() {
		return r28_fore_amt;
	}
	public void setR28_fore_amt(BigDecimal r28_fore_amt) {
		this.r28_fore_amt = r28_fore_amt;
	}
	public BigDecimal getR28_no_of_acc() {
		return r28_no_of_acc;
	}
	public void setR28_no_of_acc(BigDecimal r28_no_of_acc) {
		this.r28_no_of_acc = r28_no_of_acc;
	}
	public String getR29_sche_fore_ass() {
		return r29_sche_fore_ass;
	}
	public void setR29_sche_fore_ass(String r29_sche_fore_ass) {
		this.r29_sche_fore_ass = r29_sche_fore_ass;
	}
	public BigDecimal getR29_orig_amt() {
		return r29_orig_amt;
	}
	public void setR29_orig_amt(BigDecimal r29_orig_amt) {
		this.r29_orig_amt = r29_orig_amt;
	}
	public BigDecimal getR29_fore_amt() {
		return r29_fore_amt;
	}
	public void setR29_fore_amt(BigDecimal r29_fore_amt) {
		this.r29_fore_amt = r29_fore_amt;
	}
	public BigDecimal getR29_no_of_acc() {
		return r29_no_of_acc;
	}
	public void setR29_no_of_acc(BigDecimal r29_no_of_acc) {
		this.r29_no_of_acc = r29_no_of_acc;
	}
	public String getR30_sche_fore_ass() {
		return r30_sche_fore_ass;
	}
	public void setR30_sche_fore_ass(String r30_sche_fore_ass) {
		this.r30_sche_fore_ass = r30_sche_fore_ass;
	}
	public BigDecimal getR30_orig_amt() {
		return r30_orig_amt;
	}
	public void setR30_orig_amt(BigDecimal r30_orig_amt) {
		this.r30_orig_amt = r30_orig_amt;
	}
	public BigDecimal getR30_fore_amt() {
		return r30_fore_amt;
	}
	public void setR30_fore_amt(BigDecimal r30_fore_amt) {
		this.r30_fore_amt = r30_fore_amt;
	}
	public BigDecimal getR30_no_of_acc() {
		return r30_no_of_acc;
	}
	public void setR30_no_of_acc(BigDecimal r30_no_of_acc) {
		this.r30_no_of_acc = r30_no_of_acc;
	}
	public String getR31_sche_fore_ass() {
		return r31_sche_fore_ass;
	}
	public void setR31_sche_fore_ass(String r31_sche_fore_ass) {
		this.r31_sche_fore_ass = r31_sche_fore_ass;
	}
	public BigDecimal getR31_orig_amt() {
		return r31_orig_amt;
	}
	public void setR31_orig_amt(BigDecimal r31_orig_amt) {
		this.r31_orig_amt = r31_orig_amt;
	}
	public BigDecimal getR31_fore_amt() {
		return r31_fore_amt;
	}
	public void setR31_fore_amt(BigDecimal r31_fore_amt) {
		this.r31_fore_amt = r31_fore_amt;
	}
	public BigDecimal getR31_no_of_acc() {
		return r31_no_of_acc;
	}
	public void setR31_no_of_acc(BigDecimal r31_no_of_acc) {
		this.r31_no_of_acc = r31_no_of_acc;
	}
	public String getR32_sche_fore_ass() {
		return r32_sche_fore_ass;
	}
	public void setR32_sche_fore_ass(String r32_sche_fore_ass) {
		this.r32_sche_fore_ass = r32_sche_fore_ass;
	}
	public BigDecimal getR32_orig_amt() {
		return r32_orig_amt;
	}
	public void setR32_orig_amt(BigDecimal r32_orig_amt) {
		this.r32_orig_amt = r32_orig_amt;
	}
	public BigDecimal getR32_fore_amt() {
		return r32_fore_amt;
	}
	public void setR32_fore_amt(BigDecimal r32_fore_amt) {
		this.r32_fore_amt = r32_fore_amt;
	}
	public BigDecimal getR32_no_of_acc() {
		return r32_no_of_acc;
	}
	public void setR32_no_of_acc(BigDecimal r32_no_of_acc) {
		this.r32_no_of_acc = r32_no_of_acc;
	}
	public String getR33_sche_fore_ass() {
		return r33_sche_fore_ass;
	}
	public void setR33_sche_fore_ass(String r33_sche_fore_ass) {
		this.r33_sche_fore_ass = r33_sche_fore_ass;
	}
	public BigDecimal getR33_orig_amt() {
		return r33_orig_amt;
	}
	public void setR33_orig_amt(BigDecimal r33_orig_amt) {
		this.r33_orig_amt = r33_orig_amt;
	}
	public BigDecimal getR33_fore_amt() {
		return r33_fore_amt;
	}
	public void setR33_fore_amt(BigDecimal r33_fore_amt) {
		this.r33_fore_amt = r33_fore_amt;
	}
	public BigDecimal getR33_no_of_acc() {
		return r33_no_of_acc;
	}
	public void setR33_no_of_acc(BigDecimal r33_no_of_acc) {
		this.r33_no_of_acc = r33_no_of_acc;
	}
	public String getR34_sche_fore_ass() {
		return r34_sche_fore_ass;
	}
	public void setR34_sche_fore_ass(String r34_sche_fore_ass) {
		this.r34_sche_fore_ass = r34_sche_fore_ass;
	}
	public BigDecimal getR34_orig_amt() {
		return r34_orig_amt;
	}
	public void setR34_orig_amt(BigDecimal r34_orig_amt) {
		this.r34_orig_amt = r34_orig_amt;
	}
	public BigDecimal getR34_fore_amt() {
		return r34_fore_amt;
	}
	public void setR34_fore_amt(BigDecimal r34_fore_amt) {
		this.r34_fore_amt = r34_fore_amt;
	}
	public BigDecimal getR34_no_of_acc() {
		return r34_no_of_acc;
	}
	public void setR34_no_of_acc(BigDecimal r34_no_of_acc) {
		this.r34_no_of_acc = r34_no_of_acc;
	}
	public String getR35_sche_fore_ass() {
		return r35_sche_fore_ass;
	}
	public void setR35_sche_fore_ass(String r35_sche_fore_ass) {
		this.r35_sche_fore_ass = r35_sche_fore_ass;
	}
	public BigDecimal getR35_orig_amt() {
		return r35_orig_amt;
	}
	public void setR35_orig_amt(BigDecimal r35_orig_amt) {
		this.r35_orig_amt = r35_orig_amt;
	}
	public BigDecimal getR35_fore_amt() {
		return r35_fore_amt;
	}
	public void setR35_fore_amt(BigDecimal r35_fore_amt) {
		this.r35_fore_amt = r35_fore_amt;
	}
	public BigDecimal getR35_no_of_acc() {
		return r35_no_of_acc;
	}
	public void setR35_no_of_acc(BigDecimal r35_no_of_acc) {
		this.r35_no_of_acc = r35_no_of_acc;
	}
	public String getR36_sche_fore_ass() {
		return r36_sche_fore_ass;
	}
	public void setR36_sche_fore_ass(String r36_sche_fore_ass) {
		this.r36_sche_fore_ass = r36_sche_fore_ass;
	}
	public BigDecimal getR36_orig_amt() {
		return r36_orig_amt;
	}
	public void setR36_orig_amt(BigDecimal r36_orig_amt) {
		this.r36_orig_amt = r36_orig_amt;
	}
	public BigDecimal getR36_fore_amt() {
		return r36_fore_amt;
	}
	public void setR36_fore_amt(BigDecimal r36_fore_amt) {
		this.r36_fore_amt = r36_fore_amt;
	}
	public BigDecimal getR36_no_of_acc() {
		return r36_no_of_acc;
	}
	public void setR36_no_of_acc(BigDecimal r36_no_of_acc) {
		this.r36_no_of_acc = r36_no_of_acc;
	}
	public String getR37_sche_fore_ass() {
		return r37_sche_fore_ass;
	}
	public void setR37_sche_fore_ass(String r37_sche_fore_ass) {
		this.r37_sche_fore_ass = r37_sche_fore_ass;
	}
	public BigDecimal getR37_orig_amt() {
		return r37_orig_amt;
	}
	public void setR37_orig_amt(BigDecimal r37_orig_amt) {
		this.r37_orig_amt = r37_orig_amt;
	}
	public BigDecimal getR37_fore_amt() {
		return r37_fore_amt;
	}
	public void setR37_fore_amt(BigDecimal r37_fore_amt) {
		this.r37_fore_amt = r37_fore_amt;
	}
	public BigDecimal getR37_no_of_acc() {
		return r37_no_of_acc;
	}
	public void setR37_no_of_acc(BigDecimal r37_no_of_acc) {
		this.r37_no_of_acc = r37_no_of_acc;
	}
	public String getR38_sche_fore_ass() {
		return r38_sche_fore_ass;
	}
	public void setR38_sche_fore_ass(String r38_sche_fore_ass) {
		this.r38_sche_fore_ass = r38_sche_fore_ass;
	}
	public BigDecimal getR38_orig_amt() {
		return r38_orig_amt;
	}
	public void setR38_orig_amt(BigDecimal r38_orig_amt) {
		this.r38_orig_amt = r38_orig_amt;
	}
	public BigDecimal getR38_fore_amt() {
		return r38_fore_amt;
	}
	public void setR38_fore_amt(BigDecimal r38_fore_amt) {
		this.r38_fore_amt = r38_fore_amt;
	}
	public BigDecimal getR38_no_of_acc() {
		return r38_no_of_acc;
	}
	public void setR38_no_of_acc(BigDecimal r38_no_of_acc) {
		this.r38_no_of_acc = r38_no_of_acc;
	}
	public String getR39_sche_fore_ass() {
		return r39_sche_fore_ass;
	}
	public void setR39_sche_fore_ass(String r39_sche_fore_ass) {
		this.r39_sche_fore_ass = r39_sche_fore_ass;
	}
	public BigDecimal getR39_orig_amt() {
		return r39_orig_amt;
	}
	public void setR39_orig_amt(BigDecimal r39_orig_amt) {
		this.r39_orig_amt = r39_orig_amt;
	}
	public BigDecimal getR39_fore_amt() {
		return r39_fore_amt;
	}
	public void setR39_fore_amt(BigDecimal r39_fore_amt) {
		this.r39_fore_amt = r39_fore_amt;
	}
	public BigDecimal getR39_no_of_acc() {
		return r39_no_of_acc;
	}
	public void setR39_no_of_acc(BigDecimal r39_no_of_acc) {
		this.r39_no_of_acc = r39_no_of_acc;
	}
	public String getR40_sche_fore_ass() {
		return r40_sche_fore_ass;
	}
	public void setR40_sche_fore_ass(String r40_sche_fore_ass) {
		this.r40_sche_fore_ass = r40_sche_fore_ass;
	}
	public BigDecimal getR40_orig_amt() {
		return r40_orig_amt;
	}
	public void setR40_orig_amt(BigDecimal r40_orig_amt) {
		this.r40_orig_amt = r40_orig_amt;
	}
	public BigDecimal getR40_fore_amt() {
		return r40_fore_amt;
	}
	public void setR40_fore_amt(BigDecimal r40_fore_amt) {
		this.r40_fore_amt = r40_fore_amt;
	}
	public BigDecimal getR40_no_of_acc() {
		return r40_no_of_acc;
	}
	public void setR40_no_of_acc(BigDecimal r40_no_of_acc) {
		this.r40_no_of_acc = r40_no_of_acc;
	}
	public String getR41_sche_fore_ass() {
		return r41_sche_fore_ass;
	}
	public void setR41_sche_fore_ass(String r41_sche_fore_ass) {
		this.r41_sche_fore_ass = r41_sche_fore_ass;
	}
	public BigDecimal getR41_orig_amt() {
		return r41_orig_amt;
	}
	public void setR41_orig_amt(BigDecimal r41_orig_amt) {
		this.r41_orig_amt = r41_orig_amt;
	}
	public BigDecimal getR41_fore_amt() {
		return r41_fore_amt;
	}
	public void setR41_fore_amt(BigDecimal r41_fore_amt) {
		this.r41_fore_amt = r41_fore_amt;
	}
	public BigDecimal getR41_no_of_acc() {
		return r41_no_of_acc;
	}
	public void setR41_no_of_acc(BigDecimal r41_no_of_acc) {
		this.r41_no_of_acc = r41_no_of_acc;
	}
	public String getR42_sche_fore_ass() {
		return r42_sche_fore_ass;
	}
	public void setR42_sche_fore_ass(String r42_sche_fore_ass) {
		this.r42_sche_fore_ass = r42_sche_fore_ass;
	}
	public BigDecimal getR42_orig_amt() {
		return r42_orig_amt;
	}
	public void setR42_orig_amt(BigDecimal r42_orig_amt) {
		this.r42_orig_amt = r42_orig_amt;
	}
	public BigDecimal getR42_fore_amt() {
		return r42_fore_amt;
	}
	public void setR42_fore_amt(BigDecimal r42_fore_amt) {
		this.r42_fore_amt = r42_fore_amt;
	}
	public BigDecimal getR42_no_of_acc() {
		return r42_no_of_acc;
	}
	public void setR42_no_of_acc(BigDecimal r42_no_of_acc) {
		this.r42_no_of_acc = r42_no_of_acc;
	}
	public String getR43_sche_fore_ass() {
		return r43_sche_fore_ass;
	}
	public void setR43_sche_fore_ass(String r43_sche_fore_ass) {
		this.r43_sche_fore_ass = r43_sche_fore_ass;
	}
	public BigDecimal getR43_orig_amt() {
		return r43_orig_amt;
	}
	public void setR43_orig_amt(BigDecimal r43_orig_amt) {
		this.r43_orig_amt = r43_orig_amt;
	}
	public BigDecimal getR43_fore_amt() {
		return r43_fore_amt;
	}
	public void setR43_fore_amt(BigDecimal r43_fore_amt) {
		this.r43_fore_amt = r43_fore_amt;
	}
	public BigDecimal getR43_no_of_acc() {
		return r43_no_of_acc;
	}
	public void setR43_no_of_acc(BigDecimal r43_no_of_acc) {
		this.r43_no_of_acc = r43_no_of_acc;
	}
	public String getR44_sche_fore_ass() {
		return r44_sche_fore_ass;
	}
	public void setR44_sche_fore_ass(String r44_sche_fore_ass) {
		this.r44_sche_fore_ass = r44_sche_fore_ass;
	}
	public BigDecimal getR44_orig_amt() {
		return r44_orig_amt;
	}
	public void setR44_orig_amt(BigDecimal r44_orig_amt) {
		this.r44_orig_amt = r44_orig_amt;
	}
	public BigDecimal getR44_fore_amt() {
		return r44_fore_amt;
	}
	public void setR44_fore_amt(BigDecimal r44_fore_amt) {
		this.r44_fore_amt = r44_fore_amt;
	}
	public BigDecimal getR44_no_of_acc() {
		return r44_no_of_acc;
	}
	public void setR44_no_of_acc(BigDecimal r44_no_of_acc) {
		this.r44_no_of_acc = r44_no_of_acc;
	}
	public String getR45_sche_fore_ass() {
		return r45_sche_fore_ass;
	}
	public void setR45_sche_fore_ass(String r45_sche_fore_ass) {
		this.r45_sche_fore_ass = r45_sche_fore_ass;
	}
	public BigDecimal getR45_orig_amt() {
		return r45_orig_amt;
	}
	public void setR45_orig_amt(BigDecimal r45_orig_amt) {
		this.r45_orig_amt = r45_orig_amt;
	}
	public BigDecimal getR45_fore_amt() {
		return r45_fore_amt;
	}
	public void setR45_fore_amt(BigDecimal r45_fore_amt) {
		this.r45_fore_amt = r45_fore_amt;
	}
	public BigDecimal getR45_no_of_acc() {
		return r45_no_of_acc;
	}
	public void setR45_no_of_acc(BigDecimal r45_no_of_acc) {
		this.r45_no_of_acc = r45_no_of_acc;
	}
	public String getR46_sche_fore_ass() {
		return r46_sche_fore_ass;
	}
	public void setR46_sche_fore_ass(String r46_sche_fore_ass) {
		this.r46_sche_fore_ass = r46_sche_fore_ass;
	}
	public BigDecimal getR46_orig_amt() {
		return r46_orig_amt;
	}
	public void setR46_orig_amt(BigDecimal r46_orig_amt) {
		this.r46_orig_amt = r46_orig_amt;
	}
	public BigDecimal getR46_fore_amt() {
		return r46_fore_amt;
	}
	public void setR46_fore_amt(BigDecimal r46_fore_amt) {
		this.r46_fore_amt = r46_fore_amt;
	}
	public BigDecimal getR46_no_of_acc() {
		return r46_no_of_acc;
	}
	public void setR46_no_of_acc(BigDecimal r46_no_of_acc) {
		this.r46_no_of_acc = r46_no_of_acc;
	}
	public String getR47_sche_fore_ass() {
		return r47_sche_fore_ass;
	}
	public void setR47_sche_fore_ass(String r47_sche_fore_ass) {
		this.r47_sche_fore_ass = r47_sche_fore_ass;
	}
	public BigDecimal getR47_orig_amt() {
		return r47_orig_amt;
	}
	public void setR47_orig_amt(BigDecimal r47_orig_amt) {
		this.r47_orig_amt = r47_orig_amt;
	}
	public BigDecimal getR47_fore_amt() {
		return r47_fore_amt;
	}
	public void setR47_fore_amt(BigDecimal r47_fore_amt) {
		this.r47_fore_amt = r47_fore_amt;
	}
	public BigDecimal getR47_no_of_acc() {
		return r47_no_of_acc;
	}
	public void setR47_no_of_acc(BigDecimal r47_no_of_acc) {
		this.r47_no_of_acc = r47_no_of_acc;
	}
	public String getR48_sche_fore_ass() {
		return r48_sche_fore_ass;
	}
	public void setR48_sche_fore_ass(String r48_sche_fore_ass) {
		this.r48_sche_fore_ass = r48_sche_fore_ass;
	}
	public BigDecimal getR48_orig_amt() {
		return r48_orig_amt;
	}
	public void setR48_orig_amt(BigDecimal r48_orig_amt) {
		this.r48_orig_amt = r48_orig_amt;
	}
	public BigDecimal getR48_fore_amt() {
		return r48_fore_amt;
	}
	public void setR48_fore_amt(BigDecimal r48_fore_amt) {
		this.r48_fore_amt = r48_fore_amt;
	}
	public BigDecimal getR48_no_of_acc() {
		return r48_no_of_acc;
	}
	public void setR48_no_of_acc(BigDecimal r48_no_of_acc) {
		this.r48_no_of_acc = r48_no_of_acc;
	}
	public String getR49_sche_fore_ass() {
		return r49_sche_fore_ass;
	}
	public void setR49_sche_fore_ass(String r49_sche_fore_ass) {
		this.r49_sche_fore_ass = r49_sche_fore_ass;
	}
	public BigDecimal getR49_orig_amt() {
		return r49_orig_amt;
	}
	public void setR49_orig_amt(BigDecimal r49_orig_amt) {
		this.r49_orig_amt = r49_orig_amt;
	}
	public BigDecimal getR49_fore_amt() {
		return r49_fore_amt;
	}
	public void setR49_fore_amt(BigDecimal r49_fore_amt) {
		this.r49_fore_amt = r49_fore_amt;
	}
	public BigDecimal getR49_no_of_acc() {
		return r49_no_of_acc;
	}
	public void setR49_no_of_acc(BigDecimal r49_no_of_acc) {
		this.r49_no_of_acc = r49_no_of_acc;
	}
	public String getR50_sche_fore_ass() {
		return r50_sche_fore_ass;
	}
	public void setR50_sche_fore_ass(String r50_sche_fore_ass) {
		this.r50_sche_fore_ass = r50_sche_fore_ass;
	}
	public BigDecimal getR50_orig_amt() {
		return r50_orig_amt;
	}
	public void setR50_orig_amt(BigDecimal r50_orig_amt) {
		this.r50_orig_amt = r50_orig_amt;
	}
	public BigDecimal getR50_fore_amt() {
		return r50_fore_amt;
	}
	public void setR50_fore_amt(BigDecimal r50_fore_amt) {
		this.r50_fore_amt = r50_fore_amt;
	}
	public BigDecimal getR50_no_of_acc() {
		return r50_no_of_acc;
	}
	public void setR50_no_of_acc(BigDecimal r50_no_of_acc) {
		this.r50_no_of_acc = r50_no_of_acc;
	}
	public String getR51_sche_fore_ass() {
		return r51_sche_fore_ass;
	}
	public void setR51_sche_fore_ass(String r51_sche_fore_ass) {
		this.r51_sche_fore_ass = r51_sche_fore_ass;
	}
	public BigDecimal getR51_orig_amt() {
		return r51_orig_amt;
	}
	public void setR51_orig_amt(BigDecimal r51_orig_amt) {
		this.r51_orig_amt = r51_orig_amt;
	}
	public BigDecimal getR51_fore_amt() {
		return r51_fore_amt;
	}
	public void setR51_fore_amt(BigDecimal r51_fore_amt) {
		this.r51_fore_amt = r51_fore_amt;
	}
	public BigDecimal getR51_no_of_acc() {
		return r51_no_of_acc;
	}
	public void setR51_no_of_acc(BigDecimal r51_no_of_acc) {
		this.r51_no_of_acc = r51_no_of_acc;
	}
	public String getR52_sche_fore_ass() {
		return r52_sche_fore_ass;
	}
	public void setR52_sche_fore_ass(String r52_sche_fore_ass) {
		this.r52_sche_fore_ass = r52_sche_fore_ass;
	}
	public BigDecimal getR52_orig_amt() {
		return r52_orig_amt;
	}
	public void setR52_orig_amt(BigDecimal r52_orig_amt) {
		this.r52_orig_amt = r52_orig_amt;
	}
	public BigDecimal getR52_fore_amt() {
		return r52_fore_amt;
	}
	public void setR52_fore_amt(BigDecimal r52_fore_amt) {
		this.r52_fore_amt = r52_fore_amt;
	}
	public BigDecimal getR52_no_of_acc() {
		return r52_no_of_acc;
	}
	public void setR52_no_of_acc(BigDecimal r52_no_of_acc) {
		this.r52_no_of_acc = r52_no_of_acc;
	}
	public String getR53_sche_fore_ass() {
		return r53_sche_fore_ass;
	}
	public void setR53_sche_fore_ass(String r53_sche_fore_ass) {
		this.r53_sche_fore_ass = r53_sche_fore_ass;
	}
	public BigDecimal getR53_orig_amt() {
		return r53_orig_amt;
	}
	public void setR53_orig_amt(BigDecimal r53_orig_amt) {
		this.r53_orig_amt = r53_orig_amt;
	}
	public BigDecimal getR53_fore_amt() {
		return r53_fore_amt;
	}
	public void setR53_fore_amt(BigDecimal r53_fore_amt) {
		this.r53_fore_amt = r53_fore_amt;
	}
	public BigDecimal getR53_no_of_acc() {
		return r53_no_of_acc;
	}
	public void setR53_no_of_acc(BigDecimal r53_no_of_acc) {
		this.r53_no_of_acc = r53_no_of_acc;
	}
	public String getR54_sche_fore_ass() {
		return r54_sche_fore_ass;
	}
	public void setR54_sche_fore_ass(String r54_sche_fore_ass) {
		this.r54_sche_fore_ass = r54_sche_fore_ass;
	}
	public BigDecimal getR54_orig_amt() {
		return r54_orig_amt;
	}
	public void setR54_orig_amt(BigDecimal r54_orig_amt) {
		this.r54_orig_amt = r54_orig_amt;
	}
	public BigDecimal getR54_fore_amt() {
		return r54_fore_amt;
	}
	public void setR54_fore_amt(BigDecimal r54_fore_amt) {
		this.r54_fore_amt = r54_fore_amt;
	}
	public BigDecimal getR54_no_of_acc() {
		return r54_no_of_acc;
	}
	public void setR54_no_of_acc(BigDecimal r54_no_of_acc) {
		this.r54_no_of_acc = r54_no_of_acc;
	}
	public String getR55_sche_fore_ass() {
		return r55_sche_fore_ass;
	}
	public void setR55_sche_fore_ass(String r55_sche_fore_ass) {
		this.r55_sche_fore_ass = r55_sche_fore_ass;
	}
	public BigDecimal getR55_orig_amt() {
		return r55_orig_amt;
	}
	public void setR55_orig_amt(BigDecimal r55_orig_amt) {
		this.r55_orig_amt = r55_orig_amt;
	}
	public BigDecimal getR55_fore_amt() {
		return r55_fore_amt;
	}
	public void setR55_fore_amt(BigDecimal r55_fore_amt) {
		this.r55_fore_amt = r55_fore_amt;
	}
	public BigDecimal getR55_no_of_acc() {
		return r55_no_of_acc;
	}
	public void setR55_no_of_acc(BigDecimal r55_no_of_acc) {
		this.r55_no_of_acc = r55_no_of_acc;
	}
	public String getR56_sche_fore_ass() {
		return r56_sche_fore_ass;
	}
	public void setR56_sche_fore_ass(String r56_sche_fore_ass) {
		this.r56_sche_fore_ass = r56_sche_fore_ass;
	}
	public BigDecimal getR56_orig_amt() {
		return r56_orig_amt;
	}
	public void setR56_orig_amt(BigDecimal r56_orig_amt) {
		this.r56_orig_amt = r56_orig_amt;
	}
	public BigDecimal getR56_fore_amt() {
		return r56_fore_amt;
	}
	public void setR56_fore_amt(BigDecimal r56_fore_amt) {
		this.r56_fore_amt = r56_fore_amt;
	}
	public BigDecimal getR56_no_of_acc() {
		return r56_no_of_acc;
	}
	public void setR56_no_of_acc(BigDecimal r56_no_of_acc) {
		this.r56_no_of_acc = r56_no_of_acc;
	}
	public String getR57_sche_fore_ass() {
		return r57_sche_fore_ass;
	}
	public void setR57_sche_fore_ass(String r57_sche_fore_ass) {
		this.r57_sche_fore_ass = r57_sche_fore_ass;
	}
	public BigDecimal getR57_orig_amt() {
		return r57_orig_amt;
	}
	public void setR57_orig_amt(BigDecimal r57_orig_amt) {
		this.r57_orig_amt = r57_orig_amt;
	}
	public BigDecimal getR57_fore_amt() {
		return r57_fore_amt;
	}
	public void setR57_fore_amt(BigDecimal r57_fore_amt) {
		this.r57_fore_amt = r57_fore_amt;
	}
	public BigDecimal getR57_no_of_acc() {
		return r57_no_of_acc;
	}
	public void setR57_no_of_acc(BigDecimal r57_no_of_acc) {
		this.r57_no_of_acc = r57_no_of_acc;
	}
	public String getR58_sche_fore_ass() {
		return r58_sche_fore_ass;
	}
	public void setR58_sche_fore_ass(String r58_sche_fore_ass) {
		this.r58_sche_fore_ass = r58_sche_fore_ass;
	}
	public BigDecimal getR58_orig_amt() {
		return r58_orig_amt;
	}
	public void setR58_orig_amt(BigDecimal r58_orig_amt) {
		this.r58_orig_amt = r58_orig_amt;
	}
	public BigDecimal getR58_fore_amt() {
		return r58_fore_amt;
	}
	public void setR58_fore_amt(BigDecimal r58_fore_amt) {
		this.r58_fore_amt = r58_fore_amt;
	}
	public BigDecimal getR58_no_of_acc() {
		return r58_no_of_acc;
	}
	public void setR58_no_of_acc(BigDecimal r58_no_of_acc) {
		this.r58_no_of_acc = r58_no_of_acc;
	}
	public String getR59_sche_fore_ass() {
		return r59_sche_fore_ass;
	}
	public void setR59_sche_fore_ass(String r59_sche_fore_ass) {
		this.r59_sche_fore_ass = r59_sche_fore_ass;
	}
	public BigDecimal getR59_orig_amt() {
		return r59_orig_amt;
	}
	public void setR59_orig_amt(BigDecimal r59_orig_amt) {
		this.r59_orig_amt = r59_orig_amt;
	}
	public BigDecimal getR59_fore_amt() {
		return r59_fore_amt;
	}
	public void setR59_fore_amt(BigDecimal r59_fore_amt) {
		this.r59_fore_amt = r59_fore_amt;
	}
	public BigDecimal getR59_no_of_acc() {
		return r59_no_of_acc;
	}
	public void setR59_no_of_acc(BigDecimal r59_no_of_acc) {
		this.r59_no_of_acc = r59_no_of_acc;
	}
	public String getR60_sche_fore_ass() {
		return r60_sche_fore_ass;
	}
	public void setR60_sche_fore_ass(String r60_sche_fore_ass) {
		this.r60_sche_fore_ass = r60_sche_fore_ass;
	}
	public BigDecimal getR60_orig_amt() {
		return r60_orig_amt;
	}
	public void setR60_orig_amt(BigDecimal r60_orig_amt) {
		this.r60_orig_amt = r60_orig_amt;
	}
	public BigDecimal getR60_fore_amt() {
		return r60_fore_amt;
	}
	public void setR60_fore_amt(BigDecimal r60_fore_amt) {
		this.r60_fore_amt = r60_fore_amt;
	}
	public BigDecimal getR60_no_of_acc() {
		return r60_no_of_acc;
	}
	public void setR60_no_of_acc(BigDecimal r60_no_of_acc) {
		this.r60_no_of_acc = r60_no_of_acc;
	}
	public String getR61_sche_fore_ass() {
		return r61_sche_fore_ass;
	}
	public void setR61_sche_fore_ass(String r61_sche_fore_ass) {
		this.r61_sche_fore_ass = r61_sche_fore_ass;
	}
	public BigDecimal getR61_orig_amt() {
		return r61_orig_amt;
	}
	public void setR61_orig_amt(BigDecimal r61_orig_amt) {
		this.r61_orig_amt = r61_orig_amt;
	}
	public BigDecimal getR61_fore_amt() {
		return r61_fore_amt;
	}
	public void setR61_fore_amt(BigDecimal r61_fore_amt) {
		this.r61_fore_amt = r61_fore_amt;
	}
	public BigDecimal getR61_no_of_acc() {
		return r61_no_of_acc;
	}
	public void setR61_no_of_acc(BigDecimal r61_no_of_acc) {
		this.r61_no_of_acc = r61_no_of_acc;
	}
	public String getR62_sche_fore_ass() {
		return r62_sche_fore_ass;
	}
	public void setR62_sche_fore_ass(String r62_sche_fore_ass) {
		this.r62_sche_fore_ass = r62_sche_fore_ass;
	}
	public BigDecimal getR62_orig_amt() {
		return r62_orig_amt;
	}
	public void setR62_orig_amt(BigDecimal r62_orig_amt) {
		this.r62_orig_amt = r62_orig_amt;
	}
	public BigDecimal getR62_fore_amt() {
		return r62_fore_amt;
	}
	public void setR62_fore_amt(BigDecimal r62_fore_amt) {
		this.r62_fore_amt = r62_fore_amt;
	}
	public BigDecimal getR62_no_of_acc() {
		return r62_no_of_acc;
	}
	public void setR62_no_of_acc(BigDecimal r62_no_of_acc) {
		this.r62_no_of_acc = r62_no_of_acc;
	}
	public String getR63_sche_fore_ass() {
		return r63_sche_fore_ass;
	}
	public void setR63_sche_fore_ass(String r63_sche_fore_ass) {
		this.r63_sche_fore_ass = r63_sche_fore_ass;
	}
	public BigDecimal getR63_orig_amt() {
		return r63_orig_amt;
	}
	public void setR63_orig_amt(BigDecimal r63_orig_amt) {
		this.r63_orig_amt = r63_orig_amt;
	}
	public BigDecimal getR63_fore_amt() {
		return r63_fore_amt;
	}
	public void setR63_fore_amt(BigDecimal r63_fore_amt) {
		this.r63_fore_amt = r63_fore_amt;
	}
	public BigDecimal getR63_no_of_acc() {
		return r63_no_of_acc;
	}
	public void setR63_no_of_acc(BigDecimal r63_no_of_acc) {
		this.r63_no_of_acc = r63_no_of_acc;
	}
	public String getR64_sche_fore_ass() {
		return r64_sche_fore_ass;
	}
	public void setR64_sche_fore_ass(String r64_sche_fore_ass) {
		this.r64_sche_fore_ass = r64_sche_fore_ass;
	}
	public BigDecimal getR64_orig_amt() {
		return r64_orig_amt;
	}
	public void setR64_orig_amt(BigDecimal r64_orig_amt) {
		this.r64_orig_amt = r64_orig_amt;
	}
	public BigDecimal getR64_fore_amt() {
		return r64_fore_amt;
	}
	public void setR64_fore_amt(BigDecimal r64_fore_amt) {
		this.r64_fore_amt = r64_fore_amt;
	}
	public BigDecimal getR64_no_of_acc() {
		return r64_no_of_acc;
	}
	public void setR64_no_of_acc(BigDecimal r64_no_of_acc) {
		this.r64_no_of_acc = r64_no_of_acc;
	}
	public String getR65_sche_fore_ass() {
		return r65_sche_fore_ass;
	}
	public void setR65_sche_fore_ass(String r65_sche_fore_ass) {
		this.r65_sche_fore_ass = r65_sche_fore_ass;
	}
	public BigDecimal getR65_orig_amt() {
		return r65_orig_amt;
	}
	public void setR65_orig_amt(BigDecimal r65_orig_amt) {
		this.r65_orig_amt = r65_orig_amt;
	}
	public BigDecimal getR65_fore_amt() {
		return r65_fore_amt;
	}
	public void setR65_fore_amt(BigDecimal r65_fore_amt) {
		this.r65_fore_amt = r65_fore_amt;
	}
	public BigDecimal getR65_no_of_acc() {
		return r65_no_of_acc;
	}
	public void setR65_no_of_acc(BigDecimal r65_no_of_acc) {
		this.r65_no_of_acc = r65_no_of_acc;
	}
	public String getR66_sche_fore_ass() {
		return r66_sche_fore_ass;
	}
	public void setR66_sche_fore_ass(String r66_sche_fore_ass) {
		this.r66_sche_fore_ass = r66_sche_fore_ass;
	}
	public BigDecimal getR66_orig_amt() {
		return r66_orig_amt;
	}
	public void setR66_orig_amt(BigDecimal r66_orig_amt) {
		this.r66_orig_amt = r66_orig_amt;
	}
	public BigDecimal getR66_fore_amt() {
		return r66_fore_amt;
	}
	public void setR66_fore_amt(BigDecimal r66_fore_amt) {
		this.r66_fore_amt = r66_fore_amt;
	}
	public BigDecimal getR66_no_of_acc() {
		return r66_no_of_acc;
	}
	public void setR66_no_of_acc(BigDecimal r66_no_of_acc) {
		this.r66_no_of_acc = r66_no_of_acc;
	}
	public String getR67_sche_fore_ass() {
		return r67_sche_fore_ass;
	}
	public void setR67_sche_fore_ass(String r67_sche_fore_ass) {
		this.r67_sche_fore_ass = r67_sche_fore_ass;
	}
	public BigDecimal getR67_orig_amt() {
		return r67_orig_amt;
	}
	public void setR67_orig_amt(BigDecimal r67_orig_amt) {
		this.r67_orig_amt = r67_orig_amt;
	}
	public BigDecimal getR67_fore_amt() {
		return r67_fore_amt;
	}
	public void setR67_fore_amt(BigDecimal r67_fore_amt) {
		this.r67_fore_amt = r67_fore_amt;
	}
	public BigDecimal getR67_no_of_acc() {
		return r67_no_of_acc;
	}
	public void setR67_no_of_acc(BigDecimal r67_no_of_acc) {
		this.r67_no_of_acc = r67_no_of_acc;
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
// ARCHIVAL  DETAIL ENTITY 
// =====================================================


public class Q_RLFA2_Archival_Detail_RowMapper 
        implements RowMapper<Q_RLFA2_Archival_Detail_Entity> {

    @Override
    public Q_RLFA2_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        Q_RLFA2_Archival_Detail_Entity obj = new Q_RLFA2_Archival_Detail_Entity();

// =========================
// R10
// =========================
obj.setR10_sche_fore_ass(rs.getString("r10_sche_fore_ass"));
obj.setR10_orig_amt(rs.getBigDecimal("r10_orig_amt"));
obj.setR10_fore_amt(rs.getBigDecimal("r10_fore_amt"));
obj.setR10_no_of_acc(rs.getBigDecimal("r10_no_of_acc"));


// =========================
// R11
// =========================
obj.setR11_sche_fore_ass(rs.getString("r11_sche_fore_ass"));
obj.setR11_orig_amt(rs.getBigDecimal("r11_orig_amt"));
obj.setR11_fore_amt(rs.getBigDecimal("r11_fore_amt"));
obj.setR11_no_of_acc(rs.getBigDecimal("r11_no_of_acc"));

// =========================
// R12
// =========================
obj.setR12_sche_fore_ass(rs.getString("r12_sche_fore_ass"));
obj.setR12_orig_amt(rs.getBigDecimal("r12_orig_amt"));
obj.setR12_fore_amt(rs.getBigDecimal("r12_fore_amt"));
obj.setR12_no_of_acc(rs.getBigDecimal("r12_no_of_acc"));

// =========================
// R13
// =========================
obj.setR13_sche_fore_ass(rs.getString("r13_sche_fore_ass"));
obj.setR13_orig_amt(rs.getBigDecimal("r13_orig_amt"));
obj.setR13_fore_amt(rs.getBigDecimal("r13_fore_amt"));
obj.setR13_no_of_acc(rs.getBigDecimal("r13_no_of_acc"));

// =========================
// R14
// =========================
obj.setR14_sche_fore_ass(rs.getString("r14_sche_fore_ass"));
obj.setR14_orig_amt(rs.getBigDecimal("r14_orig_amt"));
obj.setR14_fore_amt(rs.getBigDecimal("r14_fore_amt"));
obj.setR14_no_of_acc(rs.getBigDecimal("r14_no_of_acc"));

// =========================
// R15
// =========================
obj.setR15_sche_fore_ass(rs.getString("r15_sche_fore_ass"));
obj.setR15_orig_amt(rs.getBigDecimal("r15_orig_amt"));
obj.setR15_fore_amt(rs.getBigDecimal("r15_fore_amt"));
obj.setR15_no_of_acc(rs.getBigDecimal("r15_no_of_acc"));

// =========================
// R16
// =========================
obj.setR16_sche_fore_ass(rs.getString("r16_sche_fore_ass"));
obj.setR16_orig_amt(rs.getBigDecimal("r16_orig_amt"));
obj.setR16_fore_amt(rs.getBigDecimal("r16_fore_amt"));
obj.setR16_no_of_acc(rs.getBigDecimal("r16_no_of_acc"));

// =========================
// R17
// =========================
obj.setR17_sche_fore_ass(rs.getString("r17_sche_fore_ass"));
obj.setR17_orig_amt(rs.getBigDecimal("r17_orig_amt"));
obj.setR17_fore_amt(rs.getBigDecimal("r17_fore_amt"));
obj.setR17_no_of_acc(rs.getBigDecimal("r17_no_of_acc"));

// =========================
// R18
// =========================
obj.setR18_sche_fore_ass(rs.getString("r18_sche_fore_ass"));
obj.setR18_orig_amt(rs.getBigDecimal("r18_orig_amt"));
obj.setR18_fore_amt(rs.getBigDecimal("r18_fore_amt"));
obj.setR18_no_of_acc(rs.getBigDecimal("r18_no_of_acc"));

// =========================
// R19
// =========================
obj.setR19_sche_fore_ass(rs.getString("r19_sche_fore_ass"));
obj.setR19_orig_amt(rs.getBigDecimal("r19_orig_amt"));
obj.setR19_fore_amt(rs.getBigDecimal("r19_fore_amt"));
obj.setR19_no_of_acc(rs.getBigDecimal("r19_no_of_acc"));

// =========================
// R20
// =========================
obj.setR20_sche_fore_ass(rs.getString("r20_sche_fore_ass"));
obj.setR20_orig_amt(rs.getBigDecimal("r20_orig_amt"));
obj.setR20_fore_amt(rs.getBigDecimal("r20_fore_amt"));
obj.setR20_no_of_acc(rs.getBigDecimal("r20_no_of_acc"));

// =========================
// R21
// =========================
obj.setR21_sche_fore_ass(rs.getString("r21_sche_fore_ass"));
obj.setR21_orig_amt(rs.getBigDecimal("r21_orig_amt"));
obj.setR21_fore_amt(rs.getBigDecimal("r21_fore_amt"));
obj.setR21_no_of_acc(rs.getBigDecimal("r21_no_of_acc"));

// =========================
// R22
// =========================
obj.setR22_sche_fore_ass(rs.getString("r22_sche_fore_ass"));
obj.setR22_orig_amt(rs.getBigDecimal("r22_orig_amt"));
obj.setR22_fore_amt(rs.getBigDecimal("r22_fore_amt"));
obj.setR22_no_of_acc(rs.getBigDecimal("r22_no_of_acc"));

// =========================
// R23
// =========================
obj.setR23_sche_fore_ass(rs.getString("r23_sche_fore_ass"));
obj.setR23_orig_amt(rs.getBigDecimal("r23_orig_amt"));
obj.setR23_fore_amt(rs.getBigDecimal("r23_fore_amt"));
obj.setR23_no_of_acc(rs.getBigDecimal("r23_no_of_acc"));

// =========================
// R24
// =========================
obj.setR24_sche_fore_ass(rs.getString("r24_sche_fore_ass"));
obj.setR24_orig_amt(rs.getBigDecimal("r24_orig_amt"));
obj.setR24_fore_amt(rs.getBigDecimal("r24_fore_amt"));
obj.setR24_no_of_acc(rs.getBigDecimal("r24_no_of_acc"));

// =========================
// R25
// =========================
obj.setR25_sche_fore_ass(rs.getString("r25_sche_fore_ass"));
obj.setR25_orig_amt(rs.getBigDecimal("r25_orig_amt"));
obj.setR25_fore_amt(rs.getBigDecimal("r25_fore_amt"));
obj.setR25_no_of_acc(rs.getBigDecimal("r25_no_of_acc"));

// =========================
// R26
// =========================
obj.setR26_sche_fore_ass(rs.getString("r26_sche_fore_ass"));
obj.setR26_orig_amt(rs.getBigDecimal("r26_orig_amt"));
obj.setR26_fore_amt(rs.getBigDecimal("r26_fore_amt"));
obj.setR26_no_of_acc(rs.getBigDecimal("r26_no_of_acc"));

// =========================
// R27
// =========================
obj.setR27_sche_fore_ass(rs.getString("r27_sche_fore_ass"));
obj.setR27_orig_amt(rs.getBigDecimal("r27_orig_amt"));
obj.setR27_fore_amt(rs.getBigDecimal("r27_fore_amt"));
obj.setR27_no_of_acc(rs.getBigDecimal("r27_no_of_acc"));

// =========================
// R28
// =========================
obj.setR28_sche_fore_ass(rs.getString("r28_sche_fore_ass"));
obj.setR28_orig_amt(rs.getBigDecimal("r28_orig_amt"));
obj.setR28_fore_amt(rs.getBigDecimal("r28_fore_amt"));
obj.setR28_no_of_acc(rs.getBigDecimal("r28_no_of_acc"));

// =========================
// R29
// =========================
obj.setR29_sche_fore_ass(rs.getString("r29_sche_fore_ass"));
obj.setR29_orig_amt(rs.getBigDecimal("r29_orig_amt"));
obj.setR29_fore_amt(rs.getBigDecimal("r29_fore_amt"));
obj.setR29_no_of_acc(rs.getBigDecimal("r29_no_of_acc"));

// =========================
// R30
// =========================
obj.setR30_sche_fore_ass(rs.getString("r30_sche_fore_ass"));
obj.setR30_orig_amt(rs.getBigDecimal("r30_orig_amt"));
obj.setR30_fore_amt(rs.getBigDecimal("r30_fore_amt"));
obj.setR30_no_of_acc(rs.getBigDecimal("r30_no_of_acc"));


// =========================
// R31
// =========================
obj.setR31_sche_fore_ass(rs.getString("r31_sche_fore_ass"));
obj.setR31_orig_amt(rs.getBigDecimal("r31_orig_amt"));
obj.setR31_fore_amt(rs.getBigDecimal("r31_fore_amt"));
obj.setR31_no_of_acc(rs.getBigDecimal("r31_no_of_acc"));

// =========================
// R32
// =========================
obj.setR32_sche_fore_ass(rs.getString("r32_sche_fore_ass"));
obj.setR32_orig_amt(rs.getBigDecimal("r32_orig_amt"));
obj.setR32_fore_amt(rs.getBigDecimal("r32_fore_amt"));
obj.setR32_no_of_acc(rs.getBigDecimal("r32_no_of_acc"));

// =========================
// R33
// =========================
obj.setR33_sche_fore_ass(rs.getString("r33_sche_fore_ass"));
obj.setR33_orig_amt(rs.getBigDecimal("r33_orig_amt"));
obj.setR33_fore_amt(rs.getBigDecimal("r33_fore_amt"));
obj.setR33_no_of_acc(rs.getBigDecimal("r33_no_of_acc"));

// =========================
// R34
// =========================
obj.setR34_sche_fore_ass(rs.getString("r34_sche_fore_ass"));
obj.setR34_orig_amt(rs.getBigDecimal("r34_orig_amt"));
obj.setR34_fore_amt(rs.getBigDecimal("r34_fore_amt"));
obj.setR34_no_of_acc(rs.getBigDecimal("r34_no_of_acc"));

// =========================
// R35
// =========================
obj.setR35_sche_fore_ass(rs.getString("r35_sche_fore_ass"));
obj.setR35_orig_amt(rs.getBigDecimal("r35_orig_amt"));
obj.setR35_fore_amt(rs.getBigDecimal("r35_fore_amt"));
obj.setR35_no_of_acc(rs.getBigDecimal("r35_no_of_acc"));

// =========================
// R36
// =========================
obj.setR36_sche_fore_ass(rs.getString("r36_sche_fore_ass"));
obj.setR36_orig_amt(rs.getBigDecimal("r36_orig_amt"));
obj.setR36_fore_amt(rs.getBigDecimal("r36_fore_amt"));
obj.setR36_no_of_acc(rs.getBigDecimal("r36_no_of_acc"));

// =========================
// R37
// =========================
obj.setR37_sche_fore_ass(rs.getString("r37_sche_fore_ass"));
obj.setR37_orig_amt(rs.getBigDecimal("r37_orig_amt"));
obj.setR37_fore_amt(rs.getBigDecimal("r37_fore_amt"));
obj.setR37_no_of_acc(rs.getBigDecimal("r37_no_of_acc"));

// =========================
// R38
// =========================
obj.setR38_sche_fore_ass(rs.getString("r38_sche_fore_ass"));
obj.setR38_orig_amt(rs.getBigDecimal("r38_orig_amt"));
obj.setR38_fore_amt(rs.getBigDecimal("r38_fore_amt"));
obj.setR38_no_of_acc(rs.getBigDecimal("r38_no_of_acc"));

// =========================
// R39
// =========================
obj.setR39_sche_fore_ass(rs.getString("r39_sche_fore_ass"));
obj.setR39_orig_amt(rs.getBigDecimal("r39_orig_amt"));
obj.setR39_fore_amt(rs.getBigDecimal("r39_fore_amt"));
obj.setR39_no_of_acc(rs.getBigDecimal("r39_no_of_acc"));

// =========================
// R40
// =========================
obj.setR40_sche_fore_ass(rs.getString("r40_sche_fore_ass"));
obj.setR40_orig_amt(rs.getBigDecimal("r40_orig_amt"));
obj.setR40_fore_amt(rs.getBigDecimal("r40_fore_amt"));
obj.setR40_no_of_acc(rs.getBigDecimal("r40_no_of_acc"));

// =========================
// R41
// =========================
obj.setR41_sche_fore_ass(rs.getString("r41_sche_fore_ass"));
obj.setR41_orig_amt(rs.getBigDecimal("r41_orig_amt"));
obj.setR41_fore_amt(rs.getBigDecimal("r41_fore_amt"));
obj.setR41_no_of_acc(rs.getBigDecimal("r41_no_of_acc"));

// =========================
// R42
// =========================
obj.setR42_sche_fore_ass(rs.getString("r42_sche_fore_ass"));
obj.setR42_orig_amt(rs.getBigDecimal("r42_orig_amt"));
obj.setR42_fore_amt(rs.getBigDecimal("r42_fore_amt"));
obj.setR42_no_of_acc(rs.getBigDecimal("r42_no_of_acc"));

// =========================
// R43
// =========================
obj.setR43_sche_fore_ass(rs.getString("r43_sche_fore_ass"));
obj.setR43_orig_amt(rs.getBigDecimal("r43_orig_amt"));
obj.setR43_fore_amt(rs.getBigDecimal("r43_fore_amt"));
obj.setR43_no_of_acc(rs.getBigDecimal("r43_no_of_acc"));

// =========================
// R44
// =========================
obj.setR44_sche_fore_ass(rs.getString("r44_sche_fore_ass"));
obj.setR44_orig_amt(rs.getBigDecimal("r44_orig_amt"));
obj.setR44_fore_amt(rs.getBigDecimal("r44_fore_amt"));
obj.setR44_no_of_acc(rs.getBigDecimal("r44_no_of_acc"));

// =========================
// R45
// =========================
obj.setR45_sche_fore_ass(rs.getString("r45_sche_fore_ass"));
obj.setR45_orig_amt(rs.getBigDecimal("r45_orig_amt"));
obj.setR45_fore_amt(rs.getBigDecimal("r45_fore_amt"));
obj.setR45_no_of_acc(rs.getBigDecimal("r45_no_of_acc"));

// =========================
// R46
// =========================
obj.setR46_sche_fore_ass(rs.getString("r46_sche_fore_ass"));
obj.setR46_orig_amt(rs.getBigDecimal("r46_orig_amt"));
obj.setR46_fore_amt(rs.getBigDecimal("r46_fore_amt"));
obj.setR46_no_of_acc(rs.getBigDecimal("r46_no_of_acc"));

// =========================
// R47
// =========================
obj.setR47_sche_fore_ass(rs.getString("r47_sche_fore_ass"));
obj.setR47_orig_amt(rs.getBigDecimal("r47_orig_amt"));
obj.setR47_fore_amt(rs.getBigDecimal("r47_fore_amt"));
obj.setR47_no_of_acc(rs.getBigDecimal("r47_no_of_acc"));

// =========================
// R48
// =========================
obj.setR48_sche_fore_ass(rs.getString("r48_sche_fore_ass"));
obj.setR48_orig_amt(rs.getBigDecimal("r48_orig_amt"));
obj.setR48_fore_amt(rs.getBigDecimal("r48_fore_amt"));
obj.setR48_no_of_acc(rs.getBigDecimal("r48_no_of_acc"));

// =========================
// R49
// =========================
obj.setR49_sche_fore_ass(rs.getString("r49_sche_fore_ass"));
obj.setR49_orig_amt(rs.getBigDecimal("r49_orig_amt"));
obj.setR49_fore_amt(rs.getBigDecimal("r49_fore_amt"));
obj.setR49_no_of_acc(rs.getBigDecimal("r49_no_of_acc"));

// =========================
// R50
// =========================
obj.setR50_sche_fore_ass(rs.getString("r50_sche_fore_ass"));
obj.setR50_orig_amt(rs.getBigDecimal("r50_orig_amt"));
obj.setR50_fore_amt(rs.getBigDecimal("r50_fore_amt"));
obj.setR50_no_of_acc(rs.getBigDecimal("r50_no_of_acc"));

// =========================
// R51
// =========================
obj.setR51_sche_fore_ass(rs.getString("r51_sche_fore_ass"));
obj.setR51_orig_amt(rs.getBigDecimal("r51_orig_amt"));
obj.setR51_fore_amt(rs.getBigDecimal("r51_fore_amt"));
obj.setR51_no_of_acc(rs.getBigDecimal("r51_no_of_acc"));

// =========================
// R52
// =========================
obj.setR52_sche_fore_ass(rs.getString("r52_sche_fore_ass"));
obj.setR52_orig_amt(rs.getBigDecimal("r52_orig_amt"));
obj.setR52_fore_amt(rs.getBigDecimal("r52_fore_amt"));
obj.setR52_no_of_acc(rs.getBigDecimal("r52_no_of_acc"));

// =========================
// R53
// =========================
obj.setR53_sche_fore_ass(rs.getString("r53_sche_fore_ass"));
obj.setR53_orig_amt(rs.getBigDecimal("r53_orig_amt"));
obj.setR53_fore_amt(rs.getBigDecimal("r53_fore_amt"));
obj.setR53_no_of_acc(rs.getBigDecimal("r53_no_of_acc"));

// =========================
// R54
// =========================
obj.setR54_sche_fore_ass(rs.getString("r54_sche_fore_ass"));
obj.setR54_orig_amt(rs.getBigDecimal("r54_orig_amt"));
obj.setR54_fore_amt(rs.getBigDecimal("r54_fore_amt"));
obj.setR54_no_of_acc(rs.getBigDecimal("r54_no_of_acc"));

// =========================
// R55
// =========================
obj.setR55_sche_fore_ass(rs.getString("r55_sche_fore_ass"));
obj.setR55_orig_amt(rs.getBigDecimal("r55_orig_amt"));
obj.setR55_fore_amt(rs.getBigDecimal("r55_fore_amt"));
obj.setR55_no_of_acc(rs.getBigDecimal("r55_no_of_acc"));

// =========================
// R56
// =========================
obj.setR56_sche_fore_ass(rs.getString("r56_sche_fore_ass"));
obj.setR56_orig_amt(rs.getBigDecimal("r56_orig_amt"));
obj.setR56_fore_amt(rs.getBigDecimal("r56_fore_amt"));
obj.setR56_no_of_acc(rs.getBigDecimal("r56_no_of_acc"));

// =========================
// R57
// =========================
obj.setR57_sche_fore_ass(rs.getString("r57_sche_fore_ass"));
obj.setR57_orig_amt(rs.getBigDecimal("r57_orig_amt"));
obj.setR57_fore_amt(rs.getBigDecimal("r57_fore_amt"));
obj.setR57_no_of_acc(rs.getBigDecimal("r57_no_of_acc"));

// =========================
// R58
// =========================
obj.setR58_sche_fore_ass(rs.getString("r58_sche_fore_ass"));
obj.setR58_orig_amt(rs.getBigDecimal("r58_orig_amt"));
obj.setR58_fore_amt(rs.getBigDecimal("r58_fore_amt"));
obj.setR58_no_of_acc(rs.getBigDecimal("r58_no_of_acc"));

// =========================
// R59
// =========================
obj.setR59_sche_fore_ass(rs.getString("r59_sche_fore_ass"));
obj.setR59_orig_amt(rs.getBigDecimal("r59_orig_amt"));
obj.setR59_fore_amt(rs.getBigDecimal("r59_fore_amt"));
obj.setR59_no_of_acc(rs.getBigDecimal("r59_no_of_acc"));

// =========================
// R60
// =========================
obj.setR60_sche_fore_ass(rs.getString("r60_sche_fore_ass"));
obj.setR60_orig_amt(rs.getBigDecimal("r60_orig_amt"));
obj.setR60_fore_amt(rs.getBigDecimal("r60_fore_amt"));
obj.setR60_no_of_acc(rs.getBigDecimal("r60_no_of_acc"));

// =========================
// R61
// =========================
obj.setR61_sche_fore_ass(rs.getString("r61_sche_fore_ass"));
obj.setR61_orig_amt(rs.getBigDecimal("r61_orig_amt"));
obj.setR61_fore_amt(rs.getBigDecimal("r61_fore_amt"));
obj.setR61_no_of_acc(rs.getBigDecimal("r61_no_of_acc"));

// =========================
// R62
// =========================
obj.setR62_sche_fore_ass(rs.getString("r62_sche_fore_ass"));
obj.setR62_orig_amt(rs.getBigDecimal("r62_orig_amt"));
obj.setR62_fore_amt(rs.getBigDecimal("r62_fore_amt"));
obj.setR62_no_of_acc(rs.getBigDecimal("r62_no_of_acc"));

// =========================
// R63
// =========================
obj.setR63_sche_fore_ass(rs.getString("r63_sche_fore_ass"));
obj.setR63_orig_amt(rs.getBigDecimal("r63_orig_amt"));
obj.setR63_fore_amt(rs.getBigDecimal("r63_fore_amt"));
obj.setR63_no_of_acc(rs.getBigDecimal("r63_no_of_acc"));

// =========================
// R64
// =========================
obj.setR64_sche_fore_ass(rs.getString("r64_sche_fore_ass"));
obj.setR64_orig_amt(rs.getBigDecimal("r64_orig_amt"));
obj.setR64_fore_amt(rs.getBigDecimal("r64_fore_amt"));
obj.setR64_no_of_acc(rs.getBigDecimal("r64_no_of_acc"));

// =========================
// R65
// =========================
obj.setR65_sche_fore_ass(rs.getString("r65_sche_fore_ass"));
obj.setR65_orig_amt(rs.getBigDecimal("r65_orig_amt"));
obj.setR65_fore_amt(rs.getBigDecimal("r65_fore_amt"));
obj.setR65_no_of_acc(rs.getBigDecimal("r65_no_of_acc"));

// =========================
// R66
// =========================
obj.setR66_sche_fore_ass(rs.getString("r66_sche_fore_ass"));
obj.setR66_orig_amt(rs.getBigDecimal("r66_orig_amt"));
obj.setR66_fore_amt(rs.getBigDecimal("r66_fore_amt"));
obj.setR66_no_of_acc(rs.getBigDecimal("r66_no_of_acc"));

// =========================
// R67
// =========================
obj.setR67_sche_fore_ass(rs.getString("r67_sche_fore_ass"));
obj.setR67_orig_amt(rs.getBigDecimal("r67_orig_amt"));
obj.setR67_fore_amt(rs.getBigDecimal("r67_fore_amt"));
obj.setR67_no_of_acc(rs.getBigDecimal("r67_no_of_acc"));


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

public class Q_RLFA2_Archival_Detail_Entity {

   
	private String	r10_sche_fore_ass;
	private BigDecimal	r10_orig_amt;
	private BigDecimal	r10_fore_amt;
	private BigDecimal	r10_no_of_acc;
	
	private String	r11_sche_fore_ass;
	private BigDecimal	r11_orig_amt;
	private BigDecimal	r11_fore_amt;
	private BigDecimal	r11_no_of_acc;
	
	private String	r12_sche_fore_ass;
	private BigDecimal	r12_orig_amt;
	private BigDecimal	r12_fore_amt;
	private BigDecimal	r12_no_of_acc;
	
	private String	r13_sche_fore_ass;
	private BigDecimal	r13_orig_amt;
	private BigDecimal	r13_fore_amt;
	private BigDecimal	r13_no_of_acc;
	
	private String	r14_sche_fore_ass;
	private BigDecimal	r14_orig_amt;
	private BigDecimal	r14_fore_amt;
	private BigDecimal	r14_no_of_acc;
	
	private String	r15_sche_fore_ass;
	private BigDecimal	r15_orig_amt;
	private BigDecimal	r15_fore_amt;
	private BigDecimal	r15_no_of_acc;
	
	private String	r16_sche_fore_ass;
	private BigDecimal	r16_orig_amt;
	private BigDecimal	r16_fore_amt;
	private BigDecimal	r16_no_of_acc;
	
	private String	r17_sche_fore_ass;
	private BigDecimal	r17_orig_amt;
	private BigDecimal	r17_fore_amt;
	private BigDecimal	r17_no_of_acc;
	
	private String	r18_sche_fore_ass;
	private BigDecimal	r18_orig_amt;
	private BigDecimal	r18_fore_amt;
	private BigDecimal	r18_no_of_acc;
	
	private String	r19_sche_fore_ass;
	private BigDecimal	r19_orig_amt;
	private BigDecimal	r19_fore_amt;
	private BigDecimal	r19_no_of_acc;
	
	private String	r20_sche_fore_ass;
	private BigDecimal	r20_orig_amt;
	private BigDecimal	r20_fore_amt;
	private BigDecimal	r20_no_of_acc;
	
	private String	r21_sche_fore_ass;
	private BigDecimal	r21_orig_amt;
	private BigDecimal	r21_fore_amt;
	private BigDecimal	r21_no_of_acc;
	
	private String	r22_sche_fore_ass;
	private BigDecimal	r22_orig_amt;
	private BigDecimal	r22_fore_amt;
	private BigDecimal	r22_no_of_acc;
	
	private String	r23_sche_fore_ass;
	private BigDecimal	r23_orig_amt;
	private BigDecimal	r23_fore_amt;
	private BigDecimal	r23_no_of_acc;
	
	private String	r24_sche_fore_ass;
	private BigDecimal	r24_orig_amt;
	private BigDecimal	r24_fore_amt;
	private BigDecimal	r24_no_of_acc;
	
	private String	r25_sche_fore_ass;
	private BigDecimal	r25_orig_amt;
	private BigDecimal	r25_fore_amt;
	private BigDecimal	r25_no_of_acc;
	
	private String	r26_sche_fore_ass;
	private BigDecimal	r26_orig_amt;
	private BigDecimal	r26_fore_amt;
	private BigDecimal	r26_no_of_acc;
	
	private String	r27_sche_fore_ass;
	private BigDecimal	r27_orig_amt;
	private BigDecimal	r27_fore_amt;
	private BigDecimal	r27_no_of_acc;
	
	private String	r28_sche_fore_ass;
	private BigDecimal	r28_orig_amt;
	private BigDecimal	r28_fore_amt;
	private BigDecimal	r28_no_of_acc;
	
	private String	r29_sche_fore_ass;
	private BigDecimal	r29_orig_amt;
	private BigDecimal	r29_fore_amt;
	private BigDecimal	r29_no_of_acc;
	
	private String	r30_sche_fore_ass;
	private BigDecimal	r30_orig_amt;
	private BigDecimal	r30_fore_amt;
	private BigDecimal	r30_no_of_acc;
	
	private String	r31_sche_fore_ass;
	private BigDecimal	r31_orig_amt;
	private BigDecimal	r31_fore_amt;
	private BigDecimal	r31_no_of_acc;
	
	private String	r32_sche_fore_ass;
	private BigDecimal	r32_orig_amt;
	private BigDecimal	r32_fore_amt;
	private BigDecimal	r32_no_of_acc;
	
	private String	r33_sche_fore_ass;
	private BigDecimal	r33_orig_amt;
	private BigDecimal	r33_fore_amt;
	private BigDecimal	r33_no_of_acc;
	
	private String	r34_sche_fore_ass;
	private BigDecimal	r34_orig_amt;
	private BigDecimal	r34_fore_amt;
	private BigDecimal	r34_no_of_acc;
	
	private String	r35_sche_fore_ass;
	private BigDecimal	r35_orig_amt;
	private BigDecimal	r35_fore_amt;
	private BigDecimal	r35_no_of_acc;
	
	private String	r36_sche_fore_ass;
	private BigDecimal	r36_orig_amt;
	private BigDecimal	r36_fore_amt;
	private BigDecimal	r36_no_of_acc;
	
	private String	r37_sche_fore_ass;
	private BigDecimal	r37_orig_amt;
	private BigDecimal	r37_fore_amt;
	private BigDecimal	r37_no_of_acc;
	
	private String	r38_sche_fore_ass;
	private BigDecimal	r38_orig_amt;
	private BigDecimal	r38_fore_amt;
	private BigDecimal	r38_no_of_acc;
	
	private String	r39_sche_fore_ass;
	private BigDecimal	r39_orig_amt;
	private BigDecimal	r39_fore_amt;
	private BigDecimal	r39_no_of_acc;
	
	private String	r40_sche_fore_ass;
	private BigDecimal	r40_orig_amt;
	private BigDecimal	r40_fore_amt;
	private BigDecimal	r40_no_of_acc;
	
	private String	r41_sche_fore_ass;
	private BigDecimal	r41_orig_amt;
	private BigDecimal	r41_fore_amt;
	private BigDecimal	r41_no_of_acc;
	
	private String	r42_sche_fore_ass;
	private BigDecimal	r42_orig_amt;
	private BigDecimal	r42_fore_amt;
	private BigDecimal	r42_no_of_acc;
	
	private String	r43_sche_fore_ass;
	private BigDecimal	r43_orig_amt;
	private BigDecimal	r43_fore_amt;
	private BigDecimal	r43_no_of_acc;
	
	private String	r44_sche_fore_ass;
	private BigDecimal	r44_orig_amt;
	private BigDecimal	r44_fore_amt;
	private BigDecimal	r44_no_of_acc;
	
	private String	r45_sche_fore_ass;
	private BigDecimal	r45_orig_amt;
	private BigDecimal	r45_fore_amt;
	private BigDecimal	r45_no_of_acc;
	
	private String	r46_sche_fore_ass;
	private BigDecimal	r46_orig_amt;
	private BigDecimal	r46_fore_amt;
	private BigDecimal	r46_no_of_acc;
	
	private String	r47_sche_fore_ass;
	private BigDecimal	r47_orig_amt;
	private BigDecimal	r47_fore_amt;
	private BigDecimal	r47_no_of_acc;
	
	private String	r48_sche_fore_ass;
	private BigDecimal	r48_orig_amt;
	private BigDecimal	r48_fore_amt;
	private BigDecimal	r48_no_of_acc;
	
	private String	r49_sche_fore_ass;
	private BigDecimal	r49_orig_amt;
	private BigDecimal	r49_fore_amt;
	private BigDecimal	r49_no_of_acc;
	
	private String	r50_sche_fore_ass;
	private BigDecimal	r50_orig_amt;
	private BigDecimal	r50_fore_amt;
	private BigDecimal	r50_no_of_acc;
	
	private String	r51_sche_fore_ass;
	private BigDecimal	r51_orig_amt;
	private BigDecimal	r51_fore_amt;
	private BigDecimal	r51_no_of_acc;
	
	private String	r52_sche_fore_ass;
	private BigDecimal	r52_orig_amt;
	private BigDecimal	r52_fore_amt;
	private BigDecimal	r52_no_of_acc;
	
	private String	r53_sche_fore_ass;
	private BigDecimal	r53_orig_amt;
	private BigDecimal	r53_fore_amt;
	private BigDecimal	r53_no_of_acc;
	
	private String	r54_sche_fore_ass;
	private BigDecimal	r54_orig_amt;
	private BigDecimal	r54_fore_amt;
	private BigDecimal	r54_no_of_acc;
	
	private String	r55_sche_fore_ass;
	private BigDecimal	r55_orig_amt;
	private BigDecimal	r55_fore_amt;
	private BigDecimal	r55_no_of_acc;
	
	private String	r56_sche_fore_ass;
	private BigDecimal	r56_orig_amt;
	private BigDecimal	r56_fore_amt;
	private BigDecimal	r56_no_of_acc;
	
	private String	r57_sche_fore_ass;
	private BigDecimal	r57_orig_amt;
	private BigDecimal	r57_fore_amt;
	private BigDecimal	r57_no_of_acc;
	
	private String	r58_sche_fore_ass;
	private BigDecimal	r58_orig_amt;
	private BigDecimal	r58_fore_amt;
	private BigDecimal	r58_no_of_acc;
	
	private String	r59_sche_fore_ass;
	private BigDecimal	r59_orig_amt;
	private BigDecimal	r59_fore_amt;
	private BigDecimal	r59_no_of_acc;
	
	private String	r60_sche_fore_ass;
	private BigDecimal	r60_orig_amt;
	private BigDecimal	r60_fore_amt;
	private BigDecimal	r60_no_of_acc;
	
	private String	r61_sche_fore_ass;
	private BigDecimal	r61_orig_amt;
	private BigDecimal	r61_fore_amt;
	private BigDecimal	r61_no_of_acc;
	
	private String	r62_sche_fore_ass;
	private BigDecimal	r62_orig_amt;
	private BigDecimal	r62_fore_amt;
	private BigDecimal	r62_no_of_acc;
	
	private String	r63_sche_fore_ass;
	private BigDecimal	r63_orig_amt;
	private BigDecimal	r63_fore_amt;
	private BigDecimal	r63_no_of_acc;
	
	// R64
	private String     r64_sche_fore_ass;
	private BigDecimal r64_orig_amt;
	private BigDecimal r64_fore_amt;
	private BigDecimal r64_no_of_acc;

	// R65
	private String     r65_sche_fore_ass;
	private BigDecimal r65_orig_amt;
	private BigDecimal r65_fore_amt;
	private BigDecimal r65_no_of_acc;

	// R66
	private String     r66_sche_fore_ass;
	private BigDecimal r66_orig_amt;
	private BigDecimal r66_fore_amt;
	private BigDecimal r66_no_of_acc;

	// R67
	private String     r67_sche_fore_ass;
	private BigDecimal r67_orig_amt;
	private BigDecimal r67_fore_amt;
	private BigDecimal r67_no_of_acc;


	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id	
	private Date	report_date;
	
	@Id
	private BigDecimal	report_version;
	
	@Column(name = "REPORT_RESUBDATE")
    private Date reportResubDate;
	
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	 del_flg;
	
	
public String getR10_sche_fore_ass() {
		return r10_sche_fore_ass;
	}
	public void setR10_sche_fore_ass(String r10_sche_fore_ass) {
		this.r10_sche_fore_ass = r10_sche_fore_ass;
	}
	public BigDecimal getR10_orig_amt() {
		return r10_orig_amt;
	}
	public void setR10_orig_amt(BigDecimal r10_orig_amt) {
		this.r10_orig_amt = r10_orig_amt;
	}
	public BigDecimal getR10_fore_amt() {
		return r10_fore_amt;
	}
	public void setR10_fore_amt(BigDecimal r10_fore_amt) {
		this.r10_fore_amt = r10_fore_amt;
	}
	public BigDecimal getR10_no_of_acc() {
		return r10_no_of_acc;
	}
	public void setR10_no_of_acc(BigDecimal r10_no_of_acc) {
		this.r10_no_of_acc = r10_no_of_acc;
	}
	public String getR11_sche_fore_ass() {
		return r11_sche_fore_ass;
	}
	public void setR11_sche_fore_ass(String r11_sche_fore_ass) {
		this.r11_sche_fore_ass = r11_sche_fore_ass;
	}
	public BigDecimal getR11_orig_amt() {
		return r11_orig_amt;
	}
	public void setR11_orig_amt(BigDecimal r11_orig_amt) {
		this.r11_orig_amt = r11_orig_amt;
	}
	public BigDecimal getR11_fore_amt() {
		return r11_fore_amt;
	}
	public void setR11_fore_amt(BigDecimal r11_fore_amt) {
		this.r11_fore_amt = r11_fore_amt;
	}
	public BigDecimal getR11_no_of_acc() {
		return r11_no_of_acc;
	}
	public void setR11_no_of_acc(BigDecimal r11_no_of_acc) {
		this.r11_no_of_acc = r11_no_of_acc;
	}
	public String getR12_sche_fore_ass() {
		return r12_sche_fore_ass;
	}
	public void setR12_sche_fore_ass(String r12_sche_fore_ass) {
		this.r12_sche_fore_ass = r12_sche_fore_ass;
	}
	public BigDecimal getR12_orig_amt() {
		return r12_orig_amt;
	}
	public void setR12_orig_amt(BigDecimal r12_orig_amt) {
		this.r12_orig_amt = r12_orig_amt;
	}
	public BigDecimal getR12_fore_amt() {
		return r12_fore_amt;
	}
	public void setR12_fore_amt(BigDecimal r12_fore_amt) {
		this.r12_fore_amt = r12_fore_amt;
	}
	public BigDecimal getR12_no_of_acc() {
		return r12_no_of_acc;
	}
	public void setR12_no_of_acc(BigDecimal r12_no_of_acc) {
		this.r12_no_of_acc = r12_no_of_acc;
	}
	public String getR13_sche_fore_ass() {
		return r13_sche_fore_ass;
	}
	public void setR13_sche_fore_ass(String r13_sche_fore_ass) {
		this.r13_sche_fore_ass = r13_sche_fore_ass;
	}
	public BigDecimal getR13_orig_amt() {
		return r13_orig_amt;
	}
	public void setR13_orig_amt(BigDecimal r13_orig_amt) {
		this.r13_orig_amt = r13_orig_amt;
	}
	public BigDecimal getR13_fore_amt() {
		return r13_fore_amt;
	}
	public void setR13_fore_amt(BigDecimal r13_fore_amt) {
		this.r13_fore_amt = r13_fore_amt;
	}
	public BigDecimal getR13_no_of_acc() {
		return r13_no_of_acc;
	}
	public void setR13_no_of_acc(BigDecimal r13_no_of_acc) {
		this.r13_no_of_acc = r13_no_of_acc;
	}
	public String getR14_sche_fore_ass() {
		return r14_sche_fore_ass;
	}
	public void setR14_sche_fore_ass(String r14_sche_fore_ass) {
		this.r14_sche_fore_ass = r14_sche_fore_ass;
	}
	public BigDecimal getR14_orig_amt() {
		return r14_orig_amt;
	}
	public void setR14_orig_amt(BigDecimal r14_orig_amt) {
		this.r14_orig_amt = r14_orig_amt;
	}
	public BigDecimal getR14_fore_amt() {
		return r14_fore_amt;
	}
	public void setR14_fore_amt(BigDecimal r14_fore_amt) {
		this.r14_fore_amt = r14_fore_amt;
	}
	public BigDecimal getR14_no_of_acc() {
		return r14_no_of_acc;
	}
	public void setR14_no_of_acc(BigDecimal r14_no_of_acc) {
		this.r14_no_of_acc = r14_no_of_acc;
	}
	public String getR15_sche_fore_ass() {
		return r15_sche_fore_ass;
	}
	public void setR15_sche_fore_ass(String r15_sche_fore_ass) {
		this.r15_sche_fore_ass = r15_sche_fore_ass;
	}
	public BigDecimal getR15_orig_amt() {
		return r15_orig_amt;
	}
	public void setR15_orig_amt(BigDecimal r15_orig_amt) {
		this.r15_orig_amt = r15_orig_amt;
	}
	public BigDecimal getR15_fore_amt() {
		return r15_fore_amt;
	}
	public void setR15_fore_amt(BigDecimal r15_fore_amt) {
		this.r15_fore_amt = r15_fore_amt;
	}
	public BigDecimal getR15_no_of_acc() {
		return r15_no_of_acc;
	}
	public void setR15_no_of_acc(BigDecimal r15_no_of_acc) {
		this.r15_no_of_acc = r15_no_of_acc;
	}
	public String getR16_sche_fore_ass() {
		return r16_sche_fore_ass;
	}
	public void setR16_sche_fore_ass(String r16_sche_fore_ass) {
		this.r16_sche_fore_ass = r16_sche_fore_ass;
	}
	public BigDecimal getR16_orig_amt() {
		return r16_orig_amt;
	}
	public void setR16_orig_amt(BigDecimal r16_orig_amt) {
		this.r16_orig_amt = r16_orig_amt;
	}
	public BigDecimal getR16_fore_amt() {
		return r16_fore_amt;
	}
	public void setR16_fore_amt(BigDecimal r16_fore_amt) {
		this.r16_fore_amt = r16_fore_amt;
	}
	public BigDecimal getR16_no_of_acc() {
		return r16_no_of_acc;
	}
	public void setR16_no_of_acc(BigDecimal r16_no_of_acc) {
		this.r16_no_of_acc = r16_no_of_acc;
	}
	public String getR17_sche_fore_ass() {
		return r17_sche_fore_ass;
	}
	public void setR17_sche_fore_ass(String r17_sche_fore_ass) {
		this.r17_sche_fore_ass = r17_sche_fore_ass;
	}
	public BigDecimal getR17_orig_amt() {
		return r17_orig_amt;
	}
	public void setR17_orig_amt(BigDecimal r17_orig_amt) {
		this.r17_orig_amt = r17_orig_amt;
	}
	public BigDecimal getR17_fore_amt() {
		return r17_fore_amt;
	}
	public void setR17_fore_amt(BigDecimal r17_fore_amt) {
		this.r17_fore_amt = r17_fore_amt;
	}
	public BigDecimal getR17_no_of_acc() {
		return r17_no_of_acc;
	}
	public void setR17_no_of_acc(BigDecimal r17_no_of_acc) {
		this.r17_no_of_acc = r17_no_of_acc;
	}
	public String getR18_sche_fore_ass() {
		return r18_sche_fore_ass;
	}
	public void setR18_sche_fore_ass(String r18_sche_fore_ass) {
		this.r18_sche_fore_ass = r18_sche_fore_ass;
	}
	public BigDecimal getR18_orig_amt() {
		return r18_orig_amt;
	}
	public void setR18_orig_amt(BigDecimal r18_orig_amt) {
		this.r18_orig_amt = r18_orig_amt;
	}
	public BigDecimal getR18_fore_amt() {
		return r18_fore_amt;
	}
	public void setR18_fore_amt(BigDecimal r18_fore_amt) {
		this.r18_fore_amt = r18_fore_amt;
	}
	public BigDecimal getR18_no_of_acc() {
		return r18_no_of_acc;
	}
	public void setR18_no_of_acc(BigDecimal r18_no_of_acc) {
		this.r18_no_of_acc = r18_no_of_acc;
	}
	public String getR19_sche_fore_ass() {
		return r19_sche_fore_ass;
	}
	public void setR19_sche_fore_ass(String r19_sche_fore_ass) {
		this.r19_sche_fore_ass = r19_sche_fore_ass;
	}
	public BigDecimal getR19_orig_amt() {
		return r19_orig_amt;
	}
	public void setR19_orig_amt(BigDecimal r19_orig_amt) {
		this.r19_orig_amt = r19_orig_amt;
	}
	public BigDecimal getR19_fore_amt() {
		return r19_fore_amt;
	}
	public void setR19_fore_amt(BigDecimal r19_fore_amt) {
		this.r19_fore_amt = r19_fore_amt;
	}
	public BigDecimal getR19_no_of_acc() {
		return r19_no_of_acc;
	}
	public void setR19_no_of_acc(BigDecimal r19_no_of_acc) {
		this.r19_no_of_acc = r19_no_of_acc;
	}
	public String getR20_sche_fore_ass() {
		return r20_sche_fore_ass;
	}
	public void setR20_sche_fore_ass(String r20_sche_fore_ass) {
		this.r20_sche_fore_ass = r20_sche_fore_ass;
	}
	public BigDecimal getR20_orig_amt() {
		return r20_orig_amt;
	}
	public void setR20_orig_amt(BigDecimal r20_orig_amt) {
		this.r20_orig_amt = r20_orig_amt;
	}
	public BigDecimal getR20_fore_amt() {
		return r20_fore_amt;
	}
	public void setR20_fore_amt(BigDecimal r20_fore_amt) {
		this.r20_fore_amt = r20_fore_amt;
	}
	public BigDecimal getR20_no_of_acc() {
		return r20_no_of_acc;
	}
	public void setR20_no_of_acc(BigDecimal r20_no_of_acc) {
		this.r20_no_of_acc = r20_no_of_acc;
	}
	public String getR21_sche_fore_ass() {
		return r21_sche_fore_ass;
	}
	public void setR21_sche_fore_ass(String r21_sche_fore_ass) {
		this.r21_sche_fore_ass = r21_sche_fore_ass;
	}
	public BigDecimal getR21_orig_amt() {
		return r21_orig_amt;
	}
	public void setR21_orig_amt(BigDecimal r21_orig_amt) {
		this.r21_orig_amt = r21_orig_amt;
	}
	public BigDecimal getR21_fore_amt() {
		return r21_fore_amt;
	}
	public void setR21_fore_amt(BigDecimal r21_fore_amt) {
		this.r21_fore_amt = r21_fore_amt;
	}
	public BigDecimal getR21_no_of_acc() {
		return r21_no_of_acc;
	}
	public void setR21_no_of_acc(BigDecimal r21_no_of_acc) {
		this.r21_no_of_acc = r21_no_of_acc;
	}
	public String getR22_sche_fore_ass() {
		return r22_sche_fore_ass;
	}
	public void setR22_sche_fore_ass(String r22_sche_fore_ass) {
		this.r22_sche_fore_ass = r22_sche_fore_ass;
	}
	public BigDecimal getR22_orig_amt() {
		return r22_orig_amt;
	}
	public void setR22_orig_amt(BigDecimal r22_orig_amt) {
		this.r22_orig_amt = r22_orig_amt;
	}
	public BigDecimal getR22_fore_amt() {
		return r22_fore_amt;
	}
	public void setR22_fore_amt(BigDecimal r22_fore_amt) {
		this.r22_fore_amt = r22_fore_amt;
	}
	public BigDecimal getR22_no_of_acc() {
		return r22_no_of_acc;
	}
	public void setR22_no_of_acc(BigDecimal r22_no_of_acc) {
		this.r22_no_of_acc = r22_no_of_acc;
	}
	public String getR23_sche_fore_ass() {
		return r23_sche_fore_ass;
	}
	public void setR23_sche_fore_ass(String r23_sche_fore_ass) {
		this.r23_sche_fore_ass = r23_sche_fore_ass;
	}
	public BigDecimal getR23_orig_amt() {
		return r23_orig_amt;
	}
	public void setR23_orig_amt(BigDecimal r23_orig_amt) {
		this.r23_orig_amt = r23_orig_amt;
	}
	public BigDecimal getR23_fore_amt() {
		return r23_fore_amt;
	}
	public void setR23_fore_amt(BigDecimal r23_fore_amt) {
		this.r23_fore_amt = r23_fore_amt;
	}
	public BigDecimal getR23_no_of_acc() {
		return r23_no_of_acc;
	}
	public void setR23_no_of_acc(BigDecimal r23_no_of_acc) {
		this.r23_no_of_acc = r23_no_of_acc;
	}
	public String getR24_sche_fore_ass() {
		return r24_sche_fore_ass;
	}
	public void setR24_sche_fore_ass(String r24_sche_fore_ass) {
		this.r24_sche_fore_ass = r24_sche_fore_ass;
	}
	public BigDecimal getR24_orig_amt() {
		return r24_orig_amt;
	}
	public void setR24_orig_amt(BigDecimal r24_orig_amt) {
		this.r24_orig_amt = r24_orig_amt;
	}
	public BigDecimal getR24_fore_amt() {
		return r24_fore_amt;
	}
	public void setR24_fore_amt(BigDecimal r24_fore_amt) {
		this.r24_fore_amt = r24_fore_amt;
	}
	public BigDecimal getR24_no_of_acc() {
		return r24_no_of_acc;
	}
	public void setR24_no_of_acc(BigDecimal r24_no_of_acc) {
		this.r24_no_of_acc = r24_no_of_acc;
	}
	public String getR25_sche_fore_ass() {
		return r25_sche_fore_ass;
	}
	public void setR25_sche_fore_ass(String r25_sche_fore_ass) {
		this.r25_sche_fore_ass = r25_sche_fore_ass;
	}
	public BigDecimal getR25_orig_amt() {
		return r25_orig_amt;
	}
	public void setR25_orig_amt(BigDecimal r25_orig_amt) {
		this.r25_orig_amt = r25_orig_amt;
	}
	public BigDecimal getR25_fore_amt() {
		return r25_fore_amt;
	}
	public void setR25_fore_amt(BigDecimal r25_fore_amt) {
		this.r25_fore_amt = r25_fore_amt;
	}
	public BigDecimal getR25_no_of_acc() {
		return r25_no_of_acc;
	}
	public void setR25_no_of_acc(BigDecimal r25_no_of_acc) {
		this.r25_no_of_acc = r25_no_of_acc;
	}
	public String getR26_sche_fore_ass() {
		return r26_sche_fore_ass;
	}
	public void setR26_sche_fore_ass(String r26_sche_fore_ass) {
		this.r26_sche_fore_ass = r26_sche_fore_ass;
	}
	public BigDecimal getR26_orig_amt() {
		return r26_orig_amt;
	}
	public void setR26_orig_amt(BigDecimal r26_orig_amt) {
		this.r26_orig_amt = r26_orig_amt;
	}
	public BigDecimal getR26_fore_amt() {
		return r26_fore_amt;
	}
	public void setR26_fore_amt(BigDecimal r26_fore_amt) {
		this.r26_fore_amt = r26_fore_amt;
	}
	public BigDecimal getR26_no_of_acc() {
		return r26_no_of_acc;
	}
	public void setR26_no_of_acc(BigDecimal r26_no_of_acc) {
		this.r26_no_of_acc = r26_no_of_acc;
	}
	public String getR27_sche_fore_ass() {
		return r27_sche_fore_ass;
	}
	public void setR27_sche_fore_ass(String r27_sche_fore_ass) {
		this.r27_sche_fore_ass = r27_sche_fore_ass;
	}
	public BigDecimal getR27_orig_amt() {
		return r27_orig_amt;
	}
	public void setR27_orig_amt(BigDecimal r27_orig_amt) {
		this.r27_orig_amt = r27_orig_amt;
	}
	public BigDecimal getR27_fore_amt() {
		return r27_fore_amt;
	}
	public void setR27_fore_amt(BigDecimal r27_fore_amt) {
		this.r27_fore_amt = r27_fore_amt;
	}
	public BigDecimal getR27_no_of_acc() {
		return r27_no_of_acc;
	}
	public void setR27_no_of_acc(BigDecimal r27_no_of_acc) {
		this.r27_no_of_acc = r27_no_of_acc;
	}
	public String getR28_sche_fore_ass() {
		return r28_sche_fore_ass;
	}
	public void setR28_sche_fore_ass(String r28_sche_fore_ass) {
		this.r28_sche_fore_ass = r28_sche_fore_ass;
	}
	public BigDecimal getR28_orig_amt() {
		return r28_orig_amt;
	}
	public void setR28_orig_amt(BigDecimal r28_orig_amt) {
		this.r28_orig_amt = r28_orig_amt;
	}
	public BigDecimal getR28_fore_amt() {
		return r28_fore_amt;
	}
	public void setR28_fore_amt(BigDecimal r28_fore_amt) {
		this.r28_fore_amt = r28_fore_amt;
	}
	public BigDecimal getR28_no_of_acc() {
		return r28_no_of_acc;
	}
	public void setR28_no_of_acc(BigDecimal r28_no_of_acc) {
		this.r28_no_of_acc = r28_no_of_acc;
	}
	public String getR29_sche_fore_ass() {
		return r29_sche_fore_ass;
	}
	public void setR29_sche_fore_ass(String r29_sche_fore_ass) {
		this.r29_sche_fore_ass = r29_sche_fore_ass;
	}
	public BigDecimal getR29_orig_amt() {
		return r29_orig_amt;
	}
	public void setR29_orig_amt(BigDecimal r29_orig_amt) {
		this.r29_orig_amt = r29_orig_amt;
	}
	public BigDecimal getR29_fore_amt() {
		return r29_fore_amt;
	}
	public void setR29_fore_amt(BigDecimal r29_fore_amt) {
		this.r29_fore_amt = r29_fore_amt;
	}
	public BigDecimal getR29_no_of_acc() {
		return r29_no_of_acc;
	}
	public void setR29_no_of_acc(BigDecimal r29_no_of_acc) {
		this.r29_no_of_acc = r29_no_of_acc;
	}
	public String getR30_sche_fore_ass() {
		return r30_sche_fore_ass;
	}
	public void setR30_sche_fore_ass(String r30_sche_fore_ass) {
		this.r30_sche_fore_ass = r30_sche_fore_ass;
	}
	public BigDecimal getR30_orig_amt() {
		return r30_orig_amt;
	}
	public void setR30_orig_amt(BigDecimal r30_orig_amt) {
		this.r30_orig_amt = r30_orig_amt;
	}
	public BigDecimal getR30_fore_amt() {
		return r30_fore_amt;
	}
	public void setR30_fore_amt(BigDecimal r30_fore_amt) {
		this.r30_fore_amt = r30_fore_amt;
	}
	public BigDecimal getR30_no_of_acc() {
		return r30_no_of_acc;
	}
	public void setR30_no_of_acc(BigDecimal r30_no_of_acc) {
		this.r30_no_of_acc = r30_no_of_acc;
	}
	public String getR31_sche_fore_ass() {
		return r31_sche_fore_ass;
	}
	public void setR31_sche_fore_ass(String r31_sche_fore_ass) {
		this.r31_sche_fore_ass = r31_sche_fore_ass;
	}
	public BigDecimal getR31_orig_amt() {
		return r31_orig_amt;
	}
	public void setR31_orig_amt(BigDecimal r31_orig_amt) {
		this.r31_orig_amt = r31_orig_amt;
	}
	public BigDecimal getR31_fore_amt() {
		return r31_fore_amt;
	}
	public void setR31_fore_amt(BigDecimal r31_fore_amt) {
		this.r31_fore_amt = r31_fore_amt;
	}
	public BigDecimal getR31_no_of_acc() {
		return r31_no_of_acc;
	}
	public void setR31_no_of_acc(BigDecimal r31_no_of_acc) {
		this.r31_no_of_acc = r31_no_of_acc;
	}
	public String getR32_sche_fore_ass() {
		return r32_sche_fore_ass;
	}
	public void setR32_sche_fore_ass(String r32_sche_fore_ass) {
		this.r32_sche_fore_ass = r32_sche_fore_ass;
	}
	public BigDecimal getR32_orig_amt() {
		return r32_orig_amt;
	}
	public void setR32_orig_amt(BigDecimal r32_orig_amt) {
		this.r32_orig_amt = r32_orig_amt;
	}
	public BigDecimal getR32_fore_amt() {
		return r32_fore_amt;
	}
	public void setR32_fore_amt(BigDecimal r32_fore_amt) {
		this.r32_fore_amt = r32_fore_amt;
	}
	public BigDecimal getR32_no_of_acc() {
		return r32_no_of_acc;
	}
	public void setR32_no_of_acc(BigDecimal r32_no_of_acc) {
		this.r32_no_of_acc = r32_no_of_acc;
	}
	public String getR33_sche_fore_ass() {
		return r33_sche_fore_ass;
	}
	public void setR33_sche_fore_ass(String r33_sche_fore_ass) {
		this.r33_sche_fore_ass = r33_sche_fore_ass;
	}
	public BigDecimal getR33_orig_amt() {
		return r33_orig_amt;
	}
	public void setR33_orig_amt(BigDecimal r33_orig_amt) {
		this.r33_orig_amt = r33_orig_amt;
	}
	public BigDecimal getR33_fore_amt() {
		return r33_fore_amt;
	}
	public void setR33_fore_amt(BigDecimal r33_fore_amt) {
		this.r33_fore_amt = r33_fore_amt;
	}
	public BigDecimal getR33_no_of_acc() {
		return r33_no_of_acc;
	}
	public void setR33_no_of_acc(BigDecimal r33_no_of_acc) {
		this.r33_no_of_acc = r33_no_of_acc;
	}
	public String getR34_sche_fore_ass() {
		return r34_sche_fore_ass;
	}
	public void setR34_sche_fore_ass(String r34_sche_fore_ass) {
		this.r34_sche_fore_ass = r34_sche_fore_ass;
	}
	public BigDecimal getR34_orig_amt() {
		return r34_orig_amt;
	}
	public void setR34_orig_amt(BigDecimal r34_orig_amt) {
		this.r34_orig_amt = r34_orig_amt;
	}
	public BigDecimal getR34_fore_amt() {
		return r34_fore_amt;
	}
	public void setR34_fore_amt(BigDecimal r34_fore_amt) {
		this.r34_fore_amt = r34_fore_amt;
	}
	public BigDecimal getR34_no_of_acc() {
		return r34_no_of_acc;
	}
	public void setR34_no_of_acc(BigDecimal r34_no_of_acc) {
		this.r34_no_of_acc = r34_no_of_acc;
	}
	public String getR35_sche_fore_ass() {
		return r35_sche_fore_ass;
	}
	public void setR35_sche_fore_ass(String r35_sche_fore_ass) {
		this.r35_sche_fore_ass = r35_sche_fore_ass;
	}
	public BigDecimal getR35_orig_amt() {
		return r35_orig_amt;
	}
	public void setR35_orig_amt(BigDecimal r35_orig_amt) {
		this.r35_orig_amt = r35_orig_amt;
	}
	public BigDecimal getR35_fore_amt() {
		return r35_fore_amt;
	}
	public void setR35_fore_amt(BigDecimal r35_fore_amt) {
		this.r35_fore_amt = r35_fore_amt;
	}
	public BigDecimal getR35_no_of_acc() {
		return r35_no_of_acc;
	}
	public void setR35_no_of_acc(BigDecimal r35_no_of_acc) {
		this.r35_no_of_acc = r35_no_of_acc;
	}
	public String getR36_sche_fore_ass() {
		return r36_sche_fore_ass;
	}
	public void setR36_sche_fore_ass(String r36_sche_fore_ass) {
		this.r36_sche_fore_ass = r36_sche_fore_ass;
	}
	public BigDecimal getR36_orig_amt() {
		return r36_orig_amt;
	}
	public void setR36_orig_amt(BigDecimal r36_orig_amt) {
		this.r36_orig_amt = r36_orig_amt;
	}
	public BigDecimal getR36_fore_amt() {
		return r36_fore_amt;
	}
	public void setR36_fore_amt(BigDecimal r36_fore_amt) {
		this.r36_fore_amt = r36_fore_amt;
	}
	public BigDecimal getR36_no_of_acc() {
		return r36_no_of_acc;
	}
	public void setR36_no_of_acc(BigDecimal r36_no_of_acc) {
		this.r36_no_of_acc = r36_no_of_acc;
	}
	public String getR37_sche_fore_ass() {
		return r37_sche_fore_ass;
	}
	public void setR37_sche_fore_ass(String r37_sche_fore_ass) {
		this.r37_sche_fore_ass = r37_sche_fore_ass;
	}
	public BigDecimal getR37_orig_amt() {
		return r37_orig_amt;
	}
	public void setR37_orig_amt(BigDecimal r37_orig_amt) {
		this.r37_orig_amt = r37_orig_amt;
	}
	public BigDecimal getR37_fore_amt() {
		return r37_fore_amt;
	}
	public void setR37_fore_amt(BigDecimal r37_fore_amt) {
		this.r37_fore_amt = r37_fore_amt;
	}
	public BigDecimal getR37_no_of_acc() {
		return r37_no_of_acc;
	}
	public void setR37_no_of_acc(BigDecimal r37_no_of_acc) {
		this.r37_no_of_acc = r37_no_of_acc;
	}
	public String getR38_sche_fore_ass() {
		return r38_sche_fore_ass;
	}
	public void setR38_sche_fore_ass(String r38_sche_fore_ass) {
		this.r38_sche_fore_ass = r38_sche_fore_ass;
	}
	public BigDecimal getR38_orig_amt() {
		return r38_orig_amt;
	}
	public void setR38_orig_amt(BigDecimal r38_orig_amt) {
		this.r38_orig_amt = r38_orig_amt;
	}
	public BigDecimal getR38_fore_amt() {
		return r38_fore_amt;
	}
	public void setR38_fore_amt(BigDecimal r38_fore_amt) {
		this.r38_fore_amt = r38_fore_amt;
	}
	public BigDecimal getR38_no_of_acc() {
		return r38_no_of_acc;
	}
	public void setR38_no_of_acc(BigDecimal r38_no_of_acc) {
		this.r38_no_of_acc = r38_no_of_acc;
	}
	public String getR39_sche_fore_ass() {
		return r39_sche_fore_ass;
	}
	public void setR39_sche_fore_ass(String r39_sche_fore_ass) {
		this.r39_sche_fore_ass = r39_sche_fore_ass;
	}
	public BigDecimal getR39_orig_amt() {
		return r39_orig_amt;
	}
	public void setR39_orig_amt(BigDecimal r39_orig_amt) {
		this.r39_orig_amt = r39_orig_amt;
	}
	public BigDecimal getR39_fore_amt() {
		return r39_fore_amt;
	}
	public void setR39_fore_amt(BigDecimal r39_fore_amt) {
		this.r39_fore_amt = r39_fore_amt;
	}
	public BigDecimal getR39_no_of_acc() {
		return r39_no_of_acc;
	}
	public void setR39_no_of_acc(BigDecimal r39_no_of_acc) {
		this.r39_no_of_acc = r39_no_of_acc;
	}
	public String getR40_sche_fore_ass() {
		return r40_sche_fore_ass;
	}
	public void setR40_sche_fore_ass(String r40_sche_fore_ass) {
		this.r40_sche_fore_ass = r40_sche_fore_ass;
	}
	public BigDecimal getR40_orig_amt() {
		return r40_orig_amt;
	}
	public void setR40_orig_amt(BigDecimal r40_orig_amt) {
		this.r40_orig_amt = r40_orig_amt;
	}
	public BigDecimal getR40_fore_amt() {
		return r40_fore_amt;
	}
	public void setR40_fore_amt(BigDecimal r40_fore_amt) {
		this.r40_fore_amt = r40_fore_amt;
	}
	public BigDecimal getR40_no_of_acc() {
		return r40_no_of_acc;
	}
	public void setR40_no_of_acc(BigDecimal r40_no_of_acc) {
		this.r40_no_of_acc = r40_no_of_acc;
	}
	public String getR41_sche_fore_ass() {
		return r41_sche_fore_ass;
	}
	public void setR41_sche_fore_ass(String r41_sche_fore_ass) {
		this.r41_sche_fore_ass = r41_sche_fore_ass;
	}
	public BigDecimal getR41_orig_amt() {
		return r41_orig_amt;
	}
	public void setR41_orig_amt(BigDecimal r41_orig_amt) {
		this.r41_orig_amt = r41_orig_amt;
	}
	public BigDecimal getR41_fore_amt() {
		return r41_fore_amt;
	}
	public void setR41_fore_amt(BigDecimal r41_fore_amt) {
		this.r41_fore_amt = r41_fore_amt;
	}
	public BigDecimal getR41_no_of_acc() {
		return r41_no_of_acc;
	}
	public void setR41_no_of_acc(BigDecimal r41_no_of_acc) {
		this.r41_no_of_acc = r41_no_of_acc;
	}
	public String getR42_sche_fore_ass() {
		return r42_sche_fore_ass;
	}
	public void setR42_sche_fore_ass(String r42_sche_fore_ass) {
		this.r42_sche_fore_ass = r42_sche_fore_ass;
	}
	public BigDecimal getR42_orig_amt() {
		return r42_orig_amt;
	}
	public void setR42_orig_amt(BigDecimal r42_orig_amt) {
		this.r42_orig_amt = r42_orig_amt;
	}
	public BigDecimal getR42_fore_amt() {
		return r42_fore_amt;
	}
	public void setR42_fore_amt(BigDecimal r42_fore_amt) {
		this.r42_fore_amt = r42_fore_amt;
	}
	public BigDecimal getR42_no_of_acc() {
		return r42_no_of_acc;
	}
	public void setR42_no_of_acc(BigDecimal r42_no_of_acc) {
		this.r42_no_of_acc = r42_no_of_acc;
	}
	public String getR43_sche_fore_ass() {
		return r43_sche_fore_ass;
	}
	public void setR43_sche_fore_ass(String r43_sche_fore_ass) {
		this.r43_sche_fore_ass = r43_sche_fore_ass;
	}
	public BigDecimal getR43_orig_amt() {
		return r43_orig_amt;
	}
	public void setR43_orig_amt(BigDecimal r43_orig_amt) {
		this.r43_orig_amt = r43_orig_amt;
	}
	public BigDecimal getR43_fore_amt() {
		return r43_fore_amt;
	}
	public void setR43_fore_amt(BigDecimal r43_fore_amt) {
		this.r43_fore_amt = r43_fore_amt;
	}
	public BigDecimal getR43_no_of_acc() {
		return r43_no_of_acc;
	}
	public void setR43_no_of_acc(BigDecimal r43_no_of_acc) {
		this.r43_no_of_acc = r43_no_of_acc;
	}
	public String getR44_sche_fore_ass() {
		return r44_sche_fore_ass;
	}
	public void setR44_sche_fore_ass(String r44_sche_fore_ass) {
		this.r44_sche_fore_ass = r44_sche_fore_ass;
	}
	public BigDecimal getR44_orig_amt() {
		return r44_orig_amt;
	}
	public void setR44_orig_amt(BigDecimal r44_orig_amt) {
		this.r44_orig_amt = r44_orig_amt;
	}
	public BigDecimal getR44_fore_amt() {
		return r44_fore_amt;
	}
	public void setR44_fore_amt(BigDecimal r44_fore_amt) {
		this.r44_fore_amt = r44_fore_amt;
	}
	public BigDecimal getR44_no_of_acc() {
		return r44_no_of_acc;
	}
	public void setR44_no_of_acc(BigDecimal r44_no_of_acc) {
		this.r44_no_of_acc = r44_no_of_acc;
	}
	public String getR45_sche_fore_ass() {
		return r45_sche_fore_ass;
	}
	public void setR45_sche_fore_ass(String r45_sche_fore_ass) {
		this.r45_sche_fore_ass = r45_sche_fore_ass;
	}
	public BigDecimal getR45_orig_amt() {
		return r45_orig_amt;
	}
	public void setR45_orig_amt(BigDecimal r45_orig_amt) {
		this.r45_orig_amt = r45_orig_amt;
	}
	public BigDecimal getR45_fore_amt() {
		return r45_fore_amt;
	}
	public void setR45_fore_amt(BigDecimal r45_fore_amt) {
		this.r45_fore_amt = r45_fore_amt;
	}
	public BigDecimal getR45_no_of_acc() {
		return r45_no_of_acc;
	}
	public void setR45_no_of_acc(BigDecimal r45_no_of_acc) {
		this.r45_no_of_acc = r45_no_of_acc;
	}
	public String getR46_sche_fore_ass() {
		return r46_sche_fore_ass;
	}
	public void setR46_sche_fore_ass(String r46_sche_fore_ass) {
		this.r46_sche_fore_ass = r46_sche_fore_ass;
	}
	public BigDecimal getR46_orig_amt() {
		return r46_orig_amt;
	}
	public void setR46_orig_amt(BigDecimal r46_orig_amt) {
		this.r46_orig_amt = r46_orig_amt;
	}
	public BigDecimal getR46_fore_amt() {
		return r46_fore_amt;
	}
	public void setR46_fore_amt(BigDecimal r46_fore_amt) {
		this.r46_fore_amt = r46_fore_amt;
	}
	public BigDecimal getR46_no_of_acc() {
		return r46_no_of_acc;
	}
	public void setR46_no_of_acc(BigDecimal r46_no_of_acc) {
		this.r46_no_of_acc = r46_no_of_acc;
	}
	public String getR47_sche_fore_ass() {
		return r47_sche_fore_ass;
	}
	public void setR47_sche_fore_ass(String r47_sche_fore_ass) {
		this.r47_sche_fore_ass = r47_sche_fore_ass;
	}
	public BigDecimal getR47_orig_amt() {
		return r47_orig_amt;
	}
	public void setR47_orig_amt(BigDecimal r47_orig_amt) {
		this.r47_orig_amt = r47_orig_amt;
	}
	public BigDecimal getR47_fore_amt() {
		return r47_fore_amt;
	}
	public void setR47_fore_amt(BigDecimal r47_fore_amt) {
		this.r47_fore_amt = r47_fore_amt;
	}
	public BigDecimal getR47_no_of_acc() {
		return r47_no_of_acc;
	}
	public void setR47_no_of_acc(BigDecimal r47_no_of_acc) {
		this.r47_no_of_acc = r47_no_of_acc;
	}
	public String getR48_sche_fore_ass() {
		return r48_sche_fore_ass;
	}
	public void setR48_sche_fore_ass(String r48_sche_fore_ass) {
		this.r48_sche_fore_ass = r48_sche_fore_ass;
	}
	public BigDecimal getR48_orig_amt() {
		return r48_orig_amt;
	}
	public void setR48_orig_amt(BigDecimal r48_orig_amt) {
		this.r48_orig_amt = r48_orig_amt;
	}
	public BigDecimal getR48_fore_amt() {
		return r48_fore_amt;
	}
	public void setR48_fore_amt(BigDecimal r48_fore_amt) {
		this.r48_fore_amt = r48_fore_amt;
	}
	public BigDecimal getR48_no_of_acc() {
		return r48_no_of_acc;
	}
	public void setR48_no_of_acc(BigDecimal r48_no_of_acc) {
		this.r48_no_of_acc = r48_no_of_acc;
	}
	public String getR49_sche_fore_ass() {
		return r49_sche_fore_ass;
	}
	public void setR49_sche_fore_ass(String r49_sche_fore_ass) {
		this.r49_sche_fore_ass = r49_sche_fore_ass;
	}
	public BigDecimal getR49_orig_amt() {
		return r49_orig_amt;
	}
	public void setR49_orig_amt(BigDecimal r49_orig_amt) {
		this.r49_orig_amt = r49_orig_amt;
	}
	public BigDecimal getR49_fore_amt() {
		return r49_fore_amt;
	}
	public void setR49_fore_amt(BigDecimal r49_fore_amt) {
		this.r49_fore_amt = r49_fore_amt;
	}
	public BigDecimal getR49_no_of_acc() {
		return r49_no_of_acc;
	}
	public void setR49_no_of_acc(BigDecimal r49_no_of_acc) {
		this.r49_no_of_acc = r49_no_of_acc;
	}
	public String getR50_sche_fore_ass() {
		return r50_sche_fore_ass;
	}
	public void setR50_sche_fore_ass(String r50_sche_fore_ass) {
		this.r50_sche_fore_ass = r50_sche_fore_ass;
	}
	public BigDecimal getR50_orig_amt() {
		return r50_orig_amt;
	}
	public void setR50_orig_amt(BigDecimal r50_orig_amt) {
		this.r50_orig_amt = r50_orig_amt;
	}
	public BigDecimal getR50_fore_amt() {
		return r50_fore_amt;
	}
	public void setR50_fore_amt(BigDecimal r50_fore_amt) {
		this.r50_fore_amt = r50_fore_amt;
	}
	public BigDecimal getR50_no_of_acc() {
		return r50_no_of_acc;
	}
	public void setR50_no_of_acc(BigDecimal r50_no_of_acc) {
		this.r50_no_of_acc = r50_no_of_acc;
	}
	public String getR51_sche_fore_ass() {
		return r51_sche_fore_ass;
	}
	public void setR51_sche_fore_ass(String r51_sche_fore_ass) {
		this.r51_sche_fore_ass = r51_sche_fore_ass;
	}
	public BigDecimal getR51_orig_amt() {
		return r51_orig_amt;
	}
	public void setR51_orig_amt(BigDecimal r51_orig_amt) {
		this.r51_orig_amt = r51_orig_amt;
	}
	public BigDecimal getR51_fore_amt() {
		return r51_fore_amt;
	}
	public void setR51_fore_amt(BigDecimal r51_fore_amt) {
		this.r51_fore_amt = r51_fore_amt;
	}
	public BigDecimal getR51_no_of_acc() {
		return r51_no_of_acc;
	}
	public void setR51_no_of_acc(BigDecimal r51_no_of_acc) {
		this.r51_no_of_acc = r51_no_of_acc;
	}
	public String getR52_sche_fore_ass() {
		return r52_sche_fore_ass;
	}
	public void setR52_sche_fore_ass(String r52_sche_fore_ass) {
		this.r52_sche_fore_ass = r52_sche_fore_ass;
	}
	public BigDecimal getR52_orig_amt() {
		return r52_orig_amt;
	}
	public void setR52_orig_amt(BigDecimal r52_orig_amt) {
		this.r52_orig_amt = r52_orig_amt;
	}
	public BigDecimal getR52_fore_amt() {
		return r52_fore_amt;
	}
	public void setR52_fore_amt(BigDecimal r52_fore_amt) {
		this.r52_fore_amt = r52_fore_amt;
	}
	public BigDecimal getR52_no_of_acc() {
		return r52_no_of_acc;
	}
	public void setR52_no_of_acc(BigDecimal r52_no_of_acc) {
		this.r52_no_of_acc = r52_no_of_acc;
	}
	public String getR53_sche_fore_ass() {
		return r53_sche_fore_ass;
	}
	public void setR53_sche_fore_ass(String r53_sche_fore_ass) {
		this.r53_sche_fore_ass = r53_sche_fore_ass;
	}
	public BigDecimal getR53_orig_amt() {
		return r53_orig_amt;
	}
	public void setR53_orig_amt(BigDecimal r53_orig_amt) {
		this.r53_orig_amt = r53_orig_amt;
	}
	public BigDecimal getR53_fore_amt() {
		return r53_fore_amt;
	}
	public void setR53_fore_amt(BigDecimal r53_fore_amt) {
		this.r53_fore_amt = r53_fore_amt;
	}
	public BigDecimal getR53_no_of_acc() {
		return r53_no_of_acc;
	}
	public void setR53_no_of_acc(BigDecimal r53_no_of_acc) {
		this.r53_no_of_acc = r53_no_of_acc;
	}
	public String getR54_sche_fore_ass() {
		return r54_sche_fore_ass;
	}
	public void setR54_sche_fore_ass(String r54_sche_fore_ass) {
		this.r54_sche_fore_ass = r54_sche_fore_ass;
	}
	public BigDecimal getR54_orig_amt() {
		return r54_orig_amt;
	}
	public void setR54_orig_amt(BigDecimal r54_orig_amt) {
		this.r54_orig_amt = r54_orig_amt;
	}
	public BigDecimal getR54_fore_amt() {
		return r54_fore_amt;
	}
	public void setR54_fore_amt(BigDecimal r54_fore_amt) {
		this.r54_fore_amt = r54_fore_amt;
	}
	public BigDecimal getR54_no_of_acc() {
		return r54_no_of_acc;
	}
	public void setR54_no_of_acc(BigDecimal r54_no_of_acc) {
		this.r54_no_of_acc = r54_no_of_acc;
	}
	public String getR55_sche_fore_ass() {
		return r55_sche_fore_ass;
	}
	public void setR55_sche_fore_ass(String r55_sche_fore_ass) {
		this.r55_sche_fore_ass = r55_sche_fore_ass;
	}
	public BigDecimal getR55_orig_amt() {
		return r55_orig_amt;
	}
	public void setR55_orig_amt(BigDecimal r55_orig_amt) {
		this.r55_orig_amt = r55_orig_amt;
	}
	public BigDecimal getR55_fore_amt() {
		return r55_fore_amt;
	}
	public void setR55_fore_amt(BigDecimal r55_fore_amt) {
		this.r55_fore_amt = r55_fore_amt;
	}
	public BigDecimal getR55_no_of_acc() {
		return r55_no_of_acc;
	}
	public void setR55_no_of_acc(BigDecimal r55_no_of_acc) {
		this.r55_no_of_acc = r55_no_of_acc;
	}
	public String getR56_sche_fore_ass() {
		return r56_sche_fore_ass;
	}
	public void setR56_sche_fore_ass(String r56_sche_fore_ass) {
		this.r56_sche_fore_ass = r56_sche_fore_ass;
	}
	public BigDecimal getR56_orig_amt() {
		return r56_orig_amt;
	}
	public void setR56_orig_amt(BigDecimal r56_orig_amt) {
		this.r56_orig_amt = r56_orig_amt;
	}
	public BigDecimal getR56_fore_amt() {
		return r56_fore_amt;
	}
	public void setR56_fore_amt(BigDecimal r56_fore_amt) {
		this.r56_fore_amt = r56_fore_amt;
	}
	public BigDecimal getR56_no_of_acc() {
		return r56_no_of_acc;
	}
	public void setR56_no_of_acc(BigDecimal r56_no_of_acc) {
		this.r56_no_of_acc = r56_no_of_acc;
	}
	public String getR57_sche_fore_ass() {
		return r57_sche_fore_ass;
	}
	public void setR57_sche_fore_ass(String r57_sche_fore_ass) {
		this.r57_sche_fore_ass = r57_sche_fore_ass;
	}
	public BigDecimal getR57_orig_amt() {
		return r57_orig_amt;
	}
	public void setR57_orig_amt(BigDecimal r57_orig_amt) {
		this.r57_orig_amt = r57_orig_amt;
	}
	public BigDecimal getR57_fore_amt() {
		return r57_fore_amt;
	}
	public void setR57_fore_amt(BigDecimal r57_fore_amt) {
		this.r57_fore_amt = r57_fore_amt;
	}
	public BigDecimal getR57_no_of_acc() {
		return r57_no_of_acc;
	}
	public void setR57_no_of_acc(BigDecimal r57_no_of_acc) {
		this.r57_no_of_acc = r57_no_of_acc;
	}
	public String getR58_sche_fore_ass() {
		return r58_sche_fore_ass;
	}
	public void setR58_sche_fore_ass(String r58_sche_fore_ass) {
		this.r58_sche_fore_ass = r58_sche_fore_ass;
	}
	public BigDecimal getR58_orig_amt() {
		return r58_orig_amt;
	}
	public void setR58_orig_amt(BigDecimal r58_orig_amt) {
		this.r58_orig_amt = r58_orig_amt;
	}
	public BigDecimal getR58_fore_amt() {
		return r58_fore_amt;
	}
	public void setR58_fore_amt(BigDecimal r58_fore_amt) {
		this.r58_fore_amt = r58_fore_amt;
	}
	public BigDecimal getR58_no_of_acc() {
		return r58_no_of_acc;
	}
	public void setR58_no_of_acc(BigDecimal r58_no_of_acc) {
		this.r58_no_of_acc = r58_no_of_acc;
	}
	public String getR59_sche_fore_ass() {
		return r59_sche_fore_ass;
	}
	public void setR59_sche_fore_ass(String r59_sche_fore_ass) {
		this.r59_sche_fore_ass = r59_sche_fore_ass;
	}
	public BigDecimal getR59_orig_amt() {
		return r59_orig_amt;
	}
	public void setR59_orig_amt(BigDecimal r59_orig_amt) {
		this.r59_orig_amt = r59_orig_amt;
	}
	public BigDecimal getR59_fore_amt() {
		return r59_fore_amt;
	}
	public void setR59_fore_amt(BigDecimal r59_fore_amt) {
		this.r59_fore_amt = r59_fore_amt;
	}
	public BigDecimal getR59_no_of_acc() {
		return r59_no_of_acc;
	}
	public void setR59_no_of_acc(BigDecimal r59_no_of_acc) {
		this.r59_no_of_acc = r59_no_of_acc;
	}
	public String getR60_sche_fore_ass() {
		return r60_sche_fore_ass;
	}
	public void setR60_sche_fore_ass(String r60_sche_fore_ass) {
		this.r60_sche_fore_ass = r60_sche_fore_ass;
	}
	public BigDecimal getR60_orig_amt() {
		return r60_orig_amt;
	}
	public void setR60_orig_amt(BigDecimal r60_orig_amt) {
		this.r60_orig_amt = r60_orig_amt;
	}
	public BigDecimal getR60_fore_amt() {
		return r60_fore_amt;
	}
	public void setR60_fore_amt(BigDecimal r60_fore_amt) {
		this.r60_fore_amt = r60_fore_amt;
	}
	public BigDecimal getR60_no_of_acc() {
		return r60_no_of_acc;
	}
	public void setR60_no_of_acc(BigDecimal r60_no_of_acc) {
		this.r60_no_of_acc = r60_no_of_acc;
	}
	public String getR61_sche_fore_ass() {
		return r61_sche_fore_ass;
	}
	public void setR61_sche_fore_ass(String r61_sche_fore_ass) {
		this.r61_sche_fore_ass = r61_sche_fore_ass;
	}
	public BigDecimal getR61_orig_amt() {
		return r61_orig_amt;
	}
	public void setR61_orig_amt(BigDecimal r61_orig_amt) {
		this.r61_orig_amt = r61_orig_amt;
	}
	public BigDecimal getR61_fore_amt() {
		return r61_fore_amt;
	}
	public void setR61_fore_amt(BigDecimal r61_fore_amt) {
		this.r61_fore_amt = r61_fore_amt;
	}
	public BigDecimal getR61_no_of_acc() {
		return r61_no_of_acc;
	}
	public void setR61_no_of_acc(BigDecimal r61_no_of_acc) {
		this.r61_no_of_acc = r61_no_of_acc;
	}
	public String getR62_sche_fore_ass() {
		return r62_sche_fore_ass;
	}
	public void setR62_sche_fore_ass(String r62_sche_fore_ass) {
		this.r62_sche_fore_ass = r62_sche_fore_ass;
	}
	public BigDecimal getR62_orig_amt() {
		return r62_orig_amt;
	}
	public void setR62_orig_amt(BigDecimal r62_orig_amt) {
		this.r62_orig_amt = r62_orig_amt;
	}
	public BigDecimal getR62_fore_amt() {
		return r62_fore_amt;
	}
	public void setR62_fore_amt(BigDecimal r62_fore_amt) {
		this.r62_fore_amt = r62_fore_amt;
	}
	public BigDecimal getR62_no_of_acc() {
		return r62_no_of_acc;
	}
	public void setR62_no_of_acc(BigDecimal r62_no_of_acc) {
		this.r62_no_of_acc = r62_no_of_acc;
	}
	public String getR63_sche_fore_ass() {
		return r63_sche_fore_ass;
	}
	public void setR63_sche_fore_ass(String r63_sche_fore_ass) {
		this.r63_sche_fore_ass = r63_sche_fore_ass;
	}
	public BigDecimal getR63_orig_amt() {
		return r63_orig_amt;
	}
	public void setR63_orig_amt(BigDecimal r63_orig_amt) {
		this.r63_orig_amt = r63_orig_amt;
	}
	public BigDecimal getR63_fore_amt() {
		return r63_fore_amt;
	}
	public void setR63_fore_amt(BigDecimal r63_fore_amt) {
		this.r63_fore_amt = r63_fore_amt;
	}
	public BigDecimal getR63_no_of_acc() {
		return r63_no_of_acc;
	}
	public void setR63_no_of_acc(BigDecimal r63_no_of_acc) {
		this.r63_no_of_acc = r63_no_of_acc;
	}
	public String getR64_sche_fore_ass() {
		return r64_sche_fore_ass;
	}
	public void setR64_sche_fore_ass(String r64_sche_fore_ass) {
		this.r64_sche_fore_ass = r64_sche_fore_ass;
	}
	public BigDecimal getR64_orig_amt() {
		return r64_orig_amt;
	}
	public void setR64_orig_amt(BigDecimal r64_orig_amt) {
		this.r64_orig_amt = r64_orig_amt;
	}
	public BigDecimal getR64_fore_amt() {
		return r64_fore_amt;
	}
	public void setR64_fore_amt(BigDecimal r64_fore_amt) {
		this.r64_fore_amt = r64_fore_amt;
	}
	public BigDecimal getR64_no_of_acc() {
		return r64_no_of_acc;
	}
	public void setR64_no_of_acc(BigDecimal r64_no_of_acc) {
		this.r64_no_of_acc = r64_no_of_acc;
	}
	public String getR65_sche_fore_ass() {
		return r65_sche_fore_ass;
	}
	public void setR65_sche_fore_ass(String r65_sche_fore_ass) {
		this.r65_sche_fore_ass = r65_sche_fore_ass;
	}
	public BigDecimal getR65_orig_amt() {
		return r65_orig_amt;
	}
	public void setR65_orig_amt(BigDecimal r65_orig_amt) {
		this.r65_orig_amt = r65_orig_amt;
	}
	public BigDecimal getR65_fore_amt() {
		return r65_fore_amt;
	}
	public void setR65_fore_amt(BigDecimal r65_fore_amt) {
		this.r65_fore_amt = r65_fore_amt;
	}
	public BigDecimal getR65_no_of_acc() {
		return r65_no_of_acc;
	}
	public void setR65_no_of_acc(BigDecimal r65_no_of_acc) {
		this.r65_no_of_acc = r65_no_of_acc;
	}
	public String getR66_sche_fore_ass() {
		return r66_sche_fore_ass;
	}
	public void setR66_sche_fore_ass(String r66_sche_fore_ass) {
		this.r66_sche_fore_ass = r66_sche_fore_ass;
	}
	public BigDecimal getR66_orig_amt() {
		return r66_orig_amt;
	}
	public void setR66_orig_amt(BigDecimal r66_orig_amt) {
		this.r66_orig_amt = r66_orig_amt;
	}
	public BigDecimal getR66_fore_amt() {
		return r66_fore_amt;
	}
	public void setR66_fore_amt(BigDecimal r66_fore_amt) {
		this.r66_fore_amt = r66_fore_amt;
	}
	public BigDecimal getR66_no_of_acc() {
		return r66_no_of_acc;
	}
	public void setR66_no_of_acc(BigDecimal r66_no_of_acc) {
		this.r66_no_of_acc = r66_no_of_acc;
	}
	public String getR67_sche_fore_ass() {
		return r67_sche_fore_ass;
	}
	public void setR67_sche_fore_ass(String r67_sche_fore_ass) {
		this.r67_sche_fore_ass = r67_sche_fore_ass;
	}
	public BigDecimal getR67_orig_amt() {
		return r67_orig_amt;
	}
	public void setR67_orig_amt(BigDecimal r67_orig_amt) {
		this.r67_orig_amt = r67_orig_amt;
	}
	public BigDecimal getR67_fore_amt() {
		return r67_fore_amt;
	}
	public void setR67_fore_amt(BigDecimal r67_fore_amt) {
		this.r67_fore_amt = r67_fore_amt;
	}
	public BigDecimal getR67_no_of_acc() {
		return r67_no_of_acc;
	}
	public void setR67_no_of_acc(BigDecimal r67_no_of_acc) {
		this.r67_no_of_acc = r67_no_of_acc;
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
	public Date getReportResubDate() {
		return reportResubDate;
	}
	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
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


//=====================================================
// RESUB summary Q_RLFA2
//=====================================================


public class Q_RLFA2_RESUB_Summary_RowMapper 
        implements RowMapper<Q_RLFA2_RESUB_Summary_Entity> {

    @Override
    public Q_RLFA2_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        Q_RLFA2_RESUB_Summary_Entity obj = new Q_RLFA2_RESUB_Summary_Entity();

// =========================
// R10
// =========================
obj.setR10_sche_fore_ass(rs.getString("r10_sche_fore_ass"));
obj.setR10_orig_amt(rs.getBigDecimal("r10_orig_amt"));
obj.setR10_fore_amt(rs.getBigDecimal("r10_fore_amt"));
obj.setR10_no_of_acc(rs.getBigDecimal("r10_no_of_acc"));


// =========================
// R11
// =========================
obj.setR11_sche_fore_ass(rs.getString("r11_sche_fore_ass"));
obj.setR11_orig_amt(rs.getBigDecimal("r11_orig_amt"));
obj.setR11_fore_amt(rs.getBigDecimal("r11_fore_amt"));
obj.setR11_no_of_acc(rs.getBigDecimal("r11_no_of_acc"));

// =========================
// R12
// =========================
obj.setR12_sche_fore_ass(rs.getString("r12_sche_fore_ass"));
obj.setR12_orig_amt(rs.getBigDecimal("r12_orig_amt"));
obj.setR12_fore_amt(rs.getBigDecimal("r12_fore_amt"));
obj.setR12_no_of_acc(rs.getBigDecimal("r12_no_of_acc"));

// =========================
// R13
// =========================
obj.setR13_sche_fore_ass(rs.getString("r13_sche_fore_ass"));
obj.setR13_orig_amt(rs.getBigDecimal("r13_orig_amt"));
obj.setR13_fore_amt(rs.getBigDecimal("r13_fore_amt"));
obj.setR13_no_of_acc(rs.getBigDecimal("r13_no_of_acc"));

// =========================
// R14
// =========================
obj.setR14_sche_fore_ass(rs.getString("r14_sche_fore_ass"));
obj.setR14_orig_amt(rs.getBigDecimal("r14_orig_amt"));
obj.setR14_fore_amt(rs.getBigDecimal("r14_fore_amt"));
obj.setR14_no_of_acc(rs.getBigDecimal("r14_no_of_acc"));

// =========================
// R15
// =========================
obj.setR15_sche_fore_ass(rs.getString("r15_sche_fore_ass"));
obj.setR15_orig_amt(rs.getBigDecimal("r15_orig_amt"));
obj.setR15_fore_amt(rs.getBigDecimal("r15_fore_amt"));
obj.setR15_no_of_acc(rs.getBigDecimal("r15_no_of_acc"));

// =========================
// R16
// =========================
obj.setR16_sche_fore_ass(rs.getString("r16_sche_fore_ass"));
obj.setR16_orig_amt(rs.getBigDecimal("r16_orig_amt"));
obj.setR16_fore_amt(rs.getBigDecimal("r16_fore_amt"));
obj.setR16_no_of_acc(rs.getBigDecimal("r16_no_of_acc"));

// =========================
// R17
// =========================
obj.setR17_sche_fore_ass(rs.getString("r17_sche_fore_ass"));
obj.setR17_orig_amt(rs.getBigDecimal("r17_orig_amt"));
obj.setR17_fore_amt(rs.getBigDecimal("r17_fore_amt"));
obj.setR17_no_of_acc(rs.getBigDecimal("r17_no_of_acc"));

// =========================
// R18
// =========================
obj.setR18_sche_fore_ass(rs.getString("r18_sche_fore_ass"));
obj.setR18_orig_amt(rs.getBigDecimal("r18_orig_amt"));
obj.setR18_fore_amt(rs.getBigDecimal("r18_fore_amt"));
obj.setR18_no_of_acc(rs.getBigDecimal("r18_no_of_acc"));

// =========================
// R19
// =========================
obj.setR19_sche_fore_ass(rs.getString("r19_sche_fore_ass"));
obj.setR19_orig_amt(rs.getBigDecimal("r19_orig_amt"));
obj.setR19_fore_amt(rs.getBigDecimal("r19_fore_amt"));
obj.setR19_no_of_acc(rs.getBigDecimal("r19_no_of_acc"));

// =========================
// R20
// =========================
obj.setR20_sche_fore_ass(rs.getString("r20_sche_fore_ass"));
obj.setR20_orig_amt(rs.getBigDecimal("r20_orig_amt"));
obj.setR20_fore_amt(rs.getBigDecimal("r20_fore_amt"));
obj.setR20_no_of_acc(rs.getBigDecimal("r20_no_of_acc"));

// =========================
// R21
// =========================
obj.setR21_sche_fore_ass(rs.getString("r21_sche_fore_ass"));
obj.setR21_orig_amt(rs.getBigDecimal("r21_orig_amt"));
obj.setR21_fore_amt(rs.getBigDecimal("r21_fore_amt"));
obj.setR21_no_of_acc(rs.getBigDecimal("r21_no_of_acc"));

// =========================
// R22
// =========================
obj.setR22_sche_fore_ass(rs.getString("r22_sche_fore_ass"));
obj.setR22_orig_amt(rs.getBigDecimal("r22_orig_amt"));
obj.setR22_fore_amt(rs.getBigDecimal("r22_fore_amt"));
obj.setR22_no_of_acc(rs.getBigDecimal("r22_no_of_acc"));

// =========================
// R23
// =========================
obj.setR23_sche_fore_ass(rs.getString("r23_sche_fore_ass"));
obj.setR23_orig_amt(rs.getBigDecimal("r23_orig_amt"));
obj.setR23_fore_amt(rs.getBigDecimal("r23_fore_amt"));
obj.setR23_no_of_acc(rs.getBigDecimal("r23_no_of_acc"));

// =========================
// R24
// =========================
obj.setR24_sche_fore_ass(rs.getString("r24_sche_fore_ass"));
obj.setR24_orig_amt(rs.getBigDecimal("r24_orig_amt"));
obj.setR24_fore_amt(rs.getBigDecimal("r24_fore_amt"));
obj.setR24_no_of_acc(rs.getBigDecimal("r24_no_of_acc"));

// =========================
// R25
// =========================
obj.setR25_sche_fore_ass(rs.getString("r25_sche_fore_ass"));
obj.setR25_orig_amt(rs.getBigDecimal("r25_orig_amt"));
obj.setR25_fore_amt(rs.getBigDecimal("r25_fore_amt"));
obj.setR25_no_of_acc(rs.getBigDecimal("r25_no_of_acc"));

// =========================
// R26
// =========================
obj.setR26_sche_fore_ass(rs.getString("r26_sche_fore_ass"));
obj.setR26_orig_amt(rs.getBigDecimal("r26_orig_amt"));
obj.setR26_fore_amt(rs.getBigDecimal("r26_fore_amt"));
obj.setR26_no_of_acc(rs.getBigDecimal("r26_no_of_acc"));

// =========================
// R27
// =========================
obj.setR27_sche_fore_ass(rs.getString("r27_sche_fore_ass"));
obj.setR27_orig_amt(rs.getBigDecimal("r27_orig_amt"));
obj.setR27_fore_amt(rs.getBigDecimal("r27_fore_amt"));
obj.setR27_no_of_acc(rs.getBigDecimal("r27_no_of_acc"));

// =========================
// R28
// =========================
obj.setR28_sche_fore_ass(rs.getString("r28_sche_fore_ass"));
obj.setR28_orig_amt(rs.getBigDecimal("r28_orig_amt"));
obj.setR28_fore_amt(rs.getBigDecimal("r28_fore_amt"));
obj.setR28_no_of_acc(rs.getBigDecimal("r28_no_of_acc"));

// =========================
// R29
// =========================
obj.setR29_sche_fore_ass(rs.getString("r29_sche_fore_ass"));
obj.setR29_orig_amt(rs.getBigDecimal("r29_orig_amt"));
obj.setR29_fore_amt(rs.getBigDecimal("r29_fore_amt"));
obj.setR29_no_of_acc(rs.getBigDecimal("r29_no_of_acc"));

// =========================
// R30
// =========================
obj.setR30_sche_fore_ass(rs.getString("r30_sche_fore_ass"));
obj.setR30_orig_amt(rs.getBigDecimal("r30_orig_amt"));
obj.setR30_fore_amt(rs.getBigDecimal("r30_fore_amt"));
obj.setR30_no_of_acc(rs.getBigDecimal("r30_no_of_acc"));


// =========================
// R31
// =========================
obj.setR31_sche_fore_ass(rs.getString("r31_sche_fore_ass"));
obj.setR31_orig_amt(rs.getBigDecimal("r31_orig_amt"));
obj.setR31_fore_amt(rs.getBigDecimal("r31_fore_amt"));
obj.setR31_no_of_acc(rs.getBigDecimal("r31_no_of_acc"));

// =========================
// R32
// =========================
obj.setR32_sche_fore_ass(rs.getString("r32_sche_fore_ass"));
obj.setR32_orig_amt(rs.getBigDecimal("r32_orig_amt"));
obj.setR32_fore_amt(rs.getBigDecimal("r32_fore_amt"));
obj.setR32_no_of_acc(rs.getBigDecimal("r32_no_of_acc"));

// =========================
// R33
// =========================
obj.setR33_sche_fore_ass(rs.getString("r33_sche_fore_ass"));
obj.setR33_orig_amt(rs.getBigDecimal("r33_orig_amt"));
obj.setR33_fore_amt(rs.getBigDecimal("r33_fore_amt"));
obj.setR33_no_of_acc(rs.getBigDecimal("r33_no_of_acc"));

// =========================
// R34
// =========================
obj.setR34_sche_fore_ass(rs.getString("r34_sche_fore_ass"));
obj.setR34_orig_amt(rs.getBigDecimal("r34_orig_amt"));
obj.setR34_fore_amt(rs.getBigDecimal("r34_fore_amt"));
obj.setR34_no_of_acc(rs.getBigDecimal("r34_no_of_acc"));

// =========================
// R35
// =========================
obj.setR35_sche_fore_ass(rs.getString("r35_sche_fore_ass"));
obj.setR35_orig_amt(rs.getBigDecimal("r35_orig_amt"));
obj.setR35_fore_amt(rs.getBigDecimal("r35_fore_amt"));
obj.setR35_no_of_acc(rs.getBigDecimal("r35_no_of_acc"));

// =========================
// R36
// =========================
obj.setR36_sche_fore_ass(rs.getString("r36_sche_fore_ass"));
obj.setR36_orig_amt(rs.getBigDecimal("r36_orig_amt"));
obj.setR36_fore_amt(rs.getBigDecimal("r36_fore_amt"));
obj.setR36_no_of_acc(rs.getBigDecimal("r36_no_of_acc"));

// =========================
// R37
// =========================
obj.setR37_sche_fore_ass(rs.getString("r37_sche_fore_ass"));
obj.setR37_orig_amt(rs.getBigDecimal("r37_orig_amt"));
obj.setR37_fore_amt(rs.getBigDecimal("r37_fore_amt"));
obj.setR37_no_of_acc(rs.getBigDecimal("r37_no_of_acc"));

// =========================
// R38
// =========================
obj.setR38_sche_fore_ass(rs.getString("r38_sche_fore_ass"));
obj.setR38_orig_amt(rs.getBigDecimal("r38_orig_amt"));
obj.setR38_fore_amt(rs.getBigDecimal("r38_fore_amt"));
obj.setR38_no_of_acc(rs.getBigDecimal("r38_no_of_acc"));

// =========================
// R39
// =========================
obj.setR39_sche_fore_ass(rs.getString("r39_sche_fore_ass"));
obj.setR39_orig_amt(rs.getBigDecimal("r39_orig_amt"));
obj.setR39_fore_amt(rs.getBigDecimal("r39_fore_amt"));
obj.setR39_no_of_acc(rs.getBigDecimal("r39_no_of_acc"));

// =========================
// R40
// =========================
obj.setR40_sche_fore_ass(rs.getString("r40_sche_fore_ass"));
obj.setR40_orig_amt(rs.getBigDecimal("r40_orig_amt"));
obj.setR40_fore_amt(rs.getBigDecimal("r40_fore_amt"));
obj.setR40_no_of_acc(rs.getBigDecimal("r40_no_of_acc"));

// =========================
// R41
// =========================
obj.setR41_sche_fore_ass(rs.getString("r41_sche_fore_ass"));
obj.setR41_orig_amt(rs.getBigDecimal("r41_orig_amt"));
obj.setR41_fore_amt(rs.getBigDecimal("r41_fore_amt"));
obj.setR41_no_of_acc(rs.getBigDecimal("r41_no_of_acc"));

// =========================
// R42
// =========================
obj.setR42_sche_fore_ass(rs.getString("r42_sche_fore_ass"));
obj.setR42_orig_amt(rs.getBigDecimal("r42_orig_amt"));
obj.setR42_fore_amt(rs.getBigDecimal("r42_fore_amt"));
obj.setR42_no_of_acc(rs.getBigDecimal("r42_no_of_acc"));

// =========================
// R43
// =========================
obj.setR43_sche_fore_ass(rs.getString("r43_sche_fore_ass"));
obj.setR43_orig_amt(rs.getBigDecimal("r43_orig_amt"));
obj.setR43_fore_amt(rs.getBigDecimal("r43_fore_amt"));
obj.setR43_no_of_acc(rs.getBigDecimal("r43_no_of_acc"));

// =========================
// R44
// =========================
obj.setR44_sche_fore_ass(rs.getString("r44_sche_fore_ass"));
obj.setR44_orig_amt(rs.getBigDecimal("r44_orig_amt"));
obj.setR44_fore_amt(rs.getBigDecimal("r44_fore_amt"));
obj.setR44_no_of_acc(rs.getBigDecimal("r44_no_of_acc"));

// =========================
// R45
// =========================
obj.setR45_sche_fore_ass(rs.getString("r45_sche_fore_ass"));
obj.setR45_orig_amt(rs.getBigDecimal("r45_orig_amt"));
obj.setR45_fore_amt(rs.getBigDecimal("r45_fore_amt"));
obj.setR45_no_of_acc(rs.getBigDecimal("r45_no_of_acc"));

// =========================
// R46
// =========================
obj.setR46_sche_fore_ass(rs.getString("r46_sche_fore_ass"));
obj.setR46_orig_amt(rs.getBigDecimal("r46_orig_amt"));
obj.setR46_fore_amt(rs.getBigDecimal("r46_fore_amt"));
obj.setR46_no_of_acc(rs.getBigDecimal("r46_no_of_acc"));

// =========================
// R47
// =========================
obj.setR47_sche_fore_ass(rs.getString("r47_sche_fore_ass"));
obj.setR47_orig_amt(rs.getBigDecimal("r47_orig_amt"));
obj.setR47_fore_amt(rs.getBigDecimal("r47_fore_amt"));
obj.setR47_no_of_acc(rs.getBigDecimal("r47_no_of_acc"));

// =========================
// R48
// =========================
obj.setR48_sche_fore_ass(rs.getString("r48_sche_fore_ass"));
obj.setR48_orig_amt(rs.getBigDecimal("r48_orig_amt"));
obj.setR48_fore_amt(rs.getBigDecimal("r48_fore_amt"));
obj.setR48_no_of_acc(rs.getBigDecimal("r48_no_of_acc"));

// =========================
// R49
// =========================
obj.setR49_sche_fore_ass(rs.getString("r49_sche_fore_ass"));
obj.setR49_orig_amt(rs.getBigDecimal("r49_orig_amt"));
obj.setR49_fore_amt(rs.getBigDecimal("r49_fore_amt"));
obj.setR49_no_of_acc(rs.getBigDecimal("r49_no_of_acc"));

// =========================
// R50
// =========================
obj.setR50_sche_fore_ass(rs.getString("r50_sche_fore_ass"));
obj.setR50_orig_amt(rs.getBigDecimal("r50_orig_amt"));
obj.setR50_fore_amt(rs.getBigDecimal("r50_fore_amt"));
obj.setR50_no_of_acc(rs.getBigDecimal("r50_no_of_acc"));

// =========================
// R51
// =========================
obj.setR51_sche_fore_ass(rs.getString("r51_sche_fore_ass"));
obj.setR51_orig_amt(rs.getBigDecimal("r51_orig_amt"));
obj.setR51_fore_amt(rs.getBigDecimal("r51_fore_amt"));
obj.setR51_no_of_acc(rs.getBigDecimal("r51_no_of_acc"));

// =========================
// R52
// =========================
obj.setR52_sche_fore_ass(rs.getString("r52_sche_fore_ass"));
obj.setR52_orig_amt(rs.getBigDecimal("r52_orig_amt"));
obj.setR52_fore_amt(rs.getBigDecimal("r52_fore_amt"));
obj.setR52_no_of_acc(rs.getBigDecimal("r52_no_of_acc"));

// =========================
// R53
// =========================
obj.setR53_sche_fore_ass(rs.getString("r53_sche_fore_ass"));
obj.setR53_orig_amt(rs.getBigDecimal("r53_orig_amt"));
obj.setR53_fore_amt(rs.getBigDecimal("r53_fore_amt"));
obj.setR53_no_of_acc(rs.getBigDecimal("r53_no_of_acc"));

// =========================
// R54
// =========================
obj.setR54_sche_fore_ass(rs.getString("r54_sche_fore_ass"));
obj.setR54_orig_amt(rs.getBigDecimal("r54_orig_amt"));
obj.setR54_fore_amt(rs.getBigDecimal("r54_fore_amt"));
obj.setR54_no_of_acc(rs.getBigDecimal("r54_no_of_acc"));

// =========================
// R55
// =========================
obj.setR55_sche_fore_ass(rs.getString("r55_sche_fore_ass"));
obj.setR55_orig_amt(rs.getBigDecimal("r55_orig_amt"));
obj.setR55_fore_amt(rs.getBigDecimal("r55_fore_amt"));
obj.setR55_no_of_acc(rs.getBigDecimal("r55_no_of_acc"));

// =========================
// R56
// =========================
obj.setR56_sche_fore_ass(rs.getString("r56_sche_fore_ass"));
obj.setR56_orig_amt(rs.getBigDecimal("r56_orig_amt"));
obj.setR56_fore_amt(rs.getBigDecimal("r56_fore_amt"));
obj.setR56_no_of_acc(rs.getBigDecimal("r56_no_of_acc"));

// =========================
// R57
// =========================
obj.setR57_sche_fore_ass(rs.getString("r57_sche_fore_ass"));
obj.setR57_orig_amt(rs.getBigDecimal("r57_orig_amt"));
obj.setR57_fore_amt(rs.getBigDecimal("r57_fore_amt"));
obj.setR57_no_of_acc(rs.getBigDecimal("r57_no_of_acc"));

// =========================
// R58
// =========================
obj.setR58_sche_fore_ass(rs.getString("r58_sche_fore_ass"));
obj.setR58_orig_amt(rs.getBigDecimal("r58_orig_amt"));
obj.setR58_fore_amt(rs.getBigDecimal("r58_fore_amt"));
obj.setR58_no_of_acc(rs.getBigDecimal("r58_no_of_acc"));

// =========================
// R59
// =========================
obj.setR59_sche_fore_ass(rs.getString("r59_sche_fore_ass"));
obj.setR59_orig_amt(rs.getBigDecimal("r59_orig_amt"));
obj.setR59_fore_amt(rs.getBigDecimal("r59_fore_amt"));
obj.setR59_no_of_acc(rs.getBigDecimal("r59_no_of_acc"));

// =========================
// R60
// =========================
obj.setR60_sche_fore_ass(rs.getString("r60_sche_fore_ass"));
obj.setR60_orig_amt(rs.getBigDecimal("r60_orig_amt"));
obj.setR60_fore_amt(rs.getBigDecimal("r60_fore_amt"));
obj.setR60_no_of_acc(rs.getBigDecimal("r60_no_of_acc"));

// =========================
// R61
// =========================
obj.setR61_sche_fore_ass(rs.getString("r61_sche_fore_ass"));
obj.setR61_orig_amt(rs.getBigDecimal("r61_orig_amt"));
obj.setR61_fore_amt(rs.getBigDecimal("r61_fore_amt"));
obj.setR61_no_of_acc(rs.getBigDecimal("r61_no_of_acc"));

// =========================
// R62
// =========================
obj.setR62_sche_fore_ass(rs.getString("r62_sche_fore_ass"));
obj.setR62_orig_amt(rs.getBigDecimal("r62_orig_amt"));
obj.setR62_fore_amt(rs.getBigDecimal("r62_fore_amt"));
obj.setR62_no_of_acc(rs.getBigDecimal("r62_no_of_acc"));

// =========================
// R63
// =========================
obj.setR63_sche_fore_ass(rs.getString("r63_sche_fore_ass"));
obj.setR63_orig_amt(rs.getBigDecimal("r63_orig_amt"));
obj.setR63_fore_amt(rs.getBigDecimal("r63_fore_amt"));
obj.setR63_no_of_acc(rs.getBigDecimal("r63_no_of_acc"));

// =========================
// R64
// =========================
obj.setR64_sche_fore_ass(rs.getString("r64_sche_fore_ass"));
obj.setR64_orig_amt(rs.getBigDecimal("r64_orig_amt"));
obj.setR64_fore_amt(rs.getBigDecimal("r64_fore_amt"));
obj.setR64_no_of_acc(rs.getBigDecimal("r64_no_of_acc"));

// =========================
// R65
// =========================
obj.setR65_sche_fore_ass(rs.getString("r65_sche_fore_ass"));
obj.setR65_orig_amt(rs.getBigDecimal("r65_orig_amt"));
obj.setR65_fore_amt(rs.getBigDecimal("r65_fore_amt"));
obj.setR65_no_of_acc(rs.getBigDecimal("r65_no_of_acc"));

// =========================
// R66
// =========================
obj.setR66_sche_fore_ass(rs.getString("r66_sche_fore_ass"));
obj.setR66_orig_amt(rs.getBigDecimal("r66_orig_amt"));
obj.setR66_fore_amt(rs.getBigDecimal("r66_fore_amt"));
obj.setR66_no_of_acc(rs.getBigDecimal("r66_no_of_acc"));

// =========================
// R67
// =========================
obj.setR67_sche_fore_ass(rs.getString("r67_sche_fore_ass"));
obj.setR67_orig_amt(rs.getBigDecimal("r67_orig_amt"));
obj.setR67_fore_amt(rs.getBigDecimal("r67_fore_amt"));
obj.setR67_no_of_acc(rs.getBigDecimal("r67_no_of_acc"));


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

public class Q_RLFA2_RESUB_Summary_Entity {

   
private String	r10_sche_fore_ass;
	private BigDecimal	r10_orig_amt;
	private BigDecimal	r10_fore_amt;
	private BigDecimal	r10_no_of_acc;
	
	private String	r11_sche_fore_ass;
	private BigDecimal	r11_orig_amt;
	private BigDecimal	r11_fore_amt;
	private BigDecimal	r11_no_of_acc;
	
	private String	r12_sche_fore_ass;
	private BigDecimal	r12_orig_amt;
	private BigDecimal	r12_fore_amt;
	private BigDecimal	r12_no_of_acc;
	
	private String	r13_sche_fore_ass;
	private BigDecimal	r13_orig_amt;
	private BigDecimal	r13_fore_amt;
	private BigDecimal	r13_no_of_acc;
	
	private String	r14_sche_fore_ass;
	private BigDecimal	r14_orig_amt;
	private BigDecimal	r14_fore_amt;
	private BigDecimal	r14_no_of_acc;
	
	private String	r15_sche_fore_ass;
	private BigDecimal	r15_orig_amt;
	private BigDecimal	r15_fore_amt;
	private BigDecimal	r15_no_of_acc;
	
	private String	r16_sche_fore_ass;
	private BigDecimal	r16_orig_amt;
	private BigDecimal	r16_fore_amt;
	private BigDecimal	r16_no_of_acc;
	
	private String	r17_sche_fore_ass;
	private BigDecimal	r17_orig_amt;
	private BigDecimal	r17_fore_amt;
	private BigDecimal	r17_no_of_acc;
	
	private String	r18_sche_fore_ass;
	private BigDecimal	r18_orig_amt;
	private BigDecimal	r18_fore_amt;
	private BigDecimal	r18_no_of_acc;
	
	private String	r19_sche_fore_ass;
	private BigDecimal	r19_orig_amt;
	private BigDecimal	r19_fore_amt;
	private BigDecimal	r19_no_of_acc;
	
	private String	r20_sche_fore_ass;
	private BigDecimal	r20_orig_amt;
	private BigDecimal	r20_fore_amt;
	private BigDecimal	r20_no_of_acc;
	
	private String	r21_sche_fore_ass;
	private BigDecimal	r21_orig_amt;
	private BigDecimal	r21_fore_amt;
	private BigDecimal	r21_no_of_acc;
	
	private String	r22_sche_fore_ass;
	private BigDecimal	r22_orig_amt;
	private BigDecimal	r22_fore_amt;
	private BigDecimal	r22_no_of_acc;
	
	private String	r23_sche_fore_ass;
	private BigDecimal	r23_orig_amt;
	private BigDecimal	r23_fore_amt;
	private BigDecimal	r23_no_of_acc;
	
	private String	r24_sche_fore_ass;
	private BigDecimal	r24_orig_amt;
	private BigDecimal	r24_fore_amt;
	private BigDecimal	r24_no_of_acc;
	
	private String	r25_sche_fore_ass;
	private BigDecimal	r25_orig_amt;
	private BigDecimal	r25_fore_amt;
	private BigDecimal	r25_no_of_acc;
	
	private String	r26_sche_fore_ass;
	private BigDecimal	r26_orig_amt;
	private BigDecimal	r26_fore_amt;
	private BigDecimal	r26_no_of_acc;
	
	private String	r27_sche_fore_ass;
	private BigDecimal	r27_orig_amt;
	private BigDecimal	r27_fore_amt;
	private BigDecimal	r27_no_of_acc;
	
	private String	r28_sche_fore_ass;
	private BigDecimal	r28_orig_amt;
	private BigDecimal	r28_fore_amt;
	private BigDecimal	r28_no_of_acc;
	
	private String	r29_sche_fore_ass;
	private BigDecimal	r29_orig_amt;
	private BigDecimal	r29_fore_amt;
	private BigDecimal	r29_no_of_acc;
	
	private String	r30_sche_fore_ass;
	private BigDecimal	r30_orig_amt;
	private BigDecimal	r30_fore_amt;
	private BigDecimal	r30_no_of_acc;
	
	private String	r31_sche_fore_ass;
	private BigDecimal	r31_orig_amt;
	private BigDecimal	r31_fore_amt;
	private BigDecimal	r31_no_of_acc;
	
	private String	r32_sche_fore_ass;
	private BigDecimal	r32_orig_amt;
	private BigDecimal	r32_fore_amt;
	private BigDecimal	r32_no_of_acc;
	
	private String	r33_sche_fore_ass;
	private BigDecimal	r33_orig_amt;
	private BigDecimal	r33_fore_amt;
	private BigDecimal	r33_no_of_acc;
	
	private String	r34_sche_fore_ass;
	private BigDecimal	r34_orig_amt;
	private BigDecimal	r34_fore_amt;
	private BigDecimal	r34_no_of_acc;
	
	private String	r35_sche_fore_ass;
	private BigDecimal	r35_orig_amt;
	private BigDecimal	r35_fore_amt;
	private BigDecimal	r35_no_of_acc;
	
	private String	r36_sche_fore_ass;
	private BigDecimal	r36_orig_amt;
	private BigDecimal	r36_fore_amt;
	private BigDecimal	r36_no_of_acc;
	
	private String	r37_sche_fore_ass;
	private BigDecimal	r37_orig_amt;
	private BigDecimal	r37_fore_amt;
	private BigDecimal	r37_no_of_acc;
	
	private String	r38_sche_fore_ass;
	private BigDecimal	r38_orig_amt;
	private BigDecimal	r38_fore_amt;
	private BigDecimal	r38_no_of_acc;
	
	private String	r39_sche_fore_ass;
	private BigDecimal	r39_orig_amt;
	private BigDecimal	r39_fore_amt;
	private BigDecimal	r39_no_of_acc;
	
	private String	r40_sche_fore_ass;
	private BigDecimal	r40_orig_amt;
	private BigDecimal	r40_fore_amt;
	private BigDecimal	r40_no_of_acc;
	
	private String	r41_sche_fore_ass;
	private BigDecimal	r41_orig_amt;
	private BigDecimal	r41_fore_amt;
	private BigDecimal	r41_no_of_acc;
	
	private String	r42_sche_fore_ass;
	private BigDecimal	r42_orig_amt;
	private BigDecimal	r42_fore_amt;
	private BigDecimal	r42_no_of_acc;
	
	private String	r43_sche_fore_ass;
	private BigDecimal	r43_orig_amt;
	private BigDecimal	r43_fore_amt;
	private BigDecimal	r43_no_of_acc;
	
	private String	r44_sche_fore_ass;
	private BigDecimal	r44_orig_amt;
	private BigDecimal	r44_fore_amt;
	private BigDecimal	r44_no_of_acc;
	
	private String	r45_sche_fore_ass;
	private BigDecimal	r45_orig_amt;
	private BigDecimal	r45_fore_amt;
	private BigDecimal	r45_no_of_acc;
	
	private String	r46_sche_fore_ass;
	private BigDecimal	r46_orig_amt;
	private BigDecimal	r46_fore_amt;
	private BigDecimal	r46_no_of_acc;
	
	private String	r47_sche_fore_ass;
	private BigDecimal	r47_orig_amt;
	private BigDecimal	r47_fore_amt;
	private BigDecimal	r47_no_of_acc;
	
	private String	r48_sche_fore_ass;
	private BigDecimal	r48_orig_amt;
	private BigDecimal	r48_fore_amt;
	private BigDecimal	r48_no_of_acc;
	
	private String	r49_sche_fore_ass;
	private BigDecimal	r49_orig_amt;
	private BigDecimal	r49_fore_amt;
	private BigDecimal	r49_no_of_acc;
	
	private String	r50_sche_fore_ass;
	private BigDecimal	r50_orig_amt;
	private BigDecimal	r50_fore_amt;
	private BigDecimal	r50_no_of_acc;
	
	private String	r51_sche_fore_ass;
	private BigDecimal	r51_orig_amt;
	private BigDecimal	r51_fore_amt;
	private BigDecimal	r51_no_of_acc;
	
	private String	r52_sche_fore_ass;
	private BigDecimal	r52_orig_amt;
	private BigDecimal	r52_fore_amt;
	private BigDecimal	r52_no_of_acc;
	
	private String	r53_sche_fore_ass;
	private BigDecimal	r53_orig_amt;
	private BigDecimal	r53_fore_amt;
	private BigDecimal	r53_no_of_acc;
	
	private String	r54_sche_fore_ass;
	private BigDecimal	r54_orig_amt;
	private BigDecimal	r54_fore_amt;
	private BigDecimal	r54_no_of_acc;
	
	private String	r55_sche_fore_ass;
	private BigDecimal	r55_orig_amt;
	private BigDecimal	r55_fore_amt;
	private BigDecimal	r55_no_of_acc;
	
	private String	r56_sche_fore_ass;
	private BigDecimal	r56_orig_amt;
	private BigDecimal	r56_fore_amt;
	private BigDecimal	r56_no_of_acc;
	
	private String	r57_sche_fore_ass;
	private BigDecimal	r57_orig_amt;
	private BigDecimal	r57_fore_amt;
	private BigDecimal	r57_no_of_acc;
	
	private String	r58_sche_fore_ass;
	private BigDecimal	r58_orig_amt;
	private BigDecimal	r58_fore_amt;
	private BigDecimal	r58_no_of_acc;
	
	private String	r59_sche_fore_ass;
	private BigDecimal	r59_orig_amt;
	private BigDecimal	r59_fore_amt;
	private BigDecimal	r59_no_of_acc;
	
	private String	r60_sche_fore_ass;
	private BigDecimal	r60_orig_amt;
	private BigDecimal	r60_fore_amt;
	private BigDecimal	r60_no_of_acc;
	
	private String	r61_sche_fore_ass;
	private BigDecimal	r61_orig_amt;
	private BigDecimal	r61_fore_amt;
	private BigDecimal	r61_no_of_acc;
	
	private String	r62_sche_fore_ass;
	private BigDecimal	r62_orig_amt;
	private BigDecimal	r62_fore_amt;
	private BigDecimal	r62_no_of_acc;
	
	private String	r63_sche_fore_ass;
	private BigDecimal	r63_orig_amt;
	private BigDecimal	r63_fore_amt;
	private BigDecimal	r63_no_of_acc;
	
	// R64
	private String     r64_sche_fore_ass;
	private BigDecimal r64_orig_amt;
	private BigDecimal r64_fore_amt;
	private BigDecimal r64_no_of_acc;

	// R65
	private String     r65_sche_fore_ass;
	private BigDecimal r65_orig_amt;
	private BigDecimal r65_fore_amt;
	private BigDecimal r65_no_of_acc;

	// R66
	private String     r66_sche_fore_ass;
	private BigDecimal r66_orig_amt;
	private BigDecimal r66_fore_amt;
	private BigDecimal r66_no_of_acc;

	// R67
	private String     r67_sche_fore_ass;
	private BigDecimal r67_orig_amt;
	private BigDecimal r67_fore_amt;
	private BigDecimal r67_no_of_acc;

	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id	
	private Date	report_date;
	
	@Id
	private BigDecimal	report_version;
	
	@Column(name = "REPORT_RESUBDATE")
    private Date reportResubDate;
	
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	 del_flg;
	
	
	public String getR10_sche_fore_ass() {
		return r10_sche_fore_ass;
	}
	public void setR10_sche_fore_ass(String r10_sche_fore_ass) {
		this.r10_sche_fore_ass = r10_sche_fore_ass;
	}
	public BigDecimal getR10_orig_amt() {
		return r10_orig_amt;
	}
	public void setR10_orig_amt(BigDecimal r10_orig_amt) {
		this.r10_orig_amt = r10_orig_amt;
	}
	public BigDecimal getR10_fore_amt() {
		return r10_fore_amt;
	}
	public void setR10_fore_amt(BigDecimal r10_fore_amt) {
		this.r10_fore_amt = r10_fore_amt;
	}
	public BigDecimal getR10_no_of_acc() {
		return r10_no_of_acc;
	}
	public void setR10_no_of_acc(BigDecimal r10_no_of_acc) {
		this.r10_no_of_acc = r10_no_of_acc;
	}
	public String getR11_sche_fore_ass() {
		return r11_sche_fore_ass;
	}
	public void setR11_sche_fore_ass(String r11_sche_fore_ass) {
		this.r11_sche_fore_ass = r11_sche_fore_ass;
	}
	public BigDecimal getR11_orig_amt() {
		return r11_orig_amt;
	}
	public void setR11_orig_amt(BigDecimal r11_orig_amt) {
		this.r11_orig_amt = r11_orig_amt;
	}
	public BigDecimal getR11_fore_amt() {
		return r11_fore_amt;
	}
	public void setR11_fore_amt(BigDecimal r11_fore_amt) {
		this.r11_fore_amt = r11_fore_amt;
	}
	public BigDecimal getR11_no_of_acc() {
		return r11_no_of_acc;
	}
	public void setR11_no_of_acc(BigDecimal r11_no_of_acc) {
		this.r11_no_of_acc = r11_no_of_acc;
	}
	public String getR12_sche_fore_ass() {
		return r12_sche_fore_ass;
	}
	public void setR12_sche_fore_ass(String r12_sche_fore_ass) {
		this.r12_sche_fore_ass = r12_sche_fore_ass;
	}
	public BigDecimal getR12_orig_amt() {
		return r12_orig_amt;
	}
	public void setR12_orig_amt(BigDecimal r12_orig_amt) {
		this.r12_orig_amt = r12_orig_amt;
	}
	public BigDecimal getR12_fore_amt() {
		return r12_fore_amt;
	}
	public void setR12_fore_amt(BigDecimal r12_fore_amt) {
		this.r12_fore_amt = r12_fore_amt;
	}
	public BigDecimal getR12_no_of_acc() {
		return r12_no_of_acc;
	}
	public void setR12_no_of_acc(BigDecimal r12_no_of_acc) {
		this.r12_no_of_acc = r12_no_of_acc;
	}
	public String getR13_sche_fore_ass() {
		return r13_sche_fore_ass;
	}
	public void setR13_sche_fore_ass(String r13_sche_fore_ass) {
		this.r13_sche_fore_ass = r13_sche_fore_ass;
	}
	public BigDecimal getR13_orig_amt() {
		return r13_orig_amt;
	}
	public void setR13_orig_amt(BigDecimal r13_orig_amt) {
		this.r13_orig_amt = r13_orig_amt;
	}
	public BigDecimal getR13_fore_amt() {
		return r13_fore_amt;
	}
	public void setR13_fore_amt(BigDecimal r13_fore_amt) {
		this.r13_fore_amt = r13_fore_amt;
	}
	public BigDecimal getR13_no_of_acc() {
		return r13_no_of_acc;
	}
	public void setR13_no_of_acc(BigDecimal r13_no_of_acc) {
		this.r13_no_of_acc = r13_no_of_acc;
	}
	public String getR14_sche_fore_ass() {
		return r14_sche_fore_ass;
	}
	public void setR14_sche_fore_ass(String r14_sche_fore_ass) {
		this.r14_sche_fore_ass = r14_sche_fore_ass;
	}
	public BigDecimal getR14_orig_amt() {
		return r14_orig_amt;
	}
	public void setR14_orig_amt(BigDecimal r14_orig_amt) {
		this.r14_orig_amt = r14_orig_amt;
	}
	public BigDecimal getR14_fore_amt() {
		return r14_fore_amt;
	}
	public void setR14_fore_amt(BigDecimal r14_fore_amt) {
		this.r14_fore_amt = r14_fore_amt;
	}
	public BigDecimal getR14_no_of_acc() {
		return r14_no_of_acc;
	}
	public void setR14_no_of_acc(BigDecimal r14_no_of_acc) {
		this.r14_no_of_acc = r14_no_of_acc;
	}
	public String getR15_sche_fore_ass() {
		return r15_sche_fore_ass;
	}
	public void setR15_sche_fore_ass(String r15_sche_fore_ass) {
		this.r15_sche_fore_ass = r15_sche_fore_ass;
	}
	public BigDecimal getR15_orig_amt() {
		return r15_orig_amt;
	}
	public void setR15_orig_amt(BigDecimal r15_orig_amt) {
		this.r15_orig_amt = r15_orig_amt;
	}
	public BigDecimal getR15_fore_amt() {
		return r15_fore_amt;
	}
	public void setR15_fore_amt(BigDecimal r15_fore_amt) {
		this.r15_fore_amt = r15_fore_amt;
	}
	public BigDecimal getR15_no_of_acc() {
		return r15_no_of_acc;
	}
	public void setR15_no_of_acc(BigDecimal r15_no_of_acc) {
		this.r15_no_of_acc = r15_no_of_acc;
	}
	public String getR16_sche_fore_ass() {
		return r16_sche_fore_ass;
	}
	public void setR16_sche_fore_ass(String r16_sche_fore_ass) {
		this.r16_sche_fore_ass = r16_sche_fore_ass;
	}
	public BigDecimal getR16_orig_amt() {
		return r16_orig_amt;
	}
	public void setR16_orig_amt(BigDecimal r16_orig_amt) {
		this.r16_orig_amt = r16_orig_amt;
	}
	public BigDecimal getR16_fore_amt() {
		return r16_fore_amt;
	}
	public void setR16_fore_amt(BigDecimal r16_fore_amt) {
		this.r16_fore_amt = r16_fore_amt;
	}
	public BigDecimal getR16_no_of_acc() {
		return r16_no_of_acc;
	}
	public void setR16_no_of_acc(BigDecimal r16_no_of_acc) {
		this.r16_no_of_acc = r16_no_of_acc;
	}
	public String getR17_sche_fore_ass() {
		return r17_sche_fore_ass;
	}
	public void setR17_sche_fore_ass(String r17_sche_fore_ass) {
		this.r17_sche_fore_ass = r17_sche_fore_ass;
	}
	public BigDecimal getR17_orig_amt() {
		return r17_orig_amt;
	}
	public void setR17_orig_amt(BigDecimal r17_orig_amt) {
		this.r17_orig_amt = r17_orig_amt;
	}
	public BigDecimal getR17_fore_amt() {
		return r17_fore_amt;
	}
	public void setR17_fore_amt(BigDecimal r17_fore_amt) {
		this.r17_fore_amt = r17_fore_amt;
	}
	public BigDecimal getR17_no_of_acc() {
		return r17_no_of_acc;
	}
	public void setR17_no_of_acc(BigDecimal r17_no_of_acc) {
		this.r17_no_of_acc = r17_no_of_acc;
	}
	public String getR18_sche_fore_ass() {
		return r18_sche_fore_ass;
	}
	public void setR18_sche_fore_ass(String r18_sche_fore_ass) {
		this.r18_sche_fore_ass = r18_sche_fore_ass;
	}
	public BigDecimal getR18_orig_amt() {
		return r18_orig_amt;
	}
	public void setR18_orig_amt(BigDecimal r18_orig_amt) {
		this.r18_orig_amt = r18_orig_amt;
	}
	public BigDecimal getR18_fore_amt() {
		return r18_fore_amt;
	}
	public void setR18_fore_amt(BigDecimal r18_fore_amt) {
		this.r18_fore_amt = r18_fore_amt;
	}
	public BigDecimal getR18_no_of_acc() {
		return r18_no_of_acc;
	}
	public void setR18_no_of_acc(BigDecimal r18_no_of_acc) {
		this.r18_no_of_acc = r18_no_of_acc;
	}
	public String getR19_sche_fore_ass() {
		return r19_sche_fore_ass;
	}
	public void setR19_sche_fore_ass(String r19_sche_fore_ass) {
		this.r19_sche_fore_ass = r19_sche_fore_ass;
	}
	public BigDecimal getR19_orig_amt() {
		return r19_orig_amt;
	}
	public void setR19_orig_amt(BigDecimal r19_orig_amt) {
		this.r19_orig_amt = r19_orig_amt;
	}
	public BigDecimal getR19_fore_amt() {
		return r19_fore_amt;
	}
	public void setR19_fore_amt(BigDecimal r19_fore_amt) {
		this.r19_fore_amt = r19_fore_amt;
	}
	public BigDecimal getR19_no_of_acc() {
		return r19_no_of_acc;
	}
	public void setR19_no_of_acc(BigDecimal r19_no_of_acc) {
		this.r19_no_of_acc = r19_no_of_acc;
	}
	public String getR20_sche_fore_ass() {
		return r20_sche_fore_ass;
	}
	public void setR20_sche_fore_ass(String r20_sche_fore_ass) {
		this.r20_sche_fore_ass = r20_sche_fore_ass;
	}
	public BigDecimal getR20_orig_amt() {
		return r20_orig_amt;
	}
	public void setR20_orig_amt(BigDecimal r20_orig_amt) {
		this.r20_orig_amt = r20_orig_amt;
	}
	public BigDecimal getR20_fore_amt() {
		return r20_fore_amt;
	}
	public void setR20_fore_amt(BigDecimal r20_fore_amt) {
		this.r20_fore_amt = r20_fore_amt;
	}
	public BigDecimal getR20_no_of_acc() {
		return r20_no_of_acc;
	}
	public void setR20_no_of_acc(BigDecimal r20_no_of_acc) {
		this.r20_no_of_acc = r20_no_of_acc;
	}
	public String getR21_sche_fore_ass() {
		return r21_sche_fore_ass;
	}
	public void setR21_sche_fore_ass(String r21_sche_fore_ass) {
		this.r21_sche_fore_ass = r21_sche_fore_ass;
	}
	public BigDecimal getR21_orig_amt() {
		return r21_orig_amt;
	}
	public void setR21_orig_amt(BigDecimal r21_orig_amt) {
		this.r21_orig_amt = r21_orig_amt;
	}
	public BigDecimal getR21_fore_amt() {
		return r21_fore_amt;
	}
	public void setR21_fore_amt(BigDecimal r21_fore_amt) {
		this.r21_fore_amt = r21_fore_amt;
	}
	public BigDecimal getR21_no_of_acc() {
		return r21_no_of_acc;
	}
	public void setR21_no_of_acc(BigDecimal r21_no_of_acc) {
		this.r21_no_of_acc = r21_no_of_acc;
	}
	public String getR22_sche_fore_ass() {
		return r22_sche_fore_ass;
	}
	public void setR22_sche_fore_ass(String r22_sche_fore_ass) {
		this.r22_sche_fore_ass = r22_sche_fore_ass;
	}
	public BigDecimal getR22_orig_amt() {
		return r22_orig_amt;
	}
	public void setR22_orig_amt(BigDecimal r22_orig_amt) {
		this.r22_orig_amt = r22_orig_amt;
	}
	public BigDecimal getR22_fore_amt() {
		return r22_fore_amt;
	}
	public void setR22_fore_amt(BigDecimal r22_fore_amt) {
		this.r22_fore_amt = r22_fore_amt;
	}
	public BigDecimal getR22_no_of_acc() {
		return r22_no_of_acc;
	}
	public void setR22_no_of_acc(BigDecimal r22_no_of_acc) {
		this.r22_no_of_acc = r22_no_of_acc;
	}
	public String getR23_sche_fore_ass() {
		return r23_sche_fore_ass;
	}
	public void setR23_sche_fore_ass(String r23_sche_fore_ass) {
		this.r23_sche_fore_ass = r23_sche_fore_ass;
	}
	public BigDecimal getR23_orig_amt() {
		return r23_orig_amt;
	}
	public void setR23_orig_amt(BigDecimal r23_orig_amt) {
		this.r23_orig_amt = r23_orig_amt;
	}
	public BigDecimal getR23_fore_amt() {
		return r23_fore_amt;
	}
	public void setR23_fore_amt(BigDecimal r23_fore_amt) {
		this.r23_fore_amt = r23_fore_amt;
	}
	public BigDecimal getR23_no_of_acc() {
		return r23_no_of_acc;
	}
	public void setR23_no_of_acc(BigDecimal r23_no_of_acc) {
		this.r23_no_of_acc = r23_no_of_acc;
	}
	public String getR24_sche_fore_ass() {
		return r24_sche_fore_ass;
	}
	public void setR24_sche_fore_ass(String r24_sche_fore_ass) {
		this.r24_sche_fore_ass = r24_sche_fore_ass;
	}
	public BigDecimal getR24_orig_amt() {
		return r24_orig_amt;
	}
	public void setR24_orig_amt(BigDecimal r24_orig_amt) {
		this.r24_orig_amt = r24_orig_amt;
	}
	public BigDecimal getR24_fore_amt() {
		return r24_fore_amt;
	}
	public void setR24_fore_amt(BigDecimal r24_fore_amt) {
		this.r24_fore_amt = r24_fore_amt;
	}
	public BigDecimal getR24_no_of_acc() {
		return r24_no_of_acc;
	}
	public void setR24_no_of_acc(BigDecimal r24_no_of_acc) {
		this.r24_no_of_acc = r24_no_of_acc;
	}
	public String getR25_sche_fore_ass() {
		return r25_sche_fore_ass;
	}
	public void setR25_sche_fore_ass(String r25_sche_fore_ass) {
		this.r25_sche_fore_ass = r25_sche_fore_ass;
	}
	public BigDecimal getR25_orig_amt() {
		return r25_orig_amt;
	}
	public void setR25_orig_amt(BigDecimal r25_orig_amt) {
		this.r25_orig_amt = r25_orig_amt;
	}
	public BigDecimal getR25_fore_amt() {
		return r25_fore_amt;
	}
	public void setR25_fore_amt(BigDecimal r25_fore_amt) {
		this.r25_fore_amt = r25_fore_amt;
	}
	public BigDecimal getR25_no_of_acc() {
		return r25_no_of_acc;
	}
	public void setR25_no_of_acc(BigDecimal r25_no_of_acc) {
		this.r25_no_of_acc = r25_no_of_acc;
	}
	public String getR26_sche_fore_ass() {
		return r26_sche_fore_ass;
	}
	public void setR26_sche_fore_ass(String r26_sche_fore_ass) {
		this.r26_sche_fore_ass = r26_sche_fore_ass;
	}
	public BigDecimal getR26_orig_amt() {
		return r26_orig_amt;
	}
	public void setR26_orig_amt(BigDecimal r26_orig_amt) {
		this.r26_orig_amt = r26_orig_amt;
	}
	public BigDecimal getR26_fore_amt() {
		return r26_fore_amt;
	}
	public void setR26_fore_amt(BigDecimal r26_fore_amt) {
		this.r26_fore_amt = r26_fore_amt;
	}
	public BigDecimal getR26_no_of_acc() {
		return r26_no_of_acc;
	}
	public void setR26_no_of_acc(BigDecimal r26_no_of_acc) {
		this.r26_no_of_acc = r26_no_of_acc;
	}
	public String getR27_sche_fore_ass() {
		return r27_sche_fore_ass;
	}
	public void setR27_sche_fore_ass(String r27_sche_fore_ass) {
		this.r27_sche_fore_ass = r27_sche_fore_ass;
	}
	public BigDecimal getR27_orig_amt() {
		return r27_orig_amt;
	}
	public void setR27_orig_amt(BigDecimal r27_orig_amt) {
		this.r27_orig_amt = r27_orig_amt;
	}
	public BigDecimal getR27_fore_amt() {
		return r27_fore_amt;
	}
	public void setR27_fore_amt(BigDecimal r27_fore_amt) {
		this.r27_fore_amt = r27_fore_amt;
	}
	public BigDecimal getR27_no_of_acc() {
		return r27_no_of_acc;
	}
	public void setR27_no_of_acc(BigDecimal r27_no_of_acc) {
		this.r27_no_of_acc = r27_no_of_acc;
	}
	public String getR28_sche_fore_ass() {
		return r28_sche_fore_ass;
	}
	public void setR28_sche_fore_ass(String r28_sche_fore_ass) {
		this.r28_sche_fore_ass = r28_sche_fore_ass;
	}
	public BigDecimal getR28_orig_amt() {
		return r28_orig_amt;
	}
	public void setR28_orig_amt(BigDecimal r28_orig_amt) {
		this.r28_orig_amt = r28_orig_amt;
	}
	public BigDecimal getR28_fore_amt() {
		return r28_fore_amt;
	}
	public void setR28_fore_amt(BigDecimal r28_fore_amt) {
		this.r28_fore_amt = r28_fore_amt;
	}
	public BigDecimal getR28_no_of_acc() {
		return r28_no_of_acc;
	}
	public void setR28_no_of_acc(BigDecimal r28_no_of_acc) {
		this.r28_no_of_acc = r28_no_of_acc;
	}
	public String getR29_sche_fore_ass() {
		return r29_sche_fore_ass;
	}
	public void setR29_sche_fore_ass(String r29_sche_fore_ass) {
		this.r29_sche_fore_ass = r29_sche_fore_ass;
	}
	public BigDecimal getR29_orig_amt() {
		return r29_orig_amt;
	}
	public void setR29_orig_amt(BigDecimal r29_orig_amt) {
		this.r29_orig_amt = r29_orig_amt;
	}
	public BigDecimal getR29_fore_amt() {
		return r29_fore_amt;
	}
	public void setR29_fore_amt(BigDecimal r29_fore_amt) {
		this.r29_fore_amt = r29_fore_amt;
	}
	public BigDecimal getR29_no_of_acc() {
		return r29_no_of_acc;
	}
	public void setR29_no_of_acc(BigDecimal r29_no_of_acc) {
		this.r29_no_of_acc = r29_no_of_acc;
	}
	public String getR30_sche_fore_ass() {
		return r30_sche_fore_ass;
	}
	public void setR30_sche_fore_ass(String r30_sche_fore_ass) {
		this.r30_sche_fore_ass = r30_sche_fore_ass;
	}
	public BigDecimal getR30_orig_amt() {
		return r30_orig_amt;
	}
	public void setR30_orig_amt(BigDecimal r30_orig_amt) {
		this.r30_orig_amt = r30_orig_amt;
	}
	public BigDecimal getR30_fore_amt() {
		return r30_fore_amt;
	}
	public void setR30_fore_amt(BigDecimal r30_fore_amt) {
		this.r30_fore_amt = r30_fore_amt;
	}
	public BigDecimal getR30_no_of_acc() {
		return r30_no_of_acc;
	}
	public void setR30_no_of_acc(BigDecimal r30_no_of_acc) {
		this.r30_no_of_acc = r30_no_of_acc;
	}
	public String getR31_sche_fore_ass() {
		return r31_sche_fore_ass;
	}
	public void setR31_sche_fore_ass(String r31_sche_fore_ass) {
		this.r31_sche_fore_ass = r31_sche_fore_ass;
	}
	public BigDecimal getR31_orig_amt() {
		return r31_orig_amt;
	}
	public void setR31_orig_amt(BigDecimal r31_orig_amt) {
		this.r31_orig_amt = r31_orig_amt;
	}
	public BigDecimal getR31_fore_amt() {
		return r31_fore_amt;
	}
	public void setR31_fore_amt(BigDecimal r31_fore_amt) {
		this.r31_fore_amt = r31_fore_amt;
	}
	public BigDecimal getR31_no_of_acc() {
		return r31_no_of_acc;
	}
	public void setR31_no_of_acc(BigDecimal r31_no_of_acc) {
		this.r31_no_of_acc = r31_no_of_acc;
	}
	public String getR32_sche_fore_ass() {
		return r32_sche_fore_ass;
	}
	public void setR32_sche_fore_ass(String r32_sche_fore_ass) {
		this.r32_sche_fore_ass = r32_sche_fore_ass;
	}
	public BigDecimal getR32_orig_amt() {
		return r32_orig_amt;
	}
	public void setR32_orig_amt(BigDecimal r32_orig_amt) {
		this.r32_orig_amt = r32_orig_amt;
	}
	public BigDecimal getR32_fore_amt() {
		return r32_fore_amt;
	}
	public void setR32_fore_amt(BigDecimal r32_fore_amt) {
		this.r32_fore_amt = r32_fore_amt;
	}
	public BigDecimal getR32_no_of_acc() {
		return r32_no_of_acc;
	}
	public void setR32_no_of_acc(BigDecimal r32_no_of_acc) {
		this.r32_no_of_acc = r32_no_of_acc;
	}
	public String getR33_sche_fore_ass() {
		return r33_sche_fore_ass;
	}
	public void setR33_sche_fore_ass(String r33_sche_fore_ass) {
		this.r33_sche_fore_ass = r33_sche_fore_ass;
	}
	public BigDecimal getR33_orig_amt() {
		return r33_orig_amt;
	}
	public void setR33_orig_amt(BigDecimal r33_orig_amt) {
		this.r33_orig_amt = r33_orig_amt;
	}
	public BigDecimal getR33_fore_amt() {
		return r33_fore_amt;
	}
	public void setR33_fore_amt(BigDecimal r33_fore_amt) {
		this.r33_fore_amt = r33_fore_amt;
	}
	public BigDecimal getR33_no_of_acc() {
		return r33_no_of_acc;
	}
	public void setR33_no_of_acc(BigDecimal r33_no_of_acc) {
		this.r33_no_of_acc = r33_no_of_acc;
	}
	public String getR34_sche_fore_ass() {
		return r34_sche_fore_ass;
	}
	public void setR34_sche_fore_ass(String r34_sche_fore_ass) {
		this.r34_sche_fore_ass = r34_sche_fore_ass;
	}
	public BigDecimal getR34_orig_amt() {
		return r34_orig_amt;
	}
	public void setR34_orig_amt(BigDecimal r34_orig_amt) {
		this.r34_orig_amt = r34_orig_amt;
	}
	public BigDecimal getR34_fore_amt() {
		return r34_fore_amt;
	}
	public void setR34_fore_amt(BigDecimal r34_fore_amt) {
		this.r34_fore_amt = r34_fore_amt;
	}
	public BigDecimal getR34_no_of_acc() {
		return r34_no_of_acc;
	}
	public void setR34_no_of_acc(BigDecimal r34_no_of_acc) {
		this.r34_no_of_acc = r34_no_of_acc;
	}
	public String getR35_sche_fore_ass() {
		return r35_sche_fore_ass;
	}
	public void setR35_sche_fore_ass(String r35_sche_fore_ass) {
		this.r35_sche_fore_ass = r35_sche_fore_ass;
	}
	public BigDecimal getR35_orig_amt() {
		return r35_orig_amt;
	}
	public void setR35_orig_amt(BigDecimal r35_orig_amt) {
		this.r35_orig_amt = r35_orig_amt;
	}
	public BigDecimal getR35_fore_amt() {
		return r35_fore_amt;
	}
	public void setR35_fore_amt(BigDecimal r35_fore_amt) {
		this.r35_fore_amt = r35_fore_amt;
	}
	public BigDecimal getR35_no_of_acc() {
		return r35_no_of_acc;
	}
	public void setR35_no_of_acc(BigDecimal r35_no_of_acc) {
		this.r35_no_of_acc = r35_no_of_acc;
	}
	public String getR36_sche_fore_ass() {
		return r36_sche_fore_ass;
	}
	public void setR36_sche_fore_ass(String r36_sche_fore_ass) {
		this.r36_sche_fore_ass = r36_sche_fore_ass;
	}
	public BigDecimal getR36_orig_amt() {
		return r36_orig_amt;
	}
	public void setR36_orig_amt(BigDecimal r36_orig_amt) {
		this.r36_orig_amt = r36_orig_amt;
	}
	public BigDecimal getR36_fore_amt() {
		return r36_fore_amt;
	}
	public void setR36_fore_amt(BigDecimal r36_fore_amt) {
		this.r36_fore_amt = r36_fore_amt;
	}
	public BigDecimal getR36_no_of_acc() {
		return r36_no_of_acc;
	}
	public void setR36_no_of_acc(BigDecimal r36_no_of_acc) {
		this.r36_no_of_acc = r36_no_of_acc;
	}
	public String getR37_sche_fore_ass() {
		return r37_sche_fore_ass;
	}
	public void setR37_sche_fore_ass(String r37_sche_fore_ass) {
		this.r37_sche_fore_ass = r37_sche_fore_ass;
	}
	public BigDecimal getR37_orig_amt() {
		return r37_orig_amt;
	}
	public void setR37_orig_amt(BigDecimal r37_orig_amt) {
		this.r37_orig_amt = r37_orig_amt;
	}
	public BigDecimal getR37_fore_amt() {
		return r37_fore_amt;
	}
	public void setR37_fore_amt(BigDecimal r37_fore_amt) {
		this.r37_fore_amt = r37_fore_amt;
	}
	public BigDecimal getR37_no_of_acc() {
		return r37_no_of_acc;
	}
	public void setR37_no_of_acc(BigDecimal r37_no_of_acc) {
		this.r37_no_of_acc = r37_no_of_acc;
	}
	public String getR38_sche_fore_ass() {
		return r38_sche_fore_ass;
	}
	public void setR38_sche_fore_ass(String r38_sche_fore_ass) {
		this.r38_sche_fore_ass = r38_sche_fore_ass;
	}
	public BigDecimal getR38_orig_amt() {
		return r38_orig_amt;
	}
	public void setR38_orig_amt(BigDecimal r38_orig_amt) {
		this.r38_orig_amt = r38_orig_amt;
	}
	public BigDecimal getR38_fore_amt() {
		return r38_fore_amt;
	}
	public void setR38_fore_amt(BigDecimal r38_fore_amt) {
		this.r38_fore_amt = r38_fore_amt;
	}
	public BigDecimal getR38_no_of_acc() {
		return r38_no_of_acc;
	}
	public void setR38_no_of_acc(BigDecimal r38_no_of_acc) {
		this.r38_no_of_acc = r38_no_of_acc;
	}
	public String getR39_sche_fore_ass() {
		return r39_sche_fore_ass;
	}
	public void setR39_sche_fore_ass(String r39_sche_fore_ass) {
		this.r39_sche_fore_ass = r39_sche_fore_ass;
	}
	public BigDecimal getR39_orig_amt() {
		return r39_orig_amt;
	}
	public void setR39_orig_amt(BigDecimal r39_orig_amt) {
		this.r39_orig_amt = r39_orig_amt;
	}
	public BigDecimal getR39_fore_amt() {
		return r39_fore_amt;
	}
	public void setR39_fore_amt(BigDecimal r39_fore_amt) {
		this.r39_fore_amt = r39_fore_amt;
	}
	public BigDecimal getR39_no_of_acc() {
		return r39_no_of_acc;
	}
	public void setR39_no_of_acc(BigDecimal r39_no_of_acc) {
		this.r39_no_of_acc = r39_no_of_acc;
	}
	public String getR40_sche_fore_ass() {
		return r40_sche_fore_ass;
	}
	public void setR40_sche_fore_ass(String r40_sche_fore_ass) {
		this.r40_sche_fore_ass = r40_sche_fore_ass;
	}
	public BigDecimal getR40_orig_amt() {
		return r40_orig_amt;
	}
	public void setR40_orig_amt(BigDecimal r40_orig_amt) {
		this.r40_orig_amt = r40_orig_amt;
	}
	public BigDecimal getR40_fore_amt() {
		return r40_fore_amt;
	}
	public void setR40_fore_amt(BigDecimal r40_fore_amt) {
		this.r40_fore_amt = r40_fore_amt;
	}
	public BigDecimal getR40_no_of_acc() {
		return r40_no_of_acc;
	}
	public void setR40_no_of_acc(BigDecimal r40_no_of_acc) {
		this.r40_no_of_acc = r40_no_of_acc;
	}
	public String getR41_sche_fore_ass() {
		return r41_sche_fore_ass;
	}
	public void setR41_sche_fore_ass(String r41_sche_fore_ass) {
		this.r41_sche_fore_ass = r41_sche_fore_ass;
	}
	public BigDecimal getR41_orig_amt() {
		return r41_orig_amt;
	}
	public void setR41_orig_amt(BigDecimal r41_orig_amt) {
		this.r41_orig_amt = r41_orig_amt;
	}
	public BigDecimal getR41_fore_amt() {
		return r41_fore_amt;
	}
	public void setR41_fore_amt(BigDecimal r41_fore_amt) {
		this.r41_fore_amt = r41_fore_amt;
	}
	public BigDecimal getR41_no_of_acc() {
		return r41_no_of_acc;
	}
	public void setR41_no_of_acc(BigDecimal r41_no_of_acc) {
		this.r41_no_of_acc = r41_no_of_acc;
	}
	public String getR42_sche_fore_ass() {
		return r42_sche_fore_ass;
	}
	public void setR42_sche_fore_ass(String r42_sche_fore_ass) {
		this.r42_sche_fore_ass = r42_sche_fore_ass;
	}
	public BigDecimal getR42_orig_amt() {
		return r42_orig_amt;
	}
	public void setR42_orig_amt(BigDecimal r42_orig_amt) {
		this.r42_orig_amt = r42_orig_amt;
	}
	public BigDecimal getR42_fore_amt() {
		return r42_fore_amt;
	}
	public void setR42_fore_amt(BigDecimal r42_fore_amt) {
		this.r42_fore_amt = r42_fore_amt;
	}
	public BigDecimal getR42_no_of_acc() {
		return r42_no_of_acc;
	}
	public void setR42_no_of_acc(BigDecimal r42_no_of_acc) {
		this.r42_no_of_acc = r42_no_of_acc;
	}
	public String getR43_sche_fore_ass() {
		return r43_sche_fore_ass;
	}
	public void setR43_sche_fore_ass(String r43_sche_fore_ass) {
		this.r43_sche_fore_ass = r43_sche_fore_ass;
	}
	public BigDecimal getR43_orig_amt() {
		return r43_orig_amt;
	}
	public void setR43_orig_amt(BigDecimal r43_orig_amt) {
		this.r43_orig_amt = r43_orig_amt;
	}
	public BigDecimal getR43_fore_amt() {
		return r43_fore_amt;
	}
	public void setR43_fore_amt(BigDecimal r43_fore_amt) {
		this.r43_fore_amt = r43_fore_amt;
	}
	public BigDecimal getR43_no_of_acc() {
		return r43_no_of_acc;
	}
	public void setR43_no_of_acc(BigDecimal r43_no_of_acc) {
		this.r43_no_of_acc = r43_no_of_acc;
	}
	public String getR44_sche_fore_ass() {
		return r44_sche_fore_ass;
	}
	public void setR44_sche_fore_ass(String r44_sche_fore_ass) {
		this.r44_sche_fore_ass = r44_sche_fore_ass;
	}
	public BigDecimal getR44_orig_amt() {
		return r44_orig_amt;
	}
	public void setR44_orig_amt(BigDecimal r44_orig_amt) {
		this.r44_orig_amt = r44_orig_amt;
	}
	public BigDecimal getR44_fore_amt() {
		return r44_fore_amt;
	}
	public void setR44_fore_amt(BigDecimal r44_fore_amt) {
		this.r44_fore_amt = r44_fore_amt;
	}
	public BigDecimal getR44_no_of_acc() {
		return r44_no_of_acc;
	}
	public void setR44_no_of_acc(BigDecimal r44_no_of_acc) {
		this.r44_no_of_acc = r44_no_of_acc;
	}
	public String getR45_sche_fore_ass() {
		return r45_sche_fore_ass;
	}
	public void setR45_sche_fore_ass(String r45_sche_fore_ass) {
		this.r45_sche_fore_ass = r45_sche_fore_ass;
	}
	public BigDecimal getR45_orig_amt() {
		return r45_orig_amt;
	}
	public void setR45_orig_amt(BigDecimal r45_orig_amt) {
		this.r45_orig_amt = r45_orig_amt;
	}
	public BigDecimal getR45_fore_amt() {
		return r45_fore_amt;
	}
	public void setR45_fore_amt(BigDecimal r45_fore_amt) {
		this.r45_fore_amt = r45_fore_amt;
	}
	public BigDecimal getR45_no_of_acc() {
		return r45_no_of_acc;
	}
	public void setR45_no_of_acc(BigDecimal r45_no_of_acc) {
		this.r45_no_of_acc = r45_no_of_acc;
	}
	public String getR46_sche_fore_ass() {
		return r46_sche_fore_ass;
	}
	public void setR46_sche_fore_ass(String r46_sche_fore_ass) {
		this.r46_sche_fore_ass = r46_sche_fore_ass;
	}
	public BigDecimal getR46_orig_amt() {
		return r46_orig_amt;
	}
	public void setR46_orig_amt(BigDecimal r46_orig_amt) {
		this.r46_orig_amt = r46_orig_amt;
	}
	public BigDecimal getR46_fore_amt() {
		return r46_fore_amt;
	}
	public void setR46_fore_amt(BigDecimal r46_fore_amt) {
		this.r46_fore_amt = r46_fore_amt;
	}
	public BigDecimal getR46_no_of_acc() {
		return r46_no_of_acc;
	}
	public void setR46_no_of_acc(BigDecimal r46_no_of_acc) {
		this.r46_no_of_acc = r46_no_of_acc;
	}
	public String getR47_sche_fore_ass() {
		return r47_sche_fore_ass;
	}
	public void setR47_sche_fore_ass(String r47_sche_fore_ass) {
		this.r47_sche_fore_ass = r47_sche_fore_ass;
	}
	public BigDecimal getR47_orig_amt() {
		return r47_orig_amt;
	}
	public void setR47_orig_amt(BigDecimal r47_orig_amt) {
		this.r47_orig_amt = r47_orig_amt;
	}
	public BigDecimal getR47_fore_amt() {
		return r47_fore_amt;
	}
	public void setR47_fore_amt(BigDecimal r47_fore_amt) {
		this.r47_fore_amt = r47_fore_amt;
	}
	public BigDecimal getR47_no_of_acc() {
		return r47_no_of_acc;
	}
	public void setR47_no_of_acc(BigDecimal r47_no_of_acc) {
		this.r47_no_of_acc = r47_no_of_acc;
	}
	public String getR48_sche_fore_ass() {
		return r48_sche_fore_ass;
	}
	public void setR48_sche_fore_ass(String r48_sche_fore_ass) {
		this.r48_sche_fore_ass = r48_sche_fore_ass;
	}
	public BigDecimal getR48_orig_amt() {
		return r48_orig_amt;
	}
	public void setR48_orig_amt(BigDecimal r48_orig_amt) {
		this.r48_orig_amt = r48_orig_amt;
	}
	public BigDecimal getR48_fore_amt() {
		return r48_fore_amt;
	}
	public void setR48_fore_amt(BigDecimal r48_fore_amt) {
		this.r48_fore_amt = r48_fore_amt;
	}
	public BigDecimal getR48_no_of_acc() {
		return r48_no_of_acc;
	}
	public void setR48_no_of_acc(BigDecimal r48_no_of_acc) {
		this.r48_no_of_acc = r48_no_of_acc;
	}
	public String getR49_sche_fore_ass() {
		return r49_sche_fore_ass;
	}
	public void setR49_sche_fore_ass(String r49_sche_fore_ass) {
		this.r49_sche_fore_ass = r49_sche_fore_ass;
	}
	public BigDecimal getR49_orig_amt() {
		return r49_orig_amt;
	}
	public void setR49_orig_amt(BigDecimal r49_orig_amt) {
		this.r49_orig_amt = r49_orig_amt;
	}
	public BigDecimal getR49_fore_amt() {
		return r49_fore_amt;
	}
	public void setR49_fore_amt(BigDecimal r49_fore_amt) {
		this.r49_fore_amt = r49_fore_amt;
	}
	public BigDecimal getR49_no_of_acc() {
		return r49_no_of_acc;
	}
	public void setR49_no_of_acc(BigDecimal r49_no_of_acc) {
		this.r49_no_of_acc = r49_no_of_acc;
	}
	public String getR50_sche_fore_ass() {
		return r50_sche_fore_ass;
	}
	public void setR50_sche_fore_ass(String r50_sche_fore_ass) {
		this.r50_sche_fore_ass = r50_sche_fore_ass;
	}
	public BigDecimal getR50_orig_amt() {
		return r50_orig_amt;
	}
	public void setR50_orig_amt(BigDecimal r50_orig_amt) {
		this.r50_orig_amt = r50_orig_amt;
	}
	public BigDecimal getR50_fore_amt() {
		return r50_fore_amt;
	}
	public void setR50_fore_amt(BigDecimal r50_fore_amt) {
		this.r50_fore_amt = r50_fore_amt;
	}
	public BigDecimal getR50_no_of_acc() {
		return r50_no_of_acc;
	}
	public void setR50_no_of_acc(BigDecimal r50_no_of_acc) {
		this.r50_no_of_acc = r50_no_of_acc;
	}
	public String getR51_sche_fore_ass() {
		return r51_sche_fore_ass;
	}
	public void setR51_sche_fore_ass(String r51_sche_fore_ass) {
		this.r51_sche_fore_ass = r51_sche_fore_ass;
	}
	public BigDecimal getR51_orig_amt() {
		return r51_orig_amt;
	}
	public void setR51_orig_amt(BigDecimal r51_orig_amt) {
		this.r51_orig_amt = r51_orig_amt;
	}
	public BigDecimal getR51_fore_amt() {
		return r51_fore_amt;
	}
	public void setR51_fore_amt(BigDecimal r51_fore_amt) {
		this.r51_fore_amt = r51_fore_amt;
	}
	public BigDecimal getR51_no_of_acc() {
		return r51_no_of_acc;
	}
	public void setR51_no_of_acc(BigDecimal r51_no_of_acc) {
		this.r51_no_of_acc = r51_no_of_acc;
	}
	public String getR52_sche_fore_ass() {
		return r52_sche_fore_ass;
	}
	public void setR52_sche_fore_ass(String r52_sche_fore_ass) {
		this.r52_sche_fore_ass = r52_sche_fore_ass;
	}
	public BigDecimal getR52_orig_amt() {
		return r52_orig_amt;
	}
	public void setR52_orig_amt(BigDecimal r52_orig_amt) {
		this.r52_orig_amt = r52_orig_amt;
	}
	public BigDecimal getR52_fore_amt() {
		return r52_fore_amt;
	}
	public void setR52_fore_amt(BigDecimal r52_fore_amt) {
		this.r52_fore_amt = r52_fore_amt;
	}
	public BigDecimal getR52_no_of_acc() {
		return r52_no_of_acc;
	}
	public void setR52_no_of_acc(BigDecimal r52_no_of_acc) {
		this.r52_no_of_acc = r52_no_of_acc;
	}
	public String getR53_sche_fore_ass() {
		return r53_sche_fore_ass;
	}
	public void setR53_sche_fore_ass(String r53_sche_fore_ass) {
		this.r53_sche_fore_ass = r53_sche_fore_ass;
	}
	public BigDecimal getR53_orig_amt() {
		return r53_orig_amt;
	}
	public void setR53_orig_amt(BigDecimal r53_orig_amt) {
		this.r53_orig_amt = r53_orig_amt;
	}
	public BigDecimal getR53_fore_amt() {
		return r53_fore_amt;
	}
	public void setR53_fore_amt(BigDecimal r53_fore_amt) {
		this.r53_fore_amt = r53_fore_amt;
	}
	public BigDecimal getR53_no_of_acc() {
		return r53_no_of_acc;
	}
	public void setR53_no_of_acc(BigDecimal r53_no_of_acc) {
		this.r53_no_of_acc = r53_no_of_acc;
	}
	public String getR54_sche_fore_ass() {
		return r54_sche_fore_ass;
	}
	public void setR54_sche_fore_ass(String r54_sche_fore_ass) {
		this.r54_sche_fore_ass = r54_sche_fore_ass;
	}
	public BigDecimal getR54_orig_amt() {
		return r54_orig_amt;
	}
	public void setR54_orig_amt(BigDecimal r54_orig_amt) {
		this.r54_orig_amt = r54_orig_amt;
	}
	public BigDecimal getR54_fore_amt() {
		return r54_fore_amt;
	}
	public void setR54_fore_amt(BigDecimal r54_fore_amt) {
		this.r54_fore_amt = r54_fore_amt;
	}
	public BigDecimal getR54_no_of_acc() {
		return r54_no_of_acc;
	}
	public void setR54_no_of_acc(BigDecimal r54_no_of_acc) {
		this.r54_no_of_acc = r54_no_of_acc;
	}
	public String getR55_sche_fore_ass() {
		return r55_sche_fore_ass;
	}
	public void setR55_sche_fore_ass(String r55_sche_fore_ass) {
		this.r55_sche_fore_ass = r55_sche_fore_ass;
	}
	public BigDecimal getR55_orig_amt() {
		return r55_orig_amt;
	}
	public void setR55_orig_amt(BigDecimal r55_orig_amt) {
		this.r55_orig_amt = r55_orig_amt;
	}
	public BigDecimal getR55_fore_amt() {
		return r55_fore_amt;
	}
	public void setR55_fore_amt(BigDecimal r55_fore_amt) {
		this.r55_fore_amt = r55_fore_amt;
	}
	public BigDecimal getR55_no_of_acc() {
		return r55_no_of_acc;
	}
	public void setR55_no_of_acc(BigDecimal r55_no_of_acc) {
		this.r55_no_of_acc = r55_no_of_acc;
	}
	public String getR56_sche_fore_ass() {
		return r56_sche_fore_ass;
	}
	public void setR56_sche_fore_ass(String r56_sche_fore_ass) {
		this.r56_sche_fore_ass = r56_sche_fore_ass;
	}
	public BigDecimal getR56_orig_amt() {
		return r56_orig_amt;
	}
	public void setR56_orig_amt(BigDecimal r56_orig_amt) {
		this.r56_orig_amt = r56_orig_amt;
	}
	public BigDecimal getR56_fore_amt() {
		return r56_fore_amt;
	}
	public void setR56_fore_amt(BigDecimal r56_fore_amt) {
		this.r56_fore_amt = r56_fore_amt;
	}
	public BigDecimal getR56_no_of_acc() {
		return r56_no_of_acc;
	}
	public void setR56_no_of_acc(BigDecimal r56_no_of_acc) {
		this.r56_no_of_acc = r56_no_of_acc;
	}
	public String getR57_sche_fore_ass() {
		return r57_sche_fore_ass;
	}
	public void setR57_sche_fore_ass(String r57_sche_fore_ass) {
		this.r57_sche_fore_ass = r57_sche_fore_ass;
	}
	public BigDecimal getR57_orig_amt() {
		return r57_orig_amt;
	}
	public void setR57_orig_amt(BigDecimal r57_orig_amt) {
		this.r57_orig_amt = r57_orig_amt;
	}
	public BigDecimal getR57_fore_amt() {
		return r57_fore_amt;
	}
	public void setR57_fore_amt(BigDecimal r57_fore_amt) {
		this.r57_fore_amt = r57_fore_amt;
	}
	public BigDecimal getR57_no_of_acc() {
		return r57_no_of_acc;
	}
	public void setR57_no_of_acc(BigDecimal r57_no_of_acc) {
		this.r57_no_of_acc = r57_no_of_acc;
	}
	public String getR58_sche_fore_ass() {
		return r58_sche_fore_ass;
	}
	public void setR58_sche_fore_ass(String r58_sche_fore_ass) {
		this.r58_sche_fore_ass = r58_sche_fore_ass;
	}
	public BigDecimal getR58_orig_amt() {
		return r58_orig_amt;
	}
	public void setR58_orig_amt(BigDecimal r58_orig_amt) {
		this.r58_orig_amt = r58_orig_amt;
	}
	public BigDecimal getR58_fore_amt() {
		return r58_fore_amt;
	}
	public void setR58_fore_amt(BigDecimal r58_fore_amt) {
		this.r58_fore_amt = r58_fore_amt;
	}
	public BigDecimal getR58_no_of_acc() {
		return r58_no_of_acc;
	}
	public void setR58_no_of_acc(BigDecimal r58_no_of_acc) {
		this.r58_no_of_acc = r58_no_of_acc;
	}
	public String getR59_sche_fore_ass() {
		return r59_sche_fore_ass;
	}
	public void setR59_sche_fore_ass(String r59_sche_fore_ass) {
		this.r59_sche_fore_ass = r59_sche_fore_ass;
	}
	public BigDecimal getR59_orig_amt() {
		return r59_orig_amt;
	}
	public void setR59_orig_amt(BigDecimal r59_orig_amt) {
		this.r59_orig_amt = r59_orig_amt;
	}
	public BigDecimal getR59_fore_amt() {
		return r59_fore_amt;
	}
	public void setR59_fore_amt(BigDecimal r59_fore_amt) {
		this.r59_fore_amt = r59_fore_amt;
	}
	public BigDecimal getR59_no_of_acc() {
		return r59_no_of_acc;
	}
	public void setR59_no_of_acc(BigDecimal r59_no_of_acc) {
		this.r59_no_of_acc = r59_no_of_acc;
	}
	public String getR60_sche_fore_ass() {
		return r60_sche_fore_ass;
	}
	public void setR60_sche_fore_ass(String r60_sche_fore_ass) {
		this.r60_sche_fore_ass = r60_sche_fore_ass;
	}
	public BigDecimal getR60_orig_amt() {
		return r60_orig_amt;
	}
	public void setR60_orig_amt(BigDecimal r60_orig_amt) {
		this.r60_orig_amt = r60_orig_amt;
	}
	public BigDecimal getR60_fore_amt() {
		return r60_fore_amt;
	}
	public void setR60_fore_amt(BigDecimal r60_fore_amt) {
		this.r60_fore_amt = r60_fore_amt;
	}
	public BigDecimal getR60_no_of_acc() {
		return r60_no_of_acc;
	}
	public void setR60_no_of_acc(BigDecimal r60_no_of_acc) {
		this.r60_no_of_acc = r60_no_of_acc;
	}
	public String getR61_sche_fore_ass() {
		return r61_sche_fore_ass;
	}
	public void setR61_sche_fore_ass(String r61_sche_fore_ass) {
		this.r61_sche_fore_ass = r61_sche_fore_ass;
	}
	public BigDecimal getR61_orig_amt() {
		return r61_orig_amt;
	}
	public void setR61_orig_amt(BigDecimal r61_orig_amt) {
		this.r61_orig_amt = r61_orig_amt;
	}
	public BigDecimal getR61_fore_amt() {
		return r61_fore_amt;
	}
	public void setR61_fore_amt(BigDecimal r61_fore_amt) {
		this.r61_fore_amt = r61_fore_amt;
	}
	public BigDecimal getR61_no_of_acc() {
		return r61_no_of_acc;
	}
	public void setR61_no_of_acc(BigDecimal r61_no_of_acc) {
		this.r61_no_of_acc = r61_no_of_acc;
	}
	public String getR62_sche_fore_ass() {
		return r62_sche_fore_ass;
	}
	public void setR62_sche_fore_ass(String r62_sche_fore_ass) {
		this.r62_sche_fore_ass = r62_sche_fore_ass;
	}
	public BigDecimal getR62_orig_amt() {
		return r62_orig_amt;
	}
	public void setR62_orig_amt(BigDecimal r62_orig_amt) {
		this.r62_orig_amt = r62_orig_amt;
	}
	public BigDecimal getR62_fore_amt() {
		return r62_fore_amt;
	}
	public void setR62_fore_amt(BigDecimal r62_fore_amt) {
		this.r62_fore_amt = r62_fore_amt;
	}
	public BigDecimal getR62_no_of_acc() {
		return r62_no_of_acc;
	}
	public void setR62_no_of_acc(BigDecimal r62_no_of_acc) {
		this.r62_no_of_acc = r62_no_of_acc;
	}
	public String getR63_sche_fore_ass() {
		return r63_sche_fore_ass;
	}
	public void setR63_sche_fore_ass(String r63_sche_fore_ass) {
		this.r63_sche_fore_ass = r63_sche_fore_ass;
	}
	public BigDecimal getR63_orig_amt() {
		return r63_orig_amt;
	}
	public void setR63_orig_amt(BigDecimal r63_orig_amt) {
		this.r63_orig_amt = r63_orig_amt;
	}
	public BigDecimal getR63_fore_amt() {
		return r63_fore_amt;
	}
	public void setR63_fore_amt(BigDecimal r63_fore_amt) {
		this.r63_fore_amt = r63_fore_amt;
	}
	public BigDecimal getR63_no_of_acc() {
		return r63_no_of_acc;
	}
	public void setR63_no_of_acc(BigDecimal r63_no_of_acc) {
		this.r63_no_of_acc = r63_no_of_acc;
	}
	public String getR64_sche_fore_ass() {
		return r64_sche_fore_ass;
	}
	public void setR64_sche_fore_ass(String r64_sche_fore_ass) {
		this.r64_sche_fore_ass = r64_sche_fore_ass;
	}
	public BigDecimal getR64_orig_amt() {
		return r64_orig_amt;
	}
	public void setR64_orig_amt(BigDecimal r64_orig_amt) {
		this.r64_orig_amt = r64_orig_amt;
	}
	public BigDecimal getR64_fore_amt() {
		return r64_fore_amt;
	}
	public void setR64_fore_amt(BigDecimal r64_fore_amt) {
		this.r64_fore_amt = r64_fore_amt;
	}
	public BigDecimal getR64_no_of_acc() {
		return r64_no_of_acc;
	}
	public void setR64_no_of_acc(BigDecimal r64_no_of_acc) {
		this.r64_no_of_acc = r64_no_of_acc;
	}
	public String getR65_sche_fore_ass() {
		return r65_sche_fore_ass;
	}
	public void setR65_sche_fore_ass(String r65_sche_fore_ass) {
		this.r65_sche_fore_ass = r65_sche_fore_ass;
	}
	public BigDecimal getR65_orig_amt() {
		return r65_orig_amt;
	}
	public void setR65_orig_amt(BigDecimal r65_orig_amt) {
		this.r65_orig_amt = r65_orig_amt;
	}
	public BigDecimal getR65_fore_amt() {
		return r65_fore_amt;
	}
	public void setR65_fore_amt(BigDecimal r65_fore_amt) {
		this.r65_fore_amt = r65_fore_amt;
	}
	public BigDecimal getR65_no_of_acc() {
		return r65_no_of_acc;
	}
	public void setR65_no_of_acc(BigDecimal r65_no_of_acc) {
		this.r65_no_of_acc = r65_no_of_acc;
	}
	public String getR66_sche_fore_ass() {
		return r66_sche_fore_ass;
	}
	public void setR66_sche_fore_ass(String r66_sche_fore_ass) {
		this.r66_sche_fore_ass = r66_sche_fore_ass;
	}
	public BigDecimal getR66_orig_amt() {
		return r66_orig_amt;
	}
	public void setR66_orig_amt(BigDecimal r66_orig_amt) {
		this.r66_orig_amt = r66_orig_amt;
	}
	public BigDecimal getR66_fore_amt() {
		return r66_fore_amt;
	}
	public void setR66_fore_amt(BigDecimal r66_fore_amt) {
		this.r66_fore_amt = r66_fore_amt;
	}
	public BigDecimal getR66_no_of_acc() {
		return r66_no_of_acc;
	}
	public void setR66_no_of_acc(BigDecimal r66_no_of_acc) {
		this.r66_no_of_acc = r66_no_of_acc;
	}
	public String getR67_sche_fore_ass() {
		return r67_sche_fore_ass;
	}
	public void setR67_sche_fore_ass(String r67_sche_fore_ass) {
		this.r67_sche_fore_ass = r67_sche_fore_ass;
	}
	public BigDecimal getR67_orig_amt() {
		return r67_orig_amt;
	}
	public void setR67_orig_amt(BigDecimal r67_orig_amt) {
		this.r67_orig_amt = r67_orig_amt;
	}
	public BigDecimal getR67_fore_amt() {
		return r67_fore_amt;
	}
	public void setR67_fore_amt(BigDecimal r67_fore_amt) {
		this.r67_fore_amt = r67_fore_amt;
	}
	public BigDecimal getR67_no_of_acc() {
		return r67_no_of_acc;
	}
	public void setR67_no_of_acc(BigDecimal r67_no_of_acc) {
		this.r67_no_of_acc = r67_no_of_acc;
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
	public Date getReportResubDate() {
		return reportResubDate;
	}
	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
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


//=====================================================
// RESUB DETAIL Q_RLFA2
//=====================================================

public class Q_RLFA2_RESUB_Detail_RowMapper 
        implements RowMapper<Q_RLFA2_RESUB_Detail_Entity> {

    @Override
    public Q_RLFA2_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        Q_RLFA2_RESUB_Detail_Entity obj = new Q_RLFA2_RESUB_Detail_Entity();

// =========================
// R10
// =========================
obj.setR10_sche_fore_ass(rs.getString("r10_sche_fore_ass"));
obj.setR10_orig_amt(rs.getBigDecimal("r10_orig_amt"));
obj.setR10_fore_amt(rs.getBigDecimal("r10_fore_amt"));
obj.setR10_no_of_acc(rs.getBigDecimal("r10_no_of_acc"));


// =========================
// R11
// =========================
obj.setR11_sche_fore_ass(rs.getString("r11_sche_fore_ass"));
obj.setR11_orig_amt(rs.getBigDecimal("r11_orig_amt"));
obj.setR11_fore_amt(rs.getBigDecimal("r11_fore_amt"));
obj.setR11_no_of_acc(rs.getBigDecimal("r11_no_of_acc"));

// =========================
// R12
// =========================
obj.setR12_sche_fore_ass(rs.getString("r12_sche_fore_ass"));
obj.setR12_orig_amt(rs.getBigDecimal("r12_orig_amt"));
obj.setR12_fore_amt(rs.getBigDecimal("r12_fore_amt"));
obj.setR12_no_of_acc(rs.getBigDecimal("r12_no_of_acc"));

// =========================
// R13
// =========================
obj.setR13_sche_fore_ass(rs.getString("r13_sche_fore_ass"));
obj.setR13_orig_amt(rs.getBigDecimal("r13_orig_amt"));
obj.setR13_fore_amt(rs.getBigDecimal("r13_fore_amt"));
obj.setR13_no_of_acc(rs.getBigDecimal("r13_no_of_acc"));

// =========================
// R14
// =========================
obj.setR14_sche_fore_ass(rs.getString("r14_sche_fore_ass"));
obj.setR14_orig_amt(rs.getBigDecimal("r14_orig_amt"));
obj.setR14_fore_amt(rs.getBigDecimal("r14_fore_amt"));
obj.setR14_no_of_acc(rs.getBigDecimal("r14_no_of_acc"));

// =========================
// R15
// =========================
obj.setR15_sche_fore_ass(rs.getString("r15_sche_fore_ass"));
obj.setR15_orig_amt(rs.getBigDecimal("r15_orig_amt"));
obj.setR15_fore_amt(rs.getBigDecimal("r15_fore_amt"));
obj.setR15_no_of_acc(rs.getBigDecimal("r15_no_of_acc"));

// =========================
// R16
// =========================
obj.setR16_sche_fore_ass(rs.getString("r16_sche_fore_ass"));
obj.setR16_orig_amt(rs.getBigDecimal("r16_orig_amt"));
obj.setR16_fore_amt(rs.getBigDecimal("r16_fore_amt"));
obj.setR16_no_of_acc(rs.getBigDecimal("r16_no_of_acc"));

// =========================
// R17
// =========================
obj.setR17_sche_fore_ass(rs.getString("r17_sche_fore_ass"));
obj.setR17_orig_amt(rs.getBigDecimal("r17_orig_amt"));
obj.setR17_fore_amt(rs.getBigDecimal("r17_fore_amt"));
obj.setR17_no_of_acc(rs.getBigDecimal("r17_no_of_acc"));

// =========================
// R18
// =========================
obj.setR18_sche_fore_ass(rs.getString("r18_sche_fore_ass"));
obj.setR18_orig_amt(rs.getBigDecimal("r18_orig_amt"));
obj.setR18_fore_amt(rs.getBigDecimal("r18_fore_amt"));
obj.setR18_no_of_acc(rs.getBigDecimal("r18_no_of_acc"));

// =========================
// R19
// =========================
obj.setR19_sche_fore_ass(rs.getString("r19_sche_fore_ass"));
obj.setR19_orig_amt(rs.getBigDecimal("r19_orig_amt"));
obj.setR19_fore_amt(rs.getBigDecimal("r19_fore_amt"));
obj.setR19_no_of_acc(rs.getBigDecimal("r19_no_of_acc"));

// =========================
// R20
// =========================
obj.setR20_sche_fore_ass(rs.getString("r20_sche_fore_ass"));
obj.setR20_orig_amt(rs.getBigDecimal("r20_orig_amt"));
obj.setR20_fore_amt(rs.getBigDecimal("r20_fore_amt"));
obj.setR20_no_of_acc(rs.getBigDecimal("r20_no_of_acc"));

// =========================
// R21
// =========================
obj.setR21_sche_fore_ass(rs.getString("r21_sche_fore_ass"));
obj.setR21_orig_amt(rs.getBigDecimal("r21_orig_amt"));
obj.setR21_fore_amt(rs.getBigDecimal("r21_fore_amt"));
obj.setR21_no_of_acc(rs.getBigDecimal("r21_no_of_acc"));

// =========================
// R22
// =========================
obj.setR22_sche_fore_ass(rs.getString("r22_sche_fore_ass"));
obj.setR22_orig_amt(rs.getBigDecimal("r22_orig_amt"));
obj.setR22_fore_amt(rs.getBigDecimal("r22_fore_amt"));
obj.setR22_no_of_acc(rs.getBigDecimal("r22_no_of_acc"));

// =========================
// R23
// =========================
obj.setR23_sche_fore_ass(rs.getString("r23_sche_fore_ass"));
obj.setR23_orig_amt(rs.getBigDecimal("r23_orig_amt"));
obj.setR23_fore_amt(rs.getBigDecimal("r23_fore_amt"));
obj.setR23_no_of_acc(rs.getBigDecimal("r23_no_of_acc"));

// =========================
// R24
// =========================
obj.setR24_sche_fore_ass(rs.getString("r24_sche_fore_ass"));
obj.setR24_orig_amt(rs.getBigDecimal("r24_orig_amt"));
obj.setR24_fore_amt(rs.getBigDecimal("r24_fore_amt"));
obj.setR24_no_of_acc(rs.getBigDecimal("r24_no_of_acc"));

// =========================
// R25
// =========================
obj.setR25_sche_fore_ass(rs.getString("r25_sche_fore_ass"));
obj.setR25_orig_amt(rs.getBigDecimal("r25_orig_amt"));
obj.setR25_fore_amt(rs.getBigDecimal("r25_fore_amt"));
obj.setR25_no_of_acc(rs.getBigDecimal("r25_no_of_acc"));

// =========================
// R26
// =========================
obj.setR26_sche_fore_ass(rs.getString("r26_sche_fore_ass"));
obj.setR26_orig_amt(rs.getBigDecimal("r26_orig_amt"));
obj.setR26_fore_amt(rs.getBigDecimal("r26_fore_amt"));
obj.setR26_no_of_acc(rs.getBigDecimal("r26_no_of_acc"));

// =========================
// R27
// =========================
obj.setR27_sche_fore_ass(rs.getString("r27_sche_fore_ass"));
obj.setR27_orig_amt(rs.getBigDecimal("r27_orig_amt"));
obj.setR27_fore_amt(rs.getBigDecimal("r27_fore_amt"));
obj.setR27_no_of_acc(rs.getBigDecimal("r27_no_of_acc"));

// =========================
// R28
// =========================
obj.setR28_sche_fore_ass(rs.getString("r28_sche_fore_ass"));
obj.setR28_orig_amt(rs.getBigDecimal("r28_orig_amt"));
obj.setR28_fore_amt(rs.getBigDecimal("r28_fore_amt"));
obj.setR28_no_of_acc(rs.getBigDecimal("r28_no_of_acc"));

// =========================
// R29
// =========================
obj.setR29_sche_fore_ass(rs.getString("r29_sche_fore_ass"));
obj.setR29_orig_amt(rs.getBigDecimal("r29_orig_amt"));
obj.setR29_fore_amt(rs.getBigDecimal("r29_fore_amt"));
obj.setR29_no_of_acc(rs.getBigDecimal("r29_no_of_acc"));

// =========================
// R30
// =========================
obj.setR30_sche_fore_ass(rs.getString("r30_sche_fore_ass"));
obj.setR30_orig_amt(rs.getBigDecimal("r30_orig_amt"));
obj.setR30_fore_amt(rs.getBigDecimal("r30_fore_amt"));
obj.setR30_no_of_acc(rs.getBigDecimal("r30_no_of_acc"));


// =========================
// R31
// =========================
obj.setR31_sche_fore_ass(rs.getString("r31_sche_fore_ass"));
obj.setR31_orig_amt(rs.getBigDecimal("r31_orig_amt"));
obj.setR31_fore_amt(rs.getBigDecimal("r31_fore_amt"));
obj.setR31_no_of_acc(rs.getBigDecimal("r31_no_of_acc"));

// =========================
// R32
// =========================
obj.setR32_sche_fore_ass(rs.getString("r32_sche_fore_ass"));
obj.setR32_orig_amt(rs.getBigDecimal("r32_orig_amt"));
obj.setR32_fore_amt(rs.getBigDecimal("r32_fore_amt"));
obj.setR32_no_of_acc(rs.getBigDecimal("r32_no_of_acc"));

// =========================
// R33
// =========================
obj.setR33_sche_fore_ass(rs.getString("r33_sche_fore_ass"));
obj.setR33_orig_amt(rs.getBigDecimal("r33_orig_amt"));
obj.setR33_fore_amt(rs.getBigDecimal("r33_fore_amt"));
obj.setR33_no_of_acc(rs.getBigDecimal("r33_no_of_acc"));

// =========================
// R34
// =========================
obj.setR34_sche_fore_ass(rs.getString("r34_sche_fore_ass"));
obj.setR34_orig_amt(rs.getBigDecimal("r34_orig_amt"));
obj.setR34_fore_amt(rs.getBigDecimal("r34_fore_amt"));
obj.setR34_no_of_acc(rs.getBigDecimal("r34_no_of_acc"));

// =========================
// R35
// =========================
obj.setR35_sche_fore_ass(rs.getString("r35_sche_fore_ass"));
obj.setR35_orig_amt(rs.getBigDecimal("r35_orig_amt"));
obj.setR35_fore_amt(rs.getBigDecimal("r35_fore_amt"));
obj.setR35_no_of_acc(rs.getBigDecimal("r35_no_of_acc"));

// =========================
// R36
// =========================
obj.setR36_sche_fore_ass(rs.getString("r36_sche_fore_ass"));
obj.setR36_orig_amt(rs.getBigDecimal("r36_orig_amt"));
obj.setR36_fore_amt(rs.getBigDecimal("r36_fore_amt"));
obj.setR36_no_of_acc(rs.getBigDecimal("r36_no_of_acc"));

// =========================
// R37
// =========================
obj.setR37_sche_fore_ass(rs.getString("r37_sche_fore_ass"));
obj.setR37_orig_amt(rs.getBigDecimal("r37_orig_amt"));
obj.setR37_fore_amt(rs.getBigDecimal("r37_fore_amt"));
obj.setR37_no_of_acc(rs.getBigDecimal("r37_no_of_acc"));

// =========================
// R38
// =========================
obj.setR38_sche_fore_ass(rs.getString("r38_sche_fore_ass"));
obj.setR38_orig_amt(rs.getBigDecimal("r38_orig_amt"));
obj.setR38_fore_amt(rs.getBigDecimal("r38_fore_amt"));
obj.setR38_no_of_acc(rs.getBigDecimal("r38_no_of_acc"));

// =========================
// R39
// =========================
obj.setR39_sche_fore_ass(rs.getString("r39_sche_fore_ass"));
obj.setR39_orig_amt(rs.getBigDecimal("r39_orig_amt"));
obj.setR39_fore_amt(rs.getBigDecimal("r39_fore_amt"));
obj.setR39_no_of_acc(rs.getBigDecimal("r39_no_of_acc"));

// =========================
// R40
// =========================
obj.setR40_sche_fore_ass(rs.getString("r40_sche_fore_ass"));
obj.setR40_orig_amt(rs.getBigDecimal("r40_orig_amt"));
obj.setR40_fore_amt(rs.getBigDecimal("r40_fore_amt"));
obj.setR40_no_of_acc(rs.getBigDecimal("r40_no_of_acc"));

// =========================
// R41
// =========================
obj.setR41_sche_fore_ass(rs.getString("r41_sche_fore_ass"));
obj.setR41_orig_amt(rs.getBigDecimal("r41_orig_amt"));
obj.setR41_fore_amt(rs.getBigDecimal("r41_fore_amt"));
obj.setR41_no_of_acc(rs.getBigDecimal("r41_no_of_acc"));

// =========================
// R42
// =========================
obj.setR42_sche_fore_ass(rs.getString("r42_sche_fore_ass"));
obj.setR42_orig_amt(rs.getBigDecimal("r42_orig_amt"));
obj.setR42_fore_amt(rs.getBigDecimal("r42_fore_amt"));
obj.setR42_no_of_acc(rs.getBigDecimal("r42_no_of_acc"));

// =========================
// R43
// =========================
obj.setR43_sche_fore_ass(rs.getString("r43_sche_fore_ass"));
obj.setR43_orig_amt(rs.getBigDecimal("r43_orig_amt"));
obj.setR43_fore_amt(rs.getBigDecimal("r43_fore_amt"));
obj.setR43_no_of_acc(rs.getBigDecimal("r43_no_of_acc"));

// =========================
// R44
// =========================
obj.setR44_sche_fore_ass(rs.getString("r44_sche_fore_ass"));
obj.setR44_orig_amt(rs.getBigDecimal("r44_orig_amt"));
obj.setR44_fore_amt(rs.getBigDecimal("r44_fore_amt"));
obj.setR44_no_of_acc(rs.getBigDecimal("r44_no_of_acc"));

// =========================
// R45
// =========================
obj.setR45_sche_fore_ass(rs.getString("r45_sche_fore_ass"));
obj.setR45_orig_amt(rs.getBigDecimal("r45_orig_amt"));
obj.setR45_fore_amt(rs.getBigDecimal("r45_fore_amt"));
obj.setR45_no_of_acc(rs.getBigDecimal("r45_no_of_acc"));

// =========================
// R46
// =========================
obj.setR46_sche_fore_ass(rs.getString("r46_sche_fore_ass"));
obj.setR46_orig_amt(rs.getBigDecimal("r46_orig_amt"));
obj.setR46_fore_amt(rs.getBigDecimal("r46_fore_amt"));
obj.setR46_no_of_acc(rs.getBigDecimal("r46_no_of_acc"));

// =========================
// R47
// =========================
obj.setR47_sche_fore_ass(rs.getString("r47_sche_fore_ass"));
obj.setR47_orig_amt(rs.getBigDecimal("r47_orig_amt"));
obj.setR47_fore_amt(rs.getBigDecimal("r47_fore_amt"));
obj.setR47_no_of_acc(rs.getBigDecimal("r47_no_of_acc"));

// =========================
// R48
// =========================
obj.setR48_sche_fore_ass(rs.getString("r48_sche_fore_ass"));
obj.setR48_orig_amt(rs.getBigDecimal("r48_orig_amt"));
obj.setR48_fore_amt(rs.getBigDecimal("r48_fore_amt"));
obj.setR48_no_of_acc(rs.getBigDecimal("r48_no_of_acc"));

// =========================
// R49
// =========================
obj.setR49_sche_fore_ass(rs.getString("r49_sche_fore_ass"));
obj.setR49_orig_amt(rs.getBigDecimal("r49_orig_amt"));
obj.setR49_fore_amt(rs.getBigDecimal("r49_fore_amt"));
obj.setR49_no_of_acc(rs.getBigDecimal("r49_no_of_acc"));

// =========================
// R50
// =========================
obj.setR50_sche_fore_ass(rs.getString("r50_sche_fore_ass"));
obj.setR50_orig_amt(rs.getBigDecimal("r50_orig_amt"));
obj.setR50_fore_amt(rs.getBigDecimal("r50_fore_amt"));
obj.setR50_no_of_acc(rs.getBigDecimal("r50_no_of_acc"));

// =========================
// R51
// =========================
obj.setR51_sche_fore_ass(rs.getString("r51_sche_fore_ass"));
obj.setR51_orig_amt(rs.getBigDecimal("r51_orig_amt"));
obj.setR51_fore_amt(rs.getBigDecimal("r51_fore_amt"));
obj.setR51_no_of_acc(rs.getBigDecimal("r51_no_of_acc"));

// =========================
// R52
// =========================
obj.setR52_sche_fore_ass(rs.getString("r52_sche_fore_ass"));
obj.setR52_orig_amt(rs.getBigDecimal("r52_orig_amt"));
obj.setR52_fore_amt(rs.getBigDecimal("r52_fore_amt"));
obj.setR52_no_of_acc(rs.getBigDecimal("r52_no_of_acc"));

// =========================
// R53
// =========================
obj.setR53_sche_fore_ass(rs.getString("r53_sche_fore_ass"));
obj.setR53_orig_amt(rs.getBigDecimal("r53_orig_amt"));
obj.setR53_fore_amt(rs.getBigDecimal("r53_fore_amt"));
obj.setR53_no_of_acc(rs.getBigDecimal("r53_no_of_acc"));

// =========================
// R54
// =========================
obj.setR54_sche_fore_ass(rs.getString("r54_sche_fore_ass"));
obj.setR54_orig_amt(rs.getBigDecimal("r54_orig_amt"));
obj.setR54_fore_amt(rs.getBigDecimal("r54_fore_amt"));
obj.setR54_no_of_acc(rs.getBigDecimal("r54_no_of_acc"));

// =========================
// R55
// =========================
obj.setR55_sche_fore_ass(rs.getString("r55_sche_fore_ass"));
obj.setR55_orig_amt(rs.getBigDecimal("r55_orig_amt"));
obj.setR55_fore_amt(rs.getBigDecimal("r55_fore_amt"));
obj.setR55_no_of_acc(rs.getBigDecimal("r55_no_of_acc"));

// =========================
// R56
// =========================
obj.setR56_sche_fore_ass(rs.getString("r56_sche_fore_ass"));
obj.setR56_orig_amt(rs.getBigDecimal("r56_orig_amt"));
obj.setR56_fore_amt(rs.getBigDecimal("r56_fore_amt"));
obj.setR56_no_of_acc(rs.getBigDecimal("r56_no_of_acc"));

// =========================
// R57
// =========================
obj.setR57_sche_fore_ass(rs.getString("r57_sche_fore_ass"));
obj.setR57_orig_amt(rs.getBigDecimal("r57_orig_amt"));
obj.setR57_fore_amt(rs.getBigDecimal("r57_fore_amt"));
obj.setR57_no_of_acc(rs.getBigDecimal("r57_no_of_acc"));

// =========================
// R58
// =========================
obj.setR58_sche_fore_ass(rs.getString("r58_sche_fore_ass"));
obj.setR58_orig_amt(rs.getBigDecimal("r58_orig_amt"));
obj.setR58_fore_amt(rs.getBigDecimal("r58_fore_amt"));
obj.setR58_no_of_acc(rs.getBigDecimal("r58_no_of_acc"));

// =========================
// R59
// =========================
obj.setR59_sche_fore_ass(rs.getString("r59_sche_fore_ass"));
obj.setR59_orig_amt(rs.getBigDecimal("r59_orig_amt"));
obj.setR59_fore_amt(rs.getBigDecimal("r59_fore_amt"));
obj.setR59_no_of_acc(rs.getBigDecimal("r59_no_of_acc"));

// =========================
// R60
// =========================
obj.setR60_sche_fore_ass(rs.getString("r60_sche_fore_ass"));
obj.setR60_orig_amt(rs.getBigDecimal("r60_orig_amt"));
obj.setR60_fore_amt(rs.getBigDecimal("r60_fore_amt"));
obj.setR60_no_of_acc(rs.getBigDecimal("r60_no_of_acc"));

// =========================
// R61
// =========================
obj.setR61_sche_fore_ass(rs.getString("r61_sche_fore_ass"));
obj.setR61_orig_amt(rs.getBigDecimal("r61_orig_amt"));
obj.setR61_fore_amt(rs.getBigDecimal("r61_fore_amt"));
obj.setR61_no_of_acc(rs.getBigDecimal("r61_no_of_acc"));

// =========================
// R62
// =========================
obj.setR62_sche_fore_ass(rs.getString("r62_sche_fore_ass"));
obj.setR62_orig_amt(rs.getBigDecimal("r62_orig_amt"));
obj.setR62_fore_amt(rs.getBigDecimal("r62_fore_amt"));
obj.setR62_no_of_acc(rs.getBigDecimal("r62_no_of_acc"));

// =========================
// R63
// =========================
obj.setR63_sche_fore_ass(rs.getString("r63_sche_fore_ass"));
obj.setR63_orig_amt(rs.getBigDecimal("r63_orig_amt"));
obj.setR63_fore_amt(rs.getBigDecimal("r63_fore_amt"));
obj.setR63_no_of_acc(rs.getBigDecimal("r63_no_of_acc"));

// =========================
// R64
// =========================
obj.setR64_sche_fore_ass(rs.getString("r64_sche_fore_ass"));
obj.setR64_orig_amt(rs.getBigDecimal("r64_orig_amt"));
obj.setR64_fore_amt(rs.getBigDecimal("r64_fore_amt"));
obj.setR64_no_of_acc(rs.getBigDecimal("r64_no_of_acc"));

// =========================
// R65
// =========================
obj.setR65_sche_fore_ass(rs.getString("r65_sche_fore_ass"));
obj.setR65_orig_amt(rs.getBigDecimal("r65_orig_amt"));
obj.setR65_fore_amt(rs.getBigDecimal("r65_fore_amt"));
obj.setR65_no_of_acc(rs.getBigDecimal("r65_no_of_acc"));

// =========================
// R66
// =========================
obj.setR66_sche_fore_ass(rs.getString("r66_sche_fore_ass"));
obj.setR66_orig_amt(rs.getBigDecimal("r66_orig_amt"));
obj.setR66_fore_amt(rs.getBigDecimal("r66_fore_amt"));
obj.setR66_no_of_acc(rs.getBigDecimal("r66_no_of_acc"));

// =========================
// R67
// =========================
obj.setR67_sche_fore_ass(rs.getString("r67_sche_fore_ass"));
obj.setR67_orig_amt(rs.getBigDecimal("r67_orig_amt"));
obj.setR67_fore_amt(rs.getBigDecimal("r67_fore_amt"));
obj.setR67_no_of_acc(rs.getBigDecimal("r67_no_of_acc"));


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

public class Q_RLFA2_RESUB_Detail_Entity {

   
	private String	r10_sche_fore_ass;
	private BigDecimal	r10_orig_amt;
	private BigDecimal	r10_fore_amt;
	private BigDecimal	r10_no_of_acc;
	
	private String	r11_sche_fore_ass;
	private BigDecimal	r11_orig_amt;
	private BigDecimal	r11_fore_amt;
	private BigDecimal	r11_no_of_acc;
	
	private String	r12_sche_fore_ass;
	private BigDecimal	r12_orig_amt;
	private BigDecimal	r12_fore_amt;
	private BigDecimal	r12_no_of_acc;
	
	private String	r13_sche_fore_ass;
	private BigDecimal	r13_orig_amt;
	private BigDecimal	r13_fore_amt;
	private BigDecimal	r13_no_of_acc;
	
	private String	r14_sche_fore_ass;
	private BigDecimal	r14_orig_amt;
	private BigDecimal	r14_fore_amt;
	private BigDecimal	r14_no_of_acc;
	
	private String	r15_sche_fore_ass;
	private BigDecimal	r15_orig_amt;
	private BigDecimal	r15_fore_amt;
	private BigDecimal	r15_no_of_acc;
	
	private String	r16_sche_fore_ass;
	private BigDecimal	r16_orig_amt;
	private BigDecimal	r16_fore_amt;
	private BigDecimal	r16_no_of_acc;
	
	private String	r17_sche_fore_ass;
	private BigDecimal	r17_orig_amt;
	private BigDecimal	r17_fore_amt;
	private BigDecimal	r17_no_of_acc;
	
	private String	r18_sche_fore_ass;
	private BigDecimal	r18_orig_amt;
	private BigDecimal	r18_fore_amt;
	private BigDecimal	r18_no_of_acc;
	
	private String	r19_sche_fore_ass;
	private BigDecimal	r19_orig_amt;
	private BigDecimal	r19_fore_amt;
	private BigDecimal	r19_no_of_acc;
	
	private String	r20_sche_fore_ass;
	private BigDecimal	r20_orig_amt;
	private BigDecimal	r20_fore_amt;
	private BigDecimal	r20_no_of_acc;
	
	private String	r21_sche_fore_ass;
	private BigDecimal	r21_orig_amt;
	private BigDecimal	r21_fore_amt;
	private BigDecimal	r21_no_of_acc;
	
	private String	r22_sche_fore_ass;
	private BigDecimal	r22_orig_amt;
	private BigDecimal	r22_fore_amt;
	private BigDecimal	r22_no_of_acc;
	
	private String	r23_sche_fore_ass;
	private BigDecimal	r23_orig_amt;
	private BigDecimal	r23_fore_amt;
	private BigDecimal	r23_no_of_acc;
	
	private String	r24_sche_fore_ass;
	private BigDecimal	r24_orig_amt;
	private BigDecimal	r24_fore_amt;
	private BigDecimal	r24_no_of_acc;
	
	private String	r25_sche_fore_ass;
	private BigDecimal	r25_orig_amt;
	private BigDecimal	r25_fore_amt;
	private BigDecimal	r25_no_of_acc;
	
	private String	r26_sche_fore_ass;
	private BigDecimal	r26_orig_amt;
	private BigDecimal	r26_fore_amt;
	private BigDecimal	r26_no_of_acc;
	
	private String	r27_sche_fore_ass;
	private BigDecimal	r27_orig_amt;
	private BigDecimal	r27_fore_amt;
	private BigDecimal	r27_no_of_acc;
	
	private String	r28_sche_fore_ass;
	private BigDecimal	r28_orig_amt;
	private BigDecimal	r28_fore_amt;
	private BigDecimal	r28_no_of_acc;
	
	private String	r29_sche_fore_ass;
	private BigDecimal	r29_orig_amt;
	private BigDecimal	r29_fore_amt;
	private BigDecimal	r29_no_of_acc;
	
	private String	r30_sche_fore_ass;
	private BigDecimal	r30_orig_amt;
	private BigDecimal	r30_fore_amt;
	private BigDecimal	r30_no_of_acc;
	
	private String	r31_sche_fore_ass;
	private BigDecimal	r31_orig_amt;
	private BigDecimal	r31_fore_amt;
	private BigDecimal	r31_no_of_acc;
	
	private String	r32_sche_fore_ass;
	private BigDecimal	r32_orig_amt;
	private BigDecimal	r32_fore_amt;
	private BigDecimal	r32_no_of_acc;
	
	private String	r33_sche_fore_ass;
	private BigDecimal	r33_orig_amt;
	private BigDecimal	r33_fore_amt;
	private BigDecimal	r33_no_of_acc;
	
	private String	r34_sche_fore_ass;
	private BigDecimal	r34_orig_amt;
	private BigDecimal	r34_fore_amt;
	private BigDecimal	r34_no_of_acc;
	
	private String	r35_sche_fore_ass;
	private BigDecimal	r35_orig_amt;
	private BigDecimal	r35_fore_amt;
	private BigDecimal	r35_no_of_acc;
	
	private String	r36_sche_fore_ass;
	private BigDecimal	r36_orig_amt;
	private BigDecimal	r36_fore_amt;
	private BigDecimal	r36_no_of_acc;
	
	private String	r37_sche_fore_ass;
	private BigDecimal	r37_orig_amt;
	private BigDecimal	r37_fore_amt;
	private BigDecimal	r37_no_of_acc;
	
	private String	r38_sche_fore_ass;
	private BigDecimal	r38_orig_amt;
	private BigDecimal	r38_fore_amt;
	private BigDecimal	r38_no_of_acc;
	
	private String	r39_sche_fore_ass;
	private BigDecimal	r39_orig_amt;
	private BigDecimal	r39_fore_amt;
	private BigDecimal	r39_no_of_acc;
	
	private String	r40_sche_fore_ass;
	private BigDecimal	r40_orig_amt;
	private BigDecimal	r40_fore_amt;
	private BigDecimal	r40_no_of_acc;
	
	private String	r41_sche_fore_ass;
	private BigDecimal	r41_orig_amt;
	private BigDecimal	r41_fore_amt;
	private BigDecimal	r41_no_of_acc;
	
	private String	r42_sche_fore_ass;
	private BigDecimal	r42_orig_amt;
	private BigDecimal	r42_fore_amt;
	private BigDecimal	r42_no_of_acc;
	
	private String	r43_sche_fore_ass;
	private BigDecimal	r43_orig_amt;
	private BigDecimal	r43_fore_amt;
	private BigDecimal	r43_no_of_acc;
	
	private String	r44_sche_fore_ass;
	private BigDecimal	r44_orig_amt;
	private BigDecimal	r44_fore_amt;
	private BigDecimal	r44_no_of_acc;
	
	private String	r45_sche_fore_ass;
	private BigDecimal	r45_orig_amt;
	private BigDecimal	r45_fore_amt;
	private BigDecimal	r45_no_of_acc;
	
	private String	r46_sche_fore_ass;
	private BigDecimal	r46_orig_amt;
	private BigDecimal	r46_fore_amt;
	private BigDecimal	r46_no_of_acc;
	
	private String	r47_sche_fore_ass;
	private BigDecimal	r47_orig_amt;
	private BigDecimal	r47_fore_amt;
	private BigDecimal	r47_no_of_acc;
	
	private String	r48_sche_fore_ass;
	private BigDecimal	r48_orig_amt;
	private BigDecimal	r48_fore_amt;
	private BigDecimal	r48_no_of_acc;
	
	private String	r49_sche_fore_ass;
	private BigDecimal	r49_orig_amt;
	private BigDecimal	r49_fore_amt;
	private BigDecimal	r49_no_of_acc;
	
	private String	r50_sche_fore_ass;
	private BigDecimal	r50_orig_amt;
	private BigDecimal	r50_fore_amt;
	private BigDecimal	r50_no_of_acc;
	
	private String	r51_sche_fore_ass;
	private BigDecimal	r51_orig_amt;
	private BigDecimal	r51_fore_amt;
	private BigDecimal	r51_no_of_acc;
	
	private String	r52_sche_fore_ass;
	private BigDecimal	r52_orig_amt;
	private BigDecimal	r52_fore_amt;
	private BigDecimal	r52_no_of_acc;
	
	private String	r53_sche_fore_ass;
	private BigDecimal	r53_orig_amt;
	private BigDecimal	r53_fore_amt;
	private BigDecimal	r53_no_of_acc;
	
	private String	r54_sche_fore_ass;
	private BigDecimal	r54_orig_amt;
	private BigDecimal	r54_fore_amt;
	private BigDecimal	r54_no_of_acc;
	
	private String	r55_sche_fore_ass;
	private BigDecimal	r55_orig_amt;
	private BigDecimal	r55_fore_amt;
	private BigDecimal	r55_no_of_acc;
	
	private String	r56_sche_fore_ass;
	private BigDecimal	r56_orig_amt;
	private BigDecimal	r56_fore_amt;
	private BigDecimal	r56_no_of_acc;
	
	private String	r57_sche_fore_ass;
	private BigDecimal	r57_orig_amt;
	private BigDecimal	r57_fore_amt;
	private BigDecimal	r57_no_of_acc;
	
	private String	r58_sche_fore_ass;
	private BigDecimal	r58_orig_amt;
	private BigDecimal	r58_fore_amt;
	private BigDecimal	r58_no_of_acc;
	
	private String	r59_sche_fore_ass;
	private BigDecimal	r59_orig_amt;
	private BigDecimal	r59_fore_amt;
	private BigDecimal	r59_no_of_acc;
	
	private String	r60_sche_fore_ass;
	private BigDecimal	r60_orig_amt;
	private BigDecimal	r60_fore_amt;
	private BigDecimal	r60_no_of_acc;
	
	private String	r61_sche_fore_ass;
	private BigDecimal	r61_orig_amt;
	private BigDecimal	r61_fore_amt;
	private BigDecimal	r61_no_of_acc;
	
	private String	r62_sche_fore_ass;
	private BigDecimal	r62_orig_amt;
	private BigDecimal	r62_fore_amt;
	private BigDecimal	r62_no_of_acc;
	
	private String	r63_sche_fore_ass;
	private BigDecimal	r63_orig_amt;
	private BigDecimal	r63_fore_amt;
	private BigDecimal	r63_no_of_acc;
	
	// R64
	private String     r64_sche_fore_ass;
	private BigDecimal r64_orig_amt;
	private BigDecimal r64_fore_amt;
	private BigDecimal r64_no_of_acc;

	// R65
	private String     r65_sche_fore_ass;
	private BigDecimal r65_orig_amt;
	private BigDecimal r65_fore_amt;
	private BigDecimal r65_no_of_acc;

	// R66
	private String     r66_sche_fore_ass;
	private BigDecimal r66_orig_amt;
	private BigDecimal r66_fore_amt;
	private BigDecimal r66_no_of_acc;

	// R67
	private String     r67_sche_fore_ass;
	private BigDecimal r67_orig_amt;
	private BigDecimal r67_fore_amt;
	private BigDecimal r67_no_of_acc;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id	
	private Date	report_date;
	
	@Id
	private BigDecimal	report_version;
	
	@Column(name = "REPORT_RESUBDATE")
    private Date reportResubDate;
	
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	 del_flg;
	
	
		public String getR10_sche_fore_ass() {
		return r10_sche_fore_ass;
	}
	public void setR10_sche_fore_ass(String r10_sche_fore_ass) {
		this.r10_sche_fore_ass = r10_sche_fore_ass;
	}
	public BigDecimal getR10_orig_amt() {
		return r10_orig_amt;
	}
	public void setR10_orig_amt(BigDecimal r10_orig_amt) {
		this.r10_orig_amt = r10_orig_amt;
	}
	public BigDecimal getR10_fore_amt() {
		return r10_fore_amt;
	}
	public void setR10_fore_amt(BigDecimal r10_fore_amt) {
		this.r10_fore_amt = r10_fore_amt;
	}
	public BigDecimal getR10_no_of_acc() {
		return r10_no_of_acc;
	}
	public void setR10_no_of_acc(BigDecimal r10_no_of_acc) {
		this.r10_no_of_acc = r10_no_of_acc;
	}
	public String getR11_sche_fore_ass() {
		return r11_sche_fore_ass;
	}
	public void setR11_sche_fore_ass(String r11_sche_fore_ass) {
		this.r11_sche_fore_ass = r11_sche_fore_ass;
	}
	public BigDecimal getR11_orig_amt() {
		return r11_orig_amt;
	}
	public void setR11_orig_amt(BigDecimal r11_orig_amt) {
		this.r11_orig_amt = r11_orig_amt;
	}
	public BigDecimal getR11_fore_amt() {
		return r11_fore_amt;
	}
	public void setR11_fore_amt(BigDecimal r11_fore_amt) {
		this.r11_fore_amt = r11_fore_amt;
	}
	public BigDecimal getR11_no_of_acc() {
		return r11_no_of_acc;
	}
	public void setR11_no_of_acc(BigDecimal r11_no_of_acc) {
		this.r11_no_of_acc = r11_no_of_acc;
	}
	public String getR12_sche_fore_ass() {
		return r12_sche_fore_ass;
	}
	public void setR12_sche_fore_ass(String r12_sche_fore_ass) {
		this.r12_sche_fore_ass = r12_sche_fore_ass;
	}
	public BigDecimal getR12_orig_amt() {
		return r12_orig_amt;
	}
	public void setR12_orig_amt(BigDecimal r12_orig_amt) {
		this.r12_orig_amt = r12_orig_amt;
	}
	public BigDecimal getR12_fore_amt() {
		return r12_fore_amt;
	}
	public void setR12_fore_amt(BigDecimal r12_fore_amt) {
		this.r12_fore_amt = r12_fore_amt;
	}
	public BigDecimal getR12_no_of_acc() {
		return r12_no_of_acc;
	}
	public void setR12_no_of_acc(BigDecimal r12_no_of_acc) {
		this.r12_no_of_acc = r12_no_of_acc;
	}
	public String getR13_sche_fore_ass() {
		return r13_sche_fore_ass;
	}
	public void setR13_sche_fore_ass(String r13_sche_fore_ass) {
		this.r13_sche_fore_ass = r13_sche_fore_ass;
	}
	public BigDecimal getR13_orig_amt() {
		return r13_orig_amt;
	}
	public void setR13_orig_amt(BigDecimal r13_orig_amt) {
		this.r13_orig_amt = r13_orig_amt;
	}
	public BigDecimal getR13_fore_amt() {
		return r13_fore_amt;
	}
	public void setR13_fore_amt(BigDecimal r13_fore_amt) {
		this.r13_fore_amt = r13_fore_amt;
	}
	public BigDecimal getR13_no_of_acc() {
		return r13_no_of_acc;
	}
	public void setR13_no_of_acc(BigDecimal r13_no_of_acc) {
		this.r13_no_of_acc = r13_no_of_acc;
	}
	public String getR14_sche_fore_ass() {
		return r14_sche_fore_ass;
	}
	public void setR14_sche_fore_ass(String r14_sche_fore_ass) {
		this.r14_sche_fore_ass = r14_sche_fore_ass;
	}
	public BigDecimal getR14_orig_amt() {
		return r14_orig_amt;
	}
	public void setR14_orig_amt(BigDecimal r14_orig_amt) {
		this.r14_orig_amt = r14_orig_amt;
	}
	public BigDecimal getR14_fore_amt() {
		return r14_fore_amt;
	}
	public void setR14_fore_amt(BigDecimal r14_fore_amt) {
		this.r14_fore_amt = r14_fore_amt;
	}
	public BigDecimal getR14_no_of_acc() {
		return r14_no_of_acc;
	}
	public void setR14_no_of_acc(BigDecimal r14_no_of_acc) {
		this.r14_no_of_acc = r14_no_of_acc;
	}
	public String getR15_sche_fore_ass() {
		return r15_sche_fore_ass;
	}
	public void setR15_sche_fore_ass(String r15_sche_fore_ass) {
		this.r15_sche_fore_ass = r15_sche_fore_ass;
	}
	public BigDecimal getR15_orig_amt() {
		return r15_orig_amt;
	}
	public void setR15_orig_amt(BigDecimal r15_orig_amt) {
		this.r15_orig_amt = r15_orig_amt;
	}
	public BigDecimal getR15_fore_amt() {
		return r15_fore_amt;
	}
	public void setR15_fore_amt(BigDecimal r15_fore_amt) {
		this.r15_fore_amt = r15_fore_amt;
	}
	public BigDecimal getR15_no_of_acc() {
		return r15_no_of_acc;
	}
	public void setR15_no_of_acc(BigDecimal r15_no_of_acc) {
		this.r15_no_of_acc = r15_no_of_acc;
	}
	public String getR16_sche_fore_ass() {
		return r16_sche_fore_ass;
	}
	public void setR16_sche_fore_ass(String r16_sche_fore_ass) {
		this.r16_sche_fore_ass = r16_sche_fore_ass;
	}
	public BigDecimal getR16_orig_amt() {
		return r16_orig_amt;
	}
	public void setR16_orig_amt(BigDecimal r16_orig_amt) {
		this.r16_orig_amt = r16_orig_amt;
	}
	public BigDecimal getR16_fore_amt() {
		return r16_fore_amt;
	}
	public void setR16_fore_amt(BigDecimal r16_fore_amt) {
		this.r16_fore_amt = r16_fore_amt;
	}
	public BigDecimal getR16_no_of_acc() {
		return r16_no_of_acc;
	}
	public void setR16_no_of_acc(BigDecimal r16_no_of_acc) {
		this.r16_no_of_acc = r16_no_of_acc;
	}
	public String getR17_sche_fore_ass() {
		return r17_sche_fore_ass;
	}
	public void setR17_sche_fore_ass(String r17_sche_fore_ass) {
		this.r17_sche_fore_ass = r17_sche_fore_ass;
	}
	public BigDecimal getR17_orig_amt() {
		return r17_orig_amt;
	}
	public void setR17_orig_amt(BigDecimal r17_orig_amt) {
		this.r17_orig_amt = r17_orig_amt;
	}
	public BigDecimal getR17_fore_amt() {
		return r17_fore_amt;
	}
	public void setR17_fore_amt(BigDecimal r17_fore_amt) {
		this.r17_fore_amt = r17_fore_amt;
	}
	public BigDecimal getR17_no_of_acc() {
		return r17_no_of_acc;
	}
	public void setR17_no_of_acc(BigDecimal r17_no_of_acc) {
		this.r17_no_of_acc = r17_no_of_acc;
	}
	public String getR18_sche_fore_ass() {
		return r18_sche_fore_ass;
	}
	public void setR18_sche_fore_ass(String r18_sche_fore_ass) {
		this.r18_sche_fore_ass = r18_sche_fore_ass;
	}
	public BigDecimal getR18_orig_amt() {
		return r18_orig_amt;
	}
	public void setR18_orig_amt(BigDecimal r18_orig_amt) {
		this.r18_orig_amt = r18_orig_amt;
	}
	public BigDecimal getR18_fore_amt() {
		return r18_fore_amt;
	}
	public void setR18_fore_amt(BigDecimal r18_fore_amt) {
		this.r18_fore_amt = r18_fore_amt;
	}
	public BigDecimal getR18_no_of_acc() {
		return r18_no_of_acc;
	}
	public void setR18_no_of_acc(BigDecimal r18_no_of_acc) {
		this.r18_no_of_acc = r18_no_of_acc;
	}
	public String getR19_sche_fore_ass() {
		return r19_sche_fore_ass;
	}
	public void setR19_sche_fore_ass(String r19_sche_fore_ass) {
		this.r19_sche_fore_ass = r19_sche_fore_ass;
	}
	public BigDecimal getR19_orig_amt() {
		return r19_orig_amt;
	}
	public void setR19_orig_amt(BigDecimal r19_orig_amt) {
		this.r19_orig_amt = r19_orig_amt;
	}
	public BigDecimal getR19_fore_amt() {
		return r19_fore_amt;
	}
	public void setR19_fore_amt(BigDecimal r19_fore_amt) {
		this.r19_fore_amt = r19_fore_amt;
	}
	public BigDecimal getR19_no_of_acc() {
		return r19_no_of_acc;
	}
	public void setR19_no_of_acc(BigDecimal r19_no_of_acc) {
		this.r19_no_of_acc = r19_no_of_acc;
	}
	public String getR20_sche_fore_ass() {
		return r20_sche_fore_ass;
	}
	public void setR20_sche_fore_ass(String r20_sche_fore_ass) {
		this.r20_sche_fore_ass = r20_sche_fore_ass;
	}
	public BigDecimal getR20_orig_amt() {
		return r20_orig_amt;
	}
	public void setR20_orig_amt(BigDecimal r20_orig_amt) {
		this.r20_orig_amt = r20_orig_amt;
	}
	public BigDecimal getR20_fore_amt() {
		return r20_fore_amt;
	}
	public void setR20_fore_amt(BigDecimal r20_fore_amt) {
		this.r20_fore_amt = r20_fore_amt;
	}
	public BigDecimal getR20_no_of_acc() {
		return r20_no_of_acc;
	}
	public void setR20_no_of_acc(BigDecimal r20_no_of_acc) {
		this.r20_no_of_acc = r20_no_of_acc;
	}
	public String getR21_sche_fore_ass() {
		return r21_sche_fore_ass;
	}
	public void setR21_sche_fore_ass(String r21_sche_fore_ass) {
		this.r21_sche_fore_ass = r21_sche_fore_ass;
	}
	public BigDecimal getR21_orig_amt() {
		return r21_orig_amt;
	}
	public void setR21_orig_amt(BigDecimal r21_orig_amt) {
		this.r21_orig_amt = r21_orig_amt;
	}
	public BigDecimal getR21_fore_amt() {
		return r21_fore_amt;
	}
	public void setR21_fore_amt(BigDecimal r21_fore_amt) {
		this.r21_fore_amt = r21_fore_amt;
	}
	public BigDecimal getR21_no_of_acc() {
		return r21_no_of_acc;
	}
	public void setR21_no_of_acc(BigDecimal r21_no_of_acc) {
		this.r21_no_of_acc = r21_no_of_acc;
	}
	public String getR22_sche_fore_ass() {
		return r22_sche_fore_ass;
	}
	public void setR22_sche_fore_ass(String r22_sche_fore_ass) {
		this.r22_sche_fore_ass = r22_sche_fore_ass;
	}
	public BigDecimal getR22_orig_amt() {
		return r22_orig_amt;
	}
	public void setR22_orig_amt(BigDecimal r22_orig_amt) {
		this.r22_orig_amt = r22_orig_amt;
	}
	public BigDecimal getR22_fore_amt() {
		return r22_fore_amt;
	}
	public void setR22_fore_amt(BigDecimal r22_fore_amt) {
		this.r22_fore_amt = r22_fore_amt;
	}
	public BigDecimal getR22_no_of_acc() {
		return r22_no_of_acc;
	}
	public void setR22_no_of_acc(BigDecimal r22_no_of_acc) {
		this.r22_no_of_acc = r22_no_of_acc;
	}
	public String getR23_sche_fore_ass() {
		return r23_sche_fore_ass;
	}
	public void setR23_sche_fore_ass(String r23_sche_fore_ass) {
		this.r23_sche_fore_ass = r23_sche_fore_ass;
	}
	public BigDecimal getR23_orig_amt() {
		return r23_orig_amt;
	}
	public void setR23_orig_amt(BigDecimal r23_orig_amt) {
		this.r23_orig_amt = r23_orig_amt;
	}
	public BigDecimal getR23_fore_amt() {
		return r23_fore_amt;
	}
	public void setR23_fore_amt(BigDecimal r23_fore_amt) {
		this.r23_fore_amt = r23_fore_amt;
	}
	public BigDecimal getR23_no_of_acc() {
		return r23_no_of_acc;
	}
	public void setR23_no_of_acc(BigDecimal r23_no_of_acc) {
		this.r23_no_of_acc = r23_no_of_acc;
	}
	public String getR24_sche_fore_ass() {
		return r24_sche_fore_ass;
	}
	public void setR24_sche_fore_ass(String r24_sche_fore_ass) {
		this.r24_sche_fore_ass = r24_sche_fore_ass;
	}
	public BigDecimal getR24_orig_amt() {
		return r24_orig_amt;
	}
	public void setR24_orig_amt(BigDecimal r24_orig_amt) {
		this.r24_orig_amt = r24_orig_amt;
	}
	public BigDecimal getR24_fore_amt() {
		return r24_fore_amt;
	}
	public void setR24_fore_amt(BigDecimal r24_fore_amt) {
		this.r24_fore_amt = r24_fore_amt;
	}
	public BigDecimal getR24_no_of_acc() {
		return r24_no_of_acc;
	}
	public void setR24_no_of_acc(BigDecimal r24_no_of_acc) {
		this.r24_no_of_acc = r24_no_of_acc;
	}
	public String getR25_sche_fore_ass() {
		return r25_sche_fore_ass;
	}
	public void setR25_sche_fore_ass(String r25_sche_fore_ass) {
		this.r25_sche_fore_ass = r25_sche_fore_ass;
	}
	public BigDecimal getR25_orig_amt() {
		return r25_orig_amt;
	}
	public void setR25_orig_amt(BigDecimal r25_orig_amt) {
		this.r25_orig_amt = r25_orig_amt;
	}
	public BigDecimal getR25_fore_amt() {
		return r25_fore_amt;
	}
	public void setR25_fore_amt(BigDecimal r25_fore_amt) {
		this.r25_fore_amt = r25_fore_amt;
	}
	public BigDecimal getR25_no_of_acc() {
		return r25_no_of_acc;
	}
	public void setR25_no_of_acc(BigDecimal r25_no_of_acc) {
		this.r25_no_of_acc = r25_no_of_acc;
	}
	public String getR26_sche_fore_ass() {
		return r26_sche_fore_ass;
	}
	public void setR26_sche_fore_ass(String r26_sche_fore_ass) {
		this.r26_sche_fore_ass = r26_sche_fore_ass;
	}
	public BigDecimal getR26_orig_amt() {
		return r26_orig_amt;
	}
	public void setR26_orig_amt(BigDecimal r26_orig_amt) {
		this.r26_orig_amt = r26_orig_amt;
	}
	public BigDecimal getR26_fore_amt() {
		return r26_fore_amt;
	}
	public void setR26_fore_amt(BigDecimal r26_fore_amt) {
		this.r26_fore_amt = r26_fore_amt;
	}
	public BigDecimal getR26_no_of_acc() {
		return r26_no_of_acc;
	}
	public void setR26_no_of_acc(BigDecimal r26_no_of_acc) {
		this.r26_no_of_acc = r26_no_of_acc;
	}
	public String getR27_sche_fore_ass() {
		return r27_sche_fore_ass;
	}
	public void setR27_sche_fore_ass(String r27_sche_fore_ass) {
		this.r27_sche_fore_ass = r27_sche_fore_ass;
	}
	public BigDecimal getR27_orig_amt() {
		return r27_orig_amt;
	}
	public void setR27_orig_amt(BigDecimal r27_orig_amt) {
		this.r27_orig_amt = r27_orig_amt;
	}
	public BigDecimal getR27_fore_amt() {
		return r27_fore_amt;
	}
	public void setR27_fore_amt(BigDecimal r27_fore_amt) {
		this.r27_fore_amt = r27_fore_amt;
	}
	public BigDecimal getR27_no_of_acc() {
		return r27_no_of_acc;
	}
	public void setR27_no_of_acc(BigDecimal r27_no_of_acc) {
		this.r27_no_of_acc = r27_no_of_acc;
	}
	public String getR28_sche_fore_ass() {
		return r28_sche_fore_ass;
	}
	public void setR28_sche_fore_ass(String r28_sche_fore_ass) {
		this.r28_sche_fore_ass = r28_sche_fore_ass;
	}
	public BigDecimal getR28_orig_amt() {
		return r28_orig_amt;
	}
	public void setR28_orig_amt(BigDecimal r28_orig_amt) {
		this.r28_orig_amt = r28_orig_amt;
	}
	public BigDecimal getR28_fore_amt() {
		return r28_fore_amt;
	}
	public void setR28_fore_amt(BigDecimal r28_fore_amt) {
		this.r28_fore_amt = r28_fore_amt;
	}
	public BigDecimal getR28_no_of_acc() {
		return r28_no_of_acc;
	}
	public void setR28_no_of_acc(BigDecimal r28_no_of_acc) {
		this.r28_no_of_acc = r28_no_of_acc;
	}
	public String getR29_sche_fore_ass() {
		return r29_sche_fore_ass;
	}
	public void setR29_sche_fore_ass(String r29_sche_fore_ass) {
		this.r29_sche_fore_ass = r29_sche_fore_ass;
	}
	public BigDecimal getR29_orig_amt() {
		return r29_orig_amt;
	}
	public void setR29_orig_amt(BigDecimal r29_orig_amt) {
		this.r29_orig_amt = r29_orig_amt;
	}
	public BigDecimal getR29_fore_amt() {
		return r29_fore_amt;
	}
	public void setR29_fore_amt(BigDecimal r29_fore_amt) {
		this.r29_fore_amt = r29_fore_amt;
	}
	public BigDecimal getR29_no_of_acc() {
		return r29_no_of_acc;
	}
	public void setR29_no_of_acc(BigDecimal r29_no_of_acc) {
		this.r29_no_of_acc = r29_no_of_acc;
	}
	public String getR30_sche_fore_ass() {
		return r30_sche_fore_ass;
	}
	public void setR30_sche_fore_ass(String r30_sche_fore_ass) {
		this.r30_sche_fore_ass = r30_sche_fore_ass;
	}
	public BigDecimal getR30_orig_amt() {
		return r30_orig_amt;
	}
	public void setR30_orig_amt(BigDecimal r30_orig_amt) {
		this.r30_orig_amt = r30_orig_amt;
	}
	public BigDecimal getR30_fore_amt() {
		return r30_fore_amt;
	}
	public void setR30_fore_amt(BigDecimal r30_fore_amt) {
		this.r30_fore_amt = r30_fore_amt;
	}
	public BigDecimal getR30_no_of_acc() {
		return r30_no_of_acc;
	}
	public void setR30_no_of_acc(BigDecimal r30_no_of_acc) {
		this.r30_no_of_acc = r30_no_of_acc;
	}
	public String getR31_sche_fore_ass() {
		return r31_sche_fore_ass;
	}
	public void setR31_sche_fore_ass(String r31_sche_fore_ass) {
		this.r31_sche_fore_ass = r31_sche_fore_ass;
	}
	public BigDecimal getR31_orig_amt() {
		return r31_orig_amt;
	}
	public void setR31_orig_amt(BigDecimal r31_orig_amt) {
		this.r31_orig_amt = r31_orig_amt;
	}
	public BigDecimal getR31_fore_amt() {
		return r31_fore_amt;
	}
	public void setR31_fore_amt(BigDecimal r31_fore_amt) {
		this.r31_fore_amt = r31_fore_amt;
	}
	public BigDecimal getR31_no_of_acc() {
		return r31_no_of_acc;
	}
	public void setR31_no_of_acc(BigDecimal r31_no_of_acc) {
		this.r31_no_of_acc = r31_no_of_acc;
	}
	public String getR32_sche_fore_ass() {
		return r32_sche_fore_ass;
	}
	public void setR32_sche_fore_ass(String r32_sche_fore_ass) {
		this.r32_sche_fore_ass = r32_sche_fore_ass;
	}
	public BigDecimal getR32_orig_amt() {
		return r32_orig_amt;
	}
	public void setR32_orig_amt(BigDecimal r32_orig_amt) {
		this.r32_orig_amt = r32_orig_amt;
	}
	public BigDecimal getR32_fore_amt() {
		return r32_fore_amt;
	}
	public void setR32_fore_amt(BigDecimal r32_fore_amt) {
		this.r32_fore_amt = r32_fore_amt;
	}
	public BigDecimal getR32_no_of_acc() {
		return r32_no_of_acc;
	}
	public void setR32_no_of_acc(BigDecimal r32_no_of_acc) {
		this.r32_no_of_acc = r32_no_of_acc;
	}
	public String getR33_sche_fore_ass() {
		return r33_sche_fore_ass;
	}
	public void setR33_sche_fore_ass(String r33_sche_fore_ass) {
		this.r33_sche_fore_ass = r33_sche_fore_ass;
	}
	public BigDecimal getR33_orig_amt() {
		return r33_orig_amt;
	}
	public void setR33_orig_amt(BigDecimal r33_orig_amt) {
		this.r33_orig_amt = r33_orig_amt;
	}
	public BigDecimal getR33_fore_amt() {
		return r33_fore_amt;
	}
	public void setR33_fore_amt(BigDecimal r33_fore_amt) {
		this.r33_fore_amt = r33_fore_amt;
	}
	public BigDecimal getR33_no_of_acc() {
		return r33_no_of_acc;
	}
	public void setR33_no_of_acc(BigDecimal r33_no_of_acc) {
		this.r33_no_of_acc = r33_no_of_acc;
	}
	public String getR34_sche_fore_ass() {
		return r34_sche_fore_ass;
	}
	public void setR34_sche_fore_ass(String r34_sche_fore_ass) {
		this.r34_sche_fore_ass = r34_sche_fore_ass;
	}
	public BigDecimal getR34_orig_amt() {
		return r34_orig_amt;
	}
	public void setR34_orig_amt(BigDecimal r34_orig_amt) {
		this.r34_orig_amt = r34_orig_amt;
	}
	public BigDecimal getR34_fore_amt() {
		return r34_fore_amt;
	}
	public void setR34_fore_amt(BigDecimal r34_fore_amt) {
		this.r34_fore_amt = r34_fore_amt;
	}
	public BigDecimal getR34_no_of_acc() {
		return r34_no_of_acc;
	}
	public void setR34_no_of_acc(BigDecimal r34_no_of_acc) {
		this.r34_no_of_acc = r34_no_of_acc;
	}
	public String getR35_sche_fore_ass() {
		return r35_sche_fore_ass;
	}
	public void setR35_sche_fore_ass(String r35_sche_fore_ass) {
		this.r35_sche_fore_ass = r35_sche_fore_ass;
	}
	public BigDecimal getR35_orig_amt() {
		return r35_orig_amt;
	}
	public void setR35_orig_amt(BigDecimal r35_orig_amt) {
		this.r35_orig_amt = r35_orig_amt;
	}
	public BigDecimal getR35_fore_amt() {
		return r35_fore_amt;
	}
	public void setR35_fore_amt(BigDecimal r35_fore_amt) {
		this.r35_fore_amt = r35_fore_amt;
	}
	public BigDecimal getR35_no_of_acc() {
		return r35_no_of_acc;
	}
	public void setR35_no_of_acc(BigDecimal r35_no_of_acc) {
		this.r35_no_of_acc = r35_no_of_acc;
	}
	public String getR36_sche_fore_ass() {
		return r36_sche_fore_ass;
	}
	public void setR36_sche_fore_ass(String r36_sche_fore_ass) {
		this.r36_sche_fore_ass = r36_sche_fore_ass;
	}
	public BigDecimal getR36_orig_amt() {
		return r36_orig_amt;
	}
	public void setR36_orig_amt(BigDecimal r36_orig_amt) {
		this.r36_orig_amt = r36_orig_amt;
	}
	public BigDecimal getR36_fore_amt() {
		return r36_fore_amt;
	}
	public void setR36_fore_amt(BigDecimal r36_fore_amt) {
		this.r36_fore_amt = r36_fore_amt;
	}
	public BigDecimal getR36_no_of_acc() {
		return r36_no_of_acc;
	}
	public void setR36_no_of_acc(BigDecimal r36_no_of_acc) {
		this.r36_no_of_acc = r36_no_of_acc;
	}
	public String getR37_sche_fore_ass() {
		return r37_sche_fore_ass;
	}
	public void setR37_sche_fore_ass(String r37_sche_fore_ass) {
		this.r37_sche_fore_ass = r37_sche_fore_ass;
	}
	public BigDecimal getR37_orig_amt() {
		return r37_orig_amt;
	}
	public void setR37_orig_amt(BigDecimal r37_orig_amt) {
		this.r37_orig_amt = r37_orig_amt;
	}
	public BigDecimal getR37_fore_amt() {
		return r37_fore_amt;
	}
	public void setR37_fore_amt(BigDecimal r37_fore_amt) {
		this.r37_fore_amt = r37_fore_amt;
	}
	public BigDecimal getR37_no_of_acc() {
		return r37_no_of_acc;
	}
	public void setR37_no_of_acc(BigDecimal r37_no_of_acc) {
		this.r37_no_of_acc = r37_no_of_acc;
	}
	public String getR38_sche_fore_ass() {
		return r38_sche_fore_ass;
	}
	public void setR38_sche_fore_ass(String r38_sche_fore_ass) {
		this.r38_sche_fore_ass = r38_sche_fore_ass;
	}
	public BigDecimal getR38_orig_amt() {
		return r38_orig_amt;
	}
	public void setR38_orig_amt(BigDecimal r38_orig_amt) {
		this.r38_orig_amt = r38_orig_amt;
	}
	public BigDecimal getR38_fore_amt() {
		return r38_fore_amt;
	}
	public void setR38_fore_amt(BigDecimal r38_fore_amt) {
		this.r38_fore_amt = r38_fore_amt;
	}
	public BigDecimal getR38_no_of_acc() {
		return r38_no_of_acc;
	}
	public void setR38_no_of_acc(BigDecimal r38_no_of_acc) {
		this.r38_no_of_acc = r38_no_of_acc;
	}
	public String getR39_sche_fore_ass() {
		return r39_sche_fore_ass;
	}
	public void setR39_sche_fore_ass(String r39_sche_fore_ass) {
		this.r39_sche_fore_ass = r39_sche_fore_ass;
	}
	public BigDecimal getR39_orig_amt() {
		return r39_orig_amt;
	}
	public void setR39_orig_amt(BigDecimal r39_orig_amt) {
		this.r39_orig_amt = r39_orig_amt;
	}
	public BigDecimal getR39_fore_amt() {
		return r39_fore_amt;
	}
	public void setR39_fore_amt(BigDecimal r39_fore_amt) {
		this.r39_fore_amt = r39_fore_amt;
	}
	public BigDecimal getR39_no_of_acc() {
		return r39_no_of_acc;
	}
	public void setR39_no_of_acc(BigDecimal r39_no_of_acc) {
		this.r39_no_of_acc = r39_no_of_acc;
	}
	public String getR40_sche_fore_ass() {
		return r40_sche_fore_ass;
	}
	public void setR40_sche_fore_ass(String r40_sche_fore_ass) {
		this.r40_sche_fore_ass = r40_sche_fore_ass;
	}
	public BigDecimal getR40_orig_amt() {
		return r40_orig_amt;
	}
	public void setR40_orig_amt(BigDecimal r40_orig_amt) {
		this.r40_orig_amt = r40_orig_amt;
	}
	public BigDecimal getR40_fore_amt() {
		return r40_fore_amt;
	}
	public void setR40_fore_amt(BigDecimal r40_fore_amt) {
		this.r40_fore_amt = r40_fore_amt;
	}
	public BigDecimal getR40_no_of_acc() {
		return r40_no_of_acc;
	}
	public void setR40_no_of_acc(BigDecimal r40_no_of_acc) {
		this.r40_no_of_acc = r40_no_of_acc;
	}
	public String getR41_sche_fore_ass() {
		return r41_sche_fore_ass;
	}
	public void setR41_sche_fore_ass(String r41_sche_fore_ass) {
		this.r41_sche_fore_ass = r41_sche_fore_ass;
	}
	public BigDecimal getR41_orig_amt() {
		return r41_orig_amt;
	}
	public void setR41_orig_amt(BigDecimal r41_orig_amt) {
		this.r41_orig_amt = r41_orig_amt;
	}
	public BigDecimal getR41_fore_amt() {
		return r41_fore_amt;
	}
	public void setR41_fore_amt(BigDecimal r41_fore_amt) {
		this.r41_fore_amt = r41_fore_amt;
	}
	public BigDecimal getR41_no_of_acc() {
		return r41_no_of_acc;
	}
	public void setR41_no_of_acc(BigDecimal r41_no_of_acc) {
		this.r41_no_of_acc = r41_no_of_acc;
	}
	public String getR42_sche_fore_ass() {
		return r42_sche_fore_ass;
	}
	public void setR42_sche_fore_ass(String r42_sche_fore_ass) {
		this.r42_sche_fore_ass = r42_sche_fore_ass;
	}
	public BigDecimal getR42_orig_amt() {
		return r42_orig_amt;
	}
	public void setR42_orig_amt(BigDecimal r42_orig_amt) {
		this.r42_orig_amt = r42_orig_amt;
	}
	public BigDecimal getR42_fore_amt() {
		return r42_fore_amt;
	}
	public void setR42_fore_amt(BigDecimal r42_fore_amt) {
		this.r42_fore_amt = r42_fore_amt;
	}
	public BigDecimal getR42_no_of_acc() {
		return r42_no_of_acc;
	}
	public void setR42_no_of_acc(BigDecimal r42_no_of_acc) {
		this.r42_no_of_acc = r42_no_of_acc;
	}
	public String getR43_sche_fore_ass() {
		return r43_sche_fore_ass;
	}
	public void setR43_sche_fore_ass(String r43_sche_fore_ass) {
		this.r43_sche_fore_ass = r43_sche_fore_ass;
	}
	public BigDecimal getR43_orig_amt() {
		return r43_orig_amt;
	}
	public void setR43_orig_amt(BigDecimal r43_orig_amt) {
		this.r43_orig_amt = r43_orig_amt;
	}
	public BigDecimal getR43_fore_amt() {
		return r43_fore_amt;
	}
	public void setR43_fore_amt(BigDecimal r43_fore_amt) {
		this.r43_fore_amt = r43_fore_amt;
	}
	public BigDecimal getR43_no_of_acc() {
		return r43_no_of_acc;
	}
	public void setR43_no_of_acc(BigDecimal r43_no_of_acc) {
		this.r43_no_of_acc = r43_no_of_acc;
	}
	public String getR44_sche_fore_ass() {
		return r44_sche_fore_ass;
	}
	public void setR44_sche_fore_ass(String r44_sche_fore_ass) {
		this.r44_sche_fore_ass = r44_sche_fore_ass;
	}
	public BigDecimal getR44_orig_amt() {
		return r44_orig_amt;
	}
	public void setR44_orig_amt(BigDecimal r44_orig_amt) {
		this.r44_orig_amt = r44_orig_amt;
	}
	public BigDecimal getR44_fore_amt() {
		return r44_fore_amt;
	}
	public void setR44_fore_amt(BigDecimal r44_fore_amt) {
		this.r44_fore_amt = r44_fore_amt;
	}
	public BigDecimal getR44_no_of_acc() {
		return r44_no_of_acc;
	}
	public void setR44_no_of_acc(BigDecimal r44_no_of_acc) {
		this.r44_no_of_acc = r44_no_of_acc;
	}
	public String getR45_sche_fore_ass() {
		return r45_sche_fore_ass;
	}
	public void setR45_sche_fore_ass(String r45_sche_fore_ass) {
		this.r45_sche_fore_ass = r45_sche_fore_ass;
	}
	public BigDecimal getR45_orig_amt() {
		return r45_orig_amt;
	}
	public void setR45_orig_amt(BigDecimal r45_orig_amt) {
		this.r45_orig_amt = r45_orig_amt;
	}
	public BigDecimal getR45_fore_amt() {
		return r45_fore_amt;
	}
	public void setR45_fore_amt(BigDecimal r45_fore_amt) {
		this.r45_fore_amt = r45_fore_amt;
	}
	public BigDecimal getR45_no_of_acc() {
		return r45_no_of_acc;
	}
	public void setR45_no_of_acc(BigDecimal r45_no_of_acc) {
		this.r45_no_of_acc = r45_no_of_acc;
	}
	public String getR46_sche_fore_ass() {
		return r46_sche_fore_ass;
	}
	public void setR46_sche_fore_ass(String r46_sche_fore_ass) {
		this.r46_sche_fore_ass = r46_sche_fore_ass;
	}
	public BigDecimal getR46_orig_amt() {
		return r46_orig_amt;
	}
	public void setR46_orig_amt(BigDecimal r46_orig_amt) {
		this.r46_orig_amt = r46_orig_amt;
	}
	public BigDecimal getR46_fore_amt() {
		return r46_fore_amt;
	}
	public void setR46_fore_amt(BigDecimal r46_fore_amt) {
		this.r46_fore_amt = r46_fore_amt;
	}
	public BigDecimal getR46_no_of_acc() {
		return r46_no_of_acc;
	}
	public void setR46_no_of_acc(BigDecimal r46_no_of_acc) {
		this.r46_no_of_acc = r46_no_of_acc;
	}
	public String getR47_sche_fore_ass() {
		return r47_sche_fore_ass;
	}
	public void setR47_sche_fore_ass(String r47_sche_fore_ass) {
		this.r47_sche_fore_ass = r47_sche_fore_ass;
	}
	public BigDecimal getR47_orig_amt() {
		return r47_orig_amt;
	}
	public void setR47_orig_amt(BigDecimal r47_orig_amt) {
		this.r47_orig_amt = r47_orig_amt;
	}
	public BigDecimal getR47_fore_amt() {
		return r47_fore_amt;
	}
	public void setR47_fore_amt(BigDecimal r47_fore_amt) {
		this.r47_fore_amt = r47_fore_amt;
	}
	public BigDecimal getR47_no_of_acc() {
		return r47_no_of_acc;
	}
	public void setR47_no_of_acc(BigDecimal r47_no_of_acc) {
		this.r47_no_of_acc = r47_no_of_acc;
	}
	public String getR48_sche_fore_ass() {
		return r48_sche_fore_ass;
	}
	public void setR48_sche_fore_ass(String r48_sche_fore_ass) {
		this.r48_sche_fore_ass = r48_sche_fore_ass;
	}
	public BigDecimal getR48_orig_amt() {
		return r48_orig_amt;
	}
	public void setR48_orig_amt(BigDecimal r48_orig_amt) {
		this.r48_orig_amt = r48_orig_amt;
	}
	public BigDecimal getR48_fore_amt() {
		return r48_fore_amt;
	}
	public void setR48_fore_amt(BigDecimal r48_fore_amt) {
		this.r48_fore_amt = r48_fore_amt;
	}
	public BigDecimal getR48_no_of_acc() {
		return r48_no_of_acc;
	}
	public void setR48_no_of_acc(BigDecimal r48_no_of_acc) {
		this.r48_no_of_acc = r48_no_of_acc;
	}
	public String getR49_sche_fore_ass() {
		return r49_sche_fore_ass;
	}
	public void setR49_sche_fore_ass(String r49_sche_fore_ass) {
		this.r49_sche_fore_ass = r49_sche_fore_ass;
	}
	public BigDecimal getR49_orig_amt() {
		return r49_orig_amt;
	}
	public void setR49_orig_amt(BigDecimal r49_orig_amt) {
		this.r49_orig_amt = r49_orig_amt;
	}
	public BigDecimal getR49_fore_amt() {
		return r49_fore_amt;
	}
	public void setR49_fore_amt(BigDecimal r49_fore_amt) {
		this.r49_fore_amt = r49_fore_amt;
	}
	public BigDecimal getR49_no_of_acc() {
		return r49_no_of_acc;
	}
	public void setR49_no_of_acc(BigDecimal r49_no_of_acc) {
		this.r49_no_of_acc = r49_no_of_acc;
	}
	public String getR50_sche_fore_ass() {
		return r50_sche_fore_ass;
	}
	public void setR50_sche_fore_ass(String r50_sche_fore_ass) {
		this.r50_sche_fore_ass = r50_sche_fore_ass;
	}
	public BigDecimal getR50_orig_amt() {
		return r50_orig_amt;
	}
	public void setR50_orig_amt(BigDecimal r50_orig_amt) {
		this.r50_orig_amt = r50_orig_amt;
	}
	public BigDecimal getR50_fore_amt() {
		return r50_fore_amt;
	}
	public void setR50_fore_amt(BigDecimal r50_fore_amt) {
		this.r50_fore_amt = r50_fore_amt;
	}
	public BigDecimal getR50_no_of_acc() {
		return r50_no_of_acc;
	}
	public void setR50_no_of_acc(BigDecimal r50_no_of_acc) {
		this.r50_no_of_acc = r50_no_of_acc;
	}
	public String getR51_sche_fore_ass() {
		return r51_sche_fore_ass;
	}
	public void setR51_sche_fore_ass(String r51_sche_fore_ass) {
		this.r51_sche_fore_ass = r51_sche_fore_ass;
	}
	public BigDecimal getR51_orig_amt() {
		return r51_orig_amt;
	}
	public void setR51_orig_amt(BigDecimal r51_orig_amt) {
		this.r51_orig_amt = r51_orig_amt;
	}
	public BigDecimal getR51_fore_amt() {
		return r51_fore_amt;
	}
	public void setR51_fore_amt(BigDecimal r51_fore_amt) {
		this.r51_fore_amt = r51_fore_amt;
	}
	public BigDecimal getR51_no_of_acc() {
		return r51_no_of_acc;
	}
	public void setR51_no_of_acc(BigDecimal r51_no_of_acc) {
		this.r51_no_of_acc = r51_no_of_acc;
	}
	public String getR52_sche_fore_ass() {
		return r52_sche_fore_ass;
	}
	public void setR52_sche_fore_ass(String r52_sche_fore_ass) {
		this.r52_sche_fore_ass = r52_sche_fore_ass;
	}
	public BigDecimal getR52_orig_amt() {
		return r52_orig_amt;
	}
	public void setR52_orig_amt(BigDecimal r52_orig_amt) {
		this.r52_orig_amt = r52_orig_amt;
	}
	public BigDecimal getR52_fore_amt() {
		return r52_fore_amt;
	}
	public void setR52_fore_amt(BigDecimal r52_fore_amt) {
		this.r52_fore_amt = r52_fore_amt;
	}
	public BigDecimal getR52_no_of_acc() {
		return r52_no_of_acc;
	}
	public void setR52_no_of_acc(BigDecimal r52_no_of_acc) {
		this.r52_no_of_acc = r52_no_of_acc;
	}
	public String getR53_sche_fore_ass() {
		return r53_sche_fore_ass;
	}
	public void setR53_sche_fore_ass(String r53_sche_fore_ass) {
		this.r53_sche_fore_ass = r53_sche_fore_ass;
	}
	public BigDecimal getR53_orig_amt() {
		return r53_orig_amt;
	}
	public void setR53_orig_amt(BigDecimal r53_orig_amt) {
		this.r53_orig_amt = r53_orig_amt;
	}
	public BigDecimal getR53_fore_amt() {
		return r53_fore_amt;
	}
	public void setR53_fore_amt(BigDecimal r53_fore_amt) {
		this.r53_fore_amt = r53_fore_amt;
	}
	public BigDecimal getR53_no_of_acc() {
		return r53_no_of_acc;
	}
	public void setR53_no_of_acc(BigDecimal r53_no_of_acc) {
		this.r53_no_of_acc = r53_no_of_acc;
	}
	public String getR54_sche_fore_ass() {
		return r54_sche_fore_ass;
	}
	public void setR54_sche_fore_ass(String r54_sche_fore_ass) {
		this.r54_sche_fore_ass = r54_sche_fore_ass;
	}
	public BigDecimal getR54_orig_amt() {
		return r54_orig_amt;
	}
	public void setR54_orig_amt(BigDecimal r54_orig_amt) {
		this.r54_orig_amt = r54_orig_amt;
	}
	public BigDecimal getR54_fore_amt() {
		return r54_fore_amt;
	}
	public void setR54_fore_amt(BigDecimal r54_fore_amt) {
		this.r54_fore_amt = r54_fore_amt;
	}
	public BigDecimal getR54_no_of_acc() {
		return r54_no_of_acc;
	}
	public void setR54_no_of_acc(BigDecimal r54_no_of_acc) {
		this.r54_no_of_acc = r54_no_of_acc;
	}
	public String getR55_sche_fore_ass() {
		return r55_sche_fore_ass;
	}
	public void setR55_sche_fore_ass(String r55_sche_fore_ass) {
		this.r55_sche_fore_ass = r55_sche_fore_ass;
	}
	public BigDecimal getR55_orig_amt() {
		return r55_orig_amt;
	}
	public void setR55_orig_amt(BigDecimal r55_orig_amt) {
		this.r55_orig_amt = r55_orig_amt;
	}
	public BigDecimal getR55_fore_amt() {
		return r55_fore_amt;
	}
	public void setR55_fore_amt(BigDecimal r55_fore_amt) {
		this.r55_fore_amt = r55_fore_amt;
	}
	public BigDecimal getR55_no_of_acc() {
		return r55_no_of_acc;
	}
	public void setR55_no_of_acc(BigDecimal r55_no_of_acc) {
		this.r55_no_of_acc = r55_no_of_acc;
	}
	public String getR56_sche_fore_ass() {
		return r56_sche_fore_ass;
	}
	public void setR56_sche_fore_ass(String r56_sche_fore_ass) {
		this.r56_sche_fore_ass = r56_sche_fore_ass;
	}
	public BigDecimal getR56_orig_amt() {
		return r56_orig_amt;
	}
	public void setR56_orig_amt(BigDecimal r56_orig_amt) {
		this.r56_orig_amt = r56_orig_amt;
	}
	public BigDecimal getR56_fore_amt() {
		return r56_fore_amt;
	}
	public void setR56_fore_amt(BigDecimal r56_fore_amt) {
		this.r56_fore_amt = r56_fore_amt;
	}
	public BigDecimal getR56_no_of_acc() {
		return r56_no_of_acc;
	}
	public void setR56_no_of_acc(BigDecimal r56_no_of_acc) {
		this.r56_no_of_acc = r56_no_of_acc;
	}
	public String getR57_sche_fore_ass() {
		return r57_sche_fore_ass;
	}
	public void setR57_sche_fore_ass(String r57_sche_fore_ass) {
		this.r57_sche_fore_ass = r57_sche_fore_ass;
	}
	public BigDecimal getR57_orig_amt() {
		return r57_orig_amt;
	}
	public void setR57_orig_amt(BigDecimal r57_orig_amt) {
		this.r57_orig_amt = r57_orig_amt;
	}
	public BigDecimal getR57_fore_amt() {
		return r57_fore_amt;
	}
	public void setR57_fore_amt(BigDecimal r57_fore_amt) {
		this.r57_fore_amt = r57_fore_amt;
	}
	public BigDecimal getR57_no_of_acc() {
		return r57_no_of_acc;
	}
	public void setR57_no_of_acc(BigDecimal r57_no_of_acc) {
		this.r57_no_of_acc = r57_no_of_acc;
	}
	public String getR58_sche_fore_ass() {
		return r58_sche_fore_ass;
	}
	public void setR58_sche_fore_ass(String r58_sche_fore_ass) {
		this.r58_sche_fore_ass = r58_sche_fore_ass;
	}
	public BigDecimal getR58_orig_amt() {
		return r58_orig_amt;
	}
	public void setR58_orig_amt(BigDecimal r58_orig_amt) {
		this.r58_orig_amt = r58_orig_amt;
	}
	public BigDecimal getR58_fore_amt() {
		return r58_fore_amt;
	}
	public void setR58_fore_amt(BigDecimal r58_fore_amt) {
		this.r58_fore_amt = r58_fore_amt;
	}
	public BigDecimal getR58_no_of_acc() {
		return r58_no_of_acc;
	}
	public void setR58_no_of_acc(BigDecimal r58_no_of_acc) {
		this.r58_no_of_acc = r58_no_of_acc;
	}
	public String getR59_sche_fore_ass() {
		return r59_sche_fore_ass;
	}
	public void setR59_sche_fore_ass(String r59_sche_fore_ass) {
		this.r59_sche_fore_ass = r59_sche_fore_ass;
	}
	public BigDecimal getR59_orig_amt() {
		return r59_orig_amt;
	}
	public void setR59_orig_amt(BigDecimal r59_orig_amt) {
		this.r59_orig_amt = r59_orig_amt;
	}
	public BigDecimal getR59_fore_amt() {
		return r59_fore_amt;
	}
	public void setR59_fore_amt(BigDecimal r59_fore_amt) {
		this.r59_fore_amt = r59_fore_amt;
	}
	public BigDecimal getR59_no_of_acc() {
		return r59_no_of_acc;
	}
	public void setR59_no_of_acc(BigDecimal r59_no_of_acc) {
		this.r59_no_of_acc = r59_no_of_acc;
	}
	public String getR60_sche_fore_ass() {
		return r60_sche_fore_ass;
	}
	public void setR60_sche_fore_ass(String r60_sche_fore_ass) {
		this.r60_sche_fore_ass = r60_sche_fore_ass;
	}
	public BigDecimal getR60_orig_amt() {
		return r60_orig_amt;
	}
	public void setR60_orig_amt(BigDecimal r60_orig_amt) {
		this.r60_orig_amt = r60_orig_amt;
	}
	public BigDecimal getR60_fore_amt() {
		return r60_fore_amt;
	}
	public void setR60_fore_amt(BigDecimal r60_fore_amt) {
		this.r60_fore_amt = r60_fore_amt;
	}
	public BigDecimal getR60_no_of_acc() {
		return r60_no_of_acc;
	}
	public void setR60_no_of_acc(BigDecimal r60_no_of_acc) {
		this.r60_no_of_acc = r60_no_of_acc;
	}
	public String getR61_sche_fore_ass() {
		return r61_sche_fore_ass;
	}
	public void setR61_sche_fore_ass(String r61_sche_fore_ass) {
		this.r61_sche_fore_ass = r61_sche_fore_ass;
	}
	public BigDecimal getR61_orig_amt() {
		return r61_orig_amt;
	}
	public void setR61_orig_amt(BigDecimal r61_orig_amt) {
		this.r61_orig_amt = r61_orig_amt;
	}
	public BigDecimal getR61_fore_amt() {
		return r61_fore_amt;
	}
	public void setR61_fore_amt(BigDecimal r61_fore_amt) {
		this.r61_fore_amt = r61_fore_amt;
	}
	public BigDecimal getR61_no_of_acc() {
		return r61_no_of_acc;
	}
	public void setR61_no_of_acc(BigDecimal r61_no_of_acc) {
		this.r61_no_of_acc = r61_no_of_acc;
	}
	public String getR62_sche_fore_ass() {
		return r62_sche_fore_ass;
	}
	public void setR62_sche_fore_ass(String r62_sche_fore_ass) {
		this.r62_sche_fore_ass = r62_sche_fore_ass;
	}
	public BigDecimal getR62_orig_amt() {
		return r62_orig_amt;
	}
	public void setR62_orig_amt(BigDecimal r62_orig_amt) {
		this.r62_orig_amt = r62_orig_amt;
	}
	public BigDecimal getR62_fore_amt() {
		return r62_fore_amt;
	}
	public void setR62_fore_amt(BigDecimal r62_fore_amt) {
		this.r62_fore_amt = r62_fore_amt;
	}
	public BigDecimal getR62_no_of_acc() {
		return r62_no_of_acc;
	}
	public void setR62_no_of_acc(BigDecimal r62_no_of_acc) {
		this.r62_no_of_acc = r62_no_of_acc;
	}
	public String getR63_sche_fore_ass() {
		return r63_sche_fore_ass;
	}
	public void setR63_sche_fore_ass(String r63_sche_fore_ass) {
		this.r63_sche_fore_ass = r63_sche_fore_ass;
	}
	public BigDecimal getR63_orig_amt() {
		return r63_orig_amt;
	}
	public void setR63_orig_amt(BigDecimal r63_orig_amt) {
		this.r63_orig_amt = r63_orig_amt;
	}
	public BigDecimal getR63_fore_amt() {
		return r63_fore_amt;
	}
	public void setR63_fore_amt(BigDecimal r63_fore_amt) {
		this.r63_fore_amt = r63_fore_amt;
	}
	public BigDecimal getR63_no_of_acc() {
		return r63_no_of_acc;
	}
	public void setR63_no_of_acc(BigDecimal r63_no_of_acc) {
		this.r63_no_of_acc = r63_no_of_acc;
	}
	public String getR64_sche_fore_ass() {
		return r64_sche_fore_ass;
	}
	public void setR64_sche_fore_ass(String r64_sche_fore_ass) {
		this.r64_sche_fore_ass = r64_sche_fore_ass;
	}
	public BigDecimal getR64_orig_amt() {
		return r64_orig_amt;
	}
	public void setR64_orig_amt(BigDecimal r64_orig_amt) {
		this.r64_orig_amt = r64_orig_amt;
	}
	public BigDecimal getR64_fore_amt() {
		return r64_fore_amt;
	}
	public void setR64_fore_amt(BigDecimal r64_fore_amt) {
		this.r64_fore_amt = r64_fore_amt;
	}
	public BigDecimal getR64_no_of_acc() {
		return r64_no_of_acc;
	}
	public void setR64_no_of_acc(BigDecimal r64_no_of_acc) {
		this.r64_no_of_acc = r64_no_of_acc;
	}
	public String getR65_sche_fore_ass() {
		return r65_sche_fore_ass;
	}
	public void setR65_sche_fore_ass(String r65_sche_fore_ass) {
		this.r65_sche_fore_ass = r65_sche_fore_ass;
	}
	public BigDecimal getR65_orig_amt() {
		return r65_orig_amt;
	}
	public void setR65_orig_amt(BigDecimal r65_orig_amt) {
		this.r65_orig_amt = r65_orig_amt;
	}
	public BigDecimal getR65_fore_amt() {
		return r65_fore_amt;
	}
	public void setR65_fore_amt(BigDecimal r65_fore_amt) {
		this.r65_fore_amt = r65_fore_amt;
	}
	public BigDecimal getR65_no_of_acc() {
		return r65_no_of_acc;
	}
	public void setR65_no_of_acc(BigDecimal r65_no_of_acc) {
		this.r65_no_of_acc = r65_no_of_acc;
	}
	public String getR66_sche_fore_ass() {
		return r66_sche_fore_ass;
	}
	public void setR66_sche_fore_ass(String r66_sche_fore_ass) {
		this.r66_sche_fore_ass = r66_sche_fore_ass;
	}
	public BigDecimal getR66_orig_amt() {
		return r66_orig_amt;
	}
	public void setR66_orig_amt(BigDecimal r66_orig_amt) {
		this.r66_orig_amt = r66_orig_amt;
	}
	public BigDecimal getR66_fore_amt() {
		return r66_fore_amt;
	}
	public void setR66_fore_amt(BigDecimal r66_fore_amt) {
		this.r66_fore_amt = r66_fore_amt;
	}
	public BigDecimal getR66_no_of_acc() {
		return r66_no_of_acc;
	}
	public void setR66_no_of_acc(BigDecimal r66_no_of_acc) {
		this.r66_no_of_acc = r66_no_of_acc;
	}
	public String getR67_sche_fore_ass() {
		return r67_sche_fore_ass;
	}
	public void setR67_sche_fore_ass(String r67_sche_fore_ass) {
		this.r67_sche_fore_ass = r67_sche_fore_ass;
	}
	public BigDecimal getR67_orig_amt() {
		return r67_orig_amt;
	}
	public void setR67_orig_amt(BigDecimal r67_orig_amt) {
		this.r67_orig_amt = r67_orig_amt;
	}
	public BigDecimal getR67_fore_amt() {
		return r67_fore_amt;
	}
	public void setR67_fore_amt(BigDecimal r67_fore_amt) {
		this.r67_fore_amt = r67_fore_amt;
	}
	public BigDecimal getR67_no_of_acc() {
		return r67_no_of_acc;
	}
	public void setR67_no_of_acc(BigDecimal r67_no_of_acc) {
		this.r67_no_of_acc = r67_no_of_acc;
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
	public Date getReportResubDate() {
		return reportResubDate;
	}
	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
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


//=====================================================
// MODEL AND VIEW METHOD summary Q_RLFA2
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
public ModelAndView getQ_RLFA2View(
        String reportId,
        String fromdate,
        String todate,
        String currency,
        String dtltype,
        Pageable pageable,
        String type,
        BigDecimal version,HttpServletRequest req1,Model md) {

    ModelAndView mv = new ModelAndView();
	
	 String userid = (String) req1.getSession().getAttribute("USERID");
	    System.out.println("User Id Maker and Checker: " + userid);
	    String role = userProfileRep.getUserRole(userid);
	    md.addAttribute("role", role);
	    System.out.println("Role: " + role);
		
		int pageSize = pageable.getPageSize();
	    int currentPage = pageable.getPageNumber();
	    int startItem = currentPage * pageSize;

    System.out.println("Q_RLFA2 View Called");
    System.out.println("Type = " + type);
    System.out.println("Version = " + version);
    System.out.println("DtlType = " + dtltype);

    try {

        Date dt = dateformat.parse(todate);

        // =====================================================
        // DETAIL
        // =====================================================

        if ("detail".equalsIgnoreCase(dtltype)) {

            // ARCHIVAL DETAIL
            if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

                List<Q_RLFA2_Archival_Detail_Entity> T1Master =
                        getDetaildatabydateListarchival(dt, version);

                System.out.println("Archival Detail Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
                mv.addObject("displaymode", "detail");
            }

            // RESUB DETAIL
            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

                List<Q_RLFA2_RESUB_Detail_Entity> T1Master =
                        getResubDetaildatabydateList(dt, version);

                System.out.println("Resub Detail Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
                mv.addObject("displaymode", "detail");
            }

            // NORMAL DETAIL
            else {

                List<Q_RLFA2_Detail_Entity> T1Master =
                        getDetaildatabydateList(dt);

                System.out.println("Normal Detail Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
                mv.addObject("displaymode", "detail");
            }
        }

        // =====================================================
        // SUMMARY
        // =====================================================

        else {

            // ARCHIVAL SUMMARY
            if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

                List<Q_RLFA2_Archival_Summary_Entity> T1Master =
                        getDataByDateListArchival(dt, version);

                System.out.println("Archival Summary Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
            }

            // RESUB SUMMARY
            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

                List<Q_RLFA2_RESUB_Summary_Entity> T1Master =
                        getResubSummarydatabydateListarchival(dt, version);

                System.out.println("Resub Summary Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
            }

            // NORMAL SUMMARY
            else {

                List<Q_RLFA2_Summary_Entity> T1Master =
                        getSummaryDataByDate(dt);

                System.out.println("Normal Summary Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
            }

            mv.addObject("displaymode", "summary");
        }

        mv.addObject("report_date", dateformat.format(dt));

    } catch (Exception e) {
        e.printStackTrace();
    }

    mv.setViewName("BRRS/Q_RLFA2");

    System.out.println("View Loaded : " + mv.getViewName());

    return mv;
}
	
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
//=====================================================
// MODEL AND VIEW METHOD ARCHIVAL VIEW
//=====================================================

// Archival View
public List<Object[]> getQ_RLFA2Archival() {

    List<Object[]> archivalList = new ArrayList<>();

    try {

        List<Q_RLFA2_Archival_Summary_Entity> repoData =
                getarchivaldatabydateListWithVersion();

        if (repoData != null && !repoData.isEmpty()) {

            for (Q_RLFA2_Archival_Summary_Entity entity : repoData) {

                Object[] row = new Object[] {
                        entity.getReport_date(),
                        entity.getReport_version(),
                        entity.getReportResubDate()
                };

                archivalList.add(row);
            }

            System.out.println("Fetched " + archivalList.size() + " archival records");

            Q_RLFA2_Archival_Summary_Entity first = repoData.get(0);

            System.out.println("Latest archival version: "
                    + first.getReport_version());

        } else {

            System.out.println("No archival data found.");
        }

    } catch (Exception e) {

        System.err.println("Error fetching Q_RLFA2 Archival data: "
                + e.getMessage());

        e.printStackTrace();
    }

    return archivalList;
}
//=====================================================
// UPDATE REPORT
//=====================================================

@Transactional
public void updateReport(Q_RLFA2_Summary_Entity updatedEntity) {

    System.out.println("Came to Q_RLFA2 Update");
    System.out.println("Report Date: " + updatedEntity.getReport_date());

    // ==========================================
    // FETCH EXISTING RECORD FOR AUDIT
    // ==========================================

    Q_RLFA2_Summary_Entity existingSummary =
            findByReportDate(updatedEntity.getReport_date());

    if (existingSummary == null) {
        throw new RuntimeException(
                "Record not found for REPORT_DATE : "
                        + updatedEntity.getReport_date());
    }

    // ==========================================
    // OLD COPY FOR AUDIT
    // ==========================================

    Q_RLFA2_Summary_Entity oldcopy = new Q_RLFA2_Summary_Entity();
    BeanUtils.copyProperties(existingSummary, oldcopy);

    // ==========================================
    // FIELD NAMES
    // ==========================================

    String[] fields = {
            "sche_fore_ass",
            "orig_amt",
            "fore_amt",
            "no_of_acc"
    };

    try {

        // ==========================================
        // LOOP FROM R10 TO R64
        // ==========================================

        for (int i = 10; i <= 64; i++) {

            for (String field : fields) {

                String getterName = "getR" + i + "_" + field;
                String setterName = "setR" + i + "_" + field;
                String columnName = "R" + i + "_" + field;

                try {

                    Method getter =
                            Q_RLFA2_Summary_Entity.class.getMethod(getterName);

                    Object value = getter.invoke(updatedEntity);

                    // Skip if no value received
                    if (value == null) {
                        continue;
                    }

                    // ==========================================
                    // UPDATE EXISTING OBJECT (FOR AUDIT)
                    // ==========================================

                    Method setter =
                            Q_RLFA2_Summary_Entity.class.getMethod(
                                    setterName,
                                    getter.getReturnType());

                    setter.invoke(existingSummary, value);

                    // ==========================================
                    // UPDATE SUMMARY TABLE
                    // ==========================================

                    String summarySql =
                            "UPDATE BRRS_Q_RLFA2_SUMMARYTABLE " +
                            "SET " + columnName + " = ? " +
                            "WHERE REPORT_DATE = ?";

                    jdbcTemplate.update(
                            summarySql,
                            value,
                            updatedEntity.getReport_date()
                    );

                    // ==========================================
                    // UPDATE DETAIL TABLE
                    // ==========================================

                    String detailSql =
                            "UPDATE BRRS_Q_RLFA2_DETAILTABLE " +
                            "SET " + columnName + " = ? " +
                            "WHERE REPORT_DATE = ?";

                    jdbcTemplate.update(
                            detailSql,
                            value,
                            updatedEntity.getReport_date()
                    );

                } catch (NoSuchMethodException e) {
                    // Ignore if a field does not exist
                    continue;
                }
            }
        }

        // ==========================================
        // AUDIT ONLY IF CHANGES FOUND
        // ==========================================

        String changes = auditService.getChanges(oldcopy, existingSummary);

        if (!changes.isEmpty()) {

            auditService.compareEntitiesmanual(
                    oldcopy,
                    existingSummary,
                    updatedEntity.getReport_date().toString(),
                    "M Q_RLFA2 Summary Screen",
                    "BRRS_Q_RLFA2_SUMMARYTABLE"
            );
        }

        System.out.println("Q_RLFA2 Summary & Detail Update Completed");

    } catch (Exception e) {

        e.printStackTrace();

        throw new RuntimeException(
                "Error while updating Q_RLFA2 Summary fields", e);
    }
}
//=====================================================
// VIEW AND EDIT
//=====================================================

	
//=====================================================
// UPDATEDETAIL
//=====================================================


//=====================================================
// RESUB VIEW 
//=====================================================

public List<Object[]> getQ_RLFA2Resub() {

    List<Object[]> resubList = new ArrayList<>();

    try {

        List<Q_RLFA2_Archival_Summary_Entity> repoData =
                getarchivaldatabydateListWithVersion();

        if (repoData != null && !repoData.isEmpty()) {

            for (Q_RLFA2_Archival_Summary_Entity entity : repoData) {

                Object[] row = new Object[] {
                        entity.getReport_date(),
                        entity.getReport_version(),
                        entity.getReportResubDate()
                };

                resubList.add(row);
            }

            System.out.println("Fetched " + resubList.size() + " resub records");

            Q_RLFA2_Archival_Summary_Entity first = repoData.get(0);

            System.out.println("Latest resub version : "
                    + first.getReport_version());

        } else {

            System.out.println("No resub data found.");
        }

    } catch (Exception e) {

        System.err.println("Error fetching Q_RLFA2 Resub data : "
                + e.getMessage());

        e.printStackTrace();
    }

    return resubList;
}

//=====================================================
// UPDATE RESUB 
//=====================================================


@Transactional
public void updateResubReport(
        Q_RLFA2_RESUB_Summary_Entity updatedEntity) {

    System.out.println("Came to Q_RLFA2 Resub Update");

    Date reportDate = updatedEntity.getReport_date();

    // ====================================================
    // GET MAX VERSION
    // ====================================================

    BigDecimal maxVersion = findMaxVersion(reportDate);

    if (maxVersion == null) {
        throw new RuntimeException(
                "No record found for REPORT_DATE : "
                        + reportDate);
    }

    BigDecimal newVersion =
            maxVersion.add(BigDecimal.ONE);

    Date now = new Date();

    try {

        // ====================================================
        // RESUB SUMMARY
        // ====================================================

        Q_RLFA2_RESUB_Summary_Entity resubSummary =
                new Q_RLFA2_RESUB_Summary_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                resubSummary);

        resubSummary.setReport_date(reportDate);
        resubSummary.setReport_version(newVersion);
        resubSummary.setReportResubDate(now);

        // ====================================================
        // RESUB DETAIL
        // ====================================================

        Q_RLFA2_RESUB_Detail_Entity resubDetail =
                new Q_RLFA2_RESUB_Detail_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                resubDetail);

        resubDetail.setReport_date(reportDate);
        resubDetail.setReport_version(newVersion);
        resubDetail.setReportResubDate(now);

        // ====================================================
        // ARCHIVAL SUMMARY
        // ====================================================

        Q_RLFA2_Archival_Summary_Entity archivalSummary =
                new Q_RLFA2_Archival_Summary_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                archivalSummary);

        archivalSummary.setReport_date(reportDate);
        archivalSummary.setReport_version(newVersion);
        archivalSummary.setReportResubDate(now);

        // ====================================================
        // ARCHIVAL DETAIL
        // ====================================================

        Q_RLFA2_Archival_Detail_Entity archivalDetail =
                new Q_RLFA2_Archival_Detail_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                archivalDetail);

        archivalDetail.setReport_date(reportDate);
        archivalDetail.setReport_version(newVersion);
        archivalDetail.setReportResubDate(now);

        // ====================================================
        // INSERT INTO RESUB SUMMARY TABLE
        // ====================================================

        insertResubSummary(resubSummary);

        // ====================================================
        // INSERT INTO RESUB DETAIL TABLE
        // ====================================================

        insertResubDetail(resubDetail);

        // ====================================================
        // INSERT INTO ARCHIVAL SUMMARY TABLE
        // ====================================================

        insertArchivalSummary(archivalSummary);

        // ====================================================
        // INSERT INTO ARCHIVAL DETAIL TABLE
        // ====================================================

        insertArchivalDetail(archivalDetail);

        System.out.println(
                "Q_RLFA2 Resub Version Created Successfully : "
                        + newVersion);

    } catch (Exception e) {

        e.printStackTrace();

        throw new RuntimeException(
                "Error while creating Q_RLFA2 Resub Version",
                e);
    }
}

private Object getValue(Object obj, String methodName) {
    try {
        return obj.getClass().getMethod(methodName).invoke(obj);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

private void insertResubSummary(Q_RLFA2_RESUB_Summary_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_Q_RLFA2_RESUB_SUMMARYTABLE (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

        // ==========================================
        // R10 TO R64
        // ==========================================

        for (int i = 10; i <= 64; i++) {

            columns
                    .append("r").append(i).append("_sche_fore_ass,")
                    .append("r").append(i).append("_orig_amt,")
                    .append("r").append(i).append("_fore_amt,")
                    .append("r").append(i).append("_no_of_acc,");

            // 4 values for each row
            for (int j = 1; j <= 4; j++) {
                values.append("?,");
            }

            params.add(getValue(entity, "getR" + i + "_sche_fore_ass"));
            params.add(getValue(entity, "getR" + i + "_orig_amt"));
            params.add(getValue(entity, "getR" + i + "_fore_amt"));
            params.add(getValue(entity, "getR" + i + "_no_of_acc"));
        }

        // Remove last comma
        columns.deleteCharAt(columns.length() - 1);
        values.deleteCharAt(values.length() - 1);

        columns.append(")");
        values.append(")");

        String sql = columns.toString() + values.toString();

        jdbcTemplate.update(sql, params.toArray());

    } catch (Exception e) {

        e.printStackTrace();

        throw new RuntimeException(
                "Error inserting Q_RLFA2 RESUB SUMMARY",
                e);
    }
}

private void insertResubDetail(Q_RLFA2_RESUB_Detail_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_Q_RLFA2_RESUB_DETAILTABLE (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

        // ==========================================
        // R10 TO R64
        // ==========================================

        for (int i = 10; i <= 64; i++) {

            columns
                    .append("r").append(i).append("_sche_fore_ass,")
                    .append("r").append(i).append("_orig_amt,")
                    .append("r").append(i).append("_fore_amt,")
                    .append("r").append(i).append("_no_of_acc,");

            // 4 values for each row
            for (int j = 1; j <= 4; j++) {
                values.append("?,");
            }

            params.add(getValue(entity, "getR" + i + "_sche_fore_ass"));
            params.add(getValue(entity, "getR" + i + "_orig_amt"));
            params.add(getValue(entity, "getR" + i + "_fore_amt"));
            params.add(getValue(entity, "getR" + i + "_no_of_acc"));
        }

        // Remove trailing comma
        columns.deleteCharAt(columns.length() - 1);
        values.deleteCharAt(values.length() - 1);

        columns.append(")");
        values.append(")");

        String sql = columns.toString() + values.toString();

        jdbcTemplate.update(sql, params.toArray());

    } catch (Exception e) {

        e.printStackTrace();

        throw new RuntimeException(
                "Error inserting Q_RLFA2 RESUB DETAIL",
                e);
    }
}

private void insertArchivalSummary(Q_RLFA2_Archival_Summary_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_Q_RLFA2_ARCHIVALTABLE_SUMMARY (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

        // ==========================================
        // R10 TO R64
        // ==========================================

        for (int i = 10; i <= 64; i++) {

            columns
                    .append("r").append(i).append("_sche_fore_ass,")
                    .append("r").append(i).append("_orig_amt,")
                    .append("r").append(i).append("_fore_amt,")
                    .append("r").append(i).append("_no_of_acc,");

            // 4 placeholders
            for (int j = 1; j <= 4; j++) {
                values.append("?,");
            }

            params.add(getValue(entity, "getR" + i + "_sche_fore_ass"));
            params.add(getValue(entity, "getR" + i + "_orig_amt"));
            params.add(getValue(entity, "getR" + i + "_fore_amt"));
            params.add(getValue(entity, "getR" + i + "_no_of_acc"));
        }

        // Remove trailing comma
        columns.deleteCharAt(columns.length() - 1);
        values.deleteCharAt(values.length() - 1);

        columns.append(")");
        values.append(")");

        String sql = columns.toString() + values.toString();

        jdbcTemplate.update(sql, params.toArray());

    } catch (Exception e) {

        e.printStackTrace();

        throw new RuntimeException(
                "Error inserting Q_RLFA2 ARCHIVAL SUMMARY",
                e);
    }
}

private void insertArchivalDetail(Q_RLFA2_Archival_Detail_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_Q_RLFA2_ARCHIVALTABLE_DETAIL (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

        // ==========================================
        // R10 TO R64
        // ==========================================

        for (int i = 10; i <= 64; i++) {

            columns
                    .append("r").append(i).append("_sche_fore_ass,")
                    .append("r").append(i).append("_orig_amt,")
                    .append("r").append(i).append("_fore_amt,")
                    .append("r").append(i).append("_no_of_acc,");

            // 4 placeholders
            for (int j = 1; j <= 4; j++) {
                values.append("?,");
            }

            params.add(getValue(entity, "getR" + i + "_sche_fore_ass"));
            params.add(getValue(entity, "getR" + i + "_orig_amt"));
            params.add(getValue(entity, "getR" + i + "_fore_amt"));
            params.add(getValue(entity, "getR" + i + "_no_of_acc"));
        }

        // Remove trailing comma
        columns.deleteCharAt(columns.length() - 1);
        values.deleteCharAt(values.length() - 1);

        columns.append(")");
        values.append(")");

        String sql = columns.toString() + values.toString();

        jdbcTemplate.update(sql, params.toArray());

    } catch (Exception e) {

        e.printStackTrace();

        throw new RuntimeException(
                "Error inserting Q_RLFA2 ARCHIVAL DETAIL",
                e);
    }
}
	
//=====================================================
// Summary EXCEL  FORMAT
//=====================================================

public byte[] getQ_RLFA2Excel(String filename, String reportId, String fromdate, String todate, String currency,
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
						return getExcelQ_RLFA2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_Q_RLFA2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else {

					if ("email".equalsIgnoreCase(format) && version == null) {
						logger.info("Got format as Email");
						logger.info("Service: Generating Email report for version {}", version);
						return BRRS_Q_RLFA2EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} else {

						// Fetch data

						List<Q_RLFA2_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

						if (dataList.isEmpty()) {
							logger.warn("Service: No data found for BRRS_Q_RLFA2 report. Returning empty result.");
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
									Q_RLFA2_Summary_Entity record = dataList.get(i);
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
															
															
															
															//ROW 11
															row = sheet.getRow(10);
															

			// row11
							// Column B2 - Original Amount 
							Cell cellB = row.createCell(1);
							if (record.getR11_orig_amt() != null) {
								cellB.setCellValue(record.getR11_orig_amt().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row11
						    // Column C3 - Foreclosure Amount
							Cell cellC = row.createCell(2);
							if (record.getR11_fore_amt() != null) {
								cellC.setCellValue(record.getR11_fore_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
				            // Column D4 - No. of Accounts
							Cell cellE = row.createCell(3);
							if (record.getR11_no_of_acc() != null) {
								cellE.setCellValue(record.getR11_no_of_acc().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(11);
							
							// row12
							// Column B2 - Original Amount 
							 cellB = row.createCell(1);
							if (record.getR12_orig_amt() != null) {
							    cellB.setCellValue(record.getR12_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row12
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR12_fore_amt() != null) {
							    cellC.setCellValue(record.getR12_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row12
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR12_no_of_acc() != null) {
							    cellE.setCellValue(record.getR12_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							

							// row13
							row = sheet.getRow(12);

							// row13
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR13_orig_amt() != null) {
							    cellB.setCellValue(record.getR13_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row13
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR13_fore_amt() != null) {
							    cellC.setCellValue(record.getR13_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row13
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR13_no_of_acc() != null) {
							    cellE.setCellValue(record.getR13_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// row15
							row = sheet.getRow(14);

							// row15
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR15_orig_amt() != null) {
							    cellB.setCellValue(record.getR15_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row15
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR15_fore_amt() != null) {
							    cellC.setCellValue(record.getR15_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row15
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR15_no_of_acc() != null) {
							    cellE.setCellValue(record.getR15_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row16
							row = sheet.getRow(15);

							// row16
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR16_orig_amt() != null) {
							    cellB.setCellValue(record.getR16_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row16
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR16_fore_amt() != null) {
							    cellC.setCellValue(record.getR16_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row16
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR16_no_of_acc() != null) {
							    cellE.setCellValue(record.getR16_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(16);

							// row17
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR17_orig_amt() != null) {
							    cellB.setCellValue(record.getR17_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row17
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR17_fore_amt() != null) {
							    cellC.setCellValue(record.getR17_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row17
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR17_no_of_acc() != null) {
							    cellE.setCellValue(record.getR17_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row18
							row = sheet.getRow(17);

							// row18
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR18_orig_amt() != null) {
							    cellB.setCellValue(record.getR18_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row18
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR18_fore_amt() != null) {
							    cellC.setCellValue(record.getR18_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row18
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR18_no_of_acc() != null) {
							    cellE.setCellValue(record.getR18_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(18);

							// row19
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR19_orig_amt() != null) {
							    cellB.setCellValue(record.getR19_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row19
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR19_fore_amt() != null) {
							    cellC.setCellValue(record.getR19_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row19
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR19_no_of_acc() != null) {
							    cellE.setCellValue(record.getR19_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row20
							row = sheet.getRow(19);

							// row20
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR20_orig_amt() != null) {
							    cellB.setCellValue(record.getR20_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row20
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR20_fore_amt() != null) {
							    cellC.setCellValue(record.getR20_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row20
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR20_no_of_acc() != null) {
							    cellE.setCellValue(record.getR20_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row21
							row = sheet.getRow(20);

							// row21
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR21_orig_amt() != null) {
							    cellB.setCellValue(record.getR21_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row21
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR21_fore_amt() != null) {
							    cellC.setCellValue(record.getR21_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row21
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR21_no_of_acc() != null) {
							    cellE.setCellValue(record.getR21_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row22
							row = sheet.getRow(21);

							// row22
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR22_orig_amt() != null) {
							    cellB.setCellValue(record.getR22_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row22
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR22_fore_amt() != null) {
							    cellC.setCellValue(record.getR22_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row22
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR22_no_of_acc() != null) {
							    cellE.setCellValue(record.getR22_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row23
							row = sheet.getRow(22);

							// row23
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR23_orig_amt() != null) {
							    cellB.setCellValue(record.getR23_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row23
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR23_fore_amt() != null) {
							    cellC.setCellValue(record.getR23_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row23
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR23_no_of_acc() != null) {
							    cellE.setCellValue(record.getR23_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(23);

							// row24
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR24_orig_amt() != null) {
							    cellB.setCellValue(record.getR24_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row24
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR24_fore_amt() != null) {
							    cellC.setCellValue(record.getR24_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row24
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR24_no_of_acc() != null) {
							    cellE.setCellValue(record.getR24_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(24);

							// row25
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR25_orig_amt() != null) {
							    cellB.setCellValue(record.getR25_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row25
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR25_fore_amt() != null) {
							    cellC.setCellValue(record.getR25_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row25
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR25_no_of_acc() != null) {
							    cellE.setCellValue(record.getR25_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row26
							row = sheet.getRow(25);

							// row26
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR26_orig_amt() != null) {
							    cellB.setCellValue(record.getR26_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row26
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR26_fore_amt() != null) {
							    cellC.setCellValue(record.getR26_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row26
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR26_no_of_acc() != null) {
							    cellE.setCellValue(record.getR26_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row27
							row = sheet.getRow(26);

							// row27
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR27_orig_amt() != null) {
							    cellB.setCellValue(record.getR27_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row27
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR27_fore_amt() != null) {
							    cellC.setCellValue(record.getR27_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row27
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR27_no_of_acc() != null) {
							    cellE.setCellValue(record.getR27_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row29
							row = sheet.getRow(28);

							// row29
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR29_orig_amt() != null) {
							    cellB.setCellValue(record.getR29_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row29
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR29_fore_amt() != null) {
							    cellC.setCellValue(record.getR29_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row29
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR29_no_of_acc() != null) {
							    cellE.setCellValue(record.getR29_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row30
							row = sheet.getRow(29);

							// row30
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR30_orig_amt() != null) {
							    cellB.setCellValue(record.getR30_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row30
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR30_fore_amt() != null) {
							    cellC.setCellValue(record.getR30_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row30
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR30_no_of_acc() != null) {
							    cellE.setCellValue(record.getR30_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row31
							row = sheet.getRow(30);

							// row31
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR31_orig_amt() != null) {
							    cellB.setCellValue(record.getR31_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row31
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR31_fore_amt() != null) {
							    cellC.setCellValue(record.getR31_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row31
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR31_no_of_acc() != null) {
							    cellE.setCellValue(record.getR31_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row32
							row = sheet.getRow(31);

							// row32
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR32_orig_amt() != null) {
							    cellB.setCellValue(record.getR32_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row32
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR32_fore_amt() != null) {
							    cellC.setCellValue(record.getR32_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row32
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR32_no_of_acc() != null) {
							    cellE.setCellValue(record.getR32_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row33
							row = sheet.getRow(32);

							// row33
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR33_orig_amt() != null) {
							    cellB.setCellValue(record.getR33_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row33
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR33_fore_amt() != null) {
							    cellC.setCellValue(record.getR33_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row33
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR33_no_of_acc() != null) {
							    cellE.setCellValue(record.getR33_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row34
							row = sheet.getRow(33);

							// row34
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR34_orig_amt() != null) {
							    cellB.setCellValue(record.getR34_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row34
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR34_fore_amt() != null) {
							    cellC.setCellValue(record.getR34_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row34
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR34_no_of_acc() != null) {
							    cellE.setCellValue(record.getR34_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row35
							row = sheet.getRow(34);

							// row35
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR35_orig_amt() != null) {
							    cellB.setCellValue(record.getR35_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row35
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR35_fore_amt() != null) {
							    cellC.setCellValue(record.getR35_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row35
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR35_no_of_acc() != null) {
							    cellE.setCellValue(record.getR35_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row36
							row = sheet.getRow(35);

							// row36
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR36_orig_amt() != null) {
							    cellB.setCellValue(record.getR36_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row36
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR36_fore_amt() != null) {
							    cellC.setCellValue(record.getR36_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row36
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR36_no_of_acc() != null) {
							    cellE.setCellValue(record.getR36_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row38
							row = sheet.getRow(37);

							// row38
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR38_orig_amt() != null) {
							    cellB.setCellValue(record.getR38_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row38
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR38_fore_amt() != null) {
							    cellC.setCellValue(record.getR38_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row38
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR38_no_of_acc() != null) {
							    cellE.setCellValue(record.getR38_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row39
							row = sheet.getRow(38);

							// row39
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR39_orig_amt() != null) {
							    cellB.setCellValue(record.getR39_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row39
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR39_fore_amt() != null) {
							    cellC.setCellValue(record.getR39_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row39
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR39_no_of_acc() != null) {
							    cellE.setCellValue(record.getR39_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row41
							row = sheet.getRow(40);

							// row41
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR41_orig_amt() != null) {
							    cellB.setCellValue(record.getR41_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row41
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR41_fore_amt() != null) {
							    cellC.setCellValue(record.getR41_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row41
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR41_no_of_acc() != null) {
							    cellE.setCellValue(record.getR41_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row42
							row = sheet.getRow(41);

							// row42
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR42_orig_amt() != null) {
							    cellB.setCellValue(record.getR42_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row42
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR42_fore_amt() != null) {
							    cellC.setCellValue(record.getR42_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row42
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR42_no_of_acc() != null) {
							    cellE.setCellValue(record.getR42_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							// row44
							row = sheet.getRow(43);

							// row44
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR44_orig_amt() != null) {
							    cellB.setCellValue(record.getR44_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row44
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR44_fore_amt() != null) {
							    cellC.setCellValue(record.getR44_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row44
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR44_no_of_acc() != null) {
							    cellE.setCellValue(record.getR44_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row45
							row = sheet.getRow(44);

							// row45
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR45_orig_amt() != null) {
							    cellB.setCellValue(record.getR45_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row45
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR45_fore_amt() != null) {
							    cellC.setCellValue(record.getR45_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row45
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR45_no_of_acc() != null) {
							    cellE.setCellValue(record.getR45_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row46
							row = sheet.getRow(45);

							// row46
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR46_orig_amt() != null) {
							    cellB.setCellValue(record.getR46_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row46
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR46_fore_amt() != null) {
							    cellC.setCellValue(record.getR46_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row46
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR46_no_of_acc() != null) {
							    cellE.setCellValue(record.getR46_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row47
							row = sheet.getRow(46);

							// row47
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR47_orig_amt() != null) {
							    cellB.setCellValue(record.getR47_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row47
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR47_fore_amt() != null) {
							    cellC.setCellValue(record.getR47_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row47
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR47_no_of_acc() != null) {
							    cellE.setCellValue(record.getR47_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row49
							row = sheet.getRow(48);

							// row49
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR49_orig_amt() != null) {
							    cellB.setCellValue(record.getR49_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row49
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR49_fore_amt() != null) {
							    cellC.setCellValue(record.getR49_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row49
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR49_no_of_acc() != null) {
							    cellE.setCellValue(record.getR49_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row50
							row = sheet.getRow(49);

							// row50
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR50_orig_amt() != null) {
							    cellB.setCellValue(record.getR50_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row50
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR50_fore_amt() != null) {
							    cellC.setCellValue(record.getR50_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row50
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR50_no_of_acc() != null) {
							    cellE.setCellValue(record.getR50_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row51
							row = sheet.getRow(50);

							// row51
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR51_orig_amt() != null) {
							    cellB.setCellValue(record.getR51_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row51
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR51_fore_amt() != null) {
							    cellC.setCellValue(record.getR51_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row51
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR51_no_of_acc() != null) {
							    cellE.setCellValue(record.getR51_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row53
							row = sheet.getRow(52);

							// row53
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR53_orig_amt() != null) {
							    cellB.setCellValue(record.getR53_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row53
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR53_fore_amt() != null) {
							    cellC.setCellValue(record.getR53_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row53
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR53_no_of_acc() != null) {
							    cellE.setCellValue(record.getR53_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row54
							row = sheet.getRow(53);

							// row54
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR54_orig_amt() != null) {
							    cellB.setCellValue(record.getR54_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row54
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR54_fore_amt() != null) {
							    cellC.setCellValue(record.getR54_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row54
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR54_no_of_acc() != null) {
							    cellE.setCellValue(record.getR54_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row55
							row = sheet.getRow(54);

							// row55
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR55_orig_amt() != null) {
							    cellB.setCellValue(record.getR55_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row55
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR55_fore_amt() != null) {
							    cellC.setCellValue(record.getR55_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row55
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR55_no_of_acc() != null) {
							    cellE.setCellValue(record.getR55_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// row57
							row = sheet.getRow(56);

							// row57
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR57_orig_amt() != null) {
							    cellB.setCellValue(record.getR57_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row57
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR57_fore_amt() != null) {
							    cellC.setCellValue(record.getR57_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row57
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR57_no_of_acc() != null) {
							    cellE.setCellValue(record.getR57_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row58
							row = sheet.getRow(57);

							// row58
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR58_orig_amt() != null) {
							    cellB.setCellValue(record.getR58_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row58
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR58_fore_amt() != null) {
							    cellC.setCellValue(record.getR58_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row58
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR58_no_of_acc() != null) {
							    cellE.setCellValue(record.getR58_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row59
							row = sheet.getRow(58);

							// row59
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR59_orig_amt() != null) {
							    cellB.setCellValue(record.getR59_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row59
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR59_fore_amt() != null) {
							    cellC.setCellValue(record.getR59_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row59
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR59_no_of_acc() != null) {
							    cellE.setCellValue(record.getR59_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row60
							row = sheet.getRow(59);

							// row60
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR60_orig_amt() != null) {
							    cellB.setCellValue(record.getR60_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row60
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR60_fore_amt() != null) {
							    cellC.setCellValue(record.getR60_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row60
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR60_no_of_acc() != null) {
							    cellE.setCellValue(record.getR60_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row61
							row = sheet.getRow(60);

							// row61
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR61_orig_amt() != null) {
							    cellB.setCellValue(record.getR61_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row61
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR61_fore_amt() != null) {
							    cellC.setCellValue(record.getR61_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row61
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR61_no_of_acc() != null) {
							    cellE.setCellValue(record.getR61_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row62
							row = sheet.getRow(61);

							// row62
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR62_orig_amt() != null) {
							    cellB.setCellValue(record.getR62_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row62
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR62_fore_amt() != null) {
							    cellC.setCellValue(record.getR62_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row62
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR62_no_of_acc() != null) {
							    cellE.setCellValue(record.getR62_no_of_acc().doubleValue());
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
							
							//audit service 
							
							ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
							if (attrs != null) {
								HttpServletRequest request = attrs.getRequest();
								String userid = (String) request.getSession().getAttribute("USERID");
								auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_RLFA2 SUMMARY", null, "BRRS_Q_RLFA2_SUMMARYTABLE");
							}
							

							return out.toByteArray();
						}	
					}
				}
			}
			
//=====================================================
// Summary EXCEL  EMAIL
//=====================================================

	// Normal Email Excel
			public byte[] BRRS_Q_RLFA2EmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Email Excel generation process in memory.");
				
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_Q_RLFA2ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_Q_RLFA2EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 
				else {
				List<Q_RLFA2_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_Q_RLFA2 report. Returning empty result.");
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
							Q_RLFA2_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
							
							//row7
														// Column B
														Cell cellBdate = row.createCell(3);
														if (record.getReport_date() != null) {
															cellBdate.setCellValue(record.getReport_date());
															cellBdate.setCellStyle(dateStyle);
														} else {
															cellBdate.setCellValue("");
															cellBdate.setCellStyle(textStyle);
														}
														
														
														
														//ROW 9
														row = sheet.getRow(8);
														
							
							
							// row10
							// Column B2 - Original Amount 
							Cell cellB = row.createCell(3);
							if (record.getR10_orig_amt() != null) {
								cellB.setCellValue(record.getR10_orig_amt().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row10
						    // Column C3 - Foreclosure Amount
							Cell cellC = row.createCell(4);
							if (record.getR10_fore_amt() != null) {
								cellC.setCellValue(record.getR10_fore_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row10
				            // Column D4 - No. of Accounts
							Cell cellE = row.createCell(5);
							if (record.getR10_no_of_acc() != null) {
								cellE.setCellValue(record.getR10_no_of_acc().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							
							
							// row11
							
							row = sheet.getRow(9);
							
							// Column B2 - Original Amount 
							 cellB = row.createCell(3);
							if (record.getR11_orig_amt() != null) {
								cellB.setCellValue(record.getR11_orig_amt().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row11
						    // Column C3 - Foreclosure Amount
							 cellC = row.createCell(4);
							if (record.getR11_fore_amt() != null) {
								cellC.setCellValue(record.getR11_fore_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
				            // Column D4 - No. of Accounts
							 cellE = row.createCell(5);
							if (record.getR11_no_of_acc() != null) {
								cellE.setCellValue(record.getR11_no_of_acc().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							
							
							// row12
							row = sheet.getRow(10);
							
							// row12
							// Column B2 - Original Amount 
							 cellB = row.createCell(3);
							if (record.getR12_orig_amt() != null) {
							    cellB.setCellValue(record.getR12_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row12
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(4);
							if (record.getR12_fore_amt() != null) {
							    cellC.setCellValue(record.getR12_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row12
							// Column D4 - No. of Accounts
							cellE = row.createCell(5);
							if (record.getR12_no_of_acc() != null) {
							    cellE.setCellValue(record.getR12_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// row13
							row = sheet.getRow(11);

							// row13
							// Column B2 - Original Amount 
							cellB = row.createCell(3);
							if (record.getR13_orig_amt() != null) {
							    cellB.setCellValue(record.getR13_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row13
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(4);
							if (record.getR13_fore_amt() != null) {
							    cellC.setCellValue(record.getR13_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row13
							// Column D4 - No. of Accounts
							cellE = row.createCell(5);
							if (record.getR13_no_of_acc() != null) {
							    cellE.setCellValue(record.getR13_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
										// row14
							row = sheet.getRow(12);

							// row14
							// Column B2 - Original Amount 
							cellB = row.createCell(3);
							if (record.getR14_orig_amt() != null) {
							    cellB.setCellValue(record.getR14_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row14
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(4);
							if (record.getR14_fore_amt() != null) {
							    cellC.setCellValue(record.getR14_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row14
							// Column D4 - No. of Accounts
							cellE = row.createCell(5);
							if (record.getR14_no_of_acc() != null) {
							    cellE.setCellValue(record.getR14_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
			// row15------>          a) Agriculture, Forestry, Fishing(NEW 14)
			row = sheet.getRow(13);

			// row15
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR15_orig_amt() != null) {
				cellB.setCellValue(record.getR15_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row15
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR15_fore_amt() != null) {
				cellC.setCellValue(record.getR15_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row15
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR15_no_of_acc() != null) {
				cellE.setCellValue(record.getR15_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row16------->         b) Mining and Quarying(NEW 15)
			row = sheet.getRow(14);

			// row16
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR16_orig_amt() != null) {
				cellB.setCellValue(record.getR16_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row16
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR16_fore_amt() != null) {
				cellC.setCellValue(record.getR16_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row16
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR16_no_of_acc() != null) {
				cellE.setCellValue(record.getR16_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row17-------->          c) Manufacturing(NEW 16)
			row = sheet.getRow(15);

			// row17
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR17_orig_amt() != null) {
				cellB.setCellValue(record.getR17_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row17
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR17_fore_amt() != null) {
				cellC.setCellValue(record.getR17_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row17
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR17_no_of_acc() != null) {
				cellE.setCellValue(record.getR17_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row18----->          d) Construction(NEW 17)
			row = sheet.getRow(16);

			// row18
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR18_orig_amt() != null) {
				cellB.setCellValue(record.getR18_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row18
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR18_fore_amt() != null) {
				cellC.setCellValue(record.getR18_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row18
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR18_no_of_acc() != null) {
				cellE.setCellValue(record.getR18_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row19----->         e) Commercial real estate(NEW 25)
			row = sheet.getRow(24);

			// row19
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR19_orig_amt() != null) {
				cellB.setCellValue(record.getR19_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row19
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR19_fore_amt() != null) {
				cellC.setCellValue(record.getR19_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row19
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR19_no_of_acc() != null) {
				cellE.setCellValue(record.getR19_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row20----->         f) Electricity(NEW 19)
			row = sheet.getRow(18);

			// row20
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR20_orig_amt() != null) {
				cellB.setCellValue(record.getR20_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row20
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR20_fore_amt() != null) {
				cellC.setCellValue(record.getR20_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row20
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR20_no_of_acc() != null) {
				cellE.setCellValue(record.getR20_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row21->         g) Water(NEW 20)
			row = sheet.getRow(19);

			// row21
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR21_orig_amt() != null) {
				cellB.setCellValue(record.getR21_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row21
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR21_fore_amt() != null) {
				cellC.setCellValue(record.getR21_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row21
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR21_no_of_acc() != null) {
				cellE.setCellValue(record.getR21_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row22---->          h) Telecommunication and post(NEW 21)
			row = sheet.getRow(20);

			// row22
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR22_orig_amt() != null) {
				cellB.setCellValue(record.getR22_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row22
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR22_fore_amt() != null) {
				cellC.setCellValue(record.getR22_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row22
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR22_no_of_acc() != null) {
				cellE.setCellValue(record.getR22_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row23--->         i) Tourism and hotels(NEW 22)
			row = sheet.getRow(21);

			// row23
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR23_orig_amt() != null) {
				cellB.setCellValue(record.getR23_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row23
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR23_fore_amt() != null) {
				cellC.setCellValue(record.getR23_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row23
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR23_no_of_acc() != null) {
				cellE.setCellValue(record.getR23_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row24------>          j) Transport and storage(NEW 23)
			row = sheet.getRow(22);

			// row24
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR24_orig_amt() != null) {
				cellB.setCellValue(record.getR24_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row24
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR24_fore_amt() != null) {
				cellC.setCellValue(record.getR24_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row24
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR24_no_of_acc() != null) {
				cellE.setCellValue(record.getR24_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row25-->         k) Trade, restaurants and bars(NEW 24)
			row = sheet.getRow(23);

			// row25
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR25_orig_amt() != null) {
				cellB.setCellValue(record.getR25_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row25
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR25_fore_amt() != null) {
				cellC.setCellValue(record.getR25_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row25
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR25_no_of_acc() != null) {
				cellE.setCellValue(record.getR25_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			


		


			// row28------->   (v)  Households (sum of lines (a) to (h)):  (NEW 26)

			row = sheet.getRow(25);

			// row28
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR28_orig_amt() != null) {
				cellB.setCellValue(record.getR28_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row28
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR28_fore_amt() != null) {
				cellC.setCellValue(record.getR28_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row28
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR28_no_of_acc() != null) {
				cellE.setCellValue(record.getR28_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row29--->        a) Residential property (owner occupied) (NEW 27)

			row = sheet.getRow(26);

			// row29
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR29_orig_amt() != null) {
				cellB.setCellValue(record.getR29_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row29
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR29_fore_amt() != null) {
				cellC.setCellValue(record.getR29_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row29
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR29_no_of_acc() != null) {
				cellE.setCellValue(record.getR29_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row30---->        b) Residential property (rented)--( NEW 28 b) Other property)
			row = sheet.getRow(27);

			// row30
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR30_orig_amt() != null) {
				cellB.setCellValue(record.getR30_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row30
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR30_fore_amt() != null) {
				cellC.setCellValue(record.getR30_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row30
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR30_no_of_acc() != null) {
				cellE.setCellValue(record.getR30_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


		


			// row32--------->         d) Motor vehicle(NEW 29)
			row = sheet.getRow(28);

			// row32
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR32_orig_amt() != null) {
				cellB.setCellValue(record.getR32_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row32
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR32_fore_amt() != null) {
				cellC.setCellValue(record.getR32_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row32
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR32_no_of_acc() != null) {
				cellE.setCellValue(record.getR32_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row33-->         e) Household goods(NEW 30)
			row = sheet.getRow(29);

			// row33
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR33_orig_amt() != null) {
				cellB.setCellValue(record.getR33_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row33
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR33_fore_amt() != null) {
				cellC.setCellValue(record.getR33_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row33
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR33_no_of_acc() != null) {
				cellE.setCellValue(record.getR33_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row34-------->         f) Credit card loans(NEW 31)
			row = sheet.getRow(30);

			// row34
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR34_orig_amt() != null) {
				cellB.setCellValue(record.getR34_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row34
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR34_fore_amt() != null) {
				cellC.setCellValue(record.getR34_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row34
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR34_no_of_acc() != null) {
				cellE.setCellValue(record.getR34_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row35--->        g) Non-Profit Institutions Serving Households(NEW 33)
			row = sheet.getRow(32);

			// row35
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR35_orig_amt() != null) {
				cellB.setCellValue(record.getR35_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row35
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR35_fore_amt() != null) {
				cellC.setCellValue(record.getR35_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row35
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR35_no_of_acc() != null) {
				cellE.setCellValue(record.getR35_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row36------------->        h)  Other specify(NEW 32)
			row = sheet.getRow(31);

			// row36
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR36_orig_amt() != null) {
				cellB.setCellValue(record.getR36_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row36
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR36_fore_amt() != null) {
				cellC.setCellValue(record.getR36_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row36
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR36_no_of_acc() != null) {
				cellE.setCellValue(record.getR36_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row37----->    (vi) Non-Residents (sum of lines (a) and (b)):(NEW 34)
			row = sheet.getRow(33);

			// row37
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR37_orig_amt() != null) {
				cellB.setCellValue(record.getR37_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row37
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR37_fore_amt() != null) {
				cellC.setCellValue(record.getR37_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row37
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR37_no_of_acc() != null) {
				cellE.setCellValue(record.getR37_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row38-->        a) Other Non-Financial Corporations(NEW 35)
			row = sheet.getRow(34);

			// row38
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR38_orig_amt() != null) {
				cellB.setCellValue(record.getR38_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row38
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR38_fore_amt() != null) {
				cellC.setCellValue(record.getR38_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row38
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR38_no_of_acc() != null) {
				cellE.setCellValue(record.getR38_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row39---------->        b) Households(NEW 36)
			row = sheet.getRow(35);

			// row39
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR39_orig_amt() != null) {
				cellB.setCellValue(record.getR39_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row39
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR39_fore_amt() != null) {
				cellC.setCellValue(record.getR39_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row39
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR39_no_of_acc() != null) {
				cellE.setCellValue(record.getR39_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row40------->2.  Financial institutional units (sum of lines (i) to (v)):(NEW 37)
			row = sheet.getRow(36);

			// row40
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR40_orig_amt() != null) {
				cellB.setCellValue(record.getR40_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row40
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR40_fore_amt() != null) {
				cellC.setCellValue(record.getR40_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row40
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR40_no_of_acc() != null) {
				cellE.setCellValue(record.getR40_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row41-->   (i)    Central Bank(NEW 38)
			row = sheet.getRow(37);

			// row41
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR41_orig_amt() != null) {
				cellB.setCellValue(record.getR41_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row41
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR41_fore_amt() != null) {
				cellC.setCellValue(record.getR41_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row41
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR41_no_of_acc() != null) {
				cellE.setCellValue(record.getR41_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row42---------->   (ii)   Commercial Banks(NEW 39)
			row = sheet.getRow(38);

			// row42
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR42_orig_amt() != null) {
				cellB.setCellValue(record.getR42_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row42
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR42_fore_amt() != null) {
				cellC.setCellValue(record.getR42_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row42
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR42_no_of_acc() != null) {
				cellE.setCellValue(record.getR42_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row43-->   (iii)  Other Depository Corporations (sum of lines (a) to (d)):(NEW 40)
			row = sheet.getRow(39);

			// row43
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR43_orig_amt() != null) {
				cellB.setCellValue(record.getR43_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row43
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR43_fore_amt() != null) {
				cellC.setCellValue(record.getR43_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row43
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR43_no_of_acc() != null) {
				cellE.setCellValue(record.getR43_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row44--->        a) Botswana Savings Bank (BSB)(NEW 41)
			row = sheet.getRow(40);

			// row44
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR44_orig_amt() != null) {
				cellB.setCellValue(record.getR44_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row44
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR44_fore_amt() != null) {
				cellC.setCellValue(record.getR44_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row44
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR44_no_of_acc() != null) {
				cellE.setCellValue(record.getR44_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row45-->        b) Botswana Building Society (BBS)(NEW 42)
			row = sheet.getRow(41);

			// row45
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR45_orig_amt() != null) {
				cellB.setCellValue(record.getR45_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row45
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR45_fore_amt() != null) {
				cellC.setCellValue(record.getR45_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row45
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR45_no_of_acc() != null) {
				cellE.setCellValue(record.getR45_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}



			
			
			
			// row51------------>        c) SACCOs(NEW 43)
			row = sheet.getRow(42);

			// row51
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR51_orig_amt() != null) {
				cellB.setCellValue(record.getR51_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row51
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR51_fore_amt() != null) {
				cellC.setCellValue(record.getR51_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row51
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR51_no_of_acc() != null) {
				cellE.setCellValue(record.getR51_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}



			// row47-------->        d) Other (specify)*(NEW 44)
			row = sheet.getRow(43);

			// row47
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR47_orig_amt() != null) {
				cellB.setCellValue(record.getR47_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row47
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR47_fore_amt() != null) {
				cellC.setCellValue(record.getR47_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row47
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR47_no_of_acc() != null) {
				cellE.setCellValue(record.getR47_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row48-->    (iv)  Other Financial Corporations (sum of lines (a) to (e)):(NEW 45)
			row = sheet.getRow(44);

			// row48
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR48_orig_amt() != null) {
				cellB.setCellValue(record.getR48_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row48
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR48_fore_amt() != null) {
				cellC.setCellValue(record.getR48_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row48
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR48_no_of_acc() != null) {
				cellE.setCellValue(record.getR48_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row49--->        a) Insurance Companies(NEW 46)
			row = sheet.getRow(45);

			// row49
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR49_orig_amt() != null) {
				cellB.setCellValue(record.getR49_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row49
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR49_fore_amt() != null) {
				cellC.setCellValue(record.getR49_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row49
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR49_no_of_acc() != null) {
				cellE.setCellValue(record.getR49_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row50----->        b) Pension Funds(NEW 47 )
			row = sheet.getRow(46);

			// row50
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR50_orig_amt() != null) {
				cellB.setCellValue(record.getR50_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row50
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR50_fore_amt() != null) {
				cellC.setCellValue(record.getR50_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row50
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR50_no_of_acc() != null) {
				cellE.setCellValue(record.getR50_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			


			// row52----->        c) Other Financial Intermediaries (sum 1 to 4)(NEW 48)
			row = sheet.getRow(47);

			// row52
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR52_orig_amt() != null) {
				cellB.setCellValue(record.getR52_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row52
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR52_fore_amt() != null) {
				cellC.setCellValue(record.getR52_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row52
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR52_no_of_acc() != null) {
				cellE.setCellValue(record.getR52_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}
			
			
	// row57   --------- 1.Asset managers(49 NEW)

			row = sheet.getRow(48);

			// row57
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR57_orig_amt() != null) {
				cellB.setCellValue(record.getR57_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row57
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR57_fore_amt() != null) {
				cellC.setCellValue(record.getR57_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row57
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR57_no_of_acc() != null) {
				cellE.setCellValue(record.getR57_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}
			
			
			

			// row53-->             2.Finance companies
			row = sheet.getRow(49);

			// row53
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR53_orig_amt() != null) {
				cellB.setCellValue(record.getR53_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row53
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR53_fore_amt() != null) {
				cellC.setCellValue(record.getR53_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row53
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR53_no_of_acc() != null) {
				cellE.setCellValue(record.getR53_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row54--->  3.Medical Aid Schemes

			row = sheet.getRow(50);

			// row54
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR54_orig_amt() != null) {
				cellB.setCellValue(record.getR54_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row54
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR54_fore_amt() != null) {
				cellC.setCellValue(record.getR54_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row54
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR54_no_of_acc() != null) {
				cellE.setCellValue(record.getR54_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row55--->  4.Public sector financial intermediaries
			row = sheet.getRow(51);

			// row55
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR55_orig_amt() != null) {
				cellB.setCellValue(record.getR55_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row55
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR55_fore_amt() != null) {
				cellC.setCellValue(record.getR55_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row55
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR55_no_of_acc() != null) {
				cellE.setCellValue(record.getR55_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row56--->        e) Financial Auxiliaries (sum 1 to 5)
			row = sheet.getRow(52);

			// row56
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR56_orig_amt() != null) {
				cellB.setCellValue(record.getR56_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row56
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR56_fore_amt() != null) {
				cellC.setCellValue(record.getR56_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row56
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR56_no_of_acc() != null) {
				cellE.setCellValue(record.getR56_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			

			// row58
			row = sheet.getRow(53);

			// row58
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR58_orig_amt() != null) {
				cellB.setCellValue(record.getR58_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row58
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR58_fore_amt() != null) {
				cellC.setCellValue(record.getR58_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row58
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR58_no_of_acc() != null) {
				cellE.setCellValue(record.getR58_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row59
			row = sheet.getRow(54);

			// row59
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR59_orig_amt() != null) {
				cellB.setCellValue(record.getR59_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row59
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR59_fore_amt() != null) {
				cellC.setCellValue(record.getR59_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row59
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR59_no_of_acc() != null) {
				cellE.setCellValue(record.getR59_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row60
			row = sheet.getRow(55);

			// row60
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR60_orig_amt() != null) {
				cellB.setCellValue(record.getR60_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row60
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR60_fore_amt() != null) {
				cellC.setCellValue(record.getR60_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row60
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR60_no_of_acc() != null) {
				cellE.setCellValue(record.getR60_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row61
			row = sheet.getRow(56);

			// row61
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR61_orig_amt() != null) {
				cellB.setCellValue(record.getR61_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row61
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR61_fore_amt() != null) {
				cellC.setCellValue(record.getR61_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row61
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR61_no_of_acc() != null) {
				cellE.setCellValue(record.getR61_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row62
			row = sheet.getRow(57);

			// row62
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR62_orig_amt() != null) {
				cellB.setCellValue(record.getR62_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row62
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR62_fore_amt() != null) {
				cellC.setCellValue(record.getR62_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row62
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR62_no_of_acc() != null) {
				cellE.setCellValue(record.getR62_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row63
			row = sheet.getRow(58);

			// row63
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR63_orig_amt() != null) {
				cellB.setCellValue(record.getR63_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row63
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR63_fore_amt() != null) {
				cellC.setCellValue(record.getR63_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row63
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR63_no_of_acc() != null) {
				cellE.setCellValue(record.getR63_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}

	//=====================================================================================
			// row64------>         e) Real Estate/Property Development(NEW 18)
			row = sheet.getRow(17);

			// row64
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR64_orig_amt() != null) {
				cellB.setCellValue(record.getR64_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row64
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR64_fore_amt() != null) {
				cellC.setCellValue(record.getR64_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row64
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR64_no_of_acc() != null) {
				cellE.setCellValue(record.getR64_no_of_acc().doubleValue());
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
					
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_RLFA2 EMAIL SUMMARY", null, "BRRS_Q_RLFA2_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
				}
			}
			


//=====================================================
//ARCHIVAL SUMMARY EXCEL  FORMAT
//=====================================================

// Archival format excel
			public byte[] getExcelQ_RLFA2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory in Archival.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_Q_RLFA2ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 

				List<Q_RLFA2_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for Q_RLFA2 report. Returning empty result.");
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
							Q_RLFA2_Archival_Summary_Entity record = dataList.get(i);
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
													
													
													
													//ROW 13
													row = sheet.getRow(10);
													

	              			// row11
							// Column B2 - Original Amount 
							Cell cellB = row.createCell(1);
							if (record.getR11_orig_amt() != null) {
								cellB.setCellValue(record.getR11_orig_amt().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row11
						    // Column C3 - Foreclosure Amount
							Cell cellC = row.createCell(2);
							if (record.getR11_fore_amt() != null) {
								cellC.setCellValue(record.getR11_fore_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
				            // Column D4 - No. of Accounts
							Cell cellE = row.createCell(3);
							if (record.getR11_no_of_acc() != null) {
								cellE.setCellValue(record.getR11_no_of_acc().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(11);
							
							// row12
							// Column B2 - Original Amount 
							 cellB = row.createCell(1);
							if (record.getR12_orig_amt() != null) {
							    cellB.setCellValue(record.getR12_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row12
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR12_fore_amt() != null) {
							    cellC.setCellValue(record.getR12_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row12
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR12_no_of_acc() != null) {
							    cellE.setCellValue(record.getR12_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							

							// row13
							row = sheet.getRow(12);

							// row13
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR13_orig_amt() != null) {
							    cellB.setCellValue(record.getR13_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row13
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR13_fore_amt() != null) {
							    cellC.setCellValue(record.getR13_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row13
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR13_no_of_acc() != null) {
							    cellE.setCellValue(record.getR13_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// row15
							row = sheet.getRow(14);

							// row15
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR15_orig_amt() != null) {
							    cellB.setCellValue(record.getR15_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row15
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR15_fore_amt() != null) {
							    cellC.setCellValue(record.getR15_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row15
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR15_no_of_acc() != null) {
							    cellE.setCellValue(record.getR15_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row16
							row = sheet.getRow(15);

							// row16
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR16_orig_amt() != null) {
							    cellB.setCellValue(record.getR16_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row16
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR16_fore_amt() != null) {
							    cellC.setCellValue(record.getR16_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row16
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR16_no_of_acc() != null) {
							    cellE.setCellValue(record.getR16_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(16);

							// row17
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR17_orig_amt() != null) {
							    cellB.setCellValue(record.getR17_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row17
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR17_fore_amt() != null) {
							    cellC.setCellValue(record.getR17_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row17
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR17_no_of_acc() != null) {
							    cellE.setCellValue(record.getR17_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row18
							row = sheet.getRow(17);

							// row18
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR18_orig_amt() != null) {
							    cellB.setCellValue(record.getR18_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row18
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR18_fore_amt() != null) {
							    cellC.setCellValue(record.getR18_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row18
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR18_no_of_acc() != null) {
							    cellE.setCellValue(record.getR18_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(18);

							// row19
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR19_orig_amt() != null) {
							    cellB.setCellValue(record.getR19_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row19
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR19_fore_amt() != null) {
							    cellC.setCellValue(record.getR19_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row19
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR19_no_of_acc() != null) {
							    cellE.setCellValue(record.getR19_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row20
							row = sheet.getRow(19);

							// row20
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR20_orig_amt() != null) {
							    cellB.setCellValue(record.getR20_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row20
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR20_fore_amt() != null) {
							    cellC.setCellValue(record.getR20_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row20
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR20_no_of_acc() != null) {
							    cellE.setCellValue(record.getR20_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row21
							row = sheet.getRow(20);

							// row21
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR21_orig_amt() != null) {
							    cellB.setCellValue(record.getR21_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row21
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR21_fore_amt() != null) {
							    cellC.setCellValue(record.getR21_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row21
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR21_no_of_acc() != null) {
							    cellE.setCellValue(record.getR21_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row22
							row = sheet.getRow(21);

							// row22
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR22_orig_amt() != null) {
							    cellB.setCellValue(record.getR22_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row22
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR22_fore_amt() != null) {
							    cellC.setCellValue(record.getR22_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row22
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR22_no_of_acc() != null) {
							    cellE.setCellValue(record.getR22_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row23
							row = sheet.getRow(22);

							// row23
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR23_orig_amt() != null) {
							    cellB.setCellValue(record.getR23_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row23
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR23_fore_amt() != null) {
							    cellC.setCellValue(record.getR23_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row23
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR23_no_of_acc() != null) {
							    cellE.setCellValue(record.getR23_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(23);

							// row24
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR24_orig_amt() != null) {
							    cellB.setCellValue(record.getR24_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row24
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR24_fore_amt() != null) {
							    cellC.setCellValue(record.getR24_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row24
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR24_no_of_acc() != null) {
							    cellE.setCellValue(record.getR24_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(24);

							// row25
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR25_orig_amt() != null) {
							    cellB.setCellValue(record.getR25_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row25
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR25_fore_amt() != null) {
							    cellC.setCellValue(record.getR25_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row25
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR25_no_of_acc() != null) {
							    cellE.setCellValue(record.getR25_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row26
							row = sheet.getRow(25);

							// row26
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR26_orig_amt() != null) {
							    cellB.setCellValue(record.getR26_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row26
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR26_fore_amt() != null) {
							    cellC.setCellValue(record.getR26_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row26
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR26_no_of_acc() != null) {
							    cellE.setCellValue(record.getR26_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row27
							row = sheet.getRow(26);

							// row27
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR27_orig_amt() != null) {
							    cellB.setCellValue(record.getR27_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row27
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR27_fore_amt() != null) {
							    cellC.setCellValue(record.getR27_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row27
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR27_no_of_acc() != null) {
							    cellE.setCellValue(record.getR27_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row29
							row = sheet.getRow(28);

							// row29
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR29_orig_amt() != null) {
							    cellB.setCellValue(record.getR29_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row29
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR29_fore_amt() != null) {
							    cellC.setCellValue(record.getR29_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row29
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR29_no_of_acc() != null) {
							    cellE.setCellValue(record.getR29_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row30
							row = sheet.getRow(29);

							// row30
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR30_orig_amt() != null) {
							    cellB.setCellValue(record.getR30_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row30
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR30_fore_amt() != null) {
							    cellC.setCellValue(record.getR30_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row30
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR30_no_of_acc() != null) {
							    cellE.setCellValue(record.getR30_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row31
							row = sheet.getRow(30);

							// row31
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR31_orig_amt() != null) {
							    cellB.setCellValue(record.getR31_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row31
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR31_fore_amt() != null) {
							    cellC.setCellValue(record.getR31_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row31
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR31_no_of_acc() != null) {
							    cellE.setCellValue(record.getR31_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row32
							row = sheet.getRow(31);

							// row32
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR32_orig_amt() != null) {
							    cellB.setCellValue(record.getR32_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row32
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR32_fore_amt() != null) {
							    cellC.setCellValue(record.getR32_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row32
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR32_no_of_acc() != null) {
							    cellE.setCellValue(record.getR32_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row33
							row = sheet.getRow(32);

							// row33
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR33_orig_amt() != null) {
							    cellB.setCellValue(record.getR33_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row33
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR33_fore_amt() != null) {
							    cellC.setCellValue(record.getR33_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row33
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR33_no_of_acc() != null) {
							    cellE.setCellValue(record.getR33_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row34
							row = sheet.getRow(33);

							// row34
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR34_orig_amt() != null) {
							    cellB.setCellValue(record.getR34_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row34
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR34_fore_amt() != null) {
							    cellC.setCellValue(record.getR34_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row34
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR34_no_of_acc() != null) {
							    cellE.setCellValue(record.getR34_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row35
							row = sheet.getRow(34);

							// row35
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR35_orig_amt() != null) {
							    cellB.setCellValue(record.getR35_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row35
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR35_fore_amt() != null) {
							    cellC.setCellValue(record.getR35_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row35
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR35_no_of_acc() != null) {
							    cellE.setCellValue(record.getR35_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row36
							row = sheet.getRow(35);

							// row36
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR36_orig_amt() != null) {
							    cellB.setCellValue(record.getR36_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row36
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR36_fore_amt() != null) {
							    cellC.setCellValue(record.getR36_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row36
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR36_no_of_acc() != null) {
							    cellE.setCellValue(record.getR36_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row38
							row = sheet.getRow(37);

							// row38
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR38_orig_amt() != null) {
							    cellB.setCellValue(record.getR38_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row38
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR38_fore_amt() != null) {
							    cellC.setCellValue(record.getR38_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row38
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR38_no_of_acc() != null) {
							    cellE.setCellValue(record.getR38_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row39
							row = sheet.getRow(38);

							// row39
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR39_orig_amt() != null) {
							    cellB.setCellValue(record.getR39_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row39
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR39_fore_amt() != null) {
							    cellC.setCellValue(record.getR39_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row39
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR39_no_of_acc() != null) {
							    cellE.setCellValue(record.getR39_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row41
							row = sheet.getRow(40);

							// row41
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR41_orig_amt() != null) {
							    cellB.setCellValue(record.getR41_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row41
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR41_fore_amt() != null) {
							    cellC.setCellValue(record.getR41_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row41
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR41_no_of_acc() != null) {
							    cellE.setCellValue(record.getR41_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row42
							row = sheet.getRow(41);

							// row42
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR42_orig_amt() != null) {
							    cellB.setCellValue(record.getR42_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row42
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR42_fore_amt() != null) {
							    cellC.setCellValue(record.getR42_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row42
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR42_no_of_acc() != null) {
							    cellE.setCellValue(record.getR42_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							// row44
							row = sheet.getRow(43);

							// row44
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR44_orig_amt() != null) {
							    cellB.setCellValue(record.getR44_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row44
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR44_fore_amt() != null) {
							    cellC.setCellValue(record.getR44_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row44
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR44_no_of_acc() != null) {
							    cellE.setCellValue(record.getR44_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row45
							row = sheet.getRow(44);

							// row45
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR45_orig_amt() != null) {
							    cellB.setCellValue(record.getR45_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row45
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR45_fore_amt() != null) {
							    cellC.setCellValue(record.getR45_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row45
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR45_no_of_acc() != null) {
							    cellE.setCellValue(record.getR45_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row46
							row = sheet.getRow(45);

							// row46
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR46_orig_amt() != null) {
							    cellB.setCellValue(record.getR46_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row46
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR46_fore_amt() != null) {
							    cellC.setCellValue(record.getR46_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row46
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR46_no_of_acc() != null) {
							    cellE.setCellValue(record.getR46_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row47
							row = sheet.getRow(46);

							// row47
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR47_orig_amt() != null) {
							    cellB.setCellValue(record.getR47_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row47
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR47_fore_amt() != null) {
							    cellC.setCellValue(record.getR47_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row47
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR47_no_of_acc() != null) {
							    cellE.setCellValue(record.getR47_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row49
							row = sheet.getRow(48);

							// row49
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR49_orig_amt() != null) {
							    cellB.setCellValue(record.getR49_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row49
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR49_fore_amt() != null) {
							    cellC.setCellValue(record.getR49_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row49
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR49_no_of_acc() != null) {
							    cellE.setCellValue(record.getR49_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row50
							row = sheet.getRow(49);

							// row50
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR50_orig_amt() != null) {
							    cellB.setCellValue(record.getR50_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row50
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR50_fore_amt() != null) {
							    cellC.setCellValue(record.getR50_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row50
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR50_no_of_acc() != null) {
							    cellE.setCellValue(record.getR50_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row51
							row = sheet.getRow(50);

							// row51
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR51_orig_amt() != null) {
							    cellB.setCellValue(record.getR51_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row51
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR51_fore_amt() != null) {
							    cellC.setCellValue(record.getR51_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row51
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR51_no_of_acc() != null) {
							    cellE.setCellValue(record.getR51_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row53
							row = sheet.getRow(52);

							// row53
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR53_orig_amt() != null) {
							    cellB.setCellValue(record.getR53_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row53
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR53_fore_amt() != null) {
							    cellC.setCellValue(record.getR53_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row53
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR53_no_of_acc() != null) {
							    cellE.setCellValue(record.getR53_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row54
							row = sheet.getRow(53);

							// row54
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR54_orig_amt() != null) {
							    cellB.setCellValue(record.getR54_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row54
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR54_fore_amt() != null) {
							    cellC.setCellValue(record.getR54_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row54
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR54_no_of_acc() != null) {
							    cellE.setCellValue(record.getR54_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row55
							row = sheet.getRow(54);

							// row55
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR55_orig_amt() != null) {
							    cellB.setCellValue(record.getR55_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row55
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR55_fore_amt() != null) {
							    cellC.setCellValue(record.getR55_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row55
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR55_no_of_acc() != null) {
							    cellE.setCellValue(record.getR55_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// row57
							row = sheet.getRow(56);

							// row57
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR57_orig_amt() != null) {
							    cellB.setCellValue(record.getR57_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row57
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR57_fore_amt() != null) {
							    cellC.setCellValue(record.getR57_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row57
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR57_no_of_acc() != null) {
							    cellE.setCellValue(record.getR57_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row58
							row = sheet.getRow(57);

							// row58
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR58_orig_amt() != null) {
							    cellB.setCellValue(record.getR58_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row58
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR58_fore_amt() != null) {
							    cellC.setCellValue(record.getR58_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row58
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR58_no_of_acc() != null) {
							    cellE.setCellValue(record.getR58_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row59
							row = sheet.getRow(58);

							// row59
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR59_orig_amt() != null) {
							    cellB.setCellValue(record.getR59_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row59
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR59_fore_amt() != null) {
							    cellC.setCellValue(record.getR59_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row59
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR59_no_of_acc() != null) {
							    cellE.setCellValue(record.getR59_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row60
							row = sheet.getRow(59);

							// row60
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR60_orig_amt() != null) {
							    cellB.setCellValue(record.getR60_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row60
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR60_fore_amt() != null) {
							    cellC.setCellValue(record.getR60_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row60
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR60_no_of_acc() != null) {
							    cellE.setCellValue(record.getR60_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row61
							row = sheet.getRow(60);

							// row61
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR61_orig_amt() != null) {
							    cellB.setCellValue(record.getR61_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row61
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR61_fore_amt() != null) {
							    cellC.setCellValue(record.getR61_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row61
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR61_no_of_acc() != null) {
							    cellE.setCellValue(record.getR61_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row62
							row = sheet.getRow(61);

							// row62
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR62_orig_amt() != null) {
							    cellB.setCellValue(record.getR62_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row62
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR62_fore_amt() != null) {
							    cellC.setCellValue(record.getR62_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row62
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR62_no_of_acc() != null) {
							    cellE.setCellValue(record.getR62_no_of_acc().doubleValue());
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
										auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_RLFA2 ARCHIVAL SUMMARY", null, "BRRS_Q_RLFA2_ARCHIVALTABLE_SUMMARY");
									}

				return out.toByteArray();
			}

		}

	

	
//=====================================================
//ARCHIVAL SUMMARY EXCEL  EMAIL
//=====================================================

// Archival Email Excel
			public byte[] BRRS_Q_RLFA2ARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Archival Email Excel generation process in memory.");

				List<Q_RLFA2_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_Q_RLFA2 report. Returning empty result.");
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
							Q_RLFA2_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
							//row7
							// Column B
							Cell cellBdate = row.createCell(3);
							if (record.getReport_date() != null) {
								cellBdate.setCellValue(record.getReport_date());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}
							
							
							
							//ROW 9
							row = sheet.getRow(8);
							

							// row10
							// Column B2 - Original Amount 
							Cell cellB = row.createCell(3);
							if (record.getR10_orig_amt() != null) {
								cellB.setCellValue(record.getR10_orig_amt().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row10
						    // Column C3 - Foreclosure Amount
							Cell cellC = row.createCell(4);
							if (record.getR10_fore_amt() != null) {
								cellC.setCellValue(record.getR10_fore_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row10
				            // Column D4 - No. of Accounts
							Cell cellE = row.createCell(5);
							if (record.getR10_no_of_acc() != null) {
								cellE.setCellValue(record.getR10_no_of_acc().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							
							
							// row11
							
							row = sheet.getRow(9);
							
							// Column B2 - Original Amount 
							 cellB = row.createCell(3);
							if (record.getR11_orig_amt() != null) {
								cellB.setCellValue(record.getR11_orig_amt().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row11
						    // Column C3 - Foreclosure Amount
							 cellC = row.createCell(4);
							if (record.getR11_fore_amt() != null) {
								cellC.setCellValue(record.getR11_fore_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
				            // Column D4 - No. of Accounts
							 cellE = row.createCell(5);
							if (record.getR11_no_of_acc() != null) {
								cellE.setCellValue(record.getR11_no_of_acc().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							
							
							// row12
							row = sheet.getRow(10);
							
							// row12
							// Column B2 - Original Amount 
							 cellB = row.createCell(3);
							if (record.getR12_orig_amt() != null) {
							    cellB.setCellValue(record.getR12_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row12
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(4);
							if (record.getR12_fore_amt() != null) {
							    cellC.setCellValue(record.getR12_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row12
							// Column D4 - No. of Accounts
							cellE = row.createCell(5);
							if (record.getR12_no_of_acc() != null) {
							    cellE.setCellValue(record.getR12_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// row13
							row = sheet.getRow(11);

							// row13
							// Column B2 - Original Amount 
							cellB = row.createCell(3);
							if (record.getR13_orig_amt() != null) {
							    cellB.setCellValue(record.getR13_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row13
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(4);
							if (record.getR13_fore_amt() != null) {
							    cellC.setCellValue(record.getR13_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row13
							// Column D4 - No. of Accounts
							cellE = row.createCell(5);
							if (record.getR13_no_of_acc() != null) {
							    cellE.setCellValue(record.getR13_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
										// row14
							row = sheet.getRow(12);

							// row14
							// Column B2 - Original Amount 
							cellB = row.createCell(3);
							if (record.getR14_orig_amt() != null) {
							    cellB.setCellValue(record.getR14_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row14
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(4);
							if (record.getR14_fore_amt() != null) {
							    cellC.setCellValue(record.getR14_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row14
							// Column D4 - No. of Accounts
							cellE = row.createCell(5);
							if (record.getR14_no_of_acc() != null) {
							    cellE.setCellValue(record.getR14_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
			// row15------>          a) Agriculture, Forestry, Fishing(NEW 14)
			row = sheet.getRow(13);

			// row15
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR15_orig_amt() != null) {
				cellB.setCellValue(record.getR15_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row15
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR15_fore_amt() != null) {
				cellC.setCellValue(record.getR15_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row15
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR15_no_of_acc() != null) {
				cellE.setCellValue(record.getR15_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row16------->         b) Mining and Quarying(NEW 15)
			row = sheet.getRow(14);

			// row16
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR16_orig_amt() != null) {
				cellB.setCellValue(record.getR16_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row16
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR16_fore_amt() != null) {
				cellC.setCellValue(record.getR16_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row16
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR16_no_of_acc() != null) {
				cellE.setCellValue(record.getR16_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row17-------->          c) Manufacturing(NEW 16)
			row = sheet.getRow(15);

			// row17
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR17_orig_amt() != null) {
				cellB.setCellValue(record.getR17_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row17
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR17_fore_amt() != null) {
				cellC.setCellValue(record.getR17_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row17
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR17_no_of_acc() != null) {
				cellE.setCellValue(record.getR17_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row18----->          d) Construction(NEW 17)
			row = sheet.getRow(16);

			// row18
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR18_orig_amt() != null) {
				cellB.setCellValue(record.getR18_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row18
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR18_fore_amt() != null) {
				cellC.setCellValue(record.getR18_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row18
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR18_no_of_acc() != null) {
				cellE.setCellValue(record.getR18_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row19----->         e) Commercial real estate(NEW 25)
			row = sheet.getRow(24);

			// row19
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR19_orig_amt() != null) {
				cellB.setCellValue(record.getR19_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row19
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR19_fore_amt() != null) {
				cellC.setCellValue(record.getR19_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row19
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR19_no_of_acc() != null) {
				cellE.setCellValue(record.getR19_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row20----->         f) Electricity(NEW 19)
			row = sheet.getRow(18);

			// row20
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR20_orig_amt() != null) {
				cellB.setCellValue(record.getR20_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row20
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR20_fore_amt() != null) {
				cellC.setCellValue(record.getR20_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row20
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR20_no_of_acc() != null) {
				cellE.setCellValue(record.getR20_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row21->         g) Water(NEW 20)
			row = sheet.getRow(19);

			// row21
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR21_orig_amt() != null) {
				cellB.setCellValue(record.getR21_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row21
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR21_fore_amt() != null) {
				cellC.setCellValue(record.getR21_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row21
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR21_no_of_acc() != null) {
				cellE.setCellValue(record.getR21_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row22---->          h) Telecommunication and post(NEW 21)
			row = sheet.getRow(20);

			// row22
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR22_orig_amt() != null) {
				cellB.setCellValue(record.getR22_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row22
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR22_fore_amt() != null) {
				cellC.setCellValue(record.getR22_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row22
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR22_no_of_acc() != null) {
				cellE.setCellValue(record.getR22_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row23--->         i) Tourism and hotels(NEW 22)
			row = sheet.getRow(21);

			// row23
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR23_orig_amt() != null) {
				cellB.setCellValue(record.getR23_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row23
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR23_fore_amt() != null) {
				cellC.setCellValue(record.getR23_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row23
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR23_no_of_acc() != null) {
				cellE.setCellValue(record.getR23_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row24------>          j) Transport and storage(NEW 23)
			row = sheet.getRow(22);

			// row24
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR24_orig_amt() != null) {
				cellB.setCellValue(record.getR24_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row24
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR24_fore_amt() != null) {
				cellC.setCellValue(record.getR24_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row24
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR24_no_of_acc() != null) {
				cellE.setCellValue(record.getR24_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row25-->         k) Trade, restaurants and bars(NEW 24)
			row = sheet.getRow(23);

			// row25
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR25_orig_amt() != null) {
				cellB.setCellValue(record.getR25_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row25
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR25_fore_amt() != null) {
				cellC.setCellValue(record.getR25_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row25
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR25_no_of_acc() != null) {
				cellE.setCellValue(record.getR25_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			


		


			// row28------->   (v)  Households (sum of lines (a) to (h)):  (NEW 26)

			row = sheet.getRow(25);

			// row28
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR28_orig_amt() != null) {
				cellB.setCellValue(record.getR28_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row28
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR28_fore_amt() != null) {
				cellC.setCellValue(record.getR28_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row28
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR28_no_of_acc() != null) {
				cellE.setCellValue(record.getR28_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row29--->        a) Residential property (owner occupied) (NEW 27)

			row = sheet.getRow(26);

			// row29
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR29_orig_amt() != null) {
				cellB.setCellValue(record.getR29_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row29
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR29_fore_amt() != null) {
				cellC.setCellValue(record.getR29_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row29
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR29_no_of_acc() != null) {
				cellE.setCellValue(record.getR29_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row30---->        b) Residential property (rented)--( NEW 28 b) Other property)
			row = sheet.getRow(27);

			// row30
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR30_orig_amt() != null) {
				cellB.setCellValue(record.getR30_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row30
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR30_fore_amt() != null) {
				cellC.setCellValue(record.getR30_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row30
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR30_no_of_acc() != null) {
				cellE.setCellValue(record.getR30_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


		


			// row32--------->         d) Motor vehicle(NEW 29)
			row = sheet.getRow(28);

			// row32
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR32_orig_amt() != null) {
				cellB.setCellValue(record.getR32_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row32
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR32_fore_amt() != null) {
				cellC.setCellValue(record.getR32_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row32
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR32_no_of_acc() != null) {
				cellE.setCellValue(record.getR32_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row33-->         e) Household goods(NEW 30)
			row = sheet.getRow(29);

			// row33
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR33_orig_amt() != null) {
				cellB.setCellValue(record.getR33_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row33
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR33_fore_amt() != null) {
				cellC.setCellValue(record.getR33_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row33
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR33_no_of_acc() != null) {
				cellE.setCellValue(record.getR33_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row34-------->         f) Credit card loans(NEW 31)
			row = sheet.getRow(30);

			// row34
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR34_orig_amt() != null) {
				cellB.setCellValue(record.getR34_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row34
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR34_fore_amt() != null) {
				cellC.setCellValue(record.getR34_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row34
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR34_no_of_acc() != null) {
				cellE.setCellValue(record.getR34_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row35--->        g) Non-Profit Institutions Serving Households(NEW 33)
			row = sheet.getRow(32);

			// row35
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR35_orig_amt() != null) {
				cellB.setCellValue(record.getR35_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row35
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR35_fore_amt() != null) {
				cellC.setCellValue(record.getR35_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row35
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR35_no_of_acc() != null) {
				cellE.setCellValue(record.getR35_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row36------------->        h)  Other specify(NEW 32)
			row = sheet.getRow(31);

			// row36
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR36_orig_amt() != null) {
				cellB.setCellValue(record.getR36_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row36
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR36_fore_amt() != null) {
				cellC.setCellValue(record.getR36_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row36
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR36_no_of_acc() != null) {
				cellE.setCellValue(record.getR36_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row37----->    (vi) Non-Residents (sum of lines (a) and (b)):(NEW 34)
			row = sheet.getRow(33);

			// row37
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR37_orig_amt() != null) {
				cellB.setCellValue(record.getR37_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row37
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR37_fore_amt() != null) {
				cellC.setCellValue(record.getR37_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row37
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR37_no_of_acc() != null) {
				cellE.setCellValue(record.getR37_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row38-->        a) Other Non-Financial Corporations(NEW 35)
			row = sheet.getRow(34);

			// row38
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR38_orig_amt() != null) {
				cellB.setCellValue(record.getR38_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row38
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR38_fore_amt() != null) {
				cellC.setCellValue(record.getR38_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row38
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR38_no_of_acc() != null) {
				cellE.setCellValue(record.getR38_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row39---------->        b) Households(NEW 36)
			row = sheet.getRow(35);

			// row39
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR39_orig_amt() != null) {
				cellB.setCellValue(record.getR39_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row39
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR39_fore_amt() != null) {
				cellC.setCellValue(record.getR39_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row39
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR39_no_of_acc() != null) {
				cellE.setCellValue(record.getR39_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row40------->2.  Financial institutional units (sum of lines (i) to (v)):(NEW 37)
			row = sheet.getRow(36);

			// row40
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR40_orig_amt() != null) {
				cellB.setCellValue(record.getR40_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row40
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR40_fore_amt() != null) {
				cellC.setCellValue(record.getR40_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row40
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR40_no_of_acc() != null) {
				cellE.setCellValue(record.getR40_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row41-->   (i)    Central Bank(NEW 38)
			row = sheet.getRow(37);

			// row41
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR41_orig_amt() != null) {
				cellB.setCellValue(record.getR41_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row41
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR41_fore_amt() != null) {
				cellC.setCellValue(record.getR41_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row41
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR41_no_of_acc() != null) {
				cellE.setCellValue(record.getR41_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row42---------->   (ii)   Commercial Banks(NEW 39)
			row = sheet.getRow(38);

			// row42
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR42_orig_amt() != null) {
				cellB.setCellValue(record.getR42_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row42
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR42_fore_amt() != null) {
				cellC.setCellValue(record.getR42_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row42
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR42_no_of_acc() != null) {
				cellE.setCellValue(record.getR42_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row43-->   (iii)  Other Depository Corporations (sum of lines (a) to (d)):(NEW 40)
			row = sheet.getRow(39);

			// row43
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR43_orig_amt() != null) {
				cellB.setCellValue(record.getR43_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row43
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR43_fore_amt() != null) {
				cellC.setCellValue(record.getR43_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row43
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR43_no_of_acc() != null) {
				cellE.setCellValue(record.getR43_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row44--->        a) Botswana Savings Bank (BSB)(NEW 41)
			row = sheet.getRow(40);

			// row44
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR44_orig_amt() != null) {
				cellB.setCellValue(record.getR44_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row44
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR44_fore_amt() != null) {
				cellC.setCellValue(record.getR44_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row44
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR44_no_of_acc() != null) {
				cellE.setCellValue(record.getR44_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row45-->        b) Botswana Building Society (BBS)(NEW 42)
			row = sheet.getRow(41);

			// row45
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR45_orig_amt() != null) {
				cellB.setCellValue(record.getR45_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row45
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR45_fore_amt() != null) {
				cellC.setCellValue(record.getR45_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row45
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR45_no_of_acc() != null) {
				cellE.setCellValue(record.getR45_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}



			
			
			
			// row51------------>        c) SACCOs(NEW 43)
			row = sheet.getRow(42);

			// row51
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR51_orig_amt() != null) {
				cellB.setCellValue(record.getR51_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row51
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR51_fore_amt() != null) {
				cellC.setCellValue(record.getR51_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row51
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR51_no_of_acc() != null) {
				cellE.setCellValue(record.getR51_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}



			// row47-------->        d) Other (specify)*(NEW 44)
			row = sheet.getRow(43);

			// row47
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR47_orig_amt() != null) {
				cellB.setCellValue(record.getR47_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row47
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR47_fore_amt() != null) {
				cellC.setCellValue(record.getR47_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row47
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR47_no_of_acc() != null) {
				cellE.setCellValue(record.getR47_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row48-->    (iv)  Other Financial Corporations (sum of lines (a) to (e)):(NEW 45)
			row = sheet.getRow(44);

			// row48
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR48_orig_amt() != null) {
				cellB.setCellValue(record.getR48_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row48
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR48_fore_amt() != null) {
				cellC.setCellValue(record.getR48_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row48
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR48_no_of_acc() != null) {
				cellE.setCellValue(record.getR48_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row49--->        a) Insurance Companies(NEW 46)
			row = sheet.getRow(45);

			// row49
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR49_orig_amt() != null) {
				cellB.setCellValue(record.getR49_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row49
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR49_fore_amt() != null) {
				cellC.setCellValue(record.getR49_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row49
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR49_no_of_acc() != null) {
				cellE.setCellValue(record.getR49_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row50----->        b) Pension Funds(NEW 47 )
			row = sheet.getRow(46);

			// row50
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR50_orig_amt() != null) {
				cellB.setCellValue(record.getR50_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row50
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR50_fore_amt() != null) {
				cellC.setCellValue(record.getR50_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row50
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR50_no_of_acc() != null) {
				cellE.setCellValue(record.getR50_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			


			// row52----->        c) Other Financial Intermediaries (sum 1 to 4)(NEW 48)
			row = sheet.getRow(47);

			// row52
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR52_orig_amt() != null) {
				cellB.setCellValue(record.getR52_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row52
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR52_fore_amt() != null) {
				cellC.setCellValue(record.getR52_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row52
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR52_no_of_acc() != null) {
				cellE.setCellValue(record.getR52_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}
			
			
	// row57   --------- 1.Asset managers(49 NEW)

			row = sheet.getRow(48);

			// row57
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR57_orig_amt() != null) {
				cellB.setCellValue(record.getR57_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row57
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR57_fore_amt() != null) {
				cellC.setCellValue(record.getR57_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row57
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR57_no_of_acc() != null) {
				cellE.setCellValue(record.getR57_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}
			
			
			

			// row53-->             2.Finance companies
			row = sheet.getRow(49);

			// row53
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR53_orig_amt() != null) {
				cellB.setCellValue(record.getR53_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row53
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR53_fore_amt() != null) {
				cellC.setCellValue(record.getR53_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row53
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR53_no_of_acc() != null) {
				cellE.setCellValue(record.getR53_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row54--->  3.Medical Aid Schemes

			row = sheet.getRow(50);

			// row54
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR54_orig_amt() != null) {
				cellB.setCellValue(record.getR54_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row54
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR54_fore_amt() != null) {
				cellC.setCellValue(record.getR54_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row54
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR54_no_of_acc() != null) {
				cellE.setCellValue(record.getR54_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row55--->  4.Public sector financial intermediaries
			row = sheet.getRow(51);

			// row55
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR55_orig_amt() != null) {
				cellB.setCellValue(record.getR55_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row55
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR55_fore_amt() != null) {
				cellC.setCellValue(record.getR55_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row55
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR55_no_of_acc() != null) {
				cellE.setCellValue(record.getR55_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row56--->        e) Financial Auxiliaries (sum 1 to 5)
			row = sheet.getRow(52);

			// row56
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR56_orig_amt() != null) {
				cellB.setCellValue(record.getR56_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row56
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR56_fore_amt() != null) {
				cellC.setCellValue(record.getR56_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row56
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR56_no_of_acc() != null) {
				cellE.setCellValue(record.getR56_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			

			// row58
			row = sheet.getRow(53);

			// row58
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR58_orig_amt() != null) {
				cellB.setCellValue(record.getR58_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row58
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR58_fore_amt() != null) {
				cellC.setCellValue(record.getR58_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row58
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR58_no_of_acc() != null) {
				cellE.setCellValue(record.getR58_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row59
			row = sheet.getRow(54);

			// row59
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR59_orig_amt() != null) {
				cellB.setCellValue(record.getR59_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row59
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR59_fore_amt() != null) {
				cellC.setCellValue(record.getR59_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row59
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR59_no_of_acc() != null) {
				cellE.setCellValue(record.getR59_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row60
			row = sheet.getRow(55);

			// row60
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR60_orig_amt() != null) {
				cellB.setCellValue(record.getR60_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row60
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR60_fore_amt() != null) {
				cellC.setCellValue(record.getR60_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row60
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR60_no_of_acc() != null) {
				cellE.setCellValue(record.getR60_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row61
			row = sheet.getRow(56);

			// row61
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR61_orig_amt() != null) {
				cellB.setCellValue(record.getR61_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row61
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR61_fore_amt() != null) {
				cellC.setCellValue(record.getR61_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row61
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR61_no_of_acc() != null) {
				cellE.setCellValue(record.getR61_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row62
			row = sheet.getRow(57);

			// row62
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR62_orig_amt() != null) {
				cellB.setCellValue(record.getR62_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row62
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR62_fore_amt() != null) {
				cellC.setCellValue(record.getR62_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row62
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR62_no_of_acc() != null) {
				cellE.setCellValue(record.getR62_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row63
			row = sheet.getRow(58);

			// row63
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR63_orig_amt() != null) {
				cellB.setCellValue(record.getR63_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row63
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR63_fore_amt() != null) {
				cellC.setCellValue(record.getR63_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row63
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR63_no_of_acc() != null) {
				cellE.setCellValue(record.getR63_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}

	//=====================================================================================
			// row64------>         e) Real Estate/Property Development(NEW 18)
			row = sheet.getRow(17);

			// row64
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR64_orig_amt() != null) {
				cellB.setCellValue(record.getR64_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row64
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR64_fore_amt() != null) {
				cellC.setCellValue(record.getR64_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row64
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR64_no_of_acc() != null) {
				cellE.setCellValue(record.getR64_no_of_acc().doubleValue());
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
					
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_RLFA2 EMAIL ARCHIVAL SUMMARY", null, "BRRS_Q_RLFA2_ARCHIVALTABLE_SUMMARY");
					}

					return out.toByteArray();
				}
			}
	
//=====================================================
// RESUB EXCEL  FORMAT
//=====================================================

		// Resub Format excel
			public byte[] BRRS_Q_RLFA2ResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_Q_RLFA2EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			}

				List<Q_RLFA2_RESUB_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for Q_RLFA2 report. Returning empty result.");
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

							Q_RLFA2_RESUB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
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
						
						
						
						//ROW 11
						row = sheet.getRow(10);

							// row11
							// Column B2 - Original Amount 
							Cell cellB = row.createCell(1);
							if (record.getR11_orig_amt() != null) {
								cellB.setCellValue(record.getR11_orig_amt().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row11
						    // Column C3 - Foreclosure Amount
							Cell cellC = row.createCell(2);
							if (record.getR11_fore_amt() != null) {
								cellC.setCellValue(record.getR11_fore_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
				            // Column D4 - No. of Accounts
							Cell cellE = row.createCell(3);
							if (record.getR11_no_of_acc() != null) {
								cellE.setCellValue(record.getR11_no_of_acc().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(11);
							
							// row12
							// Column B2 - Original Amount 
							 cellB = row.createCell(1);
							if (record.getR12_orig_amt() != null) {
							    cellB.setCellValue(record.getR12_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row12
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR12_fore_amt() != null) {
							    cellC.setCellValue(record.getR12_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row12
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR12_no_of_acc() != null) {
							    cellE.setCellValue(record.getR12_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							

							// row13
							row = sheet.getRow(12);

							// row13
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR13_orig_amt() != null) {
							    cellB.setCellValue(record.getR13_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row13
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR13_fore_amt() != null) {
							    cellC.setCellValue(record.getR13_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row13
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR13_no_of_acc() != null) {
							    cellE.setCellValue(record.getR13_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// row15
							row = sheet.getRow(14);

							// row15
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR15_orig_amt() != null) {
							    cellB.setCellValue(record.getR15_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row15
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR15_fore_amt() != null) {
							    cellC.setCellValue(record.getR15_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row15
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR15_no_of_acc() != null) {
							    cellE.setCellValue(record.getR15_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row16
							row = sheet.getRow(15);

							// row16
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR16_orig_amt() != null) {
							    cellB.setCellValue(record.getR16_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row16
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR16_fore_amt() != null) {
							    cellC.setCellValue(record.getR16_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row16
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR16_no_of_acc() != null) {
							    cellE.setCellValue(record.getR16_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(16);

							// row17
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR17_orig_amt() != null) {
							    cellB.setCellValue(record.getR17_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row17
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR17_fore_amt() != null) {
							    cellC.setCellValue(record.getR17_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row17
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR17_no_of_acc() != null) {
							    cellE.setCellValue(record.getR17_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row18
							row = sheet.getRow(17);

							// row18
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR18_orig_amt() != null) {
							    cellB.setCellValue(record.getR18_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row18
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR18_fore_amt() != null) {
							    cellC.setCellValue(record.getR18_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row18
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR18_no_of_acc() != null) {
							    cellE.setCellValue(record.getR18_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(18);

							// row19
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR19_orig_amt() != null) {
							    cellB.setCellValue(record.getR19_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row19
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR19_fore_amt() != null) {
							    cellC.setCellValue(record.getR19_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row19
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR19_no_of_acc() != null) {
							    cellE.setCellValue(record.getR19_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row20
							row = sheet.getRow(19);

							// row20
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR20_orig_amt() != null) {
							    cellB.setCellValue(record.getR20_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row20
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR20_fore_amt() != null) {
							    cellC.setCellValue(record.getR20_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row20
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR20_no_of_acc() != null) {
							    cellE.setCellValue(record.getR20_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row21
							row = sheet.getRow(20);

							// row21
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR21_orig_amt() != null) {
							    cellB.setCellValue(record.getR21_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row21
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR21_fore_amt() != null) {
							    cellC.setCellValue(record.getR21_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row21
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR21_no_of_acc() != null) {
							    cellE.setCellValue(record.getR21_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row22
							row = sheet.getRow(21);

							// row22
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR22_orig_amt() != null) {
							    cellB.setCellValue(record.getR22_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row22
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR22_fore_amt() != null) {
							    cellC.setCellValue(record.getR22_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row22
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR22_no_of_acc() != null) {
							    cellE.setCellValue(record.getR22_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row23
							row = sheet.getRow(22);

							// row23
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR23_orig_amt() != null) {
							    cellB.setCellValue(record.getR23_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row23
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR23_fore_amt() != null) {
							    cellC.setCellValue(record.getR23_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row23
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR23_no_of_acc() != null) {
							    cellE.setCellValue(record.getR23_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(23);

							// row24
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR24_orig_amt() != null) {
							    cellB.setCellValue(record.getR24_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row24
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR24_fore_amt() != null) {
							    cellC.setCellValue(record.getR24_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row24
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR24_no_of_acc() != null) {
							    cellE.setCellValue(record.getR24_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(24);

							// row25
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR25_orig_amt() != null) {
							    cellB.setCellValue(record.getR25_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row25
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR25_fore_amt() != null) {
							    cellC.setCellValue(record.getR25_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row25
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR25_no_of_acc() != null) {
							    cellE.setCellValue(record.getR25_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row26
							row = sheet.getRow(25);

							// row26
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR26_orig_amt() != null) {
							    cellB.setCellValue(record.getR26_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row26
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR26_fore_amt() != null) {
							    cellC.setCellValue(record.getR26_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row26
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR26_no_of_acc() != null) {
							    cellE.setCellValue(record.getR26_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row27
							row = sheet.getRow(26);

							// row27
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR27_orig_amt() != null) {
							    cellB.setCellValue(record.getR27_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row27
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR27_fore_amt() != null) {
							    cellC.setCellValue(record.getR27_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row27
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR27_no_of_acc() != null) {
							    cellE.setCellValue(record.getR27_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row29
							row = sheet.getRow(28);

							// row29
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR29_orig_amt() != null) {
							    cellB.setCellValue(record.getR29_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row29
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR29_fore_amt() != null) {
							    cellC.setCellValue(record.getR29_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row29
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR29_no_of_acc() != null) {
							    cellE.setCellValue(record.getR29_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row30
							row = sheet.getRow(29);

							// row30
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR30_orig_amt() != null) {
							    cellB.setCellValue(record.getR30_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row30
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR30_fore_amt() != null) {
							    cellC.setCellValue(record.getR30_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row30
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR30_no_of_acc() != null) {
							    cellE.setCellValue(record.getR30_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row31
							row = sheet.getRow(30);

							// row31
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR31_orig_amt() != null) {
							    cellB.setCellValue(record.getR31_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row31
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR31_fore_amt() != null) {
							    cellC.setCellValue(record.getR31_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row31
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR31_no_of_acc() != null) {
							    cellE.setCellValue(record.getR31_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row32
							row = sheet.getRow(31);

							// row32
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR32_orig_amt() != null) {
							    cellB.setCellValue(record.getR32_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row32
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR32_fore_amt() != null) {
							    cellC.setCellValue(record.getR32_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row32
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR32_no_of_acc() != null) {
							    cellE.setCellValue(record.getR32_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row33
							row = sheet.getRow(32);

							// row33
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR33_orig_amt() != null) {
							    cellB.setCellValue(record.getR33_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row33
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR33_fore_amt() != null) {
							    cellC.setCellValue(record.getR33_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row33
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR33_no_of_acc() != null) {
							    cellE.setCellValue(record.getR33_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row34
							row = sheet.getRow(33);

							// row34
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR34_orig_amt() != null) {
							    cellB.setCellValue(record.getR34_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row34
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR34_fore_amt() != null) {
							    cellC.setCellValue(record.getR34_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row34
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR34_no_of_acc() != null) {
							    cellE.setCellValue(record.getR34_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row35
							row = sheet.getRow(34);

							// row35
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR35_orig_amt() != null) {
							    cellB.setCellValue(record.getR35_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row35
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR35_fore_amt() != null) {
							    cellC.setCellValue(record.getR35_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row35
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR35_no_of_acc() != null) {
							    cellE.setCellValue(record.getR35_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row36
							row = sheet.getRow(35);

							// row36
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR36_orig_amt() != null) {
							    cellB.setCellValue(record.getR36_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row36
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR36_fore_amt() != null) {
							    cellC.setCellValue(record.getR36_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row36
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR36_no_of_acc() != null) {
							    cellE.setCellValue(record.getR36_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row38
							row = sheet.getRow(37);

							// row38
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR38_orig_amt() != null) {
							    cellB.setCellValue(record.getR38_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row38
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR38_fore_amt() != null) {
							    cellC.setCellValue(record.getR38_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row38
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR38_no_of_acc() != null) {
							    cellE.setCellValue(record.getR38_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row39
							row = sheet.getRow(38);

							// row39
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR39_orig_amt() != null) {
							    cellB.setCellValue(record.getR39_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row39
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR39_fore_amt() != null) {
							    cellC.setCellValue(record.getR39_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row39
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR39_no_of_acc() != null) {
							    cellE.setCellValue(record.getR39_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row41
							row = sheet.getRow(40);

							// row41
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR41_orig_amt() != null) {
							    cellB.setCellValue(record.getR41_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row41
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR41_fore_amt() != null) {
							    cellC.setCellValue(record.getR41_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row41
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR41_no_of_acc() != null) {
							    cellE.setCellValue(record.getR41_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row42
							row = sheet.getRow(41);

							// row42
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR42_orig_amt() != null) {
							    cellB.setCellValue(record.getR42_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row42
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR42_fore_amt() != null) {
							    cellC.setCellValue(record.getR42_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row42
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR42_no_of_acc() != null) {
							    cellE.setCellValue(record.getR42_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							// row44
							row = sheet.getRow(43);

							// row44
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR44_orig_amt() != null) {
							    cellB.setCellValue(record.getR44_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row44
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR44_fore_amt() != null) {
							    cellC.setCellValue(record.getR44_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row44
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR44_no_of_acc() != null) {
							    cellE.setCellValue(record.getR44_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row45
							row = sheet.getRow(44);

							// row45
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR45_orig_amt() != null) {
							    cellB.setCellValue(record.getR45_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row45
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR45_fore_amt() != null) {
							    cellC.setCellValue(record.getR45_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row45
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR45_no_of_acc() != null) {
							    cellE.setCellValue(record.getR45_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row46
							row = sheet.getRow(45);

							// row46
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR46_orig_amt() != null) {
							    cellB.setCellValue(record.getR46_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row46
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR46_fore_amt() != null) {
							    cellC.setCellValue(record.getR46_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row46
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR46_no_of_acc() != null) {
							    cellE.setCellValue(record.getR46_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row47
							row = sheet.getRow(46);

							// row47
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR47_orig_amt() != null) {
							    cellB.setCellValue(record.getR47_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row47
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR47_fore_amt() != null) {
							    cellC.setCellValue(record.getR47_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row47
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR47_no_of_acc() != null) {
							    cellE.setCellValue(record.getR47_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row49
							row = sheet.getRow(48);

							// row49
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR49_orig_amt() != null) {
							    cellB.setCellValue(record.getR49_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row49
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR49_fore_amt() != null) {
							    cellC.setCellValue(record.getR49_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row49
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR49_no_of_acc() != null) {
							    cellE.setCellValue(record.getR49_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row50
							row = sheet.getRow(49);

							// row50
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR50_orig_amt() != null) {
							    cellB.setCellValue(record.getR50_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row50
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR50_fore_amt() != null) {
							    cellC.setCellValue(record.getR50_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row50
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR50_no_of_acc() != null) {
							    cellE.setCellValue(record.getR50_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row51
							row = sheet.getRow(50);

							// row51
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR51_orig_amt() != null) {
							    cellB.setCellValue(record.getR51_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row51
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR51_fore_amt() != null) {
							    cellC.setCellValue(record.getR51_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row51
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR51_no_of_acc() != null) {
							    cellE.setCellValue(record.getR51_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							// row53
							row = sheet.getRow(52);

							// row53
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR53_orig_amt() != null) {
							    cellB.setCellValue(record.getR53_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row53
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR53_fore_amt() != null) {
							    cellC.setCellValue(record.getR53_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row53
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR53_no_of_acc() != null) {
							    cellE.setCellValue(record.getR53_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row54
							row = sheet.getRow(53);

							// row54
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR54_orig_amt() != null) {
							    cellB.setCellValue(record.getR54_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row54
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR54_fore_amt() != null) {
							    cellC.setCellValue(record.getR54_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row54
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR54_no_of_acc() != null) {
							    cellE.setCellValue(record.getR54_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row55
							row = sheet.getRow(54);

							// row55
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR55_orig_amt() != null) {
							    cellB.setCellValue(record.getR55_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row55
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR55_fore_amt() != null) {
							    cellC.setCellValue(record.getR55_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row55
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR55_no_of_acc() != null) {
							    cellE.setCellValue(record.getR55_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// row57
							row = sheet.getRow(56);

							// row57
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR57_orig_amt() != null) {
							    cellB.setCellValue(record.getR57_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row57
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR57_fore_amt() != null) {
							    cellC.setCellValue(record.getR57_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row57
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR57_no_of_acc() != null) {
							    cellE.setCellValue(record.getR57_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row58
							row = sheet.getRow(57);

							// row58
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR58_orig_amt() != null) {
							    cellB.setCellValue(record.getR58_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row58
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR58_fore_amt() != null) {
							    cellC.setCellValue(record.getR58_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row58
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR58_no_of_acc() != null) {
							    cellE.setCellValue(record.getR58_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row59
							row = sheet.getRow(58);

							// row59
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR59_orig_amt() != null) {
							    cellB.setCellValue(record.getR59_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row59
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR59_fore_amt() != null) {
							    cellC.setCellValue(record.getR59_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row59
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR59_no_of_acc() != null) {
							    cellE.setCellValue(record.getR59_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row60
							row = sheet.getRow(59);

							// row60
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR60_orig_amt() != null) {
							    cellB.setCellValue(record.getR60_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row60
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR60_fore_amt() != null) {
							    cellC.setCellValue(record.getR60_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row60
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR60_no_of_acc() != null) {
							    cellE.setCellValue(record.getR60_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row61
							row = sheet.getRow(60);

							// row61
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR61_orig_amt() != null) {
							    cellB.setCellValue(record.getR61_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row61
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR61_fore_amt() != null) {
							    cellC.setCellValue(record.getR61_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row61
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR61_no_of_acc() != null) {
							    cellE.setCellValue(record.getR61_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row62
							row = sheet.getRow(61);

							// row62
							// Column B2 - Original Amount 
							cellB = row.createCell(1);
							if (record.getR62_orig_amt() != null) {
							    cellB.setCellValue(record.getR62_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row62
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(2);
							if (record.getR62_fore_amt() != null) {
							    cellC.setCellValue(record.getR62_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row62
							// Column D4 - No. of Accounts
							cellE = row.createCell(3);
							if (record.getR62_no_of_acc() != null) {
							    cellE.setCellValue(record.getR62_no_of_acc().doubleValue());
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
					
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_RLFA2 RESUB SUMMARY", null, "BRRS_Q_RLFA2_RESUB_SUMMARYTABLE");
					}

					return out.toByteArray();
				}

			}

	
//=====================================================
// RESUB  EXCEL EMAIL
//=====================================================

	
			// Resub Email Excel
			public byte[] BRRS_Q_RLFA2EmailResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting RESUB Email Excel generation process in memory.");

				List<Q_RLFA2_RESUB_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_Q_RLFA2 report. Returning empty result.");
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
							Q_RLFA2_RESUB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
						
							//row7
							// Column B
							Cell cellBdate = row.createCell(3);
							if (record.getReport_date() != null) {
								cellBdate.setCellValue(record.getReport_date());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}
							
							
							
							//ROW 9
							row = sheet.getRow(8);

							// row10
							// Column B2 - Original Amount 
							Cell cellB = row.createCell(3);
							if (record.getR10_orig_amt() != null) {
								cellB.setCellValue(record.getR10_orig_amt().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row10
						    // Column C3 - Foreclosure Amount
							Cell cellC = row.createCell(4);
							if (record.getR10_fore_amt() != null) {
								cellC.setCellValue(record.getR10_fore_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row10
				            // Column D4 - No. of Accounts
							Cell cellE = row.createCell(5);
							if (record.getR10_no_of_acc() != null) {
								cellE.setCellValue(record.getR10_no_of_acc().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							
							
							// row11
							
							row = sheet.getRow(9);
							
							// Column B2 - Original Amount 
							 cellB = row.createCell(3);
							if (record.getR11_orig_amt() != null) {
								cellB.setCellValue(record.getR11_orig_amt().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row11
						    // Column C3 - Foreclosure Amount
							 cellC = row.createCell(4);
							if (record.getR11_fore_amt() != null) {
								cellC.setCellValue(record.getR11_fore_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
				            // Column D4 - No. of Accounts
							 cellE = row.createCell(5);
							if (record.getR11_no_of_acc() != null) {
								cellE.setCellValue(record.getR11_no_of_acc().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							
							
							// row12
							row = sheet.getRow(10);
							
							// row12
							// Column B2 - Original Amount 
							 cellB = row.createCell(3);
							if (record.getR12_orig_amt() != null) {
							    cellB.setCellValue(record.getR12_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row12
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(4);
							if (record.getR12_fore_amt() != null) {
							    cellC.setCellValue(record.getR12_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row12
							// Column D4 - No. of Accounts
							cellE = row.createCell(5);
							if (record.getR12_no_of_acc() != null) {
							    cellE.setCellValue(record.getR12_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
							
							// row13
							row = sheet.getRow(11);

							// row13
							// Column B2 - Original Amount 
							cellB = row.createCell(3);
							if (record.getR13_orig_amt() != null) {
							    cellB.setCellValue(record.getR13_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row13
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(4);
							if (record.getR13_fore_amt() != null) {
							    cellC.setCellValue(record.getR13_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row13
							// Column D4 - No. of Accounts
							cellE = row.createCell(5);
							if (record.getR13_no_of_acc() != null) {
							    cellE.setCellValue(record.getR13_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							
										// row14
							row = sheet.getRow(12);

							// row14
							// Column B2 - Original Amount 
							cellB = row.createCell(3);
							if (record.getR14_orig_amt() != null) {
							    cellB.setCellValue(record.getR14_orig_amt().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row14
							// Column C3 - Foreclosure Amount
							cellC = row.createCell(4);
							if (record.getR14_fore_amt() != null) {
							    cellC.setCellValue(record.getR14_fore_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row14
							// Column D4 - No. of Accounts
							cellE = row.createCell(5);
							if (record.getR14_no_of_acc() != null) {
							    cellE.setCellValue(record.getR14_no_of_acc().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
			// row15------>          a) Agriculture, Forestry, Fishing(NEW 14)
			row = sheet.getRow(13);

			// row15
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR15_orig_amt() != null) {
				cellB.setCellValue(record.getR15_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row15
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR15_fore_amt() != null) {
				cellC.setCellValue(record.getR15_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row15
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR15_no_of_acc() != null) {
				cellE.setCellValue(record.getR15_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row16------->         b) Mining and Quarying(NEW 15)
			row = sheet.getRow(14);

			// row16
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR16_orig_amt() != null) {
				cellB.setCellValue(record.getR16_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row16
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR16_fore_amt() != null) {
				cellC.setCellValue(record.getR16_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row16
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR16_no_of_acc() != null) {
				cellE.setCellValue(record.getR16_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row17-------->          c) Manufacturing(NEW 16)
			row = sheet.getRow(15);

			// row17
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR17_orig_amt() != null) {
				cellB.setCellValue(record.getR17_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row17
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR17_fore_amt() != null) {
				cellC.setCellValue(record.getR17_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row17
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR17_no_of_acc() != null) {
				cellE.setCellValue(record.getR17_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row18----->          d) Construction(NEW 17)
			row = sheet.getRow(16);

			// row18
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR18_orig_amt() != null) {
				cellB.setCellValue(record.getR18_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row18
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR18_fore_amt() != null) {
				cellC.setCellValue(record.getR18_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row18
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR18_no_of_acc() != null) {
				cellE.setCellValue(record.getR18_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row19----->         e) Commercial real estate(NEW 25)
			row = sheet.getRow(24);

			// row19
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR19_orig_amt() != null) {
				cellB.setCellValue(record.getR19_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row19
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR19_fore_amt() != null) {
				cellC.setCellValue(record.getR19_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row19
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR19_no_of_acc() != null) {
				cellE.setCellValue(record.getR19_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row20----->         f) Electricity(NEW 19)
			row = sheet.getRow(18);

			// row20
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR20_orig_amt() != null) {
				cellB.setCellValue(record.getR20_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row20
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR20_fore_amt() != null) {
				cellC.setCellValue(record.getR20_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row20
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR20_no_of_acc() != null) {
				cellE.setCellValue(record.getR20_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row21->         g) Water(NEW 20)
			row = sheet.getRow(19);

			// row21
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR21_orig_amt() != null) {
				cellB.setCellValue(record.getR21_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row21
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR21_fore_amt() != null) {
				cellC.setCellValue(record.getR21_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row21
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR21_no_of_acc() != null) {
				cellE.setCellValue(record.getR21_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row22---->          h) Telecommunication and post(NEW 21)
			row = sheet.getRow(20);

			// row22
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR22_orig_amt() != null) {
				cellB.setCellValue(record.getR22_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row22
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR22_fore_amt() != null) {
				cellC.setCellValue(record.getR22_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row22
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR22_no_of_acc() != null) {
				cellE.setCellValue(record.getR22_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row23--->         i) Tourism and hotels(NEW 22)
			row = sheet.getRow(21);

			// row23
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR23_orig_amt() != null) {
				cellB.setCellValue(record.getR23_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row23
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR23_fore_amt() != null) {
				cellC.setCellValue(record.getR23_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row23
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR23_no_of_acc() != null) {
				cellE.setCellValue(record.getR23_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row24------>          j) Transport and storage(NEW 23)
			row = sheet.getRow(22);

			// row24
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR24_orig_amt() != null) {
				cellB.setCellValue(record.getR24_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row24
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR24_fore_amt() != null) {
				cellC.setCellValue(record.getR24_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row24
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR24_no_of_acc() != null) {
				cellE.setCellValue(record.getR24_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row25-->         k) Trade, restaurants and bars(NEW 24)
			row = sheet.getRow(23);

			// row25
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR25_orig_amt() != null) {
				cellB.setCellValue(record.getR25_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row25
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR25_fore_amt() != null) {
				cellC.setCellValue(record.getR25_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row25
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR25_no_of_acc() != null) {
				cellE.setCellValue(record.getR25_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			


		


			// row28------->   (v)  Households (sum of lines (a) to (h)):  (NEW 26)

			row = sheet.getRow(25);

			// row28
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR28_orig_amt() != null) {
				cellB.setCellValue(record.getR28_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row28
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR28_fore_amt() != null) {
				cellC.setCellValue(record.getR28_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row28
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR28_no_of_acc() != null) {
				cellE.setCellValue(record.getR28_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row29--->        a) Residential property (owner occupied) (NEW 27)

			row = sheet.getRow(26);

			// row29
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR29_orig_amt() != null) {
				cellB.setCellValue(record.getR29_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row29
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR29_fore_amt() != null) {
				cellC.setCellValue(record.getR29_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row29
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR29_no_of_acc() != null) {
				cellE.setCellValue(record.getR29_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row30---->        b) Residential property (rented)--( NEW 28 b) Other property)
			row = sheet.getRow(27);

			// row30
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR30_orig_amt() != null) {
				cellB.setCellValue(record.getR30_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row30
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR30_fore_amt() != null) {
				cellC.setCellValue(record.getR30_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row30
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR30_no_of_acc() != null) {
				cellE.setCellValue(record.getR30_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


		


			// row32--------->         d) Motor vehicle(NEW 29)
			row = sheet.getRow(28);

			// row32
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR32_orig_amt() != null) {
				cellB.setCellValue(record.getR32_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row32
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR32_fore_amt() != null) {
				cellC.setCellValue(record.getR32_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row32
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR32_no_of_acc() != null) {
				cellE.setCellValue(record.getR32_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row33-->         e) Household goods(NEW 30)
			row = sheet.getRow(29);

			// row33
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR33_orig_amt() != null) {
				cellB.setCellValue(record.getR33_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row33
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR33_fore_amt() != null) {
				cellC.setCellValue(record.getR33_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row33
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR33_no_of_acc() != null) {
				cellE.setCellValue(record.getR33_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row34-------->         f) Credit card loans(NEW 31)
			row = sheet.getRow(30);

			// row34
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR34_orig_amt() != null) {
				cellB.setCellValue(record.getR34_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row34
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR34_fore_amt() != null) {
				cellC.setCellValue(record.getR34_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row34
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR34_no_of_acc() != null) {
				cellE.setCellValue(record.getR34_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row35--->        g) Non-Profit Institutions Serving Households(NEW 33)
			row = sheet.getRow(32);

			// row35
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR35_orig_amt() != null) {
				cellB.setCellValue(record.getR35_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row35
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR35_fore_amt() != null) {
				cellC.setCellValue(record.getR35_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row35
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR35_no_of_acc() != null) {
				cellE.setCellValue(record.getR35_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row36------------->        h)  Other specify(NEW 32)
			row = sheet.getRow(31);

			// row36
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR36_orig_amt() != null) {
				cellB.setCellValue(record.getR36_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row36
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR36_fore_amt() != null) {
				cellC.setCellValue(record.getR36_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row36
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR36_no_of_acc() != null) {
				cellE.setCellValue(record.getR36_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row37----->    (vi) Non-Residents (sum of lines (a) and (b)):(NEW 34)
			row = sheet.getRow(33);

			// row37
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR37_orig_amt() != null) {
				cellB.setCellValue(record.getR37_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row37
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR37_fore_amt() != null) {
				cellC.setCellValue(record.getR37_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row37
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR37_no_of_acc() != null) {
				cellE.setCellValue(record.getR37_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row38-->        a) Other Non-Financial Corporations(NEW 35)
			row = sheet.getRow(34);

			// row38
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR38_orig_amt() != null) {
				cellB.setCellValue(record.getR38_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row38
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR38_fore_amt() != null) {
				cellC.setCellValue(record.getR38_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row38
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR38_no_of_acc() != null) {
				cellE.setCellValue(record.getR38_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row39---------->        b) Households(NEW 36)
			row = sheet.getRow(35);

			// row39
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR39_orig_amt() != null) {
				cellB.setCellValue(record.getR39_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row39
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR39_fore_amt() != null) {
				cellC.setCellValue(record.getR39_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row39
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR39_no_of_acc() != null) {
				cellE.setCellValue(record.getR39_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row40------->2.  Financial institutional units (sum of lines (i) to (v)):(NEW 37)
			row = sheet.getRow(36);

			// row40
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR40_orig_amt() != null) {
				cellB.setCellValue(record.getR40_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row40
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR40_fore_amt() != null) {
				cellC.setCellValue(record.getR40_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row40
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR40_no_of_acc() != null) {
				cellE.setCellValue(record.getR40_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row41-->   (i)    Central Bank(NEW 38)
			row = sheet.getRow(37);

			// row41
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR41_orig_amt() != null) {
				cellB.setCellValue(record.getR41_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row41
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR41_fore_amt() != null) {
				cellC.setCellValue(record.getR41_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row41
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR41_no_of_acc() != null) {
				cellE.setCellValue(record.getR41_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row42---------->   (ii)   Commercial Banks(NEW 39)
			row = sheet.getRow(38);

			// row42
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR42_orig_amt() != null) {
				cellB.setCellValue(record.getR42_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row42
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR42_fore_amt() != null) {
				cellC.setCellValue(record.getR42_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row42
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR42_no_of_acc() != null) {
				cellE.setCellValue(record.getR42_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row43-->   (iii)  Other Depository Corporations (sum of lines (a) to (d)):(NEW 40)
			row = sheet.getRow(39);

			// row43
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR43_orig_amt() != null) {
				cellB.setCellValue(record.getR43_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row43
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR43_fore_amt() != null) {
				cellC.setCellValue(record.getR43_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row43
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR43_no_of_acc() != null) {
				cellE.setCellValue(record.getR43_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row44--->        a) Botswana Savings Bank (BSB)(NEW 41)
			row = sheet.getRow(40);

			// row44
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR44_orig_amt() != null) {
				cellB.setCellValue(record.getR44_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row44
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR44_fore_amt() != null) {
				cellC.setCellValue(record.getR44_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row44
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR44_no_of_acc() != null) {
				cellE.setCellValue(record.getR44_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row45-->        b) Botswana Building Society (BBS)(NEW 42)
			row = sheet.getRow(41);

			// row45
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR45_orig_amt() != null) {
				cellB.setCellValue(record.getR45_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row45
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR45_fore_amt() != null) {
				cellC.setCellValue(record.getR45_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row45
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR45_no_of_acc() != null) {
				cellE.setCellValue(record.getR45_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}



			
			
			
			// row51------------>        c) SACCOs(NEW 43)
			row = sheet.getRow(42);

			// row51
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR51_orig_amt() != null) {
				cellB.setCellValue(record.getR51_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row51
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR51_fore_amt() != null) {
				cellC.setCellValue(record.getR51_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row51
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR51_no_of_acc() != null) {
				cellE.setCellValue(record.getR51_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}



			// row47-------->        d) Other (specify)*(NEW 44)
			row = sheet.getRow(43);

			// row47
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR47_orig_amt() != null) {
				cellB.setCellValue(record.getR47_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row47
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR47_fore_amt() != null) {
				cellC.setCellValue(record.getR47_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row47
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR47_no_of_acc() != null) {
				cellE.setCellValue(record.getR47_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row48-->    (iv)  Other Financial Corporations (sum of lines (a) to (e)):(NEW 45)
			row = sheet.getRow(44);

			// row48
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR48_orig_amt() != null) {
				cellB.setCellValue(record.getR48_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row48
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR48_fore_amt() != null) {
				cellC.setCellValue(record.getR48_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row48
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR48_no_of_acc() != null) {
				cellE.setCellValue(record.getR48_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row49--->        a) Insurance Companies(NEW 46)
			row = sheet.getRow(45);

			// row49
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR49_orig_amt() != null) {
				cellB.setCellValue(record.getR49_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row49
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR49_fore_amt() != null) {
				cellC.setCellValue(record.getR49_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row49
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR49_no_of_acc() != null) {
				cellE.setCellValue(record.getR49_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row50----->        b) Pension Funds(NEW 47 )
			row = sheet.getRow(46);

			// row50
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR50_orig_amt() != null) {
				cellB.setCellValue(record.getR50_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row50
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR50_fore_amt() != null) {
				cellC.setCellValue(record.getR50_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row50
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR50_no_of_acc() != null) {
				cellE.setCellValue(record.getR50_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			


			// row52----->        c) Other Financial Intermediaries (sum 1 to 4)(NEW 48)
			row = sheet.getRow(47);

			// row52
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR52_orig_amt() != null) {
				cellB.setCellValue(record.getR52_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row52
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR52_fore_amt() != null) {
				cellC.setCellValue(record.getR52_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row52
			// Column D4 - No. of Accounts
			cellE = row.createCell(6);
			if (record.getR52_no_of_acc() != null) {
				cellE.setCellValue(record.getR52_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}
			
			
	// row57   --------- 1.Asset managers(49 NEW)

			row = sheet.getRow(48);

			// row57
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR57_orig_amt() != null) {
				cellB.setCellValue(record.getR57_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row57
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR57_fore_amt() != null) {
				cellC.setCellValue(record.getR57_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row57
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR57_no_of_acc() != null) {
				cellE.setCellValue(record.getR57_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}
			
			
			

			// row53-->             2.Finance companies
			row = sheet.getRow(49);

			// row53
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR53_orig_amt() != null) {
				cellB.setCellValue(record.getR53_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row53
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR53_fore_amt() != null) {
				cellC.setCellValue(record.getR53_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row53
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR53_no_of_acc() != null) {
				cellE.setCellValue(record.getR53_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row54--->  3.Medical Aid Schemes

			row = sheet.getRow(50);

			// row54
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR54_orig_amt() != null) {
				cellB.setCellValue(record.getR54_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row54
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR54_fore_amt() != null) {
				cellC.setCellValue(record.getR54_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row54
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR54_no_of_acc() != null) {
				cellE.setCellValue(record.getR54_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row55--->  4.Public sector financial intermediaries
			row = sheet.getRow(51);

			// row55
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR55_orig_amt() != null) {
				cellB.setCellValue(record.getR55_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row55
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR55_fore_amt() != null) {
				cellC.setCellValue(record.getR55_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row55
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR55_no_of_acc() != null) {
				cellE.setCellValue(record.getR55_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row56--->        e) Financial Auxiliaries (sum 1 to 5)
			row = sheet.getRow(52);

			// row56
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR56_orig_amt() != null) {
				cellB.setCellValue(record.getR56_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row56
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR56_fore_amt() != null) {
				cellC.setCellValue(record.getR56_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row56
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR56_no_of_acc() != null) {
				cellE.setCellValue(record.getR56_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			

			// row58
			row = sheet.getRow(53);

			// row58
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR58_orig_amt() != null) {
				cellB.setCellValue(record.getR58_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row58
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR58_fore_amt() != null) {
				cellC.setCellValue(record.getR58_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row58
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR58_no_of_acc() != null) {
				cellE.setCellValue(record.getR58_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row59
			row = sheet.getRow(54);

			// row59
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR59_orig_amt() != null) {
				cellB.setCellValue(record.getR59_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row59
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR59_fore_amt() != null) {
				cellC.setCellValue(record.getR59_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row59
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR59_no_of_acc() != null) {
				cellE.setCellValue(record.getR59_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row60
			row = sheet.getRow(55);

			// row60
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR60_orig_amt() != null) {
				cellB.setCellValue(record.getR60_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row60
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR60_fore_amt() != null) {
				cellC.setCellValue(record.getR60_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row60
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR60_no_of_acc() != null) {
				cellE.setCellValue(record.getR60_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row61
			row = sheet.getRow(56);

			// row61
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR61_orig_amt() != null) {
				cellB.setCellValue(record.getR61_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row61
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR61_fore_amt() != null) {
				cellC.setCellValue(record.getR61_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row61
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR61_no_of_acc() != null) {
				cellE.setCellValue(record.getR61_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row62
			row = sheet.getRow(57);

			// row62
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR62_orig_amt() != null) {
				cellB.setCellValue(record.getR62_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row62
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR62_fore_amt() != null) {
				cellC.setCellValue(record.getR62_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row62
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR62_no_of_acc() != null) {
				cellE.setCellValue(record.getR62_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}


			// row63
			row = sheet.getRow(58);

			// row63
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR63_orig_amt() != null) {
				cellB.setCellValue(record.getR63_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row63
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR63_fore_amt() != null) {
				cellC.setCellValue(record.getR63_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row63
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR63_no_of_acc() != null) {
				cellE.setCellValue(record.getR63_no_of_acc().doubleValue());
				cellE.setCellStyle(numberStyle);
			} else {
				cellE.setCellValue("");
				cellE.setCellStyle(textStyle);
			}

	//=====================================================================================
			// row64------>         e) Real Estate/Property Development(NEW 18)
			row = sheet.getRow(17);

			// row64
			// Column B2 - Original Amount
			cellB = row.createCell(3);
			if (record.getR64_orig_amt() != null) {
				cellB.setCellValue(record.getR64_orig_amt().doubleValue());
				cellB.setCellStyle(numberStyle);
			} else {
				cellB.setCellValue("");
				cellB.setCellStyle(textStyle);
			}

			// row64
			// Column C3 - Foreclosure Amount
			cellC = row.createCell(4);
			if (record.getR64_fore_amt() != null) {
				cellC.setCellValue(record.getR64_fore_amt().doubleValue());
				cellC.setCellStyle(numberStyle);
			} else {
				cellC.setCellValue("");
				cellC.setCellStyle(textStyle);
			}

			// row64
			// Column D4 - No. of Accounts
			cellE = row.createCell(5);
			if (record.getR64_no_of_acc() != null) {
				cellE.setCellValue(record.getR64_no_of_acc().doubleValue());
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
					
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_RLFA2 EMAIL RESUB SUMMARY", null, "BRRS_Q_RLFA2_RESUB_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
			}

		
	}
	
	