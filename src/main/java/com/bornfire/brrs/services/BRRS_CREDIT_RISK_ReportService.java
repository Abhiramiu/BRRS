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

public class BRRS_CREDIT_RISK_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_CREDIT_RISK_ReportService.class);
	
	
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


	public List<CREDIT_RISK_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_CREDIT_RISK_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new CREDIT_RISK_Summary_RowMapper()
    );
}
	
// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================



public List<Object[]> getCREDIT_RISK_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_CREDIT_RISK_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<CREDIT_RISK_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_CREDIT_RISK_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new CREDIT_RISK_Archival_Summary_RowMapper()
    );
}

public List<CREDIT_RISK_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_CREDIT_RISK_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new CREDIT_RISK_Archival_Summary_RowMapper()
    );
}

// =====================================================
// DETAIL REPO
// =====================================================	


public List<CREDIT_RISK_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_CREDIT_RISK_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new CREDIT_RISK_Detail_RowMapper()
    );
}

public List<CREDIT_RISK_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_CREDIT_RISK_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? " +
                 "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, offset, limit},
            new CREDIT_RISK_Detail_RowMapper()
    );
}

public int getDataCount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_CREDIT_RISK_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(sql, new Object[]{reportDate}, Integer.class);
}

public List<CREDIT_RISK_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel, String reportAddlCriteria1, Date reportDate) {

    String sql = "SELECT * FROM BRRS_CREDIT_RISK_DETAILTABLE " +
                 "WHERE REPORT_LABEL = ? " +
                 "AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate},
            new CREDIT_RISK_Detail_RowMapper()
    );
}

public CREDIT_RISK_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_CREDIT_RISK_DETAILTABLE WHERE ACCT_NUMBER = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{acctNumber},
            new CREDIT_RISK_Detail_RowMapper()
    );
}


// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================

public List<CREDIT_RISK_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate, String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_CREDIT_RISK_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, dataEntryVersion},
            new CREDIT_RISK_Archival_Detail_RowMapper()
    );
}


public List<CREDIT_RISK_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_CREDIT_RISK_ARCHIVALTABLE_DETAIL " +
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
            new CREDIT_RISK_Archival_Detail_RowMapper()
    );
}

// =====================================================
// SUMAMRY ENTITY 
// =====================================================


public class CREDIT_RISK_Summary_RowMapper implements RowMapper<CREDIT_RISK_Summary_Entity> {

    @Override
    public CREDIT_RISK_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        CREDIT_RISK_Summary_Entity obj = new CREDIT_RISK_Summary_Entity();

       // =========================
        // R4
        // =========================
        obj.setR4_product(rs.getString("r4_product"));
        obj.setR4_num(rs.getBigDecimal("r4_num"));

        // =========================
        // R5
        // =========================
        obj.setR5_qua_disc(rs.getString("r5_qua_disc"));
        obj.setR5_product(rs.getString("r5_product"));
        obj.setR5_num(rs.getBigDecimal("r5_num"));

        // =========================
        // R6
        // =========================
        obj.setR6_product(rs.getString("r6_product"));
        obj.setR6_num(rs.getBigDecimal("r6_num"));

        // =========================
        // R7
        // =========================
        obj.setR7_product(rs.getString("r7_product"));
        obj.setR7_num(rs.getBigDecimal("r7_num"));

        // =========================
        // R8
        // =========================
        obj.setR8_product(rs.getString("r8_product"));
        obj.setR8_num(rs.getBigDecimal("r8_num"));

        // =========================
        // R9
        // =========================
        obj.setR9_product(rs.getString("r9_product"));
        obj.setR9_num(rs.getBigDecimal("r9_num"));

        // =========================
        // R10
        // =========================
        obj.setR10_product(rs.getString("r10_product"));
        obj.setR10_num(rs.getBigDecimal("r10_num"));

        // =========================
        // R11
        // =========================
        obj.setR11_product(rs.getString("r11_product"));
        obj.setR11_num(rs.getBigDecimal("r11_num"));

        // =========================
        // R12
        // =========================
        obj.setR12_product(rs.getString("r12_product"));
        obj.setR12_num(rs.getBigDecimal("r12_num"));

        // =========================
        // R16
        // =========================
        obj.setR16_qua_disc(rs.getString("r16_qua_disc"));
        obj.setR16_product(rs.getString("r16_product"));
        obj.setR16_num(rs.getBigDecimal("r16_num"));

        // =========================
        // R17
        // =========================
        obj.setR17_qua_disc(rs.getString("r17_qua_disc"));
        obj.setR17_product(rs.getString("r17_product"));
        obj.setR17_num(rs.getBigDecimal("r17_num"));

        // =========================
        // R21
        // =========================
        obj.setR21_qua_disc(rs.getString("r21_qua_disc"));
        obj.setR21_product(rs.getString("r21_product"));
        obj.setR21_num(rs.getBigDecimal("r21_num"));

        // =========================
        // R22
        // =========================
        obj.setR22_qua_disc(rs.getString("r22_qua_disc"));
        obj.setR22_product(rs.getString("r22_product"));
        obj.setR22_num(rs.getBigDecimal("r22_num"));

        // =========================
        // R26
        // =========================
        obj.setR26_qua_disc(rs.getString("r26_qua_disc"));
        obj.setR26_product(rs.getString("r26_product"));
        obj.setR26_num(rs.getBigDecimal("r26_num"));

        // =========================
        // R27
        // =========================
        obj.setR27_qua_disc(rs.getString("r27_qua_disc"));
        obj.setR27_product(rs.getString("r27_product"));
        obj.setR27_num(rs.getBigDecimal("r27_num"));

        // =========================
        // R28
        // =========================
        obj.setR28_product(rs.getString("r28_product"));
        obj.setR28_num(rs.getBigDecimal("r28_num"));

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


public class CREDIT_RISK_Summary_Entity {
	
	private String	r5_qua_disc;
	private String	r4_product;
	private BigDecimal	r4_num;
	private String	r5_product;
	private BigDecimal	r5_num;
	private String	r6_product;
	private BigDecimal	r6_num;
	private String	r7_product;
	private BigDecimal	r7_num;
	private String	r8_product;
	private BigDecimal	r8_num;
	private String	r9_product;
	private BigDecimal	r9_num;
	private String	r10_product;
	private BigDecimal	r10_num;
	private String	r11_product;
	private BigDecimal	r11_num;
	private String	r12_product;
	private BigDecimal	r12_num;

	private String	r16_qua_disc;
	private String	r16_product;
	private BigDecimal	r16_num;
	private String	r17_qua_disc;
	private String	r17_product;
	private BigDecimal	r17_num;

	private String	r21_qua_disc;
	private String	r21_product;
	private BigDecimal	r21_num;

	private String	r22_qua_disc;
	private String	r22_product;
	private BigDecimal	r22_num;

	private String	r26_qua_disc;
	private String	r26_product;
	private BigDecimal	r26_num;

	private String	r27_qua_disc;
	private String	r27_product;
	private BigDecimal	r27_num;

	private String	r28_product;
	private BigDecimal	r28_num;


	               
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
	public String getR5_qua_disc() {
		return r5_qua_disc;
	}
	public void setR5_qua_disc(String r5_qua_disc) {
		this.r5_qua_disc = r5_qua_disc;
	}
	public String getR4_product() {
		return r4_product;
	}
	public void setR4_product(String r4_product) {
		this.r4_product = r4_product;
	}
	public BigDecimal getR4_num() {
		return r4_num;
	}
	public void setR4_num(BigDecimal r4_num) {
		this.r4_num = r4_num;
	}
	public String getR5_product() {
		return r5_product;
	}
	public void setR5_product(String r5_product) {
		this.r5_product = r5_product;
	}
	public BigDecimal getR5_num() {
		return r5_num;
	}
	public void setR5_num(BigDecimal r5_num) {
		this.r5_num = r5_num;
	}
	public String getR6_product() {
		return r6_product;
	}
	public void setR6_product(String r6_product) {
		this.r6_product = r6_product;
	}
	public BigDecimal getR6_num() {
		return r6_num;
	}
	public void setR6_num(BigDecimal r6_num) {
		this.r6_num = r6_num;
	}
	public String getR7_product() {
		return r7_product;
	}
	public void setR7_product(String r7_product) {
		this.r7_product = r7_product;
	}
	public BigDecimal getR7_num() {
		return r7_num;
	}
	public void setR7_num(BigDecimal r7_num) {
		this.r7_num = r7_num;
	}
	public String getR8_product() {
		return r8_product;
	}
	public void setR8_product(String r8_product) {
		this.r8_product = r8_product;
	}
	public BigDecimal getR8_num() {
		return r8_num;
	}
	public void setR8_num(BigDecimal r8_num) {
		this.r8_num = r8_num;
	}
	public String getR9_product() {
		return r9_product;
	}
	public void setR9_product(String r9_product) {
		this.r9_product = r9_product;
	}
	public BigDecimal getR9_num() {
		return r9_num;
	}
	public void setR9_num(BigDecimal r9_num) {
		this.r9_num = r9_num;
	}
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_num() {
		return r10_num;
	}
	public void setR10_num(BigDecimal r10_num) {
		this.r10_num = r10_num;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_num() {
		return r11_num;
	}
	public void setR11_num(BigDecimal r11_num) {
		this.r11_num = r11_num;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_num() {
		return r12_num;
	}
	public void setR12_num(BigDecimal r12_num) {
		this.r12_num = r12_num;
	}
	public String getR16_qua_disc() {
		return r16_qua_disc;
	}
	public void setR16_qua_disc(String r16_qua_disc) {
		this.r16_qua_disc = r16_qua_disc;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_num() {
		return r16_num;
	}
	public void setR16_num(BigDecimal r16_num) {
		this.r16_num = r16_num;
	}
	public String getR17_qua_disc() {
		return r17_qua_disc;
	}
	public void setR17_qua_disc(String r17_qua_disc) {
		this.r17_qua_disc = r17_qua_disc;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_num() {
		return r17_num;
	}
	public void setR17_num(BigDecimal r17_num) {
		this.r17_num = r17_num;
	}
	public String getR21_qua_disc() {
		return r21_qua_disc;
	}
	public void setR21_qua_disc(String r21_qua_disc) {
		this.r21_qua_disc = r21_qua_disc;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_num() {
		return r21_num;
	}
	public void setR21_num(BigDecimal r21_num) {
		this.r21_num = r21_num;
	}
	public String getR22_qua_disc() {
		return r22_qua_disc;
	}
	public void setR22_qua_disc(String r22_qua_disc) {
		this.r22_qua_disc = r22_qua_disc;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_num() {
		return r22_num;
	}
	public void setR22_num(BigDecimal r22_num) {
		this.r22_num = r22_num;
	}
	public String getR26_qua_disc() {
		return r26_qua_disc;
	}
	public void setR26_qua_disc(String r26_qua_disc) {
		this.r26_qua_disc = r26_qua_disc;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_num() {
		return r26_num;
	}
	public void setR26_num(BigDecimal r26_num) {
		this.r26_num = r26_num;
	}
	public String getR27_qua_disc() {
		return r27_qua_disc;
	}
	public void setR27_qua_disc(String r27_qua_disc) {
		this.r27_qua_disc = r27_qua_disc;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_num() {
		return r27_num;
	}
	public void setR27_num(BigDecimal r27_num) {
		this.r27_num = r27_num;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_num() {
		return r28_num;
	}
	public void setR28_num(BigDecimal r28_num) {
		this.r28_num = r28_num;
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


public class CREDIT_RISK_Archival_Summary_RowMapper
        implements RowMapper<CREDIT_RISK_Archival_Summary_Entity> {

    @Override
    public CREDIT_RISK_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        CREDIT_RISK_Archival_Summary_Entity obj = new CREDIT_RISK_Archival_Summary_Entity();

        // =========================
        // R4
        // =========================
        obj.setR4_product(rs.getString("r4_product"));
        obj.setR4_num(rs.getBigDecimal("r4_num"));

        // =========================
        // R5
        // =========================
        obj.setR5_qua_disc(rs.getString("r5_qua_disc"));
        obj.setR5_product(rs.getString("r5_product"));
        obj.setR5_num(rs.getBigDecimal("r5_num"));

        // =========================
        // R6
        // =========================
        obj.setR6_product(rs.getString("r6_product"));
        obj.setR6_num(rs.getBigDecimal("r6_num"));

        // =========================
        // R7
        // =========================
        obj.setR7_product(rs.getString("r7_product"));
        obj.setR7_num(rs.getBigDecimal("r7_num"));

        // =========================
        // R8
        // =========================
        obj.setR8_product(rs.getString("r8_product"));
        obj.setR8_num(rs.getBigDecimal("r8_num"));

        // =========================
        // R9
        // =========================
        obj.setR9_product(rs.getString("r9_product"));
        obj.setR9_num(rs.getBigDecimal("r9_num"));

        // =========================
        // R10
        // =========================
        obj.setR10_product(rs.getString("r10_product"));
        obj.setR10_num(rs.getBigDecimal("r10_num"));

        // =========================
        // R11
        // =========================
        obj.setR11_product(rs.getString("r11_product"));
        obj.setR11_num(rs.getBigDecimal("r11_num"));

        // =========================
        // R12
        // =========================
        obj.setR12_product(rs.getString("r12_product"));
        obj.setR12_num(rs.getBigDecimal("r12_num"));

        // =========================
        // R16
        // =========================
        obj.setR16_qua_disc(rs.getString("r16_qua_disc"));
        obj.setR16_product(rs.getString("r16_product"));
        obj.setR16_num(rs.getBigDecimal("r16_num"));

        // =========================
        // R17
        // =========================
        obj.setR17_qua_disc(rs.getString("r17_qua_disc"));
        obj.setR17_product(rs.getString("r17_product"));
        obj.setR17_num(rs.getBigDecimal("r17_num"));

        // =========================
        // R21
        // =========================
        obj.setR21_qua_disc(rs.getString("r21_qua_disc"));
        obj.setR21_product(rs.getString("r21_product"));
        obj.setR21_num(rs.getBigDecimal("r21_num"));

        // =========================
        // R22
        // =========================
        obj.setR22_qua_disc(rs.getString("r22_qua_disc"));
        obj.setR22_product(rs.getString("r22_product"));
        obj.setR22_num(rs.getBigDecimal("r22_num"));

        // =========================
        // R26
        // =========================
        obj.setR26_qua_disc(rs.getString("r26_qua_disc"));
        obj.setR26_product(rs.getString("r26_product"));
        obj.setR26_num(rs.getBigDecimal("r26_num"));

        // =========================
        // R27
        // =========================
        obj.setR27_qua_disc(rs.getString("r27_qua_disc"));
        obj.setR27_product(rs.getString("r27_product"));
        obj.setR27_num(rs.getBigDecimal("r27_num"));

        // =========================
        // R28
        // =========================
        obj.setR28_product(rs.getString("r28_product"));
        obj.setR28_num(rs.getBigDecimal("r28_num"));

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


public class CREDIT_RISK_Archival_Summary_Entity {
	
	

		
	
		private String	r5_qua_disc;
	private String	r4_product;
	private BigDecimal	r4_num;
	private String	r5_product;
	private BigDecimal	r5_num;
	private String	r6_product;
	private BigDecimal	r6_num;
	private String	r7_product;
	private BigDecimal	r7_num;
	private String	r8_product;
	private BigDecimal	r8_num;
	private String	r9_product;
	private BigDecimal	r9_num;
	private String	r10_product;
	private BigDecimal	r10_num;
	private String	r11_product;
	private BigDecimal	r11_num;
	private String	r12_product;
	private BigDecimal	r12_num;

	private String	r16_qua_disc;
	private String	r16_product;
	private BigDecimal	r16_num;
	private String	r17_qua_disc;
	private String	r17_product;
	private BigDecimal	r17_num;

	private String	r21_qua_disc;
	private String	r21_product;
	private BigDecimal	r21_num;

	private String	r22_qua_disc;
	private String	r22_product;
	private BigDecimal	r22_num;

	private String	r26_qua_disc;
	private String	r26_product;
	private BigDecimal	r26_num;

	private String	r27_qua_disc;
	private String	r27_product;
	private BigDecimal	r27_num;

	private String	r28_product;
	private BigDecimal	r28_num;

	               
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
	public String getR5_qua_disc() {
		return r5_qua_disc;
	}
	public void setR5_qua_disc(String r5_qua_disc) {
		this.r5_qua_disc = r5_qua_disc;
	}
	public String getR4_product() {
		return r4_product;
	}
	public void setR4_product(String r4_product) {
		this.r4_product = r4_product;
	}
	public BigDecimal getR4_num() {
		return r4_num;
	}
	public void setR4_num(BigDecimal r4_num) {
		this.r4_num = r4_num;
	}
	public String getR5_product() {
		return r5_product;
	}
	public void setR5_product(String r5_product) {
		this.r5_product = r5_product;
	}
	public BigDecimal getR5_num() {
		return r5_num;
	}
	public void setR5_num(BigDecimal r5_num) {
		this.r5_num = r5_num;
	}
	public String getR6_product() {
		return r6_product;
	}
	public void setR6_product(String r6_product) {
		this.r6_product = r6_product;
	}
	public BigDecimal getR6_num() {
		return r6_num;
	}
	public void setR6_num(BigDecimal r6_num) {
		this.r6_num = r6_num;
	}
	public String getR7_product() {
		return r7_product;
	}
	public void setR7_product(String r7_product) {
		this.r7_product = r7_product;
	}
	public BigDecimal getR7_num() {
		return r7_num;
	}
	public void setR7_num(BigDecimal r7_num) {
		this.r7_num = r7_num;
	}
	public String getR8_product() {
		return r8_product;
	}
	public void setR8_product(String r8_product) {
		this.r8_product = r8_product;
	}
	public BigDecimal getR8_num() {
		return r8_num;
	}
	public void setR8_num(BigDecimal r8_num) {
		this.r8_num = r8_num;
	}
	public String getR9_product() {
		return r9_product;
	}
	public void setR9_product(String r9_product) {
		this.r9_product = r9_product;
	}
	public BigDecimal getR9_num() {
		return r9_num;
	}
	public void setR9_num(BigDecimal r9_num) {
		this.r9_num = r9_num;
	}
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_num() {
		return r10_num;
	}
	public void setR10_num(BigDecimal r10_num) {
		this.r10_num = r10_num;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_num() {
		return r11_num;
	}
	public void setR11_num(BigDecimal r11_num) {
		this.r11_num = r11_num;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_num() {
		return r12_num;
	}
	public void setR12_num(BigDecimal r12_num) {
		this.r12_num = r12_num;
	}
	public String getR16_qua_disc() {
		return r16_qua_disc;
	}
	public void setR16_qua_disc(String r16_qua_disc) {
		this.r16_qua_disc = r16_qua_disc;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_num() {
		return r16_num;
	}
	public void setR16_num(BigDecimal r16_num) {
		this.r16_num = r16_num;
	}
	public String getR17_qua_disc() {
		return r17_qua_disc;
	}
	public void setR17_qua_disc(String r17_qua_disc) {
		this.r17_qua_disc = r17_qua_disc;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_num() {
		return r17_num;
	}
	public void setR17_num(BigDecimal r17_num) {
		this.r17_num = r17_num;
	}
	public String getR21_qua_disc() {
		return r21_qua_disc;
	}
	public void setR21_qua_disc(String r21_qua_disc) {
		this.r21_qua_disc = r21_qua_disc;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_num() {
		return r21_num;
	}
	public void setR21_num(BigDecimal r21_num) {
		this.r21_num = r21_num;
	}
	public String getR22_qua_disc() {
		return r22_qua_disc;
	}
	public void setR22_qua_disc(String r22_qua_disc) {
		this.r22_qua_disc = r22_qua_disc;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_num() {
		return r22_num;
	}
	public void setR22_num(BigDecimal r22_num) {
		this.r22_num = r22_num;
	}
	public String getR26_qua_disc() {
		return r26_qua_disc;
	}
	public void setR26_qua_disc(String r26_qua_disc) {
		this.r26_qua_disc = r26_qua_disc;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_num() {
		return r26_num;
	}
	public void setR26_num(BigDecimal r26_num) {
		this.r26_num = r26_num;
	}
	public String getR27_qua_disc() {
		return r27_qua_disc;
	}
	public void setR27_qua_disc(String r27_qua_disc) {
		this.r27_qua_disc = r27_qua_disc;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_num() {
		return r27_num;
	}
	public void setR27_num(BigDecimal r27_num) {
		this.r27_num = r27_num;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_num() {
		return r28_num;
	}
	public void setR28_num(BigDecimal r28_num) {
		this.r28_num = r28_num;
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
// DETAIL ENTITY  CREDIT_RISK
// =====================================================	

public class CREDIT_RISK_Detail_RowMapper implements RowMapper<CREDIT_RISK_Detail_Entity> {

    @Override
    public CREDIT_RISK_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        CREDIT_RISK_Detail_Entity obj = new CREDIT_RISK_Detail_Entity();

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

public class CREDIT_RISK_Detail_Entity {

   
	
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


public class CREDIT_RISK_Archival_Detail_RowMapper 
        implements RowMapper<CREDIT_RISK_Archival_Detail_Entity> {

    @Override
    public CREDIT_RISK_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        CREDIT_RISK_Archival_Detail_Entity obj = new CREDIT_RISK_Archival_Detail_Entity();

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

public class CREDIT_RISK_Archival_Detail_Entity {

   
	
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
// MODEL AND VIEW METHOD summary CREDIT_RISK
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
		 		 	 public ModelAndView getCREDIT_RISKView(

	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("CREDIT_RISK View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    // =====================================================
	    // ARCHIVAL MODE
	    // =====================================================

	    if ("ARCHIVAL".equals(type) && version != null) {

	        List<CREDIT_RISK_Archival_Summary_Entity> T1Master = new ArrayList<>();
	    

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

	        List<CREDIT_RISK_Summary_Entity> T1Master = new ArrayList<>();
	       

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

	    mv.setViewName("BRRS/CREDIT_RISK");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getCREDIT_RISKcurrentDtl(
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

	            List<CREDIT_RISK_Archival_Detail_Entity> archivalDetailList;

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

	            List<CREDIT_RISK_Detail_Entity> currentDetailList;

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

	    mv.setViewName("BRRS/CREDIT_RISK");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
	
//=====================================================
// MODEL AND VIEW METHOD ARCHIVAL VIEW
//=====================================================

public List<Object[]> getCREDIT_RISKArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<CREDIT_RISK_Archival_Summary_Entity> repoData =
						getarchivaldatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (CREDIT_RISK_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					CREDIT_RISK_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  CREDIT_RISK  Archival data: " + e.getMessage());
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
		ModelAndView mv = new ModelAndView("BRRS/CREDIT_RISK"); 

		if (acctNo != null) {
			CREDIT_RISK_Detail_Entity credit_riskEntity = findByDetailAcctnumber(acctNo);
			if (credit_riskEntity != null && credit_riskEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(credit_riskEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("credit_riskData", credit_riskEntity);
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

			CREDIT_RISK_Detail_Entity existing = findByDetailAcctnumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}
			
			 // Create old copy for audit comparison
			CREDIT_RISK_Detail_Entity oldcopy = new CREDIT_RISK_Detail_Entity();
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
    "UPDATE BRRS_CREDIT_RISK_DETAILTABLE " +
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
		                    "CREDIT_RISK Detail Screen",
		                    "BRRS_CREDIT_RISK_DETAIL"
		            );

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_CREDIT_RISK_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_CREDIT_RISK_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating CREDIT_RISK  record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
//=====================================================
// DETAIL EXCEL 
//=====================================================


	public byte[] getCREDIT_RISKDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  CREDIT_RISK  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getCREDIT_RISKDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("CREDIT_RISK Details ");

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
String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };


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
				List<CREDIT_RISK_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (CREDIT_RISK_Detail_Entity item : reportData) { 
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
					logger.info("No data found for CREDIT_RISK — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating CREDIT_RISK Excel", e);
				return new byte[0];
			}
		}
	
	
//=====================================================
// ARCHIVAL DETAIL EXCEL 
//=====================================================

	public byte[] getCREDIT_RISKDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for CREDIT_RISK ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("CREDIT_RISK Detail NEW");

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
				List<CREDIT_RISK_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (CREDIT_RISK_Archival_Detail_Entity item : reportData) {
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
					logger.info("No data found for CREDIT_RISK — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating CREDIT_RISK NEW Excel", e);
				return new byte[0];
			}
		}
		
		
//=====================================================
// Summary EXCEL 
//=====================================================

	public byte[] getCREDIT_RISKExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.CREDIT_RISK");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelCREDIT_RISKARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<CREDIT_RISK_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		
		
			System.out.println("DATA SIZE IS : "+dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  CREDIT_RISK report. Returning empty result.");
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
						CREDIT_RISK_Summary_Entity record = dataList.get(i);
					
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

		// R4
					row = sheet.getRow(3);
					Cell cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR4_num() != null ? record.getR4_num().doubleValue() : 0);



					// R5
					row = sheet.getRow(4);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR5_num() != null ? record.getR5_num().doubleValue() : 0);

					// R6
					row = sheet.getRow(5);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR6_num() != null ? record.getR6_num().doubleValue() : 0);

					// R7
					row = sheet.getRow(6);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR7_num() != null ? record.getR7_num().doubleValue() : 0);

					// R8
					row = sheet.getRow(7);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR8_num() != null ? record.getR8_num().doubleValue() : 0);

					// R9
					row = sheet.getRow(8);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR9_num() != null ? record.getR9_num().doubleValue() : 0);

					// R10
					row = sheet.getRow(9);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR10_num() != null ? record.getR10_num().doubleValue() : 0);

					// R11
					row = sheet.getRow(10);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR11_num() != null ? record.getR11_num().doubleValue() : 0);

					// R12
					row = sheet.getRow(11);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR12_num() != null ? record.getR12_num().doubleValue() : 0);
					

					// R16
					row = sheet.getRow(15);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR16_num() != null ? record.getR16_num().doubleValue() : 0);

					// R17
					row = sheet.getRow(16);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR17_num() != null ? record.getR17_num().doubleValue() : 0);

					
					// R21
					row = sheet.getRow(20);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR21_num() != null ? record.getR21_num().doubleValue() : 0);

					// R22
					row = sheet.getRow(21);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR22_num() != null ? record.getR22_num().doubleValue() : 0);

					
					
					// R26
					row = sheet.getRow(25);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR26_num() != null ? record.getR26_num().doubleValue() : 0);

					// R27
					row = sheet.getRow(26);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR27_num() != null ? record.getR27_num().doubleValue() : 0);

					// R28
					row = sheet.getRow(27);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR28_num() != null ? record.getR28_num().doubleValue() : 0);

					
				
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
												auditService.createBusinessAudit(userid, "DOWNLOAD", "CREDIT_RISK  SUMMARY", null, "BRRS_CREDIT_RISK_SUMMARYTABLE");
											}

				return out.toByteArray();
			}

		}




//=====================================================
//ARCHIVAL SUMMARY EXCEL 
//=====================================================



				public byte[] getExcelCREDIT_RISKARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {

			}

			List<CREDIT_RISK_Archival_Summary_Entity> dataList = 
					getDataByDateListArchival(dateformat.parse(todate), version);
		
	    

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for CREDIT_RISK new report. Returning empty result.");
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
						CREDIT_RISK_Archival_Summary_Entity record = dataList.get(i);
					    
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


	
					// R4
					row = sheet.getRow(3);
					Cell cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR4_num() != null ? record.getR4_num().doubleValue() : 0);



					// R5
					row = sheet.getRow(4);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR5_num() != null ? record.getR5_num().doubleValue() : 0);

					// R6
					row = sheet.getRow(5);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR6_num() != null ? record.getR6_num().doubleValue() : 0);

					// R7
					row = sheet.getRow(6);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR7_num() != null ? record.getR7_num().doubleValue() : 0);

					// R8
					row = sheet.getRow(7);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR8_num() != null ? record.getR8_num().doubleValue() : 0);

					// R9
					row = sheet.getRow(8);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR9_num() != null ? record.getR9_num().doubleValue() : 0);

					// R10
					row = sheet.getRow(9);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR10_num() != null ? record.getR10_num().doubleValue() : 0);

					// R11
					row = sheet.getRow(10);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR11_num() != null ? record.getR11_num().doubleValue() : 0);

					// R12
					row = sheet.getRow(11);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR12_num() != null ? record.getR12_num().doubleValue() : 0);
					

					// R16
					row = sheet.getRow(15);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR16_num() != null ? record.getR16_num().doubleValue() : 0);

					// R17
					row = sheet.getRow(16);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR17_num() != null ? record.getR17_num().doubleValue() : 0);

					
					// R21
					row = sheet.getRow(20);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR21_num() != null ? record.getR21_num().doubleValue() : 0);

					// R22
					row = sheet.getRow(21);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR22_num() != null ? record.getR22_num().doubleValue() : 0);

					
					
					// R26
					row = sheet.getRow(25);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR26_num() != null ? record.getR26_num().doubleValue() : 0);

					// R27
					row = sheet.getRow(26);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR27_num() != null ? record.getR27_num().doubleValue() : 0);

					// R28
					row = sheet.getRow(27);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR28_num() != null ? record.getR28_num().doubleValue() : 0);
					

				
						
						
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
										auditService.createBusinessAudit(userid, "DOWNLOAD", "CREDIT_RISK ARCHIVAL SUMMARY", null, "BRRS_CREDIT_RISK_ARCHIVALTABLE_SUMMARY");
									}

				return out.toByteArray();
			}

		}
		
		
		
	}