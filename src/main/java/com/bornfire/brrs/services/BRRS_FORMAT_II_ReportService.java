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

public class BRRS_FORMAT_II_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_FORMAT_II_ReportService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

  
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;
	

	// Fetch data by report date - Summary

public List<FORMAT_II_Summary_Entity> getDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_FORMAT_II_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new FORMAT_II_RowMapper()   // make sure you created this RowMapper
    );
}


//---------archival summary 

public List<Object[]> getFORMAT_II_Archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}


public List<FORMAT_II_Archival_Summary_Entity> getDataByDateListArchival(Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new FORMAT_II_Archival_Summary_RowMapper()
    );
}



public List<FORMAT_II_Archival_Summary_Entity> getDataByDateListWithVersion() {

    String sql = "SELECT * FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new FORMAT_II_Archival_Summary_RowMapper()
    );
}


//-----------detail 

public List<FORMAT_II_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_FORMAT_II_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new FORMAT_II_Detail_RowMapper()
    );
}

public List<FORMAT_II_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_FORMAT_II_DETAILTABLE " +
                 "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, offset, limit},
            new FORMAT_II_Detail_RowMapper()
    );
}


public int getDataCount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_FORMAT_II_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            Integer.class
    );
}

public List<FORMAT_II_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate) {

    String sql = "SELECT * FROM BRRS_FORMAT_II_DETAILTABLE " +
                 "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate},
            new FORMAT_II_Detail_RowMapper()
    );
}


public FORMAT_II_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_FORMAT_II_DETAILTABLE WHERE ACCT_NUMBER = ?";

    List<FORMAT_II_Detail_Entity> list = jdbcTemplate.query(
            sql,
            new Object[]{acctNumber},
            new FORMAT_II_Detail_RowMapper()
    );

    return list.isEmpty() ? null : list.get(0);
}

//---------------------archival detail 

public List<FORMAT_II_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, dataEntryVersion},
            new FORMAT_II_Archival_Detail_RowMapper()
    );
}


public List<FORMAT_II_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL " +
                 "WHERE REPORT_LABEL = ? " +
                 "AND REPORT_ADDL_CRITERIA_1 = ? " +
                 "AND REPORT_DATE = ? " +
                 "AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportLabel, reportAddlCriteria1, reportDate, dataEntryVersion},
            new FORMAT_II_Archival_Detail_RowMapper()
    );
}

//======================entity cls summary  FORMAT_II_RowMapper

public class FORMAT_II_RowMapper 
        implements RowMapper<FORMAT_II_Summary_Entity> {

    @Override
    public FORMAT_II_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        FORMAT_II_Summary_Entity obj = new FORMAT_II_Summary_Entity();

        // =========================
        // R13
        // =========================
        obj.setR13_product(rs.getString("r13_product"));
        obj.setR13_amt(rs.getBigDecimal("r13_amt"));
        obj.setR13_amt_sub_add(rs.getBigDecimal("r13_amt_sub_add"));
        obj.setR13_amt_sub_del(rs.getBigDecimal("r13_amt_sub_del"));
        obj.setR13_amt_total(rs.getBigDecimal("r13_amt_total"));

        // =========================
        // R14
        // =========================
        obj.setR14_product(rs.getString("r14_product"));
        obj.setR14_amt(rs.getBigDecimal("r14_amt"));
        obj.setR14_amt_sub_add(rs.getBigDecimal("r14_amt_sub_add"));
        obj.setR14_amt_sub_del(rs.getBigDecimal("r14_amt_sub_del"));
        obj.setR14_amt_total(rs.getBigDecimal("r14_amt_total"));

        // =========================
        // R15
        // =========================
        obj.setR15_product(rs.getString("r15_product"));
        obj.setR15_amt(rs.getBigDecimal("r15_amt"));
        obj.setR15_amt_sub_add(rs.getBigDecimal("r15_amt_sub_add"));
        obj.setR15_amt_sub_del(rs.getBigDecimal("r15_amt_sub_del"));
        obj.setR15_amt_total(rs.getBigDecimal("r15_amt_total"));

        // =========================
        // R16
        // =========================
        obj.setR16_product(rs.getString("r16_product"));
        obj.setR16_amt(rs.getBigDecimal("r16_amt"));
        obj.setR16_amt_sub_add(rs.getBigDecimal("r16_amt_sub_add"));
        obj.setR16_amt_sub_del(rs.getBigDecimal("r16_amt_sub_del"));
        obj.setR16_amt_total(rs.getBigDecimal("r16_amt_total"));

        // =========================
        // R17
        // =========================
        obj.setR17_product(rs.getString("r17_product"));
        obj.setR17_amt(rs.getBigDecimal("r17_amt"));
        obj.setR17_amt_sub_add(rs.getBigDecimal("r17_amt_sub_add"));
        obj.setR17_amt_sub_del(rs.getBigDecimal("r17_amt_sub_del"));
        obj.setR17_amt_total(rs.getBigDecimal("r17_amt_total"));

        // =========================
        // R18
        // =========================
        obj.setR18_product(rs.getString("r18_product"));
        obj.setR18_amt(rs.getBigDecimal("r18_amt"));
        obj.setR18_amt_sub_add(rs.getBigDecimal("r18_amt_sub_add"));
        obj.setR18_amt_sub_del(rs.getBigDecimal("r18_amt_sub_del"));
        obj.setR18_amt_total(rs.getBigDecimal("r18_amt_total"));

        // =========================
        // R19
        // =========================
        obj.setR19_product(rs.getString("r19_product"));
        obj.setR19_amt(rs.getBigDecimal("r19_amt"));
        obj.setR19_amt_sub_add(rs.getBigDecimal("r19_amt_sub_add"));
        obj.setR19_amt_sub_del(rs.getBigDecimal("r19_amt_sub_del"));
        obj.setR19_amt_total(rs.getBigDecimal("r19_amt_total"));

        // =========================
        // R20
        // =========================
        obj.setR20_product(rs.getString("r20_product"));
        obj.setR20_amt(rs.getBigDecimal("r20_amt"));
        obj.setR20_amt_sub_add(rs.getBigDecimal("r20_amt_sub_add"));
        obj.setR20_amt_sub_del(rs.getBigDecimal("r20_amt_sub_del"));
        obj.setR20_amt_total(rs.getBigDecimal("r20_amt_total"));

        // =========================
        // R21
        // =========================
        obj.setR21_product(rs.getString("r21_product"));
        obj.setR21_amt(rs.getBigDecimal("r21_amt"));
        obj.setR21_amt_sub_add(rs.getBigDecimal("r21_amt_sub_add"));
        obj.setR21_amt_sub_del(rs.getBigDecimal("r21_amt_sub_del"));
        obj.setR21_amt_total(rs.getBigDecimal("r21_amt_total"));

        // =========================
        // R22
        // =========================
        obj.setR22_product(rs.getString("r22_product"));
        obj.setR22_amt(rs.getBigDecimal("r22_amt"));
        obj.setR22_amt_sub_add(rs.getBigDecimal("r22_amt_sub_add"));
        obj.setR22_amt_sub_del(rs.getBigDecimal("r22_amt_sub_del"));
        obj.setR22_amt_total(rs.getBigDecimal("r22_amt_total"));

        // =========================
        // R23
        // =========================
        obj.setR23_product(rs.getString("r23_product"));
        obj.setR23_amt(rs.getBigDecimal("r23_amt"));
        obj.setR23_amt_sub_add(rs.getBigDecimal("r23_amt_sub_add"));
        obj.setR23_amt_sub_del(rs.getBigDecimal("r23_amt_sub_del"));
        obj.setR23_amt_total(rs.getBigDecimal("r23_amt_total"));

        // =========================
        // R24
        // =========================
        obj.setR24_product(rs.getString("r24_product"));
        obj.setR24_amt(rs.getBigDecimal("r24_amt"));
        obj.setR24_amt_sub_add(rs.getBigDecimal("r24_amt_sub_add"));
        obj.setR24_amt_sub_del(rs.getBigDecimal("r24_amt_sub_del"));
        obj.setR24_amt_total(rs.getBigDecimal("r24_amt_total"));

        // =========================
        // R25
        // =========================
        obj.setR25_product(rs.getString("r25_product"));
        obj.setR25_amt(rs.getBigDecimal("r25_amt"));
        obj.setR25_amt_sub_add(rs.getBigDecimal("r25_amt_sub_add"));
        obj.setR25_amt_sub_del(rs.getBigDecimal("r25_amt_sub_del"));
        obj.setR25_amt_total(rs.getBigDecimal("r25_amt_total"));

        // =========================
        // R26
        // =========================
        obj.setR26_product(rs.getString("r26_product"));
        obj.setR26_amt(rs.getBigDecimal("r26_amt"));
        obj.setR26_amt_sub_add(rs.getBigDecimal("r26_amt_sub_add"));
        obj.setR26_amt_sub_del(rs.getBigDecimal("r26_amt_sub_del"));
        obj.setR26_amt_total(rs.getBigDecimal("r26_amt_total"));

        // =========================
        // R27
        // =========================
        obj.setR27_product(rs.getString("r27_product"));
        obj.setR27_amt(rs.getBigDecimal("r27_amt"));
        obj.setR27_amt_sub_add(rs.getBigDecimal("r27_amt_sub_add"));
        obj.setR27_amt_sub_del(rs.getBigDecimal("r27_amt_sub_del"));
        obj.setR27_amt_total(rs.getBigDecimal("r27_amt_total"));

        // =========================
        // R28
        // =========================
        obj.setR28_product(rs.getString("r28_product"));
        obj.setR28_amt(rs.getBigDecimal("r28_amt"));
        obj.setR28_amt_sub_add(rs.getBigDecimal("r28_amt_sub_add"));
        obj.setR28_amt_sub_del(rs.getBigDecimal("r28_amt_sub_del"));
        obj.setR28_amt_total(rs.getBigDecimal("r28_amt_total"));

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



public class FORMAT_II_Summary_Entity {
	
	

		
	
	private String	r13_product;
	private BigDecimal	r13_amt;
	private BigDecimal	r13_amt_sub_add;
	private BigDecimal	r13_amt_sub_del;
	private BigDecimal	r13_amt_total;
	private String	r14_product;
	private BigDecimal	r14_amt;
	private BigDecimal	r14_amt_sub_add;
	private BigDecimal	r14_amt_sub_del;
	private BigDecimal	r14_amt_total;
	private String	r15_product;
	private BigDecimal	r15_amt;
	private BigDecimal	r15_amt_sub_add;
	private BigDecimal	r15_amt_sub_del;
	private BigDecimal	r15_amt_total;
	private String	r16_product;
	private BigDecimal	r16_amt;
	private BigDecimal	r16_amt_sub_add;
	private BigDecimal	r16_amt_sub_del;
	private BigDecimal	r16_amt_total;
	private String	r17_product;
	private BigDecimal	r17_amt;
	private BigDecimal	r17_amt_sub_add;
	private BigDecimal	r17_amt_sub_del;
	private BigDecimal	r17_amt_total;
	private String	r18_product;
	private BigDecimal	r18_amt;
	private BigDecimal	r18_amt_sub_add;
	private BigDecimal	r18_amt_sub_del;
	private BigDecimal	r18_amt_total;
	private String	r19_product;
	private BigDecimal	r19_amt;
	private BigDecimal	r19_amt_sub_add;
	private BigDecimal	r19_amt_sub_del;
	private BigDecimal	r19_amt_total;
	private String	r20_product;
	private BigDecimal	r20_amt;
	private BigDecimal	r20_amt_sub_add;
	private BigDecimal	r20_amt_sub_del;
	private BigDecimal	r20_amt_total;
	private String	r21_product;
	private BigDecimal	r21_amt;
	private BigDecimal	r21_amt_sub_add;
	private BigDecimal	r21_amt_sub_del;
	private BigDecimal	r21_amt_total;
	private String	r22_product;
	private BigDecimal	r22_amt;
	private BigDecimal	r22_amt_sub_add;
	private BigDecimal	r22_amt_sub_del;
	private BigDecimal	r22_amt_total;
	private String	r23_product;
	private BigDecimal	r23_amt;
	private BigDecimal	r23_amt_sub_add;
	private BigDecimal	r23_amt_sub_del;
	private BigDecimal	r23_amt_total;
	private String	r24_product;
	private BigDecimal	r24_amt;
	private BigDecimal	r24_amt_sub_add;
	private BigDecimal	r24_amt_sub_del;
	private BigDecimal	r24_amt_total;
	private String	r25_product;
	private BigDecimal	r25_amt;
	private BigDecimal	r25_amt_sub_add;
	private BigDecimal	r25_amt_sub_del;
	private BigDecimal	r25_amt_total;
	private String	r26_product;
	private BigDecimal	r26_amt;
	private BigDecimal	r26_amt_sub_add;
	private BigDecimal	r26_amt_sub_del;
	private BigDecimal	r26_amt_total;
	private String	r27_product;
	private BigDecimal	r27_amt;
	private BigDecimal	r27_amt_sub_add;
	private BigDecimal	r27_amt_sub_del;
	private BigDecimal	r27_amt_total;
	private String	r28_product;
	private BigDecimal	r28_amt;
	private BigDecimal	r28_amt_sub_add;
	private BigDecimal	r28_amt_sub_del;
	private BigDecimal	r28_amt_total;


	               
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
	public BigDecimal getR13_amt() {
		return r13_amt;
	}
	public void setR13_amt(BigDecimal r13_amt) {
		this.r13_amt = r13_amt;
	}
	public BigDecimal getR13_amt_sub_add() {
		return r13_amt_sub_add;
	}
	public void setR13_amt_sub_add(BigDecimal r13_amt_sub_add) {
		this.r13_amt_sub_add = r13_amt_sub_add;
	}
	public BigDecimal getR13_amt_sub_del() {
		return r13_amt_sub_del;
	}
	public void setR13_amt_sub_del(BigDecimal r13_amt_sub_del) {
		this.r13_amt_sub_del = r13_amt_sub_del;
	}
	public BigDecimal getR13_amt_total() {
		return r13_amt_total;
	}
	public void setR13_amt_total(BigDecimal r13_amt_total) {
		this.r13_amt_total = r13_amt_total;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_amt() {
		return r14_amt;
	}
	public void setR14_amt(BigDecimal r14_amt) {
		this.r14_amt = r14_amt;
	}
	public BigDecimal getR14_amt_sub_add() {
		return r14_amt_sub_add;
	}
	public void setR14_amt_sub_add(BigDecimal r14_amt_sub_add) {
		this.r14_amt_sub_add = r14_amt_sub_add;
	}
	public BigDecimal getR14_amt_sub_del() {
		return r14_amt_sub_del;
	}
	public void setR14_amt_sub_del(BigDecimal r14_amt_sub_del) {
		this.r14_amt_sub_del = r14_amt_sub_del;
	}
	public BigDecimal getR14_amt_total() {
		return r14_amt_total;
	}
	public void setR14_amt_total(BigDecimal r14_amt_total) {
		this.r14_amt_total = r14_amt_total;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_amt() {
		return r15_amt;
	}
	public void setR15_amt(BigDecimal r15_amt) {
		this.r15_amt = r15_amt;
	}
	public BigDecimal getR15_amt_sub_add() {
		return r15_amt_sub_add;
	}
	public void setR15_amt_sub_add(BigDecimal r15_amt_sub_add) {
		this.r15_amt_sub_add = r15_amt_sub_add;
	}
	public BigDecimal getR15_amt_sub_del() {
		return r15_amt_sub_del;
	}
	public void setR15_amt_sub_del(BigDecimal r15_amt_sub_del) {
		this.r15_amt_sub_del = r15_amt_sub_del;
	}
	public BigDecimal getR15_amt_total() {
		return r15_amt_total;
	}
	public void setR15_amt_total(BigDecimal r15_amt_total) {
		this.r15_amt_total = r15_amt_total;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_amt() {
		return r16_amt;
	}
	public void setR16_amt(BigDecimal r16_amt) {
		this.r16_amt = r16_amt;
	}
	public BigDecimal getR16_amt_sub_add() {
		return r16_amt_sub_add;
	}
	public void setR16_amt_sub_add(BigDecimal r16_amt_sub_add) {
		this.r16_amt_sub_add = r16_amt_sub_add;
	}
	public BigDecimal getR16_amt_sub_del() {
		return r16_amt_sub_del;
	}
	public void setR16_amt_sub_del(BigDecimal r16_amt_sub_del) {
		this.r16_amt_sub_del = r16_amt_sub_del;
	}
	public BigDecimal getR16_amt_total() {
		return r16_amt_total;
	}
	public void setR16_amt_total(BigDecimal r16_amt_total) {
		this.r16_amt_total = r16_amt_total;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_amt() {
		return r17_amt;
	}
	public void setR17_amt(BigDecimal r17_amt) {
		this.r17_amt = r17_amt;
	}
	public BigDecimal getR17_amt_sub_add() {
		return r17_amt_sub_add;
	}
	public void setR17_amt_sub_add(BigDecimal r17_amt_sub_add) {
		this.r17_amt_sub_add = r17_amt_sub_add;
	}
	public BigDecimal getR17_amt_sub_del() {
		return r17_amt_sub_del;
	}
	public void setR17_amt_sub_del(BigDecimal r17_amt_sub_del) {
		this.r17_amt_sub_del = r17_amt_sub_del;
	}
	public BigDecimal getR17_amt_total() {
		return r17_amt_total;
	}
	public void setR17_amt_total(BigDecimal r17_amt_total) {
		this.r17_amt_total = r17_amt_total;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_amt() {
		return r18_amt;
	}
	public void setR18_amt(BigDecimal r18_amt) {
		this.r18_amt = r18_amt;
	}
	public BigDecimal getR18_amt_sub_add() {
		return r18_amt_sub_add;
	}
	public void setR18_amt_sub_add(BigDecimal r18_amt_sub_add) {
		this.r18_amt_sub_add = r18_amt_sub_add;
	}
	public BigDecimal getR18_amt_sub_del() {
		return r18_amt_sub_del;
	}
	public void setR18_amt_sub_del(BigDecimal r18_amt_sub_del) {
		this.r18_amt_sub_del = r18_amt_sub_del;
	}
	public BigDecimal getR18_amt_total() {
		return r18_amt_total;
	}
	public void setR18_amt_total(BigDecimal r18_amt_total) {
		this.r18_amt_total = r18_amt_total;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_amt() {
		return r19_amt;
	}
	public void setR19_amt(BigDecimal r19_amt) {
		this.r19_amt = r19_amt;
	}
	public BigDecimal getR19_amt_sub_add() {
		return r19_amt_sub_add;
	}
	public void setR19_amt_sub_add(BigDecimal r19_amt_sub_add) {
		this.r19_amt_sub_add = r19_amt_sub_add;
	}
	public BigDecimal getR19_amt_sub_del() {
		return r19_amt_sub_del;
	}
	public void setR19_amt_sub_del(BigDecimal r19_amt_sub_del) {
		this.r19_amt_sub_del = r19_amt_sub_del;
	}
	public BigDecimal getR19_amt_total() {
		return r19_amt_total;
	}
	public void setR19_amt_total(BigDecimal r19_amt_total) {
		this.r19_amt_total = r19_amt_total;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_amt() {
		return r20_amt;
	}
	public void setR20_amt(BigDecimal r20_amt) {
		this.r20_amt = r20_amt;
	}
	public BigDecimal getR20_amt_sub_add() {
		return r20_amt_sub_add;
	}
	public void setR20_amt_sub_add(BigDecimal r20_amt_sub_add) {
		this.r20_amt_sub_add = r20_amt_sub_add;
	}
	public BigDecimal getR20_amt_sub_del() {
		return r20_amt_sub_del;
	}
	public void setR20_amt_sub_del(BigDecimal r20_amt_sub_del) {
		this.r20_amt_sub_del = r20_amt_sub_del;
	}
	public BigDecimal getR20_amt_total() {
		return r20_amt_total;
	}
	public void setR20_amt_total(BigDecimal r20_amt_total) {
		this.r20_amt_total = r20_amt_total;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_amt() {
		return r21_amt;
	}
	public void setR21_amt(BigDecimal r21_amt) {
		this.r21_amt = r21_amt;
	}
	public BigDecimal getR21_amt_sub_add() {
		return r21_amt_sub_add;
	}
	public void setR21_amt_sub_add(BigDecimal r21_amt_sub_add) {
		this.r21_amt_sub_add = r21_amt_sub_add;
	}
	public BigDecimal getR21_amt_sub_del() {
		return r21_amt_sub_del;
	}
	public void setR21_amt_sub_del(BigDecimal r21_amt_sub_del) {
		this.r21_amt_sub_del = r21_amt_sub_del;
	}
	public BigDecimal getR21_amt_total() {
		return r21_amt_total;
	}
	public void setR21_amt_total(BigDecimal r21_amt_total) {
		this.r21_amt_total = r21_amt_total;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_amt() {
		return r22_amt;
	}
	public void setR22_amt(BigDecimal r22_amt) {
		this.r22_amt = r22_amt;
	}
	public BigDecimal getR22_amt_sub_add() {
		return r22_amt_sub_add;
	}
	public void setR22_amt_sub_add(BigDecimal r22_amt_sub_add) {
		this.r22_amt_sub_add = r22_amt_sub_add;
	}
	public BigDecimal getR22_amt_sub_del() {
		return r22_amt_sub_del;
	}
	public void setR22_amt_sub_del(BigDecimal r22_amt_sub_del) {
		this.r22_amt_sub_del = r22_amt_sub_del;
	}
	public BigDecimal getR22_amt_total() {
		return r22_amt_total;
	}
	public void setR22_amt_total(BigDecimal r22_amt_total) {
		this.r22_amt_total = r22_amt_total;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_amt() {
		return r23_amt;
	}
	public void setR23_amt(BigDecimal r23_amt) {
		this.r23_amt = r23_amt;
	}
	public BigDecimal getR23_amt_sub_add() {
		return r23_amt_sub_add;
	}
	public void setR23_amt_sub_add(BigDecimal r23_amt_sub_add) {
		this.r23_amt_sub_add = r23_amt_sub_add;
	}
	public BigDecimal getR23_amt_sub_del() {
		return r23_amt_sub_del;
	}
	public void setR23_amt_sub_del(BigDecimal r23_amt_sub_del) {
		this.r23_amt_sub_del = r23_amt_sub_del;
	}
	public BigDecimal getR23_amt_total() {
		return r23_amt_total;
	}
	public void setR23_amt_total(BigDecimal r23_amt_total) {
		this.r23_amt_total = r23_amt_total;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_amt() {
		return r24_amt;
	}
	public void setR24_amt(BigDecimal r24_amt) {
		this.r24_amt = r24_amt;
	}
	public BigDecimal getR24_amt_sub_add() {
		return r24_amt_sub_add;
	}
	public void setR24_amt_sub_add(BigDecimal r24_amt_sub_add) {
		this.r24_amt_sub_add = r24_amt_sub_add;
	}
	public BigDecimal getR24_amt_sub_del() {
		return r24_amt_sub_del;
	}
	public void setR24_amt_sub_del(BigDecimal r24_amt_sub_del) {
		this.r24_amt_sub_del = r24_amt_sub_del;
	}
	public BigDecimal getR24_amt_total() {
		return r24_amt_total;
	}
	public void setR24_amt_total(BigDecimal r24_amt_total) {
		this.r24_amt_total = r24_amt_total;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_amt() {
		return r25_amt;
	}
	public void setR25_amt(BigDecimal r25_amt) {
		this.r25_amt = r25_amt;
	}
	public BigDecimal getR25_amt_sub_add() {
		return r25_amt_sub_add;
	}
	public void setR25_amt_sub_add(BigDecimal r25_amt_sub_add) {
		this.r25_amt_sub_add = r25_amt_sub_add;
	}
	public BigDecimal getR25_amt_sub_del() {
		return r25_amt_sub_del;
	}
	public void setR25_amt_sub_del(BigDecimal r25_amt_sub_del) {
		this.r25_amt_sub_del = r25_amt_sub_del;
	}
	public BigDecimal getR25_amt_total() {
		return r25_amt_total;
	}
	public void setR25_amt_total(BigDecimal r25_amt_total) {
		this.r25_amt_total = r25_amt_total;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_amt() {
		return r26_amt;
	}
	public void setR26_amt(BigDecimal r26_amt) {
		this.r26_amt = r26_amt;
	}
	public BigDecimal getR26_amt_sub_add() {
		return r26_amt_sub_add;
	}
	public void setR26_amt_sub_add(BigDecimal r26_amt_sub_add) {
		this.r26_amt_sub_add = r26_amt_sub_add;
	}
	public BigDecimal getR26_amt_sub_del() {
		return r26_amt_sub_del;
	}
	public void setR26_amt_sub_del(BigDecimal r26_amt_sub_del) {
		this.r26_amt_sub_del = r26_amt_sub_del;
	}
	public BigDecimal getR26_amt_total() {
		return r26_amt_total;
	}
	public void setR26_amt_total(BigDecimal r26_amt_total) {
		this.r26_amt_total = r26_amt_total;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_amt() {
		return r27_amt;
	}
	public void setR27_amt(BigDecimal r27_amt) {
		this.r27_amt = r27_amt;
	}
	public BigDecimal getR27_amt_sub_add() {
		return r27_amt_sub_add;
	}
	public void setR27_amt_sub_add(BigDecimal r27_amt_sub_add) {
		this.r27_amt_sub_add = r27_amt_sub_add;
	}
	public BigDecimal getR27_amt_sub_del() {
		return r27_amt_sub_del;
	}
	public void setR27_amt_sub_del(BigDecimal r27_amt_sub_del) {
		this.r27_amt_sub_del = r27_amt_sub_del;
	}
	public BigDecimal getR27_amt_total() {
		return r27_amt_total;
	}
	public void setR27_amt_total(BigDecimal r27_amt_total) {
		this.r27_amt_total = r27_amt_total;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_amt() {
		return r28_amt;
	}
	public void setR28_amt(BigDecimal r28_amt) {
		this.r28_amt = r28_amt;
	}
	public BigDecimal getR28_amt_sub_add() {
		return r28_amt_sub_add;
	}
	public void setR28_amt_sub_add(BigDecimal r28_amt_sub_add) {
		this.r28_amt_sub_add = r28_amt_sub_add;
	}
	public BigDecimal getR28_amt_sub_del() {
		return r28_amt_sub_del;
	}
	public void setR28_amt_sub_del(BigDecimal r28_amt_sub_del) {
		this.r28_amt_sub_del = r28_amt_sub_del;
	}
	public BigDecimal getR28_amt_total() {
		return r28_amt_total;
	}
	public void setR28_amt_total(BigDecimal r28_amt_total) {
		this.r28_amt_total = r28_amt_total;
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

//------------------------archival summary  FORMAT_II_Archival_RowMapper

public class FORMAT_II_Archival_Summary_RowMapper
        implements RowMapper<FORMAT_II_Archival_Summary_Entity> {

    @Override
    public FORMAT_II_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        FORMAT_II_Archival_Summary_Entity obj = new FORMAT_II_Archival_Summary_Entity();

        // =========================
        // R13
        // =========================
        obj.setR13_product(rs.getString("r13_product"));
        obj.setR13_amt(rs.getBigDecimal("r13_amt"));
        obj.setR13_amt_sub_add(rs.getBigDecimal("r13_amt_sub_add"));
        obj.setR13_amt_sub_del(rs.getBigDecimal("r13_amt_sub_del"));
        obj.setR13_amt_total(rs.getBigDecimal("r13_amt_total"));

        // =========================
        // R14
        // =========================
        obj.setR14_product(rs.getString("r14_product"));
        obj.setR14_amt(rs.getBigDecimal("r14_amt"));
        obj.setR14_amt_sub_add(rs.getBigDecimal("r14_amt_sub_add"));
        obj.setR14_amt_sub_del(rs.getBigDecimal("r14_amt_sub_del"));
        obj.setR14_amt_total(rs.getBigDecimal("r14_amt_total"));

        // =========================
        // R15
        // =========================
        obj.setR15_product(rs.getString("r15_product"));
        obj.setR15_amt(rs.getBigDecimal("r15_amt"));
        obj.setR15_amt_sub_add(rs.getBigDecimal("r15_amt_sub_add"));
        obj.setR15_amt_sub_del(rs.getBigDecimal("r15_amt_sub_del"));
        obj.setR15_amt_total(rs.getBigDecimal("r15_amt_total"));

        // =========================
        // R16
        // =========================
        obj.setR16_product(rs.getString("r16_product"));
        obj.setR16_amt(rs.getBigDecimal("r16_amt"));
        obj.setR16_amt_sub_add(rs.getBigDecimal("r16_amt_sub_add"));
        obj.setR16_amt_sub_del(rs.getBigDecimal("r16_amt_sub_del"));
        obj.setR16_amt_total(rs.getBigDecimal("r16_amt_total"));

        // =========================
        // R17
        // =========================
        obj.setR17_product(rs.getString("r17_product"));
        obj.setR17_amt(rs.getBigDecimal("r17_amt"));
        obj.setR17_amt_sub_add(rs.getBigDecimal("r17_amt_sub_add"));
        obj.setR17_amt_sub_del(rs.getBigDecimal("r17_amt_sub_del"));
        obj.setR17_amt_total(rs.getBigDecimal("r17_amt_total"));

        // =========================
        // R18
        // =========================
        obj.setR18_product(rs.getString("r18_product"));
        obj.setR18_amt(rs.getBigDecimal("r18_amt"));
        obj.setR18_amt_sub_add(rs.getBigDecimal("r18_amt_sub_add"));
        obj.setR18_amt_sub_del(rs.getBigDecimal("r18_amt_sub_del"));
        obj.setR18_amt_total(rs.getBigDecimal("r18_amt_total"));

        // =========================
        // R19
        // =========================
        obj.setR19_product(rs.getString("r19_product"));
        obj.setR19_amt(rs.getBigDecimal("r19_amt"));
        obj.setR19_amt_sub_add(rs.getBigDecimal("r19_amt_sub_add"));
        obj.setR19_amt_sub_del(rs.getBigDecimal("r19_amt_sub_del"));
        obj.setR19_amt_total(rs.getBigDecimal("r19_amt_total"));

        // =========================
        // R20
        // =========================
        obj.setR20_product(rs.getString("r20_product"));
        obj.setR20_amt(rs.getBigDecimal("r20_amt"));
        obj.setR20_amt_sub_add(rs.getBigDecimal("r20_amt_sub_add"));
        obj.setR20_amt_sub_del(rs.getBigDecimal("r20_amt_sub_del"));
        obj.setR20_amt_total(rs.getBigDecimal("r20_amt_total"));

        // =========================
        // R21
        // =========================
        obj.setR21_product(rs.getString("r21_product"));
        obj.setR21_amt(rs.getBigDecimal("r21_amt"));
        obj.setR21_amt_sub_add(rs.getBigDecimal("r21_amt_sub_add"));
        obj.setR21_amt_sub_del(rs.getBigDecimal("r21_amt_sub_del"));
        obj.setR21_amt_total(rs.getBigDecimal("r21_amt_total"));

        // =========================
        // R22
        // =========================
        obj.setR22_product(rs.getString("r22_product"));
        obj.setR22_amt(rs.getBigDecimal("r22_amt"));
        obj.setR22_amt_sub_add(rs.getBigDecimal("r22_amt_sub_add"));
        obj.setR22_amt_sub_del(rs.getBigDecimal("r22_amt_sub_del"));
        obj.setR22_amt_total(rs.getBigDecimal("r22_amt_total"));

        // =========================
        // R23
        // =========================
        obj.setR23_product(rs.getString("r23_product"));
        obj.setR23_amt(rs.getBigDecimal("r23_amt"));
        obj.setR23_amt_sub_add(rs.getBigDecimal("r23_amt_sub_add"));
        obj.setR23_amt_sub_del(rs.getBigDecimal("r23_amt_sub_del"));
        obj.setR23_amt_total(rs.getBigDecimal("r23_amt_total"));

        // =========================
        // R24
        // =========================
        obj.setR24_product(rs.getString("r24_product"));
        obj.setR24_amt(rs.getBigDecimal("r24_amt"));
        obj.setR24_amt_sub_add(rs.getBigDecimal("r24_amt_sub_add"));
        obj.setR24_amt_sub_del(rs.getBigDecimal("r24_amt_sub_del"));
        obj.setR24_amt_total(rs.getBigDecimal("r24_amt_total"));

        // =========================
        // R25
        // =========================
        obj.setR25_product(rs.getString("r25_product"));
        obj.setR25_amt(rs.getBigDecimal("r25_amt"));
        obj.setR25_amt_sub_add(rs.getBigDecimal("r25_amt_sub_add"));
        obj.setR25_amt_sub_del(rs.getBigDecimal("r25_amt_sub_del"));
        obj.setR25_amt_total(rs.getBigDecimal("r25_amt_total"));

        // =========================
        // R26
        // =========================
        obj.setR26_product(rs.getString("r26_product"));
        obj.setR26_amt(rs.getBigDecimal("r26_amt"));
        obj.setR26_amt_sub_add(rs.getBigDecimal("r26_amt_sub_add"));
        obj.setR26_amt_sub_del(rs.getBigDecimal("r26_amt_sub_del"));
        obj.setR26_amt_total(rs.getBigDecimal("r26_amt_total"));

        // =========================
        // R27
        // =========================
        obj.setR27_product(rs.getString("r27_product"));
        obj.setR27_amt(rs.getBigDecimal("r27_amt"));
        obj.setR27_amt_sub_add(rs.getBigDecimal("r27_amt_sub_add"));
        obj.setR27_amt_sub_del(rs.getBigDecimal("r27_amt_sub_del"));
        obj.setR27_amt_total(rs.getBigDecimal("r27_amt_total"));

        // =========================
        // R28
        // =========================
        obj.setR28_product(rs.getString("r28_product"));
        obj.setR28_amt(rs.getBigDecimal("r28_amt"));
        obj.setR28_amt_sub_add(rs.getBigDecimal("r28_amt_sub_add"));
        obj.setR28_amt_sub_del(rs.getBigDecimal("r28_amt_sub_del"));
        obj.setR28_amt_total(rs.getBigDecimal("r28_amt_total"));

        // =========================
        // COMMON FIELDS
        // =========================
        obj.setReport_date(rs.getDate("report_date"));
        obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
        obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
        obj.setReport_frequency(rs.getString("report_frequency"));
        obj.setReport_code(rs.getString("report_code"));
        obj.setReport_desc(rs.getString("report_desc"));
        obj.setEntity_flg(rs.getString("entity_flg"));
        obj.setModify_flg(rs.getString("modify_flg"));
        obj.setDel_flg(rs.getString("del_flg"));

        return obj;
    }
}


public class FORMAT_II_Archival_Summary_Entity {
	
	

		
	
	private String	r13_product;
	private BigDecimal	r13_amt;
	private BigDecimal	r13_amt_sub_add;
	private BigDecimal	r13_amt_sub_del;
	private BigDecimal	r13_amt_total;
	private String	r14_product;
	private BigDecimal	r14_amt;
	private BigDecimal	r14_amt_sub_add;
	private BigDecimal	r14_amt_sub_del;
	private BigDecimal	r14_amt_total;
	private String	r15_product;
	private BigDecimal	r15_amt;
	private BigDecimal	r15_amt_sub_add;
	private BigDecimal	r15_amt_sub_del;
	private BigDecimal	r15_amt_total;
	private String	r16_product;
	private BigDecimal	r16_amt;
	private BigDecimal	r16_amt_sub_add;
	private BigDecimal	r16_amt_sub_del;
	private BigDecimal	r16_amt_total;
	private String	r17_product;
	private BigDecimal	r17_amt;
	private BigDecimal	r17_amt_sub_add;
	private BigDecimal	r17_amt_sub_del;
	private BigDecimal	r17_amt_total;
	private String	r18_product;
	private BigDecimal	r18_amt;
	private BigDecimal	r18_amt_sub_add;
	private BigDecimal	r18_amt_sub_del;
	private BigDecimal	r18_amt_total;
	private String	r19_product;
	private BigDecimal	r19_amt;
	private BigDecimal	r19_amt_sub_add;
	private BigDecimal	r19_amt_sub_del;
	private BigDecimal	r19_amt_total;
	private String	r20_product;
	private BigDecimal	r20_amt;
	private BigDecimal	r20_amt_sub_add;
	private BigDecimal	r20_amt_sub_del;
	private BigDecimal	r20_amt_total;
	private String	r21_product;
	private BigDecimal	r21_amt;
	private BigDecimal	r21_amt_sub_add;
	private BigDecimal	r21_amt_sub_del;
	private BigDecimal	r21_amt_total;
	private String	r22_product;
	private BigDecimal	r22_amt;
	private BigDecimal	r22_amt_sub_add;
	private BigDecimal	r22_amt_sub_del;
	private BigDecimal	r22_amt_total;
	private String	r23_product;
	private BigDecimal	r23_amt;
	private BigDecimal	r23_amt_sub_add;
	private BigDecimal	r23_amt_sub_del;
	private BigDecimal	r23_amt_total;
	private String	r24_product;
	private BigDecimal	r24_amt;
	private BigDecimal	r24_amt_sub_add;
	private BigDecimal	r24_amt_sub_del;
	private BigDecimal	r24_amt_total;
	private String	r25_product;
	private BigDecimal	r25_amt;
	private BigDecimal	r25_amt_sub_add;
	private BigDecimal	r25_amt_sub_del;
	private BigDecimal	r25_amt_total;
	private String	r26_product;
	private BigDecimal	r26_amt;
	private BigDecimal	r26_amt_sub_add;
	private BigDecimal	r26_amt_sub_del;
	private BigDecimal	r26_amt_total;
	private String	r27_product;
	private BigDecimal	r27_amt;
	private BigDecimal	r27_amt_sub_add;
	private BigDecimal	r27_amt_sub_del;
	private BigDecimal	r27_amt_total;
	private String	r28_product;
	private BigDecimal	r28_amt;
	private BigDecimal	r28_amt_sub_add;
	private BigDecimal	r28_amt_sub_del;
	private BigDecimal	r28_amt_total;


	               
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
	public BigDecimal getR13_amt() {
		return r13_amt;
	}
	public void setR13_amt(BigDecimal r13_amt) {
		this.r13_amt = r13_amt;
	}
	public BigDecimal getR13_amt_sub_add() {
		return r13_amt_sub_add;
	}
	public void setR13_amt_sub_add(BigDecimal r13_amt_sub_add) {
		this.r13_amt_sub_add = r13_amt_sub_add;
	}
	public BigDecimal getR13_amt_sub_del() {
		return r13_amt_sub_del;
	}
	public void setR13_amt_sub_del(BigDecimal r13_amt_sub_del) {
		this.r13_amt_sub_del = r13_amt_sub_del;
	}
	public BigDecimal getR13_amt_total() {
		return r13_amt_total;
	}
	public void setR13_amt_total(BigDecimal r13_amt_total) {
		this.r13_amt_total = r13_amt_total;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_amt() {
		return r14_amt;
	}
	public void setR14_amt(BigDecimal r14_amt) {
		this.r14_amt = r14_amt;
	}
	public BigDecimal getR14_amt_sub_add() {
		return r14_amt_sub_add;
	}
	public void setR14_amt_sub_add(BigDecimal r14_amt_sub_add) {
		this.r14_amt_sub_add = r14_amt_sub_add;
	}
	public BigDecimal getR14_amt_sub_del() {
		return r14_amt_sub_del;
	}
	public void setR14_amt_sub_del(BigDecimal r14_amt_sub_del) {
		this.r14_amt_sub_del = r14_amt_sub_del;
	}
	public BigDecimal getR14_amt_total() {
		return r14_amt_total;
	}
	public void setR14_amt_total(BigDecimal r14_amt_total) {
		this.r14_amt_total = r14_amt_total;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_amt() {
		return r15_amt;
	}
	public void setR15_amt(BigDecimal r15_amt) {
		this.r15_amt = r15_amt;
	}
	public BigDecimal getR15_amt_sub_add() {
		return r15_amt_sub_add;
	}
	public void setR15_amt_sub_add(BigDecimal r15_amt_sub_add) {
		this.r15_amt_sub_add = r15_amt_sub_add;
	}
	public BigDecimal getR15_amt_sub_del() {
		return r15_amt_sub_del;
	}
	public void setR15_amt_sub_del(BigDecimal r15_amt_sub_del) {
		this.r15_amt_sub_del = r15_amt_sub_del;
	}
	public BigDecimal getR15_amt_total() {
		return r15_amt_total;
	}
	public void setR15_amt_total(BigDecimal r15_amt_total) {
		this.r15_amt_total = r15_amt_total;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_amt() {
		return r16_amt;
	}
	public void setR16_amt(BigDecimal r16_amt) {
		this.r16_amt = r16_amt;
	}
	public BigDecimal getR16_amt_sub_add() {
		return r16_amt_sub_add;
	}
	public void setR16_amt_sub_add(BigDecimal r16_amt_sub_add) {
		this.r16_amt_sub_add = r16_amt_sub_add;
	}
	public BigDecimal getR16_amt_sub_del() {
		return r16_amt_sub_del;
	}
	public void setR16_amt_sub_del(BigDecimal r16_amt_sub_del) {
		this.r16_amt_sub_del = r16_amt_sub_del;
	}
	public BigDecimal getR16_amt_total() {
		return r16_amt_total;
	}
	public void setR16_amt_total(BigDecimal r16_amt_total) {
		this.r16_amt_total = r16_amt_total;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_amt() {
		return r17_amt;
	}
	public void setR17_amt(BigDecimal r17_amt) {
		this.r17_amt = r17_amt;
	}
	public BigDecimal getR17_amt_sub_add() {
		return r17_amt_sub_add;
	}
	public void setR17_amt_sub_add(BigDecimal r17_amt_sub_add) {
		this.r17_amt_sub_add = r17_amt_sub_add;
	}
	public BigDecimal getR17_amt_sub_del() {
		return r17_amt_sub_del;
	}
	public void setR17_amt_sub_del(BigDecimal r17_amt_sub_del) {
		this.r17_amt_sub_del = r17_amt_sub_del;
	}
	public BigDecimal getR17_amt_total() {
		return r17_amt_total;
	}
	public void setR17_amt_total(BigDecimal r17_amt_total) {
		this.r17_amt_total = r17_amt_total;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_amt() {
		return r18_amt;
	}
	public void setR18_amt(BigDecimal r18_amt) {
		this.r18_amt = r18_amt;
	}
	public BigDecimal getR18_amt_sub_add() {
		return r18_amt_sub_add;
	}
	public void setR18_amt_sub_add(BigDecimal r18_amt_sub_add) {
		this.r18_amt_sub_add = r18_amt_sub_add;
	}
	public BigDecimal getR18_amt_sub_del() {
		return r18_amt_sub_del;
	}
	public void setR18_amt_sub_del(BigDecimal r18_amt_sub_del) {
		this.r18_amt_sub_del = r18_amt_sub_del;
	}
	public BigDecimal getR18_amt_total() {
		return r18_amt_total;
	}
	public void setR18_amt_total(BigDecimal r18_amt_total) {
		this.r18_amt_total = r18_amt_total;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_amt() {
		return r19_amt;
	}
	public void setR19_amt(BigDecimal r19_amt) {
		this.r19_amt = r19_amt;
	}
	public BigDecimal getR19_amt_sub_add() {
		return r19_amt_sub_add;
	}
	public void setR19_amt_sub_add(BigDecimal r19_amt_sub_add) {
		this.r19_amt_sub_add = r19_amt_sub_add;
	}
	public BigDecimal getR19_amt_sub_del() {
		return r19_amt_sub_del;
	}
	public void setR19_amt_sub_del(BigDecimal r19_amt_sub_del) {
		this.r19_amt_sub_del = r19_amt_sub_del;
	}
	public BigDecimal getR19_amt_total() {
		return r19_amt_total;
	}
	public void setR19_amt_total(BigDecimal r19_amt_total) {
		this.r19_amt_total = r19_amt_total;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_amt() {
		return r20_amt;
	}
	public void setR20_amt(BigDecimal r20_amt) {
		this.r20_amt = r20_amt;
	}
	public BigDecimal getR20_amt_sub_add() {
		return r20_amt_sub_add;
	}
	public void setR20_amt_sub_add(BigDecimal r20_amt_sub_add) {
		this.r20_amt_sub_add = r20_amt_sub_add;
	}
	public BigDecimal getR20_amt_sub_del() {
		return r20_amt_sub_del;
	}
	public void setR20_amt_sub_del(BigDecimal r20_amt_sub_del) {
		this.r20_amt_sub_del = r20_amt_sub_del;
	}
	public BigDecimal getR20_amt_total() {
		return r20_amt_total;
	}
	public void setR20_amt_total(BigDecimal r20_amt_total) {
		this.r20_amt_total = r20_amt_total;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_amt() {
		return r21_amt;
	}
	public void setR21_amt(BigDecimal r21_amt) {
		this.r21_amt = r21_amt;
	}
	public BigDecimal getR21_amt_sub_add() {
		return r21_amt_sub_add;
	}
	public void setR21_amt_sub_add(BigDecimal r21_amt_sub_add) {
		this.r21_amt_sub_add = r21_amt_sub_add;
	}
	public BigDecimal getR21_amt_sub_del() {
		return r21_amt_sub_del;
	}
	public void setR21_amt_sub_del(BigDecimal r21_amt_sub_del) {
		this.r21_amt_sub_del = r21_amt_sub_del;
	}
	public BigDecimal getR21_amt_total() {
		return r21_amt_total;
	}
	public void setR21_amt_total(BigDecimal r21_amt_total) {
		this.r21_amt_total = r21_amt_total;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_amt() {
		return r22_amt;
	}
	public void setR22_amt(BigDecimal r22_amt) {
		this.r22_amt = r22_amt;
	}
	public BigDecimal getR22_amt_sub_add() {
		return r22_amt_sub_add;
	}
	public void setR22_amt_sub_add(BigDecimal r22_amt_sub_add) {
		this.r22_amt_sub_add = r22_amt_sub_add;
	}
	public BigDecimal getR22_amt_sub_del() {
		return r22_amt_sub_del;
	}
	public void setR22_amt_sub_del(BigDecimal r22_amt_sub_del) {
		this.r22_amt_sub_del = r22_amt_sub_del;
	}
	public BigDecimal getR22_amt_total() {
		return r22_amt_total;
	}
	public void setR22_amt_total(BigDecimal r22_amt_total) {
		this.r22_amt_total = r22_amt_total;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_amt() {
		return r23_amt;
	}
	public void setR23_amt(BigDecimal r23_amt) {
		this.r23_amt = r23_amt;
	}
	public BigDecimal getR23_amt_sub_add() {
		return r23_amt_sub_add;
	}
	public void setR23_amt_sub_add(BigDecimal r23_amt_sub_add) {
		this.r23_amt_sub_add = r23_amt_sub_add;
	}
	public BigDecimal getR23_amt_sub_del() {
		return r23_amt_sub_del;
	}
	public void setR23_amt_sub_del(BigDecimal r23_amt_sub_del) {
		this.r23_amt_sub_del = r23_amt_sub_del;
	}
	public BigDecimal getR23_amt_total() {
		return r23_amt_total;
	}
	public void setR23_amt_total(BigDecimal r23_amt_total) {
		this.r23_amt_total = r23_amt_total;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_amt() {
		return r24_amt;
	}
	public void setR24_amt(BigDecimal r24_amt) {
		this.r24_amt = r24_amt;
	}
	public BigDecimal getR24_amt_sub_add() {
		return r24_amt_sub_add;
	}
	public void setR24_amt_sub_add(BigDecimal r24_amt_sub_add) {
		this.r24_amt_sub_add = r24_amt_sub_add;
	}
	public BigDecimal getR24_amt_sub_del() {
		return r24_amt_sub_del;
	}
	public void setR24_amt_sub_del(BigDecimal r24_amt_sub_del) {
		this.r24_amt_sub_del = r24_amt_sub_del;
	}
	public BigDecimal getR24_amt_total() {
		return r24_amt_total;
	}
	public void setR24_amt_total(BigDecimal r24_amt_total) {
		this.r24_amt_total = r24_amt_total;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_amt() {
		return r25_amt;
	}
	public void setR25_amt(BigDecimal r25_amt) {
		this.r25_amt = r25_amt;
	}
	public BigDecimal getR25_amt_sub_add() {
		return r25_amt_sub_add;
	}
	public void setR25_amt_sub_add(BigDecimal r25_amt_sub_add) {
		this.r25_amt_sub_add = r25_amt_sub_add;
	}
	public BigDecimal getR25_amt_sub_del() {
		return r25_amt_sub_del;
	}
	public void setR25_amt_sub_del(BigDecimal r25_amt_sub_del) {
		this.r25_amt_sub_del = r25_amt_sub_del;
	}
	public BigDecimal getR25_amt_total() {
		return r25_amt_total;
	}
	public void setR25_amt_total(BigDecimal r25_amt_total) {
		this.r25_amt_total = r25_amt_total;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_amt() {
		return r26_amt;
	}
	public void setR26_amt(BigDecimal r26_amt) {
		this.r26_amt = r26_amt;
	}
	public BigDecimal getR26_amt_sub_add() {
		return r26_amt_sub_add;
	}
	public void setR26_amt_sub_add(BigDecimal r26_amt_sub_add) {
		this.r26_amt_sub_add = r26_amt_sub_add;
	}
	public BigDecimal getR26_amt_sub_del() {
		return r26_amt_sub_del;
	}
	public void setR26_amt_sub_del(BigDecimal r26_amt_sub_del) {
		this.r26_amt_sub_del = r26_amt_sub_del;
	}
	public BigDecimal getR26_amt_total() {
		return r26_amt_total;
	}
	public void setR26_amt_total(BigDecimal r26_amt_total) {
		this.r26_amt_total = r26_amt_total;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_amt() {
		return r27_amt;
	}
	public void setR27_amt(BigDecimal r27_amt) {
		this.r27_amt = r27_amt;
	}
	public BigDecimal getR27_amt_sub_add() {
		return r27_amt_sub_add;
	}
	public void setR27_amt_sub_add(BigDecimal r27_amt_sub_add) {
		this.r27_amt_sub_add = r27_amt_sub_add;
	}
	public BigDecimal getR27_amt_sub_del() {
		return r27_amt_sub_del;
	}
	public void setR27_amt_sub_del(BigDecimal r27_amt_sub_del) {
		this.r27_amt_sub_del = r27_amt_sub_del;
	}
	public BigDecimal getR27_amt_total() {
		return r27_amt_total;
	}
	public void setR27_amt_total(BigDecimal r27_amt_total) {
		this.r27_amt_total = r27_amt_total;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_amt() {
		return r28_amt;
	}
	public void setR28_amt(BigDecimal r28_amt) {
		this.r28_amt = r28_amt;
	}
	public BigDecimal getR28_amt_sub_add() {
		return r28_amt_sub_add;
	}
	public void setR28_amt_sub_add(BigDecimal r28_amt_sub_add) {
		this.r28_amt_sub_add = r28_amt_sub_add;
	}
	public BigDecimal getR28_amt_sub_del() {
		return r28_amt_sub_del;
	}
	public void setR28_amt_sub_del(BigDecimal r28_amt_sub_del) {
		this.r28_amt_sub_del = r28_amt_sub_del;
	}
	public BigDecimal getR28_amt_total() {
		return r28_amt_total;
	}
	public void setR28_amt_total(BigDecimal r28_amt_total) {
		this.r28_amt_total = r28_amt_total;
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

//------------------------detail 

public class FORMAT_II_Detail_RowMapper 
        implements RowMapper<FORMAT_II_Detail_Entity> {

    @Override
    public FORMAT_II_Detail_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        FORMAT_II_Detail_Entity obj = new FORMAT_II_Detail_Entity();

        // =========================
        // BASIC DETAILS
        // =========================
        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));
        obj.setDataType(rs.getString("DATA_TYPE"));
        obj.setReportName(rs.getString("REPORT_NAME"));

        // =========================
        // REPORT INFO
        // =========================
        obj.setReportLabel(rs.getString("REPORT_LABEL"));
        obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

        // =========================
        // AMOUNTS
        // =========================
        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
        obj.setAverage(rs.getBigDecimal("AVERAGE"));

        // =========================
        // GL CODES
        // =========================
        obj.setGlshCode(rs.getString("GLSH_CODE"));
        obj.setGlCode(rs.getString("GL_CODE"));

        // =========================
        // DATES
        // =========================
        obj.setReportDate(rs.getDate("REPORT_DATE"));
        obj.setCreateUser(rs.getString("CREATE_USER"));
        obj.setCreateTime(rs.getDate("CREATE_TIME"));
        obj.setModifyUser(rs.getString("MODIFY_USER"));
        obj.setModifyTime(rs.getDate("MODIFY_TIME"));
        obj.setVerifyUser(rs.getString("VERIFY_USER"));
        obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

        // =========================
        // FLAGS
        // =========================
        obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');
        obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');
        obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

        return obj;
    }
}

public class FORMAT_II_Detail_Entity {

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

    @Column(name = "AVERAGE", precision = 24, scale = 3)
    private BigDecimal average;

    @Column(name = "GLSH_CODE")
    private String glshCode;

    @Column(name = "GL_CODE")
    private String glCode;

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

    // =========================
    // GETTERS & SETTERS
    // =========================

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

    public BigDecimal getAverage() {
        return average;
    }

    public void setAverage(BigDecimal average) {
        this.average = average;
    }

    public String getGlshCode() {
        return glshCode;
    }

    public void setGlshCode(String glshCode) {
        this.glshCode = glshCode;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
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


//--------------------archival deail 


public class FORMAT_II_Archival_Detail_RowMapper 
        implements RowMapper<FORMAT_II_Archival_Detail_Entity> {

    @Override
    public FORMAT_II_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        FORMAT_II_Archival_Detail_Entity obj = new FORMAT_II_Archival_Detail_Entity();

        // =========================
        // BASIC DETAILS
        // =========================
        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));
        obj.setDataType(rs.getString("DATA_TYPE"));
        obj.setReportName(rs.getString("REPORT_NAME"));

        // =========================
        // REPORT INFO
        // =========================
        obj.setReportLabel(rs.getString("REPORT_LABEL"));
        obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

        // =========================
        // AMOUNTS
        // =========================
        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
        obj.setAverage(rs.getBigDecimal("AVERAGE"));

        // =========================
        // GL CODES
        // =========================
        obj.setGlshCode(rs.getString("GLSH_CODE"));
        obj.setGlCode(rs.getString("GL_CODE"));

        // =========================
        // DATES
        // =========================
        obj.setReportDate(rs.getDate("REPORT_DATE"));
        obj.setCreateUser(rs.getString("CREATE_USER"));
        obj.setCreateTime(rs.getDate("CREATE_TIME"));
        obj.setModifyUser(rs.getString("MODIFY_USER"));
        obj.setModifyTime(rs.getDate("MODIFY_TIME"));
        obj.setVerifyUser(rs.getString("VERIFY_USER"));
        obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

        // =========================
        // FLAGS
        // =========================
        obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');
        obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');
        obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

        return obj;
    }
}


public class FORMAT_II_Archival_Detail_Entity {

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

    @Id
    @Column(name = "DATA_ENTRY_VERSION")
    private String dataEntryVersion;

    @Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
    private BigDecimal acctBalanceInpula;

    @Column(name = "AVERAGE", precision = 24, scale = 3)
    private BigDecimal average;

    @Column(name = "GLSH_CODE")
    private String glshCode;

    @Column(name = "GL_CODE")
    private String glCode;

    @Id
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

    // =========================
    // GETTERS & SETTERS
    // =========================

    public String getCustId() { return custId; }
    public void setCustId(String custId) { this.custId = custId; }

    public String getAcctNumber() { return acctNumber; }
    public void setAcctNumber(String acctNumber) { this.acctNumber = acctNumber; }

    public String getAcctName() { return acctName; }
    public void setAcctName(String acctName) { this.acctName = acctName; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }

    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }

    public String getReportLabel() { return reportLabel; }
    public void setReportLabel(String reportLabel) { this.reportLabel = reportLabel; }

    public String getReportAddlCriteria_1() { return reportAddlCriteria_1; }
    public void setReportAddlCriteria_1(String reportAddlCriteria_1) { this.reportAddlCriteria_1 = reportAddlCriteria_1; }

    public String getReportRemarks() { return reportRemarks; }
    public void setReportRemarks(String reportRemarks) { this.reportRemarks = reportRemarks; }

    public String getModificationRemarks() { return modificationRemarks; }
    public void setModificationRemarks(String modificationRemarks) { this.modificationRemarks = modificationRemarks; }

    public String getDataEntryVersion() { return dataEntryVersion; }
    public void setDataEntryVersion(String dataEntryVersion) { this.dataEntryVersion = dataEntryVersion; }

    public BigDecimal getAcctBalanceInpula() { return acctBalanceInpula; }
    public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) { this.acctBalanceInpula = acctBalanceInpula; }

    public BigDecimal getAverage() { return average; }
    public void setAverage(BigDecimal average) { this.average = average; }

    public String getGlshCode() { return glshCode; }
    public void setGlshCode(String glshCode) { this.glshCode = glshCode; }

    public String getGlCode() { return glCode; }
    public void setGlCode(String glCode) { this.glCode = glCode; }

    public Date getReportDate() { return reportDate; }
    public void setReportDate(Date reportDate) { this.reportDate = reportDate; }

    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getModifyUser() { return modifyUser; }
    public void setModifyUser(String modifyUser) { this.modifyUser = modifyUser; }

    public Date getModifyTime() { return modifyTime; }
    public void setModifyTime(Date modifyTime) { this.modifyTime = modifyTime; }

    public String getVerifyUser() { return verifyUser; }
    public void setVerifyUser(String verifyUser) { this.verifyUser = verifyUser; }

    public Date getVerifyTime() { return verifyTime; }
    public void setVerifyTime(Date verifyTime) { this.verifyTime = verifyTime; }

    public char getEntityFlg() { return entityFlg; }
    public void setEntityFlg(char entityFlg) { this.entityFlg = entityFlg; }

    public char getModifyFlg() { return modifyFlg; }
    public void setModifyFlg(char modifyFlg) { this.modifyFlg = modifyFlg; }

    public char getDelFlg() { return delFlg; }
    public void setDelFlg(char delFlg) { this.delFlg = delFlg; }
}

  //=====================================================
 // MODEL AND VIEW METHOD summary
 //=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
		 
		 	 public ModelAndView getFORMAT_IIView(

	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("FORMAT_II View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    // =====================================================
	    // ARCHIVAL MODE
	    // =====================================================

	    if ("ARCHIVAL".equals(type) && version != null) {

	        List<FORMAT_II_Archival_Summary_Entity> T1Master = new ArrayList<>();
	    

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

	        List<FORMAT_II_Summary_Entity> T1Master = new ArrayList<>();
	       

	        try {
	            Date dt = dateformat.parse(todate);

	            // SUMMARY NORMAL
	            T1Master = getDataByDate(dt);

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

	    mv.setViewName("BRRS/FORMAT_II");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getFORMAT_IIcurrentDtl(
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

	            List<FORMAT_II_Archival_Detail_Entity> archivalDetailList;

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

	            List<FORMAT_II_Detail_Entity> currentDetailList;

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

	    mv.setViewName("BRRS/FORMAT_II");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
	
	//Archival View
	
		public List<Object[]> getFORMAT_IIArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<FORMAT_II_Archival_Summary_Entity> repoData =
						getDataByDateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (FORMAT_II_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					FORMAT_II_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  format_II  Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}
		
	//UPDate report
	
	
		public void updateReport(FORMAT_II_Summary_Entity updatedEntity) {

		    System.out.println("Came to format_II Manual Update");
		    System.out.println("Report Date: " + updatedEntity.getReport_date());

		    int[] rows = {21, 25};

		    try {

		        for (int row : rows) {

		            String[] fields;

		            if (row == 21) {
		                fields = new String[] { "amt" };              // R21_amt
		            } else if (row == 25) {
		                fields = new String[] { "amt_sub_del" };      // R25_amt_sub_del
		            } else {
		                continue;
		            }

		            for (String field : fields) {

		                String getterName = "getR" + row + "_" + field;

		                try {
		                    Method getter =
		                            FORMAT_II_Summary_Entity.class.getMethod(getterName);

		                    Object value = getter.invoke(updatedEntity);

		                    if (value == null) continue;

		                    // ✅ FIXED HERE
		                    String columnName = "R" + row + "_" + field;

		                    String sql =
		                            "UPDATE BRRS_FORMAT_II_SUMMARYTABLE " +
		                            "SET " + columnName + " = ? " +
		                            "WHERE REPORT_DATE = ?";

		                    jdbcTemplate.update(
		                            sql,
		                            value,
		                            updatedEntity.getReport_date()
		                    );

		                } catch (NoSuchMethodException e) {
		                    continue;
		                }
		            }
		        }

		        System.out.println("FORMAT_II Manual Update Completed");

		    } catch (Exception e) {
		        throw new RuntimeException(
		                "Error while updating FORMAT_II Manual fields", e);
		    }
		}
	
	//---------------getViewOrEditPage
	
	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/FORMAT_II"); 

		if (acctNo != null) {
			FORMAT_II_Detail_Entity formate_IIEntity = findByDetailAcctnumber(acctNo);
			if (formate_IIEntity != null && formate_IIEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(formate_IIEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("formate_IIData", formate_IIEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}
	
	//----------------------updateDetailEdit
	
	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("acctBalanceInpula");
			String average = request.getParameter("average");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			FORMAT_II_Detail_Entity existing = findByDetailAcctnumber(acctNo);
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
			 
			 
			 if (average != null && !average.isEmpty()) {
		            BigDecimal newaverage = new BigDecimal(average);
		            if (existing.getAverage()  == null ||
		                existing.getAverage().compareTo(newaverage) != 0) {
		            	 existing.setAverage(newaverage);
		                isChanged = true;
		                logger.info("Balance updated to {}", newaverage);
		            }
		        }
		        
			if (isChanged) {
				  String sql =
    "UPDATE BRRS_FORMAT_II_DETAILTABLE " +
    "SET ACCT_NAME = ?, " +
    "ACCT_BALANCE_IN_PULA = ?, " +
    "AVERAGE = ? " +
    "WHERE ACCT_NUMBER = ?";

		           jdbcTemplate.update(
    sql,
    existing.getAcctName(),
    existing.getAcctBalanceInpula(),
    existing.getAverage(),
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
							logger.info("Transaction committed — calling BRRS_FORMAT_II_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_FORMAT_II_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating FORMAT_II record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
	
		public byte[] getFORMAT_IIDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  FORMAT_II  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getFORMAT_IIDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("FORMAT_II Details ");

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
				String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "AVERAGE","REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

				XSSFRow headerRow = sheet.createRow(0);
				for (int i = 0; i < headers.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(headers[i]);

					if (i == 3 || i == 4 ) {  // MONTHLY_INT (3) and CREDIT_EQUIVALENT (4) nd DEBIT_EQUIVALENT(5)
				    cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
				    cell.setCellStyle(headerStyle);
				}

					sheet.setColumnWidth(i, 5000);
				}

				// Get data
				Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
				List<FORMAT_II_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (FORMAT_II_Detail_Entity item : reportData) { 
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
					// AVERAGE
					 balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						 if (j != 3 && j != 4 ) {
							row.getCell(j).setCellStyle(dataStyle);
						}
						}
					}
				} else {
					logger.info("No data found for FORMAT_II — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating FORMAT_II Excel", e);
				return new byte[0];
			}
		}
		
		
		//===========================================getFORMAT_IIDetailExcelARCHIVAL	


		public byte[] getFORMAT_IIDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for FORMAT_II ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("FORMAT_II Detail NEW");

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
				String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "AVERAGE", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

				XSSFRow headerRow = sheet.createRow(0);
				for (int i = 0; i < headers.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(headers[i]);

						if (i == 3 || i == 4 ) {  // MONTHLY_INT (3) and CREDIT_EQUIVALENT (4) nd DEBIT_EQUIVALENT(5)
				    cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
				    cell.setCellStyle(headerStyle);
				}

					sheet.setColumnWidth(i, 5000);
				}

	// Get data
				Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
				List<FORMAT_II_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (FORMAT_II_Archival_Detail_Entity item : reportData) {
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

					// AVERAGE
					 balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						 if (j != 3 && j != 4 ) {
							row.getCell(j).setCellStyle(dataStyle);
						}
						}
					}
				} else {
					logger.info("No data found for FORMAT_II — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating FORMAT_II NEW Excel", e);
				return new byte[0];
			}
		}
		
		
		//===========================EXCEL 

		
			public byte[] getFORMAT_IIExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.FORMAT_II");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelFORMAT_IIARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<FORMAT_II_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));
		
		
			System.out.println("DATA SIZE IS : "+dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  FORMAT_II report. Returning empty result.");
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

						int startRow = 12;
						
				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						FORMAT_II_Summary_Entity record = dataList.get(i);
					
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

   // ROW 13
					// Column E - 
					Cell cellE = row.createCell(4);
					if (record.getR13_amt() != null) {
					    cellE.setCellValue(record.getR13_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}
					
					
					
					// Column F - 01.04.2025
					Cell cellF = row.createCell(5);
					if (record.getR13_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR13_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}
					
					
					// Column G - Addition As on  30.06.2025
					Cell cellG = row.createCell(6);
					if (record.getR13_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR13_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}
					
					
					// Column H - Total as at 30.06.2025
					Cell cellH = row.createCell(7);
					if (record.getR13_amt_total() != null) {
						cellH.setCellValue(record.getR13_amt_total().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}
					
					/* ===================== ROW 14 ===================== */
					// Column E
					row = sheet.getRow(13);
					 cellE = row.createCell(4);
					if (record.getR14_amt() != null) {
					    cellE.setCellValue(record.getR14_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column F
					 cellF = row.createCell(5);
					if (record.getR14_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR14_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column G
					 cellG = row.createCell(6);
					if (record.getR14_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR14_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// Column H
					 cellH = row.createCell(7);
					if (record.getR14_amt_total() != null) {
					    cellH.setCellValue(record.getR14_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 15 ===================== */
					row = sheet.getRow(14);
					Cell cellE15 = row.createCell(4);
					if (record.getR15_amt() != null) {
					    cellE15.setCellValue(record.getR15_amt().doubleValue());
					    cellE15.setCellStyle(numberStyle);
					} else {
					    cellE15.setCellValue("");
					    cellE15.setCellStyle(textStyle);
					}

					Cell cellF15 = row.createCell(5);
					if (record.getR15_amt_sub_add() != null) {
					    cellF15.setCellValue(record.getR15_amt_sub_add().doubleValue());
					    cellF15.setCellStyle(numberStyle);
					} else {
					    cellF15.setCellValue("");
					    cellF15.setCellStyle(textStyle);
					}

					Cell cellG15 = row.createCell(6);
					if (record.getR15_amt_sub_del() != null) {
					    cellG15.setCellValue(record.getR15_amt_sub_del().doubleValue());
					    cellG15.setCellStyle(numberStyle);
					} else {
					    cellG15.setCellValue("");
					    cellG15.setCellStyle(textStyle);
					}

					Cell cellH15 = row.createCell(7);
					if (record.getR15_amt_total() != null) {
					    cellH15.setCellValue(record.getR15_amt_total().doubleValue());
					    cellH15.setCellStyle(numberStyle);
					} else {
					    cellH15.setCellValue("");
					    cellH15.setCellStyle(textStyle);
					}
					
					/* ===================== ROW 16 ===================== */
					row = sheet.getRow(15);
					cellE = row.createCell(4);
					if (record.getR16_amt() != null) {
					    cellE.setCellValue(record.getR16_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR16_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR16_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR16_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR16_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR16_amt_total() != null) {
					    cellH.setCellValue(record.getR16_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 17 ===================== */
					row = sheet.getRow(16);
					cellE = row.createCell(4);
					if (record.getR17_amt() != null) {
					    cellE.setCellValue(record.getR17_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR17_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR17_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR17_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR17_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR17_amt_total() != null) {
					    cellH.setCellValue(record.getR17_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 18 ===================== */
					row = sheet.getRow(17);
					cellE = row.createCell(4);
					if (record.getR18_amt() != null) {
					    cellE.setCellValue(record.getR18_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR18_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR18_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR18_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR18_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR18_amt_total() != null) {
					    cellH.setCellValue(record.getR18_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 19 ===================== */
					row = sheet.getRow(18);
					cellE = row.createCell(4);
					if (record.getR19_amt() != null) {
					    cellE.setCellValue(record.getR19_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR19_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR19_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR19_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR19_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR19_amt_total() != null) {
					    cellH.setCellValue(record.getR19_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 20 ===================== */
					row = sheet.getRow(19);
					cellE = row.createCell(4);
					if (record.getR20_amt() != null) {
					    cellE.setCellValue(record.getR20_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR20_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR20_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR20_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR20_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR20_amt_total() != null) {
					    cellH.setCellValue(record.getR20_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 21 ===================== */
					row = sheet.getRow(20);
					cellE = row.createCell(4);
					if (record.getR21_amt() != null) {
					    cellE.setCellValue(record.getR21_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR21_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR21_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR21_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR21_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR21_amt_total() != null) {
					    cellH.setCellValue(record.getR21_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 22 ===================== */
					row = sheet.getRow(21);
					cellE = row.createCell(4);
					if (record.getR22_amt() != null) {
					    cellE.setCellValue(record.getR22_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR22_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR22_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR22_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR22_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR22_amt_total() != null) {
					    cellH.setCellValue(record.getR22_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 23 ===================== */
					row = sheet.getRow(22);
					cellE = row.createCell(4);
					if (record.getR23_amt() != null) {
					    cellE.setCellValue(record.getR23_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR23_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR23_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR23_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR23_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR23_amt_total() != null) {
					    cellH.setCellValue(record.getR23_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 24 ===================== */
					row = sheet.getRow(23);
					cellE = row.createCell(4);
					if (record.getR24_amt() != null) {
					    cellE.setCellValue(record.getR24_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR24_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR24_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR24_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR24_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR24_amt_total() != null) {
					    cellH.setCellValue(record.getR24_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 25 ===================== */
					row = sheet.getRow(24);
					cellE = row.createCell(4);
					if (record.getR25_amt() != null) {
					    cellE.setCellValue(record.getR25_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR25_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR25_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR25_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR25_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR25_amt_total() != null) {
					    cellH.setCellValue(record.getR25_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 26 ===================== */
					row = sheet.getRow(25);
					cellE = row.createCell(4);
					if (record.getR26_amt() != null) {
					    cellE.setCellValue(record.getR26_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR26_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR26_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR26_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR26_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR26_amt_total() != null) {
					    cellH.setCellValue(record.getR26_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 27 ===================== */
					row = sheet.getRow(26);
					cellE = row.createCell(4);
					if (record.getR27_amt() != null) {
					    cellE.setCellValue(record.getR27_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR27_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR27_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR27_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR27_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR27_amt_total() != null) {
					    cellH.setCellValue(record.getR27_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 28 ===================== */
					row = sheet.getRow(27);
					cellE = row.createCell(4);
					if (record.getR28_amt() != null) {
					    cellE.setCellValue(record.getR28_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR28_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR28_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR28_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR28_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR28_amt_total() != null) {
					    cellH.setCellValue(record.getR28_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
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
		
		
				public byte[] getExcelFORMAT_IIARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {

			}

			List<FORMAT_II_Archival_Summary_Entity> dataList = 
					getDataByDateListArchival(dateformat.parse(todate), version);
		
	    

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for FORMAT_II new report. Returning empty result.");
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

				int startRow = 12;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						FORMAT_II_Archival_Summary_Entity record = dataList.get(i);
					    
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


							    // ROW 13
									// Column E - 
									Cell cellE = row.createCell(4);
									if (record.getR13_amt() != null) {
									    cellE.setCellValue(record.getR13_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}
									
									
									
									// Column F - 01.04.2025
									Cell cellF = row.createCell(5);
									if (record.getR13_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR13_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}
									
									
									// Column G - Addition As on  30.06.2025
									Cell cellG = row.createCell(6);
									if (record.getR13_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR13_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}
									
									
									// Column H - Total as at 30.06.2025
									Cell cellH = row.createCell(7);
									if (record.getR13_amt_total() != null) {
										cellH.setCellValue(record.getR13_amt_total().doubleValue());
										cellH.setCellStyle(numberStyle);
									} else {
										cellH.setCellValue("");
										cellH.setCellStyle(textStyle);
									}
									
									/* ===================== ROW 14 ===================== */
									// Column E
									row = sheet.getRow(13);
									 cellE = row.createCell(4);
									if (record.getR14_amt() != null) {
									    cellE.setCellValue(record.getR14_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									// Column F
									 cellF = row.createCell(5);
									if (record.getR14_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR14_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									// Column G
									 cellG = row.createCell(6);
									if (record.getR14_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR14_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									// Column H
									 cellH = row.createCell(7);
									if (record.getR14_amt_total() != null) {
									    cellH.setCellValue(record.getR14_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 15 ===================== */
									row = sheet.getRow(14);
									Cell cellE15 = row.createCell(4);
									if (record.getR15_amt() != null) {
									    cellE15.setCellValue(record.getR15_amt().doubleValue());
									    cellE15.setCellStyle(numberStyle);
									} else {
									    cellE15.setCellValue("");
									    cellE15.setCellStyle(textStyle);
									}

									Cell cellF15 = row.createCell(5);
									if (record.getR15_amt_sub_add() != null) {
									    cellF15.setCellValue(record.getR15_amt_sub_add().doubleValue());
									    cellF15.setCellStyle(numberStyle);
									} else {
									    cellF15.setCellValue("");
									    cellF15.setCellStyle(textStyle);
									}

									Cell cellG15 = row.createCell(6);
									if (record.getR15_amt_sub_del() != null) {
									    cellG15.setCellValue(record.getR15_amt_sub_del().doubleValue());
									    cellG15.setCellStyle(numberStyle);
									} else {
									    cellG15.setCellValue("");
									    cellG15.setCellStyle(textStyle);
									}

									Cell cellH15 = row.createCell(7);
									if (record.getR15_amt_total() != null) {
									    cellH15.setCellValue(record.getR15_amt_total().doubleValue());
									    cellH15.setCellStyle(numberStyle);
									} else {
									    cellH15.setCellValue("");
									    cellH15.setCellStyle(textStyle);
									}
									
									/* ===================== ROW 16 ===================== */
									row = sheet.getRow(15);
									cellE = row.createCell(4);
									if (record.getR16_amt() != null) {
									    cellE.setCellValue(record.getR16_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR16_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR16_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR16_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR16_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR16_amt_total() != null) {
									    cellH.setCellValue(record.getR16_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 17 ===================== */
									row = sheet.getRow(16);
									cellE = row.createCell(4);
									if (record.getR17_amt() != null) {
									    cellE.setCellValue(record.getR17_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR17_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR17_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR17_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR17_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR17_amt_total() != null) {
									    cellH.setCellValue(record.getR17_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 18 ===================== */
									row = sheet.getRow(17);
									cellE = row.createCell(4);
									if (record.getR18_amt() != null) {
									    cellE.setCellValue(record.getR18_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR18_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR18_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR18_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR18_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR18_amt_total() != null) {
									    cellH.setCellValue(record.getR18_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 19 ===================== */
									row = sheet.getRow(18);
									cellE = row.createCell(4);
									if (record.getR19_amt() != null) {
									    cellE.setCellValue(record.getR19_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR19_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR19_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR19_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR19_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR19_amt_total() != null) {
									    cellH.setCellValue(record.getR19_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 20 ===================== */
									row = sheet.getRow(19);
									cellE = row.createCell(4);
									if (record.getR20_amt() != null) {
									    cellE.setCellValue(record.getR20_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR20_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR20_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR20_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR20_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR20_amt_total() != null) {
									    cellH.setCellValue(record.getR20_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 21 ===================== */
									row = sheet.getRow(20);
									cellE = row.createCell(4);
									if (record.getR21_amt() != null) {
									    cellE.setCellValue(record.getR21_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR21_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR21_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR21_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR21_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR21_amt_total() != null) {
									    cellH.setCellValue(record.getR21_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 22 ===================== */
									row = sheet.getRow(21);
									cellE = row.createCell(4);
									if (record.getR22_amt() != null) {
									    cellE.setCellValue(record.getR22_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR22_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR22_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR22_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR22_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR22_amt_total() != null) {
									    cellH.setCellValue(record.getR22_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 23 ===================== */
									row = sheet.getRow(22);
									cellE = row.createCell(4);
									if (record.getR23_amt() != null) {
									    cellE.setCellValue(record.getR23_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR23_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR23_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR23_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR23_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR23_amt_total() != null) {
									    cellH.setCellValue(record.getR23_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 24 ===================== */
									row = sheet.getRow(23);
									cellE = row.createCell(4);
									if (record.getR24_amt() != null) {
									    cellE.setCellValue(record.getR24_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR24_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR24_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR24_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR24_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR24_amt_total() != null) {
									    cellH.setCellValue(record.getR24_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 25 ===================== */
									row = sheet.getRow(24);
									cellE = row.createCell(4);
									if (record.getR25_amt() != null) {
									    cellE.setCellValue(record.getR25_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR25_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR25_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR25_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR25_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR25_amt_total() != null) {
									    cellH.setCellValue(record.getR25_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 26 ===================== */
									row = sheet.getRow(25);
									cellE = row.createCell(4);
									if (record.getR26_amt() != null) {
									    cellE.setCellValue(record.getR26_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR26_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR26_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR26_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR26_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR26_amt_total() != null) {
									    cellH.setCellValue(record.getR26_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 27 ===================== */
									row = sheet.getRow(26);
									cellE = row.createCell(4);
									if (record.getR27_amt() != null) {
									    cellE.setCellValue(record.getR27_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR27_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR27_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR27_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR27_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR27_amt_total() != null) {
									    cellH.setCellValue(record.getR27_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 28 ===================== */
									row = sheet.getRow(27);
									cellE = row.createCell(4);
									if (record.getR28_amt() != null) {
									    cellE.setCellValue(record.getR28_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR28_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR28_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR28_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR28_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR28_amt_total() != null) {
									    cellH.setCellValue(record.getR28_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
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