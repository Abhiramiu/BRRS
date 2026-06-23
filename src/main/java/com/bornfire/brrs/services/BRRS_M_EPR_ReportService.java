
//========================EPR MANUAL REPORT 

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

public class BRRS_M_EPR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_EPR_ReportService.class);
	
	
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


	public List<M_EPR_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_M_EPR_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new M_EPR_Summary_RowMapper()
    );
}
	
	public M_EPR_Summary_Entity findByReportDate(Date reportDate) {

	    String sql =
	            "SELECT * FROM BRRS_M_EPR_SUMMARYTABLE " +
	            "WHERE REPORT_DATE = ?";

	    return jdbcTemplate.queryForObject(
	            sql,
	            new Object[]{reportDate},
	            new M_EPR_Summary_RowMapper()
	    );
	}
// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================



public List<Object[]> get_M_EPR_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_EPR_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<M_EPR_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_M_EPR_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new M_EPR_Archival_Summary_RowMapper()
    );
}

public List<M_EPR_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_M_EPR_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new M_EPR_Archival_Summary_RowMapper()
    );
}

public BigDecimal findMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_EPR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            BigDecimal.class
    );
}

// =====================================================
// DETAIL REPO
// =====================================================	


public List<M_EPR_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_M_EPR_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new M_EPR_Detail_RowMapper()
    );
}



// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================


public List<Map<String, Object>> getM_EPR_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_EPR_ARCHIVALTABLE_DETAIL " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.queryForList(sql);
}


public List<M_EPR_Archival_Detail_Entity> getDetaildatabydateListarchival(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * " +
                 "FROM BRRS_M_EPR_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? " +
                 "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportDate, reportVersion },
            new M_EPR_Archival_Detail_RowMapper()
    );
}

public BigDecimal findArchivalDetailMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) " +
                 "FROM BRRS_M_EPR_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[] { reportDate },
            BigDecimal.class
    );
}

public M_EPR_Archival_Detail_Entity getArchivalListWithVersion() {

    String sql = "SELECT * " +
                 "FROM BRRS_M_EPR_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC " +
                 "FETCH FIRST 1 ROWS ONLY";

    return jdbcTemplate.queryForObject(
            sql,
            new M_EPR_Archival_Detail_RowMapper()
    );
}

// =====================================================
// RESUB SUMMARY
// =====================================================

public List<M_EPR_RESUB_Summary_Entity> getResubSummarydatabydateListarchival(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * " +
                 "FROM BRRS_M_EPR_RESUB_SUMMARYTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new M_EPR_RESUB_Summary_RowMapper()
    );
}


public BigDecimal findResubSummaryMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) " +
                 "FROM BRRS_M_EPR_RESUB_SUMMARYTABLE " +
                 "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            BigDecimal.class
    );
}

public List<Map<String, Object>> getM_EPR_Archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_EPR_RESUB_SUMMARYTABLE " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.queryForList(sql);
}

public M_EPR_RESUB_Summary_Entity getResubSummarydatabydateListWithVersion() {

    String sql = "SELECT * " +
                 "FROM BRRS_M_EPR_RESUB_SUMMARYTABLE " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC " +
                 "FETCH FIRST 1 ROWS ONLY";

    return jdbcTemplate.queryForObject(
            sql,
            new M_EPR_RESUB_Summary_RowMapper()
    );
}



// =====================================================
// RESUB DETAIL
// =====================================================


public List<Map<String, Object>> get_M_EPR_Archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_EPR_RESUB_DETAILTABLE " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.queryForList(sql);
}

public List<M_EPR_RESUB_Detail_Entity> getResubDetaildatabydateList(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * " +
                 "FROM BRRS_M_EPR_RESUB_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportDate, reportVersion },
            new M_EPR_RESUB_Detail_RowMapper()
    );
}

public BigDecimal findResubDetailMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) " +
                 "FROM BRRS_M_EPR_RESUB_DETAILTABLE " +
                 "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[] { reportDate },
            BigDecimal.class
    );
}

public M_EPR_RESUB_Detail_Entity getdResubDetailDatabydateListWithVersion() {

    String sql = "SELECT * " +
                 "FROM BRRS_M_EPR_RESUB_DETAILTABLE " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC " +
                 "FETCH FIRST 1 ROWS ONLY";

    return jdbcTemplate.queryForObject(
            sql,
            new M_EPR_RESUB_Detail_RowMapper()
    );
}

// =====================================================
// SUMAMRY ENTITY & ROW MAPPER 
// =====================================================


public class M_EPR_Summary_RowMapper implements RowMapper<M_EPR_Summary_Entity> {

    @Override
    public M_EPR_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_EPR_Summary_Entity obj = new M_EPR_Summary_Entity();

// =========================
// R11
// =========================
obj.setR11_market(rs.getBigDecimal("r11_market"));
obj.setR11_gpfsr_nom_amt(rs.getBigDecimal("r11_gpfsr_nom_amt"));
obj.setR11_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att8_per_spe_ris"));
obj.setR11_gpfsr_chrg(rs.getBigDecimal("r11_gpfsr_chrg"));
obj.setR11_gpfsr_nom_amt1(rs.getBigDecimal("r11_gpfsr_nom_amt1"));
obj.setR11_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att4_per_spe_ris"));
obj.setR11_gpfsr_chrg1(rs.getBigDecimal("r11_gpfsr_chrg1"));
obj.setR11_gpfsr_nom_amt2(rs.getBigDecimal("r11_gpfsr_nom_amt2"));
obj.setR11_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att2_per_spe_ris"));
obj.setR11_gpfsr_chrg2(rs.getBigDecimal("r11_gpfsr_chrg2"));
obj.setR11_tot_spe_ris_chrg(rs.getBigDecimal("r11_tot_spe_ris_chrg"));
obj.setR11_net_pos_gen_mar_ris(rs.getBigDecimal("r11_net_pos_gen_mar_ris"));
obj.setR11_gen_mar_ris_chrg_8per(rs.getBigDecimal("r11_gen_mar_ris_chrg_8per"));
obj.setR11_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r11_2per_gen_mar_ris_chrg_div_port"));
obj.setR11_tot_gen_mar_risk_chrg(rs.getBigDecimal("r11_tot_gen_mar_risk_chrg"));
obj.setR11_tot_mar_ris_chrg(rs.getBigDecimal("r11_tot_mar_ris_chrg"));

// =========================
// R12
// =========================
obj.setR12_market(rs.getBigDecimal("r12_market"));
obj.setR12_gpfsr_nom_amt(rs.getBigDecimal("r12_gpfsr_nom_amt"));
obj.setR12_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att8_per_spe_ris"));
obj.setR12_gpfsr_chrg(rs.getBigDecimal("r12_gpfsr_chrg"));
obj.setR12_gpfsr_nom_amt1(rs.getBigDecimal("r12_gpfsr_nom_amt1"));
obj.setR12_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att4_per_spe_ris"));
obj.setR12_gpfsr_chrg1(rs.getBigDecimal("r12_gpfsr_chrg1"));
obj.setR12_gpfsr_nom_amt2(rs.getBigDecimal("r12_gpfsr_nom_amt2"));
obj.setR12_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att2_per_spe_ris"));
obj.setR12_gpfsr_chrg2(rs.getBigDecimal("r12_gpfsr_chrg2"));
obj.setR12_tot_spe_ris_chrg(rs.getBigDecimal("r12_tot_spe_ris_chrg"));
obj.setR12_net_pos_gen_mar_ris(rs.getBigDecimal("r12_net_pos_gen_mar_ris"));
obj.setR12_gen_mar_ris_chrg_8per(rs.getBigDecimal("r12_gen_mar_ris_chrg_8per"));
obj.setR12_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r12_2per_gen_mar_ris_chrg_div_port"));
obj.setR12_tot_gen_mar_risk_chrg(rs.getBigDecimal("r12_tot_gen_mar_risk_chrg"));
obj.setR12_tot_mar_ris_chrg(rs.getBigDecimal("r12_tot_mar_ris_chrg"));

// =========================
// R13
// =========================
obj.setR13_market(rs.getBigDecimal("r13_market"));
obj.setR13_gpfsr_nom_amt(rs.getBigDecimal("r13_gpfsr_nom_amt"));
obj.setR13_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att8_per_spe_ris"));
obj.setR13_gpfsr_chrg(rs.getBigDecimal("r13_gpfsr_chrg"));
obj.setR13_gpfsr_nom_amt1(rs.getBigDecimal("r13_gpfsr_nom_amt1"));
obj.setR13_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att4_per_spe_ris"));
obj.setR13_gpfsr_chrg1(rs.getBigDecimal("r13_gpfsr_chrg1"));
obj.setR13_gpfsr_nom_amt2(rs.getBigDecimal("r13_gpfsr_nom_amt2"));
obj.setR13_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att2_per_spe_ris"));
obj.setR13_gpfsr_chrg2(rs.getBigDecimal("r13_gpfsr_chrg2"));
obj.setR13_tot_spe_ris_chrg(rs.getBigDecimal("r13_tot_spe_ris_chrg"));
obj.setR13_net_pos_gen_mar_ris(rs.getBigDecimal("r13_net_pos_gen_mar_ris"));
obj.setR13_gen_mar_ris_chrg_8per(rs.getBigDecimal("r13_gen_mar_ris_chrg_8per"));
obj.setR13_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r13_2per_gen_mar_ris_chrg_div_port"));
obj.setR13_tot_gen_mar_risk_chrg(rs.getBigDecimal("r13_tot_gen_mar_risk_chrg"));
obj.setR13_tot_mar_ris_chrg(rs.getBigDecimal("r13_tot_mar_ris_chrg"));

// =========================
// R14
// =========================
obj.setR14_market(rs.getBigDecimal("r14_market"));
obj.setR14_gpfsr_nom_amt(rs.getBigDecimal("r14_gpfsr_nom_amt"));
obj.setR14_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att8_per_spe_ris"));
obj.setR14_gpfsr_chrg(rs.getBigDecimal("r14_gpfsr_chrg"));
obj.setR14_gpfsr_nom_amt1(rs.getBigDecimal("r14_gpfsr_nom_amt1"));
obj.setR14_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att4_per_spe_ris"));
obj.setR14_gpfsr_chrg1(rs.getBigDecimal("r14_gpfsr_chrg1"));
obj.setR14_gpfsr_nom_amt2(rs.getBigDecimal("r14_gpfsr_nom_amt2"));
obj.setR14_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att2_per_spe_ris"));
obj.setR14_gpfsr_chrg2(rs.getBigDecimal("r14_gpfsr_chrg2"));
obj.setR14_tot_spe_ris_chrg(rs.getBigDecimal("r14_tot_spe_ris_chrg"));
obj.setR14_net_pos_gen_mar_ris(rs.getBigDecimal("r14_net_pos_gen_mar_ris"));
obj.setR14_gen_mar_ris_chrg_8per(rs.getBigDecimal("r14_gen_mar_ris_chrg_8per"));
obj.setR14_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r14_2per_gen_mar_ris_chrg_div_port"));
obj.setR14_tot_gen_mar_risk_chrg(rs.getBigDecimal("r14_tot_gen_mar_risk_chrg"));
obj.setR14_tot_mar_ris_chrg(rs.getBigDecimal("r14_tot_mar_ris_chrg"));

// =========================
// R15
// =========================
obj.setR15_market(rs.getBigDecimal("r15_market"));
obj.setR15_gpfsr_nom_amt(rs.getBigDecimal("r15_gpfsr_nom_amt"));
obj.setR15_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att8_per_spe_ris"));
obj.setR15_gpfsr_chrg(rs.getBigDecimal("r15_gpfsr_chrg"));
obj.setR15_gpfsr_nom_amt1(rs.getBigDecimal("r15_gpfsr_nom_amt1"));
obj.setR15_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att4_per_spe_ris"));
obj.setR15_gpfsr_chrg1(rs.getBigDecimal("r15_gpfsr_chrg1"));
obj.setR15_gpfsr_nom_amt2(rs.getBigDecimal("r15_gpfsr_nom_amt2"));
obj.setR15_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att2_per_spe_ris"));
obj.setR15_gpfsr_chrg2(rs.getBigDecimal("r15_gpfsr_chrg2"));
obj.setR15_tot_spe_ris_chrg(rs.getBigDecimal("r15_tot_spe_ris_chrg"));
obj.setR15_net_pos_gen_mar_ris(rs.getBigDecimal("r15_net_pos_gen_mar_ris"));
obj.setR15_gen_mar_ris_chrg_8per(rs.getBigDecimal("r15_gen_mar_ris_chrg_8per"));
obj.setR15_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r15_2per_gen_mar_ris_chrg_div_port"));
obj.setR15_tot_gen_mar_risk_chrg(rs.getBigDecimal("r15_tot_gen_mar_risk_chrg"));
obj.setR15_tot_mar_ris_chrg(rs.getBigDecimal("r15_tot_mar_ris_chrg"));

// =========================
// R16
// =========================
obj.setR16_market(rs.getBigDecimal("r16_market"));
obj.setR16_gpfsr_nom_amt(rs.getBigDecimal("r16_gpfsr_nom_amt"));
obj.setR16_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att8_per_spe_ris"));
obj.setR16_gpfsr_chrg(rs.getBigDecimal("r16_gpfsr_chrg"));
obj.setR16_gpfsr_nom_amt1(rs.getBigDecimal("r16_gpfsr_nom_amt1"));
obj.setR16_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att4_per_spe_ris"));
obj.setR16_gpfsr_chrg1(rs.getBigDecimal("r16_gpfsr_chrg1"));
obj.setR16_gpfsr_nom_amt2(rs.getBigDecimal("r16_gpfsr_nom_amt2"));
obj.setR16_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att2_per_spe_ris"));
obj.setR16_gpfsr_chrg2(rs.getBigDecimal("r16_gpfsr_chrg2"));
obj.setR16_tot_spe_ris_chrg(rs.getBigDecimal("r16_tot_spe_ris_chrg"));
obj.setR16_net_pos_gen_mar_ris(rs.getBigDecimal("r16_net_pos_gen_mar_ris"));
obj.setR16_gen_mar_ris_chrg_8per(rs.getBigDecimal("r16_gen_mar_ris_chrg_8per"));
obj.setR16_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r16_2per_gen_mar_ris_chrg_div_port"));
obj.setR16_tot_gen_mar_risk_chrg(rs.getBigDecimal("r16_tot_gen_mar_risk_chrg"));
obj.setR16_tot_mar_ris_chrg(rs.getBigDecimal("r16_tot_mar_ris_chrg"));

// =========================
// R17
// =========================
obj.setR17_market(rs.getBigDecimal("r17_market"));
obj.setR17_gpfsr_nom_amt(rs.getBigDecimal("r17_gpfsr_nom_amt"));
obj.setR17_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att8_per_spe_ris"));
obj.setR17_gpfsr_chrg(rs.getBigDecimal("r17_gpfsr_chrg"));
obj.setR17_gpfsr_nom_amt1(rs.getBigDecimal("r17_gpfsr_nom_amt1"));
obj.setR17_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att4_per_spe_ris"));
obj.setR17_gpfsr_chrg1(rs.getBigDecimal("r17_gpfsr_chrg1"));
obj.setR17_gpfsr_nom_amt2(rs.getBigDecimal("r17_gpfsr_nom_amt2"));
obj.setR17_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att2_per_spe_ris"));
obj.setR17_gpfsr_chrg2(rs.getBigDecimal("r17_gpfsr_chrg2"));
obj.setR17_tot_spe_ris_chrg(rs.getBigDecimal("r17_tot_spe_ris_chrg"));
obj.setR17_net_pos_gen_mar_ris(rs.getBigDecimal("r17_net_pos_gen_mar_ris"));
obj.setR17_gen_mar_ris_chrg_8per(rs.getBigDecimal("r17_gen_mar_ris_chrg_8per"));
obj.setR17_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r17_2per_gen_mar_ris_chrg_div_port"));
obj.setR17_tot_gen_mar_risk_chrg(rs.getBigDecimal("r17_tot_gen_mar_risk_chrg"));
obj.setR17_tot_mar_ris_chrg(rs.getBigDecimal("r17_tot_mar_ris_chrg"));

// =========================
// R18
// =========================
obj.setR18_market(rs.getBigDecimal("r18_market"));
obj.setR18_gpfsr_nom_amt(rs.getBigDecimal("r18_gpfsr_nom_amt"));
obj.setR18_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att8_per_spe_ris"));
obj.setR18_gpfsr_chrg(rs.getBigDecimal("r18_gpfsr_chrg"));
obj.setR18_gpfsr_nom_amt1(rs.getBigDecimal("r18_gpfsr_nom_amt1"));
obj.setR18_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att4_per_spe_ris"));
obj.setR18_gpfsr_chrg1(rs.getBigDecimal("r18_gpfsr_chrg1"));
obj.setR18_gpfsr_nom_amt2(rs.getBigDecimal("r18_gpfsr_nom_amt2"));
obj.setR18_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att2_per_spe_ris"));
obj.setR18_gpfsr_chrg2(rs.getBigDecimal("r18_gpfsr_chrg2"));
obj.setR18_tot_spe_ris_chrg(rs.getBigDecimal("r18_tot_spe_ris_chrg"));
obj.setR18_net_pos_gen_mar_ris(rs.getBigDecimal("r18_net_pos_gen_mar_ris"));
obj.setR18_gen_mar_ris_chrg_8per(rs.getBigDecimal("r18_gen_mar_ris_chrg_8per"));
obj.setR18_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r18_2per_gen_mar_ris_chrg_div_port"));
obj.setR18_tot_gen_mar_risk_chrg(rs.getBigDecimal("r18_tot_gen_mar_risk_chrg"));
obj.setR18_tot_mar_ris_chrg(rs.getBigDecimal("r18_tot_mar_ris_chrg"));

// =========================
// R19
// =========================
obj.setR19_market(rs.getBigDecimal("r19_market"));
obj.setR19_gpfsr_nom_amt(rs.getBigDecimal("r19_gpfsr_nom_amt"));
obj.setR19_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att8_per_spe_ris"));
obj.setR19_gpfsr_chrg(rs.getBigDecimal("r19_gpfsr_chrg"));
obj.setR19_gpfsr_nom_amt1(rs.getBigDecimal("r19_gpfsr_nom_amt1"));
obj.setR19_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att4_per_spe_ris"));
obj.setR19_gpfsr_chrg1(rs.getBigDecimal("r19_gpfsr_chrg1"));
obj.setR19_gpfsr_nom_amt2(rs.getBigDecimal("r19_gpfsr_nom_amt2"));
obj.setR19_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att2_per_spe_ris"));
obj.setR19_gpfsr_chrg2(rs.getBigDecimal("r19_gpfsr_chrg2"));
obj.setR19_tot_spe_ris_chrg(rs.getBigDecimal("r19_tot_spe_ris_chrg"));
obj.setR19_net_pos_gen_mar_ris(rs.getBigDecimal("r19_net_pos_gen_mar_ris"));
obj.setR19_gen_mar_ris_chrg_8per(rs.getBigDecimal("r19_gen_mar_ris_chrg_8per"));
obj.setR19_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r19_2per_gen_mar_ris_chrg_div_port"));
obj.setR19_tot_gen_mar_risk_chrg(rs.getBigDecimal("r19_tot_gen_mar_risk_chrg"));
obj.setR19_tot_mar_ris_chrg(rs.getBigDecimal("r19_tot_mar_ris_chrg"));

// =========================
// R20
// =========================
obj.setR20_market(rs.getBigDecimal("r20_market"));
obj.setR20_gpfsr_nom_amt(rs.getBigDecimal("r20_gpfsr_nom_amt"));
obj.setR20_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att8_per_spe_ris"));
obj.setR20_gpfsr_chrg(rs.getBigDecimal("r20_gpfsr_chrg"));
obj.setR20_gpfsr_nom_amt1(rs.getBigDecimal("r20_gpfsr_nom_amt1"));
obj.setR20_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att4_per_spe_ris"));
obj.setR20_gpfsr_chrg1(rs.getBigDecimal("r20_gpfsr_chrg1"));
obj.setR20_gpfsr_nom_amt2(rs.getBigDecimal("r20_gpfsr_nom_amt2"));
obj.setR20_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att2_per_spe_ris"));
obj.setR20_gpfsr_chrg2(rs.getBigDecimal("r20_gpfsr_chrg2"));
obj.setR20_tot_spe_ris_chrg(rs.getBigDecimal("r20_tot_spe_ris_chrg"));
obj.setR20_net_pos_gen_mar_ris(rs.getBigDecimal("r20_net_pos_gen_mar_ris"));
obj.setR20_gen_mar_ris_chrg_8per(rs.getBigDecimal("r20_gen_mar_ris_chrg_8per"));
obj.setR20_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r20_2per_gen_mar_ris_chrg_div_port"));
obj.setR20_tot_gen_mar_risk_chrg(rs.getBigDecimal("r20_tot_gen_mar_risk_chrg"));
obj.setR20_tot_mar_ris_chrg(rs.getBigDecimal("r20_tot_mar_ris_chrg"));

// =========================
// R21
// =========================
obj.setR21_market(rs.getBigDecimal("r21_market"));
obj.setR21_gpfsr_nom_amt(rs.getBigDecimal("r21_gpfsr_nom_amt"));
obj.setR21_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att8_per_spe_ris"));
obj.setR21_gpfsr_chrg(rs.getBigDecimal("r21_gpfsr_chrg"));
obj.setR21_gpfsr_nom_amt1(rs.getBigDecimal("r21_gpfsr_nom_amt1"));
obj.setR21_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att4_per_spe_ris"));
obj.setR21_gpfsr_chrg1(rs.getBigDecimal("r21_gpfsr_chrg1"));
obj.setR21_gpfsr_nom_amt2(rs.getBigDecimal("r21_gpfsr_nom_amt2"));
obj.setR21_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att2_per_spe_ris"));
obj.setR21_gpfsr_chrg2(rs.getBigDecimal("r21_gpfsr_chrg2"));
obj.setR21_tot_spe_ris_chrg(rs.getBigDecimal("r21_tot_spe_ris_chrg"));
obj.setR21_net_pos_gen_mar_ris(rs.getBigDecimal("r21_net_pos_gen_mar_ris"));
obj.setR21_gen_mar_ris_chrg_8per(rs.getBigDecimal("r21_gen_mar_ris_chrg_8per"));
obj.setR21_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r21_2per_gen_mar_ris_chrg_div_port"));
obj.setR21_tot_gen_mar_risk_chrg(rs.getBigDecimal("r21_tot_gen_mar_risk_chrg"));
obj.setR21_tot_mar_ris_chrg(rs.getBigDecimal("r21_tot_mar_ris_chrg"));


// =========================
// R22
// =========================
obj.setR22_market(rs.getBigDecimal("r22_market"));
obj.setR22_gpfsr_nom_amt(rs.getBigDecimal("r22_gpfsr_nom_amt"));
obj.setR22_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att8_per_spe_ris"));
obj.setR22_gpfsr_chrg(rs.getBigDecimal("r22_gpfsr_chrg"));
obj.setR22_gpfsr_nom_amt1(rs.getBigDecimal("r22_gpfsr_nom_amt1"));
obj.setR22_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att4_per_spe_ris"));
obj.setR22_gpfsr_chrg1(rs.getBigDecimal("r22_gpfsr_chrg1"));
obj.setR22_gpfsr_nom_amt2(rs.getBigDecimal("r22_gpfsr_nom_amt2"));
obj.setR22_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att2_per_spe_ris"));
obj.setR22_gpfsr_chrg2(rs.getBigDecimal("r22_gpfsr_chrg2"));
obj.setR22_tot_spe_ris_chrg(rs.getBigDecimal("r22_tot_spe_ris_chrg"));
obj.setR22_net_pos_gen_mar_ris(rs.getBigDecimal("r22_net_pos_gen_mar_ris"));
obj.setR22_gen_mar_ris_chrg_8per(rs.getBigDecimal("r22_gen_mar_ris_chrg_8per"));
obj.setR22_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r22_2per_gen_mar_ris_chrg_div_port"));
obj.setR22_tot_gen_mar_risk_chrg(rs.getBigDecimal("r22_tot_gen_mar_risk_chrg"));
obj.setR22_tot_mar_ris_chrg(rs.getBigDecimal("r22_tot_mar_ris_chrg"));

// =========================
// R23
// =========================
obj.setR23_market(rs.getString("r23_market"));
obj.setR23_gpfsr_nom_amt(rs.getBigDecimal("r23_gpfsr_nom_amt"));
obj.setR23_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att8_per_spe_ris"));
obj.setR23_gpfsr_chrg(rs.getBigDecimal("r23_gpfsr_chrg"));
obj.setR23_gpfsr_nom_amt1(rs.getBigDecimal("r23_gpfsr_nom_amt1"));
obj.setR23_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att4_per_spe_ris"));
obj.setR23_gpfsr_chrg1(rs.getBigDecimal("r23_gpfsr_chrg1"));
obj.setR23_gpfsr_nom_amt2(rs.getBigDecimal("r23_gpfsr_nom_amt2"));
obj.setR23_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att2_per_spe_ris"));
obj.setR23_gpfsr_chrg2(rs.getBigDecimal("r23_gpfsr_chrg2"));
obj.setR23_tot_spe_ris_chrg(rs.getBigDecimal("r23_tot_spe_ris_chrg"));
obj.setR23_net_pos_gen_mar_ris(rs.getBigDecimal("r23_net_pos_gen_mar_ris"));
obj.setR23_gen_mar_ris_chrg_8per(rs.getBigDecimal("r23_gen_mar_ris_chrg_8per"));
obj.setR23_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r23_2per_gen_mar_ris_chrg_div_port"));
obj.setR23_tot_gen_mar_risk_chrg(rs.getBigDecimal("r23_tot_gen_mar_risk_chrg"));
obj.setR23_tot_mar_ris_chrg(rs.getBigDecimal("r23_tot_mar_ris_chrg"));


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


public class M_EPR_Summary_Entity {
	
private BigDecimal	r11_market;
	private BigDecimal	r11_gpfsr_nom_amt;
	private BigDecimal	r11_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg;
	private BigDecimal	r11_gpfsr_nom_amt1;
	private BigDecimal	r11_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg1;
	private BigDecimal	r11_gpfsr_nom_amt2;
	private BigDecimal	r11_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg2;
	private BigDecimal	r11_tot_spe_ris_chrg;
	private BigDecimal	r11_net_pos_gen_mar_ris;
	private BigDecimal	r11_gen_mar_ris_chrg_8per;
	private BigDecimal	r11_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r11_tot_gen_mar_risk_chrg;
	private BigDecimal	r11_tot_mar_ris_chrg;
	
	private BigDecimal	r12_market;
	private BigDecimal	r12_gpfsr_nom_amt;
	private BigDecimal	r12_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg;
	private BigDecimal	r12_gpfsr_nom_amt1;
	private BigDecimal	r12_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg1;
	private BigDecimal	r12_gpfsr_nom_amt2;
	private BigDecimal	r12_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg2;
	private BigDecimal	r12_tot_spe_ris_chrg;
	private BigDecimal	r12_net_pos_gen_mar_ris;
	private BigDecimal	r12_gen_mar_ris_chrg_8per;
	private BigDecimal	r12_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r12_tot_gen_mar_risk_chrg;
	private BigDecimal	r12_tot_mar_ris_chrg;
	
	private BigDecimal	r13_market;
	private BigDecimal	r13_gpfsr_nom_amt;
	private BigDecimal	r13_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg;
	private BigDecimal	r13_gpfsr_nom_amt1;
	private BigDecimal	r13_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg1;
	private BigDecimal	r13_gpfsr_nom_amt2;
	private BigDecimal	r13_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg2;
	private BigDecimal	r13_tot_spe_ris_chrg;
	private BigDecimal	r13_net_pos_gen_mar_ris;
	private BigDecimal	r13_gen_mar_ris_chrg_8per;
	private BigDecimal	r13_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r13_tot_gen_mar_risk_chrg;
	private BigDecimal	r13_tot_mar_ris_chrg;
	
	private BigDecimal	r14_market;
	private BigDecimal	r14_gpfsr_nom_amt;
	private BigDecimal	r14_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg;
	private BigDecimal	r14_gpfsr_nom_amt1;
	private BigDecimal	r14_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg1;
	private BigDecimal	r14_gpfsr_nom_amt2;
	private BigDecimal	r14_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg2;
	private BigDecimal	r14_tot_spe_ris_chrg;
	private BigDecimal	r14_net_pos_gen_mar_ris;
	private BigDecimal	r14_gen_mar_ris_chrg_8per;
	private BigDecimal	r14_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r14_tot_gen_mar_risk_chrg;
	private BigDecimal	r14_tot_mar_ris_chrg;
	
	private BigDecimal	r15_market;
	private BigDecimal	r15_gpfsr_nom_amt;
	private BigDecimal	r15_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg;
	private BigDecimal	r15_gpfsr_nom_amt1;
	private BigDecimal	r15_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg1;
	private BigDecimal	r15_gpfsr_nom_amt2;
	private BigDecimal	r15_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg2;
	private BigDecimal	r15_tot_spe_ris_chrg;
	private BigDecimal	r15_net_pos_gen_mar_ris;
	private BigDecimal	r15_gen_mar_ris_chrg_8per;
	private BigDecimal	r15_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r15_tot_gen_mar_risk_chrg;
	private BigDecimal	r15_tot_mar_ris_chrg;
	
	private BigDecimal	r16_market;
	private BigDecimal	r16_gpfsr_nom_amt;
	private BigDecimal	r16_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg;
	private BigDecimal	r16_gpfsr_nom_amt1;
	private BigDecimal	r16_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg1;
	private BigDecimal	r16_gpfsr_nom_amt2;
	private BigDecimal	r16_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg2;
	private BigDecimal	r16_tot_spe_ris_chrg;
	private BigDecimal	r16_net_pos_gen_mar_ris;
	private BigDecimal	r16_gen_mar_ris_chrg_8per;
	private BigDecimal	r16_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r16_tot_gen_mar_risk_chrg;
	private BigDecimal	r16_tot_mar_ris_chrg;
	
	private BigDecimal	r17_market;
	private BigDecimal	r17_gpfsr_nom_amt;
	private BigDecimal	r17_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg;
	private BigDecimal	r17_gpfsr_nom_amt1;
	private BigDecimal	r17_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg1;
	private BigDecimal	r17_gpfsr_nom_amt2;
	private BigDecimal	r17_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg2;
	private BigDecimal	r17_tot_spe_ris_chrg;
	private BigDecimal	r17_net_pos_gen_mar_ris;
	private BigDecimal	r17_gen_mar_ris_chrg_8per;
	private BigDecimal	r17_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r17_tot_gen_mar_risk_chrg;
	private BigDecimal	r17_tot_mar_ris_chrg;
	
	private BigDecimal	r18_market;
	private BigDecimal	r18_gpfsr_nom_amt;
	private BigDecimal	r18_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg;
	private BigDecimal	r18_gpfsr_nom_amt1;
	private BigDecimal	r18_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg1;
	private BigDecimal	r18_gpfsr_nom_amt2;
	private BigDecimal	r18_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg2;
	private BigDecimal	r18_tot_spe_ris_chrg;
	private BigDecimal	r18_net_pos_gen_mar_ris;
	private BigDecimal	r18_gen_mar_ris_chrg_8per;
	private BigDecimal	r18_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r18_tot_gen_mar_risk_chrg;
	private BigDecimal	r18_tot_mar_ris_chrg;
	
	private BigDecimal	r19_market;
	private BigDecimal	r19_gpfsr_nom_amt;
	private BigDecimal	r19_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg;
	private BigDecimal	r19_gpfsr_nom_amt1;
	private BigDecimal	r19_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg1;
	private BigDecimal	r19_gpfsr_nom_amt2;
	private BigDecimal	r19_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg2;
	private BigDecimal	r19_tot_spe_ris_chrg;
	private BigDecimal	r19_net_pos_gen_mar_ris;
	private BigDecimal	r19_gen_mar_ris_chrg_8per;
	private BigDecimal	r19_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r19_tot_gen_mar_risk_chrg;
	private BigDecimal	r19_tot_mar_ris_chrg;
	
	private BigDecimal	r20_market;
	private BigDecimal	r20_gpfsr_nom_amt;
	private BigDecimal	r20_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg;
	private BigDecimal	r20_gpfsr_nom_amt1;
	private BigDecimal	r20_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg1;
	private BigDecimal	r20_gpfsr_nom_amt2;
	private BigDecimal	r20_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg2;
	private BigDecimal	r20_tot_spe_ris_chrg;
	private BigDecimal	r20_net_pos_gen_mar_ris;
	private BigDecimal	r20_gen_mar_ris_chrg_8per;
	private BigDecimal	r20_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r20_tot_gen_mar_risk_chrg;
	private BigDecimal	r20_tot_mar_ris_chrg;
	
	private BigDecimal	r21_market;
	private BigDecimal	r21_gpfsr_nom_amt;
	private BigDecimal	r21_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg;
	private BigDecimal	r21_gpfsr_nom_amt1;
	private BigDecimal	r21_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg1;
	private BigDecimal	r21_gpfsr_nom_amt2;
	private BigDecimal	r21_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg2;
	private BigDecimal	r21_tot_spe_ris_chrg;
	private BigDecimal	r21_net_pos_gen_mar_ris;
	private BigDecimal	r21_gen_mar_ris_chrg_8per;
	private BigDecimal	r21_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r21_tot_gen_mar_risk_chrg;
	private BigDecimal	r21_tot_mar_ris_chrg;
	
	private BigDecimal	r22_market;
	private BigDecimal	r22_gpfsr_nom_amt;
	private BigDecimal	r22_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg;
	private BigDecimal	r22_gpfsr_nom_amt1;
	private BigDecimal	r22_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg1;
	private BigDecimal	r22_gpfsr_nom_amt2;
	private BigDecimal	r22_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg2;
	private BigDecimal	r22_tot_spe_ris_chrg;
	private BigDecimal	r22_net_pos_gen_mar_ris;
	private BigDecimal	r22_gen_mar_ris_chrg_8per;
	private BigDecimal	r22_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r22_tot_gen_mar_risk_chrg;
	private BigDecimal	r22_tot_mar_ris_chrg;
	
	private String	r23_market;
	private BigDecimal	r23_gpfsr_nom_amt;
	private BigDecimal	r23_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg;
	private BigDecimal	r23_gpfsr_nom_amt1;
	private BigDecimal	r23_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg1;
	private BigDecimal	r23_gpfsr_nom_amt2;
	private BigDecimal	r23_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg2;
	private BigDecimal	r23_tot_spe_ris_chrg;
	private BigDecimal	r23_net_pos_gen_mar_ris;
	private BigDecimal	r23_gen_mar_ris_chrg_8per;
	private BigDecimal	r23_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r23_tot_gen_mar_risk_chrg;
	private BigDecimal	r23_tot_mar_ris_chrg;
	

	
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
	
	

	public BigDecimal getR11_market() {
		return r11_market;
	}
	public void setR11_market(BigDecimal r11_market) {
		this.r11_market = r11_market;
	}
	public BigDecimal getR11_gpfsr_nom_amt() {
		return r11_gpfsr_nom_amt;
	}
	public void setR11_gpfsr_nom_amt(BigDecimal r11_gpfsr_nom_amt) {
		this.r11_gpfsr_nom_amt = r11_gpfsr_nom_amt;
	}
	public BigDecimal getR11_gpfsr_pos_att8_per_spe_ris() {
		return r11_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att8_per_spe_ris(BigDecimal r11_gpfsr_pos_att8_per_spe_ris) {
		this.r11_gpfsr_pos_att8_per_spe_ris = r11_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg() {
		return r11_gpfsr_chrg;
	}
	public void setR11_gpfsr_chrg(BigDecimal r11_gpfsr_chrg) {
		this.r11_gpfsr_chrg = r11_gpfsr_chrg;
	}
	public BigDecimal getR11_gpfsr_nom_amt1() {
		return r11_gpfsr_nom_amt1;
	}
	public void setR11_gpfsr_nom_amt1(BigDecimal r11_gpfsr_nom_amt1) {
		this.r11_gpfsr_nom_amt1 = r11_gpfsr_nom_amt1;
	}
	public BigDecimal getR11_gpfsr_pos_att4_per_spe_ris() {
		return r11_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att4_per_spe_ris(BigDecimal r11_gpfsr_pos_att4_per_spe_ris) {
		this.r11_gpfsr_pos_att4_per_spe_ris = r11_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg1() {
		return r11_gpfsr_chrg1;
	}
	public void setR11_gpfsr_chrg1(BigDecimal r11_gpfsr_chrg1) {
		this.r11_gpfsr_chrg1 = r11_gpfsr_chrg1;
	}
	public BigDecimal getR11_gpfsr_nom_amt2() {
		return r11_gpfsr_nom_amt2;
	}
	public void setR11_gpfsr_nom_amt2(BigDecimal r11_gpfsr_nom_amt2) {
		this.r11_gpfsr_nom_amt2 = r11_gpfsr_nom_amt2;
	}
	public BigDecimal getR11_gpfsr_pos_att2_per_spe_ris() {
		return r11_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att2_per_spe_ris(BigDecimal r11_gpfsr_pos_att2_per_spe_ris) {
		this.r11_gpfsr_pos_att2_per_spe_ris = r11_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg2() {
		return r11_gpfsr_chrg2;
	}
	public void setR11_gpfsr_chrg2(BigDecimal r11_gpfsr_chrg2) {
		this.r11_gpfsr_chrg2 = r11_gpfsr_chrg2;
	}
	public BigDecimal getR11_tot_spe_ris_chrg() {
		return r11_tot_spe_ris_chrg;
	}
	public void setR11_tot_spe_ris_chrg(BigDecimal r11_tot_spe_ris_chrg) {
		this.r11_tot_spe_ris_chrg = r11_tot_spe_ris_chrg;
	}
	public BigDecimal getR11_net_pos_gen_mar_ris() {
		return r11_net_pos_gen_mar_ris;
	}
	public void setR11_net_pos_gen_mar_ris(BigDecimal r11_net_pos_gen_mar_ris) {
		this.r11_net_pos_gen_mar_ris = r11_net_pos_gen_mar_ris;
	}
	public BigDecimal getR11_gen_mar_ris_chrg_8per() {
		return r11_gen_mar_ris_chrg_8per;
	}
	public void setR11_gen_mar_ris_chrg_8per(BigDecimal r11_gen_mar_ris_chrg_8per) {
		this.r11_gen_mar_ris_chrg_8per = r11_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR11_2per_gen_mar_ris_chrg_div_port() {
		return r11_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR11_2per_gen_mar_ris_chrg_div_port(BigDecimal r11_2per_gen_mar_ris_chrg_div_port) {
		this.r11_2per_gen_mar_ris_chrg_div_port = r11_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR11_tot_gen_mar_risk_chrg() {
		return r11_tot_gen_mar_risk_chrg;
	}
	public void setR11_tot_gen_mar_risk_chrg(BigDecimal r11_tot_gen_mar_risk_chrg) {
		this.r11_tot_gen_mar_risk_chrg = r11_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR11_tot_mar_ris_chrg() {
		return r11_tot_mar_ris_chrg;
	}
	public void setR11_tot_mar_ris_chrg(BigDecimal r11_tot_mar_ris_chrg) {
		this.r11_tot_mar_ris_chrg = r11_tot_mar_ris_chrg;
	}
	public BigDecimal getR12_market() {
		return r12_market;
	}
	public void setR12_market(BigDecimal r12_market) {
		this.r12_market = r12_market;
	}
	public BigDecimal getR12_gpfsr_nom_amt() {
		return r12_gpfsr_nom_amt;
	}
	public void setR12_gpfsr_nom_amt(BigDecimal r12_gpfsr_nom_amt) {
		this.r12_gpfsr_nom_amt = r12_gpfsr_nom_amt;
	}
	public BigDecimal getR12_gpfsr_pos_att8_per_spe_ris() {
		return r12_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att8_per_spe_ris(BigDecimal r12_gpfsr_pos_att8_per_spe_ris) {
		this.r12_gpfsr_pos_att8_per_spe_ris = r12_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg() {
		return r12_gpfsr_chrg;
	}
	public void setR12_gpfsr_chrg(BigDecimal r12_gpfsr_chrg) {
		this.r12_gpfsr_chrg = r12_gpfsr_chrg;
	}
	public BigDecimal getR12_gpfsr_nom_amt1() {
		return r12_gpfsr_nom_amt1;
	}
	public void setR12_gpfsr_nom_amt1(BigDecimal r12_gpfsr_nom_amt1) {
		this.r12_gpfsr_nom_amt1 = r12_gpfsr_nom_amt1;
	}
	public BigDecimal getR12_gpfsr_pos_att4_per_spe_ris() {
		return r12_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att4_per_spe_ris(BigDecimal r12_gpfsr_pos_att4_per_spe_ris) {
		this.r12_gpfsr_pos_att4_per_spe_ris = r12_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg1() {
		return r12_gpfsr_chrg1;
	}
	public void setR12_gpfsr_chrg1(BigDecimal r12_gpfsr_chrg1) {
		this.r12_gpfsr_chrg1 = r12_gpfsr_chrg1;
	}
	public BigDecimal getR12_gpfsr_nom_amt2() {
		return r12_gpfsr_nom_amt2;
	}
	public void setR12_gpfsr_nom_amt2(BigDecimal r12_gpfsr_nom_amt2) {
		this.r12_gpfsr_nom_amt2 = r12_gpfsr_nom_amt2;
	}
	public BigDecimal getR12_gpfsr_pos_att2_per_spe_ris() {
		return r12_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att2_per_spe_ris(BigDecimal r12_gpfsr_pos_att2_per_spe_ris) {
		this.r12_gpfsr_pos_att2_per_spe_ris = r12_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg2() {
		return r12_gpfsr_chrg2;
	}
	public void setR12_gpfsr_chrg2(BigDecimal r12_gpfsr_chrg2) {
		this.r12_gpfsr_chrg2 = r12_gpfsr_chrg2;
	}
	public BigDecimal getR12_tot_spe_ris_chrg() {
		return r12_tot_spe_ris_chrg;
	}
	public void setR12_tot_spe_ris_chrg(BigDecimal r12_tot_spe_ris_chrg) {
		this.r12_tot_spe_ris_chrg = r12_tot_spe_ris_chrg;
	}
	public BigDecimal getR12_net_pos_gen_mar_ris() {
		return r12_net_pos_gen_mar_ris;
	}
	public void setR12_net_pos_gen_mar_ris(BigDecimal r12_net_pos_gen_mar_ris) {
		this.r12_net_pos_gen_mar_ris = r12_net_pos_gen_mar_ris;
	}
	public BigDecimal getR12_gen_mar_ris_chrg_8per() {
		return r12_gen_mar_ris_chrg_8per;
	}
	public void setR12_gen_mar_ris_chrg_8per(BigDecimal r12_gen_mar_ris_chrg_8per) {
		this.r12_gen_mar_ris_chrg_8per = r12_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR12_2per_gen_mar_ris_chrg_div_port() {
		return r12_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR12_2per_gen_mar_ris_chrg_div_port(BigDecimal r12_2per_gen_mar_ris_chrg_div_port) {
		this.r12_2per_gen_mar_ris_chrg_div_port = r12_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR12_tot_gen_mar_risk_chrg() {
		return r12_tot_gen_mar_risk_chrg;
	}
	public void setR12_tot_gen_mar_risk_chrg(BigDecimal r12_tot_gen_mar_risk_chrg) {
		this.r12_tot_gen_mar_risk_chrg = r12_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR12_tot_mar_ris_chrg() {
		return r12_tot_mar_ris_chrg;
	}
	public void setR12_tot_mar_ris_chrg(BigDecimal r12_tot_mar_ris_chrg) {
		this.r12_tot_mar_ris_chrg = r12_tot_mar_ris_chrg;
	}
	public BigDecimal getR13_market() {
		return r13_market;
	}
	public void setR13_market(BigDecimal r13_market) {
		this.r13_market = r13_market;
	}
	public BigDecimal getR13_gpfsr_nom_amt() {
		return r13_gpfsr_nom_amt;
	}
	public void setR13_gpfsr_nom_amt(BigDecimal r13_gpfsr_nom_amt) {
		this.r13_gpfsr_nom_amt = r13_gpfsr_nom_amt;
	}
	public BigDecimal getR13_gpfsr_pos_att8_per_spe_ris() {
		return r13_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att8_per_spe_ris(BigDecimal r13_gpfsr_pos_att8_per_spe_ris) {
		this.r13_gpfsr_pos_att8_per_spe_ris = r13_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg() {
		return r13_gpfsr_chrg;
	}
	public void setR13_gpfsr_chrg(BigDecimal r13_gpfsr_chrg) {
		this.r13_gpfsr_chrg = r13_gpfsr_chrg;
	}
	public BigDecimal getR13_gpfsr_nom_amt1() {
		return r13_gpfsr_nom_amt1;
	}
	public void setR13_gpfsr_nom_amt1(BigDecimal r13_gpfsr_nom_amt1) {
		this.r13_gpfsr_nom_amt1 = r13_gpfsr_nom_amt1;
	}
	public BigDecimal getR13_gpfsr_pos_att4_per_spe_ris() {
		return r13_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att4_per_spe_ris(BigDecimal r13_gpfsr_pos_att4_per_spe_ris) {
		this.r13_gpfsr_pos_att4_per_spe_ris = r13_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg1() {
		return r13_gpfsr_chrg1;
	}
	public void setR13_gpfsr_chrg1(BigDecimal r13_gpfsr_chrg1) {
		this.r13_gpfsr_chrg1 = r13_gpfsr_chrg1;
	}
	public BigDecimal getR13_gpfsr_nom_amt2() {
		return r13_gpfsr_nom_amt2;
	}
	public void setR13_gpfsr_nom_amt2(BigDecimal r13_gpfsr_nom_amt2) {
		this.r13_gpfsr_nom_amt2 = r13_gpfsr_nom_amt2;
	}
	public BigDecimal getR13_gpfsr_pos_att2_per_spe_ris() {
		return r13_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att2_per_spe_ris(BigDecimal r13_gpfsr_pos_att2_per_spe_ris) {
		this.r13_gpfsr_pos_att2_per_spe_ris = r13_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg2() {
		return r13_gpfsr_chrg2;
	}
	public void setR13_gpfsr_chrg2(BigDecimal r13_gpfsr_chrg2) {
		this.r13_gpfsr_chrg2 = r13_gpfsr_chrg2;
	}
	public BigDecimal getR13_tot_spe_ris_chrg() {
		return r13_tot_spe_ris_chrg;
	}
	public void setR13_tot_spe_ris_chrg(BigDecimal r13_tot_spe_ris_chrg) {
		this.r13_tot_spe_ris_chrg = r13_tot_spe_ris_chrg;
	}
	public BigDecimal getR13_net_pos_gen_mar_ris() {
		return r13_net_pos_gen_mar_ris;
	}
	public void setR13_net_pos_gen_mar_ris(BigDecimal r13_net_pos_gen_mar_ris) {
		this.r13_net_pos_gen_mar_ris = r13_net_pos_gen_mar_ris;
	}
	public BigDecimal getR13_gen_mar_ris_chrg_8per() {
		return r13_gen_mar_ris_chrg_8per;
	}
	public void setR13_gen_mar_ris_chrg_8per(BigDecimal r13_gen_mar_ris_chrg_8per) {
		this.r13_gen_mar_ris_chrg_8per = r13_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR13_2per_gen_mar_ris_chrg_div_port() {
		return r13_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR13_2per_gen_mar_ris_chrg_div_port(BigDecimal r13_2per_gen_mar_ris_chrg_div_port) {
		this.r13_2per_gen_mar_ris_chrg_div_port = r13_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR13_tot_gen_mar_risk_chrg() {
		return r13_tot_gen_mar_risk_chrg;
	}
	public void setR13_tot_gen_mar_risk_chrg(BigDecimal r13_tot_gen_mar_risk_chrg) {
		this.r13_tot_gen_mar_risk_chrg = r13_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR13_tot_mar_ris_chrg() {
		return r13_tot_mar_ris_chrg;
	}
	public void setR13_tot_mar_ris_chrg(BigDecimal r13_tot_mar_ris_chrg) {
		this.r13_tot_mar_ris_chrg = r13_tot_mar_ris_chrg;
	}
	public BigDecimal getR14_market() {
		return r14_market;
	}
	public void setR14_market(BigDecimal r14_market) {
		this.r14_market = r14_market;
	}
	public BigDecimal getR14_gpfsr_nom_amt() {
		return r14_gpfsr_nom_amt;
	}
	public void setR14_gpfsr_nom_amt(BigDecimal r14_gpfsr_nom_amt) {
		this.r14_gpfsr_nom_amt = r14_gpfsr_nom_amt;
	}
	public BigDecimal getR14_gpfsr_pos_att8_per_spe_ris() {
		return r14_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att8_per_spe_ris(BigDecimal r14_gpfsr_pos_att8_per_spe_ris) {
		this.r14_gpfsr_pos_att8_per_spe_ris = r14_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg() {
		return r14_gpfsr_chrg;
	}
	public void setR14_gpfsr_chrg(BigDecimal r14_gpfsr_chrg) {
		this.r14_gpfsr_chrg = r14_gpfsr_chrg;
	}
	public BigDecimal getR14_gpfsr_nom_amt1() {
		return r14_gpfsr_nom_amt1;
	}
	public void setR14_gpfsr_nom_amt1(BigDecimal r14_gpfsr_nom_amt1) {
		this.r14_gpfsr_nom_amt1 = r14_gpfsr_nom_amt1;
	}
	public BigDecimal getR14_gpfsr_pos_att4_per_spe_ris() {
		return r14_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att4_per_spe_ris(BigDecimal r14_gpfsr_pos_att4_per_spe_ris) {
		this.r14_gpfsr_pos_att4_per_spe_ris = r14_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg1() {
		return r14_gpfsr_chrg1;
	}
	public void setR14_gpfsr_chrg1(BigDecimal r14_gpfsr_chrg1) {
		this.r14_gpfsr_chrg1 = r14_gpfsr_chrg1;
	}
	public BigDecimal getR14_gpfsr_nom_amt2() {
		return r14_gpfsr_nom_amt2;
	}
	public void setR14_gpfsr_nom_amt2(BigDecimal r14_gpfsr_nom_amt2) {
		this.r14_gpfsr_nom_amt2 = r14_gpfsr_nom_amt2;
	}
	public BigDecimal getR14_gpfsr_pos_att2_per_spe_ris() {
		return r14_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att2_per_spe_ris(BigDecimal r14_gpfsr_pos_att2_per_spe_ris) {
		this.r14_gpfsr_pos_att2_per_spe_ris = r14_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg2() {
		return r14_gpfsr_chrg2;
	}
	public void setR14_gpfsr_chrg2(BigDecimal r14_gpfsr_chrg2) {
		this.r14_gpfsr_chrg2 = r14_gpfsr_chrg2;
	}
	public BigDecimal getR14_tot_spe_ris_chrg() {
		return r14_tot_spe_ris_chrg;
	}
	public void setR14_tot_spe_ris_chrg(BigDecimal r14_tot_spe_ris_chrg) {
		this.r14_tot_spe_ris_chrg = r14_tot_spe_ris_chrg;
	}
	public BigDecimal getR14_net_pos_gen_mar_ris() {
		return r14_net_pos_gen_mar_ris;
	}
	public void setR14_net_pos_gen_mar_ris(BigDecimal r14_net_pos_gen_mar_ris) {
		this.r14_net_pos_gen_mar_ris = r14_net_pos_gen_mar_ris;
	}
	public BigDecimal getR14_gen_mar_ris_chrg_8per() {
		return r14_gen_mar_ris_chrg_8per;
	}
	public void setR14_gen_mar_ris_chrg_8per(BigDecimal r14_gen_mar_ris_chrg_8per) {
		this.r14_gen_mar_ris_chrg_8per = r14_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR14_2per_gen_mar_ris_chrg_div_port() {
		return r14_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR14_2per_gen_mar_ris_chrg_div_port(BigDecimal r14_2per_gen_mar_ris_chrg_div_port) {
		this.r14_2per_gen_mar_ris_chrg_div_port = r14_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR14_tot_gen_mar_risk_chrg() {
		return r14_tot_gen_mar_risk_chrg;
	}
	public void setR14_tot_gen_mar_risk_chrg(BigDecimal r14_tot_gen_mar_risk_chrg) {
		this.r14_tot_gen_mar_risk_chrg = r14_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR14_tot_mar_ris_chrg() {
		return r14_tot_mar_ris_chrg;
	}
	public void setR14_tot_mar_ris_chrg(BigDecimal r14_tot_mar_ris_chrg) {
		this.r14_tot_mar_ris_chrg = r14_tot_mar_ris_chrg;
	}
	public BigDecimal getR15_market() {
		return r15_market;
	}
	public void setR15_market(BigDecimal r15_market) {
		this.r15_market = r15_market;
	}
	public BigDecimal getR15_gpfsr_nom_amt() {
		return r15_gpfsr_nom_amt;
	}
	public void setR15_gpfsr_nom_amt(BigDecimal r15_gpfsr_nom_amt) {
		this.r15_gpfsr_nom_amt = r15_gpfsr_nom_amt;
	}
	public BigDecimal getR15_gpfsr_pos_att8_per_spe_ris() {
		return r15_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att8_per_spe_ris(BigDecimal r15_gpfsr_pos_att8_per_spe_ris) {
		this.r15_gpfsr_pos_att8_per_spe_ris = r15_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg() {
		return r15_gpfsr_chrg;
	}
	public void setR15_gpfsr_chrg(BigDecimal r15_gpfsr_chrg) {
		this.r15_gpfsr_chrg = r15_gpfsr_chrg;
	}
	public BigDecimal getR15_gpfsr_nom_amt1() {
		return r15_gpfsr_nom_amt1;
	}
	public void setR15_gpfsr_nom_amt1(BigDecimal r15_gpfsr_nom_amt1) {
		this.r15_gpfsr_nom_amt1 = r15_gpfsr_nom_amt1;
	}
	public BigDecimal getR15_gpfsr_pos_att4_per_spe_ris() {
		return r15_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att4_per_spe_ris(BigDecimal r15_gpfsr_pos_att4_per_spe_ris) {
		this.r15_gpfsr_pos_att4_per_spe_ris = r15_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg1() {
		return r15_gpfsr_chrg1;
	}
	public void setR15_gpfsr_chrg1(BigDecimal r15_gpfsr_chrg1) {
		this.r15_gpfsr_chrg1 = r15_gpfsr_chrg1;
	}
	public BigDecimal getR15_gpfsr_nom_amt2() {
		return r15_gpfsr_nom_amt2;
	}
	public void setR15_gpfsr_nom_amt2(BigDecimal r15_gpfsr_nom_amt2) {
		this.r15_gpfsr_nom_amt2 = r15_gpfsr_nom_amt2;
	}
	public BigDecimal getR15_gpfsr_pos_att2_per_spe_ris() {
		return r15_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att2_per_spe_ris(BigDecimal r15_gpfsr_pos_att2_per_spe_ris) {
		this.r15_gpfsr_pos_att2_per_spe_ris = r15_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg2() {
		return r15_gpfsr_chrg2;
	}
	public void setR15_gpfsr_chrg2(BigDecimal r15_gpfsr_chrg2) {
		this.r15_gpfsr_chrg2 = r15_gpfsr_chrg2;
	}
	public BigDecimal getR15_tot_spe_ris_chrg() {
		return r15_tot_spe_ris_chrg;
	}
	public void setR15_tot_spe_ris_chrg(BigDecimal r15_tot_spe_ris_chrg) {
		this.r15_tot_spe_ris_chrg = r15_tot_spe_ris_chrg;
	}
	public BigDecimal getR15_net_pos_gen_mar_ris() {
		return r15_net_pos_gen_mar_ris;
	}
	public void setR15_net_pos_gen_mar_ris(BigDecimal r15_net_pos_gen_mar_ris) {
		this.r15_net_pos_gen_mar_ris = r15_net_pos_gen_mar_ris;
	}
	public BigDecimal getR15_gen_mar_ris_chrg_8per() {
		return r15_gen_mar_ris_chrg_8per;
	}
	public void setR15_gen_mar_ris_chrg_8per(BigDecimal r15_gen_mar_ris_chrg_8per) {
		this.r15_gen_mar_ris_chrg_8per = r15_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR15_2per_gen_mar_ris_chrg_div_port() {
		return r15_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR15_2per_gen_mar_ris_chrg_div_port(BigDecimal r15_2per_gen_mar_ris_chrg_div_port) {
		this.r15_2per_gen_mar_ris_chrg_div_port = r15_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR15_tot_gen_mar_risk_chrg() {
		return r15_tot_gen_mar_risk_chrg;
	}
	public void setR15_tot_gen_mar_risk_chrg(BigDecimal r15_tot_gen_mar_risk_chrg) {
		this.r15_tot_gen_mar_risk_chrg = r15_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR15_tot_mar_ris_chrg() {
		return r15_tot_mar_ris_chrg;
	}
	public void setR15_tot_mar_ris_chrg(BigDecimal r15_tot_mar_ris_chrg) {
		this.r15_tot_mar_ris_chrg = r15_tot_mar_ris_chrg;
	}
	public BigDecimal getR16_market() {
		return r16_market;
	}
	public void setR16_market(BigDecimal r16_market) {
		this.r16_market = r16_market;
	}
	public BigDecimal getR16_gpfsr_nom_amt() {
		return r16_gpfsr_nom_amt;
	}
	public void setR16_gpfsr_nom_amt(BigDecimal r16_gpfsr_nom_amt) {
		this.r16_gpfsr_nom_amt = r16_gpfsr_nom_amt;
	}
	public BigDecimal getR16_gpfsr_pos_att8_per_spe_ris() {
		return r16_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att8_per_spe_ris(BigDecimal r16_gpfsr_pos_att8_per_spe_ris) {
		this.r16_gpfsr_pos_att8_per_spe_ris = r16_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg() {
		return r16_gpfsr_chrg;
	}
	public void setR16_gpfsr_chrg(BigDecimal r16_gpfsr_chrg) {
		this.r16_gpfsr_chrg = r16_gpfsr_chrg;
	}
	public BigDecimal getR16_gpfsr_nom_amt1() {
		return r16_gpfsr_nom_amt1;
	}
	public void setR16_gpfsr_nom_amt1(BigDecimal r16_gpfsr_nom_amt1) {
		this.r16_gpfsr_nom_amt1 = r16_gpfsr_nom_amt1;
	}
	public BigDecimal getR16_gpfsr_pos_att4_per_spe_ris() {
		return r16_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att4_per_spe_ris(BigDecimal r16_gpfsr_pos_att4_per_spe_ris) {
		this.r16_gpfsr_pos_att4_per_spe_ris = r16_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg1() {
		return r16_gpfsr_chrg1;
	}
	public void setR16_gpfsr_chrg1(BigDecimal r16_gpfsr_chrg1) {
		this.r16_gpfsr_chrg1 = r16_gpfsr_chrg1;
	}
	public BigDecimal getR16_gpfsr_nom_amt2() {
		return r16_gpfsr_nom_amt2;
	}
	public void setR16_gpfsr_nom_amt2(BigDecimal r16_gpfsr_nom_amt2) {
		this.r16_gpfsr_nom_amt2 = r16_gpfsr_nom_amt2;
	}
	public BigDecimal getR16_gpfsr_pos_att2_per_spe_ris() {
		return r16_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att2_per_spe_ris(BigDecimal r16_gpfsr_pos_att2_per_spe_ris) {
		this.r16_gpfsr_pos_att2_per_spe_ris = r16_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg2() {
		return r16_gpfsr_chrg2;
	}
	public void setR16_gpfsr_chrg2(BigDecimal r16_gpfsr_chrg2) {
		this.r16_gpfsr_chrg2 = r16_gpfsr_chrg2;
	}
	public BigDecimal getR16_tot_spe_ris_chrg() {
		return r16_tot_spe_ris_chrg;
	}
	public void setR16_tot_spe_ris_chrg(BigDecimal r16_tot_spe_ris_chrg) {
		this.r16_tot_spe_ris_chrg = r16_tot_spe_ris_chrg;
	}
	public BigDecimal getR16_net_pos_gen_mar_ris() {
		return r16_net_pos_gen_mar_ris;
	}
	public void setR16_net_pos_gen_mar_ris(BigDecimal r16_net_pos_gen_mar_ris) {
		this.r16_net_pos_gen_mar_ris = r16_net_pos_gen_mar_ris;
	}
	public BigDecimal getR16_gen_mar_ris_chrg_8per() {
		return r16_gen_mar_ris_chrg_8per;
	}
	public void setR16_gen_mar_ris_chrg_8per(BigDecimal r16_gen_mar_ris_chrg_8per) {
		this.r16_gen_mar_ris_chrg_8per = r16_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR16_2per_gen_mar_ris_chrg_div_port() {
		return r16_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR16_2per_gen_mar_ris_chrg_div_port(BigDecimal r16_2per_gen_mar_ris_chrg_div_port) {
		this.r16_2per_gen_mar_ris_chrg_div_port = r16_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR16_tot_gen_mar_risk_chrg() {
		return r16_tot_gen_mar_risk_chrg;
	}
	public void setR16_tot_gen_mar_risk_chrg(BigDecimal r16_tot_gen_mar_risk_chrg) {
		this.r16_tot_gen_mar_risk_chrg = r16_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR16_tot_mar_ris_chrg() {
		return r16_tot_mar_ris_chrg;
	}
	public void setR16_tot_mar_ris_chrg(BigDecimal r16_tot_mar_ris_chrg) {
		this.r16_tot_mar_ris_chrg = r16_tot_mar_ris_chrg;
	}
	public BigDecimal getR17_market() {
		return r17_market;
	}
	public void setR17_market(BigDecimal r17_market) {
		this.r17_market = r17_market;
	}
	public BigDecimal getR17_gpfsr_nom_amt() {
		return r17_gpfsr_nom_amt;
	}
	public void setR17_gpfsr_nom_amt(BigDecimal r17_gpfsr_nom_amt) {
		this.r17_gpfsr_nom_amt = r17_gpfsr_nom_amt;
	}
	public BigDecimal getR17_gpfsr_pos_att8_per_spe_ris() {
		return r17_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att8_per_spe_ris(BigDecimal r17_gpfsr_pos_att8_per_spe_ris) {
		this.r17_gpfsr_pos_att8_per_spe_ris = r17_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg() {
		return r17_gpfsr_chrg;
	}
	public void setR17_gpfsr_chrg(BigDecimal r17_gpfsr_chrg) {
		this.r17_gpfsr_chrg = r17_gpfsr_chrg;
	}
	public BigDecimal getR17_gpfsr_nom_amt1() {
		return r17_gpfsr_nom_amt1;
	}
	public void setR17_gpfsr_nom_amt1(BigDecimal r17_gpfsr_nom_amt1) {
		this.r17_gpfsr_nom_amt1 = r17_gpfsr_nom_amt1;
	}
	public BigDecimal getR17_gpfsr_pos_att4_per_spe_ris() {
		return r17_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att4_per_spe_ris(BigDecimal r17_gpfsr_pos_att4_per_spe_ris) {
		this.r17_gpfsr_pos_att4_per_spe_ris = r17_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg1() {
		return r17_gpfsr_chrg1;
	}
	public void setR17_gpfsr_chrg1(BigDecimal r17_gpfsr_chrg1) {
		this.r17_gpfsr_chrg1 = r17_gpfsr_chrg1;
	}
	public BigDecimal getR17_gpfsr_nom_amt2() {
		return r17_gpfsr_nom_amt2;
	}
	public void setR17_gpfsr_nom_amt2(BigDecimal r17_gpfsr_nom_amt2) {
		this.r17_gpfsr_nom_amt2 = r17_gpfsr_nom_amt2;
	}
	public BigDecimal getR17_gpfsr_pos_att2_per_spe_ris() {
		return r17_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att2_per_spe_ris(BigDecimal r17_gpfsr_pos_att2_per_spe_ris) {
		this.r17_gpfsr_pos_att2_per_spe_ris = r17_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg2() {
		return r17_gpfsr_chrg2;
	}
	public void setR17_gpfsr_chrg2(BigDecimal r17_gpfsr_chrg2) {
		this.r17_gpfsr_chrg2 = r17_gpfsr_chrg2;
	}
	public BigDecimal getR17_tot_spe_ris_chrg() {
		return r17_tot_spe_ris_chrg;
	}
	public void setR17_tot_spe_ris_chrg(BigDecimal r17_tot_spe_ris_chrg) {
		this.r17_tot_spe_ris_chrg = r17_tot_spe_ris_chrg;
	}
	public BigDecimal getR17_net_pos_gen_mar_ris() {
		return r17_net_pos_gen_mar_ris;
	}
	public void setR17_net_pos_gen_mar_ris(BigDecimal r17_net_pos_gen_mar_ris) {
		this.r17_net_pos_gen_mar_ris = r17_net_pos_gen_mar_ris;
	}
	public BigDecimal getR17_gen_mar_ris_chrg_8per() {
		return r17_gen_mar_ris_chrg_8per;
	}
	public void setR17_gen_mar_ris_chrg_8per(BigDecimal r17_gen_mar_ris_chrg_8per) {
		this.r17_gen_mar_ris_chrg_8per = r17_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR17_2per_gen_mar_ris_chrg_div_port() {
		return r17_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR17_2per_gen_mar_ris_chrg_div_port(BigDecimal r17_2per_gen_mar_ris_chrg_div_port) {
		this.r17_2per_gen_mar_ris_chrg_div_port = r17_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR17_tot_gen_mar_risk_chrg() {
		return r17_tot_gen_mar_risk_chrg;
	}
	public void setR17_tot_gen_mar_risk_chrg(BigDecimal r17_tot_gen_mar_risk_chrg) {
		this.r17_tot_gen_mar_risk_chrg = r17_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR17_tot_mar_ris_chrg() {
		return r17_tot_mar_ris_chrg;
	}
	public void setR17_tot_mar_ris_chrg(BigDecimal r17_tot_mar_ris_chrg) {
		this.r17_tot_mar_ris_chrg = r17_tot_mar_ris_chrg;
	}
	public BigDecimal getR18_market() {
		return r18_market;
	}
	public void setR18_market(BigDecimal r18_market) {
		this.r18_market = r18_market;
	}
	public BigDecimal getR18_gpfsr_nom_amt() {
		return r18_gpfsr_nom_amt;
	}
	public void setR18_gpfsr_nom_amt(BigDecimal r18_gpfsr_nom_amt) {
		this.r18_gpfsr_nom_amt = r18_gpfsr_nom_amt;
	}
	public BigDecimal getR18_gpfsr_pos_att8_per_spe_ris() {
		return r18_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att8_per_spe_ris(BigDecimal r18_gpfsr_pos_att8_per_spe_ris) {
		this.r18_gpfsr_pos_att8_per_spe_ris = r18_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg() {
		return r18_gpfsr_chrg;
	}
	public void setR18_gpfsr_chrg(BigDecimal r18_gpfsr_chrg) {
		this.r18_gpfsr_chrg = r18_gpfsr_chrg;
	}
	public BigDecimal getR18_gpfsr_nom_amt1() {
		return r18_gpfsr_nom_amt1;
	}
	public void setR18_gpfsr_nom_amt1(BigDecimal r18_gpfsr_nom_amt1) {
		this.r18_gpfsr_nom_amt1 = r18_gpfsr_nom_amt1;
	}
	public BigDecimal getR18_gpfsr_pos_att4_per_spe_ris() {
		return r18_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att4_per_spe_ris(BigDecimal r18_gpfsr_pos_att4_per_spe_ris) {
		this.r18_gpfsr_pos_att4_per_spe_ris = r18_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg1() {
		return r18_gpfsr_chrg1;
	}
	public void setR18_gpfsr_chrg1(BigDecimal r18_gpfsr_chrg1) {
		this.r18_gpfsr_chrg1 = r18_gpfsr_chrg1;
	}
	public BigDecimal getR18_gpfsr_nom_amt2() {
		return r18_gpfsr_nom_amt2;
	}
	public void setR18_gpfsr_nom_amt2(BigDecimal r18_gpfsr_nom_amt2) {
		this.r18_gpfsr_nom_amt2 = r18_gpfsr_nom_amt2;
	}
	public BigDecimal getR18_gpfsr_pos_att2_per_spe_ris() {
		return r18_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att2_per_spe_ris(BigDecimal r18_gpfsr_pos_att2_per_spe_ris) {
		this.r18_gpfsr_pos_att2_per_spe_ris = r18_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg2() {
		return r18_gpfsr_chrg2;
	}
	public void setR18_gpfsr_chrg2(BigDecimal r18_gpfsr_chrg2) {
		this.r18_gpfsr_chrg2 = r18_gpfsr_chrg2;
	}
	public BigDecimal getR18_tot_spe_ris_chrg() {
		return r18_tot_spe_ris_chrg;
	}
	public void setR18_tot_spe_ris_chrg(BigDecimal r18_tot_spe_ris_chrg) {
		this.r18_tot_spe_ris_chrg = r18_tot_spe_ris_chrg;
	}
	public BigDecimal getR18_net_pos_gen_mar_ris() {
		return r18_net_pos_gen_mar_ris;
	}
	public void setR18_net_pos_gen_mar_ris(BigDecimal r18_net_pos_gen_mar_ris) {
		this.r18_net_pos_gen_mar_ris = r18_net_pos_gen_mar_ris;
	}
	public BigDecimal getR18_gen_mar_ris_chrg_8per() {
		return r18_gen_mar_ris_chrg_8per;
	}
	public void setR18_gen_mar_ris_chrg_8per(BigDecimal r18_gen_mar_ris_chrg_8per) {
		this.r18_gen_mar_ris_chrg_8per = r18_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR18_2per_gen_mar_ris_chrg_div_port() {
		return r18_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR18_2per_gen_mar_ris_chrg_div_port(BigDecimal r18_2per_gen_mar_ris_chrg_div_port) {
		this.r18_2per_gen_mar_ris_chrg_div_port = r18_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR18_tot_gen_mar_risk_chrg() {
		return r18_tot_gen_mar_risk_chrg;
	}
	public void setR18_tot_gen_mar_risk_chrg(BigDecimal r18_tot_gen_mar_risk_chrg) {
		this.r18_tot_gen_mar_risk_chrg = r18_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR18_tot_mar_ris_chrg() {
		return r18_tot_mar_ris_chrg;
	}
	public void setR18_tot_mar_ris_chrg(BigDecimal r18_tot_mar_ris_chrg) {
		this.r18_tot_mar_ris_chrg = r18_tot_mar_ris_chrg;
	}
	public BigDecimal getR19_market() {
		return r19_market;
	}
	public void setR19_market(BigDecimal r19_market) {
		this.r19_market = r19_market;
	}
	public BigDecimal getR19_gpfsr_nom_amt() {
		return r19_gpfsr_nom_amt;
	}
	public void setR19_gpfsr_nom_amt(BigDecimal r19_gpfsr_nom_amt) {
		this.r19_gpfsr_nom_amt = r19_gpfsr_nom_amt;
	}
	public BigDecimal getR19_gpfsr_pos_att8_per_spe_ris() {
		return r19_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att8_per_spe_ris(BigDecimal r19_gpfsr_pos_att8_per_spe_ris) {
		this.r19_gpfsr_pos_att8_per_spe_ris = r19_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg() {
		return r19_gpfsr_chrg;
	}
	public void setR19_gpfsr_chrg(BigDecimal r19_gpfsr_chrg) {
		this.r19_gpfsr_chrg = r19_gpfsr_chrg;
	}
	public BigDecimal getR19_gpfsr_nom_amt1() {
		return r19_gpfsr_nom_amt1;
	}
	public void setR19_gpfsr_nom_amt1(BigDecimal r19_gpfsr_nom_amt1) {
		this.r19_gpfsr_nom_amt1 = r19_gpfsr_nom_amt1;
	}
	public BigDecimal getR19_gpfsr_pos_att4_per_spe_ris() {
		return r19_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att4_per_spe_ris(BigDecimal r19_gpfsr_pos_att4_per_spe_ris) {
		this.r19_gpfsr_pos_att4_per_spe_ris = r19_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg1() {
		return r19_gpfsr_chrg1;
	}
	public void setR19_gpfsr_chrg1(BigDecimal r19_gpfsr_chrg1) {
		this.r19_gpfsr_chrg1 = r19_gpfsr_chrg1;
	}
	public BigDecimal getR19_gpfsr_nom_amt2() {
		return r19_gpfsr_nom_amt2;
	}
	public void setR19_gpfsr_nom_amt2(BigDecimal r19_gpfsr_nom_amt2) {
		this.r19_gpfsr_nom_amt2 = r19_gpfsr_nom_amt2;
	}
	public BigDecimal getR19_gpfsr_pos_att2_per_spe_ris() {
		return r19_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att2_per_spe_ris(BigDecimal r19_gpfsr_pos_att2_per_spe_ris) {
		this.r19_gpfsr_pos_att2_per_spe_ris = r19_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg2() {
		return r19_gpfsr_chrg2;
	}
	public void setR19_gpfsr_chrg2(BigDecimal r19_gpfsr_chrg2) {
		this.r19_gpfsr_chrg2 = r19_gpfsr_chrg2;
	}
	public BigDecimal getR19_tot_spe_ris_chrg() {
		return r19_tot_spe_ris_chrg;
	}
	public void setR19_tot_spe_ris_chrg(BigDecimal r19_tot_spe_ris_chrg) {
		this.r19_tot_spe_ris_chrg = r19_tot_spe_ris_chrg;
	}
	public BigDecimal getR19_net_pos_gen_mar_ris() {
		return r19_net_pos_gen_mar_ris;
	}
	public void setR19_net_pos_gen_mar_ris(BigDecimal r19_net_pos_gen_mar_ris) {
		this.r19_net_pos_gen_mar_ris = r19_net_pos_gen_mar_ris;
	}
	public BigDecimal getR19_gen_mar_ris_chrg_8per() {
		return r19_gen_mar_ris_chrg_8per;
	}
	public void setR19_gen_mar_ris_chrg_8per(BigDecimal r19_gen_mar_ris_chrg_8per) {
		this.r19_gen_mar_ris_chrg_8per = r19_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR19_2per_gen_mar_ris_chrg_div_port() {
		return r19_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR19_2per_gen_mar_ris_chrg_div_port(BigDecimal r19_2per_gen_mar_ris_chrg_div_port) {
		this.r19_2per_gen_mar_ris_chrg_div_port = r19_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR19_tot_gen_mar_risk_chrg() {
		return r19_tot_gen_mar_risk_chrg;
	}
	public void setR19_tot_gen_mar_risk_chrg(BigDecimal r19_tot_gen_mar_risk_chrg) {
		this.r19_tot_gen_mar_risk_chrg = r19_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR19_tot_mar_ris_chrg() {
		return r19_tot_mar_ris_chrg;
	}
	public void setR19_tot_mar_ris_chrg(BigDecimal r19_tot_mar_ris_chrg) {
		this.r19_tot_mar_ris_chrg = r19_tot_mar_ris_chrg;
	}
	public BigDecimal getR20_market() {
		return r20_market;
	}
	public void setR20_market(BigDecimal r20_market) {
		this.r20_market = r20_market;
	}
	public BigDecimal getR20_gpfsr_nom_amt() {
		return r20_gpfsr_nom_amt;
	}
	public void setR20_gpfsr_nom_amt(BigDecimal r20_gpfsr_nom_amt) {
		this.r20_gpfsr_nom_amt = r20_gpfsr_nom_amt;
	}
	public BigDecimal getR20_gpfsr_pos_att8_per_spe_ris() {
		return r20_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att8_per_spe_ris(BigDecimal r20_gpfsr_pos_att8_per_spe_ris) {
		this.r20_gpfsr_pos_att8_per_spe_ris = r20_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg() {
		return r20_gpfsr_chrg;
	}
	public void setR20_gpfsr_chrg(BigDecimal r20_gpfsr_chrg) {
		this.r20_gpfsr_chrg = r20_gpfsr_chrg;
	}
	public BigDecimal getR20_gpfsr_nom_amt1() {
		return r20_gpfsr_nom_amt1;
	}
	public void setR20_gpfsr_nom_amt1(BigDecimal r20_gpfsr_nom_amt1) {
		this.r20_gpfsr_nom_amt1 = r20_gpfsr_nom_amt1;
	}
	public BigDecimal getR20_gpfsr_pos_att4_per_spe_ris() {
		return r20_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att4_per_spe_ris(BigDecimal r20_gpfsr_pos_att4_per_spe_ris) {
		this.r20_gpfsr_pos_att4_per_spe_ris = r20_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg1() {
		return r20_gpfsr_chrg1;
	}
	public void setR20_gpfsr_chrg1(BigDecimal r20_gpfsr_chrg1) {
		this.r20_gpfsr_chrg1 = r20_gpfsr_chrg1;
	}
	public BigDecimal getR20_gpfsr_nom_amt2() {
		return r20_gpfsr_nom_amt2;
	}
	public void setR20_gpfsr_nom_amt2(BigDecimal r20_gpfsr_nom_amt2) {
		this.r20_gpfsr_nom_amt2 = r20_gpfsr_nom_amt2;
	}
	public BigDecimal getR20_gpfsr_pos_att2_per_spe_ris() {
		return r20_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att2_per_spe_ris(BigDecimal r20_gpfsr_pos_att2_per_spe_ris) {
		this.r20_gpfsr_pos_att2_per_spe_ris = r20_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg2() {
		return r20_gpfsr_chrg2;
	}
	public void setR20_gpfsr_chrg2(BigDecimal r20_gpfsr_chrg2) {
		this.r20_gpfsr_chrg2 = r20_gpfsr_chrg2;
	}
	public BigDecimal getR20_tot_spe_ris_chrg() {
		return r20_tot_spe_ris_chrg;
	}
	public void setR20_tot_spe_ris_chrg(BigDecimal r20_tot_spe_ris_chrg) {
		this.r20_tot_spe_ris_chrg = r20_tot_spe_ris_chrg;
	}
	public BigDecimal getR20_net_pos_gen_mar_ris() {
		return r20_net_pos_gen_mar_ris;
	}
	public void setR20_net_pos_gen_mar_ris(BigDecimal r20_net_pos_gen_mar_ris) {
		this.r20_net_pos_gen_mar_ris = r20_net_pos_gen_mar_ris;
	}
	public BigDecimal getR20_gen_mar_ris_chrg_8per() {
		return r20_gen_mar_ris_chrg_8per;
	}
	public void setR20_gen_mar_ris_chrg_8per(BigDecimal r20_gen_mar_ris_chrg_8per) {
		this.r20_gen_mar_ris_chrg_8per = r20_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR20_2per_gen_mar_ris_chrg_div_port() {
		return r20_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR20_2per_gen_mar_ris_chrg_div_port(BigDecimal r20_2per_gen_mar_ris_chrg_div_port) {
		this.r20_2per_gen_mar_ris_chrg_div_port = r20_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR20_tot_gen_mar_risk_chrg() {
		return r20_tot_gen_mar_risk_chrg;
	}
	public void setR20_tot_gen_mar_risk_chrg(BigDecimal r20_tot_gen_mar_risk_chrg) {
		this.r20_tot_gen_mar_risk_chrg = r20_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR20_tot_mar_ris_chrg() {
		return r20_tot_mar_ris_chrg;
	}
	public void setR20_tot_mar_ris_chrg(BigDecimal r20_tot_mar_ris_chrg) {
		this.r20_tot_mar_ris_chrg = r20_tot_mar_ris_chrg;
	}
	public BigDecimal getR21_market() {
		return r21_market;
	}
	public void setR21_market(BigDecimal r21_market) {
		this.r21_market = r21_market;
	}
	public BigDecimal getR21_gpfsr_nom_amt() {
		return r21_gpfsr_nom_amt;
	}
	public void setR21_gpfsr_nom_amt(BigDecimal r21_gpfsr_nom_amt) {
		this.r21_gpfsr_nom_amt = r21_gpfsr_nom_amt;
	}
	public BigDecimal getR21_gpfsr_pos_att8_per_spe_ris() {
		return r21_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att8_per_spe_ris(BigDecimal r21_gpfsr_pos_att8_per_spe_ris) {
		this.r21_gpfsr_pos_att8_per_spe_ris = r21_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg() {
		return r21_gpfsr_chrg;
	}
	public void setR21_gpfsr_chrg(BigDecimal r21_gpfsr_chrg) {
		this.r21_gpfsr_chrg = r21_gpfsr_chrg;
	}
	public BigDecimal getR21_gpfsr_nom_amt1() {
		return r21_gpfsr_nom_amt1;
	}
	public void setR21_gpfsr_nom_amt1(BigDecimal r21_gpfsr_nom_amt1) {
		this.r21_gpfsr_nom_amt1 = r21_gpfsr_nom_amt1;
	}
	public BigDecimal getR21_gpfsr_pos_att4_per_spe_ris() {
		return r21_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att4_per_spe_ris(BigDecimal r21_gpfsr_pos_att4_per_spe_ris) {
		this.r21_gpfsr_pos_att4_per_spe_ris = r21_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg1() {
		return r21_gpfsr_chrg1;
	}
	public void setR21_gpfsr_chrg1(BigDecimal r21_gpfsr_chrg1) {
		this.r21_gpfsr_chrg1 = r21_gpfsr_chrg1;
	}
	public BigDecimal getR21_gpfsr_nom_amt2() {
		return r21_gpfsr_nom_amt2;
	}
	public void setR21_gpfsr_nom_amt2(BigDecimal r21_gpfsr_nom_amt2) {
		this.r21_gpfsr_nom_amt2 = r21_gpfsr_nom_amt2;
	}
	public BigDecimal getR21_gpfsr_pos_att2_per_spe_ris() {
		return r21_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att2_per_spe_ris(BigDecimal r21_gpfsr_pos_att2_per_spe_ris) {
		this.r21_gpfsr_pos_att2_per_spe_ris = r21_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg2() {
		return r21_gpfsr_chrg2;
	}
	public void setR21_gpfsr_chrg2(BigDecimal r21_gpfsr_chrg2) {
		this.r21_gpfsr_chrg2 = r21_gpfsr_chrg2;
	}
	public BigDecimal getR21_tot_spe_ris_chrg() {
		return r21_tot_spe_ris_chrg;
	}
	public void setR21_tot_spe_ris_chrg(BigDecimal r21_tot_spe_ris_chrg) {
		this.r21_tot_spe_ris_chrg = r21_tot_spe_ris_chrg;
	}
	public BigDecimal getR21_net_pos_gen_mar_ris() {
		return r21_net_pos_gen_mar_ris;
	}
	public void setR21_net_pos_gen_mar_ris(BigDecimal r21_net_pos_gen_mar_ris) {
		this.r21_net_pos_gen_mar_ris = r21_net_pos_gen_mar_ris;
	}
	public BigDecimal getR21_gen_mar_ris_chrg_8per() {
		return r21_gen_mar_ris_chrg_8per;
	}
	public void setR21_gen_mar_ris_chrg_8per(BigDecimal r21_gen_mar_ris_chrg_8per) {
		this.r21_gen_mar_ris_chrg_8per = r21_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR21_2per_gen_mar_ris_chrg_div_port() {
		return r21_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR21_2per_gen_mar_ris_chrg_div_port(BigDecimal r21_2per_gen_mar_ris_chrg_div_port) {
		this.r21_2per_gen_mar_ris_chrg_div_port = r21_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR21_tot_gen_mar_risk_chrg() {
		return r21_tot_gen_mar_risk_chrg;
	}
	public void setR21_tot_gen_mar_risk_chrg(BigDecimal r21_tot_gen_mar_risk_chrg) {
		this.r21_tot_gen_mar_risk_chrg = r21_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR21_tot_mar_ris_chrg() {
		return r21_tot_mar_ris_chrg;
	}
	public void setR21_tot_mar_ris_chrg(BigDecimal r21_tot_mar_ris_chrg) {
		this.r21_tot_mar_ris_chrg = r21_tot_mar_ris_chrg;
	}
	public BigDecimal getR22_market() {
		return r22_market;
	}
	public void setR22_market(BigDecimal r22_market) {
		this.r22_market = r22_market;
	}
	public BigDecimal getR22_gpfsr_nom_amt() {
		return r22_gpfsr_nom_amt;
	}
	public void setR22_gpfsr_nom_amt(BigDecimal r22_gpfsr_nom_amt) {
		this.r22_gpfsr_nom_amt = r22_gpfsr_nom_amt;
	}
	public BigDecimal getR22_gpfsr_pos_att8_per_spe_ris() {
		return r22_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att8_per_spe_ris(BigDecimal r22_gpfsr_pos_att8_per_spe_ris) {
		this.r22_gpfsr_pos_att8_per_spe_ris = r22_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg() {
		return r22_gpfsr_chrg;
	}
	public void setR22_gpfsr_chrg(BigDecimal r22_gpfsr_chrg) {
		this.r22_gpfsr_chrg = r22_gpfsr_chrg;
	}
	public BigDecimal getR22_gpfsr_nom_amt1() {
		return r22_gpfsr_nom_amt1;
	}
	public void setR22_gpfsr_nom_amt1(BigDecimal r22_gpfsr_nom_amt1) {
		this.r22_gpfsr_nom_amt1 = r22_gpfsr_nom_amt1;
	}
	public BigDecimal getR22_gpfsr_pos_att4_per_spe_ris() {
		return r22_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att4_per_spe_ris(BigDecimal r22_gpfsr_pos_att4_per_spe_ris) {
		this.r22_gpfsr_pos_att4_per_spe_ris = r22_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg1() {
		return r22_gpfsr_chrg1;
	}
	public void setR22_gpfsr_chrg1(BigDecimal r22_gpfsr_chrg1) {
		this.r22_gpfsr_chrg1 = r22_gpfsr_chrg1;
	}
	public BigDecimal getR22_gpfsr_nom_amt2() {
		return r22_gpfsr_nom_amt2;
	}
	public void setR22_gpfsr_nom_amt2(BigDecimal r22_gpfsr_nom_amt2) {
		this.r22_gpfsr_nom_amt2 = r22_gpfsr_nom_amt2;
	}
	public BigDecimal getR22_gpfsr_pos_att2_per_spe_ris() {
		return r22_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att2_per_spe_ris(BigDecimal r22_gpfsr_pos_att2_per_spe_ris) {
		this.r22_gpfsr_pos_att2_per_spe_ris = r22_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg2() {
		return r22_gpfsr_chrg2;
	}
	public void setR22_gpfsr_chrg2(BigDecimal r22_gpfsr_chrg2) {
		this.r22_gpfsr_chrg2 = r22_gpfsr_chrg2;
	}
	public BigDecimal getR22_tot_spe_ris_chrg() {
		return r22_tot_spe_ris_chrg;
	}
	public void setR22_tot_spe_ris_chrg(BigDecimal r22_tot_spe_ris_chrg) {
		this.r22_tot_spe_ris_chrg = r22_tot_spe_ris_chrg;
	}
	public BigDecimal getR22_net_pos_gen_mar_ris() {
		return r22_net_pos_gen_mar_ris;
	}
	public void setR22_net_pos_gen_mar_ris(BigDecimal r22_net_pos_gen_mar_ris) {
		this.r22_net_pos_gen_mar_ris = r22_net_pos_gen_mar_ris;
	}
	public BigDecimal getR22_gen_mar_ris_chrg_8per() {
		return r22_gen_mar_ris_chrg_8per;
	}
	public void setR22_gen_mar_ris_chrg_8per(BigDecimal r22_gen_mar_ris_chrg_8per) {
		this.r22_gen_mar_ris_chrg_8per = r22_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR22_2per_gen_mar_ris_chrg_div_port() {
		return r22_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR22_2per_gen_mar_ris_chrg_div_port(BigDecimal r22_2per_gen_mar_ris_chrg_div_port) {
		this.r22_2per_gen_mar_ris_chrg_div_port = r22_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR22_tot_gen_mar_risk_chrg() {
		return r22_tot_gen_mar_risk_chrg;
	}
	public void setR22_tot_gen_mar_risk_chrg(BigDecimal r22_tot_gen_mar_risk_chrg) {
		this.r22_tot_gen_mar_risk_chrg = r22_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR22_tot_mar_ris_chrg() {
		return r22_tot_mar_ris_chrg;
	}
	public void setR22_tot_mar_ris_chrg(BigDecimal r22_tot_mar_ris_chrg) {
		this.r22_tot_mar_ris_chrg = r22_tot_mar_ris_chrg;
	}
	public String getR23_market() {
		return r23_market;
	}
	public void setR23_market(String r23_market) {
		this.r23_market = r23_market;
	}
	public BigDecimal getR23_gpfsr_nom_amt() {
		return r23_gpfsr_nom_amt;
	}
	public void setR23_gpfsr_nom_amt(BigDecimal r23_gpfsr_nom_amt) {
		this.r23_gpfsr_nom_amt = r23_gpfsr_nom_amt;
	}
	public BigDecimal getR23_gpfsr_pos_att8_per_spe_ris() {
		return r23_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att8_per_spe_ris(BigDecimal r23_gpfsr_pos_att8_per_spe_ris) {
		this.r23_gpfsr_pos_att8_per_spe_ris = r23_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg() {
		return r23_gpfsr_chrg;
	}
	public void setR23_gpfsr_chrg(BigDecimal r23_gpfsr_chrg) {
		this.r23_gpfsr_chrg = r23_gpfsr_chrg;
	}
	public BigDecimal getR23_gpfsr_nom_amt1() {
		return r23_gpfsr_nom_amt1;
	}
	public void setR23_gpfsr_nom_amt1(BigDecimal r23_gpfsr_nom_amt1) {
		this.r23_gpfsr_nom_amt1 = r23_gpfsr_nom_amt1;
	}
	public BigDecimal getR23_gpfsr_pos_att4_per_spe_ris() {
		return r23_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att4_per_spe_ris(BigDecimal r23_gpfsr_pos_att4_per_spe_ris) {
		this.r23_gpfsr_pos_att4_per_spe_ris = r23_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg1() {
		return r23_gpfsr_chrg1;
	}
	public void setR23_gpfsr_chrg1(BigDecimal r23_gpfsr_chrg1) {
		this.r23_gpfsr_chrg1 = r23_gpfsr_chrg1;
	}
	public BigDecimal getR23_gpfsr_nom_amt2() {
		return r23_gpfsr_nom_amt2;
	}
	public void setR23_gpfsr_nom_amt2(BigDecimal r23_gpfsr_nom_amt2) {
		this.r23_gpfsr_nom_amt2 = r23_gpfsr_nom_amt2;
	}
	public BigDecimal getR23_gpfsr_pos_att2_per_spe_ris() {
		return r23_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att2_per_spe_ris(BigDecimal r23_gpfsr_pos_att2_per_spe_ris) {
		this.r23_gpfsr_pos_att2_per_spe_ris = r23_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg2() {
		return r23_gpfsr_chrg2;
	}
	public void setR23_gpfsr_chrg2(BigDecimal r23_gpfsr_chrg2) {
		this.r23_gpfsr_chrg2 = r23_gpfsr_chrg2;
	}
	public BigDecimal getR23_tot_spe_ris_chrg() {
		return r23_tot_spe_ris_chrg;
	}
	public void setR23_tot_spe_ris_chrg(BigDecimal r23_tot_spe_ris_chrg) {
		this.r23_tot_spe_ris_chrg = r23_tot_spe_ris_chrg;
	}
	public BigDecimal getR23_net_pos_gen_mar_ris() {
		return r23_net_pos_gen_mar_ris;
	}
	public void setR23_net_pos_gen_mar_ris(BigDecimal r23_net_pos_gen_mar_ris) {
		this.r23_net_pos_gen_mar_ris = r23_net_pos_gen_mar_ris;
	}
	public BigDecimal getR23_gen_mar_ris_chrg_8per() {
		return r23_gen_mar_ris_chrg_8per;
	}
	public void setR23_gen_mar_ris_chrg_8per(BigDecimal r23_gen_mar_ris_chrg_8per) {
		this.r23_gen_mar_ris_chrg_8per = r23_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR23_2per_gen_mar_ris_chrg_div_port() {
		return r23_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR23_2per_gen_mar_ris_chrg_div_port(BigDecimal r23_2per_gen_mar_ris_chrg_div_port) {
		this.r23_2per_gen_mar_ris_chrg_div_port = r23_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR23_tot_gen_mar_risk_chrg() {
		return r23_tot_gen_mar_risk_chrg;
	}
	public void setR23_tot_gen_mar_risk_chrg(BigDecimal r23_tot_gen_mar_risk_chrg) {
		this.r23_tot_gen_mar_risk_chrg = r23_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR23_tot_mar_ris_chrg() {
		return r23_tot_mar_ris_chrg;
	}
	public void setR23_tot_mar_ris_chrg(BigDecimal r23_tot_mar_ris_chrg) {
		this.r23_tot_mar_ris_chrg = r23_tot_mar_ris_chrg;
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


public class M_EPR_Archival_Summary_RowMapper
        implements RowMapper<M_EPR_Archival_Summary_Entity> {

    @Override
    public M_EPR_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        M_EPR_Archival_Summary_Entity obj = new M_EPR_Archival_Summary_Entity();

// =========================
// R11
// =========================
obj.setR11_market(rs.getBigDecimal("r11_market"));
obj.setR11_gpfsr_nom_amt(rs.getBigDecimal("r11_gpfsr_nom_amt"));
obj.setR11_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att8_per_spe_ris"));
obj.setR11_gpfsr_chrg(rs.getBigDecimal("r11_gpfsr_chrg"));
obj.setR11_gpfsr_nom_amt1(rs.getBigDecimal("r11_gpfsr_nom_amt1"));
obj.setR11_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att4_per_spe_ris"));
obj.setR11_gpfsr_chrg1(rs.getBigDecimal("r11_gpfsr_chrg1"));
obj.setR11_gpfsr_nom_amt2(rs.getBigDecimal("r11_gpfsr_nom_amt2"));
obj.setR11_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att2_per_spe_ris"));
obj.setR11_gpfsr_chrg2(rs.getBigDecimal("r11_gpfsr_chrg2"));
obj.setR11_tot_spe_ris_chrg(rs.getBigDecimal("r11_tot_spe_ris_chrg"));
obj.setR11_net_pos_gen_mar_ris(rs.getBigDecimal("r11_net_pos_gen_mar_ris"));
obj.setR11_gen_mar_ris_chrg_8per(rs.getBigDecimal("r11_gen_mar_ris_chrg_8per"));
obj.setR11_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r11_2per_gen_mar_ris_chrg_div_port"));
obj.setR11_tot_gen_mar_risk_chrg(rs.getBigDecimal("r11_tot_gen_mar_risk_chrg"));
obj.setR11_tot_mar_ris_chrg(rs.getBigDecimal("r11_tot_mar_ris_chrg"));

// =========================
// R12
// =========================
obj.setR12_market(rs.getBigDecimal("r12_market"));
obj.setR12_gpfsr_nom_amt(rs.getBigDecimal("r12_gpfsr_nom_amt"));
obj.setR12_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att8_per_spe_ris"));
obj.setR12_gpfsr_chrg(rs.getBigDecimal("r12_gpfsr_chrg"));
obj.setR12_gpfsr_nom_amt1(rs.getBigDecimal("r12_gpfsr_nom_amt1"));
obj.setR12_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att4_per_spe_ris"));
obj.setR12_gpfsr_chrg1(rs.getBigDecimal("r12_gpfsr_chrg1"));
obj.setR12_gpfsr_nom_amt2(rs.getBigDecimal("r12_gpfsr_nom_amt2"));
obj.setR12_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att2_per_spe_ris"));
obj.setR12_gpfsr_chrg2(rs.getBigDecimal("r12_gpfsr_chrg2"));
obj.setR12_tot_spe_ris_chrg(rs.getBigDecimal("r12_tot_spe_ris_chrg"));
obj.setR12_net_pos_gen_mar_ris(rs.getBigDecimal("r12_net_pos_gen_mar_ris"));
obj.setR12_gen_mar_ris_chrg_8per(rs.getBigDecimal("r12_gen_mar_ris_chrg_8per"));
obj.setR12_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r12_2per_gen_mar_ris_chrg_div_port"));
obj.setR12_tot_gen_mar_risk_chrg(rs.getBigDecimal("r12_tot_gen_mar_risk_chrg"));
obj.setR12_tot_mar_ris_chrg(rs.getBigDecimal("r12_tot_mar_ris_chrg"));

// =========================
// R13
// =========================
obj.setR13_market(rs.getBigDecimal("r13_market"));
obj.setR13_gpfsr_nom_amt(rs.getBigDecimal("r13_gpfsr_nom_amt"));
obj.setR13_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att8_per_spe_ris"));
obj.setR13_gpfsr_chrg(rs.getBigDecimal("r13_gpfsr_chrg"));
obj.setR13_gpfsr_nom_amt1(rs.getBigDecimal("r13_gpfsr_nom_amt1"));
obj.setR13_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att4_per_spe_ris"));
obj.setR13_gpfsr_chrg1(rs.getBigDecimal("r13_gpfsr_chrg1"));
obj.setR13_gpfsr_nom_amt2(rs.getBigDecimal("r13_gpfsr_nom_amt2"));
obj.setR13_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att2_per_spe_ris"));
obj.setR13_gpfsr_chrg2(rs.getBigDecimal("r13_gpfsr_chrg2"));
obj.setR13_tot_spe_ris_chrg(rs.getBigDecimal("r13_tot_spe_ris_chrg"));
obj.setR13_net_pos_gen_mar_ris(rs.getBigDecimal("r13_net_pos_gen_mar_ris"));
obj.setR13_gen_mar_ris_chrg_8per(rs.getBigDecimal("r13_gen_mar_ris_chrg_8per"));
obj.setR13_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r13_2per_gen_mar_ris_chrg_div_port"));
obj.setR13_tot_gen_mar_risk_chrg(rs.getBigDecimal("r13_tot_gen_mar_risk_chrg"));
obj.setR13_tot_mar_ris_chrg(rs.getBigDecimal("r13_tot_mar_ris_chrg"));

// =========================
// R14
// =========================
obj.setR14_market(rs.getBigDecimal("r14_market"));
obj.setR14_gpfsr_nom_amt(rs.getBigDecimal("r14_gpfsr_nom_amt"));
obj.setR14_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att8_per_spe_ris"));
obj.setR14_gpfsr_chrg(rs.getBigDecimal("r14_gpfsr_chrg"));
obj.setR14_gpfsr_nom_amt1(rs.getBigDecimal("r14_gpfsr_nom_amt1"));
obj.setR14_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att4_per_spe_ris"));
obj.setR14_gpfsr_chrg1(rs.getBigDecimal("r14_gpfsr_chrg1"));
obj.setR14_gpfsr_nom_amt2(rs.getBigDecimal("r14_gpfsr_nom_amt2"));
obj.setR14_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att2_per_spe_ris"));
obj.setR14_gpfsr_chrg2(rs.getBigDecimal("r14_gpfsr_chrg2"));
obj.setR14_tot_spe_ris_chrg(rs.getBigDecimal("r14_tot_spe_ris_chrg"));
obj.setR14_net_pos_gen_mar_ris(rs.getBigDecimal("r14_net_pos_gen_mar_ris"));
obj.setR14_gen_mar_ris_chrg_8per(rs.getBigDecimal("r14_gen_mar_ris_chrg_8per"));
obj.setR14_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r14_2per_gen_mar_ris_chrg_div_port"));
obj.setR14_tot_gen_mar_risk_chrg(rs.getBigDecimal("r14_tot_gen_mar_risk_chrg"));
obj.setR14_tot_mar_ris_chrg(rs.getBigDecimal("r14_tot_mar_ris_chrg"));

// =========================
// R15
// =========================
obj.setR15_market(rs.getBigDecimal("r15_market"));
obj.setR15_gpfsr_nom_amt(rs.getBigDecimal("r15_gpfsr_nom_amt"));
obj.setR15_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att8_per_spe_ris"));
obj.setR15_gpfsr_chrg(rs.getBigDecimal("r15_gpfsr_chrg"));
obj.setR15_gpfsr_nom_amt1(rs.getBigDecimal("r15_gpfsr_nom_amt1"));
obj.setR15_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att4_per_spe_ris"));
obj.setR15_gpfsr_chrg1(rs.getBigDecimal("r15_gpfsr_chrg1"));
obj.setR15_gpfsr_nom_amt2(rs.getBigDecimal("r15_gpfsr_nom_amt2"));
obj.setR15_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att2_per_spe_ris"));
obj.setR15_gpfsr_chrg2(rs.getBigDecimal("r15_gpfsr_chrg2"));
obj.setR15_tot_spe_ris_chrg(rs.getBigDecimal("r15_tot_spe_ris_chrg"));
obj.setR15_net_pos_gen_mar_ris(rs.getBigDecimal("r15_net_pos_gen_mar_ris"));
obj.setR15_gen_mar_ris_chrg_8per(rs.getBigDecimal("r15_gen_mar_ris_chrg_8per"));
obj.setR15_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r15_2per_gen_mar_ris_chrg_div_port"));
obj.setR15_tot_gen_mar_risk_chrg(rs.getBigDecimal("r15_tot_gen_mar_risk_chrg"));
obj.setR15_tot_mar_ris_chrg(rs.getBigDecimal("r15_tot_mar_ris_chrg"));

// =========================
// R16
// =========================
obj.setR16_market(rs.getBigDecimal("r16_market"));
obj.setR16_gpfsr_nom_amt(rs.getBigDecimal("r16_gpfsr_nom_amt"));
obj.setR16_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att8_per_spe_ris"));
obj.setR16_gpfsr_chrg(rs.getBigDecimal("r16_gpfsr_chrg"));
obj.setR16_gpfsr_nom_amt1(rs.getBigDecimal("r16_gpfsr_nom_amt1"));
obj.setR16_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att4_per_spe_ris"));
obj.setR16_gpfsr_chrg1(rs.getBigDecimal("r16_gpfsr_chrg1"));
obj.setR16_gpfsr_nom_amt2(rs.getBigDecimal("r16_gpfsr_nom_amt2"));
obj.setR16_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att2_per_spe_ris"));
obj.setR16_gpfsr_chrg2(rs.getBigDecimal("r16_gpfsr_chrg2"));
obj.setR16_tot_spe_ris_chrg(rs.getBigDecimal("r16_tot_spe_ris_chrg"));
obj.setR16_net_pos_gen_mar_ris(rs.getBigDecimal("r16_net_pos_gen_mar_ris"));
obj.setR16_gen_mar_ris_chrg_8per(rs.getBigDecimal("r16_gen_mar_ris_chrg_8per"));
obj.setR16_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r16_2per_gen_mar_ris_chrg_div_port"));
obj.setR16_tot_gen_mar_risk_chrg(rs.getBigDecimal("r16_tot_gen_mar_risk_chrg"));
obj.setR16_tot_mar_ris_chrg(rs.getBigDecimal("r16_tot_mar_ris_chrg"));

// =========================
// R17
// =========================
obj.setR17_market(rs.getBigDecimal("r17_market"));
obj.setR17_gpfsr_nom_amt(rs.getBigDecimal("r17_gpfsr_nom_amt"));
obj.setR17_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att8_per_spe_ris"));
obj.setR17_gpfsr_chrg(rs.getBigDecimal("r17_gpfsr_chrg"));
obj.setR17_gpfsr_nom_amt1(rs.getBigDecimal("r17_gpfsr_nom_amt1"));
obj.setR17_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att4_per_spe_ris"));
obj.setR17_gpfsr_chrg1(rs.getBigDecimal("r17_gpfsr_chrg1"));
obj.setR17_gpfsr_nom_amt2(rs.getBigDecimal("r17_gpfsr_nom_amt2"));
obj.setR17_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att2_per_spe_ris"));
obj.setR17_gpfsr_chrg2(rs.getBigDecimal("r17_gpfsr_chrg2"));
obj.setR17_tot_spe_ris_chrg(rs.getBigDecimal("r17_tot_spe_ris_chrg"));
obj.setR17_net_pos_gen_mar_ris(rs.getBigDecimal("r17_net_pos_gen_mar_ris"));
obj.setR17_gen_mar_ris_chrg_8per(rs.getBigDecimal("r17_gen_mar_ris_chrg_8per"));
obj.setR17_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r17_2per_gen_mar_ris_chrg_div_port"));
obj.setR17_tot_gen_mar_risk_chrg(rs.getBigDecimal("r17_tot_gen_mar_risk_chrg"));
obj.setR17_tot_mar_ris_chrg(rs.getBigDecimal("r17_tot_mar_ris_chrg"));

// =========================
// R18
// =========================
obj.setR18_market(rs.getBigDecimal("r18_market"));
obj.setR18_gpfsr_nom_amt(rs.getBigDecimal("r18_gpfsr_nom_amt"));
obj.setR18_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att8_per_spe_ris"));
obj.setR18_gpfsr_chrg(rs.getBigDecimal("r18_gpfsr_chrg"));
obj.setR18_gpfsr_nom_amt1(rs.getBigDecimal("r18_gpfsr_nom_amt1"));
obj.setR18_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att4_per_spe_ris"));
obj.setR18_gpfsr_chrg1(rs.getBigDecimal("r18_gpfsr_chrg1"));
obj.setR18_gpfsr_nom_amt2(rs.getBigDecimal("r18_gpfsr_nom_amt2"));
obj.setR18_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att2_per_spe_ris"));
obj.setR18_gpfsr_chrg2(rs.getBigDecimal("r18_gpfsr_chrg2"));
obj.setR18_tot_spe_ris_chrg(rs.getBigDecimal("r18_tot_spe_ris_chrg"));
obj.setR18_net_pos_gen_mar_ris(rs.getBigDecimal("r18_net_pos_gen_mar_ris"));
obj.setR18_gen_mar_ris_chrg_8per(rs.getBigDecimal("r18_gen_mar_ris_chrg_8per"));
obj.setR18_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r18_2per_gen_mar_ris_chrg_div_port"));
obj.setR18_tot_gen_mar_risk_chrg(rs.getBigDecimal("r18_tot_gen_mar_risk_chrg"));
obj.setR18_tot_mar_ris_chrg(rs.getBigDecimal("r18_tot_mar_ris_chrg"));

// =========================
// R19
// =========================
obj.setR19_market(rs.getBigDecimal("r19_market"));
obj.setR19_gpfsr_nom_amt(rs.getBigDecimal("r19_gpfsr_nom_amt"));
obj.setR19_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att8_per_spe_ris"));
obj.setR19_gpfsr_chrg(rs.getBigDecimal("r19_gpfsr_chrg"));
obj.setR19_gpfsr_nom_amt1(rs.getBigDecimal("r19_gpfsr_nom_amt1"));
obj.setR19_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att4_per_spe_ris"));
obj.setR19_gpfsr_chrg1(rs.getBigDecimal("r19_gpfsr_chrg1"));
obj.setR19_gpfsr_nom_amt2(rs.getBigDecimal("r19_gpfsr_nom_amt2"));
obj.setR19_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att2_per_spe_ris"));
obj.setR19_gpfsr_chrg2(rs.getBigDecimal("r19_gpfsr_chrg2"));
obj.setR19_tot_spe_ris_chrg(rs.getBigDecimal("r19_tot_spe_ris_chrg"));
obj.setR19_net_pos_gen_mar_ris(rs.getBigDecimal("r19_net_pos_gen_mar_ris"));
obj.setR19_gen_mar_ris_chrg_8per(rs.getBigDecimal("r19_gen_mar_ris_chrg_8per"));
obj.setR19_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r19_2per_gen_mar_ris_chrg_div_port"));
obj.setR19_tot_gen_mar_risk_chrg(rs.getBigDecimal("r19_tot_gen_mar_risk_chrg"));
obj.setR19_tot_mar_ris_chrg(rs.getBigDecimal("r19_tot_mar_ris_chrg"));

// =========================
// R20
// =========================
obj.setR20_market(rs.getBigDecimal("r20_market"));
obj.setR20_gpfsr_nom_amt(rs.getBigDecimal("r20_gpfsr_nom_amt"));
obj.setR20_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att8_per_spe_ris"));
obj.setR20_gpfsr_chrg(rs.getBigDecimal("r20_gpfsr_chrg"));
obj.setR20_gpfsr_nom_amt1(rs.getBigDecimal("r20_gpfsr_nom_amt1"));
obj.setR20_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att4_per_spe_ris"));
obj.setR20_gpfsr_chrg1(rs.getBigDecimal("r20_gpfsr_chrg1"));
obj.setR20_gpfsr_nom_amt2(rs.getBigDecimal("r20_gpfsr_nom_amt2"));
obj.setR20_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att2_per_spe_ris"));
obj.setR20_gpfsr_chrg2(rs.getBigDecimal("r20_gpfsr_chrg2"));
obj.setR20_tot_spe_ris_chrg(rs.getBigDecimal("r20_tot_spe_ris_chrg"));
obj.setR20_net_pos_gen_mar_ris(rs.getBigDecimal("r20_net_pos_gen_mar_ris"));
obj.setR20_gen_mar_ris_chrg_8per(rs.getBigDecimal("r20_gen_mar_ris_chrg_8per"));
obj.setR20_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r20_2per_gen_mar_ris_chrg_div_port"));
obj.setR20_tot_gen_mar_risk_chrg(rs.getBigDecimal("r20_tot_gen_mar_risk_chrg"));
obj.setR20_tot_mar_ris_chrg(rs.getBigDecimal("r20_tot_mar_ris_chrg"));

// =========================
// R21
// =========================
obj.setR21_market(rs.getBigDecimal("r21_market"));
obj.setR21_gpfsr_nom_amt(rs.getBigDecimal("r21_gpfsr_nom_amt"));
obj.setR21_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att8_per_spe_ris"));
obj.setR21_gpfsr_chrg(rs.getBigDecimal("r21_gpfsr_chrg"));
obj.setR21_gpfsr_nom_amt1(rs.getBigDecimal("r21_gpfsr_nom_amt1"));
obj.setR21_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att4_per_spe_ris"));
obj.setR21_gpfsr_chrg1(rs.getBigDecimal("r21_gpfsr_chrg1"));
obj.setR21_gpfsr_nom_amt2(rs.getBigDecimal("r21_gpfsr_nom_amt2"));
obj.setR21_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att2_per_spe_ris"));
obj.setR21_gpfsr_chrg2(rs.getBigDecimal("r21_gpfsr_chrg2"));
obj.setR21_tot_spe_ris_chrg(rs.getBigDecimal("r21_tot_spe_ris_chrg"));
obj.setR21_net_pos_gen_mar_ris(rs.getBigDecimal("r21_net_pos_gen_mar_ris"));
obj.setR21_gen_mar_ris_chrg_8per(rs.getBigDecimal("r21_gen_mar_ris_chrg_8per"));
obj.setR21_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r21_2per_gen_mar_ris_chrg_div_port"));
obj.setR21_tot_gen_mar_risk_chrg(rs.getBigDecimal("r21_tot_gen_mar_risk_chrg"));
obj.setR21_tot_mar_ris_chrg(rs.getBigDecimal("r21_tot_mar_ris_chrg"));


// =========================
// R22
// =========================
obj.setR22_market(rs.getBigDecimal("r22_market"));
obj.setR22_gpfsr_nom_amt(rs.getBigDecimal("r22_gpfsr_nom_amt"));
obj.setR22_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att8_per_spe_ris"));
obj.setR22_gpfsr_chrg(rs.getBigDecimal("r22_gpfsr_chrg"));
obj.setR22_gpfsr_nom_amt1(rs.getBigDecimal("r22_gpfsr_nom_amt1"));
obj.setR22_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att4_per_spe_ris"));
obj.setR22_gpfsr_chrg1(rs.getBigDecimal("r22_gpfsr_chrg1"));
obj.setR22_gpfsr_nom_amt2(rs.getBigDecimal("r22_gpfsr_nom_amt2"));
obj.setR22_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att2_per_spe_ris"));
obj.setR22_gpfsr_chrg2(rs.getBigDecimal("r22_gpfsr_chrg2"));
obj.setR22_tot_spe_ris_chrg(rs.getBigDecimal("r22_tot_spe_ris_chrg"));
obj.setR22_net_pos_gen_mar_ris(rs.getBigDecimal("r22_net_pos_gen_mar_ris"));
obj.setR22_gen_mar_ris_chrg_8per(rs.getBigDecimal("r22_gen_mar_ris_chrg_8per"));
obj.setR22_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r22_2per_gen_mar_ris_chrg_div_port"));
obj.setR22_tot_gen_mar_risk_chrg(rs.getBigDecimal("r22_tot_gen_mar_risk_chrg"));
obj.setR22_tot_mar_ris_chrg(rs.getBigDecimal("r22_tot_mar_ris_chrg"));

// =========================
// R23
// =========================
obj.setR23_market(rs.getString("r23_market"));
obj.setR23_gpfsr_nom_amt(rs.getBigDecimal("r23_gpfsr_nom_amt"));
obj.setR23_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att8_per_spe_ris"));
obj.setR23_gpfsr_chrg(rs.getBigDecimal("r23_gpfsr_chrg"));
obj.setR23_gpfsr_nom_amt1(rs.getBigDecimal("r23_gpfsr_nom_amt1"));
obj.setR23_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att4_per_spe_ris"));
obj.setR23_gpfsr_chrg1(rs.getBigDecimal("r23_gpfsr_chrg1"));
obj.setR23_gpfsr_nom_amt2(rs.getBigDecimal("r23_gpfsr_nom_amt2"));
obj.setR23_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att2_per_spe_ris"));
obj.setR23_gpfsr_chrg2(rs.getBigDecimal("r23_gpfsr_chrg2"));
obj.setR23_tot_spe_ris_chrg(rs.getBigDecimal("r23_tot_spe_ris_chrg"));
obj.setR23_net_pos_gen_mar_ris(rs.getBigDecimal("r23_net_pos_gen_mar_ris"));
obj.setR23_gen_mar_ris_chrg_8per(rs.getBigDecimal("r23_gen_mar_ris_chrg_8per"));
obj.setR23_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r23_2per_gen_mar_ris_chrg_div_port"));
obj.setR23_tot_gen_mar_risk_chrg(rs.getBigDecimal("r23_tot_gen_mar_risk_chrg"));
obj.setR23_tot_mar_ris_chrg(rs.getBigDecimal("r23_tot_mar_ris_chrg"));

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


public class M_EPR_Archival_Summary_Entity {
	
	
private BigDecimal	r11_market;
	private BigDecimal	r11_gpfsr_nom_amt;
	private BigDecimal	r11_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg;
	private BigDecimal	r11_gpfsr_nom_amt1;
	private BigDecimal	r11_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg1;
	private BigDecimal	r11_gpfsr_nom_amt2;
	private BigDecimal	r11_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg2;
	private BigDecimal	r11_tot_spe_ris_chrg;
	private BigDecimal	r11_net_pos_gen_mar_ris;
	private BigDecimal	r11_gen_mar_ris_chrg_8per;
	private BigDecimal	r11_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r11_tot_gen_mar_risk_chrg;
	private BigDecimal	r11_tot_mar_ris_chrg;
	
	private BigDecimal	r12_market;
	private BigDecimal	r12_gpfsr_nom_amt;
	private BigDecimal	r12_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg;
	private BigDecimal	r12_gpfsr_nom_amt1;
	private BigDecimal	r12_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg1;
	private BigDecimal	r12_gpfsr_nom_amt2;
	private BigDecimal	r12_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg2;
	private BigDecimal	r12_tot_spe_ris_chrg;
	private BigDecimal	r12_net_pos_gen_mar_ris;
	private BigDecimal	r12_gen_mar_ris_chrg_8per;
	private BigDecimal	r12_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r12_tot_gen_mar_risk_chrg;
	private BigDecimal	r12_tot_mar_ris_chrg;
	
	private BigDecimal	r13_market;
	private BigDecimal	r13_gpfsr_nom_amt;
	private BigDecimal	r13_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg;
	private BigDecimal	r13_gpfsr_nom_amt1;
	private BigDecimal	r13_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg1;
	private BigDecimal	r13_gpfsr_nom_amt2;
	private BigDecimal	r13_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg2;
	private BigDecimal	r13_tot_spe_ris_chrg;
	private BigDecimal	r13_net_pos_gen_mar_ris;
	private BigDecimal	r13_gen_mar_ris_chrg_8per;
	private BigDecimal	r13_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r13_tot_gen_mar_risk_chrg;
	private BigDecimal	r13_tot_mar_ris_chrg;
	
	private BigDecimal	r14_market;
	private BigDecimal	r14_gpfsr_nom_amt;
	private BigDecimal	r14_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg;
	private BigDecimal	r14_gpfsr_nom_amt1;
	private BigDecimal	r14_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg1;
	private BigDecimal	r14_gpfsr_nom_amt2;
	private BigDecimal	r14_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg2;
	private BigDecimal	r14_tot_spe_ris_chrg;
	private BigDecimal	r14_net_pos_gen_mar_ris;
	private BigDecimal	r14_gen_mar_ris_chrg_8per;
	private BigDecimal	r14_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r14_tot_gen_mar_risk_chrg;
	private BigDecimal	r14_tot_mar_ris_chrg;
	
	private BigDecimal	r15_market;
	private BigDecimal	r15_gpfsr_nom_amt;
	private BigDecimal	r15_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg;
	private BigDecimal	r15_gpfsr_nom_amt1;
	private BigDecimal	r15_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg1;
	private BigDecimal	r15_gpfsr_nom_amt2;
	private BigDecimal	r15_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg2;
	private BigDecimal	r15_tot_spe_ris_chrg;
	private BigDecimal	r15_net_pos_gen_mar_ris;
	private BigDecimal	r15_gen_mar_ris_chrg_8per;
	private BigDecimal	r15_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r15_tot_gen_mar_risk_chrg;
	private BigDecimal	r15_tot_mar_ris_chrg;
	
	private BigDecimal	r16_market;
	private BigDecimal	r16_gpfsr_nom_amt;
	private BigDecimal	r16_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg;
	private BigDecimal	r16_gpfsr_nom_amt1;
	private BigDecimal	r16_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg1;
	private BigDecimal	r16_gpfsr_nom_amt2;
	private BigDecimal	r16_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg2;
	private BigDecimal	r16_tot_spe_ris_chrg;
	private BigDecimal	r16_net_pos_gen_mar_ris;
	private BigDecimal	r16_gen_mar_ris_chrg_8per;
	private BigDecimal	r16_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r16_tot_gen_mar_risk_chrg;
	private BigDecimal	r16_tot_mar_ris_chrg;
	
	private BigDecimal	r17_market;
	private BigDecimal	r17_gpfsr_nom_amt;
	private BigDecimal	r17_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg;
	private BigDecimal	r17_gpfsr_nom_amt1;
	private BigDecimal	r17_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg1;
	private BigDecimal	r17_gpfsr_nom_amt2;
	private BigDecimal	r17_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg2;
	private BigDecimal	r17_tot_spe_ris_chrg;
	private BigDecimal	r17_net_pos_gen_mar_ris;
	private BigDecimal	r17_gen_mar_ris_chrg_8per;
	private BigDecimal	r17_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r17_tot_gen_mar_risk_chrg;
	private BigDecimal	r17_tot_mar_ris_chrg;
	
	private BigDecimal	r18_market;
	private BigDecimal	r18_gpfsr_nom_amt;
	private BigDecimal	r18_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg;
	private BigDecimal	r18_gpfsr_nom_amt1;
	private BigDecimal	r18_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg1;
	private BigDecimal	r18_gpfsr_nom_amt2;
	private BigDecimal	r18_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg2;
	private BigDecimal	r18_tot_spe_ris_chrg;
	private BigDecimal	r18_net_pos_gen_mar_ris;
	private BigDecimal	r18_gen_mar_ris_chrg_8per;
	private BigDecimal	r18_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r18_tot_gen_mar_risk_chrg;
	private BigDecimal	r18_tot_mar_ris_chrg;
	
	private BigDecimal	r19_market;
	private BigDecimal	r19_gpfsr_nom_amt;
	private BigDecimal	r19_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg;
	private BigDecimal	r19_gpfsr_nom_amt1;
	private BigDecimal	r19_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg1;
	private BigDecimal	r19_gpfsr_nom_amt2;
	private BigDecimal	r19_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg2;
	private BigDecimal	r19_tot_spe_ris_chrg;
	private BigDecimal	r19_net_pos_gen_mar_ris;
	private BigDecimal	r19_gen_mar_ris_chrg_8per;
	private BigDecimal	r19_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r19_tot_gen_mar_risk_chrg;
	private BigDecimal	r19_tot_mar_ris_chrg;
	
	private BigDecimal	r20_market;
	private BigDecimal	r20_gpfsr_nom_amt;
	private BigDecimal	r20_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg;
	private BigDecimal	r20_gpfsr_nom_amt1;
	private BigDecimal	r20_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg1;
	private BigDecimal	r20_gpfsr_nom_amt2;
	private BigDecimal	r20_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg2;
	private BigDecimal	r20_tot_spe_ris_chrg;
	private BigDecimal	r20_net_pos_gen_mar_ris;
	private BigDecimal	r20_gen_mar_ris_chrg_8per;
	private BigDecimal	r20_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r20_tot_gen_mar_risk_chrg;
	private BigDecimal	r20_tot_mar_ris_chrg;
	
	private BigDecimal	r21_market;
	private BigDecimal	r21_gpfsr_nom_amt;
	private BigDecimal	r21_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg;
	private BigDecimal	r21_gpfsr_nom_amt1;
	private BigDecimal	r21_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg1;
	private BigDecimal	r21_gpfsr_nom_amt2;
	private BigDecimal	r21_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg2;
	private BigDecimal	r21_tot_spe_ris_chrg;
	private BigDecimal	r21_net_pos_gen_mar_ris;
	private BigDecimal	r21_gen_mar_ris_chrg_8per;
	private BigDecimal	r21_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r21_tot_gen_mar_risk_chrg;
	private BigDecimal	r21_tot_mar_ris_chrg;
	
	private BigDecimal	r22_market;
	private BigDecimal	r22_gpfsr_nom_amt;
	private BigDecimal	r22_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg;
	private BigDecimal	r22_gpfsr_nom_amt1;
	private BigDecimal	r22_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg1;
	private BigDecimal	r22_gpfsr_nom_amt2;
	private BigDecimal	r22_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg2;
	private BigDecimal	r22_tot_spe_ris_chrg;
	private BigDecimal	r22_net_pos_gen_mar_ris;
	private BigDecimal	r22_gen_mar_ris_chrg_8per;
	private BigDecimal	r22_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r22_tot_gen_mar_risk_chrg;
	private BigDecimal	r22_tot_mar_ris_chrg;
	
	private String	r23_market;
	private BigDecimal	r23_gpfsr_nom_amt;
	private BigDecimal	r23_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg;
	private BigDecimal	r23_gpfsr_nom_amt1;
	private BigDecimal	r23_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg1;
	private BigDecimal	r23_gpfsr_nom_amt2;
	private BigDecimal	r23_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg2;
	private BigDecimal	r23_tot_spe_ris_chrg;
	private BigDecimal	r23_net_pos_gen_mar_ris;
	private BigDecimal	r23_gen_mar_ris_chrg_8per;
	private BigDecimal	r23_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r23_tot_gen_mar_risk_chrg;
	private BigDecimal	r23_tot_mar_ris_chrg;
	               
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
	
public BigDecimal getR11_market() {
		return r11_market;
	}
	public void setR11_market(BigDecimal r11_market) {
		this.r11_market = r11_market;
	}
	public BigDecimal getR11_gpfsr_nom_amt() {
		return r11_gpfsr_nom_amt;
	}
	public void setR11_gpfsr_nom_amt(BigDecimal r11_gpfsr_nom_amt) {
		this.r11_gpfsr_nom_amt = r11_gpfsr_nom_amt;
	}
	public BigDecimal getR11_gpfsr_pos_att8_per_spe_ris() {
		return r11_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att8_per_spe_ris(BigDecimal r11_gpfsr_pos_att8_per_spe_ris) {
		this.r11_gpfsr_pos_att8_per_spe_ris = r11_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg() {
		return r11_gpfsr_chrg;
	}
	public void setR11_gpfsr_chrg(BigDecimal r11_gpfsr_chrg) {
		this.r11_gpfsr_chrg = r11_gpfsr_chrg;
	}
	public BigDecimal getR11_gpfsr_nom_amt1() {
		return r11_gpfsr_nom_amt1;
	}
	public void setR11_gpfsr_nom_amt1(BigDecimal r11_gpfsr_nom_amt1) {
		this.r11_gpfsr_nom_amt1 = r11_gpfsr_nom_amt1;
	}
	public BigDecimal getR11_gpfsr_pos_att4_per_spe_ris() {
		return r11_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att4_per_spe_ris(BigDecimal r11_gpfsr_pos_att4_per_spe_ris) {
		this.r11_gpfsr_pos_att4_per_spe_ris = r11_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg1() {
		return r11_gpfsr_chrg1;
	}
	public void setR11_gpfsr_chrg1(BigDecimal r11_gpfsr_chrg1) {
		this.r11_gpfsr_chrg1 = r11_gpfsr_chrg1;
	}
	public BigDecimal getR11_gpfsr_nom_amt2() {
		return r11_gpfsr_nom_amt2;
	}
	public void setR11_gpfsr_nom_amt2(BigDecimal r11_gpfsr_nom_amt2) {
		this.r11_gpfsr_nom_amt2 = r11_gpfsr_nom_amt2;
	}
	public BigDecimal getR11_gpfsr_pos_att2_per_spe_ris() {
		return r11_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att2_per_spe_ris(BigDecimal r11_gpfsr_pos_att2_per_spe_ris) {
		this.r11_gpfsr_pos_att2_per_spe_ris = r11_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg2() {
		return r11_gpfsr_chrg2;
	}
	public void setR11_gpfsr_chrg2(BigDecimal r11_gpfsr_chrg2) {
		this.r11_gpfsr_chrg2 = r11_gpfsr_chrg2;
	}
	public BigDecimal getR11_tot_spe_ris_chrg() {
		return r11_tot_spe_ris_chrg;
	}
	public void setR11_tot_spe_ris_chrg(BigDecimal r11_tot_spe_ris_chrg) {
		this.r11_tot_spe_ris_chrg = r11_tot_spe_ris_chrg;
	}
	public BigDecimal getR11_net_pos_gen_mar_ris() {
		return r11_net_pos_gen_mar_ris;
	}
	public void setR11_net_pos_gen_mar_ris(BigDecimal r11_net_pos_gen_mar_ris) {
		this.r11_net_pos_gen_mar_ris = r11_net_pos_gen_mar_ris;
	}
	public BigDecimal getR11_gen_mar_ris_chrg_8per() {
		return r11_gen_mar_ris_chrg_8per;
	}
	public void setR11_gen_mar_ris_chrg_8per(BigDecimal r11_gen_mar_ris_chrg_8per) {
		this.r11_gen_mar_ris_chrg_8per = r11_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR11_2per_gen_mar_ris_chrg_div_port() {
		return r11_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR11_2per_gen_mar_ris_chrg_div_port(BigDecimal r11_2per_gen_mar_ris_chrg_div_port) {
		this.r11_2per_gen_mar_ris_chrg_div_port = r11_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR11_tot_gen_mar_risk_chrg() {
		return r11_tot_gen_mar_risk_chrg;
	}
	public void setR11_tot_gen_mar_risk_chrg(BigDecimal r11_tot_gen_mar_risk_chrg) {
		this.r11_tot_gen_mar_risk_chrg = r11_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR11_tot_mar_ris_chrg() {
		return r11_tot_mar_ris_chrg;
	}
	public void setR11_tot_mar_ris_chrg(BigDecimal r11_tot_mar_ris_chrg) {
		this.r11_tot_mar_ris_chrg = r11_tot_mar_ris_chrg;
	}
	public BigDecimal getR12_market() {
		return r12_market;
	}
	public void setR12_market(BigDecimal r12_market) {
		this.r12_market = r12_market;
	}
	public BigDecimal getR12_gpfsr_nom_amt() {
		return r12_gpfsr_nom_amt;
	}
	public void setR12_gpfsr_nom_amt(BigDecimal r12_gpfsr_nom_amt) {
		this.r12_gpfsr_nom_amt = r12_gpfsr_nom_amt;
	}
	public BigDecimal getR12_gpfsr_pos_att8_per_spe_ris() {
		return r12_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att8_per_spe_ris(BigDecimal r12_gpfsr_pos_att8_per_spe_ris) {
		this.r12_gpfsr_pos_att8_per_spe_ris = r12_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg() {
		return r12_gpfsr_chrg;
	}
	public void setR12_gpfsr_chrg(BigDecimal r12_gpfsr_chrg) {
		this.r12_gpfsr_chrg = r12_gpfsr_chrg;
	}
	public BigDecimal getR12_gpfsr_nom_amt1() {
		return r12_gpfsr_nom_amt1;
	}
	public void setR12_gpfsr_nom_amt1(BigDecimal r12_gpfsr_nom_amt1) {
		this.r12_gpfsr_nom_amt1 = r12_gpfsr_nom_amt1;
	}
	public BigDecimal getR12_gpfsr_pos_att4_per_spe_ris() {
		return r12_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att4_per_spe_ris(BigDecimal r12_gpfsr_pos_att4_per_spe_ris) {
		this.r12_gpfsr_pos_att4_per_spe_ris = r12_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg1() {
		return r12_gpfsr_chrg1;
	}
	public void setR12_gpfsr_chrg1(BigDecimal r12_gpfsr_chrg1) {
		this.r12_gpfsr_chrg1 = r12_gpfsr_chrg1;
	}
	public BigDecimal getR12_gpfsr_nom_amt2() {
		return r12_gpfsr_nom_amt2;
	}
	public void setR12_gpfsr_nom_amt2(BigDecimal r12_gpfsr_nom_amt2) {
		this.r12_gpfsr_nom_amt2 = r12_gpfsr_nom_amt2;
	}
	public BigDecimal getR12_gpfsr_pos_att2_per_spe_ris() {
		return r12_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att2_per_spe_ris(BigDecimal r12_gpfsr_pos_att2_per_spe_ris) {
		this.r12_gpfsr_pos_att2_per_spe_ris = r12_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg2() {
		return r12_gpfsr_chrg2;
	}
	public void setR12_gpfsr_chrg2(BigDecimal r12_gpfsr_chrg2) {
		this.r12_gpfsr_chrg2 = r12_gpfsr_chrg2;
	}
	public BigDecimal getR12_tot_spe_ris_chrg() {
		return r12_tot_spe_ris_chrg;
	}
	public void setR12_tot_spe_ris_chrg(BigDecimal r12_tot_spe_ris_chrg) {
		this.r12_tot_spe_ris_chrg = r12_tot_spe_ris_chrg;
	}
	public BigDecimal getR12_net_pos_gen_mar_ris() {
		return r12_net_pos_gen_mar_ris;
	}
	public void setR12_net_pos_gen_mar_ris(BigDecimal r12_net_pos_gen_mar_ris) {
		this.r12_net_pos_gen_mar_ris = r12_net_pos_gen_mar_ris;
	}
	public BigDecimal getR12_gen_mar_ris_chrg_8per() {
		return r12_gen_mar_ris_chrg_8per;
	}
	public void setR12_gen_mar_ris_chrg_8per(BigDecimal r12_gen_mar_ris_chrg_8per) {
		this.r12_gen_mar_ris_chrg_8per = r12_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR12_2per_gen_mar_ris_chrg_div_port() {
		return r12_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR12_2per_gen_mar_ris_chrg_div_port(BigDecimal r12_2per_gen_mar_ris_chrg_div_port) {
		this.r12_2per_gen_mar_ris_chrg_div_port = r12_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR12_tot_gen_mar_risk_chrg() {
		return r12_tot_gen_mar_risk_chrg;
	}
	public void setR12_tot_gen_mar_risk_chrg(BigDecimal r12_tot_gen_mar_risk_chrg) {
		this.r12_tot_gen_mar_risk_chrg = r12_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR12_tot_mar_ris_chrg() {
		return r12_tot_mar_ris_chrg;
	}
	public void setR12_tot_mar_ris_chrg(BigDecimal r12_tot_mar_ris_chrg) {
		this.r12_tot_mar_ris_chrg = r12_tot_mar_ris_chrg;
	}
	public BigDecimal getR13_market() {
		return r13_market;
	}
	public void setR13_market(BigDecimal r13_market) {
		this.r13_market = r13_market;
	}
	public BigDecimal getR13_gpfsr_nom_amt() {
		return r13_gpfsr_nom_amt;
	}
	public void setR13_gpfsr_nom_amt(BigDecimal r13_gpfsr_nom_amt) {
		this.r13_gpfsr_nom_amt = r13_gpfsr_nom_amt;
	}
	public BigDecimal getR13_gpfsr_pos_att8_per_spe_ris() {
		return r13_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att8_per_spe_ris(BigDecimal r13_gpfsr_pos_att8_per_spe_ris) {
		this.r13_gpfsr_pos_att8_per_spe_ris = r13_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg() {
		return r13_gpfsr_chrg;
	}
	public void setR13_gpfsr_chrg(BigDecimal r13_gpfsr_chrg) {
		this.r13_gpfsr_chrg = r13_gpfsr_chrg;
	}
	public BigDecimal getR13_gpfsr_nom_amt1() {
		return r13_gpfsr_nom_amt1;
	}
	public void setR13_gpfsr_nom_amt1(BigDecimal r13_gpfsr_nom_amt1) {
		this.r13_gpfsr_nom_amt1 = r13_gpfsr_nom_amt1;
	}
	public BigDecimal getR13_gpfsr_pos_att4_per_spe_ris() {
		return r13_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att4_per_spe_ris(BigDecimal r13_gpfsr_pos_att4_per_spe_ris) {
		this.r13_gpfsr_pos_att4_per_spe_ris = r13_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg1() {
		return r13_gpfsr_chrg1;
	}
	public void setR13_gpfsr_chrg1(BigDecimal r13_gpfsr_chrg1) {
		this.r13_gpfsr_chrg1 = r13_gpfsr_chrg1;
	}
	public BigDecimal getR13_gpfsr_nom_amt2() {
		return r13_gpfsr_nom_amt2;
	}
	public void setR13_gpfsr_nom_amt2(BigDecimal r13_gpfsr_nom_amt2) {
		this.r13_gpfsr_nom_amt2 = r13_gpfsr_nom_amt2;
	}
	public BigDecimal getR13_gpfsr_pos_att2_per_spe_ris() {
		return r13_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att2_per_spe_ris(BigDecimal r13_gpfsr_pos_att2_per_spe_ris) {
		this.r13_gpfsr_pos_att2_per_spe_ris = r13_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg2() {
		return r13_gpfsr_chrg2;
	}
	public void setR13_gpfsr_chrg2(BigDecimal r13_gpfsr_chrg2) {
		this.r13_gpfsr_chrg2 = r13_gpfsr_chrg2;
	}
	public BigDecimal getR13_tot_spe_ris_chrg() {
		return r13_tot_spe_ris_chrg;
	}
	public void setR13_tot_spe_ris_chrg(BigDecimal r13_tot_spe_ris_chrg) {
		this.r13_tot_spe_ris_chrg = r13_tot_spe_ris_chrg;
	}
	public BigDecimal getR13_net_pos_gen_mar_ris() {
		return r13_net_pos_gen_mar_ris;
	}
	public void setR13_net_pos_gen_mar_ris(BigDecimal r13_net_pos_gen_mar_ris) {
		this.r13_net_pos_gen_mar_ris = r13_net_pos_gen_mar_ris;
	}
	public BigDecimal getR13_gen_mar_ris_chrg_8per() {
		return r13_gen_mar_ris_chrg_8per;
	}
	public void setR13_gen_mar_ris_chrg_8per(BigDecimal r13_gen_mar_ris_chrg_8per) {
		this.r13_gen_mar_ris_chrg_8per = r13_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR13_2per_gen_mar_ris_chrg_div_port() {
		return r13_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR13_2per_gen_mar_ris_chrg_div_port(BigDecimal r13_2per_gen_mar_ris_chrg_div_port) {
		this.r13_2per_gen_mar_ris_chrg_div_port = r13_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR13_tot_gen_mar_risk_chrg() {
		return r13_tot_gen_mar_risk_chrg;
	}
	public void setR13_tot_gen_mar_risk_chrg(BigDecimal r13_tot_gen_mar_risk_chrg) {
		this.r13_tot_gen_mar_risk_chrg = r13_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR13_tot_mar_ris_chrg() {
		return r13_tot_mar_ris_chrg;
	}
	public void setR13_tot_mar_ris_chrg(BigDecimal r13_tot_mar_ris_chrg) {
		this.r13_tot_mar_ris_chrg = r13_tot_mar_ris_chrg;
	}
	public BigDecimal getR14_market() {
		return r14_market;
	}
	public void setR14_market(BigDecimal r14_market) {
		this.r14_market = r14_market;
	}
	public BigDecimal getR14_gpfsr_nom_amt() {
		return r14_gpfsr_nom_amt;
	}
	public void setR14_gpfsr_nom_amt(BigDecimal r14_gpfsr_nom_amt) {
		this.r14_gpfsr_nom_amt = r14_gpfsr_nom_amt;
	}
	public BigDecimal getR14_gpfsr_pos_att8_per_spe_ris() {
		return r14_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att8_per_spe_ris(BigDecimal r14_gpfsr_pos_att8_per_spe_ris) {
		this.r14_gpfsr_pos_att8_per_spe_ris = r14_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg() {
		return r14_gpfsr_chrg;
	}
	public void setR14_gpfsr_chrg(BigDecimal r14_gpfsr_chrg) {
		this.r14_gpfsr_chrg = r14_gpfsr_chrg;
	}
	public BigDecimal getR14_gpfsr_nom_amt1() {
		return r14_gpfsr_nom_amt1;
	}
	public void setR14_gpfsr_nom_amt1(BigDecimal r14_gpfsr_nom_amt1) {
		this.r14_gpfsr_nom_amt1 = r14_gpfsr_nom_amt1;
	}
	public BigDecimal getR14_gpfsr_pos_att4_per_spe_ris() {
		return r14_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att4_per_spe_ris(BigDecimal r14_gpfsr_pos_att4_per_spe_ris) {
		this.r14_gpfsr_pos_att4_per_spe_ris = r14_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg1() {
		return r14_gpfsr_chrg1;
	}
	public void setR14_gpfsr_chrg1(BigDecimal r14_gpfsr_chrg1) {
		this.r14_gpfsr_chrg1 = r14_gpfsr_chrg1;
	}
	public BigDecimal getR14_gpfsr_nom_amt2() {
		return r14_gpfsr_nom_amt2;
	}
	public void setR14_gpfsr_nom_amt2(BigDecimal r14_gpfsr_nom_amt2) {
		this.r14_gpfsr_nom_amt2 = r14_gpfsr_nom_amt2;
	}
	public BigDecimal getR14_gpfsr_pos_att2_per_spe_ris() {
		return r14_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att2_per_spe_ris(BigDecimal r14_gpfsr_pos_att2_per_spe_ris) {
		this.r14_gpfsr_pos_att2_per_spe_ris = r14_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg2() {
		return r14_gpfsr_chrg2;
	}
	public void setR14_gpfsr_chrg2(BigDecimal r14_gpfsr_chrg2) {
		this.r14_gpfsr_chrg2 = r14_gpfsr_chrg2;
	}
	public BigDecimal getR14_tot_spe_ris_chrg() {
		return r14_tot_spe_ris_chrg;
	}
	public void setR14_tot_spe_ris_chrg(BigDecimal r14_tot_spe_ris_chrg) {
		this.r14_tot_spe_ris_chrg = r14_tot_spe_ris_chrg;
	}
	public BigDecimal getR14_net_pos_gen_mar_ris() {
		return r14_net_pos_gen_mar_ris;
	}
	public void setR14_net_pos_gen_mar_ris(BigDecimal r14_net_pos_gen_mar_ris) {
		this.r14_net_pos_gen_mar_ris = r14_net_pos_gen_mar_ris;
	}
	public BigDecimal getR14_gen_mar_ris_chrg_8per() {
		return r14_gen_mar_ris_chrg_8per;
	}
	public void setR14_gen_mar_ris_chrg_8per(BigDecimal r14_gen_mar_ris_chrg_8per) {
		this.r14_gen_mar_ris_chrg_8per = r14_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR14_2per_gen_mar_ris_chrg_div_port() {
		return r14_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR14_2per_gen_mar_ris_chrg_div_port(BigDecimal r14_2per_gen_mar_ris_chrg_div_port) {
		this.r14_2per_gen_mar_ris_chrg_div_port = r14_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR14_tot_gen_mar_risk_chrg() {
		return r14_tot_gen_mar_risk_chrg;
	}
	public void setR14_tot_gen_mar_risk_chrg(BigDecimal r14_tot_gen_mar_risk_chrg) {
		this.r14_tot_gen_mar_risk_chrg = r14_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR14_tot_mar_ris_chrg() {
		return r14_tot_mar_ris_chrg;
	}
	public void setR14_tot_mar_ris_chrg(BigDecimal r14_tot_mar_ris_chrg) {
		this.r14_tot_mar_ris_chrg = r14_tot_mar_ris_chrg;
	}
	public BigDecimal getR15_market() {
		return r15_market;
	}
	public void setR15_market(BigDecimal r15_market) {
		this.r15_market = r15_market;
	}
	public BigDecimal getR15_gpfsr_nom_amt() {
		return r15_gpfsr_nom_amt;
	}
	public void setR15_gpfsr_nom_amt(BigDecimal r15_gpfsr_nom_amt) {
		this.r15_gpfsr_nom_amt = r15_gpfsr_nom_amt;
	}
	public BigDecimal getR15_gpfsr_pos_att8_per_spe_ris() {
		return r15_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att8_per_spe_ris(BigDecimal r15_gpfsr_pos_att8_per_spe_ris) {
		this.r15_gpfsr_pos_att8_per_spe_ris = r15_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg() {
		return r15_gpfsr_chrg;
	}
	public void setR15_gpfsr_chrg(BigDecimal r15_gpfsr_chrg) {
		this.r15_gpfsr_chrg = r15_gpfsr_chrg;
	}
	public BigDecimal getR15_gpfsr_nom_amt1() {
		return r15_gpfsr_nom_amt1;
	}
	public void setR15_gpfsr_nom_amt1(BigDecimal r15_gpfsr_nom_amt1) {
		this.r15_gpfsr_nom_amt1 = r15_gpfsr_nom_amt1;
	}
	public BigDecimal getR15_gpfsr_pos_att4_per_spe_ris() {
		return r15_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att4_per_spe_ris(BigDecimal r15_gpfsr_pos_att4_per_spe_ris) {
		this.r15_gpfsr_pos_att4_per_spe_ris = r15_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg1() {
		return r15_gpfsr_chrg1;
	}
	public void setR15_gpfsr_chrg1(BigDecimal r15_gpfsr_chrg1) {
		this.r15_gpfsr_chrg1 = r15_gpfsr_chrg1;
	}
	public BigDecimal getR15_gpfsr_nom_amt2() {
		return r15_gpfsr_nom_amt2;
	}
	public void setR15_gpfsr_nom_amt2(BigDecimal r15_gpfsr_nom_amt2) {
		this.r15_gpfsr_nom_amt2 = r15_gpfsr_nom_amt2;
	}
	public BigDecimal getR15_gpfsr_pos_att2_per_spe_ris() {
		return r15_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att2_per_spe_ris(BigDecimal r15_gpfsr_pos_att2_per_spe_ris) {
		this.r15_gpfsr_pos_att2_per_spe_ris = r15_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg2() {
		return r15_gpfsr_chrg2;
	}
	public void setR15_gpfsr_chrg2(BigDecimal r15_gpfsr_chrg2) {
		this.r15_gpfsr_chrg2 = r15_gpfsr_chrg2;
	}
	public BigDecimal getR15_tot_spe_ris_chrg() {
		return r15_tot_spe_ris_chrg;
	}
	public void setR15_tot_spe_ris_chrg(BigDecimal r15_tot_spe_ris_chrg) {
		this.r15_tot_spe_ris_chrg = r15_tot_spe_ris_chrg;
	}
	public BigDecimal getR15_net_pos_gen_mar_ris() {
		return r15_net_pos_gen_mar_ris;
	}
	public void setR15_net_pos_gen_mar_ris(BigDecimal r15_net_pos_gen_mar_ris) {
		this.r15_net_pos_gen_mar_ris = r15_net_pos_gen_mar_ris;
	}
	public BigDecimal getR15_gen_mar_ris_chrg_8per() {
		return r15_gen_mar_ris_chrg_8per;
	}
	public void setR15_gen_mar_ris_chrg_8per(BigDecimal r15_gen_mar_ris_chrg_8per) {
		this.r15_gen_mar_ris_chrg_8per = r15_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR15_2per_gen_mar_ris_chrg_div_port() {
		return r15_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR15_2per_gen_mar_ris_chrg_div_port(BigDecimal r15_2per_gen_mar_ris_chrg_div_port) {
		this.r15_2per_gen_mar_ris_chrg_div_port = r15_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR15_tot_gen_mar_risk_chrg() {
		return r15_tot_gen_mar_risk_chrg;
	}
	public void setR15_tot_gen_mar_risk_chrg(BigDecimal r15_tot_gen_mar_risk_chrg) {
		this.r15_tot_gen_mar_risk_chrg = r15_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR15_tot_mar_ris_chrg() {
		return r15_tot_mar_ris_chrg;
	}
	public void setR15_tot_mar_ris_chrg(BigDecimal r15_tot_mar_ris_chrg) {
		this.r15_tot_mar_ris_chrg = r15_tot_mar_ris_chrg;
	}
	public BigDecimal getR16_market() {
		return r16_market;
	}
	public void setR16_market(BigDecimal r16_market) {
		this.r16_market = r16_market;
	}
	public BigDecimal getR16_gpfsr_nom_amt() {
		return r16_gpfsr_nom_amt;
	}
	public void setR16_gpfsr_nom_amt(BigDecimal r16_gpfsr_nom_amt) {
		this.r16_gpfsr_nom_amt = r16_gpfsr_nom_amt;
	}
	public BigDecimal getR16_gpfsr_pos_att8_per_spe_ris() {
		return r16_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att8_per_spe_ris(BigDecimal r16_gpfsr_pos_att8_per_spe_ris) {
		this.r16_gpfsr_pos_att8_per_spe_ris = r16_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg() {
		return r16_gpfsr_chrg;
	}
	public void setR16_gpfsr_chrg(BigDecimal r16_gpfsr_chrg) {
		this.r16_gpfsr_chrg = r16_gpfsr_chrg;
	}
	public BigDecimal getR16_gpfsr_nom_amt1() {
		return r16_gpfsr_nom_amt1;
	}
	public void setR16_gpfsr_nom_amt1(BigDecimal r16_gpfsr_nom_amt1) {
		this.r16_gpfsr_nom_amt1 = r16_gpfsr_nom_amt1;
	}
	public BigDecimal getR16_gpfsr_pos_att4_per_spe_ris() {
		return r16_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att4_per_spe_ris(BigDecimal r16_gpfsr_pos_att4_per_spe_ris) {
		this.r16_gpfsr_pos_att4_per_spe_ris = r16_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg1() {
		return r16_gpfsr_chrg1;
	}
	public void setR16_gpfsr_chrg1(BigDecimal r16_gpfsr_chrg1) {
		this.r16_gpfsr_chrg1 = r16_gpfsr_chrg1;
	}
	public BigDecimal getR16_gpfsr_nom_amt2() {
		return r16_gpfsr_nom_amt2;
	}
	public void setR16_gpfsr_nom_amt2(BigDecimal r16_gpfsr_nom_amt2) {
		this.r16_gpfsr_nom_amt2 = r16_gpfsr_nom_amt2;
	}
	public BigDecimal getR16_gpfsr_pos_att2_per_spe_ris() {
		return r16_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att2_per_spe_ris(BigDecimal r16_gpfsr_pos_att2_per_spe_ris) {
		this.r16_gpfsr_pos_att2_per_spe_ris = r16_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg2() {
		return r16_gpfsr_chrg2;
	}
	public void setR16_gpfsr_chrg2(BigDecimal r16_gpfsr_chrg2) {
		this.r16_gpfsr_chrg2 = r16_gpfsr_chrg2;
	}
	public BigDecimal getR16_tot_spe_ris_chrg() {
		return r16_tot_spe_ris_chrg;
	}
	public void setR16_tot_spe_ris_chrg(BigDecimal r16_tot_spe_ris_chrg) {
		this.r16_tot_spe_ris_chrg = r16_tot_spe_ris_chrg;
	}
	public BigDecimal getR16_net_pos_gen_mar_ris() {
		return r16_net_pos_gen_mar_ris;
	}
	public void setR16_net_pos_gen_mar_ris(BigDecimal r16_net_pos_gen_mar_ris) {
		this.r16_net_pos_gen_mar_ris = r16_net_pos_gen_mar_ris;
	}
	public BigDecimal getR16_gen_mar_ris_chrg_8per() {
		return r16_gen_mar_ris_chrg_8per;
	}
	public void setR16_gen_mar_ris_chrg_8per(BigDecimal r16_gen_mar_ris_chrg_8per) {
		this.r16_gen_mar_ris_chrg_8per = r16_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR16_2per_gen_mar_ris_chrg_div_port() {
		return r16_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR16_2per_gen_mar_ris_chrg_div_port(BigDecimal r16_2per_gen_mar_ris_chrg_div_port) {
		this.r16_2per_gen_mar_ris_chrg_div_port = r16_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR16_tot_gen_mar_risk_chrg() {
		return r16_tot_gen_mar_risk_chrg;
	}
	public void setR16_tot_gen_mar_risk_chrg(BigDecimal r16_tot_gen_mar_risk_chrg) {
		this.r16_tot_gen_mar_risk_chrg = r16_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR16_tot_mar_ris_chrg() {
		return r16_tot_mar_ris_chrg;
	}
	public void setR16_tot_mar_ris_chrg(BigDecimal r16_tot_mar_ris_chrg) {
		this.r16_tot_mar_ris_chrg = r16_tot_mar_ris_chrg;
	}
	public BigDecimal getR17_market() {
		return r17_market;
	}
	public void setR17_market(BigDecimal r17_market) {
		this.r17_market = r17_market;
	}
	public BigDecimal getR17_gpfsr_nom_amt() {
		return r17_gpfsr_nom_amt;
	}
	public void setR17_gpfsr_nom_amt(BigDecimal r17_gpfsr_nom_amt) {
		this.r17_gpfsr_nom_amt = r17_gpfsr_nom_amt;
	}
	public BigDecimal getR17_gpfsr_pos_att8_per_spe_ris() {
		return r17_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att8_per_spe_ris(BigDecimal r17_gpfsr_pos_att8_per_spe_ris) {
		this.r17_gpfsr_pos_att8_per_spe_ris = r17_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg() {
		return r17_gpfsr_chrg;
	}
	public void setR17_gpfsr_chrg(BigDecimal r17_gpfsr_chrg) {
		this.r17_gpfsr_chrg = r17_gpfsr_chrg;
	}
	public BigDecimal getR17_gpfsr_nom_amt1() {
		return r17_gpfsr_nom_amt1;
	}
	public void setR17_gpfsr_nom_amt1(BigDecimal r17_gpfsr_nom_amt1) {
		this.r17_gpfsr_nom_amt1 = r17_gpfsr_nom_amt1;
	}
	public BigDecimal getR17_gpfsr_pos_att4_per_spe_ris() {
		return r17_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att4_per_spe_ris(BigDecimal r17_gpfsr_pos_att4_per_spe_ris) {
		this.r17_gpfsr_pos_att4_per_spe_ris = r17_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg1() {
		return r17_gpfsr_chrg1;
	}
	public void setR17_gpfsr_chrg1(BigDecimal r17_gpfsr_chrg1) {
		this.r17_gpfsr_chrg1 = r17_gpfsr_chrg1;
	}
	public BigDecimal getR17_gpfsr_nom_amt2() {
		return r17_gpfsr_nom_amt2;
	}
	public void setR17_gpfsr_nom_amt2(BigDecimal r17_gpfsr_nom_amt2) {
		this.r17_gpfsr_nom_amt2 = r17_gpfsr_nom_amt2;
	}
	public BigDecimal getR17_gpfsr_pos_att2_per_spe_ris() {
		return r17_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att2_per_spe_ris(BigDecimal r17_gpfsr_pos_att2_per_spe_ris) {
		this.r17_gpfsr_pos_att2_per_spe_ris = r17_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg2() {
		return r17_gpfsr_chrg2;
	}
	public void setR17_gpfsr_chrg2(BigDecimal r17_gpfsr_chrg2) {
		this.r17_gpfsr_chrg2 = r17_gpfsr_chrg2;
	}
	public BigDecimal getR17_tot_spe_ris_chrg() {
		return r17_tot_spe_ris_chrg;
	}
	public void setR17_tot_spe_ris_chrg(BigDecimal r17_tot_spe_ris_chrg) {
		this.r17_tot_spe_ris_chrg = r17_tot_spe_ris_chrg;
	}
	public BigDecimal getR17_net_pos_gen_mar_ris() {
		return r17_net_pos_gen_mar_ris;
	}
	public void setR17_net_pos_gen_mar_ris(BigDecimal r17_net_pos_gen_mar_ris) {
		this.r17_net_pos_gen_mar_ris = r17_net_pos_gen_mar_ris;
	}
	public BigDecimal getR17_gen_mar_ris_chrg_8per() {
		return r17_gen_mar_ris_chrg_8per;
	}
	public void setR17_gen_mar_ris_chrg_8per(BigDecimal r17_gen_mar_ris_chrg_8per) {
		this.r17_gen_mar_ris_chrg_8per = r17_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR17_2per_gen_mar_ris_chrg_div_port() {
		return r17_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR17_2per_gen_mar_ris_chrg_div_port(BigDecimal r17_2per_gen_mar_ris_chrg_div_port) {
		this.r17_2per_gen_mar_ris_chrg_div_port = r17_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR17_tot_gen_mar_risk_chrg() {
		return r17_tot_gen_mar_risk_chrg;
	}
	public void setR17_tot_gen_mar_risk_chrg(BigDecimal r17_tot_gen_mar_risk_chrg) {
		this.r17_tot_gen_mar_risk_chrg = r17_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR17_tot_mar_ris_chrg() {
		return r17_tot_mar_ris_chrg;
	}
	public void setR17_tot_mar_ris_chrg(BigDecimal r17_tot_mar_ris_chrg) {
		this.r17_tot_mar_ris_chrg = r17_tot_mar_ris_chrg;
	}
	public BigDecimal getR18_market() {
		return r18_market;
	}
	public void setR18_market(BigDecimal r18_market) {
		this.r18_market = r18_market;
	}
	public BigDecimal getR18_gpfsr_nom_amt() {
		return r18_gpfsr_nom_amt;
	}
	public void setR18_gpfsr_nom_amt(BigDecimal r18_gpfsr_nom_amt) {
		this.r18_gpfsr_nom_amt = r18_gpfsr_nom_amt;
	}
	public BigDecimal getR18_gpfsr_pos_att8_per_spe_ris() {
		return r18_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att8_per_spe_ris(BigDecimal r18_gpfsr_pos_att8_per_spe_ris) {
		this.r18_gpfsr_pos_att8_per_spe_ris = r18_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg() {
		return r18_gpfsr_chrg;
	}
	public void setR18_gpfsr_chrg(BigDecimal r18_gpfsr_chrg) {
		this.r18_gpfsr_chrg = r18_gpfsr_chrg;
	}
	public BigDecimal getR18_gpfsr_nom_amt1() {
		return r18_gpfsr_nom_amt1;
	}
	public void setR18_gpfsr_nom_amt1(BigDecimal r18_gpfsr_nom_amt1) {
		this.r18_gpfsr_nom_amt1 = r18_gpfsr_nom_amt1;
	}
	public BigDecimal getR18_gpfsr_pos_att4_per_spe_ris() {
		return r18_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att4_per_spe_ris(BigDecimal r18_gpfsr_pos_att4_per_spe_ris) {
		this.r18_gpfsr_pos_att4_per_spe_ris = r18_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg1() {
		return r18_gpfsr_chrg1;
	}
	public void setR18_gpfsr_chrg1(BigDecimal r18_gpfsr_chrg1) {
		this.r18_gpfsr_chrg1 = r18_gpfsr_chrg1;
	}
	public BigDecimal getR18_gpfsr_nom_amt2() {
		return r18_gpfsr_nom_amt2;
	}
	public void setR18_gpfsr_nom_amt2(BigDecimal r18_gpfsr_nom_amt2) {
		this.r18_gpfsr_nom_amt2 = r18_gpfsr_nom_amt2;
	}
	public BigDecimal getR18_gpfsr_pos_att2_per_spe_ris() {
		return r18_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att2_per_spe_ris(BigDecimal r18_gpfsr_pos_att2_per_spe_ris) {
		this.r18_gpfsr_pos_att2_per_spe_ris = r18_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg2() {
		return r18_gpfsr_chrg2;
	}
	public void setR18_gpfsr_chrg2(BigDecimal r18_gpfsr_chrg2) {
		this.r18_gpfsr_chrg2 = r18_gpfsr_chrg2;
	}
	public BigDecimal getR18_tot_spe_ris_chrg() {
		return r18_tot_spe_ris_chrg;
	}
	public void setR18_tot_spe_ris_chrg(BigDecimal r18_tot_spe_ris_chrg) {
		this.r18_tot_spe_ris_chrg = r18_tot_spe_ris_chrg;
	}
	public BigDecimal getR18_net_pos_gen_mar_ris() {
		return r18_net_pos_gen_mar_ris;
	}
	public void setR18_net_pos_gen_mar_ris(BigDecimal r18_net_pos_gen_mar_ris) {
		this.r18_net_pos_gen_mar_ris = r18_net_pos_gen_mar_ris;
	}
	public BigDecimal getR18_gen_mar_ris_chrg_8per() {
		return r18_gen_mar_ris_chrg_8per;
	}
	public void setR18_gen_mar_ris_chrg_8per(BigDecimal r18_gen_mar_ris_chrg_8per) {
		this.r18_gen_mar_ris_chrg_8per = r18_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR18_2per_gen_mar_ris_chrg_div_port() {
		return r18_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR18_2per_gen_mar_ris_chrg_div_port(BigDecimal r18_2per_gen_mar_ris_chrg_div_port) {
		this.r18_2per_gen_mar_ris_chrg_div_port = r18_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR18_tot_gen_mar_risk_chrg() {
		return r18_tot_gen_mar_risk_chrg;
	}
	public void setR18_tot_gen_mar_risk_chrg(BigDecimal r18_tot_gen_mar_risk_chrg) {
		this.r18_tot_gen_mar_risk_chrg = r18_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR18_tot_mar_ris_chrg() {
		return r18_tot_mar_ris_chrg;
	}
	public void setR18_tot_mar_ris_chrg(BigDecimal r18_tot_mar_ris_chrg) {
		this.r18_tot_mar_ris_chrg = r18_tot_mar_ris_chrg;
	}
	public BigDecimal getR19_market() {
		return r19_market;
	}
	public void setR19_market(BigDecimal r19_market) {
		this.r19_market = r19_market;
	}
	public BigDecimal getR19_gpfsr_nom_amt() {
		return r19_gpfsr_nom_amt;
	}
	public void setR19_gpfsr_nom_amt(BigDecimal r19_gpfsr_nom_amt) {
		this.r19_gpfsr_nom_amt = r19_gpfsr_nom_amt;
	}
	public BigDecimal getR19_gpfsr_pos_att8_per_spe_ris() {
		return r19_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att8_per_spe_ris(BigDecimal r19_gpfsr_pos_att8_per_spe_ris) {
		this.r19_gpfsr_pos_att8_per_spe_ris = r19_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg() {
		return r19_gpfsr_chrg;
	}
	public void setR19_gpfsr_chrg(BigDecimal r19_gpfsr_chrg) {
		this.r19_gpfsr_chrg = r19_gpfsr_chrg;
	}
	public BigDecimal getR19_gpfsr_nom_amt1() {
		return r19_gpfsr_nom_amt1;
	}
	public void setR19_gpfsr_nom_amt1(BigDecimal r19_gpfsr_nom_amt1) {
		this.r19_gpfsr_nom_amt1 = r19_gpfsr_nom_amt1;
	}
	public BigDecimal getR19_gpfsr_pos_att4_per_spe_ris() {
		return r19_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att4_per_spe_ris(BigDecimal r19_gpfsr_pos_att4_per_spe_ris) {
		this.r19_gpfsr_pos_att4_per_spe_ris = r19_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg1() {
		return r19_gpfsr_chrg1;
	}
	public void setR19_gpfsr_chrg1(BigDecimal r19_gpfsr_chrg1) {
		this.r19_gpfsr_chrg1 = r19_gpfsr_chrg1;
	}
	public BigDecimal getR19_gpfsr_nom_amt2() {
		return r19_gpfsr_nom_amt2;
	}
	public void setR19_gpfsr_nom_amt2(BigDecimal r19_gpfsr_nom_amt2) {
		this.r19_gpfsr_nom_amt2 = r19_gpfsr_nom_amt2;
	}
	public BigDecimal getR19_gpfsr_pos_att2_per_spe_ris() {
		return r19_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att2_per_spe_ris(BigDecimal r19_gpfsr_pos_att2_per_spe_ris) {
		this.r19_gpfsr_pos_att2_per_spe_ris = r19_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg2() {
		return r19_gpfsr_chrg2;
	}
	public void setR19_gpfsr_chrg2(BigDecimal r19_gpfsr_chrg2) {
		this.r19_gpfsr_chrg2 = r19_gpfsr_chrg2;
	}
	public BigDecimal getR19_tot_spe_ris_chrg() {
		return r19_tot_spe_ris_chrg;
	}
	public void setR19_tot_spe_ris_chrg(BigDecimal r19_tot_spe_ris_chrg) {
		this.r19_tot_spe_ris_chrg = r19_tot_spe_ris_chrg;
	}
	public BigDecimal getR19_net_pos_gen_mar_ris() {
		return r19_net_pos_gen_mar_ris;
	}
	public void setR19_net_pos_gen_mar_ris(BigDecimal r19_net_pos_gen_mar_ris) {
		this.r19_net_pos_gen_mar_ris = r19_net_pos_gen_mar_ris;
	}
	public BigDecimal getR19_gen_mar_ris_chrg_8per() {
		return r19_gen_mar_ris_chrg_8per;
	}
	public void setR19_gen_mar_ris_chrg_8per(BigDecimal r19_gen_mar_ris_chrg_8per) {
		this.r19_gen_mar_ris_chrg_8per = r19_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR19_2per_gen_mar_ris_chrg_div_port() {
		return r19_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR19_2per_gen_mar_ris_chrg_div_port(BigDecimal r19_2per_gen_mar_ris_chrg_div_port) {
		this.r19_2per_gen_mar_ris_chrg_div_port = r19_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR19_tot_gen_mar_risk_chrg() {
		return r19_tot_gen_mar_risk_chrg;
	}
	public void setR19_tot_gen_mar_risk_chrg(BigDecimal r19_tot_gen_mar_risk_chrg) {
		this.r19_tot_gen_mar_risk_chrg = r19_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR19_tot_mar_ris_chrg() {
		return r19_tot_mar_ris_chrg;
	}
	public void setR19_tot_mar_ris_chrg(BigDecimal r19_tot_mar_ris_chrg) {
		this.r19_tot_mar_ris_chrg = r19_tot_mar_ris_chrg;
	}
	public BigDecimal getR20_market() {
		return r20_market;
	}
	public void setR20_market(BigDecimal r20_market) {
		this.r20_market = r20_market;
	}
	public BigDecimal getR20_gpfsr_nom_amt() {
		return r20_gpfsr_nom_amt;
	}
	public void setR20_gpfsr_nom_amt(BigDecimal r20_gpfsr_nom_amt) {
		this.r20_gpfsr_nom_amt = r20_gpfsr_nom_amt;
	}
	public BigDecimal getR20_gpfsr_pos_att8_per_spe_ris() {
		return r20_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att8_per_spe_ris(BigDecimal r20_gpfsr_pos_att8_per_spe_ris) {
		this.r20_gpfsr_pos_att8_per_spe_ris = r20_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg() {
		return r20_gpfsr_chrg;
	}
	public void setR20_gpfsr_chrg(BigDecimal r20_gpfsr_chrg) {
		this.r20_gpfsr_chrg = r20_gpfsr_chrg;
	}
	public BigDecimal getR20_gpfsr_nom_amt1() {
		return r20_gpfsr_nom_amt1;
	}
	public void setR20_gpfsr_nom_amt1(BigDecimal r20_gpfsr_nom_amt1) {
		this.r20_gpfsr_nom_amt1 = r20_gpfsr_nom_amt1;
	}
	public BigDecimal getR20_gpfsr_pos_att4_per_spe_ris() {
		return r20_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att4_per_spe_ris(BigDecimal r20_gpfsr_pos_att4_per_spe_ris) {
		this.r20_gpfsr_pos_att4_per_spe_ris = r20_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg1() {
		return r20_gpfsr_chrg1;
	}
	public void setR20_gpfsr_chrg1(BigDecimal r20_gpfsr_chrg1) {
		this.r20_gpfsr_chrg1 = r20_gpfsr_chrg1;
	}
	public BigDecimal getR20_gpfsr_nom_amt2() {
		return r20_gpfsr_nom_amt2;
	}
	public void setR20_gpfsr_nom_amt2(BigDecimal r20_gpfsr_nom_amt2) {
		this.r20_gpfsr_nom_amt2 = r20_gpfsr_nom_amt2;
	}
	public BigDecimal getR20_gpfsr_pos_att2_per_spe_ris() {
		return r20_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att2_per_spe_ris(BigDecimal r20_gpfsr_pos_att2_per_spe_ris) {
		this.r20_gpfsr_pos_att2_per_spe_ris = r20_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg2() {
		return r20_gpfsr_chrg2;
	}
	public void setR20_gpfsr_chrg2(BigDecimal r20_gpfsr_chrg2) {
		this.r20_gpfsr_chrg2 = r20_gpfsr_chrg2;
	}
	public BigDecimal getR20_tot_spe_ris_chrg() {
		return r20_tot_spe_ris_chrg;
	}
	public void setR20_tot_spe_ris_chrg(BigDecimal r20_tot_spe_ris_chrg) {
		this.r20_tot_spe_ris_chrg = r20_tot_spe_ris_chrg;
	}
	public BigDecimal getR20_net_pos_gen_mar_ris() {
		return r20_net_pos_gen_mar_ris;
	}
	public void setR20_net_pos_gen_mar_ris(BigDecimal r20_net_pos_gen_mar_ris) {
		this.r20_net_pos_gen_mar_ris = r20_net_pos_gen_mar_ris;
	}
	public BigDecimal getR20_gen_mar_ris_chrg_8per() {
		return r20_gen_mar_ris_chrg_8per;
	}
	public void setR20_gen_mar_ris_chrg_8per(BigDecimal r20_gen_mar_ris_chrg_8per) {
		this.r20_gen_mar_ris_chrg_8per = r20_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR20_2per_gen_mar_ris_chrg_div_port() {
		return r20_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR20_2per_gen_mar_ris_chrg_div_port(BigDecimal r20_2per_gen_mar_ris_chrg_div_port) {
		this.r20_2per_gen_mar_ris_chrg_div_port = r20_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR20_tot_gen_mar_risk_chrg() {
		return r20_tot_gen_mar_risk_chrg;
	}
	public void setR20_tot_gen_mar_risk_chrg(BigDecimal r20_tot_gen_mar_risk_chrg) {
		this.r20_tot_gen_mar_risk_chrg = r20_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR20_tot_mar_ris_chrg() {
		return r20_tot_mar_ris_chrg;
	}
	public void setR20_tot_mar_ris_chrg(BigDecimal r20_tot_mar_ris_chrg) {
		this.r20_tot_mar_ris_chrg = r20_tot_mar_ris_chrg;
	}
	public BigDecimal getR21_market() {
		return r21_market;
	}
	public void setR21_market(BigDecimal r21_market) {
		this.r21_market = r21_market;
	}
	public BigDecimal getR21_gpfsr_nom_amt() {
		return r21_gpfsr_nom_amt;
	}
	public void setR21_gpfsr_nom_amt(BigDecimal r21_gpfsr_nom_amt) {
		this.r21_gpfsr_nom_amt = r21_gpfsr_nom_amt;
	}
	public BigDecimal getR21_gpfsr_pos_att8_per_spe_ris() {
		return r21_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att8_per_spe_ris(BigDecimal r21_gpfsr_pos_att8_per_spe_ris) {
		this.r21_gpfsr_pos_att8_per_spe_ris = r21_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg() {
		return r21_gpfsr_chrg;
	}
	public void setR21_gpfsr_chrg(BigDecimal r21_gpfsr_chrg) {
		this.r21_gpfsr_chrg = r21_gpfsr_chrg;
	}
	public BigDecimal getR21_gpfsr_nom_amt1() {
		return r21_gpfsr_nom_amt1;
	}
	public void setR21_gpfsr_nom_amt1(BigDecimal r21_gpfsr_nom_amt1) {
		this.r21_gpfsr_nom_amt1 = r21_gpfsr_nom_amt1;
	}
	public BigDecimal getR21_gpfsr_pos_att4_per_spe_ris() {
		return r21_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att4_per_spe_ris(BigDecimal r21_gpfsr_pos_att4_per_spe_ris) {
		this.r21_gpfsr_pos_att4_per_spe_ris = r21_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg1() {
		return r21_gpfsr_chrg1;
	}
	public void setR21_gpfsr_chrg1(BigDecimal r21_gpfsr_chrg1) {
		this.r21_gpfsr_chrg1 = r21_gpfsr_chrg1;
	}
	public BigDecimal getR21_gpfsr_nom_amt2() {
		return r21_gpfsr_nom_amt2;
	}
	public void setR21_gpfsr_nom_amt2(BigDecimal r21_gpfsr_nom_amt2) {
		this.r21_gpfsr_nom_amt2 = r21_gpfsr_nom_amt2;
	}
	public BigDecimal getR21_gpfsr_pos_att2_per_spe_ris() {
		return r21_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att2_per_spe_ris(BigDecimal r21_gpfsr_pos_att2_per_spe_ris) {
		this.r21_gpfsr_pos_att2_per_spe_ris = r21_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg2() {
		return r21_gpfsr_chrg2;
	}
	public void setR21_gpfsr_chrg2(BigDecimal r21_gpfsr_chrg2) {
		this.r21_gpfsr_chrg2 = r21_gpfsr_chrg2;
	}
	public BigDecimal getR21_tot_spe_ris_chrg() {
		return r21_tot_spe_ris_chrg;
	}
	public void setR21_tot_spe_ris_chrg(BigDecimal r21_tot_spe_ris_chrg) {
		this.r21_tot_spe_ris_chrg = r21_tot_spe_ris_chrg;
	}
	public BigDecimal getR21_net_pos_gen_mar_ris() {
		return r21_net_pos_gen_mar_ris;
	}
	public void setR21_net_pos_gen_mar_ris(BigDecimal r21_net_pos_gen_mar_ris) {
		this.r21_net_pos_gen_mar_ris = r21_net_pos_gen_mar_ris;
	}
	public BigDecimal getR21_gen_mar_ris_chrg_8per() {
		return r21_gen_mar_ris_chrg_8per;
	}
	public void setR21_gen_mar_ris_chrg_8per(BigDecimal r21_gen_mar_ris_chrg_8per) {
		this.r21_gen_mar_ris_chrg_8per = r21_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR21_2per_gen_mar_ris_chrg_div_port() {
		return r21_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR21_2per_gen_mar_ris_chrg_div_port(BigDecimal r21_2per_gen_mar_ris_chrg_div_port) {
		this.r21_2per_gen_mar_ris_chrg_div_port = r21_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR21_tot_gen_mar_risk_chrg() {
		return r21_tot_gen_mar_risk_chrg;
	}
	public void setR21_tot_gen_mar_risk_chrg(BigDecimal r21_tot_gen_mar_risk_chrg) {
		this.r21_tot_gen_mar_risk_chrg = r21_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR21_tot_mar_ris_chrg() {
		return r21_tot_mar_ris_chrg;
	}
	public void setR21_tot_mar_ris_chrg(BigDecimal r21_tot_mar_ris_chrg) {
		this.r21_tot_mar_ris_chrg = r21_tot_mar_ris_chrg;
	}
	public BigDecimal getR22_market() {
		return r22_market;
	}
	public void setR22_market(BigDecimal r22_market) {
		this.r22_market = r22_market;
	}
	public BigDecimal getR22_gpfsr_nom_amt() {
		return r22_gpfsr_nom_amt;
	}
	public void setR22_gpfsr_nom_amt(BigDecimal r22_gpfsr_nom_amt) {
		this.r22_gpfsr_nom_amt = r22_gpfsr_nom_amt;
	}
	public BigDecimal getR22_gpfsr_pos_att8_per_spe_ris() {
		return r22_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att8_per_spe_ris(BigDecimal r22_gpfsr_pos_att8_per_spe_ris) {
		this.r22_gpfsr_pos_att8_per_spe_ris = r22_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg() {
		return r22_gpfsr_chrg;
	}
	public void setR22_gpfsr_chrg(BigDecimal r22_gpfsr_chrg) {
		this.r22_gpfsr_chrg = r22_gpfsr_chrg;
	}
	public BigDecimal getR22_gpfsr_nom_amt1() {
		return r22_gpfsr_nom_amt1;
	}
	public void setR22_gpfsr_nom_amt1(BigDecimal r22_gpfsr_nom_amt1) {
		this.r22_gpfsr_nom_amt1 = r22_gpfsr_nom_amt1;
	}
	public BigDecimal getR22_gpfsr_pos_att4_per_spe_ris() {
		return r22_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att4_per_spe_ris(BigDecimal r22_gpfsr_pos_att4_per_spe_ris) {
		this.r22_gpfsr_pos_att4_per_spe_ris = r22_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg1() {
		return r22_gpfsr_chrg1;
	}
	public void setR22_gpfsr_chrg1(BigDecimal r22_gpfsr_chrg1) {
		this.r22_gpfsr_chrg1 = r22_gpfsr_chrg1;
	}
	public BigDecimal getR22_gpfsr_nom_amt2() {
		return r22_gpfsr_nom_amt2;
	}
	public void setR22_gpfsr_nom_amt2(BigDecimal r22_gpfsr_nom_amt2) {
		this.r22_gpfsr_nom_amt2 = r22_gpfsr_nom_amt2;
	}
	public BigDecimal getR22_gpfsr_pos_att2_per_spe_ris() {
		return r22_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att2_per_spe_ris(BigDecimal r22_gpfsr_pos_att2_per_spe_ris) {
		this.r22_gpfsr_pos_att2_per_spe_ris = r22_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg2() {
		return r22_gpfsr_chrg2;
	}
	public void setR22_gpfsr_chrg2(BigDecimal r22_gpfsr_chrg2) {
		this.r22_gpfsr_chrg2 = r22_gpfsr_chrg2;
	}
	public BigDecimal getR22_tot_spe_ris_chrg() {
		return r22_tot_spe_ris_chrg;
	}
	public void setR22_tot_spe_ris_chrg(BigDecimal r22_tot_spe_ris_chrg) {
		this.r22_tot_spe_ris_chrg = r22_tot_spe_ris_chrg;
	}
	public BigDecimal getR22_net_pos_gen_mar_ris() {
		return r22_net_pos_gen_mar_ris;
	}
	public void setR22_net_pos_gen_mar_ris(BigDecimal r22_net_pos_gen_mar_ris) {
		this.r22_net_pos_gen_mar_ris = r22_net_pos_gen_mar_ris;
	}
	public BigDecimal getR22_gen_mar_ris_chrg_8per() {
		return r22_gen_mar_ris_chrg_8per;
	}
	public void setR22_gen_mar_ris_chrg_8per(BigDecimal r22_gen_mar_ris_chrg_8per) {
		this.r22_gen_mar_ris_chrg_8per = r22_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR22_2per_gen_mar_ris_chrg_div_port() {
		return r22_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR22_2per_gen_mar_ris_chrg_div_port(BigDecimal r22_2per_gen_mar_ris_chrg_div_port) {
		this.r22_2per_gen_mar_ris_chrg_div_port = r22_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR22_tot_gen_mar_risk_chrg() {
		return r22_tot_gen_mar_risk_chrg;
	}
	public void setR22_tot_gen_mar_risk_chrg(BigDecimal r22_tot_gen_mar_risk_chrg) {
		this.r22_tot_gen_mar_risk_chrg = r22_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR22_tot_mar_ris_chrg() {
		return r22_tot_mar_ris_chrg;
	}
	public void setR22_tot_mar_ris_chrg(BigDecimal r22_tot_mar_ris_chrg) {
		this.r22_tot_mar_ris_chrg = r22_tot_mar_ris_chrg;
	}
	public String getR23_market() {
		return r23_market;
	}
	public void setR23_market(String r23_market) {
		this.r23_market = r23_market;
	}
	public BigDecimal getR23_gpfsr_nom_amt() {
		return r23_gpfsr_nom_amt;
	}
	public void setR23_gpfsr_nom_amt(BigDecimal r23_gpfsr_nom_amt) {
		this.r23_gpfsr_nom_amt = r23_gpfsr_nom_amt;
	}
	public BigDecimal getR23_gpfsr_pos_att8_per_spe_ris() {
		return r23_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att8_per_spe_ris(BigDecimal r23_gpfsr_pos_att8_per_spe_ris) {
		this.r23_gpfsr_pos_att8_per_spe_ris = r23_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg() {
		return r23_gpfsr_chrg;
	}
	public void setR23_gpfsr_chrg(BigDecimal r23_gpfsr_chrg) {
		this.r23_gpfsr_chrg = r23_gpfsr_chrg;
	}
	public BigDecimal getR23_gpfsr_nom_amt1() {
		return r23_gpfsr_nom_amt1;
	}
	public void setR23_gpfsr_nom_amt1(BigDecimal r23_gpfsr_nom_amt1) {
		this.r23_gpfsr_nom_amt1 = r23_gpfsr_nom_amt1;
	}
	public BigDecimal getR23_gpfsr_pos_att4_per_spe_ris() {
		return r23_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att4_per_spe_ris(BigDecimal r23_gpfsr_pos_att4_per_spe_ris) {
		this.r23_gpfsr_pos_att4_per_spe_ris = r23_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg1() {
		return r23_gpfsr_chrg1;
	}
	public void setR23_gpfsr_chrg1(BigDecimal r23_gpfsr_chrg1) {
		this.r23_gpfsr_chrg1 = r23_gpfsr_chrg1;
	}
	public BigDecimal getR23_gpfsr_nom_amt2() {
		return r23_gpfsr_nom_amt2;
	}
	public void setR23_gpfsr_nom_amt2(BigDecimal r23_gpfsr_nom_amt2) {
		this.r23_gpfsr_nom_amt2 = r23_gpfsr_nom_amt2;
	}
	public BigDecimal getR23_gpfsr_pos_att2_per_spe_ris() {
		return r23_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att2_per_spe_ris(BigDecimal r23_gpfsr_pos_att2_per_spe_ris) {
		this.r23_gpfsr_pos_att2_per_spe_ris = r23_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg2() {
		return r23_gpfsr_chrg2;
	}
	public void setR23_gpfsr_chrg2(BigDecimal r23_gpfsr_chrg2) {
		this.r23_gpfsr_chrg2 = r23_gpfsr_chrg2;
	}
	public BigDecimal getR23_tot_spe_ris_chrg() {
		return r23_tot_spe_ris_chrg;
	}
	public void setR23_tot_spe_ris_chrg(BigDecimal r23_tot_spe_ris_chrg) {
		this.r23_tot_spe_ris_chrg = r23_tot_spe_ris_chrg;
	}
	public BigDecimal getR23_net_pos_gen_mar_ris() {
		return r23_net_pos_gen_mar_ris;
	}
	public void setR23_net_pos_gen_mar_ris(BigDecimal r23_net_pos_gen_mar_ris) {
		this.r23_net_pos_gen_mar_ris = r23_net_pos_gen_mar_ris;
	}
	public BigDecimal getR23_gen_mar_ris_chrg_8per() {
		return r23_gen_mar_ris_chrg_8per;
	}
	public void setR23_gen_mar_ris_chrg_8per(BigDecimal r23_gen_mar_ris_chrg_8per) {
		this.r23_gen_mar_ris_chrg_8per = r23_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR23_2per_gen_mar_ris_chrg_div_port() {
		return r23_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR23_2per_gen_mar_ris_chrg_div_port(BigDecimal r23_2per_gen_mar_ris_chrg_div_port) {
		this.r23_2per_gen_mar_ris_chrg_div_port = r23_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR23_tot_gen_mar_risk_chrg() {
		return r23_tot_gen_mar_risk_chrg;
	}
	public void setR23_tot_gen_mar_risk_chrg(BigDecimal r23_tot_gen_mar_risk_chrg) {
		this.r23_tot_gen_mar_risk_chrg = r23_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR23_tot_mar_ris_chrg() {
		return r23_tot_mar_ris_chrg;
	}
	public void setR23_tot_mar_ris_chrg(BigDecimal r23_tot_mar_ris_chrg) {
		this.r23_tot_mar_ris_chrg = r23_tot_mar_ris_chrg;
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
// DETAIL ENTITY  M_EPR
// =====================================================	

public class M_EPR_Detail_RowMapper implements RowMapper<M_EPR_Detail_Entity> {

    @Override
    public M_EPR_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_EPR_Detail_Entity obj = new M_EPR_Detail_Entity();

// =========================
// R11
// =========================
obj.setR11_market(rs.getBigDecimal("r11_market"));
obj.setR11_gpfsr_nom_amt(rs.getBigDecimal("r11_gpfsr_nom_amt"));
obj.setR11_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att8_per_spe_ris"));
obj.setR11_gpfsr_chrg(rs.getBigDecimal("r11_gpfsr_chrg"));
obj.setR11_gpfsr_nom_amt1(rs.getBigDecimal("r11_gpfsr_nom_amt1"));
obj.setR11_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att4_per_spe_ris"));
obj.setR11_gpfsr_chrg1(rs.getBigDecimal("r11_gpfsr_chrg1"));
obj.setR11_gpfsr_nom_amt2(rs.getBigDecimal("r11_gpfsr_nom_amt2"));
obj.setR11_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att2_per_spe_ris"));
obj.setR11_gpfsr_chrg2(rs.getBigDecimal("r11_gpfsr_chrg2"));
obj.setR11_tot_spe_ris_chrg(rs.getBigDecimal("r11_tot_spe_ris_chrg"));
obj.setR11_net_pos_gen_mar_ris(rs.getBigDecimal("r11_net_pos_gen_mar_ris"));
obj.setR11_gen_mar_ris_chrg_8per(rs.getBigDecimal("r11_gen_mar_ris_chrg_8per"));
obj.setR11_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r11_2per_gen_mar_ris_chrg_div_port"));
obj.setR11_tot_gen_mar_risk_chrg(rs.getBigDecimal("r11_tot_gen_mar_risk_chrg"));
obj.setR11_tot_mar_ris_chrg(rs.getBigDecimal("r11_tot_mar_ris_chrg"));

// =========================
// R12
// =========================
obj.setR12_market(rs.getBigDecimal("r12_market"));
obj.setR12_gpfsr_nom_amt(rs.getBigDecimal("r12_gpfsr_nom_amt"));
obj.setR12_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att8_per_spe_ris"));
obj.setR12_gpfsr_chrg(rs.getBigDecimal("r12_gpfsr_chrg"));
obj.setR12_gpfsr_nom_amt1(rs.getBigDecimal("r12_gpfsr_nom_amt1"));
obj.setR12_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att4_per_spe_ris"));
obj.setR12_gpfsr_chrg1(rs.getBigDecimal("r12_gpfsr_chrg1"));
obj.setR12_gpfsr_nom_amt2(rs.getBigDecimal("r12_gpfsr_nom_amt2"));
obj.setR12_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att2_per_spe_ris"));
obj.setR12_gpfsr_chrg2(rs.getBigDecimal("r12_gpfsr_chrg2"));
obj.setR12_tot_spe_ris_chrg(rs.getBigDecimal("r12_tot_spe_ris_chrg"));
obj.setR12_net_pos_gen_mar_ris(rs.getBigDecimal("r12_net_pos_gen_mar_ris"));
obj.setR12_gen_mar_ris_chrg_8per(rs.getBigDecimal("r12_gen_mar_ris_chrg_8per"));
obj.setR12_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r12_2per_gen_mar_ris_chrg_div_port"));
obj.setR12_tot_gen_mar_risk_chrg(rs.getBigDecimal("r12_tot_gen_mar_risk_chrg"));
obj.setR12_tot_mar_ris_chrg(rs.getBigDecimal("r12_tot_mar_ris_chrg"));

// =========================
// R13
// =========================
obj.setR13_market(rs.getBigDecimal("r13_market"));
obj.setR13_gpfsr_nom_amt(rs.getBigDecimal("r13_gpfsr_nom_amt"));
obj.setR13_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att8_per_spe_ris"));
obj.setR13_gpfsr_chrg(rs.getBigDecimal("r13_gpfsr_chrg"));
obj.setR13_gpfsr_nom_amt1(rs.getBigDecimal("r13_gpfsr_nom_amt1"));
obj.setR13_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att4_per_spe_ris"));
obj.setR13_gpfsr_chrg1(rs.getBigDecimal("r13_gpfsr_chrg1"));
obj.setR13_gpfsr_nom_amt2(rs.getBigDecimal("r13_gpfsr_nom_amt2"));
obj.setR13_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att2_per_spe_ris"));
obj.setR13_gpfsr_chrg2(rs.getBigDecimal("r13_gpfsr_chrg2"));
obj.setR13_tot_spe_ris_chrg(rs.getBigDecimal("r13_tot_spe_ris_chrg"));
obj.setR13_net_pos_gen_mar_ris(rs.getBigDecimal("r13_net_pos_gen_mar_ris"));
obj.setR13_gen_mar_ris_chrg_8per(rs.getBigDecimal("r13_gen_mar_ris_chrg_8per"));
obj.setR13_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r13_2per_gen_mar_ris_chrg_div_port"));
obj.setR13_tot_gen_mar_risk_chrg(rs.getBigDecimal("r13_tot_gen_mar_risk_chrg"));
obj.setR13_tot_mar_ris_chrg(rs.getBigDecimal("r13_tot_mar_ris_chrg"));

// =========================
// R14
// =========================
obj.setR14_market(rs.getBigDecimal("r14_market"));
obj.setR14_gpfsr_nom_amt(rs.getBigDecimal("r14_gpfsr_nom_amt"));
obj.setR14_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att8_per_spe_ris"));
obj.setR14_gpfsr_chrg(rs.getBigDecimal("r14_gpfsr_chrg"));
obj.setR14_gpfsr_nom_amt1(rs.getBigDecimal("r14_gpfsr_nom_amt1"));
obj.setR14_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att4_per_spe_ris"));
obj.setR14_gpfsr_chrg1(rs.getBigDecimal("r14_gpfsr_chrg1"));
obj.setR14_gpfsr_nom_amt2(rs.getBigDecimal("r14_gpfsr_nom_amt2"));
obj.setR14_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att2_per_spe_ris"));
obj.setR14_gpfsr_chrg2(rs.getBigDecimal("r14_gpfsr_chrg2"));
obj.setR14_tot_spe_ris_chrg(rs.getBigDecimal("r14_tot_spe_ris_chrg"));
obj.setR14_net_pos_gen_mar_ris(rs.getBigDecimal("r14_net_pos_gen_mar_ris"));
obj.setR14_gen_mar_ris_chrg_8per(rs.getBigDecimal("r14_gen_mar_ris_chrg_8per"));
obj.setR14_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r14_2per_gen_mar_ris_chrg_div_port"));
obj.setR14_tot_gen_mar_risk_chrg(rs.getBigDecimal("r14_tot_gen_mar_risk_chrg"));
obj.setR14_tot_mar_ris_chrg(rs.getBigDecimal("r14_tot_mar_ris_chrg"));

// =========================
// R15
// =========================
obj.setR15_market(rs.getBigDecimal("r15_market"));
obj.setR15_gpfsr_nom_amt(rs.getBigDecimal("r15_gpfsr_nom_amt"));
obj.setR15_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att8_per_spe_ris"));
obj.setR15_gpfsr_chrg(rs.getBigDecimal("r15_gpfsr_chrg"));
obj.setR15_gpfsr_nom_amt1(rs.getBigDecimal("r15_gpfsr_nom_amt1"));
obj.setR15_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att4_per_spe_ris"));
obj.setR15_gpfsr_chrg1(rs.getBigDecimal("r15_gpfsr_chrg1"));
obj.setR15_gpfsr_nom_amt2(rs.getBigDecimal("r15_gpfsr_nom_amt2"));
obj.setR15_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att2_per_spe_ris"));
obj.setR15_gpfsr_chrg2(rs.getBigDecimal("r15_gpfsr_chrg2"));
obj.setR15_tot_spe_ris_chrg(rs.getBigDecimal("r15_tot_spe_ris_chrg"));
obj.setR15_net_pos_gen_mar_ris(rs.getBigDecimal("r15_net_pos_gen_mar_ris"));
obj.setR15_gen_mar_ris_chrg_8per(rs.getBigDecimal("r15_gen_mar_ris_chrg_8per"));
obj.setR15_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r15_2per_gen_mar_ris_chrg_div_port"));
obj.setR15_tot_gen_mar_risk_chrg(rs.getBigDecimal("r15_tot_gen_mar_risk_chrg"));
obj.setR15_tot_mar_ris_chrg(rs.getBigDecimal("r15_tot_mar_ris_chrg"));

// =========================
// R16
// =========================
obj.setR16_market(rs.getBigDecimal("r16_market"));
obj.setR16_gpfsr_nom_amt(rs.getBigDecimal("r16_gpfsr_nom_amt"));
obj.setR16_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att8_per_spe_ris"));
obj.setR16_gpfsr_chrg(rs.getBigDecimal("r16_gpfsr_chrg"));
obj.setR16_gpfsr_nom_amt1(rs.getBigDecimal("r16_gpfsr_nom_amt1"));
obj.setR16_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att4_per_spe_ris"));
obj.setR16_gpfsr_chrg1(rs.getBigDecimal("r16_gpfsr_chrg1"));
obj.setR16_gpfsr_nom_amt2(rs.getBigDecimal("r16_gpfsr_nom_amt2"));
obj.setR16_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att2_per_spe_ris"));
obj.setR16_gpfsr_chrg2(rs.getBigDecimal("r16_gpfsr_chrg2"));
obj.setR16_tot_spe_ris_chrg(rs.getBigDecimal("r16_tot_spe_ris_chrg"));
obj.setR16_net_pos_gen_mar_ris(rs.getBigDecimal("r16_net_pos_gen_mar_ris"));
obj.setR16_gen_mar_ris_chrg_8per(rs.getBigDecimal("r16_gen_mar_ris_chrg_8per"));
obj.setR16_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r16_2per_gen_mar_ris_chrg_div_port"));
obj.setR16_tot_gen_mar_risk_chrg(rs.getBigDecimal("r16_tot_gen_mar_risk_chrg"));
obj.setR16_tot_mar_ris_chrg(rs.getBigDecimal("r16_tot_mar_ris_chrg"));

// =========================
// R17
// =========================
obj.setR17_market(rs.getBigDecimal("r17_market"));
obj.setR17_gpfsr_nom_amt(rs.getBigDecimal("r17_gpfsr_nom_amt"));
obj.setR17_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att8_per_spe_ris"));
obj.setR17_gpfsr_chrg(rs.getBigDecimal("r17_gpfsr_chrg"));
obj.setR17_gpfsr_nom_amt1(rs.getBigDecimal("r17_gpfsr_nom_amt1"));
obj.setR17_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att4_per_spe_ris"));
obj.setR17_gpfsr_chrg1(rs.getBigDecimal("r17_gpfsr_chrg1"));
obj.setR17_gpfsr_nom_amt2(rs.getBigDecimal("r17_gpfsr_nom_amt2"));
obj.setR17_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att2_per_spe_ris"));
obj.setR17_gpfsr_chrg2(rs.getBigDecimal("r17_gpfsr_chrg2"));
obj.setR17_tot_spe_ris_chrg(rs.getBigDecimal("r17_tot_spe_ris_chrg"));
obj.setR17_net_pos_gen_mar_ris(rs.getBigDecimal("r17_net_pos_gen_mar_ris"));
obj.setR17_gen_mar_ris_chrg_8per(rs.getBigDecimal("r17_gen_mar_ris_chrg_8per"));
obj.setR17_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r17_2per_gen_mar_ris_chrg_div_port"));
obj.setR17_tot_gen_mar_risk_chrg(rs.getBigDecimal("r17_tot_gen_mar_risk_chrg"));
obj.setR17_tot_mar_ris_chrg(rs.getBigDecimal("r17_tot_mar_ris_chrg"));

// =========================
// R18
// =========================
obj.setR18_market(rs.getBigDecimal("r18_market"));
obj.setR18_gpfsr_nom_amt(rs.getBigDecimal("r18_gpfsr_nom_amt"));
obj.setR18_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att8_per_spe_ris"));
obj.setR18_gpfsr_chrg(rs.getBigDecimal("r18_gpfsr_chrg"));
obj.setR18_gpfsr_nom_amt1(rs.getBigDecimal("r18_gpfsr_nom_amt1"));
obj.setR18_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att4_per_spe_ris"));
obj.setR18_gpfsr_chrg1(rs.getBigDecimal("r18_gpfsr_chrg1"));
obj.setR18_gpfsr_nom_amt2(rs.getBigDecimal("r18_gpfsr_nom_amt2"));
obj.setR18_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att2_per_spe_ris"));
obj.setR18_gpfsr_chrg2(rs.getBigDecimal("r18_gpfsr_chrg2"));
obj.setR18_tot_spe_ris_chrg(rs.getBigDecimal("r18_tot_spe_ris_chrg"));
obj.setR18_net_pos_gen_mar_ris(rs.getBigDecimal("r18_net_pos_gen_mar_ris"));
obj.setR18_gen_mar_ris_chrg_8per(rs.getBigDecimal("r18_gen_mar_ris_chrg_8per"));
obj.setR18_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r18_2per_gen_mar_ris_chrg_div_port"));
obj.setR18_tot_gen_mar_risk_chrg(rs.getBigDecimal("r18_tot_gen_mar_risk_chrg"));
obj.setR18_tot_mar_ris_chrg(rs.getBigDecimal("r18_tot_mar_ris_chrg"));

// =========================
// R19
// =========================
obj.setR19_market(rs.getBigDecimal("r19_market"));
obj.setR19_gpfsr_nom_amt(rs.getBigDecimal("r19_gpfsr_nom_amt"));
obj.setR19_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att8_per_spe_ris"));
obj.setR19_gpfsr_chrg(rs.getBigDecimal("r19_gpfsr_chrg"));
obj.setR19_gpfsr_nom_amt1(rs.getBigDecimal("r19_gpfsr_nom_amt1"));
obj.setR19_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att4_per_spe_ris"));
obj.setR19_gpfsr_chrg1(rs.getBigDecimal("r19_gpfsr_chrg1"));
obj.setR19_gpfsr_nom_amt2(rs.getBigDecimal("r19_gpfsr_nom_amt2"));
obj.setR19_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att2_per_spe_ris"));
obj.setR19_gpfsr_chrg2(rs.getBigDecimal("r19_gpfsr_chrg2"));
obj.setR19_tot_spe_ris_chrg(rs.getBigDecimal("r19_tot_spe_ris_chrg"));
obj.setR19_net_pos_gen_mar_ris(rs.getBigDecimal("r19_net_pos_gen_mar_ris"));
obj.setR19_gen_mar_ris_chrg_8per(rs.getBigDecimal("r19_gen_mar_ris_chrg_8per"));
obj.setR19_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r19_2per_gen_mar_ris_chrg_div_port"));
obj.setR19_tot_gen_mar_risk_chrg(rs.getBigDecimal("r19_tot_gen_mar_risk_chrg"));
obj.setR19_tot_mar_ris_chrg(rs.getBigDecimal("r19_tot_mar_ris_chrg"));

// =========================
// R20
// =========================
obj.setR20_market(rs.getBigDecimal("r20_market"));
obj.setR20_gpfsr_nom_amt(rs.getBigDecimal("r20_gpfsr_nom_amt"));
obj.setR20_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att8_per_spe_ris"));
obj.setR20_gpfsr_chrg(rs.getBigDecimal("r20_gpfsr_chrg"));
obj.setR20_gpfsr_nom_amt1(rs.getBigDecimal("r20_gpfsr_nom_amt1"));
obj.setR20_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att4_per_spe_ris"));
obj.setR20_gpfsr_chrg1(rs.getBigDecimal("r20_gpfsr_chrg1"));
obj.setR20_gpfsr_nom_amt2(rs.getBigDecimal("r20_gpfsr_nom_amt2"));
obj.setR20_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att2_per_spe_ris"));
obj.setR20_gpfsr_chrg2(rs.getBigDecimal("r20_gpfsr_chrg2"));
obj.setR20_tot_spe_ris_chrg(rs.getBigDecimal("r20_tot_spe_ris_chrg"));
obj.setR20_net_pos_gen_mar_ris(rs.getBigDecimal("r20_net_pos_gen_mar_ris"));
obj.setR20_gen_mar_ris_chrg_8per(rs.getBigDecimal("r20_gen_mar_ris_chrg_8per"));
obj.setR20_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r20_2per_gen_mar_ris_chrg_div_port"));
obj.setR20_tot_gen_mar_risk_chrg(rs.getBigDecimal("r20_tot_gen_mar_risk_chrg"));
obj.setR20_tot_mar_ris_chrg(rs.getBigDecimal("r20_tot_mar_ris_chrg"));

// =========================
// R21
// =========================
obj.setR21_market(rs.getBigDecimal("r21_market"));
obj.setR21_gpfsr_nom_amt(rs.getBigDecimal("r21_gpfsr_nom_amt"));
obj.setR21_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att8_per_spe_ris"));
obj.setR21_gpfsr_chrg(rs.getBigDecimal("r21_gpfsr_chrg"));
obj.setR21_gpfsr_nom_amt1(rs.getBigDecimal("r21_gpfsr_nom_amt1"));
obj.setR21_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att4_per_spe_ris"));
obj.setR21_gpfsr_chrg1(rs.getBigDecimal("r21_gpfsr_chrg1"));
obj.setR21_gpfsr_nom_amt2(rs.getBigDecimal("r21_gpfsr_nom_amt2"));
obj.setR21_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att2_per_spe_ris"));
obj.setR21_gpfsr_chrg2(rs.getBigDecimal("r21_gpfsr_chrg2"));
obj.setR21_tot_spe_ris_chrg(rs.getBigDecimal("r21_tot_spe_ris_chrg"));
obj.setR21_net_pos_gen_mar_ris(rs.getBigDecimal("r21_net_pos_gen_mar_ris"));
obj.setR21_gen_mar_ris_chrg_8per(rs.getBigDecimal("r21_gen_mar_ris_chrg_8per"));
obj.setR21_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r21_2per_gen_mar_ris_chrg_div_port"));
obj.setR21_tot_gen_mar_risk_chrg(rs.getBigDecimal("r21_tot_gen_mar_risk_chrg"));
obj.setR21_tot_mar_ris_chrg(rs.getBigDecimal("r21_tot_mar_ris_chrg"));


// =========================
// R22
// =========================
obj.setR22_market(rs.getBigDecimal("r22_market"));
obj.setR22_gpfsr_nom_amt(rs.getBigDecimal("r22_gpfsr_nom_amt"));
obj.setR22_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att8_per_spe_ris"));
obj.setR22_gpfsr_chrg(rs.getBigDecimal("r22_gpfsr_chrg"));
obj.setR22_gpfsr_nom_amt1(rs.getBigDecimal("r22_gpfsr_nom_amt1"));
obj.setR22_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att4_per_spe_ris"));
obj.setR22_gpfsr_chrg1(rs.getBigDecimal("r22_gpfsr_chrg1"));
obj.setR22_gpfsr_nom_amt2(rs.getBigDecimal("r22_gpfsr_nom_amt2"));
obj.setR22_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att2_per_spe_ris"));
obj.setR22_gpfsr_chrg2(rs.getBigDecimal("r22_gpfsr_chrg2"));
obj.setR22_tot_spe_ris_chrg(rs.getBigDecimal("r22_tot_spe_ris_chrg"));
obj.setR22_net_pos_gen_mar_ris(rs.getBigDecimal("r22_net_pos_gen_mar_ris"));
obj.setR22_gen_mar_ris_chrg_8per(rs.getBigDecimal("r22_gen_mar_ris_chrg_8per"));
obj.setR22_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r22_2per_gen_mar_ris_chrg_div_port"));
obj.setR22_tot_gen_mar_risk_chrg(rs.getBigDecimal("r22_tot_gen_mar_risk_chrg"));
obj.setR22_tot_mar_ris_chrg(rs.getBigDecimal("r22_tot_mar_ris_chrg"));

// =========================
// R23
// =========================
obj.setR23_market(rs.getString("r23_market"));
obj.setR23_gpfsr_nom_amt(rs.getBigDecimal("r23_gpfsr_nom_amt"));
obj.setR23_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att8_per_spe_ris"));
obj.setR23_gpfsr_chrg(rs.getBigDecimal("r23_gpfsr_chrg"));
obj.setR23_gpfsr_nom_amt1(rs.getBigDecimal("r23_gpfsr_nom_amt1"));
obj.setR23_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att4_per_spe_ris"));
obj.setR23_gpfsr_chrg1(rs.getBigDecimal("r23_gpfsr_chrg1"));
obj.setR23_gpfsr_nom_amt2(rs.getBigDecimal("r23_gpfsr_nom_amt2"));
obj.setR23_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att2_per_spe_ris"));
obj.setR23_gpfsr_chrg2(rs.getBigDecimal("r23_gpfsr_chrg2"));
obj.setR23_tot_spe_ris_chrg(rs.getBigDecimal("r23_tot_spe_ris_chrg"));
obj.setR23_net_pos_gen_mar_ris(rs.getBigDecimal("r23_net_pos_gen_mar_ris"));
obj.setR23_gen_mar_ris_chrg_8per(rs.getBigDecimal("r23_gen_mar_ris_chrg_8per"));
obj.setR23_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r23_2per_gen_mar_ris_chrg_div_port"));
obj.setR23_tot_gen_mar_risk_chrg(rs.getBigDecimal("r23_tot_gen_mar_risk_chrg"));
obj.setR23_tot_mar_ris_chrg(rs.getBigDecimal("r23_tot_mar_ris_chrg"));


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

public class M_EPR_Detail_Entity {

   
private BigDecimal	r11_market;
	private BigDecimal	r11_gpfsr_nom_amt;
	private BigDecimal	r11_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg;
	private BigDecimal	r11_gpfsr_nom_amt1;
	private BigDecimal	r11_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg1;
	private BigDecimal	r11_gpfsr_nom_amt2;
	private BigDecimal	r11_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg2;
	private BigDecimal	r11_tot_spe_ris_chrg;
	private BigDecimal	r11_net_pos_gen_mar_ris;
	private BigDecimal	r11_gen_mar_ris_chrg_8per;
	private BigDecimal	r11_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r11_tot_gen_mar_risk_chrg;
	private BigDecimal	r11_tot_mar_ris_chrg;
	
	private BigDecimal	r12_market;
	private BigDecimal	r12_gpfsr_nom_amt;
	private BigDecimal	r12_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg;
	private BigDecimal	r12_gpfsr_nom_amt1;
	private BigDecimal	r12_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg1;
	private BigDecimal	r12_gpfsr_nom_amt2;
	private BigDecimal	r12_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg2;
	private BigDecimal	r12_tot_spe_ris_chrg;
	private BigDecimal	r12_net_pos_gen_mar_ris;
	private BigDecimal	r12_gen_mar_ris_chrg_8per;
	private BigDecimal	r12_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r12_tot_gen_mar_risk_chrg;
	private BigDecimal	r12_tot_mar_ris_chrg;
	
	private BigDecimal	r13_market;
	private BigDecimal	r13_gpfsr_nom_amt;
	private BigDecimal	r13_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg;
	private BigDecimal	r13_gpfsr_nom_amt1;
	private BigDecimal	r13_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg1;
	private BigDecimal	r13_gpfsr_nom_amt2;
	private BigDecimal	r13_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg2;
	private BigDecimal	r13_tot_spe_ris_chrg;
	private BigDecimal	r13_net_pos_gen_mar_ris;
	private BigDecimal	r13_gen_mar_ris_chrg_8per;
	private BigDecimal	r13_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r13_tot_gen_mar_risk_chrg;
	private BigDecimal	r13_tot_mar_ris_chrg;
	
	private BigDecimal	r14_market;
	private BigDecimal	r14_gpfsr_nom_amt;
	private BigDecimal	r14_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg;
	private BigDecimal	r14_gpfsr_nom_amt1;
	private BigDecimal	r14_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg1;
	private BigDecimal	r14_gpfsr_nom_amt2;
	private BigDecimal	r14_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg2;
	private BigDecimal	r14_tot_spe_ris_chrg;
	private BigDecimal	r14_net_pos_gen_mar_ris;
	private BigDecimal	r14_gen_mar_ris_chrg_8per;
	private BigDecimal	r14_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r14_tot_gen_mar_risk_chrg;
	private BigDecimal	r14_tot_mar_ris_chrg;
	
	private BigDecimal	r15_market;
	private BigDecimal	r15_gpfsr_nom_amt;
	private BigDecimal	r15_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg;
	private BigDecimal	r15_gpfsr_nom_amt1;
	private BigDecimal	r15_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg1;
	private BigDecimal	r15_gpfsr_nom_amt2;
	private BigDecimal	r15_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg2;
	private BigDecimal	r15_tot_spe_ris_chrg;
	private BigDecimal	r15_net_pos_gen_mar_ris;
	private BigDecimal	r15_gen_mar_ris_chrg_8per;
	private BigDecimal	r15_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r15_tot_gen_mar_risk_chrg;
	private BigDecimal	r15_tot_mar_ris_chrg;
	
	private BigDecimal	r16_market;
	private BigDecimal	r16_gpfsr_nom_amt;
	private BigDecimal	r16_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg;
	private BigDecimal	r16_gpfsr_nom_amt1;
	private BigDecimal	r16_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg1;
	private BigDecimal	r16_gpfsr_nom_amt2;
	private BigDecimal	r16_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg2;
	private BigDecimal	r16_tot_spe_ris_chrg;
	private BigDecimal	r16_net_pos_gen_mar_ris;
	private BigDecimal	r16_gen_mar_ris_chrg_8per;
	private BigDecimal	r16_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r16_tot_gen_mar_risk_chrg;
	private BigDecimal	r16_tot_mar_ris_chrg;
	
	private BigDecimal	r17_market;
	private BigDecimal	r17_gpfsr_nom_amt;
	private BigDecimal	r17_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg;
	private BigDecimal	r17_gpfsr_nom_amt1;
	private BigDecimal	r17_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg1;
	private BigDecimal	r17_gpfsr_nom_amt2;
	private BigDecimal	r17_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg2;
	private BigDecimal	r17_tot_spe_ris_chrg;
	private BigDecimal	r17_net_pos_gen_mar_ris;
	private BigDecimal	r17_gen_mar_ris_chrg_8per;
	private BigDecimal	r17_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r17_tot_gen_mar_risk_chrg;
	private BigDecimal	r17_tot_mar_ris_chrg;
	
	private BigDecimal	r18_market;
	private BigDecimal	r18_gpfsr_nom_amt;
	private BigDecimal	r18_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg;
	private BigDecimal	r18_gpfsr_nom_amt1;
	private BigDecimal	r18_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg1;
	private BigDecimal	r18_gpfsr_nom_amt2;
	private BigDecimal	r18_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg2;
	private BigDecimal	r18_tot_spe_ris_chrg;
	private BigDecimal	r18_net_pos_gen_mar_ris;
	private BigDecimal	r18_gen_mar_ris_chrg_8per;
	private BigDecimal	r18_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r18_tot_gen_mar_risk_chrg;
	private BigDecimal	r18_tot_mar_ris_chrg;
	
	private BigDecimal	r19_market;
	private BigDecimal	r19_gpfsr_nom_amt;
	private BigDecimal	r19_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg;
	private BigDecimal	r19_gpfsr_nom_amt1;
	private BigDecimal	r19_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg1;
	private BigDecimal	r19_gpfsr_nom_amt2;
	private BigDecimal	r19_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg2;
	private BigDecimal	r19_tot_spe_ris_chrg;
	private BigDecimal	r19_net_pos_gen_mar_ris;
	private BigDecimal	r19_gen_mar_ris_chrg_8per;
	private BigDecimal	r19_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r19_tot_gen_mar_risk_chrg;
	private BigDecimal	r19_tot_mar_ris_chrg;
	
	private BigDecimal	r20_market;
	private BigDecimal	r20_gpfsr_nom_amt;
	private BigDecimal	r20_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg;
	private BigDecimal	r20_gpfsr_nom_amt1;
	private BigDecimal	r20_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg1;
	private BigDecimal	r20_gpfsr_nom_amt2;
	private BigDecimal	r20_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg2;
	private BigDecimal	r20_tot_spe_ris_chrg;
	private BigDecimal	r20_net_pos_gen_mar_ris;
	private BigDecimal	r20_gen_mar_ris_chrg_8per;
	private BigDecimal	r20_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r20_tot_gen_mar_risk_chrg;
	private BigDecimal	r20_tot_mar_ris_chrg;
	
	private BigDecimal	r21_market;
	private BigDecimal	r21_gpfsr_nom_amt;
	private BigDecimal	r21_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg;
	private BigDecimal	r21_gpfsr_nom_amt1;
	private BigDecimal	r21_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg1;
	private BigDecimal	r21_gpfsr_nom_amt2;
	private BigDecimal	r21_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg2;
	private BigDecimal	r21_tot_spe_ris_chrg;
	private BigDecimal	r21_net_pos_gen_mar_ris;
	private BigDecimal	r21_gen_mar_ris_chrg_8per;
	private BigDecimal	r21_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r21_tot_gen_mar_risk_chrg;
	private BigDecimal	r21_tot_mar_ris_chrg;
	
	private BigDecimal	r22_market;
	private BigDecimal	r22_gpfsr_nom_amt;
	private BigDecimal	r22_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg;
	private BigDecimal	r22_gpfsr_nom_amt1;
	private BigDecimal	r22_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg1;
	private BigDecimal	r22_gpfsr_nom_amt2;
	private BigDecimal	r22_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg2;
	private BigDecimal	r22_tot_spe_ris_chrg;
	private BigDecimal	r22_net_pos_gen_mar_ris;
	private BigDecimal	r22_gen_mar_ris_chrg_8per;
	private BigDecimal	r22_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r22_tot_gen_mar_risk_chrg;
	private BigDecimal	r22_tot_mar_ris_chrg;
	
	private String	r23_market;
	private BigDecimal	r23_gpfsr_nom_amt;
	private BigDecimal	r23_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg;
	private BigDecimal	r23_gpfsr_nom_amt1;
	private BigDecimal	r23_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg1;
	private BigDecimal	r23_gpfsr_nom_amt2;
	private BigDecimal	r23_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg2;
	private BigDecimal	r23_tot_spe_ris_chrg;
	private BigDecimal	r23_net_pos_gen_mar_ris;
	private BigDecimal	r23_gen_mar_ris_chrg_8per;
	private BigDecimal	r23_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r23_tot_gen_mar_risk_chrg;
	private BigDecimal	r23_tot_mar_ris_chrg;
	
	
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
	
	
	
	public BigDecimal getR11_market() {
		return r11_market;
	}
	public void setR11_market(BigDecimal r11_market) {
		this.r11_market = r11_market;
	}
	public BigDecimal getR11_gpfsr_nom_amt() {
		return r11_gpfsr_nom_amt;
	}
	public void setR11_gpfsr_nom_amt(BigDecimal r11_gpfsr_nom_amt) {
		this.r11_gpfsr_nom_amt = r11_gpfsr_nom_amt;
	}
	public BigDecimal getR11_gpfsr_pos_att8_per_spe_ris() {
		return r11_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att8_per_spe_ris(BigDecimal r11_gpfsr_pos_att8_per_spe_ris) {
		this.r11_gpfsr_pos_att8_per_spe_ris = r11_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg() {
		return r11_gpfsr_chrg;
	}
	public void setR11_gpfsr_chrg(BigDecimal r11_gpfsr_chrg) {
		this.r11_gpfsr_chrg = r11_gpfsr_chrg;
	}
	public BigDecimal getR11_gpfsr_nom_amt1() {
		return r11_gpfsr_nom_amt1;
	}
	public void setR11_gpfsr_nom_amt1(BigDecimal r11_gpfsr_nom_amt1) {
		this.r11_gpfsr_nom_amt1 = r11_gpfsr_nom_amt1;
	}
	public BigDecimal getR11_gpfsr_pos_att4_per_spe_ris() {
		return r11_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att4_per_spe_ris(BigDecimal r11_gpfsr_pos_att4_per_spe_ris) {
		this.r11_gpfsr_pos_att4_per_spe_ris = r11_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg1() {
		return r11_gpfsr_chrg1;
	}
	public void setR11_gpfsr_chrg1(BigDecimal r11_gpfsr_chrg1) {
		this.r11_gpfsr_chrg1 = r11_gpfsr_chrg1;
	}
	public BigDecimal getR11_gpfsr_nom_amt2() {
		return r11_gpfsr_nom_amt2;
	}
	public void setR11_gpfsr_nom_amt2(BigDecimal r11_gpfsr_nom_amt2) {
		this.r11_gpfsr_nom_amt2 = r11_gpfsr_nom_amt2;
	}
	public BigDecimal getR11_gpfsr_pos_att2_per_spe_ris() {
		return r11_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att2_per_spe_ris(BigDecimal r11_gpfsr_pos_att2_per_spe_ris) {
		this.r11_gpfsr_pos_att2_per_spe_ris = r11_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg2() {
		return r11_gpfsr_chrg2;
	}
	public void setR11_gpfsr_chrg2(BigDecimal r11_gpfsr_chrg2) {
		this.r11_gpfsr_chrg2 = r11_gpfsr_chrg2;
	}
	public BigDecimal getR11_tot_spe_ris_chrg() {
		return r11_tot_spe_ris_chrg;
	}
	public void setR11_tot_spe_ris_chrg(BigDecimal r11_tot_spe_ris_chrg) {
		this.r11_tot_spe_ris_chrg = r11_tot_spe_ris_chrg;
	}
	public BigDecimal getR11_net_pos_gen_mar_ris() {
		return r11_net_pos_gen_mar_ris;
	}
	public void setR11_net_pos_gen_mar_ris(BigDecimal r11_net_pos_gen_mar_ris) {
		this.r11_net_pos_gen_mar_ris = r11_net_pos_gen_mar_ris;
	}
	public BigDecimal getR11_gen_mar_ris_chrg_8per() {
		return r11_gen_mar_ris_chrg_8per;
	}
	public void setR11_gen_mar_ris_chrg_8per(BigDecimal r11_gen_mar_ris_chrg_8per) {
		this.r11_gen_mar_ris_chrg_8per = r11_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR11_2per_gen_mar_ris_chrg_div_port() {
		return r11_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR11_2per_gen_mar_ris_chrg_div_port(BigDecimal r11_2per_gen_mar_ris_chrg_div_port) {
		this.r11_2per_gen_mar_ris_chrg_div_port = r11_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR11_tot_gen_mar_risk_chrg() {
		return r11_tot_gen_mar_risk_chrg;
	}
	public void setR11_tot_gen_mar_risk_chrg(BigDecimal r11_tot_gen_mar_risk_chrg) {
		this.r11_tot_gen_mar_risk_chrg = r11_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR11_tot_mar_ris_chrg() {
		return r11_tot_mar_ris_chrg;
	}
	public void setR11_tot_mar_ris_chrg(BigDecimal r11_tot_mar_ris_chrg) {
		this.r11_tot_mar_ris_chrg = r11_tot_mar_ris_chrg;
	}
	public BigDecimal getR12_market() {
		return r12_market;
	}
	public void setR12_market(BigDecimal r12_market) {
		this.r12_market = r12_market;
	}
	public BigDecimal getR12_gpfsr_nom_amt() {
		return r12_gpfsr_nom_amt;
	}
	public void setR12_gpfsr_nom_amt(BigDecimal r12_gpfsr_nom_amt) {
		this.r12_gpfsr_nom_amt = r12_gpfsr_nom_amt;
	}
	public BigDecimal getR12_gpfsr_pos_att8_per_spe_ris() {
		return r12_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att8_per_spe_ris(BigDecimal r12_gpfsr_pos_att8_per_spe_ris) {
		this.r12_gpfsr_pos_att8_per_spe_ris = r12_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg() {
		return r12_gpfsr_chrg;
	}
	public void setR12_gpfsr_chrg(BigDecimal r12_gpfsr_chrg) {
		this.r12_gpfsr_chrg = r12_gpfsr_chrg;
	}
	public BigDecimal getR12_gpfsr_nom_amt1() {
		return r12_gpfsr_nom_amt1;
	}
	public void setR12_gpfsr_nom_amt1(BigDecimal r12_gpfsr_nom_amt1) {
		this.r12_gpfsr_nom_amt1 = r12_gpfsr_nom_amt1;
	}
	public BigDecimal getR12_gpfsr_pos_att4_per_spe_ris() {
		return r12_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att4_per_spe_ris(BigDecimal r12_gpfsr_pos_att4_per_spe_ris) {
		this.r12_gpfsr_pos_att4_per_spe_ris = r12_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg1() {
		return r12_gpfsr_chrg1;
	}
	public void setR12_gpfsr_chrg1(BigDecimal r12_gpfsr_chrg1) {
		this.r12_gpfsr_chrg1 = r12_gpfsr_chrg1;
	}
	public BigDecimal getR12_gpfsr_nom_amt2() {
		return r12_gpfsr_nom_amt2;
	}
	public void setR12_gpfsr_nom_amt2(BigDecimal r12_gpfsr_nom_amt2) {
		this.r12_gpfsr_nom_amt2 = r12_gpfsr_nom_amt2;
	}
	public BigDecimal getR12_gpfsr_pos_att2_per_spe_ris() {
		return r12_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att2_per_spe_ris(BigDecimal r12_gpfsr_pos_att2_per_spe_ris) {
		this.r12_gpfsr_pos_att2_per_spe_ris = r12_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg2() {
		return r12_gpfsr_chrg2;
	}
	public void setR12_gpfsr_chrg2(BigDecimal r12_gpfsr_chrg2) {
		this.r12_gpfsr_chrg2 = r12_gpfsr_chrg2;
	}
	public BigDecimal getR12_tot_spe_ris_chrg() {
		return r12_tot_spe_ris_chrg;
	}
	public void setR12_tot_spe_ris_chrg(BigDecimal r12_tot_spe_ris_chrg) {
		this.r12_tot_spe_ris_chrg = r12_tot_spe_ris_chrg;
	}
	public BigDecimal getR12_net_pos_gen_mar_ris() {
		return r12_net_pos_gen_mar_ris;
	}
	public void setR12_net_pos_gen_mar_ris(BigDecimal r12_net_pos_gen_mar_ris) {
		this.r12_net_pos_gen_mar_ris = r12_net_pos_gen_mar_ris;
	}
	public BigDecimal getR12_gen_mar_ris_chrg_8per() {
		return r12_gen_mar_ris_chrg_8per;
	}
	public void setR12_gen_mar_ris_chrg_8per(BigDecimal r12_gen_mar_ris_chrg_8per) {
		this.r12_gen_mar_ris_chrg_8per = r12_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR12_2per_gen_mar_ris_chrg_div_port() {
		return r12_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR12_2per_gen_mar_ris_chrg_div_port(BigDecimal r12_2per_gen_mar_ris_chrg_div_port) {
		this.r12_2per_gen_mar_ris_chrg_div_port = r12_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR12_tot_gen_mar_risk_chrg() {
		return r12_tot_gen_mar_risk_chrg;
	}
	public void setR12_tot_gen_mar_risk_chrg(BigDecimal r12_tot_gen_mar_risk_chrg) {
		this.r12_tot_gen_mar_risk_chrg = r12_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR12_tot_mar_ris_chrg() {
		return r12_tot_mar_ris_chrg;
	}
	public void setR12_tot_mar_ris_chrg(BigDecimal r12_tot_mar_ris_chrg) {
		this.r12_tot_mar_ris_chrg = r12_tot_mar_ris_chrg;
	}
	public BigDecimal getR13_market() {
		return r13_market;
	}
	public void setR13_market(BigDecimal r13_market) {
		this.r13_market = r13_market;
	}
	public BigDecimal getR13_gpfsr_nom_amt() {
		return r13_gpfsr_nom_amt;
	}
	public void setR13_gpfsr_nom_amt(BigDecimal r13_gpfsr_nom_amt) {
		this.r13_gpfsr_nom_amt = r13_gpfsr_nom_amt;
	}
	public BigDecimal getR13_gpfsr_pos_att8_per_spe_ris() {
		return r13_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att8_per_spe_ris(BigDecimal r13_gpfsr_pos_att8_per_spe_ris) {
		this.r13_gpfsr_pos_att8_per_spe_ris = r13_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg() {
		return r13_gpfsr_chrg;
	}
	public void setR13_gpfsr_chrg(BigDecimal r13_gpfsr_chrg) {
		this.r13_gpfsr_chrg = r13_gpfsr_chrg;
	}
	public BigDecimal getR13_gpfsr_nom_amt1() {
		return r13_gpfsr_nom_amt1;
	}
	public void setR13_gpfsr_nom_amt1(BigDecimal r13_gpfsr_nom_amt1) {
		this.r13_gpfsr_nom_amt1 = r13_gpfsr_nom_amt1;
	}
	public BigDecimal getR13_gpfsr_pos_att4_per_spe_ris() {
		return r13_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att4_per_spe_ris(BigDecimal r13_gpfsr_pos_att4_per_spe_ris) {
		this.r13_gpfsr_pos_att4_per_spe_ris = r13_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg1() {
		return r13_gpfsr_chrg1;
	}
	public void setR13_gpfsr_chrg1(BigDecimal r13_gpfsr_chrg1) {
		this.r13_gpfsr_chrg1 = r13_gpfsr_chrg1;
	}
	public BigDecimal getR13_gpfsr_nom_amt2() {
		return r13_gpfsr_nom_amt2;
	}
	public void setR13_gpfsr_nom_amt2(BigDecimal r13_gpfsr_nom_amt2) {
		this.r13_gpfsr_nom_amt2 = r13_gpfsr_nom_amt2;
	}
	public BigDecimal getR13_gpfsr_pos_att2_per_spe_ris() {
		return r13_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att2_per_spe_ris(BigDecimal r13_gpfsr_pos_att2_per_spe_ris) {
		this.r13_gpfsr_pos_att2_per_spe_ris = r13_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg2() {
		return r13_gpfsr_chrg2;
	}
	public void setR13_gpfsr_chrg2(BigDecimal r13_gpfsr_chrg2) {
		this.r13_gpfsr_chrg2 = r13_gpfsr_chrg2;
	}
	public BigDecimal getR13_tot_spe_ris_chrg() {
		return r13_tot_spe_ris_chrg;
	}
	public void setR13_tot_spe_ris_chrg(BigDecimal r13_tot_spe_ris_chrg) {
		this.r13_tot_spe_ris_chrg = r13_tot_spe_ris_chrg;
	}
	public BigDecimal getR13_net_pos_gen_mar_ris() {
		return r13_net_pos_gen_mar_ris;
	}
	public void setR13_net_pos_gen_mar_ris(BigDecimal r13_net_pos_gen_mar_ris) {
		this.r13_net_pos_gen_mar_ris = r13_net_pos_gen_mar_ris;
	}
	public BigDecimal getR13_gen_mar_ris_chrg_8per() {
		return r13_gen_mar_ris_chrg_8per;
	}
	public void setR13_gen_mar_ris_chrg_8per(BigDecimal r13_gen_mar_ris_chrg_8per) {
		this.r13_gen_mar_ris_chrg_8per = r13_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR13_2per_gen_mar_ris_chrg_div_port() {
		return r13_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR13_2per_gen_mar_ris_chrg_div_port(BigDecimal r13_2per_gen_mar_ris_chrg_div_port) {
		this.r13_2per_gen_mar_ris_chrg_div_port = r13_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR13_tot_gen_mar_risk_chrg() {
		return r13_tot_gen_mar_risk_chrg;
	}
	public void setR13_tot_gen_mar_risk_chrg(BigDecimal r13_tot_gen_mar_risk_chrg) {
		this.r13_tot_gen_mar_risk_chrg = r13_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR13_tot_mar_ris_chrg() {
		return r13_tot_mar_ris_chrg;
	}
	public void setR13_tot_mar_ris_chrg(BigDecimal r13_tot_mar_ris_chrg) {
		this.r13_tot_mar_ris_chrg = r13_tot_mar_ris_chrg;
	}
	public BigDecimal getR14_market() {
		return r14_market;
	}
	public void setR14_market(BigDecimal r14_market) {
		this.r14_market = r14_market;
	}
	public BigDecimal getR14_gpfsr_nom_amt() {
		return r14_gpfsr_nom_amt;
	}
	public void setR14_gpfsr_nom_amt(BigDecimal r14_gpfsr_nom_amt) {
		this.r14_gpfsr_nom_amt = r14_gpfsr_nom_amt;
	}
	public BigDecimal getR14_gpfsr_pos_att8_per_spe_ris() {
		return r14_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att8_per_spe_ris(BigDecimal r14_gpfsr_pos_att8_per_spe_ris) {
		this.r14_gpfsr_pos_att8_per_spe_ris = r14_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg() {
		return r14_gpfsr_chrg;
	}
	public void setR14_gpfsr_chrg(BigDecimal r14_gpfsr_chrg) {
		this.r14_gpfsr_chrg = r14_gpfsr_chrg;
	}
	public BigDecimal getR14_gpfsr_nom_amt1() {
		return r14_gpfsr_nom_amt1;
	}
	public void setR14_gpfsr_nom_amt1(BigDecimal r14_gpfsr_nom_amt1) {
		this.r14_gpfsr_nom_amt1 = r14_gpfsr_nom_amt1;
	}
	public BigDecimal getR14_gpfsr_pos_att4_per_spe_ris() {
		return r14_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att4_per_spe_ris(BigDecimal r14_gpfsr_pos_att4_per_spe_ris) {
		this.r14_gpfsr_pos_att4_per_spe_ris = r14_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg1() {
		return r14_gpfsr_chrg1;
	}
	public void setR14_gpfsr_chrg1(BigDecimal r14_gpfsr_chrg1) {
		this.r14_gpfsr_chrg1 = r14_gpfsr_chrg1;
	}
	public BigDecimal getR14_gpfsr_nom_amt2() {
		return r14_gpfsr_nom_amt2;
	}
	public void setR14_gpfsr_nom_amt2(BigDecimal r14_gpfsr_nom_amt2) {
		this.r14_gpfsr_nom_amt2 = r14_gpfsr_nom_amt2;
	}
	public BigDecimal getR14_gpfsr_pos_att2_per_spe_ris() {
		return r14_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att2_per_spe_ris(BigDecimal r14_gpfsr_pos_att2_per_spe_ris) {
		this.r14_gpfsr_pos_att2_per_spe_ris = r14_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg2() {
		return r14_gpfsr_chrg2;
	}
	public void setR14_gpfsr_chrg2(BigDecimal r14_gpfsr_chrg2) {
		this.r14_gpfsr_chrg2 = r14_gpfsr_chrg2;
	}
	public BigDecimal getR14_tot_spe_ris_chrg() {
		return r14_tot_spe_ris_chrg;
	}
	public void setR14_tot_spe_ris_chrg(BigDecimal r14_tot_spe_ris_chrg) {
		this.r14_tot_spe_ris_chrg = r14_tot_spe_ris_chrg;
	}
	public BigDecimal getR14_net_pos_gen_mar_ris() {
		return r14_net_pos_gen_mar_ris;
	}
	public void setR14_net_pos_gen_mar_ris(BigDecimal r14_net_pos_gen_mar_ris) {
		this.r14_net_pos_gen_mar_ris = r14_net_pos_gen_mar_ris;
	}
	public BigDecimal getR14_gen_mar_ris_chrg_8per() {
		return r14_gen_mar_ris_chrg_8per;
	}
	public void setR14_gen_mar_ris_chrg_8per(BigDecimal r14_gen_mar_ris_chrg_8per) {
		this.r14_gen_mar_ris_chrg_8per = r14_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR14_2per_gen_mar_ris_chrg_div_port() {
		return r14_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR14_2per_gen_mar_ris_chrg_div_port(BigDecimal r14_2per_gen_mar_ris_chrg_div_port) {
		this.r14_2per_gen_mar_ris_chrg_div_port = r14_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR14_tot_gen_mar_risk_chrg() {
		return r14_tot_gen_mar_risk_chrg;
	}
	public void setR14_tot_gen_mar_risk_chrg(BigDecimal r14_tot_gen_mar_risk_chrg) {
		this.r14_tot_gen_mar_risk_chrg = r14_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR14_tot_mar_ris_chrg() {
		return r14_tot_mar_ris_chrg;
	}
	public void setR14_tot_mar_ris_chrg(BigDecimal r14_tot_mar_ris_chrg) {
		this.r14_tot_mar_ris_chrg = r14_tot_mar_ris_chrg;
	}
	public BigDecimal getR15_market() {
		return r15_market;
	}
	public void setR15_market(BigDecimal r15_market) {
		this.r15_market = r15_market;
	}
	public BigDecimal getR15_gpfsr_nom_amt() {
		return r15_gpfsr_nom_amt;
	}
	public void setR15_gpfsr_nom_amt(BigDecimal r15_gpfsr_nom_amt) {
		this.r15_gpfsr_nom_amt = r15_gpfsr_nom_amt;
	}
	public BigDecimal getR15_gpfsr_pos_att8_per_spe_ris() {
		return r15_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att8_per_spe_ris(BigDecimal r15_gpfsr_pos_att8_per_spe_ris) {
		this.r15_gpfsr_pos_att8_per_spe_ris = r15_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg() {
		return r15_gpfsr_chrg;
	}
	public void setR15_gpfsr_chrg(BigDecimal r15_gpfsr_chrg) {
		this.r15_gpfsr_chrg = r15_gpfsr_chrg;
	}
	public BigDecimal getR15_gpfsr_nom_amt1() {
		return r15_gpfsr_nom_amt1;
	}
	public void setR15_gpfsr_nom_amt1(BigDecimal r15_gpfsr_nom_amt1) {
		this.r15_gpfsr_nom_amt1 = r15_gpfsr_nom_amt1;
	}
	public BigDecimal getR15_gpfsr_pos_att4_per_spe_ris() {
		return r15_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att4_per_spe_ris(BigDecimal r15_gpfsr_pos_att4_per_spe_ris) {
		this.r15_gpfsr_pos_att4_per_spe_ris = r15_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg1() {
		return r15_gpfsr_chrg1;
	}
	public void setR15_gpfsr_chrg1(BigDecimal r15_gpfsr_chrg1) {
		this.r15_gpfsr_chrg1 = r15_gpfsr_chrg1;
	}
	public BigDecimal getR15_gpfsr_nom_amt2() {
		return r15_gpfsr_nom_amt2;
	}
	public void setR15_gpfsr_nom_amt2(BigDecimal r15_gpfsr_nom_amt2) {
		this.r15_gpfsr_nom_amt2 = r15_gpfsr_nom_amt2;
	}
	public BigDecimal getR15_gpfsr_pos_att2_per_spe_ris() {
		return r15_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att2_per_spe_ris(BigDecimal r15_gpfsr_pos_att2_per_spe_ris) {
		this.r15_gpfsr_pos_att2_per_spe_ris = r15_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg2() {
		return r15_gpfsr_chrg2;
	}
	public void setR15_gpfsr_chrg2(BigDecimal r15_gpfsr_chrg2) {
		this.r15_gpfsr_chrg2 = r15_gpfsr_chrg2;
	}
	public BigDecimal getR15_tot_spe_ris_chrg() {
		return r15_tot_spe_ris_chrg;
	}
	public void setR15_tot_spe_ris_chrg(BigDecimal r15_tot_spe_ris_chrg) {
		this.r15_tot_spe_ris_chrg = r15_tot_spe_ris_chrg;
	}
	public BigDecimal getR15_net_pos_gen_mar_ris() {
		return r15_net_pos_gen_mar_ris;
	}
	public void setR15_net_pos_gen_mar_ris(BigDecimal r15_net_pos_gen_mar_ris) {
		this.r15_net_pos_gen_mar_ris = r15_net_pos_gen_mar_ris;
	}
	public BigDecimal getR15_gen_mar_ris_chrg_8per() {
		return r15_gen_mar_ris_chrg_8per;
	}
	public void setR15_gen_mar_ris_chrg_8per(BigDecimal r15_gen_mar_ris_chrg_8per) {
		this.r15_gen_mar_ris_chrg_8per = r15_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR15_2per_gen_mar_ris_chrg_div_port() {
		return r15_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR15_2per_gen_mar_ris_chrg_div_port(BigDecimal r15_2per_gen_mar_ris_chrg_div_port) {
		this.r15_2per_gen_mar_ris_chrg_div_port = r15_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR15_tot_gen_mar_risk_chrg() {
		return r15_tot_gen_mar_risk_chrg;
	}
	public void setR15_tot_gen_mar_risk_chrg(BigDecimal r15_tot_gen_mar_risk_chrg) {
		this.r15_tot_gen_mar_risk_chrg = r15_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR15_tot_mar_ris_chrg() {
		return r15_tot_mar_ris_chrg;
	}
	public void setR15_tot_mar_ris_chrg(BigDecimal r15_tot_mar_ris_chrg) {
		this.r15_tot_mar_ris_chrg = r15_tot_mar_ris_chrg;
	}
	public BigDecimal getR16_market() {
		return r16_market;
	}
	public void setR16_market(BigDecimal r16_market) {
		this.r16_market = r16_market;
	}
	public BigDecimal getR16_gpfsr_nom_amt() {
		return r16_gpfsr_nom_amt;
	}
	public void setR16_gpfsr_nom_amt(BigDecimal r16_gpfsr_nom_amt) {
		this.r16_gpfsr_nom_amt = r16_gpfsr_nom_amt;
	}
	public BigDecimal getR16_gpfsr_pos_att8_per_spe_ris() {
		return r16_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att8_per_spe_ris(BigDecimal r16_gpfsr_pos_att8_per_spe_ris) {
		this.r16_gpfsr_pos_att8_per_spe_ris = r16_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg() {
		return r16_gpfsr_chrg;
	}
	public void setR16_gpfsr_chrg(BigDecimal r16_gpfsr_chrg) {
		this.r16_gpfsr_chrg = r16_gpfsr_chrg;
	}
	public BigDecimal getR16_gpfsr_nom_amt1() {
		return r16_gpfsr_nom_amt1;
	}
	public void setR16_gpfsr_nom_amt1(BigDecimal r16_gpfsr_nom_amt1) {
		this.r16_gpfsr_nom_amt1 = r16_gpfsr_nom_amt1;
	}
	public BigDecimal getR16_gpfsr_pos_att4_per_spe_ris() {
		return r16_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att4_per_spe_ris(BigDecimal r16_gpfsr_pos_att4_per_spe_ris) {
		this.r16_gpfsr_pos_att4_per_spe_ris = r16_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg1() {
		return r16_gpfsr_chrg1;
	}
	public void setR16_gpfsr_chrg1(BigDecimal r16_gpfsr_chrg1) {
		this.r16_gpfsr_chrg1 = r16_gpfsr_chrg1;
	}
	public BigDecimal getR16_gpfsr_nom_amt2() {
		return r16_gpfsr_nom_amt2;
	}
	public void setR16_gpfsr_nom_amt2(BigDecimal r16_gpfsr_nom_amt2) {
		this.r16_gpfsr_nom_amt2 = r16_gpfsr_nom_amt2;
	}
	public BigDecimal getR16_gpfsr_pos_att2_per_spe_ris() {
		return r16_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att2_per_spe_ris(BigDecimal r16_gpfsr_pos_att2_per_spe_ris) {
		this.r16_gpfsr_pos_att2_per_spe_ris = r16_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg2() {
		return r16_gpfsr_chrg2;
	}
	public void setR16_gpfsr_chrg2(BigDecimal r16_gpfsr_chrg2) {
		this.r16_gpfsr_chrg2 = r16_gpfsr_chrg2;
	}
	public BigDecimal getR16_tot_spe_ris_chrg() {
		return r16_tot_spe_ris_chrg;
	}
	public void setR16_tot_spe_ris_chrg(BigDecimal r16_tot_spe_ris_chrg) {
		this.r16_tot_spe_ris_chrg = r16_tot_spe_ris_chrg;
	}
	public BigDecimal getR16_net_pos_gen_mar_ris() {
		return r16_net_pos_gen_mar_ris;
	}
	public void setR16_net_pos_gen_mar_ris(BigDecimal r16_net_pos_gen_mar_ris) {
		this.r16_net_pos_gen_mar_ris = r16_net_pos_gen_mar_ris;
	}
	public BigDecimal getR16_gen_mar_ris_chrg_8per() {
		return r16_gen_mar_ris_chrg_8per;
	}
	public void setR16_gen_mar_ris_chrg_8per(BigDecimal r16_gen_mar_ris_chrg_8per) {
		this.r16_gen_mar_ris_chrg_8per = r16_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR16_2per_gen_mar_ris_chrg_div_port() {
		return r16_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR16_2per_gen_mar_ris_chrg_div_port(BigDecimal r16_2per_gen_mar_ris_chrg_div_port) {
		this.r16_2per_gen_mar_ris_chrg_div_port = r16_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR16_tot_gen_mar_risk_chrg() {
		return r16_tot_gen_mar_risk_chrg;
	}
	public void setR16_tot_gen_mar_risk_chrg(BigDecimal r16_tot_gen_mar_risk_chrg) {
		this.r16_tot_gen_mar_risk_chrg = r16_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR16_tot_mar_ris_chrg() {
		return r16_tot_mar_ris_chrg;
	}
	public void setR16_tot_mar_ris_chrg(BigDecimal r16_tot_mar_ris_chrg) {
		this.r16_tot_mar_ris_chrg = r16_tot_mar_ris_chrg;
	}
	public BigDecimal getR17_market() {
		return r17_market;
	}
	public void setR17_market(BigDecimal r17_market) {
		this.r17_market = r17_market;
	}
	public BigDecimal getR17_gpfsr_nom_amt() {
		return r17_gpfsr_nom_amt;
	}
	public void setR17_gpfsr_nom_amt(BigDecimal r17_gpfsr_nom_amt) {
		this.r17_gpfsr_nom_amt = r17_gpfsr_nom_amt;
	}
	public BigDecimal getR17_gpfsr_pos_att8_per_spe_ris() {
		return r17_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att8_per_spe_ris(BigDecimal r17_gpfsr_pos_att8_per_spe_ris) {
		this.r17_gpfsr_pos_att8_per_spe_ris = r17_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg() {
		return r17_gpfsr_chrg;
	}
	public void setR17_gpfsr_chrg(BigDecimal r17_gpfsr_chrg) {
		this.r17_gpfsr_chrg = r17_gpfsr_chrg;
	}
	public BigDecimal getR17_gpfsr_nom_amt1() {
		return r17_gpfsr_nom_amt1;
	}
	public void setR17_gpfsr_nom_amt1(BigDecimal r17_gpfsr_nom_amt1) {
		this.r17_gpfsr_nom_amt1 = r17_gpfsr_nom_amt1;
	}
	public BigDecimal getR17_gpfsr_pos_att4_per_spe_ris() {
		return r17_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att4_per_spe_ris(BigDecimal r17_gpfsr_pos_att4_per_spe_ris) {
		this.r17_gpfsr_pos_att4_per_spe_ris = r17_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg1() {
		return r17_gpfsr_chrg1;
	}
	public void setR17_gpfsr_chrg1(BigDecimal r17_gpfsr_chrg1) {
		this.r17_gpfsr_chrg1 = r17_gpfsr_chrg1;
	}
	public BigDecimal getR17_gpfsr_nom_amt2() {
		return r17_gpfsr_nom_amt2;
	}
	public void setR17_gpfsr_nom_amt2(BigDecimal r17_gpfsr_nom_amt2) {
		this.r17_gpfsr_nom_amt2 = r17_gpfsr_nom_amt2;
	}
	public BigDecimal getR17_gpfsr_pos_att2_per_spe_ris() {
		return r17_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att2_per_spe_ris(BigDecimal r17_gpfsr_pos_att2_per_spe_ris) {
		this.r17_gpfsr_pos_att2_per_spe_ris = r17_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg2() {
		return r17_gpfsr_chrg2;
	}
	public void setR17_gpfsr_chrg2(BigDecimal r17_gpfsr_chrg2) {
		this.r17_gpfsr_chrg2 = r17_gpfsr_chrg2;
	}
	public BigDecimal getR17_tot_spe_ris_chrg() {
		return r17_tot_spe_ris_chrg;
	}
	public void setR17_tot_spe_ris_chrg(BigDecimal r17_tot_spe_ris_chrg) {
		this.r17_tot_spe_ris_chrg = r17_tot_spe_ris_chrg;
	}
	public BigDecimal getR17_net_pos_gen_mar_ris() {
		return r17_net_pos_gen_mar_ris;
	}
	public void setR17_net_pos_gen_mar_ris(BigDecimal r17_net_pos_gen_mar_ris) {
		this.r17_net_pos_gen_mar_ris = r17_net_pos_gen_mar_ris;
	}
	public BigDecimal getR17_gen_mar_ris_chrg_8per() {
		return r17_gen_mar_ris_chrg_8per;
	}
	public void setR17_gen_mar_ris_chrg_8per(BigDecimal r17_gen_mar_ris_chrg_8per) {
		this.r17_gen_mar_ris_chrg_8per = r17_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR17_2per_gen_mar_ris_chrg_div_port() {
		return r17_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR17_2per_gen_mar_ris_chrg_div_port(BigDecimal r17_2per_gen_mar_ris_chrg_div_port) {
		this.r17_2per_gen_mar_ris_chrg_div_port = r17_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR17_tot_gen_mar_risk_chrg() {
		return r17_tot_gen_mar_risk_chrg;
	}
	public void setR17_tot_gen_mar_risk_chrg(BigDecimal r17_tot_gen_mar_risk_chrg) {
		this.r17_tot_gen_mar_risk_chrg = r17_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR17_tot_mar_ris_chrg() {
		return r17_tot_mar_ris_chrg;
	}
	public void setR17_tot_mar_ris_chrg(BigDecimal r17_tot_mar_ris_chrg) {
		this.r17_tot_mar_ris_chrg = r17_tot_mar_ris_chrg;
	}
	public BigDecimal getR18_market() {
		return r18_market;
	}
	public void setR18_market(BigDecimal r18_market) {
		this.r18_market = r18_market;
	}
	public BigDecimal getR18_gpfsr_nom_amt() {
		return r18_gpfsr_nom_amt;
	}
	public void setR18_gpfsr_nom_amt(BigDecimal r18_gpfsr_nom_amt) {
		this.r18_gpfsr_nom_amt = r18_gpfsr_nom_amt;
	}
	public BigDecimal getR18_gpfsr_pos_att8_per_spe_ris() {
		return r18_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att8_per_spe_ris(BigDecimal r18_gpfsr_pos_att8_per_spe_ris) {
		this.r18_gpfsr_pos_att8_per_spe_ris = r18_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg() {
		return r18_gpfsr_chrg;
	}
	public void setR18_gpfsr_chrg(BigDecimal r18_gpfsr_chrg) {
		this.r18_gpfsr_chrg = r18_gpfsr_chrg;
	}
	public BigDecimal getR18_gpfsr_nom_amt1() {
		return r18_gpfsr_nom_amt1;
	}
	public void setR18_gpfsr_nom_amt1(BigDecimal r18_gpfsr_nom_amt1) {
		this.r18_gpfsr_nom_amt1 = r18_gpfsr_nom_amt1;
	}
	public BigDecimal getR18_gpfsr_pos_att4_per_spe_ris() {
		return r18_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att4_per_spe_ris(BigDecimal r18_gpfsr_pos_att4_per_spe_ris) {
		this.r18_gpfsr_pos_att4_per_spe_ris = r18_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg1() {
		return r18_gpfsr_chrg1;
	}
	public void setR18_gpfsr_chrg1(BigDecimal r18_gpfsr_chrg1) {
		this.r18_gpfsr_chrg1 = r18_gpfsr_chrg1;
	}
	public BigDecimal getR18_gpfsr_nom_amt2() {
		return r18_gpfsr_nom_amt2;
	}
	public void setR18_gpfsr_nom_amt2(BigDecimal r18_gpfsr_nom_amt2) {
		this.r18_gpfsr_nom_amt2 = r18_gpfsr_nom_amt2;
	}
	public BigDecimal getR18_gpfsr_pos_att2_per_spe_ris() {
		return r18_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att2_per_spe_ris(BigDecimal r18_gpfsr_pos_att2_per_spe_ris) {
		this.r18_gpfsr_pos_att2_per_spe_ris = r18_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg2() {
		return r18_gpfsr_chrg2;
	}
	public void setR18_gpfsr_chrg2(BigDecimal r18_gpfsr_chrg2) {
		this.r18_gpfsr_chrg2 = r18_gpfsr_chrg2;
	}
	public BigDecimal getR18_tot_spe_ris_chrg() {
		return r18_tot_spe_ris_chrg;
	}
	public void setR18_tot_spe_ris_chrg(BigDecimal r18_tot_spe_ris_chrg) {
		this.r18_tot_spe_ris_chrg = r18_tot_spe_ris_chrg;
	}
	public BigDecimal getR18_net_pos_gen_mar_ris() {
		return r18_net_pos_gen_mar_ris;
	}
	public void setR18_net_pos_gen_mar_ris(BigDecimal r18_net_pos_gen_mar_ris) {
		this.r18_net_pos_gen_mar_ris = r18_net_pos_gen_mar_ris;
	}
	public BigDecimal getR18_gen_mar_ris_chrg_8per() {
		return r18_gen_mar_ris_chrg_8per;
	}
	public void setR18_gen_mar_ris_chrg_8per(BigDecimal r18_gen_mar_ris_chrg_8per) {
		this.r18_gen_mar_ris_chrg_8per = r18_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR18_2per_gen_mar_ris_chrg_div_port() {
		return r18_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR18_2per_gen_mar_ris_chrg_div_port(BigDecimal r18_2per_gen_mar_ris_chrg_div_port) {
		this.r18_2per_gen_mar_ris_chrg_div_port = r18_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR18_tot_gen_mar_risk_chrg() {
		return r18_tot_gen_mar_risk_chrg;
	}
	public void setR18_tot_gen_mar_risk_chrg(BigDecimal r18_tot_gen_mar_risk_chrg) {
		this.r18_tot_gen_mar_risk_chrg = r18_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR18_tot_mar_ris_chrg() {
		return r18_tot_mar_ris_chrg;
	}
	public void setR18_tot_mar_ris_chrg(BigDecimal r18_tot_mar_ris_chrg) {
		this.r18_tot_mar_ris_chrg = r18_tot_mar_ris_chrg;
	}
	public BigDecimal getR19_market() {
		return r19_market;
	}
	public void setR19_market(BigDecimal r19_market) {
		this.r19_market = r19_market;
	}
	public BigDecimal getR19_gpfsr_nom_amt() {
		return r19_gpfsr_nom_amt;
	}
	public void setR19_gpfsr_nom_amt(BigDecimal r19_gpfsr_nom_amt) {
		this.r19_gpfsr_nom_amt = r19_gpfsr_nom_amt;
	}
	public BigDecimal getR19_gpfsr_pos_att8_per_spe_ris() {
		return r19_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att8_per_spe_ris(BigDecimal r19_gpfsr_pos_att8_per_spe_ris) {
		this.r19_gpfsr_pos_att8_per_spe_ris = r19_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg() {
		return r19_gpfsr_chrg;
	}
	public void setR19_gpfsr_chrg(BigDecimal r19_gpfsr_chrg) {
		this.r19_gpfsr_chrg = r19_gpfsr_chrg;
	}
	public BigDecimal getR19_gpfsr_nom_amt1() {
		return r19_gpfsr_nom_amt1;
	}
	public void setR19_gpfsr_nom_amt1(BigDecimal r19_gpfsr_nom_amt1) {
		this.r19_gpfsr_nom_amt1 = r19_gpfsr_nom_amt1;
	}
	public BigDecimal getR19_gpfsr_pos_att4_per_spe_ris() {
		return r19_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att4_per_spe_ris(BigDecimal r19_gpfsr_pos_att4_per_spe_ris) {
		this.r19_gpfsr_pos_att4_per_spe_ris = r19_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg1() {
		return r19_gpfsr_chrg1;
	}
	public void setR19_gpfsr_chrg1(BigDecimal r19_gpfsr_chrg1) {
		this.r19_gpfsr_chrg1 = r19_gpfsr_chrg1;
	}
	public BigDecimal getR19_gpfsr_nom_amt2() {
		return r19_gpfsr_nom_amt2;
	}
	public void setR19_gpfsr_nom_amt2(BigDecimal r19_gpfsr_nom_amt2) {
		this.r19_gpfsr_nom_amt2 = r19_gpfsr_nom_amt2;
	}
	public BigDecimal getR19_gpfsr_pos_att2_per_spe_ris() {
		return r19_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att2_per_spe_ris(BigDecimal r19_gpfsr_pos_att2_per_spe_ris) {
		this.r19_gpfsr_pos_att2_per_spe_ris = r19_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg2() {
		return r19_gpfsr_chrg2;
	}
	public void setR19_gpfsr_chrg2(BigDecimal r19_gpfsr_chrg2) {
		this.r19_gpfsr_chrg2 = r19_gpfsr_chrg2;
	}
	public BigDecimal getR19_tot_spe_ris_chrg() {
		return r19_tot_spe_ris_chrg;
	}
	public void setR19_tot_spe_ris_chrg(BigDecimal r19_tot_spe_ris_chrg) {
		this.r19_tot_spe_ris_chrg = r19_tot_spe_ris_chrg;
	}
	public BigDecimal getR19_net_pos_gen_mar_ris() {
		return r19_net_pos_gen_mar_ris;
	}
	public void setR19_net_pos_gen_mar_ris(BigDecimal r19_net_pos_gen_mar_ris) {
		this.r19_net_pos_gen_mar_ris = r19_net_pos_gen_mar_ris;
	}
	public BigDecimal getR19_gen_mar_ris_chrg_8per() {
		return r19_gen_mar_ris_chrg_8per;
	}
	public void setR19_gen_mar_ris_chrg_8per(BigDecimal r19_gen_mar_ris_chrg_8per) {
		this.r19_gen_mar_ris_chrg_8per = r19_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR19_2per_gen_mar_ris_chrg_div_port() {
		return r19_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR19_2per_gen_mar_ris_chrg_div_port(BigDecimal r19_2per_gen_mar_ris_chrg_div_port) {
		this.r19_2per_gen_mar_ris_chrg_div_port = r19_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR19_tot_gen_mar_risk_chrg() {
		return r19_tot_gen_mar_risk_chrg;
	}
	public void setR19_tot_gen_mar_risk_chrg(BigDecimal r19_tot_gen_mar_risk_chrg) {
		this.r19_tot_gen_mar_risk_chrg = r19_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR19_tot_mar_ris_chrg() {
		return r19_tot_mar_ris_chrg;
	}
	public void setR19_tot_mar_ris_chrg(BigDecimal r19_tot_mar_ris_chrg) {
		this.r19_tot_mar_ris_chrg = r19_tot_mar_ris_chrg;
	}
	public BigDecimal getR20_market() {
		return r20_market;
	}
	public void setR20_market(BigDecimal r20_market) {
		this.r20_market = r20_market;
	}
	public BigDecimal getR20_gpfsr_nom_amt() {
		return r20_gpfsr_nom_amt;
	}
	public void setR20_gpfsr_nom_amt(BigDecimal r20_gpfsr_nom_amt) {
		this.r20_gpfsr_nom_amt = r20_gpfsr_nom_amt;
	}
	public BigDecimal getR20_gpfsr_pos_att8_per_spe_ris() {
		return r20_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att8_per_spe_ris(BigDecimal r20_gpfsr_pos_att8_per_spe_ris) {
		this.r20_gpfsr_pos_att8_per_spe_ris = r20_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg() {
		return r20_gpfsr_chrg;
	}
	public void setR20_gpfsr_chrg(BigDecimal r20_gpfsr_chrg) {
		this.r20_gpfsr_chrg = r20_gpfsr_chrg;
	}
	public BigDecimal getR20_gpfsr_nom_amt1() {
		return r20_gpfsr_nom_amt1;
	}
	public void setR20_gpfsr_nom_amt1(BigDecimal r20_gpfsr_nom_amt1) {
		this.r20_gpfsr_nom_amt1 = r20_gpfsr_nom_amt1;
	}
	public BigDecimal getR20_gpfsr_pos_att4_per_spe_ris() {
		return r20_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att4_per_spe_ris(BigDecimal r20_gpfsr_pos_att4_per_spe_ris) {
		this.r20_gpfsr_pos_att4_per_spe_ris = r20_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg1() {
		return r20_gpfsr_chrg1;
	}
	public void setR20_gpfsr_chrg1(BigDecimal r20_gpfsr_chrg1) {
		this.r20_gpfsr_chrg1 = r20_gpfsr_chrg1;
	}
	public BigDecimal getR20_gpfsr_nom_amt2() {
		return r20_gpfsr_nom_amt2;
	}
	public void setR20_gpfsr_nom_amt2(BigDecimal r20_gpfsr_nom_amt2) {
		this.r20_gpfsr_nom_amt2 = r20_gpfsr_nom_amt2;
	}
	public BigDecimal getR20_gpfsr_pos_att2_per_spe_ris() {
		return r20_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att2_per_spe_ris(BigDecimal r20_gpfsr_pos_att2_per_spe_ris) {
		this.r20_gpfsr_pos_att2_per_spe_ris = r20_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg2() {
		return r20_gpfsr_chrg2;
	}
	public void setR20_gpfsr_chrg2(BigDecimal r20_gpfsr_chrg2) {
		this.r20_gpfsr_chrg2 = r20_gpfsr_chrg2;
	}
	public BigDecimal getR20_tot_spe_ris_chrg() {
		return r20_tot_spe_ris_chrg;
	}
	public void setR20_tot_spe_ris_chrg(BigDecimal r20_tot_spe_ris_chrg) {
		this.r20_tot_spe_ris_chrg = r20_tot_spe_ris_chrg;
	}
	public BigDecimal getR20_net_pos_gen_mar_ris() {
		return r20_net_pos_gen_mar_ris;
	}
	public void setR20_net_pos_gen_mar_ris(BigDecimal r20_net_pos_gen_mar_ris) {
		this.r20_net_pos_gen_mar_ris = r20_net_pos_gen_mar_ris;
	}
	public BigDecimal getR20_gen_mar_ris_chrg_8per() {
		return r20_gen_mar_ris_chrg_8per;
	}
	public void setR20_gen_mar_ris_chrg_8per(BigDecimal r20_gen_mar_ris_chrg_8per) {
		this.r20_gen_mar_ris_chrg_8per = r20_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR20_2per_gen_mar_ris_chrg_div_port() {
		return r20_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR20_2per_gen_mar_ris_chrg_div_port(BigDecimal r20_2per_gen_mar_ris_chrg_div_port) {
		this.r20_2per_gen_mar_ris_chrg_div_port = r20_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR20_tot_gen_mar_risk_chrg() {
		return r20_tot_gen_mar_risk_chrg;
	}
	public void setR20_tot_gen_mar_risk_chrg(BigDecimal r20_tot_gen_mar_risk_chrg) {
		this.r20_tot_gen_mar_risk_chrg = r20_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR20_tot_mar_ris_chrg() {
		return r20_tot_mar_ris_chrg;
	}
	public void setR20_tot_mar_ris_chrg(BigDecimal r20_tot_mar_ris_chrg) {
		this.r20_tot_mar_ris_chrg = r20_tot_mar_ris_chrg;
	}
	public BigDecimal getR21_market() {
		return r21_market;
	}
	public void setR21_market(BigDecimal r21_market) {
		this.r21_market = r21_market;
	}
	public BigDecimal getR21_gpfsr_nom_amt() {
		return r21_gpfsr_nom_amt;
	}
	public void setR21_gpfsr_nom_amt(BigDecimal r21_gpfsr_nom_amt) {
		this.r21_gpfsr_nom_amt = r21_gpfsr_nom_amt;
	}
	public BigDecimal getR21_gpfsr_pos_att8_per_spe_ris() {
		return r21_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att8_per_spe_ris(BigDecimal r21_gpfsr_pos_att8_per_spe_ris) {
		this.r21_gpfsr_pos_att8_per_spe_ris = r21_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg() {
		return r21_gpfsr_chrg;
	}
	public void setR21_gpfsr_chrg(BigDecimal r21_gpfsr_chrg) {
		this.r21_gpfsr_chrg = r21_gpfsr_chrg;
	}
	public BigDecimal getR21_gpfsr_nom_amt1() {
		return r21_gpfsr_nom_amt1;
	}
	public void setR21_gpfsr_nom_amt1(BigDecimal r21_gpfsr_nom_amt1) {
		this.r21_gpfsr_nom_amt1 = r21_gpfsr_nom_amt1;
	}
	public BigDecimal getR21_gpfsr_pos_att4_per_spe_ris() {
		return r21_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att4_per_spe_ris(BigDecimal r21_gpfsr_pos_att4_per_spe_ris) {
		this.r21_gpfsr_pos_att4_per_spe_ris = r21_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg1() {
		return r21_gpfsr_chrg1;
	}
	public void setR21_gpfsr_chrg1(BigDecimal r21_gpfsr_chrg1) {
		this.r21_gpfsr_chrg1 = r21_gpfsr_chrg1;
	}
	public BigDecimal getR21_gpfsr_nom_amt2() {
		return r21_gpfsr_nom_amt2;
	}
	public void setR21_gpfsr_nom_amt2(BigDecimal r21_gpfsr_nom_amt2) {
		this.r21_gpfsr_nom_amt2 = r21_gpfsr_nom_amt2;
	}
	public BigDecimal getR21_gpfsr_pos_att2_per_spe_ris() {
		return r21_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att2_per_spe_ris(BigDecimal r21_gpfsr_pos_att2_per_spe_ris) {
		this.r21_gpfsr_pos_att2_per_spe_ris = r21_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg2() {
		return r21_gpfsr_chrg2;
	}
	public void setR21_gpfsr_chrg2(BigDecimal r21_gpfsr_chrg2) {
		this.r21_gpfsr_chrg2 = r21_gpfsr_chrg2;
	}
	public BigDecimal getR21_tot_spe_ris_chrg() {
		return r21_tot_spe_ris_chrg;
	}
	public void setR21_tot_spe_ris_chrg(BigDecimal r21_tot_spe_ris_chrg) {
		this.r21_tot_spe_ris_chrg = r21_tot_spe_ris_chrg;
	}
	public BigDecimal getR21_net_pos_gen_mar_ris() {
		return r21_net_pos_gen_mar_ris;
	}
	public void setR21_net_pos_gen_mar_ris(BigDecimal r21_net_pos_gen_mar_ris) {
		this.r21_net_pos_gen_mar_ris = r21_net_pos_gen_mar_ris;
	}
	public BigDecimal getR21_gen_mar_ris_chrg_8per() {
		return r21_gen_mar_ris_chrg_8per;
	}
	public void setR21_gen_mar_ris_chrg_8per(BigDecimal r21_gen_mar_ris_chrg_8per) {
		this.r21_gen_mar_ris_chrg_8per = r21_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR21_2per_gen_mar_ris_chrg_div_port() {
		return r21_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR21_2per_gen_mar_ris_chrg_div_port(BigDecimal r21_2per_gen_mar_ris_chrg_div_port) {
		this.r21_2per_gen_mar_ris_chrg_div_port = r21_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR21_tot_gen_mar_risk_chrg() {
		return r21_tot_gen_mar_risk_chrg;
	}
	public void setR21_tot_gen_mar_risk_chrg(BigDecimal r21_tot_gen_mar_risk_chrg) {
		this.r21_tot_gen_mar_risk_chrg = r21_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR21_tot_mar_ris_chrg() {
		return r21_tot_mar_ris_chrg;
	}
	public void setR21_tot_mar_ris_chrg(BigDecimal r21_tot_mar_ris_chrg) {
		this.r21_tot_mar_ris_chrg = r21_tot_mar_ris_chrg;
	}
	public BigDecimal getR22_market() {
		return r22_market;
	}
	public void setR22_market(BigDecimal r22_market) {
		this.r22_market = r22_market;
	}
	public BigDecimal getR22_gpfsr_nom_amt() {
		return r22_gpfsr_nom_amt;
	}
	public void setR22_gpfsr_nom_amt(BigDecimal r22_gpfsr_nom_amt) {
		this.r22_gpfsr_nom_amt = r22_gpfsr_nom_amt;
	}
	public BigDecimal getR22_gpfsr_pos_att8_per_spe_ris() {
		return r22_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att8_per_spe_ris(BigDecimal r22_gpfsr_pos_att8_per_spe_ris) {
		this.r22_gpfsr_pos_att8_per_spe_ris = r22_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg() {
		return r22_gpfsr_chrg;
	}
	public void setR22_gpfsr_chrg(BigDecimal r22_gpfsr_chrg) {
		this.r22_gpfsr_chrg = r22_gpfsr_chrg;
	}
	public BigDecimal getR22_gpfsr_nom_amt1() {
		return r22_gpfsr_nom_amt1;
	}
	public void setR22_gpfsr_nom_amt1(BigDecimal r22_gpfsr_nom_amt1) {
		this.r22_gpfsr_nom_amt1 = r22_gpfsr_nom_amt1;
	}
	public BigDecimal getR22_gpfsr_pos_att4_per_spe_ris() {
		return r22_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att4_per_spe_ris(BigDecimal r22_gpfsr_pos_att4_per_spe_ris) {
		this.r22_gpfsr_pos_att4_per_spe_ris = r22_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg1() {
		return r22_gpfsr_chrg1;
	}
	public void setR22_gpfsr_chrg1(BigDecimal r22_gpfsr_chrg1) {
		this.r22_gpfsr_chrg1 = r22_gpfsr_chrg1;
	}
	public BigDecimal getR22_gpfsr_nom_amt2() {
		return r22_gpfsr_nom_amt2;
	}
	public void setR22_gpfsr_nom_amt2(BigDecimal r22_gpfsr_nom_amt2) {
		this.r22_gpfsr_nom_amt2 = r22_gpfsr_nom_amt2;
	}
	public BigDecimal getR22_gpfsr_pos_att2_per_spe_ris() {
		return r22_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att2_per_spe_ris(BigDecimal r22_gpfsr_pos_att2_per_spe_ris) {
		this.r22_gpfsr_pos_att2_per_spe_ris = r22_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg2() {
		return r22_gpfsr_chrg2;
	}
	public void setR22_gpfsr_chrg2(BigDecimal r22_gpfsr_chrg2) {
		this.r22_gpfsr_chrg2 = r22_gpfsr_chrg2;
	}
	public BigDecimal getR22_tot_spe_ris_chrg() {
		return r22_tot_spe_ris_chrg;
	}
	public void setR22_tot_spe_ris_chrg(BigDecimal r22_tot_spe_ris_chrg) {
		this.r22_tot_spe_ris_chrg = r22_tot_spe_ris_chrg;
	}
	public BigDecimal getR22_net_pos_gen_mar_ris() {
		return r22_net_pos_gen_mar_ris;
	}
	public void setR22_net_pos_gen_mar_ris(BigDecimal r22_net_pos_gen_mar_ris) {
		this.r22_net_pos_gen_mar_ris = r22_net_pos_gen_mar_ris;
	}
	public BigDecimal getR22_gen_mar_ris_chrg_8per() {
		return r22_gen_mar_ris_chrg_8per;
	}
	public void setR22_gen_mar_ris_chrg_8per(BigDecimal r22_gen_mar_ris_chrg_8per) {
		this.r22_gen_mar_ris_chrg_8per = r22_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR22_2per_gen_mar_ris_chrg_div_port() {
		return r22_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR22_2per_gen_mar_ris_chrg_div_port(BigDecimal r22_2per_gen_mar_ris_chrg_div_port) {
		this.r22_2per_gen_mar_ris_chrg_div_port = r22_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR22_tot_gen_mar_risk_chrg() {
		return r22_tot_gen_mar_risk_chrg;
	}
	public void setR22_tot_gen_mar_risk_chrg(BigDecimal r22_tot_gen_mar_risk_chrg) {
		this.r22_tot_gen_mar_risk_chrg = r22_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR22_tot_mar_ris_chrg() {
		return r22_tot_mar_ris_chrg;
	}
	public void setR22_tot_mar_ris_chrg(BigDecimal r22_tot_mar_ris_chrg) {
		this.r22_tot_mar_ris_chrg = r22_tot_mar_ris_chrg;
	}
	public String getR23_market() {
		return r23_market;
	}
	public void setR23_market(String r23_market) {
		this.r23_market = r23_market;
	}
	public BigDecimal getR23_gpfsr_nom_amt() {
		return r23_gpfsr_nom_amt;
	}
	public void setR23_gpfsr_nom_amt(BigDecimal r23_gpfsr_nom_amt) {
		this.r23_gpfsr_nom_amt = r23_gpfsr_nom_amt;
	}
	public BigDecimal getR23_gpfsr_pos_att8_per_spe_ris() {
		return r23_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att8_per_spe_ris(BigDecimal r23_gpfsr_pos_att8_per_spe_ris) {
		this.r23_gpfsr_pos_att8_per_spe_ris = r23_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg() {
		return r23_gpfsr_chrg;
	}
	public void setR23_gpfsr_chrg(BigDecimal r23_gpfsr_chrg) {
		this.r23_gpfsr_chrg = r23_gpfsr_chrg;
	}
	public BigDecimal getR23_gpfsr_nom_amt1() {
		return r23_gpfsr_nom_amt1;
	}
	public void setR23_gpfsr_nom_amt1(BigDecimal r23_gpfsr_nom_amt1) {
		this.r23_gpfsr_nom_amt1 = r23_gpfsr_nom_amt1;
	}
	public BigDecimal getR23_gpfsr_pos_att4_per_spe_ris() {
		return r23_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att4_per_spe_ris(BigDecimal r23_gpfsr_pos_att4_per_spe_ris) {
		this.r23_gpfsr_pos_att4_per_spe_ris = r23_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg1() {
		return r23_gpfsr_chrg1;
	}
	public void setR23_gpfsr_chrg1(BigDecimal r23_gpfsr_chrg1) {
		this.r23_gpfsr_chrg1 = r23_gpfsr_chrg1;
	}
	public BigDecimal getR23_gpfsr_nom_amt2() {
		return r23_gpfsr_nom_amt2;
	}
	public void setR23_gpfsr_nom_amt2(BigDecimal r23_gpfsr_nom_amt2) {
		this.r23_gpfsr_nom_amt2 = r23_gpfsr_nom_amt2;
	}
	public BigDecimal getR23_gpfsr_pos_att2_per_spe_ris() {
		return r23_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att2_per_spe_ris(BigDecimal r23_gpfsr_pos_att2_per_spe_ris) {
		this.r23_gpfsr_pos_att2_per_spe_ris = r23_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg2() {
		return r23_gpfsr_chrg2;
	}
	public void setR23_gpfsr_chrg2(BigDecimal r23_gpfsr_chrg2) {
		this.r23_gpfsr_chrg2 = r23_gpfsr_chrg2;
	}
	public BigDecimal getR23_tot_spe_ris_chrg() {
		return r23_tot_spe_ris_chrg;
	}
	public void setR23_tot_spe_ris_chrg(BigDecimal r23_tot_spe_ris_chrg) {
		this.r23_tot_spe_ris_chrg = r23_tot_spe_ris_chrg;
	}
	public BigDecimal getR23_net_pos_gen_mar_ris() {
		return r23_net_pos_gen_mar_ris;
	}
	public void setR23_net_pos_gen_mar_ris(BigDecimal r23_net_pos_gen_mar_ris) {
		this.r23_net_pos_gen_mar_ris = r23_net_pos_gen_mar_ris;
	}
	public BigDecimal getR23_gen_mar_ris_chrg_8per() {
		return r23_gen_mar_ris_chrg_8per;
	}
	public void setR23_gen_mar_ris_chrg_8per(BigDecimal r23_gen_mar_ris_chrg_8per) {
		this.r23_gen_mar_ris_chrg_8per = r23_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR23_2per_gen_mar_ris_chrg_div_port() {
		return r23_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR23_2per_gen_mar_ris_chrg_div_port(BigDecimal r23_2per_gen_mar_ris_chrg_div_port) {
		this.r23_2per_gen_mar_ris_chrg_div_port = r23_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR23_tot_gen_mar_risk_chrg() {
		return r23_tot_gen_mar_risk_chrg;
	}
	public void setR23_tot_gen_mar_risk_chrg(BigDecimal r23_tot_gen_mar_risk_chrg) {
		this.r23_tot_gen_mar_risk_chrg = r23_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR23_tot_mar_ris_chrg() {
		return r23_tot_mar_ris_chrg;
	}
	public void setR23_tot_mar_ris_chrg(BigDecimal r23_tot_mar_ris_chrg) {
		this.r23_tot_mar_ris_chrg = r23_tot_mar_ris_chrg;
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


public class M_EPR_Archival_Detail_RowMapper 
        implements RowMapper<M_EPR_Archival_Detail_Entity> {

    @Override
    public M_EPR_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_EPR_Archival_Detail_Entity obj = new M_EPR_Archival_Detail_Entity();

// =========================
// R11
// =========================
obj.setR11_market(rs.getBigDecimal("r11_market"));
obj.setR11_gpfsr_nom_amt(rs.getBigDecimal("r11_gpfsr_nom_amt"));
obj.setR11_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att8_per_spe_ris"));
obj.setR11_gpfsr_chrg(rs.getBigDecimal("r11_gpfsr_chrg"));
obj.setR11_gpfsr_nom_amt1(rs.getBigDecimal("r11_gpfsr_nom_amt1"));
obj.setR11_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att4_per_spe_ris"));
obj.setR11_gpfsr_chrg1(rs.getBigDecimal("r11_gpfsr_chrg1"));
obj.setR11_gpfsr_nom_amt2(rs.getBigDecimal("r11_gpfsr_nom_amt2"));
obj.setR11_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att2_per_spe_ris"));
obj.setR11_gpfsr_chrg2(rs.getBigDecimal("r11_gpfsr_chrg2"));
obj.setR11_tot_spe_ris_chrg(rs.getBigDecimal("r11_tot_spe_ris_chrg"));
obj.setR11_net_pos_gen_mar_ris(rs.getBigDecimal("r11_net_pos_gen_mar_ris"));
obj.setR11_gen_mar_ris_chrg_8per(rs.getBigDecimal("r11_gen_mar_ris_chrg_8per"));
obj.setR11_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r11_2per_gen_mar_ris_chrg_div_port"));
obj.setR11_tot_gen_mar_risk_chrg(rs.getBigDecimal("r11_tot_gen_mar_risk_chrg"));
obj.setR11_tot_mar_ris_chrg(rs.getBigDecimal("r11_tot_mar_ris_chrg"));

// =========================
// R12
// =========================
obj.setR12_market(rs.getBigDecimal("r12_market"));
obj.setR12_gpfsr_nom_amt(rs.getBigDecimal("r12_gpfsr_nom_amt"));
obj.setR12_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att8_per_spe_ris"));
obj.setR12_gpfsr_chrg(rs.getBigDecimal("r12_gpfsr_chrg"));
obj.setR12_gpfsr_nom_amt1(rs.getBigDecimal("r12_gpfsr_nom_amt1"));
obj.setR12_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att4_per_spe_ris"));
obj.setR12_gpfsr_chrg1(rs.getBigDecimal("r12_gpfsr_chrg1"));
obj.setR12_gpfsr_nom_amt2(rs.getBigDecimal("r12_gpfsr_nom_amt2"));
obj.setR12_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att2_per_spe_ris"));
obj.setR12_gpfsr_chrg2(rs.getBigDecimal("r12_gpfsr_chrg2"));
obj.setR12_tot_spe_ris_chrg(rs.getBigDecimal("r12_tot_spe_ris_chrg"));
obj.setR12_net_pos_gen_mar_ris(rs.getBigDecimal("r12_net_pos_gen_mar_ris"));
obj.setR12_gen_mar_ris_chrg_8per(rs.getBigDecimal("r12_gen_mar_ris_chrg_8per"));
obj.setR12_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r12_2per_gen_mar_ris_chrg_div_port"));
obj.setR12_tot_gen_mar_risk_chrg(rs.getBigDecimal("r12_tot_gen_mar_risk_chrg"));
obj.setR12_tot_mar_ris_chrg(rs.getBigDecimal("r12_tot_mar_ris_chrg"));

// =========================
// R13
// =========================
obj.setR13_market(rs.getBigDecimal("r13_market"));
obj.setR13_gpfsr_nom_amt(rs.getBigDecimal("r13_gpfsr_nom_amt"));
obj.setR13_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att8_per_spe_ris"));
obj.setR13_gpfsr_chrg(rs.getBigDecimal("r13_gpfsr_chrg"));
obj.setR13_gpfsr_nom_amt1(rs.getBigDecimal("r13_gpfsr_nom_amt1"));
obj.setR13_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att4_per_spe_ris"));
obj.setR13_gpfsr_chrg1(rs.getBigDecimal("r13_gpfsr_chrg1"));
obj.setR13_gpfsr_nom_amt2(rs.getBigDecimal("r13_gpfsr_nom_amt2"));
obj.setR13_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att2_per_spe_ris"));
obj.setR13_gpfsr_chrg2(rs.getBigDecimal("r13_gpfsr_chrg2"));
obj.setR13_tot_spe_ris_chrg(rs.getBigDecimal("r13_tot_spe_ris_chrg"));
obj.setR13_net_pos_gen_mar_ris(rs.getBigDecimal("r13_net_pos_gen_mar_ris"));
obj.setR13_gen_mar_ris_chrg_8per(rs.getBigDecimal("r13_gen_mar_ris_chrg_8per"));
obj.setR13_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r13_2per_gen_mar_ris_chrg_div_port"));
obj.setR13_tot_gen_mar_risk_chrg(rs.getBigDecimal("r13_tot_gen_mar_risk_chrg"));
obj.setR13_tot_mar_ris_chrg(rs.getBigDecimal("r13_tot_mar_ris_chrg"));

// =========================
// R14
// =========================
obj.setR14_market(rs.getBigDecimal("r14_market"));
obj.setR14_gpfsr_nom_amt(rs.getBigDecimal("r14_gpfsr_nom_amt"));
obj.setR14_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att8_per_spe_ris"));
obj.setR14_gpfsr_chrg(rs.getBigDecimal("r14_gpfsr_chrg"));
obj.setR14_gpfsr_nom_amt1(rs.getBigDecimal("r14_gpfsr_nom_amt1"));
obj.setR14_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att4_per_spe_ris"));
obj.setR14_gpfsr_chrg1(rs.getBigDecimal("r14_gpfsr_chrg1"));
obj.setR14_gpfsr_nom_amt2(rs.getBigDecimal("r14_gpfsr_nom_amt2"));
obj.setR14_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att2_per_spe_ris"));
obj.setR14_gpfsr_chrg2(rs.getBigDecimal("r14_gpfsr_chrg2"));
obj.setR14_tot_spe_ris_chrg(rs.getBigDecimal("r14_tot_spe_ris_chrg"));
obj.setR14_net_pos_gen_mar_ris(rs.getBigDecimal("r14_net_pos_gen_mar_ris"));
obj.setR14_gen_mar_ris_chrg_8per(rs.getBigDecimal("r14_gen_mar_ris_chrg_8per"));
obj.setR14_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r14_2per_gen_mar_ris_chrg_div_port"));
obj.setR14_tot_gen_mar_risk_chrg(rs.getBigDecimal("r14_tot_gen_mar_risk_chrg"));
obj.setR14_tot_mar_ris_chrg(rs.getBigDecimal("r14_tot_mar_ris_chrg"));

// =========================
// R15
// =========================
obj.setR15_market(rs.getBigDecimal("r15_market"));
obj.setR15_gpfsr_nom_amt(rs.getBigDecimal("r15_gpfsr_nom_amt"));
obj.setR15_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att8_per_spe_ris"));
obj.setR15_gpfsr_chrg(rs.getBigDecimal("r15_gpfsr_chrg"));
obj.setR15_gpfsr_nom_amt1(rs.getBigDecimal("r15_gpfsr_nom_amt1"));
obj.setR15_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att4_per_spe_ris"));
obj.setR15_gpfsr_chrg1(rs.getBigDecimal("r15_gpfsr_chrg1"));
obj.setR15_gpfsr_nom_amt2(rs.getBigDecimal("r15_gpfsr_nom_amt2"));
obj.setR15_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att2_per_spe_ris"));
obj.setR15_gpfsr_chrg2(rs.getBigDecimal("r15_gpfsr_chrg2"));
obj.setR15_tot_spe_ris_chrg(rs.getBigDecimal("r15_tot_spe_ris_chrg"));
obj.setR15_net_pos_gen_mar_ris(rs.getBigDecimal("r15_net_pos_gen_mar_ris"));
obj.setR15_gen_mar_ris_chrg_8per(rs.getBigDecimal("r15_gen_mar_ris_chrg_8per"));
obj.setR15_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r15_2per_gen_mar_ris_chrg_div_port"));
obj.setR15_tot_gen_mar_risk_chrg(rs.getBigDecimal("r15_tot_gen_mar_risk_chrg"));
obj.setR15_tot_mar_ris_chrg(rs.getBigDecimal("r15_tot_mar_ris_chrg"));

// =========================
// R16
// =========================
obj.setR16_market(rs.getBigDecimal("r16_market"));
obj.setR16_gpfsr_nom_amt(rs.getBigDecimal("r16_gpfsr_nom_amt"));
obj.setR16_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att8_per_spe_ris"));
obj.setR16_gpfsr_chrg(rs.getBigDecimal("r16_gpfsr_chrg"));
obj.setR16_gpfsr_nom_amt1(rs.getBigDecimal("r16_gpfsr_nom_amt1"));
obj.setR16_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att4_per_spe_ris"));
obj.setR16_gpfsr_chrg1(rs.getBigDecimal("r16_gpfsr_chrg1"));
obj.setR16_gpfsr_nom_amt2(rs.getBigDecimal("r16_gpfsr_nom_amt2"));
obj.setR16_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att2_per_spe_ris"));
obj.setR16_gpfsr_chrg2(rs.getBigDecimal("r16_gpfsr_chrg2"));
obj.setR16_tot_spe_ris_chrg(rs.getBigDecimal("r16_tot_spe_ris_chrg"));
obj.setR16_net_pos_gen_mar_ris(rs.getBigDecimal("r16_net_pos_gen_mar_ris"));
obj.setR16_gen_mar_ris_chrg_8per(rs.getBigDecimal("r16_gen_mar_ris_chrg_8per"));
obj.setR16_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r16_2per_gen_mar_ris_chrg_div_port"));
obj.setR16_tot_gen_mar_risk_chrg(rs.getBigDecimal("r16_tot_gen_mar_risk_chrg"));
obj.setR16_tot_mar_ris_chrg(rs.getBigDecimal("r16_tot_mar_ris_chrg"));

// =========================
// R17
// =========================
obj.setR17_market(rs.getBigDecimal("r17_market"));
obj.setR17_gpfsr_nom_amt(rs.getBigDecimal("r17_gpfsr_nom_amt"));
obj.setR17_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att8_per_spe_ris"));
obj.setR17_gpfsr_chrg(rs.getBigDecimal("r17_gpfsr_chrg"));
obj.setR17_gpfsr_nom_amt1(rs.getBigDecimal("r17_gpfsr_nom_amt1"));
obj.setR17_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att4_per_spe_ris"));
obj.setR17_gpfsr_chrg1(rs.getBigDecimal("r17_gpfsr_chrg1"));
obj.setR17_gpfsr_nom_amt2(rs.getBigDecimal("r17_gpfsr_nom_amt2"));
obj.setR17_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att2_per_spe_ris"));
obj.setR17_gpfsr_chrg2(rs.getBigDecimal("r17_gpfsr_chrg2"));
obj.setR17_tot_spe_ris_chrg(rs.getBigDecimal("r17_tot_spe_ris_chrg"));
obj.setR17_net_pos_gen_mar_ris(rs.getBigDecimal("r17_net_pos_gen_mar_ris"));
obj.setR17_gen_mar_ris_chrg_8per(rs.getBigDecimal("r17_gen_mar_ris_chrg_8per"));
obj.setR17_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r17_2per_gen_mar_ris_chrg_div_port"));
obj.setR17_tot_gen_mar_risk_chrg(rs.getBigDecimal("r17_tot_gen_mar_risk_chrg"));
obj.setR17_tot_mar_ris_chrg(rs.getBigDecimal("r17_tot_mar_ris_chrg"));

// =========================
// R18
// =========================
obj.setR18_market(rs.getBigDecimal("r18_market"));
obj.setR18_gpfsr_nom_amt(rs.getBigDecimal("r18_gpfsr_nom_amt"));
obj.setR18_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att8_per_spe_ris"));
obj.setR18_gpfsr_chrg(rs.getBigDecimal("r18_gpfsr_chrg"));
obj.setR18_gpfsr_nom_amt1(rs.getBigDecimal("r18_gpfsr_nom_amt1"));
obj.setR18_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att4_per_spe_ris"));
obj.setR18_gpfsr_chrg1(rs.getBigDecimal("r18_gpfsr_chrg1"));
obj.setR18_gpfsr_nom_amt2(rs.getBigDecimal("r18_gpfsr_nom_amt2"));
obj.setR18_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att2_per_spe_ris"));
obj.setR18_gpfsr_chrg2(rs.getBigDecimal("r18_gpfsr_chrg2"));
obj.setR18_tot_spe_ris_chrg(rs.getBigDecimal("r18_tot_spe_ris_chrg"));
obj.setR18_net_pos_gen_mar_ris(rs.getBigDecimal("r18_net_pos_gen_mar_ris"));
obj.setR18_gen_mar_ris_chrg_8per(rs.getBigDecimal("r18_gen_mar_ris_chrg_8per"));
obj.setR18_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r18_2per_gen_mar_ris_chrg_div_port"));
obj.setR18_tot_gen_mar_risk_chrg(rs.getBigDecimal("r18_tot_gen_mar_risk_chrg"));
obj.setR18_tot_mar_ris_chrg(rs.getBigDecimal("r18_tot_mar_ris_chrg"));

// =========================
// R19
// =========================
obj.setR19_market(rs.getBigDecimal("r19_market"));
obj.setR19_gpfsr_nom_amt(rs.getBigDecimal("r19_gpfsr_nom_amt"));
obj.setR19_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att8_per_spe_ris"));
obj.setR19_gpfsr_chrg(rs.getBigDecimal("r19_gpfsr_chrg"));
obj.setR19_gpfsr_nom_amt1(rs.getBigDecimal("r19_gpfsr_nom_amt1"));
obj.setR19_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att4_per_spe_ris"));
obj.setR19_gpfsr_chrg1(rs.getBigDecimal("r19_gpfsr_chrg1"));
obj.setR19_gpfsr_nom_amt2(rs.getBigDecimal("r19_gpfsr_nom_amt2"));
obj.setR19_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att2_per_spe_ris"));
obj.setR19_gpfsr_chrg2(rs.getBigDecimal("r19_gpfsr_chrg2"));
obj.setR19_tot_spe_ris_chrg(rs.getBigDecimal("r19_tot_spe_ris_chrg"));
obj.setR19_net_pos_gen_mar_ris(rs.getBigDecimal("r19_net_pos_gen_mar_ris"));
obj.setR19_gen_mar_ris_chrg_8per(rs.getBigDecimal("r19_gen_mar_ris_chrg_8per"));
obj.setR19_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r19_2per_gen_mar_ris_chrg_div_port"));
obj.setR19_tot_gen_mar_risk_chrg(rs.getBigDecimal("r19_tot_gen_mar_risk_chrg"));
obj.setR19_tot_mar_ris_chrg(rs.getBigDecimal("r19_tot_mar_ris_chrg"));

// =========================
// R20
// =========================
obj.setR20_market(rs.getBigDecimal("r20_market"));
obj.setR20_gpfsr_nom_amt(rs.getBigDecimal("r20_gpfsr_nom_amt"));
obj.setR20_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att8_per_spe_ris"));
obj.setR20_gpfsr_chrg(rs.getBigDecimal("r20_gpfsr_chrg"));
obj.setR20_gpfsr_nom_amt1(rs.getBigDecimal("r20_gpfsr_nom_amt1"));
obj.setR20_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att4_per_spe_ris"));
obj.setR20_gpfsr_chrg1(rs.getBigDecimal("r20_gpfsr_chrg1"));
obj.setR20_gpfsr_nom_amt2(rs.getBigDecimal("r20_gpfsr_nom_amt2"));
obj.setR20_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att2_per_spe_ris"));
obj.setR20_gpfsr_chrg2(rs.getBigDecimal("r20_gpfsr_chrg2"));
obj.setR20_tot_spe_ris_chrg(rs.getBigDecimal("r20_tot_spe_ris_chrg"));
obj.setR20_net_pos_gen_mar_ris(rs.getBigDecimal("r20_net_pos_gen_mar_ris"));
obj.setR20_gen_mar_ris_chrg_8per(rs.getBigDecimal("r20_gen_mar_ris_chrg_8per"));
obj.setR20_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r20_2per_gen_mar_ris_chrg_div_port"));
obj.setR20_tot_gen_mar_risk_chrg(rs.getBigDecimal("r20_tot_gen_mar_risk_chrg"));
obj.setR20_tot_mar_ris_chrg(rs.getBigDecimal("r20_tot_mar_ris_chrg"));

// =========================
// R21
// =========================
obj.setR21_market(rs.getBigDecimal("r21_market"));
obj.setR21_gpfsr_nom_amt(rs.getBigDecimal("r21_gpfsr_nom_amt"));
obj.setR21_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att8_per_spe_ris"));
obj.setR21_gpfsr_chrg(rs.getBigDecimal("r21_gpfsr_chrg"));
obj.setR21_gpfsr_nom_amt1(rs.getBigDecimal("r21_gpfsr_nom_amt1"));
obj.setR21_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att4_per_spe_ris"));
obj.setR21_gpfsr_chrg1(rs.getBigDecimal("r21_gpfsr_chrg1"));
obj.setR21_gpfsr_nom_amt2(rs.getBigDecimal("r21_gpfsr_nom_amt2"));
obj.setR21_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att2_per_spe_ris"));
obj.setR21_gpfsr_chrg2(rs.getBigDecimal("r21_gpfsr_chrg2"));
obj.setR21_tot_spe_ris_chrg(rs.getBigDecimal("r21_tot_spe_ris_chrg"));
obj.setR21_net_pos_gen_mar_ris(rs.getBigDecimal("r21_net_pos_gen_mar_ris"));
obj.setR21_gen_mar_ris_chrg_8per(rs.getBigDecimal("r21_gen_mar_ris_chrg_8per"));
obj.setR21_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r21_2per_gen_mar_ris_chrg_div_port"));
obj.setR21_tot_gen_mar_risk_chrg(rs.getBigDecimal("r21_tot_gen_mar_risk_chrg"));
obj.setR21_tot_mar_ris_chrg(rs.getBigDecimal("r21_tot_mar_ris_chrg"));


// =========================
// R22
// =========================
obj.setR22_market(rs.getBigDecimal("r22_market"));
obj.setR22_gpfsr_nom_amt(rs.getBigDecimal("r22_gpfsr_nom_amt"));
obj.setR22_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att8_per_spe_ris"));
obj.setR22_gpfsr_chrg(rs.getBigDecimal("r22_gpfsr_chrg"));
obj.setR22_gpfsr_nom_amt1(rs.getBigDecimal("r22_gpfsr_nom_amt1"));
obj.setR22_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att4_per_spe_ris"));
obj.setR22_gpfsr_chrg1(rs.getBigDecimal("r22_gpfsr_chrg1"));
obj.setR22_gpfsr_nom_amt2(rs.getBigDecimal("r22_gpfsr_nom_amt2"));
obj.setR22_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att2_per_spe_ris"));
obj.setR22_gpfsr_chrg2(rs.getBigDecimal("r22_gpfsr_chrg2"));
obj.setR22_tot_spe_ris_chrg(rs.getBigDecimal("r22_tot_spe_ris_chrg"));
obj.setR22_net_pos_gen_mar_ris(rs.getBigDecimal("r22_net_pos_gen_mar_ris"));
obj.setR22_gen_mar_ris_chrg_8per(rs.getBigDecimal("r22_gen_mar_ris_chrg_8per"));
obj.setR22_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r22_2per_gen_mar_ris_chrg_div_port"));
obj.setR22_tot_gen_mar_risk_chrg(rs.getBigDecimal("r22_tot_gen_mar_risk_chrg"));
obj.setR22_tot_mar_ris_chrg(rs.getBigDecimal("r22_tot_mar_ris_chrg"));

// =========================
// R23
// =========================
obj.setR23_market(rs.getString("r23_market"));
obj.setR23_gpfsr_nom_amt(rs.getBigDecimal("r23_gpfsr_nom_amt"));
obj.setR23_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att8_per_spe_ris"));
obj.setR23_gpfsr_chrg(rs.getBigDecimal("r23_gpfsr_chrg"));
obj.setR23_gpfsr_nom_amt1(rs.getBigDecimal("r23_gpfsr_nom_amt1"));
obj.setR23_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att4_per_spe_ris"));
obj.setR23_gpfsr_chrg1(rs.getBigDecimal("r23_gpfsr_chrg1"));
obj.setR23_gpfsr_nom_amt2(rs.getBigDecimal("r23_gpfsr_nom_amt2"));
obj.setR23_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att2_per_spe_ris"));
obj.setR23_gpfsr_chrg2(rs.getBigDecimal("r23_gpfsr_chrg2"));
obj.setR23_tot_spe_ris_chrg(rs.getBigDecimal("r23_tot_spe_ris_chrg"));
obj.setR23_net_pos_gen_mar_ris(rs.getBigDecimal("r23_net_pos_gen_mar_ris"));
obj.setR23_gen_mar_ris_chrg_8per(rs.getBigDecimal("r23_gen_mar_ris_chrg_8per"));
obj.setR23_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r23_2per_gen_mar_ris_chrg_div_port"));
obj.setR23_tot_gen_mar_risk_chrg(rs.getBigDecimal("r23_tot_gen_mar_risk_chrg"));
obj.setR23_tot_mar_ris_chrg(rs.getBigDecimal("r23_tot_mar_ris_chrg"));


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

public class M_EPR_Archival_Detail_Entity {

   
	private BigDecimal	r11_market;
	private BigDecimal	r11_gpfsr_nom_amt;
	private BigDecimal	r11_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg;
	private BigDecimal	r11_gpfsr_nom_amt1;
	private BigDecimal	r11_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg1;
	private BigDecimal	r11_gpfsr_nom_amt2;
	private BigDecimal	r11_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg2;
	private BigDecimal	r11_tot_spe_ris_chrg;
	private BigDecimal	r11_net_pos_gen_mar_ris;
	private BigDecimal	r11_gen_mar_ris_chrg_8per;
	private BigDecimal	r11_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r11_tot_gen_mar_risk_chrg;
	private BigDecimal	r11_tot_mar_ris_chrg;
	
	private BigDecimal	r12_market;
	private BigDecimal	r12_gpfsr_nom_amt;
	private BigDecimal	r12_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg;
	private BigDecimal	r12_gpfsr_nom_amt1;
	private BigDecimal	r12_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg1;
	private BigDecimal	r12_gpfsr_nom_amt2;
	private BigDecimal	r12_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg2;
	private BigDecimal	r12_tot_spe_ris_chrg;
	private BigDecimal	r12_net_pos_gen_mar_ris;
	private BigDecimal	r12_gen_mar_ris_chrg_8per;
	private BigDecimal	r12_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r12_tot_gen_mar_risk_chrg;
	private BigDecimal	r12_tot_mar_ris_chrg;
	
	private BigDecimal	r13_market;
	private BigDecimal	r13_gpfsr_nom_amt;
	private BigDecimal	r13_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg;
	private BigDecimal	r13_gpfsr_nom_amt1;
	private BigDecimal	r13_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg1;
	private BigDecimal	r13_gpfsr_nom_amt2;
	private BigDecimal	r13_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg2;
	private BigDecimal	r13_tot_spe_ris_chrg;
	private BigDecimal	r13_net_pos_gen_mar_ris;
	private BigDecimal	r13_gen_mar_ris_chrg_8per;
	private BigDecimal	r13_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r13_tot_gen_mar_risk_chrg;
	private BigDecimal	r13_tot_mar_ris_chrg;
	
	private BigDecimal	r14_market;
	private BigDecimal	r14_gpfsr_nom_amt;
	private BigDecimal	r14_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg;
	private BigDecimal	r14_gpfsr_nom_amt1;
	private BigDecimal	r14_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg1;
	private BigDecimal	r14_gpfsr_nom_amt2;
	private BigDecimal	r14_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg2;
	private BigDecimal	r14_tot_spe_ris_chrg;
	private BigDecimal	r14_net_pos_gen_mar_ris;
	private BigDecimal	r14_gen_mar_ris_chrg_8per;
	private BigDecimal	r14_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r14_tot_gen_mar_risk_chrg;
	private BigDecimal	r14_tot_mar_ris_chrg;
	
	private BigDecimal	r15_market;
	private BigDecimal	r15_gpfsr_nom_amt;
	private BigDecimal	r15_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg;
	private BigDecimal	r15_gpfsr_nom_amt1;
	private BigDecimal	r15_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg1;
	private BigDecimal	r15_gpfsr_nom_amt2;
	private BigDecimal	r15_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg2;
	private BigDecimal	r15_tot_spe_ris_chrg;
	private BigDecimal	r15_net_pos_gen_mar_ris;
	private BigDecimal	r15_gen_mar_ris_chrg_8per;
	private BigDecimal	r15_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r15_tot_gen_mar_risk_chrg;
	private BigDecimal	r15_tot_mar_ris_chrg;
	
	private BigDecimal	r16_market;
	private BigDecimal	r16_gpfsr_nom_amt;
	private BigDecimal	r16_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg;
	private BigDecimal	r16_gpfsr_nom_amt1;
	private BigDecimal	r16_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg1;
	private BigDecimal	r16_gpfsr_nom_amt2;
	private BigDecimal	r16_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg2;
	private BigDecimal	r16_tot_spe_ris_chrg;
	private BigDecimal	r16_net_pos_gen_mar_ris;
	private BigDecimal	r16_gen_mar_ris_chrg_8per;
	private BigDecimal	r16_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r16_tot_gen_mar_risk_chrg;
	private BigDecimal	r16_tot_mar_ris_chrg;
	
	private BigDecimal	r17_market;
	private BigDecimal	r17_gpfsr_nom_amt;
	private BigDecimal	r17_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg;
	private BigDecimal	r17_gpfsr_nom_amt1;
	private BigDecimal	r17_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg1;
	private BigDecimal	r17_gpfsr_nom_amt2;
	private BigDecimal	r17_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg2;
	private BigDecimal	r17_tot_spe_ris_chrg;
	private BigDecimal	r17_net_pos_gen_mar_ris;
	private BigDecimal	r17_gen_mar_ris_chrg_8per;
	private BigDecimal	r17_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r17_tot_gen_mar_risk_chrg;
	private BigDecimal	r17_tot_mar_ris_chrg;
	
	private BigDecimal	r18_market;
	private BigDecimal	r18_gpfsr_nom_amt;
	private BigDecimal	r18_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg;
	private BigDecimal	r18_gpfsr_nom_amt1;
	private BigDecimal	r18_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg1;
	private BigDecimal	r18_gpfsr_nom_amt2;
	private BigDecimal	r18_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg2;
	private BigDecimal	r18_tot_spe_ris_chrg;
	private BigDecimal	r18_net_pos_gen_mar_ris;
	private BigDecimal	r18_gen_mar_ris_chrg_8per;
	private BigDecimal	r18_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r18_tot_gen_mar_risk_chrg;
	private BigDecimal	r18_tot_mar_ris_chrg;
	
	private BigDecimal	r19_market;
	private BigDecimal	r19_gpfsr_nom_amt;
	private BigDecimal	r19_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg;
	private BigDecimal	r19_gpfsr_nom_amt1;
	private BigDecimal	r19_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg1;
	private BigDecimal	r19_gpfsr_nom_amt2;
	private BigDecimal	r19_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg2;
	private BigDecimal	r19_tot_spe_ris_chrg;
	private BigDecimal	r19_net_pos_gen_mar_ris;
	private BigDecimal	r19_gen_mar_ris_chrg_8per;
	private BigDecimal	r19_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r19_tot_gen_mar_risk_chrg;
	private BigDecimal	r19_tot_mar_ris_chrg;
	
	private BigDecimal	r20_market;
	private BigDecimal	r20_gpfsr_nom_amt;
	private BigDecimal	r20_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg;
	private BigDecimal	r20_gpfsr_nom_amt1;
	private BigDecimal	r20_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg1;
	private BigDecimal	r20_gpfsr_nom_amt2;
	private BigDecimal	r20_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg2;
	private BigDecimal	r20_tot_spe_ris_chrg;
	private BigDecimal	r20_net_pos_gen_mar_ris;
	private BigDecimal	r20_gen_mar_ris_chrg_8per;
	private BigDecimal	r20_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r20_tot_gen_mar_risk_chrg;
	private BigDecimal	r20_tot_mar_ris_chrg;
	
	private BigDecimal	r21_market;
	private BigDecimal	r21_gpfsr_nom_amt;
	private BigDecimal	r21_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg;
	private BigDecimal	r21_gpfsr_nom_amt1;
	private BigDecimal	r21_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg1;
	private BigDecimal	r21_gpfsr_nom_amt2;
	private BigDecimal	r21_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg2;
	private BigDecimal	r21_tot_spe_ris_chrg;
	private BigDecimal	r21_net_pos_gen_mar_ris;
	private BigDecimal	r21_gen_mar_ris_chrg_8per;
	private BigDecimal	r21_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r21_tot_gen_mar_risk_chrg;
	private BigDecimal	r21_tot_mar_ris_chrg;
	
	private BigDecimal	r22_market;
	private BigDecimal	r22_gpfsr_nom_amt;
	private BigDecimal	r22_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg;
	private BigDecimal	r22_gpfsr_nom_amt1;
	private BigDecimal	r22_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg1;
	private BigDecimal	r22_gpfsr_nom_amt2;
	private BigDecimal	r22_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg2;
	private BigDecimal	r22_tot_spe_ris_chrg;
	private BigDecimal	r22_net_pos_gen_mar_ris;
	private BigDecimal	r22_gen_mar_ris_chrg_8per;
	private BigDecimal	r22_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r22_tot_gen_mar_risk_chrg;
	private BigDecimal	r22_tot_mar_ris_chrg;
	
	private String	r23_market;
	private BigDecimal	r23_gpfsr_nom_amt;
	private BigDecimal	r23_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg;
	private BigDecimal	r23_gpfsr_nom_amt1;
	private BigDecimal	r23_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg1;
	private BigDecimal	r23_gpfsr_nom_amt2;
	private BigDecimal	r23_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg2;
	private BigDecimal	r23_tot_spe_ris_chrg;
	private BigDecimal	r23_net_pos_gen_mar_ris;
	private BigDecimal	r23_gen_mar_ris_chrg_8per;
	private BigDecimal	r23_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r23_tot_gen_mar_risk_chrg;
	private BigDecimal	r23_tot_mar_ris_chrg;
	
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
	
	
public BigDecimal getR11_market() {
		return r11_market;
	}
	public void setR11_market(BigDecimal r11_market) {
		this.r11_market = r11_market;
	}
	public BigDecimal getR11_gpfsr_nom_amt() {
		return r11_gpfsr_nom_amt;
	}
	public void setR11_gpfsr_nom_amt(BigDecimal r11_gpfsr_nom_amt) {
		this.r11_gpfsr_nom_amt = r11_gpfsr_nom_amt;
	}
	public BigDecimal getR11_gpfsr_pos_att8_per_spe_ris() {
		return r11_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att8_per_spe_ris(BigDecimal r11_gpfsr_pos_att8_per_spe_ris) {
		this.r11_gpfsr_pos_att8_per_spe_ris = r11_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg() {
		return r11_gpfsr_chrg;
	}
	public void setR11_gpfsr_chrg(BigDecimal r11_gpfsr_chrg) {
		this.r11_gpfsr_chrg = r11_gpfsr_chrg;
	}
	public BigDecimal getR11_gpfsr_nom_amt1() {
		return r11_gpfsr_nom_amt1;
	}
	public void setR11_gpfsr_nom_amt1(BigDecimal r11_gpfsr_nom_amt1) {
		this.r11_gpfsr_nom_amt1 = r11_gpfsr_nom_amt1;
	}
	public BigDecimal getR11_gpfsr_pos_att4_per_spe_ris() {
		return r11_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att4_per_spe_ris(BigDecimal r11_gpfsr_pos_att4_per_spe_ris) {
		this.r11_gpfsr_pos_att4_per_spe_ris = r11_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg1() {
		return r11_gpfsr_chrg1;
	}
	public void setR11_gpfsr_chrg1(BigDecimal r11_gpfsr_chrg1) {
		this.r11_gpfsr_chrg1 = r11_gpfsr_chrg1;
	}
	public BigDecimal getR11_gpfsr_nom_amt2() {
		return r11_gpfsr_nom_amt2;
	}
	public void setR11_gpfsr_nom_amt2(BigDecimal r11_gpfsr_nom_amt2) {
		this.r11_gpfsr_nom_amt2 = r11_gpfsr_nom_amt2;
	}
	public BigDecimal getR11_gpfsr_pos_att2_per_spe_ris() {
		return r11_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att2_per_spe_ris(BigDecimal r11_gpfsr_pos_att2_per_spe_ris) {
		this.r11_gpfsr_pos_att2_per_spe_ris = r11_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg2() {
		return r11_gpfsr_chrg2;
	}
	public void setR11_gpfsr_chrg2(BigDecimal r11_gpfsr_chrg2) {
		this.r11_gpfsr_chrg2 = r11_gpfsr_chrg2;
	}
	public BigDecimal getR11_tot_spe_ris_chrg() {
		return r11_tot_spe_ris_chrg;
	}
	public void setR11_tot_spe_ris_chrg(BigDecimal r11_tot_spe_ris_chrg) {
		this.r11_tot_spe_ris_chrg = r11_tot_spe_ris_chrg;
	}
	public BigDecimal getR11_net_pos_gen_mar_ris() {
		return r11_net_pos_gen_mar_ris;
	}
	public void setR11_net_pos_gen_mar_ris(BigDecimal r11_net_pos_gen_mar_ris) {
		this.r11_net_pos_gen_mar_ris = r11_net_pos_gen_mar_ris;
	}
	public BigDecimal getR11_gen_mar_ris_chrg_8per() {
		return r11_gen_mar_ris_chrg_8per;
	}
	public void setR11_gen_mar_ris_chrg_8per(BigDecimal r11_gen_mar_ris_chrg_8per) {
		this.r11_gen_mar_ris_chrg_8per = r11_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR11_2per_gen_mar_ris_chrg_div_port() {
		return r11_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR11_2per_gen_mar_ris_chrg_div_port(BigDecimal r11_2per_gen_mar_ris_chrg_div_port) {
		this.r11_2per_gen_mar_ris_chrg_div_port = r11_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR11_tot_gen_mar_risk_chrg() {
		return r11_tot_gen_mar_risk_chrg;
	}
	public void setR11_tot_gen_mar_risk_chrg(BigDecimal r11_tot_gen_mar_risk_chrg) {
		this.r11_tot_gen_mar_risk_chrg = r11_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR11_tot_mar_ris_chrg() {
		return r11_tot_mar_ris_chrg;
	}
	public void setR11_tot_mar_ris_chrg(BigDecimal r11_tot_mar_ris_chrg) {
		this.r11_tot_mar_ris_chrg = r11_tot_mar_ris_chrg;
	}
	public BigDecimal getR12_market() {
		return r12_market;
	}
	public void setR12_market(BigDecimal r12_market) {
		this.r12_market = r12_market;
	}
	public BigDecimal getR12_gpfsr_nom_amt() {
		return r12_gpfsr_nom_amt;
	}
	public void setR12_gpfsr_nom_amt(BigDecimal r12_gpfsr_nom_amt) {
		this.r12_gpfsr_nom_amt = r12_gpfsr_nom_amt;
	}
	public BigDecimal getR12_gpfsr_pos_att8_per_spe_ris() {
		return r12_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att8_per_spe_ris(BigDecimal r12_gpfsr_pos_att8_per_spe_ris) {
		this.r12_gpfsr_pos_att8_per_spe_ris = r12_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg() {
		return r12_gpfsr_chrg;
	}
	public void setR12_gpfsr_chrg(BigDecimal r12_gpfsr_chrg) {
		this.r12_gpfsr_chrg = r12_gpfsr_chrg;
	}
	public BigDecimal getR12_gpfsr_nom_amt1() {
		return r12_gpfsr_nom_amt1;
	}
	public void setR12_gpfsr_nom_amt1(BigDecimal r12_gpfsr_nom_amt1) {
		this.r12_gpfsr_nom_amt1 = r12_gpfsr_nom_amt1;
	}
	public BigDecimal getR12_gpfsr_pos_att4_per_spe_ris() {
		return r12_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att4_per_spe_ris(BigDecimal r12_gpfsr_pos_att4_per_spe_ris) {
		this.r12_gpfsr_pos_att4_per_spe_ris = r12_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg1() {
		return r12_gpfsr_chrg1;
	}
	public void setR12_gpfsr_chrg1(BigDecimal r12_gpfsr_chrg1) {
		this.r12_gpfsr_chrg1 = r12_gpfsr_chrg1;
	}
	public BigDecimal getR12_gpfsr_nom_amt2() {
		return r12_gpfsr_nom_amt2;
	}
	public void setR12_gpfsr_nom_amt2(BigDecimal r12_gpfsr_nom_amt2) {
		this.r12_gpfsr_nom_amt2 = r12_gpfsr_nom_amt2;
	}
	public BigDecimal getR12_gpfsr_pos_att2_per_spe_ris() {
		return r12_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att2_per_spe_ris(BigDecimal r12_gpfsr_pos_att2_per_spe_ris) {
		this.r12_gpfsr_pos_att2_per_spe_ris = r12_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg2() {
		return r12_gpfsr_chrg2;
	}
	public void setR12_gpfsr_chrg2(BigDecimal r12_gpfsr_chrg2) {
		this.r12_gpfsr_chrg2 = r12_gpfsr_chrg2;
	}
	public BigDecimal getR12_tot_spe_ris_chrg() {
		return r12_tot_spe_ris_chrg;
	}
	public void setR12_tot_spe_ris_chrg(BigDecimal r12_tot_spe_ris_chrg) {
		this.r12_tot_spe_ris_chrg = r12_tot_spe_ris_chrg;
	}
	public BigDecimal getR12_net_pos_gen_mar_ris() {
		return r12_net_pos_gen_mar_ris;
	}
	public void setR12_net_pos_gen_mar_ris(BigDecimal r12_net_pos_gen_mar_ris) {
		this.r12_net_pos_gen_mar_ris = r12_net_pos_gen_mar_ris;
	}
	public BigDecimal getR12_gen_mar_ris_chrg_8per() {
		return r12_gen_mar_ris_chrg_8per;
	}
	public void setR12_gen_mar_ris_chrg_8per(BigDecimal r12_gen_mar_ris_chrg_8per) {
		this.r12_gen_mar_ris_chrg_8per = r12_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR12_2per_gen_mar_ris_chrg_div_port() {
		return r12_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR12_2per_gen_mar_ris_chrg_div_port(BigDecimal r12_2per_gen_mar_ris_chrg_div_port) {
		this.r12_2per_gen_mar_ris_chrg_div_port = r12_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR12_tot_gen_mar_risk_chrg() {
		return r12_tot_gen_mar_risk_chrg;
	}
	public void setR12_tot_gen_mar_risk_chrg(BigDecimal r12_tot_gen_mar_risk_chrg) {
		this.r12_tot_gen_mar_risk_chrg = r12_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR12_tot_mar_ris_chrg() {
		return r12_tot_mar_ris_chrg;
	}
	public void setR12_tot_mar_ris_chrg(BigDecimal r12_tot_mar_ris_chrg) {
		this.r12_tot_mar_ris_chrg = r12_tot_mar_ris_chrg;
	}
	public BigDecimal getR13_market() {
		return r13_market;
	}
	public void setR13_market(BigDecimal r13_market) {
		this.r13_market = r13_market;
	}
	public BigDecimal getR13_gpfsr_nom_amt() {
		return r13_gpfsr_nom_amt;
	}
	public void setR13_gpfsr_nom_amt(BigDecimal r13_gpfsr_nom_amt) {
		this.r13_gpfsr_nom_amt = r13_gpfsr_nom_amt;
	}
	public BigDecimal getR13_gpfsr_pos_att8_per_spe_ris() {
		return r13_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att8_per_spe_ris(BigDecimal r13_gpfsr_pos_att8_per_spe_ris) {
		this.r13_gpfsr_pos_att8_per_spe_ris = r13_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg() {
		return r13_gpfsr_chrg;
	}
	public void setR13_gpfsr_chrg(BigDecimal r13_gpfsr_chrg) {
		this.r13_gpfsr_chrg = r13_gpfsr_chrg;
	}
	public BigDecimal getR13_gpfsr_nom_amt1() {
		return r13_gpfsr_nom_amt1;
	}
	public void setR13_gpfsr_nom_amt1(BigDecimal r13_gpfsr_nom_amt1) {
		this.r13_gpfsr_nom_amt1 = r13_gpfsr_nom_amt1;
	}
	public BigDecimal getR13_gpfsr_pos_att4_per_spe_ris() {
		return r13_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att4_per_spe_ris(BigDecimal r13_gpfsr_pos_att4_per_spe_ris) {
		this.r13_gpfsr_pos_att4_per_spe_ris = r13_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg1() {
		return r13_gpfsr_chrg1;
	}
	public void setR13_gpfsr_chrg1(BigDecimal r13_gpfsr_chrg1) {
		this.r13_gpfsr_chrg1 = r13_gpfsr_chrg1;
	}
	public BigDecimal getR13_gpfsr_nom_amt2() {
		return r13_gpfsr_nom_amt2;
	}
	public void setR13_gpfsr_nom_amt2(BigDecimal r13_gpfsr_nom_amt2) {
		this.r13_gpfsr_nom_amt2 = r13_gpfsr_nom_amt2;
	}
	public BigDecimal getR13_gpfsr_pos_att2_per_spe_ris() {
		return r13_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att2_per_spe_ris(BigDecimal r13_gpfsr_pos_att2_per_spe_ris) {
		this.r13_gpfsr_pos_att2_per_spe_ris = r13_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg2() {
		return r13_gpfsr_chrg2;
	}
	public void setR13_gpfsr_chrg2(BigDecimal r13_gpfsr_chrg2) {
		this.r13_gpfsr_chrg2 = r13_gpfsr_chrg2;
	}
	public BigDecimal getR13_tot_spe_ris_chrg() {
		return r13_tot_spe_ris_chrg;
	}
	public void setR13_tot_spe_ris_chrg(BigDecimal r13_tot_spe_ris_chrg) {
		this.r13_tot_spe_ris_chrg = r13_tot_spe_ris_chrg;
	}
	public BigDecimal getR13_net_pos_gen_mar_ris() {
		return r13_net_pos_gen_mar_ris;
	}
	public void setR13_net_pos_gen_mar_ris(BigDecimal r13_net_pos_gen_mar_ris) {
		this.r13_net_pos_gen_mar_ris = r13_net_pos_gen_mar_ris;
	}
	public BigDecimal getR13_gen_mar_ris_chrg_8per() {
		return r13_gen_mar_ris_chrg_8per;
	}
	public void setR13_gen_mar_ris_chrg_8per(BigDecimal r13_gen_mar_ris_chrg_8per) {
		this.r13_gen_mar_ris_chrg_8per = r13_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR13_2per_gen_mar_ris_chrg_div_port() {
		return r13_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR13_2per_gen_mar_ris_chrg_div_port(BigDecimal r13_2per_gen_mar_ris_chrg_div_port) {
		this.r13_2per_gen_mar_ris_chrg_div_port = r13_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR13_tot_gen_mar_risk_chrg() {
		return r13_tot_gen_mar_risk_chrg;
	}
	public void setR13_tot_gen_mar_risk_chrg(BigDecimal r13_tot_gen_mar_risk_chrg) {
		this.r13_tot_gen_mar_risk_chrg = r13_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR13_tot_mar_ris_chrg() {
		return r13_tot_mar_ris_chrg;
	}
	public void setR13_tot_mar_ris_chrg(BigDecimal r13_tot_mar_ris_chrg) {
		this.r13_tot_mar_ris_chrg = r13_tot_mar_ris_chrg;
	}
	public BigDecimal getR14_market() {
		return r14_market;
	}
	public void setR14_market(BigDecimal r14_market) {
		this.r14_market = r14_market;
	}
	public BigDecimal getR14_gpfsr_nom_amt() {
		return r14_gpfsr_nom_amt;
	}
	public void setR14_gpfsr_nom_amt(BigDecimal r14_gpfsr_nom_amt) {
		this.r14_gpfsr_nom_amt = r14_gpfsr_nom_amt;
	}
	public BigDecimal getR14_gpfsr_pos_att8_per_spe_ris() {
		return r14_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att8_per_spe_ris(BigDecimal r14_gpfsr_pos_att8_per_spe_ris) {
		this.r14_gpfsr_pos_att8_per_spe_ris = r14_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg() {
		return r14_gpfsr_chrg;
	}
	public void setR14_gpfsr_chrg(BigDecimal r14_gpfsr_chrg) {
		this.r14_gpfsr_chrg = r14_gpfsr_chrg;
	}
	public BigDecimal getR14_gpfsr_nom_amt1() {
		return r14_gpfsr_nom_amt1;
	}
	public void setR14_gpfsr_nom_amt1(BigDecimal r14_gpfsr_nom_amt1) {
		this.r14_gpfsr_nom_amt1 = r14_gpfsr_nom_amt1;
	}
	public BigDecimal getR14_gpfsr_pos_att4_per_spe_ris() {
		return r14_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att4_per_spe_ris(BigDecimal r14_gpfsr_pos_att4_per_spe_ris) {
		this.r14_gpfsr_pos_att4_per_spe_ris = r14_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg1() {
		return r14_gpfsr_chrg1;
	}
	public void setR14_gpfsr_chrg1(BigDecimal r14_gpfsr_chrg1) {
		this.r14_gpfsr_chrg1 = r14_gpfsr_chrg1;
	}
	public BigDecimal getR14_gpfsr_nom_amt2() {
		return r14_gpfsr_nom_amt2;
	}
	public void setR14_gpfsr_nom_amt2(BigDecimal r14_gpfsr_nom_amt2) {
		this.r14_gpfsr_nom_amt2 = r14_gpfsr_nom_amt2;
	}
	public BigDecimal getR14_gpfsr_pos_att2_per_spe_ris() {
		return r14_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att2_per_spe_ris(BigDecimal r14_gpfsr_pos_att2_per_spe_ris) {
		this.r14_gpfsr_pos_att2_per_spe_ris = r14_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg2() {
		return r14_gpfsr_chrg2;
	}
	public void setR14_gpfsr_chrg2(BigDecimal r14_gpfsr_chrg2) {
		this.r14_gpfsr_chrg2 = r14_gpfsr_chrg2;
	}
	public BigDecimal getR14_tot_spe_ris_chrg() {
		return r14_tot_spe_ris_chrg;
	}
	public void setR14_tot_spe_ris_chrg(BigDecimal r14_tot_spe_ris_chrg) {
		this.r14_tot_spe_ris_chrg = r14_tot_spe_ris_chrg;
	}
	public BigDecimal getR14_net_pos_gen_mar_ris() {
		return r14_net_pos_gen_mar_ris;
	}
	public void setR14_net_pos_gen_mar_ris(BigDecimal r14_net_pos_gen_mar_ris) {
		this.r14_net_pos_gen_mar_ris = r14_net_pos_gen_mar_ris;
	}
	public BigDecimal getR14_gen_mar_ris_chrg_8per() {
		return r14_gen_mar_ris_chrg_8per;
	}
	public void setR14_gen_mar_ris_chrg_8per(BigDecimal r14_gen_mar_ris_chrg_8per) {
		this.r14_gen_mar_ris_chrg_8per = r14_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR14_2per_gen_mar_ris_chrg_div_port() {
		return r14_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR14_2per_gen_mar_ris_chrg_div_port(BigDecimal r14_2per_gen_mar_ris_chrg_div_port) {
		this.r14_2per_gen_mar_ris_chrg_div_port = r14_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR14_tot_gen_mar_risk_chrg() {
		return r14_tot_gen_mar_risk_chrg;
	}
	public void setR14_tot_gen_mar_risk_chrg(BigDecimal r14_tot_gen_mar_risk_chrg) {
		this.r14_tot_gen_mar_risk_chrg = r14_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR14_tot_mar_ris_chrg() {
		return r14_tot_mar_ris_chrg;
	}
	public void setR14_tot_mar_ris_chrg(BigDecimal r14_tot_mar_ris_chrg) {
		this.r14_tot_mar_ris_chrg = r14_tot_mar_ris_chrg;
	}
	public BigDecimal getR15_market() {
		return r15_market;
	}
	public void setR15_market(BigDecimal r15_market) {
		this.r15_market = r15_market;
	}
	public BigDecimal getR15_gpfsr_nom_amt() {
		return r15_gpfsr_nom_amt;
	}
	public void setR15_gpfsr_nom_amt(BigDecimal r15_gpfsr_nom_amt) {
		this.r15_gpfsr_nom_amt = r15_gpfsr_nom_amt;
	}
	public BigDecimal getR15_gpfsr_pos_att8_per_spe_ris() {
		return r15_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att8_per_spe_ris(BigDecimal r15_gpfsr_pos_att8_per_spe_ris) {
		this.r15_gpfsr_pos_att8_per_spe_ris = r15_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg() {
		return r15_gpfsr_chrg;
	}
	public void setR15_gpfsr_chrg(BigDecimal r15_gpfsr_chrg) {
		this.r15_gpfsr_chrg = r15_gpfsr_chrg;
	}
	public BigDecimal getR15_gpfsr_nom_amt1() {
		return r15_gpfsr_nom_amt1;
	}
	public void setR15_gpfsr_nom_amt1(BigDecimal r15_gpfsr_nom_amt1) {
		this.r15_gpfsr_nom_amt1 = r15_gpfsr_nom_amt1;
	}
	public BigDecimal getR15_gpfsr_pos_att4_per_spe_ris() {
		return r15_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att4_per_spe_ris(BigDecimal r15_gpfsr_pos_att4_per_spe_ris) {
		this.r15_gpfsr_pos_att4_per_spe_ris = r15_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg1() {
		return r15_gpfsr_chrg1;
	}
	public void setR15_gpfsr_chrg1(BigDecimal r15_gpfsr_chrg1) {
		this.r15_gpfsr_chrg1 = r15_gpfsr_chrg1;
	}
	public BigDecimal getR15_gpfsr_nom_amt2() {
		return r15_gpfsr_nom_amt2;
	}
	public void setR15_gpfsr_nom_amt2(BigDecimal r15_gpfsr_nom_amt2) {
		this.r15_gpfsr_nom_amt2 = r15_gpfsr_nom_amt2;
	}
	public BigDecimal getR15_gpfsr_pos_att2_per_spe_ris() {
		return r15_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att2_per_spe_ris(BigDecimal r15_gpfsr_pos_att2_per_spe_ris) {
		this.r15_gpfsr_pos_att2_per_spe_ris = r15_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg2() {
		return r15_gpfsr_chrg2;
	}
	public void setR15_gpfsr_chrg2(BigDecimal r15_gpfsr_chrg2) {
		this.r15_gpfsr_chrg2 = r15_gpfsr_chrg2;
	}
	public BigDecimal getR15_tot_spe_ris_chrg() {
		return r15_tot_spe_ris_chrg;
	}
	public void setR15_tot_spe_ris_chrg(BigDecimal r15_tot_spe_ris_chrg) {
		this.r15_tot_spe_ris_chrg = r15_tot_spe_ris_chrg;
	}
	public BigDecimal getR15_net_pos_gen_mar_ris() {
		return r15_net_pos_gen_mar_ris;
	}
	public void setR15_net_pos_gen_mar_ris(BigDecimal r15_net_pos_gen_mar_ris) {
		this.r15_net_pos_gen_mar_ris = r15_net_pos_gen_mar_ris;
	}
	public BigDecimal getR15_gen_mar_ris_chrg_8per() {
		return r15_gen_mar_ris_chrg_8per;
	}
	public void setR15_gen_mar_ris_chrg_8per(BigDecimal r15_gen_mar_ris_chrg_8per) {
		this.r15_gen_mar_ris_chrg_8per = r15_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR15_2per_gen_mar_ris_chrg_div_port() {
		return r15_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR15_2per_gen_mar_ris_chrg_div_port(BigDecimal r15_2per_gen_mar_ris_chrg_div_port) {
		this.r15_2per_gen_mar_ris_chrg_div_port = r15_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR15_tot_gen_mar_risk_chrg() {
		return r15_tot_gen_mar_risk_chrg;
	}
	public void setR15_tot_gen_mar_risk_chrg(BigDecimal r15_tot_gen_mar_risk_chrg) {
		this.r15_tot_gen_mar_risk_chrg = r15_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR15_tot_mar_ris_chrg() {
		return r15_tot_mar_ris_chrg;
	}
	public void setR15_tot_mar_ris_chrg(BigDecimal r15_tot_mar_ris_chrg) {
		this.r15_tot_mar_ris_chrg = r15_tot_mar_ris_chrg;
	}
	public BigDecimal getR16_market() {
		return r16_market;
	}
	public void setR16_market(BigDecimal r16_market) {
		this.r16_market = r16_market;
	}
	public BigDecimal getR16_gpfsr_nom_amt() {
		return r16_gpfsr_nom_amt;
	}
	public void setR16_gpfsr_nom_amt(BigDecimal r16_gpfsr_nom_amt) {
		this.r16_gpfsr_nom_amt = r16_gpfsr_nom_amt;
	}
	public BigDecimal getR16_gpfsr_pos_att8_per_spe_ris() {
		return r16_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att8_per_spe_ris(BigDecimal r16_gpfsr_pos_att8_per_spe_ris) {
		this.r16_gpfsr_pos_att8_per_spe_ris = r16_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg() {
		return r16_gpfsr_chrg;
	}
	public void setR16_gpfsr_chrg(BigDecimal r16_gpfsr_chrg) {
		this.r16_gpfsr_chrg = r16_gpfsr_chrg;
	}
	public BigDecimal getR16_gpfsr_nom_amt1() {
		return r16_gpfsr_nom_amt1;
	}
	public void setR16_gpfsr_nom_amt1(BigDecimal r16_gpfsr_nom_amt1) {
		this.r16_gpfsr_nom_amt1 = r16_gpfsr_nom_amt1;
	}
	public BigDecimal getR16_gpfsr_pos_att4_per_spe_ris() {
		return r16_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att4_per_spe_ris(BigDecimal r16_gpfsr_pos_att4_per_spe_ris) {
		this.r16_gpfsr_pos_att4_per_spe_ris = r16_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg1() {
		return r16_gpfsr_chrg1;
	}
	public void setR16_gpfsr_chrg1(BigDecimal r16_gpfsr_chrg1) {
		this.r16_gpfsr_chrg1 = r16_gpfsr_chrg1;
	}
	public BigDecimal getR16_gpfsr_nom_amt2() {
		return r16_gpfsr_nom_amt2;
	}
	public void setR16_gpfsr_nom_amt2(BigDecimal r16_gpfsr_nom_amt2) {
		this.r16_gpfsr_nom_amt2 = r16_gpfsr_nom_amt2;
	}
	public BigDecimal getR16_gpfsr_pos_att2_per_spe_ris() {
		return r16_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att2_per_spe_ris(BigDecimal r16_gpfsr_pos_att2_per_spe_ris) {
		this.r16_gpfsr_pos_att2_per_spe_ris = r16_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg2() {
		return r16_gpfsr_chrg2;
	}
	public void setR16_gpfsr_chrg2(BigDecimal r16_gpfsr_chrg2) {
		this.r16_gpfsr_chrg2 = r16_gpfsr_chrg2;
	}
	public BigDecimal getR16_tot_spe_ris_chrg() {
		return r16_tot_spe_ris_chrg;
	}
	public void setR16_tot_spe_ris_chrg(BigDecimal r16_tot_spe_ris_chrg) {
		this.r16_tot_spe_ris_chrg = r16_tot_spe_ris_chrg;
	}
	public BigDecimal getR16_net_pos_gen_mar_ris() {
		return r16_net_pos_gen_mar_ris;
	}
	public void setR16_net_pos_gen_mar_ris(BigDecimal r16_net_pos_gen_mar_ris) {
		this.r16_net_pos_gen_mar_ris = r16_net_pos_gen_mar_ris;
	}
	public BigDecimal getR16_gen_mar_ris_chrg_8per() {
		return r16_gen_mar_ris_chrg_8per;
	}
	public void setR16_gen_mar_ris_chrg_8per(BigDecimal r16_gen_mar_ris_chrg_8per) {
		this.r16_gen_mar_ris_chrg_8per = r16_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR16_2per_gen_mar_ris_chrg_div_port() {
		return r16_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR16_2per_gen_mar_ris_chrg_div_port(BigDecimal r16_2per_gen_mar_ris_chrg_div_port) {
		this.r16_2per_gen_mar_ris_chrg_div_port = r16_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR16_tot_gen_mar_risk_chrg() {
		return r16_tot_gen_mar_risk_chrg;
	}
	public void setR16_tot_gen_mar_risk_chrg(BigDecimal r16_tot_gen_mar_risk_chrg) {
		this.r16_tot_gen_mar_risk_chrg = r16_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR16_tot_mar_ris_chrg() {
		return r16_tot_mar_ris_chrg;
	}
	public void setR16_tot_mar_ris_chrg(BigDecimal r16_tot_mar_ris_chrg) {
		this.r16_tot_mar_ris_chrg = r16_tot_mar_ris_chrg;
	}
	public BigDecimal getR17_market() {
		return r17_market;
	}
	public void setR17_market(BigDecimal r17_market) {
		this.r17_market = r17_market;
	}
	public BigDecimal getR17_gpfsr_nom_amt() {
		return r17_gpfsr_nom_amt;
	}
	public void setR17_gpfsr_nom_amt(BigDecimal r17_gpfsr_nom_amt) {
		this.r17_gpfsr_nom_amt = r17_gpfsr_nom_amt;
	}
	public BigDecimal getR17_gpfsr_pos_att8_per_spe_ris() {
		return r17_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att8_per_spe_ris(BigDecimal r17_gpfsr_pos_att8_per_spe_ris) {
		this.r17_gpfsr_pos_att8_per_spe_ris = r17_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg() {
		return r17_gpfsr_chrg;
	}
	public void setR17_gpfsr_chrg(BigDecimal r17_gpfsr_chrg) {
		this.r17_gpfsr_chrg = r17_gpfsr_chrg;
	}
	public BigDecimal getR17_gpfsr_nom_amt1() {
		return r17_gpfsr_nom_amt1;
	}
	public void setR17_gpfsr_nom_amt1(BigDecimal r17_gpfsr_nom_amt1) {
		this.r17_gpfsr_nom_amt1 = r17_gpfsr_nom_amt1;
	}
	public BigDecimal getR17_gpfsr_pos_att4_per_spe_ris() {
		return r17_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att4_per_spe_ris(BigDecimal r17_gpfsr_pos_att4_per_spe_ris) {
		this.r17_gpfsr_pos_att4_per_spe_ris = r17_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg1() {
		return r17_gpfsr_chrg1;
	}
	public void setR17_gpfsr_chrg1(BigDecimal r17_gpfsr_chrg1) {
		this.r17_gpfsr_chrg1 = r17_gpfsr_chrg1;
	}
	public BigDecimal getR17_gpfsr_nom_amt2() {
		return r17_gpfsr_nom_amt2;
	}
	public void setR17_gpfsr_nom_amt2(BigDecimal r17_gpfsr_nom_amt2) {
		this.r17_gpfsr_nom_amt2 = r17_gpfsr_nom_amt2;
	}
	public BigDecimal getR17_gpfsr_pos_att2_per_spe_ris() {
		return r17_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att2_per_spe_ris(BigDecimal r17_gpfsr_pos_att2_per_spe_ris) {
		this.r17_gpfsr_pos_att2_per_spe_ris = r17_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg2() {
		return r17_gpfsr_chrg2;
	}
	public void setR17_gpfsr_chrg2(BigDecimal r17_gpfsr_chrg2) {
		this.r17_gpfsr_chrg2 = r17_gpfsr_chrg2;
	}
	public BigDecimal getR17_tot_spe_ris_chrg() {
		return r17_tot_spe_ris_chrg;
	}
	public void setR17_tot_spe_ris_chrg(BigDecimal r17_tot_spe_ris_chrg) {
		this.r17_tot_spe_ris_chrg = r17_tot_spe_ris_chrg;
	}
	public BigDecimal getR17_net_pos_gen_mar_ris() {
		return r17_net_pos_gen_mar_ris;
	}
	public void setR17_net_pos_gen_mar_ris(BigDecimal r17_net_pos_gen_mar_ris) {
		this.r17_net_pos_gen_mar_ris = r17_net_pos_gen_mar_ris;
	}
	public BigDecimal getR17_gen_mar_ris_chrg_8per() {
		return r17_gen_mar_ris_chrg_8per;
	}
	public void setR17_gen_mar_ris_chrg_8per(BigDecimal r17_gen_mar_ris_chrg_8per) {
		this.r17_gen_mar_ris_chrg_8per = r17_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR17_2per_gen_mar_ris_chrg_div_port() {
		return r17_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR17_2per_gen_mar_ris_chrg_div_port(BigDecimal r17_2per_gen_mar_ris_chrg_div_port) {
		this.r17_2per_gen_mar_ris_chrg_div_port = r17_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR17_tot_gen_mar_risk_chrg() {
		return r17_tot_gen_mar_risk_chrg;
	}
	public void setR17_tot_gen_mar_risk_chrg(BigDecimal r17_tot_gen_mar_risk_chrg) {
		this.r17_tot_gen_mar_risk_chrg = r17_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR17_tot_mar_ris_chrg() {
		return r17_tot_mar_ris_chrg;
	}
	public void setR17_tot_mar_ris_chrg(BigDecimal r17_tot_mar_ris_chrg) {
		this.r17_tot_mar_ris_chrg = r17_tot_mar_ris_chrg;
	}
	public BigDecimal getR18_market() {
		return r18_market;
	}
	public void setR18_market(BigDecimal r18_market) {
		this.r18_market = r18_market;
	}
	public BigDecimal getR18_gpfsr_nom_amt() {
		return r18_gpfsr_nom_amt;
	}
	public void setR18_gpfsr_nom_amt(BigDecimal r18_gpfsr_nom_amt) {
		this.r18_gpfsr_nom_amt = r18_gpfsr_nom_amt;
	}
	public BigDecimal getR18_gpfsr_pos_att8_per_spe_ris() {
		return r18_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att8_per_spe_ris(BigDecimal r18_gpfsr_pos_att8_per_spe_ris) {
		this.r18_gpfsr_pos_att8_per_spe_ris = r18_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg() {
		return r18_gpfsr_chrg;
	}
	public void setR18_gpfsr_chrg(BigDecimal r18_gpfsr_chrg) {
		this.r18_gpfsr_chrg = r18_gpfsr_chrg;
	}
	public BigDecimal getR18_gpfsr_nom_amt1() {
		return r18_gpfsr_nom_amt1;
	}
	public void setR18_gpfsr_nom_amt1(BigDecimal r18_gpfsr_nom_amt1) {
		this.r18_gpfsr_nom_amt1 = r18_gpfsr_nom_amt1;
	}
	public BigDecimal getR18_gpfsr_pos_att4_per_spe_ris() {
		return r18_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att4_per_spe_ris(BigDecimal r18_gpfsr_pos_att4_per_spe_ris) {
		this.r18_gpfsr_pos_att4_per_spe_ris = r18_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg1() {
		return r18_gpfsr_chrg1;
	}
	public void setR18_gpfsr_chrg1(BigDecimal r18_gpfsr_chrg1) {
		this.r18_gpfsr_chrg1 = r18_gpfsr_chrg1;
	}
	public BigDecimal getR18_gpfsr_nom_amt2() {
		return r18_gpfsr_nom_amt2;
	}
	public void setR18_gpfsr_nom_amt2(BigDecimal r18_gpfsr_nom_amt2) {
		this.r18_gpfsr_nom_amt2 = r18_gpfsr_nom_amt2;
	}
	public BigDecimal getR18_gpfsr_pos_att2_per_spe_ris() {
		return r18_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att2_per_spe_ris(BigDecimal r18_gpfsr_pos_att2_per_spe_ris) {
		this.r18_gpfsr_pos_att2_per_spe_ris = r18_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg2() {
		return r18_gpfsr_chrg2;
	}
	public void setR18_gpfsr_chrg2(BigDecimal r18_gpfsr_chrg2) {
		this.r18_gpfsr_chrg2 = r18_gpfsr_chrg2;
	}
	public BigDecimal getR18_tot_spe_ris_chrg() {
		return r18_tot_spe_ris_chrg;
	}
	public void setR18_tot_spe_ris_chrg(BigDecimal r18_tot_spe_ris_chrg) {
		this.r18_tot_spe_ris_chrg = r18_tot_spe_ris_chrg;
	}
	public BigDecimal getR18_net_pos_gen_mar_ris() {
		return r18_net_pos_gen_mar_ris;
	}
	public void setR18_net_pos_gen_mar_ris(BigDecimal r18_net_pos_gen_mar_ris) {
		this.r18_net_pos_gen_mar_ris = r18_net_pos_gen_mar_ris;
	}
	public BigDecimal getR18_gen_mar_ris_chrg_8per() {
		return r18_gen_mar_ris_chrg_8per;
	}
	public void setR18_gen_mar_ris_chrg_8per(BigDecimal r18_gen_mar_ris_chrg_8per) {
		this.r18_gen_mar_ris_chrg_8per = r18_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR18_2per_gen_mar_ris_chrg_div_port() {
		return r18_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR18_2per_gen_mar_ris_chrg_div_port(BigDecimal r18_2per_gen_mar_ris_chrg_div_port) {
		this.r18_2per_gen_mar_ris_chrg_div_port = r18_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR18_tot_gen_mar_risk_chrg() {
		return r18_tot_gen_mar_risk_chrg;
	}
	public void setR18_tot_gen_mar_risk_chrg(BigDecimal r18_tot_gen_mar_risk_chrg) {
		this.r18_tot_gen_mar_risk_chrg = r18_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR18_tot_mar_ris_chrg() {
		return r18_tot_mar_ris_chrg;
	}
	public void setR18_tot_mar_ris_chrg(BigDecimal r18_tot_mar_ris_chrg) {
		this.r18_tot_mar_ris_chrg = r18_tot_mar_ris_chrg;
	}
	public BigDecimal getR19_market() {
		return r19_market;
	}
	public void setR19_market(BigDecimal r19_market) {
		this.r19_market = r19_market;
	}
	public BigDecimal getR19_gpfsr_nom_amt() {
		return r19_gpfsr_nom_amt;
	}
	public void setR19_gpfsr_nom_amt(BigDecimal r19_gpfsr_nom_amt) {
		this.r19_gpfsr_nom_amt = r19_gpfsr_nom_amt;
	}
	public BigDecimal getR19_gpfsr_pos_att8_per_spe_ris() {
		return r19_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att8_per_spe_ris(BigDecimal r19_gpfsr_pos_att8_per_spe_ris) {
		this.r19_gpfsr_pos_att8_per_spe_ris = r19_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg() {
		return r19_gpfsr_chrg;
	}
	public void setR19_gpfsr_chrg(BigDecimal r19_gpfsr_chrg) {
		this.r19_gpfsr_chrg = r19_gpfsr_chrg;
	}
	public BigDecimal getR19_gpfsr_nom_amt1() {
		return r19_gpfsr_nom_amt1;
	}
	public void setR19_gpfsr_nom_amt1(BigDecimal r19_gpfsr_nom_amt1) {
		this.r19_gpfsr_nom_amt1 = r19_gpfsr_nom_amt1;
	}
	public BigDecimal getR19_gpfsr_pos_att4_per_spe_ris() {
		return r19_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att4_per_spe_ris(BigDecimal r19_gpfsr_pos_att4_per_spe_ris) {
		this.r19_gpfsr_pos_att4_per_spe_ris = r19_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg1() {
		return r19_gpfsr_chrg1;
	}
	public void setR19_gpfsr_chrg1(BigDecimal r19_gpfsr_chrg1) {
		this.r19_gpfsr_chrg1 = r19_gpfsr_chrg1;
	}
	public BigDecimal getR19_gpfsr_nom_amt2() {
		return r19_gpfsr_nom_amt2;
	}
	public void setR19_gpfsr_nom_amt2(BigDecimal r19_gpfsr_nom_amt2) {
		this.r19_gpfsr_nom_amt2 = r19_gpfsr_nom_amt2;
	}
	public BigDecimal getR19_gpfsr_pos_att2_per_spe_ris() {
		return r19_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att2_per_spe_ris(BigDecimal r19_gpfsr_pos_att2_per_spe_ris) {
		this.r19_gpfsr_pos_att2_per_spe_ris = r19_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg2() {
		return r19_gpfsr_chrg2;
	}
	public void setR19_gpfsr_chrg2(BigDecimal r19_gpfsr_chrg2) {
		this.r19_gpfsr_chrg2 = r19_gpfsr_chrg2;
	}
	public BigDecimal getR19_tot_spe_ris_chrg() {
		return r19_tot_spe_ris_chrg;
	}
	public void setR19_tot_spe_ris_chrg(BigDecimal r19_tot_spe_ris_chrg) {
		this.r19_tot_spe_ris_chrg = r19_tot_spe_ris_chrg;
	}
	public BigDecimal getR19_net_pos_gen_mar_ris() {
		return r19_net_pos_gen_mar_ris;
	}
	public void setR19_net_pos_gen_mar_ris(BigDecimal r19_net_pos_gen_mar_ris) {
		this.r19_net_pos_gen_mar_ris = r19_net_pos_gen_mar_ris;
	}
	public BigDecimal getR19_gen_mar_ris_chrg_8per() {
		return r19_gen_mar_ris_chrg_8per;
	}
	public void setR19_gen_mar_ris_chrg_8per(BigDecimal r19_gen_mar_ris_chrg_8per) {
		this.r19_gen_mar_ris_chrg_8per = r19_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR19_2per_gen_mar_ris_chrg_div_port() {
		return r19_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR19_2per_gen_mar_ris_chrg_div_port(BigDecimal r19_2per_gen_mar_ris_chrg_div_port) {
		this.r19_2per_gen_mar_ris_chrg_div_port = r19_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR19_tot_gen_mar_risk_chrg() {
		return r19_tot_gen_mar_risk_chrg;
	}
	public void setR19_tot_gen_mar_risk_chrg(BigDecimal r19_tot_gen_mar_risk_chrg) {
		this.r19_tot_gen_mar_risk_chrg = r19_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR19_tot_mar_ris_chrg() {
		return r19_tot_mar_ris_chrg;
	}
	public void setR19_tot_mar_ris_chrg(BigDecimal r19_tot_mar_ris_chrg) {
		this.r19_tot_mar_ris_chrg = r19_tot_mar_ris_chrg;
	}
	public BigDecimal getR20_market() {
		return r20_market;
	}
	public void setR20_market(BigDecimal r20_market) {
		this.r20_market = r20_market;
	}
	public BigDecimal getR20_gpfsr_nom_amt() {
		return r20_gpfsr_nom_amt;
	}
	public void setR20_gpfsr_nom_amt(BigDecimal r20_gpfsr_nom_amt) {
		this.r20_gpfsr_nom_amt = r20_gpfsr_nom_amt;
	}
	public BigDecimal getR20_gpfsr_pos_att8_per_spe_ris() {
		return r20_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att8_per_spe_ris(BigDecimal r20_gpfsr_pos_att8_per_spe_ris) {
		this.r20_gpfsr_pos_att8_per_spe_ris = r20_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg() {
		return r20_gpfsr_chrg;
	}
	public void setR20_gpfsr_chrg(BigDecimal r20_gpfsr_chrg) {
		this.r20_gpfsr_chrg = r20_gpfsr_chrg;
	}
	public BigDecimal getR20_gpfsr_nom_amt1() {
		return r20_gpfsr_nom_amt1;
	}
	public void setR20_gpfsr_nom_amt1(BigDecimal r20_gpfsr_nom_amt1) {
		this.r20_gpfsr_nom_amt1 = r20_gpfsr_nom_amt1;
	}
	public BigDecimal getR20_gpfsr_pos_att4_per_spe_ris() {
		return r20_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att4_per_spe_ris(BigDecimal r20_gpfsr_pos_att4_per_spe_ris) {
		this.r20_gpfsr_pos_att4_per_spe_ris = r20_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg1() {
		return r20_gpfsr_chrg1;
	}
	public void setR20_gpfsr_chrg1(BigDecimal r20_gpfsr_chrg1) {
		this.r20_gpfsr_chrg1 = r20_gpfsr_chrg1;
	}
	public BigDecimal getR20_gpfsr_nom_amt2() {
		return r20_gpfsr_nom_amt2;
	}
	public void setR20_gpfsr_nom_amt2(BigDecimal r20_gpfsr_nom_amt2) {
		this.r20_gpfsr_nom_amt2 = r20_gpfsr_nom_amt2;
	}
	public BigDecimal getR20_gpfsr_pos_att2_per_spe_ris() {
		return r20_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att2_per_spe_ris(BigDecimal r20_gpfsr_pos_att2_per_spe_ris) {
		this.r20_gpfsr_pos_att2_per_spe_ris = r20_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg2() {
		return r20_gpfsr_chrg2;
	}
	public void setR20_gpfsr_chrg2(BigDecimal r20_gpfsr_chrg2) {
		this.r20_gpfsr_chrg2 = r20_gpfsr_chrg2;
	}
	public BigDecimal getR20_tot_spe_ris_chrg() {
		return r20_tot_spe_ris_chrg;
	}
	public void setR20_tot_spe_ris_chrg(BigDecimal r20_tot_spe_ris_chrg) {
		this.r20_tot_spe_ris_chrg = r20_tot_spe_ris_chrg;
	}
	public BigDecimal getR20_net_pos_gen_mar_ris() {
		return r20_net_pos_gen_mar_ris;
	}
	public void setR20_net_pos_gen_mar_ris(BigDecimal r20_net_pos_gen_mar_ris) {
		this.r20_net_pos_gen_mar_ris = r20_net_pos_gen_mar_ris;
	}
	public BigDecimal getR20_gen_mar_ris_chrg_8per() {
		return r20_gen_mar_ris_chrg_8per;
	}
	public void setR20_gen_mar_ris_chrg_8per(BigDecimal r20_gen_mar_ris_chrg_8per) {
		this.r20_gen_mar_ris_chrg_8per = r20_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR20_2per_gen_mar_ris_chrg_div_port() {
		return r20_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR20_2per_gen_mar_ris_chrg_div_port(BigDecimal r20_2per_gen_mar_ris_chrg_div_port) {
		this.r20_2per_gen_mar_ris_chrg_div_port = r20_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR20_tot_gen_mar_risk_chrg() {
		return r20_tot_gen_mar_risk_chrg;
	}
	public void setR20_tot_gen_mar_risk_chrg(BigDecimal r20_tot_gen_mar_risk_chrg) {
		this.r20_tot_gen_mar_risk_chrg = r20_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR20_tot_mar_ris_chrg() {
		return r20_tot_mar_ris_chrg;
	}
	public void setR20_tot_mar_ris_chrg(BigDecimal r20_tot_mar_ris_chrg) {
		this.r20_tot_mar_ris_chrg = r20_tot_mar_ris_chrg;
	}
	public BigDecimal getR21_market() {
		return r21_market;
	}
	public void setR21_market(BigDecimal r21_market) {
		this.r21_market = r21_market;
	}
	public BigDecimal getR21_gpfsr_nom_amt() {
		return r21_gpfsr_nom_amt;
	}
	public void setR21_gpfsr_nom_amt(BigDecimal r21_gpfsr_nom_amt) {
		this.r21_gpfsr_nom_amt = r21_gpfsr_nom_amt;
	}
	public BigDecimal getR21_gpfsr_pos_att8_per_spe_ris() {
		return r21_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att8_per_spe_ris(BigDecimal r21_gpfsr_pos_att8_per_spe_ris) {
		this.r21_gpfsr_pos_att8_per_spe_ris = r21_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg() {
		return r21_gpfsr_chrg;
	}
	public void setR21_gpfsr_chrg(BigDecimal r21_gpfsr_chrg) {
		this.r21_gpfsr_chrg = r21_gpfsr_chrg;
	}
	public BigDecimal getR21_gpfsr_nom_amt1() {
		return r21_gpfsr_nom_amt1;
	}
	public void setR21_gpfsr_nom_amt1(BigDecimal r21_gpfsr_nom_amt1) {
		this.r21_gpfsr_nom_amt1 = r21_gpfsr_nom_amt1;
	}
	public BigDecimal getR21_gpfsr_pos_att4_per_spe_ris() {
		return r21_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att4_per_spe_ris(BigDecimal r21_gpfsr_pos_att4_per_spe_ris) {
		this.r21_gpfsr_pos_att4_per_spe_ris = r21_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg1() {
		return r21_gpfsr_chrg1;
	}
	public void setR21_gpfsr_chrg1(BigDecimal r21_gpfsr_chrg1) {
		this.r21_gpfsr_chrg1 = r21_gpfsr_chrg1;
	}
	public BigDecimal getR21_gpfsr_nom_amt2() {
		return r21_gpfsr_nom_amt2;
	}
	public void setR21_gpfsr_nom_amt2(BigDecimal r21_gpfsr_nom_amt2) {
		this.r21_gpfsr_nom_amt2 = r21_gpfsr_nom_amt2;
	}
	public BigDecimal getR21_gpfsr_pos_att2_per_spe_ris() {
		return r21_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att2_per_spe_ris(BigDecimal r21_gpfsr_pos_att2_per_spe_ris) {
		this.r21_gpfsr_pos_att2_per_spe_ris = r21_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg2() {
		return r21_gpfsr_chrg2;
	}
	public void setR21_gpfsr_chrg2(BigDecimal r21_gpfsr_chrg2) {
		this.r21_gpfsr_chrg2 = r21_gpfsr_chrg2;
	}
	public BigDecimal getR21_tot_spe_ris_chrg() {
		return r21_tot_spe_ris_chrg;
	}
	public void setR21_tot_spe_ris_chrg(BigDecimal r21_tot_spe_ris_chrg) {
		this.r21_tot_spe_ris_chrg = r21_tot_spe_ris_chrg;
	}
	public BigDecimal getR21_net_pos_gen_mar_ris() {
		return r21_net_pos_gen_mar_ris;
	}
	public void setR21_net_pos_gen_mar_ris(BigDecimal r21_net_pos_gen_mar_ris) {
		this.r21_net_pos_gen_mar_ris = r21_net_pos_gen_mar_ris;
	}
	public BigDecimal getR21_gen_mar_ris_chrg_8per() {
		return r21_gen_mar_ris_chrg_8per;
	}
	public void setR21_gen_mar_ris_chrg_8per(BigDecimal r21_gen_mar_ris_chrg_8per) {
		this.r21_gen_mar_ris_chrg_8per = r21_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR21_2per_gen_mar_ris_chrg_div_port() {
		return r21_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR21_2per_gen_mar_ris_chrg_div_port(BigDecimal r21_2per_gen_mar_ris_chrg_div_port) {
		this.r21_2per_gen_mar_ris_chrg_div_port = r21_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR21_tot_gen_mar_risk_chrg() {
		return r21_tot_gen_mar_risk_chrg;
	}
	public void setR21_tot_gen_mar_risk_chrg(BigDecimal r21_tot_gen_mar_risk_chrg) {
		this.r21_tot_gen_mar_risk_chrg = r21_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR21_tot_mar_ris_chrg() {
		return r21_tot_mar_ris_chrg;
	}
	public void setR21_tot_mar_ris_chrg(BigDecimal r21_tot_mar_ris_chrg) {
		this.r21_tot_mar_ris_chrg = r21_tot_mar_ris_chrg;
	}
	public BigDecimal getR22_market() {
		return r22_market;
	}
	public void setR22_market(BigDecimal r22_market) {
		this.r22_market = r22_market;
	}
	public BigDecimal getR22_gpfsr_nom_amt() {
		return r22_gpfsr_nom_amt;
	}
	public void setR22_gpfsr_nom_amt(BigDecimal r22_gpfsr_nom_amt) {
		this.r22_gpfsr_nom_amt = r22_gpfsr_nom_amt;
	}
	public BigDecimal getR22_gpfsr_pos_att8_per_spe_ris() {
		return r22_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att8_per_spe_ris(BigDecimal r22_gpfsr_pos_att8_per_spe_ris) {
		this.r22_gpfsr_pos_att8_per_spe_ris = r22_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg() {
		return r22_gpfsr_chrg;
	}
	public void setR22_gpfsr_chrg(BigDecimal r22_gpfsr_chrg) {
		this.r22_gpfsr_chrg = r22_gpfsr_chrg;
	}
	public BigDecimal getR22_gpfsr_nom_amt1() {
		return r22_gpfsr_nom_amt1;
	}
	public void setR22_gpfsr_nom_amt1(BigDecimal r22_gpfsr_nom_amt1) {
		this.r22_gpfsr_nom_amt1 = r22_gpfsr_nom_amt1;
	}
	public BigDecimal getR22_gpfsr_pos_att4_per_spe_ris() {
		return r22_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att4_per_spe_ris(BigDecimal r22_gpfsr_pos_att4_per_spe_ris) {
		this.r22_gpfsr_pos_att4_per_spe_ris = r22_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg1() {
		return r22_gpfsr_chrg1;
	}
	public void setR22_gpfsr_chrg1(BigDecimal r22_gpfsr_chrg1) {
		this.r22_gpfsr_chrg1 = r22_gpfsr_chrg1;
	}
	public BigDecimal getR22_gpfsr_nom_amt2() {
		return r22_gpfsr_nom_amt2;
	}
	public void setR22_gpfsr_nom_amt2(BigDecimal r22_gpfsr_nom_amt2) {
		this.r22_gpfsr_nom_amt2 = r22_gpfsr_nom_amt2;
	}
	public BigDecimal getR22_gpfsr_pos_att2_per_spe_ris() {
		return r22_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att2_per_spe_ris(BigDecimal r22_gpfsr_pos_att2_per_spe_ris) {
		this.r22_gpfsr_pos_att2_per_spe_ris = r22_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg2() {
		return r22_gpfsr_chrg2;
	}
	public void setR22_gpfsr_chrg2(BigDecimal r22_gpfsr_chrg2) {
		this.r22_gpfsr_chrg2 = r22_gpfsr_chrg2;
	}
	public BigDecimal getR22_tot_spe_ris_chrg() {
		return r22_tot_spe_ris_chrg;
	}
	public void setR22_tot_spe_ris_chrg(BigDecimal r22_tot_spe_ris_chrg) {
		this.r22_tot_spe_ris_chrg = r22_tot_spe_ris_chrg;
	}
	public BigDecimal getR22_net_pos_gen_mar_ris() {
		return r22_net_pos_gen_mar_ris;
	}
	public void setR22_net_pos_gen_mar_ris(BigDecimal r22_net_pos_gen_mar_ris) {
		this.r22_net_pos_gen_mar_ris = r22_net_pos_gen_mar_ris;
	}
	public BigDecimal getR22_gen_mar_ris_chrg_8per() {
		return r22_gen_mar_ris_chrg_8per;
	}
	public void setR22_gen_mar_ris_chrg_8per(BigDecimal r22_gen_mar_ris_chrg_8per) {
		this.r22_gen_mar_ris_chrg_8per = r22_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR22_2per_gen_mar_ris_chrg_div_port() {
		return r22_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR22_2per_gen_mar_ris_chrg_div_port(BigDecimal r22_2per_gen_mar_ris_chrg_div_port) {
		this.r22_2per_gen_mar_ris_chrg_div_port = r22_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR22_tot_gen_mar_risk_chrg() {
		return r22_tot_gen_mar_risk_chrg;
	}
	public void setR22_tot_gen_mar_risk_chrg(BigDecimal r22_tot_gen_mar_risk_chrg) {
		this.r22_tot_gen_mar_risk_chrg = r22_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR22_tot_mar_ris_chrg() {
		return r22_tot_mar_ris_chrg;
	}
	public void setR22_tot_mar_ris_chrg(BigDecimal r22_tot_mar_ris_chrg) {
		this.r22_tot_mar_ris_chrg = r22_tot_mar_ris_chrg;
	}
	public String getR23_market() {
		return r23_market;
	}
	public void setR23_market(String r23_market) {
		this.r23_market = r23_market;
	}
	public BigDecimal getR23_gpfsr_nom_amt() {
		return r23_gpfsr_nom_amt;
	}
	public void setR23_gpfsr_nom_amt(BigDecimal r23_gpfsr_nom_amt) {
		this.r23_gpfsr_nom_amt = r23_gpfsr_nom_amt;
	}
	public BigDecimal getR23_gpfsr_pos_att8_per_spe_ris() {
		return r23_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att8_per_spe_ris(BigDecimal r23_gpfsr_pos_att8_per_spe_ris) {
		this.r23_gpfsr_pos_att8_per_spe_ris = r23_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg() {
		return r23_gpfsr_chrg;
	}
	public void setR23_gpfsr_chrg(BigDecimal r23_gpfsr_chrg) {
		this.r23_gpfsr_chrg = r23_gpfsr_chrg;
	}
	public BigDecimal getR23_gpfsr_nom_amt1() {
		return r23_gpfsr_nom_amt1;
	}
	public void setR23_gpfsr_nom_amt1(BigDecimal r23_gpfsr_nom_amt1) {
		this.r23_gpfsr_nom_amt1 = r23_gpfsr_nom_amt1;
	}
	public BigDecimal getR23_gpfsr_pos_att4_per_spe_ris() {
		return r23_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att4_per_spe_ris(BigDecimal r23_gpfsr_pos_att4_per_spe_ris) {
		this.r23_gpfsr_pos_att4_per_spe_ris = r23_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg1() {
		return r23_gpfsr_chrg1;
	}
	public void setR23_gpfsr_chrg1(BigDecimal r23_gpfsr_chrg1) {
		this.r23_gpfsr_chrg1 = r23_gpfsr_chrg1;
	}
	public BigDecimal getR23_gpfsr_nom_amt2() {
		return r23_gpfsr_nom_amt2;
	}
	public void setR23_gpfsr_nom_amt2(BigDecimal r23_gpfsr_nom_amt2) {
		this.r23_gpfsr_nom_amt2 = r23_gpfsr_nom_amt2;
	}
	public BigDecimal getR23_gpfsr_pos_att2_per_spe_ris() {
		return r23_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att2_per_spe_ris(BigDecimal r23_gpfsr_pos_att2_per_spe_ris) {
		this.r23_gpfsr_pos_att2_per_spe_ris = r23_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg2() {
		return r23_gpfsr_chrg2;
	}
	public void setR23_gpfsr_chrg2(BigDecimal r23_gpfsr_chrg2) {
		this.r23_gpfsr_chrg2 = r23_gpfsr_chrg2;
	}
	public BigDecimal getR23_tot_spe_ris_chrg() {
		return r23_tot_spe_ris_chrg;
	}
	public void setR23_tot_spe_ris_chrg(BigDecimal r23_tot_spe_ris_chrg) {
		this.r23_tot_spe_ris_chrg = r23_tot_spe_ris_chrg;
	}
	public BigDecimal getR23_net_pos_gen_mar_ris() {
		return r23_net_pos_gen_mar_ris;
	}
	public void setR23_net_pos_gen_mar_ris(BigDecimal r23_net_pos_gen_mar_ris) {
		this.r23_net_pos_gen_mar_ris = r23_net_pos_gen_mar_ris;
	}
	public BigDecimal getR23_gen_mar_ris_chrg_8per() {
		return r23_gen_mar_ris_chrg_8per;
	}
	public void setR23_gen_mar_ris_chrg_8per(BigDecimal r23_gen_mar_ris_chrg_8per) {
		this.r23_gen_mar_ris_chrg_8per = r23_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR23_2per_gen_mar_ris_chrg_div_port() {
		return r23_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR23_2per_gen_mar_ris_chrg_div_port(BigDecimal r23_2per_gen_mar_ris_chrg_div_port) {
		this.r23_2per_gen_mar_ris_chrg_div_port = r23_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR23_tot_gen_mar_risk_chrg() {
		return r23_tot_gen_mar_risk_chrg;
	}
	public void setR23_tot_gen_mar_risk_chrg(BigDecimal r23_tot_gen_mar_risk_chrg) {
		this.r23_tot_gen_mar_risk_chrg = r23_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR23_tot_mar_ris_chrg() {
		return r23_tot_mar_ris_chrg;
	}
	public void setR23_tot_mar_ris_chrg(BigDecimal r23_tot_mar_ris_chrg) {
		this.r23_tot_mar_ris_chrg = r23_tot_mar_ris_chrg;
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
// RESUB summary M_EPR
//=====================================================


public class M_EPR_RESUB_Summary_RowMapper 
        implements RowMapper<M_EPR_RESUB_Summary_Entity> {

    @Override
    public M_EPR_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_EPR_RESUB_Summary_Entity obj = new M_EPR_RESUB_Summary_Entity();

// =========================
// R11
// =========================
obj.setR11_market(rs.getBigDecimal("r11_market"));
obj.setR11_gpfsr_nom_amt(rs.getBigDecimal("r11_gpfsr_nom_amt"));
obj.setR11_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att8_per_spe_ris"));
obj.setR11_gpfsr_chrg(rs.getBigDecimal("r11_gpfsr_chrg"));
obj.setR11_gpfsr_nom_amt1(rs.getBigDecimal("r11_gpfsr_nom_amt1"));
obj.setR11_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att4_per_spe_ris"));
obj.setR11_gpfsr_chrg1(rs.getBigDecimal("r11_gpfsr_chrg1"));
obj.setR11_gpfsr_nom_amt2(rs.getBigDecimal("r11_gpfsr_nom_amt2"));
obj.setR11_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att2_per_spe_ris"));
obj.setR11_gpfsr_chrg2(rs.getBigDecimal("r11_gpfsr_chrg2"));
obj.setR11_tot_spe_ris_chrg(rs.getBigDecimal("r11_tot_spe_ris_chrg"));
obj.setR11_net_pos_gen_mar_ris(rs.getBigDecimal("r11_net_pos_gen_mar_ris"));
obj.setR11_gen_mar_ris_chrg_8per(rs.getBigDecimal("r11_gen_mar_ris_chrg_8per"));
obj.setR11_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r11_2per_gen_mar_ris_chrg_div_port"));
obj.setR11_tot_gen_mar_risk_chrg(rs.getBigDecimal("r11_tot_gen_mar_risk_chrg"));
obj.setR11_tot_mar_ris_chrg(rs.getBigDecimal("r11_tot_mar_ris_chrg"));

// =========================
// R12
// =========================
obj.setR12_market(rs.getBigDecimal("r12_market"));
obj.setR12_gpfsr_nom_amt(rs.getBigDecimal("r12_gpfsr_nom_amt"));
obj.setR12_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att8_per_spe_ris"));
obj.setR12_gpfsr_chrg(rs.getBigDecimal("r12_gpfsr_chrg"));
obj.setR12_gpfsr_nom_amt1(rs.getBigDecimal("r12_gpfsr_nom_amt1"));
obj.setR12_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att4_per_spe_ris"));
obj.setR12_gpfsr_chrg1(rs.getBigDecimal("r12_gpfsr_chrg1"));
obj.setR12_gpfsr_nom_amt2(rs.getBigDecimal("r12_gpfsr_nom_amt2"));
obj.setR12_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att2_per_spe_ris"));
obj.setR12_gpfsr_chrg2(rs.getBigDecimal("r12_gpfsr_chrg2"));
obj.setR12_tot_spe_ris_chrg(rs.getBigDecimal("r12_tot_spe_ris_chrg"));
obj.setR12_net_pos_gen_mar_ris(rs.getBigDecimal("r12_net_pos_gen_mar_ris"));
obj.setR12_gen_mar_ris_chrg_8per(rs.getBigDecimal("r12_gen_mar_ris_chrg_8per"));
obj.setR12_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r12_2per_gen_mar_ris_chrg_div_port"));
obj.setR12_tot_gen_mar_risk_chrg(rs.getBigDecimal("r12_tot_gen_mar_risk_chrg"));
obj.setR12_tot_mar_ris_chrg(rs.getBigDecimal("r12_tot_mar_ris_chrg"));

// =========================
// R13
// =========================
obj.setR13_market(rs.getBigDecimal("r13_market"));
obj.setR13_gpfsr_nom_amt(rs.getBigDecimal("r13_gpfsr_nom_amt"));
obj.setR13_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att8_per_spe_ris"));
obj.setR13_gpfsr_chrg(rs.getBigDecimal("r13_gpfsr_chrg"));
obj.setR13_gpfsr_nom_amt1(rs.getBigDecimal("r13_gpfsr_nom_amt1"));
obj.setR13_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att4_per_spe_ris"));
obj.setR13_gpfsr_chrg1(rs.getBigDecimal("r13_gpfsr_chrg1"));
obj.setR13_gpfsr_nom_amt2(rs.getBigDecimal("r13_gpfsr_nom_amt2"));
obj.setR13_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att2_per_spe_ris"));
obj.setR13_gpfsr_chrg2(rs.getBigDecimal("r13_gpfsr_chrg2"));
obj.setR13_tot_spe_ris_chrg(rs.getBigDecimal("r13_tot_spe_ris_chrg"));
obj.setR13_net_pos_gen_mar_ris(rs.getBigDecimal("r13_net_pos_gen_mar_ris"));
obj.setR13_gen_mar_ris_chrg_8per(rs.getBigDecimal("r13_gen_mar_ris_chrg_8per"));
obj.setR13_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r13_2per_gen_mar_ris_chrg_div_port"));
obj.setR13_tot_gen_mar_risk_chrg(rs.getBigDecimal("r13_tot_gen_mar_risk_chrg"));
obj.setR13_tot_mar_ris_chrg(rs.getBigDecimal("r13_tot_mar_ris_chrg"));

// =========================
// R14
// =========================
obj.setR14_market(rs.getBigDecimal("r14_market"));
obj.setR14_gpfsr_nom_amt(rs.getBigDecimal("r14_gpfsr_nom_amt"));
obj.setR14_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att8_per_spe_ris"));
obj.setR14_gpfsr_chrg(rs.getBigDecimal("r14_gpfsr_chrg"));
obj.setR14_gpfsr_nom_amt1(rs.getBigDecimal("r14_gpfsr_nom_amt1"));
obj.setR14_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att4_per_spe_ris"));
obj.setR14_gpfsr_chrg1(rs.getBigDecimal("r14_gpfsr_chrg1"));
obj.setR14_gpfsr_nom_amt2(rs.getBigDecimal("r14_gpfsr_nom_amt2"));
obj.setR14_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att2_per_spe_ris"));
obj.setR14_gpfsr_chrg2(rs.getBigDecimal("r14_gpfsr_chrg2"));
obj.setR14_tot_spe_ris_chrg(rs.getBigDecimal("r14_tot_spe_ris_chrg"));
obj.setR14_net_pos_gen_mar_ris(rs.getBigDecimal("r14_net_pos_gen_mar_ris"));
obj.setR14_gen_mar_ris_chrg_8per(rs.getBigDecimal("r14_gen_mar_ris_chrg_8per"));
obj.setR14_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r14_2per_gen_mar_ris_chrg_div_port"));
obj.setR14_tot_gen_mar_risk_chrg(rs.getBigDecimal("r14_tot_gen_mar_risk_chrg"));
obj.setR14_tot_mar_ris_chrg(rs.getBigDecimal("r14_tot_mar_ris_chrg"));

// =========================
// R15
// =========================
obj.setR15_market(rs.getBigDecimal("r15_market"));
obj.setR15_gpfsr_nom_amt(rs.getBigDecimal("r15_gpfsr_nom_amt"));
obj.setR15_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att8_per_spe_ris"));
obj.setR15_gpfsr_chrg(rs.getBigDecimal("r15_gpfsr_chrg"));
obj.setR15_gpfsr_nom_amt1(rs.getBigDecimal("r15_gpfsr_nom_amt1"));
obj.setR15_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att4_per_spe_ris"));
obj.setR15_gpfsr_chrg1(rs.getBigDecimal("r15_gpfsr_chrg1"));
obj.setR15_gpfsr_nom_amt2(rs.getBigDecimal("r15_gpfsr_nom_amt2"));
obj.setR15_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att2_per_spe_ris"));
obj.setR15_gpfsr_chrg2(rs.getBigDecimal("r15_gpfsr_chrg2"));
obj.setR15_tot_spe_ris_chrg(rs.getBigDecimal("r15_tot_spe_ris_chrg"));
obj.setR15_net_pos_gen_mar_ris(rs.getBigDecimal("r15_net_pos_gen_mar_ris"));
obj.setR15_gen_mar_ris_chrg_8per(rs.getBigDecimal("r15_gen_mar_ris_chrg_8per"));
obj.setR15_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r15_2per_gen_mar_ris_chrg_div_port"));
obj.setR15_tot_gen_mar_risk_chrg(rs.getBigDecimal("r15_tot_gen_mar_risk_chrg"));
obj.setR15_tot_mar_ris_chrg(rs.getBigDecimal("r15_tot_mar_ris_chrg"));

// =========================
// R16
// =========================
obj.setR16_market(rs.getBigDecimal("r16_market"));
obj.setR16_gpfsr_nom_amt(rs.getBigDecimal("r16_gpfsr_nom_amt"));
obj.setR16_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att8_per_spe_ris"));
obj.setR16_gpfsr_chrg(rs.getBigDecimal("r16_gpfsr_chrg"));
obj.setR16_gpfsr_nom_amt1(rs.getBigDecimal("r16_gpfsr_nom_amt1"));
obj.setR16_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att4_per_spe_ris"));
obj.setR16_gpfsr_chrg1(rs.getBigDecimal("r16_gpfsr_chrg1"));
obj.setR16_gpfsr_nom_amt2(rs.getBigDecimal("r16_gpfsr_nom_amt2"));
obj.setR16_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att2_per_spe_ris"));
obj.setR16_gpfsr_chrg2(rs.getBigDecimal("r16_gpfsr_chrg2"));
obj.setR16_tot_spe_ris_chrg(rs.getBigDecimal("r16_tot_spe_ris_chrg"));
obj.setR16_net_pos_gen_mar_ris(rs.getBigDecimal("r16_net_pos_gen_mar_ris"));
obj.setR16_gen_mar_ris_chrg_8per(rs.getBigDecimal("r16_gen_mar_ris_chrg_8per"));
obj.setR16_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r16_2per_gen_mar_ris_chrg_div_port"));
obj.setR16_tot_gen_mar_risk_chrg(rs.getBigDecimal("r16_tot_gen_mar_risk_chrg"));
obj.setR16_tot_mar_ris_chrg(rs.getBigDecimal("r16_tot_mar_ris_chrg"));

// =========================
// R17
// =========================
obj.setR17_market(rs.getBigDecimal("r17_market"));
obj.setR17_gpfsr_nom_amt(rs.getBigDecimal("r17_gpfsr_nom_amt"));
obj.setR17_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att8_per_spe_ris"));
obj.setR17_gpfsr_chrg(rs.getBigDecimal("r17_gpfsr_chrg"));
obj.setR17_gpfsr_nom_amt1(rs.getBigDecimal("r17_gpfsr_nom_amt1"));
obj.setR17_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att4_per_spe_ris"));
obj.setR17_gpfsr_chrg1(rs.getBigDecimal("r17_gpfsr_chrg1"));
obj.setR17_gpfsr_nom_amt2(rs.getBigDecimal("r17_gpfsr_nom_amt2"));
obj.setR17_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att2_per_spe_ris"));
obj.setR17_gpfsr_chrg2(rs.getBigDecimal("r17_gpfsr_chrg2"));
obj.setR17_tot_spe_ris_chrg(rs.getBigDecimal("r17_tot_spe_ris_chrg"));
obj.setR17_net_pos_gen_mar_ris(rs.getBigDecimal("r17_net_pos_gen_mar_ris"));
obj.setR17_gen_mar_ris_chrg_8per(rs.getBigDecimal("r17_gen_mar_ris_chrg_8per"));
obj.setR17_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r17_2per_gen_mar_ris_chrg_div_port"));
obj.setR17_tot_gen_mar_risk_chrg(rs.getBigDecimal("r17_tot_gen_mar_risk_chrg"));
obj.setR17_tot_mar_ris_chrg(rs.getBigDecimal("r17_tot_mar_ris_chrg"));

// =========================
// R18
// =========================
obj.setR18_market(rs.getBigDecimal("r18_market"));
obj.setR18_gpfsr_nom_amt(rs.getBigDecimal("r18_gpfsr_nom_amt"));
obj.setR18_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att8_per_spe_ris"));
obj.setR18_gpfsr_chrg(rs.getBigDecimal("r18_gpfsr_chrg"));
obj.setR18_gpfsr_nom_amt1(rs.getBigDecimal("r18_gpfsr_nom_amt1"));
obj.setR18_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att4_per_spe_ris"));
obj.setR18_gpfsr_chrg1(rs.getBigDecimal("r18_gpfsr_chrg1"));
obj.setR18_gpfsr_nom_amt2(rs.getBigDecimal("r18_gpfsr_nom_amt2"));
obj.setR18_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att2_per_spe_ris"));
obj.setR18_gpfsr_chrg2(rs.getBigDecimal("r18_gpfsr_chrg2"));
obj.setR18_tot_spe_ris_chrg(rs.getBigDecimal("r18_tot_spe_ris_chrg"));
obj.setR18_net_pos_gen_mar_ris(rs.getBigDecimal("r18_net_pos_gen_mar_ris"));
obj.setR18_gen_mar_ris_chrg_8per(rs.getBigDecimal("r18_gen_mar_ris_chrg_8per"));
obj.setR18_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r18_2per_gen_mar_ris_chrg_div_port"));
obj.setR18_tot_gen_mar_risk_chrg(rs.getBigDecimal("r18_tot_gen_mar_risk_chrg"));
obj.setR18_tot_mar_ris_chrg(rs.getBigDecimal("r18_tot_mar_ris_chrg"));

// =========================
// R19
// =========================
obj.setR19_market(rs.getBigDecimal("r19_market"));
obj.setR19_gpfsr_nom_amt(rs.getBigDecimal("r19_gpfsr_nom_amt"));
obj.setR19_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att8_per_spe_ris"));
obj.setR19_gpfsr_chrg(rs.getBigDecimal("r19_gpfsr_chrg"));
obj.setR19_gpfsr_nom_amt1(rs.getBigDecimal("r19_gpfsr_nom_amt1"));
obj.setR19_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att4_per_spe_ris"));
obj.setR19_gpfsr_chrg1(rs.getBigDecimal("r19_gpfsr_chrg1"));
obj.setR19_gpfsr_nom_amt2(rs.getBigDecimal("r19_gpfsr_nom_amt2"));
obj.setR19_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att2_per_spe_ris"));
obj.setR19_gpfsr_chrg2(rs.getBigDecimal("r19_gpfsr_chrg2"));
obj.setR19_tot_spe_ris_chrg(rs.getBigDecimal("r19_tot_spe_ris_chrg"));
obj.setR19_net_pos_gen_mar_ris(rs.getBigDecimal("r19_net_pos_gen_mar_ris"));
obj.setR19_gen_mar_ris_chrg_8per(rs.getBigDecimal("r19_gen_mar_ris_chrg_8per"));
obj.setR19_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r19_2per_gen_mar_ris_chrg_div_port"));
obj.setR19_tot_gen_mar_risk_chrg(rs.getBigDecimal("r19_tot_gen_mar_risk_chrg"));
obj.setR19_tot_mar_ris_chrg(rs.getBigDecimal("r19_tot_mar_ris_chrg"));

// =========================
// R20
// =========================
obj.setR20_market(rs.getBigDecimal("r20_market"));
obj.setR20_gpfsr_nom_amt(rs.getBigDecimal("r20_gpfsr_nom_amt"));
obj.setR20_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att8_per_spe_ris"));
obj.setR20_gpfsr_chrg(rs.getBigDecimal("r20_gpfsr_chrg"));
obj.setR20_gpfsr_nom_amt1(rs.getBigDecimal("r20_gpfsr_nom_amt1"));
obj.setR20_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att4_per_spe_ris"));
obj.setR20_gpfsr_chrg1(rs.getBigDecimal("r20_gpfsr_chrg1"));
obj.setR20_gpfsr_nom_amt2(rs.getBigDecimal("r20_gpfsr_nom_amt2"));
obj.setR20_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att2_per_spe_ris"));
obj.setR20_gpfsr_chrg2(rs.getBigDecimal("r20_gpfsr_chrg2"));
obj.setR20_tot_spe_ris_chrg(rs.getBigDecimal("r20_tot_spe_ris_chrg"));
obj.setR20_net_pos_gen_mar_ris(rs.getBigDecimal("r20_net_pos_gen_mar_ris"));
obj.setR20_gen_mar_ris_chrg_8per(rs.getBigDecimal("r20_gen_mar_ris_chrg_8per"));
obj.setR20_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r20_2per_gen_mar_ris_chrg_div_port"));
obj.setR20_tot_gen_mar_risk_chrg(rs.getBigDecimal("r20_tot_gen_mar_risk_chrg"));
obj.setR20_tot_mar_ris_chrg(rs.getBigDecimal("r20_tot_mar_ris_chrg"));

// =========================
// R21
// =========================
obj.setR21_market(rs.getBigDecimal("r21_market"));
obj.setR21_gpfsr_nom_amt(rs.getBigDecimal("r21_gpfsr_nom_amt"));
obj.setR21_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att8_per_spe_ris"));
obj.setR21_gpfsr_chrg(rs.getBigDecimal("r21_gpfsr_chrg"));
obj.setR21_gpfsr_nom_amt1(rs.getBigDecimal("r21_gpfsr_nom_amt1"));
obj.setR21_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att4_per_spe_ris"));
obj.setR21_gpfsr_chrg1(rs.getBigDecimal("r21_gpfsr_chrg1"));
obj.setR21_gpfsr_nom_amt2(rs.getBigDecimal("r21_gpfsr_nom_amt2"));
obj.setR21_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att2_per_spe_ris"));
obj.setR21_gpfsr_chrg2(rs.getBigDecimal("r21_gpfsr_chrg2"));
obj.setR21_tot_spe_ris_chrg(rs.getBigDecimal("r21_tot_spe_ris_chrg"));
obj.setR21_net_pos_gen_mar_ris(rs.getBigDecimal("r21_net_pos_gen_mar_ris"));
obj.setR21_gen_mar_ris_chrg_8per(rs.getBigDecimal("r21_gen_mar_ris_chrg_8per"));
obj.setR21_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r21_2per_gen_mar_ris_chrg_div_port"));
obj.setR21_tot_gen_mar_risk_chrg(rs.getBigDecimal("r21_tot_gen_mar_risk_chrg"));
obj.setR21_tot_mar_ris_chrg(rs.getBigDecimal("r21_tot_mar_ris_chrg"));


// =========================
// R22
// =========================
obj.setR22_market(rs.getBigDecimal("r22_market"));
obj.setR22_gpfsr_nom_amt(rs.getBigDecimal("r22_gpfsr_nom_amt"));
obj.setR22_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att8_per_spe_ris"));
obj.setR22_gpfsr_chrg(rs.getBigDecimal("r22_gpfsr_chrg"));
obj.setR22_gpfsr_nom_amt1(rs.getBigDecimal("r22_gpfsr_nom_amt1"));
obj.setR22_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att4_per_spe_ris"));
obj.setR22_gpfsr_chrg1(rs.getBigDecimal("r22_gpfsr_chrg1"));
obj.setR22_gpfsr_nom_amt2(rs.getBigDecimal("r22_gpfsr_nom_amt2"));
obj.setR22_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att2_per_spe_ris"));
obj.setR22_gpfsr_chrg2(rs.getBigDecimal("r22_gpfsr_chrg2"));
obj.setR22_tot_spe_ris_chrg(rs.getBigDecimal("r22_tot_spe_ris_chrg"));
obj.setR22_net_pos_gen_mar_ris(rs.getBigDecimal("r22_net_pos_gen_mar_ris"));
obj.setR22_gen_mar_ris_chrg_8per(rs.getBigDecimal("r22_gen_mar_ris_chrg_8per"));
obj.setR22_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r22_2per_gen_mar_ris_chrg_div_port"));
obj.setR22_tot_gen_mar_risk_chrg(rs.getBigDecimal("r22_tot_gen_mar_risk_chrg"));
obj.setR22_tot_mar_ris_chrg(rs.getBigDecimal("r22_tot_mar_ris_chrg"));

// =========================
// R23
// =========================
obj.setR23_market(rs.getString("r23_market"));
obj.setR23_gpfsr_nom_amt(rs.getBigDecimal("r23_gpfsr_nom_amt"));
obj.setR23_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att8_per_spe_ris"));
obj.setR23_gpfsr_chrg(rs.getBigDecimal("r23_gpfsr_chrg"));
obj.setR23_gpfsr_nom_amt1(rs.getBigDecimal("r23_gpfsr_nom_amt1"));
obj.setR23_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att4_per_spe_ris"));
obj.setR23_gpfsr_chrg1(rs.getBigDecimal("r23_gpfsr_chrg1"));
obj.setR23_gpfsr_nom_amt2(rs.getBigDecimal("r23_gpfsr_nom_amt2"));
obj.setR23_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att2_per_spe_ris"));
obj.setR23_gpfsr_chrg2(rs.getBigDecimal("r23_gpfsr_chrg2"));
obj.setR23_tot_spe_ris_chrg(rs.getBigDecimal("r23_tot_spe_ris_chrg"));
obj.setR23_net_pos_gen_mar_ris(rs.getBigDecimal("r23_net_pos_gen_mar_ris"));
obj.setR23_gen_mar_ris_chrg_8per(rs.getBigDecimal("r23_gen_mar_ris_chrg_8per"));
obj.setR23_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r23_2per_gen_mar_ris_chrg_div_port"));
obj.setR23_tot_gen_mar_risk_chrg(rs.getBigDecimal("r23_tot_gen_mar_risk_chrg"));
obj.setR23_tot_mar_ris_chrg(rs.getBigDecimal("r23_tot_mar_ris_chrg"));


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

public class M_EPR_RESUB_Summary_Entity {

   
	private BigDecimal	r11_market;
	private BigDecimal	r11_gpfsr_nom_amt;
	private BigDecimal	r11_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg;
	private BigDecimal	r11_gpfsr_nom_amt1;
	private BigDecimal	r11_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg1;
	private BigDecimal	r11_gpfsr_nom_amt2;
	private BigDecimal	r11_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg2;
	private BigDecimal	r11_tot_spe_ris_chrg;
	private BigDecimal	r11_net_pos_gen_mar_ris;
	private BigDecimal	r11_gen_mar_ris_chrg_8per;
	private BigDecimal	r11_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r11_tot_gen_mar_risk_chrg;
	private BigDecimal	r11_tot_mar_ris_chrg;
	
	private BigDecimal	r12_market;
	private BigDecimal	r12_gpfsr_nom_amt;
	private BigDecimal	r12_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg;
	private BigDecimal	r12_gpfsr_nom_amt1;
	private BigDecimal	r12_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg1;
	private BigDecimal	r12_gpfsr_nom_amt2;
	private BigDecimal	r12_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg2;
	private BigDecimal	r12_tot_spe_ris_chrg;
	private BigDecimal	r12_net_pos_gen_mar_ris;
	private BigDecimal	r12_gen_mar_ris_chrg_8per;
	private BigDecimal	r12_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r12_tot_gen_mar_risk_chrg;
	private BigDecimal	r12_tot_mar_ris_chrg;
	
	private BigDecimal	r13_market;
	private BigDecimal	r13_gpfsr_nom_amt;
	private BigDecimal	r13_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg;
	private BigDecimal	r13_gpfsr_nom_amt1;
	private BigDecimal	r13_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg1;
	private BigDecimal	r13_gpfsr_nom_amt2;
	private BigDecimal	r13_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg2;
	private BigDecimal	r13_tot_spe_ris_chrg;
	private BigDecimal	r13_net_pos_gen_mar_ris;
	private BigDecimal	r13_gen_mar_ris_chrg_8per;
	private BigDecimal	r13_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r13_tot_gen_mar_risk_chrg;
	private BigDecimal	r13_tot_mar_ris_chrg;
	
	private BigDecimal	r14_market;
	private BigDecimal	r14_gpfsr_nom_amt;
	private BigDecimal	r14_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg;
	private BigDecimal	r14_gpfsr_nom_amt1;
	private BigDecimal	r14_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg1;
	private BigDecimal	r14_gpfsr_nom_amt2;
	private BigDecimal	r14_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg2;
	private BigDecimal	r14_tot_spe_ris_chrg;
	private BigDecimal	r14_net_pos_gen_mar_ris;
	private BigDecimal	r14_gen_mar_ris_chrg_8per;
	private BigDecimal	r14_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r14_tot_gen_mar_risk_chrg;
	private BigDecimal	r14_tot_mar_ris_chrg;
	
	private BigDecimal	r15_market;
	private BigDecimal	r15_gpfsr_nom_amt;
	private BigDecimal	r15_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg;
	private BigDecimal	r15_gpfsr_nom_amt1;
	private BigDecimal	r15_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg1;
	private BigDecimal	r15_gpfsr_nom_amt2;
	private BigDecimal	r15_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg2;
	private BigDecimal	r15_tot_spe_ris_chrg;
	private BigDecimal	r15_net_pos_gen_mar_ris;
	private BigDecimal	r15_gen_mar_ris_chrg_8per;
	private BigDecimal	r15_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r15_tot_gen_mar_risk_chrg;
	private BigDecimal	r15_tot_mar_ris_chrg;
	
	private BigDecimal	r16_market;
	private BigDecimal	r16_gpfsr_nom_amt;
	private BigDecimal	r16_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg;
	private BigDecimal	r16_gpfsr_nom_amt1;
	private BigDecimal	r16_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg1;
	private BigDecimal	r16_gpfsr_nom_amt2;
	private BigDecimal	r16_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg2;
	private BigDecimal	r16_tot_spe_ris_chrg;
	private BigDecimal	r16_net_pos_gen_mar_ris;
	private BigDecimal	r16_gen_mar_ris_chrg_8per;
	private BigDecimal	r16_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r16_tot_gen_mar_risk_chrg;
	private BigDecimal	r16_tot_mar_ris_chrg;
	
	private BigDecimal	r17_market;
	private BigDecimal	r17_gpfsr_nom_amt;
	private BigDecimal	r17_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg;
	private BigDecimal	r17_gpfsr_nom_amt1;
	private BigDecimal	r17_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg1;
	private BigDecimal	r17_gpfsr_nom_amt2;
	private BigDecimal	r17_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg2;
	private BigDecimal	r17_tot_spe_ris_chrg;
	private BigDecimal	r17_net_pos_gen_mar_ris;
	private BigDecimal	r17_gen_mar_ris_chrg_8per;
	private BigDecimal	r17_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r17_tot_gen_mar_risk_chrg;
	private BigDecimal	r17_tot_mar_ris_chrg;
	
	private BigDecimal	r18_market;
	private BigDecimal	r18_gpfsr_nom_amt;
	private BigDecimal	r18_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg;
	private BigDecimal	r18_gpfsr_nom_amt1;
	private BigDecimal	r18_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg1;
	private BigDecimal	r18_gpfsr_nom_amt2;
	private BigDecimal	r18_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg2;
	private BigDecimal	r18_tot_spe_ris_chrg;
	private BigDecimal	r18_net_pos_gen_mar_ris;
	private BigDecimal	r18_gen_mar_ris_chrg_8per;
	private BigDecimal	r18_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r18_tot_gen_mar_risk_chrg;
	private BigDecimal	r18_tot_mar_ris_chrg;
	
	private BigDecimal	r19_market;
	private BigDecimal	r19_gpfsr_nom_amt;
	private BigDecimal	r19_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg;
	private BigDecimal	r19_gpfsr_nom_amt1;
	private BigDecimal	r19_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg1;
	private BigDecimal	r19_gpfsr_nom_amt2;
	private BigDecimal	r19_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg2;
	private BigDecimal	r19_tot_spe_ris_chrg;
	private BigDecimal	r19_net_pos_gen_mar_ris;
	private BigDecimal	r19_gen_mar_ris_chrg_8per;
	private BigDecimal	r19_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r19_tot_gen_mar_risk_chrg;
	private BigDecimal	r19_tot_mar_ris_chrg;
	
	private BigDecimal	r20_market;
	private BigDecimal	r20_gpfsr_nom_amt;
	private BigDecimal	r20_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg;
	private BigDecimal	r20_gpfsr_nom_amt1;
	private BigDecimal	r20_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg1;
	private BigDecimal	r20_gpfsr_nom_amt2;
	private BigDecimal	r20_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg2;
	private BigDecimal	r20_tot_spe_ris_chrg;
	private BigDecimal	r20_net_pos_gen_mar_ris;
	private BigDecimal	r20_gen_mar_ris_chrg_8per;
	private BigDecimal	r20_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r20_tot_gen_mar_risk_chrg;
	private BigDecimal	r20_tot_mar_ris_chrg;
	
	private BigDecimal	r21_market;
	private BigDecimal	r21_gpfsr_nom_amt;
	private BigDecimal	r21_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg;
	private BigDecimal	r21_gpfsr_nom_amt1;
	private BigDecimal	r21_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg1;
	private BigDecimal	r21_gpfsr_nom_amt2;
	private BigDecimal	r21_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg2;
	private BigDecimal	r21_tot_spe_ris_chrg;
	private BigDecimal	r21_net_pos_gen_mar_ris;
	private BigDecimal	r21_gen_mar_ris_chrg_8per;
	private BigDecimal	r21_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r21_tot_gen_mar_risk_chrg;
	private BigDecimal	r21_tot_mar_ris_chrg;
	
	private BigDecimal	r22_market;
	private BigDecimal	r22_gpfsr_nom_amt;
	private BigDecimal	r22_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg;
	private BigDecimal	r22_gpfsr_nom_amt1;
	private BigDecimal	r22_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg1;
	private BigDecimal	r22_gpfsr_nom_amt2;
	private BigDecimal	r22_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg2;
	private BigDecimal	r22_tot_spe_ris_chrg;
	private BigDecimal	r22_net_pos_gen_mar_ris;
	private BigDecimal	r22_gen_mar_ris_chrg_8per;
	private BigDecimal	r22_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r22_tot_gen_mar_risk_chrg;
	private BigDecimal	r22_tot_mar_ris_chrg;
	
	private String	r23_market;
	private BigDecimal	r23_gpfsr_nom_amt;
	private BigDecimal	r23_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg;
	private BigDecimal	r23_gpfsr_nom_amt1;
	private BigDecimal	r23_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg1;
	private BigDecimal	r23_gpfsr_nom_amt2;
	private BigDecimal	r23_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg2;
	private BigDecimal	r23_tot_spe_ris_chrg;
	private BigDecimal	r23_net_pos_gen_mar_ris;
	private BigDecimal	r23_gen_mar_ris_chrg_8per;
	private BigDecimal	r23_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r23_tot_gen_mar_risk_chrg;
	private BigDecimal	r23_tot_mar_ris_chrg;
	
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
	
	
public BigDecimal getR11_market() {
		return r11_market;
	}
	public void setR11_market(BigDecimal r11_market) {
		this.r11_market = r11_market;
	}
	public BigDecimal getR11_gpfsr_nom_amt() {
		return r11_gpfsr_nom_amt;
	}
	public void setR11_gpfsr_nom_amt(BigDecimal r11_gpfsr_nom_amt) {
		this.r11_gpfsr_nom_amt = r11_gpfsr_nom_amt;
	}
	public BigDecimal getR11_gpfsr_pos_att8_per_spe_ris() {
		return r11_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att8_per_spe_ris(BigDecimal r11_gpfsr_pos_att8_per_spe_ris) {
		this.r11_gpfsr_pos_att8_per_spe_ris = r11_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg() {
		return r11_gpfsr_chrg;
	}
	public void setR11_gpfsr_chrg(BigDecimal r11_gpfsr_chrg) {
		this.r11_gpfsr_chrg = r11_gpfsr_chrg;
	}
	public BigDecimal getR11_gpfsr_nom_amt1() {
		return r11_gpfsr_nom_amt1;
	}
	public void setR11_gpfsr_nom_amt1(BigDecimal r11_gpfsr_nom_amt1) {
		this.r11_gpfsr_nom_amt1 = r11_gpfsr_nom_amt1;
	}
	public BigDecimal getR11_gpfsr_pos_att4_per_spe_ris() {
		return r11_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att4_per_spe_ris(BigDecimal r11_gpfsr_pos_att4_per_spe_ris) {
		this.r11_gpfsr_pos_att4_per_spe_ris = r11_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg1() {
		return r11_gpfsr_chrg1;
	}
	public void setR11_gpfsr_chrg1(BigDecimal r11_gpfsr_chrg1) {
		this.r11_gpfsr_chrg1 = r11_gpfsr_chrg1;
	}
	public BigDecimal getR11_gpfsr_nom_amt2() {
		return r11_gpfsr_nom_amt2;
	}
	public void setR11_gpfsr_nom_amt2(BigDecimal r11_gpfsr_nom_amt2) {
		this.r11_gpfsr_nom_amt2 = r11_gpfsr_nom_amt2;
	}
	public BigDecimal getR11_gpfsr_pos_att2_per_spe_ris() {
		return r11_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att2_per_spe_ris(BigDecimal r11_gpfsr_pos_att2_per_spe_ris) {
		this.r11_gpfsr_pos_att2_per_spe_ris = r11_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg2() {
		return r11_gpfsr_chrg2;
	}
	public void setR11_gpfsr_chrg2(BigDecimal r11_gpfsr_chrg2) {
		this.r11_gpfsr_chrg2 = r11_gpfsr_chrg2;
	}
	public BigDecimal getR11_tot_spe_ris_chrg() {
		return r11_tot_spe_ris_chrg;
	}
	public void setR11_tot_spe_ris_chrg(BigDecimal r11_tot_spe_ris_chrg) {
		this.r11_tot_spe_ris_chrg = r11_tot_spe_ris_chrg;
	}
	public BigDecimal getR11_net_pos_gen_mar_ris() {
		return r11_net_pos_gen_mar_ris;
	}
	public void setR11_net_pos_gen_mar_ris(BigDecimal r11_net_pos_gen_mar_ris) {
		this.r11_net_pos_gen_mar_ris = r11_net_pos_gen_mar_ris;
	}
	public BigDecimal getR11_gen_mar_ris_chrg_8per() {
		return r11_gen_mar_ris_chrg_8per;
	}
	public void setR11_gen_mar_ris_chrg_8per(BigDecimal r11_gen_mar_ris_chrg_8per) {
		this.r11_gen_mar_ris_chrg_8per = r11_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR11_2per_gen_mar_ris_chrg_div_port() {
		return r11_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR11_2per_gen_mar_ris_chrg_div_port(BigDecimal r11_2per_gen_mar_ris_chrg_div_port) {
		this.r11_2per_gen_mar_ris_chrg_div_port = r11_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR11_tot_gen_mar_risk_chrg() {
		return r11_tot_gen_mar_risk_chrg;
	}
	public void setR11_tot_gen_mar_risk_chrg(BigDecimal r11_tot_gen_mar_risk_chrg) {
		this.r11_tot_gen_mar_risk_chrg = r11_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR11_tot_mar_ris_chrg() {
		return r11_tot_mar_ris_chrg;
	}
	public void setR11_tot_mar_ris_chrg(BigDecimal r11_tot_mar_ris_chrg) {
		this.r11_tot_mar_ris_chrg = r11_tot_mar_ris_chrg;
	}
	public BigDecimal getR12_market() {
		return r12_market;
	}
	public void setR12_market(BigDecimal r12_market) {
		this.r12_market = r12_market;
	}
	public BigDecimal getR12_gpfsr_nom_amt() {
		return r12_gpfsr_nom_amt;
	}
	public void setR12_gpfsr_nom_amt(BigDecimal r12_gpfsr_nom_amt) {
		this.r12_gpfsr_nom_amt = r12_gpfsr_nom_amt;
	}
	public BigDecimal getR12_gpfsr_pos_att8_per_spe_ris() {
		return r12_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att8_per_spe_ris(BigDecimal r12_gpfsr_pos_att8_per_spe_ris) {
		this.r12_gpfsr_pos_att8_per_spe_ris = r12_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg() {
		return r12_gpfsr_chrg;
	}
	public void setR12_gpfsr_chrg(BigDecimal r12_gpfsr_chrg) {
		this.r12_gpfsr_chrg = r12_gpfsr_chrg;
	}
	public BigDecimal getR12_gpfsr_nom_amt1() {
		return r12_gpfsr_nom_amt1;
	}
	public void setR12_gpfsr_nom_amt1(BigDecimal r12_gpfsr_nom_amt1) {
		this.r12_gpfsr_nom_amt1 = r12_gpfsr_nom_amt1;
	}
	public BigDecimal getR12_gpfsr_pos_att4_per_spe_ris() {
		return r12_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att4_per_spe_ris(BigDecimal r12_gpfsr_pos_att4_per_spe_ris) {
		this.r12_gpfsr_pos_att4_per_spe_ris = r12_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg1() {
		return r12_gpfsr_chrg1;
	}
	public void setR12_gpfsr_chrg1(BigDecimal r12_gpfsr_chrg1) {
		this.r12_gpfsr_chrg1 = r12_gpfsr_chrg1;
	}
	public BigDecimal getR12_gpfsr_nom_amt2() {
		return r12_gpfsr_nom_amt2;
	}
	public void setR12_gpfsr_nom_amt2(BigDecimal r12_gpfsr_nom_amt2) {
		this.r12_gpfsr_nom_amt2 = r12_gpfsr_nom_amt2;
	}
	public BigDecimal getR12_gpfsr_pos_att2_per_spe_ris() {
		return r12_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att2_per_spe_ris(BigDecimal r12_gpfsr_pos_att2_per_spe_ris) {
		this.r12_gpfsr_pos_att2_per_spe_ris = r12_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg2() {
		return r12_gpfsr_chrg2;
	}
	public void setR12_gpfsr_chrg2(BigDecimal r12_gpfsr_chrg2) {
		this.r12_gpfsr_chrg2 = r12_gpfsr_chrg2;
	}
	public BigDecimal getR12_tot_spe_ris_chrg() {
		return r12_tot_spe_ris_chrg;
	}
	public void setR12_tot_spe_ris_chrg(BigDecimal r12_tot_spe_ris_chrg) {
		this.r12_tot_spe_ris_chrg = r12_tot_spe_ris_chrg;
	}
	public BigDecimal getR12_net_pos_gen_mar_ris() {
		return r12_net_pos_gen_mar_ris;
	}
	public void setR12_net_pos_gen_mar_ris(BigDecimal r12_net_pos_gen_mar_ris) {
		this.r12_net_pos_gen_mar_ris = r12_net_pos_gen_mar_ris;
	}
	public BigDecimal getR12_gen_mar_ris_chrg_8per() {
		return r12_gen_mar_ris_chrg_8per;
	}
	public void setR12_gen_mar_ris_chrg_8per(BigDecimal r12_gen_mar_ris_chrg_8per) {
		this.r12_gen_mar_ris_chrg_8per = r12_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR12_2per_gen_mar_ris_chrg_div_port() {
		return r12_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR12_2per_gen_mar_ris_chrg_div_port(BigDecimal r12_2per_gen_mar_ris_chrg_div_port) {
		this.r12_2per_gen_mar_ris_chrg_div_port = r12_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR12_tot_gen_mar_risk_chrg() {
		return r12_tot_gen_mar_risk_chrg;
	}
	public void setR12_tot_gen_mar_risk_chrg(BigDecimal r12_tot_gen_mar_risk_chrg) {
		this.r12_tot_gen_mar_risk_chrg = r12_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR12_tot_mar_ris_chrg() {
		return r12_tot_mar_ris_chrg;
	}
	public void setR12_tot_mar_ris_chrg(BigDecimal r12_tot_mar_ris_chrg) {
		this.r12_tot_mar_ris_chrg = r12_tot_mar_ris_chrg;
	}
	public BigDecimal getR13_market() {
		return r13_market;
	}
	public void setR13_market(BigDecimal r13_market) {
		this.r13_market = r13_market;
	}
	public BigDecimal getR13_gpfsr_nom_amt() {
		return r13_gpfsr_nom_amt;
	}
	public void setR13_gpfsr_nom_amt(BigDecimal r13_gpfsr_nom_amt) {
		this.r13_gpfsr_nom_amt = r13_gpfsr_nom_amt;
	}
	public BigDecimal getR13_gpfsr_pos_att8_per_spe_ris() {
		return r13_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att8_per_spe_ris(BigDecimal r13_gpfsr_pos_att8_per_spe_ris) {
		this.r13_gpfsr_pos_att8_per_spe_ris = r13_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg() {
		return r13_gpfsr_chrg;
	}
	public void setR13_gpfsr_chrg(BigDecimal r13_gpfsr_chrg) {
		this.r13_gpfsr_chrg = r13_gpfsr_chrg;
	}
	public BigDecimal getR13_gpfsr_nom_amt1() {
		return r13_gpfsr_nom_amt1;
	}
	public void setR13_gpfsr_nom_amt1(BigDecimal r13_gpfsr_nom_amt1) {
		this.r13_gpfsr_nom_amt1 = r13_gpfsr_nom_amt1;
	}
	public BigDecimal getR13_gpfsr_pos_att4_per_spe_ris() {
		return r13_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att4_per_spe_ris(BigDecimal r13_gpfsr_pos_att4_per_spe_ris) {
		this.r13_gpfsr_pos_att4_per_spe_ris = r13_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg1() {
		return r13_gpfsr_chrg1;
	}
	public void setR13_gpfsr_chrg1(BigDecimal r13_gpfsr_chrg1) {
		this.r13_gpfsr_chrg1 = r13_gpfsr_chrg1;
	}
	public BigDecimal getR13_gpfsr_nom_amt2() {
		return r13_gpfsr_nom_amt2;
	}
	public void setR13_gpfsr_nom_amt2(BigDecimal r13_gpfsr_nom_amt2) {
		this.r13_gpfsr_nom_amt2 = r13_gpfsr_nom_amt2;
	}
	public BigDecimal getR13_gpfsr_pos_att2_per_spe_ris() {
		return r13_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att2_per_spe_ris(BigDecimal r13_gpfsr_pos_att2_per_spe_ris) {
		this.r13_gpfsr_pos_att2_per_spe_ris = r13_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg2() {
		return r13_gpfsr_chrg2;
	}
	public void setR13_gpfsr_chrg2(BigDecimal r13_gpfsr_chrg2) {
		this.r13_gpfsr_chrg2 = r13_gpfsr_chrg2;
	}
	public BigDecimal getR13_tot_spe_ris_chrg() {
		return r13_tot_spe_ris_chrg;
	}
	public void setR13_tot_spe_ris_chrg(BigDecimal r13_tot_spe_ris_chrg) {
		this.r13_tot_spe_ris_chrg = r13_tot_spe_ris_chrg;
	}
	public BigDecimal getR13_net_pos_gen_mar_ris() {
		return r13_net_pos_gen_mar_ris;
	}
	public void setR13_net_pos_gen_mar_ris(BigDecimal r13_net_pos_gen_mar_ris) {
		this.r13_net_pos_gen_mar_ris = r13_net_pos_gen_mar_ris;
	}
	public BigDecimal getR13_gen_mar_ris_chrg_8per() {
		return r13_gen_mar_ris_chrg_8per;
	}
	public void setR13_gen_mar_ris_chrg_8per(BigDecimal r13_gen_mar_ris_chrg_8per) {
		this.r13_gen_mar_ris_chrg_8per = r13_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR13_2per_gen_mar_ris_chrg_div_port() {
		return r13_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR13_2per_gen_mar_ris_chrg_div_port(BigDecimal r13_2per_gen_mar_ris_chrg_div_port) {
		this.r13_2per_gen_mar_ris_chrg_div_port = r13_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR13_tot_gen_mar_risk_chrg() {
		return r13_tot_gen_mar_risk_chrg;
	}
	public void setR13_tot_gen_mar_risk_chrg(BigDecimal r13_tot_gen_mar_risk_chrg) {
		this.r13_tot_gen_mar_risk_chrg = r13_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR13_tot_mar_ris_chrg() {
		return r13_tot_mar_ris_chrg;
	}
	public void setR13_tot_mar_ris_chrg(BigDecimal r13_tot_mar_ris_chrg) {
		this.r13_tot_mar_ris_chrg = r13_tot_mar_ris_chrg;
	}
	public BigDecimal getR14_market() {
		return r14_market;
	}
	public void setR14_market(BigDecimal r14_market) {
		this.r14_market = r14_market;
	}
	public BigDecimal getR14_gpfsr_nom_amt() {
		return r14_gpfsr_nom_amt;
	}
	public void setR14_gpfsr_nom_amt(BigDecimal r14_gpfsr_nom_amt) {
		this.r14_gpfsr_nom_amt = r14_gpfsr_nom_amt;
	}
	public BigDecimal getR14_gpfsr_pos_att8_per_spe_ris() {
		return r14_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att8_per_spe_ris(BigDecimal r14_gpfsr_pos_att8_per_spe_ris) {
		this.r14_gpfsr_pos_att8_per_spe_ris = r14_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg() {
		return r14_gpfsr_chrg;
	}
	public void setR14_gpfsr_chrg(BigDecimal r14_gpfsr_chrg) {
		this.r14_gpfsr_chrg = r14_gpfsr_chrg;
	}
	public BigDecimal getR14_gpfsr_nom_amt1() {
		return r14_gpfsr_nom_amt1;
	}
	public void setR14_gpfsr_nom_amt1(BigDecimal r14_gpfsr_nom_amt1) {
		this.r14_gpfsr_nom_amt1 = r14_gpfsr_nom_amt1;
	}
	public BigDecimal getR14_gpfsr_pos_att4_per_spe_ris() {
		return r14_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att4_per_spe_ris(BigDecimal r14_gpfsr_pos_att4_per_spe_ris) {
		this.r14_gpfsr_pos_att4_per_spe_ris = r14_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg1() {
		return r14_gpfsr_chrg1;
	}
	public void setR14_gpfsr_chrg1(BigDecimal r14_gpfsr_chrg1) {
		this.r14_gpfsr_chrg1 = r14_gpfsr_chrg1;
	}
	public BigDecimal getR14_gpfsr_nom_amt2() {
		return r14_gpfsr_nom_amt2;
	}
	public void setR14_gpfsr_nom_amt2(BigDecimal r14_gpfsr_nom_amt2) {
		this.r14_gpfsr_nom_amt2 = r14_gpfsr_nom_amt2;
	}
	public BigDecimal getR14_gpfsr_pos_att2_per_spe_ris() {
		return r14_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att2_per_spe_ris(BigDecimal r14_gpfsr_pos_att2_per_spe_ris) {
		this.r14_gpfsr_pos_att2_per_spe_ris = r14_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg2() {
		return r14_gpfsr_chrg2;
	}
	public void setR14_gpfsr_chrg2(BigDecimal r14_gpfsr_chrg2) {
		this.r14_gpfsr_chrg2 = r14_gpfsr_chrg2;
	}
	public BigDecimal getR14_tot_spe_ris_chrg() {
		return r14_tot_spe_ris_chrg;
	}
	public void setR14_tot_spe_ris_chrg(BigDecimal r14_tot_spe_ris_chrg) {
		this.r14_tot_spe_ris_chrg = r14_tot_spe_ris_chrg;
	}
	public BigDecimal getR14_net_pos_gen_mar_ris() {
		return r14_net_pos_gen_mar_ris;
	}
	public void setR14_net_pos_gen_mar_ris(BigDecimal r14_net_pos_gen_mar_ris) {
		this.r14_net_pos_gen_mar_ris = r14_net_pos_gen_mar_ris;
	}
	public BigDecimal getR14_gen_mar_ris_chrg_8per() {
		return r14_gen_mar_ris_chrg_8per;
	}
	public void setR14_gen_mar_ris_chrg_8per(BigDecimal r14_gen_mar_ris_chrg_8per) {
		this.r14_gen_mar_ris_chrg_8per = r14_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR14_2per_gen_mar_ris_chrg_div_port() {
		return r14_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR14_2per_gen_mar_ris_chrg_div_port(BigDecimal r14_2per_gen_mar_ris_chrg_div_port) {
		this.r14_2per_gen_mar_ris_chrg_div_port = r14_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR14_tot_gen_mar_risk_chrg() {
		return r14_tot_gen_mar_risk_chrg;
	}
	public void setR14_tot_gen_mar_risk_chrg(BigDecimal r14_tot_gen_mar_risk_chrg) {
		this.r14_tot_gen_mar_risk_chrg = r14_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR14_tot_mar_ris_chrg() {
		return r14_tot_mar_ris_chrg;
	}
	public void setR14_tot_mar_ris_chrg(BigDecimal r14_tot_mar_ris_chrg) {
		this.r14_tot_mar_ris_chrg = r14_tot_mar_ris_chrg;
	}
	public BigDecimal getR15_market() {
		return r15_market;
	}
	public void setR15_market(BigDecimal r15_market) {
		this.r15_market = r15_market;
	}
	public BigDecimal getR15_gpfsr_nom_amt() {
		return r15_gpfsr_nom_amt;
	}
	public void setR15_gpfsr_nom_amt(BigDecimal r15_gpfsr_nom_amt) {
		this.r15_gpfsr_nom_amt = r15_gpfsr_nom_amt;
	}
	public BigDecimal getR15_gpfsr_pos_att8_per_spe_ris() {
		return r15_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att8_per_spe_ris(BigDecimal r15_gpfsr_pos_att8_per_spe_ris) {
		this.r15_gpfsr_pos_att8_per_spe_ris = r15_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg() {
		return r15_gpfsr_chrg;
	}
	public void setR15_gpfsr_chrg(BigDecimal r15_gpfsr_chrg) {
		this.r15_gpfsr_chrg = r15_gpfsr_chrg;
	}
	public BigDecimal getR15_gpfsr_nom_amt1() {
		return r15_gpfsr_nom_amt1;
	}
	public void setR15_gpfsr_nom_amt1(BigDecimal r15_gpfsr_nom_amt1) {
		this.r15_gpfsr_nom_amt1 = r15_gpfsr_nom_amt1;
	}
	public BigDecimal getR15_gpfsr_pos_att4_per_spe_ris() {
		return r15_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att4_per_spe_ris(BigDecimal r15_gpfsr_pos_att4_per_spe_ris) {
		this.r15_gpfsr_pos_att4_per_spe_ris = r15_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg1() {
		return r15_gpfsr_chrg1;
	}
	public void setR15_gpfsr_chrg1(BigDecimal r15_gpfsr_chrg1) {
		this.r15_gpfsr_chrg1 = r15_gpfsr_chrg1;
	}
	public BigDecimal getR15_gpfsr_nom_amt2() {
		return r15_gpfsr_nom_amt2;
	}
	public void setR15_gpfsr_nom_amt2(BigDecimal r15_gpfsr_nom_amt2) {
		this.r15_gpfsr_nom_amt2 = r15_gpfsr_nom_amt2;
	}
	public BigDecimal getR15_gpfsr_pos_att2_per_spe_ris() {
		return r15_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att2_per_spe_ris(BigDecimal r15_gpfsr_pos_att2_per_spe_ris) {
		this.r15_gpfsr_pos_att2_per_spe_ris = r15_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg2() {
		return r15_gpfsr_chrg2;
	}
	public void setR15_gpfsr_chrg2(BigDecimal r15_gpfsr_chrg2) {
		this.r15_gpfsr_chrg2 = r15_gpfsr_chrg2;
	}
	public BigDecimal getR15_tot_spe_ris_chrg() {
		return r15_tot_spe_ris_chrg;
	}
	public void setR15_tot_spe_ris_chrg(BigDecimal r15_tot_spe_ris_chrg) {
		this.r15_tot_spe_ris_chrg = r15_tot_spe_ris_chrg;
	}
	public BigDecimal getR15_net_pos_gen_mar_ris() {
		return r15_net_pos_gen_mar_ris;
	}
	public void setR15_net_pos_gen_mar_ris(BigDecimal r15_net_pos_gen_mar_ris) {
		this.r15_net_pos_gen_mar_ris = r15_net_pos_gen_mar_ris;
	}
	public BigDecimal getR15_gen_mar_ris_chrg_8per() {
		return r15_gen_mar_ris_chrg_8per;
	}
	public void setR15_gen_mar_ris_chrg_8per(BigDecimal r15_gen_mar_ris_chrg_8per) {
		this.r15_gen_mar_ris_chrg_8per = r15_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR15_2per_gen_mar_ris_chrg_div_port() {
		return r15_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR15_2per_gen_mar_ris_chrg_div_port(BigDecimal r15_2per_gen_mar_ris_chrg_div_port) {
		this.r15_2per_gen_mar_ris_chrg_div_port = r15_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR15_tot_gen_mar_risk_chrg() {
		return r15_tot_gen_mar_risk_chrg;
	}
	public void setR15_tot_gen_mar_risk_chrg(BigDecimal r15_tot_gen_mar_risk_chrg) {
		this.r15_tot_gen_mar_risk_chrg = r15_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR15_tot_mar_ris_chrg() {
		return r15_tot_mar_ris_chrg;
	}
	public void setR15_tot_mar_ris_chrg(BigDecimal r15_tot_mar_ris_chrg) {
		this.r15_tot_mar_ris_chrg = r15_tot_mar_ris_chrg;
	}
	public BigDecimal getR16_market() {
		return r16_market;
	}
	public void setR16_market(BigDecimal r16_market) {
		this.r16_market = r16_market;
	}
	public BigDecimal getR16_gpfsr_nom_amt() {
		return r16_gpfsr_nom_amt;
	}
	public void setR16_gpfsr_nom_amt(BigDecimal r16_gpfsr_nom_amt) {
		this.r16_gpfsr_nom_amt = r16_gpfsr_nom_amt;
	}
	public BigDecimal getR16_gpfsr_pos_att8_per_spe_ris() {
		return r16_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att8_per_spe_ris(BigDecimal r16_gpfsr_pos_att8_per_spe_ris) {
		this.r16_gpfsr_pos_att8_per_spe_ris = r16_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg() {
		return r16_gpfsr_chrg;
	}
	public void setR16_gpfsr_chrg(BigDecimal r16_gpfsr_chrg) {
		this.r16_gpfsr_chrg = r16_gpfsr_chrg;
	}
	public BigDecimal getR16_gpfsr_nom_amt1() {
		return r16_gpfsr_nom_amt1;
	}
	public void setR16_gpfsr_nom_amt1(BigDecimal r16_gpfsr_nom_amt1) {
		this.r16_gpfsr_nom_amt1 = r16_gpfsr_nom_amt1;
	}
	public BigDecimal getR16_gpfsr_pos_att4_per_spe_ris() {
		return r16_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att4_per_spe_ris(BigDecimal r16_gpfsr_pos_att4_per_spe_ris) {
		this.r16_gpfsr_pos_att4_per_spe_ris = r16_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg1() {
		return r16_gpfsr_chrg1;
	}
	public void setR16_gpfsr_chrg1(BigDecimal r16_gpfsr_chrg1) {
		this.r16_gpfsr_chrg1 = r16_gpfsr_chrg1;
	}
	public BigDecimal getR16_gpfsr_nom_amt2() {
		return r16_gpfsr_nom_amt2;
	}
	public void setR16_gpfsr_nom_amt2(BigDecimal r16_gpfsr_nom_amt2) {
		this.r16_gpfsr_nom_amt2 = r16_gpfsr_nom_amt2;
	}
	public BigDecimal getR16_gpfsr_pos_att2_per_spe_ris() {
		return r16_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att2_per_spe_ris(BigDecimal r16_gpfsr_pos_att2_per_spe_ris) {
		this.r16_gpfsr_pos_att2_per_spe_ris = r16_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg2() {
		return r16_gpfsr_chrg2;
	}
	public void setR16_gpfsr_chrg2(BigDecimal r16_gpfsr_chrg2) {
		this.r16_gpfsr_chrg2 = r16_gpfsr_chrg2;
	}
	public BigDecimal getR16_tot_spe_ris_chrg() {
		return r16_tot_spe_ris_chrg;
	}
	public void setR16_tot_spe_ris_chrg(BigDecimal r16_tot_spe_ris_chrg) {
		this.r16_tot_spe_ris_chrg = r16_tot_spe_ris_chrg;
	}
	public BigDecimal getR16_net_pos_gen_mar_ris() {
		return r16_net_pos_gen_mar_ris;
	}
	public void setR16_net_pos_gen_mar_ris(BigDecimal r16_net_pos_gen_mar_ris) {
		this.r16_net_pos_gen_mar_ris = r16_net_pos_gen_mar_ris;
	}
	public BigDecimal getR16_gen_mar_ris_chrg_8per() {
		return r16_gen_mar_ris_chrg_8per;
	}
	public void setR16_gen_mar_ris_chrg_8per(BigDecimal r16_gen_mar_ris_chrg_8per) {
		this.r16_gen_mar_ris_chrg_8per = r16_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR16_2per_gen_mar_ris_chrg_div_port() {
		return r16_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR16_2per_gen_mar_ris_chrg_div_port(BigDecimal r16_2per_gen_mar_ris_chrg_div_port) {
		this.r16_2per_gen_mar_ris_chrg_div_port = r16_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR16_tot_gen_mar_risk_chrg() {
		return r16_tot_gen_mar_risk_chrg;
	}
	public void setR16_tot_gen_mar_risk_chrg(BigDecimal r16_tot_gen_mar_risk_chrg) {
		this.r16_tot_gen_mar_risk_chrg = r16_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR16_tot_mar_ris_chrg() {
		return r16_tot_mar_ris_chrg;
	}
	public void setR16_tot_mar_ris_chrg(BigDecimal r16_tot_mar_ris_chrg) {
		this.r16_tot_mar_ris_chrg = r16_tot_mar_ris_chrg;
	}
	public BigDecimal getR17_market() {
		return r17_market;
	}
	public void setR17_market(BigDecimal r17_market) {
		this.r17_market = r17_market;
	}
	public BigDecimal getR17_gpfsr_nom_amt() {
		return r17_gpfsr_nom_amt;
	}
	public void setR17_gpfsr_nom_amt(BigDecimal r17_gpfsr_nom_amt) {
		this.r17_gpfsr_nom_amt = r17_gpfsr_nom_amt;
	}
	public BigDecimal getR17_gpfsr_pos_att8_per_spe_ris() {
		return r17_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att8_per_spe_ris(BigDecimal r17_gpfsr_pos_att8_per_spe_ris) {
		this.r17_gpfsr_pos_att8_per_spe_ris = r17_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg() {
		return r17_gpfsr_chrg;
	}
	public void setR17_gpfsr_chrg(BigDecimal r17_gpfsr_chrg) {
		this.r17_gpfsr_chrg = r17_gpfsr_chrg;
	}
	public BigDecimal getR17_gpfsr_nom_amt1() {
		return r17_gpfsr_nom_amt1;
	}
	public void setR17_gpfsr_nom_amt1(BigDecimal r17_gpfsr_nom_amt1) {
		this.r17_gpfsr_nom_amt1 = r17_gpfsr_nom_amt1;
	}
	public BigDecimal getR17_gpfsr_pos_att4_per_spe_ris() {
		return r17_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att4_per_spe_ris(BigDecimal r17_gpfsr_pos_att4_per_spe_ris) {
		this.r17_gpfsr_pos_att4_per_spe_ris = r17_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg1() {
		return r17_gpfsr_chrg1;
	}
	public void setR17_gpfsr_chrg1(BigDecimal r17_gpfsr_chrg1) {
		this.r17_gpfsr_chrg1 = r17_gpfsr_chrg1;
	}
	public BigDecimal getR17_gpfsr_nom_amt2() {
		return r17_gpfsr_nom_amt2;
	}
	public void setR17_gpfsr_nom_amt2(BigDecimal r17_gpfsr_nom_amt2) {
		this.r17_gpfsr_nom_amt2 = r17_gpfsr_nom_amt2;
	}
	public BigDecimal getR17_gpfsr_pos_att2_per_spe_ris() {
		return r17_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att2_per_spe_ris(BigDecimal r17_gpfsr_pos_att2_per_spe_ris) {
		this.r17_gpfsr_pos_att2_per_spe_ris = r17_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg2() {
		return r17_gpfsr_chrg2;
	}
	public void setR17_gpfsr_chrg2(BigDecimal r17_gpfsr_chrg2) {
		this.r17_gpfsr_chrg2 = r17_gpfsr_chrg2;
	}
	public BigDecimal getR17_tot_spe_ris_chrg() {
		return r17_tot_spe_ris_chrg;
	}
	public void setR17_tot_spe_ris_chrg(BigDecimal r17_tot_spe_ris_chrg) {
		this.r17_tot_spe_ris_chrg = r17_tot_spe_ris_chrg;
	}
	public BigDecimal getR17_net_pos_gen_mar_ris() {
		return r17_net_pos_gen_mar_ris;
	}
	public void setR17_net_pos_gen_mar_ris(BigDecimal r17_net_pos_gen_mar_ris) {
		this.r17_net_pos_gen_mar_ris = r17_net_pos_gen_mar_ris;
	}
	public BigDecimal getR17_gen_mar_ris_chrg_8per() {
		return r17_gen_mar_ris_chrg_8per;
	}
	public void setR17_gen_mar_ris_chrg_8per(BigDecimal r17_gen_mar_ris_chrg_8per) {
		this.r17_gen_mar_ris_chrg_8per = r17_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR17_2per_gen_mar_ris_chrg_div_port() {
		return r17_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR17_2per_gen_mar_ris_chrg_div_port(BigDecimal r17_2per_gen_mar_ris_chrg_div_port) {
		this.r17_2per_gen_mar_ris_chrg_div_port = r17_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR17_tot_gen_mar_risk_chrg() {
		return r17_tot_gen_mar_risk_chrg;
	}
	public void setR17_tot_gen_mar_risk_chrg(BigDecimal r17_tot_gen_mar_risk_chrg) {
		this.r17_tot_gen_mar_risk_chrg = r17_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR17_tot_mar_ris_chrg() {
		return r17_tot_mar_ris_chrg;
	}
	public void setR17_tot_mar_ris_chrg(BigDecimal r17_tot_mar_ris_chrg) {
		this.r17_tot_mar_ris_chrg = r17_tot_mar_ris_chrg;
	}
	public BigDecimal getR18_market() {
		return r18_market;
	}
	public void setR18_market(BigDecimal r18_market) {
		this.r18_market = r18_market;
	}
	public BigDecimal getR18_gpfsr_nom_amt() {
		return r18_gpfsr_nom_amt;
	}
	public void setR18_gpfsr_nom_amt(BigDecimal r18_gpfsr_nom_amt) {
		this.r18_gpfsr_nom_amt = r18_gpfsr_nom_amt;
	}
	public BigDecimal getR18_gpfsr_pos_att8_per_spe_ris() {
		return r18_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att8_per_spe_ris(BigDecimal r18_gpfsr_pos_att8_per_spe_ris) {
		this.r18_gpfsr_pos_att8_per_spe_ris = r18_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg() {
		return r18_gpfsr_chrg;
	}
	public void setR18_gpfsr_chrg(BigDecimal r18_gpfsr_chrg) {
		this.r18_gpfsr_chrg = r18_gpfsr_chrg;
	}
	public BigDecimal getR18_gpfsr_nom_amt1() {
		return r18_gpfsr_nom_amt1;
	}
	public void setR18_gpfsr_nom_amt1(BigDecimal r18_gpfsr_nom_amt1) {
		this.r18_gpfsr_nom_amt1 = r18_gpfsr_nom_amt1;
	}
	public BigDecimal getR18_gpfsr_pos_att4_per_spe_ris() {
		return r18_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att4_per_spe_ris(BigDecimal r18_gpfsr_pos_att4_per_spe_ris) {
		this.r18_gpfsr_pos_att4_per_spe_ris = r18_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg1() {
		return r18_gpfsr_chrg1;
	}
	public void setR18_gpfsr_chrg1(BigDecimal r18_gpfsr_chrg1) {
		this.r18_gpfsr_chrg1 = r18_gpfsr_chrg1;
	}
	public BigDecimal getR18_gpfsr_nom_amt2() {
		return r18_gpfsr_nom_amt2;
	}
	public void setR18_gpfsr_nom_amt2(BigDecimal r18_gpfsr_nom_amt2) {
		this.r18_gpfsr_nom_amt2 = r18_gpfsr_nom_amt2;
	}
	public BigDecimal getR18_gpfsr_pos_att2_per_spe_ris() {
		return r18_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att2_per_spe_ris(BigDecimal r18_gpfsr_pos_att2_per_spe_ris) {
		this.r18_gpfsr_pos_att2_per_spe_ris = r18_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg2() {
		return r18_gpfsr_chrg2;
	}
	public void setR18_gpfsr_chrg2(BigDecimal r18_gpfsr_chrg2) {
		this.r18_gpfsr_chrg2 = r18_gpfsr_chrg2;
	}
	public BigDecimal getR18_tot_spe_ris_chrg() {
		return r18_tot_spe_ris_chrg;
	}
	public void setR18_tot_spe_ris_chrg(BigDecimal r18_tot_spe_ris_chrg) {
		this.r18_tot_spe_ris_chrg = r18_tot_spe_ris_chrg;
	}
	public BigDecimal getR18_net_pos_gen_mar_ris() {
		return r18_net_pos_gen_mar_ris;
	}
	public void setR18_net_pos_gen_mar_ris(BigDecimal r18_net_pos_gen_mar_ris) {
		this.r18_net_pos_gen_mar_ris = r18_net_pos_gen_mar_ris;
	}
	public BigDecimal getR18_gen_mar_ris_chrg_8per() {
		return r18_gen_mar_ris_chrg_8per;
	}
	public void setR18_gen_mar_ris_chrg_8per(BigDecimal r18_gen_mar_ris_chrg_8per) {
		this.r18_gen_mar_ris_chrg_8per = r18_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR18_2per_gen_mar_ris_chrg_div_port() {
		return r18_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR18_2per_gen_mar_ris_chrg_div_port(BigDecimal r18_2per_gen_mar_ris_chrg_div_port) {
		this.r18_2per_gen_mar_ris_chrg_div_port = r18_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR18_tot_gen_mar_risk_chrg() {
		return r18_tot_gen_mar_risk_chrg;
	}
	public void setR18_tot_gen_mar_risk_chrg(BigDecimal r18_tot_gen_mar_risk_chrg) {
		this.r18_tot_gen_mar_risk_chrg = r18_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR18_tot_mar_ris_chrg() {
		return r18_tot_mar_ris_chrg;
	}
	public void setR18_tot_mar_ris_chrg(BigDecimal r18_tot_mar_ris_chrg) {
		this.r18_tot_mar_ris_chrg = r18_tot_mar_ris_chrg;
	}
	public BigDecimal getR19_market() {
		return r19_market;
	}
	public void setR19_market(BigDecimal r19_market) {
		this.r19_market = r19_market;
	}
	public BigDecimal getR19_gpfsr_nom_amt() {
		return r19_gpfsr_nom_amt;
	}
	public void setR19_gpfsr_nom_amt(BigDecimal r19_gpfsr_nom_amt) {
		this.r19_gpfsr_nom_amt = r19_gpfsr_nom_amt;
	}
	public BigDecimal getR19_gpfsr_pos_att8_per_spe_ris() {
		return r19_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att8_per_spe_ris(BigDecimal r19_gpfsr_pos_att8_per_spe_ris) {
		this.r19_gpfsr_pos_att8_per_spe_ris = r19_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg() {
		return r19_gpfsr_chrg;
	}
	public void setR19_gpfsr_chrg(BigDecimal r19_gpfsr_chrg) {
		this.r19_gpfsr_chrg = r19_gpfsr_chrg;
	}
	public BigDecimal getR19_gpfsr_nom_amt1() {
		return r19_gpfsr_nom_amt1;
	}
	public void setR19_gpfsr_nom_amt1(BigDecimal r19_gpfsr_nom_amt1) {
		this.r19_gpfsr_nom_amt1 = r19_gpfsr_nom_amt1;
	}
	public BigDecimal getR19_gpfsr_pos_att4_per_spe_ris() {
		return r19_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att4_per_spe_ris(BigDecimal r19_gpfsr_pos_att4_per_spe_ris) {
		this.r19_gpfsr_pos_att4_per_spe_ris = r19_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg1() {
		return r19_gpfsr_chrg1;
	}
	public void setR19_gpfsr_chrg1(BigDecimal r19_gpfsr_chrg1) {
		this.r19_gpfsr_chrg1 = r19_gpfsr_chrg1;
	}
	public BigDecimal getR19_gpfsr_nom_amt2() {
		return r19_gpfsr_nom_amt2;
	}
	public void setR19_gpfsr_nom_amt2(BigDecimal r19_gpfsr_nom_amt2) {
		this.r19_gpfsr_nom_amt2 = r19_gpfsr_nom_amt2;
	}
	public BigDecimal getR19_gpfsr_pos_att2_per_spe_ris() {
		return r19_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att2_per_spe_ris(BigDecimal r19_gpfsr_pos_att2_per_spe_ris) {
		this.r19_gpfsr_pos_att2_per_spe_ris = r19_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg2() {
		return r19_gpfsr_chrg2;
	}
	public void setR19_gpfsr_chrg2(BigDecimal r19_gpfsr_chrg2) {
		this.r19_gpfsr_chrg2 = r19_gpfsr_chrg2;
	}
	public BigDecimal getR19_tot_spe_ris_chrg() {
		return r19_tot_spe_ris_chrg;
	}
	public void setR19_tot_spe_ris_chrg(BigDecimal r19_tot_spe_ris_chrg) {
		this.r19_tot_spe_ris_chrg = r19_tot_spe_ris_chrg;
	}
	public BigDecimal getR19_net_pos_gen_mar_ris() {
		return r19_net_pos_gen_mar_ris;
	}
	public void setR19_net_pos_gen_mar_ris(BigDecimal r19_net_pos_gen_mar_ris) {
		this.r19_net_pos_gen_mar_ris = r19_net_pos_gen_mar_ris;
	}
	public BigDecimal getR19_gen_mar_ris_chrg_8per() {
		return r19_gen_mar_ris_chrg_8per;
	}
	public void setR19_gen_mar_ris_chrg_8per(BigDecimal r19_gen_mar_ris_chrg_8per) {
		this.r19_gen_mar_ris_chrg_8per = r19_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR19_2per_gen_mar_ris_chrg_div_port() {
		return r19_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR19_2per_gen_mar_ris_chrg_div_port(BigDecimal r19_2per_gen_mar_ris_chrg_div_port) {
		this.r19_2per_gen_mar_ris_chrg_div_port = r19_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR19_tot_gen_mar_risk_chrg() {
		return r19_tot_gen_mar_risk_chrg;
	}
	public void setR19_tot_gen_mar_risk_chrg(BigDecimal r19_tot_gen_mar_risk_chrg) {
		this.r19_tot_gen_mar_risk_chrg = r19_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR19_tot_mar_ris_chrg() {
		return r19_tot_mar_ris_chrg;
	}
	public void setR19_tot_mar_ris_chrg(BigDecimal r19_tot_mar_ris_chrg) {
		this.r19_tot_mar_ris_chrg = r19_tot_mar_ris_chrg;
	}
	public BigDecimal getR20_market() {
		return r20_market;
	}
	public void setR20_market(BigDecimal r20_market) {
		this.r20_market = r20_market;
	}
	public BigDecimal getR20_gpfsr_nom_amt() {
		return r20_gpfsr_nom_amt;
	}
	public void setR20_gpfsr_nom_amt(BigDecimal r20_gpfsr_nom_amt) {
		this.r20_gpfsr_nom_amt = r20_gpfsr_nom_amt;
	}
	public BigDecimal getR20_gpfsr_pos_att8_per_spe_ris() {
		return r20_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att8_per_spe_ris(BigDecimal r20_gpfsr_pos_att8_per_spe_ris) {
		this.r20_gpfsr_pos_att8_per_spe_ris = r20_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg() {
		return r20_gpfsr_chrg;
	}
	public void setR20_gpfsr_chrg(BigDecimal r20_gpfsr_chrg) {
		this.r20_gpfsr_chrg = r20_gpfsr_chrg;
	}
	public BigDecimal getR20_gpfsr_nom_amt1() {
		return r20_gpfsr_nom_amt1;
	}
	public void setR20_gpfsr_nom_amt1(BigDecimal r20_gpfsr_nom_amt1) {
		this.r20_gpfsr_nom_amt1 = r20_gpfsr_nom_amt1;
	}
	public BigDecimal getR20_gpfsr_pos_att4_per_spe_ris() {
		return r20_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att4_per_spe_ris(BigDecimal r20_gpfsr_pos_att4_per_spe_ris) {
		this.r20_gpfsr_pos_att4_per_spe_ris = r20_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg1() {
		return r20_gpfsr_chrg1;
	}
	public void setR20_gpfsr_chrg1(BigDecimal r20_gpfsr_chrg1) {
		this.r20_gpfsr_chrg1 = r20_gpfsr_chrg1;
	}
	public BigDecimal getR20_gpfsr_nom_amt2() {
		return r20_gpfsr_nom_amt2;
	}
	public void setR20_gpfsr_nom_amt2(BigDecimal r20_gpfsr_nom_amt2) {
		this.r20_gpfsr_nom_amt2 = r20_gpfsr_nom_amt2;
	}
	public BigDecimal getR20_gpfsr_pos_att2_per_spe_ris() {
		return r20_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att2_per_spe_ris(BigDecimal r20_gpfsr_pos_att2_per_spe_ris) {
		this.r20_gpfsr_pos_att2_per_spe_ris = r20_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg2() {
		return r20_gpfsr_chrg2;
	}
	public void setR20_gpfsr_chrg2(BigDecimal r20_gpfsr_chrg2) {
		this.r20_gpfsr_chrg2 = r20_gpfsr_chrg2;
	}
	public BigDecimal getR20_tot_spe_ris_chrg() {
		return r20_tot_spe_ris_chrg;
	}
	public void setR20_tot_spe_ris_chrg(BigDecimal r20_tot_spe_ris_chrg) {
		this.r20_tot_spe_ris_chrg = r20_tot_spe_ris_chrg;
	}
	public BigDecimal getR20_net_pos_gen_mar_ris() {
		return r20_net_pos_gen_mar_ris;
	}
	public void setR20_net_pos_gen_mar_ris(BigDecimal r20_net_pos_gen_mar_ris) {
		this.r20_net_pos_gen_mar_ris = r20_net_pos_gen_mar_ris;
	}
	public BigDecimal getR20_gen_mar_ris_chrg_8per() {
		return r20_gen_mar_ris_chrg_8per;
	}
	public void setR20_gen_mar_ris_chrg_8per(BigDecimal r20_gen_mar_ris_chrg_8per) {
		this.r20_gen_mar_ris_chrg_8per = r20_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR20_2per_gen_mar_ris_chrg_div_port() {
		return r20_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR20_2per_gen_mar_ris_chrg_div_port(BigDecimal r20_2per_gen_mar_ris_chrg_div_port) {
		this.r20_2per_gen_mar_ris_chrg_div_port = r20_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR20_tot_gen_mar_risk_chrg() {
		return r20_tot_gen_mar_risk_chrg;
	}
	public void setR20_tot_gen_mar_risk_chrg(BigDecimal r20_tot_gen_mar_risk_chrg) {
		this.r20_tot_gen_mar_risk_chrg = r20_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR20_tot_mar_ris_chrg() {
		return r20_tot_mar_ris_chrg;
	}
	public void setR20_tot_mar_ris_chrg(BigDecimal r20_tot_mar_ris_chrg) {
		this.r20_tot_mar_ris_chrg = r20_tot_mar_ris_chrg;
	}
	public BigDecimal getR21_market() {
		return r21_market;
	}
	public void setR21_market(BigDecimal r21_market) {
		this.r21_market = r21_market;
	}
	public BigDecimal getR21_gpfsr_nom_amt() {
		return r21_gpfsr_nom_amt;
	}
	public void setR21_gpfsr_nom_amt(BigDecimal r21_gpfsr_nom_amt) {
		this.r21_gpfsr_nom_amt = r21_gpfsr_nom_amt;
	}
	public BigDecimal getR21_gpfsr_pos_att8_per_spe_ris() {
		return r21_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att8_per_spe_ris(BigDecimal r21_gpfsr_pos_att8_per_spe_ris) {
		this.r21_gpfsr_pos_att8_per_spe_ris = r21_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg() {
		return r21_gpfsr_chrg;
	}
	public void setR21_gpfsr_chrg(BigDecimal r21_gpfsr_chrg) {
		this.r21_gpfsr_chrg = r21_gpfsr_chrg;
	}
	public BigDecimal getR21_gpfsr_nom_amt1() {
		return r21_gpfsr_nom_amt1;
	}
	public void setR21_gpfsr_nom_amt1(BigDecimal r21_gpfsr_nom_amt1) {
		this.r21_gpfsr_nom_amt1 = r21_gpfsr_nom_amt1;
	}
	public BigDecimal getR21_gpfsr_pos_att4_per_spe_ris() {
		return r21_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att4_per_spe_ris(BigDecimal r21_gpfsr_pos_att4_per_spe_ris) {
		this.r21_gpfsr_pos_att4_per_spe_ris = r21_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg1() {
		return r21_gpfsr_chrg1;
	}
	public void setR21_gpfsr_chrg1(BigDecimal r21_gpfsr_chrg1) {
		this.r21_gpfsr_chrg1 = r21_gpfsr_chrg1;
	}
	public BigDecimal getR21_gpfsr_nom_amt2() {
		return r21_gpfsr_nom_amt2;
	}
	public void setR21_gpfsr_nom_amt2(BigDecimal r21_gpfsr_nom_amt2) {
		this.r21_gpfsr_nom_amt2 = r21_gpfsr_nom_amt2;
	}
	public BigDecimal getR21_gpfsr_pos_att2_per_spe_ris() {
		return r21_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att2_per_spe_ris(BigDecimal r21_gpfsr_pos_att2_per_spe_ris) {
		this.r21_gpfsr_pos_att2_per_spe_ris = r21_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg2() {
		return r21_gpfsr_chrg2;
	}
	public void setR21_gpfsr_chrg2(BigDecimal r21_gpfsr_chrg2) {
		this.r21_gpfsr_chrg2 = r21_gpfsr_chrg2;
	}
	public BigDecimal getR21_tot_spe_ris_chrg() {
		return r21_tot_spe_ris_chrg;
	}
	public void setR21_tot_spe_ris_chrg(BigDecimal r21_tot_spe_ris_chrg) {
		this.r21_tot_spe_ris_chrg = r21_tot_spe_ris_chrg;
	}
	public BigDecimal getR21_net_pos_gen_mar_ris() {
		return r21_net_pos_gen_mar_ris;
	}
	public void setR21_net_pos_gen_mar_ris(BigDecimal r21_net_pos_gen_mar_ris) {
		this.r21_net_pos_gen_mar_ris = r21_net_pos_gen_mar_ris;
	}
	public BigDecimal getR21_gen_mar_ris_chrg_8per() {
		return r21_gen_mar_ris_chrg_8per;
	}
	public void setR21_gen_mar_ris_chrg_8per(BigDecimal r21_gen_mar_ris_chrg_8per) {
		this.r21_gen_mar_ris_chrg_8per = r21_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR21_2per_gen_mar_ris_chrg_div_port() {
		return r21_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR21_2per_gen_mar_ris_chrg_div_port(BigDecimal r21_2per_gen_mar_ris_chrg_div_port) {
		this.r21_2per_gen_mar_ris_chrg_div_port = r21_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR21_tot_gen_mar_risk_chrg() {
		return r21_tot_gen_mar_risk_chrg;
	}
	public void setR21_tot_gen_mar_risk_chrg(BigDecimal r21_tot_gen_mar_risk_chrg) {
		this.r21_tot_gen_mar_risk_chrg = r21_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR21_tot_mar_ris_chrg() {
		return r21_tot_mar_ris_chrg;
	}
	public void setR21_tot_mar_ris_chrg(BigDecimal r21_tot_mar_ris_chrg) {
		this.r21_tot_mar_ris_chrg = r21_tot_mar_ris_chrg;
	}
	public BigDecimal getR22_market() {
		return r22_market;
	}
	public void setR22_market(BigDecimal r22_market) {
		this.r22_market = r22_market;
	}
	public BigDecimal getR22_gpfsr_nom_amt() {
		return r22_gpfsr_nom_amt;
	}
	public void setR22_gpfsr_nom_amt(BigDecimal r22_gpfsr_nom_amt) {
		this.r22_gpfsr_nom_amt = r22_gpfsr_nom_amt;
	}
	public BigDecimal getR22_gpfsr_pos_att8_per_spe_ris() {
		return r22_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att8_per_spe_ris(BigDecimal r22_gpfsr_pos_att8_per_spe_ris) {
		this.r22_gpfsr_pos_att8_per_spe_ris = r22_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg() {
		return r22_gpfsr_chrg;
	}
	public void setR22_gpfsr_chrg(BigDecimal r22_gpfsr_chrg) {
		this.r22_gpfsr_chrg = r22_gpfsr_chrg;
	}
	public BigDecimal getR22_gpfsr_nom_amt1() {
		return r22_gpfsr_nom_amt1;
	}
	public void setR22_gpfsr_nom_amt1(BigDecimal r22_gpfsr_nom_amt1) {
		this.r22_gpfsr_nom_amt1 = r22_gpfsr_nom_amt1;
	}
	public BigDecimal getR22_gpfsr_pos_att4_per_spe_ris() {
		return r22_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att4_per_spe_ris(BigDecimal r22_gpfsr_pos_att4_per_spe_ris) {
		this.r22_gpfsr_pos_att4_per_spe_ris = r22_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg1() {
		return r22_gpfsr_chrg1;
	}
	public void setR22_gpfsr_chrg1(BigDecimal r22_gpfsr_chrg1) {
		this.r22_gpfsr_chrg1 = r22_gpfsr_chrg1;
	}
	public BigDecimal getR22_gpfsr_nom_amt2() {
		return r22_gpfsr_nom_amt2;
	}
	public void setR22_gpfsr_nom_amt2(BigDecimal r22_gpfsr_nom_amt2) {
		this.r22_gpfsr_nom_amt2 = r22_gpfsr_nom_amt2;
	}
	public BigDecimal getR22_gpfsr_pos_att2_per_spe_ris() {
		return r22_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att2_per_spe_ris(BigDecimal r22_gpfsr_pos_att2_per_spe_ris) {
		this.r22_gpfsr_pos_att2_per_spe_ris = r22_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg2() {
		return r22_gpfsr_chrg2;
	}
	public void setR22_gpfsr_chrg2(BigDecimal r22_gpfsr_chrg2) {
		this.r22_gpfsr_chrg2 = r22_gpfsr_chrg2;
	}
	public BigDecimal getR22_tot_spe_ris_chrg() {
		return r22_tot_spe_ris_chrg;
	}
	public void setR22_tot_spe_ris_chrg(BigDecimal r22_tot_spe_ris_chrg) {
		this.r22_tot_spe_ris_chrg = r22_tot_spe_ris_chrg;
	}
	public BigDecimal getR22_net_pos_gen_mar_ris() {
		return r22_net_pos_gen_mar_ris;
	}
	public void setR22_net_pos_gen_mar_ris(BigDecimal r22_net_pos_gen_mar_ris) {
		this.r22_net_pos_gen_mar_ris = r22_net_pos_gen_mar_ris;
	}
	public BigDecimal getR22_gen_mar_ris_chrg_8per() {
		return r22_gen_mar_ris_chrg_8per;
	}
	public void setR22_gen_mar_ris_chrg_8per(BigDecimal r22_gen_mar_ris_chrg_8per) {
		this.r22_gen_mar_ris_chrg_8per = r22_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR22_2per_gen_mar_ris_chrg_div_port() {
		return r22_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR22_2per_gen_mar_ris_chrg_div_port(BigDecimal r22_2per_gen_mar_ris_chrg_div_port) {
		this.r22_2per_gen_mar_ris_chrg_div_port = r22_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR22_tot_gen_mar_risk_chrg() {
		return r22_tot_gen_mar_risk_chrg;
	}
	public void setR22_tot_gen_mar_risk_chrg(BigDecimal r22_tot_gen_mar_risk_chrg) {
		this.r22_tot_gen_mar_risk_chrg = r22_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR22_tot_mar_ris_chrg() {
		return r22_tot_mar_ris_chrg;
	}
	public void setR22_tot_mar_ris_chrg(BigDecimal r22_tot_mar_ris_chrg) {
		this.r22_tot_mar_ris_chrg = r22_tot_mar_ris_chrg;
	}
	public String getR23_market() {
		return r23_market;
	}
	public void setR23_market(String r23_market) {
		this.r23_market = r23_market;
	}
	public BigDecimal getR23_gpfsr_nom_amt() {
		return r23_gpfsr_nom_amt;
	}
	public void setR23_gpfsr_nom_amt(BigDecimal r23_gpfsr_nom_amt) {
		this.r23_gpfsr_nom_amt = r23_gpfsr_nom_amt;
	}
	public BigDecimal getR23_gpfsr_pos_att8_per_spe_ris() {
		return r23_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att8_per_spe_ris(BigDecimal r23_gpfsr_pos_att8_per_spe_ris) {
		this.r23_gpfsr_pos_att8_per_spe_ris = r23_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg() {
		return r23_gpfsr_chrg;
	}
	public void setR23_gpfsr_chrg(BigDecimal r23_gpfsr_chrg) {
		this.r23_gpfsr_chrg = r23_gpfsr_chrg;
	}
	public BigDecimal getR23_gpfsr_nom_amt1() {
		return r23_gpfsr_nom_amt1;
	}
	public void setR23_gpfsr_nom_amt1(BigDecimal r23_gpfsr_nom_amt1) {
		this.r23_gpfsr_nom_amt1 = r23_gpfsr_nom_amt1;
	}
	public BigDecimal getR23_gpfsr_pos_att4_per_spe_ris() {
		return r23_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att4_per_spe_ris(BigDecimal r23_gpfsr_pos_att4_per_spe_ris) {
		this.r23_gpfsr_pos_att4_per_spe_ris = r23_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg1() {
		return r23_gpfsr_chrg1;
	}
	public void setR23_gpfsr_chrg1(BigDecimal r23_gpfsr_chrg1) {
		this.r23_gpfsr_chrg1 = r23_gpfsr_chrg1;
	}
	public BigDecimal getR23_gpfsr_nom_amt2() {
		return r23_gpfsr_nom_amt2;
	}
	public void setR23_gpfsr_nom_amt2(BigDecimal r23_gpfsr_nom_amt2) {
		this.r23_gpfsr_nom_amt2 = r23_gpfsr_nom_amt2;
	}
	public BigDecimal getR23_gpfsr_pos_att2_per_spe_ris() {
		return r23_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att2_per_spe_ris(BigDecimal r23_gpfsr_pos_att2_per_spe_ris) {
		this.r23_gpfsr_pos_att2_per_spe_ris = r23_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg2() {
		return r23_gpfsr_chrg2;
	}
	public void setR23_gpfsr_chrg2(BigDecimal r23_gpfsr_chrg2) {
		this.r23_gpfsr_chrg2 = r23_gpfsr_chrg2;
	}
	public BigDecimal getR23_tot_spe_ris_chrg() {
		return r23_tot_spe_ris_chrg;
	}
	public void setR23_tot_spe_ris_chrg(BigDecimal r23_tot_spe_ris_chrg) {
		this.r23_tot_spe_ris_chrg = r23_tot_spe_ris_chrg;
	}
	public BigDecimal getR23_net_pos_gen_mar_ris() {
		return r23_net_pos_gen_mar_ris;
	}
	public void setR23_net_pos_gen_mar_ris(BigDecimal r23_net_pos_gen_mar_ris) {
		this.r23_net_pos_gen_mar_ris = r23_net_pos_gen_mar_ris;
	}
	public BigDecimal getR23_gen_mar_ris_chrg_8per() {
		return r23_gen_mar_ris_chrg_8per;
	}
	public void setR23_gen_mar_ris_chrg_8per(BigDecimal r23_gen_mar_ris_chrg_8per) {
		this.r23_gen_mar_ris_chrg_8per = r23_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR23_2per_gen_mar_ris_chrg_div_port() {
		return r23_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR23_2per_gen_mar_ris_chrg_div_port(BigDecimal r23_2per_gen_mar_ris_chrg_div_port) {
		this.r23_2per_gen_mar_ris_chrg_div_port = r23_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR23_tot_gen_mar_risk_chrg() {
		return r23_tot_gen_mar_risk_chrg;
	}
	public void setR23_tot_gen_mar_risk_chrg(BigDecimal r23_tot_gen_mar_risk_chrg) {
		this.r23_tot_gen_mar_risk_chrg = r23_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR23_tot_mar_ris_chrg() {
		return r23_tot_mar_ris_chrg;
	}
	public void setR23_tot_mar_ris_chrg(BigDecimal r23_tot_mar_ris_chrg) {
		this.r23_tot_mar_ris_chrg = r23_tot_mar_ris_chrg;
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
// RESUB DETAIL M_EPR
//=====================================================

public class M_EPR_RESUB_Detail_RowMapper 
        implements RowMapper<M_EPR_RESUB_Detail_Entity> {

    @Override
    public M_EPR_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_EPR_RESUB_Detail_Entity obj = new M_EPR_RESUB_Detail_Entity();

// =========================
// R11
// =========================
obj.setR11_market(rs.getBigDecimal("r11_market"));
obj.setR11_gpfsr_nom_amt(rs.getBigDecimal("r11_gpfsr_nom_amt"));
obj.setR11_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att8_per_spe_ris"));
obj.setR11_gpfsr_chrg(rs.getBigDecimal("r11_gpfsr_chrg"));
obj.setR11_gpfsr_nom_amt1(rs.getBigDecimal("r11_gpfsr_nom_amt1"));
obj.setR11_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att4_per_spe_ris"));
obj.setR11_gpfsr_chrg1(rs.getBigDecimal("r11_gpfsr_chrg1"));
obj.setR11_gpfsr_nom_amt2(rs.getBigDecimal("r11_gpfsr_nom_amt2"));
obj.setR11_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r11_gpfsr_pos_att2_per_spe_ris"));
obj.setR11_gpfsr_chrg2(rs.getBigDecimal("r11_gpfsr_chrg2"));
obj.setR11_tot_spe_ris_chrg(rs.getBigDecimal("r11_tot_spe_ris_chrg"));
obj.setR11_net_pos_gen_mar_ris(rs.getBigDecimal("r11_net_pos_gen_mar_ris"));
obj.setR11_gen_mar_ris_chrg_8per(rs.getBigDecimal("r11_gen_mar_ris_chrg_8per"));
obj.setR11_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r11_2per_gen_mar_ris_chrg_div_port"));
obj.setR11_tot_gen_mar_risk_chrg(rs.getBigDecimal("r11_tot_gen_mar_risk_chrg"));
obj.setR11_tot_mar_ris_chrg(rs.getBigDecimal("r11_tot_mar_ris_chrg"));

// =========================
// R12
// =========================
obj.setR12_market(rs.getBigDecimal("r12_market"));
obj.setR12_gpfsr_nom_amt(rs.getBigDecimal("r12_gpfsr_nom_amt"));
obj.setR12_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att8_per_spe_ris"));
obj.setR12_gpfsr_chrg(rs.getBigDecimal("r12_gpfsr_chrg"));
obj.setR12_gpfsr_nom_amt1(rs.getBigDecimal("r12_gpfsr_nom_amt1"));
obj.setR12_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att4_per_spe_ris"));
obj.setR12_gpfsr_chrg1(rs.getBigDecimal("r12_gpfsr_chrg1"));
obj.setR12_gpfsr_nom_amt2(rs.getBigDecimal("r12_gpfsr_nom_amt2"));
obj.setR12_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r12_gpfsr_pos_att2_per_spe_ris"));
obj.setR12_gpfsr_chrg2(rs.getBigDecimal("r12_gpfsr_chrg2"));
obj.setR12_tot_spe_ris_chrg(rs.getBigDecimal("r12_tot_spe_ris_chrg"));
obj.setR12_net_pos_gen_mar_ris(rs.getBigDecimal("r12_net_pos_gen_mar_ris"));
obj.setR12_gen_mar_ris_chrg_8per(rs.getBigDecimal("r12_gen_mar_ris_chrg_8per"));
obj.setR12_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r12_2per_gen_mar_ris_chrg_div_port"));
obj.setR12_tot_gen_mar_risk_chrg(rs.getBigDecimal("r12_tot_gen_mar_risk_chrg"));
obj.setR12_tot_mar_ris_chrg(rs.getBigDecimal("r12_tot_mar_ris_chrg"));

// =========================
// R13
// =========================
obj.setR13_market(rs.getBigDecimal("r13_market"));
obj.setR13_gpfsr_nom_amt(rs.getBigDecimal("r13_gpfsr_nom_amt"));
obj.setR13_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att8_per_spe_ris"));
obj.setR13_gpfsr_chrg(rs.getBigDecimal("r13_gpfsr_chrg"));
obj.setR13_gpfsr_nom_amt1(rs.getBigDecimal("r13_gpfsr_nom_amt1"));
obj.setR13_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att4_per_spe_ris"));
obj.setR13_gpfsr_chrg1(rs.getBigDecimal("r13_gpfsr_chrg1"));
obj.setR13_gpfsr_nom_amt2(rs.getBigDecimal("r13_gpfsr_nom_amt2"));
obj.setR13_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r13_gpfsr_pos_att2_per_spe_ris"));
obj.setR13_gpfsr_chrg2(rs.getBigDecimal("r13_gpfsr_chrg2"));
obj.setR13_tot_spe_ris_chrg(rs.getBigDecimal("r13_tot_spe_ris_chrg"));
obj.setR13_net_pos_gen_mar_ris(rs.getBigDecimal("r13_net_pos_gen_mar_ris"));
obj.setR13_gen_mar_ris_chrg_8per(rs.getBigDecimal("r13_gen_mar_ris_chrg_8per"));
obj.setR13_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r13_2per_gen_mar_ris_chrg_div_port"));
obj.setR13_tot_gen_mar_risk_chrg(rs.getBigDecimal("r13_tot_gen_mar_risk_chrg"));
obj.setR13_tot_mar_ris_chrg(rs.getBigDecimal("r13_tot_mar_ris_chrg"));

// =========================
// R14
// =========================
obj.setR14_market(rs.getBigDecimal("r14_market"));
obj.setR14_gpfsr_nom_amt(rs.getBigDecimal("r14_gpfsr_nom_amt"));
obj.setR14_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att8_per_spe_ris"));
obj.setR14_gpfsr_chrg(rs.getBigDecimal("r14_gpfsr_chrg"));
obj.setR14_gpfsr_nom_amt1(rs.getBigDecimal("r14_gpfsr_nom_amt1"));
obj.setR14_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att4_per_spe_ris"));
obj.setR14_gpfsr_chrg1(rs.getBigDecimal("r14_gpfsr_chrg1"));
obj.setR14_gpfsr_nom_amt2(rs.getBigDecimal("r14_gpfsr_nom_amt2"));
obj.setR14_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r14_gpfsr_pos_att2_per_spe_ris"));
obj.setR14_gpfsr_chrg2(rs.getBigDecimal("r14_gpfsr_chrg2"));
obj.setR14_tot_spe_ris_chrg(rs.getBigDecimal("r14_tot_spe_ris_chrg"));
obj.setR14_net_pos_gen_mar_ris(rs.getBigDecimal("r14_net_pos_gen_mar_ris"));
obj.setR14_gen_mar_ris_chrg_8per(rs.getBigDecimal("r14_gen_mar_ris_chrg_8per"));
obj.setR14_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r14_2per_gen_mar_ris_chrg_div_port"));
obj.setR14_tot_gen_mar_risk_chrg(rs.getBigDecimal("r14_tot_gen_mar_risk_chrg"));
obj.setR14_tot_mar_ris_chrg(rs.getBigDecimal("r14_tot_mar_ris_chrg"));

// =========================
// R15
// =========================
obj.setR15_market(rs.getBigDecimal("r15_market"));
obj.setR15_gpfsr_nom_amt(rs.getBigDecimal("r15_gpfsr_nom_amt"));
obj.setR15_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att8_per_spe_ris"));
obj.setR15_gpfsr_chrg(rs.getBigDecimal("r15_gpfsr_chrg"));
obj.setR15_gpfsr_nom_amt1(rs.getBigDecimal("r15_gpfsr_nom_amt1"));
obj.setR15_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att4_per_spe_ris"));
obj.setR15_gpfsr_chrg1(rs.getBigDecimal("r15_gpfsr_chrg1"));
obj.setR15_gpfsr_nom_amt2(rs.getBigDecimal("r15_gpfsr_nom_amt2"));
obj.setR15_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r15_gpfsr_pos_att2_per_spe_ris"));
obj.setR15_gpfsr_chrg2(rs.getBigDecimal("r15_gpfsr_chrg2"));
obj.setR15_tot_spe_ris_chrg(rs.getBigDecimal("r15_tot_spe_ris_chrg"));
obj.setR15_net_pos_gen_mar_ris(rs.getBigDecimal("r15_net_pos_gen_mar_ris"));
obj.setR15_gen_mar_ris_chrg_8per(rs.getBigDecimal("r15_gen_mar_ris_chrg_8per"));
obj.setR15_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r15_2per_gen_mar_ris_chrg_div_port"));
obj.setR15_tot_gen_mar_risk_chrg(rs.getBigDecimal("r15_tot_gen_mar_risk_chrg"));
obj.setR15_tot_mar_ris_chrg(rs.getBigDecimal("r15_tot_mar_ris_chrg"));

// =========================
// R16
// =========================
obj.setR16_market(rs.getBigDecimal("r16_market"));
obj.setR16_gpfsr_nom_amt(rs.getBigDecimal("r16_gpfsr_nom_amt"));
obj.setR16_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att8_per_spe_ris"));
obj.setR16_gpfsr_chrg(rs.getBigDecimal("r16_gpfsr_chrg"));
obj.setR16_gpfsr_nom_amt1(rs.getBigDecimal("r16_gpfsr_nom_amt1"));
obj.setR16_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att4_per_spe_ris"));
obj.setR16_gpfsr_chrg1(rs.getBigDecimal("r16_gpfsr_chrg1"));
obj.setR16_gpfsr_nom_amt2(rs.getBigDecimal("r16_gpfsr_nom_amt2"));
obj.setR16_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r16_gpfsr_pos_att2_per_spe_ris"));
obj.setR16_gpfsr_chrg2(rs.getBigDecimal("r16_gpfsr_chrg2"));
obj.setR16_tot_spe_ris_chrg(rs.getBigDecimal("r16_tot_spe_ris_chrg"));
obj.setR16_net_pos_gen_mar_ris(rs.getBigDecimal("r16_net_pos_gen_mar_ris"));
obj.setR16_gen_mar_ris_chrg_8per(rs.getBigDecimal("r16_gen_mar_ris_chrg_8per"));
obj.setR16_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r16_2per_gen_mar_ris_chrg_div_port"));
obj.setR16_tot_gen_mar_risk_chrg(rs.getBigDecimal("r16_tot_gen_mar_risk_chrg"));
obj.setR16_tot_mar_ris_chrg(rs.getBigDecimal("r16_tot_mar_ris_chrg"));

// =========================
// R17
// =========================
obj.setR17_market(rs.getBigDecimal("r17_market"));
obj.setR17_gpfsr_nom_amt(rs.getBigDecimal("r17_gpfsr_nom_amt"));
obj.setR17_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att8_per_spe_ris"));
obj.setR17_gpfsr_chrg(rs.getBigDecimal("r17_gpfsr_chrg"));
obj.setR17_gpfsr_nom_amt1(rs.getBigDecimal("r17_gpfsr_nom_amt1"));
obj.setR17_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att4_per_spe_ris"));
obj.setR17_gpfsr_chrg1(rs.getBigDecimal("r17_gpfsr_chrg1"));
obj.setR17_gpfsr_nom_amt2(rs.getBigDecimal("r17_gpfsr_nom_amt2"));
obj.setR17_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r17_gpfsr_pos_att2_per_spe_ris"));
obj.setR17_gpfsr_chrg2(rs.getBigDecimal("r17_gpfsr_chrg2"));
obj.setR17_tot_spe_ris_chrg(rs.getBigDecimal("r17_tot_spe_ris_chrg"));
obj.setR17_net_pos_gen_mar_ris(rs.getBigDecimal("r17_net_pos_gen_mar_ris"));
obj.setR17_gen_mar_ris_chrg_8per(rs.getBigDecimal("r17_gen_mar_ris_chrg_8per"));
obj.setR17_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r17_2per_gen_mar_ris_chrg_div_port"));
obj.setR17_tot_gen_mar_risk_chrg(rs.getBigDecimal("r17_tot_gen_mar_risk_chrg"));
obj.setR17_tot_mar_ris_chrg(rs.getBigDecimal("r17_tot_mar_ris_chrg"));

// =========================
// R18
// =========================
obj.setR18_market(rs.getBigDecimal("r18_market"));
obj.setR18_gpfsr_nom_amt(rs.getBigDecimal("r18_gpfsr_nom_amt"));
obj.setR18_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att8_per_spe_ris"));
obj.setR18_gpfsr_chrg(rs.getBigDecimal("r18_gpfsr_chrg"));
obj.setR18_gpfsr_nom_amt1(rs.getBigDecimal("r18_gpfsr_nom_amt1"));
obj.setR18_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att4_per_spe_ris"));
obj.setR18_gpfsr_chrg1(rs.getBigDecimal("r18_gpfsr_chrg1"));
obj.setR18_gpfsr_nom_amt2(rs.getBigDecimal("r18_gpfsr_nom_amt2"));
obj.setR18_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r18_gpfsr_pos_att2_per_spe_ris"));
obj.setR18_gpfsr_chrg2(rs.getBigDecimal("r18_gpfsr_chrg2"));
obj.setR18_tot_spe_ris_chrg(rs.getBigDecimal("r18_tot_spe_ris_chrg"));
obj.setR18_net_pos_gen_mar_ris(rs.getBigDecimal("r18_net_pos_gen_mar_ris"));
obj.setR18_gen_mar_ris_chrg_8per(rs.getBigDecimal("r18_gen_mar_ris_chrg_8per"));
obj.setR18_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r18_2per_gen_mar_ris_chrg_div_port"));
obj.setR18_tot_gen_mar_risk_chrg(rs.getBigDecimal("r18_tot_gen_mar_risk_chrg"));
obj.setR18_tot_mar_ris_chrg(rs.getBigDecimal("r18_tot_mar_ris_chrg"));

// =========================
// R19
// =========================
obj.setR19_market(rs.getBigDecimal("r19_market"));
obj.setR19_gpfsr_nom_amt(rs.getBigDecimal("r19_gpfsr_nom_amt"));
obj.setR19_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att8_per_spe_ris"));
obj.setR19_gpfsr_chrg(rs.getBigDecimal("r19_gpfsr_chrg"));
obj.setR19_gpfsr_nom_amt1(rs.getBigDecimal("r19_gpfsr_nom_amt1"));
obj.setR19_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att4_per_spe_ris"));
obj.setR19_gpfsr_chrg1(rs.getBigDecimal("r19_gpfsr_chrg1"));
obj.setR19_gpfsr_nom_amt2(rs.getBigDecimal("r19_gpfsr_nom_amt2"));
obj.setR19_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r19_gpfsr_pos_att2_per_spe_ris"));
obj.setR19_gpfsr_chrg2(rs.getBigDecimal("r19_gpfsr_chrg2"));
obj.setR19_tot_spe_ris_chrg(rs.getBigDecimal("r19_tot_spe_ris_chrg"));
obj.setR19_net_pos_gen_mar_ris(rs.getBigDecimal("r19_net_pos_gen_mar_ris"));
obj.setR19_gen_mar_ris_chrg_8per(rs.getBigDecimal("r19_gen_mar_ris_chrg_8per"));
obj.setR19_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r19_2per_gen_mar_ris_chrg_div_port"));
obj.setR19_tot_gen_mar_risk_chrg(rs.getBigDecimal("r19_tot_gen_mar_risk_chrg"));
obj.setR19_tot_mar_ris_chrg(rs.getBigDecimal("r19_tot_mar_ris_chrg"));

// =========================
// R20
// =========================
obj.setR20_market(rs.getBigDecimal("r20_market"));
obj.setR20_gpfsr_nom_amt(rs.getBigDecimal("r20_gpfsr_nom_amt"));
obj.setR20_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att8_per_spe_ris"));
obj.setR20_gpfsr_chrg(rs.getBigDecimal("r20_gpfsr_chrg"));
obj.setR20_gpfsr_nom_amt1(rs.getBigDecimal("r20_gpfsr_nom_amt1"));
obj.setR20_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att4_per_spe_ris"));
obj.setR20_gpfsr_chrg1(rs.getBigDecimal("r20_gpfsr_chrg1"));
obj.setR20_gpfsr_nom_amt2(rs.getBigDecimal("r20_gpfsr_nom_amt2"));
obj.setR20_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r20_gpfsr_pos_att2_per_spe_ris"));
obj.setR20_gpfsr_chrg2(rs.getBigDecimal("r20_gpfsr_chrg2"));
obj.setR20_tot_spe_ris_chrg(rs.getBigDecimal("r20_tot_spe_ris_chrg"));
obj.setR20_net_pos_gen_mar_ris(rs.getBigDecimal("r20_net_pos_gen_mar_ris"));
obj.setR20_gen_mar_ris_chrg_8per(rs.getBigDecimal("r20_gen_mar_ris_chrg_8per"));
obj.setR20_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r20_2per_gen_mar_ris_chrg_div_port"));
obj.setR20_tot_gen_mar_risk_chrg(rs.getBigDecimal("r20_tot_gen_mar_risk_chrg"));
obj.setR20_tot_mar_ris_chrg(rs.getBigDecimal("r20_tot_mar_ris_chrg"));

// =========================
// R21
// =========================
obj.setR21_market(rs.getBigDecimal("r21_market"));
obj.setR21_gpfsr_nom_amt(rs.getBigDecimal("r21_gpfsr_nom_amt"));
obj.setR21_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att8_per_spe_ris"));
obj.setR21_gpfsr_chrg(rs.getBigDecimal("r21_gpfsr_chrg"));
obj.setR21_gpfsr_nom_amt1(rs.getBigDecimal("r21_gpfsr_nom_amt1"));
obj.setR21_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att4_per_spe_ris"));
obj.setR21_gpfsr_chrg1(rs.getBigDecimal("r21_gpfsr_chrg1"));
obj.setR21_gpfsr_nom_amt2(rs.getBigDecimal("r21_gpfsr_nom_amt2"));
obj.setR21_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r21_gpfsr_pos_att2_per_spe_ris"));
obj.setR21_gpfsr_chrg2(rs.getBigDecimal("r21_gpfsr_chrg2"));
obj.setR21_tot_spe_ris_chrg(rs.getBigDecimal("r21_tot_spe_ris_chrg"));
obj.setR21_net_pos_gen_mar_ris(rs.getBigDecimal("r21_net_pos_gen_mar_ris"));
obj.setR21_gen_mar_ris_chrg_8per(rs.getBigDecimal("r21_gen_mar_ris_chrg_8per"));
obj.setR21_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r21_2per_gen_mar_ris_chrg_div_port"));
obj.setR21_tot_gen_mar_risk_chrg(rs.getBigDecimal("r21_tot_gen_mar_risk_chrg"));
obj.setR21_tot_mar_ris_chrg(rs.getBigDecimal("r21_tot_mar_ris_chrg"));


// =========================
// R22
// =========================
obj.setR22_market(rs.getBigDecimal("r22_market"));
obj.setR22_gpfsr_nom_amt(rs.getBigDecimal("r22_gpfsr_nom_amt"));
obj.setR22_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att8_per_spe_ris"));
obj.setR22_gpfsr_chrg(rs.getBigDecimal("r22_gpfsr_chrg"));
obj.setR22_gpfsr_nom_amt1(rs.getBigDecimal("r22_gpfsr_nom_amt1"));
obj.setR22_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att4_per_spe_ris"));
obj.setR22_gpfsr_chrg1(rs.getBigDecimal("r22_gpfsr_chrg1"));
obj.setR22_gpfsr_nom_amt2(rs.getBigDecimal("r22_gpfsr_nom_amt2"));
obj.setR22_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r22_gpfsr_pos_att2_per_spe_ris"));
obj.setR22_gpfsr_chrg2(rs.getBigDecimal("r22_gpfsr_chrg2"));
obj.setR22_tot_spe_ris_chrg(rs.getBigDecimal("r22_tot_spe_ris_chrg"));
obj.setR22_net_pos_gen_mar_ris(rs.getBigDecimal("r22_net_pos_gen_mar_ris"));
obj.setR22_gen_mar_ris_chrg_8per(rs.getBigDecimal("r22_gen_mar_ris_chrg_8per"));
obj.setR22_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r22_2per_gen_mar_ris_chrg_div_port"));
obj.setR22_tot_gen_mar_risk_chrg(rs.getBigDecimal("r22_tot_gen_mar_risk_chrg"));
obj.setR22_tot_mar_ris_chrg(rs.getBigDecimal("r22_tot_mar_ris_chrg"));

// =========================
// R23
// =========================
obj.setR23_market(rs.getString("r23_market"));
obj.setR23_gpfsr_nom_amt(rs.getBigDecimal("r23_gpfsr_nom_amt"));
obj.setR23_gpfsr_pos_att8_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att8_per_spe_ris"));
obj.setR23_gpfsr_chrg(rs.getBigDecimal("r23_gpfsr_chrg"));
obj.setR23_gpfsr_nom_amt1(rs.getBigDecimal("r23_gpfsr_nom_amt1"));
obj.setR23_gpfsr_pos_att4_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att4_per_spe_ris"));
obj.setR23_gpfsr_chrg1(rs.getBigDecimal("r23_gpfsr_chrg1"));
obj.setR23_gpfsr_nom_amt2(rs.getBigDecimal("r23_gpfsr_nom_amt2"));
obj.setR23_gpfsr_pos_att2_per_spe_ris(rs.getBigDecimal("r23_gpfsr_pos_att2_per_spe_ris"));
obj.setR23_gpfsr_chrg2(rs.getBigDecimal("r23_gpfsr_chrg2"));
obj.setR23_tot_spe_ris_chrg(rs.getBigDecimal("r23_tot_spe_ris_chrg"));
obj.setR23_net_pos_gen_mar_ris(rs.getBigDecimal("r23_net_pos_gen_mar_ris"));
obj.setR23_gen_mar_ris_chrg_8per(rs.getBigDecimal("r23_gen_mar_ris_chrg_8per"));
obj.setR23_2per_gen_mar_ris_chrg_div_port(rs.getBigDecimal("r23_2per_gen_mar_ris_chrg_div_port"));
obj.setR23_tot_gen_mar_risk_chrg(rs.getBigDecimal("r23_tot_gen_mar_risk_chrg"));
obj.setR23_tot_mar_ris_chrg(rs.getBigDecimal("r23_tot_mar_ris_chrg"));


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

public class M_EPR_RESUB_Detail_Entity {

   
private BigDecimal	r11_market;
	private BigDecimal	r11_gpfsr_nom_amt;
	private BigDecimal	r11_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg;
	private BigDecimal	r11_gpfsr_nom_amt1;
	private BigDecimal	r11_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg1;
	private BigDecimal	r11_gpfsr_nom_amt2;
	private BigDecimal	r11_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg2;
	private BigDecimal	r11_tot_spe_ris_chrg;
	private BigDecimal	r11_net_pos_gen_mar_ris;
	private BigDecimal	r11_gen_mar_ris_chrg_8per;
	private BigDecimal	r11_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r11_tot_gen_mar_risk_chrg;
	private BigDecimal	r11_tot_mar_ris_chrg;
	
	private BigDecimal	r12_market;
	private BigDecimal	r12_gpfsr_nom_amt;
	private BigDecimal	r12_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg;
	private BigDecimal	r12_gpfsr_nom_amt1;
	private BigDecimal	r12_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg1;
	private BigDecimal	r12_gpfsr_nom_amt2;
	private BigDecimal	r12_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg2;
	private BigDecimal	r12_tot_spe_ris_chrg;
	private BigDecimal	r12_net_pos_gen_mar_ris;
	private BigDecimal	r12_gen_mar_ris_chrg_8per;
	private BigDecimal	r12_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r12_tot_gen_mar_risk_chrg;
	private BigDecimal	r12_tot_mar_ris_chrg;
	
	private BigDecimal	r13_market;
	private BigDecimal	r13_gpfsr_nom_amt;
	private BigDecimal	r13_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg;
	private BigDecimal	r13_gpfsr_nom_amt1;
	private BigDecimal	r13_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg1;
	private BigDecimal	r13_gpfsr_nom_amt2;
	private BigDecimal	r13_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg2;
	private BigDecimal	r13_tot_spe_ris_chrg;
	private BigDecimal	r13_net_pos_gen_mar_ris;
	private BigDecimal	r13_gen_mar_ris_chrg_8per;
	private BigDecimal	r13_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r13_tot_gen_mar_risk_chrg;
	private BigDecimal	r13_tot_mar_ris_chrg;
	
	private BigDecimal	r14_market;
	private BigDecimal	r14_gpfsr_nom_amt;
	private BigDecimal	r14_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg;
	private BigDecimal	r14_gpfsr_nom_amt1;
	private BigDecimal	r14_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg1;
	private BigDecimal	r14_gpfsr_nom_amt2;
	private BigDecimal	r14_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg2;
	private BigDecimal	r14_tot_spe_ris_chrg;
	private BigDecimal	r14_net_pos_gen_mar_ris;
	private BigDecimal	r14_gen_mar_ris_chrg_8per;
	private BigDecimal	r14_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r14_tot_gen_mar_risk_chrg;
	private BigDecimal	r14_tot_mar_ris_chrg;
	
	private BigDecimal	r15_market;
	private BigDecimal	r15_gpfsr_nom_amt;
	private BigDecimal	r15_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg;
	private BigDecimal	r15_gpfsr_nom_amt1;
	private BigDecimal	r15_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg1;
	private BigDecimal	r15_gpfsr_nom_amt2;
	private BigDecimal	r15_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg2;
	private BigDecimal	r15_tot_spe_ris_chrg;
	private BigDecimal	r15_net_pos_gen_mar_ris;
	private BigDecimal	r15_gen_mar_ris_chrg_8per;
	private BigDecimal	r15_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r15_tot_gen_mar_risk_chrg;
	private BigDecimal	r15_tot_mar_ris_chrg;
	
	private BigDecimal	r16_market;
	private BigDecimal	r16_gpfsr_nom_amt;
	private BigDecimal	r16_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg;
	private BigDecimal	r16_gpfsr_nom_amt1;
	private BigDecimal	r16_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg1;
	private BigDecimal	r16_gpfsr_nom_amt2;
	private BigDecimal	r16_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg2;
	private BigDecimal	r16_tot_spe_ris_chrg;
	private BigDecimal	r16_net_pos_gen_mar_ris;
	private BigDecimal	r16_gen_mar_ris_chrg_8per;
	private BigDecimal	r16_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r16_tot_gen_mar_risk_chrg;
	private BigDecimal	r16_tot_mar_ris_chrg;
	
	private BigDecimal	r17_market;
	private BigDecimal	r17_gpfsr_nom_amt;
	private BigDecimal	r17_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg;
	private BigDecimal	r17_gpfsr_nom_amt1;
	private BigDecimal	r17_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg1;
	private BigDecimal	r17_gpfsr_nom_amt2;
	private BigDecimal	r17_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg2;
	private BigDecimal	r17_tot_spe_ris_chrg;
	private BigDecimal	r17_net_pos_gen_mar_ris;
	private BigDecimal	r17_gen_mar_ris_chrg_8per;
	private BigDecimal	r17_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r17_tot_gen_mar_risk_chrg;
	private BigDecimal	r17_tot_mar_ris_chrg;
	
	private BigDecimal	r18_market;
	private BigDecimal	r18_gpfsr_nom_amt;
	private BigDecimal	r18_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg;
	private BigDecimal	r18_gpfsr_nom_amt1;
	private BigDecimal	r18_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg1;
	private BigDecimal	r18_gpfsr_nom_amt2;
	private BigDecimal	r18_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg2;
	private BigDecimal	r18_tot_spe_ris_chrg;
	private BigDecimal	r18_net_pos_gen_mar_ris;
	private BigDecimal	r18_gen_mar_ris_chrg_8per;
	private BigDecimal	r18_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r18_tot_gen_mar_risk_chrg;
	private BigDecimal	r18_tot_mar_ris_chrg;
	
	private BigDecimal	r19_market;
	private BigDecimal	r19_gpfsr_nom_amt;
	private BigDecimal	r19_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg;
	private BigDecimal	r19_gpfsr_nom_amt1;
	private BigDecimal	r19_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg1;
	private BigDecimal	r19_gpfsr_nom_amt2;
	private BigDecimal	r19_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg2;
	private BigDecimal	r19_tot_spe_ris_chrg;
	private BigDecimal	r19_net_pos_gen_mar_ris;
	private BigDecimal	r19_gen_mar_ris_chrg_8per;
	private BigDecimal	r19_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r19_tot_gen_mar_risk_chrg;
	private BigDecimal	r19_tot_mar_ris_chrg;
	
	private BigDecimal	r20_market;
	private BigDecimal	r20_gpfsr_nom_amt;
	private BigDecimal	r20_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg;
	private BigDecimal	r20_gpfsr_nom_amt1;
	private BigDecimal	r20_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg1;
	private BigDecimal	r20_gpfsr_nom_amt2;
	private BigDecimal	r20_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg2;
	private BigDecimal	r20_tot_spe_ris_chrg;
	private BigDecimal	r20_net_pos_gen_mar_ris;
	private BigDecimal	r20_gen_mar_ris_chrg_8per;
	private BigDecimal	r20_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r20_tot_gen_mar_risk_chrg;
	private BigDecimal	r20_tot_mar_ris_chrg;
	
	private BigDecimal	r21_market;
	private BigDecimal	r21_gpfsr_nom_amt;
	private BigDecimal	r21_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg;
	private BigDecimal	r21_gpfsr_nom_amt1;
	private BigDecimal	r21_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg1;
	private BigDecimal	r21_gpfsr_nom_amt2;
	private BigDecimal	r21_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg2;
	private BigDecimal	r21_tot_spe_ris_chrg;
	private BigDecimal	r21_net_pos_gen_mar_ris;
	private BigDecimal	r21_gen_mar_ris_chrg_8per;
	private BigDecimal	r21_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r21_tot_gen_mar_risk_chrg;
	private BigDecimal	r21_tot_mar_ris_chrg;
	
	private BigDecimal	r22_market;
	private BigDecimal	r22_gpfsr_nom_amt;
	private BigDecimal	r22_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg;
	private BigDecimal	r22_gpfsr_nom_amt1;
	private BigDecimal	r22_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg1;
	private BigDecimal	r22_gpfsr_nom_amt2;
	private BigDecimal	r22_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg2;
	private BigDecimal	r22_tot_spe_ris_chrg;
	private BigDecimal	r22_net_pos_gen_mar_ris;
	private BigDecimal	r22_gen_mar_ris_chrg_8per;
	private BigDecimal	r22_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r22_tot_gen_mar_risk_chrg;
	private BigDecimal	r22_tot_mar_ris_chrg;
	
	private String	r23_market;
	private BigDecimal	r23_gpfsr_nom_amt;
	private BigDecimal	r23_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg;
	private BigDecimal	r23_gpfsr_nom_amt1;
	private BigDecimal	r23_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg1;
	private BigDecimal	r23_gpfsr_nom_amt2;
	private BigDecimal	r23_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg2;
	private BigDecimal	r23_tot_spe_ris_chrg;
	private BigDecimal	r23_net_pos_gen_mar_ris;
	private BigDecimal	r23_gen_mar_ris_chrg_8per;
	private BigDecimal	r23_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r23_tot_gen_mar_risk_chrg;
	private BigDecimal	r23_tot_mar_ris_chrg;
	
	
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
	
	
	public BigDecimal getR11_market() {
		return r11_market;
	}
	public void setR11_market(BigDecimal r11_market) {
		this.r11_market = r11_market;
	}
	public BigDecimal getR11_gpfsr_nom_amt() {
		return r11_gpfsr_nom_amt;
	}
	public void setR11_gpfsr_nom_amt(BigDecimal r11_gpfsr_nom_amt) {
		this.r11_gpfsr_nom_amt = r11_gpfsr_nom_amt;
	}
	public BigDecimal getR11_gpfsr_pos_att8_per_spe_ris() {
		return r11_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att8_per_spe_ris(BigDecimal r11_gpfsr_pos_att8_per_spe_ris) {
		this.r11_gpfsr_pos_att8_per_spe_ris = r11_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg() {
		return r11_gpfsr_chrg;
	}
	public void setR11_gpfsr_chrg(BigDecimal r11_gpfsr_chrg) {
		this.r11_gpfsr_chrg = r11_gpfsr_chrg;
	}
	public BigDecimal getR11_gpfsr_nom_amt1() {
		return r11_gpfsr_nom_amt1;
	}
	public void setR11_gpfsr_nom_amt1(BigDecimal r11_gpfsr_nom_amt1) {
		this.r11_gpfsr_nom_amt1 = r11_gpfsr_nom_amt1;
	}
	public BigDecimal getR11_gpfsr_pos_att4_per_spe_ris() {
		return r11_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att4_per_spe_ris(BigDecimal r11_gpfsr_pos_att4_per_spe_ris) {
		this.r11_gpfsr_pos_att4_per_spe_ris = r11_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg1() {
		return r11_gpfsr_chrg1;
	}
	public void setR11_gpfsr_chrg1(BigDecimal r11_gpfsr_chrg1) {
		this.r11_gpfsr_chrg1 = r11_gpfsr_chrg1;
	}
	public BigDecimal getR11_gpfsr_nom_amt2() {
		return r11_gpfsr_nom_amt2;
	}
	public void setR11_gpfsr_nom_amt2(BigDecimal r11_gpfsr_nom_amt2) {
		this.r11_gpfsr_nom_amt2 = r11_gpfsr_nom_amt2;
	}
	public BigDecimal getR11_gpfsr_pos_att2_per_spe_ris() {
		return r11_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att2_per_spe_ris(BigDecimal r11_gpfsr_pos_att2_per_spe_ris) {
		this.r11_gpfsr_pos_att2_per_spe_ris = r11_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg2() {
		return r11_gpfsr_chrg2;
	}
	public void setR11_gpfsr_chrg2(BigDecimal r11_gpfsr_chrg2) {
		this.r11_gpfsr_chrg2 = r11_gpfsr_chrg2;
	}
	public BigDecimal getR11_tot_spe_ris_chrg() {
		return r11_tot_spe_ris_chrg;
	}
	public void setR11_tot_spe_ris_chrg(BigDecimal r11_tot_spe_ris_chrg) {
		this.r11_tot_spe_ris_chrg = r11_tot_spe_ris_chrg;
	}
	public BigDecimal getR11_net_pos_gen_mar_ris() {
		return r11_net_pos_gen_mar_ris;
	}
	public void setR11_net_pos_gen_mar_ris(BigDecimal r11_net_pos_gen_mar_ris) {
		this.r11_net_pos_gen_mar_ris = r11_net_pos_gen_mar_ris;
	}
	public BigDecimal getR11_gen_mar_ris_chrg_8per() {
		return r11_gen_mar_ris_chrg_8per;
	}
	public void setR11_gen_mar_ris_chrg_8per(BigDecimal r11_gen_mar_ris_chrg_8per) {
		this.r11_gen_mar_ris_chrg_8per = r11_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR11_2per_gen_mar_ris_chrg_div_port() {
		return r11_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR11_2per_gen_mar_ris_chrg_div_port(BigDecimal r11_2per_gen_mar_ris_chrg_div_port) {
		this.r11_2per_gen_mar_ris_chrg_div_port = r11_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR11_tot_gen_mar_risk_chrg() {
		return r11_tot_gen_mar_risk_chrg;
	}
	public void setR11_tot_gen_mar_risk_chrg(BigDecimal r11_tot_gen_mar_risk_chrg) {
		this.r11_tot_gen_mar_risk_chrg = r11_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR11_tot_mar_ris_chrg() {
		return r11_tot_mar_ris_chrg;
	}
	public void setR11_tot_mar_ris_chrg(BigDecimal r11_tot_mar_ris_chrg) {
		this.r11_tot_mar_ris_chrg = r11_tot_mar_ris_chrg;
	}
	public BigDecimal getR12_market() {
		return r12_market;
	}
	public void setR12_market(BigDecimal r12_market) {
		this.r12_market = r12_market;
	}
	public BigDecimal getR12_gpfsr_nom_amt() {
		return r12_gpfsr_nom_amt;
	}
	public void setR12_gpfsr_nom_amt(BigDecimal r12_gpfsr_nom_amt) {
		this.r12_gpfsr_nom_amt = r12_gpfsr_nom_amt;
	}
	public BigDecimal getR12_gpfsr_pos_att8_per_spe_ris() {
		return r12_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att8_per_spe_ris(BigDecimal r12_gpfsr_pos_att8_per_spe_ris) {
		this.r12_gpfsr_pos_att8_per_spe_ris = r12_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg() {
		return r12_gpfsr_chrg;
	}
	public void setR12_gpfsr_chrg(BigDecimal r12_gpfsr_chrg) {
		this.r12_gpfsr_chrg = r12_gpfsr_chrg;
	}
	public BigDecimal getR12_gpfsr_nom_amt1() {
		return r12_gpfsr_nom_amt1;
	}
	public void setR12_gpfsr_nom_amt1(BigDecimal r12_gpfsr_nom_amt1) {
		this.r12_gpfsr_nom_amt1 = r12_gpfsr_nom_amt1;
	}
	public BigDecimal getR12_gpfsr_pos_att4_per_spe_ris() {
		return r12_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att4_per_spe_ris(BigDecimal r12_gpfsr_pos_att4_per_spe_ris) {
		this.r12_gpfsr_pos_att4_per_spe_ris = r12_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg1() {
		return r12_gpfsr_chrg1;
	}
	public void setR12_gpfsr_chrg1(BigDecimal r12_gpfsr_chrg1) {
		this.r12_gpfsr_chrg1 = r12_gpfsr_chrg1;
	}
	public BigDecimal getR12_gpfsr_nom_amt2() {
		return r12_gpfsr_nom_amt2;
	}
	public void setR12_gpfsr_nom_amt2(BigDecimal r12_gpfsr_nom_amt2) {
		this.r12_gpfsr_nom_amt2 = r12_gpfsr_nom_amt2;
	}
	public BigDecimal getR12_gpfsr_pos_att2_per_spe_ris() {
		return r12_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att2_per_spe_ris(BigDecimal r12_gpfsr_pos_att2_per_spe_ris) {
		this.r12_gpfsr_pos_att2_per_spe_ris = r12_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg2() {
		return r12_gpfsr_chrg2;
	}
	public void setR12_gpfsr_chrg2(BigDecimal r12_gpfsr_chrg2) {
		this.r12_gpfsr_chrg2 = r12_gpfsr_chrg2;
	}
	public BigDecimal getR12_tot_spe_ris_chrg() {
		return r12_tot_spe_ris_chrg;
	}
	public void setR12_tot_spe_ris_chrg(BigDecimal r12_tot_spe_ris_chrg) {
		this.r12_tot_spe_ris_chrg = r12_tot_spe_ris_chrg;
	}
	public BigDecimal getR12_net_pos_gen_mar_ris() {
		return r12_net_pos_gen_mar_ris;
	}
	public void setR12_net_pos_gen_mar_ris(BigDecimal r12_net_pos_gen_mar_ris) {
		this.r12_net_pos_gen_mar_ris = r12_net_pos_gen_mar_ris;
	}
	public BigDecimal getR12_gen_mar_ris_chrg_8per() {
		return r12_gen_mar_ris_chrg_8per;
	}
	public void setR12_gen_mar_ris_chrg_8per(BigDecimal r12_gen_mar_ris_chrg_8per) {
		this.r12_gen_mar_ris_chrg_8per = r12_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR12_2per_gen_mar_ris_chrg_div_port() {
		return r12_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR12_2per_gen_mar_ris_chrg_div_port(BigDecimal r12_2per_gen_mar_ris_chrg_div_port) {
		this.r12_2per_gen_mar_ris_chrg_div_port = r12_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR12_tot_gen_mar_risk_chrg() {
		return r12_tot_gen_mar_risk_chrg;
	}
	public void setR12_tot_gen_mar_risk_chrg(BigDecimal r12_tot_gen_mar_risk_chrg) {
		this.r12_tot_gen_mar_risk_chrg = r12_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR12_tot_mar_ris_chrg() {
		return r12_tot_mar_ris_chrg;
	}
	public void setR12_tot_mar_ris_chrg(BigDecimal r12_tot_mar_ris_chrg) {
		this.r12_tot_mar_ris_chrg = r12_tot_mar_ris_chrg;
	}
	public BigDecimal getR13_market() {
		return r13_market;
	}
	public void setR13_market(BigDecimal r13_market) {
		this.r13_market = r13_market;
	}
	public BigDecimal getR13_gpfsr_nom_amt() {
		return r13_gpfsr_nom_amt;
	}
	public void setR13_gpfsr_nom_amt(BigDecimal r13_gpfsr_nom_amt) {
		this.r13_gpfsr_nom_amt = r13_gpfsr_nom_amt;
	}
	public BigDecimal getR13_gpfsr_pos_att8_per_spe_ris() {
		return r13_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att8_per_spe_ris(BigDecimal r13_gpfsr_pos_att8_per_spe_ris) {
		this.r13_gpfsr_pos_att8_per_spe_ris = r13_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg() {
		return r13_gpfsr_chrg;
	}
	public void setR13_gpfsr_chrg(BigDecimal r13_gpfsr_chrg) {
		this.r13_gpfsr_chrg = r13_gpfsr_chrg;
	}
	public BigDecimal getR13_gpfsr_nom_amt1() {
		return r13_gpfsr_nom_amt1;
	}
	public void setR13_gpfsr_nom_amt1(BigDecimal r13_gpfsr_nom_amt1) {
		this.r13_gpfsr_nom_amt1 = r13_gpfsr_nom_amt1;
	}
	public BigDecimal getR13_gpfsr_pos_att4_per_spe_ris() {
		return r13_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att4_per_spe_ris(BigDecimal r13_gpfsr_pos_att4_per_spe_ris) {
		this.r13_gpfsr_pos_att4_per_spe_ris = r13_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg1() {
		return r13_gpfsr_chrg1;
	}
	public void setR13_gpfsr_chrg1(BigDecimal r13_gpfsr_chrg1) {
		this.r13_gpfsr_chrg1 = r13_gpfsr_chrg1;
	}
	public BigDecimal getR13_gpfsr_nom_amt2() {
		return r13_gpfsr_nom_amt2;
	}
	public void setR13_gpfsr_nom_amt2(BigDecimal r13_gpfsr_nom_amt2) {
		this.r13_gpfsr_nom_amt2 = r13_gpfsr_nom_amt2;
	}
	public BigDecimal getR13_gpfsr_pos_att2_per_spe_ris() {
		return r13_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att2_per_spe_ris(BigDecimal r13_gpfsr_pos_att2_per_spe_ris) {
		this.r13_gpfsr_pos_att2_per_spe_ris = r13_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg2() {
		return r13_gpfsr_chrg2;
	}
	public void setR13_gpfsr_chrg2(BigDecimal r13_gpfsr_chrg2) {
		this.r13_gpfsr_chrg2 = r13_gpfsr_chrg2;
	}
	public BigDecimal getR13_tot_spe_ris_chrg() {
		return r13_tot_spe_ris_chrg;
	}
	public void setR13_tot_spe_ris_chrg(BigDecimal r13_tot_spe_ris_chrg) {
		this.r13_tot_spe_ris_chrg = r13_tot_spe_ris_chrg;
	}
	public BigDecimal getR13_net_pos_gen_mar_ris() {
		return r13_net_pos_gen_mar_ris;
	}
	public void setR13_net_pos_gen_mar_ris(BigDecimal r13_net_pos_gen_mar_ris) {
		this.r13_net_pos_gen_mar_ris = r13_net_pos_gen_mar_ris;
	}
	public BigDecimal getR13_gen_mar_ris_chrg_8per() {
		return r13_gen_mar_ris_chrg_8per;
	}
	public void setR13_gen_mar_ris_chrg_8per(BigDecimal r13_gen_mar_ris_chrg_8per) {
		this.r13_gen_mar_ris_chrg_8per = r13_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR13_2per_gen_mar_ris_chrg_div_port() {
		return r13_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR13_2per_gen_mar_ris_chrg_div_port(BigDecimal r13_2per_gen_mar_ris_chrg_div_port) {
		this.r13_2per_gen_mar_ris_chrg_div_port = r13_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR13_tot_gen_mar_risk_chrg() {
		return r13_tot_gen_mar_risk_chrg;
	}
	public void setR13_tot_gen_mar_risk_chrg(BigDecimal r13_tot_gen_mar_risk_chrg) {
		this.r13_tot_gen_mar_risk_chrg = r13_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR13_tot_mar_ris_chrg() {
		return r13_tot_mar_ris_chrg;
	}
	public void setR13_tot_mar_ris_chrg(BigDecimal r13_tot_mar_ris_chrg) {
		this.r13_tot_mar_ris_chrg = r13_tot_mar_ris_chrg;
	}
	public BigDecimal getR14_market() {
		return r14_market;
	}
	public void setR14_market(BigDecimal r14_market) {
		this.r14_market = r14_market;
	}
	public BigDecimal getR14_gpfsr_nom_amt() {
		return r14_gpfsr_nom_amt;
	}
	public void setR14_gpfsr_nom_amt(BigDecimal r14_gpfsr_nom_amt) {
		this.r14_gpfsr_nom_amt = r14_gpfsr_nom_amt;
	}
	public BigDecimal getR14_gpfsr_pos_att8_per_spe_ris() {
		return r14_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att8_per_spe_ris(BigDecimal r14_gpfsr_pos_att8_per_spe_ris) {
		this.r14_gpfsr_pos_att8_per_spe_ris = r14_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg() {
		return r14_gpfsr_chrg;
	}
	public void setR14_gpfsr_chrg(BigDecimal r14_gpfsr_chrg) {
		this.r14_gpfsr_chrg = r14_gpfsr_chrg;
	}
	public BigDecimal getR14_gpfsr_nom_amt1() {
		return r14_gpfsr_nom_amt1;
	}
	public void setR14_gpfsr_nom_amt1(BigDecimal r14_gpfsr_nom_amt1) {
		this.r14_gpfsr_nom_amt1 = r14_gpfsr_nom_amt1;
	}
	public BigDecimal getR14_gpfsr_pos_att4_per_spe_ris() {
		return r14_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att4_per_spe_ris(BigDecimal r14_gpfsr_pos_att4_per_spe_ris) {
		this.r14_gpfsr_pos_att4_per_spe_ris = r14_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg1() {
		return r14_gpfsr_chrg1;
	}
	public void setR14_gpfsr_chrg1(BigDecimal r14_gpfsr_chrg1) {
		this.r14_gpfsr_chrg1 = r14_gpfsr_chrg1;
	}
	public BigDecimal getR14_gpfsr_nom_amt2() {
		return r14_gpfsr_nom_amt2;
	}
	public void setR14_gpfsr_nom_amt2(BigDecimal r14_gpfsr_nom_amt2) {
		this.r14_gpfsr_nom_amt2 = r14_gpfsr_nom_amt2;
	}
	public BigDecimal getR14_gpfsr_pos_att2_per_spe_ris() {
		return r14_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att2_per_spe_ris(BigDecimal r14_gpfsr_pos_att2_per_spe_ris) {
		this.r14_gpfsr_pos_att2_per_spe_ris = r14_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg2() {
		return r14_gpfsr_chrg2;
	}
	public void setR14_gpfsr_chrg2(BigDecimal r14_gpfsr_chrg2) {
		this.r14_gpfsr_chrg2 = r14_gpfsr_chrg2;
	}
	public BigDecimal getR14_tot_spe_ris_chrg() {
		return r14_tot_spe_ris_chrg;
	}
	public void setR14_tot_spe_ris_chrg(BigDecimal r14_tot_spe_ris_chrg) {
		this.r14_tot_spe_ris_chrg = r14_tot_spe_ris_chrg;
	}
	public BigDecimal getR14_net_pos_gen_mar_ris() {
		return r14_net_pos_gen_mar_ris;
	}
	public void setR14_net_pos_gen_mar_ris(BigDecimal r14_net_pos_gen_mar_ris) {
		this.r14_net_pos_gen_mar_ris = r14_net_pos_gen_mar_ris;
	}
	public BigDecimal getR14_gen_mar_ris_chrg_8per() {
		return r14_gen_mar_ris_chrg_8per;
	}
	public void setR14_gen_mar_ris_chrg_8per(BigDecimal r14_gen_mar_ris_chrg_8per) {
		this.r14_gen_mar_ris_chrg_8per = r14_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR14_2per_gen_mar_ris_chrg_div_port() {
		return r14_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR14_2per_gen_mar_ris_chrg_div_port(BigDecimal r14_2per_gen_mar_ris_chrg_div_port) {
		this.r14_2per_gen_mar_ris_chrg_div_port = r14_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR14_tot_gen_mar_risk_chrg() {
		return r14_tot_gen_mar_risk_chrg;
	}
	public void setR14_tot_gen_mar_risk_chrg(BigDecimal r14_tot_gen_mar_risk_chrg) {
		this.r14_tot_gen_mar_risk_chrg = r14_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR14_tot_mar_ris_chrg() {
		return r14_tot_mar_ris_chrg;
	}
	public void setR14_tot_mar_ris_chrg(BigDecimal r14_tot_mar_ris_chrg) {
		this.r14_tot_mar_ris_chrg = r14_tot_mar_ris_chrg;
	}
	public BigDecimal getR15_market() {
		return r15_market;
	}
	public void setR15_market(BigDecimal r15_market) {
		this.r15_market = r15_market;
	}
	public BigDecimal getR15_gpfsr_nom_amt() {
		return r15_gpfsr_nom_amt;
	}
	public void setR15_gpfsr_nom_amt(BigDecimal r15_gpfsr_nom_amt) {
		this.r15_gpfsr_nom_amt = r15_gpfsr_nom_amt;
	}
	public BigDecimal getR15_gpfsr_pos_att8_per_spe_ris() {
		return r15_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att8_per_spe_ris(BigDecimal r15_gpfsr_pos_att8_per_spe_ris) {
		this.r15_gpfsr_pos_att8_per_spe_ris = r15_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg() {
		return r15_gpfsr_chrg;
	}
	public void setR15_gpfsr_chrg(BigDecimal r15_gpfsr_chrg) {
		this.r15_gpfsr_chrg = r15_gpfsr_chrg;
	}
	public BigDecimal getR15_gpfsr_nom_amt1() {
		return r15_gpfsr_nom_amt1;
	}
	public void setR15_gpfsr_nom_amt1(BigDecimal r15_gpfsr_nom_amt1) {
		this.r15_gpfsr_nom_amt1 = r15_gpfsr_nom_amt1;
	}
	public BigDecimal getR15_gpfsr_pos_att4_per_spe_ris() {
		return r15_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att4_per_spe_ris(BigDecimal r15_gpfsr_pos_att4_per_spe_ris) {
		this.r15_gpfsr_pos_att4_per_spe_ris = r15_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg1() {
		return r15_gpfsr_chrg1;
	}
	public void setR15_gpfsr_chrg1(BigDecimal r15_gpfsr_chrg1) {
		this.r15_gpfsr_chrg1 = r15_gpfsr_chrg1;
	}
	public BigDecimal getR15_gpfsr_nom_amt2() {
		return r15_gpfsr_nom_amt2;
	}
	public void setR15_gpfsr_nom_amt2(BigDecimal r15_gpfsr_nom_amt2) {
		this.r15_gpfsr_nom_amt2 = r15_gpfsr_nom_amt2;
	}
	public BigDecimal getR15_gpfsr_pos_att2_per_spe_ris() {
		return r15_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att2_per_spe_ris(BigDecimal r15_gpfsr_pos_att2_per_spe_ris) {
		this.r15_gpfsr_pos_att2_per_spe_ris = r15_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg2() {
		return r15_gpfsr_chrg2;
	}
	public void setR15_gpfsr_chrg2(BigDecimal r15_gpfsr_chrg2) {
		this.r15_gpfsr_chrg2 = r15_gpfsr_chrg2;
	}
	public BigDecimal getR15_tot_spe_ris_chrg() {
		return r15_tot_spe_ris_chrg;
	}
	public void setR15_tot_spe_ris_chrg(BigDecimal r15_tot_spe_ris_chrg) {
		this.r15_tot_spe_ris_chrg = r15_tot_spe_ris_chrg;
	}
	public BigDecimal getR15_net_pos_gen_mar_ris() {
		return r15_net_pos_gen_mar_ris;
	}
	public void setR15_net_pos_gen_mar_ris(BigDecimal r15_net_pos_gen_mar_ris) {
		this.r15_net_pos_gen_mar_ris = r15_net_pos_gen_mar_ris;
	}
	public BigDecimal getR15_gen_mar_ris_chrg_8per() {
		return r15_gen_mar_ris_chrg_8per;
	}
	public void setR15_gen_mar_ris_chrg_8per(BigDecimal r15_gen_mar_ris_chrg_8per) {
		this.r15_gen_mar_ris_chrg_8per = r15_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR15_2per_gen_mar_ris_chrg_div_port() {
		return r15_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR15_2per_gen_mar_ris_chrg_div_port(BigDecimal r15_2per_gen_mar_ris_chrg_div_port) {
		this.r15_2per_gen_mar_ris_chrg_div_port = r15_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR15_tot_gen_mar_risk_chrg() {
		return r15_tot_gen_mar_risk_chrg;
	}
	public void setR15_tot_gen_mar_risk_chrg(BigDecimal r15_tot_gen_mar_risk_chrg) {
		this.r15_tot_gen_mar_risk_chrg = r15_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR15_tot_mar_ris_chrg() {
		return r15_tot_mar_ris_chrg;
	}
	public void setR15_tot_mar_ris_chrg(BigDecimal r15_tot_mar_ris_chrg) {
		this.r15_tot_mar_ris_chrg = r15_tot_mar_ris_chrg;
	}
	public BigDecimal getR16_market() {
		return r16_market;
	}
	public void setR16_market(BigDecimal r16_market) {
		this.r16_market = r16_market;
	}
	public BigDecimal getR16_gpfsr_nom_amt() {
		return r16_gpfsr_nom_amt;
	}
	public void setR16_gpfsr_nom_amt(BigDecimal r16_gpfsr_nom_amt) {
		this.r16_gpfsr_nom_amt = r16_gpfsr_nom_amt;
	}
	public BigDecimal getR16_gpfsr_pos_att8_per_spe_ris() {
		return r16_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att8_per_spe_ris(BigDecimal r16_gpfsr_pos_att8_per_spe_ris) {
		this.r16_gpfsr_pos_att8_per_spe_ris = r16_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg() {
		return r16_gpfsr_chrg;
	}
	public void setR16_gpfsr_chrg(BigDecimal r16_gpfsr_chrg) {
		this.r16_gpfsr_chrg = r16_gpfsr_chrg;
	}
	public BigDecimal getR16_gpfsr_nom_amt1() {
		return r16_gpfsr_nom_amt1;
	}
	public void setR16_gpfsr_nom_amt1(BigDecimal r16_gpfsr_nom_amt1) {
		this.r16_gpfsr_nom_amt1 = r16_gpfsr_nom_amt1;
	}
	public BigDecimal getR16_gpfsr_pos_att4_per_spe_ris() {
		return r16_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att4_per_spe_ris(BigDecimal r16_gpfsr_pos_att4_per_spe_ris) {
		this.r16_gpfsr_pos_att4_per_spe_ris = r16_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg1() {
		return r16_gpfsr_chrg1;
	}
	public void setR16_gpfsr_chrg1(BigDecimal r16_gpfsr_chrg1) {
		this.r16_gpfsr_chrg1 = r16_gpfsr_chrg1;
	}
	public BigDecimal getR16_gpfsr_nom_amt2() {
		return r16_gpfsr_nom_amt2;
	}
	public void setR16_gpfsr_nom_amt2(BigDecimal r16_gpfsr_nom_amt2) {
		this.r16_gpfsr_nom_amt2 = r16_gpfsr_nom_amt2;
	}
	public BigDecimal getR16_gpfsr_pos_att2_per_spe_ris() {
		return r16_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att2_per_spe_ris(BigDecimal r16_gpfsr_pos_att2_per_spe_ris) {
		this.r16_gpfsr_pos_att2_per_spe_ris = r16_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg2() {
		return r16_gpfsr_chrg2;
	}
	public void setR16_gpfsr_chrg2(BigDecimal r16_gpfsr_chrg2) {
		this.r16_gpfsr_chrg2 = r16_gpfsr_chrg2;
	}
	public BigDecimal getR16_tot_spe_ris_chrg() {
		return r16_tot_spe_ris_chrg;
	}
	public void setR16_tot_spe_ris_chrg(BigDecimal r16_tot_spe_ris_chrg) {
		this.r16_tot_spe_ris_chrg = r16_tot_spe_ris_chrg;
	}
	public BigDecimal getR16_net_pos_gen_mar_ris() {
		return r16_net_pos_gen_mar_ris;
	}
	public void setR16_net_pos_gen_mar_ris(BigDecimal r16_net_pos_gen_mar_ris) {
		this.r16_net_pos_gen_mar_ris = r16_net_pos_gen_mar_ris;
	}
	public BigDecimal getR16_gen_mar_ris_chrg_8per() {
		return r16_gen_mar_ris_chrg_8per;
	}
	public void setR16_gen_mar_ris_chrg_8per(BigDecimal r16_gen_mar_ris_chrg_8per) {
		this.r16_gen_mar_ris_chrg_8per = r16_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR16_2per_gen_mar_ris_chrg_div_port() {
		return r16_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR16_2per_gen_mar_ris_chrg_div_port(BigDecimal r16_2per_gen_mar_ris_chrg_div_port) {
		this.r16_2per_gen_mar_ris_chrg_div_port = r16_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR16_tot_gen_mar_risk_chrg() {
		return r16_tot_gen_mar_risk_chrg;
	}
	public void setR16_tot_gen_mar_risk_chrg(BigDecimal r16_tot_gen_mar_risk_chrg) {
		this.r16_tot_gen_mar_risk_chrg = r16_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR16_tot_mar_ris_chrg() {
		return r16_tot_mar_ris_chrg;
	}
	public void setR16_tot_mar_ris_chrg(BigDecimal r16_tot_mar_ris_chrg) {
		this.r16_tot_mar_ris_chrg = r16_tot_mar_ris_chrg;
	}
	public BigDecimal getR17_market() {
		return r17_market;
	}
	public void setR17_market(BigDecimal r17_market) {
		this.r17_market = r17_market;
	}
	public BigDecimal getR17_gpfsr_nom_amt() {
		return r17_gpfsr_nom_amt;
	}
	public void setR17_gpfsr_nom_amt(BigDecimal r17_gpfsr_nom_amt) {
		this.r17_gpfsr_nom_amt = r17_gpfsr_nom_amt;
	}
	public BigDecimal getR17_gpfsr_pos_att8_per_spe_ris() {
		return r17_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att8_per_spe_ris(BigDecimal r17_gpfsr_pos_att8_per_spe_ris) {
		this.r17_gpfsr_pos_att8_per_spe_ris = r17_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg() {
		return r17_gpfsr_chrg;
	}
	public void setR17_gpfsr_chrg(BigDecimal r17_gpfsr_chrg) {
		this.r17_gpfsr_chrg = r17_gpfsr_chrg;
	}
	public BigDecimal getR17_gpfsr_nom_amt1() {
		return r17_gpfsr_nom_amt1;
	}
	public void setR17_gpfsr_nom_amt1(BigDecimal r17_gpfsr_nom_amt1) {
		this.r17_gpfsr_nom_amt1 = r17_gpfsr_nom_amt1;
	}
	public BigDecimal getR17_gpfsr_pos_att4_per_spe_ris() {
		return r17_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att4_per_spe_ris(BigDecimal r17_gpfsr_pos_att4_per_spe_ris) {
		this.r17_gpfsr_pos_att4_per_spe_ris = r17_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg1() {
		return r17_gpfsr_chrg1;
	}
	public void setR17_gpfsr_chrg1(BigDecimal r17_gpfsr_chrg1) {
		this.r17_gpfsr_chrg1 = r17_gpfsr_chrg1;
	}
	public BigDecimal getR17_gpfsr_nom_amt2() {
		return r17_gpfsr_nom_amt2;
	}
	public void setR17_gpfsr_nom_amt2(BigDecimal r17_gpfsr_nom_amt2) {
		this.r17_gpfsr_nom_amt2 = r17_gpfsr_nom_amt2;
	}
	public BigDecimal getR17_gpfsr_pos_att2_per_spe_ris() {
		return r17_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att2_per_spe_ris(BigDecimal r17_gpfsr_pos_att2_per_spe_ris) {
		this.r17_gpfsr_pos_att2_per_spe_ris = r17_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg2() {
		return r17_gpfsr_chrg2;
	}
	public void setR17_gpfsr_chrg2(BigDecimal r17_gpfsr_chrg2) {
		this.r17_gpfsr_chrg2 = r17_gpfsr_chrg2;
	}
	public BigDecimal getR17_tot_spe_ris_chrg() {
		return r17_tot_spe_ris_chrg;
	}
	public void setR17_tot_spe_ris_chrg(BigDecimal r17_tot_spe_ris_chrg) {
		this.r17_tot_spe_ris_chrg = r17_tot_spe_ris_chrg;
	}
	public BigDecimal getR17_net_pos_gen_mar_ris() {
		return r17_net_pos_gen_mar_ris;
	}
	public void setR17_net_pos_gen_mar_ris(BigDecimal r17_net_pos_gen_mar_ris) {
		this.r17_net_pos_gen_mar_ris = r17_net_pos_gen_mar_ris;
	}
	public BigDecimal getR17_gen_mar_ris_chrg_8per() {
		return r17_gen_mar_ris_chrg_8per;
	}
	public void setR17_gen_mar_ris_chrg_8per(BigDecimal r17_gen_mar_ris_chrg_8per) {
		this.r17_gen_mar_ris_chrg_8per = r17_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR17_2per_gen_mar_ris_chrg_div_port() {
		return r17_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR17_2per_gen_mar_ris_chrg_div_port(BigDecimal r17_2per_gen_mar_ris_chrg_div_port) {
		this.r17_2per_gen_mar_ris_chrg_div_port = r17_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR17_tot_gen_mar_risk_chrg() {
		return r17_tot_gen_mar_risk_chrg;
	}
	public void setR17_tot_gen_mar_risk_chrg(BigDecimal r17_tot_gen_mar_risk_chrg) {
		this.r17_tot_gen_mar_risk_chrg = r17_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR17_tot_mar_ris_chrg() {
		return r17_tot_mar_ris_chrg;
	}
	public void setR17_tot_mar_ris_chrg(BigDecimal r17_tot_mar_ris_chrg) {
		this.r17_tot_mar_ris_chrg = r17_tot_mar_ris_chrg;
	}
	public BigDecimal getR18_market() {
		return r18_market;
	}
	public void setR18_market(BigDecimal r18_market) {
		this.r18_market = r18_market;
	}
	public BigDecimal getR18_gpfsr_nom_amt() {
		return r18_gpfsr_nom_amt;
	}
	public void setR18_gpfsr_nom_amt(BigDecimal r18_gpfsr_nom_amt) {
		this.r18_gpfsr_nom_amt = r18_gpfsr_nom_amt;
	}
	public BigDecimal getR18_gpfsr_pos_att8_per_spe_ris() {
		return r18_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att8_per_spe_ris(BigDecimal r18_gpfsr_pos_att8_per_spe_ris) {
		this.r18_gpfsr_pos_att8_per_spe_ris = r18_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg() {
		return r18_gpfsr_chrg;
	}
	public void setR18_gpfsr_chrg(BigDecimal r18_gpfsr_chrg) {
		this.r18_gpfsr_chrg = r18_gpfsr_chrg;
	}
	public BigDecimal getR18_gpfsr_nom_amt1() {
		return r18_gpfsr_nom_amt1;
	}
	public void setR18_gpfsr_nom_amt1(BigDecimal r18_gpfsr_nom_amt1) {
		this.r18_gpfsr_nom_amt1 = r18_gpfsr_nom_amt1;
	}
	public BigDecimal getR18_gpfsr_pos_att4_per_spe_ris() {
		return r18_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att4_per_spe_ris(BigDecimal r18_gpfsr_pos_att4_per_spe_ris) {
		this.r18_gpfsr_pos_att4_per_spe_ris = r18_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg1() {
		return r18_gpfsr_chrg1;
	}
	public void setR18_gpfsr_chrg1(BigDecimal r18_gpfsr_chrg1) {
		this.r18_gpfsr_chrg1 = r18_gpfsr_chrg1;
	}
	public BigDecimal getR18_gpfsr_nom_amt2() {
		return r18_gpfsr_nom_amt2;
	}
	public void setR18_gpfsr_nom_amt2(BigDecimal r18_gpfsr_nom_amt2) {
		this.r18_gpfsr_nom_amt2 = r18_gpfsr_nom_amt2;
	}
	public BigDecimal getR18_gpfsr_pos_att2_per_spe_ris() {
		return r18_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att2_per_spe_ris(BigDecimal r18_gpfsr_pos_att2_per_spe_ris) {
		this.r18_gpfsr_pos_att2_per_spe_ris = r18_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg2() {
		return r18_gpfsr_chrg2;
	}
	public void setR18_gpfsr_chrg2(BigDecimal r18_gpfsr_chrg2) {
		this.r18_gpfsr_chrg2 = r18_gpfsr_chrg2;
	}
	public BigDecimal getR18_tot_spe_ris_chrg() {
		return r18_tot_spe_ris_chrg;
	}
	public void setR18_tot_spe_ris_chrg(BigDecimal r18_tot_spe_ris_chrg) {
		this.r18_tot_spe_ris_chrg = r18_tot_spe_ris_chrg;
	}
	public BigDecimal getR18_net_pos_gen_mar_ris() {
		return r18_net_pos_gen_mar_ris;
	}
	public void setR18_net_pos_gen_mar_ris(BigDecimal r18_net_pos_gen_mar_ris) {
		this.r18_net_pos_gen_mar_ris = r18_net_pos_gen_mar_ris;
	}
	public BigDecimal getR18_gen_mar_ris_chrg_8per() {
		return r18_gen_mar_ris_chrg_8per;
	}
	public void setR18_gen_mar_ris_chrg_8per(BigDecimal r18_gen_mar_ris_chrg_8per) {
		this.r18_gen_mar_ris_chrg_8per = r18_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR18_2per_gen_mar_ris_chrg_div_port() {
		return r18_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR18_2per_gen_mar_ris_chrg_div_port(BigDecimal r18_2per_gen_mar_ris_chrg_div_port) {
		this.r18_2per_gen_mar_ris_chrg_div_port = r18_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR18_tot_gen_mar_risk_chrg() {
		return r18_tot_gen_mar_risk_chrg;
	}
	public void setR18_tot_gen_mar_risk_chrg(BigDecimal r18_tot_gen_mar_risk_chrg) {
		this.r18_tot_gen_mar_risk_chrg = r18_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR18_tot_mar_ris_chrg() {
		return r18_tot_mar_ris_chrg;
	}
	public void setR18_tot_mar_ris_chrg(BigDecimal r18_tot_mar_ris_chrg) {
		this.r18_tot_mar_ris_chrg = r18_tot_mar_ris_chrg;
	}
	public BigDecimal getR19_market() {
		return r19_market;
	}
	public void setR19_market(BigDecimal r19_market) {
		this.r19_market = r19_market;
	}
	public BigDecimal getR19_gpfsr_nom_amt() {
		return r19_gpfsr_nom_amt;
	}
	public void setR19_gpfsr_nom_amt(BigDecimal r19_gpfsr_nom_amt) {
		this.r19_gpfsr_nom_amt = r19_gpfsr_nom_amt;
	}
	public BigDecimal getR19_gpfsr_pos_att8_per_spe_ris() {
		return r19_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att8_per_spe_ris(BigDecimal r19_gpfsr_pos_att8_per_spe_ris) {
		this.r19_gpfsr_pos_att8_per_spe_ris = r19_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg() {
		return r19_gpfsr_chrg;
	}
	public void setR19_gpfsr_chrg(BigDecimal r19_gpfsr_chrg) {
		this.r19_gpfsr_chrg = r19_gpfsr_chrg;
	}
	public BigDecimal getR19_gpfsr_nom_amt1() {
		return r19_gpfsr_nom_amt1;
	}
	public void setR19_gpfsr_nom_amt1(BigDecimal r19_gpfsr_nom_amt1) {
		this.r19_gpfsr_nom_amt1 = r19_gpfsr_nom_amt1;
	}
	public BigDecimal getR19_gpfsr_pos_att4_per_spe_ris() {
		return r19_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att4_per_spe_ris(BigDecimal r19_gpfsr_pos_att4_per_spe_ris) {
		this.r19_gpfsr_pos_att4_per_spe_ris = r19_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg1() {
		return r19_gpfsr_chrg1;
	}
	public void setR19_gpfsr_chrg1(BigDecimal r19_gpfsr_chrg1) {
		this.r19_gpfsr_chrg1 = r19_gpfsr_chrg1;
	}
	public BigDecimal getR19_gpfsr_nom_amt2() {
		return r19_gpfsr_nom_amt2;
	}
	public void setR19_gpfsr_nom_amt2(BigDecimal r19_gpfsr_nom_amt2) {
		this.r19_gpfsr_nom_amt2 = r19_gpfsr_nom_amt2;
	}
	public BigDecimal getR19_gpfsr_pos_att2_per_spe_ris() {
		return r19_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att2_per_spe_ris(BigDecimal r19_gpfsr_pos_att2_per_spe_ris) {
		this.r19_gpfsr_pos_att2_per_spe_ris = r19_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg2() {
		return r19_gpfsr_chrg2;
	}
	public void setR19_gpfsr_chrg2(BigDecimal r19_gpfsr_chrg2) {
		this.r19_gpfsr_chrg2 = r19_gpfsr_chrg2;
	}
	public BigDecimal getR19_tot_spe_ris_chrg() {
		return r19_tot_spe_ris_chrg;
	}
	public void setR19_tot_spe_ris_chrg(BigDecimal r19_tot_spe_ris_chrg) {
		this.r19_tot_spe_ris_chrg = r19_tot_spe_ris_chrg;
	}
	public BigDecimal getR19_net_pos_gen_mar_ris() {
		return r19_net_pos_gen_mar_ris;
	}
	public void setR19_net_pos_gen_mar_ris(BigDecimal r19_net_pos_gen_mar_ris) {
		this.r19_net_pos_gen_mar_ris = r19_net_pos_gen_mar_ris;
	}
	public BigDecimal getR19_gen_mar_ris_chrg_8per() {
		return r19_gen_mar_ris_chrg_8per;
	}
	public void setR19_gen_mar_ris_chrg_8per(BigDecimal r19_gen_mar_ris_chrg_8per) {
		this.r19_gen_mar_ris_chrg_8per = r19_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR19_2per_gen_mar_ris_chrg_div_port() {
		return r19_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR19_2per_gen_mar_ris_chrg_div_port(BigDecimal r19_2per_gen_mar_ris_chrg_div_port) {
		this.r19_2per_gen_mar_ris_chrg_div_port = r19_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR19_tot_gen_mar_risk_chrg() {
		return r19_tot_gen_mar_risk_chrg;
	}
	public void setR19_tot_gen_mar_risk_chrg(BigDecimal r19_tot_gen_mar_risk_chrg) {
		this.r19_tot_gen_mar_risk_chrg = r19_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR19_tot_mar_ris_chrg() {
		return r19_tot_mar_ris_chrg;
	}
	public void setR19_tot_mar_ris_chrg(BigDecimal r19_tot_mar_ris_chrg) {
		this.r19_tot_mar_ris_chrg = r19_tot_mar_ris_chrg;
	}
	public BigDecimal getR20_market() {
		return r20_market;
	}
	public void setR20_market(BigDecimal r20_market) {
		this.r20_market = r20_market;
	}
	public BigDecimal getR20_gpfsr_nom_amt() {
		return r20_gpfsr_nom_amt;
	}
	public void setR20_gpfsr_nom_amt(BigDecimal r20_gpfsr_nom_amt) {
		this.r20_gpfsr_nom_amt = r20_gpfsr_nom_amt;
	}
	public BigDecimal getR20_gpfsr_pos_att8_per_spe_ris() {
		return r20_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att8_per_spe_ris(BigDecimal r20_gpfsr_pos_att8_per_spe_ris) {
		this.r20_gpfsr_pos_att8_per_spe_ris = r20_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg() {
		return r20_gpfsr_chrg;
	}
	public void setR20_gpfsr_chrg(BigDecimal r20_gpfsr_chrg) {
		this.r20_gpfsr_chrg = r20_gpfsr_chrg;
	}
	public BigDecimal getR20_gpfsr_nom_amt1() {
		return r20_gpfsr_nom_amt1;
	}
	public void setR20_gpfsr_nom_amt1(BigDecimal r20_gpfsr_nom_amt1) {
		this.r20_gpfsr_nom_amt1 = r20_gpfsr_nom_amt1;
	}
	public BigDecimal getR20_gpfsr_pos_att4_per_spe_ris() {
		return r20_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att4_per_spe_ris(BigDecimal r20_gpfsr_pos_att4_per_spe_ris) {
		this.r20_gpfsr_pos_att4_per_spe_ris = r20_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg1() {
		return r20_gpfsr_chrg1;
	}
	public void setR20_gpfsr_chrg1(BigDecimal r20_gpfsr_chrg1) {
		this.r20_gpfsr_chrg1 = r20_gpfsr_chrg1;
	}
	public BigDecimal getR20_gpfsr_nom_amt2() {
		return r20_gpfsr_nom_amt2;
	}
	public void setR20_gpfsr_nom_amt2(BigDecimal r20_gpfsr_nom_amt2) {
		this.r20_gpfsr_nom_amt2 = r20_gpfsr_nom_amt2;
	}
	public BigDecimal getR20_gpfsr_pos_att2_per_spe_ris() {
		return r20_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att2_per_spe_ris(BigDecimal r20_gpfsr_pos_att2_per_spe_ris) {
		this.r20_gpfsr_pos_att2_per_spe_ris = r20_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg2() {
		return r20_gpfsr_chrg2;
	}
	public void setR20_gpfsr_chrg2(BigDecimal r20_gpfsr_chrg2) {
		this.r20_gpfsr_chrg2 = r20_gpfsr_chrg2;
	}
	public BigDecimal getR20_tot_spe_ris_chrg() {
		return r20_tot_spe_ris_chrg;
	}
	public void setR20_tot_spe_ris_chrg(BigDecimal r20_tot_spe_ris_chrg) {
		this.r20_tot_spe_ris_chrg = r20_tot_spe_ris_chrg;
	}
	public BigDecimal getR20_net_pos_gen_mar_ris() {
		return r20_net_pos_gen_mar_ris;
	}
	public void setR20_net_pos_gen_mar_ris(BigDecimal r20_net_pos_gen_mar_ris) {
		this.r20_net_pos_gen_mar_ris = r20_net_pos_gen_mar_ris;
	}
	public BigDecimal getR20_gen_mar_ris_chrg_8per() {
		return r20_gen_mar_ris_chrg_8per;
	}
	public void setR20_gen_mar_ris_chrg_8per(BigDecimal r20_gen_mar_ris_chrg_8per) {
		this.r20_gen_mar_ris_chrg_8per = r20_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR20_2per_gen_mar_ris_chrg_div_port() {
		return r20_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR20_2per_gen_mar_ris_chrg_div_port(BigDecimal r20_2per_gen_mar_ris_chrg_div_port) {
		this.r20_2per_gen_mar_ris_chrg_div_port = r20_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR20_tot_gen_mar_risk_chrg() {
		return r20_tot_gen_mar_risk_chrg;
	}
	public void setR20_tot_gen_mar_risk_chrg(BigDecimal r20_tot_gen_mar_risk_chrg) {
		this.r20_tot_gen_mar_risk_chrg = r20_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR20_tot_mar_ris_chrg() {
		return r20_tot_mar_ris_chrg;
	}
	public void setR20_tot_mar_ris_chrg(BigDecimal r20_tot_mar_ris_chrg) {
		this.r20_tot_mar_ris_chrg = r20_tot_mar_ris_chrg;
	}
	public BigDecimal getR21_market() {
		return r21_market;
	}
	public void setR21_market(BigDecimal r21_market) {
		this.r21_market = r21_market;
	}
	public BigDecimal getR21_gpfsr_nom_amt() {
		return r21_gpfsr_nom_amt;
	}
	public void setR21_gpfsr_nom_amt(BigDecimal r21_gpfsr_nom_amt) {
		this.r21_gpfsr_nom_amt = r21_gpfsr_nom_amt;
	}
	public BigDecimal getR21_gpfsr_pos_att8_per_spe_ris() {
		return r21_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att8_per_spe_ris(BigDecimal r21_gpfsr_pos_att8_per_spe_ris) {
		this.r21_gpfsr_pos_att8_per_spe_ris = r21_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg() {
		return r21_gpfsr_chrg;
	}
	public void setR21_gpfsr_chrg(BigDecimal r21_gpfsr_chrg) {
		this.r21_gpfsr_chrg = r21_gpfsr_chrg;
	}
	public BigDecimal getR21_gpfsr_nom_amt1() {
		return r21_gpfsr_nom_amt1;
	}
	public void setR21_gpfsr_nom_amt1(BigDecimal r21_gpfsr_nom_amt1) {
		this.r21_gpfsr_nom_amt1 = r21_gpfsr_nom_amt1;
	}
	public BigDecimal getR21_gpfsr_pos_att4_per_spe_ris() {
		return r21_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att4_per_spe_ris(BigDecimal r21_gpfsr_pos_att4_per_spe_ris) {
		this.r21_gpfsr_pos_att4_per_spe_ris = r21_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg1() {
		return r21_gpfsr_chrg1;
	}
	public void setR21_gpfsr_chrg1(BigDecimal r21_gpfsr_chrg1) {
		this.r21_gpfsr_chrg1 = r21_gpfsr_chrg1;
	}
	public BigDecimal getR21_gpfsr_nom_amt2() {
		return r21_gpfsr_nom_amt2;
	}
	public void setR21_gpfsr_nom_amt2(BigDecimal r21_gpfsr_nom_amt2) {
		this.r21_gpfsr_nom_amt2 = r21_gpfsr_nom_amt2;
	}
	public BigDecimal getR21_gpfsr_pos_att2_per_spe_ris() {
		return r21_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att2_per_spe_ris(BigDecimal r21_gpfsr_pos_att2_per_spe_ris) {
		this.r21_gpfsr_pos_att2_per_spe_ris = r21_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg2() {
		return r21_gpfsr_chrg2;
	}
	public void setR21_gpfsr_chrg2(BigDecimal r21_gpfsr_chrg2) {
		this.r21_gpfsr_chrg2 = r21_gpfsr_chrg2;
	}
	public BigDecimal getR21_tot_spe_ris_chrg() {
		return r21_tot_spe_ris_chrg;
	}
	public void setR21_tot_spe_ris_chrg(BigDecimal r21_tot_spe_ris_chrg) {
		this.r21_tot_spe_ris_chrg = r21_tot_spe_ris_chrg;
	}
	public BigDecimal getR21_net_pos_gen_mar_ris() {
		return r21_net_pos_gen_mar_ris;
	}
	public void setR21_net_pos_gen_mar_ris(BigDecimal r21_net_pos_gen_mar_ris) {
		this.r21_net_pos_gen_mar_ris = r21_net_pos_gen_mar_ris;
	}
	public BigDecimal getR21_gen_mar_ris_chrg_8per() {
		return r21_gen_mar_ris_chrg_8per;
	}
	public void setR21_gen_mar_ris_chrg_8per(BigDecimal r21_gen_mar_ris_chrg_8per) {
		this.r21_gen_mar_ris_chrg_8per = r21_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR21_2per_gen_mar_ris_chrg_div_port() {
		return r21_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR21_2per_gen_mar_ris_chrg_div_port(BigDecimal r21_2per_gen_mar_ris_chrg_div_port) {
		this.r21_2per_gen_mar_ris_chrg_div_port = r21_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR21_tot_gen_mar_risk_chrg() {
		return r21_tot_gen_mar_risk_chrg;
	}
	public void setR21_tot_gen_mar_risk_chrg(BigDecimal r21_tot_gen_mar_risk_chrg) {
		this.r21_tot_gen_mar_risk_chrg = r21_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR21_tot_mar_ris_chrg() {
		return r21_tot_mar_ris_chrg;
	}
	public void setR21_tot_mar_ris_chrg(BigDecimal r21_tot_mar_ris_chrg) {
		this.r21_tot_mar_ris_chrg = r21_tot_mar_ris_chrg;
	}
	public BigDecimal getR22_market() {
		return r22_market;
	}
	public void setR22_market(BigDecimal r22_market) {
		this.r22_market = r22_market;
	}
	public BigDecimal getR22_gpfsr_nom_amt() {
		return r22_gpfsr_nom_amt;
	}
	public void setR22_gpfsr_nom_amt(BigDecimal r22_gpfsr_nom_amt) {
		this.r22_gpfsr_nom_amt = r22_gpfsr_nom_amt;
	}
	public BigDecimal getR22_gpfsr_pos_att8_per_spe_ris() {
		return r22_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att8_per_spe_ris(BigDecimal r22_gpfsr_pos_att8_per_spe_ris) {
		this.r22_gpfsr_pos_att8_per_spe_ris = r22_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg() {
		return r22_gpfsr_chrg;
	}
	public void setR22_gpfsr_chrg(BigDecimal r22_gpfsr_chrg) {
		this.r22_gpfsr_chrg = r22_gpfsr_chrg;
	}
	public BigDecimal getR22_gpfsr_nom_amt1() {
		return r22_gpfsr_nom_amt1;
	}
	public void setR22_gpfsr_nom_amt1(BigDecimal r22_gpfsr_nom_amt1) {
		this.r22_gpfsr_nom_amt1 = r22_gpfsr_nom_amt1;
	}
	public BigDecimal getR22_gpfsr_pos_att4_per_spe_ris() {
		return r22_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att4_per_spe_ris(BigDecimal r22_gpfsr_pos_att4_per_spe_ris) {
		this.r22_gpfsr_pos_att4_per_spe_ris = r22_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg1() {
		return r22_gpfsr_chrg1;
	}
	public void setR22_gpfsr_chrg1(BigDecimal r22_gpfsr_chrg1) {
		this.r22_gpfsr_chrg1 = r22_gpfsr_chrg1;
	}
	public BigDecimal getR22_gpfsr_nom_amt2() {
		return r22_gpfsr_nom_amt2;
	}
	public void setR22_gpfsr_nom_amt2(BigDecimal r22_gpfsr_nom_amt2) {
		this.r22_gpfsr_nom_amt2 = r22_gpfsr_nom_amt2;
	}
	public BigDecimal getR22_gpfsr_pos_att2_per_spe_ris() {
		return r22_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att2_per_spe_ris(BigDecimal r22_gpfsr_pos_att2_per_spe_ris) {
		this.r22_gpfsr_pos_att2_per_spe_ris = r22_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg2() {
		return r22_gpfsr_chrg2;
	}
	public void setR22_gpfsr_chrg2(BigDecimal r22_gpfsr_chrg2) {
		this.r22_gpfsr_chrg2 = r22_gpfsr_chrg2;
	}
	public BigDecimal getR22_tot_spe_ris_chrg() {
		return r22_tot_spe_ris_chrg;
	}
	public void setR22_tot_spe_ris_chrg(BigDecimal r22_tot_spe_ris_chrg) {
		this.r22_tot_spe_ris_chrg = r22_tot_spe_ris_chrg;
	}
	public BigDecimal getR22_net_pos_gen_mar_ris() {
		return r22_net_pos_gen_mar_ris;
	}
	public void setR22_net_pos_gen_mar_ris(BigDecimal r22_net_pos_gen_mar_ris) {
		this.r22_net_pos_gen_mar_ris = r22_net_pos_gen_mar_ris;
	}
	public BigDecimal getR22_gen_mar_ris_chrg_8per() {
		return r22_gen_mar_ris_chrg_8per;
	}
	public void setR22_gen_mar_ris_chrg_8per(BigDecimal r22_gen_mar_ris_chrg_8per) {
		this.r22_gen_mar_ris_chrg_8per = r22_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR22_2per_gen_mar_ris_chrg_div_port() {
		return r22_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR22_2per_gen_mar_ris_chrg_div_port(BigDecimal r22_2per_gen_mar_ris_chrg_div_port) {
		this.r22_2per_gen_mar_ris_chrg_div_port = r22_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR22_tot_gen_mar_risk_chrg() {
		return r22_tot_gen_mar_risk_chrg;
	}
	public void setR22_tot_gen_mar_risk_chrg(BigDecimal r22_tot_gen_mar_risk_chrg) {
		this.r22_tot_gen_mar_risk_chrg = r22_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR22_tot_mar_ris_chrg() {
		return r22_tot_mar_ris_chrg;
	}
	public void setR22_tot_mar_ris_chrg(BigDecimal r22_tot_mar_ris_chrg) {
		this.r22_tot_mar_ris_chrg = r22_tot_mar_ris_chrg;
	}
	public String getR23_market() {
		return r23_market;
	}
	public void setR23_market(String r23_market) {
		this.r23_market = r23_market;
	}
	public BigDecimal getR23_gpfsr_nom_amt() {
		return r23_gpfsr_nom_amt;
	}
	public void setR23_gpfsr_nom_amt(BigDecimal r23_gpfsr_nom_amt) {
		this.r23_gpfsr_nom_amt = r23_gpfsr_nom_amt;
	}
	public BigDecimal getR23_gpfsr_pos_att8_per_spe_ris() {
		return r23_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att8_per_spe_ris(BigDecimal r23_gpfsr_pos_att8_per_spe_ris) {
		this.r23_gpfsr_pos_att8_per_spe_ris = r23_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg() {
		return r23_gpfsr_chrg;
	}
	public void setR23_gpfsr_chrg(BigDecimal r23_gpfsr_chrg) {
		this.r23_gpfsr_chrg = r23_gpfsr_chrg;
	}
	public BigDecimal getR23_gpfsr_nom_amt1() {
		return r23_gpfsr_nom_amt1;
	}
	public void setR23_gpfsr_nom_amt1(BigDecimal r23_gpfsr_nom_amt1) {
		this.r23_gpfsr_nom_amt1 = r23_gpfsr_nom_amt1;
	}
	public BigDecimal getR23_gpfsr_pos_att4_per_spe_ris() {
		return r23_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att4_per_spe_ris(BigDecimal r23_gpfsr_pos_att4_per_spe_ris) {
		this.r23_gpfsr_pos_att4_per_spe_ris = r23_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg1() {
		return r23_gpfsr_chrg1;
	}
	public void setR23_gpfsr_chrg1(BigDecimal r23_gpfsr_chrg1) {
		this.r23_gpfsr_chrg1 = r23_gpfsr_chrg1;
	}
	public BigDecimal getR23_gpfsr_nom_amt2() {
		return r23_gpfsr_nom_amt2;
	}
	public void setR23_gpfsr_nom_amt2(BigDecimal r23_gpfsr_nom_amt2) {
		this.r23_gpfsr_nom_amt2 = r23_gpfsr_nom_amt2;
	}
	public BigDecimal getR23_gpfsr_pos_att2_per_spe_ris() {
		return r23_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att2_per_spe_ris(BigDecimal r23_gpfsr_pos_att2_per_spe_ris) {
		this.r23_gpfsr_pos_att2_per_spe_ris = r23_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg2() {
		return r23_gpfsr_chrg2;
	}
	public void setR23_gpfsr_chrg2(BigDecimal r23_gpfsr_chrg2) {
		this.r23_gpfsr_chrg2 = r23_gpfsr_chrg2;
	}
	public BigDecimal getR23_tot_spe_ris_chrg() {
		return r23_tot_spe_ris_chrg;
	}
	public void setR23_tot_spe_ris_chrg(BigDecimal r23_tot_spe_ris_chrg) {
		this.r23_tot_spe_ris_chrg = r23_tot_spe_ris_chrg;
	}
	public BigDecimal getR23_net_pos_gen_mar_ris() {
		return r23_net_pos_gen_mar_ris;
	}
	public void setR23_net_pos_gen_mar_ris(BigDecimal r23_net_pos_gen_mar_ris) {
		this.r23_net_pos_gen_mar_ris = r23_net_pos_gen_mar_ris;
	}
	public BigDecimal getR23_gen_mar_ris_chrg_8per() {
		return r23_gen_mar_ris_chrg_8per;
	}
	public void setR23_gen_mar_ris_chrg_8per(BigDecimal r23_gen_mar_ris_chrg_8per) {
		this.r23_gen_mar_ris_chrg_8per = r23_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR23_2per_gen_mar_ris_chrg_div_port() {
		return r23_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR23_2per_gen_mar_ris_chrg_div_port(BigDecimal r23_2per_gen_mar_ris_chrg_div_port) {
		this.r23_2per_gen_mar_ris_chrg_div_port = r23_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR23_tot_gen_mar_risk_chrg() {
		return r23_tot_gen_mar_risk_chrg;
	}
	public void setR23_tot_gen_mar_risk_chrg(BigDecimal r23_tot_gen_mar_risk_chrg) {
		this.r23_tot_gen_mar_risk_chrg = r23_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR23_tot_mar_ris_chrg() {
		return r23_tot_mar_ris_chrg;
	}
	public void setR23_tot_mar_ris_chrg(BigDecimal r23_tot_mar_ris_chrg) {
		this.r23_tot_mar_ris_chrg = r23_tot_mar_ris_chrg;
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
// MODEL AND VIEW METHOD summary M_EPR
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
public ModelAndView getM_EPRView(
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

    System.out.println("M_EPR View Called");
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

                List<M_EPR_Archival_Detail_Entity> T1Master =
                        getDetaildatabydateListarchival(dt, version);

                System.out.println("Archival Detail Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
                mv.addObject("displaymode", "detail");
            }

            // RESUB DETAIL
            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

                List<M_EPR_RESUB_Detail_Entity> T1Master =
                        getResubDetaildatabydateList(dt, version);

                System.out.println("Resub Detail Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
                mv.addObject("displaymode", "detail");
            }

            // NORMAL DETAIL
            else {

                List<M_EPR_Detail_Entity> T1Master =
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

                List<M_EPR_Archival_Summary_Entity> T1Master =
                        getDataByDateListArchival(dt, version);

                System.out.println("Archival Summary Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
            }

            // RESUB SUMMARY
            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

                List<M_EPR_RESUB_Summary_Entity> T1Master =
                        getResubSummarydatabydateListarchival(dt, version);

                System.out.println("Resub Summary Size = " + T1Master.size());

                mv.addObject("reportsummary", T1Master);
            }

            // NORMAL SUMMARY
            else {

                List<M_EPR_Summary_Entity> T1Master =
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

    mv.setViewName("BRRS/M_EPR");

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
public List<Object[]> getM_EPRArchival() {

    List<Object[]> archivalList = new ArrayList<>();

    try {

        List<M_EPR_Archival_Summary_Entity> repoData =
                getarchivaldatabydateListWithVersion();

        if (repoData != null && !repoData.isEmpty()) {

            for (M_EPR_Archival_Summary_Entity entity : repoData) {

                Object[] row = new Object[] {
                        entity.getReport_date(),
                        entity.getReport_version(),
                        entity.getReportResubDate()
                };

                archivalList.add(row);
            }

            System.out.println("Fetched " + archivalList.size() + " archival records");

            M_EPR_Archival_Summary_Entity first = repoData.get(0);

            System.out.println("Latest archival version: "
                    + first.getReport_version());

        } else {

            System.out.println("No archival data found.");
        }

    } catch (Exception e) {

        System.err.println("Error fetching M_EPR Archival data: "
                + e.getMessage());

        e.printStackTrace();
    }

    return archivalList;
}
//=====================================================
// UPDATE REPORT
//=====================================================

@Transactional
public void updateReport(M_EPR_Summary_Entity updatedEntity) {

    System.out.println("Came to M_EPR Update");
    System.out.println("Report Date: " + updatedEntity.getReport_date());

    // ==========================================
    // FETCH EXISTING RECORD FOR AUDIT
    // ==========================================

    M_EPR_Summary_Entity existingSummary =
            findByReportDate(updatedEntity.getReport_date());

    if (existingSummary == null) {
        throw new RuntimeException(
                "Record not found for REPORT_DATE : "
                        + updatedEntity.getReport_date());
    }

    // ==========================================
    // OLD COPY FOR AUDIT
    // ==========================================

    M_EPR_Summary_Entity oldcopy =
            new M_EPR_Summary_Entity();

    BeanUtils.copyProperties(existingSummary, oldcopy);

    String[] fields = {
            "market",
            "gpfsr_nom_amt",
            "gpfsr_pos_att8_per_spe_ris",
            "gpfsr_chrg",
            "gpfsr_nom_amt1",
            "gpfsr_pos_att4_per_spe_ris",
            "gpfsr_chrg1",
            "gpfsr_nom_amt2",
            "gpfsr_pos_att2_per_spe_ris",
            "gpfsr_chrg2",
            "tot_spe_ris_chrg",
            "net_pos_gen_mar_ris",
            "gen_mar_ris_chrg_8per",
            "2per_gen_mar_ris_chrg_div_port",
            "tot_gen_mar_risk_chrg",
            "tot_mar_ris_chrg"
    };

    try {

        for (int i = 11; i <= 23; i++) {

            for (String field : fields) {

                String getterName = "getR" + i + "_" + field;
                String setterName = "setR" + i + "_" + field;
                String columnName = "R" + i + "_" + field;

                try {

                    Method getter =
                            M_EPR_Summary_Entity.class.getMethod(getterName);

                    Object value = getter.invoke(updatedEntity);

                    if (value == null) {
                        continue;
                    }

                    // ==========================================
                    // UPDATE EXISTING OBJECT FOR AUDIT
                    // ==========================================

                    Method setter =
                            M_EPR_Summary_Entity.class.getMethod(
                                    setterName,
                                    getter.getReturnType());

                    setter.invoke(existingSummary, value);

                    // ==========================================
                    // UPDATE SUMMARY TABLE
                    // ==========================================

                    String summarySql =
                            "UPDATE BRRS_M_EPR_SUMMARYTABLE " +
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
                            "UPDATE BRRS_M_EPR_DETAILTABLE " +
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
                    "M EPR Summary Screen",
                    "BRRS_M_EPR_SUMMARYTABLE"
            );
        }

        System.out.println(
                "M_EPR Summary & Detail Update Completed");

    } catch (Exception e) {

        throw new RuntimeException(
                "Error while updating M_EPR fields", e);
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

public List<Object[]> getM_EPRResub() {

    List<Object[]> resubList = new ArrayList<>();

    try {

        List<M_EPR_Archival_Summary_Entity> repoData =
                getarchivaldatabydateListWithVersion();

        if (repoData != null && !repoData.isEmpty()) {

            for (M_EPR_Archival_Summary_Entity entity : repoData) {

                Object[] row = new Object[] {
                        entity.getReport_date(),
                        entity.getReport_version(),
                        entity.getReportResubDate()
                };

                resubList.add(row);
            }

            System.out.println("Fetched " + resubList.size() + " resub records");

            M_EPR_Archival_Summary_Entity first = repoData.get(0);

            System.out.println("Latest resub version : "
                    + first.getReport_version());

        } else {

            System.out.println("No resub data found.");
        }

    } catch (Exception e) {

        System.err.println("Error fetching M_EPR Resub data : "
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
        M_EPR_RESUB_Summary_Entity updatedEntity) {

    System.out.println("Came to M_EPR Resub Update");

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

        M_EPR_RESUB_Summary_Entity resubSummary =
                new M_EPR_RESUB_Summary_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                resubSummary);

        resubSummary.setReport_date(reportDate);
        resubSummary.setReport_version(newVersion);
        resubSummary.setReportResubDate(now);

        // ====================================================
        // RESUB DETAIL
        // ====================================================

        M_EPR_RESUB_Detail_Entity resubDetail =
                new M_EPR_RESUB_Detail_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                resubDetail);

        resubDetail.setReport_date(reportDate);
        resubDetail.setReport_version(newVersion);
        resubDetail.setReportResubDate(now);

        // ====================================================
        // ARCHIVAL SUMMARY
        // ====================================================

        M_EPR_Archival_Summary_Entity archivalSummary =
                new M_EPR_Archival_Summary_Entity();

        BeanUtils.copyProperties(
                updatedEntity,
                archivalSummary);

        archivalSummary.setReport_date(reportDate);
        archivalSummary.setReport_version(newVersion);
        archivalSummary.setReportResubDate(now);

        // ====================================================
        // ARCHIVAL DETAIL
        // ====================================================

        M_EPR_Archival_Detail_Entity archivalDetail =
                new M_EPR_Archival_Detail_Entity();

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
                "M_EPR Resub Version Created Successfully : "
                        + newVersion);

    } catch (Exception e) {

        e.printStackTrace();

        throw new RuntimeException(
                "Error while creating M_EPR Resub Version",
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

private void insertResubSummary(M_EPR_RESUB_Summary_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_M_EPR_RESUB_SUMMARYTABLE (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

        for (int i = 11; i <= 23; i++) {

            columns
                .append("r").append(i).append("_market,")
                .append("r").append(i).append("_gpfsr_nom_amt,")
                .append("r").append(i).append("_gpfsr_pos_att8_per_spe_ris,")
                .append("r").append(i).append("_gpfsr_chrg,")
                .append("r").append(i).append("_gpfsr_nom_amt1,")
                .append("r").append(i).append("_gpfsr_pos_att4_per_spe_ris,")
                .append("r").append(i).append("_gpfsr_chrg1,")
                .append("r").append(i).append("_gpfsr_nom_amt2,")
                .append("r").append(i).append("_gpfsr_pos_att2_per_spe_ris,")
                .append("r").append(i).append("_gpfsr_chrg2,")
                .append("r").append(i).append("_tot_spe_ris_chrg,")
                .append("r").append(i).append("_net_pos_gen_mar_ris,")
                .append("r").append(i).append("_gen_mar_ris_chrg_8per,")
                .append("r").append(i).append("_2per_gen_mar_ris_chrg_div_port,")
                .append("r").append(i).append("_tot_gen_mar_risk_chrg,")
                .append("r").append(i).append("_tot_mar_ris_chrg,");

            for (int j = 1; j <= 16; j++) {
                values.append("?,");
            }

            params.add(getValue(entity, "getR" + i + "_market"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_nom_amt"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_pos_att8_per_spe_ris"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_chrg"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_nom_amt1"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_pos_att4_per_spe_ris"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_chrg1"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_nom_amt2"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_pos_att2_per_spe_ris"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_chrg2"));
            params.add(getValue(entity, "getR" + i + "_tot_spe_ris_chrg"));
            params.add(getValue(entity, "getR" + i + "_net_pos_gen_mar_ris"));
            params.add(getValue(entity, "getR" + i + "_gen_mar_ris_chrg_8per"));
            params.add(getValue(entity, "getR" + i + "_2per_gen_mar_ris_chrg_div_port"));
            params.add(getValue(entity, "getR" + i + "_tot_gen_mar_risk_chrg"));
            params.add(getValue(entity, "getR" + i + "_tot_mar_ris_chrg"));
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
                "Error inserting M_EPR RESUB SUMMARY",
                e);
    }
}

private void insertResubDetail(M_EPR_RESUB_Detail_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_M_EPR_RESUB_DETAILTABLE (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

        for (int i = 11; i <= 23; i++) {

            columns
                .append("r").append(i).append("_market,")
                .append("r").append(i).append("_gpfsr_nom_amt,")
                .append("r").append(i).append("_gpfsr_pos_att8_per_spe_ris,")
                .append("r").append(i).append("_gpfsr_chrg,")
                .append("r").append(i).append("_gpfsr_nom_amt1,")
                .append("r").append(i).append("_gpfsr_pos_att4_per_spe_ris,")
                .append("r").append(i).append("_gpfsr_chrg1,")
                .append("r").append(i).append("_gpfsr_nom_amt2,")
                .append("r").append(i).append("_gpfsr_pos_att2_per_spe_ris,")
                .append("r").append(i).append("_gpfsr_chrg2,")
                .append("r").append(i).append("_tot_spe_ris_chrg,")
                .append("r").append(i).append("_net_pos_gen_mar_ris,")
                .append("r").append(i).append("_gen_mar_ris_chrg_8per,")
                .append("r").append(i).append("_2per_gen_mar_ris_chrg_div_port,")
                .append("r").append(i).append("_tot_gen_mar_risk_chrg,")
                .append("r").append(i).append("_tot_mar_ris_chrg,");

            for (int j = 1; j <= 16; j++) {
                values.append("?,");
            }

            params.add(getValue(entity, "getR" + i + "_market"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_nom_amt"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_pos_att8_per_spe_ris"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_chrg"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_nom_amt1"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_pos_att4_per_spe_ris"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_chrg1"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_nom_amt2"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_pos_att2_per_spe_ris"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_chrg2"));
            params.add(getValue(entity, "getR" + i + "_tot_spe_ris_chrg"));
            params.add(getValue(entity, "getR" + i + "_net_pos_gen_mar_ris"));
            params.add(getValue(entity, "getR" + i + "_gen_mar_ris_chrg_8per"));
            params.add(getValue(entity, "getR" + i + "_2per_gen_mar_ris_chrg_div_port"));
            params.add(getValue(entity, "getR" + i + "_tot_gen_mar_risk_chrg"));
            params.add(getValue(entity, "getR" + i + "_tot_mar_ris_chrg"));
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
                "Error inserting M_EPR RESUB DETAIL",
                e);
    }
}

private void insertArchivalSummary(M_EPR_Archival_Summary_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_M_EPR_ARCHIVALTABLE_SUMMARY (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

        for (int i = 11; i <= 23; i++) {

            columns
                .append("r").append(i).append("_market,")
                .append("r").append(i).append("_gpfsr_nom_amt,")
                .append("r").append(i).append("_gpfsr_pos_att8_per_spe_ris,")
                .append("r").append(i).append("_gpfsr_chrg,")
                .append("r").append(i).append("_gpfsr_nom_amt1,")
                .append("r").append(i).append("_gpfsr_pos_att4_per_spe_ris,")
                .append("r").append(i).append("_gpfsr_chrg1,")
                .append("r").append(i).append("_gpfsr_nom_amt2,")
                .append("r").append(i).append("_gpfsr_pos_att2_per_spe_ris,")
                .append("r").append(i).append("_gpfsr_chrg2,")
                .append("r").append(i).append("_tot_spe_ris_chrg,")
                .append("r").append(i).append("_net_pos_gen_mar_ris,")
                .append("r").append(i).append("_gen_mar_ris_chrg_8per,")
                .append("r").append(i).append("_2per_gen_mar_ris_chrg_div_port,")
                .append("r").append(i).append("_tot_gen_mar_risk_chrg,")
                .append("r").append(i).append("_tot_mar_ris_chrg,");

            for (int j = 1; j <= 16; j++) {
                values.append("?,");
            }

            params.add(getValue(entity, "getR" + i + "_market"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_nom_amt"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_pos_att8_per_spe_ris"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_chrg"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_nom_amt1"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_pos_att4_per_spe_ris"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_chrg1"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_nom_amt2"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_pos_att2_per_spe_ris"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_chrg2"));
            params.add(getValue(entity, "getR" + i + "_tot_spe_ris_chrg"));
            params.add(getValue(entity, "getR" + i + "_net_pos_gen_mar_ris"));
            params.add(getValue(entity, "getR" + i + "_gen_mar_ris_chrg_8per"));
            params.add(getValue(entity, "getR" + i + "_2per_gen_mar_ris_chrg_div_port"));
            params.add(getValue(entity, "getR" + i + "_tot_gen_mar_risk_chrg"));
            params.add(getValue(entity, "getR" + i + "_tot_mar_ris_chrg"));
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
                "Error inserting M_EPR ARCHIVAL SUMMARY",
                e);
    }
}

private void insertArchivalDetail(M_EPR_Archival_Detail_Entity entity) {

    try {

        StringBuilder columns = new StringBuilder(
                "INSERT INTO BRRS_M_EPR_ARCHIVALTABLE_DETAIL (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

        StringBuilder values = new StringBuilder(
                " VALUES (?,?,?,");

        List<Object> params = new ArrayList<>();

        params.add(entity.getReport_date());
        params.add(entity.getReport_version());
        params.add(entity.getReportResubDate());

        for (int i = 11; i <= 23; i++) {

            columns
                .append("r").append(i).append("_market,")
                .append("r").append(i).append("_gpfsr_nom_amt,")
                .append("r").append(i).append("_gpfsr_pos_att8_per_spe_ris,")
                .append("r").append(i).append("_gpfsr_chrg,")
                .append("r").append(i).append("_gpfsr_nom_amt1,")
                .append("r").append(i).append("_gpfsr_pos_att4_per_spe_ris,")
                .append("r").append(i).append("_gpfsr_chrg1,")
                .append("r").append(i).append("_gpfsr_nom_amt2,")
                .append("r").append(i).append("_gpfsr_pos_att2_per_spe_ris,")
                .append("r").append(i).append("_gpfsr_chrg2,")
                .append("r").append(i).append("_tot_spe_ris_chrg,")
                .append("r").append(i).append("_net_pos_gen_mar_ris,")
                .append("r").append(i).append("_gen_mar_ris_chrg_8per,")
                .append("r").append(i).append("_2per_gen_mar_ris_chrg_div_port,")
                .append("r").append(i).append("_tot_gen_mar_risk_chrg,")
                .append("r").append(i).append("_tot_mar_ris_chrg,");

            for (int j = 1; j <= 16; j++) {
                values.append("?,");
            }

            params.add(getValue(entity, "getR" + i + "_market"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_nom_amt"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_pos_att8_per_spe_ris"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_chrg"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_nom_amt1"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_pos_att4_per_spe_ris"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_chrg1"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_nom_amt2"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_pos_att2_per_spe_ris"));
            params.add(getValue(entity, "getR" + i + "_gpfsr_chrg2"));
            params.add(getValue(entity, "getR" + i + "_tot_spe_ris_chrg"));
            params.add(getValue(entity, "getR" + i + "_net_pos_gen_mar_ris"));
            params.add(getValue(entity, "getR" + i + "_gen_mar_ris_chrg_8per"));
            params.add(getValue(entity, "getR" + i + "_2per_gen_mar_ris_chrg_div_port"));
            params.add(getValue(entity, "getR" + i + "_tot_gen_mar_risk_chrg"));
            params.add(getValue(entity, "getR" + i + "_tot_mar_ris_chrg"));
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
                "Error inserting M_EPR ARCHIVAL DETAIL",
                e);
    }
}
	
//=====================================================
// Summary EXCEL  FORMAT
//=====================================================

public byte[] getM_EPRExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
						return getExcelM_EPRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_EPRResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else {

					if ("email".equalsIgnoreCase(format) && version == null) {
						logger.info("Got format as Email");
						logger.info("Service: Generating Email report for version {}", version);
						return BRRS_M_EPREmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} else {

						// Fetch data

						List<M_EPR_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

						if (dataList.isEmpty()) {
							logger.warn("Service: No data found for BRRS_M_EPR report. Returning empty result.");
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

							int startRow = 5;

							if (!dataList.isEmpty()) {
								for (int i = 0; i < dataList.size(); i++) {
									M_EPR_Summary_Entity record = dataList.get(i);
									System.out.println("rownumber=" + startRow + i);
									Row row = sheet.getRow(startRow + i);
									if (row == null) {
										row = sheet.createRow(startRow + i);
									}
									
							//row6
								// Column B
								Cell cellBdate = row.createCell(2);
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
						// Column B
						Cell cellB = row.createCell(1);
						if (record.getR11_market() != null) {
							cellB.setCellValue(record.getR11_market().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row11
						// Column C
						Cell cellC = row.createCell(2);
						if (record.getR11_gpfsr_nom_amt() != null) {
							cellC.setCellValue(record.getR11_gpfsr_nom_amt().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// row11
						// Column D
						Cell cellD = row.createCell(3);
						if (record.getR11_gpfsr_pos_att8_per_spe_ris() != null) {
							cellD.setCellValue(record.getR11_gpfsr_pos_att8_per_spe_ris().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// row11
						// Column F
						Cell cellF = row.createCell(5);
						if (record.getR11_gpfsr_nom_amt1() != null) {
							cellF.setCellValue(record.getR11_gpfsr_nom_amt1().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row11
						// Column G
						Cell cellG = row.createCell(6);
						if (record.getR11_gpfsr_pos_att4_per_spe_ris() != null) {
							cellG.setCellValue(record.getR11_gpfsr_pos_att4_per_spe_ris().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row11
						// Column I
						Cell cellI = row.createCell(8);
						if (record.getR11_gpfsr_nom_amt2() != null) {
							cellI.setCellValue(record.getR11_gpfsr_nom_amt2().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row11
						// Column J
						Cell cellJ = row.createCell(9);
						if (record.getR11_gpfsr_pos_att2_per_spe_ris() != null) {
							cellJ.setCellValue(record.getR11_gpfsr_pos_att2_per_spe_ris().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// row11
						// Column M
						Cell cellM = row.createCell(12);
						if (record.getR11_net_pos_gen_mar_ris() != null) {
							cellM.setCellValue(record.getR11_net_pos_gen_mar_ris().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}
						
						// row12
						row = sheet.getRow(11);
						
						// row12
						// Column B  ->Market
						 cellB = row.createCell(1);
						if (record.getR12_market() != null) {
							cellB.setCellValue(record.getR12_market().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}
						
						
						// row12
						// Column C -->Nominal Amount
						 cellC = row.createCell(2);
						if (record.getR12_gpfsr_nom_amt() != null) {
							cellC.setCellValue(record.getR12_gpfsr_nom_amt().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// row12
						// Column D -->Positions Attracting 8 Percent Specific Risk
						 cellD = row.createCell(3);
						if (record.getR12_gpfsr_pos_att8_per_spe_ris() != null) {
							cellD.setCellValue(record.getR12_gpfsr_pos_att8_per_spe_ris().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// row12
						// Column F -->Nominal Amount
						 cellF = row.createCell(5);
						if (record.getR12_gpfsr_nom_amt1() != null) {
							cellF.setCellValue(record.getR12_gpfsr_nom_amt1().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row12
						// Column G -->Positions Attracting 4 Percent Specific Risk
						 cellG = row.createCell(6);
						if (record.getR12_gpfsr_pos_att4_per_spe_ris() != null) {
							cellG.setCellValue(record.getR12_gpfsr_pos_att4_per_spe_ris().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row12
						// Column I -->Nominal Amount
						 cellI = row.createCell(8);
						if (record.getR12_gpfsr_nom_amt2() != null) {
							cellI.setCellValue(record.getR12_gpfsr_nom_amt2().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row12
						// Column J -->Positions Attracting 2 Percent Specific Risk
					       cellJ = row.createCell(9);
						if (record.getR12_gpfsr_pos_att2_per_spe_ris() != null) {
							cellJ.setCellValue(record.getR12_gpfsr_pos_att2_per_spe_ris().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// row12
						// Column M -->Net Positions for General Market Risk
						 cellM = row.createCell(12);
						if (record.getR12_net_pos_gen_mar_ris() != null) {
							cellM.setCellValue(record.getR12_net_pos_gen_mar_ris().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}
						
						
						// ---- row13 ----
						row = sheet.getRow(12);

						// row13
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR13_market() != null) {
						    cellB.setCellValue(record.getR13_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row13
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR13_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR13_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row13
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR13_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR13_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row13
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR13_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR13_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row13
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR13_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR13_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row13
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR13_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR13_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row13
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR13_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR13_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row13
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR13_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR13_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row14 ----
						row = sheet.getRow(13);

						// row14
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR14_market() != null) {
						    cellB.setCellValue(record.getR14_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row14
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR14_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR14_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row14
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR14_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR14_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row14
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR14_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR14_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row14
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR14_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR14_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row14
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR14_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR14_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row14
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR14_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR14_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row14
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR14_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR14_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row15 ----
						row = sheet.getRow(14);

						// row15
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR15_market() != null) {
						    cellB.setCellValue(record.getR15_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row15
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR15_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR15_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row15
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR15_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR15_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row15
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR15_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR15_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row15
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR15_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR15_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row15
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR15_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR15_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row15
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR15_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR15_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row15
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR15_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR15_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row16 ----
						row = sheet.getRow(15);

						// row16
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR16_market() != null) {
						    cellB.setCellValue(record.getR16_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row16
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR16_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR16_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row16
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR16_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR16_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row16
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR16_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR16_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row16
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR16_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR16_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row16
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR16_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR16_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row16
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR16_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR16_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row16
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR16_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR16_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row17 ----
						row = sheet.getRow(16);

						// row17
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR17_market() != null) {
						    cellB.setCellValue(record.getR17_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row17
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR17_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR17_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row17
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR17_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR17_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row17
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR17_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR17_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row17
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR17_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR17_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row17
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR17_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR17_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row17
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR17_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR17_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row17
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR17_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR17_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row18 ----
						row = sheet.getRow(17);

						// row18
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR18_market() != null) {
						    cellB.setCellValue(record.getR18_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row18
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR18_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR18_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row18
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR18_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR18_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row18
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR18_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR18_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row18
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR18_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR18_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row18
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR18_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR18_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row18
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR18_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR18_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row18
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR18_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR18_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row19 ----
						row = sheet.getRow(18);

						// row19
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR19_market() != null) {
						    cellB.setCellValue(record.getR19_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row19
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR19_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR19_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row19
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR19_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR19_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row19
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR19_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR19_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row19
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR19_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR19_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row19
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR19_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR19_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row19
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR19_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR19_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row19
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR19_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR19_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row20 ----
						row = sheet.getRow(19);

						// row20
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR20_market() != null) {
						    cellB.setCellValue(record.getR20_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row20
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR20_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR20_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row20
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR20_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR20_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row20
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR20_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR20_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row20
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR20_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR20_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row20
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR20_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR20_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row20
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR20_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR20_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row20
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR20_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR20_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row21 ----
						row = sheet.getRow(20);

						// row21
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR21_market() != null) {
						    cellB.setCellValue(record.getR21_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row21
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR21_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR21_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row21
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR21_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR21_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row21
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR21_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR21_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row21
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR21_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR21_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row21
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR21_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR21_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row21
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR21_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR21_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row21
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR21_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR21_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row22 ----
						row = sheet.getRow(21);

						// row22
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR22_market() != null) {
						    cellB.setCellValue(record.getR22_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row22
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR22_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR22_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row22
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR22_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR22_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row22
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR22_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR22_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row22
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR22_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR22_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row22
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR22_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR22_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row22
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR22_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR22_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row22
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR22_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR22_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row23 ----
						row = sheet.getRow(22);

						
						// row23
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR23_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR23_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row23
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR23_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR23_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row23
						// Column E -->Charge
					Cell cellE = row.createCell(4);
						if (record.getR23_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR23_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row23
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR23_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR23_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row23
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR23_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR23_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row23
						// Column H -->Charge
					Cell cellH = row.createCell(7);
						if (record.getR23_gpfsr_chrg1() != null) {
						    cellH.setCellValue(record.getR23_gpfsr_chrg1().doubleValue());
						    cellH.setCellStyle(numberStyle);
						} else {
						    cellH.setCellValue("");
						    cellH.setCellStyle(textStyle);
						}

						// row23
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR23_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR23_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row23
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR23_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR23_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row23
						// Column K -->Charge
					Cell cellK = row.createCell(10);
						if (record.getR23_gpfsr_chrg2() != null) {
						    cellK.setCellValue(record.getR23_gpfsr_chrg2().doubleValue());
						    cellK.setCellStyle(numberStyle);
						} else {
						    cellK.setCellValue("");
						    cellK.setCellStyle(textStyle);
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
								auditService.createBusinessAudit(userid, "DOWNLOAD", "M_EPR SUMMARY", null, "BRRS_M_EPR_SUMMARYTABLE");
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
			public byte[] BRRS_M_EPREmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Email Excel generation process in memory.");
				
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_M_EPRARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_EPREmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 
				else {
				List<M_EPR_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_EPR report. Returning empty result.");
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

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_EPR_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
							
								//row6
						// Column B
						Cell cellBdate = row.createCell(2);
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
						// Column B
						Cell cellB = row.createCell(1);
						if (record.getR11_market() != null) {
							cellB.setCellValue(record.getR11_market().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row11
						// Column C
						Cell cellC = row.createCell(2);
						if (record.getR11_gpfsr_nom_amt() != null) {
							cellC.setCellValue(record.getR11_gpfsr_nom_amt().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// row11
						// Column D
						Cell cellD = row.createCell(3);
						if (record.getR11_gpfsr_pos_att8_per_spe_ris() != null) {
							cellD.setCellValue(record.getR11_gpfsr_pos_att8_per_spe_ris().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}
						
						// row11
						// Column E -->Charge
						Cell	cellE = row.createCell(4);
						if (record.getR11_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR11_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row11
						// Column F
						Cell cellF = row.createCell(5);
						if (record.getR11_gpfsr_nom_amt1() != null) {
							cellF.setCellValue(record.getR11_gpfsr_nom_amt1().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row11
						// Column G
						Cell cellG = row.createCell(6);
						if (record.getR11_gpfsr_pos_att4_per_spe_ris() != null) {
							cellG.setCellValue(record.getR11_gpfsr_pos_att4_per_spe_ris().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}
						
						// row11
						// Column H
						Cell cellH = row.createCell(7);
						if (record.getR11_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR11_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row11
						// Column I
						Cell cellI = row.createCell(8);
						if (record.getR11_gpfsr_nom_amt2() != null) {
							cellI.setCellValue(record.getR11_gpfsr_nom_amt2().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row11
						// Column J
						Cell cellJ = row.createCell(9);
						if (record.getR11_gpfsr_pos_att2_per_spe_ris() != null) {
							cellJ.setCellValue(record.getR11_gpfsr_pos_att2_per_spe_ris().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}
						
						// row11
						// Column K ---------Charge

						Cell cellK = row.createCell(10);
						if (record.getR11_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR11_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row11
						// Column M
						Cell cellM = row.createCell(12);
						if (record.getR11_net_pos_gen_mar_ris() != null) {
							cellM.setCellValue(record.getR11_net_pos_gen_mar_ris().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}
						
							// row11
						// Column N -->General Market Risk Change at 8%

						Cell cellN = row.createCell(13);
						if (record.getR11_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR11_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						Cell 	cellO = row.createCell(14);
						if (record.getR11_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR11_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						Cell cellP = row.createCell(15);
						if (record.getR11_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR11_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						
						// row12
						row = sheet.getRow(11);
						
						// row12
						// Column B  ->Market
						 cellB = row.createCell(1);
						if (record.getR12_market() != null) {
							cellB.setCellValue(record.getR12_market().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}
						
						
						// row12
						// Column C -->Nominal Amount
						 cellC = row.createCell(2);
						if (record.getR12_gpfsr_nom_amt() != null) {
							cellC.setCellValue(record.getR12_gpfsr_nom_amt().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// row12
						// Column D -->Positions Attracting 8 Percent Specific Risk
						 cellD = row.createCell(3);
						if (record.getR12_gpfsr_pos_att8_per_spe_ris() != null) {
							cellD.setCellValue(record.getR12_gpfsr_pos_att8_per_spe_ris().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}
						
						// row12
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR12_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR12_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row12
						// Column F -->Nominal Amount
						 cellF = row.createCell(5);
						if (record.getR12_gpfsr_nom_amt1() != null) {
							cellF.setCellValue(record.getR12_gpfsr_nom_amt1().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row12
						// Column G -->Positions Attracting 4 Percent Specific Risk
						 cellG = row.createCell(6);
						if (record.getR12_gpfsr_pos_att4_per_spe_ris() != null) {
							cellG.setCellValue(record.getR12_gpfsr_pos_att4_per_spe_ris().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}
						
							// row12
						// Column H
					 cellH = row.createCell(7);
						if (record.getR12_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR12_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}


						// row12
						// Column I -->Nominal Amount
						 cellI = row.createCell(8);
						if (record.getR12_gpfsr_nom_amt2() != null) {
							cellI.setCellValue(record.getR12_gpfsr_nom_amt2().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row12
						// Column J -->Positions Attracting 2 Percent Specific Risk
					       cellJ = row.createCell(9);
						if (record.getR12_gpfsr_pos_att2_per_spe_ris() != null) {
							cellJ.setCellValue(record.getR12_gpfsr_pos_att2_per_spe_ris().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}
						
						// row12
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR12_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR12_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row12
						// Column M -->Net Positions for General Market Risk
						 cellM = row.createCell(12);
						if (record.getR12_net_pos_gen_mar_ris() != null) {
							cellM.setCellValue(record.getR12_net_pos_gen_mar_ris().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}
						
							// row12
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR12_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR12_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR12_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR12_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR12_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR12_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						
						
						// ---- row13 ----
						row = sheet.getRow(12);

						// row13
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR13_market() != null) {
						    cellB.setCellValue(record.getR13_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row13
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR13_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR13_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row13
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR13_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR13_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row13
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR13_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR13_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row13
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR13_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR13_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row13
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR13_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR13_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row13
						// Column H
						 cellH = row.createCell(7);
						if (record.getR13_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR13_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row13
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR13_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR13_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row13
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR13_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR13_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row13
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR13_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR13_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row13
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR13_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR13_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
							// row13
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR13_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR13_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR13_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR13_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR13_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR13_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}

						// ---- row14 ----
						row = sheet.getRow(13);

						// row14
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR14_market() != null) {
						    cellB.setCellValue(record.getR14_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row14
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR14_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR14_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row14
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR14_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR14_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row14
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR14_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR14_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row14
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR14_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR14_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row14
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR14_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR14_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row14
						// Column H
						 cellH = row.createCell(7);
						if (record.getR14_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR14_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row14
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR14_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR14_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row14
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR14_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR14_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row14
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR14_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR14_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row14
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR14_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR14_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
							// row14
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR14_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR14_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR14_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR14_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR14_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR14_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}

						// ---- row15 ----
						row = sheet.getRow(14);

						// row15
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR15_market() != null) {
						    cellB.setCellValue(record.getR15_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row15
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR15_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR15_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row15
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR15_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR15_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						
						// row15
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR15_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR15_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// Column F --> Nominal Amount
						cellF = row.createCell(5);
						if (record.getR15_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR15_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// Column G --> Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR15_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR15_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// Column H --> Charge
						cellH = row.createCell(7);
						if (record.getR15_gpfsr_chrg1() != null) {
						    cellH.setCellValue(record.getR15_gpfsr_chrg1().doubleValue());
						    cellH.setCellStyle(numberStyle);
						} else {
						    cellH.setCellValue("");
						    cellH.setCellStyle(textStyle);
						}

						// Column I --> Nominal Amount
						cellI = row.createCell(8);
						if (record.getR15_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR15_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// Column J --> Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR15_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR15_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// Column K --> Charge
						cellK = row.createCell(10);
						if (record.getR15_gpfsr_chrg2() != null) {
						    cellK.setCellValue(record.getR15_gpfsr_chrg2().doubleValue());
						    cellK.setCellStyle(numberStyle);
						} else {
						    cellK.setCellValue("");
						    cellK.setCellStyle(textStyle);
						}
						
						
						// row15
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR15_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR15_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						
							// row15
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR15_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR15_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR15_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR15_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR15_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR15_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}

						// ---- row16 ----
						row = sheet.getRow(15);

						// row16
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR16_market() != null) {
						    cellB.setCellValue(record.getR16_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row16
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR16_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR16_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row16
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR16_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR16_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						
						// row16
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR16_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR16_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						// Column F --> Nominal Amount
						cellF = row.createCell(5);
						if (record.getR16_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR16_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// Column G --> Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR16_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR16_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// Column H --> Charge
						cellH = row.createCell(7);
						if (record.getR16_gpfsr_chrg1() != null) {
						    cellH.setCellValue(record.getR16_gpfsr_chrg1().doubleValue());
						    cellH.setCellStyle(numberStyle);
						} else {
						    cellH.setCellValue("");
						    cellH.setCellStyle(textStyle);
						}

						// Column I --> Nominal Amount
						cellI = row.createCell(8);
						if (record.getR16_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR16_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// Column J --> Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR16_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR16_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// Column K --> Charge
						cellK = row.createCell(10);
						if (record.getR16_gpfsr_chrg2() != null) {
						    cellK.setCellValue(record.getR16_gpfsr_chrg2().doubleValue());
						    cellK.setCellStyle(numberStyle);
						} else {
						    cellK.setCellValue("");
						    cellK.setCellStyle(textStyle);
						}

						// row16
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR16_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR16_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						
						// row16
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR16_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR16_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR16_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR16_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR16_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR16_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						

						// ---- row17 ----
						row = sheet.getRow(16);

						// row17
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR17_market() != null) {
						    cellB.setCellValue(record.getR17_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row17
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR17_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR17_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row17
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR17_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR17_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row17
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR17_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR17_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row17
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR17_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR17_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row17
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR17_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR17_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						// row17
						// Column H
						 cellH = row.createCell(7);
						if (record.getR17_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR17_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row17
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR17_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR17_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row17
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR17_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR17_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row17
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR17_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR17_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row17
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR17_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR17_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						
						// row17
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR17_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR17_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR17_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR17_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR17_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR17_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						// ---- row18 ----
						row = sheet.getRow(17);

						// row18
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR18_market() != null) {
						    cellB.setCellValue(record.getR18_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row18
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR18_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR18_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row18
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR18_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR18_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row18
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR18_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR18_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row18
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR18_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR18_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row18
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR18_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR18_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row18
						// Column H
						 cellH = row.createCell(7);
						if (record.getR18_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR18_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row18
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR18_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR18_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row18
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR18_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR18_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row18
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR18_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR18_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row18
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR18_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR18_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// row18
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR18_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR18_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR18_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR18_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR18_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR18_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						// ---- row19 ----
						row = sheet.getRow(18);

						// row19
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR19_market() != null) {
						    cellB.setCellValue(record.getR19_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row19
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR19_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR19_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row19
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR19_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR19_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row19
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR19_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR19_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row19
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR19_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR19_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row19
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR19_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR19_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row19
						// Column H
						 cellH = row.createCell(7);
						if (record.getR19_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR19_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row19
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR19_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR19_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row19
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR19_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR19_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row19
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR19_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR19_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row19
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR19_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR19_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						// row19
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR19_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR19_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR19_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR19_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR19_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR19_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}

						// ---- row20 ----
						row = sheet.getRow(19);

						// row20
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR20_market() != null) {
						    cellB.setCellValue(record.getR20_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row20
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR20_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR20_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row20
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR20_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR20_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row20
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR20_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR20_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row20
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR20_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR20_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row20
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR20_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR20_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row20
						// Column H
						 cellH = row.createCell(7);
						if (record.getR20_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR20_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row20
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR20_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR20_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row20
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR20_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR20_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row20
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR20_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR20_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row20
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR20_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR20_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						
						// row20
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR20_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR20_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR20_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR20_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR20_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR20_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}

						// ---- row21 ----
						row = sheet.getRow(20);

						// row21
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR21_market() != null) {
						    cellB.setCellValue(record.getR21_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row21
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR21_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR21_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row21
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR21_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR21_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row21
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR21_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR21_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row21
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR21_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR21_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row21
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR21_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR21_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row21
						// Column H
						 cellH = row.createCell(7);
						if (record.getR21_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR21_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}
						
						// row21
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR21_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR21_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row21
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR21_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR21_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row21
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR21_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR21_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row21
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR21_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR21_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						
						
								
						
						
							// row21
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR21_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR21_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR21_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR21_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR21_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR21_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						

						// ---- row22 ----
						row = sheet.getRow(21);

						// row22
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR22_market() != null) {
						    cellB.setCellValue(record.getR22_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row22
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR22_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR22_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row22
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR22_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR22_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row22
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR22_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR22_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row22
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR22_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR22_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row22
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR22_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR22_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row22
						// Column H
						 cellH = row.createCell(7);
						if (record.getR22_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR22_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row22
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR22_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR22_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row22
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR22_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR22_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row22
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR22_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR22_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row22
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR22_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR22_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						
							// row22
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR22_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR22_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR22_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR22_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}

						
						
						
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR22_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR22_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						
						
						
						
						
						

						// ---- row23 ----
						row = sheet.getRow(22);

						
						// row23
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR23_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR23_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row23
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR23_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR23_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row23
						// Column E -->Charge
					 cellE = row.createCell(4);
						if (record.getR23_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR23_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row23
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR23_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR23_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row23
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR23_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR23_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row23
						// Column H -->Charge
					 cellH = row.createCell(7);
						if (record.getR23_gpfsr_chrg1() != null) {
						    cellH.setCellValue(record.getR23_gpfsr_chrg1().doubleValue());
						    cellH.setCellStyle(numberStyle);
						} else {
						    cellH.setCellValue("");
						    cellH.setCellStyle(textStyle);
						}

						// row23
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR23_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR23_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row23
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR23_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR23_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row23
						// Column K -->Charge
					 cellK = row.createCell(10);
						if (record.getR23_gpfsr_chrg2() != null) {
						    cellK.setCellValue(record.getR23_gpfsr_chrg2().doubleValue());
						    cellK.setCellStyle(numberStyle);
						} else {
						    cellK.setCellValue("");
						    cellK.setCellStyle(textStyle);
						}
						
						
						
						// row23
						// Column L --> Total Specific Risk Charge

				Cell cellL = row.createCell(11);
						if (record.getR23_tot_spe_ris_chrg() != null) {
						    cellL.setCellValue(record.getR23_tot_spe_ris_chrg().doubleValue());
						    cellL.setCellStyle(numberStyle);
						} else {
						    cellL.setCellValue("");
						    cellL.setCellStyle(textStyle);
						}
						
						// row23
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR23_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR23_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR23_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR23_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}

						
						
						
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR23_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR23_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						
						
							// Column Q --> Total Market Risk Change

						Cell cellQ = row.createCell(16);
						if (record.getR23_tot_mar_ris_chrg() != null) {
						    cellQ.setCellValue(record.getR23_tot_mar_ris_chrg().doubleValue());
						    cellQ.setCellStyle(numberStyle);
						} else {
						    cellQ.setCellValue("");
						    cellQ.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_EPR EMAIL SUMMARY", null, "BRRS_M_EPR_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
				}
			}
			


//=====================================================
//ARCHIVAL SUMMARY EXCEL  FORMAT
//=====================================================

// Archival format excel
			public byte[] getExcelM_EPRARCHIVAL(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory in Archival.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_M_EPRARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 

				List<M_EPR_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_EPR report. Returning empty result.");
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

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_EPR_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
					//row6
						// Column B
						Cell cellBdate = row.createCell(2);
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
						// Column B
						Cell cellB = row.createCell(1);
						if (record.getR11_market() != null) {
							cellB.setCellValue(record.getR11_market().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row11
						// Column C
						Cell cellC = row.createCell(2);
						if (record.getR11_gpfsr_nom_amt() != null) {
							cellC.setCellValue(record.getR11_gpfsr_nom_amt().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// row11
						// Column D
						Cell cellD = row.createCell(3);
						if (record.getR11_gpfsr_pos_att8_per_spe_ris() != null) {
							cellD.setCellValue(record.getR11_gpfsr_pos_att8_per_spe_ris().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// row11
						// Column F
						Cell cellF = row.createCell(5);
						if (record.getR11_gpfsr_nom_amt1() != null) {
							cellF.setCellValue(record.getR11_gpfsr_nom_amt1().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row11
						// Column G
						Cell cellG = row.createCell(6);
						if (record.getR11_gpfsr_pos_att4_per_spe_ris() != null) {
							cellG.setCellValue(record.getR11_gpfsr_pos_att4_per_spe_ris().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row11
						// Column I
						Cell cellI = row.createCell(8);
						if (record.getR11_gpfsr_nom_amt2() != null) {
							cellI.setCellValue(record.getR11_gpfsr_nom_amt2().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row11
						// Column J
						Cell cellJ = row.createCell(9);
						if (record.getR11_gpfsr_pos_att2_per_spe_ris() != null) {
							cellJ.setCellValue(record.getR11_gpfsr_pos_att2_per_spe_ris().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// row11
						// Column M
						Cell cellM = row.createCell(12);
						if (record.getR11_net_pos_gen_mar_ris() != null) {
							cellM.setCellValue(record.getR11_net_pos_gen_mar_ris().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}
						
						// row12
						row = sheet.getRow(11);
						
						// row12
						// Column B  ->Market
						 cellB = row.createCell(1);
						if (record.getR12_market() != null) {
							cellB.setCellValue(record.getR12_market().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}
						
						
						// row12
						// Column C -->Nominal Amount
						 cellC = row.createCell(2);
						if (record.getR12_gpfsr_nom_amt() != null) {
							cellC.setCellValue(record.getR12_gpfsr_nom_amt().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// row12
						// Column D -->Positions Attracting 8 Percent Specific Risk
						 cellD = row.createCell(3);
						if (record.getR12_gpfsr_pos_att8_per_spe_ris() != null) {
							cellD.setCellValue(record.getR12_gpfsr_pos_att8_per_spe_ris().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// row12
						// Column F -->Nominal Amount
						 cellF = row.createCell(5);
						if (record.getR12_gpfsr_nom_amt1() != null) {
							cellF.setCellValue(record.getR12_gpfsr_nom_amt1().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row12
						// Column G -->Positions Attracting 4 Percent Specific Risk
						 cellG = row.createCell(6);
						if (record.getR12_gpfsr_pos_att4_per_spe_ris() != null) {
							cellG.setCellValue(record.getR12_gpfsr_pos_att4_per_spe_ris().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row12
						// Column I -->Nominal Amount
						 cellI = row.createCell(8);
						if (record.getR12_gpfsr_nom_amt2() != null) {
							cellI.setCellValue(record.getR12_gpfsr_nom_amt2().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row12
						// Column J -->Positions Attracting 2 Percent Specific Risk
					       cellJ = row.createCell(9);
						if (record.getR12_gpfsr_pos_att2_per_spe_ris() != null) {
							cellJ.setCellValue(record.getR12_gpfsr_pos_att2_per_spe_ris().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// row12
						// Column M -->Net Positions for General Market Risk
						 cellM = row.createCell(12);
						if (record.getR12_net_pos_gen_mar_ris() != null) {
							cellM.setCellValue(record.getR12_net_pos_gen_mar_ris().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}
						
						
						// ---- row13 ----
						row = sheet.getRow(12);

						// row13
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR13_market() != null) {
						    cellB.setCellValue(record.getR13_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row13
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR13_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR13_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row13
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR13_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR13_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row13
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR13_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR13_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row13
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR13_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR13_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row13
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR13_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR13_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row13
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR13_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR13_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row13
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR13_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR13_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row14 ----
						row = sheet.getRow(13);

						// row14
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR14_market() != null) {
						    cellB.setCellValue(record.getR14_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row14
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR14_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR14_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row14
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR14_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR14_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row14
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR14_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR14_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row14
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR14_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR14_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row14
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR14_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR14_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row14
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR14_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR14_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row14
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR14_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR14_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row15 ----
						row = sheet.getRow(14);

						// row15
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR15_market() != null) {
						    cellB.setCellValue(record.getR15_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row15
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR15_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR15_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row15
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR15_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR15_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row15
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR15_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR15_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row15
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR15_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR15_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row15
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR15_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR15_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row15
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR15_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR15_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row15
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR15_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR15_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row16 ----
						row = sheet.getRow(15);

						// row16
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR16_market() != null) {
						    cellB.setCellValue(record.getR16_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row16
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR16_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR16_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row16
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR16_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR16_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row16
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR16_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR16_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row16
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR16_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR16_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row16
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR16_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR16_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row16
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR16_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR16_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row16
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR16_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR16_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row17 ----
						row = sheet.getRow(16);

						// row17
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR17_market() != null) {
						    cellB.setCellValue(record.getR17_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row17
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR17_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR17_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row17
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR17_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR17_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row17
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR17_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR17_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row17
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR17_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR17_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row17
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR17_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR17_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row17
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR17_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR17_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row17
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR17_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR17_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row18 ----
						row = sheet.getRow(17);

						// row18
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR18_market() != null) {
						    cellB.setCellValue(record.getR18_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row18
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR18_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR18_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row18
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR18_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR18_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row18
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR18_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR18_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row18
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR18_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR18_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row18
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR18_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR18_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row18
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR18_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR18_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row18
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR18_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR18_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row19 ----
						row = sheet.getRow(18);

						// row19
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR19_market() != null) {
						    cellB.setCellValue(record.getR19_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row19
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR19_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR19_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row19
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR19_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR19_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row19
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR19_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR19_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row19
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR19_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR19_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row19
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR19_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR19_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row19
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR19_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR19_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row19
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR19_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR19_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row20 ----
						row = sheet.getRow(19);

						// row20
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR20_market() != null) {
						    cellB.setCellValue(record.getR20_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row20
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR20_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR20_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row20
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR20_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR20_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row20
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR20_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR20_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row20
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR20_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR20_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row20
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR20_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR20_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row20
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR20_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR20_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row20
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR20_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR20_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row21 ----
						row = sheet.getRow(20);

						// row21
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR21_market() != null) {
						    cellB.setCellValue(record.getR21_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row21
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR21_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR21_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row21
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR21_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR21_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row21
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR21_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR21_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row21
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR21_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR21_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row21
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR21_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR21_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row21
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR21_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR21_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row21
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR21_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR21_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row22 ----
						row = sheet.getRow(21);

						// row22
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR22_market() != null) {
						    cellB.setCellValue(record.getR22_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row22
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR22_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR22_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row22
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR22_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR22_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						// row22
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR22_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR22_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row22
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR22_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR22_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row22
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR22_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR22_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row22
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR22_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR22_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// row22
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR22_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR22_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// ---- row23 ----
						row = sheet.getRow(22);

						
						// row23
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR23_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR23_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row23
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR23_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR23_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row23
						// Column E -->Charge
					Cell cellE = row.createCell(4);
						if (record.getR23_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR23_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row23
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR23_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR23_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row23
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR23_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR23_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row23
						// Column H -->Charge
					Cell cellH = row.createCell(7);
						if (record.getR23_gpfsr_chrg1() != null) {
						    cellH.setCellValue(record.getR23_gpfsr_chrg1().doubleValue());
						    cellH.setCellStyle(numberStyle);
						} else {
						    cellH.setCellValue("");
						    cellH.setCellStyle(textStyle);
						}

						// row23
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR23_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR23_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row23
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR23_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR23_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row23
						// Column K -->Charge
					Cell cellK = row.createCell(10);
						if (record.getR23_gpfsr_chrg2() != null) {
						    cellK.setCellValue(record.getR23_gpfsr_chrg2().doubleValue());
						    cellK.setCellStyle(numberStyle);
						} else {
						    cellK.setCellValue("");
						    cellK.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_EPR ARCHIVAL SUMMARY", null, "BRRS_M_EPR_ARCHIVALTABLE_SUMMARY");
					}

					return out.toByteArray();
				}

			}


	
//=====================================================
//ARCHIVAL SUMMARY EXCEL  EMAIL
//=====================================================

// Archival Email Excel
			public byte[] BRRS_M_EPRARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Archival Email Excel generation process in memory.");

				List<M_EPR_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_EPR report. Returning empty result.");
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

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_EPR_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
						//row6
						// Column B
						Cell cellBdate = row.createCell(2);
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
						// Column B
						Cell cellB = row.createCell(1);
						if (record.getR11_market() != null) {
							cellB.setCellValue(record.getR11_market().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row11
						// Column C
						Cell cellC = row.createCell(2);
						if (record.getR11_gpfsr_nom_amt() != null) {
							cellC.setCellValue(record.getR11_gpfsr_nom_amt().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// row11
						// Column D
						Cell cellD = row.createCell(3);
						if (record.getR11_gpfsr_pos_att8_per_spe_ris() != null) {
							cellD.setCellValue(record.getR11_gpfsr_pos_att8_per_spe_ris().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}
						
						// row11
						// Column E -->Charge
						Cell	cellE = row.createCell(4);
						if (record.getR11_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR11_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row11
						// Column F
						Cell cellF = row.createCell(5);
						if (record.getR11_gpfsr_nom_amt1() != null) {
							cellF.setCellValue(record.getR11_gpfsr_nom_amt1().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row11
						// Column G
						Cell cellG = row.createCell(6);
						if (record.getR11_gpfsr_pos_att4_per_spe_ris() != null) {
							cellG.setCellValue(record.getR11_gpfsr_pos_att4_per_spe_ris().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}
						
						// row11
						// Column H
						Cell cellH = row.createCell(7);
						if (record.getR11_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR11_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row11
						// Column I
						Cell cellI = row.createCell(8);
						if (record.getR11_gpfsr_nom_amt2() != null) {
							cellI.setCellValue(record.getR11_gpfsr_nom_amt2().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row11
						// Column J
						Cell cellJ = row.createCell(9);
						if (record.getR11_gpfsr_pos_att2_per_spe_ris() != null) {
							cellJ.setCellValue(record.getR11_gpfsr_pos_att2_per_spe_ris().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}
						
						// row11
						// Column K ---------Charge

						Cell cellK = row.createCell(10);
						if (record.getR11_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR11_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row11
						// Column M
						Cell cellM = row.createCell(12);
						if (record.getR11_net_pos_gen_mar_ris() != null) {
							cellM.setCellValue(record.getR11_net_pos_gen_mar_ris().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}
						
							// row11
						// Column N -->General Market Risk Change at 8%

						Cell cellN = row.createCell(13);
						if (record.getR11_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR11_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						Cell 	cellO = row.createCell(14);
						if (record.getR11_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR11_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						Cell cellP = row.createCell(15);
						if (record.getR11_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR11_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						
						// row12
						row = sheet.getRow(11);
						
						// row12
						// Column B  ->Market
						 cellB = row.createCell(1);
						if (record.getR12_market() != null) {
							cellB.setCellValue(record.getR12_market().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}
						
						
						// row12
						// Column C -->Nominal Amount
						 cellC = row.createCell(2);
						if (record.getR12_gpfsr_nom_amt() != null) {
							cellC.setCellValue(record.getR12_gpfsr_nom_amt().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// row12
						// Column D -->Positions Attracting 8 Percent Specific Risk
						 cellD = row.createCell(3);
						if (record.getR12_gpfsr_pos_att8_per_spe_ris() != null) {
							cellD.setCellValue(record.getR12_gpfsr_pos_att8_per_spe_ris().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}
						
						// row12
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR12_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR12_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row12
						// Column F -->Nominal Amount
						 cellF = row.createCell(5);
						if (record.getR12_gpfsr_nom_amt1() != null) {
							cellF.setCellValue(record.getR12_gpfsr_nom_amt1().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row12
						// Column G -->Positions Attracting 4 Percent Specific Risk
						 cellG = row.createCell(6);
						if (record.getR12_gpfsr_pos_att4_per_spe_ris() != null) {
							cellG.setCellValue(record.getR12_gpfsr_pos_att4_per_spe_ris().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}
						
							// row12
						// Column H
					 cellH = row.createCell(7);
						if (record.getR12_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR12_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}


						// row12
						// Column I -->Nominal Amount
						 cellI = row.createCell(8);
						if (record.getR12_gpfsr_nom_amt2() != null) {
							cellI.setCellValue(record.getR12_gpfsr_nom_amt2().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row12
						// Column J -->Positions Attracting 2 Percent Specific Risk
					       cellJ = row.createCell(9);
						if (record.getR12_gpfsr_pos_att2_per_spe_ris() != null) {
							cellJ.setCellValue(record.getR12_gpfsr_pos_att2_per_spe_ris().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}
						
						// row12
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR12_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR12_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row12
						// Column M -->Net Positions for General Market Risk
						 cellM = row.createCell(12);
						if (record.getR12_net_pos_gen_mar_ris() != null) {
							cellM.setCellValue(record.getR12_net_pos_gen_mar_ris().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}
						
							// row12
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR12_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR12_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR12_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR12_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR12_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR12_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						
						
						// ---- row13 ----
						row = sheet.getRow(12);

						// row13
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR13_market() != null) {
						    cellB.setCellValue(record.getR13_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row13
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR13_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR13_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row13
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR13_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR13_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row13
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR13_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR13_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row13
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR13_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR13_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row13
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR13_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR13_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row13
						// Column H
						 cellH = row.createCell(7);
						if (record.getR13_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR13_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row13
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR13_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR13_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row13
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR13_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR13_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row13
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR13_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR13_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row13
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR13_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR13_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
							// row13
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR13_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR13_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR13_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR13_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR13_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR13_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}

						// ---- row14 ----
						row = sheet.getRow(13);

						// row14
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR14_market() != null) {
						    cellB.setCellValue(record.getR14_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row14
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR14_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR14_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row14
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR14_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR14_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row14
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR14_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR14_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row14
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR14_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR14_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row14
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR14_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR14_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row14
						// Column H
						 cellH = row.createCell(7);
						if (record.getR14_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR14_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row14
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR14_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR14_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row14
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR14_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR14_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row14
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR14_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR14_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row14
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR14_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR14_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
							// row14
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR14_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR14_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR14_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR14_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR14_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR14_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}

						// ---- row15 ----
						row = sheet.getRow(14);

						// row15
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR15_market() != null) {
						    cellB.setCellValue(record.getR15_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row15
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR15_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR15_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row15
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR15_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR15_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						
						// row15
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR15_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR15_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// Column F --> Nominal Amount
						cellF = row.createCell(5);
						if (record.getR15_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR15_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// Column G --> Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR15_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR15_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// Column H --> Charge
						cellH = row.createCell(7);
						if (record.getR15_gpfsr_chrg1() != null) {
						    cellH.setCellValue(record.getR15_gpfsr_chrg1().doubleValue());
						    cellH.setCellStyle(numberStyle);
						} else {
						    cellH.setCellValue("");
						    cellH.setCellStyle(textStyle);
						}

						// Column I --> Nominal Amount
						cellI = row.createCell(8);
						if (record.getR15_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR15_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// Column J --> Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR15_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR15_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// Column K --> Charge
						cellK = row.createCell(10);
						if (record.getR15_gpfsr_chrg2() != null) {
						    cellK.setCellValue(record.getR15_gpfsr_chrg2().doubleValue());
						    cellK.setCellStyle(numberStyle);
						} else {
						    cellK.setCellValue("");
						    cellK.setCellStyle(textStyle);
						}

						// row15
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR15_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR15_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						
							// row15
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR15_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR15_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR15_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR15_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR15_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR15_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}

						// ---- row16 ----
						row = sheet.getRow(15);

						// row16
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR16_market() != null) {
						    cellB.setCellValue(record.getR16_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row16
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR16_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR16_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row16
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR16_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR16_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}

						
						// row16
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR16_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR16_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						// Column F --> Nominal Amount
						cellF = row.createCell(5);
						if (record.getR16_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR16_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// Column G --> Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR16_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR16_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// Column H --> Charge
						cellH = row.createCell(7);
						if (record.getR16_gpfsr_chrg1() != null) {
						    cellH.setCellValue(record.getR16_gpfsr_chrg1().doubleValue());
						    cellH.setCellStyle(numberStyle);
						} else {
						    cellH.setCellValue("");
						    cellH.setCellStyle(textStyle);
						}

						// Column I --> Nominal Amount
						cellI = row.createCell(8);
						if (record.getR16_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR16_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// Column J --> Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR16_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR16_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}

						// Column K --> Charge
						cellK = row.createCell(10);
						if (record.getR16_gpfsr_chrg2() != null) {
						    cellK.setCellValue(record.getR16_gpfsr_chrg2().doubleValue());
						    cellK.setCellStyle(numberStyle);
						} else {
						    cellK.setCellValue("");
						    cellK.setCellStyle(textStyle);
						}
						

						// row16
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR16_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR16_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						
						// row16
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR16_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR16_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR16_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR16_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR16_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR16_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						

						// ---- row17 ----
						row = sheet.getRow(16);

						// row17
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR17_market() != null) {
						    cellB.setCellValue(record.getR17_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row17
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR17_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR17_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row17
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR17_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR17_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row17
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR17_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR17_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row17
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR17_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR17_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row17
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR17_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR17_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						// row17
						// Column H
						 cellH = row.createCell(7);
						if (record.getR17_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR17_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row17
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR17_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR17_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row17
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR17_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR17_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row17
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR17_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR17_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row17
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR17_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR17_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						
						// row17
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR17_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR17_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR17_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR17_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR17_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR17_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						// ---- row18 ----
						row = sheet.getRow(17);

						// row18
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR18_market() != null) {
						    cellB.setCellValue(record.getR18_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row18
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR18_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR18_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row18
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR18_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR18_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row18
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR18_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR18_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row18
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR18_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR18_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row18
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR18_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR18_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row18
						// Column H
						 cellH = row.createCell(7);
						if (record.getR18_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR18_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row18
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR18_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR18_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row18
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR18_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR18_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row18
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR18_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR18_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row18
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR18_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR18_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}

						// row18
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR18_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR18_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR18_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR18_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR18_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR18_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						// ---- row19 ----
						row = sheet.getRow(18);

						// row19
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR19_market() != null) {
						    cellB.setCellValue(record.getR19_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row19
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR19_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR19_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row19
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR19_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR19_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row19
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR19_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR19_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row19
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR19_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR19_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row19
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR19_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR19_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row19
						// Column H
						 cellH = row.createCell(7);
						if (record.getR19_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR19_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row19
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR19_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR19_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row19
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR19_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR19_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row19
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR19_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR19_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row19
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR19_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR19_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						// row19
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR19_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR19_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR19_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR19_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR19_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR19_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}

						// ---- row20 ----
						row = sheet.getRow(19);

						// row20
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR20_market() != null) {
						    cellB.setCellValue(record.getR20_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row20
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR20_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR20_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row20
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR20_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR20_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row20
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR20_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR20_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row20
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR20_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR20_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row20
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR20_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR20_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row20
						// Column H
						 cellH = row.createCell(7);
						if (record.getR20_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR20_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row20
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR20_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR20_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row20
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR20_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR20_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row20
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR20_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR20_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row20
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR20_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR20_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						
						// row20
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR20_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR20_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR20_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR20_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR20_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR20_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}

						// ---- row21 ----
						row = sheet.getRow(20);

						// row21
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR21_market() != null) {
						    cellB.setCellValue(record.getR21_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row21
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR21_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR21_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row21
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR21_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR21_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row21
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR21_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR21_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row21
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR21_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR21_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row21
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR21_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR21_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}

						// row21
						// Column H
						 cellH = row.createCell(7);
						if (record.getR21_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR21_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}
						
						// row21
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR21_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR21_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row21
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR21_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR21_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row21
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR21_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR21_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row21
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR21_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR21_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						
						
								
						
						
							// row21
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR21_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR21_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR21_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR21_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR21_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR21_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						

						// ---- row22 ----
						row = sheet.getRow(21);

						// row22
						// Column B -->Market
						cellB = row.createCell(1);
						if (record.getR22_market() != null) {
						    cellB.setCellValue(record.getR22_market().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row22
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR22_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR22_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row22
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR22_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR22_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row22
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR22_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR22_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row22
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR22_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR22_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row22
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR22_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR22_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row22
						// Column H
						 cellH = row.createCell(7);
						if (record.getR22_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR22_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row22
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR22_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR22_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row22
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR22_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR22_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row22
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR22_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR22_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row22
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR22_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR22_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						
							// row22
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR22_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR22_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR22_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR22_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}

						
						
						
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR22_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR22_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						
						
						
						
						
						

						// ---- row23 ----
						row = sheet.getRow(22);

						
						// row23
						// Column C -->Nominal Amount
						cellC = row.createCell(2);
						if (record.getR23_gpfsr_nom_amt() != null) {
						    cellC.setCellValue(record.getR23_gpfsr_nom_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row23
						// Column D -->Positions Attracting 8 Percent Specific Risk
						cellD = row.createCell(3);
						if (record.getR23_gpfsr_pos_att8_per_spe_ris() != null) {
						    cellD.setCellValue(record.getR23_gpfsr_pos_att8_per_spe_ris().doubleValue());
						    cellD.setCellStyle(numberStyle);
						} else {
						    cellD.setCellValue("");
						    cellD.setCellStyle(textStyle);
						}
						
						// row23
						// Column E -->Charge
					 cellE = row.createCell(4);
						if (record.getR23_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR23_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row23
						// Column F -->Nominal Amount
						cellF = row.createCell(5);
						if (record.getR23_gpfsr_nom_amt1() != null) {
						    cellF.setCellValue(record.getR23_gpfsr_nom_amt1().doubleValue());
						    cellF.setCellStyle(numberStyle);
						} else {
						    cellF.setCellValue("");
						    cellF.setCellStyle(textStyle);
						}

						// row23
						// Column G -->Positions Attracting 4 Percent Specific Risk
						cellG = row.createCell(6);
						if (record.getR23_gpfsr_pos_att4_per_spe_ris() != null) {
						    cellG.setCellValue(record.getR23_gpfsr_pos_att4_per_spe_ris().doubleValue());
						    cellG.setCellStyle(numberStyle);
						} else {
						    cellG.setCellValue("");
						    cellG.setCellStyle(textStyle);
						}
						
						// row23
						// Column H -->Charge
					 cellH = row.createCell(7);
						if (record.getR23_gpfsr_chrg1() != null) {
						    cellH.setCellValue(record.getR23_gpfsr_chrg1().doubleValue());
						    cellH.setCellStyle(numberStyle);
						} else {
						    cellH.setCellValue("");
						    cellH.setCellStyle(textStyle);
						}

						// row23
						// Column I -->Nominal Amount
						cellI = row.createCell(8);
						if (record.getR23_gpfsr_nom_amt2() != null) {
						    cellI.setCellValue(record.getR23_gpfsr_nom_amt2().doubleValue());
						    cellI.setCellStyle(numberStyle);
						} else {
						    cellI.setCellValue("");
						    cellI.setCellStyle(textStyle);
						}

						// row23
						// Column J -->Positions Attracting 2 Percent Specific Risk
						cellJ = row.createCell(9);
						if (record.getR23_gpfsr_pos_att2_per_spe_ris() != null) {
						    cellJ.setCellValue(record.getR23_gpfsr_pos_att2_per_spe_ris().doubleValue());
						    cellJ.setCellStyle(numberStyle);
						} else {
						    cellJ.setCellValue("");
						    cellJ.setCellStyle(textStyle);
						}
						
						// row23
						// Column K -->Charge
					 cellK = row.createCell(10);
						if (record.getR23_gpfsr_chrg2() != null) {
						    cellK.setCellValue(record.getR23_gpfsr_chrg2().doubleValue());
						    cellK.setCellStyle(numberStyle);
						} else {
						    cellK.setCellValue("");
						    cellK.setCellStyle(textStyle);
						}
						
						
						
						// row23
						// Column L --> Total Specific Risk Charge

				Cell cellL = row.createCell(11);
						if (record.getR23_tot_spe_ris_chrg() != null) {
						    cellL.setCellValue(record.getR23_tot_spe_ris_chrg().doubleValue());
						    cellL.setCellStyle(numberStyle);
						} else {
						    cellL.setCellValue("");
						    cellL.setCellStyle(textStyle);
						}
						
						// row23
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR23_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR23_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR23_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR23_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}

						
						
						
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR23_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR23_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						
						
							// Column Q --> Total Market Risk Change

						Cell cellQ = row.createCell(16);
						if (record.getR23_tot_mar_ris_chrg() != null) {
						    cellQ.setCellValue(record.getR23_tot_mar_ris_chrg().doubleValue());
						    cellQ.setCellStyle(numberStyle);
						} else {
						    cellQ.setCellValue("");
						    cellQ.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_EPR EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_EPR_ARCHIVALTABLE_SUMMARY");
					}

					return out.toByteArray();
				}
			}
	
//=====================================================
// RESUB EXCEL  FORMAT
//=====================================================

		// Resub Format excel
			public byte[] BRRS_M_EPRResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_EPREmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			}

				List<M_EPR_RESUB_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_EPR report. Returning empty result.");
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

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {

							M_EPR_RESUB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
							//row6
						// Column B
						Cell cellBdate = row.createCell(2);
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
							// Column B
							Cell cellB = row.createCell(1);
							if (record.getR11_market() != null) {
								cellB.setCellValue(record.getR11_market().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row11
							// Column C
							Cell cellC = row.createCell(2);
							if (record.getR11_gpfsr_nom_amt() != null) {
								cellC.setCellValue(record.getR11_gpfsr_nom_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D
							Cell cellD = row.createCell(3);
							if (record.getR11_gpfsr_pos_att8_per_spe_ris() != null) {
								cellD.setCellValue(record.getR11_gpfsr_pos_att8_per_spe_ris().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row11
							// Column F
							Cell cellF = row.createCell(5);
							if (record.getR11_gpfsr_nom_amt1() != null) {
								cellF.setCellValue(record.getR11_gpfsr_nom_amt1().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// row11
							// Column G
							Cell cellG = row.createCell(6);
							if (record.getR11_gpfsr_pos_att4_per_spe_ris() != null) {
								cellG.setCellValue(record.getR11_gpfsr_pos_att4_per_spe_ris().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row11
							// Column I
							Cell cellI = row.createCell(8);
							if (record.getR11_gpfsr_nom_amt2() != null) {
								cellI.setCellValue(record.getR11_gpfsr_nom_amt2().doubleValue());
								cellI.setCellStyle(numberStyle);
							} else {
								cellI.setCellValue("");
								cellI.setCellStyle(textStyle);
							}

							// row11
							// Column J
							Cell cellJ = row.createCell(9);
							if (record.getR11_gpfsr_pos_att2_per_spe_ris() != null) {
								cellJ.setCellValue(record.getR11_gpfsr_pos_att2_per_spe_ris().doubleValue());
								cellJ.setCellStyle(numberStyle);
							} else {
								cellJ.setCellValue("");
								cellJ.setCellStyle(textStyle);
							}

							// row11
							// Column M
							Cell cellM = row.createCell(12);
							if (record.getR11_net_pos_gen_mar_ris() != null) {
								cellM.setCellValue(record.getR11_net_pos_gen_mar_ris().doubleValue());
								cellM.setCellStyle(numberStyle);
							} else {
								cellM.setCellValue("");
								cellM.setCellStyle(textStyle);
							}
							
							// row12
							row = sheet.getRow(11);
							
							// row12
							// Column B  ->Market
							 cellB = row.createCell(1);
							if (record.getR12_market() != null) {
								cellB.setCellValue(record.getR12_market().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}
							
							
							// row12
							// Column C -->Nominal Amount
							 cellC = row.createCell(2);
							if (record.getR12_gpfsr_nom_amt() != null) {
								cellC.setCellValue(record.getR12_gpfsr_nom_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row12
							// Column D -->Positions Attracting 8 Percent Specific Risk
							 cellD = row.createCell(3);
							if (record.getR12_gpfsr_pos_att8_per_spe_ris() != null) {
								cellD.setCellValue(record.getR12_gpfsr_pos_att8_per_spe_ris().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row12
							// Column F -->Nominal Amount
							 cellF = row.createCell(5);
							if (record.getR12_gpfsr_nom_amt1() != null) {
								cellF.setCellValue(record.getR12_gpfsr_nom_amt1().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// row12
							// Column G -->Positions Attracting 4 Percent Specific Risk
							 cellG = row.createCell(6);
							if (record.getR12_gpfsr_pos_att4_per_spe_ris() != null) {
								cellG.setCellValue(record.getR12_gpfsr_pos_att4_per_spe_ris().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row12
							// Column I -->Nominal Amount
							 cellI = row.createCell(8);
							if (record.getR12_gpfsr_nom_amt2() != null) {
								cellI.setCellValue(record.getR12_gpfsr_nom_amt2().doubleValue());
								cellI.setCellStyle(numberStyle);
							} else {
								cellI.setCellValue("");
								cellI.setCellStyle(textStyle);
							}

							// row12
							// Column J -->Positions Attracting 2 Percent Specific Risk
						       cellJ = row.createCell(9);
							if (record.getR12_gpfsr_pos_att2_per_spe_ris() != null) {
								cellJ.setCellValue(record.getR12_gpfsr_pos_att2_per_spe_ris().doubleValue());
								cellJ.setCellStyle(numberStyle);
							} else {
								cellJ.setCellValue("");
								cellJ.setCellStyle(textStyle);
							}

							// row12
							// Column M -->Net Positions for General Market Risk
							 cellM = row.createCell(12);
							if (record.getR12_net_pos_gen_mar_ris() != null) {
								cellM.setCellValue(record.getR12_net_pos_gen_mar_ris().doubleValue());
								cellM.setCellStyle(numberStyle);
							} else {
								cellM.setCellValue("");
								cellM.setCellStyle(textStyle);
							}
							
							
							// ---- row13 ----
							row = sheet.getRow(12);

							// row13
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR13_market() != null) {
							    cellB.setCellValue(record.getR13_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row13
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR13_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR13_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row13
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR13_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR13_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// row13
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR13_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR13_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row13
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR13_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR13_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// row13
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR13_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR13_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row13
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR13_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR13_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}

							// row13
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR13_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR13_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}

							// ---- row14 ----
							row = sheet.getRow(13);

							// row14
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR14_market() != null) {
							    cellB.setCellValue(record.getR14_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row14
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR14_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR14_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row14
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR14_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR14_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// row14
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR14_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR14_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row14
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR14_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR14_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// row14
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR14_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR14_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row14
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR14_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR14_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}

							// row14
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR14_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR14_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}

							// ---- row15 ----
							row = sheet.getRow(14);

							// row15
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR15_market() != null) {
							    cellB.setCellValue(record.getR15_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row15
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR15_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR15_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row15
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR15_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR15_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// row15
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR15_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR15_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row15
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR15_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR15_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// row15
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR15_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR15_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row15
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR15_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR15_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}

							// row15
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR15_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR15_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}

							// ---- row16 ----
							row = sheet.getRow(15);

							// row16
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR16_market() != null) {
							    cellB.setCellValue(record.getR16_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row16
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR16_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR16_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row16
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR16_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR16_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// row16
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR16_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR16_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row16
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR16_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR16_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// row16
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR16_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR16_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row16
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR16_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR16_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}

							// row16
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR16_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR16_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}

							// ---- row17 ----
							row = sheet.getRow(16);

							// row17
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR17_market() != null) {
							    cellB.setCellValue(record.getR17_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row17
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR17_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR17_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row17
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR17_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR17_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// row17
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR17_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR17_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row17
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR17_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR17_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// row17
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR17_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR17_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row17
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR17_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR17_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}

							// row17
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR17_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR17_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}

							// ---- row18 ----
							row = sheet.getRow(17);

							// row18
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR18_market() != null) {
							    cellB.setCellValue(record.getR18_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row18
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR18_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR18_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row18
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR18_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR18_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// row18
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR18_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR18_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row18
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR18_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR18_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// row18
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR18_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR18_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row18
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR18_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR18_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}

							// row18
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR18_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR18_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}

							// ---- row19 ----
							row = sheet.getRow(18);

							// row19
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR19_market() != null) {
							    cellB.setCellValue(record.getR19_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row19
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR19_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR19_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row19
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR19_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR19_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// row19
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR19_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR19_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row19
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR19_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR19_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// row19
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR19_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR19_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row19
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR19_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR19_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}

							// row19
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR19_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR19_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}

							// ---- row20 ----
							row = sheet.getRow(19);

							// row20
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR20_market() != null) {
							    cellB.setCellValue(record.getR20_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row20
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR20_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR20_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row20
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR20_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR20_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// row20
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR20_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR20_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row20
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR20_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR20_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// row20
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR20_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR20_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row20
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR20_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR20_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}

							// row20
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR20_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR20_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}

							// ---- row21 ----
							row = sheet.getRow(20);

							// row21
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR21_market() != null) {
							    cellB.setCellValue(record.getR21_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row21
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR21_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR21_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row21
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR21_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR21_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// row21
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR21_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR21_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row21
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR21_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR21_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// row21
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR21_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR21_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row21
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR21_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR21_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}

							// row21
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR21_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR21_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}

							// ---- row22 ----
							row = sheet.getRow(21);

							// row22
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR22_market() != null) {
							    cellB.setCellValue(record.getR22_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row22
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR22_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR22_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row22
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR22_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR22_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							// row22
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR22_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR22_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row22
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR22_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR22_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// row22
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR22_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR22_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row22
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR22_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR22_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}

							// row22
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR22_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR22_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}

							// ---- row23 ----
							row = sheet.getRow(22);

							
							// row23
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR23_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR23_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row23
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR23_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR23_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// row23
							// Column E -->Charge
						Cell cellE = row.createCell(4);
							if (record.getR23_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR23_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row23
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR23_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR23_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row23
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR23_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR23_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}
							
							// row23
							// Column H -->Charge
						Cell cellH = row.createCell(7);
							if (record.getR23_gpfsr_chrg1() != null) {
							    cellH.setCellValue(record.getR23_gpfsr_chrg1().doubleValue());
							    cellH.setCellStyle(numberStyle);
							} else {
							    cellH.setCellValue("");
							    cellH.setCellStyle(textStyle);
							}

							// row23
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR23_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR23_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row23
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR23_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR23_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}
							
							// row23
							// Column K -->Charge
						Cell cellK = row.createCell(10);
							if (record.getR23_gpfsr_chrg2() != null) {
							    cellK.setCellValue(record.getR23_gpfsr_chrg2().doubleValue());
							    cellK.setCellStyle(numberStyle);
							} else {
							    cellK.setCellValue("");
							    cellK.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_EPR RESUB SUMMARY", null, "BRRS_M_EPR_RESUB_SUMMARYTABLE");
					}

					return out.toByteArray();
				}

			}

	
//=====================================================
// RESUB  EXCEL EMAIL
//=====================================================

	
			// Resub Email Excel
			public byte[] BRRS_M_EPREmailResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting RESUB Email Excel generation process in memory.");

				List<M_EPR_RESUB_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_EPR report. Returning empty result.");
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

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_EPR_RESUB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
						//row6
						// Column B
						Cell cellBdate = row.createCell(2);
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
							// Column B
							Cell cellB = row.createCell(1);
							if (record.getR11_market() != null) {
								cellB.setCellValue(record.getR11_market().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row11
							// Column C
							Cell cellC = row.createCell(2);
							if (record.getR11_gpfsr_nom_amt() != null) {
								cellC.setCellValue(record.getR11_gpfsr_nom_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D
							Cell cellD = row.createCell(3);
							if (record.getR11_gpfsr_pos_att8_per_spe_ris() != null) {
								cellD.setCellValue(record.getR11_gpfsr_pos_att8_per_spe_ris().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}
							
							// row11
							// Column E -->Charge
							Cell	cellE = row.createCell(4);
							if (record.getR11_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR11_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row11
							// Column F
							Cell cellF = row.createCell(5);
							if (record.getR11_gpfsr_nom_amt1() != null) {
								cellF.setCellValue(record.getR11_gpfsr_nom_amt1().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// row11
							// Column G
							Cell cellG = row.createCell(6);
							if (record.getR11_gpfsr_pos_att4_per_spe_ris() != null) {
								cellG.setCellValue(record.getR11_gpfsr_pos_att4_per_spe_ris().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}
							
							// row11
							// Column H
							Cell cellH = row.createCell(7);
							if (record.getR11_gpfsr_chrg1() != null) { 
								cellH.setCellValue(record.getR11_gpfsr_chrg1().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							// row11
							// Column I
							Cell cellI = row.createCell(8);
							if (record.getR11_gpfsr_nom_amt2() != null) {
								cellI.setCellValue(record.getR11_gpfsr_nom_amt2().doubleValue());
								cellI.setCellStyle(numberStyle);
							} else {
								cellI.setCellValue("");
								cellI.setCellStyle(textStyle);
							}

							// row11
							// Column J
							Cell cellJ = row.createCell(9);
							if (record.getR11_gpfsr_pos_att2_per_spe_ris() != null) {
								cellJ.setCellValue(record.getR11_gpfsr_pos_att2_per_spe_ris().doubleValue());
								cellJ.setCellStyle(numberStyle);
							} else {
								cellJ.setCellValue("");
								cellJ.setCellStyle(textStyle);
							}
							
							// row11
							// Column K ---------Charge

							Cell cellK = row.createCell(10);
							if (record.getR11_gpfsr_chrg2() != null) {
								cellK.setCellValue(record.getR11_gpfsr_chrg2().doubleValue());
								cellK.setCellStyle(numberStyle);
							} else {
								cellK.setCellValue("");
								cellK.setCellStyle(textStyle);
							}

							// row11
							// Column M
							Cell cellM = row.createCell(12);
							if (record.getR11_net_pos_gen_mar_ris() != null) {
								cellM.setCellValue(record.getR11_net_pos_gen_mar_ris().doubleValue());
								cellM.setCellStyle(numberStyle);
							} else {
								cellM.setCellValue("");
								cellM.setCellStyle(textStyle);
							}
							
								// row11
							// Column N -->General Market Risk Change at 8%

							Cell cellN = row.createCell(13);
							if (record.getR11_gen_mar_ris_chrg_8per() != null) {
							    cellN.setCellValue(record.getR11_gen_mar_ris_chrg_8per().doubleValue());
							    cellN.setCellStyle(numberStyle);
							} else {
							    cellN.setCellValue("");
							    cellN.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							Cell 	cellO = row.createCell(14);
							if (record.getR11_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR11_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}
			
								// Column P -->Total General Market Risk Charge


							Cell cellP = row.createCell(15);
							if (record.getR11_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR11_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}
							
							// row12
							row = sheet.getRow(11);
							
							// row12
							// Column B  ->Market
							 cellB = row.createCell(1);
							if (record.getR12_market() != null) {
								cellB.setCellValue(record.getR12_market().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}
							
							
							// row12
							// Column C -->Nominal Amount
							 cellC = row.createCell(2);
							if (record.getR12_gpfsr_nom_amt() != null) {
								cellC.setCellValue(record.getR12_gpfsr_nom_amt().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row12
							// Column D -->Positions Attracting 8 Percent Specific Risk
							 cellD = row.createCell(3);
							if (record.getR12_gpfsr_pos_att8_per_spe_ris() != null) {
								cellD.setCellValue(record.getR12_gpfsr_pos_att8_per_spe_ris().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}
							
							// row12
							// Column E -->Charge
							cellE = row.createCell(4);
							if (record.getR12_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR12_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row12
							// Column F -->Nominal Amount
							 cellF = row.createCell(5);
							if (record.getR12_gpfsr_nom_amt1() != null) {
								cellF.setCellValue(record.getR12_gpfsr_nom_amt1().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// row12
							// Column G -->Positions Attracting 4 Percent Specific Risk
							 cellG = row.createCell(6);
							if (record.getR12_gpfsr_pos_att4_per_spe_ris() != null) {
								cellG.setCellValue(record.getR12_gpfsr_pos_att4_per_spe_ris().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}
							
								// row12
							// Column H
						 cellH = row.createCell(7);
							if (record.getR12_gpfsr_chrg1() != null) { 
								cellH.setCellValue(record.getR12_gpfsr_chrg1().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}


							// row12
							// Column I -->Nominal Amount
							 cellI = row.createCell(8);
							if (record.getR12_gpfsr_nom_amt2() != null) {
								cellI.setCellValue(record.getR12_gpfsr_nom_amt2().doubleValue());
								cellI.setCellStyle(numberStyle);
							} else {
								cellI.setCellValue("");
								cellI.setCellStyle(textStyle);
							}

							// row12
							// Column J -->Positions Attracting 2 Percent Specific Risk
						       cellJ = row.createCell(9);
							if (record.getR12_gpfsr_pos_att2_per_spe_ris() != null) {
								cellJ.setCellValue(record.getR12_gpfsr_pos_att2_per_spe_ris().doubleValue());
								cellJ.setCellStyle(numberStyle);
							} else {
								cellJ.setCellValue("");
								cellJ.setCellStyle(textStyle);
							}
							
							// row12
							// Column K ---------Charge

							 cellK = row.createCell(10);
							if (record.getR12_gpfsr_chrg2() != null) {
								cellK.setCellValue(record.getR12_gpfsr_chrg2().doubleValue());
								cellK.setCellStyle(numberStyle);
							} else {
								cellK.setCellValue("");
								cellK.setCellStyle(textStyle);
							}

							// row12
							// Column M -->Net Positions for General Market Risk
							 cellM = row.createCell(12);
							if (record.getR12_net_pos_gen_mar_ris() != null) {
								cellM.setCellValue(record.getR12_net_pos_gen_mar_ris().doubleValue());
								cellM.setCellStyle(numberStyle);
							} else {
								cellM.setCellValue("");
								cellM.setCellStyle(textStyle);
							}
							
								// row12
							// Column N -->General Market Risk Change at 8%

							cellN = row.createCell(13);
							if (record.getR12_gen_mar_ris_chrg_8per() != null) {
							    cellN.setCellValue(record.getR12_gen_mar_ris_chrg_8per().doubleValue());
							    cellN.setCellStyle(numberStyle);
							} else {
							    cellN.setCellValue("");
							    cellN.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							cellO = row.createCell(14);
							if (record.getR12_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR12_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}
			
								// Column P -->Total General Market Risk Charge


							cellP = row.createCell(15);
							if (record.getR12_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR12_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}
							
							
							// ---- row13 ----
							row = sheet.getRow(12);

							// row13
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR13_market() != null) {
							    cellB.setCellValue(record.getR13_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row13
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR13_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR13_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row13
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR13_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR13_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// row13
							// Column E -->Charge
							cellE = row.createCell(4);
							if (record.getR13_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR13_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row13
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR13_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR13_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row13
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR13_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR13_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}
							
							// row13
							// Column H
							 cellH = row.createCell(7);
							if (record.getR13_gpfsr_chrg1() != null) { 
								cellH.setCellValue(record.getR13_gpfsr_chrg1().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							// row13
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR13_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR13_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row13
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR13_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR13_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}
							
							// row13
							// Column K ---------Charge

							 cellK = row.createCell(10);
							if (record.getR13_gpfsr_chrg2() != null) {
								cellK.setCellValue(record.getR13_gpfsr_chrg2().doubleValue());
								cellK.setCellStyle(numberStyle);
							} else {
								cellK.setCellValue("");
								cellK.setCellStyle(textStyle);
							}

							// row13
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR13_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR13_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}
							
								// row13
							// Column N -->General Market Risk Change at 8%

							cellN = row.createCell(13);
							if (record.getR13_gen_mar_ris_chrg_8per() != null) {
							    cellN.setCellValue(record.getR13_gen_mar_ris_chrg_8per().doubleValue());
							    cellN.setCellStyle(numberStyle);
							} else {
							    cellN.setCellValue("");
							    cellN.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							cellO = row.createCell(14);
							if (record.getR13_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR13_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}
			
								// Column P -->Total General Market Risk Charge


							cellP = row.createCell(15);
							if (record.getR13_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR13_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}

							// ---- row14 ----
							row = sheet.getRow(13);

							// row14
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR14_market() != null) {
							    cellB.setCellValue(record.getR14_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row14
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR14_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR14_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row14
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR14_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR14_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// row14
							// Column E -->Charge
							cellE = row.createCell(4);
							if (record.getR14_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR14_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row14
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR14_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR14_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row14
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR14_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR14_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}
							
							// row14
							// Column H
							 cellH = row.createCell(7);
							if (record.getR14_gpfsr_chrg1() != null) { 
								cellH.setCellValue(record.getR14_gpfsr_chrg1().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							// row14
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR14_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR14_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row14
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR14_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR14_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}
							
							// row14
							// Column K ---------Charge

							 cellK = row.createCell(10);
							if (record.getR14_gpfsr_chrg2() != null) {
								cellK.setCellValue(record.getR14_gpfsr_chrg2().doubleValue());
								cellK.setCellStyle(numberStyle);
							} else {
								cellK.setCellValue("");
								cellK.setCellStyle(textStyle);
							}

							// row14
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR14_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR14_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}
							
								// row14
							// Column N -->General Market Risk Change at 8%

							cellN = row.createCell(13);
							if (record.getR14_gen_mar_ris_chrg_8per() != null) {
							    cellN.setCellValue(record.getR14_gen_mar_ris_chrg_8per().doubleValue());
							    cellN.setCellStyle(numberStyle);
							} else {
							    cellN.setCellValue("");
							    cellN.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							cellO = row.createCell(14);
							if (record.getR14_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR14_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}
			
								// Column P -->Total General Market Risk Charge


							cellP = row.createCell(15);
							if (record.getR14_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR14_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}

							// ---- row15 ----
							row = sheet.getRow(14);

							// row15
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR15_market() != null) {
							    cellB.setCellValue(record.getR15_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row15
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR15_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR15_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row15
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR15_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR15_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							
							// row15
							// Column E -->Charge
							cellE = row.createCell(4);
							if (record.getR15_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR15_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							// Column F --> Nominal Amount
							cellF = row.createCell(5);
							if (record.getR15_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR15_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// Column G --> Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR15_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR15_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// Column H --> Charge
							cellH = row.createCell(7);
							if (record.getR15_gpfsr_chrg1() != null) {
							    cellH.setCellValue(record.getR15_gpfsr_chrg1().doubleValue());
							    cellH.setCellStyle(numberStyle);
							} else {
							    cellH.setCellValue("");
							    cellH.setCellStyle(textStyle);
							}

							// Column I --> Nominal Amount
							cellI = row.createCell(8);
							if (record.getR15_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR15_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// Column J --> Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR15_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR15_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}

							// Column K --> Charge
							cellK = row.createCell(10);
							if (record.getR15_gpfsr_chrg2() != null) {
							    cellK.setCellValue(record.getR15_gpfsr_chrg2().doubleValue());
							    cellK.setCellStyle(numberStyle);
							} else {
							    cellK.setCellValue("");
							    cellK.setCellStyle(textStyle);
							}

							// row15
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR15_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR15_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}
							
							
								// row15
							// Column N -->General Market Risk Change at 8%

							cellN = row.createCell(13);
							if (record.getR15_gen_mar_ris_chrg_8per() != null) {
							    cellN.setCellValue(record.getR15_gen_mar_ris_chrg_8per().doubleValue());
							    cellN.setCellStyle(numberStyle);
							} else {
							    cellN.setCellValue("");
							    cellN.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							cellO = row.createCell(14);
							if (record.getR15_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR15_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}
			
								// Column P -->Total General Market Risk Charge


							cellP = row.createCell(15);
							if (record.getR15_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR15_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}

							// ---- row16 ----
							row = sheet.getRow(15);

							// row16
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR16_market() != null) {
							    cellB.setCellValue(record.getR16_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row16
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR16_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR16_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row16
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR16_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR16_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}

							
							// row16
							// Column E -->Charge
							cellE = row.createCell(4);
							if (record.getR16_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR16_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}
							
							// Column F --> Nominal Amount
							cellF = row.createCell(5);
							if (record.getR16_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR16_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// Column G --> Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR16_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR16_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// Column H --> Charge
							cellH = row.createCell(7);
							if (record.getR16_gpfsr_chrg1() != null) {
							    cellH.setCellValue(record.getR16_gpfsr_chrg1().doubleValue());
							    cellH.setCellStyle(numberStyle);
							} else {
							    cellH.setCellValue("");
							    cellH.setCellStyle(textStyle);
							}

							// Column I --> Nominal Amount
							cellI = row.createCell(8);
							if (record.getR16_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR16_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// Column J --> Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR16_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR16_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}

							// Column K --> Charge
							cellK = row.createCell(10);
							if (record.getR16_gpfsr_chrg2() != null) {
							    cellK.setCellValue(record.getR16_gpfsr_chrg2().doubleValue());
							    cellK.setCellStyle(numberStyle);
							} else {
							    cellK.setCellValue("");
							    cellK.setCellStyle(textStyle);
							}
							

							// row16
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR16_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR16_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}
							
							
							// row16
							// Column N -->General Market Risk Change at 8%

							cellN = row.createCell(13);
							if (record.getR16_gen_mar_ris_chrg_8per() != null) {
							    cellN.setCellValue(record.getR16_gen_mar_ris_chrg_8per().doubleValue());
							    cellN.setCellStyle(numberStyle);
							} else {
							    cellN.setCellValue("");
							    cellN.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							cellO = row.createCell(14);
							if (record.getR16_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR16_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}
			
								// Column P -->Total General Market Risk Charge


							cellP = row.createCell(15);
							if (record.getR16_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR16_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}
							

							// ---- row17 ----
							row = sheet.getRow(16);

							// row17
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR17_market() != null) {
							    cellB.setCellValue(record.getR17_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row17
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR17_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR17_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row17
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR17_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR17_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// row17
							// Column E -->Charge
							cellE = row.createCell(4);
							if (record.getR17_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR17_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row17
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR17_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR17_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row17
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR17_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR17_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}
							// row17
							// Column H
							 cellH = row.createCell(7);
							if (record.getR17_gpfsr_chrg1() != null) { 
								cellH.setCellValue(record.getR17_gpfsr_chrg1().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							// row17
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR17_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR17_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row17
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR17_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR17_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}
							
							// row17
							// Column K ---------Charge

							 cellK = row.createCell(10);
							if (record.getR17_gpfsr_chrg2() != null) {
								cellK.setCellValue(record.getR17_gpfsr_chrg2().doubleValue());
								cellK.setCellStyle(numberStyle);
							} else {
								cellK.setCellValue("");
								cellK.setCellStyle(textStyle);
							}

							// row17
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR17_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR17_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}

							
							// row17
							// Column N -->General Market Risk Change at 8%

							cellN = row.createCell(13);
							if (record.getR17_gen_mar_ris_chrg_8per() != null) {
							    cellN.setCellValue(record.getR17_gen_mar_ris_chrg_8per().doubleValue());
							    cellN.setCellStyle(numberStyle);
							} else {
							    cellN.setCellValue("");
							    cellN.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							cellO = row.createCell(14);
							if (record.getR17_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR17_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}
			
								// Column P -->Total General Market Risk Charge


							cellP = row.createCell(15);
							if (record.getR17_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR17_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}
							// ---- row18 ----
							row = sheet.getRow(17);

							// row18
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR18_market() != null) {
							    cellB.setCellValue(record.getR18_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row18
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR18_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR18_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row18
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR18_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR18_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// row18
							// Column E -->Charge
							cellE = row.createCell(4);
							if (record.getR18_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR18_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row18
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR18_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR18_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row18
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR18_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR18_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}
							
							// row18
							// Column H
							 cellH = row.createCell(7);
							if (record.getR18_gpfsr_chrg1() != null) { 
								cellH.setCellValue(record.getR18_gpfsr_chrg1().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							// row18
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR18_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR18_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row18
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR18_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR18_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}
							
							// row18
							// Column K ---------Charge

							 cellK = row.createCell(10);
							if (record.getR18_gpfsr_chrg2() != null) {
								cellK.setCellValue(record.getR18_gpfsr_chrg2().doubleValue());
								cellK.setCellStyle(numberStyle);
							} else {
								cellK.setCellValue("");
								cellK.setCellStyle(textStyle);
							}

							// row18
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR18_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR18_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}

							// row18
							// Column N -->General Market Risk Change at 8%

							cellN = row.createCell(13);
							if (record.getR18_gen_mar_ris_chrg_8per() != null) {
							    cellN.setCellValue(record.getR18_gen_mar_ris_chrg_8per().doubleValue());
							    cellN.setCellStyle(numberStyle);
							} else {
							    cellN.setCellValue("");
							    cellN.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							cellO = row.createCell(14);
							if (record.getR18_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR18_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}
			
								// Column P -->Total General Market Risk Charge


							cellP = row.createCell(15);
							if (record.getR18_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR18_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}
							// ---- row19 ----
							row = sheet.getRow(18);

							// row19
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR19_market() != null) {
							    cellB.setCellValue(record.getR19_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row19
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR19_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR19_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row19
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR19_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR19_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// row19
							// Column E -->Charge
							cellE = row.createCell(4);
							if (record.getR19_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR19_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row19
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR19_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR19_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row19
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR19_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR19_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}
							
							// row19
							// Column H
							 cellH = row.createCell(7);
							if (record.getR19_gpfsr_chrg1() != null) { 
								cellH.setCellValue(record.getR19_gpfsr_chrg1().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							// row19
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR19_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR19_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row19
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR19_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR19_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}
							
							// row19
							// Column K ---------Charge

							 cellK = row.createCell(10);
							if (record.getR19_gpfsr_chrg2() != null) {
								cellK.setCellValue(record.getR19_gpfsr_chrg2().doubleValue());
								cellK.setCellStyle(numberStyle);
							} else {
								cellK.setCellValue("");
								cellK.setCellStyle(textStyle);
							}

							// row19
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR19_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR19_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}
							
							// row19
							// Column N -->General Market Risk Change at 8%

							cellN = row.createCell(13);
							if (record.getR19_gen_mar_ris_chrg_8per() != null) {
							    cellN.setCellValue(record.getR19_gen_mar_ris_chrg_8per().doubleValue());
							    cellN.setCellStyle(numberStyle);
							} else {
							    cellN.setCellValue("");
							    cellN.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							cellO = row.createCell(14);
							if (record.getR19_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR19_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}
			
								// Column P -->Total General Market Risk Charge


							cellP = row.createCell(15);
							if (record.getR19_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR19_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}

							// ---- row20 ----
							row = sheet.getRow(19);

							// row20
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR20_market() != null) {
							    cellB.setCellValue(record.getR20_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row20
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR20_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR20_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row20
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR20_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR20_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// row20
							// Column E -->Charge
							cellE = row.createCell(4);
							if (record.getR20_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR20_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row20
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR20_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR20_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row20
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR20_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR20_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}
							
							// row20
							// Column H
							 cellH = row.createCell(7);
							if (record.getR20_gpfsr_chrg1() != null) { 
								cellH.setCellValue(record.getR20_gpfsr_chrg1().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							// row20
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR20_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR20_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row20
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR20_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR20_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}
							
							// row20
							// Column K ---------Charge

							 cellK = row.createCell(10);
							if (record.getR20_gpfsr_chrg2() != null) {
								cellK.setCellValue(record.getR20_gpfsr_chrg2().doubleValue());
								cellK.setCellStyle(numberStyle);
							} else {
								cellK.setCellValue("");
								cellK.setCellStyle(textStyle);
							}

							// row20
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR20_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR20_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}
							
							
							// row20
							// Column N -->General Market Risk Change at 8%

							cellN = row.createCell(13);
							if (record.getR20_gen_mar_ris_chrg_8per() != null) {
							    cellN.setCellValue(record.getR20_gen_mar_ris_chrg_8per().doubleValue());
							    cellN.setCellStyle(numberStyle);
							} else {
							    cellN.setCellValue("");
							    cellN.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							cellO = row.createCell(14);
							if (record.getR20_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR20_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}
			
								// Column P -->Total General Market Risk Charge


							cellP = row.createCell(15);
							if (record.getR20_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR20_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}

							// ---- row21 ----
							row = sheet.getRow(20);

							// row21
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR21_market() != null) {
							    cellB.setCellValue(record.getR21_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row21
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR21_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR21_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row21
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR21_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR21_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// row21
							// Column E -->Charge
							cellE = row.createCell(4);
							if (record.getR21_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR21_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row21
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR21_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR21_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row21
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR21_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR21_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}

							// row21
							// Column H
							 cellH = row.createCell(7);
							if (record.getR21_gpfsr_chrg1() != null) { 
								cellH.setCellValue(record.getR21_gpfsr_chrg1().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}
							
							// row21
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR21_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR21_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row21
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR21_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR21_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}
							
							// row21
							// Column K ---------Charge

							 cellK = row.createCell(10);
							if (record.getR21_gpfsr_chrg2() != null) {
								cellK.setCellValue(record.getR21_gpfsr_chrg2().doubleValue());
								cellK.setCellStyle(numberStyle);
							} else {
								cellK.setCellValue("");
								cellK.setCellStyle(textStyle);
							}

							// row21
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR21_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR21_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}
							
							
							
									
							
							
								// row21
							// Column N -->General Market Risk Change at 8%

							cellN = row.createCell(13);
							if (record.getR21_gen_mar_ris_chrg_8per() != null) {
							    cellN.setCellValue(record.getR21_gen_mar_ris_chrg_8per().doubleValue());
							    cellN.setCellStyle(numberStyle);
							} else {
							    cellN.setCellValue("");
							    cellN.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							cellO = row.createCell(14);
							if (record.getR21_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR21_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}
			
								// Column P -->Total General Market Risk Charge


							cellP = row.createCell(15);
							if (record.getR21_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR21_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}
							

							// ---- row22 ----
							row = sheet.getRow(21);

							// row22
							// Column B -->Market
							cellB = row.createCell(1);
							if (record.getR22_market() != null) {
							    cellB.setCellValue(record.getR22_market().doubleValue());
							    cellB.setCellStyle(numberStyle);
							} else {
							    cellB.setCellValue("");
							    cellB.setCellStyle(textStyle);
							}

							// row22
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR22_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR22_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row22
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR22_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR22_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// row22
							// Column E -->Charge
							cellE = row.createCell(4);
							if (record.getR22_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR22_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row22
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR22_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR22_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row22
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR22_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR22_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}
							
							// row22
							// Column H
							 cellH = row.createCell(7);
							if (record.getR22_gpfsr_chrg1() != null) { 
								cellH.setCellValue(record.getR22_gpfsr_chrg1().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							// row22
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR22_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR22_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row22
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR22_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR22_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}
							
							// row22
							// Column K ---------Charge

							 cellK = row.createCell(10);
							if (record.getR22_gpfsr_chrg2() != null) {
								cellK.setCellValue(record.getR22_gpfsr_chrg2().doubleValue());
								cellK.setCellStyle(numberStyle);
							} else {
								cellK.setCellValue("");
								cellK.setCellStyle(textStyle);
							}

							// row22
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR22_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR22_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}
							
							
								// row22
							// Column N -->General Market Risk Change at 8%

							cellN = row.createCell(13);
							if (record.getR22_gen_mar_ris_chrg_8per() != null) {
							    cellN.setCellValue(record.getR22_gen_mar_ris_chrg_8per().doubleValue());
							    cellN.setCellStyle(numberStyle);
							} else {
							    cellN.setCellValue("");
							    cellN.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							cellO = row.createCell(14);
							if (record.getR22_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR22_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}

							
							
							
								// Column P -->Total General Market Risk Charge


							cellP = row.createCell(15);
							if (record.getR22_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR22_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}
							
							
							
							
							
							

							// ---- row23 ----
							row = sheet.getRow(22);

							
							// row23
							// Column C -->Nominal Amount
							cellC = row.createCell(2);
							if (record.getR23_gpfsr_nom_amt() != null) {
							    cellC.setCellValue(record.getR23_gpfsr_nom_amt().doubleValue());
							    cellC.setCellStyle(numberStyle);
							} else {
							    cellC.setCellValue("");
							    cellC.setCellStyle(textStyle);
							}

							// row23
							// Column D -->Positions Attracting 8 Percent Specific Risk
							cellD = row.createCell(3);
							if (record.getR23_gpfsr_pos_att8_per_spe_ris() != null) {
							    cellD.setCellValue(record.getR23_gpfsr_pos_att8_per_spe_ris().doubleValue());
							    cellD.setCellStyle(numberStyle);
							} else {
							    cellD.setCellValue("");
							    cellD.setCellStyle(textStyle);
							}
							
							// row23
							// Column E -->Charge
						 cellE = row.createCell(4);
							if (record.getR23_gpfsr_chrg() != null) {
							    cellE.setCellValue(record.getR23_gpfsr_chrg().doubleValue());
							    cellE.setCellStyle(numberStyle);
							} else {
							    cellE.setCellValue("");
							    cellE.setCellStyle(textStyle);
							}

							// row23
							// Column F -->Nominal Amount
							cellF = row.createCell(5);
							if (record.getR23_gpfsr_nom_amt1() != null) {
							    cellF.setCellValue(record.getR23_gpfsr_nom_amt1().doubleValue());
							    cellF.setCellStyle(numberStyle);
							} else {
							    cellF.setCellValue("");
							    cellF.setCellStyle(textStyle);
							}

							// row23
							// Column G -->Positions Attracting 4 Percent Specific Risk
							cellG = row.createCell(6);
							if (record.getR23_gpfsr_pos_att4_per_spe_ris() != null) {
							    cellG.setCellValue(record.getR23_gpfsr_pos_att4_per_spe_ris().doubleValue());
							    cellG.setCellStyle(numberStyle);
							} else {
							    cellG.setCellValue("");
							    cellG.setCellStyle(textStyle);
							}
							
							// row23
							// Column H -->Charge
						 cellH = row.createCell(7);
							if (record.getR23_gpfsr_chrg1() != null) {
							    cellH.setCellValue(record.getR23_gpfsr_chrg1().doubleValue());
							    cellH.setCellStyle(numberStyle);
							} else {
							    cellH.setCellValue("");
							    cellH.setCellStyle(textStyle);
							}

							// row23
							// Column I -->Nominal Amount
							cellI = row.createCell(8);
							if (record.getR23_gpfsr_nom_amt2() != null) {
							    cellI.setCellValue(record.getR23_gpfsr_nom_amt2().doubleValue());
							    cellI.setCellStyle(numberStyle);
							} else {
							    cellI.setCellValue("");
							    cellI.setCellStyle(textStyle);
							}

							// row23
							// Column J -->Positions Attracting 2 Percent Specific Risk
							cellJ = row.createCell(9);
							if (record.getR23_gpfsr_pos_att2_per_spe_ris() != null) {
							    cellJ.setCellValue(record.getR23_gpfsr_pos_att2_per_spe_ris().doubleValue());
							    cellJ.setCellStyle(numberStyle);
							} else {
							    cellJ.setCellValue("");
							    cellJ.setCellStyle(textStyle);
							}
							
							// row23
							// Column K -->Charge
						 cellK = row.createCell(10);
							if (record.getR23_gpfsr_chrg2() != null) {
							    cellK.setCellValue(record.getR23_gpfsr_chrg2().doubleValue());
							    cellK.setCellStyle(numberStyle);
							} else {
							    cellK.setCellValue("");
							    cellK.setCellStyle(textStyle);
							}
							
							
							
							// row23
							// Column L --> Total Specific Risk Charge

					Cell cellL = row.createCell(11);
							if (record.getR23_tot_spe_ris_chrg() != null) {
							    cellL.setCellValue(record.getR23_tot_spe_ris_chrg().doubleValue());
							    cellL.setCellStyle(numberStyle);
							} else {
							    cellL.setCellValue("");
							    cellL.setCellStyle(textStyle);
							}
							
							// row23
							// Column M -->Net Positions for General Market Risk
							cellM = row.createCell(12);
							if (record.getR23_net_pos_gen_mar_ris() != null) {
							    cellM.setCellValue(record.getR23_net_pos_gen_mar_ris().doubleValue());
							    cellM.setCellStyle(numberStyle);
							} else {
							    cellM.setCellValue("");
							    cellM.setCellStyle(textStyle);
							}
							
							// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

							cellO = row.createCell(14);
							if (record.getR23_2per_gen_mar_ris_chrg_div_port() != null) {
							    cellO.setCellValue(record.getR23_2per_gen_mar_ris_chrg_div_port().doubleValue());
							    cellO.setCellStyle(numberStyle);
							} else {
							    cellO.setCellValue("");
							    cellO.setCellStyle(textStyle);
							}

							
							
							
								// Column P -->Total General Market Risk Charge


							cellP = row.createCell(15);
							if (record.getR23_tot_gen_mar_risk_chrg() != null) {
							    cellP.setCellValue(record.getR23_tot_gen_mar_risk_chrg().doubleValue());
							    cellP.setCellStyle(numberStyle);
							} else {
							    cellP.setCellValue("");
							    cellP.setCellStyle(textStyle);
							}
							
							
								// Column Q --> Total Market Risk Change

							Cell cellQ = row.createCell(16);
							if (record.getR23_tot_mar_ris_chrg() != null) {
							    cellQ.setCellValue(record.getR23_tot_mar_ris_chrg().doubleValue());
							    cellQ.setCellStyle(numberStyle);
							} else {
							    cellQ.setCellValue("");
							    cellQ.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_EPR EMAIL RESUB SUMMARY", null, "BRRS_M_EPR_RESUB_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
			}

		
	}