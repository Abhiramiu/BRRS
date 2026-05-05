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

@Transactional
public class BRRS_BASEL_III_COM_EQUITY_DISC_ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(BRRS_BASEL_III_COM_EQUITY_DISC_ReportService.class);
	
	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

    // =====================================================
    // ENTITY MANAGER (Acts like Repository)
    // =====================================================

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;




	// Fetch data by report date Summary
public List<BASEL_III_COM_EQUITY_DISC_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_BASEL_III_COM_EQUITY_DISC_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new B_III_CETD_RowMapper()
    );
}

// Fetch data by report date ARCHIVAL
public List<Object[]> getB_III_CETDarchival() {

    String sql =
        "SELECT REPORT_DATE, REPORT_VERSION " +
        "FROM BRRS_BASEL_III_COM_EQUITY_DISC_ARCHIVALTABLE_SUMMARY " +
        "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
        sql,
        (rs, rowNum) -> new Object[] {
            rs.getDate("REPORT_DATE"),
            rs.getBigDecimal("REPORT_VERSION")
        }
    );
}

public List<BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity> getdatabydateListarchival(Date reportDate, BigDecimal reportVersion) {

    String sql =
        "SELECT * FROM BRRS_BASEL_III_COM_EQUITY_DISC_ARCHIVALTABLE_SUMMARY " +
        "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(	
        sql,
        new Object[]{reportDate, reportVersion},
        new B_III_CETD_Archival_RowMapper()
    );
}

public List<BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql =
        "SELECT * FROM BRRS_BASEL_III_COM_EQUITY_DISC_ARCHIVALTABLE_SUMMARY " +
        "WHERE REPORT_VERSION IS NOT NULL " +
        "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
        sql,
        new B_III_CETD_Archival_RowMapper()
    );
}


//----------detail 

public List<BASEL_III_COM_EQUITY_DISC_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql =
        "SELECT * FROM BRRS_BASEL_III_COM_EQUITY_DISC_DETAILTABLE " +
        "WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
        sql,
        new Object[]{reportDate},
        new B_III_CETD_Detail_RowMapper()
    );
}

public List<BASEL_III_COM_EQUITY_DISC_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql =
        "SELECT * FROM BRRS_BASEL_III_COM_EQUITY_DISC_DETAILTABLE " +
        "WHERE REPORT_DATE = ? " +
        "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
        sql,
        new Object[]{reportDate, offset, limit},
        new B_III_CETD_Detail_RowMapper()
    );
}

public int getdetaildatacount(Date reportDate) {

    String sql =
        "SELECT COUNT(*) FROM BRRS_BASEL_III_COM_EQUITY_DISC_DETAILTABLE " +
        "WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
        sql,
        new Object[]{reportDate},
        Integer.class
    );
}

public List<BASEL_III_COM_EQUITY_DISC_Detail_Entity> getdetailDataByRowIdAndColumnId(
        String reportLabel, String reportAddlCriteria1, Date reportDate) {

    String sql =
        "SELECT * FROM BRRS_BASEL_III_COM_EQUITY_DISC_DETAILTABLE " +
        "WHERE REPORT_LABEL = ? " +
        "AND REPORT_ADDL_CRITERIA_1 = ? " +
        "AND REPORT_DATE = ?";

    return jdbcTemplate.query(
        sql,
        new Object[]{reportLabel, reportAddlCriteria1, reportDate},
        new B_III_CETD_Detail_RowMapper()
    );
}

public BASEL_III_COM_EQUITY_DISC_Detail_Entity findByDetailAcctnumber(String acctNumber) {

    String sql =
        "SELECT * FROM BRRS_BASEL_III_COM_EQUITY_DISC_DETAILTABLE " +
        "WHERE ACCT_NUMBER = ?";

    List<BASEL_III_COM_EQUITY_DISC_Detail_Entity> list = jdbcTemplate.query(
        sql,
        new Object[]{acctNumber},
        new B_III_CETD_Detail_RowMapper()
    );

    return list.isEmpty() ? null : list.get(0);
}

//-----archival detail

public List<BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity> getarchivaldetaildatabydateList(
        Date reportDate, String dataEntryVersion) {

    String sql =
        "SELECT * FROM BRRS_BASEL_III_COM_EQUITY_DISC_ARCHIVALTABLE_DETAIL " +
        "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
        sql,
        new Object[]{reportDate, dataEntryVersion},
        new B_III_CETD_Archival_Detail_RowMapper()
    );
}

public List<BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria1,
        Date reportDate,
        String dataEntryVersion) {

    String sql =
        "SELECT * FROM BRRS_BASEL_III_COM_EQUITY_DISC_ARCHIVALTABLE_DETAIL " +
        "WHERE REPORT_LABEL = ? " +
        "AND REPORT_ADDL_CRITERIA_1 = ? " +
        "AND REPORT_DATE = ? " +
        "AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
        sql,
        new Object[]{reportLabel, reportAddlCriteria1, reportDate, dataEntryVersion},
        new B_III_CETD_Archival_Detail_RowMapper()
    );
}

//----------class and entities for summary
public class B_III_CETD_RowMapper implements RowMapper<BASEL_III_COM_EQUITY_DISC_Summary_Entity> {

    @Override
    public BASEL_III_COM_EQUITY_DISC_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        BASEL_III_COM_EQUITY_DISC_Summary_Entity obj = new BASEL_III_COM_EQUITY_DISC_Summary_Entity();

        // =========================
        // R7 - R12
        // =========================
        obj.setR7_product(rs.getString("r7_product"));
        obj.setR7_amount(rs.getBigDecimal("r7_amount"));

        obj.setR8_product(rs.getString("r8_product"));
        obj.setR8_amount(rs.getBigDecimal("r8_amount"));

        obj.setR9_product(rs.getString("r9_product"));
        obj.setR9_amount(rs.getBigDecimal("r9_amount"));

        obj.setR10_product(rs.getString("r10_product"));
        obj.setR10_amount(rs.getBigDecimal("r10_amount"));

        obj.setR11_product(rs.getString("r11_product"));
        obj.setR11_amount(rs.getBigDecimal("r11_amount"));

        obj.setR12_product(rs.getString("r12_product"));
        obj.setR12_amount(rs.getBigDecimal("r12_amount"));

        // =========================
        // R14 - R20
        // =========================
        obj.setR14_product(rs.getString("r14_product"));
        obj.setR14_amount(rs.getBigDecimal("r14_amount"));

        obj.setR15_product(rs.getString("r15_product"));
        obj.setR15_amount(rs.getBigDecimal("r15_amount"));

        obj.setR16_product(rs.getString("r16_product"));
        obj.setR16_amount(rs.getBigDecimal("r16_amount"));

        obj.setR17_product(rs.getString("r17_product"));
        obj.setR17_amount(rs.getBigDecimal("r17_amount"));

        obj.setR18_product(rs.getString("r18_product"));
        obj.setR18_amount(rs.getBigDecimal("r18_amount"));

        obj.setR19_product(rs.getString("r19_product"));
        obj.setR19_amount(rs.getBigDecimal("r19_amount"));

        obj.setR20_product(rs.getString("r20_product"));
        obj.setR20_amount(rs.getBigDecimal("r20_amount"));

        // =========================
        // R21 - R30
        // =========================
        obj.setR21_product(rs.getString("r21_product"));
        obj.setR21_amount(rs.getBigDecimal("r21_amount"));

        obj.setR22_product(rs.getString("r22_product"));
        obj.setR22_amount(rs.getBigDecimal("r22_amount"));

        obj.setR23_product(rs.getString("r23_product"));
        obj.setR23_amount(rs.getBigDecimal("r23_amount"));

        obj.setR24_product(rs.getString("r24_product"));
        obj.setR24_amount(rs.getBigDecimal("r24_amount"));

        obj.setR25_product(rs.getString("r25_product"));
        obj.setR25_amount(rs.getBigDecimal("r25_amount"));

        obj.setR26_product(rs.getString("r26_product"));
        obj.setR26_amount(rs.getBigDecimal("r26_amount"));

        obj.setR27_product(rs.getString("r27_product"));
        obj.setR27_amount(rs.getBigDecimal("r27_amount"));

        obj.setR28_product(rs.getString("r28_product"));
        obj.setR28_amount(rs.getBigDecimal("r28_amount"));

        obj.setR29_product(rs.getString("r29_product"));
        obj.setR29_amount(rs.getBigDecimal("r29_amount"));

        obj.setR30_product(rs.getString("r30_product"));
        obj.setR30_amount(rs.getBigDecimal("r30_amount"));

// =========================
// R31 - R40
// =========================
obj.setR31_product(rs.getString("r31_product"));
obj.setR31_amount(rs.getBigDecimal("r31_amount"));

obj.setR32_product(rs.getString("r32_product"));
obj.setR32_amount(rs.getBigDecimal("r32_amount"));

obj.setR33_product(rs.getString("r33_product"));
obj.setR33_amount(rs.getBigDecimal("r33_amount"));

obj.setR34_product(rs.getString("r34_product"));
obj.setR34_amount(rs.getBigDecimal("r34_amount"));

obj.setR35_product(rs.getString("r35_product"));
obj.setR35_amount(rs.getBigDecimal("r35_amount"));

obj.setR36_product(rs.getString("r36_product"));
obj.setR36_amount(rs.getBigDecimal("r36_amount"));

obj.setR38_product(rs.getString("r38_product"));
obj.setR38_amount(rs.getBigDecimal("r38_amount"));

obj.setR39_product(rs.getString("r39_product"));
obj.setR39_amount(rs.getBigDecimal("r39_amount"));

obj.setR40_product(rs.getString("r40_product"));
obj.setR40_amount(rs.getBigDecimal("r40_amount"));

// =========================
// R41 - R50
// =========================
obj.setR41_product(rs.getString("r41_product"));
obj.setR41_amount(rs.getBigDecimal("r41_amount"));

obj.setR42_product(rs.getString("r42_product"));
obj.setR42_amount(rs.getBigDecimal("r42_amount"));

obj.setR43_product(rs.getString("r43_product"));
obj.setR43_amount(rs.getBigDecimal("r43_amount"));

obj.setR44_product(rs.getString("r44_product"));
obj.setR44_amount(rs.getBigDecimal("r44_amount"));

obj.setR46_product(rs.getString("r46_product"));
obj.setR46_amount(rs.getBigDecimal("r46_amount"));

obj.setR47_product(rs.getString("r47_product"));
obj.setR47_amount(rs.getBigDecimal("r47_amount"));

obj.setR48_product(rs.getString("r48_product"));
obj.setR48_amount(rs.getBigDecimal("r48_amount"));

obj.setR49_product(rs.getString("r49_product"));
obj.setR49_amount(rs.getBigDecimal("r49_amount"));

obj.setR50_product(rs.getString("r50_product"));
obj.setR50_amount(rs.getBigDecimal("r50_amount"));

// =========================
// R51 - R60
// =========================
obj.setR51_product(rs.getString("r51_product"));
obj.setR51_amount(rs.getBigDecimal("r51_amount"));

obj.setR52_product(rs.getString("r52_product"));
obj.setR52_amount(rs.getBigDecimal("r52_amount"));

obj.setR53_product(rs.getString("r53_product"));
obj.setR53_amount(rs.getBigDecimal("r53_amount"));

obj.setR54_product(rs.getString("r54_product"));
obj.setR54_amount(rs.getBigDecimal("r54_amount"));

obj.setR55_product(rs.getString("r55_product"));
obj.setR55_amount(rs.getBigDecimal("r55_amount"));

obj.setR57_product(rs.getString("r57_product"));
obj.setR57_amount(rs.getBigDecimal("r57_amount"));

obj.setR58_product(rs.getString("r58_product"));
obj.setR58_amount(rs.getBigDecimal("r58_amount"));

obj.setR59_product(rs.getString("r59_product"));
obj.setR59_amount(rs.getBigDecimal("r59_amount"));

obj.setR60_product(rs.getString("r60_product"));
obj.setR60_amount(rs.getBigDecimal("r60_amount"));

// =========================
// R61 - R70
// =========================
obj.setR61_product(rs.getString("r61_product"));
obj.setR61_amount(rs.getBigDecimal("r61_amount"));

obj.setR62_product(rs.getString("r62_product"));
obj.setR62_amount(rs.getBigDecimal("r62_amount"));

obj.setR64_product(rs.getString("r64_product"));
obj.setR64_amount(rs.getBigDecimal("r64_amount"));

obj.setR65_product(rs.getString("r65_product"));
obj.setR65_amount(rs.getBigDecimal("r65_amount"));

obj.setR66_product(rs.getString("r66_product"));
obj.setR66_amount(rs.getBigDecimal("r66_amount"));

obj.setR67_product(rs.getString("r67_product"));
obj.setR67_amount(rs.getBigDecimal("r67_amount"));

obj.setR68_product(rs.getString("r68_product"));
obj.setR68_amount(rs.getBigDecimal("r68_amount"));

obj.setR69_product(rs.getString("r69_product"));
obj.setR69_amount(rs.getBigDecimal("r69_amount"));

obj.setR70_product(rs.getString("r70_product"));
obj.setR70_amount(rs.getBigDecimal("r70_amount"));

// =========================
// R71 - R73
// =========================
obj.setR71_product(rs.getString("r71_product"));
obj.setR71_amount(rs.getBigDecimal("r71_amount"));

obj.setR72_product(rs.getString("r72_product"));
obj.setR72_amount(rs.getBigDecimal("r72_amount"));

obj.setR73_product(rs.getString("r73_product"));
obj.setR73_amount(rs.getBigDecimal("r73_amount"));

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

public class BASEL_III_COM_EQUITY_DISC_Summary_Entity {
	
	
	private String	r7_product;
	private BigDecimal	r7_amount;
	private String	r8_product;
	private BigDecimal	r8_amount;
	private String	r9_product;
	private BigDecimal	r9_amount;
	private String	r10_product;
	private BigDecimal	r10_amount;
	private String	r11_product;
	private BigDecimal	r11_amount;
	private String	r12_product;
	private BigDecimal	r12_amount;

	private String	r14_product;
	private BigDecimal	r14_amount;
	private String	r15_product;
	private BigDecimal	r15_amount;
	private String	r16_product;
	private BigDecimal	r16_amount;
	private String	r17_product;
	private BigDecimal	r17_amount;
	private String	r18_product;
	private BigDecimal	r18_amount;
	private String	r19_product;
	private BigDecimal	r19_amount;
	private String	r20_product;
	private BigDecimal	r20_amount;
	private String	r21_product;
	private BigDecimal	r21_amount;
	private String	r22_product;
	private BigDecimal	r22_amount;
	private String	r23_product;
	private BigDecimal	r23_amount;
	private String	r24_product;
	private BigDecimal	r24_amount;
	private String	r25_product;
	private BigDecimal	r25_amount;
	private String	r26_product;
	private BigDecimal	r26_amount;
	private String	r27_product;
	private BigDecimal	r27_amount;
	private String	r28_product;
	private BigDecimal	r28_amount;
	private String	r29_product;
	private BigDecimal	r29_amount;
	private String	r30_product;
	private BigDecimal	r30_amount;
	private String	r31_product;
	private BigDecimal	r31_amount;
	private String	r32_product;
	private BigDecimal	r32_amount;
	private String	r33_product;
	private BigDecimal	r33_amount;
	private String	r34_product;
	private BigDecimal	r34_amount;
	private String	r35_product;
	private BigDecimal	r35_amount;
	private String	r36_product;
	private BigDecimal	r36_amount;

	private String	r38_product;
	private BigDecimal	r38_amount;
	private String	r39_product;
	private BigDecimal	r39_amount;
	private String	r40_product;
	private BigDecimal	r40_amount;
	private String	r41_product;
	private BigDecimal	r41_amount;
	private String	r42_product;
	private BigDecimal	r42_amount;
	private String	r43_product;
	private BigDecimal	r43_amount;
	private String	r44_product;
	private BigDecimal	r44_amount;

	private String	r46_product;
	private BigDecimal	r46_amount;
	private String	r47_product;
	private BigDecimal	r47_amount;
	private String	r48_product;
	private BigDecimal	r48_amount;
	private String	r49_product;
	private BigDecimal	r49_amount;
	private String	r50_product;
	private BigDecimal	r50_amount;
	private String	r51_product;
	private BigDecimal	r51_amount;
	private String	r52_product;
	private BigDecimal	r52_amount;
	private String	r53_product;
	private BigDecimal	r53_amount;
	private String	r54_product;
	private BigDecimal	r54_amount;
	private String	r55_product;
	private BigDecimal	r55_amount;

	private String	r57_product;
	private BigDecimal	r57_amount;
	private String	r58_product;
	private BigDecimal	r58_amount;
	private String	r59_product;
	private BigDecimal	r59_amount;
	private String	r60_product;
	private BigDecimal	r60_amount;
	private String	r61_product;
	private BigDecimal	r61_amount;
	private String	r62_product;
	private BigDecimal	r62_amount;

	private String	r64_product;
	private BigDecimal	r64_amount;
	private String	r65_product;
	private BigDecimal	r65_amount;
	private String	r66_product;
	private BigDecimal	r66_amount;
	private String	r67_product;
	private BigDecimal	r67_amount;
	private String	r68_product;
	private BigDecimal	r68_amount;
	private String	r69_product;
	private BigDecimal	r69_amount;
	private String	r70_product;
	private BigDecimal	r70_amount;
	private String	r71_product;
	private BigDecimal	r71_amount;
	private String	r72_product;
	private BigDecimal	r72_amount;
	private String	r73_product;
	private BigDecimal	r73_amount;

	
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
	public String getR7_product() {
		return r7_product;
	}
	public void setR7_product(String r7_product) {
		this.r7_product = r7_product;
	}
	public BigDecimal getR7_amount() {
		return r7_amount;
	}
	public void setR7_amount(BigDecimal r7_amount) {
		this.r7_amount = r7_amount;
	}
	public String getR8_product() {
		return r8_product;
	}
	public void setR8_product(String r8_product) {
		this.r8_product = r8_product;
	}
	public BigDecimal getR8_amount() {
		return r8_amount;
	}
	public void setR8_amount(BigDecimal r8_amount) {
		this.r8_amount = r8_amount;
	}
	public String getR9_product() {
		return r9_product;
	}
	public void setR9_product(String r9_product) {
		this.r9_product = r9_product;
	}
	public BigDecimal getR9_amount() {
		return r9_amount;
	}
	public void setR9_amount(BigDecimal r9_amount) {
		this.r9_amount = r9_amount;
	}
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_amount() {
		return r10_amount;
	}
	public void setR10_amount(BigDecimal r10_amount) {
		this.r10_amount = r10_amount;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_amount() {
		return r11_amount;
	}
	public void setR11_amount(BigDecimal r11_amount) {
		this.r11_amount = r11_amount;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_amount() {
		return r12_amount;
	}
	public void setR12_amount(BigDecimal r12_amount) {
		this.r12_amount = r12_amount;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_amount() {
		return r14_amount;
	}
	public void setR14_amount(BigDecimal r14_amount) {
		this.r14_amount = r14_amount;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_amount() {
		return r15_amount;
	}
	public void setR15_amount(BigDecimal r15_amount) {
		this.r15_amount = r15_amount;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_amount() {
		return r16_amount;
	}
	public void setR16_amount(BigDecimal r16_amount) {
		this.r16_amount = r16_amount;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_amount() {
		return r17_amount;
	}
	public void setR17_amount(BigDecimal r17_amount) {
		this.r17_amount = r17_amount;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_amount() {
		return r18_amount;
	}
	public void setR18_amount(BigDecimal r18_amount) {
		this.r18_amount = r18_amount;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_amount() {
		return r19_amount;
	}
	public void setR19_amount(BigDecimal r19_amount) {
		this.r19_amount = r19_amount;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_amount() {
		return r20_amount;
	}
	public void setR20_amount(BigDecimal r20_amount) {
		this.r20_amount = r20_amount;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_amount() {
		return r21_amount;
	}
	public void setR21_amount(BigDecimal r21_amount) {
		this.r21_amount = r21_amount;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_amount() {
		return r22_amount;
	}
	public void setR22_amount(BigDecimal r22_amount) {
		this.r22_amount = r22_amount;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_amount() {
		return r23_amount;
	}
	public void setR23_amount(BigDecimal r23_amount) {
		this.r23_amount = r23_amount;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_amount() {
		return r24_amount;
	}
	public void setR24_amount(BigDecimal r24_amount) {
		this.r24_amount = r24_amount;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_amount() {
		return r25_amount;
	}
	public void setR25_amount(BigDecimal r25_amount) {
		this.r25_amount = r25_amount;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_amount() {
		return r26_amount;
	}
	public void setR26_amount(BigDecimal r26_amount) {
		this.r26_amount = r26_amount;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_amount() {
		return r27_amount;
	}
	public void setR27_amount(BigDecimal r27_amount) {
		this.r27_amount = r27_amount;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_amount() {
		return r28_amount;
	}
	public void setR28_amount(BigDecimal r28_amount) {
		this.r28_amount = r28_amount;
	}
	public String getR29_product() {
		return r29_product;
	}
	public void setR29_product(String r29_product) {
		this.r29_product = r29_product;
	}
	public BigDecimal getR29_amount() {
		return r29_amount;
	}
	public void setR29_amount(BigDecimal r29_amount) {
		this.r29_amount = r29_amount;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_amount() {
		return r30_amount;
	}
	public void setR30_amount(BigDecimal r30_amount) {
		this.r30_amount = r30_amount;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_amount() {
		return r31_amount;
	}
	public void setR31_amount(BigDecimal r31_amount) {
		this.r31_amount = r31_amount;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_amount() {
		return r32_amount;
	}
	public void setR32_amount(BigDecimal r32_amount) {
		this.r32_amount = r32_amount;
	}
	public String getR33_product() {
		return r33_product;
	}
	public void setR33_product(String r33_product) {
		this.r33_product = r33_product;
	}
	public BigDecimal getR33_amount() {
		return r33_amount;
	}
	public void setR33_amount(BigDecimal r33_amount) {
		this.r33_amount = r33_amount;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_amount() {
		return r34_amount;
	}
	public void setR34_amount(BigDecimal r34_amount) {
		this.r34_amount = r34_amount;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_amount() {
		return r35_amount;
	}
	public void setR35_amount(BigDecimal r35_amount) {
		this.r35_amount = r35_amount;
	}
	public String getR36_product() {
		return r36_product;
	}
	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}
	public BigDecimal getR36_amount() {
		return r36_amount;
	}
	public void setR36_amount(BigDecimal r36_amount) {
		this.r36_amount = r36_amount;
	}
	public String getR38_product() {
		return r38_product;
	}
	public void setR38_product(String r38_product) {
		this.r38_product = r38_product;
	}
	public BigDecimal getR38_amount() {
		return r38_amount;
	}
	public void setR38_amount(BigDecimal r38_amount) {
		this.r38_amount = r38_amount;
	}
	public String getR39_product() {
		return r39_product;
	}
	public void setR39_product(String r39_product) {
		this.r39_product = r39_product;
	}
	public BigDecimal getR39_amount() {
		return r39_amount;
	}
	public void setR39_amount(BigDecimal r39_amount) {
		this.r39_amount = r39_amount;
	}
	public String getR40_product() {
		return r40_product;
	}
	public void setR40_product(String r40_product) {
		this.r40_product = r40_product;
	}
	public BigDecimal getR40_amount() {
		return r40_amount;
	}
	public void setR40_amount(BigDecimal r40_amount) {
		this.r40_amount = r40_amount;
	}
	public String getR41_product() {
		return r41_product;
	}
	public void setR41_product(String r41_product) {
		this.r41_product = r41_product;
	}
	public BigDecimal getR41_amount() {
		return r41_amount;
	}
	public void setR41_amount(BigDecimal r41_amount) {
		this.r41_amount = r41_amount;
	}
	public String getR42_product() {
		return r42_product;
	}
	public void setR42_product(String r42_product) {
		this.r42_product = r42_product;
	}
	public BigDecimal getR42_amount() {
		return r42_amount;
	}
	public void setR42_amount(BigDecimal r42_amount) {
		this.r42_amount = r42_amount;
	}
	public String getR43_product() {
		return r43_product;
	}
	public void setR43_product(String r43_product) {
		this.r43_product = r43_product;
	}
	public BigDecimal getR43_amount() {
		return r43_amount;
	}
	public void setR43_amount(BigDecimal r43_amount) {
		this.r43_amount = r43_amount;
	}
	public String getR44_product() {
		return r44_product;
	}
	public void setR44_product(String r44_product) {
		this.r44_product = r44_product;
	}
	public BigDecimal getR44_amount() {
		return r44_amount;
	}
	public void setR44_amount(BigDecimal r44_amount) {
		this.r44_amount = r44_amount;
	}
	public String getR46_product() {
		return r46_product;
	}
	public void setR46_product(String r46_product) {
		this.r46_product = r46_product;
	}
	public BigDecimal getR46_amount() {
		return r46_amount;
	}
	public void setR46_amount(BigDecimal r46_amount) {
		this.r46_amount = r46_amount;
	}
	public String getR47_product() {
		return r47_product;
	}
	public void setR47_product(String r47_product) {
		this.r47_product = r47_product;
	}
	public BigDecimal getR47_amount() {
		return r47_amount;
	}
	public void setR47_amount(BigDecimal r47_amount) {
		this.r47_amount = r47_amount;
	}
	public String getR48_product() {
		return r48_product;
	}
	public void setR48_product(String r48_product) {
		this.r48_product = r48_product;
	}
	public BigDecimal getR48_amount() {
		return r48_amount;
	}
	public void setR48_amount(BigDecimal r48_amount) {
		this.r48_amount = r48_amount;
	}
	public String getR49_product() {
		return r49_product;
	}
	public void setR49_product(String r49_product) {
		this.r49_product = r49_product;
	}
	public BigDecimal getR49_amount() {
		return r49_amount;
	}
	public void setR49_amount(BigDecimal r49_amount) {
		this.r49_amount = r49_amount;
	}
	public String getR50_product() {
		return r50_product;
	}
	public void setR50_product(String r50_product) {
		this.r50_product = r50_product;
	}
	public BigDecimal getR50_amount() {
		return r50_amount;
	}
	public void setR50_amount(BigDecimal r50_amount) {
		this.r50_amount = r50_amount;
	}
	public String getR51_product() {
		return r51_product;
	}
	public void setR51_product(String r51_product) {
		this.r51_product = r51_product;
	}
	public BigDecimal getR51_amount() {
		return r51_amount;
	}
	public void setR51_amount(BigDecimal r51_amount) {
		this.r51_amount = r51_amount;
	}
	public String getR52_product() {
		return r52_product;
	}
	public void setR52_product(String r52_product) {
		this.r52_product = r52_product;
	}
	public BigDecimal getR52_amount() {
		return r52_amount;
	}
	public void setR52_amount(BigDecimal r52_amount) {
		this.r52_amount = r52_amount;
	}
	public String getR53_product() {
		return r53_product;
	}
	public void setR53_product(String r53_product) {
		this.r53_product = r53_product;
	}
	public BigDecimal getR53_amount() {
		return r53_amount;
	}
	public void setR53_amount(BigDecimal r53_amount) {
		this.r53_amount = r53_amount;
	}
	public String getR54_product() {
		return r54_product;
	}
	public void setR54_product(String r54_product) {
		this.r54_product = r54_product;
	}
	public BigDecimal getR54_amount() {
		return r54_amount;
	}
	public void setR54_amount(BigDecimal r54_amount) {
		this.r54_amount = r54_amount;
	}
	public String getR55_product() {
		return r55_product;
	}
	public void setR55_product(String r55_product) {
		this.r55_product = r55_product;
	}
	public BigDecimal getR55_amount() {
		return r55_amount;
	}
	public void setR55_amount(BigDecimal r55_amount) {
		this.r55_amount = r55_amount;
	}
	public String getR57_product() {
		return r57_product;
	}
	public void setR57_product(String r57_product) {
		this.r57_product = r57_product;
	}
	public BigDecimal getR57_amount() {
		return r57_amount;
	}
	public void setR57_amount(BigDecimal r57_amount) {
		this.r57_amount = r57_amount;
	}
	public String getR58_product() {
		return r58_product;
	}
	public void setR58_product(String r58_product) {
		this.r58_product = r58_product;
	}
	public BigDecimal getR58_amount() {
		return r58_amount;
	}
	public void setR58_amount(BigDecimal r58_amount) {
		this.r58_amount = r58_amount;
	}
	public String getR59_product() {
		return r59_product;
	}
	public void setR59_product(String r59_product) {
		this.r59_product = r59_product;
	}
	public BigDecimal getR59_amount() {
		return r59_amount;
	}
	public void setR59_amount(BigDecimal r59_amount) {
		this.r59_amount = r59_amount;
	}
	public String getR60_product() {
		return r60_product;
	}
	public void setR60_product(String r60_product) {
		this.r60_product = r60_product;
	}
	public BigDecimal getR60_amount() {
		return r60_amount;
	}
	public void setR60_amount(BigDecimal r60_amount) {
		this.r60_amount = r60_amount;
	}
	public String getR61_product() {
		return r61_product;
	}
	public void setR61_product(String r61_product) {
		this.r61_product = r61_product;
	}
	public BigDecimal getR61_amount() {
		return r61_amount;
	}
	public void setR61_amount(BigDecimal r61_amount) {
		this.r61_amount = r61_amount;
	}
	public String getR62_product() {
		return r62_product;
	}
	public void setR62_product(String r62_product) {
		this.r62_product = r62_product;
	}
	public BigDecimal getR62_amount() {
		return r62_amount;
	}
	public void setR62_amount(BigDecimal r62_amount) {
		this.r62_amount = r62_amount;
	}
	public String getR64_product() {
		return r64_product;
	}
	public void setR64_product(String r64_product) {
		this.r64_product = r64_product;
	}
	public BigDecimal getR64_amount() {
		return r64_amount;
	}
	public void setR64_amount(BigDecimal r64_amount) {
		this.r64_amount = r64_amount;
	}
	public String getR65_product() {
		return r65_product;
	}
	public void setR65_product(String r65_product) {
		this.r65_product = r65_product;
	}
	public BigDecimal getR65_amount() {
		return r65_amount;
	}
	public void setR65_amount(BigDecimal r65_amount) {
		this.r65_amount = r65_amount;
	}
	public String getR66_product() {
		return r66_product;
	}
	public void setR66_product(String r66_product) {
		this.r66_product = r66_product;
	}
	public BigDecimal getR66_amount() {
		return r66_amount;
	}
	public void setR66_amount(BigDecimal r66_amount) {
		this.r66_amount = r66_amount;
	}
	public String getR67_product() {
		return r67_product;
	}
	public void setR67_product(String r67_product) {
		this.r67_product = r67_product;
	}
	public BigDecimal getR67_amount() {
		return r67_amount;
	}
	public void setR67_amount(BigDecimal r67_amount) {
		this.r67_amount = r67_amount;
	}
	public String getR68_product() {
		return r68_product;
	}
	public void setR68_product(String r68_product) {
		this.r68_product = r68_product;
	}
	public BigDecimal getR68_amount() {
		return r68_amount;
	}
	public void setR68_amount(BigDecimal r68_amount) {
		this.r68_amount = r68_amount;
	}
	public String getR69_product() {
		return r69_product;
	}
	public void setR69_product(String r69_product) {
		this.r69_product = r69_product;
	}
	public BigDecimal getR69_amount() {
		return r69_amount;
	}
	public void setR69_amount(BigDecimal r69_amount) {
		this.r69_amount = r69_amount;
	}
	public String getR70_product() {
		return r70_product;
	}
	public void setR70_product(String r70_product) {
		this.r70_product = r70_product;
	}
	public BigDecimal getR70_amount() {
		return r70_amount;
	}
	public void setR70_amount(BigDecimal r70_amount) {
		this.r70_amount = r70_amount;
	}
	public String getR71_product() {
		return r71_product;
	}
	public void setR71_product(String r71_product) {
		this.r71_product = r71_product;
	}
	public BigDecimal getR71_amount() {
		return r71_amount;
	}
	public void setR71_amount(BigDecimal r71_amount) {
		this.r71_amount = r71_amount;
	}
	public String getR72_product() {
		return r72_product;
	}
	public void setR72_product(String r72_product) {
		this.r72_product = r72_product;
	}
	public BigDecimal getR72_amount() {
		return r72_amount;
	}
	public void setR72_amount(BigDecimal r72_amount) {
		this.r72_amount = r72_amount;
	}
	public String getR73_product() {
		return r73_product;
	}
	public void setR73_product(String r73_product) {
		this.r73_product = r73_product;
	}
	public BigDecimal getR73_amount() {
		return r73_amount;
	}
	public void setR73_amount(BigDecimal r73_amount) {
		this.r73_amount = r73_amount;
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


    // =====================================================
    // GETTERS & SETTERS (FULL)
    // =====================================================
	
	
	
}


//=========================================================
//ARCHIVAL ROW MAPPER cls and entity
//=========================================================


public class B_III_CETD_Archival_RowMapper implements RowMapper<BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity> {

    @Override
    public BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity obj =
                new BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity();

        // =========================
        // R7 - R12
        // =========================
        obj.setR7_product(rs.getString("r7_product"));
        obj.setR7_amount(rs.getBigDecimal("r7_amount"));
        obj.setR8_product(rs.getString("r8_product"));
        obj.setR8_amount(rs.getBigDecimal("r8_amount"));
        obj.setR9_product(rs.getString("r9_product"));
        obj.setR9_amount(rs.getBigDecimal("r9_amount"));
        obj.setR10_product(rs.getString("r10_product"));
        obj.setR10_amount(rs.getBigDecimal("r10_amount"));
        obj.setR11_product(rs.getString("r11_product"));
        obj.setR11_amount(rs.getBigDecimal("r11_amount"));
        obj.setR12_product(rs.getString("r12_product"));
        obj.setR12_amount(rs.getBigDecimal("r12_amount"));
		
		        // =========================
        // R14 - R20
        // =========================
        obj.setR14_product(rs.getString("r14_product"));
        obj.setR14_amount(rs.getBigDecimal("r14_amount"));

        obj.setR15_product(rs.getString("r15_product"));
        obj.setR15_amount(rs.getBigDecimal("r15_amount"));

        obj.setR16_product(rs.getString("r16_product"));
        obj.setR16_amount(rs.getBigDecimal("r16_amount"));

        obj.setR17_product(rs.getString("r17_product"));
        obj.setR17_amount(rs.getBigDecimal("r17_amount"));

        obj.setR18_product(rs.getString("r18_product"));
        obj.setR18_amount(rs.getBigDecimal("r18_amount"));

        obj.setR19_product(rs.getString("r19_product"));
        obj.setR19_amount(rs.getBigDecimal("r19_amount"));

        obj.setR20_product(rs.getString("r20_product"));
        obj.setR20_amount(rs.getBigDecimal("r20_amount"));

        // =========================
        // R21 - R30
        // =========================
        obj.setR21_product(rs.getString("r21_product"));
        obj.setR21_amount(rs.getBigDecimal("r21_amount"));

        obj.setR22_product(rs.getString("r22_product"));
        obj.setR22_amount(rs.getBigDecimal("r22_amount"));

        obj.setR23_product(rs.getString("r23_product"));
        obj.setR23_amount(rs.getBigDecimal("r23_amount"));

        obj.setR24_product(rs.getString("r24_product"));
        obj.setR24_amount(rs.getBigDecimal("r24_amount"));

        obj.setR25_product(rs.getString("r25_product"));
        obj.setR25_amount(rs.getBigDecimal("r25_amount"));

        obj.setR26_product(rs.getString("r26_product"));
        obj.setR26_amount(rs.getBigDecimal("r26_amount"));

        obj.setR27_product(rs.getString("r27_product"));
        obj.setR27_amount(rs.getBigDecimal("r27_amount"));

        obj.setR28_product(rs.getString("r28_product"));
        obj.setR28_amount(rs.getBigDecimal("r28_amount"));

        obj.setR29_product(rs.getString("r29_product"));
        obj.setR29_amount(rs.getBigDecimal("r29_amount"));

        obj.setR30_product(rs.getString("r30_product"));
        obj.setR30_amount(rs.getBigDecimal("r30_amount"));

// =========================
// R31 - R40
// =========================
obj.setR31_product(rs.getString("r31_product"));
obj.setR31_amount(rs.getBigDecimal("r31_amount"));

obj.setR32_product(rs.getString("r32_product"));
obj.setR32_amount(rs.getBigDecimal("r32_amount"));

obj.setR33_product(rs.getString("r33_product"));
obj.setR33_amount(rs.getBigDecimal("r33_amount"));

obj.setR34_product(rs.getString("r34_product"));
obj.setR34_amount(rs.getBigDecimal("r34_amount"));

obj.setR35_product(rs.getString("r35_product"));
obj.setR35_amount(rs.getBigDecimal("r35_amount"));

obj.setR36_product(rs.getString("r36_product"));
obj.setR36_amount(rs.getBigDecimal("r36_amount"));

obj.setR38_product(rs.getString("r38_product"));
obj.setR38_amount(rs.getBigDecimal("r38_amount"));

obj.setR39_product(rs.getString("r39_product"));
obj.setR39_amount(rs.getBigDecimal("r39_amount"));

obj.setR40_product(rs.getString("r40_product"));
obj.setR40_amount(rs.getBigDecimal("r40_amount"));

// =========================
// R41 - R50
// =========================
obj.setR41_product(rs.getString("r41_product"));
obj.setR41_amount(rs.getBigDecimal("r41_amount"));

obj.setR42_product(rs.getString("r42_product"));
obj.setR42_amount(rs.getBigDecimal("r42_amount"));

obj.setR43_product(rs.getString("r43_product"));
obj.setR43_amount(rs.getBigDecimal("r43_amount"));

obj.setR44_product(rs.getString("r44_product"));
obj.setR44_amount(rs.getBigDecimal("r44_amount"));

obj.setR46_product(rs.getString("r46_product"));
obj.setR46_amount(rs.getBigDecimal("r46_amount"));

obj.setR47_product(rs.getString("r47_product"));
obj.setR47_amount(rs.getBigDecimal("r47_amount"));

obj.setR48_product(rs.getString("r48_product"));
obj.setR48_amount(rs.getBigDecimal("r48_amount"));

obj.setR49_product(rs.getString("r49_product"));
obj.setR49_amount(rs.getBigDecimal("r49_amount"));

obj.setR50_product(rs.getString("r50_product"));
obj.setR50_amount(rs.getBigDecimal("r50_amount"));

// =========================
// R51 - R60
// =========================
obj.setR51_product(rs.getString("r51_product"));
obj.setR51_amount(rs.getBigDecimal("r51_amount"));

obj.setR52_product(rs.getString("r52_product"));
obj.setR52_amount(rs.getBigDecimal("r52_amount"));

obj.setR53_product(rs.getString("r53_product"));
obj.setR53_amount(rs.getBigDecimal("r53_amount"));

obj.setR54_product(rs.getString("r54_product"));
obj.setR54_amount(rs.getBigDecimal("r54_amount"));

obj.setR55_product(rs.getString("r55_product"));
obj.setR55_amount(rs.getBigDecimal("r55_amount"));

obj.setR57_product(rs.getString("r57_product"));
obj.setR57_amount(rs.getBigDecimal("r57_amount"));

obj.setR58_product(rs.getString("r58_product"));
obj.setR58_amount(rs.getBigDecimal("r58_amount"));

obj.setR59_product(rs.getString("r59_product"));
obj.setR59_amount(rs.getBigDecimal("r59_amount"));

obj.setR60_product(rs.getString("r60_product"));
obj.setR60_amount(rs.getBigDecimal("r60_amount"));

// =========================
// R61 - R70
// =========================
obj.setR61_product(rs.getString("r61_product"));
obj.setR61_amount(rs.getBigDecimal("r61_amount"));

obj.setR62_product(rs.getString("r62_product"));
obj.setR62_amount(rs.getBigDecimal("r62_amount"));

obj.setR64_product(rs.getString("r64_product"));
obj.setR64_amount(rs.getBigDecimal("r64_amount"));

obj.setR65_product(rs.getString("r65_product"));
obj.setR65_amount(rs.getBigDecimal("r65_amount"));

obj.setR66_product(rs.getString("r66_product"));
obj.setR66_amount(rs.getBigDecimal("r66_amount"));

obj.setR67_product(rs.getString("r67_product"));
obj.setR67_amount(rs.getBigDecimal("r67_amount"));

obj.setR68_product(rs.getString("r68_product"));
obj.setR68_amount(rs.getBigDecimal("r68_amount"));

obj.setR69_product(rs.getString("r69_product"));
obj.setR69_amount(rs.getBigDecimal("r69_amount"));

obj.setR70_product(rs.getString("r70_product"));
obj.setR70_amount(rs.getBigDecimal("r70_amount"));

// =========================
// R71 - R73
// =========================
obj.setR71_product(rs.getString("r71_product"));
obj.setR71_amount(rs.getBigDecimal("r71_amount"));

obj.setR72_product(rs.getString("r72_product"));
obj.setR72_amount(rs.getBigDecimal("r72_amount"));

obj.setR73_product(rs.getString("r73_product"));
obj.setR73_amount(rs.getBigDecimal("r73_amount"));


        // =========================
        // REPORT DETAILS
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

public class BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity {
	
	

		
	
	private String	r7_product;
	private BigDecimal	r7_amount;
	private String	r8_product;
	private BigDecimal	r8_amount;
	private String	r9_product;
	private BigDecimal	r9_amount;
	private String	r10_product;
	private BigDecimal	r10_amount;
	private String	r11_product;
	private BigDecimal	r11_amount;
	private String	r12_product;
	private BigDecimal	r12_amount;

	private String	r14_product;
	private BigDecimal	r14_amount;
	private String	r15_product;
	private BigDecimal	r15_amount;
	private String	r16_product;
	private BigDecimal	r16_amount;
	private String	r17_product;
	private BigDecimal	r17_amount;
	private String	r18_product;
	private BigDecimal	r18_amount;
	private String	r19_product;
	private BigDecimal	r19_amount;
	private String	r20_product;
	private BigDecimal	r20_amount;
	private String	r21_product;
	private BigDecimal	r21_amount;
	private String	r22_product;
	private BigDecimal	r22_amount;
	private String	r23_product;
	private BigDecimal	r23_amount;
	private String	r24_product;
	private BigDecimal	r24_amount;
	private String	r25_product;
	private BigDecimal	r25_amount;
	private String	r26_product;
	private BigDecimal	r26_amount;
	private String	r27_product;
	private BigDecimal	r27_amount;
	private String	r28_product;
	private BigDecimal	r28_amount;
	private String	r29_product;
	private BigDecimal	r29_amount;
	private String	r30_product;
	private BigDecimal	r30_amount;
	private String	r31_product;
	private BigDecimal	r31_amount;
	private String	r32_product;
	private BigDecimal	r32_amount;
	private String	r33_product;
	private BigDecimal	r33_amount;
	private String	r34_product;
	private BigDecimal	r34_amount;
	private String	r35_product;
	private BigDecimal	r35_amount;
	private String	r36_product;
	private BigDecimal	r36_amount;

	private String	r38_product;
	private BigDecimal	r38_amount;
	private String	r39_product;
	private BigDecimal	r39_amount;
	private String	r40_product;
	private BigDecimal	r40_amount;
	private String	r41_product;
	private BigDecimal	r41_amount;
	private String	r42_product;
	private BigDecimal	r42_amount;
	private String	r43_product;
	private BigDecimal	r43_amount;
	private String	r44_product;
	private BigDecimal	r44_amount;

	private String	r46_product;
	private BigDecimal	r46_amount;
	private String	r47_product;
	private BigDecimal	r47_amount;
	private String	r48_product;
	private BigDecimal	r48_amount;
	private String	r49_product;
	private BigDecimal	r49_amount;
	private String	r50_product;
	private BigDecimal	r50_amount;
	private String	r51_product;
	private BigDecimal	r51_amount;
	private String	r52_product;
	private BigDecimal	r52_amount;
	private String	r53_product;
	private BigDecimal	r53_amount;
	private String	r54_product;
	private BigDecimal	r54_amount;
	private String	r55_product;
	private BigDecimal	r55_amount;

	private String	r57_product;
	private BigDecimal	r57_amount;
	private String	r58_product;
	private BigDecimal	r58_amount;
	private String	r59_product;
	private BigDecimal	r59_amount;
	private String	r60_product;
	private BigDecimal	r60_amount;
	private String	r61_product;
	private BigDecimal	r61_amount;
	private String	r62_product;
	private BigDecimal	r62_amount;

	private String	r64_product;
	private BigDecimal	r64_amount;
	private String	r65_product;
	private BigDecimal	r65_amount;
	private String	r66_product;
	private BigDecimal	r66_amount;
	private String	r67_product;
	private BigDecimal	r67_amount;
	private String	r68_product;
	private BigDecimal	r68_amount;
	private String	r69_product;
	private BigDecimal	r69_amount;
	private String	r70_product;
	private BigDecimal	r70_amount;
	private String	r71_product;
	private BigDecimal	r71_amount;
	private String	r72_product;
	private BigDecimal	r72_amount;
	private String	r73_product;
	private BigDecimal	r73_amount;

	
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
	public String getR7_product() {
		return r7_product;
	}
	public void setR7_product(String r7_product) {
		this.r7_product = r7_product;
	}
	public BigDecimal getR7_amount() {
		return r7_amount;
	}
	public void setR7_amount(BigDecimal r7_amount) {
		this.r7_amount = r7_amount;
	}
	public String getR8_product() {
		return r8_product;
	}
	public void setR8_product(String r8_product) {
		this.r8_product = r8_product;
	}
	public BigDecimal getR8_amount() {
		return r8_amount;
	}
	public void setR8_amount(BigDecimal r8_amount) {
		this.r8_amount = r8_amount;
	}
	public String getR9_product() {
		return r9_product;
	}
	public void setR9_product(String r9_product) {
		this.r9_product = r9_product;
	}
	public BigDecimal getR9_amount() {
		return r9_amount;
	}
	public void setR9_amount(BigDecimal r9_amount) {
		this.r9_amount = r9_amount;
	}
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_amount() {
		return r10_amount;
	}
	public void setR10_amount(BigDecimal r10_amount) {
		this.r10_amount = r10_amount;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_amount() {
		return r11_amount;
	}
	public void setR11_amount(BigDecimal r11_amount) {
		this.r11_amount = r11_amount;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_amount() {
		return r12_amount;
	}
	public void setR12_amount(BigDecimal r12_amount) {
		this.r12_amount = r12_amount;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_amount() {
		return r14_amount;
	}
	public void setR14_amount(BigDecimal r14_amount) {
		this.r14_amount = r14_amount;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_amount() {
		return r15_amount;
	}
	public void setR15_amount(BigDecimal r15_amount) {
		this.r15_amount = r15_amount;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_amount() {
		return r16_amount;
	}
	public void setR16_amount(BigDecimal r16_amount) {
		this.r16_amount = r16_amount;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_amount() {
		return r17_amount;
	}
	public void setR17_amount(BigDecimal r17_amount) {
		this.r17_amount = r17_amount;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_amount() {
		return r18_amount;
	}
	public void setR18_amount(BigDecimal r18_amount) {
		this.r18_amount = r18_amount;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_amount() {
		return r19_amount;
	}
	public void setR19_amount(BigDecimal r19_amount) {
		this.r19_amount = r19_amount;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_amount() {
		return r20_amount;
	}
	public void setR20_amount(BigDecimal r20_amount) {
		this.r20_amount = r20_amount;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_amount() {
		return r21_amount;
	}
	public void setR21_amount(BigDecimal r21_amount) {
		this.r21_amount = r21_amount;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_amount() {
		return r22_amount;
	}
	public void setR22_amount(BigDecimal r22_amount) {
		this.r22_amount = r22_amount;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_amount() {
		return r23_amount;
	}
	public void setR23_amount(BigDecimal r23_amount) {
		this.r23_amount = r23_amount;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_amount() {
		return r24_amount;
	}
	public void setR24_amount(BigDecimal r24_amount) {
		this.r24_amount = r24_amount;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_amount() {
		return r25_amount;
	}
	public void setR25_amount(BigDecimal r25_amount) {
		this.r25_amount = r25_amount;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_amount() {
		return r26_amount;
	}
	public void setR26_amount(BigDecimal r26_amount) {
		this.r26_amount = r26_amount;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_amount() {
		return r27_amount;
	}
	public void setR27_amount(BigDecimal r27_amount) {
		this.r27_amount = r27_amount;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_amount() {
		return r28_amount;
	}
	public void setR28_amount(BigDecimal r28_amount) {
		this.r28_amount = r28_amount;
	}
	public String getR29_product() {
		return r29_product;
	}
	public void setR29_product(String r29_product) {
		this.r29_product = r29_product;
	}
	public BigDecimal getR29_amount() {
		return r29_amount;
	}
	public void setR29_amount(BigDecimal r29_amount) {
		this.r29_amount = r29_amount;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_amount() {
		return r30_amount;
	}
	public void setR30_amount(BigDecimal r30_amount) {
		this.r30_amount = r30_amount;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_amount() {
		return r31_amount;
	}
	public void setR31_amount(BigDecimal r31_amount) {
		this.r31_amount = r31_amount;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_amount() {
		return r32_amount;
	}
	public void setR32_amount(BigDecimal r32_amount) {
		this.r32_amount = r32_amount;
	}
	public String getR33_product() {
		return r33_product;
	}
	public void setR33_product(String r33_product) {
		this.r33_product = r33_product;
	}
	public BigDecimal getR33_amount() {
		return r33_amount;
	}
	public void setR33_amount(BigDecimal r33_amount) {
		this.r33_amount = r33_amount;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_amount() {
		return r34_amount;
	}
	public void setR34_amount(BigDecimal r34_amount) {
		this.r34_amount = r34_amount;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_amount() {
		return r35_amount;
	}
	public void setR35_amount(BigDecimal r35_amount) {
		this.r35_amount = r35_amount;
	}
	public String getR36_product() {
		return r36_product;
	}
	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}
	public BigDecimal getR36_amount() {
		return r36_amount;
	}
	public void setR36_amount(BigDecimal r36_amount) {
		this.r36_amount = r36_amount;
	}
	public String getR38_product() {
		return r38_product;
	}
	public void setR38_product(String r38_product) {
		this.r38_product = r38_product;
	}
	public BigDecimal getR38_amount() {
		return r38_amount;
	}
	public void setR38_amount(BigDecimal r38_amount) {
		this.r38_amount = r38_amount;
	}
	public String getR39_product() {
		return r39_product;
	}
	public void setR39_product(String r39_product) {
		this.r39_product = r39_product;
	}
	public BigDecimal getR39_amount() {
		return r39_amount;
	}
	public void setR39_amount(BigDecimal r39_amount) {
		this.r39_amount = r39_amount;
	}
	public String getR40_product() {
		return r40_product;
	}
	public void setR40_product(String r40_product) {
		this.r40_product = r40_product;
	}
	public BigDecimal getR40_amount() {
		return r40_amount;
	}
	public void setR40_amount(BigDecimal r40_amount) {
		this.r40_amount = r40_amount;
	}
	public String getR41_product() {
		return r41_product;
	}
	public void setR41_product(String r41_product) {
		this.r41_product = r41_product;
	}
	public BigDecimal getR41_amount() {
		return r41_amount;
	}
	public void setR41_amount(BigDecimal r41_amount) {
		this.r41_amount = r41_amount;
	}
	public String getR42_product() {
		return r42_product;
	}
	public void setR42_product(String r42_product) {
		this.r42_product = r42_product;
	}
	public BigDecimal getR42_amount() {
		return r42_amount;
	}
	public void setR42_amount(BigDecimal r42_amount) {
		this.r42_amount = r42_amount;
	}
	public String getR43_product() {
		return r43_product;
	}
	public void setR43_product(String r43_product) {
		this.r43_product = r43_product;
	}
	public BigDecimal getR43_amount() {
		return r43_amount;
	}
	public void setR43_amount(BigDecimal r43_amount) {
		this.r43_amount = r43_amount;
	}
	public String getR44_product() {
		return r44_product;
	}
	public void setR44_product(String r44_product) {
		this.r44_product = r44_product;
	}
	public BigDecimal getR44_amount() {
		return r44_amount;
	}
	public void setR44_amount(BigDecimal r44_amount) {
		this.r44_amount = r44_amount;
	}
	public String getR46_product() {
		return r46_product;
	}
	public void setR46_product(String r46_product) {
		this.r46_product = r46_product;
	}
	public BigDecimal getR46_amount() {
		return r46_amount;
	}
	public void setR46_amount(BigDecimal r46_amount) {
		this.r46_amount = r46_amount;
	}
	public String getR47_product() {
		return r47_product;
	}
	public void setR47_product(String r47_product) {
		this.r47_product = r47_product;
	}
	public BigDecimal getR47_amount() {
		return r47_amount;
	}
	public void setR47_amount(BigDecimal r47_amount) {
		this.r47_amount = r47_amount;
	}
	public String getR48_product() {
		return r48_product;
	}
	public void setR48_product(String r48_product) {
		this.r48_product = r48_product;
	}
	public BigDecimal getR48_amount() {
		return r48_amount;
	}
	public void setR48_amount(BigDecimal r48_amount) {
		this.r48_amount = r48_amount;
	}
	public String getR49_product() {
		return r49_product;
	}
	public void setR49_product(String r49_product) {
		this.r49_product = r49_product;
	}
	public BigDecimal getR49_amount() {
		return r49_amount;
	}
	public void setR49_amount(BigDecimal r49_amount) {
		this.r49_amount = r49_amount;
	}
	public String getR50_product() {
		return r50_product;
	}
	public void setR50_product(String r50_product) {
		this.r50_product = r50_product;
	}
	public BigDecimal getR50_amount() {
		return r50_amount;
	}
	public void setR50_amount(BigDecimal r50_amount) {
		this.r50_amount = r50_amount;
	}
	public String getR51_product() {
		return r51_product;
	}
	public void setR51_product(String r51_product) {
		this.r51_product = r51_product;
	}
	public BigDecimal getR51_amount() {
		return r51_amount;
	}
	public void setR51_amount(BigDecimal r51_amount) {
		this.r51_amount = r51_amount;
	}
	public String getR52_product() {
		return r52_product;
	}
	public void setR52_product(String r52_product) {
		this.r52_product = r52_product;
	}
	public BigDecimal getR52_amount() {
		return r52_amount;
	}
	public void setR52_amount(BigDecimal r52_amount) {
		this.r52_amount = r52_amount;
	}
	public String getR53_product() {
		return r53_product;
	}
	public void setR53_product(String r53_product) {
		this.r53_product = r53_product;
	}
	public BigDecimal getR53_amount() {
		return r53_amount;
	}
	public void setR53_amount(BigDecimal r53_amount) {
		this.r53_amount = r53_amount;
	}
	public String getR54_product() {
		return r54_product;
	}
	public void setR54_product(String r54_product) {
		this.r54_product = r54_product;
	}
	public BigDecimal getR54_amount() {
		return r54_amount;
	}
	public void setR54_amount(BigDecimal r54_amount) {
		this.r54_amount = r54_amount;
	}
	public String getR55_product() {
		return r55_product;
	}
	public void setR55_product(String r55_product) {
		this.r55_product = r55_product;
	}
	public BigDecimal getR55_amount() {
		return r55_amount;
	}
	public void setR55_amount(BigDecimal r55_amount) {
		this.r55_amount = r55_amount;
	}
	public String getR57_product() {
		return r57_product;
	}
	public void setR57_product(String r57_product) {
		this.r57_product = r57_product;
	}
	public BigDecimal getR57_amount() {
		return r57_amount;
	}
	public void setR57_amount(BigDecimal r57_amount) {
		this.r57_amount = r57_amount;
	}
	public String getR58_product() {
		return r58_product;
	}
	public void setR58_product(String r58_product) {
		this.r58_product = r58_product;
	}
	public BigDecimal getR58_amount() {
		return r58_amount;
	}
	public void setR58_amount(BigDecimal r58_amount) {
		this.r58_amount = r58_amount;
	}
	public String getR59_product() {
		return r59_product;
	}
	public void setR59_product(String r59_product) {
		this.r59_product = r59_product;
	}
	public BigDecimal getR59_amount() {
		return r59_amount;
	}
	public void setR59_amount(BigDecimal r59_amount) {
		this.r59_amount = r59_amount;
	}
	public String getR60_product() {
		return r60_product;
	}
	public void setR60_product(String r60_product) {
		this.r60_product = r60_product;
	}
	public BigDecimal getR60_amount() {
		return r60_amount;
	}
	public void setR60_amount(BigDecimal r60_amount) {
		this.r60_amount = r60_amount;
	}
	public String getR61_product() {
		return r61_product;
	}
	public void setR61_product(String r61_product) {
		this.r61_product = r61_product;
	}
	public BigDecimal getR61_amount() {
		return r61_amount;
	}
	public void setR61_amount(BigDecimal r61_amount) {
		this.r61_amount = r61_amount;
	}
	public String getR62_product() {
		return r62_product;
	}
	public void setR62_product(String r62_product) {
		this.r62_product = r62_product;
	}
	public BigDecimal getR62_amount() {
		return r62_amount;
	}
	public void setR62_amount(BigDecimal r62_amount) {
		this.r62_amount = r62_amount;
	}
	public String getR64_product() {
		return r64_product;
	}
	public void setR64_product(String r64_product) {
		this.r64_product = r64_product;
	}
	public BigDecimal getR64_amount() {
		return r64_amount;
	}
	public void setR64_amount(BigDecimal r64_amount) {
		this.r64_amount = r64_amount;
	}
	public String getR65_product() {
		return r65_product;
	}
	public void setR65_product(String r65_product) {
		this.r65_product = r65_product;
	}
	public BigDecimal getR65_amount() {
		return r65_amount;
	}
	public void setR65_amount(BigDecimal r65_amount) {
		this.r65_amount = r65_amount;
	}
	public String getR66_product() {
		return r66_product;
	}
	public void setR66_product(String r66_product) {
		this.r66_product = r66_product;
	}
	public BigDecimal getR66_amount() {
		return r66_amount;
	}
	public void setR66_amount(BigDecimal r66_amount) {
		this.r66_amount = r66_amount;
	}
	public String getR67_product() {
		return r67_product;
	}
	public void setR67_product(String r67_product) {
		this.r67_product = r67_product;
	}
	public BigDecimal getR67_amount() {
		return r67_amount;
	}
	public void setR67_amount(BigDecimal r67_amount) {
		this.r67_amount = r67_amount;
	}
	public String getR68_product() {
		return r68_product;
	}
	public void setR68_product(String r68_product) {
		this.r68_product = r68_product;
	}
	public BigDecimal getR68_amount() {
		return r68_amount;
	}
	public void setR68_amount(BigDecimal r68_amount) {
		this.r68_amount = r68_amount;
	}
	public String getR69_product() {
		return r69_product;
	}
	public void setR69_product(String r69_product) {
		this.r69_product = r69_product;
	}
	public BigDecimal getR69_amount() {
		return r69_amount;
	}
	public void setR69_amount(BigDecimal r69_amount) {
		this.r69_amount = r69_amount;
	}
	public String getR70_product() {
		return r70_product;
	}
	public void setR70_product(String r70_product) {
		this.r70_product = r70_product;
	}
	public BigDecimal getR70_amount() {
		return r70_amount;
	}
	public void setR70_amount(BigDecimal r70_amount) {
		this.r70_amount = r70_amount;
	}
	public String getR71_product() {
		return r71_product;
	}
	public void setR71_product(String r71_product) {
		this.r71_product = r71_product;
	}
	public BigDecimal getR71_amount() {
		return r71_amount;
	}
	public void setR71_amount(BigDecimal r71_amount) {
		this.r71_amount = r71_amount;
	}
	public String getR72_product() {
		return r72_product;
	}
	public void setR72_product(String r72_product) {
		this.r72_product = r72_product;
	}
	public BigDecimal getR72_amount() {
		return r72_amount;
	}
	public void setR72_amount(BigDecimal r72_amount) {
		this.r72_amount = r72_amount;
	}
	public String getR73_product() {
		return r73_product;
	}
	public void setR73_product(String r73_product) {
		this.r73_product = r73_product;
	}
	public BigDecimal getR73_amount() {
		return r73_amount;
	}
	public void setR73_amount(BigDecimal r73_amount) {
		this.r73_amount = r73_amount;
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

//-----------detail entity  CLS 

class B_III_CETD_Detail_RowMapper implements RowMapper<BASEL_III_COM_EQUITY_DISC_Detail_Entity> {

    @Override
    public BASEL_III_COM_EQUITY_DISC_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        BASEL_III_COM_EQUITY_DISC_Detail_Entity obj = new BASEL_III_COM_EQUITY_DISC_Detail_Entity();

        obj.setSno(rs.getBigDecimal("SNO"));
        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));

        obj.setDataType(rs.getString("DATA_TYPE"));
        obj.setReportName(rs.getString("REPORT_NAME"));
        obj.setReportLabel(rs.getString("REPORT_LABEL"));
        obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));

        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
        obj.setAverage(rs.getBigDecimal("AVERAGE"));

        obj.setReportDate(rs.getDate("REPORT_DATE"));

        obj.setCreateUser(rs.getString("CREATE_USER"));
        obj.setCreateTime(rs.getDate("CREATE_TIME"));

        obj.setModifyUser(rs.getString("MODIFY_USER"));
        obj.setModifyTime(rs.getDate("MODIFY_TIME"));

        obj.setVerifyUser(rs.getString("VERIFY_USER"));
        obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

        obj.setEntityFlg(
            rs.getString("ENTITY_FLG") != null
                ? rs.getString("ENTITY_FLG").charAt(0)
                : null
        );

        obj.setModifyFlg(
            rs.getString("MODIFY_FLG") != null
                ? rs.getString("MODIFY_FLG").charAt(0)
                : null
        );

        obj.setDelFlg(
            rs.getString("DEL_FLG") != null
                ? rs.getString("DEL_FLG").charAt(0)
                : null
        );

        obj.setGlshCode(rs.getString("GLSH_CODE"));
        obj.setGlCode(rs.getString("GL_CODE"));

        return obj;
    }
}

//-------ENTITY

public class BASEL_III_COM_EQUITY_DISC_Detail_Entity {

   
	
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

  @Column(name = "ACCT_BALANCE_IN_PULA", precision = 18, scale = 2)
  private BigDecimal acctBalanceInpula;
  
  @Column(name = "AVERAGE", precision = 18, scale = 2)
  private BigDecimal average;


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
  
  @Column(name = "GLSH_CODE", length = 50)
  private String glshCode;
  
  @Column(name = "GL_CODE", length = 50)
  private String glCode;
  
  @Column(name = "SNO")
  private BigDecimal sno;

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

public BigDecimal getSno() {
	return sno;
}

public void setSno(BigDecimal sno) {
	this.sno = sno;
}


}




//================================ARCHIVAL DETAIL==============================================================================


class B_III_CETD_Archival_Detail_RowMapper implements RowMapper<BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity> {

    @Override
    public BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

    	BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity obj = new BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity();

        obj.setSno(rs.getBigDecimal("SNO"));
        obj.setCustId(rs.getString("CUST_ID"));
        obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
        obj.setAcctName(rs.getString("ACCT_NAME"));

        obj.setDataType(rs.getString("DATA_TYPE"));
        obj.setReportName(rs.getString("REPORT_NAME"));
        obj.setReportLabel(rs.getString("REPORT_LABEL"));
        obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));

        obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
        obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
        obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

        obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
        obj.setAverage(rs.getBigDecimal("AVERAGE"));

        obj.setReportDate(rs.getDate("REPORT_DATE"));

        obj.setCreateUser(rs.getString("CREATE_USER"));
        obj.setCreateTime(rs.getDate("CREATE_TIME"));
        obj.setModifyUser(rs.getString("MODIFY_USER"));
        obj.setModifyTime(rs.getDate("MODIFY_TIME"));
        obj.setVerifyUser(rs.getString("VERIFY_USER"));
        obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

        obj.setEntityFlg(
            rs.getString("ENTITY_FLG") != null
                ? rs.getString("ENTITY_FLG").charAt(0)
                : null
        );

        obj.setModifyFlg(
            rs.getString("MODIFY_FLG") != null
                ? rs.getString("MODIFY_FLG").charAt(0)
                : null
        );

        obj.setDelFlg(
            rs.getString("DEL_FLG") != null
                ? rs.getString("DEL_FLG").charAt(0)
                : null
        );

        obj.setGlshCode(rs.getString("GLSH_CODE"));
        obj.setGlCode(rs.getString("GL_CODE"));

        return obj;
    }
}


//=======ARCHIVAL DETAIL ENTITY 

public class BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity {

   
	
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

  @Column(name = "ACCT_BALANCE_IN_PULA", precision = 18, scale = 2)
  private BigDecimal acctBalanceInpula;
  
  @Column(name = "AVERAGE", precision = 18, scale = 2)
  private BigDecimal average;

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
  
  
  @Column(name = "GLSH_CODE", length = 50)
  private String glshCode;
  
  @Column(name = "GL_CODE", length = 50)
  private String glCode;
  
  @Column(name = "SNO")
  private BigDecimal sno;

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

public BigDecimal getSno() {
	return sno;
}

public void setSno(BigDecimal sno) {
	this.sno = sno;
}


}


  //=====================================================
 // MODEL AND VIEW METHOD summary
 //=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
		 public ModelAndView getB_III_CETDView(

	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("B_III_CETD View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    // =====================================================
	    // ARCHIVAL MODE
	    // =====================================================

	    if ("ARCHIVAL".equals(type) && version != null) {

	        List<BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity> T1Master = new ArrayList<>();
	    

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

	        List<BASEL_III_COM_EQUITY_DISC_Summary_Entity> T1Master = new ArrayList<>();
	       

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

	    mv.setViewName("BRRS/B_III_CETD");
	    mv.addObject("displaymode", "summary");
	   

	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 
 public ModelAndView getB_III_CETDcurrentDtl(
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

	            List<BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity> archivalDetailList;

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

	            List<BASEL_III_COM_EQUITY_DISC_Detail_Entity> currentDetailList;

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

	    mv.setViewName("BRRS/B_III_CETD");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}
 
 
 //Archival View
		public List<Object[]> getB_III_CETDArchival() {
			List<Object[]> archivalList = new ArrayList<>();
			

			try {
				
				List<BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity> repoData =
						getarchivaldatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  B_III_CETD  Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}
	
		public void updateReport(
		        BASEL_III_COM_EQUITY_DISC_Summary_Entity updatedEntity) {

		    System.out.println("Came to BASEL_III_COM_EQUITY_DISC Manual Update");
		    System.out.println("Report Date: " + updatedEntity.getReport_date());

		   int[] rows = {73 };

		String[] fields = { "amount"};

		    try {

		        // Loop rows
		      for (int i : rows) {
				for (String field : fields) {

					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;

		                try {

		                    Method getter =
		                            BASEL_III_COM_EQUITY_DISC_Summary_Entity.class
		                                    .getMethod(getterName);

		                    Object value =
		                            getter.invoke(updatedEntity);

		                    // Skip null values
		                    if (value == null) continue;

		                    // Column name in DB
		                    String columnName =
		                    		"R" + i + "_" + field;

		                    String sql =
		                            "UPDATE BRRS_BASEL_III_COM_EQUITY_DISC_SUMMARYTABLE " +
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
		        }

		        System.out.println("B_III_CETD Manual Update Completed");

		    } catch (Exception e) {
		        throw new RuntimeException(
		                "Error while updating B_III_CETD Manual fields", e);
		    }
		}
		
public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/B_III_CETD"); 

		if (acctNo != null) {
			BASEL_III_COM_EQUITY_DISC_Detail_Entity b_III_cetdEntity = findByDetailAcctnumber(acctNo);
			if (b_III_cetdEntity != null && b_III_cetdEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(b_III_cetdEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("b_III_cetdData", b_III_cetdEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}
	
	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("acctBalanceInpula");
			String average = request.getParameter("average");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			BASEL_III_COM_EQUITY_DISC_Detail_Entity existing = findByDetailAcctnumber(acctNo);
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
    "UPDATE BRRS_BASEL_III_COM_EQUITY_DISC_DETAILTABLE " +
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
							logger.info("Transaction committed — calling BRRS_BASEL_III_COM_EQUITY_DISC_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_BASEL_III_COM_EQUITY_DISC_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating B_III_CETD record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
	
	
		public byte[] getB_III_CETDDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
				String type, String version) {
			try {
				logger.info("Generating Excel for  B_III_CETD  Details...");
				System.out.println("came to Detail download service");

				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getB_III_CETDDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("B_III_CETD Details ");

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
				List<BASEL_III_COM_EQUITY_DISC_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (BASEL_III_COM_EQUITY_DISC_Detail_Entity item : reportData) { 
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
					logger.info("No data found for B_III_CETD — only header will be written.");
				}

				// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating B_III_CETD Excel", e);
				return new byte[0];
			}
		}
//===========================================getB_III_CETDDetailExcelARCHIVAL	


		public byte[] getB_III_CETDDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
				String dtltype, String type, String version) {
			try {
				logger.info("Generating Excel for B_III_CETD ARCHIVAL Details...");
				System.out.println("came to ARCHIVAL Detail download service");
				if (type.equals("ARCHIVAL") & version != null) {

				}
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("B_III_CETD Detail NEW");

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
				List<BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity> reportData = getarchivaldetaildatabydateList(parsedToDate,
						version);

				if (reportData != null && !reportData.isEmpty()) {
					int rowIndex = 1;
					for (BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity item : reportData) {
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
					logger.info("No data found for B_III_CETD — only header will be written.");
				}

	// Write to byte[]
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				workbook.close();

				logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
				return bos.toByteArray();

			} catch (Exception e) {
				logger.error("Error generating B_III_CETD NEW Excel", e);
				return new byte[0];
			}
		}
		
//===========================EXCEL 

		
			public byte[] getB_III_CETDExcel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, BigDecimal version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.B_III_CETD");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type)
			        && version != null
			        && version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelB_III_CETDARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			// Fetch data

			List<BASEL_III_COM_EQUITY_DISC_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		
		
			System.out.println("DATA SIZE IS : "+dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  B_III_CETD report. Returning empty result.");
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
						BASEL_III_COM_EQUITY_DISC_Summary_Entity record = dataList.get(i);
					
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

	  	// row7
					// Column C

					
					// R7
					row = sheet.getRow(6);
					Cell cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR7_amount() != null ? record.getR7_amount().doubleValue() : 0);

					// R8
					row = sheet.getRow(7);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR8_amount() != null ? record.getR8_amount().doubleValue() : 0);

					// R9
					row = sheet.getRow(8);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR9_amount() != null ? record.getR9_amount().doubleValue() : 0);

					// R10
					row = sheet.getRow(9);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR10_amount() != null ? record.getR10_amount().doubleValue() : 0);

					// R11
					row = sheet.getRow(10);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR11_amount() != null ? record.getR11_amount().doubleValue() : 0);

					/*
					 * // R12 row = sheet.getRow(11); cellC = row.getCell(4); if (cellC == null)
					 * cellC = row.createCell(4); cellC.setCellValue(record.getR12_amount() != null
					 * ? record.getR12_amount().doubleValue() : 0);
					 */

					

					// R14
					row = sheet.getRow(13);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR14_amount() != null ? record.getR14_amount().doubleValue() : 0);

					// R15
					row = sheet.getRow(14);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR15_amount() != null ? record.getR15_amount().doubleValue() : 0);

					// R16
					row = sheet.getRow(15);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR16_amount() != null ? record.getR16_amount().doubleValue() : 0);

					// R17
					row = sheet.getRow(16);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR17_amount() != null ? record.getR17_amount().doubleValue() : 0);

					// R18
					row = sheet.getRow(17);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR18_amount() != null ? record.getR18_amount().doubleValue() : 0);

					// R19
					row = sheet.getRow(18);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR19_amount() != null ? record.getR19_amount().doubleValue() : 0);

					// R20
					row = sheet.getRow(19);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR20_amount() != null ? record.getR20_amount().doubleValue() : 0);

					// R21
					row = sheet.getRow(20);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR21_amount() != null ? record.getR21_amount().doubleValue() : 0);

					// R22
					row = sheet.getRow(21);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR22_amount() != null ? record.getR22_amount().doubleValue() : 0);

					// R23
					row = sheet.getRow(22);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR23_amount() != null ? record.getR23_amount().doubleValue() : 0);

					// R24
					row = sheet.getRow(23);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR24_amount() != null ? record.getR24_amount().doubleValue() : 0);

					// R25
					row = sheet.getRow(24);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR25_amount() != null ? record.getR25_amount().doubleValue() : 0);

					// R26
					row = sheet.getRow(25);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR26_amount() != null ? record.getR26_amount().doubleValue() : 0);

					// R27
					row = sheet.getRow(26);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR27_amount() != null ? record.getR27_amount().doubleValue() : 0);

					// R28
					row = sheet.getRow(27);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR28_amount() != null ? record.getR28_amount().doubleValue() : 0);

					// R29
					row = sheet.getRow(28);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR29_amount() != null ? record.getR29_amount().doubleValue() : 0);

					// R30
					row = sheet.getRow(29);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR30_amount() != null ? record.getR30_amount().doubleValue() : 0);

					// R31
					row = sheet.getRow(30);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR31_amount() != null ? record.getR31_amount().doubleValue() : 0);

					// R32
					row = sheet.getRow(31);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR32_amount() != null ? record.getR32_amount().doubleValue() : 0);

					// R33
					row = sheet.getRow(32);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR33_amount() != null ? record.getR33_amount().doubleValue() : 0);

					// R34
					row = sheet.getRow(33);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR34_amount() != null ? record.getR34_amount().doubleValue() : 0);

					// R35
					row = sheet.getRow(34);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR35_amount() != null ? record.getR35_amount().doubleValue() : 0);

					/*
					 * // R36 row = sheet.getRow(35); cellC = row.getCell(4); if (cellC == null)
					 * cellC = row.createCell(4); cellC.setCellValue(record.getR36_amount() != null
					 * ? record.getR36_amount().doubleValue() : 0);
					 */

					

					// R38
					row = sheet.getRow(37);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR38_amount() != null ? record.getR38_amount().doubleValue() : 0);

					// R39
					row = sheet.getRow(38);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR39_amount() != null ? record.getR39_amount().doubleValue() : 0);

					// R40
					row = sheet.getRow(39);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR40_amount() != null ? record.getR40_amount().doubleValue() : 0);

					// R41
					row = sheet.getRow(40);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR41_amount() != null ? record.getR41_amount().doubleValue() : 0);

					// R42
					row = sheet.getRow(41);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR42_amount() != null ? record.getR42_amount().doubleValue() : 0);

					// R43
					row = sheet.getRow(42);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR43_amount() != null ? record.getR43_amount().doubleValue() : 0);

					// R44
					row = sheet.getRow(43);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR44_amount() != null ? record.getR44_amount().doubleValue() : 0);

				

					// R46
					row = sheet.getRow(45);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR46_amount() != null ? record.getR46_amount().doubleValue() : 0);

					// R47
					row = sheet.getRow(46);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR47_amount() != null ? record.getR47_amount().doubleValue() : 0);

					// R48
					row = sheet.getRow(47);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR48_amount() != null ? record.getR48_amount().doubleValue() : 0);

					// R49
					row = sheet.getRow(48);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR49_amount() != null ? record.getR49_amount().doubleValue() : 0);

					// R50
					row = sheet.getRow(49);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR50_amount() != null ? record.getR50_amount().doubleValue() : 0);

					// R51
					row = sheet.getRow(50);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR51_amount() != null ? record.getR51_amount().doubleValue() : 0);

					// R52
					row = sheet.getRow(51);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR52_amount() != null ? record.getR52_amount().doubleValue() : 0);

					// R53
					row = sheet.getRow(52);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR53_amount() != null ? record.getR53_amount().doubleValue() : 0);

					// R54
					row = sheet.getRow(53);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR54_amount() != null ? record.getR54_amount().doubleValue() : 0);

					/*
					 * // R55 row = sheet.getRow(54); cellC = row.getCell(4); if (cellC == null)
					 * cellC = row.createCell(4); cellC.setCellValue(record.getR55_amount() != null
					 * ? record.getR55_amount().doubleValue() : 0);
					 */

				

					// R57
					row = sheet.getRow(56);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR57_amount() != null ? record.getR57_amount().doubleValue() : 0);

					// R58
					row = sheet.getRow(57);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR58_amount() != null ? record.getR58_amount().doubleValue() : 0);

					// R59
					row = sheet.getRow(58);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR59_amount() != null ? record.getR59_amount().doubleValue() : 0);

					// R60
					row = sheet.getRow(59);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR60_amount() != null ? record.getR60_amount().doubleValue() : 0);

					// R61
					row = sheet.getRow(60);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR61_amount() != null ? record.getR61_amount().doubleValue() : 0);

					/*
					 * // R62 row = sheet.getRow(61); cellC = row.getCell(4); if (cellC == null)
					 * cellC = row.createCell(4); cellC.setCellValue(record.getR62_amount() != null
					 * ? record.getR62_amount().doubleValue() : 0);
					 */
				

					// R64
					row = sheet.getRow(63);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR64_amount() != null ? record.getR64_amount().doubleValue() : 0);

					// R65
					row = sheet.getRow(64);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR65_amount() != null ? record.getR65_amount().doubleValue() : 0);

					// R66
					row = sheet.getRow(65);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR66_amount() != null ? record.getR66_amount().doubleValue() : 0);

					// R67
					row = sheet.getRow(66);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR67_amount() != null ? record.getR67_amount().doubleValue() : 0);

					// R68
					row = sheet.getRow(67);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR68_amount() != null ? record.getR68_amount().doubleValue() : 0);

					// R69
					row = sheet.getRow(68);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR69_amount() != null ? record.getR69_amount().doubleValue() : 0);

					// R70
					row = sheet.getRow(69);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR70_amount() != null ? record.getR70_amount().doubleValue() : 0);
					/*
					 * // R71 row = sheet.getRow(70); cellC = row.getCell(4); if (cellC == null)
					 * cellC = row.createCell(4); cellC.setCellValue(record.getR71_amount() != null
					 * ? record.getR71_amount().doubleValue() : 0);
					 * 
					 * // R72 row = sheet.getRow(71); cellC = row.getCell(4); if (cellC == null)
					 * cellC = row.createCell(4); cellC.setCellValue(record.getR72_amount() != null
					 * ? record.getR72_amount().doubleValue() : 0);
					 */

					// R73
					row = sheet.getRow(72);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR73_amount() != null ? record.getR73_amount().doubleValue() : 0);

				
					
					
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
		
		
			public byte[] getExcelB_III_CETDARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {

			}

			List<BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity> dataList = 
					getdatabydateListarchival(dateformat.parse(todate), version);
		
	    

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for B_III_CETD new report. Returning empty result.");
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
						BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity record = dataList.get(i);
					    
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


					// row7
					// Column C

					
					// R7
					row = sheet.getRow(6);
					Cell cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR7_amount() != null ? record.getR7_amount().doubleValue() : 0);

					// R8
					row = sheet.getRow(7);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR8_amount() != null ? record.getR8_amount().doubleValue() : 0);

					// R9
					row = sheet.getRow(8);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR9_amount() != null ? record.getR9_amount().doubleValue() : 0);

					// R10
					row = sheet.getRow(9);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR10_amount() != null ? record.getR10_amount().doubleValue() : 0);

					// R11
					row = sheet.getRow(10);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR11_amount() != null ? record.getR11_amount().doubleValue() : 0);

//					// R12
//					row = sheet.getRow(11);
//					cellC = row.getCell(4);
//					if (cellC == null) cellC = row.createCell(4);
//					cellC.setCellValue(record.getR12_amount() != null ? record.getR12_amount().doubleValue() : 0);

					

					// R14
					row = sheet.getRow(13);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR14_amount() != null ? record.getR14_amount().doubleValue() : 0);

					// R15
					row = sheet.getRow(14);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR15_amount() != null ? record.getR15_amount().doubleValue() : 0);

					// R16
					row = sheet.getRow(15);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR16_amount() != null ? record.getR16_amount().doubleValue() : 0);

					// R17
					row = sheet.getRow(16);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR17_amount() != null ? record.getR17_amount().doubleValue() : 0);

					// R18
					row = sheet.getRow(17);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR18_amount() != null ? record.getR18_amount().doubleValue() : 0);

					// R19
					row = sheet.getRow(18);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR19_amount() != null ? record.getR19_amount().doubleValue() : 0);

					// R20
					row = sheet.getRow(19);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR20_amount() != null ? record.getR20_amount().doubleValue() : 0);

					// R21
					row = sheet.getRow(20);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR21_amount() != null ? record.getR21_amount().doubleValue() : 0);

					// R22
					row = sheet.getRow(21);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR22_amount() != null ? record.getR22_amount().doubleValue() : 0);

					// R23
					row = sheet.getRow(22);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR23_amount() != null ? record.getR23_amount().doubleValue() : 0);

					// R24
					row = sheet.getRow(23);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR24_amount() != null ? record.getR24_amount().doubleValue() : 0);

					// R25
					row = sheet.getRow(24);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR25_amount() != null ? record.getR25_amount().doubleValue() : 0);

					// R26
					row = sheet.getRow(25);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR26_amount() != null ? record.getR26_amount().doubleValue() : 0);

					// R27
					row = sheet.getRow(26);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR27_amount() != null ? record.getR27_amount().doubleValue() : 0);

					// R28
					row = sheet.getRow(27);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR28_amount() != null ? record.getR28_amount().doubleValue() : 0);

					// R29
					row = sheet.getRow(28);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR29_amount() != null ? record.getR29_amount().doubleValue() : 0);

					// R30
					row = sheet.getRow(29);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR30_amount() != null ? record.getR30_amount().doubleValue() : 0);

					// R31
					row = sheet.getRow(30);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR31_amount() != null ? record.getR31_amount().doubleValue() : 0);

					// R32
					row = sheet.getRow(31);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR32_amount() != null ? record.getR32_amount().doubleValue() : 0);

					// R33
					row = sheet.getRow(32);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR33_amount() != null ? record.getR33_amount().doubleValue() : 0);

					// R34
					row = sheet.getRow(33);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR34_amount() != null ? record.getR34_amount().doubleValue() : 0);

					// R35
					row = sheet.getRow(34);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR35_amount() != null ? record.getR35_amount().doubleValue() : 0);

//					// R36
//					row = sheet.getRow(35);
//					cellC = row.getCell(4);
//					if (cellC == null) cellC = row.createCell(4);
//					cellC.setCellValue(record.getR36_amount() != null ? record.getR36_amount().doubleValue() : 0);

					

					// R38
					row = sheet.getRow(37);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR38_amount() != null ? record.getR38_amount().doubleValue() : 0);

					// R39
					row = sheet.getRow(38);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR39_amount() != null ? record.getR39_amount().doubleValue() : 0);

					// R40
					row = sheet.getRow(39);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR40_amount() != null ? record.getR40_amount().doubleValue() : 0);

					// R41
					row = sheet.getRow(40);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR41_amount() != null ? record.getR41_amount().doubleValue() : 0);

					// R42
					row = sheet.getRow(41);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR42_amount() != null ? record.getR42_amount().doubleValue() : 0);

					// R43
					row = sheet.getRow(42);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR43_amount() != null ? record.getR43_amount().doubleValue() : 0);

					// R44
					row = sheet.getRow(43);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR44_amount() != null ? record.getR44_amount().doubleValue() : 0);

				

					// R46
					row = sheet.getRow(45);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR46_amount() != null ? record.getR46_amount().doubleValue() : 0);

					// R47
					row = sheet.getRow(46);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR47_amount() != null ? record.getR47_amount().doubleValue() : 0);

					// R48
					row = sheet.getRow(47);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR48_amount() != null ? record.getR48_amount().doubleValue() : 0);

					// R49
					row = sheet.getRow(48);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR49_amount() != null ? record.getR49_amount().doubleValue() : 0);

					// R50
					row = sheet.getRow(49);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR50_amount() != null ? record.getR50_amount().doubleValue() : 0);

					// R51
					row = sheet.getRow(50);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR51_amount() != null ? record.getR51_amount().doubleValue() : 0);

					// R52
					row = sheet.getRow(51);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR52_amount() != null ? record.getR52_amount().doubleValue() : 0);

					// R53
					row = sheet.getRow(52);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR53_amount() != null ? record.getR53_amount().doubleValue() : 0);

					// R54
					row = sheet.getRow(53);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR54_amount() != null ? record.getR54_amount().doubleValue() : 0);

					/*
					 * // R55 row = sheet.getRow(54); cellC = row.getCell(4); if (cellC == null)
					 * cellC = row.createCell(4); cellC.setCellValue(record.getR55_amount() != null
					 * ? record.getR55_amount().doubleValue() : 0);
					 */
				

					// R57
					row = sheet.getRow(56);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR57_amount() != null ? record.getR57_amount().doubleValue() : 0);

					// R58
					row = sheet.getRow(57);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR58_amount() != null ? record.getR58_amount().doubleValue() : 0);

					// R59
					row = sheet.getRow(58);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR59_amount() != null ? record.getR59_amount().doubleValue() : 0);

					// R60
					row = sheet.getRow(59);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR60_amount() != null ? record.getR60_amount().doubleValue() : 0);

					// R61
					row = sheet.getRow(60);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR61_amount() != null ? record.getR61_amount().doubleValue() : 0);

//					// R62
//					row = sheet.getRow(61);
//					cellC = row.getCell(4);
//					if (cellC == null) cellC = row.createCell(4);
//					cellC.setCellValue(record.getR62_amount() != null ? record.getR62_amount().doubleValue() : 0);

				

					// R64
					row = sheet.getRow(63);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR64_amount() != null ? record.getR64_amount().doubleValue() : 0);

					// R65
					row = sheet.getRow(64);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR65_amount() != null ? record.getR65_amount().doubleValue() : 0);

					// R66
					row = sheet.getRow(65);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR66_amount() != null ? record.getR66_amount().doubleValue() : 0);

					// R67
					row = sheet.getRow(66);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR67_amount() != null ? record.getR67_amount().doubleValue() : 0);

					// R68
					row = sheet.getRow(67);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR68_amount() != null ? record.getR68_amount().doubleValue() : 0);

					// R69
					row = sheet.getRow(68);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR69_amount() != null ? record.getR69_amount().doubleValue() : 0);

					// R70
					row = sheet.getRow(69);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR70_amount() != null ? record.getR70_amount().doubleValue() : 0);

					/*
					 * // R71 row = sheet.getRow(70); cellC = row.getCell(4); if (cellC == null)
					 * cellC = row.createCell(4); cellC.setCellValue(record.getR71_amount() != null
					 * ? record.getR71_amount().doubleValue() : 0);
					 * 
					 * // R72 row = sheet.getRow(71); cellC = row.getCell(4); if (cellC == null)
					 * cellC = row.createCell(4); cellC.setCellValue(record.getR72_amount() != null
					 * ? record.getR72_amount().doubleValue() : 0);
					 */

					// R73
					row = sheet.getRow(72);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR73_amount() != null ? record.getR73_amount().doubleValue() : 0);

						
						
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