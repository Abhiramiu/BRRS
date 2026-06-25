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

public class BRRS_M_GP_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_GP_ReportService.class);
	
	
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


	public List<M_GP_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_M_GP_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new M_GP_Summary_RowMapper()
    );
}
	
	public M_GP_Summary_Entity findByReportDate(Date reportDate) {

	    String sql =
	            "SELECT * FROM BRRS_M_GP_SUMMARYTABLE " +
	            "WHERE REPORT_DATE = ?";

	    return jdbcTemplate.queryForObject(
	            sql,
	            new Object[]{reportDate},
	            new M_GP_Summary_RowMapper()
	    );
	}
// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================



public List<Object[]> get_M_GP_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_GP_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<M_GP_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_M_GP_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new M_GP_Archival_Summary_RowMapper()
    );
}

public List<M_GP_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_M_GP_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new M_GP_Archival_Summary_RowMapper()
    );
}

public BigDecimal findMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_GP_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            BigDecimal.class
    );
}

// =====================================================
// DETAIL REPO
// =====================================================	


public List<M_GP_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_M_GP_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new M_GP_Detail_RowMapper()
    );
}



// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================


public List<Map<String, Object>> getM_GP_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_GP_ARCHIVALTABLE_DETAIL " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.queryForList(sql);
}


public List<M_GP_Archival_Detail_Entity> getDetaildatabydateListarchival(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * " +
                 "FROM BRRS_M_GP_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? " +
                 "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportDate, reportVersion },
            new M_GP_Archival_Detail_RowMapper()
    );
}

public BigDecimal findArchivalDetailMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) " +
                 "FROM BRRS_M_GP_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[] { reportDate },
            BigDecimal.class
    );
}

public M_GP_Archival_Detail_Entity getArchivalListWithVersion() {

    String sql = "SELECT * " +
                 "FROM BRRS_M_GP_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC " +
                 "FETCH FIRST 1 ROWS ONLY";

    return jdbcTemplate.queryForObject(
            sql,
            new M_GP_Archival_Detail_RowMapper()
    );
}

// =====================================================
// RESUB SUMMARY
// =====================================================

public List<M_GP_RESUB_Summary_Entity> getResubSummarydatabydateListarchival(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * " +
                 "FROM BRRS_M_GP_RESUB_SUMMARYTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new M_GP_RESUB_Summary_RowMapper()
    );
}


public BigDecimal findResubSummaryMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) " +
                 "FROM BRRS_M_GP_RESUB_SUMMARYTABLE " +
                 "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            BigDecimal.class
    );
}

public List<Map<String, Object>> getM_GP_Archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_GP_RESUB_SUMMARYTABLE " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.queryForList(sql);
}

public M_GP_RESUB_Summary_Entity getResubSummarydatabydateListWithVersion() {

    String sql = "SELECT * " +
                 "FROM BRRS_M_GP_RESUB_SUMMARYTABLE " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC " +
                 "FETCH FIRST 1 ROWS ONLY";

    return jdbcTemplate.queryForObject(
            sql,
            new M_GP_RESUB_Summary_RowMapper()
    );
}



// =====================================================
// RESUB DETAIL
// =====================================================


public List<Map<String, Object>> get_M_GP_Archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_GP_RESUB_DETAILTABLE " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.queryForList(sql);
}

public List<M_GP_RESUB_Detail_Entity> getResubDetaildatabydateList(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * " +
                 "FROM BRRS_M_GP_RESUB_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportDate, reportVersion },
            new M_GP_RESUB_Detail_RowMapper()
    );
}

public BigDecimal findResubDetailMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) " +
                 "FROM BRRS_M_GP_RESUB_DETAILTABLE " +
                 "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[] { reportDate },
            BigDecimal.class
    );
}

public M_GP_RESUB_Detail_Entity getdResubDetailDatabydateListWithVersion() {

    String sql = "SELECT * " +
                 "FROM BRRS_M_GP_RESUB_DETAILTABLE " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC " +
                 "FETCH FIRST 1 ROWS ONLY";

    return jdbcTemplate.queryForObject(
            sql,
            new M_GP_RESUB_Detail_RowMapper()
    );
}

// =====================================================
// SUMAMRY ENTITY & ROW MAPPER 
// =====================================================


public class M_GP_Summary_RowMapper implements RowMapper<M_GP_Summary_Entity> {

    @Override
    public M_GP_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_GP_Summary_Entity obj = new M_GP_Summary_Entity();

// =========================
// R11
// =========================
obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
obj.setR11_STAGE1_PROVISIONS(rs.getBigDecimal("R11_STAGE1_PROVISIONS"));
obj.setR11_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R11_QUALIFY_STAGE2_PROVISIONS"));
obj.setR11_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R11_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R12
// =========================
obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
obj.setR12_STAGE1_PROVISIONS(rs.getBigDecimal("R12_STAGE1_PROVISIONS"));
obj.setR12_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R12_QUALIFY_STAGE2_PROVISIONS"));
obj.setR12_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R12_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R13
// =========================
obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
obj.setR13_STAGE1_PROVISIONS(rs.getBigDecimal("R13_STAGE1_PROVISIONS"));
obj.setR13_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R13_QUALIFY_STAGE2_PROVISIONS"));
obj.setR13_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R13_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R14
// =========================
obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
obj.setR14_STAGE1_PROVISIONS(rs.getBigDecimal("R14_STAGE1_PROVISIONS"));
obj.setR14_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R14_QUALIFY_STAGE2_PROVISIONS"));
obj.setR14_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R14_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R15
// =========================
obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
obj.setR15_STAGE1_PROVISIONS(rs.getBigDecimal("R15_STAGE1_PROVISIONS"));
obj.setR15_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R15_QUALIFY_STAGE2_PROVISIONS"));
obj.setR15_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R15_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R16
// =========================
obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
obj.setR16_STAGE1_PROVISIONS(rs.getBigDecimal("R16_STAGE1_PROVISIONS"));
obj.setR16_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R16_QUALIFY_STAGE2_PROVISIONS"));
obj.setR16_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R16_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R17
// =========================
obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
obj.setR17_STAGE1_PROVISIONS(rs.getBigDecimal("R17_STAGE1_PROVISIONS"));
obj.setR17_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R17_QUALIFY_STAGE2_PROVISIONS"));
obj.setR17_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R17_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R18
// =========================
obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
obj.setR18_STAGE1_PROVISIONS(rs.getBigDecimal("R18_STAGE1_PROVISIONS"));
obj.setR18_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R18_QUALIFY_STAGE2_PROVISIONS"));
obj.setR18_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R18_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R19
// =========================
obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
obj.setR19_STAGE1_PROVISIONS(rs.getBigDecimal("R19_STAGE1_PROVISIONS"));
obj.setR19_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R19_QUALIFY_STAGE2_PROVISIONS"));
obj.setR19_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R19_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R20
// =========================
obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
obj.setR20_STAGE1_PROVISIONS(rs.getBigDecimal("R20_STAGE1_PROVISIONS"));
obj.setR20_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R20_QUALIFY_STAGE2_PROVISIONS"));
obj.setR20_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R20_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R21
// =========================
obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
obj.setR21_STAGE1_PROVISIONS(rs.getBigDecimal("R21_STAGE1_PROVISIONS"));
obj.setR21_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R21_QUALIFY_STAGE2_PROVISIONS"));
obj.setR21_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R21_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R22
// =========================
obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
obj.setR22_STAGE1_PROVISIONS(rs.getBigDecimal("R22_STAGE1_PROVISIONS"));
obj.setR22_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R22_QUALIFY_STAGE2_PROVISIONS"));
obj.setR22_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R22_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R23
// =========================
obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
obj.setR23_STAGE1_PROVISIONS(rs.getBigDecimal("R23_STAGE1_PROVISIONS"));
obj.setR23_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R23_QUALIFY_STAGE2_PROVISIONS"));
obj.setR23_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R23_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R24
// =========================
obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
obj.setR24_STAGE1_PROVISIONS(rs.getBigDecimal("R24_STAGE1_PROVISIONS"));
obj.setR24_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R24_QUALIFY_STAGE2_PROVISIONS"));
obj.setR24_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R24_TOTAL_GENERAL_PROVISIONS"));


// =========================
// R25
// =========================
obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
obj.setR25_STAGE1_PROVISIONS(rs.getBigDecimal("R25_STAGE1_PROVISIONS"));
obj.setR25_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R25_QUALIFY_STAGE2_PROVISIONS"));
obj.setR25_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R25_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R26
// =========================
obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
obj.setR26_STAGE1_PROVISIONS(rs.getBigDecimal("R26_STAGE1_PROVISIONS"));
obj.setR26_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R26_QUALIFY_STAGE2_PROVISIONS"));
obj.setR26_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R26_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R27
// =========================
obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
obj.setR27_STAGE1_PROVISIONS(rs.getBigDecimal("R27_STAGE1_PROVISIONS"));
obj.setR27_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R27_QUALIFY_STAGE2_PROVISIONS"));
obj.setR27_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R27_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R28
// =========================
obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
obj.setR28_STAGE1_PROVISIONS(rs.getBigDecimal("R28_STAGE1_PROVISIONS"));
obj.setR28_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R28_QUALIFY_STAGE2_PROVISIONS"));
obj.setR28_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R28_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R29
// =========================
obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
obj.setR29_STAGE1_PROVISIONS(rs.getBigDecimal("R29_STAGE1_PROVISIONS"));
obj.setR29_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R29_QUALIFY_STAGE2_PROVISIONS"));
obj.setR29_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R29_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R30
// =========================
obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
obj.setR30_STAGE1_PROVISIONS(rs.getBigDecimal("R30_STAGE1_PROVISIONS"));
obj.setR30_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R30_QUALIFY_STAGE2_PROVISIONS"));
obj.setR30_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R30_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R31
// =========================
obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
obj.setR31_STAGE1_PROVISIONS(rs.getBigDecimal("R31_STAGE1_PROVISIONS"));
obj.setR31_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R31_QUALIFY_STAGE2_PROVISIONS"));
obj.setR31_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R31_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R32
// =========================
obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
obj.setR32_STAGE1_PROVISIONS(rs.getBigDecimal("R32_STAGE1_PROVISIONS"));
obj.setR32_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R32_QUALIFY_STAGE2_PROVISIONS"));
obj.setR32_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R32_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R33
// =========================
obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
obj.setR33_STAGE1_PROVISIONS(rs.getBigDecimal("R33_STAGE1_PROVISIONS"));
obj.setR33_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R33_QUALIFY_STAGE2_PROVISIONS"));
obj.setR33_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R33_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R34
// =========================
obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
obj.setR34_STAGE1_PROVISIONS(rs.getBigDecimal("R34_STAGE1_PROVISIONS"));
obj.setR34_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R34_QUALIFY_STAGE2_PROVISIONS"));
obj.setR34_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R34_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R35
// =========================
obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
obj.setR35_STAGE1_PROVISIONS(rs.getBigDecimal("R35_STAGE1_PROVISIONS"));
obj.setR35_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R35_QUALIFY_STAGE2_PROVISIONS"));
obj.setR35_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R35_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R36
// =========================
obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
obj.setR36_STAGE1_PROVISIONS(rs.getBigDecimal("R36_STAGE1_PROVISIONS"));
obj.setR36_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R36_QUALIFY_STAGE2_PROVISIONS"));
obj.setR36_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R36_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R37
// =========================
obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
obj.setR37_STAGE1_PROVISIONS(rs.getBigDecimal("R37_STAGE1_PROVISIONS"));
obj.setR37_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R37_QUALIFY_STAGE2_PROVISIONS"));
obj.setR37_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R37_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R38
// =========================
obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
obj.setR38_STAGE1_PROVISIONS(rs.getBigDecimal("R38_STAGE1_PROVISIONS"));
obj.setR38_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R38_QUALIFY_STAGE2_PROVISIONS"));
obj.setR38_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R38_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R39
// =========================
obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
obj.setR39_STAGE1_PROVISIONS(rs.getBigDecimal("R39_STAGE1_PROVISIONS"));
obj.setR39_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R39_QUALIFY_STAGE2_PROVISIONS"));
obj.setR39_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R39_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R40
// =========================
obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
obj.setR40_STAGE1_PROVISIONS(rs.getBigDecimal("R40_STAGE1_PROVISIONS"));
obj.setR40_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R40_QUALIFY_STAGE2_PROVISIONS"));
obj.setR40_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R40_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R41
// =========================
obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
obj.setR41_STAGE1_PROVISIONS(rs.getBigDecimal("R41_STAGE1_PROVISIONS"));
obj.setR41_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R41_QUALIFY_STAGE2_PROVISIONS"));
obj.setR41_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R41_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R42
// =========================
obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
obj.setR42_STAGE1_PROVISIONS(rs.getBigDecimal("R42_STAGE1_PROVISIONS"));
obj.setR42_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R42_QUALIFY_STAGE2_PROVISIONS"));
obj.setR42_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R42_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R43
// =========================
obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
obj.setR43_STAGE1_PROVISIONS(rs.getBigDecimal("R43_STAGE1_PROVISIONS"));
obj.setR43_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R43_QUALIFY_STAGE2_PROVISIONS"));
obj.setR43_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R43_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R44
// =========================
obj.setR44_PRODUCT(rs.getString("R44_PRODUCT"));
obj.setR44_STAGE1_PROVISIONS(rs.getBigDecimal("R44_STAGE1_PROVISIONS"));
obj.setR44_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R44_QUALIFY_STAGE2_PROVISIONS"));
obj.setR44_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R44_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R45
// =========================
obj.setR45_PRODUCT(rs.getString("R45_PRODUCT"));
obj.setR45_STAGE1_PROVISIONS(rs.getBigDecimal("R45_STAGE1_PROVISIONS"));
obj.setR45_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R45_QUALIFY_STAGE2_PROVISIONS"));
obj.setR45_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R45_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R46
// =========================
obj.setR46_PRODUCT(rs.getString("R46_PRODUCT"));
obj.setR46_STAGE1_PROVISIONS(rs.getBigDecimal("R46_STAGE1_PROVISIONS"));
obj.setR46_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R46_QUALIFY_STAGE2_PROVISIONS"));
obj.setR46_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R46_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R47
// =========================
obj.setR47_PRODUCT(rs.getString("R47_PRODUCT"));
obj.setR47_STAGE1_PROVISIONS(rs.getBigDecimal("R47_STAGE1_PROVISIONS"));
obj.setR47_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R47_QUALIFY_STAGE2_PROVISIONS"));
obj.setR47_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R47_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R48
// =========================
obj.setR48_PRODUCT(rs.getString("R48_PRODUCT"));
obj.setR48_STAGE1_PROVISIONS(rs.getBigDecimal("R48_STAGE1_PROVISIONS"));
obj.setR48_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R48_QUALIFY_STAGE2_PROVISIONS"));
obj.setR48_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R48_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R49
// =========================
obj.setR49_PRODUCT(rs.getString("R49_PRODUCT"));
obj.setR49_STAGE1_PROVISIONS(rs.getBigDecimal("R49_STAGE1_PROVISIONS"));
obj.setR49_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R49_QUALIFY_STAGE2_PROVISIONS"));
obj.setR49_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R49_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R50
// =========================
obj.setR50_PRODUCT(rs.getString("R50_PRODUCT"));
obj.setR50_STAGE1_PROVISIONS(rs.getBigDecimal("R50_STAGE1_PROVISIONS"));
obj.setR50_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R50_QUALIFY_STAGE2_PROVISIONS"));
obj.setR50_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R50_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R51
// =========================
obj.setR51_PRODUCT(rs.getString("R51_PRODUCT"));
obj.setR51_STAGE1_PROVISIONS(rs.getBigDecimal("R51_STAGE1_PROVISIONS"));
obj.setR51_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R51_QUALIFY_STAGE2_PROVISIONS"));
obj.setR51_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R51_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R52
// =========================
obj.setR52_PRODUCT(rs.getString("R52_PRODUCT"));
obj.setR52_STAGE1_PROVISIONS(rs.getBigDecimal("R52_STAGE1_PROVISIONS"));
obj.setR52_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R52_QUALIFY_STAGE2_PROVISIONS"));
obj.setR52_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R52_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R53
// =========================
obj.setR53_PRODUCT(rs.getString("R53_PRODUCT"));
obj.setR53_STAGE1_PROVISIONS(rs.getBigDecimal("R53_STAGE1_PROVISIONS"));
obj.setR53_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R53_QUALIFY_STAGE2_PROVISIONS"));
obj.setR53_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R53_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R54
// =========================
obj.setR54_PRODUCT(rs.getString("R54_PRODUCT"));
obj.setR54_STAGE1_PROVISIONS(rs.getBigDecimal("R54_STAGE1_PROVISIONS"));
obj.setR54_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R54_QUALIFY_STAGE2_PROVISIONS"));
obj.setR54_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R54_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R55
// =========================
obj.setR55_PRODUCT(rs.getString("R55_PRODUCT"));
obj.setR55_STAGE1_PROVISIONS(rs.getBigDecimal("R55_STAGE1_PROVISIONS"));
obj.setR55_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R55_QUALIFY_STAGE2_PROVISIONS"));
obj.setR55_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R55_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R56
// =========================
obj.setR56_PRODUCT(rs.getString("R56_PRODUCT"));
obj.setR56_STAGE1_PROVISIONS(rs.getBigDecimal("R56_STAGE1_PROVISIONS"));
obj.setR56_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R56_QUALIFY_STAGE2_PROVISIONS"));
obj.setR56_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R56_TOTAL_GENERAL_PROVISIONS"));


// =========================
// R57
// =========================
obj.setR57_PRODUCT(rs.getString("R57_PRODUCT"));
obj.setR57_STAGE1_PROVISIONS(rs.getBigDecimal("R57_STAGE1_PROVISIONS"));
obj.setR57_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R57_QUALIFY_STAGE2_PROVISIONS"));
obj.setR57_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R57_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R58
// =========================
obj.setR58_PRODUCT(rs.getString("R58_PRODUCT"));
obj.setR58_STAGE1_PROVISIONS(rs.getBigDecimal("R58_STAGE1_PROVISIONS"));
obj.setR58_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R58_QUALIFY_STAGE2_PROVISIONS"));
obj.setR58_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R58_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R59
// =========================
obj.setR59_PRODUCT(rs.getString("R59_PRODUCT"));
obj.setR59_STAGE1_PROVISIONS(rs.getBigDecimal("R59_STAGE1_PROVISIONS"));
obj.setR59_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R59_QUALIFY_STAGE2_PROVISIONS"));
obj.setR59_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R59_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R60
// =========================
obj.setR60_PRODUCT(rs.getString("R60_PRODUCT"));
obj.setR60_STAGE1_PROVISIONS(rs.getBigDecimal("R60_STAGE1_PROVISIONS"));
obj.setR60_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R60_QUALIFY_STAGE2_PROVISIONS"));
obj.setR60_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R60_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R61
// =========================
obj.setR61_PRODUCT(rs.getString("R61_PRODUCT"));
obj.setR61_STAGE1_PROVISIONS(rs.getBigDecimal("R61_STAGE1_PROVISIONS"));
obj.setR61_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R61_QUALIFY_STAGE2_PROVISIONS"));
obj.setR61_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R61_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R62
// =========================
obj.setR62_PRODUCT(rs.getString("R62_PRODUCT"));
obj.setR62_STAGE1_PROVISIONS(rs.getBigDecimal("R62_STAGE1_PROVISIONS"));
obj.setR62_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R62_QUALIFY_STAGE2_PROVISIONS"));
obj.setR62_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R62_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R63
// =========================
obj.setR63_PRODUCT(rs.getString("R63_PRODUCT"));
obj.setR63_STAGE1_PROVISIONS(rs.getBigDecimal("R63_STAGE1_PROVISIONS"));
obj.setR63_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R63_QUALIFY_STAGE2_PROVISIONS"));
obj.setR63_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R63_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R64
// =========================
obj.setR64_PRODUCT(rs.getString("R64_PRODUCT"));
obj.setR64_STAGE1_PROVISIONS(rs.getBigDecimal("R64_STAGE1_PROVISIONS"));
obj.setR64_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R64_QUALIFY_STAGE2_PROVISIONS"));
obj.setR64_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R64_TOTAL_GENERAL_PROVISIONS"));


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


public class M_GP_Summary_Entity {
	
	private String R11_PRODUCT;
	private BigDecimal R11_STAGE1_PROVISIONS;
	private BigDecimal R11_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R11_TOTAL_GENERAL_PROVISIONS;

	private String R12_PRODUCT;
	private BigDecimal R12_STAGE1_PROVISIONS;
	private BigDecimal R12_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R12_TOTAL_GENERAL_PROVISIONS;

	private String R13_PRODUCT;
	private BigDecimal R13_STAGE1_PROVISIONS;
	private BigDecimal R13_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R13_TOTAL_GENERAL_PROVISIONS;

	private String R14_PRODUCT;
	private BigDecimal R14_STAGE1_PROVISIONS;
	private BigDecimal R14_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R14_TOTAL_GENERAL_PROVISIONS;

	private String R15_PRODUCT;
	private BigDecimal R15_STAGE1_PROVISIONS;
	private BigDecimal R15_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R15_TOTAL_GENERAL_PROVISIONS;

	private String R16_PRODUCT;
	private BigDecimal R16_STAGE1_PROVISIONS;
	private BigDecimal R16_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R16_TOTAL_GENERAL_PROVISIONS;

	private String R17_PRODUCT;
	private BigDecimal R17_STAGE1_PROVISIONS;
	private BigDecimal R17_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R17_TOTAL_GENERAL_PROVISIONS;

	private String R18_PRODUCT;
	private BigDecimal R18_STAGE1_PROVISIONS;
	private BigDecimal R18_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R18_TOTAL_GENERAL_PROVISIONS;

	private String R19_PRODUCT;
	private BigDecimal R19_STAGE1_PROVISIONS;
	private BigDecimal R19_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R19_TOTAL_GENERAL_PROVISIONS;

	private String R20_PRODUCT;
	private BigDecimal R20_STAGE1_PROVISIONS;
	private BigDecimal R20_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R20_TOTAL_GENERAL_PROVISIONS;

	private String R21_PRODUCT;
	private BigDecimal R21_STAGE1_PROVISIONS;
	private BigDecimal R21_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R21_TOTAL_GENERAL_PROVISIONS;

	private String R22_PRODUCT;
	private BigDecimal R22_STAGE1_PROVISIONS;
	private BigDecimal R22_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R22_TOTAL_GENERAL_PROVISIONS;

	private String R23_PRODUCT;
	private BigDecimal R23_STAGE1_PROVISIONS;
	private BigDecimal R23_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R23_TOTAL_GENERAL_PROVISIONS;

	private String R24_PRODUCT;
	private BigDecimal R24_STAGE1_PROVISIONS;
	private BigDecimal R24_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R24_TOTAL_GENERAL_PROVISIONS;

	private String R25_PRODUCT;
	private BigDecimal R25_STAGE1_PROVISIONS;
	private BigDecimal R25_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R25_TOTAL_GENERAL_PROVISIONS;

	private String R26_PRODUCT;
	private BigDecimal R26_STAGE1_PROVISIONS;
	private BigDecimal R26_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R26_TOTAL_GENERAL_PROVISIONS;

	private String R27_PRODUCT;
	private BigDecimal R27_STAGE1_PROVISIONS;
	private BigDecimal R27_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R27_TOTAL_GENERAL_PROVISIONS;

	private String R28_PRODUCT;
	private BigDecimal R28_STAGE1_PROVISIONS;
	private BigDecimal R28_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R28_TOTAL_GENERAL_PROVISIONS;

	private String R29_PRODUCT;
	private BigDecimal R29_STAGE1_PROVISIONS;
	private BigDecimal R29_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R29_TOTAL_GENERAL_PROVISIONS;

	private String R30_PRODUCT;
	private BigDecimal R30_STAGE1_PROVISIONS;
	private BigDecimal R30_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R30_TOTAL_GENERAL_PROVISIONS;

	private String R31_PRODUCT;
	private BigDecimal R31_STAGE1_PROVISIONS;
	private BigDecimal R31_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R31_TOTAL_GENERAL_PROVISIONS;

	private String R32_PRODUCT;
	private BigDecimal R32_STAGE1_PROVISIONS;
	private BigDecimal R32_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R32_TOTAL_GENERAL_PROVISIONS;

	private String R33_PRODUCT;
	private BigDecimal R33_STAGE1_PROVISIONS;
	private BigDecimal R33_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R33_TOTAL_GENERAL_PROVISIONS;

	private String R34_PRODUCT;
	private BigDecimal R34_STAGE1_PROVISIONS;
	private BigDecimal R34_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R34_TOTAL_GENERAL_PROVISIONS;

	private String R35_PRODUCT;
	private BigDecimal R35_STAGE1_PROVISIONS;
	private BigDecimal R35_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R35_TOTAL_GENERAL_PROVISIONS;

	private String R36_PRODUCT;
	private BigDecimal R36_STAGE1_PROVISIONS;
	private BigDecimal R36_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R36_TOTAL_GENERAL_PROVISIONS;

	private String R37_PRODUCT;
	private BigDecimal R37_STAGE1_PROVISIONS;
	private BigDecimal R37_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R37_TOTAL_GENERAL_PROVISIONS;

	private String R38_PRODUCT;
	private BigDecimal R38_STAGE1_PROVISIONS;
	private BigDecimal R38_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R38_TOTAL_GENERAL_PROVISIONS;

	private String R39_PRODUCT;
	private BigDecimal R39_STAGE1_PROVISIONS;
	private BigDecimal R39_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R39_TOTAL_GENERAL_PROVISIONS;

	private String R40_PRODUCT;
	private BigDecimal R40_STAGE1_PROVISIONS;
	private BigDecimal R40_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R40_TOTAL_GENERAL_PROVISIONS;

	private String R41_PRODUCT;
	private BigDecimal R41_STAGE1_PROVISIONS;
	private BigDecimal R41_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R41_TOTAL_GENERAL_PROVISIONS;

	private String R42_PRODUCT;
	private BigDecimal R42_STAGE1_PROVISIONS;
	private BigDecimal R42_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R42_TOTAL_GENERAL_PROVISIONS;

	private String R43_PRODUCT;
	private BigDecimal R43_STAGE1_PROVISIONS;
	private BigDecimal R43_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R43_TOTAL_GENERAL_PROVISIONS;

	private String R44_PRODUCT;
	private BigDecimal R44_STAGE1_PROVISIONS;
	private BigDecimal R44_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R44_TOTAL_GENERAL_PROVISIONS;

	private String R45_PRODUCT;
	private BigDecimal R45_STAGE1_PROVISIONS;
	private BigDecimal R45_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R45_TOTAL_GENERAL_PROVISIONS;

	private String R46_PRODUCT;
	private BigDecimal R46_STAGE1_PROVISIONS;
	private BigDecimal R46_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R46_TOTAL_GENERAL_PROVISIONS;

	private String R47_PRODUCT;
	private BigDecimal R47_STAGE1_PROVISIONS;
	private BigDecimal R47_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R47_TOTAL_GENERAL_PROVISIONS;

	private String R48_PRODUCT;
	private BigDecimal R48_STAGE1_PROVISIONS;
	private BigDecimal R48_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R48_TOTAL_GENERAL_PROVISIONS;

	private String R49_PRODUCT;
	private BigDecimal R49_STAGE1_PROVISIONS;
	private BigDecimal R49_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R49_TOTAL_GENERAL_PROVISIONS;

	private String R50_PRODUCT;
	private BigDecimal R50_STAGE1_PROVISIONS;
	private BigDecimal R50_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R50_TOTAL_GENERAL_PROVISIONS;

	private String R51_PRODUCT;
	private BigDecimal R51_STAGE1_PROVISIONS;
	private BigDecimal R51_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R51_TOTAL_GENERAL_PROVISIONS;

	private String R52_PRODUCT;
	private BigDecimal R52_STAGE1_PROVISIONS;
	private BigDecimal R52_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R52_TOTAL_GENERAL_PROVISIONS;

	private String R53_PRODUCT;
	private BigDecimal R53_STAGE1_PROVISIONS;
	private BigDecimal R53_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R53_TOTAL_GENERAL_PROVISIONS;

	private String R54_PRODUCT;
	private BigDecimal R54_STAGE1_PROVISIONS;
	private BigDecimal R54_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R54_TOTAL_GENERAL_PROVISIONS;

	private String R55_PRODUCT;
	private BigDecimal R55_STAGE1_PROVISIONS;
	private BigDecimal R55_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R55_TOTAL_GENERAL_PROVISIONS;

	private String R56_PRODUCT;
	private BigDecimal R56_STAGE1_PROVISIONS;
	private BigDecimal R56_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R56_TOTAL_GENERAL_PROVISIONS;

	private String R57_PRODUCT;
	private BigDecimal R57_STAGE1_PROVISIONS;
	private BigDecimal R57_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R57_TOTAL_GENERAL_PROVISIONS;

	private String R58_PRODUCT;
	private BigDecimal R58_STAGE1_PROVISIONS;
	private BigDecimal R58_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R58_TOTAL_GENERAL_PROVISIONS;

	private String R59_PRODUCT;
	private BigDecimal R59_STAGE1_PROVISIONS;
	private BigDecimal R59_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R59_TOTAL_GENERAL_PROVISIONS;

	private String R60_PRODUCT;
	private BigDecimal R60_STAGE1_PROVISIONS;
	private BigDecimal R60_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R60_TOTAL_GENERAL_PROVISIONS;

	private String R61_PRODUCT;
	private BigDecimal R61_STAGE1_PROVISIONS;
	private BigDecimal R61_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R61_TOTAL_GENERAL_PROVISIONS;

	private String R62_PRODUCT;
	private BigDecimal R62_STAGE1_PROVISIONS;
	private BigDecimal R62_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R62_TOTAL_GENERAL_PROVISIONS;

	private String R63_PRODUCT;
	private BigDecimal R63_STAGE1_PROVISIONS;
	private BigDecimal R63_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R63_TOTAL_GENERAL_PROVISIONS;

	private String R64_PRODUCT;
	private BigDecimal R64_STAGE1_PROVISIONS;
	private BigDecimal R64_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R64_TOTAL_GENERAL_PROVISIONS;
	

	
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
	
public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_STAGE1_PROVISIONS() {
		return R11_STAGE1_PROVISIONS;
	}
	public void setR11_STAGE1_PROVISIONS(BigDecimal r11_STAGE1_PROVISIONS) {
		R11_STAGE1_PROVISIONS = r11_STAGE1_PROVISIONS;
	}
	public BigDecimal getR11_QUALIFY_STAGE2_PROVISIONS() {
		return R11_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR11_QUALIFY_STAGE2_PROVISIONS(BigDecimal r11_QUALIFY_STAGE2_PROVISIONS) {
		R11_QUALIFY_STAGE2_PROVISIONS = r11_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR11_TOTAL_GENERAL_PROVISIONS() {
		return R11_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR11_TOTAL_GENERAL_PROVISIONS(BigDecimal r11_TOTAL_GENERAL_PROVISIONS) {
		R11_TOTAL_GENERAL_PROVISIONS = r11_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_STAGE1_PROVISIONS() {
		return R12_STAGE1_PROVISIONS;
	}
	public void setR12_STAGE1_PROVISIONS(BigDecimal r12_STAGE1_PROVISIONS) {
		R12_STAGE1_PROVISIONS = r12_STAGE1_PROVISIONS;
	}
	public BigDecimal getR12_QUALIFY_STAGE2_PROVISIONS() {
		return R12_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR12_QUALIFY_STAGE2_PROVISIONS(BigDecimal r12_QUALIFY_STAGE2_PROVISIONS) {
		R12_QUALIFY_STAGE2_PROVISIONS = r12_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR12_TOTAL_GENERAL_PROVISIONS() {
		return R12_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR12_TOTAL_GENERAL_PROVISIONS(BigDecimal r12_TOTAL_GENERAL_PROVISIONS) {
		R12_TOTAL_GENERAL_PROVISIONS = r12_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_STAGE1_PROVISIONS() {
		return R13_STAGE1_PROVISIONS;
	}
	public void setR13_STAGE1_PROVISIONS(BigDecimal r13_STAGE1_PROVISIONS) {
		R13_STAGE1_PROVISIONS = r13_STAGE1_PROVISIONS;
	}
	public BigDecimal getR13_QUALIFY_STAGE2_PROVISIONS() {
		return R13_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR13_QUALIFY_STAGE2_PROVISIONS(BigDecimal r13_QUALIFY_STAGE2_PROVISIONS) {
		R13_QUALIFY_STAGE2_PROVISIONS = r13_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR13_TOTAL_GENERAL_PROVISIONS() {
		return R13_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR13_TOTAL_GENERAL_PROVISIONS(BigDecimal r13_TOTAL_GENERAL_PROVISIONS) {
		R13_TOTAL_GENERAL_PROVISIONS = r13_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_STAGE1_PROVISIONS() {
		return R14_STAGE1_PROVISIONS;
	}
	public void setR14_STAGE1_PROVISIONS(BigDecimal r14_STAGE1_PROVISIONS) {
		R14_STAGE1_PROVISIONS = r14_STAGE1_PROVISIONS;
	}
	public BigDecimal getR14_QUALIFY_STAGE2_PROVISIONS() {
		return R14_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR14_QUALIFY_STAGE2_PROVISIONS(BigDecimal r14_QUALIFY_STAGE2_PROVISIONS) {
		R14_QUALIFY_STAGE2_PROVISIONS = r14_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR14_TOTAL_GENERAL_PROVISIONS() {
		return R14_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR14_TOTAL_GENERAL_PROVISIONS(BigDecimal r14_TOTAL_GENERAL_PROVISIONS) {
		R14_TOTAL_GENERAL_PROVISIONS = r14_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_STAGE1_PROVISIONS() {
		return R15_STAGE1_PROVISIONS;
	}
	public void setR15_STAGE1_PROVISIONS(BigDecimal r15_STAGE1_PROVISIONS) {
		R15_STAGE1_PROVISIONS = r15_STAGE1_PROVISIONS;
	}
	public BigDecimal getR15_QUALIFY_STAGE2_PROVISIONS() {
		return R15_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR15_QUALIFY_STAGE2_PROVISIONS(BigDecimal r15_QUALIFY_STAGE2_PROVISIONS) {
		R15_QUALIFY_STAGE2_PROVISIONS = r15_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR15_TOTAL_GENERAL_PROVISIONS() {
		return R15_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR15_TOTAL_GENERAL_PROVISIONS(BigDecimal r15_TOTAL_GENERAL_PROVISIONS) {
		R15_TOTAL_GENERAL_PROVISIONS = r15_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_STAGE1_PROVISIONS() {
		return R16_STAGE1_PROVISIONS;
	}
	public void setR16_STAGE1_PROVISIONS(BigDecimal r16_STAGE1_PROVISIONS) {
		R16_STAGE1_PROVISIONS = r16_STAGE1_PROVISIONS;
	}
	public BigDecimal getR16_QUALIFY_STAGE2_PROVISIONS() {
		return R16_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR16_QUALIFY_STAGE2_PROVISIONS(BigDecimal r16_QUALIFY_STAGE2_PROVISIONS) {
		R16_QUALIFY_STAGE2_PROVISIONS = r16_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR16_TOTAL_GENERAL_PROVISIONS() {
		return R16_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR16_TOTAL_GENERAL_PROVISIONS(BigDecimal r16_TOTAL_GENERAL_PROVISIONS) {
		R16_TOTAL_GENERAL_PROVISIONS = r16_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_STAGE1_PROVISIONS() {
		return R17_STAGE1_PROVISIONS;
	}
	public void setR17_STAGE1_PROVISIONS(BigDecimal r17_STAGE1_PROVISIONS) {
		R17_STAGE1_PROVISIONS = r17_STAGE1_PROVISIONS;
	}
	public BigDecimal getR17_QUALIFY_STAGE2_PROVISIONS() {
		return R17_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR17_QUALIFY_STAGE2_PROVISIONS(BigDecimal r17_QUALIFY_STAGE2_PROVISIONS) {
		R17_QUALIFY_STAGE2_PROVISIONS = r17_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR17_TOTAL_GENERAL_PROVISIONS() {
		return R17_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR17_TOTAL_GENERAL_PROVISIONS(BigDecimal r17_TOTAL_GENERAL_PROVISIONS) {
		R17_TOTAL_GENERAL_PROVISIONS = r17_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}
	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}
	public BigDecimal getR18_STAGE1_PROVISIONS() {
		return R18_STAGE1_PROVISIONS;
	}
	public void setR18_STAGE1_PROVISIONS(BigDecimal r18_STAGE1_PROVISIONS) {
		R18_STAGE1_PROVISIONS = r18_STAGE1_PROVISIONS;
	}
	public BigDecimal getR18_QUALIFY_STAGE2_PROVISIONS() {
		return R18_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR18_QUALIFY_STAGE2_PROVISIONS(BigDecimal r18_QUALIFY_STAGE2_PROVISIONS) {
		R18_QUALIFY_STAGE2_PROVISIONS = r18_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR18_TOTAL_GENERAL_PROVISIONS() {
		return R18_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR18_TOTAL_GENERAL_PROVISIONS(BigDecimal r18_TOTAL_GENERAL_PROVISIONS) {
		R18_TOTAL_GENERAL_PROVISIONS = r18_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}
	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}
	public BigDecimal getR19_STAGE1_PROVISIONS() {
		return R19_STAGE1_PROVISIONS;
	}
	public void setR19_STAGE1_PROVISIONS(BigDecimal r19_STAGE1_PROVISIONS) {
		R19_STAGE1_PROVISIONS = r19_STAGE1_PROVISIONS;
	}
	public BigDecimal getR19_QUALIFY_STAGE2_PROVISIONS() {
		return R19_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR19_QUALIFY_STAGE2_PROVISIONS(BigDecimal r19_QUALIFY_STAGE2_PROVISIONS) {
		R19_QUALIFY_STAGE2_PROVISIONS = r19_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR19_TOTAL_GENERAL_PROVISIONS() {
		return R19_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR19_TOTAL_GENERAL_PROVISIONS(BigDecimal r19_TOTAL_GENERAL_PROVISIONS) {
		R19_TOTAL_GENERAL_PROVISIONS = r19_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}
	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}
	public BigDecimal getR20_STAGE1_PROVISIONS() {
		return R20_STAGE1_PROVISIONS;
	}
	public void setR20_STAGE1_PROVISIONS(BigDecimal r20_STAGE1_PROVISIONS) {
		R20_STAGE1_PROVISIONS = r20_STAGE1_PROVISIONS;
	}
	public BigDecimal getR20_QUALIFY_STAGE2_PROVISIONS() {
		return R20_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR20_QUALIFY_STAGE2_PROVISIONS(BigDecimal r20_QUALIFY_STAGE2_PROVISIONS) {
		R20_QUALIFY_STAGE2_PROVISIONS = r20_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR20_TOTAL_GENERAL_PROVISIONS() {
		return R20_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR20_TOTAL_GENERAL_PROVISIONS(BigDecimal r20_TOTAL_GENERAL_PROVISIONS) {
		R20_TOTAL_GENERAL_PROVISIONS = r20_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}
	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}
	public BigDecimal getR21_STAGE1_PROVISIONS() {
		return R21_STAGE1_PROVISIONS;
	}
	public void setR21_STAGE1_PROVISIONS(BigDecimal r21_STAGE1_PROVISIONS) {
		R21_STAGE1_PROVISIONS = r21_STAGE1_PROVISIONS;
	}
	public BigDecimal getR21_QUALIFY_STAGE2_PROVISIONS() {
		return R21_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR21_QUALIFY_STAGE2_PROVISIONS(BigDecimal r21_QUALIFY_STAGE2_PROVISIONS) {
		R21_QUALIFY_STAGE2_PROVISIONS = r21_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR21_TOTAL_GENERAL_PROVISIONS() {
		return R21_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR21_TOTAL_GENERAL_PROVISIONS(BigDecimal r21_TOTAL_GENERAL_PROVISIONS) {
		R21_TOTAL_GENERAL_PROVISIONS = r21_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}
	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}
	public BigDecimal getR22_STAGE1_PROVISIONS() {
		return R22_STAGE1_PROVISIONS;
	}
	public void setR22_STAGE1_PROVISIONS(BigDecimal r22_STAGE1_PROVISIONS) {
		R22_STAGE1_PROVISIONS = r22_STAGE1_PROVISIONS;
	}
	public BigDecimal getR22_QUALIFY_STAGE2_PROVISIONS() {
		return R22_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR22_QUALIFY_STAGE2_PROVISIONS(BigDecimal r22_QUALIFY_STAGE2_PROVISIONS) {
		R22_QUALIFY_STAGE2_PROVISIONS = r22_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR22_TOTAL_GENERAL_PROVISIONS() {
		return R22_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR22_TOTAL_GENERAL_PROVISIONS(BigDecimal r22_TOTAL_GENERAL_PROVISIONS) {
		R22_TOTAL_GENERAL_PROVISIONS = r22_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}
	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}
	public BigDecimal getR23_STAGE1_PROVISIONS() {
		return R23_STAGE1_PROVISIONS;
	}
	public void setR23_STAGE1_PROVISIONS(BigDecimal r23_STAGE1_PROVISIONS) {
		R23_STAGE1_PROVISIONS = r23_STAGE1_PROVISIONS;
	}
	public BigDecimal getR23_QUALIFY_STAGE2_PROVISIONS() {
		return R23_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR23_QUALIFY_STAGE2_PROVISIONS(BigDecimal r23_QUALIFY_STAGE2_PROVISIONS) {
		R23_QUALIFY_STAGE2_PROVISIONS = r23_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR23_TOTAL_GENERAL_PROVISIONS() {
		return R23_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR23_TOTAL_GENERAL_PROVISIONS(BigDecimal r23_TOTAL_GENERAL_PROVISIONS) {
		R23_TOTAL_GENERAL_PROVISIONS = r23_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}
	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}
	public BigDecimal getR24_STAGE1_PROVISIONS() {
		return R24_STAGE1_PROVISIONS;
	}
	public void setR24_STAGE1_PROVISIONS(BigDecimal r24_STAGE1_PROVISIONS) {
		R24_STAGE1_PROVISIONS = r24_STAGE1_PROVISIONS;
	}
	public BigDecimal getR24_QUALIFY_STAGE2_PROVISIONS() {
		return R24_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR24_QUALIFY_STAGE2_PROVISIONS(BigDecimal r24_QUALIFY_STAGE2_PROVISIONS) {
		R24_QUALIFY_STAGE2_PROVISIONS = r24_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR24_TOTAL_GENERAL_PROVISIONS() {
		return R24_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR24_TOTAL_GENERAL_PROVISIONS(BigDecimal r24_TOTAL_GENERAL_PROVISIONS) {
		R24_TOTAL_GENERAL_PROVISIONS = r24_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}
	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}
	public BigDecimal getR25_STAGE1_PROVISIONS() {
		return R25_STAGE1_PROVISIONS;
	}
	public void setR25_STAGE1_PROVISIONS(BigDecimal r25_STAGE1_PROVISIONS) {
		R25_STAGE1_PROVISIONS = r25_STAGE1_PROVISIONS;
	}
	public BigDecimal getR25_QUALIFY_STAGE2_PROVISIONS() {
		return R25_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR25_QUALIFY_STAGE2_PROVISIONS(BigDecimal r25_QUALIFY_STAGE2_PROVISIONS) {
		R25_QUALIFY_STAGE2_PROVISIONS = r25_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR25_TOTAL_GENERAL_PROVISIONS() {
		return R25_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR25_TOTAL_GENERAL_PROVISIONS(BigDecimal r25_TOTAL_GENERAL_PROVISIONS) {
		R25_TOTAL_GENERAL_PROVISIONS = r25_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}
	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}
	public BigDecimal getR26_STAGE1_PROVISIONS() {
		return R26_STAGE1_PROVISIONS;
	}
	public void setR26_STAGE1_PROVISIONS(BigDecimal r26_STAGE1_PROVISIONS) {
		R26_STAGE1_PROVISIONS = r26_STAGE1_PROVISIONS;
	}
	public BigDecimal getR26_QUALIFY_STAGE2_PROVISIONS() {
		return R26_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR26_QUALIFY_STAGE2_PROVISIONS(BigDecimal r26_QUALIFY_STAGE2_PROVISIONS) {
		R26_QUALIFY_STAGE2_PROVISIONS = r26_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR26_TOTAL_GENERAL_PROVISIONS() {
		return R26_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR26_TOTAL_GENERAL_PROVISIONS(BigDecimal r26_TOTAL_GENERAL_PROVISIONS) {
		R26_TOTAL_GENERAL_PROVISIONS = r26_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}
	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}
	public BigDecimal getR27_STAGE1_PROVISIONS() {
		return R27_STAGE1_PROVISIONS;
	}
	public void setR27_STAGE1_PROVISIONS(BigDecimal r27_STAGE1_PROVISIONS) {
		R27_STAGE1_PROVISIONS = r27_STAGE1_PROVISIONS;
	}
	public BigDecimal getR27_QUALIFY_STAGE2_PROVISIONS() {
		return R27_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR27_QUALIFY_STAGE2_PROVISIONS(BigDecimal r27_QUALIFY_STAGE2_PROVISIONS) {
		R27_QUALIFY_STAGE2_PROVISIONS = r27_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR27_TOTAL_GENERAL_PROVISIONS() {
		return R27_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR27_TOTAL_GENERAL_PROVISIONS(BigDecimal r27_TOTAL_GENERAL_PROVISIONS) {
		R27_TOTAL_GENERAL_PROVISIONS = r27_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}
	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}
	public BigDecimal getR28_STAGE1_PROVISIONS() {
		return R28_STAGE1_PROVISIONS;
	}
	public void setR28_STAGE1_PROVISIONS(BigDecimal r28_STAGE1_PROVISIONS) {
		R28_STAGE1_PROVISIONS = r28_STAGE1_PROVISIONS;
	}
	public BigDecimal getR28_QUALIFY_STAGE2_PROVISIONS() {
		return R28_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR28_QUALIFY_STAGE2_PROVISIONS(BigDecimal r28_QUALIFY_STAGE2_PROVISIONS) {
		R28_QUALIFY_STAGE2_PROVISIONS = r28_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR28_TOTAL_GENERAL_PROVISIONS() {
		return R28_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR28_TOTAL_GENERAL_PROVISIONS(BigDecimal r28_TOTAL_GENERAL_PROVISIONS) {
		R28_TOTAL_GENERAL_PROVISIONS = r28_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}
	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}
	public BigDecimal getR29_STAGE1_PROVISIONS() {
		return R29_STAGE1_PROVISIONS;
	}
	public void setR29_STAGE1_PROVISIONS(BigDecimal r29_STAGE1_PROVISIONS) {
		R29_STAGE1_PROVISIONS = r29_STAGE1_PROVISIONS;
	}
	public BigDecimal getR29_QUALIFY_STAGE2_PROVISIONS() {
		return R29_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR29_QUALIFY_STAGE2_PROVISIONS(BigDecimal r29_QUALIFY_STAGE2_PROVISIONS) {
		R29_QUALIFY_STAGE2_PROVISIONS = r29_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR29_TOTAL_GENERAL_PROVISIONS() {
		return R29_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR29_TOTAL_GENERAL_PROVISIONS(BigDecimal r29_TOTAL_GENERAL_PROVISIONS) {
		R29_TOTAL_GENERAL_PROVISIONS = r29_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}
	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}
	public BigDecimal getR30_STAGE1_PROVISIONS() {
		return R30_STAGE1_PROVISIONS;
	}
	public void setR30_STAGE1_PROVISIONS(BigDecimal r30_STAGE1_PROVISIONS) {
		R30_STAGE1_PROVISIONS = r30_STAGE1_PROVISIONS;
	}
	public BigDecimal getR30_QUALIFY_STAGE2_PROVISIONS() {
		return R30_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR30_QUALIFY_STAGE2_PROVISIONS(BigDecimal r30_QUALIFY_STAGE2_PROVISIONS) {
		R30_QUALIFY_STAGE2_PROVISIONS = r30_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR30_TOTAL_GENERAL_PROVISIONS() {
		return R30_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR30_TOTAL_GENERAL_PROVISIONS(BigDecimal r30_TOTAL_GENERAL_PROVISIONS) {
		R30_TOTAL_GENERAL_PROVISIONS = r30_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}
	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}
	public BigDecimal getR31_STAGE1_PROVISIONS() {
		return R31_STAGE1_PROVISIONS;
	}
	public void setR31_STAGE1_PROVISIONS(BigDecimal r31_STAGE1_PROVISIONS) {
		R31_STAGE1_PROVISIONS = r31_STAGE1_PROVISIONS;
	}
	public BigDecimal getR31_QUALIFY_STAGE2_PROVISIONS() {
		return R31_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR31_QUALIFY_STAGE2_PROVISIONS(BigDecimal r31_QUALIFY_STAGE2_PROVISIONS) {
		R31_QUALIFY_STAGE2_PROVISIONS = r31_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR31_TOTAL_GENERAL_PROVISIONS() {
		return R31_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR31_TOTAL_GENERAL_PROVISIONS(BigDecimal r31_TOTAL_GENERAL_PROVISIONS) {
		R31_TOTAL_GENERAL_PROVISIONS = r31_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}
	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}
	public BigDecimal getR32_STAGE1_PROVISIONS() {
		return R32_STAGE1_PROVISIONS;
	}
	public void setR32_STAGE1_PROVISIONS(BigDecimal r32_STAGE1_PROVISIONS) {
		R32_STAGE1_PROVISIONS = r32_STAGE1_PROVISIONS;
	}
	public BigDecimal getR32_QUALIFY_STAGE2_PROVISIONS() {
		return R32_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR32_QUALIFY_STAGE2_PROVISIONS(BigDecimal r32_QUALIFY_STAGE2_PROVISIONS) {
		R32_QUALIFY_STAGE2_PROVISIONS = r32_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR32_TOTAL_GENERAL_PROVISIONS() {
		return R32_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR32_TOTAL_GENERAL_PROVISIONS(BigDecimal r32_TOTAL_GENERAL_PROVISIONS) {
		R32_TOTAL_GENERAL_PROVISIONS = r32_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}
	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}
	public BigDecimal getR33_STAGE1_PROVISIONS() {
		return R33_STAGE1_PROVISIONS;
	}
	public void setR33_STAGE1_PROVISIONS(BigDecimal r33_STAGE1_PROVISIONS) {
		R33_STAGE1_PROVISIONS = r33_STAGE1_PROVISIONS;
	}
	public BigDecimal getR33_QUALIFY_STAGE2_PROVISIONS() {
		return R33_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR33_QUALIFY_STAGE2_PROVISIONS(BigDecimal r33_QUALIFY_STAGE2_PROVISIONS) {
		R33_QUALIFY_STAGE2_PROVISIONS = r33_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR33_TOTAL_GENERAL_PROVISIONS() {
		return R33_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR33_TOTAL_GENERAL_PROVISIONS(BigDecimal r33_TOTAL_GENERAL_PROVISIONS) {
		R33_TOTAL_GENERAL_PROVISIONS = r33_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}
	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}
	public BigDecimal getR34_STAGE1_PROVISIONS() {
		return R34_STAGE1_PROVISIONS;
	}
	public void setR34_STAGE1_PROVISIONS(BigDecimal r34_STAGE1_PROVISIONS) {
		R34_STAGE1_PROVISIONS = r34_STAGE1_PROVISIONS;
	}
	public BigDecimal getR34_QUALIFY_STAGE2_PROVISIONS() {
		return R34_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR34_QUALIFY_STAGE2_PROVISIONS(BigDecimal r34_QUALIFY_STAGE2_PROVISIONS) {
		R34_QUALIFY_STAGE2_PROVISIONS = r34_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR34_TOTAL_GENERAL_PROVISIONS() {
		return R34_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR34_TOTAL_GENERAL_PROVISIONS(BigDecimal r34_TOTAL_GENERAL_PROVISIONS) {
		R34_TOTAL_GENERAL_PROVISIONS = r34_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}
	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}
	public BigDecimal getR35_STAGE1_PROVISIONS() {
		return R35_STAGE1_PROVISIONS;
	}
	public void setR35_STAGE1_PROVISIONS(BigDecimal r35_STAGE1_PROVISIONS) {
		R35_STAGE1_PROVISIONS = r35_STAGE1_PROVISIONS;
	}
	public BigDecimal getR35_QUALIFY_STAGE2_PROVISIONS() {
		return R35_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR35_QUALIFY_STAGE2_PROVISIONS(BigDecimal r35_QUALIFY_STAGE2_PROVISIONS) {
		R35_QUALIFY_STAGE2_PROVISIONS = r35_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR35_TOTAL_GENERAL_PROVISIONS() {
		return R35_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR35_TOTAL_GENERAL_PROVISIONS(BigDecimal r35_TOTAL_GENERAL_PROVISIONS) {
		R35_TOTAL_GENERAL_PROVISIONS = r35_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_STAGE1_PROVISIONS() {
		return R36_STAGE1_PROVISIONS;
	}
	public void setR36_STAGE1_PROVISIONS(BigDecimal r36_STAGE1_PROVISIONS) {
		R36_STAGE1_PROVISIONS = r36_STAGE1_PROVISIONS;
	}
	public BigDecimal getR36_QUALIFY_STAGE2_PROVISIONS() {
		return R36_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR36_QUALIFY_STAGE2_PROVISIONS(BigDecimal r36_QUALIFY_STAGE2_PROVISIONS) {
		R36_QUALIFY_STAGE2_PROVISIONS = r36_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR36_TOTAL_GENERAL_PROVISIONS() {
		return R36_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR36_TOTAL_GENERAL_PROVISIONS(BigDecimal r36_TOTAL_GENERAL_PROVISIONS) {
		R36_TOTAL_GENERAL_PROVISIONS = r36_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}
	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}
	public BigDecimal getR37_STAGE1_PROVISIONS() {
		return R37_STAGE1_PROVISIONS;
	}
	public void setR37_STAGE1_PROVISIONS(BigDecimal r37_STAGE1_PROVISIONS) {
		R37_STAGE1_PROVISIONS = r37_STAGE1_PROVISIONS;
	}
	public BigDecimal getR37_QUALIFY_STAGE2_PROVISIONS() {
		return R37_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR37_QUALIFY_STAGE2_PROVISIONS(BigDecimal r37_QUALIFY_STAGE2_PROVISIONS) {
		R37_QUALIFY_STAGE2_PROVISIONS = r37_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR37_TOTAL_GENERAL_PROVISIONS() {
		return R37_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR37_TOTAL_GENERAL_PROVISIONS(BigDecimal r37_TOTAL_GENERAL_PROVISIONS) {
		R37_TOTAL_GENERAL_PROVISIONS = r37_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}
	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}
	public BigDecimal getR38_STAGE1_PROVISIONS() {
		return R38_STAGE1_PROVISIONS;
	}
	public void setR38_STAGE1_PROVISIONS(BigDecimal r38_STAGE1_PROVISIONS) {
		R38_STAGE1_PROVISIONS = r38_STAGE1_PROVISIONS;
	}
	public BigDecimal getR38_QUALIFY_STAGE2_PROVISIONS() {
		return R38_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR38_QUALIFY_STAGE2_PROVISIONS(BigDecimal r38_QUALIFY_STAGE2_PROVISIONS) {
		R38_QUALIFY_STAGE2_PROVISIONS = r38_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR38_TOTAL_GENERAL_PROVISIONS() {
		return R38_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR38_TOTAL_GENERAL_PROVISIONS(BigDecimal r38_TOTAL_GENERAL_PROVISIONS) {
		R38_TOTAL_GENERAL_PROVISIONS = r38_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}
	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}
	public BigDecimal getR39_STAGE1_PROVISIONS() {
		return R39_STAGE1_PROVISIONS;
	}
	public void setR39_STAGE1_PROVISIONS(BigDecimal r39_STAGE1_PROVISIONS) {
		R39_STAGE1_PROVISIONS = r39_STAGE1_PROVISIONS;
	}
	public BigDecimal getR39_QUALIFY_STAGE2_PROVISIONS() {
		return R39_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR39_QUALIFY_STAGE2_PROVISIONS(BigDecimal r39_QUALIFY_STAGE2_PROVISIONS) {
		R39_QUALIFY_STAGE2_PROVISIONS = r39_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR39_TOTAL_GENERAL_PROVISIONS() {
		return R39_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR39_TOTAL_GENERAL_PROVISIONS(BigDecimal r39_TOTAL_GENERAL_PROVISIONS) {
		R39_TOTAL_GENERAL_PROVISIONS = r39_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}
	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}
	public BigDecimal getR40_STAGE1_PROVISIONS() {
		return R40_STAGE1_PROVISIONS;
	}
	public void setR40_STAGE1_PROVISIONS(BigDecimal r40_STAGE1_PROVISIONS) {
		R40_STAGE1_PROVISIONS = r40_STAGE1_PROVISIONS;
	}
	public BigDecimal getR40_QUALIFY_STAGE2_PROVISIONS() {
		return R40_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR40_QUALIFY_STAGE2_PROVISIONS(BigDecimal r40_QUALIFY_STAGE2_PROVISIONS) {
		R40_QUALIFY_STAGE2_PROVISIONS = r40_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR40_TOTAL_GENERAL_PROVISIONS() {
		return R40_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR40_TOTAL_GENERAL_PROVISIONS(BigDecimal r40_TOTAL_GENERAL_PROVISIONS) {
		R40_TOTAL_GENERAL_PROVISIONS = r40_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}
	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}
	public BigDecimal getR41_STAGE1_PROVISIONS() {
		return R41_STAGE1_PROVISIONS;
	}
	public void setR41_STAGE1_PROVISIONS(BigDecimal r41_STAGE1_PROVISIONS) {
		R41_STAGE1_PROVISIONS = r41_STAGE1_PROVISIONS;
	}
	public BigDecimal getR41_QUALIFY_STAGE2_PROVISIONS() {
		return R41_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR41_QUALIFY_STAGE2_PROVISIONS(BigDecimal r41_QUALIFY_STAGE2_PROVISIONS) {
		R41_QUALIFY_STAGE2_PROVISIONS = r41_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR41_TOTAL_GENERAL_PROVISIONS() {
		return R41_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR41_TOTAL_GENERAL_PROVISIONS(BigDecimal r41_TOTAL_GENERAL_PROVISIONS) {
		R41_TOTAL_GENERAL_PROVISIONS = r41_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}
	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}
	public BigDecimal getR42_STAGE1_PROVISIONS() {
		return R42_STAGE1_PROVISIONS;
	}
	public void setR42_STAGE1_PROVISIONS(BigDecimal r42_STAGE1_PROVISIONS) {
		R42_STAGE1_PROVISIONS = r42_STAGE1_PROVISIONS;
	}
	public BigDecimal getR42_QUALIFY_STAGE2_PROVISIONS() {
		return R42_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR42_QUALIFY_STAGE2_PROVISIONS(BigDecimal r42_QUALIFY_STAGE2_PROVISIONS) {
		R42_QUALIFY_STAGE2_PROVISIONS = r42_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR42_TOTAL_GENERAL_PROVISIONS() {
		return R42_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR42_TOTAL_GENERAL_PROVISIONS(BigDecimal r42_TOTAL_GENERAL_PROVISIONS) {
		R42_TOTAL_GENERAL_PROVISIONS = r42_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}
	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}
	public BigDecimal getR43_STAGE1_PROVISIONS() {
		return R43_STAGE1_PROVISIONS;
	}
	public void setR43_STAGE1_PROVISIONS(BigDecimal r43_STAGE1_PROVISIONS) {
		R43_STAGE1_PROVISIONS = r43_STAGE1_PROVISIONS;
	}
	public BigDecimal getR43_QUALIFY_STAGE2_PROVISIONS() {
		return R43_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR43_QUALIFY_STAGE2_PROVISIONS(BigDecimal r43_QUALIFY_STAGE2_PROVISIONS) {
		R43_QUALIFY_STAGE2_PROVISIONS = r43_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR43_TOTAL_GENERAL_PROVISIONS() {
		return R43_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR43_TOTAL_GENERAL_PROVISIONS(BigDecimal r43_TOTAL_GENERAL_PROVISIONS) {
		R43_TOTAL_GENERAL_PROVISIONS = r43_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR44_PRODUCT() {
		return R44_PRODUCT;
	}
	public void setR44_PRODUCT(String r44_PRODUCT) {
		R44_PRODUCT = r44_PRODUCT;
	}
	public BigDecimal getR44_STAGE1_PROVISIONS() {
		return R44_STAGE1_PROVISIONS;
	}
	public void setR44_STAGE1_PROVISIONS(BigDecimal r44_STAGE1_PROVISIONS) {
		R44_STAGE1_PROVISIONS = r44_STAGE1_PROVISIONS;
	}
	public BigDecimal getR44_QUALIFY_STAGE2_PROVISIONS() {
		return R44_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR44_QUALIFY_STAGE2_PROVISIONS(BigDecimal r44_QUALIFY_STAGE2_PROVISIONS) {
		R44_QUALIFY_STAGE2_PROVISIONS = r44_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR44_TOTAL_GENERAL_PROVISIONS() {
		return R44_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR44_TOTAL_GENERAL_PROVISIONS(BigDecimal r44_TOTAL_GENERAL_PROVISIONS) {
		R44_TOTAL_GENERAL_PROVISIONS = r44_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR45_PRODUCT() {
		return R45_PRODUCT;
	}
	public void setR45_PRODUCT(String r45_PRODUCT) {
		R45_PRODUCT = r45_PRODUCT;
	}
	public BigDecimal getR45_STAGE1_PROVISIONS() {
		return R45_STAGE1_PROVISIONS;
	}
	public void setR45_STAGE1_PROVISIONS(BigDecimal r45_STAGE1_PROVISIONS) {
		R45_STAGE1_PROVISIONS = r45_STAGE1_PROVISIONS;
	}
	public BigDecimal getR45_QUALIFY_STAGE2_PROVISIONS() {
		return R45_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR45_QUALIFY_STAGE2_PROVISIONS(BigDecimal r45_QUALIFY_STAGE2_PROVISIONS) {
		R45_QUALIFY_STAGE2_PROVISIONS = r45_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR45_TOTAL_GENERAL_PROVISIONS() {
		return R45_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR45_TOTAL_GENERAL_PROVISIONS(BigDecimal r45_TOTAL_GENERAL_PROVISIONS) {
		R45_TOTAL_GENERAL_PROVISIONS = r45_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR46_PRODUCT() {
		return R46_PRODUCT;
	}
	public void setR46_PRODUCT(String r46_PRODUCT) {
		R46_PRODUCT = r46_PRODUCT;
	}
	public BigDecimal getR46_STAGE1_PROVISIONS() {
		return R46_STAGE1_PROVISIONS;
	}
	public void setR46_STAGE1_PROVISIONS(BigDecimal r46_STAGE1_PROVISIONS) {
		R46_STAGE1_PROVISIONS = r46_STAGE1_PROVISIONS;
	}
	public BigDecimal getR46_QUALIFY_STAGE2_PROVISIONS() {
		return R46_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR46_QUALIFY_STAGE2_PROVISIONS(BigDecimal r46_QUALIFY_STAGE2_PROVISIONS) {
		R46_QUALIFY_STAGE2_PROVISIONS = r46_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR46_TOTAL_GENERAL_PROVISIONS() {
		return R46_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR46_TOTAL_GENERAL_PROVISIONS(BigDecimal r46_TOTAL_GENERAL_PROVISIONS) {
		R46_TOTAL_GENERAL_PROVISIONS = r46_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}
	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}
	public BigDecimal getR47_STAGE1_PROVISIONS() {
		return R47_STAGE1_PROVISIONS;
	}
	public void setR47_STAGE1_PROVISIONS(BigDecimal r47_STAGE1_PROVISIONS) {
		R47_STAGE1_PROVISIONS = r47_STAGE1_PROVISIONS;
	}
	public BigDecimal getR47_QUALIFY_STAGE2_PROVISIONS() {
		return R47_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR47_QUALIFY_STAGE2_PROVISIONS(BigDecimal r47_QUALIFY_STAGE2_PROVISIONS) {
		R47_QUALIFY_STAGE2_PROVISIONS = r47_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR47_TOTAL_GENERAL_PROVISIONS() {
		return R47_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR47_TOTAL_GENERAL_PROVISIONS(BigDecimal r47_TOTAL_GENERAL_PROVISIONS) {
		R47_TOTAL_GENERAL_PROVISIONS = r47_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR48_PRODUCT() {
		return R48_PRODUCT;
	}
	public void setR48_PRODUCT(String r48_PRODUCT) {
		R48_PRODUCT = r48_PRODUCT;
	}
	public BigDecimal getR48_STAGE1_PROVISIONS() {
		return R48_STAGE1_PROVISIONS;
	}
	public void setR48_STAGE1_PROVISIONS(BigDecimal r48_STAGE1_PROVISIONS) {
		R48_STAGE1_PROVISIONS = r48_STAGE1_PROVISIONS;
	}
	public BigDecimal getR48_QUALIFY_STAGE2_PROVISIONS() {
		return R48_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR48_QUALIFY_STAGE2_PROVISIONS(BigDecimal r48_QUALIFY_STAGE2_PROVISIONS) {
		R48_QUALIFY_STAGE2_PROVISIONS = r48_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR48_TOTAL_GENERAL_PROVISIONS() {
		return R48_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR48_TOTAL_GENERAL_PROVISIONS(BigDecimal r48_TOTAL_GENERAL_PROVISIONS) {
		R48_TOTAL_GENERAL_PROVISIONS = r48_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR49_PRODUCT() {
		return R49_PRODUCT;
	}
	public void setR49_PRODUCT(String r49_PRODUCT) {
		R49_PRODUCT = r49_PRODUCT;
	}
	public BigDecimal getR49_STAGE1_PROVISIONS() {
		return R49_STAGE1_PROVISIONS;
	}
	public void setR49_STAGE1_PROVISIONS(BigDecimal r49_STAGE1_PROVISIONS) {
		R49_STAGE1_PROVISIONS = r49_STAGE1_PROVISIONS;
	}
	public BigDecimal getR49_QUALIFY_STAGE2_PROVISIONS() {
		return R49_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR49_QUALIFY_STAGE2_PROVISIONS(BigDecimal r49_QUALIFY_STAGE2_PROVISIONS) {
		R49_QUALIFY_STAGE2_PROVISIONS = r49_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR49_TOTAL_GENERAL_PROVISIONS() {
		return R49_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR49_TOTAL_GENERAL_PROVISIONS(BigDecimal r49_TOTAL_GENERAL_PROVISIONS) {
		R49_TOTAL_GENERAL_PROVISIONS = r49_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR50_PRODUCT() {
		return R50_PRODUCT;
	}
	public void setR50_PRODUCT(String r50_PRODUCT) {
		R50_PRODUCT = r50_PRODUCT;
	}
	public BigDecimal getR50_STAGE1_PROVISIONS() {
		return R50_STAGE1_PROVISIONS;
	}
	public void setR50_STAGE1_PROVISIONS(BigDecimal r50_STAGE1_PROVISIONS) {
		R50_STAGE1_PROVISIONS = r50_STAGE1_PROVISIONS;
	}
	public BigDecimal getR50_QUALIFY_STAGE2_PROVISIONS() {
		return R50_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR50_QUALIFY_STAGE2_PROVISIONS(BigDecimal r50_QUALIFY_STAGE2_PROVISIONS) {
		R50_QUALIFY_STAGE2_PROVISIONS = r50_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR50_TOTAL_GENERAL_PROVISIONS() {
		return R50_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR50_TOTAL_GENERAL_PROVISIONS(BigDecimal r50_TOTAL_GENERAL_PROVISIONS) {
		R50_TOTAL_GENERAL_PROVISIONS = r50_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR51_PRODUCT() {
		return R51_PRODUCT;
	}
	public void setR51_PRODUCT(String r51_PRODUCT) {
		R51_PRODUCT = r51_PRODUCT;
	}
	public BigDecimal getR51_STAGE1_PROVISIONS() {
		return R51_STAGE1_PROVISIONS;
	}
	public void setR51_STAGE1_PROVISIONS(BigDecimal r51_STAGE1_PROVISIONS) {
		R51_STAGE1_PROVISIONS = r51_STAGE1_PROVISIONS;
	}
	public BigDecimal getR51_QUALIFY_STAGE2_PROVISIONS() {
		return R51_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR51_QUALIFY_STAGE2_PROVISIONS(BigDecimal r51_QUALIFY_STAGE2_PROVISIONS) {
		R51_QUALIFY_STAGE2_PROVISIONS = r51_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR51_TOTAL_GENERAL_PROVISIONS() {
		return R51_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR51_TOTAL_GENERAL_PROVISIONS(BigDecimal r51_TOTAL_GENERAL_PROVISIONS) {
		R51_TOTAL_GENERAL_PROVISIONS = r51_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR52_PRODUCT() {
		return R52_PRODUCT;
	}
	public void setR52_PRODUCT(String r52_PRODUCT) {
		R52_PRODUCT = r52_PRODUCT;
	}
	public BigDecimal getR52_STAGE1_PROVISIONS() {
		return R52_STAGE1_PROVISIONS;
	}
	public void setR52_STAGE1_PROVISIONS(BigDecimal r52_STAGE1_PROVISIONS) {
		R52_STAGE1_PROVISIONS = r52_STAGE1_PROVISIONS;
	}
	public BigDecimal getR52_QUALIFY_STAGE2_PROVISIONS() {
		return R52_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR52_QUALIFY_STAGE2_PROVISIONS(BigDecimal r52_QUALIFY_STAGE2_PROVISIONS) {
		R52_QUALIFY_STAGE2_PROVISIONS = r52_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR52_TOTAL_GENERAL_PROVISIONS() {
		return R52_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR52_TOTAL_GENERAL_PROVISIONS(BigDecimal r52_TOTAL_GENERAL_PROVISIONS) {
		R52_TOTAL_GENERAL_PROVISIONS = r52_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR53_PRODUCT() {
		return R53_PRODUCT;
	}
	public void setR53_PRODUCT(String r53_PRODUCT) {
		R53_PRODUCT = r53_PRODUCT;
	}
	public BigDecimal getR53_STAGE1_PROVISIONS() {
		return R53_STAGE1_PROVISIONS;
	}
	public void setR53_STAGE1_PROVISIONS(BigDecimal r53_STAGE1_PROVISIONS) {
		R53_STAGE1_PROVISIONS = r53_STAGE1_PROVISIONS;
	}
	public BigDecimal getR53_QUALIFY_STAGE2_PROVISIONS() {
		return R53_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR53_QUALIFY_STAGE2_PROVISIONS(BigDecimal r53_QUALIFY_STAGE2_PROVISIONS) {
		R53_QUALIFY_STAGE2_PROVISIONS = r53_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR53_TOTAL_GENERAL_PROVISIONS() {
		return R53_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR53_TOTAL_GENERAL_PROVISIONS(BigDecimal r53_TOTAL_GENERAL_PROVISIONS) {
		R53_TOTAL_GENERAL_PROVISIONS = r53_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR54_PRODUCT() {
		return R54_PRODUCT;
	}
	public void setR54_PRODUCT(String r54_PRODUCT) {
		R54_PRODUCT = r54_PRODUCT;
	}
	public BigDecimal getR54_STAGE1_PROVISIONS() {
		return R54_STAGE1_PROVISIONS;
	}
	public void setR54_STAGE1_PROVISIONS(BigDecimal r54_STAGE1_PROVISIONS) {
		R54_STAGE1_PROVISIONS = r54_STAGE1_PROVISIONS;
	}
	public BigDecimal getR54_QUALIFY_STAGE2_PROVISIONS() {
		return R54_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR54_QUALIFY_STAGE2_PROVISIONS(BigDecimal r54_QUALIFY_STAGE2_PROVISIONS) {
		R54_QUALIFY_STAGE2_PROVISIONS = r54_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR54_TOTAL_GENERAL_PROVISIONS() {
		return R54_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR54_TOTAL_GENERAL_PROVISIONS(BigDecimal r54_TOTAL_GENERAL_PROVISIONS) {
		R54_TOTAL_GENERAL_PROVISIONS = r54_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR55_PRODUCT() {
		return R55_PRODUCT;
	}
	public void setR55_PRODUCT(String r55_PRODUCT) {
		R55_PRODUCT = r55_PRODUCT;
	}
	public BigDecimal getR55_STAGE1_PROVISIONS() {
		return R55_STAGE1_PROVISIONS;
	}
	public void setR55_STAGE1_PROVISIONS(BigDecimal r55_STAGE1_PROVISIONS) {
		R55_STAGE1_PROVISIONS = r55_STAGE1_PROVISIONS;
	}
	public BigDecimal getR55_QUALIFY_STAGE2_PROVISIONS() {
		return R55_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR55_QUALIFY_STAGE2_PROVISIONS(BigDecimal r55_QUALIFY_STAGE2_PROVISIONS) {
		R55_QUALIFY_STAGE2_PROVISIONS = r55_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR55_TOTAL_GENERAL_PROVISIONS() {
		return R55_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR55_TOTAL_GENERAL_PROVISIONS(BigDecimal r55_TOTAL_GENERAL_PROVISIONS) {
		R55_TOTAL_GENERAL_PROVISIONS = r55_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR56_PRODUCT() {
		return R56_PRODUCT;
	}
	public void setR56_PRODUCT(String r56_PRODUCT) {
		R56_PRODUCT = r56_PRODUCT;
	}
	public BigDecimal getR56_STAGE1_PROVISIONS() {
		return R56_STAGE1_PROVISIONS;
	}
	public void setR56_STAGE1_PROVISIONS(BigDecimal r56_STAGE1_PROVISIONS) {
		R56_STAGE1_PROVISIONS = r56_STAGE1_PROVISIONS;
	}
	public BigDecimal getR56_QUALIFY_STAGE2_PROVISIONS() {
		return R56_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR56_QUALIFY_STAGE2_PROVISIONS(BigDecimal r56_QUALIFY_STAGE2_PROVISIONS) {
		R56_QUALIFY_STAGE2_PROVISIONS = r56_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR56_TOTAL_GENERAL_PROVISIONS() {
		return R56_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR56_TOTAL_GENERAL_PROVISIONS(BigDecimal r56_TOTAL_GENERAL_PROVISIONS) {
		R56_TOTAL_GENERAL_PROVISIONS = r56_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR57_PRODUCT() {
		return R57_PRODUCT;
	}
	public void setR57_PRODUCT(String r57_PRODUCT) {
		R57_PRODUCT = r57_PRODUCT;
	}
	public BigDecimal getR57_STAGE1_PROVISIONS() {
		return R57_STAGE1_PROVISIONS;
	}
	public void setR57_STAGE1_PROVISIONS(BigDecimal r57_STAGE1_PROVISIONS) {
		R57_STAGE1_PROVISIONS = r57_STAGE1_PROVISIONS;
	}
	public BigDecimal getR57_QUALIFY_STAGE2_PROVISIONS() {
		return R57_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR57_QUALIFY_STAGE2_PROVISIONS(BigDecimal r57_QUALIFY_STAGE2_PROVISIONS) {
		R57_QUALIFY_STAGE2_PROVISIONS = r57_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR57_TOTAL_GENERAL_PROVISIONS() {
		return R57_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR57_TOTAL_GENERAL_PROVISIONS(BigDecimal r57_TOTAL_GENERAL_PROVISIONS) {
		R57_TOTAL_GENERAL_PROVISIONS = r57_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR58_PRODUCT() {
		return R58_PRODUCT;
	}
	public void setR58_PRODUCT(String r58_PRODUCT) {
		R58_PRODUCT = r58_PRODUCT;
	}
	public BigDecimal getR58_STAGE1_PROVISIONS() {
		return R58_STAGE1_PROVISIONS;
	}
	public void setR58_STAGE1_PROVISIONS(BigDecimal r58_STAGE1_PROVISIONS) {
		R58_STAGE1_PROVISIONS = r58_STAGE1_PROVISIONS;
	}
	public BigDecimal getR58_QUALIFY_STAGE2_PROVISIONS() {
		return R58_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR58_QUALIFY_STAGE2_PROVISIONS(BigDecimal r58_QUALIFY_STAGE2_PROVISIONS) {
		R58_QUALIFY_STAGE2_PROVISIONS = r58_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR58_TOTAL_GENERAL_PROVISIONS() {
		return R58_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR58_TOTAL_GENERAL_PROVISIONS(BigDecimal r58_TOTAL_GENERAL_PROVISIONS) {
		R58_TOTAL_GENERAL_PROVISIONS = r58_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR59_PRODUCT() {
		return R59_PRODUCT;
	}
	public void setR59_PRODUCT(String r59_PRODUCT) {
		R59_PRODUCT = r59_PRODUCT;
	}
	public BigDecimal getR59_STAGE1_PROVISIONS() {
		return R59_STAGE1_PROVISIONS;
	}
	public void setR59_STAGE1_PROVISIONS(BigDecimal r59_STAGE1_PROVISIONS) {
		R59_STAGE1_PROVISIONS = r59_STAGE1_PROVISIONS;
	}
	public BigDecimal getR59_QUALIFY_STAGE2_PROVISIONS() {
		return R59_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR59_QUALIFY_STAGE2_PROVISIONS(BigDecimal r59_QUALIFY_STAGE2_PROVISIONS) {
		R59_QUALIFY_STAGE2_PROVISIONS = r59_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR59_TOTAL_GENERAL_PROVISIONS() {
		return R59_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR59_TOTAL_GENERAL_PROVISIONS(BigDecimal r59_TOTAL_GENERAL_PROVISIONS) {
		R59_TOTAL_GENERAL_PROVISIONS = r59_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR60_PRODUCT() {
		return R60_PRODUCT;
	}
	public void setR60_PRODUCT(String r60_PRODUCT) {
		R60_PRODUCT = r60_PRODUCT;
	}
	public BigDecimal getR60_STAGE1_PROVISIONS() {
		return R60_STAGE1_PROVISIONS;
	}
	public void setR60_STAGE1_PROVISIONS(BigDecimal r60_STAGE1_PROVISIONS) {
		R60_STAGE1_PROVISIONS = r60_STAGE1_PROVISIONS;
	}
	public BigDecimal getR60_QUALIFY_STAGE2_PROVISIONS() {
		return R60_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR60_QUALIFY_STAGE2_PROVISIONS(BigDecimal r60_QUALIFY_STAGE2_PROVISIONS) {
		R60_QUALIFY_STAGE2_PROVISIONS = r60_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR60_TOTAL_GENERAL_PROVISIONS() {
		return R60_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR60_TOTAL_GENERAL_PROVISIONS(BigDecimal r60_TOTAL_GENERAL_PROVISIONS) {
		R60_TOTAL_GENERAL_PROVISIONS = r60_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR61_PRODUCT() {
		return R61_PRODUCT;
	}
	public void setR61_PRODUCT(String r61_PRODUCT) {
		R61_PRODUCT = r61_PRODUCT;
	}
	public BigDecimal getR61_STAGE1_PROVISIONS() {
		return R61_STAGE1_PROVISIONS;
	}
	public void setR61_STAGE1_PROVISIONS(BigDecimal r61_STAGE1_PROVISIONS) {
		R61_STAGE1_PROVISIONS = r61_STAGE1_PROVISIONS;
	}
	public BigDecimal getR61_QUALIFY_STAGE2_PROVISIONS() {
		return R61_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR61_QUALIFY_STAGE2_PROVISIONS(BigDecimal r61_QUALIFY_STAGE2_PROVISIONS) {
		R61_QUALIFY_STAGE2_PROVISIONS = r61_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR61_TOTAL_GENERAL_PROVISIONS() {
		return R61_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR61_TOTAL_GENERAL_PROVISIONS(BigDecimal r61_TOTAL_GENERAL_PROVISIONS) {
		R61_TOTAL_GENERAL_PROVISIONS = r61_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR62_PRODUCT() {
		return R62_PRODUCT;
	}
	public void setR62_PRODUCT(String r62_PRODUCT) {
		R62_PRODUCT = r62_PRODUCT;
	}
	public BigDecimal getR62_STAGE1_PROVISIONS() {
		return R62_STAGE1_PROVISIONS;
	}
	public void setR62_STAGE1_PROVISIONS(BigDecimal r62_STAGE1_PROVISIONS) {
		R62_STAGE1_PROVISIONS = r62_STAGE1_PROVISIONS;
	}
	public BigDecimal getR62_QUALIFY_STAGE2_PROVISIONS() {
		return R62_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR62_QUALIFY_STAGE2_PROVISIONS(BigDecimal r62_QUALIFY_STAGE2_PROVISIONS) {
		R62_QUALIFY_STAGE2_PROVISIONS = r62_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR62_TOTAL_GENERAL_PROVISIONS() {
		return R62_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR62_TOTAL_GENERAL_PROVISIONS(BigDecimal r62_TOTAL_GENERAL_PROVISIONS) {
		R62_TOTAL_GENERAL_PROVISIONS = r62_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR63_PRODUCT() {
		return R63_PRODUCT;
	}
	public void setR63_PRODUCT(String r63_PRODUCT) {
		R63_PRODUCT = r63_PRODUCT;
	}
	public BigDecimal getR63_STAGE1_PROVISIONS() {
		return R63_STAGE1_PROVISIONS;
	}
	public void setR63_STAGE1_PROVISIONS(BigDecimal r63_STAGE1_PROVISIONS) {
		R63_STAGE1_PROVISIONS = r63_STAGE1_PROVISIONS;
	}
	public BigDecimal getR63_QUALIFY_STAGE2_PROVISIONS() {
		return R63_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR63_QUALIFY_STAGE2_PROVISIONS(BigDecimal r63_QUALIFY_STAGE2_PROVISIONS) {
		R63_QUALIFY_STAGE2_PROVISIONS = r63_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR63_TOTAL_GENERAL_PROVISIONS() {
		return R63_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR63_TOTAL_GENERAL_PROVISIONS(BigDecimal r63_TOTAL_GENERAL_PROVISIONS) {
		R63_TOTAL_GENERAL_PROVISIONS = r63_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR64_PRODUCT() {
		return R64_PRODUCT;
	}
	public void setR64_PRODUCT(String r64_PRODUCT) {
		R64_PRODUCT = r64_PRODUCT;
	}
	public BigDecimal getR64_STAGE1_PROVISIONS() {
		return R64_STAGE1_PROVISIONS;
	}
	public void setR64_STAGE1_PROVISIONS(BigDecimal r64_STAGE1_PROVISIONS) {
		R64_STAGE1_PROVISIONS = r64_STAGE1_PROVISIONS;
	}
	public BigDecimal getR64_QUALIFY_STAGE2_PROVISIONS() {
		return R64_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR64_QUALIFY_STAGE2_PROVISIONS(BigDecimal r64_QUALIFY_STAGE2_PROVISIONS) {
		R64_QUALIFY_STAGE2_PROVISIONS = r64_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR64_TOTAL_GENERAL_PROVISIONS() {
		return R64_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR64_TOTAL_GENERAL_PROVISIONS(BigDecimal r64_TOTAL_GENERAL_PROVISIONS) {
		R64_TOTAL_GENERAL_PROVISIONS = r64_TOTAL_GENERAL_PROVISIONS;
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


public class M_GP_Archival_Summary_RowMapper
        implements RowMapper<M_GP_Archival_Summary_Entity> {

    @Override
    public M_GP_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        M_GP_Archival_Summary_Entity obj = new M_GP_Archival_Summary_Entity();

// =========================
// R11
// =========================
obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
obj.setR11_STAGE1_PROVISIONS(rs.getBigDecimal("R11_STAGE1_PROVISIONS"));
obj.setR11_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R11_QUALIFY_STAGE2_PROVISIONS"));
obj.setR11_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R11_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R12
// =========================
obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
obj.setR12_STAGE1_PROVISIONS(rs.getBigDecimal("R12_STAGE1_PROVISIONS"));
obj.setR12_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R12_QUALIFY_STAGE2_PROVISIONS"));
obj.setR12_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R12_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R13
// =========================
obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
obj.setR13_STAGE1_PROVISIONS(rs.getBigDecimal("R13_STAGE1_PROVISIONS"));
obj.setR13_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R13_QUALIFY_STAGE2_PROVISIONS"));
obj.setR13_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R13_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R14
// =========================
obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
obj.setR14_STAGE1_PROVISIONS(rs.getBigDecimal("R14_STAGE1_PROVISIONS"));
obj.setR14_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R14_QUALIFY_STAGE2_PROVISIONS"));
obj.setR14_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R14_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R15
// =========================
obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
obj.setR15_STAGE1_PROVISIONS(rs.getBigDecimal("R15_STAGE1_PROVISIONS"));
obj.setR15_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R15_QUALIFY_STAGE2_PROVISIONS"));
obj.setR15_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R15_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R16
// =========================
obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
obj.setR16_STAGE1_PROVISIONS(rs.getBigDecimal("R16_STAGE1_PROVISIONS"));
obj.setR16_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R16_QUALIFY_STAGE2_PROVISIONS"));
obj.setR16_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R16_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R17
// =========================
obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
obj.setR17_STAGE1_PROVISIONS(rs.getBigDecimal("R17_STAGE1_PROVISIONS"));
obj.setR17_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R17_QUALIFY_STAGE2_PROVISIONS"));
obj.setR17_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R17_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R18
// =========================
obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
obj.setR18_STAGE1_PROVISIONS(rs.getBigDecimal("R18_STAGE1_PROVISIONS"));
obj.setR18_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R18_QUALIFY_STAGE2_PROVISIONS"));
obj.setR18_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R18_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R19
// =========================
obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
obj.setR19_STAGE1_PROVISIONS(rs.getBigDecimal("R19_STAGE1_PROVISIONS"));
obj.setR19_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R19_QUALIFY_STAGE2_PROVISIONS"));
obj.setR19_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R19_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R20
// =========================
obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
obj.setR20_STAGE1_PROVISIONS(rs.getBigDecimal("R20_STAGE1_PROVISIONS"));
obj.setR20_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R20_QUALIFY_STAGE2_PROVISIONS"));
obj.setR20_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R20_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R21
// =========================
obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
obj.setR21_STAGE1_PROVISIONS(rs.getBigDecimal("R21_STAGE1_PROVISIONS"));
obj.setR21_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R21_QUALIFY_STAGE2_PROVISIONS"));
obj.setR21_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R21_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R22
// =========================
obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
obj.setR22_STAGE1_PROVISIONS(rs.getBigDecimal("R22_STAGE1_PROVISIONS"));
obj.setR22_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R22_QUALIFY_STAGE2_PROVISIONS"));
obj.setR22_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R22_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R23
// =========================
obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
obj.setR23_STAGE1_PROVISIONS(rs.getBigDecimal("R23_STAGE1_PROVISIONS"));
obj.setR23_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R23_QUALIFY_STAGE2_PROVISIONS"));
obj.setR23_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R23_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R24
// =========================
obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
obj.setR24_STAGE1_PROVISIONS(rs.getBigDecimal("R24_STAGE1_PROVISIONS"));
obj.setR24_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R24_QUALIFY_STAGE2_PROVISIONS"));
obj.setR24_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R24_TOTAL_GENERAL_PROVISIONS"));


// =========================
// R25
// =========================
obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
obj.setR25_STAGE1_PROVISIONS(rs.getBigDecimal("R25_STAGE1_PROVISIONS"));
obj.setR25_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R25_QUALIFY_STAGE2_PROVISIONS"));
obj.setR25_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R25_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R26
// =========================
obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
obj.setR26_STAGE1_PROVISIONS(rs.getBigDecimal("R26_STAGE1_PROVISIONS"));
obj.setR26_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R26_QUALIFY_STAGE2_PROVISIONS"));
obj.setR26_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R26_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R27
// =========================
obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
obj.setR27_STAGE1_PROVISIONS(rs.getBigDecimal("R27_STAGE1_PROVISIONS"));
obj.setR27_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R27_QUALIFY_STAGE2_PROVISIONS"));
obj.setR27_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R27_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R28
// =========================
obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
obj.setR28_STAGE1_PROVISIONS(rs.getBigDecimal("R28_STAGE1_PROVISIONS"));
obj.setR28_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R28_QUALIFY_STAGE2_PROVISIONS"));
obj.setR28_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R28_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R29
// =========================
obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
obj.setR29_STAGE1_PROVISIONS(rs.getBigDecimal("R29_STAGE1_PROVISIONS"));
obj.setR29_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R29_QUALIFY_STAGE2_PROVISIONS"));
obj.setR29_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R29_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R30
// =========================
obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
obj.setR30_STAGE1_PROVISIONS(rs.getBigDecimal("R30_STAGE1_PROVISIONS"));
obj.setR30_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R30_QUALIFY_STAGE2_PROVISIONS"));
obj.setR30_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R30_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R31
// =========================
obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
obj.setR31_STAGE1_PROVISIONS(rs.getBigDecimal("R31_STAGE1_PROVISIONS"));
obj.setR31_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R31_QUALIFY_STAGE2_PROVISIONS"));
obj.setR31_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R31_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R32
// =========================
obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
obj.setR32_STAGE1_PROVISIONS(rs.getBigDecimal("R32_STAGE1_PROVISIONS"));
obj.setR32_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R32_QUALIFY_STAGE2_PROVISIONS"));
obj.setR32_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R32_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R33
// =========================
obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
obj.setR33_STAGE1_PROVISIONS(rs.getBigDecimal("R33_STAGE1_PROVISIONS"));
obj.setR33_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R33_QUALIFY_STAGE2_PROVISIONS"));
obj.setR33_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R33_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R34
// =========================
obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
obj.setR34_STAGE1_PROVISIONS(rs.getBigDecimal("R34_STAGE1_PROVISIONS"));
obj.setR34_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R34_QUALIFY_STAGE2_PROVISIONS"));
obj.setR34_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R34_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R35
// =========================
obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
obj.setR35_STAGE1_PROVISIONS(rs.getBigDecimal("R35_STAGE1_PROVISIONS"));
obj.setR35_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R35_QUALIFY_STAGE2_PROVISIONS"));
obj.setR35_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R35_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R36
// =========================
obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
obj.setR36_STAGE1_PROVISIONS(rs.getBigDecimal("R36_STAGE1_PROVISIONS"));
obj.setR36_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R36_QUALIFY_STAGE2_PROVISIONS"));
obj.setR36_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R36_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R37
// =========================
obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
obj.setR37_STAGE1_PROVISIONS(rs.getBigDecimal("R37_STAGE1_PROVISIONS"));
obj.setR37_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R37_QUALIFY_STAGE2_PROVISIONS"));
obj.setR37_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R37_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R38
// =========================
obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
obj.setR38_STAGE1_PROVISIONS(rs.getBigDecimal("R38_STAGE1_PROVISIONS"));
obj.setR38_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R38_QUALIFY_STAGE2_PROVISIONS"));
obj.setR38_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R38_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R39
// =========================
obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
obj.setR39_STAGE1_PROVISIONS(rs.getBigDecimal("R39_STAGE1_PROVISIONS"));
obj.setR39_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R39_QUALIFY_STAGE2_PROVISIONS"));
obj.setR39_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R39_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R40
// =========================
obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
obj.setR40_STAGE1_PROVISIONS(rs.getBigDecimal("R40_STAGE1_PROVISIONS"));
obj.setR40_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R40_QUALIFY_STAGE2_PROVISIONS"));
obj.setR40_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R40_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R41
// =========================
obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
obj.setR41_STAGE1_PROVISIONS(rs.getBigDecimal("R41_STAGE1_PROVISIONS"));
obj.setR41_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R41_QUALIFY_STAGE2_PROVISIONS"));
obj.setR41_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R41_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R42
// =========================
obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
obj.setR42_STAGE1_PROVISIONS(rs.getBigDecimal("R42_STAGE1_PROVISIONS"));
obj.setR42_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R42_QUALIFY_STAGE2_PROVISIONS"));
obj.setR42_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R42_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R43
// =========================
obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
obj.setR43_STAGE1_PROVISIONS(rs.getBigDecimal("R43_STAGE1_PROVISIONS"));
obj.setR43_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R43_QUALIFY_STAGE2_PROVISIONS"));
obj.setR43_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R43_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R44
// =========================
obj.setR44_PRODUCT(rs.getString("R44_PRODUCT"));
obj.setR44_STAGE1_PROVISIONS(rs.getBigDecimal("R44_STAGE1_PROVISIONS"));
obj.setR44_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R44_QUALIFY_STAGE2_PROVISIONS"));
obj.setR44_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R44_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R45
// =========================
obj.setR45_PRODUCT(rs.getString("R45_PRODUCT"));
obj.setR45_STAGE1_PROVISIONS(rs.getBigDecimal("R45_STAGE1_PROVISIONS"));
obj.setR45_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R45_QUALIFY_STAGE2_PROVISIONS"));
obj.setR45_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R45_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R46
// =========================
obj.setR46_PRODUCT(rs.getString("R46_PRODUCT"));
obj.setR46_STAGE1_PROVISIONS(rs.getBigDecimal("R46_STAGE1_PROVISIONS"));
obj.setR46_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R46_QUALIFY_STAGE2_PROVISIONS"));
obj.setR46_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R46_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R47
// =========================
obj.setR47_PRODUCT(rs.getString("R47_PRODUCT"));
obj.setR47_STAGE1_PROVISIONS(rs.getBigDecimal("R47_STAGE1_PROVISIONS"));
obj.setR47_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R47_QUALIFY_STAGE2_PROVISIONS"));
obj.setR47_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R47_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R48
// =========================
obj.setR48_PRODUCT(rs.getString("R48_PRODUCT"));
obj.setR48_STAGE1_PROVISIONS(rs.getBigDecimal("R48_STAGE1_PROVISIONS"));
obj.setR48_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R48_QUALIFY_STAGE2_PROVISIONS"));
obj.setR48_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R48_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R49
// =========================
obj.setR49_PRODUCT(rs.getString("R49_PRODUCT"));
obj.setR49_STAGE1_PROVISIONS(rs.getBigDecimal("R49_STAGE1_PROVISIONS"));
obj.setR49_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R49_QUALIFY_STAGE2_PROVISIONS"));
obj.setR49_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R49_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R50
// =========================
obj.setR50_PRODUCT(rs.getString("R50_PRODUCT"));
obj.setR50_STAGE1_PROVISIONS(rs.getBigDecimal("R50_STAGE1_PROVISIONS"));
obj.setR50_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R50_QUALIFY_STAGE2_PROVISIONS"));
obj.setR50_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R50_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R51
// =========================
obj.setR51_PRODUCT(rs.getString("R51_PRODUCT"));
obj.setR51_STAGE1_PROVISIONS(rs.getBigDecimal("R51_STAGE1_PROVISIONS"));
obj.setR51_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R51_QUALIFY_STAGE2_PROVISIONS"));
obj.setR51_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R51_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R52
// =========================
obj.setR52_PRODUCT(rs.getString("R52_PRODUCT"));
obj.setR52_STAGE1_PROVISIONS(rs.getBigDecimal("R52_STAGE1_PROVISIONS"));
obj.setR52_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R52_QUALIFY_STAGE2_PROVISIONS"));
obj.setR52_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R52_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R53
// =========================
obj.setR53_PRODUCT(rs.getString("R53_PRODUCT"));
obj.setR53_STAGE1_PROVISIONS(rs.getBigDecimal("R53_STAGE1_PROVISIONS"));
obj.setR53_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R53_QUALIFY_STAGE2_PROVISIONS"));
obj.setR53_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R53_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R54
// =========================
obj.setR54_PRODUCT(rs.getString("R54_PRODUCT"));
obj.setR54_STAGE1_PROVISIONS(rs.getBigDecimal("R54_STAGE1_PROVISIONS"));
obj.setR54_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R54_QUALIFY_STAGE2_PROVISIONS"));
obj.setR54_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R54_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R55
// =========================
obj.setR55_PRODUCT(rs.getString("R55_PRODUCT"));
obj.setR55_STAGE1_PROVISIONS(rs.getBigDecimal("R55_STAGE1_PROVISIONS"));
obj.setR55_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R55_QUALIFY_STAGE2_PROVISIONS"));
obj.setR55_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R55_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R56
// =========================
obj.setR56_PRODUCT(rs.getString("R56_PRODUCT"));
obj.setR56_STAGE1_PROVISIONS(rs.getBigDecimal("R56_STAGE1_PROVISIONS"));
obj.setR56_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R56_QUALIFY_STAGE2_PROVISIONS"));
obj.setR56_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R56_TOTAL_GENERAL_PROVISIONS"));


// =========================
// R57
// =========================
obj.setR57_PRODUCT(rs.getString("R57_PRODUCT"));
obj.setR57_STAGE1_PROVISIONS(rs.getBigDecimal("R57_STAGE1_PROVISIONS"));
obj.setR57_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R57_QUALIFY_STAGE2_PROVISIONS"));
obj.setR57_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R57_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R58
// =========================
obj.setR58_PRODUCT(rs.getString("R58_PRODUCT"));
obj.setR58_STAGE1_PROVISIONS(rs.getBigDecimal("R58_STAGE1_PROVISIONS"));
obj.setR58_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R58_QUALIFY_STAGE2_PROVISIONS"));
obj.setR58_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R58_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R59
// =========================
obj.setR59_PRODUCT(rs.getString("R59_PRODUCT"));
obj.setR59_STAGE1_PROVISIONS(rs.getBigDecimal("R59_STAGE1_PROVISIONS"));
obj.setR59_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R59_QUALIFY_STAGE2_PROVISIONS"));
obj.setR59_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R59_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R60
// =========================
obj.setR60_PRODUCT(rs.getString("R60_PRODUCT"));
obj.setR60_STAGE1_PROVISIONS(rs.getBigDecimal("R60_STAGE1_PROVISIONS"));
obj.setR60_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R60_QUALIFY_STAGE2_PROVISIONS"));
obj.setR60_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R60_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R61
// =========================
obj.setR61_PRODUCT(rs.getString("R61_PRODUCT"));
obj.setR61_STAGE1_PROVISIONS(rs.getBigDecimal("R61_STAGE1_PROVISIONS"));
obj.setR61_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R61_QUALIFY_STAGE2_PROVISIONS"));
obj.setR61_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R61_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R62
// =========================
obj.setR62_PRODUCT(rs.getString("R62_PRODUCT"));
obj.setR62_STAGE1_PROVISIONS(rs.getBigDecimal("R62_STAGE1_PROVISIONS"));
obj.setR62_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R62_QUALIFY_STAGE2_PROVISIONS"));
obj.setR62_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R62_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R63
// =========================
obj.setR63_PRODUCT(rs.getString("R63_PRODUCT"));
obj.setR63_STAGE1_PROVISIONS(rs.getBigDecimal("R63_STAGE1_PROVISIONS"));
obj.setR63_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R63_QUALIFY_STAGE2_PROVISIONS"));
obj.setR63_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R63_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R64
// =========================
obj.setR64_PRODUCT(rs.getString("R64_PRODUCT"));
obj.setR64_STAGE1_PROVISIONS(rs.getBigDecimal("R64_STAGE1_PROVISIONS"));
obj.setR64_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R64_QUALIFY_STAGE2_PROVISIONS"));
obj.setR64_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R64_TOTAL_GENERAL_PROVISIONS"));

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


public class M_GP_Archival_Summary_Entity {
	
	
private String R11_PRODUCT;
	private BigDecimal R11_STAGE1_PROVISIONS;
	private BigDecimal R11_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R11_TOTAL_GENERAL_PROVISIONS;

	private String R12_PRODUCT;
	private BigDecimal R12_STAGE1_PROVISIONS;
	private BigDecimal R12_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R12_TOTAL_GENERAL_PROVISIONS;

	private String R13_PRODUCT;
	private BigDecimal R13_STAGE1_PROVISIONS;
	private BigDecimal R13_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R13_TOTAL_GENERAL_PROVISIONS;

	private String R14_PRODUCT;
	private BigDecimal R14_STAGE1_PROVISIONS;
	private BigDecimal R14_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R14_TOTAL_GENERAL_PROVISIONS;

	private String R15_PRODUCT;
	private BigDecimal R15_STAGE1_PROVISIONS;
	private BigDecimal R15_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R15_TOTAL_GENERAL_PROVISIONS;

	private String R16_PRODUCT;
	private BigDecimal R16_STAGE1_PROVISIONS;
	private BigDecimal R16_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R16_TOTAL_GENERAL_PROVISIONS;

	private String R17_PRODUCT;
	private BigDecimal R17_STAGE1_PROVISIONS;
	private BigDecimal R17_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R17_TOTAL_GENERAL_PROVISIONS;

	private String R18_PRODUCT;
	private BigDecimal R18_STAGE1_PROVISIONS;
	private BigDecimal R18_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R18_TOTAL_GENERAL_PROVISIONS;

	private String R19_PRODUCT;
	private BigDecimal R19_STAGE1_PROVISIONS;
	private BigDecimal R19_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R19_TOTAL_GENERAL_PROVISIONS;

	private String R20_PRODUCT;
	private BigDecimal R20_STAGE1_PROVISIONS;
	private BigDecimal R20_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R20_TOTAL_GENERAL_PROVISIONS;

	private String R21_PRODUCT;
	private BigDecimal R21_STAGE1_PROVISIONS;
	private BigDecimal R21_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R21_TOTAL_GENERAL_PROVISIONS;

	private String R22_PRODUCT;
	private BigDecimal R22_STAGE1_PROVISIONS;
	private BigDecimal R22_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R22_TOTAL_GENERAL_PROVISIONS;

	private String R23_PRODUCT;
	private BigDecimal R23_STAGE1_PROVISIONS;
	private BigDecimal R23_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R23_TOTAL_GENERAL_PROVISIONS;

	private String R24_PRODUCT;
	private BigDecimal R24_STAGE1_PROVISIONS;
	private BigDecimal R24_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R24_TOTAL_GENERAL_PROVISIONS;

	private String R25_PRODUCT;
	private BigDecimal R25_STAGE1_PROVISIONS;
	private BigDecimal R25_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R25_TOTAL_GENERAL_PROVISIONS;

	private String R26_PRODUCT;
	private BigDecimal R26_STAGE1_PROVISIONS;
	private BigDecimal R26_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R26_TOTAL_GENERAL_PROVISIONS;

	private String R27_PRODUCT;
	private BigDecimal R27_STAGE1_PROVISIONS;
	private BigDecimal R27_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R27_TOTAL_GENERAL_PROVISIONS;

	private String R28_PRODUCT;
	private BigDecimal R28_STAGE1_PROVISIONS;
	private BigDecimal R28_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R28_TOTAL_GENERAL_PROVISIONS;

	private String R29_PRODUCT;
	private BigDecimal R29_STAGE1_PROVISIONS;
	private BigDecimal R29_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R29_TOTAL_GENERAL_PROVISIONS;

	private String R30_PRODUCT;
	private BigDecimal R30_STAGE1_PROVISIONS;
	private BigDecimal R30_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R30_TOTAL_GENERAL_PROVISIONS;

	private String R31_PRODUCT;
	private BigDecimal R31_STAGE1_PROVISIONS;
	private BigDecimal R31_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R31_TOTAL_GENERAL_PROVISIONS;

	private String R32_PRODUCT;
	private BigDecimal R32_STAGE1_PROVISIONS;
	private BigDecimal R32_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R32_TOTAL_GENERAL_PROVISIONS;

	private String R33_PRODUCT;
	private BigDecimal R33_STAGE1_PROVISIONS;
	private BigDecimal R33_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R33_TOTAL_GENERAL_PROVISIONS;

	private String R34_PRODUCT;
	private BigDecimal R34_STAGE1_PROVISIONS;
	private BigDecimal R34_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R34_TOTAL_GENERAL_PROVISIONS;

	private String R35_PRODUCT;
	private BigDecimal R35_STAGE1_PROVISIONS;
	private BigDecimal R35_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R35_TOTAL_GENERAL_PROVISIONS;

	private String R36_PRODUCT;
	private BigDecimal R36_STAGE1_PROVISIONS;
	private BigDecimal R36_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R36_TOTAL_GENERAL_PROVISIONS;

	private String R37_PRODUCT;
	private BigDecimal R37_STAGE1_PROVISIONS;
	private BigDecimal R37_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R37_TOTAL_GENERAL_PROVISIONS;

	private String R38_PRODUCT;
	private BigDecimal R38_STAGE1_PROVISIONS;
	private BigDecimal R38_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R38_TOTAL_GENERAL_PROVISIONS;

	private String R39_PRODUCT;
	private BigDecimal R39_STAGE1_PROVISIONS;
	private BigDecimal R39_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R39_TOTAL_GENERAL_PROVISIONS;

	private String R40_PRODUCT;
	private BigDecimal R40_STAGE1_PROVISIONS;
	private BigDecimal R40_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R40_TOTAL_GENERAL_PROVISIONS;

	private String R41_PRODUCT;
	private BigDecimal R41_STAGE1_PROVISIONS;
	private BigDecimal R41_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R41_TOTAL_GENERAL_PROVISIONS;

	private String R42_PRODUCT;
	private BigDecimal R42_STAGE1_PROVISIONS;
	private BigDecimal R42_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R42_TOTAL_GENERAL_PROVISIONS;

	private String R43_PRODUCT;
	private BigDecimal R43_STAGE1_PROVISIONS;
	private BigDecimal R43_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R43_TOTAL_GENERAL_PROVISIONS;

	private String R44_PRODUCT;
	private BigDecimal R44_STAGE1_PROVISIONS;
	private BigDecimal R44_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R44_TOTAL_GENERAL_PROVISIONS;

	private String R45_PRODUCT;
	private BigDecimal R45_STAGE1_PROVISIONS;
	private BigDecimal R45_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R45_TOTAL_GENERAL_PROVISIONS;

	private String R46_PRODUCT;
	private BigDecimal R46_STAGE1_PROVISIONS;
	private BigDecimal R46_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R46_TOTAL_GENERAL_PROVISIONS;

	private String R47_PRODUCT;
	private BigDecimal R47_STAGE1_PROVISIONS;
	private BigDecimal R47_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R47_TOTAL_GENERAL_PROVISIONS;

	private String R48_PRODUCT;
	private BigDecimal R48_STAGE1_PROVISIONS;
	private BigDecimal R48_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R48_TOTAL_GENERAL_PROVISIONS;

	private String R49_PRODUCT;
	private BigDecimal R49_STAGE1_PROVISIONS;
	private BigDecimal R49_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R49_TOTAL_GENERAL_PROVISIONS;

	private String R50_PRODUCT;
	private BigDecimal R50_STAGE1_PROVISIONS;
	private BigDecimal R50_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R50_TOTAL_GENERAL_PROVISIONS;

	private String R51_PRODUCT;
	private BigDecimal R51_STAGE1_PROVISIONS;
	private BigDecimal R51_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R51_TOTAL_GENERAL_PROVISIONS;

	private String R52_PRODUCT;
	private BigDecimal R52_STAGE1_PROVISIONS;
	private BigDecimal R52_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R52_TOTAL_GENERAL_PROVISIONS;

	private String R53_PRODUCT;
	private BigDecimal R53_STAGE1_PROVISIONS;
	private BigDecimal R53_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R53_TOTAL_GENERAL_PROVISIONS;

	private String R54_PRODUCT;
	private BigDecimal R54_STAGE1_PROVISIONS;
	private BigDecimal R54_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R54_TOTAL_GENERAL_PROVISIONS;

	private String R55_PRODUCT;
	private BigDecimal R55_STAGE1_PROVISIONS;
	private BigDecimal R55_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R55_TOTAL_GENERAL_PROVISIONS;

	private String R56_PRODUCT;
	private BigDecimal R56_STAGE1_PROVISIONS;
	private BigDecimal R56_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R56_TOTAL_GENERAL_PROVISIONS;

	private String R57_PRODUCT;
	private BigDecimal R57_STAGE1_PROVISIONS;
	private BigDecimal R57_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R57_TOTAL_GENERAL_PROVISIONS;

	private String R58_PRODUCT;
	private BigDecimal R58_STAGE1_PROVISIONS;
	private BigDecimal R58_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R58_TOTAL_GENERAL_PROVISIONS;

	private String R59_PRODUCT;
	private BigDecimal R59_STAGE1_PROVISIONS;
	private BigDecimal R59_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R59_TOTAL_GENERAL_PROVISIONS;

	private String R60_PRODUCT;
	private BigDecimal R60_STAGE1_PROVISIONS;
	private BigDecimal R60_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R60_TOTAL_GENERAL_PROVISIONS;

	private String R61_PRODUCT;
	private BigDecimal R61_STAGE1_PROVISIONS;
	private BigDecimal R61_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R61_TOTAL_GENERAL_PROVISIONS;

	private String R62_PRODUCT;
	private BigDecimal R62_STAGE1_PROVISIONS;
	private BigDecimal R62_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R62_TOTAL_GENERAL_PROVISIONS;

	private String R63_PRODUCT;
	private BigDecimal R63_STAGE1_PROVISIONS;
	private BigDecimal R63_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R63_TOTAL_GENERAL_PROVISIONS;

	private String R64_PRODUCT;
	private BigDecimal R64_STAGE1_PROVISIONS;
	private BigDecimal R64_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R64_TOTAL_GENERAL_PROVISIONS;
	
	               
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
	
public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_STAGE1_PROVISIONS() {
		return R11_STAGE1_PROVISIONS;
	}
	public void setR11_STAGE1_PROVISIONS(BigDecimal r11_STAGE1_PROVISIONS) {
		R11_STAGE1_PROVISIONS = r11_STAGE1_PROVISIONS;
	}
	public BigDecimal getR11_QUALIFY_STAGE2_PROVISIONS() {
		return R11_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR11_QUALIFY_STAGE2_PROVISIONS(BigDecimal r11_QUALIFY_STAGE2_PROVISIONS) {
		R11_QUALIFY_STAGE2_PROVISIONS = r11_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR11_TOTAL_GENERAL_PROVISIONS() {
		return R11_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR11_TOTAL_GENERAL_PROVISIONS(BigDecimal r11_TOTAL_GENERAL_PROVISIONS) {
		R11_TOTAL_GENERAL_PROVISIONS = r11_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_STAGE1_PROVISIONS() {
		return R12_STAGE1_PROVISIONS;
	}
	public void setR12_STAGE1_PROVISIONS(BigDecimal r12_STAGE1_PROVISIONS) {
		R12_STAGE1_PROVISIONS = r12_STAGE1_PROVISIONS;
	}
	public BigDecimal getR12_QUALIFY_STAGE2_PROVISIONS() {
		return R12_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR12_QUALIFY_STAGE2_PROVISIONS(BigDecimal r12_QUALIFY_STAGE2_PROVISIONS) {
		R12_QUALIFY_STAGE2_PROVISIONS = r12_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR12_TOTAL_GENERAL_PROVISIONS() {
		return R12_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR12_TOTAL_GENERAL_PROVISIONS(BigDecimal r12_TOTAL_GENERAL_PROVISIONS) {
		R12_TOTAL_GENERAL_PROVISIONS = r12_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_STAGE1_PROVISIONS() {
		return R13_STAGE1_PROVISIONS;
	}
	public void setR13_STAGE1_PROVISIONS(BigDecimal r13_STAGE1_PROVISIONS) {
		R13_STAGE1_PROVISIONS = r13_STAGE1_PROVISIONS;
	}
	public BigDecimal getR13_QUALIFY_STAGE2_PROVISIONS() {
		return R13_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR13_QUALIFY_STAGE2_PROVISIONS(BigDecimal r13_QUALIFY_STAGE2_PROVISIONS) {
		R13_QUALIFY_STAGE2_PROVISIONS = r13_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR13_TOTAL_GENERAL_PROVISIONS() {
		return R13_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR13_TOTAL_GENERAL_PROVISIONS(BigDecimal r13_TOTAL_GENERAL_PROVISIONS) {
		R13_TOTAL_GENERAL_PROVISIONS = r13_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_STAGE1_PROVISIONS() {
		return R14_STAGE1_PROVISIONS;
	}
	public void setR14_STAGE1_PROVISIONS(BigDecimal r14_STAGE1_PROVISIONS) {
		R14_STAGE1_PROVISIONS = r14_STAGE1_PROVISIONS;
	}
	public BigDecimal getR14_QUALIFY_STAGE2_PROVISIONS() {
		return R14_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR14_QUALIFY_STAGE2_PROVISIONS(BigDecimal r14_QUALIFY_STAGE2_PROVISIONS) {
		R14_QUALIFY_STAGE2_PROVISIONS = r14_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR14_TOTAL_GENERAL_PROVISIONS() {
		return R14_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR14_TOTAL_GENERAL_PROVISIONS(BigDecimal r14_TOTAL_GENERAL_PROVISIONS) {
		R14_TOTAL_GENERAL_PROVISIONS = r14_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_STAGE1_PROVISIONS() {
		return R15_STAGE1_PROVISIONS;
	}
	public void setR15_STAGE1_PROVISIONS(BigDecimal r15_STAGE1_PROVISIONS) {
		R15_STAGE1_PROVISIONS = r15_STAGE1_PROVISIONS;
	}
	public BigDecimal getR15_QUALIFY_STAGE2_PROVISIONS() {
		return R15_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR15_QUALIFY_STAGE2_PROVISIONS(BigDecimal r15_QUALIFY_STAGE2_PROVISIONS) {
		R15_QUALIFY_STAGE2_PROVISIONS = r15_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR15_TOTAL_GENERAL_PROVISIONS() {
		return R15_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR15_TOTAL_GENERAL_PROVISIONS(BigDecimal r15_TOTAL_GENERAL_PROVISIONS) {
		R15_TOTAL_GENERAL_PROVISIONS = r15_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_STAGE1_PROVISIONS() {
		return R16_STAGE1_PROVISIONS;
	}
	public void setR16_STAGE1_PROVISIONS(BigDecimal r16_STAGE1_PROVISIONS) {
		R16_STAGE1_PROVISIONS = r16_STAGE1_PROVISIONS;
	}
	public BigDecimal getR16_QUALIFY_STAGE2_PROVISIONS() {
		return R16_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR16_QUALIFY_STAGE2_PROVISIONS(BigDecimal r16_QUALIFY_STAGE2_PROVISIONS) {
		R16_QUALIFY_STAGE2_PROVISIONS = r16_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR16_TOTAL_GENERAL_PROVISIONS() {
		return R16_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR16_TOTAL_GENERAL_PROVISIONS(BigDecimal r16_TOTAL_GENERAL_PROVISIONS) {
		R16_TOTAL_GENERAL_PROVISIONS = r16_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_STAGE1_PROVISIONS() {
		return R17_STAGE1_PROVISIONS;
	}
	public void setR17_STAGE1_PROVISIONS(BigDecimal r17_STAGE1_PROVISIONS) {
		R17_STAGE1_PROVISIONS = r17_STAGE1_PROVISIONS;
	}
	public BigDecimal getR17_QUALIFY_STAGE2_PROVISIONS() {
		return R17_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR17_QUALIFY_STAGE2_PROVISIONS(BigDecimal r17_QUALIFY_STAGE2_PROVISIONS) {
		R17_QUALIFY_STAGE2_PROVISIONS = r17_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR17_TOTAL_GENERAL_PROVISIONS() {
		return R17_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR17_TOTAL_GENERAL_PROVISIONS(BigDecimal r17_TOTAL_GENERAL_PROVISIONS) {
		R17_TOTAL_GENERAL_PROVISIONS = r17_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}
	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}
	public BigDecimal getR18_STAGE1_PROVISIONS() {
		return R18_STAGE1_PROVISIONS;
	}
	public void setR18_STAGE1_PROVISIONS(BigDecimal r18_STAGE1_PROVISIONS) {
		R18_STAGE1_PROVISIONS = r18_STAGE1_PROVISIONS;
	}
	public BigDecimal getR18_QUALIFY_STAGE2_PROVISIONS() {
		return R18_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR18_QUALIFY_STAGE2_PROVISIONS(BigDecimal r18_QUALIFY_STAGE2_PROVISIONS) {
		R18_QUALIFY_STAGE2_PROVISIONS = r18_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR18_TOTAL_GENERAL_PROVISIONS() {
		return R18_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR18_TOTAL_GENERAL_PROVISIONS(BigDecimal r18_TOTAL_GENERAL_PROVISIONS) {
		R18_TOTAL_GENERAL_PROVISIONS = r18_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}
	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}
	public BigDecimal getR19_STAGE1_PROVISIONS() {
		return R19_STAGE1_PROVISIONS;
	}
	public void setR19_STAGE1_PROVISIONS(BigDecimal r19_STAGE1_PROVISIONS) {
		R19_STAGE1_PROVISIONS = r19_STAGE1_PROVISIONS;
	}
	public BigDecimal getR19_QUALIFY_STAGE2_PROVISIONS() {
		return R19_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR19_QUALIFY_STAGE2_PROVISIONS(BigDecimal r19_QUALIFY_STAGE2_PROVISIONS) {
		R19_QUALIFY_STAGE2_PROVISIONS = r19_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR19_TOTAL_GENERAL_PROVISIONS() {
		return R19_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR19_TOTAL_GENERAL_PROVISIONS(BigDecimal r19_TOTAL_GENERAL_PROVISIONS) {
		R19_TOTAL_GENERAL_PROVISIONS = r19_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}
	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}
	public BigDecimal getR20_STAGE1_PROVISIONS() {
		return R20_STAGE1_PROVISIONS;
	}
	public void setR20_STAGE1_PROVISIONS(BigDecimal r20_STAGE1_PROVISIONS) {
		R20_STAGE1_PROVISIONS = r20_STAGE1_PROVISIONS;
	}
	public BigDecimal getR20_QUALIFY_STAGE2_PROVISIONS() {
		return R20_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR20_QUALIFY_STAGE2_PROVISIONS(BigDecimal r20_QUALIFY_STAGE2_PROVISIONS) {
		R20_QUALIFY_STAGE2_PROVISIONS = r20_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR20_TOTAL_GENERAL_PROVISIONS() {
		return R20_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR20_TOTAL_GENERAL_PROVISIONS(BigDecimal r20_TOTAL_GENERAL_PROVISIONS) {
		R20_TOTAL_GENERAL_PROVISIONS = r20_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}
	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}
	public BigDecimal getR21_STAGE1_PROVISIONS() {
		return R21_STAGE1_PROVISIONS;
	}
	public void setR21_STAGE1_PROVISIONS(BigDecimal r21_STAGE1_PROVISIONS) {
		R21_STAGE1_PROVISIONS = r21_STAGE1_PROVISIONS;
	}
	public BigDecimal getR21_QUALIFY_STAGE2_PROVISIONS() {
		return R21_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR21_QUALIFY_STAGE2_PROVISIONS(BigDecimal r21_QUALIFY_STAGE2_PROVISIONS) {
		R21_QUALIFY_STAGE2_PROVISIONS = r21_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR21_TOTAL_GENERAL_PROVISIONS() {
		return R21_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR21_TOTAL_GENERAL_PROVISIONS(BigDecimal r21_TOTAL_GENERAL_PROVISIONS) {
		R21_TOTAL_GENERAL_PROVISIONS = r21_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}
	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}
	public BigDecimal getR22_STAGE1_PROVISIONS() {
		return R22_STAGE1_PROVISIONS;
	}
	public void setR22_STAGE1_PROVISIONS(BigDecimal r22_STAGE1_PROVISIONS) {
		R22_STAGE1_PROVISIONS = r22_STAGE1_PROVISIONS;
	}
	public BigDecimal getR22_QUALIFY_STAGE2_PROVISIONS() {
		return R22_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR22_QUALIFY_STAGE2_PROVISIONS(BigDecimal r22_QUALIFY_STAGE2_PROVISIONS) {
		R22_QUALIFY_STAGE2_PROVISIONS = r22_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR22_TOTAL_GENERAL_PROVISIONS() {
		return R22_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR22_TOTAL_GENERAL_PROVISIONS(BigDecimal r22_TOTAL_GENERAL_PROVISIONS) {
		R22_TOTAL_GENERAL_PROVISIONS = r22_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}
	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}
	public BigDecimal getR23_STAGE1_PROVISIONS() {
		return R23_STAGE1_PROVISIONS;
	}
	public void setR23_STAGE1_PROVISIONS(BigDecimal r23_STAGE1_PROVISIONS) {
		R23_STAGE1_PROVISIONS = r23_STAGE1_PROVISIONS;
	}
	public BigDecimal getR23_QUALIFY_STAGE2_PROVISIONS() {
		return R23_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR23_QUALIFY_STAGE2_PROVISIONS(BigDecimal r23_QUALIFY_STAGE2_PROVISIONS) {
		R23_QUALIFY_STAGE2_PROVISIONS = r23_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR23_TOTAL_GENERAL_PROVISIONS() {
		return R23_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR23_TOTAL_GENERAL_PROVISIONS(BigDecimal r23_TOTAL_GENERAL_PROVISIONS) {
		R23_TOTAL_GENERAL_PROVISIONS = r23_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}
	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}
	public BigDecimal getR24_STAGE1_PROVISIONS() {
		return R24_STAGE1_PROVISIONS;
	}
	public void setR24_STAGE1_PROVISIONS(BigDecimal r24_STAGE1_PROVISIONS) {
		R24_STAGE1_PROVISIONS = r24_STAGE1_PROVISIONS;
	}
	public BigDecimal getR24_QUALIFY_STAGE2_PROVISIONS() {
		return R24_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR24_QUALIFY_STAGE2_PROVISIONS(BigDecimal r24_QUALIFY_STAGE2_PROVISIONS) {
		R24_QUALIFY_STAGE2_PROVISIONS = r24_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR24_TOTAL_GENERAL_PROVISIONS() {
		return R24_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR24_TOTAL_GENERAL_PROVISIONS(BigDecimal r24_TOTAL_GENERAL_PROVISIONS) {
		R24_TOTAL_GENERAL_PROVISIONS = r24_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}
	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}
	public BigDecimal getR25_STAGE1_PROVISIONS() {
		return R25_STAGE1_PROVISIONS;
	}
	public void setR25_STAGE1_PROVISIONS(BigDecimal r25_STAGE1_PROVISIONS) {
		R25_STAGE1_PROVISIONS = r25_STAGE1_PROVISIONS;
	}
	public BigDecimal getR25_QUALIFY_STAGE2_PROVISIONS() {
		return R25_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR25_QUALIFY_STAGE2_PROVISIONS(BigDecimal r25_QUALIFY_STAGE2_PROVISIONS) {
		R25_QUALIFY_STAGE2_PROVISIONS = r25_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR25_TOTAL_GENERAL_PROVISIONS() {
		return R25_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR25_TOTAL_GENERAL_PROVISIONS(BigDecimal r25_TOTAL_GENERAL_PROVISIONS) {
		R25_TOTAL_GENERAL_PROVISIONS = r25_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}
	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}
	public BigDecimal getR26_STAGE1_PROVISIONS() {
		return R26_STAGE1_PROVISIONS;
	}
	public void setR26_STAGE1_PROVISIONS(BigDecimal r26_STAGE1_PROVISIONS) {
		R26_STAGE1_PROVISIONS = r26_STAGE1_PROVISIONS;
	}
	public BigDecimal getR26_QUALIFY_STAGE2_PROVISIONS() {
		return R26_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR26_QUALIFY_STAGE2_PROVISIONS(BigDecimal r26_QUALIFY_STAGE2_PROVISIONS) {
		R26_QUALIFY_STAGE2_PROVISIONS = r26_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR26_TOTAL_GENERAL_PROVISIONS() {
		return R26_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR26_TOTAL_GENERAL_PROVISIONS(BigDecimal r26_TOTAL_GENERAL_PROVISIONS) {
		R26_TOTAL_GENERAL_PROVISIONS = r26_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}
	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}
	public BigDecimal getR27_STAGE1_PROVISIONS() {
		return R27_STAGE1_PROVISIONS;
	}
	public void setR27_STAGE1_PROVISIONS(BigDecimal r27_STAGE1_PROVISIONS) {
		R27_STAGE1_PROVISIONS = r27_STAGE1_PROVISIONS;
	}
	public BigDecimal getR27_QUALIFY_STAGE2_PROVISIONS() {
		return R27_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR27_QUALIFY_STAGE2_PROVISIONS(BigDecimal r27_QUALIFY_STAGE2_PROVISIONS) {
		R27_QUALIFY_STAGE2_PROVISIONS = r27_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR27_TOTAL_GENERAL_PROVISIONS() {
		return R27_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR27_TOTAL_GENERAL_PROVISIONS(BigDecimal r27_TOTAL_GENERAL_PROVISIONS) {
		R27_TOTAL_GENERAL_PROVISIONS = r27_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}
	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}
	public BigDecimal getR28_STAGE1_PROVISIONS() {
		return R28_STAGE1_PROVISIONS;
	}
	public void setR28_STAGE1_PROVISIONS(BigDecimal r28_STAGE1_PROVISIONS) {
		R28_STAGE1_PROVISIONS = r28_STAGE1_PROVISIONS;
	}
	public BigDecimal getR28_QUALIFY_STAGE2_PROVISIONS() {
		return R28_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR28_QUALIFY_STAGE2_PROVISIONS(BigDecimal r28_QUALIFY_STAGE2_PROVISIONS) {
		R28_QUALIFY_STAGE2_PROVISIONS = r28_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR28_TOTAL_GENERAL_PROVISIONS() {
		return R28_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR28_TOTAL_GENERAL_PROVISIONS(BigDecimal r28_TOTAL_GENERAL_PROVISIONS) {
		R28_TOTAL_GENERAL_PROVISIONS = r28_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}
	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}
	public BigDecimal getR29_STAGE1_PROVISIONS() {
		return R29_STAGE1_PROVISIONS;
	}
	public void setR29_STAGE1_PROVISIONS(BigDecimal r29_STAGE1_PROVISIONS) {
		R29_STAGE1_PROVISIONS = r29_STAGE1_PROVISIONS;
	}
	public BigDecimal getR29_QUALIFY_STAGE2_PROVISIONS() {
		return R29_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR29_QUALIFY_STAGE2_PROVISIONS(BigDecimal r29_QUALIFY_STAGE2_PROVISIONS) {
		R29_QUALIFY_STAGE2_PROVISIONS = r29_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR29_TOTAL_GENERAL_PROVISIONS() {
		return R29_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR29_TOTAL_GENERAL_PROVISIONS(BigDecimal r29_TOTAL_GENERAL_PROVISIONS) {
		R29_TOTAL_GENERAL_PROVISIONS = r29_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}
	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}
	public BigDecimal getR30_STAGE1_PROVISIONS() {
		return R30_STAGE1_PROVISIONS;
	}
	public void setR30_STAGE1_PROVISIONS(BigDecimal r30_STAGE1_PROVISIONS) {
		R30_STAGE1_PROVISIONS = r30_STAGE1_PROVISIONS;
	}
	public BigDecimal getR30_QUALIFY_STAGE2_PROVISIONS() {
		return R30_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR30_QUALIFY_STAGE2_PROVISIONS(BigDecimal r30_QUALIFY_STAGE2_PROVISIONS) {
		R30_QUALIFY_STAGE2_PROVISIONS = r30_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR30_TOTAL_GENERAL_PROVISIONS() {
		return R30_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR30_TOTAL_GENERAL_PROVISIONS(BigDecimal r30_TOTAL_GENERAL_PROVISIONS) {
		R30_TOTAL_GENERAL_PROVISIONS = r30_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}
	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}
	public BigDecimal getR31_STAGE1_PROVISIONS() {
		return R31_STAGE1_PROVISIONS;
	}
	public void setR31_STAGE1_PROVISIONS(BigDecimal r31_STAGE1_PROVISIONS) {
		R31_STAGE1_PROVISIONS = r31_STAGE1_PROVISIONS;
	}
	public BigDecimal getR31_QUALIFY_STAGE2_PROVISIONS() {
		return R31_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR31_QUALIFY_STAGE2_PROVISIONS(BigDecimal r31_QUALIFY_STAGE2_PROVISIONS) {
		R31_QUALIFY_STAGE2_PROVISIONS = r31_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR31_TOTAL_GENERAL_PROVISIONS() {
		return R31_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR31_TOTAL_GENERAL_PROVISIONS(BigDecimal r31_TOTAL_GENERAL_PROVISIONS) {
		R31_TOTAL_GENERAL_PROVISIONS = r31_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}
	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}
	public BigDecimal getR32_STAGE1_PROVISIONS() {
		return R32_STAGE1_PROVISIONS;
	}
	public void setR32_STAGE1_PROVISIONS(BigDecimal r32_STAGE1_PROVISIONS) {
		R32_STAGE1_PROVISIONS = r32_STAGE1_PROVISIONS;
	}
	public BigDecimal getR32_QUALIFY_STAGE2_PROVISIONS() {
		return R32_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR32_QUALIFY_STAGE2_PROVISIONS(BigDecimal r32_QUALIFY_STAGE2_PROVISIONS) {
		R32_QUALIFY_STAGE2_PROVISIONS = r32_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR32_TOTAL_GENERAL_PROVISIONS() {
		return R32_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR32_TOTAL_GENERAL_PROVISIONS(BigDecimal r32_TOTAL_GENERAL_PROVISIONS) {
		R32_TOTAL_GENERAL_PROVISIONS = r32_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}
	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}
	public BigDecimal getR33_STAGE1_PROVISIONS() {
		return R33_STAGE1_PROVISIONS;
	}
	public void setR33_STAGE1_PROVISIONS(BigDecimal r33_STAGE1_PROVISIONS) {
		R33_STAGE1_PROVISIONS = r33_STAGE1_PROVISIONS;
	}
	public BigDecimal getR33_QUALIFY_STAGE2_PROVISIONS() {
		return R33_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR33_QUALIFY_STAGE2_PROVISIONS(BigDecimal r33_QUALIFY_STAGE2_PROVISIONS) {
		R33_QUALIFY_STAGE2_PROVISIONS = r33_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR33_TOTAL_GENERAL_PROVISIONS() {
		return R33_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR33_TOTAL_GENERAL_PROVISIONS(BigDecimal r33_TOTAL_GENERAL_PROVISIONS) {
		R33_TOTAL_GENERAL_PROVISIONS = r33_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}
	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}
	public BigDecimal getR34_STAGE1_PROVISIONS() {
		return R34_STAGE1_PROVISIONS;
	}
	public void setR34_STAGE1_PROVISIONS(BigDecimal r34_STAGE1_PROVISIONS) {
		R34_STAGE1_PROVISIONS = r34_STAGE1_PROVISIONS;
	}
	public BigDecimal getR34_QUALIFY_STAGE2_PROVISIONS() {
		return R34_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR34_QUALIFY_STAGE2_PROVISIONS(BigDecimal r34_QUALIFY_STAGE2_PROVISIONS) {
		R34_QUALIFY_STAGE2_PROVISIONS = r34_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR34_TOTAL_GENERAL_PROVISIONS() {
		return R34_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR34_TOTAL_GENERAL_PROVISIONS(BigDecimal r34_TOTAL_GENERAL_PROVISIONS) {
		R34_TOTAL_GENERAL_PROVISIONS = r34_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}
	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}
	public BigDecimal getR35_STAGE1_PROVISIONS() {
		return R35_STAGE1_PROVISIONS;
	}
	public void setR35_STAGE1_PROVISIONS(BigDecimal r35_STAGE1_PROVISIONS) {
		R35_STAGE1_PROVISIONS = r35_STAGE1_PROVISIONS;
	}
	public BigDecimal getR35_QUALIFY_STAGE2_PROVISIONS() {
		return R35_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR35_QUALIFY_STAGE2_PROVISIONS(BigDecimal r35_QUALIFY_STAGE2_PROVISIONS) {
		R35_QUALIFY_STAGE2_PROVISIONS = r35_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR35_TOTAL_GENERAL_PROVISIONS() {
		return R35_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR35_TOTAL_GENERAL_PROVISIONS(BigDecimal r35_TOTAL_GENERAL_PROVISIONS) {
		R35_TOTAL_GENERAL_PROVISIONS = r35_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_STAGE1_PROVISIONS() {
		return R36_STAGE1_PROVISIONS;
	}
	public void setR36_STAGE1_PROVISIONS(BigDecimal r36_STAGE1_PROVISIONS) {
		R36_STAGE1_PROVISIONS = r36_STAGE1_PROVISIONS;
	}
	public BigDecimal getR36_QUALIFY_STAGE2_PROVISIONS() {
		return R36_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR36_QUALIFY_STAGE2_PROVISIONS(BigDecimal r36_QUALIFY_STAGE2_PROVISIONS) {
		R36_QUALIFY_STAGE2_PROVISIONS = r36_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR36_TOTAL_GENERAL_PROVISIONS() {
		return R36_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR36_TOTAL_GENERAL_PROVISIONS(BigDecimal r36_TOTAL_GENERAL_PROVISIONS) {
		R36_TOTAL_GENERAL_PROVISIONS = r36_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}
	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}
	public BigDecimal getR37_STAGE1_PROVISIONS() {
		return R37_STAGE1_PROVISIONS;
	}
	public void setR37_STAGE1_PROVISIONS(BigDecimal r37_STAGE1_PROVISIONS) {
		R37_STAGE1_PROVISIONS = r37_STAGE1_PROVISIONS;
	}
	public BigDecimal getR37_QUALIFY_STAGE2_PROVISIONS() {
		return R37_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR37_QUALIFY_STAGE2_PROVISIONS(BigDecimal r37_QUALIFY_STAGE2_PROVISIONS) {
		R37_QUALIFY_STAGE2_PROVISIONS = r37_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR37_TOTAL_GENERAL_PROVISIONS() {
		return R37_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR37_TOTAL_GENERAL_PROVISIONS(BigDecimal r37_TOTAL_GENERAL_PROVISIONS) {
		R37_TOTAL_GENERAL_PROVISIONS = r37_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}
	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}
	public BigDecimal getR38_STAGE1_PROVISIONS() {
		return R38_STAGE1_PROVISIONS;
	}
	public void setR38_STAGE1_PROVISIONS(BigDecimal r38_STAGE1_PROVISIONS) {
		R38_STAGE1_PROVISIONS = r38_STAGE1_PROVISIONS;
	}
	public BigDecimal getR38_QUALIFY_STAGE2_PROVISIONS() {
		return R38_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR38_QUALIFY_STAGE2_PROVISIONS(BigDecimal r38_QUALIFY_STAGE2_PROVISIONS) {
		R38_QUALIFY_STAGE2_PROVISIONS = r38_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR38_TOTAL_GENERAL_PROVISIONS() {
		return R38_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR38_TOTAL_GENERAL_PROVISIONS(BigDecimal r38_TOTAL_GENERAL_PROVISIONS) {
		R38_TOTAL_GENERAL_PROVISIONS = r38_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}
	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}
	public BigDecimal getR39_STAGE1_PROVISIONS() {
		return R39_STAGE1_PROVISIONS;
	}
	public void setR39_STAGE1_PROVISIONS(BigDecimal r39_STAGE1_PROVISIONS) {
		R39_STAGE1_PROVISIONS = r39_STAGE1_PROVISIONS;
	}
	public BigDecimal getR39_QUALIFY_STAGE2_PROVISIONS() {
		return R39_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR39_QUALIFY_STAGE2_PROVISIONS(BigDecimal r39_QUALIFY_STAGE2_PROVISIONS) {
		R39_QUALIFY_STAGE2_PROVISIONS = r39_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR39_TOTAL_GENERAL_PROVISIONS() {
		return R39_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR39_TOTAL_GENERAL_PROVISIONS(BigDecimal r39_TOTAL_GENERAL_PROVISIONS) {
		R39_TOTAL_GENERAL_PROVISIONS = r39_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}
	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}
	public BigDecimal getR40_STAGE1_PROVISIONS() {
		return R40_STAGE1_PROVISIONS;
	}
	public void setR40_STAGE1_PROVISIONS(BigDecimal r40_STAGE1_PROVISIONS) {
		R40_STAGE1_PROVISIONS = r40_STAGE1_PROVISIONS;
	}
	public BigDecimal getR40_QUALIFY_STAGE2_PROVISIONS() {
		return R40_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR40_QUALIFY_STAGE2_PROVISIONS(BigDecimal r40_QUALIFY_STAGE2_PROVISIONS) {
		R40_QUALIFY_STAGE2_PROVISIONS = r40_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR40_TOTAL_GENERAL_PROVISIONS() {
		return R40_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR40_TOTAL_GENERAL_PROVISIONS(BigDecimal r40_TOTAL_GENERAL_PROVISIONS) {
		R40_TOTAL_GENERAL_PROVISIONS = r40_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}
	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}
	public BigDecimal getR41_STAGE1_PROVISIONS() {
		return R41_STAGE1_PROVISIONS;
	}
	public void setR41_STAGE1_PROVISIONS(BigDecimal r41_STAGE1_PROVISIONS) {
		R41_STAGE1_PROVISIONS = r41_STAGE1_PROVISIONS;
	}
	public BigDecimal getR41_QUALIFY_STAGE2_PROVISIONS() {
		return R41_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR41_QUALIFY_STAGE2_PROVISIONS(BigDecimal r41_QUALIFY_STAGE2_PROVISIONS) {
		R41_QUALIFY_STAGE2_PROVISIONS = r41_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR41_TOTAL_GENERAL_PROVISIONS() {
		return R41_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR41_TOTAL_GENERAL_PROVISIONS(BigDecimal r41_TOTAL_GENERAL_PROVISIONS) {
		R41_TOTAL_GENERAL_PROVISIONS = r41_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}
	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}
	public BigDecimal getR42_STAGE1_PROVISIONS() {
		return R42_STAGE1_PROVISIONS;
	}
	public void setR42_STAGE1_PROVISIONS(BigDecimal r42_STAGE1_PROVISIONS) {
		R42_STAGE1_PROVISIONS = r42_STAGE1_PROVISIONS;
	}
	public BigDecimal getR42_QUALIFY_STAGE2_PROVISIONS() {
		return R42_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR42_QUALIFY_STAGE2_PROVISIONS(BigDecimal r42_QUALIFY_STAGE2_PROVISIONS) {
		R42_QUALIFY_STAGE2_PROVISIONS = r42_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR42_TOTAL_GENERAL_PROVISIONS() {
		return R42_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR42_TOTAL_GENERAL_PROVISIONS(BigDecimal r42_TOTAL_GENERAL_PROVISIONS) {
		R42_TOTAL_GENERAL_PROVISIONS = r42_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}
	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}
	public BigDecimal getR43_STAGE1_PROVISIONS() {
		return R43_STAGE1_PROVISIONS;
	}
	public void setR43_STAGE1_PROVISIONS(BigDecimal r43_STAGE1_PROVISIONS) {
		R43_STAGE1_PROVISIONS = r43_STAGE1_PROVISIONS;
	}
	public BigDecimal getR43_QUALIFY_STAGE2_PROVISIONS() {
		return R43_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR43_QUALIFY_STAGE2_PROVISIONS(BigDecimal r43_QUALIFY_STAGE2_PROVISIONS) {
		R43_QUALIFY_STAGE2_PROVISIONS = r43_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR43_TOTAL_GENERAL_PROVISIONS() {
		return R43_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR43_TOTAL_GENERAL_PROVISIONS(BigDecimal r43_TOTAL_GENERAL_PROVISIONS) {
		R43_TOTAL_GENERAL_PROVISIONS = r43_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR44_PRODUCT() {
		return R44_PRODUCT;
	}
	public void setR44_PRODUCT(String r44_PRODUCT) {
		R44_PRODUCT = r44_PRODUCT;
	}
	public BigDecimal getR44_STAGE1_PROVISIONS() {
		return R44_STAGE1_PROVISIONS;
	}
	public void setR44_STAGE1_PROVISIONS(BigDecimal r44_STAGE1_PROVISIONS) {
		R44_STAGE1_PROVISIONS = r44_STAGE1_PROVISIONS;
	}
	public BigDecimal getR44_QUALIFY_STAGE2_PROVISIONS() {
		return R44_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR44_QUALIFY_STAGE2_PROVISIONS(BigDecimal r44_QUALIFY_STAGE2_PROVISIONS) {
		R44_QUALIFY_STAGE2_PROVISIONS = r44_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR44_TOTAL_GENERAL_PROVISIONS() {
		return R44_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR44_TOTAL_GENERAL_PROVISIONS(BigDecimal r44_TOTAL_GENERAL_PROVISIONS) {
		R44_TOTAL_GENERAL_PROVISIONS = r44_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR45_PRODUCT() {
		return R45_PRODUCT;
	}
	public void setR45_PRODUCT(String r45_PRODUCT) {
		R45_PRODUCT = r45_PRODUCT;
	}
	public BigDecimal getR45_STAGE1_PROVISIONS() {
		return R45_STAGE1_PROVISIONS;
	}
	public void setR45_STAGE1_PROVISIONS(BigDecimal r45_STAGE1_PROVISIONS) {
		R45_STAGE1_PROVISIONS = r45_STAGE1_PROVISIONS;
	}
	public BigDecimal getR45_QUALIFY_STAGE2_PROVISIONS() {
		return R45_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR45_QUALIFY_STAGE2_PROVISIONS(BigDecimal r45_QUALIFY_STAGE2_PROVISIONS) {
		R45_QUALIFY_STAGE2_PROVISIONS = r45_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR45_TOTAL_GENERAL_PROVISIONS() {
		return R45_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR45_TOTAL_GENERAL_PROVISIONS(BigDecimal r45_TOTAL_GENERAL_PROVISIONS) {
		R45_TOTAL_GENERAL_PROVISIONS = r45_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR46_PRODUCT() {
		return R46_PRODUCT;
	}
	public void setR46_PRODUCT(String r46_PRODUCT) {
		R46_PRODUCT = r46_PRODUCT;
	}
	public BigDecimal getR46_STAGE1_PROVISIONS() {
		return R46_STAGE1_PROVISIONS;
	}
	public void setR46_STAGE1_PROVISIONS(BigDecimal r46_STAGE1_PROVISIONS) {
		R46_STAGE1_PROVISIONS = r46_STAGE1_PROVISIONS;
	}
	public BigDecimal getR46_QUALIFY_STAGE2_PROVISIONS() {
		return R46_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR46_QUALIFY_STAGE2_PROVISIONS(BigDecimal r46_QUALIFY_STAGE2_PROVISIONS) {
		R46_QUALIFY_STAGE2_PROVISIONS = r46_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR46_TOTAL_GENERAL_PROVISIONS() {
		return R46_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR46_TOTAL_GENERAL_PROVISIONS(BigDecimal r46_TOTAL_GENERAL_PROVISIONS) {
		R46_TOTAL_GENERAL_PROVISIONS = r46_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}
	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}
	public BigDecimal getR47_STAGE1_PROVISIONS() {
		return R47_STAGE1_PROVISIONS;
	}
	public void setR47_STAGE1_PROVISIONS(BigDecimal r47_STAGE1_PROVISIONS) {
		R47_STAGE1_PROVISIONS = r47_STAGE1_PROVISIONS;
	}
	public BigDecimal getR47_QUALIFY_STAGE2_PROVISIONS() {
		return R47_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR47_QUALIFY_STAGE2_PROVISIONS(BigDecimal r47_QUALIFY_STAGE2_PROVISIONS) {
		R47_QUALIFY_STAGE2_PROVISIONS = r47_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR47_TOTAL_GENERAL_PROVISIONS() {
		return R47_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR47_TOTAL_GENERAL_PROVISIONS(BigDecimal r47_TOTAL_GENERAL_PROVISIONS) {
		R47_TOTAL_GENERAL_PROVISIONS = r47_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR48_PRODUCT() {
		return R48_PRODUCT;
	}
	public void setR48_PRODUCT(String r48_PRODUCT) {
		R48_PRODUCT = r48_PRODUCT;
	}
	public BigDecimal getR48_STAGE1_PROVISIONS() {
		return R48_STAGE1_PROVISIONS;
	}
	public void setR48_STAGE1_PROVISIONS(BigDecimal r48_STAGE1_PROVISIONS) {
		R48_STAGE1_PROVISIONS = r48_STAGE1_PROVISIONS;
	}
	public BigDecimal getR48_QUALIFY_STAGE2_PROVISIONS() {
		return R48_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR48_QUALIFY_STAGE2_PROVISIONS(BigDecimal r48_QUALIFY_STAGE2_PROVISIONS) {
		R48_QUALIFY_STAGE2_PROVISIONS = r48_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR48_TOTAL_GENERAL_PROVISIONS() {
		return R48_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR48_TOTAL_GENERAL_PROVISIONS(BigDecimal r48_TOTAL_GENERAL_PROVISIONS) {
		R48_TOTAL_GENERAL_PROVISIONS = r48_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR49_PRODUCT() {
		return R49_PRODUCT;
	}
	public void setR49_PRODUCT(String r49_PRODUCT) {
		R49_PRODUCT = r49_PRODUCT;
	}
	public BigDecimal getR49_STAGE1_PROVISIONS() {
		return R49_STAGE1_PROVISIONS;
	}
	public void setR49_STAGE1_PROVISIONS(BigDecimal r49_STAGE1_PROVISIONS) {
		R49_STAGE1_PROVISIONS = r49_STAGE1_PROVISIONS;
	}
	public BigDecimal getR49_QUALIFY_STAGE2_PROVISIONS() {
		return R49_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR49_QUALIFY_STAGE2_PROVISIONS(BigDecimal r49_QUALIFY_STAGE2_PROVISIONS) {
		R49_QUALIFY_STAGE2_PROVISIONS = r49_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR49_TOTAL_GENERAL_PROVISIONS() {
		return R49_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR49_TOTAL_GENERAL_PROVISIONS(BigDecimal r49_TOTAL_GENERAL_PROVISIONS) {
		R49_TOTAL_GENERAL_PROVISIONS = r49_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR50_PRODUCT() {
		return R50_PRODUCT;
	}
	public void setR50_PRODUCT(String r50_PRODUCT) {
		R50_PRODUCT = r50_PRODUCT;
	}
	public BigDecimal getR50_STAGE1_PROVISIONS() {
		return R50_STAGE1_PROVISIONS;
	}
	public void setR50_STAGE1_PROVISIONS(BigDecimal r50_STAGE1_PROVISIONS) {
		R50_STAGE1_PROVISIONS = r50_STAGE1_PROVISIONS;
	}
	public BigDecimal getR50_QUALIFY_STAGE2_PROVISIONS() {
		return R50_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR50_QUALIFY_STAGE2_PROVISIONS(BigDecimal r50_QUALIFY_STAGE2_PROVISIONS) {
		R50_QUALIFY_STAGE2_PROVISIONS = r50_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR50_TOTAL_GENERAL_PROVISIONS() {
		return R50_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR50_TOTAL_GENERAL_PROVISIONS(BigDecimal r50_TOTAL_GENERAL_PROVISIONS) {
		R50_TOTAL_GENERAL_PROVISIONS = r50_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR51_PRODUCT() {
		return R51_PRODUCT;
	}
	public void setR51_PRODUCT(String r51_PRODUCT) {
		R51_PRODUCT = r51_PRODUCT;
	}
	public BigDecimal getR51_STAGE1_PROVISIONS() {
		return R51_STAGE1_PROVISIONS;
	}
	public void setR51_STAGE1_PROVISIONS(BigDecimal r51_STAGE1_PROVISIONS) {
		R51_STAGE1_PROVISIONS = r51_STAGE1_PROVISIONS;
	}
	public BigDecimal getR51_QUALIFY_STAGE2_PROVISIONS() {
		return R51_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR51_QUALIFY_STAGE2_PROVISIONS(BigDecimal r51_QUALIFY_STAGE2_PROVISIONS) {
		R51_QUALIFY_STAGE2_PROVISIONS = r51_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR51_TOTAL_GENERAL_PROVISIONS() {
		return R51_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR51_TOTAL_GENERAL_PROVISIONS(BigDecimal r51_TOTAL_GENERAL_PROVISIONS) {
		R51_TOTAL_GENERAL_PROVISIONS = r51_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR52_PRODUCT() {
		return R52_PRODUCT;
	}
	public void setR52_PRODUCT(String r52_PRODUCT) {
		R52_PRODUCT = r52_PRODUCT;
	}
	public BigDecimal getR52_STAGE1_PROVISIONS() {
		return R52_STAGE1_PROVISIONS;
	}
	public void setR52_STAGE1_PROVISIONS(BigDecimal r52_STAGE1_PROVISIONS) {
		R52_STAGE1_PROVISIONS = r52_STAGE1_PROVISIONS;
	}
	public BigDecimal getR52_QUALIFY_STAGE2_PROVISIONS() {
		return R52_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR52_QUALIFY_STAGE2_PROVISIONS(BigDecimal r52_QUALIFY_STAGE2_PROVISIONS) {
		R52_QUALIFY_STAGE2_PROVISIONS = r52_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR52_TOTAL_GENERAL_PROVISIONS() {
		return R52_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR52_TOTAL_GENERAL_PROVISIONS(BigDecimal r52_TOTAL_GENERAL_PROVISIONS) {
		R52_TOTAL_GENERAL_PROVISIONS = r52_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR53_PRODUCT() {
		return R53_PRODUCT;
	}
	public void setR53_PRODUCT(String r53_PRODUCT) {
		R53_PRODUCT = r53_PRODUCT;
	}
	public BigDecimal getR53_STAGE1_PROVISIONS() {
		return R53_STAGE1_PROVISIONS;
	}
	public void setR53_STAGE1_PROVISIONS(BigDecimal r53_STAGE1_PROVISIONS) {
		R53_STAGE1_PROVISIONS = r53_STAGE1_PROVISIONS;
	}
	public BigDecimal getR53_QUALIFY_STAGE2_PROVISIONS() {
		return R53_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR53_QUALIFY_STAGE2_PROVISIONS(BigDecimal r53_QUALIFY_STAGE2_PROVISIONS) {
		R53_QUALIFY_STAGE2_PROVISIONS = r53_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR53_TOTAL_GENERAL_PROVISIONS() {
		return R53_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR53_TOTAL_GENERAL_PROVISIONS(BigDecimal r53_TOTAL_GENERAL_PROVISIONS) {
		R53_TOTAL_GENERAL_PROVISIONS = r53_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR54_PRODUCT() {
		return R54_PRODUCT;
	}
	public void setR54_PRODUCT(String r54_PRODUCT) {
		R54_PRODUCT = r54_PRODUCT;
	}
	public BigDecimal getR54_STAGE1_PROVISIONS() {
		return R54_STAGE1_PROVISIONS;
	}
	public void setR54_STAGE1_PROVISIONS(BigDecimal r54_STAGE1_PROVISIONS) {
		R54_STAGE1_PROVISIONS = r54_STAGE1_PROVISIONS;
	}
	public BigDecimal getR54_QUALIFY_STAGE2_PROVISIONS() {
		return R54_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR54_QUALIFY_STAGE2_PROVISIONS(BigDecimal r54_QUALIFY_STAGE2_PROVISIONS) {
		R54_QUALIFY_STAGE2_PROVISIONS = r54_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR54_TOTAL_GENERAL_PROVISIONS() {
		return R54_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR54_TOTAL_GENERAL_PROVISIONS(BigDecimal r54_TOTAL_GENERAL_PROVISIONS) {
		R54_TOTAL_GENERAL_PROVISIONS = r54_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR55_PRODUCT() {
		return R55_PRODUCT;
	}
	public void setR55_PRODUCT(String r55_PRODUCT) {
		R55_PRODUCT = r55_PRODUCT;
	}
	public BigDecimal getR55_STAGE1_PROVISIONS() {
		return R55_STAGE1_PROVISIONS;
	}
	public void setR55_STAGE1_PROVISIONS(BigDecimal r55_STAGE1_PROVISIONS) {
		R55_STAGE1_PROVISIONS = r55_STAGE1_PROVISIONS;
	}
	public BigDecimal getR55_QUALIFY_STAGE2_PROVISIONS() {
		return R55_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR55_QUALIFY_STAGE2_PROVISIONS(BigDecimal r55_QUALIFY_STAGE2_PROVISIONS) {
		R55_QUALIFY_STAGE2_PROVISIONS = r55_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR55_TOTAL_GENERAL_PROVISIONS() {
		return R55_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR55_TOTAL_GENERAL_PROVISIONS(BigDecimal r55_TOTAL_GENERAL_PROVISIONS) {
		R55_TOTAL_GENERAL_PROVISIONS = r55_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR56_PRODUCT() {
		return R56_PRODUCT;
	}
	public void setR56_PRODUCT(String r56_PRODUCT) {
		R56_PRODUCT = r56_PRODUCT;
	}
	public BigDecimal getR56_STAGE1_PROVISIONS() {
		return R56_STAGE1_PROVISIONS;
	}
	public void setR56_STAGE1_PROVISIONS(BigDecimal r56_STAGE1_PROVISIONS) {
		R56_STAGE1_PROVISIONS = r56_STAGE1_PROVISIONS;
	}
	public BigDecimal getR56_QUALIFY_STAGE2_PROVISIONS() {
		return R56_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR56_QUALIFY_STAGE2_PROVISIONS(BigDecimal r56_QUALIFY_STAGE2_PROVISIONS) {
		R56_QUALIFY_STAGE2_PROVISIONS = r56_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR56_TOTAL_GENERAL_PROVISIONS() {
		return R56_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR56_TOTAL_GENERAL_PROVISIONS(BigDecimal r56_TOTAL_GENERAL_PROVISIONS) {
		R56_TOTAL_GENERAL_PROVISIONS = r56_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR57_PRODUCT() {
		return R57_PRODUCT;
	}
	public void setR57_PRODUCT(String r57_PRODUCT) {
		R57_PRODUCT = r57_PRODUCT;
	}
	public BigDecimal getR57_STAGE1_PROVISIONS() {
		return R57_STAGE1_PROVISIONS;
	}
	public void setR57_STAGE1_PROVISIONS(BigDecimal r57_STAGE1_PROVISIONS) {
		R57_STAGE1_PROVISIONS = r57_STAGE1_PROVISIONS;
	}
	public BigDecimal getR57_QUALIFY_STAGE2_PROVISIONS() {
		return R57_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR57_QUALIFY_STAGE2_PROVISIONS(BigDecimal r57_QUALIFY_STAGE2_PROVISIONS) {
		R57_QUALIFY_STAGE2_PROVISIONS = r57_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR57_TOTAL_GENERAL_PROVISIONS() {
		return R57_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR57_TOTAL_GENERAL_PROVISIONS(BigDecimal r57_TOTAL_GENERAL_PROVISIONS) {
		R57_TOTAL_GENERAL_PROVISIONS = r57_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR58_PRODUCT() {
		return R58_PRODUCT;
	}
	public void setR58_PRODUCT(String r58_PRODUCT) {
		R58_PRODUCT = r58_PRODUCT;
	}
	public BigDecimal getR58_STAGE1_PROVISIONS() {
		return R58_STAGE1_PROVISIONS;
	}
	public void setR58_STAGE1_PROVISIONS(BigDecimal r58_STAGE1_PROVISIONS) {
		R58_STAGE1_PROVISIONS = r58_STAGE1_PROVISIONS;
	}
	public BigDecimal getR58_QUALIFY_STAGE2_PROVISIONS() {
		return R58_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR58_QUALIFY_STAGE2_PROVISIONS(BigDecimal r58_QUALIFY_STAGE2_PROVISIONS) {
		R58_QUALIFY_STAGE2_PROVISIONS = r58_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR58_TOTAL_GENERAL_PROVISIONS() {
		return R58_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR58_TOTAL_GENERAL_PROVISIONS(BigDecimal r58_TOTAL_GENERAL_PROVISIONS) {
		R58_TOTAL_GENERAL_PROVISIONS = r58_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR59_PRODUCT() {
		return R59_PRODUCT;
	}
	public void setR59_PRODUCT(String r59_PRODUCT) {
		R59_PRODUCT = r59_PRODUCT;
	}
	public BigDecimal getR59_STAGE1_PROVISIONS() {
		return R59_STAGE1_PROVISIONS;
	}
	public void setR59_STAGE1_PROVISIONS(BigDecimal r59_STAGE1_PROVISIONS) {
		R59_STAGE1_PROVISIONS = r59_STAGE1_PROVISIONS;
	}
	public BigDecimal getR59_QUALIFY_STAGE2_PROVISIONS() {
		return R59_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR59_QUALIFY_STAGE2_PROVISIONS(BigDecimal r59_QUALIFY_STAGE2_PROVISIONS) {
		R59_QUALIFY_STAGE2_PROVISIONS = r59_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR59_TOTAL_GENERAL_PROVISIONS() {
		return R59_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR59_TOTAL_GENERAL_PROVISIONS(BigDecimal r59_TOTAL_GENERAL_PROVISIONS) {
		R59_TOTAL_GENERAL_PROVISIONS = r59_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR60_PRODUCT() {
		return R60_PRODUCT;
	}
	public void setR60_PRODUCT(String r60_PRODUCT) {
		R60_PRODUCT = r60_PRODUCT;
	}
	public BigDecimal getR60_STAGE1_PROVISIONS() {
		return R60_STAGE1_PROVISIONS;
	}
	public void setR60_STAGE1_PROVISIONS(BigDecimal r60_STAGE1_PROVISIONS) {
		R60_STAGE1_PROVISIONS = r60_STAGE1_PROVISIONS;
	}
	public BigDecimal getR60_QUALIFY_STAGE2_PROVISIONS() {
		return R60_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR60_QUALIFY_STAGE2_PROVISIONS(BigDecimal r60_QUALIFY_STAGE2_PROVISIONS) {
		R60_QUALIFY_STAGE2_PROVISIONS = r60_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR60_TOTAL_GENERAL_PROVISIONS() {
		return R60_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR60_TOTAL_GENERAL_PROVISIONS(BigDecimal r60_TOTAL_GENERAL_PROVISIONS) {
		R60_TOTAL_GENERAL_PROVISIONS = r60_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR61_PRODUCT() {
		return R61_PRODUCT;
	}
	public void setR61_PRODUCT(String r61_PRODUCT) {
		R61_PRODUCT = r61_PRODUCT;
	}
	public BigDecimal getR61_STAGE1_PROVISIONS() {
		return R61_STAGE1_PROVISIONS;
	}
	public void setR61_STAGE1_PROVISIONS(BigDecimal r61_STAGE1_PROVISIONS) {
		R61_STAGE1_PROVISIONS = r61_STAGE1_PROVISIONS;
	}
	public BigDecimal getR61_QUALIFY_STAGE2_PROVISIONS() {
		return R61_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR61_QUALIFY_STAGE2_PROVISIONS(BigDecimal r61_QUALIFY_STAGE2_PROVISIONS) {
		R61_QUALIFY_STAGE2_PROVISIONS = r61_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR61_TOTAL_GENERAL_PROVISIONS() {
		return R61_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR61_TOTAL_GENERAL_PROVISIONS(BigDecimal r61_TOTAL_GENERAL_PROVISIONS) {
		R61_TOTAL_GENERAL_PROVISIONS = r61_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR62_PRODUCT() {
		return R62_PRODUCT;
	}
	public void setR62_PRODUCT(String r62_PRODUCT) {
		R62_PRODUCT = r62_PRODUCT;
	}
	public BigDecimal getR62_STAGE1_PROVISIONS() {
		return R62_STAGE1_PROVISIONS;
	}
	public void setR62_STAGE1_PROVISIONS(BigDecimal r62_STAGE1_PROVISIONS) {
		R62_STAGE1_PROVISIONS = r62_STAGE1_PROVISIONS;
	}
	public BigDecimal getR62_QUALIFY_STAGE2_PROVISIONS() {
		return R62_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR62_QUALIFY_STAGE2_PROVISIONS(BigDecimal r62_QUALIFY_STAGE2_PROVISIONS) {
		R62_QUALIFY_STAGE2_PROVISIONS = r62_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR62_TOTAL_GENERAL_PROVISIONS() {
		return R62_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR62_TOTAL_GENERAL_PROVISIONS(BigDecimal r62_TOTAL_GENERAL_PROVISIONS) {
		R62_TOTAL_GENERAL_PROVISIONS = r62_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR63_PRODUCT() {
		return R63_PRODUCT;
	}
	public void setR63_PRODUCT(String r63_PRODUCT) {
		R63_PRODUCT = r63_PRODUCT;
	}
	public BigDecimal getR63_STAGE1_PROVISIONS() {
		return R63_STAGE1_PROVISIONS;
	}
	public void setR63_STAGE1_PROVISIONS(BigDecimal r63_STAGE1_PROVISIONS) {
		R63_STAGE1_PROVISIONS = r63_STAGE1_PROVISIONS;
	}
	public BigDecimal getR63_QUALIFY_STAGE2_PROVISIONS() {
		return R63_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR63_QUALIFY_STAGE2_PROVISIONS(BigDecimal r63_QUALIFY_STAGE2_PROVISIONS) {
		R63_QUALIFY_STAGE2_PROVISIONS = r63_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR63_TOTAL_GENERAL_PROVISIONS() {
		return R63_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR63_TOTAL_GENERAL_PROVISIONS(BigDecimal r63_TOTAL_GENERAL_PROVISIONS) {
		R63_TOTAL_GENERAL_PROVISIONS = r63_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR64_PRODUCT() {
		return R64_PRODUCT;
	}
	public void setR64_PRODUCT(String r64_PRODUCT) {
		R64_PRODUCT = r64_PRODUCT;
	}
	public BigDecimal getR64_STAGE1_PROVISIONS() {
		return R64_STAGE1_PROVISIONS;
	}
	public void setR64_STAGE1_PROVISIONS(BigDecimal r64_STAGE1_PROVISIONS) {
		R64_STAGE1_PROVISIONS = r64_STAGE1_PROVISIONS;
	}
	public BigDecimal getR64_QUALIFY_STAGE2_PROVISIONS() {
		return R64_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR64_QUALIFY_STAGE2_PROVISIONS(BigDecimal r64_QUALIFY_STAGE2_PROVISIONS) {
		R64_QUALIFY_STAGE2_PROVISIONS = r64_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR64_TOTAL_GENERAL_PROVISIONS() {
		return R64_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR64_TOTAL_GENERAL_PROVISIONS(BigDecimal r64_TOTAL_GENERAL_PROVISIONS) {
		R64_TOTAL_GENERAL_PROVISIONS = r64_TOTAL_GENERAL_PROVISIONS;
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
// DETAIL ENTITY  M_GP
// =====================================================	

public class M_GP_Detail_RowMapper implements RowMapper<M_GP_Detail_Entity> {

    @Override
    public M_GP_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_GP_Detail_Entity obj = new M_GP_Detail_Entity();

// =========================
// R11
// =========================
obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
obj.setR11_STAGE1_PROVISIONS(rs.getBigDecimal("R11_STAGE1_PROVISIONS"));
obj.setR11_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R11_QUALIFY_STAGE2_PROVISIONS"));
obj.setR11_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R11_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R12
// =========================
obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
obj.setR12_STAGE1_PROVISIONS(rs.getBigDecimal("R12_STAGE1_PROVISIONS"));
obj.setR12_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R12_QUALIFY_STAGE2_PROVISIONS"));
obj.setR12_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R12_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R13
// =========================
obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
obj.setR13_STAGE1_PROVISIONS(rs.getBigDecimal("R13_STAGE1_PROVISIONS"));
obj.setR13_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R13_QUALIFY_STAGE2_PROVISIONS"));
obj.setR13_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R13_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R14
// =========================
obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
obj.setR14_STAGE1_PROVISIONS(rs.getBigDecimal("R14_STAGE1_PROVISIONS"));
obj.setR14_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R14_QUALIFY_STAGE2_PROVISIONS"));
obj.setR14_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R14_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R15
// =========================
obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
obj.setR15_STAGE1_PROVISIONS(rs.getBigDecimal("R15_STAGE1_PROVISIONS"));
obj.setR15_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R15_QUALIFY_STAGE2_PROVISIONS"));
obj.setR15_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R15_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R16
// =========================
obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
obj.setR16_STAGE1_PROVISIONS(rs.getBigDecimal("R16_STAGE1_PROVISIONS"));
obj.setR16_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R16_QUALIFY_STAGE2_PROVISIONS"));
obj.setR16_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R16_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R17
// =========================
obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
obj.setR17_STAGE1_PROVISIONS(rs.getBigDecimal("R17_STAGE1_PROVISIONS"));
obj.setR17_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R17_QUALIFY_STAGE2_PROVISIONS"));
obj.setR17_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R17_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R18
// =========================
obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
obj.setR18_STAGE1_PROVISIONS(rs.getBigDecimal("R18_STAGE1_PROVISIONS"));
obj.setR18_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R18_QUALIFY_STAGE2_PROVISIONS"));
obj.setR18_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R18_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R19
// =========================
obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
obj.setR19_STAGE1_PROVISIONS(rs.getBigDecimal("R19_STAGE1_PROVISIONS"));
obj.setR19_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R19_QUALIFY_STAGE2_PROVISIONS"));
obj.setR19_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R19_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R20
// =========================
obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
obj.setR20_STAGE1_PROVISIONS(rs.getBigDecimal("R20_STAGE1_PROVISIONS"));
obj.setR20_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R20_QUALIFY_STAGE2_PROVISIONS"));
obj.setR20_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R20_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R21
// =========================
obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
obj.setR21_STAGE1_PROVISIONS(rs.getBigDecimal("R21_STAGE1_PROVISIONS"));
obj.setR21_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R21_QUALIFY_STAGE2_PROVISIONS"));
obj.setR21_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R21_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R22
// =========================
obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
obj.setR22_STAGE1_PROVISIONS(rs.getBigDecimal("R22_STAGE1_PROVISIONS"));
obj.setR22_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R22_QUALIFY_STAGE2_PROVISIONS"));
obj.setR22_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R22_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R23
// =========================
obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
obj.setR23_STAGE1_PROVISIONS(rs.getBigDecimal("R23_STAGE1_PROVISIONS"));
obj.setR23_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R23_QUALIFY_STAGE2_PROVISIONS"));
obj.setR23_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R23_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R24
// =========================
obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
obj.setR24_STAGE1_PROVISIONS(rs.getBigDecimal("R24_STAGE1_PROVISIONS"));
obj.setR24_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R24_QUALIFY_STAGE2_PROVISIONS"));
obj.setR24_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R24_TOTAL_GENERAL_PROVISIONS"));


// =========================
// R25
// =========================
obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
obj.setR25_STAGE1_PROVISIONS(rs.getBigDecimal("R25_STAGE1_PROVISIONS"));
obj.setR25_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R25_QUALIFY_STAGE2_PROVISIONS"));
obj.setR25_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R25_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R26
// =========================
obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
obj.setR26_STAGE1_PROVISIONS(rs.getBigDecimal("R26_STAGE1_PROVISIONS"));
obj.setR26_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R26_QUALIFY_STAGE2_PROVISIONS"));
obj.setR26_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R26_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R27
// =========================
obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
obj.setR27_STAGE1_PROVISIONS(rs.getBigDecimal("R27_STAGE1_PROVISIONS"));
obj.setR27_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R27_QUALIFY_STAGE2_PROVISIONS"));
obj.setR27_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R27_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R28
// =========================
obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
obj.setR28_STAGE1_PROVISIONS(rs.getBigDecimal("R28_STAGE1_PROVISIONS"));
obj.setR28_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R28_QUALIFY_STAGE2_PROVISIONS"));
obj.setR28_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R28_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R29
// =========================
obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
obj.setR29_STAGE1_PROVISIONS(rs.getBigDecimal("R29_STAGE1_PROVISIONS"));
obj.setR29_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R29_QUALIFY_STAGE2_PROVISIONS"));
obj.setR29_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R29_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R30
// =========================
obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
obj.setR30_STAGE1_PROVISIONS(rs.getBigDecimal("R30_STAGE1_PROVISIONS"));
obj.setR30_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R30_QUALIFY_STAGE2_PROVISIONS"));
obj.setR30_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R30_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R31
// =========================
obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
obj.setR31_STAGE1_PROVISIONS(rs.getBigDecimal("R31_STAGE1_PROVISIONS"));
obj.setR31_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R31_QUALIFY_STAGE2_PROVISIONS"));
obj.setR31_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R31_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R32
// =========================
obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
obj.setR32_STAGE1_PROVISIONS(rs.getBigDecimal("R32_STAGE1_PROVISIONS"));
obj.setR32_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R32_QUALIFY_STAGE2_PROVISIONS"));
obj.setR32_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R32_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R33
// =========================
obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
obj.setR33_STAGE1_PROVISIONS(rs.getBigDecimal("R33_STAGE1_PROVISIONS"));
obj.setR33_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R33_QUALIFY_STAGE2_PROVISIONS"));
obj.setR33_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R33_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R34
// =========================
obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
obj.setR34_STAGE1_PROVISIONS(rs.getBigDecimal("R34_STAGE1_PROVISIONS"));
obj.setR34_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R34_QUALIFY_STAGE2_PROVISIONS"));
obj.setR34_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R34_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R35
// =========================
obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
obj.setR35_STAGE1_PROVISIONS(rs.getBigDecimal("R35_STAGE1_PROVISIONS"));
obj.setR35_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R35_QUALIFY_STAGE2_PROVISIONS"));
obj.setR35_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R35_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R36
// =========================
obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
obj.setR36_STAGE1_PROVISIONS(rs.getBigDecimal("R36_STAGE1_PROVISIONS"));
obj.setR36_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R36_QUALIFY_STAGE2_PROVISIONS"));
obj.setR36_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R36_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R37
// =========================
obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
obj.setR37_STAGE1_PROVISIONS(rs.getBigDecimal("R37_STAGE1_PROVISIONS"));
obj.setR37_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R37_QUALIFY_STAGE2_PROVISIONS"));
obj.setR37_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R37_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R38
// =========================
obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
obj.setR38_STAGE1_PROVISIONS(rs.getBigDecimal("R38_STAGE1_PROVISIONS"));
obj.setR38_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R38_QUALIFY_STAGE2_PROVISIONS"));
obj.setR38_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R38_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R39
// =========================
obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
obj.setR39_STAGE1_PROVISIONS(rs.getBigDecimal("R39_STAGE1_PROVISIONS"));
obj.setR39_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R39_QUALIFY_STAGE2_PROVISIONS"));
obj.setR39_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R39_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R40
// =========================
obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
obj.setR40_STAGE1_PROVISIONS(rs.getBigDecimal("R40_STAGE1_PROVISIONS"));
obj.setR40_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R40_QUALIFY_STAGE2_PROVISIONS"));
obj.setR40_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R40_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R41
// =========================
obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
obj.setR41_STAGE1_PROVISIONS(rs.getBigDecimal("R41_STAGE1_PROVISIONS"));
obj.setR41_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R41_QUALIFY_STAGE2_PROVISIONS"));
obj.setR41_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R41_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R42
// =========================
obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
obj.setR42_STAGE1_PROVISIONS(rs.getBigDecimal("R42_STAGE1_PROVISIONS"));
obj.setR42_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R42_QUALIFY_STAGE2_PROVISIONS"));
obj.setR42_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R42_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R43
// =========================
obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
obj.setR43_STAGE1_PROVISIONS(rs.getBigDecimal("R43_STAGE1_PROVISIONS"));
obj.setR43_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R43_QUALIFY_STAGE2_PROVISIONS"));
obj.setR43_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R43_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R44
// =========================
obj.setR44_PRODUCT(rs.getString("R44_PRODUCT"));
obj.setR44_STAGE1_PROVISIONS(rs.getBigDecimal("R44_STAGE1_PROVISIONS"));
obj.setR44_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R44_QUALIFY_STAGE2_PROVISIONS"));
obj.setR44_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R44_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R45
// =========================
obj.setR45_PRODUCT(rs.getString("R45_PRODUCT"));
obj.setR45_STAGE1_PROVISIONS(rs.getBigDecimal("R45_STAGE1_PROVISIONS"));
obj.setR45_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R45_QUALIFY_STAGE2_PROVISIONS"));
obj.setR45_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R45_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R46
// =========================
obj.setR46_PRODUCT(rs.getString("R46_PRODUCT"));
obj.setR46_STAGE1_PROVISIONS(rs.getBigDecimal("R46_STAGE1_PROVISIONS"));
obj.setR46_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R46_QUALIFY_STAGE2_PROVISIONS"));
obj.setR46_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R46_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R47
// =========================
obj.setR47_PRODUCT(rs.getString("R47_PRODUCT"));
obj.setR47_STAGE1_PROVISIONS(rs.getBigDecimal("R47_STAGE1_PROVISIONS"));
obj.setR47_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R47_QUALIFY_STAGE2_PROVISIONS"));
obj.setR47_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R47_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R48
// =========================
obj.setR48_PRODUCT(rs.getString("R48_PRODUCT"));
obj.setR48_STAGE1_PROVISIONS(rs.getBigDecimal("R48_STAGE1_PROVISIONS"));
obj.setR48_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R48_QUALIFY_STAGE2_PROVISIONS"));
obj.setR48_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R48_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R49
// =========================
obj.setR49_PRODUCT(rs.getString("R49_PRODUCT"));
obj.setR49_STAGE1_PROVISIONS(rs.getBigDecimal("R49_STAGE1_PROVISIONS"));
obj.setR49_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R49_QUALIFY_STAGE2_PROVISIONS"));
obj.setR49_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R49_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R50
// =========================
obj.setR50_PRODUCT(rs.getString("R50_PRODUCT"));
obj.setR50_STAGE1_PROVISIONS(rs.getBigDecimal("R50_STAGE1_PROVISIONS"));
obj.setR50_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R50_QUALIFY_STAGE2_PROVISIONS"));
obj.setR50_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R50_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R51
// =========================
obj.setR51_PRODUCT(rs.getString("R51_PRODUCT"));
obj.setR51_STAGE1_PROVISIONS(rs.getBigDecimal("R51_STAGE1_PROVISIONS"));
obj.setR51_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R51_QUALIFY_STAGE2_PROVISIONS"));
obj.setR51_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R51_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R52
// =========================
obj.setR52_PRODUCT(rs.getString("R52_PRODUCT"));
obj.setR52_STAGE1_PROVISIONS(rs.getBigDecimal("R52_STAGE1_PROVISIONS"));
obj.setR52_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R52_QUALIFY_STAGE2_PROVISIONS"));
obj.setR52_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R52_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R53
// =========================
obj.setR53_PRODUCT(rs.getString("R53_PRODUCT"));
obj.setR53_STAGE1_PROVISIONS(rs.getBigDecimal("R53_STAGE1_PROVISIONS"));
obj.setR53_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R53_QUALIFY_STAGE2_PROVISIONS"));
obj.setR53_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R53_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R54
// =========================
obj.setR54_PRODUCT(rs.getString("R54_PRODUCT"));
obj.setR54_STAGE1_PROVISIONS(rs.getBigDecimal("R54_STAGE1_PROVISIONS"));
obj.setR54_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R54_QUALIFY_STAGE2_PROVISIONS"));
obj.setR54_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R54_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R55
// =========================
obj.setR55_PRODUCT(rs.getString("R55_PRODUCT"));
obj.setR55_STAGE1_PROVISIONS(rs.getBigDecimal("R55_STAGE1_PROVISIONS"));
obj.setR55_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R55_QUALIFY_STAGE2_PROVISIONS"));
obj.setR55_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R55_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R56
// =========================
obj.setR56_PRODUCT(rs.getString("R56_PRODUCT"));
obj.setR56_STAGE1_PROVISIONS(rs.getBigDecimal("R56_STAGE1_PROVISIONS"));
obj.setR56_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R56_QUALIFY_STAGE2_PROVISIONS"));
obj.setR56_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R56_TOTAL_GENERAL_PROVISIONS"));


// =========================
// R57
// =========================
obj.setR57_PRODUCT(rs.getString("R57_PRODUCT"));
obj.setR57_STAGE1_PROVISIONS(rs.getBigDecimal("R57_STAGE1_PROVISIONS"));
obj.setR57_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R57_QUALIFY_STAGE2_PROVISIONS"));
obj.setR57_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R57_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R58
// =========================
obj.setR58_PRODUCT(rs.getString("R58_PRODUCT"));
obj.setR58_STAGE1_PROVISIONS(rs.getBigDecimal("R58_STAGE1_PROVISIONS"));
obj.setR58_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R58_QUALIFY_STAGE2_PROVISIONS"));
obj.setR58_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R58_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R59
// =========================
obj.setR59_PRODUCT(rs.getString("R59_PRODUCT"));
obj.setR59_STAGE1_PROVISIONS(rs.getBigDecimal("R59_STAGE1_PROVISIONS"));
obj.setR59_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R59_QUALIFY_STAGE2_PROVISIONS"));
obj.setR59_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R59_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R60
// =========================
obj.setR60_PRODUCT(rs.getString("R60_PRODUCT"));
obj.setR60_STAGE1_PROVISIONS(rs.getBigDecimal("R60_STAGE1_PROVISIONS"));
obj.setR60_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R60_QUALIFY_STAGE2_PROVISIONS"));
obj.setR60_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R60_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R61
// =========================
obj.setR61_PRODUCT(rs.getString("R61_PRODUCT"));
obj.setR61_STAGE1_PROVISIONS(rs.getBigDecimal("R61_STAGE1_PROVISIONS"));
obj.setR61_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R61_QUALIFY_STAGE2_PROVISIONS"));
obj.setR61_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R61_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R62
// =========================
obj.setR62_PRODUCT(rs.getString("R62_PRODUCT"));
obj.setR62_STAGE1_PROVISIONS(rs.getBigDecimal("R62_STAGE1_PROVISIONS"));
obj.setR62_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R62_QUALIFY_STAGE2_PROVISIONS"));
obj.setR62_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R62_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R63
// =========================
obj.setR63_PRODUCT(rs.getString("R63_PRODUCT"));
obj.setR63_STAGE1_PROVISIONS(rs.getBigDecimal("R63_STAGE1_PROVISIONS"));
obj.setR63_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R63_QUALIFY_STAGE2_PROVISIONS"));
obj.setR63_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R63_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R64
// =========================
obj.setR64_PRODUCT(rs.getString("R64_PRODUCT"));
obj.setR64_STAGE1_PROVISIONS(rs.getBigDecimal("R64_STAGE1_PROVISIONS"));
obj.setR64_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R64_QUALIFY_STAGE2_PROVISIONS"));
obj.setR64_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R64_TOTAL_GENERAL_PROVISIONS"));


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

public class M_GP_Detail_Entity {

   
private String R11_PRODUCT;
	private BigDecimal R11_STAGE1_PROVISIONS;
	private BigDecimal R11_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R11_TOTAL_GENERAL_PROVISIONS;

	private String R12_PRODUCT;
	private BigDecimal R12_STAGE1_PROVISIONS;
	private BigDecimal R12_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R12_TOTAL_GENERAL_PROVISIONS;

	private String R13_PRODUCT;
	private BigDecimal R13_STAGE1_PROVISIONS;
	private BigDecimal R13_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R13_TOTAL_GENERAL_PROVISIONS;

	private String R14_PRODUCT;
	private BigDecimal R14_STAGE1_PROVISIONS;
	private BigDecimal R14_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R14_TOTAL_GENERAL_PROVISIONS;

	private String R15_PRODUCT;
	private BigDecimal R15_STAGE1_PROVISIONS;
	private BigDecimal R15_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R15_TOTAL_GENERAL_PROVISIONS;

	private String R16_PRODUCT;
	private BigDecimal R16_STAGE1_PROVISIONS;
	private BigDecimal R16_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R16_TOTAL_GENERAL_PROVISIONS;

	private String R17_PRODUCT;
	private BigDecimal R17_STAGE1_PROVISIONS;
	private BigDecimal R17_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R17_TOTAL_GENERAL_PROVISIONS;

	private String R18_PRODUCT;
	private BigDecimal R18_STAGE1_PROVISIONS;
	private BigDecimal R18_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R18_TOTAL_GENERAL_PROVISIONS;

	private String R19_PRODUCT;
	private BigDecimal R19_STAGE1_PROVISIONS;
	private BigDecimal R19_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R19_TOTAL_GENERAL_PROVISIONS;

	private String R20_PRODUCT;
	private BigDecimal R20_STAGE1_PROVISIONS;
	private BigDecimal R20_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R20_TOTAL_GENERAL_PROVISIONS;

	private String R21_PRODUCT;
	private BigDecimal R21_STAGE1_PROVISIONS;
	private BigDecimal R21_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R21_TOTAL_GENERAL_PROVISIONS;

	private String R22_PRODUCT;
	private BigDecimal R22_STAGE1_PROVISIONS;
	private BigDecimal R22_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R22_TOTAL_GENERAL_PROVISIONS;

	private String R23_PRODUCT;
	private BigDecimal R23_STAGE1_PROVISIONS;
	private BigDecimal R23_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R23_TOTAL_GENERAL_PROVISIONS;

	private String R24_PRODUCT;
	private BigDecimal R24_STAGE1_PROVISIONS;
	private BigDecimal R24_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R24_TOTAL_GENERAL_PROVISIONS;

	private String R25_PRODUCT;
	private BigDecimal R25_STAGE1_PROVISIONS;
	private BigDecimal R25_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R25_TOTAL_GENERAL_PROVISIONS;

	private String R26_PRODUCT;
	private BigDecimal R26_STAGE1_PROVISIONS;
	private BigDecimal R26_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R26_TOTAL_GENERAL_PROVISIONS;

	private String R27_PRODUCT;
	private BigDecimal R27_STAGE1_PROVISIONS;
	private BigDecimal R27_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R27_TOTAL_GENERAL_PROVISIONS;

	private String R28_PRODUCT;
	private BigDecimal R28_STAGE1_PROVISIONS;
	private BigDecimal R28_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R28_TOTAL_GENERAL_PROVISIONS;

	private String R29_PRODUCT;
	private BigDecimal R29_STAGE1_PROVISIONS;
	private BigDecimal R29_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R29_TOTAL_GENERAL_PROVISIONS;

	private String R30_PRODUCT;
	private BigDecimal R30_STAGE1_PROVISIONS;
	private BigDecimal R30_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R30_TOTAL_GENERAL_PROVISIONS;

	private String R31_PRODUCT;
	private BigDecimal R31_STAGE1_PROVISIONS;
	private BigDecimal R31_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R31_TOTAL_GENERAL_PROVISIONS;

	private String R32_PRODUCT;
	private BigDecimal R32_STAGE1_PROVISIONS;
	private BigDecimal R32_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R32_TOTAL_GENERAL_PROVISIONS;

	private String R33_PRODUCT;
	private BigDecimal R33_STAGE1_PROVISIONS;
	private BigDecimal R33_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R33_TOTAL_GENERAL_PROVISIONS;

	private String R34_PRODUCT;
	private BigDecimal R34_STAGE1_PROVISIONS;
	private BigDecimal R34_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R34_TOTAL_GENERAL_PROVISIONS;

	private String R35_PRODUCT;
	private BigDecimal R35_STAGE1_PROVISIONS;
	private BigDecimal R35_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R35_TOTAL_GENERAL_PROVISIONS;

	private String R36_PRODUCT;
	private BigDecimal R36_STAGE1_PROVISIONS;
	private BigDecimal R36_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R36_TOTAL_GENERAL_PROVISIONS;

	private String R37_PRODUCT;
	private BigDecimal R37_STAGE1_PROVISIONS;
	private BigDecimal R37_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R37_TOTAL_GENERAL_PROVISIONS;

	private String R38_PRODUCT;
	private BigDecimal R38_STAGE1_PROVISIONS;
	private BigDecimal R38_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R38_TOTAL_GENERAL_PROVISIONS;

	private String R39_PRODUCT;
	private BigDecimal R39_STAGE1_PROVISIONS;
	private BigDecimal R39_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R39_TOTAL_GENERAL_PROVISIONS;

	private String R40_PRODUCT;
	private BigDecimal R40_STAGE1_PROVISIONS;
	private BigDecimal R40_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R40_TOTAL_GENERAL_PROVISIONS;

	private String R41_PRODUCT;
	private BigDecimal R41_STAGE1_PROVISIONS;
	private BigDecimal R41_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R41_TOTAL_GENERAL_PROVISIONS;

	private String R42_PRODUCT;
	private BigDecimal R42_STAGE1_PROVISIONS;
	private BigDecimal R42_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R42_TOTAL_GENERAL_PROVISIONS;

	private String R43_PRODUCT;
	private BigDecimal R43_STAGE1_PROVISIONS;
	private BigDecimal R43_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R43_TOTAL_GENERAL_PROVISIONS;

	private String R44_PRODUCT;
	private BigDecimal R44_STAGE1_PROVISIONS;
	private BigDecimal R44_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R44_TOTAL_GENERAL_PROVISIONS;

	private String R45_PRODUCT;
	private BigDecimal R45_STAGE1_PROVISIONS;
	private BigDecimal R45_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R45_TOTAL_GENERAL_PROVISIONS;

	private String R46_PRODUCT;
	private BigDecimal R46_STAGE1_PROVISIONS;
	private BigDecimal R46_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R46_TOTAL_GENERAL_PROVISIONS;

	private String R47_PRODUCT;
	private BigDecimal R47_STAGE1_PROVISIONS;
	private BigDecimal R47_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R47_TOTAL_GENERAL_PROVISIONS;

	private String R48_PRODUCT;
	private BigDecimal R48_STAGE1_PROVISIONS;
	private BigDecimal R48_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R48_TOTAL_GENERAL_PROVISIONS;

	private String R49_PRODUCT;
	private BigDecimal R49_STAGE1_PROVISIONS;
	private BigDecimal R49_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R49_TOTAL_GENERAL_PROVISIONS;

	private String R50_PRODUCT;
	private BigDecimal R50_STAGE1_PROVISIONS;
	private BigDecimal R50_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R50_TOTAL_GENERAL_PROVISIONS;

	private String R51_PRODUCT;
	private BigDecimal R51_STAGE1_PROVISIONS;
	private BigDecimal R51_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R51_TOTAL_GENERAL_PROVISIONS;

	private String R52_PRODUCT;
	private BigDecimal R52_STAGE1_PROVISIONS;
	private BigDecimal R52_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R52_TOTAL_GENERAL_PROVISIONS;

	private String R53_PRODUCT;
	private BigDecimal R53_STAGE1_PROVISIONS;
	private BigDecimal R53_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R53_TOTAL_GENERAL_PROVISIONS;

	private String R54_PRODUCT;
	private BigDecimal R54_STAGE1_PROVISIONS;
	private BigDecimal R54_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R54_TOTAL_GENERAL_PROVISIONS;

	private String R55_PRODUCT;
	private BigDecimal R55_STAGE1_PROVISIONS;
	private BigDecimal R55_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R55_TOTAL_GENERAL_PROVISIONS;

	private String R56_PRODUCT;
	private BigDecimal R56_STAGE1_PROVISIONS;
	private BigDecimal R56_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R56_TOTAL_GENERAL_PROVISIONS;

	private String R57_PRODUCT;
	private BigDecimal R57_STAGE1_PROVISIONS;
	private BigDecimal R57_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R57_TOTAL_GENERAL_PROVISIONS;

	private String R58_PRODUCT;
	private BigDecimal R58_STAGE1_PROVISIONS;
	private BigDecimal R58_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R58_TOTAL_GENERAL_PROVISIONS;

	private String R59_PRODUCT;
	private BigDecimal R59_STAGE1_PROVISIONS;
	private BigDecimal R59_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R59_TOTAL_GENERAL_PROVISIONS;

	private String R60_PRODUCT;
	private BigDecimal R60_STAGE1_PROVISIONS;
	private BigDecimal R60_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R60_TOTAL_GENERAL_PROVISIONS;

	private String R61_PRODUCT;
	private BigDecimal R61_STAGE1_PROVISIONS;
	private BigDecimal R61_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R61_TOTAL_GENERAL_PROVISIONS;

	private String R62_PRODUCT;
	private BigDecimal R62_STAGE1_PROVISIONS;
	private BigDecimal R62_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R62_TOTAL_GENERAL_PROVISIONS;

	private String R63_PRODUCT;
	private BigDecimal R63_STAGE1_PROVISIONS;
	private BigDecimal R63_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R63_TOTAL_GENERAL_PROVISIONS;

	private String R64_PRODUCT;
	private BigDecimal R64_STAGE1_PROVISIONS;
	private BigDecimal R64_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R64_TOTAL_GENERAL_PROVISIONS;
	
	
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
	
	
	
public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_STAGE1_PROVISIONS() {
		return R11_STAGE1_PROVISIONS;
	}
	public void setR11_STAGE1_PROVISIONS(BigDecimal r11_STAGE1_PROVISIONS) {
		R11_STAGE1_PROVISIONS = r11_STAGE1_PROVISIONS;
	}
	public BigDecimal getR11_QUALIFY_STAGE2_PROVISIONS() {
		return R11_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR11_QUALIFY_STAGE2_PROVISIONS(BigDecimal r11_QUALIFY_STAGE2_PROVISIONS) {
		R11_QUALIFY_STAGE2_PROVISIONS = r11_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR11_TOTAL_GENERAL_PROVISIONS() {
		return R11_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR11_TOTAL_GENERAL_PROVISIONS(BigDecimal r11_TOTAL_GENERAL_PROVISIONS) {
		R11_TOTAL_GENERAL_PROVISIONS = r11_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_STAGE1_PROVISIONS() {
		return R12_STAGE1_PROVISIONS;
	}
	public void setR12_STAGE1_PROVISIONS(BigDecimal r12_STAGE1_PROVISIONS) {
		R12_STAGE1_PROVISIONS = r12_STAGE1_PROVISIONS;
	}
	public BigDecimal getR12_QUALIFY_STAGE2_PROVISIONS() {
		return R12_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR12_QUALIFY_STAGE2_PROVISIONS(BigDecimal r12_QUALIFY_STAGE2_PROVISIONS) {
		R12_QUALIFY_STAGE2_PROVISIONS = r12_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR12_TOTAL_GENERAL_PROVISIONS() {
		return R12_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR12_TOTAL_GENERAL_PROVISIONS(BigDecimal r12_TOTAL_GENERAL_PROVISIONS) {
		R12_TOTAL_GENERAL_PROVISIONS = r12_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_STAGE1_PROVISIONS() {
		return R13_STAGE1_PROVISIONS;
	}
	public void setR13_STAGE1_PROVISIONS(BigDecimal r13_STAGE1_PROVISIONS) {
		R13_STAGE1_PROVISIONS = r13_STAGE1_PROVISIONS;
	}
	public BigDecimal getR13_QUALIFY_STAGE2_PROVISIONS() {
		return R13_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR13_QUALIFY_STAGE2_PROVISIONS(BigDecimal r13_QUALIFY_STAGE2_PROVISIONS) {
		R13_QUALIFY_STAGE2_PROVISIONS = r13_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR13_TOTAL_GENERAL_PROVISIONS() {
		return R13_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR13_TOTAL_GENERAL_PROVISIONS(BigDecimal r13_TOTAL_GENERAL_PROVISIONS) {
		R13_TOTAL_GENERAL_PROVISIONS = r13_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_STAGE1_PROVISIONS() {
		return R14_STAGE1_PROVISIONS;
	}
	public void setR14_STAGE1_PROVISIONS(BigDecimal r14_STAGE1_PROVISIONS) {
		R14_STAGE1_PROVISIONS = r14_STAGE1_PROVISIONS;
	}
	public BigDecimal getR14_QUALIFY_STAGE2_PROVISIONS() {
		return R14_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR14_QUALIFY_STAGE2_PROVISIONS(BigDecimal r14_QUALIFY_STAGE2_PROVISIONS) {
		R14_QUALIFY_STAGE2_PROVISIONS = r14_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR14_TOTAL_GENERAL_PROVISIONS() {
		return R14_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR14_TOTAL_GENERAL_PROVISIONS(BigDecimal r14_TOTAL_GENERAL_PROVISIONS) {
		R14_TOTAL_GENERAL_PROVISIONS = r14_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_STAGE1_PROVISIONS() {
		return R15_STAGE1_PROVISIONS;
	}
	public void setR15_STAGE1_PROVISIONS(BigDecimal r15_STAGE1_PROVISIONS) {
		R15_STAGE1_PROVISIONS = r15_STAGE1_PROVISIONS;
	}
	public BigDecimal getR15_QUALIFY_STAGE2_PROVISIONS() {
		return R15_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR15_QUALIFY_STAGE2_PROVISIONS(BigDecimal r15_QUALIFY_STAGE2_PROVISIONS) {
		R15_QUALIFY_STAGE2_PROVISIONS = r15_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR15_TOTAL_GENERAL_PROVISIONS() {
		return R15_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR15_TOTAL_GENERAL_PROVISIONS(BigDecimal r15_TOTAL_GENERAL_PROVISIONS) {
		R15_TOTAL_GENERAL_PROVISIONS = r15_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_STAGE1_PROVISIONS() {
		return R16_STAGE1_PROVISIONS;
	}
	public void setR16_STAGE1_PROVISIONS(BigDecimal r16_STAGE1_PROVISIONS) {
		R16_STAGE1_PROVISIONS = r16_STAGE1_PROVISIONS;
	}
	public BigDecimal getR16_QUALIFY_STAGE2_PROVISIONS() {
		return R16_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR16_QUALIFY_STAGE2_PROVISIONS(BigDecimal r16_QUALIFY_STAGE2_PROVISIONS) {
		R16_QUALIFY_STAGE2_PROVISIONS = r16_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR16_TOTAL_GENERAL_PROVISIONS() {
		return R16_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR16_TOTAL_GENERAL_PROVISIONS(BigDecimal r16_TOTAL_GENERAL_PROVISIONS) {
		R16_TOTAL_GENERAL_PROVISIONS = r16_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_STAGE1_PROVISIONS() {
		return R17_STAGE1_PROVISIONS;
	}
	public void setR17_STAGE1_PROVISIONS(BigDecimal r17_STAGE1_PROVISIONS) {
		R17_STAGE1_PROVISIONS = r17_STAGE1_PROVISIONS;
	}
	public BigDecimal getR17_QUALIFY_STAGE2_PROVISIONS() {
		return R17_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR17_QUALIFY_STAGE2_PROVISIONS(BigDecimal r17_QUALIFY_STAGE2_PROVISIONS) {
		R17_QUALIFY_STAGE2_PROVISIONS = r17_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR17_TOTAL_GENERAL_PROVISIONS() {
		return R17_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR17_TOTAL_GENERAL_PROVISIONS(BigDecimal r17_TOTAL_GENERAL_PROVISIONS) {
		R17_TOTAL_GENERAL_PROVISIONS = r17_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}
	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}
	public BigDecimal getR18_STAGE1_PROVISIONS() {
		return R18_STAGE1_PROVISIONS;
	}
	public void setR18_STAGE1_PROVISIONS(BigDecimal r18_STAGE1_PROVISIONS) {
		R18_STAGE1_PROVISIONS = r18_STAGE1_PROVISIONS;
	}
	public BigDecimal getR18_QUALIFY_STAGE2_PROVISIONS() {
		return R18_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR18_QUALIFY_STAGE2_PROVISIONS(BigDecimal r18_QUALIFY_STAGE2_PROVISIONS) {
		R18_QUALIFY_STAGE2_PROVISIONS = r18_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR18_TOTAL_GENERAL_PROVISIONS() {
		return R18_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR18_TOTAL_GENERAL_PROVISIONS(BigDecimal r18_TOTAL_GENERAL_PROVISIONS) {
		R18_TOTAL_GENERAL_PROVISIONS = r18_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}
	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}
	public BigDecimal getR19_STAGE1_PROVISIONS() {
		return R19_STAGE1_PROVISIONS;
	}
	public void setR19_STAGE1_PROVISIONS(BigDecimal r19_STAGE1_PROVISIONS) {
		R19_STAGE1_PROVISIONS = r19_STAGE1_PROVISIONS;
	}
	public BigDecimal getR19_QUALIFY_STAGE2_PROVISIONS() {
		return R19_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR19_QUALIFY_STAGE2_PROVISIONS(BigDecimal r19_QUALIFY_STAGE2_PROVISIONS) {
		R19_QUALIFY_STAGE2_PROVISIONS = r19_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR19_TOTAL_GENERAL_PROVISIONS() {
		return R19_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR19_TOTAL_GENERAL_PROVISIONS(BigDecimal r19_TOTAL_GENERAL_PROVISIONS) {
		R19_TOTAL_GENERAL_PROVISIONS = r19_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}
	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}
	public BigDecimal getR20_STAGE1_PROVISIONS() {
		return R20_STAGE1_PROVISIONS;
	}
	public void setR20_STAGE1_PROVISIONS(BigDecimal r20_STAGE1_PROVISIONS) {
		R20_STAGE1_PROVISIONS = r20_STAGE1_PROVISIONS;
	}
	public BigDecimal getR20_QUALIFY_STAGE2_PROVISIONS() {
		return R20_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR20_QUALIFY_STAGE2_PROVISIONS(BigDecimal r20_QUALIFY_STAGE2_PROVISIONS) {
		R20_QUALIFY_STAGE2_PROVISIONS = r20_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR20_TOTAL_GENERAL_PROVISIONS() {
		return R20_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR20_TOTAL_GENERAL_PROVISIONS(BigDecimal r20_TOTAL_GENERAL_PROVISIONS) {
		R20_TOTAL_GENERAL_PROVISIONS = r20_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}
	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}
	public BigDecimal getR21_STAGE1_PROVISIONS() {
		return R21_STAGE1_PROVISIONS;
	}
	public void setR21_STAGE1_PROVISIONS(BigDecimal r21_STAGE1_PROVISIONS) {
		R21_STAGE1_PROVISIONS = r21_STAGE1_PROVISIONS;
	}
	public BigDecimal getR21_QUALIFY_STAGE2_PROVISIONS() {
		return R21_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR21_QUALIFY_STAGE2_PROVISIONS(BigDecimal r21_QUALIFY_STAGE2_PROVISIONS) {
		R21_QUALIFY_STAGE2_PROVISIONS = r21_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR21_TOTAL_GENERAL_PROVISIONS() {
		return R21_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR21_TOTAL_GENERAL_PROVISIONS(BigDecimal r21_TOTAL_GENERAL_PROVISIONS) {
		R21_TOTAL_GENERAL_PROVISIONS = r21_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}
	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}
	public BigDecimal getR22_STAGE1_PROVISIONS() {
		return R22_STAGE1_PROVISIONS;
	}
	public void setR22_STAGE1_PROVISIONS(BigDecimal r22_STAGE1_PROVISIONS) {
		R22_STAGE1_PROVISIONS = r22_STAGE1_PROVISIONS;
	}
	public BigDecimal getR22_QUALIFY_STAGE2_PROVISIONS() {
		return R22_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR22_QUALIFY_STAGE2_PROVISIONS(BigDecimal r22_QUALIFY_STAGE2_PROVISIONS) {
		R22_QUALIFY_STAGE2_PROVISIONS = r22_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR22_TOTAL_GENERAL_PROVISIONS() {
		return R22_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR22_TOTAL_GENERAL_PROVISIONS(BigDecimal r22_TOTAL_GENERAL_PROVISIONS) {
		R22_TOTAL_GENERAL_PROVISIONS = r22_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}
	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}
	public BigDecimal getR23_STAGE1_PROVISIONS() {
		return R23_STAGE1_PROVISIONS;
	}
	public void setR23_STAGE1_PROVISIONS(BigDecimal r23_STAGE1_PROVISIONS) {
		R23_STAGE1_PROVISIONS = r23_STAGE1_PROVISIONS;
	}
	public BigDecimal getR23_QUALIFY_STAGE2_PROVISIONS() {
		return R23_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR23_QUALIFY_STAGE2_PROVISIONS(BigDecimal r23_QUALIFY_STAGE2_PROVISIONS) {
		R23_QUALIFY_STAGE2_PROVISIONS = r23_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR23_TOTAL_GENERAL_PROVISIONS() {
		return R23_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR23_TOTAL_GENERAL_PROVISIONS(BigDecimal r23_TOTAL_GENERAL_PROVISIONS) {
		R23_TOTAL_GENERAL_PROVISIONS = r23_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}
	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}
	public BigDecimal getR24_STAGE1_PROVISIONS() {
		return R24_STAGE1_PROVISIONS;
	}
	public void setR24_STAGE1_PROVISIONS(BigDecimal r24_STAGE1_PROVISIONS) {
		R24_STAGE1_PROVISIONS = r24_STAGE1_PROVISIONS;
	}
	public BigDecimal getR24_QUALIFY_STAGE2_PROVISIONS() {
		return R24_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR24_QUALIFY_STAGE2_PROVISIONS(BigDecimal r24_QUALIFY_STAGE2_PROVISIONS) {
		R24_QUALIFY_STAGE2_PROVISIONS = r24_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR24_TOTAL_GENERAL_PROVISIONS() {
		return R24_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR24_TOTAL_GENERAL_PROVISIONS(BigDecimal r24_TOTAL_GENERAL_PROVISIONS) {
		R24_TOTAL_GENERAL_PROVISIONS = r24_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}
	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}
	public BigDecimal getR25_STAGE1_PROVISIONS() {
		return R25_STAGE1_PROVISIONS;
	}
	public void setR25_STAGE1_PROVISIONS(BigDecimal r25_STAGE1_PROVISIONS) {
		R25_STAGE1_PROVISIONS = r25_STAGE1_PROVISIONS;
	}
	public BigDecimal getR25_QUALIFY_STAGE2_PROVISIONS() {
		return R25_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR25_QUALIFY_STAGE2_PROVISIONS(BigDecimal r25_QUALIFY_STAGE2_PROVISIONS) {
		R25_QUALIFY_STAGE2_PROVISIONS = r25_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR25_TOTAL_GENERAL_PROVISIONS() {
		return R25_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR25_TOTAL_GENERAL_PROVISIONS(BigDecimal r25_TOTAL_GENERAL_PROVISIONS) {
		R25_TOTAL_GENERAL_PROVISIONS = r25_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}
	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}
	public BigDecimal getR26_STAGE1_PROVISIONS() {
		return R26_STAGE1_PROVISIONS;
	}
	public void setR26_STAGE1_PROVISIONS(BigDecimal r26_STAGE1_PROVISIONS) {
		R26_STAGE1_PROVISIONS = r26_STAGE1_PROVISIONS;
	}
	public BigDecimal getR26_QUALIFY_STAGE2_PROVISIONS() {
		return R26_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR26_QUALIFY_STAGE2_PROVISIONS(BigDecimal r26_QUALIFY_STAGE2_PROVISIONS) {
		R26_QUALIFY_STAGE2_PROVISIONS = r26_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR26_TOTAL_GENERAL_PROVISIONS() {
		return R26_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR26_TOTAL_GENERAL_PROVISIONS(BigDecimal r26_TOTAL_GENERAL_PROVISIONS) {
		R26_TOTAL_GENERAL_PROVISIONS = r26_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}
	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}
	public BigDecimal getR27_STAGE1_PROVISIONS() {
		return R27_STAGE1_PROVISIONS;
	}
	public void setR27_STAGE1_PROVISIONS(BigDecimal r27_STAGE1_PROVISIONS) {
		R27_STAGE1_PROVISIONS = r27_STAGE1_PROVISIONS;
	}
	public BigDecimal getR27_QUALIFY_STAGE2_PROVISIONS() {
		return R27_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR27_QUALIFY_STAGE2_PROVISIONS(BigDecimal r27_QUALIFY_STAGE2_PROVISIONS) {
		R27_QUALIFY_STAGE2_PROVISIONS = r27_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR27_TOTAL_GENERAL_PROVISIONS() {
		return R27_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR27_TOTAL_GENERAL_PROVISIONS(BigDecimal r27_TOTAL_GENERAL_PROVISIONS) {
		R27_TOTAL_GENERAL_PROVISIONS = r27_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}
	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}
	public BigDecimal getR28_STAGE1_PROVISIONS() {
		return R28_STAGE1_PROVISIONS;
	}
	public void setR28_STAGE1_PROVISIONS(BigDecimal r28_STAGE1_PROVISIONS) {
		R28_STAGE1_PROVISIONS = r28_STAGE1_PROVISIONS;
	}
	public BigDecimal getR28_QUALIFY_STAGE2_PROVISIONS() {
		return R28_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR28_QUALIFY_STAGE2_PROVISIONS(BigDecimal r28_QUALIFY_STAGE2_PROVISIONS) {
		R28_QUALIFY_STAGE2_PROVISIONS = r28_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR28_TOTAL_GENERAL_PROVISIONS() {
		return R28_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR28_TOTAL_GENERAL_PROVISIONS(BigDecimal r28_TOTAL_GENERAL_PROVISIONS) {
		R28_TOTAL_GENERAL_PROVISIONS = r28_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}
	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}
	public BigDecimal getR29_STAGE1_PROVISIONS() {
		return R29_STAGE1_PROVISIONS;
	}
	public void setR29_STAGE1_PROVISIONS(BigDecimal r29_STAGE1_PROVISIONS) {
		R29_STAGE1_PROVISIONS = r29_STAGE1_PROVISIONS;
	}
	public BigDecimal getR29_QUALIFY_STAGE2_PROVISIONS() {
		return R29_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR29_QUALIFY_STAGE2_PROVISIONS(BigDecimal r29_QUALIFY_STAGE2_PROVISIONS) {
		R29_QUALIFY_STAGE2_PROVISIONS = r29_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR29_TOTAL_GENERAL_PROVISIONS() {
		return R29_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR29_TOTAL_GENERAL_PROVISIONS(BigDecimal r29_TOTAL_GENERAL_PROVISIONS) {
		R29_TOTAL_GENERAL_PROVISIONS = r29_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}
	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}
	public BigDecimal getR30_STAGE1_PROVISIONS() {
		return R30_STAGE1_PROVISIONS;
	}
	public void setR30_STAGE1_PROVISIONS(BigDecimal r30_STAGE1_PROVISIONS) {
		R30_STAGE1_PROVISIONS = r30_STAGE1_PROVISIONS;
	}
	public BigDecimal getR30_QUALIFY_STAGE2_PROVISIONS() {
		return R30_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR30_QUALIFY_STAGE2_PROVISIONS(BigDecimal r30_QUALIFY_STAGE2_PROVISIONS) {
		R30_QUALIFY_STAGE2_PROVISIONS = r30_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR30_TOTAL_GENERAL_PROVISIONS() {
		return R30_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR30_TOTAL_GENERAL_PROVISIONS(BigDecimal r30_TOTAL_GENERAL_PROVISIONS) {
		R30_TOTAL_GENERAL_PROVISIONS = r30_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}
	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}
	public BigDecimal getR31_STAGE1_PROVISIONS() {
		return R31_STAGE1_PROVISIONS;
	}
	public void setR31_STAGE1_PROVISIONS(BigDecimal r31_STAGE1_PROVISIONS) {
		R31_STAGE1_PROVISIONS = r31_STAGE1_PROVISIONS;
	}
	public BigDecimal getR31_QUALIFY_STAGE2_PROVISIONS() {
		return R31_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR31_QUALIFY_STAGE2_PROVISIONS(BigDecimal r31_QUALIFY_STAGE2_PROVISIONS) {
		R31_QUALIFY_STAGE2_PROVISIONS = r31_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR31_TOTAL_GENERAL_PROVISIONS() {
		return R31_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR31_TOTAL_GENERAL_PROVISIONS(BigDecimal r31_TOTAL_GENERAL_PROVISIONS) {
		R31_TOTAL_GENERAL_PROVISIONS = r31_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}
	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}
	public BigDecimal getR32_STAGE1_PROVISIONS() {
		return R32_STAGE1_PROVISIONS;
	}
	public void setR32_STAGE1_PROVISIONS(BigDecimal r32_STAGE1_PROVISIONS) {
		R32_STAGE1_PROVISIONS = r32_STAGE1_PROVISIONS;
	}
	public BigDecimal getR32_QUALIFY_STAGE2_PROVISIONS() {
		return R32_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR32_QUALIFY_STAGE2_PROVISIONS(BigDecimal r32_QUALIFY_STAGE2_PROVISIONS) {
		R32_QUALIFY_STAGE2_PROVISIONS = r32_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR32_TOTAL_GENERAL_PROVISIONS() {
		return R32_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR32_TOTAL_GENERAL_PROVISIONS(BigDecimal r32_TOTAL_GENERAL_PROVISIONS) {
		R32_TOTAL_GENERAL_PROVISIONS = r32_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}
	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}
	public BigDecimal getR33_STAGE1_PROVISIONS() {
		return R33_STAGE1_PROVISIONS;
	}
	public void setR33_STAGE1_PROVISIONS(BigDecimal r33_STAGE1_PROVISIONS) {
		R33_STAGE1_PROVISIONS = r33_STAGE1_PROVISIONS;
	}
	public BigDecimal getR33_QUALIFY_STAGE2_PROVISIONS() {
		return R33_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR33_QUALIFY_STAGE2_PROVISIONS(BigDecimal r33_QUALIFY_STAGE2_PROVISIONS) {
		R33_QUALIFY_STAGE2_PROVISIONS = r33_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR33_TOTAL_GENERAL_PROVISIONS() {
		return R33_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR33_TOTAL_GENERAL_PROVISIONS(BigDecimal r33_TOTAL_GENERAL_PROVISIONS) {
		R33_TOTAL_GENERAL_PROVISIONS = r33_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}
	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}
	public BigDecimal getR34_STAGE1_PROVISIONS() {
		return R34_STAGE1_PROVISIONS;
	}
	public void setR34_STAGE1_PROVISIONS(BigDecimal r34_STAGE1_PROVISIONS) {
		R34_STAGE1_PROVISIONS = r34_STAGE1_PROVISIONS;
	}
	public BigDecimal getR34_QUALIFY_STAGE2_PROVISIONS() {
		return R34_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR34_QUALIFY_STAGE2_PROVISIONS(BigDecimal r34_QUALIFY_STAGE2_PROVISIONS) {
		R34_QUALIFY_STAGE2_PROVISIONS = r34_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR34_TOTAL_GENERAL_PROVISIONS() {
		return R34_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR34_TOTAL_GENERAL_PROVISIONS(BigDecimal r34_TOTAL_GENERAL_PROVISIONS) {
		R34_TOTAL_GENERAL_PROVISIONS = r34_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}
	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}
	public BigDecimal getR35_STAGE1_PROVISIONS() {
		return R35_STAGE1_PROVISIONS;
	}
	public void setR35_STAGE1_PROVISIONS(BigDecimal r35_STAGE1_PROVISIONS) {
		R35_STAGE1_PROVISIONS = r35_STAGE1_PROVISIONS;
	}
	public BigDecimal getR35_QUALIFY_STAGE2_PROVISIONS() {
		return R35_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR35_QUALIFY_STAGE2_PROVISIONS(BigDecimal r35_QUALIFY_STAGE2_PROVISIONS) {
		R35_QUALIFY_STAGE2_PROVISIONS = r35_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR35_TOTAL_GENERAL_PROVISIONS() {
		return R35_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR35_TOTAL_GENERAL_PROVISIONS(BigDecimal r35_TOTAL_GENERAL_PROVISIONS) {
		R35_TOTAL_GENERAL_PROVISIONS = r35_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_STAGE1_PROVISIONS() {
		return R36_STAGE1_PROVISIONS;
	}
	public void setR36_STAGE1_PROVISIONS(BigDecimal r36_STAGE1_PROVISIONS) {
		R36_STAGE1_PROVISIONS = r36_STAGE1_PROVISIONS;
	}
	public BigDecimal getR36_QUALIFY_STAGE2_PROVISIONS() {
		return R36_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR36_QUALIFY_STAGE2_PROVISIONS(BigDecimal r36_QUALIFY_STAGE2_PROVISIONS) {
		R36_QUALIFY_STAGE2_PROVISIONS = r36_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR36_TOTAL_GENERAL_PROVISIONS() {
		return R36_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR36_TOTAL_GENERAL_PROVISIONS(BigDecimal r36_TOTAL_GENERAL_PROVISIONS) {
		R36_TOTAL_GENERAL_PROVISIONS = r36_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}
	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}
	public BigDecimal getR37_STAGE1_PROVISIONS() {
		return R37_STAGE1_PROVISIONS;
	}
	public void setR37_STAGE1_PROVISIONS(BigDecimal r37_STAGE1_PROVISIONS) {
		R37_STAGE1_PROVISIONS = r37_STAGE1_PROVISIONS;
	}
	public BigDecimal getR37_QUALIFY_STAGE2_PROVISIONS() {
		return R37_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR37_QUALIFY_STAGE2_PROVISIONS(BigDecimal r37_QUALIFY_STAGE2_PROVISIONS) {
		R37_QUALIFY_STAGE2_PROVISIONS = r37_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR37_TOTAL_GENERAL_PROVISIONS() {
		return R37_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR37_TOTAL_GENERAL_PROVISIONS(BigDecimal r37_TOTAL_GENERAL_PROVISIONS) {
		R37_TOTAL_GENERAL_PROVISIONS = r37_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}
	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}
	public BigDecimal getR38_STAGE1_PROVISIONS() {
		return R38_STAGE1_PROVISIONS;
	}
	public void setR38_STAGE1_PROVISIONS(BigDecimal r38_STAGE1_PROVISIONS) {
		R38_STAGE1_PROVISIONS = r38_STAGE1_PROVISIONS;
	}
	public BigDecimal getR38_QUALIFY_STAGE2_PROVISIONS() {
		return R38_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR38_QUALIFY_STAGE2_PROVISIONS(BigDecimal r38_QUALIFY_STAGE2_PROVISIONS) {
		R38_QUALIFY_STAGE2_PROVISIONS = r38_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR38_TOTAL_GENERAL_PROVISIONS() {
		return R38_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR38_TOTAL_GENERAL_PROVISIONS(BigDecimal r38_TOTAL_GENERAL_PROVISIONS) {
		R38_TOTAL_GENERAL_PROVISIONS = r38_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}
	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}
	public BigDecimal getR39_STAGE1_PROVISIONS() {
		return R39_STAGE1_PROVISIONS;
	}
	public void setR39_STAGE1_PROVISIONS(BigDecimal r39_STAGE1_PROVISIONS) {
		R39_STAGE1_PROVISIONS = r39_STAGE1_PROVISIONS;
	}
	public BigDecimal getR39_QUALIFY_STAGE2_PROVISIONS() {
		return R39_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR39_QUALIFY_STAGE2_PROVISIONS(BigDecimal r39_QUALIFY_STAGE2_PROVISIONS) {
		R39_QUALIFY_STAGE2_PROVISIONS = r39_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR39_TOTAL_GENERAL_PROVISIONS() {
		return R39_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR39_TOTAL_GENERAL_PROVISIONS(BigDecimal r39_TOTAL_GENERAL_PROVISIONS) {
		R39_TOTAL_GENERAL_PROVISIONS = r39_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}
	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}
	public BigDecimal getR40_STAGE1_PROVISIONS() {
		return R40_STAGE1_PROVISIONS;
	}
	public void setR40_STAGE1_PROVISIONS(BigDecimal r40_STAGE1_PROVISIONS) {
		R40_STAGE1_PROVISIONS = r40_STAGE1_PROVISIONS;
	}
	public BigDecimal getR40_QUALIFY_STAGE2_PROVISIONS() {
		return R40_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR40_QUALIFY_STAGE2_PROVISIONS(BigDecimal r40_QUALIFY_STAGE2_PROVISIONS) {
		R40_QUALIFY_STAGE2_PROVISIONS = r40_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR40_TOTAL_GENERAL_PROVISIONS() {
		return R40_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR40_TOTAL_GENERAL_PROVISIONS(BigDecimal r40_TOTAL_GENERAL_PROVISIONS) {
		R40_TOTAL_GENERAL_PROVISIONS = r40_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}
	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}
	public BigDecimal getR41_STAGE1_PROVISIONS() {
		return R41_STAGE1_PROVISIONS;
	}
	public void setR41_STAGE1_PROVISIONS(BigDecimal r41_STAGE1_PROVISIONS) {
		R41_STAGE1_PROVISIONS = r41_STAGE1_PROVISIONS;
	}
	public BigDecimal getR41_QUALIFY_STAGE2_PROVISIONS() {
		return R41_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR41_QUALIFY_STAGE2_PROVISIONS(BigDecimal r41_QUALIFY_STAGE2_PROVISIONS) {
		R41_QUALIFY_STAGE2_PROVISIONS = r41_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR41_TOTAL_GENERAL_PROVISIONS() {
		return R41_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR41_TOTAL_GENERAL_PROVISIONS(BigDecimal r41_TOTAL_GENERAL_PROVISIONS) {
		R41_TOTAL_GENERAL_PROVISIONS = r41_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}
	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}
	public BigDecimal getR42_STAGE1_PROVISIONS() {
		return R42_STAGE1_PROVISIONS;
	}
	public void setR42_STAGE1_PROVISIONS(BigDecimal r42_STAGE1_PROVISIONS) {
		R42_STAGE1_PROVISIONS = r42_STAGE1_PROVISIONS;
	}
	public BigDecimal getR42_QUALIFY_STAGE2_PROVISIONS() {
		return R42_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR42_QUALIFY_STAGE2_PROVISIONS(BigDecimal r42_QUALIFY_STAGE2_PROVISIONS) {
		R42_QUALIFY_STAGE2_PROVISIONS = r42_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR42_TOTAL_GENERAL_PROVISIONS() {
		return R42_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR42_TOTAL_GENERAL_PROVISIONS(BigDecimal r42_TOTAL_GENERAL_PROVISIONS) {
		R42_TOTAL_GENERAL_PROVISIONS = r42_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}
	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}
	public BigDecimal getR43_STAGE1_PROVISIONS() {
		return R43_STAGE1_PROVISIONS;
	}
	public void setR43_STAGE1_PROVISIONS(BigDecimal r43_STAGE1_PROVISIONS) {
		R43_STAGE1_PROVISIONS = r43_STAGE1_PROVISIONS;
	}
	public BigDecimal getR43_QUALIFY_STAGE2_PROVISIONS() {
		return R43_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR43_QUALIFY_STAGE2_PROVISIONS(BigDecimal r43_QUALIFY_STAGE2_PROVISIONS) {
		R43_QUALIFY_STAGE2_PROVISIONS = r43_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR43_TOTAL_GENERAL_PROVISIONS() {
		return R43_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR43_TOTAL_GENERAL_PROVISIONS(BigDecimal r43_TOTAL_GENERAL_PROVISIONS) {
		R43_TOTAL_GENERAL_PROVISIONS = r43_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR44_PRODUCT() {
		return R44_PRODUCT;
	}
	public void setR44_PRODUCT(String r44_PRODUCT) {
		R44_PRODUCT = r44_PRODUCT;
	}
	public BigDecimal getR44_STAGE1_PROVISIONS() {
		return R44_STAGE1_PROVISIONS;
	}
	public void setR44_STAGE1_PROVISIONS(BigDecimal r44_STAGE1_PROVISIONS) {
		R44_STAGE1_PROVISIONS = r44_STAGE1_PROVISIONS;
	}
	public BigDecimal getR44_QUALIFY_STAGE2_PROVISIONS() {
		return R44_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR44_QUALIFY_STAGE2_PROVISIONS(BigDecimal r44_QUALIFY_STAGE2_PROVISIONS) {
		R44_QUALIFY_STAGE2_PROVISIONS = r44_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR44_TOTAL_GENERAL_PROVISIONS() {
		return R44_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR44_TOTAL_GENERAL_PROVISIONS(BigDecimal r44_TOTAL_GENERAL_PROVISIONS) {
		R44_TOTAL_GENERAL_PROVISIONS = r44_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR45_PRODUCT() {
		return R45_PRODUCT;
	}
	public void setR45_PRODUCT(String r45_PRODUCT) {
		R45_PRODUCT = r45_PRODUCT;
	}
	public BigDecimal getR45_STAGE1_PROVISIONS() {
		return R45_STAGE1_PROVISIONS;
	}
	public void setR45_STAGE1_PROVISIONS(BigDecimal r45_STAGE1_PROVISIONS) {
		R45_STAGE1_PROVISIONS = r45_STAGE1_PROVISIONS;
	}
	public BigDecimal getR45_QUALIFY_STAGE2_PROVISIONS() {
		return R45_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR45_QUALIFY_STAGE2_PROVISIONS(BigDecimal r45_QUALIFY_STAGE2_PROVISIONS) {
		R45_QUALIFY_STAGE2_PROVISIONS = r45_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR45_TOTAL_GENERAL_PROVISIONS() {
		return R45_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR45_TOTAL_GENERAL_PROVISIONS(BigDecimal r45_TOTAL_GENERAL_PROVISIONS) {
		R45_TOTAL_GENERAL_PROVISIONS = r45_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR46_PRODUCT() {
		return R46_PRODUCT;
	}
	public void setR46_PRODUCT(String r46_PRODUCT) {
		R46_PRODUCT = r46_PRODUCT;
	}
	public BigDecimal getR46_STAGE1_PROVISIONS() {
		return R46_STAGE1_PROVISIONS;
	}
	public void setR46_STAGE1_PROVISIONS(BigDecimal r46_STAGE1_PROVISIONS) {
		R46_STAGE1_PROVISIONS = r46_STAGE1_PROVISIONS;
	}
	public BigDecimal getR46_QUALIFY_STAGE2_PROVISIONS() {
		return R46_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR46_QUALIFY_STAGE2_PROVISIONS(BigDecimal r46_QUALIFY_STAGE2_PROVISIONS) {
		R46_QUALIFY_STAGE2_PROVISIONS = r46_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR46_TOTAL_GENERAL_PROVISIONS() {
		return R46_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR46_TOTAL_GENERAL_PROVISIONS(BigDecimal r46_TOTAL_GENERAL_PROVISIONS) {
		R46_TOTAL_GENERAL_PROVISIONS = r46_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}
	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}
	public BigDecimal getR47_STAGE1_PROVISIONS() {
		return R47_STAGE1_PROVISIONS;
	}
	public void setR47_STAGE1_PROVISIONS(BigDecimal r47_STAGE1_PROVISIONS) {
		R47_STAGE1_PROVISIONS = r47_STAGE1_PROVISIONS;
	}
	public BigDecimal getR47_QUALIFY_STAGE2_PROVISIONS() {
		return R47_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR47_QUALIFY_STAGE2_PROVISIONS(BigDecimal r47_QUALIFY_STAGE2_PROVISIONS) {
		R47_QUALIFY_STAGE2_PROVISIONS = r47_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR47_TOTAL_GENERAL_PROVISIONS() {
		return R47_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR47_TOTAL_GENERAL_PROVISIONS(BigDecimal r47_TOTAL_GENERAL_PROVISIONS) {
		R47_TOTAL_GENERAL_PROVISIONS = r47_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR48_PRODUCT() {
		return R48_PRODUCT;
	}
	public void setR48_PRODUCT(String r48_PRODUCT) {
		R48_PRODUCT = r48_PRODUCT;
	}
	public BigDecimal getR48_STAGE1_PROVISIONS() {
		return R48_STAGE1_PROVISIONS;
	}
	public void setR48_STAGE1_PROVISIONS(BigDecimal r48_STAGE1_PROVISIONS) {
		R48_STAGE1_PROVISIONS = r48_STAGE1_PROVISIONS;
	}
	public BigDecimal getR48_QUALIFY_STAGE2_PROVISIONS() {
		return R48_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR48_QUALIFY_STAGE2_PROVISIONS(BigDecimal r48_QUALIFY_STAGE2_PROVISIONS) {
		R48_QUALIFY_STAGE2_PROVISIONS = r48_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR48_TOTAL_GENERAL_PROVISIONS() {
		return R48_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR48_TOTAL_GENERAL_PROVISIONS(BigDecimal r48_TOTAL_GENERAL_PROVISIONS) {
		R48_TOTAL_GENERAL_PROVISIONS = r48_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR49_PRODUCT() {
		return R49_PRODUCT;
	}
	public void setR49_PRODUCT(String r49_PRODUCT) {
		R49_PRODUCT = r49_PRODUCT;
	}
	public BigDecimal getR49_STAGE1_PROVISIONS() {
		return R49_STAGE1_PROVISIONS;
	}
	public void setR49_STAGE1_PROVISIONS(BigDecimal r49_STAGE1_PROVISIONS) {
		R49_STAGE1_PROVISIONS = r49_STAGE1_PROVISIONS;
	}
	public BigDecimal getR49_QUALIFY_STAGE2_PROVISIONS() {
		return R49_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR49_QUALIFY_STAGE2_PROVISIONS(BigDecimal r49_QUALIFY_STAGE2_PROVISIONS) {
		R49_QUALIFY_STAGE2_PROVISIONS = r49_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR49_TOTAL_GENERAL_PROVISIONS() {
		return R49_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR49_TOTAL_GENERAL_PROVISIONS(BigDecimal r49_TOTAL_GENERAL_PROVISIONS) {
		R49_TOTAL_GENERAL_PROVISIONS = r49_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR50_PRODUCT() {
		return R50_PRODUCT;
	}
	public void setR50_PRODUCT(String r50_PRODUCT) {
		R50_PRODUCT = r50_PRODUCT;
	}
	public BigDecimal getR50_STAGE1_PROVISIONS() {
		return R50_STAGE1_PROVISIONS;
	}
	public void setR50_STAGE1_PROVISIONS(BigDecimal r50_STAGE1_PROVISIONS) {
		R50_STAGE1_PROVISIONS = r50_STAGE1_PROVISIONS;
	}
	public BigDecimal getR50_QUALIFY_STAGE2_PROVISIONS() {
		return R50_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR50_QUALIFY_STAGE2_PROVISIONS(BigDecimal r50_QUALIFY_STAGE2_PROVISIONS) {
		R50_QUALIFY_STAGE2_PROVISIONS = r50_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR50_TOTAL_GENERAL_PROVISIONS() {
		return R50_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR50_TOTAL_GENERAL_PROVISIONS(BigDecimal r50_TOTAL_GENERAL_PROVISIONS) {
		R50_TOTAL_GENERAL_PROVISIONS = r50_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR51_PRODUCT() {
		return R51_PRODUCT;
	}
	public void setR51_PRODUCT(String r51_PRODUCT) {
		R51_PRODUCT = r51_PRODUCT;
	}
	public BigDecimal getR51_STAGE1_PROVISIONS() {
		return R51_STAGE1_PROVISIONS;
	}
	public void setR51_STAGE1_PROVISIONS(BigDecimal r51_STAGE1_PROVISIONS) {
		R51_STAGE1_PROVISIONS = r51_STAGE1_PROVISIONS;
	}
	public BigDecimal getR51_QUALIFY_STAGE2_PROVISIONS() {
		return R51_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR51_QUALIFY_STAGE2_PROVISIONS(BigDecimal r51_QUALIFY_STAGE2_PROVISIONS) {
		R51_QUALIFY_STAGE2_PROVISIONS = r51_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR51_TOTAL_GENERAL_PROVISIONS() {
		return R51_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR51_TOTAL_GENERAL_PROVISIONS(BigDecimal r51_TOTAL_GENERAL_PROVISIONS) {
		R51_TOTAL_GENERAL_PROVISIONS = r51_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR52_PRODUCT() {
		return R52_PRODUCT;
	}
	public void setR52_PRODUCT(String r52_PRODUCT) {
		R52_PRODUCT = r52_PRODUCT;
	}
	public BigDecimal getR52_STAGE1_PROVISIONS() {
		return R52_STAGE1_PROVISIONS;
	}
	public void setR52_STAGE1_PROVISIONS(BigDecimal r52_STAGE1_PROVISIONS) {
		R52_STAGE1_PROVISIONS = r52_STAGE1_PROVISIONS;
	}
	public BigDecimal getR52_QUALIFY_STAGE2_PROVISIONS() {
		return R52_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR52_QUALIFY_STAGE2_PROVISIONS(BigDecimal r52_QUALIFY_STAGE2_PROVISIONS) {
		R52_QUALIFY_STAGE2_PROVISIONS = r52_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR52_TOTAL_GENERAL_PROVISIONS() {
		return R52_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR52_TOTAL_GENERAL_PROVISIONS(BigDecimal r52_TOTAL_GENERAL_PROVISIONS) {
		R52_TOTAL_GENERAL_PROVISIONS = r52_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR53_PRODUCT() {
		return R53_PRODUCT;
	}
	public void setR53_PRODUCT(String r53_PRODUCT) {
		R53_PRODUCT = r53_PRODUCT;
	}
	public BigDecimal getR53_STAGE1_PROVISIONS() {
		return R53_STAGE1_PROVISIONS;
	}
	public void setR53_STAGE1_PROVISIONS(BigDecimal r53_STAGE1_PROVISIONS) {
		R53_STAGE1_PROVISIONS = r53_STAGE1_PROVISIONS;
	}
	public BigDecimal getR53_QUALIFY_STAGE2_PROVISIONS() {
		return R53_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR53_QUALIFY_STAGE2_PROVISIONS(BigDecimal r53_QUALIFY_STAGE2_PROVISIONS) {
		R53_QUALIFY_STAGE2_PROVISIONS = r53_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR53_TOTAL_GENERAL_PROVISIONS() {
		return R53_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR53_TOTAL_GENERAL_PROVISIONS(BigDecimal r53_TOTAL_GENERAL_PROVISIONS) {
		R53_TOTAL_GENERAL_PROVISIONS = r53_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR54_PRODUCT() {
		return R54_PRODUCT;
	}
	public void setR54_PRODUCT(String r54_PRODUCT) {
		R54_PRODUCT = r54_PRODUCT;
	}
	public BigDecimal getR54_STAGE1_PROVISIONS() {
		return R54_STAGE1_PROVISIONS;
	}
	public void setR54_STAGE1_PROVISIONS(BigDecimal r54_STAGE1_PROVISIONS) {
		R54_STAGE1_PROVISIONS = r54_STAGE1_PROVISIONS;
	}
	public BigDecimal getR54_QUALIFY_STAGE2_PROVISIONS() {
		return R54_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR54_QUALIFY_STAGE2_PROVISIONS(BigDecimal r54_QUALIFY_STAGE2_PROVISIONS) {
		R54_QUALIFY_STAGE2_PROVISIONS = r54_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR54_TOTAL_GENERAL_PROVISIONS() {
		return R54_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR54_TOTAL_GENERAL_PROVISIONS(BigDecimal r54_TOTAL_GENERAL_PROVISIONS) {
		R54_TOTAL_GENERAL_PROVISIONS = r54_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR55_PRODUCT() {
		return R55_PRODUCT;
	}
	public void setR55_PRODUCT(String r55_PRODUCT) {
		R55_PRODUCT = r55_PRODUCT;
	}
	public BigDecimal getR55_STAGE1_PROVISIONS() {
		return R55_STAGE1_PROVISIONS;
	}
	public void setR55_STAGE1_PROVISIONS(BigDecimal r55_STAGE1_PROVISIONS) {
		R55_STAGE1_PROVISIONS = r55_STAGE1_PROVISIONS;
	}
	public BigDecimal getR55_QUALIFY_STAGE2_PROVISIONS() {
		return R55_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR55_QUALIFY_STAGE2_PROVISIONS(BigDecimal r55_QUALIFY_STAGE2_PROVISIONS) {
		R55_QUALIFY_STAGE2_PROVISIONS = r55_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR55_TOTAL_GENERAL_PROVISIONS() {
		return R55_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR55_TOTAL_GENERAL_PROVISIONS(BigDecimal r55_TOTAL_GENERAL_PROVISIONS) {
		R55_TOTAL_GENERAL_PROVISIONS = r55_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR56_PRODUCT() {
		return R56_PRODUCT;
	}
	public void setR56_PRODUCT(String r56_PRODUCT) {
		R56_PRODUCT = r56_PRODUCT;
	}
	public BigDecimal getR56_STAGE1_PROVISIONS() {
		return R56_STAGE1_PROVISIONS;
	}
	public void setR56_STAGE1_PROVISIONS(BigDecimal r56_STAGE1_PROVISIONS) {
		R56_STAGE1_PROVISIONS = r56_STAGE1_PROVISIONS;
	}
	public BigDecimal getR56_QUALIFY_STAGE2_PROVISIONS() {
		return R56_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR56_QUALIFY_STAGE2_PROVISIONS(BigDecimal r56_QUALIFY_STAGE2_PROVISIONS) {
		R56_QUALIFY_STAGE2_PROVISIONS = r56_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR56_TOTAL_GENERAL_PROVISIONS() {
		return R56_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR56_TOTAL_GENERAL_PROVISIONS(BigDecimal r56_TOTAL_GENERAL_PROVISIONS) {
		R56_TOTAL_GENERAL_PROVISIONS = r56_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR57_PRODUCT() {
		return R57_PRODUCT;
	}
	public void setR57_PRODUCT(String r57_PRODUCT) {
		R57_PRODUCT = r57_PRODUCT;
	}
	public BigDecimal getR57_STAGE1_PROVISIONS() {
		return R57_STAGE1_PROVISIONS;
	}
	public void setR57_STAGE1_PROVISIONS(BigDecimal r57_STAGE1_PROVISIONS) {
		R57_STAGE1_PROVISIONS = r57_STAGE1_PROVISIONS;
	}
	public BigDecimal getR57_QUALIFY_STAGE2_PROVISIONS() {
		return R57_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR57_QUALIFY_STAGE2_PROVISIONS(BigDecimal r57_QUALIFY_STAGE2_PROVISIONS) {
		R57_QUALIFY_STAGE2_PROVISIONS = r57_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR57_TOTAL_GENERAL_PROVISIONS() {
		return R57_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR57_TOTAL_GENERAL_PROVISIONS(BigDecimal r57_TOTAL_GENERAL_PROVISIONS) {
		R57_TOTAL_GENERAL_PROVISIONS = r57_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR58_PRODUCT() {
		return R58_PRODUCT;
	}
	public void setR58_PRODUCT(String r58_PRODUCT) {
		R58_PRODUCT = r58_PRODUCT;
	}
	public BigDecimal getR58_STAGE1_PROVISIONS() {
		return R58_STAGE1_PROVISIONS;
	}
	public void setR58_STAGE1_PROVISIONS(BigDecimal r58_STAGE1_PROVISIONS) {
		R58_STAGE1_PROVISIONS = r58_STAGE1_PROVISIONS;
	}
	public BigDecimal getR58_QUALIFY_STAGE2_PROVISIONS() {
		return R58_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR58_QUALIFY_STAGE2_PROVISIONS(BigDecimal r58_QUALIFY_STAGE2_PROVISIONS) {
		R58_QUALIFY_STAGE2_PROVISIONS = r58_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR58_TOTAL_GENERAL_PROVISIONS() {
		return R58_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR58_TOTAL_GENERAL_PROVISIONS(BigDecimal r58_TOTAL_GENERAL_PROVISIONS) {
		R58_TOTAL_GENERAL_PROVISIONS = r58_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR59_PRODUCT() {
		return R59_PRODUCT;
	}
	public void setR59_PRODUCT(String r59_PRODUCT) {
		R59_PRODUCT = r59_PRODUCT;
	}
	public BigDecimal getR59_STAGE1_PROVISIONS() {
		return R59_STAGE1_PROVISIONS;
	}
	public void setR59_STAGE1_PROVISIONS(BigDecimal r59_STAGE1_PROVISIONS) {
		R59_STAGE1_PROVISIONS = r59_STAGE1_PROVISIONS;
	}
	public BigDecimal getR59_QUALIFY_STAGE2_PROVISIONS() {
		return R59_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR59_QUALIFY_STAGE2_PROVISIONS(BigDecimal r59_QUALIFY_STAGE2_PROVISIONS) {
		R59_QUALIFY_STAGE2_PROVISIONS = r59_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR59_TOTAL_GENERAL_PROVISIONS() {
		return R59_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR59_TOTAL_GENERAL_PROVISIONS(BigDecimal r59_TOTAL_GENERAL_PROVISIONS) {
		R59_TOTAL_GENERAL_PROVISIONS = r59_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR60_PRODUCT() {
		return R60_PRODUCT;
	}
	public void setR60_PRODUCT(String r60_PRODUCT) {
		R60_PRODUCT = r60_PRODUCT;
	}
	public BigDecimal getR60_STAGE1_PROVISIONS() {
		return R60_STAGE1_PROVISIONS;
	}
	public void setR60_STAGE1_PROVISIONS(BigDecimal r60_STAGE1_PROVISIONS) {
		R60_STAGE1_PROVISIONS = r60_STAGE1_PROVISIONS;
	}
	public BigDecimal getR60_QUALIFY_STAGE2_PROVISIONS() {
		return R60_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR60_QUALIFY_STAGE2_PROVISIONS(BigDecimal r60_QUALIFY_STAGE2_PROVISIONS) {
		R60_QUALIFY_STAGE2_PROVISIONS = r60_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR60_TOTAL_GENERAL_PROVISIONS() {
		return R60_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR60_TOTAL_GENERAL_PROVISIONS(BigDecimal r60_TOTAL_GENERAL_PROVISIONS) {
		R60_TOTAL_GENERAL_PROVISIONS = r60_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR61_PRODUCT() {
		return R61_PRODUCT;
	}
	public void setR61_PRODUCT(String r61_PRODUCT) {
		R61_PRODUCT = r61_PRODUCT;
	}
	public BigDecimal getR61_STAGE1_PROVISIONS() {
		return R61_STAGE1_PROVISIONS;
	}
	public void setR61_STAGE1_PROVISIONS(BigDecimal r61_STAGE1_PROVISIONS) {
		R61_STAGE1_PROVISIONS = r61_STAGE1_PROVISIONS;
	}
	public BigDecimal getR61_QUALIFY_STAGE2_PROVISIONS() {
		return R61_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR61_QUALIFY_STAGE2_PROVISIONS(BigDecimal r61_QUALIFY_STAGE2_PROVISIONS) {
		R61_QUALIFY_STAGE2_PROVISIONS = r61_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR61_TOTAL_GENERAL_PROVISIONS() {
		return R61_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR61_TOTAL_GENERAL_PROVISIONS(BigDecimal r61_TOTAL_GENERAL_PROVISIONS) {
		R61_TOTAL_GENERAL_PROVISIONS = r61_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR62_PRODUCT() {
		return R62_PRODUCT;
	}
	public void setR62_PRODUCT(String r62_PRODUCT) {
		R62_PRODUCT = r62_PRODUCT;
	}
	public BigDecimal getR62_STAGE1_PROVISIONS() {
		return R62_STAGE1_PROVISIONS;
	}
	public void setR62_STAGE1_PROVISIONS(BigDecimal r62_STAGE1_PROVISIONS) {
		R62_STAGE1_PROVISIONS = r62_STAGE1_PROVISIONS;
	}
	public BigDecimal getR62_QUALIFY_STAGE2_PROVISIONS() {
		return R62_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR62_QUALIFY_STAGE2_PROVISIONS(BigDecimal r62_QUALIFY_STAGE2_PROVISIONS) {
		R62_QUALIFY_STAGE2_PROVISIONS = r62_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR62_TOTAL_GENERAL_PROVISIONS() {
		return R62_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR62_TOTAL_GENERAL_PROVISIONS(BigDecimal r62_TOTAL_GENERAL_PROVISIONS) {
		R62_TOTAL_GENERAL_PROVISIONS = r62_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR63_PRODUCT() {
		return R63_PRODUCT;
	}
	public void setR63_PRODUCT(String r63_PRODUCT) {
		R63_PRODUCT = r63_PRODUCT;
	}
	public BigDecimal getR63_STAGE1_PROVISIONS() {
		return R63_STAGE1_PROVISIONS;
	}
	public void setR63_STAGE1_PROVISIONS(BigDecimal r63_STAGE1_PROVISIONS) {
		R63_STAGE1_PROVISIONS = r63_STAGE1_PROVISIONS;
	}
	public BigDecimal getR63_QUALIFY_STAGE2_PROVISIONS() {
		return R63_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR63_QUALIFY_STAGE2_PROVISIONS(BigDecimal r63_QUALIFY_STAGE2_PROVISIONS) {
		R63_QUALIFY_STAGE2_PROVISIONS = r63_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR63_TOTAL_GENERAL_PROVISIONS() {
		return R63_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR63_TOTAL_GENERAL_PROVISIONS(BigDecimal r63_TOTAL_GENERAL_PROVISIONS) {
		R63_TOTAL_GENERAL_PROVISIONS = r63_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR64_PRODUCT() {
		return R64_PRODUCT;
	}
	public void setR64_PRODUCT(String r64_PRODUCT) {
		R64_PRODUCT = r64_PRODUCT;
	}
	public BigDecimal getR64_STAGE1_PROVISIONS() {
		return R64_STAGE1_PROVISIONS;
	}
	public void setR64_STAGE1_PROVISIONS(BigDecimal r64_STAGE1_PROVISIONS) {
		R64_STAGE1_PROVISIONS = r64_STAGE1_PROVISIONS;
	}
	public BigDecimal getR64_QUALIFY_STAGE2_PROVISIONS() {
		return R64_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR64_QUALIFY_STAGE2_PROVISIONS(BigDecimal r64_QUALIFY_STAGE2_PROVISIONS) {
		R64_QUALIFY_STAGE2_PROVISIONS = r64_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR64_TOTAL_GENERAL_PROVISIONS() {
		return R64_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR64_TOTAL_GENERAL_PROVISIONS(BigDecimal r64_TOTAL_GENERAL_PROVISIONS) {
		R64_TOTAL_GENERAL_PROVISIONS = r64_TOTAL_GENERAL_PROVISIONS;
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


public class M_GP_Archival_Detail_RowMapper 
        implements RowMapper<M_GP_Archival_Detail_Entity> {

    @Override
    public M_GP_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_GP_Archival_Detail_Entity obj = new M_GP_Archival_Detail_Entity();

// =========================
// R11
// =========================
obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
obj.setR11_STAGE1_PROVISIONS(rs.getBigDecimal("R11_STAGE1_PROVISIONS"));
obj.setR11_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R11_QUALIFY_STAGE2_PROVISIONS"));
obj.setR11_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R11_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R12
// =========================
obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
obj.setR12_STAGE1_PROVISIONS(rs.getBigDecimal("R12_STAGE1_PROVISIONS"));
obj.setR12_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R12_QUALIFY_STAGE2_PROVISIONS"));
obj.setR12_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R12_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R13
// =========================
obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
obj.setR13_STAGE1_PROVISIONS(rs.getBigDecimal("R13_STAGE1_PROVISIONS"));
obj.setR13_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R13_QUALIFY_STAGE2_PROVISIONS"));
obj.setR13_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R13_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R14
// =========================
obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
obj.setR14_STAGE1_PROVISIONS(rs.getBigDecimal("R14_STAGE1_PROVISIONS"));
obj.setR14_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R14_QUALIFY_STAGE2_PROVISIONS"));
obj.setR14_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R14_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R15
// =========================
obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
obj.setR15_STAGE1_PROVISIONS(rs.getBigDecimal("R15_STAGE1_PROVISIONS"));
obj.setR15_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R15_QUALIFY_STAGE2_PROVISIONS"));
obj.setR15_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R15_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R16
// =========================
obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
obj.setR16_STAGE1_PROVISIONS(rs.getBigDecimal("R16_STAGE1_PROVISIONS"));
obj.setR16_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R16_QUALIFY_STAGE2_PROVISIONS"));
obj.setR16_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R16_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R17
// =========================
obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
obj.setR17_STAGE1_PROVISIONS(rs.getBigDecimal("R17_STAGE1_PROVISIONS"));
obj.setR17_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R17_QUALIFY_STAGE2_PROVISIONS"));
obj.setR17_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R17_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R18
// =========================
obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
obj.setR18_STAGE1_PROVISIONS(rs.getBigDecimal("R18_STAGE1_PROVISIONS"));
obj.setR18_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R18_QUALIFY_STAGE2_PROVISIONS"));
obj.setR18_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R18_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R19
// =========================
obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
obj.setR19_STAGE1_PROVISIONS(rs.getBigDecimal("R19_STAGE1_PROVISIONS"));
obj.setR19_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R19_QUALIFY_STAGE2_PROVISIONS"));
obj.setR19_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R19_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R20
// =========================
obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
obj.setR20_STAGE1_PROVISIONS(rs.getBigDecimal("R20_STAGE1_PROVISIONS"));
obj.setR20_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R20_QUALIFY_STAGE2_PROVISIONS"));
obj.setR20_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R20_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R21
// =========================
obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
obj.setR21_STAGE1_PROVISIONS(rs.getBigDecimal("R21_STAGE1_PROVISIONS"));
obj.setR21_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R21_QUALIFY_STAGE2_PROVISIONS"));
obj.setR21_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R21_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R22
// =========================
obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
obj.setR22_STAGE1_PROVISIONS(rs.getBigDecimal("R22_STAGE1_PROVISIONS"));
obj.setR22_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R22_QUALIFY_STAGE2_PROVISIONS"));
obj.setR22_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R22_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R23
// =========================
obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
obj.setR23_STAGE1_PROVISIONS(rs.getBigDecimal("R23_STAGE1_PROVISIONS"));
obj.setR23_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R23_QUALIFY_STAGE2_PROVISIONS"));
obj.setR23_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R23_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R24
// =========================
obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
obj.setR24_STAGE1_PROVISIONS(rs.getBigDecimal("R24_STAGE1_PROVISIONS"));
obj.setR24_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R24_QUALIFY_STAGE2_PROVISIONS"));
obj.setR24_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R24_TOTAL_GENERAL_PROVISIONS"));


// =========================
// R25
// =========================
obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
obj.setR25_STAGE1_PROVISIONS(rs.getBigDecimal("R25_STAGE1_PROVISIONS"));
obj.setR25_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R25_QUALIFY_STAGE2_PROVISIONS"));
obj.setR25_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R25_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R26
// =========================
obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
obj.setR26_STAGE1_PROVISIONS(rs.getBigDecimal("R26_STAGE1_PROVISIONS"));
obj.setR26_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R26_QUALIFY_STAGE2_PROVISIONS"));
obj.setR26_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R26_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R27
// =========================
obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
obj.setR27_STAGE1_PROVISIONS(rs.getBigDecimal("R27_STAGE1_PROVISIONS"));
obj.setR27_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R27_QUALIFY_STAGE2_PROVISIONS"));
obj.setR27_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R27_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R28
// =========================
obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
obj.setR28_STAGE1_PROVISIONS(rs.getBigDecimal("R28_STAGE1_PROVISIONS"));
obj.setR28_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R28_QUALIFY_STAGE2_PROVISIONS"));
obj.setR28_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R28_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R29
// =========================
obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
obj.setR29_STAGE1_PROVISIONS(rs.getBigDecimal("R29_STAGE1_PROVISIONS"));
obj.setR29_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R29_QUALIFY_STAGE2_PROVISIONS"));
obj.setR29_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R29_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R30
// =========================
obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
obj.setR30_STAGE1_PROVISIONS(rs.getBigDecimal("R30_STAGE1_PROVISIONS"));
obj.setR30_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R30_QUALIFY_STAGE2_PROVISIONS"));
obj.setR30_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R30_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R31
// =========================
obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
obj.setR31_STAGE1_PROVISIONS(rs.getBigDecimal("R31_STAGE1_PROVISIONS"));
obj.setR31_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R31_QUALIFY_STAGE2_PROVISIONS"));
obj.setR31_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R31_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R32
// =========================
obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
obj.setR32_STAGE1_PROVISIONS(rs.getBigDecimal("R32_STAGE1_PROVISIONS"));
obj.setR32_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R32_QUALIFY_STAGE2_PROVISIONS"));
obj.setR32_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R32_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R33
// =========================
obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
obj.setR33_STAGE1_PROVISIONS(rs.getBigDecimal("R33_STAGE1_PROVISIONS"));
obj.setR33_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R33_QUALIFY_STAGE2_PROVISIONS"));
obj.setR33_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R33_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R34
// =========================
obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
obj.setR34_STAGE1_PROVISIONS(rs.getBigDecimal("R34_STAGE1_PROVISIONS"));
obj.setR34_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R34_QUALIFY_STAGE2_PROVISIONS"));
obj.setR34_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R34_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R35
// =========================
obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
obj.setR35_STAGE1_PROVISIONS(rs.getBigDecimal("R35_STAGE1_PROVISIONS"));
obj.setR35_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R35_QUALIFY_STAGE2_PROVISIONS"));
obj.setR35_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R35_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R36
// =========================
obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
obj.setR36_STAGE1_PROVISIONS(rs.getBigDecimal("R36_STAGE1_PROVISIONS"));
obj.setR36_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R36_QUALIFY_STAGE2_PROVISIONS"));
obj.setR36_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R36_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R37
// =========================
obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
obj.setR37_STAGE1_PROVISIONS(rs.getBigDecimal("R37_STAGE1_PROVISIONS"));
obj.setR37_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R37_QUALIFY_STAGE2_PROVISIONS"));
obj.setR37_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R37_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R38
// =========================
obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
obj.setR38_STAGE1_PROVISIONS(rs.getBigDecimal("R38_STAGE1_PROVISIONS"));
obj.setR38_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R38_QUALIFY_STAGE2_PROVISIONS"));
obj.setR38_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R38_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R39
// =========================
obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
obj.setR39_STAGE1_PROVISIONS(rs.getBigDecimal("R39_STAGE1_PROVISIONS"));
obj.setR39_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R39_QUALIFY_STAGE2_PROVISIONS"));
obj.setR39_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R39_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R40
// =========================
obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
obj.setR40_STAGE1_PROVISIONS(rs.getBigDecimal("R40_STAGE1_PROVISIONS"));
obj.setR40_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R40_QUALIFY_STAGE2_PROVISIONS"));
obj.setR40_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R40_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R41
// =========================
obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
obj.setR41_STAGE1_PROVISIONS(rs.getBigDecimal("R41_STAGE1_PROVISIONS"));
obj.setR41_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R41_QUALIFY_STAGE2_PROVISIONS"));
obj.setR41_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R41_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R42
// =========================
obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
obj.setR42_STAGE1_PROVISIONS(rs.getBigDecimal("R42_STAGE1_PROVISIONS"));
obj.setR42_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R42_QUALIFY_STAGE2_PROVISIONS"));
obj.setR42_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R42_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R43
// =========================
obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
obj.setR43_STAGE1_PROVISIONS(rs.getBigDecimal("R43_STAGE1_PROVISIONS"));
obj.setR43_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R43_QUALIFY_STAGE2_PROVISIONS"));
obj.setR43_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R43_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R44
// =========================
obj.setR44_PRODUCT(rs.getString("R44_PRODUCT"));
obj.setR44_STAGE1_PROVISIONS(rs.getBigDecimal("R44_STAGE1_PROVISIONS"));
obj.setR44_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R44_QUALIFY_STAGE2_PROVISIONS"));
obj.setR44_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R44_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R45
// =========================
obj.setR45_PRODUCT(rs.getString("R45_PRODUCT"));
obj.setR45_STAGE1_PROVISIONS(rs.getBigDecimal("R45_STAGE1_PROVISIONS"));
obj.setR45_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R45_QUALIFY_STAGE2_PROVISIONS"));
obj.setR45_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R45_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R46
// =========================
obj.setR46_PRODUCT(rs.getString("R46_PRODUCT"));
obj.setR46_STAGE1_PROVISIONS(rs.getBigDecimal("R46_STAGE1_PROVISIONS"));
obj.setR46_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R46_QUALIFY_STAGE2_PROVISIONS"));
obj.setR46_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R46_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R47
// =========================
obj.setR47_PRODUCT(rs.getString("R47_PRODUCT"));
obj.setR47_STAGE1_PROVISIONS(rs.getBigDecimal("R47_STAGE1_PROVISIONS"));
obj.setR47_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R47_QUALIFY_STAGE2_PROVISIONS"));
obj.setR47_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R47_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R48
// =========================
obj.setR48_PRODUCT(rs.getString("R48_PRODUCT"));
obj.setR48_STAGE1_PROVISIONS(rs.getBigDecimal("R48_STAGE1_PROVISIONS"));
obj.setR48_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R48_QUALIFY_STAGE2_PROVISIONS"));
obj.setR48_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R48_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R49
// =========================
obj.setR49_PRODUCT(rs.getString("R49_PRODUCT"));
obj.setR49_STAGE1_PROVISIONS(rs.getBigDecimal("R49_STAGE1_PROVISIONS"));
obj.setR49_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R49_QUALIFY_STAGE2_PROVISIONS"));
obj.setR49_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R49_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R50
// =========================
obj.setR50_PRODUCT(rs.getString("R50_PRODUCT"));
obj.setR50_STAGE1_PROVISIONS(rs.getBigDecimal("R50_STAGE1_PROVISIONS"));
obj.setR50_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R50_QUALIFY_STAGE2_PROVISIONS"));
obj.setR50_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R50_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R51
// =========================
obj.setR51_PRODUCT(rs.getString("R51_PRODUCT"));
obj.setR51_STAGE1_PROVISIONS(rs.getBigDecimal("R51_STAGE1_PROVISIONS"));
obj.setR51_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R51_QUALIFY_STAGE2_PROVISIONS"));
obj.setR51_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R51_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R52
// =========================
obj.setR52_PRODUCT(rs.getString("R52_PRODUCT"));
obj.setR52_STAGE1_PROVISIONS(rs.getBigDecimal("R52_STAGE1_PROVISIONS"));
obj.setR52_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R52_QUALIFY_STAGE2_PROVISIONS"));
obj.setR52_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R52_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R53
// =========================
obj.setR53_PRODUCT(rs.getString("R53_PRODUCT"));
obj.setR53_STAGE1_PROVISIONS(rs.getBigDecimal("R53_STAGE1_PROVISIONS"));
obj.setR53_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R53_QUALIFY_STAGE2_PROVISIONS"));
obj.setR53_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R53_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R54
// =========================
obj.setR54_PRODUCT(rs.getString("R54_PRODUCT"));
obj.setR54_STAGE1_PROVISIONS(rs.getBigDecimal("R54_STAGE1_PROVISIONS"));
obj.setR54_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R54_QUALIFY_STAGE2_PROVISIONS"));
obj.setR54_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R54_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R55
// =========================
obj.setR55_PRODUCT(rs.getString("R55_PRODUCT"));
obj.setR55_STAGE1_PROVISIONS(rs.getBigDecimal("R55_STAGE1_PROVISIONS"));
obj.setR55_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R55_QUALIFY_STAGE2_PROVISIONS"));
obj.setR55_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R55_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R56
// =========================
obj.setR56_PRODUCT(rs.getString("R56_PRODUCT"));
obj.setR56_STAGE1_PROVISIONS(rs.getBigDecimal("R56_STAGE1_PROVISIONS"));
obj.setR56_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R56_QUALIFY_STAGE2_PROVISIONS"));
obj.setR56_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R56_TOTAL_GENERAL_PROVISIONS"));


// =========================
// R57
// =========================
obj.setR57_PRODUCT(rs.getString("R57_PRODUCT"));
obj.setR57_STAGE1_PROVISIONS(rs.getBigDecimal("R57_STAGE1_PROVISIONS"));
obj.setR57_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R57_QUALIFY_STAGE2_PROVISIONS"));
obj.setR57_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R57_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R58
// =========================
obj.setR58_PRODUCT(rs.getString("R58_PRODUCT"));
obj.setR58_STAGE1_PROVISIONS(rs.getBigDecimal("R58_STAGE1_PROVISIONS"));
obj.setR58_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R58_QUALIFY_STAGE2_PROVISIONS"));
obj.setR58_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R58_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R59
// =========================
obj.setR59_PRODUCT(rs.getString("R59_PRODUCT"));
obj.setR59_STAGE1_PROVISIONS(rs.getBigDecimal("R59_STAGE1_PROVISIONS"));
obj.setR59_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R59_QUALIFY_STAGE2_PROVISIONS"));
obj.setR59_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R59_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R60
// =========================
obj.setR60_PRODUCT(rs.getString("R60_PRODUCT"));
obj.setR60_STAGE1_PROVISIONS(rs.getBigDecimal("R60_STAGE1_PROVISIONS"));
obj.setR60_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R60_QUALIFY_STAGE2_PROVISIONS"));
obj.setR60_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R60_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R61
// =========================
obj.setR61_PRODUCT(rs.getString("R61_PRODUCT"));
obj.setR61_STAGE1_PROVISIONS(rs.getBigDecimal("R61_STAGE1_PROVISIONS"));
obj.setR61_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R61_QUALIFY_STAGE2_PROVISIONS"));
obj.setR61_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R61_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R62
// =========================
obj.setR62_PRODUCT(rs.getString("R62_PRODUCT"));
obj.setR62_STAGE1_PROVISIONS(rs.getBigDecimal("R62_STAGE1_PROVISIONS"));
obj.setR62_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R62_QUALIFY_STAGE2_PROVISIONS"));
obj.setR62_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R62_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R63
// =========================
obj.setR63_PRODUCT(rs.getString("R63_PRODUCT"));
obj.setR63_STAGE1_PROVISIONS(rs.getBigDecimal("R63_STAGE1_PROVISIONS"));
obj.setR63_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R63_QUALIFY_STAGE2_PROVISIONS"));
obj.setR63_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R63_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R64
// =========================
obj.setR64_PRODUCT(rs.getString("R64_PRODUCT"));
obj.setR64_STAGE1_PROVISIONS(rs.getBigDecimal("R64_STAGE1_PROVISIONS"));
obj.setR64_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R64_QUALIFY_STAGE2_PROVISIONS"));
obj.setR64_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R64_TOTAL_GENERAL_PROVISIONS"));


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

public class M_GP_Archival_Detail_Entity {

   private String R11_PRODUCT;
	private BigDecimal R11_STAGE1_PROVISIONS;
	private BigDecimal R11_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R11_TOTAL_GENERAL_PROVISIONS;

	private String R12_PRODUCT;
	private BigDecimal R12_STAGE1_PROVISIONS;
	private BigDecimal R12_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R12_TOTAL_GENERAL_PROVISIONS;

	private String R13_PRODUCT;
	private BigDecimal R13_STAGE1_PROVISIONS;
	private BigDecimal R13_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R13_TOTAL_GENERAL_PROVISIONS;

	private String R14_PRODUCT;
	private BigDecimal R14_STAGE1_PROVISIONS;
	private BigDecimal R14_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R14_TOTAL_GENERAL_PROVISIONS;

	private String R15_PRODUCT;
	private BigDecimal R15_STAGE1_PROVISIONS;
	private BigDecimal R15_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R15_TOTAL_GENERAL_PROVISIONS;

	private String R16_PRODUCT;
	private BigDecimal R16_STAGE1_PROVISIONS;
	private BigDecimal R16_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R16_TOTAL_GENERAL_PROVISIONS;

	private String R17_PRODUCT;
	private BigDecimal R17_STAGE1_PROVISIONS;
	private BigDecimal R17_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R17_TOTAL_GENERAL_PROVISIONS;

	private String R18_PRODUCT;
	private BigDecimal R18_STAGE1_PROVISIONS;
	private BigDecimal R18_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R18_TOTAL_GENERAL_PROVISIONS;

	private String R19_PRODUCT;
	private BigDecimal R19_STAGE1_PROVISIONS;
	private BigDecimal R19_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R19_TOTAL_GENERAL_PROVISIONS;

	private String R20_PRODUCT;
	private BigDecimal R20_STAGE1_PROVISIONS;
	private BigDecimal R20_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R20_TOTAL_GENERAL_PROVISIONS;

	private String R21_PRODUCT;
	private BigDecimal R21_STAGE1_PROVISIONS;
	private BigDecimal R21_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R21_TOTAL_GENERAL_PROVISIONS;

	private String R22_PRODUCT;
	private BigDecimal R22_STAGE1_PROVISIONS;
	private BigDecimal R22_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R22_TOTAL_GENERAL_PROVISIONS;

	private String R23_PRODUCT;
	private BigDecimal R23_STAGE1_PROVISIONS;
	private BigDecimal R23_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R23_TOTAL_GENERAL_PROVISIONS;

	private String R24_PRODUCT;
	private BigDecimal R24_STAGE1_PROVISIONS;
	private BigDecimal R24_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R24_TOTAL_GENERAL_PROVISIONS;

	private String R25_PRODUCT;
	private BigDecimal R25_STAGE1_PROVISIONS;
	private BigDecimal R25_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R25_TOTAL_GENERAL_PROVISIONS;

	private String R26_PRODUCT;
	private BigDecimal R26_STAGE1_PROVISIONS;
	private BigDecimal R26_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R26_TOTAL_GENERAL_PROVISIONS;

	private String R27_PRODUCT;
	private BigDecimal R27_STAGE1_PROVISIONS;
	private BigDecimal R27_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R27_TOTAL_GENERAL_PROVISIONS;

	private String R28_PRODUCT;
	private BigDecimal R28_STAGE1_PROVISIONS;
	private BigDecimal R28_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R28_TOTAL_GENERAL_PROVISIONS;

	private String R29_PRODUCT;
	private BigDecimal R29_STAGE1_PROVISIONS;
	private BigDecimal R29_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R29_TOTAL_GENERAL_PROVISIONS;

	private String R30_PRODUCT;
	private BigDecimal R30_STAGE1_PROVISIONS;
	private BigDecimal R30_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R30_TOTAL_GENERAL_PROVISIONS;

	private String R31_PRODUCT;
	private BigDecimal R31_STAGE1_PROVISIONS;
	private BigDecimal R31_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R31_TOTAL_GENERAL_PROVISIONS;

	private String R32_PRODUCT;
	private BigDecimal R32_STAGE1_PROVISIONS;
	private BigDecimal R32_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R32_TOTAL_GENERAL_PROVISIONS;

	private String R33_PRODUCT;
	private BigDecimal R33_STAGE1_PROVISIONS;
	private BigDecimal R33_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R33_TOTAL_GENERAL_PROVISIONS;

	private String R34_PRODUCT;
	private BigDecimal R34_STAGE1_PROVISIONS;
	private BigDecimal R34_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R34_TOTAL_GENERAL_PROVISIONS;

	private String R35_PRODUCT;
	private BigDecimal R35_STAGE1_PROVISIONS;
	private BigDecimal R35_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R35_TOTAL_GENERAL_PROVISIONS;

	private String R36_PRODUCT;
	private BigDecimal R36_STAGE1_PROVISIONS;
	private BigDecimal R36_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R36_TOTAL_GENERAL_PROVISIONS;

	private String R37_PRODUCT;
	private BigDecimal R37_STAGE1_PROVISIONS;
	private BigDecimal R37_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R37_TOTAL_GENERAL_PROVISIONS;

	private String R38_PRODUCT;
	private BigDecimal R38_STAGE1_PROVISIONS;
	private BigDecimal R38_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R38_TOTAL_GENERAL_PROVISIONS;

	private String R39_PRODUCT;
	private BigDecimal R39_STAGE1_PROVISIONS;
	private BigDecimal R39_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R39_TOTAL_GENERAL_PROVISIONS;

	private String R40_PRODUCT;
	private BigDecimal R40_STAGE1_PROVISIONS;
	private BigDecimal R40_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R40_TOTAL_GENERAL_PROVISIONS;

	private String R41_PRODUCT;
	private BigDecimal R41_STAGE1_PROVISIONS;
	private BigDecimal R41_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R41_TOTAL_GENERAL_PROVISIONS;

	private String R42_PRODUCT;
	private BigDecimal R42_STAGE1_PROVISIONS;
	private BigDecimal R42_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R42_TOTAL_GENERAL_PROVISIONS;

	private String R43_PRODUCT;
	private BigDecimal R43_STAGE1_PROVISIONS;
	private BigDecimal R43_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R43_TOTAL_GENERAL_PROVISIONS;

	private String R44_PRODUCT;
	private BigDecimal R44_STAGE1_PROVISIONS;
	private BigDecimal R44_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R44_TOTAL_GENERAL_PROVISIONS;

	private String R45_PRODUCT;
	private BigDecimal R45_STAGE1_PROVISIONS;
	private BigDecimal R45_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R45_TOTAL_GENERAL_PROVISIONS;

	private String R46_PRODUCT;
	private BigDecimal R46_STAGE1_PROVISIONS;
	private BigDecimal R46_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R46_TOTAL_GENERAL_PROVISIONS;

	private String R47_PRODUCT;
	private BigDecimal R47_STAGE1_PROVISIONS;
	private BigDecimal R47_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R47_TOTAL_GENERAL_PROVISIONS;

	private String R48_PRODUCT;
	private BigDecimal R48_STAGE1_PROVISIONS;
	private BigDecimal R48_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R48_TOTAL_GENERAL_PROVISIONS;

	private String R49_PRODUCT;
	private BigDecimal R49_STAGE1_PROVISIONS;
	private BigDecimal R49_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R49_TOTAL_GENERAL_PROVISIONS;

	private String R50_PRODUCT;
	private BigDecimal R50_STAGE1_PROVISIONS;
	private BigDecimal R50_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R50_TOTAL_GENERAL_PROVISIONS;

	private String R51_PRODUCT;
	private BigDecimal R51_STAGE1_PROVISIONS;
	private BigDecimal R51_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R51_TOTAL_GENERAL_PROVISIONS;

	private String R52_PRODUCT;
	private BigDecimal R52_STAGE1_PROVISIONS;
	private BigDecimal R52_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R52_TOTAL_GENERAL_PROVISIONS;

	private String R53_PRODUCT;
	private BigDecimal R53_STAGE1_PROVISIONS;
	private BigDecimal R53_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R53_TOTAL_GENERAL_PROVISIONS;

	private String R54_PRODUCT;
	private BigDecimal R54_STAGE1_PROVISIONS;
	private BigDecimal R54_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R54_TOTAL_GENERAL_PROVISIONS;

	private String R55_PRODUCT;
	private BigDecimal R55_STAGE1_PROVISIONS;
	private BigDecimal R55_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R55_TOTAL_GENERAL_PROVISIONS;

	private String R56_PRODUCT;
	private BigDecimal R56_STAGE1_PROVISIONS;
	private BigDecimal R56_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R56_TOTAL_GENERAL_PROVISIONS;

	private String R57_PRODUCT;
	private BigDecimal R57_STAGE1_PROVISIONS;
	private BigDecimal R57_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R57_TOTAL_GENERAL_PROVISIONS;

	private String R58_PRODUCT;
	private BigDecimal R58_STAGE1_PROVISIONS;
	private BigDecimal R58_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R58_TOTAL_GENERAL_PROVISIONS;

	private String R59_PRODUCT;
	private BigDecimal R59_STAGE1_PROVISIONS;
	private BigDecimal R59_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R59_TOTAL_GENERAL_PROVISIONS;

	private String R60_PRODUCT;
	private BigDecimal R60_STAGE1_PROVISIONS;
	private BigDecimal R60_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R60_TOTAL_GENERAL_PROVISIONS;

	private String R61_PRODUCT;
	private BigDecimal R61_STAGE1_PROVISIONS;
	private BigDecimal R61_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R61_TOTAL_GENERAL_PROVISIONS;

	private String R62_PRODUCT;
	private BigDecimal R62_STAGE1_PROVISIONS;
	private BigDecimal R62_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R62_TOTAL_GENERAL_PROVISIONS;

	private String R63_PRODUCT;
	private BigDecimal R63_STAGE1_PROVISIONS;
	private BigDecimal R63_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R63_TOTAL_GENERAL_PROVISIONS;

	private String R64_PRODUCT;
	private BigDecimal R64_STAGE1_PROVISIONS;
	private BigDecimal R64_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R64_TOTAL_GENERAL_PROVISIONS;
	
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
	
	
public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_STAGE1_PROVISIONS() {
		return R11_STAGE1_PROVISIONS;
	}
	public void setR11_STAGE1_PROVISIONS(BigDecimal r11_STAGE1_PROVISIONS) {
		R11_STAGE1_PROVISIONS = r11_STAGE1_PROVISIONS;
	}
	public BigDecimal getR11_QUALIFY_STAGE2_PROVISIONS() {
		return R11_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR11_QUALIFY_STAGE2_PROVISIONS(BigDecimal r11_QUALIFY_STAGE2_PROVISIONS) {
		R11_QUALIFY_STAGE2_PROVISIONS = r11_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR11_TOTAL_GENERAL_PROVISIONS() {
		return R11_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR11_TOTAL_GENERAL_PROVISIONS(BigDecimal r11_TOTAL_GENERAL_PROVISIONS) {
		R11_TOTAL_GENERAL_PROVISIONS = r11_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_STAGE1_PROVISIONS() {
		return R12_STAGE1_PROVISIONS;
	}
	public void setR12_STAGE1_PROVISIONS(BigDecimal r12_STAGE1_PROVISIONS) {
		R12_STAGE1_PROVISIONS = r12_STAGE1_PROVISIONS;
	}
	public BigDecimal getR12_QUALIFY_STAGE2_PROVISIONS() {
		return R12_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR12_QUALIFY_STAGE2_PROVISIONS(BigDecimal r12_QUALIFY_STAGE2_PROVISIONS) {
		R12_QUALIFY_STAGE2_PROVISIONS = r12_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR12_TOTAL_GENERAL_PROVISIONS() {
		return R12_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR12_TOTAL_GENERAL_PROVISIONS(BigDecimal r12_TOTAL_GENERAL_PROVISIONS) {
		R12_TOTAL_GENERAL_PROVISIONS = r12_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_STAGE1_PROVISIONS() {
		return R13_STAGE1_PROVISIONS;
	}
	public void setR13_STAGE1_PROVISIONS(BigDecimal r13_STAGE1_PROVISIONS) {
		R13_STAGE1_PROVISIONS = r13_STAGE1_PROVISIONS;
	}
	public BigDecimal getR13_QUALIFY_STAGE2_PROVISIONS() {
		return R13_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR13_QUALIFY_STAGE2_PROVISIONS(BigDecimal r13_QUALIFY_STAGE2_PROVISIONS) {
		R13_QUALIFY_STAGE2_PROVISIONS = r13_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR13_TOTAL_GENERAL_PROVISIONS() {
		return R13_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR13_TOTAL_GENERAL_PROVISIONS(BigDecimal r13_TOTAL_GENERAL_PROVISIONS) {
		R13_TOTAL_GENERAL_PROVISIONS = r13_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_STAGE1_PROVISIONS() {
		return R14_STAGE1_PROVISIONS;
	}
	public void setR14_STAGE1_PROVISIONS(BigDecimal r14_STAGE1_PROVISIONS) {
		R14_STAGE1_PROVISIONS = r14_STAGE1_PROVISIONS;
	}
	public BigDecimal getR14_QUALIFY_STAGE2_PROVISIONS() {
		return R14_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR14_QUALIFY_STAGE2_PROVISIONS(BigDecimal r14_QUALIFY_STAGE2_PROVISIONS) {
		R14_QUALIFY_STAGE2_PROVISIONS = r14_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR14_TOTAL_GENERAL_PROVISIONS() {
		return R14_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR14_TOTAL_GENERAL_PROVISIONS(BigDecimal r14_TOTAL_GENERAL_PROVISIONS) {
		R14_TOTAL_GENERAL_PROVISIONS = r14_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_STAGE1_PROVISIONS() {
		return R15_STAGE1_PROVISIONS;
	}
	public void setR15_STAGE1_PROVISIONS(BigDecimal r15_STAGE1_PROVISIONS) {
		R15_STAGE1_PROVISIONS = r15_STAGE1_PROVISIONS;
	}
	public BigDecimal getR15_QUALIFY_STAGE2_PROVISIONS() {
		return R15_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR15_QUALIFY_STAGE2_PROVISIONS(BigDecimal r15_QUALIFY_STAGE2_PROVISIONS) {
		R15_QUALIFY_STAGE2_PROVISIONS = r15_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR15_TOTAL_GENERAL_PROVISIONS() {
		return R15_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR15_TOTAL_GENERAL_PROVISIONS(BigDecimal r15_TOTAL_GENERAL_PROVISIONS) {
		R15_TOTAL_GENERAL_PROVISIONS = r15_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_STAGE1_PROVISIONS() {
		return R16_STAGE1_PROVISIONS;
	}
	public void setR16_STAGE1_PROVISIONS(BigDecimal r16_STAGE1_PROVISIONS) {
		R16_STAGE1_PROVISIONS = r16_STAGE1_PROVISIONS;
	}
	public BigDecimal getR16_QUALIFY_STAGE2_PROVISIONS() {
		return R16_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR16_QUALIFY_STAGE2_PROVISIONS(BigDecimal r16_QUALIFY_STAGE2_PROVISIONS) {
		R16_QUALIFY_STAGE2_PROVISIONS = r16_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR16_TOTAL_GENERAL_PROVISIONS() {
		return R16_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR16_TOTAL_GENERAL_PROVISIONS(BigDecimal r16_TOTAL_GENERAL_PROVISIONS) {
		R16_TOTAL_GENERAL_PROVISIONS = r16_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_STAGE1_PROVISIONS() {
		return R17_STAGE1_PROVISIONS;
	}
	public void setR17_STAGE1_PROVISIONS(BigDecimal r17_STAGE1_PROVISIONS) {
		R17_STAGE1_PROVISIONS = r17_STAGE1_PROVISIONS;
	}
	public BigDecimal getR17_QUALIFY_STAGE2_PROVISIONS() {
		return R17_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR17_QUALIFY_STAGE2_PROVISIONS(BigDecimal r17_QUALIFY_STAGE2_PROVISIONS) {
		R17_QUALIFY_STAGE2_PROVISIONS = r17_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR17_TOTAL_GENERAL_PROVISIONS() {
		return R17_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR17_TOTAL_GENERAL_PROVISIONS(BigDecimal r17_TOTAL_GENERAL_PROVISIONS) {
		R17_TOTAL_GENERAL_PROVISIONS = r17_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}
	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}
	public BigDecimal getR18_STAGE1_PROVISIONS() {
		return R18_STAGE1_PROVISIONS;
	}
	public void setR18_STAGE1_PROVISIONS(BigDecimal r18_STAGE1_PROVISIONS) {
		R18_STAGE1_PROVISIONS = r18_STAGE1_PROVISIONS;
	}
	public BigDecimal getR18_QUALIFY_STAGE2_PROVISIONS() {
		return R18_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR18_QUALIFY_STAGE2_PROVISIONS(BigDecimal r18_QUALIFY_STAGE2_PROVISIONS) {
		R18_QUALIFY_STAGE2_PROVISIONS = r18_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR18_TOTAL_GENERAL_PROVISIONS() {
		return R18_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR18_TOTAL_GENERAL_PROVISIONS(BigDecimal r18_TOTAL_GENERAL_PROVISIONS) {
		R18_TOTAL_GENERAL_PROVISIONS = r18_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}
	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}
	public BigDecimal getR19_STAGE1_PROVISIONS() {
		return R19_STAGE1_PROVISIONS;
	}
	public void setR19_STAGE1_PROVISIONS(BigDecimal r19_STAGE1_PROVISIONS) {
		R19_STAGE1_PROVISIONS = r19_STAGE1_PROVISIONS;
	}
	public BigDecimal getR19_QUALIFY_STAGE2_PROVISIONS() {
		return R19_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR19_QUALIFY_STAGE2_PROVISIONS(BigDecimal r19_QUALIFY_STAGE2_PROVISIONS) {
		R19_QUALIFY_STAGE2_PROVISIONS = r19_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR19_TOTAL_GENERAL_PROVISIONS() {
		return R19_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR19_TOTAL_GENERAL_PROVISIONS(BigDecimal r19_TOTAL_GENERAL_PROVISIONS) {
		R19_TOTAL_GENERAL_PROVISIONS = r19_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}
	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}
	public BigDecimal getR20_STAGE1_PROVISIONS() {
		return R20_STAGE1_PROVISIONS;
	}
	public void setR20_STAGE1_PROVISIONS(BigDecimal r20_STAGE1_PROVISIONS) {
		R20_STAGE1_PROVISIONS = r20_STAGE1_PROVISIONS;
	}
	public BigDecimal getR20_QUALIFY_STAGE2_PROVISIONS() {
		return R20_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR20_QUALIFY_STAGE2_PROVISIONS(BigDecimal r20_QUALIFY_STAGE2_PROVISIONS) {
		R20_QUALIFY_STAGE2_PROVISIONS = r20_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR20_TOTAL_GENERAL_PROVISIONS() {
		return R20_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR20_TOTAL_GENERAL_PROVISIONS(BigDecimal r20_TOTAL_GENERAL_PROVISIONS) {
		R20_TOTAL_GENERAL_PROVISIONS = r20_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}
	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}
	public BigDecimal getR21_STAGE1_PROVISIONS() {
		return R21_STAGE1_PROVISIONS;
	}
	public void setR21_STAGE1_PROVISIONS(BigDecimal r21_STAGE1_PROVISIONS) {
		R21_STAGE1_PROVISIONS = r21_STAGE1_PROVISIONS;
	}
	public BigDecimal getR21_QUALIFY_STAGE2_PROVISIONS() {
		return R21_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR21_QUALIFY_STAGE2_PROVISIONS(BigDecimal r21_QUALIFY_STAGE2_PROVISIONS) {
		R21_QUALIFY_STAGE2_PROVISIONS = r21_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR21_TOTAL_GENERAL_PROVISIONS() {
		return R21_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR21_TOTAL_GENERAL_PROVISIONS(BigDecimal r21_TOTAL_GENERAL_PROVISIONS) {
		R21_TOTAL_GENERAL_PROVISIONS = r21_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}
	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}
	public BigDecimal getR22_STAGE1_PROVISIONS() {
		return R22_STAGE1_PROVISIONS;
	}
	public void setR22_STAGE1_PROVISIONS(BigDecimal r22_STAGE1_PROVISIONS) {
		R22_STAGE1_PROVISIONS = r22_STAGE1_PROVISIONS;
	}
	public BigDecimal getR22_QUALIFY_STAGE2_PROVISIONS() {
		return R22_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR22_QUALIFY_STAGE2_PROVISIONS(BigDecimal r22_QUALIFY_STAGE2_PROVISIONS) {
		R22_QUALIFY_STAGE2_PROVISIONS = r22_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR22_TOTAL_GENERAL_PROVISIONS() {
		return R22_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR22_TOTAL_GENERAL_PROVISIONS(BigDecimal r22_TOTAL_GENERAL_PROVISIONS) {
		R22_TOTAL_GENERAL_PROVISIONS = r22_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}
	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}
	public BigDecimal getR23_STAGE1_PROVISIONS() {
		return R23_STAGE1_PROVISIONS;
	}
	public void setR23_STAGE1_PROVISIONS(BigDecimal r23_STAGE1_PROVISIONS) {
		R23_STAGE1_PROVISIONS = r23_STAGE1_PROVISIONS;
	}
	public BigDecimal getR23_QUALIFY_STAGE2_PROVISIONS() {
		return R23_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR23_QUALIFY_STAGE2_PROVISIONS(BigDecimal r23_QUALIFY_STAGE2_PROVISIONS) {
		R23_QUALIFY_STAGE2_PROVISIONS = r23_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR23_TOTAL_GENERAL_PROVISIONS() {
		return R23_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR23_TOTAL_GENERAL_PROVISIONS(BigDecimal r23_TOTAL_GENERAL_PROVISIONS) {
		R23_TOTAL_GENERAL_PROVISIONS = r23_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}
	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}
	public BigDecimal getR24_STAGE1_PROVISIONS() {
		return R24_STAGE1_PROVISIONS;
	}
	public void setR24_STAGE1_PROVISIONS(BigDecimal r24_STAGE1_PROVISIONS) {
		R24_STAGE1_PROVISIONS = r24_STAGE1_PROVISIONS;
	}
	public BigDecimal getR24_QUALIFY_STAGE2_PROVISIONS() {
		return R24_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR24_QUALIFY_STAGE2_PROVISIONS(BigDecimal r24_QUALIFY_STAGE2_PROVISIONS) {
		R24_QUALIFY_STAGE2_PROVISIONS = r24_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR24_TOTAL_GENERAL_PROVISIONS() {
		return R24_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR24_TOTAL_GENERAL_PROVISIONS(BigDecimal r24_TOTAL_GENERAL_PROVISIONS) {
		R24_TOTAL_GENERAL_PROVISIONS = r24_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}
	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}
	public BigDecimal getR25_STAGE1_PROVISIONS() {
		return R25_STAGE1_PROVISIONS;
	}
	public void setR25_STAGE1_PROVISIONS(BigDecimal r25_STAGE1_PROVISIONS) {
		R25_STAGE1_PROVISIONS = r25_STAGE1_PROVISIONS;
	}
	public BigDecimal getR25_QUALIFY_STAGE2_PROVISIONS() {
		return R25_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR25_QUALIFY_STAGE2_PROVISIONS(BigDecimal r25_QUALIFY_STAGE2_PROVISIONS) {
		R25_QUALIFY_STAGE2_PROVISIONS = r25_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR25_TOTAL_GENERAL_PROVISIONS() {
		return R25_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR25_TOTAL_GENERAL_PROVISIONS(BigDecimal r25_TOTAL_GENERAL_PROVISIONS) {
		R25_TOTAL_GENERAL_PROVISIONS = r25_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}
	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}
	public BigDecimal getR26_STAGE1_PROVISIONS() {
		return R26_STAGE1_PROVISIONS;
	}
	public void setR26_STAGE1_PROVISIONS(BigDecimal r26_STAGE1_PROVISIONS) {
		R26_STAGE1_PROVISIONS = r26_STAGE1_PROVISIONS;
	}
	public BigDecimal getR26_QUALIFY_STAGE2_PROVISIONS() {
		return R26_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR26_QUALIFY_STAGE2_PROVISIONS(BigDecimal r26_QUALIFY_STAGE2_PROVISIONS) {
		R26_QUALIFY_STAGE2_PROVISIONS = r26_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR26_TOTAL_GENERAL_PROVISIONS() {
		return R26_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR26_TOTAL_GENERAL_PROVISIONS(BigDecimal r26_TOTAL_GENERAL_PROVISIONS) {
		R26_TOTAL_GENERAL_PROVISIONS = r26_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}
	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}
	public BigDecimal getR27_STAGE1_PROVISIONS() {
		return R27_STAGE1_PROVISIONS;
	}
	public void setR27_STAGE1_PROVISIONS(BigDecimal r27_STAGE1_PROVISIONS) {
		R27_STAGE1_PROVISIONS = r27_STAGE1_PROVISIONS;
	}
	public BigDecimal getR27_QUALIFY_STAGE2_PROVISIONS() {
		return R27_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR27_QUALIFY_STAGE2_PROVISIONS(BigDecimal r27_QUALIFY_STAGE2_PROVISIONS) {
		R27_QUALIFY_STAGE2_PROVISIONS = r27_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR27_TOTAL_GENERAL_PROVISIONS() {
		return R27_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR27_TOTAL_GENERAL_PROVISIONS(BigDecimal r27_TOTAL_GENERAL_PROVISIONS) {
		R27_TOTAL_GENERAL_PROVISIONS = r27_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}
	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}
	public BigDecimal getR28_STAGE1_PROVISIONS() {
		return R28_STAGE1_PROVISIONS;
	}
	public void setR28_STAGE1_PROVISIONS(BigDecimal r28_STAGE1_PROVISIONS) {
		R28_STAGE1_PROVISIONS = r28_STAGE1_PROVISIONS;
	}
	public BigDecimal getR28_QUALIFY_STAGE2_PROVISIONS() {
		return R28_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR28_QUALIFY_STAGE2_PROVISIONS(BigDecimal r28_QUALIFY_STAGE2_PROVISIONS) {
		R28_QUALIFY_STAGE2_PROVISIONS = r28_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR28_TOTAL_GENERAL_PROVISIONS() {
		return R28_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR28_TOTAL_GENERAL_PROVISIONS(BigDecimal r28_TOTAL_GENERAL_PROVISIONS) {
		R28_TOTAL_GENERAL_PROVISIONS = r28_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}
	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}
	public BigDecimal getR29_STAGE1_PROVISIONS() {
		return R29_STAGE1_PROVISIONS;
	}
	public void setR29_STAGE1_PROVISIONS(BigDecimal r29_STAGE1_PROVISIONS) {
		R29_STAGE1_PROVISIONS = r29_STAGE1_PROVISIONS;
	}
	public BigDecimal getR29_QUALIFY_STAGE2_PROVISIONS() {
		return R29_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR29_QUALIFY_STAGE2_PROVISIONS(BigDecimal r29_QUALIFY_STAGE2_PROVISIONS) {
		R29_QUALIFY_STAGE2_PROVISIONS = r29_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR29_TOTAL_GENERAL_PROVISIONS() {
		return R29_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR29_TOTAL_GENERAL_PROVISIONS(BigDecimal r29_TOTAL_GENERAL_PROVISIONS) {
		R29_TOTAL_GENERAL_PROVISIONS = r29_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}
	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}
	public BigDecimal getR30_STAGE1_PROVISIONS() {
		return R30_STAGE1_PROVISIONS;
	}
	public void setR30_STAGE1_PROVISIONS(BigDecimal r30_STAGE1_PROVISIONS) {
		R30_STAGE1_PROVISIONS = r30_STAGE1_PROVISIONS;
	}
	public BigDecimal getR30_QUALIFY_STAGE2_PROVISIONS() {
		return R30_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR30_QUALIFY_STAGE2_PROVISIONS(BigDecimal r30_QUALIFY_STAGE2_PROVISIONS) {
		R30_QUALIFY_STAGE2_PROVISIONS = r30_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR30_TOTAL_GENERAL_PROVISIONS() {
		return R30_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR30_TOTAL_GENERAL_PROVISIONS(BigDecimal r30_TOTAL_GENERAL_PROVISIONS) {
		R30_TOTAL_GENERAL_PROVISIONS = r30_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}
	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}
	public BigDecimal getR31_STAGE1_PROVISIONS() {
		return R31_STAGE1_PROVISIONS;
	}
	public void setR31_STAGE1_PROVISIONS(BigDecimal r31_STAGE1_PROVISIONS) {
		R31_STAGE1_PROVISIONS = r31_STAGE1_PROVISIONS;
	}
	public BigDecimal getR31_QUALIFY_STAGE2_PROVISIONS() {
		return R31_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR31_QUALIFY_STAGE2_PROVISIONS(BigDecimal r31_QUALIFY_STAGE2_PROVISIONS) {
		R31_QUALIFY_STAGE2_PROVISIONS = r31_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR31_TOTAL_GENERAL_PROVISIONS() {
		return R31_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR31_TOTAL_GENERAL_PROVISIONS(BigDecimal r31_TOTAL_GENERAL_PROVISIONS) {
		R31_TOTAL_GENERAL_PROVISIONS = r31_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}
	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}
	public BigDecimal getR32_STAGE1_PROVISIONS() {
		return R32_STAGE1_PROVISIONS;
	}
	public void setR32_STAGE1_PROVISIONS(BigDecimal r32_STAGE1_PROVISIONS) {
		R32_STAGE1_PROVISIONS = r32_STAGE1_PROVISIONS;
	}
	public BigDecimal getR32_QUALIFY_STAGE2_PROVISIONS() {
		return R32_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR32_QUALIFY_STAGE2_PROVISIONS(BigDecimal r32_QUALIFY_STAGE2_PROVISIONS) {
		R32_QUALIFY_STAGE2_PROVISIONS = r32_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR32_TOTAL_GENERAL_PROVISIONS() {
		return R32_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR32_TOTAL_GENERAL_PROVISIONS(BigDecimal r32_TOTAL_GENERAL_PROVISIONS) {
		R32_TOTAL_GENERAL_PROVISIONS = r32_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}
	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}
	public BigDecimal getR33_STAGE1_PROVISIONS() {
		return R33_STAGE1_PROVISIONS;
	}
	public void setR33_STAGE1_PROVISIONS(BigDecimal r33_STAGE1_PROVISIONS) {
		R33_STAGE1_PROVISIONS = r33_STAGE1_PROVISIONS;
	}
	public BigDecimal getR33_QUALIFY_STAGE2_PROVISIONS() {
		return R33_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR33_QUALIFY_STAGE2_PROVISIONS(BigDecimal r33_QUALIFY_STAGE2_PROVISIONS) {
		R33_QUALIFY_STAGE2_PROVISIONS = r33_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR33_TOTAL_GENERAL_PROVISIONS() {
		return R33_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR33_TOTAL_GENERAL_PROVISIONS(BigDecimal r33_TOTAL_GENERAL_PROVISIONS) {
		R33_TOTAL_GENERAL_PROVISIONS = r33_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}
	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}
	public BigDecimal getR34_STAGE1_PROVISIONS() {
		return R34_STAGE1_PROVISIONS;
	}
	public void setR34_STAGE1_PROVISIONS(BigDecimal r34_STAGE1_PROVISIONS) {
		R34_STAGE1_PROVISIONS = r34_STAGE1_PROVISIONS;
	}
	public BigDecimal getR34_QUALIFY_STAGE2_PROVISIONS() {
		return R34_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR34_QUALIFY_STAGE2_PROVISIONS(BigDecimal r34_QUALIFY_STAGE2_PROVISIONS) {
		R34_QUALIFY_STAGE2_PROVISIONS = r34_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR34_TOTAL_GENERAL_PROVISIONS() {
		return R34_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR34_TOTAL_GENERAL_PROVISIONS(BigDecimal r34_TOTAL_GENERAL_PROVISIONS) {
		R34_TOTAL_GENERAL_PROVISIONS = r34_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}
	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}
	public BigDecimal getR35_STAGE1_PROVISIONS() {
		return R35_STAGE1_PROVISIONS;
	}
	public void setR35_STAGE1_PROVISIONS(BigDecimal r35_STAGE1_PROVISIONS) {
		R35_STAGE1_PROVISIONS = r35_STAGE1_PROVISIONS;
	}
	public BigDecimal getR35_QUALIFY_STAGE2_PROVISIONS() {
		return R35_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR35_QUALIFY_STAGE2_PROVISIONS(BigDecimal r35_QUALIFY_STAGE2_PROVISIONS) {
		R35_QUALIFY_STAGE2_PROVISIONS = r35_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR35_TOTAL_GENERAL_PROVISIONS() {
		return R35_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR35_TOTAL_GENERAL_PROVISIONS(BigDecimal r35_TOTAL_GENERAL_PROVISIONS) {
		R35_TOTAL_GENERAL_PROVISIONS = r35_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_STAGE1_PROVISIONS() {
		return R36_STAGE1_PROVISIONS;
	}
	public void setR36_STAGE1_PROVISIONS(BigDecimal r36_STAGE1_PROVISIONS) {
		R36_STAGE1_PROVISIONS = r36_STAGE1_PROVISIONS;
	}
	public BigDecimal getR36_QUALIFY_STAGE2_PROVISIONS() {
		return R36_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR36_QUALIFY_STAGE2_PROVISIONS(BigDecimal r36_QUALIFY_STAGE2_PROVISIONS) {
		R36_QUALIFY_STAGE2_PROVISIONS = r36_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR36_TOTAL_GENERAL_PROVISIONS() {
		return R36_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR36_TOTAL_GENERAL_PROVISIONS(BigDecimal r36_TOTAL_GENERAL_PROVISIONS) {
		R36_TOTAL_GENERAL_PROVISIONS = r36_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}
	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}
	public BigDecimal getR37_STAGE1_PROVISIONS() {
		return R37_STAGE1_PROVISIONS;
	}
	public void setR37_STAGE1_PROVISIONS(BigDecimal r37_STAGE1_PROVISIONS) {
		R37_STAGE1_PROVISIONS = r37_STAGE1_PROVISIONS;
	}
	public BigDecimal getR37_QUALIFY_STAGE2_PROVISIONS() {
		return R37_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR37_QUALIFY_STAGE2_PROVISIONS(BigDecimal r37_QUALIFY_STAGE2_PROVISIONS) {
		R37_QUALIFY_STAGE2_PROVISIONS = r37_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR37_TOTAL_GENERAL_PROVISIONS() {
		return R37_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR37_TOTAL_GENERAL_PROVISIONS(BigDecimal r37_TOTAL_GENERAL_PROVISIONS) {
		R37_TOTAL_GENERAL_PROVISIONS = r37_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}
	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}
	public BigDecimal getR38_STAGE1_PROVISIONS() {
		return R38_STAGE1_PROVISIONS;
	}
	public void setR38_STAGE1_PROVISIONS(BigDecimal r38_STAGE1_PROVISIONS) {
		R38_STAGE1_PROVISIONS = r38_STAGE1_PROVISIONS;
	}
	public BigDecimal getR38_QUALIFY_STAGE2_PROVISIONS() {
		return R38_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR38_QUALIFY_STAGE2_PROVISIONS(BigDecimal r38_QUALIFY_STAGE2_PROVISIONS) {
		R38_QUALIFY_STAGE2_PROVISIONS = r38_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR38_TOTAL_GENERAL_PROVISIONS() {
		return R38_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR38_TOTAL_GENERAL_PROVISIONS(BigDecimal r38_TOTAL_GENERAL_PROVISIONS) {
		R38_TOTAL_GENERAL_PROVISIONS = r38_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}
	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}
	public BigDecimal getR39_STAGE1_PROVISIONS() {
		return R39_STAGE1_PROVISIONS;
	}
	public void setR39_STAGE1_PROVISIONS(BigDecimal r39_STAGE1_PROVISIONS) {
		R39_STAGE1_PROVISIONS = r39_STAGE1_PROVISIONS;
	}
	public BigDecimal getR39_QUALIFY_STAGE2_PROVISIONS() {
		return R39_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR39_QUALIFY_STAGE2_PROVISIONS(BigDecimal r39_QUALIFY_STAGE2_PROVISIONS) {
		R39_QUALIFY_STAGE2_PROVISIONS = r39_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR39_TOTAL_GENERAL_PROVISIONS() {
		return R39_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR39_TOTAL_GENERAL_PROVISIONS(BigDecimal r39_TOTAL_GENERAL_PROVISIONS) {
		R39_TOTAL_GENERAL_PROVISIONS = r39_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}
	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}
	public BigDecimal getR40_STAGE1_PROVISIONS() {
		return R40_STAGE1_PROVISIONS;
	}
	public void setR40_STAGE1_PROVISIONS(BigDecimal r40_STAGE1_PROVISIONS) {
		R40_STAGE1_PROVISIONS = r40_STAGE1_PROVISIONS;
	}
	public BigDecimal getR40_QUALIFY_STAGE2_PROVISIONS() {
		return R40_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR40_QUALIFY_STAGE2_PROVISIONS(BigDecimal r40_QUALIFY_STAGE2_PROVISIONS) {
		R40_QUALIFY_STAGE2_PROVISIONS = r40_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR40_TOTAL_GENERAL_PROVISIONS() {
		return R40_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR40_TOTAL_GENERAL_PROVISIONS(BigDecimal r40_TOTAL_GENERAL_PROVISIONS) {
		R40_TOTAL_GENERAL_PROVISIONS = r40_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}
	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}
	public BigDecimal getR41_STAGE1_PROVISIONS() {
		return R41_STAGE1_PROVISIONS;
	}
	public void setR41_STAGE1_PROVISIONS(BigDecimal r41_STAGE1_PROVISIONS) {
		R41_STAGE1_PROVISIONS = r41_STAGE1_PROVISIONS;
	}
	public BigDecimal getR41_QUALIFY_STAGE2_PROVISIONS() {
		return R41_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR41_QUALIFY_STAGE2_PROVISIONS(BigDecimal r41_QUALIFY_STAGE2_PROVISIONS) {
		R41_QUALIFY_STAGE2_PROVISIONS = r41_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR41_TOTAL_GENERAL_PROVISIONS() {
		return R41_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR41_TOTAL_GENERAL_PROVISIONS(BigDecimal r41_TOTAL_GENERAL_PROVISIONS) {
		R41_TOTAL_GENERAL_PROVISIONS = r41_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}
	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}
	public BigDecimal getR42_STAGE1_PROVISIONS() {
		return R42_STAGE1_PROVISIONS;
	}
	public void setR42_STAGE1_PROVISIONS(BigDecimal r42_STAGE1_PROVISIONS) {
		R42_STAGE1_PROVISIONS = r42_STAGE1_PROVISIONS;
	}
	public BigDecimal getR42_QUALIFY_STAGE2_PROVISIONS() {
		return R42_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR42_QUALIFY_STAGE2_PROVISIONS(BigDecimal r42_QUALIFY_STAGE2_PROVISIONS) {
		R42_QUALIFY_STAGE2_PROVISIONS = r42_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR42_TOTAL_GENERAL_PROVISIONS() {
		return R42_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR42_TOTAL_GENERAL_PROVISIONS(BigDecimal r42_TOTAL_GENERAL_PROVISIONS) {
		R42_TOTAL_GENERAL_PROVISIONS = r42_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}
	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}
	public BigDecimal getR43_STAGE1_PROVISIONS() {
		return R43_STAGE1_PROVISIONS;
	}
	public void setR43_STAGE1_PROVISIONS(BigDecimal r43_STAGE1_PROVISIONS) {
		R43_STAGE1_PROVISIONS = r43_STAGE1_PROVISIONS;
	}
	public BigDecimal getR43_QUALIFY_STAGE2_PROVISIONS() {
		return R43_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR43_QUALIFY_STAGE2_PROVISIONS(BigDecimal r43_QUALIFY_STAGE2_PROVISIONS) {
		R43_QUALIFY_STAGE2_PROVISIONS = r43_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR43_TOTAL_GENERAL_PROVISIONS() {
		return R43_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR43_TOTAL_GENERAL_PROVISIONS(BigDecimal r43_TOTAL_GENERAL_PROVISIONS) {
		R43_TOTAL_GENERAL_PROVISIONS = r43_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR44_PRODUCT() {
		return R44_PRODUCT;
	}
	public void setR44_PRODUCT(String r44_PRODUCT) {
		R44_PRODUCT = r44_PRODUCT;
	}
	public BigDecimal getR44_STAGE1_PROVISIONS() {
		return R44_STAGE1_PROVISIONS;
	}
	public void setR44_STAGE1_PROVISIONS(BigDecimal r44_STAGE1_PROVISIONS) {
		R44_STAGE1_PROVISIONS = r44_STAGE1_PROVISIONS;
	}
	public BigDecimal getR44_QUALIFY_STAGE2_PROVISIONS() {
		return R44_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR44_QUALIFY_STAGE2_PROVISIONS(BigDecimal r44_QUALIFY_STAGE2_PROVISIONS) {
		R44_QUALIFY_STAGE2_PROVISIONS = r44_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR44_TOTAL_GENERAL_PROVISIONS() {
		return R44_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR44_TOTAL_GENERAL_PROVISIONS(BigDecimal r44_TOTAL_GENERAL_PROVISIONS) {
		R44_TOTAL_GENERAL_PROVISIONS = r44_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR45_PRODUCT() {
		return R45_PRODUCT;
	}
	public void setR45_PRODUCT(String r45_PRODUCT) {
		R45_PRODUCT = r45_PRODUCT;
	}
	public BigDecimal getR45_STAGE1_PROVISIONS() {
		return R45_STAGE1_PROVISIONS;
	}
	public void setR45_STAGE1_PROVISIONS(BigDecimal r45_STAGE1_PROVISIONS) {
		R45_STAGE1_PROVISIONS = r45_STAGE1_PROVISIONS;
	}
	public BigDecimal getR45_QUALIFY_STAGE2_PROVISIONS() {
		return R45_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR45_QUALIFY_STAGE2_PROVISIONS(BigDecimal r45_QUALIFY_STAGE2_PROVISIONS) {
		R45_QUALIFY_STAGE2_PROVISIONS = r45_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR45_TOTAL_GENERAL_PROVISIONS() {
		return R45_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR45_TOTAL_GENERAL_PROVISIONS(BigDecimal r45_TOTAL_GENERAL_PROVISIONS) {
		R45_TOTAL_GENERAL_PROVISIONS = r45_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR46_PRODUCT() {
		return R46_PRODUCT;
	}
	public void setR46_PRODUCT(String r46_PRODUCT) {
		R46_PRODUCT = r46_PRODUCT;
	}
	public BigDecimal getR46_STAGE1_PROVISIONS() {
		return R46_STAGE1_PROVISIONS;
	}
	public void setR46_STAGE1_PROVISIONS(BigDecimal r46_STAGE1_PROVISIONS) {
		R46_STAGE1_PROVISIONS = r46_STAGE1_PROVISIONS;
	}
	public BigDecimal getR46_QUALIFY_STAGE2_PROVISIONS() {
		return R46_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR46_QUALIFY_STAGE2_PROVISIONS(BigDecimal r46_QUALIFY_STAGE2_PROVISIONS) {
		R46_QUALIFY_STAGE2_PROVISIONS = r46_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR46_TOTAL_GENERAL_PROVISIONS() {
		return R46_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR46_TOTAL_GENERAL_PROVISIONS(BigDecimal r46_TOTAL_GENERAL_PROVISIONS) {
		R46_TOTAL_GENERAL_PROVISIONS = r46_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}
	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}
	public BigDecimal getR47_STAGE1_PROVISIONS() {
		return R47_STAGE1_PROVISIONS;
	}
	public void setR47_STAGE1_PROVISIONS(BigDecimal r47_STAGE1_PROVISIONS) {
		R47_STAGE1_PROVISIONS = r47_STAGE1_PROVISIONS;
	}
	public BigDecimal getR47_QUALIFY_STAGE2_PROVISIONS() {
		return R47_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR47_QUALIFY_STAGE2_PROVISIONS(BigDecimal r47_QUALIFY_STAGE2_PROVISIONS) {
		R47_QUALIFY_STAGE2_PROVISIONS = r47_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR47_TOTAL_GENERAL_PROVISIONS() {
		return R47_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR47_TOTAL_GENERAL_PROVISIONS(BigDecimal r47_TOTAL_GENERAL_PROVISIONS) {
		R47_TOTAL_GENERAL_PROVISIONS = r47_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR48_PRODUCT() {
		return R48_PRODUCT;
	}
	public void setR48_PRODUCT(String r48_PRODUCT) {
		R48_PRODUCT = r48_PRODUCT;
	}
	public BigDecimal getR48_STAGE1_PROVISIONS() {
		return R48_STAGE1_PROVISIONS;
	}
	public void setR48_STAGE1_PROVISIONS(BigDecimal r48_STAGE1_PROVISIONS) {
		R48_STAGE1_PROVISIONS = r48_STAGE1_PROVISIONS;
	}
	public BigDecimal getR48_QUALIFY_STAGE2_PROVISIONS() {
		return R48_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR48_QUALIFY_STAGE2_PROVISIONS(BigDecimal r48_QUALIFY_STAGE2_PROVISIONS) {
		R48_QUALIFY_STAGE2_PROVISIONS = r48_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR48_TOTAL_GENERAL_PROVISIONS() {
		return R48_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR48_TOTAL_GENERAL_PROVISIONS(BigDecimal r48_TOTAL_GENERAL_PROVISIONS) {
		R48_TOTAL_GENERAL_PROVISIONS = r48_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR49_PRODUCT() {
		return R49_PRODUCT;
	}
	public void setR49_PRODUCT(String r49_PRODUCT) {
		R49_PRODUCT = r49_PRODUCT;
	}
	public BigDecimal getR49_STAGE1_PROVISIONS() {
		return R49_STAGE1_PROVISIONS;
	}
	public void setR49_STAGE1_PROVISIONS(BigDecimal r49_STAGE1_PROVISIONS) {
		R49_STAGE1_PROVISIONS = r49_STAGE1_PROVISIONS;
	}
	public BigDecimal getR49_QUALIFY_STAGE2_PROVISIONS() {
		return R49_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR49_QUALIFY_STAGE2_PROVISIONS(BigDecimal r49_QUALIFY_STAGE2_PROVISIONS) {
		R49_QUALIFY_STAGE2_PROVISIONS = r49_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR49_TOTAL_GENERAL_PROVISIONS() {
		return R49_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR49_TOTAL_GENERAL_PROVISIONS(BigDecimal r49_TOTAL_GENERAL_PROVISIONS) {
		R49_TOTAL_GENERAL_PROVISIONS = r49_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR50_PRODUCT() {
		return R50_PRODUCT;
	}
	public void setR50_PRODUCT(String r50_PRODUCT) {
		R50_PRODUCT = r50_PRODUCT;
	}
	public BigDecimal getR50_STAGE1_PROVISIONS() {
		return R50_STAGE1_PROVISIONS;
	}
	public void setR50_STAGE1_PROVISIONS(BigDecimal r50_STAGE1_PROVISIONS) {
		R50_STAGE1_PROVISIONS = r50_STAGE1_PROVISIONS;
	}
	public BigDecimal getR50_QUALIFY_STAGE2_PROVISIONS() {
		return R50_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR50_QUALIFY_STAGE2_PROVISIONS(BigDecimal r50_QUALIFY_STAGE2_PROVISIONS) {
		R50_QUALIFY_STAGE2_PROVISIONS = r50_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR50_TOTAL_GENERAL_PROVISIONS() {
		return R50_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR50_TOTAL_GENERAL_PROVISIONS(BigDecimal r50_TOTAL_GENERAL_PROVISIONS) {
		R50_TOTAL_GENERAL_PROVISIONS = r50_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR51_PRODUCT() {
		return R51_PRODUCT;
	}
	public void setR51_PRODUCT(String r51_PRODUCT) {
		R51_PRODUCT = r51_PRODUCT;
	}
	public BigDecimal getR51_STAGE1_PROVISIONS() {
		return R51_STAGE1_PROVISIONS;
	}
	public void setR51_STAGE1_PROVISIONS(BigDecimal r51_STAGE1_PROVISIONS) {
		R51_STAGE1_PROVISIONS = r51_STAGE1_PROVISIONS;
	}
	public BigDecimal getR51_QUALIFY_STAGE2_PROVISIONS() {
		return R51_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR51_QUALIFY_STAGE2_PROVISIONS(BigDecimal r51_QUALIFY_STAGE2_PROVISIONS) {
		R51_QUALIFY_STAGE2_PROVISIONS = r51_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR51_TOTAL_GENERAL_PROVISIONS() {
		return R51_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR51_TOTAL_GENERAL_PROVISIONS(BigDecimal r51_TOTAL_GENERAL_PROVISIONS) {
		R51_TOTAL_GENERAL_PROVISIONS = r51_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR52_PRODUCT() {
		return R52_PRODUCT;
	}
	public void setR52_PRODUCT(String r52_PRODUCT) {
		R52_PRODUCT = r52_PRODUCT;
	}
	public BigDecimal getR52_STAGE1_PROVISIONS() {
		return R52_STAGE1_PROVISIONS;
	}
	public void setR52_STAGE1_PROVISIONS(BigDecimal r52_STAGE1_PROVISIONS) {
		R52_STAGE1_PROVISIONS = r52_STAGE1_PROVISIONS;
	}
	public BigDecimal getR52_QUALIFY_STAGE2_PROVISIONS() {
		return R52_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR52_QUALIFY_STAGE2_PROVISIONS(BigDecimal r52_QUALIFY_STAGE2_PROVISIONS) {
		R52_QUALIFY_STAGE2_PROVISIONS = r52_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR52_TOTAL_GENERAL_PROVISIONS() {
		return R52_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR52_TOTAL_GENERAL_PROVISIONS(BigDecimal r52_TOTAL_GENERAL_PROVISIONS) {
		R52_TOTAL_GENERAL_PROVISIONS = r52_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR53_PRODUCT() {
		return R53_PRODUCT;
	}
	public void setR53_PRODUCT(String r53_PRODUCT) {
		R53_PRODUCT = r53_PRODUCT;
	}
	public BigDecimal getR53_STAGE1_PROVISIONS() {
		return R53_STAGE1_PROVISIONS;
	}
	public void setR53_STAGE1_PROVISIONS(BigDecimal r53_STAGE1_PROVISIONS) {
		R53_STAGE1_PROVISIONS = r53_STAGE1_PROVISIONS;
	}
	public BigDecimal getR53_QUALIFY_STAGE2_PROVISIONS() {
		return R53_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR53_QUALIFY_STAGE2_PROVISIONS(BigDecimal r53_QUALIFY_STAGE2_PROVISIONS) {
		R53_QUALIFY_STAGE2_PROVISIONS = r53_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR53_TOTAL_GENERAL_PROVISIONS() {
		return R53_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR53_TOTAL_GENERAL_PROVISIONS(BigDecimal r53_TOTAL_GENERAL_PROVISIONS) {
		R53_TOTAL_GENERAL_PROVISIONS = r53_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR54_PRODUCT() {
		return R54_PRODUCT;
	}
	public void setR54_PRODUCT(String r54_PRODUCT) {
		R54_PRODUCT = r54_PRODUCT;
	}
	public BigDecimal getR54_STAGE1_PROVISIONS() {
		return R54_STAGE1_PROVISIONS;
	}
	public void setR54_STAGE1_PROVISIONS(BigDecimal r54_STAGE1_PROVISIONS) {
		R54_STAGE1_PROVISIONS = r54_STAGE1_PROVISIONS;
	}
	public BigDecimal getR54_QUALIFY_STAGE2_PROVISIONS() {
		return R54_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR54_QUALIFY_STAGE2_PROVISIONS(BigDecimal r54_QUALIFY_STAGE2_PROVISIONS) {
		R54_QUALIFY_STAGE2_PROVISIONS = r54_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR54_TOTAL_GENERAL_PROVISIONS() {
		return R54_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR54_TOTAL_GENERAL_PROVISIONS(BigDecimal r54_TOTAL_GENERAL_PROVISIONS) {
		R54_TOTAL_GENERAL_PROVISIONS = r54_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR55_PRODUCT() {
		return R55_PRODUCT;
	}
	public void setR55_PRODUCT(String r55_PRODUCT) {
		R55_PRODUCT = r55_PRODUCT;
	}
	public BigDecimal getR55_STAGE1_PROVISIONS() {
		return R55_STAGE1_PROVISIONS;
	}
	public void setR55_STAGE1_PROVISIONS(BigDecimal r55_STAGE1_PROVISIONS) {
		R55_STAGE1_PROVISIONS = r55_STAGE1_PROVISIONS;
	}
	public BigDecimal getR55_QUALIFY_STAGE2_PROVISIONS() {
		return R55_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR55_QUALIFY_STAGE2_PROVISIONS(BigDecimal r55_QUALIFY_STAGE2_PROVISIONS) {
		R55_QUALIFY_STAGE2_PROVISIONS = r55_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR55_TOTAL_GENERAL_PROVISIONS() {
		return R55_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR55_TOTAL_GENERAL_PROVISIONS(BigDecimal r55_TOTAL_GENERAL_PROVISIONS) {
		R55_TOTAL_GENERAL_PROVISIONS = r55_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR56_PRODUCT() {
		return R56_PRODUCT;
	}
	public void setR56_PRODUCT(String r56_PRODUCT) {
		R56_PRODUCT = r56_PRODUCT;
	}
	public BigDecimal getR56_STAGE1_PROVISIONS() {
		return R56_STAGE1_PROVISIONS;
	}
	public void setR56_STAGE1_PROVISIONS(BigDecimal r56_STAGE1_PROVISIONS) {
		R56_STAGE1_PROVISIONS = r56_STAGE1_PROVISIONS;
	}
	public BigDecimal getR56_QUALIFY_STAGE2_PROVISIONS() {
		return R56_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR56_QUALIFY_STAGE2_PROVISIONS(BigDecimal r56_QUALIFY_STAGE2_PROVISIONS) {
		R56_QUALIFY_STAGE2_PROVISIONS = r56_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR56_TOTAL_GENERAL_PROVISIONS() {
		return R56_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR56_TOTAL_GENERAL_PROVISIONS(BigDecimal r56_TOTAL_GENERAL_PROVISIONS) {
		R56_TOTAL_GENERAL_PROVISIONS = r56_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR57_PRODUCT() {
		return R57_PRODUCT;
	}
	public void setR57_PRODUCT(String r57_PRODUCT) {
		R57_PRODUCT = r57_PRODUCT;
	}
	public BigDecimal getR57_STAGE1_PROVISIONS() {
		return R57_STAGE1_PROVISIONS;
	}
	public void setR57_STAGE1_PROVISIONS(BigDecimal r57_STAGE1_PROVISIONS) {
		R57_STAGE1_PROVISIONS = r57_STAGE1_PROVISIONS;
	}
	public BigDecimal getR57_QUALIFY_STAGE2_PROVISIONS() {
		return R57_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR57_QUALIFY_STAGE2_PROVISIONS(BigDecimal r57_QUALIFY_STAGE2_PROVISIONS) {
		R57_QUALIFY_STAGE2_PROVISIONS = r57_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR57_TOTAL_GENERAL_PROVISIONS() {
		return R57_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR57_TOTAL_GENERAL_PROVISIONS(BigDecimal r57_TOTAL_GENERAL_PROVISIONS) {
		R57_TOTAL_GENERAL_PROVISIONS = r57_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR58_PRODUCT() {
		return R58_PRODUCT;
	}
	public void setR58_PRODUCT(String r58_PRODUCT) {
		R58_PRODUCT = r58_PRODUCT;
	}
	public BigDecimal getR58_STAGE1_PROVISIONS() {
		return R58_STAGE1_PROVISIONS;
	}
	public void setR58_STAGE1_PROVISIONS(BigDecimal r58_STAGE1_PROVISIONS) {
		R58_STAGE1_PROVISIONS = r58_STAGE1_PROVISIONS;
	}
	public BigDecimal getR58_QUALIFY_STAGE2_PROVISIONS() {
		return R58_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR58_QUALIFY_STAGE2_PROVISIONS(BigDecimal r58_QUALIFY_STAGE2_PROVISIONS) {
		R58_QUALIFY_STAGE2_PROVISIONS = r58_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR58_TOTAL_GENERAL_PROVISIONS() {
		return R58_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR58_TOTAL_GENERAL_PROVISIONS(BigDecimal r58_TOTAL_GENERAL_PROVISIONS) {
		R58_TOTAL_GENERAL_PROVISIONS = r58_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR59_PRODUCT() {
		return R59_PRODUCT;
	}
	public void setR59_PRODUCT(String r59_PRODUCT) {
		R59_PRODUCT = r59_PRODUCT;
	}
	public BigDecimal getR59_STAGE1_PROVISIONS() {
		return R59_STAGE1_PROVISIONS;
	}
	public void setR59_STAGE1_PROVISIONS(BigDecimal r59_STAGE1_PROVISIONS) {
		R59_STAGE1_PROVISIONS = r59_STAGE1_PROVISIONS;
	}
	public BigDecimal getR59_QUALIFY_STAGE2_PROVISIONS() {
		return R59_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR59_QUALIFY_STAGE2_PROVISIONS(BigDecimal r59_QUALIFY_STAGE2_PROVISIONS) {
		R59_QUALIFY_STAGE2_PROVISIONS = r59_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR59_TOTAL_GENERAL_PROVISIONS() {
		return R59_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR59_TOTAL_GENERAL_PROVISIONS(BigDecimal r59_TOTAL_GENERAL_PROVISIONS) {
		R59_TOTAL_GENERAL_PROVISIONS = r59_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR60_PRODUCT() {
		return R60_PRODUCT;
	}
	public void setR60_PRODUCT(String r60_PRODUCT) {
		R60_PRODUCT = r60_PRODUCT;
	}
	public BigDecimal getR60_STAGE1_PROVISIONS() {
		return R60_STAGE1_PROVISIONS;
	}
	public void setR60_STAGE1_PROVISIONS(BigDecimal r60_STAGE1_PROVISIONS) {
		R60_STAGE1_PROVISIONS = r60_STAGE1_PROVISIONS;
	}
	public BigDecimal getR60_QUALIFY_STAGE2_PROVISIONS() {
		return R60_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR60_QUALIFY_STAGE2_PROVISIONS(BigDecimal r60_QUALIFY_STAGE2_PROVISIONS) {
		R60_QUALIFY_STAGE2_PROVISIONS = r60_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR60_TOTAL_GENERAL_PROVISIONS() {
		return R60_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR60_TOTAL_GENERAL_PROVISIONS(BigDecimal r60_TOTAL_GENERAL_PROVISIONS) {
		R60_TOTAL_GENERAL_PROVISIONS = r60_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR61_PRODUCT() {
		return R61_PRODUCT;
	}
	public void setR61_PRODUCT(String r61_PRODUCT) {
		R61_PRODUCT = r61_PRODUCT;
	}
	public BigDecimal getR61_STAGE1_PROVISIONS() {
		return R61_STAGE1_PROVISIONS;
	}
	public void setR61_STAGE1_PROVISIONS(BigDecimal r61_STAGE1_PROVISIONS) {
		R61_STAGE1_PROVISIONS = r61_STAGE1_PROVISIONS;
	}
	public BigDecimal getR61_QUALIFY_STAGE2_PROVISIONS() {
		return R61_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR61_QUALIFY_STAGE2_PROVISIONS(BigDecimal r61_QUALIFY_STAGE2_PROVISIONS) {
		R61_QUALIFY_STAGE2_PROVISIONS = r61_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR61_TOTAL_GENERAL_PROVISIONS() {
		return R61_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR61_TOTAL_GENERAL_PROVISIONS(BigDecimal r61_TOTAL_GENERAL_PROVISIONS) {
		R61_TOTAL_GENERAL_PROVISIONS = r61_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR62_PRODUCT() {
		return R62_PRODUCT;
	}
	public void setR62_PRODUCT(String r62_PRODUCT) {
		R62_PRODUCT = r62_PRODUCT;
	}
	public BigDecimal getR62_STAGE1_PROVISIONS() {
		return R62_STAGE1_PROVISIONS;
	}
	public void setR62_STAGE1_PROVISIONS(BigDecimal r62_STAGE1_PROVISIONS) {
		R62_STAGE1_PROVISIONS = r62_STAGE1_PROVISIONS;
	}
	public BigDecimal getR62_QUALIFY_STAGE2_PROVISIONS() {
		return R62_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR62_QUALIFY_STAGE2_PROVISIONS(BigDecimal r62_QUALIFY_STAGE2_PROVISIONS) {
		R62_QUALIFY_STAGE2_PROVISIONS = r62_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR62_TOTAL_GENERAL_PROVISIONS() {
		return R62_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR62_TOTAL_GENERAL_PROVISIONS(BigDecimal r62_TOTAL_GENERAL_PROVISIONS) {
		R62_TOTAL_GENERAL_PROVISIONS = r62_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR63_PRODUCT() {
		return R63_PRODUCT;
	}
	public void setR63_PRODUCT(String r63_PRODUCT) {
		R63_PRODUCT = r63_PRODUCT;
	}
	public BigDecimal getR63_STAGE1_PROVISIONS() {
		return R63_STAGE1_PROVISIONS;
	}
	public void setR63_STAGE1_PROVISIONS(BigDecimal r63_STAGE1_PROVISIONS) {
		R63_STAGE1_PROVISIONS = r63_STAGE1_PROVISIONS;
	}
	public BigDecimal getR63_QUALIFY_STAGE2_PROVISIONS() {
		return R63_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR63_QUALIFY_STAGE2_PROVISIONS(BigDecimal r63_QUALIFY_STAGE2_PROVISIONS) {
		R63_QUALIFY_STAGE2_PROVISIONS = r63_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR63_TOTAL_GENERAL_PROVISIONS() {
		return R63_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR63_TOTAL_GENERAL_PROVISIONS(BigDecimal r63_TOTAL_GENERAL_PROVISIONS) {
		R63_TOTAL_GENERAL_PROVISIONS = r63_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR64_PRODUCT() {
		return R64_PRODUCT;
	}
	public void setR64_PRODUCT(String r64_PRODUCT) {
		R64_PRODUCT = r64_PRODUCT;
	}
	public BigDecimal getR64_STAGE1_PROVISIONS() {
		return R64_STAGE1_PROVISIONS;
	}
	public void setR64_STAGE1_PROVISIONS(BigDecimal r64_STAGE1_PROVISIONS) {
		R64_STAGE1_PROVISIONS = r64_STAGE1_PROVISIONS;
	}
	public BigDecimal getR64_QUALIFY_STAGE2_PROVISIONS() {
		return R64_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR64_QUALIFY_STAGE2_PROVISIONS(BigDecimal r64_QUALIFY_STAGE2_PROVISIONS) {
		R64_QUALIFY_STAGE2_PROVISIONS = r64_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR64_TOTAL_GENERAL_PROVISIONS() {
		return R64_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR64_TOTAL_GENERAL_PROVISIONS(BigDecimal r64_TOTAL_GENERAL_PROVISIONS) {
		R64_TOTAL_GENERAL_PROVISIONS = r64_TOTAL_GENERAL_PROVISIONS;
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
// RESUB summary M_GP
//=====================================================


public class M_GP_RESUB_Summary_RowMapper 
        implements RowMapper<M_GP_RESUB_Summary_Entity> {

    @Override
    public M_GP_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_GP_RESUB_Summary_Entity obj = new M_GP_RESUB_Summary_Entity();

// =========================
// R11
// =========================
obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
obj.setR11_STAGE1_PROVISIONS(rs.getBigDecimal("R11_STAGE1_PROVISIONS"));
obj.setR11_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R11_QUALIFY_STAGE2_PROVISIONS"));
obj.setR11_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R11_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R12
// =========================
obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
obj.setR12_STAGE1_PROVISIONS(rs.getBigDecimal("R12_STAGE1_PROVISIONS"));
obj.setR12_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R12_QUALIFY_STAGE2_PROVISIONS"));
obj.setR12_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R12_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R13
// =========================
obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
obj.setR13_STAGE1_PROVISIONS(rs.getBigDecimal("R13_STAGE1_PROVISIONS"));
obj.setR13_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R13_QUALIFY_STAGE2_PROVISIONS"));
obj.setR13_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R13_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R14
// =========================
obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
obj.setR14_STAGE1_PROVISIONS(rs.getBigDecimal("R14_STAGE1_PROVISIONS"));
obj.setR14_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R14_QUALIFY_STAGE2_PROVISIONS"));
obj.setR14_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R14_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R15
// =========================
obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
obj.setR15_STAGE1_PROVISIONS(rs.getBigDecimal("R15_STAGE1_PROVISIONS"));
obj.setR15_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R15_QUALIFY_STAGE2_PROVISIONS"));
obj.setR15_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R15_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R16
// =========================
obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
obj.setR16_STAGE1_PROVISIONS(rs.getBigDecimal("R16_STAGE1_PROVISIONS"));
obj.setR16_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R16_QUALIFY_STAGE2_PROVISIONS"));
obj.setR16_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R16_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R17
// =========================
obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
obj.setR17_STAGE1_PROVISIONS(rs.getBigDecimal("R17_STAGE1_PROVISIONS"));
obj.setR17_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R17_QUALIFY_STAGE2_PROVISIONS"));
obj.setR17_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R17_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R18
// =========================
obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
obj.setR18_STAGE1_PROVISIONS(rs.getBigDecimal("R18_STAGE1_PROVISIONS"));
obj.setR18_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R18_QUALIFY_STAGE2_PROVISIONS"));
obj.setR18_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R18_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R19
// =========================
obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
obj.setR19_STAGE1_PROVISIONS(rs.getBigDecimal("R19_STAGE1_PROVISIONS"));
obj.setR19_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R19_QUALIFY_STAGE2_PROVISIONS"));
obj.setR19_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R19_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R20
// =========================
obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
obj.setR20_STAGE1_PROVISIONS(rs.getBigDecimal("R20_STAGE1_PROVISIONS"));
obj.setR20_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R20_QUALIFY_STAGE2_PROVISIONS"));
obj.setR20_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R20_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R21
// =========================
obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
obj.setR21_STAGE1_PROVISIONS(rs.getBigDecimal("R21_STAGE1_PROVISIONS"));
obj.setR21_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R21_QUALIFY_STAGE2_PROVISIONS"));
obj.setR21_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R21_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R22
// =========================
obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
obj.setR22_STAGE1_PROVISIONS(rs.getBigDecimal("R22_STAGE1_PROVISIONS"));
obj.setR22_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R22_QUALIFY_STAGE2_PROVISIONS"));
obj.setR22_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R22_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R23
// =========================
obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
obj.setR23_STAGE1_PROVISIONS(rs.getBigDecimal("R23_STAGE1_PROVISIONS"));
obj.setR23_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R23_QUALIFY_STAGE2_PROVISIONS"));
obj.setR23_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R23_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R24
// =========================
obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
obj.setR24_STAGE1_PROVISIONS(rs.getBigDecimal("R24_STAGE1_PROVISIONS"));
obj.setR24_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R24_QUALIFY_STAGE2_PROVISIONS"));
obj.setR24_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R24_TOTAL_GENERAL_PROVISIONS"));


// =========================
// R25
// =========================
obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
obj.setR25_STAGE1_PROVISIONS(rs.getBigDecimal("R25_STAGE1_PROVISIONS"));
obj.setR25_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R25_QUALIFY_STAGE2_PROVISIONS"));
obj.setR25_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R25_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R26
// =========================
obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
obj.setR26_STAGE1_PROVISIONS(rs.getBigDecimal("R26_STAGE1_PROVISIONS"));
obj.setR26_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R26_QUALIFY_STAGE2_PROVISIONS"));
obj.setR26_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R26_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R27
// =========================
obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
obj.setR27_STAGE1_PROVISIONS(rs.getBigDecimal("R27_STAGE1_PROVISIONS"));
obj.setR27_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R27_QUALIFY_STAGE2_PROVISIONS"));
obj.setR27_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R27_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R28
// =========================
obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
obj.setR28_STAGE1_PROVISIONS(rs.getBigDecimal("R28_STAGE1_PROVISIONS"));
obj.setR28_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R28_QUALIFY_STAGE2_PROVISIONS"));
obj.setR28_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R28_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R29
// =========================
obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
obj.setR29_STAGE1_PROVISIONS(rs.getBigDecimal("R29_STAGE1_PROVISIONS"));
obj.setR29_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R29_QUALIFY_STAGE2_PROVISIONS"));
obj.setR29_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R29_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R30
// =========================
obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
obj.setR30_STAGE1_PROVISIONS(rs.getBigDecimal("R30_STAGE1_PROVISIONS"));
obj.setR30_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R30_QUALIFY_STAGE2_PROVISIONS"));
obj.setR30_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R30_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R31
// =========================
obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
obj.setR31_STAGE1_PROVISIONS(rs.getBigDecimal("R31_STAGE1_PROVISIONS"));
obj.setR31_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R31_QUALIFY_STAGE2_PROVISIONS"));
obj.setR31_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R31_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R32
// =========================
obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
obj.setR32_STAGE1_PROVISIONS(rs.getBigDecimal("R32_STAGE1_PROVISIONS"));
obj.setR32_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R32_QUALIFY_STAGE2_PROVISIONS"));
obj.setR32_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R32_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R33
// =========================
obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
obj.setR33_STAGE1_PROVISIONS(rs.getBigDecimal("R33_STAGE1_PROVISIONS"));
obj.setR33_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R33_QUALIFY_STAGE2_PROVISIONS"));
obj.setR33_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R33_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R34
// =========================
obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
obj.setR34_STAGE1_PROVISIONS(rs.getBigDecimal("R34_STAGE1_PROVISIONS"));
obj.setR34_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R34_QUALIFY_STAGE2_PROVISIONS"));
obj.setR34_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R34_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R35
// =========================
obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
obj.setR35_STAGE1_PROVISIONS(rs.getBigDecimal("R35_STAGE1_PROVISIONS"));
obj.setR35_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R35_QUALIFY_STAGE2_PROVISIONS"));
obj.setR35_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R35_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R36
// =========================
obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
obj.setR36_STAGE1_PROVISIONS(rs.getBigDecimal("R36_STAGE1_PROVISIONS"));
obj.setR36_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R36_QUALIFY_STAGE2_PROVISIONS"));
obj.setR36_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R36_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R37
// =========================
obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
obj.setR37_STAGE1_PROVISIONS(rs.getBigDecimal("R37_STAGE1_PROVISIONS"));
obj.setR37_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R37_QUALIFY_STAGE2_PROVISIONS"));
obj.setR37_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R37_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R38
// =========================
obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
obj.setR38_STAGE1_PROVISIONS(rs.getBigDecimal("R38_STAGE1_PROVISIONS"));
obj.setR38_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R38_QUALIFY_STAGE2_PROVISIONS"));
obj.setR38_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R38_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R39
// =========================
obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
obj.setR39_STAGE1_PROVISIONS(rs.getBigDecimal("R39_STAGE1_PROVISIONS"));
obj.setR39_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R39_QUALIFY_STAGE2_PROVISIONS"));
obj.setR39_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R39_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R40
// =========================
obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
obj.setR40_STAGE1_PROVISIONS(rs.getBigDecimal("R40_STAGE1_PROVISIONS"));
obj.setR40_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R40_QUALIFY_STAGE2_PROVISIONS"));
obj.setR40_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R40_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R41
// =========================
obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
obj.setR41_STAGE1_PROVISIONS(rs.getBigDecimal("R41_STAGE1_PROVISIONS"));
obj.setR41_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R41_QUALIFY_STAGE2_PROVISIONS"));
obj.setR41_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R41_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R42
// =========================
obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
obj.setR42_STAGE1_PROVISIONS(rs.getBigDecimal("R42_STAGE1_PROVISIONS"));
obj.setR42_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R42_QUALIFY_STAGE2_PROVISIONS"));
obj.setR42_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R42_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R43
// =========================
obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
obj.setR43_STAGE1_PROVISIONS(rs.getBigDecimal("R43_STAGE1_PROVISIONS"));
obj.setR43_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R43_QUALIFY_STAGE2_PROVISIONS"));
obj.setR43_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R43_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R44
// =========================
obj.setR44_PRODUCT(rs.getString("R44_PRODUCT"));
obj.setR44_STAGE1_PROVISIONS(rs.getBigDecimal("R44_STAGE1_PROVISIONS"));
obj.setR44_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R44_QUALIFY_STAGE2_PROVISIONS"));
obj.setR44_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R44_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R45
// =========================
obj.setR45_PRODUCT(rs.getString("R45_PRODUCT"));
obj.setR45_STAGE1_PROVISIONS(rs.getBigDecimal("R45_STAGE1_PROVISIONS"));
obj.setR45_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R45_QUALIFY_STAGE2_PROVISIONS"));
obj.setR45_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R45_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R46
// =========================
obj.setR46_PRODUCT(rs.getString("R46_PRODUCT"));
obj.setR46_STAGE1_PROVISIONS(rs.getBigDecimal("R46_STAGE1_PROVISIONS"));
obj.setR46_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R46_QUALIFY_STAGE2_PROVISIONS"));
obj.setR46_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R46_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R47
// =========================
obj.setR47_PRODUCT(rs.getString("R47_PRODUCT"));
obj.setR47_STAGE1_PROVISIONS(rs.getBigDecimal("R47_STAGE1_PROVISIONS"));
obj.setR47_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R47_QUALIFY_STAGE2_PROVISIONS"));
obj.setR47_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R47_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R48
// =========================
obj.setR48_PRODUCT(rs.getString("R48_PRODUCT"));
obj.setR48_STAGE1_PROVISIONS(rs.getBigDecimal("R48_STAGE1_PROVISIONS"));
obj.setR48_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R48_QUALIFY_STAGE2_PROVISIONS"));
obj.setR48_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R48_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R49
// =========================
obj.setR49_PRODUCT(rs.getString("R49_PRODUCT"));
obj.setR49_STAGE1_PROVISIONS(rs.getBigDecimal("R49_STAGE1_PROVISIONS"));
obj.setR49_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R49_QUALIFY_STAGE2_PROVISIONS"));
obj.setR49_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R49_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R50
// =========================
obj.setR50_PRODUCT(rs.getString("R50_PRODUCT"));
obj.setR50_STAGE1_PROVISIONS(rs.getBigDecimal("R50_STAGE1_PROVISIONS"));
obj.setR50_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R50_QUALIFY_STAGE2_PROVISIONS"));
obj.setR50_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R50_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R51
// =========================
obj.setR51_PRODUCT(rs.getString("R51_PRODUCT"));
obj.setR51_STAGE1_PROVISIONS(rs.getBigDecimal("R51_STAGE1_PROVISIONS"));
obj.setR51_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R51_QUALIFY_STAGE2_PROVISIONS"));
obj.setR51_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R51_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R52
// =========================
obj.setR52_PRODUCT(rs.getString("R52_PRODUCT"));
obj.setR52_STAGE1_PROVISIONS(rs.getBigDecimal("R52_STAGE1_PROVISIONS"));
obj.setR52_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R52_QUALIFY_STAGE2_PROVISIONS"));
obj.setR52_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R52_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R53
// =========================
obj.setR53_PRODUCT(rs.getString("R53_PRODUCT"));
obj.setR53_STAGE1_PROVISIONS(rs.getBigDecimal("R53_STAGE1_PROVISIONS"));
obj.setR53_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R53_QUALIFY_STAGE2_PROVISIONS"));
obj.setR53_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R53_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R54
// =========================
obj.setR54_PRODUCT(rs.getString("R54_PRODUCT"));
obj.setR54_STAGE1_PROVISIONS(rs.getBigDecimal("R54_STAGE1_PROVISIONS"));
obj.setR54_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R54_QUALIFY_STAGE2_PROVISIONS"));
obj.setR54_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R54_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R55
// =========================
obj.setR55_PRODUCT(rs.getString("R55_PRODUCT"));
obj.setR55_STAGE1_PROVISIONS(rs.getBigDecimal("R55_STAGE1_PROVISIONS"));
obj.setR55_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R55_QUALIFY_STAGE2_PROVISIONS"));
obj.setR55_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R55_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R56
// =========================
obj.setR56_PRODUCT(rs.getString("R56_PRODUCT"));
obj.setR56_STAGE1_PROVISIONS(rs.getBigDecimal("R56_STAGE1_PROVISIONS"));
obj.setR56_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R56_QUALIFY_STAGE2_PROVISIONS"));
obj.setR56_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R56_TOTAL_GENERAL_PROVISIONS"));


// =========================
// R57
// =========================
obj.setR57_PRODUCT(rs.getString("R57_PRODUCT"));
obj.setR57_STAGE1_PROVISIONS(rs.getBigDecimal("R57_STAGE1_PROVISIONS"));
obj.setR57_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R57_QUALIFY_STAGE2_PROVISIONS"));
obj.setR57_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R57_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R58
// =========================
obj.setR58_PRODUCT(rs.getString("R58_PRODUCT"));
obj.setR58_STAGE1_PROVISIONS(rs.getBigDecimal("R58_STAGE1_PROVISIONS"));
obj.setR58_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R58_QUALIFY_STAGE2_PROVISIONS"));
obj.setR58_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R58_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R59
// =========================
obj.setR59_PRODUCT(rs.getString("R59_PRODUCT"));
obj.setR59_STAGE1_PROVISIONS(rs.getBigDecimal("R59_STAGE1_PROVISIONS"));
obj.setR59_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R59_QUALIFY_STAGE2_PROVISIONS"));
obj.setR59_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R59_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R60
// =========================
obj.setR60_PRODUCT(rs.getString("R60_PRODUCT"));
obj.setR60_STAGE1_PROVISIONS(rs.getBigDecimal("R60_STAGE1_PROVISIONS"));
obj.setR60_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R60_QUALIFY_STAGE2_PROVISIONS"));
obj.setR60_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R60_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R61
// =========================
obj.setR61_PRODUCT(rs.getString("R61_PRODUCT"));
obj.setR61_STAGE1_PROVISIONS(rs.getBigDecimal("R61_STAGE1_PROVISIONS"));
obj.setR61_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R61_QUALIFY_STAGE2_PROVISIONS"));
obj.setR61_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R61_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R62
// =========================
obj.setR62_PRODUCT(rs.getString("R62_PRODUCT"));
obj.setR62_STAGE1_PROVISIONS(rs.getBigDecimal("R62_STAGE1_PROVISIONS"));
obj.setR62_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R62_QUALIFY_STAGE2_PROVISIONS"));
obj.setR62_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R62_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R63
// =========================
obj.setR63_PRODUCT(rs.getString("R63_PRODUCT"));
obj.setR63_STAGE1_PROVISIONS(rs.getBigDecimal("R63_STAGE1_PROVISIONS"));
obj.setR63_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R63_QUALIFY_STAGE2_PROVISIONS"));
obj.setR63_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R63_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R64
// =========================
obj.setR64_PRODUCT(rs.getString("R64_PRODUCT"));
obj.setR64_STAGE1_PROVISIONS(rs.getBigDecimal("R64_STAGE1_PROVISIONS"));
obj.setR64_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R64_QUALIFY_STAGE2_PROVISIONS"));
obj.setR64_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R64_TOTAL_GENERAL_PROVISIONS"));

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

public class M_GP_RESUB_Summary_Entity {

   
		private String R11_PRODUCT;
	private BigDecimal R11_STAGE1_PROVISIONS;
	private BigDecimal R11_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R11_TOTAL_GENERAL_PROVISIONS;

	private String R12_PRODUCT;
	private BigDecimal R12_STAGE1_PROVISIONS;
	private BigDecimal R12_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R12_TOTAL_GENERAL_PROVISIONS;

	private String R13_PRODUCT;
	private BigDecimal R13_STAGE1_PROVISIONS;
	private BigDecimal R13_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R13_TOTAL_GENERAL_PROVISIONS;

	private String R14_PRODUCT;
	private BigDecimal R14_STAGE1_PROVISIONS;
	private BigDecimal R14_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R14_TOTAL_GENERAL_PROVISIONS;

	private String R15_PRODUCT;
	private BigDecimal R15_STAGE1_PROVISIONS;
	private BigDecimal R15_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R15_TOTAL_GENERAL_PROVISIONS;

	private String R16_PRODUCT;
	private BigDecimal R16_STAGE1_PROVISIONS;
	private BigDecimal R16_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R16_TOTAL_GENERAL_PROVISIONS;

	private String R17_PRODUCT;
	private BigDecimal R17_STAGE1_PROVISIONS;
	private BigDecimal R17_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R17_TOTAL_GENERAL_PROVISIONS;

	private String R18_PRODUCT;
	private BigDecimal R18_STAGE1_PROVISIONS;
	private BigDecimal R18_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R18_TOTAL_GENERAL_PROVISIONS;

	private String R19_PRODUCT;
	private BigDecimal R19_STAGE1_PROVISIONS;
	private BigDecimal R19_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R19_TOTAL_GENERAL_PROVISIONS;

	private String R20_PRODUCT;
	private BigDecimal R20_STAGE1_PROVISIONS;
	private BigDecimal R20_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R20_TOTAL_GENERAL_PROVISIONS;

	private String R21_PRODUCT;
	private BigDecimal R21_STAGE1_PROVISIONS;
	private BigDecimal R21_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R21_TOTAL_GENERAL_PROVISIONS;

	private String R22_PRODUCT;
	private BigDecimal R22_STAGE1_PROVISIONS;
	private BigDecimal R22_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R22_TOTAL_GENERAL_PROVISIONS;

	private String R23_PRODUCT;
	private BigDecimal R23_STAGE1_PROVISIONS;
	private BigDecimal R23_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R23_TOTAL_GENERAL_PROVISIONS;

	private String R24_PRODUCT;
	private BigDecimal R24_STAGE1_PROVISIONS;
	private BigDecimal R24_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R24_TOTAL_GENERAL_PROVISIONS;

	private String R25_PRODUCT;
	private BigDecimal R25_STAGE1_PROVISIONS;
	private BigDecimal R25_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R25_TOTAL_GENERAL_PROVISIONS;

	private String R26_PRODUCT;
	private BigDecimal R26_STAGE1_PROVISIONS;
	private BigDecimal R26_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R26_TOTAL_GENERAL_PROVISIONS;

	private String R27_PRODUCT;
	private BigDecimal R27_STAGE1_PROVISIONS;
	private BigDecimal R27_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R27_TOTAL_GENERAL_PROVISIONS;

	private String R28_PRODUCT;
	private BigDecimal R28_STAGE1_PROVISIONS;
	private BigDecimal R28_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R28_TOTAL_GENERAL_PROVISIONS;

	private String R29_PRODUCT;
	private BigDecimal R29_STAGE1_PROVISIONS;
	private BigDecimal R29_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R29_TOTAL_GENERAL_PROVISIONS;

	private String R30_PRODUCT;
	private BigDecimal R30_STAGE1_PROVISIONS;
	private BigDecimal R30_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R30_TOTAL_GENERAL_PROVISIONS;

	private String R31_PRODUCT;
	private BigDecimal R31_STAGE1_PROVISIONS;
	private BigDecimal R31_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R31_TOTAL_GENERAL_PROVISIONS;

	private String R32_PRODUCT;
	private BigDecimal R32_STAGE1_PROVISIONS;
	private BigDecimal R32_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R32_TOTAL_GENERAL_PROVISIONS;

	private String R33_PRODUCT;
	private BigDecimal R33_STAGE1_PROVISIONS;
	private BigDecimal R33_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R33_TOTAL_GENERAL_PROVISIONS;

	private String R34_PRODUCT;
	private BigDecimal R34_STAGE1_PROVISIONS;
	private BigDecimal R34_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R34_TOTAL_GENERAL_PROVISIONS;

	private String R35_PRODUCT;
	private BigDecimal R35_STAGE1_PROVISIONS;
	private BigDecimal R35_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R35_TOTAL_GENERAL_PROVISIONS;

	private String R36_PRODUCT;
	private BigDecimal R36_STAGE1_PROVISIONS;
	private BigDecimal R36_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R36_TOTAL_GENERAL_PROVISIONS;

	private String R37_PRODUCT;
	private BigDecimal R37_STAGE1_PROVISIONS;
	private BigDecimal R37_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R37_TOTAL_GENERAL_PROVISIONS;

	private String R38_PRODUCT;
	private BigDecimal R38_STAGE1_PROVISIONS;
	private BigDecimal R38_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R38_TOTAL_GENERAL_PROVISIONS;

	private String R39_PRODUCT;
	private BigDecimal R39_STAGE1_PROVISIONS;
	private BigDecimal R39_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R39_TOTAL_GENERAL_PROVISIONS;

	private String R40_PRODUCT;
	private BigDecimal R40_STAGE1_PROVISIONS;
	private BigDecimal R40_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R40_TOTAL_GENERAL_PROVISIONS;

	private String R41_PRODUCT;
	private BigDecimal R41_STAGE1_PROVISIONS;
	private BigDecimal R41_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R41_TOTAL_GENERAL_PROVISIONS;

	private String R42_PRODUCT;
	private BigDecimal R42_STAGE1_PROVISIONS;
	private BigDecimal R42_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R42_TOTAL_GENERAL_PROVISIONS;

	private String R43_PRODUCT;
	private BigDecimal R43_STAGE1_PROVISIONS;
	private BigDecimal R43_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R43_TOTAL_GENERAL_PROVISIONS;

	private String R44_PRODUCT;
	private BigDecimal R44_STAGE1_PROVISIONS;
	private BigDecimal R44_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R44_TOTAL_GENERAL_PROVISIONS;

	private String R45_PRODUCT;
	private BigDecimal R45_STAGE1_PROVISIONS;
	private BigDecimal R45_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R45_TOTAL_GENERAL_PROVISIONS;

	private String R46_PRODUCT;
	private BigDecimal R46_STAGE1_PROVISIONS;
	private BigDecimal R46_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R46_TOTAL_GENERAL_PROVISIONS;

	private String R47_PRODUCT;
	private BigDecimal R47_STAGE1_PROVISIONS;
	private BigDecimal R47_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R47_TOTAL_GENERAL_PROVISIONS;

	private String R48_PRODUCT;
	private BigDecimal R48_STAGE1_PROVISIONS;
	private BigDecimal R48_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R48_TOTAL_GENERAL_PROVISIONS;

	private String R49_PRODUCT;
	private BigDecimal R49_STAGE1_PROVISIONS;
	private BigDecimal R49_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R49_TOTAL_GENERAL_PROVISIONS;

	private String R50_PRODUCT;
	private BigDecimal R50_STAGE1_PROVISIONS;
	private BigDecimal R50_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R50_TOTAL_GENERAL_PROVISIONS;

	private String R51_PRODUCT;
	private BigDecimal R51_STAGE1_PROVISIONS;
	private BigDecimal R51_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R51_TOTAL_GENERAL_PROVISIONS;

	private String R52_PRODUCT;
	private BigDecimal R52_STAGE1_PROVISIONS;
	private BigDecimal R52_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R52_TOTAL_GENERAL_PROVISIONS;

	private String R53_PRODUCT;
	private BigDecimal R53_STAGE1_PROVISIONS;
	private BigDecimal R53_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R53_TOTAL_GENERAL_PROVISIONS;

	private String R54_PRODUCT;
	private BigDecimal R54_STAGE1_PROVISIONS;
	private BigDecimal R54_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R54_TOTAL_GENERAL_PROVISIONS;

	private String R55_PRODUCT;
	private BigDecimal R55_STAGE1_PROVISIONS;
	private BigDecimal R55_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R55_TOTAL_GENERAL_PROVISIONS;

	private String R56_PRODUCT;
	private BigDecimal R56_STAGE1_PROVISIONS;
	private BigDecimal R56_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R56_TOTAL_GENERAL_PROVISIONS;

	private String R57_PRODUCT;
	private BigDecimal R57_STAGE1_PROVISIONS;
	private BigDecimal R57_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R57_TOTAL_GENERAL_PROVISIONS;

	private String R58_PRODUCT;
	private BigDecimal R58_STAGE1_PROVISIONS;
	private BigDecimal R58_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R58_TOTAL_GENERAL_PROVISIONS;

	private String R59_PRODUCT;
	private BigDecimal R59_STAGE1_PROVISIONS;
	private BigDecimal R59_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R59_TOTAL_GENERAL_PROVISIONS;

	private String R60_PRODUCT;
	private BigDecimal R60_STAGE1_PROVISIONS;
	private BigDecimal R60_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R60_TOTAL_GENERAL_PROVISIONS;

	private String R61_PRODUCT;
	private BigDecimal R61_STAGE1_PROVISIONS;
	private BigDecimal R61_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R61_TOTAL_GENERAL_PROVISIONS;

	private String R62_PRODUCT;
	private BigDecimal R62_STAGE1_PROVISIONS;
	private BigDecimal R62_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R62_TOTAL_GENERAL_PROVISIONS;

	private String R63_PRODUCT;
	private BigDecimal R63_STAGE1_PROVISIONS;
	private BigDecimal R63_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R63_TOTAL_GENERAL_PROVISIONS;

	private String R64_PRODUCT;
	private BigDecimal R64_STAGE1_PROVISIONS;
	private BigDecimal R64_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R64_TOTAL_GENERAL_PROVISIONS;
	
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
	
	
public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_STAGE1_PROVISIONS() {
		return R11_STAGE1_PROVISIONS;
	}
	public void setR11_STAGE1_PROVISIONS(BigDecimal r11_STAGE1_PROVISIONS) {
		R11_STAGE1_PROVISIONS = r11_STAGE1_PROVISIONS;
	}
	public BigDecimal getR11_QUALIFY_STAGE2_PROVISIONS() {
		return R11_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR11_QUALIFY_STAGE2_PROVISIONS(BigDecimal r11_QUALIFY_STAGE2_PROVISIONS) {
		R11_QUALIFY_STAGE2_PROVISIONS = r11_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR11_TOTAL_GENERAL_PROVISIONS() {
		return R11_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR11_TOTAL_GENERAL_PROVISIONS(BigDecimal r11_TOTAL_GENERAL_PROVISIONS) {
		R11_TOTAL_GENERAL_PROVISIONS = r11_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_STAGE1_PROVISIONS() {
		return R12_STAGE1_PROVISIONS;
	}
	public void setR12_STAGE1_PROVISIONS(BigDecimal r12_STAGE1_PROVISIONS) {
		R12_STAGE1_PROVISIONS = r12_STAGE1_PROVISIONS;
	}
	public BigDecimal getR12_QUALIFY_STAGE2_PROVISIONS() {
		return R12_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR12_QUALIFY_STAGE2_PROVISIONS(BigDecimal r12_QUALIFY_STAGE2_PROVISIONS) {
		R12_QUALIFY_STAGE2_PROVISIONS = r12_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR12_TOTAL_GENERAL_PROVISIONS() {
		return R12_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR12_TOTAL_GENERAL_PROVISIONS(BigDecimal r12_TOTAL_GENERAL_PROVISIONS) {
		R12_TOTAL_GENERAL_PROVISIONS = r12_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_STAGE1_PROVISIONS() {
		return R13_STAGE1_PROVISIONS;
	}
	public void setR13_STAGE1_PROVISIONS(BigDecimal r13_STAGE1_PROVISIONS) {
		R13_STAGE1_PROVISIONS = r13_STAGE1_PROVISIONS;
	}
	public BigDecimal getR13_QUALIFY_STAGE2_PROVISIONS() {
		return R13_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR13_QUALIFY_STAGE2_PROVISIONS(BigDecimal r13_QUALIFY_STAGE2_PROVISIONS) {
		R13_QUALIFY_STAGE2_PROVISIONS = r13_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR13_TOTAL_GENERAL_PROVISIONS() {
		return R13_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR13_TOTAL_GENERAL_PROVISIONS(BigDecimal r13_TOTAL_GENERAL_PROVISIONS) {
		R13_TOTAL_GENERAL_PROVISIONS = r13_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_STAGE1_PROVISIONS() {
		return R14_STAGE1_PROVISIONS;
	}
	public void setR14_STAGE1_PROVISIONS(BigDecimal r14_STAGE1_PROVISIONS) {
		R14_STAGE1_PROVISIONS = r14_STAGE1_PROVISIONS;
	}
	public BigDecimal getR14_QUALIFY_STAGE2_PROVISIONS() {
		return R14_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR14_QUALIFY_STAGE2_PROVISIONS(BigDecimal r14_QUALIFY_STAGE2_PROVISIONS) {
		R14_QUALIFY_STAGE2_PROVISIONS = r14_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR14_TOTAL_GENERAL_PROVISIONS() {
		return R14_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR14_TOTAL_GENERAL_PROVISIONS(BigDecimal r14_TOTAL_GENERAL_PROVISIONS) {
		R14_TOTAL_GENERAL_PROVISIONS = r14_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_STAGE1_PROVISIONS() {
		return R15_STAGE1_PROVISIONS;
	}
	public void setR15_STAGE1_PROVISIONS(BigDecimal r15_STAGE1_PROVISIONS) {
		R15_STAGE1_PROVISIONS = r15_STAGE1_PROVISIONS;
	}
	public BigDecimal getR15_QUALIFY_STAGE2_PROVISIONS() {
		return R15_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR15_QUALIFY_STAGE2_PROVISIONS(BigDecimal r15_QUALIFY_STAGE2_PROVISIONS) {
		R15_QUALIFY_STAGE2_PROVISIONS = r15_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR15_TOTAL_GENERAL_PROVISIONS() {
		return R15_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR15_TOTAL_GENERAL_PROVISIONS(BigDecimal r15_TOTAL_GENERAL_PROVISIONS) {
		R15_TOTAL_GENERAL_PROVISIONS = r15_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_STAGE1_PROVISIONS() {
		return R16_STAGE1_PROVISIONS;
	}
	public void setR16_STAGE1_PROVISIONS(BigDecimal r16_STAGE1_PROVISIONS) {
		R16_STAGE1_PROVISIONS = r16_STAGE1_PROVISIONS;
	}
	public BigDecimal getR16_QUALIFY_STAGE2_PROVISIONS() {
		return R16_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR16_QUALIFY_STAGE2_PROVISIONS(BigDecimal r16_QUALIFY_STAGE2_PROVISIONS) {
		R16_QUALIFY_STAGE2_PROVISIONS = r16_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR16_TOTAL_GENERAL_PROVISIONS() {
		return R16_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR16_TOTAL_GENERAL_PROVISIONS(BigDecimal r16_TOTAL_GENERAL_PROVISIONS) {
		R16_TOTAL_GENERAL_PROVISIONS = r16_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_STAGE1_PROVISIONS() {
		return R17_STAGE1_PROVISIONS;
	}
	public void setR17_STAGE1_PROVISIONS(BigDecimal r17_STAGE1_PROVISIONS) {
		R17_STAGE1_PROVISIONS = r17_STAGE1_PROVISIONS;
	}
	public BigDecimal getR17_QUALIFY_STAGE2_PROVISIONS() {
		return R17_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR17_QUALIFY_STAGE2_PROVISIONS(BigDecimal r17_QUALIFY_STAGE2_PROVISIONS) {
		R17_QUALIFY_STAGE2_PROVISIONS = r17_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR17_TOTAL_GENERAL_PROVISIONS() {
		return R17_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR17_TOTAL_GENERAL_PROVISIONS(BigDecimal r17_TOTAL_GENERAL_PROVISIONS) {
		R17_TOTAL_GENERAL_PROVISIONS = r17_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}
	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}
	public BigDecimal getR18_STAGE1_PROVISIONS() {
		return R18_STAGE1_PROVISIONS;
	}
	public void setR18_STAGE1_PROVISIONS(BigDecimal r18_STAGE1_PROVISIONS) {
		R18_STAGE1_PROVISIONS = r18_STAGE1_PROVISIONS;
	}
	public BigDecimal getR18_QUALIFY_STAGE2_PROVISIONS() {
		return R18_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR18_QUALIFY_STAGE2_PROVISIONS(BigDecimal r18_QUALIFY_STAGE2_PROVISIONS) {
		R18_QUALIFY_STAGE2_PROVISIONS = r18_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR18_TOTAL_GENERAL_PROVISIONS() {
		return R18_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR18_TOTAL_GENERAL_PROVISIONS(BigDecimal r18_TOTAL_GENERAL_PROVISIONS) {
		R18_TOTAL_GENERAL_PROVISIONS = r18_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}
	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}
	public BigDecimal getR19_STAGE1_PROVISIONS() {
		return R19_STAGE1_PROVISIONS;
	}
	public void setR19_STAGE1_PROVISIONS(BigDecimal r19_STAGE1_PROVISIONS) {
		R19_STAGE1_PROVISIONS = r19_STAGE1_PROVISIONS;
	}
	public BigDecimal getR19_QUALIFY_STAGE2_PROVISIONS() {
		return R19_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR19_QUALIFY_STAGE2_PROVISIONS(BigDecimal r19_QUALIFY_STAGE2_PROVISIONS) {
		R19_QUALIFY_STAGE2_PROVISIONS = r19_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR19_TOTAL_GENERAL_PROVISIONS() {
		return R19_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR19_TOTAL_GENERAL_PROVISIONS(BigDecimal r19_TOTAL_GENERAL_PROVISIONS) {
		R19_TOTAL_GENERAL_PROVISIONS = r19_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}
	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}
	public BigDecimal getR20_STAGE1_PROVISIONS() {
		return R20_STAGE1_PROVISIONS;
	}
	public void setR20_STAGE1_PROVISIONS(BigDecimal r20_STAGE1_PROVISIONS) {
		R20_STAGE1_PROVISIONS = r20_STAGE1_PROVISIONS;
	}
	public BigDecimal getR20_QUALIFY_STAGE2_PROVISIONS() {
		return R20_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR20_QUALIFY_STAGE2_PROVISIONS(BigDecimal r20_QUALIFY_STAGE2_PROVISIONS) {
		R20_QUALIFY_STAGE2_PROVISIONS = r20_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR20_TOTAL_GENERAL_PROVISIONS() {
		return R20_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR20_TOTAL_GENERAL_PROVISIONS(BigDecimal r20_TOTAL_GENERAL_PROVISIONS) {
		R20_TOTAL_GENERAL_PROVISIONS = r20_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}
	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}
	public BigDecimal getR21_STAGE1_PROVISIONS() {
		return R21_STAGE1_PROVISIONS;
	}
	public void setR21_STAGE1_PROVISIONS(BigDecimal r21_STAGE1_PROVISIONS) {
		R21_STAGE1_PROVISIONS = r21_STAGE1_PROVISIONS;
	}
	public BigDecimal getR21_QUALIFY_STAGE2_PROVISIONS() {
		return R21_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR21_QUALIFY_STAGE2_PROVISIONS(BigDecimal r21_QUALIFY_STAGE2_PROVISIONS) {
		R21_QUALIFY_STAGE2_PROVISIONS = r21_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR21_TOTAL_GENERAL_PROVISIONS() {
		return R21_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR21_TOTAL_GENERAL_PROVISIONS(BigDecimal r21_TOTAL_GENERAL_PROVISIONS) {
		R21_TOTAL_GENERAL_PROVISIONS = r21_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}
	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}
	public BigDecimal getR22_STAGE1_PROVISIONS() {
		return R22_STAGE1_PROVISIONS;
	}
	public void setR22_STAGE1_PROVISIONS(BigDecimal r22_STAGE1_PROVISIONS) {
		R22_STAGE1_PROVISIONS = r22_STAGE1_PROVISIONS;
	}
	public BigDecimal getR22_QUALIFY_STAGE2_PROVISIONS() {
		return R22_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR22_QUALIFY_STAGE2_PROVISIONS(BigDecimal r22_QUALIFY_STAGE2_PROVISIONS) {
		R22_QUALIFY_STAGE2_PROVISIONS = r22_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR22_TOTAL_GENERAL_PROVISIONS() {
		return R22_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR22_TOTAL_GENERAL_PROVISIONS(BigDecimal r22_TOTAL_GENERAL_PROVISIONS) {
		R22_TOTAL_GENERAL_PROVISIONS = r22_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}
	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}
	public BigDecimal getR23_STAGE1_PROVISIONS() {
		return R23_STAGE1_PROVISIONS;
	}
	public void setR23_STAGE1_PROVISIONS(BigDecimal r23_STAGE1_PROVISIONS) {
		R23_STAGE1_PROVISIONS = r23_STAGE1_PROVISIONS;
	}
	public BigDecimal getR23_QUALIFY_STAGE2_PROVISIONS() {
		return R23_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR23_QUALIFY_STAGE2_PROVISIONS(BigDecimal r23_QUALIFY_STAGE2_PROVISIONS) {
		R23_QUALIFY_STAGE2_PROVISIONS = r23_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR23_TOTAL_GENERAL_PROVISIONS() {
		return R23_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR23_TOTAL_GENERAL_PROVISIONS(BigDecimal r23_TOTAL_GENERAL_PROVISIONS) {
		R23_TOTAL_GENERAL_PROVISIONS = r23_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}
	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}
	public BigDecimal getR24_STAGE1_PROVISIONS() {
		return R24_STAGE1_PROVISIONS;
	}
	public void setR24_STAGE1_PROVISIONS(BigDecimal r24_STAGE1_PROVISIONS) {
		R24_STAGE1_PROVISIONS = r24_STAGE1_PROVISIONS;
	}
	public BigDecimal getR24_QUALIFY_STAGE2_PROVISIONS() {
		return R24_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR24_QUALIFY_STAGE2_PROVISIONS(BigDecimal r24_QUALIFY_STAGE2_PROVISIONS) {
		R24_QUALIFY_STAGE2_PROVISIONS = r24_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR24_TOTAL_GENERAL_PROVISIONS() {
		return R24_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR24_TOTAL_GENERAL_PROVISIONS(BigDecimal r24_TOTAL_GENERAL_PROVISIONS) {
		R24_TOTAL_GENERAL_PROVISIONS = r24_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}
	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}
	public BigDecimal getR25_STAGE1_PROVISIONS() {
		return R25_STAGE1_PROVISIONS;
	}
	public void setR25_STAGE1_PROVISIONS(BigDecimal r25_STAGE1_PROVISIONS) {
		R25_STAGE1_PROVISIONS = r25_STAGE1_PROVISIONS;
	}
	public BigDecimal getR25_QUALIFY_STAGE2_PROVISIONS() {
		return R25_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR25_QUALIFY_STAGE2_PROVISIONS(BigDecimal r25_QUALIFY_STAGE2_PROVISIONS) {
		R25_QUALIFY_STAGE2_PROVISIONS = r25_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR25_TOTAL_GENERAL_PROVISIONS() {
		return R25_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR25_TOTAL_GENERAL_PROVISIONS(BigDecimal r25_TOTAL_GENERAL_PROVISIONS) {
		R25_TOTAL_GENERAL_PROVISIONS = r25_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}
	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}
	public BigDecimal getR26_STAGE1_PROVISIONS() {
		return R26_STAGE1_PROVISIONS;
	}
	public void setR26_STAGE1_PROVISIONS(BigDecimal r26_STAGE1_PROVISIONS) {
		R26_STAGE1_PROVISIONS = r26_STAGE1_PROVISIONS;
	}
	public BigDecimal getR26_QUALIFY_STAGE2_PROVISIONS() {
		return R26_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR26_QUALIFY_STAGE2_PROVISIONS(BigDecimal r26_QUALIFY_STAGE2_PROVISIONS) {
		R26_QUALIFY_STAGE2_PROVISIONS = r26_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR26_TOTAL_GENERAL_PROVISIONS() {
		return R26_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR26_TOTAL_GENERAL_PROVISIONS(BigDecimal r26_TOTAL_GENERAL_PROVISIONS) {
		R26_TOTAL_GENERAL_PROVISIONS = r26_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}
	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}
	public BigDecimal getR27_STAGE1_PROVISIONS() {
		return R27_STAGE1_PROVISIONS;
	}
	public void setR27_STAGE1_PROVISIONS(BigDecimal r27_STAGE1_PROVISIONS) {
		R27_STAGE1_PROVISIONS = r27_STAGE1_PROVISIONS;
	}
	public BigDecimal getR27_QUALIFY_STAGE2_PROVISIONS() {
		return R27_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR27_QUALIFY_STAGE2_PROVISIONS(BigDecimal r27_QUALIFY_STAGE2_PROVISIONS) {
		R27_QUALIFY_STAGE2_PROVISIONS = r27_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR27_TOTAL_GENERAL_PROVISIONS() {
		return R27_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR27_TOTAL_GENERAL_PROVISIONS(BigDecimal r27_TOTAL_GENERAL_PROVISIONS) {
		R27_TOTAL_GENERAL_PROVISIONS = r27_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}
	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}
	public BigDecimal getR28_STAGE1_PROVISIONS() {
		return R28_STAGE1_PROVISIONS;
	}
	public void setR28_STAGE1_PROVISIONS(BigDecimal r28_STAGE1_PROVISIONS) {
		R28_STAGE1_PROVISIONS = r28_STAGE1_PROVISIONS;
	}
	public BigDecimal getR28_QUALIFY_STAGE2_PROVISIONS() {
		return R28_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR28_QUALIFY_STAGE2_PROVISIONS(BigDecimal r28_QUALIFY_STAGE2_PROVISIONS) {
		R28_QUALIFY_STAGE2_PROVISIONS = r28_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR28_TOTAL_GENERAL_PROVISIONS() {
		return R28_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR28_TOTAL_GENERAL_PROVISIONS(BigDecimal r28_TOTAL_GENERAL_PROVISIONS) {
		R28_TOTAL_GENERAL_PROVISIONS = r28_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}
	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}
	public BigDecimal getR29_STAGE1_PROVISIONS() {
		return R29_STAGE1_PROVISIONS;
	}
	public void setR29_STAGE1_PROVISIONS(BigDecimal r29_STAGE1_PROVISIONS) {
		R29_STAGE1_PROVISIONS = r29_STAGE1_PROVISIONS;
	}
	public BigDecimal getR29_QUALIFY_STAGE2_PROVISIONS() {
		return R29_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR29_QUALIFY_STAGE2_PROVISIONS(BigDecimal r29_QUALIFY_STAGE2_PROVISIONS) {
		R29_QUALIFY_STAGE2_PROVISIONS = r29_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR29_TOTAL_GENERAL_PROVISIONS() {
		return R29_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR29_TOTAL_GENERAL_PROVISIONS(BigDecimal r29_TOTAL_GENERAL_PROVISIONS) {
		R29_TOTAL_GENERAL_PROVISIONS = r29_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}
	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}
	public BigDecimal getR30_STAGE1_PROVISIONS() {
		return R30_STAGE1_PROVISIONS;
	}
	public void setR30_STAGE1_PROVISIONS(BigDecimal r30_STAGE1_PROVISIONS) {
		R30_STAGE1_PROVISIONS = r30_STAGE1_PROVISIONS;
	}
	public BigDecimal getR30_QUALIFY_STAGE2_PROVISIONS() {
		return R30_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR30_QUALIFY_STAGE2_PROVISIONS(BigDecimal r30_QUALIFY_STAGE2_PROVISIONS) {
		R30_QUALIFY_STAGE2_PROVISIONS = r30_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR30_TOTAL_GENERAL_PROVISIONS() {
		return R30_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR30_TOTAL_GENERAL_PROVISIONS(BigDecimal r30_TOTAL_GENERAL_PROVISIONS) {
		R30_TOTAL_GENERAL_PROVISIONS = r30_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}
	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}
	public BigDecimal getR31_STAGE1_PROVISIONS() {
		return R31_STAGE1_PROVISIONS;
	}
	public void setR31_STAGE1_PROVISIONS(BigDecimal r31_STAGE1_PROVISIONS) {
		R31_STAGE1_PROVISIONS = r31_STAGE1_PROVISIONS;
	}
	public BigDecimal getR31_QUALIFY_STAGE2_PROVISIONS() {
		return R31_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR31_QUALIFY_STAGE2_PROVISIONS(BigDecimal r31_QUALIFY_STAGE2_PROVISIONS) {
		R31_QUALIFY_STAGE2_PROVISIONS = r31_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR31_TOTAL_GENERAL_PROVISIONS() {
		return R31_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR31_TOTAL_GENERAL_PROVISIONS(BigDecimal r31_TOTAL_GENERAL_PROVISIONS) {
		R31_TOTAL_GENERAL_PROVISIONS = r31_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}
	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}
	public BigDecimal getR32_STAGE1_PROVISIONS() {
		return R32_STAGE1_PROVISIONS;
	}
	public void setR32_STAGE1_PROVISIONS(BigDecimal r32_STAGE1_PROVISIONS) {
		R32_STAGE1_PROVISIONS = r32_STAGE1_PROVISIONS;
	}
	public BigDecimal getR32_QUALIFY_STAGE2_PROVISIONS() {
		return R32_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR32_QUALIFY_STAGE2_PROVISIONS(BigDecimal r32_QUALIFY_STAGE2_PROVISIONS) {
		R32_QUALIFY_STAGE2_PROVISIONS = r32_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR32_TOTAL_GENERAL_PROVISIONS() {
		return R32_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR32_TOTAL_GENERAL_PROVISIONS(BigDecimal r32_TOTAL_GENERAL_PROVISIONS) {
		R32_TOTAL_GENERAL_PROVISIONS = r32_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}
	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}
	public BigDecimal getR33_STAGE1_PROVISIONS() {
		return R33_STAGE1_PROVISIONS;
	}
	public void setR33_STAGE1_PROVISIONS(BigDecimal r33_STAGE1_PROVISIONS) {
		R33_STAGE1_PROVISIONS = r33_STAGE1_PROVISIONS;
	}
	public BigDecimal getR33_QUALIFY_STAGE2_PROVISIONS() {
		return R33_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR33_QUALIFY_STAGE2_PROVISIONS(BigDecimal r33_QUALIFY_STAGE2_PROVISIONS) {
		R33_QUALIFY_STAGE2_PROVISIONS = r33_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR33_TOTAL_GENERAL_PROVISIONS() {
		return R33_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR33_TOTAL_GENERAL_PROVISIONS(BigDecimal r33_TOTAL_GENERAL_PROVISIONS) {
		R33_TOTAL_GENERAL_PROVISIONS = r33_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}
	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}
	public BigDecimal getR34_STAGE1_PROVISIONS() {
		return R34_STAGE1_PROVISIONS;
	}
	public void setR34_STAGE1_PROVISIONS(BigDecimal r34_STAGE1_PROVISIONS) {
		R34_STAGE1_PROVISIONS = r34_STAGE1_PROVISIONS;
	}
	public BigDecimal getR34_QUALIFY_STAGE2_PROVISIONS() {
		return R34_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR34_QUALIFY_STAGE2_PROVISIONS(BigDecimal r34_QUALIFY_STAGE2_PROVISIONS) {
		R34_QUALIFY_STAGE2_PROVISIONS = r34_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR34_TOTAL_GENERAL_PROVISIONS() {
		return R34_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR34_TOTAL_GENERAL_PROVISIONS(BigDecimal r34_TOTAL_GENERAL_PROVISIONS) {
		R34_TOTAL_GENERAL_PROVISIONS = r34_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}
	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}
	public BigDecimal getR35_STAGE1_PROVISIONS() {
		return R35_STAGE1_PROVISIONS;
	}
	public void setR35_STAGE1_PROVISIONS(BigDecimal r35_STAGE1_PROVISIONS) {
		R35_STAGE1_PROVISIONS = r35_STAGE1_PROVISIONS;
	}
	public BigDecimal getR35_QUALIFY_STAGE2_PROVISIONS() {
		return R35_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR35_QUALIFY_STAGE2_PROVISIONS(BigDecimal r35_QUALIFY_STAGE2_PROVISIONS) {
		R35_QUALIFY_STAGE2_PROVISIONS = r35_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR35_TOTAL_GENERAL_PROVISIONS() {
		return R35_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR35_TOTAL_GENERAL_PROVISIONS(BigDecimal r35_TOTAL_GENERAL_PROVISIONS) {
		R35_TOTAL_GENERAL_PROVISIONS = r35_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_STAGE1_PROVISIONS() {
		return R36_STAGE1_PROVISIONS;
	}
	public void setR36_STAGE1_PROVISIONS(BigDecimal r36_STAGE1_PROVISIONS) {
		R36_STAGE1_PROVISIONS = r36_STAGE1_PROVISIONS;
	}
	public BigDecimal getR36_QUALIFY_STAGE2_PROVISIONS() {
		return R36_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR36_QUALIFY_STAGE2_PROVISIONS(BigDecimal r36_QUALIFY_STAGE2_PROVISIONS) {
		R36_QUALIFY_STAGE2_PROVISIONS = r36_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR36_TOTAL_GENERAL_PROVISIONS() {
		return R36_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR36_TOTAL_GENERAL_PROVISIONS(BigDecimal r36_TOTAL_GENERAL_PROVISIONS) {
		R36_TOTAL_GENERAL_PROVISIONS = r36_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}
	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}
	public BigDecimal getR37_STAGE1_PROVISIONS() {
		return R37_STAGE1_PROVISIONS;
	}
	public void setR37_STAGE1_PROVISIONS(BigDecimal r37_STAGE1_PROVISIONS) {
		R37_STAGE1_PROVISIONS = r37_STAGE1_PROVISIONS;
	}
	public BigDecimal getR37_QUALIFY_STAGE2_PROVISIONS() {
		return R37_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR37_QUALIFY_STAGE2_PROVISIONS(BigDecimal r37_QUALIFY_STAGE2_PROVISIONS) {
		R37_QUALIFY_STAGE2_PROVISIONS = r37_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR37_TOTAL_GENERAL_PROVISIONS() {
		return R37_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR37_TOTAL_GENERAL_PROVISIONS(BigDecimal r37_TOTAL_GENERAL_PROVISIONS) {
		R37_TOTAL_GENERAL_PROVISIONS = r37_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}
	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}
	public BigDecimal getR38_STAGE1_PROVISIONS() {
		return R38_STAGE1_PROVISIONS;
	}
	public void setR38_STAGE1_PROVISIONS(BigDecimal r38_STAGE1_PROVISIONS) {
		R38_STAGE1_PROVISIONS = r38_STAGE1_PROVISIONS;
	}
	public BigDecimal getR38_QUALIFY_STAGE2_PROVISIONS() {
		return R38_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR38_QUALIFY_STAGE2_PROVISIONS(BigDecimal r38_QUALIFY_STAGE2_PROVISIONS) {
		R38_QUALIFY_STAGE2_PROVISIONS = r38_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR38_TOTAL_GENERAL_PROVISIONS() {
		return R38_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR38_TOTAL_GENERAL_PROVISIONS(BigDecimal r38_TOTAL_GENERAL_PROVISIONS) {
		R38_TOTAL_GENERAL_PROVISIONS = r38_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}
	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}
	public BigDecimal getR39_STAGE1_PROVISIONS() {
		return R39_STAGE1_PROVISIONS;
	}
	public void setR39_STAGE1_PROVISIONS(BigDecimal r39_STAGE1_PROVISIONS) {
		R39_STAGE1_PROVISIONS = r39_STAGE1_PROVISIONS;
	}
	public BigDecimal getR39_QUALIFY_STAGE2_PROVISIONS() {
		return R39_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR39_QUALIFY_STAGE2_PROVISIONS(BigDecimal r39_QUALIFY_STAGE2_PROVISIONS) {
		R39_QUALIFY_STAGE2_PROVISIONS = r39_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR39_TOTAL_GENERAL_PROVISIONS() {
		return R39_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR39_TOTAL_GENERAL_PROVISIONS(BigDecimal r39_TOTAL_GENERAL_PROVISIONS) {
		R39_TOTAL_GENERAL_PROVISIONS = r39_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}
	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}
	public BigDecimal getR40_STAGE1_PROVISIONS() {
		return R40_STAGE1_PROVISIONS;
	}
	public void setR40_STAGE1_PROVISIONS(BigDecimal r40_STAGE1_PROVISIONS) {
		R40_STAGE1_PROVISIONS = r40_STAGE1_PROVISIONS;
	}
	public BigDecimal getR40_QUALIFY_STAGE2_PROVISIONS() {
		return R40_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR40_QUALIFY_STAGE2_PROVISIONS(BigDecimal r40_QUALIFY_STAGE2_PROVISIONS) {
		R40_QUALIFY_STAGE2_PROVISIONS = r40_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR40_TOTAL_GENERAL_PROVISIONS() {
		return R40_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR40_TOTAL_GENERAL_PROVISIONS(BigDecimal r40_TOTAL_GENERAL_PROVISIONS) {
		R40_TOTAL_GENERAL_PROVISIONS = r40_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}
	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}
	public BigDecimal getR41_STAGE1_PROVISIONS() {
		return R41_STAGE1_PROVISIONS;
	}
	public void setR41_STAGE1_PROVISIONS(BigDecimal r41_STAGE1_PROVISIONS) {
		R41_STAGE1_PROVISIONS = r41_STAGE1_PROVISIONS;
	}
	public BigDecimal getR41_QUALIFY_STAGE2_PROVISIONS() {
		return R41_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR41_QUALIFY_STAGE2_PROVISIONS(BigDecimal r41_QUALIFY_STAGE2_PROVISIONS) {
		R41_QUALIFY_STAGE2_PROVISIONS = r41_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR41_TOTAL_GENERAL_PROVISIONS() {
		return R41_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR41_TOTAL_GENERAL_PROVISIONS(BigDecimal r41_TOTAL_GENERAL_PROVISIONS) {
		R41_TOTAL_GENERAL_PROVISIONS = r41_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}
	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}
	public BigDecimal getR42_STAGE1_PROVISIONS() {
		return R42_STAGE1_PROVISIONS;
	}
	public void setR42_STAGE1_PROVISIONS(BigDecimal r42_STAGE1_PROVISIONS) {
		R42_STAGE1_PROVISIONS = r42_STAGE1_PROVISIONS;
	}
	public BigDecimal getR42_QUALIFY_STAGE2_PROVISIONS() {
		return R42_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR42_QUALIFY_STAGE2_PROVISIONS(BigDecimal r42_QUALIFY_STAGE2_PROVISIONS) {
		R42_QUALIFY_STAGE2_PROVISIONS = r42_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR42_TOTAL_GENERAL_PROVISIONS() {
		return R42_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR42_TOTAL_GENERAL_PROVISIONS(BigDecimal r42_TOTAL_GENERAL_PROVISIONS) {
		R42_TOTAL_GENERAL_PROVISIONS = r42_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}
	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}
	public BigDecimal getR43_STAGE1_PROVISIONS() {
		return R43_STAGE1_PROVISIONS;
	}
	public void setR43_STAGE1_PROVISIONS(BigDecimal r43_STAGE1_PROVISIONS) {
		R43_STAGE1_PROVISIONS = r43_STAGE1_PROVISIONS;
	}
	public BigDecimal getR43_QUALIFY_STAGE2_PROVISIONS() {
		return R43_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR43_QUALIFY_STAGE2_PROVISIONS(BigDecimal r43_QUALIFY_STAGE2_PROVISIONS) {
		R43_QUALIFY_STAGE2_PROVISIONS = r43_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR43_TOTAL_GENERAL_PROVISIONS() {
		return R43_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR43_TOTAL_GENERAL_PROVISIONS(BigDecimal r43_TOTAL_GENERAL_PROVISIONS) {
		R43_TOTAL_GENERAL_PROVISIONS = r43_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR44_PRODUCT() {
		return R44_PRODUCT;
	}
	public void setR44_PRODUCT(String r44_PRODUCT) {
		R44_PRODUCT = r44_PRODUCT;
	}
	public BigDecimal getR44_STAGE1_PROVISIONS() {
		return R44_STAGE1_PROVISIONS;
	}
	public void setR44_STAGE1_PROVISIONS(BigDecimal r44_STAGE1_PROVISIONS) {
		R44_STAGE1_PROVISIONS = r44_STAGE1_PROVISIONS;
	}
	public BigDecimal getR44_QUALIFY_STAGE2_PROVISIONS() {
		return R44_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR44_QUALIFY_STAGE2_PROVISIONS(BigDecimal r44_QUALIFY_STAGE2_PROVISIONS) {
		R44_QUALIFY_STAGE2_PROVISIONS = r44_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR44_TOTAL_GENERAL_PROVISIONS() {
		return R44_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR44_TOTAL_GENERAL_PROVISIONS(BigDecimal r44_TOTAL_GENERAL_PROVISIONS) {
		R44_TOTAL_GENERAL_PROVISIONS = r44_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR45_PRODUCT() {
		return R45_PRODUCT;
	}
	public void setR45_PRODUCT(String r45_PRODUCT) {
		R45_PRODUCT = r45_PRODUCT;
	}
	public BigDecimal getR45_STAGE1_PROVISIONS() {
		return R45_STAGE1_PROVISIONS;
	}
	public void setR45_STAGE1_PROVISIONS(BigDecimal r45_STAGE1_PROVISIONS) {
		R45_STAGE1_PROVISIONS = r45_STAGE1_PROVISIONS;
	}
	public BigDecimal getR45_QUALIFY_STAGE2_PROVISIONS() {
		return R45_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR45_QUALIFY_STAGE2_PROVISIONS(BigDecimal r45_QUALIFY_STAGE2_PROVISIONS) {
		R45_QUALIFY_STAGE2_PROVISIONS = r45_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR45_TOTAL_GENERAL_PROVISIONS() {
		return R45_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR45_TOTAL_GENERAL_PROVISIONS(BigDecimal r45_TOTAL_GENERAL_PROVISIONS) {
		R45_TOTAL_GENERAL_PROVISIONS = r45_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR46_PRODUCT() {
		return R46_PRODUCT;
	}
	public void setR46_PRODUCT(String r46_PRODUCT) {
		R46_PRODUCT = r46_PRODUCT;
	}
	public BigDecimal getR46_STAGE1_PROVISIONS() {
		return R46_STAGE1_PROVISIONS;
	}
	public void setR46_STAGE1_PROVISIONS(BigDecimal r46_STAGE1_PROVISIONS) {
		R46_STAGE1_PROVISIONS = r46_STAGE1_PROVISIONS;
	}
	public BigDecimal getR46_QUALIFY_STAGE2_PROVISIONS() {
		return R46_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR46_QUALIFY_STAGE2_PROVISIONS(BigDecimal r46_QUALIFY_STAGE2_PROVISIONS) {
		R46_QUALIFY_STAGE2_PROVISIONS = r46_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR46_TOTAL_GENERAL_PROVISIONS() {
		return R46_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR46_TOTAL_GENERAL_PROVISIONS(BigDecimal r46_TOTAL_GENERAL_PROVISIONS) {
		R46_TOTAL_GENERAL_PROVISIONS = r46_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}
	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}
	public BigDecimal getR47_STAGE1_PROVISIONS() {
		return R47_STAGE1_PROVISIONS;
	}
	public void setR47_STAGE1_PROVISIONS(BigDecimal r47_STAGE1_PROVISIONS) {
		R47_STAGE1_PROVISIONS = r47_STAGE1_PROVISIONS;
	}
	public BigDecimal getR47_QUALIFY_STAGE2_PROVISIONS() {
		return R47_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR47_QUALIFY_STAGE2_PROVISIONS(BigDecimal r47_QUALIFY_STAGE2_PROVISIONS) {
		R47_QUALIFY_STAGE2_PROVISIONS = r47_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR47_TOTAL_GENERAL_PROVISIONS() {
		return R47_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR47_TOTAL_GENERAL_PROVISIONS(BigDecimal r47_TOTAL_GENERAL_PROVISIONS) {
		R47_TOTAL_GENERAL_PROVISIONS = r47_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR48_PRODUCT() {
		return R48_PRODUCT;
	}
	public void setR48_PRODUCT(String r48_PRODUCT) {
		R48_PRODUCT = r48_PRODUCT;
	}
	public BigDecimal getR48_STAGE1_PROVISIONS() {
		return R48_STAGE1_PROVISIONS;
	}
	public void setR48_STAGE1_PROVISIONS(BigDecimal r48_STAGE1_PROVISIONS) {
		R48_STAGE1_PROVISIONS = r48_STAGE1_PROVISIONS;
	}
	public BigDecimal getR48_QUALIFY_STAGE2_PROVISIONS() {
		return R48_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR48_QUALIFY_STAGE2_PROVISIONS(BigDecimal r48_QUALIFY_STAGE2_PROVISIONS) {
		R48_QUALIFY_STAGE2_PROVISIONS = r48_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR48_TOTAL_GENERAL_PROVISIONS() {
		return R48_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR48_TOTAL_GENERAL_PROVISIONS(BigDecimal r48_TOTAL_GENERAL_PROVISIONS) {
		R48_TOTAL_GENERAL_PROVISIONS = r48_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR49_PRODUCT() {
		return R49_PRODUCT;
	}
	public void setR49_PRODUCT(String r49_PRODUCT) {
		R49_PRODUCT = r49_PRODUCT;
	}
	public BigDecimal getR49_STAGE1_PROVISIONS() {
		return R49_STAGE1_PROVISIONS;
	}
	public void setR49_STAGE1_PROVISIONS(BigDecimal r49_STAGE1_PROVISIONS) {
		R49_STAGE1_PROVISIONS = r49_STAGE1_PROVISIONS;
	}
	public BigDecimal getR49_QUALIFY_STAGE2_PROVISIONS() {
		return R49_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR49_QUALIFY_STAGE2_PROVISIONS(BigDecimal r49_QUALIFY_STAGE2_PROVISIONS) {
		R49_QUALIFY_STAGE2_PROVISIONS = r49_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR49_TOTAL_GENERAL_PROVISIONS() {
		return R49_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR49_TOTAL_GENERAL_PROVISIONS(BigDecimal r49_TOTAL_GENERAL_PROVISIONS) {
		R49_TOTAL_GENERAL_PROVISIONS = r49_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR50_PRODUCT() {
		return R50_PRODUCT;
	}
	public void setR50_PRODUCT(String r50_PRODUCT) {
		R50_PRODUCT = r50_PRODUCT;
	}
	public BigDecimal getR50_STAGE1_PROVISIONS() {
		return R50_STAGE1_PROVISIONS;
	}
	public void setR50_STAGE1_PROVISIONS(BigDecimal r50_STAGE1_PROVISIONS) {
		R50_STAGE1_PROVISIONS = r50_STAGE1_PROVISIONS;
	}
	public BigDecimal getR50_QUALIFY_STAGE2_PROVISIONS() {
		return R50_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR50_QUALIFY_STAGE2_PROVISIONS(BigDecimal r50_QUALIFY_STAGE2_PROVISIONS) {
		R50_QUALIFY_STAGE2_PROVISIONS = r50_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR50_TOTAL_GENERAL_PROVISIONS() {
		return R50_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR50_TOTAL_GENERAL_PROVISIONS(BigDecimal r50_TOTAL_GENERAL_PROVISIONS) {
		R50_TOTAL_GENERAL_PROVISIONS = r50_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR51_PRODUCT() {
		return R51_PRODUCT;
	}
	public void setR51_PRODUCT(String r51_PRODUCT) {
		R51_PRODUCT = r51_PRODUCT;
	}
	public BigDecimal getR51_STAGE1_PROVISIONS() {
		return R51_STAGE1_PROVISIONS;
	}
	public void setR51_STAGE1_PROVISIONS(BigDecimal r51_STAGE1_PROVISIONS) {
		R51_STAGE1_PROVISIONS = r51_STAGE1_PROVISIONS;
	}
	public BigDecimal getR51_QUALIFY_STAGE2_PROVISIONS() {
		return R51_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR51_QUALIFY_STAGE2_PROVISIONS(BigDecimal r51_QUALIFY_STAGE2_PROVISIONS) {
		R51_QUALIFY_STAGE2_PROVISIONS = r51_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR51_TOTAL_GENERAL_PROVISIONS() {
		return R51_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR51_TOTAL_GENERAL_PROVISIONS(BigDecimal r51_TOTAL_GENERAL_PROVISIONS) {
		R51_TOTAL_GENERAL_PROVISIONS = r51_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR52_PRODUCT() {
		return R52_PRODUCT;
	}
	public void setR52_PRODUCT(String r52_PRODUCT) {
		R52_PRODUCT = r52_PRODUCT;
	}
	public BigDecimal getR52_STAGE1_PROVISIONS() {
		return R52_STAGE1_PROVISIONS;
	}
	public void setR52_STAGE1_PROVISIONS(BigDecimal r52_STAGE1_PROVISIONS) {
		R52_STAGE1_PROVISIONS = r52_STAGE1_PROVISIONS;
	}
	public BigDecimal getR52_QUALIFY_STAGE2_PROVISIONS() {
		return R52_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR52_QUALIFY_STAGE2_PROVISIONS(BigDecimal r52_QUALIFY_STAGE2_PROVISIONS) {
		R52_QUALIFY_STAGE2_PROVISIONS = r52_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR52_TOTAL_GENERAL_PROVISIONS() {
		return R52_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR52_TOTAL_GENERAL_PROVISIONS(BigDecimal r52_TOTAL_GENERAL_PROVISIONS) {
		R52_TOTAL_GENERAL_PROVISIONS = r52_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR53_PRODUCT() {
		return R53_PRODUCT;
	}
	public void setR53_PRODUCT(String r53_PRODUCT) {
		R53_PRODUCT = r53_PRODUCT;
	}
	public BigDecimal getR53_STAGE1_PROVISIONS() {
		return R53_STAGE1_PROVISIONS;
	}
	public void setR53_STAGE1_PROVISIONS(BigDecimal r53_STAGE1_PROVISIONS) {
		R53_STAGE1_PROVISIONS = r53_STAGE1_PROVISIONS;
	}
	public BigDecimal getR53_QUALIFY_STAGE2_PROVISIONS() {
		return R53_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR53_QUALIFY_STAGE2_PROVISIONS(BigDecimal r53_QUALIFY_STAGE2_PROVISIONS) {
		R53_QUALIFY_STAGE2_PROVISIONS = r53_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR53_TOTAL_GENERAL_PROVISIONS() {
		return R53_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR53_TOTAL_GENERAL_PROVISIONS(BigDecimal r53_TOTAL_GENERAL_PROVISIONS) {
		R53_TOTAL_GENERAL_PROVISIONS = r53_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR54_PRODUCT() {
		return R54_PRODUCT;
	}
	public void setR54_PRODUCT(String r54_PRODUCT) {
		R54_PRODUCT = r54_PRODUCT;
	}
	public BigDecimal getR54_STAGE1_PROVISIONS() {
		return R54_STAGE1_PROVISIONS;
	}
	public void setR54_STAGE1_PROVISIONS(BigDecimal r54_STAGE1_PROVISIONS) {
		R54_STAGE1_PROVISIONS = r54_STAGE1_PROVISIONS;
	}
	public BigDecimal getR54_QUALIFY_STAGE2_PROVISIONS() {
		return R54_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR54_QUALIFY_STAGE2_PROVISIONS(BigDecimal r54_QUALIFY_STAGE2_PROVISIONS) {
		R54_QUALIFY_STAGE2_PROVISIONS = r54_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR54_TOTAL_GENERAL_PROVISIONS() {
		return R54_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR54_TOTAL_GENERAL_PROVISIONS(BigDecimal r54_TOTAL_GENERAL_PROVISIONS) {
		R54_TOTAL_GENERAL_PROVISIONS = r54_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR55_PRODUCT() {
		return R55_PRODUCT;
	}
	public void setR55_PRODUCT(String r55_PRODUCT) {
		R55_PRODUCT = r55_PRODUCT;
	}
	public BigDecimal getR55_STAGE1_PROVISIONS() {
		return R55_STAGE1_PROVISIONS;
	}
	public void setR55_STAGE1_PROVISIONS(BigDecimal r55_STAGE1_PROVISIONS) {
		R55_STAGE1_PROVISIONS = r55_STAGE1_PROVISIONS;
	}
	public BigDecimal getR55_QUALIFY_STAGE2_PROVISIONS() {
		return R55_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR55_QUALIFY_STAGE2_PROVISIONS(BigDecimal r55_QUALIFY_STAGE2_PROVISIONS) {
		R55_QUALIFY_STAGE2_PROVISIONS = r55_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR55_TOTAL_GENERAL_PROVISIONS() {
		return R55_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR55_TOTAL_GENERAL_PROVISIONS(BigDecimal r55_TOTAL_GENERAL_PROVISIONS) {
		R55_TOTAL_GENERAL_PROVISIONS = r55_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR56_PRODUCT() {
		return R56_PRODUCT;
	}
	public void setR56_PRODUCT(String r56_PRODUCT) {
		R56_PRODUCT = r56_PRODUCT;
	}
	public BigDecimal getR56_STAGE1_PROVISIONS() {
		return R56_STAGE1_PROVISIONS;
	}
	public void setR56_STAGE1_PROVISIONS(BigDecimal r56_STAGE1_PROVISIONS) {
		R56_STAGE1_PROVISIONS = r56_STAGE1_PROVISIONS;
	}
	public BigDecimal getR56_QUALIFY_STAGE2_PROVISIONS() {
		return R56_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR56_QUALIFY_STAGE2_PROVISIONS(BigDecimal r56_QUALIFY_STAGE2_PROVISIONS) {
		R56_QUALIFY_STAGE2_PROVISIONS = r56_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR56_TOTAL_GENERAL_PROVISIONS() {
		return R56_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR56_TOTAL_GENERAL_PROVISIONS(BigDecimal r56_TOTAL_GENERAL_PROVISIONS) {
		R56_TOTAL_GENERAL_PROVISIONS = r56_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR57_PRODUCT() {
		return R57_PRODUCT;
	}
	public void setR57_PRODUCT(String r57_PRODUCT) {
		R57_PRODUCT = r57_PRODUCT;
	}
	public BigDecimal getR57_STAGE1_PROVISIONS() {
		return R57_STAGE1_PROVISIONS;
	}
	public void setR57_STAGE1_PROVISIONS(BigDecimal r57_STAGE1_PROVISIONS) {
		R57_STAGE1_PROVISIONS = r57_STAGE1_PROVISIONS;
	}
	public BigDecimal getR57_QUALIFY_STAGE2_PROVISIONS() {
		return R57_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR57_QUALIFY_STAGE2_PROVISIONS(BigDecimal r57_QUALIFY_STAGE2_PROVISIONS) {
		R57_QUALIFY_STAGE2_PROVISIONS = r57_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR57_TOTAL_GENERAL_PROVISIONS() {
		return R57_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR57_TOTAL_GENERAL_PROVISIONS(BigDecimal r57_TOTAL_GENERAL_PROVISIONS) {
		R57_TOTAL_GENERAL_PROVISIONS = r57_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR58_PRODUCT() {
		return R58_PRODUCT;
	}
	public void setR58_PRODUCT(String r58_PRODUCT) {
		R58_PRODUCT = r58_PRODUCT;
	}
	public BigDecimal getR58_STAGE1_PROVISIONS() {
		return R58_STAGE1_PROVISIONS;
	}
	public void setR58_STAGE1_PROVISIONS(BigDecimal r58_STAGE1_PROVISIONS) {
		R58_STAGE1_PROVISIONS = r58_STAGE1_PROVISIONS;
	}
	public BigDecimal getR58_QUALIFY_STAGE2_PROVISIONS() {
		return R58_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR58_QUALIFY_STAGE2_PROVISIONS(BigDecimal r58_QUALIFY_STAGE2_PROVISIONS) {
		R58_QUALIFY_STAGE2_PROVISIONS = r58_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR58_TOTAL_GENERAL_PROVISIONS() {
		return R58_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR58_TOTAL_GENERAL_PROVISIONS(BigDecimal r58_TOTAL_GENERAL_PROVISIONS) {
		R58_TOTAL_GENERAL_PROVISIONS = r58_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR59_PRODUCT() {
		return R59_PRODUCT;
	}
	public void setR59_PRODUCT(String r59_PRODUCT) {
		R59_PRODUCT = r59_PRODUCT;
	}
	public BigDecimal getR59_STAGE1_PROVISIONS() {
		return R59_STAGE1_PROVISIONS;
	}
	public void setR59_STAGE1_PROVISIONS(BigDecimal r59_STAGE1_PROVISIONS) {
		R59_STAGE1_PROVISIONS = r59_STAGE1_PROVISIONS;
	}
	public BigDecimal getR59_QUALIFY_STAGE2_PROVISIONS() {
		return R59_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR59_QUALIFY_STAGE2_PROVISIONS(BigDecimal r59_QUALIFY_STAGE2_PROVISIONS) {
		R59_QUALIFY_STAGE2_PROVISIONS = r59_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR59_TOTAL_GENERAL_PROVISIONS() {
		return R59_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR59_TOTAL_GENERAL_PROVISIONS(BigDecimal r59_TOTAL_GENERAL_PROVISIONS) {
		R59_TOTAL_GENERAL_PROVISIONS = r59_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR60_PRODUCT() {
		return R60_PRODUCT;
	}
	public void setR60_PRODUCT(String r60_PRODUCT) {
		R60_PRODUCT = r60_PRODUCT;
	}
	public BigDecimal getR60_STAGE1_PROVISIONS() {
		return R60_STAGE1_PROVISIONS;
	}
	public void setR60_STAGE1_PROVISIONS(BigDecimal r60_STAGE1_PROVISIONS) {
		R60_STAGE1_PROVISIONS = r60_STAGE1_PROVISIONS;
	}
	public BigDecimal getR60_QUALIFY_STAGE2_PROVISIONS() {
		return R60_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR60_QUALIFY_STAGE2_PROVISIONS(BigDecimal r60_QUALIFY_STAGE2_PROVISIONS) {
		R60_QUALIFY_STAGE2_PROVISIONS = r60_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR60_TOTAL_GENERAL_PROVISIONS() {
		return R60_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR60_TOTAL_GENERAL_PROVISIONS(BigDecimal r60_TOTAL_GENERAL_PROVISIONS) {
		R60_TOTAL_GENERAL_PROVISIONS = r60_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR61_PRODUCT() {
		return R61_PRODUCT;
	}
	public void setR61_PRODUCT(String r61_PRODUCT) {
		R61_PRODUCT = r61_PRODUCT;
	}
	public BigDecimal getR61_STAGE1_PROVISIONS() {
		return R61_STAGE1_PROVISIONS;
	}
	public void setR61_STAGE1_PROVISIONS(BigDecimal r61_STAGE1_PROVISIONS) {
		R61_STAGE1_PROVISIONS = r61_STAGE1_PROVISIONS;
	}
	public BigDecimal getR61_QUALIFY_STAGE2_PROVISIONS() {
		return R61_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR61_QUALIFY_STAGE2_PROVISIONS(BigDecimal r61_QUALIFY_STAGE2_PROVISIONS) {
		R61_QUALIFY_STAGE2_PROVISIONS = r61_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR61_TOTAL_GENERAL_PROVISIONS() {
		return R61_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR61_TOTAL_GENERAL_PROVISIONS(BigDecimal r61_TOTAL_GENERAL_PROVISIONS) {
		R61_TOTAL_GENERAL_PROVISIONS = r61_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR62_PRODUCT() {
		return R62_PRODUCT;
	}
	public void setR62_PRODUCT(String r62_PRODUCT) {
		R62_PRODUCT = r62_PRODUCT;
	}
	public BigDecimal getR62_STAGE1_PROVISIONS() {
		return R62_STAGE1_PROVISIONS;
	}
	public void setR62_STAGE1_PROVISIONS(BigDecimal r62_STAGE1_PROVISIONS) {
		R62_STAGE1_PROVISIONS = r62_STAGE1_PROVISIONS;
	}
	public BigDecimal getR62_QUALIFY_STAGE2_PROVISIONS() {
		return R62_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR62_QUALIFY_STAGE2_PROVISIONS(BigDecimal r62_QUALIFY_STAGE2_PROVISIONS) {
		R62_QUALIFY_STAGE2_PROVISIONS = r62_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR62_TOTAL_GENERAL_PROVISIONS() {
		return R62_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR62_TOTAL_GENERAL_PROVISIONS(BigDecimal r62_TOTAL_GENERAL_PROVISIONS) {
		R62_TOTAL_GENERAL_PROVISIONS = r62_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR63_PRODUCT() {
		return R63_PRODUCT;
	}
	public void setR63_PRODUCT(String r63_PRODUCT) {
		R63_PRODUCT = r63_PRODUCT;
	}
	public BigDecimal getR63_STAGE1_PROVISIONS() {
		return R63_STAGE1_PROVISIONS;
	}
	public void setR63_STAGE1_PROVISIONS(BigDecimal r63_STAGE1_PROVISIONS) {
		R63_STAGE1_PROVISIONS = r63_STAGE1_PROVISIONS;
	}
	public BigDecimal getR63_QUALIFY_STAGE2_PROVISIONS() {
		return R63_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR63_QUALIFY_STAGE2_PROVISIONS(BigDecimal r63_QUALIFY_STAGE2_PROVISIONS) {
		R63_QUALIFY_STAGE2_PROVISIONS = r63_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR63_TOTAL_GENERAL_PROVISIONS() {
		return R63_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR63_TOTAL_GENERAL_PROVISIONS(BigDecimal r63_TOTAL_GENERAL_PROVISIONS) {
		R63_TOTAL_GENERAL_PROVISIONS = r63_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR64_PRODUCT() {
		return R64_PRODUCT;
	}
	public void setR64_PRODUCT(String r64_PRODUCT) {
		R64_PRODUCT = r64_PRODUCT;
	}
	public BigDecimal getR64_STAGE1_PROVISIONS() {
		return R64_STAGE1_PROVISIONS;
	}
	public void setR64_STAGE1_PROVISIONS(BigDecimal r64_STAGE1_PROVISIONS) {
		R64_STAGE1_PROVISIONS = r64_STAGE1_PROVISIONS;
	}
	public BigDecimal getR64_QUALIFY_STAGE2_PROVISIONS() {
		return R64_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR64_QUALIFY_STAGE2_PROVISIONS(BigDecimal r64_QUALIFY_STAGE2_PROVISIONS) {
		R64_QUALIFY_STAGE2_PROVISIONS = r64_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR64_TOTAL_GENERAL_PROVISIONS() {
		return R64_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR64_TOTAL_GENERAL_PROVISIONS(BigDecimal r64_TOTAL_GENERAL_PROVISIONS) {
		R64_TOTAL_GENERAL_PROVISIONS = r64_TOTAL_GENERAL_PROVISIONS;
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
// RESUB DETAIL M_GP
//=====================================================

public class M_GP_RESUB_Detail_RowMapper 
        implements RowMapper<M_GP_RESUB_Detail_Entity> {

    @Override
    public M_GP_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_GP_RESUB_Detail_Entity obj = new M_GP_RESUB_Detail_Entity();

// =========================
// R11
// =========================
obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
obj.setR11_STAGE1_PROVISIONS(rs.getBigDecimal("R11_STAGE1_PROVISIONS"));
obj.setR11_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R11_QUALIFY_STAGE2_PROVISIONS"));
obj.setR11_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R11_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R12
// =========================
obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
obj.setR12_STAGE1_PROVISIONS(rs.getBigDecimal("R12_STAGE1_PROVISIONS"));
obj.setR12_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R12_QUALIFY_STAGE2_PROVISIONS"));
obj.setR12_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R12_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R13
// =========================
obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
obj.setR13_STAGE1_PROVISIONS(rs.getBigDecimal("R13_STAGE1_PROVISIONS"));
obj.setR13_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R13_QUALIFY_STAGE2_PROVISIONS"));
obj.setR13_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R13_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R14
// =========================
obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
obj.setR14_STAGE1_PROVISIONS(rs.getBigDecimal("R14_STAGE1_PROVISIONS"));
obj.setR14_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R14_QUALIFY_STAGE2_PROVISIONS"));
obj.setR14_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R14_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R15
// =========================
obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
obj.setR15_STAGE1_PROVISIONS(rs.getBigDecimal("R15_STAGE1_PROVISIONS"));
obj.setR15_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R15_QUALIFY_STAGE2_PROVISIONS"));
obj.setR15_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R15_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R16
// =========================
obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
obj.setR16_STAGE1_PROVISIONS(rs.getBigDecimal("R16_STAGE1_PROVISIONS"));
obj.setR16_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R16_QUALIFY_STAGE2_PROVISIONS"));
obj.setR16_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R16_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R17
// =========================
obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
obj.setR17_STAGE1_PROVISIONS(rs.getBigDecimal("R17_STAGE1_PROVISIONS"));
obj.setR17_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R17_QUALIFY_STAGE2_PROVISIONS"));
obj.setR17_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R17_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R18
// =========================
obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
obj.setR18_STAGE1_PROVISIONS(rs.getBigDecimal("R18_STAGE1_PROVISIONS"));
obj.setR18_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R18_QUALIFY_STAGE2_PROVISIONS"));
obj.setR18_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R18_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R19
// =========================
obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
obj.setR19_STAGE1_PROVISIONS(rs.getBigDecimal("R19_STAGE1_PROVISIONS"));
obj.setR19_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R19_QUALIFY_STAGE2_PROVISIONS"));
obj.setR19_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R19_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R20
// =========================
obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
obj.setR20_STAGE1_PROVISIONS(rs.getBigDecimal("R20_STAGE1_PROVISIONS"));
obj.setR20_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R20_QUALIFY_STAGE2_PROVISIONS"));
obj.setR20_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R20_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R21
// =========================
obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
obj.setR21_STAGE1_PROVISIONS(rs.getBigDecimal("R21_STAGE1_PROVISIONS"));
obj.setR21_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R21_QUALIFY_STAGE2_PROVISIONS"));
obj.setR21_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R21_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R22
// =========================
obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
obj.setR22_STAGE1_PROVISIONS(rs.getBigDecimal("R22_STAGE1_PROVISIONS"));
obj.setR22_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R22_QUALIFY_STAGE2_PROVISIONS"));
obj.setR22_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R22_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R23
// =========================
obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
obj.setR23_STAGE1_PROVISIONS(rs.getBigDecimal("R23_STAGE1_PROVISIONS"));
obj.setR23_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R23_QUALIFY_STAGE2_PROVISIONS"));
obj.setR23_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R23_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R24
// =========================
obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
obj.setR24_STAGE1_PROVISIONS(rs.getBigDecimal("R24_STAGE1_PROVISIONS"));
obj.setR24_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R24_QUALIFY_STAGE2_PROVISIONS"));
obj.setR24_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R24_TOTAL_GENERAL_PROVISIONS"));


// =========================
// R25
// =========================
obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
obj.setR25_STAGE1_PROVISIONS(rs.getBigDecimal("R25_STAGE1_PROVISIONS"));
obj.setR25_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R25_QUALIFY_STAGE2_PROVISIONS"));
obj.setR25_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R25_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R26
// =========================
obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
obj.setR26_STAGE1_PROVISIONS(rs.getBigDecimal("R26_STAGE1_PROVISIONS"));
obj.setR26_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R26_QUALIFY_STAGE2_PROVISIONS"));
obj.setR26_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R26_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R27
// =========================
obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
obj.setR27_STAGE1_PROVISIONS(rs.getBigDecimal("R27_STAGE1_PROVISIONS"));
obj.setR27_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R27_QUALIFY_STAGE2_PROVISIONS"));
obj.setR27_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R27_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R28
// =========================
obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
obj.setR28_STAGE1_PROVISIONS(rs.getBigDecimal("R28_STAGE1_PROVISIONS"));
obj.setR28_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R28_QUALIFY_STAGE2_PROVISIONS"));
obj.setR28_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R28_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R29
// =========================
obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
obj.setR29_STAGE1_PROVISIONS(rs.getBigDecimal("R29_STAGE1_PROVISIONS"));
obj.setR29_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R29_QUALIFY_STAGE2_PROVISIONS"));
obj.setR29_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R29_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R30
// =========================
obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
obj.setR30_STAGE1_PROVISIONS(rs.getBigDecimal("R30_STAGE1_PROVISIONS"));
obj.setR30_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R30_QUALIFY_STAGE2_PROVISIONS"));
obj.setR30_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R30_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R31
// =========================
obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
obj.setR31_STAGE1_PROVISIONS(rs.getBigDecimal("R31_STAGE1_PROVISIONS"));
obj.setR31_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R31_QUALIFY_STAGE2_PROVISIONS"));
obj.setR31_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R31_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R32
// =========================
obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
obj.setR32_STAGE1_PROVISIONS(rs.getBigDecimal("R32_STAGE1_PROVISIONS"));
obj.setR32_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R32_QUALIFY_STAGE2_PROVISIONS"));
obj.setR32_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R32_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R33
// =========================
obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
obj.setR33_STAGE1_PROVISIONS(rs.getBigDecimal("R33_STAGE1_PROVISIONS"));
obj.setR33_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R33_QUALIFY_STAGE2_PROVISIONS"));
obj.setR33_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R33_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R34
// =========================
obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
obj.setR34_STAGE1_PROVISIONS(rs.getBigDecimal("R34_STAGE1_PROVISIONS"));
obj.setR34_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R34_QUALIFY_STAGE2_PROVISIONS"));
obj.setR34_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R34_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R35
// =========================
obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
obj.setR35_STAGE1_PROVISIONS(rs.getBigDecimal("R35_STAGE1_PROVISIONS"));
obj.setR35_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R35_QUALIFY_STAGE2_PROVISIONS"));
obj.setR35_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R35_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R36
// =========================
obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
obj.setR36_STAGE1_PROVISIONS(rs.getBigDecimal("R36_STAGE1_PROVISIONS"));
obj.setR36_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R36_QUALIFY_STAGE2_PROVISIONS"));
obj.setR36_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R36_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R37
// =========================
obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
obj.setR37_STAGE1_PROVISIONS(rs.getBigDecimal("R37_STAGE1_PROVISIONS"));
obj.setR37_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R37_QUALIFY_STAGE2_PROVISIONS"));
obj.setR37_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R37_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R38
// =========================
obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
obj.setR38_STAGE1_PROVISIONS(rs.getBigDecimal("R38_STAGE1_PROVISIONS"));
obj.setR38_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R38_QUALIFY_STAGE2_PROVISIONS"));
obj.setR38_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R38_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R39
// =========================
obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
obj.setR39_STAGE1_PROVISIONS(rs.getBigDecimal("R39_STAGE1_PROVISIONS"));
obj.setR39_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R39_QUALIFY_STAGE2_PROVISIONS"));
obj.setR39_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R39_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R40
// =========================
obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
obj.setR40_STAGE1_PROVISIONS(rs.getBigDecimal("R40_STAGE1_PROVISIONS"));
obj.setR40_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R40_QUALIFY_STAGE2_PROVISIONS"));
obj.setR40_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R40_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R41
// =========================
obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
obj.setR41_STAGE1_PROVISIONS(rs.getBigDecimal("R41_STAGE1_PROVISIONS"));
obj.setR41_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R41_QUALIFY_STAGE2_PROVISIONS"));
obj.setR41_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R41_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R42
// =========================
obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
obj.setR42_STAGE1_PROVISIONS(rs.getBigDecimal("R42_STAGE1_PROVISIONS"));
obj.setR42_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R42_QUALIFY_STAGE2_PROVISIONS"));
obj.setR42_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R42_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R43
// =========================
obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
obj.setR43_STAGE1_PROVISIONS(rs.getBigDecimal("R43_STAGE1_PROVISIONS"));
obj.setR43_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R43_QUALIFY_STAGE2_PROVISIONS"));
obj.setR43_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R43_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R44
// =========================
obj.setR44_PRODUCT(rs.getString("R44_PRODUCT"));
obj.setR44_STAGE1_PROVISIONS(rs.getBigDecimal("R44_STAGE1_PROVISIONS"));
obj.setR44_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R44_QUALIFY_STAGE2_PROVISIONS"));
obj.setR44_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R44_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R45
// =========================
obj.setR45_PRODUCT(rs.getString("R45_PRODUCT"));
obj.setR45_STAGE1_PROVISIONS(rs.getBigDecimal("R45_STAGE1_PROVISIONS"));
obj.setR45_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R45_QUALIFY_STAGE2_PROVISIONS"));
obj.setR45_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R45_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R46
// =========================
obj.setR46_PRODUCT(rs.getString("R46_PRODUCT"));
obj.setR46_STAGE1_PROVISIONS(rs.getBigDecimal("R46_STAGE1_PROVISIONS"));
obj.setR46_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R46_QUALIFY_STAGE2_PROVISIONS"));
obj.setR46_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R46_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R47
// =========================
obj.setR47_PRODUCT(rs.getString("R47_PRODUCT"));
obj.setR47_STAGE1_PROVISIONS(rs.getBigDecimal("R47_STAGE1_PROVISIONS"));
obj.setR47_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R47_QUALIFY_STAGE2_PROVISIONS"));
obj.setR47_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R47_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R48
// =========================
obj.setR48_PRODUCT(rs.getString("R48_PRODUCT"));
obj.setR48_STAGE1_PROVISIONS(rs.getBigDecimal("R48_STAGE1_PROVISIONS"));
obj.setR48_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R48_QUALIFY_STAGE2_PROVISIONS"));
obj.setR48_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R48_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R49
// =========================
obj.setR49_PRODUCT(rs.getString("R49_PRODUCT"));
obj.setR49_STAGE1_PROVISIONS(rs.getBigDecimal("R49_STAGE1_PROVISIONS"));
obj.setR49_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R49_QUALIFY_STAGE2_PROVISIONS"));
obj.setR49_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R49_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R50
// =========================
obj.setR50_PRODUCT(rs.getString("R50_PRODUCT"));
obj.setR50_STAGE1_PROVISIONS(rs.getBigDecimal("R50_STAGE1_PROVISIONS"));
obj.setR50_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R50_QUALIFY_STAGE2_PROVISIONS"));
obj.setR50_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R50_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R51
// =========================
obj.setR51_PRODUCT(rs.getString("R51_PRODUCT"));
obj.setR51_STAGE1_PROVISIONS(rs.getBigDecimal("R51_STAGE1_PROVISIONS"));
obj.setR51_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R51_QUALIFY_STAGE2_PROVISIONS"));
obj.setR51_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R51_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R52
// =========================
obj.setR52_PRODUCT(rs.getString("R52_PRODUCT"));
obj.setR52_STAGE1_PROVISIONS(rs.getBigDecimal("R52_STAGE1_PROVISIONS"));
obj.setR52_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R52_QUALIFY_STAGE2_PROVISIONS"));
obj.setR52_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R52_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R53
// =========================
obj.setR53_PRODUCT(rs.getString("R53_PRODUCT"));
obj.setR53_STAGE1_PROVISIONS(rs.getBigDecimal("R53_STAGE1_PROVISIONS"));
obj.setR53_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R53_QUALIFY_STAGE2_PROVISIONS"));
obj.setR53_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R53_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R54
// =========================
obj.setR54_PRODUCT(rs.getString("R54_PRODUCT"));
obj.setR54_STAGE1_PROVISIONS(rs.getBigDecimal("R54_STAGE1_PROVISIONS"));
obj.setR54_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R54_QUALIFY_STAGE2_PROVISIONS"));
obj.setR54_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R54_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R55
// =========================
obj.setR55_PRODUCT(rs.getString("R55_PRODUCT"));
obj.setR55_STAGE1_PROVISIONS(rs.getBigDecimal("R55_STAGE1_PROVISIONS"));
obj.setR55_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R55_QUALIFY_STAGE2_PROVISIONS"));
obj.setR55_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R55_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R56
// =========================
obj.setR56_PRODUCT(rs.getString("R56_PRODUCT"));
obj.setR56_STAGE1_PROVISIONS(rs.getBigDecimal("R56_STAGE1_PROVISIONS"));
obj.setR56_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R56_QUALIFY_STAGE2_PROVISIONS"));
obj.setR56_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R56_TOTAL_GENERAL_PROVISIONS"));


// =========================
// R57
// =========================
obj.setR57_PRODUCT(rs.getString("R57_PRODUCT"));
obj.setR57_STAGE1_PROVISIONS(rs.getBigDecimal("R57_STAGE1_PROVISIONS"));
obj.setR57_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R57_QUALIFY_STAGE2_PROVISIONS"));
obj.setR57_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R57_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R58
// =========================
obj.setR58_PRODUCT(rs.getString("R58_PRODUCT"));
obj.setR58_STAGE1_PROVISIONS(rs.getBigDecimal("R58_STAGE1_PROVISIONS"));
obj.setR58_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R58_QUALIFY_STAGE2_PROVISIONS"));
obj.setR58_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R58_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R59
// =========================
obj.setR59_PRODUCT(rs.getString("R59_PRODUCT"));
obj.setR59_STAGE1_PROVISIONS(rs.getBigDecimal("R59_STAGE1_PROVISIONS"));
obj.setR59_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R59_QUALIFY_STAGE2_PROVISIONS"));
obj.setR59_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R59_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R60
// =========================
obj.setR60_PRODUCT(rs.getString("R60_PRODUCT"));
obj.setR60_STAGE1_PROVISIONS(rs.getBigDecimal("R60_STAGE1_PROVISIONS"));
obj.setR60_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R60_QUALIFY_STAGE2_PROVISIONS"));
obj.setR60_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R60_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R61
// =========================
obj.setR61_PRODUCT(rs.getString("R61_PRODUCT"));
obj.setR61_STAGE1_PROVISIONS(rs.getBigDecimal("R61_STAGE1_PROVISIONS"));
obj.setR61_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R61_QUALIFY_STAGE2_PROVISIONS"));
obj.setR61_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R61_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R62
// =========================
obj.setR62_PRODUCT(rs.getString("R62_PRODUCT"));
obj.setR62_STAGE1_PROVISIONS(rs.getBigDecimal("R62_STAGE1_PROVISIONS"));
obj.setR62_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R62_QUALIFY_STAGE2_PROVISIONS"));
obj.setR62_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R62_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R63
// =========================
obj.setR63_PRODUCT(rs.getString("R63_PRODUCT"));
obj.setR63_STAGE1_PROVISIONS(rs.getBigDecimal("R63_STAGE1_PROVISIONS"));
obj.setR63_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R63_QUALIFY_STAGE2_PROVISIONS"));
obj.setR63_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R63_TOTAL_GENERAL_PROVISIONS"));

// =========================
// R64
// =========================
obj.setR64_PRODUCT(rs.getString("R64_PRODUCT"));
obj.setR64_STAGE1_PROVISIONS(rs.getBigDecimal("R64_STAGE1_PROVISIONS"));
obj.setR64_QUALIFY_STAGE2_PROVISIONS(rs.getBigDecimal("R64_QUALIFY_STAGE2_PROVISIONS"));
obj.setR64_TOTAL_GENERAL_PROVISIONS(rs.getBigDecimal("R64_TOTAL_GENERAL_PROVISIONS"));


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

public class M_GP_RESUB_Detail_Entity {

   
private String R11_PRODUCT;
	private BigDecimal R11_STAGE1_PROVISIONS;
	private BigDecimal R11_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R11_TOTAL_GENERAL_PROVISIONS;

	private String R12_PRODUCT;
	private BigDecimal R12_STAGE1_PROVISIONS;
	private BigDecimal R12_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R12_TOTAL_GENERAL_PROVISIONS;

	private String R13_PRODUCT;
	private BigDecimal R13_STAGE1_PROVISIONS;
	private BigDecimal R13_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R13_TOTAL_GENERAL_PROVISIONS;

	private String R14_PRODUCT;
	private BigDecimal R14_STAGE1_PROVISIONS;
	private BigDecimal R14_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R14_TOTAL_GENERAL_PROVISIONS;

	private String R15_PRODUCT;
	private BigDecimal R15_STAGE1_PROVISIONS;
	private BigDecimal R15_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R15_TOTAL_GENERAL_PROVISIONS;

	private String R16_PRODUCT;
	private BigDecimal R16_STAGE1_PROVISIONS;
	private BigDecimal R16_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R16_TOTAL_GENERAL_PROVISIONS;

	private String R17_PRODUCT;
	private BigDecimal R17_STAGE1_PROVISIONS;
	private BigDecimal R17_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R17_TOTAL_GENERAL_PROVISIONS;

	private String R18_PRODUCT;
	private BigDecimal R18_STAGE1_PROVISIONS;
	private BigDecimal R18_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R18_TOTAL_GENERAL_PROVISIONS;

	private String R19_PRODUCT;
	private BigDecimal R19_STAGE1_PROVISIONS;
	private BigDecimal R19_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R19_TOTAL_GENERAL_PROVISIONS;

	private String R20_PRODUCT;
	private BigDecimal R20_STAGE1_PROVISIONS;
	private BigDecimal R20_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R20_TOTAL_GENERAL_PROVISIONS;

	private String R21_PRODUCT;
	private BigDecimal R21_STAGE1_PROVISIONS;
	private BigDecimal R21_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R21_TOTAL_GENERAL_PROVISIONS;

	private String R22_PRODUCT;
	private BigDecimal R22_STAGE1_PROVISIONS;
	private BigDecimal R22_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R22_TOTAL_GENERAL_PROVISIONS;

	private String R23_PRODUCT;
	private BigDecimal R23_STAGE1_PROVISIONS;
	private BigDecimal R23_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R23_TOTAL_GENERAL_PROVISIONS;

	private String R24_PRODUCT;
	private BigDecimal R24_STAGE1_PROVISIONS;
	private BigDecimal R24_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R24_TOTAL_GENERAL_PROVISIONS;

	private String R25_PRODUCT;
	private BigDecimal R25_STAGE1_PROVISIONS;
	private BigDecimal R25_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R25_TOTAL_GENERAL_PROVISIONS;

	private String R26_PRODUCT;
	private BigDecimal R26_STAGE1_PROVISIONS;
	private BigDecimal R26_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R26_TOTAL_GENERAL_PROVISIONS;

	private String R27_PRODUCT;
	private BigDecimal R27_STAGE1_PROVISIONS;
	private BigDecimal R27_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R27_TOTAL_GENERAL_PROVISIONS;

	private String R28_PRODUCT;
	private BigDecimal R28_STAGE1_PROVISIONS;
	private BigDecimal R28_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R28_TOTAL_GENERAL_PROVISIONS;

	private String R29_PRODUCT;
	private BigDecimal R29_STAGE1_PROVISIONS;
	private BigDecimal R29_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R29_TOTAL_GENERAL_PROVISIONS;

	private String R30_PRODUCT;
	private BigDecimal R30_STAGE1_PROVISIONS;
	private BigDecimal R30_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R30_TOTAL_GENERAL_PROVISIONS;

	private String R31_PRODUCT;
	private BigDecimal R31_STAGE1_PROVISIONS;
	private BigDecimal R31_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R31_TOTAL_GENERAL_PROVISIONS;

	private String R32_PRODUCT;
	private BigDecimal R32_STAGE1_PROVISIONS;
	private BigDecimal R32_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R32_TOTAL_GENERAL_PROVISIONS;

	private String R33_PRODUCT;
	private BigDecimal R33_STAGE1_PROVISIONS;
	private BigDecimal R33_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R33_TOTAL_GENERAL_PROVISIONS;

	private String R34_PRODUCT;
	private BigDecimal R34_STAGE1_PROVISIONS;
	private BigDecimal R34_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R34_TOTAL_GENERAL_PROVISIONS;

	private String R35_PRODUCT;
	private BigDecimal R35_STAGE1_PROVISIONS;
	private BigDecimal R35_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R35_TOTAL_GENERAL_PROVISIONS;

	private String R36_PRODUCT;
	private BigDecimal R36_STAGE1_PROVISIONS;
	private BigDecimal R36_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R36_TOTAL_GENERAL_PROVISIONS;

	private String R37_PRODUCT;
	private BigDecimal R37_STAGE1_PROVISIONS;
	private BigDecimal R37_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R37_TOTAL_GENERAL_PROVISIONS;

	private String R38_PRODUCT;
	private BigDecimal R38_STAGE1_PROVISIONS;
	private BigDecimal R38_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R38_TOTAL_GENERAL_PROVISIONS;

	private String R39_PRODUCT;
	private BigDecimal R39_STAGE1_PROVISIONS;
	private BigDecimal R39_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R39_TOTAL_GENERAL_PROVISIONS;

	private String R40_PRODUCT;
	private BigDecimal R40_STAGE1_PROVISIONS;
	private BigDecimal R40_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R40_TOTAL_GENERAL_PROVISIONS;

	private String R41_PRODUCT;
	private BigDecimal R41_STAGE1_PROVISIONS;
	private BigDecimal R41_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R41_TOTAL_GENERAL_PROVISIONS;

	private String R42_PRODUCT;
	private BigDecimal R42_STAGE1_PROVISIONS;
	private BigDecimal R42_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R42_TOTAL_GENERAL_PROVISIONS;

	private String R43_PRODUCT;
	private BigDecimal R43_STAGE1_PROVISIONS;
	private BigDecimal R43_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R43_TOTAL_GENERAL_PROVISIONS;

	private String R44_PRODUCT;
	private BigDecimal R44_STAGE1_PROVISIONS;
	private BigDecimal R44_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R44_TOTAL_GENERAL_PROVISIONS;

	private String R45_PRODUCT;
	private BigDecimal R45_STAGE1_PROVISIONS;
	private BigDecimal R45_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R45_TOTAL_GENERAL_PROVISIONS;

	private String R46_PRODUCT;
	private BigDecimal R46_STAGE1_PROVISIONS;
	private BigDecimal R46_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R46_TOTAL_GENERAL_PROVISIONS;

	private String R47_PRODUCT;
	private BigDecimal R47_STAGE1_PROVISIONS;
	private BigDecimal R47_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R47_TOTAL_GENERAL_PROVISIONS;

	private String R48_PRODUCT;
	private BigDecimal R48_STAGE1_PROVISIONS;
	private BigDecimal R48_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R48_TOTAL_GENERAL_PROVISIONS;

	private String R49_PRODUCT;
	private BigDecimal R49_STAGE1_PROVISIONS;
	private BigDecimal R49_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R49_TOTAL_GENERAL_PROVISIONS;

	private String R50_PRODUCT;
	private BigDecimal R50_STAGE1_PROVISIONS;
	private BigDecimal R50_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R50_TOTAL_GENERAL_PROVISIONS;

	private String R51_PRODUCT;
	private BigDecimal R51_STAGE1_PROVISIONS;
	private BigDecimal R51_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R51_TOTAL_GENERAL_PROVISIONS;

	private String R52_PRODUCT;
	private BigDecimal R52_STAGE1_PROVISIONS;
	private BigDecimal R52_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R52_TOTAL_GENERAL_PROVISIONS;

	private String R53_PRODUCT;
	private BigDecimal R53_STAGE1_PROVISIONS;
	private BigDecimal R53_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R53_TOTAL_GENERAL_PROVISIONS;

	private String R54_PRODUCT;
	private BigDecimal R54_STAGE1_PROVISIONS;
	private BigDecimal R54_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R54_TOTAL_GENERAL_PROVISIONS;

	private String R55_PRODUCT;
	private BigDecimal R55_STAGE1_PROVISIONS;
	private BigDecimal R55_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R55_TOTAL_GENERAL_PROVISIONS;

	private String R56_PRODUCT;
	private BigDecimal R56_STAGE1_PROVISIONS;
	private BigDecimal R56_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R56_TOTAL_GENERAL_PROVISIONS;

	private String R57_PRODUCT;
	private BigDecimal R57_STAGE1_PROVISIONS;
	private BigDecimal R57_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R57_TOTAL_GENERAL_PROVISIONS;

	private String R58_PRODUCT;
	private BigDecimal R58_STAGE1_PROVISIONS;
	private BigDecimal R58_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R58_TOTAL_GENERAL_PROVISIONS;

	private String R59_PRODUCT;
	private BigDecimal R59_STAGE1_PROVISIONS;
	private BigDecimal R59_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R59_TOTAL_GENERAL_PROVISIONS;

	private String R60_PRODUCT;
	private BigDecimal R60_STAGE1_PROVISIONS;
	private BigDecimal R60_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R60_TOTAL_GENERAL_PROVISIONS;

	private String R61_PRODUCT;
	private BigDecimal R61_STAGE1_PROVISIONS;
	private BigDecimal R61_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R61_TOTAL_GENERAL_PROVISIONS;

	private String R62_PRODUCT;
	private BigDecimal R62_STAGE1_PROVISIONS;
	private BigDecimal R62_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R62_TOTAL_GENERAL_PROVISIONS;

	private String R63_PRODUCT;
	private BigDecimal R63_STAGE1_PROVISIONS;
	private BigDecimal R63_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R63_TOTAL_GENERAL_PROVISIONS;

	private String R64_PRODUCT;
	private BigDecimal R64_STAGE1_PROVISIONS;
	private BigDecimal R64_QUALIFY_STAGE2_PROVISIONS;
	private BigDecimal R64_TOTAL_GENERAL_PROVISIONS;
	
	
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
	
	
	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_STAGE1_PROVISIONS() {
		return R11_STAGE1_PROVISIONS;
	}
	public void setR11_STAGE1_PROVISIONS(BigDecimal r11_STAGE1_PROVISIONS) {
		R11_STAGE1_PROVISIONS = r11_STAGE1_PROVISIONS;
	}
	public BigDecimal getR11_QUALIFY_STAGE2_PROVISIONS() {
		return R11_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR11_QUALIFY_STAGE2_PROVISIONS(BigDecimal r11_QUALIFY_STAGE2_PROVISIONS) {
		R11_QUALIFY_STAGE2_PROVISIONS = r11_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR11_TOTAL_GENERAL_PROVISIONS() {
		return R11_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR11_TOTAL_GENERAL_PROVISIONS(BigDecimal r11_TOTAL_GENERAL_PROVISIONS) {
		R11_TOTAL_GENERAL_PROVISIONS = r11_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_STAGE1_PROVISIONS() {
		return R12_STAGE1_PROVISIONS;
	}
	public void setR12_STAGE1_PROVISIONS(BigDecimal r12_STAGE1_PROVISIONS) {
		R12_STAGE1_PROVISIONS = r12_STAGE1_PROVISIONS;
	}
	public BigDecimal getR12_QUALIFY_STAGE2_PROVISIONS() {
		return R12_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR12_QUALIFY_STAGE2_PROVISIONS(BigDecimal r12_QUALIFY_STAGE2_PROVISIONS) {
		R12_QUALIFY_STAGE2_PROVISIONS = r12_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR12_TOTAL_GENERAL_PROVISIONS() {
		return R12_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR12_TOTAL_GENERAL_PROVISIONS(BigDecimal r12_TOTAL_GENERAL_PROVISIONS) {
		R12_TOTAL_GENERAL_PROVISIONS = r12_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_STAGE1_PROVISIONS() {
		return R13_STAGE1_PROVISIONS;
	}
	public void setR13_STAGE1_PROVISIONS(BigDecimal r13_STAGE1_PROVISIONS) {
		R13_STAGE1_PROVISIONS = r13_STAGE1_PROVISIONS;
	}
	public BigDecimal getR13_QUALIFY_STAGE2_PROVISIONS() {
		return R13_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR13_QUALIFY_STAGE2_PROVISIONS(BigDecimal r13_QUALIFY_STAGE2_PROVISIONS) {
		R13_QUALIFY_STAGE2_PROVISIONS = r13_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR13_TOTAL_GENERAL_PROVISIONS() {
		return R13_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR13_TOTAL_GENERAL_PROVISIONS(BigDecimal r13_TOTAL_GENERAL_PROVISIONS) {
		R13_TOTAL_GENERAL_PROVISIONS = r13_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_STAGE1_PROVISIONS() {
		return R14_STAGE1_PROVISIONS;
	}
	public void setR14_STAGE1_PROVISIONS(BigDecimal r14_STAGE1_PROVISIONS) {
		R14_STAGE1_PROVISIONS = r14_STAGE1_PROVISIONS;
	}
	public BigDecimal getR14_QUALIFY_STAGE2_PROVISIONS() {
		return R14_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR14_QUALIFY_STAGE2_PROVISIONS(BigDecimal r14_QUALIFY_STAGE2_PROVISIONS) {
		R14_QUALIFY_STAGE2_PROVISIONS = r14_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR14_TOTAL_GENERAL_PROVISIONS() {
		return R14_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR14_TOTAL_GENERAL_PROVISIONS(BigDecimal r14_TOTAL_GENERAL_PROVISIONS) {
		R14_TOTAL_GENERAL_PROVISIONS = r14_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_STAGE1_PROVISIONS() {
		return R15_STAGE1_PROVISIONS;
	}
	public void setR15_STAGE1_PROVISIONS(BigDecimal r15_STAGE1_PROVISIONS) {
		R15_STAGE1_PROVISIONS = r15_STAGE1_PROVISIONS;
	}
	public BigDecimal getR15_QUALIFY_STAGE2_PROVISIONS() {
		return R15_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR15_QUALIFY_STAGE2_PROVISIONS(BigDecimal r15_QUALIFY_STAGE2_PROVISIONS) {
		R15_QUALIFY_STAGE2_PROVISIONS = r15_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR15_TOTAL_GENERAL_PROVISIONS() {
		return R15_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR15_TOTAL_GENERAL_PROVISIONS(BigDecimal r15_TOTAL_GENERAL_PROVISIONS) {
		R15_TOTAL_GENERAL_PROVISIONS = r15_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_STAGE1_PROVISIONS() {
		return R16_STAGE1_PROVISIONS;
	}
	public void setR16_STAGE1_PROVISIONS(BigDecimal r16_STAGE1_PROVISIONS) {
		R16_STAGE1_PROVISIONS = r16_STAGE1_PROVISIONS;
	}
	public BigDecimal getR16_QUALIFY_STAGE2_PROVISIONS() {
		return R16_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR16_QUALIFY_STAGE2_PROVISIONS(BigDecimal r16_QUALIFY_STAGE2_PROVISIONS) {
		R16_QUALIFY_STAGE2_PROVISIONS = r16_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR16_TOTAL_GENERAL_PROVISIONS() {
		return R16_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR16_TOTAL_GENERAL_PROVISIONS(BigDecimal r16_TOTAL_GENERAL_PROVISIONS) {
		R16_TOTAL_GENERAL_PROVISIONS = r16_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_STAGE1_PROVISIONS() {
		return R17_STAGE1_PROVISIONS;
	}
	public void setR17_STAGE1_PROVISIONS(BigDecimal r17_STAGE1_PROVISIONS) {
		R17_STAGE1_PROVISIONS = r17_STAGE1_PROVISIONS;
	}
	public BigDecimal getR17_QUALIFY_STAGE2_PROVISIONS() {
		return R17_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR17_QUALIFY_STAGE2_PROVISIONS(BigDecimal r17_QUALIFY_STAGE2_PROVISIONS) {
		R17_QUALIFY_STAGE2_PROVISIONS = r17_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR17_TOTAL_GENERAL_PROVISIONS() {
		return R17_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR17_TOTAL_GENERAL_PROVISIONS(BigDecimal r17_TOTAL_GENERAL_PROVISIONS) {
		R17_TOTAL_GENERAL_PROVISIONS = r17_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}
	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}
	public BigDecimal getR18_STAGE1_PROVISIONS() {
		return R18_STAGE1_PROVISIONS;
	}
	public void setR18_STAGE1_PROVISIONS(BigDecimal r18_STAGE1_PROVISIONS) {
		R18_STAGE1_PROVISIONS = r18_STAGE1_PROVISIONS;
	}
	public BigDecimal getR18_QUALIFY_STAGE2_PROVISIONS() {
		return R18_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR18_QUALIFY_STAGE2_PROVISIONS(BigDecimal r18_QUALIFY_STAGE2_PROVISIONS) {
		R18_QUALIFY_STAGE2_PROVISIONS = r18_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR18_TOTAL_GENERAL_PROVISIONS() {
		return R18_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR18_TOTAL_GENERAL_PROVISIONS(BigDecimal r18_TOTAL_GENERAL_PROVISIONS) {
		R18_TOTAL_GENERAL_PROVISIONS = r18_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}
	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}
	public BigDecimal getR19_STAGE1_PROVISIONS() {
		return R19_STAGE1_PROVISIONS;
	}
	public void setR19_STAGE1_PROVISIONS(BigDecimal r19_STAGE1_PROVISIONS) {
		R19_STAGE1_PROVISIONS = r19_STAGE1_PROVISIONS;
	}
	public BigDecimal getR19_QUALIFY_STAGE2_PROVISIONS() {
		return R19_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR19_QUALIFY_STAGE2_PROVISIONS(BigDecimal r19_QUALIFY_STAGE2_PROVISIONS) {
		R19_QUALIFY_STAGE2_PROVISIONS = r19_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR19_TOTAL_GENERAL_PROVISIONS() {
		return R19_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR19_TOTAL_GENERAL_PROVISIONS(BigDecimal r19_TOTAL_GENERAL_PROVISIONS) {
		R19_TOTAL_GENERAL_PROVISIONS = r19_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}
	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}
	public BigDecimal getR20_STAGE1_PROVISIONS() {
		return R20_STAGE1_PROVISIONS;
	}
	public void setR20_STAGE1_PROVISIONS(BigDecimal r20_STAGE1_PROVISIONS) {
		R20_STAGE1_PROVISIONS = r20_STAGE1_PROVISIONS;
	}
	public BigDecimal getR20_QUALIFY_STAGE2_PROVISIONS() {
		return R20_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR20_QUALIFY_STAGE2_PROVISIONS(BigDecimal r20_QUALIFY_STAGE2_PROVISIONS) {
		R20_QUALIFY_STAGE2_PROVISIONS = r20_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR20_TOTAL_GENERAL_PROVISIONS() {
		return R20_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR20_TOTAL_GENERAL_PROVISIONS(BigDecimal r20_TOTAL_GENERAL_PROVISIONS) {
		R20_TOTAL_GENERAL_PROVISIONS = r20_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}
	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}
	public BigDecimal getR21_STAGE1_PROVISIONS() {
		return R21_STAGE1_PROVISIONS;
	}
	public void setR21_STAGE1_PROVISIONS(BigDecimal r21_STAGE1_PROVISIONS) {
		R21_STAGE1_PROVISIONS = r21_STAGE1_PROVISIONS;
	}
	public BigDecimal getR21_QUALIFY_STAGE2_PROVISIONS() {
		return R21_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR21_QUALIFY_STAGE2_PROVISIONS(BigDecimal r21_QUALIFY_STAGE2_PROVISIONS) {
		R21_QUALIFY_STAGE2_PROVISIONS = r21_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR21_TOTAL_GENERAL_PROVISIONS() {
		return R21_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR21_TOTAL_GENERAL_PROVISIONS(BigDecimal r21_TOTAL_GENERAL_PROVISIONS) {
		R21_TOTAL_GENERAL_PROVISIONS = r21_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}
	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}
	public BigDecimal getR22_STAGE1_PROVISIONS() {
		return R22_STAGE1_PROVISIONS;
	}
	public void setR22_STAGE1_PROVISIONS(BigDecimal r22_STAGE1_PROVISIONS) {
		R22_STAGE1_PROVISIONS = r22_STAGE1_PROVISIONS;
	}
	public BigDecimal getR22_QUALIFY_STAGE2_PROVISIONS() {
		return R22_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR22_QUALIFY_STAGE2_PROVISIONS(BigDecimal r22_QUALIFY_STAGE2_PROVISIONS) {
		R22_QUALIFY_STAGE2_PROVISIONS = r22_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR22_TOTAL_GENERAL_PROVISIONS() {
		return R22_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR22_TOTAL_GENERAL_PROVISIONS(BigDecimal r22_TOTAL_GENERAL_PROVISIONS) {
		R22_TOTAL_GENERAL_PROVISIONS = r22_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}
	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}
	public BigDecimal getR23_STAGE1_PROVISIONS() {
		return R23_STAGE1_PROVISIONS;
	}
	public void setR23_STAGE1_PROVISIONS(BigDecimal r23_STAGE1_PROVISIONS) {
		R23_STAGE1_PROVISIONS = r23_STAGE1_PROVISIONS;
	}
	public BigDecimal getR23_QUALIFY_STAGE2_PROVISIONS() {
		return R23_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR23_QUALIFY_STAGE2_PROVISIONS(BigDecimal r23_QUALIFY_STAGE2_PROVISIONS) {
		R23_QUALIFY_STAGE2_PROVISIONS = r23_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR23_TOTAL_GENERAL_PROVISIONS() {
		return R23_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR23_TOTAL_GENERAL_PROVISIONS(BigDecimal r23_TOTAL_GENERAL_PROVISIONS) {
		R23_TOTAL_GENERAL_PROVISIONS = r23_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}
	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}
	public BigDecimal getR24_STAGE1_PROVISIONS() {
		return R24_STAGE1_PROVISIONS;
	}
	public void setR24_STAGE1_PROVISIONS(BigDecimal r24_STAGE1_PROVISIONS) {
		R24_STAGE1_PROVISIONS = r24_STAGE1_PROVISIONS;
	}
	public BigDecimal getR24_QUALIFY_STAGE2_PROVISIONS() {
		return R24_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR24_QUALIFY_STAGE2_PROVISIONS(BigDecimal r24_QUALIFY_STAGE2_PROVISIONS) {
		R24_QUALIFY_STAGE2_PROVISIONS = r24_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR24_TOTAL_GENERAL_PROVISIONS() {
		return R24_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR24_TOTAL_GENERAL_PROVISIONS(BigDecimal r24_TOTAL_GENERAL_PROVISIONS) {
		R24_TOTAL_GENERAL_PROVISIONS = r24_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}
	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}
	public BigDecimal getR25_STAGE1_PROVISIONS() {
		return R25_STAGE1_PROVISIONS;
	}
	public void setR25_STAGE1_PROVISIONS(BigDecimal r25_STAGE1_PROVISIONS) {
		R25_STAGE1_PROVISIONS = r25_STAGE1_PROVISIONS;
	}
	public BigDecimal getR25_QUALIFY_STAGE2_PROVISIONS() {
		return R25_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR25_QUALIFY_STAGE2_PROVISIONS(BigDecimal r25_QUALIFY_STAGE2_PROVISIONS) {
		R25_QUALIFY_STAGE2_PROVISIONS = r25_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR25_TOTAL_GENERAL_PROVISIONS() {
		return R25_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR25_TOTAL_GENERAL_PROVISIONS(BigDecimal r25_TOTAL_GENERAL_PROVISIONS) {
		R25_TOTAL_GENERAL_PROVISIONS = r25_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}
	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}
	public BigDecimal getR26_STAGE1_PROVISIONS() {
		return R26_STAGE1_PROVISIONS;
	}
	public void setR26_STAGE1_PROVISIONS(BigDecimal r26_STAGE1_PROVISIONS) {
		R26_STAGE1_PROVISIONS = r26_STAGE1_PROVISIONS;
	}
	public BigDecimal getR26_QUALIFY_STAGE2_PROVISIONS() {
		return R26_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR26_QUALIFY_STAGE2_PROVISIONS(BigDecimal r26_QUALIFY_STAGE2_PROVISIONS) {
		R26_QUALIFY_STAGE2_PROVISIONS = r26_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR26_TOTAL_GENERAL_PROVISIONS() {
		return R26_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR26_TOTAL_GENERAL_PROVISIONS(BigDecimal r26_TOTAL_GENERAL_PROVISIONS) {
		R26_TOTAL_GENERAL_PROVISIONS = r26_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}
	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}
	public BigDecimal getR27_STAGE1_PROVISIONS() {
		return R27_STAGE1_PROVISIONS;
	}
	public void setR27_STAGE1_PROVISIONS(BigDecimal r27_STAGE1_PROVISIONS) {
		R27_STAGE1_PROVISIONS = r27_STAGE1_PROVISIONS;
	}
	public BigDecimal getR27_QUALIFY_STAGE2_PROVISIONS() {
		return R27_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR27_QUALIFY_STAGE2_PROVISIONS(BigDecimal r27_QUALIFY_STAGE2_PROVISIONS) {
		R27_QUALIFY_STAGE2_PROVISIONS = r27_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR27_TOTAL_GENERAL_PROVISIONS() {
		return R27_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR27_TOTAL_GENERAL_PROVISIONS(BigDecimal r27_TOTAL_GENERAL_PROVISIONS) {
		R27_TOTAL_GENERAL_PROVISIONS = r27_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}
	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}
	public BigDecimal getR28_STAGE1_PROVISIONS() {
		return R28_STAGE1_PROVISIONS;
	}
	public void setR28_STAGE1_PROVISIONS(BigDecimal r28_STAGE1_PROVISIONS) {
		R28_STAGE1_PROVISIONS = r28_STAGE1_PROVISIONS;
	}
	public BigDecimal getR28_QUALIFY_STAGE2_PROVISIONS() {
		return R28_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR28_QUALIFY_STAGE2_PROVISIONS(BigDecimal r28_QUALIFY_STAGE2_PROVISIONS) {
		R28_QUALIFY_STAGE2_PROVISIONS = r28_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR28_TOTAL_GENERAL_PROVISIONS() {
		return R28_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR28_TOTAL_GENERAL_PROVISIONS(BigDecimal r28_TOTAL_GENERAL_PROVISIONS) {
		R28_TOTAL_GENERAL_PROVISIONS = r28_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}
	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}
	public BigDecimal getR29_STAGE1_PROVISIONS() {
		return R29_STAGE1_PROVISIONS;
	}
	public void setR29_STAGE1_PROVISIONS(BigDecimal r29_STAGE1_PROVISIONS) {
		R29_STAGE1_PROVISIONS = r29_STAGE1_PROVISIONS;
	}
	public BigDecimal getR29_QUALIFY_STAGE2_PROVISIONS() {
		return R29_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR29_QUALIFY_STAGE2_PROVISIONS(BigDecimal r29_QUALIFY_STAGE2_PROVISIONS) {
		R29_QUALIFY_STAGE2_PROVISIONS = r29_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR29_TOTAL_GENERAL_PROVISIONS() {
		return R29_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR29_TOTAL_GENERAL_PROVISIONS(BigDecimal r29_TOTAL_GENERAL_PROVISIONS) {
		R29_TOTAL_GENERAL_PROVISIONS = r29_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}
	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}
	public BigDecimal getR30_STAGE1_PROVISIONS() {
		return R30_STAGE1_PROVISIONS;
	}
	public void setR30_STAGE1_PROVISIONS(BigDecimal r30_STAGE1_PROVISIONS) {
		R30_STAGE1_PROVISIONS = r30_STAGE1_PROVISIONS;
	}
	public BigDecimal getR30_QUALIFY_STAGE2_PROVISIONS() {
		return R30_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR30_QUALIFY_STAGE2_PROVISIONS(BigDecimal r30_QUALIFY_STAGE2_PROVISIONS) {
		R30_QUALIFY_STAGE2_PROVISIONS = r30_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR30_TOTAL_GENERAL_PROVISIONS() {
		return R30_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR30_TOTAL_GENERAL_PROVISIONS(BigDecimal r30_TOTAL_GENERAL_PROVISIONS) {
		R30_TOTAL_GENERAL_PROVISIONS = r30_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}
	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}
	public BigDecimal getR31_STAGE1_PROVISIONS() {
		return R31_STAGE1_PROVISIONS;
	}
	public void setR31_STAGE1_PROVISIONS(BigDecimal r31_STAGE1_PROVISIONS) {
		R31_STAGE1_PROVISIONS = r31_STAGE1_PROVISIONS;
	}
	public BigDecimal getR31_QUALIFY_STAGE2_PROVISIONS() {
		return R31_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR31_QUALIFY_STAGE2_PROVISIONS(BigDecimal r31_QUALIFY_STAGE2_PROVISIONS) {
		R31_QUALIFY_STAGE2_PROVISIONS = r31_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR31_TOTAL_GENERAL_PROVISIONS() {
		return R31_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR31_TOTAL_GENERAL_PROVISIONS(BigDecimal r31_TOTAL_GENERAL_PROVISIONS) {
		R31_TOTAL_GENERAL_PROVISIONS = r31_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}
	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}
	public BigDecimal getR32_STAGE1_PROVISIONS() {
		return R32_STAGE1_PROVISIONS;
	}
	public void setR32_STAGE1_PROVISIONS(BigDecimal r32_STAGE1_PROVISIONS) {
		R32_STAGE1_PROVISIONS = r32_STAGE1_PROVISIONS;
	}
	public BigDecimal getR32_QUALIFY_STAGE2_PROVISIONS() {
		return R32_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR32_QUALIFY_STAGE2_PROVISIONS(BigDecimal r32_QUALIFY_STAGE2_PROVISIONS) {
		R32_QUALIFY_STAGE2_PROVISIONS = r32_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR32_TOTAL_GENERAL_PROVISIONS() {
		return R32_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR32_TOTAL_GENERAL_PROVISIONS(BigDecimal r32_TOTAL_GENERAL_PROVISIONS) {
		R32_TOTAL_GENERAL_PROVISIONS = r32_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}
	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}
	public BigDecimal getR33_STAGE1_PROVISIONS() {
		return R33_STAGE1_PROVISIONS;
	}
	public void setR33_STAGE1_PROVISIONS(BigDecimal r33_STAGE1_PROVISIONS) {
		R33_STAGE1_PROVISIONS = r33_STAGE1_PROVISIONS;
	}
	public BigDecimal getR33_QUALIFY_STAGE2_PROVISIONS() {
		return R33_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR33_QUALIFY_STAGE2_PROVISIONS(BigDecimal r33_QUALIFY_STAGE2_PROVISIONS) {
		R33_QUALIFY_STAGE2_PROVISIONS = r33_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR33_TOTAL_GENERAL_PROVISIONS() {
		return R33_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR33_TOTAL_GENERAL_PROVISIONS(BigDecimal r33_TOTAL_GENERAL_PROVISIONS) {
		R33_TOTAL_GENERAL_PROVISIONS = r33_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}
	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}
	public BigDecimal getR34_STAGE1_PROVISIONS() {
		return R34_STAGE1_PROVISIONS;
	}
	public void setR34_STAGE1_PROVISIONS(BigDecimal r34_STAGE1_PROVISIONS) {
		R34_STAGE1_PROVISIONS = r34_STAGE1_PROVISIONS;
	}
	public BigDecimal getR34_QUALIFY_STAGE2_PROVISIONS() {
		return R34_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR34_QUALIFY_STAGE2_PROVISIONS(BigDecimal r34_QUALIFY_STAGE2_PROVISIONS) {
		R34_QUALIFY_STAGE2_PROVISIONS = r34_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR34_TOTAL_GENERAL_PROVISIONS() {
		return R34_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR34_TOTAL_GENERAL_PROVISIONS(BigDecimal r34_TOTAL_GENERAL_PROVISIONS) {
		R34_TOTAL_GENERAL_PROVISIONS = r34_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}
	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}
	public BigDecimal getR35_STAGE1_PROVISIONS() {
		return R35_STAGE1_PROVISIONS;
	}
	public void setR35_STAGE1_PROVISIONS(BigDecimal r35_STAGE1_PROVISIONS) {
		R35_STAGE1_PROVISIONS = r35_STAGE1_PROVISIONS;
	}
	public BigDecimal getR35_QUALIFY_STAGE2_PROVISIONS() {
		return R35_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR35_QUALIFY_STAGE2_PROVISIONS(BigDecimal r35_QUALIFY_STAGE2_PROVISIONS) {
		R35_QUALIFY_STAGE2_PROVISIONS = r35_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR35_TOTAL_GENERAL_PROVISIONS() {
		return R35_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR35_TOTAL_GENERAL_PROVISIONS(BigDecimal r35_TOTAL_GENERAL_PROVISIONS) {
		R35_TOTAL_GENERAL_PROVISIONS = r35_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_STAGE1_PROVISIONS() {
		return R36_STAGE1_PROVISIONS;
	}
	public void setR36_STAGE1_PROVISIONS(BigDecimal r36_STAGE1_PROVISIONS) {
		R36_STAGE1_PROVISIONS = r36_STAGE1_PROVISIONS;
	}
	public BigDecimal getR36_QUALIFY_STAGE2_PROVISIONS() {
		return R36_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR36_QUALIFY_STAGE2_PROVISIONS(BigDecimal r36_QUALIFY_STAGE2_PROVISIONS) {
		R36_QUALIFY_STAGE2_PROVISIONS = r36_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR36_TOTAL_GENERAL_PROVISIONS() {
		return R36_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR36_TOTAL_GENERAL_PROVISIONS(BigDecimal r36_TOTAL_GENERAL_PROVISIONS) {
		R36_TOTAL_GENERAL_PROVISIONS = r36_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}
	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}
	public BigDecimal getR37_STAGE1_PROVISIONS() {
		return R37_STAGE1_PROVISIONS;
	}
	public void setR37_STAGE1_PROVISIONS(BigDecimal r37_STAGE1_PROVISIONS) {
		R37_STAGE1_PROVISIONS = r37_STAGE1_PROVISIONS;
	}
	public BigDecimal getR37_QUALIFY_STAGE2_PROVISIONS() {
		return R37_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR37_QUALIFY_STAGE2_PROVISIONS(BigDecimal r37_QUALIFY_STAGE2_PROVISIONS) {
		R37_QUALIFY_STAGE2_PROVISIONS = r37_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR37_TOTAL_GENERAL_PROVISIONS() {
		return R37_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR37_TOTAL_GENERAL_PROVISIONS(BigDecimal r37_TOTAL_GENERAL_PROVISIONS) {
		R37_TOTAL_GENERAL_PROVISIONS = r37_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}
	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}
	public BigDecimal getR38_STAGE1_PROVISIONS() {
		return R38_STAGE1_PROVISIONS;
	}
	public void setR38_STAGE1_PROVISIONS(BigDecimal r38_STAGE1_PROVISIONS) {
		R38_STAGE1_PROVISIONS = r38_STAGE1_PROVISIONS;
	}
	public BigDecimal getR38_QUALIFY_STAGE2_PROVISIONS() {
		return R38_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR38_QUALIFY_STAGE2_PROVISIONS(BigDecimal r38_QUALIFY_STAGE2_PROVISIONS) {
		R38_QUALIFY_STAGE2_PROVISIONS = r38_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR38_TOTAL_GENERAL_PROVISIONS() {
		return R38_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR38_TOTAL_GENERAL_PROVISIONS(BigDecimal r38_TOTAL_GENERAL_PROVISIONS) {
		R38_TOTAL_GENERAL_PROVISIONS = r38_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}
	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}
	public BigDecimal getR39_STAGE1_PROVISIONS() {
		return R39_STAGE1_PROVISIONS;
	}
	public void setR39_STAGE1_PROVISIONS(BigDecimal r39_STAGE1_PROVISIONS) {
		R39_STAGE1_PROVISIONS = r39_STAGE1_PROVISIONS;
	}
	public BigDecimal getR39_QUALIFY_STAGE2_PROVISIONS() {
		return R39_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR39_QUALIFY_STAGE2_PROVISIONS(BigDecimal r39_QUALIFY_STAGE2_PROVISIONS) {
		R39_QUALIFY_STAGE2_PROVISIONS = r39_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR39_TOTAL_GENERAL_PROVISIONS() {
		return R39_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR39_TOTAL_GENERAL_PROVISIONS(BigDecimal r39_TOTAL_GENERAL_PROVISIONS) {
		R39_TOTAL_GENERAL_PROVISIONS = r39_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}
	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}
	public BigDecimal getR40_STAGE1_PROVISIONS() {
		return R40_STAGE1_PROVISIONS;
	}
	public void setR40_STAGE1_PROVISIONS(BigDecimal r40_STAGE1_PROVISIONS) {
		R40_STAGE1_PROVISIONS = r40_STAGE1_PROVISIONS;
	}
	public BigDecimal getR40_QUALIFY_STAGE2_PROVISIONS() {
		return R40_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR40_QUALIFY_STAGE2_PROVISIONS(BigDecimal r40_QUALIFY_STAGE2_PROVISIONS) {
		R40_QUALIFY_STAGE2_PROVISIONS = r40_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR40_TOTAL_GENERAL_PROVISIONS() {
		return R40_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR40_TOTAL_GENERAL_PROVISIONS(BigDecimal r40_TOTAL_GENERAL_PROVISIONS) {
		R40_TOTAL_GENERAL_PROVISIONS = r40_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}
	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}
	public BigDecimal getR41_STAGE1_PROVISIONS() {
		return R41_STAGE1_PROVISIONS;
	}
	public void setR41_STAGE1_PROVISIONS(BigDecimal r41_STAGE1_PROVISIONS) {
		R41_STAGE1_PROVISIONS = r41_STAGE1_PROVISIONS;
	}
	public BigDecimal getR41_QUALIFY_STAGE2_PROVISIONS() {
		return R41_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR41_QUALIFY_STAGE2_PROVISIONS(BigDecimal r41_QUALIFY_STAGE2_PROVISIONS) {
		R41_QUALIFY_STAGE2_PROVISIONS = r41_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR41_TOTAL_GENERAL_PROVISIONS() {
		return R41_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR41_TOTAL_GENERAL_PROVISIONS(BigDecimal r41_TOTAL_GENERAL_PROVISIONS) {
		R41_TOTAL_GENERAL_PROVISIONS = r41_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}
	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}
	public BigDecimal getR42_STAGE1_PROVISIONS() {
		return R42_STAGE1_PROVISIONS;
	}
	public void setR42_STAGE1_PROVISIONS(BigDecimal r42_STAGE1_PROVISIONS) {
		R42_STAGE1_PROVISIONS = r42_STAGE1_PROVISIONS;
	}
	public BigDecimal getR42_QUALIFY_STAGE2_PROVISIONS() {
		return R42_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR42_QUALIFY_STAGE2_PROVISIONS(BigDecimal r42_QUALIFY_STAGE2_PROVISIONS) {
		R42_QUALIFY_STAGE2_PROVISIONS = r42_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR42_TOTAL_GENERAL_PROVISIONS() {
		return R42_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR42_TOTAL_GENERAL_PROVISIONS(BigDecimal r42_TOTAL_GENERAL_PROVISIONS) {
		R42_TOTAL_GENERAL_PROVISIONS = r42_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}
	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}
	public BigDecimal getR43_STAGE1_PROVISIONS() {
		return R43_STAGE1_PROVISIONS;
	}
	public void setR43_STAGE1_PROVISIONS(BigDecimal r43_STAGE1_PROVISIONS) {
		R43_STAGE1_PROVISIONS = r43_STAGE1_PROVISIONS;
	}
	public BigDecimal getR43_QUALIFY_STAGE2_PROVISIONS() {
		return R43_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR43_QUALIFY_STAGE2_PROVISIONS(BigDecimal r43_QUALIFY_STAGE2_PROVISIONS) {
		R43_QUALIFY_STAGE2_PROVISIONS = r43_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR43_TOTAL_GENERAL_PROVISIONS() {
		return R43_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR43_TOTAL_GENERAL_PROVISIONS(BigDecimal r43_TOTAL_GENERAL_PROVISIONS) {
		R43_TOTAL_GENERAL_PROVISIONS = r43_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR44_PRODUCT() {
		return R44_PRODUCT;
	}
	public void setR44_PRODUCT(String r44_PRODUCT) {
		R44_PRODUCT = r44_PRODUCT;
	}
	public BigDecimal getR44_STAGE1_PROVISIONS() {
		return R44_STAGE1_PROVISIONS;
	}
	public void setR44_STAGE1_PROVISIONS(BigDecimal r44_STAGE1_PROVISIONS) {
		R44_STAGE1_PROVISIONS = r44_STAGE1_PROVISIONS;
	}
	public BigDecimal getR44_QUALIFY_STAGE2_PROVISIONS() {
		return R44_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR44_QUALIFY_STAGE2_PROVISIONS(BigDecimal r44_QUALIFY_STAGE2_PROVISIONS) {
		R44_QUALIFY_STAGE2_PROVISIONS = r44_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR44_TOTAL_GENERAL_PROVISIONS() {
		return R44_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR44_TOTAL_GENERAL_PROVISIONS(BigDecimal r44_TOTAL_GENERAL_PROVISIONS) {
		R44_TOTAL_GENERAL_PROVISIONS = r44_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR45_PRODUCT() {
		return R45_PRODUCT;
	}
	public void setR45_PRODUCT(String r45_PRODUCT) {
		R45_PRODUCT = r45_PRODUCT;
	}
	public BigDecimal getR45_STAGE1_PROVISIONS() {
		return R45_STAGE1_PROVISIONS;
	}
	public void setR45_STAGE1_PROVISIONS(BigDecimal r45_STAGE1_PROVISIONS) {
		R45_STAGE1_PROVISIONS = r45_STAGE1_PROVISIONS;
	}
	public BigDecimal getR45_QUALIFY_STAGE2_PROVISIONS() {
		return R45_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR45_QUALIFY_STAGE2_PROVISIONS(BigDecimal r45_QUALIFY_STAGE2_PROVISIONS) {
		R45_QUALIFY_STAGE2_PROVISIONS = r45_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR45_TOTAL_GENERAL_PROVISIONS() {
		return R45_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR45_TOTAL_GENERAL_PROVISIONS(BigDecimal r45_TOTAL_GENERAL_PROVISIONS) {
		R45_TOTAL_GENERAL_PROVISIONS = r45_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR46_PRODUCT() {
		return R46_PRODUCT;
	}
	public void setR46_PRODUCT(String r46_PRODUCT) {
		R46_PRODUCT = r46_PRODUCT;
	}
	public BigDecimal getR46_STAGE1_PROVISIONS() {
		return R46_STAGE1_PROVISIONS;
	}
	public void setR46_STAGE1_PROVISIONS(BigDecimal r46_STAGE1_PROVISIONS) {
		R46_STAGE1_PROVISIONS = r46_STAGE1_PROVISIONS;
	}
	public BigDecimal getR46_QUALIFY_STAGE2_PROVISIONS() {
		return R46_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR46_QUALIFY_STAGE2_PROVISIONS(BigDecimal r46_QUALIFY_STAGE2_PROVISIONS) {
		R46_QUALIFY_STAGE2_PROVISIONS = r46_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR46_TOTAL_GENERAL_PROVISIONS() {
		return R46_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR46_TOTAL_GENERAL_PROVISIONS(BigDecimal r46_TOTAL_GENERAL_PROVISIONS) {
		R46_TOTAL_GENERAL_PROVISIONS = r46_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}
	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}
	public BigDecimal getR47_STAGE1_PROVISIONS() {
		return R47_STAGE1_PROVISIONS;
	}
	public void setR47_STAGE1_PROVISIONS(BigDecimal r47_STAGE1_PROVISIONS) {
		R47_STAGE1_PROVISIONS = r47_STAGE1_PROVISIONS;
	}
	public BigDecimal getR47_QUALIFY_STAGE2_PROVISIONS() {
		return R47_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR47_QUALIFY_STAGE2_PROVISIONS(BigDecimal r47_QUALIFY_STAGE2_PROVISIONS) {
		R47_QUALIFY_STAGE2_PROVISIONS = r47_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR47_TOTAL_GENERAL_PROVISIONS() {
		return R47_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR47_TOTAL_GENERAL_PROVISIONS(BigDecimal r47_TOTAL_GENERAL_PROVISIONS) {
		R47_TOTAL_GENERAL_PROVISIONS = r47_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR48_PRODUCT() {
		return R48_PRODUCT;
	}
	public void setR48_PRODUCT(String r48_PRODUCT) {
		R48_PRODUCT = r48_PRODUCT;
	}
	public BigDecimal getR48_STAGE1_PROVISIONS() {
		return R48_STAGE1_PROVISIONS;
	}
	public void setR48_STAGE1_PROVISIONS(BigDecimal r48_STAGE1_PROVISIONS) {
		R48_STAGE1_PROVISIONS = r48_STAGE1_PROVISIONS;
	}
	public BigDecimal getR48_QUALIFY_STAGE2_PROVISIONS() {
		return R48_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR48_QUALIFY_STAGE2_PROVISIONS(BigDecimal r48_QUALIFY_STAGE2_PROVISIONS) {
		R48_QUALIFY_STAGE2_PROVISIONS = r48_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR48_TOTAL_GENERAL_PROVISIONS() {
		return R48_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR48_TOTAL_GENERAL_PROVISIONS(BigDecimal r48_TOTAL_GENERAL_PROVISIONS) {
		R48_TOTAL_GENERAL_PROVISIONS = r48_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR49_PRODUCT() {
		return R49_PRODUCT;
	}
	public void setR49_PRODUCT(String r49_PRODUCT) {
		R49_PRODUCT = r49_PRODUCT;
	}
	public BigDecimal getR49_STAGE1_PROVISIONS() {
		return R49_STAGE1_PROVISIONS;
	}
	public void setR49_STAGE1_PROVISIONS(BigDecimal r49_STAGE1_PROVISIONS) {
		R49_STAGE1_PROVISIONS = r49_STAGE1_PROVISIONS;
	}
	public BigDecimal getR49_QUALIFY_STAGE2_PROVISIONS() {
		return R49_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR49_QUALIFY_STAGE2_PROVISIONS(BigDecimal r49_QUALIFY_STAGE2_PROVISIONS) {
		R49_QUALIFY_STAGE2_PROVISIONS = r49_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR49_TOTAL_GENERAL_PROVISIONS() {
		return R49_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR49_TOTAL_GENERAL_PROVISIONS(BigDecimal r49_TOTAL_GENERAL_PROVISIONS) {
		R49_TOTAL_GENERAL_PROVISIONS = r49_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR50_PRODUCT() {
		return R50_PRODUCT;
	}
	public void setR50_PRODUCT(String r50_PRODUCT) {
		R50_PRODUCT = r50_PRODUCT;
	}
	public BigDecimal getR50_STAGE1_PROVISIONS() {
		return R50_STAGE1_PROVISIONS;
	}
	public void setR50_STAGE1_PROVISIONS(BigDecimal r50_STAGE1_PROVISIONS) {
		R50_STAGE1_PROVISIONS = r50_STAGE1_PROVISIONS;
	}
	public BigDecimal getR50_QUALIFY_STAGE2_PROVISIONS() {
		return R50_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR50_QUALIFY_STAGE2_PROVISIONS(BigDecimal r50_QUALIFY_STAGE2_PROVISIONS) {
		R50_QUALIFY_STAGE2_PROVISIONS = r50_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR50_TOTAL_GENERAL_PROVISIONS() {
		return R50_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR50_TOTAL_GENERAL_PROVISIONS(BigDecimal r50_TOTAL_GENERAL_PROVISIONS) {
		R50_TOTAL_GENERAL_PROVISIONS = r50_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR51_PRODUCT() {
		return R51_PRODUCT;
	}
	public void setR51_PRODUCT(String r51_PRODUCT) {
		R51_PRODUCT = r51_PRODUCT;
	}
	public BigDecimal getR51_STAGE1_PROVISIONS() {
		return R51_STAGE1_PROVISIONS;
	}
	public void setR51_STAGE1_PROVISIONS(BigDecimal r51_STAGE1_PROVISIONS) {
		R51_STAGE1_PROVISIONS = r51_STAGE1_PROVISIONS;
	}
	public BigDecimal getR51_QUALIFY_STAGE2_PROVISIONS() {
		return R51_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR51_QUALIFY_STAGE2_PROVISIONS(BigDecimal r51_QUALIFY_STAGE2_PROVISIONS) {
		R51_QUALIFY_STAGE2_PROVISIONS = r51_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR51_TOTAL_GENERAL_PROVISIONS() {
		return R51_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR51_TOTAL_GENERAL_PROVISIONS(BigDecimal r51_TOTAL_GENERAL_PROVISIONS) {
		R51_TOTAL_GENERAL_PROVISIONS = r51_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR52_PRODUCT() {
		return R52_PRODUCT;
	}
	public void setR52_PRODUCT(String r52_PRODUCT) {
		R52_PRODUCT = r52_PRODUCT;
	}
	public BigDecimal getR52_STAGE1_PROVISIONS() {
		return R52_STAGE1_PROVISIONS;
	}
	public void setR52_STAGE1_PROVISIONS(BigDecimal r52_STAGE1_PROVISIONS) {
		R52_STAGE1_PROVISIONS = r52_STAGE1_PROVISIONS;
	}
	public BigDecimal getR52_QUALIFY_STAGE2_PROVISIONS() {
		return R52_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR52_QUALIFY_STAGE2_PROVISIONS(BigDecimal r52_QUALIFY_STAGE2_PROVISIONS) {
		R52_QUALIFY_STAGE2_PROVISIONS = r52_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR52_TOTAL_GENERAL_PROVISIONS() {
		return R52_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR52_TOTAL_GENERAL_PROVISIONS(BigDecimal r52_TOTAL_GENERAL_PROVISIONS) {
		R52_TOTAL_GENERAL_PROVISIONS = r52_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR53_PRODUCT() {
		return R53_PRODUCT;
	}
	public void setR53_PRODUCT(String r53_PRODUCT) {
		R53_PRODUCT = r53_PRODUCT;
	}
	public BigDecimal getR53_STAGE1_PROVISIONS() {
		return R53_STAGE1_PROVISIONS;
	}
	public void setR53_STAGE1_PROVISIONS(BigDecimal r53_STAGE1_PROVISIONS) {
		R53_STAGE1_PROVISIONS = r53_STAGE1_PROVISIONS;
	}
	public BigDecimal getR53_QUALIFY_STAGE2_PROVISIONS() {
		return R53_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR53_QUALIFY_STAGE2_PROVISIONS(BigDecimal r53_QUALIFY_STAGE2_PROVISIONS) {
		R53_QUALIFY_STAGE2_PROVISIONS = r53_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR53_TOTAL_GENERAL_PROVISIONS() {
		return R53_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR53_TOTAL_GENERAL_PROVISIONS(BigDecimal r53_TOTAL_GENERAL_PROVISIONS) {
		R53_TOTAL_GENERAL_PROVISIONS = r53_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR54_PRODUCT() {
		return R54_PRODUCT;
	}
	public void setR54_PRODUCT(String r54_PRODUCT) {
		R54_PRODUCT = r54_PRODUCT;
	}
	public BigDecimal getR54_STAGE1_PROVISIONS() {
		return R54_STAGE1_PROVISIONS;
	}
	public void setR54_STAGE1_PROVISIONS(BigDecimal r54_STAGE1_PROVISIONS) {
		R54_STAGE1_PROVISIONS = r54_STAGE1_PROVISIONS;
	}
	public BigDecimal getR54_QUALIFY_STAGE2_PROVISIONS() {
		return R54_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR54_QUALIFY_STAGE2_PROVISIONS(BigDecimal r54_QUALIFY_STAGE2_PROVISIONS) {
		R54_QUALIFY_STAGE2_PROVISIONS = r54_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR54_TOTAL_GENERAL_PROVISIONS() {
		return R54_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR54_TOTAL_GENERAL_PROVISIONS(BigDecimal r54_TOTAL_GENERAL_PROVISIONS) {
		R54_TOTAL_GENERAL_PROVISIONS = r54_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR55_PRODUCT() {
		return R55_PRODUCT;
	}
	public void setR55_PRODUCT(String r55_PRODUCT) {
		R55_PRODUCT = r55_PRODUCT;
	}
	public BigDecimal getR55_STAGE1_PROVISIONS() {
		return R55_STAGE1_PROVISIONS;
	}
	public void setR55_STAGE1_PROVISIONS(BigDecimal r55_STAGE1_PROVISIONS) {
		R55_STAGE1_PROVISIONS = r55_STAGE1_PROVISIONS;
	}
	public BigDecimal getR55_QUALIFY_STAGE2_PROVISIONS() {
		return R55_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR55_QUALIFY_STAGE2_PROVISIONS(BigDecimal r55_QUALIFY_STAGE2_PROVISIONS) {
		R55_QUALIFY_STAGE2_PROVISIONS = r55_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR55_TOTAL_GENERAL_PROVISIONS() {
		return R55_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR55_TOTAL_GENERAL_PROVISIONS(BigDecimal r55_TOTAL_GENERAL_PROVISIONS) {
		R55_TOTAL_GENERAL_PROVISIONS = r55_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR56_PRODUCT() {
		return R56_PRODUCT;
	}
	public void setR56_PRODUCT(String r56_PRODUCT) {
		R56_PRODUCT = r56_PRODUCT;
	}
	public BigDecimal getR56_STAGE1_PROVISIONS() {
		return R56_STAGE1_PROVISIONS;
	}
	public void setR56_STAGE1_PROVISIONS(BigDecimal r56_STAGE1_PROVISIONS) {
		R56_STAGE1_PROVISIONS = r56_STAGE1_PROVISIONS;
	}
	public BigDecimal getR56_QUALIFY_STAGE2_PROVISIONS() {
		return R56_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR56_QUALIFY_STAGE2_PROVISIONS(BigDecimal r56_QUALIFY_STAGE2_PROVISIONS) {
		R56_QUALIFY_STAGE2_PROVISIONS = r56_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR56_TOTAL_GENERAL_PROVISIONS() {
		return R56_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR56_TOTAL_GENERAL_PROVISIONS(BigDecimal r56_TOTAL_GENERAL_PROVISIONS) {
		R56_TOTAL_GENERAL_PROVISIONS = r56_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR57_PRODUCT() {
		return R57_PRODUCT;
	}
	public void setR57_PRODUCT(String r57_PRODUCT) {
		R57_PRODUCT = r57_PRODUCT;
	}
	public BigDecimal getR57_STAGE1_PROVISIONS() {
		return R57_STAGE1_PROVISIONS;
	}
	public void setR57_STAGE1_PROVISIONS(BigDecimal r57_STAGE1_PROVISIONS) {
		R57_STAGE1_PROVISIONS = r57_STAGE1_PROVISIONS;
	}
	public BigDecimal getR57_QUALIFY_STAGE2_PROVISIONS() {
		return R57_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR57_QUALIFY_STAGE2_PROVISIONS(BigDecimal r57_QUALIFY_STAGE2_PROVISIONS) {
		R57_QUALIFY_STAGE2_PROVISIONS = r57_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR57_TOTAL_GENERAL_PROVISIONS() {
		return R57_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR57_TOTAL_GENERAL_PROVISIONS(BigDecimal r57_TOTAL_GENERAL_PROVISIONS) {
		R57_TOTAL_GENERAL_PROVISIONS = r57_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR58_PRODUCT() {
		return R58_PRODUCT;
	}
	public void setR58_PRODUCT(String r58_PRODUCT) {
		R58_PRODUCT = r58_PRODUCT;
	}
	public BigDecimal getR58_STAGE1_PROVISIONS() {
		return R58_STAGE1_PROVISIONS;
	}
	public void setR58_STAGE1_PROVISIONS(BigDecimal r58_STAGE1_PROVISIONS) {
		R58_STAGE1_PROVISIONS = r58_STAGE1_PROVISIONS;
	}
	public BigDecimal getR58_QUALIFY_STAGE2_PROVISIONS() {
		return R58_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR58_QUALIFY_STAGE2_PROVISIONS(BigDecimal r58_QUALIFY_STAGE2_PROVISIONS) {
		R58_QUALIFY_STAGE2_PROVISIONS = r58_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR58_TOTAL_GENERAL_PROVISIONS() {
		return R58_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR58_TOTAL_GENERAL_PROVISIONS(BigDecimal r58_TOTAL_GENERAL_PROVISIONS) {
		R58_TOTAL_GENERAL_PROVISIONS = r58_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR59_PRODUCT() {
		return R59_PRODUCT;
	}
	public void setR59_PRODUCT(String r59_PRODUCT) {
		R59_PRODUCT = r59_PRODUCT;
	}
	public BigDecimal getR59_STAGE1_PROVISIONS() {
		return R59_STAGE1_PROVISIONS;
	}
	public void setR59_STAGE1_PROVISIONS(BigDecimal r59_STAGE1_PROVISIONS) {
		R59_STAGE1_PROVISIONS = r59_STAGE1_PROVISIONS;
	}
	public BigDecimal getR59_QUALIFY_STAGE2_PROVISIONS() {
		return R59_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR59_QUALIFY_STAGE2_PROVISIONS(BigDecimal r59_QUALIFY_STAGE2_PROVISIONS) {
		R59_QUALIFY_STAGE2_PROVISIONS = r59_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR59_TOTAL_GENERAL_PROVISIONS() {
		return R59_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR59_TOTAL_GENERAL_PROVISIONS(BigDecimal r59_TOTAL_GENERAL_PROVISIONS) {
		R59_TOTAL_GENERAL_PROVISIONS = r59_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR60_PRODUCT() {
		return R60_PRODUCT;
	}
	public void setR60_PRODUCT(String r60_PRODUCT) {
		R60_PRODUCT = r60_PRODUCT;
	}
	public BigDecimal getR60_STAGE1_PROVISIONS() {
		return R60_STAGE1_PROVISIONS;
	}
	public void setR60_STAGE1_PROVISIONS(BigDecimal r60_STAGE1_PROVISIONS) {
		R60_STAGE1_PROVISIONS = r60_STAGE1_PROVISIONS;
	}
	public BigDecimal getR60_QUALIFY_STAGE2_PROVISIONS() {
		return R60_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR60_QUALIFY_STAGE2_PROVISIONS(BigDecimal r60_QUALIFY_STAGE2_PROVISIONS) {
		R60_QUALIFY_STAGE2_PROVISIONS = r60_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR60_TOTAL_GENERAL_PROVISIONS() {
		return R60_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR60_TOTAL_GENERAL_PROVISIONS(BigDecimal r60_TOTAL_GENERAL_PROVISIONS) {
		R60_TOTAL_GENERAL_PROVISIONS = r60_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR61_PRODUCT() {
		return R61_PRODUCT;
	}
	public void setR61_PRODUCT(String r61_PRODUCT) {
		R61_PRODUCT = r61_PRODUCT;
	}
	public BigDecimal getR61_STAGE1_PROVISIONS() {
		return R61_STAGE1_PROVISIONS;
	}
	public void setR61_STAGE1_PROVISIONS(BigDecimal r61_STAGE1_PROVISIONS) {
		R61_STAGE1_PROVISIONS = r61_STAGE1_PROVISIONS;
	}
	public BigDecimal getR61_QUALIFY_STAGE2_PROVISIONS() {
		return R61_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR61_QUALIFY_STAGE2_PROVISIONS(BigDecimal r61_QUALIFY_STAGE2_PROVISIONS) {
		R61_QUALIFY_STAGE2_PROVISIONS = r61_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR61_TOTAL_GENERAL_PROVISIONS() {
		return R61_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR61_TOTAL_GENERAL_PROVISIONS(BigDecimal r61_TOTAL_GENERAL_PROVISIONS) {
		R61_TOTAL_GENERAL_PROVISIONS = r61_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR62_PRODUCT() {
		return R62_PRODUCT;
	}
	public void setR62_PRODUCT(String r62_PRODUCT) {
		R62_PRODUCT = r62_PRODUCT;
	}
	public BigDecimal getR62_STAGE1_PROVISIONS() {
		return R62_STAGE1_PROVISIONS;
	}
	public void setR62_STAGE1_PROVISIONS(BigDecimal r62_STAGE1_PROVISIONS) {
		R62_STAGE1_PROVISIONS = r62_STAGE1_PROVISIONS;
	}
	public BigDecimal getR62_QUALIFY_STAGE2_PROVISIONS() {
		return R62_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR62_QUALIFY_STAGE2_PROVISIONS(BigDecimal r62_QUALIFY_STAGE2_PROVISIONS) {
		R62_QUALIFY_STAGE2_PROVISIONS = r62_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR62_TOTAL_GENERAL_PROVISIONS() {
		return R62_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR62_TOTAL_GENERAL_PROVISIONS(BigDecimal r62_TOTAL_GENERAL_PROVISIONS) {
		R62_TOTAL_GENERAL_PROVISIONS = r62_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR63_PRODUCT() {
		return R63_PRODUCT;
	}
	public void setR63_PRODUCT(String r63_PRODUCT) {
		R63_PRODUCT = r63_PRODUCT;
	}
	public BigDecimal getR63_STAGE1_PROVISIONS() {
		return R63_STAGE1_PROVISIONS;
	}
	public void setR63_STAGE1_PROVISIONS(BigDecimal r63_STAGE1_PROVISIONS) {
		R63_STAGE1_PROVISIONS = r63_STAGE1_PROVISIONS;
	}
	public BigDecimal getR63_QUALIFY_STAGE2_PROVISIONS() {
		return R63_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR63_QUALIFY_STAGE2_PROVISIONS(BigDecimal r63_QUALIFY_STAGE2_PROVISIONS) {
		R63_QUALIFY_STAGE2_PROVISIONS = r63_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR63_TOTAL_GENERAL_PROVISIONS() {
		return R63_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR63_TOTAL_GENERAL_PROVISIONS(BigDecimal r63_TOTAL_GENERAL_PROVISIONS) {
		R63_TOTAL_GENERAL_PROVISIONS = r63_TOTAL_GENERAL_PROVISIONS;
	}
	public String getR64_PRODUCT() {
		return R64_PRODUCT;
	}
	public void setR64_PRODUCT(String r64_PRODUCT) {
		R64_PRODUCT = r64_PRODUCT;
	}
	public BigDecimal getR64_STAGE1_PROVISIONS() {
		return R64_STAGE1_PROVISIONS;
	}
	public void setR64_STAGE1_PROVISIONS(BigDecimal r64_STAGE1_PROVISIONS) {
		R64_STAGE1_PROVISIONS = r64_STAGE1_PROVISIONS;
	}
	public BigDecimal getR64_QUALIFY_STAGE2_PROVISIONS() {
		return R64_QUALIFY_STAGE2_PROVISIONS;
	}
	public void setR64_QUALIFY_STAGE2_PROVISIONS(BigDecimal r64_QUALIFY_STAGE2_PROVISIONS) {
		R64_QUALIFY_STAGE2_PROVISIONS = r64_QUALIFY_STAGE2_PROVISIONS;
	}
	public BigDecimal getR64_TOTAL_GENERAL_PROVISIONS() {
		return R64_TOTAL_GENERAL_PROVISIONS;
	}
	public void setR64_TOTAL_GENERAL_PROVISIONS(BigDecimal r64_TOTAL_GENERAL_PROVISIONS) {
		R64_TOTAL_GENERAL_PROVISIONS = r64_TOTAL_GENERAL_PROVISIONS;
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
// MODEL AND VIEW METHOD summary M_GP
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
public ModelAndView getM_GPView(
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

    System.out.println("M_GP View Called");
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

                List<M_GP_Archival_Detail_Entity> T1Master =
                        getDetaildatabydateListarchival(dt, version);

                System.out.println("Archival Detail Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
                mv.addObject("displaymode", "detail");
            }

            // RESUB DETAIL
            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

                List<M_GP_RESUB_Detail_Entity> T1Master =
                        getResubDetaildatabydateList(dt, version);

                System.out.println("Resub Detail Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
                 mv.addObject("displaymode", "resub");
            }

            // NORMAL DETAIL
            else {

                List<M_GP_Detail_Entity> T1Master =
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

                List<M_GP_Archival_Summary_Entity> T1Master =
                        getDataByDateListArchival(dt, version);

                System.out.println("Archival Summary Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "summary");
            }

            // RESUB SUMMARY
            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

                List<M_GP_RESUB_Summary_Entity> T1Master =
                        getResubSummarydatabydateListarchival(dt, version);

                System.out.println("Resub Summary Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
				  mv.addObject("displaymode", "resub");
            }

            // NORMAL SUMMARY
            else {

                List<M_GP_Summary_Entity> T1Master =
                        getSummaryDataByDate(dt);

                System.out.println("Normal Summary Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
				 mv.addObject("displaymode", "summary");
            }

           
        }

        mv.addObject("report_date", dateformat.format(dt));

    } catch (Exception e) {
        e.printStackTrace();
    }

    mv.setViewName("BRRS/M_GP");

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
public List<Object[]> getM_GPArchival() {

    List<Object[]> archivalList = new ArrayList<>();

    try {

        List<M_GP_Archival_Summary_Entity> repoData =
                getarchivaldatabydateListWithVersion();

        if (repoData != null && !repoData.isEmpty()) {

            for (M_GP_Archival_Summary_Entity entity : repoData) {

                Object[] row = new Object[] {
                        entity.getReport_date(),
                        entity.getReport_version(),
                        entity.getReportResubDate()
                };

                archivalList.add(row);
            }

            System.out.println("Fetched " + archivalList.size() + " archival records");

            M_GP_Archival_Summary_Entity first = repoData.get(0);

            System.out.println("Latest archival version: "
                    + first.getReport_version());

        } else {

            System.out.println("No archival data found.");
        }

    } catch (Exception e) {

        System.err.println("Error fetching M_GP Archival data: "
                + e.getMessage());

        e.printStackTrace();
    }

    return archivalList;
}
//=====================================================
// UPDATE REPORT
//=====================================================

@Transactional
public void updateReport(M_GP_Summary_Entity updatedEntity) {

    System.out.println("Came to M_GP Update");
    System.out.println("Report Date: " + updatedEntity.getReport_date());

    // ==========================================
    // FETCH EXISTING RECORD FOR AUDIT
    // ==========================================

    M_GP_Summary_Entity existingSummary =
            findByReportDate(updatedEntity.getReport_date());

    if (existingSummary == null) {
        throw new RuntimeException(
                "Record not found for REPORT_DATE : "
                        + updatedEntity.getReport_date());
    }

    // ==========================================
    // OLD COPY FOR AUDIT
    // ==========================================

    M_GP_Summary_Entity oldcopy =
            new M_GP_Summary_Entity();

    BeanUtils.copyProperties(existingSummary, oldcopy);

    String[] fields = {
	                    "PRODUCT",
	                    "STAGE1_PROVISIONS",
	                    "QUALIFY_STAGE2_PROVISIONS",
	                    "TOTAL_GENERAL_PROVISIONS"
	            };

    try {

        for (int i = 11; i <= 64; i++) {

            for (String field : fields) {

                String getterName = "getR" + i + "_" + field;
                String setterName = "setR" + i + "_" + field;
                String columnName = "R" + i + "_" + field;

                try {

                    Method getter =
                            M_GP_Summary_Entity.class.getMethod(getterName);

                    Object value = getter.invoke(updatedEntity);

                    if (value == null) {
                        continue;
                    }

                    // ==========================================
                    // UPDATE EXISTING OBJECT FOR AUDIT
                    // ==========================================

                    Method setter =
                            M_GP_Summary_Entity.class.getMethod(
                                    setterName,
                                    getter.getReturnType());

                    setter.invoke(existingSummary, value);

                    // ==========================================
                    // UPDATE SUMMARY TABLE
                    // ==========================================

                    String summarySql =
                            "UPDATE BRRS_M_GP_SUMMARYTABLE " +
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
                            "UPDATE BRRS_M_GP_DETAILTABLE " +
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

        // ==========================================
        // AUDIT ONLY IF CHANGES FOUND
        // ==========================================

        String changes =
                auditService.getChanges(
                        oldcopy,
                        existingSummary);

        if (!changes.isEmpty()) {

            auditService.compareEntitiesmanual(
                    oldcopy,
                    existingSummary,
                    updatedEntity.getReport_date().toString(),
                    "M GP Summary Screen",
                    "BRRS_M_GP_SUMMARYTABLE"
            );
        }

        System.out.println(
                "M_GP Summary & Detail Update Completed");

    } catch (Exception e) {

        throw new RuntimeException(
                "Error while updating M_GP fields", e);
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

public List<Object[]> getM_GPResub() {

    List<Object[]> resubList = new ArrayList<>();

    try {

        List<M_GP_Archival_Summary_Entity> repoData =
                getarchivaldatabydateListWithVersion();

        if (repoData != null && !repoData.isEmpty()) {

            for (M_GP_Archival_Summary_Entity entity : repoData) {

                Object[] row = new Object[] {
                        entity.getReport_date(),
                        entity.getReport_version(),
                        entity.getReportResubDate()
                };

                resubList.add(row);
            }

            System.out.println("Fetched " + resubList.size() + " resub records");

            M_GP_Archival_Summary_Entity first = repoData.get(0);

            System.out.println("Latest resub version : "
                    + first.getReport_version());

        } else {

            System.out.println("No resub data found.");
        }

    } catch (Exception e) {

        System.err.println("Error fetching M_GP Resub data : "
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
        M_GP_RESUB_Summary_Entity updatedEntity) {

    System.out.println("Came to M_GP Resub Update");

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

        M_GP_RESUB_Summary_Entity resubSummary =
                new M_GP_RESUB_Summary_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                resubSummary);

        resubSummary.setReport_date(reportDate);
        resubSummary.setReport_version(newVersion);
        resubSummary.setReportResubDate(now);

        // ====================================================
        // RESUB DETAIL
        // ====================================================

        M_GP_RESUB_Detail_Entity resubDetail =
                new M_GP_RESUB_Detail_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                resubDetail);

        resubDetail.setReport_date(reportDate);
        resubDetail.setReport_version(newVersion);
        resubDetail.setReportResubDate(now);

        // ====================================================
        // ARCHIVAL SUMMARY
        // ====================================================

        M_GP_Archival_Summary_Entity archivalSummary =
                new M_GP_Archival_Summary_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                archivalSummary);

        archivalSummary.setReport_date(reportDate);
        archivalSummary.setReport_version(newVersion);
        archivalSummary.setReportResubDate(now);

        // ====================================================
        // ARCHIVAL DETAIL
        // ====================================================

        M_GP_Archival_Detail_Entity archivalDetail =
                new M_GP_Archival_Detail_Entity();

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
                "M_GP Resub Version Created Successfully : "
                        + newVersion);

    } catch (Exception e) {

        e.printStackTrace();

        throw new RuntimeException(
                "Error while creating M_GP Resub Version",
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

private void insertResubSummary(M_GP_RESUB_Summary_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_M_GP_RESUB_SUMMARYTABLE (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

      for (int i = 11; i <= 64; i++) {

    columns
        .append("R").append(i).append("_PRODUCT,")
        .append("R").append(i).append("_STAGE1_PROVISIONS,")
        .append("R").append(i).append("_QUALIFY_STAGE2_PROVISIONS,")
        .append("R").append(i).append("_TOTAL_GENERAL_PROVISIONS,");

    for (int j = 1; j <= 4; j++) {
        values.append("?,");
    }

    params.add(getValue(entity, "getR" + i + "_PRODUCT"));
    params.add(getValue(entity, "getR" + i + "_STAGE1_PROVISIONS"));
    params.add(getValue(entity, "getR" + i + "_QUALIFY_STAGE2_PROVISIONS"));
    params.add(getValue(entity, "getR" + i + "_TOTAL_GENERAL_PROVISIONS"));
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
                "Error inserting M_GP RESUB SUMMARY",
                e);
    }
}

private void insertResubDetail(M_GP_RESUB_Detail_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_M_GP_RESUB_DETAILTABLE (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

       for (int i = 11; i <= 64; i++) {

    columns
        .append("R").append(i).append("_PRODUCT,")
        .append("R").append(i).append("_STAGE1_PROVISIONS,")
        .append("R").append(i).append("_QUALIFY_STAGE2_PROVISIONS,")
        .append("R").append(i).append("_TOTAL_GENERAL_PROVISIONS,");

    for (int j = 1; j <= 4; j++) {
        values.append("?,");
    }

    params.add(getValue(entity, "getR" + i + "_PRODUCT"));
    params.add(getValue(entity, "getR" + i + "_STAGE1_PROVISIONS"));
    params.add(getValue(entity, "getR" + i + "_QUALIFY_STAGE2_PROVISIONS"));
    params.add(getValue(entity, "getR" + i + "_TOTAL_GENERAL_PROVISIONS"));
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
                "Error inserting M_GP RESUB DETAIL",
                e);
    }
}

private void insertArchivalSummary(M_GP_Archival_Summary_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_M_GP_ARCHIVALTABLE_SUMMARY (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

      for (int i = 11; i <= 64; i++) {

    columns
        .append("R").append(i).append("_PRODUCT,")
        .append("R").append(i).append("_STAGE1_PROVISIONS,")
        .append("R").append(i).append("_QUALIFY_STAGE2_PROVISIONS,")
        .append("R").append(i).append("_TOTAL_GENERAL_PROVISIONS,");

    for (int j = 1; j <= 4; j++) {
        values.append("?,");
    }

    params.add(getValue(entity, "getR" + i + "_PRODUCT"));
    params.add(getValue(entity, "getR" + i + "_STAGE1_PROVISIONS"));
    params.add(getValue(entity, "getR" + i + "_QUALIFY_STAGE2_PROVISIONS"));
    params.add(getValue(entity, "getR" + i + "_TOTAL_GENERAL_PROVISIONS"));
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
                "Error inserting M_GP ARCHIVAL SUMMARY",
                e);
    }
}

private void insertArchivalDetail(M_GP_Archival_Detail_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_M_GP_ARCHIVALTABLE_DETAIL (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

     for (int i = 11; i <= 64; i++) {

    columns
        .append("R").append(i).append("_PRODUCT,")
        .append("R").append(i).append("_STAGE1_PROVISIONS,")
        .append("R").append(i).append("_QUALIFY_STAGE2_PROVISIONS,")
        .append("R").append(i).append("_TOTAL_GENERAL_PROVISIONS,");

    for (int j = 1; j <= 4; j++) {
        values.append("?,");
    }

    params.add(getValue(entity, "getR" + i + "_PRODUCT"));
    params.add(getValue(entity, "getR" + i + "_STAGE1_PROVISIONS"));
    params.add(getValue(entity, "getR" + i + "_QUALIFY_STAGE2_PROVISIONS"));
    params.add(getValue(entity, "getR" + i + "_TOTAL_GENERAL_PROVISIONS"));
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
                "Error inserting M_GP ARCHIVAL DETAIL",
                e);
    }
}
	
//=====================================================
// Summary EXCEL  FORMAT
//=====================================================

public byte[] getM_GPExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
						return getExcelM_GPARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_GPResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else {

					if ("email".equalsIgnoreCase(format) && version == null) {
						logger.info("Got format as Email");
						logger.info("Service: Generating Email report for version {}", version);
						return BRRS_M_GPEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} else {

						// Fetch data

						List<M_GP_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

						if (dataList.isEmpty()) {
							logger.warn("Service: No data found for BRRS_M_GP report. Returning empty result.");
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

							int startRow = 3;

							if (!dataList.isEmpty()) {
								for (int i = 0; i < dataList.size(); i++) {
									M_GP_Summary_Entity record = dataList.get(i);
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
								
								
								
								//ROW 11
								row = sheet.getRow(10);

		// row10
						// Column C

						Cell cell1 = row.getCell(1);
						if (record.getR11_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR11_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR11_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR11_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR11_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR11_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(11);

						cell1 = row.getCell(1);
						if (record.getR12_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR12_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR12_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(12);

						cell1 = row.getCell(1);
						if (record.getR13_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR13_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR13_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(13);
						cell1 = row.getCell(1);
						if (record.getR14_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR14_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR14_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row10
						// Column C
						row = sheet.getRow(14);

						cell1 = row.getCell(1);
						if (record.getR15_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR15_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR15_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR15_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR15_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR15_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(15);

						cell1 = row.getCell(1);
						if (record.getR16_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR16_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR16_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(16);

						cell1 = row.getCell(1);
						if (record.getR17_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR17_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR17_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(17);
						cell1 = row.getCell(1);
						if (record.getR18_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR18_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR18_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row10
						// Column C
						row = sheet.getRow(18);

						cell1 = row.getCell(1);
						if (record.getR19_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR19_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR19_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(19);

						cell1 = row.getCell(1);
						if (record.getR20_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR20_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR20_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(20);

						cell1 = row.getCell(1);
						if (record.getR21_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR21_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR21_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(21);
						cell1 = row.getCell(1);
						if (record.getR22_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR22_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR22_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row10
						// Column C
						row = sheet.getRow(22);

						cell1 = row.getCell(1);
						if (record.getR23_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR23_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR23_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(23);

						cell1 = row.getCell(1);
						if (record.getR24_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR24_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR24_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(24);

						cell1 = row.getCell(1);
						if (record.getR25_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR25_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR25_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(25);
						cell1 = row.getCell(1);
						if (record.getR26_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR26_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR26_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row10
						// Column C
						row = sheet.getRow(26);

						cell1 = row.getCell(1);
						if (record.getR27_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR27_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR27_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(27);

						cell1 = row.getCell(1);
						if (record.getR28_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR28_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR28_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(28);

						cell1 = row.getCell(1);
						if (record.getR29_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR29_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR29_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR29_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR29_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR29_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(29);
						cell1 = row.getCell(1);
						if (record.getR30_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR30_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR30_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row10
						// Column C
						row = sheet.getRow(30);

						cell1 = row.getCell(1);
						if (record.getR31_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR31_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR31_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(31);

						cell1 = row.getCell(1);
						if (record.getR32_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR32_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR32_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(32);

						cell1 = row.getCell(1);
						if (record.getR33_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR33_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR33_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column C
						row = sheet.getRow(33);

						cell1 = row.getCell(1);
						if (record.getR34_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR34_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR34_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						row = sheet.getRow(34);

						cell1 = row.getCell(1);
						if (record.getR35_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR35_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR35_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column C
						row = sheet.getRow(35);

						cell1 = row.getCell(1);
						if (record.getR36_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR36_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR36_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column C
						row = sheet.getRow(36);

						cell1 = row.getCell(1);
						if (record.getR37_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR37_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR37_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						row = sheet.getRow(37);

						cell1 = row.getCell(1);
						if (record.getR38_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR38_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR38_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR38_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR38_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR38_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// Column C
						row = sheet.getRow(38);

						cell1 = row.getCell(1);
						if (record.getR39_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR39_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR39_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// ===== R40 =====
						row = sheet.getRow(39);

						cell1 = row.getCell(1);
						if (record.getR40_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR40_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR40_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R41 =====
						row = sheet.getRow(40);

						cell1 = row.getCell(1);
						if (record.getR41_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR41_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR41_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR41_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR41_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR41_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R42 =====
						row = sheet.getRow(41);

						cell1 = row.getCell(1);
						if (record.getR42_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR42_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR42_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R43 =====
						row = sheet.getRow(42);

						cell1 = row.getCell(1);
						if (record.getR43_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR43_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR43_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R44 =====
						row = sheet.getRow(43);

						cell1 = row.getCell(1);
						if (record.getR44_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR44_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR44_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR44_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR44_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR44_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R45 =====
						row = sheet.getRow(44);

						cell1 = row.getCell(1);
						if (record.getR45_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR45_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR45_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// ===== R46 =====
						row = sheet.getRow(45);

						cell1 = row.getCell(1);
						if (record.getR46_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR46_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR46_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R47 =====
						row = sheet.getRow(46);

						cell1 = row.getCell(1);
						if (record.getR47_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR47_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR47_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R48 =====
						row = sheet.getRow(47);

						cell1 = row.getCell(1);
						if (record.getR48_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR48_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR48_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R49 =====
						row = sheet.getRow(48);

						cell1 = row.getCell(1);
						if (record.getR49_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR49_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR49_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR49_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR49_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR49_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R50 =====
						row = sheet.getRow(49);

						cell1 = row.getCell(1);
						if (record.getR50_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR50_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR50_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// ===== R51 =====
						row = sheet.getRow(50);

						cell1 = row.getCell(1);
						if (record.getR51_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR51_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR51_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R52 =====
						row = sheet.getRow(51);

						cell1 = row.getCell(1);
						if (record.getR52_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR52_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR52_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R53 =====
						row = sheet.getRow(52);

						cell1 = row.getCell(1);
						if (record.getR53_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR53_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR53_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR53_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR53_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR53_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R54 =====
						row = sheet.getRow(53);

						cell1 = row.getCell(1);
						if (record.getR54_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR54_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR54_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R55 =====
						row = sheet.getRow(54);

						cell1 = row.getCell(1);
						if (record.getR55_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR55_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR55_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// ===== R56 =====
						row = sheet.getRow(55);

						cell1 = row.getCell(1);
						if (record.getR56_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR56_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR56_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R57 =====
						row = sheet.getRow(56);

						cell1 = row.getCell(1);
						if (record.getR57_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR57_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR57_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR57_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR57_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR57_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R58 =====
						row = sheet.getRow(57);

						cell1 = row.getCell(1);
						if (record.getR58_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR58_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR58_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R59 =====
						row = sheet.getRow(58);

						cell1 = row.getCell(1);
						if (record.getR59_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR59_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR59_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R60 =====
						row = sheet.getRow(59);

						cell1 = row.getCell(1);
						if (record.getR60_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR60_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR60_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R61 =====
						row = sheet.getRow(60);

						cell1 = row.getCell(1);
						if (record.getR61_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR61_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR61_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R62 =====
						row = sheet.getRow(61);

						cell1 = row.getCell(1);
						if (record.getR62_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR62_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR62_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R63 =====
						row = sheet.getRow(62);

						cell1 = row.getCell(1);
						if (record.getR63_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR63_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR63_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R64 =====
						row = sheet.getRow(63);

						cell1 = row.getCell(1);
						if (record.getR64_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR64_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR64_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR64_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR64_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR64_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
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
								auditService.createBusinessAudit(userid, "DOWNLOAD", "M_GP SUMMARY", null, "BRRS_M_GP_SUMMARYTABLE");
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
			public byte[] BRRS_M_GPEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Email Excel generation process in memory.");
				
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_M_GPARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_GPEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 
				else {
				List<M_GP_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_GP report. Returning empty result.");
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
							M_GP_Summary_Entity record = dataList.get(i);
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
						
						
						
						//ROW 11
						row = sheet.getRow(7);

	//-------------------8
					
					
					Cell cell1 = row.getCell(1);
					if (record.getR12_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					 cell1 = row.getCell(2);
					if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------9
					
					// Column C
					row = sheet.getRow(8);

					cell1 = row.getCell(1);
					if (record.getR13_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------10
				
					row = sheet.getRow(9);
					
					cell1 = row.getCell(1);
					if (record.getR14_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------12
				
				row = sheet.getRow(11);

					cell1 = row.getCell(1);
					if (record.getR16_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
				
				//-------------------13
				
				row = sheet.getRow(12);

					cell1 = row.getCell(1);
					if (record.getR17_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					
						//-------------------14
						
						
						
	row = sheet.getRow(13);

	cell1 = row.getCell(1);
	if (record.getR18_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------15
	row = sheet.getRow(14);

	cell1 = row.getCell(1);
	if (record.getR19_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------16
	row = sheet.getRow(15);

	cell1 = row.getCell(1);
	if (record.getR20_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------17
	row = sheet.getRow(16);

	cell1 = row.getCell(1);
	if (record.getR21_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------18
	row = sheet.getRow(17);

	cell1 = row.getCell(1);
	if (record.getR22_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------19
	row = sheet.getRow(18);

	cell1 = row.getCell(1);
	if (record.getR23_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------20
	row = sheet.getRow(19);

	cell1 = row.getCell(1);
	if (record.getR24_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------21
	row = sheet.getRow(20);

	cell1 = row.getCell(1);
	if (record.getR25_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------22
	row = sheet.getRow(21);

	cell1 = row.getCell(1);
	if (record.getR26_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------23
	row = sheet.getRow(22);

	cell1 = row.getCell(1);
	if (record.getR27_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------24
	row = sheet.getRow(23);

	cell1 = row.getCell(1);
	if (record.getR28_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}



	//-------------------26
	row = sheet.getRow(25);

	cell1 = row.getCell(1);
	if (record.getR30_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------27
	row = sheet.getRow(26);

	cell1 = row.getCell(1);
	if (record.getR31_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------28
	row = sheet.getRow(27);

	cell1 = row.getCell(1);
	if (record.getR32_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------29
	row = sheet.getRow(28);

	cell1 = row.getCell(1);
	if (record.getR33_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------30
	row = sheet.getRow(29);

	cell1 = row.getCell(1);
	if (record.getR34_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------31
	row = sheet.getRow(30);

	cell1 = row.getCell(1);
	if (record.getR35_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------32
	row = sheet.getRow(31);

	cell1 = row.getCell(1);
	if (record.getR36_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------33
	row = sheet.getRow(32);

	cell1 = row.getCell(1);
	if (record.getR37_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}


	//-------------------35



	row = sheet.getRow(34);

	cell1 = row.getCell(1);
	if (record.getR39_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------36
	row = sheet.getRow(35);

	cell1 = row.getCell(1);
	if (record.getR40_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}
		

		//-------------------38
	row = sheet.getRow(37);

	cell1 = row.getCell(1);
	if (record.getR42_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------39
	row = sheet.getRow(38);

	cell1 = row.getCell(1);
	if (record.getR43_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}
		
	//-------------------41	
	row = sheet.getRow(40);

	cell1 = row.getCell(1);
	if (record.getR45_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------42
	row = sheet.getRow(41);

	cell1 = row.getCell(1);
	if (record.getR46_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}	


	//-------------------43
	row = sheet.getRow(42);

	cell1 = row.getCell(1);
	if (record.getR47_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------44
	row = sheet.getRow(43);

	cell1 = row.getCell(1);
	if (record.getR48_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------46
	row = sheet.getRow(45);

	cell1 = row.getCell(1);
	if (record.getR50_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------47
	row = sheet.getRow(46);

	cell1 = row.getCell(1);
	if (record.getR51_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------48
	row = sheet.getRow(47);

	cell1 = row.getCell(1);
	if (record.getR52_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}


	//-------------------50
	row = sheet.getRow(49);

	cell1 = row.getCell(1);
	if (record.getR54_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------51
	row = sheet.getRow(50);

	cell1 = row.getCell(1);
	if (record.getR55_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------52
	row = sheet.getRow(51);

	cell1 = row.getCell(1);
	if (record.getR56_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------54
	row = sheet.getRow(53);

	cell1 = row.getCell(1);
	if (record.getR58_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------55
	row = sheet.getRow(54);

	cell1 = row.getCell(1);
	if (record.getR59_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------56
	row = sheet.getRow(55);

	cell1 = row.getCell(1);
	if (record.getR60_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------57
	row = sheet.getRow(56);

	cell1 = row.getCell(1);
	if (record.getR61_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------58
	row = sheet.getRow(57);

	cell1 = row.getCell(1);
	if (record.getR62_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------59
	row = sheet.getRow(58);

	cell1 = row.getCell(1);
	if (record.getR63_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_GP EMAIL SUMMARY", null, "BRRS_M_GP_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
				}
			}
			


//=====================================================
//ARCHIVAL SUMMARY EXCEL  FORMAT
//=====================================================

// Archival format excel
			public byte[] getExcelM_GPARCHIVAL(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory in Archival.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_M_GPARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 

				List<M_GP_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_GP report. Returning empty result.");
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
							M_GP_Archival_Summary_Entity record = dataList.get(i);
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
						
						
						
						//ROW 11
						row = sheet.getRow(10);

	// row10
										// Column C

										Cell cell1 = row.getCell(1);
										if (record.getR11_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR11_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR11_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR11_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR11_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR11_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(11);

										cell1 = row.getCell(1);
										if (record.getR12_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR12_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR12_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(12);

										cell1 = row.getCell(1);
										if (record.getR13_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR13_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR13_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(13);
										cell1 = row.getCell(1);
										if (record.getR14_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR14_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR14_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(14);

										cell1 = row.getCell(1);
										if (record.getR15_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR15_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR15_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR15_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR15_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR15_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(15);

										cell1 = row.getCell(1);
										if (record.getR16_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR16_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR16_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(16);

										cell1 = row.getCell(1);
										if (record.getR17_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR17_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR17_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(17);
										cell1 = row.getCell(1);
										if (record.getR18_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR18_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR18_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(18);

										cell1 = row.getCell(1);
										if (record.getR19_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR19_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR19_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(19);

										cell1 = row.getCell(1);
										if (record.getR20_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR20_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR20_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(20);

										cell1 = row.getCell(1);
										if (record.getR21_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR21_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR21_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(21);
										cell1 = row.getCell(1);
										if (record.getR22_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR22_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR22_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(22);

										cell1 = row.getCell(1);
										if (record.getR23_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR23_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR23_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(23);

										cell1 = row.getCell(1);
										if (record.getR24_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR24_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR24_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(24);

										cell1 = row.getCell(1);
										if (record.getR25_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR25_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR25_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(25);
										cell1 = row.getCell(1);
										if (record.getR26_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR26_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR26_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(26);

										cell1 = row.getCell(1);
										if (record.getR27_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR27_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR27_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(27);

										cell1 = row.getCell(1);
										if (record.getR28_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR28_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR28_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(28);

										cell1 = row.getCell(1);
										if (record.getR29_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR29_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR29_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR29_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR29_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR29_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(29);
										cell1 = row.getCell(1);
										if (record.getR30_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR30_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR30_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(30);

										cell1 = row.getCell(1);
										if (record.getR31_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR31_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR31_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(31);

										cell1 = row.getCell(1);
										if (record.getR32_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR32_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR32_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(32);

										cell1 = row.getCell(1);
										if (record.getR33_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR33_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR33_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// Column C
										row = sheet.getRow(33);

										cell1 = row.getCell(1);
										if (record.getR34_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR34_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR34_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										row = sheet.getRow(34);

										cell1 = row.getCell(1);
										if (record.getR35_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR35_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR35_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// Column C
										row = sheet.getRow(35);

										cell1 = row.getCell(1);
										if (record.getR36_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR36_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR36_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// Column C
										row = sheet.getRow(36);

										cell1 = row.getCell(1);
										if (record.getR37_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR37_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR37_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										row = sheet.getRow(37);

										cell1 = row.getCell(1);
										if (record.getR38_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR38_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR38_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR38_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR38_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR38_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// Column C
										row = sheet.getRow(38);

										cell1 = row.getCell(1);
										if (record.getR39_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR39_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR39_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R40 =====
										row = sheet.getRow(39);

										cell1 = row.getCell(1);
										if (record.getR40_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR40_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR40_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R41 =====
										row = sheet.getRow(40);

										cell1 = row.getCell(1);
										if (record.getR41_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR41_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR41_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR41_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR41_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR41_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R42 =====
										row = sheet.getRow(41);

										cell1 = row.getCell(1);
										if (record.getR42_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR42_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR42_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R43 =====
										row = sheet.getRow(42);

										cell1 = row.getCell(1);
										if (record.getR43_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR43_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR43_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R44 =====
										row = sheet.getRow(43);

										cell1 = row.getCell(1);
										if (record.getR44_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR44_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR44_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR44_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR44_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR44_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R45 =====
										row = sheet.getRow(44);

										cell1 = row.getCell(1);
										if (record.getR45_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR45_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR45_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R46 =====
										row = sheet.getRow(45);

										cell1 = row.getCell(1);
										if (record.getR46_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR46_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR46_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R47 =====
										row = sheet.getRow(46);

										cell1 = row.getCell(1);
										if (record.getR47_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR47_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR47_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R48 =====
										row = sheet.getRow(47);

										cell1 = row.getCell(1);
										if (record.getR48_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR48_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR48_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R49 =====
										row = sheet.getRow(48);

										cell1 = row.getCell(1);
										if (record.getR49_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR49_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR49_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR49_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR49_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR49_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R50 =====
										row = sheet.getRow(49);

										cell1 = row.getCell(1);
										if (record.getR50_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR50_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR50_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R51 =====
										row = sheet.getRow(50);

										cell1 = row.getCell(1);
										if (record.getR51_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR51_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR51_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R52 =====
										row = sheet.getRow(51);

										cell1 = row.getCell(1);
										if (record.getR52_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR52_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR52_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R53 =====
										row = sheet.getRow(52);

										cell1 = row.getCell(1);
										if (record.getR53_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR53_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR53_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR53_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR53_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR53_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R54 =====
										row = sheet.getRow(53);

										cell1 = row.getCell(1);
										if (record.getR54_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR54_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR54_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R55 =====
										row = sheet.getRow(54);

										cell1 = row.getCell(1);
										if (record.getR55_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR55_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR55_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R56 =====
										row = sheet.getRow(55);

										cell1 = row.getCell(1);
										if (record.getR56_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR56_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR56_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R57 =====
										row = sheet.getRow(56);

										cell1 = row.getCell(1);
										if (record.getR57_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR57_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR57_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR57_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR57_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR57_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R58 =====
										row = sheet.getRow(57);

										cell1 = row.getCell(1);
										if (record.getR58_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR58_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR58_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R59 =====
										row = sheet.getRow(58);

										cell1 = row.getCell(1);
										if (record.getR59_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR59_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR59_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R60 =====
										row = sheet.getRow(59);

										cell1 = row.getCell(1);
										if (record.getR60_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR60_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR60_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R61 =====
										row = sheet.getRow(60);

										cell1 = row.getCell(1);
										if (record.getR61_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR61_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR61_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R62 =====
										row = sheet.getRow(61);

										cell1 = row.getCell(1);
										if (record.getR62_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR62_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR62_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R63 =====
										row = sheet.getRow(62);

										cell1 = row.getCell(1);
										if (record.getR63_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR63_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR63_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R64 =====
										row = sheet.getRow(63);

										cell1 = row.getCell(1);
										if (record.getR64_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR64_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR64_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR64_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR64_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR64_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_GP ARCHIVAL SUMMARY", null, "BRRS_M_GP_ARCHIVALTABLE_SUMMARY");
					}

					return out.toByteArray();
				}

			}


	
//=====================================================
//ARCHIVAL SUMMARY EXCEL  EMAIL
//=====================================================

// Archival Email Excel
			public byte[] BRRS_M_GPARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Archival Email Excel generation process in memory.");

				List<M_GP_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_GP report. Returning empty result.");
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
							M_GP_Archival_Summary_Entity record = dataList.get(i);
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
						
						
						
						//ROW 11
						row = sheet.getRow(7);


										//-------------------8
					
					
					Cell cell1 = row.getCell(1);
					if (record.getR12_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					 cell1 = row.getCell(2);
					if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------9
					
					// Column C
					row = sheet.getRow(8);

					cell1 = row.getCell(1);
					if (record.getR13_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------10
				
					row = sheet.getRow(9);
					
					cell1 = row.getCell(1);
					if (record.getR14_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------12
				
				row = sheet.getRow(11);

					cell1 = row.getCell(1);
					if (record.getR16_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
				
				//-------------------13
				
				row = sheet.getRow(12);

					cell1 = row.getCell(1);
					if (record.getR17_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					
						//-------------------14
						
						
						
	row = sheet.getRow(13);

	cell1 = row.getCell(1);
	if (record.getR18_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------15
	row = sheet.getRow(14);

	cell1 = row.getCell(1);
	if (record.getR19_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------16
	row = sheet.getRow(15);

	cell1 = row.getCell(1);
	if (record.getR20_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------17
	row = sheet.getRow(16);

	cell1 = row.getCell(1);
	if (record.getR21_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------18
	row = sheet.getRow(17);

	cell1 = row.getCell(1);
	if (record.getR22_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------19
	row = sheet.getRow(18);

	cell1 = row.getCell(1);
	if (record.getR23_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------20
	row = sheet.getRow(19);

	cell1 = row.getCell(1);
	if (record.getR24_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------21
	row = sheet.getRow(20);

	cell1 = row.getCell(1);
	if (record.getR25_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------22
	row = sheet.getRow(21);

	cell1 = row.getCell(1);
	if (record.getR26_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------23
	row = sheet.getRow(22);

	cell1 = row.getCell(1);
	if (record.getR27_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------24
	row = sheet.getRow(23);

	cell1 = row.getCell(1);
	if (record.getR28_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}



	//-------------------26
	row = sheet.getRow(25);

	cell1 = row.getCell(1);
	if (record.getR30_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------27
	row = sheet.getRow(26);

	cell1 = row.getCell(1);
	if (record.getR31_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------28
	row = sheet.getRow(27);

	cell1 = row.getCell(1);
	if (record.getR32_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------29
	row = sheet.getRow(28);

	cell1 = row.getCell(1);
	if (record.getR33_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------30
	row = sheet.getRow(29);

	cell1 = row.getCell(1);
	if (record.getR34_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------31
	row = sheet.getRow(30);

	cell1 = row.getCell(1);
	if (record.getR35_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------32
	row = sheet.getRow(31);

	cell1 = row.getCell(1);
	if (record.getR36_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------33
	row = sheet.getRow(32);

	cell1 = row.getCell(1);
	if (record.getR37_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}


	//-------------------35



	row = sheet.getRow(34);

	cell1 = row.getCell(1);
	if (record.getR39_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------36
	row = sheet.getRow(35);

	cell1 = row.getCell(1);
	if (record.getR40_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}
		

		//-------------------38
	row = sheet.getRow(37);

	cell1 = row.getCell(1);
	if (record.getR42_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------39
	row = sheet.getRow(38);

	cell1 = row.getCell(1);
	if (record.getR43_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}
		
	//-------------------41	
	row = sheet.getRow(40);

	cell1 = row.getCell(1);
	if (record.getR45_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------42
	row = sheet.getRow(41);

	cell1 = row.getCell(1);
	if (record.getR46_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}	


	//-------------------43
	row = sheet.getRow(42);

	cell1 = row.getCell(1);
	if (record.getR47_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------44
	row = sheet.getRow(43);

	cell1 = row.getCell(1);
	if (record.getR48_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------46
	row = sheet.getRow(45);

	cell1 = row.getCell(1);
	if (record.getR50_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------47
	row = sheet.getRow(46);

	cell1 = row.getCell(1);
	if (record.getR51_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------48
	row = sheet.getRow(47);

	cell1 = row.getCell(1);
	if (record.getR52_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}


	//-------------------50
	row = sheet.getRow(49);

	cell1 = row.getCell(1);
	if (record.getR54_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------51
	row = sheet.getRow(50);

	cell1 = row.getCell(1);
	if (record.getR55_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------52
	row = sheet.getRow(51);

	cell1 = row.getCell(1);
	if (record.getR56_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------54
	row = sheet.getRow(53);

	cell1 = row.getCell(1);
	if (record.getR58_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------55
	row = sheet.getRow(54);

	cell1 = row.getCell(1);
	if (record.getR59_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------56
	row = sheet.getRow(55);

	cell1 = row.getCell(1);
	if (record.getR60_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------57
	row = sheet.getRow(56);

	cell1 = row.getCell(1);
	if (record.getR61_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------58
	row = sheet.getRow(57);

	cell1 = row.getCell(1);
	if (record.getR62_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------59
	row = sheet.getRow(58);

	cell1 = row.getCell(1);
	if (record.getR63_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);

					
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_GP EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_GP_ARCHIVALTABLE_SUMMARY");
					}

					return out.toByteArray();
				}
			}
	
//=====================================================
// RESUB EXCEL  FORMAT
//=====================================================

		// Resub Format excel
			public byte[] BRRS_M_GPResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_GPEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			}

				List<M_GP_RESUB_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_GP report. Returning empty result.");
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

							M_GP_RESUB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
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
						
						
						
						//ROW 11
						row = sheet.getRow(10);

					// row10
										// Column C

										Cell cell1 = row.getCell(1);
										if (record.getR11_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR11_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR11_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR11_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR11_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR11_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(11);

										cell1 = row.getCell(1);
										if (record.getR12_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR12_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR12_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(12);

										cell1 = row.getCell(1);
										if (record.getR13_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR13_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR13_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(13);
										cell1 = row.getCell(1);
										if (record.getR14_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR14_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR14_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(14);

										cell1 = row.getCell(1);
										if (record.getR15_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR15_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR15_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR15_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR15_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR15_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(15);

										cell1 = row.getCell(1);
										if (record.getR16_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR16_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR16_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(16);

										cell1 = row.getCell(1);
										if (record.getR17_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR17_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR17_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(17);
										cell1 = row.getCell(1);
										if (record.getR18_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR18_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR18_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(18);

										cell1 = row.getCell(1);
										if (record.getR19_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR19_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR19_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(19);

										cell1 = row.getCell(1);
										if (record.getR20_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR20_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR20_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(20);

										cell1 = row.getCell(1);
										if (record.getR21_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR21_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR21_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(21);
										cell1 = row.getCell(1);
										if (record.getR22_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR22_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR22_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(22);

										cell1 = row.getCell(1);
										if (record.getR23_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR23_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR23_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(23);

										cell1 = row.getCell(1);
										if (record.getR24_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR24_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR24_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(24);

										cell1 = row.getCell(1);
										if (record.getR25_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR25_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR25_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(25);
										cell1 = row.getCell(1);
										if (record.getR26_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR26_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR26_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(26);

										cell1 = row.getCell(1);
										if (record.getR27_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR27_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR27_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(27);

										cell1 = row.getCell(1);
										if (record.getR28_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR28_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR28_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(28);

										cell1 = row.getCell(1);
										if (record.getR29_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR29_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR29_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR29_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR29_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR29_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(29);
										cell1 = row.getCell(1);
										if (record.getR30_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR30_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR30_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(30);

										cell1 = row.getCell(1);
										if (record.getR31_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR31_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR31_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(31);

										cell1 = row.getCell(1);
										if (record.getR32_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR32_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR32_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(32);

										cell1 = row.getCell(1);
										if (record.getR33_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR33_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR33_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// Column C
										row = sheet.getRow(33);

										cell1 = row.getCell(1);
										if (record.getR34_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR34_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR34_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										row = sheet.getRow(34);

										cell1 = row.getCell(1);
										if (record.getR35_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR35_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR35_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// Column C
										row = sheet.getRow(35);

										cell1 = row.getCell(1);
										if (record.getR36_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR36_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR36_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// Column C
										row = sheet.getRow(36);

										cell1 = row.getCell(1);
										if (record.getR37_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR37_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR37_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										row = sheet.getRow(37);

										cell1 = row.getCell(1);
										if (record.getR38_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR38_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR38_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR38_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR38_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR38_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// Column C
										row = sheet.getRow(38);

										cell1 = row.getCell(1);
										if (record.getR39_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR39_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR39_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R40 =====
										row = sheet.getRow(39);

										cell1 = row.getCell(1);
										if (record.getR40_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR40_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR40_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R41 =====
										row = sheet.getRow(40);

										cell1 = row.getCell(1);
										if (record.getR41_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR41_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR41_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR41_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR41_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR41_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R42 =====
										row = sheet.getRow(41);

										cell1 = row.getCell(1);
										if (record.getR42_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR42_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR42_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R43 =====
										row = sheet.getRow(42);

										cell1 = row.getCell(1);
										if (record.getR43_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR43_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR43_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R44 =====
										row = sheet.getRow(43);

										cell1 = row.getCell(1);
										if (record.getR44_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR44_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR44_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR44_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR44_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR44_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R45 =====
										row = sheet.getRow(44);

										cell1 = row.getCell(1);
										if (record.getR45_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR45_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR45_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R46 =====
										row = sheet.getRow(45);

										cell1 = row.getCell(1);
										if (record.getR46_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR46_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR46_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R47 =====
										row = sheet.getRow(46);

										cell1 = row.getCell(1);
										if (record.getR47_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR47_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR47_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R48 =====
										row = sheet.getRow(47);

										cell1 = row.getCell(1);
										if (record.getR48_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR48_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR48_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R49 =====
										row = sheet.getRow(48);

										cell1 = row.getCell(1);
										if (record.getR49_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR49_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR49_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR49_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR49_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR49_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R50 =====
										row = sheet.getRow(49);

										cell1 = row.getCell(1);
										if (record.getR50_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR50_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR50_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R51 =====
										row = sheet.getRow(50);

										cell1 = row.getCell(1);
										if (record.getR51_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR51_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR51_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R52 =====
										row = sheet.getRow(51);

										cell1 = row.getCell(1);
										if (record.getR52_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR52_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR52_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R53 =====
										row = sheet.getRow(52);

										cell1 = row.getCell(1);
										if (record.getR53_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR53_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR53_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR53_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR53_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR53_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R54 =====
										row = sheet.getRow(53);

										cell1 = row.getCell(1);
										if (record.getR54_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR54_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR54_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R55 =====
										row = sheet.getRow(54);

										cell1 = row.getCell(1);
										if (record.getR55_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR55_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR55_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R56 =====
										row = sheet.getRow(55);

										cell1 = row.getCell(1);
										if (record.getR56_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR56_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR56_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R57 =====
										row = sheet.getRow(56);

										cell1 = row.getCell(1);
										if (record.getR57_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR57_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR57_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR57_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR57_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR57_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R58 =====
										row = sheet.getRow(57);

										cell1 = row.getCell(1);
										if (record.getR58_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR58_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR58_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R59 =====
										row = sheet.getRow(58);

										cell1 = row.getCell(1);
										if (record.getR59_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR59_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR59_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R60 =====
										row = sheet.getRow(59);

										cell1 = row.getCell(1);
										if (record.getR60_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR60_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR60_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R61 =====
										row = sheet.getRow(60);

										cell1 = row.getCell(1);
										if (record.getR61_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR61_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR61_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R62 =====
										row = sheet.getRow(61);

										cell1 = row.getCell(1);
										if (record.getR62_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR62_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR62_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R63 =====
										row = sheet.getRow(62);

										cell1 = row.getCell(1);
										if (record.getR63_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR63_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR63_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R64 =====
										row = sheet.getRow(63);

										cell1 = row.getCell(1);
										if (record.getR64_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR64_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR64_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR64_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR64_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR64_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_GP RESUB SUMMARY", null, "BRRS_M_GP_RESUB_SUMMARYTABLE");
					}

					return out.toByteArray();
				}

			}

	
//=====================================================
// RESUB  EXCEL EMAIL
//=====================================================

	
			// Resub Email Excel
			public byte[] BRRS_M_GPEmailResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting RESUB Email Excel generation process in memory.");

				List<M_GP_RESUB_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_GP report. Returning empty result.");
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
							M_GP_RESUB_Summary_Entity record = dataList.get(i);
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
						
						
						
						//ROW 11
						row = sheet.getRow(7);

		//-------------------8
					
					
					Cell cell1 = row.getCell(1);
					if (record.getR12_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					 cell1 = row.getCell(2);
					if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------9
					
					// Column C
					row = sheet.getRow(8);

					cell1 = row.getCell(1);
					if (record.getR13_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------10
				
					row = sheet.getRow(9);
					
					cell1 = row.getCell(1);
					if (record.getR14_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------12
				
				row = sheet.getRow(11);

					cell1 = row.getCell(1);
					if (record.getR16_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
				
				//-------------------13
				
				row = sheet.getRow(12);

					cell1 = row.getCell(1);
					if (record.getR17_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					
						//-------------------14
						
						
						
	row = sheet.getRow(13);

	cell1 = row.getCell(1);
	if (record.getR18_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------15
	row = sheet.getRow(14);

	cell1 = row.getCell(1);
	if (record.getR19_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------16
	row = sheet.getRow(15);

	cell1 = row.getCell(1);
	if (record.getR20_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------17
	row = sheet.getRow(16);

	cell1 = row.getCell(1);
	if (record.getR21_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------18
	row = sheet.getRow(17);

	cell1 = row.getCell(1);
	if (record.getR22_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------19
	row = sheet.getRow(18);

	cell1 = row.getCell(1);
	if (record.getR23_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------20
	row = sheet.getRow(19);

	cell1 = row.getCell(1);
	if (record.getR24_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------21
	row = sheet.getRow(20);

	cell1 = row.getCell(1);
	if (record.getR25_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------22
	row = sheet.getRow(21);

	cell1 = row.getCell(1);
	if (record.getR26_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------23
	row = sheet.getRow(22);

	cell1 = row.getCell(1);
	if (record.getR27_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------24
	row = sheet.getRow(23);

	cell1 = row.getCell(1);
	if (record.getR28_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}



	//-------------------26
	row = sheet.getRow(25);

	cell1 = row.getCell(1);
	if (record.getR30_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------27
	row = sheet.getRow(26);

	cell1 = row.getCell(1);
	if (record.getR31_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------28
	row = sheet.getRow(27);

	cell1 = row.getCell(1);
	if (record.getR32_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------29
	row = sheet.getRow(28);

	cell1 = row.getCell(1);
	if (record.getR33_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------30
	row = sheet.getRow(29);

	cell1 = row.getCell(1);
	if (record.getR34_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------31
	row = sheet.getRow(30);

	cell1 = row.getCell(1);
	if (record.getR35_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------32
	row = sheet.getRow(31);

	cell1 = row.getCell(1);
	if (record.getR36_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------33
	row = sheet.getRow(32);

	cell1 = row.getCell(1);
	if (record.getR37_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}


	//-------------------35



	row = sheet.getRow(34);

	cell1 = row.getCell(1);
	if (record.getR39_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------36
	row = sheet.getRow(35);

	cell1 = row.getCell(1);
	if (record.getR40_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}
		

		//-------------------38
	row = sheet.getRow(37);

	cell1 = row.getCell(1);
	if (record.getR42_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------39
	row = sheet.getRow(38);

	cell1 = row.getCell(1);
	if (record.getR43_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}
		
	//-------------------41	
	row = sheet.getRow(40);

	cell1 = row.getCell(1);
	if (record.getR45_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------42
	row = sheet.getRow(41);

	cell1 = row.getCell(1);
	if (record.getR46_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}	


	//-------------------43
	row = sheet.getRow(42);

	cell1 = row.getCell(1);
	if (record.getR47_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------44
	row = sheet.getRow(43);

	cell1 = row.getCell(1);
	if (record.getR48_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------46
	row = sheet.getRow(45);

	cell1 = row.getCell(1);
	if (record.getR50_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------47
	row = sheet.getRow(46);

	cell1 = row.getCell(1);
	if (record.getR51_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------48
	row = sheet.getRow(47);

	cell1 = row.getCell(1);
	if (record.getR52_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}


	//-------------------50
	row = sheet.getRow(49);

	cell1 = row.getCell(1);
	if (record.getR54_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------51
	row = sheet.getRow(50);

	cell1 = row.getCell(1);
	if (record.getR55_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------52
	row = sheet.getRow(51);

	cell1 = row.getCell(1);
	if (record.getR56_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------54
	row = sheet.getRow(53);

	cell1 = row.getCell(1);
	if (record.getR58_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------55
	row = sheet.getRow(54);

	cell1 = row.getCell(1);
	if (record.getR59_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------56
	row = sheet.getRow(55);

	cell1 = row.getCell(1);
	if (record.getR60_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------57
	row = sheet.getRow(56);

	cell1 = row.getCell(1);
	if (record.getR61_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------58
	row = sheet.getRow(57);

	cell1 = row.getCell(1);
	if (record.getR62_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------59
	row = sheet.getRow(58);

	cell1 = row.getCell(1);
	if (record.getR63_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);

					
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_GP EMAIL RESUB SUMMARY", null, "BRRS_M_GP_RESUB_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
			}

		
	}