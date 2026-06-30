
//========================MANUAL REPORT 

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
import com.bornfire.brrs.services.BRRS_BASEL_III_COM_EQUITY_DISC_ReportService.BASEL_III_COM_EQUITY_DISC_Summary_Entity;
import com.bornfire.brrs.services.BRRS_BASEL_III_COM_EQUITY_DISC_ReportService.B_III_CETD_RowMapper;


@Service

public class BRRS_M_BOP_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_BOP_ReportService.class);
	
	
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


	public List<M_BOP_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_M_BOP_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new M_BOP_Summary_RowMapper()
    );
}
	
	//findbyreportdate

	public M_BOP_Summary_Entity findByReportDate(Date reportDate) {

	    String sql =
	            "SELECT * FROM BRRS_M_BOP_SUMMARYTABLE " +
	            "WHERE REPORT_DATE = ?";

	    List<M_BOP_Summary_Entity> list =
	            jdbcTemplate.query(
	                    sql,
	                    new Object[] { reportDate },
	                    new M_BOP_Summary_RowMapper()
	            );

	    return list.isEmpty() ? null : list.get(0);
	}
	
// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================



public List<Object[]> get_M_BOP_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_BOP_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<M_BOP_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_M_BOP_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new M_BOP_Archival_Summary_RowMapper()
    );
}

public List<M_BOP_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_M_BOP_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new M_BOP_Archival_Summary_RowMapper()
    );
}

public BigDecimal findMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_BOP_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            BigDecimal.class
    );
}

// =====================================================
// DETAIL REPO
// =====================================================	


public List<M_BOP_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_M_BOP_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new M_BOP_Detail_RowMapper()
    );
}



// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================


public List<Map<String, Object>> getM_BOP_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_BOP_ARCHIVALTABLE_DETAIL " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.queryForList(sql);
}


public List<M_BOP_Archival_Detail_Entity> getDetaildatabydateListarchival(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * " +
                 "FROM BRRS_M_BOP_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? " +
                 "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportDate, reportVersion },
            new M_BOP_Archival_Detail_RowMapper()
    );
}

public BigDecimal findDETAILMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) " +
                 "FROM BRRS_M_BOP_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[] { reportDate },
            BigDecimal.class
    );
}

public M_BOP_Archival_Detail_Entity getArchivalListWithVersion() {

    String sql = "SELECT * " +
                 "FROM BRRS_M_BOP_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC " +
                 "FETCH FIRST 1 ROWS ONLY";

    return jdbcTemplate.queryForObject(
            sql,
            new M_BOP_Archival_Detail_RowMapper()
    );
}

// =====================================================
// RESUB SUMMARY
// =====================================================

public List<M_BOP_RESUB_Summary_Entity> getResubSummarydatabydateListarchival(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * " +
                 "FROM BRRS_M_BOP_RESUB_SUMMARYTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new M_BOP_RESUB_Summary_RowMapper()
    );
}


public BigDecimal findResubSummaryMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) " +
                 "FROM BRRS_M_BOP_RESUB_SUMMARYTABLE " +
                 "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            BigDecimal.class
    );
}

public List<Map<String, Object>> getM_BOP_Archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_BOP_RESUB_SUMMARYTABLE " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.queryForList(sql);
}

public M_BOP_RESUB_Summary_Entity getResubSummarydatabydateListWithVersion() {

    String sql = "SELECT * " +
                 "FROM BRRS_M_BOP_RESUB_SUMMARYTABLE " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC " +
                 "FETCH FIRST 1 ROWS ONLY";

    return jdbcTemplate.queryForObject(
            sql,
            new M_BOP_RESUB_Summary_RowMapper()
    );
}



// =====================================================
// RESUB DETAIL
// =====================================================


public List<Map<String, Object>> get_M_BOPArchival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_BOP_RESUB_DETAILTABLE " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.queryForList(sql);
}

public List<M_BOP_RESUB_Detail_Entity> getResubDetaildatabydateList(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * " +
                 "FROM BRRS_M_BOP_RESUB_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportDate, reportVersion },
            new M_BOP_RESUB_Detail_RowMapper()
    );
}

public BigDecimal findResubDetailMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) " +
                 "FROM BRRS_M_BOP_RESUB_DETAILTABLE " +
                 "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[] { reportDate },
            BigDecimal.class
    );
}

public M_BOP_RESUB_Detail_Entity getdResubDetailDatabydateListWithVersion() {

    String sql = "SELECT * " +
                 "FROM BRRS_M_BOP_RESUB_DETAILTABLE " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC " +
                 "FETCH FIRST 1 ROWS ONLY";

    return jdbcTemplate.queryForObject(
            sql,
            new M_BOP_RESUB_Detail_RowMapper()
    );
}

// =====================================================
// SUMAMRY ENTITY & ROW MAPPER 
// =====================================================


public class M_BOP_Summary_RowMapper implements RowMapper<M_BOP_Summary_Entity> {

    @Override
    public M_BOP_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_BOP_Summary_Entity obj = new M_BOP_Summary_Entity();

// =========================
// R13
// =========================
obj.setR13_product(rs.getString("r13_product"));
obj.setR13_open_position(rs.getBigDecimal("r13_open_position"));
obj.setR13_cpdm_dt_inc(rs.getBigDecimal("r13_cpdm_dt_inc"));
obj.setR13_cpdm_dt_dec(rs.getBigDecimal("r13_cpdm_dt_dec"));
obj.setR13_net(rs.getBigDecimal("r13_net"));
obj.setR13_cpdm_dt_der(rs.getBigDecimal("r13_cpdm_dt_der"));
obj.setR13_cpdm_dt_dto(rs.getBigDecimal("r13_cpdm_dt_dto"));
obj.setR13_cp(rs.getBigDecimal("r13_cp"));


// =========================
// R14
// =========================
obj.setR14_product(rs.getString("r14_product"));
obj.setR14_open_position(rs.getBigDecimal("r14_open_position"));
obj.setR14_cpdm_dt_inc(rs.getBigDecimal("r14_cpdm_dt_inc"));
obj.setR14_cpdm_dt_dec(rs.getBigDecimal("r14_cpdm_dt_dec"));
obj.setR14_net(rs.getBigDecimal("r14_net"));
obj.setR14_cpdm_dt_der(rs.getBigDecimal("r14_cpdm_dt_der"));
obj.setR14_cpdm_dt_dto(rs.getBigDecimal("r14_cpdm_dt_dto"));
obj.setR14_cp(rs.getBigDecimal("r14_cp"));

// =========================
// R15
// =========================
obj.setR15_product(rs.getString("r15_product"));
obj.setR15_open_position(rs.getBigDecimal("r15_open_position"));
obj.setR15_cpdm_dt_inc(rs.getBigDecimal("r15_cpdm_dt_inc"));
obj.setR15_cpdm_dt_dec(rs.getBigDecimal("r15_cpdm_dt_dec"));
obj.setR15_net(rs.getBigDecimal("r15_net"));
obj.setR15_cpdm_dt_der(rs.getBigDecimal("r15_cpdm_dt_der"));
obj.setR15_cpdm_dt_dto(rs.getBigDecimal("r15_cpdm_dt_dto"));
obj.setR15_cp(rs.getBigDecimal("r15_cp"));

// =========================
// R16
// =========================
obj.setR16_product(rs.getString("r16_product"));
obj.setR16_open_position(rs.getBigDecimal("r16_open_position"));
obj.setR16_cpdm_dt_inc(rs.getBigDecimal("r16_cpdm_dt_inc"));
obj.setR16_cpdm_dt_dec(rs.getBigDecimal("r16_cpdm_dt_dec"));
obj.setR16_net(rs.getBigDecimal("r16_net"));
obj.setR16_cpdm_dt_der(rs.getBigDecimal("r16_cpdm_dt_der"));
obj.setR16_cpdm_dt_dto(rs.getBigDecimal("r16_cpdm_dt_dto"));
obj.setR16_cp(rs.getBigDecimal("r16_cp"));

// =========================
// R17
// =========================
obj.setR17_product(rs.getString("r17_product"));
obj.setR17_open_position(rs.getBigDecimal("r17_open_position"));
obj.setR17_cpdm_dt_inc(rs.getBigDecimal("r17_cpdm_dt_inc"));
obj.setR17_cpdm_dt_dec(rs.getBigDecimal("r17_cpdm_dt_dec"));
obj.setR17_net(rs.getBigDecimal("r17_net"));
obj.setR17_cpdm_dt_der(rs.getBigDecimal("r17_cpdm_dt_der"));
obj.setR17_cpdm_dt_dto(rs.getBigDecimal("r17_cpdm_dt_dto"));
obj.setR17_cp(rs.getBigDecimal("r17_cp"));

// =========================
// R18
// =========================
obj.setR18_product(rs.getString("r18_product"));
obj.setR18_open_position(rs.getBigDecimal("r18_open_position"));
obj.setR18_cpdm_dt_inc(rs.getBigDecimal("r18_cpdm_dt_inc"));
obj.setR18_cpdm_dt_dec(rs.getBigDecimal("r18_cpdm_dt_dec"));
obj.setR18_net(rs.getBigDecimal("r18_net"));
obj.setR18_cpdm_dt_der(rs.getBigDecimal("r18_cpdm_dt_der"));
obj.setR18_cpdm_dt_dto(rs.getBigDecimal("r18_cpdm_dt_dto"));
obj.setR18_cp(rs.getBigDecimal("r18_cp"));

// =========================
// R19
// =========================
obj.setR19_product(rs.getString("r19_product"));
obj.setR19_open_position(rs.getBigDecimal("r19_open_position"));
obj.setR19_cpdm_dt_inc(rs.getBigDecimal("r19_cpdm_dt_inc"));
obj.setR19_cpdm_dt_dec(rs.getBigDecimal("r19_cpdm_dt_dec"));
obj.setR19_net(rs.getBigDecimal("r19_net"));
obj.setR19_cpdm_dt_der(rs.getBigDecimal("r19_cpdm_dt_der"));
obj.setR19_cpdm_dt_dto(rs.getBigDecimal("r19_cpdm_dt_dto"));
obj.setR19_cp(rs.getBigDecimal("r19_cp"));

// =========================
// R20
// =========================
obj.setR20_product(rs.getString("r20_product"));
obj.setR20_open_position(rs.getBigDecimal("r20_open_position"));
obj.setR20_cpdm_dt_inc(rs.getBigDecimal("r20_cpdm_dt_inc"));
obj.setR20_cpdm_dt_dec(rs.getBigDecimal("r20_cpdm_dt_dec"));
obj.setR20_net(rs.getBigDecimal("r20_net"));
obj.setR20_cpdm_dt_der(rs.getBigDecimal("r20_cpdm_dt_der"));
obj.setR20_cpdm_dt_dto(rs.getBigDecimal("r20_cpdm_dt_dto"));
obj.setR20_cp(rs.getBigDecimal("r20_cp"));


// =========================
// R21
// =========================
obj.setR21_product(rs.getString("r21_product"));
obj.setR21_open_position(rs.getBigDecimal("r21_open_position"));
obj.setR21_cpdm_dt_inc(rs.getBigDecimal("r21_cpdm_dt_inc"));
obj.setR21_cpdm_dt_dec(rs.getBigDecimal("r21_cpdm_dt_dec"));
obj.setR21_net(rs.getBigDecimal("r21_net"));
obj.setR21_cpdm_dt_der(rs.getBigDecimal("r21_cpdm_dt_der"));
obj.setR21_cpdm_dt_dto(rs.getBigDecimal("r21_cpdm_dt_dto"));
obj.setR21_cp(rs.getBigDecimal("r21_cp"));

// =========================
// R22
// =========================
obj.setR22_product(rs.getString("r22_product"));
obj.setR22_open_position(rs.getBigDecimal("r22_open_position"));
obj.setR22_cpdm_dt_inc(rs.getBigDecimal("r22_cpdm_dt_inc"));
obj.setR22_cpdm_dt_dec(rs.getBigDecimal("r22_cpdm_dt_dec"));
obj.setR22_net(rs.getBigDecimal("r22_net"));
obj.setR22_cpdm_dt_der(rs.getBigDecimal("r22_cpdm_dt_der"));
obj.setR22_cpdm_dt_dto(rs.getBigDecimal("r22_cpdm_dt_dto"));
obj.setR22_cp(rs.getBigDecimal("r22_cp"));

// =========================
// R23
// =========================
obj.setR23_product(rs.getString("r23_product"));
obj.setR23_open_position(rs.getBigDecimal("r23_open_position"));
obj.setR23_cpdm_dt_inc(rs.getBigDecimal("r23_cpdm_dt_inc"));
obj.setR23_cpdm_dt_dec(rs.getBigDecimal("r23_cpdm_dt_dec"));
obj.setR23_net(rs.getBigDecimal("r23_net"));
obj.setR23_cpdm_dt_der(rs.getBigDecimal("r23_cpdm_dt_der"));
obj.setR23_cpdm_dt_dto(rs.getBigDecimal("r23_cpdm_dt_dto"));
obj.setR23_cp(rs.getBigDecimal("r23_cp"));

// =========================
// R24
// =========================
obj.setR24_product(rs.getString("r24_product"));
obj.setR24_open_position(rs.getBigDecimal("r24_open_position"));
obj.setR24_cpdm_dt_inc(rs.getBigDecimal("r24_cpdm_dt_inc"));
obj.setR24_cpdm_dt_dec(rs.getBigDecimal("r24_cpdm_dt_dec"));
obj.setR24_net(rs.getBigDecimal("r24_net"));
obj.setR24_cpdm_dt_der(rs.getBigDecimal("r24_cpdm_dt_der"));
obj.setR24_cpdm_dt_dto(rs.getBigDecimal("r24_cpdm_dt_dto"));
obj.setR24_cp(rs.getBigDecimal("r24_cp"));

// =========================
// R25
// =========================
obj.setR25_product(rs.getString("r25_product"));
obj.setR25_open_position(rs.getBigDecimal("r25_open_position"));
obj.setR25_cpdm_dt_inc(rs.getBigDecimal("r25_cpdm_dt_inc"));
obj.setR25_cpdm_dt_dec(rs.getBigDecimal("r25_cpdm_dt_dec"));
obj.setR25_net(rs.getBigDecimal("r25_net"));
obj.setR25_cpdm_dt_der(rs.getBigDecimal("r25_cpdm_dt_der"));
obj.setR25_cpdm_dt_dto(rs.getBigDecimal("r25_cpdm_dt_dto"));
obj.setR25_cp(rs.getBigDecimal("r25_cp"));

// =========================
// R26
// =========================
obj.setR26_product(rs.getString("r26_product"));
obj.setR26_open_position(rs.getBigDecimal("r26_open_position"));
obj.setR26_cpdm_dt_inc(rs.getBigDecimal("r26_cpdm_dt_inc"));
obj.setR26_cpdm_dt_dec(rs.getBigDecimal("r26_cpdm_dt_dec"));
obj.setR26_net(rs.getBigDecimal("r26_net"));
obj.setR26_cpdm_dt_der(rs.getBigDecimal("r26_cpdm_dt_der"));
obj.setR26_cpdm_dt_dto(rs.getBigDecimal("r26_cpdm_dt_dto"));
obj.setR26_cp(rs.getBigDecimal("r26_cp"));

// =========================
// R27
// =========================
obj.setR27_product(rs.getString("r27_product"));
obj.setR27_open_position(rs.getBigDecimal("r27_open_position"));
obj.setR27_cpdm_dt_inc(rs.getBigDecimal("r27_cpdm_dt_inc"));
obj.setR27_cpdm_dt_dec(rs.getBigDecimal("r27_cpdm_dt_dec"));
obj.setR27_net(rs.getBigDecimal("r27_net"));
obj.setR27_cpdm_dt_der(rs.getBigDecimal("r27_cpdm_dt_der"));
obj.setR27_cpdm_dt_dto(rs.getBigDecimal("r27_cpdm_dt_dto"));
obj.setR27_cp(rs.getBigDecimal("r27_cp"));

// =========================
// R28
// =========================
obj.setR28_product(rs.getString("r28_product"));
obj.setR28_open_position(rs.getBigDecimal("r28_open_position"));
obj.setR28_cpdm_dt_inc(rs.getBigDecimal("r28_cpdm_dt_inc"));
obj.setR28_cpdm_dt_dec(rs.getBigDecimal("r28_cpdm_dt_dec"));
obj.setR28_net(rs.getBigDecimal("r28_net"));
obj.setR28_cpdm_dt_der(rs.getBigDecimal("r28_cpdm_dt_der"));
obj.setR28_cpdm_dt_dto(rs.getBigDecimal("r28_cpdm_dt_dto"));
obj.setR28_cp(rs.getBigDecimal("r28_cp"));

// =========================
// R29
// =========================
obj.setR29_product(rs.getString("r29_product"));
obj.setR29_open_position(rs.getBigDecimal("r29_open_position"));
obj.setR29_cpdm_dt_inc(rs.getBigDecimal("r29_cpdm_dt_inc"));
obj.setR29_cpdm_dt_dec(rs.getBigDecimal("r29_cpdm_dt_dec"));
obj.setR29_net(rs.getBigDecimal("r29_net"));
obj.setR29_cpdm_dt_der(rs.getBigDecimal("r29_cpdm_dt_der"));
obj.setR29_cpdm_dt_dto(rs.getBigDecimal("r29_cpdm_dt_dto"));
obj.setR29_cp(rs.getBigDecimal("r29_cp"));

// =========================
// R30
// =========================
obj.setR30_product(rs.getString("r30_product"));
obj.setR30_open_position(rs.getBigDecimal("r30_open_position"));
obj.setR30_cpdm_dt_inc(rs.getBigDecimal("r30_cpdm_dt_inc"));
obj.setR30_cpdm_dt_dec(rs.getBigDecimal("r30_cpdm_dt_dec"));
obj.setR30_net(rs.getBigDecimal("r30_net"));
obj.setR30_cpdm_dt_der(rs.getBigDecimal("r30_cpdm_dt_der"));
obj.setR30_cpdm_dt_dto(rs.getBigDecimal("r30_cpdm_dt_dto"));
obj.setR30_cp(rs.getBigDecimal("r30_cp"));

// =========================
// R31
// =========================
obj.setR31_product(rs.getString("r31_product"));
obj.setR31_open_position(rs.getBigDecimal("r31_open_position"));
obj.setR31_cpdm_dt_inc(rs.getBigDecimal("r31_cpdm_dt_inc"));
obj.setR31_cpdm_dt_dec(rs.getBigDecimal("r31_cpdm_dt_dec"));
obj.setR31_net(rs.getBigDecimal("r31_net"));
obj.setR31_cpdm_dt_der(rs.getBigDecimal("r31_cpdm_dt_der"));
obj.setR31_cpdm_dt_dto(rs.getBigDecimal("r31_cpdm_dt_dto"));
obj.setR31_cp(rs.getBigDecimal("r31_cp"));

// =========================
// R32
// =========================
obj.setR32_product(rs.getString("r32_product"));
obj.setR32_open_position(rs.getBigDecimal("r32_open_position"));
obj.setR32_cpdm_dt_inc(rs.getBigDecimal("r32_cpdm_dt_inc"));
obj.setR32_cpdm_dt_dec(rs.getBigDecimal("r32_cpdm_dt_dec"));
obj.setR32_net(rs.getBigDecimal("r32_net"));
obj.setR32_cpdm_dt_der(rs.getBigDecimal("r32_cpdm_dt_der"));
obj.setR32_cpdm_dt_dto(rs.getBigDecimal("r32_cpdm_dt_dto"));
obj.setR32_cp(rs.getBigDecimal("r32_cp"));

// =========================
// R33
// =========================
obj.setR33_product(rs.getString("r33_product"));
obj.setR33_open_position(rs.getBigDecimal("r33_open_position"));
obj.setR33_cpdm_dt_inc(rs.getBigDecimal("r33_cpdm_dt_inc"));
obj.setR33_cpdm_dt_dec(rs.getBigDecimal("r33_cpdm_dt_dec"));
obj.setR33_net(rs.getBigDecimal("r33_net"));
obj.setR33_cpdm_dt_der(rs.getBigDecimal("r33_cpdm_dt_der"));
obj.setR33_cpdm_dt_dto(rs.getBigDecimal("r33_cpdm_dt_dto"));
obj.setR33_cp(rs.getBigDecimal("r33_cp"));

// =========================
// R34
// =========================
obj.setR34_product(rs.getString("r34_product"));
obj.setR34_open_position(rs.getBigDecimal("r34_open_position"));
obj.setR34_cpdm_dt_inc(rs.getBigDecimal("r34_cpdm_dt_inc"));
obj.setR34_cpdm_dt_dec(rs.getBigDecimal("r34_cpdm_dt_dec"));
obj.setR34_net(rs.getBigDecimal("r34_net"));
obj.setR34_cpdm_dt_der(rs.getBigDecimal("r34_cpdm_dt_der"));
obj.setR34_cpdm_dt_dto(rs.getBigDecimal("r34_cpdm_dt_dto"));
obj.setR34_cp(rs.getBigDecimal("r34_cp"));

// =========================
// R35
// =========================
obj.setR35_product(rs.getString("r35_product"));
obj.setR35_open_position(rs.getBigDecimal("r35_open_position"));
obj.setR35_cpdm_dt_inc(rs.getBigDecimal("r35_cpdm_dt_inc"));
obj.setR35_cpdm_dt_dec(rs.getBigDecimal("r35_cpdm_dt_dec"));
obj.setR35_net(rs.getBigDecimal("r35_net"));
obj.setR35_cpdm_dt_der(rs.getBigDecimal("r35_cpdm_dt_der"));
obj.setR35_cpdm_dt_dto(rs.getBigDecimal("r35_cpdm_dt_dto"));
obj.setR35_cp(rs.getBigDecimal("r35_cp"));

// =========================
// R36
// =========================
obj.setR36_product(rs.getString("r36_product"));
obj.setR36_open_position(rs.getBigDecimal("r36_open_position"));
obj.setR36_cpdm_dt_inc(rs.getBigDecimal("r36_cpdm_dt_inc"));
obj.setR36_cpdm_dt_dec(rs.getBigDecimal("r36_cpdm_dt_dec"));
obj.setR36_net(rs.getBigDecimal("r36_net"));
obj.setR36_cpdm_dt_der(rs.getBigDecimal("r36_cpdm_dt_der"));
obj.setR36_cpdm_dt_dto(rs.getBigDecimal("r36_cpdm_dt_dto"));
obj.setR36_cp(rs.getBigDecimal("r36_cp"));

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


public class M_BOP_Summary_Entity {
	
	 private String	r13_product;
	private BigDecimal	r13_open_position;
	private BigDecimal	r13_cpdm_dt_inc;
	private BigDecimal	r13_cpdm_dt_dec;
	private BigDecimal	r13_net;
	private BigDecimal	r13_cpdm_dt_der;
	private BigDecimal	r13_cpdm_dt_dto;
	private BigDecimal	r13_cp;
	private String	r14_product;
	private BigDecimal	r14_open_position;
	private BigDecimal	r14_cpdm_dt_inc;
	private BigDecimal	r14_cpdm_dt_dec;
	private BigDecimal	r14_net;
	private BigDecimal	r14_cpdm_dt_der;
	private BigDecimal	r14_cpdm_dt_dto;
	private BigDecimal	r14_cp;
	private String	r15_product;
	private BigDecimal	r15_open_position;
	private BigDecimal	r15_cpdm_dt_inc;
	private BigDecimal	r15_cpdm_dt_dec;
	private BigDecimal	r15_net;
	private BigDecimal	r15_cpdm_dt_der;
	private BigDecimal	r15_cpdm_dt_dto;
	private BigDecimal	r15_cp;
	private String	r16_product;
	private BigDecimal	r16_open_position;
	private BigDecimal	r16_cpdm_dt_inc;
	private BigDecimal	r16_cpdm_dt_dec;
	private BigDecimal	r16_net;
	private BigDecimal	r16_cpdm_dt_der;
	private BigDecimal	r16_cpdm_dt_dto;
	private BigDecimal	r16_cp;
	private String	r17_product;
	private BigDecimal	r17_open_position;
	private BigDecimal	r17_cpdm_dt_inc;
	private BigDecimal	r17_cpdm_dt_dec;
	private BigDecimal	r17_net;
	private BigDecimal	r17_cpdm_dt_der;
	private BigDecimal	r17_cpdm_dt_dto;
	private BigDecimal	r17_cp;
	private String	r18_product;
	private BigDecimal	r18_open_position;
	private BigDecimal	r18_cpdm_dt_inc;
	private BigDecimal	r18_cpdm_dt_dec;
	private BigDecimal	r18_net;
	private BigDecimal	r18_cpdm_dt_der;
	private BigDecimal	r18_cpdm_dt_dto;
	private BigDecimal	r18_cp;
	private String	r19_product;
	private BigDecimal	r19_open_position;
	private BigDecimal	r19_cpdm_dt_inc;
	private BigDecimal	r19_cpdm_dt_dec;
	private BigDecimal	r19_net;
	private BigDecimal	r19_cpdm_dt_der;
	private BigDecimal	r19_cpdm_dt_dto;
	private BigDecimal	r19_cp;
	private String	r20_product;
	private BigDecimal	r20_open_position;
	private BigDecimal	r20_cpdm_dt_inc;
	private BigDecimal	r20_cpdm_dt_dec;
	private BigDecimal	r20_net;
	private BigDecimal	r20_cpdm_dt_der;
	private BigDecimal	r20_cpdm_dt_dto;
	private BigDecimal	r20_cp;
	private String	r21_product;
	private BigDecimal	r21_open_position;
	private BigDecimal	r21_cpdm_dt_inc;
	private BigDecimal	r21_cpdm_dt_dec;
	private BigDecimal	r21_net;
	private BigDecimal	r21_cpdm_dt_der;
	private BigDecimal	r21_cpdm_dt_dto;
	private BigDecimal	r21_cp;
	private String	r22_product;
	private BigDecimal	r22_open_position;
	private BigDecimal	r22_cpdm_dt_inc;
	private BigDecimal	r22_cpdm_dt_dec;
	private BigDecimal	r22_net;
	private BigDecimal	r22_cpdm_dt_der;
	private BigDecimal	r22_cpdm_dt_dto;
	private BigDecimal	r22_cp;
	private String	r23_product;
	private BigDecimal	r23_open_position;
	private BigDecimal	r23_cpdm_dt_inc;
	private BigDecimal	r23_cpdm_dt_dec;
	private BigDecimal	r23_net;
	private BigDecimal	r23_cpdm_dt_der;
	private BigDecimal	r23_cpdm_dt_dto;
	private BigDecimal	r23_cp;
	private String	r24_product;
	private BigDecimal	r24_open_position;
	private BigDecimal	r24_cpdm_dt_inc;
	private BigDecimal	r24_cpdm_dt_dec;
	private BigDecimal	r24_net;
	private BigDecimal	r24_cpdm_dt_der;
	private BigDecimal	r24_cpdm_dt_dto;
	private BigDecimal	r24_cp;
	private String	r25_product;
	private BigDecimal	r25_open_position;
	private BigDecimal	r25_cpdm_dt_inc;
	private BigDecimal	r25_cpdm_dt_dec;
	private BigDecimal	r25_net;
	private BigDecimal	r25_cpdm_dt_der;
	private BigDecimal	r25_cpdm_dt_dto;
	private BigDecimal	r25_cp;
	private String	r26_product;
	private BigDecimal	r26_open_position;
	private BigDecimal	r26_cpdm_dt_inc;
	private BigDecimal	r26_cpdm_dt_dec;
	private BigDecimal	r26_net;
	private BigDecimal	r26_cpdm_dt_der;
	private BigDecimal	r26_cpdm_dt_dto;
	private BigDecimal	r26_cp;
	private String	r27_product;
	private BigDecimal	r27_open_position;
	private BigDecimal	r27_cpdm_dt_inc;
	private BigDecimal	r27_cpdm_dt_dec;
	private BigDecimal	r27_net;
	private BigDecimal	r27_cpdm_dt_der;
	private BigDecimal	r27_cpdm_dt_dto;
	private BigDecimal	r27_cp;
	private String	r28_product;
	private BigDecimal	r28_open_position;
	private BigDecimal	r28_cpdm_dt_inc;
	private BigDecimal	r28_cpdm_dt_dec;
	private BigDecimal	r28_net;
	private BigDecimal	r28_cpdm_dt_der;
	private BigDecimal	r28_cpdm_dt_dto;
	private BigDecimal	r28_cp;
	private String	r29_product;
	private BigDecimal	r29_open_position;
	private BigDecimal	r29_cpdm_dt_inc;
	private BigDecimal	r29_cpdm_dt_dec;
	private BigDecimal	r29_net;
	private BigDecimal	r29_cpdm_dt_der;
	private BigDecimal	r29_cpdm_dt_dto;
	private BigDecimal	r29_cp;
	private String	r30_product;
	private BigDecimal	r30_open_position;
	private BigDecimal	r30_cpdm_dt_inc;
	private BigDecimal	r30_cpdm_dt_dec;
	private BigDecimal	r30_net;
	private BigDecimal	r30_cpdm_dt_der;
	private BigDecimal	r30_cpdm_dt_dto;
	private BigDecimal	r30_cp;
	private String	r31_product;
	private BigDecimal	r31_open_position;
	private BigDecimal	r31_cpdm_dt_inc;
	private BigDecimal	r31_cpdm_dt_dec;
	private BigDecimal	r31_net;
	private BigDecimal	r31_cpdm_dt_der;
	private BigDecimal	r31_cpdm_dt_dto;
	private BigDecimal	r31_cp;
	private String	r32_product;
	private BigDecimal	r32_open_position;
	private BigDecimal	r32_cpdm_dt_inc;
	private BigDecimal	r32_cpdm_dt_dec;
	private BigDecimal	r32_net;
	private BigDecimal	r32_cpdm_dt_der;
	private BigDecimal	r32_cpdm_dt_dto;
	private BigDecimal	r32_cp;
	private String	r33_product;
	private BigDecimal	r33_open_position;
	private BigDecimal	r33_cpdm_dt_inc;
	private BigDecimal	r33_cpdm_dt_dec;
	private BigDecimal	r33_net;
	private BigDecimal	r33_cpdm_dt_der;
	private BigDecimal	r33_cpdm_dt_dto;
	private BigDecimal	r33_cp;
	private String	r34_product;
	private BigDecimal	r34_open_position;
	private BigDecimal	r34_cpdm_dt_inc;
	private BigDecimal	r34_cpdm_dt_dec;
	private BigDecimal	r34_net;
	private BigDecimal	r34_cpdm_dt_der;
	private BigDecimal	r34_cpdm_dt_dto;
	private BigDecimal	r34_cp;
	private String	r35_product;
	private BigDecimal	r35_open_position;
	private BigDecimal	r35_cpdm_dt_inc;
	private BigDecimal	r35_cpdm_dt_dec;
	private BigDecimal	r35_net;
	private BigDecimal	r35_cpdm_dt_der;
	private BigDecimal	r35_cpdm_dt_dto;
	private BigDecimal	r35_cp;
	private String	r36_product;
	private BigDecimal	r36_open_position;
	private BigDecimal	r36_cpdm_dt_inc;
	private BigDecimal	r36_cpdm_dt_dec;
	private BigDecimal	r36_net;
	private BigDecimal	r36_cpdm_dt_der;
	private BigDecimal	r36_cpdm_dt_dto;
	private BigDecimal	r36_cp;
	

	
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
	
	
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_open_position() {
		return r13_open_position;
	}
	public void setR13_open_position(BigDecimal r13_open_position) {
		this.r13_open_position = r13_open_position;
	}
	public BigDecimal getR13_cpdm_dt_inc() {
		return r13_cpdm_dt_inc;
	}
	public void setR13_cpdm_dt_inc(BigDecimal r13_cpdm_dt_inc) {
		this.r13_cpdm_dt_inc = r13_cpdm_dt_inc;
	}
	public BigDecimal getR13_cpdm_dt_dec() {
		return r13_cpdm_dt_dec;
	}
	public void setR13_cpdm_dt_dec(BigDecimal r13_cpdm_dt_dec) {
		this.r13_cpdm_dt_dec = r13_cpdm_dt_dec;
	}
	public BigDecimal getR13_net() {
		return r13_net;
	}
	public void setR13_net(BigDecimal r13_net) {
		this.r13_net = r13_net;
	}
	public BigDecimal getR13_cpdm_dt_der() {
		return r13_cpdm_dt_der;
	}
	public void setR13_cpdm_dt_der(BigDecimal r13_cpdm_dt_der) {
		this.r13_cpdm_dt_der = r13_cpdm_dt_der;
	}
	public BigDecimal getR13_cpdm_dt_dto() {
		return r13_cpdm_dt_dto;
	}
	public void setR13_cpdm_dt_dto(BigDecimal r13_cpdm_dt_dto) {
		this.r13_cpdm_dt_dto = r13_cpdm_dt_dto;
	}
	public BigDecimal getR13_cp() {
		return r13_cp;
	}
	public void setR13_cp(BigDecimal r13_cp) {
		this.r13_cp = r13_cp;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_open_position() {
		return r14_open_position;
	}
	public void setR14_open_position(BigDecimal r14_open_position) {
		this.r14_open_position = r14_open_position;
	}
	public BigDecimal getR14_cpdm_dt_inc() {
		return r14_cpdm_dt_inc;
	}
	public void setR14_cpdm_dt_inc(BigDecimal r14_cpdm_dt_inc) {
		this.r14_cpdm_dt_inc = r14_cpdm_dt_inc;
	}
	public BigDecimal getR14_cpdm_dt_dec() {
		return r14_cpdm_dt_dec;
	}
	public void setR14_cpdm_dt_dec(BigDecimal r14_cpdm_dt_dec) {
		this.r14_cpdm_dt_dec = r14_cpdm_dt_dec;
	}
	public BigDecimal getR14_net() {
		return r14_net;
	}
	public void setR14_net(BigDecimal r14_net) {
		this.r14_net = r14_net;
	}
	public BigDecimal getR14_cpdm_dt_der() {
		return r14_cpdm_dt_der;
	}
	public void setR14_cpdm_dt_der(BigDecimal r14_cpdm_dt_der) {
		this.r14_cpdm_dt_der = r14_cpdm_dt_der;
	}
	public BigDecimal getR14_cpdm_dt_dto() {
		return r14_cpdm_dt_dto;
	}
	public void setR14_cpdm_dt_dto(BigDecimal r14_cpdm_dt_dto) {
		this.r14_cpdm_dt_dto = r14_cpdm_dt_dto;
	}
	public BigDecimal getR14_cp() {
		return r14_cp;
	}
	public void setR14_cp(BigDecimal r14_cp) {
		this.r14_cp = r14_cp;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_open_position() {
		return r15_open_position;
	}
	public void setR15_open_position(BigDecimal r15_open_position) {
		this.r15_open_position = r15_open_position;
	}
	public BigDecimal getR15_cpdm_dt_inc() {
		return r15_cpdm_dt_inc;
	}
	public void setR15_cpdm_dt_inc(BigDecimal r15_cpdm_dt_inc) {
		this.r15_cpdm_dt_inc = r15_cpdm_dt_inc;
	}
	public BigDecimal getR15_cpdm_dt_dec() {
		return r15_cpdm_dt_dec;
	}
	public void setR15_cpdm_dt_dec(BigDecimal r15_cpdm_dt_dec) {
		this.r15_cpdm_dt_dec = r15_cpdm_dt_dec;
	}
	public BigDecimal getR15_net() {
		return r15_net;
	}
	public void setR15_net(BigDecimal r15_net) {
		this.r15_net = r15_net;
	}
	public BigDecimal getR15_cpdm_dt_der() {
		return r15_cpdm_dt_der;
	}
	public void setR15_cpdm_dt_der(BigDecimal r15_cpdm_dt_der) {
		this.r15_cpdm_dt_der = r15_cpdm_dt_der;
	}
	public BigDecimal getR15_cpdm_dt_dto() {
		return r15_cpdm_dt_dto;
	}
	public void setR15_cpdm_dt_dto(BigDecimal r15_cpdm_dt_dto) {
		this.r15_cpdm_dt_dto = r15_cpdm_dt_dto;
	}
	public BigDecimal getR15_cp() {
		return r15_cp;
	}
	public void setR15_cp(BigDecimal r15_cp) {
		this.r15_cp = r15_cp;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_open_position() {
		return r16_open_position;
	}
	public void setR16_open_position(BigDecimal r16_open_position) {
		this.r16_open_position = r16_open_position;
	}
	public BigDecimal getR16_cpdm_dt_inc() {
		return r16_cpdm_dt_inc;
	}
	public void setR16_cpdm_dt_inc(BigDecimal r16_cpdm_dt_inc) {
		this.r16_cpdm_dt_inc = r16_cpdm_dt_inc;
	}
	public BigDecimal getR16_cpdm_dt_dec() {
		return r16_cpdm_dt_dec;
	}
	public void setR16_cpdm_dt_dec(BigDecimal r16_cpdm_dt_dec) {
		this.r16_cpdm_dt_dec = r16_cpdm_dt_dec;
	}
	public BigDecimal getR16_net() {
		return r16_net;
	}
	public void setR16_net(BigDecimal r16_net) {
		this.r16_net = r16_net;
	}
	public BigDecimal getR16_cpdm_dt_der() {
		return r16_cpdm_dt_der;
	}
	public void setR16_cpdm_dt_der(BigDecimal r16_cpdm_dt_der) {
		this.r16_cpdm_dt_der = r16_cpdm_dt_der;
	}
	public BigDecimal getR16_cpdm_dt_dto() {
		return r16_cpdm_dt_dto;
	}
	public void setR16_cpdm_dt_dto(BigDecimal r16_cpdm_dt_dto) {
		this.r16_cpdm_dt_dto = r16_cpdm_dt_dto;
	}
	public BigDecimal getR16_cp() {
		return r16_cp;
	}
	public void setR16_cp(BigDecimal r16_cp) {
		this.r16_cp = r16_cp;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_open_position() {
		return r17_open_position;
	}
	public void setR17_open_position(BigDecimal r17_open_position) {
		this.r17_open_position = r17_open_position;
	}
	public BigDecimal getR17_cpdm_dt_inc() {
		return r17_cpdm_dt_inc;
	}
	public void setR17_cpdm_dt_inc(BigDecimal r17_cpdm_dt_inc) {
		this.r17_cpdm_dt_inc = r17_cpdm_dt_inc;
	}
	public BigDecimal getR17_cpdm_dt_dec() {
		return r17_cpdm_dt_dec;
	}
	public void setR17_cpdm_dt_dec(BigDecimal r17_cpdm_dt_dec) {
		this.r17_cpdm_dt_dec = r17_cpdm_dt_dec;
	}
	public BigDecimal getR17_net() {
		return r17_net;
	}
	public void setR17_net(BigDecimal r17_net) {
		this.r17_net = r17_net;
	}
	public BigDecimal getR17_cpdm_dt_der() {
		return r17_cpdm_dt_der;
	}
	public void setR17_cpdm_dt_der(BigDecimal r17_cpdm_dt_der) {
		this.r17_cpdm_dt_der = r17_cpdm_dt_der;
	}
	public BigDecimal getR17_cpdm_dt_dto() {
		return r17_cpdm_dt_dto;
	}
	public void setR17_cpdm_dt_dto(BigDecimal r17_cpdm_dt_dto) {
		this.r17_cpdm_dt_dto = r17_cpdm_dt_dto;
	}
	public BigDecimal getR17_cp() {
		return r17_cp;
	}
	public void setR17_cp(BigDecimal r17_cp) {
		this.r17_cp = r17_cp;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_open_position() {
		return r18_open_position;
	}
	public void setR18_open_position(BigDecimal r18_open_position) {
		this.r18_open_position = r18_open_position;
	}
	public BigDecimal getR18_cpdm_dt_inc() {
		return r18_cpdm_dt_inc;
	}
	public void setR18_cpdm_dt_inc(BigDecimal r18_cpdm_dt_inc) {
		this.r18_cpdm_dt_inc = r18_cpdm_dt_inc;
	}
	public BigDecimal getR18_cpdm_dt_dec() {
		return r18_cpdm_dt_dec;
	}
	public void setR18_cpdm_dt_dec(BigDecimal r18_cpdm_dt_dec) {
		this.r18_cpdm_dt_dec = r18_cpdm_dt_dec;
	}
	public BigDecimal getR18_net() {
		return r18_net;
	}
	public void setR18_net(BigDecimal r18_net) {
		this.r18_net = r18_net;
	}
	public BigDecimal getR18_cpdm_dt_der() {
		return r18_cpdm_dt_der;
	}
	public void setR18_cpdm_dt_der(BigDecimal r18_cpdm_dt_der) {
		this.r18_cpdm_dt_der = r18_cpdm_dt_der;
	}
	public BigDecimal getR18_cpdm_dt_dto() {
		return r18_cpdm_dt_dto;
	}
	public void setR18_cpdm_dt_dto(BigDecimal r18_cpdm_dt_dto) {
		this.r18_cpdm_dt_dto = r18_cpdm_dt_dto;
	}
	public BigDecimal getR18_cp() {
		return r18_cp;
	}
	public void setR18_cp(BigDecimal r18_cp) {
		this.r18_cp = r18_cp;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_open_position() {
		return r19_open_position;
	}
	public void setR19_open_position(BigDecimal r19_open_position) {
		this.r19_open_position = r19_open_position;
	}
	public BigDecimal getR19_cpdm_dt_inc() {
		return r19_cpdm_dt_inc;
	}
	public void setR19_cpdm_dt_inc(BigDecimal r19_cpdm_dt_inc) {
		this.r19_cpdm_dt_inc = r19_cpdm_dt_inc;
	}
	public BigDecimal getR19_cpdm_dt_dec() {
		return r19_cpdm_dt_dec;
	}
	public void setR19_cpdm_dt_dec(BigDecimal r19_cpdm_dt_dec) {
		this.r19_cpdm_dt_dec = r19_cpdm_dt_dec;
	}
	public BigDecimal getR19_net() {
		return r19_net;
	}
	public void setR19_net(BigDecimal r19_net) {
		this.r19_net = r19_net;
	}
	public BigDecimal getR19_cpdm_dt_der() {
		return r19_cpdm_dt_der;
	}
	public void setR19_cpdm_dt_der(BigDecimal r19_cpdm_dt_der) {
		this.r19_cpdm_dt_der = r19_cpdm_dt_der;
	}
	public BigDecimal getR19_cpdm_dt_dto() {
		return r19_cpdm_dt_dto;
	}
	public void setR19_cpdm_dt_dto(BigDecimal r19_cpdm_dt_dto) {
		this.r19_cpdm_dt_dto = r19_cpdm_dt_dto;
	}
	public BigDecimal getR19_cp() {
		return r19_cp;
	}
	public void setR19_cp(BigDecimal r19_cp) {
		this.r19_cp = r19_cp;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_open_position() {
		return r20_open_position;
	}
	public void setR20_open_position(BigDecimal r20_open_position) {
		this.r20_open_position = r20_open_position;
	}
	public BigDecimal getR20_cpdm_dt_inc() {
		return r20_cpdm_dt_inc;
	}
	public void setR20_cpdm_dt_inc(BigDecimal r20_cpdm_dt_inc) {
		this.r20_cpdm_dt_inc = r20_cpdm_dt_inc;
	}
	public BigDecimal getR20_cpdm_dt_dec() {
		return r20_cpdm_dt_dec;
	}
	public void setR20_cpdm_dt_dec(BigDecimal r20_cpdm_dt_dec) {
		this.r20_cpdm_dt_dec = r20_cpdm_dt_dec;
	}
	public BigDecimal getR20_net() {
		return r20_net;
	}
	public void setR20_net(BigDecimal r20_net) {
		this.r20_net = r20_net;
	}
	public BigDecimal getR20_cpdm_dt_der() {
		return r20_cpdm_dt_der;
	}
	public void setR20_cpdm_dt_der(BigDecimal r20_cpdm_dt_der) {
		this.r20_cpdm_dt_der = r20_cpdm_dt_der;
	}
	public BigDecimal getR20_cpdm_dt_dto() {
		return r20_cpdm_dt_dto;
	}
	public void setR20_cpdm_dt_dto(BigDecimal r20_cpdm_dt_dto) {
		this.r20_cpdm_dt_dto = r20_cpdm_dt_dto;
	}
	public BigDecimal getR20_cp() {
		return r20_cp;
	}
	public void setR20_cp(BigDecimal r20_cp) {
		this.r20_cp = r20_cp;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_open_position() {
		return r21_open_position;
	}
	public void setR21_open_position(BigDecimal r21_open_position) {
		this.r21_open_position = r21_open_position;
	}
	public BigDecimal getR21_cpdm_dt_inc() {
		return r21_cpdm_dt_inc;
	}
	public void setR21_cpdm_dt_inc(BigDecimal r21_cpdm_dt_inc) {
		this.r21_cpdm_dt_inc = r21_cpdm_dt_inc;
	}
	public BigDecimal getR21_cpdm_dt_dec() {
		return r21_cpdm_dt_dec;
	}
	public void setR21_cpdm_dt_dec(BigDecimal r21_cpdm_dt_dec) {
		this.r21_cpdm_dt_dec = r21_cpdm_dt_dec;
	}
	public BigDecimal getR21_net() {
		return r21_net;
	}
	public void setR21_net(BigDecimal r21_net) {
		this.r21_net = r21_net;
	}
	public BigDecimal getR21_cpdm_dt_der() {
		return r21_cpdm_dt_der;
	}
	public void setR21_cpdm_dt_der(BigDecimal r21_cpdm_dt_der) {
		this.r21_cpdm_dt_der = r21_cpdm_dt_der;
	}
	public BigDecimal getR21_cpdm_dt_dto() {
		return r21_cpdm_dt_dto;
	}
	public void setR21_cpdm_dt_dto(BigDecimal r21_cpdm_dt_dto) {
		this.r21_cpdm_dt_dto = r21_cpdm_dt_dto;
	}
	public BigDecimal getR21_cp() {
		return r21_cp;
	}
	public void setR21_cp(BigDecimal r21_cp) {
		this.r21_cp = r21_cp;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_open_position() {
		return r22_open_position;
	}
	public void setR22_open_position(BigDecimal r22_open_position) {
		this.r22_open_position = r22_open_position;
	}
	public BigDecimal getR22_cpdm_dt_inc() {
		return r22_cpdm_dt_inc;
	}
	public void setR22_cpdm_dt_inc(BigDecimal r22_cpdm_dt_inc) {
		this.r22_cpdm_dt_inc = r22_cpdm_dt_inc;
	}
	public BigDecimal getR22_cpdm_dt_dec() {
		return r22_cpdm_dt_dec;
	}
	public void setR22_cpdm_dt_dec(BigDecimal r22_cpdm_dt_dec) {
		this.r22_cpdm_dt_dec = r22_cpdm_dt_dec;
	}
	public BigDecimal getR22_net() {
		return r22_net;
	}
	public void setR22_net(BigDecimal r22_net) {
		this.r22_net = r22_net;
	}
	public BigDecimal getR22_cpdm_dt_der() {
		return r22_cpdm_dt_der;
	}
	public void setR22_cpdm_dt_der(BigDecimal r22_cpdm_dt_der) {
		this.r22_cpdm_dt_der = r22_cpdm_dt_der;
	}
	public BigDecimal getR22_cpdm_dt_dto() {
		return r22_cpdm_dt_dto;
	}
	public void setR22_cpdm_dt_dto(BigDecimal r22_cpdm_dt_dto) {
		this.r22_cpdm_dt_dto = r22_cpdm_dt_dto;
	}
	public BigDecimal getR22_cp() {
		return r22_cp;
	}
	public void setR22_cp(BigDecimal r22_cp) {
		this.r22_cp = r22_cp;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_open_position() {
		return r23_open_position;
	}
	public void setR23_open_position(BigDecimal r23_open_position) {
		this.r23_open_position = r23_open_position;
	}
	public BigDecimal getR23_cpdm_dt_inc() {
		return r23_cpdm_dt_inc;
	}
	public void setR23_cpdm_dt_inc(BigDecimal r23_cpdm_dt_inc) {
		this.r23_cpdm_dt_inc = r23_cpdm_dt_inc;
	}
	public BigDecimal getR23_cpdm_dt_dec() {
		return r23_cpdm_dt_dec;
	}
	public void setR23_cpdm_dt_dec(BigDecimal r23_cpdm_dt_dec) {
		this.r23_cpdm_dt_dec = r23_cpdm_dt_dec;
	}
	public BigDecimal getR23_net() {
		return r23_net;
	}
	public void setR23_net(BigDecimal r23_net) {
		this.r23_net = r23_net;
	}
	public BigDecimal getR23_cpdm_dt_der() {
		return r23_cpdm_dt_der;
	}
	public void setR23_cpdm_dt_der(BigDecimal r23_cpdm_dt_der) {
		this.r23_cpdm_dt_der = r23_cpdm_dt_der;
	}
	public BigDecimal getR23_cpdm_dt_dto() {
		return r23_cpdm_dt_dto;
	}
	public void setR23_cpdm_dt_dto(BigDecimal r23_cpdm_dt_dto) {
		this.r23_cpdm_dt_dto = r23_cpdm_dt_dto;
	}
	public BigDecimal getR23_cp() {
		return r23_cp;
	}
	public void setR23_cp(BigDecimal r23_cp) {
		this.r23_cp = r23_cp;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_open_position() {
		return r24_open_position;
	}
	public void setR24_open_position(BigDecimal r24_open_position) {
		this.r24_open_position = r24_open_position;
	}
	public BigDecimal getR24_cpdm_dt_inc() {
		return r24_cpdm_dt_inc;
	}
	public void setR24_cpdm_dt_inc(BigDecimal r24_cpdm_dt_inc) {
		this.r24_cpdm_dt_inc = r24_cpdm_dt_inc;
	}
	public BigDecimal getR24_cpdm_dt_dec() {
		return r24_cpdm_dt_dec;
	}
	public void setR24_cpdm_dt_dec(BigDecimal r24_cpdm_dt_dec) {
		this.r24_cpdm_dt_dec = r24_cpdm_dt_dec;
	}
	public BigDecimal getR24_net() {
		return r24_net;
	}
	public void setR24_net(BigDecimal r24_net) {
		this.r24_net = r24_net;
	}
	public BigDecimal getR24_cpdm_dt_der() {
		return r24_cpdm_dt_der;
	}
	public void setR24_cpdm_dt_der(BigDecimal r24_cpdm_dt_der) {
		this.r24_cpdm_dt_der = r24_cpdm_dt_der;
	}
	public BigDecimal getR24_cpdm_dt_dto() {
		return r24_cpdm_dt_dto;
	}
	public void setR24_cpdm_dt_dto(BigDecimal r24_cpdm_dt_dto) {
		this.r24_cpdm_dt_dto = r24_cpdm_dt_dto;
	}
	public BigDecimal getR24_cp() {
		return r24_cp;
	}
	public void setR24_cp(BigDecimal r24_cp) {
		this.r24_cp = r24_cp;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_open_position() {
		return r25_open_position;
	}
	public void setR25_open_position(BigDecimal r25_open_position) {
		this.r25_open_position = r25_open_position;
	}
	public BigDecimal getR25_cpdm_dt_inc() {
		return r25_cpdm_dt_inc;
	}
	public void setR25_cpdm_dt_inc(BigDecimal r25_cpdm_dt_inc) {
		this.r25_cpdm_dt_inc = r25_cpdm_dt_inc;
	}
	public BigDecimal getR25_cpdm_dt_dec() {
		return r25_cpdm_dt_dec;
	}
	public void setR25_cpdm_dt_dec(BigDecimal r25_cpdm_dt_dec) {
		this.r25_cpdm_dt_dec = r25_cpdm_dt_dec;
	}
	public BigDecimal getR25_net() {
		return r25_net;
	}
	public void setR25_net(BigDecimal r25_net) {
		this.r25_net = r25_net;
	}
	public BigDecimal getR25_cpdm_dt_der() {
		return r25_cpdm_dt_der;
	}
	public void setR25_cpdm_dt_der(BigDecimal r25_cpdm_dt_der) {
		this.r25_cpdm_dt_der = r25_cpdm_dt_der;
	}
	public BigDecimal getR25_cpdm_dt_dto() {
		return r25_cpdm_dt_dto;
	}
	public void setR25_cpdm_dt_dto(BigDecimal r25_cpdm_dt_dto) {
		this.r25_cpdm_dt_dto = r25_cpdm_dt_dto;
	}
	public BigDecimal getR25_cp() {
		return r25_cp;
	}
	public void setR25_cp(BigDecimal r25_cp) {
		this.r25_cp = r25_cp;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_open_position() {
		return r26_open_position;
	}
	public void setR26_open_position(BigDecimal r26_open_position) {
		this.r26_open_position = r26_open_position;
	}
	public BigDecimal getR26_cpdm_dt_inc() {
		return r26_cpdm_dt_inc;
	}
	public void setR26_cpdm_dt_inc(BigDecimal r26_cpdm_dt_inc) {
		this.r26_cpdm_dt_inc = r26_cpdm_dt_inc;
	}
	public BigDecimal getR26_cpdm_dt_dec() {
		return r26_cpdm_dt_dec;
	}
	public void setR26_cpdm_dt_dec(BigDecimal r26_cpdm_dt_dec) {
		this.r26_cpdm_dt_dec = r26_cpdm_dt_dec;
	}
	public BigDecimal getR26_net() {
		return r26_net;
	}
	public void setR26_net(BigDecimal r26_net) {
		this.r26_net = r26_net;
	}
	public BigDecimal getR26_cpdm_dt_der() {
		return r26_cpdm_dt_der;
	}
	public void setR26_cpdm_dt_der(BigDecimal r26_cpdm_dt_der) {
		this.r26_cpdm_dt_der = r26_cpdm_dt_der;
	}
	public BigDecimal getR26_cpdm_dt_dto() {
		return r26_cpdm_dt_dto;
	}
	public void setR26_cpdm_dt_dto(BigDecimal r26_cpdm_dt_dto) {
		this.r26_cpdm_dt_dto = r26_cpdm_dt_dto;
	}
	public BigDecimal getR26_cp() {
		return r26_cp;
	}
	public void setR26_cp(BigDecimal r26_cp) {
		this.r26_cp = r26_cp;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_open_position() {
		return r27_open_position;
	}
	public void setR27_open_position(BigDecimal r27_open_position) {
		this.r27_open_position = r27_open_position;
	}
	public BigDecimal getR27_cpdm_dt_inc() {
		return r27_cpdm_dt_inc;
	}
	public void setR27_cpdm_dt_inc(BigDecimal r27_cpdm_dt_inc) {
		this.r27_cpdm_dt_inc = r27_cpdm_dt_inc;
	}
	public BigDecimal getR27_cpdm_dt_dec() {
		return r27_cpdm_dt_dec;
	}
	public void setR27_cpdm_dt_dec(BigDecimal r27_cpdm_dt_dec) {
		this.r27_cpdm_dt_dec = r27_cpdm_dt_dec;
	}
	public BigDecimal getR27_net() {
		return r27_net;
	}
	public void setR27_net(BigDecimal r27_net) {
		this.r27_net = r27_net;
	}
	public BigDecimal getR27_cpdm_dt_der() {
		return r27_cpdm_dt_der;
	}
	public void setR27_cpdm_dt_der(BigDecimal r27_cpdm_dt_der) {
		this.r27_cpdm_dt_der = r27_cpdm_dt_der;
	}
	public BigDecimal getR27_cpdm_dt_dto() {
		return r27_cpdm_dt_dto;
	}
	public void setR27_cpdm_dt_dto(BigDecimal r27_cpdm_dt_dto) {
		this.r27_cpdm_dt_dto = r27_cpdm_dt_dto;
	}
	public BigDecimal getR27_cp() {
		return r27_cp;
	}
	public void setR27_cp(BigDecimal r27_cp) {
		this.r27_cp = r27_cp;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_open_position() {
		return r28_open_position;
	}
	public void setR28_open_position(BigDecimal r28_open_position) {
		this.r28_open_position = r28_open_position;
	}
	public BigDecimal getR28_cpdm_dt_inc() {
		return r28_cpdm_dt_inc;
	}
	public void setR28_cpdm_dt_inc(BigDecimal r28_cpdm_dt_inc) {
		this.r28_cpdm_dt_inc = r28_cpdm_dt_inc;
	}
	public BigDecimal getR28_cpdm_dt_dec() {
		return r28_cpdm_dt_dec;
	}
	public void setR28_cpdm_dt_dec(BigDecimal r28_cpdm_dt_dec) {
		this.r28_cpdm_dt_dec = r28_cpdm_dt_dec;
	}
	public BigDecimal getR28_net() {
		return r28_net;
	}
	public void setR28_net(BigDecimal r28_net) {
		this.r28_net = r28_net;
	}
	public BigDecimal getR28_cpdm_dt_der() {
		return r28_cpdm_dt_der;
	}
	public void setR28_cpdm_dt_der(BigDecimal r28_cpdm_dt_der) {
		this.r28_cpdm_dt_der = r28_cpdm_dt_der;
	}
	public BigDecimal getR28_cpdm_dt_dto() {
		return r28_cpdm_dt_dto;
	}
	public void setR28_cpdm_dt_dto(BigDecimal r28_cpdm_dt_dto) {
		this.r28_cpdm_dt_dto = r28_cpdm_dt_dto;
	}
	public BigDecimal getR28_cp() {
		return r28_cp;
	}
	public void setR28_cp(BigDecimal r28_cp) {
		this.r28_cp = r28_cp;
	}
	public String getR29_product() {
		return r29_product;
	}
	public void setR29_product(String r29_product) {
		this.r29_product = r29_product;
	}
	public BigDecimal getR29_open_position() {
		return r29_open_position;
	}
	public void setR29_open_position(BigDecimal r29_open_position) {
		this.r29_open_position = r29_open_position;
	}
	public BigDecimal getR29_cpdm_dt_inc() {
		return r29_cpdm_dt_inc;
	}
	public void setR29_cpdm_dt_inc(BigDecimal r29_cpdm_dt_inc) {
		this.r29_cpdm_dt_inc = r29_cpdm_dt_inc;
	}
	public BigDecimal getR29_cpdm_dt_dec() {
		return r29_cpdm_dt_dec;
	}
	public void setR29_cpdm_dt_dec(BigDecimal r29_cpdm_dt_dec) {
		this.r29_cpdm_dt_dec = r29_cpdm_dt_dec;
	}
	public BigDecimal getR29_net() {
		return r29_net;
	}
	public void setR29_net(BigDecimal r29_net) {
		this.r29_net = r29_net;
	}
	public BigDecimal getR29_cpdm_dt_der() {
		return r29_cpdm_dt_der;
	}
	public void setR29_cpdm_dt_der(BigDecimal r29_cpdm_dt_der) {
		this.r29_cpdm_dt_der = r29_cpdm_dt_der;
	}
	public BigDecimal getR29_cpdm_dt_dto() {
		return r29_cpdm_dt_dto;
	}
	public void setR29_cpdm_dt_dto(BigDecimal r29_cpdm_dt_dto) {
		this.r29_cpdm_dt_dto = r29_cpdm_dt_dto;
	}
	public BigDecimal getR29_cp() {
		return r29_cp;
	}
	public void setR29_cp(BigDecimal r29_cp) {
		this.r29_cp = r29_cp;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_open_position() {
		return r30_open_position;
	}
	public void setR30_open_position(BigDecimal r30_open_position) {
		this.r30_open_position = r30_open_position;
	}
	public BigDecimal getR30_cpdm_dt_inc() {
		return r30_cpdm_dt_inc;
	}
	public void setR30_cpdm_dt_inc(BigDecimal r30_cpdm_dt_inc) {
		this.r30_cpdm_dt_inc = r30_cpdm_dt_inc;
	}
	public BigDecimal getR30_cpdm_dt_dec() {
		return r30_cpdm_dt_dec;
	}
	public void setR30_cpdm_dt_dec(BigDecimal r30_cpdm_dt_dec) {
		this.r30_cpdm_dt_dec = r30_cpdm_dt_dec;
	}
	public BigDecimal getR30_net() {
		return r30_net;
	}
	public void setR30_net(BigDecimal r30_net) {
		this.r30_net = r30_net;
	}
	public BigDecimal getR30_cpdm_dt_der() {
		return r30_cpdm_dt_der;
	}
	public void setR30_cpdm_dt_der(BigDecimal r30_cpdm_dt_der) {
		this.r30_cpdm_dt_der = r30_cpdm_dt_der;
	}
	public BigDecimal getR30_cpdm_dt_dto() {
		return r30_cpdm_dt_dto;
	}
	public void setR30_cpdm_dt_dto(BigDecimal r30_cpdm_dt_dto) {
		this.r30_cpdm_dt_dto = r30_cpdm_dt_dto;
	}
	public BigDecimal getR30_cp() {
		return r30_cp;
	}
	public void setR30_cp(BigDecimal r30_cp) {
		this.r30_cp = r30_cp;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_open_position() {
		return r31_open_position;
	}
	public void setR31_open_position(BigDecimal r31_open_position) {
		this.r31_open_position = r31_open_position;
	}
	public BigDecimal getR31_cpdm_dt_inc() {
		return r31_cpdm_dt_inc;
	}
	public void setR31_cpdm_dt_inc(BigDecimal r31_cpdm_dt_inc) {
		this.r31_cpdm_dt_inc = r31_cpdm_dt_inc;
	}
	public BigDecimal getR31_cpdm_dt_dec() {
		return r31_cpdm_dt_dec;
	}
	public void setR31_cpdm_dt_dec(BigDecimal r31_cpdm_dt_dec) {
		this.r31_cpdm_dt_dec = r31_cpdm_dt_dec;
	}
	public BigDecimal getR31_net() {
		return r31_net;
	}
	public void setR31_net(BigDecimal r31_net) {
		this.r31_net = r31_net;
	}
	public BigDecimal getR31_cpdm_dt_der() {
		return r31_cpdm_dt_der;
	}
	public void setR31_cpdm_dt_der(BigDecimal r31_cpdm_dt_der) {
		this.r31_cpdm_dt_der = r31_cpdm_dt_der;
	}
	public BigDecimal getR31_cpdm_dt_dto() {
		return r31_cpdm_dt_dto;
	}
	public void setR31_cpdm_dt_dto(BigDecimal r31_cpdm_dt_dto) {
		this.r31_cpdm_dt_dto = r31_cpdm_dt_dto;
	}
	public BigDecimal getR31_cp() {
		return r31_cp;
	}
	public void setR31_cp(BigDecimal r31_cp) {
		this.r31_cp = r31_cp;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_open_position() {
		return r32_open_position;
	}
	public void setR32_open_position(BigDecimal r32_open_position) {
		this.r32_open_position = r32_open_position;
	}
	public BigDecimal getR32_cpdm_dt_inc() {
		return r32_cpdm_dt_inc;
	}
	public void setR32_cpdm_dt_inc(BigDecimal r32_cpdm_dt_inc) {
		this.r32_cpdm_dt_inc = r32_cpdm_dt_inc;
	}
	public BigDecimal getR32_cpdm_dt_dec() {
		return r32_cpdm_dt_dec;
	}
	public void setR32_cpdm_dt_dec(BigDecimal r32_cpdm_dt_dec) {
		this.r32_cpdm_dt_dec = r32_cpdm_dt_dec;
	}
	public BigDecimal getR32_net() {
		return r32_net;
	}
	public void setR32_net(BigDecimal r32_net) {
		this.r32_net = r32_net;
	}
	public BigDecimal getR32_cpdm_dt_der() {
		return r32_cpdm_dt_der;
	}
	public void setR32_cpdm_dt_der(BigDecimal r32_cpdm_dt_der) {
		this.r32_cpdm_dt_der = r32_cpdm_dt_der;
	}
	public BigDecimal getR32_cpdm_dt_dto() {
		return r32_cpdm_dt_dto;
	}
	public void setR32_cpdm_dt_dto(BigDecimal r32_cpdm_dt_dto) {
		this.r32_cpdm_dt_dto = r32_cpdm_dt_dto;
	}
	public BigDecimal getR32_cp() {
		return r32_cp;
	}
	public void setR32_cp(BigDecimal r32_cp) {
		this.r32_cp = r32_cp;
	}
	public String getR33_product() {
		return r33_product;
	}
	public void setR33_product(String r33_product) {
		this.r33_product = r33_product;
	}
	public BigDecimal getR33_open_position() {
		return r33_open_position;
	}
	public void setR33_open_position(BigDecimal r33_open_position) {
		this.r33_open_position = r33_open_position;
	}
	public BigDecimal getR33_cpdm_dt_inc() {
		return r33_cpdm_dt_inc;
	}
	public void setR33_cpdm_dt_inc(BigDecimal r33_cpdm_dt_inc) {
		this.r33_cpdm_dt_inc = r33_cpdm_dt_inc;
	}
	public BigDecimal getR33_cpdm_dt_dec() {
		return r33_cpdm_dt_dec;
	}
	public void setR33_cpdm_dt_dec(BigDecimal r33_cpdm_dt_dec) {
		this.r33_cpdm_dt_dec = r33_cpdm_dt_dec;
	}
	public BigDecimal getR33_net() {
		return r33_net;
	}
	public void setR33_net(BigDecimal r33_net) {
		this.r33_net = r33_net;
	}
	public BigDecimal getR33_cpdm_dt_der() {
		return r33_cpdm_dt_der;
	}
	public void setR33_cpdm_dt_der(BigDecimal r33_cpdm_dt_der) {
		this.r33_cpdm_dt_der = r33_cpdm_dt_der;
	}
	public BigDecimal getR33_cpdm_dt_dto() {
		return r33_cpdm_dt_dto;
	}
	public void setR33_cpdm_dt_dto(BigDecimal r33_cpdm_dt_dto) {
		this.r33_cpdm_dt_dto = r33_cpdm_dt_dto;
	}
	public BigDecimal getR33_cp() {
		return r33_cp;
	}
	public void setR33_cp(BigDecimal r33_cp) {
		this.r33_cp = r33_cp;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_open_position() {
		return r34_open_position;
	}
	public void setR34_open_position(BigDecimal r34_open_position) {
		this.r34_open_position = r34_open_position;
	}
	public BigDecimal getR34_cpdm_dt_inc() {
		return r34_cpdm_dt_inc;
	}
	public void setR34_cpdm_dt_inc(BigDecimal r34_cpdm_dt_inc) {
		this.r34_cpdm_dt_inc = r34_cpdm_dt_inc;
	}
	public BigDecimal getR34_cpdm_dt_dec() {
		return r34_cpdm_dt_dec;
	}
	public void setR34_cpdm_dt_dec(BigDecimal r34_cpdm_dt_dec) {
		this.r34_cpdm_dt_dec = r34_cpdm_dt_dec;
	}
	public BigDecimal getR34_net() {
		return r34_net;
	}
	public void setR34_net(BigDecimal r34_net) {
		this.r34_net = r34_net;
	}
	public BigDecimal getR34_cpdm_dt_der() {
		return r34_cpdm_dt_der;
	}
	public void setR34_cpdm_dt_der(BigDecimal r34_cpdm_dt_der) {
		this.r34_cpdm_dt_der = r34_cpdm_dt_der;
	}
	public BigDecimal getR34_cpdm_dt_dto() {
		return r34_cpdm_dt_dto;
	}
	public void setR34_cpdm_dt_dto(BigDecimal r34_cpdm_dt_dto) {
		this.r34_cpdm_dt_dto = r34_cpdm_dt_dto;
	}
	public BigDecimal getR34_cp() {
		return r34_cp;
	}
	public void setR34_cp(BigDecimal r34_cp) {
		this.r34_cp = r34_cp;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_open_position() {
		return r35_open_position;
	}
	public void setR35_open_position(BigDecimal r35_open_position) {
		this.r35_open_position = r35_open_position;
	}
	public BigDecimal getR35_cpdm_dt_inc() {
		return r35_cpdm_dt_inc;
	}
	public void setR35_cpdm_dt_inc(BigDecimal r35_cpdm_dt_inc) {
		this.r35_cpdm_dt_inc = r35_cpdm_dt_inc;
	}
	public BigDecimal getR35_cpdm_dt_dec() {
		return r35_cpdm_dt_dec;
	}
	public void setR35_cpdm_dt_dec(BigDecimal r35_cpdm_dt_dec) {
		this.r35_cpdm_dt_dec = r35_cpdm_dt_dec;
	}
	public BigDecimal getR35_net() {
		return r35_net;
	}
	public void setR35_net(BigDecimal r35_net) {
		this.r35_net = r35_net;
	}
	public BigDecimal getR35_cpdm_dt_der() {
		return r35_cpdm_dt_der;
	}
	public void setR35_cpdm_dt_der(BigDecimal r35_cpdm_dt_der) {
		this.r35_cpdm_dt_der = r35_cpdm_dt_der;
	}
	public BigDecimal getR35_cpdm_dt_dto() {
		return r35_cpdm_dt_dto;
	}
	public void setR35_cpdm_dt_dto(BigDecimal r35_cpdm_dt_dto) {
		this.r35_cpdm_dt_dto = r35_cpdm_dt_dto;
	}
	public BigDecimal getR35_cp() {
		return r35_cp;
	}
	public void setR35_cp(BigDecimal r35_cp) {
		this.r35_cp = r35_cp;
	}
	public String getR36_product() {
		return r36_product;
	}
	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}
	public BigDecimal getR36_open_position() {
		return r36_open_position;
	}
	public void setR36_open_position(BigDecimal r36_open_position) {
		this.r36_open_position = r36_open_position;
	}
	public BigDecimal getR36_cpdm_dt_inc() {
		return r36_cpdm_dt_inc;
	}
	public void setR36_cpdm_dt_inc(BigDecimal r36_cpdm_dt_inc) {
		this.r36_cpdm_dt_inc = r36_cpdm_dt_inc;
	}
	public BigDecimal getR36_cpdm_dt_dec() {
		return r36_cpdm_dt_dec;
	}
	public void setR36_cpdm_dt_dec(BigDecimal r36_cpdm_dt_dec) {
		this.r36_cpdm_dt_dec = r36_cpdm_dt_dec;
	}
	public BigDecimal getR36_net() {
		return r36_net;
	}
	public void setR36_net(BigDecimal r36_net) {
		this.r36_net = r36_net;
	}
	public BigDecimal getR36_cpdm_dt_der() {
		return r36_cpdm_dt_der;
	}
	public void setR36_cpdm_dt_der(BigDecimal r36_cpdm_dt_der) {
		this.r36_cpdm_dt_der = r36_cpdm_dt_der;
	}
	public BigDecimal getR36_cpdm_dt_dto() {
		return r36_cpdm_dt_dto;
	}
	public void setR36_cpdm_dt_dto(BigDecimal r36_cpdm_dt_dto) {
		this.r36_cpdm_dt_dto = r36_cpdm_dt_dto;
	}
	public BigDecimal getR36_cp() {
		return r36_cp;
	}
	public void setR36_cp(BigDecimal r36_cp) {
		this.r36_cp = r36_cp;
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


public class M_BOP_Archival_Summary_RowMapper
        implements RowMapper<M_BOP_Archival_Summary_Entity> {

    @Override
    public M_BOP_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        M_BOP_Archival_Summary_Entity obj = new M_BOP_Archival_Summary_Entity();

// =========================
// R13
// =========================
obj.setR13_product(rs.getString("r13_product"));
obj.setR13_open_position(rs.getBigDecimal("r13_open_position"));
obj.setR13_cpdm_dt_inc(rs.getBigDecimal("r13_cpdm_dt_inc"));
obj.setR13_cpdm_dt_dec(rs.getBigDecimal("r13_cpdm_dt_dec"));
obj.setR13_net(rs.getBigDecimal("r13_net"));
obj.setR13_cpdm_dt_der(rs.getBigDecimal("r13_cpdm_dt_der"));
obj.setR13_cpdm_dt_dto(rs.getBigDecimal("r13_cpdm_dt_dto"));
obj.setR13_cp(rs.getBigDecimal("r13_cp"));


// =========================
// R14
// =========================
obj.setR14_product(rs.getString("r14_product"));
obj.setR14_open_position(rs.getBigDecimal("r14_open_position"));
obj.setR14_cpdm_dt_inc(rs.getBigDecimal("r14_cpdm_dt_inc"));
obj.setR14_cpdm_dt_dec(rs.getBigDecimal("r14_cpdm_dt_dec"));
obj.setR14_net(rs.getBigDecimal("r14_net"));
obj.setR14_cpdm_dt_der(rs.getBigDecimal("r14_cpdm_dt_der"));
obj.setR14_cpdm_dt_dto(rs.getBigDecimal("r14_cpdm_dt_dto"));
obj.setR14_cp(rs.getBigDecimal("r14_cp"));

// =========================
// R15
// =========================
obj.setR15_product(rs.getString("r15_product"));
obj.setR15_open_position(rs.getBigDecimal("r15_open_position"));
obj.setR15_cpdm_dt_inc(rs.getBigDecimal("r15_cpdm_dt_inc"));
obj.setR15_cpdm_dt_dec(rs.getBigDecimal("r15_cpdm_dt_dec"));
obj.setR15_net(rs.getBigDecimal("r15_net"));
obj.setR15_cpdm_dt_der(rs.getBigDecimal("r15_cpdm_dt_der"));
obj.setR15_cpdm_dt_dto(rs.getBigDecimal("r15_cpdm_dt_dto"));
obj.setR15_cp(rs.getBigDecimal("r15_cp"));

// =========================
// R16
// =========================
obj.setR16_product(rs.getString("r16_product"));
obj.setR16_open_position(rs.getBigDecimal("r16_open_position"));
obj.setR16_cpdm_dt_inc(rs.getBigDecimal("r16_cpdm_dt_inc"));
obj.setR16_cpdm_dt_dec(rs.getBigDecimal("r16_cpdm_dt_dec"));
obj.setR16_net(rs.getBigDecimal("r16_net"));
obj.setR16_cpdm_dt_der(rs.getBigDecimal("r16_cpdm_dt_der"));
obj.setR16_cpdm_dt_dto(rs.getBigDecimal("r16_cpdm_dt_dto"));
obj.setR16_cp(rs.getBigDecimal("r16_cp"));

// =========================
// R17
// =========================
obj.setR17_product(rs.getString("r17_product"));
obj.setR17_open_position(rs.getBigDecimal("r17_open_position"));
obj.setR17_cpdm_dt_inc(rs.getBigDecimal("r17_cpdm_dt_inc"));
obj.setR17_cpdm_dt_dec(rs.getBigDecimal("r17_cpdm_dt_dec"));
obj.setR17_net(rs.getBigDecimal("r17_net"));
obj.setR17_cpdm_dt_der(rs.getBigDecimal("r17_cpdm_dt_der"));
obj.setR17_cpdm_dt_dto(rs.getBigDecimal("r17_cpdm_dt_dto"));
obj.setR17_cp(rs.getBigDecimal("r17_cp"));

// =========================
// R18
// =========================
obj.setR18_product(rs.getString("r18_product"));
obj.setR18_open_position(rs.getBigDecimal("r18_open_position"));
obj.setR18_cpdm_dt_inc(rs.getBigDecimal("r18_cpdm_dt_inc"));
obj.setR18_cpdm_dt_dec(rs.getBigDecimal("r18_cpdm_dt_dec"));
obj.setR18_net(rs.getBigDecimal("r18_net"));
obj.setR18_cpdm_dt_der(rs.getBigDecimal("r18_cpdm_dt_der"));
obj.setR18_cpdm_dt_dto(rs.getBigDecimal("r18_cpdm_dt_dto"));
obj.setR18_cp(rs.getBigDecimal("r18_cp"));

// =========================
// R19
// =========================
obj.setR19_product(rs.getString("r19_product"));
obj.setR19_open_position(rs.getBigDecimal("r19_open_position"));
obj.setR19_cpdm_dt_inc(rs.getBigDecimal("r19_cpdm_dt_inc"));
obj.setR19_cpdm_dt_dec(rs.getBigDecimal("r19_cpdm_dt_dec"));
obj.setR19_net(rs.getBigDecimal("r19_net"));
obj.setR19_cpdm_dt_der(rs.getBigDecimal("r19_cpdm_dt_der"));
obj.setR19_cpdm_dt_dto(rs.getBigDecimal("r19_cpdm_dt_dto"));
obj.setR19_cp(rs.getBigDecimal("r19_cp"));

// =========================
// R20
// =========================
obj.setR20_product(rs.getString("r20_product"));
obj.setR20_open_position(rs.getBigDecimal("r20_open_position"));
obj.setR20_cpdm_dt_inc(rs.getBigDecimal("r20_cpdm_dt_inc"));
obj.setR20_cpdm_dt_dec(rs.getBigDecimal("r20_cpdm_dt_dec"));
obj.setR20_net(rs.getBigDecimal("r20_net"));
obj.setR20_cpdm_dt_der(rs.getBigDecimal("r20_cpdm_dt_der"));
obj.setR20_cpdm_dt_dto(rs.getBigDecimal("r20_cpdm_dt_dto"));
obj.setR20_cp(rs.getBigDecimal("r20_cp"));


// =========================
// R21
// =========================
obj.setR21_product(rs.getString("r21_product"));
obj.setR21_open_position(rs.getBigDecimal("r21_open_position"));
obj.setR21_cpdm_dt_inc(rs.getBigDecimal("r21_cpdm_dt_inc"));
obj.setR21_cpdm_dt_dec(rs.getBigDecimal("r21_cpdm_dt_dec"));
obj.setR21_net(rs.getBigDecimal("r21_net"));
obj.setR21_cpdm_dt_der(rs.getBigDecimal("r21_cpdm_dt_der"));
obj.setR21_cpdm_dt_dto(rs.getBigDecimal("r21_cpdm_dt_dto"));
obj.setR21_cp(rs.getBigDecimal("r21_cp"));

// =========================
// R22
// =========================
obj.setR22_product(rs.getString("r22_product"));
obj.setR22_open_position(rs.getBigDecimal("r22_open_position"));
obj.setR22_cpdm_dt_inc(rs.getBigDecimal("r22_cpdm_dt_inc"));
obj.setR22_cpdm_dt_dec(rs.getBigDecimal("r22_cpdm_dt_dec"));
obj.setR22_net(rs.getBigDecimal("r22_net"));
obj.setR22_cpdm_dt_der(rs.getBigDecimal("r22_cpdm_dt_der"));
obj.setR22_cpdm_dt_dto(rs.getBigDecimal("r22_cpdm_dt_dto"));
obj.setR22_cp(rs.getBigDecimal("r22_cp"));

// =========================
// R23
// =========================
obj.setR23_product(rs.getString("r23_product"));
obj.setR23_open_position(rs.getBigDecimal("r23_open_position"));
obj.setR23_cpdm_dt_inc(rs.getBigDecimal("r23_cpdm_dt_inc"));
obj.setR23_cpdm_dt_dec(rs.getBigDecimal("r23_cpdm_dt_dec"));
obj.setR23_net(rs.getBigDecimal("r23_net"));
obj.setR23_cpdm_dt_der(rs.getBigDecimal("r23_cpdm_dt_der"));
obj.setR23_cpdm_dt_dto(rs.getBigDecimal("r23_cpdm_dt_dto"));
obj.setR23_cp(rs.getBigDecimal("r23_cp"));

// =========================
// R24
// =========================
obj.setR24_product(rs.getString("r24_product"));
obj.setR24_open_position(rs.getBigDecimal("r24_open_position"));
obj.setR24_cpdm_dt_inc(rs.getBigDecimal("r24_cpdm_dt_inc"));
obj.setR24_cpdm_dt_dec(rs.getBigDecimal("r24_cpdm_dt_dec"));
obj.setR24_net(rs.getBigDecimal("r24_net"));
obj.setR24_cpdm_dt_der(rs.getBigDecimal("r24_cpdm_dt_der"));
obj.setR24_cpdm_dt_dto(rs.getBigDecimal("r24_cpdm_dt_dto"));
obj.setR24_cp(rs.getBigDecimal("r24_cp"));

// =========================
// R25
// =========================
obj.setR25_product(rs.getString("r25_product"));
obj.setR25_open_position(rs.getBigDecimal("r25_open_position"));
obj.setR25_cpdm_dt_inc(rs.getBigDecimal("r25_cpdm_dt_inc"));
obj.setR25_cpdm_dt_dec(rs.getBigDecimal("r25_cpdm_dt_dec"));
obj.setR25_net(rs.getBigDecimal("r25_net"));
obj.setR25_cpdm_dt_der(rs.getBigDecimal("r25_cpdm_dt_der"));
obj.setR25_cpdm_dt_dto(rs.getBigDecimal("r25_cpdm_dt_dto"));
obj.setR25_cp(rs.getBigDecimal("r25_cp"));

// =========================
// R26
// =========================
obj.setR26_product(rs.getString("r26_product"));
obj.setR26_open_position(rs.getBigDecimal("r26_open_position"));
obj.setR26_cpdm_dt_inc(rs.getBigDecimal("r26_cpdm_dt_inc"));
obj.setR26_cpdm_dt_dec(rs.getBigDecimal("r26_cpdm_dt_dec"));
obj.setR26_net(rs.getBigDecimal("r26_net"));
obj.setR26_cpdm_dt_der(rs.getBigDecimal("r26_cpdm_dt_der"));
obj.setR26_cpdm_dt_dto(rs.getBigDecimal("r26_cpdm_dt_dto"));
obj.setR26_cp(rs.getBigDecimal("r26_cp"));

// =========================
// R27
// =========================
obj.setR27_product(rs.getString("r27_product"));
obj.setR27_open_position(rs.getBigDecimal("r27_open_position"));
obj.setR27_cpdm_dt_inc(rs.getBigDecimal("r27_cpdm_dt_inc"));
obj.setR27_cpdm_dt_dec(rs.getBigDecimal("r27_cpdm_dt_dec"));
obj.setR27_net(rs.getBigDecimal("r27_net"));
obj.setR27_cpdm_dt_der(rs.getBigDecimal("r27_cpdm_dt_der"));
obj.setR27_cpdm_dt_dto(rs.getBigDecimal("r27_cpdm_dt_dto"));
obj.setR27_cp(rs.getBigDecimal("r27_cp"));

// =========================
// R28
// =========================
obj.setR28_product(rs.getString("r28_product"));
obj.setR28_open_position(rs.getBigDecimal("r28_open_position"));
obj.setR28_cpdm_dt_inc(rs.getBigDecimal("r28_cpdm_dt_inc"));
obj.setR28_cpdm_dt_dec(rs.getBigDecimal("r28_cpdm_dt_dec"));
obj.setR28_net(rs.getBigDecimal("r28_net"));
obj.setR28_cpdm_dt_der(rs.getBigDecimal("r28_cpdm_dt_der"));
obj.setR28_cpdm_dt_dto(rs.getBigDecimal("r28_cpdm_dt_dto"));
obj.setR28_cp(rs.getBigDecimal("r28_cp"));

// =========================
// R29
// =========================
obj.setR29_product(rs.getString("r29_product"));
obj.setR29_open_position(rs.getBigDecimal("r29_open_position"));
obj.setR29_cpdm_dt_inc(rs.getBigDecimal("r29_cpdm_dt_inc"));
obj.setR29_cpdm_dt_dec(rs.getBigDecimal("r29_cpdm_dt_dec"));
obj.setR29_net(rs.getBigDecimal("r29_net"));
obj.setR29_cpdm_dt_der(rs.getBigDecimal("r29_cpdm_dt_der"));
obj.setR29_cpdm_dt_dto(rs.getBigDecimal("r29_cpdm_dt_dto"));
obj.setR29_cp(rs.getBigDecimal("r29_cp"));

// =========================
// R30
// =========================
obj.setR30_product(rs.getString("r30_product"));
obj.setR30_open_position(rs.getBigDecimal("r30_open_position"));
obj.setR30_cpdm_dt_inc(rs.getBigDecimal("r30_cpdm_dt_inc"));
obj.setR30_cpdm_dt_dec(rs.getBigDecimal("r30_cpdm_dt_dec"));
obj.setR30_net(rs.getBigDecimal("r30_net"));
obj.setR30_cpdm_dt_der(rs.getBigDecimal("r30_cpdm_dt_der"));
obj.setR30_cpdm_dt_dto(rs.getBigDecimal("r30_cpdm_dt_dto"));
obj.setR30_cp(rs.getBigDecimal("r30_cp"));

// =========================
// R31
// =========================
obj.setR31_product(rs.getString("r31_product"));
obj.setR31_open_position(rs.getBigDecimal("r31_open_position"));
obj.setR31_cpdm_dt_inc(rs.getBigDecimal("r31_cpdm_dt_inc"));
obj.setR31_cpdm_dt_dec(rs.getBigDecimal("r31_cpdm_dt_dec"));
obj.setR31_net(rs.getBigDecimal("r31_net"));
obj.setR31_cpdm_dt_der(rs.getBigDecimal("r31_cpdm_dt_der"));
obj.setR31_cpdm_dt_dto(rs.getBigDecimal("r31_cpdm_dt_dto"));
obj.setR31_cp(rs.getBigDecimal("r31_cp"));

// =========================
// R32
// =========================
obj.setR32_product(rs.getString("r32_product"));
obj.setR32_open_position(rs.getBigDecimal("r32_open_position"));
obj.setR32_cpdm_dt_inc(rs.getBigDecimal("r32_cpdm_dt_inc"));
obj.setR32_cpdm_dt_dec(rs.getBigDecimal("r32_cpdm_dt_dec"));
obj.setR32_net(rs.getBigDecimal("r32_net"));
obj.setR32_cpdm_dt_der(rs.getBigDecimal("r32_cpdm_dt_der"));
obj.setR32_cpdm_dt_dto(rs.getBigDecimal("r32_cpdm_dt_dto"));
obj.setR32_cp(rs.getBigDecimal("r32_cp"));

// =========================
// R33
// =========================
obj.setR33_product(rs.getString("r33_product"));
obj.setR33_open_position(rs.getBigDecimal("r33_open_position"));
obj.setR33_cpdm_dt_inc(rs.getBigDecimal("r33_cpdm_dt_inc"));
obj.setR33_cpdm_dt_dec(rs.getBigDecimal("r33_cpdm_dt_dec"));
obj.setR33_net(rs.getBigDecimal("r33_net"));
obj.setR33_cpdm_dt_der(rs.getBigDecimal("r33_cpdm_dt_der"));
obj.setR33_cpdm_dt_dto(rs.getBigDecimal("r33_cpdm_dt_dto"));
obj.setR33_cp(rs.getBigDecimal("r33_cp"));

// =========================
// R34
// =========================
obj.setR34_product(rs.getString("r34_product"));
obj.setR34_open_position(rs.getBigDecimal("r34_open_position"));
obj.setR34_cpdm_dt_inc(rs.getBigDecimal("r34_cpdm_dt_inc"));
obj.setR34_cpdm_dt_dec(rs.getBigDecimal("r34_cpdm_dt_dec"));
obj.setR34_net(rs.getBigDecimal("r34_net"));
obj.setR34_cpdm_dt_der(rs.getBigDecimal("r34_cpdm_dt_der"));
obj.setR34_cpdm_dt_dto(rs.getBigDecimal("r34_cpdm_dt_dto"));
obj.setR34_cp(rs.getBigDecimal("r34_cp"));

// =========================
// R35
// =========================
obj.setR35_product(rs.getString("r35_product"));
obj.setR35_open_position(rs.getBigDecimal("r35_open_position"));
obj.setR35_cpdm_dt_inc(rs.getBigDecimal("r35_cpdm_dt_inc"));
obj.setR35_cpdm_dt_dec(rs.getBigDecimal("r35_cpdm_dt_dec"));
obj.setR35_net(rs.getBigDecimal("r35_net"));
obj.setR35_cpdm_dt_der(rs.getBigDecimal("r35_cpdm_dt_der"));
obj.setR35_cpdm_dt_dto(rs.getBigDecimal("r35_cpdm_dt_dto"));
obj.setR35_cp(rs.getBigDecimal("r35_cp"));

// =========================
// R36
// =========================
obj.setR36_product(rs.getString("r36_product"));
obj.setR36_open_position(rs.getBigDecimal("r36_open_position"));
obj.setR36_cpdm_dt_inc(rs.getBigDecimal("r36_cpdm_dt_inc"));
obj.setR36_cpdm_dt_dec(rs.getBigDecimal("r36_cpdm_dt_dec"));
obj.setR36_net(rs.getBigDecimal("r36_net"));
obj.setR36_cpdm_dt_der(rs.getBigDecimal("r36_cpdm_dt_der"));
obj.setR36_cpdm_dt_dto(rs.getBigDecimal("r36_cpdm_dt_dto"));
obj.setR36_cp(rs.getBigDecimal("r36_cp"));

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


public class M_BOP_Archival_Summary_Entity {
	
	
private String	r13_product;
	private BigDecimal	r13_open_position;
	private BigDecimal	r13_cpdm_dt_inc;
	private BigDecimal	r13_cpdm_dt_dec;
	private BigDecimal	r13_net;
	private BigDecimal	r13_cpdm_dt_der;
	private BigDecimal	r13_cpdm_dt_dto;
	private BigDecimal	r13_cp;
	private String	r14_product;
	private BigDecimal	r14_open_position;
	private BigDecimal	r14_cpdm_dt_inc;
	private BigDecimal	r14_cpdm_dt_dec;
	private BigDecimal	r14_net;
	private BigDecimal	r14_cpdm_dt_der;
	private BigDecimal	r14_cpdm_dt_dto;
	private BigDecimal	r14_cp;
	private String	r15_product;
	private BigDecimal	r15_open_position;
	private BigDecimal	r15_cpdm_dt_inc;
	private BigDecimal	r15_cpdm_dt_dec;
	private BigDecimal	r15_net;
	private BigDecimal	r15_cpdm_dt_der;
	private BigDecimal	r15_cpdm_dt_dto;
	private BigDecimal	r15_cp;
	private String	r16_product;
	private BigDecimal	r16_open_position;
	private BigDecimal	r16_cpdm_dt_inc;
	private BigDecimal	r16_cpdm_dt_dec;
	private BigDecimal	r16_net;
	private BigDecimal	r16_cpdm_dt_der;
	private BigDecimal	r16_cpdm_dt_dto;
	private BigDecimal	r16_cp;
	private String	r17_product;
	private BigDecimal	r17_open_position;
	private BigDecimal	r17_cpdm_dt_inc;
	private BigDecimal	r17_cpdm_dt_dec;
	private BigDecimal	r17_net;
	private BigDecimal	r17_cpdm_dt_der;
	private BigDecimal	r17_cpdm_dt_dto;
	private BigDecimal	r17_cp;
	private String	r18_product;
	private BigDecimal	r18_open_position;
	private BigDecimal	r18_cpdm_dt_inc;
	private BigDecimal	r18_cpdm_dt_dec;
	private BigDecimal	r18_net;
	private BigDecimal	r18_cpdm_dt_der;
	private BigDecimal	r18_cpdm_dt_dto;
	private BigDecimal	r18_cp;
	private String	r19_product;
	private BigDecimal	r19_open_position;
	private BigDecimal	r19_cpdm_dt_inc;
	private BigDecimal	r19_cpdm_dt_dec;
	private BigDecimal	r19_net;
	private BigDecimal	r19_cpdm_dt_der;
	private BigDecimal	r19_cpdm_dt_dto;
	private BigDecimal	r19_cp;
	private String	r20_product;
	private BigDecimal	r20_open_position;
	private BigDecimal	r20_cpdm_dt_inc;
	private BigDecimal	r20_cpdm_dt_dec;
	private BigDecimal	r20_net;
	private BigDecimal	r20_cpdm_dt_der;
	private BigDecimal	r20_cpdm_dt_dto;
	private BigDecimal	r20_cp;
	private String	r21_product;
	private BigDecimal	r21_open_position;
	private BigDecimal	r21_cpdm_dt_inc;
	private BigDecimal	r21_cpdm_dt_dec;
	private BigDecimal	r21_net;
	private BigDecimal	r21_cpdm_dt_der;
	private BigDecimal	r21_cpdm_dt_dto;
	private BigDecimal	r21_cp;
	private String	r22_product;
	private BigDecimal	r22_open_position;
	private BigDecimal	r22_cpdm_dt_inc;
	private BigDecimal	r22_cpdm_dt_dec;
	private BigDecimal	r22_net;
	private BigDecimal	r22_cpdm_dt_der;
	private BigDecimal	r22_cpdm_dt_dto;
	private BigDecimal	r22_cp;
	private String	r23_product;
	private BigDecimal	r23_open_position;
	private BigDecimal	r23_cpdm_dt_inc;
	private BigDecimal	r23_cpdm_dt_dec;
	private BigDecimal	r23_net;
	private BigDecimal	r23_cpdm_dt_der;
	private BigDecimal	r23_cpdm_dt_dto;
	private BigDecimal	r23_cp;
	private String	r24_product;
	private BigDecimal	r24_open_position;
	private BigDecimal	r24_cpdm_dt_inc;
	private BigDecimal	r24_cpdm_dt_dec;
	private BigDecimal	r24_net;
	private BigDecimal	r24_cpdm_dt_der;
	private BigDecimal	r24_cpdm_dt_dto;
	private BigDecimal	r24_cp;
	private String	r25_product;
	private BigDecimal	r25_open_position;
	private BigDecimal	r25_cpdm_dt_inc;
	private BigDecimal	r25_cpdm_dt_dec;
	private BigDecimal	r25_net;
	private BigDecimal	r25_cpdm_dt_der;
	private BigDecimal	r25_cpdm_dt_dto;
	private BigDecimal	r25_cp;
	private String	r26_product;
	private BigDecimal	r26_open_position;
	private BigDecimal	r26_cpdm_dt_inc;
	private BigDecimal	r26_cpdm_dt_dec;
	private BigDecimal	r26_net;
	private BigDecimal	r26_cpdm_dt_der;
	private BigDecimal	r26_cpdm_dt_dto;
	private BigDecimal	r26_cp;
	private String	r27_product;
	private BigDecimal	r27_open_position;
	private BigDecimal	r27_cpdm_dt_inc;
	private BigDecimal	r27_cpdm_dt_dec;
	private BigDecimal	r27_net;
	private BigDecimal	r27_cpdm_dt_der;
	private BigDecimal	r27_cpdm_dt_dto;
	private BigDecimal	r27_cp;
	private String	r28_product;
	private BigDecimal	r28_open_position;
	private BigDecimal	r28_cpdm_dt_inc;
	private BigDecimal	r28_cpdm_dt_dec;
	private BigDecimal	r28_net;
	private BigDecimal	r28_cpdm_dt_der;
	private BigDecimal	r28_cpdm_dt_dto;
	private BigDecimal	r28_cp;
	private String	r29_product;
	private BigDecimal	r29_open_position;
	private BigDecimal	r29_cpdm_dt_inc;
	private BigDecimal	r29_cpdm_dt_dec;
	private BigDecimal	r29_net;
	private BigDecimal	r29_cpdm_dt_der;
	private BigDecimal	r29_cpdm_dt_dto;
	private BigDecimal	r29_cp;
	private String	r30_product;
	private BigDecimal	r30_open_position;
	private BigDecimal	r30_cpdm_dt_inc;
	private BigDecimal	r30_cpdm_dt_dec;
	private BigDecimal	r30_net;
	private BigDecimal	r30_cpdm_dt_der;
	private BigDecimal	r30_cpdm_dt_dto;
	private BigDecimal	r30_cp;
	private String	r31_product;
	private BigDecimal	r31_open_position;
	private BigDecimal	r31_cpdm_dt_inc;
	private BigDecimal	r31_cpdm_dt_dec;
	private BigDecimal	r31_net;
	private BigDecimal	r31_cpdm_dt_der;
	private BigDecimal	r31_cpdm_dt_dto;
	private BigDecimal	r31_cp;
	private String	r32_product;
	private BigDecimal	r32_open_position;
	private BigDecimal	r32_cpdm_dt_inc;
	private BigDecimal	r32_cpdm_dt_dec;
	private BigDecimal	r32_net;
	private BigDecimal	r32_cpdm_dt_der;
	private BigDecimal	r32_cpdm_dt_dto;
	private BigDecimal	r32_cp;
	private String	r33_product;
	private BigDecimal	r33_open_position;
	private BigDecimal	r33_cpdm_dt_inc;
	private BigDecimal	r33_cpdm_dt_dec;
	private BigDecimal	r33_net;
	private BigDecimal	r33_cpdm_dt_der;
	private BigDecimal	r33_cpdm_dt_dto;
	private BigDecimal	r33_cp;
	private String	r34_product;
	private BigDecimal	r34_open_position;
	private BigDecimal	r34_cpdm_dt_inc;
	private BigDecimal	r34_cpdm_dt_dec;
	private BigDecimal	r34_net;
	private BigDecimal	r34_cpdm_dt_der;
	private BigDecimal	r34_cpdm_dt_dto;
	private BigDecimal	r34_cp;
	private String	r35_product;
	private BigDecimal	r35_open_position;
	private BigDecimal	r35_cpdm_dt_inc;
	private BigDecimal	r35_cpdm_dt_dec;
	private BigDecimal	r35_net;
	private BigDecimal	r35_cpdm_dt_der;
	private BigDecimal	r35_cpdm_dt_dto;
	private BigDecimal	r35_cp;
	private String	r36_product;
	private BigDecimal	r36_open_position;
	private BigDecimal	r36_cpdm_dt_inc;
	private BigDecimal	r36_cpdm_dt_dec;
	private BigDecimal	r36_net;
	private BigDecimal	r36_cpdm_dt_der;
	private BigDecimal	r36_cpdm_dt_dto;
	private BigDecimal	r36_cp;
	               
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
	
public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_open_position() {
		return r13_open_position;
	}
	public void setR13_open_position(BigDecimal r13_open_position) {
		this.r13_open_position = r13_open_position;
	}
	public BigDecimal getR13_cpdm_dt_inc() {
		return r13_cpdm_dt_inc;
	}
	public void setR13_cpdm_dt_inc(BigDecimal r13_cpdm_dt_inc) {
		this.r13_cpdm_dt_inc = r13_cpdm_dt_inc;
	}
	public BigDecimal getR13_cpdm_dt_dec() {
		return r13_cpdm_dt_dec;
	}
	public void setR13_cpdm_dt_dec(BigDecimal r13_cpdm_dt_dec) {
		this.r13_cpdm_dt_dec = r13_cpdm_dt_dec;
	}
	public BigDecimal getR13_net() {
		return r13_net;
	}
	public void setR13_net(BigDecimal r13_net) {
		this.r13_net = r13_net;
	}
	public BigDecimal getR13_cpdm_dt_der() {
		return r13_cpdm_dt_der;
	}
	public void setR13_cpdm_dt_der(BigDecimal r13_cpdm_dt_der) {
		this.r13_cpdm_dt_der = r13_cpdm_dt_der;
	}
	public BigDecimal getR13_cpdm_dt_dto() {
		return r13_cpdm_dt_dto;
	}
	public void setR13_cpdm_dt_dto(BigDecimal r13_cpdm_dt_dto) {
		this.r13_cpdm_dt_dto = r13_cpdm_dt_dto;
	}
	public BigDecimal getR13_cp() {
		return r13_cp;
	}
	public void setR13_cp(BigDecimal r13_cp) {
		this.r13_cp = r13_cp;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_open_position() {
		return r14_open_position;
	}
	public void setR14_open_position(BigDecimal r14_open_position) {
		this.r14_open_position = r14_open_position;
	}
	public BigDecimal getR14_cpdm_dt_inc() {
		return r14_cpdm_dt_inc;
	}
	public void setR14_cpdm_dt_inc(BigDecimal r14_cpdm_dt_inc) {
		this.r14_cpdm_dt_inc = r14_cpdm_dt_inc;
	}
	public BigDecimal getR14_cpdm_dt_dec() {
		return r14_cpdm_dt_dec;
	}
	public void setR14_cpdm_dt_dec(BigDecimal r14_cpdm_dt_dec) {
		this.r14_cpdm_dt_dec = r14_cpdm_dt_dec;
	}
	public BigDecimal getR14_net() {
		return r14_net;
	}
	public void setR14_net(BigDecimal r14_net) {
		this.r14_net = r14_net;
	}
	public BigDecimal getR14_cpdm_dt_der() {
		return r14_cpdm_dt_der;
	}
	public void setR14_cpdm_dt_der(BigDecimal r14_cpdm_dt_der) {
		this.r14_cpdm_dt_der = r14_cpdm_dt_der;
	}
	public BigDecimal getR14_cpdm_dt_dto() {
		return r14_cpdm_dt_dto;
	}
	public void setR14_cpdm_dt_dto(BigDecimal r14_cpdm_dt_dto) {
		this.r14_cpdm_dt_dto = r14_cpdm_dt_dto;
	}
	public BigDecimal getR14_cp() {
		return r14_cp;
	}
	public void setR14_cp(BigDecimal r14_cp) {
		this.r14_cp = r14_cp;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_open_position() {
		return r15_open_position;
	}
	public void setR15_open_position(BigDecimal r15_open_position) {
		this.r15_open_position = r15_open_position;
	}
	public BigDecimal getR15_cpdm_dt_inc() {
		return r15_cpdm_dt_inc;
	}
	public void setR15_cpdm_dt_inc(BigDecimal r15_cpdm_dt_inc) {
		this.r15_cpdm_dt_inc = r15_cpdm_dt_inc;
	}
	public BigDecimal getR15_cpdm_dt_dec() {
		return r15_cpdm_dt_dec;
	}
	public void setR15_cpdm_dt_dec(BigDecimal r15_cpdm_dt_dec) {
		this.r15_cpdm_dt_dec = r15_cpdm_dt_dec;
	}
	public BigDecimal getR15_net() {
		return r15_net;
	}
	public void setR15_net(BigDecimal r15_net) {
		this.r15_net = r15_net;
	}
	public BigDecimal getR15_cpdm_dt_der() {
		return r15_cpdm_dt_der;
	}
	public void setR15_cpdm_dt_der(BigDecimal r15_cpdm_dt_der) {
		this.r15_cpdm_dt_der = r15_cpdm_dt_der;
	}
	public BigDecimal getR15_cpdm_dt_dto() {
		return r15_cpdm_dt_dto;
	}
	public void setR15_cpdm_dt_dto(BigDecimal r15_cpdm_dt_dto) {
		this.r15_cpdm_dt_dto = r15_cpdm_dt_dto;
	}
	public BigDecimal getR15_cp() {
		return r15_cp;
	}
	public void setR15_cp(BigDecimal r15_cp) {
		this.r15_cp = r15_cp;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_open_position() {
		return r16_open_position;
	}
	public void setR16_open_position(BigDecimal r16_open_position) {
		this.r16_open_position = r16_open_position;
	}
	public BigDecimal getR16_cpdm_dt_inc() {
		return r16_cpdm_dt_inc;
	}
	public void setR16_cpdm_dt_inc(BigDecimal r16_cpdm_dt_inc) {
		this.r16_cpdm_dt_inc = r16_cpdm_dt_inc;
	}
	public BigDecimal getR16_cpdm_dt_dec() {
		return r16_cpdm_dt_dec;
	}
	public void setR16_cpdm_dt_dec(BigDecimal r16_cpdm_dt_dec) {
		this.r16_cpdm_dt_dec = r16_cpdm_dt_dec;
	}
	public BigDecimal getR16_net() {
		return r16_net;
	}
	public void setR16_net(BigDecimal r16_net) {
		this.r16_net = r16_net;
	}
	public BigDecimal getR16_cpdm_dt_der() {
		return r16_cpdm_dt_der;
	}
	public void setR16_cpdm_dt_der(BigDecimal r16_cpdm_dt_der) {
		this.r16_cpdm_dt_der = r16_cpdm_dt_der;
	}
	public BigDecimal getR16_cpdm_dt_dto() {
		return r16_cpdm_dt_dto;
	}
	public void setR16_cpdm_dt_dto(BigDecimal r16_cpdm_dt_dto) {
		this.r16_cpdm_dt_dto = r16_cpdm_dt_dto;
	}
	public BigDecimal getR16_cp() {
		return r16_cp;
	}
	public void setR16_cp(BigDecimal r16_cp) {
		this.r16_cp = r16_cp;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_open_position() {
		return r17_open_position;
	}
	public void setR17_open_position(BigDecimal r17_open_position) {
		this.r17_open_position = r17_open_position;
	}
	public BigDecimal getR17_cpdm_dt_inc() {
		return r17_cpdm_dt_inc;
	}
	public void setR17_cpdm_dt_inc(BigDecimal r17_cpdm_dt_inc) {
		this.r17_cpdm_dt_inc = r17_cpdm_dt_inc;
	}
	public BigDecimal getR17_cpdm_dt_dec() {
		return r17_cpdm_dt_dec;
	}
	public void setR17_cpdm_dt_dec(BigDecimal r17_cpdm_dt_dec) {
		this.r17_cpdm_dt_dec = r17_cpdm_dt_dec;
	}
	public BigDecimal getR17_net() {
		return r17_net;
	}
	public void setR17_net(BigDecimal r17_net) {
		this.r17_net = r17_net;
	}
	public BigDecimal getR17_cpdm_dt_der() {
		return r17_cpdm_dt_der;
	}
	public void setR17_cpdm_dt_der(BigDecimal r17_cpdm_dt_der) {
		this.r17_cpdm_dt_der = r17_cpdm_dt_der;
	}
	public BigDecimal getR17_cpdm_dt_dto() {
		return r17_cpdm_dt_dto;
	}
	public void setR17_cpdm_dt_dto(BigDecimal r17_cpdm_dt_dto) {
		this.r17_cpdm_dt_dto = r17_cpdm_dt_dto;
	}
	public BigDecimal getR17_cp() {
		return r17_cp;
	}
	public void setR17_cp(BigDecimal r17_cp) {
		this.r17_cp = r17_cp;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_open_position() {
		return r18_open_position;
	}
	public void setR18_open_position(BigDecimal r18_open_position) {
		this.r18_open_position = r18_open_position;
	}
	public BigDecimal getR18_cpdm_dt_inc() {
		return r18_cpdm_dt_inc;
	}
	public void setR18_cpdm_dt_inc(BigDecimal r18_cpdm_dt_inc) {
		this.r18_cpdm_dt_inc = r18_cpdm_dt_inc;
	}
	public BigDecimal getR18_cpdm_dt_dec() {
		return r18_cpdm_dt_dec;
	}
	public void setR18_cpdm_dt_dec(BigDecimal r18_cpdm_dt_dec) {
		this.r18_cpdm_dt_dec = r18_cpdm_dt_dec;
	}
	public BigDecimal getR18_net() {
		return r18_net;
	}
	public void setR18_net(BigDecimal r18_net) {
		this.r18_net = r18_net;
	}
	public BigDecimal getR18_cpdm_dt_der() {
		return r18_cpdm_dt_der;
	}
	public void setR18_cpdm_dt_der(BigDecimal r18_cpdm_dt_der) {
		this.r18_cpdm_dt_der = r18_cpdm_dt_der;
	}
	public BigDecimal getR18_cpdm_dt_dto() {
		return r18_cpdm_dt_dto;
	}
	public void setR18_cpdm_dt_dto(BigDecimal r18_cpdm_dt_dto) {
		this.r18_cpdm_dt_dto = r18_cpdm_dt_dto;
	}
	public BigDecimal getR18_cp() {
		return r18_cp;
	}
	public void setR18_cp(BigDecimal r18_cp) {
		this.r18_cp = r18_cp;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_open_position() {
		return r19_open_position;
	}
	public void setR19_open_position(BigDecimal r19_open_position) {
		this.r19_open_position = r19_open_position;
	}
	public BigDecimal getR19_cpdm_dt_inc() {
		return r19_cpdm_dt_inc;
	}
	public void setR19_cpdm_dt_inc(BigDecimal r19_cpdm_dt_inc) {
		this.r19_cpdm_dt_inc = r19_cpdm_dt_inc;
	}
	public BigDecimal getR19_cpdm_dt_dec() {
		return r19_cpdm_dt_dec;
	}
	public void setR19_cpdm_dt_dec(BigDecimal r19_cpdm_dt_dec) {
		this.r19_cpdm_dt_dec = r19_cpdm_dt_dec;
	}
	public BigDecimal getR19_net() {
		return r19_net;
	}
	public void setR19_net(BigDecimal r19_net) {
		this.r19_net = r19_net;
	}
	public BigDecimal getR19_cpdm_dt_der() {
		return r19_cpdm_dt_der;
	}
	public void setR19_cpdm_dt_der(BigDecimal r19_cpdm_dt_der) {
		this.r19_cpdm_dt_der = r19_cpdm_dt_der;
	}
	public BigDecimal getR19_cpdm_dt_dto() {
		return r19_cpdm_dt_dto;
	}
	public void setR19_cpdm_dt_dto(BigDecimal r19_cpdm_dt_dto) {
		this.r19_cpdm_dt_dto = r19_cpdm_dt_dto;
	}
	public BigDecimal getR19_cp() {
		return r19_cp;
	}
	public void setR19_cp(BigDecimal r19_cp) {
		this.r19_cp = r19_cp;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_open_position() {
		return r20_open_position;
	}
	public void setR20_open_position(BigDecimal r20_open_position) {
		this.r20_open_position = r20_open_position;
	}
	public BigDecimal getR20_cpdm_dt_inc() {
		return r20_cpdm_dt_inc;
	}
	public void setR20_cpdm_dt_inc(BigDecimal r20_cpdm_dt_inc) {
		this.r20_cpdm_dt_inc = r20_cpdm_dt_inc;
	}
	public BigDecimal getR20_cpdm_dt_dec() {
		return r20_cpdm_dt_dec;
	}
	public void setR20_cpdm_dt_dec(BigDecimal r20_cpdm_dt_dec) {
		this.r20_cpdm_dt_dec = r20_cpdm_dt_dec;
	}
	public BigDecimal getR20_net() {
		return r20_net;
	}
	public void setR20_net(BigDecimal r20_net) {
		this.r20_net = r20_net;
	}
	public BigDecimal getR20_cpdm_dt_der() {
		return r20_cpdm_dt_der;
	}
	public void setR20_cpdm_dt_der(BigDecimal r20_cpdm_dt_der) {
		this.r20_cpdm_dt_der = r20_cpdm_dt_der;
	}
	public BigDecimal getR20_cpdm_dt_dto() {
		return r20_cpdm_dt_dto;
	}
	public void setR20_cpdm_dt_dto(BigDecimal r20_cpdm_dt_dto) {
		this.r20_cpdm_dt_dto = r20_cpdm_dt_dto;
	}
	public BigDecimal getR20_cp() {
		return r20_cp;
	}
	public void setR20_cp(BigDecimal r20_cp) {
		this.r20_cp = r20_cp;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_open_position() {
		return r21_open_position;
	}
	public void setR21_open_position(BigDecimal r21_open_position) {
		this.r21_open_position = r21_open_position;
	}
	public BigDecimal getR21_cpdm_dt_inc() {
		return r21_cpdm_dt_inc;
	}
	public void setR21_cpdm_dt_inc(BigDecimal r21_cpdm_dt_inc) {
		this.r21_cpdm_dt_inc = r21_cpdm_dt_inc;
	}
	public BigDecimal getR21_cpdm_dt_dec() {
		return r21_cpdm_dt_dec;
	}
	public void setR21_cpdm_dt_dec(BigDecimal r21_cpdm_dt_dec) {
		this.r21_cpdm_dt_dec = r21_cpdm_dt_dec;
	}
	public BigDecimal getR21_net() {
		return r21_net;
	}
	public void setR21_net(BigDecimal r21_net) {
		this.r21_net = r21_net;
	}
	public BigDecimal getR21_cpdm_dt_der() {
		return r21_cpdm_dt_der;
	}
	public void setR21_cpdm_dt_der(BigDecimal r21_cpdm_dt_der) {
		this.r21_cpdm_dt_der = r21_cpdm_dt_der;
	}
	public BigDecimal getR21_cpdm_dt_dto() {
		return r21_cpdm_dt_dto;
	}
	public void setR21_cpdm_dt_dto(BigDecimal r21_cpdm_dt_dto) {
		this.r21_cpdm_dt_dto = r21_cpdm_dt_dto;
	}
	public BigDecimal getR21_cp() {
		return r21_cp;
	}
	public void setR21_cp(BigDecimal r21_cp) {
		this.r21_cp = r21_cp;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_open_position() {
		return r22_open_position;
	}
	public void setR22_open_position(BigDecimal r22_open_position) {
		this.r22_open_position = r22_open_position;
	}
	public BigDecimal getR22_cpdm_dt_inc() {
		return r22_cpdm_dt_inc;
	}
	public void setR22_cpdm_dt_inc(BigDecimal r22_cpdm_dt_inc) {
		this.r22_cpdm_dt_inc = r22_cpdm_dt_inc;
	}
	public BigDecimal getR22_cpdm_dt_dec() {
		return r22_cpdm_dt_dec;
	}
	public void setR22_cpdm_dt_dec(BigDecimal r22_cpdm_dt_dec) {
		this.r22_cpdm_dt_dec = r22_cpdm_dt_dec;
	}
	public BigDecimal getR22_net() {
		return r22_net;
	}
	public void setR22_net(BigDecimal r22_net) {
		this.r22_net = r22_net;
	}
	public BigDecimal getR22_cpdm_dt_der() {
		return r22_cpdm_dt_der;
	}
	public void setR22_cpdm_dt_der(BigDecimal r22_cpdm_dt_der) {
		this.r22_cpdm_dt_der = r22_cpdm_dt_der;
	}
	public BigDecimal getR22_cpdm_dt_dto() {
		return r22_cpdm_dt_dto;
	}
	public void setR22_cpdm_dt_dto(BigDecimal r22_cpdm_dt_dto) {
		this.r22_cpdm_dt_dto = r22_cpdm_dt_dto;
	}
	public BigDecimal getR22_cp() {
		return r22_cp;
	}
	public void setR22_cp(BigDecimal r22_cp) {
		this.r22_cp = r22_cp;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_open_position() {
		return r23_open_position;
	}
	public void setR23_open_position(BigDecimal r23_open_position) {
		this.r23_open_position = r23_open_position;
	}
	public BigDecimal getR23_cpdm_dt_inc() {
		return r23_cpdm_dt_inc;
	}
	public void setR23_cpdm_dt_inc(BigDecimal r23_cpdm_dt_inc) {
		this.r23_cpdm_dt_inc = r23_cpdm_dt_inc;
	}
	public BigDecimal getR23_cpdm_dt_dec() {
		return r23_cpdm_dt_dec;
	}
	public void setR23_cpdm_dt_dec(BigDecimal r23_cpdm_dt_dec) {
		this.r23_cpdm_dt_dec = r23_cpdm_dt_dec;
	}
	public BigDecimal getR23_net() {
		return r23_net;
	}
	public void setR23_net(BigDecimal r23_net) {
		this.r23_net = r23_net;
	}
	public BigDecimal getR23_cpdm_dt_der() {
		return r23_cpdm_dt_der;
	}
	public void setR23_cpdm_dt_der(BigDecimal r23_cpdm_dt_der) {
		this.r23_cpdm_dt_der = r23_cpdm_dt_der;
	}
	public BigDecimal getR23_cpdm_dt_dto() {
		return r23_cpdm_dt_dto;
	}
	public void setR23_cpdm_dt_dto(BigDecimal r23_cpdm_dt_dto) {
		this.r23_cpdm_dt_dto = r23_cpdm_dt_dto;
	}
	public BigDecimal getR23_cp() {
		return r23_cp;
	}
	public void setR23_cp(BigDecimal r23_cp) {
		this.r23_cp = r23_cp;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_open_position() {
		return r24_open_position;
	}
	public void setR24_open_position(BigDecimal r24_open_position) {
		this.r24_open_position = r24_open_position;
	}
	public BigDecimal getR24_cpdm_dt_inc() {
		return r24_cpdm_dt_inc;
	}
	public void setR24_cpdm_dt_inc(BigDecimal r24_cpdm_dt_inc) {
		this.r24_cpdm_dt_inc = r24_cpdm_dt_inc;
	}
	public BigDecimal getR24_cpdm_dt_dec() {
		return r24_cpdm_dt_dec;
	}
	public void setR24_cpdm_dt_dec(BigDecimal r24_cpdm_dt_dec) {
		this.r24_cpdm_dt_dec = r24_cpdm_dt_dec;
	}
	public BigDecimal getR24_net() {
		return r24_net;
	}
	public void setR24_net(BigDecimal r24_net) {
		this.r24_net = r24_net;
	}
	public BigDecimal getR24_cpdm_dt_der() {
		return r24_cpdm_dt_der;
	}
	public void setR24_cpdm_dt_der(BigDecimal r24_cpdm_dt_der) {
		this.r24_cpdm_dt_der = r24_cpdm_dt_der;
	}
	public BigDecimal getR24_cpdm_dt_dto() {
		return r24_cpdm_dt_dto;
	}
	public void setR24_cpdm_dt_dto(BigDecimal r24_cpdm_dt_dto) {
		this.r24_cpdm_dt_dto = r24_cpdm_dt_dto;
	}
	public BigDecimal getR24_cp() {
		return r24_cp;
	}
	public void setR24_cp(BigDecimal r24_cp) {
		this.r24_cp = r24_cp;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_open_position() {
		return r25_open_position;
	}
	public void setR25_open_position(BigDecimal r25_open_position) {
		this.r25_open_position = r25_open_position;
	}
	public BigDecimal getR25_cpdm_dt_inc() {
		return r25_cpdm_dt_inc;
	}
	public void setR25_cpdm_dt_inc(BigDecimal r25_cpdm_dt_inc) {
		this.r25_cpdm_dt_inc = r25_cpdm_dt_inc;
	}
	public BigDecimal getR25_cpdm_dt_dec() {
		return r25_cpdm_dt_dec;
	}
	public void setR25_cpdm_dt_dec(BigDecimal r25_cpdm_dt_dec) {
		this.r25_cpdm_dt_dec = r25_cpdm_dt_dec;
	}
	public BigDecimal getR25_net() {
		return r25_net;
	}
	public void setR25_net(BigDecimal r25_net) {
		this.r25_net = r25_net;
	}
	public BigDecimal getR25_cpdm_dt_der() {
		return r25_cpdm_dt_der;
	}
	public void setR25_cpdm_dt_der(BigDecimal r25_cpdm_dt_der) {
		this.r25_cpdm_dt_der = r25_cpdm_dt_der;
	}
	public BigDecimal getR25_cpdm_dt_dto() {
		return r25_cpdm_dt_dto;
	}
	public void setR25_cpdm_dt_dto(BigDecimal r25_cpdm_dt_dto) {
		this.r25_cpdm_dt_dto = r25_cpdm_dt_dto;
	}
	public BigDecimal getR25_cp() {
		return r25_cp;
	}
	public void setR25_cp(BigDecimal r25_cp) {
		this.r25_cp = r25_cp;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_open_position() {
		return r26_open_position;
	}
	public void setR26_open_position(BigDecimal r26_open_position) {
		this.r26_open_position = r26_open_position;
	}
	public BigDecimal getR26_cpdm_dt_inc() {
		return r26_cpdm_dt_inc;
	}
	public void setR26_cpdm_dt_inc(BigDecimal r26_cpdm_dt_inc) {
		this.r26_cpdm_dt_inc = r26_cpdm_dt_inc;
	}
	public BigDecimal getR26_cpdm_dt_dec() {
		return r26_cpdm_dt_dec;
	}
	public void setR26_cpdm_dt_dec(BigDecimal r26_cpdm_dt_dec) {
		this.r26_cpdm_dt_dec = r26_cpdm_dt_dec;
	}
	public BigDecimal getR26_net() {
		return r26_net;
	}
	public void setR26_net(BigDecimal r26_net) {
		this.r26_net = r26_net;
	}
	public BigDecimal getR26_cpdm_dt_der() {
		return r26_cpdm_dt_der;
	}
	public void setR26_cpdm_dt_der(BigDecimal r26_cpdm_dt_der) {
		this.r26_cpdm_dt_der = r26_cpdm_dt_der;
	}
	public BigDecimal getR26_cpdm_dt_dto() {
		return r26_cpdm_dt_dto;
	}
	public void setR26_cpdm_dt_dto(BigDecimal r26_cpdm_dt_dto) {
		this.r26_cpdm_dt_dto = r26_cpdm_dt_dto;
	}
	public BigDecimal getR26_cp() {
		return r26_cp;
	}
	public void setR26_cp(BigDecimal r26_cp) {
		this.r26_cp = r26_cp;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_open_position() {
		return r27_open_position;
	}
	public void setR27_open_position(BigDecimal r27_open_position) {
		this.r27_open_position = r27_open_position;
	}
	public BigDecimal getR27_cpdm_dt_inc() {
		return r27_cpdm_dt_inc;
	}
	public void setR27_cpdm_dt_inc(BigDecimal r27_cpdm_dt_inc) {
		this.r27_cpdm_dt_inc = r27_cpdm_dt_inc;
	}
	public BigDecimal getR27_cpdm_dt_dec() {
		return r27_cpdm_dt_dec;
	}
	public void setR27_cpdm_dt_dec(BigDecimal r27_cpdm_dt_dec) {
		this.r27_cpdm_dt_dec = r27_cpdm_dt_dec;
	}
	public BigDecimal getR27_net() {
		return r27_net;
	}
	public void setR27_net(BigDecimal r27_net) {
		this.r27_net = r27_net;
	}
	public BigDecimal getR27_cpdm_dt_der() {
		return r27_cpdm_dt_der;
	}
	public void setR27_cpdm_dt_der(BigDecimal r27_cpdm_dt_der) {
		this.r27_cpdm_dt_der = r27_cpdm_dt_der;
	}
	public BigDecimal getR27_cpdm_dt_dto() {
		return r27_cpdm_dt_dto;
	}
	public void setR27_cpdm_dt_dto(BigDecimal r27_cpdm_dt_dto) {
		this.r27_cpdm_dt_dto = r27_cpdm_dt_dto;
	}
	public BigDecimal getR27_cp() {
		return r27_cp;
	}
	public void setR27_cp(BigDecimal r27_cp) {
		this.r27_cp = r27_cp;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_open_position() {
		return r28_open_position;
	}
	public void setR28_open_position(BigDecimal r28_open_position) {
		this.r28_open_position = r28_open_position;
	}
	public BigDecimal getR28_cpdm_dt_inc() {
		return r28_cpdm_dt_inc;
	}
	public void setR28_cpdm_dt_inc(BigDecimal r28_cpdm_dt_inc) {
		this.r28_cpdm_dt_inc = r28_cpdm_dt_inc;
	}
	public BigDecimal getR28_cpdm_dt_dec() {
		return r28_cpdm_dt_dec;
	}
	public void setR28_cpdm_dt_dec(BigDecimal r28_cpdm_dt_dec) {
		this.r28_cpdm_dt_dec = r28_cpdm_dt_dec;
	}
	public BigDecimal getR28_net() {
		return r28_net;
	}
	public void setR28_net(BigDecimal r28_net) {
		this.r28_net = r28_net;
	}
	public BigDecimal getR28_cpdm_dt_der() {
		return r28_cpdm_dt_der;
	}
	public void setR28_cpdm_dt_der(BigDecimal r28_cpdm_dt_der) {
		this.r28_cpdm_dt_der = r28_cpdm_dt_der;
	}
	public BigDecimal getR28_cpdm_dt_dto() {
		return r28_cpdm_dt_dto;
	}
	public void setR28_cpdm_dt_dto(BigDecimal r28_cpdm_dt_dto) {
		this.r28_cpdm_dt_dto = r28_cpdm_dt_dto;
	}
	public BigDecimal getR28_cp() {
		return r28_cp;
	}
	public void setR28_cp(BigDecimal r28_cp) {
		this.r28_cp = r28_cp;
	}
	public String getR29_product() {
		return r29_product;
	}
	public void setR29_product(String r29_product) {
		this.r29_product = r29_product;
	}
	public BigDecimal getR29_open_position() {
		return r29_open_position;
	}
	public void setR29_open_position(BigDecimal r29_open_position) {
		this.r29_open_position = r29_open_position;
	}
	public BigDecimal getR29_cpdm_dt_inc() {
		return r29_cpdm_dt_inc;
	}
	public void setR29_cpdm_dt_inc(BigDecimal r29_cpdm_dt_inc) {
		this.r29_cpdm_dt_inc = r29_cpdm_dt_inc;
	}
	public BigDecimal getR29_cpdm_dt_dec() {
		return r29_cpdm_dt_dec;
	}
	public void setR29_cpdm_dt_dec(BigDecimal r29_cpdm_dt_dec) {
		this.r29_cpdm_dt_dec = r29_cpdm_dt_dec;
	}
	public BigDecimal getR29_net() {
		return r29_net;
	}
	public void setR29_net(BigDecimal r29_net) {
		this.r29_net = r29_net;
	}
	public BigDecimal getR29_cpdm_dt_der() {
		return r29_cpdm_dt_der;
	}
	public void setR29_cpdm_dt_der(BigDecimal r29_cpdm_dt_der) {
		this.r29_cpdm_dt_der = r29_cpdm_dt_der;
	}
	public BigDecimal getR29_cpdm_dt_dto() {
		return r29_cpdm_dt_dto;
	}
	public void setR29_cpdm_dt_dto(BigDecimal r29_cpdm_dt_dto) {
		this.r29_cpdm_dt_dto = r29_cpdm_dt_dto;
	}
	public BigDecimal getR29_cp() {
		return r29_cp;
	}
	public void setR29_cp(BigDecimal r29_cp) {
		this.r29_cp = r29_cp;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_open_position() {
		return r30_open_position;
	}
	public void setR30_open_position(BigDecimal r30_open_position) {
		this.r30_open_position = r30_open_position;
	}
	public BigDecimal getR30_cpdm_dt_inc() {
		return r30_cpdm_dt_inc;
	}
	public void setR30_cpdm_dt_inc(BigDecimal r30_cpdm_dt_inc) {
		this.r30_cpdm_dt_inc = r30_cpdm_dt_inc;
	}
	public BigDecimal getR30_cpdm_dt_dec() {
		return r30_cpdm_dt_dec;
	}
	public void setR30_cpdm_dt_dec(BigDecimal r30_cpdm_dt_dec) {
		this.r30_cpdm_dt_dec = r30_cpdm_dt_dec;
	}
	public BigDecimal getR30_net() {
		return r30_net;
	}
	public void setR30_net(BigDecimal r30_net) {
		this.r30_net = r30_net;
	}
	public BigDecimal getR30_cpdm_dt_der() {
		return r30_cpdm_dt_der;
	}
	public void setR30_cpdm_dt_der(BigDecimal r30_cpdm_dt_der) {
		this.r30_cpdm_dt_der = r30_cpdm_dt_der;
	}
	public BigDecimal getR30_cpdm_dt_dto() {
		return r30_cpdm_dt_dto;
	}
	public void setR30_cpdm_dt_dto(BigDecimal r30_cpdm_dt_dto) {
		this.r30_cpdm_dt_dto = r30_cpdm_dt_dto;
	}
	public BigDecimal getR30_cp() {
		return r30_cp;
	}
	public void setR30_cp(BigDecimal r30_cp) {
		this.r30_cp = r30_cp;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_open_position() {
		return r31_open_position;
	}
	public void setR31_open_position(BigDecimal r31_open_position) {
		this.r31_open_position = r31_open_position;
	}
	public BigDecimal getR31_cpdm_dt_inc() {
		return r31_cpdm_dt_inc;
	}
	public void setR31_cpdm_dt_inc(BigDecimal r31_cpdm_dt_inc) {
		this.r31_cpdm_dt_inc = r31_cpdm_dt_inc;
	}
	public BigDecimal getR31_cpdm_dt_dec() {
		return r31_cpdm_dt_dec;
	}
	public void setR31_cpdm_dt_dec(BigDecimal r31_cpdm_dt_dec) {
		this.r31_cpdm_dt_dec = r31_cpdm_dt_dec;
	}
	public BigDecimal getR31_net() {
		return r31_net;
	}
	public void setR31_net(BigDecimal r31_net) {
		this.r31_net = r31_net;
	}
	public BigDecimal getR31_cpdm_dt_der() {
		return r31_cpdm_dt_der;
	}
	public void setR31_cpdm_dt_der(BigDecimal r31_cpdm_dt_der) {
		this.r31_cpdm_dt_der = r31_cpdm_dt_der;
	}
	public BigDecimal getR31_cpdm_dt_dto() {
		return r31_cpdm_dt_dto;
	}
	public void setR31_cpdm_dt_dto(BigDecimal r31_cpdm_dt_dto) {
		this.r31_cpdm_dt_dto = r31_cpdm_dt_dto;
	}
	public BigDecimal getR31_cp() {
		return r31_cp;
	}
	public void setR31_cp(BigDecimal r31_cp) {
		this.r31_cp = r31_cp;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_open_position() {
		return r32_open_position;
	}
	public void setR32_open_position(BigDecimal r32_open_position) {
		this.r32_open_position = r32_open_position;
	}
	public BigDecimal getR32_cpdm_dt_inc() {
		return r32_cpdm_dt_inc;
	}
	public void setR32_cpdm_dt_inc(BigDecimal r32_cpdm_dt_inc) {
		this.r32_cpdm_dt_inc = r32_cpdm_dt_inc;
	}
	public BigDecimal getR32_cpdm_dt_dec() {
		return r32_cpdm_dt_dec;
	}
	public void setR32_cpdm_dt_dec(BigDecimal r32_cpdm_dt_dec) {
		this.r32_cpdm_dt_dec = r32_cpdm_dt_dec;
	}
	public BigDecimal getR32_net() {
		return r32_net;
	}
	public void setR32_net(BigDecimal r32_net) {
		this.r32_net = r32_net;
	}
	public BigDecimal getR32_cpdm_dt_der() {
		return r32_cpdm_dt_der;
	}
	public void setR32_cpdm_dt_der(BigDecimal r32_cpdm_dt_der) {
		this.r32_cpdm_dt_der = r32_cpdm_dt_der;
	}
	public BigDecimal getR32_cpdm_dt_dto() {
		return r32_cpdm_dt_dto;
	}
	public void setR32_cpdm_dt_dto(BigDecimal r32_cpdm_dt_dto) {
		this.r32_cpdm_dt_dto = r32_cpdm_dt_dto;
	}
	public BigDecimal getR32_cp() {
		return r32_cp;
	}
	public void setR32_cp(BigDecimal r32_cp) {
		this.r32_cp = r32_cp;
	}
	public String getR33_product() {
		return r33_product;
	}
	public void setR33_product(String r33_product) {
		this.r33_product = r33_product;
	}
	public BigDecimal getR33_open_position() {
		return r33_open_position;
	}
	public void setR33_open_position(BigDecimal r33_open_position) {
		this.r33_open_position = r33_open_position;
	}
	public BigDecimal getR33_cpdm_dt_inc() {
		return r33_cpdm_dt_inc;
	}
	public void setR33_cpdm_dt_inc(BigDecimal r33_cpdm_dt_inc) {
		this.r33_cpdm_dt_inc = r33_cpdm_dt_inc;
	}
	public BigDecimal getR33_cpdm_dt_dec() {
		return r33_cpdm_dt_dec;
	}
	public void setR33_cpdm_dt_dec(BigDecimal r33_cpdm_dt_dec) {
		this.r33_cpdm_dt_dec = r33_cpdm_dt_dec;
	}
	public BigDecimal getR33_net() {
		return r33_net;
	}
	public void setR33_net(BigDecimal r33_net) {
		this.r33_net = r33_net;
	}
	public BigDecimal getR33_cpdm_dt_der() {
		return r33_cpdm_dt_der;
	}
	public void setR33_cpdm_dt_der(BigDecimal r33_cpdm_dt_der) {
		this.r33_cpdm_dt_der = r33_cpdm_dt_der;
	}
	public BigDecimal getR33_cpdm_dt_dto() {
		return r33_cpdm_dt_dto;
	}
	public void setR33_cpdm_dt_dto(BigDecimal r33_cpdm_dt_dto) {
		this.r33_cpdm_dt_dto = r33_cpdm_dt_dto;
	}
	public BigDecimal getR33_cp() {
		return r33_cp;
	}
	public void setR33_cp(BigDecimal r33_cp) {
		this.r33_cp = r33_cp;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_open_position() {
		return r34_open_position;
	}
	public void setR34_open_position(BigDecimal r34_open_position) {
		this.r34_open_position = r34_open_position;
	}
	public BigDecimal getR34_cpdm_dt_inc() {
		return r34_cpdm_dt_inc;
	}
	public void setR34_cpdm_dt_inc(BigDecimal r34_cpdm_dt_inc) {
		this.r34_cpdm_dt_inc = r34_cpdm_dt_inc;
	}
	public BigDecimal getR34_cpdm_dt_dec() {
		return r34_cpdm_dt_dec;
	}
	public void setR34_cpdm_dt_dec(BigDecimal r34_cpdm_dt_dec) {
		this.r34_cpdm_dt_dec = r34_cpdm_dt_dec;
	}
	public BigDecimal getR34_net() {
		return r34_net;
	}
	public void setR34_net(BigDecimal r34_net) {
		this.r34_net = r34_net;
	}
	public BigDecimal getR34_cpdm_dt_der() {
		return r34_cpdm_dt_der;
	}
	public void setR34_cpdm_dt_der(BigDecimal r34_cpdm_dt_der) {
		this.r34_cpdm_dt_der = r34_cpdm_dt_der;
	}
	public BigDecimal getR34_cpdm_dt_dto() {
		return r34_cpdm_dt_dto;
	}
	public void setR34_cpdm_dt_dto(BigDecimal r34_cpdm_dt_dto) {
		this.r34_cpdm_dt_dto = r34_cpdm_dt_dto;
	}
	public BigDecimal getR34_cp() {
		return r34_cp;
	}
	public void setR34_cp(BigDecimal r34_cp) {
		this.r34_cp = r34_cp;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_open_position() {
		return r35_open_position;
	}
	public void setR35_open_position(BigDecimal r35_open_position) {
		this.r35_open_position = r35_open_position;
	}
	public BigDecimal getR35_cpdm_dt_inc() {
		return r35_cpdm_dt_inc;
	}
	public void setR35_cpdm_dt_inc(BigDecimal r35_cpdm_dt_inc) {
		this.r35_cpdm_dt_inc = r35_cpdm_dt_inc;
	}
	public BigDecimal getR35_cpdm_dt_dec() {
		return r35_cpdm_dt_dec;
	}
	public void setR35_cpdm_dt_dec(BigDecimal r35_cpdm_dt_dec) {
		this.r35_cpdm_dt_dec = r35_cpdm_dt_dec;
	}
	public BigDecimal getR35_net() {
		return r35_net;
	}
	public void setR35_net(BigDecimal r35_net) {
		this.r35_net = r35_net;
	}
	public BigDecimal getR35_cpdm_dt_der() {
		return r35_cpdm_dt_der;
	}
	public void setR35_cpdm_dt_der(BigDecimal r35_cpdm_dt_der) {
		this.r35_cpdm_dt_der = r35_cpdm_dt_der;
	}
	public BigDecimal getR35_cpdm_dt_dto() {
		return r35_cpdm_dt_dto;
	}
	public void setR35_cpdm_dt_dto(BigDecimal r35_cpdm_dt_dto) {
		this.r35_cpdm_dt_dto = r35_cpdm_dt_dto;
	}
	public BigDecimal getR35_cp() {
		return r35_cp;
	}
	public void setR35_cp(BigDecimal r35_cp) {
		this.r35_cp = r35_cp;
	}
	public String getR36_product() {
		return r36_product;
	}
	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}
	public BigDecimal getR36_open_position() {
		return r36_open_position;
	}
	public void setR36_open_position(BigDecimal r36_open_position) {
		this.r36_open_position = r36_open_position;
	}
	public BigDecimal getR36_cpdm_dt_inc() {
		return r36_cpdm_dt_inc;
	}
	public void setR36_cpdm_dt_inc(BigDecimal r36_cpdm_dt_inc) {
		this.r36_cpdm_dt_inc = r36_cpdm_dt_inc;
	}
	public BigDecimal getR36_cpdm_dt_dec() {
		return r36_cpdm_dt_dec;
	}
	public void setR36_cpdm_dt_dec(BigDecimal r36_cpdm_dt_dec) {
		this.r36_cpdm_dt_dec = r36_cpdm_dt_dec;
	}
	public BigDecimal getR36_net() {
		return r36_net;
	}
	public void setR36_net(BigDecimal r36_net) {
		this.r36_net = r36_net;
	}
	public BigDecimal getR36_cpdm_dt_der() {
		return r36_cpdm_dt_der;
	}
	public void setR36_cpdm_dt_der(BigDecimal r36_cpdm_dt_der) {
		this.r36_cpdm_dt_der = r36_cpdm_dt_der;
	}
	public BigDecimal getR36_cpdm_dt_dto() {
		return r36_cpdm_dt_dto;
	}
	public void setR36_cpdm_dt_dto(BigDecimal r36_cpdm_dt_dto) {
		this.r36_cpdm_dt_dto = r36_cpdm_dt_dto;
	}
	public BigDecimal getR36_cp() {
		return r36_cp;
	}
	public void setR36_cp(BigDecimal r36_cp) {
		this.r36_cp = r36_cp;
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
// DETAIL ENTITY  M_BOP
// =====================================================	

public class M_BOP_Detail_RowMapper implements RowMapper<M_BOP_Detail_Entity> {

    @Override
    public M_BOP_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_BOP_Detail_Entity obj = new M_BOP_Detail_Entity();

// =========================
// R13
// =========================
obj.setR13_product(rs.getString("r13_product"));
obj.setR13_open_position(rs.getBigDecimal("r13_open_position"));
obj.setR13_cpdm_dt_inc(rs.getBigDecimal("r13_cpdm_dt_inc"));
obj.setR13_cpdm_dt_dec(rs.getBigDecimal("r13_cpdm_dt_dec"));
obj.setR13_net(rs.getBigDecimal("r13_net"));
obj.setR13_cpdm_dt_der(rs.getBigDecimal("r13_cpdm_dt_der"));
obj.setR13_cpdm_dt_dto(rs.getBigDecimal("r13_cpdm_dt_dto"));
obj.setR13_cp(rs.getBigDecimal("r13_cp"));


// =========================
// R14
// =========================
obj.setR14_product(rs.getString("r14_product"));
obj.setR14_open_position(rs.getBigDecimal("r14_open_position"));
obj.setR14_cpdm_dt_inc(rs.getBigDecimal("r14_cpdm_dt_inc"));
obj.setR14_cpdm_dt_dec(rs.getBigDecimal("r14_cpdm_dt_dec"));
obj.setR14_net(rs.getBigDecimal("r14_net"));
obj.setR14_cpdm_dt_der(rs.getBigDecimal("r14_cpdm_dt_der"));
obj.setR14_cpdm_dt_dto(rs.getBigDecimal("r14_cpdm_dt_dto"));
obj.setR14_cp(rs.getBigDecimal("r14_cp"));

// =========================
// R15
// =========================
obj.setR15_product(rs.getString("r15_product"));
obj.setR15_open_position(rs.getBigDecimal("r15_open_position"));
obj.setR15_cpdm_dt_inc(rs.getBigDecimal("r15_cpdm_dt_inc"));
obj.setR15_cpdm_dt_dec(rs.getBigDecimal("r15_cpdm_dt_dec"));
obj.setR15_net(rs.getBigDecimal("r15_net"));
obj.setR15_cpdm_dt_der(rs.getBigDecimal("r15_cpdm_dt_der"));
obj.setR15_cpdm_dt_dto(rs.getBigDecimal("r15_cpdm_dt_dto"));
obj.setR15_cp(rs.getBigDecimal("r15_cp"));

// =========================
// R16
// =========================
obj.setR16_product(rs.getString("r16_product"));
obj.setR16_open_position(rs.getBigDecimal("r16_open_position"));
obj.setR16_cpdm_dt_inc(rs.getBigDecimal("r16_cpdm_dt_inc"));
obj.setR16_cpdm_dt_dec(rs.getBigDecimal("r16_cpdm_dt_dec"));
obj.setR16_net(rs.getBigDecimal("r16_net"));
obj.setR16_cpdm_dt_der(rs.getBigDecimal("r16_cpdm_dt_der"));
obj.setR16_cpdm_dt_dto(rs.getBigDecimal("r16_cpdm_dt_dto"));
obj.setR16_cp(rs.getBigDecimal("r16_cp"));

// =========================
// R17
// =========================
obj.setR17_product(rs.getString("r17_product"));
obj.setR17_open_position(rs.getBigDecimal("r17_open_position"));
obj.setR17_cpdm_dt_inc(rs.getBigDecimal("r17_cpdm_dt_inc"));
obj.setR17_cpdm_dt_dec(rs.getBigDecimal("r17_cpdm_dt_dec"));
obj.setR17_net(rs.getBigDecimal("r17_net"));
obj.setR17_cpdm_dt_der(rs.getBigDecimal("r17_cpdm_dt_der"));
obj.setR17_cpdm_dt_dto(rs.getBigDecimal("r17_cpdm_dt_dto"));
obj.setR17_cp(rs.getBigDecimal("r17_cp"));

// =========================
// R18
// =========================
obj.setR18_product(rs.getString("r18_product"));
obj.setR18_open_position(rs.getBigDecimal("r18_open_position"));
obj.setR18_cpdm_dt_inc(rs.getBigDecimal("r18_cpdm_dt_inc"));
obj.setR18_cpdm_dt_dec(rs.getBigDecimal("r18_cpdm_dt_dec"));
obj.setR18_net(rs.getBigDecimal("r18_net"));
obj.setR18_cpdm_dt_der(rs.getBigDecimal("r18_cpdm_dt_der"));
obj.setR18_cpdm_dt_dto(rs.getBigDecimal("r18_cpdm_dt_dto"));
obj.setR18_cp(rs.getBigDecimal("r18_cp"));

// =========================
// R19
// =========================
obj.setR19_product(rs.getString("r19_product"));
obj.setR19_open_position(rs.getBigDecimal("r19_open_position"));
obj.setR19_cpdm_dt_inc(rs.getBigDecimal("r19_cpdm_dt_inc"));
obj.setR19_cpdm_dt_dec(rs.getBigDecimal("r19_cpdm_dt_dec"));
obj.setR19_net(rs.getBigDecimal("r19_net"));
obj.setR19_cpdm_dt_der(rs.getBigDecimal("r19_cpdm_dt_der"));
obj.setR19_cpdm_dt_dto(rs.getBigDecimal("r19_cpdm_dt_dto"));
obj.setR19_cp(rs.getBigDecimal("r19_cp"));

// =========================
// R20
// =========================
obj.setR20_product(rs.getString("r20_product"));
obj.setR20_open_position(rs.getBigDecimal("r20_open_position"));
obj.setR20_cpdm_dt_inc(rs.getBigDecimal("r20_cpdm_dt_inc"));
obj.setR20_cpdm_dt_dec(rs.getBigDecimal("r20_cpdm_dt_dec"));
obj.setR20_net(rs.getBigDecimal("r20_net"));
obj.setR20_cpdm_dt_der(rs.getBigDecimal("r20_cpdm_dt_der"));
obj.setR20_cpdm_dt_dto(rs.getBigDecimal("r20_cpdm_dt_dto"));
obj.setR20_cp(rs.getBigDecimal("r20_cp"));


// =========================
// R21
// =========================
obj.setR21_product(rs.getString("r21_product"));
obj.setR21_open_position(rs.getBigDecimal("r21_open_position"));
obj.setR21_cpdm_dt_inc(rs.getBigDecimal("r21_cpdm_dt_inc"));
obj.setR21_cpdm_dt_dec(rs.getBigDecimal("r21_cpdm_dt_dec"));
obj.setR21_net(rs.getBigDecimal("r21_net"));
obj.setR21_cpdm_dt_der(rs.getBigDecimal("r21_cpdm_dt_der"));
obj.setR21_cpdm_dt_dto(rs.getBigDecimal("r21_cpdm_dt_dto"));
obj.setR21_cp(rs.getBigDecimal("r21_cp"));

// =========================
// R22
// =========================
obj.setR22_product(rs.getString("r22_product"));
obj.setR22_open_position(rs.getBigDecimal("r22_open_position"));
obj.setR22_cpdm_dt_inc(rs.getBigDecimal("r22_cpdm_dt_inc"));
obj.setR22_cpdm_dt_dec(rs.getBigDecimal("r22_cpdm_dt_dec"));
obj.setR22_net(rs.getBigDecimal("r22_net"));
obj.setR22_cpdm_dt_der(rs.getBigDecimal("r22_cpdm_dt_der"));
obj.setR22_cpdm_dt_dto(rs.getBigDecimal("r22_cpdm_dt_dto"));
obj.setR22_cp(rs.getBigDecimal("r22_cp"));

// =========================
// R23
// =========================
obj.setR23_product(rs.getString("r23_product"));
obj.setR23_open_position(rs.getBigDecimal("r23_open_position"));
obj.setR23_cpdm_dt_inc(rs.getBigDecimal("r23_cpdm_dt_inc"));
obj.setR23_cpdm_dt_dec(rs.getBigDecimal("r23_cpdm_dt_dec"));
obj.setR23_net(rs.getBigDecimal("r23_net"));
obj.setR23_cpdm_dt_der(rs.getBigDecimal("r23_cpdm_dt_der"));
obj.setR23_cpdm_dt_dto(rs.getBigDecimal("r23_cpdm_dt_dto"));
obj.setR23_cp(rs.getBigDecimal("r23_cp"));

// =========================
// R24
// =========================
obj.setR24_product(rs.getString("r24_product"));
obj.setR24_open_position(rs.getBigDecimal("r24_open_position"));
obj.setR24_cpdm_dt_inc(rs.getBigDecimal("r24_cpdm_dt_inc"));
obj.setR24_cpdm_dt_dec(rs.getBigDecimal("r24_cpdm_dt_dec"));
obj.setR24_net(rs.getBigDecimal("r24_net"));
obj.setR24_cpdm_dt_der(rs.getBigDecimal("r24_cpdm_dt_der"));
obj.setR24_cpdm_dt_dto(rs.getBigDecimal("r24_cpdm_dt_dto"));
obj.setR24_cp(rs.getBigDecimal("r24_cp"));

// =========================
// R25
// =========================
obj.setR25_product(rs.getString("r25_product"));
obj.setR25_open_position(rs.getBigDecimal("r25_open_position"));
obj.setR25_cpdm_dt_inc(rs.getBigDecimal("r25_cpdm_dt_inc"));
obj.setR25_cpdm_dt_dec(rs.getBigDecimal("r25_cpdm_dt_dec"));
obj.setR25_net(rs.getBigDecimal("r25_net"));
obj.setR25_cpdm_dt_der(rs.getBigDecimal("r25_cpdm_dt_der"));
obj.setR25_cpdm_dt_dto(rs.getBigDecimal("r25_cpdm_dt_dto"));
obj.setR25_cp(rs.getBigDecimal("r25_cp"));

// =========================
// R26
// =========================
obj.setR26_product(rs.getString("r26_product"));
obj.setR26_open_position(rs.getBigDecimal("r26_open_position"));
obj.setR26_cpdm_dt_inc(rs.getBigDecimal("r26_cpdm_dt_inc"));
obj.setR26_cpdm_dt_dec(rs.getBigDecimal("r26_cpdm_dt_dec"));
obj.setR26_net(rs.getBigDecimal("r26_net"));
obj.setR26_cpdm_dt_der(rs.getBigDecimal("r26_cpdm_dt_der"));
obj.setR26_cpdm_dt_dto(rs.getBigDecimal("r26_cpdm_dt_dto"));
obj.setR26_cp(rs.getBigDecimal("r26_cp"));

// =========================
// R27
// =========================
obj.setR27_product(rs.getString("r27_product"));
obj.setR27_open_position(rs.getBigDecimal("r27_open_position"));
obj.setR27_cpdm_dt_inc(rs.getBigDecimal("r27_cpdm_dt_inc"));
obj.setR27_cpdm_dt_dec(rs.getBigDecimal("r27_cpdm_dt_dec"));
obj.setR27_net(rs.getBigDecimal("r27_net"));
obj.setR27_cpdm_dt_der(rs.getBigDecimal("r27_cpdm_dt_der"));
obj.setR27_cpdm_dt_dto(rs.getBigDecimal("r27_cpdm_dt_dto"));
obj.setR27_cp(rs.getBigDecimal("r27_cp"));

// =========================
// R28
// =========================
obj.setR28_product(rs.getString("r28_product"));
obj.setR28_open_position(rs.getBigDecimal("r28_open_position"));
obj.setR28_cpdm_dt_inc(rs.getBigDecimal("r28_cpdm_dt_inc"));
obj.setR28_cpdm_dt_dec(rs.getBigDecimal("r28_cpdm_dt_dec"));
obj.setR28_net(rs.getBigDecimal("r28_net"));
obj.setR28_cpdm_dt_der(rs.getBigDecimal("r28_cpdm_dt_der"));
obj.setR28_cpdm_dt_dto(rs.getBigDecimal("r28_cpdm_dt_dto"));
obj.setR28_cp(rs.getBigDecimal("r28_cp"));

// =========================
// R29
// =========================
obj.setR29_product(rs.getString("r29_product"));
obj.setR29_open_position(rs.getBigDecimal("r29_open_position"));
obj.setR29_cpdm_dt_inc(rs.getBigDecimal("r29_cpdm_dt_inc"));
obj.setR29_cpdm_dt_dec(rs.getBigDecimal("r29_cpdm_dt_dec"));
obj.setR29_net(rs.getBigDecimal("r29_net"));
obj.setR29_cpdm_dt_der(rs.getBigDecimal("r29_cpdm_dt_der"));
obj.setR29_cpdm_dt_dto(rs.getBigDecimal("r29_cpdm_dt_dto"));
obj.setR29_cp(rs.getBigDecimal("r29_cp"));

// =========================
// R30
// =========================
obj.setR30_product(rs.getString("r30_product"));
obj.setR30_open_position(rs.getBigDecimal("r30_open_position"));
obj.setR30_cpdm_dt_inc(rs.getBigDecimal("r30_cpdm_dt_inc"));
obj.setR30_cpdm_dt_dec(rs.getBigDecimal("r30_cpdm_dt_dec"));
obj.setR30_net(rs.getBigDecimal("r30_net"));
obj.setR30_cpdm_dt_der(rs.getBigDecimal("r30_cpdm_dt_der"));
obj.setR30_cpdm_dt_dto(rs.getBigDecimal("r30_cpdm_dt_dto"));
obj.setR30_cp(rs.getBigDecimal("r30_cp"));

// =========================
// R31
// =========================
obj.setR31_product(rs.getString("r31_product"));
obj.setR31_open_position(rs.getBigDecimal("r31_open_position"));
obj.setR31_cpdm_dt_inc(rs.getBigDecimal("r31_cpdm_dt_inc"));
obj.setR31_cpdm_dt_dec(rs.getBigDecimal("r31_cpdm_dt_dec"));
obj.setR31_net(rs.getBigDecimal("r31_net"));
obj.setR31_cpdm_dt_der(rs.getBigDecimal("r31_cpdm_dt_der"));
obj.setR31_cpdm_dt_dto(rs.getBigDecimal("r31_cpdm_dt_dto"));
obj.setR31_cp(rs.getBigDecimal("r31_cp"));

// =========================
// R32
// =========================
obj.setR32_product(rs.getString("r32_product"));
obj.setR32_open_position(rs.getBigDecimal("r32_open_position"));
obj.setR32_cpdm_dt_inc(rs.getBigDecimal("r32_cpdm_dt_inc"));
obj.setR32_cpdm_dt_dec(rs.getBigDecimal("r32_cpdm_dt_dec"));
obj.setR32_net(rs.getBigDecimal("r32_net"));
obj.setR32_cpdm_dt_der(rs.getBigDecimal("r32_cpdm_dt_der"));
obj.setR32_cpdm_dt_dto(rs.getBigDecimal("r32_cpdm_dt_dto"));
obj.setR32_cp(rs.getBigDecimal("r32_cp"));

// =========================
// R33
// =========================
obj.setR33_product(rs.getString("r33_product"));
obj.setR33_open_position(rs.getBigDecimal("r33_open_position"));
obj.setR33_cpdm_dt_inc(rs.getBigDecimal("r33_cpdm_dt_inc"));
obj.setR33_cpdm_dt_dec(rs.getBigDecimal("r33_cpdm_dt_dec"));
obj.setR33_net(rs.getBigDecimal("r33_net"));
obj.setR33_cpdm_dt_der(rs.getBigDecimal("r33_cpdm_dt_der"));
obj.setR33_cpdm_dt_dto(rs.getBigDecimal("r33_cpdm_dt_dto"));
obj.setR33_cp(rs.getBigDecimal("r33_cp"));

// =========================
// R34
// =========================
obj.setR34_product(rs.getString("r34_product"));
obj.setR34_open_position(rs.getBigDecimal("r34_open_position"));
obj.setR34_cpdm_dt_inc(rs.getBigDecimal("r34_cpdm_dt_inc"));
obj.setR34_cpdm_dt_dec(rs.getBigDecimal("r34_cpdm_dt_dec"));
obj.setR34_net(rs.getBigDecimal("r34_net"));
obj.setR34_cpdm_dt_der(rs.getBigDecimal("r34_cpdm_dt_der"));
obj.setR34_cpdm_dt_dto(rs.getBigDecimal("r34_cpdm_dt_dto"));
obj.setR34_cp(rs.getBigDecimal("r34_cp"));

// =========================
// R35
// =========================
obj.setR35_product(rs.getString("r35_product"));
obj.setR35_open_position(rs.getBigDecimal("r35_open_position"));
obj.setR35_cpdm_dt_inc(rs.getBigDecimal("r35_cpdm_dt_inc"));
obj.setR35_cpdm_dt_dec(rs.getBigDecimal("r35_cpdm_dt_dec"));
obj.setR35_net(rs.getBigDecimal("r35_net"));
obj.setR35_cpdm_dt_der(rs.getBigDecimal("r35_cpdm_dt_der"));
obj.setR35_cpdm_dt_dto(rs.getBigDecimal("r35_cpdm_dt_dto"));
obj.setR35_cp(rs.getBigDecimal("r35_cp"));

// =========================
// R36
// =========================
obj.setR36_product(rs.getString("r36_product"));
obj.setR36_open_position(rs.getBigDecimal("r36_open_position"));
obj.setR36_cpdm_dt_inc(rs.getBigDecimal("r36_cpdm_dt_inc"));
obj.setR36_cpdm_dt_dec(rs.getBigDecimal("r36_cpdm_dt_dec"));
obj.setR36_net(rs.getBigDecimal("r36_net"));
obj.setR36_cpdm_dt_der(rs.getBigDecimal("r36_cpdm_dt_der"));
obj.setR36_cpdm_dt_dto(rs.getBigDecimal("r36_cpdm_dt_dto"));
obj.setR36_cp(rs.getBigDecimal("r36_cp"));


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

public class M_BOP_Detail_Entity {

   
private String	r13_product;
	private BigDecimal	r13_open_position;
	private BigDecimal	r13_cpdm_dt_inc;
	private BigDecimal	r13_cpdm_dt_dec;
	private BigDecimal	r13_net;
	private BigDecimal	r13_cpdm_dt_der;
	private BigDecimal	r13_cpdm_dt_dto;
	private BigDecimal	r13_cp;
	private String	r14_product;
	private BigDecimal	r14_open_position;
	private BigDecimal	r14_cpdm_dt_inc;
	private BigDecimal	r14_cpdm_dt_dec;
	private BigDecimal	r14_net;
	private BigDecimal	r14_cpdm_dt_der;
	private BigDecimal	r14_cpdm_dt_dto;
	private BigDecimal	r14_cp;
	private String	r15_product;
	private BigDecimal	r15_open_position;
	private BigDecimal	r15_cpdm_dt_inc;
	private BigDecimal	r15_cpdm_dt_dec;
	private BigDecimal	r15_net;
	private BigDecimal	r15_cpdm_dt_der;
	private BigDecimal	r15_cpdm_dt_dto;
	private BigDecimal	r15_cp;
	private String	r16_product;
	private BigDecimal	r16_open_position;
	private BigDecimal	r16_cpdm_dt_inc;
	private BigDecimal	r16_cpdm_dt_dec;
	private BigDecimal	r16_net;
	private BigDecimal	r16_cpdm_dt_der;
	private BigDecimal	r16_cpdm_dt_dto;
	private BigDecimal	r16_cp;
	private String	r17_product;
	private BigDecimal	r17_open_position;
	private BigDecimal	r17_cpdm_dt_inc;
	private BigDecimal	r17_cpdm_dt_dec;
	private BigDecimal	r17_net;
	private BigDecimal	r17_cpdm_dt_der;
	private BigDecimal	r17_cpdm_dt_dto;
	private BigDecimal	r17_cp;
	private String	r18_product;
	private BigDecimal	r18_open_position;
	private BigDecimal	r18_cpdm_dt_inc;
	private BigDecimal	r18_cpdm_dt_dec;
	private BigDecimal	r18_net;
	private BigDecimal	r18_cpdm_dt_der;
	private BigDecimal	r18_cpdm_dt_dto;
	private BigDecimal	r18_cp;
	private String	r19_product;
	private BigDecimal	r19_open_position;
	private BigDecimal	r19_cpdm_dt_inc;
	private BigDecimal	r19_cpdm_dt_dec;
	private BigDecimal	r19_net;
	private BigDecimal	r19_cpdm_dt_der;
	private BigDecimal	r19_cpdm_dt_dto;
	private BigDecimal	r19_cp;
	private String	r20_product;
	private BigDecimal	r20_open_position;
	private BigDecimal	r20_cpdm_dt_inc;
	private BigDecimal	r20_cpdm_dt_dec;
	private BigDecimal	r20_net;
	private BigDecimal	r20_cpdm_dt_der;
	private BigDecimal	r20_cpdm_dt_dto;
	private BigDecimal	r20_cp;
	private String	r21_product;
	private BigDecimal	r21_open_position;
	private BigDecimal	r21_cpdm_dt_inc;
	private BigDecimal	r21_cpdm_dt_dec;
	private BigDecimal	r21_net;
	private BigDecimal	r21_cpdm_dt_der;
	private BigDecimal	r21_cpdm_dt_dto;
	private BigDecimal	r21_cp;
	private String	r22_product;
	private BigDecimal	r22_open_position;
	private BigDecimal	r22_cpdm_dt_inc;
	private BigDecimal	r22_cpdm_dt_dec;
	private BigDecimal	r22_net;
	private BigDecimal	r22_cpdm_dt_der;
	private BigDecimal	r22_cpdm_dt_dto;
	private BigDecimal	r22_cp;
	private String	r23_product;
	private BigDecimal	r23_open_position;
	private BigDecimal	r23_cpdm_dt_inc;
	private BigDecimal	r23_cpdm_dt_dec;
	private BigDecimal	r23_net;
	private BigDecimal	r23_cpdm_dt_der;
	private BigDecimal	r23_cpdm_dt_dto;
	private BigDecimal	r23_cp;
	private String	r24_product;
	private BigDecimal	r24_open_position;
	private BigDecimal	r24_cpdm_dt_inc;
	private BigDecimal	r24_cpdm_dt_dec;
	private BigDecimal	r24_net;
	private BigDecimal	r24_cpdm_dt_der;
	private BigDecimal	r24_cpdm_dt_dto;
	private BigDecimal	r24_cp;
	private String	r25_product;
	private BigDecimal	r25_open_position;
	private BigDecimal	r25_cpdm_dt_inc;
	private BigDecimal	r25_cpdm_dt_dec;
	private BigDecimal	r25_net;
	private BigDecimal	r25_cpdm_dt_der;
	private BigDecimal	r25_cpdm_dt_dto;
	private BigDecimal	r25_cp;
	private String	r26_product;
	private BigDecimal	r26_open_position;
	private BigDecimal	r26_cpdm_dt_inc;
	private BigDecimal	r26_cpdm_dt_dec;
	private BigDecimal	r26_net;
	private BigDecimal	r26_cpdm_dt_der;
	private BigDecimal	r26_cpdm_dt_dto;
	private BigDecimal	r26_cp;
	private String	r27_product;
	private BigDecimal	r27_open_position;
	private BigDecimal	r27_cpdm_dt_inc;
	private BigDecimal	r27_cpdm_dt_dec;
	private BigDecimal	r27_net;
	private BigDecimal	r27_cpdm_dt_der;
	private BigDecimal	r27_cpdm_dt_dto;
	private BigDecimal	r27_cp;
	private String	r28_product;
	private BigDecimal	r28_open_position;
	private BigDecimal	r28_cpdm_dt_inc;
	private BigDecimal	r28_cpdm_dt_dec;
	private BigDecimal	r28_net;
	private BigDecimal	r28_cpdm_dt_der;
	private BigDecimal	r28_cpdm_dt_dto;
	private BigDecimal	r28_cp;
	private String	r29_product;
	private BigDecimal	r29_open_position;
	private BigDecimal	r29_cpdm_dt_inc;
	private BigDecimal	r29_cpdm_dt_dec;
	private BigDecimal	r29_net;
	private BigDecimal	r29_cpdm_dt_der;
	private BigDecimal	r29_cpdm_dt_dto;
	private BigDecimal	r29_cp;
	private String	r30_product;
	private BigDecimal	r30_open_position;
	private BigDecimal	r30_cpdm_dt_inc;
	private BigDecimal	r30_cpdm_dt_dec;
	private BigDecimal	r30_net;
	private BigDecimal	r30_cpdm_dt_der;
	private BigDecimal	r30_cpdm_dt_dto;
	private BigDecimal	r30_cp;
	private String	r31_product;
	private BigDecimal	r31_open_position;
	private BigDecimal	r31_cpdm_dt_inc;
	private BigDecimal	r31_cpdm_dt_dec;
	private BigDecimal	r31_net;
	private BigDecimal	r31_cpdm_dt_der;
	private BigDecimal	r31_cpdm_dt_dto;
	private BigDecimal	r31_cp;
	private String	r32_product;
	private BigDecimal	r32_open_position;
	private BigDecimal	r32_cpdm_dt_inc;
	private BigDecimal	r32_cpdm_dt_dec;
	private BigDecimal	r32_net;
	private BigDecimal	r32_cpdm_dt_der;
	private BigDecimal	r32_cpdm_dt_dto;
	private BigDecimal	r32_cp;
	private String	r33_product;
	private BigDecimal	r33_open_position;
	private BigDecimal	r33_cpdm_dt_inc;
	private BigDecimal	r33_cpdm_dt_dec;
	private BigDecimal	r33_net;
	private BigDecimal	r33_cpdm_dt_der;
	private BigDecimal	r33_cpdm_dt_dto;
	private BigDecimal	r33_cp;
	private String	r34_product;
	private BigDecimal	r34_open_position;
	private BigDecimal	r34_cpdm_dt_inc;
	private BigDecimal	r34_cpdm_dt_dec;
	private BigDecimal	r34_net;
	private BigDecimal	r34_cpdm_dt_der;
	private BigDecimal	r34_cpdm_dt_dto;
	private BigDecimal	r34_cp;
	private String	r35_product;
	private BigDecimal	r35_open_position;
	private BigDecimal	r35_cpdm_dt_inc;
	private BigDecimal	r35_cpdm_dt_dec;
	private BigDecimal	r35_net;
	private BigDecimal	r35_cpdm_dt_der;
	private BigDecimal	r35_cpdm_dt_dto;
	private BigDecimal	r35_cp;
	private String	r36_product;
	private BigDecimal	r36_open_position;
	private BigDecimal	r36_cpdm_dt_inc;
	private BigDecimal	r36_cpdm_dt_dec;
	private BigDecimal	r36_net;
	private BigDecimal	r36_cpdm_dt_der;
	private BigDecimal	r36_cpdm_dt_dto;
	private BigDecimal	r36_cp;
	
	
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
	
	
	
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_open_position() {
		return r13_open_position;
	}
	public void setR13_open_position(BigDecimal r13_open_position) {
		this.r13_open_position = r13_open_position;
	}
	public BigDecimal getR13_cpdm_dt_inc() {
		return r13_cpdm_dt_inc;
	}
	public void setR13_cpdm_dt_inc(BigDecimal r13_cpdm_dt_inc) {
		this.r13_cpdm_dt_inc = r13_cpdm_dt_inc;
	}
	public BigDecimal getR13_cpdm_dt_dec() {
		return r13_cpdm_dt_dec;
	}
	public void setR13_cpdm_dt_dec(BigDecimal r13_cpdm_dt_dec) {
		this.r13_cpdm_dt_dec = r13_cpdm_dt_dec;
	}
	public BigDecimal getR13_net() {
		return r13_net;
	}
	public void setR13_net(BigDecimal r13_net) {
		this.r13_net = r13_net;
	}
	public BigDecimal getR13_cpdm_dt_der() {
		return r13_cpdm_dt_der;
	}
	public void setR13_cpdm_dt_der(BigDecimal r13_cpdm_dt_der) {
		this.r13_cpdm_dt_der = r13_cpdm_dt_der;
	}
	public BigDecimal getR13_cpdm_dt_dto() {
		return r13_cpdm_dt_dto;
	}
	public void setR13_cpdm_dt_dto(BigDecimal r13_cpdm_dt_dto) {
		this.r13_cpdm_dt_dto = r13_cpdm_dt_dto;
	}
	public BigDecimal getR13_cp() {
		return r13_cp;
	}
	public void setR13_cp(BigDecimal r13_cp) {
		this.r13_cp = r13_cp;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_open_position() {
		return r14_open_position;
	}
	public void setR14_open_position(BigDecimal r14_open_position) {
		this.r14_open_position = r14_open_position;
	}
	public BigDecimal getR14_cpdm_dt_inc() {
		return r14_cpdm_dt_inc;
	}
	public void setR14_cpdm_dt_inc(BigDecimal r14_cpdm_dt_inc) {
		this.r14_cpdm_dt_inc = r14_cpdm_dt_inc;
	}
	public BigDecimal getR14_cpdm_dt_dec() {
		return r14_cpdm_dt_dec;
	}
	public void setR14_cpdm_dt_dec(BigDecimal r14_cpdm_dt_dec) {
		this.r14_cpdm_dt_dec = r14_cpdm_dt_dec;
	}
	public BigDecimal getR14_net() {
		return r14_net;
	}
	public void setR14_net(BigDecimal r14_net) {
		this.r14_net = r14_net;
	}
	public BigDecimal getR14_cpdm_dt_der() {
		return r14_cpdm_dt_der;
	}
	public void setR14_cpdm_dt_der(BigDecimal r14_cpdm_dt_der) {
		this.r14_cpdm_dt_der = r14_cpdm_dt_der;
	}
	public BigDecimal getR14_cpdm_dt_dto() {
		return r14_cpdm_dt_dto;
	}
	public void setR14_cpdm_dt_dto(BigDecimal r14_cpdm_dt_dto) {
		this.r14_cpdm_dt_dto = r14_cpdm_dt_dto;
	}
	public BigDecimal getR14_cp() {
		return r14_cp;
	}
	public void setR14_cp(BigDecimal r14_cp) {
		this.r14_cp = r14_cp;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_open_position() {
		return r15_open_position;
	}
	public void setR15_open_position(BigDecimal r15_open_position) {
		this.r15_open_position = r15_open_position;
	}
	public BigDecimal getR15_cpdm_dt_inc() {
		return r15_cpdm_dt_inc;
	}
	public void setR15_cpdm_dt_inc(BigDecimal r15_cpdm_dt_inc) {
		this.r15_cpdm_dt_inc = r15_cpdm_dt_inc;
	}
	public BigDecimal getR15_cpdm_dt_dec() {
		return r15_cpdm_dt_dec;
	}
	public void setR15_cpdm_dt_dec(BigDecimal r15_cpdm_dt_dec) {
		this.r15_cpdm_dt_dec = r15_cpdm_dt_dec;
	}
	public BigDecimal getR15_net() {
		return r15_net;
	}
	public void setR15_net(BigDecimal r15_net) {
		this.r15_net = r15_net;
	}
	public BigDecimal getR15_cpdm_dt_der() {
		return r15_cpdm_dt_der;
	}
	public void setR15_cpdm_dt_der(BigDecimal r15_cpdm_dt_der) {
		this.r15_cpdm_dt_der = r15_cpdm_dt_der;
	}
	public BigDecimal getR15_cpdm_dt_dto() {
		return r15_cpdm_dt_dto;
	}
	public void setR15_cpdm_dt_dto(BigDecimal r15_cpdm_dt_dto) {
		this.r15_cpdm_dt_dto = r15_cpdm_dt_dto;
	}
	public BigDecimal getR15_cp() {
		return r15_cp;
	}
	public void setR15_cp(BigDecimal r15_cp) {
		this.r15_cp = r15_cp;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_open_position() {
		return r16_open_position;
	}
	public void setR16_open_position(BigDecimal r16_open_position) {
		this.r16_open_position = r16_open_position;
	}
	public BigDecimal getR16_cpdm_dt_inc() {
		return r16_cpdm_dt_inc;
	}
	public void setR16_cpdm_dt_inc(BigDecimal r16_cpdm_dt_inc) {
		this.r16_cpdm_dt_inc = r16_cpdm_dt_inc;
	}
	public BigDecimal getR16_cpdm_dt_dec() {
		return r16_cpdm_dt_dec;
	}
	public void setR16_cpdm_dt_dec(BigDecimal r16_cpdm_dt_dec) {
		this.r16_cpdm_dt_dec = r16_cpdm_dt_dec;
	}
	public BigDecimal getR16_net() {
		return r16_net;
	}
	public void setR16_net(BigDecimal r16_net) {
		this.r16_net = r16_net;
	}
	public BigDecimal getR16_cpdm_dt_der() {
		return r16_cpdm_dt_der;
	}
	public void setR16_cpdm_dt_der(BigDecimal r16_cpdm_dt_der) {
		this.r16_cpdm_dt_der = r16_cpdm_dt_der;
	}
	public BigDecimal getR16_cpdm_dt_dto() {
		return r16_cpdm_dt_dto;
	}
	public void setR16_cpdm_dt_dto(BigDecimal r16_cpdm_dt_dto) {
		this.r16_cpdm_dt_dto = r16_cpdm_dt_dto;
	}
	public BigDecimal getR16_cp() {
		return r16_cp;
	}
	public void setR16_cp(BigDecimal r16_cp) {
		this.r16_cp = r16_cp;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_open_position() {
		return r17_open_position;
	}
	public void setR17_open_position(BigDecimal r17_open_position) {
		this.r17_open_position = r17_open_position;
	}
	public BigDecimal getR17_cpdm_dt_inc() {
		return r17_cpdm_dt_inc;
	}
	public void setR17_cpdm_dt_inc(BigDecimal r17_cpdm_dt_inc) {
		this.r17_cpdm_dt_inc = r17_cpdm_dt_inc;
	}
	public BigDecimal getR17_cpdm_dt_dec() {
		return r17_cpdm_dt_dec;
	}
	public void setR17_cpdm_dt_dec(BigDecimal r17_cpdm_dt_dec) {
		this.r17_cpdm_dt_dec = r17_cpdm_dt_dec;
	}
	public BigDecimal getR17_net() {
		return r17_net;
	}
	public void setR17_net(BigDecimal r17_net) {
		this.r17_net = r17_net;
	}
	public BigDecimal getR17_cpdm_dt_der() {
		return r17_cpdm_dt_der;
	}
	public void setR17_cpdm_dt_der(BigDecimal r17_cpdm_dt_der) {
		this.r17_cpdm_dt_der = r17_cpdm_dt_der;
	}
	public BigDecimal getR17_cpdm_dt_dto() {
		return r17_cpdm_dt_dto;
	}
	public void setR17_cpdm_dt_dto(BigDecimal r17_cpdm_dt_dto) {
		this.r17_cpdm_dt_dto = r17_cpdm_dt_dto;
	}
	public BigDecimal getR17_cp() {
		return r17_cp;
	}
	public void setR17_cp(BigDecimal r17_cp) {
		this.r17_cp = r17_cp;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_open_position() {
		return r18_open_position;
	}
	public void setR18_open_position(BigDecimal r18_open_position) {
		this.r18_open_position = r18_open_position;
	}
	public BigDecimal getR18_cpdm_dt_inc() {
		return r18_cpdm_dt_inc;
	}
	public void setR18_cpdm_dt_inc(BigDecimal r18_cpdm_dt_inc) {
		this.r18_cpdm_dt_inc = r18_cpdm_dt_inc;
	}
	public BigDecimal getR18_cpdm_dt_dec() {
		return r18_cpdm_dt_dec;
	}
	public void setR18_cpdm_dt_dec(BigDecimal r18_cpdm_dt_dec) {
		this.r18_cpdm_dt_dec = r18_cpdm_dt_dec;
	}
	public BigDecimal getR18_net() {
		return r18_net;
	}
	public void setR18_net(BigDecimal r18_net) {
		this.r18_net = r18_net;
	}
	public BigDecimal getR18_cpdm_dt_der() {
		return r18_cpdm_dt_der;
	}
	public void setR18_cpdm_dt_der(BigDecimal r18_cpdm_dt_der) {
		this.r18_cpdm_dt_der = r18_cpdm_dt_der;
	}
	public BigDecimal getR18_cpdm_dt_dto() {
		return r18_cpdm_dt_dto;
	}
	public void setR18_cpdm_dt_dto(BigDecimal r18_cpdm_dt_dto) {
		this.r18_cpdm_dt_dto = r18_cpdm_dt_dto;
	}
	public BigDecimal getR18_cp() {
		return r18_cp;
	}
	public void setR18_cp(BigDecimal r18_cp) {
		this.r18_cp = r18_cp;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_open_position() {
		return r19_open_position;
	}
	public void setR19_open_position(BigDecimal r19_open_position) {
		this.r19_open_position = r19_open_position;
	}
	public BigDecimal getR19_cpdm_dt_inc() {
		return r19_cpdm_dt_inc;
	}
	public void setR19_cpdm_dt_inc(BigDecimal r19_cpdm_dt_inc) {
		this.r19_cpdm_dt_inc = r19_cpdm_dt_inc;
	}
	public BigDecimal getR19_cpdm_dt_dec() {
		return r19_cpdm_dt_dec;
	}
	public void setR19_cpdm_dt_dec(BigDecimal r19_cpdm_dt_dec) {
		this.r19_cpdm_dt_dec = r19_cpdm_dt_dec;
	}
	public BigDecimal getR19_net() {
		return r19_net;
	}
	public void setR19_net(BigDecimal r19_net) {
		this.r19_net = r19_net;
	}
	public BigDecimal getR19_cpdm_dt_der() {
		return r19_cpdm_dt_der;
	}
	public void setR19_cpdm_dt_der(BigDecimal r19_cpdm_dt_der) {
		this.r19_cpdm_dt_der = r19_cpdm_dt_der;
	}
	public BigDecimal getR19_cpdm_dt_dto() {
		return r19_cpdm_dt_dto;
	}
	public void setR19_cpdm_dt_dto(BigDecimal r19_cpdm_dt_dto) {
		this.r19_cpdm_dt_dto = r19_cpdm_dt_dto;
	}
	public BigDecimal getR19_cp() {
		return r19_cp;
	}
	public void setR19_cp(BigDecimal r19_cp) {
		this.r19_cp = r19_cp;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_open_position() {
		return r20_open_position;
	}
	public void setR20_open_position(BigDecimal r20_open_position) {
		this.r20_open_position = r20_open_position;
	}
	public BigDecimal getR20_cpdm_dt_inc() {
		return r20_cpdm_dt_inc;
	}
	public void setR20_cpdm_dt_inc(BigDecimal r20_cpdm_dt_inc) {
		this.r20_cpdm_dt_inc = r20_cpdm_dt_inc;
	}
	public BigDecimal getR20_cpdm_dt_dec() {
		return r20_cpdm_dt_dec;
	}
	public void setR20_cpdm_dt_dec(BigDecimal r20_cpdm_dt_dec) {
		this.r20_cpdm_dt_dec = r20_cpdm_dt_dec;
	}
	public BigDecimal getR20_net() {
		return r20_net;
	}
	public void setR20_net(BigDecimal r20_net) {
		this.r20_net = r20_net;
	}
	public BigDecimal getR20_cpdm_dt_der() {
		return r20_cpdm_dt_der;
	}
	public void setR20_cpdm_dt_der(BigDecimal r20_cpdm_dt_der) {
		this.r20_cpdm_dt_der = r20_cpdm_dt_der;
	}
	public BigDecimal getR20_cpdm_dt_dto() {
		return r20_cpdm_dt_dto;
	}
	public void setR20_cpdm_dt_dto(BigDecimal r20_cpdm_dt_dto) {
		this.r20_cpdm_dt_dto = r20_cpdm_dt_dto;
	}
	public BigDecimal getR20_cp() {
		return r20_cp;
	}
	public void setR20_cp(BigDecimal r20_cp) {
		this.r20_cp = r20_cp;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_open_position() {
		return r21_open_position;
	}
	public void setR21_open_position(BigDecimal r21_open_position) {
		this.r21_open_position = r21_open_position;
	}
	public BigDecimal getR21_cpdm_dt_inc() {
		return r21_cpdm_dt_inc;
	}
	public void setR21_cpdm_dt_inc(BigDecimal r21_cpdm_dt_inc) {
		this.r21_cpdm_dt_inc = r21_cpdm_dt_inc;
	}
	public BigDecimal getR21_cpdm_dt_dec() {
		return r21_cpdm_dt_dec;
	}
	public void setR21_cpdm_dt_dec(BigDecimal r21_cpdm_dt_dec) {
		this.r21_cpdm_dt_dec = r21_cpdm_dt_dec;
	}
	public BigDecimal getR21_net() {
		return r21_net;
	}
	public void setR21_net(BigDecimal r21_net) {
		this.r21_net = r21_net;
	}
	public BigDecimal getR21_cpdm_dt_der() {
		return r21_cpdm_dt_der;
	}
	public void setR21_cpdm_dt_der(BigDecimal r21_cpdm_dt_der) {
		this.r21_cpdm_dt_der = r21_cpdm_dt_der;
	}
	public BigDecimal getR21_cpdm_dt_dto() {
		return r21_cpdm_dt_dto;
	}
	public void setR21_cpdm_dt_dto(BigDecimal r21_cpdm_dt_dto) {
		this.r21_cpdm_dt_dto = r21_cpdm_dt_dto;
	}
	public BigDecimal getR21_cp() {
		return r21_cp;
	}
	public void setR21_cp(BigDecimal r21_cp) {
		this.r21_cp = r21_cp;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_open_position() {
		return r22_open_position;
	}
	public void setR22_open_position(BigDecimal r22_open_position) {
		this.r22_open_position = r22_open_position;
	}
	public BigDecimal getR22_cpdm_dt_inc() {
		return r22_cpdm_dt_inc;
	}
	public void setR22_cpdm_dt_inc(BigDecimal r22_cpdm_dt_inc) {
		this.r22_cpdm_dt_inc = r22_cpdm_dt_inc;
	}
	public BigDecimal getR22_cpdm_dt_dec() {
		return r22_cpdm_dt_dec;
	}
	public void setR22_cpdm_dt_dec(BigDecimal r22_cpdm_dt_dec) {
		this.r22_cpdm_dt_dec = r22_cpdm_dt_dec;
	}
	public BigDecimal getR22_net() {
		return r22_net;
	}
	public void setR22_net(BigDecimal r22_net) {
		this.r22_net = r22_net;
	}
	public BigDecimal getR22_cpdm_dt_der() {
		return r22_cpdm_dt_der;
	}
	public void setR22_cpdm_dt_der(BigDecimal r22_cpdm_dt_der) {
		this.r22_cpdm_dt_der = r22_cpdm_dt_der;
	}
	public BigDecimal getR22_cpdm_dt_dto() {
		return r22_cpdm_dt_dto;
	}
	public void setR22_cpdm_dt_dto(BigDecimal r22_cpdm_dt_dto) {
		this.r22_cpdm_dt_dto = r22_cpdm_dt_dto;
	}
	public BigDecimal getR22_cp() {
		return r22_cp;
	}
	public void setR22_cp(BigDecimal r22_cp) {
		this.r22_cp = r22_cp;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_open_position() {
		return r23_open_position;
	}
	public void setR23_open_position(BigDecimal r23_open_position) {
		this.r23_open_position = r23_open_position;
	}
	public BigDecimal getR23_cpdm_dt_inc() {
		return r23_cpdm_dt_inc;
	}
	public void setR23_cpdm_dt_inc(BigDecimal r23_cpdm_dt_inc) {
		this.r23_cpdm_dt_inc = r23_cpdm_dt_inc;
	}
	public BigDecimal getR23_cpdm_dt_dec() {
		return r23_cpdm_dt_dec;
	}
	public void setR23_cpdm_dt_dec(BigDecimal r23_cpdm_dt_dec) {
		this.r23_cpdm_dt_dec = r23_cpdm_dt_dec;
	}
	public BigDecimal getR23_net() {
		return r23_net;
	}
	public void setR23_net(BigDecimal r23_net) {
		this.r23_net = r23_net;
	}
	public BigDecimal getR23_cpdm_dt_der() {
		return r23_cpdm_dt_der;
	}
	public void setR23_cpdm_dt_der(BigDecimal r23_cpdm_dt_der) {
		this.r23_cpdm_dt_der = r23_cpdm_dt_der;
	}
	public BigDecimal getR23_cpdm_dt_dto() {
		return r23_cpdm_dt_dto;
	}
	public void setR23_cpdm_dt_dto(BigDecimal r23_cpdm_dt_dto) {
		this.r23_cpdm_dt_dto = r23_cpdm_dt_dto;
	}
	public BigDecimal getR23_cp() {
		return r23_cp;
	}
	public void setR23_cp(BigDecimal r23_cp) {
		this.r23_cp = r23_cp;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_open_position() {
		return r24_open_position;
	}
	public void setR24_open_position(BigDecimal r24_open_position) {
		this.r24_open_position = r24_open_position;
	}
	public BigDecimal getR24_cpdm_dt_inc() {
		return r24_cpdm_dt_inc;
	}
	public void setR24_cpdm_dt_inc(BigDecimal r24_cpdm_dt_inc) {
		this.r24_cpdm_dt_inc = r24_cpdm_dt_inc;
	}
	public BigDecimal getR24_cpdm_dt_dec() {
		return r24_cpdm_dt_dec;
	}
	public void setR24_cpdm_dt_dec(BigDecimal r24_cpdm_dt_dec) {
		this.r24_cpdm_dt_dec = r24_cpdm_dt_dec;
	}
	public BigDecimal getR24_net() {
		return r24_net;
	}
	public void setR24_net(BigDecimal r24_net) {
		this.r24_net = r24_net;
	}
	public BigDecimal getR24_cpdm_dt_der() {
		return r24_cpdm_dt_der;
	}
	public void setR24_cpdm_dt_der(BigDecimal r24_cpdm_dt_der) {
		this.r24_cpdm_dt_der = r24_cpdm_dt_der;
	}
	public BigDecimal getR24_cpdm_dt_dto() {
		return r24_cpdm_dt_dto;
	}
	public void setR24_cpdm_dt_dto(BigDecimal r24_cpdm_dt_dto) {
		this.r24_cpdm_dt_dto = r24_cpdm_dt_dto;
	}
	public BigDecimal getR24_cp() {
		return r24_cp;
	}
	public void setR24_cp(BigDecimal r24_cp) {
		this.r24_cp = r24_cp;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_open_position() {
		return r25_open_position;
	}
	public void setR25_open_position(BigDecimal r25_open_position) {
		this.r25_open_position = r25_open_position;
	}
	public BigDecimal getR25_cpdm_dt_inc() {
		return r25_cpdm_dt_inc;
	}
	public void setR25_cpdm_dt_inc(BigDecimal r25_cpdm_dt_inc) {
		this.r25_cpdm_dt_inc = r25_cpdm_dt_inc;
	}
	public BigDecimal getR25_cpdm_dt_dec() {
		return r25_cpdm_dt_dec;
	}
	public void setR25_cpdm_dt_dec(BigDecimal r25_cpdm_dt_dec) {
		this.r25_cpdm_dt_dec = r25_cpdm_dt_dec;
	}
	public BigDecimal getR25_net() {
		return r25_net;
	}
	public void setR25_net(BigDecimal r25_net) {
		this.r25_net = r25_net;
	}
	public BigDecimal getR25_cpdm_dt_der() {
		return r25_cpdm_dt_der;
	}
	public void setR25_cpdm_dt_der(BigDecimal r25_cpdm_dt_der) {
		this.r25_cpdm_dt_der = r25_cpdm_dt_der;
	}
	public BigDecimal getR25_cpdm_dt_dto() {
		return r25_cpdm_dt_dto;
	}
	public void setR25_cpdm_dt_dto(BigDecimal r25_cpdm_dt_dto) {
		this.r25_cpdm_dt_dto = r25_cpdm_dt_dto;
	}
	public BigDecimal getR25_cp() {
		return r25_cp;
	}
	public void setR25_cp(BigDecimal r25_cp) {
		this.r25_cp = r25_cp;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_open_position() {
		return r26_open_position;
	}
	public void setR26_open_position(BigDecimal r26_open_position) {
		this.r26_open_position = r26_open_position;
	}
	public BigDecimal getR26_cpdm_dt_inc() {
		return r26_cpdm_dt_inc;
	}
	public void setR26_cpdm_dt_inc(BigDecimal r26_cpdm_dt_inc) {
		this.r26_cpdm_dt_inc = r26_cpdm_dt_inc;
	}
	public BigDecimal getR26_cpdm_dt_dec() {
		return r26_cpdm_dt_dec;
	}
	public void setR26_cpdm_dt_dec(BigDecimal r26_cpdm_dt_dec) {
		this.r26_cpdm_dt_dec = r26_cpdm_dt_dec;
	}
	public BigDecimal getR26_net() {
		return r26_net;
	}
	public void setR26_net(BigDecimal r26_net) {
		this.r26_net = r26_net;
	}
	public BigDecimal getR26_cpdm_dt_der() {
		return r26_cpdm_dt_der;
	}
	public void setR26_cpdm_dt_der(BigDecimal r26_cpdm_dt_der) {
		this.r26_cpdm_dt_der = r26_cpdm_dt_der;
	}
	public BigDecimal getR26_cpdm_dt_dto() {
		return r26_cpdm_dt_dto;
	}
	public void setR26_cpdm_dt_dto(BigDecimal r26_cpdm_dt_dto) {
		this.r26_cpdm_dt_dto = r26_cpdm_dt_dto;
	}
	public BigDecimal getR26_cp() {
		return r26_cp;
	}
	public void setR26_cp(BigDecimal r26_cp) {
		this.r26_cp = r26_cp;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_open_position() {
		return r27_open_position;
	}
	public void setR27_open_position(BigDecimal r27_open_position) {
		this.r27_open_position = r27_open_position;
	}
	public BigDecimal getR27_cpdm_dt_inc() {
		return r27_cpdm_dt_inc;
	}
	public void setR27_cpdm_dt_inc(BigDecimal r27_cpdm_dt_inc) {
		this.r27_cpdm_dt_inc = r27_cpdm_dt_inc;
	}
	public BigDecimal getR27_cpdm_dt_dec() {
		return r27_cpdm_dt_dec;
	}
	public void setR27_cpdm_dt_dec(BigDecimal r27_cpdm_dt_dec) {
		this.r27_cpdm_dt_dec = r27_cpdm_dt_dec;
	}
	public BigDecimal getR27_net() {
		return r27_net;
	}
	public void setR27_net(BigDecimal r27_net) {
		this.r27_net = r27_net;
	}
	public BigDecimal getR27_cpdm_dt_der() {
		return r27_cpdm_dt_der;
	}
	public void setR27_cpdm_dt_der(BigDecimal r27_cpdm_dt_der) {
		this.r27_cpdm_dt_der = r27_cpdm_dt_der;
	}
	public BigDecimal getR27_cpdm_dt_dto() {
		return r27_cpdm_dt_dto;
	}
	public void setR27_cpdm_dt_dto(BigDecimal r27_cpdm_dt_dto) {
		this.r27_cpdm_dt_dto = r27_cpdm_dt_dto;
	}
	public BigDecimal getR27_cp() {
		return r27_cp;
	}
	public void setR27_cp(BigDecimal r27_cp) {
		this.r27_cp = r27_cp;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_open_position() {
		return r28_open_position;
	}
	public void setR28_open_position(BigDecimal r28_open_position) {
		this.r28_open_position = r28_open_position;
	}
	public BigDecimal getR28_cpdm_dt_inc() {
		return r28_cpdm_dt_inc;
	}
	public void setR28_cpdm_dt_inc(BigDecimal r28_cpdm_dt_inc) {
		this.r28_cpdm_dt_inc = r28_cpdm_dt_inc;
	}
	public BigDecimal getR28_cpdm_dt_dec() {
		return r28_cpdm_dt_dec;
	}
	public void setR28_cpdm_dt_dec(BigDecimal r28_cpdm_dt_dec) {
		this.r28_cpdm_dt_dec = r28_cpdm_dt_dec;
	}
	public BigDecimal getR28_net() {
		return r28_net;
	}
	public void setR28_net(BigDecimal r28_net) {
		this.r28_net = r28_net;
	}
	public BigDecimal getR28_cpdm_dt_der() {
		return r28_cpdm_dt_der;
	}
	public void setR28_cpdm_dt_der(BigDecimal r28_cpdm_dt_der) {
		this.r28_cpdm_dt_der = r28_cpdm_dt_der;
	}
	public BigDecimal getR28_cpdm_dt_dto() {
		return r28_cpdm_dt_dto;
	}
	public void setR28_cpdm_dt_dto(BigDecimal r28_cpdm_dt_dto) {
		this.r28_cpdm_dt_dto = r28_cpdm_dt_dto;
	}
	public BigDecimal getR28_cp() {
		return r28_cp;
	}
	public void setR28_cp(BigDecimal r28_cp) {
		this.r28_cp = r28_cp;
	}
	public String getR29_product() {
		return r29_product;
	}
	public void setR29_product(String r29_product) {
		this.r29_product = r29_product;
	}
	public BigDecimal getR29_open_position() {
		return r29_open_position;
	}
	public void setR29_open_position(BigDecimal r29_open_position) {
		this.r29_open_position = r29_open_position;
	}
	public BigDecimal getR29_cpdm_dt_inc() {
		return r29_cpdm_dt_inc;
	}
	public void setR29_cpdm_dt_inc(BigDecimal r29_cpdm_dt_inc) {
		this.r29_cpdm_dt_inc = r29_cpdm_dt_inc;
	}
	public BigDecimal getR29_cpdm_dt_dec() {
		return r29_cpdm_dt_dec;
	}
	public void setR29_cpdm_dt_dec(BigDecimal r29_cpdm_dt_dec) {
		this.r29_cpdm_dt_dec = r29_cpdm_dt_dec;
	}
	public BigDecimal getR29_net() {
		return r29_net;
	}
	public void setR29_net(BigDecimal r29_net) {
		this.r29_net = r29_net;
	}
	public BigDecimal getR29_cpdm_dt_der() {
		return r29_cpdm_dt_der;
	}
	public void setR29_cpdm_dt_der(BigDecimal r29_cpdm_dt_der) {
		this.r29_cpdm_dt_der = r29_cpdm_dt_der;
	}
	public BigDecimal getR29_cpdm_dt_dto() {
		return r29_cpdm_dt_dto;
	}
	public void setR29_cpdm_dt_dto(BigDecimal r29_cpdm_dt_dto) {
		this.r29_cpdm_dt_dto = r29_cpdm_dt_dto;
	}
	public BigDecimal getR29_cp() {
		return r29_cp;
	}
	public void setR29_cp(BigDecimal r29_cp) {
		this.r29_cp = r29_cp;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_open_position() {
		return r30_open_position;
	}
	public void setR30_open_position(BigDecimal r30_open_position) {
		this.r30_open_position = r30_open_position;
	}
	public BigDecimal getR30_cpdm_dt_inc() {
		return r30_cpdm_dt_inc;
	}
	public void setR30_cpdm_dt_inc(BigDecimal r30_cpdm_dt_inc) {
		this.r30_cpdm_dt_inc = r30_cpdm_dt_inc;
	}
	public BigDecimal getR30_cpdm_dt_dec() {
		return r30_cpdm_dt_dec;
	}
	public void setR30_cpdm_dt_dec(BigDecimal r30_cpdm_dt_dec) {
		this.r30_cpdm_dt_dec = r30_cpdm_dt_dec;
	}
	public BigDecimal getR30_net() {
		return r30_net;
	}
	public void setR30_net(BigDecimal r30_net) {
		this.r30_net = r30_net;
	}
	public BigDecimal getR30_cpdm_dt_der() {
		return r30_cpdm_dt_der;
	}
	public void setR30_cpdm_dt_der(BigDecimal r30_cpdm_dt_der) {
		this.r30_cpdm_dt_der = r30_cpdm_dt_der;
	}
	public BigDecimal getR30_cpdm_dt_dto() {
		return r30_cpdm_dt_dto;
	}
	public void setR30_cpdm_dt_dto(BigDecimal r30_cpdm_dt_dto) {
		this.r30_cpdm_dt_dto = r30_cpdm_dt_dto;
	}
	public BigDecimal getR30_cp() {
		return r30_cp;
	}
	public void setR30_cp(BigDecimal r30_cp) {
		this.r30_cp = r30_cp;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_open_position() {
		return r31_open_position;
	}
	public void setR31_open_position(BigDecimal r31_open_position) {
		this.r31_open_position = r31_open_position;
	}
	public BigDecimal getR31_cpdm_dt_inc() {
		return r31_cpdm_dt_inc;
	}
	public void setR31_cpdm_dt_inc(BigDecimal r31_cpdm_dt_inc) {
		this.r31_cpdm_dt_inc = r31_cpdm_dt_inc;
	}
	public BigDecimal getR31_cpdm_dt_dec() {
		return r31_cpdm_dt_dec;
	}
	public void setR31_cpdm_dt_dec(BigDecimal r31_cpdm_dt_dec) {
		this.r31_cpdm_dt_dec = r31_cpdm_dt_dec;
	}
	public BigDecimal getR31_net() {
		return r31_net;
	}
	public void setR31_net(BigDecimal r31_net) {
		this.r31_net = r31_net;
	}
	public BigDecimal getR31_cpdm_dt_der() {
		return r31_cpdm_dt_der;
	}
	public void setR31_cpdm_dt_der(BigDecimal r31_cpdm_dt_der) {
		this.r31_cpdm_dt_der = r31_cpdm_dt_der;
	}
	public BigDecimal getR31_cpdm_dt_dto() {
		return r31_cpdm_dt_dto;
	}
	public void setR31_cpdm_dt_dto(BigDecimal r31_cpdm_dt_dto) {
		this.r31_cpdm_dt_dto = r31_cpdm_dt_dto;
	}
	public BigDecimal getR31_cp() {
		return r31_cp;
	}
	public void setR31_cp(BigDecimal r31_cp) {
		this.r31_cp = r31_cp;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_open_position() {
		return r32_open_position;
	}
	public void setR32_open_position(BigDecimal r32_open_position) {
		this.r32_open_position = r32_open_position;
	}
	public BigDecimal getR32_cpdm_dt_inc() {
		return r32_cpdm_dt_inc;
	}
	public void setR32_cpdm_dt_inc(BigDecimal r32_cpdm_dt_inc) {
		this.r32_cpdm_dt_inc = r32_cpdm_dt_inc;
	}
	public BigDecimal getR32_cpdm_dt_dec() {
		return r32_cpdm_dt_dec;
	}
	public void setR32_cpdm_dt_dec(BigDecimal r32_cpdm_dt_dec) {
		this.r32_cpdm_dt_dec = r32_cpdm_dt_dec;
	}
	public BigDecimal getR32_net() {
		return r32_net;
	}
	public void setR32_net(BigDecimal r32_net) {
		this.r32_net = r32_net;
	}
	public BigDecimal getR32_cpdm_dt_der() {
		return r32_cpdm_dt_der;
	}
	public void setR32_cpdm_dt_der(BigDecimal r32_cpdm_dt_der) {
		this.r32_cpdm_dt_der = r32_cpdm_dt_der;
	}
	public BigDecimal getR32_cpdm_dt_dto() {
		return r32_cpdm_dt_dto;
	}
	public void setR32_cpdm_dt_dto(BigDecimal r32_cpdm_dt_dto) {
		this.r32_cpdm_dt_dto = r32_cpdm_dt_dto;
	}
	public BigDecimal getR32_cp() {
		return r32_cp;
	}
	public void setR32_cp(BigDecimal r32_cp) {
		this.r32_cp = r32_cp;
	}
	public String getR33_product() {
		return r33_product;
	}
	public void setR33_product(String r33_product) {
		this.r33_product = r33_product;
	}
	public BigDecimal getR33_open_position() {
		return r33_open_position;
	}
	public void setR33_open_position(BigDecimal r33_open_position) {
		this.r33_open_position = r33_open_position;
	}
	public BigDecimal getR33_cpdm_dt_inc() {
		return r33_cpdm_dt_inc;
	}
	public void setR33_cpdm_dt_inc(BigDecimal r33_cpdm_dt_inc) {
		this.r33_cpdm_dt_inc = r33_cpdm_dt_inc;
	}
	public BigDecimal getR33_cpdm_dt_dec() {
		return r33_cpdm_dt_dec;
	}
	public void setR33_cpdm_dt_dec(BigDecimal r33_cpdm_dt_dec) {
		this.r33_cpdm_dt_dec = r33_cpdm_dt_dec;
	}
	public BigDecimal getR33_net() {
		return r33_net;
	}
	public void setR33_net(BigDecimal r33_net) {
		this.r33_net = r33_net;
	}
	public BigDecimal getR33_cpdm_dt_der() {
		return r33_cpdm_dt_der;
	}
	public void setR33_cpdm_dt_der(BigDecimal r33_cpdm_dt_der) {
		this.r33_cpdm_dt_der = r33_cpdm_dt_der;
	}
	public BigDecimal getR33_cpdm_dt_dto() {
		return r33_cpdm_dt_dto;
	}
	public void setR33_cpdm_dt_dto(BigDecimal r33_cpdm_dt_dto) {
		this.r33_cpdm_dt_dto = r33_cpdm_dt_dto;
	}
	public BigDecimal getR33_cp() {
		return r33_cp;
	}
	public void setR33_cp(BigDecimal r33_cp) {
		this.r33_cp = r33_cp;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_open_position() {
		return r34_open_position;
	}
	public void setR34_open_position(BigDecimal r34_open_position) {
		this.r34_open_position = r34_open_position;
	}
	public BigDecimal getR34_cpdm_dt_inc() {
		return r34_cpdm_dt_inc;
	}
	public void setR34_cpdm_dt_inc(BigDecimal r34_cpdm_dt_inc) {
		this.r34_cpdm_dt_inc = r34_cpdm_dt_inc;
	}
	public BigDecimal getR34_cpdm_dt_dec() {
		return r34_cpdm_dt_dec;
	}
	public void setR34_cpdm_dt_dec(BigDecimal r34_cpdm_dt_dec) {
		this.r34_cpdm_dt_dec = r34_cpdm_dt_dec;
	}
	public BigDecimal getR34_net() {
		return r34_net;
	}
	public void setR34_net(BigDecimal r34_net) {
		this.r34_net = r34_net;
	}
	public BigDecimal getR34_cpdm_dt_der() {
		return r34_cpdm_dt_der;
	}
	public void setR34_cpdm_dt_der(BigDecimal r34_cpdm_dt_der) {
		this.r34_cpdm_dt_der = r34_cpdm_dt_der;
	}
	public BigDecimal getR34_cpdm_dt_dto() {
		return r34_cpdm_dt_dto;
	}
	public void setR34_cpdm_dt_dto(BigDecimal r34_cpdm_dt_dto) {
		this.r34_cpdm_dt_dto = r34_cpdm_dt_dto;
	}
	public BigDecimal getR34_cp() {
		return r34_cp;
	}
	public void setR34_cp(BigDecimal r34_cp) {
		this.r34_cp = r34_cp;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_open_position() {
		return r35_open_position;
	}
	public void setR35_open_position(BigDecimal r35_open_position) {
		this.r35_open_position = r35_open_position;
	}
	public BigDecimal getR35_cpdm_dt_inc() {
		return r35_cpdm_dt_inc;
	}
	public void setR35_cpdm_dt_inc(BigDecimal r35_cpdm_dt_inc) {
		this.r35_cpdm_dt_inc = r35_cpdm_dt_inc;
	}
	public BigDecimal getR35_cpdm_dt_dec() {
		return r35_cpdm_dt_dec;
	}
	public void setR35_cpdm_dt_dec(BigDecimal r35_cpdm_dt_dec) {
		this.r35_cpdm_dt_dec = r35_cpdm_dt_dec;
	}
	public BigDecimal getR35_net() {
		return r35_net;
	}
	public void setR35_net(BigDecimal r35_net) {
		this.r35_net = r35_net;
	}
	public BigDecimal getR35_cpdm_dt_der() {
		return r35_cpdm_dt_der;
	}
	public void setR35_cpdm_dt_der(BigDecimal r35_cpdm_dt_der) {
		this.r35_cpdm_dt_der = r35_cpdm_dt_der;
	}
	public BigDecimal getR35_cpdm_dt_dto() {
		return r35_cpdm_dt_dto;
	}
	public void setR35_cpdm_dt_dto(BigDecimal r35_cpdm_dt_dto) {
		this.r35_cpdm_dt_dto = r35_cpdm_dt_dto;
	}
	public BigDecimal getR35_cp() {
		return r35_cp;
	}
	public void setR35_cp(BigDecimal r35_cp) {
		this.r35_cp = r35_cp;
	}
	public String getR36_product() {
		return r36_product;
	}
	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}
	public BigDecimal getR36_open_position() {
		return r36_open_position;
	}
	public void setR36_open_position(BigDecimal r36_open_position) {
		this.r36_open_position = r36_open_position;
	}
	public BigDecimal getR36_cpdm_dt_inc() {
		return r36_cpdm_dt_inc;
	}
	public void setR36_cpdm_dt_inc(BigDecimal r36_cpdm_dt_inc) {
		this.r36_cpdm_dt_inc = r36_cpdm_dt_inc;
	}
	public BigDecimal getR36_cpdm_dt_dec() {
		return r36_cpdm_dt_dec;
	}
	public void setR36_cpdm_dt_dec(BigDecimal r36_cpdm_dt_dec) {
		this.r36_cpdm_dt_dec = r36_cpdm_dt_dec;
	}
	public BigDecimal getR36_net() {
		return r36_net;
	}
	public void setR36_net(BigDecimal r36_net) {
		this.r36_net = r36_net;
	}
	public BigDecimal getR36_cpdm_dt_der() {
		return r36_cpdm_dt_der;
	}
	public void setR36_cpdm_dt_der(BigDecimal r36_cpdm_dt_der) {
		this.r36_cpdm_dt_der = r36_cpdm_dt_der;
	}
	public BigDecimal getR36_cpdm_dt_dto() {
		return r36_cpdm_dt_dto;
	}
	public void setR36_cpdm_dt_dto(BigDecimal r36_cpdm_dt_dto) {
		this.r36_cpdm_dt_dto = r36_cpdm_dt_dto;
	}
	public BigDecimal getR36_cp() {
		return r36_cp;
	}
	public void setR36_cp(BigDecimal r36_cp) {
		this.r36_cp = r36_cp;
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


public class M_BOP_Archival_Detail_RowMapper 
        implements RowMapper<M_BOP_Archival_Detail_Entity> {

    @Override
    public M_BOP_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_BOP_Archival_Detail_Entity obj = new M_BOP_Archival_Detail_Entity();

       // =========================
// R13
// =========================
obj.setR13_product(rs.getString("r13_product"));
obj.setR13_open_position(rs.getBigDecimal("r13_open_position"));
obj.setR13_cpdm_dt_inc(rs.getBigDecimal("r13_cpdm_dt_inc"));
obj.setR13_cpdm_dt_dec(rs.getBigDecimal("r13_cpdm_dt_dec"));
obj.setR13_net(rs.getBigDecimal("r13_net"));
obj.setR13_cpdm_dt_der(rs.getBigDecimal("r13_cpdm_dt_der"));
obj.setR13_cpdm_dt_dto(rs.getBigDecimal("r13_cpdm_dt_dto"));
obj.setR13_cp(rs.getBigDecimal("r13_cp"));


// =========================
// R14
// =========================
obj.setR14_product(rs.getString("r14_product"));
obj.setR14_open_position(rs.getBigDecimal("r14_open_position"));
obj.setR14_cpdm_dt_inc(rs.getBigDecimal("r14_cpdm_dt_inc"));
obj.setR14_cpdm_dt_dec(rs.getBigDecimal("r14_cpdm_dt_dec"));
obj.setR14_net(rs.getBigDecimal("r14_net"));
obj.setR14_cpdm_dt_der(rs.getBigDecimal("r14_cpdm_dt_der"));
obj.setR14_cpdm_dt_dto(rs.getBigDecimal("r14_cpdm_dt_dto"));
obj.setR14_cp(rs.getBigDecimal("r14_cp"));

// =========================
// R15
// =========================
obj.setR15_product(rs.getString("r15_product"));
obj.setR15_open_position(rs.getBigDecimal("r15_open_position"));
obj.setR15_cpdm_dt_inc(rs.getBigDecimal("r15_cpdm_dt_inc"));
obj.setR15_cpdm_dt_dec(rs.getBigDecimal("r15_cpdm_dt_dec"));
obj.setR15_net(rs.getBigDecimal("r15_net"));
obj.setR15_cpdm_dt_der(rs.getBigDecimal("r15_cpdm_dt_der"));
obj.setR15_cpdm_dt_dto(rs.getBigDecimal("r15_cpdm_dt_dto"));
obj.setR15_cp(rs.getBigDecimal("r15_cp"));

// =========================
// R16
// =========================
obj.setR16_product(rs.getString("r16_product"));
obj.setR16_open_position(rs.getBigDecimal("r16_open_position"));
obj.setR16_cpdm_dt_inc(rs.getBigDecimal("r16_cpdm_dt_inc"));
obj.setR16_cpdm_dt_dec(rs.getBigDecimal("r16_cpdm_dt_dec"));
obj.setR16_net(rs.getBigDecimal("r16_net"));
obj.setR16_cpdm_dt_der(rs.getBigDecimal("r16_cpdm_dt_der"));
obj.setR16_cpdm_dt_dto(rs.getBigDecimal("r16_cpdm_dt_dto"));
obj.setR16_cp(rs.getBigDecimal("r16_cp"));

// =========================
// R17
// =========================
obj.setR17_product(rs.getString("r17_product"));
obj.setR17_open_position(rs.getBigDecimal("r17_open_position"));
obj.setR17_cpdm_dt_inc(rs.getBigDecimal("r17_cpdm_dt_inc"));
obj.setR17_cpdm_dt_dec(rs.getBigDecimal("r17_cpdm_dt_dec"));
obj.setR17_net(rs.getBigDecimal("r17_net"));
obj.setR17_cpdm_dt_der(rs.getBigDecimal("r17_cpdm_dt_der"));
obj.setR17_cpdm_dt_dto(rs.getBigDecimal("r17_cpdm_dt_dto"));
obj.setR17_cp(rs.getBigDecimal("r17_cp"));

// =========================
// R18
// =========================
obj.setR18_product(rs.getString("r18_product"));
obj.setR18_open_position(rs.getBigDecimal("r18_open_position"));
obj.setR18_cpdm_dt_inc(rs.getBigDecimal("r18_cpdm_dt_inc"));
obj.setR18_cpdm_dt_dec(rs.getBigDecimal("r18_cpdm_dt_dec"));
obj.setR18_net(rs.getBigDecimal("r18_net"));
obj.setR18_cpdm_dt_der(rs.getBigDecimal("r18_cpdm_dt_der"));
obj.setR18_cpdm_dt_dto(rs.getBigDecimal("r18_cpdm_dt_dto"));
obj.setR18_cp(rs.getBigDecimal("r18_cp"));

// =========================
// R19
// =========================
obj.setR19_product(rs.getString("r19_product"));
obj.setR19_open_position(rs.getBigDecimal("r19_open_position"));
obj.setR19_cpdm_dt_inc(rs.getBigDecimal("r19_cpdm_dt_inc"));
obj.setR19_cpdm_dt_dec(rs.getBigDecimal("r19_cpdm_dt_dec"));
obj.setR19_net(rs.getBigDecimal("r19_net"));
obj.setR19_cpdm_dt_der(rs.getBigDecimal("r19_cpdm_dt_der"));
obj.setR19_cpdm_dt_dto(rs.getBigDecimal("r19_cpdm_dt_dto"));
obj.setR19_cp(rs.getBigDecimal("r19_cp"));

// =========================
// R20
// =========================
obj.setR20_product(rs.getString("r20_product"));
obj.setR20_open_position(rs.getBigDecimal("r20_open_position"));
obj.setR20_cpdm_dt_inc(rs.getBigDecimal("r20_cpdm_dt_inc"));
obj.setR20_cpdm_dt_dec(rs.getBigDecimal("r20_cpdm_dt_dec"));
obj.setR20_net(rs.getBigDecimal("r20_net"));
obj.setR20_cpdm_dt_der(rs.getBigDecimal("r20_cpdm_dt_der"));
obj.setR20_cpdm_dt_dto(rs.getBigDecimal("r20_cpdm_dt_dto"));
obj.setR20_cp(rs.getBigDecimal("r20_cp"));


// =========================
// R21
// =========================
obj.setR21_product(rs.getString("r21_product"));
obj.setR21_open_position(rs.getBigDecimal("r21_open_position"));
obj.setR21_cpdm_dt_inc(rs.getBigDecimal("r21_cpdm_dt_inc"));
obj.setR21_cpdm_dt_dec(rs.getBigDecimal("r21_cpdm_dt_dec"));
obj.setR21_net(rs.getBigDecimal("r21_net"));
obj.setR21_cpdm_dt_der(rs.getBigDecimal("r21_cpdm_dt_der"));
obj.setR21_cpdm_dt_dto(rs.getBigDecimal("r21_cpdm_dt_dto"));
obj.setR21_cp(rs.getBigDecimal("r21_cp"));

// =========================
// R22
// =========================
obj.setR22_product(rs.getString("r22_product"));
obj.setR22_open_position(rs.getBigDecimal("r22_open_position"));
obj.setR22_cpdm_dt_inc(rs.getBigDecimal("r22_cpdm_dt_inc"));
obj.setR22_cpdm_dt_dec(rs.getBigDecimal("r22_cpdm_dt_dec"));
obj.setR22_net(rs.getBigDecimal("r22_net"));
obj.setR22_cpdm_dt_der(rs.getBigDecimal("r22_cpdm_dt_der"));
obj.setR22_cpdm_dt_dto(rs.getBigDecimal("r22_cpdm_dt_dto"));
obj.setR22_cp(rs.getBigDecimal("r22_cp"));

// =========================
// R23
// =========================
obj.setR23_product(rs.getString("r23_product"));
obj.setR23_open_position(rs.getBigDecimal("r23_open_position"));
obj.setR23_cpdm_dt_inc(rs.getBigDecimal("r23_cpdm_dt_inc"));
obj.setR23_cpdm_dt_dec(rs.getBigDecimal("r23_cpdm_dt_dec"));
obj.setR23_net(rs.getBigDecimal("r23_net"));
obj.setR23_cpdm_dt_der(rs.getBigDecimal("r23_cpdm_dt_der"));
obj.setR23_cpdm_dt_dto(rs.getBigDecimal("r23_cpdm_dt_dto"));
obj.setR23_cp(rs.getBigDecimal("r23_cp"));

// =========================
// R24
// =========================
obj.setR24_product(rs.getString("r24_product"));
obj.setR24_open_position(rs.getBigDecimal("r24_open_position"));
obj.setR24_cpdm_dt_inc(rs.getBigDecimal("r24_cpdm_dt_inc"));
obj.setR24_cpdm_dt_dec(rs.getBigDecimal("r24_cpdm_dt_dec"));
obj.setR24_net(rs.getBigDecimal("r24_net"));
obj.setR24_cpdm_dt_der(rs.getBigDecimal("r24_cpdm_dt_der"));
obj.setR24_cpdm_dt_dto(rs.getBigDecimal("r24_cpdm_dt_dto"));
obj.setR24_cp(rs.getBigDecimal("r24_cp"));

// =========================
// R25
// =========================
obj.setR25_product(rs.getString("r25_product"));
obj.setR25_open_position(rs.getBigDecimal("r25_open_position"));
obj.setR25_cpdm_dt_inc(rs.getBigDecimal("r25_cpdm_dt_inc"));
obj.setR25_cpdm_dt_dec(rs.getBigDecimal("r25_cpdm_dt_dec"));
obj.setR25_net(rs.getBigDecimal("r25_net"));
obj.setR25_cpdm_dt_der(rs.getBigDecimal("r25_cpdm_dt_der"));
obj.setR25_cpdm_dt_dto(rs.getBigDecimal("r25_cpdm_dt_dto"));
obj.setR25_cp(rs.getBigDecimal("r25_cp"));

// =========================
// R26
// =========================
obj.setR26_product(rs.getString("r26_product"));
obj.setR26_open_position(rs.getBigDecimal("r26_open_position"));
obj.setR26_cpdm_dt_inc(rs.getBigDecimal("r26_cpdm_dt_inc"));
obj.setR26_cpdm_dt_dec(rs.getBigDecimal("r26_cpdm_dt_dec"));
obj.setR26_net(rs.getBigDecimal("r26_net"));
obj.setR26_cpdm_dt_der(rs.getBigDecimal("r26_cpdm_dt_der"));
obj.setR26_cpdm_dt_dto(rs.getBigDecimal("r26_cpdm_dt_dto"));
obj.setR26_cp(rs.getBigDecimal("r26_cp"));

// =========================
// R27
// =========================
obj.setR27_product(rs.getString("r27_product"));
obj.setR27_open_position(rs.getBigDecimal("r27_open_position"));
obj.setR27_cpdm_dt_inc(rs.getBigDecimal("r27_cpdm_dt_inc"));
obj.setR27_cpdm_dt_dec(rs.getBigDecimal("r27_cpdm_dt_dec"));
obj.setR27_net(rs.getBigDecimal("r27_net"));
obj.setR27_cpdm_dt_der(rs.getBigDecimal("r27_cpdm_dt_der"));
obj.setR27_cpdm_dt_dto(rs.getBigDecimal("r27_cpdm_dt_dto"));
obj.setR27_cp(rs.getBigDecimal("r27_cp"));

// =========================
// R28
// =========================
obj.setR28_product(rs.getString("r28_product"));
obj.setR28_open_position(rs.getBigDecimal("r28_open_position"));
obj.setR28_cpdm_dt_inc(rs.getBigDecimal("r28_cpdm_dt_inc"));
obj.setR28_cpdm_dt_dec(rs.getBigDecimal("r28_cpdm_dt_dec"));
obj.setR28_net(rs.getBigDecimal("r28_net"));
obj.setR28_cpdm_dt_der(rs.getBigDecimal("r28_cpdm_dt_der"));
obj.setR28_cpdm_dt_dto(rs.getBigDecimal("r28_cpdm_dt_dto"));
obj.setR28_cp(rs.getBigDecimal("r28_cp"));

// =========================
// R29
// =========================
obj.setR29_product(rs.getString("r29_product"));
obj.setR29_open_position(rs.getBigDecimal("r29_open_position"));
obj.setR29_cpdm_dt_inc(rs.getBigDecimal("r29_cpdm_dt_inc"));
obj.setR29_cpdm_dt_dec(rs.getBigDecimal("r29_cpdm_dt_dec"));
obj.setR29_net(rs.getBigDecimal("r29_net"));
obj.setR29_cpdm_dt_der(rs.getBigDecimal("r29_cpdm_dt_der"));
obj.setR29_cpdm_dt_dto(rs.getBigDecimal("r29_cpdm_dt_dto"));
obj.setR29_cp(rs.getBigDecimal("r29_cp"));

// =========================
// R30
// =========================
obj.setR30_product(rs.getString("r30_product"));
obj.setR30_open_position(rs.getBigDecimal("r30_open_position"));
obj.setR30_cpdm_dt_inc(rs.getBigDecimal("r30_cpdm_dt_inc"));
obj.setR30_cpdm_dt_dec(rs.getBigDecimal("r30_cpdm_dt_dec"));
obj.setR30_net(rs.getBigDecimal("r30_net"));
obj.setR30_cpdm_dt_der(rs.getBigDecimal("r30_cpdm_dt_der"));
obj.setR30_cpdm_dt_dto(rs.getBigDecimal("r30_cpdm_dt_dto"));
obj.setR30_cp(rs.getBigDecimal("r30_cp"));

// =========================
// R31
// =========================
obj.setR31_product(rs.getString("r31_product"));
obj.setR31_open_position(rs.getBigDecimal("r31_open_position"));
obj.setR31_cpdm_dt_inc(rs.getBigDecimal("r31_cpdm_dt_inc"));
obj.setR31_cpdm_dt_dec(rs.getBigDecimal("r31_cpdm_dt_dec"));
obj.setR31_net(rs.getBigDecimal("r31_net"));
obj.setR31_cpdm_dt_der(rs.getBigDecimal("r31_cpdm_dt_der"));
obj.setR31_cpdm_dt_dto(rs.getBigDecimal("r31_cpdm_dt_dto"));
obj.setR31_cp(rs.getBigDecimal("r31_cp"));

// =========================
// R32
// =========================
obj.setR32_product(rs.getString("r32_product"));
obj.setR32_open_position(rs.getBigDecimal("r32_open_position"));
obj.setR32_cpdm_dt_inc(rs.getBigDecimal("r32_cpdm_dt_inc"));
obj.setR32_cpdm_dt_dec(rs.getBigDecimal("r32_cpdm_dt_dec"));
obj.setR32_net(rs.getBigDecimal("r32_net"));
obj.setR32_cpdm_dt_der(rs.getBigDecimal("r32_cpdm_dt_der"));
obj.setR32_cpdm_dt_dto(rs.getBigDecimal("r32_cpdm_dt_dto"));
obj.setR32_cp(rs.getBigDecimal("r32_cp"));

// =========================
// R33
// =========================
obj.setR33_product(rs.getString("r33_product"));
obj.setR33_open_position(rs.getBigDecimal("r33_open_position"));
obj.setR33_cpdm_dt_inc(rs.getBigDecimal("r33_cpdm_dt_inc"));
obj.setR33_cpdm_dt_dec(rs.getBigDecimal("r33_cpdm_dt_dec"));
obj.setR33_net(rs.getBigDecimal("r33_net"));
obj.setR33_cpdm_dt_der(rs.getBigDecimal("r33_cpdm_dt_der"));
obj.setR33_cpdm_dt_dto(rs.getBigDecimal("r33_cpdm_dt_dto"));
obj.setR33_cp(rs.getBigDecimal("r33_cp"));

// =========================
// R34
// =========================
obj.setR34_product(rs.getString("r34_product"));
obj.setR34_open_position(rs.getBigDecimal("r34_open_position"));
obj.setR34_cpdm_dt_inc(rs.getBigDecimal("r34_cpdm_dt_inc"));
obj.setR34_cpdm_dt_dec(rs.getBigDecimal("r34_cpdm_dt_dec"));
obj.setR34_net(rs.getBigDecimal("r34_net"));
obj.setR34_cpdm_dt_der(rs.getBigDecimal("r34_cpdm_dt_der"));
obj.setR34_cpdm_dt_dto(rs.getBigDecimal("r34_cpdm_dt_dto"));
obj.setR34_cp(rs.getBigDecimal("r34_cp"));

// =========================
// R35
// =========================
obj.setR35_product(rs.getString("r35_product"));
obj.setR35_open_position(rs.getBigDecimal("r35_open_position"));
obj.setR35_cpdm_dt_inc(rs.getBigDecimal("r35_cpdm_dt_inc"));
obj.setR35_cpdm_dt_dec(rs.getBigDecimal("r35_cpdm_dt_dec"));
obj.setR35_net(rs.getBigDecimal("r35_net"));
obj.setR35_cpdm_dt_der(rs.getBigDecimal("r35_cpdm_dt_der"));
obj.setR35_cpdm_dt_dto(rs.getBigDecimal("r35_cpdm_dt_dto"));
obj.setR35_cp(rs.getBigDecimal("r35_cp"));

// =========================
// R36
// =========================
obj.setR36_product(rs.getString("r36_product"));
obj.setR36_open_position(rs.getBigDecimal("r36_open_position"));
obj.setR36_cpdm_dt_inc(rs.getBigDecimal("r36_cpdm_dt_inc"));
obj.setR36_cpdm_dt_dec(rs.getBigDecimal("r36_cpdm_dt_dec"));
obj.setR36_net(rs.getBigDecimal("r36_net"));
obj.setR36_cpdm_dt_der(rs.getBigDecimal("r36_cpdm_dt_der"));
obj.setR36_cpdm_dt_dto(rs.getBigDecimal("r36_cpdm_dt_dto"));
obj.setR36_cp(rs.getBigDecimal("r36_cp"));


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

public class M_BOP_Archival_Detail_Entity {

   
	private String	r13_product;
	private BigDecimal	r13_open_position;
	private BigDecimal	r13_cpdm_dt_inc;
	private BigDecimal	r13_cpdm_dt_dec;
	private BigDecimal	r13_net;
	private BigDecimal	r13_cpdm_dt_der;
	private BigDecimal	r13_cpdm_dt_dto;
	private BigDecimal	r13_cp;
	private String	r14_product;
	private BigDecimal	r14_open_position;
	private BigDecimal	r14_cpdm_dt_inc;
	private BigDecimal	r14_cpdm_dt_dec;
	private BigDecimal	r14_net;
	private BigDecimal	r14_cpdm_dt_der;
	private BigDecimal	r14_cpdm_dt_dto;
	private BigDecimal	r14_cp;
	private String	r15_product;
	private BigDecimal	r15_open_position;
	private BigDecimal	r15_cpdm_dt_inc;
	private BigDecimal	r15_cpdm_dt_dec;
	private BigDecimal	r15_net;
	private BigDecimal	r15_cpdm_dt_der;
	private BigDecimal	r15_cpdm_dt_dto;
	private BigDecimal	r15_cp;
	private String	r16_product;
	private BigDecimal	r16_open_position;
	private BigDecimal	r16_cpdm_dt_inc;
	private BigDecimal	r16_cpdm_dt_dec;
	private BigDecimal	r16_net;
	private BigDecimal	r16_cpdm_dt_der;
	private BigDecimal	r16_cpdm_dt_dto;
	private BigDecimal	r16_cp;
	private String	r17_product;
	private BigDecimal	r17_open_position;
	private BigDecimal	r17_cpdm_dt_inc;
	private BigDecimal	r17_cpdm_dt_dec;
	private BigDecimal	r17_net;
	private BigDecimal	r17_cpdm_dt_der;
	private BigDecimal	r17_cpdm_dt_dto;
	private BigDecimal	r17_cp;
	private String	r18_product;
	private BigDecimal	r18_open_position;
	private BigDecimal	r18_cpdm_dt_inc;
	private BigDecimal	r18_cpdm_dt_dec;
	private BigDecimal	r18_net;
	private BigDecimal	r18_cpdm_dt_der;
	private BigDecimal	r18_cpdm_dt_dto;
	private BigDecimal	r18_cp;
	private String	r19_product;
	private BigDecimal	r19_open_position;
	private BigDecimal	r19_cpdm_dt_inc;
	private BigDecimal	r19_cpdm_dt_dec;
	private BigDecimal	r19_net;
	private BigDecimal	r19_cpdm_dt_der;
	private BigDecimal	r19_cpdm_dt_dto;
	private BigDecimal	r19_cp;
	private String	r20_product;
	private BigDecimal	r20_open_position;
	private BigDecimal	r20_cpdm_dt_inc;
	private BigDecimal	r20_cpdm_dt_dec;
	private BigDecimal	r20_net;
	private BigDecimal	r20_cpdm_dt_der;
	private BigDecimal	r20_cpdm_dt_dto;
	private BigDecimal	r20_cp;
	private String	r21_product;
	private BigDecimal	r21_open_position;
	private BigDecimal	r21_cpdm_dt_inc;
	private BigDecimal	r21_cpdm_dt_dec;
	private BigDecimal	r21_net;
	private BigDecimal	r21_cpdm_dt_der;
	private BigDecimal	r21_cpdm_dt_dto;
	private BigDecimal	r21_cp;
	private String	r22_product;
	private BigDecimal	r22_open_position;
	private BigDecimal	r22_cpdm_dt_inc;
	private BigDecimal	r22_cpdm_dt_dec;
	private BigDecimal	r22_net;
	private BigDecimal	r22_cpdm_dt_der;
	private BigDecimal	r22_cpdm_dt_dto;
	private BigDecimal	r22_cp;
	private String	r23_product;
	private BigDecimal	r23_open_position;
	private BigDecimal	r23_cpdm_dt_inc;
	private BigDecimal	r23_cpdm_dt_dec;
	private BigDecimal	r23_net;
	private BigDecimal	r23_cpdm_dt_der;
	private BigDecimal	r23_cpdm_dt_dto;
	private BigDecimal	r23_cp;
	private String	r24_product;
	private BigDecimal	r24_open_position;
	private BigDecimal	r24_cpdm_dt_inc;
	private BigDecimal	r24_cpdm_dt_dec;
	private BigDecimal	r24_net;
	private BigDecimal	r24_cpdm_dt_der;
	private BigDecimal	r24_cpdm_dt_dto;
	private BigDecimal	r24_cp;
	private String	r25_product;
	private BigDecimal	r25_open_position;
	private BigDecimal	r25_cpdm_dt_inc;
	private BigDecimal	r25_cpdm_dt_dec;
	private BigDecimal	r25_net;
	private BigDecimal	r25_cpdm_dt_der;
	private BigDecimal	r25_cpdm_dt_dto;
	private BigDecimal	r25_cp;
	private String	r26_product;
	private BigDecimal	r26_open_position;
	private BigDecimal	r26_cpdm_dt_inc;
	private BigDecimal	r26_cpdm_dt_dec;
	private BigDecimal	r26_net;
	private BigDecimal	r26_cpdm_dt_der;
	private BigDecimal	r26_cpdm_dt_dto;
	private BigDecimal	r26_cp;
	private String	r27_product;
	private BigDecimal	r27_open_position;
	private BigDecimal	r27_cpdm_dt_inc;
	private BigDecimal	r27_cpdm_dt_dec;
	private BigDecimal	r27_net;
	private BigDecimal	r27_cpdm_dt_der;
	private BigDecimal	r27_cpdm_dt_dto;
	private BigDecimal	r27_cp;
	private String	r28_product;
	private BigDecimal	r28_open_position;
	private BigDecimal	r28_cpdm_dt_inc;
	private BigDecimal	r28_cpdm_dt_dec;
	private BigDecimal	r28_net;
	private BigDecimal	r28_cpdm_dt_der;
	private BigDecimal	r28_cpdm_dt_dto;
	private BigDecimal	r28_cp;
	private String	r29_product;
	private BigDecimal	r29_open_position;
	private BigDecimal	r29_cpdm_dt_inc;
	private BigDecimal	r29_cpdm_dt_dec;
	private BigDecimal	r29_net;
	private BigDecimal	r29_cpdm_dt_der;
	private BigDecimal	r29_cpdm_dt_dto;
	private BigDecimal	r29_cp;
	private String	r30_product;
	private BigDecimal	r30_open_position;
	private BigDecimal	r30_cpdm_dt_inc;
	private BigDecimal	r30_cpdm_dt_dec;
	private BigDecimal	r30_net;
	private BigDecimal	r30_cpdm_dt_der;
	private BigDecimal	r30_cpdm_dt_dto;
	private BigDecimal	r30_cp;
	private String	r31_product;
	private BigDecimal	r31_open_position;
	private BigDecimal	r31_cpdm_dt_inc;
	private BigDecimal	r31_cpdm_dt_dec;
	private BigDecimal	r31_net;
	private BigDecimal	r31_cpdm_dt_der;
	private BigDecimal	r31_cpdm_dt_dto;
	private BigDecimal	r31_cp;
	private String	r32_product;
	private BigDecimal	r32_open_position;
	private BigDecimal	r32_cpdm_dt_inc;
	private BigDecimal	r32_cpdm_dt_dec;
	private BigDecimal	r32_net;
	private BigDecimal	r32_cpdm_dt_der;
	private BigDecimal	r32_cpdm_dt_dto;
	private BigDecimal	r32_cp;
	private String	r33_product;
	private BigDecimal	r33_open_position;
	private BigDecimal	r33_cpdm_dt_inc;
	private BigDecimal	r33_cpdm_dt_dec;
	private BigDecimal	r33_net;
	private BigDecimal	r33_cpdm_dt_der;
	private BigDecimal	r33_cpdm_dt_dto;
	private BigDecimal	r33_cp;
	private String	r34_product;
	private BigDecimal	r34_open_position;
	private BigDecimal	r34_cpdm_dt_inc;
	private BigDecimal	r34_cpdm_dt_dec;
	private BigDecimal	r34_net;
	private BigDecimal	r34_cpdm_dt_der;
	private BigDecimal	r34_cpdm_dt_dto;
	private BigDecimal	r34_cp;
	private String	r35_product;
	private BigDecimal	r35_open_position;
	private BigDecimal	r35_cpdm_dt_inc;
	private BigDecimal	r35_cpdm_dt_dec;
	private BigDecimal	r35_net;
	private BigDecimal	r35_cpdm_dt_der;
	private BigDecimal	r35_cpdm_dt_dto;
	private BigDecimal	r35_cp;
	private String	r36_product;
	private BigDecimal	r36_open_position;
	private BigDecimal	r36_cpdm_dt_inc;
	private BigDecimal	r36_cpdm_dt_dec;
	private BigDecimal	r36_net;
	private BigDecimal	r36_cpdm_dt_der;
	private BigDecimal	r36_cpdm_dt_dto;
	private BigDecimal	r36_cp;
	
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
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_open_position() {
		return r13_open_position;
	}
	public void setR13_open_position(BigDecimal r13_open_position) {
		this.r13_open_position = r13_open_position;
	}
	public BigDecimal getR13_cpdm_dt_inc() {
		return r13_cpdm_dt_inc;
	}
	public void setR13_cpdm_dt_inc(BigDecimal r13_cpdm_dt_inc) {
		this.r13_cpdm_dt_inc = r13_cpdm_dt_inc;
	}
	public BigDecimal getR13_cpdm_dt_dec() {
		return r13_cpdm_dt_dec;
	}
	public void setR13_cpdm_dt_dec(BigDecimal r13_cpdm_dt_dec) {
		this.r13_cpdm_dt_dec = r13_cpdm_dt_dec;
	}
	public BigDecimal getR13_net() {
		return r13_net;
	}
	public void setR13_net(BigDecimal r13_net) {
		this.r13_net = r13_net;
	}
	public BigDecimal getR13_cpdm_dt_der() {
		return r13_cpdm_dt_der;
	}
	public void setR13_cpdm_dt_der(BigDecimal r13_cpdm_dt_der) {
		this.r13_cpdm_dt_der = r13_cpdm_dt_der;
	}
	public BigDecimal getR13_cpdm_dt_dto() {
		return r13_cpdm_dt_dto;
	}
	public void setR13_cpdm_dt_dto(BigDecimal r13_cpdm_dt_dto) {
		this.r13_cpdm_dt_dto = r13_cpdm_dt_dto;
	}
	public BigDecimal getR13_cp() {
		return r13_cp;
	}
	public void setR13_cp(BigDecimal r13_cp) {
		this.r13_cp = r13_cp;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_open_position() {
		return r14_open_position;
	}
	public void setR14_open_position(BigDecimal r14_open_position) {
		this.r14_open_position = r14_open_position;
	}
	public BigDecimal getR14_cpdm_dt_inc() {
		return r14_cpdm_dt_inc;
	}
	public void setR14_cpdm_dt_inc(BigDecimal r14_cpdm_dt_inc) {
		this.r14_cpdm_dt_inc = r14_cpdm_dt_inc;
	}
	public BigDecimal getR14_cpdm_dt_dec() {
		return r14_cpdm_dt_dec;
	}
	public void setR14_cpdm_dt_dec(BigDecimal r14_cpdm_dt_dec) {
		this.r14_cpdm_dt_dec = r14_cpdm_dt_dec;
	}
	public BigDecimal getR14_net() {
		return r14_net;
	}
	public void setR14_net(BigDecimal r14_net) {
		this.r14_net = r14_net;
	}
	public BigDecimal getR14_cpdm_dt_der() {
		return r14_cpdm_dt_der;
	}
	public void setR14_cpdm_dt_der(BigDecimal r14_cpdm_dt_der) {
		this.r14_cpdm_dt_der = r14_cpdm_dt_der;
	}
	public BigDecimal getR14_cpdm_dt_dto() {
		return r14_cpdm_dt_dto;
	}
	public void setR14_cpdm_dt_dto(BigDecimal r14_cpdm_dt_dto) {
		this.r14_cpdm_dt_dto = r14_cpdm_dt_dto;
	}
	public BigDecimal getR14_cp() {
		return r14_cp;
	}
	public void setR14_cp(BigDecimal r14_cp) {
		this.r14_cp = r14_cp;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_open_position() {
		return r15_open_position;
	}
	public void setR15_open_position(BigDecimal r15_open_position) {
		this.r15_open_position = r15_open_position;
	}
	public BigDecimal getR15_cpdm_dt_inc() {
		return r15_cpdm_dt_inc;
	}
	public void setR15_cpdm_dt_inc(BigDecimal r15_cpdm_dt_inc) {
		this.r15_cpdm_dt_inc = r15_cpdm_dt_inc;
	}
	public BigDecimal getR15_cpdm_dt_dec() {
		return r15_cpdm_dt_dec;
	}
	public void setR15_cpdm_dt_dec(BigDecimal r15_cpdm_dt_dec) {
		this.r15_cpdm_dt_dec = r15_cpdm_dt_dec;
	}
	public BigDecimal getR15_net() {
		return r15_net;
	}
	public void setR15_net(BigDecimal r15_net) {
		this.r15_net = r15_net;
	}
	public BigDecimal getR15_cpdm_dt_der() {
		return r15_cpdm_dt_der;
	}
	public void setR15_cpdm_dt_der(BigDecimal r15_cpdm_dt_der) {
		this.r15_cpdm_dt_der = r15_cpdm_dt_der;
	}
	public BigDecimal getR15_cpdm_dt_dto() {
		return r15_cpdm_dt_dto;
	}
	public void setR15_cpdm_dt_dto(BigDecimal r15_cpdm_dt_dto) {
		this.r15_cpdm_dt_dto = r15_cpdm_dt_dto;
	}
	public BigDecimal getR15_cp() {
		return r15_cp;
	}
	public void setR15_cp(BigDecimal r15_cp) {
		this.r15_cp = r15_cp;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_open_position() {
		return r16_open_position;
	}
	public void setR16_open_position(BigDecimal r16_open_position) {
		this.r16_open_position = r16_open_position;
	}
	public BigDecimal getR16_cpdm_dt_inc() {
		return r16_cpdm_dt_inc;
	}
	public void setR16_cpdm_dt_inc(BigDecimal r16_cpdm_dt_inc) {
		this.r16_cpdm_dt_inc = r16_cpdm_dt_inc;
	}
	public BigDecimal getR16_cpdm_dt_dec() {
		return r16_cpdm_dt_dec;
	}
	public void setR16_cpdm_dt_dec(BigDecimal r16_cpdm_dt_dec) {
		this.r16_cpdm_dt_dec = r16_cpdm_dt_dec;
	}
	public BigDecimal getR16_net() {
		return r16_net;
	}
	public void setR16_net(BigDecimal r16_net) {
		this.r16_net = r16_net;
	}
	public BigDecimal getR16_cpdm_dt_der() {
		return r16_cpdm_dt_der;
	}
	public void setR16_cpdm_dt_der(BigDecimal r16_cpdm_dt_der) {
		this.r16_cpdm_dt_der = r16_cpdm_dt_der;
	}
	public BigDecimal getR16_cpdm_dt_dto() {
		return r16_cpdm_dt_dto;
	}
	public void setR16_cpdm_dt_dto(BigDecimal r16_cpdm_dt_dto) {
		this.r16_cpdm_dt_dto = r16_cpdm_dt_dto;
	}
	public BigDecimal getR16_cp() {
		return r16_cp;
	}
	public void setR16_cp(BigDecimal r16_cp) {
		this.r16_cp = r16_cp;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_open_position() {
		return r17_open_position;
	}
	public void setR17_open_position(BigDecimal r17_open_position) {
		this.r17_open_position = r17_open_position;
	}
	public BigDecimal getR17_cpdm_dt_inc() {
		return r17_cpdm_dt_inc;
	}
	public void setR17_cpdm_dt_inc(BigDecimal r17_cpdm_dt_inc) {
		this.r17_cpdm_dt_inc = r17_cpdm_dt_inc;
	}
	public BigDecimal getR17_cpdm_dt_dec() {
		return r17_cpdm_dt_dec;
	}
	public void setR17_cpdm_dt_dec(BigDecimal r17_cpdm_dt_dec) {
		this.r17_cpdm_dt_dec = r17_cpdm_dt_dec;
	}
	public BigDecimal getR17_net() {
		return r17_net;
	}
	public void setR17_net(BigDecimal r17_net) {
		this.r17_net = r17_net;
	}
	public BigDecimal getR17_cpdm_dt_der() {
		return r17_cpdm_dt_der;
	}
	public void setR17_cpdm_dt_der(BigDecimal r17_cpdm_dt_der) {
		this.r17_cpdm_dt_der = r17_cpdm_dt_der;
	}
	public BigDecimal getR17_cpdm_dt_dto() {
		return r17_cpdm_dt_dto;
	}
	public void setR17_cpdm_dt_dto(BigDecimal r17_cpdm_dt_dto) {
		this.r17_cpdm_dt_dto = r17_cpdm_dt_dto;
	}
	public BigDecimal getR17_cp() {
		return r17_cp;
	}
	public void setR17_cp(BigDecimal r17_cp) {
		this.r17_cp = r17_cp;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_open_position() {
		return r18_open_position;
	}
	public void setR18_open_position(BigDecimal r18_open_position) {
		this.r18_open_position = r18_open_position;
	}
	public BigDecimal getR18_cpdm_dt_inc() {
		return r18_cpdm_dt_inc;
	}
	public void setR18_cpdm_dt_inc(BigDecimal r18_cpdm_dt_inc) {
		this.r18_cpdm_dt_inc = r18_cpdm_dt_inc;
	}
	public BigDecimal getR18_cpdm_dt_dec() {
		return r18_cpdm_dt_dec;
	}
	public void setR18_cpdm_dt_dec(BigDecimal r18_cpdm_dt_dec) {
		this.r18_cpdm_dt_dec = r18_cpdm_dt_dec;
	}
	public BigDecimal getR18_net() {
		return r18_net;
	}
	public void setR18_net(BigDecimal r18_net) {
		this.r18_net = r18_net;
	}
	public BigDecimal getR18_cpdm_dt_der() {
		return r18_cpdm_dt_der;
	}
	public void setR18_cpdm_dt_der(BigDecimal r18_cpdm_dt_der) {
		this.r18_cpdm_dt_der = r18_cpdm_dt_der;
	}
	public BigDecimal getR18_cpdm_dt_dto() {
		return r18_cpdm_dt_dto;
	}
	public void setR18_cpdm_dt_dto(BigDecimal r18_cpdm_dt_dto) {
		this.r18_cpdm_dt_dto = r18_cpdm_dt_dto;
	}
	public BigDecimal getR18_cp() {
		return r18_cp;
	}
	public void setR18_cp(BigDecimal r18_cp) {
		this.r18_cp = r18_cp;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_open_position() {
		return r19_open_position;
	}
	public void setR19_open_position(BigDecimal r19_open_position) {
		this.r19_open_position = r19_open_position;
	}
	public BigDecimal getR19_cpdm_dt_inc() {
		return r19_cpdm_dt_inc;
	}
	public void setR19_cpdm_dt_inc(BigDecimal r19_cpdm_dt_inc) {
		this.r19_cpdm_dt_inc = r19_cpdm_dt_inc;
	}
	public BigDecimal getR19_cpdm_dt_dec() {
		return r19_cpdm_dt_dec;
	}
	public void setR19_cpdm_dt_dec(BigDecimal r19_cpdm_dt_dec) {
		this.r19_cpdm_dt_dec = r19_cpdm_dt_dec;
	}
	public BigDecimal getR19_net() {
		return r19_net;
	}
	public void setR19_net(BigDecimal r19_net) {
		this.r19_net = r19_net;
	}
	public BigDecimal getR19_cpdm_dt_der() {
		return r19_cpdm_dt_der;
	}
	public void setR19_cpdm_dt_der(BigDecimal r19_cpdm_dt_der) {
		this.r19_cpdm_dt_der = r19_cpdm_dt_der;
	}
	public BigDecimal getR19_cpdm_dt_dto() {
		return r19_cpdm_dt_dto;
	}
	public void setR19_cpdm_dt_dto(BigDecimal r19_cpdm_dt_dto) {
		this.r19_cpdm_dt_dto = r19_cpdm_dt_dto;
	}
	public BigDecimal getR19_cp() {
		return r19_cp;
	}
	public void setR19_cp(BigDecimal r19_cp) {
		this.r19_cp = r19_cp;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_open_position() {
		return r20_open_position;
	}
	public void setR20_open_position(BigDecimal r20_open_position) {
		this.r20_open_position = r20_open_position;
	}
	public BigDecimal getR20_cpdm_dt_inc() {
		return r20_cpdm_dt_inc;
	}
	public void setR20_cpdm_dt_inc(BigDecimal r20_cpdm_dt_inc) {
		this.r20_cpdm_dt_inc = r20_cpdm_dt_inc;
	}
	public BigDecimal getR20_cpdm_dt_dec() {
		return r20_cpdm_dt_dec;
	}
	public void setR20_cpdm_dt_dec(BigDecimal r20_cpdm_dt_dec) {
		this.r20_cpdm_dt_dec = r20_cpdm_dt_dec;
	}
	public BigDecimal getR20_net() {
		return r20_net;
	}
	public void setR20_net(BigDecimal r20_net) {
		this.r20_net = r20_net;
	}
	public BigDecimal getR20_cpdm_dt_der() {
		return r20_cpdm_dt_der;
	}
	public void setR20_cpdm_dt_der(BigDecimal r20_cpdm_dt_der) {
		this.r20_cpdm_dt_der = r20_cpdm_dt_der;
	}
	public BigDecimal getR20_cpdm_dt_dto() {
		return r20_cpdm_dt_dto;
	}
	public void setR20_cpdm_dt_dto(BigDecimal r20_cpdm_dt_dto) {
		this.r20_cpdm_dt_dto = r20_cpdm_dt_dto;
	}
	public BigDecimal getR20_cp() {
		return r20_cp;
	}
	public void setR20_cp(BigDecimal r20_cp) {
		this.r20_cp = r20_cp;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_open_position() {
		return r21_open_position;
	}
	public void setR21_open_position(BigDecimal r21_open_position) {
		this.r21_open_position = r21_open_position;
	}
	public BigDecimal getR21_cpdm_dt_inc() {
		return r21_cpdm_dt_inc;
	}
	public void setR21_cpdm_dt_inc(BigDecimal r21_cpdm_dt_inc) {
		this.r21_cpdm_dt_inc = r21_cpdm_dt_inc;
	}
	public BigDecimal getR21_cpdm_dt_dec() {
		return r21_cpdm_dt_dec;
	}
	public void setR21_cpdm_dt_dec(BigDecimal r21_cpdm_dt_dec) {
		this.r21_cpdm_dt_dec = r21_cpdm_dt_dec;
	}
	public BigDecimal getR21_net() {
		return r21_net;
	}
	public void setR21_net(BigDecimal r21_net) {
		this.r21_net = r21_net;
	}
	public BigDecimal getR21_cpdm_dt_der() {
		return r21_cpdm_dt_der;
	}
	public void setR21_cpdm_dt_der(BigDecimal r21_cpdm_dt_der) {
		this.r21_cpdm_dt_der = r21_cpdm_dt_der;
	}
	public BigDecimal getR21_cpdm_dt_dto() {
		return r21_cpdm_dt_dto;
	}
	public void setR21_cpdm_dt_dto(BigDecimal r21_cpdm_dt_dto) {
		this.r21_cpdm_dt_dto = r21_cpdm_dt_dto;
	}
	public BigDecimal getR21_cp() {
		return r21_cp;
	}
	public void setR21_cp(BigDecimal r21_cp) {
		this.r21_cp = r21_cp;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_open_position() {
		return r22_open_position;
	}
	public void setR22_open_position(BigDecimal r22_open_position) {
		this.r22_open_position = r22_open_position;
	}
	public BigDecimal getR22_cpdm_dt_inc() {
		return r22_cpdm_dt_inc;
	}
	public void setR22_cpdm_dt_inc(BigDecimal r22_cpdm_dt_inc) {
		this.r22_cpdm_dt_inc = r22_cpdm_dt_inc;
	}
	public BigDecimal getR22_cpdm_dt_dec() {
		return r22_cpdm_dt_dec;
	}
	public void setR22_cpdm_dt_dec(BigDecimal r22_cpdm_dt_dec) {
		this.r22_cpdm_dt_dec = r22_cpdm_dt_dec;
	}
	public BigDecimal getR22_net() {
		return r22_net;
	}
	public void setR22_net(BigDecimal r22_net) {
		this.r22_net = r22_net;
	}
	public BigDecimal getR22_cpdm_dt_der() {
		return r22_cpdm_dt_der;
	}
	public void setR22_cpdm_dt_der(BigDecimal r22_cpdm_dt_der) {
		this.r22_cpdm_dt_der = r22_cpdm_dt_der;
	}
	public BigDecimal getR22_cpdm_dt_dto() {
		return r22_cpdm_dt_dto;
	}
	public void setR22_cpdm_dt_dto(BigDecimal r22_cpdm_dt_dto) {
		this.r22_cpdm_dt_dto = r22_cpdm_dt_dto;
	}
	public BigDecimal getR22_cp() {
		return r22_cp;
	}
	public void setR22_cp(BigDecimal r22_cp) {
		this.r22_cp = r22_cp;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_open_position() {
		return r23_open_position;
	}
	public void setR23_open_position(BigDecimal r23_open_position) {
		this.r23_open_position = r23_open_position;
	}
	public BigDecimal getR23_cpdm_dt_inc() {
		return r23_cpdm_dt_inc;
	}
	public void setR23_cpdm_dt_inc(BigDecimal r23_cpdm_dt_inc) {
		this.r23_cpdm_dt_inc = r23_cpdm_dt_inc;
	}
	public BigDecimal getR23_cpdm_dt_dec() {
		return r23_cpdm_dt_dec;
	}
	public void setR23_cpdm_dt_dec(BigDecimal r23_cpdm_dt_dec) {
		this.r23_cpdm_dt_dec = r23_cpdm_dt_dec;
	}
	public BigDecimal getR23_net() {
		return r23_net;
	}
	public void setR23_net(BigDecimal r23_net) {
		this.r23_net = r23_net;
	}
	public BigDecimal getR23_cpdm_dt_der() {
		return r23_cpdm_dt_der;
	}
	public void setR23_cpdm_dt_der(BigDecimal r23_cpdm_dt_der) {
		this.r23_cpdm_dt_der = r23_cpdm_dt_der;
	}
	public BigDecimal getR23_cpdm_dt_dto() {
		return r23_cpdm_dt_dto;
	}
	public void setR23_cpdm_dt_dto(BigDecimal r23_cpdm_dt_dto) {
		this.r23_cpdm_dt_dto = r23_cpdm_dt_dto;
	}
	public BigDecimal getR23_cp() {
		return r23_cp;
	}
	public void setR23_cp(BigDecimal r23_cp) {
		this.r23_cp = r23_cp;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_open_position() {
		return r24_open_position;
	}
	public void setR24_open_position(BigDecimal r24_open_position) {
		this.r24_open_position = r24_open_position;
	}
	public BigDecimal getR24_cpdm_dt_inc() {
		return r24_cpdm_dt_inc;
	}
	public void setR24_cpdm_dt_inc(BigDecimal r24_cpdm_dt_inc) {
		this.r24_cpdm_dt_inc = r24_cpdm_dt_inc;
	}
	public BigDecimal getR24_cpdm_dt_dec() {
		return r24_cpdm_dt_dec;
	}
	public void setR24_cpdm_dt_dec(BigDecimal r24_cpdm_dt_dec) {
		this.r24_cpdm_dt_dec = r24_cpdm_dt_dec;
	}
	public BigDecimal getR24_net() {
		return r24_net;
	}
	public void setR24_net(BigDecimal r24_net) {
		this.r24_net = r24_net;
	}
	public BigDecimal getR24_cpdm_dt_der() {
		return r24_cpdm_dt_der;
	}
	public void setR24_cpdm_dt_der(BigDecimal r24_cpdm_dt_der) {
		this.r24_cpdm_dt_der = r24_cpdm_dt_der;
	}
	public BigDecimal getR24_cpdm_dt_dto() {
		return r24_cpdm_dt_dto;
	}
	public void setR24_cpdm_dt_dto(BigDecimal r24_cpdm_dt_dto) {
		this.r24_cpdm_dt_dto = r24_cpdm_dt_dto;
	}
	public BigDecimal getR24_cp() {
		return r24_cp;
	}
	public void setR24_cp(BigDecimal r24_cp) {
		this.r24_cp = r24_cp;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_open_position() {
		return r25_open_position;
	}
	public void setR25_open_position(BigDecimal r25_open_position) {
		this.r25_open_position = r25_open_position;
	}
	public BigDecimal getR25_cpdm_dt_inc() {
		return r25_cpdm_dt_inc;
	}
	public void setR25_cpdm_dt_inc(BigDecimal r25_cpdm_dt_inc) {
		this.r25_cpdm_dt_inc = r25_cpdm_dt_inc;
	}
	public BigDecimal getR25_cpdm_dt_dec() {
		return r25_cpdm_dt_dec;
	}
	public void setR25_cpdm_dt_dec(BigDecimal r25_cpdm_dt_dec) {
		this.r25_cpdm_dt_dec = r25_cpdm_dt_dec;
	}
	public BigDecimal getR25_net() {
		return r25_net;
	}
	public void setR25_net(BigDecimal r25_net) {
		this.r25_net = r25_net;
	}
	public BigDecimal getR25_cpdm_dt_der() {
		return r25_cpdm_dt_der;
	}
	public void setR25_cpdm_dt_der(BigDecimal r25_cpdm_dt_der) {
		this.r25_cpdm_dt_der = r25_cpdm_dt_der;
	}
	public BigDecimal getR25_cpdm_dt_dto() {
		return r25_cpdm_dt_dto;
	}
	public void setR25_cpdm_dt_dto(BigDecimal r25_cpdm_dt_dto) {
		this.r25_cpdm_dt_dto = r25_cpdm_dt_dto;
	}
	public BigDecimal getR25_cp() {
		return r25_cp;
	}
	public void setR25_cp(BigDecimal r25_cp) {
		this.r25_cp = r25_cp;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_open_position() {
		return r26_open_position;
	}
	public void setR26_open_position(BigDecimal r26_open_position) {
		this.r26_open_position = r26_open_position;
	}
	public BigDecimal getR26_cpdm_dt_inc() {
		return r26_cpdm_dt_inc;
	}
	public void setR26_cpdm_dt_inc(BigDecimal r26_cpdm_dt_inc) {
		this.r26_cpdm_dt_inc = r26_cpdm_dt_inc;
	}
	public BigDecimal getR26_cpdm_dt_dec() {
		return r26_cpdm_dt_dec;
	}
	public void setR26_cpdm_dt_dec(BigDecimal r26_cpdm_dt_dec) {
		this.r26_cpdm_dt_dec = r26_cpdm_dt_dec;
	}
	public BigDecimal getR26_net() {
		return r26_net;
	}
	public void setR26_net(BigDecimal r26_net) {
		this.r26_net = r26_net;
	}
	public BigDecimal getR26_cpdm_dt_der() {
		return r26_cpdm_dt_der;
	}
	public void setR26_cpdm_dt_der(BigDecimal r26_cpdm_dt_der) {
		this.r26_cpdm_dt_der = r26_cpdm_dt_der;
	}
	public BigDecimal getR26_cpdm_dt_dto() {
		return r26_cpdm_dt_dto;
	}
	public void setR26_cpdm_dt_dto(BigDecimal r26_cpdm_dt_dto) {
		this.r26_cpdm_dt_dto = r26_cpdm_dt_dto;
	}
	public BigDecimal getR26_cp() {
		return r26_cp;
	}
	public void setR26_cp(BigDecimal r26_cp) {
		this.r26_cp = r26_cp;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_open_position() {
		return r27_open_position;
	}
	public void setR27_open_position(BigDecimal r27_open_position) {
		this.r27_open_position = r27_open_position;
	}
	public BigDecimal getR27_cpdm_dt_inc() {
		return r27_cpdm_dt_inc;
	}
	public void setR27_cpdm_dt_inc(BigDecimal r27_cpdm_dt_inc) {
		this.r27_cpdm_dt_inc = r27_cpdm_dt_inc;
	}
	public BigDecimal getR27_cpdm_dt_dec() {
		return r27_cpdm_dt_dec;
	}
	public void setR27_cpdm_dt_dec(BigDecimal r27_cpdm_dt_dec) {
		this.r27_cpdm_dt_dec = r27_cpdm_dt_dec;
	}
	public BigDecimal getR27_net() {
		return r27_net;
	}
	public void setR27_net(BigDecimal r27_net) {
		this.r27_net = r27_net;
	}
	public BigDecimal getR27_cpdm_dt_der() {
		return r27_cpdm_dt_der;
	}
	public void setR27_cpdm_dt_der(BigDecimal r27_cpdm_dt_der) {
		this.r27_cpdm_dt_der = r27_cpdm_dt_der;
	}
	public BigDecimal getR27_cpdm_dt_dto() {
		return r27_cpdm_dt_dto;
	}
	public void setR27_cpdm_dt_dto(BigDecimal r27_cpdm_dt_dto) {
		this.r27_cpdm_dt_dto = r27_cpdm_dt_dto;
	}
	public BigDecimal getR27_cp() {
		return r27_cp;
	}
	public void setR27_cp(BigDecimal r27_cp) {
		this.r27_cp = r27_cp;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_open_position() {
		return r28_open_position;
	}
	public void setR28_open_position(BigDecimal r28_open_position) {
		this.r28_open_position = r28_open_position;
	}
	public BigDecimal getR28_cpdm_dt_inc() {
		return r28_cpdm_dt_inc;
	}
	public void setR28_cpdm_dt_inc(BigDecimal r28_cpdm_dt_inc) {
		this.r28_cpdm_dt_inc = r28_cpdm_dt_inc;
	}
	public BigDecimal getR28_cpdm_dt_dec() {
		return r28_cpdm_dt_dec;
	}
	public void setR28_cpdm_dt_dec(BigDecimal r28_cpdm_dt_dec) {
		this.r28_cpdm_dt_dec = r28_cpdm_dt_dec;
	}
	public BigDecimal getR28_net() {
		return r28_net;
	}
	public void setR28_net(BigDecimal r28_net) {
		this.r28_net = r28_net;
	}
	public BigDecimal getR28_cpdm_dt_der() {
		return r28_cpdm_dt_der;
	}
	public void setR28_cpdm_dt_der(BigDecimal r28_cpdm_dt_der) {
		this.r28_cpdm_dt_der = r28_cpdm_dt_der;
	}
	public BigDecimal getR28_cpdm_dt_dto() {
		return r28_cpdm_dt_dto;
	}
	public void setR28_cpdm_dt_dto(BigDecimal r28_cpdm_dt_dto) {
		this.r28_cpdm_dt_dto = r28_cpdm_dt_dto;
	}
	public BigDecimal getR28_cp() {
		return r28_cp;
	}
	public void setR28_cp(BigDecimal r28_cp) {
		this.r28_cp = r28_cp;
	}
	public String getR29_product() {
		return r29_product;
	}
	public void setR29_product(String r29_product) {
		this.r29_product = r29_product;
	}
	public BigDecimal getR29_open_position() {
		return r29_open_position;
	}
	public void setR29_open_position(BigDecimal r29_open_position) {
		this.r29_open_position = r29_open_position;
	}
	public BigDecimal getR29_cpdm_dt_inc() {
		return r29_cpdm_dt_inc;
	}
	public void setR29_cpdm_dt_inc(BigDecimal r29_cpdm_dt_inc) {
		this.r29_cpdm_dt_inc = r29_cpdm_dt_inc;
	}
	public BigDecimal getR29_cpdm_dt_dec() {
		return r29_cpdm_dt_dec;
	}
	public void setR29_cpdm_dt_dec(BigDecimal r29_cpdm_dt_dec) {
		this.r29_cpdm_dt_dec = r29_cpdm_dt_dec;
	}
	public BigDecimal getR29_net() {
		return r29_net;
	}
	public void setR29_net(BigDecimal r29_net) {
		this.r29_net = r29_net;
	}
	public BigDecimal getR29_cpdm_dt_der() {
		return r29_cpdm_dt_der;
	}
	public void setR29_cpdm_dt_der(BigDecimal r29_cpdm_dt_der) {
		this.r29_cpdm_dt_der = r29_cpdm_dt_der;
	}
	public BigDecimal getR29_cpdm_dt_dto() {
		return r29_cpdm_dt_dto;
	}
	public void setR29_cpdm_dt_dto(BigDecimal r29_cpdm_dt_dto) {
		this.r29_cpdm_dt_dto = r29_cpdm_dt_dto;
	}
	public BigDecimal getR29_cp() {
		return r29_cp;
	}
	public void setR29_cp(BigDecimal r29_cp) {
		this.r29_cp = r29_cp;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_open_position() {
		return r30_open_position;
	}
	public void setR30_open_position(BigDecimal r30_open_position) {
		this.r30_open_position = r30_open_position;
	}
	public BigDecimal getR30_cpdm_dt_inc() {
		return r30_cpdm_dt_inc;
	}
	public void setR30_cpdm_dt_inc(BigDecimal r30_cpdm_dt_inc) {
		this.r30_cpdm_dt_inc = r30_cpdm_dt_inc;
	}
	public BigDecimal getR30_cpdm_dt_dec() {
		return r30_cpdm_dt_dec;
	}
	public void setR30_cpdm_dt_dec(BigDecimal r30_cpdm_dt_dec) {
		this.r30_cpdm_dt_dec = r30_cpdm_dt_dec;
	}
	public BigDecimal getR30_net() {
		return r30_net;
	}
	public void setR30_net(BigDecimal r30_net) {
		this.r30_net = r30_net;
	}
	public BigDecimal getR30_cpdm_dt_der() {
		return r30_cpdm_dt_der;
	}
	public void setR30_cpdm_dt_der(BigDecimal r30_cpdm_dt_der) {
		this.r30_cpdm_dt_der = r30_cpdm_dt_der;
	}
	public BigDecimal getR30_cpdm_dt_dto() {
		return r30_cpdm_dt_dto;
	}
	public void setR30_cpdm_dt_dto(BigDecimal r30_cpdm_dt_dto) {
		this.r30_cpdm_dt_dto = r30_cpdm_dt_dto;
	}
	public BigDecimal getR30_cp() {
		return r30_cp;
	}
	public void setR30_cp(BigDecimal r30_cp) {
		this.r30_cp = r30_cp;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_open_position() {
		return r31_open_position;
	}
	public void setR31_open_position(BigDecimal r31_open_position) {
		this.r31_open_position = r31_open_position;
	}
	public BigDecimal getR31_cpdm_dt_inc() {
		return r31_cpdm_dt_inc;
	}
	public void setR31_cpdm_dt_inc(BigDecimal r31_cpdm_dt_inc) {
		this.r31_cpdm_dt_inc = r31_cpdm_dt_inc;
	}
	public BigDecimal getR31_cpdm_dt_dec() {
		return r31_cpdm_dt_dec;
	}
	public void setR31_cpdm_dt_dec(BigDecimal r31_cpdm_dt_dec) {
		this.r31_cpdm_dt_dec = r31_cpdm_dt_dec;
	}
	public BigDecimal getR31_net() {
		return r31_net;
	}
	public void setR31_net(BigDecimal r31_net) {
		this.r31_net = r31_net;
	}
	public BigDecimal getR31_cpdm_dt_der() {
		return r31_cpdm_dt_der;
	}
	public void setR31_cpdm_dt_der(BigDecimal r31_cpdm_dt_der) {
		this.r31_cpdm_dt_der = r31_cpdm_dt_der;
	}
	public BigDecimal getR31_cpdm_dt_dto() {
		return r31_cpdm_dt_dto;
	}
	public void setR31_cpdm_dt_dto(BigDecimal r31_cpdm_dt_dto) {
		this.r31_cpdm_dt_dto = r31_cpdm_dt_dto;
	}
	public BigDecimal getR31_cp() {
		return r31_cp;
	}
	public void setR31_cp(BigDecimal r31_cp) {
		this.r31_cp = r31_cp;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_open_position() {
		return r32_open_position;
	}
	public void setR32_open_position(BigDecimal r32_open_position) {
		this.r32_open_position = r32_open_position;
	}
	public BigDecimal getR32_cpdm_dt_inc() {
		return r32_cpdm_dt_inc;
	}
	public void setR32_cpdm_dt_inc(BigDecimal r32_cpdm_dt_inc) {
		this.r32_cpdm_dt_inc = r32_cpdm_dt_inc;
	}
	public BigDecimal getR32_cpdm_dt_dec() {
		return r32_cpdm_dt_dec;
	}
	public void setR32_cpdm_dt_dec(BigDecimal r32_cpdm_dt_dec) {
		this.r32_cpdm_dt_dec = r32_cpdm_dt_dec;
	}
	public BigDecimal getR32_net() {
		return r32_net;
	}
	public void setR32_net(BigDecimal r32_net) {
		this.r32_net = r32_net;
	}
	public BigDecimal getR32_cpdm_dt_der() {
		return r32_cpdm_dt_der;
	}
	public void setR32_cpdm_dt_der(BigDecimal r32_cpdm_dt_der) {
		this.r32_cpdm_dt_der = r32_cpdm_dt_der;
	}
	public BigDecimal getR32_cpdm_dt_dto() {
		return r32_cpdm_dt_dto;
	}
	public void setR32_cpdm_dt_dto(BigDecimal r32_cpdm_dt_dto) {
		this.r32_cpdm_dt_dto = r32_cpdm_dt_dto;
	}
	public BigDecimal getR32_cp() {
		return r32_cp;
	}
	public void setR32_cp(BigDecimal r32_cp) {
		this.r32_cp = r32_cp;
	}
	public String getR33_product() {
		return r33_product;
	}
	public void setR33_product(String r33_product) {
		this.r33_product = r33_product;
	}
	public BigDecimal getR33_open_position() {
		return r33_open_position;
	}
	public void setR33_open_position(BigDecimal r33_open_position) {
		this.r33_open_position = r33_open_position;
	}
	public BigDecimal getR33_cpdm_dt_inc() {
		return r33_cpdm_dt_inc;
	}
	public void setR33_cpdm_dt_inc(BigDecimal r33_cpdm_dt_inc) {
		this.r33_cpdm_dt_inc = r33_cpdm_dt_inc;
	}
	public BigDecimal getR33_cpdm_dt_dec() {
		return r33_cpdm_dt_dec;
	}
	public void setR33_cpdm_dt_dec(BigDecimal r33_cpdm_dt_dec) {
		this.r33_cpdm_dt_dec = r33_cpdm_dt_dec;
	}
	public BigDecimal getR33_net() {
		return r33_net;
	}
	public void setR33_net(BigDecimal r33_net) {
		this.r33_net = r33_net;
	}
	public BigDecimal getR33_cpdm_dt_der() {
		return r33_cpdm_dt_der;
	}
	public void setR33_cpdm_dt_der(BigDecimal r33_cpdm_dt_der) {
		this.r33_cpdm_dt_der = r33_cpdm_dt_der;
	}
	public BigDecimal getR33_cpdm_dt_dto() {
		return r33_cpdm_dt_dto;
	}
	public void setR33_cpdm_dt_dto(BigDecimal r33_cpdm_dt_dto) {
		this.r33_cpdm_dt_dto = r33_cpdm_dt_dto;
	}
	public BigDecimal getR33_cp() {
		return r33_cp;
	}
	public void setR33_cp(BigDecimal r33_cp) {
		this.r33_cp = r33_cp;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_open_position() {
		return r34_open_position;
	}
	public void setR34_open_position(BigDecimal r34_open_position) {
		this.r34_open_position = r34_open_position;
	}
	public BigDecimal getR34_cpdm_dt_inc() {
		return r34_cpdm_dt_inc;
	}
	public void setR34_cpdm_dt_inc(BigDecimal r34_cpdm_dt_inc) {
		this.r34_cpdm_dt_inc = r34_cpdm_dt_inc;
	}
	public BigDecimal getR34_cpdm_dt_dec() {
		return r34_cpdm_dt_dec;
	}
	public void setR34_cpdm_dt_dec(BigDecimal r34_cpdm_dt_dec) {
		this.r34_cpdm_dt_dec = r34_cpdm_dt_dec;
	}
	public BigDecimal getR34_net() {
		return r34_net;
	}
	public void setR34_net(BigDecimal r34_net) {
		this.r34_net = r34_net;
	}
	public BigDecimal getR34_cpdm_dt_der() {
		return r34_cpdm_dt_der;
	}
	public void setR34_cpdm_dt_der(BigDecimal r34_cpdm_dt_der) {
		this.r34_cpdm_dt_der = r34_cpdm_dt_der;
	}
	public BigDecimal getR34_cpdm_dt_dto() {
		return r34_cpdm_dt_dto;
	}
	public void setR34_cpdm_dt_dto(BigDecimal r34_cpdm_dt_dto) {
		this.r34_cpdm_dt_dto = r34_cpdm_dt_dto;
	}
	public BigDecimal getR34_cp() {
		return r34_cp;
	}
	public void setR34_cp(BigDecimal r34_cp) {
		this.r34_cp = r34_cp;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_open_position() {
		return r35_open_position;
	}
	public void setR35_open_position(BigDecimal r35_open_position) {
		this.r35_open_position = r35_open_position;
	}
	public BigDecimal getR35_cpdm_dt_inc() {
		return r35_cpdm_dt_inc;
	}
	public void setR35_cpdm_dt_inc(BigDecimal r35_cpdm_dt_inc) {
		this.r35_cpdm_dt_inc = r35_cpdm_dt_inc;
	}
	public BigDecimal getR35_cpdm_dt_dec() {
		return r35_cpdm_dt_dec;
	}
	public void setR35_cpdm_dt_dec(BigDecimal r35_cpdm_dt_dec) {
		this.r35_cpdm_dt_dec = r35_cpdm_dt_dec;
	}
	public BigDecimal getR35_net() {
		return r35_net;
	}
	public void setR35_net(BigDecimal r35_net) {
		this.r35_net = r35_net;
	}
	public BigDecimal getR35_cpdm_dt_der() {
		return r35_cpdm_dt_der;
	}
	public void setR35_cpdm_dt_der(BigDecimal r35_cpdm_dt_der) {
		this.r35_cpdm_dt_der = r35_cpdm_dt_der;
	}
	public BigDecimal getR35_cpdm_dt_dto() {
		return r35_cpdm_dt_dto;
	}
	public void setR35_cpdm_dt_dto(BigDecimal r35_cpdm_dt_dto) {
		this.r35_cpdm_dt_dto = r35_cpdm_dt_dto;
	}
	public BigDecimal getR35_cp() {
		return r35_cp;
	}
	public void setR35_cp(BigDecimal r35_cp) {
		this.r35_cp = r35_cp;
	}
	public String getR36_product() {
		return r36_product;
	}
	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}
	public BigDecimal getR36_open_position() {
		return r36_open_position;
	}
	public void setR36_open_position(BigDecimal r36_open_position) {
		this.r36_open_position = r36_open_position;
	}
	public BigDecimal getR36_cpdm_dt_inc() {
		return r36_cpdm_dt_inc;
	}
	public void setR36_cpdm_dt_inc(BigDecimal r36_cpdm_dt_inc) {
		this.r36_cpdm_dt_inc = r36_cpdm_dt_inc;
	}
	public BigDecimal getR36_cpdm_dt_dec() {
		return r36_cpdm_dt_dec;
	}
	public void setR36_cpdm_dt_dec(BigDecimal r36_cpdm_dt_dec) {
		this.r36_cpdm_dt_dec = r36_cpdm_dt_dec;
	}
	public BigDecimal getR36_net() {
		return r36_net;
	}
	public void setR36_net(BigDecimal r36_net) {
		this.r36_net = r36_net;
	}
	public BigDecimal getR36_cpdm_dt_der() {
		return r36_cpdm_dt_der;
	}
	public void setR36_cpdm_dt_der(BigDecimal r36_cpdm_dt_der) {
		this.r36_cpdm_dt_der = r36_cpdm_dt_der;
	}
	public BigDecimal getR36_cpdm_dt_dto() {
		return r36_cpdm_dt_dto;
	}
	public void setR36_cpdm_dt_dto(BigDecimal r36_cpdm_dt_dto) {
		this.r36_cpdm_dt_dto = r36_cpdm_dt_dto;
	}
	public BigDecimal getR36_cp() {
		return r36_cp;
	}
	public void setR36_cp(BigDecimal r36_cp) {
		this.r36_cp = r36_cp;
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


//====================================================================================================================================
// RESUB summary M_BOP
//=====================================================


public class M_BOP_RESUB_Summary_RowMapper 
        implements RowMapper<M_BOP_RESUB_Summary_Entity> {

    @Override
    public M_BOP_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_BOP_RESUB_Summary_Entity obj = new M_BOP_RESUB_Summary_Entity();

// =========================
// R13
// =========================
obj.setR13_product(rs.getString("r13_product"));
obj.setR13_open_position(rs.getBigDecimal("r13_open_position"));
obj.setR13_cpdm_dt_inc(rs.getBigDecimal("r13_cpdm_dt_inc"));
obj.setR13_cpdm_dt_dec(rs.getBigDecimal("r13_cpdm_dt_dec"));
obj.setR13_net(rs.getBigDecimal("r13_net"));
obj.setR13_cpdm_dt_der(rs.getBigDecimal("r13_cpdm_dt_der"));
obj.setR13_cpdm_dt_dto(rs.getBigDecimal("r13_cpdm_dt_dto"));
obj.setR13_cp(rs.getBigDecimal("r13_cp"));


// =========================
// R14
// =========================
obj.setR14_product(rs.getString("r14_product"));
obj.setR14_open_position(rs.getBigDecimal("r14_open_position"));
obj.setR14_cpdm_dt_inc(rs.getBigDecimal("r14_cpdm_dt_inc"));
obj.setR14_cpdm_dt_dec(rs.getBigDecimal("r14_cpdm_dt_dec"));
obj.setR14_net(rs.getBigDecimal("r14_net"));
obj.setR14_cpdm_dt_der(rs.getBigDecimal("r14_cpdm_dt_der"));
obj.setR14_cpdm_dt_dto(rs.getBigDecimal("r14_cpdm_dt_dto"));
obj.setR14_cp(rs.getBigDecimal("r14_cp"));

// =========================
// R15
// =========================
obj.setR15_product(rs.getString("r15_product"));
obj.setR15_open_position(rs.getBigDecimal("r15_open_position"));
obj.setR15_cpdm_dt_inc(rs.getBigDecimal("r15_cpdm_dt_inc"));
obj.setR15_cpdm_dt_dec(rs.getBigDecimal("r15_cpdm_dt_dec"));
obj.setR15_net(rs.getBigDecimal("r15_net"));
obj.setR15_cpdm_dt_der(rs.getBigDecimal("r15_cpdm_dt_der"));
obj.setR15_cpdm_dt_dto(rs.getBigDecimal("r15_cpdm_dt_dto"));
obj.setR15_cp(rs.getBigDecimal("r15_cp"));

// =========================
// R16
// =========================
obj.setR16_product(rs.getString("r16_product"));
obj.setR16_open_position(rs.getBigDecimal("r16_open_position"));
obj.setR16_cpdm_dt_inc(rs.getBigDecimal("r16_cpdm_dt_inc"));
obj.setR16_cpdm_dt_dec(rs.getBigDecimal("r16_cpdm_dt_dec"));
obj.setR16_net(rs.getBigDecimal("r16_net"));
obj.setR16_cpdm_dt_der(rs.getBigDecimal("r16_cpdm_dt_der"));
obj.setR16_cpdm_dt_dto(rs.getBigDecimal("r16_cpdm_dt_dto"));
obj.setR16_cp(rs.getBigDecimal("r16_cp"));

// =========================
// R17
// =========================
obj.setR17_product(rs.getString("r17_product"));
obj.setR17_open_position(rs.getBigDecimal("r17_open_position"));
obj.setR17_cpdm_dt_inc(rs.getBigDecimal("r17_cpdm_dt_inc"));
obj.setR17_cpdm_dt_dec(rs.getBigDecimal("r17_cpdm_dt_dec"));
obj.setR17_net(rs.getBigDecimal("r17_net"));
obj.setR17_cpdm_dt_der(rs.getBigDecimal("r17_cpdm_dt_der"));
obj.setR17_cpdm_dt_dto(rs.getBigDecimal("r17_cpdm_dt_dto"));
obj.setR17_cp(rs.getBigDecimal("r17_cp"));

// =========================
// R18
// =========================
obj.setR18_product(rs.getString("r18_product"));
obj.setR18_open_position(rs.getBigDecimal("r18_open_position"));
obj.setR18_cpdm_dt_inc(rs.getBigDecimal("r18_cpdm_dt_inc"));
obj.setR18_cpdm_dt_dec(rs.getBigDecimal("r18_cpdm_dt_dec"));
obj.setR18_net(rs.getBigDecimal("r18_net"));
obj.setR18_cpdm_dt_der(rs.getBigDecimal("r18_cpdm_dt_der"));
obj.setR18_cpdm_dt_dto(rs.getBigDecimal("r18_cpdm_dt_dto"));
obj.setR18_cp(rs.getBigDecimal("r18_cp"));

// =========================
// R19
// =========================
obj.setR19_product(rs.getString("r19_product"));
obj.setR19_open_position(rs.getBigDecimal("r19_open_position"));
obj.setR19_cpdm_dt_inc(rs.getBigDecimal("r19_cpdm_dt_inc"));
obj.setR19_cpdm_dt_dec(rs.getBigDecimal("r19_cpdm_dt_dec"));
obj.setR19_net(rs.getBigDecimal("r19_net"));
obj.setR19_cpdm_dt_der(rs.getBigDecimal("r19_cpdm_dt_der"));
obj.setR19_cpdm_dt_dto(rs.getBigDecimal("r19_cpdm_dt_dto"));
obj.setR19_cp(rs.getBigDecimal("r19_cp"));

// =========================
// R20
// =========================
obj.setR20_product(rs.getString("r20_product"));
obj.setR20_open_position(rs.getBigDecimal("r20_open_position"));
obj.setR20_cpdm_dt_inc(rs.getBigDecimal("r20_cpdm_dt_inc"));
obj.setR20_cpdm_dt_dec(rs.getBigDecimal("r20_cpdm_dt_dec"));
obj.setR20_net(rs.getBigDecimal("r20_net"));
obj.setR20_cpdm_dt_der(rs.getBigDecimal("r20_cpdm_dt_der"));
obj.setR20_cpdm_dt_dto(rs.getBigDecimal("r20_cpdm_dt_dto"));
obj.setR20_cp(rs.getBigDecimal("r20_cp"));


// =========================
// R21
// =========================
obj.setR21_product(rs.getString("r21_product"));
obj.setR21_open_position(rs.getBigDecimal("r21_open_position"));
obj.setR21_cpdm_dt_inc(rs.getBigDecimal("r21_cpdm_dt_inc"));
obj.setR21_cpdm_dt_dec(rs.getBigDecimal("r21_cpdm_dt_dec"));
obj.setR21_net(rs.getBigDecimal("r21_net"));
obj.setR21_cpdm_dt_der(rs.getBigDecimal("r21_cpdm_dt_der"));
obj.setR21_cpdm_dt_dto(rs.getBigDecimal("r21_cpdm_dt_dto"));
obj.setR21_cp(rs.getBigDecimal("r21_cp"));

// =========================
// R22
// =========================
obj.setR22_product(rs.getString("r22_product"));
obj.setR22_open_position(rs.getBigDecimal("r22_open_position"));
obj.setR22_cpdm_dt_inc(rs.getBigDecimal("r22_cpdm_dt_inc"));
obj.setR22_cpdm_dt_dec(rs.getBigDecimal("r22_cpdm_dt_dec"));
obj.setR22_net(rs.getBigDecimal("r22_net"));
obj.setR22_cpdm_dt_der(rs.getBigDecimal("r22_cpdm_dt_der"));
obj.setR22_cpdm_dt_dto(rs.getBigDecimal("r22_cpdm_dt_dto"));
obj.setR22_cp(rs.getBigDecimal("r22_cp"));

// =========================
// R23
// =========================
obj.setR23_product(rs.getString("r23_product"));
obj.setR23_open_position(rs.getBigDecimal("r23_open_position"));
obj.setR23_cpdm_dt_inc(rs.getBigDecimal("r23_cpdm_dt_inc"));
obj.setR23_cpdm_dt_dec(rs.getBigDecimal("r23_cpdm_dt_dec"));
obj.setR23_net(rs.getBigDecimal("r23_net"));
obj.setR23_cpdm_dt_der(rs.getBigDecimal("r23_cpdm_dt_der"));
obj.setR23_cpdm_dt_dto(rs.getBigDecimal("r23_cpdm_dt_dto"));
obj.setR23_cp(rs.getBigDecimal("r23_cp"));

// =========================
// R24
// =========================
obj.setR24_product(rs.getString("r24_product"));
obj.setR24_open_position(rs.getBigDecimal("r24_open_position"));
obj.setR24_cpdm_dt_inc(rs.getBigDecimal("r24_cpdm_dt_inc"));
obj.setR24_cpdm_dt_dec(rs.getBigDecimal("r24_cpdm_dt_dec"));
obj.setR24_net(rs.getBigDecimal("r24_net"));
obj.setR24_cpdm_dt_der(rs.getBigDecimal("r24_cpdm_dt_der"));
obj.setR24_cpdm_dt_dto(rs.getBigDecimal("r24_cpdm_dt_dto"));
obj.setR24_cp(rs.getBigDecimal("r24_cp"));

// =========================
// R25
// =========================
obj.setR25_product(rs.getString("r25_product"));
obj.setR25_open_position(rs.getBigDecimal("r25_open_position"));
obj.setR25_cpdm_dt_inc(rs.getBigDecimal("r25_cpdm_dt_inc"));
obj.setR25_cpdm_dt_dec(rs.getBigDecimal("r25_cpdm_dt_dec"));
obj.setR25_net(rs.getBigDecimal("r25_net"));
obj.setR25_cpdm_dt_der(rs.getBigDecimal("r25_cpdm_dt_der"));
obj.setR25_cpdm_dt_dto(rs.getBigDecimal("r25_cpdm_dt_dto"));
obj.setR25_cp(rs.getBigDecimal("r25_cp"));

// =========================
// R26
// =========================
obj.setR26_product(rs.getString("r26_product"));
obj.setR26_open_position(rs.getBigDecimal("r26_open_position"));
obj.setR26_cpdm_dt_inc(rs.getBigDecimal("r26_cpdm_dt_inc"));
obj.setR26_cpdm_dt_dec(rs.getBigDecimal("r26_cpdm_dt_dec"));
obj.setR26_net(rs.getBigDecimal("r26_net"));
obj.setR26_cpdm_dt_der(rs.getBigDecimal("r26_cpdm_dt_der"));
obj.setR26_cpdm_dt_dto(rs.getBigDecimal("r26_cpdm_dt_dto"));
obj.setR26_cp(rs.getBigDecimal("r26_cp"));

// =========================
// R27
// =========================
obj.setR27_product(rs.getString("r27_product"));
obj.setR27_open_position(rs.getBigDecimal("r27_open_position"));
obj.setR27_cpdm_dt_inc(rs.getBigDecimal("r27_cpdm_dt_inc"));
obj.setR27_cpdm_dt_dec(rs.getBigDecimal("r27_cpdm_dt_dec"));
obj.setR27_net(rs.getBigDecimal("r27_net"));
obj.setR27_cpdm_dt_der(rs.getBigDecimal("r27_cpdm_dt_der"));
obj.setR27_cpdm_dt_dto(rs.getBigDecimal("r27_cpdm_dt_dto"));
obj.setR27_cp(rs.getBigDecimal("r27_cp"));

// =========================
// R28
// =========================
obj.setR28_product(rs.getString("r28_product"));
obj.setR28_open_position(rs.getBigDecimal("r28_open_position"));
obj.setR28_cpdm_dt_inc(rs.getBigDecimal("r28_cpdm_dt_inc"));
obj.setR28_cpdm_dt_dec(rs.getBigDecimal("r28_cpdm_dt_dec"));
obj.setR28_net(rs.getBigDecimal("r28_net"));
obj.setR28_cpdm_dt_der(rs.getBigDecimal("r28_cpdm_dt_der"));
obj.setR28_cpdm_dt_dto(rs.getBigDecimal("r28_cpdm_dt_dto"));
obj.setR28_cp(rs.getBigDecimal("r28_cp"));

// =========================
// R29
// =========================
obj.setR29_product(rs.getString("r29_product"));
obj.setR29_open_position(rs.getBigDecimal("r29_open_position"));
obj.setR29_cpdm_dt_inc(rs.getBigDecimal("r29_cpdm_dt_inc"));
obj.setR29_cpdm_dt_dec(rs.getBigDecimal("r29_cpdm_dt_dec"));
obj.setR29_net(rs.getBigDecimal("r29_net"));
obj.setR29_cpdm_dt_der(rs.getBigDecimal("r29_cpdm_dt_der"));
obj.setR29_cpdm_dt_dto(rs.getBigDecimal("r29_cpdm_dt_dto"));
obj.setR29_cp(rs.getBigDecimal("r29_cp"));

// =========================
// R30
// =========================
obj.setR30_product(rs.getString("r30_product"));
obj.setR30_open_position(rs.getBigDecimal("r30_open_position"));
obj.setR30_cpdm_dt_inc(rs.getBigDecimal("r30_cpdm_dt_inc"));
obj.setR30_cpdm_dt_dec(rs.getBigDecimal("r30_cpdm_dt_dec"));
obj.setR30_net(rs.getBigDecimal("r30_net"));
obj.setR30_cpdm_dt_der(rs.getBigDecimal("r30_cpdm_dt_der"));
obj.setR30_cpdm_dt_dto(rs.getBigDecimal("r30_cpdm_dt_dto"));
obj.setR30_cp(rs.getBigDecimal("r30_cp"));

// =========================
// R31
// =========================
obj.setR31_product(rs.getString("r31_product"));
obj.setR31_open_position(rs.getBigDecimal("r31_open_position"));
obj.setR31_cpdm_dt_inc(rs.getBigDecimal("r31_cpdm_dt_inc"));
obj.setR31_cpdm_dt_dec(rs.getBigDecimal("r31_cpdm_dt_dec"));
obj.setR31_net(rs.getBigDecimal("r31_net"));
obj.setR31_cpdm_dt_der(rs.getBigDecimal("r31_cpdm_dt_der"));
obj.setR31_cpdm_dt_dto(rs.getBigDecimal("r31_cpdm_dt_dto"));
obj.setR31_cp(rs.getBigDecimal("r31_cp"));

// =========================
// R32
// =========================
obj.setR32_product(rs.getString("r32_product"));
obj.setR32_open_position(rs.getBigDecimal("r32_open_position"));
obj.setR32_cpdm_dt_inc(rs.getBigDecimal("r32_cpdm_dt_inc"));
obj.setR32_cpdm_dt_dec(rs.getBigDecimal("r32_cpdm_dt_dec"));
obj.setR32_net(rs.getBigDecimal("r32_net"));
obj.setR32_cpdm_dt_der(rs.getBigDecimal("r32_cpdm_dt_der"));
obj.setR32_cpdm_dt_dto(rs.getBigDecimal("r32_cpdm_dt_dto"));
obj.setR32_cp(rs.getBigDecimal("r32_cp"));

// =========================
// R33
// =========================
obj.setR33_product(rs.getString("r33_product"));
obj.setR33_open_position(rs.getBigDecimal("r33_open_position"));
obj.setR33_cpdm_dt_inc(rs.getBigDecimal("r33_cpdm_dt_inc"));
obj.setR33_cpdm_dt_dec(rs.getBigDecimal("r33_cpdm_dt_dec"));
obj.setR33_net(rs.getBigDecimal("r33_net"));
obj.setR33_cpdm_dt_der(rs.getBigDecimal("r33_cpdm_dt_der"));
obj.setR33_cpdm_dt_dto(rs.getBigDecimal("r33_cpdm_dt_dto"));
obj.setR33_cp(rs.getBigDecimal("r33_cp"));

// =========================
// R34
// =========================
obj.setR34_product(rs.getString("r34_product"));
obj.setR34_open_position(rs.getBigDecimal("r34_open_position"));
obj.setR34_cpdm_dt_inc(rs.getBigDecimal("r34_cpdm_dt_inc"));
obj.setR34_cpdm_dt_dec(rs.getBigDecimal("r34_cpdm_dt_dec"));
obj.setR34_net(rs.getBigDecimal("r34_net"));
obj.setR34_cpdm_dt_der(rs.getBigDecimal("r34_cpdm_dt_der"));
obj.setR34_cpdm_dt_dto(rs.getBigDecimal("r34_cpdm_dt_dto"));
obj.setR34_cp(rs.getBigDecimal("r34_cp"));

// =========================
// R35
// =========================
obj.setR35_product(rs.getString("r35_product"));
obj.setR35_open_position(rs.getBigDecimal("r35_open_position"));
obj.setR35_cpdm_dt_inc(rs.getBigDecimal("r35_cpdm_dt_inc"));
obj.setR35_cpdm_dt_dec(rs.getBigDecimal("r35_cpdm_dt_dec"));
obj.setR35_net(rs.getBigDecimal("r35_net"));
obj.setR35_cpdm_dt_der(rs.getBigDecimal("r35_cpdm_dt_der"));
obj.setR35_cpdm_dt_dto(rs.getBigDecimal("r35_cpdm_dt_dto"));
obj.setR35_cp(rs.getBigDecimal("r35_cp"));

// =========================
// R36
// =========================
obj.setR36_product(rs.getString("r36_product"));
obj.setR36_open_position(rs.getBigDecimal("r36_open_position"));
obj.setR36_cpdm_dt_inc(rs.getBigDecimal("r36_cpdm_dt_inc"));
obj.setR36_cpdm_dt_dec(rs.getBigDecimal("r36_cpdm_dt_dec"));
obj.setR36_net(rs.getBigDecimal("r36_net"));
obj.setR36_cpdm_dt_der(rs.getBigDecimal("r36_cpdm_dt_der"));
obj.setR36_cpdm_dt_dto(rs.getBigDecimal("r36_cpdm_dt_dto"));
obj.setR36_cp(rs.getBigDecimal("r36_cp"));


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

public class M_BOP_RESUB_Summary_Entity {

   
	private String	r13_product;
	private BigDecimal	r13_open_position;
	private BigDecimal	r13_cpdm_dt_inc;
	private BigDecimal	r13_cpdm_dt_dec;
	private BigDecimal	r13_net;
	private BigDecimal	r13_cpdm_dt_der;
	private BigDecimal	r13_cpdm_dt_dto;
	private BigDecimal	r13_cp;
	private String	r14_product;
	private BigDecimal	r14_open_position;
	private BigDecimal	r14_cpdm_dt_inc;
	private BigDecimal	r14_cpdm_dt_dec;
	private BigDecimal	r14_net;
	private BigDecimal	r14_cpdm_dt_der;
	private BigDecimal	r14_cpdm_dt_dto;
	private BigDecimal	r14_cp;
	private String	r15_product;
	private BigDecimal	r15_open_position;
	private BigDecimal	r15_cpdm_dt_inc;
	private BigDecimal	r15_cpdm_dt_dec;
	private BigDecimal	r15_net;
	private BigDecimal	r15_cpdm_dt_der;
	private BigDecimal	r15_cpdm_dt_dto;
	private BigDecimal	r15_cp;
	private String	r16_product;
	private BigDecimal	r16_open_position;
	private BigDecimal	r16_cpdm_dt_inc;
	private BigDecimal	r16_cpdm_dt_dec;
	private BigDecimal	r16_net;
	private BigDecimal	r16_cpdm_dt_der;
	private BigDecimal	r16_cpdm_dt_dto;
	private BigDecimal	r16_cp;
	private String	r17_product;
	private BigDecimal	r17_open_position;
	private BigDecimal	r17_cpdm_dt_inc;
	private BigDecimal	r17_cpdm_dt_dec;
	private BigDecimal	r17_net;
	private BigDecimal	r17_cpdm_dt_der;
	private BigDecimal	r17_cpdm_dt_dto;
	private BigDecimal	r17_cp;
	private String	r18_product;
	private BigDecimal	r18_open_position;
	private BigDecimal	r18_cpdm_dt_inc;
	private BigDecimal	r18_cpdm_dt_dec;
	private BigDecimal	r18_net;
	private BigDecimal	r18_cpdm_dt_der;
	private BigDecimal	r18_cpdm_dt_dto;
	private BigDecimal	r18_cp;
	private String	r19_product;
	private BigDecimal	r19_open_position;
	private BigDecimal	r19_cpdm_dt_inc;
	private BigDecimal	r19_cpdm_dt_dec;
	private BigDecimal	r19_net;
	private BigDecimal	r19_cpdm_dt_der;
	private BigDecimal	r19_cpdm_dt_dto;
	private BigDecimal	r19_cp;
	private String	r20_product;
	private BigDecimal	r20_open_position;
	private BigDecimal	r20_cpdm_dt_inc;
	private BigDecimal	r20_cpdm_dt_dec;
	private BigDecimal	r20_net;
	private BigDecimal	r20_cpdm_dt_der;
	private BigDecimal	r20_cpdm_dt_dto;
	private BigDecimal	r20_cp;
	private String	r21_product;
	private BigDecimal	r21_open_position;
	private BigDecimal	r21_cpdm_dt_inc;
	private BigDecimal	r21_cpdm_dt_dec;
	private BigDecimal	r21_net;
	private BigDecimal	r21_cpdm_dt_der;
	private BigDecimal	r21_cpdm_dt_dto;
	private BigDecimal	r21_cp;
	private String	r22_product;
	private BigDecimal	r22_open_position;
	private BigDecimal	r22_cpdm_dt_inc;
	private BigDecimal	r22_cpdm_dt_dec;
	private BigDecimal	r22_net;
	private BigDecimal	r22_cpdm_dt_der;
	private BigDecimal	r22_cpdm_dt_dto;
	private BigDecimal	r22_cp;
	private String	r23_product;
	private BigDecimal	r23_open_position;
	private BigDecimal	r23_cpdm_dt_inc;
	private BigDecimal	r23_cpdm_dt_dec;
	private BigDecimal	r23_net;
	private BigDecimal	r23_cpdm_dt_der;
	private BigDecimal	r23_cpdm_dt_dto;
	private BigDecimal	r23_cp;
	private String	r24_product;
	private BigDecimal	r24_open_position;
	private BigDecimal	r24_cpdm_dt_inc;
	private BigDecimal	r24_cpdm_dt_dec;
	private BigDecimal	r24_net;
	private BigDecimal	r24_cpdm_dt_der;
	private BigDecimal	r24_cpdm_dt_dto;
	private BigDecimal	r24_cp;
	private String	r25_product;
	private BigDecimal	r25_open_position;
	private BigDecimal	r25_cpdm_dt_inc;
	private BigDecimal	r25_cpdm_dt_dec;
	private BigDecimal	r25_net;
	private BigDecimal	r25_cpdm_dt_der;
	private BigDecimal	r25_cpdm_dt_dto;
	private BigDecimal	r25_cp;
	private String	r26_product;
	private BigDecimal	r26_open_position;
	private BigDecimal	r26_cpdm_dt_inc;
	private BigDecimal	r26_cpdm_dt_dec;
	private BigDecimal	r26_net;
	private BigDecimal	r26_cpdm_dt_der;
	private BigDecimal	r26_cpdm_dt_dto;
	private BigDecimal	r26_cp;
	private String	r27_product;
	private BigDecimal	r27_open_position;
	private BigDecimal	r27_cpdm_dt_inc;
	private BigDecimal	r27_cpdm_dt_dec;
	private BigDecimal	r27_net;
	private BigDecimal	r27_cpdm_dt_der;
	private BigDecimal	r27_cpdm_dt_dto;
	private BigDecimal	r27_cp;
	private String	r28_product;
	private BigDecimal	r28_open_position;
	private BigDecimal	r28_cpdm_dt_inc;
	private BigDecimal	r28_cpdm_dt_dec;
	private BigDecimal	r28_net;
	private BigDecimal	r28_cpdm_dt_der;
	private BigDecimal	r28_cpdm_dt_dto;
	private BigDecimal	r28_cp;
	private String	r29_product;
	private BigDecimal	r29_open_position;
	private BigDecimal	r29_cpdm_dt_inc;
	private BigDecimal	r29_cpdm_dt_dec;
	private BigDecimal	r29_net;
	private BigDecimal	r29_cpdm_dt_der;
	private BigDecimal	r29_cpdm_dt_dto;
	private BigDecimal	r29_cp;
	private String	r30_product;
	private BigDecimal	r30_open_position;
	private BigDecimal	r30_cpdm_dt_inc;
	private BigDecimal	r30_cpdm_dt_dec;
	private BigDecimal	r30_net;
	private BigDecimal	r30_cpdm_dt_der;
	private BigDecimal	r30_cpdm_dt_dto;
	private BigDecimal	r30_cp;
	private String	r31_product;
	private BigDecimal	r31_open_position;
	private BigDecimal	r31_cpdm_dt_inc;
	private BigDecimal	r31_cpdm_dt_dec;
	private BigDecimal	r31_net;
	private BigDecimal	r31_cpdm_dt_der;
	private BigDecimal	r31_cpdm_dt_dto;
	private BigDecimal	r31_cp;
	private String	r32_product;
	private BigDecimal	r32_open_position;
	private BigDecimal	r32_cpdm_dt_inc;
	private BigDecimal	r32_cpdm_dt_dec;
	private BigDecimal	r32_net;
	private BigDecimal	r32_cpdm_dt_der;
	private BigDecimal	r32_cpdm_dt_dto;
	private BigDecimal	r32_cp;
	private String	r33_product;
	private BigDecimal	r33_open_position;
	private BigDecimal	r33_cpdm_dt_inc;
	private BigDecimal	r33_cpdm_dt_dec;
	private BigDecimal	r33_net;
	private BigDecimal	r33_cpdm_dt_der;
	private BigDecimal	r33_cpdm_dt_dto;
	private BigDecimal	r33_cp;
	private String	r34_product;
	private BigDecimal	r34_open_position;
	private BigDecimal	r34_cpdm_dt_inc;
	private BigDecimal	r34_cpdm_dt_dec;
	private BigDecimal	r34_net;
	private BigDecimal	r34_cpdm_dt_der;
	private BigDecimal	r34_cpdm_dt_dto;
	private BigDecimal	r34_cp;
	private String	r35_product;
	private BigDecimal	r35_open_position;
	private BigDecimal	r35_cpdm_dt_inc;
	private BigDecimal	r35_cpdm_dt_dec;
	private BigDecimal	r35_net;
	private BigDecimal	r35_cpdm_dt_der;
	private BigDecimal	r35_cpdm_dt_dto;
	private BigDecimal	r35_cp;
	private String	r36_product;
	private BigDecimal	r36_open_position;
	private BigDecimal	r36_cpdm_dt_inc;
	private BigDecimal	r36_cpdm_dt_dec;
	private BigDecimal	r36_net;
	private BigDecimal	r36_cpdm_dt_der;
	private BigDecimal	r36_cpdm_dt_dto;
	private BigDecimal	r36_cp;
	
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
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_open_position() {
		return r13_open_position;
	}
	public void setR13_open_position(BigDecimal r13_open_position) {
		this.r13_open_position = r13_open_position;
	}
	public BigDecimal getR13_cpdm_dt_inc() {
		return r13_cpdm_dt_inc;
	}
	public void setR13_cpdm_dt_inc(BigDecimal r13_cpdm_dt_inc) {
		this.r13_cpdm_dt_inc = r13_cpdm_dt_inc;
	}
	public BigDecimal getR13_cpdm_dt_dec() {
		return r13_cpdm_dt_dec;
	}
	public void setR13_cpdm_dt_dec(BigDecimal r13_cpdm_dt_dec) {
		this.r13_cpdm_dt_dec = r13_cpdm_dt_dec;
	}
	public BigDecimal getR13_net() {
		return r13_net;
	}
	public void setR13_net(BigDecimal r13_net) {
		this.r13_net = r13_net;
	}
	public BigDecimal getR13_cpdm_dt_der() {
		return r13_cpdm_dt_der;
	}
	public void setR13_cpdm_dt_der(BigDecimal r13_cpdm_dt_der) {
		this.r13_cpdm_dt_der = r13_cpdm_dt_der;
	}
	public BigDecimal getR13_cpdm_dt_dto() {
		return r13_cpdm_dt_dto;
	}
	public void setR13_cpdm_dt_dto(BigDecimal r13_cpdm_dt_dto) {
		this.r13_cpdm_dt_dto = r13_cpdm_dt_dto;
	}
	public BigDecimal getR13_cp() {
		return r13_cp;
	}
	public void setR13_cp(BigDecimal r13_cp) {
		this.r13_cp = r13_cp;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_open_position() {
		return r14_open_position;
	}
	public void setR14_open_position(BigDecimal r14_open_position) {
		this.r14_open_position = r14_open_position;
	}
	public BigDecimal getR14_cpdm_dt_inc() {
		return r14_cpdm_dt_inc;
	}
	public void setR14_cpdm_dt_inc(BigDecimal r14_cpdm_dt_inc) {
		this.r14_cpdm_dt_inc = r14_cpdm_dt_inc;
	}
	public BigDecimal getR14_cpdm_dt_dec() {
		return r14_cpdm_dt_dec;
	}
	public void setR14_cpdm_dt_dec(BigDecimal r14_cpdm_dt_dec) {
		this.r14_cpdm_dt_dec = r14_cpdm_dt_dec;
	}
	public BigDecimal getR14_net() {
		return r14_net;
	}
	public void setR14_net(BigDecimal r14_net) {
		this.r14_net = r14_net;
	}
	public BigDecimal getR14_cpdm_dt_der() {
		return r14_cpdm_dt_der;
	}
	public void setR14_cpdm_dt_der(BigDecimal r14_cpdm_dt_der) {
		this.r14_cpdm_dt_der = r14_cpdm_dt_der;
	}
	public BigDecimal getR14_cpdm_dt_dto() {
		return r14_cpdm_dt_dto;
	}
	public void setR14_cpdm_dt_dto(BigDecimal r14_cpdm_dt_dto) {
		this.r14_cpdm_dt_dto = r14_cpdm_dt_dto;
	}
	public BigDecimal getR14_cp() {
		return r14_cp;
	}
	public void setR14_cp(BigDecimal r14_cp) {
		this.r14_cp = r14_cp;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_open_position() {
		return r15_open_position;
	}
	public void setR15_open_position(BigDecimal r15_open_position) {
		this.r15_open_position = r15_open_position;
	}
	public BigDecimal getR15_cpdm_dt_inc() {
		return r15_cpdm_dt_inc;
	}
	public void setR15_cpdm_dt_inc(BigDecimal r15_cpdm_dt_inc) {
		this.r15_cpdm_dt_inc = r15_cpdm_dt_inc;
	}
	public BigDecimal getR15_cpdm_dt_dec() {
		return r15_cpdm_dt_dec;
	}
	public void setR15_cpdm_dt_dec(BigDecimal r15_cpdm_dt_dec) {
		this.r15_cpdm_dt_dec = r15_cpdm_dt_dec;
	}
	public BigDecimal getR15_net() {
		return r15_net;
	}
	public void setR15_net(BigDecimal r15_net) {
		this.r15_net = r15_net;
	}
	public BigDecimal getR15_cpdm_dt_der() {
		return r15_cpdm_dt_der;
	}
	public void setR15_cpdm_dt_der(BigDecimal r15_cpdm_dt_der) {
		this.r15_cpdm_dt_der = r15_cpdm_dt_der;
	}
	public BigDecimal getR15_cpdm_dt_dto() {
		return r15_cpdm_dt_dto;
	}
	public void setR15_cpdm_dt_dto(BigDecimal r15_cpdm_dt_dto) {
		this.r15_cpdm_dt_dto = r15_cpdm_dt_dto;
	}
	public BigDecimal getR15_cp() {
		return r15_cp;
	}
	public void setR15_cp(BigDecimal r15_cp) {
		this.r15_cp = r15_cp;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_open_position() {
		return r16_open_position;
	}
	public void setR16_open_position(BigDecimal r16_open_position) {
		this.r16_open_position = r16_open_position;
	}
	public BigDecimal getR16_cpdm_dt_inc() {
		return r16_cpdm_dt_inc;
	}
	public void setR16_cpdm_dt_inc(BigDecimal r16_cpdm_dt_inc) {
		this.r16_cpdm_dt_inc = r16_cpdm_dt_inc;
	}
	public BigDecimal getR16_cpdm_dt_dec() {
		return r16_cpdm_dt_dec;
	}
	public void setR16_cpdm_dt_dec(BigDecimal r16_cpdm_dt_dec) {
		this.r16_cpdm_dt_dec = r16_cpdm_dt_dec;
	}
	public BigDecimal getR16_net() {
		return r16_net;
	}
	public void setR16_net(BigDecimal r16_net) {
		this.r16_net = r16_net;
	}
	public BigDecimal getR16_cpdm_dt_der() {
		return r16_cpdm_dt_der;
	}
	public void setR16_cpdm_dt_der(BigDecimal r16_cpdm_dt_der) {
		this.r16_cpdm_dt_der = r16_cpdm_dt_der;
	}
	public BigDecimal getR16_cpdm_dt_dto() {
		return r16_cpdm_dt_dto;
	}
	public void setR16_cpdm_dt_dto(BigDecimal r16_cpdm_dt_dto) {
		this.r16_cpdm_dt_dto = r16_cpdm_dt_dto;
	}
	public BigDecimal getR16_cp() {
		return r16_cp;
	}
	public void setR16_cp(BigDecimal r16_cp) {
		this.r16_cp = r16_cp;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_open_position() {
		return r17_open_position;
	}
	public void setR17_open_position(BigDecimal r17_open_position) {
		this.r17_open_position = r17_open_position;
	}
	public BigDecimal getR17_cpdm_dt_inc() {
		return r17_cpdm_dt_inc;
	}
	public void setR17_cpdm_dt_inc(BigDecimal r17_cpdm_dt_inc) {
		this.r17_cpdm_dt_inc = r17_cpdm_dt_inc;
	}
	public BigDecimal getR17_cpdm_dt_dec() {
		return r17_cpdm_dt_dec;
	}
	public void setR17_cpdm_dt_dec(BigDecimal r17_cpdm_dt_dec) {
		this.r17_cpdm_dt_dec = r17_cpdm_dt_dec;
	}
	public BigDecimal getR17_net() {
		return r17_net;
	}
	public void setR17_net(BigDecimal r17_net) {
		this.r17_net = r17_net;
	}
	public BigDecimal getR17_cpdm_dt_der() {
		return r17_cpdm_dt_der;
	}
	public void setR17_cpdm_dt_der(BigDecimal r17_cpdm_dt_der) {
		this.r17_cpdm_dt_der = r17_cpdm_dt_der;
	}
	public BigDecimal getR17_cpdm_dt_dto() {
		return r17_cpdm_dt_dto;
	}
	public void setR17_cpdm_dt_dto(BigDecimal r17_cpdm_dt_dto) {
		this.r17_cpdm_dt_dto = r17_cpdm_dt_dto;
	}
	public BigDecimal getR17_cp() {
		return r17_cp;
	}
	public void setR17_cp(BigDecimal r17_cp) {
		this.r17_cp = r17_cp;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_open_position() {
		return r18_open_position;
	}
	public void setR18_open_position(BigDecimal r18_open_position) {
		this.r18_open_position = r18_open_position;
	}
	public BigDecimal getR18_cpdm_dt_inc() {
		return r18_cpdm_dt_inc;
	}
	public void setR18_cpdm_dt_inc(BigDecimal r18_cpdm_dt_inc) {
		this.r18_cpdm_dt_inc = r18_cpdm_dt_inc;
	}
	public BigDecimal getR18_cpdm_dt_dec() {
		return r18_cpdm_dt_dec;
	}
	public void setR18_cpdm_dt_dec(BigDecimal r18_cpdm_dt_dec) {
		this.r18_cpdm_dt_dec = r18_cpdm_dt_dec;
	}
	public BigDecimal getR18_net() {
		return r18_net;
	}
	public void setR18_net(BigDecimal r18_net) {
		this.r18_net = r18_net;
	}
	public BigDecimal getR18_cpdm_dt_der() {
		return r18_cpdm_dt_der;
	}
	public void setR18_cpdm_dt_der(BigDecimal r18_cpdm_dt_der) {
		this.r18_cpdm_dt_der = r18_cpdm_dt_der;
	}
	public BigDecimal getR18_cpdm_dt_dto() {
		return r18_cpdm_dt_dto;
	}
	public void setR18_cpdm_dt_dto(BigDecimal r18_cpdm_dt_dto) {
		this.r18_cpdm_dt_dto = r18_cpdm_dt_dto;
	}
	public BigDecimal getR18_cp() {
		return r18_cp;
	}
	public void setR18_cp(BigDecimal r18_cp) {
		this.r18_cp = r18_cp;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_open_position() {
		return r19_open_position;
	}
	public void setR19_open_position(BigDecimal r19_open_position) {
		this.r19_open_position = r19_open_position;
	}
	public BigDecimal getR19_cpdm_dt_inc() {
		return r19_cpdm_dt_inc;
	}
	public void setR19_cpdm_dt_inc(BigDecimal r19_cpdm_dt_inc) {
		this.r19_cpdm_dt_inc = r19_cpdm_dt_inc;
	}
	public BigDecimal getR19_cpdm_dt_dec() {
		return r19_cpdm_dt_dec;
	}
	public void setR19_cpdm_dt_dec(BigDecimal r19_cpdm_dt_dec) {
		this.r19_cpdm_dt_dec = r19_cpdm_dt_dec;
	}
	public BigDecimal getR19_net() {
		return r19_net;
	}
	public void setR19_net(BigDecimal r19_net) {
		this.r19_net = r19_net;
	}
	public BigDecimal getR19_cpdm_dt_der() {
		return r19_cpdm_dt_der;
	}
	public void setR19_cpdm_dt_der(BigDecimal r19_cpdm_dt_der) {
		this.r19_cpdm_dt_der = r19_cpdm_dt_der;
	}
	public BigDecimal getR19_cpdm_dt_dto() {
		return r19_cpdm_dt_dto;
	}
	public void setR19_cpdm_dt_dto(BigDecimal r19_cpdm_dt_dto) {
		this.r19_cpdm_dt_dto = r19_cpdm_dt_dto;
	}
	public BigDecimal getR19_cp() {
		return r19_cp;
	}
	public void setR19_cp(BigDecimal r19_cp) {
		this.r19_cp = r19_cp;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_open_position() {
		return r20_open_position;
	}
	public void setR20_open_position(BigDecimal r20_open_position) {
		this.r20_open_position = r20_open_position;
	}
	public BigDecimal getR20_cpdm_dt_inc() {
		return r20_cpdm_dt_inc;
	}
	public void setR20_cpdm_dt_inc(BigDecimal r20_cpdm_dt_inc) {
		this.r20_cpdm_dt_inc = r20_cpdm_dt_inc;
	}
	public BigDecimal getR20_cpdm_dt_dec() {
		return r20_cpdm_dt_dec;
	}
	public void setR20_cpdm_dt_dec(BigDecimal r20_cpdm_dt_dec) {
		this.r20_cpdm_dt_dec = r20_cpdm_dt_dec;
	}
	public BigDecimal getR20_net() {
		return r20_net;
	}
	public void setR20_net(BigDecimal r20_net) {
		this.r20_net = r20_net;
	}
	public BigDecimal getR20_cpdm_dt_der() {
		return r20_cpdm_dt_der;
	}
	public void setR20_cpdm_dt_der(BigDecimal r20_cpdm_dt_der) {
		this.r20_cpdm_dt_der = r20_cpdm_dt_der;
	}
	public BigDecimal getR20_cpdm_dt_dto() {
		return r20_cpdm_dt_dto;
	}
	public void setR20_cpdm_dt_dto(BigDecimal r20_cpdm_dt_dto) {
		this.r20_cpdm_dt_dto = r20_cpdm_dt_dto;
	}
	public BigDecimal getR20_cp() {
		return r20_cp;
	}
	public void setR20_cp(BigDecimal r20_cp) {
		this.r20_cp = r20_cp;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_open_position() {
		return r21_open_position;
	}
	public void setR21_open_position(BigDecimal r21_open_position) {
		this.r21_open_position = r21_open_position;
	}
	public BigDecimal getR21_cpdm_dt_inc() {
		return r21_cpdm_dt_inc;
	}
	public void setR21_cpdm_dt_inc(BigDecimal r21_cpdm_dt_inc) {
		this.r21_cpdm_dt_inc = r21_cpdm_dt_inc;
	}
	public BigDecimal getR21_cpdm_dt_dec() {
		return r21_cpdm_dt_dec;
	}
	public void setR21_cpdm_dt_dec(BigDecimal r21_cpdm_dt_dec) {
		this.r21_cpdm_dt_dec = r21_cpdm_dt_dec;
	}
	public BigDecimal getR21_net() {
		return r21_net;
	}
	public void setR21_net(BigDecimal r21_net) {
		this.r21_net = r21_net;
	}
	public BigDecimal getR21_cpdm_dt_der() {
		return r21_cpdm_dt_der;
	}
	public void setR21_cpdm_dt_der(BigDecimal r21_cpdm_dt_der) {
		this.r21_cpdm_dt_der = r21_cpdm_dt_der;
	}
	public BigDecimal getR21_cpdm_dt_dto() {
		return r21_cpdm_dt_dto;
	}
	public void setR21_cpdm_dt_dto(BigDecimal r21_cpdm_dt_dto) {
		this.r21_cpdm_dt_dto = r21_cpdm_dt_dto;
	}
	public BigDecimal getR21_cp() {
		return r21_cp;
	}
	public void setR21_cp(BigDecimal r21_cp) {
		this.r21_cp = r21_cp;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_open_position() {
		return r22_open_position;
	}
	public void setR22_open_position(BigDecimal r22_open_position) {
		this.r22_open_position = r22_open_position;
	}
	public BigDecimal getR22_cpdm_dt_inc() {
		return r22_cpdm_dt_inc;
	}
	public void setR22_cpdm_dt_inc(BigDecimal r22_cpdm_dt_inc) {
		this.r22_cpdm_dt_inc = r22_cpdm_dt_inc;
	}
	public BigDecimal getR22_cpdm_dt_dec() {
		return r22_cpdm_dt_dec;
	}
	public void setR22_cpdm_dt_dec(BigDecimal r22_cpdm_dt_dec) {
		this.r22_cpdm_dt_dec = r22_cpdm_dt_dec;
	}
	public BigDecimal getR22_net() {
		return r22_net;
	}
	public void setR22_net(BigDecimal r22_net) {
		this.r22_net = r22_net;
	}
	public BigDecimal getR22_cpdm_dt_der() {
		return r22_cpdm_dt_der;
	}
	public void setR22_cpdm_dt_der(BigDecimal r22_cpdm_dt_der) {
		this.r22_cpdm_dt_der = r22_cpdm_dt_der;
	}
	public BigDecimal getR22_cpdm_dt_dto() {
		return r22_cpdm_dt_dto;
	}
	public void setR22_cpdm_dt_dto(BigDecimal r22_cpdm_dt_dto) {
		this.r22_cpdm_dt_dto = r22_cpdm_dt_dto;
	}
	public BigDecimal getR22_cp() {
		return r22_cp;
	}
	public void setR22_cp(BigDecimal r22_cp) {
		this.r22_cp = r22_cp;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_open_position() {
		return r23_open_position;
	}
	public void setR23_open_position(BigDecimal r23_open_position) {
		this.r23_open_position = r23_open_position;
	}
	public BigDecimal getR23_cpdm_dt_inc() {
		return r23_cpdm_dt_inc;
	}
	public void setR23_cpdm_dt_inc(BigDecimal r23_cpdm_dt_inc) {
		this.r23_cpdm_dt_inc = r23_cpdm_dt_inc;
	}
	public BigDecimal getR23_cpdm_dt_dec() {
		return r23_cpdm_dt_dec;
	}
	public void setR23_cpdm_dt_dec(BigDecimal r23_cpdm_dt_dec) {
		this.r23_cpdm_dt_dec = r23_cpdm_dt_dec;
	}
	public BigDecimal getR23_net() {
		return r23_net;
	}
	public void setR23_net(BigDecimal r23_net) {
		this.r23_net = r23_net;
	}
	public BigDecimal getR23_cpdm_dt_der() {
		return r23_cpdm_dt_der;
	}
	public void setR23_cpdm_dt_der(BigDecimal r23_cpdm_dt_der) {
		this.r23_cpdm_dt_der = r23_cpdm_dt_der;
	}
	public BigDecimal getR23_cpdm_dt_dto() {
		return r23_cpdm_dt_dto;
	}
	public void setR23_cpdm_dt_dto(BigDecimal r23_cpdm_dt_dto) {
		this.r23_cpdm_dt_dto = r23_cpdm_dt_dto;
	}
	public BigDecimal getR23_cp() {
		return r23_cp;
	}
	public void setR23_cp(BigDecimal r23_cp) {
		this.r23_cp = r23_cp;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_open_position() {
		return r24_open_position;
	}
	public void setR24_open_position(BigDecimal r24_open_position) {
		this.r24_open_position = r24_open_position;
	}
	public BigDecimal getR24_cpdm_dt_inc() {
		return r24_cpdm_dt_inc;
	}
	public void setR24_cpdm_dt_inc(BigDecimal r24_cpdm_dt_inc) {
		this.r24_cpdm_dt_inc = r24_cpdm_dt_inc;
	}
	public BigDecimal getR24_cpdm_dt_dec() {
		return r24_cpdm_dt_dec;
	}
	public void setR24_cpdm_dt_dec(BigDecimal r24_cpdm_dt_dec) {
		this.r24_cpdm_dt_dec = r24_cpdm_dt_dec;
	}
	public BigDecimal getR24_net() {
		return r24_net;
	}
	public void setR24_net(BigDecimal r24_net) {
		this.r24_net = r24_net;
	}
	public BigDecimal getR24_cpdm_dt_der() {
		return r24_cpdm_dt_der;
	}
	public void setR24_cpdm_dt_der(BigDecimal r24_cpdm_dt_der) {
		this.r24_cpdm_dt_der = r24_cpdm_dt_der;
	}
	public BigDecimal getR24_cpdm_dt_dto() {
		return r24_cpdm_dt_dto;
	}
	public void setR24_cpdm_dt_dto(BigDecimal r24_cpdm_dt_dto) {
		this.r24_cpdm_dt_dto = r24_cpdm_dt_dto;
	}
	public BigDecimal getR24_cp() {
		return r24_cp;
	}
	public void setR24_cp(BigDecimal r24_cp) {
		this.r24_cp = r24_cp;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_open_position() {
		return r25_open_position;
	}
	public void setR25_open_position(BigDecimal r25_open_position) {
		this.r25_open_position = r25_open_position;
	}
	public BigDecimal getR25_cpdm_dt_inc() {
		return r25_cpdm_dt_inc;
	}
	public void setR25_cpdm_dt_inc(BigDecimal r25_cpdm_dt_inc) {
		this.r25_cpdm_dt_inc = r25_cpdm_dt_inc;
	}
	public BigDecimal getR25_cpdm_dt_dec() {
		return r25_cpdm_dt_dec;
	}
	public void setR25_cpdm_dt_dec(BigDecimal r25_cpdm_dt_dec) {
		this.r25_cpdm_dt_dec = r25_cpdm_dt_dec;
	}
	public BigDecimal getR25_net() {
		return r25_net;
	}
	public void setR25_net(BigDecimal r25_net) {
		this.r25_net = r25_net;
	}
	public BigDecimal getR25_cpdm_dt_der() {
		return r25_cpdm_dt_der;
	}
	public void setR25_cpdm_dt_der(BigDecimal r25_cpdm_dt_der) {
		this.r25_cpdm_dt_der = r25_cpdm_dt_der;
	}
	public BigDecimal getR25_cpdm_dt_dto() {
		return r25_cpdm_dt_dto;
	}
	public void setR25_cpdm_dt_dto(BigDecimal r25_cpdm_dt_dto) {
		this.r25_cpdm_dt_dto = r25_cpdm_dt_dto;
	}
	public BigDecimal getR25_cp() {
		return r25_cp;
	}
	public void setR25_cp(BigDecimal r25_cp) {
		this.r25_cp = r25_cp;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_open_position() {
		return r26_open_position;
	}
	public void setR26_open_position(BigDecimal r26_open_position) {
		this.r26_open_position = r26_open_position;
	}
	public BigDecimal getR26_cpdm_dt_inc() {
		return r26_cpdm_dt_inc;
	}
	public void setR26_cpdm_dt_inc(BigDecimal r26_cpdm_dt_inc) {
		this.r26_cpdm_dt_inc = r26_cpdm_dt_inc;
	}
	public BigDecimal getR26_cpdm_dt_dec() {
		return r26_cpdm_dt_dec;
	}
	public void setR26_cpdm_dt_dec(BigDecimal r26_cpdm_dt_dec) {
		this.r26_cpdm_dt_dec = r26_cpdm_dt_dec;
	}
	public BigDecimal getR26_net() {
		return r26_net;
	}
	public void setR26_net(BigDecimal r26_net) {
		this.r26_net = r26_net;
	}
	public BigDecimal getR26_cpdm_dt_der() {
		return r26_cpdm_dt_der;
	}
	public void setR26_cpdm_dt_der(BigDecimal r26_cpdm_dt_der) {
		this.r26_cpdm_dt_der = r26_cpdm_dt_der;
	}
	public BigDecimal getR26_cpdm_dt_dto() {
		return r26_cpdm_dt_dto;
	}
	public void setR26_cpdm_dt_dto(BigDecimal r26_cpdm_dt_dto) {
		this.r26_cpdm_dt_dto = r26_cpdm_dt_dto;
	}
	public BigDecimal getR26_cp() {
		return r26_cp;
	}
	public void setR26_cp(BigDecimal r26_cp) {
		this.r26_cp = r26_cp;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_open_position() {
		return r27_open_position;
	}
	public void setR27_open_position(BigDecimal r27_open_position) {
		this.r27_open_position = r27_open_position;
	}
	public BigDecimal getR27_cpdm_dt_inc() {
		return r27_cpdm_dt_inc;
	}
	public void setR27_cpdm_dt_inc(BigDecimal r27_cpdm_dt_inc) {
		this.r27_cpdm_dt_inc = r27_cpdm_dt_inc;
	}
	public BigDecimal getR27_cpdm_dt_dec() {
		return r27_cpdm_dt_dec;
	}
	public void setR27_cpdm_dt_dec(BigDecimal r27_cpdm_dt_dec) {
		this.r27_cpdm_dt_dec = r27_cpdm_dt_dec;
	}
	public BigDecimal getR27_net() {
		return r27_net;
	}
	public void setR27_net(BigDecimal r27_net) {
		this.r27_net = r27_net;
	}
	public BigDecimal getR27_cpdm_dt_der() {
		return r27_cpdm_dt_der;
	}
	public void setR27_cpdm_dt_der(BigDecimal r27_cpdm_dt_der) {
		this.r27_cpdm_dt_der = r27_cpdm_dt_der;
	}
	public BigDecimal getR27_cpdm_dt_dto() {
		return r27_cpdm_dt_dto;
	}
	public void setR27_cpdm_dt_dto(BigDecimal r27_cpdm_dt_dto) {
		this.r27_cpdm_dt_dto = r27_cpdm_dt_dto;
	}
	public BigDecimal getR27_cp() {
		return r27_cp;
	}
	public void setR27_cp(BigDecimal r27_cp) {
		this.r27_cp = r27_cp;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_open_position() {
		return r28_open_position;
	}
	public void setR28_open_position(BigDecimal r28_open_position) {
		this.r28_open_position = r28_open_position;
	}
	public BigDecimal getR28_cpdm_dt_inc() {
		return r28_cpdm_dt_inc;
	}
	public void setR28_cpdm_dt_inc(BigDecimal r28_cpdm_dt_inc) {
		this.r28_cpdm_dt_inc = r28_cpdm_dt_inc;
	}
	public BigDecimal getR28_cpdm_dt_dec() {
		return r28_cpdm_dt_dec;
	}
	public void setR28_cpdm_dt_dec(BigDecimal r28_cpdm_dt_dec) {
		this.r28_cpdm_dt_dec = r28_cpdm_dt_dec;
	}
	public BigDecimal getR28_net() {
		return r28_net;
	}
	public void setR28_net(BigDecimal r28_net) {
		this.r28_net = r28_net;
	}
	public BigDecimal getR28_cpdm_dt_der() {
		return r28_cpdm_dt_der;
	}
	public void setR28_cpdm_dt_der(BigDecimal r28_cpdm_dt_der) {
		this.r28_cpdm_dt_der = r28_cpdm_dt_der;
	}
	public BigDecimal getR28_cpdm_dt_dto() {
		return r28_cpdm_dt_dto;
	}
	public void setR28_cpdm_dt_dto(BigDecimal r28_cpdm_dt_dto) {
		this.r28_cpdm_dt_dto = r28_cpdm_dt_dto;
	}
	public BigDecimal getR28_cp() {
		return r28_cp;
	}
	public void setR28_cp(BigDecimal r28_cp) {
		this.r28_cp = r28_cp;
	}
	public String getR29_product() {
		return r29_product;
	}
	public void setR29_product(String r29_product) {
		this.r29_product = r29_product;
	}
	public BigDecimal getR29_open_position() {
		return r29_open_position;
	}
	public void setR29_open_position(BigDecimal r29_open_position) {
		this.r29_open_position = r29_open_position;
	}
	public BigDecimal getR29_cpdm_dt_inc() {
		return r29_cpdm_dt_inc;
	}
	public void setR29_cpdm_dt_inc(BigDecimal r29_cpdm_dt_inc) {
		this.r29_cpdm_dt_inc = r29_cpdm_dt_inc;
	}
	public BigDecimal getR29_cpdm_dt_dec() {
		return r29_cpdm_dt_dec;
	}
	public void setR29_cpdm_dt_dec(BigDecimal r29_cpdm_dt_dec) {
		this.r29_cpdm_dt_dec = r29_cpdm_dt_dec;
	}
	public BigDecimal getR29_net() {
		return r29_net;
	}
	public void setR29_net(BigDecimal r29_net) {
		this.r29_net = r29_net;
	}
	public BigDecimal getR29_cpdm_dt_der() {
		return r29_cpdm_dt_der;
	}
	public void setR29_cpdm_dt_der(BigDecimal r29_cpdm_dt_der) {
		this.r29_cpdm_dt_der = r29_cpdm_dt_der;
	}
	public BigDecimal getR29_cpdm_dt_dto() {
		return r29_cpdm_dt_dto;
	}
	public void setR29_cpdm_dt_dto(BigDecimal r29_cpdm_dt_dto) {
		this.r29_cpdm_dt_dto = r29_cpdm_dt_dto;
	}
	public BigDecimal getR29_cp() {
		return r29_cp;
	}
	public void setR29_cp(BigDecimal r29_cp) {
		this.r29_cp = r29_cp;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_open_position() {
		return r30_open_position;
	}
	public void setR30_open_position(BigDecimal r30_open_position) {
		this.r30_open_position = r30_open_position;
	}
	public BigDecimal getR30_cpdm_dt_inc() {
		return r30_cpdm_dt_inc;
	}
	public void setR30_cpdm_dt_inc(BigDecimal r30_cpdm_dt_inc) {
		this.r30_cpdm_dt_inc = r30_cpdm_dt_inc;
	}
	public BigDecimal getR30_cpdm_dt_dec() {
		return r30_cpdm_dt_dec;
	}
	public void setR30_cpdm_dt_dec(BigDecimal r30_cpdm_dt_dec) {
		this.r30_cpdm_dt_dec = r30_cpdm_dt_dec;
	}
	public BigDecimal getR30_net() {
		return r30_net;
	}
	public void setR30_net(BigDecimal r30_net) {
		this.r30_net = r30_net;
	}
	public BigDecimal getR30_cpdm_dt_der() {
		return r30_cpdm_dt_der;
	}
	public void setR30_cpdm_dt_der(BigDecimal r30_cpdm_dt_der) {
		this.r30_cpdm_dt_der = r30_cpdm_dt_der;
	}
	public BigDecimal getR30_cpdm_dt_dto() {
		return r30_cpdm_dt_dto;
	}
	public void setR30_cpdm_dt_dto(BigDecimal r30_cpdm_dt_dto) {
		this.r30_cpdm_dt_dto = r30_cpdm_dt_dto;
	}
	public BigDecimal getR30_cp() {
		return r30_cp;
	}
	public void setR30_cp(BigDecimal r30_cp) {
		this.r30_cp = r30_cp;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_open_position() {
		return r31_open_position;
	}
	public void setR31_open_position(BigDecimal r31_open_position) {
		this.r31_open_position = r31_open_position;
	}
	public BigDecimal getR31_cpdm_dt_inc() {
		return r31_cpdm_dt_inc;
	}
	public void setR31_cpdm_dt_inc(BigDecimal r31_cpdm_dt_inc) {
		this.r31_cpdm_dt_inc = r31_cpdm_dt_inc;
	}
	public BigDecimal getR31_cpdm_dt_dec() {
		return r31_cpdm_dt_dec;
	}
	public void setR31_cpdm_dt_dec(BigDecimal r31_cpdm_dt_dec) {
		this.r31_cpdm_dt_dec = r31_cpdm_dt_dec;
	}
	public BigDecimal getR31_net() {
		return r31_net;
	}
	public void setR31_net(BigDecimal r31_net) {
		this.r31_net = r31_net;
	}
	public BigDecimal getR31_cpdm_dt_der() {
		return r31_cpdm_dt_der;
	}
	public void setR31_cpdm_dt_der(BigDecimal r31_cpdm_dt_der) {
		this.r31_cpdm_dt_der = r31_cpdm_dt_der;
	}
	public BigDecimal getR31_cpdm_dt_dto() {
		return r31_cpdm_dt_dto;
	}
	public void setR31_cpdm_dt_dto(BigDecimal r31_cpdm_dt_dto) {
		this.r31_cpdm_dt_dto = r31_cpdm_dt_dto;
	}
	public BigDecimal getR31_cp() {
		return r31_cp;
	}
	public void setR31_cp(BigDecimal r31_cp) {
		this.r31_cp = r31_cp;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_open_position() {
		return r32_open_position;
	}
	public void setR32_open_position(BigDecimal r32_open_position) {
		this.r32_open_position = r32_open_position;
	}
	public BigDecimal getR32_cpdm_dt_inc() {
		return r32_cpdm_dt_inc;
	}
	public void setR32_cpdm_dt_inc(BigDecimal r32_cpdm_dt_inc) {
		this.r32_cpdm_dt_inc = r32_cpdm_dt_inc;
	}
	public BigDecimal getR32_cpdm_dt_dec() {
		return r32_cpdm_dt_dec;
	}
	public void setR32_cpdm_dt_dec(BigDecimal r32_cpdm_dt_dec) {
		this.r32_cpdm_dt_dec = r32_cpdm_dt_dec;
	}
	public BigDecimal getR32_net() {
		return r32_net;
	}
	public void setR32_net(BigDecimal r32_net) {
		this.r32_net = r32_net;
	}
	public BigDecimal getR32_cpdm_dt_der() {
		return r32_cpdm_dt_der;
	}
	public void setR32_cpdm_dt_der(BigDecimal r32_cpdm_dt_der) {
		this.r32_cpdm_dt_der = r32_cpdm_dt_der;
	}
	public BigDecimal getR32_cpdm_dt_dto() {
		return r32_cpdm_dt_dto;
	}
	public void setR32_cpdm_dt_dto(BigDecimal r32_cpdm_dt_dto) {
		this.r32_cpdm_dt_dto = r32_cpdm_dt_dto;
	}
	public BigDecimal getR32_cp() {
		return r32_cp;
	}
	public void setR32_cp(BigDecimal r32_cp) {
		this.r32_cp = r32_cp;
	}
	public String getR33_product() {
		return r33_product;
	}
	public void setR33_product(String r33_product) {
		this.r33_product = r33_product;
	}
	public BigDecimal getR33_open_position() {
		return r33_open_position;
	}
	public void setR33_open_position(BigDecimal r33_open_position) {
		this.r33_open_position = r33_open_position;
	}
	public BigDecimal getR33_cpdm_dt_inc() {
		return r33_cpdm_dt_inc;
	}
	public void setR33_cpdm_dt_inc(BigDecimal r33_cpdm_dt_inc) {
		this.r33_cpdm_dt_inc = r33_cpdm_dt_inc;
	}
	public BigDecimal getR33_cpdm_dt_dec() {
		return r33_cpdm_dt_dec;
	}
	public void setR33_cpdm_dt_dec(BigDecimal r33_cpdm_dt_dec) {
		this.r33_cpdm_dt_dec = r33_cpdm_dt_dec;
	}
	public BigDecimal getR33_net() {
		return r33_net;
	}
	public void setR33_net(BigDecimal r33_net) {
		this.r33_net = r33_net;
	}
	public BigDecimal getR33_cpdm_dt_der() {
		return r33_cpdm_dt_der;
	}
	public void setR33_cpdm_dt_der(BigDecimal r33_cpdm_dt_der) {
		this.r33_cpdm_dt_der = r33_cpdm_dt_der;
	}
	public BigDecimal getR33_cpdm_dt_dto() {
		return r33_cpdm_dt_dto;
	}
	public void setR33_cpdm_dt_dto(BigDecimal r33_cpdm_dt_dto) {
		this.r33_cpdm_dt_dto = r33_cpdm_dt_dto;
	}
	public BigDecimal getR33_cp() {
		return r33_cp;
	}
	public void setR33_cp(BigDecimal r33_cp) {
		this.r33_cp = r33_cp;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_open_position() {
		return r34_open_position;
	}
	public void setR34_open_position(BigDecimal r34_open_position) {
		this.r34_open_position = r34_open_position;
	}
	public BigDecimal getR34_cpdm_dt_inc() {
		return r34_cpdm_dt_inc;
	}
	public void setR34_cpdm_dt_inc(BigDecimal r34_cpdm_dt_inc) {
		this.r34_cpdm_dt_inc = r34_cpdm_dt_inc;
	}
	public BigDecimal getR34_cpdm_dt_dec() {
		return r34_cpdm_dt_dec;
	}
	public void setR34_cpdm_dt_dec(BigDecimal r34_cpdm_dt_dec) {
		this.r34_cpdm_dt_dec = r34_cpdm_dt_dec;
	}
	public BigDecimal getR34_net() {
		return r34_net;
	}
	public void setR34_net(BigDecimal r34_net) {
		this.r34_net = r34_net;
	}
	public BigDecimal getR34_cpdm_dt_der() {
		return r34_cpdm_dt_der;
	}
	public void setR34_cpdm_dt_der(BigDecimal r34_cpdm_dt_der) {
		this.r34_cpdm_dt_der = r34_cpdm_dt_der;
	}
	public BigDecimal getR34_cpdm_dt_dto() {
		return r34_cpdm_dt_dto;
	}
	public void setR34_cpdm_dt_dto(BigDecimal r34_cpdm_dt_dto) {
		this.r34_cpdm_dt_dto = r34_cpdm_dt_dto;
	}
	public BigDecimal getR34_cp() {
		return r34_cp;
	}
	public void setR34_cp(BigDecimal r34_cp) {
		this.r34_cp = r34_cp;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_open_position() {
		return r35_open_position;
	}
	public void setR35_open_position(BigDecimal r35_open_position) {
		this.r35_open_position = r35_open_position;
	}
	public BigDecimal getR35_cpdm_dt_inc() {
		return r35_cpdm_dt_inc;
	}
	public void setR35_cpdm_dt_inc(BigDecimal r35_cpdm_dt_inc) {
		this.r35_cpdm_dt_inc = r35_cpdm_dt_inc;
	}
	public BigDecimal getR35_cpdm_dt_dec() {
		return r35_cpdm_dt_dec;
	}
	public void setR35_cpdm_dt_dec(BigDecimal r35_cpdm_dt_dec) {
		this.r35_cpdm_dt_dec = r35_cpdm_dt_dec;
	}
	public BigDecimal getR35_net() {
		return r35_net;
	}
	public void setR35_net(BigDecimal r35_net) {
		this.r35_net = r35_net;
	}
	public BigDecimal getR35_cpdm_dt_der() {
		return r35_cpdm_dt_der;
	}
	public void setR35_cpdm_dt_der(BigDecimal r35_cpdm_dt_der) {
		this.r35_cpdm_dt_der = r35_cpdm_dt_der;
	}
	public BigDecimal getR35_cpdm_dt_dto() {
		return r35_cpdm_dt_dto;
	}
	public void setR35_cpdm_dt_dto(BigDecimal r35_cpdm_dt_dto) {
		this.r35_cpdm_dt_dto = r35_cpdm_dt_dto;
	}
	public BigDecimal getR35_cp() {
		return r35_cp;
	}
	public void setR35_cp(BigDecimal r35_cp) {
		this.r35_cp = r35_cp;
	}
	public String getR36_product() {
		return r36_product;
	}
	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}
	public BigDecimal getR36_open_position() {
		return r36_open_position;
	}
	public void setR36_open_position(BigDecimal r36_open_position) {
		this.r36_open_position = r36_open_position;
	}
	public BigDecimal getR36_cpdm_dt_inc() {
		return r36_cpdm_dt_inc;
	}
	public void setR36_cpdm_dt_inc(BigDecimal r36_cpdm_dt_inc) {
		this.r36_cpdm_dt_inc = r36_cpdm_dt_inc;
	}
	public BigDecimal getR36_cpdm_dt_dec() {
		return r36_cpdm_dt_dec;
	}
	public void setR36_cpdm_dt_dec(BigDecimal r36_cpdm_dt_dec) {
		this.r36_cpdm_dt_dec = r36_cpdm_dt_dec;
	}
	public BigDecimal getR36_net() {
		return r36_net;
	}
	public void setR36_net(BigDecimal r36_net) {
		this.r36_net = r36_net;
	}
	public BigDecimal getR36_cpdm_dt_der() {
		return r36_cpdm_dt_der;
	}
	public void setR36_cpdm_dt_der(BigDecimal r36_cpdm_dt_der) {
		this.r36_cpdm_dt_der = r36_cpdm_dt_der;
	}
	public BigDecimal getR36_cpdm_dt_dto() {
		return r36_cpdm_dt_dto;
	}
	public void setR36_cpdm_dt_dto(BigDecimal r36_cpdm_dt_dto) {
		this.r36_cpdm_dt_dto = r36_cpdm_dt_dto;
	}
	public BigDecimal getR36_cp() {
		return r36_cp;
	}
	public void setR36_cp(BigDecimal r36_cp) {
		this.r36_cp = r36_cp;
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
// RESUB DETAIL M_BOP
//=====================================================

public class M_BOP_RESUB_Detail_RowMapper 
        implements RowMapper<M_BOP_RESUB_Detail_Entity> {

    @Override
    public M_BOP_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_BOP_RESUB_Detail_Entity obj = new M_BOP_RESUB_Detail_Entity();

// =========================
// R13
// =========================
obj.setR13_product(rs.getString("r13_product"));
obj.setR13_open_position(rs.getBigDecimal("r13_open_position"));
obj.setR13_cpdm_dt_inc(rs.getBigDecimal("r13_cpdm_dt_inc"));
obj.setR13_cpdm_dt_dec(rs.getBigDecimal("r13_cpdm_dt_dec"));
obj.setR13_net(rs.getBigDecimal("r13_net"));
obj.setR13_cpdm_dt_der(rs.getBigDecimal("r13_cpdm_dt_der"));
obj.setR13_cpdm_dt_dto(rs.getBigDecimal("r13_cpdm_dt_dto"));
obj.setR13_cp(rs.getBigDecimal("r13_cp"));


// =========================
// R14
// =========================
obj.setR14_product(rs.getString("r14_product"));
obj.setR14_open_position(rs.getBigDecimal("r14_open_position"));
obj.setR14_cpdm_dt_inc(rs.getBigDecimal("r14_cpdm_dt_inc"));
obj.setR14_cpdm_dt_dec(rs.getBigDecimal("r14_cpdm_dt_dec"));
obj.setR14_net(rs.getBigDecimal("r14_net"));
obj.setR14_cpdm_dt_der(rs.getBigDecimal("r14_cpdm_dt_der"));
obj.setR14_cpdm_dt_dto(rs.getBigDecimal("r14_cpdm_dt_dto"));
obj.setR14_cp(rs.getBigDecimal("r14_cp"));

// =========================
// R15
// =========================
obj.setR15_product(rs.getString("r15_product"));
obj.setR15_open_position(rs.getBigDecimal("r15_open_position"));
obj.setR15_cpdm_dt_inc(rs.getBigDecimal("r15_cpdm_dt_inc"));
obj.setR15_cpdm_dt_dec(rs.getBigDecimal("r15_cpdm_dt_dec"));
obj.setR15_net(rs.getBigDecimal("r15_net"));
obj.setR15_cpdm_dt_der(rs.getBigDecimal("r15_cpdm_dt_der"));
obj.setR15_cpdm_dt_dto(rs.getBigDecimal("r15_cpdm_dt_dto"));
obj.setR15_cp(rs.getBigDecimal("r15_cp"));

// =========================
// R16
// =========================
obj.setR16_product(rs.getString("r16_product"));
obj.setR16_open_position(rs.getBigDecimal("r16_open_position"));
obj.setR16_cpdm_dt_inc(rs.getBigDecimal("r16_cpdm_dt_inc"));
obj.setR16_cpdm_dt_dec(rs.getBigDecimal("r16_cpdm_dt_dec"));
obj.setR16_net(rs.getBigDecimal("r16_net"));
obj.setR16_cpdm_dt_der(rs.getBigDecimal("r16_cpdm_dt_der"));
obj.setR16_cpdm_dt_dto(rs.getBigDecimal("r16_cpdm_dt_dto"));
obj.setR16_cp(rs.getBigDecimal("r16_cp"));

// =========================
// R17
// =========================
obj.setR17_product(rs.getString("r17_product"));
obj.setR17_open_position(rs.getBigDecimal("r17_open_position"));
obj.setR17_cpdm_dt_inc(rs.getBigDecimal("r17_cpdm_dt_inc"));
obj.setR17_cpdm_dt_dec(rs.getBigDecimal("r17_cpdm_dt_dec"));
obj.setR17_net(rs.getBigDecimal("r17_net"));
obj.setR17_cpdm_dt_der(rs.getBigDecimal("r17_cpdm_dt_der"));
obj.setR17_cpdm_dt_dto(rs.getBigDecimal("r17_cpdm_dt_dto"));
obj.setR17_cp(rs.getBigDecimal("r17_cp"));

// =========================
// R18
// =========================
obj.setR18_product(rs.getString("r18_product"));
obj.setR18_open_position(rs.getBigDecimal("r18_open_position"));
obj.setR18_cpdm_dt_inc(rs.getBigDecimal("r18_cpdm_dt_inc"));
obj.setR18_cpdm_dt_dec(rs.getBigDecimal("r18_cpdm_dt_dec"));
obj.setR18_net(rs.getBigDecimal("r18_net"));
obj.setR18_cpdm_dt_der(rs.getBigDecimal("r18_cpdm_dt_der"));
obj.setR18_cpdm_dt_dto(rs.getBigDecimal("r18_cpdm_dt_dto"));
obj.setR18_cp(rs.getBigDecimal("r18_cp"));

// =========================
// R19
// =========================
obj.setR19_product(rs.getString("r19_product"));
obj.setR19_open_position(rs.getBigDecimal("r19_open_position"));
obj.setR19_cpdm_dt_inc(rs.getBigDecimal("r19_cpdm_dt_inc"));
obj.setR19_cpdm_dt_dec(rs.getBigDecimal("r19_cpdm_dt_dec"));
obj.setR19_net(rs.getBigDecimal("r19_net"));
obj.setR19_cpdm_dt_der(rs.getBigDecimal("r19_cpdm_dt_der"));
obj.setR19_cpdm_dt_dto(rs.getBigDecimal("r19_cpdm_dt_dto"));
obj.setR19_cp(rs.getBigDecimal("r19_cp"));

// =========================
// R20
// =========================
obj.setR20_product(rs.getString("r20_product"));
obj.setR20_open_position(rs.getBigDecimal("r20_open_position"));
obj.setR20_cpdm_dt_inc(rs.getBigDecimal("r20_cpdm_dt_inc"));
obj.setR20_cpdm_dt_dec(rs.getBigDecimal("r20_cpdm_dt_dec"));
obj.setR20_net(rs.getBigDecimal("r20_net"));
obj.setR20_cpdm_dt_der(rs.getBigDecimal("r20_cpdm_dt_der"));
obj.setR20_cpdm_dt_dto(rs.getBigDecimal("r20_cpdm_dt_dto"));
obj.setR20_cp(rs.getBigDecimal("r20_cp"));


// =========================
// R21
// =========================
obj.setR21_product(rs.getString("r21_product"));
obj.setR21_open_position(rs.getBigDecimal("r21_open_position"));
obj.setR21_cpdm_dt_inc(rs.getBigDecimal("r21_cpdm_dt_inc"));
obj.setR21_cpdm_dt_dec(rs.getBigDecimal("r21_cpdm_dt_dec"));
obj.setR21_net(rs.getBigDecimal("r21_net"));
obj.setR21_cpdm_dt_der(rs.getBigDecimal("r21_cpdm_dt_der"));
obj.setR21_cpdm_dt_dto(rs.getBigDecimal("r21_cpdm_dt_dto"));
obj.setR21_cp(rs.getBigDecimal("r21_cp"));

// =========================
// R22
// =========================
obj.setR22_product(rs.getString("r22_product"));
obj.setR22_open_position(rs.getBigDecimal("r22_open_position"));
obj.setR22_cpdm_dt_inc(rs.getBigDecimal("r22_cpdm_dt_inc"));
obj.setR22_cpdm_dt_dec(rs.getBigDecimal("r22_cpdm_dt_dec"));
obj.setR22_net(rs.getBigDecimal("r22_net"));
obj.setR22_cpdm_dt_der(rs.getBigDecimal("r22_cpdm_dt_der"));
obj.setR22_cpdm_dt_dto(rs.getBigDecimal("r22_cpdm_dt_dto"));
obj.setR22_cp(rs.getBigDecimal("r22_cp"));

// =========================
// R23
// =========================
obj.setR23_product(rs.getString("r23_product"));
obj.setR23_open_position(rs.getBigDecimal("r23_open_position"));
obj.setR23_cpdm_dt_inc(rs.getBigDecimal("r23_cpdm_dt_inc"));
obj.setR23_cpdm_dt_dec(rs.getBigDecimal("r23_cpdm_dt_dec"));
obj.setR23_net(rs.getBigDecimal("r23_net"));
obj.setR23_cpdm_dt_der(rs.getBigDecimal("r23_cpdm_dt_der"));
obj.setR23_cpdm_dt_dto(rs.getBigDecimal("r23_cpdm_dt_dto"));
obj.setR23_cp(rs.getBigDecimal("r23_cp"));

// =========================
// R24
// =========================
obj.setR24_product(rs.getString("r24_product"));
obj.setR24_open_position(rs.getBigDecimal("r24_open_position"));
obj.setR24_cpdm_dt_inc(rs.getBigDecimal("r24_cpdm_dt_inc"));
obj.setR24_cpdm_dt_dec(rs.getBigDecimal("r24_cpdm_dt_dec"));
obj.setR24_net(rs.getBigDecimal("r24_net"));
obj.setR24_cpdm_dt_der(rs.getBigDecimal("r24_cpdm_dt_der"));
obj.setR24_cpdm_dt_dto(rs.getBigDecimal("r24_cpdm_dt_dto"));
obj.setR24_cp(rs.getBigDecimal("r24_cp"));

// =========================
// R25
// =========================
obj.setR25_product(rs.getString("r25_product"));
obj.setR25_open_position(rs.getBigDecimal("r25_open_position"));
obj.setR25_cpdm_dt_inc(rs.getBigDecimal("r25_cpdm_dt_inc"));
obj.setR25_cpdm_dt_dec(rs.getBigDecimal("r25_cpdm_dt_dec"));
obj.setR25_net(rs.getBigDecimal("r25_net"));
obj.setR25_cpdm_dt_der(rs.getBigDecimal("r25_cpdm_dt_der"));
obj.setR25_cpdm_dt_dto(rs.getBigDecimal("r25_cpdm_dt_dto"));
obj.setR25_cp(rs.getBigDecimal("r25_cp"));

// =========================
// R26
// =========================
obj.setR26_product(rs.getString("r26_product"));
obj.setR26_open_position(rs.getBigDecimal("r26_open_position"));
obj.setR26_cpdm_dt_inc(rs.getBigDecimal("r26_cpdm_dt_inc"));
obj.setR26_cpdm_dt_dec(rs.getBigDecimal("r26_cpdm_dt_dec"));
obj.setR26_net(rs.getBigDecimal("r26_net"));
obj.setR26_cpdm_dt_der(rs.getBigDecimal("r26_cpdm_dt_der"));
obj.setR26_cpdm_dt_dto(rs.getBigDecimal("r26_cpdm_dt_dto"));
obj.setR26_cp(rs.getBigDecimal("r26_cp"));

// =========================
// R27
// =========================
obj.setR27_product(rs.getString("r27_product"));
obj.setR27_open_position(rs.getBigDecimal("r27_open_position"));
obj.setR27_cpdm_dt_inc(rs.getBigDecimal("r27_cpdm_dt_inc"));
obj.setR27_cpdm_dt_dec(rs.getBigDecimal("r27_cpdm_dt_dec"));
obj.setR27_net(rs.getBigDecimal("r27_net"));
obj.setR27_cpdm_dt_der(rs.getBigDecimal("r27_cpdm_dt_der"));
obj.setR27_cpdm_dt_dto(rs.getBigDecimal("r27_cpdm_dt_dto"));
obj.setR27_cp(rs.getBigDecimal("r27_cp"));

// =========================
// R28
// =========================
obj.setR28_product(rs.getString("r28_product"));
obj.setR28_open_position(rs.getBigDecimal("r28_open_position"));
obj.setR28_cpdm_dt_inc(rs.getBigDecimal("r28_cpdm_dt_inc"));
obj.setR28_cpdm_dt_dec(rs.getBigDecimal("r28_cpdm_dt_dec"));
obj.setR28_net(rs.getBigDecimal("r28_net"));
obj.setR28_cpdm_dt_der(rs.getBigDecimal("r28_cpdm_dt_der"));
obj.setR28_cpdm_dt_dto(rs.getBigDecimal("r28_cpdm_dt_dto"));
obj.setR28_cp(rs.getBigDecimal("r28_cp"));

// =========================
// R29
// =========================
obj.setR29_product(rs.getString("r29_product"));
obj.setR29_open_position(rs.getBigDecimal("r29_open_position"));
obj.setR29_cpdm_dt_inc(rs.getBigDecimal("r29_cpdm_dt_inc"));
obj.setR29_cpdm_dt_dec(rs.getBigDecimal("r29_cpdm_dt_dec"));
obj.setR29_net(rs.getBigDecimal("r29_net"));
obj.setR29_cpdm_dt_der(rs.getBigDecimal("r29_cpdm_dt_der"));
obj.setR29_cpdm_dt_dto(rs.getBigDecimal("r29_cpdm_dt_dto"));
obj.setR29_cp(rs.getBigDecimal("r29_cp"));

// =========================
// R30
// =========================
obj.setR30_product(rs.getString("r30_product"));
obj.setR30_open_position(rs.getBigDecimal("r30_open_position"));
obj.setR30_cpdm_dt_inc(rs.getBigDecimal("r30_cpdm_dt_inc"));
obj.setR30_cpdm_dt_dec(rs.getBigDecimal("r30_cpdm_dt_dec"));
obj.setR30_net(rs.getBigDecimal("r30_net"));
obj.setR30_cpdm_dt_der(rs.getBigDecimal("r30_cpdm_dt_der"));
obj.setR30_cpdm_dt_dto(rs.getBigDecimal("r30_cpdm_dt_dto"));
obj.setR30_cp(rs.getBigDecimal("r30_cp"));

// =========================
// R31
// =========================
obj.setR31_product(rs.getString("r31_product"));
obj.setR31_open_position(rs.getBigDecimal("r31_open_position"));
obj.setR31_cpdm_dt_inc(rs.getBigDecimal("r31_cpdm_dt_inc"));
obj.setR31_cpdm_dt_dec(rs.getBigDecimal("r31_cpdm_dt_dec"));
obj.setR31_net(rs.getBigDecimal("r31_net"));
obj.setR31_cpdm_dt_der(rs.getBigDecimal("r31_cpdm_dt_der"));
obj.setR31_cpdm_dt_dto(rs.getBigDecimal("r31_cpdm_dt_dto"));
obj.setR31_cp(rs.getBigDecimal("r31_cp"));

// =========================
// R32
// =========================
obj.setR32_product(rs.getString("r32_product"));
obj.setR32_open_position(rs.getBigDecimal("r32_open_position"));
obj.setR32_cpdm_dt_inc(rs.getBigDecimal("r32_cpdm_dt_inc"));
obj.setR32_cpdm_dt_dec(rs.getBigDecimal("r32_cpdm_dt_dec"));
obj.setR32_net(rs.getBigDecimal("r32_net"));
obj.setR32_cpdm_dt_der(rs.getBigDecimal("r32_cpdm_dt_der"));
obj.setR32_cpdm_dt_dto(rs.getBigDecimal("r32_cpdm_dt_dto"));
obj.setR32_cp(rs.getBigDecimal("r32_cp"));

// =========================
// R33
// =========================
obj.setR33_product(rs.getString("r33_product"));
obj.setR33_open_position(rs.getBigDecimal("r33_open_position"));
obj.setR33_cpdm_dt_inc(rs.getBigDecimal("r33_cpdm_dt_inc"));
obj.setR33_cpdm_dt_dec(rs.getBigDecimal("r33_cpdm_dt_dec"));
obj.setR33_net(rs.getBigDecimal("r33_net"));
obj.setR33_cpdm_dt_der(rs.getBigDecimal("r33_cpdm_dt_der"));
obj.setR33_cpdm_dt_dto(rs.getBigDecimal("r33_cpdm_dt_dto"));
obj.setR33_cp(rs.getBigDecimal("r33_cp"));

// =========================
// R34
// =========================
obj.setR34_product(rs.getString("r34_product"));
obj.setR34_open_position(rs.getBigDecimal("r34_open_position"));
obj.setR34_cpdm_dt_inc(rs.getBigDecimal("r34_cpdm_dt_inc"));
obj.setR34_cpdm_dt_dec(rs.getBigDecimal("r34_cpdm_dt_dec"));
obj.setR34_net(rs.getBigDecimal("r34_net"));
obj.setR34_cpdm_dt_der(rs.getBigDecimal("r34_cpdm_dt_der"));
obj.setR34_cpdm_dt_dto(rs.getBigDecimal("r34_cpdm_dt_dto"));
obj.setR34_cp(rs.getBigDecimal("r34_cp"));

// =========================
// R35
// =========================
obj.setR35_product(rs.getString("r35_product"));
obj.setR35_open_position(rs.getBigDecimal("r35_open_position"));
obj.setR35_cpdm_dt_inc(rs.getBigDecimal("r35_cpdm_dt_inc"));
obj.setR35_cpdm_dt_dec(rs.getBigDecimal("r35_cpdm_dt_dec"));
obj.setR35_net(rs.getBigDecimal("r35_net"));
obj.setR35_cpdm_dt_der(rs.getBigDecimal("r35_cpdm_dt_der"));
obj.setR35_cpdm_dt_dto(rs.getBigDecimal("r35_cpdm_dt_dto"));
obj.setR35_cp(rs.getBigDecimal("r35_cp"));

// =========================
// R36
// =========================
obj.setR36_product(rs.getString("r36_product"));
obj.setR36_open_position(rs.getBigDecimal("r36_open_position"));
obj.setR36_cpdm_dt_inc(rs.getBigDecimal("r36_cpdm_dt_inc"));
obj.setR36_cpdm_dt_dec(rs.getBigDecimal("r36_cpdm_dt_dec"));
obj.setR36_net(rs.getBigDecimal("r36_net"));
obj.setR36_cpdm_dt_der(rs.getBigDecimal("r36_cpdm_dt_der"));
obj.setR36_cpdm_dt_dto(rs.getBigDecimal("r36_cpdm_dt_dto"));
obj.setR36_cp(rs.getBigDecimal("r36_cp"));


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

public class M_BOP_RESUB_Detail_Entity {

   
	private String	r13_product;
	private BigDecimal	r13_open_position;
	private BigDecimal	r13_cpdm_dt_inc;
	private BigDecimal	r13_cpdm_dt_dec;
	private BigDecimal	r13_net;
	private BigDecimal	r13_cpdm_dt_der;
	private BigDecimal	r13_cpdm_dt_dto;
	private BigDecimal	r13_cp;
	private String	r14_product;
	private BigDecimal	r14_open_position;
	private BigDecimal	r14_cpdm_dt_inc;
	private BigDecimal	r14_cpdm_dt_dec;
	private BigDecimal	r14_net;
	private BigDecimal	r14_cpdm_dt_der;
	private BigDecimal	r14_cpdm_dt_dto;
	private BigDecimal	r14_cp;
	private String	r15_product;
	private BigDecimal	r15_open_position;
	private BigDecimal	r15_cpdm_dt_inc;
	private BigDecimal	r15_cpdm_dt_dec;
	private BigDecimal	r15_net;
	private BigDecimal	r15_cpdm_dt_der;
	private BigDecimal	r15_cpdm_dt_dto;
	private BigDecimal	r15_cp;
	private String	r16_product;
	private BigDecimal	r16_open_position;
	private BigDecimal	r16_cpdm_dt_inc;
	private BigDecimal	r16_cpdm_dt_dec;
	private BigDecimal	r16_net;
	private BigDecimal	r16_cpdm_dt_der;
	private BigDecimal	r16_cpdm_dt_dto;
	private BigDecimal	r16_cp;
	private String	r17_product;
	private BigDecimal	r17_open_position;
	private BigDecimal	r17_cpdm_dt_inc;
	private BigDecimal	r17_cpdm_dt_dec;
	private BigDecimal	r17_net;
	private BigDecimal	r17_cpdm_dt_der;
	private BigDecimal	r17_cpdm_dt_dto;
	private BigDecimal	r17_cp;
	private String	r18_product;
	private BigDecimal	r18_open_position;
	private BigDecimal	r18_cpdm_dt_inc;
	private BigDecimal	r18_cpdm_dt_dec;
	private BigDecimal	r18_net;
	private BigDecimal	r18_cpdm_dt_der;
	private BigDecimal	r18_cpdm_dt_dto;
	private BigDecimal	r18_cp;
	private String	r19_product;
	private BigDecimal	r19_open_position;
	private BigDecimal	r19_cpdm_dt_inc;
	private BigDecimal	r19_cpdm_dt_dec;
	private BigDecimal	r19_net;
	private BigDecimal	r19_cpdm_dt_der;
	private BigDecimal	r19_cpdm_dt_dto;
	private BigDecimal	r19_cp;
	private String	r20_product;
	private BigDecimal	r20_open_position;
	private BigDecimal	r20_cpdm_dt_inc;
	private BigDecimal	r20_cpdm_dt_dec;
	private BigDecimal	r20_net;
	private BigDecimal	r20_cpdm_dt_der;
	private BigDecimal	r20_cpdm_dt_dto;
	private BigDecimal	r20_cp;
	private String	r21_product;
	private BigDecimal	r21_open_position;
	private BigDecimal	r21_cpdm_dt_inc;
	private BigDecimal	r21_cpdm_dt_dec;
	private BigDecimal	r21_net;
	private BigDecimal	r21_cpdm_dt_der;
	private BigDecimal	r21_cpdm_dt_dto;
	private BigDecimal	r21_cp;
	private String	r22_product;
	private BigDecimal	r22_open_position;
	private BigDecimal	r22_cpdm_dt_inc;
	private BigDecimal	r22_cpdm_dt_dec;
	private BigDecimal	r22_net;
	private BigDecimal	r22_cpdm_dt_der;
	private BigDecimal	r22_cpdm_dt_dto;
	private BigDecimal	r22_cp;
	private String	r23_product;
	private BigDecimal	r23_open_position;
	private BigDecimal	r23_cpdm_dt_inc;
	private BigDecimal	r23_cpdm_dt_dec;
	private BigDecimal	r23_net;
	private BigDecimal	r23_cpdm_dt_der;
	private BigDecimal	r23_cpdm_dt_dto;
	private BigDecimal	r23_cp;
	private String	r24_product;
	private BigDecimal	r24_open_position;
	private BigDecimal	r24_cpdm_dt_inc;
	private BigDecimal	r24_cpdm_dt_dec;
	private BigDecimal	r24_net;
	private BigDecimal	r24_cpdm_dt_der;
	private BigDecimal	r24_cpdm_dt_dto;
	private BigDecimal	r24_cp;
	private String	r25_product;
	private BigDecimal	r25_open_position;
	private BigDecimal	r25_cpdm_dt_inc;
	private BigDecimal	r25_cpdm_dt_dec;
	private BigDecimal	r25_net;
	private BigDecimal	r25_cpdm_dt_der;
	private BigDecimal	r25_cpdm_dt_dto;
	private BigDecimal	r25_cp;
	private String	r26_product;
	private BigDecimal	r26_open_position;
	private BigDecimal	r26_cpdm_dt_inc;
	private BigDecimal	r26_cpdm_dt_dec;
	private BigDecimal	r26_net;
	private BigDecimal	r26_cpdm_dt_der;
	private BigDecimal	r26_cpdm_dt_dto;
	private BigDecimal	r26_cp;
	private String	r27_product;
	private BigDecimal	r27_open_position;
	private BigDecimal	r27_cpdm_dt_inc;
	private BigDecimal	r27_cpdm_dt_dec;
	private BigDecimal	r27_net;
	private BigDecimal	r27_cpdm_dt_der;
	private BigDecimal	r27_cpdm_dt_dto;
	private BigDecimal	r27_cp;
	private String	r28_product;
	private BigDecimal	r28_open_position;
	private BigDecimal	r28_cpdm_dt_inc;
	private BigDecimal	r28_cpdm_dt_dec;
	private BigDecimal	r28_net;
	private BigDecimal	r28_cpdm_dt_der;
	private BigDecimal	r28_cpdm_dt_dto;
	private BigDecimal	r28_cp;
	private String	r29_product;
	private BigDecimal	r29_open_position;
	private BigDecimal	r29_cpdm_dt_inc;
	private BigDecimal	r29_cpdm_dt_dec;
	private BigDecimal	r29_net;
	private BigDecimal	r29_cpdm_dt_der;
	private BigDecimal	r29_cpdm_dt_dto;
	private BigDecimal	r29_cp;
	private String	r30_product;
	private BigDecimal	r30_open_position;
	private BigDecimal	r30_cpdm_dt_inc;
	private BigDecimal	r30_cpdm_dt_dec;
	private BigDecimal	r30_net;
	private BigDecimal	r30_cpdm_dt_der;
	private BigDecimal	r30_cpdm_dt_dto;
	private BigDecimal	r30_cp;
	private String	r31_product;
	private BigDecimal	r31_open_position;
	private BigDecimal	r31_cpdm_dt_inc;
	private BigDecimal	r31_cpdm_dt_dec;
	private BigDecimal	r31_net;
	private BigDecimal	r31_cpdm_dt_der;
	private BigDecimal	r31_cpdm_dt_dto;
	private BigDecimal	r31_cp;
	private String	r32_product;
	private BigDecimal	r32_open_position;
	private BigDecimal	r32_cpdm_dt_inc;
	private BigDecimal	r32_cpdm_dt_dec;
	private BigDecimal	r32_net;
	private BigDecimal	r32_cpdm_dt_der;
	private BigDecimal	r32_cpdm_dt_dto;
	private BigDecimal	r32_cp;
	private String	r33_product;
	private BigDecimal	r33_open_position;
	private BigDecimal	r33_cpdm_dt_inc;
	private BigDecimal	r33_cpdm_dt_dec;
	private BigDecimal	r33_net;
	private BigDecimal	r33_cpdm_dt_der;
	private BigDecimal	r33_cpdm_dt_dto;
	private BigDecimal	r33_cp;
	private String	r34_product;
	private BigDecimal	r34_open_position;
	private BigDecimal	r34_cpdm_dt_inc;
	private BigDecimal	r34_cpdm_dt_dec;
	private BigDecimal	r34_net;
	private BigDecimal	r34_cpdm_dt_der;
	private BigDecimal	r34_cpdm_dt_dto;
	private BigDecimal	r34_cp;
	private String	r35_product;
	private BigDecimal	r35_open_position;
	private BigDecimal	r35_cpdm_dt_inc;
	private BigDecimal	r35_cpdm_dt_dec;
	private BigDecimal	r35_net;
	private BigDecimal	r35_cpdm_dt_der;
	private BigDecimal	r35_cpdm_dt_dto;
	private BigDecimal	r35_cp;
	private String	r36_product;
	private BigDecimal	r36_open_position;
	private BigDecimal	r36_cpdm_dt_inc;
	private BigDecimal	r36_cpdm_dt_dec;
	private BigDecimal	r36_net;
	private BigDecimal	r36_cpdm_dt_der;
	private BigDecimal	r36_cpdm_dt_dto;
	private BigDecimal	r36_cp;
	
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
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_open_position() {
		return r13_open_position;
	}
	public void setR13_open_position(BigDecimal r13_open_position) {
		this.r13_open_position = r13_open_position;
	}
	public BigDecimal getR13_cpdm_dt_inc() {
		return r13_cpdm_dt_inc;
	}
	public void setR13_cpdm_dt_inc(BigDecimal r13_cpdm_dt_inc) {
		this.r13_cpdm_dt_inc = r13_cpdm_dt_inc;
	}
	public BigDecimal getR13_cpdm_dt_dec() {
		return r13_cpdm_dt_dec;
	}
	public void setR13_cpdm_dt_dec(BigDecimal r13_cpdm_dt_dec) {
		this.r13_cpdm_dt_dec = r13_cpdm_dt_dec;
	}
	public BigDecimal getR13_net() {
		return r13_net;
	}
	public void setR13_net(BigDecimal r13_net) {
		this.r13_net = r13_net;
	}
	public BigDecimal getR13_cpdm_dt_der() {
		return r13_cpdm_dt_der;
	}
	public void setR13_cpdm_dt_der(BigDecimal r13_cpdm_dt_der) {
		this.r13_cpdm_dt_der = r13_cpdm_dt_der;
	}
	public BigDecimal getR13_cpdm_dt_dto() {
		return r13_cpdm_dt_dto;
	}
	public void setR13_cpdm_dt_dto(BigDecimal r13_cpdm_dt_dto) {
		this.r13_cpdm_dt_dto = r13_cpdm_dt_dto;
	}
	public BigDecimal getR13_cp() {
		return r13_cp;
	}
	public void setR13_cp(BigDecimal r13_cp) {
		this.r13_cp = r13_cp;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_open_position() {
		return r14_open_position;
	}
	public void setR14_open_position(BigDecimal r14_open_position) {
		this.r14_open_position = r14_open_position;
	}
	public BigDecimal getR14_cpdm_dt_inc() {
		return r14_cpdm_dt_inc;
	}
	public void setR14_cpdm_dt_inc(BigDecimal r14_cpdm_dt_inc) {
		this.r14_cpdm_dt_inc = r14_cpdm_dt_inc;
	}
	public BigDecimal getR14_cpdm_dt_dec() {
		return r14_cpdm_dt_dec;
	}
	public void setR14_cpdm_dt_dec(BigDecimal r14_cpdm_dt_dec) {
		this.r14_cpdm_dt_dec = r14_cpdm_dt_dec;
	}
	public BigDecimal getR14_net() {
		return r14_net;
	}
	public void setR14_net(BigDecimal r14_net) {
		this.r14_net = r14_net;
	}
	public BigDecimal getR14_cpdm_dt_der() {
		return r14_cpdm_dt_der;
	}
	public void setR14_cpdm_dt_der(BigDecimal r14_cpdm_dt_der) {
		this.r14_cpdm_dt_der = r14_cpdm_dt_der;
	}
	public BigDecimal getR14_cpdm_dt_dto() {
		return r14_cpdm_dt_dto;
	}
	public void setR14_cpdm_dt_dto(BigDecimal r14_cpdm_dt_dto) {
		this.r14_cpdm_dt_dto = r14_cpdm_dt_dto;
	}
	public BigDecimal getR14_cp() {
		return r14_cp;
	}
	public void setR14_cp(BigDecimal r14_cp) {
		this.r14_cp = r14_cp;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_open_position() {
		return r15_open_position;
	}
	public void setR15_open_position(BigDecimal r15_open_position) {
		this.r15_open_position = r15_open_position;
	}
	public BigDecimal getR15_cpdm_dt_inc() {
		return r15_cpdm_dt_inc;
	}
	public void setR15_cpdm_dt_inc(BigDecimal r15_cpdm_dt_inc) {
		this.r15_cpdm_dt_inc = r15_cpdm_dt_inc;
	}
	public BigDecimal getR15_cpdm_dt_dec() {
		return r15_cpdm_dt_dec;
	}
	public void setR15_cpdm_dt_dec(BigDecimal r15_cpdm_dt_dec) {
		this.r15_cpdm_dt_dec = r15_cpdm_dt_dec;
	}
	public BigDecimal getR15_net() {
		return r15_net;
	}
	public void setR15_net(BigDecimal r15_net) {
		this.r15_net = r15_net;
	}
	public BigDecimal getR15_cpdm_dt_der() {
		return r15_cpdm_dt_der;
	}
	public void setR15_cpdm_dt_der(BigDecimal r15_cpdm_dt_der) {
		this.r15_cpdm_dt_der = r15_cpdm_dt_der;
	}
	public BigDecimal getR15_cpdm_dt_dto() {
		return r15_cpdm_dt_dto;
	}
	public void setR15_cpdm_dt_dto(BigDecimal r15_cpdm_dt_dto) {
		this.r15_cpdm_dt_dto = r15_cpdm_dt_dto;
	}
	public BigDecimal getR15_cp() {
		return r15_cp;
	}
	public void setR15_cp(BigDecimal r15_cp) {
		this.r15_cp = r15_cp;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_open_position() {
		return r16_open_position;
	}
	public void setR16_open_position(BigDecimal r16_open_position) {
		this.r16_open_position = r16_open_position;
	}
	public BigDecimal getR16_cpdm_dt_inc() {
		return r16_cpdm_dt_inc;
	}
	public void setR16_cpdm_dt_inc(BigDecimal r16_cpdm_dt_inc) {
		this.r16_cpdm_dt_inc = r16_cpdm_dt_inc;
	}
	public BigDecimal getR16_cpdm_dt_dec() {
		return r16_cpdm_dt_dec;
	}
	public void setR16_cpdm_dt_dec(BigDecimal r16_cpdm_dt_dec) {
		this.r16_cpdm_dt_dec = r16_cpdm_dt_dec;
	}
	public BigDecimal getR16_net() {
		return r16_net;
	}
	public void setR16_net(BigDecimal r16_net) {
		this.r16_net = r16_net;
	}
	public BigDecimal getR16_cpdm_dt_der() {
		return r16_cpdm_dt_der;
	}
	public void setR16_cpdm_dt_der(BigDecimal r16_cpdm_dt_der) {
		this.r16_cpdm_dt_der = r16_cpdm_dt_der;
	}
	public BigDecimal getR16_cpdm_dt_dto() {
		return r16_cpdm_dt_dto;
	}
	public void setR16_cpdm_dt_dto(BigDecimal r16_cpdm_dt_dto) {
		this.r16_cpdm_dt_dto = r16_cpdm_dt_dto;
	}
	public BigDecimal getR16_cp() {
		return r16_cp;
	}
	public void setR16_cp(BigDecimal r16_cp) {
		this.r16_cp = r16_cp;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_open_position() {
		return r17_open_position;
	}
	public void setR17_open_position(BigDecimal r17_open_position) {
		this.r17_open_position = r17_open_position;
	}
	public BigDecimal getR17_cpdm_dt_inc() {
		return r17_cpdm_dt_inc;
	}
	public void setR17_cpdm_dt_inc(BigDecimal r17_cpdm_dt_inc) {
		this.r17_cpdm_dt_inc = r17_cpdm_dt_inc;
	}
	public BigDecimal getR17_cpdm_dt_dec() {
		return r17_cpdm_dt_dec;
	}
	public void setR17_cpdm_dt_dec(BigDecimal r17_cpdm_dt_dec) {
		this.r17_cpdm_dt_dec = r17_cpdm_dt_dec;
	}
	public BigDecimal getR17_net() {
		return r17_net;
	}
	public void setR17_net(BigDecimal r17_net) {
		this.r17_net = r17_net;
	}
	public BigDecimal getR17_cpdm_dt_der() {
		return r17_cpdm_dt_der;
	}
	public void setR17_cpdm_dt_der(BigDecimal r17_cpdm_dt_der) {
		this.r17_cpdm_dt_der = r17_cpdm_dt_der;
	}
	public BigDecimal getR17_cpdm_dt_dto() {
		return r17_cpdm_dt_dto;
	}
	public void setR17_cpdm_dt_dto(BigDecimal r17_cpdm_dt_dto) {
		this.r17_cpdm_dt_dto = r17_cpdm_dt_dto;
	}
	public BigDecimal getR17_cp() {
		return r17_cp;
	}
	public void setR17_cp(BigDecimal r17_cp) {
		this.r17_cp = r17_cp;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_open_position() {
		return r18_open_position;
	}
	public void setR18_open_position(BigDecimal r18_open_position) {
		this.r18_open_position = r18_open_position;
	}
	public BigDecimal getR18_cpdm_dt_inc() {
		return r18_cpdm_dt_inc;
	}
	public void setR18_cpdm_dt_inc(BigDecimal r18_cpdm_dt_inc) {
		this.r18_cpdm_dt_inc = r18_cpdm_dt_inc;
	}
	public BigDecimal getR18_cpdm_dt_dec() {
		return r18_cpdm_dt_dec;
	}
	public void setR18_cpdm_dt_dec(BigDecimal r18_cpdm_dt_dec) {
		this.r18_cpdm_dt_dec = r18_cpdm_dt_dec;
	}
	public BigDecimal getR18_net() {
		return r18_net;
	}
	public void setR18_net(BigDecimal r18_net) {
		this.r18_net = r18_net;
	}
	public BigDecimal getR18_cpdm_dt_der() {
		return r18_cpdm_dt_der;
	}
	public void setR18_cpdm_dt_der(BigDecimal r18_cpdm_dt_der) {
		this.r18_cpdm_dt_der = r18_cpdm_dt_der;
	}
	public BigDecimal getR18_cpdm_dt_dto() {
		return r18_cpdm_dt_dto;
	}
	public void setR18_cpdm_dt_dto(BigDecimal r18_cpdm_dt_dto) {
		this.r18_cpdm_dt_dto = r18_cpdm_dt_dto;
	}
	public BigDecimal getR18_cp() {
		return r18_cp;
	}
	public void setR18_cp(BigDecimal r18_cp) {
		this.r18_cp = r18_cp;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_open_position() {
		return r19_open_position;
	}
	public void setR19_open_position(BigDecimal r19_open_position) {
		this.r19_open_position = r19_open_position;
	}
	public BigDecimal getR19_cpdm_dt_inc() {
		return r19_cpdm_dt_inc;
	}
	public void setR19_cpdm_dt_inc(BigDecimal r19_cpdm_dt_inc) {
		this.r19_cpdm_dt_inc = r19_cpdm_dt_inc;
	}
	public BigDecimal getR19_cpdm_dt_dec() {
		return r19_cpdm_dt_dec;
	}
	public void setR19_cpdm_dt_dec(BigDecimal r19_cpdm_dt_dec) {
		this.r19_cpdm_dt_dec = r19_cpdm_dt_dec;
	}
	public BigDecimal getR19_net() {
		return r19_net;
	}
	public void setR19_net(BigDecimal r19_net) {
		this.r19_net = r19_net;
	}
	public BigDecimal getR19_cpdm_dt_der() {
		return r19_cpdm_dt_der;
	}
	public void setR19_cpdm_dt_der(BigDecimal r19_cpdm_dt_der) {
		this.r19_cpdm_dt_der = r19_cpdm_dt_der;
	}
	public BigDecimal getR19_cpdm_dt_dto() {
		return r19_cpdm_dt_dto;
	}
	public void setR19_cpdm_dt_dto(BigDecimal r19_cpdm_dt_dto) {
		this.r19_cpdm_dt_dto = r19_cpdm_dt_dto;
	}
	public BigDecimal getR19_cp() {
		return r19_cp;
	}
	public void setR19_cp(BigDecimal r19_cp) {
		this.r19_cp = r19_cp;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_open_position() {
		return r20_open_position;
	}
	public void setR20_open_position(BigDecimal r20_open_position) {
		this.r20_open_position = r20_open_position;
	}
	public BigDecimal getR20_cpdm_dt_inc() {
		return r20_cpdm_dt_inc;
	}
	public void setR20_cpdm_dt_inc(BigDecimal r20_cpdm_dt_inc) {
		this.r20_cpdm_dt_inc = r20_cpdm_dt_inc;
	}
	public BigDecimal getR20_cpdm_dt_dec() {
		return r20_cpdm_dt_dec;
	}
	public void setR20_cpdm_dt_dec(BigDecimal r20_cpdm_dt_dec) {
		this.r20_cpdm_dt_dec = r20_cpdm_dt_dec;
	}
	public BigDecimal getR20_net() {
		return r20_net;
	}
	public void setR20_net(BigDecimal r20_net) {
		this.r20_net = r20_net;
	}
	public BigDecimal getR20_cpdm_dt_der() {
		return r20_cpdm_dt_der;
	}
	public void setR20_cpdm_dt_der(BigDecimal r20_cpdm_dt_der) {
		this.r20_cpdm_dt_der = r20_cpdm_dt_der;
	}
	public BigDecimal getR20_cpdm_dt_dto() {
		return r20_cpdm_dt_dto;
	}
	public void setR20_cpdm_dt_dto(BigDecimal r20_cpdm_dt_dto) {
		this.r20_cpdm_dt_dto = r20_cpdm_dt_dto;
	}
	public BigDecimal getR20_cp() {
		return r20_cp;
	}
	public void setR20_cp(BigDecimal r20_cp) {
		this.r20_cp = r20_cp;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_open_position() {
		return r21_open_position;
	}
	public void setR21_open_position(BigDecimal r21_open_position) {
		this.r21_open_position = r21_open_position;
	}
	public BigDecimal getR21_cpdm_dt_inc() {
		return r21_cpdm_dt_inc;
	}
	public void setR21_cpdm_dt_inc(BigDecimal r21_cpdm_dt_inc) {
		this.r21_cpdm_dt_inc = r21_cpdm_dt_inc;
	}
	public BigDecimal getR21_cpdm_dt_dec() {
		return r21_cpdm_dt_dec;
	}
	public void setR21_cpdm_dt_dec(BigDecimal r21_cpdm_dt_dec) {
		this.r21_cpdm_dt_dec = r21_cpdm_dt_dec;
	}
	public BigDecimal getR21_net() {
		return r21_net;
	}
	public void setR21_net(BigDecimal r21_net) {
		this.r21_net = r21_net;
	}
	public BigDecimal getR21_cpdm_dt_der() {
		return r21_cpdm_dt_der;
	}
	public void setR21_cpdm_dt_der(BigDecimal r21_cpdm_dt_der) {
		this.r21_cpdm_dt_der = r21_cpdm_dt_der;
	}
	public BigDecimal getR21_cpdm_dt_dto() {
		return r21_cpdm_dt_dto;
	}
	public void setR21_cpdm_dt_dto(BigDecimal r21_cpdm_dt_dto) {
		this.r21_cpdm_dt_dto = r21_cpdm_dt_dto;
	}
	public BigDecimal getR21_cp() {
		return r21_cp;
	}
	public void setR21_cp(BigDecimal r21_cp) {
		this.r21_cp = r21_cp;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_open_position() {
		return r22_open_position;
	}
	public void setR22_open_position(BigDecimal r22_open_position) {
		this.r22_open_position = r22_open_position;
	}
	public BigDecimal getR22_cpdm_dt_inc() {
		return r22_cpdm_dt_inc;
	}
	public void setR22_cpdm_dt_inc(BigDecimal r22_cpdm_dt_inc) {
		this.r22_cpdm_dt_inc = r22_cpdm_dt_inc;
	}
	public BigDecimal getR22_cpdm_dt_dec() {
		return r22_cpdm_dt_dec;
	}
	public void setR22_cpdm_dt_dec(BigDecimal r22_cpdm_dt_dec) {
		this.r22_cpdm_dt_dec = r22_cpdm_dt_dec;
	}
	public BigDecimal getR22_net() {
		return r22_net;
	}
	public void setR22_net(BigDecimal r22_net) {
		this.r22_net = r22_net;
	}
	public BigDecimal getR22_cpdm_dt_der() {
		return r22_cpdm_dt_der;
	}
	public void setR22_cpdm_dt_der(BigDecimal r22_cpdm_dt_der) {
		this.r22_cpdm_dt_der = r22_cpdm_dt_der;
	}
	public BigDecimal getR22_cpdm_dt_dto() {
		return r22_cpdm_dt_dto;
	}
	public void setR22_cpdm_dt_dto(BigDecimal r22_cpdm_dt_dto) {
		this.r22_cpdm_dt_dto = r22_cpdm_dt_dto;
	}
	public BigDecimal getR22_cp() {
		return r22_cp;
	}
	public void setR22_cp(BigDecimal r22_cp) {
		this.r22_cp = r22_cp;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_open_position() {
		return r23_open_position;
	}
	public void setR23_open_position(BigDecimal r23_open_position) {
		this.r23_open_position = r23_open_position;
	}
	public BigDecimal getR23_cpdm_dt_inc() {
		return r23_cpdm_dt_inc;
	}
	public void setR23_cpdm_dt_inc(BigDecimal r23_cpdm_dt_inc) {
		this.r23_cpdm_dt_inc = r23_cpdm_dt_inc;
	}
	public BigDecimal getR23_cpdm_dt_dec() {
		return r23_cpdm_dt_dec;
	}
	public void setR23_cpdm_dt_dec(BigDecimal r23_cpdm_dt_dec) {
		this.r23_cpdm_dt_dec = r23_cpdm_dt_dec;
	}
	public BigDecimal getR23_net() {
		return r23_net;
	}
	public void setR23_net(BigDecimal r23_net) {
		this.r23_net = r23_net;
	}
	public BigDecimal getR23_cpdm_dt_der() {
		return r23_cpdm_dt_der;
	}
	public void setR23_cpdm_dt_der(BigDecimal r23_cpdm_dt_der) {
		this.r23_cpdm_dt_der = r23_cpdm_dt_der;
	}
	public BigDecimal getR23_cpdm_dt_dto() {
		return r23_cpdm_dt_dto;
	}
	public void setR23_cpdm_dt_dto(BigDecimal r23_cpdm_dt_dto) {
		this.r23_cpdm_dt_dto = r23_cpdm_dt_dto;
	}
	public BigDecimal getR23_cp() {
		return r23_cp;
	}
	public void setR23_cp(BigDecimal r23_cp) {
		this.r23_cp = r23_cp;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_open_position() {
		return r24_open_position;
	}
	public void setR24_open_position(BigDecimal r24_open_position) {
		this.r24_open_position = r24_open_position;
	}
	public BigDecimal getR24_cpdm_dt_inc() {
		return r24_cpdm_dt_inc;
	}
	public void setR24_cpdm_dt_inc(BigDecimal r24_cpdm_dt_inc) {
		this.r24_cpdm_dt_inc = r24_cpdm_dt_inc;
	}
	public BigDecimal getR24_cpdm_dt_dec() {
		return r24_cpdm_dt_dec;
	}
	public void setR24_cpdm_dt_dec(BigDecimal r24_cpdm_dt_dec) {
		this.r24_cpdm_dt_dec = r24_cpdm_dt_dec;
	}
	public BigDecimal getR24_net() {
		return r24_net;
	}
	public void setR24_net(BigDecimal r24_net) {
		this.r24_net = r24_net;
	}
	public BigDecimal getR24_cpdm_dt_der() {
		return r24_cpdm_dt_der;
	}
	public void setR24_cpdm_dt_der(BigDecimal r24_cpdm_dt_der) {
		this.r24_cpdm_dt_der = r24_cpdm_dt_der;
	}
	public BigDecimal getR24_cpdm_dt_dto() {
		return r24_cpdm_dt_dto;
	}
	public void setR24_cpdm_dt_dto(BigDecimal r24_cpdm_dt_dto) {
		this.r24_cpdm_dt_dto = r24_cpdm_dt_dto;
	}
	public BigDecimal getR24_cp() {
		return r24_cp;
	}
	public void setR24_cp(BigDecimal r24_cp) {
		this.r24_cp = r24_cp;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_open_position() {
		return r25_open_position;
	}
	public void setR25_open_position(BigDecimal r25_open_position) {
		this.r25_open_position = r25_open_position;
	}
	public BigDecimal getR25_cpdm_dt_inc() {
		return r25_cpdm_dt_inc;
	}
	public void setR25_cpdm_dt_inc(BigDecimal r25_cpdm_dt_inc) {
		this.r25_cpdm_dt_inc = r25_cpdm_dt_inc;
	}
	public BigDecimal getR25_cpdm_dt_dec() {
		return r25_cpdm_dt_dec;
	}
	public void setR25_cpdm_dt_dec(BigDecimal r25_cpdm_dt_dec) {
		this.r25_cpdm_dt_dec = r25_cpdm_dt_dec;
	}
	public BigDecimal getR25_net() {
		return r25_net;
	}
	public void setR25_net(BigDecimal r25_net) {
		this.r25_net = r25_net;
	}
	public BigDecimal getR25_cpdm_dt_der() {
		return r25_cpdm_dt_der;
	}
	public void setR25_cpdm_dt_der(BigDecimal r25_cpdm_dt_der) {
		this.r25_cpdm_dt_der = r25_cpdm_dt_der;
	}
	public BigDecimal getR25_cpdm_dt_dto() {
		return r25_cpdm_dt_dto;
	}
	public void setR25_cpdm_dt_dto(BigDecimal r25_cpdm_dt_dto) {
		this.r25_cpdm_dt_dto = r25_cpdm_dt_dto;
	}
	public BigDecimal getR25_cp() {
		return r25_cp;
	}
	public void setR25_cp(BigDecimal r25_cp) {
		this.r25_cp = r25_cp;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_open_position() {
		return r26_open_position;
	}
	public void setR26_open_position(BigDecimal r26_open_position) {
		this.r26_open_position = r26_open_position;
	}
	public BigDecimal getR26_cpdm_dt_inc() {
		return r26_cpdm_dt_inc;
	}
	public void setR26_cpdm_dt_inc(BigDecimal r26_cpdm_dt_inc) {
		this.r26_cpdm_dt_inc = r26_cpdm_dt_inc;
	}
	public BigDecimal getR26_cpdm_dt_dec() {
		return r26_cpdm_dt_dec;
	}
	public void setR26_cpdm_dt_dec(BigDecimal r26_cpdm_dt_dec) {
		this.r26_cpdm_dt_dec = r26_cpdm_dt_dec;
	}
	public BigDecimal getR26_net() {
		return r26_net;
	}
	public void setR26_net(BigDecimal r26_net) {
		this.r26_net = r26_net;
	}
	public BigDecimal getR26_cpdm_dt_der() {
		return r26_cpdm_dt_der;
	}
	public void setR26_cpdm_dt_der(BigDecimal r26_cpdm_dt_der) {
		this.r26_cpdm_dt_der = r26_cpdm_dt_der;
	}
	public BigDecimal getR26_cpdm_dt_dto() {
		return r26_cpdm_dt_dto;
	}
	public void setR26_cpdm_dt_dto(BigDecimal r26_cpdm_dt_dto) {
		this.r26_cpdm_dt_dto = r26_cpdm_dt_dto;
	}
	public BigDecimal getR26_cp() {
		return r26_cp;
	}
	public void setR26_cp(BigDecimal r26_cp) {
		this.r26_cp = r26_cp;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_open_position() {
		return r27_open_position;
	}
	public void setR27_open_position(BigDecimal r27_open_position) {
		this.r27_open_position = r27_open_position;
	}
	public BigDecimal getR27_cpdm_dt_inc() {
		return r27_cpdm_dt_inc;
	}
	public void setR27_cpdm_dt_inc(BigDecimal r27_cpdm_dt_inc) {
		this.r27_cpdm_dt_inc = r27_cpdm_dt_inc;
	}
	public BigDecimal getR27_cpdm_dt_dec() {
		return r27_cpdm_dt_dec;
	}
	public void setR27_cpdm_dt_dec(BigDecimal r27_cpdm_dt_dec) {
		this.r27_cpdm_dt_dec = r27_cpdm_dt_dec;
	}
	public BigDecimal getR27_net() {
		return r27_net;
	}
	public void setR27_net(BigDecimal r27_net) {
		this.r27_net = r27_net;
	}
	public BigDecimal getR27_cpdm_dt_der() {
		return r27_cpdm_dt_der;
	}
	public void setR27_cpdm_dt_der(BigDecimal r27_cpdm_dt_der) {
		this.r27_cpdm_dt_der = r27_cpdm_dt_der;
	}
	public BigDecimal getR27_cpdm_dt_dto() {
		return r27_cpdm_dt_dto;
	}
	public void setR27_cpdm_dt_dto(BigDecimal r27_cpdm_dt_dto) {
		this.r27_cpdm_dt_dto = r27_cpdm_dt_dto;
	}
	public BigDecimal getR27_cp() {
		return r27_cp;
	}
	public void setR27_cp(BigDecimal r27_cp) {
		this.r27_cp = r27_cp;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_open_position() {
		return r28_open_position;
	}
	public void setR28_open_position(BigDecimal r28_open_position) {
		this.r28_open_position = r28_open_position;
	}
	public BigDecimal getR28_cpdm_dt_inc() {
		return r28_cpdm_dt_inc;
	}
	public void setR28_cpdm_dt_inc(BigDecimal r28_cpdm_dt_inc) {
		this.r28_cpdm_dt_inc = r28_cpdm_dt_inc;
	}
	public BigDecimal getR28_cpdm_dt_dec() {
		return r28_cpdm_dt_dec;
	}
	public void setR28_cpdm_dt_dec(BigDecimal r28_cpdm_dt_dec) {
		this.r28_cpdm_dt_dec = r28_cpdm_dt_dec;
	}
	public BigDecimal getR28_net() {
		return r28_net;
	}
	public void setR28_net(BigDecimal r28_net) {
		this.r28_net = r28_net;
	}
	public BigDecimal getR28_cpdm_dt_der() {
		return r28_cpdm_dt_der;
	}
	public void setR28_cpdm_dt_der(BigDecimal r28_cpdm_dt_der) {
		this.r28_cpdm_dt_der = r28_cpdm_dt_der;
	}
	public BigDecimal getR28_cpdm_dt_dto() {
		return r28_cpdm_dt_dto;
	}
	public void setR28_cpdm_dt_dto(BigDecimal r28_cpdm_dt_dto) {
		this.r28_cpdm_dt_dto = r28_cpdm_dt_dto;
	}
	public BigDecimal getR28_cp() {
		return r28_cp;
	}
	public void setR28_cp(BigDecimal r28_cp) {
		this.r28_cp = r28_cp;
	}
	public String getR29_product() {
		return r29_product;
	}
	public void setR29_product(String r29_product) {
		this.r29_product = r29_product;
	}
	public BigDecimal getR29_open_position() {
		return r29_open_position;
	}
	public void setR29_open_position(BigDecimal r29_open_position) {
		this.r29_open_position = r29_open_position;
	}
	public BigDecimal getR29_cpdm_dt_inc() {
		return r29_cpdm_dt_inc;
	}
	public void setR29_cpdm_dt_inc(BigDecimal r29_cpdm_dt_inc) {
		this.r29_cpdm_dt_inc = r29_cpdm_dt_inc;
	}
	public BigDecimal getR29_cpdm_dt_dec() {
		return r29_cpdm_dt_dec;
	}
	public void setR29_cpdm_dt_dec(BigDecimal r29_cpdm_dt_dec) {
		this.r29_cpdm_dt_dec = r29_cpdm_dt_dec;
	}
	public BigDecimal getR29_net() {
		return r29_net;
	}
	public void setR29_net(BigDecimal r29_net) {
		this.r29_net = r29_net;
	}
	public BigDecimal getR29_cpdm_dt_der() {
		return r29_cpdm_dt_der;
	}
	public void setR29_cpdm_dt_der(BigDecimal r29_cpdm_dt_der) {
		this.r29_cpdm_dt_der = r29_cpdm_dt_der;
	}
	public BigDecimal getR29_cpdm_dt_dto() {
		return r29_cpdm_dt_dto;
	}
	public void setR29_cpdm_dt_dto(BigDecimal r29_cpdm_dt_dto) {
		this.r29_cpdm_dt_dto = r29_cpdm_dt_dto;
	}
	public BigDecimal getR29_cp() {
		return r29_cp;
	}
	public void setR29_cp(BigDecimal r29_cp) {
		this.r29_cp = r29_cp;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_open_position() {
		return r30_open_position;
	}
	public void setR30_open_position(BigDecimal r30_open_position) {
		this.r30_open_position = r30_open_position;
	}
	public BigDecimal getR30_cpdm_dt_inc() {
		return r30_cpdm_dt_inc;
	}
	public void setR30_cpdm_dt_inc(BigDecimal r30_cpdm_dt_inc) {
		this.r30_cpdm_dt_inc = r30_cpdm_dt_inc;
	}
	public BigDecimal getR30_cpdm_dt_dec() {
		return r30_cpdm_dt_dec;
	}
	public void setR30_cpdm_dt_dec(BigDecimal r30_cpdm_dt_dec) {
		this.r30_cpdm_dt_dec = r30_cpdm_dt_dec;
	}
	public BigDecimal getR30_net() {
		return r30_net;
	}
	public void setR30_net(BigDecimal r30_net) {
		this.r30_net = r30_net;
	}
	public BigDecimal getR30_cpdm_dt_der() {
		return r30_cpdm_dt_der;
	}
	public void setR30_cpdm_dt_der(BigDecimal r30_cpdm_dt_der) {
		this.r30_cpdm_dt_der = r30_cpdm_dt_der;
	}
	public BigDecimal getR30_cpdm_dt_dto() {
		return r30_cpdm_dt_dto;
	}
	public void setR30_cpdm_dt_dto(BigDecimal r30_cpdm_dt_dto) {
		this.r30_cpdm_dt_dto = r30_cpdm_dt_dto;
	}
	public BigDecimal getR30_cp() {
		return r30_cp;
	}
	public void setR30_cp(BigDecimal r30_cp) {
		this.r30_cp = r30_cp;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_open_position() {
		return r31_open_position;
	}
	public void setR31_open_position(BigDecimal r31_open_position) {
		this.r31_open_position = r31_open_position;
	}
	public BigDecimal getR31_cpdm_dt_inc() {
		return r31_cpdm_dt_inc;
	}
	public void setR31_cpdm_dt_inc(BigDecimal r31_cpdm_dt_inc) {
		this.r31_cpdm_dt_inc = r31_cpdm_dt_inc;
	}
	public BigDecimal getR31_cpdm_dt_dec() {
		return r31_cpdm_dt_dec;
	}
	public void setR31_cpdm_dt_dec(BigDecimal r31_cpdm_dt_dec) {
		this.r31_cpdm_dt_dec = r31_cpdm_dt_dec;
	}
	public BigDecimal getR31_net() {
		return r31_net;
	}
	public void setR31_net(BigDecimal r31_net) {
		this.r31_net = r31_net;
	}
	public BigDecimal getR31_cpdm_dt_der() {
		return r31_cpdm_dt_der;
	}
	public void setR31_cpdm_dt_der(BigDecimal r31_cpdm_dt_der) {
		this.r31_cpdm_dt_der = r31_cpdm_dt_der;
	}
	public BigDecimal getR31_cpdm_dt_dto() {
		return r31_cpdm_dt_dto;
	}
	public void setR31_cpdm_dt_dto(BigDecimal r31_cpdm_dt_dto) {
		this.r31_cpdm_dt_dto = r31_cpdm_dt_dto;
	}
	public BigDecimal getR31_cp() {
		return r31_cp;
	}
	public void setR31_cp(BigDecimal r31_cp) {
		this.r31_cp = r31_cp;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_open_position() {
		return r32_open_position;
	}
	public void setR32_open_position(BigDecimal r32_open_position) {
		this.r32_open_position = r32_open_position;
	}
	public BigDecimal getR32_cpdm_dt_inc() {
		return r32_cpdm_dt_inc;
	}
	public void setR32_cpdm_dt_inc(BigDecimal r32_cpdm_dt_inc) {
		this.r32_cpdm_dt_inc = r32_cpdm_dt_inc;
	}
	public BigDecimal getR32_cpdm_dt_dec() {
		return r32_cpdm_dt_dec;
	}
	public void setR32_cpdm_dt_dec(BigDecimal r32_cpdm_dt_dec) {
		this.r32_cpdm_dt_dec = r32_cpdm_dt_dec;
	}
	public BigDecimal getR32_net() {
		return r32_net;
	}
	public void setR32_net(BigDecimal r32_net) {
		this.r32_net = r32_net;
	}
	public BigDecimal getR32_cpdm_dt_der() {
		return r32_cpdm_dt_der;
	}
	public void setR32_cpdm_dt_der(BigDecimal r32_cpdm_dt_der) {
		this.r32_cpdm_dt_der = r32_cpdm_dt_der;
	}
	public BigDecimal getR32_cpdm_dt_dto() {
		return r32_cpdm_dt_dto;
	}
	public void setR32_cpdm_dt_dto(BigDecimal r32_cpdm_dt_dto) {
		this.r32_cpdm_dt_dto = r32_cpdm_dt_dto;
	}
	public BigDecimal getR32_cp() {
		return r32_cp;
	}
	public void setR32_cp(BigDecimal r32_cp) {
		this.r32_cp = r32_cp;
	}
	public String getR33_product() {
		return r33_product;
	}
	public void setR33_product(String r33_product) {
		this.r33_product = r33_product;
	}
	public BigDecimal getR33_open_position() {
		return r33_open_position;
	}
	public void setR33_open_position(BigDecimal r33_open_position) {
		this.r33_open_position = r33_open_position;
	}
	public BigDecimal getR33_cpdm_dt_inc() {
		return r33_cpdm_dt_inc;
	}
	public void setR33_cpdm_dt_inc(BigDecimal r33_cpdm_dt_inc) {
		this.r33_cpdm_dt_inc = r33_cpdm_dt_inc;
	}
	public BigDecimal getR33_cpdm_dt_dec() {
		return r33_cpdm_dt_dec;
	}
	public void setR33_cpdm_dt_dec(BigDecimal r33_cpdm_dt_dec) {
		this.r33_cpdm_dt_dec = r33_cpdm_dt_dec;
	}
	public BigDecimal getR33_net() {
		return r33_net;
	}
	public void setR33_net(BigDecimal r33_net) {
		this.r33_net = r33_net;
	}
	public BigDecimal getR33_cpdm_dt_der() {
		return r33_cpdm_dt_der;
	}
	public void setR33_cpdm_dt_der(BigDecimal r33_cpdm_dt_der) {
		this.r33_cpdm_dt_der = r33_cpdm_dt_der;
	}
	public BigDecimal getR33_cpdm_dt_dto() {
		return r33_cpdm_dt_dto;
	}
	public void setR33_cpdm_dt_dto(BigDecimal r33_cpdm_dt_dto) {
		this.r33_cpdm_dt_dto = r33_cpdm_dt_dto;
	}
	public BigDecimal getR33_cp() {
		return r33_cp;
	}
	public void setR33_cp(BigDecimal r33_cp) {
		this.r33_cp = r33_cp;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_open_position() {
		return r34_open_position;
	}
	public void setR34_open_position(BigDecimal r34_open_position) {
		this.r34_open_position = r34_open_position;
	}
	public BigDecimal getR34_cpdm_dt_inc() {
		return r34_cpdm_dt_inc;
	}
	public void setR34_cpdm_dt_inc(BigDecimal r34_cpdm_dt_inc) {
		this.r34_cpdm_dt_inc = r34_cpdm_dt_inc;
	}
	public BigDecimal getR34_cpdm_dt_dec() {
		return r34_cpdm_dt_dec;
	}
	public void setR34_cpdm_dt_dec(BigDecimal r34_cpdm_dt_dec) {
		this.r34_cpdm_dt_dec = r34_cpdm_dt_dec;
	}
	public BigDecimal getR34_net() {
		return r34_net;
	}
	public void setR34_net(BigDecimal r34_net) {
		this.r34_net = r34_net;
	}
	public BigDecimal getR34_cpdm_dt_der() {
		return r34_cpdm_dt_der;
	}
	public void setR34_cpdm_dt_der(BigDecimal r34_cpdm_dt_der) {
		this.r34_cpdm_dt_der = r34_cpdm_dt_der;
	}
	public BigDecimal getR34_cpdm_dt_dto() {
		return r34_cpdm_dt_dto;
	}
	public void setR34_cpdm_dt_dto(BigDecimal r34_cpdm_dt_dto) {
		this.r34_cpdm_dt_dto = r34_cpdm_dt_dto;
	}
	public BigDecimal getR34_cp() {
		return r34_cp;
	}
	public void setR34_cp(BigDecimal r34_cp) {
		this.r34_cp = r34_cp;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_open_position() {
		return r35_open_position;
	}
	public void setR35_open_position(BigDecimal r35_open_position) {
		this.r35_open_position = r35_open_position;
	}
	public BigDecimal getR35_cpdm_dt_inc() {
		return r35_cpdm_dt_inc;
	}
	public void setR35_cpdm_dt_inc(BigDecimal r35_cpdm_dt_inc) {
		this.r35_cpdm_dt_inc = r35_cpdm_dt_inc;
	}
	public BigDecimal getR35_cpdm_dt_dec() {
		return r35_cpdm_dt_dec;
	}
	public void setR35_cpdm_dt_dec(BigDecimal r35_cpdm_dt_dec) {
		this.r35_cpdm_dt_dec = r35_cpdm_dt_dec;
	}
	public BigDecimal getR35_net() {
		return r35_net;
	}
	public void setR35_net(BigDecimal r35_net) {
		this.r35_net = r35_net;
	}
	public BigDecimal getR35_cpdm_dt_der() {
		return r35_cpdm_dt_der;
	}
	public void setR35_cpdm_dt_der(BigDecimal r35_cpdm_dt_der) {
		this.r35_cpdm_dt_der = r35_cpdm_dt_der;
	}
	public BigDecimal getR35_cpdm_dt_dto() {
		return r35_cpdm_dt_dto;
	}
	public void setR35_cpdm_dt_dto(BigDecimal r35_cpdm_dt_dto) {
		this.r35_cpdm_dt_dto = r35_cpdm_dt_dto;
	}
	public BigDecimal getR35_cp() {
		return r35_cp;
	}
	public void setR35_cp(BigDecimal r35_cp) {
		this.r35_cp = r35_cp;
	}
	public String getR36_product() {
		return r36_product;
	}
	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}
	public BigDecimal getR36_open_position() {
		return r36_open_position;
	}
	public void setR36_open_position(BigDecimal r36_open_position) {
		this.r36_open_position = r36_open_position;
	}
	public BigDecimal getR36_cpdm_dt_inc() {
		return r36_cpdm_dt_inc;
	}
	public void setR36_cpdm_dt_inc(BigDecimal r36_cpdm_dt_inc) {
		this.r36_cpdm_dt_inc = r36_cpdm_dt_inc;
	}
	public BigDecimal getR36_cpdm_dt_dec() {
		return r36_cpdm_dt_dec;
	}
	public void setR36_cpdm_dt_dec(BigDecimal r36_cpdm_dt_dec) {
		this.r36_cpdm_dt_dec = r36_cpdm_dt_dec;
	}
	public BigDecimal getR36_net() {
		return r36_net;
	}
	public void setR36_net(BigDecimal r36_net) {
		this.r36_net = r36_net;
	}
	public BigDecimal getR36_cpdm_dt_der() {
		return r36_cpdm_dt_der;
	}
	public void setR36_cpdm_dt_der(BigDecimal r36_cpdm_dt_der) {
		this.r36_cpdm_dt_der = r36_cpdm_dt_der;
	}
	public BigDecimal getR36_cpdm_dt_dto() {
		return r36_cpdm_dt_dto;
	}
	public void setR36_cpdm_dt_dto(BigDecimal r36_cpdm_dt_dto) {
		this.r36_cpdm_dt_dto = r36_cpdm_dt_dto;
	}
	public BigDecimal getR36_cp() {
		return r36_cp;
	}
	public void setR36_cp(BigDecimal r36_cp) {
		this.r36_cp = r36_cp;
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
// MODEL AND VIEW METHOD summary M_BOP
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 

public ModelAndView getM_BOPView(
        String reportId,
        String fromdate,
        String todate,
        String currency,
        String dtltype,
        Pageable pageable,
        String type,
        BigDecimal version, HttpServletRequest req1,Model md) {

    ModelAndView mv = new ModelAndView();

    String userid = (String) req1.getSession().getAttribute("USERID");
    System.out.println("User Id Maker and Checker: " + userid);
    String role = userProfileRep.getUserRole(userid);
    md.addAttribute("role", role);
    System.out.println("Role: " + role);


    System.out.println("M_BOP View Called");
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

                List<M_BOP_Archival_Detail_Entity> T1Master =
                        getDetaildatabydateListarchival(dt, version);

                System.out.println("Archival Detail Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
                mv.addObject("displaymode", "detail");
            }

            // RESUB DETAIL
            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

                List<M_BOP_RESUB_Detail_Entity> T1Master =
                        getResubDetaildatabydateList(dt, version);

                System.out.println("Resub Detail Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
                mv.addObject("displaymode", "detail");
            }

            // NORMAL DETAIL
            else {

                List<M_BOP_Detail_Entity> T1Master =
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

                List<M_BOP_Archival_Summary_Entity> T1Master =
                        getDataByDateListArchival(dt, version);

                System.out.println("Archival Summary Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
            }

            // RESUB SUMMARY
            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

                List<M_BOP_RESUB_Summary_Entity> T1Master =
                        getResubSummarydatabydateListarchival(dt, version);

                System.out.println("Resub Summary Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
            }

            // NORMAL SUMMARY
            else {

                List<M_BOP_Summary_Entity> T1Master =
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

    mv.setViewName("BRRS/M_BOP");

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
public List<Object[]> getM_BOPArchival() {

    List<Object[]> archivalList = new ArrayList<>();

    try {

        List<M_BOP_Archival_Summary_Entity> repoData =
                getarchivaldatabydateListWithVersion();

        if (repoData != null && !repoData.isEmpty()) {

            for (M_BOP_Archival_Summary_Entity entity : repoData) {

                Object[] row = new Object[] {
                        entity.getReport_date(),
                        entity.getReport_version(),
                        entity.getReportResubDate()
                };

                archivalList.add(row);
            }

            System.out.println("Fetched " + archivalList.size() + " archival records");

            M_BOP_Archival_Summary_Entity first = repoData.get(0);

            System.out.println("Latest archival version: "
                    + first.getReport_version());

        } else {

            System.out.println("No archival data found.");
        }

    } catch (Exception e) {

        System.err.println("Error fetching M_BOP Archival data: "
                + e.getMessage());

        e.printStackTrace();
    }

    return archivalList;
}
//=====================================================
// UPDATE REPORT
//=====================================================

//@Transactional
//public void updateReport(M_BOP_Summary_Entity updatedEntity) {
//
//    System.out.println("Came to M_BOP Update");
//    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//    String[] fields = {
//            "product",
//            "open_position",
//            "cpdm_dt_inc",
//            "cpdm_dt_dec",
//            "net",
//            "cpdm_dt_der",
//            "cpdm_dt_dto",
//            "cp"
//    };
//
//    try {
//
//        for (int i = 13; i <= 36; i++) {
//
//            for (String field : fields) {
//
//                String getterName = "getR" + i + "_" + field;
//                String columnName = "R" + i + "_" + field;
//
//                try {
//
//                    Method getter =
//                            M_BOP_Summary_Entity.class.getMethod(getterName);
//
//                    Object value = getter.invoke(updatedEntity);
//
//                    if (value == null) {
//                        continue;
//                    }
//
//                    // ===========================
//                    // UPDATE SUMMARY TABLE
//                    // ===========================
//
//                    String summarySql =
//                            "UPDATE BRRS_M_BOP_SUMMARYTABLE " +
//                            "SET " + columnName + " = ? " +
//                            "WHERE REPORT_DATE = ?";
//
//                    jdbcTemplate.update(
//                            summarySql,
//                            value,
//                            updatedEntity.getReport_date()
//                    );
//
//                    // ===========================
//                    // UPDATE DETAIL TABLE
//                    // ===========================
//
//                    String detailSql =
//                            "UPDATE BRRS_M_BOP_DETAILTABLE " +
//                            "SET " + columnName + " = ? " +
//                            "WHERE REPORT_DATE = ?";
//
//                    jdbcTemplate.update(
//                            detailSql,
//                            value,
//                            updatedEntity.getReport_date()
//                    );
//
//                } catch (NoSuchMethodException e) {
//                    continue;
//                }
//            }
//        }
//
//        System.out.println("M_BOP Summary & Detail Update Completed");
//
//    } catch (Exception e) {
//
//        throw new RuntimeException(
//                "Error while updating M_BOP fields", e);
//    }
//}


@Transactional
public void updateReport(M_BOP_Summary_Entity updatedEntity) {

    System.out.println("Came to M_BOP Update");
    System.out.println("Report Date: " + updatedEntity.getReport_date());

    // Fetch existing summary record for audit
    M_BOP_Summary_Entity existingSummary =
            findByReportDate(updatedEntity.getReport_date());

    if (existingSummary == null) {
        throw new RuntimeException(
                "Record not found for REPORT_DATE : "
                        + updatedEntity.getReport_date());
    }

    // Audit old copy
    M_BOP_Summary_Entity oldcopy =
            new M_BOP_Summary_Entity();

    BeanUtils.copyProperties(existingSummary, oldcopy);

    String[] fields = {
            "product",
            "open_position",
            "cpdm_dt_inc",
            "cpdm_dt_dec",
            "net",
            "cpdm_dt_der",
            "cpdm_dt_dto",
            "cp"
    };

    try {

        for (int i = 13; i <= 36; i++) {

            for (String field : fields) {

                String getterName = "getR" + i + "_" + field;
                String setterName = "setR" + i + "_" + field;
                String columnName = "R" + i + "_" + field;

                try {

                    Method getter =
                            M_BOP_Summary_Entity.class.getMethod(getterName);

                    Object value = getter.invoke(updatedEntity);

                    if (value == null) {
                        continue;
                    }

                    // Update existing object for audit
                    Method setter =
                            M_BOP_Summary_Entity.class.getMethod(
                                    setterName,
                                    getter.getReturnType());

                    setter.invoke(existingSummary, value);

                    // ===========================
                    // UPDATE SUMMARY TABLE
                    // ===========================

                    String summarySql =
                            "UPDATE BRRS_M_BOP_SUMMARYTABLE " +
                            "SET " + columnName + " = ? " +
                            "WHERE REPORT_DATE = ?";

                    jdbcTemplate.update(
                            summarySql,
                            value,
                            updatedEntity.getReport_date()
                    );

                    // ===========================
                    // UPDATE DETAIL TABLE
                    // ===========================

                    String detailSql =
                            "UPDATE BRRS_M_BOP_DETAILTABLE " +
                            "SET " + columnName + " = ? " +
                            "WHERE REPORT_DATE = ?";

                    jdbcTemplate.update(
                            detailSql,
                            value,
                            updatedEntity.getReport_date()
                    );

                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
        }

        // Audit only if changes found
        String changes =
                auditService.getChanges(
                        oldcopy,
                        existingSummary);

        if (!changes.isEmpty()) {

            auditService.compareEntitiesmanual(
                    oldcopy,
                    existingSummary,
                    updatedEntity.getReport_date().toString(),
                    "M BOP Summary Screen",
                    "BRRS_M_BOP_SUMMARY"
            );
        }

        System.out.println(
                "M_BOP Summary & Detail Update Completed");

    } catch (Exception e) {

        throw new RuntimeException(
                "Error while updating M_BOP fields", e);
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

public List<Object[]> getM_BOPResub() {

    List<Object[]> resubList = new ArrayList<>();

    try {

        List<M_BOP_Archival_Summary_Entity> repoData =
                getarchivaldatabydateListWithVersion();

        if (repoData != null && !repoData.isEmpty()) {

            for (M_BOP_Archival_Summary_Entity entity : repoData) {

                Object[] row = new Object[] {
                        entity.getReport_date(),
                        entity.getReport_version(),
                        entity.getReportResubDate()
                };

                resubList.add(row);
            }

            System.out.println("Fetched " + resubList.size() + " resub records");

            M_BOP_Archival_Summary_Entity first = repoData.get(0);

            System.out.println("Latest resub version : "
                    + first.getReport_version());

        } else {

            System.out.println("No resub data found.");
        }

    } catch (Exception e) {

        System.err.println("Error fetching M_BOP Resub data : "
                + e.getMessage());

        e.printStackTrace();
    }

    return resubList;
}

//=====================================================
// UPDATE RESUB 
//=====================================================


//@Transactional
//public void updateResubReport(
//        M_BOP_RESUB_Summary_Entity updatedEntity) {
//
//    System.out.println("Came to M_BOP Resub Update");
//
//    Date reportDate = updatedEntity.getReport_date();
//
//    // ====================================================
//    // GET MAX VERSION
//    // ====================================================
//
//    BigDecimal maxVersion = findMaxVersion(reportDate);
//
//    if (maxVersion == null) {
//        throw new RuntimeException(
//                "No record found for REPORT_DATE : "
//                        + reportDate);
//    }
//
//    BigDecimal newVersion =
//            maxVersion.add(BigDecimal.ONE);
//
//    Date now = new Date();
//
//    try {
//
//        // ====================================================
//        // RESUB SUMMARY
//        // ====================================================
//
//        M_BOP_RESUB_Summary_Entity resubSummary =
//                new M_BOP_RESUB_Summary_Entity();
//
//        BeanUtils.copyProperties(
//                updatedEntity,
//                resubSummary);
//
//        resubSummary.setReport_date(reportDate);
//        resubSummary.setReport_version(newVersion);
//        resubSummary.setReportResubDate(now);
//
//        // ====================================================
//        // RESUB DETAIL
//        // ====================================================
//
//        M_BOP_RESUB_Detail_Entity resubDetail =
//                new M_BOP_RESUB_Detail_Entity();
//
//        BeanUtils.copyProperties(
//                updatedEntity,
//                resubDetail);
//
//        resubDetail.setReport_date(reportDate);
//        resubDetail.setReport_version(newVersion);
//        resubDetail.setReportResubDate(now);
//
//        // ====================================================
//        // ARCHIVAL SUMMARY
//        // ====================================================
//
//        M_BOP_Archival_Summary_Entity archivalSummary =
//                new M_BOP_Archival_Summary_Entity();
//
//        BeanUtils.copyProperties(
//                updatedEntity,
//                archivalSummary);
//
//        archivalSummary.setReport_date(reportDate);
//        archivalSummary.setReport_version(newVersion);
//        archivalSummary.setReportResubDate(now);
//
//        // ====================================================
//        // ARCHIVAL DETAIL
//        // ====================================================
//
//        M_BOP_Archival_Detail_Entity archivalDetail =
//                new M_BOP_Archival_Detail_Entity();
//
//        BeanUtils.copyProperties(
//                updatedEntity,
//                archivalDetail);
//
//        archivalDetail.setReport_date(reportDate);
//        archivalDetail.setReport_version(newVersion);
//        archivalDetail.setReportResubDate(now);
//
//        // ====================================================
//        // INSERT INTO RESUB SUMMARY TABLE
//        // ====================================================
//
//        insertResubSummary(resubSummary);
//
//        // ====================================================
//        // INSERT INTO RESUB DETAIL TABLE
//        // ====================================================
//
//        insertResubDetail(resubDetail);
//
//        // ====================================================
//        // INSERT INTO ARCHIVAL SUMMARY TABLE
//        // ====================================================
//
//        insertArchivalSummary(archivalSummary);
//
//        // ====================================================
//        // INSERT INTO ARCHIVAL DETAIL TABLE
//        // ====================================================
//
//        insertArchivalDetail(archivalDetail);
//
//        System.out.println(
//                "M_BOP Resub Version Created Successfully : "
//                        + newVersion);
//
//    } catch (Exception e) {
//
//        e.printStackTrace();
//
//        throw new RuntimeException(
//                "Error while creating M_BOP Resub Version",
//                e);
//    }
//}

@Transactional
public void updateResubReport(
        M_BOP_RESUB_Summary_Entity updatedEntity) {

    System.out.println("Came to M_BOP Resub Update");

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

        M_BOP_RESUB_Summary_Entity resubSummary =
                new M_BOP_RESUB_Summary_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                resubSummary);

        resubSummary.setReport_date(reportDate);
        resubSummary.setReport_version(newVersion);
        resubSummary.setReportResubDate(now);

        // ====================================================
        // RESUB DETAIL
        // ====================================================

        M_BOP_RESUB_Detail_Entity resubDetail =
                new M_BOP_RESUB_Detail_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                resubDetail);

        resubDetail.setReport_date(reportDate);
        resubDetail.setReport_version(newVersion);
        resubDetail.setReportResubDate(now);

        // ====================================================
        // ARCHIVAL SUMMARY
        // ====================================================

        M_BOP_Archival_Summary_Entity archivalSummary =
                new M_BOP_Archival_Summary_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                archivalSummary);

        archivalSummary.setReport_date(reportDate);
        archivalSummary.setReport_version(newVersion);
        archivalSummary.setReportResubDate(now);

        // ====================================================
        // ARCHIVAL DETAIL
        // ====================================================

        M_BOP_Archival_Detail_Entity archivalDetail =
                new M_BOP_Archival_Detail_Entity();

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

        // ====================================================
        // AUDIT
        // ====================================================

        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs != null) {

            HttpServletRequest request = attrs.getRequest();

            String userid =
                    (String) request.getSession()
                            .getAttribute("USERID");

            auditService.createBusinessAudit(
                    userid,
                    "RESUBMIT",
                    "M BOP Resub Summary",
                    null,
                    "BRRS_M_BOP_RESUB_SUMMARYTABLE"
            );
        }

        System.out.println(
                "M_BOP Resub Version Created Successfully : "
                        + newVersion);

    } catch (Exception e) {

        e.printStackTrace();

        throw new RuntimeException(
                "Error while creating M_BOP Resub Version",
                e);
    }
}

private void insertResubSummary(M_BOP_RESUB_Summary_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_M_BOP_RESUB_SUMMARYTABLE (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

        for (int i = 13; i <= 36; i++) {

        	columns
            .append("r").append(i).append("_open_position,")
            .append("r").append(i).append("_cpdm_dt_inc,")
            .append("r").append(i).append("_cpdm_dt_dec,")
            .append("r").append(i).append("_net,")
            .append("r").append(i).append("_cpdm_dt_der,")
            .append("r").append(i).append("_cpdm_dt_dto,")
            .append("r").append(i).append("_cp,");

            for (int j = 1; j <= 7; j++) {
                values.append("?,");
            }

         
            params.add(getValue(entity, "getR" + i + "_open_position"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_inc"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_dec"));
            params.add(getValue(entity, "getR" + i + "_net"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_der"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_dto"));
            params.add(getValue(entity, "getR" + i + "_cp"));
        }

        columns.deleteCharAt(columns.length() - 1);
        values.deleteCharAt(values.length() - 1);

        columns.append(")");
        values.append(")");

        jdbcTemplate.update(
                columns.toString() + values.toString(),
                params.toArray());

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(
                "Error inserting M_BOP RESUB SUMMARY",
                e);
    }
}

private void insertResubDetail(M_BOP_RESUB_Detail_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_M_BOP_RESUB_DETAILTABLE (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

        for (int i = 13; i <= 36; i++) {

        	columns
            .append("r").append(i).append("_open_position,")
            .append("r").append(i).append("_cpdm_dt_inc,")
            .append("r").append(i).append("_cpdm_dt_dec,")
            .append("r").append(i).append("_net,")
            .append("r").append(i).append("_cpdm_dt_der,")
            .append("r").append(i).append("_cpdm_dt_dto,")
            .append("r").append(i).append("_cp,");

            for (int j = 1; j <= 7; j++) {
                values.append("?,");
            }

            
            params.add(getValue(entity, "getR" + i + "_open_position"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_inc"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_dec"));
            params.add(getValue(entity, "getR" + i + "_net"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_der"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_dto"));
            params.add(getValue(entity, "getR" + i + "_cp"));
        }

        columns.deleteCharAt(columns.length() - 1);
        values.deleteCharAt(values.length() - 1);

        columns.append(")");
        values.append(")");

        jdbcTemplate.update(
                columns.toString() + values.toString(),
                params.toArray());

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(
                "Error inserting M_BOP RESUB DETAIL",
                e);
    }
}

private void insertArchivalSummary(M_BOP_Archival_Summary_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_M_BOP_ARCHIVALTABLE_SUMMARY (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

        for (int i = 13; i <= 36; i++) {

        	columns
            .append("r").append(i).append("_open_position,")
            .append("r").append(i).append("_cpdm_dt_inc,")
            .append("r").append(i).append("_cpdm_dt_dec,")
            .append("r").append(i).append("_net,")
            .append("r").append(i).append("_cpdm_dt_der,")
            .append("r").append(i).append("_cpdm_dt_dto,")
            .append("r").append(i).append("_cp,");

            for (int j = 1; j <= 7; j++) {
                values.append("?,");
            }

            
            params.add(getValue(entity, "getR" + i + "_open_position"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_inc"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_dec"));
            params.add(getValue(entity, "getR" + i + "_net"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_der"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_dto"));
            params.add(getValue(entity, "getR" + i + "_cp"));
        }

        columns.deleteCharAt(columns.length() - 1);
        values.deleteCharAt(values.length() - 1);

        columns.append(")");
        values.append(")");

        jdbcTemplate.update(
                columns.toString() + values.toString(),
                params.toArray());

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(
                "Error inserting M_BOP ARCHIVAL SUMMARY",
                e);
    }
}

private void insertArchivalDetail(M_BOP_Archival_Detail_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_M_BOP_ARCHIVALTABLE_DETAIL (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

        for (int i = 13; i <= 36; i++) {

        	columns
            .append("r").append(i).append("_open_position,")
            .append("r").append(i).append("_cpdm_dt_inc,")
            .append("r").append(i).append("_cpdm_dt_dec,")
            .append("r").append(i).append("_net,")
            .append("r").append(i).append("_cpdm_dt_der,")
            .append("r").append(i).append("_cpdm_dt_dto,")
            .append("r").append(i).append("_cp,");
            for (int j = 1; j <= 7; j++) {
                values.append("?,");
            }

           
            params.add(getValue(entity, "getR" + i + "_open_position"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_inc"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_dec"));
            params.add(getValue(entity, "getR" + i + "_net"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_der"));
            params.add(getValue(entity, "getR" + i + "_cpdm_dt_dto"));
            params.add(getValue(entity, "getR" + i + "_cp"));
        }

        columns.deleteCharAt(columns.length() - 1);
        values.deleteCharAt(values.length() - 1);

        columns.append(")");
        values.append(")");

        jdbcTemplate.update(
                columns.toString() + values.toString(),
                params.toArray());

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(
                "Error inserting M_BOP ARCHIVAL DETAIL",
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
//=====================================================
// Summary EXCEL  FORMAT
//=====================================================

public byte[] getM_BOPExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
						return getExcelM_BOPARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_BOPResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else {

					if ("email".equalsIgnoreCase(format) && version == null) {
						logger.info("Got format as Email");
						logger.info("Service: Generating Email report for version {}", version);
						return BRRS_M_BOPEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} else {

						// Fetch data

						List<M_BOP_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

						if (dataList.isEmpty()) {
							logger.warn("Service: No data found for BRRS_M_BOP report. Returning empty result.");
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
									M_BOP_Summary_Entity record = dataList.get(i);
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
									row = sheet.getRow(12);

			//row13
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR13_open_position() != null) {
								cell2.setCellValue(record.getR13_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							//row13
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR13_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row13
							// Column D
							Cell cell4 = row.createCell(3);
							if (record.getR13_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row13
							// Column B
							Cell cell6 = row.createCell(5);
							if (record.getR13_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(6);
							if (record.getR13_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							
							//ROW 14
							row = sheet.getRow(13);

							cell2 = row.createCell(1);
							if (record.getR14_open_position() != null) {
								cell2.setCellValue(record.getR14_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row14
							// Column C
							cell3 = row.createCell(2);
							if (record.getR14_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row14
							// Column D
							cell4 = row.createCell(3);
							if (record.getR14_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row14
							// Column f
							cell6 = row.createCell(5);
							if (record.getR14_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row14
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR14_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							
							

							//ROW 16
							row = sheet.getRow(15);

							
							cell2 = row.createCell(1);
							if (record.getR16_open_position() != null) {
								cell2.setCellValue(record.getR16_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row16
							// Column C
							cell3 = row.createCell(2);
							if (record.getR16_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row16
							// Column D
							cell4 = row.createCell(3);
							if (record.getR16_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row16
							// Column f
							cell6 = row.createCell(5);
							if (record.getR16_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row16
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR16_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							

							//ROW 17
			row = sheet.getRow(16);

			cell2 = row.createCell(1);
			if (record.getR17_open_position() != null) {
			    cell2.setCellValue(record.getR17_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			// Column C
			cell3 = row.createCell(2);
			if (record.getR17_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			// Column D
			cell4 = row.createCell(3);
			if (record.getR17_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			// Column F
			cell6 = row.createCell(5);
			if (record.getR17_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			// Column G
			cell7 = row.createCell(6);
			if (record.getR17_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 18
			row = sheet.getRow(17);

			cell2 = row.createCell(1);
			if (record.getR18_open_position() != null) {
			    cell2.setCellValue(record.getR18_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR18_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR18_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR18_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR18_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 19
			row = sheet.getRow(18);

			cell2 = row.createCell(1);
			if (record.getR19_open_position() != null) {
			    cell2.setCellValue(record.getR19_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR19_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR19_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR19_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR19_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 20
			row = sheet.getRow(19);

			cell2 = row.createCell(1);
			if (record.getR20_open_position() != null) {
			    cell2.setCellValue(record.getR20_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR20_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR20_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR20_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR20_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 21
			row = sheet.getRow(20);

			cell2 = row.createCell(1);
			if (record.getR21_open_position() != null) {
			    cell2.setCellValue(record.getR21_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR21_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR21_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR21_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR21_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 22
			row = sheet.getRow(21);

			cell2 = row.createCell(1);
			if (record.getR22_open_position() != null) {
			    cell2.setCellValue(record.getR22_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR22_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR22_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR22_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR22_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 23
			row = sheet.getRow(22);

			cell2 = row.createCell(1);
			if (record.getR23_open_position() != null) {
			    cell2.setCellValue(record.getR23_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR23_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR23_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR23_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR23_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			

			//ROW 25
			row = sheet.getRow(24);

			cell2 = row.createCell(1);
			if (record.getR25_open_position() != null) {
			    cell2.setCellValue(record.getR25_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR25_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR25_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			Cell cell5 = row.createCell(4);
			if (record.getR25_net() != null) {
			    cell5.setCellValue(record.getR25_net().doubleValue());
			    cell5.setCellStyle(numberStyle);
			} else {
			    cell5.setCellValue("");
			    cell5.setCellStyle(textStyle);
			}

			cell6 = row.createCell(5);
			if (record.getR25_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR25_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			Cell cell8 = row.createCell(7);
			if (record.getR25_cp() != null) {
			    cell8.setCellValue(record.getR25_cp().doubleValue());
			    cell8.setCellStyle(numberStyle);
			} else {
			    cell8.setCellValue("");
			    cell8.setCellStyle(textStyle);
			}

			//ROW 26
			row = sheet.getRow(25);

			cell2 = row.createCell(1);
			if (record.getR26_open_position() != null) {
			    cell2.setCellValue(record.getR26_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR26_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR26_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR26_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR26_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			

			//ROW 28
			row = sheet.getRow(27);

			cell2 = row.createCell(1);
			if (record.getR28_open_position() != null) {
			    cell2.setCellValue(record.getR28_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR28_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR28_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR28_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR28_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 29
			row = sheet.getRow(28);

			cell2 = row.createCell(1);
			if (record.getR29_open_position() != null) {
			    cell2.setCellValue(record.getR29_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR29_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR29_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			
			cell6 = row.createCell(5);
			if (record.getR29_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR29_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 30
			row = sheet.getRow(29);

			cell2 = row.createCell(1);
			if (record.getR30_open_position() != null) {
			    cell2.setCellValue(record.getR30_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR30_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR30_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			
			cell6 = row.createCell(5);
			if (record.getR30_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR30_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 31
			row = sheet.getRow(30);

			cell2 = row.createCell(1);
			if (record.getR31_open_position() != null) {
			    cell2.setCellValue(record.getR31_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR31_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR31_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR31_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR31_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 32
			row = sheet.getRow(31);

			cell2 = row.createCell(1);
			if (record.getR32_open_position() != null) {
			    cell2.setCellValue(record.getR32_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR32_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR32_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR32_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR32_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 33
			row = sheet.getRow(32);

			cell2 = row.createCell(1);
			if (record.getR33_open_position() != null) {
			    cell2.setCellValue(record.getR33_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR33_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR33_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR33_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR33_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 34
			row = sheet.getRow(33);

			cell2 = row.createCell(1);
			if (record.getR34_open_position() != null) {
			    cell2.setCellValue(record.getR34_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR34_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR34_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR34_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR34_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 35
			row = sheet.getRow(34);

			cell2 = row.createCell(1);
			if (record.getR35_open_position() != null) {
			    cell2.setCellValue(record.getR35_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR35_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR35_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR35_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR35_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
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
								auditService.createBusinessAudit(userid, "DOWNLOAD", "M_BOP SUMMARY", null, "BRRS_M_BOP_SUMMARYTABLE");
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
			public byte[] BRRS_M_BOPEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Email Excel generation process in memory.");
				
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_M_BOPARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_BOPEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 
				else {
				List<M_BOP_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_BOP report. Returning empty result.");
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
							M_BOP_Summary_Entity record = dataList.get(i);
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
							row = sheet.getRow(12);

			//row13
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR13_open_position() != null) {
								cell2.setCellValue(record.getR13_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							//row13
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR13_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row13
							// Column D
							Cell cell4 = row.createCell(3);
							if (record.getR13_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row13
							// Column F
							Cell cell6 = row.createCell(5);
							if (record.getR13_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
		                   // Column G
							Cell cell7 = row.createCell(6);
							if (record.getR13_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							
							//ROW 14
							row = sheet.getRow(13);
		                     // Column B
							cell2 = row.createCell(1);
							if (record.getR14_open_position() != null) {
								cell2.setCellValue(record.getR14_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row14
							// Column C
							cell3 = row.createCell(2);
							if (record.getR14_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row14
							// Column D
							cell4 = row.createCell(3);
							if (record.getR14_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row14
							// Column f
							cell6 = row.createCell(5);
							if (record.getR14_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row14
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR14_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							
							

							

		// ROW 15
		row = sheet.getRow(14);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR15_open_position() != null) {
		    cell2.setCellValue(record.getR15_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR15_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR15_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR15_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR15_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR15_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR15_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR15_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR15_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 16
		row = sheet.getRow(15);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR16_open_position() != null) {
		    cell2.setCellValue(record.getR16_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR16_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR16_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR16_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR16_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 17
		row = sheet.getRow(16);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR17_open_position() != null) {
		    cell2.setCellValue(record.getR17_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR17_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR17_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR17_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR17_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 18
		row = sheet.getRow(17);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR18_open_position() != null) {
		    cell2.setCellValue(record.getR18_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR18_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR18_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR18_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR18_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 19
		row = sheet.getRow(18);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR19_open_position() != null) {
		    cell2.setCellValue(record.getR19_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR19_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR19_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR19_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR19_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 20
		row = sheet.getRow(19);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR20_open_position() != null) {
		    cell2.setCellValue(record.getR20_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR20_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR20_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR20_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR20_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 21
		row = sheet.getRow(20);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR21_open_position() != null) {
		    cell2.setCellValue(record.getR21_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR21_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR21_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR21_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR21_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 22
		row = sheet.getRow(21);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR22_open_position() != null) {
		    cell2.setCellValue(record.getR22_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR22_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR22_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR22_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR22_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 23
		row = sheet.getRow(22);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR23_open_position() != null) {
		    cell2.setCellValue(record.getR23_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR23_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR23_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR23_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR23_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 24
		row = sheet.getRow(23);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR24_open_position() != null) {
		    cell2.setCellValue(record.getR24_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR24_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR24_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR24_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR24_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR24_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR24_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR24_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR24_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 25
		row = sheet.getRow(24);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR25_open_position() != null) {
		    cell2.setCellValue(record.getR25_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR25_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR25_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		
		Cell cell5 = row.createCell(4);
		if (record.getR25_net() != null) {
		    cell5.setCellValue(record.getR25_net().doubleValue());
		    cell5.setCellStyle(numberStyle);
		} else {
		    cell5.setCellValue("");
		    cell5.setCellStyle(textStyle);
		}
		
		// Column F
		cell6 = row.createCell(5);
		if (record.getR25_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR25_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}
		
		Cell cell8 = row.createCell(7);
		if (record.getR25_cp() != null) {
		    cell8.setCellValue(record.getR25_cp().doubleValue());
		    cell8.setCellStyle(numberStyle);
		} else {
		    cell8.setCellValue("");
		    cell8.setCellStyle(textStyle);
		}

		// ROW 26
		row = sheet.getRow(25);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR26_open_position() != null) {
		    cell2.setCellValue(record.getR26_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR26_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR26_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR26_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR26_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}
		

		// ROW 27
		row = sheet.getRow(26);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR27_open_position() != null) {
		    cell2.setCellValue(record.getR27_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR27_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR27_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR27_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR27_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR27_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR27_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR27_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR27_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 28
		row = sheet.getRow(27);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR28_open_position() != null) {
		    cell2.setCellValue(record.getR28_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR28_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR28_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR28_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR28_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 29
		row = sheet.getRow(28);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR29_open_position() != null) {
		    cell2.setCellValue(record.getR29_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR29_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR29_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR29_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR29_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 30
		row = sheet.getRow(29);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR30_open_position() != null) {
		    cell2.setCellValue(record.getR30_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR30_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR30_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR30_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR30_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 31
		row = sheet.getRow(30);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR31_open_position() != null) {
		    cell2.setCellValue(record.getR31_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR31_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR31_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR31_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR31_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 32
		row = sheet.getRow(31);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR32_open_position() != null) {
		    cell2.setCellValue(record.getR32_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR32_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR32_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR32_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR32_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 33
		row = sheet.getRow(32);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR33_open_position() != null) {
		    cell2.setCellValue(record.getR33_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR33_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR33_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR33_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR33_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 34
		row = sheet.getRow(33);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR34_open_position() != null) {
		    cell2.setCellValue(record.getR34_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR34_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR34_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR34_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR34_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 35
		row = sheet.getRow(34);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR35_open_position() != null) {
		    cell2.setCellValue(record.getR35_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR35_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR35_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR35_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR35_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 36
		row = sheet.getRow(35);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR36_open_position() != null) {
		    cell2.setCellValue(record.getR36_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR36_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR36_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR36_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR36_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR36_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR36_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR36_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR36_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_BOP EMAIL SUMMARY", null, "BRRS_M_BOP_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
				}
			}
			


//=====================================================
//ARCHIVAL SUMMARY EXCEL  FORMAT
//=====================================================

// Archival format excel
			public byte[] getExcelM_BOPARCHIVAL(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory in Archival.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_M_BOPARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 

				List<M_BOP_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_BOP report. Returning empty result.");
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
							M_BOP_Archival_Summary_Entity record = dataList.get(i);
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
							row = sheet.getRow(12);

		//row13
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR13_open_position() != null) {
								cell2.setCellValue(record.getR13_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							//row13
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR13_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row13
							// Column D
							Cell cell4 = row.createCell(3);
							if (record.getR13_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row13
							// Column B
							Cell cell6 = row.createCell(5);
							if (record.getR13_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(6);
							if (record.getR13_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							
							//ROW 14
							row = sheet.getRow(13);

							cell2 = row.createCell(1);
							if (record.getR14_open_position() != null) {
								cell2.setCellValue(record.getR14_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row14
							// Column C
							cell3 = row.createCell(2);
							if (record.getR14_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row14
							// Column D
							cell4 = row.createCell(3);
							if (record.getR14_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row14
							// Column f
							cell6 = row.createCell(5);
							if (record.getR14_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row14
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR14_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							
							

							//ROW 16
							row = sheet.getRow(15);

							
							cell2 = row.createCell(1);
							if (record.getR16_open_position() != null) {
								cell2.setCellValue(record.getR16_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row16
							// Column C
							cell3 = row.createCell(2);
							if (record.getR16_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row16
							// Column D
							cell4 = row.createCell(3);
							if (record.getR16_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row16
							// Column f
							cell6 = row.createCell(5);
							if (record.getR16_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row16
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR16_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							

							//ROW 17
			row = sheet.getRow(16);

			cell2 = row.createCell(1);
			if (record.getR17_open_position() != null) {
			    cell2.setCellValue(record.getR17_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			// Column C
			cell3 = row.createCell(2);
			if (record.getR17_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			// Column D
			cell4 = row.createCell(3);
			if (record.getR17_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			// Column F
			cell6 = row.createCell(5);
			if (record.getR17_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			// Column G
			cell7 = row.createCell(6);
			if (record.getR17_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 18
			row = sheet.getRow(17);

			cell2 = row.createCell(1);
			if (record.getR18_open_position() != null) {
			    cell2.setCellValue(record.getR18_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR18_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR18_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR18_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR18_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 19
			row = sheet.getRow(18);

			cell2 = row.createCell(1);
			if (record.getR19_open_position() != null) {
			    cell2.setCellValue(record.getR19_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR19_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR19_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR19_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR19_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 20
			row = sheet.getRow(19);

			cell2 = row.createCell(1);
			if (record.getR20_open_position() != null) {
			    cell2.setCellValue(record.getR20_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR20_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR20_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR20_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR20_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 21
			row = sheet.getRow(20);

			cell2 = row.createCell(1);
			if (record.getR21_open_position() != null) {
			    cell2.setCellValue(record.getR21_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR21_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR21_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR21_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR21_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 22
			row = sheet.getRow(21);

			cell2 = row.createCell(1);
			if (record.getR22_open_position() != null) {
			    cell2.setCellValue(record.getR22_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR22_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR22_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR22_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR22_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 23
			row = sheet.getRow(22);

			cell2 = row.createCell(1);
			if (record.getR23_open_position() != null) {
			    cell2.setCellValue(record.getR23_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR23_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR23_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR23_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR23_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			

			//ROW 25
			row = sheet.getRow(24);

			cell2 = row.createCell(1);
			if (record.getR25_open_position() != null) {
			    cell2.setCellValue(record.getR25_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR25_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR25_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			Cell cell5 = row.createCell(4);
			if (record.getR25_net() != null) {
			    cell5.setCellValue(record.getR25_net().doubleValue());
			    cell5.setCellStyle(numberStyle);
			} else {
			    cell5.setCellValue("");
			    cell5.setCellStyle(textStyle);
			}

			cell6 = row.createCell(5);
			if (record.getR25_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR25_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			Cell cell8 = row.createCell(7);
			if (record.getR25_cp() != null) {
			    cell8.setCellValue(record.getR25_cp().doubleValue());
			    cell8.setCellStyle(numberStyle);
			} else {
			    cell8.setCellValue("");
			    cell8.setCellStyle(textStyle);
			}

			//ROW 26
			row = sheet.getRow(25);

			cell2 = row.createCell(1);
			if (record.getR26_open_position() != null) {
			    cell2.setCellValue(record.getR26_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR26_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR26_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR26_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR26_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			

			//ROW 28
			row = sheet.getRow(27);

			cell2 = row.createCell(1);
			if (record.getR28_open_position() != null) {
			    cell2.setCellValue(record.getR28_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR28_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR28_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR28_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR28_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 29
			row = sheet.getRow(28);

			cell2 = row.createCell(1);
			if (record.getR29_open_position() != null) {
			    cell2.setCellValue(record.getR29_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR29_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR29_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			
			cell6 = row.createCell(5);
			if (record.getR29_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR29_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 30
			row = sheet.getRow(29);

			cell2 = row.createCell(1);
			if (record.getR30_open_position() != null) {
			    cell2.setCellValue(record.getR30_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR30_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR30_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			
			cell6 = row.createCell(5);
			if (record.getR30_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR30_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 31
			row = sheet.getRow(30);

			cell2 = row.createCell(1);
			if (record.getR31_open_position() != null) {
			    cell2.setCellValue(record.getR31_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR31_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR31_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR31_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR31_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 32
			row = sheet.getRow(31);

			cell2 = row.createCell(1);
			if (record.getR32_open_position() != null) {
			    cell2.setCellValue(record.getR32_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR32_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR32_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR32_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR32_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 33
			row = sheet.getRow(32);

			cell2 = row.createCell(1);
			if (record.getR33_open_position() != null) {
			    cell2.setCellValue(record.getR33_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR33_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR33_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR33_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR33_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 34
			row = sheet.getRow(33);

			cell2 = row.createCell(1);
			if (record.getR34_open_position() != null) {
			    cell2.setCellValue(record.getR34_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR34_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR34_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR34_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR34_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 35
			row = sheet.getRow(34);

			cell2 = row.createCell(1);
			if (record.getR35_open_position() != null) {
			    cell2.setCellValue(record.getR35_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR35_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR35_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR35_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR35_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_BOP ARCHIVAL SUMMARY", null, "BRRS_M_BOP_ARCHIVALTABLE_SUMMARY");
					}

					return out.toByteArray();
				}

			}


	
//=====================================================
//ARCHIVAL SUMMARY EXCEL  EMAIL
//=====================================================

// Archival Email Excel
			public byte[] BRRS_M_BOPARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Archival Email Excel generation process in memory.");

				List<M_BOP_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_BOP report. Returning empty result.");
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
							M_BOP_Archival_Summary_Entity record = dataList.get(i);
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
							row = sheet.getRow(12);

			//row13
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR13_open_position() != null) {
								cell2.setCellValue(record.getR13_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							//row13
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR13_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row13
							// Column D
							Cell cell4 = row.createCell(3);
							if (record.getR13_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row13
							// Column F
							Cell cell6 = row.createCell(5);
							if (record.getR13_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
		                   // Column G
							Cell cell7 = row.createCell(6);
							if (record.getR13_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							
							//ROW 14
							row = sheet.getRow(13);
		                     // Column B
							cell2 = row.createCell(1);
							if (record.getR14_open_position() != null) {
								cell2.setCellValue(record.getR14_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row14
							// Column C
							cell3 = row.createCell(2);
							if (record.getR14_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row14
							// Column D
							cell4 = row.createCell(3);
							if (record.getR14_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row14
							// Column f
							cell6 = row.createCell(5);
							if (record.getR14_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row14
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR14_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							
							

							

		// ROW 15
		row = sheet.getRow(14);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR15_open_position() != null) {
		    cell2.setCellValue(record.getR15_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR15_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR15_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR15_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR15_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR15_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR15_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR15_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR15_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 16
		row = sheet.getRow(15);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR16_open_position() != null) {
		    cell2.setCellValue(record.getR16_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR16_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR16_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR16_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR16_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 17
		row = sheet.getRow(16);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR17_open_position() != null) {
		    cell2.setCellValue(record.getR17_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR17_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR17_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR17_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR17_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 18
		row = sheet.getRow(17);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR18_open_position() != null) {
		    cell2.setCellValue(record.getR18_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR18_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR18_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR18_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR18_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 19
		row = sheet.getRow(18);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR19_open_position() != null) {
		    cell2.setCellValue(record.getR19_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR19_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR19_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR19_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR19_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 20
		row = sheet.getRow(19);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR20_open_position() != null) {
		    cell2.setCellValue(record.getR20_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR20_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR20_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR20_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR20_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 21
		row = sheet.getRow(20);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR21_open_position() != null) {
		    cell2.setCellValue(record.getR21_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR21_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR21_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR21_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR21_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 22
		row = sheet.getRow(21);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR22_open_position() != null) {
		    cell2.setCellValue(record.getR22_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR22_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR22_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR22_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR22_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 23
		row = sheet.getRow(22);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR23_open_position() != null) {
		    cell2.setCellValue(record.getR23_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR23_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR23_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR23_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR23_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 24
		row = sheet.getRow(23);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR24_open_position() != null) {
		    cell2.setCellValue(record.getR24_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR24_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR24_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR24_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR24_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR24_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR24_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR24_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR24_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 25
		row = sheet.getRow(24);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR25_open_position() != null) {
		    cell2.setCellValue(record.getR25_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR25_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR25_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		
		Cell cell5 = row.createCell(4);
		if (record.getR25_net() != null) {
		    cell5.setCellValue(record.getR25_net().doubleValue());
		    cell5.setCellStyle(numberStyle);
		} else {
		    cell5.setCellValue("");
		    cell5.setCellStyle(textStyle);
		}
		
		// Column F
		cell6 = row.createCell(5);
		if (record.getR25_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR25_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}
		
		Cell cell8 = row.createCell(7);
		if (record.getR25_cp() != null) {
		    cell8.setCellValue(record.getR25_cp().doubleValue());
		    cell8.setCellStyle(numberStyle);
		} else {
		    cell8.setCellValue("");
		    cell8.setCellStyle(textStyle);
		}

		// ROW 26
		row = sheet.getRow(25);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR26_open_position() != null) {
		    cell2.setCellValue(record.getR26_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR26_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR26_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR26_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR26_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 27
		row = sheet.getRow(26);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR27_open_position() != null) {
		    cell2.setCellValue(record.getR27_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR27_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR27_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR27_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR27_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR27_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR27_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR27_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR27_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 28
		row = sheet.getRow(27);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR28_open_position() != null) {
		    cell2.setCellValue(record.getR28_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR28_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR28_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR28_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR28_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 29
		row = sheet.getRow(28);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR29_open_position() != null) {
		    cell2.setCellValue(record.getR29_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR29_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR29_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR29_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR29_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 30
		row = sheet.getRow(29);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR30_open_position() != null) {
		    cell2.setCellValue(record.getR30_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR30_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR30_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR30_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR30_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 31
		row = sheet.getRow(30);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR31_open_position() != null) {
		    cell2.setCellValue(record.getR31_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR31_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR31_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR31_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR31_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 32
		row = sheet.getRow(31);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR32_open_position() != null) {
		    cell2.setCellValue(record.getR32_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR32_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR32_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR32_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR32_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 33
		row = sheet.getRow(32);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR33_open_position() != null) {
		    cell2.setCellValue(record.getR33_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR33_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR33_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR33_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR33_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 34
		row = sheet.getRow(33);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR34_open_position() != null) {
		    cell2.setCellValue(record.getR34_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR34_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR34_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR34_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR34_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 35
		row = sheet.getRow(34);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR35_open_position() != null) {
		    cell2.setCellValue(record.getR35_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR35_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR35_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR35_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR35_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 36
		row = sheet.getRow(35);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR36_open_position() != null) {
		    cell2.setCellValue(record.getR36_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR36_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR36_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR36_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR36_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR36_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR36_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR36_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR36_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_BOP EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_BOP_ARCHIVALTABLE_SUMMARY");
					}

					return out.toByteArray();
				}
			}
	
//=====================================================
// RESUB EXCEL  FORMAT
//=====================================================

		// Resub Format excel
			public byte[] BRRS_M_BOPResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_BOPEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			}

				List<M_BOP_RESUB_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_BOP report. Returning empty result.");
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

							M_BOP_RESUB_Summary_Entity record = dataList.get(i);
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
							
							
							
							//ROW 13
							row = sheet.getRow(12);

							//row13
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR13_open_position() != null) {
								cell2.setCellValue(record.getR13_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							//row13
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR13_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row13
							// Column D
							Cell cell4 = row.createCell(3);
							if (record.getR13_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row13
							// Column B
							Cell cell6 = row.createCell(5);
							if (record.getR13_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(6);
							if (record.getR13_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							
							//ROW 14
							row = sheet.getRow(13);

							cell2 = row.createCell(1);
							if (record.getR14_open_position() != null) {
								cell2.setCellValue(record.getR14_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row14
							// Column C
							cell3 = row.createCell(2);
							if (record.getR14_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row14
							// Column D
							cell4 = row.createCell(3);
							if (record.getR14_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row14
							// Column f
							cell6 = row.createCell(5);
							if (record.getR14_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row14
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR14_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							
							

							//ROW 16
							row = sheet.getRow(15);

							
							cell2 = row.createCell(1);
							if (record.getR16_open_position() != null) {
								cell2.setCellValue(record.getR16_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row16
							// Column C
							cell3 = row.createCell(2);
							if (record.getR16_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row16
							// Column D
							cell4 = row.createCell(3);
							if (record.getR16_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row16
							// Column f
							cell6 = row.createCell(5);
							if (record.getR16_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row16
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR16_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							

							//ROW 17
			row = sheet.getRow(16);

			cell2 = row.createCell(1);
			if (record.getR17_open_position() != null) {
			    cell2.setCellValue(record.getR17_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			// Column C
			cell3 = row.createCell(2);
			if (record.getR17_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			// Column D
			cell4 = row.createCell(3);
			if (record.getR17_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			// Column F
			cell6 = row.createCell(5);
			if (record.getR17_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			// Column G
			cell7 = row.createCell(6);
			if (record.getR17_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 18
			row = sheet.getRow(17);

			cell2 = row.createCell(1);
			if (record.getR18_open_position() != null) {
			    cell2.setCellValue(record.getR18_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR18_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR18_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR18_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR18_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 19
			row = sheet.getRow(18);

			cell2 = row.createCell(1);
			if (record.getR19_open_position() != null) {
			    cell2.setCellValue(record.getR19_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR19_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR19_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR19_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR19_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 20
			row = sheet.getRow(19);

			cell2 = row.createCell(1);
			if (record.getR20_open_position() != null) {
			    cell2.setCellValue(record.getR20_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR20_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR20_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR20_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR20_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 21
			row = sheet.getRow(20);

			cell2 = row.createCell(1);
			if (record.getR21_open_position() != null) {
			    cell2.setCellValue(record.getR21_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR21_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR21_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR21_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR21_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 22
			row = sheet.getRow(21);

			cell2 = row.createCell(1);
			if (record.getR22_open_position() != null) {
			    cell2.setCellValue(record.getR22_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR22_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR22_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR22_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR22_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 23
			row = sheet.getRow(22);

			cell2 = row.createCell(1);
			if (record.getR23_open_position() != null) {
			    cell2.setCellValue(record.getR23_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR23_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR23_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR23_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR23_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			

			//ROW 25
			row = sheet.getRow(24);

			cell2 = row.createCell(1);
			if (record.getR25_open_position() != null) {
			    cell2.setCellValue(record.getR25_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR25_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR25_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			Cell cell5 = row.createCell(4);
			if (record.getR25_net() != null) {
			    cell5.setCellValue(record.getR25_net().doubleValue());
			    cell5.setCellStyle(numberStyle);
			} else {
			    cell5.setCellValue("");
			    cell5.setCellStyle(textStyle);
			}

			cell6 = row.createCell(5);
			if (record.getR25_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR25_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			Cell cell8 = row.createCell(7);
			if (record.getR25_cp() != null) {
			    cell8.setCellValue(record.getR25_cp().doubleValue());
			    cell8.setCellStyle(numberStyle);
			} else {
			    cell8.setCellValue("");
			    cell8.setCellStyle(textStyle);
			}

			//ROW 26
			row = sheet.getRow(25);

			cell2 = row.createCell(1);
			if (record.getR26_open_position() != null) {
			    cell2.setCellValue(record.getR26_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR26_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR26_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR26_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR26_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			

			//ROW 28
			row = sheet.getRow(27);

			cell2 = row.createCell(1);
			if (record.getR28_open_position() != null) {
			    cell2.setCellValue(record.getR28_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR28_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR28_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR28_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR28_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 29
			row = sheet.getRow(28);

			cell2 = row.createCell(1);
			if (record.getR29_open_position() != null) {
			    cell2.setCellValue(record.getR29_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR29_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR29_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			
			cell6 = row.createCell(5);
			if (record.getR29_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR29_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 30
			row = sheet.getRow(29);

			cell2 = row.createCell(1);
			if (record.getR30_open_position() != null) {
			    cell2.setCellValue(record.getR30_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR30_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR30_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			
			cell6 = row.createCell(5);
			if (record.getR30_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR30_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 31
			row = sheet.getRow(30);

			cell2 = row.createCell(1);
			if (record.getR31_open_position() != null) {
			    cell2.setCellValue(record.getR31_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR31_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR31_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR31_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR31_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 32
			row = sheet.getRow(31);

			cell2 = row.createCell(1);
			if (record.getR32_open_position() != null) {
			    cell2.setCellValue(record.getR32_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR32_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR32_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR32_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR32_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 33
			row = sheet.getRow(32);

			cell2 = row.createCell(1);
			if (record.getR33_open_position() != null) {
			    cell2.setCellValue(record.getR33_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR33_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR33_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR33_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR33_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 34
			row = sheet.getRow(33);

			cell2 = row.createCell(1);
			if (record.getR34_open_position() != null) {
			    cell2.setCellValue(record.getR34_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR34_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR34_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR34_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR34_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 35
			row = sheet.getRow(34);

			cell2 = row.createCell(1);
			if (record.getR35_open_position() != null) {
			    cell2.setCellValue(record.getR35_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR35_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR35_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR35_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR35_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_BOP RESUB SUMMARY", null, "BRRS_M_BOP_RESUB_SUMMARYTABLE");
					}

					return out.toByteArray();
				}

			}

	
//=====================================================
// RESUB  EXCEL EMAIL
//=====================================================

	
			// Resub Email Excel
			public byte[] BRRS_M_BOPEmailResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting RESUB Email Excel generation process in memory.");

				List<M_BOP_RESUB_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_BOP report. Returning empty result.");
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
							M_BOP_RESUB_Summary_Entity record = dataList.get(i);
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
							row = sheet.getRow(12);

			//row13
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR13_open_position() != null) {
								cell2.setCellValue(record.getR13_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							//row13
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR13_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row13
							// Column D
							Cell cell4 = row.createCell(3);
							if (record.getR13_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row13
							// Column F
							Cell cell6 = row.createCell(5);
							if (record.getR13_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
		                   // Column G
							Cell cell7 = row.createCell(6);
							if (record.getR13_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							
							//ROW 14
							row = sheet.getRow(13);
		                     // Column B
							cell2 = row.createCell(1);
							if (record.getR14_open_position() != null) {
								cell2.setCellValue(record.getR14_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row14
							// Column C
							cell3 = row.createCell(2);
							if (record.getR14_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row14
							// Column D
							cell4 = row.createCell(3);
							if (record.getR14_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row14
							// Column f
							cell6 = row.createCell(5);
							if (record.getR14_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row14
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR14_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							
							

							

		// ROW 15
		row = sheet.getRow(14);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR15_open_position() != null) {
		    cell2.setCellValue(record.getR15_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR15_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR15_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR15_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR15_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR15_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR15_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR15_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR15_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 16
		row = sheet.getRow(15);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR16_open_position() != null) {
		    cell2.setCellValue(record.getR16_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR16_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR16_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR16_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR16_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 17
		row = sheet.getRow(16);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR17_open_position() != null) {
		    cell2.setCellValue(record.getR17_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR17_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR17_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR17_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR17_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 18
		row = sheet.getRow(17);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR18_open_position() != null) {
		    cell2.setCellValue(record.getR18_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR18_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR18_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR18_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR18_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 19
		row = sheet.getRow(18);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR19_open_position() != null) {
		    cell2.setCellValue(record.getR19_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR19_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR19_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR19_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR19_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 20
		row = sheet.getRow(19);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR20_open_position() != null) {
		    cell2.setCellValue(record.getR20_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR20_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR20_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR20_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR20_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 21
		row = sheet.getRow(20);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR21_open_position() != null) {
		    cell2.setCellValue(record.getR21_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR21_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR21_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR21_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR21_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 22
		row = sheet.getRow(21);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR22_open_position() != null) {
		    cell2.setCellValue(record.getR22_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR22_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR22_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR22_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR22_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 23
		row = sheet.getRow(22);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR23_open_position() != null) {
		    cell2.setCellValue(record.getR23_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR23_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR23_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR23_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR23_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 24
		row = sheet.getRow(23);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR24_open_position() != null) {
		    cell2.setCellValue(record.getR24_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR24_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR24_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR24_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR24_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR24_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR24_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR24_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR24_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 25
		row = sheet.getRow(24);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR25_open_position() != null) {
		    cell2.setCellValue(record.getR25_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR25_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR25_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		
		Cell cell5 = row.createCell(4);
		if (record.getR25_net() != null) {
		    cell5.setCellValue(record.getR25_net().doubleValue());
		    cell5.setCellStyle(numberStyle);
		} else {
		    cell5.setCellValue("");
		    cell5.setCellStyle(textStyle);
		}
		
		// Column F
		cell6 = row.createCell(5);
		if (record.getR25_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR25_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}
		
		Cell cell8 = row.createCell(7);
		if (record.getR25_cp() != null) {
		    cell8.setCellValue(record.getR25_cp().doubleValue());
		    cell8.setCellStyle(numberStyle);
		} else {
		    cell8.setCellValue("");
		    cell8.setCellStyle(textStyle);
		}

		// ROW 26
		row = sheet.getRow(25);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR26_open_position() != null) {
		    cell2.setCellValue(record.getR26_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR26_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR26_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR26_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR26_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 27
		row = sheet.getRow(26);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR27_open_position() != null) {
		    cell2.setCellValue(record.getR27_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR27_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR27_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR27_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR27_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR27_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR27_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR27_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR27_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 28
		row = sheet.getRow(27);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR28_open_position() != null) {
		    cell2.setCellValue(record.getR28_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR28_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR28_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR28_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR28_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 29
		row = sheet.getRow(28);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR29_open_position() != null) {
		    cell2.setCellValue(record.getR29_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR29_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR29_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR29_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR29_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 30
		row = sheet.getRow(29);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR30_open_position() != null) {
		    cell2.setCellValue(record.getR30_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR30_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR30_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR30_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR30_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 31
		row = sheet.getRow(30);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR31_open_position() != null) {
		    cell2.setCellValue(record.getR31_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR31_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR31_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR31_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR31_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 32
		row = sheet.getRow(31);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR32_open_position() != null) {
		    cell2.setCellValue(record.getR32_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR32_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR32_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR32_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR32_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 33
		row = sheet.getRow(32);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR33_open_position() != null) {
		    cell2.setCellValue(record.getR33_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR33_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR33_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR33_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR33_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 34
		row = sheet.getRow(33);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR34_open_position() != null) {
		    cell2.setCellValue(record.getR34_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR34_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR34_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR34_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR34_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 35
		row = sheet.getRow(34);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR35_open_position() != null) {
		    cell2.setCellValue(record.getR35_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR35_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR35_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR35_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR35_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 36
		row = sheet.getRow(35);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR36_open_position() != null) {
		    cell2.setCellValue(record.getR36_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR36_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR36_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR36_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR36_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR36_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR36_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR36_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR36_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_BOP EMAIL RESUB SUMMARY", null, "BRRS_M_BOP_RESUB_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
			}

		
	}