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

public class BRRS_CAP_RATIO_BUFFER_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_CAP_RATIO_BUFFER_ReportService.class);

	


	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

  
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

	
	
	
	



	


	// Fetch data by report date - Summary
	
	
public List<CAP_RATIO_BUFFER_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_CAP_RATIO_BUFFER_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new CAP_RATIO_BUFFER_RowMapper()
    );
}


     // archival

public List<Object[]> getCAP_RATIO_BUFFERarchival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_CAP_RATIO_BUFFER_ARCHIVALTABLE_SUMMARY ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[] {
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

 public List<CAP_RATIO_BUFFER_Archival_Summary_Entity> getdatabydateListarchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_CAP_RATIO_BUFFER_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new CAP_RATIO_BUFFER_Archival_RowMapper()
    );
}

public List<CAP_RATIO_BUFFER_Archival_Summary_Entity> getdatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_CAP_RATIO_BUFFER_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new CAP_RATIO_BUFFER_Archival_RowMapper()
    );
}


//----------detail 


public List<CAP_RATIO_BUFFER_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_CAP_RATIO_BUFFER_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new CAP_RATIO_BUFFER_Detail_RowMapper()
    );
}

public List<CAP_RATIO_BUFFER_Detail_Entity> getDetaildatabydateList(
        Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_CAP_RATIO_BUFFER_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, offset, limit},
            new CAP_RATIO_BUFFER_Detail_RowMapper()
    );
}

public int getdatacount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_CAP_RATIO_BUFFER_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            Integer.class
    );
}

public List<CAP_RATIO_BUFFER_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel, String reportAddlCriteria1, Date reportDate) {

    String sql = "SELECT * FROM BRRS_CAP_RATIO_BUFFER_DETAILTABLE " +
                 "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate},
            new CAP_RATIO_BUFFER_Detail_RowMapper()
    );
}

public CAP_RATIO_BUFFER_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_CAP_RATIO_BUFFER_DETAILTABLE WHERE ACCT_NUMBER = ?";

    List<CAP_RATIO_BUFFER_Detail_Entity> list = jdbcTemplate.query(
            sql,
            new Object[]{acctNumber},
            new CAP_RATIO_BUFFER_Detail_RowMapper()
    );

    return list.isEmpty() ? null : list.get(0);
}

//============archival detail 

public List<CAP_RATIO_BUFFER_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate, String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_CAP_RATIO_BUFFER_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, dataEntryVersion},
            new CAP_RATIO_BUFFER_Archival_Detail_RowMapper()
    );
}

public List<CAP_RATIO_BUFFER_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_CAP_RATIO_BUFFER_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate, dataEntryVersion},
            new CAP_RATIO_BUFFER_Archival_Detail_RowMapper()
    );
}


//======================entity cls summary 

public class CAP_RATIO_BUFFER_RowMapper 
        implements RowMapper<CAP_RATIO_BUFFER_Summary_Entity> {

    @Override
    public CAP_RATIO_BUFFER_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        CAP_RATIO_BUFFER_Summary_Entity obj = new CAP_RATIO_BUFFER_Summary_Entity();

        // =========================
        // R2 - R8
        // =========================
        obj.setR2_cap_ratio_buff(rs.getString("r2_cap_ratio_buff"));
        obj.setR2_cap_ratio_buff_amt(rs.getBigDecimal("r2_cap_ratio_buff_amt"));

        obj.setR3_cap_ratio_buff(rs.getString("r3_cap_ratio_buff"));
        obj.setR3_cap_ratio_buff_amt(rs.getBigDecimal("r3_cap_ratio_buff_amt"));

        obj.setR4_cap_ratio_buff(rs.getString("r4_cap_ratio_buff"));
        obj.setR4_cap_ratio_buff_amt(rs.getBigDecimal("r4_cap_ratio_buff_amt"));

        obj.setR5_cap_ratio_buff(rs.getString("r5_cap_ratio_buff"));
        obj.setR5_cap_ratio_buff_amt(rs.getBigDecimal("r5_cap_ratio_buff_amt"));

        obj.setR6_cap_ratio_buff(rs.getString("r6_cap_ratio_buff"));
        obj.setR6_cap_ratio_buff_amt(rs.getBigDecimal("r6_cap_ratio_buff_amt"));

        obj.setR7_cap_ratio_buff(rs.getString("r7_cap_ratio_buff"));
        obj.setR7_cap_ratio_buff_amt(rs.getBigDecimal("r7_cap_ratio_buff_amt"));

        obj.setR8_cap_ratio_buff(rs.getString("r8_cap_ratio_buff"));
        obj.setR8_cap_ratio_buff_amt(rs.getBigDecimal("r8_cap_ratio_buff_amt"));

        // =========================
        // R11 - R13
        // =========================
        obj.setR11_cap_ratio_buff(rs.getString("r11_cap_ratio_buff"));
        obj.setR11_cap_ratio_buff_amt(rs.getBigDecimal("r11_cap_ratio_buff_amt"));

        obj.setR12_cap_ratio_buff(rs.getString("r12_cap_ratio_buff"));
        obj.setR12_cap_ratio_buff_amt(rs.getBigDecimal("r12_cap_ratio_buff_amt"));

        obj.setR13_cap_ratio_buff(rs.getString("r13_cap_ratio_buff"));
        obj.setR13_cap_ratio_buff_amt(rs.getBigDecimal("r13_cap_ratio_buff_amt"));

        // =========================
        // R15 - R18
        // =========================
        obj.setR15_cap_ratio_buff(rs.getString("r15_cap_ratio_buff"));
        obj.setR15_cap_ratio_buff_amt(rs.getBigDecimal("r15_cap_ratio_buff_amt"));

        obj.setR16_cap_ratio_buff(rs.getString("r16_cap_ratio_buff"));
        obj.setR16_cap_ratio_buff_amt(rs.getBigDecimal("r16_cap_ratio_buff_amt"));

        obj.setR17_cap_ratio_buff(rs.getString("r17_cap_ratio_buff"));
        obj.setR17_cap_ratio_buff_amt(rs.getBigDecimal("r17_cap_ratio_buff_amt"));

        obj.setR18_cap_ratio_buff(rs.getString("r18_cap_ratio_buff"));
        obj.setR18_cap_ratio_buff_amt(rs.getBigDecimal("r18_cap_ratio_buff_amt"));

        // =========================
        // R20 - R23
        // =========================
        obj.setR20_cap_ratio_buff(rs.getString("r20_cap_ratio_buff"));
        obj.setR20_cap_ratio_buff_amt(rs.getBigDecimal("r20_cap_ratio_buff_amt"));

        obj.setR21_cap_ratio_buff(rs.getString("r21_cap_ratio_buff"));
        obj.setR21_cap_ratio_buff_amt(rs.getBigDecimal("r21_cap_ratio_buff_amt"));

        obj.setR22_cap_ratio_buff(rs.getString("r22_cap_ratio_buff"));
        obj.setR22_cap_ratio_buff_amt(rs.getBigDecimal("r22_cap_ratio_buff_amt"));

        obj.setR23_cap_ratio_buff(rs.getString("r23_cap_ratio_buff"));
        obj.setR23_cap_ratio_buff_amt(rs.getBigDecimal("r23_cap_ratio_buff_amt"));

        // =========================
        // R25 - R30
        // =========================
        obj.setR25_cap_ratio_buff(rs.getString("r25_cap_ratio_buff"));
        obj.setR25_cap_ratio_buff_amt(rs.getBigDecimal("r25_cap_ratio_buff_amt"));

        obj.setR26_cap_ratio_buff(rs.getString("r26_cap_ratio_buff"));
        obj.setR26_cap_ratio_buff_amt(rs.getBigDecimal("r26_cap_ratio_buff_amt"));

        obj.setR27_cap_ratio_buff(rs.getString("r27_cap_ratio_buff"));
        obj.setR27_cap_ratio_buff_amt(rs.getBigDecimal("r27_cap_ratio_buff_amt"));

        obj.setR28_cap_ratio_buff(rs.getString("r28_cap_ratio_buff"));
        obj.setR28_cap_ratio_buff_amt(rs.getBigDecimal("r28_cap_ratio_buff_amt"));

        obj.setR29_cap_ratio_buff(rs.getString("r29_cap_ratio_buff"));
        obj.setR29_cap_ratio_buff_amt(rs.getBigDecimal("r29_cap_ratio_buff_amt"));

        obj.setR30_cap_ratio_buff(rs.getString("r30_cap_ratio_buff"));
        obj.setR30_cap_ratio_buff_amt(rs.getBigDecimal("r30_cap_ratio_buff_amt"));

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

public class CAP_RATIO_BUFFER_Summary_Entity {
	
	

		
	
	private String	r2_cap_ratio_buff;
	private BigDecimal	r2_cap_ratio_buff_amt;
	private String	r3_cap_ratio_buff;
	private BigDecimal	r3_cap_ratio_buff_amt;
	private String	r4_cap_ratio_buff;
	private BigDecimal	r4_cap_ratio_buff_amt;
	private String	r5_cap_ratio_buff;
	private BigDecimal	r5_cap_ratio_buff_amt;
	private String	r6_cap_ratio_buff;
	private BigDecimal	r6_cap_ratio_buff_amt;
	private String	r7_cap_ratio_buff;
	private BigDecimal	r7_cap_ratio_buff_amt;
	private String	r8_cap_ratio_buff;
	private BigDecimal	r8_cap_ratio_buff_amt;

	private String	r11_cap_ratio_buff;
	private BigDecimal	r11_cap_ratio_buff_amt;
	private String	r12_cap_ratio_buff;
	private BigDecimal	r12_cap_ratio_buff_amt;
	private String	r13_cap_ratio_buff;
	private BigDecimal	r13_cap_ratio_buff_amt;

	private String	r15_cap_ratio_buff;
	private BigDecimal	r15_cap_ratio_buff_amt;
	private String	r16_cap_ratio_buff;
	private BigDecimal	r16_cap_ratio_buff_amt;
	private String	r17_cap_ratio_buff;
	private BigDecimal	r17_cap_ratio_buff_amt;
	private String	r18_cap_ratio_buff;
	private BigDecimal	r18_cap_ratio_buff_amt;

	private String	r20_cap_ratio_buff;
	private BigDecimal	r20_cap_ratio_buff_amt;
	private String	r21_cap_ratio_buff;
	private BigDecimal	r21_cap_ratio_buff_amt;
	private String	r22_cap_ratio_buff;
	private BigDecimal	r22_cap_ratio_buff_amt;
	private String	r23_cap_ratio_buff;
	private BigDecimal	r23_cap_ratio_buff_amt;

	private String	r25_cap_ratio_buff;
	private BigDecimal	r25_cap_ratio_buff_amt;
	private String	r26_cap_ratio_buff;
	private BigDecimal	r26_cap_ratio_buff_amt;
	private String	r27_cap_ratio_buff;
	private BigDecimal	r27_cap_ratio_buff_amt;
	private String	r28_cap_ratio_buff;
	private BigDecimal	r28_cap_ratio_buff_amt;
	private String	r29_cap_ratio_buff;
	private BigDecimal	r29_cap_ratio_buff_amt;
	private String	r30_cap_ratio_buff;
	private BigDecimal	r30_cap_ratio_buff_amt;
	
	
	
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
	public String getR2_cap_ratio_buff() {
		return r2_cap_ratio_buff;
	}
	public void setR2_cap_ratio_buff(String r2_cap_ratio_buff) {
		this.r2_cap_ratio_buff = r2_cap_ratio_buff;
	}
	public BigDecimal getR2_cap_ratio_buff_amt() {
		return r2_cap_ratio_buff_amt;
	}
	public void setR2_cap_ratio_buff_amt(BigDecimal r2_cap_ratio_buff_amt) {
		this.r2_cap_ratio_buff_amt = r2_cap_ratio_buff_amt;
	}
	public String getR3_cap_ratio_buff() {
		return r3_cap_ratio_buff;
	}
	public void setR3_cap_ratio_buff(String r3_cap_ratio_buff) {
		this.r3_cap_ratio_buff = r3_cap_ratio_buff;
	}
	public BigDecimal getR3_cap_ratio_buff_amt() {
		return r3_cap_ratio_buff_amt;
	}
	public void setR3_cap_ratio_buff_amt(BigDecimal r3_cap_ratio_buff_amt) {
		this.r3_cap_ratio_buff_amt = r3_cap_ratio_buff_amt;
	}
	public String getR4_cap_ratio_buff() {
		return r4_cap_ratio_buff;
	}
	public void setR4_cap_ratio_buff(String r4_cap_ratio_buff) {
		this.r4_cap_ratio_buff = r4_cap_ratio_buff;
	}
	public BigDecimal getR4_cap_ratio_buff_amt() {
		return r4_cap_ratio_buff_amt;
	}
	public void setR4_cap_ratio_buff_amt(BigDecimal r4_cap_ratio_buff_amt) {
		this.r4_cap_ratio_buff_amt = r4_cap_ratio_buff_amt;
	}
	public String getR5_cap_ratio_buff() {
		return r5_cap_ratio_buff;
	}
	public void setR5_cap_ratio_buff(String r5_cap_ratio_buff) {
		this.r5_cap_ratio_buff = r5_cap_ratio_buff;
	}
	public BigDecimal getR5_cap_ratio_buff_amt() {
		return r5_cap_ratio_buff_amt;
	}
	public void setR5_cap_ratio_buff_amt(BigDecimal r5_cap_ratio_buff_amt) {
		this.r5_cap_ratio_buff_amt = r5_cap_ratio_buff_amt;
	}
	public String getR6_cap_ratio_buff() {
		return r6_cap_ratio_buff;
	}
	public void setR6_cap_ratio_buff(String r6_cap_ratio_buff) {
		this.r6_cap_ratio_buff = r6_cap_ratio_buff;
	}
	public BigDecimal getR6_cap_ratio_buff_amt() {
		return r6_cap_ratio_buff_amt;
	}
	public void setR6_cap_ratio_buff_amt(BigDecimal r6_cap_ratio_buff_amt) {
		this.r6_cap_ratio_buff_amt = r6_cap_ratio_buff_amt;
	}
	public String getR7_cap_ratio_buff() {
		return r7_cap_ratio_buff;
	}
	public void setR7_cap_ratio_buff(String r7_cap_ratio_buff) {
		this.r7_cap_ratio_buff = r7_cap_ratio_buff;
	}
	public BigDecimal getR7_cap_ratio_buff_amt() {
		return r7_cap_ratio_buff_amt;
	}
	public void setR7_cap_ratio_buff_amt(BigDecimal r7_cap_ratio_buff_amt) {
		this.r7_cap_ratio_buff_amt = r7_cap_ratio_buff_amt;
	}
	public String getR8_cap_ratio_buff() {
		return r8_cap_ratio_buff;
	}
	public void setR8_cap_ratio_buff(String r8_cap_ratio_buff) {
		this.r8_cap_ratio_buff = r8_cap_ratio_buff;
	}
	public BigDecimal getR8_cap_ratio_buff_amt() {
		return r8_cap_ratio_buff_amt;
	}
	public void setR8_cap_ratio_buff_amt(BigDecimal r8_cap_ratio_buff_amt) {
		this.r8_cap_ratio_buff_amt = r8_cap_ratio_buff_amt;
	}
	public String getR11_cap_ratio_buff() {
		return r11_cap_ratio_buff;
	}
	public void setR11_cap_ratio_buff(String r11_cap_ratio_buff) {
		this.r11_cap_ratio_buff = r11_cap_ratio_buff;
	}
	public BigDecimal getR11_cap_ratio_buff_amt() {
		return r11_cap_ratio_buff_amt;
	}
	public void setR11_cap_ratio_buff_amt(BigDecimal r11_cap_ratio_buff_amt) {
		this.r11_cap_ratio_buff_amt = r11_cap_ratio_buff_amt;
	}
	public String getR12_cap_ratio_buff() {
		return r12_cap_ratio_buff;
	}
	public void setR12_cap_ratio_buff(String r12_cap_ratio_buff) {
		this.r12_cap_ratio_buff = r12_cap_ratio_buff;
	}
	public BigDecimal getR12_cap_ratio_buff_amt() {
		return r12_cap_ratio_buff_amt;
	}
	public void setR12_cap_ratio_buff_amt(BigDecimal r12_cap_ratio_buff_amt) {
		this.r12_cap_ratio_buff_amt = r12_cap_ratio_buff_amt;
	}
	public String getR13_cap_ratio_buff() {
		return r13_cap_ratio_buff;
	}
	public void setR13_cap_ratio_buff(String r13_cap_ratio_buff) {
		this.r13_cap_ratio_buff = r13_cap_ratio_buff;
	}
	public BigDecimal getR13_cap_ratio_buff_amt() {
		return r13_cap_ratio_buff_amt;
	}
	public void setR13_cap_ratio_buff_amt(BigDecimal r13_cap_ratio_buff_amt) {
		this.r13_cap_ratio_buff_amt = r13_cap_ratio_buff_amt;
	}
	public String getR15_cap_ratio_buff() {
		return r15_cap_ratio_buff;
	}
	public void setR15_cap_ratio_buff(String r15_cap_ratio_buff) {
		this.r15_cap_ratio_buff = r15_cap_ratio_buff;
	}
	public BigDecimal getR15_cap_ratio_buff_amt() {
		return r15_cap_ratio_buff_amt;
	}
	public void setR15_cap_ratio_buff_amt(BigDecimal r15_cap_ratio_buff_amt) {
		this.r15_cap_ratio_buff_amt = r15_cap_ratio_buff_amt;
	}
	public String getR16_cap_ratio_buff() {
		return r16_cap_ratio_buff;
	}
	public void setR16_cap_ratio_buff(String r16_cap_ratio_buff) {
		this.r16_cap_ratio_buff = r16_cap_ratio_buff;
	}
	public BigDecimal getR16_cap_ratio_buff_amt() {
		return r16_cap_ratio_buff_amt;
	}
	public void setR16_cap_ratio_buff_amt(BigDecimal r16_cap_ratio_buff_amt) {
		this.r16_cap_ratio_buff_amt = r16_cap_ratio_buff_amt;
	}
	public String getR17_cap_ratio_buff() {
		return r17_cap_ratio_buff;
	}
	public void setR17_cap_ratio_buff(String r17_cap_ratio_buff) {
		this.r17_cap_ratio_buff = r17_cap_ratio_buff;
	}
	public BigDecimal getR17_cap_ratio_buff_amt() {
		return r17_cap_ratio_buff_amt;
	}
	public void setR17_cap_ratio_buff_amt(BigDecimal r17_cap_ratio_buff_amt) {
		this.r17_cap_ratio_buff_amt = r17_cap_ratio_buff_amt;
	}
	public String getR18_cap_ratio_buff() {
		return r18_cap_ratio_buff;
	}
	public void setR18_cap_ratio_buff(String r18_cap_ratio_buff) {
		this.r18_cap_ratio_buff = r18_cap_ratio_buff;
	}
	public BigDecimal getR18_cap_ratio_buff_amt() {
		return r18_cap_ratio_buff_amt;
	}
	public void setR18_cap_ratio_buff_amt(BigDecimal r18_cap_ratio_buff_amt) {
		this.r18_cap_ratio_buff_amt = r18_cap_ratio_buff_amt;
	}
	public String getR20_cap_ratio_buff() {
		return r20_cap_ratio_buff;
	}
	public void setR20_cap_ratio_buff(String r20_cap_ratio_buff) {
		this.r20_cap_ratio_buff = r20_cap_ratio_buff;
	}
	public BigDecimal getR20_cap_ratio_buff_amt() {
		return r20_cap_ratio_buff_amt;
	}
	public void setR20_cap_ratio_buff_amt(BigDecimal r20_cap_ratio_buff_amt) {
		this.r20_cap_ratio_buff_amt = r20_cap_ratio_buff_amt;
	}
	public String getR21_cap_ratio_buff() {
		return r21_cap_ratio_buff;
	}
	public void setR21_cap_ratio_buff(String r21_cap_ratio_buff) {
		this.r21_cap_ratio_buff = r21_cap_ratio_buff;
	}
	public BigDecimal getR21_cap_ratio_buff_amt() {
		return r21_cap_ratio_buff_amt;
	}
	public void setR21_cap_ratio_buff_amt(BigDecimal r21_cap_ratio_buff_amt) {
		this.r21_cap_ratio_buff_amt = r21_cap_ratio_buff_amt;
	}
	public String getR22_cap_ratio_buff() {
		return r22_cap_ratio_buff;
	}
	public void setR22_cap_ratio_buff(String r22_cap_ratio_buff) {
		this.r22_cap_ratio_buff = r22_cap_ratio_buff;
	}
	public BigDecimal getR22_cap_ratio_buff_amt() {
		return r22_cap_ratio_buff_amt;
	}
	public void setR22_cap_ratio_buff_amt(BigDecimal r22_cap_ratio_buff_amt) {
		this.r22_cap_ratio_buff_amt = r22_cap_ratio_buff_amt;
	}
	public String getR23_cap_ratio_buff() {
		return r23_cap_ratio_buff;
	}
	public void setR23_cap_ratio_buff(String r23_cap_ratio_buff) {
		this.r23_cap_ratio_buff = r23_cap_ratio_buff;
	}
	public BigDecimal getR23_cap_ratio_buff_amt() {
		return r23_cap_ratio_buff_amt;
	}
	public void setR23_cap_ratio_buff_amt(BigDecimal r23_cap_ratio_buff_amt) {
		this.r23_cap_ratio_buff_amt = r23_cap_ratio_buff_amt;
	}
	public String getR25_cap_ratio_buff() {
		return r25_cap_ratio_buff;
	}
	public void setR25_cap_ratio_buff(String r25_cap_ratio_buff) {
		this.r25_cap_ratio_buff = r25_cap_ratio_buff;
	}
	public BigDecimal getR25_cap_ratio_buff_amt() {
		return r25_cap_ratio_buff_amt;
	}
	public void setR25_cap_ratio_buff_amt(BigDecimal r25_cap_ratio_buff_amt) {
		this.r25_cap_ratio_buff_amt = r25_cap_ratio_buff_amt;
	}
	public String getR26_cap_ratio_buff() {
		return r26_cap_ratio_buff;
	}
	public void setR26_cap_ratio_buff(String r26_cap_ratio_buff) {
		this.r26_cap_ratio_buff = r26_cap_ratio_buff;
	}
	public BigDecimal getR26_cap_ratio_buff_amt() {
		return r26_cap_ratio_buff_amt;
	}
	public void setR26_cap_ratio_buff_amt(BigDecimal r26_cap_ratio_buff_amt) {
		this.r26_cap_ratio_buff_amt = r26_cap_ratio_buff_amt;
	}
	public String getR27_cap_ratio_buff() {
		return r27_cap_ratio_buff;
	}
	public void setR27_cap_ratio_buff(String r27_cap_ratio_buff) {
		this.r27_cap_ratio_buff = r27_cap_ratio_buff;
	}
	public BigDecimal getR27_cap_ratio_buff_amt() {
		return r27_cap_ratio_buff_amt;
	}
	public void setR27_cap_ratio_buff_amt(BigDecimal r27_cap_ratio_buff_amt) {
		this.r27_cap_ratio_buff_amt = r27_cap_ratio_buff_amt;
	}
	public String getR28_cap_ratio_buff() {
		return r28_cap_ratio_buff;
	}
	public void setR28_cap_ratio_buff(String r28_cap_ratio_buff) {
		this.r28_cap_ratio_buff = r28_cap_ratio_buff;
	}
	public BigDecimal getR28_cap_ratio_buff_amt() {
		return r28_cap_ratio_buff_amt;
	}
	public void setR28_cap_ratio_buff_amt(BigDecimal r28_cap_ratio_buff_amt) {
		this.r28_cap_ratio_buff_amt = r28_cap_ratio_buff_amt;
	}
	public String getR29_cap_ratio_buff() {
		return r29_cap_ratio_buff;
	}
	public void setR29_cap_ratio_buff(String r29_cap_ratio_buff) {
		this.r29_cap_ratio_buff = r29_cap_ratio_buff;
	}
	public BigDecimal getR29_cap_ratio_buff_amt() {
		return r29_cap_ratio_buff_amt;
	}
	public void setR29_cap_ratio_buff_amt(BigDecimal r29_cap_ratio_buff_amt) {
		this.r29_cap_ratio_buff_amt = r29_cap_ratio_buff_amt;
	}
	public String getR30_cap_ratio_buff() {
		return r30_cap_ratio_buff;
	}
	public void setR30_cap_ratio_buff(String r30_cap_ratio_buff) {
		this.r30_cap_ratio_buff = r30_cap_ratio_buff;
	}
	public BigDecimal getR30_cap_ratio_buff_amt() {
		return r30_cap_ratio_buff_amt;
	}
	public void setR30_cap_ratio_buff_amt(BigDecimal r30_cap_ratio_buff_amt) {
		this.r30_cap_ratio_buff_amt = r30_cap_ratio_buff_amt;
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


//======================archival summary 


public class CAP_RATIO_BUFFER_Archival_RowMapper
        implements RowMapper<CAP_RATIO_BUFFER_Archival_Summary_Entity> {

    @Override
    public CAP_RATIO_BUFFER_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        CAP_RATIO_BUFFER_Archival_Summary_Entity obj =
                new CAP_RATIO_BUFFER_Archival_Summary_Entity();

        // =========================
        // R2 - R8
        // =========================
        obj.setR2_cap_ratio_buff(rs.getString("r2_cap_ratio_buff"));
        obj.setR2_cap_ratio_buff_amt(rs.getBigDecimal("r2_cap_ratio_buff_amt"));

        obj.setR3_cap_ratio_buff(rs.getString("r3_cap_ratio_buff"));
        obj.setR3_cap_ratio_buff_amt(rs.getBigDecimal("r3_cap_ratio_buff_amt"));

        obj.setR4_cap_ratio_buff(rs.getString("r4_cap_ratio_buff"));
        obj.setR4_cap_ratio_buff_amt(rs.getBigDecimal("r4_cap_ratio_buff_amt"));

        obj.setR5_cap_ratio_buff(rs.getString("r5_cap_ratio_buff"));
        obj.setR5_cap_ratio_buff_amt(rs.getBigDecimal("r5_cap_ratio_buff_amt"));

        obj.setR6_cap_ratio_buff(rs.getString("r6_cap_ratio_buff"));
        obj.setR6_cap_ratio_buff_amt(rs.getBigDecimal("r6_cap_ratio_buff_amt"));

        obj.setR7_cap_ratio_buff(rs.getString("r7_cap_ratio_buff"));
        obj.setR7_cap_ratio_buff_amt(rs.getBigDecimal("r7_cap_ratio_buff_amt"));

        obj.setR8_cap_ratio_buff(rs.getString("r8_cap_ratio_buff"));
        obj.setR8_cap_ratio_buff_amt(rs.getBigDecimal("r8_cap_ratio_buff_amt"));

        // =========================
        // R11 - R13
        // =========================
        obj.setR11_cap_ratio_buff(rs.getString("r11_cap_ratio_buff"));
        obj.setR11_cap_ratio_buff_amt(rs.getBigDecimal("r11_cap_ratio_buff_amt"));

        obj.setR12_cap_ratio_buff(rs.getString("r12_cap_ratio_buff"));
        obj.setR12_cap_ratio_buff_amt(rs.getBigDecimal("r12_cap_ratio_buff_amt"));

        obj.setR13_cap_ratio_buff(rs.getString("r13_cap_ratio_buff"));
        obj.setR13_cap_ratio_buff_amt(rs.getBigDecimal("r13_cap_ratio_buff_amt"));

        // =========================
        // R15 - R18
        // =========================
        obj.setR15_cap_ratio_buff(rs.getString("r15_cap_ratio_buff"));
        obj.setR15_cap_ratio_buff_amt(rs.getBigDecimal("r15_cap_ratio_buff_amt"));

        obj.setR16_cap_ratio_buff(rs.getString("r16_cap_ratio_buff"));
        obj.setR16_cap_ratio_buff_amt(rs.getBigDecimal("r16_cap_ratio_buff_amt"));

        obj.setR17_cap_ratio_buff(rs.getString("r17_cap_ratio_buff"));
        obj.setR17_cap_ratio_buff_amt(rs.getBigDecimal("r17_cap_ratio_buff_amt"));

        obj.setR18_cap_ratio_buff(rs.getString("r18_cap_ratio_buff"));
        obj.setR18_cap_ratio_buff_amt(rs.getBigDecimal("r18_cap_ratio_buff_amt"));

        // =========================
        // R20 - R23
        // =========================
        obj.setR20_cap_ratio_buff(rs.getString("r20_cap_ratio_buff"));
        obj.setR20_cap_ratio_buff_amt(rs.getBigDecimal("r20_cap_ratio_buff_amt"));

        obj.setR21_cap_ratio_buff(rs.getString("r21_cap_ratio_buff"));
        obj.setR21_cap_ratio_buff_amt(rs.getBigDecimal("r21_cap_ratio_buff_amt"));

        obj.setR22_cap_ratio_buff(rs.getString("r22_cap_ratio_buff"));
        obj.setR22_cap_ratio_buff_amt(rs.getBigDecimal("r22_cap_ratio_buff_amt"));

        obj.setR23_cap_ratio_buff(rs.getString("r23_cap_ratio_buff"));
        obj.setR23_cap_ratio_buff_amt(rs.getBigDecimal("r23_cap_ratio_buff_amt"));

        // =========================
        // R25 - R30
        // =========================
        obj.setR25_cap_ratio_buff(rs.getString("r25_cap_ratio_buff"));
        obj.setR25_cap_ratio_buff_amt(rs.getBigDecimal("r25_cap_ratio_buff_amt"));

        obj.setR26_cap_ratio_buff(rs.getString("r26_cap_ratio_buff"));
        obj.setR26_cap_ratio_buff_amt(rs.getBigDecimal("r26_cap_ratio_buff_amt"));

        obj.setR27_cap_ratio_buff(rs.getString("r27_cap_ratio_buff"));
        obj.setR27_cap_ratio_buff_amt(rs.getBigDecimal("r27_cap_ratio_buff_amt"));

        obj.setR28_cap_ratio_buff(rs.getString("r28_cap_ratio_buff"));
        obj.setR28_cap_ratio_buff_amt(rs.getBigDecimal("r28_cap_ratio_buff_amt"));

        obj.setR29_cap_ratio_buff(rs.getString("r29_cap_ratio_buff"));
        obj.setR29_cap_ratio_buff_amt(rs.getBigDecimal("r29_cap_ratio_buff_amt"));

        obj.setR30_cap_ratio_buff(rs.getString("r30_cap_ratio_buff"));
        obj.setR30_cap_ratio_buff_amt(rs.getBigDecimal("r30_cap_ratio_buff_amt"));

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

public class CAP_RATIO_BUFFER_Archival_Summary_Entity {
	
	

		
	private String	r2_cap_ratio_buff;
	private BigDecimal	r2_cap_ratio_buff_amt;
	private String	r3_cap_ratio_buff;
	private BigDecimal	r3_cap_ratio_buff_amt;
	private String	r4_cap_ratio_buff;
	private BigDecimal	r4_cap_ratio_buff_amt;
	private String	r5_cap_ratio_buff;
	private BigDecimal	r5_cap_ratio_buff_amt;
	private String	r6_cap_ratio_buff;
	private BigDecimal	r6_cap_ratio_buff_amt;
	private String	r7_cap_ratio_buff;
	private BigDecimal	r7_cap_ratio_buff_amt;
	private String	r8_cap_ratio_buff;
	private BigDecimal	r8_cap_ratio_buff_amt;

	private String	r11_cap_ratio_buff;
	private BigDecimal	r11_cap_ratio_buff_amt;
	private String	r12_cap_ratio_buff;
	private BigDecimal	r12_cap_ratio_buff_amt;
	private String	r13_cap_ratio_buff;
	private BigDecimal	r13_cap_ratio_buff_amt;

	private String	r15_cap_ratio_buff;
	private BigDecimal	r15_cap_ratio_buff_amt;
	private String	r16_cap_ratio_buff;
	private BigDecimal	r16_cap_ratio_buff_amt;
	private String	r17_cap_ratio_buff;
	private BigDecimal	r17_cap_ratio_buff_amt;
	private String	r18_cap_ratio_buff;
	private BigDecimal	r18_cap_ratio_buff_amt;

	private String	r20_cap_ratio_buff;
	private BigDecimal	r20_cap_ratio_buff_amt;
	private String	r21_cap_ratio_buff;
	private BigDecimal	r21_cap_ratio_buff_amt;
	private String	r22_cap_ratio_buff;
	private BigDecimal	r22_cap_ratio_buff_amt;
	private String	r23_cap_ratio_buff;
	private BigDecimal	r23_cap_ratio_buff_amt;

	private String	r25_cap_ratio_buff;
	private BigDecimal	r25_cap_ratio_buff_amt;
	private String	r26_cap_ratio_buff;
	private BigDecimal	r26_cap_ratio_buff_amt;
	private String	r27_cap_ratio_buff;
	private BigDecimal	r27_cap_ratio_buff_amt;
	private String	r28_cap_ratio_buff;
	private BigDecimal	r28_cap_ratio_buff_amt;
	private String	r29_cap_ratio_buff;
	private BigDecimal	r29_cap_ratio_buff_amt;
	private String	r30_cap_ratio_buff;
	private BigDecimal	r30_cap_ratio_buff_amt;
	
	
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
	public String getR2_cap_ratio_buff() {
		return r2_cap_ratio_buff;
	}
	public void setR2_cap_ratio_buff(String r2_cap_ratio_buff) {
		this.r2_cap_ratio_buff = r2_cap_ratio_buff;
	}
	public BigDecimal getR2_cap_ratio_buff_amt() {
		return r2_cap_ratio_buff_amt;
	}
	public void setR2_cap_ratio_buff_amt(BigDecimal r2_cap_ratio_buff_amt) {
		this.r2_cap_ratio_buff_amt = r2_cap_ratio_buff_amt;
	}
	public String getR3_cap_ratio_buff() {
		return r3_cap_ratio_buff;
	}
	public void setR3_cap_ratio_buff(String r3_cap_ratio_buff) {
		this.r3_cap_ratio_buff = r3_cap_ratio_buff;
	}
	public BigDecimal getR3_cap_ratio_buff_amt() {
		return r3_cap_ratio_buff_amt;
	}
	public void setR3_cap_ratio_buff_amt(BigDecimal r3_cap_ratio_buff_amt) {
		this.r3_cap_ratio_buff_amt = r3_cap_ratio_buff_amt;
	}
	public String getR4_cap_ratio_buff() {
		return r4_cap_ratio_buff;
	}
	public void setR4_cap_ratio_buff(String r4_cap_ratio_buff) {
		this.r4_cap_ratio_buff = r4_cap_ratio_buff;
	}
	public BigDecimal getR4_cap_ratio_buff_amt() {
		return r4_cap_ratio_buff_amt;
	}
	public void setR4_cap_ratio_buff_amt(BigDecimal r4_cap_ratio_buff_amt) {
		this.r4_cap_ratio_buff_amt = r4_cap_ratio_buff_amt;
	}
	public String getR5_cap_ratio_buff() {
		return r5_cap_ratio_buff;
	}
	public void setR5_cap_ratio_buff(String r5_cap_ratio_buff) {
		this.r5_cap_ratio_buff = r5_cap_ratio_buff;
	}
	public BigDecimal getR5_cap_ratio_buff_amt() {
		return r5_cap_ratio_buff_amt;
	}
	public void setR5_cap_ratio_buff_amt(BigDecimal r5_cap_ratio_buff_amt) {
		this.r5_cap_ratio_buff_amt = r5_cap_ratio_buff_amt;
	}
	public String getR6_cap_ratio_buff() {
		return r6_cap_ratio_buff;
	}
	public void setR6_cap_ratio_buff(String r6_cap_ratio_buff) {
		this.r6_cap_ratio_buff = r6_cap_ratio_buff;
	}
	public BigDecimal getR6_cap_ratio_buff_amt() {
		return r6_cap_ratio_buff_amt;
	}
	public void setR6_cap_ratio_buff_amt(BigDecimal r6_cap_ratio_buff_amt) {
		this.r6_cap_ratio_buff_amt = r6_cap_ratio_buff_amt;
	}
	public String getR7_cap_ratio_buff() {
		return r7_cap_ratio_buff;
	}
	public void setR7_cap_ratio_buff(String r7_cap_ratio_buff) {
		this.r7_cap_ratio_buff = r7_cap_ratio_buff;
	}
	public BigDecimal getR7_cap_ratio_buff_amt() {
		return r7_cap_ratio_buff_amt;
	}
	public void setR7_cap_ratio_buff_amt(BigDecimal r7_cap_ratio_buff_amt) {
		this.r7_cap_ratio_buff_amt = r7_cap_ratio_buff_amt;
	}
	public String getR8_cap_ratio_buff() {
		return r8_cap_ratio_buff;
	}
	public void setR8_cap_ratio_buff(String r8_cap_ratio_buff) {
		this.r8_cap_ratio_buff = r8_cap_ratio_buff;
	}
	public BigDecimal getR8_cap_ratio_buff_amt() {
		return r8_cap_ratio_buff_amt;
	}
	public void setR8_cap_ratio_buff_amt(BigDecimal r8_cap_ratio_buff_amt) {
		this.r8_cap_ratio_buff_amt = r8_cap_ratio_buff_amt;
	}
	public String getR11_cap_ratio_buff() {
		return r11_cap_ratio_buff;
	}
	public void setR11_cap_ratio_buff(String r11_cap_ratio_buff) {
		this.r11_cap_ratio_buff = r11_cap_ratio_buff;
	}
	public BigDecimal getR11_cap_ratio_buff_amt() {
		return r11_cap_ratio_buff_amt;
	}
	public void setR11_cap_ratio_buff_amt(BigDecimal r11_cap_ratio_buff_amt) {
		this.r11_cap_ratio_buff_amt = r11_cap_ratio_buff_amt;
	}
	public String getR12_cap_ratio_buff() {
		return r12_cap_ratio_buff;
	}
	public void setR12_cap_ratio_buff(String r12_cap_ratio_buff) {
		this.r12_cap_ratio_buff = r12_cap_ratio_buff;
	}
	public BigDecimal getR12_cap_ratio_buff_amt() {
		return r12_cap_ratio_buff_amt;
	}
	public void setR12_cap_ratio_buff_amt(BigDecimal r12_cap_ratio_buff_amt) {
		this.r12_cap_ratio_buff_amt = r12_cap_ratio_buff_amt;
	}
	public String getR13_cap_ratio_buff() {
		return r13_cap_ratio_buff;
	}
	public void setR13_cap_ratio_buff(String r13_cap_ratio_buff) {
		this.r13_cap_ratio_buff = r13_cap_ratio_buff;
	}
	public BigDecimal getR13_cap_ratio_buff_amt() {
		return r13_cap_ratio_buff_amt;
	}
	public void setR13_cap_ratio_buff_amt(BigDecimal r13_cap_ratio_buff_amt) {
		this.r13_cap_ratio_buff_amt = r13_cap_ratio_buff_amt;
	}
	public String getR15_cap_ratio_buff() {
		return r15_cap_ratio_buff;
	}
	public void setR15_cap_ratio_buff(String r15_cap_ratio_buff) {
		this.r15_cap_ratio_buff = r15_cap_ratio_buff;
	}
	public BigDecimal getR15_cap_ratio_buff_amt() {
		return r15_cap_ratio_buff_amt;
	}
	public void setR15_cap_ratio_buff_amt(BigDecimal r15_cap_ratio_buff_amt) {
		this.r15_cap_ratio_buff_amt = r15_cap_ratio_buff_amt;
	}
	public String getR16_cap_ratio_buff() {
		return r16_cap_ratio_buff;
	}
	public void setR16_cap_ratio_buff(String r16_cap_ratio_buff) {
		this.r16_cap_ratio_buff = r16_cap_ratio_buff;
	}
	public BigDecimal getR16_cap_ratio_buff_amt() {
		return r16_cap_ratio_buff_amt;
	}
	public void setR16_cap_ratio_buff_amt(BigDecimal r16_cap_ratio_buff_amt) {
		this.r16_cap_ratio_buff_amt = r16_cap_ratio_buff_amt;
	}
	public String getR17_cap_ratio_buff() {
		return r17_cap_ratio_buff;
	}
	public void setR17_cap_ratio_buff(String r17_cap_ratio_buff) {
		this.r17_cap_ratio_buff = r17_cap_ratio_buff;
	}
	public BigDecimal getR17_cap_ratio_buff_amt() {
		return r17_cap_ratio_buff_amt;
	}
	public void setR17_cap_ratio_buff_amt(BigDecimal r17_cap_ratio_buff_amt) {
		this.r17_cap_ratio_buff_amt = r17_cap_ratio_buff_amt;
	}
	public String getR18_cap_ratio_buff() {
		return r18_cap_ratio_buff;
	}
	public void setR18_cap_ratio_buff(String r18_cap_ratio_buff) {
		this.r18_cap_ratio_buff = r18_cap_ratio_buff;
	}
	public BigDecimal getR18_cap_ratio_buff_amt() {
		return r18_cap_ratio_buff_amt;
	}
	public void setR18_cap_ratio_buff_amt(BigDecimal r18_cap_ratio_buff_amt) {
		this.r18_cap_ratio_buff_amt = r18_cap_ratio_buff_amt;
	}
	public String getR20_cap_ratio_buff() {
		return r20_cap_ratio_buff;
	}
	public void setR20_cap_ratio_buff(String r20_cap_ratio_buff) {
		this.r20_cap_ratio_buff = r20_cap_ratio_buff;
	}
	public BigDecimal getR20_cap_ratio_buff_amt() {
		return r20_cap_ratio_buff_amt;
	}
	public void setR20_cap_ratio_buff_amt(BigDecimal r20_cap_ratio_buff_amt) {
		this.r20_cap_ratio_buff_amt = r20_cap_ratio_buff_amt;
	}
	public String getR21_cap_ratio_buff() {
		return r21_cap_ratio_buff;
	}
	public void setR21_cap_ratio_buff(String r21_cap_ratio_buff) {
		this.r21_cap_ratio_buff = r21_cap_ratio_buff;
	}
	public BigDecimal getR21_cap_ratio_buff_amt() {
		return r21_cap_ratio_buff_amt;
	}
	public void setR21_cap_ratio_buff_amt(BigDecimal r21_cap_ratio_buff_amt) {
		this.r21_cap_ratio_buff_amt = r21_cap_ratio_buff_amt;
	}
	public String getR22_cap_ratio_buff() {
		return r22_cap_ratio_buff;
	}
	public void setR22_cap_ratio_buff(String r22_cap_ratio_buff) {
		this.r22_cap_ratio_buff = r22_cap_ratio_buff;
	}
	public BigDecimal getR22_cap_ratio_buff_amt() {
		return r22_cap_ratio_buff_amt;
	}
	public void setR22_cap_ratio_buff_amt(BigDecimal r22_cap_ratio_buff_amt) {
		this.r22_cap_ratio_buff_amt = r22_cap_ratio_buff_amt;
	}
	public String getR23_cap_ratio_buff() {
		return r23_cap_ratio_buff;
	}
	public void setR23_cap_ratio_buff(String r23_cap_ratio_buff) {
		this.r23_cap_ratio_buff = r23_cap_ratio_buff;
	}
	public BigDecimal getR23_cap_ratio_buff_amt() {
		return r23_cap_ratio_buff_amt;
	}
	public void setR23_cap_ratio_buff_amt(BigDecimal r23_cap_ratio_buff_amt) {
		this.r23_cap_ratio_buff_amt = r23_cap_ratio_buff_amt;
	}
	public String getR25_cap_ratio_buff() {
		return r25_cap_ratio_buff;
	}
	public void setR25_cap_ratio_buff(String r25_cap_ratio_buff) {
		this.r25_cap_ratio_buff = r25_cap_ratio_buff;
	}
	public BigDecimal getR25_cap_ratio_buff_amt() {
		return r25_cap_ratio_buff_amt;
	}
	public void setR25_cap_ratio_buff_amt(BigDecimal r25_cap_ratio_buff_amt) {
		this.r25_cap_ratio_buff_amt = r25_cap_ratio_buff_amt;
	}
	public String getR26_cap_ratio_buff() {
		return r26_cap_ratio_buff;
	}
	public void setR26_cap_ratio_buff(String r26_cap_ratio_buff) {
		this.r26_cap_ratio_buff = r26_cap_ratio_buff;
	}
	public BigDecimal getR26_cap_ratio_buff_amt() {
		return r26_cap_ratio_buff_amt;
	}
	public void setR26_cap_ratio_buff_amt(BigDecimal r26_cap_ratio_buff_amt) {
		this.r26_cap_ratio_buff_amt = r26_cap_ratio_buff_amt;
	}
	public String getR27_cap_ratio_buff() {
		return r27_cap_ratio_buff;
	}
	public void setR27_cap_ratio_buff(String r27_cap_ratio_buff) {
		this.r27_cap_ratio_buff = r27_cap_ratio_buff;
	}
	public BigDecimal getR27_cap_ratio_buff_amt() {
		return r27_cap_ratio_buff_amt;
	}
	public void setR27_cap_ratio_buff_amt(BigDecimal r27_cap_ratio_buff_amt) {
		this.r27_cap_ratio_buff_amt = r27_cap_ratio_buff_amt;
	}
	public String getR28_cap_ratio_buff() {
		return r28_cap_ratio_buff;
	}
	public void setR28_cap_ratio_buff(String r28_cap_ratio_buff) {
		this.r28_cap_ratio_buff = r28_cap_ratio_buff;
	}
	public BigDecimal getR28_cap_ratio_buff_amt() {
		return r28_cap_ratio_buff_amt;
	}
	public void setR28_cap_ratio_buff_amt(BigDecimal r28_cap_ratio_buff_amt) {
		this.r28_cap_ratio_buff_amt = r28_cap_ratio_buff_amt;
	}
	public String getR29_cap_ratio_buff() {
		return r29_cap_ratio_buff;
	}
	public void setR29_cap_ratio_buff(String r29_cap_ratio_buff) {
		this.r29_cap_ratio_buff = r29_cap_ratio_buff;
	}
	public BigDecimal getR29_cap_ratio_buff_amt() {
		return r29_cap_ratio_buff_amt;
	}
	public void setR29_cap_ratio_buff_amt(BigDecimal r29_cap_ratio_buff_amt) {
		this.r29_cap_ratio_buff_amt = r29_cap_ratio_buff_amt;
	}
	public String getR30_cap_ratio_buff() {
		return r30_cap_ratio_buff;
	}
	public void setR30_cap_ratio_buff(String r30_cap_ratio_buff) {
		this.r30_cap_ratio_buff = r30_cap_ratio_buff;
	}
	public BigDecimal getR30_cap_ratio_buff_amt() {
		return r30_cap_ratio_buff_amt;
	}
	public void setR30_cap_ratio_buff_amt(BigDecimal r30_cap_ratio_buff_amt) {
		this.r30_cap_ratio_buff_amt = r30_cap_ratio_buff_amt;
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


//=========================detail 


//-----------detail entity CLS 

public class CAP_RATIO_BUFFER_Detail_RowMapper 
        implements RowMapper<CAP_RATIO_BUFFER_Detail_Entity> {

    @Override
    public CAP_RATIO_BUFFER_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        CAP_RATIO_BUFFER_Detail_Entity obj = new CAP_RATIO_BUFFER_Detail_Entity();

        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));

        obj.setDataType(rs.getString("DATA_TYPE"));
        obj.setReportName(rs.getString("REPORT_NAME"));
        obj.setReportLable(rs.getString("REPORT_LABEL"));
        obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));

        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

        obj.setReportDate(rs.getDate("REPORT_DATE"));

        obj.setCreateUser(rs.getString("CREATE_USER"));
        obj.setCreateTime(rs.getDate("CREATE_TIME"));

        obj.setModifyUser(rs.getString("MODIFY_USER"));
        obj.setModifyTime(rs.getDate("MODIFY_TIME"));

        obj.setVerifyUser(rs.getString("VERIFY_USER"));
        obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

        // =========================
        // FLAG HANDLING (SAFE)
        // =========================
        obj.setEntityFlg(
                rs.getString("ENTITY_FLG") != null
                        ? rs.getString("ENTITY_FLG").charAt(0)
                        : ' '
        );

        obj.setModifyFlg(
                rs.getString("MODIFY_FLG") != null
                        ? rs.getString("MODIFY_FLG").charAt(0)
                        : ' '
        );

        obj.setDelFlg(
                rs.getString("DEL_FLG") != null
                        ? rs.getString("DEL_FLG").charAt(0)
                        : ' '
        );

        return obj;
    }
}

public class CAP_RATIO_BUFFER_Detail_Entity {

   
	
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
  private String reportLable;
  
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

public String getReportLable() {
	return reportLable;
}

public void setReportLable(String reportLable) {
	this.reportLable = reportLable;
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

//====================archival detail 


//-----------archival detail entity CLS 

public class CAP_RATIO_BUFFER_Archival_Detail_RowMapper 
        implements RowMapper<CAP_RATIO_BUFFER_Archival_Detail_Entity> {

    @Override
    public CAP_RATIO_BUFFER_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        CAP_RATIO_BUFFER_Archival_Detail_Entity obj = new CAP_RATIO_BUFFER_Archival_Detail_Entity();

        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));

        obj.setDataType(rs.getString("DATA_TYPE"));
        obj.setReportName(rs.getString("REPORT_NAME"));
        obj.setReportLable(rs.getString("REPORT_LABEL"));
        obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));

        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

        obj.setReportDate(rs.getDate("REPORT_DATE"));

        obj.setCreateUser(rs.getString("CREATE_USER"));
        obj.setCreateTime(rs.getDate("CREATE_TIME"));

        obj.setModifyUser(rs.getString("MODIFY_USER"));
        obj.setModifyTime(rs.getDate("MODIFY_TIME"));

        obj.setVerifyUser(rs.getString("VERIFY_USER"));
        obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

        // =========================
        // FLAG HANDLING (SAFE)
        // =========================
        obj.setEntityFlg(
                rs.getString("ENTITY_FLG") != null
                        ? rs.getString("ENTITY_FLG").charAt(0)
                        : ' '
        );

        obj.setModifyFlg(
                rs.getString("MODIFY_FLG") != null
                        ? rs.getString("MODIFY_FLG").charAt(0)
                        : ' '
        );

        obj.setDelFlg(
                rs.getString("DEL_FLG") != null
                        ? rs.getString("DEL_FLG").charAt(0)
                        : ' '
        );

        return obj;
    }
}


public class CAP_RATIO_BUFFER_Archival_Detail_Entity {

   
	
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
  private String reportLable;
  
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

public String getReportLable() {
	return reportLable;
}

public void setReportLable(String reportLable) {
	this.reportLable = reportLable;
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
 // MODEL AND VIEW METHOD summary
 //=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
		 		 public ModelAndView getCAP_RATIO_BUFFERView(

	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("CAP_RATIO_BUFFER View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    // =====================================================
	    // ARCHIVAL MODE
	    // =====================================================

	    if ("ARCHIVAL".equals(type) && version != null) {

	        List<CAP_RATIO_BUFFER_Archival_Summary_Entity> T1Master = new ArrayList<>();
	    

	        try {
	            Date dt = dateformat.parse(todate);
	            
	        
	            // ============================
	            // SUMMARY ARCHIVAL
	            // ============================
	            T1Master = getdatabydateListarchival(dt, version);

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

	        List<CAP_RATIO_BUFFER_Summary_Entity> T1Master = new ArrayList<>();
	       

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

	    mv.setViewName("BRRS/CAP_RATIO_BUFFER");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getCAP_RATIO_BUFFERcurrentDtl(
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

	            List<CAP_RATIO_BUFFER_Archival_Detail_Entity> archivalDetailList;

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

	            List<CAP_RATIO_BUFFER_Detail_Entity> currentDetailList;

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

	    mv.setViewName("BRRS/CAP_RATIO_BUFFER");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
	
	
	//Archival View
		public List<Object[]> getCAP_RATIO_BUFFERArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<CAP_RATIO_BUFFER_Archival_Summary_Entity> repoData =
						getdatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (CAP_RATIO_BUFFER_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					CAP_RATIO_BUFFER_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  CAP_RATIO_BUFFER  Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}
		
		
	//==================================update report
	
	
		public void updateReport(
		        CAP_RATIO_BUFFER_Summary_Entity updatedEntity) {

		    System.out.println("Came to CAP_RATIO_BUFFER Manual Update");
		    System.out.println("Report Date: " + updatedEntity.getReport_date());

		 

		    try {

		         // ✅ Target Rows
	        int[] targetRows = {2, 3, 4, 11, 12, 13};

	        for (int i : targetRows) {

	            String field = "cap_ratio_buff_amt";

	            String getterName = "getR" + i + "_" + field;
	            String setterName = "setR" + i + "_" + field;

		                try {

		                    Method getter =
		                            CAP_RATIO_BUFFER_Summary_Entity.class
		                                    .getMethod(getterName);

		                    Object value =
		                            getter.invoke(updatedEntity);

		                    // Skip null values
		                    if (value == null) continue;

		                    // Column name in DB
		                    String columnName =
		                    		"R" + i + "_" + field;

		                    String sql =
		                            "UPDATE BRRS_CAP_RATIO_BUFFER_SUMMARYTABLE " +
		                            "SET " + columnName + " = ? " +
		                            "WHERE REPORT_DATE = ?";

		                    jdbcTemplate.update(
		                            sql,
		                            value,
		                            updatedEntity.getReport_date()
		                    );

		                } catch (NoSuchMethodException e) {
		                    // Skip if method not exists
		                    continue;
		                }
		            }
		       

		        System.out.println("CAP_RATIO_BUFFER Manual Update Completed");

		    } catch (Exception e) {
		        throw new RuntimeException(
		                "Error while updating CAP_RATIO_BUFFER Manual fields", e);
		    }
		}
		
		
		//-------------getViewOrEditPage
		
		public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/CAP_RATIO_BUFFER"); 

		if (acctNo != null) {
			CAP_RATIO_BUFFER_Detail_Entity cap_ratio_buffEntity = findByDetailAcctnumber(acctNo);
			if (cap_ratio_buffEntity != null && cap_ratio_buffEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(cap_ratio_buffEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("cap_ratio_buffData", cap_ratio_buffEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}
	
	//-------------updateDetailEdit
	
		@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("acctBalanceInpula");
		
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			CAP_RATIO_BUFFER_Detail_Entity existing = findByDetailAcctnumber(acctNo);
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
    "UPDATE BRRS_CAP_RATIO_BUFFER_DETAILTABLE " +
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
							logger.info("Transaction committed — calling BRRS_CAP_RATIO_BUFFER_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_CAP_RATIO_BUFFER_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating CAP_RATIO_BUFFER record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
	
	//-------------------detail excel 
	
		public byte[] getCAP_RATIO_BUFFERDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  CAP_RATIO_BUFFER  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getCAP_RATIO_BUFFERDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("CAP_RATIO_BUFFER Details ");

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
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

				XSSFRow headerRow = sheet.createRow(0);
				for (int i = 0; i < headers.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(headers[i]);

					if (i == 3) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

					sheet.setColumnWidth(i, 5000);
				}

				// Get data
				Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
				List<CAP_RATIO_BUFFER_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (CAP_RATIO_BUFFER_Detail_Entity item : reportData) { 
						XSSFRow row = sheet.createRow(rowIndex++);

				row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);

					

					row.createCell(4).setCellValue(item.getReportLable());
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
					logger.info("No data found for CAP_RATIO_BUFFER — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating CAP_RATIO_BUFFER Excel", e);
				return new byte[0];
			}
		}
		
		
//===========================================    DetailExcel  ARCHIVAL	


		public byte[] getCAP_RATIO_BUFFERDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for CAP_RATIO_BUFFER ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("CAP_RATIO_BUFFER Detail NEW");

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

						if (i == 3) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

					sheet.setColumnWidth(i, 5000);
				}

	// Get data
				Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
				List<CAP_RATIO_BUFFER_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (CAP_RATIO_BUFFER_Archival_Detail_Entity item : reportData) {
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
					logger.info("No data found for CAP_RATIO_BUFFER — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating CAP_RATIO_BUFFER NEW Excel", e);
				return new byte[0];
			}
		}
		
		
//=========================================================EXCEL =====================================================



		
			public byte[] getCAP_RATIO_BUFFERExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.CAP_RATIO_BUFFER");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelCAP_RATIO_BUFFERARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<CAP_RATIO_BUFFER_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		
		
			System.out.println("DATA SIZE IS : "+dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  CAP_RATIO_BUFFER report. Returning empty result.");
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

				int startRow = 1;
				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						CAP_RATIO_BUFFER_Summary_Entity record = dataList.get(i);
					
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
		// row2
					// Column C
					

					
				Cell cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR2_cap_ratio_buff_amt() != null) {
					    cellC.setCellValue(record.getR2_cap_ratio_buff_amt().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}
					
					
					
					
					
					// row3
					// Column C
					
					row = sheet.getRow(2);
					
					 cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR3_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR3_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}
					
					
					
					
						// row4
						// Column C

						row = sheet.getRow(3);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR4_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR4_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						
						
						// row5
						// Column C

						row = sheet.getRow(4);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR5_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR5_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

					
					
						// row6
						// Column C

						row = sheet.getRow(5);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR6_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR6_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						
						// row7
						// Column C

						row = sheet.getRow(6);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR7_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR7_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						
						// row8
						// Column C

						row = sheet.getRow(7);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR8_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR8_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						
						// ======================= R11 =======================
						// row11
						// Column C
						row = sheet.getRow(10);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR11_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR11_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}


						// ======================= R12 =======================
						// row12
						// Column C
						row = sheet.getRow(11);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR12_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR12_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}


						// ======================= R13 =======================
						// row13
						// Column C
						row = sheet.getRow(12);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR13_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR13_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

					
					
						// ======================= R15 =======================
						// row15
						// Column C
						row = sheet.getRow(14);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR15_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR15_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}


						// ======================= R16 =======================
						// row16
						// Column C
						row = sheet.getRow(15);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR16_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR16_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}


						// ======================= R17 =======================
						// row17
						// Column C
						row = sheet.getRow(16);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR17_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR17_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}


						// ======================= R18 =======================
						// row18
						// Column C
						row = sheet.getRow(17);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR18_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR18_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

					
					
					
						// ======================= R20 =======================
						row = sheet.getRow(19);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR20_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR20_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R21 =======================
						row = sheet.getRow(20);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR21_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR21_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R22 =======================
						row = sheet.getRow(21);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR22_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR22_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R23 =======================
						row = sheet.getRow(22);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR23_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR23_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}
						
						// ======================= R25 =======================
						row = sheet.getRow(24);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR25_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR25_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R26 =======================
						row = sheet.getRow(25);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR26_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR26_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R27 =======================
						row = sheet.getRow(26);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR27_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR27_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R28 =======================
						row = sheet.getRow(27);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR28_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR28_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R29 =======================
						row = sheet.getRow(28);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR29_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR29_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R30 =======================
						row = sheet.getRow(29);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR30_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR30_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
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
		

	public byte[] getExcelCAP_RATIO_BUFFERARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {

			}

			List<CAP_RATIO_BUFFER_Archival_Summary_Entity> dataList = 
					getdatabydateListarchival(dateformat.parse(todate), version);
		
	    

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for CAP_RATIO_BUFFER new report. Returning empty result.");
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

				int startRow = 1;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						CAP_RATIO_BUFFER_Archival_Summary_Entity record = dataList.get(i);
					    
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


	// row2
					// Column C
					

					
				Cell cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR2_cap_ratio_buff_amt() != null) {
					    cellC.setCellValue(record.getR2_cap_ratio_buff_amt().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}
					
					
					
					
					
					// row3
					// Column C
					
					row = sheet.getRow(2);
					
					 cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR3_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR3_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}
					
					
					
					
						// row4
						// Column C

						row = sheet.getRow(3);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR4_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR4_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						
						
						// row5
						// Column C

						row = sheet.getRow(4);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR5_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR5_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

					
					
						// row6
						// Column C

						row = sheet.getRow(5);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR6_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR6_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						
						// row7
						// Column C

						row = sheet.getRow(6);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR7_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR7_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						
						// row8
						// Column C

						row = sheet.getRow(7);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR8_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR8_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						
						// ======================= R11 =======================
						// row11
						// Column C
						row = sheet.getRow(10);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR11_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR11_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}


						// ======================= R12 =======================
						// row12
						// Column C
						row = sheet.getRow(11);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR12_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR12_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}


						// ======================= R13 =======================
						// row13
						// Column C
						row = sheet.getRow(12);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR13_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR13_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

					
					
						// ======================= R15 =======================
						// row15
						// Column C
						row = sheet.getRow(14);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR15_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR15_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}


						// ======================= R16 =======================
						// row16
						// Column C
						row = sheet.getRow(15);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR16_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR16_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}


						// ======================= R17 =======================
						// row17
						// Column C
						row = sheet.getRow(16);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR17_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR17_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}


						// ======================= R18 =======================
						// row18
						// Column C
						row = sheet.getRow(17);

						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR18_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR18_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

					
					
					
						// ======================= R20 =======================
						row = sheet.getRow(19);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR20_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR20_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R21 =======================
						row = sheet.getRow(20);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR21_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR21_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R22 =======================
						row = sheet.getRow(21);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR22_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR22_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R23 =======================
						row = sheet.getRow(22);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR23_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR23_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}
						
						// ======================= R25 =======================
						row = sheet.getRow(24);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR25_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR25_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R26 =======================
						row = sheet.getRow(25);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR26_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR26_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R27 =======================
						row = sheet.getRow(26);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR27_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR27_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R28 =======================
						row = sheet.getRow(27);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR28_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR28_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R29 =======================
						row = sheet.getRow(28);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR29_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR29_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
						}

						// ======================= R30 =======================
						row = sheet.getRow(29);
						cellC = row.getCell(2);
						if (cellC == null) cellC = row.createCell(2);
						if (record.getR30_cap_ratio_buff_amt() != null) {
						    cellC.setCellValue(record.getR30_cap_ratio_buff_amt().doubleValue());
						} else {
						    cellC.setCellValue(0);
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