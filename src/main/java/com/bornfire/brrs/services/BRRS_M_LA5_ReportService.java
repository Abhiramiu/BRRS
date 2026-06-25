package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
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
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.IdClass;
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
import org.springframework.expression.ParseException;
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
@Transactional
public class BRRS_M_LA5_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LA5_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	// ENTITY MANAGER (Acts like Repository)
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	// Fetch data by report date
	public List<M_LA5_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_LA5_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_LA5RowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getM_LA5Archival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_M_LA5_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<M_LA5_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_LA5_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_LA5ArchivalRowMapper());
	}

	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_M_LA5_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}
//GET ALL WITH VERSION

	public List<M_LA5_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_M_LA5_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_LA5ArchivalRowMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_LA5_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<M_LA5_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_M_LA5_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new M_LA5DetailRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<M_LA5_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA5_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_LA5DetailRowMapper());
	}

// 3. PAGINATION

	public List<M_LA5_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_M_LA5_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new M_LA5DetailRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_M_LA5_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<M_LA5_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA5_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_LA5DetailRowMapper());
	}

	public M_LA5_Detail_Entity findBySno(String sno) {

		String sql = "SELECT * FROM BRRS_M_LA5_DETAILTABLE WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new M_LA5DetailRowMapper());
	}

	public M_LA5_Detail_Entity findBySnoArch(String sno) {

		String sql = "SELECT * FROM BRRS_M_LA5_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new M_LA5DetailRowMapper());
	}

// 1. GET BY DATE + VERSION

	public List<M_LA5_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA5_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ?  ";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_LA5ArchivalDetailRowMapper());
	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<M_LA5_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA5_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND DATA_ENTRY_VERSION = ? ";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_LA5ArchivalDetailRowMapper());
	}

	// ROW MAPPER

	class M_LA5RowMapper implements RowMapper<M_LA5_Summary_Entity> {

		@Override
		public M_LA5_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA5_Summary_Entity obj = new M_LA5_Summary_Entity();

// Row r6
			obj.setR6_product(rs.getString("r6_product"));
			obj.setR6_usd(rs.getBigDecimal("r6_usd"));
			obj.setR6_zar(rs.getBigDecimal("r6_zar"));
			obj.setR6_gbp(rs.getBigDecimal("r6_gbp"));
			obj.setR6_euro(rs.getBigDecimal("r6_euro"));
			obj.setR6_yen(rs.getBigDecimal("r6_yen"));
			obj.setR6_c6(rs.getBigDecimal("r6_c6"));
			obj.setR6_c7(rs.getBigDecimal("r6_c7"));
			obj.setR6_c8(rs.getBigDecimal("r6_c8"));
			obj.setR6_total(rs.getBigDecimal("r6_total"));

// Row r7
			obj.setR7_product(rs.getString("r7_product"));
			obj.setR7_usd(rs.getBigDecimal("r7_usd"));
			obj.setR7_zar(rs.getBigDecimal("r7_zar"));
			obj.setR7_gbp(rs.getBigDecimal("r7_gbp"));
			obj.setR7_euro(rs.getBigDecimal("r7_euro"));
			obj.setR7_yen(rs.getBigDecimal("r7_yen"));
			obj.setR7_c6(rs.getBigDecimal("r7_c6"));
			obj.setR7_c7(rs.getBigDecimal("r7_c7"));
			obj.setR7_c8(rs.getBigDecimal("r7_c8"));
			obj.setR7_total(rs.getBigDecimal("r7_total"));

// Row r8
			obj.setR8_product(rs.getString("r8_product"));
			obj.setR8_usd(rs.getBigDecimal("r8_usd"));
			obj.setR8_zar(rs.getBigDecimal("r8_zar"));
			obj.setR8_gbp(rs.getBigDecimal("r8_gbp"));
			obj.setR8_euro(rs.getBigDecimal("r8_euro"));
			obj.setR8_yen(rs.getBigDecimal("r8_yen"));
			obj.setR8_c6(rs.getBigDecimal("r8_c6"));
			obj.setR8_c7(rs.getBigDecimal("r8_c7"));
			obj.setR8_c8(rs.getBigDecimal("r8_c8"));
			obj.setR8_total(rs.getBigDecimal("r8_total"));

// Row r9
			obj.setR9_product(rs.getString("r9_product"));
			obj.setR9_usd(rs.getBigDecimal("r9_usd"));
			obj.setR9_zar(rs.getBigDecimal("r9_zar"));
			obj.setR9_gbp(rs.getBigDecimal("r9_gbp"));
			obj.setR9_euro(rs.getBigDecimal("r9_euro"));
			obj.setR9_yen(rs.getBigDecimal("r9_yen"));
			obj.setR9_c6(rs.getBigDecimal("r9_c6"));
			obj.setR9_c7(rs.getBigDecimal("r9_c7"));
			obj.setR9_c8(rs.getBigDecimal("r9_c8"));
			obj.setR9_total(rs.getBigDecimal("r9_total"));

// Row r10
			obj.setR10_product(rs.getString("r10_product"));
			obj.setR10_usd(rs.getBigDecimal("r10_usd"));
			obj.setR10_zar(rs.getBigDecimal("r10_zar"));
			obj.setR10_gbp(rs.getBigDecimal("r10_gbp"));
			obj.setR10_euro(rs.getBigDecimal("r10_euro"));
			obj.setR10_yen(rs.getBigDecimal("r10_yen"));
			obj.setR10_c6(rs.getBigDecimal("r10_c6"));
			obj.setR10_c7(rs.getBigDecimal("r10_c7"));
			obj.setR10_c8(rs.getBigDecimal("r10_c8"));
			obj.setR10_total(rs.getBigDecimal("r10_total"));

// Row r11
			obj.setR11_product(rs.getString("r11_product"));
			obj.setR11_usd(rs.getBigDecimal("r11_usd"));
			obj.setR11_zar(rs.getBigDecimal("r11_zar"));
			obj.setR11_gbp(rs.getBigDecimal("r11_gbp"));
			obj.setR11_euro(rs.getBigDecimal("r11_euro"));
			obj.setR11_yen(rs.getBigDecimal("r11_yen"));
			obj.setR11_c6(rs.getBigDecimal("r11_c6"));
			obj.setR11_c7(rs.getBigDecimal("r11_c7"));
			obj.setR11_c8(rs.getBigDecimal("r11_c8"));
			obj.setR11_total(rs.getBigDecimal("r11_total"));

// Row r12
			obj.setR12_product(rs.getString("r12_product"));
			obj.setR12_usd(rs.getBigDecimal("r12_usd"));
			obj.setR12_zar(rs.getBigDecimal("r12_zar"));
			obj.setR12_gbp(rs.getBigDecimal("r12_gbp"));
			obj.setR12_euro(rs.getBigDecimal("r12_euro"));
			obj.setR12_yen(rs.getBigDecimal("r12_yen"));
			obj.setR12_c6(rs.getBigDecimal("r12_c6"));
			obj.setR12_c7(rs.getBigDecimal("r12_c7"));
			obj.setR12_c8(rs.getBigDecimal("r12_c8"));
			obj.setR12_total(rs.getBigDecimal("r12_total"));

// Row r13
			obj.setR13_product(rs.getString("r13_product"));
			obj.setR13_usd(rs.getBigDecimal("r13_usd"));
			obj.setR13_zar(rs.getBigDecimal("r13_zar"));
			obj.setR13_gbp(rs.getBigDecimal("r13_gbp"));
			obj.setR13_euro(rs.getBigDecimal("r13_euro"));
			obj.setR13_yen(rs.getBigDecimal("r13_yen"));
			obj.setR13_c6(rs.getBigDecimal("r13_c6"));
			obj.setR13_c7(rs.getBigDecimal("r13_c7"));
			obj.setR13_c8(rs.getBigDecimal("r13_c8"));
			obj.setR13_total(rs.getBigDecimal("r13_total"));

// Row r14
			obj.setR14_product(rs.getString("r14_product"));
			obj.setR14_usd(rs.getBigDecimal("r14_usd"));
			obj.setR14_zar(rs.getBigDecimal("r14_zar"));
			obj.setR14_gbp(rs.getBigDecimal("r14_gbp"));
			obj.setR14_euro(rs.getBigDecimal("r14_euro"));
			obj.setR14_yen(rs.getBigDecimal("r14_yen"));
			obj.setR14_c6(rs.getBigDecimal("r14_c6"));
			obj.setR14_c7(rs.getBigDecimal("r14_c7"));
			obj.setR14_c8(rs.getBigDecimal("r14_c8"));
			obj.setR14_total(rs.getBigDecimal("r14_total"));

// Row r15
			obj.setR15_product(rs.getString("r15_product"));
			obj.setR15_usd(rs.getBigDecimal("r15_usd"));
			obj.setR15_zar(rs.getBigDecimal("r15_zar"));
			obj.setR15_gbp(rs.getBigDecimal("r15_gbp"));
			obj.setR15_euro(rs.getBigDecimal("r15_euro"));
			obj.setR15_yen(rs.getBigDecimal("r15_yen"));
			obj.setR15_c6(rs.getBigDecimal("r15_c6"));
			obj.setR15_c7(rs.getBigDecimal("r15_c7"));
			obj.setR15_c8(rs.getBigDecimal("r15_c8"));
			obj.setR15_total(rs.getBigDecimal("r15_total"));

// Row r16
			obj.setR16_product(rs.getString("r16_product"));
			obj.setR16_usd(rs.getBigDecimal("r16_usd"));
			obj.setR16_zar(rs.getBigDecimal("r16_zar"));
			obj.setR16_gbp(rs.getBigDecimal("r16_gbp"));
			obj.setR16_euro(rs.getBigDecimal("r16_euro"));
			obj.setR16_yen(rs.getBigDecimal("r16_yen"));
			obj.setR16_c6(rs.getBigDecimal("r16_c6"));
			obj.setR16_c7(rs.getBigDecimal("r16_c7"));
			obj.setR16_c8(rs.getBigDecimal("r16_c8"));
			obj.setR16_total(rs.getBigDecimal("r16_total"));

// Row r17
			obj.setR17_product(rs.getString("r17_product"));
			obj.setR17_usd(rs.getBigDecimal("r17_usd"));
			obj.setR17_zar(rs.getBigDecimal("r17_zar"));
			obj.setR17_gbp(rs.getBigDecimal("r17_gbp"));
			obj.setR17_euro(rs.getBigDecimal("r17_euro"));
			obj.setR17_yen(rs.getBigDecimal("r17_yen"));
			obj.setR17_c6(rs.getBigDecimal("r17_c6"));
			obj.setR17_c7(rs.getBigDecimal("r17_c7"));
			obj.setR17_c8(rs.getBigDecimal("r17_c8"));
			obj.setR17_total(rs.getBigDecimal("r17_total"));

// Row r18
			obj.setR18_product(rs.getString("r18_product"));
			obj.setR18_usd(rs.getBigDecimal("r18_usd"));
			obj.setR18_zar(rs.getBigDecimal("r18_zar"));
			obj.setR18_gbp(rs.getBigDecimal("r18_gbp"));
			obj.setR18_euro(rs.getBigDecimal("r18_euro"));
			obj.setR18_yen(rs.getBigDecimal("r18_yen"));
			obj.setR18_c6(rs.getBigDecimal("r18_c6"));
			obj.setR18_c7(rs.getBigDecimal("r18_c7"));
			obj.setR18_c8(rs.getBigDecimal("r18_c8"));
			obj.setR18_total(rs.getBigDecimal("r18_total"));

// Row r19
			obj.setR19_product(rs.getString("r19_product"));
			obj.setR19_usd(rs.getBigDecimal("r19_usd"));
			obj.setR19_zar(rs.getBigDecimal("r19_zar"));
			obj.setR19_gbp(rs.getBigDecimal("r19_gbp"));
			obj.setR19_euro(rs.getBigDecimal("r19_euro"));
			obj.setR19_yen(rs.getBigDecimal("r19_yen"));
			obj.setR19_c6(rs.getBigDecimal("r19_c6"));
			obj.setR19_c7(rs.getBigDecimal("r19_c7"));
			obj.setR19_c8(rs.getBigDecimal("r19_c8"));
			obj.setR19_total(rs.getBigDecimal("r19_total"));

// Row r20
			obj.setR20_product(rs.getString("r20_product"));
			obj.setR20_usd(rs.getBigDecimal("r20_usd"));
			obj.setR20_zar(rs.getBigDecimal("r20_zar"));
			obj.setR20_gbp(rs.getBigDecimal("r20_gbp"));
			obj.setR20_euro(rs.getBigDecimal("r20_euro"));
			obj.setR20_yen(rs.getBigDecimal("r20_yen"));
			obj.setR20_c6(rs.getBigDecimal("r20_c6"));
			obj.setR20_c7(rs.getBigDecimal("r20_c7"));
			obj.setR20_c8(rs.getBigDecimal("r20_c8"));
			obj.setR20_total(rs.getBigDecimal("r20_total"));

// Row r21
			obj.setR21_product(rs.getString("r21_product"));
			obj.setR21_usd(rs.getBigDecimal("r21_usd"));
			obj.setR21_zar(rs.getBigDecimal("r21_zar"));
			obj.setR21_gbp(rs.getBigDecimal("r21_gbp"));
			obj.setR21_euro(rs.getBigDecimal("r21_euro"));
			obj.setR21_yen(rs.getBigDecimal("r21_yen"));
			obj.setR21_c6(rs.getBigDecimal("r21_c6"));
			obj.setR21_c7(rs.getBigDecimal("r21_c7"));
			obj.setR21_c8(rs.getBigDecimal("r21_c8"));
			obj.setR21_total(rs.getBigDecimal("r21_total"));

// Row r22
			obj.setR22_product(rs.getString("r22_product"));
			obj.setR22_usd(rs.getBigDecimal("r22_usd"));
			obj.setR22_zar(rs.getBigDecimal("r22_zar"));
			obj.setR22_gbp(rs.getBigDecimal("r22_gbp"));
			obj.setR22_euro(rs.getBigDecimal("r22_euro"));
			obj.setR22_yen(rs.getBigDecimal("r22_yen"));
			obj.setR22_c6(rs.getBigDecimal("r22_c6"));
			obj.setR22_c7(rs.getBigDecimal("r22_c7"));
			obj.setR22_c8(rs.getBigDecimal("r22_c8"));
			obj.setR22_total(rs.getBigDecimal("r22_total"));

// Row r23
			obj.setR23_product(rs.getString("r23_product"));
			obj.setR23_usd(rs.getBigDecimal("r23_usd"));
			obj.setR23_zar(rs.getBigDecimal("r23_zar"));
			obj.setR23_gbp(rs.getBigDecimal("r23_gbp"));
			obj.setR23_euro(rs.getBigDecimal("r23_euro"));
			obj.setR23_yen(rs.getBigDecimal("r23_yen"));
			obj.setR23_c6(rs.getBigDecimal("r23_c6"));
			obj.setR23_c7(rs.getBigDecimal("r23_c7"));
			obj.setR23_c8(rs.getBigDecimal("r23_c8"));
			obj.setR23_total(rs.getBigDecimal("r23_total"));

// Row r24
			obj.setR24_product(rs.getString("r24_product"));
			obj.setR24_usd(rs.getBigDecimal("r24_usd"));
			obj.setR24_zar(rs.getBigDecimal("r24_zar"));
			obj.setR24_gbp(rs.getBigDecimal("r24_gbp"));
			obj.setR24_euro(rs.getBigDecimal("r24_euro"));
			obj.setR24_yen(rs.getBigDecimal("r24_yen"));
			obj.setR24_c6(rs.getBigDecimal("r24_c6"));
			obj.setR24_c7(rs.getBigDecimal("r24_c7"));
			obj.setR24_c8(rs.getBigDecimal("r24_c8"));
			obj.setR24_total(rs.getBigDecimal("r24_total"));

// Row r25
			obj.setR25_product(rs.getString("r25_product"));
			obj.setR25_usd(rs.getBigDecimal("r25_usd"));
			obj.setR25_zar(rs.getBigDecimal("r25_zar"));
			obj.setR25_gbp(rs.getBigDecimal("r25_gbp"));
			obj.setR25_euro(rs.getBigDecimal("r25_euro"));
			obj.setR25_yen(rs.getBigDecimal("r25_yen"));
			obj.setR25_c6(rs.getBigDecimal("r25_c6"));
			obj.setR25_c7(rs.getBigDecimal("r25_c7"));
			obj.setR25_c8(rs.getBigDecimal("r25_c8"));
			obj.setR25_total(rs.getBigDecimal("r25_total"));

// Row r26
			obj.setR26_product(rs.getString("r26_product"));
			obj.setR26_usd(rs.getBigDecimal("r26_usd"));
			obj.setR26_zar(rs.getBigDecimal("r26_zar"));
			obj.setR26_gbp(rs.getBigDecimal("r26_gbp"));
			obj.setR26_euro(rs.getBigDecimal("r26_euro"));
			obj.setR26_yen(rs.getBigDecimal("r26_yen"));
			obj.setR26_c6(rs.getBigDecimal("r26_c6"));
			obj.setR26_c7(rs.getBigDecimal("r26_c7"));
			obj.setR26_c8(rs.getBigDecimal("r26_c8"));
			obj.setR26_total(rs.getBigDecimal("r26_total"));

// Row r27
			obj.setR27_product(rs.getString("r27_product"));
			obj.setR27_usd(rs.getBigDecimal("r27_usd"));
			obj.setR27_zar(rs.getBigDecimal("r27_zar"));
			obj.setR27_gbp(rs.getBigDecimal("r27_gbp"));
			obj.setR27_euro(rs.getBigDecimal("r27_euro"));
			obj.setR27_yen(rs.getBigDecimal("r27_yen"));
			obj.setR27_c6(rs.getBigDecimal("r27_c6"));
			obj.setR27_c7(rs.getBigDecimal("r27_c7"));
			obj.setR27_c8(rs.getBigDecimal("r27_c8"));
			obj.setR27_total(rs.getBigDecimal("r27_total"));

// Row r28
			obj.setR28_product(rs.getString("r28_product"));
			obj.setR28_usd(rs.getBigDecimal("r28_usd"));
			obj.setR28_zar(rs.getBigDecimal("r28_zar"));
			obj.setR28_gbp(rs.getBigDecimal("r28_gbp"));
			obj.setR28_euro(rs.getBigDecimal("r28_euro"));
			obj.setR28_yen(rs.getBigDecimal("r28_yen"));
			obj.setR28_c6(rs.getBigDecimal("r28_c6"));
			obj.setR28_c7(rs.getBigDecimal("r28_c7"));
			obj.setR28_c8(rs.getBigDecimal("r28_c8"));
			obj.setR28_total(rs.getBigDecimal("r28_total"));

// Row r29
			obj.setR29_product(rs.getString("r29_product"));
			obj.setR29_usd(rs.getBigDecimal("r29_usd"));
			obj.setR29_zar(rs.getBigDecimal("r29_zar"));
			obj.setR29_gbp(rs.getBigDecimal("r29_gbp"));
			obj.setR29_euro(rs.getBigDecimal("r29_euro"));
			obj.setR29_yen(rs.getBigDecimal("r29_yen"));
			obj.setR29_c6(rs.getBigDecimal("r29_c6"));
			obj.setR29_c7(rs.getBigDecimal("r29_c7"));
			obj.setR29_c8(rs.getBigDecimal("r29_c8"));
			obj.setR29_total(rs.getBigDecimal("r29_total"));

// Row r30
			obj.setR30_product(rs.getString("r30_product"));
			obj.setR30_usd(rs.getBigDecimal("r30_usd"));
			obj.setR30_zar(rs.getBigDecimal("r30_zar"));
			obj.setR30_gbp(rs.getBigDecimal("r30_gbp"));
			obj.setR30_euro(rs.getBigDecimal("r30_euro"));
			obj.setR30_yen(rs.getBigDecimal("r30_yen"));
			obj.setR30_c6(rs.getBigDecimal("r30_c6"));
			obj.setR30_c7(rs.getBigDecimal("r30_c7"));
			obj.setR30_c8(rs.getBigDecimal("r30_c8"));
			obj.setR30_total(rs.getBigDecimal("r30_total"));

// Row r31
			obj.setR31_product(rs.getString("r31_product"));
			obj.setR31_usd(rs.getBigDecimal("r31_usd"));
			obj.setR31_zar(rs.getBigDecimal("r31_zar"));
			obj.setR31_gbp(rs.getBigDecimal("r31_gbp"));
			obj.setR31_euro(rs.getBigDecimal("r31_euro"));
			obj.setR31_yen(rs.getBigDecimal("r31_yen"));
			obj.setR31_c6(rs.getBigDecimal("r31_c6"));
			obj.setR31_c7(rs.getBigDecimal("r31_c7"));
			obj.setR31_c8(rs.getBigDecimal("r31_c8"));
			obj.setR31_total(rs.getBigDecimal("r31_total"));

// Row r32
			obj.setR32_product(rs.getString("r32_product"));
			obj.setR32_usd(rs.getBigDecimal("r32_usd"));
			obj.setR32_zar(rs.getBigDecimal("r32_zar"));
			obj.setR32_gbp(rs.getBigDecimal("r32_gbp"));
			obj.setR32_euro(rs.getBigDecimal("r32_euro"));
			obj.setR32_yen(rs.getBigDecimal("r32_yen"));
			obj.setR32_c6(rs.getBigDecimal("r32_c6"));
			obj.setR32_c7(rs.getBigDecimal("r32_c7"));
			obj.setR32_c8(rs.getBigDecimal("r32_c8"));
			obj.setR32_total(rs.getBigDecimal("r32_total"));

// Row r33
			obj.setR33_product(rs.getString("r33_product"));
			obj.setR33_usd(rs.getBigDecimal("r33_usd"));
			obj.setR33_zar(rs.getBigDecimal("r33_zar"));
			obj.setR33_gbp(rs.getBigDecimal("r33_gbp"));
			obj.setR33_euro(rs.getBigDecimal("r33_euro"));
			obj.setR33_yen(rs.getBigDecimal("r33_yen"));
			obj.setR33_c6(rs.getBigDecimal("r33_c6"));
			obj.setR33_c7(rs.getBigDecimal("r33_c7"));
			obj.setR33_c8(rs.getBigDecimal("r33_c8"));
			obj.setR33_total(rs.getBigDecimal("r33_total"));

// Row r34
			obj.setR34_product(rs.getString("r34_product"));
			obj.setR34_usd(rs.getBigDecimal("r34_usd"));
			obj.setR34_zar(rs.getBigDecimal("r34_zar"));
			obj.setR34_gbp(rs.getBigDecimal("r34_gbp"));
			obj.setR34_euro(rs.getBigDecimal("r34_euro"));
			obj.setR34_yen(rs.getBigDecimal("r34_yen"));
			obj.setR34_c6(rs.getBigDecimal("r34_c6"));
			obj.setR34_c7(rs.getBigDecimal("r34_c7"));
			obj.setR34_c8(rs.getBigDecimal("r34_c8"));
			obj.setR34_total(rs.getBigDecimal("r34_total"));

// Row r35
			obj.setR35_product(rs.getString("r35_product"));
			obj.setR35_usd(rs.getBigDecimal("r35_usd"));
			obj.setR35_zar(rs.getBigDecimal("r35_zar"));
			obj.setR35_gbp(rs.getBigDecimal("r35_gbp"));
			obj.setR35_euro(rs.getBigDecimal("r35_euro"));
			obj.setR35_yen(rs.getBigDecimal("r35_yen"));
			obj.setR35_c6(rs.getBigDecimal("r35_c6"));
			obj.setR35_c7(rs.getBigDecimal("r35_c7"));
			obj.setR35_c8(rs.getBigDecimal("r35_c8"));
			obj.setR35_total(rs.getBigDecimal("r35_total"));

// Row r36
			obj.setR36_product(rs.getString("r36_product"));
			obj.setR36_usd(rs.getBigDecimal("r36_usd"));
			obj.setR36_zar(rs.getBigDecimal("r36_zar"));
			obj.setR36_gbp(rs.getBigDecimal("r36_gbp"));
			obj.setR36_euro(rs.getBigDecimal("r36_euro"));
			obj.setR36_yen(rs.getBigDecimal("r36_yen"));
			obj.setR36_c6(rs.getBigDecimal("r36_c6"));
			obj.setR36_c7(rs.getBigDecimal("r36_c7"));
			obj.setR36_c8(rs.getBigDecimal("r36_c8"));
			obj.setR36_total(rs.getBigDecimal("r36_total"));

// Row r37
			obj.setR37_product(rs.getString("r37_product"));
			obj.setR37_usd(rs.getBigDecimal("r37_usd"));
			obj.setR37_zar(rs.getBigDecimal("r37_zar"));
			obj.setR37_gbp(rs.getBigDecimal("r37_gbp"));
			obj.setR37_euro(rs.getBigDecimal("r37_euro"));
			obj.setR37_yen(rs.getBigDecimal("r37_yen"));
			obj.setR37_c6(rs.getBigDecimal("r37_c6"));
			obj.setR37_c7(rs.getBigDecimal("r37_c7"));
			obj.setR37_c8(rs.getBigDecimal("r37_c8"));
			obj.setR37_total(rs.getBigDecimal("r37_total"));

// Row r38
			obj.setR38_product(rs.getString("r38_product"));
			obj.setR38_usd(rs.getBigDecimal("r38_usd"));
			obj.setR38_zar(rs.getBigDecimal("r38_zar"));
			obj.setR38_gbp(rs.getBigDecimal("r38_gbp"));
			obj.setR38_euro(rs.getBigDecimal("r38_euro"));
			obj.setR38_yen(rs.getBigDecimal("r38_yen"));
			obj.setR38_c6(rs.getBigDecimal("r38_c6"));
			obj.setR38_c7(rs.getBigDecimal("r38_c7"));
			obj.setR38_c8(rs.getBigDecimal("r38_c8"));
			obj.setR38_total(rs.getBigDecimal("r38_total"));

// Row r39
			obj.setR39_product(rs.getString("r39_product"));
			obj.setR39_usd(rs.getBigDecimal("r39_usd"));
			obj.setR39_zar(rs.getBigDecimal("r39_zar"));
			obj.setR39_gbp(rs.getBigDecimal("r39_gbp"));
			obj.setR39_euro(rs.getBigDecimal("r39_euro"));
			obj.setR39_yen(rs.getBigDecimal("r39_yen"));
			obj.setR39_c6(rs.getBigDecimal("r39_c6"));
			obj.setR39_c7(rs.getBigDecimal("r39_c7"));
			obj.setR39_c8(rs.getBigDecimal("r39_c8"));
			obj.setR39_total(rs.getBigDecimal("r39_total"));

// Row r40
			obj.setR40_product(rs.getString("r40_product"));
			obj.setR40_usd(rs.getBigDecimal("r40_usd"));
			obj.setR40_zar(rs.getBigDecimal("r40_zar"));
			obj.setR40_gbp(rs.getBigDecimal("r40_gbp"));
			obj.setR40_euro(rs.getBigDecimal("r40_euro"));
			obj.setR40_yen(rs.getBigDecimal("r40_yen"));
			obj.setR40_c6(rs.getBigDecimal("r40_c6"));
			obj.setR40_c7(rs.getBigDecimal("r40_c7"));
			obj.setR40_c8(rs.getBigDecimal("r40_c8"));
			obj.setR40_total(rs.getBigDecimal("r40_total"));

// Row r41
			obj.setR41_product(rs.getString("r41_product"));
			obj.setR41_usd(rs.getBigDecimal("r41_usd"));
			obj.setR41_zar(rs.getBigDecimal("r41_zar"));
			obj.setR41_gbp(rs.getBigDecimal("r41_gbp"));
			obj.setR41_euro(rs.getBigDecimal("r41_euro"));
			obj.setR41_yen(rs.getBigDecimal("r41_yen"));
			obj.setR41_c6(rs.getBigDecimal("r41_c6"));
			obj.setR41_c7(rs.getBigDecimal("r41_c7"));
			obj.setR41_c8(rs.getBigDecimal("r41_c8"));
			obj.setR41_total(rs.getBigDecimal("r41_total"));

// Row r42
			obj.setR42_product(rs.getString("r42_product"));
			obj.setR42_usd(rs.getBigDecimal("r42_usd"));
			obj.setR42_zar(rs.getBigDecimal("r42_zar"));
			obj.setR42_gbp(rs.getBigDecimal("r42_gbp"));
			obj.setR42_euro(rs.getBigDecimal("r42_euro"));
			obj.setR42_yen(rs.getBigDecimal("r42_yen"));
			obj.setR42_c6(rs.getBigDecimal("r42_c6"));
			obj.setR42_c7(rs.getBigDecimal("r42_c7"));
			obj.setR42_c8(rs.getBigDecimal("r42_c8"));
			obj.setR42_total(rs.getBigDecimal("r42_total"));

// Row r43
			obj.setR43_product(rs.getString("r43_product"));
			obj.setR43_usd(rs.getBigDecimal("r43_usd"));
			obj.setR43_zar(rs.getBigDecimal("r43_zar"));
			obj.setR43_gbp(rs.getBigDecimal("r43_gbp"));
			obj.setR43_euro(rs.getBigDecimal("r43_euro"));
			obj.setR43_yen(rs.getBigDecimal("r43_yen"));
			obj.setR43_c6(rs.getBigDecimal("r43_c6"));
			obj.setR43_c7(rs.getBigDecimal("r43_c7"));
			obj.setR43_c8(rs.getBigDecimal("r43_c8"));
			obj.setR43_total(rs.getBigDecimal("r43_total"));

// Row r44
			obj.setR44_product(rs.getString("r44_product"));
			obj.setR44_usd(rs.getBigDecimal("r44_usd"));
			obj.setR44_zar(rs.getBigDecimal("r44_zar"));
			obj.setR44_gbp(rs.getBigDecimal("r44_gbp"));
			obj.setR44_euro(rs.getBigDecimal("r44_euro"));
			obj.setR44_yen(rs.getBigDecimal("r44_yen"));
			obj.setR44_c6(rs.getBigDecimal("r44_c6"));
			obj.setR44_c7(rs.getBigDecimal("r44_c7"));
			obj.setR44_c8(rs.getBigDecimal("r44_c8"));
			obj.setR44_total(rs.getBigDecimal("r44_total"));

// Row r45
			obj.setR45_product(rs.getString("r45_product"));
			obj.setR45_usd(rs.getBigDecimal("r45_usd"));
			obj.setR45_zar(rs.getBigDecimal("r45_zar"));
			obj.setR45_gbp(rs.getBigDecimal("r45_gbp"));
			obj.setR45_euro(rs.getBigDecimal("r45_euro"));
			obj.setR45_yen(rs.getBigDecimal("r45_yen"));
			obj.setR45_c6(rs.getBigDecimal("r45_c6"));
			obj.setR45_c7(rs.getBigDecimal("r45_c7"));
			obj.setR45_c8(rs.getBigDecimal("r45_c8"));
			obj.setR45_total(rs.getBigDecimal("r45_total"));

// Row r46
			obj.setR46_product(rs.getString("r46_product"));
			obj.setR46_usd(rs.getBigDecimal("r46_usd"));
			obj.setR46_zar(rs.getBigDecimal("r46_zar"));
			obj.setR46_gbp(rs.getBigDecimal("r46_gbp"));
			obj.setR46_euro(rs.getBigDecimal("r46_euro"));
			obj.setR46_yen(rs.getBigDecimal("r46_yen"));
			obj.setR46_c6(rs.getBigDecimal("r46_c6"));
			obj.setR46_c7(rs.getBigDecimal("r46_c7"));
			obj.setR46_c8(rs.getBigDecimal("r46_c8"));
			obj.setR46_total(rs.getBigDecimal("r46_total"));

// Row r47
			obj.setR47_product(rs.getString("r47_product"));
			obj.setR47_usd(rs.getBigDecimal("r47_usd"));
			obj.setR47_zar(rs.getBigDecimal("r47_zar"));
			obj.setR47_gbp(rs.getBigDecimal("r47_gbp"));
			obj.setR47_euro(rs.getBigDecimal("r47_euro"));
			obj.setR47_yen(rs.getBigDecimal("r47_yen"));
			obj.setR47_c6(rs.getBigDecimal("r47_c6"));
			obj.setR47_c7(rs.getBigDecimal("r47_c7"));
			obj.setR47_c8(rs.getBigDecimal("r47_c8"));
			obj.setR47_total(rs.getBigDecimal("r47_total"));

// Row r48
			obj.setR48_product(rs.getString("r48_product"));
			obj.setR48_usd(rs.getBigDecimal("r48_usd"));
			obj.setR48_zar(rs.getBigDecimal("r48_zar"));
			obj.setR48_gbp(rs.getBigDecimal("r48_gbp"));
			obj.setR48_euro(rs.getBigDecimal("r48_euro"));
			obj.setR48_yen(rs.getBigDecimal("r48_yen"));
			obj.setR48_c6(rs.getBigDecimal("r48_c6"));
			obj.setR48_c7(rs.getBigDecimal("r48_c7"));
			obj.setR48_c8(rs.getBigDecimal("r48_c8"));
			obj.setR48_total(rs.getBigDecimal("r48_total"));

// Row r49
			obj.setR49_product(rs.getString("r49_product"));
			obj.setR49_usd(rs.getBigDecimal("r49_usd"));
			obj.setR49_zar(rs.getBigDecimal("r49_zar"));
			obj.setR49_gbp(rs.getBigDecimal("r49_gbp"));
			obj.setR49_euro(rs.getBigDecimal("r49_euro"));
			obj.setR49_yen(rs.getBigDecimal("r49_yen"));
			obj.setR49_c6(rs.getBigDecimal("r49_c6"));
			obj.setR49_c7(rs.getBigDecimal("r49_c7"));
			obj.setR49_c8(rs.getBigDecimal("r49_c8"));
			obj.setR49_total(rs.getBigDecimal("r49_total"));

// Row r50
			obj.setR50_product(rs.getString("r50_product"));
			obj.setR50_usd(rs.getBigDecimal("r50_usd"));
			obj.setR50_zar(rs.getBigDecimal("r50_zar"));
			obj.setR50_gbp(rs.getBigDecimal("r50_gbp"));
			obj.setR50_euro(rs.getBigDecimal("r50_euro"));
			obj.setR50_yen(rs.getBigDecimal("r50_yen"));
			obj.setR50_c6(rs.getBigDecimal("r50_c6"));
			obj.setR50_c7(rs.getBigDecimal("r50_c7"));
			obj.setR50_c8(rs.getBigDecimal("r50_c8"));
			obj.setR50_total(rs.getBigDecimal("r50_total"));

// Row r51
			obj.setR51_product(rs.getString("r51_product"));
			obj.setR51_usd(rs.getBigDecimal("r51_usd"));
			obj.setR51_zar(rs.getBigDecimal("r51_zar"));
			obj.setR51_gbp(rs.getBigDecimal("r51_gbp"));
			obj.setR51_euro(rs.getBigDecimal("r51_euro"));
			obj.setR51_yen(rs.getBigDecimal("r51_yen"));
			obj.setR51_c6(rs.getBigDecimal("r51_c6"));
			obj.setR51_c7(rs.getBigDecimal("r51_c7"));
			obj.setR51_c8(rs.getBigDecimal("r51_c8"));
			obj.setR51_total(rs.getBigDecimal("r51_total"));

// Row r52
			obj.setR52_product(rs.getString("r52_product"));
			obj.setR52_usd(rs.getBigDecimal("r52_usd"));
			obj.setR52_zar(rs.getBigDecimal("r52_zar"));
			obj.setR52_gbp(rs.getBigDecimal("r52_gbp"));
			obj.setR52_euro(rs.getBigDecimal("r52_euro"));
			obj.setR52_yen(rs.getBigDecimal("r52_yen"));
			obj.setR52_c6(rs.getBigDecimal("r52_c6"));
			obj.setR52_c7(rs.getBigDecimal("r52_c7"));
			obj.setR52_c8(rs.getBigDecimal("r52_c8"));
			obj.setR52_total(rs.getBigDecimal("r52_total"));

// Row r53
			obj.setR53_product(rs.getString("r53_product"));
			obj.setR53_usd(rs.getBigDecimal("r53_usd"));
			obj.setR53_zar(rs.getBigDecimal("r53_zar"));
			obj.setR53_gbp(rs.getBigDecimal("r53_gbp"));
			obj.setR53_euro(rs.getBigDecimal("r53_euro"));
			obj.setR53_yen(rs.getBigDecimal("r53_yen"));
			obj.setR53_c6(rs.getBigDecimal("r53_c6"));
			obj.setR53_c7(rs.getBigDecimal("r53_c7"));
			obj.setR53_c8(rs.getBigDecimal("r53_c8"));
			obj.setR53_total(rs.getBigDecimal("r53_total"));

// Row r54
			obj.setR54_product(rs.getString("r54_product"));
			obj.setR54_usd(rs.getBigDecimal("r54_usd"));
			obj.setR54_zar(rs.getBigDecimal("r54_zar"));
			obj.setR54_gbp(rs.getBigDecimal("r54_gbp"));
			obj.setR54_euro(rs.getBigDecimal("r54_euro"));
			obj.setR54_yen(rs.getBigDecimal("r54_yen"));
			obj.setR54_c6(rs.getBigDecimal("r54_c6"));
			obj.setR54_c7(rs.getBigDecimal("r54_c7"));
			obj.setR54_c8(rs.getBigDecimal("r54_c8"));
			obj.setR54_total(rs.getBigDecimal("r54_total"));

// Row r55
			obj.setR55_product(rs.getString("r55_product"));
			obj.setR55_usd(rs.getBigDecimal("r55_usd"));
			obj.setR55_zar(rs.getBigDecimal("r55_zar"));
			obj.setR55_gbp(rs.getBigDecimal("r55_gbp"));
			obj.setR55_euro(rs.getBigDecimal("r55_euro"));
			obj.setR55_yen(rs.getBigDecimal("r55_yen"));
			obj.setR55_c6(rs.getBigDecimal("r55_c6"));
			obj.setR55_c7(rs.getBigDecimal("r55_c7"));
			obj.setR55_c8(rs.getBigDecimal("r55_c8"));
			obj.setR55_total(rs.getBigDecimal("r55_total"));

// Row r56
			obj.setR56_product(rs.getString("r56_product"));
			obj.setR56_usd(rs.getBigDecimal("r56_usd"));
			obj.setR56_zar(rs.getBigDecimal("r56_zar"));
			obj.setR56_gbp(rs.getBigDecimal("r56_gbp"));
			obj.setR56_euro(rs.getBigDecimal("r56_euro"));
			obj.setR56_yen(rs.getBigDecimal("r56_yen"));
			obj.setR56_c6(rs.getBigDecimal("r56_c6"));
			obj.setR56_c7(rs.getBigDecimal("r56_c7"));
			obj.setR56_c8(rs.getBigDecimal("r56_c8"));
			obj.setR56_total(rs.getBigDecimal("r56_total"));

// Row r57
			obj.setR57_product(rs.getString("r57_product"));
			obj.setR57_usd(rs.getBigDecimal("r57_usd"));
			obj.setR57_zar(rs.getBigDecimal("r57_zar"));
			obj.setR57_gbp(rs.getBigDecimal("r57_gbp"));
			obj.setR57_euro(rs.getBigDecimal("r57_euro"));
			obj.setR57_yen(rs.getBigDecimal("r57_yen"));
			obj.setR57_c6(rs.getBigDecimal("r57_c6"));
			obj.setR57_c7(rs.getBigDecimal("r57_c7"));
			obj.setR57_c8(rs.getBigDecimal("r57_c8"));
			obj.setR57_total(rs.getBigDecimal("r57_total"));

// Row r58
			obj.setR58_product(rs.getString("r58_product"));
			obj.setR58_usd(rs.getBigDecimal("r58_usd"));
			obj.setR58_zar(rs.getBigDecimal("r58_zar"));
			obj.setR58_gbp(rs.getBigDecimal("r58_gbp"));
			obj.setR58_euro(rs.getBigDecimal("r58_euro"));
			obj.setR58_yen(rs.getBigDecimal("r58_yen"));
			obj.setR58_c6(rs.getBigDecimal("r58_c6"));
			obj.setR58_c7(rs.getBigDecimal("r58_c7"));
			obj.setR58_c8(rs.getBigDecimal("r58_c8"));
			obj.setR58_total(rs.getBigDecimal("r58_total"));

// Row r59
			obj.setR59_product(rs.getString("r59_product"));
			obj.setR59_usd(rs.getBigDecimal("r59_usd"));
			obj.setR59_zar(rs.getBigDecimal("r59_zar"));
			obj.setR59_gbp(rs.getBigDecimal("r59_gbp"));
			obj.setR59_euro(rs.getBigDecimal("r59_euro"));
			obj.setR59_yen(rs.getBigDecimal("r59_yen"));
			obj.setR59_c6(rs.getBigDecimal("r59_c6"));
			obj.setR59_c7(rs.getBigDecimal("r59_c7"));
			obj.setR59_c8(rs.getBigDecimal("r59_c8"));
			obj.setR59_total(rs.getBigDecimal("r59_total"));

// Row r60
			obj.setR60_product(rs.getString("r60_product"));
			obj.setR60_usd(rs.getBigDecimal("r60_usd"));
			obj.setR60_zar(rs.getBigDecimal("r60_zar"));
			obj.setR60_gbp(rs.getBigDecimal("r60_gbp"));
			obj.setR60_euro(rs.getBigDecimal("r60_euro"));
			obj.setR60_yen(rs.getBigDecimal("r60_yen"));
			obj.setR60_c6(rs.getBigDecimal("r60_c6"));
			obj.setR60_c7(rs.getBigDecimal("r60_c7"));
			obj.setR60_c8(rs.getBigDecimal("r60_c8"));
			obj.setR60_total(rs.getBigDecimal("r60_total"));

// Row r61
			obj.setR61_product(rs.getString("r61_product"));
			obj.setR61_usd(rs.getBigDecimal("r61_usd"));
			obj.setR61_zar(rs.getBigDecimal("r61_zar"));
			obj.setR61_gbp(rs.getBigDecimal("r61_gbp"));
			obj.setR61_euro(rs.getBigDecimal("r61_euro"));
			obj.setR61_yen(rs.getBigDecimal("r61_yen"));
			obj.setR61_c6(rs.getBigDecimal("r61_c6"));
			obj.setR61_c7(rs.getBigDecimal("r61_c7"));
			obj.setR61_c8(rs.getBigDecimal("r61_c8"));
			obj.setR61_total(rs.getBigDecimal("r61_total"));

// Row r62
			obj.setR62_product(rs.getString("r62_product"));
			obj.setR62_usd(rs.getBigDecimal("r62_usd"));
			obj.setR62_zar(rs.getBigDecimal("r62_zar"));
			obj.setR62_gbp(rs.getBigDecimal("r62_gbp"));
			obj.setR62_euro(rs.getBigDecimal("r62_euro"));
			obj.setR62_yen(rs.getBigDecimal("r62_yen"));
			obj.setR62_c6(rs.getBigDecimal("r62_c6"));
			obj.setR62_c7(rs.getBigDecimal("r62_c7"));
			obj.setR62_c8(rs.getBigDecimal("r62_c8"));
			obj.setR62_total(rs.getBigDecimal("r62_total"));

			// COMMON FIELDS
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	public static class M_LA5_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		// Row r6
		private String r6_product;
		private BigDecimal r6_usd;
		private BigDecimal r6_zar;
		private BigDecimal r6_gbp;
		private BigDecimal r6_euro;
		private BigDecimal r6_yen;
		private BigDecimal r6_c6;
		private BigDecimal r6_c7;
		private BigDecimal r6_c8;
		private BigDecimal r6_total;

		// Row r7
		private String r7_product;
		private BigDecimal r7_usd;
		private BigDecimal r7_zar;
		private BigDecimal r7_gbp;
		private BigDecimal r7_euro;
		private BigDecimal r7_yen;
		private BigDecimal r7_c6;
		private BigDecimal r7_c7;
		private BigDecimal r7_c8;
		private BigDecimal r7_total;

		// Row r8
		private String r8_product;
		private BigDecimal r8_usd;
		private BigDecimal r8_zar;
		private BigDecimal r8_gbp;
		private BigDecimal r8_euro;
		private BigDecimal r8_yen;
		private BigDecimal r8_c6;
		private BigDecimal r8_c7;
		private BigDecimal r8_c8;
		private BigDecimal r8_total;

		// Row r9
		private String r9_product;
		private BigDecimal r9_usd;
		private BigDecimal r9_zar;
		private BigDecimal r9_gbp;
		private BigDecimal r9_euro;
		private BigDecimal r9_yen;
		private BigDecimal r9_c6;
		private BigDecimal r9_c7;
		private BigDecimal r9_c8;
		private BigDecimal r9_total;

		// Row r10
		private String r10_product;
		private BigDecimal r10_usd;
		private BigDecimal r10_zar;
		private BigDecimal r10_gbp;
		private BigDecimal r10_euro;
		private BigDecimal r10_yen;
		private BigDecimal r10_c6;
		private BigDecimal r10_c7;
		private BigDecimal r10_c8;
		private BigDecimal r10_total;

		// Row r11
		private String r11_product;
		private BigDecimal r11_usd;
		private BigDecimal r11_zar;
		private BigDecimal r11_gbp;
		private BigDecimal r11_euro;
		private BigDecimal r11_yen;
		private BigDecimal r11_c6;
		private BigDecimal r11_c7;
		private BigDecimal r11_c8;
		private BigDecimal r11_total;

		// Row r12
		private String r12_product;
		private BigDecimal r12_usd;
		private BigDecimal r12_zar;
		private BigDecimal r12_gbp;
		private BigDecimal r12_euro;
		private BigDecimal r12_yen;
		private BigDecimal r12_c6;
		private BigDecimal r12_c7;
		private BigDecimal r12_c8;
		private BigDecimal r12_total;

		// Row r13
		private String r13_product;
		private BigDecimal r13_usd;
		private BigDecimal r13_zar;
		private BigDecimal r13_gbp;
		private BigDecimal r13_euro;
		private BigDecimal r13_yen;
		private BigDecimal r13_c6;
		private BigDecimal r13_c7;
		private BigDecimal r13_c8;
		private BigDecimal r13_total;

		// Row r14
		private String r14_product;
		private BigDecimal r14_usd;
		private BigDecimal r14_zar;
		private BigDecimal r14_gbp;
		private BigDecimal r14_euro;
		private BigDecimal r14_yen;
		private BigDecimal r14_c6;
		private BigDecimal r14_c7;
		private BigDecimal r14_c8;
		private BigDecimal r14_total;

		// Row r15
		private String r15_product;
		private BigDecimal r15_usd;
		private BigDecimal r15_zar;
		private BigDecimal r15_gbp;
		private BigDecimal r15_euro;
		private BigDecimal r15_yen;
		private BigDecimal r15_c6;
		private BigDecimal r15_c7;
		private BigDecimal r15_c8;
		private BigDecimal r15_total;

		// Row r16
		private String r16_product;
		private BigDecimal r16_usd;
		private BigDecimal r16_zar;
		private BigDecimal r16_gbp;
		private BigDecimal r16_euro;
		private BigDecimal r16_yen;
		private BigDecimal r16_c6;
		private BigDecimal r16_c7;
		private BigDecimal r16_c8;
		private BigDecimal r16_total;

		// Row r17
		private String r17_product;
		private BigDecimal r17_usd;
		private BigDecimal r17_zar;
		private BigDecimal r17_gbp;
		private BigDecimal r17_euro;
		private BigDecimal r17_yen;
		private BigDecimal r17_c6;
		private BigDecimal r17_c7;
		private BigDecimal r17_c8;
		private BigDecimal r17_total;

		// Row r18
		private String r18_product;
		private BigDecimal r18_usd;
		private BigDecimal r18_zar;
		private BigDecimal r18_gbp;
		private BigDecimal r18_euro;
		private BigDecimal r18_yen;
		private BigDecimal r18_c6;
		private BigDecimal r18_c7;
		private BigDecimal r18_c8;
		private BigDecimal r18_total;

		// Row r19
		private String r19_product;
		private BigDecimal r19_usd;
		private BigDecimal r19_zar;
		private BigDecimal r19_gbp;
		private BigDecimal r19_euro;
		private BigDecimal r19_yen;
		private BigDecimal r19_c6;
		private BigDecimal r19_c7;
		private BigDecimal r19_c8;
		private BigDecimal r19_total;

		// Row r20
		private String r20_product;
		private BigDecimal r20_usd;
		private BigDecimal r20_zar;
		private BigDecimal r20_gbp;
		private BigDecimal r20_euro;
		private BigDecimal r20_yen;
		private BigDecimal r20_c6;
		private BigDecimal r20_c7;
		private BigDecimal r20_c8;
		private BigDecimal r20_total;

		// Row r21
		private String r21_product;
		private BigDecimal r21_usd;
		private BigDecimal r21_zar;
		private BigDecimal r21_gbp;
		private BigDecimal r21_euro;
		private BigDecimal r21_yen;
		private BigDecimal r21_c6;
		private BigDecimal r21_c7;
		private BigDecimal r21_c8;
		private BigDecimal r21_total;

		// Row r22
		private String r22_product;
		private BigDecimal r22_usd;
		private BigDecimal r22_zar;
		private BigDecimal r22_gbp;
		private BigDecimal r22_euro;
		private BigDecimal r22_yen;
		private BigDecimal r22_c6;
		private BigDecimal r22_c7;
		private BigDecimal r22_c8;
		private BigDecimal r22_total;

		// Row r23
		private String r23_product;
		private BigDecimal r23_usd;
		private BigDecimal r23_zar;
		private BigDecimal r23_gbp;
		private BigDecimal r23_euro;
		private BigDecimal r23_yen;
		private BigDecimal r23_c6;
		private BigDecimal r23_c7;
		private BigDecimal r23_c8;
		private BigDecimal r23_total;

		// Row r24
		private String r24_product;
		private BigDecimal r24_usd;
		private BigDecimal r24_zar;
		private BigDecimal r24_gbp;
		private BigDecimal r24_euro;
		private BigDecimal r24_yen;
		private BigDecimal r24_c6;
		private BigDecimal r24_c7;
		private BigDecimal r24_c8;
		private BigDecimal r24_total;

		// Row r25
		private String r25_product;
		private BigDecimal r25_usd;
		private BigDecimal r25_zar;
		private BigDecimal r25_gbp;
		private BigDecimal r25_euro;
		private BigDecimal r25_yen;
		private BigDecimal r25_c6;
		private BigDecimal r25_c7;
		private BigDecimal r25_c8;
		private BigDecimal r25_total;

		// Row r26
		private String r26_product;
		private BigDecimal r26_usd;
		private BigDecimal r26_zar;
		private BigDecimal r26_gbp;
		private BigDecimal r26_euro;
		private BigDecimal r26_yen;
		private BigDecimal r26_c6;
		private BigDecimal r26_c7;
		private BigDecimal r26_c8;
		private BigDecimal r26_total;

		// Row r27
		private String r27_product;
		private BigDecimal r27_usd;
		private BigDecimal r27_zar;
		private BigDecimal r27_gbp;
		private BigDecimal r27_euro;
		private BigDecimal r27_yen;
		private BigDecimal r27_c6;
		private BigDecimal r27_c7;
		private BigDecimal r27_c8;
		private BigDecimal r27_total;

		// Row r28
		private String r28_product;
		private BigDecimal r28_usd;
		private BigDecimal r28_zar;
		private BigDecimal r28_gbp;
		private BigDecimal r28_euro;
		private BigDecimal r28_yen;
		private BigDecimal r28_c6;
		private BigDecimal r28_c7;
		private BigDecimal r28_c8;
		private BigDecimal r28_total;

		// Row r29
		private String r29_product;
		private BigDecimal r29_usd;
		private BigDecimal r29_zar;
		private BigDecimal r29_gbp;
		private BigDecimal r29_euro;
		private BigDecimal r29_yen;
		private BigDecimal r29_c6;
		private BigDecimal r29_c7;
		private BigDecimal r29_c8;
		private BigDecimal r29_total;

		// Row r30
		private String r30_product;
		private BigDecimal r30_usd;
		private BigDecimal r30_zar;
		private BigDecimal r30_gbp;
		private BigDecimal r30_euro;
		private BigDecimal r30_yen;
		private BigDecimal r30_c6;
		private BigDecimal r30_c7;
		private BigDecimal r30_c8;
		private BigDecimal r30_total;

		// Row r31
		private String r31_product;
		private BigDecimal r31_usd;
		private BigDecimal r31_zar;
		private BigDecimal r31_gbp;
		private BigDecimal r31_euro;
		private BigDecimal r31_yen;
		private BigDecimal r31_c6;
		private BigDecimal r31_c7;
		private BigDecimal r31_c8;
		private BigDecimal r31_total;

		// Row r32
		private String r32_product;
		private BigDecimal r32_usd;
		private BigDecimal r32_zar;
		private BigDecimal r32_gbp;
		private BigDecimal r32_euro;
		private BigDecimal r32_yen;
		private BigDecimal r32_c6;
		private BigDecimal r32_c7;
		private BigDecimal r32_c8;
		private BigDecimal r32_total;

		// Row r33
		private String r33_product;
		private BigDecimal r33_usd;
		private BigDecimal r33_zar;
		private BigDecimal r33_gbp;
		private BigDecimal r33_euro;
		private BigDecimal r33_yen;
		private BigDecimal r33_c6;
		private BigDecimal r33_c7;
		private BigDecimal r33_c8;
		private BigDecimal r33_total;

		// Row r34
		private String r34_product;
		private BigDecimal r34_usd;
		private BigDecimal r34_zar;
		private BigDecimal r34_gbp;
		private BigDecimal r34_euro;
		private BigDecimal r34_yen;
		private BigDecimal r34_c6;
		private BigDecimal r34_c7;
		private BigDecimal r34_c8;
		private BigDecimal r34_total;

		// Row r35
		private String r35_product;
		private BigDecimal r35_usd;
		private BigDecimal r35_zar;
		private BigDecimal r35_gbp;
		private BigDecimal r35_euro;
		private BigDecimal r35_yen;
		private BigDecimal r35_c6;
		private BigDecimal r35_c7;
		private BigDecimal r35_c8;
		private BigDecimal r35_total;

		// Row r36
		private String r36_product;
		private BigDecimal r36_usd;
		private BigDecimal r36_zar;
		private BigDecimal r36_gbp;
		private BigDecimal r36_euro;
		private BigDecimal r36_yen;
		private BigDecimal r36_c6;
		private BigDecimal r36_c7;
		private BigDecimal r36_c8;
		private BigDecimal r36_total;

		// Row r37
		private String r37_product;
		private BigDecimal r37_usd;
		private BigDecimal r37_zar;
		private BigDecimal r37_gbp;
		private BigDecimal r37_euro;
		private BigDecimal r37_yen;
		private BigDecimal r37_c6;
		private BigDecimal r37_c7;
		private BigDecimal r37_c8;
		private BigDecimal r37_total;

		// Row r38
		private String r38_product;
		private BigDecimal r38_usd;
		private BigDecimal r38_zar;
		private BigDecimal r38_gbp;
		private BigDecimal r38_euro;
		private BigDecimal r38_yen;
		private BigDecimal r38_c6;
		private BigDecimal r38_c7;
		private BigDecimal r38_c8;
		private BigDecimal r38_total;

		// Row r39
		private String r39_product;
		private BigDecimal r39_usd;
		private BigDecimal r39_zar;
		private BigDecimal r39_gbp;
		private BigDecimal r39_euro;
		private BigDecimal r39_yen;
		private BigDecimal r39_c6;
		private BigDecimal r39_c7;
		private BigDecimal r39_c8;
		private BigDecimal r39_total;

		// Row r40
		private String r40_product;
		private BigDecimal r40_usd;
		private BigDecimal r40_zar;
		private BigDecimal r40_gbp;
		private BigDecimal r40_euro;
		private BigDecimal r40_yen;
		private BigDecimal r40_c6;
		private BigDecimal r40_c7;
		private BigDecimal r40_c8;
		private BigDecimal r40_total;

		// Row r41
		private String r41_product;
		private BigDecimal r41_usd;
		private BigDecimal r41_zar;
		private BigDecimal r41_gbp;
		private BigDecimal r41_euro;
		private BigDecimal r41_yen;
		private BigDecimal r41_c6;
		private BigDecimal r41_c7;
		private BigDecimal r41_c8;
		private BigDecimal r41_total;

		// Row r42
		private String r42_product;
		private BigDecimal r42_usd;
		private BigDecimal r42_zar;
		private BigDecimal r42_gbp;
		private BigDecimal r42_euro;
		private BigDecimal r42_yen;
		private BigDecimal r42_c6;
		private BigDecimal r42_c7;
		private BigDecimal r42_c8;
		private BigDecimal r42_total;

		// Row r43
		private String r43_product;
		private BigDecimal r43_usd;
		private BigDecimal r43_zar;
		private BigDecimal r43_gbp;
		private BigDecimal r43_euro;
		private BigDecimal r43_yen;
		private BigDecimal r43_c6;
		private BigDecimal r43_c7;
		private BigDecimal r43_c8;
		private BigDecimal r43_total;

		// Row r44
		private String r44_product;
		private BigDecimal r44_usd;
		private BigDecimal r44_zar;
		private BigDecimal r44_gbp;
		private BigDecimal r44_euro;
		private BigDecimal r44_yen;
		private BigDecimal r44_c6;
		private BigDecimal r44_c7;
		private BigDecimal r44_c8;
		private BigDecimal r44_total;

		// Row r45
		private String r45_product;
		private BigDecimal r45_usd;
		private BigDecimal r45_zar;
		private BigDecimal r45_gbp;
		private BigDecimal r45_euro;
		private BigDecimal r45_yen;
		private BigDecimal r45_c6;
		private BigDecimal r45_c7;
		private BigDecimal r45_c8;
		private BigDecimal r45_total;

		// Row r46
		private String r46_product;
		private BigDecimal r46_usd;
		private BigDecimal r46_zar;
		private BigDecimal r46_gbp;
		private BigDecimal r46_euro;
		private BigDecimal r46_yen;
		private BigDecimal r46_c6;
		private BigDecimal r46_c7;
		private BigDecimal r46_c8;
		private BigDecimal r46_total;

		// Row r47
		private String r47_product;
		private BigDecimal r47_usd;
		private BigDecimal r47_zar;
		private BigDecimal r47_gbp;
		private BigDecimal r47_euro;
		private BigDecimal r47_yen;
		private BigDecimal r47_c6;
		private BigDecimal r47_c7;
		private BigDecimal r47_c8;
		private BigDecimal r47_total;

		// Row r48
		private String r48_product;
		private BigDecimal r48_usd;
		private BigDecimal r48_zar;
		private BigDecimal r48_gbp;
		private BigDecimal r48_euro;
		private BigDecimal r48_yen;
		private BigDecimal r48_c6;
		private BigDecimal r48_c7;
		private BigDecimal r48_c8;
		private BigDecimal r48_total;

		// Row r49
		private String r49_product;
		private BigDecimal r49_usd;
		private BigDecimal r49_zar;
		private BigDecimal r49_gbp;
		private BigDecimal r49_euro;
		private BigDecimal r49_yen;
		private BigDecimal r49_c6;
		private BigDecimal r49_c7;
		private BigDecimal r49_c8;
		private BigDecimal r49_total;

		// Row r50
		private String r50_product;
		private BigDecimal r50_usd;
		private BigDecimal r50_zar;
		private BigDecimal r50_gbp;
		private BigDecimal r50_euro;
		private BigDecimal r50_yen;
		private BigDecimal r50_c6;
		private BigDecimal r50_c7;
		private BigDecimal r50_c8;
		private BigDecimal r50_total;

		// Row r51
		private String r51_product;
		private BigDecimal r51_usd;
		private BigDecimal r51_zar;
		private BigDecimal r51_gbp;
		private BigDecimal r51_euro;
		private BigDecimal r51_yen;
		private BigDecimal r51_c6;
		private BigDecimal r51_c7;
		private BigDecimal r51_c8;
		private BigDecimal r51_total;

		// Row r52
		private String r52_product;
		private BigDecimal r52_usd;
		private BigDecimal r52_zar;
		private BigDecimal r52_gbp;
		private BigDecimal r52_euro;
		private BigDecimal r52_yen;
		private BigDecimal r52_c6;
		private BigDecimal r52_c7;
		private BigDecimal r52_c8;
		private BigDecimal r52_total;

		// Row r53
		private String r53_product;
		private BigDecimal r53_usd;
		private BigDecimal r53_zar;
		private BigDecimal r53_gbp;
		private BigDecimal r53_euro;
		private BigDecimal r53_yen;
		private BigDecimal r53_c6;
		private BigDecimal r53_c7;
		private BigDecimal r53_c8;
		private BigDecimal r53_total;

		// Row r54
		private String r54_product;
		private BigDecimal r54_usd;
		private BigDecimal r54_zar;
		private BigDecimal r54_gbp;
		private BigDecimal r54_euro;
		private BigDecimal r54_yen;
		private BigDecimal r54_c6;
		private BigDecimal r54_c7;
		private BigDecimal r54_c8;
		private BigDecimal r54_total;

		// Row r55
		private String r55_product;
		private BigDecimal r55_usd;
		private BigDecimal r55_zar;
		private BigDecimal r55_gbp;
		private BigDecimal r55_euro;
		private BigDecimal r55_yen;
		private BigDecimal r55_c6;
		private BigDecimal r55_c7;
		private BigDecimal r55_c8;
		private BigDecimal r55_total;

		// Row r56
		private String r56_product;
		private BigDecimal r56_usd;
		private BigDecimal r56_zar;
		private BigDecimal r56_gbp;
		private BigDecimal r56_euro;
		private BigDecimal r56_yen;
		private BigDecimal r56_c6;
		private BigDecimal r56_c7;
		private BigDecimal r56_c8;
		private BigDecimal r56_total;

		// Row r57
		private String r57_product;
		private BigDecimal r57_usd;
		private BigDecimal r57_zar;
		private BigDecimal r57_gbp;
		private BigDecimal r57_euro;
		private BigDecimal r57_yen;
		private BigDecimal r57_c6;
		private BigDecimal r57_c7;
		private BigDecimal r57_c8;
		private BigDecimal r57_total;

		// Row r58
		private String r58_product;
		private BigDecimal r58_usd;
		private BigDecimal r58_zar;
		private BigDecimal r58_gbp;
		private BigDecimal r58_euro;
		private BigDecimal r58_yen;
		private BigDecimal r58_c6;
		private BigDecimal r58_c7;
		private BigDecimal r58_c8;
		private BigDecimal r58_total;

		// Row r59
		private String r59_product;
		private BigDecimal r59_usd;
		private BigDecimal r59_zar;
		private BigDecimal r59_gbp;
		private BigDecimal r59_euro;
		private BigDecimal r59_yen;
		private BigDecimal r59_c6;
		private BigDecimal r59_c7;
		private BigDecimal r59_c8;
		private BigDecimal r59_total;

		// Row r60
		private String r60_product;
		private BigDecimal r60_usd;
		private BigDecimal r60_zar;
		private BigDecimal r60_gbp;
		private BigDecimal r60_euro;
		private BigDecimal r60_yen;
		private BigDecimal r60_c6;
		private BigDecimal r60_c7;
		private BigDecimal r60_c8;
		private BigDecimal r60_total;

		// Row r61
		private String r61_product;
		private BigDecimal r61_usd;
		private BigDecimal r61_zar;
		private BigDecimal r61_gbp;
		private BigDecimal r61_euro;
		private BigDecimal r61_yen;
		private BigDecimal r61_c6;
		private BigDecimal r61_c7;
		private BigDecimal r61_c8;
		private BigDecimal r61_total;

		// Row r62
		private String r62_product;
		private BigDecimal r62_usd;
		private BigDecimal r62_zar;
		private BigDecimal r62_gbp;
		private BigDecimal r62_euro;
		private BigDecimal r62_yen;
		private BigDecimal r62_c6;
		private BigDecimal r62_c7;
		private BigDecimal r62_c8;
		private BigDecimal r62_total;

		@Column(name = "REPORT_VERSION", length = 100)
		private BigDecimal REPORT_VERSION;

		@Column(name = "REPORT_FREQUENCY", length = 100)
		private String REPORT_FREQUENCY;

		@Column(name = "REPORT_CODE", length = 100)
		private String REPORT_CODE;

		@Column(name = "REPORT_DESC", length = 100)
		private String REPORT_DESC;

		@Column(name = "ENTITY_FLG", length = 1)
		private String ENTITY_FLG;

		@Column(name = "MODIFY_FLG", length = 1)
		private String MODIFY_FLG;

		@Column(name = "DEL_FLG", length = 1)
		private String DEL_FLG;

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public String getR6_product() {
			return r6_product;
		}

		public void setR6_product(String r6_product) {
			this.r6_product = r6_product;
		}

		public BigDecimal getR6_usd() {
			return r6_usd;
		}

		public void setR6_usd(BigDecimal r6_usd) {
			this.r6_usd = r6_usd;
		}

		public BigDecimal getR6_zar() {
			return r6_zar;
		}

		public void setR6_zar(BigDecimal r6_zar) {
			this.r6_zar = r6_zar;
		}

		public BigDecimal getR6_gbp() {
			return r6_gbp;
		}

		public void setR6_gbp(BigDecimal r6_gbp) {
			this.r6_gbp = r6_gbp;
		}

		public BigDecimal getR6_euro() {
			return r6_euro;
		}

		public void setR6_euro(BigDecimal r6_euro) {
			this.r6_euro = r6_euro;
		}

		public BigDecimal getR6_yen() {
			return r6_yen;
		}

		public void setR6_yen(BigDecimal r6_yen) {
			this.r6_yen = r6_yen;
		}

		public BigDecimal getR6_c6() {
			return r6_c6;
		}

		public void setR6_c6(BigDecimal r6_c6) {
			this.r6_c6 = r6_c6;
		}

		public BigDecimal getR6_c7() {
			return r6_c7;
		}

		public void setR6_c7(BigDecimal r6_c7) {
			this.r6_c7 = r6_c7;
		}

		public BigDecimal getR6_c8() {
			return r6_c8;
		}

		public void setR6_c8(BigDecimal r6_c8) {
			this.r6_c8 = r6_c8;
		}

		public BigDecimal getR6_total() {
			return r6_total;
		}

		public void setR6_total(BigDecimal r6_total) {
			this.r6_total = r6_total;
		}

		public String getR7_product() {
			return r7_product;
		}

		public void setR7_product(String r7_product) {
			this.r7_product = r7_product;
		}

		public BigDecimal getR7_usd() {
			return r7_usd;
		}

		public void setR7_usd(BigDecimal r7_usd) {
			this.r7_usd = r7_usd;
		}

		public BigDecimal getR7_zar() {
			return r7_zar;
		}

		public void setR7_zar(BigDecimal r7_zar) {
			this.r7_zar = r7_zar;
		}

		public BigDecimal getR7_gbp() {
			return r7_gbp;
		}

		public void setR7_gbp(BigDecimal r7_gbp) {
			this.r7_gbp = r7_gbp;
		}

		public BigDecimal getR7_euro() {
			return r7_euro;
		}

		public void setR7_euro(BigDecimal r7_euro) {
			this.r7_euro = r7_euro;
		}

		public BigDecimal getR7_yen() {
			return r7_yen;
		}

		public void setR7_yen(BigDecimal r7_yen) {
			this.r7_yen = r7_yen;
		}

		public BigDecimal getR7_c6() {
			return r7_c6;
		}

		public void setR7_c6(BigDecimal r7_c6) {
			this.r7_c6 = r7_c6;
		}

		public BigDecimal getR7_c7() {
			return r7_c7;
		}

		public void setR7_c7(BigDecimal r7_c7) {
			this.r7_c7 = r7_c7;
		}

		public BigDecimal getR7_c8() {
			return r7_c8;
		}

		public void setR7_c8(BigDecimal r7_c8) {
			this.r7_c8 = r7_c8;
		}

		public BigDecimal getR7_total() {
			return r7_total;
		}

		public void setR7_total(BigDecimal r7_total) {
			this.r7_total = r7_total;
		}

		public String getR8_product() {
			return r8_product;
		}

		public void setR8_product(String r8_product) {
			this.r8_product = r8_product;
		}

		public BigDecimal getR8_usd() {
			return r8_usd;
		}

		public void setR8_usd(BigDecimal r8_usd) {
			this.r8_usd = r8_usd;
		}

		public BigDecimal getR8_zar() {
			return r8_zar;
		}

		public void setR8_zar(BigDecimal r8_zar) {
			this.r8_zar = r8_zar;
		}

		public BigDecimal getR8_gbp() {
			return r8_gbp;
		}

		public void setR8_gbp(BigDecimal r8_gbp) {
			this.r8_gbp = r8_gbp;
		}

		public BigDecimal getR8_euro() {
			return r8_euro;
		}

		public void setR8_euro(BigDecimal r8_euro) {
			this.r8_euro = r8_euro;
		}

		public BigDecimal getR8_yen() {
			return r8_yen;
		}

		public void setR8_yen(BigDecimal r8_yen) {
			this.r8_yen = r8_yen;
		}

		public BigDecimal getR8_c6() {
			return r8_c6;
		}

		public void setR8_c6(BigDecimal r8_c6) {
			this.r8_c6 = r8_c6;
		}

		public BigDecimal getR8_c7() {
			return r8_c7;
		}

		public void setR8_c7(BigDecimal r8_c7) {
			this.r8_c7 = r8_c7;
		}

		public BigDecimal getR8_c8() {
			return r8_c8;
		}

		public void setR8_c8(BigDecimal r8_c8) {
			this.r8_c8 = r8_c8;
		}

		public BigDecimal getR8_total() {
			return r8_total;
		}

		public void setR8_total(BigDecimal r8_total) {
			this.r8_total = r8_total;
		}

		public String getR9_product() {
			return r9_product;
		}

		public void setR9_product(String r9_product) {
			this.r9_product = r9_product;
		}

		public BigDecimal getR9_usd() {
			return r9_usd;
		}

		public void setR9_usd(BigDecimal r9_usd) {
			this.r9_usd = r9_usd;
		}

		public BigDecimal getR9_zar() {
			return r9_zar;
		}

		public void setR9_zar(BigDecimal r9_zar) {
			this.r9_zar = r9_zar;
		}

		public BigDecimal getR9_gbp() {
			return r9_gbp;
		}

		public void setR9_gbp(BigDecimal r9_gbp) {
			this.r9_gbp = r9_gbp;
		}

		public BigDecimal getR9_euro() {
			return r9_euro;
		}

		public void setR9_euro(BigDecimal r9_euro) {
			this.r9_euro = r9_euro;
		}

		public BigDecimal getR9_yen() {
			return r9_yen;
		}

		public void setR9_yen(BigDecimal r9_yen) {
			this.r9_yen = r9_yen;
		}

		public BigDecimal getR9_c6() {
			return r9_c6;
		}

		public void setR9_c6(BigDecimal r9_c6) {
			this.r9_c6 = r9_c6;
		}

		public BigDecimal getR9_c7() {
			return r9_c7;
		}

		public void setR9_c7(BigDecimal r9_c7) {
			this.r9_c7 = r9_c7;
		}

		public BigDecimal getR9_c8() {
			return r9_c8;
		}

		public void setR9_c8(BigDecimal r9_c8) {
			this.r9_c8 = r9_c8;
		}

		public BigDecimal getR9_total() {
			return r9_total;
		}

		public void setR9_total(BigDecimal r9_total) {
			this.r9_total = r9_total;
		}

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public BigDecimal getR10_usd() {
			return r10_usd;
		}

		public void setR10_usd(BigDecimal r10_usd) {
			this.r10_usd = r10_usd;
		}

		public BigDecimal getR10_zar() {
			return r10_zar;
		}

		public void setR10_zar(BigDecimal r10_zar) {
			this.r10_zar = r10_zar;
		}

		public BigDecimal getR10_gbp() {
			return r10_gbp;
		}

		public void setR10_gbp(BigDecimal r10_gbp) {
			this.r10_gbp = r10_gbp;
		}

		public BigDecimal getR10_euro() {
			return r10_euro;
		}

		public void setR10_euro(BigDecimal r10_euro) {
			this.r10_euro = r10_euro;
		}

		public BigDecimal getR10_yen() {
			return r10_yen;
		}

		public void setR10_yen(BigDecimal r10_yen) {
			this.r10_yen = r10_yen;
		}

		public BigDecimal getR10_c6() {
			return r10_c6;
		}

		public void setR10_c6(BigDecimal r10_c6) {
			this.r10_c6 = r10_c6;
		}

		public BigDecimal getR10_c7() {
			return r10_c7;
		}

		public void setR10_c7(BigDecimal r10_c7) {
			this.r10_c7 = r10_c7;
		}

		public BigDecimal getR10_c8() {
			return r10_c8;
		}

		public void setR10_c8(BigDecimal r10_c8) {
			this.r10_c8 = r10_c8;
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

		public BigDecimal getR11_usd() {
			return r11_usd;
		}

		public void setR11_usd(BigDecimal r11_usd) {
			this.r11_usd = r11_usd;
		}

		public BigDecimal getR11_zar() {
			return r11_zar;
		}

		public void setR11_zar(BigDecimal r11_zar) {
			this.r11_zar = r11_zar;
		}

		public BigDecimal getR11_gbp() {
			return r11_gbp;
		}

		public void setR11_gbp(BigDecimal r11_gbp) {
			this.r11_gbp = r11_gbp;
		}

		public BigDecimal getR11_euro() {
			return r11_euro;
		}

		public void setR11_euro(BigDecimal r11_euro) {
			this.r11_euro = r11_euro;
		}

		public BigDecimal getR11_yen() {
			return r11_yen;
		}

		public void setR11_yen(BigDecimal r11_yen) {
			this.r11_yen = r11_yen;
		}

		public BigDecimal getR11_c6() {
			return r11_c6;
		}

		public void setR11_c6(BigDecimal r11_c6) {
			this.r11_c6 = r11_c6;
		}

		public BigDecimal getR11_c7() {
			return r11_c7;
		}

		public void setR11_c7(BigDecimal r11_c7) {
			this.r11_c7 = r11_c7;
		}

		public BigDecimal getR11_c8() {
			return r11_c8;
		}

		public void setR11_c8(BigDecimal r11_c8) {
			this.r11_c8 = r11_c8;
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

		public BigDecimal getR12_usd() {
			return r12_usd;
		}

		public void setR12_usd(BigDecimal r12_usd) {
			this.r12_usd = r12_usd;
		}

		public BigDecimal getR12_zar() {
			return r12_zar;
		}

		public void setR12_zar(BigDecimal r12_zar) {
			this.r12_zar = r12_zar;
		}

		public BigDecimal getR12_gbp() {
			return r12_gbp;
		}

		public void setR12_gbp(BigDecimal r12_gbp) {
			this.r12_gbp = r12_gbp;
		}

		public BigDecimal getR12_euro() {
			return r12_euro;
		}

		public void setR12_euro(BigDecimal r12_euro) {
			this.r12_euro = r12_euro;
		}

		public BigDecimal getR12_yen() {
			return r12_yen;
		}

		public void setR12_yen(BigDecimal r12_yen) {
			this.r12_yen = r12_yen;
		}

		public BigDecimal getR12_c6() {
			return r12_c6;
		}

		public void setR12_c6(BigDecimal r12_c6) {
			this.r12_c6 = r12_c6;
		}

		public BigDecimal getR12_c7() {
			return r12_c7;
		}

		public void setR12_c7(BigDecimal r12_c7) {
			this.r12_c7 = r12_c7;
		}

		public BigDecimal getR12_c8() {
			return r12_c8;
		}

		public void setR12_c8(BigDecimal r12_c8) {
			this.r12_c8 = r12_c8;
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

		public BigDecimal getR13_usd() {
			return r13_usd;
		}

		public void setR13_usd(BigDecimal r13_usd) {
			this.r13_usd = r13_usd;
		}

		public BigDecimal getR13_zar() {
			return r13_zar;
		}

		public void setR13_zar(BigDecimal r13_zar) {
			this.r13_zar = r13_zar;
		}

		public BigDecimal getR13_gbp() {
			return r13_gbp;
		}

		public void setR13_gbp(BigDecimal r13_gbp) {
			this.r13_gbp = r13_gbp;
		}

		public BigDecimal getR13_euro() {
			return r13_euro;
		}

		public void setR13_euro(BigDecimal r13_euro) {
			this.r13_euro = r13_euro;
		}

		public BigDecimal getR13_yen() {
			return r13_yen;
		}

		public void setR13_yen(BigDecimal r13_yen) {
			this.r13_yen = r13_yen;
		}

		public BigDecimal getR13_c6() {
			return r13_c6;
		}

		public void setR13_c6(BigDecimal r13_c6) {
			this.r13_c6 = r13_c6;
		}

		public BigDecimal getR13_c7() {
			return r13_c7;
		}

		public void setR13_c7(BigDecimal r13_c7) {
			this.r13_c7 = r13_c7;
		}

		public BigDecimal getR13_c8() {
			return r13_c8;
		}

		public void setR13_c8(BigDecimal r13_c8) {
			this.r13_c8 = r13_c8;
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

		public BigDecimal getR14_usd() {
			return r14_usd;
		}

		public void setR14_usd(BigDecimal r14_usd) {
			this.r14_usd = r14_usd;
		}

		public BigDecimal getR14_zar() {
			return r14_zar;
		}

		public void setR14_zar(BigDecimal r14_zar) {
			this.r14_zar = r14_zar;
		}

		public BigDecimal getR14_gbp() {
			return r14_gbp;
		}

		public void setR14_gbp(BigDecimal r14_gbp) {
			this.r14_gbp = r14_gbp;
		}

		public BigDecimal getR14_euro() {
			return r14_euro;
		}

		public void setR14_euro(BigDecimal r14_euro) {
			this.r14_euro = r14_euro;
		}

		public BigDecimal getR14_yen() {
			return r14_yen;
		}

		public void setR14_yen(BigDecimal r14_yen) {
			this.r14_yen = r14_yen;
		}

		public BigDecimal getR14_c6() {
			return r14_c6;
		}

		public void setR14_c6(BigDecimal r14_c6) {
			this.r14_c6 = r14_c6;
		}

		public BigDecimal getR14_c7() {
			return r14_c7;
		}

		public void setR14_c7(BigDecimal r14_c7) {
			this.r14_c7 = r14_c7;
		}

		public BigDecimal getR14_c8() {
			return r14_c8;
		}

		public void setR14_c8(BigDecimal r14_c8) {
			this.r14_c8 = r14_c8;
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

		public BigDecimal getR15_usd() {
			return r15_usd;
		}

		public void setR15_usd(BigDecimal r15_usd) {
			this.r15_usd = r15_usd;
		}

		public BigDecimal getR15_zar() {
			return r15_zar;
		}

		public void setR15_zar(BigDecimal r15_zar) {
			this.r15_zar = r15_zar;
		}

		public BigDecimal getR15_gbp() {
			return r15_gbp;
		}

		public void setR15_gbp(BigDecimal r15_gbp) {
			this.r15_gbp = r15_gbp;
		}

		public BigDecimal getR15_euro() {
			return r15_euro;
		}

		public void setR15_euro(BigDecimal r15_euro) {
			this.r15_euro = r15_euro;
		}

		public BigDecimal getR15_yen() {
			return r15_yen;
		}

		public void setR15_yen(BigDecimal r15_yen) {
			this.r15_yen = r15_yen;
		}

		public BigDecimal getR15_c6() {
			return r15_c6;
		}

		public void setR15_c6(BigDecimal r15_c6) {
			this.r15_c6 = r15_c6;
		}

		public BigDecimal getR15_c7() {
			return r15_c7;
		}

		public void setR15_c7(BigDecimal r15_c7) {
			this.r15_c7 = r15_c7;
		}

		public BigDecimal getR15_c8() {
			return r15_c8;
		}

		public void setR15_c8(BigDecimal r15_c8) {
			this.r15_c8 = r15_c8;
		}

		public BigDecimal getR15_total() {
			return r15_total;
		}

		public void setR15_total(BigDecimal r15_total) {
			this.r15_total = r15_total;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_usd() {
			return r16_usd;
		}

		public void setR16_usd(BigDecimal r16_usd) {
			this.r16_usd = r16_usd;
		}

		public BigDecimal getR16_zar() {
			return r16_zar;
		}

		public void setR16_zar(BigDecimal r16_zar) {
			this.r16_zar = r16_zar;
		}

		public BigDecimal getR16_gbp() {
			return r16_gbp;
		}

		public void setR16_gbp(BigDecimal r16_gbp) {
			this.r16_gbp = r16_gbp;
		}

		public BigDecimal getR16_euro() {
			return r16_euro;
		}

		public void setR16_euro(BigDecimal r16_euro) {
			this.r16_euro = r16_euro;
		}

		public BigDecimal getR16_yen() {
			return r16_yen;
		}

		public void setR16_yen(BigDecimal r16_yen) {
			this.r16_yen = r16_yen;
		}

		public BigDecimal getR16_c6() {
			return r16_c6;
		}

		public void setR16_c6(BigDecimal r16_c6) {
			this.r16_c6 = r16_c6;
		}

		public BigDecimal getR16_c7() {
			return r16_c7;
		}

		public void setR16_c7(BigDecimal r16_c7) {
			this.r16_c7 = r16_c7;
		}

		public BigDecimal getR16_c8() {
			return r16_c8;
		}

		public void setR16_c8(BigDecimal r16_c8) {
			this.r16_c8 = r16_c8;
		}

		public BigDecimal getR16_total() {
			return r16_total;
		}

		public void setR16_total(BigDecimal r16_total) {
			this.r16_total = r16_total;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_usd() {
			return r17_usd;
		}

		public void setR17_usd(BigDecimal r17_usd) {
			this.r17_usd = r17_usd;
		}

		public BigDecimal getR17_zar() {
			return r17_zar;
		}

		public void setR17_zar(BigDecimal r17_zar) {
			this.r17_zar = r17_zar;
		}

		public BigDecimal getR17_gbp() {
			return r17_gbp;
		}

		public void setR17_gbp(BigDecimal r17_gbp) {
			this.r17_gbp = r17_gbp;
		}

		public BigDecimal getR17_euro() {
			return r17_euro;
		}

		public void setR17_euro(BigDecimal r17_euro) {
			this.r17_euro = r17_euro;
		}

		public BigDecimal getR17_yen() {
			return r17_yen;
		}

		public void setR17_yen(BigDecimal r17_yen) {
			this.r17_yen = r17_yen;
		}

		public BigDecimal getR17_c6() {
			return r17_c6;
		}

		public void setR17_c6(BigDecimal r17_c6) {
			this.r17_c6 = r17_c6;
		}

		public BigDecimal getR17_c7() {
			return r17_c7;
		}

		public void setR17_c7(BigDecimal r17_c7) {
			this.r17_c7 = r17_c7;
		}

		public BigDecimal getR17_c8() {
			return r17_c8;
		}

		public void setR17_c8(BigDecimal r17_c8) {
			this.r17_c8 = r17_c8;
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

		public BigDecimal getR18_usd() {
			return r18_usd;
		}

		public void setR18_usd(BigDecimal r18_usd) {
			this.r18_usd = r18_usd;
		}

		public BigDecimal getR18_zar() {
			return r18_zar;
		}

		public void setR18_zar(BigDecimal r18_zar) {
			this.r18_zar = r18_zar;
		}

		public BigDecimal getR18_gbp() {
			return r18_gbp;
		}

		public void setR18_gbp(BigDecimal r18_gbp) {
			this.r18_gbp = r18_gbp;
		}

		public BigDecimal getR18_euro() {
			return r18_euro;
		}

		public void setR18_euro(BigDecimal r18_euro) {
			this.r18_euro = r18_euro;
		}

		public BigDecimal getR18_yen() {
			return r18_yen;
		}

		public void setR18_yen(BigDecimal r18_yen) {
			this.r18_yen = r18_yen;
		}

		public BigDecimal getR18_c6() {
			return r18_c6;
		}

		public void setR18_c6(BigDecimal r18_c6) {
			this.r18_c6 = r18_c6;
		}

		public BigDecimal getR18_c7() {
			return r18_c7;
		}

		public void setR18_c7(BigDecimal r18_c7) {
			this.r18_c7 = r18_c7;
		}

		public BigDecimal getR18_c8() {
			return r18_c8;
		}

		public void setR18_c8(BigDecimal r18_c8) {
			this.r18_c8 = r18_c8;
		}

		public BigDecimal getR18_total() {
			return r18_total;
		}

		public void setR18_total(BigDecimal r18_total) {
			this.r18_total = r18_total;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_usd() {
			return r19_usd;
		}

		public void setR19_usd(BigDecimal r19_usd) {
			this.r19_usd = r19_usd;
		}

		public BigDecimal getR19_zar() {
			return r19_zar;
		}

		public void setR19_zar(BigDecimal r19_zar) {
			this.r19_zar = r19_zar;
		}

		public BigDecimal getR19_gbp() {
			return r19_gbp;
		}

		public void setR19_gbp(BigDecimal r19_gbp) {
			this.r19_gbp = r19_gbp;
		}

		public BigDecimal getR19_euro() {
			return r19_euro;
		}

		public void setR19_euro(BigDecimal r19_euro) {
			this.r19_euro = r19_euro;
		}

		public BigDecimal getR19_yen() {
			return r19_yen;
		}

		public void setR19_yen(BigDecimal r19_yen) {
			this.r19_yen = r19_yen;
		}

		public BigDecimal getR19_c6() {
			return r19_c6;
		}

		public void setR19_c6(BigDecimal r19_c6) {
			this.r19_c6 = r19_c6;
		}

		public BigDecimal getR19_c7() {
			return r19_c7;
		}

		public void setR19_c7(BigDecimal r19_c7) {
			this.r19_c7 = r19_c7;
		}

		public BigDecimal getR19_c8() {
			return r19_c8;
		}

		public void setR19_c8(BigDecimal r19_c8) {
			this.r19_c8 = r19_c8;
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

		public BigDecimal getR20_usd() {
			return r20_usd;
		}

		public void setR20_usd(BigDecimal r20_usd) {
			this.r20_usd = r20_usd;
		}

		public BigDecimal getR20_zar() {
			return r20_zar;
		}

		public void setR20_zar(BigDecimal r20_zar) {
			this.r20_zar = r20_zar;
		}

		public BigDecimal getR20_gbp() {
			return r20_gbp;
		}

		public void setR20_gbp(BigDecimal r20_gbp) {
			this.r20_gbp = r20_gbp;
		}

		public BigDecimal getR20_euro() {
			return r20_euro;
		}

		public void setR20_euro(BigDecimal r20_euro) {
			this.r20_euro = r20_euro;
		}

		public BigDecimal getR20_yen() {
			return r20_yen;
		}

		public void setR20_yen(BigDecimal r20_yen) {
			this.r20_yen = r20_yen;
		}

		public BigDecimal getR20_c6() {
			return r20_c6;
		}

		public void setR20_c6(BigDecimal r20_c6) {
			this.r20_c6 = r20_c6;
		}

		public BigDecimal getR20_c7() {
			return r20_c7;
		}

		public void setR20_c7(BigDecimal r20_c7) {
			this.r20_c7 = r20_c7;
		}

		public BigDecimal getR20_c8() {
			return r20_c8;
		}

		public void setR20_c8(BigDecimal r20_c8) {
			this.r20_c8 = r20_c8;
		}

		public BigDecimal getR20_total() {
			return r20_total;
		}

		public void setR20_total(BigDecimal r20_total) {
			this.r20_total = r20_total;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_usd() {
			return r21_usd;
		}

		public void setR21_usd(BigDecimal r21_usd) {
			this.r21_usd = r21_usd;
		}

		public BigDecimal getR21_zar() {
			return r21_zar;
		}

		public void setR21_zar(BigDecimal r21_zar) {
			this.r21_zar = r21_zar;
		}

		public BigDecimal getR21_gbp() {
			return r21_gbp;
		}

		public void setR21_gbp(BigDecimal r21_gbp) {
			this.r21_gbp = r21_gbp;
		}

		public BigDecimal getR21_euro() {
			return r21_euro;
		}

		public void setR21_euro(BigDecimal r21_euro) {
			this.r21_euro = r21_euro;
		}

		public BigDecimal getR21_yen() {
			return r21_yen;
		}

		public void setR21_yen(BigDecimal r21_yen) {
			this.r21_yen = r21_yen;
		}

		public BigDecimal getR21_c6() {
			return r21_c6;
		}

		public void setR21_c6(BigDecimal r21_c6) {
			this.r21_c6 = r21_c6;
		}

		public BigDecimal getR21_c7() {
			return r21_c7;
		}

		public void setR21_c7(BigDecimal r21_c7) {
			this.r21_c7 = r21_c7;
		}

		public BigDecimal getR21_c8() {
			return r21_c8;
		}

		public void setR21_c8(BigDecimal r21_c8) {
			this.r21_c8 = r21_c8;
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

		public BigDecimal getR22_usd() {
			return r22_usd;
		}

		public void setR22_usd(BigDecimal r22_usd) {
			this.r22_usd = r22_usd;
		}

		public BigDecimal getR22_zar() {
			return r22_zar;
		}

		public void setR22_zar(BigDecimal r22_zar) {
			this.r22_zar = r22_zar;
		}

		public BigDecimal getR22_gbp() {
			return r22_gbp;
		}

		public void setR22_gbp(BigDecimal r22_gbp) {
			this.r22_gbp = r22_gbp;
		}

		public BigDecimal getR22_euro() {
			return r22_euro;
		}

		public void setR22_euro(BigDecimal r22_euro) {
			this.r22_euro = r22_euro;
		}

		public BigDecimal getR22_yen() {
			return r22_yen;
		}

		public void setR22_yen(BigDecimal r22_yen) {
			this.r22_yen = r22_yen;
		}

		public BigDecimal getR22_c6() {
			return r22_c6;
		}

		public void setR22_c6(BigDecimal r22_c6) {
			this.r22_c6 = r22_c6;
		}

		public BigDecimal getR22_c7() {
			return r22_c7;
		}

		public void setR22_c7(BigDecimal r22_c7) {
			this.r22_c7 = r22_c7;
		}

		public BigDecimal getR22_c8() {
			return r22_c8;
		}

		public void setR22_c8(BigDecimal r22_c8) {
			this.r22_c8 = r22_c8;
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

		public BigDecimal getR23_usd() {
			return r23_usd;
		}

		public void setR23_usd(BigDecimal r23_usd) {
			this.r23_usd = r23_usd;
		}

		public BigDecimal getR23_zar() {
			return r23_zar;
		}

		public void setR23_zar(BigDecimal r23_zar) {
			this.r23_zar = r23_zar;
		}

		public BigDecimal getR23_gbp() {
			return r23_gbp;
		}

		public void setR23_gbp(BigDecimal r23_gbp) {
			this.r23_gbp = r23_gbp;
		}

		public BigDecimal getR23_euro() {
			return r23_euro;
		}

		public void setR23_euro(BigDecimal r23_euro) {
			this.r23_euro = r23_euro;
		}

		public BigDecimal getR23_yen() {
			return r23_yen;
		}

		public void setR23_yen(BigDecimal r23_yen) {
			this.r23_yen = r23_yen;
		}

		public BigDecimal getR23_c6() {
			return r23_c6;
		}

		public void setR23_c6(BigDecimal r23_c6) {
			this.r23_c6 = r23_c6;
		}

		public BigDecimal getR23_c7() {
			return r23_c7;
		}

		public void setR23_c7(BigDecimal r23_c7) {
			this.r23_c7 = r23_c7;
		}

		public BigDecimal getR23_c8() {
			return r23_c8;
		}

		public void setR23_c8(BigDecimal r23_c8) {
			this.r23_c8 = r23_c8;
		}

		public BigDecimal getR23_total() {
			return r23_total;
		}

		public void setR23_total(BigDecimal r23_total) {
			this.r23_total = r23_total;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_usd() {
			return r24_usd;
		}

		public void setR24_usd(BigDecimal r24_usd) {
			this.r24_usd = r24_usd;
		}

		public BigDecimal getR24_zar() {
			return r24_zar;
		}

		public void setR24_zar(BigDecimal r24_zar) {
			this.r24_zar = r24_zar;
		}

		public BigDecimal getR24_gbp() {
			return r24_gbp;
		}

		public void setR24_gbp(BigDecimal r24_gbp) {
			this.r24_gbp = r24_gbp;
		}

		public BigDecimal getR24_euro() {
			return r24_euro;
		}

		public void setR24_euro(BigDecimal r24_euro) {
			this.r24_euro = r24_euro;
		}

		public BigDecimal getR24_yen() {
			return r24_yen;
		}

		public void setR24_yen(BigDecimal r24_yen) {
			this.r24_yen = r24_yen;
		}

		public BigDecimal getR24_c6() {
			return r24_c6;
		}

		public void setR24_c6(BigDecimal r24_c6) {
			this.r24_c6 = r24_c6;
		}

		public BigDecimal getR24_c7() {
			return r24_c7;
		}

		public void setR24_c7(BigDecimal r24_c7) {
			this.r24_c7 = r24_c7;
		}

		public BigDecimal getR24_c8() {
			return r24_c8;
		}

		public void setR24_c8(BigDecimal r24_c8) {
			this.r24_c8 = r24_c8;
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

		public BigDecimal getR25_usd() {
			return r25_usd;
		}

		public void setR25_usd(BigDecimal r25_usd) {
			this.r25_usd = r25_usd;
		}

		public BigDecimal getR25_zar() {
			return r25_zar;
		}

		public void setR25_zar(BigDecimal r25_zar) {
			this.r25_zar = r25_zar;
		}

		public BigDecimal getR25_gbp() {
			return r25_gbp;
		}

		public void setR25_gbp(BigDecimal r25_gbp) {
			this.r25_gbp = r25_gbp;
		}

		public BigDecimal getR25_euro() {
			return r25_euro;
		}

		public void setR25_euro(BigDecimal r25_euro) {
			this.r25_euro = r25_euro;
		}

		public BigDecimal getR25_yen() {
			return r25_yen;
		}

		public void setR25_yen(BigDecimal r25_yen) {
			this.r25_yen = r25_yen;
		}

		public BigDecimal getR25_c6() {
			return r25_c6;
		}

		public void setR25_c6(BigDecimal r25_c6) {
			this.r25_c6 = r25_c6;
		}

		public BigDecimal getR25_c7() {
			return r25_c7;
		}

		public void setR25_c7(BigDecimal r25_c7) {
			this.r25_c7 = r25_c7;
		}

		public BigDecimal getR25_c8() {
			return r25_c8;
		}

		public void setR25_c8(BigDecimal r25_c8) {
			this.r25_c8 = r25_c8;
		}

		public BigDecimal getR25_total() {
			return r25_total;
		}

		public void setR25_total(BigDecimal r25_total) {
			this.r25_total = r25_total;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_usd() {
			return r26_usd;
		}

		public void setR26_usd(BigDecimal r26_usd) {
			this.r26_usd = r26_usd;
		}

		public BigDecimal getR26_zar() {
			return r26_zar;
		}

		public void setR26_zar(BigDecimal r26_zar) {
			this.r26_zar = r26_zar;
		}

		public BigDecimal getR26_gbp() {
			return r26_gbp;
		}

		public void setR26_gbp(BigDecimal r26_gbp) {
			this.r26_gbp = r26_gbp;
		}

		public BigDecimal getR26_euro() {
			return r26_euro;
		}

		public void setR26_euro(BigDecimal r26_euro) {
			this.r26_euro = r26_euro;
		}

		public BigDecimal getR26_yen() {
			return r26_yen;
		}

		public void setR26_yen(BigDecimal r26_yen) {
			this.r26_yen = r26_yen;
		}

		public BigDecimal getR26_c6() {
			return r26_c6;
		}

		public void setR26_c6(BigDecimal r26_c6) {
			this.r26_c6 = r26_c6;
		}

		public BigDecimal getR26_c7() {
			return r26_c7;
		}

		public void setR26_c7(BigDecimal r26_c7) {
			this.r26_c7 = r26_c7;
		}

		public BigDecimal getR26_c8() {
			return r26_c8;
		}

		public void setR26_c8(BigDecimal r26_c8) {
			this.r26_c8 = r26_c8;
		}

		public BigDecimal getR26_total() {
			return r26_total;
		}

		public void setR26_total(BigDecimal r26_total) {
			this.r26_total = r26_total;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_usd() {
			return r27_usd;
		}

		public void setR27_usd(BigDecimal r27_usd) {
			this.r27_usd = r27_usd;
		}

		public BigDecimal getR27_zar() {
			return r27_zar;
		}

		public void setR27_zar(BigDecimal r27_zar) {
			this.r27_zar = r27_zar;
		}

		public BigDecimal getR27_gbp() {
			return r27_gbp;
		}

		public void setR27_gbp(BigDecimal r27_gbp) {
			this.r27_gbp = r27_gbp;
		}

		public BigDecimal getR27_euro() {
			return r27_euro;
		}

		public void setR27_euro(BigDecimal r27_euro) {
			this.r27_euro = r27_euro;
		}

		public BigDecimal getR27_yen() {
			return r27_yen;
		}

		public void setR27_yen(BigDecimal r27_yen) {
			this.r27_yen = r27_yen;
		}

		public BigDecimal getR27_c6() {
			return r27_c6;
		}

		public void setR27_c6(BigDecimal r27_c6) {
			this.r27_c6 = r27_c6;
		}

		public BigDecimal getR27_c7() {
			return r27_c7;
		}

		public void setR27_c7(BigDecimal r27_c7) {
			this.r27_c7 = r27_c7;
		}

		public BigDecimal getR27_c8() {
			return r27_c8;
		}

		public void setR27_c8(BigDecimal r27_c8) {
			this.r27_c8 = r27_c8;
		}

		public BigDecimal getR27_total() {
			return r27_total;
		}

		public void setR27_total(BigDecimal r27_total) {
			this.r27_total = r27_total;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_usd() {
			return r28_usd;
		}

		public void setR28_usd(BigDecimal r28_usd) {
			this.r28_usd = r28_usd;
		}

		public BigDecimal getR28_zar() {
			return r28_zar;
		}

		public void setR28_zar(BigDecimal r28_zar) {
			this.r28_zar = r28_zar;
		}

		public BigDecimal getR28_gbp() {
			return r28_gbp;
		}

		public void setR28_gbp(BigDecimal r28_gbp) {
			this.r28_gbp = r28_gbp;
		}

		public BigDecimal getR28_euro() {
			return r28_euro;
		}

		public void setR28_euro(BigDecimal r28_euro) {
			this.r28_euro = r28_euro;
		}

		public BigDecimal getR28_yen() {
			return r28_yen;
		}

		public void setR28_yen(BigDecimal r28_yen) {
			this.r28_yen = r28_yen;
		}

		public BigDecimal getR28_c6() {
			return r28_c6;
		}

		public void setR28_c6(BigDecimal r28_c6) {
			this.r28_c6 = r28_c6;
		}

		public BigDecimal getR28_c7() {
			return r28_c7;
		}

		public void setR28_c7(BigDecimal r28_c7) {
			this.r28_c7 = r28_c7;
		}

		public BigDecimal getR28_c8() {
			return r28_c8;
		}

		public void setR28_c8(BigDecimal r28_c8) {
			this.r28_c8 = r28_c8;
		}

		public BigDecimal getR28_total() {
			return r28_total;
		}

		public void setR28_total(BigDecimal r28_total) {
			this.r28_total = r28_total;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_usd() {
			return r29_usd;
		}

		public void setR29_usd(BigDecimal r29_usd) {
			this.r29_usd = r29_usd;
		}

		public BigDecimal getR29_zar() {
			return r29_zar;
		}

		public void setR29_zar(BigDecimal r29_zar) {
			this.r29_zar = r29_zar;
		}

		public BigDecimal getR29_gbp() {
			return r29_gbp;
		}

		public void setR29_gbp(BigDecimal r29_gbp) {
			this.r29_gbp = r29_gbp;
		}

		public BigDecimal getR29_euro() {
			return r29_euro;
		}

		public void setR29_euro(BigDecimal r29_euro) {
			this.r29_euro = r29_euro;
		}

		public BigDecimal getR29_yen() {
			return r29_yen;
		}

		public void setR29_yen(BigDecimal r29_yen) {
			this.r29_yen = r29_yen;
		}

		public BigDecimal getR29_c6() {
			return r29_c6;
		}

		public void setR29_c6(BigDecimal r29_c6) {
			this.r29_c6 = r29_c6;
		}

		public BigDecimal getR29_c7() {
			return r29_c7;
		}

		public void setR29_c7(BigDecimal r29_c7) {
			this.r29_c7 = r29_c7;
		}

		public BigDecimal getR29_c8() {
			return r29_c8;
		}

		public void setR29_c8(BigDecimal r29_c8) {
			this.r29_c8 = r29_c8;
		}

		public BigDecimal getR29_total() {
			return r29_total;
		}

		public void setR29_total(BigDecimal r29_total) {
			this.r29_total = r29_total;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_usd() {
			return r30_usd;
		}

		public void setR30_usd(BigDecimal r30_usd) {
			this.r30_usd = r30_usd;
		}

		public BigDecimal getR30_zar() {
			return r30_zar;
		}

		public void setR30_zar(BigDecimal r30_zar) {
			this.r30_zar = r30_zar;
		}

		public BigDecimal getR30_gbp() {
			return r30_gbp;
		}

		public void setR30_gbp(BigDecimal r30_gbp) {
			this.r30_gbp = r30_gbp;
		}

		public BigDecimal getR30_euro() {
			return r30_euro;
		}

		public void setR30_euro(BigDecimal r30_euro) {
			this.r30_euro = r30_euro;
		}

		public BigDecimal getR30_yen() {
			return r30_yen;
		}

		public void setR30_yen(BigDecimal r30_yen) {
			this.r30_yen = r30_yen;
		}

		public BigDecimal getR30_c6() {
			return r30_c6;
		}

		public void setR30_c6(BigDecimal r30_c6) {
			this.r30_c6 = r30_c6;
		}

		public BigDecimal getR30_c7() {
			return r30_c7;
		}

		public void setR30_c7(BigDecimal r30_c7) {
			this.r30_c7 = r30_c7;
		}

		public BigDecimal getR30_c8() {
			return r30_c8;
		}

		public void setR30_c8(BigDecimal r30_c8) {
			this.r30_c8 = r30_c8;
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

		public BigDecimal getR31_usd() {
			return r31_usd;
		}

		public void setR31_usd(BigDecimal r31_usd) {
			this.r31_usd = r31_usd;
		}

		public BigDecimal getR31_zar() {
			return r31_zar;
		}

		public void setR31_zar(BigDecimal r31_zar) {
			this.r31_zar = r31_zar;
		}

		public BigDecimal getR31_gbp() {
			return r31_gbp;
		}

		public void setR31_gbp(BigDecimal r31_gbp) {
			this.r31_gbp = r31_gbp;
		}

		public BigDecimal getR31_euro() {
			return r31_euro;
		}

		public void setR31_euro(BigDecimal r31_euro) {
			this.r31_euro = r31_euro;
		}

		public BigDecimal getR31_yen() {
			return r31_yen;
		}

		public void setR31_yen(BigDecimal r31_yen) {
			this.r31_yen = r31_yen;
		}

		public BigDecimal getR31_c6() {
			return r31_c6;
		}

		public void setR31_c6(BigDecimal r31_c6) {
			this.r31_c6 = r31_c6;
		}

		public BigDecimal getR31_c7() {
			return r31_c7;
		}

		public void setR31_c7(BigDecimal r31_c7) {
			this.r31_c7 = r31_c7;
		}

		public BigDecimal getR31_c8() {
			return r31_c8;
		}

		public void setR31_c8(BigDecimal r31_c8) {
			this.r31_c8 = r31_c8;
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

		public BigDecimal getR32_usd() {
			return r32_usd;
		}

		public void setR32_usd(BigDecimal r32_usd) {
			this.r32_usd = r32_usd;
		}

		public BigDecimal getR32_zar() {
			return r32_zar;
		}

		public void setR32_zar(BigDecimal r32_zar) {
			this.r32_zar = r32_zar;
		}

		public BigDecimal getR32_gbp() {
			return r32_gbp;
		}

		public void setR32_gbp(BigDecimal r32_gbp) {
			this.r32_gbp = r32_gbp;
		}

		public BigDecimal getR32_euro() {
			return r32_euro;
		}

		public void setR32_euro(BigDecimal r32_euro) {
			this.r32_euro = r32_euro;
		}

		public BigDecimal getR32_yen() {
			return r32_yen;
		}

		public void setR32_yen(BigDecimal r32_yen) {
			this.r32_yen = r32_yen;
		}

		public BigDecimal getR32_c6() {
			return r32_c6;
		}

		public void setR32_c6(BigDecimal r32_c6) {
			this.r32_c6 = r32_c6;
		}

		public BigDecimal getR32_c7() {
			return r32_c7;
		}

		public void setR32_c7(BigDecimal r32_c7) {
			this.r32_c7 = r32_c7;
		}

		public BigDecimal getR32_c8() {
			return r32_c8;
		}

		public void setR32_c8(BigDecimal r32_c8) {
			this.r32_c8 = r32_c8;
		}

		public BigDecimal getR32_total() {
			return r32_total;
		}

		public void setR32_total(BigDecimal r32_total) {
			this.r32_total = r32_total;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_usd() {
			return r33_usd;
		}

		public void setR33_usd(BigDecimal r33_usd) {
			this.r33_usd = r33_usd;
		}

		public BigDecimal getR33_zar() {
			return r33_zar;
		}

		public void setR33_zar(BigDecimal r33_zar) {
			this.r33_zar = r33_zar;
		}

		public BigDecimal getR33_gbp() {
			return r33_gbp;
		}

		public void setR33_gbp(BigDecimal r33_gbp) {
			this.r33_gbp = r33_gbp;
		}

		public BigDecimal getR33_euro() {
			return r33_euro;
		}

		public void setR33_euro(BigDecimal r33_euro) {
			this.r33_euro = r33_euro;
		}

		public BigDecimal getR33_yen() {
			return r33_yen;
		}

		public void setR33_yen(BigDecimal r33_yen) {
			this.r33_yen = r33_yen;
		}

		public BigDecimal getR33_c6() {
			return r33_c6;
		}

		public void setR33_c6(BigDecimal r33_c6) {
			this.r33_c6 = r33_c6;
		}

		public BigDecimal getR33_c7() {
			return r33_c7;
		}

		public void setR33_c7(BigDecimal r33_c7) {
			this.r33_c7 = r33_c7;
		}

		public BigDecimal getR33_c8() {
			return r33_c8;
		}

		public void setR33_c8(BigDecimal r33_c8) {
			this.r33_c8 = r33_c8;
		}

		public BigDecimal getR33_total() {
			return r33_total;
		}

		public void setR33_total(BigDecimal r33_total) {
			this.r33_total = r33_total;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_usd() {
			return r34_usd;
		}

		public void setR34_usd(BigDecimal r34_usd) {
			this.r34_usd = r34_usd;
		}

		public BigDecimal getR34_zar() {
			return r34_zar;
		}

		public void setR34_zar(BigDecimal r34_zar) {
			this.r34_zar = r34_zar;
		}

		public BigDecimal getR34_gbp() {
			return r34_gbp;
		}

		public void setR34_gbp(BigDecimal r34_gbp) {
			this.r34_gbp = r34_gbp;
		}

		public BigDecimal getR34_euro() {
			return r34_euro;
		}

		public void setR34_euro(BigDecimal r34_euro) {
			this.r34_euro = r34_euro;
		}

		public BigDecimal getR34_yen() {
			return r34_yen;
		}

		public void setR34_yen(BigDecimal r34_yen) {
			this.r34_yen = r34_yen;
		}

		public BigDecimal getR34_c6() {
			return r34_c6;
		}

		public void setR34_c6(BigDecimal r34_c6) {
			this.r34_c6 = r34_c6;
		}

		public BigDecimal getR34_c7() {
			return r34_c7;
		}

		public void setR34_c7(BigDecimal r34_c7) {
			this.r34_c7 = r34_c7;
		}

		public BigDecimal getR34_c8() {
			return r34_c8;
		}

		public void setR34_c8(BigDecimal r34_c8) {
			this.r34_c8 = r34_c8;
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

		public BigDecimal getR35_usd() {
			return r35_usd;
		}

		public void setR35_usd(BigDecimal r35_usd) {
			this.r35_usd = r35_usd;
		}

		public BigDecimal getR35_zar() {
			return r35_zar;
		}

		public void setR35_zar(BigDecimal r35_zar) {
			this.r35_zar = r35_zar;
		}

		public BigDecimal getR35_gbp() {
			return r35_gbp;
		}

		public void setR35_gbp(BigDecimal r35_gbp) {
			this.r35_gbp = r35_gbp;
		}

		public BigDecimal getR35_euro() {
			return r35_euro;
		}

		public void setR35_euro(BigDecimal r35_euro) {
			this.r35_euro = r35_euro;
		}

		public BigDecimal getR35_yen() {
			return r35_yen;
		}

		public void setR35_yen(BigDecimal r35_yen) {
			this.r35_yen = r35_yen;
		}

		public BigDecimal getR35_c6() {
			return r35_c6;
		}

		public void setR35_c6(BigDecimal r35_c6) {
			this.r35_c6 = r35_c6;
		}

		public BigDecimal getR35_c7() {
			return r35_c7;
		}

		public void setR35_c7(BigDecimal r35_c7) {
			this.r35_c7 = r35_c7;
		}

		public BigDecimal getR35_c8() {
			return r35_c8;
		}

		public void setR35_c8(BigDecimal r35_c8) {
			this.r35_c8 = r35_c8;
		}

		public BigDecimal getR35_total() {
			return r35_total;
		}

		public void setR35_total(BigDecimal r35_total) {
			this.r35_total = r35_total;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_usd() {
			return r36_usd;
		}

		public void setR36_usd(BigDecimal r36_usd) {
			this.r36_usd = r36_usd;
		}

		public BigDecimal getR36_zar() {
			return r36_zar;
		}

		public void setR36_zar(BigDecimal r36_zar) {
			this.r36_zar = r36_zar;
		}

		public BigDecimal getR36_gbp() {
			return r36_gbp;
		}

		public void setR36_gbp(BigDecimal r36_gbp) {
			this.r36_gbp = r36_gbp;
		}

		public BigDecimal getR36_euro() {
			return r36_euro;
		}

		public void setR36_euro(BigDecimal r36_euro) {
			this.r36_euro = r36_euro;
		}

		public BigDecimal getR36_yen() {
			return r36_yen;
		}

		public void setR36_yen(BigDecimal r36_yen) {
			this.r36_yen = r36_yen;
		}

		public BigDecimal getR36_c6() {
			return r36_c6;
		}

		public void setR36_c6(BigDecimal r36_c6) {
			this.r36_c6 = r36_c6;
		}

		public BigDecimal getR36_c7() {
			return r36_c7;
		}

		public void setR36_c7(BigDecimal r36_c7) {
			this.r36_c7 = r36_c7;
		}

		public BigDecimal getR36_c8() {
			return r36_c8;
		}

		public void setR36_c8(BigDecimal r36_c8) {
			this.r36_c8 = r36_c8;
		}

		public BigDecimal getR36_total() {
			return r36_total;
		}

		public void setR36_total(BigDecimal r36_total) {
			this.r36_total = r36_total;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_usd() {
			return r37_usd;
		}

		public void setR37_usd(BigDecimal r37_usd) {
			this.r37_usd = r37_usd;
		}

		public BigDecimal getR37_zar() {
			return r37_zar;
		}

		public void setR37_zar(BigDecimal r37_zar) {
			this.r37_zar = r37_zar;
		}

		public BigDecimal getR37_gbp() {
			return r37_gbp;
		}

		public void setR37_gbp(BigDecimal r37_gbp) {
			this.r37_gbp = r37_gbp;
		}

		public BigDecimal getR37_euro() {
			return r37_euro;
		}

		public void setR37_euro(BigDecimal r37_euro) {
			this.r37_euro = r37_euro;
		}

		public BigDecimal getR37_yen() {
			return r37_yen;
		}

		public void setR37_yen(BigDecimal r37_yen) {
			this.r37_yen = r37_yen;
		}

		public BigDecimal getR37_c6() {
			return r37_c6;
		}

		public void setR37_c6(BigDecimal r37_c6) {
			this.r37_c6 = r37_c6;
		}

		public BigDecimal getR37_c7() {
			return r37_c7;
		}

		public void setR37_c7(BigDecimal r37_c7) {
			this.r37_c7 = r37_c7;
		}

		public BigDecimal getR37_c8() {
			return r37_c8;
		}

		public void setR37_c8(BigDecimal r37_c8) {
			this.r37_c8 = r37_c8;
		}

		public BigDecimal getR37_total() {
			return r37_total;
		}

		public void setR37_total(BigDecimal r37_total) {
			this.r37_total = r37_total;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_usd() {
			return r38_usd;
		}

		public void setR38_usd(BigDecimal r38_usd) {
			this.r38_usd = r38_usd;
		}

		public BigDecimal getR38_zar() {
			return r38_zar;
		}

		public void setR38_zar(BigDecimal r38_zar) {
			this.r38_zar = r38_zar;
		}

		public BigDecimal getR38_gbp() {
			return r38_gbp;
		}

		public void setR38_gbp(BigDecimal r38_gbp) {
			this.r38_gbp = r38_gbp;
		}

		public BigDecimal getR38_euro() {
			return r38_euro;
		}

		public void setR38_euro(BigDecimal r38_euro) {
			this.r38_euro = r38_euro;
		}

		public BigDecimal getR38_yen() {
			return r38_yen;
		}

		public void setR38_yen(BigDecimal r38_yen) {
			this.r38_yen = r38_yen;
		}

		public BigDecimal getR38_c6() {
			return r38_c6;
		}

		public void setR38_c6(BigDecimal r38_c6) {
			this.r38_c6 = r38_c6;
		}

		public BigDecimal getR38_c7() {
			return r38_c7;
		}

		public void setR38_c7(BigDecimal r38_c7) {
			this.r38_c7 = r38_c7;
		}

		public BigDecimal getR38_c8() {
			return r38_c8;
		}

		public void setR38_c8(BigDecimal r38_c8) {
			this.r38_c8 = r38_c8;
		}

		public BigDecimal getR38_total() {
			return r38_total;
		}

		public void setR38_total(BigDecimal r38_total) {
			this.r38_total = r38_total;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_usd() {
			return r39_usd;
		}

		public void setR39_usd(BigDecimal r39_usd) {
			this.r39_usd = r39_usd;
		}

		public BigDecimal getR39_zar() {
			return r39_zar;
		}

		public void setR39_zar(BigDecimal r39_zar) {
			this.r39_zar = r39_zar;
		}

		public BigDecimal getR39_gbp() {
			return r39_gbp;
		}

		public void setR39_gbp(BigDecimal r39_gbp) {
			this.r39_gbp = r39_gbp;
		}

		public BigDecimal getR39_euro() {
			return r39_euro;
		}

		public void setR39_euro(BigDecimal r39_euro) {
			this.r39_euro = r39_euro;
		}

		public BigDecimal getR39_yen() {
			return r39_yen;
		}

		public void setR39_yen(BigDecimal r39_yen) {
			this.r39_yen = r39_yen;
		}

		public BigDecimal getR39_c6() {
			return r39_c6;
		}

		public void setR39_c6(BigDecimal r39_c6) {
			this.r39_c6 = r39_c6;
		}

		public BigDecimal getR39_c7() {
			return r39_c7;
		}

		public void setR39_c7(BigDecimal r39_c7) {
			this.r39_c7 = r39_c7;
		}

		public BigDecimal getR39_c8() {
			return r39_c8;
		}

		public void setR39_c8(BigDecimal r39_c8) {
			this.r39_c8 = r39_c8;
		}

		public BigDecimal getR39_total() {
			return r39_total;
		}

		public void setR39_total(BigDecimal r39_total) {
			this.r39_total = r39_total;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_usd() {
			return r40_usd;
		}

		public void setR40_usd(BigDecimal r40_usd) {
			this.r40_usd = r40_usd;
		}

		public BigDecimal getR40_zar() {
			return r40_zar;
		}

		public void setR40_zar(BigDecimal r40_zar) {
			this.r40_zar = r40_zar;
		}

		public BigDecimal getR40_gbp() {
			return r40_gbp;
		}

		public void setR40_gbp(BigDecimal r40_gbp) {
			this.r40_gbp = r40_gbp;
		}

		public BigDecimal getR40_euro() {
			return r40_euro;
		}

		public void setR40_euro(BigDecimal r40_euro) {
			this.r40_euro = r40_euro;
		}

		public BigDecimal getR40_yen() {
			return r40_yen;
		}

		public void setR40_yen(BigDecimal r40_yen) {
			this.r40_yen = r40_yen;
		}

		public BigDecimal getR40_c6() {
			return r40_c6;
		}

		public void setR40_c6(BigDecimal r40_c6) {
			this.r40_c6 = r40_c6;
		}

		public BigDecimal getR40_c7() {
			return r40_c7;
		}

		public void setR40_c7(BigDecimal r40_c7) {
			this.r40_c7 = r40_c7;
		}

		public BigDecimal getR40_c8() {
			return r40_c8;
		}

		public void setR40_c8(BigDecimal r40_c8) {
			this.r40_c8 = r40_c8;
		}

		public BigDecimal getR40_total() {
			return r40_total;
		}

		public void setR40_total(BigDecimal r40_total) {
			this.r40_total = r40_total;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_usd() {
			return r41_usd;
		}

		public void setR41_usd(BigDecimal r41_usd) {
			this.r41_usd = r41_usd;
		}

		public BigDecimal getR41_zar() {
			return r41_zar;
		}

		public void setR41_zar(BigDecimal r41_zar) {
			this.r41_zar = r41_zar;
		}

		public BigDecimal getR41_gbp() {
			return r41_gbp;
		}

		public void setR41_gbp(BigDecimal r41_gbp) {
			this.r41_gbp = r41_gbp;
		}

		public BigDecimal getR41_euro() {
			return r41_euro;
		}

		public void setR41_euro(BigDecimal r41_euro) {
			this.r41_euro = r41_euro;
		}

		public BigDecimal getR41_yen() {
			return r41_yen;
		}

		public void setR41_yen(BigDecimal r41_yen) {
			this.r41_yen = r41_yen;
		}

		public BigDecimal getR41_c6() {
			return r41_c6;
		}

		public void setR41_c6(BigDecimal r41_c6) {
			this.r41_c6 = r41_c6;
		}

		public BigDecimal getR41_c7() {
			return r41_c7;
		}

		public void setR41_c7(BigDecimal r41_c7) {
			this.r41_c7 = r41_c7;
		}

		public BigDecimal getR41_c8() {
			return r41_c8;
		}

		public void setR41_c8(BigDecimal r41_c8) {
			this.r41_c8 = r41_c8;
		}

		public BigDecimal getR41_total() {
			return r41_total;
		}

		public void setR41_total(BigDecimal r41_total) {
			this.r41_total = r41_total;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_usd() {
			return r42_usd;
		}

		public void setR42_usd(BigDecimal r42_usd) {
			this.r42_usd = r42_usd;
		}

		public BigDecimal getR42_zar() {
			return r42_zar;
		}

		public void setR42_zar(BigDecimal r42_zar) {
			this.r42_zar = r42_zar;
		}

		public BigDecimal getR42_gbp() {
			return r42_gbp;
		}

		public void setR42_gbp(BigDecimal r42_gbp) {
			this.r42_gbp = r42_gbp;
		}

		public BigDecimal getR42_euro() {
			return r42_euro;
		}

		public void setR42_euro(BigDecimal r42_euro) {
			this.r42_euro = r42_euro;
		}

		public BigDecimal getR42_yen() {
			return r42_yen;
		}

		public void setR42_yen(BigDecimal r42_yen) {
			this.r42_yen = r42_yen;
		}

		public BigDecimal getR42_c6() {
			return r42_c6;
		}

		public void setR42_c6(BigDecimal r42_c6) {
			this.r42_c6 = r42_c6;
		}

		public BigDecimal getR42_c7() {
			return r42_c7;
		}

		public void setR42_c7(BigDecimal r42_c7) {
			this.r42_c7 = r42_c7;
		}

		public BigDecimal getR42_c8() {
			return r42_c8;
		}

		public void setR42_c8(BigDecimal r42_c8) {
			this.r42_c8 = r42_c8;
		}

		public BigDecimal getR42_total() {
			return r42_total;
		}

		public void setR42_total(BigDecimal r42_total) {
			this.r42_total = r42_total;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_usd() {
			return r43_usd;
		}

		public void setR43_usd(BigDecimal r43_usd) {
			this.r43_usd = r43_usd;
		}

		public BigDecimal getR43_zar() {
			return r43_zar;
		}

		public void setR43_zar(BigDecimal r43_zar) {
			this.r43_zar = r43_zar;
		}

		public BigDecimal getR43_gbp() {
			return r43_gbp;
		}

		public void setR43_gbp(BigDecimal r43_gbp) {
			this.r43_gbp = r43_gbp;
		}

		public BigDecimal getR43_euro() {
			return r43_euro;
		}

		public void setR43_euro(BigDecimal r43_euro) {
			this.r43_euro = r43_euro;
		}

		public BigDecimal getR43_yen() {
			return r43_yen;
		}

		public void setR43_yen(BigDecimal r43_yen) {
			this.r43_yen = r43_yen;
		}

		public BigDecimal getR43_c6() {
			return r43_c6;
		}

		public void setR43_c6(BigDecimal r43_c6) {
			this.r43_c6 = r43_c6;
		}

		public BigDecimal getR43_c7() {
			return r43_c7;
		}

		public void setR43_c7(BigDecimal r43_c7) {
			this.r43_c7 = r43_c7;
		}

		public BigDecimal getR43_c8() {
			return r43_c8;
		}

		public void setR43_c8(BigDecimal r43_c8) {
			this.r43_c8 = r43_c8;
		}

		public BigDecimal getR43_total() {
			return r43_total;
		}

		public void setR43_total(BigDecimal r43_total) {
			this.r43_total = r43_total;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_usd() {
			return r44_usd;
		}

		public void setR44_usd(BigDecimal r44_usd) {
			this.r44_usd = r44_usd;
		}

		public BigDecimal getR44_zar() {
			return r44_zar;
		}

		public void setR44_zar(BigDecimal r44_zar) {
			this.r44_zar = r44_zar;
		}

		public BigDecimal getR44_gbp() {
			return r44_gbp;
		}

		public void setR44_gbp(BigDecimal r44_gbp) {
			this.r44_gbp = r44_gbp;
		}

		public BigDecimal getR44_euro() {
			return r44_euro;
		}

		public void setR44_euro(BigDecimal r44_euro) {
			this.r44_euro = r44_euro;
		}

		public BigDecimal getR44_yen() {
			return r44_yen;
		}

		public void setR44_yen(BigDecimal r44_yen) {
			this.r44_yen = r44_yen;
		}

		public BigDecimal getR44_c6() {
			return r44_c6;
		}

		public void setR44_c6(BigDecimal r44_c6) {
			this.r44_c6 = r44_c6;
		}

		public BigDecimal getR44_c7() {
			return r44_c7;
		}

		public void setR44_c7(BigDecimal r44_c7) {
			this.r44_c7 = r44_c7;
		}

		public BigDecimal getR44_c8() {
			return r44_c8;
		}

		public void setR44_c8(BigDecimal r44_c8) {
			this.r44_c8 = r44_c8;
		}

		public BigDecimal getR44_total() {
			return r44_total;
		}

		public void setR44_total(BigDecimal r44_total) {
			this.r44_total = r44_total;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_usd() {
			return r45_usd;
		}

		public void setR45_usd(BigDecimal r45_usd) {
			this.r45_usd = r45_usd;
		}

		public BigDecimal getR45_zar() {
			return r45_zar;
		}

		public void setR45_zar(BigDecimal r45_zar) {
			this.r45_zar = r45_zar;
		}

		public BigDecimal getR45_gbp() {
			return r45_gbp;
		}

		public void setR45_gbp(BigDecimal r45_gbp) {
			this.r45_gbp = r45_gbp;
		}

		public BigDecimal getR45_euro() {
			return r45_euro;
		}

		public void setR45_euro(BigDecimal r45_euro) {
			this.r45_euro = r45_euro;
		}

		public BigDecimal getR45_yen() {
			return r45_yen;
		}

		public void setR45_yen(BigDecimal r45_yen) {
			this.r45_yen = r45_yen;
		}

		public BigDecimal getR45_c6() {
			return r45_c6;
		}

		public void setR45_c6(BigDecimal r45_c6) {
			this.r45_c6 = r45_c6;
		}

		public BigDecimal getR45_c7() {
			return r45_c7;
		}

		public void setR45_c7(BigDecimal r45_c7) {
			this.r45_c7 = r45_c7;
		}

		public BigDecimal getR45_c8() {
			return r45_c8;
		}

		public void setR45_c8(BigDecimal r45_c8) {
			this.r45_c8 = r45_c8;
		}

		public BigDecimal getR45_total() {
			return r45_total;
		}

		public void setR45_total(BigDecimal r45_total) {
			this.r45_total = r45_total;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_usd() {
			return r46_usd;
		}

		public void setR46_usd(BigDecimal r46_usd) {
			this.r46_usd = r46_usd;
		}

		public BigDecimal getR46_zar() {
			return r46_zar;
		}

		public void setR46_zar(BigDecimal r46_zar) {
			this.r46_zar = r46_zar;
		}

		public BigDecimal getR46_gbp() {
			return r46_gbp;
		}

		public void setR46_gbp(BigDecimal r46_gbp) {
			this.r46_gbp = r46_gbp;
		}

		public BigDecimal getR46_euro() {
			return r46_euro;
		}

		public void setR46_euro(BigDecimal r46_euro) {
			this.r46_euro = r46_euro;
		}

		public BigDecimal getR46_yen() {
			return r46_yen;
		}

		public void setR46_yen(BigDecimal r46_yen) {
			this.r46_yen = r46_yen;
		}

		public BigDecimal getR46_c6() {
			return r46_c6;
		}

		public void setR46_c6(BigDecimal r46_c6) {
			this.r46_c6 = r46_c6;
		}

		public BigDecimal getR46_c7() {
			return r46_c7;
		}

		public void setR46_c7(BigDecimal r46_c7) {
			this.r46_c7 = r46_c7;
		}

		public BigDecimal getR46_c8() {
			return r46_c8;
		}

		public void setR46_c8(BigDecimal r46_c8) {
			this.r46_c8 = r46_c8;
		}

		public BigDecimal getR46_total() {
			return r46_total;
		}

		public void setR46_total(BigDecimal r46_total) {
			this.r46_total = r46_total;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_usd() {
			return r47_usd;
		}

		public void setR47_usd(BigDecimal r47_usd) {
			this.r47_usd = r47_usd;
		}

		public BigDecimal getR47_zar() {
			return r47_zar;
		}

		public void setR47_zar(BigDecimal r47_zar) {
			this.r47_zar = r47_zar;
		}

		public BigDecimal getR47_gbp() {
			return r47_gbp;
		}

		public void setR47_gbp(BigDecimal r47_gbp) {
			this.r47_gbp = r47_gbp;
		}

		public BigDecimal getR47_euro() {
			return r47_euro;
		}

		public void setR47_euro(BigDecimal r47_euro) {
			this.r47_euro = r47_euro;
		}

		public BigDecimal getR47_yen() {
			return r47_yen;
		}

		public void setR47_yen(BigDecimal r47_yen) {
			this.r47_yen = r47_yen;
		}

		public BigDecimal getR47_c6() {
			return r47_c6;
		}

		public void setR47_c6(BigDecimal r47_c6) {
			this.r47_c6 = r47_c6;
		}

		public BigDecimal getR47_c7() {
			return r47_c7;
		}

		public void setR47_c7(BigDecimal r47_c7) {
			this.r47_c7 = r47_c7;
		}

		public BigDecimal getR47_c8() {
			return r47_c8;
		}

		public void setR47_c8(BigDecimal r47_c8) {
			this.r47_c8 = r47_c8;
		}

		public BigDecimal getR47_total() {
			return r47_total;
		}

		public void setR47_total(BigDecimal r47_total) {
			this.r47_total = r47_total;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_usd() {
			return r48_usd;
		}

		public void setR48_usd(BigDecimal r48_usd) {
			this.r48_usd = r48_usd;
		}

		public BigDecimal getR48_zar() {
			return r48_zar;
		}

		public void setR48_zar(BigDecimal r48_zar) {
			this.r48_zar = r48_zar;
		}

		public BigDecimal getR48_gbp() {
			return r48_gbp;
		}

		public void setR48_gbp(BigDecimal r48_gbp) {
			this.r48_gbp = r48_gbp;
		}

		public BigDecimal getR48_euro() {
			return r48_euro;
		}

		public void setR48_euro(BigDecimal r48_euro) {
			this.r48_euro = r48_euro;
		}

		public BigDecimal getR48_yen() {
			return r48_yen;
		}

		public void setR48_yen(BigDecimal r48_yen) {
			this.r48_yen = r48_yen;
		}

		public BigDecimal getR48_c6() {
			return r48_c6;
		}

		public void setR48_c6(BigDecimal r48_c6) {
			this.r48_c6 = r48_c6;
		}

		public BigDecimal getR48_c7() {
			return r48_c7;
		}

		public void setR48_c7(BigDecimal r48_c7) {
			this.r48_c7 = r48_c7;
		}

		public BigDecimal getR48_c8() {
			return r48_c8;
		}

		public void setR48_c8(BigDecimal r48_c8) {
			this.r48_c8 = r48_c8;
		}

		public BigDecimal getR48_total() {
			return r48_total;
		}

		public void setR48_total(BigDecimal r48_total) {
			this.r48_total = r48_total;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_usd() {
			return r49_usd;
		}

		public void setR49_usd(BigDecimal r49_usd) {
			this.r49_usd = r49_usd;
		}

		public BigDecimal getR49_zar() {
			return r49_zar;
		}

		public void setR49_zar(BigDecimal r49_zar) {
			this.r49_zar = r49_zar;
		}

		public BigDecimal getR49_gbp() {
			return r49_gbp;
		}

		public void setR49_gbp(BigDecimal r49_gbp) {
			this.r49_gbp = r49_gbp;
		}

		public BigDecimal getR49_euro() {
			return r49_euro;
		}

		public void setR49_euro(BigDecimal r49_euro) {
			this.r49_euro = r49_euro;
		}

		public BigDecimal getR49_yen() {
			return r49_yen;
		}

		public void setR49_yen(BigDecimal r49_yen) {
			this.r49_yen = r49_yen;
		}

		public BigDecimal getR49_c6() {
			return r49_c6;
		}

		public void setR49_c6(BigDecimal r49_c6) {
			this.r49_c6 = r49_c6;
		}

		public BigDecimal getR49_c7() {
			return r49_c7;
		}

		public void setR49_c7(BigDecimal r49_c7) {
			this.r49_c7 = r49_c7;
		}

		public BigDecimal getR49_c8() {
			return r49_c8;
		}

		public void setR49_c8(BigDecimal r49_c8) {
			this.r49_c8 = r49_c8;
		}

		public BigDecimal getR49_total() {
			return r49_total;
		}

		public void setR49_total(BigDecimal r49_total) {
			this.r49_total = r49_total;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_usd() {
			return r50_usd;
		}

		public void setR50_usd(BigDecimal r50_usd) {
			this.r50_usd = r50_usd;
		}

		public BigDecimal getR50_zar() {
			return r50_zar;
		}

		public void setR50_zar(BigDecimal r50_zar) {
			this.r50_zar = r50_zar;
		}

		public BigDecimal getR50_gbp() {
			return r50_gbp;
		}

		public void setR50_gbp(BigDecimal r50_gbp) {
			this.r50_gbp = r50_gbp;
		}

		public BigDecimal getR50_euro() {
			return r50_euro;
		}

		public void setR50_euro(BigDecimal r50_euro) {
			this.r50_euro = r50_euro;
		}

		public BigDecimal getR50_yen() {
			return r50_yen;
		}

		public void setR50_yen(BigDecimal r50_yen) {
			this.r50_yen = r50_yen;
		}

		public BigDecimal getR50_c6() {
			return r50_c6;
		}

		public void setR50_c6(BigDecimal r50_c6) {
			this.r50_c6 = r50_c6;
		}

		public BigDecimal getR50_c7() {
			return r50_c7;
		}

		public void setR50_c7(BigDecimal r50_c7) {
			this.r50_c7 = r50_c7;
		}

		public BigDecimal getR50_c8() {
			return r50_c8;
		}

		public void setR50_c8(BigDecimal r50_c8) {
			this.r50_c8 = r50_c8;
		}

		public BigDecimal getR50_total() {
			return r50_total;
		}

		public void setR50_total(BigDecimal r50_total) {
			this.r50_total = r50_total;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public BigDecimal getR51_usd() {
			return r51_usd;
		}

		public void setR51_usd(BigDecimal r51_usd) {
			this.r51_usd = r51_usd;
		}

		public BigDecimal getR51_zar() {
			return r51_zar;
		}

		public void setR51_zar(BigDecimal r51_zar) {
			this.r51_zar = r51_zar;
		}

		public BigDecimal getR51_gbp() {
			return r51_gbp;
		}

		public void setR51_gbp(BigDecimal r51_gbp) {
			this.r51_gbp = r51_gbp;
		}

		public BigDecimal getR51_euro() {
			return r51_euro;
		}

		public void setR51_euro(BigDecimal r51_euro) {
			this.r51_euro = r51_euro;
		}

		public BigDecimal getR51_yen() {
			return r51_yen;
		}

		public void setR51_yen(BigDecimal r51_yen) {
			this.r51_yen = r51_yen;
		}

		public BigDecimal getR51_c6() {
			return r51_c6;
		}

		public void setR51_c6(BigDecimal r51_c6) {
			this.r51_c6 = r51_c6;
		}

		public BigDecimal getR51_c7() {
			return r51_c7;
		}

		public void setR51_c7(BigDecimal r51_c7) {
			this.r51_c7 = r51_c7;
		}

		public BigDecimal getR51_c8() {
			return r51_c8;
		}

		public void setR51_c8(BigDecimal r51_c8) {
			this.r51_c8 = r51_c8;
		}

		public BigDecimal getR51_total() {
			return r51_total;
		}

		public void setR51_total(BigDecimal r51_total) {
			this.r51_total = r51_total;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public BigDecimal getR52_usd() {
			return r52_usd;
		}

		public void setR52_usd(BigDecimal r52_usd) {
			this.r52_usd = r52_usd;
		}

		public BigDecimal getR52_zar() {
			return r52_zar;
		}

		public void setR52_zar(BigDecimal r52_zar) {
			this.r52_zar = r52_zar;
		}

		public BigDecimal getR52_gbp() {
			return r52_gbp;
		}

		public void setR52_gbp(BigDecimal r52_gbp) {
			this.r52_gbp = r52_gbp;
		}

		public BigDecimal getR52_euro() {
			return r52_euro;
		}

		public void setR52_euro(BigDecimal r52_euro) {
			this.r52_euro = r52_euro;
		}

		public BigDecimal getR52_yen() {
			return r52_yen;
		}

		public void setR52_yen(BigDecimal r52_yen) {
			this.r52_yen = r52_yen;
		}

		public BigDecimal getR52_c6() {
			return r52_c6;
		}

		public void setR52_c6(BigDecimal r52_c6) {
			this.r52_c6 = r52_c6;
		}

		public BigDecimal getR52_c7() {
			return r52_c7;
		}

		public void setR52_c7(BigDecimal r52_c7) {
			this.r52_c7 = r52_c7;
		}

		public BigDecimal getR52_c8() {
			return r52_c8;
		}

		public void setR52_c8(BigDecimal r52_c8) {
			this.r52_c8 = r52_c8;
		}

		public BigDecimal getR52_total() {
			return r52_total;
		}

		public void setR52_total(BigDecimal r52_total) {
			this.r52_total = r52_total;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public BigDecimal getR53_usd() {
			return r53_usd;
		}

		public void setR53_usd(BigDecimal r53_usd) {
			this.r53_usd = r53_usd;
		}

		public BigDecimal getR53_zar() {
			return r53_zar;
		}

		public void setR53_zar(BigDecimal r53_zar) {
			this.r53_zar = r53_zar;
		}

		public BigDecimal getR53_gbp() {
			return r53_gbp;
		}

		public void setR53_gbp(BigDecimal r53_gbp) {
			this.r53_gbp = r53_gbp;
		}

		public BigDecimal getR53_euro() {
			return r53_euro;
		}

		public void setR53_euro(BigDecimal r53_euro) {
			this.r53_euro = r53_euro;
		}

		public BigDecimal getR53_yen() {
			return r53_yen;
		}

		public void setR53_yen(BigDecimal r53_yen) {
			this.r53_yen = r53_yen;
		}

		public BigDecimal getR53_c6() {
			return r53_c6;
		}

		public void setR53_c6(BigDecimal r53_c6) {
			this.r53_c6 = r53_c6;
		}

		public BigDecimal getR53_c7() {
			return r53_c7;
		}

		public void setR53_c7(BigDecimal r53_c7) {
			this.r53_c7 = r53_c7;
		}

		public BigDecimal getR53_c8() {
			return r53_c8;
		}

		public void setR53_c8(BigDecimal r53_c8) {
			this.r53_c8 = r53_c8;
		}

		public BigDecimal getR53_total() {
			return r53_total;
		}

		public void setR53_total(BigDecimal r53_total) {
			this.r53_total = r53_total;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public BigDecimal getR54_usd() {
			return r54_usd;
		}

		public void setR54_usd(BigDecimal r54_usd) {
			this.r54_usd = r54_usd;
		}

		public BigDecimal getR54_zar() {
			return r54_zar;
		}

		public void setR54_zar(BigDecimal r54_zar) {
			this.r54_zar = r54_zar;
		}

		public BigDecimal getR54_gbp() {
			return r54_gbp;
		}

		public void setR54_gbp(BigDecimal r54_gbp) {
			this.r54_gbp = r54_gbp;
		}

		public BigDecimal getR54_euro() {
			return r54_euro;
		}

		public void setR54_euro(BigDecimal r54_euro) {
			this.r54_euro = r54_euro;
		}

		public BigDecimal getR54_yen() {
			return r54_yen;
		}

		public void setR54_yen(BigDecimal r54_yen) {
			this.r54_yen = r54_yen;
		}

		public BigDecimal getR54_c6() {
			return r54_c6;
		}

		public void setR54_c6(BigDecimal r54_c6) {
			this.r54_c6 = r54_c6;
		}

		public BigDecimal getR54_c7() {
			return r54_c7;
		}

		public void setR54_c7(BigDecimal r54_c7) {
			this.r54_c7 = r54_c7;
		}

		public BigDecimal getR54_c8() {
			return r54_c8;
		}

		public void setR54_c8(BigDecimal r54_c8) {
			this.r54_c8 = r54_c8;
		}

		public BigDecimal getR54_total() {
			return r54_total;
		}

		public void setR54_total(BigDecimal r54_total) {
			this.r54_total = r54_total;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public BigDecimal getR55_usd() {
			return r55_usd;
		}

		public void setR55_usd(BigDecimal r55_usd) {
			this.r55_usd = r55_usd;
		}

		public BigDecimal getR55_zar() {
			return r55_zar;
		}

		public void setR55_zar(BigDecimal r55_zar) {
			this.r55_zar = r55_zar;
		}

		public BigDecimal getR55_gbp() {
			return r55_gbp;
		}

		public void setR55_gbp(BigDecimal r55_gbp) {
			this.r55_gbp = r55_gbp;
		}

		public BigDecimal getR55_euro() {
			return r55_euro;
		}

		public void setR55_euro(BigDecimal r55_euro) {
			this.r55_euro = r55_euro;
		}

		public BigDecimal getR55_yen() {
			return r55_yen;
		}

		public void setR55_yen(BigDecimal r55_yen) {
			this.r55_yen = r55_yen;
		}

		public BigDecimal getR55_c6() {
			return r55_c6;
		}

		public void setR55_c6(BigDecimal r55_c6) {
			this.r55_c6 = r55_c6;
		}

		public BigDecimal getR55_c7() {
			return r55_c7;
		}

		public void setR55_c7(BigDecimal r55_c7) {
			this.r55_c7 = r55_c7;
		}

		public BigDecimal getR55_c8() {
			return r55_c8;
		}

		public void setR55_c8(BigDecimal r55_c8) {
			this.r55_c8 = r55_c8;
		}

		public BigDecimal getR55_total() {
			return r55_total;
		}

		public void setR55_total(BigDecimal r55_total) {
			this.r55_total = r55_total;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public BigDecimal getR56_usd() {
			return r56_usd;
		}

		public void setR56_usd(BigDecimal r56_usd) {
			this.r56_usd = r56_usd;
		}

		public BigDecimal getR56_zar() {
			return r56_zar;
		}

		public void setR56_zar(BigDecimal r56_zar) {
			this.r56_zar = r56_zar;
		}

		public BigDecimal getR56_gbp() {
			return r56_gbp;
		}

		public void setR56_gbp(BigDecimal r56_gbp) {
			this.r56_gbp = r56_gbp;
		}

		public BigDecimal getR56_euro() {
			return r56_euro;
		}

		public void setR56_euro(BigDecimal r56_euro) {
			this.r56_euro = r56_euro;
		}

		public BigDecimal getR56_yen() {
			return r56_yen;
		}

		public void setR56_yen(BigDecimal r56_yen) {
			this.r56_yen = r56_yen;
		}

		public BigDecimal getR56_c6() {
			return r56_c6;
		}

		public void setR56_c6(BigDecimal r56_c6) {
			this.r56_c6 = r56_c6;
		}

		public BigDecimal getR56_c7() {
			return r56_c7;
		}

		public void setR56_c7(BigDecimal r56_c7) {
			this.r56_c7 = r56_c7;
		}

		public BigDecimal getR56_c8() {
			return r56_c8;
		}

		public void setR56_c8(BigDecimal r56_c8) {
			this.r56_c8 = r56_c8;
		}

		public BigDecimal getR56_total() {
			return r56_total;
		}

		public void setR56_total(BigDecimal r56_total) {
			this.r56_total = r56_total;
		}

		public String getR57_product() {
			return r57_product;
		}

		public void setR57_product(String r57_product) {
			this.r57_product = r57_product;
		}

		public BigDecimal getR57_usd() {
			return r57_usd;
		}

		public void setR57_usd(BigDecimal r57_usd) {
			this.r57_usd = r57_usd;
		}

		public BigDecimal getR57_zar() {
			return r57_zar;
		}

		public void setR57_zar(BigDecimal r57_zar) {
			this.r57_zar = r57_zar;
		}

		public BigDecimal getR57_gbp() {
			return r57_gbp;
		}

		public void setR57_gbp(BigDecimal r57_gbp) {
			this.r57_gbp = r57_gbp;
		}

		public BigDecimal getR57_euro() {
			return r57_euro;
		}

		public void setR57_euro(BigDecimal r57_euro) {
			this.r57_euro = r57_euro;
		}

		public BigDecimal getR57_yen() {
			return r57_yen;
		}

		public void setR57_yen(BigDecimal r57_yen) {
			this.r57_yen = r57_yen;
		}

		public BigDecimal getR57_c6() {
			return r57_c6;
		}

		public void setR57_c6(BigDecimal r57_c6) {
			this.r57_c6 = r57_c6;
		}

		public BigDecimal getR57_c7() {
			return r57_c7;
		}

		public void setR57_c7(BigDecimal r57_c7) {
			this.r57_c7 = r57_c7;
		}

		public BigDecimal getR57_c8() {
			return r57_c8;
		}

		public void setR57_c8(BigDecimal r57_c8) {
			this.r57_c8 = r57_c8;
		}

		public BigDecimal getR57_total() {
			return r57_total;
		}

		public void setR57_total(BigDecimal r57_total) {
			this.r57_total = r57_total;
		}

		public String getR58_product() {
			return r58_product;
		}

		public void setR58_product(String r58_product) {
			this.r58_product = r58_product;
		}

		public BigDecimal getR58_usd() {
			return r58_usd;
		}

		public void setR58_usd(BigDecimal r58_usd) {
			this.r58_usd = r58_usd;
		}

		public BigDecimal getR58_zar() {
			return r58_zar;
		}

		public void setR58_zar(BigDecimal r58_zar) {
			this.r58_zar = r58_zar;
		}

		public BigDecimal getR58_gbp() {
			return r58_gbp;
		}

		public void setR58_gbp(BigDecimal r58_gbp) {
			this.r58_gbp = r58_gbp;
		}

		public BigDecimal getR58_euro() {
			return r58_euro;
		}

		public void setR58_euro(BigDecimal r58_euro) {
			this.r58_euro = r58_euro;
		}

		public BigDecimal getR58_yen() {
			return r58_yen;
		}

		public void setR58_yen(BigDecimal r58_yen) {
			this.r58_yen = r58_yen;
		}

		public BigDecimal getR58_c6() {
			return r58_c6;
		}

		public void setR58_c6(BigDecimal r58_c6) {
			this.r58_c6 = r58_c6;
		}

		public BigDecimal getR58_c7() {
			return r58_c7;
		}

		public void setR58_c7(BigDecimal r58_c7) {
			this.r58_c7 = r58_c7;
		}

		public BigDecimal getR58_c8() {
			return r58_c8;
		}

		public void setR58_c8(BigDecimal r58_c8) {
			this.r58_c8 = r58_c8;
		}

		public BigDecimal getR58_total() {
			return r58_total;
		}

		public void setR58_total(BigDecimal r58_total) {
			this.r58_total = r58_total;
		}

		public String getR59_product() {
			return r59_product;
		}

		public void setR59_product(String r59_product) {
			this.r59_product = r59_product;
		}

		public BigDecimal getR59_usd() {
			return r59_usd;
		}

		public void setR59_usd(BigDecimal r59_usd) {
			this.r59_usd = r59_usd;
		}

		public BigDecimal getR59_zar() {
			return r59_zar;
		}

		public void setR59_zar(BigDecimal r59_zar) {
			this.r59_zar = r59_zar;
		}

		public BigDecimal getR59_gbp() {
			return r59_gbp;
		}

		public void setR59_gbp(BigDecimal r59_gbp) {
			this.r59_gbp = r59_gbp;
		}

		public BigDecimal getR59_euro() {
			return r59_euro;
		}

		public void setR59_euro(BigDecimal r59_euro) {
			this.r59_euro = r59_euro;
		}

		public BigDecimal getR59_yen() {
			return r59_yen;
		}

		public void setR59_yen(BigDecimal r59_yen) {
			this.r59_yen = r59_yen;
		}

		public BigDecimal getR59_c6() {
			return r59_c6;
		}

		public void setR59_c6(BigDecimal r59_c6) {
			this.r59_c6 = r59_c6;
		}

		public BigDecimal getR59_c7() {
			return r59_c7;
		}

		public void setR59_c7(BigDecimal r59_c7) {
			this.r59_c7 = r59_c7;
		}

		public BigDecimal getR59_c8() {
			return r59_c8;
		}

		public void setR59_c8(BigDecimal r59_c8) {
			this.r59_c8 = r59_c8;
		}

		public BigDecimal getR59_total() {
			return r59_total;
		}

		public void setR59_total(BigDecimal r59_total) {
			this.r59_total = r59_total;
		}

		public String getR60_product() {
			return r60_product;
		}

		public void setR60_product(String r60_product) {
			this.r60_product = r60_product;
		}

		public BigDecimal getR60_usd() {
			return r60_usd;
		}

		public void setR60_usd(BigDecimal r60_usd) {
			this.r60_usd = r60_usd;
		}

		public BigDecimal getR60_zar() {
			return r60_zar;
		}

		public void setR60_zar(BigDecimal r60_zar) {
			this.r60_zar = r60_zar;
		}

		public BigDecimal getR60_gbp() {
			return r60_gbp;
		}

		public void setR60_gbp(BigDecimal r60_gbp) {
			this.r60_gbp = r60_gbp;
		}

		public BigDecimal getR60_euro() {
			return r60_euro;
		}

		public void setR60_euro(BigDecimal r60_euro) {
			this.r60_euro = r60_euro;
		}

		public BigDecimal getR60_yen() {
			return r60_yen;
		}

		public void setR60_yen(BigDecimal r60_yen) {
			this.r60_yen = r60_yen;
		}

		public BigDecimal getR60_c6() {
			return r60_c6;
		}

		public void setR60_c6(BigDecimal r60_c6) {
			this.r60_c6 = r60_c6;
		}

		public BigDecimal getR60_c7() {
			return r60_c7;
		}

		public void setR60_c7(BigDecimal r60_c7) {
			this.r60_c7 = r60_c7;
		}

		public BigDecimal getR60_c8() {
			return r60_c8;
		}

		public void setR60_c8(BigDecimal r60_c8) {
			this.r60_c8 = r60_c8;
		}

		public BigDecimal getR60_total() {
			return r60_total;
		}

		public void setR60_total(BigDecimal r60_total) {
			this.r60_total = r60_total;
		}

		public String getR61_product() {
			return r61_product;
		}

		public void setR61_product(String r61_product) {
			this.r61_product = r61_product;
		}

		public BigDecimal getR61_usd() {
			return r61_usd;
		}

		public void setR61_usd(BigDecimal r61_usd) {
			this.r61_usd = r61_usd;
		}

		public BigDecimal getR61_zar() {
			return r61_zar;
		}

		public void setR61_zar(BigDecimal r61_zar) {
			this.r61_zar = r61_zar;
		}

		public BigDecimal getR61_gbp() {
			return r61_gbp;
		}

		public void setR61_gbp(BigDecimal r61_gbp) {
			this.r61_gbp = r61_gbp;
		}

		public BigDecimal getR61_euro() {
			return r61_euro;
		}

		public void setR61_euro(BigDecimal r61_euro) {
			this.r61_euro = r61_euro;
		}

		public BigDecimal getR61_yen() {
			return r61_yen;
		}

		public void setR61_yen(BigDecimal r61_yen) {
			this.r61_yen = r61_yen;
		}

		public BigDecimal getR61_c6() {
			return r61_c6;
		}

		public void setR61_c6(BigDecimal r61_c6) {
			this.r61_c6 = r61_c6;
		}

		public BigDecimal getR61_c7() {
			return r61_c7;
		}

		public void setR61_c7(BigDecimal r61_c7) {
			this.r61_c7 = r61_c7;
		}

		public BigDecimal getR61_c8() {
			return r61_c8;
		}

		public void setR61_c8(BigDecimal r61_c8) {
			this.r61_c8 = r61_c8;
		}

		public BigDecimal getR61_total() {
			return r61_total;
		}

		public void setR61_total(BigDecimal r61_total) {
			this.r61_total = r61_total;
		}

		public String getR62_product() {
			return r62_product;
		}

		public void setR62_product(String r62_product) {
			this.r62_product = r62_product;
		}

		public BigDecimal getR62_usd() {
			return r62_usd;
		}

		public void setR62_usd(BigDecimal r62_usd) {
			this.r62_usd = r62_usd;
		}

		public BigDecimal getR62_zar() {
			return r62_zar;
		}

		public void setR62_zar(BigDecimal r62_zar) {
			this.r62_zar = r62_zar;
		}

		public BigDecimal getR62_gbp() {
			return r62_gbp;
		}

		public void setR62_gbp(BigDecimal r62_gbp) {
			this.r62_gbp = r62_gbp;
		}

		public BigDecimal getR62_euro() {
			return r62_euro;
		}

		public void setR62_euro(BigDecimal r62_euro) {
			this.r62_euro = r62_euro;
		}

		public BigDecimal getR62_yen() {
			return r62_yen;
		}

		public void setR62_yen(BigDecimal r62_yen) {
			this.r62_yen = r62_yen;
		}

		public BigDecimal getR62_c6() {
			return r62_c6;
		}

		public void setR62_c6(BigDecimal r62_c6) {
			this.r62_c6 = r62_c6;
		}

		public BigDecimal getR62_c7() {
			return r62_c7;
		}

		public void setR62_c7(BigDecimal r62_c7) {
			this.r62_c7 = r62_c7;
		}

		public BigDecimal getR62_c8() {
			return r62_c8;
		}

		public void setR62_c8(BigDecimal r62_c8) {
			this.r62_c8 = r62_c8;
		}

		public BigDecimal getR62_total() {
			return r62_total;
		}

		public void setR62_total(BigDecimal r62_total) {
			this.r62_total = r62_total;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}

	}

//ARCHIVAL ROW MAPPER

	class M_LA5ArchivalRowMapper implements RowMapper<M_LA5_Archival_Summary_Entity> {

		@Override
		public M_LA5_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA5_Archival_Summary_Entity obj = new M_LA5_Archival_Summary_Entity();

// Row r6
			obj.setR6_product(rs.getString("r6_product"));
			obj.setR6_usd(rs.getBigDecimal("r6_usd"));
			obj.setR6_zar(rs.getBigDecimal("r6_zar"));
			obj.setR6_gbp(rs.getBigDecimal("r6_gbp"));
			obj.setR6_euro(rs.getBigDecimal("r6_euro"));
			obj.setR6_yen(rs.getBigDecimal("r6_yen"));
			obj.setR6_c6(rs.getBigDecimal("r6_c6"));
			obj.setR6_c7(rs.getBigDecimal("r6_c7"));
			obj.setR6_c8(rs.getBigDecimal("r6_c8"));
			obj.setR6_total(rs.getBigDecimal("r6_total"));

// Row r7
			obj.setR7_product(rs.getString("r7_product"));
			obj.setR7_usd(rs.getBigDecimal("r7_usd"));
			obj.setR7_zar(rs.getBigDecimal("r7_zar"));
			obj.setR7_gbp(rs.getBigDecimal("r7_gbp"));
			obj.setR7_euro(rs.getBigDecimal("r7_euro"));
			obj.setR7_yen(rs.getBigDecimal("r7_yen"));
			obj.setR7_c6(rs.getBigDecimal("r7_c6"));
			obj.setR7_c7(rs.getBigDecimal("r7_c7"));
			obj.setR7_c8(rs.getBigDecimal("r7_c8"));
			obj.setR7_total(rs.getBigDecimal("r7_total"));

// Row r8
			obj.setR8_product(rs.getString("r8_product"));
			obj.setR8_usd(rs.getBigDecimal("r8_usd"));
			obj.setR8_zar(rs.getBigDecimal("r8_zar"));
			obj.setR8_gbp(rs.getBigDecimal("r8_gbp"));
			obj.setR8_euro(rs.getBigDecimal("r8_euro"));
			obj.setR8_yen(rs.getBigDecimal("r8_yen"));
			obj.setR8_c6(rs.getBigDecimal("r8_c6"));
			obj.setR8_c7(rs.getBigDecimal("r8_c7"));
			obj.setR8_c8(rs.getBigDecimal("r8_c8"));
			obj.setR8_total(rs.getBigDecimal("r8_total"));

// Row r9
			obj.setR9_product(rs.getString("r9_product"));
			obj.setR9_usd(rs.getBigDecimal("r9_usd"));
			obj.setR9_zar(rs.getBigDecimal("r9_zar"));
			obj.setR9_gbp(rs.getBigDecimal("r9_gbp"));
			obj.setR9_euro(rs.getBigDecimal("r9_euro"));
			obj.setR9_yen(rs.getBigDecimal("r9_yen"));
			obj.setR9_c6(rs.getBigDecimal("r9_c6"));
			obj.setR9_c7(rs.getBigDecimal("r9_c7"));
			obj.setR9_c8(rs.getBigDecimal("r9_c8"));
			obj.setR9_total(rs.getBigDecimal("r9_total"));

// Row r10
			obj.setR10_product(rs.getString("r10_product"));
			obj.setR10_usd(rs.getBigDecimal("r10_usd"));
			obj.setR10_zar(rs.getBigDecimal("r10_zar"));
			obj.setR10_gbp(rs.getBigDecimal("r10_gbp"));
			obj.setR10_euro(rs.getBigDecimal("r10_euro"));
			obj.setR10_yen(rs.getBigDecimal("r10_yen"));
			obj.setR10_c6(rs.getBigDecimal("r10_c6"));
			obj.setR10_c7(rs.getBigDecimal("r10_c7"));
			obj.setR10_c8(rs.getBigDecimal("r10_c8"));
			obj.setR10_total(rs.getBigDecimal("r10_total"));

// Row r11
			obj.setR11_product(rs.getString("r11_product"));
			obj.setR11_usd(rs.getBigDecimal("r11_usd"));
			obj.setR11_zar(rs.getBigDecimal("r11_zar"));
			obj.setR11_gbp(rs.getBigDecimal("r11_gbp"));
			obj.setR11_euro(rs.getBigDecimal("r11_euro"));
			obj.setR11_yen(rs.getBigDecimal("r11_yen"));
			obj.setR11_c6(rs.getBigDecimal("r11_c6"));
			obj.setR11_c7(rs.getBigDecimal("r11_c7"));
			obj.setR11_c8(rs.getBigDecimal("r11_c8"));
			obj.setR11_total(rs.getBigDecimal("r11_total"));

// Row r12
			obj.setR12_product(rs.getString("r12_product"));
			obj.setR12_usd(rs.getBigDecimal("r12_usd"));
			obj.setR12_zar(rs.getBigDecimal("r12_zar"));
			obj.setR12_gbp(rs.getBigDecimal("r12_gbp"));
			obj.setR12_euro(rs.getBigDecimal("r12_euro"));
			obj.setR12_yen(rs.getBigDecimal("r12_yen"));
			obj.setR12_c6(rs.getBigDecimal("r12_c6"));
			obj.setR12_c7(rs.getBigDecimal("r12_c7"));
			obj.setR12_c8(rs.getBigDecimal("r12_c8"));
			obj.setR12_total(rs.getBigDecimal("r12_total"));

// Row r13
			obj.setR13_product(rs.getString("r13_product"));
			obj.setR13_usd(rs.getBigDecimal("r13_usd"));
			obj.setR13_zar(rs.getBigDecimal("r13_zar"));
			obj.setR13_gbp(rs.getBigDecimal("r13_gbp"));
			obj.setR13_euro(rs.getBigDecimal("r13_euro"));
			obj.setR13_yen(rs.getBigDecimal("r13_yen"));
			obj.setR13_c6(rs.getBigDecimal("r13_c6"));
			obj.setR13_c7(rs.getBigDecimal("r13_c7"));
			obj.setR13_c8(rs.getBigDecimal("r13_c8"));
			obj.setR13_total(rs.getBigDecimal("r13_total"));

// Row r14
			obj.setR14_product(rs.getString("r14_product"));
			obj.setR14_usd(rs.getBigDecimal("r14_usd"));
			obj.setR14_zar(rs.getBigDecimal("r14_zar"));
			obj.setR14_gbp(rs.getBigDecimal("r14_gbp"));
			obj.setR14_euro(rs.getBigDecimal("r14_euro"));
			obj.setR14_yen(rs.getBigDecimal("r14_yen"));
			obj.setR14_c6(rs.getBigDecimal("r14_c6"));
			obj.setR14_c7(rs.getBigDecimal("r14_c7"));
			obj.setR14_c8(rs.getBigDecimal("r14_c8"));
			obj.setR14_total(rs.getBigDecimal("r14_total"));

// Row r15
			obj.setR15_product(rs.getString("r15_product"));
			obj.setR15_usd(rs.getBigDecimal("r15_usd"));
			obj.setR15_zar(rs.getBigDecimal("r15_zar"));
			obj.setR15_gbp(rs.getBigDecimal("r15_gbp"));
			obj.setR15_euro(rs.getBigDecimal("r15_euro"));
			obj.setR15_yen(rs.getBigDecimal("r15_yen"));
			obj.setR15_c6(rs.getBigDecimal("r15_c6"));
			obj.setR15_c7(rs.getBigDecimal("r15_c7"));
			obj.setR15_c8(rs.getBigDecimal("r15_c8"));
			obj.setR15_total(rs.getBigDecimal("r15_total"));

// Row r16
			obj.setR16_product(rs.getString("r16_product"));
			obj.setR16_usd(rs.getBigDecimal("r16_usd"));
			obj.setR16_zar(rs.getBigDecimal("r16_zar"));
			obj.setR16_gbp(rs.getBigDecimal("r16_gbp"));
			obj.setR16_euro(rs.getBigDecimal("r16_euro"));
			obj.setR16_yen(rs.getBigDecimal("r16_yen"));
			obj.setR16_c6(rs.getBigDecimal("r16_c6"));
			obj.setR16_c7(rs.getBigDecimal("r16_c7"));
			obj.setR16_c8(rs.getBigDecimal("r16_c8"));
			obj.setR16_total(rs.getBigDecimal("r16_total"));

// Row r17
			obj.setR17_product(rs.getString("r17_product"));
			obj.setR17_usd(rs.getBigDecimal("r17_usd"));
			obj.setR17_zar(rs.getBigDecimal("r17_zar"));
			obj.setR17_gbp(rs.getBigDecimal("r17_gbp"));
			obj.setR17_euro(rs.getBigDecimal("r17_euro"));
			obj.setR17_yen(rs.getBigDecimal("r17_yen"));
			obj.setR17_c6(rs.getBigDecimal("r17_c6"));
			obj.setR17_c7(rs.getBigDecimal("r17_c7"));
			obj.setR17_c8(rs.getBigDecimal("r17_c8"));
			obj.setR17_total(rs.getBigDecimal("r17_total"));

// Row r18
			obj.setR18_product(rs.getString("r18_product"));
			obj.setR18_usd(rs.getBigDecimal("r18_usd"));
			obj.setR18_zar(rs.getBigDecimal("r18_zar"));
			obj.setR18_gbp(rs.getBigDecimal("r18_gbp"));
			obj.setR18_euro(rs.getBigDecimal("r18_euro"));
			obj.setR18_yen(rs.getBigDecimal("r18_yen"));
			obj.setR18_c6(rs.getBigDecimal("r18_c6"));
			obj.setR18_c7(rs.getBigDecimal("r18_c7"));
			obj.setR18_c8(rs.getBigDecimal("r18_c8"));
			obj.setR18_total(rs.getBigDecimal("r18_total"));

// Row r19
			obj.setR19_product(rs.getString("r19_product"));
			obj.setR19_usd(rs.getBigDecimal("r19_usd"));
			obj.setR19_zar(rs.getBigDecimal("r19_zar"));
			obj.setR19_gbp(rs.getBigDecimal("r19_gbp"));
			obj.setR19_euro(rs.getBigDecimal("r19_euro"));
			obj.setR19_yen(rs.getBigDecimal("r19_yen"));
			obj.setR19_c6(rs.getBigDecimal("r19_c6"));
			obj.setR19_c7(rs.getBigDecimal("r19_c7"));
			obj.setR19_c8(rs.getBigDecimal("r19_c8"));
			obj.setR19_total(rs.getBigDecimal("r19_total"));

// Row r20
			obj.setR20_product(rs.getString("r20_product"));
			obj.setR20_usd(rs.getBigDecimal("r20_usd"));
			obj.setR20_zar(rs.getBigDecimal("r20_zar"));
			obj.setR20_gbp(rs.getBigDecimal("r20_gbp"));
			obj.setR20_euro(rs.getBigDecimal("r20_euro"));
			obj.setR20_yen(rs.getBigDecimal("r20_yen"));
			obj.setR20_c6(rs.getBigDecimal("r20_c6"));
			obj.setR20_c7(rs.getBigDecimal("r20_c7"));
			obj.setR20_c8(rs.getBigDecimal("r20_c8"));
			obj.setR20_total(rs.getBigDecimal("r20_total"));

// Row r21
			obj.setR21_product(rs.getString("r21_product"));
			obj.setR21_usd(rs.getBigDecimal("r21_usd"));
			obj.setR21_zar(rs.getBigDecimal("r21_zar"));
			obj.setR21_gbp(rs.getBigDecimal("r21_gbp"));
			obj.setR21_euro(rs.getBigDecimal("r21_euro"));
			obj.setR21_yen(rs.getBigDecimal("r21_yen"));
			obj.setR21_c6(rs.getBigDecimal("r21_c6"));
			obj.setR21_c7(rs.getBigDecimal("r21_c7"));
			obj.setR21_c8(rs.getBigDecimal("r21_c8"));
			obj.setR21_total(rs.getBigDecimal("r21_total"));

// Row r22
			obj.setR22_product(rs.getString("r22_product"));
			obj.setR22_usd(rs.getBigDecimal("r22_usd"));
			obj.setR22_zar(rs.getBigDecimal("r22_zar"));
			obj.setR22_gbp(rs.getBigDecimal("r22_gbp"));
			obj.setR22_euro(rs.getBigDecimal("r22_euro"));
			obj.setR22_yen(rs.getBigDecimal("r22_yen"));
			obj.setR22_c6(rs.getBigDecimal("r22_c6"));
			obj.setR22_c7(rs.getBigDecimal("r22_c7"));
			obj.setR22_c8(rs.getBigDecimal("r22_c8"));
			obj.setR22_total(rs.getBigDecimal("r22_total"));

// Row r23
			obj.setR23_product(rs.getString("r23_product"));
			obj.setR23_usd(rs.getBigDecimal("r23_usd"));
			obj.setR23_zar(rs.getBigDecimal("r23_zar"));
			obj.setR23_gbp(rs.getBigDecimal("r23_gbp"));
			obj.setR23_euro(rs.getBigDecimal("r23_euro"));
			obj.setR23_yen(rs.getBigDecimal("r23_yen"));
			obj.setR23_c6(rs.getBigDecimal("r23_c6"));
			obj.setR23_c7(rs.getBigDecimal("r23_c7"));
			obj.setR23_c8(rs.getBigDecimal("r23_c8"));
			obj.setR23_total(rs.getBigDecimal("r23_total"));

// Row r24
			obj.setR24_product(rs.getString("r24_product"));
			obj.setR24_usd(rs.getBigDecimal("r24_usd"));
			obj.setR24_zar(rs.getBigDecimal("r24_zar"));
			obj.setR24_gbp(rs.getBigDecimal("r24_gbp"));
			obj.setR24_euro(rs.getBigDecimal("r24_euro"));
			obj.setR24_yen(rs.getBigDecimal("r24_yen"));
			obj.setR24_c6(rs.getBigDecimal("r24_c6"));
			obj.setR24_c7(rs.getBigDecimal("r24_c7"));
			obj.setR24_c8(rs.getBigDecimal("r24_c8"));
			obj.setR24_total(rs.getBigDecimal("r24_total"));

// Row r25
			obj.setR25_product(rs.getString("r25_product"));
			obj.setR25_usd(rs.getBigDecimal("r25_usd"));
			obj.setR25_zar(rs.getBigDecimal("r25_zar"));
			obj.setR25_gbp(rs.getBigDecimal("r25_gbp"));
			obj.setR25_euro(rs.getBigDecimal("r25_euro"));
			obj.setR25_yen(rs.getBigDecimal("r25_yen"));
			obj.setR25_c6(rs.getBigDecimal("r25_c6"));
			obj.setR25_c7(rs.getBigDecimal("r25_c7"));
			obj.setR25_c8(rs.getBigDecimal("r25_c8"));
			obj.setR25_total(rs.getBigDecimal("r25_total"));

// Row r26
			obj.setR26_product(rs.getString("r26_product"));
			obj.setR26_usd(rs.getBigDecimal("r26_usd"));
			obj.setR26_zar(rs.getBigDecimal("r26_zar"));
			obj.setR26_gbp(rs.getBigDecimal("r26_gbp"));
			obj.setR26_euro(rs.getBigDecimal("r26_euro"));
			obj.setR26_yen(rs.getBigDecimal("r26_yen"));
			obj.setR26_c6(rs.getBigDecimal("r26_c6"));
			obj.setR26_c7(rs.getBigDecimal("r26_c7"));
			obj.setR26_c8(rs.getBigDecimal("r26_c8"));
			obj.setR26_total(rs.getBigDecimal("r26_total"));

// Row r27
			obj.setR27_product(rs.getString("r27_product"));
			obj.setR27_usd(rs.getBigDecimal("r27_usd"));
			obj.setR27_zar(rs.getBigDecimal("r27_zar"));
			obj.setR27_gbp(rs.getBigDecimal("r27_gbp"));
			obj.setR27_euro(rs.getBigDecimal("r27_euro"));
			obj.setR27_yen(rs.getBigDecimal("r27_yen"));
			obj.setR27_c6(rs.getBigDecimal("r27_c6"));
			obj.setR27_c7(rs.getBigDecimal("r27_c7"));
			obj.setR27_c8(rs.getBigDecimal("r27_c8"));
			obj.setR27_total(rs.getBigDecimal("r27_total"));

// Row r28
			obj.setR28_product(rs.getString("r28_product"));
			obj.setR28_usd(rs.getBigDecimal("r28_usd"));
			obj.setR28_zar(rs.getBigDecimal("r28_zar"));
			obj.setR28_gbp(rs.getBigDecimal("r28_gbp"));
			obj.setR28_euro(rs.getBigDecimal("r28_euro"));
			obj.setR28_yen(rs.getBigDecimal("r28_yen"));
			obj.setR28_c6(rs.getBigDecimal("r28_c6"));
			obj.setR28_c7(rs.getBigDecimal("r28_c7"));
			obj.setR28_c8(rs.getBigDecimal("r28_c8"));
			obj.setR28_total(rs.getBigDecimal("r28_total"));

// Row r29
			obj.setR29_product(rs.getString("r29_product"));
			obj.setR29_usd(rs.getBigDecimal("r29_usd"));
			obj.setR29_zar(rs.getBigDecimal("r29_zar"));
			obj.setR29_gbp(rs.getBigDecimal("r29_gbp"));
			obj.setR29_euro(rs.getBigDecimal("r29_euro"));
			obj.setR29_yen(rs.getBigDecimal("r29_yen"));
			obj.setR29_c6(rs.getBigDecimal("r29_c6"));
			obj.setR29_c7(rs.getBigDecimal("r29_c7"));
			obj.setR29_c8(rs.getBigDecimal("r29_c8"));
			obj.setR29_total(rs.getBigDecimal("r29_total"));

// Row r30
			obj.setR30_product(rs.getString("r30_product"));
			obj.setR30_usd(rs.getBigDecimal("r30_usd"));
			obj.setR30_zar(rs.getBigDecimal("r30_zar"));
			obj.setR30_gbp(rs.getBigDecimal("r30_gbp"));
			obj.setR30_euro(rs.getBigDecimal("r30_euro"));
			obj.setR30_yen(rs.getBigDecimal("r30_yen"));
			obj.setR30_c6(rs.getBigDecimal("r30_c6"));
			obj.setR30_c7(rs.getBigDecimal("r30_c7"));
			obj.setR30_c8(rs.getBigDecimal("r30_c8"));
			obj.setR30_total(rs.getBigDecimal("r30_total"));

// Row r31
			obj.setR31_product(rs.getString("r31_product"));
			obj.setR31_usd(rs.getBigDecimal("r31_usd"));
			obj.setR31_zar(rs.getBigDecimal("r31_zar"));
			obj.setR31_gbp(rs.getBigDecimal("r31_gbp"));
			obj.setR31_euro(rs.getBigDecimal("r31_euro"));
			obj.setR31_yen(rs.getBigDecimal("r31_yen"));
			obj.setR31_c6(rs.getBigDecimal("r31_c6"));
			obj.setR31_c7(rs.getBigDecimal("r31_c7"));
			obj.setR31_c8(rs.getBigDecimal("r31_c8"));
			obj.setR31_total(rs.getBigDecimal("r31_total"));

// Row r32
			obj.setR32_product(rs.getString("r32_product"));
			obj.setR32_usd(rs.getBigDecimal("r32_usd"));
			obj.setR32_zar(rs.getBigDecimal("r32_zar"));
			obj.setR32_gbp(rs.getBigDecimal("r32_gbp"));
			obj.setR32_euro(rs.getBigDecimal("r32_euro"));
			obj.setR32_yen(rs.getBigDecimal("r32_yen"));
			obj.setR32_c6(rs.getBigDecimal("r32_c6"));
			obj.setR32_c7(rs.getBigDecimal("r32_c7"));
			obj.setR32_c8(rs.getBigDecimal("r32_c8"));
			obj.setR32_total(rs.getBigDecimal("r32_total"));

// Row r33
			obj.setR33_product(rs.getString("r33_product"));
			obj.setR33_usd(rs.getBigDecimal("r33_usd"));
			obj.setR33_zar(rs.getBigDecimal("r33_zar"));
			obj.setR33_gbp(rs.getBigDecimal("r33_gbp"));
			obj.setR33_euro(rs.getBigDecimal("r33_euro"));
			obj.setR33_yen(rs.getBigDecimal("r33_yen"));
			obj.setR33_c6(rs.getBigDecimal("r33_c6"));
			obj.setR33_c7(rs.getBigDecimal("r33_c7"));
			obj.setR33_c8(rs.getBigDecimal("r33_c8"));
			obj.setR33_total(rs.getBigDecimal("r33_total"));

// Row r34
			obj.setR34_product(rs.getString("r34_product"));
			obj.setR34_usd(rs.getBigDecimal("r34_usd"));
			obj.setR34_zar(rs.getBigDecimal("r34_zar"));
			obj.setR34_gbp(rs.getBigDecimal("r34_gbp"));
			obj.setR34_euro(rs.getBigDecimal("r34_euro"));
			obj.setR34_yen(rs.getBigDecimal("r34_yen"));
			obj.setR34_c6(rs.getBigDecimal("r34_c6"));
			obj.setR34_c7(rs.getBigDecimal("r34_c7"));
			obj.setR34_c8(rs.getBigDecimal("r34_c8"));
			obj.setR34_total(rs.getBigDecimal("r34_total"));

// Row r35
			obj.setR35_product(rs.getString("r35_product"));
			obj.setR35_usd(rs.getBigDecimal("r35_usd"));
			obj.setR35_zar(rs.getBigDecimal("r35_zar"));
			obj.setR35_gbp(rs.getBigDecimal("r35_gbp"));
			obj.setR35_euro(rs.getBigDecimal("r35_euro"));
			obj.setR35_yen(rs.getBigDecimal("r35_yen"));
			obj.setR35_c6(rs.getBigDecimal("r35_c6"));
			obj.setR35_c7(rs.getBigDecimal("r35_c7"));
			obj.setR35_c8(rs.getBigDecimal("r35_c8"));
			obj.setR35_total(rs.getBigDecimal("r35_total"));

// Row r36
			obj.setR36_product(rs.getString("r36_product"));
			obj.setR36_usd(rs.getBigDecimal("r36_usd"));
			obj.setR36_zar(rs.getBigDecimal("r36_zar"));
			obj.setR36_gbp(rs.getBigDecimal("r36_gbp"));
			obj.setR36_euro(rs.getBigDecimal("r36_euro"));
			obj.setR36_yen(rs.getBigDecimal("r36_yen"));
			obj.setR36_c6(rs.getBigDecimal("r36_c6"));
			obj.setR36_c7(rs.getBigDecimal("r36_c7"));
			obj.setR36_c8(rs.getBigDecimal("r36_c8"));
			obj.setR36_total(rs.getBigDecimal("r36_total"));

// Row r37
			obj.setR37_product(rs.getString("r37_product"));
			obj.setR37_usd(rs.getBigDecimal("r37_usd"));
			obj.setR37_zar(rs.getBigDecimal("r37_zar"));
			obj.setR37_gbp(rs.getBigDecimal("r37_gbp"));
			obj.setR37_euro(rs.getBigDecimal("r37_euro"));
			obj.setR37_yen(rs.getBigDecimal("r37_yen"));
			obj.setR37_c6(rs.getBigDecimal("r37_c6"));
			obj.setR37_c7(rs.getBigDecimal("r37_c7"));
			obj.setR37_c8(rs.getBigDecimal("r37_c8"));
			obj.setR37_total(rs.getBigDecimal("r37_total"));

// Row r38
			obj.setR38_product(rs.getString("r38_product"));
			obj.setR38_usd(rs.getBigDecimal("r38_usd"));
			obj.setR38_zar(rs.getBigDecimal("r38_zar"));
			obj.setR38_gbp(rs.getBigDecimal("r38_gbp"));
			obj.setR38_euro(rs.getBigDecimal("r38_euro"));
			obj.setR38_yen(rs.getBigDecimal("r38_yen"));
			obj.setR38_c6(rs.getBigDecimal("r38_c6"));
			obj.setR38_c7(rs.getBigDecimal("r38_c7"));
			obj.setR38_c8(rs.getBigDecimal("r38_c8"));
			obj.setR38_total(rs.getBigDecimal("r38_total"));

// Row r39
			obj.setR39_product(rs.getString("r39_product"));
			obj.setR39_usd(rs.getBigDecimal("r39_usd"));
			obj.setR39_zar(rs.getBigDecimal("r39_zar"));
			obj.setR39_gbp(rs.getBigDecimal("r39_gbp"));
			obj.setR39_euro(rs.getBigDecimal("r39_euro"));
			obj.setR39_yen(rs.getBigDecimal("r39_yen"));
			obj.setR39_c6(rs.getBigDecimal("r39_c6"));
			obj.setR39_c7(rs.getBigDecimal("r39_c7"));
			obj.setR39_c8(rs.getBigDecimal("r39_c8"));
			obj.setR39_total(rs.getBigDecimal("r39_total"));

// Row r40
			obj.setR40_product(rs.getString("r40_product"));
			obj.setR40_usd(rs.getBigDecimal("r40_usd"));
			obj.setR40_zar(rs.getBigDecimal("r40_zar"));
			obj.setR40_gbp(rs.getBigDecimal("r40_gbp"));
			obj.setR40_euro(rs.getBigDecimal("r40_euro"));
			obj.setR40_yen(rs.getBigDecimal("r40_yen"));
			obj.setR40_c6(rs.getBigDecimal("r40_c6"));
			obj.setR40_c7(rs.getBigDecimal("r40_c7"));
			obj.setR40_c8(rs.getBigDecimal("r40_c8"));
			obj.setR40_total(rs.getBigDecimal("r40_total"));

// Row r41
			obj.setR41_product(rs.getString("r41_product"));
			obj.setR41_usd(rs.getBigDecimal("r41_usd"));
			obj.setR41_zar(rs.getBigDecimal("r41_zar"));
			obj.setR41_gbp(rs.getBigDecimal("r41_gbp"));
			obj.setR41_euro(rs.getBigDecimal("r41_euro"));
			obj.setR41_yen(rs.getBigDecimal("r41_yen"));
			obj.setR41_c6(rs.getBigDecimal("r41_c6"));
			obj.setR41_c7(rs.getBigDecimal("r41_c7"));
			obj.setR41_c8(rs.getBigDecimal("r41_c8"));
			obj.setR41_total(rs.getBigDecimal("r41_total"));

// Row r42
			obj.setR42_product(rs.getString("r42_product"));
			obj.setR42_usd(rs.getBigDecimal("r42_usd"));
			obj.setR42_zar(rs.getBigDecimal("r42_zar"));
			obj.setR42_gbp(rs.getBigDecimal("r42_gbp"));
			obj.setR42_euro(rs.getBigDecimal("r42_euro"));
			obj.setR42_yen(rs.getBigDecimal("r42_yen"));
			obj.setR42_c6(rs.getBigDecimal("r42_c6"));
			obj.setR42_c7(rs.getBigDecimal("r42_c7"));
			obj.setR42_c8(rs.getBigDecimal("r42_c8"));
			obj.setR42_total(rs.getBigDecimal("r42_total"));

// Row r43
			obj.setR43_product(rs.getString("r43_product"));
			obj.setR43_usd(rs.getBigDecimal("r43_usd"));
			obj.setR43_zar(rs.getBigDecimal("r43_zar"));
			obj.setR43_gbp(rs.getBigDecimal("r43_gbp"));
			obj.setR43_euro(rs.getBigDecimal("r43_euro"));
			obj.setR43_yen(rs.getBigDecimal("r43_yen"));
			obj.setR43_c6(rs.getBigDecimal("r43_c6"));
			obj.setR43_c7(rs.getBigDecimal("r43_c7"));
			obj.setR43_c8(rs.getBigDecimal("r43_c8"));
			obj.setR43_total(rs.getBigDecimal("r43_total"));

// Row r44
			obj.setR44_product(rs.getString("r44_product"));
			obj.setR44_usd(rs.getBigDecimal("r44_usd"));
			obj.setR44_zar(rs.getBigDecimal("r44_zar"));
			obj.setR44_gbp(rs.getBigDecimal("r44_gbp"));
			obj.setR44_euro(rs.getBigDecimal("r44_euro"));
			obj.setR44_yen(rs.getBigDecimal("r44_yen"));
			obj.setR44_c6(rs.getBigDecimal("r44_c6"));
			obj.setR44_c7(rs.getBigDecimal("r44_c7"));
			obj.setR44_c8(rs.getBigDecimal("r44_c8"));
			obj.setR44_total(rs.getBigDecimal("r44_total"));

// Row r45
			obj.setR45_product(rs.getString("r45_product"));
			obj.setR45_usd(rs.getBigDecimal("r45_usd"));
			obj.setR45_zar(rs.getBigDecimal("r45_zar"));
			obj.setR45_gbp(rs.getBigDecimal("r45_gbp"));
			obj.setR45_euro(rs.getBigDecimal("r45_euro"));
			obj.setR45_yen(rs.getBigDecimal("r45_yen"));
			obj.setR45_c6(rs.getBigDecimal("r45_c6"));
			obj.setR45_c7(rs.getBigDecimal("r45_c7"));
			obj.setR45_c8(rs.getBigDecimal("r45_c8"));
			obj.setR45_total(rs.getBigDecimal("r45_total"));

// Row r46
			obj.setR46_product(rs.getString("r46_product"));
			obj.setR46_usd(rs.getBigDecimal("r46_usd"));
			obj.setR46_zar(rs.getBigDecimal("r46_zar"));
			obj.setR46_gbp(rs.getBigDecimal("r46_gbp"));
			obj.setR46_euro(rs.getBigDecimal("r46_euro"));
			obj.setR46_yen(rs.getBigDecimal("r46_yen"));
			obj.setR46_c6(rs.getBigDecimal("r46_c6"));
			obj.setR46_c7(rs.getBigDecimal("r46_c7"));
			obj.setR46_c8(rs.getBigDecimal("r46_c8"));
			obj.setR46_total(rs.getBigDecimal("r46_total"));

// Row r47
			obj.setR47_product(rs.getString("r47_product"));
			obj.setR47_usd(rs.getBigDecimal("r47_usd"));
			obj.setR47_zar(rs.getBigDecimal("r47_zar"));
			obj.setR47_gbp(rs.getBigDecimal("r47_gbp"));
			obj.setR47_euro(rs.getBigDecimal("r47_euro"));
			obj.setR47_yen(rs.getBigDecimal("r47_yen"));
			obj.setR47_c6(rs.getBigDecimal("r47_c6"));
			obj.setR47_c7(rs.getBigDecimal("r47_c7"));
			obj.setR47_c8(rs.getBigDecimal("r47_c8"));
			obj.setR47_total(rs.getBigDecimal("r47_total"));

// Row r48
			obj.setR48_product(rs.getString("r48_product"));
			obj.setR48_usd(rs.getBigDecimal("r48_usd"));
			obj.setR48_zar(rs.getBigDecimal("r48_zar"));
			obj.setR48_gbp(rs.getBigDecimal("r48_gbp"));
			obj.setR48_euro(rs.getBigDecimal("r48_euro"));
			obj.setR48_yen(rs.getBigDecimal("r48_yen"));
			obj.setR48_c6(rs.getBigDecimal("r48_c6"));
			obj.setR48_c7(rs.getBigDecimal("r48_c7"));
			obj.setR48_c8(rs.getBigDecimal("r48_c8"));
			obj.setR48_total(rs.getBigDecimal("r48_total"));

// Row r49
			obj.setR49_product(rs.getString("r49_product"));
			obj.setR49_usd(rs.getBigDecimal("r49_usd"));
			obj.setR49_zar(rs.getBigDecimal("r49_zar"));
			obj.setR49_gbp(rs.getBigDecimal("r49_gbp"));
			obj.setR49_euro(rs.getBigDecimal("r49_euro"));
			obj.setR49_yen(rs.getBigDecimal("r49_yen"));
			obj.setR49_c6(rs.getBigDecimal("r49_c6"));
			obj.setR49_c7(rs.getBigDecimal("r49_c7"));
			obj.setR49_c8(rs.getBigDecimal("r49_c8"));
			obj.setR49_total(rs.getBigDecimal("r49_total"));

// Row r50
			obj.setR50_product(rs.getString("r50_product"));
			obj.setR50_usd(rs.getBigDecimal("r50_usd"));
			obj.setR50_zar(rs.getBigDecimal("r50_zar"));
			obj.setR50_gbp(rs.getBigDecimal("r50_gbp"));
			obj.setR50_euro(rs.getBigDecimal("r50_euro"));
			obj.setR50_yen(rs.getBigDecimal("r50_yen"));
			obj.setR50_c6(rs.getBigDecimal("r50_c6"));
			obj.setR50_c7(rs.getBigDecimal("r50_c7"));
			obj.setR50_c8(rs.getBigDecimal("r50_c8"));
			obj.setR50_total(rs.getBigDecimal("r50_total"));

// Row r51
			obj.setR51_product(rs.getString("r51_product"));
			obj.setR51_usd(rs.getBigDecimal("r51_usd"));
			obj.setR51_zar(rs.getBigDecimal("r51_zar"));
			obj.setR51_gbp(rs.getBigDecimal("r51_gbp"));
			obj.setR51_euro(rs.getBigDecimal("r51_euro"));
			obj.setR51_yen(rs.getBigDecimal("r51_yen"));
			obj.setR51_c6(rs.getBigDecimal("r51_c6"));
			obj.setR51_c7(rs.getBigDecimal("r51_c7"));
			obj.setR51_c8(rs.getBigDecimal("r51_c8"));
			obj.setR51_total(rs.getBigDecimal("r51_total"));

// Row r52
			obj.setR52_product(rs.getString("r52_product"));
			obj.setR52_usd(rs.getBigDecimal("r52_usd"));
			obj.setR52_zar(rs.getBigDecimal("r52_zar"));
			obj.setR52_gbp(rs.getBigDecimal("r52_gbp"));
			obj.setR52_euro(rs.getBigDecimal("r52_euro"));
			obj.setR52_yen(rs.getBigDecimal("r52_yen"));
			obj.setR52_c6(rs.getBigDecimal("r52_c6"));
			obj.setR52_c7(rs.getBigDecimal("r52_c7"));
			obj.setR52_c8(rs.getBigDecimal("r52_c8"));
			obj.setR52_total(rs.getBigDecimal("r52_total"));

// Row r53
			obj.setR53_product(rs.getString("r53_product"));
			obj.setR53_usd(rs.getBigDecimal("r53_usd"));
			obj.setR53_zar(rs.getBigDecimal("r53_zar"));
			obj.setR53_gbp(rs.getBigDecimal("r53_gbp"));
			obj.setR53_euro(rs.getBigDecimal("r53_euro"));
			obj.setR53_yen(rs.getBigDecimal("r53_yen"));
			obj.setR53_c6(rs.getBigDecimal("r53_c6"));
			obj.setR53_c7(rs.getBigDecimal("r53_c7"));
			obj.setR53_c8(rs.getBigDecimal("r53_c8"));
			obj.setR53_total(rs.getBigDecimal("r53_total"));

// Row r54
			obj.setR54_product(rs.getString("r54_product"));
			obj.setR54_usd(rs.getBigDecimal("r54_usd"));
			obj.setR54_zar(rs.getBigDecimal("r54_zar"));
			obj.setR54_gbp(rs.getBigDecimal("r54_gbp"));
			obj.setR54_euro(rs.getBigDecimal("r54_euro"));
			obj.setR54_yen(rs.getBigDecimal("r54_yen"));
			obj.setR54_c6(rs.getBigDecimal("r54_c6"));
			obj.setR54_c7(rs.getBigDecimal("r54_c7"));
			obj.setR54_c8(rs.getBigDecimal("r54_c8"));
			obj.setR54_total(rs.getBigDecimal("r54_total"));

// Row r55
			obj.setR55_product(rs.getString("r55_product"));
			obj.setR55_usd(rs.getBigDecimal("r55_usd"));
			obj.setR55_zar(rs.getBigDecimal("r55_zar"));
			obj.setR55_gbp(rs.getBigDecimal("r55_gbp"));
			obj.setR55_euro(rs.getBigDecimal("r55_euro"));
			obj.setR55_yen(rs.getBigDecimal("r55_yen"));
			obj.setR55_c6(rs.getBigDecimal("r55_c6"));
			obj.setR55_c7(rs.getBigDecimal("r55_c7"));
			obj.setR55_c8(rs.getBigDecimal("r55_c8"));
			obj.setR55_total(rs.getBigDecimal("r55_total"));

// Row r56
			obj.setR56_product(rs.getString("r56_product"));
			obj.setR56_usd(rs.getBigDecimal("r56_usd"));
			obj.setR56_zar(rs.getBigDecimal("r56_zar"));
			obj.setR56_gbp(rs.getBigDecimal("r56_gbp"));
			obj.setR56_euro(rs.getBigDecimal("r56_euro"));
			obj.setR56_yen(rs.getBigDecimal("r56_yen"));
			obj.setR56_c6(rs.getBigDecimal("r56_c6"));
			obj.setR56_c7(rs.getBigDecimal("r56_c7"));
			obj.setR56_c8(rs.getBigDecimal("r56_c8"));
			obj.setR56_total(rs.getBigDecimal("r56_total"));

// Row r57
			obj.setR57_product(rs.getString("r57_product"));
			obj.setR57_usd(rs.getBigDecimal("r57_usd"));
			obj.setR57_zar(rs.getBigDecimal("r57_zar"));
			obj.setR57_gbp(rs.getBigDecimal("r57_gbp"));
			obj.setR57_euro(rs.getBigDecimal("r57_euro"));
			obj.setR57_yen(rs.getBigDecimal("r57_yen"));
			obj.setR57_c6(rs.getBigDecimal("r57_c6"));
			obj.setR57_c7(rs.getBigDecimal("r57_c7"));
			obj.setR57_c8(rs.getBigDecimal("r57_c8"));
			obj.setR57_total(rs.getBigDecimal("r57_total"));

// Row r58
			obj.setR58_product(rs.getString("r58_product"));
			obj.setR58_usd(rs.getBigDecimal("r58_usd"));
			obj.setR58_zar(rs.getBigDecimal("r58_zar"));
			obj.setR58_gbp(rs.getBigDecimal("r58_gbp"));
			obj.setR58_euro(rs.getBigDecimal("r58_euro"));
			obj.setR58_yen(rs.getBigDecimal("r58_yen"));
			obj.setR58_c6(rs.getBigDecimal("r58_c6"));
			obj.setR58_c7(rs.getBigDecimal("r58_c7"));
			obj.setR58_c8(rs.getBigDecimal("r58_c8"));
			obj.setR58_total(rs.getBigDecimal("r58_total"));

// Row r59
			obj.setR59_product(rs.getString("r59_product"));
			obj.setR59_usd(rs.getBigDecimal("r59_usd"));
			obj.setR59_zar(rs.getBigDecimal("r59_zar"));
			obj.setR59_gbp(rs.getBigDecimal("r59_gbp"));
			obj.setR59_euro(rs.getBigDecimal("r59_euro"));
			obj.setR59_yen(rs.getBigDecimal("r59_yen"));
			obj.setR59_c6(rs.getBigDecimal("r59_c6"));
			obj.setR59_c7(rs.getBigDecimal("r59_c7"));
			obj.setR59_c8(rs.getBigDecimal("r59_c8"));
			obj.setR59_total(rs.getBigDecimal("r59_total"));

// Row r60
			obj.setR60_product(rs.getString("r60_product"));
			obj.setR60_usd(rs.getBigDecimal("r60_usd"));
			obj.setR60_zar(rs.getBigDecimal("r60_zar"));
			obj.setR60_gbp(rs.getBigDecimal("r60_gbp"));
			obj.setR60_euro(rs.getBigDecimal("r60_euro"));
			obj.setR60_yen(rs.getBigDecimal("r60_yen"));
			obj.setR60_c6(rs.getBigDecimal("r60_c6"));
			obj.setR60_c7(rs.getBigDecimal("r60_c7"));
			obj.setR60_c8(rs.getBigDecimal("r60_c8"));
			obj.setR60_total(rs.getBigDecimal("r60_total"));

// Row r61
			obj.setR61_product(rs.getString("r61_product"));
			obj.setR61_usd(rs.getBigDecimal("r61_usd"));
			obj.setR61_zar(rs.getBigDecimal("r61_zar"));
			obj.setR61_gbp(rs.getBigDecimal("r61_gbp"));
			obj.setR61_euro(rs.getBigDecimal("r61_euro"));
			obj.setR61_yen(rs.getBigDecimal("r61_yen"));
			obj.setR61_c6(rs.getBigDecimal("r61_c6"));
			obj.setR61_c7(rs.getBigDecimal("r61_c7"));
			obj.setR61_c8(rs.getBigDecimal("r61_c8"));
			obj.setR61_total(rs.getBigDecimal("r61_total"));

// Row r62
			obj.setR62_product(rs.getString("r62_product"));
			obj.setR62_usd(rs.getBigDecimal("r62_usd"));
			obj.setR62_zar(rs.getBigDecimal("r62_zar"));
			obj.setR62_gbp(rs.getBigDecimal("r62_gbp"));
			obj.setR62_euro(rs.getBigDecimal("r62_euro"));
			obj.setR62_yen(rs.getBigDecimal("r62_yen"));
			obj.setR62_c6(rs.getBigDecimal("r62_c6"));
			obj.setR62_c7(rs.getBigDecimal("r62_c7"));
			obj.setR62_c8(rs.getBigDecimal("r62_c8"));
			obj.setR62_total(rs.getBigDecimal("r62_total"));

			// COMMON FIELDS
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	@IdClass(M_LA5_PK.class)
	public class M_LA5_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		// Row r6
		private String r6_product;
		private BigDecimal r6_usd;
		private BigDecimal r6_zar;
		private BigDecimal r6_gbp;
		private BigDecimal r6_euro;
		private BigDecimal r6_yen;
		private BigDecimal r6_c6;
		private BigDecimal r6_c7;
		private BigDecimal r6_c8;
		private BigDecimal r6_total;

		// Row r7
		private String r7_product;
		private BigDecimal r7_usd;
		private BigDecimal r7_zar;
		private BigDecimal r7_gbp;
		private BigDecimal r7_euro;
		private BigDecimal r7_yen;
		private BigDecimal r7_c6;
		private BigDecimal r7_c7;
		private BigDecimal r7_c8;
		private BigDecimal r7_total;

		// Row r8
		private String r8_product;
		private BigDecimal r8_usd;
		private BigDecimal r8_zar;
		private BigDecimal r8_gbp;
		private BigDecimal r8_euro;
		private BigDecimal r8_yen;
		private BigDecimal r8_c6;
		private BigDecimal r8_c7;
		private BigDecimal r8_c8;
		private BigDecimal r8_total;

		// Row r9
		private String r9_product;
		private BigDecimal r9_usd;
		private BigDecimal r9_zar;
		private BigDecimal r9_gbp;
		private BigDecimal r9_euro;
		private BigDecimal r9_yen;
		private BigDecimal r9_c6;
		private BigDecimal r9_c7;
		private BigDecimal r9_c8;
		private BigDecimal r9_total;

		// Row r10
		private String r10_product;
		private BigDecimal r10_usd;
		private BigDecimal r10_zar;
		private BigDecimal r10_gbp;
		private BigDecimal r10_euro;
		private BigDecimal r10_yen;
		private BigDecimal r10_c6;
		private BigDecimal r10_c7;
		private BigDecimal r10_c8;
		private BigDecimal r10_total;

		// Row r11
		private String r11_product;
		private BigDecimal r11_usd;
		private BigDecimal r11_zar;
		private BigDecimal r11_gbp;
		private BigDecimal r11_euro;
		private BigDecimal r11_yen;
		private BigDecimal r11_c6;
		private BigDecimal r11_c7;
		private BigDecimal r11_c8;
		private BigDecimal r11_total;

		// Row r12
		private String r12_product;
		private BigDecimal r12_usd;
		private BigDecimal r12_zar;
		private BigDecimal r12_gbp;
		private BigDecimal r12_euro;
		private BigDecimal r12_yen;
		private BigDecimal r12_c6;
		private BigDecimal r12_c7;
		private BigDecimal r12_c8;
		private BigDecimal r12_total;

		// Row r13
		private String r13_product;
		private BigDecimal r13_usd;
		private BigDecimal r13_zar;
		private BigDecimal r13_gbp;
		private BigDecimal r13_euro;
		private BigDecimal r13_yen;
		private BigDecimal r13_c6;
		private BigDecimal r13_c7;
		private BigDecimal r13_c8;
		private BigDecimal r13_total;

		// Row r14
		private String r14_product;
		private BigDecimal r14_usd;
		private BigDecimal r14_zar;
		private BigDecimal r14_gbp;
		private BigDecimal r14_euro;
		private BigDecimal r14_yen;
		private BigDecimal r14_c6;
		private BigDecimal r14_c7;
		private BigDecimal r14_c8;
		private BigDecimal r14_total;

		// Row r15
		private String r15_product;
		private BigDecimal r15_usd;
		private BigDecimal r15_zar;
		private BigDecimal r15_gbp;
		private BigDecimal r15_euro;
		private BigDecimal r15_yen;
		private BigDecimal r15_c6;
		private BigDecimal r15_c7;
		private BigDecimal r15_c8;
		private BigDecimal r15_total;

		// Row r16
		private String r16_product;
		private BigDecimal r16_usd;
		private BigDecimal r16_zar;
		private BigDecimal r16_gbp;
		private BigDecimal r16_euro;
		private BigDecimal r16_yen;
		private BigDecimal r16_c6;
		private BigDecimal r16_c7;
		private BigDecimal r16_c8;
		private BigDecimal r16_total;

		// Row r17
		private String r17_product;
		private BigDecimal r17_usd;
		private BigDecimal r17_zar;
		private BigDecimal r17_gbp;
		private BigDecimal r17_euro;
		private BigDecimal r17_yen;
		private BigDecimal r17_c6;
		private BigDecimal r17_c7;
		private BigDecimal r17_c8;
		private BigDecimal r17_total;

		// Row r18
		private String r18_product;
		private BigDecimal r18_usd;
		private BigDecimal r18_zar;
		private BigDecimal r18_gbp;
		private BigDecimal r18_euro;
		private BigDecimal r18_yen;
		private BigDecimal r18_c6;
		private BigDecimal r18_c7;
		private BigDecimal r18_c8;
		private BigDecimal r18_total;

		// Row r19
		private String r19_product;
		private BigDecimal r19_usd;
		private BigDecimal r19_zar;
		private BigDecimal r19_gbp;
		private BigDecimal r19_euro;
		private BigDecimal r19_yen;
		private BigDecimal r19_c6;
		private BigDecimal r19_c7;
		private BigDecimal r19_c8;
		private BigDecimal r19_total;

		// Row r20
		private String r20_product;
		private BigDecimal r20_usd;
		private BigDecimal r20_zar;
		private BigDecimal r20_gbp;
		private BigDecimal r20_euro;
		private BigDecimal r20_yen;
		private BigDecimal r20_c6;
		private BigDecimal r20_c7;
		private BigDecimal r20_c8;
		private BigDecimal r20_total;

		// Row r21
		private String r21_product;
		private BigDecimal r21_usd;
		private BigDecimal r21_zar;
		private BigDecimal r21_gbp;
		private BigDecimal r21_euro;
		private BigDecimal r21_yen;
		private BigDecimal r21_c6;
		private BigDecimal r21_c7;
		private BigDecimal r21_c8;
		private BigDecimal r21_total;

		// Row r22
		private String r22_product;
		private BigDecimal r22_usd;
		private BigDecimal r22_zar;
		private BigDecimal r22_gbp;
		private BigDecimal r22_euro;
		private BigDecimal r22_yen;
		private BigDecimal r22_c6;
		private BigDecimal r22_c7;
		private BigDecimal r22_c8;
		private BigDecimal r22_total;

		// Row r23
		private String r23_product;
		private BigDecimal r23_usd;
		private BigDecimal r23_zar;
		private BigDecimal r23_gbp;
		private BigDecimal r23_euro;
		private BigDecimal r23_yen;
		private BigDecimal r23_c6;
		private BigDecimal r23_c7;
		private BigDecimal r23_c8;
		private BigDecimal r23_total;

		// Row r24
		private String r24_product;
		private BigDecimal r24_usd;
		private BigDecimal r24_zar;
		private BigDecimal r24_gbp;
		private BigDecimal r24_euro;
		private BigDecimal r24_yen;
		private BigDecimal r24_c6;
		private BigDecimal r24_c7;
		private BigDecimal r24_c8;
		private BigDecimal r24_total;

		// Row r25
		private String r25_product;
		private BigDecimal r25_usd;
		private BigDecimal r25_zar;
		private BigDecimal r25_gbp;
		private BigDecimal r25_euro;
		private BigDecimal r25_yen;
		private BigDecimal r25_c6;
		private BigDecimal r25_c7;
		private BigDecimal r25_c8;
		private BigDecimal r25_total;

		// Row r26
		private String r26_product;
		private BigDecimal r26_usd;
		private BigDecimal r26_zar;
		private BigDecimal r26_gbp;
		private BigDecimal r26_euro;
		private BigDecimal r26_yen;
		private BigDecimal r26_c6;
		private BigDecimal r26_c7;
		private BigDecimal r26_c8;
		private BigDecimal r26_total;

		// Row r27
		private String r27_product;
		private BigDecimal r27_usd;
		private BigDecimal r27_zar;
		private BigDecimal r27_gbp;
		private BigDecimal r27_euro;
		private BigDecimal r27_yen;
		private BigDecimal r27_c6;
		private BigDecimal r27_c7;
		private BigDecimal r27_c8;
		private BigDecimal r27_total;

		// Row r28
		private String r28_product;
		private BigDecimal r28_usd;
		private BigDecimal r28_zar;
		private BigDecimal r28_gbp;
		private BigDecimal r28_euro;
		private BigDecimal r28_yen;
		private BigDecimal r28_c6;
		private BigDecimal r28_c7;
		private BigDecimal r28_c8;
		private BigDecimal r28_total;

		// Row r29
		private String r29_product;
		private BigDecimal r29_usd;
		private BigDecimal r29_zar;
		private BigDecimal r29_gbp;
		private BigDecimal r29_euro;
		private BigDecimal r29_yen;
		private BigDecimal r29_c6;
		private BigDecimal r29_c7;
		private BigDecimal r29_c8;
		private BigDecimal r29_total;

		// Row r30
		private String r30_product;
		private BigDecimal r30_usd;
		private BigDecimal r30_zar;
		private BigDecimal r30_gbp;
		private BigDecimal r30_euro;
		private BigDecimal r30_yen;
		private BigDecimal r30_c6;
		private BigDecimal r30_c7;
		private BigDecimal r30_c8;
		private BigDecimal r30_total;

		// Row r31
		private String r31_product;
		private BigDecimal r31_usd;
		private BigDecimal r31_zar;
		private BigDecimal r31_gbp;
		private BigDecimal r31_euro;
		private BigDecimal r31_yen;
		private BigDecimal r31_c6;
		private BigDecimal r31_c7;
		private BigDecimal r31_c8;
		private BigDecimal r31_total;

		// Row r32
		private String r32_product;
		private BigDecimal r32_usd;
		private BigDecimal r32_zar;
		private BigDecimal r32_gbp;
		private BigDecimal r32_euro;
		private BigDecimal r32_yen;
		private BigDecimal r32_c6;
		private BigDecimal r32_c7;
		private BigDecimal r32_c8;
		private BigDecimal r32_total;

		// Row r33
		private String r33_product;
		private BigDecimal r33_usd;
		private BigDecimal r33_zar;
		private BigDecimal r33_gbp;
		private BigDecimal r33_euro;
		private BigDecimal r33_yen;
		private BigDecimal r33_c6;
		private BigDecimal r33_c7;
		private BigDecimal r33_c8;
		private BigDecimal r33_total;

		// Row r34
		private String r34_product;
		private BigDecimal r34_usd;
		private BigDecimal r34_zar;
		private BigDecimal r34_gbp;
		private BigDecimal r34_euro;
		private BigDecimal r34_yen;
		private BigDecimal r34_c6;
		private BigDecimal r34_c7;
		private BigDecimal r34_c8;
		private BigDecimal r34_total;

		// Row r35
		private String r35_product;
		private BigDecimal r35_usd;
		private BigDecimal r35_zar;
		private BigDecimal r35_gbp;
		private BigDecimal r35_euro;
		private BigDecimal r35_yen;
		private BigDecimal r35_c6;
		private BigDecimal r35_c7;
		private BigDecimal r35_c8;
		private BigDecimal r35_total;

		// Row r36
		private String r36_product;
		private BigDecimal r36_usd;
		private BigDecimal r36_zar;
		private BigDecimal r36_gbp;
		private BigDecimal r36_euro;
		private BigDecimal r36_yen;
		private BigDecimal r36_c6;
		private BigDecimal r36_c7;
		private BigDecimal r36_c8;
		private BigDecimal r36_total;

		// Row r37
		private String r37_product;
		private BigDecimal r37_usd;
		private BigDecimal r37_zar;
		private BigDecimal r37_gbp;
		private BigDecimal r37_euro;
		private BigDecimal r37_yen;
		private BigDecimal r37_c6;
		private BigDecimal r37_c7;
		private BigDecimal r37_c8;
		private BigDecimal r37_total;

		// Row r38
		private String r38_product;
		private BigDecimal r38_usd;
		private BigDecimal r38_zar;
		private BigDecimal r38_gbp;
		private BigDecimal r38_euro;
		private BigDecimal r38_yen;
		private BigDecimal r38_c6;
		private BigDecimal r38_c7;
		private BigDecimal r38_c8;
		private BigDecimal r38_total;

		// Row r39
		private String r39_product;
		private BigDecimal r39_usd;
		private BigDecimal r39_zar;
		private BigDecimal r39_gbp;
		private BigDecimal r39_euro;
		private BigDecimal r39_yen;
		private BigDecimal r39_c6;
		private BigDecimal r39_c7;
		private BigDecimal r39_c8;
		private BigDecimal r39_total;

		// Row r40
		private String r40_product;
		private BigDecimal r40_usd;
		private BigDecimal r40_zar;
		private BigDecimal r40_gbp;
		private BigDecimal r40_euro;
		private BigDecimal r40_yen;
		private BigDecimal r40_c6;
		private BigDecimal r40_c7;
		private BigDecimal r40_c8;
		private BigDecimal r40_total;

		// Row r41
		private String r41_product;
		private BigDecimal r41_usd;
		private BigDecimal r41_zar;
		private BigDecimal r41_gbp;
		private BigDecimal r41_euro;
		private BigDecimal r41_yen;
		private BigDecimal r41_c6;
		private BigDecimal r41_c7;
		private BigDecimal r41_c8;
		private BigDecimal r41_total;

		// Row r42
		private String r42_product;
		private BigDecimal r42_usd;
		private BigDecimal r42_zar;
		private BigDecimal r42_gbp;
		private BigDecimal r42_euro;
		private BigDecimal r42_yen;
		private BigDecimal r42_c6;
		private BigDecimal r42_c7;
		private BigDecimal r42_c8;
		private BigDecimal r42_total;

		// Row r43
		private String r43_product;
		private BigDecimal r43_usd;
		private BigDecimal r43_zar;
		private BigDecimal r43_gbp;
		private BigDecimal r43_euro;
		private BigDecimal r43_yen;
		private BigDecimal r43_c6;
		private BigDecimal r43_c7;
		private BigDecimal r43_c8;
		private BigDecimal r43_total;

		// Row r44
		private String r44_product;
		private BigDecimal r44_usd;
		private BigDecimal r44_zar;
		private BigDecimal r44_gbp;
		private BigDecimal r44_euro;
		private BigDecimal r44_yen;
		private BigDecimal r44_c6;
		private BigDecimal r44_c7;
		private BigDecimal r44_c8;
		private BigDecimal r44_total;

		// Row r45
		private String r45_product;
		private BigDecimal r45_usd;
		private BigDecimal r45_zar;
		private BigDecimal r45_gbp;
		private BigDecimal r45_euro;
		private BigDecimal r45_yen;
		private BigDecimal r45_c6;
		private BigDecimal r45_c7;
		private BigDecimal r45_c8;
		private BigDecimal r45_total;

		// Row r46
		private String r46_product;
		private BigDecimal r46_usd;
		private BigDecimal r46_zar;
		private BigDecimal r46_gbp;
		private BigDecimal r46_euro;
		private BigDecimal r46_yen;
		private BigDecimal r46_c6;
		private BigDecimal r46_c7;
		private BigDecimal r46_c8;
		private BigDecimal r46_total;

		// Row r47
		private String r47_product;
		private BigDecimal r47_usd;
		private BigDecimal r47_zar;
		private BigDecimal r47_gbp;
		private BigDecimal r47_euro;
		private BigDecimal r47_yen;
		private BigDecimal r47_c6;
		private BigDecimal r47_c7;
		private BigDecimal r47_c8;
		private BigDecimal r47_total;

		// Row r48
		private String r48_product;
		private BigDecimal r48_usd;
		private BigDecimal r48_zar;
		private BigDecimal r48_gbp;
		private BigDecimal r48_euro;
		private BigDecimal r48_yen;
		private BigDecimal r48_c6;
		private BigDecimal r48_c7;
		private BigDecimal r48_c8;
		private BigDecimal r48_total;

		// Row r49
		private String r49_product;
		private BigDecimal r49_usd;
		private BigDecimal r49_zar;
		private BigDecimal r49_gbp;
		private BigDecimal r49_euro;
		private BigDecimal r49_yen;
		private BigDecimal r49_c6;
		private BigDecimal r49_c7;
		private BigDecimal r49_c8;
		private BigDecimal r49_total;

		// Row r50
		private String r50_product;
		private BigDecimal r50_usd;
		private BigDecimal r50_zar;
		private BigDecimal r50_gbp;
		private BigDecimal r50_euro;
		private BigDecimal r50_yen;
		private BigDecimal r50_c6;
		private BigDecimal r50_c7;
		private BigDecimal r50_c8;
		private BigDecimal r50_total;

		// Row r51
		private String r51_product;
		private BigDecimal r51_usd;
		private BigDecimal r51_zar;
		private BigDecimal r51_gbp;
		private BigDecimal r51_euro;
		private BigDecimal r51_yen;
		private BigDecimal r51_c6;
		private BigDecimal r51_c7;
		private BigDecimal r51_c8;
		private BigDecimal r51_total;

		// Row r52
		private String r52_product;
		private BigDecimal r52_usd;
		private BigDecimal r52_zar;
		private BigDecimal r52_gbp;
		private BigDecimal r52_euro;
		private BigDecimal r52_yen;
		private BigDecimal r52_c6;
		private BigDecimal r52_c7;
		private BigDecimal r52_c8;
		private BigDecimal r52_total;

		// Row r53
		private String r53_product;
		private BigDecimal r53_usd;
		private BigDecimal r53_zar;
		private BigDecimal r53_gbp;
		private BigDecimal r53_euro;
		private BigDecimal r53_yen;
		private BigDecimal r53_c6;
		private BigDecimal r53_c7;
		private BigDecimal r53_c8;
		private BigDecimal r53_total;

		// Row r54
		private String r54_product;
		private BigDecimal r54_usd;
		private BigDecimal r54_zar;
		private BigDecimal r54_gbp;
		private BigDecimal r54_euro;
		private BigDecimal r54_yen;
		private BigDecimal r54_c6;
		private BigDecimal r54_c7;
		private BigDecimal r54_c8;
		private BigDecimal r54_total;

		// Row r55
		private String r55_product;
		private BigDecimal r55_usd;
		private BigDecimal r55_zar;
		private BigDecimal r55_gbp;
		private BigDecimal r55_euro;
		private BigDecimal r55_yen;
		private BigDecimal r55_c6;
		private BigDecimal r55_c7;
		private BigDecimal r55_c8;
		private BigDecimal r55_total;

		// Row r56
		private String r56_product;
		private BigDecimal r56_usd;
		private BigDecimal r56_zar;
		private BigDecimal r56_gbp;
		private BigDecimal r56_euro;
		private BigDecimal r56_yen;
		private BigDecimal r56_c6;
		private BigDecimal r56_c7;
		private BigDecimal r56_c8;
		private BigDecimal r56_total;

		// Row r57
		private String r57_product;
		private BigDecimal r57_usd;
		private BigDecimal r57_zar;
		private BigDecimal r57_gbp;
		private BigDecimal r57_euro;
		private BigDecimal r57_yen;
		private BigDecimal r57_c6;
		private BigDecimal r57_c7;
		private BigDecimal r57_c8;
		private BigDecimal r57_total;

		// Row r58
		private String r58_product;
		private BigDecimal r58_usd;
		private BigDecimal r58_zar;
		private BigDecimal r58_gbp;
		private BigDecimal r58_euro;
		private BigDecimal r58_yen;
		private BigDecimal r58_c6;
		private BigDecimal r58_c7;
		private BigDecimal r58_c8;
		private BigDecimal r58_total;

		// Row r59
		private String r59_product;
		private BigDecimal r59_usd;
		private BigDecimal r59_zar;
		private BigDecimal r59_gbp;
		private BigDecimal r59_euro;
		private BigDecimal r59_yen;
		private BigDecimal r59_c6;
		private BigDecimal r59_c7;
		private BigDecimal r59_c8;
		private BigDecimal r59_total;

		// Row r60
		private String r60_product;
		private BigDecimal r60_usd;
		private BigDecimal r60_zar;
		private BigDecimal r60_gbp;
		private BigDecimal r60_euro;
		private BigDecimal r60_yen;
		private BigDecimal r60_c6;
		private BigDecimal r60_c7;
		private BigDecimal r60_c8;
		private BigDecimal r60_total;

		// Row r61
		private String r61_product;
		private BigDecimal r61_usd;
		private BigDecimal r61_zar;
		private BigDecimal r61_gbp;
		private BigDecimal r61_euro;
		private BigDecimal r61_yen;
		private BigDecimal r61_c6;
		private BigDecimal r61_c7;
		private BigDecimal r61_c8;
		private BigDecimal r61_total;

		// Row r62
		private String r62_product;
		private BigDecimal r62_usd;
		private BigDecimal r62_zar;
		private BigDecimal r62_gbp;
		private BigDecimal r62_euro;
		private BigDecimal r62_yen;
		private BigDecimal r62_c6;
		private BigDecimal r62_c7;
		private BigDecimal r62_c8;
		private BigDecimal r62_total;
		@Id
		@Column(name = "REPORT_VERSION", length = 100)
		private BigDecimal REPORT_VERSION;

		@Column(name = "REPORT_FREQUENCY", length = 100)
		private String REPORT_FREQUENCY;

		@Column(name = "REPORT_CODE", length = 100)
		private String REPORT_CODE;

		@Column(name = "REPORT_DESC", length = 100)
		private String REPORT_DESC;

		@Column(name = "ENTITY_FLG", length = 1)
		private String ENTITY_FLG;

		@Column(name = "MODIFY_FLG", length = 1)
		private String MODIFY_FLG;

		@Column(name = "DEL_FLG", length = 1)
		private String DEL_FLG;

		private Date REPORT_RESUBDATE;

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public String getR6_product() {
			return r6_product;
		}

		public void setR6_product(String r6_product) {
			this.r6_product = r6_product;
		}

		public BigDecimal getR6_usd() {
			return r6_usd;
		}

		public void setR6_usd(BigDecimal r6_usd) {
			this.r6_usd = r6_usd;
		}

		public BigDecimal getR6_zar() {
			return r6_zar;
		}

		public void setR6_zar(BigDecimal r6_zar) {
			this.r6_zar = r6_zar;
		}

		public BigDecimal getR6_gbp() {
			return r6_gbp;
		}

		public void setR6_gbp(BigDecimal r6_gbp) {
			this.r6_gbp = r6_gbp;
		}

		public BigDecimal getR6_euro() {
			return r6_euro;
		}

		public void setR6_euro(BigDecimal r6_euro) {
			this.r6_euro = r6_euro;
		}

		public BigDecimal getR6_yen() {
			return r6_yen;
		}

		public void setR6_yen(BigDecimal r6_yen) {
			this.r6_yen = r6_yen;
		}

		public BigDecimal getR6_c6() {
			return r6_c6;
		}

		public void setR6_c6(BigDecimal r6_c6) {
			this.r6_c6 = r6_c6;
		}

		public BigDecimal getR6_c7() {
			return r6_c7;
		}

		public void setR6_c7(BigDecimal r6_c7) {
			this.r6_c7 = r6_c7;
		}

		public BigDecimal getR6_c8() {
			return r6_c8;
		}

		public void setR6_c8(BigDecimal r6_c8) {
			this.r6_c8 = r6_c8;
		}

		public BigDecimal getR6_total() {
			return r6_total;
		}

		public void setR6_total(BigDecimal r6_total) {
			this.r6_total = r6_total;
		}

		public String getR7_product() {
			return r7_product;
		}

		public void setR7_product(String r7_product) {
			this.r7_product = r7_product;
		}

		public BigDecimal getR7_usd() {
			return r7_usd;
		}

		public void setR7_usd(BigDecimal r7_usd) {
			this.r7_usd = r7_usd;
		}

		public BigDecimal getR7_zar() {
			return r7_zar;
		}

		public void setR7_zar(BigDecimal r7_zar) {
			this.r7_zar = r7_zar;
		}

		public BigDecimal getR7_gbp() {
			return r7_gbp;
		}

		public void setR7_gbp(BigDecimal r7_gbp) {
			this.r7_gbp = r7_gbp;
		}

		public BigDecimal getR7_euro() {
			return r7_euro;
		}

		public void setR7_euro(BigDecimal r7_euro) {
			this.r7_euro = r7_euro;
		}

		public BigDecimal getR7_yen() {
			return r7_yen;
		}

		public void setR7_yen(BigDecimal r7_yen) {
			this.r7_yen = r7_yen;
		}

		public BigDecimal getR7_c6() {
			return r7_c6;
		}

		public void setR7_c6(BigDecimal r7_c6) {
			this.r7_c6 = r7_c6;
		}

		public BigDecimal getR7_c7() {
			return r7_c7;
		}

		public void setR7_c7(BigDecimal r7_c7) {
			this.r7_c7 = r7_c7;
		}

		public BigDecimal getR7_c8() {
			return r7_c8;
		}

		public void setR7_c8(BigDecimal r7_c8) {
			this.r7_c8 = r7_c8;
		}

		public BigDecimal getR7_total() {
			return r7_total;
		}

		public void setR7_total(BigDecimal r7_total) {
			this.r7_total = r7_total;
		}

		public String getR8_product() {
			return r8_product;
		}

		public void setR8_product(String r8_product) {
			this.r8_product = r8_product;
		}

		public BigDecimal getR8_usd() {
			return r8_usd;
		}

		public void setR8_usd(BigDecimal r8_usd) {
			this.r8_usd = r8_usd;
		}

		public BigDecimal getR8_zar() {
			return r8_zar;
		}

		public void setR8_zar(BigDecimal r8_zar) {
			this.r8_zar = r8_zar;
		}

		public BigDecimal getR8_gbp() {
			return r8_gbp;
		}

		public void setR8_gbp(BigDecimal r8_gbp) {
			this.r8_gbp = r8_gbp;
		}

		public BigDecimal getR8_euro() {
			return r8_euro;
		}

		public void setR8_euro(BigDecimal r8_euro) {
			this.r8_euro = r8_euro;
		}

		public BigDecimal getR8_yen() {
			return r8_yen;
		}

		public void setR8_yen(BigDecimal r8_yen) {
			this.r8_yen = r8_yen;
		}

		public BigDecimal getR8_c6() {
			return r8_c6;
		}

		public void setR8_c6(BigDecimal r8_c6) {
			this.r8_c6 = r8_c6;
		}

		public BigDecimal getR8_c7() {
			return r8_c7;
		}

		public void setR8_c7(BigDecimal r8_c7) {
			this.r8_c7 = r8_c7;
		}

		public BigDecimal getR8_c8() {
			return r8_c8;
		}

		public void setR8_c8(BigDecimal r8_c8) {
			this.r8_c8 = r8_c8;
		}

		public BigDecimal getR8_total() {
			return r8_total;
		}

		public void setR8_total(BigDecimal r8_total) {
			this.r8_total = r8_total;
		}

		public String getR9_product() {
			return r9_product;
		}

		public void setR9_product(String r9_product) {
			this.r9_product = r9_product;
		}

		public BigDecimal getR9_usd() {
			return r9_usd;
		}

		public void setR9_usd(BigDecimal r9_usd) {
			this.r9_usd = r9_usd;
		}

		public BigDecimal getR9_zar() {
			return r9_zar;
		}

		public void setR9_zar(BigDecimal r9_zar) {
			this.r9_zar = r9_zar;
		}

		public BigDecimal getR9_gbp() {
			return r9_gbp;
		}

		public void setR9_gbp(BigDecimal r9_gbp) {
			this.r9_gbp = r9_gbp;
		}

		public BigDecimal getR9_euro() {
			return r9_euro;
		}

		public void setR9_euro(BigDecimal r9_euro) {
			this.r9_euro = r9_euro;
		}

		public BigDecimal getR9_yen() {
			return r9_yen;
		}

		public void setR9_yen(BigDecimal r9_yen) {
			this.r9_yen = r9_yen;
		}

		public BigDecimal getR9_c6() {
			return r9_c6;
		}

		public void setR9_c6(BigDecimal r9_c6) {
			this.r9_c6 = r9_c6;
		}

		public BigDecimal getR9_c7() {
			return r9_c7;
		}

		public void setR9_c7(BigDecimal r9_c7) {
			this.r9_c7 = r9_c7;
		}

		public BigDecimal getR9_c8() {
			return r9_c8;
		}

		public void setR9_c8(BigDecimal r9_c8) {
			this.r9_c8 = r9_c8;
		}

		public BigDecimal getR9_total() {
			return r9_total;
		}

		public void setR9_total(BigDecimal r9_total) {
			this.r9_total = r9_total;
		}

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public BigDecimal getR10_usd() {
			return r10_usd;
		}

		public void setR10_usd(BigDecimal r10_usd) {
			this.r10_usd = r10_usd;
		}

		public BigDecimal getR10_zar() {
			return r10_zar;
		}

		public void setR10_zar(BigDecimal r10_zar) {
			this.r10_zar = r10_zar;
		}

		public BigDecimal getR10_gbp() {
			return r10_gbp;
		}

		public void setR10_gbp(BigDecimal r10_gbp) {
			this.r10_gbp = r10_gbp;
		}

		public BigDecimal getR10_euro() {
			return r10_euro;
		}

		public void setR10_euro(BigDecimal r10_euro) {
			this.r10_euro = r10_euro;
		}

		public BigDecimal getR10_yen() {
			return r10_yen;
		}

		public void setR10_yen(BigDecimal r10_yen) {
			this.r10_yen = r10_yen;
		}

		public BigDecimal getR10_c6() {
			return r10_c6;
		}

		public void setR10_c6(BigDecimal r10_c6) {
			this.r10_c6 = r10_c6;
		}

		public BigDecimal getR10_c7() {
			return r10_c7;
		}

		public void setR10_c7(BigDecimal r10_c7) {
			this.r10_c7 = r10_c7;
		}

		public BigDecimal getR10_c8() {
			return r10_c8;
		}

		public void setR10_c8(BigDecimal r10_c8) {
			this.r10_c8 = r10_c8;
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

		public BigDecimal getR11_usd() {
			return r11_usd;
		}

		public void setR11_usd(BigDecimal r11_usd) {
			this.r11_usd = r11_usd;
		}

		public BigDecimal getR11_zar() {
			return r11_zar;
		}

		public void setR11_zar(BigDecimal r11_zar) {
			this.r11_zar = r11_zar;
		}

		public BigDecimal getR11_gbp() {
			return r11_gbp;
		}

		public void setR11_gbp(BigDecimal r11_gbp) {
			this.r11_gbp = r11_gbp;
		}

		public BigDecimal getR11_euro() {
			return r11_euro;
		}

		public void setR11_euro(BigDecimal r11_euro) {
			this.r11_euro = r11_euro;
		}

		public BigDecimal getR11_yen() {
			return r11_yen;
		}

		public void setR11_yen(BigDecimal r11_yen) {
			this.r11_yen = r11_yen;
		}

		public BigDecimal getR11_c6() {
			return r11_c6;
		}

		public void setR11_c6(BigDecimal r11_c6) {
			this.r11_c6 = r11_c6;
		}

		public BigDecimal getR11_c7() {
			return r11_c7;
		}

		public void setR11_c7(BigDecimal r11_c7) {
			this.r11_c7 = r11_c7;
		}

		public BigDecimal getR11_c8() {
			return r11_c8;
		}

		public void setR11_c8(BigDecimal r11_c8) {
			this.r11_c8 = r11_c8;
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

		public BigDecimal getR12_usd() {
			return r12_usd;
		}

		public void setR12_usd(BigDecimal r12_usd) {
			this.r12_usd = r12_usd;
		}

		public BigDecimal getR12_zar() {
			return r12_zar;
		}

		public void setR12_zar(BigDecimal r12_zar) {
			this.r12_zar = r12_zar;
		}

		public BigDecimal getR12_gbp() {
			return r12_gbp;
		}

		public void setR12_gbp(BigDecimal r12_gbp) {
			this.r12_gbp = r12_gbp;
		}

		public BigDecimal getR12_euro() {
			return r12_euro;
		}

		public void setR12_euro(BigDecimal r12_euro) {
			this.r12_euro = r12_euro;
		}

		public BigDecimal getR12_yen() {
			return r12_yen;
		}

		public void setR12_yen(BigDecimal r12_yen) {
			this.r12_yen = r12_yen;
		}

		public BigDecimal getR12_c6() {
			return r12_c6;
		}

		public void setR12_c6(BigDecimal r12_c6) {
			this.r12_c6 = r12_c6;
		}

		public BigDecimal getR12_c7() {
			return r12_c7;
		}

		public void setR12_c7(BigDecimal r12_c7) {
			this.r12_c7 = r12_c7;
		}

		public BigDecimal getR12_c8() {
			return r12_c8;
		}

		public void setR12_c8(BigDecimal r12_c8) {
			this.r12_c8 = r12_c8;
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

		public BigDecimal getR13_usd() {
			return r13_usd;
		}

		public void setR13_usd(BigDecimal r13_usd) {
			this.r13_usd = r13_usd;
		}

		public BigDecimal getR13_zar() {
			return r13_zar;
		}

		public void setR13_zar(BigDecimal r13_zar) {
			this.r13_zar = r13_zar;
		}

		public BigDecimal getR13_gbp() {
			return r13_gbp;
		}

		public void setR13_gbp(BigDecimal r13_gbp) {
			this.r13_gbp = r13_gbp;
		}

		public BigDecimal getR13_euro() {
			return r13_euro;
		}

		public void setR13_euro(BigDecimal r13_euro) {
			this.r13_euro = r13_euro;
		}

		public BigDecimal getR13_yen() {
			return r13_yen;
		}

		public void setR13_yen(BigDecimal r13_yen) {
			this.r13_yen = r13_yen;
		}

		public BigDecimal getR13_c6() {
			return r13_c6;
		}

		public void setR13_c6(BigDecimal r13_c6) {
			this.r13_c6 = r13_c6;
		}

		public BigDecimal getR13_c7() {
			return r13_c7;
		}

		public void setR13_c7(BigDecimal r13_c7) {
			this.r13_c7 = r13_c7;
		}

		public BigDecimal getR13_c8() {
			return r13_c8;
		}

		public void setR13_c8(BigDecimal r13_c8) {
			this.r13_c8 = r13_c8;
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

		public BigDecimal getR14_usd() {
			return r14_usd;
		}

		public void setR14_usd(BigDecimal r14_usd) {
			this.r14_usd = r14_usd;
		}

		public BigDecimal getR14_zar() {
			return r14_zar;
		}

		public void setR14_zar(BigDecimal r14_zar) {
			this.r14_zar = r14_zar;
		}

		public BigDecimal getR14_gbp() {
			return r14_gbp;
		}

		public void setR14_gbp(BigDecimal r14_gbp) {
			this.r14_gbp = r14_gbp;
		}

		public BigDecimal getR14_euro() {
			return r14_euro;
		}

		public void setR14_euro(BigDecimal r14_euro) {
			this.r14_euro = r14_euro;
		}

		public BigDecimal getR14_yen() {
			return r14_yen;
		}

		public void setR14_yen(BigDecimal r14_yen) {
			this.r14_yen = r14_yen;
		}

		public BigDecimal getR14_c6() {
			return r14_c6;
		}

		public void setR14_c6(BigDecimal r14_c6) {
			this.r14_c6 = r14_c6;
		}

		public BigDecimal getR14_c7() {
			return r14_c7;
		}

		public void setR14_c7(BigDecimal r14_c7) {
			this.r14_c7 = r14_c7;
		}

		public BigDecimal getR14_c8() {
			return r14_c8;
		}

		public void setR14_c8(BigDecimal r14_c8) {
			this.r14_c8 = r14_c8;
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

		public BigDecimal getR15_usd() {
			return r15_usd;
		}

		public void setR15_usd(BigDecimal r15_usd) {
			this.r15_usd = r15_usd;
		}

		public BigDecimal getR15_zar() {
			return r15_zar;
		}

		public void setR15_zar(BigDecimal r15_zar) {
			this.r15_zar = r15_zar;
		}

		public BigDecimal getR15_gbp() {
			return r15_gbp;
		}

		public void setR15_gbp(BigDecimal r15_gbp) {
			this.r15_gbp = r15_gbp;
		}

		public BigDecimal getR15_euro() {
			return r15_euro;
		}

		public void setR15_euro(BigDecimal r15_euro) {
			this.r15_euro = r15_euro;
		}

		public BigDecimal getR15_yen() {
			return r15_yen;
		}

		public void setR15_yen(BigDecimal r15_yen) {
			this.r15_yen = r15_yen;
		}

		public BigDecimal getR15_c6() {
			return r15_c6;
		}

		public void setR15_c6(BigDecimal r15_c6) {
			this.r15_c6 = r15_c6;
		}

		public BigDecimal getR15_c7() {
			return r15_c7;
		}

		public void setR15_c7(BigDecimal r15_c7) {
			this.r15_c7 = r15_c7;
		}

		public BigDecimal getR15_c8() {
			return r15_c8;
		}

		public void setR15_c8(BigDecimal r15_c8) {
			this.r15_c8 = r15_c8;
		}

		public BigDecimal getR15_total() {
			return r15_total;
		}

		public void setR15_total(BigDecimal r15_total) {
			this.r15_total = r15_total;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_usd() {
			return r16_usd;
		}

		public void setR16_usd(BigDecimal r16_usd) {
			this.r16_usd = r16_usd;
		}

		public BigDecimal getR16_zar() {
			return r16_zar;
		}

		public void setR16_zar(BigDecimal r16_zar) {
			this.r16_zar = r16_zar;
		}

		public BigDecimal getR16_gbp() {
			return r16_gbp;
		}

		public void setR16_gbp(BigDecimal r16_gbp) {
			this.r16_gbp = r16_gbp;
		}

		public BigDecimal getR16_euro() {
			return r16_euro;
		}

		public void setR16_euro(BigDecimal r16_euro) {
			this.r16_euro = r16_euro;
		}

		public BigDecimal getR16_yen() {
			return r16_yen;
		}

		public void setR16_yen(BigDecimal r16_yen) {
			this.r16_yen = r16_yen;
		}

		public BigDecimal getR16_c6() {
			return r16_c6;
		}

		public void setR16_c6(BigDecimal r16_c6) {
			this.r16_c6 = r16_c6;
		}

		public BigDecimal getR16_c7() {
			return r16_c7;
		}

		public void setR16_c7(BigDecimal r16_c7) {
			this.r16_c7 = r16_c7;
		}

		public BigDecimal getR16_c8() {
			return r16_c8;
		}

		public void setR16_c8(BigDecimal r16_c8) {
			this.r16_c8 = r16_c8;
		}

		public BigDecimal getR16_total() {
			return r16_total;
		}

		public void setR16_total(BigDecimal r16_total) {
			this.r16_total = r16_total;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_usd() {
			return r17_usd;
		}

		public void setR17_usd(BigDecimal r17_usd) {
			this.r17_usd = r17_usd;
		}

		public BigDecimal getR17_zar() {
			return r17_zar;
		}

		public void setR17_zar(BigDecimal r17_zar) {
			this.r17_zar = r17_zar;
		}

		public BigDecimal getR17_gbp() {
			return r17_gbp;
		}

		public void setR17_gbp(BigDecimal r17_gbp) {
			this.r17_gbp = r17_gbp;
		}

		public BigDecimal getR17_euro() {
			return r17_euro;
		}

		public void setR17_euro(BigDecimal r17_euro) {
			this.r17_euro = r17_euro;
		}

		public BigDecimal getR17_yen() {
			return r17_yen;
		}

		public void setR17_yen(BigDecimal r17_yen) {
			this.r17_yen = r17_yen;
		}

		public BigDecimal getR17_c6() {
			return r17_c6;
		}

		public void setR17_c6(BigDecimal r17_c6) {
			this.r17_c6 = r17_c6;
		}

		public BigDecimal getR17_c7() {
			return r17_c7;
		}

		public void setR17_c7(BigDecimal r17_c7) {
			this.r17_c7 = r17_c7;
		}

		public BigDecimal getR17_c8() {
			return r17_c8;
		}

		public void setR17_c8(BigDecimal r17_c8) {
			this.r17_c8 = r17_c8;
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

		public BigDecimal getR18_usd() {
			return r18_usd;
		}

		public void setR18_usd(BigDecimal r18_usd) {
			this.r18_usd = r18_usd;
		}

		public BigDecimal getR18_zar() {
			return r18_zar;
		}

		public void setR18_zar(BigDecimal r18_zar) {
			this.r18_zar = r18_zar;
		}

		public BigDecimal getR18_gbp() {
			return r18_gbp;
		}

		public void setR18_gbp(BigDecimal r18_gbp) {
			this.r18_gbp = r18_gbp;
		}

		public BigDecimal getR18_euro() {
			return r18_euro;
		}

		public void setR18_euro(BigDecimal r18_euro) {
			this.r18_euro = r18_euro;
		}

		public BigDecimal getR18_yen() {
			return r18_yen;
		}

		public void setR18_yen(BigDecimal r18_yen) {
			this.r18_yen = r18_yen;
		}

		public BigDecimal getR18_c6() {
			return r18_c6;
		}

		public void setR18_c6(BigDecimal r18_c6) {
			this.r18_c6 = r18_c6;
		}

		public BigDecimal getR18_c7() {
			return r18_c7;
		}

		public void setR18_c7(BigDecimal r18_c7) {
			this.r18_c7 = r18_c7;
		}

		public BigDecimal getR18_c8() {
			return r18_c8;
		}

		public void setR18_c8(BigDecimal r18_c8) {
			this.r18_c8 = r18_c8;
		}

		public BigDecimal getR18_total() {
			return r18_total;
		}

		public void setR18_total(BigDecimal r18_total) {
			this.r18_total = r18_total;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_usd() {
			return r19_usd;
		}

		public void setR19_usd(BigDecimal r19_usd) {
			this.r19_usd = r19_usd;
		}

		public BigDecimal getR19_zar() {
			return r19_zar;
		}

		public void setR19_zar(BigDecimal r19_zar) {
			this.r19_zar = r19_zar;
		}

		public BigDecimal getR19_gbp() {
			return r19_gbp;
		}

		public void setR19_gbp(BigDecimal r19_gbp) {
			this.r19_gbp = r19_gbp;
		}

		public BigDecimal getR19_euro() {
			return r19_euro;
		}

		public void setR19_euro(BigDecimal r19_euro) {
			this.r19_euro = r19_euro;
		}

		public BigDecimal getR19_yen() {
			return r19_yen;
		}

		public void setR19_yen(BigDecimal r19_yen) {
			this.r19_yen = r19_yen;
		}

		public BigDecimal getR19_c6() {
			return r19_c6;
		}

		public void setR19_c6(BigDecimal r19_c6) {
			this.r19_c6 = r19_c6;
		}

		public BigDecimal getR19_c7() {
			return r19_c7;
		}

		public void setR19_c7(BigDecimal r19_c7) {
			this.r19_c7 = r19_c7;
		}

		public BigDecimal getR19_c8() {
			return r19_c8;
		}

		public void setR19_c8(BigDecimal r19_c8) {
			this.r19_c8 = r19_c8;
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

		public BigDecimal getR20_usd() {
			return r20_usd;
		}

		public void setR20_usd(BigDecimal r20_usd) {
			this.r20_usd = r20_usd;
		}

		public BigDecimal getR20_zar() {
			return r20_zar;
		}

		public void setR20_zar(BigDecimal r20_zar) {
			this.r20_zar = r20_zar;
		}

		public BigDecimal getR20_gbp() {
			return r20_gbp;
		}

		public void setR20_gbp(BigDecimal r20_gbp) {
			this.r20_gbp = r20_gbp;
		}

		public BigDecimal getR20_euro() {
			return r20_euro;
		}

		public void setR20_euro(BigDecimal r20_euro) {
			this.r20_euro = r20_euro;
		}

		public BigDecimal getR20_yen() {
			return r20_yen;
		}

		public void setR20_yen(BigDecimal r20_yen) {
			this.r20_yen = r20_yen;
		}

		public BigDecimal getR20_c6() {
			return r20_c6;
		}

		public void setR20_c6(BigDecimal r20_c6) {
			this.r20_c6 = r20_c6;
		}

		public BigDecimal getR20_c7() {
			return r20_c7;
		}

		public void setR20_c7(BigDecimal r20_c7) {
			this.r20_c7 = r20_c7;
		}

		public BigDecimal getR20_c8() {
			return r20_c8;
		}

		public void setR20_c8(BigDecimal r20_c8) {
			this.r20_c8 = r20_c8;
		}

		public BigDecimal getR20_total() {
			return r20_total;
		}

		public void setR20_total(BigDecimal r20_total) {
			this.r20_total = r20_total;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_usd() {
			return r21_usd;
		}

		public void setR21_usd(BigDecimal r21_usd) {
			this.r21_usd = r21_usd;
		}

		public BigDecimal getR21_zar() {
			return r21_zar;
		}

		public void setR21_zar(BigDecimal r21_zar) {
			this.r21_zar = r21_zar;
		}

		public BigDecimal getR21_gbp() {
			return r21_gbp;
		}

		public void setR21_gbp(BigDecimal r21_gbp) {
			this.r21_gbp = r21_gbp;
		}

		public BigDecimal getR21_euro() {
			return r21_euro;
		}

		public void setR21_euro(BigDecimal r21_euro) {
			this.r21_euro = r21_euro;
		}

		public BigDecimal getR21_yen() {
			return r21_yen;
		}

		public void setR21_yen(BigDecimal r21_yen) {
			this.r21_yen = r21_yen;
		}

		public BigDecimal getR21_c6() {
			return r21_c6;
		}

		public void setR21_c6(BigDecimal r21_c6) {
			this.r21_c6 = r21_c6;
		}

		public BigDecimal getR21_c7() {
			return r21_c7;
		}

		public void setR21_c7(BigDecimal r21_c7) {
			this.r21_c7 = r21_c7;
		}

		public BigDecimal getR21_c8() {
			return r21_c8;
		}

		public void setR21_c8(BigDecimal r21_c8) {
			this.r21_c8 = r21_c8;
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

		public BigDecimal getR22_usd() {
			return r22_usd;
		}

		public void setR22_usd(BigDecimal r22_usd) {
			this.r22_usd = r22_usd;
		}

		public BigDecimal getR22_zar() {
			return r22_zar;
		}

		public void setR22_zar(BigDecimal r22_zar) {
			this.r22_zar = r22_zar;
		}

		public BigDecimal getR22_gbp() {
			return r22_gbp;
		}

		public void setR22_gbp(BigDecimal r22_gbp) {
			this.r22_gbp = r22_gbp;
		}

		public BigDecimal getR22_euro() {
			return r22_euro;
		}

		public void setR22_euro(BigDecimal r22_euro) {
			this.r22_euro = r22_euro;
		}

		public BigDecimal getR22_yen() {
			return r22_yen;
		}

		public void setR22_yen(BigDecimal r22_yen) {
			this.r22_yen = r22_yen;
		}

		public BigDecimal getR22_c6() {
			return r22_c6;
		}

		public void setR22_c6(BigDecimal r22_c6) {
			this.r22_c6 = r22_c6;
		}

		public BigDecimal getR22_c7() {
			return r22_c7;
		}

		public void setR22_c7(BigDecimal r22_c7) {
			this.r22_c7 = r22_c7;
		}

		public BigDecimal getR22_c8() {
			return r22_c8;
		}

		public void setR22_c8(BigDecimal r22_c8) {
			this.r22_c8 = r22_c8;
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

		public BigDecimal getR23_usd() {
			return r23_usd;
		}

		public void setR23_usd(BigDecimal r23_usd) {
			this.r23_usd = r23_usd;
		}

		public BigDecimal getR23_zar() {
			return r23_zar;
		}

		public void setR23_zar(BigDecimal r23_zar) {
			this.r23_zar = r23_zar;
		}

		public BigDecimal getR23_gbp() {
			return r23_gbp;
		}

		public void setR23_gbp(BigDecimal r23_gbp) {
			this.r23_gbp = r23_gbp;
		}

		public BigDecimal getR23_euro() {
			return r23_euro;
		}

		public void setR23_euro(BigDecimal r23_euro) {
			this.r23_euro = r23_euro;
		}

		public BigDecimal getR23_yen() {
			return r23_yen;
		}

		public void setR23_yen(BigDecimal r23_yen) {
			this.r23_yen = r23_yen;
		}

		public BigDecimal getR23_c6() {
			return r23_c6;
		}

		public void setR23_c6(BigDecimal r23_c6) {
			this.r23_c6 = r23_c6;
		}

		public BigDecimal getR23_c7() {
			return r23_c7;
		}

		public void setR23_c7(BigDecimal r23_c7) {
			this.r23_c7 = r23_c7;
		}

		public BigDecimal getR23_c8() {
			return r23_c8;
		}

		public void setR23_c8(BigDecimal r23_c8) {
			this.r23_c8 = r23_c8;
		}

		public BigDecimal getR23_total() {
			return r23_total;
		}

		public void setR23_total(BigDecimal r23_total) {
			this.r23_total = r23_total;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_usd() {
			return r24_usd;
		}

		public void setR24_usd(BigDecimal r24_usd) {
			this.r24_usd = r24_usd;
		}

		public BigDecimal getR24_zar() {
			return r24_zar;
		}

		public void setR24_zar(BigDecimal r24_zar) {
			this.r24_zar = r24_zar;
		}

		public BigDecimal getR24_gbp() {
			return r24_gbp;
		}

		public void setR24_gbp(BigDecimal r24_gbp) {
			this.r24_gbp = r24_gbp;
		}

		public BigDecimal getR24_euro() {
			return r24_euro;
		}

		public void setR24_euro(BigDecimal r24_euro) {
			this.r24_euro = r24_euro;
		}

		public BigDecimal getR24_yen() {
			return r24_yen;
		}

		public void setR24_yen(BigDecimal r24_yen) {
			this.r24_yen = r24_yen;
		}

		public BigDecimal getR24_c6() {
			return r24_c6;
		}

		public void setR24_c6(BigDecimal r24_c6) {
			this.r24_c6 = r24_c6;
		}

		public BigDecimal getR24_c7() {
			return r24_c7;
		}

		public void setR24_c7(BigDecimal r24_c7) {
			this.r24_c7 = r24_c7;
		}

		public BigDecimal getR24_c8() {
			return r24_c8;
		}

		public void setR24_c8(BigDecimal r24_c8) {
			this.r24_c8 = r24_c8;
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

		public BigDecimal getR25_usd() {
			return r25_usd;
		}

		public void setR25_usd(BigDecimal r25_usd) {
			this.r25_usd = r25_usd;
		}

		public BigDecimal getR25_zar() {
			return r25_zar;
		}

		public void setR25_zar(BigDecimal r25_zar) {
			this.r25_zar = r25_zar;
		}

		public BigDecimal getR25_gbp() {
			return r25_gbp;
		}

		public void setR25_gbp(BigDecimal r25_gbp) {
			this.r25_gbp = r25_gbp;
		}

		public BigDecimal getR25_euro() {
			return r25_euro;
		}

		public void setR25_euro(BigDecimal r25_euro) {
			this.r25_euro = r25_euro;
		}

		public BigDecimal getR25_yen() {
			return r25_yen;
		}

		public void setR25_yen(BigDecimal r25_yen) {
			this.r25_yen = r25_yen;
		}

		public BigDecimal getR25_c6() {
			return r25_c6;
		}

		public void setR25_c6(BigDecimal r25_c6) {
			this.r25_c6 = r25_c6;
		}

		public BigDecimal getR25_c7() {
			return r25_c7;
		}

		public void setR25_c7(BigDecimal r25_c7) {
			this.r25_c7 = r25_c7;
		}

		public BigDecimal getR25_c8() {
			return r25_c8;
		}

		public void setR25_c8(BigDecimal r25_c8) {
			this.r25_c8 = r25_c8;
		}

		public BigDecimal getR25_total() {
			return r25_total;
		}

		public void setR25_total(BigDecimal r25_total) {
			this.r25_total = r25_total;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_usd() {
			return r26_usd;
		}

		public void setR26_usd(BigDecimal r26_usd) {
			this.r26_usd = r26_usd;
		}

		public BigDecimal getR26_zar() {
			return r26_zar;
		}

		public void setR26_zar(BigDecimal r26_zar) {
			this.r26_zar = r26_zar;
		}

		public BigDecimal getR26_gbp() {
			return r26_gbp;
		}

		public void setR26_gbp(BigDecimal r26_gbp) {
			this.r26_gbp = r26_gbp;
		}

		public BigDecimal getR26_euro() {
			return r26_euro;
		}

		public void setR26_euro(BigDecimal r26_euro) {
			this.r26_euro = r26_euro;
		}

		public BigDecimal getR26_yen() {
			return r26_yen;
		}

		public void setR26_yen(BigDecimal r26_yen) {
			this.r26_yen = r26_yen;
		}

		public BigDecimal getR26_c6() {
			return r26_c6;
		}

		public void setR26_c6(BigDecimal r26_c6) {
			this.r26_c6 = r26_c6;
		}

		public BigDecimal getR26_c7() {
			return r26_c7;
		}

		public void setR26_c7(BigDecimal r26_c7) {
			this.r26_c7 = r26_c7;
		}

		public BigDecimal getR26_c8() {
			return r26_c8;
		}

		public void setR26_c8(BigDecimal r26_c8) {
			this.r26_c8 = r26_c8;
		}

		public BigDecimal getR26_total() {
			return r26_total;
		}

		public void setR26_total(BigDecimal r26_total) {
			this.r26_total = r26_total;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_usd() {
			return r27_usd;
		}

		public void setR27_usd(BigDecimal r27_usd) {
			this.r27_usd = r27_usd;
		}

		public BigDecimal getR27_zar() {
			return r27_zar;
		}

		public void setR27_zar(BigDecimal r27_zar) {
			this.r27_zar = r27_zar;
		}

		public BigDecimal getR27_gbp() {
			return r27_gbp;
		}

		public void setR27_gbp(BigDecimal r27_gbp) {
			this.r27_gbp = r27_gbp;
		}

		public BigDecimal getR27_euro() {
			return r27_euro;
		}

		public void setR27_euro(BigDecimal r27_euro) {
			this.r27_euro = r27_euro;
		}

		public BigDecimal getR27_yen() {
			return r27_yen;
		}

		public void setR27_yen(BigDecimal r27_yen) {
			this.r27_yen = r27_yen;
		}

		public BigDecimal getR27_c6() {
			return r27_c6;
		}

		public void setR27_c6(BigDecimal r27_c6) {
			this.r27_c6 = r27_c6;
		}

		public BigDecimal getR27_c7() {
			return r27_c7;
		}

		public void setR27_c7(BigDecimal r27_c7) {
			this.r27_c7 = r27_c7;
		}

		public BigDecimal getR27_c8() {
			return r27_c8;
		}

		public void setR27_c8(BigDecimal r27_c8) {
			this.r27_c8 = r27_c8;
		}

		public BigDecimal getR27_total() {
			return r27_total;
		}

		public void setR27_total(BigDecimal r27_total) {
			this.r27_total = r27_total;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_usd() {
			return r28_usd;
		}

		public void setR28_usd(BigDecimal r28_usd) {
			this.r28_usd = r28_usd;
		}

		public BigDecimal getR28_zar() {
			return r28_zar;
		}

		public void setR28_zar(BigDecimal r28_zar) {
			this.r28_zar = r28_zar;
		}

		public BigDecimal getR28_gbp() {
			return r28_gbp;
		}

		public void setR28_gbp(BigDecimal r28_gbp) {
			this.r28_gbp = r28_gbp;
		}

		public BigDecimal getR28_euro() {
			return r28_euro;
		}

		public void setR28_euro(BigDecimal r28_euro) {
			this.r28_euro = r28_euro;
		}

		public BigDecimal getR28_yen() {
			return r28_yen;
		}

		public void setR28_yen(BigDecimal r28_yen) {
			this.r28_yen = r28_yen;
		}

		public BigDecimal getR28_c6() {
			return r28_c6;
		}

		public void setR28_c6(BigDecimal r28_c6) {
			this.r28_c6 = r28_c6;
		}

		public BigDecimal getR28_c7() {
			return r28_c7;
		}

		public void setR28_c7(BigDecimal r28_c7) {
			this.r28_c7 = r28_c7;
		}

		public BigDecimal getR28_c8() {
			return r28_c8;
		}

		public void setR28_c8(BigDecimal r28_c8) {
			this.r28_c8 = r28_c8;
		}

		public BigDecimal getR28_total() {
			return r28_total;
		}

		public void setR28_total(BigDecimal r28_total) {
			this.r28_total = r28_total;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_usd() {
			return r29_usd;
		}

		public void setR29_usd(BigDecimal r29_usd) {
			this.r29_usd = r29_usd;
		}

		public BigDecimal getR29_zar() {
			return r29_zar;
		}

		public void setR29_zar(BigDecimal r29_zar) {
			this.r29_zar = r29_zar;
		}

		public BigDecimal getR29_gbp() {
			return r29_gbp;
		}

		public void setR29_gbp(BigDecimal r29_gbp) {
			this.r29_gbp = r29_gbp;
		}

		public BigDecimal getR29_euro() {
			return r29_euro;
		}

		public void setR29_euro(BigDecimal r29_euro) {
			this.r29_euro = r29_euro;
		}

		public BigDecimal getR29_yen() {
			return r29_yen;
		}

		public void setR29_yen(BigDecimal r29_yen) {
			this.r29_yen = r29_yen;
		}

		public BigDecimal getR29_c6() {
			return r29_c6;
		}

		public void setR29_c6(BigDecimal r29_c6) {
			this.r29_c6 = r29_c6;
		}

		public BigDecimal getR29_c7() {
			return r29_c7;
		}

		public void setR29_c7(BigDecimal r29_c7) {
			this.r29_c7 = r29_c7;
		}

		public BigDecimal getR29_c8() {
			return r29_c8;
		}

		public void setR29_c8(BigDecimal r29_c8) {
			this.r29_c8 = r29_c8;
		}

		public BigDecimal getR29_total() {
			return r29_total;
		}

		public void setR29_total(BigDecimal r29_total) {
			this.r29_total = r29_total;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_usd() {
			return r30_usd;
		}

		public void setR30_usd(BigDecimal r30_usd) {
			this.r30_usd = r30_usd;
		}

		public BigDecimal getR30_zar() {
			return r30_zar;
		}

		public void setR30_zar(BigDecimal r30_zar) {
			this.r30_zar = r30_zar;
		}

		public BigDecimal getR30_gbp() {
			return r30_gbp;
		}

		public void setR30_gbp(BigDecimal r30_gbp) {
			this.r30_gbp = r30_gbp;
		}

		public BigDecimal getR30_euro() {
			return r30_euro;
		}

		public void setR30_euro(BigDecimal r30_euro) {
			this.r30_euro = r30_euro;
		}

		public BigDecimal getR30_yen() {
			return r30_yen;
		}

		public void setR30_yen(BigDecimal r30_yen) {
			this.r30_yen = r30_yen;
		}

		public BigDecimal getR30_c6() {
			return r30_c6;
		}

		public void setR30_c6(BigDecimal r30_c6) {
			this.r30_c6 = r30_c6;
		}

		public BigDecimal getR30_c7() {
			return r30_c7;
		}

		public void setR30_c7(BigDecimal r30_c7) {
			this.r30_c7 = r30_c7;
		}

		public BigDecimal getR30_c8() {
			return r30_c8;
		}

		public void setR30_c8(BigDecimal r30_c8) {
			this.r30_c8 = r30_c8;
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

		public BigDecimal getR31_usd() {
			return r31_usd;
		}

		public void setR31_usd(BigDecimal r31_usd) {
			this.r31_usd = r31_usd;
		}

		public BigDecimal getR31_zar() {
			return r31_zar;
		}

		public void setR31_zar(BigDecimal r31_zar) {
			this.r31_zar = r31_zar;
		}

		public BigDecimal getR31_gbp() {
			return r31_gbp;
		}

		public void setR31_gbp(BigDecimal r31_gbp) {
			this.r31_gbp = r31_gbp;
		}

		public BigDecimal getR31_euro() {
			return r31_euro;
		}

		public void setR31_euro(BigDecimal r31_euro) {
			this.r31_euro = r31_euro;
		}

		public BigDecimal getR31_yen() {
			return r31_yen;
		}

		public void setR31_yen(BigDecimal r31_yen) {
			this.r31_yen = r31_yen;
		}

		public BigDecimal getR31_c6() {
			return r31_c6;
		}

		public void setR31_c6(BigDecimal r31_c6) {
			this.r31_c6 = r31_c6;
		}

		public BigDecimal getR31_c7() {
			return r31_c7;
		}

		public void setR31_c7(BigDecimal r31_c7) {
			this.r31_c7 = r31_c7;
		}

		public BigDecimal getR31_c8() {
			return r31_c8;
		}

		public void setR31_c8(BigDecimal r31_c8) {
			this.r31_c8 = r31_c8;
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

		public BigDecimal getR32_usd() {
			return r32_usd;
		}

		public void setR32_usd(BigDecimal r32_usd) {
			this.r32_usd = r32_usd;
		}

		public BigDecimal getR32_zar() {
			return r32_zar;
		}

		public void setR32_zar(BigDecimal r32_zar) {
			this.r32_zar = r32_zar;
		}

		public BigDecimal getR32_gbp() {
			return r32_gbp;
		}

		public void setR32_gbp(BigDecimal r32_gbp) {
			this.r32_gbp = r32_gbp;
		}

		public BigDecimal getR32_euro() {
			return r32_euro;
		}

		public void setR32_euro(BigDecimal r32_euro) {
			this.r32_euro = r32_euro;
		}

		public BigDecimal getR32_yen() {
			return r32_yen;
		}

		public void setR32_yen(BigDecimal r32_yen) {
			this.r32_yen = r32_yen;
		}

		public BigDecimal getR32_c6() {
			return r32_c6;
		}

		public void setR32_c6(BigDecimal r32_c6) {
			this.r32_c6 = r32_c6;
		}

		public BigDecimal getR32_c7() {
			return r32_c7;
		}

		public void setR32_c7(BigDecimal r32_c7) {
			this.r32_c7 = r32_c7;
		}

		public BigDecimal getR32_c8() {
			return r32_c8;
		}

		public void setR32_c8(BigDecimal r32_c8) {
			this.r32_c8 = r32_c8;
		}

		public BigDecimal getR32_total() {
			return r32_total;
		}

		public void setR32_total(BigDecimal r32_total) {
			this.r32_total = r32_total;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_usd() {
			return r33_usd;
		}

		public void setR33_usd(BigDecimal r33_usd) {
			this.r33_usd = r33_usd;
		}

		public BigDecimal getR33_zar() {
			return r33_zar;
		}

		public void setR33_zar(BigDecimal r33_zar) {
			this.r33_zar = r33_zar;
		}

		public BigDecimal getR33_gbp() {
			return r33_gbp;
		}

		public void setR33_gbp(BigDecimal r33_gbp) {
			this.r33_gbp = r33_gbp;
		}

		public BigDecimal getR33_euro() {
			return r33_euro;
		}

		public void setR33_euro(BigDecimal r33_euro) {
			this.r33_euro = r33_euro;
		}

		public BigDecimal getR33_yen() {
			return r33_yen;
		}

		public void setR33_yen(BigDecimal r33_yen) {
			this.r33_yen = r33_yen;
		}

		public BigDecimal getR33_c6() {
			return r33_c6;
		}

		public void setR33_c6(BigDecimal r33_c6) {
			this.r33_c6 = r33_c6;
		}

		public BigDecimal getR33_c7() {
			return r33_c7;
		}

		public void setR33_c7(BigDecimal r33_c7) {
			this.r33_c7 = r33_c7;
		}

		public BigDecimal getR33_c8() {
			return r33_c8;
		}

		public void setR33_c8(BigDecimal r33_c8) {
			this.r33_c8 = r33_c8;
		}

		public BigDecimal getR33_total() {
			return r33_total;
		}

		public void setR33_total(BigDecimal r33_total) {
			this.r33_total = r33_total;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_usd() {
			return r34_usd;
		}

		public void setR34_usd(BigDecimal r34_usd) {
			this.r34_usd = r34_usd;
		}

		public BigDecimal getR34_zar() {
			return r34_zar;
		}

		public void setR34_zar(BigDecimal r34_zar) {
			this.r34_zar = r34_zar;
		}

		public BigDecimal getR34_gbp() {
			return r34_gbp;
		}

		public void setR34_gbp(BigDecimal r34_gbp) {
			this.r34_gbp = r34_gbp;
		}

		public BigDecimal getR34_euro() {
			return r34_euro;
		}

		public void setR34_euro(BigDecimal r34_euro) {
			this.r34_euro = r34_euro;
		}

		public BigDecimal getR34_yen() {
			return r34_yen;
		}

		public void setR34_yen(BigDecimal r34_yen) {
			this.r34_yen = r34_yen;
		}

		public BigDecimal getR34_c6() {
			return r34_c6;
		}

		public void setR34_c6(BigDecimal r34_c6) {
			this.r34_c6 = r34_c6;
		}

		public BigDecimal getR34_c7() {
			return r34_c7;
		}

		public void setR34_c7(BigDecimal r34_c7) {
			this.r34_c7 = r34_c7;
		}

		public BigDecimal getR34_c8() {
			return r34_c8;
		}

		public void setR34_c8(BigDecimal r34_c8) {
			this.r34_c8 = r34_c8;
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

		public BigDecimal getR35_usd() {
			return r35_usd;
		}

		public void setR35_usd(BigDecimal r35_usd) {
			this.r35_usd = r35_usd;
		}

		public BigDecimal getR35_zar() {
			return r35_zar;
		}

		public void setR35_zar(BigDecimal r35_zar) {
			this.r35_zar = r35_zar;
		}

		public BigDecimal getR35_gbp() {
			return r35_gbp;
		}

		public void setR35_gbp(BigDecimal r35_gbp) {
			this.r35_gbp = r35_gbp;
		}

		public BigDecimal getR35_euro() {
			return r35_euro;
		}

		public void setR35_euro(BigDecimal r35_euro) {
			this.r35_euro = r35_euro;
		}

		public BigDecimal getR35_yen() {
			return r35_yen;
		}

		public void setR35_yen(BigDecimal r35_yen) {
			this.r35_yen = r35_yen;
		}

		public BigDecimal getR35_c6() {
			return r35_c6;
		}

		public void setR35_c6(BigDecimal r35_c6) {
			this.r35_c6 = r35_c6;
		}

		public BigDecimal getR35_c7() {
			return r35_c7;
		}

		public void setR35_c7(BigDecimal r35_c7) {
			this.r35_c7 = r35_c7;
		}

		public BigDecimal getR35_c8() {
			return r35_c8;
		}

		public void setR35_c8(BigDecimal r35_c8) {
			this.r35_c8 = r35_c8;
		}

		public BigDecimal getR35_total() {
			return r35_total;
		}

		public void setR35_total(BigDecimal r35_total) {
			this.r35_total = r35_total;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_usd() {
			return r36_usd;
		}

		public void setR36_usd(BigDecimal r36_usd) {
			this.r36_usd = r36_usd;
		}

		public BigDecimal getR36_zar() {
			return r36_zar;
		}

		public void setR36_zar(BigDecimal r36_zar) {
			this.r36_zar = r36_zar;
		}

		public BigDecimal getR36_gbp() {
			return r36_gbp;
		}

		public void setR36_gbp(BigDecimal r36_gbp) {
			this.r36_gbp = r36_gbp;
		}

		public BigDecimal getR36_euro() {
			return r36_euro;
		}

		public void setR36_euro(BigDecimal r36_euro) {
			this.r36_euro = r36_euro;
		}

		public BigDecimal getR36_yen() {
			return r36_yen;
		}

		public void setR36_yen(BigDecimal r36_yen) {
			this.r36_yen = r36_yen;
		}

		public BigDecimal getR36_c6() {
			return r36_c6;
		}

		public void setR36_c6(BigDecimal r36_c6) {
			this.r36_c6 = r36_c6;
		}

		public BigDecimal getR36_c7() {
			return r36_c7;
		}

		public void setR36_c7(BigDecimal r36_c7) {
			this.r36_c7 = r36_c7;
		}

		public BigDecimal getR36_c8() {
			return r36_c8;
		}

		public void setR36_c8(BigDecimal r36_c8) {
			this.r36_c8 = r36_c8;
		}

		public BigDecimal getR36_total() {
			return r36_total;
		}

		public void setR36_total(BigDecimal r36_total) {
			this.r36_total = r36_total;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_usd() {
			return r37_usd;
		}

		public void setR37_usd(BigDecimal r37_usd) {
			this.r37_usd = r37_usd;
		}

		public BigDecimal getR37_zar() {
			return r37_zar;
		}

		public void setR37_zar(BigDecimal r37_zar) {
			this.r37_zar = r37_zar;
		}

		public BigDecimal getR37_gbp() {
			return r37_gbp;
		}

		public void setR37_gbp(BigDecimal r37_gbp) {
			this.r37_gbp = r37_gbp;
		}

		public BigDecimal getR37_euro() {
			return r37_euro;
		}

		public void setR37_euro(BigDecimal r37_euro) {
			this.r37_euro = r37_euro;
		}

		public BigDecimal getR37_yen() {
			return r37_yen;
		}

		public void setR37_yen(BigDecimal r37_yen) {
			this.r37_yen = r37_yen;
		}

		public BigDecimal getR37_c6() {
			return r37_c6;
		}

		public void setR37_c6(BigDecimal r37_c6) {
			this.r37_c6 = r37_c6;
		}

		public BigDecimal getR37_c7() {
			return r37_c7;
		}

		public void setR37_c7(BigDecimal r37_c7) {
			this.r37_c7 = r37_c7;
		}

		public BigDecimal getR37_c8() {
			return r37_c8;
		}

		public void setR37_c8(BigDecimal r37_c8) {
			this.r37_c8 = r37_c8;
		}

		public BigDecimal getR37_total() {
			return r37_total;
		}

		public void setR37_total(BigDecimal r37_total) {
			this.r37_total = r37_total;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_usd() {
			return r38_usd;
		}

		public void setR38_usd(BigDecimal r38_usd) {
			this.r38_usd = r38_usd;
		}

		public BigDecimal getR38_zar() {
			return r38_zar;
		}

		public void setR38_zar(BigDecimal r38_zar) {
			this.r38_zar = r38_zar;
		}

		public BigDecimal getR38_gbp() {
			return r38_gbp;
		}

		public void setR38_gbp(BigDecimal r38_gbp) {
			this.r38_gbp = r38_gbp;
		}

		public BigDecimal getR38_euro() {
			return r38_euro;
		}

		public void setR38_euro(BigDecimal r38_euro) {
			this.r38_euro = r38_euro;
		}

		public BigDecimal getR38_yen() {
			return r38_yen;
		}

		public void setR38_yen(BigDecimal r38_yen) {
			this.r38_yen = r38_yen;
		}

		public BigDecimal getR38_c6() {
			return r38_c6;
		}

		public void setR38_c6(BigDecimal r38_c6) {
			this.r38_c6 = r38_c6;
		}

		public BigDecimal getR38_c7() {
			return r38_c7;
		}

		public void setR38_c7(BigDecimal r38_c7) {
			this.r38_c7 = r38_c7;
		}

		public BigDecimal getR38_c8() {
			return r38_c8;
		}

		public void setR38_c8(BigDecimal r38_c8) {
			this.r38_c8 = r38_c8;
		}

		public BigDecimal getR38_total() {
			return r38_total;
		}

		public void setR38_total(BigDecimal r38_total) {
			this.r38_total = r38_total;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_usd() {
			return r39_usd;
		}

		public void setR39_usd(BigDecimal r39_usd) {
			this.r39_usd = r39_usd;
		}

		public BigDecimal getR39_zar() {
			return r39_zar;
		}

		public void setR39_zar(BigDecimal r39_zar) {
			this.r39_zar = r39_zar;
		}

		public BigDecimal getR39_gbp() {
			return r39_gbp;
		}

		public void setR39_gbp(BigDecimal r39_gbp) {
			this.r39_gbp = r39_gbp;
		}

		public BigDecimal getR39_euro() {
			return r39_euro;
		}

		public void setR39_euro(BigDecimal r39_euro) {
			this.r39_euro = r39_euro;
		}

		public BigDecimal getR39_yen() {
			return r39_yen;
		}

		public void setR39_yen(BigDecimal r39_yen) {
			this.r39_yen = r39_yen;
		}

		public BigDecimal getR39_c6() {
			return r39_c6;
		}

		public void setR39_c6(BigDecimal r39_c6) {
			this.r39_c6 = r39_c6;
		}

		public BigDecimal getR39_c7() {
			return r39_c7;
		}

		public void setR39_c7(BigDecimal r39_c7) {
			this.r39_c7 = r39_c7;
		}

		public BigDecimal getR39_c8() {
			return r39_c8;
		}

		public void setR39_c8(BigDecimal r39_c8) {
			this.r39_c8 = r39_c8;
		}

		public BigDecimal getR39_total() {
			return r39_total;
		}

		public void setR39_total(BigDecimal r39_total) {
			this.r39_total = r39_total;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_usd() {
			return r40_usd;
		}

		public void setR40_usd(BigDecimal r40_usd) {
			this.r40_usd = r40_usd;
		}

		public BigDecimal getR40_zar() {
			return r40_zar;
		}

		public void setR40_zar(BigDecimal r40_zar) {
			this.r40_zar = r40_zar;
		}

		public BigDecimal getR40_gbp() {
			return r40_gbp;
		}

		public void setR40_gbp(BigDecimal r40_gbp) {
			this.r40_gbp = r40_gbp;
		}

		public BigDecimal getR40_euro() {
			return r40_euro;
		}

		public void setR40_euro(BigDecimal r40_euro) {
			this.r40_euro = r40_euro;
		}

		public BigDecimal getR40_yen() {
			return r40_yen;
		}

		public void setR40_yen(BigDecimal r40_yen) {
			this.r40_yen = r40_yen;
		}

		public BigDecimal getR40_c6() {
			return r40_c6;
		}

		public void setR40_c6(BigDecimal r40_c6) {
			this.r40_c6 = r40_c6;
		}

		public BigDecimal getR40_c7() {
			return r40_c7;
		}

		public void setR40_c7(BigDecimal r40_c7) {
			this.r40_c7 = r40_c7;
		}

		public BigDecimal getR40_c8() {
			return r40_c8;
		}

		public void setR40_c8(BigDecimal r40_c8) {
			this.r40_c8 = r40_c8;
		}

		public BigDecimal getR40_total() {
			return r40_total;
		}

		public void setR40_total(BigDecimal r40_total) {
			this.r40_total = r40_total;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_usd() {
			return r41_usd;
		}

		public void setR41_usd(BigDecimal r41_usd) {
			this.r41_usd = r41_usd;
		}

		public BigDecimal getR41_zar() {
			return r41_zar;
		}

		public void setR41_zar(BigDecimal r41_zar) {
			this.r41_zar = r41_zar;
		}

		public BigDecimal getR41_gbp() {
			return r41_gbp;
		}

		public void setR41_gbp(BigDecimal r41_gbp) {
			this.r41_gbp = r41_gbp;
		}

		public BigDecimal getR41_euro() {
			return r41_euro;
		}

		public void setR41_euro(BigDecimal r41_euro) {
			this.r41_euro = r41_euro;
		}

		public BigDecimal getR41_yen() {
			return r41_yen;
		}

		public void setR41_yen(BigDecimal r41_yen) {
			this.r41_yen = r41_yen;
		}

		public BigDecimal getR41_c6() {
			return r41_c6;
		}

		public void setR41_c6(BigDecimal r41_c6) {
			this.r41_c6 = r41_c6;
		}

		public BigDecimal getR41_c7() {
			return r41_c7;
		}

		public void setR41_c7(BigDecimal r41_c7) {
			this.r41_c7 = r41_c7;
		}

		public BigDecimal getR41_c8() {
			return r41_c8;
		}

		public void setR41_c8(BigDecimal r41_c8) {
			this.r41_c8 = r41_c8;
		}

		public BigDecimal getR41_total() {
			return r41_total;
		}

		public void setR41_total(BigDecimal r41_total) {
			this.r41_total = r41_total;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_usd() {
			return r42_usd;
		}

		public void setR42_usd(BigDecimal r42_usd) {
			this.r42_usd = r42_usd;
		}

		public BigDecimal getR42_zar() {
			return r42_zar;
		}

		public void setR42_zar(BigDecimal r42_zar) {
			this.r42_zar = r42_zar;
		}

		public BigDecimal getR42_gbp() {
			return r42_gbp;
		}

		public void setR42_gbp(BigDecimal r42_gbp) {
			this.r42_gbp = r42_gbp;
		}

		public BigDecimal getR42_euro() {
			return r42_euro;
		}

		public void setR42_euro(BigDecimal r42_euro) {
			this.r42_euro = r42_euro;
		}

		public BigDecimal getR42_yen() {
			return r42_yen;
		}

		public void setR42_yen(BigDecimal r42_yen) {
			this.r42_yen = r42_yen;
		}

		public BigDecimal getR42_c6() {
			return r42_c6;
		}

		public void setR42_c6(BigDecimal r42_c6) {
			this.r42_c6 = r42_c6;
		}

		public BigDecimal getR42_c7() {
			return r42_c7;
		}

		public void setR42_c7(BigDecimal r42_c7) {
			this.r42_c7 = r42_c7;
		}

		public BigDecimal getR42_c8() {
			return r42_c8;
		}

		public void setR42_c8(BigDecimal r42_c8) {
			this.r42_c8 = r42_c8;
		}

		public BigDecimal getR42_total() {
			return r42_total;
		}

		public void setR42_total(BigDecimal r42_total) {
			this.r42_total = r42_total;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_usd() {
			return r43_usd;
		}

		public void setR43_usd(BigDecimal r43_usd) {
			this.r43_usd = r43_usd;
		}

		public BigDecimal getR43_zar() {
			return r43_zar;
		}

		public void setR43_zar(BigDecimal r43_zar) {
			this.r43_zar = r43_zar;
		}

		public BigDecimal getR43_gbp() {
			return r43_gbp;
		}

		public void setR43_gbp(BigDecimal r43_gbp) {
			this.r43_gbp = r43_gbp;
		}

		public BigDecimal getR43_euro() {
			return r43_euro;
		}

		public void setR43_euro(BigDecimal r43_euro) {
			this.r43_euro = r43_euro;
		}

		public BigDecimal getR43_yen() {
			return r43_yen;
		}

		public void setR43_yen(BigDecimal r43_yen) {
			this.r43_yen = r43_yen;
		}

		public BigDecimal getR43_c6() {
			return r43_c6;
		}

		public void setR43_c6(BigDecimal r43_c6) {
			this.r43_c6 = r43_c6;
		}

		public BigDecimal getR43_c7() {
			return r43_c7;
		}

		public void setR43_c7(BigDecimal r43_c7) {
			this.r43_c7 = r43_c7;
		}

		public BigDecimal getR43_c8() {
			return r43_c8;
		}

		public void setR43_c8(BigDecimal r43_c8) {
			this.r43_c8 = r43_c8;
		}

		public BigDecimal getR43_total() {
			return r43_total;
		}

		public void setR43_total(BigDecimal r43_total) {
			this.r43_total = r43_total;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_usd() {
			return r44_usd;
		}

		public void setR44_usd(BigDecimal r44_usd) {
			this.r44_usd = r44_usd;
		}

		public BigDecimal getR44_zar() {
			return r44_zar;
		}

		public void setR44_zar(BigDecimal r44_zar) {
			this.r44_zar = r44_zar;
		}

		public BigDecimal getR44_gbp() {
			return r44_gbp;
		}

		public void setR44_gbp(BigDecimal r44_gbp) {
			this.r44_gbp = r44_gbp;
		}

		public BigDecimal getR44_euro() {
			return r44_euro;
		}

		public void setR44_euro(BigDecimal r44_euro) {
			this.r44_euro = r44_euro;
		}

		public BigDecimal getR44_yen() {
			return r44_yen;
		}

		public void setR44_yen(BigDecimal r44_yen) {
			this.r44_yen = r44_yen;
		}

		public BigDecimal getR44_c6() {
			return r44_c6;
		}

		public void setR44_c6(BigDecimal r44_c6) {
			this.r44_c6 = r44_c6;
		}

		public BigDecimal getR44_c7() {
			return r44_c7;
		}

		public void setR44_c7(BigDecimal r44_c7) {
			this.r44_c7 = r44_c7;
		}

		public BigDecimal getR44_c8() {
			return r44_c8;
		}

		public void setR44_c8(BigDecimal r44_c8) {
			this.r44_c8 = r44_c8;
		}

		public BigDecimal getR44_total() {
			return r44_total;
		}

		public void setR44_total(BigDecimal r44_total) {
			this.r44_total = r44_total;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_usd() {
			return r45_usd;
		}

		public void setR45_usd(BigDecimal r45_usd) {
			this.r45_usd = r45_usd;
		}

		public BigDecimal getR45_zar() {
			return r45_zar;
		}

		public void setR45_zar(BigDecimal r45_zar) {
			this.r45_zar = r45_zar;
		}

		public BigDecimal getR45_gbp() {
			return r45_gbp;
		}

		public void setR45_gbp(BigDecimal r45_gbp) {
			this.r45_gbp = r45_gbp;
		}

		public BigDecimal getR45_euro() {
			return r45_euro;
		}

		public void setR45_euro(BigDecimal r45_euro) {
			this.r45_euro = r45_euro;
		}

		public BigDecimal getR45_yen() {
			return r45_yen;
		}

		public void setR45_yen(BigDecimal r45_yen) {
			this.r45_yen = r45_yen;
		}

		public BigDecimal getR45_c6() {
			return r45_c6;
		}

		public void setR45_c6(BigDecimal r45_c6) {
			this.r45_c6 = r45_c6;
		}

		public BigDecimal getR45_c7() {
			return r45_c7;
		}

		public void setR45_c7(BigDecimal r45_c7) {
			this.r45_c7 = r45_c7;
		}

		public BigDecimal getR45_c8() {
			return r45_c8;
		}

		public void setR45_c8(BigDecimal r45_c8) {
			this.r45_c8 = r45_c8;
		}

		public BigDecimal getR45_total() {
			return r45_total;
		}

		public void setR45_total(BigDecimal r45_total) {
			this.r45_total = r45_total;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_usd() {
			return r46_usd;
		}

		public void setR46_usd(BigDecimal r46_usd) {
			this.r46_usd = r46_usd;
		}

		public BigDecimal getR46_zar() {
			return r46_zar;
		}

		public void setR46_zar(BigDecimal r46_zar) {
			this.r46_zar = r46_zar;
		}

		public BigDecimal getR46_gbp() {
			return r46_gbp;
		}

		public void setR46_gbp(BigDecimal r46_gbp) {
			this.r46_gbp = r46_gbp;
		}

		public BigDecimal getR46_euro() {
			return r46_euro;
		}

		public void setR46_euro(BigDecimal r46_euro) {
			this.r46_euro = r46_euro;
		}

		public BigDecimal getR46_yen() {
			return r46_yen;
		}

		public void setR46_yen(BigDecimal r46_yen) {
			this.r46_yen = r46_yen;
		}

		public BigDecimal getR46_c6() {
			return r46_c6;
		}

		public void setR46_c6(BigDecimal r46_c6) {
			this.r46_c6 = r46_c6;
		}

		public BigDecimal getR46_c7() {
			return r46_c7;
		}

		public void setR46_c7(BigDecimal r46_c7) {
			this.r46_c7 = r46_c7;
		}

		public BigDecimal getR46_c8() {
			return r46_c8;
		}

		public void setR46_c8(BigDecimal r46_c8) {
			this.r46_c8 = r46_c8;
		}

		public BigDecimal getR46_total() {
			return r46_total;
		}

		public void setR46_total(BigDecimal r46_total) {
			this.r46_total = r46_total;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_usd() {
			return r47_usd;
		}

		public void setR47_usd(BigDecimal r47_usd) {
			this.r47_usd = r47_usd;
		}

		public BigDecimal getR47_zar() {
			return r47_zar;
		}

		public void setR47_zar(BigDecimal r47_zar) {
			this.r47_zar = r47_zar;
		}

		public BigDecimal getR47_gbp() {
			return r47_gbp;
		}

		public void setR47_gbp(BigDecimal r47_gbp) {
			this.r47_gbp = r47_gbp;
		}

		public BigDecimal getR47_euro() {
			return r47_euro;
		}

		public void setR47_euro(BigDecimal r47_euro) {
			this.r47_euro = r47_euro;
		}

		public BigDecimal getR47_yen() {
			return r47_yen;
		}

		public void setR47_yen(BigDecimal r47_yen) {
			this.r47_yen = r47_yen;
		}

		public BigDecimal getR47_c6() {
			return r47_c6;
		}

		public void setR47_c6(BigDecimal r47_c6) {
			this.r47_c6 = r47_c6;
		}

		public BigDecimal getR47_c7() {
			return r47_c7;
		}

		public void setR47_c7(BigDecimal r47_c7) {
			this.r47_c7 = r47_c7;
		}

		public BigDecimal getR47_c8() {
			return r47_c8;
		}

		public void setR47_c8(BigDecimal r47_c8) {
			this.r47_c8 = r47_c8;
		}

		public BigDecimal getR47_total() {
			return r47_total;
		}

		public void setR47_total(BigDecimal r47_total) {
			this.r47_total = r47_total;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_usd() {
			return r48_usd;
		}

		public void setR48_usd(BigDecimal r48_usd) {
			this.r48_usd = r48_usd;
		}

		public BigDecimal getR48_zar() {
			return r48_zar;
		}

		public void setR48_zar(BigDecimal r48_zar) {
			this.r48_zar = r48_zar;
		}

		public BigDecimal getR48_gbp() {
			return r48_gbp;
		}

		public void setR48_gbp(BigDecimal r48_gbp) {
			this.r48_gbp = r48_gbp;
		}

		public BigDecimal getR48_euro() {
			return r48_euro;
		}

		public void setR48_euro(BigDecimal r48_euro) {
			this.r48_euro = r48_euro;
		}

		public BigDecimal getR48_yen() {
			return r48_yen;
		}

		public void setR48_yen(BigDecimal r48_yen) {
			this.r48_yen = r48_yen;
		}

		public BigDecimal getR48_c6() {
			return r48_c6;
		}

		public void setR48_c6(BigDecimal r48_c6) {
			this.r48_c6 = r48_c6;
		}

		public BigDecimal getR48_c7() {
			return r48_c7;
		}

		public void setR48_c7(BigDecimal r48_c7) {
			this.r48_c7 = r48_c7;
		}

		public BigDecimal getR48_c8() {
			return r48_c8;
		}

		public void setR48_c8(BigDecimal r48_c8) {
			this.r48_c8 = r48_c8;
		}

		public BigDecimal getR48_total() {
			return r48_total;
		}

		public void setR48_total(BigDecimal r48_total) {
			this.r48_total = r48_total;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_usd() {
			return r49_usd;
		}

		public void setR49_usd(BigDecimal r49_usd) {
			this.r49_usd = r49_usd;
		}

		public BigDecimal getR49_zar() {
			return r49_zar;
		}

		public void setR49_zar(BigDecimal r49_zar) {
			this.r49_zar = r49_zar;
		}

		public BigDecimal getR49_gbp() {
			return r49_gbp;
		}

		public void setR49_gbp(BigDecimal r49_gbp) {
			this.r49_gbp = r49_gbp;
		}

		public BigDecimal getR49_euro() {
			return r49_euro;
		}

		public void setR49_euro(BigDecimal r49_euro) {
			this.r49_euro = r49_euro;
		}

		public BigDecimal getR49_yen() {
			return r49_yen;
		}

		public void setR49_yen(BigDecimal r49_yen) {
			this.r49_yen = r49_yen;
		}

		public BigDecimal getR49_c6() {
			return r49_c6;
		}

		public void setR49_c6(BigDecimal r49_c6) {
			this.r49_c6 = r49_c6;
		}

		public BigDecimal getR49_c7() {
			return r49_c7;
		}

		public void setR49_c7(BigDecimal r49_c7) {
			this.r49_c7 = r49_c7;
		}

		public BigDecimal getR49_c8() {
			return r49_c8;
		}

		public void setR49_c8(BigDecimal r49_c8) {
			this.r49_c8 = r49_c8;
		}

		public BigDecimal getR49_total() {
			return r49_total;
		}

		public void setR49_total(BigDecimal r49_total) {
			this.r49_total = r49_total;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_usd() {
			return r50_usd;
		}

		public void setR50_usd(BigDecimal r50_usd) {
			this.r50_usd = r50_usd;
		}

		public BigDecimal getR50_zar() {
			return r50_zar;
		}

		public void setR50_zar(BigDecimal r50_zar) {
			this.r50_zar = r50_zar;
		}

		public BigDecimal getR50_gbp() {
			return r50_gbp;
		}

		public void setR50_gbp(BigDecimal r50_gbp) {
			this.r50_gbp = r50_gbp;
		}

		public BigDecimal getR50_euro() {
			return r50_euro;
		}

		public void setR50_euro(BigDecimal r50_euro) {
			this.r50_euro = r50_euro;
		}

		public BigDecimal getR50_yen() {
			return r50_yen;
		}

		public void setR50_yen(BigDecimal r50_yen) {
			this.r50_yen = r50_yen;
		}

		public BigDecimal getR50_c6() {
			return r50_c6;
		}

		public void setR50_c6(BigDecimal r50_c6) {
			this.r50_c6 = r50_c6;
		}

		public BigDecimal getR50_c7() {
			return r50_c7;
		}

		public void setR50_c7(BigDecimal r50_c7) {
			this.r50_c7 = r50_c7;
		}

		public BigDecimal getR50_c8() {
			return r50_c8;
		}

		public void setR50_c8(BigDecimal r50_c8) {
			this.r50_c8 = r50_c8;
		}

		public BigDecimal getR50_total() {
			return r50_total;
		}

		public void setR50_total(BigDecimal r50_total) {
			this.r50_total = r50_total;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public BigDecimal getR51_usd() {
			return r51_usd;
		}

		public void setR51_usd(BigDecimal r51_usd) {
			this.r51_usd = r51_usd;
		}

		public BigDecimal getR51_zar() {
			return r51_zar;
		}

		public void setR51_zar(BigDecimal r51_zar) {
			this.r51_zar = r51_zar;
		}

		public BigDecimal getR51_gbp() {
			return r51_gbp;
		}

		public void setR51_gbp(BigDecimal r51_gbp) {
			this.r51_gbp = r51_gbp;
		}

		public BigDecimal getR51_euro() {
			return r51_euro;
		}

		public void setR51_euro(BigDecimal r51_euro) {
			this.r51_euro = r51_euro;
		}

		public BigDecimal getR51_yen() {
			return r51_yen;
		}

		public void setR51_yen(BigDecimal r51_yen) {
			this.r51_yen = r51_yen;
		}

		public BigDecimal getR51_c6() {
			return r51_c6;
		}

		public void setR51_c6(BigDecimal r51_c6) {
			this.r51_c6 = r51_c6;
		}

		public BigDecimal getR51_c7() {
			return r51_c7;
		}

		public void setR51_c7(BigDecimal r51_c7) {
			this.r51_c7 = r51_c7;
		}

		public BigDecimal getR51_c8() {
			return r51_c8;
		}

		public void setR51_c8(BigDecimal r51_c8) {
			this.r51_c8 = r51_c8;
		}

		public BigDecimal getR51_total() {
			return r51_total;
		}

		public void setR51_total(BigDecimal r51_total) {
			this.r51_total = r51_total;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public BigDecimal getR52_usd() {
			return r52_usd;
		}

		public void setR52_usd(BigDecimal r52_usd) {
			this.r52_usd = r52_usd;
		}

		public BigDecimal getR52_zar() {
			return r52_zar;
		}

		public void setR52_zar(BigDecimal r52_zar) {
			this.r52_zar = r52_zar;
		}

		public BigDecimal getR52_gbp() {
			return r52_gbp;
		}

		public void setR52_gbp(BigDecimal r52_gbp) {
			this.r52_gbp = r52_gbp;
		}

		public BigDecimal getR52_euro() {
			return r52_euro;
		}

		public void setR52_euro(BigDecimal r52_euro) {
			this.r52_euro = r52_euro;
		}

		public BigDecimal getR52_yen() {
			return r52_yen;
		}

		public void setR52_yen(BigDecimal r52_yen) {
			this.r52_yen = r52_yen;
		}

		public BigDecimal getR52_c6() {
			return r52_c6;
		}

		public void setR52_c6(BigDecimal r52_c6) {
			this.r52_c6 = r52_c6;
		}

		public BigDecimal getR52_c7() {
			return r52_c7;
		}

		public void setR52_c7(BigDecimal r52_c7) {
			this.r52_c7 = r52_c7;
		}

		public BigDecimal getR52_c8() {
			return r52_c8;
		}

		public void setR52_c8(BigDecimal r52_c8) {
			this.r52_c8 = r52_c8;
		}

		public BigDecimal getR52_total() {
			return r52_total;
		}

		public void setR52_total(BigDecimal r52_total) {
			this.r52_total = r52_total;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public BigDecimal getR53_usd() {
			return r53_usd;
		}

		public void setR53_usd(BigDecimal r53_usd) {
			this.r53_usd = r53_usd;
		}

		public BigDecimal getR53_zar() {
			return r53_zar;
		}

		public void setR53_zar(BigDecimal r53_zar) {
			this.r53_zar = r53_zar;
		}

		public BigDecimal getR53_gbp() {
			return r53_gbp;
		}

		public void setR53_gbp(BigDecimal r53_gbp) {
			this.r53_gbp = r53_gbp;
		}

		public BigDecimal getR53_euro() {
			return r53_euro;
		}

		public void setR53_euro(BigDecimal r53_euro) {
			this.r53_euro = r53_euro;
		}

		public BigDecimal getR53_yen() {
			return r53_yen;
		}

		public void setR53_yen(BigDecimal r53_yen) {
			this.r53_yen = r53_yen;
		}

		public BigDecimal getR53_c6() {
			return r53_c6;
		}

		public void setR53_c6(BigDecimal r53_c6) {
			this.r53_c6 = r53_c6;
		}

		public BigDecimal getR53_c7() {
			return r53_c7;
		}

		public void setR53_c7(BigDecimal r53_c7) {
			this.r53_c7 = r53_c7;
		}

		public BigDecimal getR53_c8() {
			return r53_c8;
		}

		public void setR53_c8(BigDecimal r53_c8) {
			this.r53_c8 = r53_c8;
		}

		public BigDecimal getR53_total() {
			return r53_total;
		}

		public void setR53_total(BigDecimal r53_total) {
			this.r53_total = r53_total;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public BigDecimal getR54_usd() {
			return r54_usd;
		}

		public void setR54_usd(BigDecimal r54_usd) {
			this.r54_usd = r54_usd;
		}

		public BigDecimal getR54_zar() {
			return r54_zar;
		}

		public void setR54_zar(BigDecimal r54_zar) {
			this.r54_zar = r54_zar;
		}

		public BigDecimal getR54_gbp() {
			return r54_gbp;
		}

		public void setR54_gbp(BigDecimal r54_gbp) {
			this.r54_gbp = r54_gbp;
		}

		public BigDecimal getR54_euro() {
			return r54_euro;
		}

		public void setR54_euro(BigDecimal r54_euro) {
			this.r54_euro = r54_euro;
		}

		public BigDecimal getR54_yen() {
			return r54_yen;
		}

		public void setR54_yen(BigDecimal r54_yen) {
			this.r54_yen = r54_yen;
		}

		public BigDecimal getR54_c6() {
			return r54_c6;
		}

		public void setR54_c6(BigDecimal r54_c6) {
			this.r54_c6 = r54_c6;
		}

		public BigDecimal getR54_c7() {
			return r54_c7;
		}

		public void setR54_c7(BigDecimal r54_c7) {
			this.r54_c7 = r54_c7;
		}

		public BigDecimal getR54_c8() {
			return r54_c8;
		}

		public void setR54_c8(BigDecimal r54_c8) {
			this.r54_c8 = r54_c8;
		}

		public BigDecimal getR54_total() {
			return r54_total;
		}

		public void setR54_total(BigDecimal r54_total) {
			this.r54_total = r54_total;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public BigDecimal getR55_usd() {
			return r55_usd;
		}

		public void setR55_usd(BigDecimal r55_usd) {
			this.r55_usd = r55_usd;
		}

		public BigDecimal getR55_zar() {
			return r55_zar;
		}

		public void setR55_zar(BigDecimal r55_zar) {
			this.r55_zar = r55_zar;
		}

		public BigDecimal getR55_gbp() {
			return r55_gbp;
		}

		public void setR55_gbp(BigDecimal r55_gbp) {
			this.r55_gbp = r55_gbp;
		}

		public BigDecimal getR55_euro() {
			return r55_euro;
		}

		public void setR55_euro(BigDecimal r55_euro) {
			this.r55_euro = r55_euro;
		}

		public BigDecimal getR55_yen() {
			return r55_yen;
		}

		public void setR55_yen(BigDecimal r55_yen) {
			this.r55_yen = r55_yen;
		}

		public BigDecimal getR55_c6() {
			return r55_c6;
		}

		public void setR55_c6(BigDecimal r55_c6) {
			this.r55_c6 = r55_c6;
		}

		public BigDecimal getR55_c7() {
			return r55_c7;
		}

		public void setR55_c7(BigDecimal r55_c7) {
			this.r55_c7 = r55_c7;
		}

		public BigDecimal getR55_c8() {
			return r55_c8;
		}

		public void setR55_c8(BigDecimal r55_c8) {
			this.r55_c8 = r55_c8;
		}

		public BigDecimal getR55_total() {
			return r55_total;
		}

		public void setR55_total(BigDecimal r55_total) {
			this.r55_total = r55_total;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public BigDecimal getR56_usd() {
			return r56_usd;
		}

		public void setR56_usd(BigDecimal r56_usd) {
			this.r56_usd = r56_usd;
		}

		public BigDecimal getR56_zar() {
			return r56_zar;
		}

		public void setR56_zar(BigDecimal r56_zar) {
			this.r56_zar = r56_zar;
		}

		public BigDecimal getR56_gbp() {
			return r56_gbp;
		}

		public void setR56_gbp(BigDecimal r56_gbp) {
			this.r56_gbp = r56_gbp;
		}

		public BigDecimal getR56_euro() {
			return r56_euro;
		}

		public void setR56_euro(BigDecimal r56_euro) {
			this.r56_euro = r56_euro;
		}

		public BigDecimal getR56_yen() {
			return r56_yen;
		}

		public void setR56_yen(BigDecimal r56_yen) {
			this.r56_yen = r56_yen;
		}

		public BigDecimal getR56_c6() {
			return r56_c6;
		}

		public void setR56_c6(BigDecimal r56_c6) {
			this.r56_c6 = r56_c6;
		}

		public BigDecimal getR56_c7() {
			return r56_c7;
		}

		public void setR56_c7(BigDecimal r56_c7) {
			this.r56_c7 = r56_c7;
		}

		public BigDecimal getR56_c8() {
			return r56_c8;
		}

		public void setR56_c8(BigDecimal r56_c8) {
			this.r56_c8 = r56_c8;
		}

		public BigDecimal getR56_total() {
			return r56_total;
		}

		public void setR56_total(BigDecimal r56_total) {
			this.r56_total = r56_total;
		}

		public String getR57_product() {
			return r57_product;
		}

		public void setR57_product(String r57_product) {
			this.r57_product = r57_product;
		}

		public BigDecimal getR57_usd() {
			return r57_usd;
		}

		public void setR57_usd(BigDecimal r57_usd) {
			this.r57_usd = r57_usd;
		}

		public BigDecimal getR57_zar() {
			return r57_zar;
		}

		public void setR57_zar(BigDecimal r57_zar) {
			this.r57_zar = r57_zar;
		}

		public BigDecimal getR57_gbp() {
			return r57_gbp;
		}

		public void setR57_gbp(BigDecimal r57_gbp) {
			this.r57_gbp = r57_gbp;
		}

		public BigDecimal getR57_euro() {
			return r57_euro;
		}

		public void setR57_euro(BigDecimal r57_euro) {
			this.r57_euro = r57_euro;
		}

		public BigDecimal getR57_yen() {
			return r57_yen;
		}

		public void setR57_yen(BigDecimal r57_yen) {
			this.r57_yen = r57_yen;
		}

		public BigDecimal getR57_c6() {
			return r57_c6;
		}

		public void setR57_c6(BigDecimal r57_c6) {
			this.r57_c6 = r57_c6;
		}

		public BigDecimal getR57_c7() {
			return r57_c7;
		}

		public void setR57_c7(BigDecimal r57_c7) {
			this.r57_c7 = r57_c7;
		}

		public BigDecimal getR57_c8() {
			return r57_c8;
		}

		public void setR57_c8(BigDecimal r57_c8) {
			this.r57_c8 = r57_c8;
		}

		public BigDecimal getR57_total() {
			return r57_total;
		}

		public void setR57_total(BigDecimal r57_total) {
			this.r57_total = r57_total;
		}

		public String getR58_product() {
			return r58_product;
		}

		public void setR58_product(String r58_product) {
			this.r58_product = r58_product;
		}

		public BigDecimal getR58_usd() {
			return r58_usd;
		}

		public void setR58_usd(BigDecimal r58_usd) {
			this.r58_usd = r58_usd;
		}

		public BigDecimal getR58_zar() {
			return r58_zar;
		}

		public void setR58_zar(BigDecimal r58_zar) {
			this.r58_zar = r58_zar;
		}

		public BigDecimal getR58_gbp() {
			return r58_gbp;
		}

		public void setR58_gbp(BigDecimal r58_gbp) {
			this.r58_gbp = r58_gbp;
		}

		public BigDecimal getR58_euro() {
			return r58_euro;
		}

		public void setR58_euro(BigDecimal r58_euro) {
			this.r58_euro = r58_euro;
		}

		public BigDecimal getR58_yen() {
			return r58_yen;
		}

		public void setR58_yen(BigDecimal r58_yen) {
			this.r58_yen = r58_yen;
		}

		public BigDecimal getR58_c6() {
			return r58_c6;
		}

		public void setR58_c6(BigDecimal r58_c6) {
			this.r58_c6 = r58_c6;
		}

		public BigDecimal getR58_c7() {
			return r58_c7;
		}

		public void setR58_c7(BigDecimal r58_c7) {
			this.r58_c7 = r58_c7;
		}

		public BigDecimal getR58_c8() {
			return r58_c8;
		}

		public void setR58_c8(BigDecimal r58_c8) {
			this.r58_c8 = r58_c8;
		}

		public BigDecimal getR58_total() {
			return r58_total;
		}

		public void setR58_total(BigDecimal r58_total) {
			this.r58_total = r58_total;
		}

		public String getR59_product() {
			return r59_product;
		}

		public void setR59_product(String r59_product) {
			this.r59_product = r59_product;
		}

		public BigDecimal getR59_usd() {
			return r59_usd;
		}

		public void setR59_usd(BigDecimal r59_usd) {
			this.r59_usd = r59_usd;
		}

		public BigDecimal getR59_zar() {
			return r59_zar;
		}

		public void setR59_zar(BigDecimal r59_zar) {
			this.r59_zar = r59_zar;
		}

		public BigDecimal getR59_gbp() {
			return r59_gbp;
		}

		public void setR59_gbp(BigDecimal r59_gbp) {
			this.r59_gbp = r59_gbp;
		}

		public BigDecimal getR59_euro() {
			return r59_euro;
		}

		public void setR59_euro(BigDecimal r59_euro) {
			this.r59_euro = r59_euro;
		}

		public BigDecimal getR59_yen() {
			return r59_yen;
		}

		public void setR59_yen(BigDecimal r59_yen) {
			this.r59_yen = r59_yen;
		}

		public BigDecimal getR59_c6() {
			return r59_c6;
		}

		public void setR59_c6(BigDecimal r59_c6) {
			this.r59_c6 = r59_c6;
		}

		public BigDecimal getR59_c7() {
			return r59_c7;
		}

		public void setR59_c7(BigDecimal r59_c7) {
			this.r59_c7 = r59_c7;
		}

		public BigDecimal getR59_c8() {
			return r59_c8;
		}

		public void setR59_c8(BigDecimal r59_c8) {
			this.r59_c8 = r59_c8;
		}

		public BigDecimal getR59_total() {
			return r59_total;
		}

		public void setR59_total(BigDecimal r59_total) {
			this.r59_total = r59_total;
		}

		public String getR60_product() {
			return r60_product;
		}

		public void setR60_product(String r60_product) {
			this.r60_product = r60_product;
		}

		public BigDecimal getR60_usd() {
			return r60_usd;
		}

		public void setR60_usd(BigDecimal r60_usd) {
			this.r60_usd = r60_usd;
		}

		public BigDecimal getR60_zar() {
			return r60_zar;
		}

		public void setR60_zar(BigDecimal r60_zar) {
			this.r60_zar = r60_zar;
		}

		public BigDecimal getR60_gbp() {
			return r60_gbp;
		}

		public void setR60_gbp(BigDecimal r60_gbp) {
			this.r60_gbp = r60_gbp;
		}

		public BigDecimal getR60_euro() {
			return r60_euro;
		}

		public void setR60_euro(BigDecimal r60_euro) {
			this.r60_euro = r60_euro;
		}

		public BigDecimal getR60_yen() {
			return r60_yen;
		}

		public void setR60_yen(BigDecimal r60_yen) {
			this.r60_yen = r60_yen;
		}

		public BigDecimal getR60_c6() {
			return r60_c6;
		}

		public void setR60_c6(BigDecimal r60_c6) {
			this.r60_c6 = r60_c6;
		}

		public BigDecimal getR60_c7() {
			return r60_c7;
		}

		public void setR60_c7(BigDecimal r60_c7) {
			this.r60_c7 = r60_c7;
		}

		public BigDecimal getR60_c8() {
			return r60_c8;
		}

		public void setR60_c8(BigDecimal r60_c8) {
			this.r60_c8 = r60_c8;
		}

		public BigDecimal getR60_total() {
			return r60_total;
		}

		public void setR60_total(BigDecimal r60_total) {
			this.r60_total = r60_total;
		}

		public String getR61_product() {
			return r61_product;
		}

		public void setR61_product(String r61_product) {
			this.r61_product = r61_product;
		}

		public BigDecimal getR61_usd() {
			return r61_usd;
		}

		public void setR61_usd(BigDecimal r61_usd) {
			this.r61_usd = r61_usd;
		}

		public BigDecimal getR61_zar() {
			return r61_zar;
		}

		public void setR61_zar(BigDecimal r61_zar) {
			this.r61_zar = r61_zar;
		}

		public BigDecimal getR61_gbp() {
			return r61_gbp;
		}

		public void setR61_gbp(BigDecimal r61_gbp) {
			this.r61_gbp = r61_gbp;
		}

		public BigDecimal getR61_euro() {
			return r61_euro;
		}

		public void setR61_euro(BigDecimal r61_euro) {
			this.r61_euro = r61_euro;
		}

		public BigDecimal getR61_yen() {
			return r61_yen;
		}

		public void setR61_yen(BigDecimal r61_yen) {
			this.r61_yen = r61_yen;
		}

		public BigDecimal getR61_c6() {
			return r61_c6;
		}

		public void setR61_c6(BigDecimal r61_c6) {
			this.r61_c6 = r61_c6;
		}

		public BigDecimal getR61_c7() {
			return r61_c7;
		}

		public void setR61_c7(BigDecimal r61_c7) {
			this.r61_c7 = r61_c7;
		}

		public BigDecimal getR61_c8() {
			return r61_c8;
		}

		public void setR61_c8(BigDecimal r61_c8) {
			this.r61_c8 = r61_c8;
		}

		public BigDecimal getR61_total() {
			return r61_total;
		}

		public void setR61_total(BigDecimal r61_total) {
			this.r61_total = r61_total;
		}

		public String getR62_product() {
			return r62_product;
		}

		public void setR62_product(String r62_product) {
			this.r62_product = r62_product;
		}

		public BigDecimal getR62_usd() {
			return r62_usd;
		}

		public void setR62_usd(BigDecimal r62_usd) {
			this.r62_usd = r62_usd;
		}

		public BigDecimal getR62_zar() {
			return r62_zar;
		}

		public void setR62_zar(BigDecimal r62_zar) {
			this.r62_zar = r62_zar;
		}

		public BigDecimal getR62_gbp() {
			return r62_gbp;
		}

		public void setR62_gbp(BigDecimal r62_gbp) {
			this.r62_gbp = r62_gbp;
		}

		public BigDecimal getR62_euro() {
			return r62_euro;
		}

		public void setR62_euro(BigDecimal r62_euro) {
			this.r62_euro = r62_euro;
		}

		public BigDecimal getR62_yen() {
			return r62_yen;
		}

		public void setR62_yen(BigDecimal r62_yen) {
			this.r62_yen = r62_yen;
		}

		public BigDecimal getR62_c6() {
			return r62_c6;
		}

		public void setR62_c6(BigDecimal r62_c6) {
			this.r62_c6 = r62_c6;
		}

		public BigDecimal getR62_c7() {
			return r62_c7;
		}

		public void setR62_c7(BigDecimal r62_c7) {
			this.r62_c7 = r62_c7;
		}

		public BigDecimal getR62_c8() {
			return r62_c8;
		}

		public void setR62_c8(BigDecimal r62_c8) {
			this.r62_c8 = r62_c8;
		}

		public BigDecimal getR62_total() {
			return r62_total;
		}

		public void setR62_total(BigDecimal r62_total) {
			this.r62_total = r62_total;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
			REPORT_VERSION = rEPORT_VERSION;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
		}
	}

// COMPOSITE KEY CLASS INSIDE SERVICE

	public static class M_LA5_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public M_LA5_PK() {
		}

		public M_LA5_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof M_LA5_PK))
				return false;
			M_LA5_PK that = (M_LA5_PK) o;
			return Objects.equals(REPORT_DATE, that.REPORT_DATE) && Objects.equals(REPORT_VERSION, that.REPORT_VERSION);
		}

		@Override
		public int hashCode() {
			return Objects.hash(REPORT_DATE, REPORT_VERSION);
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}
	}

	public class M_LA5_Detail_Entity {

		private Long sno;
		private String cust_id;
		private String acct_number;
		private String acct_name;
		private String data_type;
		private String report_remarks;
		private String modification_remarks;
		private String data_entry_version;
		private BigDecimal acct_balance_in_pula;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date report_date;
		private String report_name;
		private String create_user;
		private Date create_time;
		private String modify_user;
		private Date modify_time;
		private String verify_user;
		private Date verify_time;
		private char entity_flg;
		private char modify_flg;
		private char del_flg;
		private String report_name_1;
		private String ccy;
		private String segment;
		private String report_label;
		private String report_addl_criteria_1;
		private String report_addl_criteria_2;
		private String report_addl_criteria_3;
		private BigDecimal sanction_limit;

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

		public String getReport_remarks() {
			return report_remarks;
		}

		public void setReport_remarks(String report_remarks) {
			this.report_remarks = report_remarks;
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

		public String getReport_name() {
			return report_name;
		}

		public void setReport_name(String report_name) {
			this.report_name = report_name;
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

		public char getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(char entity_flg) {
			this.entity_flg = entity_flg;
		}

		public char getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(char modify_flg) {
			this.modify_flg = modify_flg;
		}

		public char getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(char del_flg) {
			this.del_flg = del_flg;
		}

		public String getReport_name_1() {
			return report_name_1;
		}

		public void setReport_name_1(String report_name_1) {
			this.report_name_1 = report_name_1;
		}

		public String getCcy() {
			return ccy;
		}

		public void setCcy(String ccy) {
			this.ccy = ccy;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
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

		public BigDecimal getSanction_limit() {
			return sanction_limit;
		}

		public void setSanction_limit(BigDecimal sanction_limit) {
			this.sanction_limit = sanction_limit;
		}
	}

	class M_LA5DetailRowMapper implements RowMapper<M_LA5_Detail_Entity> {

		@Override
		public M_LA5_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA5_Detail_Entity obj = new M_LA5_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCust_id(rs.getString("cust_id"));
			obj.setAcct_number(rs.getString("acct_number"));
			obj.setAcct_name(rs.getString("acct_name"));
			obj.setData_type(rs.getString("data_type"));
			obj.setReport_remarks(rs.getString("report_remarks"));
			obj.setModification_remarks(rs.getString("modification_remarks"));
			obj.setData_entry_version(rs.getString("data_entry_version"));
			obj.setAcct_balance_in_pula(rs.getBigDecimal("acct_balance_in_pula"));
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_name(rs.getString("report_name"));
			obj.setCreate_user(rs.getString("create_user"));
			obj.setCreate_time(rs.getDate("create_time"));
			obj.setModify_user(rs.getString("modify_user"));
			obj.setModify_time(rs.getDate("modify_time"));
			obj.setVerify_user(rs.getString("verify_user"));
			obj.setVerify_time(rs.getDate("verify_time"));
			obj.setEntity_flg(rs.getString("entity_flg") != null ? rs.getString("entity_flg").charAt(0) : ' ');
			obj.setModify_flg(rs.getString("modify_flg") != null ? rs.getString("modify_flg").charAt(0) : ' ');
			obj.setDel_flg(rs.getString("del_flg") != null ? rs.getString("del_flg").charAt(0) : ' ');
			obj.setReport_name_1(rs.getString("report_name_1"));
			obj.setCcy(rs.getString("ccy"));
			obj.setSegment(rs.getString("segment"));
			obj.setReport_label(rs.getString("report_label"));
			obj.setReport_addl_criteria_1(rs.getString("report_addl_criteria_1"));
			obj.setReport_addl_criteria_2(rs.getString("report_addl_criteria_2"));
			obj.setReport_addl_criteria_3(rs.getString("report_addl_criteria_3"));
			obj.setSanction_limit(rs.getBigDecimal("sanction_limit"));

			return obj;
		}
	}

	class M_LA5ArchivalDetailRowMapper implements RowMapper<M_LA5_Archival_Detail_Entity> {

		@Override
		public M_LA5_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA5_Archival_Detail_Entity obj = new M_LA5_Archival_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCust_id(rs.getString("cust_id"));
			obj.setAcct_number(rs.getString("acct_number"));
			obj.setAcct_name(rs.getString("acct_name"));
			obj.setData_type(rs.getString("data_type"));
			obj.setReport_remarks(rs.getString("report_remarks"));
			obj.setModification_remarks(rs.getString("modification_remarks"));
			obj.setData_entry_version(rs.getString("data_entry_version"));
			obj.setAcct_balance_in_pula(rs.getBigDecimal("acct_balance_in_pula"));
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_name(rs.getString("report_name"));
			obj.setCreate_user(rs.getString("create_user"));
			obj.setCreate_time(rs.getDate("create_time"));
			obj.setModify_user(rs.getString("modify_user"));
			obj.setModify_time(rs.getDate("modify_time"));
			obj.setVerify_user(rs.getString("verify_user"));
			obj.setVerify_time(rs.getDate("verify_time"));
			obj.setEntity_flg(rs.getString("entity_flg") != null ? rs.getString("entity_flg").charAt(0) : ' ');
			obj.setModify_flg(rs.getString("modify_flg") != null ? rs.getString("modify_flg").charAt(0) : ' ');
			obj.setDel_flg(rs.getString("del_flg") != null ? rs.getString("del_flg").charAt(0) : ' ');
			obj.setReport_name_1(rs.getString("report_name_1"));
			obj.setCcy(rs.getString("ccy"));
			obj.setSegment(rs.getString("segment"));
			obj.setReport_label(rs.getString("report_label"));
			obj.setReport_addl_criteria_1(rs.getString("report_addl_criteria_1"));
			obj.setReport_addl_criteria_2(rs.getString("report_addl_criteria_2"));
			obj.setReport_addl_criteria_3(rs.getString("report_addl_criteria_3"));
			obj.setSanction_limit(rs.getBigDecimal("sanction_limit"));
			return obj;
		}
	}

	public class M_LA5_Archival_Detail_Entity {
		private Long sno;
		private String cust_id;

		private String acct_number;
		private String acct_name;
		private String data_type;
		private String report_remarks;
		private String modification_remarks;
		private String data_entry_version;
		private BigDecimal acct_balance_in_pula;

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date report_date;
		private String report_name;
		private String create_user;
		private Date create_time;
		private String modify_user;
		private Date modify_time;
		private String verify_user;
		private Date verify_time;
		private char entity_flg;
		private char modify_flg;
		private char del_flg;
		private String report_name_1;
		private String ccy;
		private String segment;
		private String report_label;
		private String report_addl_criteria_1;
		private String report_addl_criteria_2;
		private String report_addl_criteria_3;
		private BigDecimal sanction_limit;

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

		public String getReport_remarks() {
			return report_remarks;
		}

		public void setReport_remarks(String report_remarks) {
			this.report_remarks = report_remarks;
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

		public String getReport_name() {
			return report_name;
		}

		public void setReport_name(String report_name) {
			this.report_name = report_name;
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

		public char getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(char entity_flg) {
			this.entity_flg = entity_flg;
		}

		public char getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(char modify_flg) {
			this.modify_flg = modify_flg;
		}

		public char getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(char del_flg) {
			this.del_flg = del_flg;
		}

		public String getReport_name_1() {
			return report_name_1;
		}

		public void setReport_name_1(String report_name_1) {
			this.report_name_1 = report_name_1;
		}

		public String getCcy() {
			return ccy;
		}

		public void setCcy(String ccy) {
			this.ccy = ccy;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
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

		public BigDecimal getSanction_limit() {
			return sanction_limit;
		}

		public void setSanction_limit(BigDecimal sanction_limit) {
			this.sanction_limit = sanction_limit;
		}
	}

	// MODEL AND VIEW METHOD summary

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_LA5View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		System.out.println("M_LA5 View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL + RESUB MODE
		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

			List<M_LA5_Archival_Summary_Entity> T1Master = new ArrayList<>();

			try {

				Date dt = dateformat.parse(todate);

				T1Master = getdatabydateListarchival(dt, version);

				System.out.println(type + " Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));
				System.out.println("getishighestversion(dt, version) : " + getishighestversion(dt, version));
				mv.addObject("allowdetail", getishighestversion(dt, version));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		}

		// NORMAL MODE
		else {

			List<M_LA5_Summary_Entity> T1Master = new ArrayList<>();

			try {

				Date dt = dateformat.parse(todate);

				T1Master = getDataByDate(dt);

				System.out.println("Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/M_LA5");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getM_LA5currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version, HttpServletRequest req1,
			Model md) {

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

			// ARCHIVAL / RESUB MODE
			if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

				System.out.println(type + " DETAIL MODE");

				List<M_LA5_Archival_Detail_Entity> detailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {

					detailList = GetArchivalDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);

				} else {

					detailList = getArchivalDetaildatabydateList(parsedDate);
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);

				System.out.println(type + " DETAIL COUNT: " + detailList.size());
			}

			// CURRENT MODE
			else {

				List<M_LA5_Detail_Entity> currentDetailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {

					currentDetailList = GetDetailDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);

				} else {

					currentDetailList = getDetaildatabydateList(parsedDate);
				}

				mv.addObject("reportdetails", currentDetailList);
				mv.addObject("reportmaster12", currentDetailList);

				System.out.println("CURRENT DETAIL COUNT: " + currentDetailList.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", e.getMessage());
		}

		mv.setViewName("BRRS/M_LA5");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getM_LA5Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<M_LA5_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_LA5_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_LA5_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_LA5  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		ModelAndView mv = new ModelAndView("BRRS/M_LA5");

		System.out.println("sno is : " + SNO);
		System.out.println("Type: " + type);
		if (SNO != null) {
			if (type == "RESUB" || type.equals("RESUB")) {
				System.out.println("Inside RESUB FETCH");
				M_LA5_Detail_Entity M_LA5Entity = findBySnoArch(SNO);
				if (M_LA5Entity != null && M_LA5Entity.getReport_date() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(M_LA5Entity.getReport_date());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("M_LA5Data", M_LA5Entity);
			} else {
				M_LA5_Detail_Entity M_LA5Entity = findBySno(SNO);
				if (M_LA5Entity != null && M_LA5Entity.getReport_date() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(M_LA5Entity.getReport_date());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("M_LA5Data", M_LA5Entity);
			}
		}
		mv.addObject("type", type);
		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {

		try {

			String Sno = request.getParameter("sno");

			String acctBalanceInpula = request.getParameter("acct_balance_in_pula");

			String acctName = request.getParameter("acct_name");

			String reportDateStr = request.getParameter("report_date");

			System.out.println("Sno is : " + Sno);
			String type = request.getParameter("type");
			String entry = (request.getParameter("entry") != null) ? request.getParameter("entry") : "YES";

			// Load Existing Record
			M_LA5_Detail_Entity existing = null;

			System.out.println("type is : " + type);
			if ((type == "RESUB") || (type.equals("RESUB"))) {
				existing = findBySnoArch(Sno);
			} else {
				existing = findBySno(Sno);
			}
			M_LA5_Detail_Entity oldcopy = new M_LA5_Detail_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

			if (existing == null) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			// Update Name
			if (acctName != null && !acctName.isEmpty()) {

				if (existing.getAcct_name() == null || !existing.getAcct_name().equals(acctName)) {

					existing.setAcct_name(acctName);

					isChanged = true;
				}
			}

			// Update Balance
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
				if (type == "RESUB" || type.equals("RESUB")) {
					System.out.println("Inside RESUB UPDATE");
					sql = "UPDATE BRRS_M_LA5_ARCHIVALTABLE_DETAIL " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ? " + "WHERE SNO = ?";
				} else {
					sql = "UPDATE BRRS_M_LA5_DETAILTABLE " + "SET ACCT_NAME = ?, " + "ACCT_BALANCE_IN_PULA = ?" + //
							"WHERE SNO = ?";
				}
				jdbcTemplate.update(sql, existing.getAcct_name(), existing.getAcct_balance_in_pula(), Sno);
				if ((type == "RESUB") || (type.equals("RESUB"))) {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "M_LA5 Archival Screen",
							"BRRS_M_LA5_ARCHIVALTABLE_DETAIL");
				} else {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "M_LA5 Screen",
							"BRRS_M_LA5_DETAILTABLE");
				}
				System.out.println("Record updated using JDBC");

				Run_M_LA5_Procudure(reportDateStr, type, entry);

				if ((type == "RESUB" || type.equals("RESUB")) && (entry == "NO" || entry.equals("NO"))) {
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}
				return ResponseEntity.ok("Record updated successfully!");
			} else {
				return ResponseEntity.ok("No changes were made.");
			}

		}

		catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<?> callregenprocedure(HttpServletRequest request) {
		try {
			Run_M_LA5_Procudure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());

		}
	}

	private void Run_M_LA5_Procudure(String reportDateStr, String type, String entry) {

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
						String bdsql = "DELETE FROM BRRS_M_LA5_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(bdsql, formattedDate);
						System.out.println("Successfully deleted before executing procedure " + rowsDeleted + " rows.");

						String sqltransfer = "INSERT INTO BRRS_M_LA5_DETAILTABLE ("
								+ "SNO, CUST_ID, ACCT_NUMBER, ACCT_BALANCE_IN_PULA, "
								+ "REPORT_LABEL, REPORT_ADDL_CRITERIA_1, MODIFICATION_REMARKS, REPORT_REMARKS, "
								+ "REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION) "
								+ "SELECT SNO, CUST_ID, ACCT_NUMBER, ACCT_BALANCE_IN_PULA, "
								+ "REPORT_LABEL, REPORT_ADDL_CRITERIA_1, MODIFICATION_REMARKS, REPORT_REMARKS, "
								+ "REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION "
								+ "FROM BRRS_M_LA5_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

						int rowsInserted = jdbcTemplate.update(sqltransfer, formattedDate);
						System.out.println("Successfully transferred " + rowsInserted + " rows.");
					}

					if (shouldExecuteProcedure) {
						jdbcTemplate.update("BEGIN BRRS_M_LA5_SUMMARY_PROCEDURE(?); END;", formattedDate);
						System.out.println("Procedure executed");
					}

					if (isResubNoEntry) {
						String adsql = "DELETE FROM BRRS_M_LA5_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(adsql, formattedDate);
						System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

						String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_LA5_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
						Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, formattedDate);
						int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

						StringBuilder columnsPart = new StringBuilder();
						String[] tokens = { "PRODUCT", "USD", "ZAR", "GBP", "EURO", "YEN", "C6", "C7", "C8", "TOTAL" };

						// Dynamically generate R6 to R62 columns
						for (int i = 6; i <= 62; i++) {
							for (String token : tokens) {
								columnsPart.append("R").append(i).append("_").append(token).append(", ");
							}
						}

						// Build the final query cleanly - Notice the '?' replacing REPORT_VERSION in
						// SELECT
						String finalsql = "INSERT INTO BRRS_M_LA5_ARCHIVALTABLE_SUMMARY (" + columnsPart.toString()
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
								+ "SELECT " + columnsPart.toString()
								+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
								+ "FROM BRRS_M_LA5_SUMMARYTABLE WHERE REPORT_DATE = ?";

						int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, formattedDate);
						System.out.println("Successfully transferred " + rowsInsertedSum + " rows.");

						String adsumsql = "DELETE FROM BRRS_M_LA5_SUMMARYTABLE WHERE REPORT_DATE = ?";
						int rowsDeletedSum = jdbcTemplate.update(adsumsql, formattedDate);
						System.out.println("Deleted from summary " + rowsDeletedSum + " rows after transfering.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public byte[] BRRS_M_LA5DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  M_LA5 Details...");
			System.out.println("came to Detail download service");

			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {
				byte[] ARCHIVALreport = getM_LA5DetailNewExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LA5DetailsDetail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_LA5_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA5_Detail_Entity item : reportData) {
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
				logger.info("No data found for M_LA5 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LA5 Excel", e);
			return new byte[0];
		}
	}

	public byte[] getM_LA5DetailNewExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for M_LA5 ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LA5 Detail NEW");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };
			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_LA5_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA5_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for M_LA5 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LA5 NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] BRRS_M_LA5Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.M_LA5");

		// ARCHIVAL check
		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null
				&& version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_LA5ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
		}
		if ("email".equalsIgnoreCase(format) && version == null) {
			logger.info("Got format as Email");
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_LA5EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
		} else {
			// Fetch data

			List<M_LA5_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

			System.out.println("DATA SIZE IS : " + dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  M_LA5 report. Returning empty result.");
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

				int startRow = 1;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_LA5_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						Cell R12Cell = row.createCell(4);

						if (record.getREPORT_DATE() != null) {

							R12Cell.setCellValue(record.getREPORT_DATE());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
						}

						row = sheet.getRow(5);
						// row6
						Cell cell = row.getCell(1);
						if (record.getR6_usd() != null) {
							cell.setCellValue(record.getR6_usd().doubleValue());

						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						cell = row.getCell(2);
						if (record.getR6_zar() != null) {
							cell.setCellValue(record.getR6_zar().doubleValue());

						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						cell = row.getCell(3);
						if (record.getR6_gbp() != null) {
							cell.setCellValue(record.getR6_gbp().doubleValue());

						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						cell = row.getCell(4);
						if (record.getR6_euro() != null) {
							cell.setCellValue(record.getR6_euro().doubleValue());

						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						cell = row.getCell(5);
						if (record.getR6_yen() != null) {
							cell.setCellValue(record.getR6_yen().doubleValue());

						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						cell = row.getCell(6);
						if (record.getR6_c6() != null) {
							cell.setCellValue(record.getR6_c6().doubleValue());

						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						cell = row.getCell(7);
						if (record.getR6_c7() != null) {
							cell.setCellValue(record.getR6_c7().doubleValue());

						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						cell = row.getCell(8);
						if (record.getR6_c8() != null) {
							cell.setCellValue(record.getR6_c8().doubleValue());

						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						cell = row.getCell(9);
						if (record.getR6_total() != null) {
							cell.setCellValue(record.getR6_total().doubleValue());

						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row7
						row = sheet.getRow(6);
						cell = row.getCell(1);
						if (record.getR7_usd() != null) {
							cell.setCellValue(record.getR7_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR7_zar() != null) {
							cell.setCellValue(record.getR7_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR7_gbp() != null) {
							cell.setCellValue(record.getR7_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR7_euro() != null) {
							cell.setCellValue(record.getR7_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR7_yen() != null) {
							cell.setCellValue(record.getR7_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR7_c6() != null) {
							cell.setCellValue(record.getR7_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR7_c7() != null) {
							cell.setCellValue(record.getR7_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR7_c8() != null) {
							cell.setCellValue(record.getR7_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR7_total() != null) {
							cell.setCellValue(record.getR7_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row8
						row = sheet.getRow(7);
						cell = row.getCell(1);
						if (record.getR8_usd() != null) {
							cell.setCellValue(record.getR8_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR8_zar() != null) {
							cell.setCellValue(record.getR8_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR8_gbp() != null) {
							cell.setCellValue(record.getR8_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR8_euro() != null) {
							cell.setCellValue(record.getR8_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR8_yen() != null) {
							cell.setCellValue(record.getR8_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR8_c6() != null) {
							cell.setCellValue(record.getR8_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR8_c7() != null) {
							cell.setCellValue(record.getR8_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR8_c8() != null) {
							cell.setCellValue(record.getR8_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR8_total() != null) {
							cell.setCellValue(record.getR8_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row9
						row = sheet.getRow(8);

						cell = row.getCell(1);
						if (record.getR9_usd() != null) {
							cell.setCellValue(record.getR9_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR9_zar() != null) {
							cell.setCellValue(record.getR9_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR9_gbp() != null) {
							cell.setCellValue(record.getR9_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR9_euro() != null) {
							cell.setCellValue(record.getR9_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR9_yen() != null) {
							cell.setCellValue(record.getR9_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR9_c6() != null) {
							cell.setCellValue(record.getR9_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR9_c7() != null) {
							cell.setCellValue(record.getR9_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR9_c8() != null) {
							cell.setCellValue(record.getR9_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR9_total() != null) {
							cell.setCellValue(record.getR9_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row10
						row = sheet.getRow(9);

						cell = row.getCell(1);
						if (record.getR10_usd() != null) {
							cell.setCellValue(record.getR10_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR10_zar() != null) {
							cell.setCellValue(record.getR10_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR10_gbp() != null) {
							cell.setCellValue(record.getR10_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR10_euro() != null) {
							cell.setCellValue(record.getR10_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR10_yen() != null) {
							cell.setCellValue(record.getR10_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR10_c6() != null) {
							cell.setCellValue(record.getR10_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR10_c7() != null) {
							cell.setCellValue(record.getR10_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR10_c8() != null) {
							cell.setCellValue(record.getR10_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR10_total() != null) {
							cell.setCellValue(record.getR10_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row11
						row = sheet.getRow(10);

						cell = row.getCell(1);
						if (record.getR11_usd() != null) {
							cell.setCellValue(record.getR11_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR11_zar() != null) {
							cell.setCellValue(record.getR11_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR11_gbp() != null) {
							cell.setCellValue(record.getR11_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR11_euro() != null) {
							cell.setCellValue(record.getR11_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR11_yen() != null) {
							cell.setCellValue(record.getR11_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR11_c6() != null) {
							cell.setCellValue(record.getR11_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR11_c7() != null) {
							cell.setCellValue(record.getR11_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR11_c8() != null) {
							cell.setCellValue(record.getR11_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR11_total() != null) {
							cell.setCellValue(record.getR11_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row12
						row = sheet.getRow(11);

						cell = row.getCell(1);
						if (record.getR12_usd() != null) {
							cell.setCellValue(record.getR12_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR12_zar() != null) {
							cell.setCellValue(record.getR12_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR12_gbp() != null) {
							cell.setCellValue(record.getR12_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR12_euro() != null) {
							cell.setCellValue(record.getR12_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR12_yen() != null) {
							cell.setCellValue(record.getR12_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR12_c6() != null) {
							cell.setCellValue(record.getR12_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR12_c7() != null) {
							cell.setCellValue(record.getR12_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR12_c8() != null) {
							cell.setCellValue(record.getR12_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR12_total() != null) {
							cell.setCellValue(record.getR12_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row13
						row = sheet.getRow(12);

						cell = row.getCell(1);
						if (record.getR13_usd() != null) {
							cell.setCellValue(record.getR13_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR13_zar() != null) {
							cell.setCellValue(record.getR13_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR13_gbp() != null) {
							cell.setCellValue(record.getR13_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR13_euro() != null) {
							cell.setCellValue(record.getR13_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR13_yen() != null) {
							cell.setCellValue(record.getR13_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR13_c6() != null) {
							cell.setCellValue(record.getR13_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR13_c7() != null) {
							cell.setCellValue(record.getR13_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR13_c8() != null) {
							cell.setCellValue(record.getR13_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR13_total() != null) {
							cell.setCellValue(record.getR13_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row14
						row = sheet.getRow(13);

						cell = row.getCell(1);
						if (record.getR14_usd() != null) {
							cell.setCellValue(record.getR14_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR14_zar() != null) {
							cell.setCellValue(record.getR14_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR14_gbp() != null) {
							cell.setCellValue(record.getR14_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR14_euro() != null) {
							cell.setCellValue(record.getR14_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR14_yen() != null) {
							cell.setCellValue(record.getR14_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR14_c6() != null) {
							cell.setCellValue(record.getR14_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR14_c7() != null) {
							cell.setCellValue(record.getR14_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR14_c8() != null) {
							cell.setCellValue(record.getR14_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR14_total() != null) {
							cell.setCellValue(record.getR14_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row15
						row = sheet.getRow(14);

						cell = row.getCell(1);
						if (record.getR15_usd() != null) {
							cell.setCellValue(record.getR15_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR15_zar() != null) {
							cell.setCellValue(record.getR15_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR15_gbp() != null) {
							cell.setCellValue(record.getR15_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR15_euro() != null) {
							cell.setCellValue(record.getR15_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR15_yen() != null) {
							cell.setCellValue(record.getR15_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR15_c6() != null) {
							cell.setCellValue(record.getR15_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR15_c7() != null) {
							cell.setCellValue(record.getR15_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR15_c8() != null) {
							cell.setCellValue(record.getR15_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR15_total() != null) {
							cell.setCellValue(record.getR15_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row16
						row = sheet.getRow(15);

						cell = row.getCell(1);
						if (record.getR16_usd() != null) {
							cell.setCellValue(record.getR16_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR16_zar() != null) {
							cell.setCellValue(record.getR16_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR16_gbp() != null) {
							cell.setCellValue(record.getR16_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR16_euro() != null) {
							cell.setCellValue(record.getR16_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR16_yen() != null) {
							cell.setCellValue(record.getR16_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR16_c6() != null) {
							cell.setCellValue(record.getR16_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR16_c7() != null) {
							cell.setCellValue(record.getR16_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR16_c8() != null) {
							cell.setCellValue(record.getR16_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR16_total() != null) {
							cell.setCellValue(record.getR16_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row17
						row = sheet.getRow(16);

						cell = row.getCell(1);
						if (record.getR17_usd() != null) {
							cell.setCellValue(record.getR17_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR17_zar() != null) {
							cell.setCellValue(record.getR17_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR17_gbp() != null) {
							cell.setCellValue(record.getR17_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR17_euro() != null) {
							cell.setCellValue(record.getR17_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR17_yen() != null) {
							cell.setCellValue(record.getR17_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR17_c6() != null) {
							cell.setCellValue(record.getR17_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR17_c7() != null) {
							cell.setCellValue(record.getR17_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR17_c8() != null) {
							cell.setCellValue(record.getR17_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR17_total() != null) {
							cell.setCellValue(record.getR17_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row18
						row = sheet.getRow(17);

						cell = row.getCell(1);
						if (record.getR18_usd() != null) {
							cell.setCellValue(record.getR18_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR18_zar() != null) {
							cell.setCellValue(record.getR18_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR18_gbp() != null) {
							cell.setCellValue(record.getR18_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR18_euro() != null) {
							cell.setCellValue(record.getR18_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR18_yen() != null) {
							cell.setCellValue(record.getR18_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR18_c6() != null) {
							cell.setCellValue(record.getR18_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR18_c7() != null) {
							cell.setCellValue(record.getR18_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR18_c8() != null) {
							cell.setCellValue(record.getR18_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR18_total() != null) {
							cell.setCellValue(record.getR18_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row19
						row = sheet.getRow(18);

						cell = row.getCell(1);
						if (record.getR19_usd() != null) {
							cell.setCellValue(record.getR19_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR19_zar() != null) {
							cell.setCellValue(record.getR19_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR19_gbp() != null) {
							cell.setCellValue(record.getR19_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR19_euro() != null) {
							cell.setCellValue(record.getR19_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR19_yen() != null) {
							cell.setCellValue(record.getR19_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR19_c6() != null) {
							cell.setCellValue(record.getR19_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR19_c7() != null) {
							cell.setCellValue(record.getR19_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR19_c8() != null) {
							cell.setCellValue(record.getR19_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR19_total() != null) {
							cell.setCellValue(record.getR19_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row20
						row = sheet.getRow(19);

						cell = row.getCell(1);
						if (record.getR20_usd() != null) {
							cell.setCellValue(record.getR20_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR20_zar() != null) {
							cell.setCellValue(record.getR20_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR20_gbp() != null) {
							cell.setCellValue(record.getR20_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR20_euro() != null) {
							cell.setCellValue(record.getR20_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR20_yen() != null) {
							cell.setCellValue(record.getR20_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR20_c6() != null) {
							cell.setCellValue(record.getR20_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR20_c7() != null) {
							cell.setCellValue(record.getR20_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR20_c8() != null) {
							cell.setCellValue(record.getR20_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR20_total() != null) {
							cell.setCellValue(record.getR20_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row21
						row = sheet.getRow(20);

						cell = row.getCell(1);
						if (record.getR21_usd() != null) {
							cell.setCellValue(record.getR21_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR21_zar() != null) {
							cell.setCellValue(record.getR21_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR21_gbp() != null) {
							cell.setCellValue(record.getR21_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR21_euro() != null) {
							cell.setCellValue(record.getR21_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR21_yen() != null) {
							cell.setCellValue(record.getR21_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR21_c6() != null) {
							cell.setCellValue(record.getR21_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR21_c7() != null) {
							cell.setCellValue(record.getR21_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR21_c8() != null) {
							cell.setCellValue(record.getR21_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR21_total() != null) {
							cell.setCellValue(record.getR21_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row22
						row = sheet.getRow(21);

						cell = row.getCell(1);
						if (record.getR22_usd() != null) {
							cell.setCellValue(record.getR22_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR22_zar() != null) {
							cell.setCellValue(record.getR22_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR22_gbp() != null) {
							cell.setCellValue(record.getR22_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR22_euro() != null) {
							cell.setCellValue(record.getR22_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR22_yen() != null) {
							cell.setCellValue(record.getR22_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR22_c6() != null) {
							cell.setCellValue(record.getR22_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR22_c7() != null) {
							cell.setCellValue(record.getR22_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR22_c8() != null) {
							cell.setCellValue(record.getR22_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR22_total() != null) {
							cell.setCellValue(record.getR22_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row23
						row = sheet.getRow(22);

						cell = row.getCell(1);
						if (record.getR23_usd() != null) {
							cell.setCellValue(record.getR23_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR23_zar() != null) {
							cell.setCellValue(record.getR23_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR23_gbp() != null) {
							cell.setCellValue(record.getR23_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR23_euro() != null) {
							cell.setCellValue(record.getR23_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR23_yen() != null) {
							cell.setCellValue(record.getR23_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR23_c6() != null) {
							cell.setCellValue(record.getR23_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR23_c7() != null) {
							cell.setCellValue(record.getR23_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR23_c8() != null) {
							cell.setCellValue(record.getR23_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR23_total() != null) {
							cell.setCellValue(record.getR23_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row24
						row = sheet.getRow(23);

						cell = row.getCell(1);
						if (record.getR24_usd() != null) {
							cell.setCellValue(record.getR24_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR24_zar() != null) {
							cell.setCellValue(record.getR24_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR24_gbp() != null) {
							cell.setCellValue(record.getR24_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR24_euro() != null) {
							cell.setCellValue(record.getR24_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR24_yen() != null) {
							cell.setCellValue(record.getR24_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR24_c6() != null) {
							cell.setCellValue(record.getR24_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR24_c7() != null) {
							cell.setCellValue(record.getR24_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR24_c8() != null) {
							cell.setCellValue(record.getR24_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR24_total() != null) {
							cell.setCellValue(record.getR24_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row25
						row = sheet.getRow(24);

						cell = row.getCell(1);
						if (record.getR25_usd() != null) {
							cell.setCellValue(record.getR25_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR25_zar() != null) {
							cell.setCellValue(record.getR25_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR25_gbp() != null) {
							cell.setCellValue(record.getR25_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR25_euro() != null) {
							cell.setCellValue(record.getR25_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR25_yen() != null) {
							cell.setCellValue(record.getR25_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR25_c6() != null) {
							cell.setCellValue(record.getR25_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR25_c7() != null) {
							cell.setCellValue(record.getR25_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR25_c8() != null) {
							cell.setCellValue(record.getR25_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR25_total() != null) {
							cell.setCellValue(record.getR25_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row26
						row = sheet.getRow(25);

						cell = row.getCell(1);
						if (record.getR26_usd() != null) {
							cell.setCellValue(record.getR26_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR26_zar() != null) {
							cell.setCellValue(record.getR26_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR26_gbp() != null) {
							cell.setCellValue(record.getR26_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR26_euro() != null) {
							cell.setCellValue(record.getR26_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR26_yen() != null) {
							cell.setCellValue(record.getR26_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR26_c6() != null) {
							cell.setCellValue(record.getR26_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR26_c7() != null) {
							cell.setCellValue(record.getR26_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR26_c8() != null) {
							cell.setCellValue(record.getR26_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR26_total() != null) {
							cell.setCellValue(record.getR26_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row27
						row = sheet.getRow(26);

						cell = row.getCell(1);
						if (record.getR27_usd() != null) {
							cell.setCellValue(record.getR27_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR27_zar() != null) {
							cell.setCellValue(record.getR27_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR27_gbp() != null) {
							cell.setCellValue(record.getR27_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR27_euro() != null) {
							cell.setCellValue(record.getR27_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR27_yen() != null) {
							cell.setCellValue(record.getR27_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR27_c6() != null) {
							cell.setCellValue(record.getR27_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR27_c7() != null) {
							cell.setCellValue(record.getR27_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR27_c8() != null) {
							cell.setCellValue(record.getR27_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR27_total() != null) {
							cell.setCellValue(record.getR27_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row28
						row = sheet.getRow(27);

						cell = row.getCell(1);
						if (record.getR28_usd() != null) {
							cell.setCellValue(record.getR28_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR28_zar() != null) {
							cell.setCellValue(record.getR28_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR28_gbp() != null) {
							cell.setCellValue(record.getR28_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR28_euro() != null) {
							cell.setCellValue(record.getR28_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR28_yen() != null) {
							cell.setCellValue(record.getR28_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR28_c6() != null) {
							cell.setCellValue(record.getR28_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR28_c7() != null) {
							cell.setCellValue(record.getR28_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR28_c8() != null) {
							cell.setCellValue(record.getR28_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR28_total() != null) {
							cell.setCellValue(record.getR28_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row29
						row = sheet.getRow(28);

						cell = row.getCell(1);
						if (record.getR29_usd() != null) {
							cell.setCellValue(record.getR29_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR29_zar() != null) {
							cell.setCellValue(record.getR29_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR29_gbp() != null) {
							cell.setCellValue(record.getR29_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR29_euro() != null) {
							cell.setCellValue(record.getR29_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR29_yen() != null) {
							cell.setCellValue(record.getR29_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR29_c6() != null) {
							cell.setCellValue(record.getR29_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR29_c7() != null) {
							cell.setCellValue(record.getR29_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR29_c8() != null) {
							cell.setCellValue(record.getR29_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR29_total() != null) {
							cell.setCellValue(record.getR29_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row30
						row = sheet.getRow(29);

						cell = row.getCell(1);
						if (record.getR30_usd() != null) {
							cell.setCellValue(record.getR30_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR30_zar() != null) {
							cell.setCellValue(record.getR30_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR30_gbp() != null) {
							cell.setCellValue(record.getR30_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR30_euro() != null) {
							cell.setCellValue(record.getR30_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR30_yen() != null) {
							cell.setCellValue(record.getR30_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR30_c6() != null) {
							cell.setCellValue(record.getR30_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR30_c7() != null) {
							cell.setCellValue(record.getR30_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR30_c8() != null) {
							cell.setCellValue(record.getR30_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR30_total() != null) {
							cell.setCellValue(record.getR30_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row31
						row = sheet.getRow(30);

						cell = row.getCell(1);
						if (record.getR31_usd() != null) {
							cell.setCellValue(record.getR31_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR31_zar() != null) {
							cell.setCellValue(record.getR31_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR31_gbp() != null) {
							cell.setCellValue(record.getR31_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR31_euro() != null) {
							cell.setCellValue(record.getR31_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR31_yen() != null) {
							cell.setCellValue(record.getR31_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR31_c6() != null) {
							cell.setCellValue(record.getR31_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR31_c7() != null) {
							cell.setCellValue(record.getR31_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR31_c8() != null) {
							cell.setCellValue(record.getR31_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR31_total() != null) {
							cell.setCellValue(record.getR31_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row32
						row = sheet.getRow(31);

						cell = row.getCell(1);
						if (record.getR32_usd() != null) {
							cell.setCellValue(record.getR32_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR32_zar() != null) {
							cell.setCellValue(record.getR32_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR32_gbp() != null) {
							cell.setCellValue(record.getR32_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR32_euro() != null) {
							cell.setCellValue(record.getR32_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR32_yen() != null) {
							cell.setCellValue(record.getR32_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR32_c6() != null) {
							cell.setCellValue(record.getR32_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR32_c7() != null) {
							cell.setCellValue(record.getR32_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR32_c8() != null) {
							cell.setCellValue(record.getR32_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR32_total() != null) {
							cell.setCellValue(record.getR32_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row33
						row = sheet.getRow(32);

						cell = row.getCell(1);
						if (record.getR33_usd() != null) {
							cell.setCellValue(record.getR33_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR33_zar() != null) {
							cell.setCellValue(record.getR33_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR33_gbp() != null) {
							cell.setCellValue(record.getR33_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR33_euro() != null) {
							cell.setCellValue(record.getR33_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR33_yen() != null) {
							cell.setCellValue(record.getR33_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR33_c6() != null) {
							cell.setCellValue(record.getR33_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR33_c7() != null) {
							cell.setCellValue(record.getR33_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR33_c8() != null) {
							cell.setCellValue(record.getR33_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR33_total() != null) {
							cell.setCellValue(record.getR33_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row34
						row = sheet.getRow(33);

						cell = row.getCell(1);
						if (record.getR34_usd() != null) {
							cell.setCellValue(record.getR34_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR34_zar() != null) {
							cell.setCellValue(record.getR34_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR34_gbp() != null) {
							cell.setCellValue(record.getR34_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR34_euro() != null) {
							cell.setCellValue(record.getR34_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR34_yen() != null) {
							cell.setCellValue(record.getR34_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR34_c6() != null) {
							cell.setCellValue(record.getR34_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR34_c7() != null) {
							cell.setCellValue(record.getR34_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR34_c8() != null) {
							cell.setCellValue(record.getR34_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR34_total() != null) {
							cell.setCellValue(record.getR34_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row35
						row = sheet.getRow(34);

						cell = row.getCell(1);
						if (record.getR35_usd() != null) {
							cell.setCellValue(record.getR35_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR35_zar() != null) {
							cell.setCellValue(record.getR35_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR35_gbp() != null) {
							cell.setCellValue(record.getR35_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR35_euro() != null) {
							cell.setCellValue(record.getR35_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR35_yen() != null) {
							cell.setCellValue(record.getR35_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR35_c6() != null) {
							cell.setCellValue(record.getR35_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR35_c7() != null) {
							cell.setCellValue(record.getR35_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR35_c8() != null) {
							cell.setCellValue(record.getR35_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR35_total() != null) {
							cell.setCellValue(record.getR35_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row36
						row = sheet.getRow(35);

						cell = row.getCell(1);
						if (record.getR36_usd() != null) {
							cell.setCellValue(record.getR36_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR36_zar() != null) {
							cell.setCellValue(record.getR36_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR36_gbp() != null) {
							cell.setCellValue(record.getR36_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR36_euro() != null) {
							cell.setCellValue(record.getR36_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR36_yen() != null) {
							cell.setCellValue(record.getR36_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR36_c6() != null) {
							cell.setCellValue(record.getR36_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR36_c7() != null) {
							cell.setCellValue(record.getR36_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR36_c8() != null) {
							cell.setCellValue(record.getR36_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR36_total() != null) {
							cell.setCellValue(record.getR36_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row37
						row = sheet.getRow(36);

						cell = row.getCell(1);
						if (record.getR37_usd() != null) {
							cell.setCellValue(record.getR37_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR37_zar() != null) {
							cell.setCellValue(record.getR37_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR37_gbp() != null) {
							cell.setCellValue(record.getR37_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR37_euro() != null) {
							cell.setCellValue(record.getR37_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR37_yen() != null) {
							cell.setCellValue(record.getR37_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR37_c6() != null) {
							cell.setCellValue(record.getR37_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR37_c7() != null) {
							cell.setCellValue(record.getR37_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR37_c8() != null) {
							cell.setCellValue(record.getR37_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR37_total() != null) {
							cell.setCellValue(record.getR37_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row38
						row = sheet.getRow(37);

						cell = row.getCell(1);
						if (record.getR38_usd() != null) {
							cell.setCellValue(record.getR38_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR38_zar() != null) {
							cell.setCellValue(record.getR38_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR38_gbp() != null) {
							cell.setCellValue(record.getR38_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR38_euro() != null) {
							cell.setCellValue(record.getR38_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR38_yen() != null) {
							cell.setCellValue(record.getR38_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR38_c6() != null) {
							cell.setCellValue(record.getR38_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR38_c7() != null) {
							cell.setCellValue(record.getR38_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR38_c8() != null) {
							cell.setCellValue(record.getR38_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR38_total() != null) {
							cell.setCellValue(record.getR38_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row39
						row = sheet.getRow(38);

						cell = row.getCell(1);
						if (record.getR39_usd() != null) {
							cell.setCellValue(record.getR39_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR39_zar() != null) {
							cell.setCellValue(record.getR39_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR39_gbp() != null) {
							cell.setCellValue(record.getR39_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR39_euro() != null) {
							cell.setCellValue(record.getR39_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR39_yen() != null) {
							cell.setCellValue(record.getR39_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR39_c6() != null) {
							cell.setCellValue(record.getR39_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR39_c7() != null) {
							cell.setCellValue(record.getR39_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR39_c8() != null) {
							cell.setCellValue(record.getR39_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR39_total() != null) {
							cell.setCellValue(record.getR39_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row40
						row = sheet.getRow(39);

						cell = row.getCell(1);
						if (record.getR40_usd() != null) {
							cell.setCellValue(record.getR40_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR40_zar() != null) {
							cell.setCellValue(record.getR40_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR40_gbp() != null) {
							cell.setCellValue(record.getR40_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR40_euro() != null) {
							cell.setCellValue(record.getR40_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR40_yen() != null) {
							cell.setCellValue(record.getR40_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR40_c6() != null) {
							cell.setCellValue(record.getR40_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR40_c7() != null) {
							cell.setCellValue(record.getR40_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR40_c8() != null) {
							cell.setCellValue(record.getR40_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR40_total() != null) {
							cell.setCellValue(record.getR40_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row41
						row = sheet.getRow(40);

						cell = row.getCell(1);
						if (record.getR41_usd() != null) {
							cell.setCellValue(record.getR41_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR41_zar() != null) {
							cell.setCellValue(record.getR41_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR41_gbp() != null) {
							cell.setCellValue(record.getR41_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR41_euro() != null) {
							cell.setCellValue(record.getR41_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR41_yen() != null) {
							cell.setCellValue(record.getR41_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR41_c6() != null) {
							cell.setCellValue(record.getR41_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR41_c7() != null) {
							cell.setCellValue(record.getR41_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR41_c8() != null) {
							cell.setCellValue(record.getR41_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR41_total() != null) {
							cell.setCellValue(record.getR41_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row42
						row = sheet.getRow(41);

						cell = row.getCell(1);
						if (record.getR42_usd() != null) {
							cell.setCellValue(record.getR42_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR42_zar() != null) {
							cell.setCellValue(record.getR42_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR42_gbp() != null) {
							cell.setCellValue(record.getR42_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR42_euro() != null) {
							cell.setCellValue(record.getR42_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR42_yen() != null) {
							cell.setCellValue(record.getR42_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR42_c6() != null) {
							cell.setCellValue(record.getR42_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR42_c7() != null) {
							cell.setCellValue(record.getR42_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR42_c8() != null) {
							cell.setCellValue(record.getR42_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR42_total() != null) {
							cell.setCellValue(record.getR42_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						} // row43
						row = sheet.getRow(42);

						cell = row.getCell(1);
						if (record.getR43_usd() != null) {
							cell.setCellValue(record.getR43_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR43_zar() != null) {
							cell.setCellValue(record.getR43_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR43_gbp() != null) {
							cell.setCellValue(record.getR43_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR43_euro() != null) {
							cell.setCellValue(record.getR43_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR43_yen() != null) {
							cell.setCellValue(record.getR43_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR43_c6() != null) {
							cell.setCellValue(record.getR43_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR43_c7() != null) {
							cell.setCellValue(record.getR43_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR43_c8() != null) {
							cell.setCellValue(record.getR43_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR43_total() != null) {
							cell.setCellValue(record.getR43_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row44
						row = sheet.getRow(43);

						cell = row.getCell(1);
						if (record.getR44_usd() != null) {
							cell.setCellValue(record.getR44_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR44_zar() != null) {
							cell.setCellValue(record.getR44_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR44_gbp() != null) {
							cell.setCellValue(record.getR44_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR44_euro() != null) {
							cell.setCellValue(record.getR44_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR44_yen() != null) {
							cell.setCellValue(record.getR44_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR44_c6() != null) {
							cell.setCellValue(record.getR44_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR44_c7() != null) {
							cell.setCellValue(record.getR44_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR44_c8() != null) {
							cell.setCellValue(record.getR44_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR44_total() != null) {
							cell.setCellValue(record.getR44_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row45
						row = sheet.getRow(44);

						cell = row.getCell(1);
						if (record.getR45_usd() != null) {
							cell.setCellValue(record.getR45_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR45_zar() != null) {
							cell.setCellValue(record.getR45_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR45_gbp() != null) {
							cell.setCellValue(record.getR45_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR45_euro() != null) {
							cell.setCellValue(record.getR45_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR45_yen() != null) {
							cell.setCellValue(record.getR45_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR45_c6() != null) {
							cell.setCellValue(record.getR45_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR45_c7() != null) {
							cell.setCellValue(record.getR45_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR45_c8() != null) {
							cell.setCellValue(record.getR45_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR45_total() != null) {
							cell.setCellValue(record.getR45_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row46
						row = sheet.getRow(45);

						cell = row.getCell(1);
						if (record.getR46_usd() != null) {
							cell.setCellValue(record.getR46_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR46_zar() != null) {
							cell.setCellValue(record.getR46_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR46_gbp() != null) {
							cell.setCellValue(record.getR46_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR46_euro() != null) {
							cell.setCellValue(record.getR46_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR46_yen() != null) {
							cell.setCellValue(record.getR46_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR46_c6() != null) {
							cell.setCellValue(record.getR46_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR46_c7() != null) {
							cell.setCellValue(record.getR46_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR46_c8() != null) {
							cell.setCellValue(record.getR46_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR46_total() != null) {
							cell.setCellValue(record.getR46_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row47
						row = sheet.getRow(46);

						cell = row.getCell(1);
						if (record.getR47_usd() != null) {
							cell.setCellValue(record.getR47_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR47_zar() != null) {
							cell.setCellValue(record.getR47_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR47_gbp() != null) {
							cell.setCellValue(record.getR47_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR47_euro() != null) {
							cell.setCellValue(record.getR47_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR47_yen() != null) {
							cell.setCellValue(record.getR47_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR47_c6() != null) {
							cell.setCellValue(record.getR47_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR47_c7() != null) {
							cell.setCellValue(record.getR47_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR47_c8() != null) {
							cell.setCellValue(record.getR47_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR47_total() != null) {
							cell.setCellValue(record.getR47_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row48
						row = sheet.getRow(47);

						cell = row.getCell(1);
						if (record.getR48_usd() != null) {
							cell.setCellValue(record.getR48_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR48_zar() != null) {
							cell.setCellValue(record.getR48_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR48_gbp() != null) {
							cell.setCellValue(record.getR48_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR48_euro() != null) {
							cell.setCellValue(record.getR48_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR48_yen() != null) {
							cell.setCellValue(record.getR48_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR48_c6() != null) {
							cell.setCellValue(record.getR48_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR48_c7() != null) {
							cell.setCellValue(record.getR48_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR48_c8() != null) {
							cell.setCellValue(record.getR48_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR48_total() != null) {
							cell.setCellValue(record.getR48_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row49
						row = sheet.getRow(48);

						cell = row.getCell(1);
						if (record.getR49_usd() != null) {
							cell.setCellValue(record.getR49_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR49_zar() != null) {
							cell.setCellValue(record.getR49_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR49_gbp() != null) {
							cell.setCellValue(record.getR49_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR49_euro() != null) {
							cell.setCellValue(record.getR49_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR49_yen() != null) {
							cell.setCellValue(record.getR49_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR49_c6() != null) {
							cell.setCellValue(record.getR49_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR49_c7() != null) {
							cell.setCellValue(record.getR49_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR49_c8() != null) {
							cell.setCellValue(record.getR49_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR49_total() != null) {
							cell.setCellValue(record.getR49_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row50
						row = sheet.getRow(49);

						cell = row.getCell(1);
						if (record.getR50_usd() != null) {
							cell.setCellValue(record.getR50_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR50_zar() != null) {
							cell.setCellValue(record.getR50_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR50_gbp() != null) {
							cell.setCellValue(record.getR50_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR50_euro() != null) {
							cell.setCellValue(record.getR50_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR50_yen() != null) {
							cell.setCellValue(record.getR50_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR50_c6() != null) {
							cell.setCellValue(record.getR50_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR50_c7() != null) {
							cell.setCellValue(record.getR50_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR50_c8() != null) {
							cell.setCellValue(record.getR50_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR50_total() != null) {
							cell.setCellValue(record.getR50_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row51
						row = sheet.getRow(50);

						cell = row.getCell(1);
						if (record.getR51_usd() != null) {
							cell.setCellValue(record.getR51_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR51_zar() != null) {
							cell.setCellValue(record.getR51_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR51_gbp() != null) {
							cell.setCellValue(record.getR51_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR51_euro() != null) {
							cell.setCellValue(record.getR51_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR51_yen() != null) {
							cell.setCellValue(record.getR51_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR51_c6() != null) {
							cell.setCellValue(record.getR51_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR51_c7() != null) {
							cell.setCellValue(record.getR51_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR51_c8() != null) {
							cell.setCellValue(record.getR51_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR51_total() != null) {
							cell.setCellValue(record.getR51_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row52
						row = sheet.getRow(51);

						cell = row.getCell(1);
						if (record.getR52_usd() != null) {
							cell.setCellValue(record.getR52_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR52_zar() != null) {
							cell.setCellValue(record.getR52_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR52_gbp() != null) {
							cell.setCellValue(record.getR52_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR52_euro() != null) {
							cell.setCellValue(record.getR52_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR52_yen() != null) {
							cell.setCellValue(record.getR52_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR52_c6() != null) {
							cell.setCellValue(record.getR52_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR52_c7() != null) {
							cell.setCellValue(record.getR52_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR52_c8() != null) {
							cell.setCellValue(record.getR52_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR52_total() != null) {
							cell.setCellValue(record.getR52_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row53
						row = sheet.getRow(52);

						cell = row.getCell(1);
						if (record.getR53_usd() != null) {
							cell.setCellValue(record.getR53_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR53_zar() != null) {
							cell.setCellValue(record.getR53_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR53_gbp() != null) {
							cell.setCellValue(record.getR53_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR53_euro() != null) {
							cell.setCellValue(record.getR53_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR53_yen() != null) {
							cell.setCellValue(record.getR53_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR53_c6() != null) {
							cell.setCellValue(record.getR53_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR53_c7() != null) {
							cell.setCellValue(record.getR53_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR53_c8() != null) {
							cell.setCellValue(record.getR53_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR53_total() != null) {
							cell.setCellValue(record.getR53_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row54
						row = sheet.getRow(53);

						cell = row.getCell(1);
						if (record.getR54_usd() != null) {
							cell.setCellValue(record.getR54_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR54_zar() != null) {
							cell.setCellValue(record.getR54_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR54_gbp() != null) {
							cell.setCellValue(record.getR54_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR54_euro() != null) {
							cell.setCellValue(record.getR54_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR54_yen() != null) {
							cell.setCellValue(record.getR54_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR54_c6() != null) {
							cell.setCellValue(record.getR54_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR54_c7() != null) {
							cell.setCellValue(record.getR54_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR54_c8() != null) {
							cell.setCellValue(record.getR54_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR54_total() != null) {
							cell.setCellValue(record.getR54_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row55
						row = sheet.getRow(54);

						cell = row.getCell(1);
						if (record.getR55_usd() != null) {
							cell.setCellValue(record.getR55_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR55_zar() != null) {
							cell.setCellValue(record.getR55_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR55_gbp() != null) {
							cell.setCellValue(record.getR55_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR55_euro() != null) {
							cell.setCellValue(record.getR55_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR55_yen() != null) {
							cell.setCellValue(record.getR55_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR55_c6() != null) {
							cell.setCellValue(record.getR55_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR55_c7() != null) {
							cell.setCellValue(record.getR55_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR55_c8() != null) {
							cell.setCellValue(record.getR55_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR55_total() != null) {
							cell.setCellValue(record.getR55_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row56
						row = sheet.getRow(55);

						cell = row.getCell(1);
						if (record.getR56_usd() != null) {
							cell.setCellValue(record.getR56_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR56_zar() != null) {
							cell.setCellValue(record.getR56_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR56_gbp() != null) {
							cell.setCellValue(record.getR56_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR56_euro() != null) {
							cell.setCellValue(record.getR56_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR56_yen() != null) {
							cell.setCellValue(record.getR56_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR56_c6() != null) {
							cell.setCellValue(record.getR56_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR56_c7() != null) {
							cell.setCellValue(record.getR56_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR56_c8() != null) {
							cell.setCellValue(record.getR56_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR56_total() != null) {
							cell.setCellValue(record.getR56_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row57
						row = sheet.getRow(56);

						cell = row.getCell(1);
						if (record.getR57_usd() != null) {
							cell.setCellValue(record.getR57_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR57_zar() != null) {
							cell.setCellValue(record.getR57_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR57_gbp() != null) {
							cell.setCellValue(record.getR57_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR57_euro() != null) {
							cell.setCellValue(record.getR57_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR57_yen() != null) {
							cell.setCellValue(record.getR57_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR57_c6() != null) {
							cell.setCellValue(record.getR57_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR57_c7() != null) {
							cell.setCellValue(record.getR57_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR57_c8() != null) {
							cell.setCellValue(record.getR57_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR57_total() != null) {
							cell.setCellValue(record.getR57_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row58
						row = sheet.getRow(57);

						cell = row.getCell(1);
						if (record.getR58_usd() != null) {
							cell.setCellValue(record.getR58_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR58_zar() != null) {
							cell.setCellValue(record.getR58_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR58_gbp() != null) {
							cell.setCellValue(record.getR58_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR58_euro() != null) {
							cell.setCellValue(record.getR58_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR58_yen() != null) {
							cell.setCellValue(record.getR58_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR58_c6() != null) {
							cell.setCellValue(record.getR58_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR58_c7() != null) {
							cell.setCellValue(record.getR58_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR58_c8() != null) {
							cell.setCellValue(record.getR58_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR58_total() != null) {
							cell.setCellValue(record.getR58_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row59
						row = sheet.getRow(58);

						cell = row.getCell(1);
						if (record.getR59_usd() != null) {
							cell.setCellValue(record.getR59_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR59_zar() != null) {
							cell.setCellValue(record.getR59_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR59_gbp() != null) {
							cell.setCellValue(record.getR59_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR59_euro() != null) {
							cell.setCellValue(record.getR59_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR59_yen() != null) {
							cell.setCellValue(record.getR59_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR59_c6() != null) {
							cell.setCellValue(record.getR59_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR59_c7() != null) {
							cell.setCellValue(record.getR59_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR59_c8() != null) {
							cell.setCellValue(record.getR59_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR59_total() != null) {
							cell.setCellValue(record.getR59_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row60
						row = sheet.getRow(59);

						cell = row.getCell(1);
						if (record.getR60_usd() != null) {
							cell.setCellValue(record.getR60_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR60_zar() != null) {
							cell.setCellValue(record.getR60_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR60_gbp() != null) {
							cell.setCellValue(record.getR60_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR60_euro() != null) {
							cell.setCellValue(record.getR60_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR60_yen() != null) {
							cell.setCellValue(record.getR60_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR60_c6() != null) {
							cell.setCellValue(record.getR60_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR60_c7() != null) {
							cell.setCellValue(record.getR60_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR60_c8() != null) {
							cell.setCellValue(record.getR60_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR60_total() != null) {
							cell.setCellValue(record.getR60_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row61
						row = sheet.getRow(60);

						cell = row.getCell(1);
						if (record.getR61_usd() != null) {
							cell.setCellValue(record.getR61_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR61_zar() != null) {
							cell.setCellValue(record.getR61_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR61_gbp() != null) {
							cell.setCellValue(record.getR61_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR61_euro() != null) {
							cell.setCellValue(record.getR61_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR61_yen() != null) {
							cell.setCellValue(record.getR61_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR61_c6() != null) {
							cell.setCellValue(record.getR61_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR61_c7() != null) {
							cell.setCellValue(record.getR61_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR61_c8() != null) {
							cell.setCellValue(record.getR61_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR61_total() != null) {
							cell.setCellValue(record.getR61_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row62
						row = sheet.getRow(61);

						cell = row.getCell(1);
						if (record.getR62_usd() != null) {
							cell.setCellValue(record.getR62_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR62_zar() != null) {
							cell.setCellValue(record.getR62_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR62_gbp() != null) {
							cell.setCellValue(record.getR62_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR62_euro() != null) {
							cell.setCellValue(record.getR62_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR62_yen() != null) {
							cell.setCellValue(record.getR62_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR62_c6() != null) {
							cell.setCellValue(record.getR62_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR62_c7() != null) {
							cell.setCellValue(record.getR62_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR62_c8() != null) {
							cell.setCellValue(record.getR62_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR62_total() != null) {
							cell.setCellValue(record.getR62_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA5 SUMMARY", null,
							"BRRS_M_LA5_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	public byte[] getExcelM_LA5ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {

		}

		List<M_LA5_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA5 new report. Returning empty result.");
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

			// --- End of Style Definitions ---

			int startRow = 1;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LA5_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell R12Cell = row.createCell(4);

					if (record.getREPORT_DATE() != null) {

						R12Cell.setCellValue(record.getREPORT_DATE());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}

					row = sheet.getRow(5);
					// row6
					Cell cell = row.getCell(1);
					if (record.getR6_usd() != null) {
						cell.setCellValue(record.getR6_usd().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(2);
					if (record.getR6_zar() != null) {
						cell.setCellValue(record.getR6_zar().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(3);
					if (record.getR6_gbp() != null) {
						cell.setCellValue(record.getR6_gbp().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(4);
					if (record.getR6_euro() != null) {
						cell.setCellValue(record.getR6_euro().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(5);
					if (record.getR6_yen() != null) {
						cell.setCellValue(record.getR6_yen().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(6);
					if (record.getR6_c6() != null) {
						cell.setCellValue(record.getR6_c6().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(7);
					if (record.getR6_c7() != null) {
						cell.setCellValue(record.getR6_c7().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(8);
					if (record.getR6_c8() != null) {
						cell.setCellValue(record.getR6_c8().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(9);
					if (record.getR6_total() != null) {
						cell.setCellValue(record.getR6_total().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row7
					row = sheet.getRow(6);
					cell = row.getCell(1);
					if (record.getR7_usd() != null) {
						cell.setCellValue(record.getR7_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR7_zar() != null) {
						cell.setCellValue(record.getR7_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR7_gbp() != null) {
						cell.setCellValue(record.getR7_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR7_euro() != null) {
						cell.setCellValue(record.getR7_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR7_yen() != null) {
						cell.setCellValue(record.getR7_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR7_c6() != null) {
						cell.setCellValue(record.getR7_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR7_c7() != null) {
						cell.setCellValue(record.getR7_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR7_c8() != null) {
						cell.setCellValue(record.getR7_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR7_total() != null) {
						cell.setCellValue(record.getR7_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row8
					row = sheet.getRow(7);
					cell = row.getCell(1);
					if (record.getR8_usd() != null) {
						cell.setCellValue(record.getR8_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR8_zar() != null) {
						cell.setCellValue(record.getR8_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR8_gbp() != null) {
						cell.setCellValue(record.getR8_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR8_euro() != null) {
						cell.setCellValue(record.getR8_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR8_yen() != null) {
						cell.setCellValue(record.getR8_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR8_c6() != null) {
						cell.setCellValue(record.getR8_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR8_c7() != null) {
						cell.setCellValue(record.getR8_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR8_c8() != null) {
						cell.setCellValue(record.getR8_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR8_total() != null) {
						cell.setCellValue(record.getR8_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row9
					row = sheet.getRow(8);

					cell = row.getCell(1);
					if (record.getR9_usd() != null) {
						cell.setCellValue(record.getR9_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR9_zar() != null) {
						cell.setCellValue(record.getR9_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR9_gbp() != null) {
						cell.setCellValue(record.getR9_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR9_euro() != null) {
						cell.setCellValue(record.getR9_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR9_yen() != null) {
						cell.setCellValue(record.getR9_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR9_c6() != null) {
						cell.setCellValue(record.getR9_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR9_c7() != null) {
						cell.setCellValue(record.getR9_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR9_c8() != null) {
						cell.setCellValue(record.getR9_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR9_total() != null) {
						cell.setCellValue(record.getR9_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row10
					row = sheet.getRow(9);

					cell = row.getCell(1);
					if (record.getR10_usd() != null) {
						cell.setCellValue(record.getR10_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR10_zar() != null) {
						cell.setCellValue(record.getR10_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR10_gbp() != null) {
						cell.setCellValue(record.getR10_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR10_euro() != null) {
						cell.setCellValue(record.getR10_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR10_yen() != null) {
						cell.setCellValue(record.getR10_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR10_c6() != null) {
						cell.setCellValue(record.getR10_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR10_c7() != null) {
						cell.setCellValue(record.getR10_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR10_c8() != null) {
						cell.setCellValue(record.getR10_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR10_total() != null) {
						cell.setCellValue(record.getR10_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					cell = row.getCell(1);
					if (record.getR11_usd() != null) {
						cell.setCellValue(record.getR11_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR11_zar() != null) {
						cell.setCellValue(record.getR11_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR11_gbp() != null) {
						cell.setCellValue(record.getR11_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR11_euro() != null) {
						cell.setCellValue(record.getR11_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR11_yen() != null) {
						cell.setCellValue(record.getR11_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR11_c6() != null) {
						cell.setCellValue(record.getR11_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR11_c7() != null) {
						cell.setCellValue(record.getR11_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR11_c8() != null) {
						cell.setCellValue(record.getR11_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR11_total() != null) {
						cell.setCellValue(record.getR11_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell = row.getCell(1);
					if (record.getR12_usd() != null) {
						cell.setCellValue(record.getR12_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR12_zar() != null) {
						cell.setCellValue(record.getR12_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR12_gbp() != null) {
						cell.setCellValue(record.getR12_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR12_euro() != null) {
						cell.setCellValue(record.getR12_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR12_yen() != null) {
						cell.setCellValue(record.getR12_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR12_c6() != null) {
						cell.setCellValue(record.getR12_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR12_c7() != null) {
						cell.setCellValue(record.getR12_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR12_c8() != null) {
						cell.setCellValue(record.getR12_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR12_total() != null) {
						cell.setCellValue(record.getR12_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					cell = row.getCell(1);
					if (record.getR13_usd() != null) {
						cell.setCellValue(record.getR13_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR13_zar() != null) {
						cell.setCellValue(record.getR13_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR13_gbp() != null) {
						cell.setCellValue(record.getR13_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR13_euro() != null) {
						cell.setCellValue(record.getR13_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR13_yen() != null) {
						cell.setCellValue(record.getR13_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR13_c6() != null) {
						cell.setCellValue(record.getR13_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR13_c7() != null) {
						cell.setCellValue(record.getR13_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR13_c8() != null) {
						cell.setCellValue(record.getR13_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR13_total() != null) {
						cell.setCellValue(record.getR13_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					cell = row.getCell(1);
					if (record.getR14_usd() != null) {
						cell.setCellValue(record.getR14_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR14_zar() != null) {
						cell.setCellValue(record.getR14_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR14_gbp() != null) {
						cell.setCellValue(record.getR14_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR14_euro() != null) {
						cell.setCellValue(record.getR14_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR14_yen() != null) {
						cell.setCellValue(record.getR14_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR14_c6() != null) {
						cell.setCellValue(record.getR14_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR14_c7() != null) {
						cell.setCellValue(record.getR14_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR14_c8() != null) {
						cell.setCellValue(record.getR14_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR14_total() != null) {
						cell.setCellValue(record.getR14_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row15
					row = sheet.getRow(14);

					cell = row.getCell(1);
					if (record.getR15_usd() != null) {
						cell.setCellValue(record.getR15_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR15_zar() != null) {
						cell.setCellValue(record.getR15_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR15_gbp() != null) {
						cell.setCellValue(record.getR15_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR15_euro() != null) {
						cell.setCellValue(record.getR15_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR15_yen() != null) {
						cell.setCellValue(record.getR15_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR15_c6() != null) {
						cell.setCellValue(record.getR15_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR15_c7() != null) {
						cell.setCellValue(record.getR15_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR15_c8() != null) {
						cell.setCellValue(record.getR15_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR15_total() != null) {
						cell.setCellValue(record.getR15_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					cell = row.getCell(1);
					if (record.getR16_usd() != null) {
						cell.setCellValue(record.getR16_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR16_zar() != null) {
						cell.setCellValue(record.getR16_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR16_gbp() != null) {
						cell.setCellValue(record.getR16_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR16_euro() != null) {
						cell.setCellValue(record.getR16_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR16_yen() != null) {
						cell.setCellValue(record.getR16_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR16_c6() != null) {
						cell.setCellValue(record.getR16_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR16_c7() != null) {
						cell.setCellValue(record.getR16_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR16_c8() != null) {
						cell.setCellValue(record.getR16_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR16_total() != null) {
						cell.setCellValue(record.getR16_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row17
					row = sheet.getRow(16);

					cell = row.getCell(1);
					if (record.getR17_usd() != null) {
						cell.setCellValue(record.getR17_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR17_zar() != null) {
						cell.setCellValue(record.getR17_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR17_gbp() != null) {
						cell.setCellValue(record.getR17_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR17_euro() != null) {
						cell.setCellValue(record.getR17_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR17_yen() != null) {
						cell.setCellValue(record.getR17_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR17_c6() != null) {
						cell.setCellValue(record.getR17_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR17_c7() != null) {
						cell.setCellValue(record.getR17_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR17_c8() != null) {
						cell.setCellValue(record.getR17_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR17_total() != null) {
						cell.setCellValue(record.getR17_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					cell = row.getCell(1);
					if (record.getR18_usd() != null) {
						cell.setCellValue(record.getR18_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR18_zar() != null) {
						cell.setCellValue(record.getR18_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR18_gbp() != null) {
						cell.setCellValue(record.getR18_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR18_euro() != null) {
						cell.setCellValue(record.getR18_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR18_yen() != null) {
						cell.setCellValue(record.getR18_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR18_c6() != null) {
						cell.setCellValue(record.getR18_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR18_c7() != null) {
						cell.setCellValue(record.getR18_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR18_c8() != null) {
						cell.setCellValue(record.getR18_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR18_total() != null) {
						cell.setCellValue(record.getR18_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row19
					row = sheet.getRow(18);

					cell = row.getCell(1);
					if (record.getR19_usd() != null) {
						cell.setCellValue(record.getR19_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR19_zar() != null) {
						cell.setCellValue(record.getR19_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR19_gbp() != null) {
						cell.setCellValue(record.getR19_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR19_euro() != null) {
						cell.setCellValue(record.getR19_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR19_yen() != null) {
						cell.setCellValue(record.getR19_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR19_c6() != null) {
						cell.setCellValue(record.getR19_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR19_c7() != null) {
						cell.setCellValue(record.getR19_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR19_c8() != null) {
						cell.setCellValue(record.getR19_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR19_total() != null) {
						cell.setCellValue(record.getR19_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					cell = row.getCell(1);
					if (record.getR20_usd() != null) {
						cell.setCellValue(record.getR20_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR20_zar() != null) {
						cell.setCellValue(record.getR20_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR20_gbp() != null) {
						cell.setCellValue(record.getR20_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR20_euro() != null) {
						cell.setCellValue(record.getR20_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR20_yen() != null) {
						cell.setCellValue(record.getR20_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR20_c6() != null) {
						cell.setCellValue(record.getR20_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR20_c7() != null) {
						cell.setCellValue(record.getR20_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR20_c8() != null) {
						cell.setCellValue(record.getR20_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR20_total() != null) {
						cell.setCellValue(record.getR20_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row21
					row = sheet.getRow(20);

					cell = row.getCell(1);
					if (record.getR21_usd() != null) {
						cell.setCellValue(record.getR21_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR21_zar() != null) {
						cell.setCellValue(record.getR21_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR21_gbp() != null) {
						cell.setCellValue(record.getR21_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR21_euro() != null) {
						cell.setCellValue(record.getR21_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR21_yen() != null) {
						cell.setCellValue(record.getR21_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR21_c6() != null) {
						cell.setCellValue(record.getR21_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR21_c7() != null) {
						cell.setCellValue(record.getR21_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR21_c8() != null) {
						cell.setCellValue(record.getR21_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR21_total() != null) {
						cell.setCellValue(record.getR21_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					cell = row.getCell(1);
					if (record.getR22_usd() != null) {
						cell.setCellValue(record.getR22_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR22_zar() != null) {
						cell.setCellValue(record.getR22_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR22_gbp() != null) {
						cell.setCellValue(record.getR22_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR22_euro() != null) {
						cell.setCellValue(record.getR22_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR22_yen() != null) {
						cell.setCellValue(record.getR22_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR22_c6() != null) {
						cell.setCellValue(record.getR22_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR22_c7() != null) {
						cell.setCellValue(record.getR22_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR22_c8() != null) {
						cell.setCellValue(record.getR22_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR22_total() != null) {
						cell.setCellValue(record.getR22_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row23
					row = sheet.getRow(22);

					cell = row.getCell(1);
					if (record.getR23_usd() != null) {
						cell.setCellValue(record.getR23_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR23_zar() != null) {
						cell.setCellValue(record.getR23_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR23_gbp() != null) {
						cell.setCellValue(record.getR23_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR23_euro() != null) {
						cell.setCellValue(record.getR23_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR23_yen() != null) {
						cell.setCellValue(record.getR23_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR23_c6() != null) {
						cell.setCellValue(record.getR23_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR23_c7() != null) {
						cell.setCellValue(record.getR23_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR23_c8() != null) {
						cell.setCellValue(record.getR23_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR23_total() != null) {
						cell.setCellValue(record.getR23_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					cell = row.getCell(1);
					if (record.getR24_usd() != null) {
						cell.setCellValue(record.getR24_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR24_zar() != null) {
						cell.setCellValue(record.getR24_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR24_gbp() != null) {
						cell.setCellValue(record.getR24_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR24_euro() != null) {
						cell.setCellValue(record.getR24_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR24_yen() != null) {
						cell.setCellValue(record.getR24_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR24_c6() != null) {
						cell.setCellValue(record.getR24_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR24_c7() != null) {
						cell.setCellValue(record.getR24_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR24_c8() != null) {
						cell.setCellValue(record.getR24_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR24_total() != null) {
						cell.setCellValue(record.getR24_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row25
					row = sheet.getRow(24);

					cell = row.getCell(1);
					if (record.getR25_usd() != null) {
						cell.setCellValue(record.getR25_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR25_zar() != null) {
						cell.setCellValue(record.getR25_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR25_gbp() != null) {
						cell.setCellValue(record.getR25_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR25_euro() != null) {
						cell.setCellValue(record.getR25_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR25_yen() != null) {
						cell.setCellValue(record.getR25_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR25_c6() != null) {
						cell.setCellValue(record.getR25_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR25_c7() != null) {
						cell.setCellValue(record.getR25_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR25_c8() != null) {
						cell.setCellValue(record.getR25_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR25_total() != null) {
						cell.setCellValue(record.getR25_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					cell = row.getCell(1);
					if (record.getR26_usd() != null) {
						cell.setCellValue(record.getR26_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR26_zar() != null) {
						cell.setCellValue(record.getR26_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR26_gbp() != null) {
						cell.setCellValue(record.getR26_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR26_euro() != null) {
						cell.setCellValue(record.getR26_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR26_yen() != null) {
						cell.setCellValue(record.getR26_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR26_c6() != null) {
						cell.setCellValue(record.getR26_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR26_c7() != null) {
						cell.setCellValue(record.getR26_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR26_c8() != null) {
						cell.setCellValue(record.getR26_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR26_total() != null) {
						cell.setCellValue(record.getR26_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					cell = row.getCell(1);
					if (record.getR27_usd() != null) {
						cell.setCellValue(record.getR27_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR27_zar() != null) {
						cell.setCellValue(record.getR27_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR27_gbp() != null) {
						cell.setCellValue(record.getR27_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR27_euro() != null) {
						cell.setCellValue(record.getR27_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR27_yen() != null) {
						cell.setCellValue(record.getR27_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR27_c6() != null) {
						cell.setCellValue(record.getR27_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR27_c7() != null) {
						cell.setCellValue(record.getR27_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR27_c8() != null) {
						cell.setCellValue(record.getR27_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR27_total() != null) {
						cell.setCellValue(record.getR27_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);

					cell = row.getCell(1);
					if (record.getR28_usd() != null) {
						cell.setCellValue(record.getR28_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR28_zar() != null) {
						cell.setCellValue(record.getR28_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR28_gbp() != null) {
						cell.setCellValue(record.getR28_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR28_euro() != null) {
						cell.setCellValue(record.getR28_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR28_yen() != null) {
						cell.setCellValue(record.getR28_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR28_c6() != null) {
						cell.setCellValue(record.getR28_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR28_c7() != null) {
						cell.setCellValue(record.getR28_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR28_c8() != null) {
						cell.setCellValue(record.getR28_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR28_total() != null) {
						cell.setCellValue(record.getR28_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cell = row.getCell(1);
					if (record.getR29_usd() != null) {
						cell.setCellValue(record.getR29_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR29_zar() != null) {
						cell.setCellValue(record.getR29_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR29_gbp() != null) {
						cell.setCellValue(record.getR29_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR29_euro() != null) {
						cell.setCellValue(record.getR29_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR29_yen() != null) {
						cell.setCellValue(record.getR29_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR29_c6() != null) {
						cell.setCellValue(record.getR29_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR29_c7() != null) {
						cell.setCellValue(record.getR29_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR29_c8() != null) {
						cell.setCellValue(record.getR29_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR29_total() != null) {
						cell.setCellValue(record.getR29_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cell = row.getCell(1);
					if (record.getR30_usd() != null) {
						cell.setCellValue(record.getR30_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR30_zar() != null) {
						cell.setCellValue(record.getR30_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR30_gbp() != null) {
						cell.setCellValue(record.getR30_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR30_euro() != null) {
						cell.setCellValue(record.getR30_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR30_yen() != null) {
						cell.setCellValue(record.getR30_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR30_c6() != null) {
						cell.setCellValue(record.getR30_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR30_c7() != null) {
						cell.setCellValue(record.getR30_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR30_c8() != null) {
						cell.setCellValue(record.getR30_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR30_total() != null) {
						cell.setCellValue(record.getR30_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					cell = row.getCell(1);
					if (record.getR31_usd() != null) {
						cell.setCellValue(record.getR31_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR31_zar() != null) {
						cell.setCellValue(record.getR31_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR31_gbp() != null) {
						cell.setCellValue(record.getR31_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR31_euro() != null) {
						cell.setCellValue(record.getR31_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR31_yen() != null) {
						cell.setCellValue(record.getR31_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR31_c6() != null) {
						cell.setCellValue(record.getR31_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR31_c7() != null) {
						cell.setCellValue(record.getR31_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR31_c8() != null) {
						cell.setCellValue(record.getR31_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR31_total() != null) {
						cell.setCellValue(record.getR31_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					cell = row.getCell(1);
					if (record.getR32_usd() != null) {
						cell.setCellValue(record.getR32_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR32_zar() != null) {
						cell.setCellValue(record.getR32_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR32_gbp() != null) {
						cell.setCellValue(record.getR32_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR32_euro() != null) {
						cell.setCellValue(record.getR32_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR32_yen() != null) {
						cell.setCellValue(record.getR32_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR32_c6() != null) {
						cell.setCellValue(record.getR32_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR32_c7() != null) {
						cell.setCellValue(record.getR32_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR32_c8() != null) {
						cell.setCellValue(record.getR32_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR32_total() != null) {
						cell.setCellValue(record.getR32_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					cell = row.getCell(1);
					if (record.getR33_usd() != null) {
						cell.setCellValue(record.getR33_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR33_zar() != null) {
						cell.setCellValue(record.getR33_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR33_gbp() != null) {
						cell.setCellValue(record.getR33_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR33_euro() != null) {
						cell.setCellValue(record.getR33_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR33_yen() != null) {
						cell.setCellValue(record.getR33_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR33_c6() != null) {
						cell.setCellValue(record.getR33_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR33_c7() != null) {
						cell.setCellValue(record.getR33_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR33_c8() != null) {
						cell.setCellValue(record.getR33_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR33_total() != null) {
						cell.setCellValue(record.getR33_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					cell = row.getCell(1);
					if (record.getR34_usd() != null) {
						cell.setCellValue(record.getR34_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR34_zar() != null) {
						cell.setCellValue(record.getR34_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR34_gbp() != null) {
						cell.setCellValue(record.getR34_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR34_euro() != null) {
						cell.setCellValue(record.getR34_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR34_yen() != null) {
						cell.setCellValue(record.getR34_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR34_c6() != null) {
						cell.setCellValue(record.getR34_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR34_c7() != null) {
						cell.setCellValue(record.getR34_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR34_c8() != null) {
						cell.setCellValue(record.getR34_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR34_total() != null) {
						cell.setCellValue(record.getR34_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					cell = row.getCell(1);
					if (record.getR35_usd() != null) {
						cell.setCellValue(record.getR35_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR35_zar() != null) {
						cell.setCellValue(record.getR35_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR35_gbp() != null) {
						cell.setCellValue(record.getR35_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR35_euro() != null) {
						cell.setCellValue(record.getR35_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR35_yen() != null) {
						cell.setCellValue(record.getR35_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR35_c6() != null) {
						cell.setCellValue(record.getR35_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR35_c7() != null) {
						cell.setCellValue(record.getR35_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR35_c8() != null) {
						cell.setCellValue(record.getR35_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR35_total() != null) {
						cell.setCellValue(record.getR35_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					cell = row.getCell(1);
					if (record.getR36_usd() != null) {
						cell.setCellValue(record.getR36_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR36_zar() != null) {
						cell.setCellValue(record.getR36_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR36_gbp() != null) {
						cell.setCellValue(record.getR36_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR36_euro() != null) {
						cell.setCellValue(record.getR36_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR36_yen() != null) {
						cell.setCellValue(record.getR36_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR36_c6() != null) {
						cell.setCellValue(record.getR36_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR36_c7() != null) {
						cell.setCellValue(record.getR36_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR36_c8() != null) {
						cell.setCellValue(record.getR36_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR36_total() != null) {
						cell.setCellValue(record.getR36_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);

					cell = row.getCell(1);
					if (record.getR37_usd() != null) {
						cell.setCellValue(record.getR37_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR37_zar() != null) {
						cell.setCellValue(record.getR37_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR37_gbp() != null) {
						cell.setCellValue(record.getR37_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR37_euro() != null) {
						cell.setCellValue(record.getR37_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR37_yen() != null) {
						cell.setCellValue(record.getR37_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR37_c6() != null) {
						cell.setCellValue(record.getR37_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR37_c7() != null) {
						cell.setCellValue(record.getR37_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR37_c8() != null) {
						cell.setCellValue(record.getR37_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR37_total() != null) {
						cell.setCellValue(record.getR37_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);

					cell = row.getCell(1);
					if (record.getR38_usd() != null) {
						cell.setCellValue(record.getR38_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR38_zar() != null) {
						cell.setCellValue(record.getR38_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR38_gbp() != null) {
						cell.setCellValue(record.getR38_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR38_euro() != null) {
						cell.setCellValue(record.getR38_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR38_yen() != null) {
						cell.setCellValue(record.getR38_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR38_c6() != null) {
						cell.setCellValue(record.getR38_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR38_c7() != null) {
						cell.setCellValue(record.getR38_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR38_c8() != null) {
						cell.setCellValue(record.getR38_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR38_total() != null) {
						cell.setCellValue(record.getR38_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);

					cell = row.getCell(1);
					if (record.getR39_usd() != null) {
						cell.setCellValue(record.getR39_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR39_zar() != null) {
						cell.setCellValue(record.getR39_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR39_gbp() != null) {
						cell.setCellValue(record.getR39_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR39_euro() != null) {
						cell.setCellValue(record.getR39_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR39_yen() != null) {
						cell.setCellValue(record.getR39_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR39_c6() != null) {
						cell.setCellValue(record.getR39_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR39_c7() != null) {
						cell.setCellValue(record.getR39_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR39_c8() != null) {
						cell.setCellValue(record.getR39_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR39_total() != null) {
						cell.setCellValue(record.getR39_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row40
					row = sheet.getRow(39);

					cell = row.getCell(1);
					if (record.getR40_usd() != null) {
						cell.setCellValue(record.getR40_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR40_zar() != null) {
						cell.setCellValue(record.getR40_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR40_gbp() != null) {
						cell.setCellValue(record.getR40_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR40_euro() != null) {
						cell.setCellValue(record.getR40_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR40_yen() != null) {
						cell.setCellValue(record.getR40_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR40_c6() != null) {
						cell.setCellValue(record.getR40_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR40_c7() != null) {
						cell.setCellValue(record.getR40_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR40_c8() != null) {
						cell.setCellValue(record.getR40_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR40_total() != null) {
						cell.setCellValue(record.getR40_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);

					cell = row.getCell(1);
					if (record.getR41_usd() != null) {
						cell.setCellValue(record.getR41_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR41_zar() != null) {
						cell.setCellValue(record.getR41_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR41_gbp() != null) {
						cell.setCellValue(record.getR41_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR41_euro() != null) {
						cell.setCellValue(record.getR41_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR41_yen() != null) {
						cell.setCellValue(record.getR41_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR41_c6() != null) {
						cell.setCellValue(record.getR41_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR41_c7() != null) {
						cell.setCellValue(record.getR41_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR41_c8() != null) {
						cell.setCellValue(record.getR41_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR41_total() != null) {
						cell.setCellValue(record.getR41_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					cell = row.getCell(1);
					if (record.getR42_usd() != null) {
						cell.setCellValue(record.getR42_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR42_zar() != null) {
						cell.setCellValue(record.getR42_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR42_gbp() != null) {
						cell.setCellValue(record.getR42_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR42_euro() != null) {
						cell.setCellValue(record.getR42_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR42_yen() != null) {
						cell.setCellValue(record.getR42_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR42_c6() != null) {
						cell.setCellValue(record.getR42_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR42_c7() != null) {
						cell.setCellValue(record.getR42_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR42_c8() != null) {
						cell.setCellValue(record.getR42_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR42_total() != null) {
						cell.setCellValue(record.getR42_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					} // row43
					row = sheet.getRow(42);

					cell = row.getCell(1);
					if (record.getR43_usd() != null) {
						cell.setCellValue(record.getR43_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR43_zar() != null) {
						cell.setCellValue(record.getR43_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR43_gbp() != null) {
						cell.setCellValue(record.getR43_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR43_euro() != null) {
						cell.setCellValue(record.getR43_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR43_yen() != null) {
						cell.setCellValue(record.getR43_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR43_c6() != null) {
						cell.setCellValue(record.getR43_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR43_c7() != null) {
						cell.setCellValue(record.getR43_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR43_c8() != null) {
						cell.setCellValue(record.getR43_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR43_total() != null) {
						cell.setCellValue(record.getR43_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row44
					row = sheet.getRow(43);

					cell = row.getCell(1);
					if (record.getR44_usd() != null) {
						cell.setCellValue(record.getR44_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR44_zar() != null) {
						cell.setCellValue(record.getR44_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR44_gbp() != null) {
						cell.setCellValue(record.getR44_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR44_euro() != null) {
						cell.setCellValue(record.getR44_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR44_yen() != null) {
						cell.setCellValue(record.getR44_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR44_c6() != null) {
						cell.setCellValue(record.getR44_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR44_c7() != null) {
						cell.setCellValue(record.getR44_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR44_c8() != null) {
						cell.setCellValue(record.getR44_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR44_total() != null) {
						cell.setCellValue(record.getR44_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);

					cell = row.getCell(1);
					if (record.getR45_usd() != null) {
						cell.setCellValue(record.getR45_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR45_zar() != null) {
						cell.setCellValue(record.getR45_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR45_gbp() != null) {
						cell.setCellValue(record.getR45_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR45_euro() != null) {
						cell.setCellValue(record.getR45_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR45_yen() != null) {
						cell.setCellValue(record.getR45_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR45_c6() != null) {
						cell.setCellValue(record.getR45_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR45_c7() != null) {
						cell.setCellValue(record.getR45_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR45_c8() != null) {
						cell.setCellValue(record.getR45_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR45_total() != null) {
						cell.setCellValue(record.getR45_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row46
					row = sheet.getRow(45);

					cell = row.getCell(1);
					if (record.getR46_usd() != null) {
						cell.setCellValue(record.getR46_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR46_zar() != null) {
						cell.setCellValue(record.getR46_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR46_gbp() != null) {
						cell.setCellValue(record.getR46_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR46_euro() != null) {
						cell.setCellValue(record.getR46_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR46_yen() != null) {
						cell.setCellValue(record.getR46_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR46_c6() != null) {
						cell.setCellValue(record.getR46_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR46_c7() != null) {
						cell.setCellValue(record.getR46_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR46_c8() != null) {
						cell.setCellValue(record.getR46_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR46_total() != null) {
						cell.setCellValue(record.getR46_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					cell = row.getCell(1);
					if (record.getR47_usd() != null) {
						cell.setCellValue(record.getR47_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR47_zar() != null) {
						cell.setCellValue(record.getR47_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR47_gbp() != null) {
						cell.setCellValue(record.getR47_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR47_euro() != null) {
						cell.setCellValue(record.getR47_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR47_yen() != null) {
						cell.setCellValue(record.getR47_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR47_c6() != null) {
						cell.setCellValue(record.getR47_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR47_c7() != null) {
						cell.setCellValue(record.getR47_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR47_c8() != null) {
						cell.setCellValue(record.getR47_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR47_total() != null) {
						cell.setCellValue(record.getR47_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row48
					row = sheet.getRow(47);

					cell = row.getCell(1);
					if (record.getR48_usd() != null) {
						cell.setCellValue(record.getR48_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR48_zar() != null) {
						cell.setCellValue(record.getR48_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR48_gbp() != null) {
						cell.setCellValue(record.getR48_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR48_euro() != null) {
						cell.setCellValue(record.getR48_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR48_yen() != null) {
						cell.setCellValue(record.getR48_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR48_c6() != null) {
						cell.setCellValue(record.getR48_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR48_c7() != null) {
						cell.setCellValue(record.getR48_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR48_c8() != null) {
						cell.setCellValue(record.getR48_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR48_total() != null) {
						cell.setCellValue(record.getR48_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row49
					row = sheet.getRow(48);

					cell = row.getCell(1);
					if (record.getR49_usd() != null) {
						cell.setCellValue(record.getR49_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR49_zar() != null) {
						cell.setCellValue(record.getR49_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR49_gbp() != null) {
						cell.setCellValue(record.getR49_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR49_euro() != null) {
						cell.setCellValue(record.getR49_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR49_yen() != null) {
						cell.setCellValue(record.getR49_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR49_c6() != null) {
						cell.setCellValue(record.getR49_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR49_c7() != null) {
						cell.setCellValue(record.getR49_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR49_c8() != null) {
						cell.setCellValue(record.getR49_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR49_total() != null) {
						cell.setCellValue(record.getR49_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);

					cell = row.getCell(1);
					if (record.getR50_usd() != null) {
						cell.setCellValue(record.getR50_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR50_zar() != null) {
						cell.setCellValue(record.getR50_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR50_gbp() != null) {
						cell.setCellValue(record.getR50_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR50_euro() != null) {
						cell.setCellValue(record.getR50_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR50_yen() != null) {
						cell.setCellValue(record.getR50_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR50_c6() != null) {
						cell.setCellValue(record.getR50_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR50_c7() != null) {
						cell.setCellValue(record.getR50_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR50_c8() != null) {
						cell.setCellValue(record.getR50_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR50_total() != null) {
						cell.setCellValue(record.getR50_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					cell = row.getCell(1);
					if (record.getR51_usd() != null) {
						cell.setCellValue(record.getR51_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR51_zar() != null) {
						cell.setCellValue(record.getR51_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR51_gbp() != null) {
						cell.setCellValue(record.getR51_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR51_euro() != null) {
						cell.setCellValue(record.getR51_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR51_yen() != null) {
						cell.setCellValue(record.getR51_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR51_c6() != null) {
						cell.setCellValue(record.getR51_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR51_c7() != null) {
						cell.setCellValue(record.getR51_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR51_c8() != null) {
						cell.setCellValue(record.getR51_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR51_total() != null) {
						cell.setCellValue(record.getR51_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);

					cell = row.getCell(1);
					if (record.getR52_usd() != null) {
						cell.setCellValue(record.getR52_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR52_zar() != null) {
						cell.setCellValue(record.getR52_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR52_gbp() != null) {
						cell.setCellValue(record.getR52_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR52_euro() != null) {
						cell.setCellValue(record.getR52_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR52_yen() != null) {
						cell.setCellValue(record.getR52_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR52_c6() != null) {
						cell.setCellValue(record.getR52_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR52_c7() != null) {
						cell.setCellValue(record.getR52_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR52_c8() != null) {
						cell.setCellValue(record.getR52_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR52_total() != null) {
						cell.setCellValue(record.getR52_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);

					cell = row.getCell(1);
					if (record.getR53_usd() != null) {
						cell.setCellValue(record.getR53_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR53_zar() != null) {
						cell.setCellValue(record.getR53_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR53_gbp() != null) {
						cell.setCellValue(record.getR53_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR53_euro() != null) {
						cell.setCellValue(record.getR53_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR53_yen() != null) {
						cell.setCellValue(record.getR53_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR53_c6() != null) {
						cell.setCellValue(record.getR53_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR53_c7() != null) {
						cell.setCellValue(record.getR53_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR53_c8() != null) {
						cell.setCellValue(record.getR53_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR53_total() != null) {
						cell.setCellValue(record.getR53_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);

					cell = row.getCell(1);
					if (record.getR54_usd() != null) {
						cell.setCellValue(record.getR54_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR54_zar() != null) {
						cell.setCellValue(record.getR54_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR54_gbp() != null) {
						cell.setCellValue(record.getR54_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR54_euro() != null) {
						cell.setCellValue(record.getR54_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR54_yen() != null) {
						cell.setCellValue(record.getR54_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR54_c6() != null) {
						cell.setCellValue(record.getR54_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR54_c7() != null) {
						cell.setCellValue(record.getR54_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR54_c8() != null) {
						cell.setCellValue(record.getR54_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR54_total() != null) {
						cell.setCellValue(record.getR54_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					cell = row.getCell(1);
					if (record.getR55_usd() != null) {
						cell.setCellValue(record.getR55_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR55_zar() != null) {
						cell.setCellValue(record.getR55_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR55_gbp() != null) {
						cell.setCellValue(record.getR55_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR55_euro() != null) {
						cell.setCellValue(record.getR55_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR55_yen() != null) {
						cell.setCellValue(record.getR55_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR55_c6() != null) {
						cell.setCellValue(record.getR55_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR55_c7() != null) {
						cell.setCellValue(record.getR55_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR55_c8() != null) {
						cell.setCellValue(record.getR55_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR55_total() != null) {
						cell.setCellValue(record.getR55_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);

					cell = row.getCell(1);
					if (record.getR56_usd() != null) {
						cell.setCellValue(record.getR56_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR56_zar() != null) {
						cell.setCellValue(record.getR56_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR56_gbp() != null) {
						cell.setCellValue(record.getR56_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR56_euro() != null) {
						cell.setCellValue(record.getR56_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR56_yen() != null) {
						cell.setCellValue(record.getR56_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR56_c6() != null) {
						cell.setCellValue(record.getR56_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR56_c7() != null) {
						cell.setCellValue(record.getR56_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR56_c8() != null) {
						cell.setCellValue(record.getR56_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR56_total() != null) {
						cell.setCellValue(record.getR56_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row57
					row = sheet.getRow(56);

					cell = row.getCell(1);
					if (record.getR57_usd() != null) {
						cell.setCellValue(record.getR57_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR57_zar() != null) {
						cell.setCellValue(record.getR57_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR57_gbp() != null) {
						cell.setCellValue(record.getR57_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR57_euro() != null) {
						cell.setCellValue(record.getR57_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR57_yen() != null) {
						cell.setCellValue(record.getR57_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR57_c6() != null) {
						cell.setCellValue(record.getR57_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR57_c7() != null) {
						cell.setCellValue(record.getR57_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR57_c8() != null) {
						cell.setCellValue(record.getR57_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR57_total() != null) {
						cell.setCellValue(record.getR57_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row58
					row = sheet.getRow(57);

					cell = row.getCell(1);
					if (record.getR58_usd() != null) {
						cell.setCellValue(record.getR58_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR58_zar() != null) {
						cell.setCellValue(record.getR58_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR58_gbp() != null) {
						cell.setCellValue(record.getR58_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR58_euro() != null) {
						cell.setCellValue(record.getR58_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR58_yen() != null) {
						cell.setCellValue(record.getR58_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR58_c6() != null) {
						cell.setCellValue(record.getR58_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR58_c7() != null) {
						cell.setCellValue(record.getR58_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR58_c8() != null) {
						cell.setCellValue(record.getR58_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR58_total() != null) {
						cell.setCellValue(record.getR58_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					cell = row.getCell(1);
					if (record.getR59_usd() != null) {
						cell.setCellValue(record.getR59_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR59_zar() != null) {
						cell.setCellValue(record.getR59_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR59_gbp() != null) {
						cell.setCellValue(record.getR59_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR59_euro() != null) {
						cell.setCellValue(record.getR59_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR59_yen() != null) {
						cell.setCellValue(record.getR59_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR59_c6() != null) {
						cell.setCellValue(record.getR59_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR59_c7() != null) {
						cell.setCellValue(record.getR59_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR59_c8() != null) {
						cell.setCellValue(record.getR59_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR59_total() != null) {
						cell.setCellValue(record.getR59_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					cell = row.getCell(1);
					if (record.getR60_usd() != null) {
						cell.setCellValue(record.getR60_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR60_zar() != null) {
						cell.setCellValue(record.getR60_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR60_gbp() != null) {
						cell.setCellValue(record.getR60_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR60_euro() != null) {
						cell.setCellValue(record.getR60_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR60_yen() != null) {
						cell.setCellValue(record.getR60_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR60_c6() != null) {
						cell.setCellValue(record.getR60_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR60_c7() != null) {
						cell.setCellValue(record.getR60_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR60_c8() != null) {
						cell.setCellValue(record.getR60_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR60_total() != null) {
						cell.setCellValue(record.getR60_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);

					cell = row.getCell(1);
					if (record.getR61_usd() != null) {
						cell.setCellValue(record.getR61_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR61_zar() != null) {
						cell.setCellValue(record.getR61_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR61_gbp() != null) {
						cell.setCellValue(record.getR61_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR61_euro() != null) {
						cell.setCellValue(record.getR61_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR61_yen() != null) {
						cell.setCellValue(record.getR61_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR61_c6() != null) {
						cell.setCellValue(record.getR61_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR61_c7() != null) {
						cell.setCellValue(record.getR61_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR61_c8() != null) {
						cell.setCellValue(record.getR61_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR61_total() != null) {
						cell.setCellValue(record.getR61_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);

					cell = row.getCell(1);
					if (record.getR62_usd() != null) {
						cell.setCellValue(record.getR62_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR62_zar() != null) {
						cell.setCellValue(record.getR62_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR62_gbp() != null) {
						cell.setCellValue(record.getR62_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR62_euro() != null) {
						cell.setCellValue(record.getR62_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR62_yen() != null) {
						cell.setCellValue(record.getR62_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR62_c6() != null) {
						cell.setCellValue(record.getR62_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR62_c7() != null) {
						cell.setCellValue(record.getR62_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR62_c8() != null) {
						cell.setCellValue(record.getR62_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR62_total() != null) {
						cell.setCellValue(record.getR62_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
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

	public byte[] getExcelExcelRESUB(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		List<M_LA5_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_BRANCHNET report. Returning empty result.");
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
					M_LA5_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell R12Cell = row.createCell(4);

					if (record.getREPORT_DATE() != null) {

						R12Cell.setCellValue(record.getREPORT_DATE());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}

					row = sheet.getRow(5);
					// row6
					Cell cell = row.getCell(1);
					if (record.getR6_usd() != null) {
						cell.setCellValue(record.getR6_usd().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(2);
					if (record.getR6_zar() != null) {
						cell.setCellValue(record.getR6_zar().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(3);
					if (record.getR6_gbp() != null) {
						cell.setCellValue(record.getR6_gbp().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(4);
					if (record.getR6_euro() != null) {
						cell.setCellValue(record.getR6_euro().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(5);
					if (record.getR6_yen() != null) {
						cell.setCellValue(record.getR6_yen().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(6);
					if (record.getR6_c6() != null) {
						cell.setCellValue(record.getR6_c6().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(7);
					if (record.getR6_c7() != null) {
						cell.setCellValue(record.getR6_c7().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(8);
					if (record.getR6_c8() != null) {
						cell.setCellValue(record.getR6_c8().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(9);
					if (record.getR6_total() != null) {
						cell.setCellValue(record.getR6_total().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row7
					row = sheet.getRow(6);
					cell = row.getCell(1);
					if (record.getR7_usd() != null) {
						cell.setCellValue(record.getR7_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR7_zar() != null) {
						cell.setCellValue(record.getR7_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR7_gbp() != null) {
						cell.setCellValue(record.getR7_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR7_euro() != null) {
						cell.setCellValue(record.getR7_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR7_yen() != null) {
						cell.setCellValue(record.getR7_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR7_c6() != null) {
						cell.setCellValue(record.getR7_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR7_c7() != null) {
						cell.setCellValue(record.getR7_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR7_c8() != null) {
						cell.setCellValue(record.getR7_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR7_total() != null) {
						cell.setCellValue(record.getR7_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row8
					row = sheet.getRow(7);
					cell = row.getCell(1);
					if (record.getR8_usd() != null) {
						cell.setCellValue(record.getR8_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR8_zar() != null) {
						cell.setCellValue(record.getR8_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR8_gbp() != null) {
						cell.setCellValue(record.getR8_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR8_euro() != null) {
						cell.setCellValue(record.getR8_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR8_yen() != null) {
						cell.setCellValue(record.getR8_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR8_c6() != null) {
						cell.setCellValue(record.getR8_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR8_c7() != null) {
						cell.setCellValue(record.getR8_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR8_c8() != null) {
						cell.setCellValue(record.getR8_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR8_total() != null) {
						cell.setCellValue(record.getR8_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row9
					row = sheet.getRow(8);

					cell = row.getCell(1);
					if (record.getR9_usd() != null) {
						cell.setCellValue(record.getR9_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR9_zar() != null) {
						cell.setCellValue(record.getR9_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR9_gbp() != null) {
						cell.setCellValue(record.getR9_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR9_euro() != null) {
						cell.setCellValue(record.getR9_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR9_yen() != null) {
						cell.setCellValue(record.getR9_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR9_c6() != null) {
						cell.setCellValue(record.getR9_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR9_c7() != null) {
						cell.setCellValue(record.getR9_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR9_c8() != null) {
						cell.setCellValue(record.getR9_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR9_total() != null) {
						cell.setCellValue(record.getR9_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row10
					row = sheet.getRow(9);

					cell = row.getCell(1);
					if (record.getR10_usd() != null) {
						cell.setCellValue(record.getR10_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR10_zar() != null) {
						cell.setCellValue(record.getR10_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR10_gbp() != null) {
						cell.setCellValue(record.getR10_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR10_euro() != null) {
						cell.setCellValue(record.getR10_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR10_yen() != null) {
						cell.setCellValue(record.getR10_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR10_c6() != null) {
						cell.setCellValue(record.getR10_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR10_c7() != null) {
						cell.setCellValue(record.getR10_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR10_c8() != null) {
						cell.setCellValue(record.getR10_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR10_total() != null) {
						cell.setCellValue(record.getR10_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					cell = row.getCell(1);
					if (record.getR11_usd() != null) {
						cell.setCellValue(record.getR11_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR11_zar() != null) {
						cell.setCellValue(record.getR11_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR11_gbp() != null) {
						cell.setCellValue(record.getR11_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR11_euro() != null) {
						cell.setCellValue(record.getR11_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR11_yen() != null) {
						cell.setCellValue(record.getR11_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR11_c6() != null) {
						cell.setCellValue(record.getR11_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR11_c7() != null) {
						cell.setCellValue(record.getR11_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR11_c8() != null) {
						cell.setCellValue(record.getR11_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR11_total() != null) {
						cell.setCellValue(record.getR11_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell = row.getCell(1);
					if (record.getR12_usd() != null) {
						cell.setCellValue(record.getR12_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR12_zar() != null) {
						cell.setCellValue(record.getR12_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR12_gbp() != null) {
						cell.setCellValue(record.getR12_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR12_euro() != null) {
						cell.setCellValue(record.getR12_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR12_yen() != null) {
						cell.setCellValue(record.getR12_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR12_c6() != null) {
						cell.setCellValue(record.getR12_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR12_c7() != null) {
						cell.setCellValue(record.getR12_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR12_c8() != null) {
						cell.setCellValue(record.getR12_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR12_total() != null) {
						cell.setCellValue(record.getR12_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					cell = row.getCell(1);
					if (record.getR13_usd() != null) {
						cell.setCellValue(record.getR13_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR13_zar() != null) {
						cell.setCellValue(record.getR13_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR13_gbp() != null) {
						cell.setCellValue(record.getR13_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR13_euro() != null) {
						cell.setCellValue(record.getR13_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR13_yen() != null) {
						cell.setCellValue(record.getR13_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR13_c6() != null) {
						cell.setCellValue(record.getR13_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR13_c7() != null) {
						cell.setCellValue(record.getR13_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR13_c8() != null) {
						cell.setCellValue(record.getR13_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR13_total() != null) {
						cell.setCellValue(record.getR13_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					cell = row.getCell(1);
					if (record.getR14_usd() != null) {
						cell.setCellValue(record.getR14_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR14_zar() != null) {
						cell.setCellValue(record.getR14_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR14_gbp() != null) {
						cell.setCellValue(record.getR14_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR14_euro() != null) {
						cell.setCellValue(record.getR14_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR14_yen() != null) {
						cell.setCellValue(record.getR14_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR14_c6() != null) {
						cell.setCellValue(record.getR14_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR14_c7() != null) {
						cell.setCellValue(record.getR14_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR14_c8() != null) {
						cell.setCellValue(record.getR14_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR14_total() != null) {
						cell.setCellValue(record.getR14_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row15
					row = sheet.getRow(14);

					cell = row.getCell(1);
					if (record.getR15_usd() != null) {
						cell.setCellValue(record.getR15_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR15_zar() != null) {
						cell.setCellValue(record.getR15_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR15_gbp() != null) {
						cell.setCellValue(record.getR15_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR15_euro() != null) {
						cell.setCellValue(record.getR15_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR15_yen() != null) {
						cell.setCellValue(record.getR15_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR15_c6() != null) {
						cell.setCellValue(record.getR15_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR15_c7() != null) {
						cell.setCellValue(record.getR15_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR15_c8() != null) {
						cell.setCellValue(record.getR15_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR15_total() != null) {
						cell.setCellValue(record.getR15_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					cell = row.getCell(1);
					if (record.getR16_usd() != null) {
						cell.setCellValue(record.getR16_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR16_zar() != null) {
						cell.setCellValue(record.getR16_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR16_gbp() != null) {
						cell.setCellValue(record.getR16_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR16_euro() != null) {
						cell.setCellValue(record.getR16_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR16_yen() != null) {
						cell.setCellValue(record.getR16_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR16_c6() != null) {
						cell.setCellValue(record.getR16_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR16_c7() != null) {
						cell.setCellValue(record.getR16_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR16_c8() != null) {
						cell.setCellValue(record.getR16_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR16_total() != null) {
						cell.setCellValue(record.getR16_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row17
					row = sheet.getRow(16);

					cell = row.getCell(1);
					if (record.getR17_usd() != null) {
						cell.setCellValue(record.getR17_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR17_zar() != null) {
						cell.setCellValue(record.getR17_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR17_gbp() != null) {
						cell.setCellValue(record.getR17_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR17_euro() != null) {
						cell.setCellValue(record.getR17_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR17_yen() != null) {
						cell.setCellValue(record.getR17_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR17_c6() != null) {
						cell.setCellValue(record.getR17_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR17_c7() != null) {
						cell.setCellValue(record.getR17_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR17_c8() != null) {
						cell.setCellValue(record.getR17_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR17_total() != null) {
						cell.setCellValue(record.getR17_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					cell = row.getCell(1);
					if (record.getR18_usd() != null) {
						cell.setCellValue(record.getR18_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR18_zar() != null) {
						cell.setCellValue(record.getR18_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR18_gbp() != null) {
						cell.setCellValue(record.getR18_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR18_euro() != null) {
						cell.setCellValue(record.getR18_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR18_yen() != null) {
						cell.setCellValue(record.getR18_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR18_c6() != null) {
						cell.setCellValue(record.getR18_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR18_c7() != null) {
						cell.setCellValue(record.getR18_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR18_c8() != null) {
						cell.setCellValue(record.getR18_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR18_total() != null) {
						cell.setCellValue(record.getR18_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row19
					row = sheet.getRow(18);

					cell = row.getCell(1);
					if (record.getR19_usd() != null) {
						cell.setCellValue(record.getR19_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR19_zar() != null) {
						cell.setCellValue(record.getR19_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR19_gbp() != null) {
						cell.setCellValue(record.getR19_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR19_euro() != null) {
						cell.setCellValue(record.getR19_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR19_yen() != null) {
						cell.setCellValue(record.getR19_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR19_c6() != null) {
						cell.setCellValue(record.getR19_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR19_c7() != null) {
						cell.setCellValue(record.getR19_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR19_c8() != null) {
						cell.setCellValue(record.getR19_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR19_total() != null) {
						cell.setCellValue(record.getR19_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					cell = row.getCell(1);
					if (record.getR20_usd() != null) {
						cell.setCellValue(record.getR20_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR20_zar() != null) {
						cell.setCellValue(record.getR20_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR20_gbp() != null) {
						cell.setCellValue(record.getR20_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR20_euro() != null) {
						cell.setCellValue(record.getR20_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR20_yen() != null) {
						cell.setCellValue(record.getR20_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR20_c6() != null) {
						cell.setCellValue(record.getR20_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR20_c7() != null) {
						cell.setCellValue(record.getR20_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR20_c8() != null) {
						cell.setCellValue(record.getR20_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR20_total() != null) {
						cell.setCellValue(record.getR20_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row21
					row = sheet.getRow(20);

					cell = row.getCell(1);
					if (record.getR21_usd() != null) {
						cell.setCellValue(record.getR21_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR21_zar() != null) {
						cell.setCellValue(record.getR21_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR21_gbp() != null) {
						cell.setCellValue(record.getR21_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR21_euro() != null) {
						cell.setCellValue(record.getR21_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR21_yen() != null) {
						cell.setCellValue(record.getR21_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR21_c6() != null) {
						cell.setCellValue(record.getR21_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR21_c7() != null) {
						cell.setCellValue(record.getR21_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR21_c8() != null) {
						cell.setCellValue(record.getR21_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR21_total() != null) {
						cell.setCellValue(record.getR21_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					cell = row.getCell(1);
					if (record.getR22_usd() != null) {
						cell.setCellValue(record.getR22_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR22_zar() != null) {
						cell.setCellValue(record.getR22_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR22_gbp() != null) {
						cell.setCellValue(record.getR22_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR22_euro() != null) {
						cell.setCellValue(record.getR22_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR22_yen() != null) {
						cell.setCellValue(record.getR22_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR22_c6() != null) {
						cell.setCellValue(record.getR22_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR22_c7() != null) {
						cell.setCellValue(record.getR22_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR22_c8() != null) {
						cell.setCellValue(record.getR22_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR22_total() != null) {
						cell.setCellValue(record.getR22_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row23
					row = sheet.getRow(22);

					cell = row.getCell(1);
					if (record.getR23_usd() != null) {
						cell.setCellValue(record.getR23_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR23_zar() != null) {
						cell.setCellValue(record.getR23_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR23_gbp() != null) {
						cell.setCellValue(record.getR23_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR23_euro() != null) {
						cell.setCellValue(record.getR23_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR23_yen() != null) {
						cell.setCellValue(record.getR23_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR23_c6() != null) {
						cell.setCellValue(record.getR23_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR23_c7() != null) {
						cell.setCellValue(record.getR23_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR23_c8() != null) {
						cell.setCellValue(record.getR23_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR23_total() != null) {
						cell.setCellValue(record.getR23_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					cell = row.getCell(1);
					if (record.getR24_usd() != null) {
						cell.setCellValue(record.getR24_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR24_zar() != null) {
						cell.setCellValue(record.getR24_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR24_gbp() != null) {
						cell.setCellValue(record.getR24_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR24_euro() != null) {
						cell.setCellValue(record.getR24_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR24_yen() != null) {
						cell.setCellValue(record.getR24_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR24_c6() != null) {
						cell.setCellValue(record.getR24_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR24_c7() != null) {
						cell.setCellValue(record.getR24_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR24_c8() != null) {
						cell.setCellValue(record.getR24_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR24_total() != null) {
						cell.setCellValue(record.getR24_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row25
					row = sheet.getRow(24);

					cell = row.getCell(1);
					if (record.getR25_usd() != null) {
						cell.setCellValue(record.getR25_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR25_zar() != null) {
						cell.setCellValue(record.getR25_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR25_gbp() != null) {
						cell.setCellValue(record.getR25_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR25_euro() != null) {
						cell.setCellValue(record.getR25_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR25_yen() != null) {
						cell.setCellValue(record.getR25_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR25_c6() != null) {
						cell.setCellValue(record.getR25_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR25_c7() != null) {
						cell.setCellValue(record.getR25_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR25_c8() != null) {
						cell.setCellValue(record.getR25_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR25_total() != null) {
						cell.setCellValue(record.getR25_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					cell = row.getCell(1);
					if (record.getR26_usd() != null) {
						cell.setCellValue(record.getR26_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR26_zar() != null) {
						cell.setCellValue(record.getR26_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR26_gbp() != null) {
						cell.setCellValue(record.getR26_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR26_euro() != null) {
						cell.setCellValue(record.getR26_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR26_yen() != null) {
						cell.setCellValue(record.getR26_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR26_c6() != null) {
						cell.setCellValue(record.getR26_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR26_c7() != null) {
						cell.setCellValue(record.getR26_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR26_c8() != null) {
						cell.setCellValue(record.getR26_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR26_total() != null) {
						cell.setCellValue(record.getR26_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					cell = row.getCell(1);
					if (record.getR27_usd() != null) {
						cell.setCellValue(record.getR27_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR27_zar() != null) {
						cell.setCellValue(record.getR27_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR27_gbp() != null) {
						cell.setCellValue(record.getR27_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR27_euro() != null) {
						cell.setCellValue(record.getR27_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR27_yen() != null) {
						cell.setCellValue(record.getR27_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR27_c6() != null) {
						cell.setCellValue(record.getR27_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR27_c7() != null) {
						cell.setCellValue(record.getR27_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR27_c8() != null) {
						cell.setCellValue(record.getR27_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR27_total() != null) {
						cell.setCellValue(record.getR27_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);

					cell = row.getCell(1);
					if (record.getR28_usd() != null) {
						cell.setCellValue(record.getR28_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR28_zar() != null) {
						cell.setCellValue(record.getR28_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR28_gbp() != null) {
						cell.setCellValue(record.getR28_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR28_euro() != null) {
						cell.setCellValue(record.getR28_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR28_yen() != null) {
						cell.setCellValue(record.getR28_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR28_c6() != null) {
						cell.setCellValue(record.getR28_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR28_c7() != null) {
						cell.setCellValue(record.getR28_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR28_c8() != null) {
						cell.setCellValue(record.getR28_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR28_total() != null) {
						cell.setCellValue(record.getR28_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cell = row.getCell(1);
					if (record.getR29_usd() != null) {
						cell.setCellValue(record.getR29_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR29_zar() != null) {
						cell.setCellValue(record.getR29_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR29_gbp() != null) {
						cell.setCellValue(record.getR29_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR29_euro() != null) {
						cell.setCellValue(record.getR29_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR29_yen() != null) {
						cell.setCellValue(record.getR29_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR29_c6() != null) {
						cell.setCellValue(record.getR29_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR29_c7() != null) {
						cell.setCellValue(record.getR29_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR29_c8() != null) {
						cell.setCellValue(record.getR29_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR29_total() != null) {
						cell.setCellValue(record.getR29_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cell = row.getCell(1);
					if (record.getR30_usd() != null) {
						cell.setCellValue(record.getR30_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR30_zar() != null) {
						cell.setCellValue(record.getR30_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR30_gbp() != null) {
						cell.setCellValue(record.getR30_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR30_euro() != null) {
						cell.setCellValue(record.getR30_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR30_yen() != null) {
						cell.setCellValue(record.getR30_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR30_c6() != null) {
						cell.setCellValue(record.getR30_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR30_c7() != null) {
						cell.setCellValue(record.getR30_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR30_c8() != null) {
						cell.setCellValue(record.getR30_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR30_total() != null) {
						cell.setCellValue(record.getR30_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					cell = row.getCell(1);
					if (record.getR31_usd() != null) {
						cell.setCellValue(record.getR31_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR31_zar() != null) {
						cell.setCellValue(record.getR31_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR31_gbp() != null) {
						cell.setCellValue(record.getR31_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR31_euro() != null) {
						cell.setCellValue(record.getR31_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR31_yen() != null) {
						cell.setCellValue(record.getR31_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR31_c6() != null) {
						cell.setCellValue(record.getR31_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR31_c7() != null) {
						cell.setCellValue(record.getR31_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR31_c8() != null) {
						cell.setCellValue(record.getR31_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR31_total() != null) {
						cell.setCellValue(record.getR31_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					cell = row.getCell(1);
					if (record.getR32_usd() != null) {
						cell.setCellValue(record.getR32_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR32_zar() != null) {
						cell.setCellValue(record.getR32_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR32_gbp() != null) {
						cell.setCellValue(record.getR32_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR32_euro() != null) {
						cell.setCellValue(record.getR32_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR32_yen() != null) {
						cell.setCellValue(record.getR32_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR32_c6() != null) {
						cell.setCellValue(record.getR32_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR32_c7() != null) {
						cell.setCellValue(record.getR32_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR32_c8() != null) {
						cell.setCellValue(record.getR32_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR32_total() != null) {
						cell.setCellValue(record.getR32_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					cell = row.getCell(1);
					if (record.getR33_usd() != null) {
						cell.setCellValue(record.getR33_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR33_zar() != null) {
						cell.setCellValue(record.getR33_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR33_gbp() != null) {
						cell.setCellValue(record.getR33_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR33_euro() != null) {
						cell.setCellValue(record.getR33_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR33_yen() != null) {
						cell.setCellValue(record.getR33_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR33_c6() != null) {
						cell.setCellValue(record.getR33_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR33_c7() != null) {
						cell.setCellValue(record.getR33_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR33_c8() != null) {
						cell.setCellValue(record.getR33_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR33_total() != null) {
						cell.setCellValue(record.getR33_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					cell = row.getCell(1);
					if (record.getR34_usd() != null) {
						cell.setCellValue(record.getR34_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR34_zar() != null) {
						cell.setCellValue(record.getR34_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR34_gbp() != null) {
						cell.setCellValue(record.getR34_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR34_euro() != null) {
						cell.setCellValue(record.getR34_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR34_yen() != null) {
						cell.setCellValue(record.getR34_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR34_c6() != null) {
						cell.setCellValue(record.getR34_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR34_c7() != null) {
						cell.setCellValue(record.getR34_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR34_c8() != null) {
						cell.setCellValue(record.getR34_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR34_total() != null) {
						cell.setCellValue(record.getR34_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					cell = row.getCell(1);
					if (record.getR35_usd() != null) {
						cell.setCellValue(record.getR35_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR35_zar() != null) {
						cell.setCellValue(record.getR35_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR35_gbp() != null) {
						cell.setCellValue(record.getR35_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR35_euro() != null) {
						cell.setCellValue(record.getR35_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR35_yen() != null) {
						cell.setCellValue(record.getR35_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR35_c6() != null) {
						cell.setCellValue(record.getR35_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR35_c7() != null) {
						cell.setCellValue(record.getR35_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR35_c8() != null) {
						cell.setCellValue(record.getR35_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR35_total() != null) {
						cell.setCellValue(record.getR35_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					cell = row.getCell(1);
					if (record.getR36_usd() != null) {
						cell.setCellValue(record.getR36_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR36_zar() != null) {
						cell.setCellValue(record.getR36_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR36_gbp() != null) {
						cell.setCellValue(record.getR36_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR36_euro() != null) {
						cell.setCellValue(record.getR36_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR36_yen() != null) {
						cell.setCellValue(record.getR36_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR36_c6() != null) {
						cell.setCellValue(record.getR36_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR36_c7() != null) {
						cell.setCellValue(record.getR36_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR36_c8() != null) {
						cell.setCellValue(record.getR36_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR36_total() != null) {
						cell.setCellValue(record.getR36_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);

					cell = row.getCell(1);
					if (record.getR37_usd() != null) {
						cell.setCellValue(record.getR37_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR37_zar() != null) {
						cell.setCellValue(record.getR37_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR37_gbp() != null) {
						cell.setCellValue(record.getR37_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR37_euro() != null) {
						cell.setCellValue(record.getR37_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR37_yen() != null) {
						cell.setCellValue(record.getR37_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR37_c6() != null) {
						cell.setCellValue(record.getR37_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR37_c7() != null) {
						cell.setCellValue(record.getR37_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR37_c8() != null) {
						cell.setCellValue(record.getR37_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR37_total() != null) {
						cell.setCellValue(record.getR37_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);

					cell = row.getCell(1);
					if (record.getR38_usd() != null) {
						cell.setCellValue(record.getR38_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR38_zar() != null) {
						cell.setCellValue(record.getR38_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR38_gbp() != null) {
						cell.setCellValue(record.getR38_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR38_euro() != null) {
						cell.setCellValue(record.getR38_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR38_yen() != null) {
						cell.setCellValue(record.getR38_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR38_c6() != null) {
						cell.setCellValue(record.getR38_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR38_c7() != null) {
						cell.setCellValue(record.getR38_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR38_c8() != null) {
						cell.setCellValue(record.getR38_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR38_total() != null) {
						cell.setCellValue(record.getR38_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);

					cell = row.getCell(1);
					if (record.getR39_usd() != null) {
						cell.setCellValue(record.getR39_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR39_zar() != null) {
						cell.setCellValue(record.getR39_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR39_gbp() != null) {
						cell.setCellValue(record.getR39_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR39_euro() != null) {
						cell.setCellValue(record.getR39_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR39_yen() != null) {
						cell.setCellValue(record.getR39_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR39_c6() != null) {
						cell.setCellValue(record.getR39_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR39_c7() != null) {
						cell.setCellValue(record.getR39_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR39_c8() != null) {
						cell.setCellValue(record.getR39_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR39_total() != null) {
						cell.setCellValue(record.getR39_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row40
					row = sheet.getRow(39);

					cell = row.getCell(1);
					if (record.getR40_usd() != null) {
						cell.setCellValue(record.getR40_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR40_zar() != null) {
						cell.setCellValue(record.getR40_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR40_gbp() != null) {
						cell.setCellValue(record.getR40_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR40_euro() != null) {
						cell.setCellValue(record.getR40_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR40_yen() != null) {
						cell.setCellValue(record.getR40_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR40_c6() != null) {
						cell.setCellValue(record.getR40_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR40_c7() != null) {
						cell.setCellValue(record.getR40_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR40_c8() != null) {
						cell.setCellValue(record.getR40_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR40_total() != null) {
						cell.setCellValue(record.getR40_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);

					cell = row.getCell(1);
					if (record.getR41_usd() != null) {
						cell.setCellValue(record.getR41_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR41_zar() != null) {
						cell.setCellValue(record.getR41_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR41_gbp() != null) {
						cell.setCellValue(record.getR41_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR41_euro() != null) {
						cell.setCellValue(record.getR41_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR41_yen() != null) {
						cell.setCellValue(record.getR41_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR41_c6() != null) {
						cell.setCellValue(record.getR41_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR41_c7() != null) {
						cell.setCellValue(record.getR41_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR41_c8() != null) {
						cell.setCellValue(record.getR41_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR41_total() != null) {
						cell.setCellValue(record.getR41_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					cell = row.getCell(1);
					if (record.getR42_usd() != null) {
						cell.setCellValue(record.getR42_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR42_zar() != null) {
						cell.setCellValue(record.getR42_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR42_gbp() != null) {
						cell.setCellValue(record.getR42_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR42_euro() != null) {
						cell.setCellValue(record.getR42_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR42_yen() != null) {
						cell.setCellValue(record.getR42_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR42_c6() != null) {
						cell.setCellValue(record.getR42_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR42_c7() != null) {
						cell.setCellValue(record.getR42_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR42_c8() != null) {
						cell.setCellValue(record.getR42_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR42_total() != null) {
						cell.setCellValue(record.getR42_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					} // row43
					row = sheet.getRow(42);

					cell = row.getCell(1);
					if (record.getR43_usd() != null) {
						cell.setCellValue(record.getR43_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR43_zar() != null) {
						cell.setCellValue(record.getR43_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR43_gbp() != null) {
						cell.setCellValue(record.getR43_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR43_euro() != null) {
						cell.setCellValue(record.getR43_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR43_yen() != null) {
						cell.setCellValue(record.getR43_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR43_c6() != null) {
						cell.setCellValue(record.getR43_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR43_c7() != null) {
						cell.setCellValue(record.getR43_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR43_c8() != null) {
						cell.setCellValue(record.getR43_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR43_total() != null) {
						cell.setCellValue(record.getR43_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row44
					row = sheet.getRow(43);

					cell = row.getCell(1);
					if (record.getR44_usd() != null) {
						cell.setCellValue(record.getR44_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR44_zar() != null) {
						cell.setCellValue(record.getR44_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR44_gbp() != null) {
						cell.setCellValue(record.getR44_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR44_euro() != null) {
						cell.setCellValue(record.getR44_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR44_yen() != null) {
						cell.setCellValue(record.getR44_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR44_c6() != null) {
						cell.setCellValue(record.getR44_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR44_c7() != null) {
						cell.setCellValue(record.getR44_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR44_c8() != null) {
						cell.setCellValue(record.getR44_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR44_total() != null) {
						cell.setCellValue(record.getR44_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);

					cell = row.getCell(1);
					if (record.getR45_usd() != null) {
						cell.setCellValue(record.getR45_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR45_zar() != null) {
						cell.setCellValue(record.getR45_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR45_gbp() != null) {
						cell.setCellValue(record.getR45_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR45_euro() != null) {
						cell.setCellValue(record.getR45_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR45_yen() != null) {
						cell.setCellValue(record.getR45_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR45_c6() != null) {
						cell.setCellValue(record.getR45_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR45_c7() != null) {
						cell.setCellValue(record.getR45_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR45_c8() != null) {
						cell.setCellValue(record.getR45_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR45_total() != null) {
						cell.setCellValue(record.getR45_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row46
					row = sheet.getRow(45);

					cell = row.getCell(1);
					if (record.getR46_usd() != null) {
						cell.setCellValue(record.getR46_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR46_zar() != null) {
						cell.setCellValue(record.getR46_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR46_gbp() != null) {
						cell.setCellValue(record.getR46_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR46_euro() != null) {
						cell.setCellValue(record.getR46_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR46_yen() != null) {
						cell.setCellValue(record.getR46_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR46_c6() != null) {
						cell.setCellValue(record.getR46_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR46_c7() != null) {
						cell.setCellValue(record.getR46_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR46_c8() != null) {
						cell.setCellValue(record.getR46_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR46_total() != null) {
						cell.setCellValue(record.getR46_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					cell = row.getCell(1);
					if (record.getR47_usd() != null) {
						cell.setCellValue(record.getR47_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR47_zar() != null) {
						cell.setCellValue(record.getR47_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR47_gbp() != null) {
						cell.setCellValue(record.getR47_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR47_euro() != null) {
						cell.setCellValue(record.getR47_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR47_yen() != null) {
						cell.setCellValue(record.getR47_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR47_c6() != null) {
						cell.setCellValue(record.getR47_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR47_c7() != null) {
						cell.setCellValue(record.getR47_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR47_c8() != null) {
						cell.setCellValue(record.getR47_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR47_total() != null) {
						cell.setCellValue(record.getR47_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row48
					row = sheet.getRow(47);

					cell = row.getCell(1);
					if (record.getR48_usd() != null) {
						cell.setCellValue(record.getR48_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR48_zar() != null) {
						cell.setCellValue(record.getR48_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR48_gbp() != null) {
						cell.setCellValue(record.getR48_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR48_euro() != null) {
						cell.setCellValue(record.getR48_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR48_yen() != null) {
						cell.setCellValue(record.getR48_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR48_c6() != null) {
						cell.setCellValue(record.getR48_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR48_c7() != null) {
						cell.setCellValue(record.getR48_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR48_c8() != null) {
						cell.setCellValue(record.getR48_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR48_total() != null) {
						cell.setCellValue(record.getR48_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row49
					row = sheet.getRow(48);

					cell = row.getCell(1);
					if (record.getR49_usd() != null) {
						cell.setCellValue(record.getR49_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR49_zar() != null) {
						cell.setCellValue(record.getR49_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR49_gbp() != null) {
						cell.setCellValue(record.getR49_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR49_euro() != null) {
						cell.setCellValue(record.getR49_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR49_yen() != null) {
						cell.setCellValue(record.getR49_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR49_c6() != null) {
						cell.setCellValue(record.getR49_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR49_c7() != null) {
						cell.setCellValue(record.getR49_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR49_c8() != null) {
						cell.setCellValue(record.getR49_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR49_total() != null) {
						cell.setCellValue(record.getR49_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);

					cell = row.getCell(1);
					if (record.getR50_usd() != null) {
						cell.setCellValue(record.getR50_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR50_zar() != null) {
						cell.setCellValue(record.getR50_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR50_gbp() != null) {
						cell.setCellValue(record.getR50_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR50_euro() != null) {
						cell.setCellValue(record.getR50_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR50_yen() != null) {
						cell.setCellValue(record.getR50_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR50_c6() != null) {
						cell.setCellValue(record.getR50_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR50_c7() != null) {
						cell.setCellValue(record.getR50_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR50_c8() != null) {
						cell.setCellValue(record.getR50_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR50_total() != null) {
						cell.setCellValue(record.getR50_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					cell = row.getCell(1);
					if (record.getR51_usd() != null) {
						cell.setCellValue(record.getR51_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR51_zar() != null) {
						cell.setCellValue(record.getR51_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR51_gbp() != null) {
						cell.setCellValue(record.getR51_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR51_euro() != null) {
						cell.setCellValue(record.getR51_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR51_yen() != null) {
						cell.setCellValue(record.getR51_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR51_c6() != null) {
						cell.setCellValue(record.getR51_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR51_c7() != null) {
						cell.setCellValue(record.getR51_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR51_c8() != null) {
						cell.setCellValue(record.getR51_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR51_total() != null) {
						cell.setCellValue(record.getR51_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);

					cell = row.getCell(1);
					if (record.getR52_usd() != null) {
						cell.setCellValue(record.getR52_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR52_zar() != null) {
						cell.setCellValue(record.getR52_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR52_gbp() != null) {
						cell.setCellValue(record.getR52_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR52_euro() != null) {
						cell.setCellValue(record.getR52_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR52_yen() != null) {
						cell.setCellValue(record.getR52_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR52_c6() != null) {
						cell.setCellValue(record.getR52_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR52_c7() != null) {
						cell.setCellValue(record.getR52_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR52_c8() != null) {
						cell.setCellValue(record.getR52_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR52_total() != null) {
						cell.setCellValue(record.getR52_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);

					cell = row.getCell(1);
					if (record.getR53_usd() != null) {
						cell.setCellValue(record.getR53_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR53_zar() != null) {
						cell.setCellValue(record.getR53_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR53_gbp() != null) {
						cell.setCellValue(record.getR53_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR53_euro() != null) {
						cell.setCellValue(record.getR53_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR53_yen() != null) {
						cell.setCellValue(record.getR53_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR53_c6() != null) {
						cell.setCellValue(record.getR53_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR53_c7() != null) {
						cell.setCellValue(record.getR53_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR53_c8() != null) {
						cell.setCellValue(record.getR53_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR53_total() != null) {
						cell.setCellValue(record.getR53_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);

					cell = row.getCell(1);
					if (record.getR54_usd() != null) {
						cell.setCellValue(record.getR54_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR54_zar() != null) {
						cell.setCellValue(record.getR54_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR54_gbp() != null) {
						cell.setCellValue(record.getR54_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR54_euro() != null) {
						cell.setCellValue(record.getR54_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR54_yen() != null) {
						cell.setCellValue(record.getR54_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR54_c6() != null) {
						cell.setCellValue(record.getR54_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR54_c7() != null) {
						cell.setCellValue(record.getR54_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR54_c8() != null) {
						cell.setCellValue(record.getR54_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR54_total() != null) {
						cell.setCellValue(record.getR54_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					cell = row.getCell(1);
					if (record.getR55_usd() != null) {
						cell.setCellValue(record.getR55_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR55_zar() != null) {
						cell.setCellValue(record.getR55_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR55_gbp() != null) {
						cell.setCellValue(record.getR55_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR55_euro() != null) {
						cell.setCellValue(record.getR55_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR55_yen() != null) {
						cell.setCellValue(record.getR55_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR55_c6() != null) {
						cell.setCellValue(record.getR55_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR55_c7() != null) {
						cell.setCellValue(record.getR55_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR55_c8() != null) {
						cell.setCellValue(record.getR55_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR55_total() != null) {
						cell.setCellValue(record.getR55_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);

					cell = row.getCell(1);
					if (record.getR56_usd() != null) {
						cell.setCellValue(record.getR56_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR56_zar() != null) {
						cell.setCellValue(record.getR56_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR56_gbp() != null) {
						cell.setCellValue(record.getR56_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR56_euro() != null) {
						cell.setCellValue(record.getR56_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR56_yen() != null) {
						cell.setCellValue(record.getR56_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR56_c6() != null) {
						cell.setCellValue(record.getR56_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR56_c7() != null) {
						cell.setCellValue(record.getR56_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR56_c8() != null) {
						cell.setCellValue(record.getR56_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR56_total() != null) {
						cell.setCellValue(record.getR56_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row57
					row = sheet.getRow(56);

					cell = row.getCell(1);
					if (record.getR57_usd() != null) {
						cell.setCellValue(record.getR57_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR57_zar() != null) {
						cell.setCellValue(record.getR57_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR57_gbp() != null) {
						cell.setCellValue(record.getR57_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR57_euro() != null) {
						cell.setCellValue(record.getR57_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR57_yen() != null) {
						cell.setCellValue(record.getR57_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR57_c6() != null) {
						cell.setCellValue(record.getR57_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR57_c7() != null) {
						cell.setCellValue(record.getR57_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR57_c8() != null) {
						cell.setCellValue(record.getR57_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR57_total() != null) {
						cell.setCellValue(record.getR57_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row58
					row = sheet.getRow(57);

					cell = row.getCell(1);
					if (record.getR58_usd() != null) {
						cell.setCellValue(record.getR58_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR58_zar() != null) {
						cell.setCellValue(record.getR58_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR58_gbp() != null) {
						cell.setCellValue(record.getR58_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR58_euro() != null) {
						cell.setCellValue(record.getR58_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR58_yen() != null) {
						cell.setCellValue(record.getR58_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR58_c6() != null) {
						cell.setCellValue(record.getR58_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR58_c7() != null) {
						cell.setCellValue(record.getR58_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR58_c8() != null) {
						cell.setCellValue(record.getR58_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR58_total() != null) {
						cell.setCellValue(record.getR58_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					cell = row.getCell(1);
					if (record.getR59_usd() != null) {
						cell.setCellValue(record.getR59_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR59_zar() != null) {
						cell.setCellValue(record.getR59_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR59_gbp() != null) {
						cell.setCellValue(record.getR59_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR59_euro() != null) {
						cell.setCellValue(record.getR59_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR59_yen() != null) {
						cell.setCellValue(record.getR59_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR59_c6() != null) {
						cell.setCellValue(record.getR59_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR59_c7() != null) {
						cell.setCellValue(record.getR59_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR59_c8() != null) {
						cell.setCellValue(record.getR59_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR59_total() != null) {
						cell.setCellValue(record.getR59_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					cell = row.getCell(1);
					if (record.getR60_usd() != null) {
						cell.setCellValue(record.getR60_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR60_zar() != null) {
						cell.setCellValue(record.getR60_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR60_gbp() != null) {
						cell.setCellValue(record.getR60_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR60_euro() != null) {
						cell.setCellValue(record.getR60_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR60_yen() != null) {
						cell.setCellValue(record.getR60_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR60_c6() != null) {
						cell.setCellValue(record.getR60_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR60_c7() != null) {
						cell.setCellValue(record.getR60_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR60_c8() != null) {
						cell.setCellValue(record.getR60_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR60_total() != null) {
						cell.setCellValue(record.getR60_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);

					cell = row.getCell(1);
					if (record.getR61_usd() != null) {
						cell.setCellValue(record.getR61_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR61_zar() != null) {
						cell.setCellValue(record.getR61_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR61_gbp() != null) {
						cell.setCellValue(record.getR61_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR61_euro() != null) {
						cell.setCellValue(record.getR61_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR61_yen() != null) {
						cell.setCellValue(record.getR61_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR61_c6() != null) {
						cell.setCellValue(record.getR61_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR61_c7() != null) {
						cell.setCellValue(record.getR61_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR61_c8() != null) {
						cell.setCellValue(record.getR61_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR61_total() != null) {
						cell.setCellValue(record.getR61_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);

					cell = row.getCell(1);
					if (record.getR62_usd() != null) {
						cell.setCellValue(record.getR62_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR62_zar() != null) {
						cell.setCellValue(record.getR62_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR62_gbp() != null) {
						cell.setCellValue(record.getR62_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR62_euro() != null) {
						cell.setCellValue(record.getR62_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR62_yen() != null) {
						cell.setCellValue(record.getR62_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR62_c6() != null) {
						cell.setCellValue(record.getR62_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR62_c7() != null) {
						cell.setCellValue(record.getR62_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR62_c8() != null) {
						cell.setCellValue(record.getR62_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR62_total() != null) {
						cell.setCellValue(record.getR62_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
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

//Resubmission
	public List<Object[]> getM_LA5Resub() {
		List<Object[]> resubList = new ArrayList<>();

		try {

			List<M_LA5_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_LA5_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " Resub records");
				M_LA5_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest Resub version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No Resub data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_LA5  Resub data: " + e.getMessage());
			e.printStackTrace();
		}

		return resubList;
	}

// Normal Email Excel
	public byte[] BRRS_M_LA5EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_LA5EmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
//		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//			logger.info("Service: Generating RESUB report for version {}", version);
//
//			try {
//				// ✅ Redirecting to Resub Excel
//				return BRRS_M_LA5ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
		} else {
			List<M_LA5_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_LA5 report. Returning empty result.");
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

				int startRow = 1;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_LA5_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						Cell R12Cell = row.createCell(4);

						if (record.getREPORT_DATE() != null) {

							R12Cell.setCellValue(record.getREPORT_DATE());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
						}
//EMAIL
						row = sheet.getRow(5);
						// row8
						row = sheet.getRow(7);
						Cell cell = row.getCell(1);
						if (record.getR8_usd() != null) {
							cell.setCellValue(record.getR8_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR8_zar() != null) {
							cell.setCellValue(record.getR8_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR8_gbp() != null) {
							cell.setCellValue(record.getR8_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR8_euro() != null) {
							cell.setCellValue(record.getR8_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR8_yen() != null) {
							cell.setCellValue(record.getR8_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR8_c6() != null) {
							cell.setCellValue(record.getR8_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR8_c7() != null) {
							cell.setCellValue(record.getR8_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR8_c8() != null) {
							cell.setCellValue(record.getR8_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR8_total() != null) {
							cell.setCellValue(record.getR8_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row9
						row = sheet.getRow(8);

						cell = row.getCell(1);
						if (record.getR9_usd() != null) {
							cell.setCellValue(record.getR9_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR9_zar() != null) {
							cell.setCellValue(record.getR9_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR9_gbp() != null) {
							cell.setCellValue(record.getR9_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR9_euro() != null) {
							cell.setCellValue(record.getR9_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR9_yen() != null) {
							cell.setCellValue(record.getR9_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR9_c6() != null) {
							cell.setCellValue(record.getR9_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR9_c7() != null) {
							cell.setCellValue(record.getR9_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR9_c8() != null) {
							cell.setCellValue(record.getR9_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR9_total() != null) {
							cell.setCellValue(record.getR9_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row10
						row = sheet.getRow(9);

						cell = row.getCell(1);
						if (record.getR10_usd() != null) {
							cell.setCellValue(record.getR10_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR10_zar() != null) {
							cell.setCellValue(record.getR10_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR10_gbp() != null) {
							cell.setCellValue(record.getR10_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR10_euro() != null) {
							cell.setCellValue(record.getR10_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR10_yen() != null) {
							cell.setCellValue(record.getR10_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR10_c6() != null) {
							cell.setCellValue(record.getR10_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR10_c7() != null) {
							cell.setCellValue(record.getR10_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR10_c8() != null) {
							cell.setCellValue(record.getR10_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR10_total() != null) {
							cell.setCellValue(record.getR10_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row11
						row = sheet.getRow(10);

						cell = row.getCell(1);
						if (record.getR11_usd() != null) {
							cell.setCellValue(record.getR11_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR11_zar() != null) {
							cell.setCellValue(record.getR11_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR11_gbp() != null) {
							cell.setCellValue(record.getR11_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR11_euro() != null) {
							cell.setCellValue(record.getR11_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR11_yen() != null) {
							cell.setCellValue(record.getR11_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR11_c6() != null) {
							cell.setCellValue(record.getR11_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR11_c7() != null) {
							cell.setCellValue(record.getR11_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR11_c8() != null) {
							cell.setCellValue(record.getR11_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR11_total() != null) {
							cell.setCellValue(record.getR11_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row12
						row = sheet.getRow(11);

						cell = row.getCell(1);
						if (record.getR12_usd() != null) {
							cell.setCellValue(record.getR12_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR12_zar() != null) {
							cell.setCellValue(record.getR12_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR12_gbp() != null) {
							cell.setCellValue(record.getR12_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR12_euro() != null) {
							cell.setCellValue(record.getR12_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR12_yen() != null) {
							cell.setCellValue(record.getR12_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR12_c6() != null) {
							cell.setCellValue(record.getR12_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR12_c7() != null) {
							cell.setCellValue(record.getR12_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR12_c8() != null) {
							cell.setCellValue(record.getR12_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR12_total() != null) {
							cell.setCellValue(record.getR12_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row13
						row = sheet.getRow(12);

						cell = row.getCell(1);
						if (record.getR13_usd() != null) {
							cell.setCellValue(record.getR13_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR13_zar() != null) {
							cell.setCellValue(record.getR13_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR13_gbp() != null) {
							cell.setCellValue(record.getR13_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR13_euro() != null) {
							cell.setCellValue(record.getR13_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR13_yen() != null) {
							cell.setCellValue(record.getR13_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR13_c6() != null) {
							cell.setCellValue(record.getR13_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR13_c7() != null) {
							cell.setCellValue(record.getR13_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR13_c8() != null) {
							cell.setCellValue(record.getR13_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR13_total() != null) {
							cell.setCellValue(record.getR13_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row14
						row = sheet.getRow(13);

						cell = row.getCell(1);
						if (record.getR14_usd() != null) {
							cell.setCellValue(record.getR14_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR14_zar() != null) {
							cell.setCellValue(record.getR14_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR14_gbp() != null) {
							cell.setCellValue(record.getR14_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR14_euro() != null) {
							cell.setCellValue(record.getR14_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR14_yen() != null) {
							cell.setCellValue(record.getR14_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR14_c6() != null) {
							cell.setCellValue(record.getR14_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR14_c7() != null) {
							cell.setCellValue(record.getR14_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR14_c8() != null) {
							cell.setCellValue(record.getR14_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR14_total() != null) {
							cell.setCellValue(record.getR14_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row15
						row = sheet.getRow(14);

						cell = row.getCell(1);
						if (record.getR15_usd() != null) {
							cell.setCellValue(record.getR15_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR15_zar() != null) {
							cell.setCellValue(record.getR15_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR15_gbp() != null) {
							cell.setCellValue(record.getR15_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR15_euro() != null) {
							cell.setCellValue(record.getR15_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR15_yen() != null) {
							cell.setCellValue(record.getR15_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR15_c6() != null) {
							cell.setCellValue(record.getR15_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR15_c7() != null) {
							cell.setCellValue(record.getR15_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR15_c8() != null) {
							cell.setCellValue(record.getR15_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR15_total() != null) {
							cell.setCellValue(record.getR15_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row16
						row = sheet.getRow(15);

						cell = row.getCell(1);
						if (record.getR16_usd() != null) {
							cell.setCellValue(record.getR16_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR16_zar() != null) {
							cell.setCellValue(record.getR16_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR16_gbp() != null) {
							cell.setCellValue(record.getR16_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR16_euro() != null) {
							cell.setCellValue(record.getR16_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR16_yen() != null) {
							cell.setCellValue(record.getR16_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR16_c6() != null) {
							cell.setCellValue(record.getR16_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR16_c7() != null) {
							cell.setCellValue(record.getR16_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR16_c8() != null) {
							cell.setCellValue(record.getR16_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR16_total() != null) {
							cell.setCellValue(record.getR16_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row17
						row = sheet.getRow(16);

						cell = row.getCell(1);
						if (record.getR17_usd() != null) {
							cell.setCellValue(record.getR17_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR17_zar() != null) {
							cell.setCellValue(record.getR17_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR17_gbp() != null) {
							cell.setCellValue(record.getR17_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR17_euro() != null) {
							cell.setCellValue(record.getR17_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR17_yen() != null) {
							cell.setCellValue(record.getR17_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR17_c6() != null) {
							cell.setCellValue(record.getR17_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR17_c7() != null) {
							cell.setCellValue(record.getR17_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR17_c8() != null) {
							cell.setCellValue(record.getR17_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR17_total() != null) {
							cell.setCellValue(record.getR17_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row18
						row = sheet.getRow(17);

						cell = row.getCell(1);
						if (record.getR18_usd() != null) {
							cell.setCellValue(record.getR18_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR18_zar() != null) {
							cell.setCellValue(record.getR18_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR18_gbp() != null) {
							cell.setCellValue(record.getR18_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR18_euro() != null) {
							cell.setCellValue(record.getR18_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR18_yen() != null) {
							cell.setCellValue(record.getR18_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR18_c6() != null) {
							cell.setCellValue(record.getR18_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR18_c7() != null) {
							cell.setCellValue(record.getR18_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR18_c8() != null) {
							cell.setCellValue(record.getR18_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR18_total() != null) {
							cell.setCellValue(record.getR18_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row19
						row = sheet.getRow(18);

						cell = row.getCell(1);
						if (record.getR19_usd() != null) {
							cell.setCellValue(record.getR19_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR19_zar() != null) {
							cell.setCellValue(record.getR19_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR19_gbp() != null) {
							cell.setCellValue(record.getR19_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR19_euro() != null) {
							cell.setCellValue(record.getR19_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR19_yen() != null) {
							cell.setCellValue(record.getR19_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR19_c6() != null) {
							cell.setCellValue(record.getR19_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR19_c7() != null) {
							cell.setCellValue(record.getR19_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR19_c8() != null) {
							cell.setCellValue(record.getR19_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR19_total() != null) {
							cell.setCellValue(record.getR19_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row20
						row = sheet.getRow(19);

						cell = row.getCell(1);
						if (record.getR20_usd() != null) {
							cell.setCellValue(record.getR20_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR20_zar() != null) {
							cell.setCellValue(record.getR20_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR20_gbp() != null) {
							cell.setCellValue(record.getR20_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR20_euro() != null) {
							cell.setCellValue(record.getR20_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR20_yen() != null) {
							cell.setCellValue(record.getR20_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR20_c6() != null) {
							cell.setCellValue(record.getR20_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR20_c7() != null) {
							cell.setCellValue(record.getR20_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR20_c8() != null) {
							cell.setCellValue(record.getR20_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR20_total() != null) {
							cell.setCellValue(record.getR20_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row21
						row = sheet.getRow(20);

						cell = row.getCell(1);
						if (record.getR21_usd() != null) {
							cell.setCellValue(record.getR21_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR21_zar() != null) {
							cell.setCellValue(record.getR21_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR21_gbp() != null) {
							cell.setCellValue(record.getR21_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR21_euro() != null) {
							cell.setCellValue(record.getR21_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR21_yen() != null) {
							cell.setCellValue(record.getR21_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR21_c6() != null) {
							cell.setCellValue(record.getR21_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR21_c7() != null) {
							cell.setCellValue(record.getR21_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR21_c8() != null) {
							cell.setCellValue(record.getR21_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR21_total() != null) {
							cell.setCellValue(record.getR21_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row22
						row = sheet.getRow(21);

						cell = row.getCell(1);
						if (record.getR22_usd() != null) {
							cell.setCellValue(record.getR22_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR22_zar() != null) {
							cell.setCellValue(record.getR22_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR22_gbp() != null) {
							cell.setCellValue(record.getR22_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR22_euro() != null) {
							cell.setCellValue(record.getR22_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR22_yen() != null) {
							cell.setCellValue(record.getR22_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR22_c6() != null) {
							cell.setCellValue(record.getR22_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR22_c7() != null) {
							cell.setCellValue(record.getR22_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR22_c8() != null) {
							cell.setCellValue(record.getR22_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR22_total() != null) {
							cell.setCellValue(record.getR22_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row23
						row = sheet.getRow(22);

						cell = row.getCell(1);
						if (record.getR23_usd() != null) {
							cell.setCellValue(record.getR23_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR23_zar() != null) {
							cell.setCellValue(record.getR23_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR23_gbp() != null) {
							cell.setCellValue(record.getR23_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR23_euro() != null) {
							cell.setCellValue(record.getR23_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR23_yen() != null) {
							cell.setCellValue(record.getR23_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR23_c6() != null) {
							cell.setCellValue(record.getR23_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR23_c7() != null) {
							cell.setCellValue(record.getR23_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR23_c8() != null) {
							cell.setCellValue(record.getR23_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR23_total() != null) {
							cell.setCellValue(record.getR23_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row24
						row = sheet.getRow(23);

						cell = row.getCell(1);
						if (record.getR24_usd() != null) {
							cell.setCellValue(record.getR24_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR24_zar() != null) {
							cell.setCellValue(record.getR24_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR24_gbp() != null) {
							cell.setCellValue(record.getR24_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR24_euro() != null) {
							cell.setCellValue(record.getR24_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR24_yen() != null) {
							cell.setCellValue(record.getR24_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR24_c6() != null) {
							cell.setCellValue(record.getR24_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR24_c7() != null) {
							cell.setCellValue(record.getR24_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR24_c8() != null) {
							cell.setCellValue(record.getR24_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR24_total() != null) {
							cell.setCellValue(record.getR24_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row25
						row = sheet.getRow(24);

						cell = row.getCell(1);
						if (record.getR25_usd() != null) {
							cell.setCellValue(record.getR25_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR25_zar() != null) {
							cell.setCellValue(record.getR25_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR25_gbp() != null) {
							cell.setCellValue(record.getR25_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR25_euro() != null) {
							cell.setCellValue(record.getR25_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR25_yen() != null) {
							cell.setCellValue(record.getR25_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR25_c6() != null) {
							cell.setCellValue(record.getR25_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR25_c7() != null) {
							cell.setCellValue(record.getR25_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR25_c8() != null) {
							cell.setCellValue(record.getR25_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR25_total() != null) {
							cell.setCellValue(record.getR25_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row26
						row = sheet.getRow(25);

						cell = row.getCell(1);
						if (record.getR26_usd() != null) {
							cell.setCellValue(record.getR26_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR26_zar() != null) {
							cell.setCellValue(record.getR26_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR26_gbp() != null) {
							cell.setCellValue(record.getR26_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR26_euro() != null) {
							cell.setCellValue(record.getR26_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR26_yen() != null) {
							cell.setCellValue(record.getR26_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR26_c6() != null) {
							cell.setCellValue(record.getR26_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR26_c7() != null) {
							cell.setCellValue(record.getR26_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR26_c8() != null) {
							cell.setCellValue(record.getR26_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR26_total() != null) {
							cell.setCellValue(record.getR26_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row27
						row = sheet.getRow(26);

						cell = row.getCell(1);
						if (record.getR27_usd() != null) {
							cell.setCellValue(record.getR27_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR27_zar() != null) {
							cell.setCellValue(record.getR27_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR27_gbp() != null) {
							cell.setCellValue(record.getR27_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR27_euro() != null) {
							cell.setCellValue(record.getR27_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR27_yen() != null) {
							cell.setCellValue(record.getR27_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR27_c6() != null) {
							cell.setCellValue(record.getR27_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR27_c7() != null) {
							cell.setCellValue(record.getR27_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR27_c8() != null) {
							cell.setCellValue(record.getR27_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR27_total() != null) {
							cell.setCellValue(record.getR27_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row28
						row = sheet.getRow(27);

						cell = row.getCell(1);
						if (record.getR28_usd() != null) {
							cell.setCellValue(record.getR28_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR28_zar() != null) {
							cell.setCellValue(record.getR28_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR28_gbp() != null) {
							cell.setCellValue(record.getR28_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR28_euro() != null) {
							cell.setCellValue(record.getR28_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR28_yen() != null) {
							cell.setCellValue(record.getR28_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR28_c6() != null) {
							cell.setCellValue(record.getR28_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR28_c7() != null) {
							cell.setCellValue(record.getR28_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR28_c8() != null) {
							cell.setCellValue(record.getR28_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR28_total() != null) {
							cell.setCellValue(record.getR28_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row29
						row = sheet.getRow(28);

						cell = row.getCell(1);
						if (record.getR29_usd() != null) {
							cell.setCellValue(record.getR29_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR29_zar() != null) {
							cell.setCellValue(record.getR29_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR29_gbp() != null) {
							cell.setCellValue(record.getR29_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR29_euro() != null) {
							cell.setCellValue(record.getR29_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR29_yen() != null) {
							cell.setCellValue(record.getR29_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR29_c6() != null) {
							cell.setCellValue(record.getR29_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR29_c7() != null) {
							cell.setCellValue(record.getR29_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR29_c8() != null) {
							cell.setCellValue(record.getR29_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR29_total() != null) {
							cell.setCellValue(record.getR29_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row30
						row = sheet.getRow(29);

						cell = row.getCell(1);
						if (record.getR30_usd() != null) {
							cell.setCellValue(record.getR30_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR30_zar() != null) {
							cell.setCellValue(record.getR30_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR30_gbp() != null) {
							cell.setCellValue(record.getR30_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR30_euro() != null) {
							cell.setCellValue(record.getR30_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR30_yen() != null) {
							cell.setCellValue(record.getR30_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR30_c6() != null) {
							cell.setCellValue(record.getR30_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR30_c7() != null) {
							cell.setCellValue(record.getR30_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR30_c8() != null) {
							cell.setCellValue(record.getR30_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR30_total() != null) {
							cell.setCellValue(record.getR30_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row31
						row = sheet.getRow(30);

						cell = row.getCell(1);
						if (record.getR31_usd() != null) {
							cell.setCellValue(record.getR31_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR31_zar() != null) {
							cell.setCellValue(record.getR31_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR31_gbp() != null) {
							cell.setCellValue(record.getR31_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR31_euro() != null) {
							cell.setCellValue(record.getR31_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR31_yen() != null) {
							cell.setCellValue(record.getR31_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR31_c6() != null) {
							cell.setCellValue(record.getR31_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR31_c7() != null) {
							cell.setCellValue(record.getR31_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR31_c8() != null) {
							cell.setCellValue(record.getR31_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR31_total() != null) {
							cell.setCellValue(record.getR31_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row32
						row = sheet.getRow(31);

						cell = row.getCell(1);
						if (record.getR32_usd() != null) {
							cell.setCellValue(record.getR32_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR32_zar() != null) {
							cell.setCellValue(record.getR32_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR32_gbp() != null) {
							cell.setCellValue(record.getR32_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR32_euro() != null) {
							cell.setCellValue(record.getR32_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR32_yen() != null) {
							cell.setCellValue(record.getR32_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR32_c6() != null) {
							cell.setCellValue(record.getR32_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR32_c7() != null) {
							cell.setCellValue(record.getR32_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR32_c8() != null) {
							cell.setCellValue(record.getR32_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR32_total() != null) {
							cell.setCellValue(record.getR32_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row33
						row = sheet.getRow(32);

						cell = row.getCell(1);
						if (record.getR33_usd() != null) {
							cell.setCellValue(record.getR33_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR33_zar() != null) {
							cell.setCellValue(record.getR33_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR33_gbp() != null) {
							cell.setCellValue(record.getR33_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR33_euro() != null) {
							cell.setCellValue(record.getR33_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR33_yen() != null) {
							cell.setCellValue(record.getR33_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR33_c6() != null) {
							cell.setCellValue(record.getR33_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR33_c7() != null) {
							cell.setCellValue(record.getR33_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR33_c8() != null) {
							cell.setCellValue(record.getR33_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR33_total() != null) {
							cell.setCellValue(record.getR33_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row34
						row = sheet.getRow(33);

						cell = row.getCell(1);
						if (record.getR34_usd() != null) {
							cell.setCellValue(record.getR34_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR34_zar() != null) {
							cell.setCellValue(record.getR34_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR34_gbp() != null) {
							cell.setCellValue(record.getR34_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR34_euro() != null) {
							cell.setCellValue(record.getR34_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR34_yen() != null) {
							cell.setCellValue(record.getR34_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR34_c6() != null) {
							cell.setCellValue(record.getR34_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR34_c7() != null) {
							cell.setCellValue(record.getR34_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR34_c8() != null) {
							cell.setCellValue(record.getR34_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR34_total() != null) {
							cell.setCellValue(record.getR34_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row35
						row = sheet.getRow(34);

						cell = row.getCell(1);
						if (record.getR35_usd() != null) {
							cell.setCellValue(record.getR35_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR35_zar() != null) {
							cell.setCellValue(record.getR35_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR35_gbp() != null) {
							cell.setCellValue(record.getR35_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR35_euro() != null) {
							cell.setCellValue(record.getR35_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR35_yen() != null) {
							cell.setCellValue(record.getR35_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR35_c6() != null) {
							cell.setCellValue(record.getR35_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR35_c7() != null) {
							cell.setCellValue(record.getR35_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR35_c8() != null) {
							cell.setCellValue(record.getR35_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR35_total() != null) {
							cell.setCellValue(record.getR35_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row36
						row = sheet.getRow(35);

						cell = row.getCell(1);
						if (record.getR36_usd() != null) {
							cell.setCellValue(record.getR36_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR36_zar() != null) {
							cell.setCellValue(record.getR36_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR36_gbp() != null) {
							cell.setCellValue(record.getR36_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR36_euro() != null) {
							cell.setCellValue(record.getR36_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR36_yen() != null) {
							cell.setCellValue(record.getR36_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR36_c6() != null) {
							cell.setCellValue(record.getR36_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR36_c7() != null) {
							cell.setCellValue(record.getR36_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR36_c8() != null) {
							cell.setCellValue(record.getR36_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR36_total() != null) {
							cell.setCellValue(record.getR36_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row37
						row = sheet.getRow(36);

						cell = row.getCell(1);
						if (record.getR37_usd() != null) {
							cell.setCellValue(record.getR37_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR37_zar() != null) {
							cell.setCellValue(record.getR37_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR37_gbp() != null) {
							cell.setCellValue(record.getR37_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR37_euro() != null) {
							cell.setCellValue(record.getR37_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR37_yen() != null) {
							cell.setCellValue(record.getR37_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR37_c6() != null) {
							cell.setCellValue(record.getR37_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR37_c7() != null) {
							cell.setCellValue(record.getR37_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR37_c8() != null) {
							cell.setCellValue(record.getR37_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR37_total() != null) {
							cell.setCellValue(record.getR37_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row38
						row = sheet.getRow(37);

						cell = row.getCell(1);
						if (record.getR38_usd() != null) {
							cell.setCellValue(record.getR38_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR38_zar() != null) {
							cell.setCellValue(record.getR38_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR38_gbp() != null) {
							cell.setCellValue(record.getR38_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR38_euro() != null) {
							cell.setCellValue(record.getR38_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR38_yen() != null) {
							cell.setCellValue(record.getR38_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR38_c6() != null) {
							cell.setCellValue(record.getR38_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR38_c7() != null) {
							cell.setCellValue(record.getR38_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR38_c8() != null) {
							cell.setCellValue(record.getR38_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR38_total() != null) {
							cell.setCellValue(record.getR38_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row39
						row = sheet.getRow(38);

						cell = row.getCell(1);
						if (record.getR39_usd() != null) {
							cell.setCellValue(record.getR39_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR39_zar() != null) {
							cell.setCellValue(record.getR39_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR39_gbp() != null) {
							cell.setCellValue(record.getR39_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR39_euro() != null) {
							cell.setCellValue(record.getR39_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR39_yen() != null) {
							cell.setCellValue(record.getR39_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR39_c6() != null) {
							cell.setCellValue(record.getR39_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR39_c7() != null) {
							cell.setCellValue(record.getR39_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR39_c8() != null) {
							cell.setCellValue(record.getR39_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR39_total() != null) {
							cell.setCellValue(record.getR39_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row40
						row = sheet.getRow(39);

						cell = row.getCell(1);
						if (record.getR40_usd() != null) {
							cell.setCellValue(record.getR40_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR40_zar() != null) {
							cell.setCellValue(record.getR40_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR40_gbp() != null) {
							cell.setCellValue(record.getR40_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR40_euro() != null) {
							cell.setCellValue(record.getR40_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR40_yen() != null) {
							cell.setCellValue(record.getR40_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR40_c6() != null) {
							cell.setCellValue(record.getR40_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR40_c7() != null) {
							cell.setCellValue(record.getR40_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR40_c8() != null) {
							cell.setCellValue(record.getR40_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR40_total() != null) {
							cell.setCellValue(record.getR40_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row41
						row = sheet.getRow(40);

						cell = row.getCell(1);
						if (record.getR41_usd() != null) {
							cell.setCellValue(record.getR41_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR41_zar() != null) {
							cell.setCellValue(record.getR41_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR41_gbp() != null) {
							cell.setCellValue(record.getR41_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR41_euro() != null) {
							cell.setCellValue(record.getR41_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR41_yen() != null) {
							cell.setCellValue(record.getR41_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR41_c6() != null) {
							cell.setCellValue(record.getR41_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR41_c7() != null) {
							cell.setCellValue(record.getR41_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR41_c8() != null) {
							cell.setCellValue(record.getR41_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR41_total() != null) {
							cell.setCellValue(record.getR41_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row42
						row = sheet.getRow(41);

						cell = row.getCell(1);
						if (record.getR42_usd() != null) {
							cell.setCellValue(record.getR42_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR42_zar() != null) {
							cell.setCellValue(record.getR42_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR42_gbp() != null) {
							cell.setCellValue(record.getR42_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR42_euro() != null) {
							cell.setCellValue(record.getR42_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR42_yen() != null) {
							cell.setCellValue(record.getR42_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR42_c6() != null) {
							cell.setCellValue(record.getR42_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR42_c7() != null) {
							cell.setCellValue(record.getR42_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR42_c8() != null) {
							cell.setCellValue(record.getR42_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR42_total() != null) {
							cell.setCellValue(record.getR42_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						} // row43
						row = sheet.getRow(42);

						cell = row.getCell(1);
						if (record.getR43_usd() != null) {
							cell.setCellValue(record.getR43_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR43_zar() != null) {
							cell.setCellValue(record.getR43_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR43_gbp() != null) {
							cell.setCellValue(record.getR43_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR43_euro() != null) {
							cell.setCellValue(record.getR43_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR43_yen() != null) {
							cell.setCellValue(record.getR43_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR43_c6() != null) {
							cell.setCellValue(record.getR43_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR43_c7() != null) {
							cell.setCellValue(record.getR43_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR43_c8() != null) {
							cell.setCellValue(record.getR43_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR43_total() != null) {
							cell.setCellValue(record.getR43_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row44
						row = sheet.getRow(43);

						cell = row.getCell(1);
						if (record.getR44_usd() != null) {
							cell.setCellValue(record.getR44_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR44_zar() != null) {
							cell.setCellValue(record.getR44_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR44_gbp() != null) {
							cell.setCellValue(record.getR44_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR44_euro() != null) {
							cell.setCellValue(record.getR44_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR44_yen() != null) {
							cell.setCellValue(record.getR44_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR44_c6() != null) {
							cell.setCellValue(record.getR44_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR44_c7() != null) {
							cell.setCellValue(record.getR44_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR44_c8() != null) {
							cell.setCellValue(record.getR44_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR44_total() != null) {
							cell.setCellValue(record.getR44_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row45
						row = sheet.getRow(44);

						cell = row.getCell(1);
						if (record.getR45_usd() != null) {
							cell.setCellValue(record.getR45_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR45_zar() != null) {
							cell.setCellValue(record.getR45_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR45_gbp() != null) {
							cell.setCellValue(record.getR45_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR45_euro() != null) {
							cell.setCellValue(record.getR45_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR45_yen() != null) {
							cell.setCellValue(record.getR45_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR45_c6() != null) {
							cell.setCellValue(record.getR45_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR45_c7() != null) {
							cell.setCellValue(record.getR45_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR45_c8() != null) {
							cell.setCellValue(record.getR45_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR45_total() != null) {
							cell.setCellValue(record.getR45_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row46
						row = sheet.getRow(45);

						cell = row.getCell(1);
						if (record.getR46_usd() != null) {
							cell.setCellValue(record.getR46_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR46_zar() != null) {
							cell.setCellValue(record.getR46_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR46_gbp() != null) {
							cell.setCellValue(record.getR46_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR46_euro() != null) {
							cell.setCellValue(record.getR46_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR46_yen() != null) {
							cell.setCellValue(record.getR46_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR46_c6() != null) {
							cell.setCellValue(record.getR46_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR46_c7() != null) {
							cell.setCellValue(record.getR46_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR46_c8() != null) {
							cell.setCellValue(record.getR46_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR46_total() != null) {
							cell.setCellValue(record.getR46_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row47
						row = sheet.getRow(46);

						cell = row.getCell(1);
						if (record.getR47_usd() != null) {
							cell.setCellValue(record.getR47_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR47_zar() != null) {
							cell.setCellValue(record.getR47_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR47_gbp() != null) {
							cell.setCellValue(record.getR47_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR47_euro() != null) {
							cell.setCellValue(record.getR47_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR47_yen() != null) {
							cell.setCellValue(record.getR47_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR47_c6() != null) {
							cell.setCellValue(record.getR47_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR47_c7() != null) {
							cell.setCellValue(record.getR47_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR47_c8() != null) {
							cell.setCellValue(record.getR47_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR47_total() != null) {
							cell.setCellValue(record.getR47_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row48
						row = sheet.getRow(47);

						cell = row.getCell(1);
						if (record.getR48_usd() != null) {
							cell.setCellValue(record.getR48_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR48_zar() != null) {
							cell.setCellValue(record.getR48_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR48_gbp() != null) {
							cell.setCellValue(record.getR48_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR48_euro() != null) {
							cell.setCellValue(record.getR48_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR48_yen() != null) {
							cell.setCellValue(record.getR48_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR48_c6() != null) {
							cell.setCellValue(record.getR48_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR48_c7() != null) {
							cell.setCellValue(record.getR48_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR48_c8() != null) {
							cell.setCellValue(record.getR48_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR48_total() != null) {
							cell.setCellValue(record.getR48_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row49
						row = sheet.getRow(48);

						cell = row.getCell(1);
						if (record.getR49_usd() != null) {
							cell.setCellValue(record.getR49_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR49_zar() != null) {
							cell.setCellValue(record.getR49_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR49_gbp() != null) {
							cell.setCellValue(record.getR49_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR49_euro() != null) {
							cell.setCellValue(record.getR49_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR49_yen() != null) {
							cell.setCellValue(record.getR49_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR49_c6() != null) {
							cell.setCellValue(record.getR49_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR49_c7() != null) {
							cell.setCellValue(record.getR49_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR49_c8() != null) {
							cell.setCellValue(record.getR49_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR49_total() != null) {
							cell.setCellValue(record.getR49_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row50
						row = sheet.getRow(49);

						cell = row.getCell(1);
						if (record.getR50_usd() != null) {
							cell.setCellValue(record.getR50_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR50_zar() != null) {
							cell.setCellValue(record.getR50_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR50_gbp() != null) {
							cell.setCellValue(record.getR50_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR50_euro() != null) {
							cell.setCellValue(record.getR50_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR50_yen() != null) {
							cell.setCellValue(record.getR50_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR50_c6() != null) {
							cell.setCellValue(record.getR50_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR50_c7() != null) {
							cell.setCellValue(record.getR50_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR50_c8() != null) {
							cell.setCellValue(record.getR50_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR50_total() != null) {
							cell.setCellValue(record.getR50_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row51
						row = sheet.getRow(50);

						cell = row.getCell(1);
						if (record.getR51_usd() != null) {
							cell.setCellValue(record.getR51_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR51_zar() != null) {
							cell.setCellValue(record.getR51_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR51_gbp() != null) {
							cell.setCellValue(record.getR51_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR51_euro() != null) {
							cell.setCellValue(record.getR51_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR51_yen() != null) {
							cell.setCellValue(record.getR51_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR51_c6() != null) {
							cell.setCellValue(record.getR51_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR51_c7() != null) {
							cell.setCellValue(record.getR51_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR51_c8() != null) {
							cell.setCellValue(record.getR51_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR51_total() != null) {
							cell.setCellValue(record.getR51_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row52
						row = sheet.getRow(51);

						cell = row.getCell(1);
						if (record.getR52_usd() != null) {
							cell.setCellValue(record.getR52_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR52_zar() != null) {
							cell.setCellValue(record.getR52_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR52_gbp() != null) {
							cell.setCellValue(record.getR52_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR52_euro() != null) {
							cell.setCellValue(record.getR52_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR52_yen() != null) {
							cell.setCellValue(record.getR52_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR52_c6() != null) {
							cell.setCellValue(record.getR52_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR52_c7() != null) {
							cell.setCellValue(record.getR52_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR52_c8() != null) {
							cell.setCellValue(record.getR52_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR52_total() != null) {
							cell.setCellValue(record.getR52_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row53
						row = sheet.getRow(52);

						cell = row.getCell(1);
						if (record.getR53_usd() != null) {
							cell.setCellValue(record.getR53_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR53_zar() != null) {
							cell.setCellValue(record.getR53_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR53_gbp() != null) {
							cell.setCellValue(record.getR53_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR53_euro() != null) {
							cell.setCellValue(record.getR53_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR53_yen() != null) {
							cell.setCellValue(record.getR53_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR53_c6() != null) {
							cell.setCellValue(record.getR53_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR53_c7() != null) {
							cell.setCellValue(record.getR53_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR53_c8() != null) {
							cell.setCellValue(record.getR53_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR53_total() != null) {
							cell.setCellValue(record.getR53_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row54
						row = sheet.getRow(53);

						cell = row.getCell(1);
						if (record.getR54_usd() != null) {
							cell.setCellValue(record.getR54_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR54_zar() != null) {
							cell.setCellValue(record.getR54_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR54_gbp() != null) {
							cell.setCellValue(record.getR54_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR54_euro() != null) {
							cell.setCellValue(record.getR54_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR54_yen() != null) {
							cell.setCellValue(record.getR54_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR54_c6() != null) {
							cell.setCellValue(record.getR54_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR54_c7() != null) {
							cell.setCellValue(record.getR54_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR54_c8() != null) {
							cell.setCellValue(record.getR54_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR54_total() != null) {
							cell.setCellValue(record.getR54_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row55
						row = sheet.getRow(54);

						cell = row.getCell(1);
						if (record.getR55_usd() != null) {
							cell.setCellValue(record.getR55_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR55_zar() != null) {
							cell.setCellValue(record.getR55_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR55_gbp() != null) {
							cell.setCellValue(record.getR55_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR55_euro() != null) {
							cell.setCellValue(record.getR55_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR55_yen() != null) {
							cell.setCellValue(record.getR55_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR55_c6() != null) {
							cell.setCellValue(record.getR55_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR55_c7() != null) {
							cell.setCellValue(record.getR55_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR55_c8() != null) {
							cell.setCellValue(record.getR55_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR55_total() != null) {
							cell.setCellValue(record.getR55_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row56
						row = sheet.getRow(55);

						cell = row.getCell(1);
						if (record.getR56_usd() != null) {
							cell.setCellValue(record.getR56_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR56_zar() != null) {
							cell.setCellValue(record.getR56_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR56_gbp() != null) {
							cell.setCellValue(record.getR56_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR56_euro() != null) {
							cell.setCellValue(record.getR56_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR56_yen() != null) {
							cell.setCellValue(record.getR56_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR56_c6() != null) {
							cell.setCellValue(record.getR56_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR56_c7() != null) {
							cell.setCellValue(record.getR56_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR56_c8() != null) {
							cell.setCellValue(record.getR56_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR56_total() != null) {
							cell.setCellValue(record.getR56_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row57
						row = sheet.getRow(56);

						cell = row.getCell(1);
						if (record.getR57_usd() != null) {
							cell.setCellValue(record.getR57_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR57_zar() != null) {
							cell.setCellValue(record.getR57_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR57_gbp() != null) {
							cell.setCellValue(record.getR57_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR57_euro() != null) {
							cell.setCellValue(record.getR57_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR57_yen() != null) {
							cell.setCellValue(record.getR57_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR57_c6() != null) {
							cell.setCellValue(record.getR57_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR57_c7() != null) {
							cell.setCellValue(record.getR57_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR57_c8() != null) {
							cell.setCellValue(record.getR57_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR57_total() != null) {
							cell.setCellValue(record.getR57_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}
						// row58
						row = sheet.getRow(57);

						cell = row.getCell(1);
						if (record.getR58_usd() != null) {
							cell.setCellValue(record.getR58_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR58_zar() != null) {
							cell.setCellValue(record.getR58_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR58_gbp() != null) {
							cell.setCellValue(record.getR58_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR58_euro() != null) {
							cell.setCellValue(record.getR58_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR58_yen() != null) {
							cell.setCellValue(record.getR58_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR58_c6() != null) {
							cell.setCellValue(record.getR58_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR58_c7() != null) {
							cell.setCellValue(record.getR58_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR58_c8() != null) {
							cell.setCellValue(record.getR58_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR58_total() != null) {
							cell.setCellValue(record.getR58_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row59
						row = sheet.getRow(58);

						cell = row.getCell(1);
						if (record.getR59_usd() != null) {
							cell.setCellValue(record.getR59_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR59_zar() != null) {
							cell.setCellValue(record.getR59_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR59_gbp() != null) {
							cell.setCellValue(record.getR59_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR59_euro() != null) {
							cell.setCellValue(record.getR59_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR59_yen() != null) {
							cell.setCellValue(record.getR59_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR59_c6() != null) {
							cell.setCellValue(record.getR59_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR59_c7() != null) {
							cell.setCellValue(record.getR59_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR59_c8() != null) {
							cell.setCellValue(record.getR59_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR59_total() != null) {
							cell.setCellValue(record.getR59_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row60
						row = sheet.getRow(59);

						cell = row.getCell(1);
						if (record.getR60_usd() != null) {
							cell.setCellValue(record.getR60_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR60_zar() != null) {
							cell.setCellValue(record.getR60_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR60_gbp() != null) {
							cell.setCellValue(record.getR60_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR60_euro() != null) {
							cell.setCellValue(record.getR60_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR60_yen() != null) {
							cell.setCellValue(record.getR60_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR60_c6() != null) {
							cell.setCellValue(record.getR60_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR60_c7() != null) {
							cell.setCellValue(record.getR60_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR60_c8() != null) {
							cell.setCellValue(record.getR60_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR60_total() != null) {
							cell.setCellValue(record.getR60_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row61
						row = sheet.getRow(60);

						cell = row.getCell(1);
						if (record.getR61_usd() != null) {
							cell.setCellValue(record.getR61_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR61_zar() != null) {
							cell.setCellValue(record.getR61_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR61_gbp() != null) {
							cell.setCellValue(record.getR61_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR61_euro() != null) {
							cell.setCellValue(record.getR61_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR61_yen() != null) {
							cell.setCellValue(record.getR61_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR61_c6() != null) {
							cell.setCellValue(record.getR61_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR61_c7() != null) {
							cell.setCellValue(record.getR61_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR61_c8() != null) {
							cell.setCellValue(record.getR61_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR61_total() != null) {
							cell.setCellValue(record.getR61_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						// row62
						row = sheet.getRow(61);

						cell = row.getCell(1);
						if (record.getR62_usd() != null) {
							cell.setCellValue(record.getR62_usd().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(2);
						if (record.getR62_zar() != null) {
							cell.setCellValue(record.getR62_zar().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(3);
						if (record.getR62_gbp() != null) {
							cell.setCellValue(record.getR62_gbp().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(4);
						if (record.getR62_euro() != null) {
							cell.setCellValue(record.getR62_euro().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(5);
						if (record.getR62_yen() != null) {
							cell.setCellValue(record.getR62_yen().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(6);
						if (record.getR62_c6() != null) {
							cell.setCellValue(record.getR62_c6().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(7);
						if (record.getR62_c7() != null) {
							cell.setCellValue(record.getR62_c7().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(8);
						if (record.getR62_c8() != null) {
							cell.setCellValue(record.getR62_c8().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
						}

						cell = row.getCell(9);
						if (record.getR62_total() != null) {
							cell.setCellValue(record.getR62_total().doubleValue());
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textStyle);
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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA5 EMAIL SUMMARY", null,
							"BRRS_M_LA5_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival Email Excel
	public byte[] BRRS_M_LA5EmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_LA5_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_LA5 report. Returning empty result.");
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
					M_LA5_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(4);

					if (record.getREPORT_DATE() != null) {

						R12Cell.setCellValue(record.getREPORT_DATE());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
//EMAIL
					row = sheet.getRow(5);
					// row8
					row = sheet.getRow(7);
					Cell cell = row.getCell(1);
					if (record.getR8_usd() != null) {
						cell.setCellValue(record.getR8_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR8_zar() != null) {
						cell.setCellValue(record.getR8_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR8_gbp() != null) {
						cell.setCellValue(record.getR8_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR8_euro() != null) {
						cell.setCellValue(record.getR8_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR8_yen() != null) {
						cell.setCellValue(record.getR8_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR8_c6() != null) {
						cell.setCellValue(record.getR8_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR8_c7() != null) {
						cell.setCellValue(record.getR8_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR8_c8() != null) {
						cell.setCellValue(record.getR8_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR8_total() != null) {
						cell.setCellValue(record.getR8_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row9
					row = sheet.getRow(8);

					cell = row.getCell(1);
					if (record.getR9_usd() != null) {
						cell.setCellValue(record.getR9_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR9_zar() != null) {
						cell.setCellValue(record.getR9_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR9_gbp() != null) {
						cell.setCellValue(record.getR9_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR9_euro() != null) {
						cell.setCellValue(record.getR9_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR9_yen() != null) {
						cell.setCellValue(record.getR9_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR9_c6() != null) {
						cell.setCellValue(record.getR9_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR9_c7() != null) {
						cell.setCellValue(record.getR9_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR9_c8() != null) {
						cell.setCellValue(record.getR9_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR9_total() != null) {
						cell.setCellValue(record.getR9_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row10
					row = sheet.getRow(9);

					cell = row.getCell(1);
					if (record.getR10_usd() != null) {
						cell.setCellValue(record.getR10_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR10_zar() != null) {
						cell.setCellValue(record.getR10_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR10_gbp() != null) {
						cell.setCellValue(record.getR10_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR10_euro() != null) {
						cell.setCellValue(record.getR10_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR10_yen() != null) {
						cell.setCellValue(record.getR10_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR10_c6() != null) {
						cell.setCellValue(record.getR10_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR10_c7() != null) {
						cell.setCellValue(record.getR10_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR10_c8() != null) {
						cell.setCellValue(record.getR10_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR10_total() != null) {
						cell.setCellValue(record.getR10_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					cell = row.getCell(1);
					if (record.getR11_usd() != null) {
						cell.setCellValue(record.getR11_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR11_zar() != null) {
						cell.setCellValue(record.getR11_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR11_gbp() != null) {
						cell.setCellValue(record.getR11_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR11_euro() != null) {
						cell.setCellValue(record.getR11_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR11_yen() != null) {
						cell.setCellValue(record.getR11_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR11_c6() != null) {
						cell.setCellValue(record.getR11_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR11_c7() != null) {
						cell.setCellValue(record.getR11_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR11_c8() != null) {
						cell.setCellValue(record.getR11_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR11_total() != null) {
						cell.setCellValue(record.getR11_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell = row.getCell(1);
					if (record.getR12_usd() != null) {
						cell.setCellValue(record.getR12_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR12_zar() != null) {
						cell.setCellValue(record.getR12_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR12_gbp() != null) {
						cell.setCellValue(record.getR12_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR12_euro() != null) {
						cell.setCellValue(record.getR12_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR12_yen() != null) {
						cell.setCellValue(record.getR12_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR12_c6() != null) {
						cell.setCellValue(record.getR12_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR12_c7() != null) {
						cell.setCellValue(record.getR12_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR12_c8() != null) {
						cell.setCellValue(record.getR12_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR12_total() != null) {
						cell.setCellValue(record.getR12_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					cell = row.getCell(1);
					if (record.getR13_usd() != null) {
						cell.setCellValue(record.getR13_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR13_zar() != null) {
						cell.setCellValue(record.getR13_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR13_gbp() != null) {
						cell.setCellValue(record.getR13_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR13_euro() != null) {
						cell.setCellValue(record.getR13_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR13_yen() != null) {
						cell.setCellValue(record.getR13_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR13_c6() != null) {
						cell.setCellValue(record.getR13_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR13_c7() != null) {
						cell.setCellValue(record.getR13_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR13_c8() != null) {
						cell.setCellValue(record.getR13_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR13_total() != null) {
						cell.setCellValue(record.getR13_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					cell = row.getCell(1);
					if (record.getR14_usd() != null) {
						cell.setCellValue(record.getR14_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR14_zar() != null) {
						cell.setCellValue(record.getR14_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR14_gbp() != null) {
						cell.setCellValue(record.getR14_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR14_euro() != null) {
						cell.setCellValue(record.getR14_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR14_yen() != null) {
						cell.setCellValue(record.getR14_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR14_c6() != null) {
						cell.setCellValue(record.getR14_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR14_c7() != null) {
						cell.setCellValue(record.getR14_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR14_c8() != null) {
						cell.setCellValue(record.getR14_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR14_total() != null) {
						cell.setCellValue(record.getR14_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row15
					row = sheet.getRow(14);

					cell = row.getCell(1);
					if (record.getR15_usd() != null) {
						cell.setCellValue(record.getR15_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR15_zar() != null) {
						cell.setCellValue(record.getR15_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR15_gbp() != null) {
						cell.setCellValue(record.getR15_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR15_euro() != null) {
						cell.setCellValue(record.getR15_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR15_yen() != null) {
						cell.setCellValue(record.getR15_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR15_c6() != null) {
						cell.setCellValue(record.getR15_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR15_c7() != null) {
						cell.setCellValue(record.getR15_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR15_c8() != null) {
						cell.setCellValue(record.getR15_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR15_total() != null) {
						cell.setCellValue(record.getR15_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					cell = row.getCell(1);
					if (record.getR16_usd() != null) {
						cell.setCellValue(record.getR16_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR16_zar() != null) {
						cell.setCellValue(record.getR16_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR16_gbp() != null) {
						cell.setCellValue(record.getR16_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR16_euro() != null) {
						cell.setCellValue(record.getR16_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR16_yen() != null) {
						cell.setCellValue(record.getR16_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR16_c6() != null) {
						cell.setCellValue(record.getR16_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR16_c7() != null) {
						cell.setCellValue(record.getR16_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR16_c8() != null) {
						cell.setCellValue(record.getR16_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR16_total() != null) {
						cell.setCellValue(record.getR16_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row17
					row = sheet.getRow(16);

					cell = row.getCell(1);
					if (record.getR17_usd() != null) {
						cell.setCellValue(record.getR17_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR17_zar() != null) {
						cell.setCellValue(record.getR17_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR17_gbp() != null) {
						cell.setCellValue(record.getR17_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR17_euro() != null) {
						cell.setCellValue(record.getR17_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR17_yen() != null) {
						cell.setCellValue(record.getR17_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR17_c6() != null) {
						cell.setCellValue(record.getR17_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR17_c7() != null) {
						cell.setCellValue(record.getR17_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR17_c8() != null) {
						cell.setCellValue(record.getR17_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR17_total() != null) {
						cell.setCellValue(record.getR17_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					cell = row.getCell(1);
					if (record.getR18_usd() != null) {
						cell.setCellValue(record.getR18_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR18_zar() != null) {
						cell.setCellValue(record.getR18_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR18_gbp() != null) {
						cell.setCellValue(record.getR18_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR18_euro() != null) {
						cell.setCellValue(record.getR18_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR18_yen() != null) {
						cell.setCellValue(record.getR18_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR18_c6() != null) {
						cell.setCellValue(record.getR18_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR18_c7() != null) {
						cell.setCellValue(record.getR18_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR18_c8() != null) {
						cell.setCellValue(record.getR18_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR18_total() != null) {
						cell.setCellValue(record.getR18_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row19
					row = sheet.getRow(18);

					cell = row.getCell(1);
					if (record.getR19_usd() != null) {
						cell.setCellValue(record.getR19_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR19_zar() != null) {
						cell.setCellValue(record.getR19_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR19_gbp() != null) {
						cell.setCellValue(record.getR19_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR19_euro() != null) {
						cell.setCellValue(record.getR19_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR19_yen() != null) {
						cell.setCellValue(record.getR19_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR19_c6() != null) {
						cell.setCellValue(record.getR19_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR19_c7() != null) {
						cell.setCellValue(record.getR19_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR19_c8() != null) {
						cell.setCellValue(record.getR19_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR19_total() != null) {
						cell.setCellValue(record.getR19_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					cell = row.getCell(1);
					if (record.getR20_usd() != null) {
						cell.setCellValue(record.getR20_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR20_zar() != null) {
						cell.setCellValue(record.getR20_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR20_gbp() != null) {
						cell.setCellValue(record.getR20_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR20_euro() != null) {
						cell.setCellValue(record.getR20_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR20_yen() != null) {
						cell.setCellValue(record.getR20_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR20_c6() != null) {
						cell.setCellValue(record.getR20_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR20_c7() != null) {
						cell.setCellValue(record.getR20_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR20_c8() != null) {
						cell.setCellValue(record.getR20_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR20_total() != null) {
						cell.setCellValue(record.getR20_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row21
					row = sheet.getRow(20);

					cell = row.getCell(1);
					if (record.getR21_usd() != null) {
						cell.setCellValue(record.getR21_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR21_zar() != null) {
						cell.setCellValue(record.getR21_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR21_gbp() != null) {
						cell.setCellValue(record.getR21_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR21_euro() != null) {
						cell.setCellValue(record.getR21_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR21_yen() != null) {
						cell.setCellValue(record.getR21_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR21_c6() != null) {
						cell.setCellValue(record.getR21_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR21_c7() != null) {
						cell.setCellValue(record.getR21_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR21_c8() != null) {
						cell.setCellValue(record.getR21_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR21_total() != null) {
						cell.setCellValue(record.getR21_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					cell = row.getCell(1);
					if (record.getR22_usd() != null) {
						cell.setCellValue(record.getR22_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR22_zar() != null) {
						cell.setCellValue(record.getR22_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR22_gbp() != null) {
						cell.setCellValue(record.getR22_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR22_euro() != null) {
						cell.setCellValue(record.getR22_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR22_yen() != null) {
						cell.setCellValue(record.getR22_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR22_c6() != null) {
						cell.setCellValue(record.getR22_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR22_c7() != null) {
						cell.setCellValue(record.getR22_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR22_c8() != null) {
						cell.setCellValue(record.getR22_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR22_total() != null) {
						cell.setCellValue(record.getR22_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row23
					row = sheet.getRow(22);

					cell = row.getCell(1);
					if (record.getR23_usd() != null) {
						cell.setCellValue(record.getR23_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR23_zar() != null) {
						cell.setCellValue(record.getR23_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR23_gbp() != null) {
						cell.setCellValue(record.getR23_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR23_euro() != null) {
						cell.setCellValue(record.getR23_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR23_yen() != null) {
						cell.setCellValue(record.getR23_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR23_c6() != null) {
						cell.setCellValue(record.getR23_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR23_c7() != null) {
						cell.setCellValue(record.getR23_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR23_c8() != null) {
						cell.setCellValue(record.getR23_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR23_total() != null) {
						cell.setCellValue(record.getR23_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					cell = row.getCell(1);
					if (record.getR24_usd() != null) {
						cell.setCellValue(record.getR24_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR24_zar() != null) {
						cell.setCellValue(record.getR24_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR24_gbp() != null) {
						cell.setCellValue(record.getR24_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR24_euro() != null) {
						cell.setCellValue(record.getR24_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR24_yen() != null) {
						cell.setCellValue(record.getR24_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR24_c6() != null) {
						cell.setCellValue(record.getR24_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR24_c7() != null) {
						cell.setCellValue(record.getR24_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR24_c8() != null) {
						cell.setCellValue(record.getR24_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR24_total() != null) {
						cell.setCellValue(record.getR24_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row25
					row = sheet.getRow(24);

					cell = row.getCell(1);
					if (record.getR25_usd() != null) {
						cell.setCellValue(record.getR25_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR25_zar() != null) {
						cell.setCellValue(record.getR25_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR25_gbp() != null) {
						cell.setCellValue(record.getR25_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR25_euro() != null) {
						cell.setCellValue(record.getR25_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR25_yen() != null) {
						cell.setCellValue(record.getR25_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR25_c6() != null) {
						cell.setCellValue(record.getR25_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR25_c7() != null) {
						cell.setCellValue(record.getR25_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR25_c8() != null) {
						cell.setCellValue(record.getR25_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR25_total() != null) {
						cell.setCellValue(record.getR25_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					cell = row.getCell(1);
					if (record.getR26_usd() != null) {
						cell.setCellValue(record.getR26_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR26_zar() != null) {
						cell.setCellValue(record.getR26_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR26_gbp() != null) {
						cell.setCellValue(record.getR26_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR26_euro() != null) {
						cell.setCellValue(record.getR26_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR26_yen() != null) {
						cell.setCellValue(record.getR26_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR26_c6() != null) {
						cell.setCellValue(record.getR26_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR26_c7() != null) {
						cell.setCellValue(record.getR26_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR26_c8() != null) {
						cell.setCellValue(record.getR26_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR26_total() != null) {
						cell.setCellValue(record.getR26_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					cell = row.getCell(1);
					if (record.getR27_usd() != null) {
						cell.setCellValue(record.getR27_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR27_zar() != null) {
						cell.setCellValue(record.getR27_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR27_gbp() != null) {
						cell.setCellValue(record.getR27_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR27_euro() != null) {
						cell.setCellValue(record.getR27_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR27_yen() != null) {
						cell.setCellValue(record.getR27_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR27_c6() != null) {
						cell.setCellValue(record.getR27_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR27_c7() != null) {
						cell.setCellValue(record.getR27_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR27_c8() != null) {
						cell.setCellValue(record.getR27_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR27_total() != null) {
						cell.setCellValue(record.getR27_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);

					cell = row.getCell(1);
					if (record.getR28_usd() != null) {
						cell.setCellValue(record.getR28_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR28_zar() != null) {
						cell.setCellValue(record.getR28_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR28_gbp() != null) {
						cell.setCellValue(record.getR28_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR28_euro() != null) {
						cell.setCellValue(record.getR28_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR28_yen() != null) {
						cell.setCellValue(record.getR28_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR28_c6() != null) {
						cell.setCellValue(record.getR28_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR28_c7() != null) {
						cell.setCellValue(record.getR28_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR28_c8() != null) {
						cell.setCellValue(record.getR28_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR28_total() != null) {
						cell.setCellValue(record.getR28_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cell = row.getCell(1);
					if (record.getR29_usd() != null) {
						cell.setCellValue(record.getR29_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR29_zar() != null) {
						cell.setCellValue(record.getR29_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR29_gbp() != null) {
						cell.setCellValue(record.getR29_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR29_euro() != null) {
						cell.setCellValue(record.getR29_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR29_yen() != null) {
						cell.setCellValue(record.getR29_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR29_c6() != null) {
						cell.setCellValue(record.getR29_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR29_c7() != null) {
						cell.setCellValue(record.getR29_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR29_c8() != null) {
						cell.setCellValue(record.getR29_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR29_total() != null) {
						cell.setCellValue(record.getR29_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cell = row.getCell(1);
					if (record.getR30_usd() != null) {
						cell.setCellValue(record.getR30_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR30_zar() != null) {
						cell.setCellValue(record.getR30_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR30_gbp() != null) {
						cell.setCellValue(record.getR30_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR30_euro() != null) {
						cell.setCellValue(record.getR30_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR30_yen() != null) {
						cell.setCellValue(record.getR30_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR30_c6() != null) {
						cell.setCellValue(record.getR30_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR30_c7() != null) {
						cell.setCellValue(record.getR30_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR30_c8() != null) {
						cell.setCellValue(record.getR30_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR30_total() != null) {
						cell.setCellValue(record.getR30_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					cell = row.getCell(1);
					if (record.getR31_usd() != null) {
						cell.setCellValue(record.getR31_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR31_zar() != null) {
						cell.setCellValue(record.getR31_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR31_gbp() != null) {
						cell.setCellValue(record.getR31_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR31_euro() != null) {
						cell.setCellValue(record.getR31_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR31_yen() != null) {
						cell.setCellValue(record.getR31_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR31_c6() != null) {
						cell.setCellValue(record.getR31_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR31_c7() != null) {
						cell.setCellValue(record.getR31_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR31_c8() != null) {
						cell.setCellValue(record.getR31_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR31_total() != null) {
						cell.setCellValue(record.getR31_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					cell = row.getCell(1);
					if (record.getR32_usd() != null) {
						cell.setCellValue(record.getR32_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR32_zar() != null) {
						cell.setCellValue(record.getR32_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR32_gbp() != null) {
						cell.setCellValue(record.getR32_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR32_euro() != null) {
						cell.setCellValue(record.getR32_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR32_yen() != null) {
						cell.setCellValue(record.getR32_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR32_c6() != null) {
						cell.setCellValue(record.getR32_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR32_c7() != null) {
						cell.setCellValue(record.getR32_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR32_c8() != null) {
						cell.setCellValue(record.getR32_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR32_total() != null) {
						cell.setCellValue(record.getR32_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					cell = row.getCell(1);
					if (record.getR33_usd() != null) {
						cell.setCellValue(record.getR33_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR33_zar() != null) {
						cell.setCellValue(record.getR33_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR33_gbp() != null) {
						cell.setCellValue(record.getR33_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR33_euro() != null) {
						cell.setCellValue(record.getR33_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR33_yen() != null) {
						cell.setCellValue(record.getR33_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR33_c6() != null) {
						cell.setCellValue(record.getR33_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR33_c7() != null) {
						cell.setCellValue(record.getR33_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR33_c8() != null) {
						cell.setCellValue(record.getR33_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR33_total() != null) {
						cell.setCellValue(record.getR33_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					cell = row.getCell(1);
					if (record.getR34_usd() != null) {
						cell.setCellValue(record.getR34_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR34_zar() != null) {
						cell.setCellValue(record.getR34_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR34_gbp() != null) {
						cell.setCellValue(record.getR34_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR34_euro() != null) {
						cell.setCellValue(record.getR34_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR34_yen() != null) {
						cell.setCellValue(record.getR34_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR34_c6() != null) {
						cell.setCellValue(record.getR34_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR34_c7() != null) {
						cell.setCellValue(record.getR34_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR34_c8() != null) {
						cell.setCellValue(record.getR34_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR34_total() != null) {
						cell.setCellValue(record.getR34_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					cell = row.getCell(1);
					if (record.getR35_usd() != null) {
						cell.setCellValue(record.getR35_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR35_zar() != null) {
						cell.setCellValue(record.getR35_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR35_gbp() != null) {
						cell.setCellValue(record.getR35_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR35_euro() != null) {
						cell.setCellValue(record.getR35_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR35_yen() != null) {
						cell.setCellValue(record.getR35_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR35_c6() != null) {
						cell.setCellValue(record.getR35_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR35_c7() != null) {
						cell.setCellValue(record.getR35_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR35_c8() != null) {
						cell.setCellValue(record.getR35_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR35_total() != null) {
						cell.setCellValue(record.getR35_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					cell = row.getCell(1);
					if (record.getR36_usd() != null) {
						cell.setCellValue(record.getR36_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR36_zar() != null) {
						cell.setCellValue(record.getR36_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR36_gbp() != null) {
						cell.setCellValue(record.getR36_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR36_euro() != null) {
						cell.setCellValue(record.getR36_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR36_yen() != null) {
						cell.setCellValue(record.getR36_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR36_c6() != null) {
						cell.setCellValue(record.getR36_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR36_c7() != null) {
						cell.setCellValue(record.getR36_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR36_c8() != null) {
						cell.setCellValue(record.getR36_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR36_total() != null) {
						cell.setCellValue(record.getR36_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);

					cell = row.getCell(1);
					if (record.getR37_usd() != null) {
						cell.setCellValue(record.getR37_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR37_zar() != null) {
						cell.setCellValue(record.getR37_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR37_gbp() != null) {
						cell.setCellValue(record.getR37_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR37_euro() != null) {
						cell.setCellValue(record.getR37_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR37_yen() != null) {
						cell.setCellValue(record.getR37_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR37_c6() != null) {
						cell.setCellValue(record.getR37_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR37_c7() != null) {
						cell.setCellValue(record.getR37_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR37_c8() != null) {
						cell.setCellValue(record.getR37_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR37_total() != null) {
						cell.setCellValue(record.getR37_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);

					cell = row.getCell(1);
					if (record.getR38_usd() != null) {
						cell.setCellValue(record.getR38_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR38_zar() != null) {
						cell.setCellValue(record.getR38_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR38_gbp() != null) {
						cell.setCellValue(record.getR38_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR38_euro() != null) {
						cell.setCellValue(record.getR38_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR38_yen() != null) {
						cell.setCellValue(record.getR38_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR38_c6() != null) {
						cell.setCellValue(record.getR38_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR38_c7() != null) {
						cell.setCellValue(record.getR38_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR38_c8() != null) {
						cell.setCellValue(record.getR38_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR38_total() != null) {
						cell.setCellValue(record.getR38_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);

					cell = row.getCell(1);
					if (record.getR39_usd() != null) {
						cell.setCellValue(record.getR39_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR39_zar() != null) {
						cell.setCellValue(record.getR39_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR39_gbp() != null) {
						cell.setCellValue(record.getR39_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR39_euro() != null) {
						cell.setCellValue(record.getR39_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR39_yen() != null) {
						cell.setCellValue(record.getR39_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR39_c6() != null) {
						cell.setCellValue(record.getR39_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR39_c7() != null) {
						cell.setCellValue(record.getR39_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR39_c8() != null) {
						cell.setCellValue(record.getR39_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR39_total() != null) {
						cell.setCellValue(record.getR39_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row40
					row = sheet.getRow(39);

					cell = row.getCell(1);
					if (record.getR40_usd() != null) {
						cell.setCellValue(record.getR40_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR40_zar() != null) {
						cell.setCellValue(record.getR40_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR40_gbp() != null) {
						cell.setCellValue(record.getR40_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR40_euro() != null) {
						cell.setCellValue(record.getR40_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR40_yen() != null) {
						cell.setCellValue(record.getR40_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR40_c6() != null) {
						cell.setCellValue(record.getR40_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR40_c7() != null) {
						cell.setCellValue(record.getR40_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR40_c8() != null) {
						cell.setCellValue(record.getR40_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR40_total() != null) {
						cell.setCellValue(record.getR40_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);

					cell = row.getCell(1);
					if (record.getR41_usd() != null) {
						cell.setCellValue(record.getR41_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR41_zar() != null) {
						cell.setCellValue(record.getR41_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR41_gbp() != null) {
						cell.setCellValue(record.getR41_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR41_euro() != null) {
						cell.setCellValue(record.getR41_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR41_yen() != null) {
						cell.setCellValue(record.getR41_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR41_c6() != null) {
						cell.setCellValue(record.getR41_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR41_c7() != null) {
						cell.setCellValue(record.getR41_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR41_c8() != null) {
						cell.setCellValue(record.getR41_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR41_total() != null) {
						cell.setCellValue(record.getR41_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					cell = row.getCell(1);
					if (record.getR42_usd() != null) {
						cell.setCellValue(record.getR42_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR42_zar() != null) {
						cell.setCellValue(record.getR42_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR42_gbp() != null) {
						cell.setCellValue(record.getR42_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR42_euro() != null) {
						cell.setCellValue(record.getR42_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR42_yen() != null) {
						cell.setCellValue(record.getR42_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR42_c6() != null) {
						cell.setCellValue(record.getR42_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR42_c7() != null) {
						cell.setCellValue(record.getR42_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR42_c8() != null) {
						cell.setCellValue(record.getR42_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR42_total() != null) {
						cell.setCellValue(record.getR42_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					} // row43
					row = sheet.getRow(42);

					cell = row.getCell(1);
					if (record.getR43_usd() != null) {
						cell.setCellValue(record.getR43_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR43_zar() != null) {
						cell.setCellValue(record.getR43_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR43_gbp() != null) {
						cell.setCellValue(record.getR43_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR43_euro() != null) {
						cell.setCellValue(record.getR43_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR43_yen() != null) {
						cell.setCellValue(record.getR43_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR43_c6() != null) {
						cell.setCellValue(record.getR43_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR43_c7() != null) {
						cell.setCellValue(record.getR43_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR43_c8() != null) {
						cell.setCellValue(record.getR43_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR43_total() != null) {
						cell.setCellValue(record.getR43_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row44
					row = sheet.getRow(43);

					cell = row.getCell(1);
					if (record.getR44_usd() != null) {
						cell.setCellValue(record.getR44_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR44_zar() != null) {
						cell.setCellValue(record.getR44_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR44_gbp() != null) {
						cell.setCellValue(record.getR44_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR44_euro() != null) {
						cell.setCellValue(record.getR44_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR44_yen() != null) {
						cell.setCellValue(record.getR44_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR44_c6() != null) {
						cell.setCellValue(record.getR44_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR44_c7() != null) {
						cell.setCellValue(record.getR44_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR44_c8() != null) {
						cell.setCellValue(record.getR44_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR44_total() != null) {
						cell.setCellValue(record.getR44_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);

					cell = row.getCell(1);
					if (record.getR45_usd() != null) {
						cell.setCellValue(record.getR45_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR45_zar() != null) {
						cell.setCellValue(record.getR45_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR45_gbp() != null) {
						cell.setCellValue(record.getR45_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR45_euro() != null) {
						cell.setCellValue(record.getR45_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR45_yen() != null) {
						cell.setCellValue(record.getR45_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR45_c6() != null) {
						cell.setCellValue(record.getR45_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR45_c7() != null) {
						cell.setCellValue(record.getR45_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR45_c8() != null) {
						cell.setCellValue(record.getR45_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR45_total() != null) {
						cell.setCellValue(record.getR45_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row46
					row = sheet.getRow(45);

					cell = row.getCell(1);
					if (record.getR46_usd() != null) {
						cell.setCellValue(record.getR46_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR46_zar() != null) {
						cell.setCellValue(record.getR46_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR46_gbp() != null) {
						cell.setCellValue(record.getR46_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR46_euro() != null) {
						cell.setCellValue(record.getR46_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR46_yen() != null) {
						cell.setCellValue(record.getR46_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR46_c6() != null) {
						cell.setCellValue(record.getR46_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR46_c7() != null) {
						cell.setCellValue(record.getR46_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR46_c8() != null) {
						cell.setCellValue(record.getR46_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR46_total() != null) {
						cell.setCellValue(record.getR46_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					cell = row.getCell(1);
					if (record.getR47_usd() != null) {
						cell.setCellValue(record.getR47_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR47_zar() != null) {
						cell.setCellValue(record.getR47_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR47_gbp() != null) {
						cell.setCellValue(record.getR47_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR47_euro() != null) {
						cell.setCellValue(record.getR47_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR47_yen() != null) {
						cell.setCellValue(record.getR47_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR47_c6() != null) {
						cell.setCellValue(record.getR47_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR47_c7() != null) {
						cell.setCellValue(record.getR47_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR47_c8() != null) {
						cell.setCellValue(record.getR47_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR47_total() != null) {
						cell.setCellValue(record.getR47_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row48
					row = sheet.getRow(47);

					cell = row.getCell(1);
					if (record.getR48_usd() != null) {
						cell.setCellValue(record.getR48_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR48_zar() != null) {
						cell.setCellValue(record.getR48_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR48_gbp() != null) {
						cell.setCellValue(record.getR48_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR48_euro() != null) {
						cell.setCellValue(record.getR48_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR48_yen() != null) {
						cell.setCellValue(record.getR48_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR48_c6() != null) {
						cell.setCellValue(record.getR48_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR48_c7() != null) {
						cell.setCellValue(record.getR48_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR48_c8() != null) {
						cell.setCellValue(record.getR48_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR48_total() != null) {
						cell.setCellValue(record.getR48_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row49
					row = sheet.getRow(48);

					cell = row.getCell(1);
					if (record.getR49_usd() != null) {
						cell.setCellValue(record.getR49_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR49_zar() != null) {
						cell.setCellValue(record.getR49_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR49_gbp() != null) {
						cell.setCellValue(record.getR49_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR49_euro() != null) {
						cell.setCellValue(record.getR49_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR49_yen() != null) {
						cell.setCellValue(record.getR49_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR49_c6() != null) {
						cell.setCellValue(record.getR49_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR49_c7() != null) {
						cell.setCellValue(record.getR49_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR49_c8() != null) {
						cell.setCellValue(record.getR49_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR49_total() != null) {
						cell.setCellValue(record.getR49_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);

					cell = row.getCell(1);
					if (record.getR50_usd() != null) {
						cell.setCellValue(record.getR50_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR50_zar() != null) {
						cell.setCellValue(record.getR50_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR50_gbp() != null) {
						cell.setCellValue(record.getR50_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR50_euro() != null) {
						cell.setCellValue(record.getR50_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR50_yen() != null) {
						cell.setCellValue(record.getR50_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR50_c6() != null) {
						cell.setCellValue(record.getR50_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR50_c7() != null) {
						cell.setCellValue(record.getR50_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR50_c8() != null) {
						cell.setCellValue(record.getR50_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR50_total() != null) {
						cell.setCellValue(record.getR50_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					cell = row.getCell(1);
					if (record.getR51_usd() != null) {
						cell.setCellValue(record.getR51_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR51_zar() != null) {
						cell.setCellValue(record.getR51_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR51_gbp() != null) {
						cell.setCellValue(record.getR51_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR51_euro() != null) {
						cell.setCellValue(record.getR51_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR51_yen() != null) {
						cell.setCellValue(record.getR51_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR51_c6() != null) {
						cell.setCellValue(record.getR51_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR51_c7() != null) {
						cell.setCellValue(record.getR51_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR51_c8() != null) {
						cell.setCellValue(record.getR51_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR51_total() != null) {
						cell.setCellValue(record.getR51_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);

					cell = row.getCell(1);
					if (record.getR52_usd() != null) {
						cell.setCellValue(record.getR52_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR52_zar() != null) {
						cell.setCellValue(record.getR52_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR52_gbp() != null) {
						cell.setCellValue(record.getR52_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR52_euro() != null) {
						cell.setCellValue(record.getR52_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR52_yen() != null) {
						cell.setCellValue(record.getR52_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR52_c6() != null) {
						cell.setCellValue(record.getR52_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR52_c7() != null) {
						cell.setCellValue(record.getR52_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR52_c8() != null) {
						cell.setCellValue(record.getR52_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR52_total() != null) {
						cell.setCellValue(record.getR52_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);

					cell = row.getCell(1);
					if (record.getR53_usd() != null) {
						cell.setCellValue(record.getR53_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR53_zar() != null) {
						cell.setCellValue(record.getR53_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR53_gbp() != null) {
						cell.setCellValue(record.getR53_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR53_euro() != null) {
						cell.setCellValue(record.getR53_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR53_yen() != null) {
						cell.setCellValue(record.getR53_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR53_c6() != null) {
						cell.setCellValue(record.getR53_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR53_c7() != null) {
						cell.setCellValue(record.getR53_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR53_c8() != null) {
						cell.setCellValue(record.getR53_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR53_total() != null) {
						cell.setCellValue(record.getR53_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);

					cell = row.getCell(1);
					if (record.getR54_usd() != null) {
						cell.setCellValue(record.getR54_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR54_zar() != null) {
						cell.setCellValue(record.getR54_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR54_gbp() != null) {
						cell.setCellValue(record.getR54_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR54_euro() != null) {
						cell.setCellValue(record.getR54_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR54_yen() != null) {
						cell.setCellValue(record.getR54_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR54_c6() != null) {
						cell.setCellValue(record.getR54_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR54_c7() != null) {
						cell.setCellValue(record.getR54_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR54_c8() != null) {
						cell.setCellValue(record.getR54_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR54_total() != null) {
						cell.setCellValue(record.getR54_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					cell = row.getCell(1);
					if (record.getR55_usd() != null) {
						cell.setCellValue(record.getR55_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR55_zar() != null) {
						cell.setCellValue(record.getR55_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR55_gbp() != null) {
						cell.setCellValue(record.getR55_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR55_euro() != null) {
						cell.setCellValue(record.getR55_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR55_yen() != null) {
						cell.setCellValue(record.getR55_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR55_c6() != null) {
						cell.setCellValue(record.getR55_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR55_c7() != null) {
						cell.setCellValue(record.getR55_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR55_c8() != null) {
						cell.setCellValue(record.getR55_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR55_total() != null) {
						cell.setCellValue(record.getR55_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);

					cell = row.getCell(1);
					if (record.getR56_usd() != null) {
						cell.setCellValue(record.getR56_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR56_zar() != null) {
						cell.setCellValue(record.getR56_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR56_gbp() != null) {
						cell.setCellValue(record.getR56_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR56_euro() != null) {
						cell.setCellValue(record.getR56_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR56_yen() != null) {
						cell.setCellValue(record.getR56_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR56_c6() != null) {
						cell.setCellValue(record.getR56_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR56_c7() != null) {
						cell.setCellValue(record.getR56_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR56_c8() != null) {
						cell.setCellValue(record.getR56_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR56_total() != null) {
						cell.setCellValue(record.getR56_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row57
					row = sheet.getRow(56);

					cell = row.getCell(1);
					if (record.getR57_usd() != null) {
						cell.setCellValue(record.getR57_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR57_zar() != null) {
						cell.setCellValue(record.getR57_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR57_gbp() != null) {
						cell.setCellValue(record.getR57_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR57_euro() != null) {
						cell.setCellValue(record.getR57_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR57_yen() != null) {
						cell.setCellValue(record.getR57_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR57_c6() != null) {
						cell.setCellValue(record.getR57_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR57_c7() != null) {
						cell.setCellValue(record.getR57_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR57_c8() != null) {
						cell.setCellValue(record.getR57_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR57_total() != null) {
						cell.setCellValue(record.getR57_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					// row58
					row = sheet.getRow(57);

					cell = row.getCell(1);
					if (record.getR58_usd() != null) {
						cell.setCellValue(record.getR58_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR58_zar() != null) {
						cell.setCellValue(record.getR58_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR58_gbp() != null) {
						cell.setCellValue(record.getR58_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR58_euro() != null) {
						cell.setCellValue(record.getR58_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR58_yen() != null) {
						cell.setCellValue(record.getR58_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR58_c6() != null) {
						cell.setCellValue(record.getR58_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR58_c7() != null) {
						cell.setCellValue(record.getR58_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR58_c8() != null) {
						cell.setCellValue(record.getR58_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR58_total() != null) {
						cell.setCellValue(record.getR58_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					cell = row.getCell(1);
					if (record.getR59_usd() != null) {
						cell.setCellValue(record.getR59_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR59_zar() != null) {
						cell.setCellValue(record.getR59_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR59_gbp() != null) {
						cell.setCellValue(record.getR59_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR59_euro() != null) {
						cell.setCellValue(record.getR59_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR59_yen() != null) {
						cell.setCellValue(record.getR59_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR59_c6() != null) {
						cell.setCellValue(record.getR59_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR59_c7() != null) {
						cell.setCellValue(record.getR59_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR59_c8() != null) {
						cell.setCellValue(record.getR59_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR59_total() != null) {
						cell.setCellValue(record.getR59_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					cell = row.getCell(1);
					if (record.getR60_usd() != null) {
						cell.setCellValue(record.getR60_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR60_zar() != null) {
						cell.setCellValue(record.getR60_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR60_gbp() != null) {
						cell.setCellValue(record.getR60_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR60_euro() != null) {
						cell.setCellValue(record.getR60_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR60_yen() != null) {
						cell.setCellValue(record.getR60_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR60_c6() != null) {
						cell.setCellValue(record.getR60_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR60_c7() != null) {
						cell.setCellValue(record.getR60_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR60_c8() != null) {
						cell.setCellValue(record.getR60_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR60_total() != null) {
						cell.setCellValue(record.getR60_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);

					cell = row.getCell(1);
					if (record.getR61_usd() != null) {
						cell.setCellValue(record.getR61_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR61_zar() != null) {
						cell.setCellValue(record.getR61_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR61_gbp() != null) {
						cell.setCellValue(record.getR61_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR61_euro() != null) {
						cell.setCellValue(record.getR61_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR61_yen() != null) {
						cell.setCellValue(record.getR61_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR61_c6() != null) {
						cell.setCellValue(record.getR61_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR61_c7() != null) {
						cell.setCellValue(record.getR61_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR61_c8() != null) {
						cell.setCellValue(record.getR61_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR61_total() != null) {
						cell.setCellValue(record.getR61_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);

					cell = row.getCell(1);
					if (record.getR62_usd() != null) {
						cell.setCellValue(record.getR62_usd().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR62_zar() != null) {
						cell.setCellValue(record.getR62_zar().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR62_gbp() != null) {
						cell.setCellValue(record.getR62_gbp().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR62_euro() != null) {
						cell.setCellValue(record.getR62_euro().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR62_yen() != null) {
						cell.setCellValue(record.getR62_yen().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR62_c6() != null) {
						cell.setCellValue(record.getR62_c6().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR62_c7() != null) {
						cell.setCellValue(record.getR62_c7().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR62_c8() != null) {
						cell.setCellValue(record.getR62_c8().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR62_total() != null) {
						cell.setCellValue(record.getR62_total().doubleValue());
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA5 EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_LA5_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}
}