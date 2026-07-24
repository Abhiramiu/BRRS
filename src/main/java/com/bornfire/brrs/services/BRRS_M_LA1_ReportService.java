package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
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

import com.bornfire.brrs.dto.ReportLineItemDTO;
import com.bornfire.brrs.entities.UserProfileRep;



@Service
@Transactional
public class BRRS_M_LA1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LA1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// ENTITY MANAGER (Acts like Repository)
	@PersistenceContext
	private EntityManager entityManager;

	// Fetch data by report date
	public List<M_LA1_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_LA1_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_LA1RowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getM_LA1Archival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_M_LA1_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	// GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<M_LA1_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_LA1_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_LA1ArchivalRowMapper());
	}
	// GET ALL WITH VERSION

	public List<M_LA1_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_M_LA1_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_LA1ArchivalRowMapper());
	}

	// GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_LA1_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_M_LA1_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}

	// 1. BY DATE + LABEL + CRITERIA

	public List<M_LA1_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_M_LA1_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new M_LA1DetaillRowMapper());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<M_LA1_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA1_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_LA1DetaillRowMapper());
	}

	// 3. PAGINATION

	public List<M_LA1_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_M_LA1_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new M_LA1DetaillRowMapper());
	}

	// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_M_LA1_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<M_LA1_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA1_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_LA1DetaillRowMapper());
	}
	// 6. BY ACCOUNT NUMBER

	public M_LA1_Detail_Entity findByAcctnumber(String acctNumber) {

		String sql = "SELECT * FROM BRRS_M_LA1_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new M_LA1DetaillRowMapper());
	}

	// 1. GET BY DATE + VERSION

	public List<M_LA1_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
			String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_M_LA1_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
				new M_LA1ArchivalDetaillRowMapper());
	}

	// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<M_LA1_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			String reportAddlCriteria2, String reportAddlCriteria3, Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA1_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_ADDL_CRITERIA_2 = ? AND REPORT_ADDL_CRITERIA_3 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql,
				new Object[] { reportLabel, reportAddlCriteria1, reportAddlCriteria2, reportAddlCriteria3, reportdate },
				new M_LA1DetaillRowMapper());
	}
	
	
	public List<M_LA1_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String report_label,
			String report_addl_criteria_1, String report_addl_criteria_2, String report_addl_criteria_3,
			Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_M_LA1_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_ADDL_CRITERIA_2= ? "
				+ "AND REPORT_ADDL_CRITERIA_3 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate
				.query(sql,
						new Object[] { report_label, report_addl_criteria_1, report_addl_criteria_2,
								report_addl_criteria_3, reportdate, dataEntryVersion },
						new M_LA1ArchivalDetaillRowMapper());
	}

	
	public M_LA1_Detail_Entity findBySno(String sno) {

		String sql = "SELECT * FROM BRRS_M_LA1_DETAILTABLE WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new M_LA1DetaillRowMapper());
	}

	public M_LA1_Detail_Entity findBySnoArch(String sno) {
		return findBySnoArch(sno, null);
	}

	public M_LA1_Detail_Entity findBySnoArch(String sno, String version) {
		String sql;
		Object[] args;
		if (version != null && !version.trim().isEmpty()) {
			sql = "SELECT * FROM BRRS_M_LA1_ARCHIVALTABLE_DETAIL WHERE SNO = ? AND DATA_ENTRY_VERSION = ?";
			args = new Object[] { sno, version };
		} else {
			sql = "SELECT * FROM BRRS_M_LA1_ARCHIVALTABLE_DETAIL WHERE SNO = ? ORDER BY DATA_ENTRY_VERSION DESC";
			args = new Object[] { sno };
		}
		List<M_LA1_Detail_Entity> list = jdbcTemplate.query(sql, args, new M_LA1DetaillRowMapper());
		return list.isEmpty() ? null : list.get(0);
	}

	// ROW MAPPER

	class M_LA1RowMapper implements RowMapper<M_LA1_Summary_Entity> {

		@Override
		public M_LA1_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA1_Summary_Entity obj = new M_LA1_Summary_Entity();

			obj.setR11_product(rs.getString("r11_product"));
			obj.setR11_approved_limit(rs.getBigDecimal("r11_approved_limit"));
			obj.setR11_balance_outstanding(rs.getBigDecimal("r11_balance_outstanding"));
			obj.setR11_no_of_acct(rs.getBigDecimal("r11_no_of_acct"));

			obj.setR12_product(rs.getString("r12_product"));
			obj.setR12_approved_limit(rs.getBigDecimal("r12_approved_limit"));
			obj.setR12_balance_outstanding(rs.getBigDecimal("r12_balance_outstanding"));
			obj.setR12_no_of_acct(rs.getBigDecimal("r12_no_of_acct"));

			obj.setR13_product(rs.getString("r13_product"));
			obj.setR13_approved_limit(rs.getBigDecimal("r13_approved_limit"));
			obj.setR13_balance_outstanding(rs.getBigDecimal("r13_balance_outstanding"));
			obj.setR13_no_of_acct(rs.getBigDecimal("r13_no_of_acct"));

			obj.setR14_product(rs.getString("r14_product"));
			obj.setR14_approved_limit(rs.getBigDecimal("r14_approved_limit"));
			obj.setR14_balance_outstanding(rs.getBigDecimal("r14_balance_outstanding"));
			obj.setR14_no_of_acct(rs.getBigDecimal("r14_no_of_acct"));

			obj.setR15_product(rs.getString("r15_product"));
			obj.setR15_approved_limit(rs.getBigDecimal("r15_approved_limit"));
			obj.setR15_balance_outstanding(rs.getBigDecimal("r15_balance_outstanding"));
			obj.setR15_no_of_acct(rs.getBigDecimal("r15_no_of_acct"));

			obj.setR16_product(rs.getString("r16_product"));
			obj.setR16_approved_limit(rs.getBigDecimal("r16_approved_limit"));
			obj.setR16_balance_outstanding(rs.getBigDecimal("r16_balance_outstanding"));
			obj.setR16_no_of_acct(rs.getBigDecimal("r16_no_of_acct"));

			obj.setR17_product(rs.getString("r17_product"));
			obj.setR17_approved_limit(rs.getBigDecimal("r17_approved_limit"));
			obj.setR17_balance_outstanding(rs.getBigDecimal("r17_balance_outstanding"));
			obj.setR17_no_of_acct(rs.getBigDecimal("r17_no_of_acct"));

			obj.setR18_product(rs.getString("r18_product"));
			obj.setR18_approved_limit(rs.getBigDecimal("r18_approved_limit"));
			obj.setR18_balance_outstanding(rs.getBigDecimal("r18_balance_outstanding"));
			obj.setR18_no_of_acct(rs.getBigDecimal("r18_no_of_acct"));

			obj.setR19_product(rs.getString("r19_product"));
			obj.setR19_approved_limit(rs.getBigDecimal("r19_approved_limit"));
			obj.setR19_balance_outstanding(rs.getBigDecimal("r19_balance_outstanding"));
			obj.setR19_no_of_acct(rs.getBigDecimal("r19_no_of_acct"));

			obj.setR20_product(rs.getString("r20_product"));
			obj.setR20_approved_limit(rs.getBigDecimal("r20_approved_limit"));
			obj.setR20_balance_outstanding(rs.getBigDecimal("r20_balance_outstanding"));
			obj.setR20_no_of_acct(rs.getBigDecimal("r20_no_of_acct"));

			obj.setR21_product(rs.getString("r21_product"));
			obj.setR21_approved_limit(rs.getBigDecimal("r21_approved_limit"));
			obj.setR21_balance_outstanding(rs.getBigDecimal("r21_balance_outstanding"));
			obj.setR21_no_of_acct(rs.getBigDecimal("r21_no_of_acct"));

			obj.setR22_product(rs.getString("r22_product"));
			obj.setR22_approved_limit(rs.getBigDecimal("r22_approved_limit"));
			obj.setR22_balance_outstanding(rs.getBigDecimal("r22_balance_outstanding"));
			obj.setR22_no_of_acct(rs.getBigDecimal("r22_no_of_acct"));

			obj.setR23_product(rs.getString("r23_product"));
			obj.setR23_approved_limit(rs.getBigDecimal("r23_approved_limit"));
			obj.setR23_balance_outstanding(rs.getBigDecimal("r23_balance_outstanding"));
			obj.setR23_no_of_acct(rs.getBigDecimal("r23_no_of_acct"));

			obj.setR24_product(rs.getString("r24_product"));
			obj.setR24_approved_limit(rs.getBigDecimal("r24_approved_limit"));
			obj.setR24_balance_outstanding(rs.getBigDecimal("r24_balance_outstanding"));
			obj.setR24_no_of_acct(rs.getBigDecimal("r24_no_of_acct"));

			obj.setR25_product(rs.getString("r25_product"));
			obj.setR25_approved_limit(rs.getBigDecimal("r25_approved_limit"));
			obj.setR25_balance_outstanding(rs.getBigDecimal("r25_balance_outstanding"));
			obj.setR25_no_of_acct(rs.getBigDecimal("r25_no_of_acct"));

			obj.setR26_product(rs.getString("r26_product"));
			obj.setR26_approved_limit(rs.getBigDecimal("r26_approved_limit"));
			obj.setR26_balance_outstanding(rs.getBigDecimal("r26_balance_outstanding"));
			obj.setR26_no_of_acct(rs.getBigDecimal("r26_no_of_acct"));

			obj.setR27_product(rs.getString("r27_product"));
			obj.setR27_approved_limit(rs.getBigDecimal("r27_approved_limit"));
			obj.setR27_balance_outstanding(rs.getBigDecimal("r27_balance_outstanding"));
			obj.setR27_no_of_acct(rs.getBigDecimal("r27_no_of_acct"));

			obj.setR28_product(rs.getString("r28_product"));
			obj.setR28_approved_limit(rs.getBigDecimal("r28_approved_limit"));
			obj.setR28_balance_outstanding(rs.getBigDecimal("r28_balance_outstanding"));
			obj.setR28_no_of_acct(rs.getBigDecimal("r28_no_of_acct"));

			obj.setR29_product(rs.getString("r29_product"));
			obj.setR29_approved_limit(rs.getBigDecimal("r29_approved_limit"));
			obj.setR29_balance_outstanding(rs.getBigDecimal("r29_balance_outstanding"));
			obj.setR29_no_of_acct(rs.getBigDecimal("r29_no_of_acct"));

			obj.setR30_product(rs.getString("r30_product"));
			obj.setR30_approved_limit(rs.getBigDecimal("r30_approved_limit"));
			obj.setR30_balance_outstanding(rs.getBigDecimal("r30_balance_outstanding"));
			obj.setR30_no_of_acct(rs.getBigDecimal("r30_no_of_acct"));

			obj.setR31_product(rs.getString("r31_product"));
			obj.setR31_approved_limit(rs.getBigDecimal("r31_approved_limit"));
			obj.setR31_balance_outstanding(rs.getBigDecimal("r31_balance_outstanding"));
			obj.setR31_no_of_acct(rs.getBigDecimal("r31_no_of_acct"));

			obj.setR32_product(rs.getString("r32_product"));
			obj.setR32_approved_limit(rs.getBigDecimal("r32_approved_limit"));
			obj.setR32_balance_outstanding(rs.getBigDecimal("r32_balance_outstanding"));
			obj.setR32_no_of_acct(rs.getBigDecimal("r32_no_of_acct"));

			obj.setR33_product(rs.getString("r33_product"));
			obj.setR33_approved_limit(rs.getBigDecimal("r33_approved_limit"));
			obj.setR33_balance_outstanding(rs.getBigDecimal("r33_balance_outstanding"));
			obj.setR33_no_of_acct(rs.getBigDecimal("r33_no_of_acct"));

			obj.setR34_product(rs.getString("r34_product"));
			obj.setR34_approved_limit(rs.getBigDecimal("r34_approved_limit"));
			obj.setR34_balance_outstanding(rs.getBigDecimal("r34_balance_outstanding"));
			obj.setR34_no_of_acct(rs.getBigDecimal("r34_no_of_acct"));

			obj.setR35_product(rs.getString("r35_product"));
			obj.setR35_approved_limit(rs.getBigDecimal("r35_approved_limit"));
			obj.setR35_balance_outstanding(rs.getBigDecimal("r35_balance_outstanding"));
			obj.setR35_no_of_acct(rs.getBigDecimal("r35_no_of_acct"));

			obj.setR36_product(rs.getString("r36_product"));
			obj.setR36_approved_limit(rs.getBigDecimal("r36_approved_limit"));
			obj.setR36_balance_outstanding(rs.getBigDecimal("r36_balance_outstanding"));
			obj.setR36_no_of_acct(rs.getBigDecimal("r36_no_of_acct"));

			obj.setR37_product(rs.getString("r37_product"));
			obj.setR37_approved_limit(rs.getBigDecimal("r37_approved_limit"));
			obj.setR37_balance_outstanding(rs.getBigDecimal("r37_balance_outstanding"));
			obj.setR37_no_of_acct(rs.getBigDecimal("r37_no_of_acct"));

			obj.setR38_product(rs.getString("r38_product"));
			obj.setR38_approved_limit(rs.getBigDecimal("r38_approved_limit"));
			obj.setR38_balance_outstanding(rs.getBigDecimal("r38_balance_outstanding"));
			obj.setR38_no_of_acct(rs.getBigDecimal("r38_no_of_acct"));

			obj.setR39_product(rs.getString("r39_product"));
			obj.setR39_approved_limit(rs.getBigDecimal("r39_approved_limit"));
			obj.setR39_balance_outstanding(rs.getBigDecimal("r39_balance_outstanding"));
			obj.setR39_no_of_acct(rs.getBigDecimal("r39_no_of_acct"));

			obj.setR40_product(rs.getString("r40_product"));
			obj.setR40_approved_limit(rs.getBigDecimal("r40_approved_limit"));
			obj.setR40_balance_outstanding(rs.getBigDecimal("r40_balance_outstanding"));
			obj.setR40_no_of_acct(rs.getBigDecimal("r40_no_of_acct"));

			obj.setR41_product(rs.getString("r41_product"));
			obj.setR41_approved_limit(rs.getBigDecimal("r41_approved_limit"));
			obj.setR41_balance_outstanding(rs.getBigDecimal("r41_balance_outstanding"));
			obj.setR41_no_of_acct(rs.getBigDecimal("r41_no_of_acct"));

			obj.setR42_product(rs.getString("r42_product"));
			obj.setR42_approved_limit(rs.getBigDecimal("r42_approved_limit"));
			obj.setR42_balance_outstanding(rs.getBigDecimal("r42_balance_outstanding"));
			obj.setR42_no_of_acct(rs.getBigDecimal("r42_no_of_acct"));

			obj.setR43_product(rs.getString("r43_product"));
			obj.setR43_approved_limit(rs.getBigDecimal("r43_approved_limit"));
			obj.setR43_balance_outstanding(rs.getBigDecimal("r43_balance_outstanding"));
			obj.setR43_no_of_acct(rs.getBigDecimal("r43_no_of_acct"));

			obj.setR44_product(rs.getString("r44_product"));
			obj.setR44_approved_limit(rs.getBigDecimal("r44_approved_limit"));
			obj.setR44_balance_outstanding(rs.getBigDecimal("r44_balance_outstanding"));
			obj.setR44_no_of_acct(rs.getBigDecimal("r44_no_of_acct"));

			obj.setR45_product(rs.getString("r45_product"));
			obj.setR45_approved_limit(rs.getBigDecimal("r45_approved_limit"));
			obj.setR45_balance_outstanding(rs.getBigDecimal("r45_balance_outstanding"));
			obj.setR45_no_of_acct(rs.getBigDecimal("r45_no_of_acct"));

			obj.setR46_product(rs.getString("r46_product"));
			obj.setR46_approved_limit(rs.getBigDecimal("r46_approved_limit"));
			obj.setR46_balance_outstanding(rs.getBigDecimal("r46_balance_outstanding"));
			obj.setR46_no_of_acct(rs.getBigDecimal("r46_no_of_acct"));

			obj.setR47_product(rs.getString("r47_product"));
			obj.setR47_approved_limit(rs.getBigDecimal("r47_approved_limit"));
			obj.setR47_balance_outstanding(rs.getBigDecimal("r47_balance_outstanding"));
			obj.setR47_no_of_acct(rs.getBigDecimal("r47_no_of_acct"));

			obj.setR48_product(rs.getString("r48_product"));
			obj.setR48_approved_limit(rs.getBigDecimal("r48_approved_limit"));
			obj.setR48_balance_outstanding(rs.getBigDecimal("r48_balance_outstanding"));
			obj.setR48_no_of_acct(rs.getBigDecimal("r48_no_of_acct"));

			obj.setR49_product(rs.getString("r49_product"));
			obj.setR49_approved_limit(rs.getBigDecimal("r49_approved_limit"));
			obj.setR49_balance_outstanding(rs.getBigDecimal("r49_balance_outstanding"));
			obj.setR49_no_of_acct(rs.getBigDecimal("r49_no_of_acct"));

			obj.setR50_product(rs.getString("r50_product"));
			obj.setR50_approved_limit(rs.getBigDecimal("r50_approved_limit"));
			obj.setR50_balance_outstanding(rs.getBigDecimal("r50_balance_outstanding"));
			obj.setR50_no_of_acct(rs.getBigDecimal("r50_no_of_acct"));

			obj.setR51_product(rs.getString("r51_product"));
			obj.setR51_approved_limit(rs.getBigDecimal("r51_approved_limit"));
			obj.setR51_balance_outstanding(rs.getBigDecimal("r51_balance_outstanding"));
			obj.setR51_no_of_acct(rs.getBigDecimal("r51_no_of_acct"));

			obj.setR52_product(rs.getString("r52_product"));
			obj.setR52_approved_limit(rs.getBigDecimal("r52_approved_limit"));
			obj.setR52_balance_outstanding(rs.getBigDecimal("r52_balance_outstanding"));
			obj.setR52_no_of_acct(rs.getBigDecimal("r52_no_of_acct"));

			obj.setR53_product(rs.getString("r53_product"));
			obj.setR53_approved_limit(rs.getBigDecimal("r53_approved_limit"));
			obj.setR53_balance_outstanding(rs.getBigDecimal("r53_balance_outstanding"));
			obj.setR53_no_of_acct(rs.getBigDecimal("r53_no_of_acct"));

			obj.setR54_product(rs.getString("r54_product"));
			obj.setR54_approved_limit(rs.getBigDecimal("r54_approved_limit"));
			obj.setR54_balance_outstanding(rs.getBigDecimal("r54_balance_outstanding"));
			obj.setR54_no_of_acct(rs.getBigDecimal("r54_no_of_acct"));

			obj.setR55_product(rs.getString("r55_product"));
			obj.setR55_approved_limit(rs.getBigDecimal("r55_approved_limit"));
			obj.setR55_balance_outstanding(rs.getBigDecimal("r55_balance_outstanding"));
			obj.setR55_no_of_acct(rs.getBigDecimal("r55_no_of_acct"));

			obj.setR56_product(rs.getString("r56_product"));
			obj.setR56_approved_limit(rs.getBigDecimal("r56_approved_limit"));
			obj.setR56_balance_outstanding(rs.getBigDecimal("r56_balance_outstanding"));
			obj.setR56_no_of_acct(rs.getBigDecimal("r56_no_of_acct"));

			obj.setR57_product(rs.getString("r57_product"));
			obj.setR57_approved_limit(rs.getBigDecimal("r57_approved_limit"));
			obj.setR57_balance_outstanding(rs.getBigDecimal("r57_balance_outstanding"));
			obj.setR57_no_of_acct(rs.getBigDecimal("r57_no_of_acct"));

			obj.setR58_product(rs.getString("r58_product"));
			obj.setR58_approved_limit(rs.getBigDecimal("r58_approved_limit"));
			obj.setR58_balance_outstanding(rs.getBigDecimal("r58_balance_outstanding"));
			obj.setR58_no_of_acct(rs.getBigDecimal("r58_no_of_acct"));

			obj.setR59_product(rs.getString("r59_product"));
			obj.setR59_approved_limit(rs.getBigDecimal("r59_approved_limit"));
			obj.setR59_balance_outstanding(rs.getBigDecimal("r59_balance_outstanding"));
			obj.setR59_no_of_acct(rs.getBigDecimal("r59_no_of_acct"));

			obj.setR60_product(rs.getString("r60_product"));
			obj.setR60_approved_limit(rs.getBigDecimal("r60_approved_limit"));
			obj.setR60_balance_outstanding(rs.getBigDecimal("r60_balance_outstanding"));
			obj.setR60_no_of_acct(rs.getBigDecimal("r60_no_of_acct"));

			obj.setR61_product(rs.getString("r61_product"));
			obj.setR61_approved_limit(rs.getBigDecimal("r61_approved_limit"));
			obj.setR61_balance_outstanding(rs.getBigDecimal("r61_balance_outstanding"));
			obj.setR61_no_of_acct(rs.getBigDecimal("r61_no_of_acct"));

			obj.setR62_product(rs.getString("r62_product"));
			obj.setR62_approved_limit(rs.getBigDecimal("r62_approved_limit"));
			obj.setR62_balance_outstanding(rs.getBigDecimal("r62_balance_outstanding"));
			obj.setR62_no_of_acct(rs.getBigDecimal("r62_no_of_acct"));

			obj.setR63_product(rs.getString("r63_product"));
			obj.setR63_approved_limit(rs.getBigDecimal("r63_approved_limit"));
			obj.setR63_balance_outstanding(rs.getBigDecimal("r63_balance_outstanding"));
			obj.setR63_no_of_acct(rs.getBigDecimal("r63_no_of_acct"));

			obj.setR64_product(rs.getString("r64_product"));
			obj.setR64_approved_limit(rs.getBigDecimal("r64_approved_limit"));
			obj.setR64_balance_outstanding(rs.getBigDecimal("r64_balance_outstanding"));
			obj.setR64_no_of_acct(rs.getBigDecimal("r64_no_of_acct"));

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

	public static class M_LA1_Summary_Entity {

		private String r11_product;
		private BigDecimal r11_approved_limit;
		private BigDecimal r11_balance_outstanding;
		private BigDecimal r11_no_of_acct;

		private String r12_product;
		private BigDecimal r12_approved_limit;
		private BigDecimal r12_balance_outstanding;
		private BigDecimal r12_no_of_acct;

		private String r13_product;
		private BigDecimal r13_approved_limit;
		private BigDecimal r13_balance_outstanding;
		private BigDecimal r13_no_of_acct;

		private String r14_product;
		private BigDecimal r14_approved_limit;
		private BigDecimal r14_balance_outstanding;
		private BigDecimal r14_no_of_acct;

		private String r15_product;
		private BigDecimal r15_approved_limit;
		private BigDecimal r15_balance_outstanding;
		private BigDecimal r15_no_of_acct;

		private String r16_product;
		private BigDecimal r16_approved_limit;
		private BigDecimal r16_balance_outstanding;
		private BigDecimal r16_no_of_acct;

		private String r17_product;
		private BigDecimal r17_approved_limit;
		private BigDecimal r17_balance_outstanding;
		private BigDecimal r17_no_of_acct;

		private String r18_product;
		private BigDecimal r18_approved_limit;
		private BigDecimal r18_balance_outstanding;
		private BigDecimal r18_no_of_acct;

		private String r19_product;
		private BigDecimal r19_approved_limit;
		private BigDecimal r19_balance_outstanding;
		private BigDecimal r19_no_of_acct;

		private String r20_product;
		private BigDecimal r20_approved_limit;
		private BigDecimal r20_balance_outstanding;
		private BigDecimal r20_no_of_acct;

		private String r21_product;
		private BigDecimal r21_approved_limit;
		private BigDecimal r21_balance_outstanding;
		private BigDecimal r21_no_of_acct;

		private String r22_product;
		private BigDecimal r22_approved_limit;
		private BigDecimal r22_balance_outstanding;
		private BigDecimal r22_no_of_acct;

		private String r23_product;
		private BigDecimal r23_approved_limit;
		private BigDecimal r23_balance_outstanding;
		private BigDecimal r23_no_of_acct;

		private String r24_product;
		private BigDecimal r24_approved_limit;
		private BigDecimal r24_balance_outstanding;
		private BigDecimal r24_no_of_acct;

		private String r25_product;
		private BigDecimal r25_approved_limit;
		private BigDecimal r25_balance_outstanding;
		private BigDecimal r25_no_of_acct;

		private String r26_product;
		private BigDecimal r26_approved_limit;
		private BigDecimal r26_balance_outstanding;
		private BigDecimal r26_no_of_acct;

		private String r27_product;
		private BigDecimal r27_approved_limit;
		private BigDecimal r27_balance_outstanding;
		private BigDecimal r27_no_of_acct;

		private String r28_product;
		private BigDecimal r28_approved_limit;
		private BigDecimal r28_balance_outstanding;
		private BigDecimal r28_no_of_acct;

		private String r29_product;
		private BigDecimal r29_approved_limit;
		private BigDecimal r29_balance_outstanding;
		private BigDecimal r29_no_of_acct;

		private String r30_product;
		private BigDecimal r30_approved_limit;
		private BigDecimal r30_balance_outstanding;
		private BigDecimal r30_no_of_acct;

		private String r31_product;
		private BigDecimal r31_approved_limit;
		private BigDecimal r31_balance_outstanding;
		private BigDecimal r31_no_of_acct;

		private String r32_product;
		private BigDecimal r32_approved_limit;
		private BigDecimal r32_balance_outstanding;
		private BigDecimal r32_no_of_acct;

		private String r33_product;
		private BigDecimal r33_approved_limit;
		private BigDecimal r33_balance_outstanding;
		private BigDecimal r33_no_of_acct;

		private String r34_product;
		private BigDecimal r34_approved_limit;
		private BigDecimal r34_balance_outstanding;
		private BigDecimal r34_no_of_acct;

		private String r35_product;
		private BigDecimal r35_approved_limit;
		private BigDecimal r35_balance_outstanding;
		private BigDecimal r35_no_of_acct;

		private String r36_product;
		private BigDecimal r36_approved_limit;
		private BigDecimal r36_balance_outstanding;
		private BigDecimal r36_no_of_acct;

		private String r37_product;
		private BigDecimal r37_approved_limit;
		private BigDecimal r37_balance_outstanding;
		private BigDecimal r37_no_of_acct;

		private String r38_product;
		private BigDecimal r38_approved_limit;
		private BigDecimal r38_balance_outstanding;
		private BigDecimal r38_no_of_acct;

		private String r39_product;
		private BigDecimal r39_approved_limit;
		private BigDecimal r39_balance_outstanding;
		private BigDecimal r39_no_of_acct;

		private String r40_product;
		private BigDecimal r40_approved_limit;
		private BigDecimal r40_balance_outstanding;
		private BigDecimal r40_no_of_acct;

		private String r41_product;
		private BigDecimal r41_approved_limit;
		private BigDecimal r41_balance_outstanding;
		private BigDecimal r41_no_of_acct;

		private String r42_product;
		private BigDecimal r42_approved_limit;
		private BigDecimal r42_balance_outstanding;
		private BigDecimal r42_no_of_acct;

		private String r43_product;
		private BigDecimal r43_approved_limit;
		private BigDecimal r43_balance_outstanding;
		private BigDecimal r43_no_of_acct;

		private String r44_product;
		private BigDecimal r44_approved_limit;
		private BigDecimal r44_balance_outstanding;
		private BigDecimal r44_no_of_acct;

		private String r45_product;
		private BigDecimal r45_approved_limit;
		private BigDecimal r45_balance_outstanding;
		private BigDecimal r45_no_of_acct;

		private String r46_product;
		private BigDecimal r46_approved_limit;
		private BigDecimal r46_balance_outstanding;
		private BigDecimal r46_no_of_acct;

		private String r47_product;
		private BigDecimal r47_approved_limit;
		private BigDecimal r47_balance_outstanding;
		private BigDecimal r47_no_of_acct;

		private String r48_product;
		private BigDecimal r48_approved_limit;
		private BigDecimal r48_balance_outstanding;
		private BigDecimal r48_no_of_acct;

		private String r49_product;
		private BigDecimal r49_approved_limit;
		private BigDecimal r49_balance_outstanding;
		private BigDecimal r49_no_of_acct;

		private String r50_product;
		private BigDecimal r50_approved_limit;
		private BigDecimal r50_balance_outstanding;
		private BigDecimal r50_no_of_acct;

		private String r51_product;
		private BigDecimal r51_approved_limit;
		private BigDecimal r51_balance_outstanding;
		private BigDecimal r51_no_of_acct;

		private String r52_product;
		private BigDecimal r52_approved_limit;
		private BigDecimal r52_balance_outstanding;
		private BigDecimal r52_no_of_acct;

		private String r53_product;
		private BigDecimal r53_approved_limit;
		private BigDecimal r53_balance_outstanding;
		private BigDecimal r53_no_of_acct;

		private String r54_product;
		private BigDecimal r54_approved_limit;
		private BigDecimal r54_balance_outstanding;
		private BigDecimal r54_no_of_acct;

		private String r55_product;
		private BigDecimal r55_approved_limit;
		private BigDecimal r55_balance_outstanding;
		private BigDecimal r55_no_of_acct;

		private String r56_product;
		private BigDecimal r56_approved_limit;
		private BigDecimal r56_balance_outstanding;
		private BigDecimal r56_no_of_acct;

		private String r57_product;
		private BigDecimal r57_approved_limit;
		private BigDecimal r57_balance_outstanding;
		private BigDecimal r57_no_of_acct;

		private String r58_product;
		private BigDecimal r58_approved_limit;
		private BigDecimal r58_balance_outstanding;
		private BigDecimal r58_no_of_acct;

		private String r59_product;
		private BigDecimal r59_approved_limit;
		private BigDecimal r59_balance_outstanding;
		private BigDecimal r59_no_of_acct;

		private String r60_product;
		private BigDecimal r60_approved_limit;
		private BigDecimal r60_balance_outstanding;
		private BigDecimal r60_no_of_acct;

		private String r61_product;
		private BigDecimal r61_approved_limit;
		private BigDecimal r61_balance_outstanding;
		private BigDecimal r61_no_of_acct;

		private String r62_product;
		private BigDecimal r62_approved_limit;
		private BigDecimal r62_balance_outstanding;
		private BigDecimal r62_no_of_acct;

		private String r63_product;
		private BigDecimal r63_approved_limit;
		private BigDecimal r63_balance_outstanding;
		private BigDecimal r63_no_of_acct;

		private String r64_product;
		private BigDecimal r64_approved_limit;
		private BigDecimal r64_balance_outstanding;
		private BigDecimal r64_no_of_acct;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

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

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_approved_limit() {
			return r11_approved_limit;
		}

		public void setR11_approved_limit(BigDecimal r11_approved_limit) {
			this.r11_approved_limit = r11_approved_limit;
		}

		public BigDecimal getR11_balance_outstanding() {
			return r11_balance_outstanding;
		}

		public void setR11_balance_outstanding(BigDecimal r11_balance_outstanding) {
			this.r11_balance_outstanding = r11_balance_outstanding;
		}

		public BigDecimal getR11_no_of_acct() {
			return r11_no_of_acct;
		}

		public void setR11_no_of_acct(BigDecimal r11_no_of_acct) {
			this.r11_no_of_acct = r11_no_of_acct;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_approved_limit() {
			return r12_approved_limit;
		}

		public void setR12_approved_limit(BigDecimal r12_approved_limit) {
			this.r12_approved_limit = r12_approved_limit;
		}

		public BigDecimal getR12_balance_outstanding() {
			return r12_balance_outstanding;
		}

		public void setR12_balance_outstanding(BigDecimal r12_balance_outstanding) {
			this.r12_balance_outstanding = r12_balance_outstanding;
		}

		public BigDecimal getR12_no_of_acct() {
			return r12_no_of_acct;
		}

		public void setR12_no_of_acct(BigDecimal r12_no_of_acct) {
			this.r12_no_of_acct = r12_no_of_acct;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_approved_limit() {
			return r13_approved_limit;
		}

		public void setR13_approved_limit(BigDecimal r13_approved_limit) {
			this.r13_approved_limit = r13_approved_limit;
		}

		public BigDecimal getR13_balance_outstanding() {
			return r13_balance_outstanding;
		}

		public void setR13_balance_outstanding(BigDecimal r13_balance_outstanding) {
			this.r13_balance_outstanding = r13_balance_outstanding;
		}

		public BigDecimal getR13_no_of_acct() {
			return r13_no_of_acct;
		}

		public void setR13_no_of_acct(BigDecimal r13_no_of_acct) {
			this.r13_no_of_acct = r13_no_of_acct;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_approved_limit() {
			return r14_approved_limit;
		}

		public void setR14_approved_limit(BigDecimal r14_approved_limit) {
			this.r14_approved_limit = r14_approved_limit;
		}

		public BigDecimal getR14_balance_outstanding() {
			return r14_balance_outstanding;
		}

		public void setR14_balance_outstanding(BigDecimal r14_balance_outstanding) {
			this.r14_balance_outstanding = r14_balance_outstanding;
		}

		public BigDecimal getR14_no_of_acct() {
			return r14_no_of_acct;
		}

		public void setR14_no_of_acct(BigDecimal r14_no_of_acct) {
			this.r14_no_of_acct = r14_no_of_acct;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_approved_limit() {
			return r15_approved_limit;
		}

		public void setR15_approved_limit(BigDecimal r15_approved_limit) {
			this.r15_approved_limit = r15_approved_limit;
		}

		public BigDecimal getR15_balance_outstanding() {
			return r15_balance_outstanding;
		}

		public void setR15_balance_outstanding(BigDecimal r15_balance_outstanding) {
			this.r15_balance_outstanding = r15_balance_outstanding;
		}

		public BigDecimal getR15_no_of_acct() {
			return r15_no_of_acct;
		}

		public void setR15_no_of_acct(BigDecimal r15_no_of_acct) {
			this.r15_no_of_acct = r15_no_of_acct;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_approved_limit() {
			return r16_approved_limit;
		}

		public void setR16_approved_limit(BigDecimal r16_approved_limit) {
			this.r16_approved_limit = r16_approved_limit;
		}

		public BigDecimal getR16_balance_outstanding() {
			return r16_balance_outstanding;
		}

		public void setR16_balance_outstanding(BigDecimal r16_balance_outstanding) {
			this.r16_balance_outstanding = r16_balance_outstanding;
		}

		public BigDecimal getR16_no_of_acct() {
			return r16_no_of_acct;
		}

		public void setR16_no_of_acct(BigDecimal r16_no_of_acct) {
			this.r16_no_of_acct = r16_no_of_acct;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_approved_limit() {
			return r17_approved_limit;
		}

		public void setR17_approved_limit(BigDecimal r17_approved_limit) {
			this.r17_approved_limit = r17_approved_limit;
		}

		public BigDecimal getR17_balance_outstanding() {
			return r17_balance_outstanding;
		}

		public void setR17_balance_outstanding(BigDecimal r17_balance_outstanding) {
			this.r17_balance_outstanding = r17_balance_outstanding;
		}

		public BigDecimal getR17_no_of_acct() {
			return r17_no_of_acct;
		}

		public void setR17_no_of_acct(BigDecimal r17_no_of_acct) {
			this.r17_no_of_acct = r17_no_of_acct;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_approved_limit() {
			return r18_approved_limit;
		}

		public void setR18_approved_limit(BigDecimal r18_approved_limit) {
			this.r18_approved_limit = r18_approved_limit;
		}

		public BigDecimal getR18_balance_outstanding() {
			return r18_balance_outstanding;
		}

		public void setR18_balance_outstanding(BigDecimal r18_balance_outstanding) {
			this.r18_balance_outstanding = r18_balance_outstanding;
		}

		public BigDecimal getR18_no_of_acct() {
			return r18_no_of_acct;
		}

		public void setR18_no_of_acct(BigDecimal r18_no_of_acct) {
			this.r18_no_of_acct = r18_no_of_acct;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_approved_limit() {
			return r19_approved_limit;
		}

		public void setR19_approved_limit(BigDecimal r19_approved_limit) {
			this.r19_approved_limit = r19_approved_limit;
		}

		public BigDecimal getR19_balance_outstanding() {
			return r19_balance_outstanding;
		}

		public void setR19_balance_outstanding(BigDecimal r19_balance_outstanding) {
			this.r19_balance_outstanding = r19_balance_outstanding;
		}

		public BigDecimal getR19_no_of_acct() {
			return r19_no_of_acct;
		}

		public void setR19_no_of_acct(BigDecimal r19_no_of_acct) {
			this.r19_no_of_acct = r19_no_of_acct;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_approved_limit() {
			return r20_approved_limit;
		}

		public void setR20_approved_limit(BigDecimal r20_approved_limit) {
			this.r20_approved_limit = r20_approved_limit;
		}

		public BigDecimal getR20_balance_outstanding() {
			return r20_balance_outstanding;
		}

		public void setR20_balance_outstanding(BigDecimal r20_balance_outstanding) {
			this.r20_balance_outstanding = r20_balance_outstanding;
		}

		public BigDecimal getR20_no_of_acct() {
			return r20_no_of_acct;
		}

		public void setR20_no_of_acct(BigDecimal r20_no_of_acct) {
			this.r20_no_of_acct = r20_no_of_acct;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_approved_limit() {
			return r21_approved_limit;
		}

		public void setR21_approved_limit(BigDecimal r21_approved_limit) {
			this.r21_approved_limit = r21_approved_limit;
		}

		public BigDecimal getR21_balance_outstanding() {
			return r21_balance_outstanding;
		}

		public void setR21_balance_outstanding(BigDecimal r21_balance_outstanding) {
			this.r21_balance_outstanding = r21_balance_outstanding;
		}

		public BigDecimal getR21_no_of_acct() {
			return r21_no_of_acct;
		}

		public void setR21_no_of_acct(BigDecimal r21_no_of_acct) {
			this.r21_no_of_acct = r21_no_of_acct;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_approved_limit() {
			return r22_approved_limit;
		}

		public void setR22_approved_limit(BigDecimal r22_approved_limit) {
			this.r22_approved_limit = r22_approved_limit;
		}

		public BigDecimal getR22_balance_outstanding() {
			return r22_balance_outstanding;
		}

		public void setR22_balance_outstanding(BigDecimal r22_balance_outstanding) {
			this.r22_balance_outstanding = r22_balance_outstanding;
		}

		public BigDecimal getR22_no_of_acct() {
			return r22_no_of_acct;
		}

		public void setR22_no_of_acct(BigDecimal r22_no_of_acct) {
			this.r22_no_of_acct = r22_no_of_acct;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_approved_limit() {
			return r23_approved_limit;
		}

		public void setR23_approved_limit(BigDecimal r23_approved_limit) {
			this.r23_approved_limit = r23_approved_limit;
		}

		public BigDecimal getR23_balance_outstanding() {
			return r23_balance_outstanding;
		}

		public void setR23_balance_outstanding(BigDecimal r23_balance_outstanding) {
			this.r23_balance_outstanding = r23_balance_outstanding;
		}

		public BigDecimal getR23_no_of_acct() {
			return r23_no_of_acct;
		}

		public void setR23_no_of_acct(BigDecimal r23_no_of_acct) {
			this.r23_no_of_acct = r23_no_of_acct;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_approved_limit() {
			return r24_approved_limit;
		}

		public void setR24_approved_limit(BigDecimal r24_approved_limit) {
			this.r24_approved_limit = r24_approved_limit;
		}

		public BigDecimal getR24_balance_outstanding() {
			return r24_balance_outstanding;
		}

		public void setR24_balance_outstanding(BigDecimal r24_balance_outstanding) {
			this.r24_balance_outstanding = r24_balance_outstanding;
		}

		public BigDecimal getR24_no_of_acct() {
			return r24_no_of_acct;
		}

		public void setR24_no_of_acct(BigDecimal r24_no_of_acct) {
			this.r24_no_of_acct = r24_no_of_acct;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_approved_limit() {
			return r25_approved_limit;
		}

		public void setR25_approved_limit(BigDecimal r25_approved_limit) {
			this.r25_approved_limit = r25_approved_limit;
		}

		public BigDecimal getR25_balance_outstanding() {
			return r25_balance_outstanding;
		}

		public void setR25_balance_outstanding(BigDecimal r25_balance_outstanding) {
			this.r25_balance_outstanding = r25_balance_outstanding;
		}

		public BigDecimal getR25_no_of_acct() {
			return r25_no_of_acct;
		}

		public void setR25_no_of_acct(BigDecimal r25_no_of_acct) {
			this.r25_no_of_acct = r25_no_of_acct;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_approved_limit() {
			return r26_approved_limit;
		}

		public void setR26_approved_limit(BigDecimal r26_approved_limit) {
			this.r26_approved_limit = r26_approved_limit;
		}

		public BigDecimal getR26_balance_outstanding() {
			return r26_balance_outstanding;
		}

		public void setR26_balance_outstanding(BigDecimal r26_balance_outstanding) {
			this.r26_balance_outstanding = r26_balance_outstanding;
		}

		public BigDecimal getR26_no_of_acct() {
			return r26_no_of_acct;
		}

		public void setR26_no_of_acct(BigDecimal r26_no_of_acct) {
			this.r26_no_of_acct = r26_no_of_acct;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_approved_limit() {
			return r27_approved_limit;
		}

		public void setR27_approved_limit(BigDecimal r27_approved_limit) {
			this.r27_approved_limit = r27_approved_limit;
		}

		public BigDecimal getR27_balance_outstanding() {
			return r27_balance_outstanding;
		}

		public void setR27_balance_outstanding(BigDecimal r27_balance_outstanding) {
			this.r27_balance_outstanding = r27_balance_outstanding;
		}

		public BigDecimal getR27_no_of_acct() {
			return r27_no_of_acct;
		}

		public void setR27_no_of_acct(BigDecimal r27_no_of_acct) {
			this.r27_no_of_acct = r27_no_of_acct;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_approved_limit() {
			return r28_approved_limit;
		}

		public void setR28_approved_limit(BigDecimal r28_approved_limit) {
			this.r28_approved_limit = r28_approved_limit;
		}

		public BigDecimal getR28_balance_outstanding() {
			return r28_balance_outstanding;
		}

		public void setR28_balance_outstanding(BigDecimal r28_balance_outstanding) {
			this.r28_balance_outstanding = r28_balance_outstanding;
		}

		public BigDecimal getR28_no_of_acct() {
			return r28_no_of_acct;
		}

		public void setR28_no_of_acct(BigDecimal r28_no_of_acct) {
			this.r28_no_of_acct = r28_no_of_acct;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_approved_limit() {
			return r29_approved_limit;
		}

		public void setR29_approved_limit(BigDecimal r29_approved_limit) {
			this.r29_approved_limit = r29_approved_limit;
		}

		public BigDecimal getR29_balance_outstanding() {
			return r29_balance_outstanding;
		}

		public void setR29_balance_outstanding(BigDecimal r29_balance_outstanding) {
			this.r29_balance_outstanding = r29_balance_outstanding;
		}

		public BigDecimal getR29_no_of_acct() {
			return r29_no_of_acct;
		}

		public void setR29_no_of_acct(BigDecimal r29_no_of_acct) {
			this.r29_no_of_acct = r29_no_of_acct;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_approved_limit() {
			return r30_approved_limit;
		}

		public void setR30_approved_limit(BigDecimal r30_approved_limit) {
			this.r30_approved_limit = r30_approved_limit;
		}

		public BigDecimal getR30_balance_outstanding() {
			return r30_balance_outstanding;
		}

		public void setR30_balance_outstanding(BigDecimal r30_balance_outstanding) {
			this.r30_balance_outstanding = r30_balance_outstanding;
		}

		public BigDecimal getR30_no_of_acct() {
			return r30_no_of_acct;
		}

		public void setR30_no_of_acct(BigDecimal r30_no_of_acct) {
			this.r30_no_of_acct = r30_no_of_acct;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_approved_limit() {
			return r31_approved_limit;
		}

		public void setR31_approved_limit(BigDecimal r31_approved_limit) {
			this.r31_approved_limit = r31_approved_limit;
		}

		public BigDecimal getR31_balance_outstanding() {
			return r31_balance_outstanding;
		}

		public void setR31_balance_outstanding(BigDecimal r31_balance_outstanding) {
			this.r31_balance_outstanding = r31_balance_outstanding;
		}

		public BigDecimal getR31_no_of_acct() {
			return r31_no_of_acct;
		}

		public void setR31_no_of_acct(BigDecimal r31_no_of_acct) {
			this.r31_no_of_acct = r31_no_of_acct;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_approved_limit() {
			return r32_approved_limit;
		}

		public void setR32_approved_limit(BigDecimal r32_approved_limit) {
			this.r32_approved_limit = r32_approved_limit;
		}

		public BigDecimal getR32_balance_outstanding() {
			return r32_balance_outstanding;
		}

		public void setR32_balance_outstanding(BigDecimal r32_balance_outstanding) {
			this.r32_balance_outstanding = r32_balance_outstanding;
		}

		public BigDecimal getR32_no_of_acct() {
			return r32_no_of_acct;
		}

		public void setR32_no_of_acct(BigDecimal r32_no_of_acct) {
			this.r32_no_of_acct = r32_no_of_acct;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_approved_limit() {
			return r33_approved_limit;
		}

		public void setR33_approved_limit(BigDecimal r33_approved_limit) {
			this.r33_approved_limit = r33_approved_limit;
		}

		public BigDecimal getR33_balance_outstanding() {
			return r33_balance_outstanding;
		}

		public void setR33_balance_outstanding(BigDecimal r33_balance_outstanding) {
			this.r33_balance_outstanding = r33_balance_outstanding;
		}

		public BigDecimal getR33_no_of_acct() {
			return r33_no_of_acct;
		}

		public void setR33_no_of_acct(BigDecimal r33_no_of_acct) {
			this.r33_no_of_acct = r33_no_of_acct;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_approved_limit() {
			return r34_approved_limit;
		}

		public void setR34_approved_limit(BigDecimal r34_approved_limit) {
			this.r34_approved_limit = r34_approved_limit;
		}

		public BigDecimal getR34_balance_outstanding() {
			return r34_balance_outstanding;
		}

		public void setR34_balance_outstanding(BigDecimal r34_balance_outstanding) {
			this.r34_balance_outstanding = r34_balance_outstanding;
		}

		public BigDecimal getR34_no_of_acct() {
			return r34_no_of_acct;
		}

		public void setR34_no_of_acct(BigDecimal r34_no_of_acct) {
			this.r34_no_of_acct = r34_no_of_acct;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_approved_limit() {
			return r35_approved_limit;
		}

		public void setR35_approved_limit(BigDecimal r35_approved_limit) {
			this.r35_approved_limit = r35_approved_limit;
		}

		public BigDecimal getR35_balance_outstanding() {
			return r35_balance_outstanding;
		}

		public void setR35_balance_outstanding(BigDecimal r35_balance_outstanding) {
			this.r35_balance_outstanding = r35_balance_outstanding;
		}

		public BigDecimal getR35_no_of_acct() {
			return r35_no_of_acct;
		}

		public void setR35_no_of_acct(BigDecimal r35_no_of_acct) {
			this.r35_no_of_acct = r35_no_of_acct;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_approved_limit() {
			return r36_approved_limit;
		}

		public void setR36_approved_limit(BigDecimal r36_approved_limit) {
			this.r36_approved_limit = r36_approved_limit;
		}

		public BigDecimal getR36_balance_outstanding() {
			return r36_balance_outstanding;
		}

		public void setR36_balance_outstanding(BigDecimal r36_balance_outstanding) {
			this.r36_balance_outstanding = r36_balance_outstanding;
		}

		public BigDecimal getR36_no_of_acct() {
			return r36_no_of_acct;
		}

		public void setR36_no_of_acct(BigDecimal r36_no_of_acct) {
			this.r36_no_of_acct = r36_no_of_acct;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_approved_limit() {
			return r37_approved_limit;
		}

		public void setR37_approved_limit(BigDecimal r37_approved_limit) {
			this.r37_approved_limit = r37_approved_limit;
		}

		public BigDecimal getR37_balance_outstanding() {
			return r37_balance_outstanding;
		}

		public void setR37_balance_outstanding(BigDecimal r37_balance_outstanding) {
			this.r37_balance_outstanding = r37_balance_outstanding;
		}

		public BigDecimal getR37_no_of_acct() {
			return r37_no_of_acct;
		}

		public void setR37_no_of_acct(BigDecimal r37_no_of_acct) {
			this.r37_no_of_acct = r37_no_of_acct;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_approved_limit() {
			return r38_approved_limit;
		}

		public void setR38_approved_limit(BigDecimal r38_approved_limit) {
			this.r38_approved_limit = r38_approved_limit;
		}

		public BigDecimal getR38_balance_outstanding() {
			return r38_balance_outstanding;
		}

		public void setR38_balance_outstanding(BigDecimal r38_balance_outstanding) {
			this.r38_balance_outstanding = r38_balance_outstanding;
		}

		public BigDecimal getR38_no_of_acct() {
			return r38_no_of_acct;
		}

		public void setR38_no_of_acct(BigDecimal r38_no_of_acct) {
			this.r38_no_of_acct = r38_no_of_acct;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_approved_limit() {
			return r39_approved_limit;
		}

		public void setR39_approved_limit(BigDecimal r39_approved_limit) {
			this.r39_approved_limit = r39_approved_limit;
		}

		public BigDecimal getR39_balance_outstanding() {
			return r39_balance_outstanding;
		}

		public void setR39_balance_outstanding(BigDecimal r39_balance_outstanding) {
			this.r39_balance_outstanding = r39_balance_outstanding;
		}

		public BigDecimal getR39_no_of_acct() {
			return r39_no_of_acct;
		}

		public void setR39_no_of_acct(BigDecimal r39_no_of_acct) {
			this.r39_no_of_acct = r39_no_of_acct;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_approved_limit() {
			return r40_approved_limit;
		}

		public void setR40_approved_limit(BigDecimal r40_approved_limit) {
			this.r40_approved_limit = r40_approved_limit;
		}

		public BigDecimal getR40_balance_outstanding() {
			return r40_balance_outstanding;
		}

		public void setR40_balance_outstanding(BigDecimal r40_balance_outstanding) {
			this.r40_balance_outstanding = r40_balance_outstanding;
		}

		public BigDecimal getR40_no_of_acct() {
			return r40_no_of_acct;
		}

		public void setR40_no_of_acct(BigDecimal r40_no_of_acct) {
			this.r40_no_of_acct = r40_no_of_acct;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_approved_limit() {
			return r41_approved_limit;
		}

		public void setR41_approved_limit(BigDecimal r41_approved_limit) {
			this.r41_approved_limit = r41_approved_limit;
		}

		public BigDecimal getR41_balance_outstanding() {
			return r41_balance_outstanding;
		}

		public void setR41_balance_outstanding(BigDecimal r41_balance_outstanding) {
			this.r41_balance_outstanding = r41_balance_outstanding;
		}

		public BigDecimal getR41_no_of_acct() {
			return r41_no_of_acct;
		}

		public void setR41_no_of_acct(BigDecimal r41_no_of_acct) {
			this.r41_no_of_acct = r41_no_of_acct;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_approved_limit() {
			return r42_approved_limit;
		}

		public void setR42_approved_limit(BigDecimal r42_approved_limit) {
			this.r42_approved_limit = r42_approved_limit;
		}

		public BigDecimal getR42_balance_outstanding() {
			return r42_balance_outstanding;
		}

		public void setR42_balance_outstanding(BigDecimal r42_balance_outstanding) {
			this.r42_balance_outstanding = r42_balance_outstanding;
		}

		public BigDecimal getR42_no_of_acct() {
			return r42_no_of_acct;
		}

		public void setR42_no_of_acct(BigDecimal r42_no_of_acct) {
			this.r42_no_of_acct = r42_no_of_acct;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_approved_limit() {
			return r43_approved_limit;
		}

		public void setR43_approved_limit(BigDecimal r43_approved_limit) {
			this.r43_approved_limit = r43_approved_limit;
		}

		public BigDecimal getR43_balance_outstanding() {
			return r43_balance_outstanding;
		}

		public void setR43_balance_outstanding(BigDecimal r43_balance_outstanding) {
			this.r43_balance_outstanding = r43_balance_outstanding;
		}

		public BigDecimal getR43_no_of_acct() {
			return r43_no_of_acct;
		}

		public void setR43_no_of_acct(BigDecimal r43_no_of_acct) {
			this.r43_no_of_acct = r43_no_of_acct;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_approved_limit() {
			return r44_approved_limit;
		}

		public void setR44_approved_limit(BigDecimal r44_approved_limit) {
			this.r44_approved_limit = r44_approved_limit;
		}

		public BigDecimal getR44_balance_outstanding() {
			return r44_balance_outstanding;
		}

		public void setR44_balance_outstanding(BigDecimal r44_balance_outstanding) {
			this.r44_balance_outstanding = r44_balance_outstanding;
		}

		public BigDecimal getR44_no_of_acct() {
			return r44_no_of_acct;
		}

		public void setR44_no_of_acct(BigDecimal r44_no_of_acct) {
			this.r44_no_of_acct = r44_no_of_acct;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_approved_limit() {
			return r45_approved_limit;
		}

		public void setR45_approved_limit(BigDecimal r45_approved_limit) {
			this.r45_approved_limit = r45_approved_limit;
		}

		public BigDecimal getR45_balance_outstanding() {
			return r45_balance_outstanding;
		}

		public void setR45_balance_outstanding(BigDecimal r45_balance_outstanding) {
			this.r45_balance_outstanding = r45_balance_outstanding;
		}

		public BigDecimal getR45_no_of_acct() {
			return r45_no_of_acct;
		}

		public void setR45_no_of_acct(BigDecimal r45_no_of_acct) {
			this.r45_no_of_acct = r45_no_of_acct;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_approved_limit() {
			return r46_approved_limit;
		}

		public void setR46_approved_limit(BigDecimal r46_approved_limit) {
			this.r46_approved_limit = r46_approved_limit;
		}

		public BigDecimal getR46_balance_outstanding() {
			return r46_balance_outstanding;
		}

		public void setR46_balance_outstanding(BigDecimal r46_balance_outstanding) {
			this.r46_balance_outstanding = r46_balance_outstanding;
		}

		public BigDecimal getR46_no_of_acct() {
			return r46_no_of_acct;
		}

		public void setR46_no_of_acct(BigDecimal r46_no_of_acct) {
			this.r46_no_of_acct = r46_no_of_acct;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_approved_limit() {
			return r47_approved_limit;
		}

		public void setR47_approved_limit(BigDecimal r47_approved_limit) {
			this.r47_approved_limit = r47_approved_limit;
		}

		public BigDecimal getR47_balance_outstanding() {
			return r47_balance_outstanding;
		}

		public void setR47_balance_outstanding(BigDecimal r47_balance_outstanding) {
			this.r47_balance_outstanding = r47_balance_outstanding;
		}

		public BigDecimal getR47_no_of_acct() {
			return r47_no_of_acct;
		}

		public void setR47_no_of_acct(BigDecimal r47_no_of_acct) {
			this.r47_no_of_acct = r47_no_of_acct;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_approved_limit() {
			return r48_approved_limit;
		}

		public void setR48_approved_limit(BigDecimal r48_approved_limit) {
			this.r48_approved_limit = r48_approved_limit;
		}

		public BigDecimal getR48_balance_outstanding() {
			return r48_balance_outstanding;
		}

		public void setR48_balance_outstanding(BigDecimal r48_balance_outstanding) {
			this.r48_balance_outstanding = r48_balance_outstanding;
		}

		public BigDecimal getR48_no_of_acct() {
			return r48_no_of_acct;
		}

		public void setR48_no_of_acct(BigDecimal r48_no_of_acct) {
			this.r48_no_of_acct = r48_no_of_acct;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_approved_limit() {
			return r49_approved_limit;
		}

		public void setR49_approved_limit(BigDecimal r49_approved_limit) {
			this.r49_approved_limit = r49_approved_limit;
		}

		public BigDecimal getR49_balance_outstanding() {
			return r49_balance_outstanding;
		}

		public void setR49_balance_outstanding(BigDecimal r49_balance_outstanding) {
			this.r49_balance_outstanding = r49_balance_outstanding;
		}

		public BigDecimal getR49_no_of_acct() {
			return r49_no_of_acct;
		}

		public void setR49_no_of_acct(BigDecimal r49_no_of_acct) {
			this.r49_no_of_acct = r49_no_of_acct;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_approved_limit() {
			return r50_approved_limit;
		}

		public void setR50_approved_limit(BigDecimal r50_approved_limit) {
			this.r50_approved_limit = r50_approved_limit;
		}

		public BigDecimal getR50_balance_outstanding() {
			return r50_balance_outstanding;
		}

		public void setR50_balance_outstanding(BigDecimal r50_balance_outstanding) {
			this.r50_balance_outstanding = r50_balance_outstanding;
		}

		public BigDecimal getR50_no_of_acct() {
			return r50_no_of_acct;
		}

		public void setR50_no_of_acct(BigDecimal r50_no_of_acct) {
			this.r50_no_of_acct = r50_no_of_acct;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public BigDecimal getR51_approved_limit() {
			return r51_approved_limit;
		}

		public void setR51_approved_limit(BigDecimal r51_approved_limit) {
			this.r51_approved_limit = r51_approved_limit;
		}

		public BigDecimal getR51_balance_outstanding() {
			return r51_balance_outstanding;
		}

		public void setR51_balance_outstanding(BigDecimal r51_balance_outstanding) {
			this.r51_balance_outstanding = r51_balance_outstanding;
		}

		public BigDecimal getR51_no_of_acct() {
			return r51_no_of_acct;
		}

		public void setR51_no_of_acct(BigDecimal r51_no_of_acct) {
			this.r51_no_of_acct = r51_no_of_acct;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public BigDecimal getR52_approved_limit() {
			return r52_approved_limit;
		}

		public void setR52_approved_limit(BigDecimal r52_approved_limit) {
			this.r52_approved_limit = r52_approved_limit;
		}

		public BigDecimal getR52_balance_outstanding() {
			return r52_balance_outstanding;
		}

		public void setR52_balance_outstanding(BigDecimal r52_balance_outstanding) {
			this.r52_balance_outstanding = r52_balance_outstanding;
		}

		public BigDecimal getR52_no_of_acct() {
			return r52_no_of_acct;
		}

		public void setR52_no_of_acct(BigDecimal r52_no_of_acct) {
			this.r52_no_of_acct = r52_no_of_acct;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public BigDecimal getR53_approved_limit() {
			return r53_approved_limit;
		}

		public void setR53_approved_limit(BigDecimal r53_approved_limit) {
			this.r53_approved_limit = r53_approved_limit;
		}

		public BigDecimal getR53_balance_outstanding() {
			return r53_balance_outstanding;
		}

		public void setR53_balance_outstanding(BigDecimal r53_balance_outstanding) {
			this.r53_balance_outstanding = r53_balance_outstanding;
		}

		public BigDecimal getR53_no_of_acct() {
			return r53_no_of_acct;
		}

		public void setR53_no_of_acct(BigDecimal r53_no_of_acct) {
			this.r53_no_of_acct = r53_no_of_acct;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public BigDecimal getR54_approved_limit() {
			return r54_approved_limit;
		}

		public void setR54_approved_limit(BigDecimal r54_approved_limit) {
			this.r54_approved_limit = r54_approved_limit;
		}

		public BigDecimal getR54_balance_outstanding() {
			return r54_balance_outstanding;
		}

		public void setR54_balance_outstanding(BigDecimal r54_balance_outstanding) {
			this.r54_balance_outstanding = r54_balance_outstanding;
		}

		public BigDecimal getR54_no_of_acct() {
			return r54_no_of_acct;
		}

		public void setR54_no_of_acct(BigDecimal r54_no_of_acct) {
			this.r54_no_of_acct = r54_no_of_acct;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public BigDecimal getR55_approved_limit() {
			return r55_approved_limit;
		}

		public void setR55_approved_limit(BigDecimal r55_approved_limit) {
			this.r55_approved_limit = r55_approved_limit;
		}

		public BigDecimal getR55_balance_outstanding() {
			return r55_balance_outstanding;
		}

		public void setR55_balance_outstanding(BigDecimal r55_balance_outstanding) {
			this.r55_balance_outstanding = r55_balance_outstanding;
		}

		public BigDecimal getR55_no_of_acct() {
			return r55_no_of_acct;
		}

		public void setR55_no_of_acct(BigDecimal r55_no_of_acct) {
			this.r55_no_of_acct = r55_no_of_acct;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public BigDecimal getR56_approved_limit() {
			return r56_approved_limit;
		}

		public void setR56_approved_limit(BigDecimal r56_approved_limit) {
			this.r56_approved_limit = r56_approved_limit;
		}

		public BigDecimal getR56_balance_outstanding() {
			return r56_balance_outstanding;
		}

		public void setR56_balance_outstanding(BigDecimal r56_balance_outstanding) {
			this.r56_balance_outstanding = r56_balance_outstanding;
		}

		public BigDecimal getR56_no_of_acct() {
			return r56_no_of_acct;
		}

		public void setR56_no_of_acct(BigDecimal r56_no_of_acct) {
			this.r56_no_of_acct = r56_no_of_acct;
		}

		public String getR57_product() {
			return r57_product;
		}

		public void setR57_product(String r57_product) {
			this.r57_product = r57_product;
		}

		public BigDecimal getR57_approved_limit() {
			return r57_approved_limit;
		}

		public void setR57_approved_limit(BigDecimal r57_approved_limit) {
			this.r57_approved_limit = r57_approved_limit;
		}

		public BigDecimal getR57_balance_outstanding() {
			return r57_balance_outstanding;
		}

		public void setR57_balance_outstanding(BigDecimal r57_balance_outstanding) {
			this.r57_balance_outstanding = r57_balance_outstanding;
		}

		public BigDecimal getR57_no_of_acct() {
			return r57_no_of_acct;
		}

		public void setR57_no_of_acct(BigDecimal r57_no_of_acct) {
			this.r57_no_of_acct = r57_no_of_acct;
		}

		public String getR58_product() {
			return r58_product;
		}

		public void setR58_product(String r58_product) {
			this.r58_product = r58_product;
		}

		public BigDecimal getR58_approved_limit() {
			return r58_approved_limit;
		}

		public void setR58_approved_limit(BigDecimal r58_approved_limit) {
			this.r58_approved_limit = r58_approved_limit;
		}

		public BigDecimal getR58_balance_outstanding() {
			return r58_balance_outstanding;
		}

		public void setR58_balance_outstanding(BigDecimal r58_balance_outstanding) {
			this.r58_balance_outstanding = r58_balance_outstanding;
		}

		public BigDecimal getR58_no_of_acct() {
			return r58_no_of_acct;
		}

		public void setR58_no_of_acct(BigDecimal r58_no_of_acct) {
			this.r58_no_of_acct = r58_no_of_acct;
		}

		public String getR59_product() {
			return r59_product;
		}

		public void setR59_product(String r59_product) {
			this.r59_product = r59_product;
		}

		public BigDecimal getR59_approved_limit() {
			return r59_approved_limit;
		}

		public void setR59_approved_limit(BigDecimal r59_approved_limit) {
			this.r59_approved_limit = r59_approved_limit;
		}

		public BigDecimal getR59_balance_outstanding() {
			return r59_balance_outstanding;
		}

		public void setR59_balance_outstanding(BigDecimal r59_balance_outstanding) {
			this.r59_balance_outstanding = r59_balance_outstanding;
		}

		public BigDecimal getR59_no_of_acct() {
			return r59_no_of_acct;
		}

		public void setR59_no_of_acct(BigDecimal r59_no_of_acct) {
			this.r59_no_of_acct = r59_no_of_acct;
		}

		public String getR60_product() {
			return r60_product;
		}

		public void setR60_product(String r60_product) {
			this.r60_product = r60_product;
		}

		public BigDecimal getR60_approved_limit() {
			return r60_approved_limit;
		}

		public void setR60_approved_limit(BigDecimal r60_approved_limit) {
			this.r60_approved_limit = r60_approved_limit;
		}

		public BigDecimal getR60_balance_outstanding() {
			return r60_balance_outstanding;
		}

		public void setR60_balance_outstanding(BigDecimal r60_balance_outstanding) {
			this.r60_balance_outstanding = r60_balance_outstanding;
		}

		public BigDecimal getR60_no_of_acct() {
			return r60_no_of_acct;
		}

		public void setR60_no_of_acct(BigDecimal r60_no_of_acct) {
			this.r60_no_of_acct = r60_no_of_acct;
		}

		public String getR61_product() {
			return r61_product;
		}

		public void setR61_product(String r61_product) {
			this.r61_product = r61_product;
		}

		public BigDecimal getR61_approved_limit() {
			return r61_approved_limit;
		}

		public void setR61_approved_limit(BigDecimal r61_approved_limit) {
			this.r61_approved_limit = r61_approved_limit;
		}

		public BigDecimal getR61_balance_outstanding() {
			return r61_balance_outstanding;
		}

		public void setR61_balance_outstanding(BigDecimal r61_balance_outstanding) {
			this.r61_balance_outstanding = r61_balance_outstanding;
		}

		public BigDecimal getR61_no_of_acct() {
			return r61_no_of_acct;
		}

		public void setR61_no_of_acct(BigDecimal r61_no_of_acct) {
			this.r61_no_of_acct = r61_no_of_acct;
		}

		public String getR62_product() {
			return r62_product;
		}

		public void setR62_product(String r62_product) {
			this.r62_product = r62_product;
		}

		public BigDecimal getR62_approved_limit() {
			return r62_approved_limit;
		}

		public void setR62_approved_limit(BigDecimal r62_approved_limit) {
			this.r62_approved_limit = r62_approved_limit;
		}

		public BigDecimal getR62_balance_outstanding() {
			return r62_balance_outstanding;
		}

		public void setR62_balance_outstanding(BigDecimal r62_balance_outstanding) {
			this.r62_balance_outstanding = r62_balance_outstanding;
		}

		public BigDecimal getR62_no_of_acct() {
			return r62_no_of_acct;
		}

		public void setR62_no_of_acct(BigDecimal r62_no_of_acct) {
			this.r62_no_of_acct = r62_no_of_acct;
		}

		public String getR63_product() {
			return r63_product;
		}

		public void setR63_product(String r63_product) {
			this.r63_product = r63_product;
		}

		public BigDecimal getR63_approved_limit() {
			return r63_approved_limit;
		}

		public void setR63_approved_limit(BigDecimal r63_approved_limit) {
			this.r63_approved_limit = r63_approved_limit;
		}

		public BigDecimal getR63_balance_outstanding() {
			return r63_balance_outstanding;
		}

		public void setR63_balance_outstanding(BigDecimal r63_balance_outstanding) {
			this.r63_balance_outstanding = r63_balance_outstanding;
		}

		public BigDecimal getR63_no_of_acct() {
			return r63_no_of_acct;
		}

		public void setR63_no_of_acct(BigDecimal r63_no_of_acct) {
			this.r63_no_of_acct = r63_no_of_acct;
		}

		public String getR64_product() {
			return r64_product;
		}

		public void setR64_product(String r64_product) {
			this.r64_product = r64_product;
		}

		public BigDecimal getR64_approved_limit() {
			return r64_approved_limit;
		}

		public void setR64_approved_limit(BigDecimal r64_approved_limit) {
			this.r64_approved_limit = r64_approved_limit;
		}

		public BigDecimal getR64_balance_outstanding() {
			return r64_balance_outstanding;
		}

		public void setR64_balance_outstanding(BigDecimal r64_balance_outstanding) {
			this.r64_balance_outstanding = r64_balance_outstanding;
		}

		public BigDecimal getR64_no_of_acct() {
			return r64_no_of_acct;
		}

		public void setR64_no_of_acct(BigDecimal r64_no_of_acct) {
			this.r64_no_of_acct = r64_no_of_acct;
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

	}

	// COMPOSITE KEY CLASS INSIDE SERVICE

	public static class M_LA1_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public M_LA1_PK() {
		}

		public M_LA1_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof M_LA1_PK))
				return false;
			M_LA1_PK that = (M_LA1_PK) o;
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

	// ARCHIVAL SUMMARY ROW MAPPER

	class M_LA1ArchivalRowMapper implements RowMapper<M_LA1_Archival_Summary_Entity> {

		@Override
		public M_LA1_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA1_Archival_Summary_Entity obj = new M_LA1_Archival_Summary_Entity();

			obj.setR11_product(rs.getString("r11_product"));
			obj.setR11_approved_limit(rs.getBigDecimal("r11_approved_limit"));
			obj.setR11_balance_outstanding(rs.getBigDecimal("r11_balance_outstanding"));
			obj.setR11_no_of_acct(rs.getBigDecimal("r11_no_of_acct"));

			obj.setR12_product(rs.getString("r12_product"));
			obj.setR12_approved_limit(rs.getBigDecimal("r12_approved_limit"));
			obj.setR12_balance_outstanding(rs.getBigDecimal("r12_balance_outstanding"));
			obj.setR12_no_of_acct(rs.getBigDecimal("r12_no_of_acct"));

			obj.setR13_product(rs.getString("r13_product"));
			obj.setR13_approved_limit(rs.getBigDecimal("r13_approved_limit"));
			obj.setR13_balance_outstanding(rs.getBigDecimal("r13_balance_outstanding"));
			obj.setR13_no_of_acct(rs.getBigDecimal("r13_no_of_acct"));

			obj.setR14_product(rs.getString("r14_product"));
			obj.setR14_approved_limit(rs.getBigDecimal("r14_approved_limit"));
			obj.setR14_balance_outstanding(rs.getBigDecimal("r14_balance_outstanding"));
			obj.setR14_no_of_acct(rs.getBigDecimal("r14_no_of_acct"));

			obj.setR15_product(rs.getString("r15_product"));
			obj.setR15_approved_limit(rs.getBigDecimal("r15_approved_limit"));
			obj.setR15_balance_outstanding(rs.getBigDecimal("r15_balance_outstanding"));
			obj.setR15_no_of_acct(rs.getBigDecimal("r15_no_of_acct"));

			obj.setR16_product(rs.getString("r16_product"));
			obj.setR16_approved_limit(rs.getBigDecimal("r16_approved_limit"));
			obj.setR16_balance_outstanding(rs.getBigDecimal("r16_balance_outstanding"));
			obj.setR16_no_of_acct(rs.getBigDecimal("r16_no_of_acct"));

			obj.setR17_product(rs.getString("r17_product"));
			obj.setR17_approved_limit(rs.getBigDecimal("r17_approved_limit"));
			obj.setR17_balance_outstanding(rs.getBigDecimal("r17_balance_outstanding"));
			obj.setR17_no_of_acct(rs.getBigDecimal("r17_no_of_acct"));

			obj.setR18_product(rs.getString("r18_product"));
			obj.setR18_approved_limit(rs.getBigDecimal("r18_approved_limit"));
			obj.setR18_balance_outstanding(rs.getBigDecimal("r18_balance_outstanding"));
			obj.setR18_no_of_acct(rs.getBigDecimal("r18_no_of_acct"));

			obj.setR19_product(rs.getString("r19_product"));
			obj.setR19_approved_limit(rs.getBigDecimal("r19_approved_limit"));
			obj.setR19_balance_outstanding(rs.getBigDecimal("r19_balance_outstanding"));
			obj.setR19_no_of_acct(rs.getBigDecimal("r19_no_of_acct"));

			obj.setR20_product(rs.getString("r20_product"));
			obj.setR20_approved_limit(rs.getBigDecimal("r20_approved_limit"));
			obj.setR20_balance_outstanding(rs.getBigDecimal("r20_balance_outstanding"));
			obj.setR20_no_of_acct(rs.getBigDecimal("r20_no_of_acct"));

			obj.setR21_product(rs.getString("r21_product"));
			obj.setR21_approved_limit(rs.getBigDecimal("r21_approved_limit"));
			obj.setR21_balance_outstanding(rs.getBigDecimal("r21_balance_outstanding"));
			obj.setR21_no_of_acct(rs.getBigDecimal("r21_no_of_acct"));

			obj.setR22_product(rs.getString("r22_product"));
			obj.setR22_approved_limit(rs.getBigDecimal("r22_approved_limit"));
			obj.setR22_balance_outstanding(rs.getBigDecimal("r22_balance_outstanding"));
			obj.setR22_no_of_acct(rs.getBigDecimal("r22_no_of_acct"));

			obj.setR23_product(rs.getString("r23_product"));
			obj.setR23_approved_limit(rs.getBigDecimal("r23_approved_limit"));
			obj.setR23_balance_outstanding(rs.getBigDecimal("r23_balance_outstanding"));
			obj.setR23_no_of_acct(rs.getBigDecimal("r23_no_of_acct"));

			obj.setR24_product(rs.getString("r24_product"));
			obj.setR24_approved_limit(rs.getBigDecimal("r24_approved_limit"));
			obj.setR24_balance_outstanding(rs.getBigDecimal("r24_balance_outstanding"));
			obj.setR24_no_of_acct(rs.getBigDecimal("r24_no_of_acct"));

			obj.setR25_product(rs.getString("r25_product"));
			obj.setR25_approved_limit(rs.getBigDecimal("r25_approved_limit"));
			obj.setR25_balance_outstanding(rs.getBigDecimal("r25_balance_outstanding"));
			obj.setR25_no_of_acct(rs.getBigDecimal("r25_no_of_acct"));

			obj.setR26_product(rs.getString("r26_product"));
			obj.setR26_approved_limit(rs.getBigDecimal("r26_approved_limit"));
			obj.setR26_balance_outstanding(rs.getBigDecimal("r26_balance_outstanding"));
			obj.setR26_no_of_acct(rs.getBigDecimal("r26_no_of_acct"));

			obj.setR27_product(rs.getString("r27_product"));
			obj.setR27_approved_limit(rs.getBigDecimal("r27_approved_limit"));
			obj.setR27_balance_outstanding(rs.getBigDecimal("r27_balance_outstanding"));
			obj.setR27_no_of_acct(rs.getBigDecimal("r27_no_of_acct"));

			obj.setR28_product(rs.getString("r28_product"));
			obj.setR28_approved_limit(rs.getBigDecimal("r28_approved_limit"));
			obj.setR28_balance_outstanding(rs.getBigDecimal("r28_balance_outstanding"));
			obj.setR28_no_of_acct(rs.getBigDecimal("r28_no_of_acct"));

			obj.setR29_product(rs.getString("r29_product"));
			obj.setR29_approved_limit(rs.getBigDecimal("r29_approved_limit"));
			obj.setR29_balance_outstanding(rs.getBigDecimal("r29_balance_outstanding"));
			obj.setR29_no_of_acct(rs.getBigDecimal("r29_no_of_acct"));

			obj.setR30_product(rs.getString("r30_product"));
			obj.setR30_approved_limit(rs.getBigDecimal("r30_approved_limit"));
			obj.setR30_balance_outstanding(rs.getBigDecimal("r30_balance_outstanding"));
			obj.setR30_no_of_acct(rs.getBigDecimal("r30_no_of_acct"));

			obj.setR31_product(rs.getString("r31_product"));
			obj.setR31_approved_limit(rs.getBigDecimal("r31_approved_limit"));
			obj.setR31_balance_outstanding(rs.getBigDecimal("r31_balance_outstanding"));
			obj.setR31_no_of_acct(rs.getBigDecimal("r31_no_of_acct"));

			obj.setR32_product(rs.getString("r32_product"));
			obj.setR32_approved_limit(rs.getBigDecimal("r32_approved_limit"));
			obj.setR32_balance_outstanding(rs.getBigDecimal("r32_balance_outstanding"));
			obj.setR32_no_of_acct(rs.getBigDecimal("r32_no_of_acct"));

			obj.setR33_product(rs.getString("r33_product"));
			obj.setR33_approved_limit(rs.getBigDecimal("r33_approved_limit"));
			obj.setR33_balance_outstanding(rs.getBigDecimal("r33_balance_outstanding"));
			obj.setR33_no_of_acct(rs.getBigDecimal("r33_no_of_acct"));

			obj.setR34_product(rs.getString("r34_product"));
			obj.setR34_approved_limit(rs.getBigDecimal("r34_approved_limit"));
			obj.setR34_balance_outstanding(rs.getBigDecimal("r34_balance_outstanding"));
			obj.setR34_no_of_acct(rs.getBigDecimal("r34_no_of_acct"));

			obj.setR35_product(rs.getString("r35_product"));
			obj.setR35_approved_limit(rs.getBigDecimal("r35_approved_limit"));
			obj.setR35_balance_outstanding(rs.getBigDecimal("r35_balance_outstanding"));
			obj.setR35_no_of_acct(rs.getBigDecimal("r35_no_of_acct"));

			obj.setR36_product(rs.getString("r36_product"));
			obj.setR36_approved_limit(rs.getBigDecimal("r36_approved_limit"));
			obj.setR36_balance_outstanding(rs.getBigDecimal("r36_balance_outstanding"));
			obj.setR36_no_of_acct(rs.getBigDecimal("r36_no_of_acct"));

			obj.setR37_product(rs.getString("r37_product"));
			obj.setR37_approved_limit(rs.getBigDecimal("r37_approved_limit"));
			obj.setR37_balance_outstanding(rs.getBigDecimal("r37_balance_outstanding"));
			obj.setR37_no_of_acct(rs.getBigDecimal("r37_no_of_acct"));

			obj.setR38_product(rs.getString("r38_product"));
			obj.setR38_approved_limit(rs.getBigDecimal("r38_approved_limit"));
			obj.setR38_balance_outstanding(rs.getBigDecimal("r38_balance_outstanding"));
			obj.setR38_no_of_acct(rs.getBigDecimal("r38_no_of_acct"));

			obj.setR39_product(rs.getString("r39_product"));
			obj.setR39_approved_limit(rs.getBigDecimal("r39_approved_limit"));
			obj.setR39_balance_outstanding(rs.getBigDecimal("r39_balance_outstanding"));
			obj.setR39_no_of_acct(rs.getBigDecimal("r39_no_of_acct"));

			obj.setR40_product(rs.getString("r40_product"));
			obj.setR40_approved_limit(rs.getBigDecimal("r40_approved_limit"));
			obj.setR40_balance_outstanding(rs.getBigDecimal("r40_balance_outstanding"));
			obj.setR40_no_of_acct(rs.getBigDecimal("r40_no_of_acct"));

			obj.setR41_product(rs.getString("r41_product"));
			obj.setR41_approved_limit(rs.getBigDecimal("r41_approved_limit"));
			obj.setR41_balance_outstanding(rs.getBigDecimal("r41_balance_outstanding"));
			obj.setR41_no_of_acct(rs.getBigDecimal("r41_no_of_acct"));

			obj.setR42_product(rs.getString("r42_product"));
			obj.setR42_approved_limit(rs.getBigDecimal("r42_approved_limit"));
			obj.setR42_balance_outstanding(rs.getBigDecimal("r42_balance_outstanding"));
			obj.setR42_no_of_acct(rs.getBigDecimal("r42_no_of_acct"));

			obj.setR43_product(rs.getString("r43_product"));
			obj.setR43_approved_limit(rs.getBigDecimal("r43_approved_limit"));
			obj.setR43_balance_outstanding(rs.getBigDecimal("r43_balance_outstanding"));
			obj.setR43_no_of_acct(rs.getBigDecimal("r43_no_of_acct"));

			obj.setR44_product(rs.getString("r44_product"));
			obj.setR44_approved_limit(rs.getBigDecimal("r44_approved_limit"));
			obj.setR44_balance_outstanding(rs.getBigDecimal("r44_balance_outstanding"));
			obj.setR44_no_of_acct(rs.getBigDecimal("r44_no_of_acct"));

			obj.setR45_product(rs.getString("r45_product"));
			obj.setR45_approved_limit(rs.getBigDecimal("r45_approved_limit"));
			obj.setR45_balance_outstanding(rs.getBigDecimal("r45_balance_outstanding"));
			obj.setR45_no_of_acct(rs.getBigDecimal("r45_no_of_acct"));

			obj.setR46_product(rs.getString("r46_product"));
			obj.setR46_approved_limit(rs.getBigDecimal("r46_approved_limit"));
			obj.setR46_balance_outstanding(rs.getBigDecimal("r46_balance_outstanding"));
			obj.setR46_no_of_acct(rs.getBigDecimal("r46_no_of_acct"));

			obj.setR47_product(rs.getString("r47_product"));
			obj.setR47_approved_limit(rs.getBigDecimal("r47_approved_limit"));
			obj.setR47_balance_outstanding(rs.getBigDecimal("r47_balance_outstanding"));
			obj.setR47_no_of_acct(rs.getBigDecimal("r47_no_of_acct"));

			obj.setR48_product(rs.getString("r48_product"));
			obj.setR48_approved_limit(rs.getBigDecimal("r48_approved_limit"));
			obj.setR48_balance_outstanding(rs.getBigDecimal("r48_balance_outstanding"));
			obj.setR48_no_of_acct(rs.getBigDecimal("r48_no_of_acct"));

			obj.setR49_product(rs.getString("r49_product"));
			obj.setR49_approved_limit(rs.getBigDecimal("r49_approved_limit"));
			obj.setR49_balance_outstanding(rs.getBigDecimal("r49_balance_outstanding"));
			obj.setR49_no_of_acct(rs.getBigDecimal("r49_no_of_acct"));

			obj.setR50_product(rs.getString("r50_product"));
			obj.setR50_approved_limit(rs.getBigDecimal("r50_approved_limit"));
			obj.setR50_balance_outstanding(rs.getBigDecimal("r50_balance_outstanding"));
			obj.setR50_no_of_acct(rs.getBigDecimal("r50_no_of_acct"));

			obj.setR51_product(rs.getString("r51_product"));
			obj.setR51_approved_limit(rs.getBigDecimal("r51_approved_limit"));
			obj.setR51_balance_outstanding(rs.getBigDecimal("r51_balance_outstanding"));
			obj.setR51_no_of_acct(rs.getBigDecimal("r51_no_of_acct"));

			obj.setR52_product(rs.getString("r52_product"));
			obj.setR52_approved_limit(rs.getBigDecimal("r52_approved_limit"));
			obj.setR52_balance_outstanding(rs.getBigDecimal("r52_balance_outstanding"));
			obj.setR52_no_of_acct(rs.getBigDecimal("r52_no_of_acct"));

			obj.setR53_product(rs.getString("r53_product"));
			obj.setR53_approved_limit(rs.getBigDecimal("r53_approved_limit"));
			obj.setR53_balance_outstanding(rs.getBigDecimal("r53_balance_outstanding"));
			obj.setR53_no_of_acct(rs.getBigDecimal("r53_no_of_acct"));

			obj.setR54_product(rs.getString("r54_product"));
			obj.setR54_approved_limit(rs.getBigDecimal("r54_approved_limit"));
			obj.setR54_balance_outstanding(rs.getBigDecimal("r54_balance_outstanding"));
			obj.setR54_no_of_acct(rs.getBigDecimal("r54_no_of_acct"));

			obj.setR55_product(rs.getString("r55_product"));
			obj.setR55_approved_limit(rs.getBigDecimal("r55_approved_limit"));
			obj.setR55_balance_outstanding(rs.getBigDecimal("r55_balance_outstanding"));
			obj.setR55_no_of_acct(rs.getBigDecimal("r55_no_of_acct"));

			obj.setR56_product(rs.getString("r56_product"));
			obj.setR56_approved_limit(rs.getBigDecimal("r56_approved_limit"));
			obj.setR56_balance_outstanding(rs.getBigDecimal("r56_balance_outstanding"));
			obj.setR56_no_of_acct(rs.getBigDecimal("r56_no_of_acct"));

			obj.setR57_product(rs.getString("r57_product"));
			obj.setR57_approved_limit(rs.getBigDecimal("r57_approved_limit"));
			obj.setR57_balance_outstanding(rs.getBigDecimal("r57_balance_outstanding"));
			obj.setR57_no_of_acct(rs.getBigDecimal("r57_no_of_acct"));

			obj.setR58_product(rs.getString("r58_product"));
			obj.setR58_approved_limit(rs.getBigDecimal("r58_approved_limit"));
			obj.setR58_balance_outstanding(rs.getBigDecimal("r58_balance_outstanding"));
			obj.setR58_no_of_acct(rs.getBigDecimal("r58_no_of_acct"));

			obj.setR59_product(rs.getString("r59_product"));
			obj.setR59_approved_limit(rs.getBigDecimal("r59_approved_limit"));
			obj.setR59_balance_outstanding(rs.getBigDecimal("r59_balance_outstanding"));
			obj.setR59_no_of_acct(rs.getBigDecimal("r59_no_of_acct"));

			obj.setR60_product(rs.getString("r60_product"));
			obj.setR60_approved_limit(rs.getBigDecimal("r60_approved_limit"));
			obj.setR60_balance_outstanding(rs.getBigDecimal("r60_balance_outstanding"));
			obj.setR60_no_of_acct(rs.getBigDecimal("r60_no_of_acct"));

			obj.setR61_product(rs.getString("r61_product"));
			obj.setR61_approved_limit(rs.getBigDecimal("r61_approved_limit"));
			obj.setR61_balance_outstanding(rs.getBigDecimal("r61_balance_outstanding"));
			obj.setR61_no_of_acct(rs.getBigDecimal("r61_no_of_acct"));

			obj.setR62_product(rs.getString("r62_product"));
			obj.setR62_approved_limit(rs.getBigDecimal("r62_approved_limit"));
			obj.setR62_balance_outstanding(rs.getBigDecimal("r62_balance_outstanding"));
			obj.setR62_no_of_acct(rs.getBigDecimal("r62_no_of_acct"));

			obj.setR63_product(rs.getString("r63_product"));
			obj.setR63_approved_limit(rs.getBigDecimal("r63_approved_limit"));
			obj.setR63_balance_outstanding(rs.getBigDecimal("r63_balance_outstanding"));
			obj.setR63_no_of_acct(rs.getBigDecimal("r63_no_of_acct"));

			obj.setR64_product(rs.getString("r64_product"));
			obj.setR64_approved_limit(rs.getBigDecimal("r64_approved_limit"));
			obj.setR64_balance_outstanding(rs.getBigDecimal("r64_balance_outstanding"));
			obj.setR64_no_of_acct(rs.getBigDecimal("r64_no_of_acct"));

			// COMMON FIELDS
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
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

	public static class M_LA1_Archival_Summary_Entity {

		private String r11_product;
		private BigDecimal r11_approved_limit;
		private BigDecimal r11_balance_outstanding;
		private BigDecimal r11_no_of_acct;

		private String r12_product;
		private BigDecimal r12_approved_limit;
		private BigDecimal r12_balance_outstanding;
		private BigDecimal r12_no_of_acct;

		private String r13_product;
		private BigDecimal r13_approved_limit;
		private BigDecimal r13_balance_outstanding;
		private BigDecimal r13_no_of_acct;

		private String r14_product;
		private BigDecimal r14_approved_limit;
		private BigDecimal r14_balance_outstanding;
		private BigDecimal r14_no_of_acct;

		private String r15_product;
		private BigDecimal r15_approved_limit;
		private BigDecimal r15_balance_outstanding;
		private BigDecimal r15_no_of_acct;

		private String r16_product;
		private BigDecimal r16_approved_limit;
		private BigDecimal r16_balance_outstanding;
		private BigDecimal r16_no_of_acct;

		private String r17_product;
		private BigDecimal r17_approved_limit;
		private BigDecimal r17_balance_outstanding;
		private BigDecimal r17_no_of_acct;

		private String r18_product;
		private BigDecimal r18_approved_limit;
		private BigDecimal r18_balance_outstanding;
		private BigDecimal r18_no_of_acct;

		private String r19_product;
		private BigDecimal r19_approved_limit;
		private BigDecimal r19_balance_outstanding;
		private BigDecimal r19_no_of_acct;

		private String r20_product;
		private BigDecimal r20_approved_limit;
		private BigDecimal r20_balance_outstanding;
		private BigDecimal r20_no_of_acct;

		private String r21_product;
		private BigDecimal r21_approved_limit;
		private BigDecimal r21_balance_outstanding;
		private BigDecimal r21_no_of_acct;

		private String r22_product;
		private BigDecimal r22_approved_limit;
		private BigDecimal r22_balance_outstanding;
		private BigDecimal r22_no_of_acct;

		private String r23_product;
		private BigDecimal r23_approved_limit;
		private BigDecimal r23_balance_outstanding;
		private BigDecimal r23_no_of_acct;

		private String r24_product;
		private BigDecimal r24_approved_limit;
		private BigDecimal r24_balance_outstanding;
		private BigDecimal r24_no_of_acct;

		private String r25_product;
		private BigDecimal r25_approved_limit;
		private BigDecimal r25_balance_outstanding;
		private BigDecimal r25_no_of_acct;

		private String r26_product;
		private BigDecimal r26_approved_limit;
		private BigDecimal r26_balance_outstanding;
		private BigDecimal r26_no_of_acct;

		private String r27_product;
		private BigDecimal r27_approved_limit;
		private BigDecimal r27_balance_outstanding;
		private BigDecimal r27_no_of_acct;

		private String r28_product;
		private BigDecimal r28_approved_limit;
		private BigDecimal r28_balance_outstanding;
		private BigDecimal r28_no_of_acct;

		private String r29_product;
		private BigDecimal r29_approved_limit;
		private BigDecimal r29_balance_outstanding;
		private BigDecimal r29_no_of_acct;

		private String r30_product;
		private BigDecimal r30_approved_limit;
		private BigDecimal r30_balance_outstanding;
		private BigDecimal r30_no_of_acct;

		private String r31_product;
		private BigDecimal r31_approved_limit;
		private BigDecimal r31_balance_outstanding;
		private BigDecimal r31_no_of_acct;

		private String r32_product;
		private BigDecimal r32_approved_limit;
		private BigDecimal r32_balance_outstanding;
		private BigDecimal r32_no_of_acct;

		private String r33_product;
		private BigDecimal r33_approved_limit;
		private BigDecimal r33_balance_outstanding;
		private BigDecimal r33_no_of_acct;

		private String r34_product;
		private BigDecimal r34_approved_limit;
		private BigDecimal r34_balance_outstanding;
		private BigDecimal r34_no_of_acct;

		private String r35_product;
		private BigDecimal r35_approved_limit;
		private BigDecimal r35_balance_outstanding;
		private BigDecimal r35_no_of_acct;

		private String r36_product;
		private BigDecimal r36_approved_limit;
		private BigDecimal r36_balance_outstanding;
		private BigDecimal r36_no_of_acct;

		private String r37_product;
		private BigDecimal r37_approved_limit;
		private BigDecimal r37_balance_outstanding;
		private BigDecimal r37_no_of_acct;

		private String r38_product;
		private BigDecimal r38_approved_limit;
		private BigDecimal r38_balance_outstanding;
		private BigDecimal r38_no_of_acct;

		private String r39_product;
		private BigDecimal r39_approved_limit;
		private BigDecimal r39_balance_outstanding;
		private BigDecimal r39_no_of_acct;

		private String r40_product;
		private BigDecimal r40_approved_limit;
		private BigDecimal r40_balance_outstanding;
		private BigDecimal r40_no_of_acct;

		private String r41_product;
		private BigDecimal r41_approved_limit;
		private BigDecimal r41_balance_outstanding;
		private BigDecimal r41_no_of_acct;

		private String r42_product;
		private BigDecimal r42_approved_limit;
		private BigDecimal r42_balance_outstanding;
		private BigDecimal r42_no_of_acct;

		private String r43_product;
		private BigDecimal r43_approved_limit;
		private BigDecimal r43_balance_outstanding;
		private BigDecimal r43_no_of_acct;

		private String r44_product;
		private BigDecimal r44_approved_limit;
		private BigDecimal r44_balance_outstanding;
		private BigDecimal r44_no_of_acct;

		private String r45_product;
		private BigDecimal r45_approved_limit;
		private BigDecimal r45_balance_outstanding;
		private BigDecimal r45_no_of_acct;

		private String r46_product;
		private BigDecimal r46_approved_limit;
		private BigDecimal r46_balance_outstanding;
		private BigDecimal r46_no_of_acct;

		private String r47_product;
		private BigDecimal r47_approved_limit;
		private BigDecimal r47_balance_outstanding;
		private BigDecimal r47_no_of_acct;

		private String r48_product;
		private BigDecimal r48_approved_limit;
		private BigDecimal r48_balance_outstanding;
		private BigDecimal r48_no_of_acct;

		private String r49_product;
		private BigDecimal r49_approved_limit;
		private BigDecimal r49_balance_outstanding;
		private BigDecimal r49_no_of_acct;

		private String r50_product;
		private BigDecimal r50_approved_limit;
		private BigDecimal r50_balance_outstanding;
		private BigDecimal r50_no_of_acct;

		private String r51_product;
		private BigDecimal r51_approved_limit;
		private BigDecimal r51_balance_outstanding;
		private BigDecimal r51_no_of_acct;

		private String r52_product;
		private BigDecimal r52_approved_limit;
		private BigDecimal r52_balance_outstanding;
		private BigDecimal r52_no_of_acct;

		private String r53_product;
		private BigDecimal r53_approved_limit;
		private BigDecimal r53_balance_outstanding;
		private BigDecimal r53_no_of_acct;

		private String r54_product;
		private BigDecimal r54_approved_limit;
		private BigDecimal r54_balance_outstanding;
		private BigDecimal r54_no_of_acct;

		private String r55_product;
		private BigDecimal r55_approved_limit;
		private BigDecimal r55_balance_outstanding;
		private BigDecimal r55_no_of_acct;

		private String r56_product;
		private BigDecimal r56_approved_limit;
		private BigDecimal r56_balance_outstanding;
		private BigDecimal r56_no_of_acct;

		private String r57_product;
		private BigDecimal r57_approved_limit;
		private BigDecimal r57_balance_outstanding;
		private BigDecimal r57_no_of_acct;

		private String r58_product;
		private BigDecimal r58_approved_limit;
		private BigDecimal r58_balance_outstanding;
		private BigDecimal r58_no_of_acct;

		private String r59_product;
		private BigDecimal r59_approved_limit;
		private BigDecimal r59_balance_outstanding;
		private BigDecimal r59_no_of_acct;

		private String r60_product;
		private BigDecimal r60_approved_limit;
		private BigDecimal r60_balance_outstanding;
		private BigDecimal r60_no_of_acct;

		private String r61_product;
		private BigDecimal r61_approved_limit;
		private BigDecimal r61_balance_outstanding;
		private BigDecimal r61_no_of_acct;

		private String r62_product;
		private BigDecimal r62_approved_limit;
		private BigDecimal r62_balance_outstanding;
		private BigDecimal r62_no_of_acct;

		private String r63_product;
		private BigDecimal r63_approved_limit;
		private BigDecimal r63_balance_outstanding;
		private BigDecimal r63_no_of_acct;

		private String r64_product;
		private BigDecimal r64_approved_limit;
		private BigDecimal r64_balance_outstanding;
		private BigDecimal r64_no_of_acct;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "REPORT_RESUBDATE")
		private Date REPORT_RESUBDATE;

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

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_approved_limit() {
			return r11_approved_limit;
		}

		public void setR11_approved_limit(BigDecimal r11_approved_limit) {
			this.r11_approved_limit = r11_approved_limit;
		}

		public BigDecimal getR11_balance_outstanding() {
			return r11_balance_outstanding;
		}

		public void setR11_balance_outstanding(BigDecimal r11_balance_outstanding) {
			this.r11_balance_outstanding = r11_balance_outstanding;
		}

		public BigDecimal getR11_no_of_acct() {
			return r11_no_of_acct;
		}

		public void setR11_no_of_acct(BigDecimal r11_no_of_acct) {
			this.r11_no_of_acct = r11_no_of_acct;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_approved_limit() {
			return r12_approved_limit;
		}

		public void setR12_approved_limit(BigDecimal r12_approved_limit) {
			this.r12_approved_limit = r12_approved_limit;
		}

		public BigDecimal getR12_balance_outstanding() {
			return r12_balance_outstanding;
		}

		public void setR12_balance_outstanding(BigDecimal r12_balance_outstanding) {
			this.r12_balance_outstanding = r12_balance_outstanding;
		}

		public BigDecimal getR12_no_of_acct() {
			return r12_no_of_acct;
		}

		public void setR12_no_of_acct(BigDecimal r12_no_of_acct) {
			this.r12_no_of_acct = r12_no_of_acct;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_approved_limit() {
			return r13_approved_limit;
		}

		public void setR13_approved_limit(BigDecimal r13_approved_limit) {
			this.r13_approved_limit = r13_approved_limit;
		}

		public BigDecimal getR13_balance_outstanding() {
			return r13_balance_outstanding;
		}

		public void setR13_balance_outstanding(BigDecimal r13_balance_outstanding) {
			this.r13_balance_outstanding = r13_balance_outstanding;
		}

		public BigDecimal getR13_no_of_acct() {
			return r13_no_of_acct;
		}

		public void setR13_no_of_acct(BigDecimal r13_no_of_acct) {
			this.r13_no_of_acct = r13_no_of_acct;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_approved_limit() {
			return r14_approved_limit;
		}

		public void setR14_approved_limit(BigDecimal r14_approved_limit) {
			this.r14_approved_limit = r14_approved_limit;
		}

		public BigDecimal getR14_balance_outstanding() {
			return r14_balance_outstanding;
		}

		public void setR14_balance_outstanding(BigDecimal r14_balance_outstanding) {
			this.r14_balance_outstanding = r14_balance_outstanding;
		}

		public BigDecimal getR14_no_of_acct() {
			return r14_no_of_acct;
		}

		public void setR14_no_of_acct(BigDecimal r14_no_of_acct) {
			this.r14_no_of_acct = r14_no_of_acct;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_approved_limit() {
			return r15_approved_limit;
		}

		public void setR15_approved_limit(BigDecimal r15_approved_limit) {
			this.r15_approved_limit = r15_approved_limit;
		}

		public BigDecimal getR15_balance_outstanding() {
			return r15_balance_outstanding;
		}

		public void setR15_balance_outstanding(BigDecimal r15_balance_outstanding) {
			this.r15_balance_outstanding = r15_balance_outstanding;
		}

		public BigDecimal getR15_no_of_acct() {
			return r15_no_of_acct;
		}

		public void setR15_no_of_acct(BigDecimal r15_no_of_acct) {
			this.r15_no_of_acct = r15_no_of_acct;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_approved_limit() {
			return r16_approved_limit;
		}

		public void setR16_approved_limit(BigDecimal r16_approved_limit) {
			this.r16_approved_limit = r16_approved_limit;
		}

		public BigDecimal getR16_balance_outstanding() {
			return r16_balance_outstanding;
		}

		public void setR16_balance_outstanding(BigDecimal r16_balance_outstanding) {
			this.r16_balance_outstanding = r16_balance_outstanding;
		}

		public BigDecimal getR16_no_of_acct() {
			return r16_no_of_acct;
		}

		public void setR16_no_of_acct(BigDecimal r16_no_of_acct) {
			this.r16_no_of_acct = r16_no_of_acct;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_approved_limit() {
			return r17_approved_limit;
		}

		public void setR17_approved_limit(BigDecimal r17_approved_limit) {
			this.r17_approved_limit = r17_approved_limit;
		}

		public BigDecimal getR17_balance_outstanding() {
			return r17_balance_outstanding;
		}

		public void setR17_balance_outstanding(BigDecimal r17_balance_outstanding) {
			this.r17_balance_outstanding = r17_balance_outstanding;
		}

		public BigDecimal getR17_no_of_acct() {
			return r17_no_of_acct;
		}

		public void setR17_no_of_acct(BigDecimal r17_no_of_acct) {
			this.r17_no_of_acct = r17_no_of_acct;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_approved_limit() {
			return r18_approved_limit;
		}

		public void setR18_approved_limit(BigDecimal r18_approved_limit) {
			this.r18_approved_limit = r18_approved_limit;
		}

		public BigDecimal getR18_balance_outstanding() {
			return r18_balance_outstanding;
		}

		public void setR18_balance_outstanding(BigDecimal r18_balance_outstanding) {
			this.r18_balance_outstanding = r18_balance_outstanding;
		}

		public BigDecimal getR18_no_of_acct() {
			return r18_no_of_acct;
		}

		public void setR18_no_of_acct(BigDecimal r18_no_of_acct) {
			this.r18_no_of_acct = r18_no_of_acct;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_approved_limit() {
			return r19_approved_limit;
		}

		public void setR19_approved_limit(BigDecimal r19_approved_limit) {
			this.r19_approved_limit = r19_approved_limit;
		}

		public BigDecimal getR19_balance_outstanding() {
			return r19_balance_outstanding;
		}

		public void setR19_balance_outstanding(BigDecimal r19_balance_outstanding) {
			this.r19_balance_outstanding = r19_balance_outstanding;
		}

		public BigDecimal getR19_no_of_acct() {
			return r19_no_of_acct;
		}

		public void setR19_no_of_acct(BigDecimal r19_no_of_acct) {
			this.r19_no_of_acct = r19_no_of_acct;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_approved_limit() {
			return r20_approved_limit;
		}

		public void setR20_approved_limit(BigDecimal r20_approved_limit) {
			this.r20_approved_limit = r20_approved_limit;
		}

		public BigDecimal getR20_balance_outstanding() {
			return r20_balance_outstanding;
		}

		public void setR20_balance_outstanding(BigDecimal r20_balance_outstanding) {
			this.r20_balance_outstanding = r20_balance_outstanding;
		}

		public BigDecimal getR20_no_of_acct() {
			return r20_no_of_acct;
		}

		public void setR20_no_of_acct(BigDecimal r20_no_of_acct) {
			this.r20_no_of_acct = r20_no_of_acct;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_approved_limit() {
			return r21_approved_limit;
		}

		public void setR21_approved_limit(BigDecimal r21_approved_limit) {
			this.r21_approved_limit = r21_approved_limit;
		}

		public BigDecimal getR21_balance_outstanding() {
			return r21_balance_outstanding;
		}

		public void setR21_balance_outstanding(BigDecimal r21_balance_outstanding) {
			this.r21_balance_outstanding = r21_balance_outstanding;
		}

		public BigDecimal getR21_no_of_acct() {
			return r21_no_of_acct;
		}

		public void setR21_no_of_acct(BigDecimal r21_no_of_acct) {
			this.r21_no_of_acct = r21_no_of_acct;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_approved_limit() {
			return r22_approved_limit;
		}

		public void setR22_approved_limit(BigDecimal r22_approved_limit) {
			this.r22_approved_limit = r22_approved_limit;
		}

		public BigDecimal getR22_balance_outstanding() {
			return r22_balance_outstanding;
		}

		public void setR22_balance_outstanding(BigDecimal r22_balance_outstanding) {
			this.r22_balance_outstanding = r22_balance_outstanding;
		}

		public BigDecimal getR22_no_of_acct() {
			return r22_no_of_acct;
		}

		public void setR22_no_of_acct(BigDecimal r22_no_of_acct) {
			this.r22_no_of_acct = r22_no_of_acct;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_approved_limit() {
			return r23_approved_limit;
		}

		public void setR23_approved_limit(BigDecimal r23_approved_limit) {
			this.r23_approved_limit = r23_approved_limit;
		}

		public BigDecimal getR23_balance_outstanding() {
			return r23_balance_outstanding;
		}

		public void setR23_balance_outstanding(BigDecimal r23_balance_outstanding) {
			this.r23_balance_outstanding = r23_balance_outstanding;
		}

		public BigDecimal getR23_no_of_acct() {
			return r23_no_of_acct;
		}

		public void setR23_no_of_acct(BigDecimal r23_no_of_acct) {
			this.r23_no_of_acct = r23_no_of_acct;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_approved_limit() {
			return r24_approved_limit;
		}

		public void setR24_approved_limit(BigDecimal r24_approved_limit) {
			this.r24_approved_limit = r24_approved_limit;
		}

		public BigDecimal getR24_balance_outstanding() {
			return r24_balance_outstanding;
		}

		public void setR24_balance_outstanding(BigDecimal r24_balance_outstanding) {
			this.r24_balance_outstanding = r24_balance_outstanding;
		}

		public BigDecimal getR24_no_of_acct() {
			return r24_no_of_acct;
		}

		public void setR24_no_of_acct(BigDecimal r24_no_of_acct) {
			this.r24_no_of_acct = r24_no_of_acct;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_approved_limit() {
			return r25_approved_limit;
		}

		public void setR25_approved_limit(BigDecimal r25_approved_limit) {
			this.r25_approved_limit = r25_approved_limit;
		}

		public BigDecimal getR25_balance_outstanding() {
			return r25_balance_outstanding;
		}

		public void setR25_balance_outstanding(BigDecimal r25_balance_outstanding) {
			this.r25_balance_outstanding = r25_balance_outstanding;
		}

		public BigDecimal getR25_no_of_acct() {
			return r25_no_of_acct;
		}

		public void setR25_no_of_acct(BigDecimal r25_no_of_acct) {
			this.r25_no_of_acct = r25_no_of_acct;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_approved_limit() {
			return r26_approved_limit;
		}

		public void setR26_approved_limit(BigDecimal r26_approved_limit) {
			this.r26_approved_limit = r26_approved_limit;
		}

		public BigDecimal getR26_balance_outstanding() {
			return r26_balance_outstanding;
		}

		public void setR26_balance_outstanding(BigDecimal r26_balance_outstanding) {
			this.r26_balance_outstanding = r26_balance_outstanding;
		}

		public BigDecimal getR26_no_of_acct() {
			return r26_no_of_acct;
		}

		public void setR26_no_of_acct(BigDecimal r26_no_of_acct) {
			this.r26_no_of_acct = r26_no_of_acct;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_approved_limit() {
			return r27_approved_limit;
		}

		public void setR27_approved_limit(BigDecimal r27_approved_limit) {
			this.r27_approved_limit = r27_approved_limit;
		}

		public BigDecimal getR27_balance_outstanding() {
			return r27_balance_outstanding;
		}

		public void setR27_balance_outstanding(BigDecimal r27_balance_outstanding) {
			this.r27_balance_outstanding = r27_balance_outstanding;
		}

		public BigDecimal getR27_no_of_acct() {
			return r27_no_of_acct;
		}

		public void setR27_no_of_acct(BigDecimal r27_no_of_acct) {
			this.r27_no_of_acct = r27_no_of_acct;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_approved_limit() {
			return r28_approved_limit;
		}

		public void setR28_approved_limit(BigDecimal r28_approved_limit) {
			this.r28_approved_limit = r28_approved_limit;
		}

		public BigDecimal getR28_balance_outstanding() {
			return r28_balance_outstanding;
		}

		public void setR28_balance_outstanding(BigDecimal r28_balance_outstanding) {
			this.r28_balance_outstanding = r28_balance_outstanding;
		}

		public BigDecimal getR28_no_of_acct() {
			return r28_no_of_acct;
		}

		public void setR28_no_of_acct(BigDecimal r28_no_of_acct) {
			this.r28_no_of_acct = r28_no_of_acct;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_approved_limit() {
			return r29_approved_limit;
		}

		public void setR29_approved_limit(BigDecimal r29_approved_limit) {
			this.r29_approved_limit = r29_approved_limit;
		}

		public BigDecimal getR29_balance_outstanding() {
			return r29_balance_outstanding;
		}

		public void setR29_balance_outstanding(BigDecimal r29_balance_outstanding) {
			this.r29_balance_outstanding = r29_balance_outstanding;
		}

		public BigDecimal getR29_no_of_acct() {
			return r29_no_of_acct;
		}

		public void setR29_no_of_acct(BigDecimal r29_no_of_acct) {
			this.r29_no_of_acct = r29_no_of_acct;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_approved_limit() {
			return r30_approved_limit;
		}

		public void setR30_approved_limit(BigDecimal r30_approved_limit) {
			this.r30_approved_limit = r30_approved_limit;
		}

		public BigDecimal getR30_balance_outstanding() {
			return r30_balance_outstanding;
		}

		public void setR30_balance_outstanding(BigDecimal r30_balance_outstanding) {
			this.r30_balance_outstanding = r30_balance_outstanding;
		}

		public BigDecimal getR30_no_of_acct() {
			return r30_no_of_acct;
		}

		public void setR30_no_of_acct(BigDecimal r30_no_of_acct) {
			this.r30_no_of_acct = r30_no_of_acct;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_approved_limit() {
			return r31_approved_limit;
		}

		public void setR31_approved_limit(BigDecimal r31_approved_limit) {
			this.r31_approved_limit = r31_approved_limit;
		}

		public BigDecimal getR31_balance_outstanding() {
			return r31_balance_outstanding;
		}

		public void setR31_balance_outstanding(BigDecimal r31_balance_outstanding) {
			this.r31_balance_outstanding = r31_balance_outstanding;
		}

		public BigDecimal getR31_no_of_acct() {
			return r31_no_of_acct;
		}

		public void setR31_no_of_acct(BigDecimal r31_no_of_acct) {
			this.r31_no_of_acct = r31_no_of_acct;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_approved_limit() {
			return r32_approved_limit;
		}

		public void setR32_approved_limit(BigDecimal r32_approved_limit) {
			this.r32_approved_limit = r32_approved_limit;
		}

		public BigDecimal getR32_balance_outstanding() {
			return r32_balance_outstanding;
		}

		public void setR32_balance_outstanding(BigDecimal r32_balance_outstanding) {
			this.r32_balance_outstanding = r32_balance_outstanding;
		}

		public BigDecimal getR32_no_of_acct() {
			return r32_no_of_acct;
		}

		public void setR32_no_of_acct(BigDecimal r32_no_of_acct) {
			this.r32_no_of_acct = r32_no_of_acct;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_approved_limit() {
			return r33_approved_limit;
		}

		public void setR33_approved_limit(BigDecimal r33_approved_limit) {
			this.r33_approved_limit = r33_approved_limit;
		}

		public BigDecimal getR33_balance_outstanding() {
			return r33_balance_outstanding;
		}

		public void setR33_balance_outstanding(BigDecimal r33_balance_outstanding) {
			this.r33_balance_outstanding = r33_balance_outstanding;
		}

		public BigDecimal getR33_no_of_acct() {
			return r33_no_of_acct;
		}

		public void setR33_no_of_acct(BigDecimal r33_no_of_acct) {
			this.r33_no_of_acct = r33_no_of_acct;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_approved_limit() {
			return r34_approved_limit;
		}

		public void setR34_approved_limit(BigDecimal r34_approved_limit) {
			this.r34_approved_limit = r34_approved_limit;
		}

		public BigDecimal getR34_balance_outstanding() {
			return r34_balance_outstanding;
		}

		public void setR34_balance_outstanding(BigDecimal r34_balance_outstanding) {
			this.r34_balance_outstanding = r34_balance_outstanding;
		}

		public BigDecimal getR34_no_of_acct() {
			return r34_no_of_acct;
		}

		public void setR34_no_of_acct(BigDecimal r34_no_of_acct) {
			this.r34_no_of_acct = r34_no_of_acct;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_approved_limit() {
			return r35_approved_limit;
		}

		public void setR35_approved_limit(BigDecimal r35_approved_limit) {
			this.r35_approved_limit = r35_approved_limit;
		}

		public BigDecimal getR35_balance_outstanding() {
			return r35_balance_outstanding;
		}

		public void setR35_balance_outstanding(BigDecimal r35_balance_outstanding) {
			this.r35_balance_outstanding = r35_balance_outstanding;
		}

		public BigDecimal getR35_no_of_acct() {
			return r35_no_of_acct;
		}

		public void setR35_no_of_acct(BigDecimal r35_no_of_acct) {
			this.r35_no_of_acct = r35_no_of_acct;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_approved_limit() {
			return r36_approved_limit;
		}

		public void setR36_approved_limit(BigDecimal r36_approved_limit) {
			this.r36_approved_limit = r36_approved_limit;
		}

		public BigDecimal getR36_balance_outstanding() {
			return r36_balance_outstanding;
		}

		public void setR36_balance_outstanding(BigDecimal r36_balance_outstanding) {
			this.r36_balance_outstanding = r36_balance_outstanding;
		}

		public BigDecimal getR36_no_of_acct() {
			return r36_no_of_acct;
		}

		public void setR36_no_of_acct(BigDecimal r36_no_of_acct) {
			this.r36_no_of_acct = r36_no_of_acct;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_approved_limit() {
			return r37_approved_limit;
		}

		public void setR37_approved_limit(BigDecimal r37_approved_limit) {
			this.r37_approved_limit = r37_approved_limit;
		}

		public BigDecimal getR37_balance_outstanding() {
			return r37_balance_outstanding;
		}

		public void setR37_balance_outstanding(BigDecimal r37_balance_outstanding) {
			this.r37_balance_outstanding = r37_balance_outstanding;
		}

		public BigDecimal getR37_no_of_acct() {
			return r37_no_of_acct;
		}

		public void setR37_no_of_acct(BigDecimal r37_no_of_acct) {
			this.r37_no_of_acct = r37_no_of_acct;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_approved_limit() {
			return r38_approved_limit;
		}

		public void setR38_approved_limit(BigDecimal r38_approved_limit) {
			this.r38_approved_limit = r38_approved_limit;
		}

		public BigDecimal getR38_balance_outstanding() {
			return r38_balance_outstanding;
		}

		public void setR38_balance_outstanding(BigDecimal r38_balance_outstanding) {
			this.r38_balance_outstanding = r38_balance_outstanding;
		}

		public BigDecimal getR38_no_of_acct() {
			return r38_no_of_acct;
		}

		public void setR38_no_of_acct(BigDecimal r38_no_of_acct) {
			this.r38_no_of_acct = r38_no_of_acct;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_approved_limit() {
			return r39_approved_limit;
		}

		public void setR39_approved_limit(BigDecimal r39_approved_limit) {
			this.r39_approved_limit = r39_approved_limit;
		}

		public BigDecimal getR39_balance_outstanding() {
			return r39_balance_outstanding;
		}

		public void setR39_balance_outstanding(BigDecimal r39_balance_outstanding) {
			this.r39_balance_outstanding = r39_balance_outstanding;
		}

		public BigDecimal getR39_no_of_acct() {
			return r39_no_of_acct;
		}

		public void setR39_no_of_acct(BigDecimal r39_no_of_acct) {
			this.r39_no_of_acct = r39_no_of_acct;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_approved_limit() {
			return r40_approved_limit;
		}

		public void setR40_approved_limit(BigDecimal r40_approved_limit) {
			this.r40_approved_limit = r40_approved_limit;
		}

		public BigDecimal getR40_balance_outstanding() {
			return r40_balance_outstanding;
		}

		public void setR40_balance_outstanding(BigDecimal r40_balance_outstanding) {
			this.r40_balance_outstanding = r40_balance_outstanding;
		}

		public BigDecimal getR40_no_of_acct() {
			return r40_no_of_acct;
		}

		public void setR40_no_of_acct(BigDecimal r40_no_of_acct) {
			this.r40_no_of_acct = r40_no_of_acct;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_approved_limit() {
			return r41_approved_limit;
		}

		public void setR41_approved_limit(BigDecimal r41_approved_limit) {
			this.r41_approved_limit = r41_approved_limit;
		}

		public BigDecimal getR41_balance_outstanding() {
			return r41_balance_outstanding;
		}

		public void setR41_balance_outstanding(BigDecimal r41_balance_outstanding) {
			this.r41_balance_outstanding = r41_balance_outstanding;
		}

		public BigDecimal getR41_no_of_acct() {
			return r41_no_of_acct;
		}

		public void setR41_no_of_acct(BigDecimal r41_no_of_acct) {
			this.r41_no_of_acct = r41_no_of_acct;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_approved_limit() {
			return r42_approved_limit;
		}

		public void setR42_approved_limit(BigDecimal r42_approved_limit) {
			this.r42_approved_limit = r42_approved_limit;
		}

		public BigDecimal getR42_balance_outstanding() {
			return r42_balance_outstanding;
		}

		public void setR42_balance_outstanding(BigDecimal r42_balance_outstanding) {
			this.r42_balance_outstanding = r42_balance_outstanding;
		}

		public BigDecimal getR42_no_of_acct() {
			return r42_no_of_acct;
		}

		public void setR42_no_of_acct(BigDecimal r42_no_of_acct) {
			this.r42_no_of_acct = r42_no_of_acct;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_approved_limit() {
			return r43_approved_limit;
		}

		public void setR43_approved_limit(BigDecimal r43_approved_limit) {
			this.r43_approved_limit = r43_approved_limit;
		}

		public BigDecimal getR43_balance_outstanding() {
			return r43_balance_outstanding;
		}

		public void setR43_balance_outstanding(BigDecimal r43_balance_outstanding) {
			this.r43_balance_outstanding = r43_balance_outstanding;
		}

		public BigDecimal getR43_no_of_acct() {
			return r43_no_of_acct;
		}

		public void setR43_no_of_acct(BigDecimal r43_no_of_acct) {
			this.r43_no_of_acct = r43_no_of_acct;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_approved_limit() {
			return r44_approved_limit;
		}

		public void setR44_approved_limit(BigDecimal r44_approved_limit) {
			this.r44_approved_limit = r44_approved_limit;
		}

		public BigDecimal getR44_balance_outstanding() {
			return r44_balance_outstanding;
		}

		public void setR44_balance_outstanding(BigDecimal r44_balance_outstanding) {
			this.r44_balance_outstanding = r44_balance_outstanding;
		}

		public BigDecimal getR44_no_of_acct() {
			return r44_no_of_acct;
		}

		public void setR44_no_of_acct(BigDecimal r44_no_of_acct) {
			this.r44_no_of_acct = r44_no_of_acct;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_approved_limit() {
			return r45_approved_limit;
		}

		public void setR45_approved_limit(BigDecimal r45_approved_limit) {
			this.r45_approved_limit = r45_approved_limit;
		}

		public BigDecimal getR45_balance_outstanding() {
			return r45_balance_outstanding;
		}

		public void setR45_balance_outstanding(BigDecimal r45_balance_outstanding) {
			this.r45_balance_outstanding = r45_balance_outstanding;
		}

		public BigDecimal getR45_no_of_acct() {
			return r45_no_of_acct;
		}

		public void setR45_no_of_acct(BigDecimal r45_no_of_acct) {
			this.r45_no_of_acct = r45_no_of_acct;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_approved_limit() {
			return r46_approved_limit;
		}

		public void setR46_approved_limit(BigDecimal r46_approved_limit) {
			this.r46_approved_limit = r46_approved_limit;
		}

		public BigDecimal getR46_balance_outstanding() {
			return r46_balance_outstanding;
		}

		public void setR46_balance_outstanding(BigDecimal r46_balance_outstanding) {
			this.r46_balance_outstanding = r46_balance_outstanding;
		}

		public BigDecimal getR46_no_of_acct() {
			return r46_no_of_acct;
		}

		public void setR46_no_of_acct(BigDecimal r46_no_of_acct) {
			this.r46_no_of_acct = r46_no_of_acct;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_approved_limit() {
			return r47_approved_limit;
		}

		public void setR47_approved_limit(BigDecimal r47_approved_limit) {
			this.r47_approved_limit = r47_approved_limit;
		}

		public BigDecimal getR47_balance_outstanding() {
			return r47_balance_outstanding;
		}

		public void setR47_balance_outstanding(BigDecimal r47_balance_outstanding) {
			this.r47_balance_outstanding = r47_balance_outstanding;
		}

		public BigDecimal getR47_no_of_acct() {
			return r47_no_of_acct;
		}

		public void setR47_no_of_acct(BigDecimal r47_no_of_acct) {
			this.r47_no_of_acct = r47_no_of_acct;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_approved_limit() {
			return r48_approved_limit;
		}

		public void setR48_approved_limit(BigDecimal r48_approved_limit) {
			this.r48_approved_limit = r48_approved_limit;
		}

		public BigDecimal getR48_balance_outstanding() {
			return r48_balance_outstanding;
		}

		public void setR48_balance_outstanding(BigDecimal r48_balance_outstanding) {
			this.r48_balance_outstanding = r48_balance_outstanding;
		}

		public BigDecimal getR48_no_of_acct() {
			return r48_no_of_acct;
		}

		public void setR48_no_of_acct(BigDecimal r48_no_of_acct) {
			this.r48_no_of_acct = r48_no_of_acct;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_approved_limit() {
			return r49_approved_limit;
		}

		public void setR49_approved_limit(BigDecimal r49_approved_limit) {
			this.r49_approved_limit = r49_approved_limit;
		}

		public BigDecimal getR49_balance_outstanding() {
			return r49_balance_outstanding;
		}

		public void setR49_balance_outstanding(BigDecimal r49_balance_outstanding) {
			this.r49_balance_outstanding = r49_balance_outstanding;
		}

		public BigDecimal getR49_no_of_acct() {
			return r49_no_of_acct;
		}

		public void setR49_no_of_acct(BigDecimal r49_no_of_acct) {
			this.r49_no_of_acct = r49_no_of_acct;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_approved_limit() {
			return r50_approved_limit;
		}

		public void setR50_approved_limit(BigDecimal r50_approved_limit) {
			this.r50_approved_limit = r50_approved_limit;
		}

		public BigDecimal getR50_balance_outstanding() {
			return r50_balance_outstanding;
		}

		public void setR50_balance_outstanding(BigDecimal r50_balance_outstanding) {
			this.r50_balance_outstanding = r50_balance_outstanding;
		}

		public BigDecimal getR50_no_of_acct() {
			return r50_no_of_acct;
		}

		public void setR50_no_of_acct(BigDecimal r50_no_of_acct) {
			this.r50_no_of_acct = r50_no_of_acct;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public BigDecimal getR51_approved_limit() {
			return r51_approved_limit;
		}

		public void setR51_approved_limit(BigDecimal r51_approved_limit) {
			this.r51_approved_limit = r51_approved_limit;
		}

		public BigDecimal getR51_balance_outstanding() {
			return r51_balance_outstanding;
		}

		public void setR51_balance_outstanding(BigDecimal r51_balance_outstanding) {
			this.r51_balance_outstanding = r51_balance_outstanding;
		}

		public BigDecimal getR51_no_of_acct() {
			return r51_no_of_acct;
		}

		public void setR51_no_of_acct(BigDecimal r51_no_of_acct) {
			this.r51_no_of_acct = r51_no_of_acct;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public BigDecimal getR52_approved_limit() {
			return r52_approved_limit;
		}

		public void setR52_approved_limit(BigDecimal r52_approved_limit) {
			this.r52_approved_limit = r52_approved_limit;
		}

		public BigDecimal getR52_balance_outstanding() {
			return r52_balance_outstanding;
		}

		public void setR52_balance_outstanding(BigDecimal r52_balance_outstanding) {
			this.r52_balance_outstanding = r52_balance_outstanding;
		}

		public BigDecimal getR52_no_of_acct() {
			return r52_no_of_acct;
		}

		public void setR52_no_of_acct(BigDecimal r52_no_of_acct) {
			this.r52_no_of_acct = r52_no_of_acct;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public BigDecimal getR53_approved_limit() {
			return r53_approved_limit;
		}

		public void setR53_approved_limit(BigDecimal r53_approved_limit) {
			this.r53_approved_limit = r53_approved_limit;
		}

		public BigDecimal getR53_balance_outstanding() {
			return r53_balance_outstanding;
		}

		public void setR53_balance_outstanding(BigDecimal r53_balance_outstanding) {
			this.r53_balance_outstanding = r53_balance_outstanding;
		}

		public BigDecimal getR53_no_of_acct() {
			return r53_no_of_acct;
		}

		public void setR53_no_of_acct(BigDecimal r53_no_of_acct) {
			this.r53_no_of_acct = r53_no_of_acct;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public BigDecimal getR54_approved_limit() {
			return r54_approved_limit;
		}

		public void setR54_approved_limit(BigDecimal r54_approved_limit) {
			this.r54_approved_limit = r54_approved_limit;
		}

		public BigDecimal getR54_balance_outstanding() {
			return r54_balance_outstanding;
		}

		public void setR54_balance_outstanding(BigDecimal r54_balance_outstanding) {
			this.r54_balance_outstanding = r54_balance_outstanding;
		}

		public BigDecimal getR54_no_of_acct() {
			return r54_no_of_acct;
		}

		public void setR54_no_of_acct(BigDecimal r54_no_of_acct) {
			this.r54_no_of_acct = r54_no_of_acct;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public BigDecimal getR55_approved_limit() {
			return r55_approved_limit;
		}

		public void setR55_approved_limit(BigDecimal r55_approved_limit) {
			this.r55_approved_limit = r55_approved_limit;
		}

		public BigDecimal getR55_balance_outstanding() {
			return r55_balance_outstanding;
		}

		public void setR55_balance_outstanding(BigDecimal r55_balance_outstanding) {
			this.r55_balance_outstanding = r55_balance_outstanding;
		}

		public BigDecimal getR55_no_of_acct() {
			return r55_no_of_acct;
		}

		public void setR55_no_of_acct(BigDecimal r55_no_of_acct) {
			this.r55_no_of_acct = r55_no_of_acct;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public BigDecimal getR56_approved_limit() {
			return r56_approved_limit;
		}

		public void setR56_approved_limit(BigDecimal r56_approved_limit) {
			this.r56_approved_limit = r56_approved_limit;
		}

		public BigDecimal getR56_balance_outstanding() {
			return r56_balance_outstanding;
		}

		public void setR56_balance_outstanding(BigDecimal r56_balance_outstanding) {
			this.r56_balance_outstanding = r56_balance_outstanding;
		}

		public BigDecimal getR56_no_of_acct() {
			return r56_no_of_acct;
		}

		public void setR56_no_of_acct(BigDecimal r56_no_of_acct) {
			this.r56_no_of_acct = r56_no_of_acct;
		}

		public String getR57_product() {
			return r57_product;
		}

		public void setR57_product(String r57_product) {
			this.r57_product = r57_product;
		}

		public BigDecimal getR57_approved_limit() {
			return r57_approved_limit;
		}

		public void setR57_approved_limit(BigDecimal r57_approved_limit) {
			this.r57_approved_limit = r57_approved_limit;
		}

		public BigDecimal getR57_balance_outstanding() {
			return r57_balance_outstanding;
		}

		public void setR57_balance_outstanding(BigDecimal r57_balance_outstanding) {
			this.r57_balance_outstanding = r57_balance_outstanding;
		}

		public BigDecimal getR57_no_of_acct() {
			return r57_no_of_acct;
		}

		public void setR57_no_of_acct(BigDecimal r57_no_of_acct) {
			this.r57_no_of_acct = r57_no_of_acct;
		}

		public String getR58_product() {
			return r58_product;
		}

		public void setR58_product(String r58_product) {
			this.r58_product = r58_product;
		}

		public BigDecimal getR58_approved_limit() {
			return r58_approved_limit;
		}

		public void setR58_approved_limit(BigDecimal r58_approved_limit) {
			this.r58_approved_limit = r58_approved_limit;
		}

		public BigDecimal getR58_balance_outstanding() {
			return r58_balance_outstanding;
		}

		public void setR58_balance_outstanding(BigDecimal r58_balance_outstanding) {
			this.r58_balance_outstanding = r58_balance_outstanding;
		}

		public BigDecimal getR58_no_of_acct() {
			return r58_no_of_acct;
		}

		public void setR58_no_of_acct(BigDecimal r58_no_of_acct) {
			this.r58_no_of_acct = r58_no_of_acct;
		}

		public String getR59_product() {
			return r59_product;
		}

		public void setR59_product(String r59_product) {
			this.r59_product = r59_product;
		}

		public BigDecimal getR59_approved_limit() {
			return r59_approved_limit;
		}

		public void setR59_approved_limit(BigDecimal r59_approved_limit) {
			this.r59_approved_limit = r59_approved_limit;
		}

		public BigDecimal getR59_balance_outstanding() {
			return r59_balance_outstanding;
		}

		public void setR59_balance_outstanding(BigDecimal r59_balance_outstanding) {
			this.r59_balance_outstanding = r59_balance_outstanding;
		}

		public BigDecimal getR59_no_of_acct() {
			return r59_no_of_acct;
		}

		public void setR59_no_of_acct(BigDecimal r59_no_of_acct) {
			this.r59_no_of_acct = r59_no_of_acct;
		}

		public String getR60_product() {
			return r60_product;
		}

		public void setR60_product(String r60_product) {
			this.r60_product = r60_product;
		}

		public BigDecimal getR60_approved_limit() {
			return r60_approved_limit;
		}

		public void setR60_approved_limit(BigDecimal r60_approved_limit) {
			this.r60_approved_limit = r60_approved_limit;
		}

		public BigDecimal getR60_balance_outstanding() {
			return r60_balance_outstanding;
		}

		public void setR60_balance_outstanding(BigDecimal r60_balance_outstanding) {
			this.r60_balance_outstanding = r60_balance_outstanding;
		}

		public BigDecimal getR60_no_of_acct() {
			return r60_no_of_acct;
		}

		public void setR60_no_of_acct(BigDecimal r60_no_of_acct) {
			this.r60_no_of_acct = r60_no_of_acct;
		}

		public String getR61_product() {
			return r61_product;
		}

		public void setR61_product(String r61_product) {
			this.r61_product = r61_product;
		}

		public BigDecimal getR61_approved_limit() {
			return r61_approved_limit;
		}

		public void setR61_approved_limit(BigDecimal r61_approved_limit) {
			this.r61_approved_limit = r61_approved_limit;
		}

		public BigDecimal getR61_balance_outstanding() {
			return r61_balance_outstanding;
		}

		public void setR61_balance_outstanding(BigDecimal r61_balance_outstanding) {
			this.r61_balance_outstanding = r61_balance_outstanding;
		}

		public BigDecimal getR61_no_of_acct() {
			return r61_no_of_acct;
		}

		public void setR61_no_of_acct(BigDecimal r61_no_of_acct) {
			this.r61_no_of_acct = r61_no_of_acct;
		}

		public String getR62_product() {
			return r62_product;
		}

		public void setR62_product(String r62_product) {
			this.r62_product = r62_product;
		}

		public BigDecimal getR62_approved_limit() {
			return r62_approved_limit;
		}

		public void setR62_approved_limit(BigDecimal r62_approved_limit) {
			this.r62_approved_limit = r62_approved_limit;
		}

		public BigDecimal getR62_balance_outstanding() {
			return r62_balance_outstanding;
		}

		public void setR62_balance_outstanding(BigDecimal r62_balance_outstanding) {
			this.r62_balance_outstanding = r62_balance_outstanding;
		}

		public BigDecimal getR62_no_of_acct() {
			return r62_no_of_acct;
		}

		public void setR62_no_of_acct(BigDecimal r62_no_of_acct) {
			this.r62_no_of_acct = r62_no_of_acct;
		}

		public String getR63_product() {
			return r63_product;
		}

		public void setR63_product(String r63_product) {
			this.r63_product = r63_product;
		}

		public BigDecimal getR63_approved_limit() {
			return r63_approved_limit;
		}

		public void setR63_approved_limit(BigDecimal r63_approved_limit) {
			this.r63_approved_limit = r63_approved_limit;
		}

		public BigDecimal getR63_balance_outstanding() {
			return r63_balance_outstanding;
		}

		public void setR63_balance_outstanding(BigDecimal r63_balance_outstanding) {
			this.r63_balance_outstanding = r63_balance_outstanding;
		}

		public BigDecimal getR63_no_of_acct() {
			return r63_no_of_acct;
		}

		public void setR63_no_of_acct(BigDecimal r63_no_of_acct) {
			this.r63_no_of_acct = r63_no_of_acct;
		}

		public String getR64_product() {
			return r64_product;
		}

		public void setR64_product(String r64_product) {
			this.r64_product = r64_product;
		}

		public BigDecimal getR64_approved_limit() {
			return r64_approved_limit;
		}

		public void setR64_approved_limit(BigDecimal r64_approved_limit) {
			this.r64_approved_limit = r64_approved_limit;
		}

		public BigDecimal getR64_balance_outstanding() {
			return r64_balance_outstanding;
		}

		public void setR64_balance_outstanding(BigDecimal r64_balance_outstanding) {
			this.r64_balance_outstanding = r64_balance_outstanding;
		}

		public BigDecimal getR64_no_of_acct() {
			return r64_no_of_acct;
		}

		public void setR64_no_of_acct(BigDecimal r64_no_of_acct) {
			this.r64_no_of_acct = r64_no_of_acct;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date REPORT_RESUBDATE) {
			this.REPORT_RESUBDATE = REPORT_RESUBDATE;
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

	}

	// DETAIL ROW MAPPER
	class M_LA1DetaillRowMapper implements RowMapper<M_LA1_Detail_Entity> {

		@Override
		public M_LA1_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA1_Detail_Entity obj = new M_LA1_Detail_Entity();

			obj.setSno(rs.getLong("SNO"));
			obj.setCust_id(rs.getString("cust_id"));
			obj.setAcct_number(rs.getString("acct_number"));
			obj.setAcct_name(rs.getString("acct_name"));
			obj.setData_type(rs.getString("data_type"));
			obj.setReport_label(rs.getString("report_label"));
			obj.setReport_addl_criteria_1(rs.getString("report_addl_criteria_1"));
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

			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));
			obj.setSanction_limit(rs.getBigDecimal("sanction_limit"));
			obj.setSegment(rs.getString("segment"));
			obj.setReport_addl_criteria_2(rs.getString("report_addl_criteria_2"));
			obj.setReport_addl_criteria_3(rs.getString("report_addl_criteria_3"));
			obj.setSchm_desc(rs.getString("schm_desc"));

			return obj;
		}
	}

	public class M_LA1_Detail_Entity {

		private Long sno;
		private String cust_id;
		private String acct_number;
		private String acct_name;
		private String data_type;
		private String report_label;
		private String report_addl_criteria_1;
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
		private String entity_flg;
		private String modify_flg;
		private String del_flg;
		private BigDecimal sanction_limit;
		private String segment;
		private String report_addl_criteria_2;
		private String report_addl_criteria_3;
		private String schm_desc;

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

		public BigDecimal getSanction_limit() {
			return sanction_limit;
		}

		public void setSanction_limit(BigDecimal sanction_limit) {
			this.sanction_limit = sanction_limit;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
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

		public String getSchm_desc() {
			return schm_desc;
		}

		public void setSchm_desc(String schm_desc) {
			this.schm_desc = schm_desc;
		}
	}

	// DETAIL ARCHIVAL ROW MAPPER
	class M_LA1ArchivalDetaillRowMapper implements RowMapper<M_LA1_Archival_Detail_Entity> {

		@Override
		public M_LA1_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA1_Archival_Detail_Entity obj = new M_LA1_Archival_Detail_Entity();

			obj.setSno(rs.getLong("SNO"));
			obj.setCust_id(rs.getString("cust_id"));
			obj.setAcct_number(rs.getString("acct_number"));
			obj.setAcct_name(rs.getString("acct_name"));
			obj.setData_type(rs.getString("data_type"));
			obj.setReport_label(rs.getString("report_label"));
			obj.setReport_addl_criteria_1(rs.getString("report_addl_criteria_1"));
			obj.setReport_remarks(rs.getString("report_remarks"));
			obj.setModification_remarks(rs.getString("modification_remarks"));
			obj.setData_entry_version(rs.getString("data_entry_version"));
			obj.setAcct_balance_in_pula(rs.getBigDecimal("acct_balance_in_pula"));

			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_name(rs.getString("report_name"));
			obj.setCreate_user(rs.getString("create_user"));
			obj.setCreate_time(rs.getDate("create_time"));
			obj.setModify_user(rs.getString("modify_user"));
			obj.setModify_time(rs.getDate("modify_time"));
			obj.setVerify_user(rs.getString("verify_user"));
			obj.setVerify_time(rs.getDate("verify_time"));

			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));
			obj.setSanction_limit(rs.getBigDecimal("sanction_limit"));
			obj.setSegment(rs.getString("segment"));
			obj.setReport_addl_criteria_2(rs.getString("report_addl_criteria_2"));
			obj.setReport_addl_criteria_3(rs.getString("report_addl_criteria_3"));
			obj.setSchm_desc(rs.getString("schm_desc"));

			return obj;
		}
	}

	public class M_LA1_Archival_Detail_Entity {

		private Long sno;
		private String cust_id;
		private String acct_number;
		private String acct_name;
		private String data_type;
		private String report_label;
		private String report_addl_criteria_1;
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
		private String entity_flg;
		private String modify_flg;
		private String del_flg;
		private BigDecimal sanction_limit;
		private String segment;
		private String report_addl_criteria_2;
		private String report_addl_criteria_3;
		private String schm_desc;

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

		public BigDecimal getSanction_limit() {
			return sanction_limit;
		}

		public void setSanction_limit(BigDecimal sanction_limit) {
			this.sanction_limit = sanction_limit;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
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

		public String getSchm_desc() {
			return schm_desc;
		}

		public void setSchm_desc(String schm_desc) {
			this.schm_desc = schm_desc;
		}
	}

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");


	public ModelAndView getM_LA1View(String reportId, String fromdate, String todate, String currency, String dtltype,
	        Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

	    ModelAndView mv = new ModelAndView();

	    String userid = (String) req1.getSession().getAttribute("USERID");
	    System.out.println("User Id Maker and Checker: " + userid);
	    String role = userProfileRep.getUserRole(userid);
	    md.addAttribute("role", role);
	    System.out.println("Role: " + role);

	    Session hs = sessionFactory.getCurrentSession();
	    int pageSize = pageable.getPageSize();
	    int currentPage = pageable.getPageNumber();
	    int startItem = currentPage * pageSize;

	    System.out.println("M_LA1 View Called");
	    System.out.println("Type = " + type);
	    System.out.println("Version = " + version);

	    if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

	        List<M_LA1_Archival_Summary_Entity> T1Master = new ArrayList<>();

	        try {

	            Date dt = dateformat.parse(todate);

	            T1Master = getdatabydateListarchival(dt, version);
	            System.out.println(type + " Summary size = " + T1Master.size());

	            mv.addObject("REPORT_DATE", dateformat.format(dt));
	            mv.addObject("allowdetail", getishighestversion(dt, version));

	        } catch (ParseException e) {
	            e.printStackTrace();
	        }

	        mv.addObject("reportsummary", T1Master);

	    } else {

	        List<M_LA1_Summary_Entity> T1Master = new ArrayList<>();

	        try {

	            Date dt = dateformat.parse(todate);

	            T1Master = getDataByDate(dt);
	            System.out.println("Summary size = " + T1Master.size());

	            mv.addObject("REPORT_DATE", dateformat.format(dt));

	        } catch (ParseException e) {
	            e.printStackTrace();
	        }

	        mv.addObject("reportsummary", T1Master);
	    }

	    mv.setViewName("BRRS/M_LA1");
	    mv.addObject("displaymode", "summary");

	    System.out.println("scv" + mv.getViewName());
	    System.out.println("View Loaded: " + mv.getViewName());

	    return mv;
	}
	
	// =========================
	// MODEL AND VIEW METHOD detail
	//=========================
	
	public ModelAndView getM_LA1currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version, HttpServletRequest req1,
			Model md) {

		ModelAndView mv = new ModelAndView("BRRS/M_LA1");

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalRecords = 0;

		try {
// ✅ Parse toDate
			Date parsedDate = null;

	        if (todate != null && !todate.isEmpty()) {
	            parsedDate = dateformat.parse(todate);
	        }

	        String reportLabel = null;
	        String reportAddlCriteria1 = null;
	        String reportAddlCriteria2 = null;
	        String reportAddlCriteria3 = null;

	        if (filter != null && filter.contains(",")) {

	            String[] parts = filter.split(",");

	            if (parts.length >= 4) {
	                reportLabel = parts[0];
	                reportAddlCriteria1 = parts[1];
	                reportAddlCriteria2 = parts[2];
	                reportAddlCriteria3 = parts[3];
	            }
	        }

			// ARCHIVAL / RESUB MODE
			
			if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

				System.out.println(type + " DETAIL MODE");

				List<M_LA1_Archival_Detail_Entity> detailList;

// 🔹 Filtered (ROWID + COLUMNID)
				  if (reportLabel != null) {
					  
					logger.info("➡ ARCHIVAL DETAIL QUERY TRIGGERED (with filters)");
					detailList = GetArchivalDataByRowIdAndColumnId(
	                        reportLabel,
	                        reportAddlCriteria1,
	                        reportAddlCriteria2,
	                        reportAddlCriteria3,
	                        parsedDate,version );

				} else {
					logger.info("➡ ARCHIVAL LIST QUERY TRIGGERED (with pagination)");
					detailList = getArchivalDetaildatabydateList(parsedDate, version);
					
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);
				mv.addObject("allowdetail", getishighestversion(parsedDate, version != null ? new BigDecimal(version) : null));
				System.out.println(type + " DETAIL COUNT: " + detailList.size());
			}

			// CURRENT MODE
			
			else {

				logger.info("Fetching CURRENT data for M_LA1");

				List<M_LA1_Detail_Entity> detailList;

				 if (reportLabel != null) {

					 detailList = GetDetailDataByRowIdAndColumnId(
		                        reportLabel,
		                        reportAddlCriteria1,
		                        reportAddlCriteria2,
		                        reportAddlCriteria3,
		                        parsedDate);

				} else {
					logger.info("➡ CURRENT LIST QUERY TRIGGERED (with pagination)");
					detailList = getDetaildatabydateList(parsedDate);
				
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", e.getMessage());
		}
// ✅ Common model attributes
		mv.setViewName("BRRS/M_LA1");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}



	//Helper for null/empty check
	private boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	public byte[] BRRS_M_LA1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_LA1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		List<M_LA1_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for LA1 report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
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
					M_LA1_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getREPORT_DATE() != null) {
						cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row12
					// Column B
					row = sheet.getRow(11);
					cell1 = row.getCell(1);
					if (record.getR12_approved_limit() != null) {
						cell1.setCellValue(record.getR12_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR12_balance_outstanding() != null) {
						cell2.setCellValue(record.getR12_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_no_of_acct() != null) {
						cell3.setCellValue(record.getR12_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR13_approved_limit() != null) {
						cell1.setCellValue(record.getR13_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row13
					// Column C
					cell2 = row.createCell(2);
					if (record.getR13_balance_outstanding() != null) {
						cell2.setCellValue(record.getR13_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_no_of_acct() != null) {
						cell3.setCellValue(record.getR13_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR14_approved_limit() != null) {
						cell1.setCellValue(record.getR14_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row14
					// Column C
					cell2 = row.getCell(2);
					if (record.getR14_balance_outstanding() != null) {
						cell2.setCellValue(record.getR14_balance_outstanding().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_no_of_acct() != null) {
						cell3.setCellValue(record.getR14_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR16_approved_limit() != null) {
						cell1.setCellValue(record.getR16_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row16
					// Column C
					cell2 = row.createCell(2);
					if (record.getR16_balance_outstanding() != null) {
						cell2.setCellValue(record.getR16_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_no_of_acct() != null) {
						cell3.setCellValue(record.getR16_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR17_approved_limit() != null) {
						cell1.setCellValue(record.getR17_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row17
					// Column C
					cell2 = row.createCell(2);
					if (record.getR17_balance_outstanding() != null) {
						cell2.setCellValue(record.getR17_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					// Column D
					cell3 = row.createCell(3);
					if (record.getR17_no_of_acct() != null) {
						cell3.setCellValue(record.getR17_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR18_approved_limit() != null) {
						cell1.setCellValue(record.getR18_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row18
					// Column C
					cell2 = row.createCell(2);
					if (record.getR18_balance_outstanding() != null) {
						cell2.setCellValue(record.getR18_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					// Column D
					cell3 = row.createCell(3);
					if (record.getR18_no_of_acct() != null) {
						cell3.setCellValue(record.getR18_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR19_approved_limit() != null) {
						cell1.setCellValue(record.getR19_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row19
					// Column C
					cell2 = row.createCell(2);
					if (record.getR19_balance_outstanding() != null) {
						cell2.setCellValue(record.getR19_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					// Column D
					cell3 = row.createCell(3);
					if (record.getR19_no_of_acct() != null) {
						cell3.setCellValue(record.getR19_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR20_approved_limit() != null) {
						cell1.setCellValue(record.getR20_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row20
					// Column C
					cell2 = row.createCell(2);
					if (record.getR20_balance_outstanding() != null) {
						cell2.setCellValue(record.getR20_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_no_of_acct() != null) {
						cell3.setCellValue(record.getR20_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR21_approved_limit() != null) {
						cell1.setCellValue(record.getR21_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row21
					// Column C
					cell2 = row.createCell(2);
					if (record.getR21_balance_outstanding() != null) {
						cell2.setCellValue(record.getR21_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_no_of_acct() != null) {
						cell3.setCellValue(record.getR21_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR22_approved_limit() != null) {
						cell1.setCellValue(record.getR22_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row22
					// Column C
					cell2 = row.createCell(2);
					if (record.getR22_balance_outstanding() != null) {
						cell2.setCellValue(record.getR22_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_no_of_acct() != null) {
						cell3.setCellValue(record.getR22_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR23_approved_limit() != null) {
						cell1.setCellValue(record.getR23_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row23
					// Column C
					cell2 = row.createCell(2);
					if (record.getR23_balance_outstanding() != null) {
						cell2.setCellValue(record.getR23_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_no_of_acct() != null) {
						cell3.setCellValue(record.getR23_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR24_approved_limit() != null) {
						cell1.setCellValue(record.getR24_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row24
					// Column C
					cell2 = row.createCell(2);
					if (record.getR24_balance_outstanding() != null) {
						cell2.setCellValue(record.getR24_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_no_of_acct() != null) {
						cell3.setCellValue(record.getR24_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR25_approved_limit() != null) {
						cell1.setCellValue(record.getR25_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row25
					// Column C
					cell2 = row.createCell(2);
					if (record.getR25_balance_outstanding() != null) {
						cell2.setCellValue(record.getR25_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_no_of_acct() != null) {
						cell3.setCellValue(record.getR25_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR26_approved_limit() != null) {
						cell1.setCellValue(record.getR26_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row26
					// Column C
					cell2 = row.createCell(2);
					if (record.getR26_balance_outstanding() != null) {
						cell2.setCellValue(record.getR26_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_no_of_acct() != null) {
						cell3.setCellValue(record.getR26_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR27_approved_limit() != null) {
						cell1.setCellValue(record.getR27_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row27
					// Column C
					cell2 = row.createCell(2);
					if (record.getR27_balance_outstanding() != null) {
						cell2.setCellValue(record.getR27_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_no_of_acct() != null) {
						cell3.setCellValue(record.getR27_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR28_approved_limit() != null) {
						cell1.setCellValue(record.getR28_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row28
					// Column C
					cell2 = row.createCell(2);
					if (record.getR28_balance_outstanding() != null) {
						cell2.setCellValue(record.getR28_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_no_of_acct() != null) {
						cell3.setCellValue(record.getR28_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR30_approved_limit() != null) {
						cell1.setCellValue(record.getR30_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row30
					// Column C
					cell2 = row.createCell(2);
					if (record.getR30_balance_outstanding() != null) {
						cell2.setCellValue(record.getR30_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					// Column D
					cell3 = row.createCell(3);
					if (record.getR30_no_of_acct() != null) {
						cell3.setCellValue(record.getR30_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR31_approved_limit() != null) {
						cell1.setCellValue(record.getR31_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row31
					// Column C
					cell2 = row.createCell(2);
					if (record.getR31_balance_outstanding() != null) {
						cell2.setCellValue(record.getR31_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_no_of_acct() != null) {
						cell3.setCellValue(record.getR31_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR32_approved_limit() != null) {
						cell1.setCellValue(record.getR32_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row32
					// Column C
					cell2 = row.createCell(2);
					if (record.getR32_balance_outstanding() != null) {
						cell2.setCellValue(record.getR32_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row32
					// Column D
					cell3 = row.createCell(3);
					if (record.getR32_no_of_acct() != null) {
						cell3.setCellValue(record.getR32_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR33_approved_limit() != null) {
						cell1.setCellValue(record.getR33_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row33
					// Column C
					cell2 = row.createCell(2);
					if (record.getR33_balance_outstanding() != null) {
						cell2.setCellValue(record.getR33_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR33_no_of_acct() != null) {
						cell3.setCellValue(record.getR33_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR34_approved_limit() != null) {
						cell1.setCellValue(record.getR34_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row34
					// Column C
					cell2 = row.createCell(2);
					if (record.getR34_balance_outstanding() != null) {
						cell2.setCellValue(record.getR34_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row34
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_no_of_acct() != null) {
						cell3.setCellValue(record.getR34_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR35_approved_limit() != null) {
						cell1.setCellValue(record.getR35_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row35
					// Column C
					cell2 = row.createCell(2);
					if (record.getR35_balance_outstanding() != null) {
						cell2.setCellValue(record.getR35_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_no_of_acct() != null) {
						cell3.setCellValue(record.getR35_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR36_approved_limit() != null) {
						cell1.setCellValue(record.getR36_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row36
					// Column C
					cell2 = row.createCell(2);
					if (record.getR36_balance_outstanding() != null) {
						cell2.setCellValue(record.getR36_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row36
					// Column D
					cell3 = row.createCell(3);
					if (record.getR36_no_of_acct() != null) {
						cell3.setCellValue(record.getR36_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR37_approved_limit() != null) {
						cell1.setCellValue(record.getR37_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row37
					// Column C
					cell2 = row.createCell(2);
					if (record.getR37_balance_outstanding() != null) {
						cell2.setCellValue(record.getR37_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_no_of_acct() != null) {
						cell3.setCellValue(record.getR37_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR39_approved_limit() != null) {
						cell1.setCellValue(record.getR39_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row39
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_balance_outstanding() != null) {
						cell2.setCellValue(record.getR39_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_no_of_acct() != null) {
						cell3.setCellValue(record.getR39_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR40_approved_limit() != null) {
						cell1.setCellValue(record.getR40_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_balance_outstanding() != null) {
						cell2.setCellValue(record.getR40_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_no_of_acct() != null) {
						cell3.setCellValue(record.getR40_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR42_approved_limit() != null) {
						cell1.setCellValue(record.getR42_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row42
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_balance_outstanding() != null) {
						cell2.setCellValue(record.getR42_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row42
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_no_of_acct() != null) {
						cell3.setCellValue(record.getR42_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row43
					row = sheet.getRow(42);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR43_approved_limit() != null) {
						cell1.setCellValue(record.getR43_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row43
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_balance_outstanding() != null) {
						cell2.setCellValue(record.getR43_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row43
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_no_of_acct() != null) {
						cell3.setCellValue(record.getR43_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR45_approved_limit() != null) {
						cell1.setCellValue(record.getR45_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR45_balance_outstanding() != null) {
						cell2.setCellValue(record.getR45_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row45
					// Column D
					cell3 = row.createCell(3);
					if (record.getR45_no_of_acct() != null) {
						cell3.setCellValue(record.getR45_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR46_approved_limit() != null) {
						cell1.setCellValue(record.getR46_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row46
					// Column C
					cell2 = row.createCell(2);
					if (record.getR46_balance_outstanding() != null) {
						cell2.setCellValue(record.getR46_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row46
					// Column D
					cell3 = row.createCell(3);
					if (record.getR46_no_of_acct() != null) {
						cell3.setCellValue(record.getR46_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR47_approved_limit() != null) {
						cell1.setCellValue(record.getR47_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row47
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_balance_outstanding() != null) {
						cell2.setCellValue(record.getR47_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row47
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_no_of_acct() != null) {
						cell3.setCellValue(record.getR47_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row48
					row = sheet.getRow(47);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR48_approved_limit() != null) {
						cell1.setCellValue(record.getR48_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row48
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_balance_outstanding() != null) {
						cell2.setCellValue(record.getR48_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row48
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_no_of_acct() != null) {
						cell3.setCellValue(record.getR48_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR50_approved_limit() != null) {
						cell1.setCellValue(record.getR50_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row50
					// Column C
					cell2 = row.createCell(2);
					if (record.getR50_balance_outstanding() != null) {
						cell2.setCellValue(record.getR50_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row50
					// Column D
					cell3 = row.createCell(3);
					if (record.getR50_no_of_acct() != null) {
						cell3.setCellValue(record.getR50_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR51_approved_limit() != null) {
						cell1.setCellValue(record.getR51_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row51
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_balance_outstanding() != null) {
						cell2.setCellValue(record.getR51_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_no_of_acct() != null) {
						cell3.setCellValue(record.getR51_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR52_approved_limit() != null) {
						cell1.setCellValue(record.getR52_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row52
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_balance_outstanding() != null) {
						cell2.setCellValue(record.getR52_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_no_of_acct() != null) {
						cell3.setCellValue(record.getR52_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR54_approved_limit() != null) {
						cell1.setCellValue(record.getR54_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row54
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_balance_outstanding() != null) {
						cell2.setCellValue(record.getR54_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_no_of_acct() != null) {
						cell3.setCellValue(record.getR54_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR55_approved_limit() != null) {
						cell1.setCellValue(record.getR55_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row55
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_balance_outstanding() != null) {
						cell2.setCellValue(record.getR55_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_no_of_acct() != null) {
						cell3.setCellValue(record.getR55_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR56_approved_limit() != null) {
						cell1.setCellValue(record.getR56_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row56
					// Column C
					cell2 = row.createCell(2);
					if (record.getR56_balance_outstanding() != null) {
						cell2.setCellValue(record.getR56_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row56
					// Column D
					cell3 = row.createCell(3);
					if (record.getR56_no_of_acct() != null) {
						cell3.setCellValue(record.getR56_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row58
					row = sheet.getRow(57);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR58_approved_limit() != null) {
						cell1.setCellValue(record.getR58_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row58
					// Column C
					cell2 = row.createCell(2);
					if (record.getR58_balance_outstanding() != null) {
						cell2.setCellValue(record.getR58_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row58
					// Column D
					cell3 = row.createCell(3);
					if (record.getR58_no_of_acct() != null) {
						cell3.setCellValue(record.getR58_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR59_approved_limit() != null) {
						cell1.setCellValue(record.getR59_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row59
					// Column C
					cell2 = row.createCell(2);
					if (record.getR59_balance_outstanding() != null) {
						cell2.setCellValue(record.getR59_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row59
					// Column D
					cell3 = row.createCell(3);
					if (record.getR59_no_of_acct() != null) {
						cell3.setCellValue(record.getR59_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR60_approved_limit() != null) {
						cell1.setCellValue(record.getR60_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row60
					// Column C
					cell2 = row.createCell(2);
					if (record.getR60_balance_outstanding() != null) {
						cell2.setCellValue(record.getR60_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row60
					// Column D
					cell3 = row.createCell(3);
					if (record.getR60_no_of_acct() != null) {
						cell3.setCellValue(record.getR60_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR61_approved_limit() != null) {
						cell1.setCellValue(record.getR61_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row61
					// Column C
					cell2 = row.createCell(2);
					if (record.getR61_balance_outstanding() != null) {
						cell2.setCellValue(record.getR61_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row61
					// Column D
					cell3 = row.createCell(3);
					if (record.getR61_no_of_acct() != null) {
						cell3.setCellValue(record.getR61_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR62_approved_limit() != null) {
						cell1.setCellValue(record.getR62_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row62
					// Column C
					cell2 = row.createCell(2);
					if (record.getR62_balance_outstanding() != null) {
						cell2.setCellValue(record.getR62_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row62
					// Column D
					cell3 = row.createCell(3);
					if (record.getR62_no_of_acct() != null) {
						cell3.setCellValue(record.getR62_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row63
					row = sheet.getRow(62);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR63_approved_limit() != null) {
						cell1.setCellValue(record.getR63_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row63
					// Column C
					cell2 = row.createCell(2);
					if (record.getR63_balance_outstanding() != null) {
						cell2.setCellValue(record.getR63_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row63
					// Column D
					cell3 = row.createCell(3);
					if (record.getR63_no_of_acct() != null) {
						cell3.setCellValue(record.getR63_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA1 SUMMARY", null, "BRRS_M_LA1_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	public byte[] BRRS_M_LA1DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_LA1 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_LA1Details");

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

			// sanction style (right aligned with 3 decimals)
			CellStyle sanctionStyle = workbook.createCellStyle();
			sanctionStyle.setAlignment(HorizontalAlignment.RIGHT);
			sanctionStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			sanctionStyle.setBorderTop(border);
			sanctionStyle.setBorderBottom(border);
			sanctionStyle.setBorderLeft(border);
			sanctionStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NUMBER", "SCHM DESC", "ACCT BALANCE IN PULA", "APPROVED LIMIT",
					"REPORT LABEL", "REPORT ADDL CRITERIA 1", "REPORT ADDL CRITERIA 2", "REPORT ADDL CRITERIA 3",
					"REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				if (i == 3 || i == 4) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}
				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_LA1_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA1_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getSchm_desc());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					// sanction (right aligned, 3 decimal places)
					Cell balanceCell1 = row.createCell(4);
					if (item.getSanction_limit() != null) {
						balanceCell1.setCellValue(item.getSanction_limit().doubleValue());
					} else {
						balanceCell1.setCellValue(0);
					}
					balanceCell1.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReport_label());
					row.createCell(6).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(7).setCellValue(item.getReport_addl_criteria_2());
					row.createCell(8).setCellValue(item.getReport_addl_criteria_3());
					row.createCell(9)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");

					// Apply border style to all cells in the row
					for (int colIndex = 0; colIndex < headers.length; colIndex++) {
						Cell cell = row.getCell(colIndex);
						if (cell != null) {
							if (colIndex == 3) { // ACCT BALANCE
								cell.setCellStyle(balanceStyle);
							} else if (colIndex == 4) { // APPROVED LIMIT
								cell.setCellStyle(sanctionStyle);
							} else {
								cell.setCellStyle(dataStyle);
							}
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_LA1 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_LA1 Excel", e);
			return new byte[0];
		}
	}

	// Archival View
	public List<Object[]> getM_LA1Archival() {
	    List<Object[]> archivalList = new ArrayList<>();

	    try {

	        List<M_LA1_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

	        if (repoData != null && !repoData.isEmpty()) {
	            for (M_LA1_Archival_Summary_Entity entity : repoData) {
	                Object[] row = new Object[] {
	                        entity.getREPORT_DATE(),
	                        entity.getREPORT_VERSION(),
	                        entity.getREPORT_RESUBDATE()
	                };
	                archivalList.add(row);
	            }

	            System.out.println("Fetched " + archivalList.size() + " archival records");
	            M_LA1_Archival_Summary_Entity first = repoData.get(0);
	            System.out.println("Latest archival version: " + first.getREPORT_VERSION());

	        } else {
	            System.out.println("No archival data found.");
	        }

	    } catch (Exception e) {
	        System.err.println("Error fetching M_LA1 Archival data: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return archivalList;
	}

	// Resubmission View
	public List<Object[]> getM_LA1Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM BRRS_M_LA1_ARCHIVALTABLE_SUMMARY"
					+ " WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
			resubList = jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] {
					rs.getDate("REPORT_DATE"),
					rs.getBigDecimal("REPORT_VERSION"),
					rs.getDate("REPORT_RESUBDATE")
			});
			System.out.println("Fetched " + resubList.size() + " M_LA1 Resub records");
		} catch (Exception e) {
			System.err.println("Error fetching M_LA1 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public byte[] getExcelM_LA1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_LA1_Archival_Summary_Entity> dataList =  getdatabydateListarchival(dateformat.parse(todate),version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA1 report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---
			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LA1_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getREPORT_DATE() != null) {
						cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row12
					// Column B
					row = sheet.getRow(11);
					cell1 = row.getCell(1);
					if (record.getR12_approved_limit() != null) {
						cell1.setCellValue(record.getR12_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR12_balance_outstanding() != null) {
						cell2.setCellValue(record.getR12_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_no_of_acct() != null) {
						cell3.setCellValue(record.getR12_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR13_approved_limit() != null) {
						cell1.setCellValue(record.getR13_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row13
					// Column C
					cell2 = row.createCell(2);
					if (record.getR13_balance_outstanding() != null) {
						cell2.setCellValue(record.getR13_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_no_of_acct() != null) {
						cell3.setCellValue(record.getR13_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR14_approved_limit() != null) {
						cell1.setCellValue(record.getR14_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row14
					// Column C
					cell2 = row.getCell(2);
					if (record.getR14_balance_outstanding() != null) {
						cell2.setCellValue(record.getR14_balance_outstanding().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_no_of_acct() != null) {
						cell3.setCellValue(record.getR14_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR16_approved_limit() != null) {
						cell1.setCellValue(record.getR16_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row16
					// Column C
					cell2 = row.createCell(2);
					if (record.getR16_balance_outstanding() != null) {
						cell2.setCellValue(record.getR16_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_no_of_acct() != null) {
						cell3.setCellValue(record.getR16_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR17_approved_limit() != null) {
						cell1.setCellValue(record.getR17_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row17
					// Column C
					cell2 = row.createCell(2);
					if (record.getR17_balance_outstanding() != null) {
						cell2.setCellValue(record.getR17_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					// Column D
					cell3 = row.createCell(3);
					if (record.getR17_no_of_acct() != null) {
						cell3.setCellValue(record.getR17_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR18_approved_limit() != null) {
						cell1.setCellValue(record.getR18_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row18
					// Column C
					cell2 = row.createCell(2);
					if (record.getR18_balance_outstanding() != null) {
						cell2.setCellValue(record.getR18_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					// Column D
					cell3 = row.createCell(3);
					if (record.getR18_no_of_acct() != null) {
						cell3.setCellValue(record.getR18_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR19_approved_limit() != null) {
						cell1.setCellValue(record.getR19_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row19
					// Column C
					cell2 = row.createCell(2);
					if (record.getR19_balance_outstanding() != null) {
						cell2.setCellValue(record.getR19_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					// Column D
					cell3 = row.createCell(3);
					if (record.getR19_no_of_acct() != null) {
						cell3.setCellValue(record.getR19_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR20_approved_limit() != null) {
						cell1.setCellValue(record.getR20_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row20
					// Column C
					cell2 = row.createCell(2);
					if (record.getR20_balance_outstanding() != null) {
						cell2.setCellValue(record.getR20_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_no_of_acct() != null) {
						cell3.setCellValue(record.getR20_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR21_approved_limit() != null) {
						cell1.setCellValue(record.getR21_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row21
					// Column C
					cell2 = row.createCell(2);
					if (record.getR21_balance_outstanding() != null) {
						cell2.setCellValue(record.getR21_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_no_of_acct() != null) {
						cell3.setCellValue(record.getR21_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR22_approved_limit() != null) {
						cell1.setCellValue(record.getR22_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row22
					// Column C
					cell2 = row.createCell(2);
					if (record.getR22_balance_outstanding() != null) {
						cell2.setCellValue(record.getR22_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_no_of_acct() != null) {
						cell3.setCellValue(record.getR22_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR23_approved_limit() != null) {
						cell1.setCellValue(record.getR23_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row23
					// Column C
					cell2 = row.createCell(2);
					if (record.getR23_balance_outstanding() != null) {
						cell2.setCellValue(record.getR23_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_no_of_acct() != null) {
						cell3.setCellValue(record.getR23_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR24_approved_limit() != null) {
						cell1.setCellValue(record.getR24_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row24
					// Column C
					cell2 = row.createCell(2);
					if (record.getR24_balance_outstanding() != null) {
						cell2.setCellValue(record.getR24_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_no_of_acct() != null) {
						cell3.setCellValue(record.getR24_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR25_approved_limit() != null) {
						cell1.setCellValue(record.getR25_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row25
					// Column C
					cell2 = row.createCell(2);
					if (record.getR25_balance_outstanding() != null) {
						cell2.setCellValue(record.getR25_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_no_of_acct() != null) {
						cell3.setCellValue(record.getR25_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR26_approved_limit() != null) {
						cell1.setCellValue(record.getR26_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row26
					// Column C
					cell2 = row.createCell(2);
					if (record.getR26_balance_outstanding() != null) {
						cell2.setCellValue(record.getR26_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_no_of_acct() != null) {
						cell3.setCellValue(record.getR26_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR27_approved_limit() != null) {
						cell1.setCellValue(record.getR27_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row27
					// Column C
					cell2 = row.createCell(2);
					if (record.getR27_balance_outstanding() != null) {
						cell2.setCellValue(record.getR27_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_no_of_acct() != null) {
						cell3.setCellValue(record.getR27_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR28_approved_limit() != null) {
						cell1.setCellValue(record.getR28_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row28
					// Column C
					cell2 = row.createCell(2);
					if (record.getR28_balance_outstanding() != null) {
						cell2.setCellValue(record.getR28_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_no_of_acct() != null) {
						cell3.setCellValue(record.getR28_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR30_approved_limit() != null) {
						cell1.setCellValue(record.getR30_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row30
					// Column C
					cell2 = row.createCell(2);
					if (record.getR30_balance_outstanding() != null) {
						cell2.setCellValue(record.getR30_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					// Column D
					cell3 = row.createCell(3);
					if (record.getR30_no_of_acct() != null) {
						cell3.setCellValue(record.getR30_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR31_approved_limit() != null) {
						cell1.setCellValue(record.getR31_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row31
					// Column C
					cell2 = row.createCell(2);
					if (record.getR31_balance_outstanding() != null) {
						cell2.setCellValue(record.getR31_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_no_of_acct() != null) {
						cell3.setCellValue(record.getR31_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR32_approved_limit() != null) {
						cell1.setCellValue(record.getR32_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row32
					// Column C
					cell2 = row.createCell(2);
					if (record.getR32_balance_outstanding() != null) {
						cell2.setCellValue(record.getR32_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row32
					// Column D
					cell3 = row.createCell(3);
					if (record.getR32_no_of_acct() != null) {
						cell3.setCellValue(record.getR32_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR33_approved_limit() != null) {
						cell1.setCellValue(record.getR33_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row33
					// Column C
					cell2 = row.createCell(2);
					if (record.getR33_balance_outstanding() != null) {
						cell2.setCellValue(record.getR33_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR33_no_of_acct() != null) {
						cell3.setCellValue(record.getR33_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR34_approved_limit() != null) {
						cell1.setCellValue(record.getR34_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row34
					// Column C
					cell2 = row.createCell(2);
					if (record.getR34_balance_outstanding() != null) {
						cell2.setCellValue(record.getR34_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row34
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_no_of_acct() != null) {
						cell3.setCellValue(record.getR34_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR35_approved_limit() != null) {
						cell1.setCellValue(record.getR35_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row35
					// Column C
					cell2 = row.createCell(2);
					if (record.getR35_balance_outstanding() != null) {
						cell2.setCellValue(record.getR35_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_no_of_acct() != null) {
						cell3.setCellValue(record.getR35_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR36_approved_limit() != null) {
						cell1.setCellValue(record.getR36_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row36
					// Column C
					cell2 = row.createCell(2);
					if (record.getR36_balance_outstanding() != null) {
						cell2.setCellValue(record.getR36_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row36
					// Column D
					cell3 = row.createCell(3);
					if (record.getR36_no_of_acct() != null) {
						cell3.setCellValue(record.getR36_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR37_approved_limit() != null) {
						cell1.setCellValue(record.getR37_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row37
					// Column C
					cell2 = row.createCell(2);
					if (record.getR37_balance_outstanding() != null) {
						cell2.setCellValue(record.getR37_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_no_of_acct() != null) {
						cell3.setCellValue(record.getR37_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR39_approved_limit() != null) {
						cell1.setCellValue(record.getR39_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row39
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_balance_outstanding() != null) {
						cell2.setCellValue(record.getR39_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_no_of_acct() != null) {
						cell3.setCellValue(record.getR39_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR40_approved_limit() != null) {
						cell1.setCellValue(record.getR40_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_balance_outstanding() != null) {
						cell2.setCellValue(record.getR40_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_no_of_acct() != null) {
						cell3.setCellValue(record.getR40_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR42_approved_limit() != null) {
						cell1.setCellValue(record.getR42_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row42
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_balance_outstanding() != null) {
						cell2.setCellValue(record.getR42_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row42
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_no_of_acct() != null) {
						cell3.setCellValue(record.getR42_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row43
					row = sheet.getRow(42);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR43_approved_limit() != null) {
						cell1.setCellValue(record.getR43_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row43
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_balance_outstanding() != null) {
						cell2.setCellValue(record.getR43_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row43
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_no_of_acct() != null) {
						cell3.setCellValue(record.getR43_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR45_approved_limit() != null) {
						cell1.setCellValue(record.getR45_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR45_balance_outstanding() != null) {
						cell2.setCellValue(record.getR45_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row45
					// Column D
					cell3 = row.createCell(3);
					if (record.getR45_no_of_acct() != null) {
						cell3.setCellValue(record.getR45_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR46_approved_limit() != null) {
						cell1.setCellValue(record.getR46_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row46
					// Column C
					cell2 = row.createCell(2);
					if (record.getR46_balance_outstanding() != null) {
						cell2.setCellValue(record.getR46_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row46
					// Column D
					cell3 = row.createCell(3);
					if (record.getR46_no_of_acct() != null) {
						cell3.setCellValue(record.getR46_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR47_approved_limit() != null) {
						cell1.setCellValue(record.getR47_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row47
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_balance_outstanding() != null) {
						cell2.setCellValue(record.getR47_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row47
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_no_of_acct() != null) {
						cell3.setCellValue(record.getR47_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row48
					row = sheet.getRow(47);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR48_approved_limit() != null) {
						cell1.setCellValue(record.getR48_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row48
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_balance_outstanding() != null) {
						cell2.setCellValue(record.getR48_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row48
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_no_of_acct() != null) {
						cell3.setCellValue(record.getR48_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR50_approved_limit() != null) {
						cell1.setCellValue(record.getR50_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row50
					// Column C
					cell2 = row.createCell(2);
					if (record.getR50_balance_outstanding() != null) {
						cell2.setCellValue(record.getR50_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row50
					// Column D
					cell3 = row.createCell(3);
					if (record.getR50_no_of_acct() != null) {
						cell3.setCellValue(record.getR50_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR51_approved_limit() != null) {
						cell1.setCellValue(record.getR51_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row51
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_balance_outstanding() != null) {
						cell2.setCellValue(record.getR51_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_no_of_acct() != null) {
						cell3.setCellValue(record.getR51_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR52_approved_limit() != null) {
						cell1.setCellValue(record.getR52_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row52
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_balance_outstanding() != null) {
						cell2.setCellValue(record.getR52_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_no_of_acct() != null) {
						cell3.setCellValue(record.getR52_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR54_approved_limit() != null) {
						cell1.setCellValue(record.getR54_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row54
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_balance_outstanding() != null) {
						cell2.setCellValue(record.getR54_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_no_of_acct() != null) {
						cell3.setCellValue(record.getR54_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR55_approved_limit() != null) {
						cell1.setCellValue(record.getR55_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row55
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_balance_outstanding() != null) {
						cell2.setCellValue(record.getR55_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_no_of_acct() != null) {
						cell3.setCellValue(record.getR55_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR56_approved_limit() != null) {
						cell1.setCellValue(record.getR56_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row56
					// Column C
					cell2 = row.createCell(2);
					if (record.getR56_balance_outstanding() != null) {
						cell2.setCellValue(record.getR56_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row56
					// Column D
					cell3 = row.createCell(3);
					if (record.getR56_no_of_acct() != null) {
						cell3.setCellValue(record.getR56_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row58
					row = sheet.getRow(57);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR58_approved_limit() != null) {
						cell1.setCellValue(record.getR58_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row58
					// Column C
					cell2 = row.createCell(2);
					if (record.getR58_balance_outstanding() != null) {
						cell2.setCellValue(record.getR58_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row58
					// Column D
					cell3 = row.createCell(3);
					if (record.getR58_no_of_acct() != null) {
						cell3.setCellValue(record.getR58_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR59_approved_limit() != null) {
						cell1.setCellValue(record.getR59_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row59
					// Column C
					cell2 = row.createCell(2);
					if (record.getR59_balance_outstanding() != null) {
						cell2.setCellValue(record.getR59_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row59
					// Column D
					cell3 = row.createCell(3);
					if (record.getR59_no_of_acct() != null) {
						cell3.setCellValue(record.getR59_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR60_approved_limit() != null) {
						cell1.setCellValue(record.getR60_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row60
					// Column C
					cell2 = row.createCell(2);
					if (record.getR60_balance_outstanding() != null) {
						cell2.setCellValue(record.getR60_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row60
					// Column D
					cell3 = row.createCell(3);
					if (record.getR60_no_of_acct() != null) {
						cell3.setCellValue(record.getR60_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR61_approved_limit() != null) {
						cell1.setCellValue(record.getR61_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row61
					// Column C
					cell2 = row.createCell(2);
					if (record.getR61_balance_outstanding() != null) {
						cell2.setCellValue(record.getR61_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row61
					// Column D
					cell3 = row.createCell(3);
					if (record.getR61_no_of_acct() != null) {
						cell3.setCellValue(record.getR61_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR62_approved_limit() != null) {
						cell1.setCellValue(record.getR62_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row62
					// Column C
					cell2 = row.createCell(2);
					if (record.getR62_balance_outstanding() != null) {
						cell2.setCellValue(record.getR62_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row62
					// Column D
					cell3 = row.createCell(3);
					if (record.getR62_no_of_acct() != null) {
						cell3.setCellValue(record.getR62_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row63
					row = sheet.getRow(62);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR63_approved_limit() != null) {
						cell1.setCellValue(record.getR63_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row63
					// Column C
					cell2 = row.createCell(2);
					if (record.getR63_balance_outstanding() != null) {
						cell2.setCellValue(record.getR63_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row63
					// Column D
					cell3 = row.createCell(3);
					if (record.getR63_no_of_acct() != null) {
						cell3.setCellValue(record.getR63_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA1 ARCHIVAL SUMMARY", null,
						"BRRS_M_LA1_ARCHIVALTABLE_SUMMARY");
			}

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_LA1 ARCHIVAL Details...");
			System.out.println("Came to Detail download service");

			// Only proceed if ARCHIVAL and version provided
			if (!"ARCHIVAL".equalsIgnoreCase(type) || version == null || version.isEmpty()) {
				logger.warn("Invalid type/version for archival download.");
				return new byte[0];
			}

			// Create workbook and sheet
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_LA1_Archival_Detail");

			// Border style
			BorderStyle border = BorderStyle.THIN;

			// Header style
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

			// Right-aligned header (for numeric columns)
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			// Data style (text)
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
			String[] headers = { "CUST ID", "ACCT NUMBER", "SCHM DESC", "ACCT BALANCE IN PULA", "APPROVED LIMIT",
					"REPORT LABEL", "REPORT ADDL CRITERIA 1", "REPORT ADDL CRITERIA 2", "REPORT ADDL CRITERIA 3",
					"REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Parse date
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);

			// Fetch data
			List<M_LA1_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA1_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					// Text columns
					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getSchm_desc());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell sanctionCell = row.createCell(4);
					if (item.getSanction_limit() != null) {
						sanctionCell.setCellValue(item.getSanction_limit().doubleValue());
					} else {
						sanctionCell.setCellValue(0);
					}
					sanctionCell.setCellStyle(balanceStyle);

					// Remaining text columns
					row.createCell(5).setCellValue(item.getReport_label());
					row.createCell(6).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(7).setCellValue(item.getReport_addl_criteria_2());
					row.createCell(8).setCellValue(item.getReport_addl_criteria_3());
					row.createCell(9)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");

					// Apply text style to non-numeric cells
					for (int j = 0; j < headers.length; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			}

			// Write to byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_LA1 ARCHIVAL Excel", e);
			return new byte[0];
		}
	}

	public List<ReportLineItemDTO> getReportData(String filename) throws Exception {
		List<ReportLineItemDTO> reportData = new ArrayList<>();

		File file = new File(filename);
		if (!file.exists()) {
			throw new Exception("File not found: " + filename);
		}

		FileInputStream fis = new FileInputStream(file);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0);

		final int START_ROW_INDEX = 10;
		final int END_ROW_INDEX = 63;

		Iterator<Row> rowIterator = sheet.iterator();
		int srlNo = 1;

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			int currentRowIndex = row.getRowNum();

			if (currentRowIndex < START_ROW_INDEX) {
				continue;
			}

			if (currentRowIndex > END_ROW_INDEX) {
				break;
			}

			Cell fieldDescCell = row.getCell(0);

			if (fieldDescCell == null || fieldDescCell.getCellType() == Cell.CELL_TYPE_BLANK) {
				continue;
			}

			String fieldDesc = "";
			try {
				fieldDesc = fieldDescCell.getStringCellValue();
			} catch (IllegalStateException e) {

				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				CellValue cellValue = evaluator.evaluate(fieldDescCell);
				if (cellValue != null) {
					if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
						fieldDesc = cellValue.getStringValue();
					} else if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						fieldDesc = String.valueOf(cellValue.getNumberValue());
					} else if (cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						fieldDesc = String.valueOf(cellValue.getBooleanValue());
					}

				}
				if (fieldDesc.isEmpty() && fieldDescCell.getCellType() == Cell.CELL_TYPE_FORMULA) {

					fieldDesc = fieldDescCell.getCellFormula();
				}
			} catch (Exception e) {
				System.err.println("Error reading cell A" + (currentRowIndex + 1) + ": " + e.getMessage());
				continue;
			}

			if (fieldDesc == null || fieldDesc.trim().isEmpty()) {
				continue;
			}

			ReportLineItemDTO dto = new ReportLineItemDTO();
			dto.setSrlNo(srlNo++);
			dto.setFieldDescription(fieldDesc.trim());

			dto.setReportLabel("R" + (currentRowIndex + 1));

			boolean hasFormula = false;
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					hasFormula = true;
					break;
				}
			}
			dto.setHeader(hasFormula ? "Y" : " ");

			dto.setRemarks("");

			reportData.add(dto);
		}

		workbook.close();
		fis.close();

		System.out.println("✅ M_LA1 Report data processed (Excel Row " + (START_ROW_INDEX + 1) + " to "
				+ (END_ROW_INDEX + 1) + "). Total items: " + reportData.size());
		return reportData;
	}

//	public boolean updateProvision(M_LA1_Detail_Entity la1Data) {
//		try {
//			System.out.println("Came to LA1 Service");
//
//			// ✅ Must match your entity field name exactly
//			M_LA1_Detail_Entity existing = M_LA1_Detail_Repo.findByAcctnumber(la1Data.getAcct_number());
//
//			if (existing != null) {
//
//				existing.setAcct_name(la1Data.getAcct_name());
//
//				// existing.setAcct_name(la1Data.getAcct_name());
//
//				existing.setSanction_limit(la1Data.getSanction_limit());
//				existing.setAcct_balance_in_pula(la1Data.getAcct_balance_in_pula());
//
//				M_LA1_Detail_Repo.save(existing);
//
//				System.out.println("Updated successfully for ACCT_NO: " + la1Data.getAcct_number());
//				return true;
//			} else {
//				System.out.println("Record not found for Account No: " + la1Data.getAcct_number());
//				return false;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}



	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		return getViewOrEditPage(SNO, formMode, type, null);
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type, String version) {

	    ModelAndView mv = new ModelAndView("BRRS/M_LA1");

	    System.out.println("SNO : " + SNO);
	    System.out.println("Type : " + type);
	    System.out.println("Version : " + version);

	    if (SNO != null) {

	        if ("RESUB".equals(type)) {

	            System.out.println("Inside RESUB FETCH");

	            M_LA1_Detail_Entity la1Entity = findBySnoArch(SNO, version);

	            if (la1Entity == null) {
	                System.out.println("RESUB Data is NULL");
	            } else {
	                System.out.println("RESUB Data Found : " + la1Entity.getSno());

	                if (la1Entity.getReport_date() != null) {
	                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
	                            .format(la1Entity.getReport_date());
	                    mv.addObject("asondate", formattedDate);
	                }
	            }

	            mv.addObject("Data", la1Entity);

	        } else {

	            System.out.println("Inside CURRENT FETCH");

	            M_LA1_Detail_Entity la1Entity = findBySno(SNO);

	            if (la1Entity == null) {
	                System.out.println("CURRENT Data is NULL");
	            } else {
	                System.out.println("CURRENT Data Found : " + la1Entity.getSno());

	                if (la1Entity.getReport_date() != null) {
	                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
	                            .format(la1Entity.getReport_date());
	                    mv.addObject("asondate", formattedDate);
	                }
	            }

	            mv.addObject("Data", la1Entity);
	        }
	    }

	    mv.addObject("type", type);
	    mv.addObject("version", version);
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
	        String sanctionLimitStr = request.getParameter("sanction_limit");
	        String reportDateStr = request.getParameter("report_date");
	        if (reportDateStr == null) {
	            reportDateStr = request.getParameter("report_Date");
	        }
	        if (reportDateStr == null) {
	            reportDateStr = request.getParameter("reportDate");
	        }

	        String type = request.getParameter("type");
	        String version = request.getParameter("version");
	        String entry = (request.getParameter("entry") != null)
	                ? request.getParameter("entry")
	                : "YES";

	        System.out.println("SNO : " + Sno);
	        System.out.println("Type : " + type);
	        System.out.println("Version : " + version);

	        // Load Existing Record
	        M_LA1_Detail_Entity existing = null;

	        if ("RESUB".equals(type)) {
	            existing = findBySnoArch(Sno, version);
	        } else {
	            existing = findBySno(Sno);
	        }

	        if (existing == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Record not found for update.");
	        }

	        // Create old copy for audit
	        M_LA1_Detail_Entity oldcopy = new M_LA1_Detail_Entity();
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

	        // Update Balance
	        if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {

	            BigDecimal newBalance = new BigDecimal(acctBalanceInpula);

	            if (existing.getAcct_balance_in_pula() == null
	                    || existing.getAcct_balance_in_pula().compareTo(newBalance) != 0) {

	                existing.setAcct_balance_in_pula(newBalance);
	                isChanged = true;
	            }
	        }

	        // Update Sanction Limit
	        if (sanctionLimitStr != null && !sanctionLimitStr.isEmpty()) {

	            BigDecimal newSanctionLimit = new BigDecimal(sanctionLimitStr);

	            if (existing.getSanction_limit() == null
	                    || existing.getSanction_limit().compareTo(newSanctionLimit) != 0) {

	                existing.setSanction_limit(newSanctionLimit);
	                isChanged = true;
	            }
	        }

	        if (isChanged) {

	            String sql;

	            if ("RESUB".equals(type)) {

	                sql = "UPDATE BRRS_M_LA1_ARCHIVALTABLE_DETAIL "
	                        + "SET ACCT_NAME = ?, "
	                        + "ACCT_BALANCE_IN_PULA = ?, "
	                        + "SANCTION_LIMIT = ? "
	                        + "WHERE SNO = ? AND DATA_ENTRY_VERSION = ?";

	                jdbcTemplate.update(sql,
	                        existing.getAcct_name(),
	                        existing.getAcct_balance_in_pula(),
	                        existing.getSanction_limit(),
	                        Sno,
	                        version);

	            } else {

	                sql = "UPDATE BRRS_M_LA1_DETAILTABLE "
	                        + "SET ACCT_NAME = ?, "
	                        + "ACCT_BALANCE_IN_PULA = ?, "
	                        + "SANCTION_LIMIT = ? "
	                        + "WHERE SNO = ?";

	                jdbcTemplate.update(sql,
	                        existing.getAcct_name(),
	                        existing.getAcct_balance_in_pula(),
	                        existing.getSanction_limit(),
	                        Sno);
	            }

	            // Audit
	            if ("RESUB".equals(type)) {

	                auditService.compareEntitiesmanual(
	                        oldcopy,
	                        existing,
	                        Sno,
	                        "M_LA1 Archival Screen",
	                        "BRRS_M_LA1_ARCHIVALTABLE_DETAIL");

	            } else {

	                auditService.compareEntitiesmanual(
	                        oldcopy,
	                        existing,
	                        Sno,
	                        "M_LA1 Screen",
	                        "BRRS_M_LA1_DETAILTABLE");
	            }

	            System.out.println("Record updated successfully.");

	            // Regenerate Report
	            Run_M_LA1_Procudure(reportDateStr, type, entry);

	            if ("RESUB".equals(type) && "NO".equals(entry)) {
	                return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
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
	
	@Transactional
	public ResponseEntity<?> callregenprocedure(HttpServletRequest request) {

	    try {

	        Run_M_LA1_Procudure(
	                request.getParameter("reportDate"),
	                request.getParameter("type"),
	                request.getParameter("entry"));

	        return ResponseEntity.ok("Resubmitted successfully!");

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}
	
	private void Run_M_LA1_Procudure(String reportDateStr, String type, String entry) {

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
						String bdsql = "DELETE FROM BRRS_M_LA1_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(bdsql, formattedDate);
						System.out.println("Successfully deleted before executing procedure " + rowsDeleted + " rows.");

						String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_LA1_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
						Integer maxVersionVal = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, formattedDate);
						int maxVersion = (maxVersionVal != null) ? maxVersionVal : 1;

						String sqltransfer = "INSERT INTO BRRS_M_LA1_DETAILTABLE ("
						        + "SNO, CUST_ID, ACCT_NUMBER, ACCT_NAME, "
						        + "ACCT_BALANCE_IN_PULA, SANCTION_LIMIT, SEGMENT, SCHM_DESC, "
						        + "REPORT_LABEL, REPORT_ADDL_CRITERIA_1, REPORT_ADDL_CRITERIA_2, REPORT_ADDL_CRITERIA_3, "
						        + "MODIFICATION_REMARKS, REPORT_REMARKS, REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION) "
						        + "SELECT "
						        + "SNO, CUST_ID, ACCT_NUMBER, ACCT_NAME, "
						        + "ACCT_BALANCE_IN_PULA, SANCTION_LIMIT, SEGMENT, SCHM_DESC, "
						        + "REPORT_LABEL, REPORT_ADDL_CRITERIA_1, REPORT_ADDL_CRITERIA_2, REPORT_ADDL_CRITERIA_3, "
						        + "MODIFICATION_REMARKS, REPORT_REMARKS, REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION "
						        + "FROM BRRS_M_LA1_ARCHIVALTABLE_DETAIL "
						        + "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
						
						int rowsInserted = jdbcTemplate.update(sqltransfer, formattedDate, String.valueOf(maxVersion));
						System.out.println("Successfully transferred " + rowsInserted + " rows from version " + maxVersion);
					}

					if (shouldExecuteProcedure) {
						jdbcTemplate.update("BEGIN BRRS_M_LA1_SUMMARY_PROCEDURE(?); END;", formattedDate);
						System.out.println("Procedure executed");
					}

					if (isResubNoEntry) {
						String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_LA1_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
						Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, formattedDate);
						int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

						// Copy modified detail records back to ARCHIVALTABLE_DETAIL with new version
						String ins_dtl_sql = "INSERT INTO BRRS_M_LA1_ARCHIVALTABLE_DETAIL ("
								+ "SNO, CUST_ID, ACCT_NUMBER, ACCT_NAME, DATA_TYPE, "
								+ "REPORT_LABEL, REPORT_ADDL_CRITERIA_1, REPORT_REMARKS, MODIFICATION_REMARKS, "
								+ "ACCT_BALANCE_IN_PULA, SANCTION_LIMIT, SEGMENT, SCHM_DESC, "
								+ "REPORT_ADDL_CRITERIA_2, REPORT_ADDL_CRITERIA_3, "
								+ "REPORT_NAME, REPORT_DATE, CREATE_USER, CREATE_TIME, MODIFY_USER, MODIFY_TIME, "
								+ "VERIFY_USER, VERIFY_TIME, ENTITY_FLG, MODIFY_FLG, DEL_FLG, DATA_ENTRY_VERSION) "
								+ "SELECT "
								+ "SNO, CUST_ID, ACCT_NUMBER, ACCT_NAME, DATA_TYPE, "
								+ "REPORT_LABEL, REPORT_ADDL_CRITERIA_1, REPORT_REMARKS, MODIFICATION_REMARKS, "
								+ "ACCT_BALANCE_IN_PULA, SANCTION_LIMIT, SEGMENT, SCHM_DESC, "
								+ "REPORT_ADDL_CRITERIA_2, REPORT_ADDL_CRITERIA_3, "
								+ "REPORT_NAME, REPORT_DATE, CREATE_USER, CREATE_TIME, MODIFY_USER, MODIFY_TIME, "
								+ "VERIFY_USER, VERIFY_TIME, ENTITY_FLG, MODIFY_FLG, DEL_FLG, ? "
								+ "FROM BRRS_M_LA1_DETAILTABLE "
								+ "WHERE REPORT_DATE = ?";
						int rowsInsertedDtl = jdbcTemplate.update(ins_dtl_sql, String.valueOf(highestValue), formattedDate);
						System.out.println("Successfully inserted " + rowsInsertedDtl + " detail rows into ARCHIVAL for version " + highestValue);

						String adsql = "DELETE FROM BRRS_M_LA1_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(adsql, formattedDate);
						System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

						StringBuilder columnsPart = new StringBuilder();
						String[] tokens = {
								"product",
								"approved_limit",
								"balance_outstanding",
								"no_of_acct"
						};
						// Dynamically generate R6 to R62 columns
						for (int i = 11; i <= 64; i++) {
							for (String token : tokens) {
								columnsPart.append("R").append(i).append("_").append(token).append(", ");
							}
						}

						// Build the final query cleanly
						String finalsql = "INSERT INTO BRRS_M_LA1_ARCHIVALTABLE_SUMMARY (" + columnsPart.toString()
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
								+ "SELECT " + columnsPart.toString()
								+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
								+ "FROM BRRS_M_LA1_SUMMARYTABLE WHERE REPORT_DATE = ?";

						int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, formattedDate);
						System.out.println("Successfully transferred summary " + rowsInsertedSum + " rows.");

						String adsumsql = "DELETE FROM BRRS_M_LA1_SUMMARYTABLE WHERE REPORT_DATE = ?";
						int rowsDeletedSum = jdbcTemplate.update(adsumsql, formattedDate);
						System.out.println("Deleted from summary " + rowsDeletedSum + " rows after transfering.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
