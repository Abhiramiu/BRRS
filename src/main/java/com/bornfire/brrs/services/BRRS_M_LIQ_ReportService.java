
//========================LIQ MANUAL+MAPPING REPORT 

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


@Service

public class BRRS_M_LIQ_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LIQ_ReportService.class);
	
	
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


	public List<M_LIQ_Summary_Entity> getSummaryDataByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_M_LIQ_SUMMARYTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new M_LIQ_Summary_RowMapper()
    );
}
	

// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================



public List<Object[]> get_M_LIQ_archival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
                 "FROM BRRS_M_LIQ_ARCHIVALTABLE_SUMMARY " +
                 "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[]{
                    rs.getDate("REPORT_DATE"),
                    rs.getBigDecimal("REPORT_VERSION")
            }
    );
}

public List<M_LIQ_Archival_Summary_Entity> getDataByDateListArchival(
        Date reportDate, BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_M_LIQ_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate, reportVersion},
            new M_LIQ_Archival_Summary_RowMapper()
    );
}

public List<M_LIQ_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

    String sql = "SELECT * FROM BRRS_M_LIQ_ARCHIVALTABLE_SUMMARY " +
                 "WHERE REPORT_VERSION IS NOT NULL " +
                 "ORDER BY REPORT_VERSION ASC";

    return jdbcTemplate.query(
            sql,
            new M_LIQ_Archival_Summary_RowMapper()
    );
}

public BigDecimal findMaxVersion(Date reportDate) {

    String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_LIQ_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[]{reportDate},
            BigDecimal.class
    );
}

// =====================================================
// DETAIL REPO
// =====================================================	

// Fetch all records for a given date
public List<M_LIQ_Detail_Entity> getDetaildatabydateList(Date reportDate) {

    String sql = "SELECT * FROM BRRS_M_LIQ_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[]{reportDate},
            new M_LIQ_Detail_RowMapper()
    );
}
//  Pagination fixed → use OFFSET and LIMIT correctly
public List<M_LIQ_Detail_Entity> get_DetaildatabydateList(Date reportDate, int offset, int limit) {

    String sql = "SELECT * FROM BRRS_M_LIQ_DETAILTABLE "
               + "WHERE REPORT_DATE = ? "
               + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportDate, offset, limit },
            new M_LIQ_Detail_RowMapper()
    );
}
// Count rows by date
public int getdatacount(Date reportDate) {

    String sql = "SELECT COUNT(*) FROM BRRS_M_LIQ_DETAILTABLE WHERE REPORT_DATE = ?";

    return jdbcTemplate.queryForObject(
            sql,
            new Object[] { reportDate },
            Integer.class
    );
}



public List<M_LIQ_Detail_Entity> GetDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria_1,
        Date reportDate) {

    String sql = "SELECT * FROM BRRS_M_LIQ_DETAILTABLE "
               + "WHERE REPORT_LABEL = ? "
               + "AND REPORT_ADDL_CRITERIA_1 = ? "
               + "AND REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportLabel, reportAddlCriteria_1, reportDate },
            new M_LIQ_Detail_RowMapper()
    );
}


public M_LIQ_Detail_Entity findByAcctnumber(String acctNumber) {

    String sql = "SELECT * FROM BRRS_M_LIQ_DETAILTABLE WHERE ACCT_NUMBER = ?";

    List<M_LIQ_Detail_Entity> list = jdbcTemplate.query(
            sql,
            new Object[] { acctNumber },
            new M_LIQ_Detail_RowMapper());

    return list.isEmpty() ? null : list.get(0);
}

// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================


public List<M_LIQ_Archival_Detail_Entity> getArchival_DetaildatabydateList(
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_M_LIQ_ARCHIVALTABLE_DETAIL "
               + "WHERE REPORT_DATE = ? "
               + "AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportDate, dataEntryVersion },
            new M_LIQ_Archival_Detail_RowMapper()
    );
}


public List<M_LIQ_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(
        String reportLabel,
        String reportAddlCriteria_1,
        Date reportDate,
        String dataEntryVersion) {

    String sql = "SELECT * FROM BRRS_M_LIQ_ARCHIVALTABLE_DETAIL "
               + "WHERE REPORT_LABEL = ? "
               + "AND REPORT_ADDL_CRITERIA_1 = ? "
               + "AND REPORT_DATE = ? "
               + "AND DATA_ENTRY_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] {
                    reportLabel,
                    reportAddlCriteria_1,
                    reportDate,
                    dataEntryVersion
            },
            new M_LIQ_Archival_Detail_RowMapper());
}

// =====================================================
// MANUAL SUMMARY 
// =====================================================

public List<M_LIQ_Manual_Summary_Entity> getManualSummaryByDate(Date reportDate) {

    String sql = "SELECT * FROM BRRS_M_LIQ_MANUAL_SUMMARYTABLE "
               + "WHERE REPORT_DATE = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportDate },
            new M_LIQ_Manual_Summary_RowMapper()
    );
}

// =====================================================
// MANUAL ARCHIVAL SUMMARY 
// =====================================================

public List<Object[]> getM_LIQmanualarchival() {

    String sql = "SELECT REPORT_DATE, REPORT_VERSION "
               + "FROM BRRS_M_LIQ_MANUAL_ARCHIVALTABLE_SUMMARY "
               + "ORDER BY REPORT_VERSION";

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Object[] {
                    rs.getDate("REPORT_DATE"),
                    rs.getString("REPORT_VERSION")
            });
}

public List<M_LIQ_Archival_Manual_Summary_Entity> getArchival_ManualdatabydateListarchival(
        Date reportDate,
        BigDecimal reportVersion) {

    String sql = "SELECT * FROM BRRS_M_LIQ_MANUAL_ARCHIVALTABLE_SUMMARY "
               + "WHERE REPORT_DATE = ? "
               + "AND REPORT_VERSION = ?";

    return jdbcTemplate.query(
            sql,
            new Object[] { reportDate, reportVersion },
            new M_LIQ_Archival_Manual_Summary_RowMapper()
    );
}

// =====================================================
// RESUBMISSION
// =====================================================


public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
	String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
			+ "FROM BRRS_M_LIQ_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";
	return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

}

public M_LIQ_Detail_Entity findBysnoArch(String sno) {

	String sql = "SELECT * FROM BRRS_M_LIQ_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

	return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new M_LIQ_Detail_RowMapper());
}

public M_LIQ_Detail_Entity findBysno(String sno) {

	String sql = "SELECT * FROM BRRS_M_LIQ_DETAILTABLE WHERE SNO = ?";

	return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new M_LIQ_Detail_RowMapper());
}


// =====================================================
// SUMAMRY ENTITY & ROW MAPPER 
// =====================================================


public class M_LIQ_Summary_RowMapper implements RowMapper<M_LIQ_Summary_Entity> {

    @Override
    public M_LIQ_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_LIQ_Summary_Entity obj = new M_LIQ_Summary_Entity();


obj.setR10_product(rs.getString("r10_product"));
obj.setR10_total(rs.getBigDecimal("r10_total"));

obj.setR11_product(rs.getString("r11_product"));
obj.setR11_total(rs.getBigDecimal("r11_total"));

obj.setR12_product(rs.getString("r12_product"));
obj.setR12_total(rs.getBigDecimal("r12_total"));

obj.setR13_product(rs.getString("r13_product"));
obj.setR13_total(rs.getBigDecimal("r13_total"));

obj.setR14_product(rs.getString("r14_product"));
obj.setR14_total(rs.getBigDecimal("r14_total"));

obj.setR15_product(rs.getString("r15_product"));
obj.setR15_total(rs.getBigDecimal("r15_total"));

obj.setR17_product(rs.getString("r17_product"));
obj.setR17_total(rs.getBigDecimal("r17_total"));

obj.setR18_product(rs.getString("r18_product"));
obj.setR18_total(rs.getBigDecimal("r18_total"));

obj.setR21_product(rs.getString("r21_product"));
obj.setR21_total(rs.getBigDecimal("r21_total"));

obj.setR24_product(rs.getString("r24_product"));
obj.setR24_total(rs.getBigDecimal("r24_total"));

obj.setR25_product(rs.getString("r25_product"));
obj.setR25_total(rs.getBigDecimal("r25_total"));

obj.setR27_product(rs.getString("r27_product"));
obj.setR27_total(rs.getBigDecimal("r27_total"));

obj.setR30_product(rs.getString("r30_product"));
obj.setR30_total(rs.getBigDecimal("r30_total"));

obj.setR31_product(rs.getString("r31_product"));
obj.setR31_total(rs.getBigDecimal("r31_total"));

obj.setR32_product(rs.getString("r32_product"));
obj.setR32_total(rs.getBigDecimal("r32_total"));

obj.setR34_product(rs.getString("r34_product"));
obj.setR34_total(rs.getBigDecimal("r34_total"));

obj.setR35_product(rs.getString("r35_product"));
obj.setR35_total(rs.getBigDecimal("r35_total"));

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


public class M_LIQ_Summary_Entity {
	
	private String	r10_product;
	private BigDecimal	r10_total;
	
	private String	r11_product;
	private BigDecimal	r11_total;
	
	private String	r12_product;
	private BigDecimal	r12_total;
	
	private String	r13_product;
	private BigDecimal	r13_total;
	
	private String	r14_product;
	private BigDecimal	r14_total;
	
	private String	r15_product;
	private BigDecimal	r15_total;
	
	private String	r17_product;
	private BigDecimal	r17_total;
	
	private String	r18_product;
	private BigDecimal	r18_total;
	
	private String	r21_product;
	private BigDecimal	r21_total;
	
	private String	r24_product;
	private BigDecimal	r24_total;
	
	private String	r25_product;
	private BigDecimal	r25_total;
	
	private String	r27_product;
	private BigDecimal	r27_total;
	
	private String	r30_product;
	private BigDecimal	r30_total;
	
	private String	r31_product;
	private BigDecimal	r31_total;
	
	private String	r32_product;
	private BigDecimal	r32_total;
	
	private String	r34_product;
	private BigDecimal	r34_total;
	
	private String	r35_product;
	private BigDecimal	r35_total;
	

	
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
	
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_total() {
		return r10_total;
	}
	public void setR10_total(BigDecimal r10_total) {
		this.r10_total = r10_total;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_total() {
		return r11_total;
	}
	public void setR11_total(BigDecimal r11_total) {
		this.r11_total = r11_total;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_total() {
		return r12_total;
	}
	public void setR12_total(BigDecimal r12_total) {
		this.r12_total = r12_total;
	}
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_total() {
		return r13_total;
	}
	public void setR13_total(BigDecimal r13_total) {
		this.r13_total = r13_total;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_total() {
		return r14_total;
	}
	public void setR14_total(BigDecimal r14_total) {
		this.r14_total = r14_total;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_total() {
		return r15_total;
	}
	public void setR15_total(BigDecimal r15_total) {
		this.r15_total = r15_total;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_total() {
		return r17_total;
	}
	public void setR17_total(BigDecimal r17_total) {
		this.r17_total = r17_total;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_total() {
		return r18_total;
	}
	public void setR18_total(BigDecimal r18_total) {
		this.r18_total = r18_total;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_total() {
		return r21_total;
	}
	public void setR21_total(BigDecimal r21_total) {
		this.r21_total = r21_total;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_total() {
		return r24_total;
	}
	public void setR24_total(BigDecimal r24_total) {
		this.r24_total = r24_total;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_total() {
		return r25_total;
	}
	public void setR25_total(BigDecimal r25_total) {
		this.r25_total = r25_total;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_total() {
		return r27_total;
	}
	public void setR27_total(BigDecimal r27_total) {
		this.r27_total = r27_total;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_total() {
		return r30_total;
	}
	public void setR30_total(BigDecimal r30_total) {
		this.r30_total = r30_total;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_total() {
		return r31_total;
	}
	public void setR31_total(BigDecimal r31_total) {
		this.r31_total = r31_total;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_total() {
		return r32_total;
	}
	public void setR32_total(BigDecimal r32_total) {
		this.r32_total = r32_total;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_total() {
		return r34_total;
	}
	public void setR34_total(BigDecimal r34_total) {
		this.r34_total = r34_total;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_total() {
		return r35_total;
	}
	public void setR35_total(BigDecimal r35_total) {
		this.r35_total = r35_total;
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
// ARCHIVAL  SUMAMRY ENTITY  RowMapper
// =====================================================


public class M_LIQ_Archival_Summary_RowMapper
        implements RowMapper<M_LIQ_Archival_Summary_Entity> {

    @Override
    public M_LIQ_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        M_LIQ_Archival_Summary_Entity obj = new M_LIQ_Archival_Summary_Entity();

obj.setR10_product(rs.getString("r10_product"));
obj.setR10_total(rs.getBigDecimal("r10_total"));

obj.setR11_product(rs.getString("r11_product"));
obj.setR11_total(rs.getBigDecimal("r11_total"));

obj.setR12_product(rs.getString("r12_product"));
obj.setR12_total(rs.getBigDecimal("r12_total"));

obj.setR13_product(rs.getString("r13_product"));
obj.setR13_total(rs.getBigDecimal("r13_total"));

obj.setR14_product(rs.getString("r14_product"));
obj.setR14_total(rs.getBigDecimal("r14_total"));

obj.setR15_product(rs.getString("r15_product"));
obj.setR15_total(rs.getBigDecimal("r15_total"));

obj.setR17_product(rs.getString("r17_product"));
obj.setR17_total(rs.getBigDecimal("r17_total"));

obj.setR18_product(rs.getString("r18_product"));
obj.setR18_total(rs.getBigDecimal("r18_total"));

obj.setR21_product(rs.getString("r21_product"));
obj.setR21_total(rs.getBigDecimal("r21_total"));

obj.setR24_product(rs.getString("r24_product"));
obj.setR24_total(rs.getBigDecimal("r24_total"));

obj.setR25_product(rs.getString("r25_product"));
obj.setR25_total(rs.getBigDecimal("r25_total"));

obj.setR27_product(rs.getString("r27_product"));
obj.setR27_total(rs.getBigDecimal("r27_total"));

obj.setR30_product(rs.getString("r30_product"));
obj.setR30_total(rs.getBigDecimal("r30_total"));

obj.setR31_product(rs.getString("r31_product"));
obj.setR31_total(rs.getBigDecimal("r31_total"));

obj.setR32_product(rs.getString("r32_product"));
obj.setR32_total(rs.getBigDecimal("r32_total"));

obj.setR34_product(rs.getString("r34_product"));
obj.setR34_total(rs.getBigDecimal("r34_total"));

obj.setR35_product(rs.getString("r35_product"));
obj.setR35_total(rs.getBigDecimal("r35_total"));

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


public class M_LIQ_Archival_Summary_Entity {
	
	
	private String	r10_product;
	private BigDecimal	r10_total;
	
	private String	r11_product;
	private BigDecimal	r11_total;
	
	private String	r12_product;
	private BigDecimal	r12_total;
	
	private String	r13_product;
	private BigDecimal	r13_total;
	
	private String	r14_product;
	private BigDecimal	r14_total;
	
	private String	r15_product;
	private BigDecimal	r15_total;
	
	private String	r17_product;
	private BigDecimal	r17_total;
	
	private String	r18_product;
	private BigDecimal	r18_total;
	
	private String	r21_product;
	private BigDecimal	r21_total;
	
	private String	r22_product;
	
	private String	r24_product;
	private BigDecimal	r24_total;
	
	private String	r25_product;
	private BigDecimal	r25_total;
	
	private String	r27_product;
	private BigDecimal	r27_total;
	
	private String	r30_product;
	private BigDecimal	r30_total;
	
	private String	r31_product;
	private BigDecimal	r31_total;
	
	private String	r32_product;
	private BigDecimal	r32_total;
	
	private String	r34_product;
	private BigDecimal	r34_total;
	
	private String	r35_product;
	private BigDecimal	r35_total;
	
	
	               
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
	
	
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_total() {
		return r10_total;
	}
	public void setR10_total(BigDecimal r10_total) {
		this.r10_total = r10_total;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_total() {
		return r11_total;
	}
	public void setR11_total(BigDecimal r11_total) {
		this.r11_total = r11_total;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_total() {
		return r12_total;
	}
	public void setR12_total(BigDecimal r12_total) {
		this.r12_total = r12_total;
	}
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_total() {
		return r13_total;
	}
	public void setR13_total(BigDecimal r13_total) {
		this.r13_total = r13_total;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_total() {
		return r14_total;
	}
	public void setR14_total(BigDecimal r14_total) {
		this.r14_total = r14_total;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_total() {
		return r15_total;
	}
	public void setR15_total(BigDecimal r15_total) {
		this.r15_total = r15_total;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_total() {
		return r17_total;
	}
	public void setR17_total(BigDecimal r17_total) {
		this.r17_total = r17_total;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_total() {
		return r18_total;
	}
	public void setR18_total(BigDecimal r18_total) {
		this.r18_total = r18_total;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_total() {
		return r21_total;
	}
	public void setR21_total(BigDecimal r21_total) {
		this.r21_total = r21_total;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_total() {
		return r24_total;
	}
	public void setR24_total(BigDecimal r24_total) {
		this.r24_total = r24_total;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_total() {
		return r25_total;
	}
	public void setR25_total(BigDecimal r25_total) {
		this.r25_total = r25_total;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_total() {
		return r27_total;
	}
	public void setR27_total(BigDecimal r27_total) {
		this.r27_total = r27_total;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_total() {
		return r30_total;
	}
	public void setR30_total(BigDecimal r30_total) {
		this.r30_total = r30_total;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_total() {
		return r31_total;
	}
	public void setR31_total(BigDecimal r31_total) {
		this.r31_total = r31_total;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_total() {
		return r32_total;
	}
	public void setR32_total(BigDecimal r32_total) {
		this.r32_total = r32_total;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_total() {
		return r34_total;
	}
	public void setR34_total(BigDecimal r34_total) {
		this.r34_total = r34_total;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_total() {
		return r35_total;
	}
	public void setR35_total(BigDecimal r35_total) {
		this.r35_total = r35_total;
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
// DETAIL ENTITY  M_LIQ
// =====================================================	

public class M_LIQ_Detail_RowMapper implements RowMapper<M_LIQ_Detail_Entity> {

    @Override
    public M_LIQ_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_LIQ_Detail_Entity obj = new M_LIQ_Detail_Entity();

obj.setSno(rs.getLong("sno"));

obj.setCust_id(rs.getString("cust_id"));
obj.setAcct_number(rs.getString("acct_number"));
obj.setAcct_name(rs.getString("acct_name"));
obj.setData_type(rs.getString("data_type"));
obj.setReport_name(rs.getString("report_name"));
obj.setReport_label(rs.getString("report_label"));
obj.setReport_addl_criteria_1(rs.getString("report_addl_criteria_1"));
obj.setReport_addl_criteria_2(rs.getString("report_addl_criteria_2"));
obj.setReport_addl_criteria_3(rs.getString("report_addl_criteria_3"));
obj.setReport_remarks(rs.getString("report_remarks"));

obj.setSanction_limit(rs.getBigDecimal("sanction_limit"));
obj.setModification_remarks(rs.getString("modification_remarks"));
obj.setData_entry_version(rs.getString("data_entry_version"));
obj.setAcct_balance_in_pula(rs.getBigDecimal("acct_balance_in_pula"));

obj.setReport_date(rs.getDate("report_date"));

obj.setCreate_user(rs.getString("create_user"));
obj.setCreate_time(rs.getTimestamp("create_time"));

obj.setModify_user(rs.getString("modify_user"));
obj.setModify_time(rs.getTimestamp("modify_time"));

obj.setVerify_user(rs.getString("verify_user"));
obj.setVerify_time(rs.getTimestamp("verify_time"));

obj.setEntity_flg(rs.getString("entity_flg"));
obj.setModify_flg(rs.getString("modify_flg"));
obj.setDel_flg(rs.getString("del_flg"));

obj.setGl_code(rs.getString("gl_code"));
obj.setGlsh_code(rs.getString("glsh_code"));
obj.setCurrency(rs.getString("currency"));

        return obj;
    }
}

public class M_LIQ_Detail_Entity {

   
	@Id
	@Column(name = "SNO")
	private Long sno;

	private String	cust_id;
	
	private String	acct_number;
	private String	acct_name;
	private String	data_type;
	private String	report_name;
	private String	report_label;
	private String	report_addl_criteria_1;
	private String	report_addl_criteria_2;
	private String	report_addl_criteria_3;
	private String	report_remarks;
	private BigDecimal	sanction_limit;
	private String	modification_remarks;
	private String	data_entry_version;
	private BigDecimal	acct_balance_in_pula;
	
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date	report_date;
	private String	create_user;
	private Date	create_time;
	private String	modify_user;
	private Date	modify_time;
	private String	verify_user;
	private Date	verify_time;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	private String	gl_code;
	private String	glsh_code;
	private String	currency;
	
	
	
public Long getSno() {
		return sno;
	}
	public void setSno(Long sno) {
		this.sno = sno;
	}
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getAcct_number() {
		return acct_number;
	}
	public void setAcct_number(String acct_number) {
		this.acct_number = acct_number;
	}
	public String getAcct_name() {
		return acct_name;
	}
	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getReport_name() {
		return report_name;
	}
	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}
	public String getReport_label() {
		return report_label;
	}
	public void setReport_label(String report_label) {
		this.report_label = report_label;
	}
	public String getReport_addl_criteria_1() {
		return report_addl_criteria_1;
	}
	public void setReport_addl_criteria_1(String report_addl_criteria_1) {
		this.report_addl_criteria_1 = report_addl_criteria_1;
	}
	public String getReport_addl_criteria_2() {
		return report_addl_criteria_2;
	}
	public void setReport_addl_criteria_2(String report_addl_criteria_2) {
		this.report_addl_criteria_2 = report_addl_criteria_2;
	}
	public String getReport_addl_criteria_3() {
		return report_addl_criteria_3;
	}
	public void setReport_addl_criteria_3(String report_addl_criteria_3) {
		this.report_addl_criteria_3 = report_addl_criteria_3;
	}
	public String getReport_remarks() {
		return report_remarks;
	}
	public void setReport_remarks(String report_remarks) {
		this.report_remarks = report_remarks;
	}
	public BigDecimal getSanction_limit() {
		return sanction_limit;
	}
	public void setSanction_limit(BigDecimal sanction_limit) {
		this.sanction_limit = sanction_limit;
	}
	public String getModification_remarks() {
		return modification_remarks;
	}
	public void setModification_remarks(String modification_remarks) {
		this.modification_remarks = modification_remarks;
	}
	public String getData_entry_version() {
		return data_entry_version;
	}
	public void setData_entry_version(String data_entry_version) {
		this.data_entry_version = data_entry_version;
	}
	public BigDecimal getAcct_balance_in_pula() {
		return acct_balance_in_pula;
	}
	public void setAcct_balance_in_pula(BigDecimal acct_balance_in_pula) {
		this.acct_balance_in_pula = acct_balance_in_pula;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public String getVerify_user() {
		return verify_user;
	}
	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}
	public Date getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
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
	public String getGl_code() {
		return gl_code;
	}
	public void setGl_code(String gl_code) {
		this.gl_code = gl_code;
	}
	public String getGlsh_code() {
		return glsh_code;
	}
	public void setGlsh_code(String glsh_code) {
		this.glsh_code = glsh_code;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
}

			  

// =====================================================
// ARCHIVAL  DETAIL ENTITY 
// =====================================================


public class M_LIQ_Archival_Detail_RowMapper 
        implements RowMapper<M_LIQ_Archival_Detail_Entity> {

    @Override
    public M_LIQ_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_LIQ_Archival_Detail_Entity obj = new M_LIQ_Archival_Detail_Entity();

obj.setSno(rs.getLong("sno"));

obj.setCust_id(rs.getString("cust_id"));
obj.setAcct_number(rs.getString("acct_number"));
obj.setAcct_name(rs.getString("acct_name"));
obj.setData_type(rs.getString("data_type"));
obj.setReport_name(rs.getString("report_name"));
obj.setReport_label(rs.getString("report_label"));
obj.setReport_addl_criteria_1(rs.getString("report_addl_criteria_1"));
obj.setReport_addl_criteria_2(rs.getString("report_addl_criteria_2"));
obj.setReport_addl_criteria_3(rs.getString("report_addl_criteria_3"));
obj.setReport_remarks(rs.getString("report_remarks"));

obj.setSanction_limit(rs.getBigDecimal("sanction_limit"));
obj.setModification_remarks(rs.getString("modification_remarks"));
obj.setData_entry_version(rs.getString("data_entry_version"));
obj.setAcct_balance_in_pula(rs.getBigDecimal("acct_balance_in_pula"));

obj.setReport_date(rs.getDate("report_date"));

obj.setCreate_user(rs.getString("create_user"));
obj.setCreate_time(rs.getDate("create_time"));

obj.setModify_user(rs.getString("modify_user"));
obj.setModify_time(rs.getDate("modify_time"));

obj.setVerify_user(rs.getString("verify_user"));
obj.setVerify_time(rs.getDate("verify_time"));

obj.setEntity_flg(rs.getString("entity_flg"));
obj.setModify_flg(rs.getString("modify_flg"));
obj.setDel_flg(rs.getString("del_flg"));

obj.setGl_code(rs.getString("gl_code"));
obj.setGlsh_code(rs.getString("glsh_code"));
obj.setCurrency(rs.getString("currency"));
        return obj;
    }
}

public class M_LIQ_Archival_Detail_Entity {

  @Id
	@Column(name = "SNO")
	private Long sno;

	private String	cust_id;
	private String	acct_number;
	private String	acct_name;
	private String	data_type;
	private String	report_name;
	private String	report_label;
	private String	report_addl_criteria_1;
	private String	report_addl_criteria_2;
	private String	report_addl_criteria_3;
	private String	report_remarks;
	private BigDecimal	sanction_limit;
	private String	modification_remarks;
	private String	data_entry_version;
	private BigDecimal	acct_balance_in_pula;
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date	report_date;
	private String	create_user;
	private Date	create_time;
	private String	modify_user;
	private Date	modify_time;
	private String	verify_user;
	private Date	verify_time;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	private String	gl_code;
	private String	glsh_code;
	private String	currency;
	
public Long getSno() {
		return sno;
	}
	public void setSno(Long sno) {
		this.sno = sno;
	}
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getAcct_number() {
		return acct_number;
	}
	public void setAcct_number(String acct_number) {
		this.acct_number = acct_number;
	}
	public String getAcct_name() {
		return acct_name;
	}
	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getReport_name() {
		return report_name;
	}
	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}
	public String getReport_label() {
		return report_label;
	}
	public void setReport_label(String report_label) {
		this.report_label = report_label;
	}
	public String getReport_addl_criteria_1() {
		return report_addl_criteria_1;
	}
	public void setReport_addl_criteria_1(String report_addl_criteria_1) {
		this.report_addl_criteria_1 = report_addl_criteria_1;
	}
	public String getReport_addl_criteria_2() {
		return report_addl_criteria_2;
	}
	public void setReport_addl_criteria_2(String report_addl_criteria_2) {
		this.report_addl_criteria_2 = report_addl_criteria_2;
	}
	public String getReport_addl_criteria_3() {
		return report_addl_criteria_3;
	}
	public void setReport_addl_criteria_3(String report_addl_criteria_3) {
		this.report_addl_criteria_3 = report_addl_criteria_3;
	}
	public String getReport_remarks() {
		return report_remarks;
	}
	public void setReport_remarks(String report_remarks) {
		this.report_remarks = report_remarks;
	}
	public BigDecimal getSanction_limit() {
		return sanction_limit;
	}
	public void setSanction_limit(BigDecimal sanction_limit) {
		this.sanction_limit = sanction_limit;
	}
	public String getModification_remarks() {
		return modification_remarks;
	}
	public void setModification_remarks(String modification_remarks) {
		this.modification_remarks = modification_remarks;
	}
	public String getData_entry_version() {
		return data_entry_version;
	}
	public void setData_entry_version(String data_entry_version) {
		this.data_entry_version = data_entry_version;
	}
	public BigDecimal getAcct_balance_in_pula() {
		return acct_balance_in_pula;
	}
	public void setAcct_balance_in_pula(BigDecimal acct_balance_in_pula) {
		this.acct_balance_in_pula = acct_balance_in_pula;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public String getVerify_user() {
		return verify_user;
	}
	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}
	public Date getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
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
	public String getGl_code() {
		return gl_code;
	}
	public void setGl_code(String gl_code) {
		this.gl_code = gl_code;
	}
	public String getGlsh_code() {
		return glsh_code;
	}
	public void setGlsh_code(String glsh_code) {
		this.glsh_code = glsh_code;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

    

	

}


//=====================================================
// MANUAL summary M_LIQ
//=====================================================


public class M_LIQ_Manual_Summary_RowMapper 
        implements RowMapper<M_LIQ_Manual_Summary_Entity> {

    @Override
    public M_LIQ_Manual_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_LIQ_Manual_Summary_Entity obj = new M_LIQ_Manual_Summary_Entity();

obj.setR16_product(rs.getString("r16_product"));
obj.setR16_total(rs.getBigDecimal("r16_total"));

obj.setR19_product(rs.getString("r19_product"));
obj.setR19_total(rs.getBigDecimal("r19_total"));

obj.setR20_product(rs.getString("r20_product"));
obj.setR20_total(rs.getBigDecimal("r20_total"));

obj.setR22_product(rs.getString("r22_product"));
obj.setR22_total(rs.getBigDecimal("r22_total"));

obj.setR23_product(rs.getString("r23_product"));
obj.setR23_total(rs.getBigDecimal("r23_total"));

obj.setR26_product(rs.getString("r26_product"));
obj.setR26_total(rs.getBigDecimal("r26_total"));

       // =========================
        // COMMON FIELDS
        // =========================
        obj.setReport_date(rs.getDate("report_date"));
        obj.setReport_version(rs.getBigDecimal("report_version"));
//        obj.setReportResubDate(rs.getDate("report_resubdate"));

        obj.setReport_frequency(rs.getString("report_frequency"));
        obj.setReport_code(rs.getString("report_code"));
        obj.setReport_desc(rs.getString("report_desc"));

        obj.setEntity_flg(rs.getString("entity_flg"));
        obj.setModify_flg(rs.getString("modify_flg"));
        obj.setDel_flg(rs.getString("del_flg"));

        return obj;
    }
}

public static class M_LIQ_Manual_Summary_Entity {

   
private String	r16_product;
	private BigDecimal	r16_total;

	private String	r19_product;
	private BigDecimal	r19_total;

	private String	r20_product;
	private BigDecimal	r20_total;

	private String	r22_product;
	private BigDecimal	r22_total;

	private String	r23_product;
	private BigDecimal	r23_total;

	
	private String	r26_product;
	  @Column(name = "R26_TOTAL")
	private BigDecimal	r26_total;
	

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id	
	private Date	report_date;
	
	@Id
	private BigDecimal	report_version;
	
//	@Column(name = "REPORT_RESUBDATE")
//    private Date reportResubDate;
	
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	 del_flg;
	
	
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_total() {
		return r16_total;
	}
	public void setR16_total(BigDecimal r16_total) {
		this.r16_total = r16_total;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_total() {
		return r19_total;
	}
	public void setR19_total(BigDecimal r19_total) {
		this.r19_total = r19_total;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_total() {
		return r20_total;
	}
	public void setR20_total(BigDecimal r20_total) {
		this.r20_total = r20_total;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_total() {
		return r22_total;
	}
	public void setR22_total(BigDecimal r22_total) {
		this.r22_total = r22_total;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_total() {
		return r23_total;
	}
	public void setR23_total(BigDecimal r23_total) {
		this.r23_total = r23_total;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_total() {
		return r26_total;
	}
	public void setR26_total(BigDecimal r26_total) {
		this.r26_total = r26_total;
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
//	public Date getReportResubDate() {
//		return reportResubDate;
//	}
//	public void setReportResubDate(Date reportResubDate) {
//		this.reportResubDate = reportResubDate;
//	}
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
// ARCHIVAL MANUAL SUMMARY  M_LIQ
//=====================================================

public class M_LIQ_Archival_Manual_Summary_RowMapper 
        implements RowMapper<M_LIQ_Archival_Manual_Summary_Entity> {

    @Override
    public M_LIQ_Archival_Manual_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        M_LIQ_Archival_Manual_Summary_Entity obj = new M_LIQ_Archival_Manual_Summary_Entity();

obj.setR16_product(rs.getString("r16_product"));
obj.setR16_total(rs.getBigDecimal("r16_total"));

obj.setR19_product(rs.getString("r19_product"));
obj.setR19_total(rs.getBigDecimal("r19_total"));

obj.setR20_product(rs.getString("r20_product"));
obj.setR20_total(rs.getBigDecimal("r20_total"));

obj.setR22_product(rs.getString("r22_product"));
obj.setR22_total(rs.getBigDecimal("r22_total"));

obj.setR23_product(rs.getString("r23_product"));
obj.setR23_total(rs.getBigDecimal("r23_total"));

obj.setR26_product(rs.getString("r26_product"));
obj.setR26_total(rs.getBigDecimal("r26_total"));

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

public static class M_LIQ_Archival_Manual_Summary_Entity {

   	private String	r16_product;
	private BigDecimal	r16_total;

	private String	r19_product;
	private BigDecimal	r19_total;

	private String	r20_product;
	private BigDecimal	r20_total;

	private String	r22_product;
	private BigDecimal	r22_total;

	private String	r23_product;
	private BigDecimal	r23_total;

	
	private String	r26_product;
	  @Column(name = "R26_TOTAL")
	private BigDecimal	r26_total;
	
	
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
	
	
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_total() {
		return r16_total;
	}
	public void setR16_total(BigDecimal r16_total) {
		this.r16_total = r16_total;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_total() {
		return r19_total;
	}
	public void setR19_total(BigDecimal r19_total) {
		this.r19_total = r19_total;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_total() {
		return r20_total;
	}
	public void setR20_total(BigDecimal r20_total) {
		this.r20_total = r20_total;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_total() {
		return r22_total;
	}
	public void setR22_total(BigDecimal r22_total) {
		this.r22_total = r22_total;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_total() {
		return r23_total;
	}
	public void setR23_total(BigDecimal r23_total) {
		this.r23_total = r23_total;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_total() {
		return r26_total;
	}
	public void setR26_total(BigDecimal r26_total) {
		this.r26_total = r26_total;
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
// MODEL AND VIEW METHOD summary M_LIQ
//=====================================================

 SimpleDateFormat dateformat =
         new SimpleDateFormat("dd-MMM-yyyy");
		 
		 
//public ModelAndView getM_LIQView(
//        String reportId,
//        String fromdate,
//        String todate,
//        String currency,
//        String dtltype,
//        Pageable pageable,
//        String type,
//        BigDecimal version,
//        HttpServletRequest req1,
//        Model md) {
//
//    ModelAndView mv = new ModelAndView();
//
//    String userid = (String) req1.getSession().getAttribute("USERID");
//
//    System.out.println("User Id Maker and Checker: " + userid);
//
//    String role = userProfileRep.getUserRole(userid);
//
//    md.addAttribute("role", role);
//
//    System.out.println("Role: " + role);
//
//    System.out.println("Testing");
//    System.out.println("Type : " + type);
//    System.out.println("Version : " + version);
//
//    try {
//
//        Date reportDate = dateformat.parse(todate);
//
//        // =====================================================
//        // ARCHIVAL
//        // =====================================================
//
//        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
//
//            List<M_LIQ_Archival_Summary_Entity> T1Master =
//                    getDataByDateListArchival(reportDate, version);
//
//            List<M_LIQ_Archival_Manual_Summary_Entity> T2Master =
//            		getArchival_ManualdatabydateListarchival(reportDate, version);
//
//            mv.addObject("reportsummary", T1Master);
//            mv.addObject("reportsummary1", T2Master);
//
//        }
//
//        // =====================================================
//        // NORMAL
//        // =====================================================
//
//        else {
//
//            List<M_LIQ_Summary_Entity> T1Master =
//                    getSummaryDataByDate(reportDate);
//
//            List<M_LIQ_Manual_Summary_Entity> T2Master =
//                    getManualSummaryByDate(reportDate);
//
//            System.out.println("Manual Summary Size : " + T2Master.size());
//
//            mv.addObject("reportsummary", T1Master);
//            mv.addObject("reportsummary1", T2Master);
//        }
//
//        mv.addObject("report_date", dateformat.format(reportDate));
//
//    } catch (ParseException e) {
//        e.printStackTrace();
//    }
//
//    mv.setViewName("BRRS/M_LIQ");
//
//    mv.addObject("displaymode", "summary");
//
//    System.out.println("View Loaded : " + mv.getViewName());
//
//    return mv;
//}
 
 public ModelAndView getM_LIQView(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version,
	        HttpServletRequest req1,
	        Model md) {

	    ModelAndView mv = new ModelAndView();

	    String userid = (String) req1.getSession().getAttribute("USERID");

	    System.out.println("User Id Maker and Checker: " + userid);

	    String role = userProfileRep.getUserRole(userid);

	    md.addAttribute("role", role);

	    System.out.println("Role: " + role);

	    System.out.println("Testing");
	    System.out.println("Type : " + type);
	    System.out.println("Version : " + version);

	    try {

	        Date reportDate = dateformat.parse(todate);

	        // =====================================================
	        // ARCHIVAL + RESUB
	        // =====================================================

	        if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))
	                && version != null) {

	            List<M_LIQ_Archival_Summary_Entity> T1Master =
	                    getDataByDateListArchival(reportDate, version);

	            List<M_LIQ_Archival_Manual_Summary_Entity> T2Master =
	                    getArchival_ManualdatabydateListarchival(reportDate, version);

	            System.out.println("Archival Manual Summary Size : " + T2Master.size());

	            mv.addObject("reportsummary", T1Master);
	            mv.addObject("reportsummary1", T2Master);

	            mv.addObject("report_date", dateformat.format(reportDate));
	            mv.addObject("REPORT_DATE", dateformat.format(reportDate));

	            System.out.println("allowdetail : "
	                    + getishighestversion(reportDate, version));

	            mv.addObject("allowdetail",
	                    getishighestversion(reportDate, version));
	        }

	        // =====================================================
	        // NORMAL
	        // =====================================================

	        else {

	            List<M_LIQ_Summary_Entity> T1Master =
	                    getSummaryDataByDate(reportDate);

	            List<M_LIQ_Manual_Summary_Entity> T2Master =
	                    getManualSummaryByDate(reportDate);

	            System.out.println("Manual Summary Size : " + T2Master.size());

	            mv.addObject("reportsummary", T1Master);
	            mv.addObject("reportsummary1", T2Master);

	            mv.addObject("report_date", dateformat.format(reportDate));
	            mv.addObject("REPORT_DATE", dateformat.format(reportDate));
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_LIQ");
	    mv.addObject("displaymode", "summary");

	    System.out.println("View Loaded : " + mv.getViewName());

	    return mv;
	}
	
	
//=====================================================
// MODEL AND VIEW METHOD detail
//=====================================================
 
 public ModelAndView getM_LIQcurrentDtl(
        String reportId,
        String fromdate,
        String todate,
        String currency,
        String dtltype,
        Pageable pageable,
        String filter,
        String type,
        String version,
        HttpServletRequest req1,
        Model md) {

    int pageSize = pageable != null ? pageable.getPageSize() : 10;
    int currentPage = pageable != null ? pageable.getPageNumber() : 0;
    int totalPages = 0;

    ModelAndView mv = new ModelAndView();

    String userid = (String) req1.getSession().getAttribute("USERID");

    System.out.println("User Id Maker and Checker : " + userid);

    String role = userProfileRep.getUserRole(userid);

    md.addAttribute("role", role);

    System.out.println("Role : " + role);

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

      

      //=====================================================
      // ARCHIVAL / RESUB
      //=====================================================

     if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))
             && version != null) {

         System.out.println(type + " DETAIL MODE");

         List<M_LIQ_Archival_Detail_Entity> T1Dt1;

         if (reportLabel != null && reportAddlCriteria1 != null) {

             T1Dt1 = GetArchivalDataByRowIdAndColumnId(
                     reportLabel,
                     reportAddlCriteria1,
                     parsedDate,
                     version);

         } else {

             T1Dt1 = getArchival_DetaildatabydateList(
                     parsedDate,
                     version);
         }

         mv.addObject("reportdetails", T1Dt1);
         mv.addObject("reportmaster12", T1Dt1);

         System.out.println(type + " DETAIL COUNT : " + T1Dt1.size());
     }
     
   //=====================================================
   // CURRENT
   //=====================================================

  else {

      List<M_LIQ_Detail_Entity> T1Dt1;

      if (reportLabel != null && reportAddlCriteria1 != null) {

          T1Dt1 = GetDataByRowIdAndColumnId(
                  reportLabel,
                  reportAddlCriteria1,
                  parsedDate);

      } else {

          T1Dt1 = getDetaildatabydateList(parsedDate);

          totalPages = getdatacount(parsedDate);

          mv.addObject("pagination", "YES");
      }

      mv.addObject("reportdetails", T1Dt1);
      mv.addObject("reportmaster12", T1Dt1);

      System.out.println("CURRENT COUNT : " + T1Dt1.size());
  }

    } catch (ParseException e) {

        e.printStackTrace();
        mv.addObject("errorMessage", "Invalid date format : " + todate);

    } catch (Exception e) {

        e.printStackTrace();
        mv.addObject("errorMessage", "Unexpected error : " + e.getMessage());
    }

    mv.setViewName("BRRS/M_LIQ");

    mv.addObject("displaymode", "Details");
    mv.addObject("currentPage", currentPage);
    mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
    mv.addObject("reportsflag", "reportsflag");
    mv.addObject("menu", reportId);

    return mv;
}
 
//=====================================================
// MODEL AND VIEW METHOD ARCHIVAL VIEW
//=====================================================

// Archival View
public List<Object[]> getM_LIQArchival() {

    List<Object[]> archivalList = new ArrayList<>();

    try {

        List<M_LIQ_Archival_Summary_Entity> repoData =
                getarchivaldatabydateListWithVersion();

        if (repoData != null && !repoData.isEmpty()) {

            for (M_LIQ_Archival_Summary_Entity entity : repoData) {

                Object[] row = new Object[] {
                        entity.getReport_date(),
                        entity.getReport_version(),
                        entity.getReportResubDate()
                };

                archivalList.add(row);
            }

            System.out.println("Fetched " + archivalList.size() + " archival records");

            M_LIQ_Archival_Summary_Entity first = repoData.get(0);

            System.out.println("Latest archival version: "
                    + first.getReport_version());

        } else {

            System.out.println("No archival data found.");
        }

    } catch (Exception e) {

        System.err.println("Error fetching M_LIQ Archival data: "
                + e.getMessage());

        e.printStackTrace();
    }

    return archivalList;
}
//=====================================================
// UPDATE REPORT
//=====================================================

@Transactional
public void updateReport(Object entity, String type) {

    boolean isResub = "RESUB".equalsIgnoreCase(type);

    System.out.println("Came to M_LIQ Manual Summary Update. Type : "
            + (isResub ? "RESUB" : "NORMAL"));

    String tableName = isResub
            ? "BRRS_M_LIQ_MANUAL_ARCHIVALTABLE_SUMMARY"
            : "BRRS_M_LIQ_MANUAL_SUMMARYTABLE";

    int[] rows = {16, 19, 20, 22, 23, 26};

    try {

        Class<?> entityClass = entity.getClass();

        Method getDateMethod = entityClass.getMethod("getReport_date");
        Object reportDateObj = getDateMethod.invoke(entity);

        if (reportDateObj == null) {
            throw new RuntimeException("Report Date is NULL");
        }

        Date reportDate = (Date) reportDateObj;
        java.sql.Date sqlReportDate = new java.sql.Date(reportDate.getTime());

        System.out.println("Report Date : " + reportDate);
        System.out.println("Entity Class : " + entityClass.getName());

        StringBuilder changesBuilder = new StringBuilder();

        String[] fields = { "product", "total" };

        for (int r : rows) {

            for (String field : fields) {

                String getterName = "getR" + r + "_" + field;
                String columnName = "R" + r + "_" + field.toUpperCase();

                try {

                    Method getter = entityClass.getMethod(getterName);
                    Object newValueObj = getter.invoke(entity);

                    System.out.println("Processing -> " + getterName + " = " + newValueObj);

                    if (newValueObj == null) {
                        continue;
                    }

                    // Fetch current value from database
                    String selectSql = "SELECT " + columnName
                            + " FROM " + tableName
                            + " WHERE REPORT_DATE = ?";

                    Object dbValueObj = null;

                    try {
                        dbValueObj = jdbcTemplate.queryForObject(
                                selectSql,
                                Object.class,
                                sqlReportDate);
                    } catch (Exception e) {
                        dbValueObj = null;
                    }

                    String currentVal =
                            dbValueObj == null ? "" : dbValueObj.toString().trim();

                    String newVal =
                            newValueObj.toString().trim();

                    if (currentVal.equals(newVal)) {
                        continue;
                    }

                    if (changesBuilder.length() > 0) {
                        changesBuilder.append("|||");
                    }

                    changesBuilder.append(columnName)
                            .append(": OldValue: ")
                            .append(currentVal.isEmpty() ? "null" : currentVal)
                            .append(", NewValue: ")
                            .append(newVal);

                    String updateSql =
                            "UPDATE " + tableName
                                    + " SET " + columnName + "=?"
                                    + " WHERE REPORT_DATE=?";

                    int count = jdbcTemplate.update(
                            updateSql,
                            newValueObj,
                            sqlReportDate);

                    System.out.println("Updated Column : "
                            + columnName
                            + " Rows Affected : "
                            + count);

                } catch (NoSuchMethodException ex) {
                    System.out.println("Method not found : "
                            + getterName
                            + " - Skipping");
                }
            }
        }

        String changes = changesBuilder.toString();

        System.out.println("M_LIQ Manual Changes Length = " + changes.length());

        if (!changes.isEmpty()) {

            if (changes.length() > 1900) {
                changes = changes.substring(0, 1900);
            }

            auditService.compareEntitiesmanual(
                    entity,
                    entity,
                    reportDate.toString(),
                    "M LIQ Manual Summary Screen",
                    tableName);
        }

        System.out.println("M_LIQ Manual Summary Updated Successfully");

    } catch (Exception e) {

        System.err.println("===== M_LIQ MANUAL UPDATE ERROR =====");
        e.printStackTrace();

        Throwable root = e;
        while (root.getCause() != null) {
            root = root.getCause();
        }

        System.err.println("ROOT CAUSE : " + root.getMessage());

        throw new RuntimeException(
                "Error while updating M_LIQ Manual Summary",
                e);
    }
}

//=====================================================
// VIEW AND EDIT
//=====================================================

//public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
//
//    ModelAndView mv = new ModelAndView("BRRS/M_LIQ");
//
//    System.out.println("ACCOUNT NO to update IS : " + acctNo);
//
//    M_LIQ_Detail_Entity liqEntity = null;
//
//    if (acctNo != null && !acctNo.isEmpty()) {
//        liqEntity = findByAcctnumber(acctNo);
//    }
//
//    // Never send null to Thymeleaf
//    if (liqEntity == null) {
//        liqEntity = new M_LIQ_Detail_Entity();
//    }
//
//    if (liqEntity.getReport_date() != null) {
//
//        String formattedDate =
//                new SimpleDateFormat("dd/MM/yyyy")
//                        .format(liqEntity.getReport_date());
//
//        mv.addObject("asondate", formattedDate);
//    }
//
//    mv.addObject("Data", liqEntity);
//    mv.addObject("displaymode", "edit");
//    mv.addObject("formmode", formMode != null ? formMode : "edit");
//
//    return mv;
//}

public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {

    ModelAndView mv = new ModelAndView("BRRS/M_LIQ");

    System.out.println("SNO is : " + SNO);
    System.out.println("Type : " + type);

    if (SNO != null) {

        if (type == "RESUB" || type.equals("RESUB")) {

            System.out.println("Inside RESUB FETCH");

            M_LIQ_Detail_Entity liqEntity = findBysnoArch(SNO);

            if (liqEntity != null && liqEntity.getReport_date() != null) {

                String formattedDate =
                        new SimpleDateFormat("dd/MM/yyyy")
                                .format(liqEntity.getReport_date());

                mv.addObject("asondate", formattedDate);
            }

            mv.addObject("Data", liqEntity);

        } else {

            M_LIQ_Detail_Entity liqEntity = findBysno(SNO);

            if (liqEntity != null && liqEntity.getReport_date() != null) {

                String formattedDate =
                        new SimpleDateFormat("dd/MM/yyyy")
                                .format(liqEntity.getReport_date());

                mv.addObject("asondate", formattedDate);
            }

            mv.addObject("Data", liqEntity);
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

        String acctBalanceInpula = request.getParameter("acct_balance_in_pula");

        String acctName = request.getParameter("acct_name");

        String reportDateStr = request.getParameter("report_date");

        System.out.println("Sno is : " + Sno);

        String type = request.getParameter("type");

        String entry = (request.getParameter("entry") != null)
                ? request.getParameter("entry")
                : "YES";

        // Load Existing Record
        M_LIQ_Detail_Entity existing = null;

        System.out.println("Type is : " + type);

        if ("RESUB".equalsIgnoreCase(type)) {
            existing = findBysnoArch(Sno);
        } else {
            existing = findBysno(Sno);
        }

        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Record not found for update.");
        }

        // Keep old copy for audit
        M_LIQ_Detail_Entity oldcopy = new M_LIQ_Detail_Entity();
        BeanUtils.copyProperties(existing, oldcopy);

        boolean isChanged = false;

        // Update Account Name
        if (acctName != null && !acctName.isEmpty()) {

            if (existing.getAcct_name() == null
                    || !existing.getAcct_name().equals(acctName)) {

                existing.setAcct_name(acctName);

                isChanged = true;
            }
        }

        // Update Account Balance
        if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {

            BigDecimal newBalance = new BigDecimal(acctBalanceInpula);

            if (existing.getAcct_balance_in_pula() == null
                    || existing.getAcct_balance_in_pula().compareTo(newBalance) != 0) {

                existing.setAcct_balance_in_pula(newBalance);

                isChanged = true;
            }
        }

        // Save using JDBC
        if (isChanged) {

            String sql;

            System.out.println("Type in update block : " + type);

            if ("RESUB".equalsIgnoreCase(type)) {

                System.out.println("Inside RESUB UPDATE");

                sql = "UPDATE BRRS_M_LIQ_ARCHIVALTABLE_DETAIL "
                        + "SET ACCT_NAME = ?, "
                        + "ACCT_BALANCE_IN_PULA = ? "
                        + "WHERE SNO = ?";

            } else {

                System.out.println("Inside NORMAL UPDATE");

                sql = "UPDATE BRRS_M_LIQ_DETAILTABLE "
                        + "SET ACCT_NAME = ?, "
                        + "ACCT_BALANCE_IN_PULA = ? "
                        + "WHERE SNO = ?";
            }

            jdbcTemplate.update(
                    sql,
                    existing.getAcct_name(),
                    existing.getAcct_balance_in_pula(),
                    Sno);

            if ("RESUB".equalsIgnoreCase(type)) {

                auditService.compareEntitiesmanual(
                        oldcopy,
                        existing,
                        Sno,
                        "M_LIQ Archival Detail Screen",
                        "BRRS_M_LIQ_ARCHIVALTABLE_DETAIL");

            } else {

                auditService.compareEntitiesmanual(
                        oldcopy,
                        existing,
                        Sno,
                        "M_LIQ Detail Screen",
                        "BRRS_M_LIQ_DETAILTABLE");
            }

            System.out.println("Record updated using JDBC");

            Run_LIQ_Procudure(reportDateStr, type, entry);

            if ("RESUB".equalsIgnoreCase(type)
                    && "NO".equalsIgnoreCase(entry)) {

                return ResponseEntity.ok(
                        "Record updated and Report Regenerated successfully!");
            }

            return ResponseEntity.ok("Record updated successfully!");

        } else {

            return ResponseEntity.ok("No changes were made.");
        }

    } catch (Exception e) {

        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating record: " + e.getMessage());
    }
}
//public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
//
//    try {
//
//        String Sno = request.getParameter("sno");
//        String acctBalanceInpula = request.getParameter("acct_balance_in_pula");
//        String acctName = request.getParameter("acct_name");
//        String reportDateStr = request.getParameter("report_date");
//
//        System.out.println("SNO is : " + Sno);
//
//        String type = request.getParameter("type");
//        String entry = (request.getParameter("entry") != null)
//                ? request.getParameter("entry")
//                : "YES";
//
//        // ==========================================
//        // FETCH EXISTING RECORD
//        // ==========================================
//
//        M_LIQ_Detail_Entity existing = null;
//
//        System.out.println("Type is : " + type);
//
//        if ("RESUB".equalsIgnoreCase(type)) {
//
//            existing = findBysnoArch(Sno);
//
//        } else {
//
//            existing = findBysno(Sno);
//        }
//
//        if (existing == null) {
//
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Record not found for update.");
//        }
//
//        // ==========================================
//        // OLD COPY FOR AUDIT
//        // ==========================================
//
//        M_LIQ_Detail_Entity oldcopy = new M_LIQ_Detail_Entity();
//        BeanUtils.copyProperties(existing, oldcopy);
//
//        boolean isChanged = false;
//
//        // ==========================================
//        // UPDATE ACCOUNT NAME
//        // ==========================================
//
//        if (acctName != null && !acctName.isEmpty()) {
//
//            if (existing.getAcct_name() == null
//                    || !existing.getAcct_name().equals(acctName)) {
//
//                existing.setAcct_name(acctName);
//                isChanged = true;
//            }
//        }
//
//        // ==========================================
//        // UPDATE ACCOUNT BALANCE
//        // ==========================================
//
//        if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {
//
//            BigDecimal newBalance = new BigDecimal(acctBalanceInpula);
//
//            if (existing.getAcct_balance_in_pula() == null
//                    || existing.getAcct_balance_in_pula().compareTo(newBalance) != 0) {
//
//                existing.setAcct_balance_in_pula(newBalance);
//                isChanged = true;
//            }
//        }
//
//        // ==========================================
//        // UPDATE DATABASE
//        // ==========================================
//
//        if (isChanged) {
//
//            String sql;
//
//            System.out.println("Type in update block : " + type);
//
//            if ("RESUB".equalsIgnoreCase(type)) {
//
//                System.out.println("Inside RESUB UPDATE");
//
//                sql = "UPDATE BRRS_M_LIQ_ARCHIVALTABLE_DETAIL "
//                        + "SET ACCT_NAME = ?, "
//                        + "ACCT_BALANCE_IN_PULA = ? "
//                        + "WHERE SNO = ?";
//
//            } else {
//
//                System.out.println("Inside NORMAL UPDATE");
//
//                sql = "UPDATE BRRS_M_LIQ_DETAILTABLE "
//                        + "SET ACCT_NAME = ?, "
//                        + "ACCT_BALANCE_IN_PULA = ? "
//                        + "WHERE SNO = ?";
//            }
//
//            jdbcTemplate.update(
//                    sql,
//                    existing.getAcct_name(),
//                    existing.getAcct_balance_in_pula(),
//                    Sno);
//
//            if ("RESUB".equalsIgnoreCase(type)) {
//
//                auditService.compareEntitiesmanual(
//                        oldcopy,
//                        existing,
//                        Sno,
//                        "M_LIQ Archival Detail Screen",
//                        "BRRS_M_LIQ_ARCHIVALTABLE_DETAIL");
//
//            } else {
//
//                auditService.compareEntitiesmanual(
//                        oldcopy,
//                        existing,
//                        Sno,
//                        "M_LIQ Detail Screen",
//                        "BRRS_M_LIQ_DETAILTABLE");
//            }
//
//            System.out.println("Record updated using JDBC");
//
//            Run_LIQ_Procudure(reportDateStr, type, entry);
//
//            if ("RESUB".equalsIgnoreCase(type)
//                    && "NO".equalsIgnoreCase(entry)) {
//
//                return ResponseEntity.ok(
//                        "Record updated and Report Regenerated successfully!");
//            }
//
//            return ResponseEntity.ok("Record updated successfully!");
//
//        } else {
//
//            return ResponseEntity.ok("No changes were made.");
//        }
//
//    } catch (Exception e) {
//
//        e.printStackTrace();
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Error updating record: " + e.getMessage());
//    }
//}

@Transactional
public ResponseEntity<?> callregenprocedure(HttpServletRequest request) {
	try {
		Run_LIQ_Procudure(request.getParameter("reportDate"), request.getParameter("type"),
				request.getParameter("entry"));
		return ResponseEntity.ok("Resubmitted successfully!");
	} catch (Exception e) {

		e.printStackTrace();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Error updating record: " + e.getMessage());

	}
}

private void Run_LIQ_Procudure(String reportDateStr, String type, String entry) {

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
					String bdsql = "DELETE FROM BRRS_M_LIQ_DETAILTABLE WHERE REPORT_DATE = ?";
					int rowsDeleted = jdbcTemplate.update(bdsql, formattedDate);
					System.out.println("Successfully deleted before executing procedure " + rowsDeleted + " rows.");

					String sqltransfer = "INSERT INTO BRRS_M_LIQ_DETAILTABLE "
					        + "SELECT * "
					        + "FROM BRRS_M_LIQ_ARCHIVALTABLE_DETAIL "
					        + "WHERE REPORT_DATE = ?";
					int rowsInserted = jdbcTemplate.update(sqltransfer, formattedDate);
					System.out.println("Successfully transferred " + rowsInserted + " rows.");
				}

				if (shouldExecuteProcedure) {
					jdbcTemplate.update("BEGIN BRRS_M_LIQ_SUMMARY_PROCEDURE(?); END;", formattedDate);
					System.out.println("Procedure executed");
				}

				if (isResubNoEntry) {
					String adsql = "DELETE FROM BRRS_M_LIQ_DETAILTABLE WHERE REPORT_DATE = ?";
					int rowsDeleted = jdbcTemplate.update(adsql, formattedDate);
					System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

					// 1. Handle Archival Summary Table (System Generated)
					String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_LIQ_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
					Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, formattedDate);
					int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

					String finalsql = "INSERT INTO BRRS_M_LIQ_ARCHIVALTABLE_SUMMARY ("
					        + "R10_PRODUCT, R10_TOTAL, "
					        + "R11_PRODUCT, R11_TOTAL, "
					        + "R12_PRODUCT, R12_TOTAL, "
					        + "R13_PRODUCT, R13_TOTAL, "
					        + "R14_PRODUCT, R14_TOTAL, "
					        + "R15_PRODUCT, R15_TOTAL, "
//					        + "R16_PRODUCT, R16_TOTAL, "
					        + "R17_PRODUCT, R17_TOTAL, "
					        + "R18_PRODUCT, R18_TOTAL, "
//					        + "R19_PRODUCT, R19_TOTAL, "
//					        + "R20_PRODUCT, R20_TOTAL, "
					        + "R21_PRODUCT, R21_TOTAL, "
//					        + "R22_PRODUCT, R22_TOTAL, "
//					        + "R23_PRODUCT, R23_TOTAL, "
					        + "R24_PRODUCT, R24_TOTAL, "
					        + "R25_PRODUCT, R25_TOTAL, "
//					        + "R26_PRODUCT, R26_TOTAL, "
					        + "R27_PRODUCT, R27_TOTAL, "
					        + "R30_PRODUCT, R30_TOTAL, "
					        + "R31_PRODUCT, R31_TOTAL, "
					        + "R32_PRODUCT, R32_TOTAL, "
					        + "R34_PRODUCT, R34_TOTAL, "
					        + "R35_PRODUCT, R35_TOTAL, "
					        + "REPORT_DATE, "
					        + "REPORT_VERSION, "
					        + "REPORT_FREQUENCY, "
					        + "REPORT_CODE, "
					        + "REPORT_DESC, "
					        + "ENTITY_FLG, "
					        + "MODIFY_FLG, "
					        + "DEL_FLG, "
					        + "REPORT_RESUBDATE"
					        + ") "
					        + "SELECT "
					        + "R10_PRODUCT, R10_TOTAL, "
					        + "R11_PRODUCT, R11_TOTAL, "
					        + "R12_PRODUCT, R12_TOTAL, "
					        + "R13_PRODUCT, R13_TOTAL, "
					        + "R14_PRODUCT, R14_TOTAL, "
					        + "R15_PRODUCT, R15_TOTAL, "
//					        + "R16_PRODUCT, R16_TOTAL, "
					        + "R17_PRODUCT, R17_TOTAL, "
					        + "R18_PRODUCT, R18_TOTAL, "
//					        + "R19_PRODUCT, R19_TOTAL, "
//					        + "R20_PRODUCT, R20_TOTAL, "
					        + "R21_PRODUCT, R21_TOTAL, "
//					        + "R22_PRODUCT, R22_TOTAL, "
//					        + "R23_PRODUCT, R23_TOTAL, "
					        + "R24_PRODUCT, R24_TOTAL, "
					        + "R25_PRODUCT, R25_TOTAL, "
//					        + "R26_PRODUCT, R26_TOTAL, "
					        + "R27_PRODUCT, R27_TOTAL, "
					        + "R30_PRODUCT, R30_TOTAL, "
					        + "R31_PRODUCT, R31_TOTAL, "
					        + "R32_PRODUCT, R32_TOTAL, "
					        + "R34_PRODUCT, R34_TOTAL, "
					        + "R35_PRODUCT, R35_TOTAL, "
					        + "REPORT_DATE, "
					        + "?, "
					        + "REPORT_FREQUENCY, "
					        + "REPORT_CODE, "
					        + "REPORT_DESC, "
					        + "ENTITY_FLG, "
					        + "MODIFY_FLG, "
					        + "DEL_FLG, "
					        + "SYSDATE "
					        + "FROM BRRS_M_LIQ_SUMMARYTABLE "
					        + "WHERE REPORT_DATE = ?";

					int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, formattedDate);
					System.out.println("Successfully transferred system summary " + rowsInsertedSum + " rows.");

					// 2. Handle Manual Archival Summary Table (User Edited - Carry Forward updated
					// fields)
					String insManualSql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_LIQ_MANUAL_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
					Integer maxManualVersion = jdbcTemplate.queryForObject(insManualSql, Integer.class,
							formattedDate);

					// Calculate the new version number
					int manualVersion = (maxManualVersion != null ? maxManualVersion : 0) + 1;
					int manualRowsInserted = 0;

					if (maxManualVersion != null && maxManualVersion > 0) {
						// Fetch from PREVIOUS VERSION of the ARCHIVAL table itself to capture the
						// updates made screen-side
						String manualFinalSql = "INSERT INTO BRRS_M_LIQ_MANUAL_ARCHIVALTABLE_SUMMARY ("
						        + "R16_PRODUCT, "
						        + "R16_TOTAL, "
						        + "R19_PRODUCT, "
						        + "R19_TOTAL, "
						        + "R20_PRODUCT, "
						        + "R20_TOTAL, "
						        + "R22_PRODUCT, "
						        + "R22_TOTAL, "
						        + "R23_PRODUCT, "
						        + "R23_TOTAL, "
						        + "R26_PRODUCT, "
						        + "R26_TOTAL, "
						        + "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, "
						        + "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE"
						        + ") "
						        + "SELECT "
						        + "R16_PRODUCT, "
						        + "R16_TOTAL, "
						        + "R19_PRODUCT, "
						        + "R19_TOTAL, "
						        + "R20_PRODUCT, "
						        + "R20_TOTAL, "
						        + "R22_PRODUCT, "
						        + "R22_TOTAL, "
						        + "R23_PRODUCT, "
						        + "R23_TOTAL, "
						        + "R26_PRODUCT, "
						        + "R26_TOTAL, "
						        + "REPORT_DATE, ?, REPORT_FREQUENCY, "
						        + "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
						        + "FROM BRRS_M_LIQ_MANUAL_ARCHIVALTABLE_SUMMARY "
						        + "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

						manualRowsInserted = jdbcTemplate.update(manualFinalSql, manualVersion, formattedDate,
								maxManualVersion);
					} else {
						// Fallback option: If no previous version row exists in archival yet, fetch
						// from active summary table
						String manualFallbackSql = "INSERT INTO BRRS_M_LIQ_MANUAL_ARCHIVALTABLE_SUMMARY ("
								+ "R16_TOTAL,"
								+ "R19_TOTAL,"
								+ "R20_TOTAL,"
								+ "R22_TOTAL, "
								+ "R23_TOTAL,"
								+ "R26_TOTAL, "
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, "
								+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE"
								+ ") " + "SELECT " + "R16_TOTAL,"
								+ "R19_TOTAL,"
								+ "R20_TOTAL,"
								+ "R22_TOTAL, "
								+ "R23_TOTAL,"
								+ "R26_TOTAL, " + "REPORT_DATE, ?, REPORT_FREQUENCY, "
								+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
								+ "FROM BRRS_M_LIQ_MANUAL_SUMMARYTABLE WHERE REPORT_DATE = ?";

						manualRowsInserted = jdbcTemplate.update(manualFallbackSql, manualVersion, formattedDate);
					}

					System.out.println("Manual summary archived successfully into version (" + manualVersion + "): "
							+ manualRowsInserted + " rows.");

					String adsumsql = "DELETE FROM BRRS_M_LIQ_SUMMARYTABLE WHERE REPORT_DATE = ?";
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
// RESUB VIEW 
//=====================================================

//Resubmission
public List<Object[]> getM_LIQResub() {
	List<Object[]> resubList = new ArrayList<>();

	try {

		List<M_LIQ_Archival_Summary_Entity> repoData = getarchivaldatabydateListWithVersion();

		if (repoData != null && !repoData.isEmpty()) {
			for (M_LIQ_Archival_Summary_Entity entity : repoData) {
				Object[] row = new Object[] {
						entity.getReport_date(),
						entity.getReport_version(),
						entity.getReportResubDate()
				};
				resubList.add(row);
			}

			System.out.println("Fetched " + resubList.size() + " Resub records");
			M_LIQ_Archival_Summary_Entity first = repoData.get(0);
			System.out.println("Latest Resub version: " + first.getReport_version());
		} else {
			System.out.println("No Resub data found.");
		}

	} catch (Exception e) {
		System.err.println("Error fetching M_LIQ Resub data: " + e.getMessage());
		e.printStackTrace();
	}

	return resubList;
}

//=====================================================
// UPDATE RESUB 
//=====================================================



	
//=====================================================
// Summary EXCEL  FORMAT
//=====================================================

	public byte[] getM_LIQExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		
		// ARCHIVAL check
				if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null
						&& version.compareTo(BigDecimal.ZERO) >= 0) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
					return getExcelM_LIQARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,version);
				}
//		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
//			logger.info("Service: Generating ARCHIVAL report for version {}", version);
//			return getExcelM_LIQARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format, version);
//		}

		// Fetch data

		List<M_LIQ_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		List<M_LIQ_Manual_Summary_Entity> dataList1 = 
				getManualSummaryByDate(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LIQ report. Returning empty result.");
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

			 try {

			       // Row 6 = Excel row 7
			       Row dateRow = sheet.getRow(6);

			       if (dateRow == null) {
			           dateRow = sheet.createRow(6);
			       }

			       // Column 4 = Excel column D
			       Cell dateCell = dateRow.getCell(3);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(3);
			       }

			       // Date conversion
			       SimpleDateFormat inputFormat =
			               new SimpleDateFormat("dd-MMM-yyyy");

			       SimpleDateFormat outputFormat =
			               new SimpleDateFormat("dd/MM/yyyy");

			       Date reportDateValue =
			               inputFormat.parse(todate);

			       // Set formatted date
			       dateCell.setCellValue(
			               outputFormat.format(reportDateValue));

			       dateCell.setCellStyle(textStyle);

			   } catch (ParseException e) {

			       logger.error("Error parsing todate: {}", todate, e);
			   }
			   
			 
			 
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LIQ_Summary_Entity record = dataList.get(i);
					M_LIQ_Manual_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					row = sheet.getRow(11);

					// Column 2 - total
					Cell cellE = row.createCell(4);
					if (record.getR12_total() != null) {
						cellE.setCellValue(record.getR12_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR13_total() != null) {
						cellE.setCellValue(record.getR13_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR15_total() != null) {
						cellE.setCellValue(record.getR15_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR16_total() != null) {
						cellE.setCellValue(record1.getR16_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR17_total() != null) {
						cellE.setCellValue(record.getR17_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR18_total() != null) {
						cellE.setCellValue(record.getR18_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR19_total() != null) {
						cellE.setCellValue(record1.getR19_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR20_total() != null) {
						cellE.setCellValue(record1.getR20_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row21
					
					row = sheet.getRow(20);
					if (row == null) row = sheet.createRow(20);

					// Reuse existing cell if it exists, otherwise create new
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR21_total() != null) {
					    cellE.setCellValue(record.getR21_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave previous value if preferred
					}
					/*
					 * row = sheet.getRow(20);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR21_total() !=
					 * null) { cellE.setCellValue(record.getR21_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

					// row22
					row = sheet.getRow(21);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR22_total() != null) {
						cellE.setCellValue(record1.getR22_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR23_total() != null) {
						cellE.setCellValue(record1.getR23_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					
					
					
					// row25
					row = sheet.getRow(24);
					if (row == null) row = sheet.createRow(24);

					// Reuse existing cell if it exists, otherwise create new
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR25_total() != null) {
					    cellE.setCellValue(record.getR25_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave as is if you prefer
					}
					/*
					 * // row25 row = sheet.getRow(24);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR25_total() !=
					 * null) { cellE.setCellValue(record.getR25_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

					// row26
					row = sheet.getRow(25);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR26_total() != null) {
						cellE.setCellValue(record1.getR26_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column 1 - product name

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR30_total() != null) {
						cellE.setCellValue(record.getR30_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR31_total() != null) {
						cellE.setCellValue(record.getR31_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row32
					
					row = sheet.getRow(31);
					if (row == null) row = sheet.createRow(31);

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR32_total() != null) {
					    cellE.setCellValue(record.getR32_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave previous value
					}
					
					
					/*
					 * row = sheet.getRow(31);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR32_total() !=
					 * null) { cellE.setCellValue(record.getR32_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

					
					// row34
					row = sheet.getRow(33);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR34_total() != null) {
						cellE.setCellValue(record.getR34_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					
					// row35
					row = sheet.getRow(34);
					if (row == null) row = sheet.createRow(34);

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR35_total() != null) {
					    cellE.setCellValue(record.getR35_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave previous value
					}
					/*
					 * // row35 row = sheet.getRow(34);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR35_total() !=
					 * null) { cellE.setCellValue(record.getR35_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LIQ SUMMARY", null, "BRRS_M_LIQ_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}


//=====================================================
// Summary EXCEL  ARCHIVL
//=====================================================

	public byte[] getExcelM_LIQARCHIVAL(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {

		}

		// Fetch data

		List<M_LIQ_Archival_Summary_Entity> dataList = 
				getDataByDateListArchival(dateformat.parse(todate), version);
		List<M_LIQ_Archival_Manual_Summary_Entity> dataList1 = 
				getArchival_ManualdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LIQ report. Returning empty result.");
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

			 try {

			       // Row 6 = Excel row 7
			       Row dateRow = sheet.getRow(6);

			       if (dateRow == null) {
			           dateRow = sheet.createRow(6);
			       }

			       // Column 4 = Excel column D
			       Cell dateCell = dateRow.getCell(3);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(3);
			       }

			       // Date conversion
			       SimpleDateFormat inputFormat =
			               new SimpleDateFormat("dd-MMM-yyyy");

			       SimpleDateFormat outputFormat =
			               new SimpleDateFormat("dd/MM/yyyy");

			       Date reportDateValue =
			               inputFormat.parse(todate);

			       // Set formatted date
			       dateCell.setCellValue(
			               outputFormat.format(reportDateValue));

			       dateCell.setCellStyle(textStyle);

			   } catch (ParseException e) {

			       logger.error("Error parsing todate: {}", todate, e);
			   }
			 
			int startRow = 11;

			if (!dataList.isEmpty() && !dataList1.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LIQ_Archival_Summary_Entity record = dataList.get(i);
					M_LIQ_Archival_Manual_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					row = sheet.getRow(11);

					// Column 2 - total
					Cell cellE = row.createCell(4);
					if (record.getR12_total() != null) {
						cellE.setCellValue(record.getR12_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR13_total() != null) {
						cellE.setCellValue(record.getR13_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR15_total() != null) {
						cellE.setCellValue(record.getR15_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR16_total() != null) {
						cellE.setCellValue(record1.getR16_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR17_total() != null) {
						cellE.setCellValue(record.getR17_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR18_total() != null) {
						cellE.setCellValue(record.getR18_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR19_total() != null) {
						cellE.setCellValue(record1.getR19_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR20_total() != null) {
						cellE.setCellValue(record1.getR20_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row21
					
					row = sheet.getRow(20);
					if (row == null) row = sheet.createRow(20);

					// Reuse existing cell if it exists, otherwise create new
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR21_total() != null) {
					    cellE.setCellValue(record.getR21_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave previous value if preferred
					}
					/*
					 * row = sheet.getRow(20);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR21_total() !=
					 * null) { cellE.setCellValue(record.getR21_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

					// row22
					row = sheet.getRow(21);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR22_total() != null) {
						cellE.setCellValue(record1.getR22_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR23_total() != null) {
						cellE.setCellValue(record1.getR23_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					
					
					
					// row25
					row = sheet.getRow(24);
					if (row == null) row = sheet.createRow(24);

					// Reuse existing cell if it exists, otherwise create new
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR25_total() != null) {
					    cellE.setCellValue(record.getR25_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave as is if you prefer
					}
					/*
					 * // row25 row = sheet.getRow(24);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR25_total() !=
					 * null) { cellE.setCellValue(record.getR25_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

					// row26
					row = sheet.getRow(25);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR26_total() != null) {
						cellE.setCellValue(record1.getR26_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column 1 - product name

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR30_total() != null) {
						cellE.setCellValue(record.getR30_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR31_total() != null) {
						cellE.setCellValue(record.getR31_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row32
					
					row = sheet.getRow(31);
					if (row == null) row = sheet.createRow(31);

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR32_total() != null) {
					    cellE.setCellValue(record.getR32_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave previous value
					}
					
					
					/*
					 * row = sheet.getRow(31);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR32_total() !=
					 * null) { cellE.setCellValue(record.getR32_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

					
					// row34
					row = sheet.getRow(33);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR34_total() != null) {
						cellE.setCellValue(record.getR34_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					
					// row35
					row = sheet.getRow(34);
					if (row == null) row = sheet.createRow(34);

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR35_total() != null) {
					    cellE.setCellValue(record.getR35_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave previous value
					}
					/*
					 * // row35 row = sheet.getRow(34);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR35_total() !=
					 * null) { cellE.setCellValue(record.getR35_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LIQ ARCHIVAL SUMMARY", null, "BRRS_M_LIQ_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}
			




//=====================================================
// DETAL EXCEL  
//=====================================================

public byte[] getM_LIQDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_LIQ Details...");
			System.out.println("came to Detail download service");
			
			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {
				byte[] ARCHIVALreport = getM_LIQDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			}

//			if (type.equals("ARCHIVAL") & version != null) {
//				byte[] ARCHIVALreport = getM_LIQDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
//						version);
//				return ARCHIVALreport;
//			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LIQDetails");

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

			// ACCT BALANCE style 
	        CellStyle balanceStyle = workbook.createCellStyle();
	        balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	        balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
	        balanceStyle.setBorderTop(border);
	        balanceStyle.setBorderBottom(border);
	        balanceStyle.setBorderLeft(border);
	        balanceStyle.setBorderRight(border);

	
			// Header row
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			System.out.println("Parsed Date: " + parsedToDate);
			List<M_LIQ_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LIQ_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);

					

					row.createCell(4).setCellValue(item.getReport_label());
					row.createCell(5).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(6)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_LIQ — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LIQ Excel", e);
			return new byte[0];
		}
	}


//=====================================================
// DETAIL EXCEL  ARCHIVL
//=====================================================

	public byte[] getM_LIQDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for M_LIQ ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MLIQDetail");

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
			// ACCT BALANCE style (right aligned with 3 decimals)
	        CellStyle balanceStyle = workbook.createCellStyle();
	        balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	        balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
	        balanceStyle.setBorderTop(border);
	        balanceStyle.setBorderBottom(border);
	        balanceStyle.setBorderLeft(border);
	        balanceStyle.setBorderRight(border);



// Header row
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA",  "REPORT LABEL", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<M_LIQ_Archival_Detail_Entity> reportData = getArchival_DetaildatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LIQ_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					 row.createCell(2).setCellValue(item.getAcct_name()); 

// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					
					row.createCell(4).setCellValue(item.getReport_label());
					row.createCell(5).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(6)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");

// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_LIQ — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LIQExcel", e);
			return new byte[0];
		}
	}


}


