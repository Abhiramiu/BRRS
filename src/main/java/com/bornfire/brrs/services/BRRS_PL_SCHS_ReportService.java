package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional
public class BRRS_PL_SCHS_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_PL_SCHS_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	// ENTITY MANAGER (Acts like Repository)
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PlatformTransactionManager transactionManager;

	// Fetch data by report date
	public List<PL_SCHS_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new PL_SCHSRowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getPL_SCHSArchival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_PL_SCHS_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<PL_SCHS_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new PL_SCHSArchivalRowMapper());
	}
//GET ALL WITH VERSION

	public List<PL_SCHS_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_PL_SCHS_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new PL_SCHSArchivalRowMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_PL_SCHS_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<PL_SCHS_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new PL_SCHSDetailRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<PL_SCHS_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new PL_SCHSDetailRowMapper());
	}

// 3. PAGINATION

	public List<PL_SCHS_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new PL_SCHSDetailRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_PL_SCHS_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<PL_SCHS_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new PL_SCHSDetailRowMapper());
	}
// 6. BY ACCOUNT NUMBER

	public PL_SCHS_Detail_Entity findByAcctnumber(String acctNumber) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new PL_SCHSDetailRowMapper());
	}

// 1. GET BY DATE + VERSION

	public List<PL_SCHS_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
			String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
				new PL_SCHSArchivalDetailRowMapper());
	}

	public List<PL_SCHS_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND DATA_ENTRY_VERSION = ? ";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new PL_SCHSArchivalDetailRowMapper());
	}

	public List<PL_SCHS_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ?  ";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new PL_SCHSArchivalDetailRowMapper());
	}

	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_PL_SCHS_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}

	public PL_SCHS_Detail_Entity findBysnoArch(String sno) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new PL_SCHSDetailRowMapper());
	}

	public PL_SCHS_Detail_Entity findBySno(String sno) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_DETAILTABLE WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new PL_SCHSDetailRowMapper());
	}

	public PL_SCHS_Detail_Entity findBySnoArch(String sno) {

		String sql = "SELECT * FROM BRRS_PL_SCHS_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new PL_SCHSDetailRowMapper());
	}
	// ROW MAPPER

	class PL_SCHSRowMapper implements RowMapper<PL_SCHS_Summary_Entity> {

		@Override
		public PL_SCHS_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			PL_SCHS_Summary_Entity obj = new PL_SCHS_Summary_Entity();

// ================= R9 =================
			obj.setR9_intrest_div(rs.getString("R9_INTREST_DIV"));
			obj.setR9_fig_bal_sheet(rs.getBigDecimal("R9_FIG_BAL_SHEET"));
			obj.setR9_fig_bal_sheet_bwp(rs.getBigDecimal("R9_FIG_BAL_SHEET_BWP"));
			obj.setR9_amt_statement_adj(rs.getBigDecimal("R9_AMT_STATEMENT_ADJ"));
			obj.setR9_amt_statement_adj_bwp(rs.getBigDecimal("R9_AMT_STATEMENT_ADJ_BWP"));
			obj.setR9_net_amt(rs.getBigDecimal("R9_NET_AMT"));
			obj.setR9_net_amt_bwp(rs.getBigDecimal("R9_NET_AMT_BWP"));
			obj.setR9_bal_sub(rs.getBigDecimal("R9_BAL_SUB"));
			obj.setR9_bal_sub_bwp(rs.getBigDecimal("R9_BAL_SUB_BWP"));
			obj.setR9_bal_sub_diaries(rs.getBigDecimal("R9_BAL_SUB_DIARIES"));
			obj.setR9_bal_sub_diaries_bwp(rs.getBigDecimal("R9_BAL_SUB_DIARIES_BWP"));

			// ================= R10 =================
			obj.setR10_intrest_div(rs.getString("R10_INTREST_DIV"));
			obj.setR10_fig_bal_sheet(rs.getBigDecimal("R10_FIG_BAL_SHEET"));
			obj.setR10_fig_bal_sheet_bwp(rs.getBigDecimal("R10_FIG_BAL_SHEET_BWP"));
			obj.setR10_amt_statement_adj(rs.getBigDecimal("R10_AMT_STATEMENT_ADJ"));
			obj.setR10_amt_statement_adj_bwp(rs.getBigDecimal("R10_AMT_STATEMENT_ADJ_BWP"));
			obj.setR10_net_amt(rs.getBigDecimal("R10_NET_AMT"));
			obj.setR10_net_amt_bwp(rs.getBigDecimal("R10_NET_AMT_BWP"));
			obj.setR10_bal_sub(rs.getBigDecimal("R10_BAL_SUB"));
			obj.setR10_bal_sub_bwp(rs.getBigDecimal("R10_BAL_SUB_BWP"));
			obj.setR10_bal_sub_diaries(rs.getBigDecimal("R10_BAL_SUB_DIARIES"));
			obj.setR10_bal_sub_diaries_bwp(rs.getBigDecimal("R10_BAL_SUB_DIARIES_BWP"));

			// ================= R11 =================
			obj.setR11_intrest_div(rs.getString("R11_INTREST_DIV"));
			obj.setR11_fig_bal_sheet(rs.getBigDecimal("R11_FIG_BAL_SHEET"));
			obj.setR11_fig_bal_sheet_bwp(rs.getBigDecimal("R11_FIG_BAL_SHEET_BWP"));
			obj.setR11_amt_statement_adj(rs.getBigDecimal("R11_AMT_STATEMENT_ADJ"));
			obj.setR11_amt_statement_adj_bwp(rs.getBigDecimal("R11_AMT_STATEMENT_ADJ_BWP"));
			obj.setR11_net_amt(rs.getBigDecimal("R11_NET_AMT"));
			obj.setR11_net_amt_bwp(rs.getBigDecimal("R11_NET_AMT_BWP"));
			obj.setR11_bal_sub(rs.getBigDecimal("R11_BAL_SUB"));
			obj.setR11_bal_sub_bwp(rs.getBigDecimal("R11_BAL_SUB_BWP"));
			obj.setR11_bal_sub_diaries(rs.getBigDecimal("R11_BAL_SUB_DIARIES"));
			obj.setR11_bal_sub_diaries_bwp(rs.getBigDecimal("R11_BAL_SUB_DIARIES_BWP"));

			// ================= R12 =================
			obj.setR12_intrest_div(rs.getString("R12_INTREST_DIV"));
			obj.setR12_fig_bal_sheet(rs.getBigDecimal("R12_FIG_BAL_SHEET"));
			obj.setR12_fig_bal_sheet_bwp(rs.getBigDecimal("R12_FIG_BAL_SHEET_BWP"));
			obj.setR12_amt_statement_adj(rs.getBigDecimal("R12_AMT_STATEMENT_ADJ"));
			obj.setR12_amt_statement_adj_bwp(rs.getBigDecimal("R12_AMT_STATEMENT_ADJ_BWP"));
			obj.setR12_net_amt(rs.getBigDecimal("R12_NET_AMT"));
			obj.setR12_net_amt_bwp(rs.getBigDecimal("R12_NET_AMT_BWP"));
			obj.setR12_bal_sub(rs.getBigDecimal("R12_BAL_SUB"));
			obj.setR12_bal_sub_bwp(rs.getBigDecimal("R12_BAL_SUB_BWP"));
			obj.setR12_bal_sub_diaries(rs.getBigDecimal("R12_BAL_SUB_DIARIES"));
			obj.setR12_bal_sub_diaries_bwp(rs.getBigDecimal("R12_BAL_SUB_DIARIES_BWP"));

			// ================= R13 =================
			obj.setR13_intrest_div(rs.getString("R13_INTREST_DIV"));
			obj.setR13_fig_bal_sheet(rs.getBigDecimal("R13_FIG_BAL_SHEET"));
			obj.setR13_fig_bal_sheet_bwp(rs.getBigDecimal("R13_FIG_BAL_SHEET_BWP"));
			obj.setR13_amt_statement_adj(rs.getBigDecimal("R13_AMT_STATEMENT_ADJ"));
			obj.setR13_amt_statement_adj_bwp(rs.getBigDecimal("R13_AMT_STATEMENT_ADJ_BWP"));
			obj.setR13_net_amt(rs.getBigDecimal("R13_NET_AMT"));
			obj.setR13_net_amt_bwp(rs.getBigDecimal("R13_NET_AMT_BWP"));
			obj.setR13_bal_sub(rs.getBigDecimal("R13_BAL_SUB"));
			obj.setR13_bal_sub_bwp(rs.getBigDecimal("R13_BAL_SUB_BWP"));
			obj.setR13_bal_sub_diaries(rs.getBigDecimal("R13_BAL_SUB_DIARIES"));
			obj.setR13_bal_sub_diaries_bwp(rs.getBigDecimal("R13_BAL_SUB_DIARIES_BWP"));

			obj.setR17_other_income(rs.getString("R17_OTHER_INCOME"));
			obj.setR17_fig_bal_sheet(rs.getBigDecimal("R17_FIG_BAL_SHEET"));
			obj.setR17_fig_bal_sheet_bwp(rs.getBigDecimal("R17_FIG_BAL_SHEET_BWP"));
			obj.setR17_amt_statement_adj(rs.getBigDecimal("R17_AMT_STATEMENT_ADJ"));
			obj.setR17_amt_statement_adj_bwp(rs.getBigDecimal("R17_AMT_STATEMENT_ADJ_BWP"));
			obj.setR17_net_amt(rs.getBigDecimal("R17_NET_AMT"));
			obj.setR17_net_amt_bwp(rs.getBigDecimal("R17_NET_AMT_BWP"));
			obj.setR17_bal_sub(rs.getBigDecimal("R17_BAL_SUB"));
			obj.setR17_bal_sub_bwp(rs.getBigDecimal("R17_BAL_SUB_BWP"));
			obj.setR17_bal_sub_diaries(rs.getBigDecimal("R17_BAL_SUB_DIARIES"));
			obj.setR17_bal_sub_diaries_bwp(rs.getBigDecimal("R17_BAL_SUB_DIARIES_BWP"));

			obj.setR18_other_income(rs.getString("R18_OTHER_INCOME"));
			obj.setR18_fig_bal_sheet(rs.getBigDecimal("R18_FIG_BAL_SHEET"));
			obj.setR18_fig_bal_sheet_bwp(rs.getBigDecimal("R18_FIG_BAL_SHEET_BWP"));
			obj.setR18_amt_statement_adj(rs.getBigDecimal("R18_AMT_STATEMENT_ADJ"));
			obj.setR18_amt_statement_adj_bwp(rs.getBigDecimal("R18_AMT_STATEMENT_ADJ_BWP"));
			obj.setR18_net_amt(rs.getBigDecimal("R18_NET_AMT"));
			obj.setR18_net_amt_bwp(rs.getBigDecimal("R18_NET_AMT_BWP"));
			obj.setR18_bal_sub(rs.getBigDecimal("R18_BAL_SUB"));
			obj.setR18_bal_sub_bwp(rs.getBigDecimal("R18_BAL_SUB_BWP"));
			obj.setR18_bal_sub_diaries(rs.getBigDecimal("R18_BAL_SUB_DIARIES"));
			obj.setR18_bal_sub_diaries_bwp(rs.getBigDecimal("R18_BAL_SUB_DIARIES_BWP"));

			obj.setR19_other_income(rs.getString("R19_OTHER_INCOME"));
			obj.setR19_fig_bal_sheet(rs.getBigDecimal("R19_FIG_BAL_SHEET"));
			obj.setR19_fig_bal_sheet_bwp(rs.getBigDecimal("R19_FIG_BAL_SHEET_BWP"));
			obj.setR19_amt_statement_adj(rs.getBigDecimal("R19_AMT_STATEMENT_ADJ"));
			obj.setR19_amt_statement_adj_bwp(rs.getBigDecimal("R19_AMT_STATEMENT_ADJ_BWP"));
			obj.setR19_net_amt(rs.getBigDecimal("R19_NET_AMT"));
			obj.setR19_net_amt_bwp(rs.getBigDecimal("R19_NET_AMT_BWP"));
			obj.setR19_bal_sub(rs.getBigDecimal("R19_BAL_SUB"));
			obj.setR19_bal_sub_bwp(rs.getBigDecimal("R19_BAL_SUB_BWP"));
			obj.setR19_bal_sub_diaries(rs.getBigDecimal("R19_BAL_SUB_DIARIES"));
			obj.setR19_bal_sub_diaries_bwp(rs.getBigDecimal("R19_BAL_SUB_DIARIES_BWP"));

			obj.setR20_other_income(rs.getString("R20_OTHER_INCOME"));
			obj.setR20_fig_bal_sheet(rs.getBigDecimal("R20_FIG_BAL_SHEET"));
			obj.setR20_fig_bal_sheet_bwp(rs.getBigDecimal("R20_FIG_BAL_SHEET_BWP"));
			obj.setR20_amt_statement_adj(rs.getBigDecimal("R20_AMT_STATEMENT_ADJ"));
			obj.setR20_amt_statement_adj_bwp(rs.getBigDecimal("R20_AMT_STATEMENT_ADJ_BWP"));
			obj.setR20_net_amt(rs.getBigDecimal("R20_NET_AMT"));
			obj.setR20_net_amt_bwp(rs.getBigDecimal("R20_NET_AMT_BWP"));
			obj.setR20_bal_sub(rs.getBigDecimal("R20_BAL_SUB"));
			obj.setR20_bal_sub_bwp(rs.getBigDecimal("R20_BAL_SUB_BWP"));
			obj.setR20_bal_sub_diaries(rs.getBigDecimal("R20_BAL_SUB_DIARIES"));
			obj.setR20_bal_sub_diaries_bwp(rs.getBigDecimal("R20_BAL_SUB_DIARIES_BWP"));

			obj.setR21_other_income(rs.getString("R21_OTHER_INCOME"));
			obj.setR21_fig_bal_sheet(rs.getBigDecimal("R21_FIG_BAL_SHEET"));
			obj.setR21_fig_bal_sheet_bwp(rs.getBigDecimal("R21_FIG_BAL_SHEET_BWP"));
			obj.setR21_amt_statement_adj(rs.getBigDecimal("R21_AMT_STATEMENT_ADJ"));
			obj.setR21_amt_statement_adj_bwp(rs.getBigDecimal("R21_AMT_STATEMENT_ADJ_BWP"));
			obj.setR21_net_amt(rs.getBigDecimal("R21_NET_AMT"));
			obj.setR21_net_amt_bwp(rs.getBigDecimal("R21_NET_AMT_BWP"));
			obj.setR21_bal_sub(rs.getBigDecimal("R21_BAL_SUB"));
			obj.setR21_bal_sub_bwp(rs.getBigDecimal("R21_BAL_SUB_BWP"));
			obj.setR21_bal_sub_diaries(rs.getBigDecimal("R21_BAL_SUB_DIARIES"));
			obj.setR21_bal_sub_diaries_bwp(rs.getBigDecimal("R21_BAL_SUB_DIARIES_BWP"));

			obj.setR22_other_income(rs.getString("R22_OTHER_INCOME"));
			obj.setR22_fig_bal_sheet(rs.getBigDecimal("R22_FIG_BAL_SHEET"));
			obj.setR22_fig_bal_sheet_bwp(rs.getBigDecimal("R22_FIG_BAL_SHEET_BWP"));
			obj.setR22_amt_statement_adj(rs.getBigDecimal("R22_AMT_STATEMENT_ADJ"));
			obj.setR22_amt_statement_adj_bwp(rs.getBigDecimal("R22_AMT_STATEMENT_ADJ_BWP"));
			obj.setR22_net_amt(rs.getBigDecimal("R22_NET_AMT"));
			obj.setR22_net_amt_bwp(rs.getBigDecimal("R22_NET_AMT_BWP"));
			obj.setR22_bal_sub(rs.getBigDecimal("R22_BAL_SUB"));
			obj.setR22_bal_sub_bwp(rs.getBigDecimal("R22_BAL_SUB_BWP"));
			obj.setR22_bal_sub_diaries(rs.getBigDecimal("R22_BAL_SUB_DIARIES"));
			obj.setR22_bal_sub_diaries_bwp(rs.getBigDecimal("R22_BAL_SUB_DIARIES_BWP"));

			obj.setR23_other_income(rs.getString("R23_OTHER_INCOME"));
			obj.setR23_fig_bal_sheet(rs.getBigDecimal("R23_FIG_BAL_SHEET"));
			obj.setR23_fig_bal_sheet_bwp(rs.getBigDecimal("R23_FIG_BAL_SHEET_BWP"));
			obj.setR23_amt_statement_adj(rs.getBigDecimal("R23_AMT_STATEMENT_ADJ"));
			obj.setR23_amt_statement_adj_bwp(rs.getBigDecimal("R23_AMT_STATEMENT_ADJ_BWP"));
			obj.setR23_net_amt(rs.getBigDecimal("R23_NET_AMT"));
			obj.setR23_net_amt_bwp(rs.getBigDecimal("R23_NET_AMT_BWP"));
			obj.setR23_bal_sub(rs.getBigDecimal("R23_BAL_SUB"));
			obj.setR23_bal_sub_bwp(rs.getBigDecimal("R23_BAL_SUB_BWP"));
			obj.setR23_bal_sub_diaries(rs.getBigDecimal("R23_BAL_SUB_DIARIES"));
			obj.setR23_bal_sub_diaries_bwp(rs.getBigDecimal("R23_BAL_SUB_DIARIES_BWP"));

			obj.setR24_other_income(rs.getString("R24_OTHER_INCOME"));
			obj.setR24_fig_bal_sheet(rs.getBigDecimal("R24_FIG_BAL_SHEET"));
			obj.setR24_fig_bal_sheet_bwp(rs.getBigDecimal("R24_FIG_BAL_SHEET_BWP"));
			obj.setR24_amt_statement_adj(rs.getBigDecimal("R24_AMT_STATEMENT_ADJ"));
			obj.setR24_amt_statement_adj_bwp(rs.getBigDecimal("R24_AMT_STATEMENT_ADJ_BWP"));
			obj.setR24_net_amt(rs.getBigDecimal("R24_NET_AMT"));
			obj.setR24_net_amt_bwp(rs.getBigDecimal("R24_NET_AMT_BWP"));
			obj.setR24_bal_sub(rs.getBigDecimal("R24_BAL_SUB"));
			obj.setR24_bal_sub_bwp(rs.getBigDecimal("R24_BAL_SUB_BWP"));
			obj.setR24_bal_sub_diaries(rs.getBigDecimal("R24_BAL_SUB_DIARIES"));
			obj.setR24_bal_sub_diaries_bwp(rs.getBigDecimal("R24_BAL_SUB_DIARIES_BWP"));

			obj.setR25_other_income(rs.getString("R25_OTHER_INCOME"));
			obj.setR25_fig_bal_sheet(rs.getBigDecimal("R25_FIG_BAL_SHEET"));
			obj.setR25_fig_bal_sheet_bwp(rs.getBigDecimal("R25_FIG_BAL_SHEET_BWP"));
			obj.setR25_amt_statement_adj(rs.getBigDecimal("R25_AMT_STATEMENT_ADJ"));
			obj.setR25_amt_statement_adj_bwp(rs.getBigDecimal("R25_AMT_STATEMENT_ADJ_BWP"));
			obj.setR25_net_amt(rs.getBigDecimal("R25_NET_AMT"));
			obj.setR25_net_amt_bwp(rs.getBigDecimal("R25_NET_AMT_BWP"));
			obj.setR25_bal_sub(rs.getBigDecimal("R25_BAL_SUB"));
			obj.setR25_bal_sub_bwp(rs.getBigDecimal("R25_BAL_SUB_BWP"));
			obj.setR25_bal_sub_diaries(rs.getBigDecimal("R25_BAL_SUB_DIARIES"));
			obj.setR25_bal_sub_diaries_bwp(rs.getBigDecimal("R25_BAL_SUB_DIARIES_BWP"));

			obj.setR26_other_income(rs.getString("R26_OTHER_INCOME"));
			obj.setR26_fig_bal_sheet(rs.getBigDecimal("R26_FIG_BAL_SHEET"));
			obj.setR26_fig_bal_sheet_bwp(rs.getBigDecimal("R26_FIG_BAL_SHEET_BWP"));
			obj.setR26_amt_statement_adj(rs.getBigDecimal("R26_AMT_STATEMENT_ADJ"));
			obj.setR26_amt_statement_adj_bwp(rs.getBigDecimal("R26_AMT_STATEMENT_ADJ_BWP"));
			obj.setR26_net_amt(rs.getBigDecimal("R26_NET_AMT"));
			obj.setR26_net_amt_bwp(rs.getBigDecimal("R26_NET_AMT_BWP"));
			obj.setR26_bal_sub(rs.getBigDecimal("R26_BAL_SUB"));
			obj.setR26_bal_sub_bwp(rs.getBigDecimal("R26_BAL_SUB_BWP"));
			obj.setR26_bal_sub_diaries(rs.getBigDecimal("R26_BAL_SUB_DIARIES"));
			obj.setR26_bal_sub_diaries_bwp(rs.getBigDecimal("R26_BAL_SUB_DIARIES_BWP"));
			// R27
			obj.setR27_other_income(rs.getString("R27_OTHER_INCOME"));
			obj.setR27_fig_bal_sheet(rs.getBigDecimal("R27_FIG_BAL_SHEET"));
			obj.setR27_fig_bal_sheet_bwp(rs.getBigDecimal("R27_FIG_BAL_SHEET_BWP"));
			obj.setR27_amt_statement_adj(rs.getBigDecimal("R27_AMT_STATEMENT_ADJ"));
			obj.setR27_amt_statement_adj_bwp(rs.getBigDecimal("R27_AMT_STATEMENT_ADJ_BWP"));
			obj.setR27_net_amt(rs.getBigDecimal("R27_NET_AMT"));
			obj.setR27_net_amt_bwp(rs.getBigDecimal("R27_NET_AMT_BWP"));
			obj.setR27_bal_sub(rs.getBigDecimal("R27_BAL_SUB"));
			obj.setR27_bal_sub_bwp(rs.getBigDecimal("R27_BAL_SUB_BWP"));
			obj.setR27_bal_sub_diaries(rs.getBigDecimal("R27_BAL_SUB_DIARIES"));
			obj.setR27_bal_sub_diaries_bwp(rs.getBigDecimal("R27_BAL_SUB_DIARIES_BWP"));

			// R28
			obj.setR28_other_income(rs.getString("R28_OTHER_INCOME"));
			obj.setR28_fig_bal_sheet(rs.getBigDecimal("R28_FIG_BAL_SHEET"));
			obj.setR28_fig_bal_sheet_bwp(rs.getBigDecimal("R28_FIG_BAL_SHEET_BWP"));
			obj.setR28_amt_statement_adj(rs.getBigDecimal("R28_AMT_STATEMENT_ADJ"));
			obj.setR28_amt_statement_adj_bwp(rs.getBigDecimal("R28_AMT_STATEMENT_ADJ_BWP"));
			obj.setR28_net_amt(rs.getBigDecimal("R28_NET_AMT"));
			obj.setR28_net_amt_bwp(rs.getBigDecimal("R28_NET_AMT_BWP"));
			obj.setR28_bal_sub(rs.getBigDecimal("R28_BAL_SUB"));
			obj.setR28_bal_sub_bwp(rs.getBigDecimal("R28_BAL_SUB_BWP"));
			obj.setR28_bal_sub_diaries(rs.getBigDecimal("R28_BAL_SUB_DIARIES"));
			obj.setR28_bal_sub_diaries_bwp(rs.getBigDecimal("R28_BAL_SUB_DIARIES_BWP"));

			// R29
			obj.setR29_other_income(rs.getString("R29_OTHER_INCOME"));
			obj.setR29_fig_bal_sheet(rs.getBigDecimal("R29_FIG_BAL_SHEET"));
			obj.setR29_fig_bal_sheet_bwp(rs.getBigDecimal("R29_FIG_BAL_SHEET_BWP"));
			obj.setR29_amt_statement_adj(rs.getBigDecimal("R29_AMT_STATEMENT_ADJ"));
			obj.setR29_amt_statement_adj_bwp(rs.getBigDecimal("R29_AMT_STATEMENT_ADJ_BWP"));
			obj.setR29_net_amt(rs.getBigDecimal("R29_NET_AMT"));
			obj.setR29_net_amt_bwp(rs.getBigDecimal("R29_NET_AMT_BWP"));
			obj.setR29_bal_sub(rs.getBigDecimal("R29_BAL_SUB"));
			obj.setR29_bal_sub_bwp(rs.getBigDecimal("R29_BAL_SUB_BWP"));
			obj.setR29_bal_sub_diaries(rs.getBigDecimal("R29_BAL_SUB_DIARIES"));
			obj.setR29_bal_sub_diaries_bwp(rs.getBigDecimal("R29_BAL_SUB_DIARIES_BWP"));

			// R30
			obj.setR30_other_income(rs.getString("R30_OTHER_INCOME"));
			obj.setR30_fig_bal_sheet(rs.getBigDecimal("R30_FIG_BAL_SHEET"));
			obj.setR30_fig_bal_sheet_bwp(rs.getBigDecimal("R30_FIG_BAL_SHEET_BWP"));
			obj.setR30_amt_statement_adj(rs.getBigDecimal("R30_AMT_STATEMENT_ADJ"));
			obj.setR30_amt_statement_adj_bwp(rs.getBigDecimal("R30_AMT_STATEMENT_ADJ_BWP"));
			obj.setR30_net_amt(rs.getBigDecimal("R30_NET_AMT"));
			obj.setR30_net_amt_bwp(rs.getBigDecimal("R30_NET_AMT_BWP"));
			obj.setR30_bal_sub(rs.getBigDecimal("R30_BAL_SUB"));
			obj.setR30_bal_sub_bwp(rs.getBigDecimal("R30_BAL_SUB_BWP"));
			obj.setR30_bal_sub_diaries(rs.getBigDecimal("R30_BAL_SUB_DIARIES"));
			obj.setR30_bal_sub_diaries_bwp(rs.getBigDecimal("R30_BAL_SUB_DIARIES_BWP"));

			// R31
			obj.setR31_other_income(rs.getString("R31_OTHER_INCOME"));
			obj.setR31_fig_bal_sheet(rs.getBigDecimal("R31_FIG_BAL_SHEET"));
			obj.setR31_fig_bal_sheet_bwp(rs.getBigDecimal("R31_FIG_BAL_SHEET_BWP"));
			obj.setR31_amt_statement_adj(rs.getBigDecimal("R31_AMT_STATEMENT_ADJ"));
			obj.setR31_amt_statement_adj_bwp(rs.getBigDecimal("R31_AMT_STATEMENT_ADJ_BWP"));
			obj.setR31_net_amt(rs.getBigDecimal("R31_NET_AMT"));
			obj.setR31_net_amt_bwp(rs.getBigDecimal("R31_NET_AMT_BWP"));
			obj.setR31_bal_sub(rs.getBigDecimal("R31_BAL_SUB"));
			obj.setR31_bal_sub_bwp(rs.getBigDecimal("R31_BAL_SUB_BWP"));
			obj.setR31_bal_sub_diaries(rs.getBigDecimal("R31_BAL_SUB_DIARIES"));
			obj.setR31_bal_sub_diaries_bwp(rs.getBigDecimal("R31_BAL_SUB_DIARIES_BWP"));
// ================= R40 =================
			obj.setR40_intrest_expended(rs.getString("R40_INTREST_EXPENDED"));
			obj.setR40_fig_bal_sheet(rs.getBigDecimal("R40_FIG_BAL_SHEET"));
			obj.setR40_fig_bal_sheet_bwp(rs.getBigDecimal("R40_FIG_BAL_SHEET_BWP"));
			obj.setR40_amt_statement_adj(rs.getBigDecimal("R40_AMT_STATEMENT_ADJ"));
			obj.setR40_amt_statement_adj_bwp(rs.getBigDecimal("R40_AMT_STATEMENT_ADJ_BWP"));
			obj.setR40_net_amt(rs.getBigDecimal("R40_NET_AMT"));
			obj.setR40_net_amt_bwp(rs.getBigDecimal("R40_NET_AMT_BWP"));
			obj.setR40_bal_sub(rs.getBigDecimal("R40_BAL_SUB"));
			obj.setR40_bal_sub_bwp(rs.getBigDecimal("R40_BAL_SUB_BWP"));
			obj.setR40_bal_sub_diaries_bwp(rs.getBigDecimal("R40_BAL_SUB_DIARIES_BWP"));

			obj.setR41_intrest_expended(rs.getString("R41_INTREST_EXPENDED"));
			obj.setR41_fig_bal_sheet(rs.getBigDecimal("R41_FIG_BAL_SHEET"));
			obj.setR41_fig_bal_sheet_bwp(rs.getBigDecimal("R41_FIG_BAL_SHEET_BWP"));
			obj.setR41_amt_statement_adj(rs.getBigDecimal("R41_AMT_STATEMENT_ADJ"));
			obj.setR41_amt_statement_adj_bwp(rs.getBigDecimal("R41_AMT_STATEMENT_ADJ_BWP"));
			obj.setR41_net_amt(rs.getBigDecimal("R41_NET_AMT"));
			obj.setR41_net_amt_bwp(rs.getBigDecimal("R41_NET_AMT_BWP"));
			obj.setR41_bal_sub(rs.getBigDecimal("R41_BAL_SUB"));
			obj.setR41_bal_sub_bwp(rs.getBigDecimal("R41_BAL_SUB_BWP"));
			obj.setR41_bal_sub_diaries(rs.getBigDecimal("R41_BAL_SUB_DIARIES"));
			obj.setR41_bal_sub_diaries_bwp(rs.getBigDecimal("R41_BAL_SUB_DIARIES_BWP"));

			obj.setR42_intrest_expended(rs.getString("R42_INTREST_EXPENDED"));
			obj.setR42_fig_bal_sheet(rs.getBigDecimal("R42_FIG_BAL_SHEET"));
			obj.setR42_fig_bal_sheet_bwp(rs.getBigDecimal("R42_FIG_BAL_SHEET_BWP"));
			obj.setR42_amt_statement_adj(rs.getBigDecimal("R42_AMT_STATEMENT_ADJ"));
			obj.setR42_amt_statement_adj_bwp(rs.getBigDecimal("R42_AMT_STATEMENT_ADJ_BWP"));
			obj.setR42_net_amt(rs.getBigDecimal("R42_NET_AMT"));
			obj.setR42_net_amt_bwp(rs.getBigDecimal("R42_NET_AMT_BWP"));
			obj.setR42_bal_sub(rs.getBigDecimal("R42_BAL_SUB"));
			obj.setR42_bal_sub_bwp(rs.getBigDecimal("R42_BAL_SUB_BWP"));
			obj.setR42_bal_sub_diaries(rs.getBigDecimal("R42_BAL_SUB_DIARIES"));
			obj.setR42_bal_sub_diaries_bwp(rs.getBigDecimal("R42_BAL_SUB_DIARIES_BWP"));

			obj.setR43_intrest_expended(rs.getString("R43_INTREST_EXPENDED"));
			obj.setR43_fig_bal_sheet(rs.getBigDecimal("R43_FIG_BAL_SHEET"));
			obj.setR43_fig_bal_sheet_bwp(rs.getBigDecimal("R43_FIG_BAL_SHEET_BWP"));
			obj.setR43_amt_statement_adj(rs.getBigDecimal("R43_AMT_STATEMENT_ADJ"));
			obj.setR43_amt_statement_adj_bwp(rs.getBigDecimal("R43_AMT_STATEMENT_ADJ_BWP"));
			obj.setR43_net_amt(rs.getBigDecimal("R43_NET_AMT"));
			obj.setR43_net_amt_bwp(rs.getBigDecimal("R43_NET_AMT_BWP"));
			obj.setR43_bal_sub(rs.getBigDecimal("R43_BAL_SUB"));
			obj.setR43_bal_sub_bwp(rs.getBigDecimal("R43_BAL_SUB_BWP"));
			obj.setR43_bal_sub_diaries(rs.getBigDecimal("R43_BAL_SUB_DIARIES"));
			obj.setR43_bal_sub_diaries_bwp(rs.getBigDecimal("R43_BAL_SUB_DIARIES_BWP"));

// ================= R48 =================
			obj.setR48_operating_expenses(rs.getString("R48_OPERATING_EXPENSES"));
			obj.setR48_fig_bal_sheet(rs.getBigDecimal("R48_FIG_BAL_SHEET"));
			obj.setR48_fig_bal_sheet_bwp(rs.getBigDecimal("R48_FIG_BAL_SHEET_BWP"));
			obj.setR48_amt_statement_adj(rs.getBigDecimal("R48_AMT_STATEMENT_ADJ"));
			obj.setR48_amt_statement_adj_bwp(rs.getBigDecimal("R48_AMT_STATEMENT_ADJ_BWP"));
			obj.setR48_net_amt(rs.getBigDecimal("R48_NET_AMT"));
			obj.setR48_net_amt_bwp(rs.getBigDecimal("R48_NET_AMT_BWP"));
			obj.setR48_bal_sub(rs.getBigDecimal("R48_BAL_SUB"));
			obj.setR48_bal_sub_bwp(rs.getBigDecimal("R48_BAL_SUB_BWP"));
			obj.setR48_bal_sub_diaries(rs.getBigDecimal("R48_BAL_SUB_DIARIES"));
			obj.setR48_bal_sub_diaries_bwp(rs.getBigDecimal("R48_BAL_SUB_DIARIES_BWP"));

			obj.setR49_operating_expenses(rs.getString("R49_OPERATING_EXPENSES"));
			obj.setR49_fig_bal_sheet(rs.getBigDecimal("R49_FIG_BAL_SHEET"));
			obj.setR49_fig_bal_sheet_bwp(rs.getBigDecimal("R49_FIG_BAL_SHEET_BWP"));
			obj.setR49_amt_statement_adj(rs.getBigDecimal("R49_AMT_STATEMENT_ADJ"));
			obj.setR49_amt_statement_adj_bwp(rs.getBigDecimal("R49_AMT_STATEMENT_ADJ_BWP"));
			obj.setR49_net_amt(rs.getBigDecimal("R49_NET_AMT"));
			obj.setR49_net_amt_bwp(rs.getBigDecimal("R49_NET_AMT_BWP"));
			obj.setR49_bal_sub(rs.getBigDecimal("R49_BAL_SUB"));
			obj.setR49_bal_sub_bwp(rs.getBigDecimal("R49_BAL_SUB_BWP"));
			obj.setR49_bal_sub_diaries(rs.getBigDecimal("R49_BAL_SUB_DIARIES"));
			obj.setR49_bal_sub_diaries_bwp(rs.getBigDecimal("R49_BAL_SUB_DIARIES_BWP"));

			obj.setR50_operating_expenses(rs.getString("R50_OPERATING_EXPENSES"));
			obj.setR50_fig_bal_sheet(rs.getBigDecimal("R50_FIG_BAL_SHEET"));
			obj.setR50_fig_bal_sheet_bwp(rs.getBigDecimal("R50_FIG_BAL_SHEET_BWP"));
			obj.setR50_amt_statement_adj(rs.getBigDecimal("R50_AMT_STATEMENT_ADJ"));
			obj.setR50_amt_statement_adj_bwp(rs.getBigDecimal("R50_AMT_STATEMENT_ADJ_BWP"));
			obj.setR50_net_amt(rs.getBigDecimal("R50_NET_AMT"));
			obj.setR50_net_amt_bwp(rs.getBigDecimal("R50_NET_AMT_BWP"));
			obj.setR50_bal_sub(rs.getBigDecimal("R50_BAL_SUB"));
			obj.setR50_bal_sub_bwp(rs.getBigDecimal("R50_BAL_SUB_BWP"));
			obj.setR50_bal_sub_diaries(rs.getBigDecimal("R50_BAL_SUB_DIARIES"));
			obj.setR50_bal_sub_diaries_bwp(rs.getBigDecimal("R50_BAL_SUB_DIARIES_BWP"));

			obj.setR51_operating_expenses(rs.getString("R51_OPERATING_EXPENSES"));
			obj.setR51_fig_bal_sheet(rs.getBigDecimal("R51_FIG_BAL_SHEET"));
			obj.setR51_fig_bal_sheet_bwp(rs.getBigDecimal("R51_FIG_BAL_SHEET_BWP"));
			obj.setR51_amt_statement_adj(rs.getBigDecimal("R51_AMT_STATEMENT_ADJ"));
			obj.setR51_amt_statement_adj_bwp(rs.getBigDecimal("R51_AMT_STATEMENT_ADJ_BWP"));
			obj.setR51_net_amt(rs.getBigDecimal("R51_NET_AMT"));
			obj.setR51_net_amt_bwp(rs.getBigDecimal("R51_NET_AMT_BWP"));
			obj.setR51_bal_sub(rs.getBigDecimal("R51_BAL_SUB"));
			obj.setR51_bal_sub_bwp(rs.getBigDecimal("R51_BAL_SUB_BWP"));
			obj.setR51_bal_sub_diaries(rs.getBigDecimal("R51_BAL_SUB_DIARIES"));
			obj.setR51_bal_sub_diaries_bwp(rs.getBigDecimal("R51_BAL_SUB_DIARIES_BWP"));

			obj.setR52_operating_expenses(rs.getString("R52_OPERATING_EXPENSES"));
			obj.setR52_fig_bal_sheet(rs.getBigDecimal("R52_FIG_BAL_SHEET"));
			obj.setR52_fig_bal_sheet_bwp(rs.getBigDecimal("R52_FIG_BAL_SHEET_BWP"));
			obj.setR52_amt_statement_adj(rs.getBigDecimal("R52_AMT_STATEMENT_ADJ"));
			obj.setR52_amt_statement_adj_bwp(rs.getBigDecimal("R52_AMT_STATEMENT_ADJ_BWP"));
			obj.setR52_net_amt(rs.getBigDecimal("R52_NET_AMT"));
			obj.setR52_net_amt_bwp(rs.getBigDecimal("R52_NET_AMT_BWP"));
			obj.setR52_bal_sub(rs.getBigDecimal("R52_BAL_SUB"));
			obj.setR52_bal_sub_bwp(rs.getBigDecimal("R52_BAL_SUB_BWP"));
			obj.setR52_bal_sub_diaries(rs.getBigDecimal("R52_BAL_SUB_DIARIES"));
			obj.setR52_bal_sub_diaries_bwp(rs.getBigDecimal("R52_BAL_SUB_DIARIES_BWP"));

			obj.setR53_operating_expenses(rs.getString("R53_OPERATING_EXPENSES"));
			obj.setR53_fig_bal_sheet(rs.getBigDecimal("R53_FIG_BAL_SHEET"));
			obj.setR53_fig_bal_sheet_bwp(rs.getBigDecimal("R53_FIG_BAL_SHEET_BWP"));
			obj.setR53_amt_statement_adj(rs.getBigDecimal("R53_AMT_STATEMENT_ADJ"));
			obj.setR53_amt_statement_adj_bwp(rs.getBigDecimal("R53_AMT_STATEMENT_ADJ_BWP"));
			obj.setR53_net_amt(rs.getBigDecimal("R53_NET_AMT"));
			obj.setR53_net_amt_bwp(rs.getBigDecimal("R53_NET_AMT_BWP"));
			obj.setR53_bal_sub(rs.getBigDecimal("R53_BAL_SUB"));
			obj.setR53_bal_sub_bwp(rs.getBigDecimal("R53_BAL_SUB_BWP"));
			obj.setR53_bal_sub_diaries(rs.getBigDecimal("R53_BAL_SUB_DIARIES"));
			obj.setR53_bal_sub_diaries_bwp(rs.getBigDecimal("R53_BAL_SUB_DIARIES_BWP"));

			obj.setR54_operating_expenses(rs.getString("R54_OPERATING_EXPENSES"));
			obj.setR54_fig_bal_sheet(rs.getBigDecimal("R54_FIG_BAL_SHEET"));
			obj.setR54_fig_bal_sheet_bwp(rs.getBigDecimal("R54_FIG_BAL_SHEET_BWP"));
			obj.setR54_amt_statement_adj(rs.getBigDecimal("R54_AMT_STATEMENT_ADJ"));
			obj.setR54_amt_statement_adj_bwp(rs.getBigDecimal("R54_AMT_STATEMENT_ADJ_BWP"));
			obj.setR54_net_amt(rs.getBigDecimal("R54_NET_AMT"));
			obj.setR54_net_amt_bwp(rs.getBigDecimal("R54_NET_AMT_BWP"));
			obj.setR54_bal_sub(rs.getBigDecimal("R54_BAL_SUB"));
			obj.setR54_bal_sub_bwp(rs.getBigDecimal("R54_BAL_SUB_BWP"));
			obj.setR54_bal_sub_diaries(rs.getBigDecimal("R54_BAL_SUB_DIARIES"));
			obj.setR54_bal_sub_diaries_bwp(rs.getBigDecimal("R54_BAL_SUB_DIARIES_BWP"));

			obj.setR55_operating_expenses(rs.getString("R55_OPERATING_EXPENSES"));
			obj.setR55_fig_bal_sheet(rs.getBigDecimal("R55_FIG_BAL_SHEET"));
			obj.setR55_fig_bal_sheet_bwp(rs.getBigDecimal("R55_FIG_BAL_SHEET_BWP"));
			obj.setR55_amt_statement_adj(rs.getBigDecimal("R55_AMT_STATEMENT_ADJ"));
			obj.setR55_amt_statement_adj_bwp(rs.getBigDecimal("R55_AMT_STATEMENT_ADJ_BWP"));
			obj.setR55_net_amt(rs.getBigDecimal("R55_NET_AMT"));
			obj.setR55_net_amt_bwp(rs.getBigDecimal("R55_NET_AMT_BWP"));
			obj.setR55_bal_sub(rs.getBigDecimal("R55_BAL_SUB"));
			obj.setR55_bal_sub_bwp(rs.getBigDecimal("R55_BAL_SUB_BWP"));
			obj.setR55_bal_sub_diaries(rs.getBigDecimal("R55_BAL_SUB_DIARIES"));
			obj.setR55_bal_sub_diaries_bwp(rs.getBigDecimal("R55_BAL_SUB_DIARIES_BWP"));

			obj.setR56_operating_expenses(rs.getString("R56_OPERATING_EXPENSES"));
			obj.setR56_fig_bal_sheet(rs.getBigDecimal("R56_FIG_BAL_SHEET"));
			obj.setR56_fig_bal_sheet_bwp(rs.getBigDecimal("R56_FIG_BAL_SHEET_BWP"));
			obj.setR56_amt_statement_adj(rs.getBigDecimal("R56_AMT_STATEMENT_ADJ"));
			obj.setR56_amt_statement_adj_bwp(rs.getBigDecimal("R56_AMT_STATEMENT_ADJ_BWP"));
			obj.setR56_net_amt(rs.getBigDecimal("R56_NET_AMT"));
			obj.setR56_net_amt_bwp(rs.getBigDecimal("R56_NET_AMT_BWP"));
			obj.setR56_bal_sub(rs.getBigDecimal("R56_BAL_SUB"));
			obj.setR56_bal_sub_bwp(rs.getBigDecimal("R56_BAL_SUB_BWP"));
			obj.setR56_bal_sub_diaries(rs.getBigDecimal("R56_BAL_SUB_DIARIES"));
			obj.setR56_bal_sub_diaries_bwp(rs.getBigDecimal("R56_BAL_SUB_DIARIES_BWP"));

			obj.setR57_operating_expenses(rs.getString("R57_OPERATING_EXPENSES"));
			obj.setR57_fig_bal_sheet(rs.getBigDecimal("R57_FIG_BAL_SHEET"));
			obj.setR57_fig_bal_sheet_bwp(rs.getBigDecimal("R57_FIG_BAL_SHEET_BWP"));
			obj.setR57_amt_statement_adj(rs.getBigDecimal("R57_AMT_STATEMENT_ADJ"));
			obj.setR57_amt_statement_adj_bwp(rs.getBigDecimal("R57_AMT_STATEMENT_ADJ_BWP"));
			obj.setR57_net_amt(rs.getBigDecimal("R57_NET_AMT"));
			obj.setR57_net_amt_bwp(rs.getBigDecimal("R57_NET_AMT_BWP"));
			obj.setR57_bal_sub(rs.getBigDecimal("R57_BAL_SUB"));
			obj.setR57_bal_sub_bwp(rs.getBigDecimal("R57_BAL_SUB_BWP"));
			obj.setR57_bal_sub_diaries(rs.getBigDecimal("R57_BAL_SUB_DIARIES"));
			obj.setR57_bal_sub_diaries_bwp(rs.getBigDecimal("R57_BAL_SUB_DIARIES_BWP"));

			obj.setR58_operating_expenses(rs.getString("R58_OPERATING_EXPENSES"));
			obj.setR58_fig_bal_sheet(rs.getBigDecimal("R58_FIG_BAL_SHEET"));
			obj.setR58_fig_bal_sheet_bwp(rs.getBigDecimal("R58_FIG_BAL_SHEET_BWP"));
			obj.setR58_amt_statement_adj(rs.getBigDecimal("R58_AMT_STATEMENT_ADJ"));
			obj.setR58_amt_statement_adj_bwp(rs.getBigDecimal("R58_AMT_STATEMENT_ADJ_BWP"));
			obj.setR58_net_amt(rs.getBigDecimal("R58_NET_AMT"));
			obj.setR58_net_amt_bwp(rs.getBigDecimal("R58_NET_AMT_BWP"));
			obj.setR58_bal_sub(rs.getBigDecimal("R58_BAL_SUB"));
			obj.setR58_bal_sub_bwp(rs.getBigDecimal("R58_BAL_SUB_BWP"));
			obj.setR58_bal_sub_diaries(rs.getBigDecimal("R58_BAL_SUB_DIARIES"));
			obj.setR58_bal_sub_diaries_bwp(rs.getBigDecimal("R58_BAL_SUB_DIARIES_BWP"));

			obj.setR59_operating_expenses(rs.getString("R59_OPERATING_EXPENSES"));
			obj.setR59_fig_bal_sheet(rs.getBigDecimal("R59_FIG_BAL_SHEET"));
			obj.setR59_fig_bal_sheet_bwp(rs.getBigDecimal("R59_FIG_BAL_SHEET_BWP"));
			obj.setR59_amt_statement_adj(rs.getBigDecimal("R59_AMT_STATEMENT_ADJ"));
			obj.setR59_amt_statement_adj_bwp(rs.getBigDecimal("R59_AMT_STATEMENT_ADJ_BWP"));
			obj.setR59_net_amt(rs.getBigDecimal("R59_NET_AMT"));
			obj.setR59_net_amt_bwp(rs.getBigDecimal("R59_NET_AMT_BWP"));
			obj.setR59_bal_sub(rs.getBigDecimal("R59_BAL_SUB"));
			obj.setR59_bal_sub_bwp(rs.getBigDecimal("R59_BAL_SUB_BWP"));
			obj.setR59_bal_sub_diaries(rs.getBigDecimal("R59_BAL_SUB_DIARIES"));
			obj.setR59_bal_sub_diaries_bwp(rs.getBigDecimal("R59_BAL_SUB_DIARIES_BWP"));

			obj.setR60_operating_expenses(rs.getString("R60_OPERATING_EXPENSES"));
			obj.setR60_fig_bal_sheet(rs.getBigDecimal("R60_FIG_BAL_SHEET"));
			obj.setR60_fig_bal_sheet_bwp(rs.getBigDecimal("R60_FIG_BAL_SHEET_BWP"));
			obj.setR60_amt_statement_adj(rs.getBigDecimal("R60_AMT_STATEMENT_ADJ"));
			obj.setR60_amt_statement_adj_bwp(rs.getBigDecimal("R60_AMT_STATEMENT_ADJ_BWP"));
			obj.setR60_net_amt(rs.getBigDecimal("R60_NET_AMT"));
			obj.setR60_net_amt_bwp(rs.getBigDecimal("R60_NET_AMT_BWP"));
			obj.setR60_bal_sub(rs.getBigDecimal("R60_BAL_SUB"));
			obj.setR60_bal_sub_bwp(rs.getBigDecimal("R60_BAL_SUB_BWP"));
			obj.setR60_bal_sub_diaries(rs.getBigDecimal("R60_BAL_SUB_DIARIES"));
			obj.setR60_bal_sub_diaries_bwp(rs.getBigDecimal("R60_BAL_SUB_DIARIES_BWP"));

			obj.setR61_operating_expenses(rs.getString("R61_OPERATING_EXPENSES"));
			obj.setR61_fig_bal_sheet(rs.getBigDecimal("R61_FIG_BAL_SHEET"));
			obj.setR61_fig_bal_sheet_bwp(rs.getBigDecimal("R61_FIG_BAL_SHEET_BWP"));
			obj.setR61_amt_statement_adj(rs.getBigDecimal("R61_AMT_STATEMENT_ADJ"));
			obj.setR61_amt_statement_adj_bwp(rs.getBigDecimal("R61_AMT_STATEMENT_ADJ_BWP"));
			obj.setR61_net_amt(rs.getBigDecimal("R61_NET_AMT"));
			obj.setR61_net_amt_bwp(rs.getBigDecimal("R61_NET_AMT_BWP"));
			obj.setR61_bal_sub(rs.getBigDecimal("R61_BAL_SUB"));
			obj.setR61_bal_sub_bwp(rs.getBigDecimal("R61_BAL_SUB_BWP"));
			obj.setR61_bal_sub_diaries(rs.getBigDecimal("R61_BAL_SUB_DIARIES"));
			obj.setR61_bal_sub_diaries_bwp(rs.getBigDecimal("R61_BAL_SUB_DIARIES_BWP"));

			obj.setR62_operating_expenses(rs.getString("R62_OPERATING_EXPENSES"));
			obj.setR62_fig_bal_sheet(rs.getBigDecimal("R62_FIG_BAL_SHEET"));
			obj.setR62_fig_bal_sheet_bwp(rs.getBigDecimal("R62_FIG_BAL_SHEET_BWP"));
			obj.setR62_amt_statement_adj(rs.getBigDecimal("R62_AMT_STATEMENT_ADJ"));
			obj.setR62_amt_statement_adj_bwp(rs.getBigDecimal("R62_AMT_STATEMENT_ADJ_BWP"));
			obj.setR62_net_amt(rs.getBigDecimal("R62_NET_AMT"));
			obj.setR62_net_amt_bwp(rs.getBigDecimal("R62_NET_AMT_BWP"));
			obj.setR62_bal_sub(rs.getBigDecimal("R62_BAL_SUB"));
			obj.setR62_bal_sub_bwp(rs.getBigDecimal("R62_BAL_SUB_BWP"));
			obj.setR62_bal_sub_diaries(rs.getBigDecimal("R62_BAL_SUB_DIARIES"));
			obj.setR62_bal_sub_diaries_bwp(rs.getBigDecimal("R62_BAL_SUB_DIARIES_BWP"));

			obj.setR63_operating_expenses(rs.getString("R63_OPERATING_EXPENSES"));
			obj.setR63_fig_bal_sheet(rs.getBigDecimal("R63_FIG_BAL_SHEET"));
			obj.setR63_fig_bal_sheet_bwp(rs.getBigDecimal("R63_FIG_BAL_SHEET_BWP"));
			obj.setR63_amt_statement_adj(rs.getBigDecimal("R63_AMT_STATEMENT_ADJ"));
			obj.setR63_amt_statement_adj_bwp(rs.getBigDecimal("R63_AMT_STATEMENT_ADJ_BWP"));
			obj.setR63_net_amt(rs.getBigDecimal("R63_NET_AMT"));
			obj.setR63_net_amt_bwp(rs.getBigDecimal("R63_NET_AMT_BWP"));
			obj.setR63_bal_sub(rs.getBigDecimal("R63_BAL_SUB"));
			obj.setR63_bal_sub_bwp(rs.getBigDecimal("R63_BAL_SUB_BWP"));
			obj.setR63_bal_sub_diaries(rs.getBigDecimal("R63_BAL_SUB_DIARIES"));
			obj.setR63_bal_sub_diaries_bwp(rs.getBigDecimal("R63_BAL_SUB_DIARIES_BWP"));

			obj.setR18_bal_sub_diaries(rs.getBigDecimal("R18_BAL_SUB_DIARIES"));
			obj.setR19_bal_sub_diaries(rs.getBigDecimal("R19_BAL_SUB_DIARIES"));
			obj.setR20_bal_sub_diaries(rs.getBigDecimal("R20_BAL_SUB_DIARIES"));
			obj.setR21_bal_sub_diaries(rs.getBigDecimal("R21_BAL_SUB_DIARIES"));
			obj.setR22_bal_sub_diaries(rs.getBigDecimal("R22_BAL_SUB_DIARIES"));
			obj.setR23_bal_sub_diaries(rs.getBigDecimal("R23_BAL_SUB_DIARIES"));
			obj.setR24_bal_sub_diaries(rs.getBigDecimal("R24_BAL_SUB_DIARIES"));
			obj.setR25_bal_sub_diaries(rs.getBigDecimal("R25_BAL_SUB_DIARIES"));
			obj.setR26_bal_sub_diaries(rs.getBigDecimal("R26_BAL_SUB_DIARIES"));
			obj.setR27_bal_sub_diaries(rs.getBigDecimal("R27_BAL_SUB_DIARIES"));
			obj.setR28_bal_sub_diaries(rs.getBigDecimal("R28_BAL_SUB_DIARIES"));
			obj.setR29_bal_sub_diaries(rs.getBigDecimal("R29_BAL_SUB_DIARIES"));
			obj.setR30_bal_sub_diaries(rs.getBigDecimal("R30_BAL_SUB_DIARIES"));
			obj.setR31_bal_sub_diaries(rs.getBigDecimal("R31_BAL_SUB_DIARIES"));

			obj.setR40_bal_sub_diaries(rs.getBigDecimal("R40_BAL_SUB_DIARIES"));
			obj.setR41_bal_sub_diaries(rs.getBigDecimal("R41_BAL_SUB_DIARIES"));
			obj.setR42_bal_sub_diaries(rs.getBigDecimal("R42_BAL_SUB_DIARIES"));
			obj.setR43_bal_sub_diaries(rs.getBigDecimal("R43_BAL_SUB_DIARIES"));

			obj.setR48_bal_sub_diaries(rs.getBigDecimal("R48_BAL_SUB_DIARIES"));
			obj.setR49_bal_sub_diaries(rs.getBigDecimal("R49_BAL_SUB_DIARIES"));
			obj.setR50_bal_sub_diaries(rs.getBigDecimal("R50_BAL_SUB_DIARIES"));
			obj.setR51_bal_sub_diaries(rs.getBigDecimal("R51_BAL_SUB_DIARIES"));
			obj.setR52_bal_sub_diaries(rs.getBigDecimal("R52_BAL_SUB_DIARIES"));
			obj.setR53_bal_sub_diaries(rs.getBigDecimal("R53_BAL_SUB_DIARIES"));
			obj.setR54_bal_sub_diaries(rs.getBigDecimal("R54_BAL_SUB_DIARIES"));
			obj.setR55_bal_sub_diaries(rs.getBigDecimal("R55_BAL_SUB_DIARIES"));
			obj.setR56_bal_sub_diaries(rs.getBigDecimal("R56_BAL_SUB_DIARIES"));
			obj.setR57_bal_sub_diaries(rs.getBigDecimal("R57_BAL_SUB_DIARIES"));
			obj.setR58_bal_sub_diaries(rs.getBigDecimal("R58_BAL_SUB_DIARIES"));
			obj.setR59_bal_sub_diaries(rs.getBigDecimal("R59_BAL_SUB_DIARIES"));
			obj.setR60_bal_sub_diaries(rs.getBigDecimal("R60_BAL_SUB_DIARIES"));
			obj.setR61_bal_sub_diaries(rs.getBigDecimal("R61_BAL_SUB_DIARIES"));
			obj.setR62_bal_sub_diaries(rs.getBigDecimal("R62_BAL_SUB_DIARIES"));
			obj.setR63_bal_sub_diaries(rs.getBigDecimal("R63_BAL_SUB_DIARIES"));

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

	public static class PL_SCHS_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		// ================= R9 =================
		@Column(name = "R9_INTREST_DIV")
		private String r9_intrest_div;

		@Column(name = "R9_FIG_BAL_SHEET")
		private BigDecimal r9_fig_bal_sheet;

		@Column(name = "R9_FIG_BAL_SHEET_BWP")
		private BigDecimal r9_fig_bal_sheet_bwp;

		@Column(name = "R9_AMT_STATEMENT_ADJ")
		private BigDecimal r9_amt_statement_adj;

		@Column(name = "R9_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r9_amt_statement_adj_bwp;

		@Column(name = "R9_NET_AMT")
		private BigDecimal r9_net_amt;

		@Column(name = "R9_NET_AMT_BWP")
		private BigDecimal r9_net_amt_bwp;

		@Column(name = "R9_BAL_SUB")
		private BigDecimal r9_bal_sub;

		@Column(name = "R9_BAL_SUB_BWP")
		private BigDecimal r9_bal_sub_bwp;

		@Column(name = "R9_BAL_SUB_DIARIES")
		private BigDecimal r9_bal_sub_diaries;

		@Column(name = "R9_BAL_SUB_DIARIES_BWP")
		private BigDecimal r9_bal_sub_diaries_bwp;

		// ================= R10 =================
		@Column(name = "R10_INTREST_DIV")
		private String r10_intrest_div;

		@Column(name = "R10_FIG_BAL_SHEET")
		private BigDecimal r10_fig_bal_sheet;

		@Column(name = "R10_FIG_BAL_SHEET_BWP")
		private BigDecimal r10_fig_bal_sheet_bwp;

		@Column(name = "R10_AMT_STATEMENT_ADJ")
		private BigDecimal r10_amt_statement_adj;

		@Column(name = "R10_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r10_amt_statement_adj_bwp;

		@Column(name = "R10_NET_AMT")
		private BigDecimal r10_net_amt;

		@Column(name = "R10_NET_AMT_BWP")
		private BigDecimal r10_net_amt_bwp;

		@Column(name = "R10_BAL_SUB")
		private BigDecimal r10_bal_sub;

		@Column(name = "R10_BAL_SUB_BWP")
		private BigDecimal r10_bal_sub_bwp;

		@Column(name = "R10_BAL_SUB_DIARIES")
		private BigDecimal r10_bal_sub_diaries;

		@Column(name = "R10_BAL_SUB_DIARIES_BWP")
		private BigDecimal r10_bal_sub_diaries_bwp;

		// ================= R11 =================
		@Column(name = "R11_INTREST_DIV")
		private String r11_intrest_div;

		@Column(name = "R11_FIG_BAL_SHEET")
		private BigDecimal r11_fig_bal_sheet;

		@Column(name = "R11_FIG_BAL_SHEET_BWP")
		private BigDecimal r11_fig_bal_sheet_bwp;

		@Column(name = "R11_AMT_STATEMENT_ADJ")
		private BigDecimal r11_amt_statement_adj;

		@Column(name = "R11_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r11_amt_statement_adj_bwp;

		@Column(name = "R11_NET_AMT")
		private BigDecimal r11_net_amt;

		@Column(name = "R11_NET_AMT_BWP")
		private BigDecimal r11_net_amt_bwp;

		@Column(name = "R11_BAL_SUB")
		private BigDecimal r11_bal_sub;

		@Column(name = "R11_BAL_SUB_BWP")
		private BigDecimal r11_bal_sub_bwp;

		@Column(name = "R11_BAL_SUB_DIARIES")
		private BigDecimal r11_bal_sub_diaries;

		@Column(name = "R11_BAL_SUB_DIARIES_BWP")
		private BigDecimal r11_bal_sub_diaries_bwp;

		// ================= R12 =================
		@Column(name = "R12_INTREST_DIV")
		private String r12_intrest_div;

		@Column(name = "R12_FIG_BAL_SHEET")
		private BigDecimal r12_fig_bal_sheet;

		@Column(name = "R12_FIG_BAL_SHEET_BWP")
		private BigDecimal r12_fig_bal_sheet_bwp;

		@Column(name = "R12_AMT_STATEMENT_ADJ")
		private BigDecimal r12_amt_statement_adj;

		@Column(name = "R12_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r12_amt_statement_adj_bwp;

		@Column(name = "R12_NET_AMT")
		private BigDecimal r12_net_amt;

		@Column(name = "R12_NET_AMT_BWP")
		private BigDecimal r12_net_amt_bwp;

		@Column(name = "R12_BAL_SUB")
		private BigDecimal r12_bal_sub;

		@Column(name = "R12_BAL_SUB_BWP")
		private BigDecimal r12_bal_sub_bwp;

		@Column(name = "R12_BAL_SUB_DIARIES")
		private BigDecimal r12_bal_sub_diaries;

		@Column(name = "R12_BAL_SUB_DIARIES_BWP")
		private BigDecimal r12_bal_sub_diaries_bwp;

		// ================= R13 =================
		@Column(name = "R13_INTREST_DIV")
		private String r13_intrest_div;

		@Column(name = "R13_FIG_BAL_SHEET")
		private BigDecimal r13_fig_bal_sheet;

		@Column(name = "R13_FIG_BAL_SHEET_BWP")
		private BigDecimal r13_fig_bal_sheet_bwp;

		@Column(name = "R13_AMT_STATEMENT_ADJ")
		private BigDecimal r13_amt_statement_adj;

		@Column(name = "R13_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r13_amt_statement_adj_bwp;

		@Column(name = "R13_NET_AMT")
		private BigDecimal r13_net_amt;

		@Column(name = "R13_NET_AMT_BWP")
		private BigDecimal r13_net_amt_bwp;

		@Column(name = "R13_BAL_SUB")
		private BigDecimal r13_bal_sub;

		@Column(name = "R13_BAL_SUB_BWP")
		private BigDecimal r13_bal_sub_bwp;

		@Column(name = "R13_BAL_SUB_DIARIES")
		private BigDecimal r13_bal_sub_diaries;

		@Column(name = "R13_BAL_SUB_DIARIES_BWP")
		private BigDecimal r13_bal_sub_diaries_bwp;

		// ================= R17 =================
		@Column(name = "R17_OTHER_INCOME")
		private String r17_other_income;

		@Column(name = "R17_FIG_BAL_SHEET")
		private BigDecimal r17_fig_bal_sheet;

		@Column(name = "R17_FIG_BAL_SHEET_BWP")
		private BigDecimal r17_fig_bal_sheet_bwp;

		@Column(name = "R17_AMT_STATEMENT_ADJ")
		private BigDecimal r17_amt_statement_adj;

		@Column(name = "R17_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r17_amt_statement_adj_bwp;

		@Column(name = "R17_NET_AMT")
		private BigDecimal r17_net_amt;

		@Column(name = "R17_NET_AMT_BWP")
		private BigDecimal r17_net_amt_bwp;

		@Column(name = "R17_BAL_SUB")
		private BigDecimal r17_bal_sub;

		@Column(name = "R17_BAL_SUB_BWP")
		private BigDecimal r17_bal_sub_bwp;

		@Column(name = "R17_BAL_SUB_DIARIES")
		private BigDecimal r17_bal_sub_diaries;

		@Column(name = "R17_BAL_SUB_DIARIES_BWP")
		private BigDecimal r17_bal_sub_diaries_bwp;

		// ================= R18 =================
		@Column(name = "R18_OTHER_INCOME")
		private String r18_other_income;

		@Column(name = "R18_FIG_BAL_SHEET")
		private BigDecimal r18_fig_bal_sheet;

		@Column(name = "R18_FIG_BAL_SHEET_BWP")
		private BigDecimal r18_fig_bal_sheet_bwp;

		@Column(name = "R18_AMT_STATEMENT_ADJ")
		private BigDecimal r18_amt_statement_adj;

		@Column(name = "R18_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r18_amt_statement_adj_bwp;

		@Column(name = "R18_NET_AMT")
		private BigDecimal r18_net_amt;

		@Column(name = "R18_NET_AMT_BWP")
		private BigDecimal r18_net_amt_bwp;

		@Column(name = "R18_BAL_SUB")
		private BigDecimal r18_bal_sub;

		@Column(name = "R18_BAL_SUB_BWP")
		private BigDecimal r18_bal_sub_bwp;

		@Column(name = "R18_BAL_SUB_DIARIES_BWP")
		private BigDecimal r18_bal_sub_diaries_bwp;

		// ================= R19 =================
		@Column(name = "R19_OTHER_INCOME")
		private String r19_other_income;

		@Column(name = "R19_FIG_BAL_SHEET")
		private BigDecimal r19_fig_bal_sheet;

		@Column(name = "R19_FIG_BAL_SHEET_BWP")
		private BigDecimal r19_fig_bal_sheet_bwp;

		@Column(name = "R19_AMT_STATEMENT_ADJ")
		private BigDecimal r19_amt_statement_adj;

		@Column(name = "R19_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r19_amt_statement_adj_bwp;

		@Column(name = "R19_NET_AMT")
		private BigDecimal r19_net_amt;

		@Column(name = "R19_NET_AMT_BWP")
		private BigDecimal r19_net_amt_bwp;

		@Column(name = "R19_BAL_SUB")
		private BigDecimal r19_bal_sub;

		@Column(name = "R19_BAL_SUB_BWP")
		private BigDecimal r19_bal_sub_bwp;

		@Column(name = "R19_BAL_SUB_DIARIES_BWP")
		private BigDecimal r19_bal_sub_diaries_bwp;

		// ================= R20 =================
		@Column(name = "R20_OTHER_INCOME")
		private String r20_other_income;

		@Column(name = "R20_FIG_BAL_SHEET")
		private BigDecimal r20_fig_bal_sheet;

		@Column(name = "R20_FIG_BAL_SHEET_BWP")
		private BigDecimal r20_fig_bal_sheet_bwp;

		@Column(name = "R20_AMT_STATEMENT_ADJ")
		private BigDecimal r20_amt_statement_adj;

		@Column(name = "R20_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r20_amt_statement_adj_bwp;

		@Column(name = "R20_NET_AMT")
		private BigDecimal r20_net_amt;

		@Column(name = "R20_NET_AMT_BWP")
		private BigDecimal r20_net_amt_bwp;

		@Column(name = "R20_BAL_SUB")
		private BigDecimal r20_bal_sub;

		@Column(name = "R20_BAL_SUB_BWP")
		private BigDecimal r20_bal_sub_bwp;

		@Column(name = "R20_BAL_SUB_DIARIES_BWP")
		private BigDecimal r20_bal_sub_diaries_bwp;

		// ================= R21 =================
		@Column(name = "R21_OTHER_INCOME")
		private String r21_other_income;

		@Column(name = "R21_FIG_BAL_SHEET")
		private BigDecimal r21_fig_bal_sheet;

		@Column(name = "R21_FIG_BAL_SHEET_BWP")
		private BigDecimal r21_fig_bal_sheet_bwp;

		@Column(name = "R21_AMT_STATEMENT_ADJ")
		private BigDecimal r21_amt_statement_adj;

		@Column(name = "R21_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r21_amt_statement_adj_bwp;

		@Column(name = "R21_NET_AMT")
		private BigDecimal r21_net_amt;

		@Column(name = "R21_NET_AMT_BWP")
		private BigDecimal r21_net_amt_bwp;

		@Column(name = "R21_BAL_SUB")
		private BigDecimal r21_bal_sub;

		@Column(name = "R21_BAL_SUB_BWP")
		private BigDecimal r21_bal_sub_bwp;

		@Column(name = "R21_BAL_SUB_DIARIES_BWP")
		private BigDecimal r21_bal_sub_diaries_bwp;

		// ================= R22 =================
		@Column(name = "R22_OTHER_INCOME")
		private String r22_other_income;

		@Column(name = "R22_FIG_BAL_SHEET")
		private BigDecimal r22_fig_bal_sheet;

		@Column(name = "R22_FIG_BAL_SHEET_BWP")
		private BigDecimal r22_fig_bal_sheet_bwp;

		@Column(name = "R22_AMT_STATEMENT_ADJ")
		private BigDecimal r22_amt_statement_adj;

		@Column(name = "R22_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r22_amt_statement_adj_bwp;

		@Column(name = "R22_NET_AMT")
		private BigDecimal r22_net_amt;

		@Column(name = "R22_NET_AMT_BWP")
		private BigDecimal r22_net_amt_bwp;

		@Column(name = "R22_BAL_SUB")
		private BigDecimal r22_bal_sub;

		@Column(name = "R22_BAL_SUB_BWP")
		private BigDecimal r22_bal_sub_bwp;

		@Column(name = "R22_BAL_SUB_DIARIES_BWP")
		private BigDecimal r22_bal_sub_diaries_bwp;

		// ================= R23 =================
		@Column(name = "R23_OTHER_INCOME")
		private String r23_other_income;

		@Column(name = "R23_FIG_BAL_SHEET")
		private BigDecimal r23_fig_bal_sheet;

		@Column(name = "R23_FIG_BAL_SHEET_BWP")
		private BigDecimal r23_fig_bal_sheet_bwp;

		@Column(name = "R23_AMT_STATEMENT_ADJ")
		private BigDecimal r23_amt_statement_adj;

		@Column(name = "R23_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r23_amt_statement_adj_bwp;

		@Column(name = "R23_NET_AMT")
		private BigDecimal r23_net_amt;

		@Column(name = "R23_NET_AMT_BWP")
		private BigDecimal r23_net_amt_bwp;

		@Column(name = "R23_BAL_SUB")
		private BigDecimal r23_bal_sub;

		@Column(name = "R23_BAL_SUB_BWP")
		private BigDecimal r23_bal_sub_bwp;

		@Column(name = "R23_BAL_SUB_DIARIES_BWP")
		private BigDecimal r23_bal_sub_diaries_bwp;

		// ================= R24 =================
		@Column(name = "R24_OTHER_INCOME")
		private String r24_other_income;

		@Column(name = "R24_FIG_BAL_SHEET")
		private BigDecimal r24_fig_bal_sheet;

		@Column(name = "R24_FIG_BAL_SHEET_BWP")
		private BigDecimal r24_fig_bal_sheet_bwp;

		@Column(name = "R24_AMT_STATEMENT_ADJ")
		private BigDecimal r24_amt_statement_adj;

		@Column(name = "R24_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r24_amt_statement_adj_bwp;

		@Column(name = "R24_NET_AMT")
		private BigDecimal r24_net_amt;

		@Column(name = "R24_NET_AMT_BWP")
		private BigDecimal r24_net_amt_bwp;

		@Column(name = "R24_BAL_SUB")
		private BigDecimal r24_bal_sub;

		@Column(name = "R24_BAL_SUB_BWP")
		private BigDecimal r24_bal_sub_bwp;

		@Column(name = "R24_BAL_SUB_DIARIES_BWP")
		private BigDecimal r24_bal_sub_diaries_bwp;

		// ================= R25 =================
		@Column(name = "R25_OTHER_INCOME")
		private String r25_other_income;

		@Column(name = "R25_FIG_BAL_SHEET")
		private BigDecimal r25_fig_bal_sheet;

		@Column(name = "R25_FIG_BAL_SHEET_BWP")
		private BigDecimal r25_fig_bal_sheet_bwp;

		@Column(name = "R25_AMT_STATEMENT_ADJ")
		private BigDecimal r25_amt_statement_adj;

		@Column(name = "R25_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r25_amt_statement_adj_bwp;

		@Column(name = "R25_NET_AMT")
		private BigDecimal r25_net_amt;

		@Column(name = "R25_NET_AMT_BWP")
		private BigDecimal r25_net_amt_bwp;

		@Column(name = "R25_BAL_SUB")
		private BigDecimal r25_bal_sub;

		@Column(name = "R25_BAL_SUB_BWP")
		private BigDecimal r25_bal_sub_bwp;

		@Column(name = "R25_BAL_SUB_DIARIES_BWP")
		private BigDecimal r25_bal_sub_diaries_bwp;

		// ================= R26 =================
		@Column(name = "R26_OTHER_INCOME")
		private String r26_other_income;

		@Column(name = "R26_FIG_BAL_SHEET")
		private BigDecimal r26_fig_bal_sheet;

		@Column(name = "R26_FIG_BAL_SHEET_BWP")
		private BigDecimal r26_fig_bal_sheet_bwp;

		@Column(name = "R26_AMT_STATEMENT_ADJ")
		private BigDecimal r26_amt_statement_adj;

		@Column(name = "R26_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r26_amt_statement_adj_bwp;

		@Column(name = "R26_NET_AMT")
		private BigDecimal r26_net_amt;

		@Column(name = "R26_NET_AMT_BWP")
		private BigDecimal r26_net_amt_bwp;

		@Column(name = "R26_BAL_SUB")
		private BigDecimal r26_bal_sub;

		@Column(name = "R26_BAL_SUB_BWP")
		private BigDecimal r26_bal_sub_bwp;

		@Column(name = "R26_BAL_SUB_DIARIES_BWP")
		private BigDecimal r26_bal_sub_diaries_bwp;

		// ================= R27 =================
		@Column(name = "R27_OTHER_INCOME")
		private String r27_other_income;

		@Column(name = "R27_FIG_BAL_SHEET")
		private BigDecimal r27_fig_bal_sheet;

		@Column(name = "R27_FIG_BAL_SHEET_BWP")
		private BigDecimal r27_fig_bal_sheet_bwp;

		@Column(name = "R27_AMT_STATEMENT_ADJ")
		private BigDecimal r27_amt_statement_adj;

		@Column(name = "R27_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r27_amt_statement_adj_bwp;

		@Column(name = "R27_NET_AMT")
		private BigDecimal r27_net_amt;

		@Column(name = "R27_NET_AMT_BWP")
		private BigDecimal r27_net_amt_bwp;

		@Column(name = "R27_BAL_SUB")
		private BigDecimal r27_bal_sub;

		@Column(name = "R27_BAL_SUB_BWP")
		private BigDecimal r27_bal_sub_bwp;

		@Column(name = "R27_BAL_SUB_DIARIES_BWP")
		private BigDecimal r27_bal_sub_diaries_bwp;

		// ================= R28 =================
		@Column(name = "R28_OTHER_INCOME")
		private String r28_other_income;

		@Column(name = "R28_FIG_BAL_SHEET")
		private BigDecimal r28_fig_bal_sheet;

		@Column(name = "R28_FIG_BAL_SHEET_BWP")
		private BigDecimal r28_fig_bal_sheet_bwp;

		@Column(name = "R28_AMT_STATEMENT_ADJ")
		private BigDecimal r28_amt_statement_adj;

		@Column(name = "R28_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r28_amt_statement_adj_bwp;

		@Column(name = "R28_NET_AMT")
		private BigDecimal r28_net_amt;

		@Column(name = "R28_NET_AMT_BWP")
		private BigDecimal r28_net_amt_bwp;

		@Column(name = "R28_BAL_SUB")
		private BigDecimal r28_bal_sub;

		@Column(name = "R28_BAL_SUB_BWP")
		private BigDecimal r28_bal_sub_bwp;

		@Column(name = "R28_BAL_SUB_DIARIES_BWP")
		private BigDecimal r28_bal_sub_diaries_bwp;

		// ================= R29 =================
		@Column(name = "R29_OTHER_INCOME")
		private String r29_other_income;

		@Column(name = "R29_FIG_BAL_SHEET")
		private BigDecimal r29_fig_bal_sheet;

		@Column(name = "R29_FIG_BAL_SHEET_BWP")
		private BigDecimal r29_fig_bal_sheet_bwp;

		@Column(name = "R29_AMT_STATEMENT_ADJ")
		private BigDecimal r29_amt_statement_adj;

		@Column(name = "R29_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r29_amt_statement_adj_bwp;

		@Column(name = "R29_NET_AMT")
		private BigDecimal r29_net_amt;

		@Column(name = "R29_NET_AMT_BWP")
		private BigDecimal r29_net_amt_bwp;

		@Column(name = "R29_BAL_SUB")
		private BigDecimal r29_bal_sub;

		@Column(name = "R29_BAL_SUB_BWP")
		private BigDecimal r29_bal_sub_bwp;

		@Column(name = "R29_BAL_SUB_DIARIES_BWP")
		private BigDecimal r29_bal_sub_diaries_bwp;

		// ================= R30 =================
		@Column(name = "R30_OTHER_INCOME")
		private String r30_other_income;

		@Column(name = "R30_FIG_BAL_SHEET")
		private BigDecimal r30_fig_bal_sheet;

		@Column(name = "R30_FIG_BAL_SHEET_BWP")
		private BigDecimal r30_fig_bal_sheet_bwp;

		@Column(name = "R30_AMT_STATEMENT_ADJ")
		private BigDecimal r30_amt_statement_adj;

		@Column(name = "R30_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r30_amt_statement_adj_bwp;

		@Column(name = "R30_NET_AMT")
		private BigDecimal r30_net_amt;

		@Column(name = "R30_NET_AMT_BWP")
		private BigDecimal r30_net_amt_bwp;

		@Column(name = "R30_BAL_SUB")
		private BigDecimal r30_bal_sub;

		@Column(name = "R30_BAL_SUB_BWP")
		private BigDecimal r30_bal_sub_bwp;

		@Column(name = "R30_BAL_SUB_DIARIES_BWP")
		private BigDecimal r30_bal_sub_diaries_bwp;

		// ================= R31 =================
		@Column(name = "R31_OTHER_INCOME")
		private String r31_other_income;

		@Column(name = "R31_FIG_BAL_SHEET")
		private BigDecimal r31_fig_bal_sheet;

		@Column(name = "R31_FIG_BAL_SHEET_BWP")
		private BigDecimal r31_fig_bal_sheet_bwp;

		@Column(name = "R31_AMT_STATEMENT_ADJ")
		private BigDecimal r31_amt_statement_adj;

		@Column(name = "R31_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r31_amt_statement_adj_bwp;

		@Column(name = "R31_NET_AMT")
		private BigDecimal r31_net_amt;

		@Column(name = "R31_NET_AMT_BWP")
		private BigDecimal r31_net_amt_bwp;

		@Column(name = "R31_BAL_SUB")
		private BigDecimal r31_bal_sub;

		@Column(name = "R31_BAL_SUB_BWP")
		private BigDecimal r31_bal_sub_bwp;

		@Column(name = "R31_BAL_SUB_DIARIES_BWP")
		private BigDecimal r31_bal_sub_diaries_bwp;

		// ================= R40 =================
		@Column(name = "R40_INTREST_EXPENDED")
		private String r40_intrest_expended;

		@Column(name = "R40_FIG_BAL_SHEET")
		private BigDecimal r40_fig_bal_sheet;

		@Column(name = "R40_FIG_BAL_SHEET_BWP")
		private BigDecimal r40_fig_bal_sheet_bwp;

		@Column(name = "R40_AMT_STATEMENT_ADJ")
		private BigDecimal r40_amt_statement_adj;

		@Column(name = "R40_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r40_amt_statement_adj_bwp;

		@Column(name = "R40_NET_AMT")
		private BigDecimal r40_net_amt;

		@Column(name = "R40_NET_AMT_BWP")
		private BigDecimal r40_net_amt_bwp;

		@Column(name = "R40_BAL_SUB")
		private BigDecimal r40_bal_sub;

		@Column(name = "R40_BAL_SUB_BWP")
		private BigDecimal r40_bal_sub_bwp;

		@Column(name = "R40_BAL_SUB_DIARIES_BWP")
		private BigDecimal r40_bal_sub_diaries_bwp;

		// ================= R41 =================
		@Column(name = "R41_INTREST_EXPENDED")
		private String r41_intrest_expended;

		@Column(name = "R41_FIG_BAL_SHEET")
		private BigDecimal r41_fig_bal_sheet;

		@Column(name = "R41_FIG_BAL_SHEET_BWP")
		private BigDecimal r41_fig_bal_sheet_bwp;

		@Column(name = "R41_AMT_STATEMENT_ADJ")
		private BigDecimal r41_amt_statement_adj;

		@Column(name = "R41_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r41_amt_statement_adj_bwp;

		@Column(name = "R41_NET_AMT")
		private BigDecimal r41_net_amt;

		@Column(name = "R41_NET_AMT_BWP")
		private BigDecimal r41_net_amt_bwp;

		@Column(name = "R41_BAL_SUB")
		private BigDecimal r41_bal_sub;

		@Column(name = "R41_BAL_SUB_BWP")
		private BigDecimal r41_bal_sub_bwp;

		@Column(name = "R41_BAL_SUB_DIARIES_BWP")
		private BigDecimal r41_bal_sub_diaries_bwp;

		// ================= R42 =================
		@Column(name = "R42_INTREST_EXPENDED")
		private String r42_intrest_expended;

		@Column(name = "R42_FIG_BAL_SHEET")
		private BigDecimal r42_fig_bal_sheet;

		@Column(name = "R42_FIG_BAL_SHEET_BWP")
		private BigDecimal r42_fig_bal_sheet_bwp;

		@Column(name = "R42_AMT_STATEMENT_ADJ")
		private BigDecimal r42_amt_statement_adj;

		@Column(name = "R42_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r42_amt_statement_adj_bwp;

		@Column(name = "R42_NET_AMT")
		private BigDecimal r42_net_amt;

		@Column(name = "R42_NET_AMT_BWP")
		private BigDecimal r42_net_amt_bwp;

		@Column(name = "R42_BAL_SUB")
		private BigDecimal r42_bal_sub;

		@Column(name = "R42_BAL_SUB_BWP")
		private BigDecimal r42_bal_sub_bwp;

		@Column(name = "R42_BAL_SUB_DIARIES_BWP")
		private BigDecimal r42_bal_sub_diaries_bwp;

		// ================= R43 =================
		@Column(name = "R43_INTREST_EXPENDED")
		private String r43_intrest_expended;

		@Column(name = "R43_FIG_BAL_SHEET")
		private BigDecimal r43_fig_bal_sheet;

		@Column(name = "R43_FIG_BAL_SHEET_BWP")
		private BigDecimal r43_fig_bal_sheet_bwp;

		@Column(name = "R43_AMT_STATEMENT_ADJ")
		private BigDecimal r43_amt_statement_adj;

		@Column(name = "R43_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r43_amt_statement_adj_bwp;

		@Column(name = "R43_NET_AMT")
		private BigDecimal r43_net_amt;

		@Column(name = "R43_NET_AMT_BWP")
		private BigDecimal r43_net_amt_bwp;

		@Column(name = "R43_BAL_SUB")
		private BigDecimal r43_bal_sub;

		@Column(name = "R43_BAL_SUB_BWP")
		private BigDecimal r43_bal_sub_bwp;

		@Column(name = "R43_BAL_SUB_DIARIES_BWP")
		private BigDecimal r43_bal_sub_diaries_bwp;

		// ================= R48 =================
		@Column(name = "R48_OPERATING_EXPENSES")
		private String r48_operating_expenses;

		@Column(name = "R48_FIG_BAL_SHEET")
		private BigDecimal r48_fig_bal_sheet;

		@Column(name = "R48_FIG_BAL_SHEET_BWP")
		private BigDecimal r48_fig_bal_sheet_bwp;

		@Column(name = "R48_AMT_STATEMENT_ADJ")
		private BigDecimal r48_amt_statement_adj;

		@Column(name = "R48_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r48_amt_statement_adj_bwp;

		@Column(name = "R48_NET_AMT")
		private BigDecimal r48_net_amt;

		@Column(name = "R48_NET_AMT_BWP")
		private BigDecimal r48_net_amt_bwp;

		@Column(name = "R48_BAL_SUB")
		private BigDecimal r48_bal_sub;

		@Column(name = "R48_BAL_SUB_BWP")
		private BigDecimal r48_bal_sub_bwp;

		@Column(name = "R48_BAL_SUB_DIARIES_BWP")
		private BigDecimal r48_bal_sub_diaries_bwp;

		// ================= R49 =================
		@Column(name = "R49_OPERATING_EXPENSES")
		private String r49_operating_expenses;

		@Column(name = "R49_FIG_BAL_SHEET")
		private BigDecimal r49_fig_bal_sheet;

		@Column(name = "R49_FIG_BAL_SHEET_BWP")
		private BigDecimal r49_fig_bal_sheet_bwp;

		@Column(name = "R49_AMT_STATEMENT_ADJ")
		private BigDecimal r49_amt_statement_adj;

		@Column(name = "R49_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r49_amt_statement_adj_bwp;

		@Column(name = "R49_NET_AMT")
		private BigDecimal r49_net_amt;

		@Column(name = "R49_NET_AMT_BWP")
		private BigDecimal r49_net_amt_bwp;

		@Column(name = "R49_BAL_SUB")
		private BigDecimal r49_bal_sub;

		@Column(name = "R49_BAL_SUB_BWP")
		private BigDecimal r49_bal_sub_bwp;

		@Column(name = "R49_BAL_SUB_DIARIES_BWP")
		private BigDecimal r49_bal_sub_diaries_bwp;

		// ================= R50 =================
		@Column(name = "R50_OPERATING_EXPENSES")
		private String r50_operating_expenses;

		@Column(name = "R50_FIG_BAL_SHEET")
		private BigDecimal r50_fig_bal_sheet;

		@Column(name = "R50_FIG_BAL_SHEET_BWP")
		private BigDecimal r50_fig_bal_sheet_bwp;

		@Column(name = "R50_AMT_STATEMENT_ADJ")
		private BigDecimal r50_amt_statement_adj;

		@Column(name = "R50_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r50_amt_statement_adj_bwp;

		@Column(name = "R50_NET_AMT")
		private BigDecimal r50_net_amt;

		@Column(name = "R50_NET_AMT_BWP")
		private BigDecimal r50_net_amt_bwp;

		@Column(name = "R50_BAL_SUB")
		private BigDecimal r50_bal_sub;

		@Column(name = "R50_BAL_SUB_BWP")
		private BigDecimal r50_bal_sub_bwp;

		@Column(name = "R50_BAL_SUB_DIARIES_BWP")
		private BigDecimal r50_bal_sub_diaries_bwp;

		// ================= R51 =================
		@Column(name = "R51_OPERATING_EXPENSES")
		private String r51_operating_expenses;

		@Column(name = "R51_FIG_BAL_SHEET")
		private BigDecimal r51_fig_bal_sheet;

		@Column(name = "R51_FIG_BAL_SHEET_BWP")
		private BigDecimal r51_fig_bal_sheet_bwp;

		@Column(name = "R51_AMT_STATEMENT_ADJ")
		private BigDecimal r51_amt_statement_adj;

		@Column(name = "R51_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r51_amt_statement_adj_bwp;

		@Column(name = "R51_NET_AMT")
		private BigDecimal r51_net_amt;

		@Column(name = "R51_NET_AMT_BWP")
		private BigDecimal r51_net_amt_bwp;

		@Column(name = "R51_BAL_SUB")
		private BigDecimal r51_bal_sub;

		@Column(name = "R51_BAL_SUB_BWP")
		private BigDecimal r51_bal_sub_bwp;

		@Column(name = "R51_BAL_SUB_DIARIES_BWP")
		private BigDecimal r51_bal_sub_diaries_bwp;

		@Column(name = "R52_OPERATING_EXPENSES")
		private String r52_operating_expenses;

		@Column(name = "R52_FIG_BAL_SHEET")
		private BigDecimal r52_fig_bal_sheet;

		@Column(name = "R52_FIG_BAL_SHEET_BWP")
		private BigDecimal r52_fig_bal_sheet_bwp;

		@Column(name = "R52_AMT_STATEMENT_ADJ")
		private BigDecimal r52_amt_statement_adj;

		@Column(name = "R52_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r52_amt_statement_adj_bwp;

		@Column(name = "R52_NET_AMT")
		private BigDecimal r52_net_amt;

		@Column(name = "R52_NET_AMT_BWP")
		private BigDecimal r52_net_amt_bwp;

		@Column(name = "R52_BAL_SUB")
		private BigDecimal r52_bal_sub;

		@Column(name = "R52_BAL_SUB_BWP")
		private BigDecimal r52_bal_sub_bwp;

		@Column(name = "R52_BAL_SUB_DIARIES_BWP")
		private BigDecimal r52_bal_sub_diaries_bwp;

		@Column(name = "R53_OPERATING_EXPENSES")
		private String r53_operating_expenses;

		@Column(name = "R53_FIG_BAL_SHEET")
		private BigDecimal r53_fig_bal_sheet;

		@Column(name = "R53_FIG_BAL_SHEET_BWP")
		private BigDecimal r53_fig_bal_sheet_bwp;

		@Column(name = "R53_AMT_STATEMENT_ADJ")
		private BigDecimal r53_amt_statement_adj;

		@Column(name = "R53_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r53_amt_statement_adj_bwp;

		@Column(name = "R53_NET_AMT")
		private BigDecimal r53_net_amt;

		@Column(name = "R53_NET_AMT_BWP")
		private BigDecimal r53_net_amt_bwp;

		@Column(name = "R53_BAL_SUB")
		private BigDecimal r53_bal_sub;

		@Column(name = "R53_BAL_SUB_BWP")
		private BigDecimal r53_bal_sub_bwp;

		@Column(name = "R53_BAL_SUB_DIARIES_BWP")
		private BigDecimal r53_bal_sub_diaries_bwp;

		@Column(name = "R54_OPERATING_EXPENSES")
		private String r54_operating_expenses;

		@Column(name = "R54_FIG_BAL_SHEET")
		private BigDecimal r54_fig_bal_sheet;

		@Column(name = "R54_FIG_BAL_SHEET_BWP")
		private BigDecimal r54_fig_bal_sheet_bwp;

		@Column(name = "R54_AMT_STATEMENT_ADJ")
		private BigDecimal r54_amt_statement_adj;

		@Column(name = "R54_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r54_amt_statement_adj_bwp;

		@Column(name = "R54_NET_AMT")
		private BigDecimal r54_net_amt;

		@Column(name = "R54_NET_AMT_BWP")
		private BigDecimal r54_net_amt_bwp;

		@Column(name = "R54_BAL_SUB")
		private BigDecimal r54_bal_sub;

		@Column(name = "R54_BAL_SUB_BWP")
		private BigDecimal r54_bal_sub_bwp;

		@Column(name = "R54_BAL_SUB_DIARIES_BWP")
		private BigDecimal r54_bal_sub_diaries_bwp;

		@Column(name = "R55_OPERATING_EXPENSES")
		private String r55_operating_expenses;

		@Column(name = "R55_FIG_BAL_SHEET")
		private BigDecimal r55_fig_bal_sheet;

		@Column(name = "R55_FIG_BAL_SHEET_BWP")
		private BigDecimal r55_fig_bal_sheet_bwp;

		@Column(name = "R55_AMT_STATEMENT_ADJ")
		private BigDecimal r55_amt_statement_adj;

		@Column(name = "R55_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r55_amt_statement_adj_bwp;

		@Column(name = "R55_NET_AMT")
		private BigDecimal r55_net_amt;

		@Column(name = "R55_NET_AMT_BWP")
		private BigDecimal r55_net_amt_bwp;

		@Column(name = "R55_BAL_SUB")
		private BigDecimal r55_bal_sub;

		@Column(name = "R55_BAL_SUB_BWP")
		private BigDecimal r55_bal_sub_bwp;

		@Column(name = "R55_BAL_SUB_DIARIES_BWP")
		private BigDecimal r55_bal_sub_diaries_bwp;

		@Column(name = "R56_OPERATING_EXPENSES")
		private String r56_operating_expenses;

		@Column(name = "R56_FIG_BAL_SHEET")
		private BigDecimal r56_fig_bal_sheet;

		@Column(name = "R56_FIG_BAL_SHEET_BWP")
		private BigDecimal r56_fig_bal_sheet_bwp;

		@Column(name = "R56_AMT_STATEMENT_ADJ")
		private BigDecimal r56_amt_statement_adj;

		@Column(name = "R56_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r56_amt_statement_adj_bwp;

		@Column(name = "R56_NET_AMT")
		private BigDecimal r56_net_amt;

		@Column(name = "R56_NET_AMT_BWP")
		private BigDecimal r56_net_amt_bwp;

		@Column(name = "R56_BAL_SUB")
		private BigDecimal r56_bal_sub;

		@Column(name = "R56_BAL_SUB_BWP")
		private BigDecimal r56_bal_sub_bwp;

		@Column(name = "R56_BAL_SUB_DIARIES_BWP")
		private BigDecimal r56_bal_sub_diaries_bwp;

		@Column(name = "R57_OPERATING_EXPENSES")
		private String r57_operating_expenses;

		@Column(name = "R57_FIG_BAL_SHEET")
		private BigDecimal r57_fig_bal_sheet;

		@Column(name = "R57_FIG_BAL_SHEET_BWP")
		private BigDecimal r57_fig_bal_sheet_bwp;

		@Column(name = "R57_AMT_STATEMENT_ADJ")
		private BigDecimal r57_amt_statement_adj;

		@Column(name = "R57_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r57_amt_statement_adj_bwp;

		@Column(name = "R57_NET_AMT")
		private BigDecimal r57_net_amt;

		@Column(name = "R57_NET_AMT_BWP")
		private BigDecimal r57_net_amt_bwp;

		@Column(name = "R57_BAL_SUB")
		private BigDecimal r57_bal_sub;

		@Column(name = "R57_BAL_SUB_BWP")
		private BigDecimal r57_bal_sub_bwp;

		@Column(name = "R57_BAL_SUB_DIARIES_BWP")
		private BigDecimal r57_bal_sub_diaries_bwp;

		@Column(name = "R58_OPERATING_EXPENSES")
		private String r58_operating_expenses;

		@Column(name = "R58_FIG_BAL_SHEET")
		private BigDecimal r58_fig_bal_sheet;

		@Column(name = "R58_FIG_BAL_SHEET_BWP")
		private BigDecimal r58_fig_bal_sheet_bwp;

		@Column(name = "R58_AMT_STATEMENT_ADJ")
		private BigDecimal r58_amt_statement_adj;

		@Column(name = "R58_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r58_amt_statement_adj_bwp;

		@Column(name = "R58_NET_AMT")
		private BigDecimal r58_net_amt;

		@Column(name = "R58_NET_AMT_BWP")
		private BigDecimal r58_net_amt_bwp;

		@Column(name = "R58_BAL_SUB")
		private BigDecimal r58_bal_sub;

		@Column(name = "R58_BAL_SUB_BWP")
		private BigDecimal r58_bal_sub_bwp;

		@Column(name = "R58_BAL_SUB_DIARIES_BWP")
		private BigDecimal r58_bal_sub_diaries_bwp;

		@Column(name = "R59_OPERATING_EXPENSES")
		private String r59_operating_expenses;

		@Column(name = "R59_FIG_BAL_SHEET")
		private BigDecimal r59_fig_bal_sheet;

		@Column(name = "R59_FIG_BAL_SHEET_BWP")
		private BigDecimal r59_fig_bal_sheet_bwp;

		@Column(name = "R59_AMT_STATEMENT_ADJ")
		private BigDecimal r59_amt_statement_adj;

		@Column(name = "R59_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r59_amt_statement_adj_bwp;

		@Column(name = "R59_NET_AMT")
		private BigDecimal r59_net_amt;

		@Column(name = "R59_NET_AMT_BWP")
		private BigDecimal r59_net_amt_bwp;

		@Column(name = "R59_BAL_SUB")
		private BigDecimal r59_bal_sub;

		@Column(name = "R59_BAL_SUB_BWP")
		private BigDecimal r59_bal_sub_bwp;

		@Column(name = "R59_BAL_SUB_DIARIES_BWP")
		private BigDecimal r59_bal_sub_diaries_bwp;

		@Column(name = "R60_OPERATING_EXPENSES")
		private String r60_operating_expenses;

		@Column(name = "R60_FIG_BAL_SHEET")
		private BigDecimal r60_fig_bal_sheet;

		@Column(name = "R60_FIG_BAL_SHEET_BWP")
		private BigDecimal r60_fig_bal_sheet_bwp;

		@Column(name = "R60_AMT_STATEMENT_ADJ")
		private BigDecimal r60_amt_statement_adj;

		@Column(name = "R60_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r60_amt_statement_adj_bwp;

		@Column(name = "R60_NET_AMT")
		private BigDecimal r60_net_amt;

		@Column(name = "R60_NET_AMT_BWP")
		private BigDecimal r60_net_amt_bwp;

		@Column(name = "R60_BAL_SUB")
		private BigDecimal r60_bal_sub;

		@Column(name = "R60_BAL_SUB_BWP")
		private BigDecimal r60_bal_sub_bwp;

		@Column(name = "R60_BAL_SUB_DIARIES_BWP")
		private BigDecimal r60_bal_sub_diaries_bwp;

		@Column(name = "R61_OPERATING_EXPENSES")
		private String r61_operating_expenses;

		@Column(name = "R61_FIG_BAL_SHEET")
		private BigDecimal r61_fig_bal_sheet;

		@Column(name = "R61_FIG_BAL_SHEET_BWP")
		private BigDecimal r61_fig_bal_sheet_bwp;

		@Column(name = "R61_AMT_STATEMENT_ADJ")
		private BigDecimal r61_amt_statement_adj;

		@Column(name = "R61_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r61_amt_statement_adj_bwp;

		@Column(name = "R61_NET_AMT")
		private BigDecimal r61_net_amt;

		@Column(name = "R61_NET_AMT_BWP")
		private BigDecimal r61_net_amt_bwp;

		@Column(name = "R61_BAL_SUB")
		private BigDecimal r61_bal_sub;

		@Column(name = "R61_BAL_SUB_BWP")
		private BigDecimal r61_bal_sub_bwp;

		@Column(name = "R61_BAL_SUB_DIARIES_BWP")
		private BigDecimal r61_bal_sub_diaries_bwp;

		@Column(name = "R62_OPERATING_EXPENSES")
		private String r62_operating_expenses;

		@Column(name = "R62_FIG_BAL_SHEET")
		private BigDecimal r62_fig_bal_sheet;

		@Column(name = "R62_FIG_BAL_SHEET_BWP")
		private BigDecimal r62_fig_bal_sheet_bwp;

		@Column(name = "R62_AMT_STATEMENT_ADJ")
		private BigDecimal r62_amt_statement_adj;

		@Column(name = "R62_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r62_amt_statement_adj_bwp;

		@Column(name = "R62_NET_AMT")
		private BigDecimal r62_net_amt;

		@Column(name = "R62_NET_AMT_BWP")
		private BigDecimal r62_net_amt_bwp;

		@Column(name = "R62_BAL_SUB")
		private BigDecimal r62_bal_sub;

		@Column(name = "R62_BAL_SUB_BWP")
		private BigDecimal r62_bal_sub_bwp;

		@Column(name = "R62_BAL_SUB_DIARIES_BWP")
		private BigDecimal r62_bal_sub_diaries_bwp;

		@Column(name = "R63_OPERATING_EXPENSES")
		private String r63_operating_expenses;

		@Column(name = "R63_FIG_BAL_SHEET")
		private BigDecimal r63_fig_bal_sheet;

		@Column(name = "R63_FIG_BAL_SHEET_BWP")
		private BigDecimal r63_fig_bal_sheet_bwp;

		@Column(name = "R63_AMT_STATEMENT_ADJ")
		private BigDecimal r63_amt_statement_adj;

		@Column(name = "R63_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r63_amt_statement_adj_bwp;

		@Column(name = "R63_NET_AMT")
		private BigDecimal r63_net_amt;

		@Column(name = "R63_NET_AMT_BWP")
		private BigDecimal r63_net_amt_bwp;

		@Column(name = "R63_BAL_SUB")
		private BigDecimal r63_bal_sub;

		@Column(name = "R63_BAL_SUB_BWP")
		private BigDecimal r63_bal_sub_bwp;

		@Column(name = "R63_BAL_SUB_DIARIES_BWP")
		private BigDecimal r63_bal_sub_diaries_bwp;

		@Column(name = "R18_BAL_SUB_DIARIES")
		private BigDecimal r18_bal_sub_diaries;

		@Column(name = "R19_BAL_SUB_DIARIES")
		private BigDecimal r19_bal_sub_diaries;

		@Column(name = "R20_BAL_SUB_DIARIES")
		private BigDecimal r20_bal_sub_diaries;

		@Column(name = "R21_BAL_SUB_DIARIES")
		private BigDecimal r21_bal_sub_diaries;

		@Column(name = "R22_BAL_SUB_DIARIES")
		private BigDecimal r22_bal_sub_diaries;

		@Column(name = "R23_BAL_SUB_DIARIES")
		private BigDecimal r23_bal_sub_diaries;

		@Column(name = "R24_BAL_SUB_DIARIES")
		private BigDecimal r24_bal_sub_diaries;

		@Column(name = "R25_BAL_SUB_DIARIES")
		private BigDecimal r25_bal_sub_diaries;

		@Column(name = "R26_BAL_SUB_DIARIES")
		private BigDecimal r26_bal_sub_diaries;

		@Column(name = "R27_BAL_SUB_DIARIES")
		private BigDecimal r27_bal_sub_diaries;

		@Column(name = "R28_BAL_SUB_DIARIES")
		private BigDecimal r28_bal_sub_diaries;

		@Column(name = "R29_BAL_SUB_DIARIES")
		private BigDecimal r29_bal_sub_diaries;

		@Column(name = "R30_BAL_SUB_DIARIES")
		private BigDecimal r30_bal_sub_diaries;

		@Column(name = "R31_BAL_SUB_DIARIES")
		private BigDecimal r31_bal_sub_diaries;

		@Column(name = "R40_BAL_SUB_DIARIES")
		private BigDecimal r40_bal_sub_diaries;

		@Column(name = "R41_BAL_SUB_DIARIES")
		private BigDecimal r41_bal_sub_diaries;

		@Column(name = "R42_BAL_SUB_DIARIES")
		private BigDecimal r42_bal_sub_diaries;

		@Column(name = "R43_BAL_SUB_DIARIES")
		private BigDecimal r43_bal_sub_diaries;

		@Column(name = "R48_BAL_SUB_DIARIES")
		private BigDecimal r48_bal_sub_diaries;

		@Column(name = "R49_BAL_SUB_DIARIES")
		private BigDecimal r49_bal_sub_diaries;

		@Column(name = "R50_BAL_SUB_DIARIES")
		private BigDecimal r50_bal_sub_diaries;

		@Column(name = "R51_BAL_SUB_DIARIES")
		private BigDecimal r51_bal_sub_diaries;

		@Column(name = "R52_BAL_SUB_DIARIES")
		private BigDecimal r52_bal_sub_diaries;

		@Column(name = "R53_BAL_SUB_DIARIES")
		private BigDecimal r53_bal_sub_diaries;

		@Column(name = "R54_BAL_SUB_DIARIES")
		private BigDecimal r54_bal_sub_diaries;

		@Column(name = "R55_BAL_SUB_DIARIES")
		private BigDecimal r55_bal_sub_diaries;

		@Column(name = "R56_BAL_SUB_DIARIES")
		private BigDecimal r56_bal_sub_diaries;

		@Column(name = "R57_BAL_SUB_DIARIES")
		private BigDecimal r57_bal_sub_diaries;

		@Column(name = "R58_BAL_SUB_DIARIES")
		private BigDecimal r58_bal_sub_diaries;

		@Column(name = "R59_BAL_SUB_DIARIES")
		private BigDecimal r59_bal_sub_diaries;

		@Column(name = "R60_BAL_SUB_DIARIES")
		private BigDecimal r60_bal_sub_diaries;

		@Column(name = "R61_BAL_SUB_DIARIES")
		private BigDecimal r61_bal_sub_diaries;

		@Column(name = "R62_BAL_SUB_DIARIES")
		private BigDecimal r62_bal_sub_diaries;

		@Column(name = "R63_BAL_SUB_DIARIES")
		private BigDecimal r63_bal_sub_diaries;

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

		public BigDecimal getR18_bal_sub_diaries() {
			return r18_bal_sub_diaries;
		}

		public void setR18_bal_sub_diaries(BigDecimal r18_bal_sub_diaries) {
			this.r18_bal_sub_diaries = r18_bal_sub_diaries;
		}

		public BigDecimal getR19_bal_sub_diaries() {
			return r19_bal_sub_diaries;
		}

		public void setR19_bal_sub_diaries(BigDecimal r19_bal_sub_diaries) {
			this.r19_bal_sub_diaries = r19_bal_sub_diaries;
		}

		public BigDecimal getR20_bal_sub_diaries() {
			return r20_bal_sub_diaries;
		}

		public void setR20_bal_sub_diaries(BigDecimal r20_bal_sub_diaries) {
			this.r20_bal_sub_diaries = r20_bal_sub_diaries;
		}

		public BigDecimal getR21_bal_sub_diaries() {
			return r21_bal_sub_diaries;
		}

		public void setR21_bal_sub_diaries(BigDecimal r21_bal_sub_diaries) {
			this.r21_bal_sub_diaries = r21_bal_sub_diaries;
		}

		public BigDecimal getR22_bal_sub_diaries() {
			return r22_bal_sub_diaries;
		}

		public void setR22_bal_sub_diaries(BigDecimal r22_bal_sub_diaries) {
			this.r22_bal_sub_diaries = r22_bal_sub_diaries;
		}

		public BigDecimal getR23_bal_sub_diaries() {
			return r23_bal_sub_diaries;
		}

		public void setR23_bal_sub_diaries(BigDecimal r23_bal_sub_diaries) {
			this.r23_bal_sub_diaries = r23_bal_sub_diaries;
		}

		public BigDecimal getR24_bal_sub_diaries() {
			return r24_bal_sub_diaries;
		}

		public void setR24_bal_sub_diaries(BigDecimal r24_bal_sub_diaries) {
			this.r24_bal_sub_diaries = r24_bal_sub_diaries;
		}

		public BigDecimal getR25_bal_sub_diaries() {
			return r25_bal_sub_diaries;
		}

		public void setR25_bal_sub_diaries(BigDecimal r25_bal_sub_diaries) {
			this.r25_bal_sub_diaries = r25_bal_sub_diaries;
		}

		public BigDecimal getR26_bal_sub_diaries() {
			return r26_bal_sub_diaries;
		}

		public void setR26_bal_sub_diaries(BigDecimal r26_bal_sub_diaries) {
			this.r26_bal_sub_diaries = r26_bal_sub_diaries;
		}

		public BigDecimal getR27_bal_sub_diaries() {
			return r27_bal_sub_diaries;
		}

		public void setR27_bal_sub_diaries(BigDecimal r27_bal_sub_diaries) {
			this.r27_bal_sub_diaries = r27_bal_sub_diaries;
		}

		public BigDecimal getR28_bal_sub_diaries() {
			return r28_bal_sub_diaries;
		}

		public void setR28_bal_sub_diaries(BigDecimal r28_bal_sub_diaries) {
			this.r28_bal_sub_diaries = r28_bal_sub_diaries;
		}

		public BigDecimal getR29_bal_sub_diaries() {
			return r29_bal_sub_diaries;
		}

		public void setR29_bal_sub_diaries(BigDecimal r29_bal_sub_diaries) {
			this.r29_bal_sub_diaries = r29_bal_sub_diaries;
		}

		public BigDecimal getR30_bal_sub_diaries() {
			return r30_bal_sub_diaries;
		}

		public void setR30_bal_sub_diaries(BigDecimal r30_bal_sub_diaries) {
			this.r30_bal_sub_diaries = r30_bal_sub_diaries;
		}

		public BigDecimal getR31_bal_sub_diaries() {
			return r31_bal_sub_diaries;
		}

		public void setR31_bal_sub_diaries(BigDecimal r31_bal_sub_diaries) {
			this.r31_bal_sub_diaries = r31_bal_sub_diaries;
		}

		public BigDecimal getR40_bal_sub_diaries() {
			return r40_bal_sub_diaries;
		}

		public void setR40_bal_sub_diaries(BigDecimal r40_bal_sub_diaries) {
			this.r40_bal_sub_diaries = r40_bal_sub_diaries;
		}

		public BigDecimal getR41_bal_sub_diaries() {
			return r41_bal_sub_diaries;
		}

		public void setR41_bal_sub_diaries(BigDecimal r41_bal_sub_diaries) {
			this.r41_bal_sub_diaries = r41_bal_sub_diaries;
		}

		public BigDecimal getR42_bal_sub_diaries() {
			return r42_bal_sub_diaries;
		}

		public void setR42_bal_sub_diaries(BigDecimal r42_bal_sub_diaries) {
			this.r42_bal_sub_diaries = r42_bal_sub_diaries;
		}

		public BigDecimal getR43_bal_sub_diaries() {
			return r43_bal_sub_diaries;
		}

		public void setR43_bal_sub_diaries(BigDecimal r43_bal_sub_diaries) {
			this.r43_bal_sub_diaries = r43_bal_sub_diaries;
		}

		public BigDecimal getR49_bal_sub_diaries() {
			return r49_bal_sub_diaries;
		}

		public void setR49_bal_sub_diaries(BigDecimal r49_bal_sub_diaries) {
			this.r49_bal_sub_diaries = r49_bal_sub_diaries;
		}

		public BigDecimal getR50_bal_sub_diaries() {
			return r50_bal_sub_diaries;
		}

		public void setR50_bal_sub_diaries(BigDecimal r50_bal_sub_diaries) {
			this.r50_bal_sub_diaries = r50_bal_sub_diaries;
		}

		public BigDecimal getR51_bal_sub_diaries() {
			return r51_bal_sub_diaries;
		}

		public void setR51_bal_sub_diaries(BigDecimal r51_bal_sub_diaries) {
			this.r51_bal_sub_diaries = r51_bal_sub_diaries;
		}

		public BigDecimal getR52_bal_sub_diaries() {
			return r52_bal_sub_diaries;
		}

		public void setR52_bal_sub_diaries(BigDecimal r52_bal_sub_diaries) {
			this.r52_bal_sub_diaries = r52_bal_sub_diaries;
		}

		public BigDecimal getR53_bal_sub_diaries() {
			return r53_bal_sub_diaries;
		}

		public void setR53_bal_sub_diaries(BigDecimal r53_bal_sub_diaries) {
			this.r53_bal_sub_diaries = r53_bal_sub_diaries;
		}

		public BigDecimal getR54_bal_sub_diaries() {
			return r54_bal_sub_diaries;
		}

		public void setR54_bal_sub_diaries(BigDecimal r54_bal_sub_diaries) {
			this.r54_bal_sub_diaries = r54_bal_sub_diaries;
		}

		public BigDecimal getR55_bal_sub_diaries() {
			return r55_bal_sub_diaries;
		}

		public void setR55_bal_sub_diaries(BigDecimal r55_bal_sub_diaries) {
			this.r55_bal_sub_diaries = r55_bal_sub_diaries;
		}

		public BigDecimal getR56_bal_sub_diaries() {
			return r56_bal_sub_diaries;
		}

		public void setR56_bal_sub_diaries(BigDecimal r56_bal_sub_diaries) {
			this.r56_bal_sub_diaries = r56_bal_sub_diaries;
		}

		public BigDecimal getR57_bal_sub_diaries() {
			return r57_bal_sub_diaries;
		}

		public void setR57_bal_sub_diaries(BigDecimal r57_bal_sub_diaries) {
			this.r57_bal_sub_diaries = r57_bal_sub_diaries;
		}

		public BigDecimal getR58_bal_sub_diaries() {
			return r58_bal_sub_diaries;
		}

		public void setR58_bal_sub_diaries(BigDecimal r58_bal_sub_diaries) {
			this.r58_bal_sub_diaries = r58_bal_sub_diaries;
		}

		public BigDecimal getR59_bal_sub_diaries() {
			return r59_bal_sub_diaries;
		}

		public void setR59_bal_sub_diaries(BigDecimal r59_bal_sub_diaries) {
			this.r59_bal_sub_diaries = r59_bal_sub_diaries;
		}

		public BigDecimal getR60_bal_sub_diaries() {
			return r60_bal_sub_diaries;
		}

		public void setR60_bal_sub_diaries(BigDecimal r60_bal_sub_diaries) {
			this.r60_bal_sub_diaries = r60_bal_sub_diaries;
		}

		public BigDecimal getR61_bal_sub_diaries() {
			return r61_bal_sub_diaries;
		}

		public void setR61_bal_sub_diaries(BigDecimal r61_bal_sub_diaries) {
			this.r61_bal_sub_diaries = r61_bal_sub_diaries;
		}

		public BigDecimal getR62_bal_sub_diaries() {
			return r62_bal_sub_diaries;
		}

		public void setR62_bal_sub_diaries(BigDecimal r62_bal_sub_diaries) {
			this.r62_bal_sub_diaries = r62_bal_sub_diaries;
		}

		public BigDecimal getR63_bal_sub_diaries() {
			return r63_bal_sub_diaries;
		}

		public void setR63_bal_sub_diaries(BigDecimal r63_bal_sub_diaries) {
			this.r63_bal_sub_diaries = r63_bal_sub_diaries;
		}

		public String getR9_intrest_div() {
			return r9_intrest_div;
		}

		public void setR9_intrest_div(String r9_intrest_div) {
			this.r9_intrest_div = r9_intrest_div;
		}

		public BigDecimal getR9_fig_bal_sheet() {
			return r9_fig_bal_sheet;
		}

		public void setR9_fig_bal_sheet(BigDecimal r9_fig_bal_sheet) {
			this.r9_fig_bal_sheet = r9_fig_bal_sheet;
		}

		public BigDecimal getR9_fig_bal_sheet_bwp() {
			return r9_fig_bal_sheet_bwp;
		}

		public void setR9_fig_bal_sheet_bwp(BigDecimal r9_fig_bal_sheet_bwp) {
			this.r9_fig_bal_sheet_bwp = r9_fig_bal_sheet_bwp;
		}

		public BigDecimal getR9_amt_statement_adj() {
			return r9_amt_statement_adj;
		}

		public void setR9_amt_statement_adj(BigDecimal r9_amt_statement_adj) {
			this.r9_amt_statement_adj = r9_amt_statement_adj;
		}

		public BigDecimal getR9_amt_statement_adj_bwp() {
			return r9_amt_statement_adj_bwp;
		}

		public void setR9_amt_statement_adj_bwp(BigDecimal r9_amt_statement_adj_bwp) {
			this.r9_amt_statement_adj_bwp = r9_amt_statement_adj_bwp;
		}

		public BigDecimal getR9_net_amt() {
			return r9_net_amt;
		}

		public void setR9_net_amt(BigDecimal r9_net_amt) {
			this.r9_net_amt = r9_net_amt;
		}

		public BigDecimal getR9_net_amt_bwp() {
			return r9_net_amt_bwp;
		}

		public void setR9_net_amt_bwp(BigDecimal r9_net_amt_bwp) {
			this.r9_net_amt_bwp = r9_net_amt_bwp;
		}

		public BigDecimal getR9_bal_sub() {
			return r9_bal_sub;
		}

		public void setR9_bal_sub(BigDecimal r9_bal_sub) {
			this.r9_bal_sub = r9_bal_sub;
		}

		public BigDecimal getR9_bal_sub_bwp() {
			return r9_bal_sub_bwp;
		}

		public void setR9_bal_sub_bwp(BigDecimal r9_bal_sub_bwp) {
			this.r9_bal_sub_bwp = r9_bal_sub_bwp;
		}

		public BigDecimal getR9_bal_sub_diaries() {
			return r9_bal_sub_diaries;
		}

		public void setR9_bal_sub_diaries(BigDecimal r9_bal_sub_diaries) {
			this.r9_bal_sub_diaries = r9_bal_sub_diaries;
		}

		public BigDecimal getR9_bal_sub_diaries_bwp() {
			return r9_bal_sub_diaries_bwp;
		}

		public void setR9_bal_sub_diaries_bwp(BigDecimal r9_bal_sub_diaries_bwp) {
			this.r9_bal_sub_diaries_bwp = r9_bal_sub_diaries_bwp;
		}

		public String getR10_intrest_div() {
			return r10_intrest_div;
		}

		public void setR10_intrest_div(String r10_intrest_div) {
			this.r10_intrest_div = r10_intrest_div;
		}

		public BigDecimal getR10_fig_bal_sheet() {
			return r10_fig_bal_sheet;
		}

		public void setR10_fig_bal_sheet(BigDecimal r10_fig_bal_sheet) {
			this.r10_fig_bal_sheet = r10_fig_bal_sheet;
		}

		public BigDecimal getR10_fig_bal_sheet_bwp() {
			return r10_fig_bal_sheet_bwp;
		}

		public void setR10_fig_bal_sheet_bwp(BigDecimal r10_fig_bal_sheet_bwp) {
			this.r10_fig_bal_sheet_bwp = r10_fig_bal_sheet_bwp;
		}

		public BigDecimal getR10_amt_statement_adj() {
			return r10_amt_statement_adj;
		}

		public void setR10_amt_statement_adj(BigDecimal r10_amt_statement_adj) {
			this.r10_amt_statement_adj = r10_amt_statement_adj;
		}

		public BigDecimal getR10_amt_statement_adj_bwp() {
			return r10_amt_statement_adj_bwp;
		}

		public void setR10_amt_statement_adj_bwp(BigDecimal r10_amt_statement_adj_bwp) {
			this.r10_amt_statement_adj_bwp = r10_amt_statement_adj_bwp;
		}

		public BigDecimal getR10_net_amt() {
			return r10_net_amt;
		}

		public void setR10_net_amt(BigDecimal r10_net_amt) {
			this.r10_net_amt = r10_net_amt;
		}

		public BigDecimal getR10_net_amt_bwp() {
			return r10_net_amt_bwp;
		}

		public void setR10_net_amt_bwp(BigDecimal r10_net_amt_bwp) {
			this.r10_net_amt_bwp = r10_net_amt_bwp;
		}

		public BigDecimal getR10_bal_sub() {
			return r10_bal_sub;
		}

		public void setR10_bal_sub(BigDecimal r10_bal_sub) {
			this.r10_bal_sub = r10_bal_sub;
		}

		public BigDecimal getR10_bal_sub_bwp() {
			return r10_bal_sub_bwp;
		}

		public void setR10_bal_sub_bwp(BigDecimal r10_bal_sub_bwp) {
			this.r10_bal_sub_bwp = r10_bal_sub_bwp;
		}

		public BigDecimal getR10_bal_sub_diaries() {
			return r10_bal_sub_diaries;
		}

		public void setR10_bal_sub_diaries(BigDecimal r10_bal_sub_diaries) {
			this.r10_bal_sub_diaries = r10_bal_sub_diaries;
		}

		public BigDecimal getR10_bal_sub_diaries_bwp() {
			return r10_bal_sub_diaries_bwp;
		}

		public void setR10_bal_sub_diaries_bwp(BigDecimal r10_bal_sub_diaries_bwp) {
			this.r10_bal_sub_diaries_bwp = r10_bal_sub_diaries_bwp;
		}

		public String getR11_intrest_div() {
			return r11_intrest_div;
		}

		public void setR11_intrest_div(String r11_intrest_div) {
			this.r11_intrest_div = r11_intrest_div;
		}

		public BigDecimal getR11_fig_bal_sheet() {
			return r11_fig_bal_sheet;
		}

		public void setR11_fig_bal_sheet(BigDecimal r11_fig_bal_sheet) {
			this.r11_fig_bal_sheet = r11_fig_bal_sheet;
		}

		public BigDecimal getR11_fig_bal_sheet_bwp() {
			return r11_fig_bal_sheet_bwp;
		}

		public void setR11_fig_bal_sheet_bwp(BigDecimal r11_fig_bal_sheet_bwp) {
			this.r11_fig_bal_sheet_bwp = r11_fig_bal_sheet_bwp;
		}

		public BigDecimal getR11_amt_statement_adj() {
			return r11_amt_statement_adj;
		}

		public void setR11_amt_statement_adj(BigDecimal r11_amt_statement_adj) {
			this.r11_amt_statement_adj = r11_amt_statement_adj;
		}

		public BigDecimal getR11_amt_statement_adj_bwp() {
			return r11_amt_statement_adj_bwp;
		}

		public void setR11_amt_statement_adj_bwp(BigDecimal r11_amt_statement_adj_bwp) {
			this.r11_amt_statement_adj_bwp = r11_amt_statement_adj_bwp;
		}

		public BigDecimal getR11_net_amt() {
			return r11_net_amt;
		}

		public void setR11_net_amt(BigDecimal r11_net_amt) {
			this.r11_net_amt = r11_net_amt;
		}

		public BigDecimal getR11_net_amt_bwp() {
			return r11_net_amt_bwp;
		}

		public void setR11_net_amt_bwp(BigDecimal r11_net_amt_bwp) {
			this.r11_net_amt_bwp = r11_net_amt_bwp;
		}

		public BigDecimal getR11_bal_sub() {
			return r11_bal_sub;
		}

		public void setR11_bal_sub(BigDecimal r11_bal_sub) {
			this.r11_bal_sub = r11_bal_sub;
		}

		public BigDecimal getR11_bal_sub_bwp() {
			return r11_bal_sub_bwp;
		}

		public void setR11_bal_sub_bwp(BigDecimal r11_bal_sub_bwp) {
			this.r11_bal_sub_bwp = r11_bal_sub_bwp;
		}

		public BigDecimal getR11_bal_sub_diaries() {
			return r11_bal_sub_diaries;
		}

		public void setR11_bal_sub_diaries(BigDecimal r11_bal_sub_diaries) {
			this.r11_bal_sub_diaries = r11_bal_sub_diaries;
		}

		public BigDecimal getR11_bal_sub_diaries_bwp() {
			return r11_bal_sub_diaries_bwp;
		}

		public void setR11_bal_sub_diaries_bwp(BigDecimal r11_bal_sub_diaries_bwp) {
			this.r11_bal_sub_diaries_bwp = r11_bal_sub_diaries_bwp;
		}

		public String getR12_intrest_div() {
			return r12_intrest_div;
		}

		public void setR12_intrest_div(String r12_intrest_div) {
			this.r12_intrest_div = r12_intrest_div;
		}

		public BigDecimal getR12_fig_bal_sheet() {
			return r12_fig_bal_sheet;
		}

		public void setR12_fig_bal_sheet(BigDecimal r12_fig_bal_sheet) {
			this.r12_fig_bal_sheet = r12_fig_bal_sheet;
		}

		public BigDecimal getR12_fig_bal_sheet_bwp() {
			return r12_fig_bal_sheet_bwp;
		}

		public void setR12_fig_bal_sheet_bwp(BigDecimal r12_fig_bal_sheet_bwp) {
			this.r12_fig_bal_sheet_bwp = r12_fig_bal_sheet_bwp;
		}

		public BigDecimal getR12_amt_statement_adj() {
			return r12_amt_statement_adj;
		}

		public void setR12_amt_statement_adj(BigDecimal r12_amt_statement_adj) {
			this.r12_amt_statement_adj = r12_amt_statement_adj;
		}

		public BigDecimal getR12_amt_statement_adj_bwp() {
			return r12_amt_statement_adj_bwp;
		}

		public void setR12_amt_statement_adj_bwp(BigDecimal r12_amt_statement_adj_bwp) {
			this.r12_amt_statement_adj_bwp = r12_amt_statement_adj_bwp;
		}

		public BigDecimal getR12_net_amt() {
			return r12_net_amt;
		}

		public void setR12_net_amt(BigDecimal r12_net_amt) {
			this.r12_net_amt = r12_net_amt;
		}

		public BigDecimal getR12_net_amt_bwp() {
			return r12_net_amt_bwp;
		}

		public void setR12_net_amt_bwp(BigDecimal r12_net_amt_bwp) {
			this.r12_net_amt_bwp = r12_net_amt_bwp;
		}

		public BigDecimal getR12_bal_sub() {
			return r12_bal_sub;
		}

		public void setR12_bal_sub(BigDecimal r12_bal_sub) {
			this.r12_bal_sub = r12_bal_sub;
		}

		public BigDecimal getR12_bal_sub_bwp() {
			return r12_bal_sub_bwp;
		}

		public void setR12_bal_sub_bwp(BigDecimal r12_bal_sub_bwp) {
			this.r12_bal_sub_bwp = r12_bal_sub_bwp;
		}

		public BigDecimal getR12_bal_sub_diaries() {
			return r12_bal_sub_diaries;
		}

		public void setR12_bal_sub_diaries(BigDecimal r12_bal_sub_diaries) {
			this.r12_bal_sub_diaries = r12_bal_sub_diaries;
		}

		public BigDecimal getR12_bal_sub_diaries_bwp() {
			return r12_bal_sub_diaries_bwp;
		}

		public void setR12_bal_sub_diaries_bwp(BigDecimal r12_bal_sub_diaries_bwp) {
			this.r12_bal_sub_diaries_bwp = r12_bal_sub_diaries_bwp;
		}

		public String getR13_intrest_div() {
			return r13_intrest_div;
		}

		public void setR13_intrest_div(String r13_intrest_div) {
			this.r13_intrest_div = r13_intrest_div;
		}

		public BigDecimal getR13_fig_bal_sheet() {
			return r13_fig_bal_sheet;
		}

		public void setR13_fig_bal_sheet(BigDecimal r13_fig_bal_sheet) {
			this.r13_fig_bal_sheet = r13_fig_bal_sheet;
		}

		public BigDecimal getR13_fig_bal_sheet_bwp() {
			return r13_fig_bal_sheet_bwp;
		}

		public void setR13_fig_bal_sheet_bwp(BigDecimal r13_fig_bal_sheet_bwp) {
			this.r13_fig_bal_sheet_bwp = r13_fig_bal_sheet_bwp;
		}

		public BigDecimal getR13_amt_statement_adj() {
			return r13_amt_statement_adj;
		}

		public void setR13_amt_statement_adj(BigDecimal r13_amt_statement_adj) {
			this.r13_amt_statement_adj = r13_amt_statement_adj;
		}

		public BigDecimal getR13_amt_statement_adj_bwp() {
			return r13_amt_statement_adj_bwp;
		}

		public void setR13_amt_statement_adj_bwp(BigDecimal r13_amt_statement_adj_bwp) {
			this.r13_amt_statement_adj_bwp = r13_amt_statement_adj_bwp;
		}

		public BigDecimal getR13_net_amt() {
			return r13_net_amt;
		}

		public void setR13_net_amt(BigDecimal r13_net_amt) {
			this.r13_net_amt = r13_net_amt;
		}

		public BigDecimal getR13_net_amt_bwp() {
			return r13_net_amt_bwp;
		}

		public void setR13_net_amt_bwp(BigDecimal r13_net_amt_bwp) {
			this.r13_net_amt_bwp = r13_net_amt_bwp;
		}

		public BigDecimal getR13_bal_sub() {
			return r13_bal_sub;
		}

		public void setR13_bal_sub(BigDecimal r13_bal_sub) {
			this.r13_bal_sub = r13_bal_sub;
		}

		public BigDecimal getR13_bal_sub_bwp() {
			return r13_bal_sub_bwp;
		}

		public void setR13_bal_sub_bwp(BigDecimal r13_bal_sub_bwp) {
			this.r13_bal_sub_bwp = r13_bal_sub_bwp;
		}

		public BigDecimal getR13_bal_sub_diaries() {
			return r13_bal_sub_diaries;
		}

		public void setR13_bal_sub_diaries(BigDecimal r13_bal_sub_diaries) {
			this.r13_bal_sub_diaries = r13_bal_sub_diaries;
		}

		public BigDecimal getR13_bal_sub_diaries_bwp() {
			return r13_bal_sub_diaries_bwp;
		}

		public void setR13_bal_sub_diaries_bwp(BigDecimal r13_bal_sub_diaries_bwp) {
			this.r13_bal_sub_diaries_bwp = r13_bal_sub_diaries_bwp;
		}

		public String getR17_other_income() {
			return r17_other_income;
		}

		public void setR17_other_income(String r17_other_income) {
			this.r17_other_income = r17_other_income;
		}

		public BigDecimal getR17_fig_bal_sheet() {
			return r17_fig_bal_sheet;
		}

		public void setR17_fig_bal_sheet(BigDecimal r17_fig_bal_sheet) {
			this.r17_fig_bal_sheet = r17_fig_bal_sheet;
		}

		public BigDecimal getR17_fig_bal_sheet_bwp() {
			return r17_fig_bal_sheet_bwp;
		}

		public void setR17_fig_bal_sheet_bwp(BigDecimal r17_fig_bal_sheet_bwp) {
			this.r17_fig_bal_sheet_bwp = r17_fig_bal_sheet_bwp;
		}

		public BigDecimal getR17_amt_statement_adj() {
			return r17_amt_statement_adj;
		}

		public void setR17_amt_statement_adj(BigDecimal r17_amt_statement_adj) {
			this.r17_amt_statement_adj = r17_amt_statement_adj;
		}

		public BigDecimal getR17_amt_statement_adj_bwp() {
			return r17_amt_statement_adj_bwp;
		}

		public void setR17_amt_statement_adj_bwp(BigDecimal r17_amt_statement_adj_bwp) {
			this.r17_amt_statement_adj_bwp = r17_amt_statement_adj_bwp;
		}

		public BigDecimal getR17_net_amt() {
			return r17_net_amt;
		}

		public void setR17_net_amt(BigDecimal r17_net_amt) {
			this.r17_net_amt = r17_net_amt;
		}

		public BigDecimal getR17_net_amt_bwp() {
			return r17_net_amt_bwp;
		}

		public void setR17_net_amt_bwp(BigDecimal r17_net_amt_bwp) {
			this.r17_net_amt_bwp = r17_net_amt_bwp;
		}

		public BigDecimal getR17_bal_sub() {
			return r17_bal_sub;
		}

		public void setR17_bal_sub(BigDecimal r17_bal_sub) {
			this.r17_bal_sub = r17_bal_sub;
		}

		public BigDecimal getR17_bal_sub_bwp() {
			return r17_bal_sub_bwp;
		}

		public void setR17_bal_sub_bwp(BigDecimal r17_bal_sub_bwp) {
			this.r17_bal_sub_bwp = r17_bal_sub_bwp;
		}

		public BigDecimal getR17_bal_sub_diaries_bwp() {
			return r17_bal_sub_diaries_bwp;
		}

		public void setR17_bal_sub_diaries_bwp(BigDecimal r17_bal_sub_diaries_bwp) {
			this.r17_bal_sub_diaries_bwp = r17_bal_sub_diaries_bwp;
		}

		public String getR18_other_income() {
			return r18_other_income;
		}

		public void setR18_other_income(String r18_other_income) {
			this.r18_other_income = r18_other_income;
		}

		public BigDecimal getR18_fig_bal_sheet() {
			return r18_fig_bal_sheet;
		}

		public void setR18_fig_bal_sheet(BigDecimal r18_fig_bal_sheet) {
			this.r18_fig_bal_sheet = r18_fig_bal_sheet;
		}

		public BigDecimal getR18_fig_bal_sheet_bwp() {
			return r18_fig_bal_sheet_bwp;
		}

		public void setR18_fig_bal_sheet_bwp(BigDecimal r18_fig_bal_sheet_bwp) {
			this.r18_fig_bal_sheet_bwp = r18_fig_bal_sheet_bwp;
		}

		public BigDecimal getR18_amt_statement_adj() {
			return r18_amt_statement_adj;
		}

		public void setR18_amt_statement_adj(BigDecimal r18_amt_statement_adj) {
			this.r18_amt_statement_adj = r18_amt_statement_adj;
		}

		public BigDecimal getR18_amt_statement_adj_bwp() {
			return r18_amt_statement_adj_bwp;
		}

		public void setR18_amt_statement_adj_bwp(BigDecimal r18_amt_statement_adj_bwp) {
			this.r18_amt_statement_adj_bwp = r18_amt_statement_adj_bwp;
		}

		public BigDecimal getR18_net_amt() {
			return r18_net_amt;
		}

		public void setR18_net_amt(BigDecimal r18_net_amt) {
			this.r18_net_amt = r18_net_amt;
		}

		public BigDecimal getR18_net_amt_bwp() {
			return r18_net_amt_bwp;
		}

		public void setR18_net_amt_bwp(BigDecimal r18_net_amt_bwp) {
			this.r18_net_amt_bwp = r18_net_amt_bwp;
		}

		public BigDecimal getR18_bal_sub() {
			return r18_bal_sub;
		}

		public void setR18_bal_sub(BigDecimal r18_bal_sub) {
			this.r18_bal_sub = r18_bal_sub;
		}

		public BigDecimal getR18_bal_sub_bwp() {
			return r18_bal_sub_bwp;
		}

		public void setR18_bal_sub_bwp(BigDecimal r18_bal_sub_bwp) {
			this.r18_bal_sub_bwp = r18_bal_sub_bwp;
		}

		public BigDecimal getR18_bal_sub_diaries_bwp() {
			return r18_bal_sub_diaries_bwp;
		}

		public void setR18_bal_sub_diaries_bwp(BigDecimal r18_bal_sub_diaries_bwp) {
			this.r18_bal_sub_diaries_bwp = r18_bal_sub_diaries_bwp;
		}

		public String getR19_other_income() {
			return r19_other_income;
		}

		public void setR19_other_income(String r19_other_income) {
			this.r19_other_income = r19_other_income;
		}

		public BigDecimal getR19_fig_bal_sheet() {
			return r19_fig_bal_sheet;
		}

		public void setR19_fig_bal_sheet(BigDecimal r19_fig_bal_sheet) {
			this.r19_fig_bal_sheet = r19_fig_bal_sheet;
		}

		public BigDecimal getR19_fig_bal_sheet_bwp() {
			return r19_fig_bal_sheet_bwp;
		}

		public void setR19_fig_bal_sheet_bwp(BigDecimal r19_fig_bal_sheet_bwp) {
			this.r19_fig_bal_sheet_bwp = r19_fig_bal_sheet_bwp;
		}

		public BigDecimal getR19_amt_statement_adj() {
			return r19_amt_statement_adj;
		}

		public void setR19_amt_statement_adj(BigDecimal r19_amt_statement_adj) {
			this.r19_amt_statement_adj = r19_amt_statement_adj;
		}

		public BigDecimal getR19_amt_statement_adj_bwp() {
			return r19_amt_statement_adj_bwp;
		}

		public void setR19_amt_statement_adj_bwp(BigDecimal r19_amt_statement_adj_bwp) {
			this.r19_amt_statement_adj_bwp = r19_amt_statement_adj_bwp;
		}

		public BigDecimal getR19_net_amt() {
			return r19_net_amt;
		}

		public void setR19_net_amt(BigDecimal r19_net_amt) {
			this.r19_net_amt = r19_net_amt;
		}

		public BigDecimal getR19_net_amt_bwp() {
			return r19_net_amt_bwp;
		}

		public void setR19_net_amt_bwp(BigDecimal r19_net_amt_bwp) {
			this.r19_net_amt_bwp = r19_net_amt_bwp;
		}

		public BigDecimal getR19_bal_sub() {
			return r19_bal_sub;
		}

		public void setR19_bal_sub(BigDecimal r19_bal_sub) {
			this.r19_bal_sub = r19_bal_sub;
		}

		public BigDecimal getR19_bal_sub_bwp() {
			return r19_bal_sub_bwp;
		}

		public void setR19_bal_sub_bwp(BigDecimal r19_bal_sub_bwp) {
			this.r19_bal_sub_bwp = r19_bal_sub_bwp;
		}

		public BigDecimal getR19_bal_sub_diaries_bwp() {
			return r19_bal_sub_diaries_bwp;
		}

		public void setR19_bal_sub_diaries_bwp(BigDecimal r19_bal_sub_diaries_bwp) {
			this.r19_bal_sub_diaries_bwp = r19_bal_sub_diaries_bwp;
		}

		public String getR20_other_income() {
			return r20_other_income;
		}

		public void setR20_other_income(String r20_other_income) {
			this.r20_other_income = r20_other_income;
		}

		public BigDecimal getR20_fig_bal_sheet() {
			return r20_fig_bal_sheet;
		}

		public void setR20_fig_bal_sheet(BigDecimal r20_fig_bal_sheet) {
			this.r20_fig_bal_sheet = r20_fig_bal_sheet;
		}

		public BigDecimal getR20_fig_bal_sheet_bwp() {
			return r20_fig_bal_sheet_bwp;
		}

		public void setR20_fig_bal_sheet_bwp(BigDecimal r20_fig_bal_sheet_bwp) {
			this.r20_fig_bal_sheet_bwp = r20_fig_bal_sheet_bwp;
		}

		public BigDecimal getR20_amt_statement_adj() {
			return r20_amt_statement_adj;
		}

		public void setR20_amt_statement_adj(BigDecimal r20_amt_statement_adj) {
			this.r20_amt_statement_adj = r20_amt_statement_adj;
		}

		public BigDecimal getR20_amt_statement_adj_bwp() {
			return r20_amt_statement_adj_bwp;
		}

		public void setR20_amt_statement_adj_bwp(BigDecimal r20_amt_statement_adj_bwp) {
			this.r20_amt_statement_adj_bwp = r20_amt_statement_adj_bwp;
		}

		public BigDecimal getR20_net_amt() {
			return r20_net_amt;
		}

		public void setR20_net_amt(BigDecimal r20_net_amt) {
			this.r20_net_amt = r20_net_amt;
		}

		public BigDecimal getR20_net_amt_bwp() {
			return r20_net_amt_bwp;
		}

		public void setR20_net_amt_bwp(BigDecimal r20_net_amt_bwp) {
			this.r20_net_amt_bwp = r20_net_amt_bwp;
		}

		public BigDecimal getR20_bal_sub() {
			return r20_bal_sub;
		}

		public void setR20_bal_sub(BigDecimal r20_bal_sub) {
			this.r20_bal_sub = r20_bal_sub;
		}

		public BigDecimal getR20_bal_sub_bwp() {
			return r20_bal_sub_bwp;
		}

		public void setR20_bal_sub_bwp(BigDecimal r20_bal_sub_bwp) {
			this.r20_bal_sub_bwp = r20_bal_sub_bwp;
		}

		public BigDecimal getR20_bal_sub_diaries_bwp() {
			return r20_bal_sub_diaries_bwp;
		}

		public void setR20_bal_sub_diaries_bwp(BigDecimal r20_bal_sub_diaries_bwp) {
			this.r20_bal_sub_diaries_bwp = r20_bal_sub_diaries_bwp;
		}

		public String getR21_other_income() {
			return r21_other_income;
		}

		public void setR21_other_income(String r21_other_income) {
			this.r21_other_income = r21_other_income;
		}

		public BigDecimal getR21_fig_bal_sheet() {
			return r21_fig_bal_sheet;
		}

		public void setR21_fig_bal_sheet(BigDecimal r21_fig_bal_sheet) {
			this.r21_fig_bal_sheet = r21_fig_bal_sheet;
		}

		public BigDecimal getR21_fig_bal_sheet_bwp() {
			return r21_fig_bal_sheet_bwp;
		}

		public void setR21_fig_bal_sheet_bwp(BigDecimal r21_fig_bal_sheet_bwp) {
			this.r21_fig_bal_sheet_bwp = r21_fig_bal_sheet_bwp;
		}

		public BigDecimal getR21_amt_statement_adj() {
			return r21_amt_statement_adj;
		}

		public void setR21_amt_statement_adj(BigDecimal r21_amt_statement_adj) {
			this.r21_amt_statement_adj = r21_amt_statement_adj;
		}

		public BigDecimal getR21_amt_statement_adj_bwp() {
			return r21_amt_statement_adj_bwp;
		}

		public void setR21_amt_statement_adj_bwp(BigDecimal r21_amt_statement_adj_bwp) {
			this.r21_amt_statement_adj_bwp = r21_amt_statement_adj_bwp;
		}

		public BigDecimal getR21_net_amt() {
			return r21_net_amt;
		}

		public void setR21_net_amt(BigDecimal r21_net_amt) {
			this.r21_net_amt = r21_net_amt;
		}

		public BigDecimal getR21_net_amt_bwp() {
			return r21_net_amt_bwp;
		}

		public void setR21_net_amt_bwp(BigDecimal r21_net_amt_bwp) {
			this.r21_net_amt_bwp = r21_net_amt_bwp;
		}

		public BigDecimal getR21_bal_sub() {
			return r21_bal_sub;
		}

		public void setR21_bal_sub(BigDecimal r21_bal_sub) {
			this.r21_bal_sub = r21_bal_sub;
		}

		public BigDecimal getR21_bal_sub_bwp() {
			return r21_bal_sub_bwp;
		}

		public void setR21_bal_sub_bwp(BigDecimal r21_bal_sub_bwp) {
			this.r21_bal_sub_bwp = r21_bal_sub_bwp;
		}

		public BigDecimal getR21_bal_sub_diaries_bwp() {
			return r21_bal_sub_diaries_bwp;
		}

		public void setR21_bal_sub_diaries_bwp(BigDecimal r21_bal_sub_diaries_bwp) {
			this.r21_bal_sub_diaries_bwp = r21_bal_sub_diaries_bwp;
		}

		public String getR22_other_income() {
			return r22_other_income;
		}

		public void setR22_other_income(String r22_other_income) {
			this.r22_other_income = r22_other_income;
		}

		public BigDecimal getR22_fig_bal_sheet() {
			return r22_fig_bal_sheet;
		}

		public void setR22_fig_bal_sheet(BigDecimal r22_fig_bal_sheet) {
			this.r22_fig_bal_sheet = r22_fig_bal_sheet;
		}

		public BigDecimal getR22_fig_bal_sheet_bwp() {
			return r22_fig_bal_sheet_bwp;
		}

		public void setR22_fig_bal_sheet_bwp(BigDecimal r22_fig_bal_sheet_bwp) {
			this.r22_fig_bal_sheet_bwp = r22_fig_bal_sheet_bwp;
		}

		public BigDecimal getR22_amt_statement_adj() {
			return r22_amt_statement_adj;
		}

		public void setR22_amt_statement_adj(BigDecimal r22_amt_statement_adj) {
			this.r22_amt_statement_adj = r22_amt_statement_adj;
		}

		public BigDecimal getR22_amt_statement_adj_bwp() {
			return r22_amt_statement_adj_bwp;
		}

		public void setR22_amt_statement_adj_bwp(BigDecimal r22_amt_statement_adj_bwp) {
			this.r22_amt_statement_adj_bwp = r22_amt_statement_adj_bwp;
		}

		public BigDecimal getR22_net_amt() {
			return r22_net_amt;
		}

		public void setR22_net_amt(BigDecimal r22_net_amt) {
			this.r22_net_amt = r22_net_amt;
		}

		public BigDecimal getR22_net_amt_bwp() {
			return r22_net_amt_bwp;
		}

		public void setR22_net_amt_bwp(BigDecimal r22_net_amt_bwp) {
			this.r22_net_amt_bwp = r22_net_amt_bwp;
		}

		public BigDecimal getR22_bal_sub() {
			return r22_bal_sub;
		}

		public void setR22_bal_sub(BigDecimal r22_bal_sub) {
			this.r22_bal_sub = r22_bal_sub;
		}

		public BigDecimal getR22_bal_sub_bwp() {
			return r22_bal_sub_bwp;
		}

		public void setR22_bal_sub_bwp(BigDecimal r22_bal_sub_bwp) {
			this.r22_bal_sub_bwp = r22_bal_sub_bwp;
		}

		public BigDecimal getR22_bal_sub_diaries_bwp() {
			return r22_bal_sub_diaries_bwp;
		}

		public void setR22_bal_sub_diaries_bwp(BigDecimal r22_bal_sub_diaries_bwp) {
			this.r22_bal_sub_diaries_bwp = r22_bal_sub_diaries_bwp;
		}

		public String getR23_other_income() {
			return r23_other_income;
		}

		public void setR23_other_income(String r23_other_income) {
			this.r23_other_income = r23_other_income;
		}

		public BigDecimal getR23_fig_bal_sheet() {
			return r23_fig_bal_sheet;
		}

		public void setR23_fig_bal_sheet(BigDecimal r23_fig_bal_sheet) {
			this.r23_fig_bal_sheet = r23_fig_bal_sheet;
		}

		public BigDecimal getR23_fig_bal_sheet_bwp() {
			return r23_fig_bal_sheet_bwp;
		}

		public void setR23_fig_bal_sheet_bwp(BigDecimal r23_fig_bal_sheet_bwp) {
			this.r23_fig_bal_sheet_bwp = r23_fig_bal_sheet_bwp;
		}

		public BigDecimal getR23_amt_statement_adj() {
			return r23_amt_statement_adj;
		}

		public void setR23_amt_statement_adj(BigDecimal r23_amt_statement_adj) {
			this.r23_amt_statement_adj = r23_amt_statement_adj;
		}

		public BigDecimal getR23_amt_statement_adj_bwp() {
			return r23_amt_statement_adj_bwp;
		}

		public void setR23_amt_statement_adj_bwp(BigDecimal r23_amt_statement_adj_bwp) {
			this.r23_amt_statement_adj_bwp = r23_amt_statement_adj_bwp;
		}

		public BigDecimal getR23_net_amt() {
			return r23_net_amt;
		}

		public void setR23_net_amt(BigDecimal r23_net_amt) {
			this.r23_net_amt = r23_net_amt;
		}

		public BigDecimal getR23_net_amt_bwp() {
			return r23_net_amt_bwp;
		}

		public void setR23_net_amt_bwp(BigDecimal r23_net_amt_bwp) {
			this.r23_net_amt_bwp = r23_net_amt_bwp;
		}

		public BigDecimal getR23_bal_sub() {
			return r23_bal_sub;
		}

		public void setR23_bal_sub(BigDecimal r23_bal_sub) {
			this.r23_bal_sub = r23_bal_sub;
		}

		public BigDecimal getR23_bal_sub_bwp() {
			return r23_bal_sub_bwp;
		}

		public void setR23_bal_sub_bwp(BigDecimal r23_bal_sub_bwp) {
			this.r23_bal_sub_bwp = r23_bal_sub_bwp;
		}

		public BigDecimal getR23_bal_sub_diaries_bwp() {
			return r23_bal_sub_diaries_bwp;
		}

		public void setR23_bal_sub_diaries_bwp(BigDecimal r23_bal_sub_diaries_bwp) {
			this.r23_bal_sub_diaries_bwp = r23_bal_sub_diaries_bwp;
		}

		public String getR24_other_income() {
			return r24_other_income;
		}

		public void setR24_other_income(String r24_other_income) {
			this.r24_other_income = r24_other_income;
		}

		public BigDecimal getR24_fig_bal_sheet() {
			return r24_fig_bal_sheet;
		}

		public void setR24_fig_bal_sheet(BigDecimal r24_fig_bal_sheet) {
			this.r24_fig_bal_sheet = r24_fig_bal_sheet;
		}

		public BigDecimal getR24_fig_bal_sheet_bwp() {
			return r24_fig_bal_sheet_bwp;
		}

		public void setR24_fig_bal_sheet_bwp(BigDecimal r24_fig_bal_sheet_bwp) {
			this.r24_fig_bal_sheet_bwp = r24_fig_bal_sheet_bwp;
		}

		public BigDecimal getR24_amt_statement_adj() {
			return r24_amt_statement_adj;
		}

		public void setR24_amt_statement_adj(BigDecimal r24_amt_statement_adj) {
			this.r24_amt_statement_adj = r24_amt_statement_adj;
		}

		public BigDecimal getR24_amt_statement_adj_bwp() {
			return r24_amt_statement_adj_bwp;
		}

		public void setR24_amt_statement_adj_bwp(BigDecimal r24_amt_statement_adj_bwp) {
			this.r24_amt_statement_adj_bwp = r24_amt_statement_adj_bwp;
		}

		public BigDecimal getR24_net_amt() {
			return r24_net_amt;
		}

		public void setR24_net_amt(BigDecimal r24_net_amt) {
			this.r24_net_amt = r24_net_amt;
		}

		public BigDecimal getR24_net_amt_bwp() {
			return r24_net_amt_bwp;
		}

		public void setR24_net_amt_bwp(BigDecimal r24_net_amt_bwp) {
			this.r24_net_amt_bwp = r24_net_amt_bwp;
		}

		public BigDecimal getR24_bal_sub() {
			return r24_bal_sub;
		}

		public void setR24_bal_sub(BigDecimal r24_bal_sub) {
			this.r24_bal_sub = r24_bal_sub;
		}

		public BigDecimal getR24_bal_sub_bwp() {
			return r24_bal_sub_bwp;
		}

		public void setR24_bal_sub_bwp(BigDecimal r24_bal_sub_bwp) {
			this.r24_bal_sub_bwp = r24_bal_sub_bwp;
		}

		public BigDecimal getR24_bal_sub_diaries_bwp() {
			return r24_bal_sub_diaries_bwp;
		}

		public void setR24_bal_sub_diaries_bwp(BigDecimal r24_bal_sub_diaries_bwp) {
			this.r24_bal_sub_diaries_bwp = r24_bal_sub_diaries_bwp;
		}

		public String getR25_other_income() {
			return r25_other_income;
		}

		public void setR25_other_income(String r25_other_income) {
			this.r25_other_income = r25_other_income;
		}

		public BigDecimal getR25_fig_bal_sheet() {
			return r25_fig_bal_sheet;
		}

		public void setR25_fig_bal_sheet(BigDecimal r25_fig_bal_sheet) {
			this.r25_fig_bal_sheet = r25_fig_bal_sheet;
		}

		public BigDecimal getR25_fig_bal_sheet_bwp() {
			return r25_fig_bal_sheet_bwp;
		}

		public void setR25_fig_bal_sheet_bwp(BigDecimal r25_fig_bal_sheet_bwp) {
			this.r25_fig_bal_sheet_bwp = r25_fig_bal_sheet_bwp;
		}

		public BigDecimal getR25_amt_statement_adj() {
			return r25_amt_statement_adj;
		}

		public void setR25_amt_statement_adj(BigDecimal r25_amt_statement_adj) {
			this.r25_amt_statement_adj = r25_amt_statement_adj;
		}

		public BigDecimal getR25_amt_statement_adj_bwp() {
			return r25_amt_statement_adj_bwp;
		}

		public void setR25_amt_statement_adj_bwp(BigDecimal r25_amt_statement_adj_bwp) {
			this.r25_amt_statement_adj_bwp = r25_amt_statement_adj_bwp;
		}

		public BigDecimal getR25_net_amt() {
			return r25_net_amt;
		}

		public void setR25_net_amt(BigDecimal r25_net_amt) {
			this.r25_net_amt = r25_net_amt;
		}

		public BigDecimal getR25_net_amt_bwp() {
			return r25_net_amt_bwp;
		}

		public void setR25_net_amt_bwp(BigDecimal r25_net_amt_bwp) {
			this.r25_net_amt_bwp = r25_net_amt_bwp;
		}

		public BigDecimal getR25_bal_sub() {
			return r25_bal_sub;
		}

		public void setR25_bal_sub(BigDecimal r25_bal_sub) {
			this.r25_bal_sub = r25_bal_sub;
		}

		public BigDecimal getR25_bal_sub_bwp() {
			return r25_bal_sub_bwp;
		}

		public void setR25_bal_sub_bwp(BigDecimal r25_bal_sub_bwp) {
			this.r25_bal_sub_bwp = r25_bal_sub_bwp;
		}

		public BigDecimal getR25_bal_sub_diaries_bwp() {
			return r25_bal_sub_diaries_bwp;
		}

		public void setR25_bal_sub_diaries_bwp(BigDecimal r25_bal_sub_diaries_bwp) {
			this.r25_bal_sub_diaries_bwp = r25_bal_sub_diaries_bwp;
		}

		public String getR26_other_income() {
			return r26_other_income;
		}

		public void setR26_other_income(String r26_other_income) {
			this.r26_other_income = r26_other_income;
		}

		public BigDecimal getR26_fig_bal_sheet() {
			return r26_fig_bal_sheet;
		}

		public void setR26_fig_bal_sheet(BigDecimal r26_fig_bal_sheet) {
			this.r26_fig_bal_sheet = r26_fig_bal_sheet;
		}

		public BigDecimal getR26_fig_bal_sheet_bwp() {
			return r26_fig_bal_sheet_bwp;
		}

		public void setR26_fig_bal_sheet_bwp(BigDecimal r26_fig_bal_sheet_bwp) {
			this.r26_fig_bal_sheet_bwp = r26_fig_bal_sheet_bwp;
		}

		public BigDecimal getR26_amt_statement_adj() {
			return r26_amt_statement_adj;
		}

		public void setR26_amt_statement_adj(BigDecimal r26_amt_statement_adj) {
			this.r26_amt_statement_adj = r26_amt_statement_adj;
		}

		public BigDecimal getR26_amt_statement_adj_bwp() {
			return r26_amt_statement_adj_bwp;
		}

		public void setR26_amt_statement_adj_bwp(BigDecimal r26_amt_statement_adj_bwp) {
			this.r26_amt_statement_adj_bwp = r26_amt_statement_adj_bwp;
		}

		public BigDecimal getR26_net_amt() {
			return r26_net_amt;
		}

		public void setR26_net_amt(BigDecimal r26_net_amt) {
			this.r26_net_amt = r26_net_amt;
		}

		public BigDecimal getR26_net_amt_bwp() {
			return r26_net_amt_bwp;
		}

		public void setR26_net_amt_bwp(BigDecimal r26_net_amt_bwp) {
			this.r26_net_amt_bwp = r26_net_amt_bwp;
		}

		public BigDecimal getR26_bal_sub() {
			return r26_bal_sub;
		}

		public void setR26_bal_sub(BigDecimal r26_bal_sub) {
			this.r26_bal_sub = r26_bal_sub;
		}

		public BigDecimal getR26_bal_sub_bwp() {
			return r26_bal_sub_bwp;
		}

		public void setR26_bal_sub_bwp(BigDecimal r26_bal_sub_bwp) {
			this.r26_bal_sub_bwp = r26_bal_sub_bwp;
		}

		public BigDecimal getR26_bal_sub_diaries_bwp() {
			return r26_bal_sub_diaries_bwp;
		}

		public void setR26_bal_sub_diaries_bwp(BigDecimal r26_bal_sub_diaries_bwp) {
			this.r26_bal_sub_diaries_bwp = r26_bal_sub_diaries_bwp;
		}

		public String getR27_other_income() {
			return r27_other_income;
		}

		public void setR27_other_income(String r27_other_income) {
			this.r27_other_income = r27_other_income;
		}

		public BigDecimal getR27_fig_bal_sheet() {
			return r27_fig_bal_sheet;
		}

		public void setR27_fig_bal_sheet(BigDecimal r27_fig_bal_sheet) {
			this.r27_fig_bal_sheet = r27_fig_bal_sheet;
		}

		public BigDecimal getR27_fig_bal_sheet_bwp() {
			return r27_fig_bal_sheet_bwp;
		}

		public void setR27_fig_bal_sheet_bwp(BigDecimal r27_fig_bal_sheet_bwp) {
			this.r27_fig_bal_sheet_bwp = r27_fig_bal_sheet_bwp;
		}

		public BigDecimal getR27_amt_statement_adj() {
			return r27_amt_statement_adj;
		}

		public void setR27_amt_statement_adj(BigDecimal r27_amt_statement_adj) {
			this.r27_amt_statement_adj = r27_amt_statement_adj;
		}

		public BigDecimal getR27_amt_statement_adj_bwp() {
			return r27_amt_statement_adj_bwp;
		}

		public void setR27_amt_statement_adj_bwp(BigDecimal r27_amt_statement_adj_bwp) {
			this.r27_amt_statement_adj_bwp = r27_amt_statement_adj_bwp;
		}

		public BigDecimal getR27_net_amt() {
			return r27_net_amt;
		}

		public void setR27_net_amt(BigDecimal r27_net_amt) {
			this.r27_net_amt = r27_net_amt;
		}

		public BigDecimal getR27_net_amt_bwp() {
			return r27_net_amt_bwp;
		}

		public void setR27_net_amt_bwp(BigDecimal r27_net_amt_bwp) {
			this.r27_net_amt_bwp = r27_net_amt_bwp;
		}

		public BigDecimal getR27_bal_sub() {
			return r27_bal_sub;
		}

		public void setR27_bal_sub(BigDecimal r27_bal_sub) {
			this.r27_bal_sub = r27_bal_sub;
		}

		public BigDecimal getR27_bal_sub_bwp() {
			return r27_bal_sub_bwp;
		}

		public void setR27_bal_sub_bwp(BigDecimal r27_bal_sub_bwp) {
			this.r27_bal_sub_bwp = r27_bal_sub_bwp;
		}

		public BigDecimal getR27_bal_sub_diaries_bwp() {
			return r27_bal_sub_diaries_bwp;
		}

		public void setR27_bal_sub_diaries_bwp(BigDecimal r27_bal_sub_diaries_bwp) {
			this.r27_bal_sub_diaries_bwp = r27_bal_sub_diaries_bwp;
		}

		public String getR28_other_income() {
			return r28_other_income;
		}

		public void setR28_other_income(String r28_other_income) {
			this.r28_other_income = r28_other_income;
		}

		public BigDecimal getR28_fig_bal_sheet() {
			return r28_fig_bal_sheet;
		}

		public void setR28_fig_bal_sheet(BigDecimal r28_fig_bal_sheet) {
			this.r28_fig_bal_sheet = r28_fig_bal_sheet;
		}

		public BigDecimal getR28_fig_bal_sheet_bwp() {
			return r28_fig_bal_sheet_bwp;
		}

		public void setR28_fig_bal_sheet_bwp(BigDecimal r28_fig_bal_sheet_bwp) {
			this.r28_fig_bal_sheet_bwp = r28_fig_bal_sheet_bwp;
		}

		public BigDecimal getR28_amt_statement_adj() {
			return r28_amt_statement_adj;
		}

		public void setR28_amt_statement_adj(BigDecimal r28_amt_statement_adj) {
			this.r28_amt_statement_adj = r28_amt_statement_adj;
		}

		public BigDecimal getR28_amt_statement_adj_bwp() {
			return r28_amt_statement_adj_bwp;
		}

		public void setR28_amt_statement_adj_bwp(BigDecimal r28_amt_statement_adj_bwp) {
			this.r28_amt_statement_adj_bwp = r28_amt_statement_adj_bwp;
		}

		public BigDecimal getR28_net_amt() {
			return r28_net_amt;
		}

		public void setR28_net_amt(BigDecimal r28_net_amt) {
			this.r28_net_amt = r28_net_amt;
		}

		public BigDecimal getR28_net_amt_bwp() {
			return r28_net_amt_bwp;
		}

		public void setR28_net_amt_bwp(BigDecimal r28_net_amt_bwp) {
			this.r28_net_amt_bwp = r28_net_amt_bwp;
		}

		public BigDecimal getR28_bal_sub() {
			return r28_bal_sub;
		}

		public void setR28_bal_sub(BigDecimal r28_bal_sub) {
			this.r28_bal_sub = r28_bal_sub;
		}

		public BigDecimal getR28_bal_sub_bwp() {
			return r28_bal_sub_bwp;
		}

		public void setR28_bal_sub_bwp(BigDecimal r28_bal_sub_bwp) {
			this.r28_bal_sub_bwp = r28_bal_sub_bwp;
		}

		public BigDecimal getR28_bal_sub_diaries_bwp() {
			return r28_bal_sub_diaries_bwp;
		}

		public void setR28_bal_sub_diaries_bwp(BigDecimal r28_bal_sub_diaries_bwp) {
			this.r28_bal_sub_diaries_bwp = r28_bal_sub_diaries_bwp;
		}

		public String getR29_other_income() {
			return r29_other_income;
		}

		public void setR29_other_income(String r29_other_income) {
			this.r29_other_income = r29_other_income;
		}

		public BigDecimal getR29_fig_bal_sheet() {
			return r29_fig_bal_sheet;
		}

		public void setR29_fig_bal_sheet(BigDecimal r29_fig_bal_sheet) {
			this.r29_fig_bal_sheet = r29_fig_bal_sheet;
		}

		public BigDecimal getR29_fig_bal_sheet_bwp() {
			return r29_fig_bal_sheet_bwp;
		}

		public void setR29_fig_bal_sheet_bwp(BigDecimal r29_fig_bal_sheet_bwp) {
			this.r29_fig_bal_sheet_bwp = r29_fig_bal_sheet_bwp;
		}

		public BigDecimal getR29_amt_statement_adj() {
			return r29_amt_statement_adj;
		}

		public void setR29_amt_statement_adj(BigDecimal r29_amt_statement_adj) {
			this.r29_amt_statement_adj = r29_amt_statement_adj;
		}

		public BigDecimal getR29_amt_statement_adj_bwp() {
			return r29_amt_statement_adj_bwp;
		}

		public void setR29_amt_statement_adj_bwp(BigDecimal r29_amt_statement_adj_bwp) {
			this.r29_amt_statement_adj_bwp = r29_amt_statement_adj_bwp;
		}

		public BigDecimal getR29_net_amt() {
			return r29_net_amt;
		}

		public void setR29_net_amt(BigDecimal r29_net_amt) {
			this.r29_net_amt = r29_net_amt;
		}

		public BigDecimal getR29_net_amt_bwp() {
			return r29_net_amt_bwp;
		}

		public void setR29_net_amt_bwp(BigDecimal r29_net_amt_bwp) {
			this.r29_net_amt_bwp = r29_net_amt_bwp;
		}

		public BigDecimal getR29_bal_sub() {
			return r29_bal_sub;
		}

		public void setR29_bal_sub(BigDecimal r29_bal_sub) {
			this.r29_bal_sub = r29_bal_sub;
		}

		public BigDecimal getR29_bal_sub_bwp() {
			return r29_bal_sub_bwp;
		}

		public void setR29_bal_sub_bwp(BigDecimal r29_bal_sub_bwp) {
			this.r29_bal_sub_bwp = r29_bal_sub_bwp;
		}

		public BigDecimal getR29_bal_sub_diaries_bwp() {
			return r29_bal_sub_diaries_bwp;
		}

		public void setR29_bal_sub_diaries_bwp(BigDecimal r29_bal_sub_diaries_bwp) {
			this.r29_bal_sub_diaries_bwp = r29_bal_sub_diaries_bwp;
		}

		public String getR30_other_income() {
			return r30_other_income;
		}

		public void setR30_other_income(String r30_other_income) {
			this.r30_other_income = r30_other_income;
		}

		public BigDecimal getR30_fig_bal_sheet() {
			return r30_fig_bal_sheet;
		}

		public void setR30_fig_bal_sheet(BigDecimal r30_fig_bal_sheet) {
			this.r30_fig_bal_sheet = r30_fig_bal_sheet;
		}

		public BigDecimal getR30_fig_bal_sheet_bwp() {
			return r30_fig_bal_sheet_bwp;
		}

		public void setR30_fig_bal_sheet_bwp(BigDecimal r30_fig_bal_sheet_bwp) {
			this.r30_fig_bal_sheet_bwp = r30_fig_bal_sheet_bwp;
		}

		public BigDecimal getR30_amt_statement_adj() {
			return r30_amt_statement_adj;
		}

		public void setR30_amt_statement_adj(BigDecimal r30_amt_statement_adj) {
			this.r30_amt_statement_adj = r30_amt_statement_adj;
		}

		public BigDecimal getR30_amt_statement_adj_bwp() {
			return r30_amt_statement_adj_bwp;
		}

		public void setR30_amt_statement_adj_bwp(BigDecimal r30_amt_statement_adj_bwp) {
			this.r30_amt_statement_adj_bwp = r30_amt_statement_adj_bwp;
		}

		public BigDecimal getR30_net_amt() {
			return r30_net_amt;
		}

		public void setR30_net_amt(BigDecimal r30_net_amt) {
			this.r30_net_amt = r30_net_amt;
		}

		public BigDecimal getR30_net_amt_bwp() {
			return r30_net_amt_bwp;
		}

		public void setR30_net_amt_bwp(BigDecimal r30_net_amt_bwp) {
			this.r30_net_amt_bwp = r30_net_amt_bwp;
		}

		public BigDecimal getR30_bal_sub() {
			return r30_bal_sub;
		}

		public void setR30_bal_sub(BigDecimal r30_bal_sub) {
			this.r30_bal_sub = r30_bal_sub;
		}

		public BigDecimal getR30_bal_sub_bwp() {
			return r30_bal_sub_bwp;
		}

		public void setR30_bal_sub_bwp(BigDecimal r30_bal_sub_bwp) {
			this.r30_bal_sub_bwp = r30_bal_sub_bwp;
		}

		public BigDecimal getR30_bal_sub_diaries_bwp() {
			return r30_bal_sub_diaries_bwp;
		}

		public void setR30_bal_sub_diaries_bwp(BigDecimal r30_bal_sub_diaries_bwp) {
			this.r30_bal_sub_diaries_bwp = r30_bal_sub_diaries_bwp;
		}

		public String getR31_other_income() {
			return r31_other_income;
		}

		public void setR31_other_income(String r31_other_income) {
			this.r31_other_income = r31_other_income;
		}

		public BigDecimal getR31_fig_bal_sheet() {
			return r31_fig_bal_sheet;
		}

		public void setR31_fig_bal_sheet(BigDecimal r31_fig_bal_sheet) {
			this.r31_fig_bal_sheet = r31_fig_bal_sheet;
		}

		public BigDecimal getR31_fig_bal_sheet_bwp() {
			return r31_fig_bal_sheet_bwp;
		}

		public void setR31_fig_bal_sheet_bwp(BigDecimal r31_fig_bal_sheet_bwp) {
			this.r31_fig_bal_sheet_bwp = r31_fig_bal_sheet_bwp;
		}

		public BigDecimal getR31_amt_statement_adj() {
			return r31_amt_statement_adj;
		}

		public void setR31_amt_statement_adj(BigDecimal r31_amt_statement_adj) {
			this.r31_amt_statement_adj = r31_amt_statement_adj;
		}

		public BigDecimal getR31_amt_statement_adj_bwp() {
			return r31_amt_statement_adj_bwp;
		}

		public void setR31_amt_statement_adj_bwp(BigDecimal r31_amt_statement_adj_bwp) {
			this.r31_amt_statement_adj_bwp = r31_amt_statement_adj_bwp;
		}

		public BigDecimal getR31_net_amt() {
			return r31_net_amt;
		}

		public void setR31_net_amt(BigDecimal r31_net_amt) {
			this.r31_net_amt = r31_net_amt;
		}

		public BigDecimal getR31_net_amt_bwp() {
			return r31_net_amt_bwp;
		}

		public void setR31_net_amt_bwp(BigDecimal r31_net_amt_bwp) {
			this.r31_net_amt_bwp = r31_net_amt_bwp;
		}

		public BigDecimal getR31_bal_sub() {
			return r31_bal_sub;
		}

		public void setR31_bal_sub(BigDecimal r31_bal_sub) {
			this.r31_bal_sub = r31_bal_sub;
		}

		public BigDecimal getR31_bal_sub_bwp() {
			return r31_bal_sub_bwp;
		}

		public void setR31_bal_sub_bwp(BigDecimal r31_bal_sub_bwp) {
			this.r31_bal_sub_bwp = r31_bal_sub_bwp;
		}

		public BigDecimal getR31_bal_sub_diaries_bwp() {
			return r31_bal_sub_diaries_bwp;
		}

		public void setR31_bal_sub_diaries_bwp(BigDecimal r31_bal_sub_diaries_bwp) {
			this.r31_bal_sub_diaries_bwp = r31_bal_sub_diaries_bwp;
		}

		public String getR40_intrest_expended() {
			return r40_intrest_expended;
		}

		public void setR40_intrest_expended(String r40_intrest_expended) {
			this.r40_intrest_expended = r40_intrest_expended;
		}

		public BigDecimal getR40_fig_bal_sheet() {
			return r40_fig_bal_sheet;
		}

		public void setR40_fig_bal_sheet(BigDecimal r40_fig_bal_sheet) {
			this.r40_fig_bal_sheet = r40_fig_bal_sheet;
		}

		public BigDecimal getR40_fig_bal_sheet_bwp() {
			return r40_fig_bal_sheet_bwp;
		}

		public void setR40_fig_bal_sheet_bwp(BigDecimal r40_fig_bal_sheet_bwp) {
			this.r40_fig_bal_sheet_bwp = r40_fig_bal_sheet_bwp;
		}

		public BigDecimal getR40_amt_statement_adj() {
			return r40_amt_statement_adj;
		}

		public void setR40_amt_statement_adj(BigDecimal r40_amt_statement_adj) {
			this.r40_amt_statement_adj = r40_amt_statement_adj;
		}

		public BigDecimal getR40_amt_statement_adj_bwp() {
			return r40_amt_statement_adj_bwp;
		}

		public void setR40_amt_statement_adj_bwp(BigDecimal r40_amt_statement_adj_bwp) {
			this.r40_amt_statement_adj_bwp = r40_amt_statement_adj_bwp;
		}

		public BigDecimal getR40_net_amt() {
			return r40_net_amt;
		}

		public void setR40_net_amt(BigDecimal r40_net_amt) {
			this.r40_net_amt = r40_net_amt;
		}

		public BigDecimal getR40_net_amt_bwp() {
			return r40_net_amt_bwp;
		}

		public void setR40_net_amt_bwp(BigDecimal r40_net_amt_bwp) {
			this.r40_net_amt_bwp = r40_net_amt_bwp;
		}

		public BigDecimal getR40_bal_sub() {
			return r40_bal_sub;
		}

		public void setR40_bal_sub(BigDecimal r40_bal_sub) {
			this.r40_bal_sub = r40_bal_sub;
		}

		public BigDecimal getR40_bal_sub_bwp() {
			return r40_bal_sub_bwp;
		}

		public void setR40_bal_sub_bwp(BigDecimal r40_bal_sub_bwp) {
			this.r40_bal_sub_bwp = r40_bal_sub_bwp;
		}

		public BigDecimal getR40_bal_sub_diaries_bwp() {
			return r40_bal_sub_diaries_bwp;
		}

		public void setR40_bal_sub_diaries_bwp(BigDecimal r40_bal_sub_diaries_bwp) {
			this.r40_bal_sub_diaries_bwp = r40_bal_sub_diaries_bwp;
		}

		public String getR41_intrest_expended() {
			return r41_intrest_expended;
		}

		public void setR41_intrest_expended(String r41_intrest_expended) {
			this.r41_intrest_expended = r41_intrest_expended;
		}

		public BigDecimal getR41_fig_bal_sheet() {
			return r41_fig_bal_sheet;
		}

		public void setR41_fig_bal_sheet(BigDecimal r41_fig_bal_sheet) {
			this.r41_fig_bal_sheet = r41_fig_bal_sheet;
		}

		public BigDecimal getR41_fig_bal_sheet_bwp() {
			return r41_fig_bal_sheet_bwp;
		}

		public void setR41_fig_bal_sheet_bwp(BigDecimal r41_fig_bal_sheet_bwp) {
			this.r41_fig_bal_sheet_bwp = r41_fig_bal_sheet_bwp;
		}

		public BigDecimal getR41_amt_statement_adj() {
			return r41_amt_statement_adj;
		}

		public void setR41_amt_statement_adj(BigDecimal r41_amt_statement_adj) {
			this.r41_amt_statement_adj = r41_amt_statement_adj;
		}

		public BigDecimal getR41_amt_statement_adj_bwp() {
			return r41_amt_statement_adj_bwp;
		}

		public void setR41_amt_statement_adj_bwp(BigDecimal r41_amt_statement_adj_bwp) {
			this.r41_amt_statement_adj_bwp = r41_amt_statement_adj_bwp;
		}

		public BigDecimal getR41_net_amt() {
			return r41_net_amt;
		}

		public void setR41_net_amt(BigDecimal r41_net_amt) {
			this.r41_net_amt = r41_net_amt;
		}

		public BigDecimal getR41_net_amt_bwp() {
			return r41_net_amt_bwp;
		}

		public void setR41_net_amt_bwp(BigDecimal r41_net_amt_bwp) {
			this.r41_net_amt_bwp = r41_net_amt_bwp;
		}

		public BigDecimal getR41_bal_sub() {
			return r41_bal_sub;
		}

		public void setR41_bal_sub(BigDecimal r41_bal_sub) {
			this.r41_bal_sub = r41_bal_sub;
		}

		public BigDecimal getR41_bal_sub_bwp() {
			return r41_bal_sub_bwp;
		}

		public void setR41_bal_sub_bwp(BigDecimal r41_bal_sub_bwp) {
			this.r41_bal_sub_bwp = r41_bal_sub_bwp;
		}

		public BigDecimal getR41_bal_sub_diaries_bwp() {
			return r41_bal_sub_diaries_bwp;
		}

		public void setR41_bal_sub_diaries_bwp(BigDecimal r41_bal_sub_diaries_bwp) {
			this.r41_bal_sub_diaries_bwp = r41_bal_sub_diaries_bwp;
		}

		public String getR42_intrest_expended() {
			return r42_intrest_expended;
		}

		public void setR42_intrest_expended(String r42_intrest_expended) {
			this.r42_intrest_expended = r42_intrest_expended;
		}

		public BigDecimal getR42_fig_bal_sheet() {
			return r42_fig_bal_sheet;
		}

		public void setR42_fig_bal_sheet(BigDecimal r42_fig_bal_sheet) {
			this.r42_fig_bal_sheet = r42_fig_bal_sheet;
		}

		public BigDecimal getR42_fig_bal_sheet_bwp() {
			return r42_fig_bal_sheet_bwp;
		}

		public void setR42_fig_bal_sheet_bwp(BigDecimal r42_fig_bal_sheet_bwp) {
			this.r42_fig_bal_sheet_bwp = r42_fig_bal_sheet_bwp;
		}

		public BigDecimal getR42_amt_statement_adj() {
			return r42_amt_statement_adj;
		}

		public void setR42_amt_statement_adj(BigDecimal r42_amt_statement_adj) {
			this.r42_amt_statement_adj = r42_amt_statement_adj;
		}

		public BigDecimal getR42_amt_statement_adj_bwp() {
			return r42_amt_statement_adj_bwp;
		}

		public void setR42_amt_statement_adj_bwp(BigDecimal r42_amt_statement_adj_bwp) {
			this.r42_amt_statement_adj_bwp = r42_amt_statement_adj_bwp;
		}

		public BigDecimal getR42_net_amt() {
			return r42_net_amt;
		}

		public void setR42_net_amt(BigDecimal r42_net_amt) {
			this.r42_net_amt = r42_net_amt;
		}

		public BigDecimal getR42_net_amt_bwp() {
			return r42_net_amt_bwp;
		}

		public void setR42_net_amt_bwp(BigDecimal r42_net_amt_bwp) {
			this.r42_net_amt_bwp = r42_net_amt_bwp;
		}

		public BigDecimal getR42_bal_sub() {
			return r42_bal_sub;
		}

		public void setR42_bal_sub(BigDecimal r42_bal_sub) {
			this.r42_bal_sub = r42_bal_sub;
		}

		public BigDecimal getR42_bal_sub_bwp() {
			return r42_bal_sub_bwp;
		}

		public void setR42_bal_sub_bwp(BigDecimal r42_bal_sub_bwp) {
			this.r42_bal_sub_bwp = r42_bal_sub_bwp;
		}

		public BigDecimal getR42_bal_sub_diaries_bwp() {
			return r42_bal_sub_diaries_bwp;
		}

		public void setR42_bal_sub_diaries_bwp(BigDecimal r42_bal_sub_diaries_bwp) {
			this.r42_bal_sub_diaries_bwp = r42_bal_sub_diaries_bwp;
		}

		public String getR43_intrest_expended() {
			return r43_intrest_expended;
		}

		public void setR43_intrest_expended(String r43_intrest_expended) {
			this.r43_intrest_expended = r43_intrest_expended;
		}

		public BigDecimal getR43_fig_bal_sheet() {
			return r43_fig_bal_sheet;
		}

		public void setR43_fig_bal_sheet(BigDecimal r43_fig_bal_sheet) {
			this.r43_fig_bal_sheet = r43_fig_bal_sheet;
		}

		public BigDecimal getR43_fig_bal_sheet_bwp() {
			return r43_fig_bal_sheet_bwp;
		}

		public void setR43_fig_bal_sheet_bwp(BigDecimal r43_fig_bal_sheet_bwp) {
			this.r43_fig_bal_sheet_bwp = r43_fig_bal_sheet_bwp;
		}

		public BigDecimal getR43_amt_statement_adj() {
			return r43_amt_statement_adj;
		}

		public void setR43_amt_statement_adj(BigDecimal r43_amt_statement_adj) {
			this.r43_amt_statement_adj = r43_amt_statement_adj;
		}

		public BigDecimal getR43_amt_statement_adj_bwp() {
			return r43_amt_statement_adj_bwp;
		}

		public void setR43_amt_statement_adj_bwp(BigDecimal r43_amt_statement_adj_bwp) {
			this.r43_amt_statement_adj_bwp = r43_amt_statement_adj_bwp;
		}

		public BigDecimal getR43_net_amt() {
			return r43_net_amt;
		}

		public void setR43_net_amt(BigDecimal r43_net_amt) {
			this.r43_net_amt = r43_net_amt;
		}

		public BigDecimal getR43_net_amt_bwp() {
			return r43_net_amt_bwp;
		}

		public void setR43_net_amt_bwp(BigDecimal r43_net_amt_bwp) {
			this.r43_net_amt_bwp = r43_net_amt_bwp;
		}

		public BigDecimal getR43_bal_sub() {
			return r43_bal_sub;
		}

		public void setR43_bal_sub(BigDecimal r43_bal_sub) {
			this.r43_bal_sub = r43_bal_sub;
		}

		public BigDecimal getR43_bal_sub_bwp() {
			return r43_bal_sub_bwp;
		}

		public void setR43_bal_sub_bwp(BigDecimal r43_bal_sub_bwp) {
			this.r43_bal_sub_bwp = r43_bal_sub_bwp;
		}

		public BigDecimal getR43_bal_sub_diaries_bwp() {
			return r43_bal_sub_diaries_bwp;
		}

		public void setR43_bal_sub_diaries_bwp(BigDecimal r43_bal_sub_diaries_bwp) {
			this.r43_bal_sub_diaries_bwp = r43_bal_sub_diaries_bwp;
		}

		public String getR48_operating_expenses() {
			return r48_operating_expenses;
		}

		public void setR48_operating_expenses(String r48_operating_expenses) {
			this.r48_operating_expenses = r48_operating_expenses;
		}

		public BigDecimal getR48_fig_bal_sheet() {
			return r48_fig_bal_sheet;
		}

		public void setR48_fig_bal_sheet(BigDecimal r48_fig_bal_sheet) {
			this.r48_fig_bal_sheet = r48_fig_bal_sheet;
		}

		public BigDecimal getR48_fig_bal_sheet_bwp() {
			return r48_fig_bal_sheet_bwp;
		}

		public void setR48_fig_bal_sheet_bwp(BigDecimal r48_fig_bal_sheet_bwp) {
			this.r48_fig_bal_sheet_bwp = r48_fig_bal_sheet_bwp;
		}

		public BigDecimal getR48_amt_statement_adj() {
			return r48_amt_statement_adj;
		}

		public void setR48_amt_statement_adj(BigDecimal r48_amt_statement_adj) {
			this.r48_amt_statement_adj = r48_amt_statement_adj;
		}

		public BigDecimal getR48_amt_statement_adj_bwp() {
			return r48_amt_statement_adj_bwp;
		}

		public void setR48_amt_statement_adj_bwp(BigDecimal r48_amt_statement_adj_bwp) {
			this.r48_amt_statement_adj_bwp = r48_amt_statement_adj_bwp;
		}

		public BigDecimal getR48_net_amt() {
			return r48_net_amt;
		}

		public void setR48_net_amt(BigDecimal r48_net_amt) {
			this.r48_net_amt = r48_net_amt;
		}

		public BigDecimal getR48_net_amt_bwp() {
			return r48_net_amt_bwp;
		}

		public void setR48_net_amt_bwp(BigDecimal r48_net_amt_bwp) {
			this.r48_net_amt_bwp = r48_net_amt_bwp;
		}

		public BigDecimal getR48_bal_sub() {
			return r48_bal_sub;
		}

		public void setR48_bal_sub(BigDecimal r48_bal_sub) {
			this.r48_bal_sub = r48_bal_sub;
		}

		public BigDecimal getR48_bal_sub_bwp() {
			return r48_bal_sub_bwp;
		}

		public void setR48_bal_sub_bwp(BigDecimal r48_bal_sub_bwp) {
			this.r48_bal_sub_bwp = r48_bal_sub_bwp;
		}

		public BigDecimal getR48_bal_sub_diaries_bwp() {
			return r48_bal_sub_diaries_bwp;
		}

		public void setR48_bal_sub_diaries_bwp(BigDecimal r48_bal_sub_diaries_bwp) {
			this.r48_bal_sub_diaries_bwp = r48_bal_sub_diaries_bwp;
		}

		public String getR49_operating_expenses() {
			return r49_operating_expenses;
		}

		public void setR49_operating_expenses(String r49_operating_expenses) {
			this.r49_operating_expenses = r49_operating_expenses;
		}

		public BigDecimal getR49_fig_bal_sheet() {
			return r49_fig_bal_sheet;
		}

		public void setR49_fig_bal_sheet(BigDecimal r49_fig_bal_sheet) {
			this.r49_fig_bal_sheet = r49_fig_bal_sheet;
		}

		public BigDecimal getR49_fig_bal_sheet_bwp() {
			return r49_fig_bal_sheet_bwp;
		}

		public void setR49_fig_bal_sheet_bwp(BigDecimal r49_fig_bal_sheet_bwp) {
			this.r49_fig_bal_sheet_bwp = r49_fig_bal_sheet_bwp;
		}

		public BigDecimal getR49_amt_statement_adj() {
			return r49_amt_statement_adj;
		}

		public void setR49_amt_statement_adj(BigDecimal r49_amt_statement_adj) {
			this.r49_amt_statement_adj = r49_amt_statement_adj;
		}

		public BigDecimal getR49_amt_statement_adj_bwp() {
			return r49_amt_statement_adj_bwp;
		}

		public void setR49_amt_statement_adj_bwp(BigDecimal r49_amt_statement_adj_bwp) {
			this.r49_amt_statement_adj_bwp = r49_amt_statement_adj_bwp;
		}

		public BigDecimal getR49_net_amt() {
			return r49_net_amt;
		}

		public void setR49_net_amt(BigDecimal r49_net_amt) {
			this.r49_net_amt = r49_net_amt;
		}

		public BigDecimal getR49_net_amt_bwp() {
			return r49_net_amt_bwp;
		}

		public void setR49_net_amt_bwp(BigDecimal r49_net_amt_bwp) {
			this.r49_net_amt_bwp = r49_net_amt_bwp;
		}

		public BigDecimal getR49_bal_sub() {
			return r49_bal_sub;
		}

		public void setR49_bal_sub(BigDecimal r49_bal_sub) {
			this.r49_bal_sub = r49_bal_sub;
		}

		public BigDecimal getR49_bal_sub_bwp() {
			return r49_bal_sub_bwp;
		}

		public void setR49_bal_sub_bwp(BigDecimal r49_bal_sub_bwp) {
			this.r49_bal_sub_bwp = r49_bal_sub_bwp;
		}

		public BigDecimal getR49_bal_sub_diaries_bwp() {
			return r49_bal_sub_diaries_bwp;
		}

		public void setR49_bal_sub_diaries_bwp(BigDecimal r49_bal_sub_diaries_bwp) {
			this.r49_bal_sub_diaries_bwp = r49_bal_sub_diaries_bwp;
		}

		public String getR50_operating_expenses() {
			return r50_operating_expenses;
		}

		public void setR50_operating_expenses(String r50_operating_expenses) {
			this.r50_operating_expenses = r50_operating_expenses;
		}

		public BigDecimal getR50_fig_bal_sheet() {
			return r50_fig_bal_sheet;
		}

		public void setR50_fig_bal_sheet(BigDecimal r50_fig_bal_sheet) {
			this.r50_fig_bal_sheet = r50_fig_bal_sheet;
		}

		public BigDecimal getR50_fig_bal_sheet_bwp() {
			return r50_fig_bal_sheet_bwp;
		}

		public void setR50_fig_bal_sheet_bwp(BigDecimal r50_fig_bal_sheet_bwp) {
			this.r50_fig_bal_sheet_bwp = r50_fig_bal_sheet_bwp;
		}

		public BigDecimal getR50_amt_statement_adj() {
			return r50_amt_statement_adj;
		}

		public void setR50_amt_statement_adj(BigDecimal r50_amt_statement_adj) {
			this.r50_amt_statement_adj = r50_amt_statement_adj;
		}

		public BigDecimal getR50_amt_statement_adj_bwp() {
			return r50_amt_statement_adj_bwp;
		}

		public void setR50_amt_statement_adj_bwp(BigDecimal r50_amt_statement_adj_bwp) {
			this.r50_amt_statement_adj_bwp = r50_amt_statement_adj_bwp;
		}

		public BigDecimal getR50_net_amt() {
			return r50_net_amt;
		}

		public void setR50_net_amt(BigDecimal r50_net_amt) {
			this.r50_net_amt = r50_net_amt;
		}

		public BigDecimal getR50_net_amt_bwp() {
			return r50_net_amt_bwp;
		}

		public void setR50_net_amt_bwp(BigDecimal r50_net_amt_bwp) {
			this.r50_net_amt_bwp = r50_net_amt_bwp;
		}

		public BigDecimal getR50_bal_sub() {
			return r50_bal_sub;
		}

		public void setR50_bal_sub(BigDecimal r50_bal_sub) {
			this.r50_bal_sub = r50_bal_sub;
		}

		public BigDecimal getR50_bal_sub_bwp() {
			return r50_bal_sub_bwp;
		}

		public void setR50_bal_sub_bwp(BigDecimal r50_bal_sub_bwp) {
			this.r50_bal_sub_bwp = r50_bal_sub_bwp;
		}

		public BigDecimal getR50_bal_sub_diaries_bwp() {
			return r50_bal_sub_diaries_bwp;
		}

		public void setR50_bal_sub_diaries_bwp(BigDecimal r50_bal_sub_diaries_bwp) {
			this.r50_bal_sub_diaries_bwp = r50_bal_sub_diaries_bwp;
		}

		public String getR51_operating_expenses() {
			return r51_operating_expenses;
		}

		public void setR51_operating_expenses(String r51_operating_expenses) {
			this.r51_operating_expenses = r51_operating_expenses;
		}

		public BigDecimal getR51_fig_bal_sheet() {
			return r51_fig_bal_sheet;
		}

		public void setR51_fig_bal_sheet(BigDecimal r51_fig_bal_sheet) {
			this.r51_fig_bal_sheet = r51_fig_bal_sheet;
		}

		public BigDecimal getR51_fig_bal_sheet_bwp() {
			return r51_fig_bal_sheet_bwp;
		}

		public void setR51_fig_bal_sheet_bwp(BigDecimal r51_fig_bal_sheet_bwp) {
			this.r51_fig_bal_sheet_bwp = r51_fig_bal_sheet_bwp;
		}

		public BigDecimal getR51_amt_statement_adj() {
			return r51_amt_statement_adj;
		}

		public void setR51_amt_statement_adj(BigDecimal r51_amt_statement_adj) {
			this.r51_amt_statement_adj = r51_amt_statement_adj;
		}

		public BigDecimal getR51_amt_statement_adj_bwp() {
			return r51_amt_statement_adj_bwp;
		}

		public void setR51_amt_statement_adj_bwp(BigDecimal r51_amt_statement_adj_bwp) {
			this.r51_amt_statement_adj_bwp = r51_amt_statement_adj_bwp;
		}

		public BigDecimal getR51_net_amt() {
			return r51_net_amt;
		}

		public void setR51_net_amt(BigDecimal r51_net_amt) {
			this.r51_net_amt = r51_net_amt;
		}

		public BigDecimal getR51_net_amt_bwp() {
			return r51_net_amt_bwp;
		}

		public void setR51_net_amt_bwp(BigDecimal r51_net_amt_bwp) {
			this.r51_net_amt_bwp = r51_net_amt_bwp;
		}

		public BigDecimal getR51_bal_sub() {
			return r51_bal_sub;
		}

		public void setR51_bal_sub(BigDecimal r51_bal_sub) {
			this.r51_bal_sub = r51_bal_sub;
		}

		public BigDecimal getR51_bal_sub_bwp() {
			return r51_bal_sub_bwp;
		}

		public void setR51_bal_sub_bwp(BigDecimal r51_bal_sub_bwp) {
			this.r51_bal_sub_bwp = r51_bal_sub_bwp;
		}

		public BigDecimal getR51_bal_sub_diaries_bwp() {
			return r51_bal_sub_diaries_bwp;
		}

		public void setR51_bal_sub_diaries_bwp(BigDecimal r51_bal_sub_diaries_bwp) {
			this.r51_bal_sub_diaries_bwp = r51_bal_sub_diaries_bwp;
		}

		public String getR52_operating_expenses() {
			return r52_operating_expenses;
		}

		public void setR52_operating_expenses(String r52_operating_expenses) {
			this.r52_operating_expenses = r52_operating_expenses;
		}

		public BigDecimal getR52_fig_bal_sheet() {
			return r52_fig_bal_sheet;
		}

		public void setR52_fig_bal_sheet(BigDecimal r52_fig_bal_sheet) {
			this.r52_fig_bal_sheet = r52_fig_bal_sheet;
		}

		public BigDecimal getR52_fig_bal_sheet_bwp() {
			return r52_fig_bal_sheet_bwp;
		}

		public void setR52_fig_bal_sheet_bwp(BigDecimal r52_fig_bal_sheet_bwp) {
			this.r52_fig_bal_sheet_bwp = r52_fig_bal_sheet_bwp;
		}

		public BigDecimal getR52_amt_statement_adj() {
			return r52_amt_statement_adj;
		}

		public void setR52_amt_statement_adj(BigDecimal r52_amt_statement_adj) {
			this.r52_amt_statement_adj = r52_amt_statement_adj;
		}

		public BigDecimal getR52_amt_statement_adj_bwp() {
			return r52_amt_statement_adj_bwp;
		}

		public void setR52_amt_statement_adj_bwp(BigDecimal r52_amt_statement_adj_bwp) {
			this.r52_amt_statement_adj_bwp = r52_amt_statement_adj_bwp;
		}

		public BigDecimal getR52_net_amt() {
			return r52_net_amt;
		}

		public void setR52_net_amt(BigDecimal r52_net_amt) {
			this.r52_net_amt = r52_net_amt;
		}

		public BigDecimal getR52_net_amt_bwp() {
			return r52_net_amt_bwp;
		}

		public void setR52_net_amt_bwp(BigDecimal r52_net_amt_bwp) {
			this.r52_net_amt_bwp = r52_net_amt_bwp;
		}

		public BigDecimal getR52_bal_sub() {
			return r52_bal_sub;
		}

		public void setR52_bal_sub(BigDecimal r52_bal_sub) {
			this.r52_bal_sub = r52_bal_sub;
		}

		public BigDecimal getR52_bal_sub_bwp() {
			return r52_bal_sub_bwp;
		}

		public void setR52_bal_sub_bwp(BigDecimal r52_bal_sub_bwp) {
			this.r52_bal_sub_bwp = r52_bal_sub_bwp;
		}

		public BigDecimal getR52_bal_sub_diaries_bwp() {
			return r52_bal_sub_diaries_bwp;
		}

		public void setR52_bal_sub_diaries_bwp(BigDecimal r52_bal_sub_diaries_bwp) {
			this.r52_bal_sub_diaries_bwp = r52_bal_sub_diaries_bwp;
		}

		public String getR53_operating_expenses() {
			return r53_operating_expenses;
		}

		public void setR53_operating_expenses(String r53_operating_expenses) {
			this.r53_operating_expenses = r53_operating_expenses;
		}

		public BigDecimal getR53_fig_bal_sheet() {
			return r53_fig_bal_sheet;
		}

		public void setR53_fig_bal_sheet(BigDecimal r53_fig_bal_sheet) {
			this.r53_fig_bal_sheet = r53_fig_bal_sheet;
		}

		public BigDecimal getR53_fig_bal_sheet_bwp() {
			return r53_fig_bal_sheet_bwp;
		}

		public void setR53_fig_bal_sheet_bwp(BigDecimal r53_fig_bal_sheet_bwp) {
			this.r53_fig_bal_sheet_bwp = r53_fig_bal_sheet_bwp;
		}

		public BigDecimal getR53_amt_statement_adj() {
			return r53_amt_statement_adj;
		}

		public void setR53_amt_statement_adj(BigDecimal r53_amt_statement_adj) {
			this.r53_amt_statement_adj = r53_amt_statement_adj;
		}

		public BigDecimal getR53_amt_statement_adj_bwp() {
			return r53_amt_statement_adj_bwp;
		}

		public void setR53_amt_statement_adj_bwp(BigDecimal r53_amt_statement_adj_bwp) {
			this.r53_amt_statement_adj_bwp = r53_amt_statement_adj_bwp;
		}

		public BigDecimal getR53_net_amt() {
			return r53_net_amt;
		}

		public void setR53_net_amt(BigDecimal r53_net_amt) {
			this.r53_net_amt = r53_net_amt;
		}

		public BigDecimal getR53_net_amt_bwp() {
			return r53_net_amt_bwp;
		}

		public void setR53_net_amt_bwp(BigDecimal r53_net_amt_bwp) {
			this.r53_net_amt_bwp = r53_net_amt_bwp;
		}

		public BigDecimal getR53_bal_sub() {
			return r53_bal_sub;
		}

		public void setR53_bal_sub(BigDecimal r53_bal_sub) {
			this.r53_bal_sub = r53_bal_sub;
		}

		public BigDecimal getR53_bal_sub_bwp() {
			return r53_bal_sub_bwp;
		}

		public void setR53_bal_sub_bwp(BigDecimal r53_bal_sub_bwp) {
			this.r53_bal_sub_bwp = r53_bal_sub_bwp;
		}

		public BigDecimal getR53_bal_sub_diaries_bwp() {
			return r53_bal_sub_diaries_bwp;
		}

		public void setR53_bal_sub_diaries_bwp(BigDecimal r53_bal_sub_diaries_bwp) {
			this.r53_bal_sub_diaries_bwp = r53_bal_sub_diaries_bwp;
		}

		public String getR54_operating_expenses() {
			return r54_operating_expenses;
		}

		public void setR54_operating_expenses(String r54_operating_expenses) {
			this.r54_operating_expenses = r54_operating_expenses;
		}

		public BigDecimal getR54_fig_bal_sheet() {
			return r54_fig_bal_sheet;
		}

		public void setR54_fig_bal_sheet(BigDecimal r54_fig_bal_sheet) {
			this.r54_fig_bal_sheet = r54_fig_bal_sheet;
		}

		public BigDecimal getR54_fig_bal_sheet_bwp() {
			return r54_fig_bal_sheet_bwp;
		}

		public void setR54_fig_bal_sheet_bwp(BigDecimal r54_fig_bal_sheet_bwp) {
			this.r54_fig_bal_sheet_bwp = r54_fig_bal_sheet_bwp;
		}

		public BigDecimal getR54_amt_statement_adj() {
			return r54_amt_statement_adj;
		}

		public void setR54_amt_statement_adj(BigDecimal r54_amt_statement_adj) {
			this.r54_amt_statement_adj = r54_amt_statement_adj;
		}

		public BigDecimal getR54_amt_statement_adj_bwp() {
			return r54_amt_statement_adj_bwp;
		}

		public void setR54_amt_statement_adj_bwp(BigDecimal r54_amt_statement_adj_bwp) {
			this.r54_amt_statement_adj_bwp = r54_amt_statement_adj_bwp;
		}

		public BigDecimal getR54_net_amt() {
			return r54_net_amt;
		}

		public void setR54_net_amt(BigDecimal r54_net_amt) {
			this.r54_net_amt = r54_net_amt;
		}

		public BigDecimal getR54_net_amt_bwp() {
			return r54_net_amt_bwp;
		}

		public void setR54_net_amt_bwp(BigDecimal r54_net_amt_bwp) {
			this.r54_net_amt_bwp = r54_net_amt_bwp;
		}

		public BigDecimal getR54_bal_sub() {
			return r54_bal_sub;
		}

		public void setR54_bal_sub(BigDecimal r54_bal_sub) {
			this.r54_bal_sub = r54_bal_sub;
		}

		public BigDecimal getR54_bal_sub_bwp() {
			return r54_bal_sub_bwp;
		}

		public void setR54_bal_sub_bwp(BigDecimal r54_bal_sub_bwp) {
			this.r54_bal_sub_bwp = r54_bal_sub_bwp;
		}

		public BigDecimal getR54_bal_sub_diaries_bwp() {
			return r54_bal_sub_diaries_bwp;
		}

		public void setR54_bal_sub_diaries_bwp(BigDecimal r54_bal_sub_diaries_bwp) {
			this.r54_bal_sub_diaries_bwp = r54_bal_sub_diaries_bwp;
		}

		public String getR55_operating_expenses() {
			return r55_operating_expenses;
		}

		public void setR55_operating_expenses(String r55_operating_expenses) {
			this.r55_operating_expenses = r55_operating_expenses;
		}

		public BigDecimal getR55_fig_bal_sheet() {
			return r55_fig_bal_sheet;
		}

		public void setR55_fig_bal_sheet(BigDecimal r55_fig_bal_sheet) {
			this.r55_fig_bal_sheet = r55_fig_bal_sheet;
		}

		public BigDecimal getR55_fig_bal_sheet_bwp() {
			return r55_fig_bal_sheet_bwp;
		}

		public void setR55_fig_bal_sheet_bwp(BigDecimal r55_fig_bal_sheet_bwp) {
			this.r55_fig_bal_sheet_bwp = r55_fig_bal_sheet_bwp;
		}

		public BigDecimal getR55_amt_statement_adj() {
			return r55_amt_statement_adj;
		}

		public void setR55_amt_statement_adj(BigDecimal r55_amt_statement_adj) {
			this.r55_amt_statement_adj = r55_amt_statement_adj;
		}

		public BigDecimal getR55_amt_statement_adj_bwp() {
			return r55_amt_statement_adj_bwp;
		}

		public void setR55_amt_statement_adj_bwp(BigDecimal r55_amt_statement_adj_bwp) {
			this.r55_amt_statement_adj_bwp = r55_amt_statement_adj_bwp;
		}

		public BigDecimal getR55_net_amt() {
			return r55_net_amt;
		}

		public void setR55_net_amt(BigDecimal r55_net_amt) {
			this.r55_net_amt = r55_net_amt;
		}

		public BigDecimal getR55_net_amt_bwp() {
			return r55_net_amt_bwp;
		}

		public void setR55_net_amt_bwp(BigDecimal r55_net_amt_bwp) {
			this.r55_net_amt_bwp = r55_net_amt_bwp;
		}

		public BigDecimal getR55_bal_sub() {
			return r55_bal_sub;
		}

		public void setR55_bal_sub(BigDecimal r55_bal_sub) {
			this.r55_bal_sub = r55_bal_sub;
		}

		public BigDecimal getR55_bal_sub_bwp() {
			return r55_bal_sub_bwp;
		}

		public void setR55_bal_sub_bwp(BigDecimal r55_bal_sub_bwp) {
			this.r55_bal_sub_bwp = r55_bal_sub_bwp;
		}

		public BigDecimal getR55_bal_sub_diaries_bwp() {
			return r55_bal_sub_diaries_bwp;
		}

		public void setR55_bal_sub_diaries_bwp(BigDecimal r55_bal_sub_diaries_bwp) {
			this.r55_bal_sub_diaries_bwp = r55_bal_sub_diaries_bwp;
		}

		public String getR56_operating_expenses() {
			return r56_operating_expenses;
		}

		public void setR56_operating_expenses(String r56_operating_expenses) {
			this.r56_operating_expenses = r56_operating_expenses;
		}

		public BigDecimal getR56_fig_bal_sheet() {
			return r56_fig_bal_sheet;
		}

		public void setR56_fig_bal_sheet(BigDecimal r56_fig_bal_sheet) {
			this.r56_fig_bal_sheet = r56_fig_bal_sheet;
		}

		public BigDecimal getR56_fig_bal_sheet_bwp() {
			return r56_fig_bal_sheet_bwp;
		}

		public void setR56_fig_bal_sheet_bwp(BigDecimal r56_fig_bal_sheet_bwp) {
			this.r56_fig_bal_sheet_bwp = r56_fig_bal_sheet_bwp;
		}

		public BigDecimal getR56_amt_statement_adj() {
			return r56_amt_statement_adj;
		}

		public void setR56_amt_statement_adj(BigDecimal r56_amt_statement_adj) {
			this.r56_amt_statement_adj = r56_amt_statement_adj;
		}

		public BigDecimal getR56_amt_statement_adj_bwp() {
			return r56_amt_statement_adj_bwp;
		}

		public void setR56_amt_statement_adj_bwp(BigDecimal r56_amt_statement_adj_bwp) {
			this.r56_amt_statement_adj_bwp = r56_amt_statement_adj_bwp;
		}

		public BigDecimal getR56_net_amt() {
			return r56_net_amt;
		}

		public void setR56_net_amt(BigDecimal r56_net_amt) {
			this.r56_net_amt = r56_net_amt;
		}

		public BigDecimal getR56_net_amt_bwp() {
			return r56_net_amt_bwp;
		}

		public void setR56_net_amt_bwp(BigDecimal r56_net_amt_bwp) {
			this.r56_net_amt_bwp = r56_net_amt_bwp;
		}

		public BigDecimal getR56_bal_sub() {
			return r56_bal_sub;
		}

		public void setR56_bal_sub(BigDecimal r56_bal_sub) {
			this.r56_bal_sub = r56_bal_sub;
		}

		public BigDecimal getR56_bal_sub_bwp() {
			return r56_bal_sub_bwp;
		}

		public void setR56_bal_sub_bwp(BigDecimal r56_bal_sub_bwp) {
			this.r56_bal_sub_bwp = r56_bal_sub_bwp;
		}

		public BigDecimal getR56_bal_sub_diaries_bwp() {
			return r56_bal_sub_diaries_bwp;
		}

		public void setR56_bal_sub_diaries_bwp(BigDecimal r56_bal_sub_diaries_bwp) {
			this.r56_bal_sub_diaries_bwp = r56_bal_sub_diaries_bwp;
		}

		public String getR57_operating_expenses() {
			return r57_operating_expenses;
		}

		public void setR57_operating_expenses(String r57_operating_expenses) {
			this.r57_operating_expenses = r57_operating_expenses;
		}

		public BigDecimal getR57_fig_bal_sheet() {
			return r57_fig_bal_sheet;
		}

		public void setR57_fig_bal_sheet(BigDecimal r57_fig_bal_sheet) {
			this.r57_fig_bal_sheet = r57_fig_bal_sheet;
		}

		public BigDecimal getR57_fig_bal_sheet_bwp() {
			return r57_fig_bal_sheet_bwp;
		}

		public void setR57_fig_bal_sheet_bwp(BigDecimal r57_fig_bal_sheet_bwp) {
			this.r57_fig_bal_sheet_bwp = r57_fig_bal_sheet_bwp;
		}

		public BigDecimal getR57_amt_statement_adj() {
			return r57_amt_statement_adj;
		}

		public void setR57_amt_statement_adj(BigDecimal r57_amt_statement_adj) {
			this.r57_amt_statement_adj = r57_amt_statement_adj;
		}

		public BigDecimal getR57_amt_statement_adj_bwp() {
			return r57_amt_statement_adj_bwp;
		}

		public void setR57_amt_statement_adj_bwp(BigDecimal r57_amt_statement_adj_bwp) {
			this.r57_amt_statement_adj_bwp = r57_amt_statement_adj_bwp;
		}

		public BigDecimal getR57_net_amt() {
			return r57_net_amt;
		}

		public void setR57_net_amt(BigDecimal r57_net_amt) {
			this.r57_net_amt = r57_net_amt;
		}

		public BigDecimal getR57_net_amt_bwp() {
			return r57_net_amt_bwp;
		}

		public void setR57_net_amt_bwp(BigDecimal r57_net_amt_bwp) {
			this.r57_net_amt_bwp = r57_net_amt_bwp;
		}

		public BigDecimal getR57_bal_sub() {
			return r57_bal_sub;
		}

		public void setR57_bal_sub(BigDecimal r57_bal_sub) {
			this.r57_bal_sub = r57_bal_sub;
		}

		public BigDecimal getR57_bal_sub_bwp() {
			return r57_bal_sub_bwp;
		}

		public void setR57_bal_sub_bwp(BigDecimal r57_bal_sub_bwp) {
			this.r57_bal_sub_bwp = r57_bal_sub_bwp;
		}

		public BigDecimal getR57_bal_sub_diaries_bwp() {
			return r57_bal_sub_diaries_bwp;
		}

		public void setR57_bal_sub_diaries_bwp(BigDecimal r57_bal_sub_diaries_bwp) {
			this.r57_bal_sub_diaries_bwp = r57_bal_sub_diaries_bwp;
		}

		public String getR58_operating_expenses() {
			return r58_operating_expenses;
		}

		public void setR58_operating_expenses(String r58_operating_expenses) {
			this.r58_operating_expenses = r58_operating_expenses;
		}

		public BigDecimal getR58_fig_bal_sheet() {
			return r58_fig_bal_sheet;
		}

		public void setR58_fig_bal_sheet(BigDecimal r58_fig_bal_sheet) {
			this.r58_fig_bal_sheet = r58_fig_bal_sheet;
		}

		public BigDecimal getR58_fig_bal_sheet_bwp() {
			return r58_fig_bal_sheet_bwp;
		}

		public void setR58_fig_bal_sheet_bwp(BigDecimal r58_fig_bal_sheet_bwp) {
			this.r58_fig_bal_sheet_bwp = r58_fig_bal_sheet_bwp;
		}

		public BigDecimal getR58_amt_statement_adj() {
			return r58_amt_statement_adj;
		}

		public void setR58_amt_statement_adj(BigDecimal r58_amt_statement_adj) {
			this.r58_amt_statement_adj = r58_amt_statement_adj;
		}

		public BigDecimal getR58_amt_statement_adj_bwp() {
			return r58_amt_statement_adj_bwp;
		}

		public void setR58_amt_statement_adj_bwp(BigDecimal r58_amt_statement_adj_bwp) {
			this.r58_amt_statement_adj_bwp = r58_amt_statement_adj_bwp;
		}

		public BigDecimal getR58_net_amt() {
			return r58_net_amt;
		}

		public void setR58_net_amt(BigDecimal r58_net_amt) {
			this.r58_net_amt = r58_net_amt;
		}

		public BigDecimal getR58_net_amt_bwp() {
			return r58_net_amt_bwp;
		}

		public void setR58_net_amt_bwp(BigDecimal r58_net_amt_bwp) {
			this.r58_net_amt_bwp = r58_net_amt_bwp;
		}

		public BigDecimal getR58_bal_sub() {
			return r58_bal_sub;
		}

		public void setR58_bal_sub(BigDecimal r58_bal_sub) {
			this.r58_bal_sub = r58_bal_sub;
		}

		public BigDecimal getR58_bal_sub_bwp() {
			return r58_bal_sub_bwp;
		}

		public void setR58_bal_sub_bwp(BigDecimal r58_bal_sub_bwp) {
			this.r58_bal_sub_bwp = r58_bal_sub_bwp;
		}

		public BigDecimal getR58_bal_sub_diaries_bwp() {
			return r58_bal_sub_diaries_bwp;
		}

		public void setR58_bal_sub_diaries_bwp(BigDecimal r58_bal_sub_diaries_bwp) {
			this.r58_bal_sub_diaries_bwp = r58_bal_sub_diaries_bwp;
		}

		public String getR59_operating_expenses() {
			return r59_operating_expenses;
		}

		public void setR59_operating_expenses(String r59_operating_expenses) {
			this.r59_operating_expenses = r59_operating_expenses;
		}

		public BigDecimal getR59_fig_bal_sheet() {
			return r59_fig_bal_sheet;
		}

		public void setR59_fig_bal_sheet(BigDecimal r59_fig_bal_sheet) {
			this.r59_fig_bal_sheet = r59_fig_bal_sheet;
		}

		public BigDecimal getR59_fig_bal_sheet_bwp() {
			return r59_fig_bal_sheet_bwp;
		}

		public void setR59_fig_bal_sheet_bwp(BigDecimal r59_fig_bal_sheet_bwp) {
			this.r59_fig_bal_sheet_bwp = r59_fig_bal_sheet_bwp;
		}

		public BigDecimal getR59_amt_statement_adj() {
			return r59_amt_statement_adj;
		}

		public void setR59_amt_statement_adj(BigDecimal r59_amt_statement_adj) {
			this.r59_amt_statement_adj = r59_amt_statement_adj;
		}

		public BigDecimal getR59_amt_statement_adj_bwp() {
			return r59_amt_statement_adj_bwp;
		}

		public void setR59_amt_statement_adj_bwp(BigDecimal r59_amt_statement_adj_bwp) {
			this.r59_amt_statement_adj_bwp = r59_amt_statement_adj_bwp;
		}

		public BigDecimal getR59_net_amt() {
			return r59_net_amt;
		}

		public void setR59_net_amt(BigDecimal r59_net_amt) {
			this.r59_net_amt = r59_net_amt;
		}

		public BigDecimal getR59_net_amt_bwp() {
			return r59_net_amt_bwp;
		}

		public void setR59_net_amt_bwp(BigDecimal r59_net_amt_bwp) {
			this.r59_net_amt_bwp = r59_net_amt_bwp;
		}

		public BigDecimal getR59_bal_sub() {
			return r59_bal_sub;
		}

		public void setR59_bal_sub(BigDecimal r59_bal_sub) {
			this.r59_bal_sub = r59_bal_sub;
		}

		public BigDecimal getR59_bal_sub_bwp() {
			return r59_bal_sub_bwp;
		}

		public void setR59_bal_sub_bwp(BigDecimal r59_bal_sub_bwp) {
			this.r59_bal_sub_bwp = r59_bal_sub_bwp;
		}

		public BigDecimal getR59_bal_sub_diaries_bwp() {
			return r59_bal_sub_diaries_bwp;
		}

		public void setR59_bal_sub_diaries_bwp(BigDecimal r59_bal_sub_diaries_bwp) {
			this.r59_bal_sub_diaries_bwp = r59_bal_sub_diaries_bwp;
		}

		public String getR60_operating_expenses() {
			return r60_operating_expenses;
		}

		public void setR60_operating_expenses(String r60_operating_expenses) {
			this.r60_operating_expenses = r60_operating_expenses;
		}

		public BigDecimal getR60_fig_bal_sheet() {
			return r60_fig_bal_sheet;
		}

		public void setR60_fig_bal_sheet(BigDecimal r60_fig_bal_sheet) {
			this.r60_fig_bal_sheet = r60_fig_bal_sheet;
		}

		public BigDecimal getR60_fig_bal_sheet_bwp() {
			return r60_fig_bal_sheet_bwp;
		}

		public void setR60_fig_bal_sheet_bwp(BigDecimal r60_fig_bal_sheet_bwp) {
			this.r60_fig_bal_sheet_bwp = r60_fig_bal_sheet_bwp;
		}

		public BigDecimal getR60_amt_statement_adj() {
			return r60_amt_statement_adj;
		}

		public void setR60_amt_statement_adj(BigDecimal r60_amt_statement_adj) {
			this.r60_amt_statement_adj = r60_amt_statement_adj;
		}

		public BigDecimal getR60_amt_statement_adj_bwp() {
			return r60_amt_statement_adj_bwp;
		}

		public void setR60_amt_statement_adj_bwp(BigDecimal r60_amt_statement_adj_bwp) {
			this.r60_amt_statement_adj_bwp = r60_amt_statement_adj_bwp;
		}

		public BigDecimal getR60_net_amt() {
			return r60_net_amt;
		}

		public void setR60_net_amt(BigDecimal r60_net_amt) {
			this.r60_net_amt = r60_net_amt;
		}

		public BigDecimal getR60_net_amt_bwp() {
			return r60_net_amt_bwp;
		}

		public void setR60_net_amt_bwp(BigDecimal r60_net_amt_bwp) {
			this.r60_net_amt_bwp = r60_net_amt_bwp;
		}

		public BigDecimal getR60_bal_sub() {
			return r60_bal_sub;
		}

		public void setR60_bal_sub(BigDecimal r60_bal_sub) {
			this.r60_bal_sub = r60_bal_sub;
		}

		public BigDecimal getR60_bal_sub_bwp() {
			return r60_bal_sub_bwp;
		}

		public void setR60_bal_sub_bwp(BigDecimal r60_bal_sub_bwp) {
			this.r60_bal_sub_bwp = r60_bal_sub_bwp;
		}

		public BigDecimal getR60_bal_sub_diaries_bwp() {
			return r60_bal_sub_diaries_bwp;
		}

		public void setR60_bal_sub_diaries_bwp(BigDecimal r60_bal_sub_diaries_bwp) {
			this.r60_bal_sub_diaries_bwp = r60_bal_sub_diaries_bwp;
		}

		public String getR61_operating_expenses() {
			return r61_operating_expenses;
		}

		public void setR61_operating_expenses(String r61_operating_expenses) {
			this.r61_operating_expenses = r61_operating_expenses;
		}

		public BigDecimal getR61_fig_bal_sheet() {
			return r61_fig_bal_sheet;
		}

		public void setR61_fig_bal_sheet(BigDecimal r61_fig_bal_sheet) {
			this.r61_fig_bal_sheet = r61_fig_bal_sheet;
		}

		public BigDecimal getR61_fig_bal_sheet_bwp() {
			return r61_fig_bal_sheet_bwp;
		}

		public void setR61_fig_bal_sheet_bwp(BigDecimal r61_fig_bal_sheet_bwp) {
			this.r61_fig_bal_sheet_bwp = r61_fig_bal_sheet_bwp;
		}

		public BigDecimal getR61_amt_statement_adj() {
			return r61_amt_statement_adj;
		}

		public void setR61_amt_statement_adj(BigDecimal r61_amt_statement_adj) {
			this.r61_amt_statement_adj = r61_amt_statement_adj;
		}

		public BigDecimal getR61_amt_statement_adj_bwp() {
			return r61_amt_statement_adj_bwp;
		}

		public void setR61_amt_statement_adj_bwp(BigDecimal r61_amt_statement_adj_bwp) {
			this.r61_amt_statement_adj_bwp = r61_amt_statement_adj_bwp;
		}

		public BigDecimal getR61_net_amt() {
			return r61_net_amt;
		}

		public void setR61_net_amt(BigDecimal r61_net_amt) {
			this.r61_net_amt = r61_net_amt;
		}

		public BigDecimal getR61_net_amt_bwp() {
			return r61_net_amt_bwp;
		}

		public void setR61_net_amt_bwp(BigDecimal r61_net_amt_bwp) {
			this.r61_net_amt_bwp = r61_net_amt_bwp;
		}

		public BigDecimal getR61_bal_sub() {
			return r61_bal_sub;
		}

		public void setR61_bal_sub(BigDecimal r61_bal_sub) {
			this.r61_bal_sub = r61_bal_sub;
		}

		public BigDecimal getR61_bal_sub_bwp() {
			return r61_bal_sub_bwp;
		}

		public void setR61_bal_sub_bwp(BigDecimal r61_bal_sub_bwp) {
			this.r61_bal_sub_bwp = r61_bal_sub_bwp;
		}

		public BigDecimal getR61_bal_sub_diaries_bwp() {
			return r61_bal_sub_diaries_bwp;
		}

		public void setR61_bal_sub_diaries_bwp(BigDecimal r61_bal_sub_diaries_bwp) {
			this.r61_bal_sub_diaries_bwp = r61_bal_sub_diaries_bwp;
		}

		public String getR62_operating_expenses() {
			return r62_operating_expenses;
		}

		public void setR62_operating_expenses(String r62_operating_expenses) {
			this.r62_operating_expenses = r62_operating_expenses;
		}

		public BigDecimal getR62_fig_bal_sheet() {
			return r62_fig_bal_sheet;
		}

		public void setR62_fig_bal_sheet(BigDecimal r62_fig_bal_sheet) {
			this.r62_fig_bal_sheet = r62_fig_bal_sheet;
		}

		public BigDecimal getR62_fig_bal_sheet_bwp() {
			return r62_fig_bal_sheet_bwp;
		}

		public void setR62_fig_bal_sheet_bwp(BigDecimal r62_fig_bal_sheet_bwp) {
			this.r62_fig_bal_sheet_bwp = r62_fig_bal_sheet_bwp;
		}

		public BigDecimal getR62_amt_statement_adj() {
			return r62_amt_statement_adj;
		}

		public void setR62_amt_statement_adj(BigDecimal r62_amt_statement_adj) {
			this.r62_amt_statement_adj = r62_amt_statement_adj;
		}

		public BigDecimal getR62_amt_statement_adj_bwp() {
			return r62_amt_statement_adj_bwp;
		}

		public void setR62_amt_statement_adj_bwp(BigDecimal r62_amt_statement_adj_bwp) {
			this.r62_amt_statement_adj_bwp = r62_amt_statement_adj_bwp;
		}

		public BigDecimal getR62_net_amt() {
			return r62_net_amt;
		}

		public void setR62_net_amt(BigDecimal r62_net_amt) {
			this.r62_net_amt = r62_net_amt;
		}

		public BigDecimal getR62_net_amt_bwp() {
			return r62_net_amt_bwp;
		}

		public void setR62_net_amt_bwp(BigDecimal r62_net_amt_bwp) {
			this.r62_net_amt_bwp = r62_net_amt_bwp;
		}

		public BigDecimal getR62_bal_sub() {
			return r62_bal_sub;
		}

		public void setR62_bal_sub(BigDecimal r62_bal_sub) {
			this.r62_bal_sub = r62_bal_sub;
		}

		public BigDecimal getR62_bal_sub_bwp() {
			return r62_bal_sub_bwp;
		}

		public void setR62_bal_sub_bwp(BigDecimal r62_bal_sub_bwp) {
			this.r62_bal_sub_bwp = r62_bal_sub_bwp;
		}

		public BigDecimal getR62_bal_sub_diaries_bwp() {
			return r62_bal_sub_diaries_bwp;
		}

		public void setR62_bal_sub_diaries_bwp(BigDecimal r62_bal_sub_diaries_bwp) {
			this.r62_bal_sub_diaries_bwp = r62_bal_sub_diaries_bwp;
		}

		public String getR63_operating_expenses() {
			return r63_operating_expenses;
		}

		public void setR63_operating_expenses(String r63_operating_expenses) {
			this.r63_operating_expenses = r63_operating_expenses;
		}

		public BigDecimal getR63_fig_bal_sheet() {
			return r63_fig_bal_sheet;
		}

		public void setR63_fig_bal_sheet(BigDecimal r63_fig_bal_sheet) {
			this.r63_fig_bal_sheet = r63_fig_bal_sheet;
		}

		public BigDecimal getR63_fig_bal_sheet_bwp() {
			return r63_fig_bal_sheet_bwp;
		}

		public void setR63_fig_bal_sheet_bwp(BigDecimal r63_fig_bal_sheet_bwp) {
			this.r63_fig_bal_sheet_bwp = r63_fig_bal_sheet_bwp;
		}

		public BigDecimal getR63_amt_statement_adj() {
			return r63_amt_statement_adj;
		}

		public void setR63_amt_statement_adj(BigDecimal r63_amt_statement_adj) {
			this.r63_amt_statement_adj = r63_amt_statement_adj;
		}

		public BigDecimal getR63_amt_statement_adj_bwp() {
			return r63_amt_statement_adj_bwp;
		}

		public void setR63_amt_statement_adj_bwp(BigDecimal r63_amt_statement_adj_bwp) {
			this.r63_amt_statement_adj_bwp = r63_amt_statement_adj_bwp;
		}

		public BigDecimal getR63_net_amt() {
			return r63_net_amt;
		}

		public void setR63_net_amt(BigDecimal r63_net_amt) {
			this.r63_net_amt = r63_net_amt;
		}

		public BigDecimal getR63_net_amt_bwp() {
			return r63_net_amt_bwp;
		}

		public void setR63_net_amt_bwp(BigDecimal r63_net_amt_bwp) {
			this.r63_net_amt_bwp = r63_net_amt_bwp;
		}

		public BigDecimal getR63_bal_sub() {
			return r63_bal_sub;
		}

		public void setR63_bal_sub(BigDecimal r63_bal_sub) {
			this.r63_bal_sub = r63_bal_sub;
		}

		public BigDecimal getR63_bal_sub_bwp() {
			return r63_bal_sub_bwp;
		}

		public void setR63_bal_sub_bwp(BigDecimal r63_bal_sub_bwp) {
			this.r63_bal_sub_bwp = r63_bal_sub_bwp;
		}

		public BigDecimal getR63_bal_sub_diaries_bwp() {
			return r63_bal_sub_diaries_bwp;
		}

		public void setR63_bal_sub_diaries_bwp(BigDecimal r63_bal_sub_diaries_bwp) {
			this.r63_bal_sub_diaries_bwp = r63_bal_sub_diaries_bwp;
		}

		public BigDecimal getR17_bal_sub_diaries() {
			return r17_bal_sub_diaries;
		}

		public void setR17_bal_sub_diaries(BigDecimal r17_bal_sub_diaries) {
			this.r17_bal_sub_diaries = r17_bal_sub_diaries;
		}

		public BigDecimal getR48_bal_sub_diaries() {
			return r48_bal_sub_diaries;
		}

		public void setR48_bal_sub_diaries(BigDecimal r48_bal_sub_diaries) {
			this.r48_bal_sub_diaries = r48_bal_sub_diaries;
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

	class PL_SCHSArchivalRowMapper implements RowMapper<PL_SCHS_Archival_Summary_Entity> {

		@Override
		public PL_SCHS_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			PL_SCHS_Archival_Summary_Entity obj = new PL_SCHS_Archival_Summary_Entity();

			// ================= R9 =================
			obj.setR9_intrest_div(rs.getString("R9_INTREST_DIV"));
			obj.setR9_fig_bal_sheet(rs.getBigDecimal("R9_FIG_BAL_SHEET"));
			obj.setR9_fig_bal_sheet_bwp(rs.getBigDecimal("R9_FIG_BAL_SHEET_BWP"));
			obj.setR9_amt_statement_adj(rs.getBigDecimal("R9_AMT_STATEMENT_ADJ"));
			obj.setR9_amt_statement_adj_bwp(rs.getBigDecimal("R9_AMT_STATEMENT_ADJ_BWP"));
			obj.setR9_net_amt(rs.getBigDecimal("R9_NET_AMT"));
			obj.setR9_net_amt_bwp(rs.getBigDecimal("R9_NET_AMT_BWP"));
			obj.setR9_bal_sub(rs.getBigDecimal("R9_BAL_SUB"));
			obj.setR9_bal_sub_bwp(rs.getBigDecimal("R9_BAL_SUB_BWP"));
			obj.setR9_bal_sub_diaries(rs.getBigDecimal("R9_BAL_SUB_DIARIES"));
			obj.setR9_bal_sub_diaries_bwp(rs.getBigDecimal("R9_BAL_SUB_DIARIES_BWP"));

			// ================= R10 =================
			obj.setR10_intrest_div(rs.getString("R10_INTREST_DIV"));
			obj.setR10_fig_bal_sheet(rs.getBigDecimal("R10_FIG_BAL_SHEET"));
			obj.setR10_fig_bal_sheet_bwp(rs.getBigDecimal("R10_FIG_BAL_SHEET_BWP"));
			obj.setR10_amt_statement_adj(rs.getBigDecimal("R10_AMT_STATEMENT_ADJ"));
			obj.setR10_amt_statement_adj_bwp(rs.getBigDecimal("R10_AMT_STATEMENT_ADJ_BWP"));
			obj.setR10_net_amt(rs.getBigDecimal("R10_NET_AMT"));
			obj.setR10_net_amt_bwp(rs.getBigDecimal("R10_NET_AMT_BWP"));
			obj.setR10_bal_sub(rs.getBigDecimal("R10_BAL_SUB"));
			obj.setR10_bal_sub_bwp(rs.getBigDecimal("R10_BAL_SUB_BWP"));
			obj.setR10_bal_sub_diaries(rs.getBigDecimal("R10_BAL_SUB_DIARIES"));
			obj.setR10_bal_sub_diaries_bwp(rs.getBigDecimal("R10_BAL_SUB_DIARIES_BWP"));

			// ================= R11 =================
			obj.setR11_intrest_div(rs.getString("R11_INTREST_DIV"));
			obj.setR11_fig_bal_sheet(rs.getBigDecimal("R11_FIG_BAL_SHEET"));
			obj.setR11_fig_bal_sheet_bwp(rs.getBigDecimal("R11_FIG_BAL_SHEET_BWP"));
			obj.setR11_amt_statement_adj(rs.getBigDecimal("R11_AMT_STATEMENT_ADJ"));
			obj.setR11_amt_statement_adj_bwp(rs.getBigDecimal("R11_AMT_STATEMENT_ADJ_BWP"));
			obj.setR11_net_amt(rs.getBigDecimal("R11_NET_AMT"));
			obj.setR11_net_amt_bwp(rs.getBigDecimal("R11_NET_AMT_BWP"));
			obj.setR11_bal_sub(rs.getBigDecimal("R11_BAL_SUB"));
			obj.setR11_bal_sub_bwp(rs.getBigDecimal("R11_BAL_SUB_BWP"));
			obj.setR11_bal_sub_diaries(rs.getBigDecimal("R11_BAL_SUB_DIARIES"));
			obj.setR11_bal_sub_diaries_bwp(rs.getBigDecimal("R11_BAL_SUB_DIARIES_BWP"));

			// ================= R12 =================
			obj.setR12_intrest_div(rs.getString("R12_INTREST_DIV"));
			obj.setR12_fig_bal_sheet(rs.getBigDecimal("R12_FIG_BAL_SHEET"));
			obj.setR12_fig_bal_sheet_bwp(rs.getBigDecimal("R12_FIG_BAL_SHEET_BWP"));
			obj.setR12_amt_statement_adj(rs.getBigDecimal("R12_AMT_STATEMENT_ADJ"));
			obj.setR12_amt_statement_adj_bwp(rs.getBigDecimal("R12_AMT_STATEMENT_ADJ_BWP"));
			obj.setR12_net_amt(rs.getBigDecimal("R12_NET_AMT"));
			obj.setR12_net_amt_bwp(rs.getBigDecimal("R12_NET_AMT_BWP"));
			obj.setR12_bal_sub(rs.getBigDecimal("R12_BAL_SUB"));
			obj.setR12_bal_sub_bwp(rs.getBigDecimal("R12_BAL_SUB_BWP"));
			obj.setR12_bal_sub_diaries(rs.getBigDecimal("R12_BAL_SUB_DIARIES"));
			obj.setR12_bal_sub_diaries_bwp(rs.getBigDecimal("R12_BAL_SUB_DIARIES_BWP"));

			// ================= R13 =================
			obj.setR13_intrest_div(rs.getString("R13_INTREST_DIV"));
			obj.setR13_fig_bal_sheet(rs.getBigDecimal("R13_FIG_BAL_SHEET"));
			obj.setR13_fig_bal_sheet_bwp(rs.getBigDecimal("R13_FIG_BAL_SHEET_BWP"));
			obj.setR13_amt_statement_adj(rs.getBigDecimal("R13_AMT_STATEMENT_ADJ"));
			obj.setR13_amt_statement_adj_bwp(rs.getBigDecimal("R13_AMT_STATEMENT_ADJ_BWP"));
			obj.setR13_net_amt(rs.getBigDecimal("R13_NET_AMT"));
			obj.setR13_net_amt_bwp(rs.getBigDecimal("R13_NET_AMT_BWP"));
			obj.setR13_bal_sub(rs.getBigDecimal("R13_BAL_SUB"));
			obj.setR13_bal_sub_bwp(rs.getBigDecimal("R13_BAL_SUB_BWP"));
			obj.setR13_bal_sub_diaries(rs.getBigDecimal("R13_BAL_SUB_DIARIES"));
			obj.setR13_bal_sub_diaries_bwp(rs.getBigDecimal("R13_BAL_SUB_DIARIES_BWP"));

			obj.setR17_other_income(rs.getString("R17_OTHER_INCOME"));
			obj.setR17_fig_bal_sheet(rs.getBigDecimal("R17_FIG_BAL_SHEET"));
			obj.setR17_fig_bal_sheet_bwp(rs.getBigDecimal("R17_FIG_BAL_SHEET_BWP"));
			obj.setR17_amt_statement_adj(rs.getBigDecimal("R17_AMT_STATEMENT_ADJ"));
			obj.setR17_amt_statement_adj_bwp(rs.getBigDecimal("R17_AMT_STATEMENT_ADJ_BWP"));
			obj.setR17_net_amt(rs.getBigDecimal("R17_NET_AMT"));
			obj.setR17_net_amt_bwp(rs.getBigDecimal("R17_NET_AMT_BWP"));
			obj.setR17_bal_sub(rs.getBigDecimal("R17_BAL_SUB"));
			obj.setR17_bal_sub_bwp(rs.getBigDecimal("R17_BAL_SUB_BWP"));
			obj.setR17_bal_sub_diaries(rs.getBigDecimal("R17_BAL_SUB_DIARIES"));
			obj.setR17_bal_sub_diaries_bwp(rs.getBigDecimal("R17_BAL_SUB_DIARIES_BWP"));

			obj.setR18_other_income(rs.getString("R18_OTHER_INCOME"));
			obj.setR18_fig_bal_sheet(rs.getBigDecimal("R18_FIG_BAL_SHEET"));
			obj.setR18_fig_bal_sheet_bwp(rs.getBigDecimal("R18_FIG_BAL_SHEET_BWP"));
			obj.setR18_amt_statement_adj(rs.getBigDecimal("R18_AMT_STATEMENT_ADJ"));
			obj.setR18_amt_statement_adj_bwp(rs.getBigDecimal("R18_AMT_STATEMENT_ADJ_BWP"));
			obj.setR18_net_amt(rs.getBigDecimal("R18_NET_AMT"));
			obj.setR18_net_amt_bwp(rs.getBigDecimal("R18_NET_AMT_BWP"));
			obj.setR18_bal_sub(rs.getBigDecimal("R18_BAL_SUB"));
			obj.setR18_bal_sub_bwp(rs.getBigDecimal("R18_BAL_SUB_BWP"));
			obj.setR18_bal_sub_diaries(rs.getBigDecimal("R18_BAL_SUB_DIARIES"));
			obj.setR18_bal_sub_diaries_bwp(rs.getBigDecimal("R18_BAL_SUB_DIARIES_BWP"));

			obj.setR19_other_income(rs.getString("R19_OTHER_INCOME"));
			obj.setR19_fig_bal_sheet(rs.getBigDecimal("R19_FIG_BAL_SHEET"));
			obj.setR19_fig_bal_sheet_bwp(rs.getBigDecimal("R19_FIG_BAL_SHEET_BWP"));
			obj.setR19_amt_statement_adj(rs.getBigDecimal("R19_AMT_STATEMENT_ADJ"));
			obj.setR19_amt_statement_adj_bwp(rs.getBigDecimal("R19_AMT_STATEMENT_ADJ_BWP"));
			obj.setR19_net_amt(rs.getBigDecimal("R19_NET_AMT"));
			obj.setR19_net_amt_bwp(rs.getBigDecimal("R19_NET_AMT_BWP"));
			obj.setR19_bal_sub(rs.getBigDecimal("R19_BAL_SUB"));
			obj.setR19_bal_sub_bwp(rs.getBigDecimal("R19_BAL_SUB_BWP"));
			obj.setR19_bal_sub_diaries(rs.getBigDecimal("R19_BAL_SUB_DIARIES"));
			obj.setR19_bal_sub_diaries_bwp(rs.getBigDecimal("R19_BAL_SUB_DIARIES_BWP"));

			obj.setR20_other_income(rs.getString("R20_OTHER_INCOME"));
			obj.setR20_fig_bal_sheet(rs.getBigDecimal("R20_FIG_BAL_SHEET"));
			obj.setR20_fig_bal_sheet_bwp(rs.getBigDecimal("R20_FIG_BAL_SHEET_BWP"));
			obj.setR20_amt_statement_adj(rs.getBigDecimal("R20_AMT_STATEMENT_ADJ"));
			obj.setR20_amt_statement_adj_bwp(rs.getBigDecimal("R20_AMT_STATEMENT_ADJ_BWP"));
			obj.setR20_net_amt(rs.getBigDecimal("R20_NET_AMT"));
			obj.setR20_net_amt_bwp(rs.getBigDecimal("R20_NET_AMT_BWP"));
			obj.setR20_bal_sub(rs.getBigDecimal("R20_BAL_SUB"));
			obj.setR20_bal_sub_bwp(rs.getBigDecimal("R20_BAL_SUB_BWP"));
			obj.setR20_bal_sub_diaries(rs.getBigDecimal("R20_BAL_SUB_DIARIES"));
			obj.setR20_bal_sub_diaries_bwp(rs.getBigDecimal("R20_BAL_SUB_DIARIES_BWP"));

			obj.setR21_other_income(rs.getString("R21_OTHER_INCOME"));
			obj.setR21_fig_bal_sheet(rs.getBigDecimal("R21_FIG_BAL_SHEET"));
			obj.setR21_fig_bal_sheet_bwp(rs.getBigDecimal("R21_FIG_BAL_SHEET_BWP"));
			obj.setR21_amt_statement_adj(rs.getBigDecimal("R21_AMT_STATEMENT_ADJ"));
			obj.setR21_amt_statement_adj_bwp(rs.getBigDecimal("R21_AMT_STATEMENT_ADJ_BWP"));
			obj.setR21_net_amt(rs.getBigDecimal("R21_NET_AMT"));
			obj.setR21_net_amt_bwp(rs.getBigDecimal("R21_NET_AMT_BWP"));
			obj.setR21_bal_sub(rs.getBigDecimal("R21_BAL_SUB"));
			obj.setR21_bal_sub_bwp(rs.getBigDecimal("R21_BAL_SUB_BWP"));
			obj.setR21_bal_sub_diaries(rs.getBigDecimal("R21_BAL_SUB_DIARIES"));
			obj.setR21_bal_sub_diaries_bwp(rs.getBigDecimal("R21_BAL_SUB_DIARIES_BWP"));

			obj.setR22_other_income(rs.getString("R22_OTHER_INCOME"));
			obj.setR22_fig_bal_sheet(rs.getBigDecimal("R22_FIG_BAL_SHEET"));
			obj.setR22_fig_bal_sheet_bwp(rs.getBigDecimal("R22_FIG_BAL_SHEET_BWP"));
			obj.setR22_amt_statement_adj(rs.getBigDecimal("R22_AMT_STATEMENT_ADJ"));
			obj.setR22_amt_statement_adj_bwp(rs.getBigDecimal("R22_AMT_STATEMENT_ADJ_BWP"));
			obj.setR22_net_amt(rs.getBigDecimal("R22_NET_AMT"));
			obj.setR22_net_amt_bwp(rs.getBigDecimal("R22_NET_AMT_BWP"));
			obj.setR22_bal_sub(rs.getBigDecimal("R22_BAL_SUB"));
			obj.setR22_bal_sub_bwp(rs.getBigDecimal("R22_BAL_SUB_BWP"));
			obj.setR22_bal_sub_diaries(rs.getBigDecimal("R22_BAL_SUB_DIARIES"));
			obj.setR22_bal_sub_diaries_bwp(rs.getBigDecimal("R22_BAL_SUB_DIARIES_BWP"));

			obj.setR23_other_income(rs.getString("R23_OTHER_INCOME"));
			obj.setR23_fig_bal_sheet(rs.getBigDecimal("R23_FIG_BAL_SHEET"));
			obj.setR23_fig_bal_sheet_bwp(rs.getBigDecimal("R23_FIG_BAL_SHEET_BWP"));
			obj.setR23_amt_statement_adj(rs.getBigDecimal("R23_AMT_STATEMENT_ADJ"));
			obj.setR23_amt_statement_adj_bwp(rs.getBigDecimal("R23_AMT_STATEMENT_ADJ_BWP"));
			obj.setR23_net_amt(rs.getBigDecimal("R23_NET_AMT"));
			obj.setR23_net_amt_bwp(rs.getBigDecimal("R23_NET_AMT_BWP"));
			obj.setR23_bal_sub(rs.getBigDecimal("R23_BAL_SUB"));
			obj.setR23_bal_sub_bwp(rs.getBigDecimal("R23_BAL_SUB_BWP"));
			obj.setR23_bal_sub_diaries(rs.getBigDecimal("R23_BAL_SUB_DIARIES"));
			obj.setR23_bal_sub_diaries_bwp(rs.getBigDecimal("R23_BAL_SUB_DIARIES_BWP"));

			obj.setR24_other_income(rs.getString("R24_OTHER_INCOME"));
			obj.setR24_fig_bal_sheet(rs.getBigDecimal("R24_FIG_BAL_SHEET"));
			obj.setR24_fig_bal_sheet_bwp(rs.getBigDecimal("R24_FIG_BAL_SHEET_BWP"));
			obj.setR24_amt_statement_adj(rs.getBigDecimal("R24_AMT_STATEMENT_ADJ"));
			obj.setR24_amt_statement_adj_bwp(rs.getBigDecimal("R24_AMT_STATEMENT_ADJ_BWP"));
			obj.setR24_net_amt(rs.getBigDecimal("R24_NET_AMT"));
			obj.setR24_net_amt_bwp(rs.getBigDecimal("R24_NET_AMT_BWP"));
			obj.setR24_bal_sub(rs.getBigDecimal("R24_BAL_SUB"));
			obj.setR24_bal_sub_bwp(rs.getBigDecimal("R24_BAL_SUB_BWP"));
			obj.setR24_bal_sub_diaries(rs.getBigDecimal("R24_BAL_SUB_DIARIES"));
			obj.setR24_bal_sub_diaries_bwp(rs.getBigDecimal("R24_BAL_SUB_DIARIES_BWP"));

			obj.setR25_other_income(rs.getString("R25_OTHER_INCOME"));
			obj.setR25_fig_bal_sheet(rs.getBigDecimal("R25_FIG_BAL_SHEET"));
			obj.setR25_fig_bal_sheet_bwp(rs.getBigDecimal("R25_FIG_BAL_SHEET_BWP"));
			obj.setR25_amt_statement_adj(rs.getBigDecimal("R25_AMT_STATEMENT_ADJ"));
			obj.setR25_amt_statement_adj_bwp(rs.getBigDecimal("R25_AMT_STATEMENT_ADJ_BWP"));
			obj.setR25_net_amt(rs.getBigDecimal("R25_NET_AMT"));
			obj.setR25_net_amt_bwp(rs.getBigDecimal("R25_NET_AMT_BWP"));
			obj.setR25_bal_sub(rs.getBigDecimal("R25_BAL_SUB"));
			obj.setR25_bal_sub_bwp(rs.getBigDecimal("R25_BAL_SUB_BWP"));
			obj.setR25_bal_sub_diaries(rs.getBigDecimal("R25_BAL_SUB_DIARIES"));
			obj.setR25_bal_sub_diaries_bwp(rs.getBigDecimal("R25_BAL_SUB_DIARIES_BWP"));

			obj.setR26_other_income(rs.getString("R26_OTHER_INCOME"));
			obj.setR26_fig_bal_sheet(rs.getBigDecimal("R26_FIG_BAL_SHEET"));
			obj.setR26_fig_bal_sheet_bwp(rs.getBigDecimal("R26_FIG_BAL_SHEET_BWP"));
			obj.setR26_amt_statement_adj(rs.getBigDecimal("R26_AMT_STATEMENT_ADJ"));
			obj.setR26_amt_statement_adj_bwp(rs.getBigDecimal("R26_AMT_STATEMENT_ADJ_BWP"));
			obj.setR26_net_amt(rs.getBigDecimal("R26_NET_AMT"));
			obj.setR26_net_amt_bwp(rs.getBigDecimal("R26_NET_AMT_BWP"));
			obj.setR26_bal_sub(rs.getBigDecimal("R26_BAL_SUB"));
			obj.setR26_bal_sub_bwp(rs.getBigDecimal("R26_BAL_SUB_BWP"));
			obj.setR26_bal_sub_diaries(rs.getBigDecimal("R26_BAL_SUB_DIARIES"));
			obj.setR26_bal_sub_diaries_bwp(rs.getBigDecimal("R26_BAL_SUB_DIARIES_BWP"));

			// R27
			obj.setR27_other_income(rs.getString("R27_OTHER_INCOME"));
			obj.setR27_fig_bal_sheet(rs.getBigDecimal("R27_FIG_BAL_SHEET"));
			obj.setR27_fig_bal_sheet_bwp(rs.getBigDecimal("R27_FIG_BAL_SHEET_BWP"));
			obj.setR27_amt_statement_adj(rs.getBigDecimal("R27_AMT_STATEMENT_ADJ"));
			obj.setR27_amt_statement_adj_bwp(rs.getBigDecimal("R27_AMT_STATEMENT_ADJ_BWP"));
			obj.setR27_net_amt(rs.getBigDecimal("R27_NET_AMT"));
			obj.setR27_net_amt_bwp(rs.getBigDecimal("R27_NET_AMT_BWP"));
			obj.setR27_bal_sub(rs.getBigDecimal("R27_BAL_SUB"));
			obj.setR27_bal_sub_bwp(rs.getBigDecimal("R27_BAL_SUB_BWP"));
			obj.setR27_bal_sub_diaries(rs.getBigDecimal("R27_BAL_SUB_DIARIES"));
			obj.setR27_bal_sub_diaries_bwp(rs.getBigDecimal("R27_BAL_SUB_DIARIES_BWP"));

			// R28
			obj.setR28_other_income(rs.getString("R28_OTHER_INCOME"));
			obj.setR28_fig_bal_sheet(rs.getBigDecimal("R28_FIG_BAL_SHEET"));
			obj.setR28_fig_bal_sheet_bwp(rs.getBigDecimal("R28_FIG_BAL_SHEET_BWP"));
			obj.setR28_amt_statement_adj(rs.getBigDecimal("R28_AMT_STATEMENT_ADJ"));
			obj.setR28_amt_statement_adj_bwp(rs.getBigDecimal("R28_AMT_STATEMENT_ADJ_BWP"));
			obj.setR28_net_amt(rs.getBigDecimal("R28_NET_AMT"));
			obj.setR28_net_amt_bwp(rs.getBigDecimal("R28_NET_AMT_BWP"));
			obj.setR28_bal_sub(rs.getBigDecimal("R28_BAL_SUB"));
			obj.setR28_bal_sub_bwp(rs.getBigDecimal("R28_BAL_SUB_BWP"));
			obj.setR28_bal_sub_diaries(rs.getBigDecimal("R28_BAL_SUB_DIARIES"));
			obj.setR28_bal_sub_diaries_bwp(rs.getBigDecimal("R28_BAL_SUB_DIARIES_BWP"));

			// R29
			obj.setR29_other_income(rs.getString("R29_OTHER_INCOME"));
			obj.setR29_fig_bal_sheet(rs.getBigDecimal("R29_FIG_BAL_SHEET"));
			obj.setR29_fig_bal_sheet_bwp(rs.getBigDecimal("R29_FIG_BAL_SHEET_BWP"));
			obj.setR29_amt_statement_adj(rs.getBigDecimal("R29_AMT_STATEMENT_ADJ"));
			obj.setR29_amt_statement_adj_bwp(rs.getBigDecimal("R29_AMT_STATEMENT_ADJ_BWP"));
			obj.setR29_net_amt(rs.getBigDecimal("R29_NET_AMT"));
			obj.setR29_net_amt_bwp(rs.getBigDecimal("R29_NET_AMT_BWP"));
			obj.setR29_bal_sub(rs.getBigDecimal("R29_BAL_SUB"));
			obj.setR29_bal_sub_bwp(rs.getBigDecimal("R29_BAL_SUB_BWP"));
			obj.setR29_bal_sub_diaries(rs.getBigDecimal("R29_BAL_SUB_DIARIES"));
			obj.setR29_bal_sub_diaries_bwp(rs.getBigDecimal("R29_BAL_SUB_DIARIES_BWP"));

			// R30
			obj.setR30_other_income(rs.getString("R30_OTHER_INCOME"));
			obj.setR30_fig_bal_sheet(rs.getBigDecimal("R30_FIG_BAL_SHEET"));
			obj.setR30_fig_bal_sheet_bwp(rs.getBigDecimal("R30_FIG_BAL_SHEET_BWP"));
			obj.setR30_amt_statement_adj(rs.getBigDecimal("R30_AMT_STATEMENT_ADJ"));
			obj.setR30_amt_statement_adj_bwp(rs.getBigDecimal("R30_AMT_STATEMENT_ADJ_BWP"));
			obj.setR30_net_amt(rs.getBigDecimal("R30_NET_AMT"));
			obj.setR30_net_amt_bwp(rs.getBigDecimal("R30_NET_AMT_BWP"));
			obj.setR30_bal_sub(rs.getBigDecimal("R30_BAL_SUB"));
			obj.setR30_bal_sub_bwp(rs.getBigDecimal("R30_BAL_SUB_BWP"));
			obj.setR30_bal_sub_diaries(rs.getBigDecimal("R30_BAL_SUB_DIARIES"));
			obj.setR30_bal_sub_diaries_bwp(rs.getBigDecimal("R30_BAL_SUB_DIARIES_BWP"));

			// R31
			obj.setR31_other_income(rs.getString("R31_OTHER_INCOME"));
			obj.setR31_fig_bal_sheet(rs.getBigDecimal("R31_FIG_BAL_SHEET"));
			obj.setR31_fig_bal_sheet_bwp(rs.getBigDecimal("R31_FIG_BAL_SHEET_BWP"));
			obj.setR31_amt_statement_adj(rs.getBigDecimal("R31_AMT_STATEMENT_ADJ"));
			obj.setR31_amt_statement_adj_bwp(rs.getBigDecimal("R31_AMT_STATEMENT_ADJ_BWP"));
			obj.setR31_net_amt(rs.getBigDecimal("R31_NET_AMT"));
			obj.setR31_net_amt_bwp(rs.getBigDecimal("R31_NET_AMT_BWP"));
			obj.setR31_bal_sub(rs.getBigDecimal("R31_BAL_SUB"));
			obj.setR31_bal_sub_bwp(rs.getBigDecimal("R31_BAL_SUB_BWP"));
			obj.setR31_bal_sub_diaries(rs.getBigDecimal("R31_BAL_SUB_DIARIES"));
			obj.setR31_bal_sub_diaries_bwp(rs.getBigDecimal("R31_BAL_SUB_DIARIES_BWP"));

			// ================= R40 =================
			obj.setR40_intrest_expended(rs.getString("R40_INTREST_EXPENDED"));
			obj.setR40_fig_bal_sheet(rs.getBigDecimal("R40_FIG_BAL_SHEET"));
			obj.setR40_fig_bal_sheet_bwp(rs.getBigDecimal("R40_FIG_BAL_SHEET_BWP"));
			obj.setR40_amt_statement_adj(rs.getBigDecimal("R40_AMT_STATEMENT_ADJ"));
			obj.setR40_amt_statement_adj_bwp(rs.getBigDecimal("R40_AMT_STATEMENT_ADJ_BWP"));
			obj.setR40_net_amt(rs.getBigDecimal("R40_NET_AMT"));
			obj.setR40_net_amt_bwp(rs.getBigDecimal("R40_NET_AMT_BWP"));
			obj.setR40_bal_sub(rs.getBigDecimal("R40_BAL_SUB"));
			obj.setR40_bal_sub_bwp(rs.getBigDecimal("R40_BAL_SUB_BWP"));
			obj.setR40_bal_sub_diaries_bwp(rs.getBigDecimal("R40_BAL_SUB_DIARIES_BWP"));

			obj.setR41_intrest_expended(rs.getString("R41_INTREST_EXPENDED"));
			obj.setR41_fig_bal_sheet(rs.getBigDecimal("R41_FIG_BAL_SHEET"));
			obj.setR41_fig_bal_sheet_bwp(rs.getBigDecimal("R41_FIG_BAL_SHEET_BWP"));
			obj.setR41_amt_statement_adj(rs.getBigDecimal("R41_AMT_STATEMENT_ADJ"));
			obj.setR41_amt_statement_adj_bwp(rs.getBigDecimal("R41_AMT_STATEMENT_ADJ_BWP"));
			obj.setR41_net_amt(rs.getBigDecimal("R41_NET_AMT"));
			obj.setR41_net_amt_bwp(rs.getBigDecimal("R41_NET_AMT_BWP"));
			obj.setR41_bal_sub(rs.getBigDecimal("R41_BAL_SUB"));
			obj.setR41_bal_sub_bwp(rs.getBigDecimal("R41_BAL_SUB_BWP"));
			obj.setR41_bal_sub_diaries(rs.getBigDecimal("R41_BAL_SUB_DIARIES"));
			obj.setR41_bal_sub_diaries_bwp(rs.getBigDecimal("R41_BAL_SUB_DIARIES_BWP"));

			obj.setR42_intrest_expended(rs.getString("R42_INTREST_EXPENDED"));
			obj.setR42_fig_bal_sheet(rs.getBigDecimal("R42_FIG_BAL_SHEET"));
			obj.setR42_fig_bal_sheet_bwp(rs.getBigDecimal("R42_FIG_BAL_SHEET_BWP"));
			obj.setR42_amt_statement_adj(rs.getBigDecimal("R42_AMT_STATEMENT_ADJ"));
			obj.setR42_amt_statement_adj_bwp(rs.getBigDecimal("R42_AMT_STATEMENT_ADJ_BWP"));
			obj.setR42_net_amt(rs.getBigDecimal("R42_NET_AMT"));
			obj.setR42_net_amt_bwp(rs.getBigDecimal("R42_NET_AMT_BWP"));
			obj.setR42_bal_sub(rs.getBigDecimal("R42_BAL_SUB"));
			obj.setR42_bal_sub_bwp(rs.getBigDecimal("R42_BAL_SUB_BWP"));
			obj.setR42_bal_sub_diaries(rs.getBigDecimal("R42_BAL_SUB_DIARIES"));
			obj.setR42_bal_sub_diaries_bwp(rs.getBigDecimal("R42_BAL_SUB_DIARIES_BWP"));

			obj.setR43_intrest_expended(rs.getString("R43_INTREST_EXPENDED"));
			obj.setR43_fig_bal_sheet(rs.getBigDecimal("R43_FIG_BAL_SHEET"));
			obj.setR43_fig_bal_sheet_bwp(rs.getBigDecimal("R43_FIG_BAL_SHEET_BWP"));
			obj.setR43_amt_statement_adj(rs.getBigDecimal("R43_AMT_STATEMENT_ADJ"));
			obj.setR43_amt_statement_adj_bwp(rs.getBigDecimal("R43_AMT_STATEMENT_ADJ_BWP"));
			obj.setR43_net_amt(rs.getBigDecimal("R43_NET_AMT"));
			obj.setR43_net_amt_bwp(rs.getBigDecimal("R43_NET_AMT_BWP"));
			obj.setR43_bal_sub(rs.getBigDecimal("R43_BAL_SUB"));
			obj.setR43_bal_sub_bwp(rs.getBigDecimal("R43_BAL_SUB_BWP"));
			obj.setR43_bal_sub_diaries(rs.getBigDecimal("R43_BAL_SUB_DIARIES"));
			obj.setR43_bal_sub_diaries_bwp(rs.getBigDecimal("R43_BAL_SUB_DIARIES_BWP"));

			// ================= R48 =================
			obj.setR48_operating_expenses(rs.getString("R48_OPERATING_EXPENSES"));
			obj.setR48_fig_bal_sheet(rs.getBigDecimal("R48_FIG_BAL_SHEET"));
			obj.setR48_fig_bal_sheet_bwp(rs.getBigDecimal("R48_FIG_BAL_SHEET_BWP"));
			obj.setR48_amt_statement_adj(rs.getBigDecimal("R48_AMT_STATEMENT_ADJ"));
			obj.setR48_amt_statement_adj_bwp(rs.getBigDecimal("R48_AMT_STATEMENT_ADJ_BWP"));
			obj.setR48_net_amt(rs.getBigDecimal("R48_NET_AMT"));
			obj.setR48_net_amt_bwp(rs.getBigDecimal("R48_NET_AMT_BWP"));
			obj.setR48_bal_sub(rs.getBigDecimal("R48_BAL_SUB"));
			obj.setR48_bal_sub_bwp(rs.getBigDecimal("R48_BAL_SUB_BWP"));
			obj.setR48_bal_sub_diaries(rs.getBigDecimal("R48_BAL_SUB_DIARIES"));
			obj.setR48_bal_sub_diaries_bwp(rs.getBigDecimal("R48_BAL_SUB_DIARIES_BWP"));

			obj.setR49_operating_expenses(rs.getString("R49_OPERATING_EXPENSES"));
			obj.setR49_fig_bal_sheet(rs.getBigDecimal("R49_FIG_BAL_SHEET"));
			obj.setR49_fig_bal_sheet_bwp(rs.getBigDecimal("R49_FIG_BAL_SHEET_BWP"));
			obj.setR49_amt_statement_adj(rs.getBigDecimal("R49_AMT_STATEMENT_ADJ"));
			obj.setR49_amt_statement_adj_bwp(rs.getBigDecimal("R49_AMT_STATEMENT_ADJ_BWP"));
			obj.setR49_net_amt(rs.getBigDecimal("R49_NET_AMT"));
			obj.setR49_net_amt_bwp(rs.getBigDecimal("R49_NET_AMT_BWP"));
			obj.setR49_bal_sub(rs.getBigDecimal("R49_BAL_SUB"));
			obj.setR49_bal_sub_bwp(rs.getBigDecimal("R49_BAL_SUB_BWP"));
			obj.setR49_bal_sub_diaries(rs.getBigDecimal("R49_BAL_SUB_DIARIES"));
			obj.setR49_bal_sub_diaries_bwp(rs.getBigDecimal("R49_BAL_SUB_DIARIES_BWP"));

			obj.setR50_operating_expenses(rs.getString("R50_OPERATING_EXPENSES"));
			obj.setR50_fig_bal_sheet(rs.getBigDecimal("R50_FIG_BAL_SHEET"));
			obj.setR50_fig_bal_sheet_bwp(rs.getBigDecimal("R50_FIG_BAL_SHEET_BWP"));
			obj.setR50_amt_statement_adj(rs.getBigDecimal("R50_AMT_STATEMENT_ADJ"));
			obj.setR50_amt_statement_adj_bwp(rs.getBigDecimal("R50_AMT_STATEMENT_ADJ_BWP"));
			obj.setR50_net_amt(rs.getBigDecimal("R50_NET_AMT"));
			obj.setR50_net_amt_bwp(rs.getBigDecimal("R50_NET_AMT_BWP"));
			obj.setR50_bal_sub(rs.getBigDecimal("R50_BAL_SUB"));
			obj.setR50_bal_sub_bwp(rs.getBigDecimal("R50_BAL_SUB_BWP"));
			obj.setR50_bal_sub_diaries(rs.getBigDecimal("R50_BAL_SUB_DIARIES"));
			obj.setR50_bal_sub_diaries_bwp(rs.getBigDecimal("R50_BAL_SUB_DIARIES_BWP"));

			obj.setR51_operating_expenses(rs.getString("R51_OPERATING_EXPENSES"));
			obj.setR51_fig_bal_sheet(rs.getBigDecimal("R51_FIG_BAL_SHEET"));
			obj.setR51_fig_bal_sheet_bwp(rs.getBigDecimal("R51_FIG_BAL_SHEET_BWP"));
			obj.setR51_amt_statement_adj(rs.getBigDecimal("R51_AMT_STATEMENT_ADJ"));
			obj.setR51_amt_statement_adj_bwp(rs.getBigDecimal("R51_AMT_STATEMENT_ADJ_BWP"));
			obj.setR51_net_amt(rs.getBigDecimal("R51_NET_AMT"));
			obj.setR51_net_amt_bwp(rs.getBigDecimal("R51_NET_AMT_BWP"));
			obj.setR51_bal_sub(rs.getBigDecimal("R51_BAL_SUB"));
			obj.setR51_bal_sub_bwp(rs.getBigDecimal("R51_BAL_SUB_BWP"));
			obj.setR51_bal_sub_diaries(rs.getBigDecimal("R51_BAL_SUB_DIARIES"));
			obj.setR51_bal_sub_diaries_bwp(rs.getBigDecimal("R51_BAL_SUB_DIARIES_BWP"));

			obj.setR52_operating_expenses(rs.getString("R52_OPERATING_EXPENSES"));
			obj.setR52_fig_bal_sheet(rs.getBigDecimal("R52_FIG_BAL_SHEET"));
			obj.setR52_fig_bal_sheet_bwp(rs.getBigDecimal("R52_FIG_BAL_SHEET_BWP"));
			obj.setR52_amt_statement_adj(rs.getBigDecimal("R52_AMT_STATEMENT_ADJ"));
			obj.setR52_amt_statement_adj_bwp(rs.getBigDecimal("R52_AMT_STATEMENT_ADJ_BWP"));
			obj.setR52_net_amt(rs.getBigDecimal("R52_NET_AMT"));
			obj.setR52_net_amt_bwp(rs.getBigDecimal("R52_NET_AMT_BWP"));
			obj.setR52_bal_sub(rs.getBigDecimal("R52_BAL_SUB"));
			obj.setR52_bal_sub_bwp(rs.getBigDecimal("R52_BAL_SUB_BWP"));
			obj.setR52_bal_sub_diaries(rs.getBigDecimal("R52_BAL_SUB_DIARIES"));
			obj.setR52_bal_sub_diaries_bwp(rs.getBigDecimal("R52_BAL_SUB_DIARIES_BWP"));

			obj.setR53_operating_expenses(rs.getString("R53_OPERATING_EXPENSES"));
			obj.setR53_fig_bal_sheet(rs.getBigDecimal("R53_FIG_BAL_SHEET"));
			obj.setR53_fig_bal_sheet_bwp(rs.getBigDecimal("R53_FIG_BAL_SHEET_BWP"));
			obj.setR53_amt_statement_adj(rs.getBigDecimal("R53_AMT_STATEMENT_ADJ"));
			obj.setR53_amt_statement_adj_bwp(rs.getBigDecimal("R53_AMT_STATEMENT_ADJ_BWP"));
			obj.setR53_net_amt(rs.getBigDecimal("R53_NET_AMT"));
			obj.setR53_net_amt_bwp(rs.getBigDecimal("R53_NET_AMT_BWP"));
			obj.setR53_bal_sub(rs.getBigDecimal("R53_BAL_SUB"));
			obj.setR53_bal_sub_bwp(rs.getBigDecimal("R53_BAL_SUB_BWP"));
			obj.setR53_bal_sub_diaries(rs.getBigDecimal("R53_BAL_SUB_DIARIES"));
			obj.setR53_bal_sub_diaries_bwp(rs.getBigDecimal("R53_BAL_SUB_DIARIES_BWP"));

			obj.setR54_operating_expenses(rs.getString("R54_OPERATING_EXPENSES"));
			obj.setR54_fig_bal_sheet(rs.getBigDecimal("R54_FIG_BAL_SHEET"));
			obj.setR54_fig_bal_sheet_bwp(rs.getBigDecimal("R54_FIG_BAL_SHEET_BWP"));
			obj.setR54_amt_statement_adj(rs.getBigDecimal("R54_AMT_STATEMENT_ADJ"));
			obj.setR54_amt_statement_adj_bwp(rs.getBigDecimal("R54_AMT_STATEMENT_ADJ_BWP"));
			obj.setR54_net_amt(rs.getBigDecimal("R54_NET_AMT"));
			obj.setR54_net_amt_bwp(rs.getBigDecimal("R54_NET_AMT_BWP"));
			obj.setR54_bal_sub(rs.getBigDecimal("R54_BAL_SUB"));
			obj.setR54_bal_sub_bwp(rs.getBigDecimal("R54_BAL_SUB_BWP"));
			obj.setR54_bal_sub_diaries(rs.getBigDecimal("R54_BAL_SUB_DIARIES"));
			obj.setR54_bal_sub_diaries_bwp(rs.getBigDecimal("R54_BAL_SUB_DIARIES_BWP"));

			obj.setR55_operating_expenses(rs.getString("R55_OPERATING_EXPENSES"));
			obj.setR55_fig_bal_sheet(rs.getBigDecimal("R55_FIG_BAL_SHEET"));
			obj.setR55_fig_bal_sheet_bwp(rs.getBigDecimal("R55_FIG_BAL_SHEET_BWP"));
			obj.setR55_amt_statement_adj(rs.getBigDecimal("R55_AMT_STATEMENT_ADJ"));
			obj.setR55_amt_statement_adj_bwp(rs.getBigDecimal("R55_AMT_STATEMENT_ADJ_BWP"));
			obj.setR55_net_amt(rs.getBigDecimal("R55_NET_AMT"));
			obj.setR55_net_amt_bwp(rs.getBigDecimal("R55_NET_AMT_BWP"));
			obj.setR55_bal_sub(rs.getBigDecimal("R55_BAL_SUB"));
			obj.setR55_bal_sub_bwp(rs.getBigDecimal("R55_BAL_SUB_BWP"));
			obj.setR55_bal_sub_diaries(rs.getBigDecimal("R55_BAL_SUB_DIARIES"));
			obj.setR55_bal_sub_diaries_bwp(rs.getBigDecimal("R55_BAL_SUB_DIARIES_BWP"));

			obj.setR56_operating_expenses(rs.getString("R56_OPERATING_EXPENSES"));
			obj.setR56_fig_bal_sheet(rs.getBigDecimal("R56_FIG_BAL_SHEET"));
			obj.setR56_fig_bal_sheet_bwp(rs.getBigDecimal("R56_FIG_BAL_SHEET_BWP"));
			obj.setR56_amt_statement_adj(rs.getBigDecimal("R56_AMT_STATEMENT_ADJ"));
			obj.setR56_amt_statement_adj_bwp(rs.getBigDecimal("R56_AMT_STATEMENT_ADJ_BWP"));
			obj.setR56_net_amt(rs.getBigDecimal("R56_NET_AMT"));
			obj.setR56_net_amt_bwp(rs.getBigDecimal("R56_NET_AMT_BWP"));
			obj.setR56_bal_sub(rs.getBigDecimal("R56_BAL_SUB"));
			obj.setR56_bal_sub_bwp(rs.getBigDecimal("R56_BAL_SUB_BWP"));
			obj.setR56_bal_sub_diaries(rs.getBigDecimal("R56_BAL_SUB_DIARIES"));
			obj.setR56_bal_sub_diaries_bwp(rs.getBigDecimal("R56_BAL_SUB_DIARIES_BWP"));

			obj.setR57_operating_expenses(rs.getString("R57_OPERATING_EXPENSES"));
			obj.setR57_fig_bal_sheet(rs.getBigDecimal("R57_FIG_BAL_SHEET"));
			obj.setR57_fig_bal_sheet_bwp(rs.getBigDecimal("R57_FIG_BAL_SHEET_BWP"));
			obj.setR57_amt_statement_adj(rs.getBigDecimal("R57_AMT_STATEMENT_ADJ"));
			obj.setR57_amt_statement_adj_bwp(rs.getBigDecimal("R57_AMT_STATEMENT_ADJ_BWP"));
			obj.setR57_net_amt(rs.getBigDecimal("R57_NET_AMT"));
			obj.setR57_net_amt_bwp(rs.getBigDecimal("R57_NET_AMT_BWP"));
			obj.setR57_bal_sub(rs.getBigDecimal("R57_BAL_SUB"));
			obj.setR57_bal_sub_bwp(rs.getBigDecimal("R57_BAL_SUB_BWP"));
			obj.setR57_bal_sub_diaries(rs.getBigDecimal("R57_BAL_SUB_DIARIES"));
			obj.setR57_bal_sub_diaries_bwp(rs.getBigDecimal("R57_BAL_SUB_DIARIES_BWP"));

			obj.setR58_operating_expenses(rs.getString("R58_OPERATING_EXPENSES"));
			obj.setR58_fig_bal_sheet(rs.getBigDecimal("R58_FIG_BAL_SHEET"));
			obj.setR58_fig_bal_sheet_bwp(rs.getBigDecimal("R58_FIG_BAL_SHEET_BWP"));
			obj.setR58_amt_statement_adj(rs.getBigDecimal("R58_AMT_STATEMENT_ADJ"));
			obj.setR58_amt_statement_adj_bwp(rs.getBigDecimal("R58_AMT_STATEMENT_ADJ_BWP"));
			obj.setR58_net_amt(rs.getBigDecimal("R58_NET_AMT"));
			obj.setR58_net_amt_bwp(rs.getBigDecimal("R58_NET_AMT_BWP"));
			obj.setR58_bal_sub(rs.getBigDecimal("R58_BAL_SUB"));
			obj.setR58_bal_sub_bwp(rs.getBigDecimal("R58_BAL_SUB_BWP"));
			obj.setR58_bal_sub_diaries(rs.getBigDecimal("R58_BAL_SUB_DIARIES"));
			obj.setR58_bal_sub_diaries_bwp(rs.getBigDecimal("R58_BAL_SUB_DIARIES_BWP"));

			obj.setR59_operating_expenses(rs.getString("R59_OPERATING_EXPENSES"));
			obj.setR59_fig_bal_sheet(rs.getBigDecimal("R59_FIG_BAL_SHEET"));
			obj.setR59_fig_bal_sheet_bwp(rs.getBigDecimal("R59_FIG_BAL_SHEET_BWP"));
			obj.setR59_amt_statement_adj(rs.getBigDecimal("R59_AMT_STATEMENT_ADJ"));
			obj.setR59_amt_statement_adj_bwp(rs.getBigDecimal("R59_AMT_STATEMENT_ADJ_BWP"));
			obj.setR59_net_amt(rs.getBigDecimal("R59_NET_AMT"));
			obj.setR59_net_amt_bwp(rs.getBigDecimal("R59_NET_AMT_BWP"));
			obj.setR59_bal_sub(rs.getBigDecimal("R59_BAL_SUB"));
			obj.setR59_bal_sub_bwp(rs.getBigDecimal("R59_BAL_SUB_BWP"));
			obj.setR59_bal_sub_diaries(rs.getBigDecimal("R59_BAL_SUB_DIARIES"));
			obj.setR59_bal_sub_diaries_bwp(rs.getBigDecimal("R59_BAL_SUB_DIARIES_BWP"));

			obj.setR60_operating_expenses(rs.getString("R60_OPERATING_EXPENSES"));
			obj.setR60_fig_bal_sheet(rs.getBigDecimal("R60_FIG_BAL_SHEET"));
			obj.setR60_fig_bal_sheet_bwp(rs.getBigDecimal("R60_FIG_BAL_SHEET_BWP"));
			obj.setR60_amt_statement_adj(rs.getBigDecimal("R60_AMT_STATEMENT_ADJ"));
			obj.setR60_amt_statement_adj_bwp(rs.getBigDecimal("R60_AMT_STATEMENT_ADJ_BWP"));
			obj.setR60_net_amt(rs.getBigDecimal("R60_NET_AMT"));
			obj.setR60_net_amt_bwp(rs.getBigDecimal("R60_NET_AMT_BWP"));
			obj.setR60_bal_sub(rs.getBigDecimal("R60_BAL_SUB"));
			obj.setR60_bal_sub_bwp(rs.getBigDecimal("R60_BAL_SUB_BWP"));
			obj.setR60_bal_sub_diaries(rs.getBigDecimal("R60_BAL_SUB_DIARIES"));
			obj.setR60_bal_sub_diaries_bwp(rs.getBigDecimal("R60_BAL_SUB_DIARIES_BWP"));

			obj.setR61_operating_expenses(rs.getString("R61_OPERATING_EXPENSES"));
			obj.setR61_fig_bal_sheet(rs.getBigDecimal("R61_FIG_BAL_SHEET"));
			obj.setR61_fig_bal_sheet_bwp(rs.getBigDecimal("R61_FIG_BAL_SHEET_BWP"));
			obj.setR61_amt_statement_adj(rs.getBigDecimal("R61_AMT_STATEMENT_ADJ"));
			obj.setR61_amt_statement_adj_bwp(rs.getBigDecimal("R61_AMT_STATEMENT_ADJ_BWP"));
			obj.setR61_net_amt(rs.getBigDecimal("R61_NET_AMT"));
			obj.setR61_net_amt_bwp(rs.getBigDecimal("R61_NET_AMT_BWP"));
			obj.setR61_bal_sub(rs.getBigDecimal("R61_BAL_SUB"));
			obj.setR61_bal_sub_bwp(rs.getBigDecimal("R61_BAL_SUB_BWP"));
			obj.setR61_bal_sub_diaries(rs.getBigDecimal("R61_BAL_SUB_DIARIES"));
			obj.setR61_bal_sub_diaries_bwp(rs.getBigDecimal("R61_BAL_SUB_DIARIES_BWP"));

			obj.setR62_operating_expenses(rs.getString("R62_OPERATING_EXPENSES"));
			obj.setR62_fig_bal_sheet(rs.getBigDecimal("R62_FIG_BAL_SHEET"));
			obj.setR62_fig_bal_sheet_bwp(rs.getBigDecimal("R62_FIG_BAL_SHEET_BWP"));
			obj.setR62_amt_statement_adj(rs.getBigDecimal("R62_AMT_STATEMENT_ADJ"));
			obj.setR62_amt_statement_adj_bwp(rs.getBigDecimal("R62_AMT_STATEMENT_ADJ_BWP"));
			obj.setR62_net_amt(rs.getBigDecimal("R62_NET_AMT"));
			obj.setR62_net_amt_bwp(rs.getBigDecimal("R62_NET_AMT_BWP"));
			obj.setR62_bal_sub(rs.getBigDecimal("R62_BAL_SUB"));
			obj.setR62_bal_sub_bwp(rs.getBigDecimal("R62_BAL_SUB_BWP"));
			obj.setR62_bal_sub_diaries(rs.getBigDecimal("R62_BAL_SUB_DIARIES"));
			obj.setR62_bal_sub_diaries_bwp(rs.getBigDecimal("R62_BAL_SUB_DIARIES_BWP"));

			obj.setR63_operating_expenses(rs.getString("R63_OPERATING_EXPENSES"));
			obj.setR63_fig_bal_sheet(rs.getBigDecimal("R63_FIG_BAL_SHEET"));
			obj.setR63_fig_bal_sheet_bwp(rs.getBigDecimal("R63_FIG_BAL_SHEET_BWP"));
			obj.setR63_amt_statement_adj(rs.getBigDecimal("R63_AMT_STATEMENT_ADJ"));
			obj.setR63_amt_statement_adj_bwp(rs.getBigDecimal("R63_AMT_STATEMENT_ADJ_BWP"));
			obj.setR63_net_amt(rs.getBigDecimal("R63_NET_AMT"));
			obj.setR63_net_amt_bwp(rs.getBigDecimal("R63_NET_AMT_BWP"));
			obj.setR63_bal_sub(rs.getBigDecimal("R63_BAL_SUB"));
			obj.setR63_bal_sub_bwp(rs.getBigDecimal("R63_BAL_SUB_BWP"));
			obj.setR63_bal_sub_diaries(rs.getBigDecimal("R63_BAL_SUB_DIARIES"));
			obj.setR63_bal_sub_diaries_bwp(rs.getBigDecimal("R63_BAL_SUB_DIARIES_BWP"));

			obj.setR18_bal_sub_diaries(rs.getBigDecimal("R18_BAL_SUB_DIARIES"));
			obj.setR19_bal_sub_diaries(rs.getBigDecimal("R19_BAL_SUB_DIARIES"));
			obj.setR20_bal_sub_diaries(rs.getBigDecimal("R20_BAL_SUB_DIARIES"));
			obj.setR21_bal_sub_diaries(rs.getBigDecimal("R21_BAL_SUB_DIARIES"));
			obj.setR22_bal_sub_diaries(rs.getBigDecimal("R22_BAL_SUB_DIARIES"));
			obj.setR23_bal_sub_diaries(rs.getBigDecimal("R23_BAL_SUB_DIARIES"));
			obj.setR24_bal_sub_diaries(rs.getBigDecimal("R24_BAL_SUB_DIARIES"));
			obj.setR25_bal_sub_diaries(rs.getBigDecimal("R25_BAL_SUB_DIARIES"));
			obj.setR26_bal_sub_diaries(rs.getBigDecimal("R26_BAL_SUB_DIARIES"));
			obj.setR27_bal_sub_diaries(rs.getBigDecimal("R27_BAL_SUB_DIARIES"));
			obj.setR28_bal_sub_diaries(rs.getBigDecimal("R28_BAL_SUB_DIARIES"));
			obj.setR29_bal_sub_diaries(rs.getBigDecimal("R29_BAL_SUB_DIARIES"));
			obj.setR30_bal_sub_diaries(rs.getBigDecimal("R30_BAL_SUB_DIARIES"));
			obj.setR31_bal_sub_diaries(rs.getBigDecimal("R31_BAL_SUB_DIARIES"));

			obj.setR40_bal_sub_diaries(rs.getBigDecimal("R40_BAL_SUB_DIARIES"));
			obj.setR41_bal_sub_diaries(rs.getBigDecimal("R41_BAL_SUB_DIARIES"));
			obj.setR42_bal_sub_diaries(rs.getBigDecimal("R42_BAL_SUB_DIARIES"));
			obj.setR43_bal_sub_diaries(rs.getBigDecimal("R43_BAL_SUB_DIARIES"));

			obj.setR48_bal_sub_diaries(rs.getBigDecimal("R48_BAL_SUB_DIARIES"));
			obj.setR49_bal_sub_diaries(rs.getBigDecimal("R49_BAL_SUB_DIARIES"));
			obj.setR50_bal_sub_diaries(rs.getBigDecimal("R50_BAL_SUB_DIARIES"));
			obj.setR51_bal_sub_diaries(rs.getBigDecimal("R51_BAL_SUB_DIARIES"));
			obj.setR52_bal_sub_diaries(rs.getBigDecimal("R52_BAL_SUB_DIARIES"));
			obj.setR53_bal_sub_diaries(rs.getBigDecimal("R53_BAL_SUB_DIARIES"));
			obj.setR54_bal_sub_diaries(rs.getBigDecimal("R54_BAL_SUB_DIARIES"));
			obj.setR55_bal_sub_diaries(rs.getBigDecimal("R55_BAL_SUB_DIARIES"));
			obj.setR56_bal_sub_diaries(rs.getBigDecimal("R56_BAL_SUB_DIARIES"));
			obj.setR57_bal_sub_diaries(rs.getBigDecimal("R57_BAL_SUB_DIARIES"));
			obj.setR58_bal_sub_diaries(rs.getBigDecimal("R58_BAL_SUB_DIARIES"));
			obj.setR59_bal_sub_diaries(rs.getBigDecimal("R59_BAL_SUB_DIARIES"));
			obj.setR60_bal_sub_diaries(rs.getBigDecimal("R60_BAL_SUB_DIARIES"));
			obj.setR61_bal_sub_diaries(rs.getBigDecimal("R61_BAL_SUB_DIARIES"));
			obj.setR62_bal_sub_diaries(rs.getBigDecimal("R62_BAL_SUB_DIARIES"));
			obj.setR63_bal_sub_diaries(rs.getBigDecimal("R63_BAL_SUB_DIARIES"));
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

	@IdClass(PL_SCHS_PK.class)
	public class PL_SCHS_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

// ================= R9 =================
		@Column(name = "R9_INTREST_DIV")
		private String r9_intrest_div;

		@Column(name = "R9_FIG_BAL_SHEET")
		private BigDecimal r9_fig_bal_sheet;

		@Column(name = "R9_FIG_BAL_SHEET_BWP")
		private BigDecimal r9_fig_bal_sheet_bwp;

		@Column(name = "R9_AMT_STATEMENT_ADJ")
		private BigDecimal r9_amt_statement_adj;

		@Column(name = "R9_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r9_amt_statement_adj_bwp;

		@Column(name = "R9_NET_AMT")
		private BigDecimal r9_net_amt;

		@Column(name = "R9_NET_AMT_BWP")
		private BigDecimal r9_net_amt_bwp;

		@Column(name = "R9_BAL_SUB")
		private BigDecimal r9_bal_sub;

		@Column(name = "R9_BAL_SUB_BWP")
		private BigDecimal r9_bal_sub_bwp;

		@Column(name = "R9_BAL_SUB_DIARIES")
		private BigDecimal r9_bal_sub_diaries;

		@Column(name = "R9_BAL_SUB_DIARIES_BWP")
		private BigDecimal r9_bal_sub_diaries_bwp;

		// ================= R10 =================
		@Column(name = "R10_INTREST_DIV")
		private String r10_intrest_div;

		@Column(name = "R10_FIG_BAL_SHEET")
		private BigDecimal r10_fig_bal_sheet;

		@Column(name = "R10_FIG_BAL_SHEET_BWP")
		private BigDecimal r10_fig_bal_sheet_bwp;

		@Column(name = "R10_AMT_STATEMENT_ADJ")
		private BigDecimal r10_amt_statement_adj;

		@Column(name = "R10_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r10_amt_statement_adj_bwp;

		@Column(name = "R10_NET_AMT")
		private BigDecimal r10_net_amt;

		@Column(name = "R10_NET_AMT_BWP")
		private BigDecimal r10_net_amt_bwp;

		@Column(name = "R10_BAL_SUB")
		private BigDecimal r10_bal_sub;

		@Column(name = "R10_BAL_SUB_BWP")
		private BigDecimal r10_bal_sub_bwp;

		@Column(name = "R10_BAL_SUB_DIARIES")
		private BigDecimal r10_bal_sub_diaries;

		@Column(name = "R10_BAL_SUB_DIARIES_BWP")
		private BigDecimal r10_bal_sub_diaries_bwp;

		// ================= R11 =================
		@Column(name = "R11_INTREST_DIV")
		private String r11_intrest_div;

		@Column(name = "R11_FIG_BAL_SHEET")
		private BigDecimal r11_fig_bal_sheet;

		@Column(name = "R11_FIG_BAL_SHEET_BWP")
		private BigDecimal r11_fig_bal_sheet_bwp;

		@Column(name = "R11_AMT_STATEMENT_ADJ")
		private BigDecimal r11_amt_statement_adj;

		@Column(name = "R11_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r11_amt_statement_adj_bwp;

		@Column(name = "R11_NET_AMT")
		private BigDecimal r11_net_amt;

		@Column(name = "R11_NET_AMT_BWP")
		private BigDecimal r11_net_amt_bwp;

		@Column(name = "R11_BAL_SUB")
		private BigDecimal r11_bal_sub;

		@Column(name = "R11_BAL_SUB_BWP")
		private BigDecimal r11_bal_sub_bwp;

		@Column(name = "R11_BAL_SUB_DIARIES")
		private BigDecimal r11_bal_sub_diaries;

		@Column(name = "R11_BAL_SUB_DIARIES_BWP")
		private BigDecimal r11_bal_sub_diaries_bwp;

		// ================= R12 =================
		@Column(name = "R12_INTREST_DIV")
		private String r12_intrest_div;

		@Column(name = "R12_FIG_BAL_SHEET")
		private BigDecimal r12_fig_bal_sheet;

		@Column(name = "R12_FIG_BAL_SHEET_BWP")
		private BigDecimal r12_fig_bal_sheet_bwp;

		@Column(name = "R12_AMT_STATEMENT_ADJ")
		private BigDecimal r12_amt_statement_adj;

		@Column(name = "R12_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r12_amt_statement_adj_bwp;

		@Column(name = "R12_NET_AMT")
		private BigDecimal r12_net_amt;

		@Column(name = "R12_NET_AMT_BWP")
		private BigDecimal r12_net_amt_bwp;

		@Column(name = "R12_BAL_SUB")
		private BigDecimal r12_bal_sub;

		@Column(name = "R12_BAL_SUB_BWP")
		private BigDecimal r12_bal_sub_bwp;

		@Column(name = "R12_BAL_SUB_DIARIES")
		private BigDecimal r12_bal_sub_diaries;

		@Column(name = "R12_BAL_SUB_DIARIES_BWP")
		private BigDecimal r12_bal_sub_diaries_bwp;

		// ================= R13 =================
		@Column(name = "R13_INTREST_DIV")
		private String r13_intrest_div;

		@Column(name = "R13_FIG_BAL_SHEET")
		private BigDecimal r13_fig_bal_sheet;

		@Column(name = "R13_FIG_BAL_SHEET_BWP")
		private BigDecimal r13_fig_bal_sheet_bwp;

		@Column(name = "R13_AMT_STATEMENT_ADJ")
		private BigDecimal r13_amt_statement_adj;

		@Column(name = "R13_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r13_amt_statement_adj_bwp;

		@Column(name = "R13_NET_AMT")
		private BigDecimal r13_net_amt;

		@Column(name = "R13_NET_AMT_BWP")
		private BigDecimal r13_net_amt_bwp;

		@Column(name = "R13_BAL_SUB")
		private BigDecimal r13_bal_sub;

		@Column(name = "R13_BAL_SUB_BWP")
		private BigDecimal r13_bal_sub_bwp;

		@Column(name = "R13_BAL_SUB_DIARIES")
		private BigDecimal r13_bal_sub_diaries;

		@Column(name = "R13_BAL_SUB_DIARIES_BWP")
		private BigDecimal r13_bal_sub_diaries_bwp;

		// ================= R17 =================
		@Column(name = "R17_OTHER_INCOME")
		private String r17_other_income;

		@Column(name = "R17_FIG_BAL_SHEET")
		private BigDecimal r17_fig_bal_sheet;

		@Column(name = "R17_FIG_BAL_SHEET_BWP")
		private BigDecimal r17_fig_bal_sheet_bwp;

		@Column(name = "R17_AMT_STATEMENT_ADJ")
		private BigDecimal r17_amt_statement_adj;

		@Column(name = "R17_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r17_amt_statement_adj_bwp;

		@Column(name = "R17_NET_AMT")
		private BigDecimal r17_net_amt;

		@Column(name = "R17_NET_AMT_BWP")
		private BigDecimal r17_net_amt_bwp;

		@Column(name = "R17_BAL_SUB")
		private BigDecimal r17_bal_sub;

		@Column(name = "R17_BAL_SUB_BWP")
		private BigDecimal r17_bal_sub_bwp;

		@Column(name = "R17_BAL_SUB_DIARIES")
		private BigDecimal r17_bal_sub_diaries;

		@Column(name = "R17_BAL_SUB_DIARIES_BWP")
		private BigDecimal r17_bal_sub_diaries_bwp;

		// ================= R18 =================
		@Column(name = "R18_OTHER_INCOME")
		private String r18_other_income;

		@Column(name = "R18_FIG_BAL_SHEET")
		private BigDecimal r18_fig_bal_sheet;

		@Column(name = "R18_FIG_BAL_SHEET_BWP")
		private BigDecimal r18_fig_bal_sheet_bwp;

		@Column(name = "R18_AMT_STATEMENT_ADJ")
		private BigDecimal r18_amt_statement_adj;

		@Column(name = "R18_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r18_amt_statement_adj_bwp;

		@Column(name = "R18_NET_AMT")
		private BigDecimal r18_net_amt;

		@Column(name = "R18_NET_AMT_BWP")
		private BigDecimal r18_net_amt_bwp;

		@Column(name = "R18_BAL_SUB")
		private BigDecimal r18_bal_sub;

		@Column(name = "R18_BAL_SUB_BWP")
		private BigDecimal r18_bal_sub_bwp;

		@Column(name = "R18_BAL_SUB_DIARIES_BWP")
		private BigDecimal r18_bal_sub_diaries_bwp;

		// ================= R19 =================
		@Column(name = "R19_OTHER_INCOME")
		private String r19_other_income;

		@Column(name = "R19_FIG_BAL_SHEET")
		private BigDecimal r19_fig_bal_sheet;

		@Column(name = "R19_FIG_BAL_SHEET_BWP")
		private BigDecimal r19_fig_bal_sheet_bwp;

		@Column(name = "R19_AMT_STATEMENT_ADJ")
		private BigDecimal r19_amt_statement_adj;

		@Column(name = "R19_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r19_amt_statement_adj_bwp;

		@Column(name = "R19_NET_AMT")
		private BigDecimal r19_net_amt;

		@Column(name = "R19_NET_AMT_BWP")
		private BigDecimal r19_net_amt_bwp;

		@Column(name = "R19_BAL_SUB")
		private BigDecimal r19_bal_sub;

		@Column(name = "R19_BAL_SUB_BWP")
		private BigDecimal r19_bal_sub_bwp;

		@Column(name = "R19_BAL_SUB_DIARIES_BWP")
		private BigDecimal r19_bal_sub_diaries_bwp;

		// ================= R20 =================
		@Column(name = "R20_OTHER_INCOME")
		private String r20_other_income;

		@Column(name = "R20_FIG_BAL_SHEET")
		private BigDecimal r20_fig_bal_sheet;

		@Column(name = "R20_FIG_BAL_SHEET_BWP")
		private BigDecimal r20_fig_bal_sheet_bwp;

		@Column(name = "R20_AMT_STATEMENT_ADJ")
		private BigDecimal r20_amt_statement_adj;

		@Column(name = "R20_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r20_amt_statement_adj_bwp;

		@Column(name = "R20_NET_AMT")
		private BigDecimal r20_net_amt;

		@Column(name = "R20_NET_AMT_BWP")
		private BigDecimal r20_net_amt_bwp;

		@Column(name = "R20_BAL_SUB")
		private BigDecimal r20_bal_sub;

		@Column(name = "R20_BAL_SUB_BWP")
		private BigDecimal r20_bal_sub_bwp;

		@Column(name = "R20_BAL_SUB_DIARIES_BWP")
		private BigDecimal r20_bal_sub_diaries_bwp;

		// ================= R21 =================
		@Column(name = "R21_OTHER_INCOME")
		private String r21_other_income;

		@Column(name = "R21_FIG_BAL_SHEET")
		private BigDecimal r21_fig_bal_sheet;

		@Column(name = "R21_FIG_BAL_SHEET_BWP")
		private BigDecimal r21_fig_bal_sheet_bwp;

		@Column(name = "R21_AMT_STATEMENT_ADJ")
		private BigDecimal r21_amt_statement_adj;

		@Column(name = "R21_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r21_amt_statement_adj_bwp;

		@Column(name = "R21_NET_AMT")
		private BigDecimal r21_net_amt;

		@Column(name = "R21_NET_AMT_BWP")
		private BigDecimal r21_net_amt_bwp;

		@Column(name = "R21_BAL_SUB")
		private BigDecimal r21_bal_sub;

		@Column(name = "R21_BAL_SUB_BWP")
		private BigDecimal r21_bal_sub_bwp;

		@Column(name = "R21_BAL_SUB_DIARIES_BWP")
		private BigDecimal r21_bal_sub_diaries_bwp;

		// ================= R22 =================
		@Column(name = "R22_OTHER_INCOME")
		private String r22_other_income;

		@Column(name = "R22_FIG_BAL_SHEET")
		private BigDecimal r22_fig_bal_sheet;

		@Column(name = "R22_FIG_BAL_SHEET_BWP")
		private BigDecimal r22_fig_bal_sheet_bwp;

		@Column(name = "R22_AMT_STATEMENT_ADJ")
		private BigDecimal r22_amt_statement_adj;

		@Column(name = "R22_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r22_amt_statement_adj_bwp;

		@Column(name = "R22_NET_AMT")
		private BigDecimal r22_net_amt;

		@Column(name = "R22_NET_AMT_BWP")
		private BigDecimal r22_net_amt_bwp;

		@Column(name = "R22_BAL_SUB")
		private BigDecimal r22_bal_sub;

		@Column(name = "R22_BAL_SUB_BWP")
		private BigDecimal r22_bal_sub_bwp;

		@Column(name = "R22_BAL_SUB_DIARIES_BWP")
		private BigDecimal r22_bal_sub_diaries_bwp;

		// ================= R23 =================
		@Column(name = "R23_OTHER_INCOME")
		private String r23_other_income;

		@Column(name = "R23_FIG_BAL_SHEET")
		private BigDecimal r23_fig_bal_sheet;

		@Column(name = "R23_FIG_BAL_SHEET_BWP")
		private BigDecimal r23_fig_bal_sheet_bwp;

		@Column(name = "R23_AMT_STATEMENT_ADJ")
		private BigDecimal r23_amt_statement_adj;

		@Column(name = "R23_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r23_amt_statement_adj_bwp;

		@Column(name = "R23_NET_AMT")
		private BigDecimal r23_net_amt;

		@Column(name = "R23_NET_AMT_BWP")
		private BigDecimal r23_net_amt_bwp;

		@Column(name = "R23_BAL_SUB")
		private BigDecimal r23_bal_sub;

		@Column(name = "R23_BAL_SUB_BWP")
		private BigDecimal r23_bal_sub_bwp;

		@Column(name = "R23_BAL_SUB_DIARIES_BWP")
		private BigDecimal r23_bal_sub_diaries_bwp;

		// ================= R24 =================
		@Column(name = "R24_OTHER_INCOME")
		private String r24_other_income;

		@Column(name = "R24_FIG_BAL_SHEET")
		private BigDecimal r24_fig_bal_sheet;

		@Column(name = "R24_FIG_BAL_SHEET_BWP")
		private BigDecimal r24_fig_bal_sheet_bwp;

		@Column(name = "R24_AMT_STATEMENT_ADJ")
		private BigDecimal r24_amt_statement_adj;

		@Column(name = "R24_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r24_amt_statement_adj_bwp;

		@Column(name = "R24_NET_AMT")
		private BigDecimal r24_net_amt;

		@Column(name = "R24_NET_AMT_BWP")
		private BigDecimal r24_net_amt_bwp;

		@Column(name = "R24_BAL_SUB")
		private BigDecimal r24_bal_sub;

		@Column(name = "R24_BAL_SUB_BWP")
		private BigDecimal r24_bal_sub_bwp;

		@Column(name = "R24_BAL_SUB_DIARIES_BWP")
		private BigDecimal r24_bal_sub_diaries_bwp;

		// ================= R25 =================
		@Column(name = "R25_OTHER_INCOME")
		private String r25_other_income;

		@Column(name = "R25_FIG_BAL_SHEET")
		private BigDecimal r25_fig_bal_sheet;

		@Column(name = "R25_FIG_BAL_SHEET_BWP")
		private BigDecimal r25_fig_bal_sheet_bwp;

		@Column(name = "R25_AMT_STATEMENT_ADJ")
		private BigDecimal r25_amt_statement_adj;

		@Column(name = "R25_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r25_amt_statement_adj_bwp;

		@Column(name = "R25_NET_AMT")
		private BigDecimal r25_net_amt;

		@Column(name = "R25_NET_AMT_BWP")
		private BigDecimal r25_net_amt_bwp;

		@Column(name = "R25_BAL_SUB")
		private BigDecimal r25_bal_sub;

		@Column(name = "R25_BAL_SUB_BWP")
		private BigDecimal r25_bal_sub_bwp;

		@Column(name = "R25_BAL_SUB_DIARIES_BWP")
		private BigDecimal r25_bal_sub_diaries_bwp;

		// ================= R26 =================
		@Column(name = "R26_OTHER_INCOME")
		private String r26_other_income;

		@Column(name = "R26_FIG_BAL_SHEET")
		private BigDecimal r26_fig_bal_sheet;

		@Column(name = "R26_FIG_BAL_SHEET_BWP")
		private BigDecimal r26_fig_bal_sheet_bwp;

		@Column(name = "R26_AMT_STATEMENT_ADJ")
		private BigDecimal r26_amt_statement_adj;

		@Column(name = "R26_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r26_amt_statement_adj_bwp;

		@Column(name = "R26_NET_AMT")
		private BigDecimal r26_net_amt;

		@Column(name = "R26_NET_AMT_BWP")
		private BigDecimal r26_net_amt_bwp;

		@Column(name = "R26_BAL_SUB")
		private BigDecimal r26_bal_sub;

		@Column(name = "R26_BAL_SUB_BWP")
		private BigDecimal r26_bal_sub_bwp;

		@Column(name = "R26_BAL_SUB_DIARIES_BWP")
		private BigDecimal r26_bal_sub_diaries_bwp;

		// ================= R27 =================
		@Column(name = "R27_OTHER_INCOME")
		private String r27_other_income;

		@Column(name = "R27_FIG_BAL_SHEET")
		private BigDecimal r27_fig_bal_sheet;

		@Column(name = "R27_FIG_BAL_SHEET_BWP")
		private BigDecimal r27_fig_bal_sheet_bwp;

		@Column(name = "R27_AMT_STATEMENT_ADJ")
		private BigDecimal r27_amt_statement_adj;

		@Column(name = "R27_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r27_amt_statement_adj_bwp;

		@Column(name = "R27_NET_AMT")
		private BigDecimal r27_net_amt;

		@Column(name = "R27_NET_AMT_BWP")
		private BigDecimal r27_net_amt_bwp;

		@Column(name = "R27_BAL_SUB")
		private BigDecimal r27_bal_sub;

		@Column(name = "R27_BAL_SUB_BWP")
		private BigDecimal r27_bal_sub_bwp;

		@Column(name = "R27_BAL_SUB_DIARIES_BWP")
		private BigDecimal r27_bal_sub_diaries_bwp;

		// ================= R28 =================
		@Column(name = "R28_OTHER_INCOME")
		private String r28_other_income;

		@Column(name = "R28_FIG_BAL_SHEET")
		private BigDecimal r28_fig_bal_sheet;

		@Column(name = "R28_FIG_BAL_SHEET_BWP")
		private BigDecimal r28_fig_bal_sheet_bwp;

		@Column(name = "R28_AMT_STATEMENT_ADJ")
		private BigDecimal r28_amt_statement_adj;

		@Column(name = "R28_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r28_amt_statement_adj_bwp;

		@Column(name = "R28_NET_AMT")
		private BigDecimal r28_net_amt;

		@Column(name = "R28_NET_AMT_BWP")
		private BigDecimal r28_net_amt_bwp;

		@Column(name = "R28_BAL_SUB")
		private BigDecimal r28_bal_sub;

		@Column(name = "R28_BAL_SUB_BWP")
		private BigDecimal r28_bal_sub_bwp;

		@Column(name = "R28_BAL_SUB_DIARIES_BWP")
		private BigDecimal r28_bal_sub_diaries_bwp;

		// ================= R29 =================
		@Column(name = "R29_OTHER_INCOME")
		private String r29_other_income;

		@Column(name = "R29_FIG_BAL_SHEET")
		private BigDecimal r29_fig_bal_sheet;

		@Column(name = "R29_FIG_BAL_SHEET_BWP")
		private BigDecimal r29_fig_bal_sheet_bwp;

		@Column(name = "R29_AMT_STATEMENT_ADJ")
		private BigDecimal r29_amt_statement_adj;

		@Column(name = "R29_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r29_amt_statement_adj_bwp;

		@Column(name = "R29_NET_AMT")
		private BigDecimal r29_net_amt;

		@Column(name = "R29_NET_AMT_BWP")
		private BigDecimal r29_net_amt_bwp;

		@Column(name = "R29_BAL_SUB")
		private BigDecimal r29_bal_sub;

		@Column(name = "R29_BAL_SUB_BWP")
		private BigDecimal r29_bal_sub_bwp;

		@Column(name = "R29_BAL_SUB_DIARIES_BWP")
		private BigDecimal r29_bal_sub_diaries_bwp;

		// ================= R30 =================
		@Column(name = "R30_OTHER_INCOME")
		private String r30_other_income;

		@Column(name = "R30_FIG_BAL_SHEET")
		private BigDecimal r30_fig_bal_sheet;

		@Column(name = "R30_FIG_BAL_SHEET_BWP")
		private BigDecimal r30_fig_bal_sheet_bwp;

		@Column(name = "R30_AMT_STATEMENT_ADJ")
		private BigDecimal r30_amt_statement_adj;

		@Column(name = "R30_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r30_amt_statement_adj_bwp;

		@Column(name = "R30_NET_AMT")
		private BigDecimal r30_net_amt;

		@Column(name = "R30_NET_AMT_BWP")
		private BigDecimal r30_net_amt_bwp;

		@Column(name = "R30_BAL_SUB")
		private BigDecimal r30_bal_sub;

		@Column(name = "R30_BAL_SUB_BWP")
		private BigDecimal r30_bal_sub_bwp;

		@Column(name = "R30_BAL_SUB_DIARIES_BWP")
		private BigDecimal r30_bal_sub_diaries_bwp;

		// ================= R31 =================
		@Column(name = "R31_OTHER_INCOME")
		private String r31_other_income;

		@Column(name = "R31_FIG_BAL_SHEET")
		private BigDecimal r31_fig_bal_sheet;

		@Column(name = "R31_FIG_BAL_SHEET_BWP")
		private BigDecimal r31_fig_bal_sheet_bwp;

		@Column(name = "R31_AMT_STATEMENT_ADJ")
		private BigDecimal r31_amt_statement_adj;

		@Column(name = "R31_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r31_amt_statement_adj_bwp;

		@Column(name = "R31_NET_AMT")
		private BigDecimal r31_net_amt;

		@Column(name = "R31_NET_AMT_BWP")
		private BigDecimal r31_net_amt_bwp;

		@Column(name = "R31_BAL_SUB")
		private BigDecimal r31_bal_sub;

		@Column(name = "R31_BAL_SUB_BWP")
		private BigDecimal r31_bal_sub_bwp;

		@Column(name = "R31_BAL_SUB_DIARIES_BWP")
		private BigDecimal r31_bal_sub_diaries_bwp;

		// ================= R40 =================
		@Column(name = "R40_INTREST_EXPENDED")
		private String r40_intrest_expended;

		@Column(name = "R40_FIG_BAL_SHEET")
		private BigDecimal r40_fig_bal_sheet;

		@Column(name = "R40_FIG_BAL_SHEET_BWP")
		private BigDecimal r40_fig_bal_sheet_bwp;

		@Column(name = "R40_AMT_STATEMENT_ADJ")
		private BigDecimal r40_amt_statement_adj;

		@Column(name = "R40_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r40_amt_statement_adj_bwp;

		@Column(name = "R40_NET_AMT")
		private BigDecimal r40_net_amt;

		@Column(name = "R40_NET_AMT_BWP")
		private BigDecimal r40_net_amt_bwp;

		@Column(name = "R40_BAL_SUB")
		private BigDecimal r40_bal_sub;

		@Column(name = "R40_BAL_SUB_BWP")
		private BigDecimal r40_bal_sub_bwp;

		@Column(name = "R40_BAL_SUB_DIARIES_BWP")
		private BigDecimal r40_bal_sub_diaries_bwp;

		// ================= R41 =================
		@Column(name = "R41_INTREST_EXPENDED")
		private String r41_intrest_expended;

		@Column(name = "R41_FIG_BAL_SHEET")
		private BigDecimal r41_fig_bal_sheet;

		@Column(name = "R41_FIG_BAL_SHEET_BWP")
		private BigDecimal r41_fig_bal_sheet_bwp;

		@Column(name = "R41_AMT_STATEMENT_ADJ")
		private BigDecimal r41_amt_statement_adj;

		@Column(name = "R41_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r41_amt_statement_adj_bwp;

		@Column(name = "R41_NET_AMT")
		private BigDecimal r41_net_amt;

		@Column(name = "R41_NET_AMT_BWP")
		private BigDecimal r41_net_amt_bwp;

		@Column(name = "R41_BAL_SUB")
		private BigDecimal r41_bal_sub;

		@Column(name = "R41_BAL_SUB_BWP")
		private BigDecimal r41_bal_sub_bwp;

		@Column(name = "R41_BAL_SUB_DIARIES_BWP")
		private BigDecimal r41_bal_sub_diaries_bwp;

		// ================= R42 =================
		@Column(name = "R42_INTREST_EXPENDED")
		private String r42_intrest_expended;

		@Column(name = "R42_FIG_BAL_SHEET")
		private BigDecimal r42_fig_bal_sheet;

		@Column(name = "R42_FIG_BAL_SHEET_BWP")
		private BigDecimal r42_fig_bal_sheet_bwp;

		@Column(name = "R42_AMT_STATEMENT_ADJ")
		private BigDecimal r42_amt_statement_adj;

		@Column(name = "R42_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r42_amt_statement_adj_bwp;

		@Column(name = "R42_NET_AMT")
		private BigDecimal r42_net_amt;

		@Column(name = "R42_NET_AMT_BWP")
		private BigDecimal r42_net_amt_bwp;

		@Column(name = "R42_BAL_SUB")
		private BigDecimal r42_bal_sub;

		@Column(name = "R42_BAL_SUB_BWP")
		private BigDecimal r42_bal_sub_bwp;

		@Column(name = "R42_BAL_SUB_DIARIES_BWP")
		private BigDecimal r42_bal_sub_diaries_bwp;

		// ================= R43 =================
		@Column(name = "R43_INTREST_EXPENDED")
		private String r43_intrest_expended;

		@Column(name = "R43_FIG_BAL_SHEET")
		private BigDecimal r43_fig_bal_sheet;

		@Column(name = "R43_FIG_BAL_SHEET_BWP")
		private BigDecimal r43_fig_bal_sheet_bwp;

		@Column(name = "R43_AMT_STATEMENT_ADJ")
		private BigDecimal r43_amt_statement_adj;

		@Column(name = "R43_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r43_amt_statement_adj_bwp;

		@Column(name = "R43_NET_AMT")
		private BigDecimal r43_net_amt;

		@Column(name = "R43_NET_AMT_BWP")
		private BigDecimal r43_net_amt_bwp;

		@Column(name = "R43_BAL_SUB")
		private BigDecimal r43_bal_sub;

		@Column(name = "R43_BAL_SUB_BWP")
		private BigDecimal r43_bal_sub_bwp;

		@Column(name = "R43_BAL_SUB_DIARIES_BWP")
		private BigDecimal r43_bal_sub_diaries_bwp;

		// ================= R48 =================
		@Column(name = "R48_OPERATING_EXPENSES")
		private String r48_operating_expenses;

		@Column(name = "R48_FIG_BAL_SHEET")
		private BigDecimal r48_fig_bal_sheet;

		@Column(name = "R48_FIG_BAL_SHEET_BWP")
		private BigDecimal r48_fig_bal_sheet_bwp;

		@Column(name = "R48_AMT_STATEMENT_ADJ")
		private BigDecimal r48_amt_statement_adj;

		@Column(name = "R48_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r48_amt_statement_adj_bwp;

		@Column(name = "R48_NET_AMT")
		private BigDecimal r48_net_amt;

		@Column(name = "R48_NET_AMT_BWP")
		private BigDecimal r48_net_amt_bwp;

		@Column(name = "R48_BAL_SUB")
		private BigDecimal r48_bal_sub;

		@Column(name = "R48_BAL_SUB_BWP")
		private BigDecimal r48_bal_sub_bwp;

		@Column(name = "R48_BAL_SUB_DIARIES_BWP")
		private BigDecimal r48_bal_sub_diaries_bwp;

		// ================= R49 =================
		@Column(name = "R49_OPERATING_EXPENSES")
		private String r49_operating_expenses;

		@Column(name = "R49_FIG_BAL_SHEET")
		private BigDecimal r49_fig_bal_sheet;

		@Column(name = "R49_FIG_BAL_SHEET_BWP")
		private BigDecimal r49_fig_bal_sheet_bwp;

		@Column(name = "R49_AMT_STATEMENT_ADJ")
		private BigDecimal r49_amt_statement_adj;

		@Column(name = "R49_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r49_amt_statement_adj_bwp;

		@Column(name = "R49_NET_AMT")
		private BigDecimal r49_net_amt;

		@Column(name = "R49_NET_AMT_BWP")
		private BigDecimal r49_net_amt_bwp;

		@Column(name = "R49_BAL_SUB")
		private BigDecimal r49_bal_sub;

		@Column(name = "R49_BAL_SUB_BWP")
		private BigDecimal r49_bal_sub_bwp;

		@Column(name = "R49_BAL_SUB_DIARIES_BWP")
		private BigDecimal r49_bal_sub_diaries_bwp;

		// ================= R50 =================
		@Column(name = "R50_OPERATING_EXPENSES")
		private String r50_operating_expenses;

		@Column(name = "R50_FIG_BAL_SHEET")
		private BigDecimal r50_fig_bal_sheet;

		@Column(name = "R50_FIG_BAL_SHEET_BWP")
		private BigDecimal r50_fig_bal_sheet_bwp;

		@Column(name = "R50_AMT_STATEMENT_ADJ")
		private BigDecimal r50_amt_statement_adj;

		@Column(name = "R50_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r50_amt_statement_adj_bwp;

		@Column(name = "R50_NET_AMT")
		private BigDecimal r50_net_amt;

		@Column(name = "R50_NET_AMT_BWP")
		private BigDecimal r50_net_amt_bwp;

		@Column(name = "R50_BAL_SUB")
		private BigDecimal r50_bal_sub;

		@Column(name = "R50_BAL_SUB_BWP")
		private BigDecimal r50_bal_sub_bwp;

		@Column(name = "R50_BAL_SUB_DIARIES_BWP")
		private BigDecimal r50_bal_sub_diaries_bwp;

		// ================= R51 =================
		@Column(name = "R51_OPERATING_EXPENSES")
		private String r51_operating_expenses;

		@Column(name = "R51_FIG_BAL_SHEET")
		private BigDecimal r51_fig_bal_sheet;

		@Column(name = "R51_FIG_BAL_SHEET_BWP")
		private BigDecimal r51_fig_bal_sheet_bwp;

		@Column(name = "R51_AMT_STATEMENT_ADJ")
		private BigDecimal r51_amt_statement_adj;

		@Column(name = "R51_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r51_amt_statement_adj_bwp;

		@Column(name = "R51_NET_AMT")
		private BigDecimal r51_net_amt;

		@Column(name = "R51_NET_AMT_BWP")
		private BigDecimal r51_net_amt_bwp;

		@Column(name = "R51_BAL_SUB")
		private BigDecimal r51_bal_sub;

		@Column(name = "R51_BAL_SUB_BWP")
		private BigDecimal r51_bal_sub_bwp;

		@Column(name = "R51_BAL_SUB_DIARIES_BWP")
		private BigDecimal r51_bal_sub_diaries_bwp;

		@Column(name = "R52_OPERATING_EXPENSES")
		private String r52_operating_expenses;

		@Column(name = "R52_FIG_BAL_SHEET")
		private BigDecimal r52_fig_bal_sheet;

		@Column(name = "R52_FIG_BAL_SHEET_BWP")
		private BigDecimal r52_fig_bal_sheet_bwp;

		@Column(name = "R52_AMT_STATEMENT_ADJ")
		private BigDecimal r52_amt_statement_adj;

		@Column(name = "R52_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r52_amt_statement_adj_bwp;

		@Column(name = "R52_NET_AMT")
		private BigDecimal r52_net_amt;

		@Column(name = "R52_NET_AMT_BWP")
		private BigDecimal r52_net_amt_bwp;

		@Column(name = "R52_BAL_SUB")
		private BigDecimal r52_bal_sub;

		@Column(name = "R52_BAL_SUB_BWP")
		private BigDecimal r52_bal_sub_bwp;

		@Column(name = "R52_BAL_SUB_DIARIES_BWP")
		private BigDecimal r52_bal_sub_diaries_bwp;

		@Column(name = "R53_OPERATING_EXPENSES")
		private String r53_operating_expenses;

		@Column(name = "R53_FIG_BAL_SHEET")
		private BigDecimal r53_fig_bal_sheet;

		@Column(name = "R53_FIG_BAL_SHEET_BWP")
		private BigDecimal r53_fig_bal_sheet_bwp;

		@Column(name = "R53_AMT_STATEMENT_ADJ")
		private BigDecimal r53_amt_statement_adj;

		@Column(name = "R53_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r53_amt_statement_adj_bwp;

		@Column(name = "R53_NET_AMT")
		private BigDecimal r53_net_amt;

		@Column(name = "R53_NET_AMT_BWP")
		private BigDecimal r53_net_amt_bwp;

		@Column(name = "R53_BAL_SUB")
		private BigDecimal r53_bal_sub;

		@Column(name = "R53_BAL_SUB_BWP")
		private BigDecimal r53_bal_sub_bwp;

		@Column(name = "R53_BAL_SUB_DIARIES_BWP")
		private BigDecimal r53_bal_sub_diaries_bwp;

		@Column(name = "R54_OPERATING_EXPENSES")
		private String r54_operating_expenses;

		@Column(name = "R54_FIG_BAL_SHEET")
		private BigDecimal r54_fig_bal_sheet;

		@Column(name = "R54_FIG_BAL_SHEET_BWP")
		private BigDecimal r54_fig_bal_sheet_bwp;

		@Column(name = "R54_AMT_STATEMENT_ADJ")
		private BigDecimal r54_amt_statement_adj;

		@Column(name = "R54_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r54_amt_statement_adj_bwp;

		@Column(name = "R54_NET_AMT")
		private BigDecimal r54_net_amt;

		@Column(name = "R54_NET_AMT_BWP")
		private BigDecimal r54_net_amt_bwp;

		@Column(name = "R54_BAL_SUB")
		private BigDecimal r54_bal_sub;

		@Column(name = "R54_BAL_SUB_BWP")
		private BigDecimal r54_bal_sub_bwp;

		@Column(name = "R54_BAL_SUB_DIARIES_BWP")
		private BigDecimal r54_bal_sub_diaries_bwp;

		@Column(name = "R55_OPERATING_EXPENSES")
		private String r55_operating_expenses;

		@Column(name = "R55_FIG_BAL_SHEET")
		private BigDecimal r55_fig_bal_sheet;

		@Column(name = "R55_FIG_BAL_SHEET_BWP")
		private BigDecimal r55_fig_bal_sheet_bwp;

		@Column(name = "R55_AMT_STATEMENT_ADJ")
		private BigDecimal r55_amt_statement_adj;

		@Column(name = "R55_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r55_amt_statement_adj_bwp;

		@Column(name = "R55_NET_AMT")
		private BigDecimal r55_net_amt;

		@Column(name = "R55_NET_AMT_BWP")
		private BigDecimal r55_net_amt_bwp;

		@Column(name = "R55_BAL_SUB")
		private BigDecimal r55_bal_sub;

		@Column(name = "R55_BAL_SUB_BWP")
		private BigDecimal r55_bal_sub_bwp;

		@Column(name = "R55_BAL_SUB_DIARIES_BWP")
		private BigDecimal r55_bal_sub_diaries_bwp;

		@Column(name = "R56_OPERATING_EXPENSES")
		private String r56_operating_expenses;

		@Column(name = "R56_FIG_BAL_SHEET")
		private BigDecimal r56_fig_bal_sheet;

		@Column(name = "R56_FIG_BAL_SHEET_BWP")
		private BigDecimal r56_fig_bal_sheet_bwp;

		@Column(name = "R56_AMT_STATEMENT_ADJ")
		private BigDecimal r56_amt_statement_adj;

		@Column(name = "R56_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r56_amt_statement_adj_bwp;

		@Column(name = "R56_NET_AMT")
		private BigDecimal r56_net_amt;

		@Column(name = "R56_NET_AMT_BWP")
		private BigDecimal r56_net_amt_bwp;

		@Column(name = "R56_BAL_SUB")
		private BigDecimal r56_bal_sub;

		@Column(name = "R56_BAL_SUB_BWP")
		private BigDecimal r56_bal_sub_bwp;

		@Column(name = "R56_BAL_SUB_DIARIES_BWP")
		private BigDecimal r56_bal_sub_diaries_bwp;

		@Column(name = "R57_OPERATING_EXPENSES")
		private String r57_operating_expenses;

		@Column(name = "R57_FIG_BAL_SHEET")
		private BigDecimal r57_fig_bal_sheet;

		@Column(name = "R57_FIG_BAL_SHEET_BWP")
		private BigDecimal r57_fig_bal_sheet_bwp;

		@Column(name = "R57_AMT_STATEMENT_ADJ")
		private BigDecimal r57_amt_statement_adj;

		@Column(name = "R57_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r57_amt_statement_adj_bwp;

		@Column(name = "R57_NET_AMT")
		private BigDecimal r57_net_amt;

		@Column(name = "R57_NET_AMT_BWP")
		private BigDecimal r57_net_amt_bwp;

		@Column(name = "R57_BAL_SUB")
		private BigDecimal r57_bal_sub;

		@Column(name = "R57_BAL_SUB_BWP")
		private BigDecimal r57_bal_sub_bwp;

		@Column(name = "R57_BAL_SUB_DIARIES_BWP")
		private BigDecimal r57_bal_sub_diaries_bwp;

		@Column(name = "R58_OPERATING_EXPENSES")
		private String r58_operating_expenses;

		@Column(name = "R58_FIG_BAL_SHEET")
		private BigDecimal r58_fig_bal_sheet;

		@Column(name = "R58_FIG_BAL_SHEET_BWP")
		private BigDecimal r58_fig_bal_sheet_bwp;

		@Column(name = "R58_AMT_STATEMENT_ADJ")
		private BigDecimal r58_amt_statement_adj;

		@Column(name = "R58_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r58_amt_statement_adj_bwp;

		@Column(name = "R58_NET_AMT")
		private BigDecimal r58_net_amt;

		@Column(name = "R58_NET_AMT_BWP")
		private BigDecimal r58_net_amt_bwp;

		@Column(name = "R58_BAL_SUB")
		private BigDecimal r58_bal_sub;

		@Column(name = "R58_BAL_SUB_BWP")
		private BigDecimal r58_bal_sub_bwp;

		@Column(name = "R58_BAL_SUB_DIARIES_BWP")
		private BigDecimal r58_bal_sub_diaries_bwp;

		@Column(name = "R59_OPERATING_EXPENSES")
		private String r59_operating_expenses;

		@Column(name = "R59_FIG_BAL_SHEET")
		private BigDecimal r59_fig_bal_sheet;

		@Column(name = "R59_FIG_BAL_SHEET_BWP")
		private BigDecimal r59_fig_bal_sheet_bwp;

		@Column(name = "R59_AMT_STATEMENT_ADJ")
		private BigDecimal r59_amt_statement_adj;

		@Column(name = "R59_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r59_amt_statement_adj_bwp;

		@Column(name = "R59_NET_AMT")
		private BigDecimal r59_net_amt;

		@Column(name = "R59_NET_AMT_BWP")
		private BigDecimal r59_net_amt_bwp;

		@Column(name = "R59_BAL_SUB")
		private BigDecimal r59_bal_sub;

		@Column(name = "R59_BAL_SUB_BWP")
		private BigDecimal r59_bal_sub_bwp;

		@Column(name = "R59_BAL_SUB_DIARIES_BWP")
		private BigDecimal r59_bal_sub_diaries_bwp;

		@Column(name = "R60_OPERATING_EXPENSES")
		private String r60_operating_expenses;

		@Column(name = "R60_FIG_BAL_SHEET")
		private BigDecimal r60_fig_bal_sheet;

		@Column(name = "R60_FIG_BAL_SHEET_BWP")
		private BigDecimal r60_fig_bal_sheet_bwp;

		@Column(name = "R60_AMT_STATEMENT_ADJ")
		private BigDecimal r60_amt_statement_adj;

		@Column(name = "R60_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r60_amt_statement_adj_bwp;

		@Column(name = "R60_NET_AMT")
		private BigDecimal r60_net_amt;

		@Column(name = "R60_NET_AMT_BWP")
		private BigDecimal r60_net_amt_bwp;

		@Column(name = "R60_BAL_SUB")
		private BigDecimal r60_bal_sub;

		@Column(name = "R60_BAL_SUB_BWP")
		private BigDecimal r60_bal_sub_bwp;

		@Column(name = "R60_BAL_SUB_DIARIES_BWP")
		private BigDecimal r60_bal_sub_diaries_bwp;

		@Column(name = "R61_OPERATING_EXPENSES")
		private String r61_operating_expenses;

		@Column(name = "R61_FIG_BAL_SHEET")
		private BigDecimal r61_fig_bal_sheet;

		@Column(name = "R61_FIG_BAL_SHEET_BWP")
		private BigDecimal r61_fig_bal_sheet_bwp;

		@Column(name = "R61_AMT_STATEMENT_ADJ")
		private BigDecimal r61_amt_statement_adj;

		@Column(name = "R61_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r61_amt_statement_adj_bwp;

		@Column(name = "R61_NET_AMT")
		private BigDecimal r61_net_amt;

		@Column(name = "R61_NET_AMT_BWP")
		private BigDecimal r61_net_amt_bwp;

		@Column(name = "R61_BAL_SUB")
		private BigDecimal r61_bal_sub;

		@Column(name = "R61_BAL_SUB_BWP")
		private BigDecimal r61_bal_sub_bwp;

		@Column(name = "R61_BAL_SUB_DIARIES_BWP")
		private BigDecimal r61_bal_sub_diaries_bwp;

		@Column(name = "R62_OPERATING_EXPENSES")
		private String r62_operating_expenses;

		@Column(name = "R62_FIG_BAL_SHEET")
		private BigDecimal r62_fig_bal_sheet;

		@Column(name = "R62_FIG_BAL_SHEET_BWP")
		private BigDecimal r62_fig_bal_sheet_bwp;

		@Column(name = "R62_AMT_STATEMENT_ADJ")
		private BigDecimal r62_amt_statement_adj;

		@Column(name = "R62_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r62_amt_statement_adj_bwp;

		@Column(name = "R62_NET_AMT")
		private BigDecimal r62_net_amt;

		@Column(name = "R62_NET_AMT_BWP")
		private BigDecimal r62_net_amt_bwp;

		@Column(name = "R62_BAL_SUB")
		private BigDecimal r62_bal_sub;

		@Column(name = "R62_BAL_SUB_BWP")
		private BigDecimal r62_bal_sub_bwp;

		@Column(name = "R62_BAL_SUB_DIARIES_BWP")
		private BigDecimal r62_bal_sub_diaries_bwp;

		@Column(name = "R63_OPERATING_EXPENSES")
		private String r63_operating_expenses;

		@Column(name = "R63_FIG_BAL_SHEET")
		private BigDecimal r63_fig_bal_sheet;

		@Column(name = "R63_FIG_BAL_SHEET_BWP")
		private BigDecimal r63_fig_bal_sheet_bwp;

		@Column(name = "R63_AMT_STATEMENT_ADJ")
		private BigDecimal r63_amt_statement_adj;

		@Column(name = "R63_AMT_STATEMENT_ADJ_BWP")
		private BigDecimal r63_amt_statement_adj_bwp;

		@Column(name = "R63_NET_AMT")
		private BigDecimal r63_net_amt;

		@Column(name = "R63_NET_AMT_BWP")
		private BigDecimal r63_net_amt_bwp;

		@Column(name = "R63_BAL_SUB")
		private BigDecimal r63_bal_sub;

		@Column(name = "R63_BAL_SUB_BWP")
		private BigDecimal r63_bal_sub_bwp;

		@Column(name = "R63_BAL_SUB_DIARIES_BWP")
		private BigDecimal r63_bal_sub_diaries_bwp;

		@Column(name = "R18_BAL_SUB_DIARIES")
		private BigDecimal r18_bal_sub_diaries;

		@Column(name = "R19_BAL_SUB_DIARIES")
		private BigDecimal r19_bal_sub_diaries;

		@Column(name = "R20_BAL_SUB_DIARIES")
		private BigDecimal r20_bal_sub_diaries;

		@Column(name = "R21_BAL_SUB_DIARIES")
		private BigDecimal r21_bal_sub_diaries;

		@Column(name = "R22_BAL_SUB_DIARIES")
		private BigDecimal r22_bal_sub_diaries;

		@Column(name = "R23_BAL_SUB_DIARIES")
		private BigDecimal r23_bal_sub_diaries;

		@Column(name = "R24_BAL_SUB_DIARIES")
		private BigDecimal r24_bal_sub_diaries;

		@Column(name = "R25_BAL_SUB_DIARIES")
		private BigDecimal r25_bal_sub_diaries;

		@Column(name = "R26_BAL_SUB_DIARIES")
		private BigDecimal r26_bal_sub_diaries;

		@Column(name = "R27_BAL_SUB_DIARIES")
		private BigDecimal r27_bal_sub_diaries;

		@Column(name = "R28_BAL_SUB_DIARIES")
		private BigDecimal r28_bal_sub_diaries;

		@Column(name = "R29_BAL_SUB_DIARIES")
		private BigDecimal r29_bal_sub_diaries;

		@Column(name = "R30_BAL_SUB_DIARIES")
		private BigDecimal r30_bal_sub_diaries;

		@Column(name = "R31_BAL_SUB_DIARIES")
		private BigDecimal r31_bal_sub_diaries;

		@Column(name = "R40_BAL_SUB_DIARIES")
		private BigDecimal r40_bal_sub_diaries;

		@Column(name = "R41_BAL_SUB_DIARIES")
		private BigDecimal r41_bal_sub_diaries;

		@Column(name = "R42_BAL_SUB_DIARIES")
		private BigDecimal r42_bal_sub_diaries;

		@Column(name = "R43_BAL_SUB_DIARIES")
		private BigDecimal r43_bal_sub_diaries;

		@Column(name = "R48_BAL_SUB_DIARIES")
		private BigDecimal r48_bal_sub_diaries;

		@Column(name = "R49_BAL_SUB_DIARIES")
		private BigDecimal r49_bal_sub_diaries;

		@Column(name = "R50_BAL_SUB_DIARIES")
		private BigDecimal r50_bal_sub_diaries;

		@Column(name = "R51_BAL_SUB_DIARIES")
		private BigDecimal r51_bal_sub_diaries;

		@Column(name = "R52_BAL_SUB_DIARIES")
		private BigDecimal r52_bal_sub_diaries;

		@Column(name = "R53_BAL_SUB_DIARIES")
		private BigDecimal r53_bal_sub_diaries;

		@Column(name = "R54_BAL_SUB_DIARIES")
		private BigDecimal r54_bal_sub_diaries;

		@Column(name = "R55_BAL_SUB_DIARIES")
		private BigDecimal r55_bal_sub_diaries;

		@Column(name = "R56_BAL_SUB_DIARIES")
		private BigDecimal r56_bal_sub_diaries;

		@Column(name = "R57_BAL_SUB_DIARIES")
		private BigDecimal r57_bal_sub_diaries;

		@Column(name = "R58_BAL_SUB_DIARIES")
		private BigDecimal r58_bal_sub_diaries;

		@Column(name = "R59_BAL_SUB_DIARIES")
		private BigDecimal r59_bal_sub_diaries;

		@Column(name = "R60_BAL_SUB_DIARIES")
		private BigDecimal r60_bal_sub_diaries;

		@Column(name = "R61_BAL_SUB_DIARIES")
		private BigDecimal r61_bal_sub_diaries;

		@Column(name = "R62_BAL_SUB_DIARIES")
		private BigDecimal r62_bal_sub_diaries;

		@Column(name = "R63_BAL_SUB_DIARIES")
		private BigDecimal r63_bal_sub_diaries;

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

		public BigDecimal getR18_bal_sub_diaries() {
			return r18_bal_sub_diaries;
		}

		public void setR18_bal_sub_diaries(BigDecimal r18_bal_sub_diaries) {
			this.r18_bal_sub_diaries = r18_bal_sub_diaries;
		}

		public BigDecimal getR19_bal_sub_diaries() {
			return r19_bal_sub_diaries;
		}

		public void setR19_bal_sub_diaries(BigDecimal r19_bal_sub_diaries) {
			this.r19_bal_sub_diaries = r19_bal_sub_diaries;
		}

		public BigDecimal getR20_bal_sub_diaries() {
			return r20_bal_sub_diaries;
		}

		public void setR20_bal_sub_diaries(BigDecimal r20_bal_sub_diaries) {
			this.r20_bal_sub_diaries = r20_bal_sub_diaries;
		}

		public BigDecimal getR21_bal_sub_diaries() {
			return r21_bal_sub_diaries;
		}

		public void setR21_bal_sub_diaries(BigDecimal r21_bal_sub_diaries) {
			this.r21_bal_sub_diaries = r21_bal_sub_diaries;
		}

		public BigDecimal getR22_bal_sub_diaries() {
			return r22_bal_sub_diaries;
		}

		public void setR22_bal_sub_diaries(BigDecimal r22_bal_sub_diaries) {
			this.r22_bal_sub_diaries = r22_bal_sub_diaries;
		}

		public BigDecimal getR23_bal_sub_diaries() {
			return r23_bal_sub_diaries;
		}

		public void setR23_bal_sub_diaries(BigDecimal r23_bal_sub_diaries) {
			this.r23_bal_sub_diaries = r23_bal_sub_diaries;
		}

		public BigDecimal getR24_bal_sub_diaries() {
			return r24_bal_sub_diaries;
		}

		public void setR24_bal_sub_diaries(BigDecimal r24_bal_sub_diaries) {
			this.r24_bal_sub_diaries = r24_bal_sub_diaries;
		}

		public BigDecimal getR25_bal_sub_diaries() {
			return r25_bal_sub_diaries;
		}

		public void setR25_bal_sub_diaries(BigDecimal r25_bal_sub_diaries) {
			this.r25_bal_sub_diaries = r25_bal_sub_diaries;
		}

		public BigDecimal getR26_bal_sub_diaries() {
			return r26_bal_sub_diaries;
		}

		public void setR26_bal_sub_diaries(BigDecimal r26_bal_sub_diaries) {
			this.r26_bal_sub_diaries = r26_bal_sub_diaries;
		}

		public BigDecimal getR27_bal_sub_diaries() {
			return r27_bal_sub_diaries;
		}

		public void setR27_bal_sub_diaries(BigDecimal r27_bal_sub_diaries) {
			this.r27_bal_sub_diaries = r27_bal_sub_diaries;
		}

		public BigDecimal getR28_bal_sub_diaries() {
			return r28_bal_sub_diaries;
		}

		public void setR28_bal_sub_diaries(BigDecimal r28_bal_sub_diaries) {
			this.r28_bal_sub_diaries = r28_bal_sub_diaries;
		}

		public BigDecimal getR29_bal_sub_diaries() {
			return r29_bal_sub_diaries;
		}

		public void setR29_bal_sub_diaries(BigDecimal r29_bal_sub_diaries) {
			this.r29_bal_sub_diaries = r29_bal_sub_diaries;
		}

		public BigDecimal getR30_bal_sub_diaries() {
			return r30_bal_sub_diaries;
		}

		public void setR30_bal_sub_diaries(BigDecimal r30_bal_sub_diaries) {
			this.r30_bal_sub_diaries = r30_bal_sub_diaries;
		}

		public BigDecimal getR31_bal_sub_diaries() {
			return r31_bal_sub_diaries;
		}

		public void setR31_bal_sub_diaries(BigDecimal r31_bal_sub_diaries) {
			this.r31_bal_sub_diaries = r31_bal_sub_diaries;
		}

		public BigDecimal getR40_bal_sub_diaries() {
			return r40_bal_sub_diaries;
		}

		public void setR40_bal_sub_diaries(BigDecimal r40_bal_sub_diaries) {
			this.r40_bal_sub_diaries = r40_bal_sub_diaries;
		}

		public BigDecimal getR41_bal_sub_diaries() {
			return r41_bal_sub_diaries;
		}

		public void setR41_bal_sub_diaries(BigDecimal r41_bal_sub_diaries) {
			this.r41_bal_sub_diaries = r41_bal_sub_diaries;
		}

		public BigDecimal getR42_bal_sub_diaries() {
			return r42_bal_sub_diaries;
		}

		public void setR42_bal_sub_diaries(BigDecimal r42_bal_sub_diaries) {
			this.r42_bal_sub_diaries = r42_bal_sub_diaries;
		}

		public BigDecimal getR43_bal_sub_diaries() {
			return r43_bal_sub_diaries;
		}

		public void setR43_bal_sub_diaries(BigDecimal r43_bal_sub_diaries) {
			this.r43_bal_sub_diaries = r43_bal_sub_diaries;
		}

		public BigDecimal getR49_bal_sub_diaries() {
			return r49_bal_sub_diaries;
		}

		public void setR49_bal_sub_diaries(BigDecimal r49_bal_sub_diaries) {
			this.r49_bal_sub_diaries = r49_bal_sub_diaries;
		}

		public BigDecimal getR50_bal_sub_diaries() {
			return r50_bal_sub_diaries;
		}

		public void setR50_bal_sub_diaries(BigDecimal r50_bal_sub_diaries) {
			this.r50_bal_sub_diaries = r50_bal_sub_diaries;
		}

		public BigDecimal getR51_bal_sub_diaries() {
			return r51_bal_sub_diaries;
		}

		public void setR51_bal_sub_diaries(BigDecimal r51_bal_sub_diaries) {
			this.r51_bal_sub_diaries = r51_bal_sub_diaries;
		}

		public BigDecimal getR52_bal_sub_diaries() {
			return r52_bal_sub_diaries;
		}

		public void setR52_bal_sub_diaries(BigDecimal r52_bal_sub_diaries) {
			this.r52_bal_sub_diaries = r52_bal_sub_diaries;
		}

		public BigDecimal getR53_bal_sub_diaries() {
			return r53_bal_sub_diaries;
		}

		public void setR53_bal_sub_diaries(BigDecimal r53_bal_sub_diaries) {
			this.r53_bal_sub_diaries = r53_bal_sub_diaries;
		}

		public BigDecimal getR54_bal_sub_diaries() {
			return r54_bal_sub_diaries;
		}

		public void setR54_bal_sub_diaries(BigDecimal r54_bal_sub_diaries) {
			this.r54_bal_sub_diaries = r54_bal_sub_diaries;
		}

		public BigDecimal getR55_bal_sub_diaries() {
			return r55_bal_sub_diaries;
		}

		public void setR55_bal_sub_diaries(BigDecimal r55_bal_sub_diaries) {
			this.r55_bal_sub_diaries = r55_bal_sub_diaries;
		}

		public BigDecimal getR56_bal_sub_diaries() {
			return r56_bal_sub_diaries;
		}

		public void setR56_bal_sub_diaries(BigDecimal r56_bal_sub_diaries) {
			this.r56_bal_sub_diaries = r56_bal_sub_diaries;
		}

		public BigDecimal getR57_bal_sub_diaries() {
			return r57_bal_sub_diaries;
		}

		public void setR57_bal_sub_diaries(BigDecimal r57_bal_sub_diaries) {
			this.r57_bal_sub_diaries = r57_bal_sub_diaries;
		}

		public BigDecimal getR58_bal_sub_diaries() {
			return r58_bal_sub_diaries;
		}

		public void setR58_bal_sub_diaries(BigDecimal r58_bal_sub_diaries) {
			this.r58_bal_sub_diaries = r58_bal_sub_diaries;
		}

		public BigDecimal getR59_bal_sub_diaries() {
			return r59_bal_sub_diaries;
		}

		public void setR59_bal_sub_diaries(BigDecimal r59_bal_sub_diaries) {
			this.r59_bal_sub_diaries = r59_bal_sub_diaries;
		}

		public BigDecimal getR60_bal_sub_diaries() {
			return r60_bal_sub_diaries;
		}

		public void setR60_bal_sub_diaries(BigDecimal r60_bal_sub_diaries) {
			this.r60_bal_sub_diaries = r60_bal_sub_diaries;
		}

		public BigDecimal getR61_bal_sub_diaries() {
			return r61_bal_sub_diaries;
		}

		public void setR61_bal_sub_diaries(BigDecimal r61_bal_sub_diaries) {
			this.r61_bal_sub_diaries = r61_bal_sub_diaries;
		}

		public BigDecimal getR62_bal_sub_diaries() {
			return r62_bal_sub_diaries;
		}

		public void setR62_bal_sub_diaries(BigDecimal r62_bal_sub_diaries) {
			this.r62_bal_sub_diaries = r62_bal_sub_diaries;
		}

		public BigDecimal getR63_bal_sub_diaries() {
			return r63_bal_sub_diaries;
		}

		public void setR63_bal_sub_diaries(BigDecimal r63_bal_sub_diaries) {
			this.r63_bal_sub_diaries = r63_bal_sub_diaries;
		}

		public String getR9_intrest_div() {
			return r9_intrest_div;
		}

		public void setR9_intrest_div(String r9_intrest_div) {
			this.r9_intrest_div = r9_intrest_div;
		}

		public BigDecimal getR9_fig_bal_sheet() {
			return r9_fig_bal_sheet;
		}

		public void setR9_fig_bal_sheet(BigDecimal r9_fig_bal_sheet) {
			this.r9_fig_bal_sheet = r9_fig_bal_sheet;
		}

		public BigDecimal getR9_fig_bal_sheet_bwp() {
			return r9_fig_bal_sheet_bwp;
		}

		public void setR9_fig_bal_sheet_bwp(BigDecimal r9_fig_bal_sheet_bwp) {
			this.r9_fig_bal_sheet_bwp = r9_fig_bal_sheet_bwp;
		}

		public BigDecimal getR9_amt_statement_adj() {
			return r9_amt_statement_adj;
		}

		public void setR9_amt_statement_adj(BigDecimal r9_amt_statement_adj) {
			this.r9_amt_statement_adj = r9_amt_statement_adj;
		}

		public BigDecimal getR9_amt_statement_adj_bwp() {
			return r9_amt_statement_adj_bwp;
		}

		public void setR9_amt_statement_adj_bwp(BigDecimal r9_amt_statement_adj_bwp) {
			this.r9_amt_statement_adj_bwp = r9_amt_statement_adj_bwp;
		}

		public BigDecimal getR9_net_amt() {
			return r9_net_amt;
		}

		public void setR9_net_amt(BigDecimal r9_net_amt) {
			this.r9_net_amt = r9_net_amt;
		}

		public BigDecimal getR9_net_amt_bwp() {
			return r9_net_amt_bwp;
		}

		public void setR9_net_amt_bwp(BigDecimal r9_net_amt_bwp) {
			this.r9_net_amt_bwp = r9_net_amt_bwp;
		}

		public BigDecimal getR9_bal_sub() {
			return r9_bal_sub;
		}

		public void setR9_bal_sub(BigDecimal r9_bal_sub) {
			this.r9_bal_sub = r9_bal_sub;
		}

		public BigDecimal getR9_bal_sub_bwp() {
			return r9_bal_sub_bwp;
		}

		public void setR9_bal_sub_bwp(BigDecimal r9_bal_sub_bwp) {
			this.r9_bal_sub_bwp = r9_bal_sub_bwp;
		}

		public BigDecimal getR9_bal_sub_diaries() {
			return r9_bal_sub_diaries;
		}

		public void setR9_bal_sub_diaries(BigDecimal r9_bal_sub_diaries) {
			this.r9_bal_sub_diaries = r9_bal_sub_diaries;
		}

		public BigDecimal getR9_bal_sub_diaries_bwp() {
			return r9_bal_sub_diaries_bwp;
		}

		public void setR9_bal_sub_diaries_bwp(BigDecimal r9_bal_sub_diaries_bwp) {
			this.r9_bal_sub_diaries_bwp = r9_bal_sub_diaries_bwp;
		}

		public String getR10_intrest_div() {
			return r10_intrest_div;
		}

		public void setR10_intrest_div(String r10_intrest_div) {
			this.r10_intrest_div = r10_intrest_div;
		}

		public BigDecimal getR10_fig_bal_sheet() {
			return r10_fig_bal_sheet;
		}

		public void setR10_fig_bal_sheet(BigDecimal r10_fig_bal_sheet) {
			this.r10_fig_bal_sheet = r10_fig_bal_sheet;
		}

		public BigDecimal getR10_fig_bal_sheet_bwp() {
			return r10_fig_bal_sheet_bwp;
		}

		public void setR10_fig_bal_sheet_bwp(BigDecimal r10_fig_bal_sheet_bwp) {
			this.r10_fig_bal_sheet_bwp = r10_fig_bal_sheet_bwp;
		}

		public BigDecimal getR10_amt_statement_adj() {
			return r10_amt_statement_adj;
		}

		public void setR10_amt_statement_adj(BigDecimal r10_amt_statement_adj) {
			this.r10_amt_statement_adj = r10_amt_statement_adj;
		}

		public BigDecimal getR10_amt_statement_adj_bwp() {
			return r10_amt_statement_adj_bwp;
		}

		public void setR10_amt_statement_adj_bwp(BigDecimal r10_amt_statement_adj_bwp) {
			this.r10_amt_statement_adj_bwp = r10_amt_statement_adj_bwp;
		}

		public BigDecimal getR10_net_amt() {
			return r10_net_amt;
		}

		public void setR10_net_amt(BigDecimal r10_net_amt) {
			this.r10_net_amt = r10_net_amt;
		}

		public BigDecimal getR10_net_amt_bwp() {
			return r10_net_amt_bwp;
		}

		public void setR10_net_amt_bwp(BigDecimal r10_net_amt_bwp) {
			this.r10_net_amt_bwp = r10_net_amt_bwp;
		}

		public BigDecimal getR10_bal_sub() {
			return r10_bal_sub;
		}

		public void setR10_bal_sub(BigDecimal r10_bal_sub) {
			this.r10_bal_sub = r10_bal_sub;
		}

		public BigDecimal getR10_bal_sub_bwp() {
			return r10_bal_sub_bwp;
		}

		public void setR10_bal_sub_bwp(BigDecimal r10_bal_sub_bwp) {
			this.r10_bal_sub_bwp = r10_bal_sub_bwp;
		}

		public BigDecimal getR10_bal_sub_diaries() {
			return r10_bal_sub_diaries;
		}

		public void setR10_bal_sub_diaries(BigDecimal r10_bal_sub_diaries) {
			this.r10_bal_sub_diaries = r10_bal_sub_diaries;
		}

		public BigDecimal getR10_bal_sub_diaries_bwp() {
			return r10_bal_sub_diaries_bwp;
		}

		public void setR10_bal_sub_diaries_bwp(BigDecimal r10_bal_sub_diaries_bwp) {
			this.r10_bal_sub_diaries_bwp = r10_bal_sub_diaries_bwp;
		}

		public String getR11_intrest_div() {
			return r11_intrest_div;
		}

		public void setR11_intrest_div(String r11_intrest_div) {
			this.r11_intrest_div = r11_intrest_div;
		}

		public BigDecimal getR11_fig_bal_sheet() {
			return r11_fig_bal_sheet;
		}

		public void setR11_fig_bal_sheet(BigDecimal r11_fig_bal_sheet) {
			this.r11_fig_bal_sheet = r11_fig_bal_sheet;
		}

		public BigDecimal getR11_fig_bal_sheet_bwp() {
			return r11_fig_bal_sheet_bwp;
		}

		public void setR11_fig_bal_sheet_bwp(BigDecimal r11_fig_bal_sheet_bwp) {
			this.r11_fig_bal_sheet_bwp = r11_fig_bal_sheet_bwp;
		}

		public BigDecimal getR11_amt_statement_adj() {
			return r11_amt_statement_adj;
		}

		public void setR11_amt_statement_adj(BigDecimal r11_amt_statement_adj) {
			this.r11_amt_statement_adj = r11_amt_statement_adj;
		}

		public BigDecimal getR11_amt_statement_adj_bwp() {
			return r11_amt_statement_adj_bwp;
		}

		public void setR11_amt_statement_adj_bwp(BigDecimal r11_amt_statement_adj_bwp) {
			this.r11_amt_statement_adj_bwp = r11_amt_statement_adj_bwp;
		}

		public BigDecimal getR11_net_amt() {
			return r11_net_amt;
		}

		public void setR11_net_amt(BigDecimal r11_net_amt) {
			this.r11_net_amt = r11_net_amt;
		}

		public BigDecimal getR11_net_amt_bwp() {
			return r11_net_amt_bwp;
		}

		public void setR11_net_amt_bwp(BigDecimal r11_net_amt_bwp) {
			this.r11_net_amt_bwp = r11_net_amt_bwp;
		}

		public BigDecimal getR11_bal_sub() {
			return r11_bal_sub;
		}

		public void setR11_bal_sub(BigDecimal r11_bal_sub) {
			this.r11_bal_sub = r11_bal_sub;
		}

		public BigDecimal getR11_bal_sub_bwp() {
			return r11_bal_sub_bwp;
		}

		public void setR11_bal_sub_bwp(BigDecimal r11_bal_sub_bwp) {
			this.r11_bal_sub_bwp = r11_bal_sub_bwp;
		}

		public BigDecimal getR11_bal_sub_diaries() {
			return r11_bal_sub_diaries;
		}

		public void setR11_bal_sub_diaries(BigDecimal r11_bal_sub_diaries) {
			this.r11_bal_sub_diaries = r11_bal_sub_diaries;
		}

		public BigDecimal getR11_bal_sub_diaries_bwp() {
			return r11_bal_sub_diaries_bwp;
		}

		public void setR11_bal_sub_diaries_bwp(BigDecimal r11_bal_sub_diaries_bwp) {
			this.r11_bal_sub_diaries_bwp = r11_bal_sub_diaries_bwp;
		}

		public String getR12_intrest_div() {
			return r12_intrest_div;
		}

		public void setR12_intrest_div(String r12_intrest_div) {
			this.r12_intrest_div = r12_intrest_div;
		}

		public BigDecimal getR12_fig_bal_sheet() {
			return r12_fig_bal_sheet;
		}

		public void setR12_fig_bal_sheet(BigDecimal r12_fig_bal_sheet) {
			this.r12_fig_bal_sheet = r12_fig_bal_sheet;
		}

		public BigDecimal getR12_fig_bal_sheet_bwp() {
			return r12_fig_bal_sheet_bwp;
		}

		public void setR12_fig_bal_sheet_bwp(BigDecimal r12_fig_bal_sheet_bwp) {
			this.r12_fig_bal_sheet_bwp = r12_fig_bal_sheet_bwp;
		}

		public BigDecimal getR12_amt_statement_adj() {
			return r12_amt_statement_adj;
		}

		public void setR12_amt_statement_adj(BigDecimal r12_amt_statement_adj) {
			this.r12_amt_statement_adj = r12_amt_statement_adj;
		}

		public BigDecimal getR12_amt_statement_adj_bwp() {
			return r12_amt_statement_adj_bwp;
		}

		public void setR12_amt_statement_adj_bwp(BigDecimal r12_amt_statement_adj_bwp) {
			this.r12_amt_statement_adj_bwp = r12_amt_statement_adj_bwp;
		}

		public BigDecimal getR12_net_amt() {
			return r12_net_amt;
		}

		public void setR12_net_amt(BigDecimal r12_net_amt) {
			this.r12_net_amt = r12_net_amt;
		}

		public BigDecimal getR12_net_amt_bwp() {
			return r12_net_amt_bwp;
		}

		public void setR12_net_amt_bwp(BigDecimal r12_net_amt_bwp) {
			this.r12_net_amt_bwp = r12_net_amt_bwp;
		}

		public BigDecimal getR12_bal_sub() {
			return r12_bal_sub;
		}

		public void setR12_bal_sub(BigDecimal r12_bal_sub) {
			this.r12_bal_sub = r12_bal_sub;
		}

		public BigDecimal getR12_bal_sub_bwp() {
			return r12_bal_sub_bwp;
		}

		public void setR12_bal_sub_bwp(BigDecimal r12_bal_sub_bwp) {
			this.r12_bal_sub_bwp = r12_bal_sub_bwp;
		}

		public BigDecimal getR12_bal_sub_diaries() {
			return r12_bal_sub_diaries;
		}

		public void setR12_bal_sub_diaries(BigDecimal r12_bal_sub_diaries) {
			this.r12_bal_sub_diaries = r12_bal_sub_diaries;
		}

		public BigDecimal getR12_bal_sub_diaries_bwp() {
			return r12_bal_sub_diaries_bwp;
		}

		public void setR12_bal_sub_diaries_bwp(BigDecimal r12_bal_sub_diaries_bwp) {
			this.r12_bal_sub_diaries_bwp = r12_bal_sub_diaries_bwp;
		}

		public String getR13_intrest_div() {
			return r13_intrest_div;
		}

		public void setR13_intrest_div(String r13_intrest_div) {
			this.r13_intrest_div = r13_intrest_div;
		}

		public BigDecimal getR13_fig_bal_sheet() {
			return r13_fig_bal_sheet;
		}

		public void setR13_fig_bal_sheet(BigDecimal r13_fig_bal_sheet) {
			this.r13_fig_bal_sheet = r13_fig_bal_sheet;
		}

		public BigDecimal getR13_fig_bal_sheet_bwp() {
			return r13_fig_bal_sheet_bwp;
		}

		public void setR13_fig_bal_sheet_bwp(BigDecimal r13_fig_bal_sheet_bwp) {
			this.r13_fig_bal_sheet_bwp = r13_fig_bal_sheet_bwp;
		}

		public BigDecimal getR13_amt_statement_adj() {
			return r13_amt_statement_adj;
		}

		public void setR13_amt_statement_adj(BigDecimal r13_amt_statement_adj) {
			this.r13_amt_statement_adj = r13_amt_statement_adj;
		}

		public BigDecimal getR13_amt_statement_adj_bwp() {
			return r13_amt_statement_adj_bwp;
		}

		public void setR13_amt_statement_adj_bwp(BigDecimal r13_amt_statement_adj_bwp) {
			this.r13_amt_statement_adj_bwp = r13_amt_statement_adj_bwp;
		}

		public BigDecimal getR13_net_amt() {
			return r13_net_amt;
		}

		public void setR13_net_amt(BigDecimal r13_net_amt) {
			this.r13_net_amt = r13_net_amt;
		}

		public BigDecimal getR13_net_amt_bwp() {
			return r13_net_amt_bwp;
		}

		public void setR13_net_amt_bwp(BigDecimal r13_net_amt_bwp) {
			this.r13_net_amt_bwp = r13_net_amt_bwp;
		}

		public BigDecimal getR13_bal_sub() {
			return r13_bal_sub;
		}

		public void setR13_bal_sub(BigDecimal r13_bal_sub) {
			this.r13_bal_sub = r13_bal_sub;
		}

		public BigDecimal getR13_bal_sub_bwp() {
			return r13_bal_sub_bwp;
		}

		public void setR13_bal_sub_bwp(BigDecimal r13_bal_sub_bwp) {
			this.r13_bal_sub_bwp = r13_bal_sub_bwp;
		}

		public BigDecimal getR13_bal_sub_diaries() {
			return r13_bal_sub_diaries;
		}

		public void setR13_bal_sub_diaries(BigDecimal r13_bal_sub_diaries) {
			this.r13_bal_sub_diaries = r13_bal_sub_diaries;
		}

		public BigDecimal getR13_bal_sub_diaries_bwp() {
			return r13_bal_sub_diaries_bwp;
		}

		public void setR13_bal_sub_diaries_bwp(BigDecimal r13_bal_sub_diaries_bwp) {
			this.r13_bal_sub_diaries_bwp = r13_bal_sub_diaries_bwp;
		}

		public String getR17_other_income() {
			return r17_other_income;
		}

		public void setR17_other_income(String r17_other_income) {
			this.r17_other_income = r17_other_income;
		}

		public BigDecimal getR17_fig_bal_sheet() {
			return r17_fig_bal_sheet;
		}

		public void setR17_fig_bal_sheet(BigDecimal r17_fig_bal_sheet) {
			this.r17_fig_bal_sheet = r17_fig_bal_sheet;
		}

		public BigDecimal getR17_fig_bal_sheet_bwp() {
			return r17_fig_bal_sheet_bwp;
		}

		public void setR17_fig_bal_sheet_bwp(BigDecimal r17_fig_bal_sheet_bwp) {
			this.r17_fig_bal_sheet_bwp = r17_fig_bal_sheet_bwp;
		}

		public BigDecimal getR17_amt_statement_adj() {
			return r17_amt_statement_adj;
		}

		public void setR17_amt_statement_adj(BigDecimal r17_amt_statement_adj) {
			this.r17_amt_statement_adj = r17_amt_statement_adj;
		}

		public BigDecimal getR17_amt_statement_adj_bwp() {
			return r17_amt_statement_adj_bwp;
		}

		public void setR17_amt_statement_adj_bwp(BigDecimal r17_amt_statement_adj_bwp) {
			this.r17_amt_statement_adj_bwp = r17_amt_statement_adj_bwp;
		}

		public BigDecimal getR17_net_amt() {
			return r17_net_amt;
		}

		public void setR17_net_amt(BigDecimal r17_net_amt) {
			this.r17_net_amt = r17_net_amt;
		}

		public BigDecimal getR17_net_amt_bwp() {
			return r17_net_amt_bwp;
		}

		public void setR17_net_amt_bwp(BigDecimal r17_net_amt_bwp) {
			this.r17_net_amt_bwp = r17_net_amt_bwp;
		}

		public BigDecimal getR17_bal_sub() {
			return r17_bal_sub;
		}

		public void setR17_bal_sub(BigDecimal r17_bal_sub) {
			this.r17_bal_sub = r17_bal_sub;
		}

		public BigDecimal getR17_bal_sub_bwp() {
			return r17_bal_sub_bwp;
		}

		public void setR17_bal_sub_bwp(BigDecimal r17_bal_sub_bwp) {
			this.r17_bal_sub_bwp = r17_bal_sub_bwp;
		}

		public BigDecimal getR17_bal_sub_diaries_bwp() {
			return r17_bal_sub_diaries_bwp;
		}

		public void setR17_bal_sub_diaries_bwp(BigDecimal r17_bal_sub_diaries_bwp) {
			this.r17_bal_sub_diaries_bwp = r17_bal_sub_diaries_bwp;
		}

		public String getR18_other_income() {
			return r18_other_income;
		}

		public void setR18_other_income(String r18_other_income) {
			this.r18_other_income = r18_other_income;
		}

		public BigDecimal getR18_fig_bal_sheet() {
			return r18_fig_bal_sheet;
		}

		public void setR18_fig_bal_sheet(BigDecimal r18_fig_bal_sheet) {
			this.r18_fig_bal_sheet = r18_fig_bal_sheet;
		}

		public BigDecimal getR18_fig_bal_sheet_bwp() {
			return r18_fig_bal_sheet_bwp;
		}

		public void setR18_fig_bal_sheet_bwp(BigDecimal r18_fig_bal_sheet_bwp) {
			this.r18_fig_bal_sheet_bwp = r18_fig_bal_sheet_bwp;
		}

		public BigDecimal getR18_amt_statement_adj() {
			return r18_amt_statement_adj;
		}

		public void setR18_amt_statement_adj(BigDecimal r18_amt_statement_adj) {
			this.r18_amt_statement_adj = r18_amt_statement_adj;
		}

		public BigDecimal getR18_amt_statement_adj_bwp() {
			return r18_amt_statement_adj_bwp;
		}

		public void setR18_amt_statement_adj_bwp(BigDecimal r18_amt_statement_adj_bwp) {
			this.r18_amt_statement_adj_bwp = r18_amt_statement_adj_bwp;
		}

		public BigDecimal getR18_net_amt() {
			return r18_net_amt;
		}

		public void setR18_net_amt(BigDecimal r18_net_amt) {
			this.r18_net_amt = r18_net_amt;
		}

		public BigDecimal getR18_net_amt_bwp() {
			return r18_net_amt_bwp;
		}

		public void setR18_net_amt_bwp(BigDecimal r18_net_amt_bwp) {
			this.r18_net_amt_bwp = r18_net_amt_bwp;
		}

		public BigDecimal getR18_bal_sub() {
			return r18_bal_sub;
		}

		public void setR18_bal_sub(BigDecimal r18_bal_sub) {
			this.r18_bal_sub = r18_bal_sub;
		}

		public BigDecimal getR18_bal_sub_bwp() {
			return r18_bal_sub_bwp;
		}

		public void setR18_bal_sub_bwp(BigDecimal r18_bal_sub_bwp) {
			this.r18_bal_sub_bwp = r18_bal_sub_bwp;
		}

		public BigDecimal getR18_bal_sub_diaries_bwp() {
			return r18_bal_sub_diaries_bwp;
		}

		public void setR18_bal_sub_diaries_bwp(BigDecimal r18_bal_sub_diaries_bwp) {
			this.r18_bal_sub_diaries_bwp = r18_bal_sub_diaries_bwp;
		}

		public String getR19_other_income() {
			return r19_other_income;
		}

		public void setR19_other_income(String r19_other_income) {
			this.r19_other_income = r19_other_income;
		}

		public BigDecimal getR19_fig_bal_sheet() {
			return r19_fig_bal_sheet;
		}

		public void setR19_fig_bal_sheet(BigDecimal r19_fig_bal_sheet) {
			this.r19_fig_bal_sheet = r19_fig_bal_sheet;
		}

		public BigDecimal getR19_fig_bal_sheet_bwp() {
			return r19_fig_bal_sheet_bwp;
		}

		public void setR19_fig_bal_sheet_bwp(BigDecimal r19_fig_bal_sheet_bwp) {
			this.r19_fig_bal_sheet_bwp = r19_fig_bal_sheet_bwp;
		}

		public BigDecimal getR19_amt_statement_adj() {
			return r19_amt_statement_adj;
		}

		public void setR19_amt_statement_adj(BigDecimal r19_amt_statement_adj) {
			this.r19_amt_statement_adj = r19_amt_statement_adj;
		}

		public BigDecimal getR19_amt_statement_adj_bwp() {
			return r19_amt_statement_adj_bwp;
		}

		public void setR19_amt_statement_adj_bwp(BigDecimal r19_amt_statement_adj_bwp) {
			this.r19_amt_statement_adj_bwp = r19_amt_statement_adj_bwp;
		}

		public BigDecimal getR19_net_amt() {
			return r19_net_amt;
		}

		public void setR19_net_amt(BigDecimal r19_net_amt) {
			this.r19_net_amt = r19_net_amt;
		}

		public BigDecimal getR19_net_amt_bwp() {
			return r19_net_amt_bwp;
		}

		public void setR19_net_amt_bwp(BigDecimal r19_net_amt_bwp) {
			this.r19_net_amt_bwp = r19_net_amt_bwp;
		}

		public BigDecimal getR19_bal_sub() {
			return r19_bal_sub;
		}

		public void setR19_bal_sub(BigDecimal r19_bal_sub) {
			this.r19_bal_sub = r19_bal_sub;
		}

		public BigDecimal getR19_bal_sub_bwp() {
			return r19_bal_sub_bwp;
		}

		public void setR19_bal_sub_bwp(BigDecimal r19_bal_sub_bwp) {
			this.r19_bal_sub_bwp = r19_bal_sub_bwp;
		}

		public BigDecimal getR19_bal_sub_diaries_bwp() {
			return r19_bal_sub_diaries_bwp;
		}

		public void setR19_bal_sub_diaries_bwp(BigDecimal r19_bal_sub_diaries_bwp) {
			this.r19_bal_sub_diaries_bwp = r19_bal_sub_diaries_bwp;
		}

		public String getR20_other_income() {
			return r20_other_income;
		}

		public void setR20_other_income(String r20_other_income) {
			this.r20_other_income = r20_other_income;
		}

		public BigDecimal getR20_fig_bal_sheet() {
			return r20_fig_bal_sheet;
		}

		public void setR20_fig_bal_sheet(BigDecimal r20_fig_bal_sheet) {
			this.r20_fig_bal_sheet = r20_fig_bal_sheet;
		}

		public BigDecimal getR20_fig_bal_sheet_bwp() {
			return r20_fig_bal_sheet_bwp;
		}

		public void setR20_fig_bal_sheet_bwp(BigDecimal r20_fig_bal_sheet_bwp) {
			this.r20_fig_bal_sheet_bwp = r20_fig_bal_sheet_bwp;
		}

		public BigDecimal getR20_amt_statement_adj() {
			return r20_amt_statement_adj;
		}

		public void setR20_amt_statement_adj(BigDecimal r20_amt_statement_adj) {
			this.r20_amt_statement_adj = r20_amt_statement_adj;
		}

		public BigDecimal getR20_amt_statement_adj_bwp() {
			return r20_amt_statement_adj_bwp;
		}

		public void setR20_amt_statement_adj_bwp(BigDecimal r20_amt_statement_adj_bwp) {
			this.r20_amt_statement_adj_bwp = r20_amt_statement_adj_bwp;
		}

		public BigDecimal getR20_net_amt() {
			return r20_net_amt;
		}

		public void setR20_net_amt(BigDecimal r20_net_amt) {
			this.r20_net_amt = r20_net_amt;
		}

		public BigDecimal getR20_net_amt_bwp() {
			return r20_net_amt_bwp;
		}

		public void setR20_net_amt_bwp(BigDecimal r20_net_amt_bwp) {
			this.r20_net_amt_bwp = r20_net_amt_bwp;
		}

		public BigDecimal getR20_bal_sub() {
			return r20_bal_sub;
		}

		public void setR20_bal_sub(BigDecimal r20_bal_sub) {
			this.r20_bal_sub = r20_bal_sub;
		}

		public BigDecimal getR20_bal_sub_bwp() {
			return r20_bal_sub_bwp;
		}

		public void setR20_bal_sub_bwp(BigDecimal r20_bal_sub_bwp) {
			this.r20_bal_sub_bwp = r20_bal_sub_bwp;
		}

		public BigDecimal getR20_bal_sub_diaries_bwp() {
			return r20_bal_sub_diaries_bwp;
		}

		public void setR20_bal_sub_diaries_bwp(BigDecimal r20_bal_sub_diaries_bwp) {
			this.r20_bal_sub_diaries_bwp = r20_bal_sub_diaries_bwp;
		}

		public String getR21_other_income() {
			return r21_other_income;
		}

		public void setR21_other_income(String r21_other_income) {
			this.r21_other_income = r21_other_income;
		}

		public BigDecimal getR21_fig_bal_sheet() {
			return r21_fig_bal_sheet;
		}

		public void setR21_fig_bal_sheet(BigDecimal r21_fig_bal_sheet) {
			this.r21_fig_bal_sheet = r21_fig_bal_sheet;
		}

		public BigDecimal getR21_fig_bal_sheet_bwp() {
			return r21_fig_bal_sheet_bwp;
		}

		public void setR21_fig_bal_sheet_bwp(BigDecimal r21_fig_bal_sheet_bwp) {
			this.r21_fig_bal_sheet_bwp = r21_fig_bal_sheet_bwp;
		}

		public BigDecimal getR21_amt_statement_adj() {
			return r21_amt_statement_adj;
		}

		public void setR21_amt_statement_adj(BigDecimal r21_amt_statement_adj) {
			this.r21_amt_statement_adj = r21_amt_statement_adj;
		}

		public BigDecimal getR21_amt_statement_adj_bwp() {
			return r21_amt_statement_adj_bwp;
		}

		public void setR21_amt_statement_adj_bwp(BigDecimal r21_amt_statement_adj_bwp) {
			this.r21_amt_statement_adj_bwp = r21_amt_statement_adj_bwp;
		}

		public BigDecimal getR21_net_amt() {
			return r21_net_amt;
		}

		public void setR21_net_amt(BigDecimal r21_net_amt) {
			this.r21_net_amt = r21_net_amt;
		}

		public BigDecimal getR21_net_amt_bwp() {
			return r21_net_amt_bwp;
		}

		public void setR21_net_amt_bwp(BigDecimal r21_net_amt_bwp) {
			this.r21_net_amt_bwp = r21_net_amt_bwp;
		}

		public BigDecimal getR21_bal_sub() {
			return r21_bal_sub;
		}

		public void setR21_bal_sub(BigDecimal r21_bal_sub) {
			this.r21_bal_sub = r21_bal_sub;
		}

		public BigDecimal getR21_bal_sub_bwp() {
			return r21_bal_sub_bwp;
		}

		public void setR21_bal_sub_bwp(BigDecimal r21_bal_sub_bwp) {
			this.r21_bal_sub_bwp = r21_bal_sub_bwp;
		}

		public BigDecimal getR21_bal_sub_diaries_bwp() {
			return r21_bal_sub_diaries_bwp;
		}

		public void setR21_bal_sub_diaries_bwp(BigDecimal r21_bal_sub_diaries_bwp) {
			this.r21_bal_sub_diaries_bwp = r21_bal_sub_diaries_bwp;
		}

		public String getR22_other_income() {
			return r22_other_income;
		}

		public void setR22_other_income(String r22_other_income) {
			this.r22_other_income = r22_other_income;
		}

		public BigDecimal getR22_fig_bal_sheet() {
			return r22_fig_bal_sheet;
		}

		public void setR22_fig_bal_sheet(BigDecimal r22_fig_bal_sheet) {
			this.r22_fig_bal_sheet = r22_fig_bal_sheet;
		}

		public BigDecimal getR22_fig_bal_sheet_bwp() {
			return r22_fig_bal_sheet_bwp;
		}

		public void setR22_fig_bal_sheet_bwp(BigDecimal r22_fig_bal_sheet_bwp) {
			this.r22_fig_bal_sheet_bwp = r22_fig_bal_sheet_bwp;
		}

		public BigDecimal getR22_amt_statement_adj() {
			return r22_amt_statement_adj;
		}

		public void setR22_amt_statement_adj(BigDecimal r22_amt_statement_adj) {
			this.r22_amt_statement_adj = r22_amt_statement_adj;
		}

		public BigDecimal getR22_amt_statement_adj_bwp() {
			return r22_amt_statement_adj_bwp;
		}

		public void setR22_amt_statement_adj_bwp(BigDecimal r22_amt_statement_adj_bwp) {
			this.r22_amt_statement_adj_bwp = r22_amt_statement_adj_bwp;
		}

		public BigDecimal getR22_net_amt() {
			return r22_net_amt;
		}

		public void setR22_net_amt(BigDecimal r22_net_amt) {
			this.r22_net_amt = r22_net_amt;
		}

		public BigDecimal getR22_net_amt_bwp() {
			return r22_net_amt_bwp;
		}

		public void setR22_net_amt_bwp(BigDecimal r22_net_amt_bwp) {
			this.r22_net_amt_bwp = r22_net_amt_bwp;
		}

		public BigDecimal getR22_bal_sub() {
			return r22_bal_sub;
		}

		public void setR22_bal_sub(BigDecimal r22_bal_sub) {
			this.r22_bal_sub = r22_bal_sub;
		}

		public BigDecimal getR22_bal_sub_bwp() {
			return r22_bal_sub_bwp;
		}

		public void setR22_bal_sub_bwp(BigDecimal r22_bal_sub_bwp) {
			this.r22_bal_sub_bwp = r22_bal_sub_bwp;
		}

		public BigDecimal getR22_bal_sub_diaries_bwp() {
			return r22_bal_sub_diaries_bwp;
		}

		public void setR22_bal_sub_diaries_bwp(BigDecimal r22_bal_sub_diaries_bwp) {
			this.r22_bal_sub_diaries_bwp = r22_bal_sub_diaries_bwp;
		}

		public String getR23_other_income() {
			return r23_other_income;
		}

		public void setR23_other_income(String r23_other_income) {
			this.r23_other_income = r23_other_income;
		}

		public BigDecimal getR23_fig_bal_sheet() {
			return r23_fig_bal_sheet;
		}

		public void setR23_fig_bal_sheet(BigDecimal r23_fig_bal_sheet) {
			this.r23_fig_bal_sheet = r23_fig_bal_sheet;
		}

		public BigDecimal getR23_fig_bal_sheet_bwp() {
			return r23_fig_bal_sheet_bwp;
		}

		public void setR23_fig_bal_sheet_bwp(BigDecimal r23_fig_bal_sheet_bwp) {
			this.r23_fig_bal_sheet_bwp = r23_fig_bal_sheet_bwp;
		}

		public BigDecimal getR23_amt_statement_adj() {
			return r23_amt_statement_adj;
		}

		public void setR23_amt_statement_adj(BigDecimal r23_amt_statement_adj) {
			this.r23_amt_statement_adj = r23_amt_statement_adj;
		}

		public BigDecimal getR23_amt_statement_adj_bwp() {
			return r23_amt_statement_adj_bwp;
		}

		public void setR23_amt_statement_adj_bwp(BigDecimal r23_amt_statement_adj_bwp) {
			this.r23_amt_statement_adj_bwp = r23_amt_statement_adj_bwp;
		}

		public BigDecimal getR23_net_amt() {
			return r23_net_amt;
		}

		public void setR23_net_amt(BigDecimal r23_net_amt) {
			this.r23_net_amt = r23_net_amt;
		}

		public BigDecimal getR23_net_amt_bwp() {
			return r23_net_amt_bwp;
		}

		public void setR23_net_amt_bwp(BigDecimal r23_net_amt_bwp) {
			this.r23_net_amt_bwp = r23_net_amt_bwp;
		}

		public BigDecimal getR23_bal_sub() {
			return r23_bal_sub;
		}

		public void setR23_bal_sub(BigDecimal r23_bal_sub) {
			this.r23_bal_sub = r23_bal_sub;
		}

		public BigDecimal getR23_bal_sub_bwp() {
			return r23_bal_sub_bwp;
		}

		public void setR23_bal_sub_bwp(BigDecimal r23_bal_sub_bwp) {
			this.r23_bal_sub_bwp = r23_bal_sub_bwp;
		}

		public BigDecimal getR23_bal_sub_diaries_bwp() {
			return r23_bal_sub_diaries_bwp;
		}

		public void setR23_bal_sub_diaries_bwp(BigDecimal r23_bal_sub_diaries_bwp) {
			this.r23_bal_sub_diaries_bwp = r23_bal_sub_diaries_bwp;
		}

		public String getR24_other_income() {
			return r24_other_income;
		}

		public void setR24_other_income(String r24_other_income) {
			this.r24_other_income = r24_other_income;
		}

		public BigDecimal getR24_fig_bal_sheet() {
			return r24_fig_bal_sheet;
		}

		public void setR24_fig_bal_sheet(BigDecimal r24_fig_bal_sheet) {
			this.r24_fig_bal_sheet = r24_fig_bal_sheet;
		}

		public BigDecimal getR24_fig_bal_sheet_bwp() {
			return r24_fig_bal_sheet_bwp;
		}

		public void setR24_fig_bal_sheet_bwp(BigDecimal r24_fig_bal_sheet_bwp) {
			this.r24_fig_bal_sheet_bwp = r24_fig_bal_sheet_bwp;
		}

		public BigDecimal getR24_amt_statement_adj() {
			return r24_amt_statement_adj;
		}

		public void setR24_amt_statement_adj(BigDecimal r24_amt_statement_adj) {
			this.r24_amt_statement_adj = r24_amt_statement_adj;
		}

		public BigDecimal getR24_amt_statement_adj_bwp() {
			return r24_amt_statement_adj_bwp;
		}

		public void setR24_amt_statement_adj_bwp(BigDecimal r24_amt_statement_adj_bwp) {
			this.r24_amt_statement_adj_bwp = r24_amt_statement_adj_bwp;
		}

		public BigDecimal getR24_net_amt() {
			return r24_net_amt;
		}

		public void setR24_net_amt(BigDecimal r24_net_amt) {
			this.r24_net_amt = r24_net_amt;
		}

		public BigDecimal getR24_net_amt_bwp() {
			return r24_net_amt_bwp;
		}

		public void setR24_net_amt_bwp(BigDecimal r24_net_amt_bwp) {
			this.r24_net_amt_bwp = r24_net_amt_bwp;
		}

		public BigDecimal getR24_bal_sub() {
			return r24_bal_sub;
		}

		public void setR24_bal_sub(BigDecimal r24_bal_sub) {
			this.r24_bal_sub = r24_bal_sub;
		}

		public BigDecimal getR24_bal_sub_bwp() {
			return r24_bal_sub_bwp;
		}

		public void setR24_bal_sub_bwp(BigDecimal r24_bal_sub_bwp) {
			this.r24_bal_sub_bwp = r24_bal_sub_bwp;
		}

		public BigDecimal getR24_bal_sub_diaries_bwp() {
			return r24_bal_sub_diaries_bwp;
		}

		public void setR24_bal_sub_diaries_bwp(BigDecimal r24_bal_sub_diaries_bwp) {
			this.r24_bal_sub_diaries_bwp = r24_bal_sub_diaries_bwp;
		}

		public String getR25_other_income() {
			return r25_other_income;
		}

		public void setR25_other_income(String r25_other_income) {
			this.r25_other_income = r25_other_income;
		}

		public BigDecimal getR25_fig_bal_sheet() {
			return r25_fig_bal_sheet;
		}

		public void setR25_fig_bal_sheet(BigDecimal r25_fig_bal_sheet) {
			this.r25_fig_bal_sheet = r25_fig_bal_sheet;
		}

		public BigDecimal getR25_fig_bal_sheet_bwp() {
			return r25_fig_bal_sheet_bwp;
		}

		public void setR25_fig_bal_sheet_bwp(BigDecimal r25_fig_bal_sheet_bwp) {
			this.r25_fig_bal_sheet_bwp = r25_fig_bal_sheet_bwp;
		}

		public BigDecimal getR25_amt_statement_adj() {
			return r25_amt_statement_adj;
		}

		public void setR25_amt_statement_adj(BigDecimal r25_amt_statement_adj) {
			this.r25_amt_statement_adj = r25_amt_statement_adj;
		}

		public BigDecimal getR25_amt_statement_adj_bwp() {
			return r25_amt_statement_adj_bwp;
		}

		public void setR25_amt_statement_adj_bwp(BigDecimal r25_amt_statement_adj_bwp) {
			this.r25_amt_statement_adj_bwp = r25_amt_statement_adj_bwp;
		}

		public BigDecimal getR25_net_amt() {
			return r25_net_amt;
		}

		public void setR25_net_amt(BigDecimal r25_net_amt) {
			this.r25_net_amt = r25_net_amt;
		}

		public BigDecimal getR25_net_amt_bwp() {
			return r25_net_amt_bwp;
		}

		public void setR25_net_amt_bwp(BigDecimal r25_net_amt_bwp) {
			this.r25_net_amt_bwp = r25_net_amt_bwp;
		}

		public BigDecimal getR25_bal_sub() {
			return r25_bal_sub;
		}

		public void setR25_bal_sub(BigDecimal r25_bal_sub) {
			this.r25_bal_sub = r25_bal_sub;
		}

		public BigDecimal getR25_bal_sub_bwp() {
			return r25_bal_sub_bwp;
		}

		public void setR25_bal_sub_bwp(BigDecimal r25_bal_sub_bwp) {
			this.r25_bal_sub_bwp = r25_bal_sub_bwp;
		}

		public BigDecimal getR25_bal_sub_diaries_bwp() {
			return r25_bal_sub_diaries_bwp;
		}

		public void setR25_bal_sub_diaries_bwp(BigDecimal r25_bal_sub_diaries_bwp) {
			this.r25_bal_sub_diaries_bwp = r25_bal_sub_diaries_bwp;
		}

		public String getR26_other_income() {
			return r26_other_income;
		}

		public void setR26_other_income(String r26_other_income) {
			this.r26_other_income = r26_other_income;
		}

		public BigDecimal getR26_fig_bal_sheet() {
			return r26_fig_bal_sheet;
		}

		public void setR26_fig_bal_sheet(BigDecimal r26_fig_bal_sheet) {
			this.r26_fig_bal_sheet = r26_fig_bal_sheet;
		}

		public BigDecimal getR26_fig_bal_sheet_bwp() {
			return r26_fig_bal_sheet_bwp;
		}

		public void setR26_fig_bal_sheet_bwp(BigDecimal r26_fig_bal_sheet_bwp) {
			this.r26_fig_bal_sheet_bwp = r26_fig_bal_sheet_bwp;
		}

		public BigDecimal getR26_amt_statement_adj() {
			return r26_amt_statement_adj;
		}

		public void setR26_amt_statement_adj(BigDecimal r26_amt_statement_adj) {
			this.r26_amt_statement_adj = r26_amt_statement_adj;
		}

		public BigDecimal getR26_amt_statement_adj_bwp() {
			return r26_amt_statement_adj_bwp;
		}

		public void setR26_amt_statement_adj_bwp(BigDecimal r26_amt_statement_adj_bwp) {
			this.r26_amt_statement_adj_bwp = r26_amt_statement_adj_bwp;
		}

		public BigDecimal getR26_net_amt() {
			return r26_net_amt;
		}

		public void setR26_net_amt(BigDecimal r26_net_amt) {
			this.r26_net_amt = r26_net_amt;
		}

		public BigDecimal getR26_net_amt_bwp() {
			return r26_net_amt_bwp;
		}

		public void setR26_net_amt_bwp(BigDecimal r26_net_amt_bwp) {
			this.r26_net_amt_bwp = r26_net_amt_bwp;
		}

		public BigDecimal getR26_bal_sub() {
			return r26_bal_sub;
		}

		public void setR26_bal_sub(BigDecimal r26_bal_sub) {
			this.r26_bal_sub = r26_bal_sub;
		}

		public BigDecimal getR26_bal_sub_bwp() {
			return r26_bal_sub_bwp;
		}

		public void setR26_bal_sub_bwp(BigDecimal r26_bal_sub_bwp) {
			this.r26_bal_sub_bwp = r26_bal_sub_bwp;
		}

		public BigDecimal getR26_bal_sub_diaries_bwp() {
			return r26_bal_sub_diaries_bwp;
		}

		public void setR26_bal_sub_diaries_bwp(BigDecimal r26_bal_sub_diaries_bwp) {
			this.r26_bal_sub_diaries_bwp = r26_bal_sub_diaries_bwp;
		}

		public String getR27_other_income() {
			return r27_other_income;
		}

		public void setR27_other_income(String r27_other_income) {
			this.r27_other_income = r27_other_income;
		}

		public BigDecimal getR27_fig_bal_sheet() {
			return r27_fig_bal_sheet;
		}

		public void setR27_fig_bal_sheet(BigDecimal r27_fig_bal_sheet) {
			this.r27_fig_bal_sheet = r27_fig_bal_sheet;
		}

		public BigDecimal getR27_fig_bal_sheet_bwp() {
			return r27_fig_bal_sheet_bwp;
		}

		public void setR27_fig_bal_sheet_bwp(BigDecimal r27_fig_bal_sheet_bwp) {
			this.r27_fig_bal_sheet_bwp = r27_fig_bal_sheet_bwp;
		}

		public BigDecimal getR27_amt_statement_adj() {
			return r27_amt_statement_adj;
		}

		public void setR27_amt_statement_adj(BigDecimal r27_amt_statement_adj) {
			this.r27_amt_statement_adj = r27_amt_statement_adj;
		}

		public BigDecimal getR27_amt_statement_adj_bwp() {
			return r27_amt_statement_adj_bwp;
		}

		public void setR27_amt_statement_adj_bwp(BigDecimal r27_amt_statement_adj_bwp) {
			this.r27_amt_statement_adj_bwp = r27_amt_statement_adj_bwp;
		}

		public BigDecimal getR27_net_amt() {
			return r27_net_amt;
		}

		public void setR27_net_amt(BigDecimal r27_net_amt) {
			this.r27_net_amt = r27_net_amt;
		}

		public BigDecimal getR27_net_amt_bwp() {
			return r27_net_amt_bwp;
		}

		public void setR27_net_amt_bwp(BigDecimal r27_net_amt_bwp) {
			this.r27_net_amt_bwp = r27_net_amt_bwp;
		}

		public BigDecimal getR27_bal_sub() {
			return r27_bal_sub;
		}

		public void setR27_bal_sub(BigDecimal r27_bal_sub) {
			this.r27_bal_sub = r27_bal_sub;
		}

		public BigDecimal getR27_bal_sub_bwp() {
			return r27_bal_sub_bwp;
		}

		public void setR27_bal_sub_bwp(BigDecimal r27_bal_sub_bwp) {
			this.r27_bal_sub_bwp = r27_bal_sub_bwp;
		}

		public BigDecimal getR27_bal_sub_diaries_bwp() {
			return r27_bal_sub_diaries_bwp;
		}

		public void setR27_bal_sub_diaries_bwp(BigDecimal r27_bal_sub_diaries_bwp) {
			this.r27_bal_sub_diaries_bwp = r27_bal_sub_diaries_bwp;
		}

		public String getR28_other_income() {
			return r28_other_income;
		}

		public void setR28_other_income(String r28_other_income) {
			this.r28_other_income = r28_other_income;
		}

		public BigDecimal getR28_fig_bal_sheet() {
			return r28_fig_bal_sheet;
		}

		public void setR28_fig_bal_sheet(BigDecimal r28_fig_bal_sheet) {
			this.r28_fig_bal_sheet = r28_fig_bal_sheet;
		}

		public BigDecimal getR28_fig_bal_sheet_bwp() {
			return r28_fig_bal_sheet_bwp;
		}

		public void setR28_fig_bal_sheet_bwp(BigDecimal r28_fig_bal_sheet_bwp) {
			this.r28_fig_bal_sheet_bwp = r28_fig_bal_sheet_bwp;
		}

		public BigDecimal getR28_amt_statement_adj() {
			return r28_amt_statement_adj;
		}

		public void setR28_amt_statement_adj(BigDecimal r28_amt_statement_adj) {
			this.r28_amt_statement_adj = r28_amt_statement_adj;
		}

		public BigDecimal getR28_amt_statement_adj_bwp() {
			return r28_amt_statement_adj_bwp;
		}

		public void setR28_amt_statement_adj_bwp(BigDecimal r28_amt_statement_adj_bwp) {
			this.r28_amt_statement_adj_bwp = r28_amt_statement_adj_bwp;
		}

		public BigDecimal getR28_net_amt() {
			return r28_net_amt;
		}

		public void setR28_net_amt(BigDecimal r28_net_amt) {
			this.r28_net_amt = r28_net_amt;
		}

		public BigDecimal getR28_net_amt_bwp() {
			return r28_net_amt_bwp;
		}

		public void setR28_net_amt_bwp(BigDecimal r28_net_amt_bwp) {
			this.r28_net_amt_bwp = r28_net_amt_bwp;
		}

		public BigDecimal getR28_bal_sub() {
			return r28_bal_sub;
		}

		public void setR28_bal_sub(BigDecimal r28_bal_sub) {
			this.r28_bal_sub = r28_bal_sub;
		}

		public BigDecimal getR28_bal_sub_bwp() {
			return r28_bal_sub_bwp;
		}

		public void setR28_bal_sub_bwp(BigDecimal r28_bal_sub_bwp) {
			this.r28_bal_sub_bwp = r28_bal_sub_bwp;
		}

		public BigDecimal getR28_bal_sub_diaries_bwp() {
			return r28_bal_sub_diaries_bwp;
		}

		public void setR28_bal_sub_diaries_bwp(BigDecimal r28_bal_sub_diaries_bwp) {
			this.r28_bal_sub_diaries_bwp = r28_bal_sub_diaries_bwp;
		}

		public String getR29_other_income() {
			return r29_other_income;
		}

		public void setR29_other_income(String r29_other_income) {
			this.r29_other_income = r29_other_income;
		}

		public BigDecimal getR29_fig_bal_sheet() {
			return r29_fig_bal_sheet;
		}

		public void setR29_fig_bal_sheet(BigDecimal r29_fig_bal_sheet) {
			this.r29_fig_bal_sheet = r29_fig_bal_sheet;
		}

		public BigDecimal getR29_fig_bal_sheet_bwp() {
			return r29_fig_bal_sheet_bwp;
		}

		public void setR29_fig_bal_sheet_bwp(BigDecimal r29_fig_bal_sheet_bwp) {
			this.r29_fig_bal_sheet_bwp = r29_fig_bal_sheet_bwp;
		}

		public BigDecimal getR29_amt_statement_adj() {
			return r29_amt_statement_adj;
		}

		public void setR29_amt_statement_adj(BigDecimal r29_amt_statement_adj) {
			this.r29_amt_statement_adj = r29_amt_statement_adj;
		}

		public BigDecimal getR29_amt_statement_adj_bwp() {
			return r29_amt_statement_adj_bwp;
		}

		public void setR29_amt_statement_adj_bwp(BigDecimal r29_amt_statement_adj_bwp) {
			this.r29_amt_statement_adj_bwp = r29_amt_statement_adj_bwp;
		}

		public BigDecimal getR29_net_amt() {
			return r29_net_amt;
		}

		public void setR29_net_amt(BigDecimal r29_net_amt) {
			this.r29_net_amt = r29_net_amt;
		}

		public BigDecimal getR29_net_amt_bwp() {
			return r29_net_amt_bwp;
		}

		public void setR29_net_amt_bwp(BigDecimal r29_net_amt_bwp) {
			this.r29_net_amt_bwp = r29_net_amt_bwp;
		}

		public BigDecimal getR29_bal_sub() {
			return r29_bal_sub;
		}

		public void setR29_bal_sub(BigDecimal r29_bal_sub) {
			this.r29_bal_sub = r29_bal_sub;
		}

		public BigDecimal getR29_bal_sub_bwp() {
			return r29_bal_sub_bwp;
		}

		public void setR29_bal_sub_bwp(BigDecimal r29_bal_sub_bwp) {
			this.r29_bal_sub_bwp = r29_bal_sub_bwp;
		}

		public BigDecimal getR29_bal_sub_diaries_bwp() {
			return r29_bal_sub_diaries_bwp;
		}

		public void setR29_bal_sub_diaries_bwp(BigDecimal r29_bal_sub_diaries_bwp) {
			this.r29_bal_sub_diaries_bwp = r29_bal_sub_diaries_bwp;
		}

		public String getR30_other_income() {
			return r30_other_income;
		}

		public void setR30_other_income(String r30_other_income) {
			this.r30_other_income = r30_other_income;
		}

		public BigDecimal getR30_fig_bal_sheet() {
			return r30_fig_bal_sheet;
		}

		public void setR30_fig_bal_sheet(BigDecimal r30_fig_bal_sheet) {
			this.r30_fig_bal_sheet = r30_fig_bal_sheet;
		}

		public BigDecimal getR30_fig_bal_sheet_bwp() {
			return r30_fig_bal_sheet_bwp;
		}

		public void setR30_fig_bal_sheet_bwp(BigDecimal r30_fig_bal_sheet_bwp) {
			this.r30_fig_bal_sheet_bwp = r30_fig_bal_sheet_bwp;
		}

		public BigDecimal getR30_amt_statement_adj() {
			return r30_amt_statement_adj;
		}

		public void setR30_amt_statement_adj(BigDecimal r30_amt_statement_adj) {
			this.r30_amt_statement_adj = r30_amt_statement_adj;
		}

		public BigDecimal getR30_amt_statement_adj_bwp() {
			return r30_amt_statement_adj_bwp;
		}

		public void setR30_amt_statement_adj_bwp(BigDecimal r30_amt_statement_adj_bwp) {
			this.r30_amt_statement_adj_bwp = r30_amt_statement_adj_bwp;
		}

		public BigDecimal getR30_net_amt() {
			return r30_net_amt;
		}

		public void setR30_net_amt(BigDecimal r30_net_amt) {
			this.r30_net_amt = r30_net_amt;
		}

		public BigDecimal getR30_net_amt_bwp() {
			return r30_net_amt_bwp;
		}

		public void setR30_net_amt_bwp(BigDecimal r30_net_amt_bwp) {
			this.r30_net_amt_bwp = r30_net_amt_bwp;
		}

		public BigDecimal getR30_bal_sub() {
			return r30_bal_sub;
		}

		public void setR30_bal_sub(BigDecimal r30_bal_sub) {
			this.r30_bal_sub = r30_bal_sub;
		}

		public BigDecimal getR30_bal_sub_bwp() {
			return r30_bal_sub_bwp;
		}

		public void setR30_bal_sub_bwp(BigDecimal r30_bal_sub_bwp) {
			this.r30_bal_sub_bwp = r30_bal_sub_bwp;
		}

		public BigDecimal getR30_bal_sub_diaries_bwp() {
			return r30_bal_sub_diaries_bwp;
		}

		public void setR30_bal_sub_diaries_bwp(BigDecimal r30_bal_sub_diaries_bwp) {
			this.r30_bal_sub_diaries_bwp = r30_bal_sub_diaries_bwp;
		}

		public String getR31_other_income() {
			return r31_other_income;
		}

		public void setR31_other_income(String r31_other_income) {
			this.r31_other_income = r31_other_income;
		}

		public BigDecimal getR31_fig_bal_sheet() {
			return r31_fig_bal_sheet;
		}

		public void setR31_fig_bal_sheet(BigDecimal r31_fig_bal_sheet) {
			this.r31_fig_bal_sheet = r31_fig_bal_sheet;
		}

		public BigDecimal getR31_fig_bal_sheet_bwp() {
			return r31_fig_bal_sheet_bwp;
		}

		public void setR31_fig_bal_sheet_bwp(BigDecimal r31_fig_bal_sheet_bwp) {
			this.r31_fig_bal_sheet_bwp = r31_fig_bal_sheet_bwp;
		}

		public BigDecimal getR31_amt_statement_adj() {
			return r31_amt_statement_adj;
		}

		public void setR31_amt_statement_adj(BigDecimal r31_amt_statement_adj) {
			this.r31_amt_statement_adj = r31_amt_statement_adj;
		}

		public BigDecimal getR31_amt_statement_adj_bwp() {
			return r31_amt_statement_adj_bwp;
		}

		public void setR31_amt_statement_adj_bwp(BigDecimal r31_amt_statement_adj_bwp) {
			this.r31_amt_statement_adj_bwp = r31_amt_statement_adj_bwp;
		}

		public BigDecimal getR31_net_amt() {
			return r31_net_amt;
		}

		public void setR31_net_amt(BigDecimal r31_net_amt) {
			this.r31_net_amt = r31_net_amt;
		}

		public BigDecimal getR31_net_amt_bwp() {
			return r31_net_amt_bwp;
		}

		public void setR31_net_amt_bwp(BigDecimal r31_net_amt_bwp) {
			this.r31_net_amt_bwp = r31_net_amt_bwp;
		}

		public BigDecimal getR31_bal_sub() {
			return r31_bal_sub;
		}

		public void setR31_bal_sub(BigDecimal r31_bal_sub) {
			this.r31_bal_sub = r31_bal_sub;
		}

		public BigDecimal getR31_bal_sub_bwp() {
			return r31_bal_sub_bwp;
		}

		public void setR31_bal_sub_bwp(BigDecimal r31_bal_sub_bwp) {
			this.r31_bal_sub_bwp = r31_bal_sub_bwp;
		}

		public BigDecimal getR31_bal_sub_diaries_bwp() {
			return r31_bal_sub_diaries_bwp;
		}

		public void setR31_bal_sub_diaries_bwp(BigDecimal r31_bal_sub_diaries_bwp) {
			this.r31_bal_sub_diaries_bwp = r31_bal_sub_diaries_bwp;
		}

		public String getR40_intrest_expended() {
			return r40_intrest_expended;
		}

		public void setR40_intrest_expended(String r40_intrest_expended) {
			this.r40_intrest_expended = r40_intrest_expended;
		}

		public BigDecimal getR40_fig_bal_sheet() {
			return r40_fig_bal_sheet;
		}

		public void setR40_fig_bal_sheet(BigDecimal r40_fig_bal_sheet) {
			this.r40_fig_bal_sheet = r40_fig_bal_sheet;
		}

		public BigDecimal getR40_fig_bal_sheet_bwp() {
			return r40_fig_bal_sheet_bwp;
		}

		public void setR40_fig_bal_sheet_bwp(BigDecimal r40_fig_bal_sheet_bwp) {
			this.r40_fig_bal_sheet_bwp = r40_fig_bal_sheet_bwp;
		}

		public BigDecimal getR40_amt_statement_adj() {
			return r40_amt_statement_adj;
		}

		public void setR40_amt_statement_adj(BigDecimal r40_amt_statement_adj) {
			this.r40_amt_statement_adj = r40_amt_statement_adj;
		}

		public BigDecimal getR40_amt_statement_adj_bwp() {
			return r40_amt_statement_adj_bwp;
		}

		public void setR40_amt_statement_adj_bwp(BigDecimal r40_amt_statement_adj_bwp) {
			this.r40_amt_statement_adj_bwp = r40_amt_statement_adj_bwp;
		}

		public BigDecimal getR40_net_amt() {
			return r40_net_amt;
		}

		public void setR40_net_amt(BigDecimal r40_net_amt) {
			this.r40_net_amt = r40_net_amt;
		}

		public BigDecimal getR40_net_amt_bwp() {
			return r40_net_amt_bwp;
		}

		public void setR40_net_amt_bwp(BigDecimal r40_net_amt_bwp) {
			this.r40_net_amt_bwp = r40_net_amt_bwp;
		}

		public BigDecimal getR40_bal_sub() {
			return r40_bal_sub;
		}

		public void setR40_bal_sub(BigDecimal r40_bal_sub) {
			this.r40_bal_sub = r40_bal_sub;
		}

		public BigDecimal getR40_bal_sub_bwp() {
			return r40_bal_sub_bwp;
		}

		public void setR40_bal_sub_bwp(BigDecimal r40_bal_sub_bwp) {
			this.r40_bal_sub_bwp = r40_bal_sub_bwp;
		}

		public BigDecimal getR40_bal_sub_diaries_bwp() {
			return r40_bal_sub_diaries_bwp;
		}

		public void setR40_bal_sub_diaries_bwp(BigDecimal r40_bal_sub_diaries_bwp) {
			this.r40_bal_sub_diaries_bwp = r40_bal_sub_diaries_bwp;
		}

		public String getR41_intrest_expended() {
			return r41_intrest_expended;
		}

		public void setR41_intrest_expended(String r41_intrest_expended) {
			this.r41_intrest_expended = r41_intrest_expended;
		}

		public BigDecimal getR41_fig_bal_sheet() {
			return r41_fig_bal_sheet;
		}

		public void setR41_fig_bal_sheet(BigDecimal r41_fig_bal_sheet) {
			this.r41_fig_bal_sheet = r41_fig_bal_sheet;
		}

		public BigDecimal getR41_fig_bal_sheet_bwp() {
			return r41_fig_bal_sheet_bwp;
		}

		public void setR41_fig_bal_sheet_bwp(BigDecimal r41_fig_bal_sheet_bwp) {
			this.r41_fig_bal_sheet_bwp = r41_fig_bal_sheet_bwp;
		}

		public BigDecimal getR41_amt_statement_adj() {
			return r41_amt_statement_adj;
		}

		public void setR41_amt_statement_adj(BigDecimal r41_amt_statement_adj) {
			this.r41_amt_statement_adj = r41_amt_statement_adj;
		}

		public BigDecimal getR41_amt_statement_adj_bwp() {
			return r41_amt_statement_adj_bwp;
		}

		public void setR41_amt_statement_adj_bwp(BigDecimal r41_amt_statement_adj_bwp) {
			this.r41_amt_statement_adj_bwp = r41_amt_statement_adj_bwp;
		}

		public BigDecimal getR41_net_amt() {
			return r41_net_amt;
		}

		public void setR41_net_amt(BigDecimal r41_net_amt) {
			this.r41_net_amt = r41_net_amt;
		}

		public BigDecimal getR41_net_amt_bwp() {
			return r41_net_amt_bwp;
		}

		public void setR41_net_amt_bwp(BigDecimal r41_net_amt_bwp) {
			this.r41_net_amt_bwp = r41_net_amt_bwp;
		}

		public BigDecimal getR41_bal_sub() {
			return r41_bal_sub;
		}

		public void setR41_bal_sub(BigDecimal r41_bal_sub) {
			this.r41_bal_sub = r41_bal_sub;
		}

		public BigDecimal getR41_bal_sub_bwp() {
			return r41_bal_sub_bwp;
		}

		public void setR41_bal_sub_bwp(BigDecimal r41_bal_sub_bwp) {
			this.r41_bal_sub_bwp = r41_bal_sub_bwp;
		}

		public BigDecimal getR41_bal_sub_diaries_bwp() {
			return r41_bal_sub_diaries_bwp;
		}

		public void setR41_bal_sub_diaries_bwp(BigDecimal r41_bal_sub_diaries_bwp) {
			this.r41_bal_sub_diaries_bwp = r41_bal_sub_diaries_bwp;
		}

		public String getR42_intrest_expended() {
			return r42_intrest_expended;
		}

		public void setR42_intrest_expended(String r42_intrest_expended) {
			this.r42_intrest_expended = r42_intrest_expended;
		}

		public BigDecimal getR42_fig_bal_sheet() {
			return r42_fig_bal_sheet;
		}

		public void setR42_fig_bal_sheet(BigDecimal r42_fig_bal_sheet) {
			this.r42_fig_bal_sheet = r42_fig_bal_sheet;
		}

		public BigDecimal getR42_fig_bal_sheet_bwp() {
			return r42_fig_bal_sheet_bwp;
		}

		public void setR42_fig_bal_sheet_bwp(BigDecimal r42_fig_bal_sheet_bwp) {
			this.r42_fig_bal_sheet_bwp = r42_fig_bal_sheet_bwp;
		}

		public BigDecimal getR42_amt_statement_adj() {
			return r42_amt_statement_adj;
		}

		public void setR42_amt_statement_adj(BigDecimal r42_amt_statement_adj) {
			this.r42_amt_statement_adj = r42_amt_statement_adj;
		}

		public BigDecimal getR42_amt_statement_adj_bwp() {
			return r42_amt_statement_adj_bwp;
		}

		public void setR42_amt_statement_adj_bwp(BigDecimal r42_amt_statement_adj_bwp) {
			this.r42_amt_statement_adj_bwp = r42_amt_statement_adj_bwp;
		}

		public BigDecimal getR42_net_amt() {
			return r42_net_amt;
		}

		public void setR42_net_amt(BigDecimal r42_net_amt) {
			this.r42_net_amt = r42_net_amt;
		}

		public BigDecimal getR42_net_amt_bwp() {
			return r42_net_amt_bwp;
		}

		public void setR42_net_amt_bwp(BigDecimal r42_net_amt_bwp) {
			this.r42_net_amt_bwp = r42_net_amt_bwp;
		}

		public BigDecimal getR42_bal_sub() {
			return r42_bal_sub;
		}

		public void setR42_bal_sub(BigDecimal r42_bal_sub) {
			this.r42_bal_sub = r42_bal_sub;
		}

		public BigDecimal getR42_bal_sub_bwp() {
			return r42_bal_sub_bwp;
		}

		public void setR42_bal_sub_bwp(BigDecimal r42_bal_sub_bwp) {
			this.r42_bal_sub_bwp = r42_bal_sub_bwp;
		}

		public BigDecimal getR42_bal_sub_diaries_bwp() {
			return r42_bal_sub_diaries_bwp;
		}

		public void setR42_bal_sub_diaries_bwp(BigDecimal r42_bal_sub_diaries_bwp) {
			this.r42_bal_sub_diaries_bwp = r42_bal_sub_diaries_bwp;
		}

		public String getR43_intrest_expended() {
			return r43_intrest_expended;
		}

		public void setR43_intrest_expended(String r43_intrest_expended) {
			this.r43_intrest_expended = r43_intrest_expended;
		}

		public BigDecimal getR43_fig_bal_sheet() {
			return r43_fig_bal_sheet;
		}

		public void setR43_fig_bal_sheet(BigDecimal r43_fig_bal_sheet) {
			this.r43_fig_bal_sheet = r43_fig_bal_sheet;
		}

		public BigDecimal getR43_fig_bal_sheet_bwp() {
			return r43_fig_bal_sheet_bwp;
		}

		public void setR43_fig_bal_sheet_bwp(BigDecimal r43_fig_bal_sheet_bwp) {
			this.r43_fig_bal_sheet_bwp = r43_fig_bal_sheet_bwp;
		}

		public BigDecimal getR43_amt_statement_adj() {
			return r43_amt_statement_adj;
		}

		public void setR43_amt_statement_adj(BigDecimal r43_amt_statement_adj) {
			this.r43_amt_statement_adj = r43_amt_statement_adj;
		}

		public BigDecimal getR43_amt_statement_adj_bwp() {
			return r43_amt_statement_adj_bwp;
		}

		public void setR43_amt_statement_adj_bwp(BigDecimal r43_amt_statement_adj_bwp) {
			this.r43_amt_statement_adj_bwp = r43_amt_statement_adj_bwp;
		}

		public BigDecimal getR43_net_amt() {
			return r43_net_amt;
		}

		public void setR43_net_amt(BigDecimal r43_net_amt) {
			this.r43_net_amt = r43_net_amt;
		}

		public BigDecimal getR43_net_amt_bwp() {
			return r43_net_amt_bwp;
		}

		public void setR43_net_amt_bwp(BigDecimal r43_net_amt_bwp) {
			this.r43_net_amt_bwp = r43_net_amt_bwp;
		}

		public BigDecimal getR43_bal_sub() {
			return r43_bal_sub;
		}

		public void setR43_bal_sub(BigDecimal r43_bal_sub) {
			this.r43_bal_sub = r43_bal_sub;
		}

		public BigDecimal getR43_bal_sub_bwp() {
			return r43_bal_sub_bwp;
		}

		public void setR43_bal_sub_bwp(BigDecimal r43_bal_sub_bwp) {
			this.r43_bal_sub_bwp = r43_bal_sub_bwp;
		}

		public BigDecimal getR43_bal_sub_diaries_bwp() {
			return r43_bal_sub_diaries_bwp;
		}

		public void setR43_bal_sub_diaries_bwp(BigDecimal r43_bal_sub_diaries_bwp) {
			this.r43_bal_sub_diaries_bwp = r43_bal_sub_diaries_bwp;
		}

		public String getR48_operating_expenses() {
			return r48_operating_expenses;
		}

		public void setR48_operating_expenses(String r48_operating_expenses) {
			this.r48_operating_expenses = r48_operating_expenses;
		}

		public BigDecimal getR48_fig_bal_sheet() {
			return r48_fig_bal_sheet;
		}

		public void setR48_fig_bal_sheet(BigDecimal r48_fig_bal_sheet) {
			this.r48_fig_bal_sheet = r48_fig_bal_sheet;
		}

		public BigDecimal getR48_fig_bal_sheet_bwp() {
			return r48_fig_bal_sheet_bwp;
		}

		public void setR48_fig_bal_sheet_bwp(BigDecimal r48_fig_bal_sheet_bwp) {
			this.r48_fig_bal_sheet_bwp = r48_fig_bal_sheet_bwp;
		}

		public BigDecimal getR48_amt_statement_adj() {
			return r48_amt_statement_adj;
		}

		public void setR48_amt_statement_adj(BigDecimal r48_amt_statement_adj) {
			this.r48_amt_statement_adj = r48_amt_statement_adj;
		}

		public BigDecimal getR48_amt_statement_adj_bwp() {
			return r48_amt_statement_adj_bwp;
		}

		public void setR48_amt_statement_adj_bwp(BigDecimal r48_amt_statement_adj_bwp) {
			this.r48_amt_statement_adj_bwp = r48_amt_statement_adj_bwp;
		}

		public BigDecimal getR48_net_amt() {
			return r48_net_amt;
		}

		public void setR48_net_amt(BigDecimal r48_net_amt) {
			this.r48_net_amt = r48_net_amt;
		}

		public BigDecimal getR48_net_amt_bwp() {
			return r48_net_amt_bwp;
		}

		public void setR48_net_amt_bwp(BigDecimal r48_net_amt_bwp) {
			this.r48_net_amt_bwp = r48_net_amt_bwp;
		}

		public BigDecimal getR48_bal_sub() {
			return r48_bal_sub;
		}

		public void setR48_bal_sub(BigDecimal r48_bal_sub) {
			this.r48_bal_sub = r48_bal_sub;
		}

		public BigDecimal getR48_bal_sub_bwp() {
			return r48_bal_sub_bwp;
		}

		public void setR48_bal_sub_bwp(BigDecimal r48_bal_sub_bwp) {
			this.r48_bal_sub_bwp = r48_bal_sub_bwp;
		}

		public BigDecimal getR48_bal_sub_diaries_bwp() {
			return r48_bal_sub_diaries_bwp;
		}

		public void setR48_bal_sub_diaries_bwp(BigDecimal r48_bal_sub_diaries_bwp) {
			this.r48_bal_sub_diaries_bwp = r48_bal_sub_diaries_bwp;
		}

		public String getR49_operating_expenses() {
			return r49_operating_expenses;
		}

		public void setR49_operating_expenses(String r49_operating_expenses) {
			this.r49_operating_expenses = r49_operating_expenses;
		}

		public BigDecimal getR49_fig_bal_sheet() {
			return r49_fig_bal_sheet;
		}

		public void setR49_fig_bal_sheet(BigDecimal r49_fig_bal_sheet) {
			this.r49_fig_bal_sheet = r49_fig_bal_sheet;
		}

		public BigDecimal getR49_fig_bal_sheet_bwp() {
			return r49_fig_bal_sheet_bwp;
		}

		public void setR49_fig_bal_sheet_bwp(BigDecimal r49_fig_bal_sheet_bwp) {
			this.r49_fig_bal_sheet_bwp = r49_fig_bal_sheet_bwp;
		}

		public BigDecimal getR49_amt_statement_adj() {
			return r49_amt_statement_adj;
		}

		public void setR49_amt_statement_adj(BigDecimal r49_amt_statement_adj) {
			this.r49_amt_statement_adj = r49_amt_statement_adj;
		}

		public BigDecimal getR49_amt_statement_adj_bwp() {
			return r49_amt_statement_adj_bwp;
		}

		public void setR49_amt_statement_adj_bwp(BigDecimal r49_amt_statement_adj_bwp) {
			this.r49_amt_statement_adj_bwp = r49_amt_statement_adj_bwp;
		}

		public BigDecimal getR49_net_amt() {
			return r49_net_amt;
		}

		public void setR49_net_amt(BigDecimal r49_net_amt) {
			this.r49_net_amt = r49_net_amt;
		}

		public BigDecimal getR49_net_amt_bwp() {
			return r49_net_amt_bwp;
		}

		public void setR49_net_amt_bwp(BigDecimal r49_net_amt_bwp) {
			this.r49_net_amt_bwp = r49_net_amt_bwp;
		}

		public BigDecimal getR49_bal_sub() {
			return r49_bal_sub;
		}

		public void setR49_bal_sub(BigDecimal r49_bal_sub) {
			this.r49_bal_sub = r49_bal_sub;
		}

		public BigDecimal getR49_bal_sub_bwp() {
			return r49_bal_sub_bwp;
		}

		public void setR49_bal_sub_bwp(BigDecimal r49_bal_sub_bwp) {
			this.r49_bal_sub_bwp = r49_bal_sub_bwp;
		}

		public BigDecimal getR49_bal_sub_diaries_bwp() {
			return r49_bal_sub_diaries_bwp;
		}

		public void setR49_bal_sub_diaries_bwp(BigDecimal r49_bal_sub_diaries_bwp) {
			this.r49_bal_sub_diaries_bwp = r49_bal_sub_diaries_bwp;
		}

		public String getR50_operating_expenses() {
			return r50_operating_expenses;
		}

		public void setR50_operating_expenses(String r50_operating_expenses) {
			this.r50_operating_expenses = r50_operating_expenses;
		}

		public BigDecimal getR50_fig_bal_sheet() {
			return r50_fig_bal_sheet;
		}

		public void setR50_fig_bal_sheet(BigDecimal r50_fig_bal_sheet) {
			this.r50_fig_bal_sheet = r50_fig_bal_sheet;
		}

		public BigDecimal getR50_fig_bal_sheet_bwp() {
			return r50_fig_bal_sheet_bwp;
		}

		public void setR50_fig_bal_sheet_bwp(BigDecimal r50_fig_bal_sheet_bwp) {
			this.r50_fig_bal_sheet_bwp = r50_fig_bal_sheet_bwp;
		}

		public BigDecimal getR50_amt_statement_adj() {
			return r50_amt_statement_adj;
		}

		public void setR50_amt_statement_adj(BigDecimal r50_amt_statement_adj) {
			this.r50_amt_statement_adj = r50_amt_statement_adj;
		}

		public BigDecimal getR50_amt_statement_adj_bwp() {
			return r50_amt_statement_adj_bwp;
		}

		public void setR50_amt_statement_adj_bwp(BigDecimal r50_amt_statement_adj_bwp) {
			this.r50_amt_statement_adj_bwp = r50_amt_statement_adj_bwp;
		}

		public BigDecimal getR50_net_amt() {
			return r50_net_amt;
		}

		public void setR50_net_amt(BigDecimal r50_net_amt) {
			this.r50_net_amt = r50_net_amt;
		}

		public BigDecimal getR50_net_amt_bwp() {
			return r50_net_amt_bwp;
		}

		public void setR50_net_amt_bwp(BigDecimal r50_net_amt_bwp) {
			this.r50_net_amt_bwp = r50_net_amt_bwp;
		}

		public BigDecimal getR50_bal_sub() {
			return r50_bal_sub;
		}

		public void setR50_bal_sub(BigDecimal r50_bal_sub) {
			this.r50_bal_sub = r50_bal_sub;
		}

		public BigDecimal getR50_bal_sub_bwp() {
			return r50_bal_sub_bwp;
		}

		public void setR50_bal_sub_bwp(BigDecimal r50_bal_sub_bwp) {
			this.r50_bal_sub_bwp = r50_bal_sub_bwp;
		}

		public BigDecimal getR50_bal_sub_diaries_bwp() {
			return r50_bal_sub_diaries_bwp;
		}

		public void setR50_bal_sub_diaries_bwp(BigDecimal r50_bal_sub_diaries_bwp) {
			this.r50_bal_sub_diaries_bwp = r50_bal_sub_diaries_bwp;
		}

		public String getR51_operating_expenses() {
			return r51_operating_expenses;
		}

		public void setR51_operating_expenses(String r51_operating_expenses) {
			this.r51_operating_expenses = r51_operating_expenses;
		}

		public BigDecimal getR51_fig_bal_sheet() {
			return r51_fig_bal_sheet;
		}

		public void setR51_fig_bal_sheet(BigDecimal r51_fig_bal_sheet) {
			this.r51_fig_bal_sheet = r51_fig_bal_sheet;
		}

		public BigDecimal getR51_fig_bal_sheet_bwp() {
			return r51_fig_bal_sheet_bwp;
		}

		public void setR51_fig_bal_sheet_bwp(BigDecimal r51_fig_bal_sheet_bwp) {
			this.r51_fig_bal_sheet_bwp = r51_fig_bal_sheet_bwp;
		}

		public BigDecimal getR51_amt_statement_adj() {
			return r51_amt_statement_adj;
		}

		public void setR51_amt_statement_adj(BigDecimal r51_amt_statement_adj) {
			this.r51_amt_statement_adj = r51_amt_statement_adj;
		}

		public BigDecimal getR51_amt_statement_adj_bwp() {
			return r51_amt_statement_adj_bwp;
		}

		public void setR51_amt_statement_adj_bwp(BigDecimal r51_amt_statement_adj_bwp) {
			this.r51_amt_statement_adj_bwp = r51_amt_statement_adj_bwp;
		}

		public BigDecimal getR51_net_amt() {
			return r51_net_amt;
		}

		public void setR51_net_amt(BigDecimal r51_net_amt) {
			this.r51_net_amt = r51_net_amt;
		}

		public BigDecimal getR51_net_amt_bwp() {
			return r51_net_amt_bwp;
		}

		public void setR51_net_amt_bwp(BigDecimal r51_net_amt_bwp) {
			this.r51_net_amt_bwp = r51_net_amt_bwp;
		}

		public BigDecimal getR51_bal_sub() {
			return r51_bal_sub;
		}

		public void setR51_bal_sub(BigDecimal r51_bal_sub) {
			this.r51_bal_sub = r51_bal_sub;
		}

		public BigDecimal getR51_bal_sub_bwp() {
			return r51_bal_sub_bwp;
		}

		public void setR51_bal_sub_bwp(BigDecimal r51_bal_sub_bwp) {
			this.r51_bal_sub_bwp = r51_bal_sub_bwp;
		}

		public BigDecimal getR51_bal_sub_diaries_bwp() {
			return r51_bal_sub_diaries_bwp;
		}

		public void setR51_bal_sub_diaries_bwp(BigDecimal r51_bal_sub_diaries_bwp) {
			this.r51_bal_sub_diaries_bwp = r51_bal_sub_diaries_bwp;
		}

		public String getR52_operating_expenses() {
			return r52_operating_expenses;
		}

		public void setR52_operating_expenses(String r52_operating_expenses) {
			this.r52_operating_expenses = r52_operating_expenses;
		}

		public BigDecimal getR52_fig_bal_sheet() {
			return r52_fig_bal_sheet;
		}

		public void setR52_fig_bal_sheet(BigDecimal r52_fig_bal_sheet) {
			this.r52_fig_bal_sheet = r52_fig_bal_sheet;
		}

		public BigDecimal getR52_fig_bal_sheet_bwp() {
			return r52_fig_bal_sheet_bwp;
		}

		public void setR52_fig_bal_sheet_bwp(BigDecimal r52_fig_bal_sheet_bwp) {
			this.r52_fig_bal_sheet_bwp = r52_fig_bal_sheet_bwp;
		}

		public BigDecimal getR52_amt_statement_adj() {
			return r52_amt_statement_adj;
		}

		public void setR52_amt_statement_adj(BigDecimal r52_amt_statement_adj) {
			this.r52_amt_statement_adj = r52_amt_statement_adj;
		}

		public BigDecimal getR52_amt_statement_adj_bwp() {
			return r52_amt_statement_adj_bwp;
		}

		public void setR52_amt_statement_adj_bwp(BigDecimal r52_amt_statement_adj_bwp) {
			this.r52_amt_statement_adj_bwp = r52_amt_statement_adj_bwp;
		}

		public BigDecimal getR52_net_amt() {
			return r52_net_amt;
		}

		public void setR52_net_amt(BigDecimal r52_net_amt) {
			this.r52_net_amt = r52_net_amt;
		}

		public BigDecimal getR52_net_amt_bwp() {
			return r52_net_amt_bwp;
		}

		public void setR52_net_amt_bwp(BigDecimal r52_net_amt_bwp) {
			this.r52_net_amt_bwp = r52_net_amt_bwp;
		}

		public BigDecimal getR52_bal_sub() {
			return r52_bal_sub;
		}

		public void setR52_bal_sub(BigDecimal r52_bal_sub) {
			this.r52_bal_sub = r52_bal_sub;
		}

		public BigDecimal getR52_bal_sub_bwp() {
			return r52_bal_sub_bwp;
		}

		public void setR52_bal_sub_bwp(BigDecimal r52_bal_sub_bwp) {
			this.r52_bal_sub_bwp = r52_bal_sub_bwp;
		}

		public BigDecimal getR52_bal_sub_diaries_bwp() {
			return r52_bal_sub_diaries_bwp;
		}

		public void setR52_bal_sub_diaries_bwp(BigDecimal r52_bal_sub_diaries_bwp) {
			this.r52_bal_sub_diaries_bwp = r52_bal_sub_diaries_bwp;
		}

		public String getR53_operating_expenses() {
			return r53_operating_expenses;
		}

		public void setR53_operating_expenses(String r53_operating_expenses) {
			this.r53_operating_expenses = r53_operating_expenses;
		}

		public BigDecimal getR53_fig_bal_sheet() {
			return r53_fig_bal_sheet;
		}

		public void setR53_fig_bal_sheet(BigDecimal r53_fig_bal_sheet) {
			this.r53_fig_bal_sheet = r53_fig_bal_sheet;
		}

		public BigDecimal getR53_fig_bal_sheet_bwp() {
			return r53_fig_bal_sheet_bwp;
		}

		public void setR53_fig_bal_sheet_bwp(BigDecimal r53_fig_bal_sheet_bwp) {
			this.r53_fig_bal_sheet_bwp = r53_fig_bal_sheet_bwp;
		}

		public BigDecimal getR53_amt_statement_adj() {
			return r53_amt_statement_adj;
		}

		public void setR53_amt_statement_adj(BigDecimal r53_amt_statement_adj) {
			this.r53_amt_statement_adj = r53_amt_statement_adj;
		}

		public BigDecimal getR53_amt_statement_adj_bwp() {
			return r53_amt_statement_adj_bwp;
		}

		public void setR53_amt_statement_adj_bwp(BigDecimal r53_amt_statement_adj_bwp) {
			this.r53_amt_statement_adj_bwp = r53_amt_statement_adj_bwp;
		}

		public BigDecimal getR53_net_amt() {
			return r53_net_amt;
		}

		public void setR53_net_amt(BigDecimal r53_net_amt) {
			this.r53_net_amt = r53_net_amt;
		}

		public BigDecimal getR53_net_amt_bwp() {
			return r53_net_amt_bwp;
		}

		public void setR53_net_amt_bwp(BigDecimal r53_net_amt_bwp) {
			this.r53_net_amt_bwp = r53_net_amt_bwp;
		}

		public BigDecimal getR53_bal_sub() {
			return r53_bal_sub;
		}

		public void setR53_bal_sub(BigDecimal r53_bal_sub) {
			this.r53_bal_sub = r53_bal_sub;
		}

		public BigDecimal getR53_bal_sub_bwp() {
			return r53_bal_sub_bwp;
		}

		public void setR53_bal_sub_bwp(BigDecimal r53_bal_sub_bwp) {
			this.r53_bal_sub_bwp = r53_bal_sub_bwp;
		}

		public BigDecimal getR53_bal_sub_diaries_bwp() {
			return r53_bal_sub_diaries_bwp;
		}

		public void setR53_bal_sub_diaries_bwp(BigDecimal r53_bal_sub_diaries_bwp) {
			this.r53_bal_sub_diaries_bwp = r53_bal_sub_diaries_bwp;
		}

		public String getR54_operating_expenses() {
			return r54_operating_expenses;
		}

		public void setR54_operating_expenses(String r54_operating_expenses) {
			this.r54_operating_expenses = r54_operating_expenses;
		}

		public BigDecimal getR54_fig_bal_sheet() {
			return r54_fig_bal_sheet;
		}

		public void setR54_fig_bal_sheet(BigDecimal r54_fig_bal_sheet) {
			this.r54_fig_bal_sheet = r54_fig_bal_sheet;
		}

		public BigDecimal getR54_fig_bal_sheet_bwp() {
			return r54_fig_bal_sheet_bwp;
		}

		public void setR54_fig_bal_sheet_bwp(BigDecimal r54_fig_bal_sheet_bwp) {
			this.r54_fig_bal_sheet_bwp = r54_fig_bal_sheet_bwp;
		}

		public BigDecimal getR54_amt_statement_adj() {
			return r54_amt_statement_adj;
		}

		public void setR54_amt_statement_adj(BigDecimal r54_amt_statement_adj) {
			this.r54_amt_statement_adj = r54_amt_statement_adj;
		}

		public BigDecimal getR54_amt_statement_adj_bwp() {
			return r54_amt_statement_adj_bwp;
		}

		public void setR54_amt_statement_adj_bwp(BigDecimal r54_amt_statement_adj_bwp) {
			this.r54_amt_statement_adj_bwp = r54_amt_statement_adj_bwp;
		}

		public BigDecimal getR54_net_amt() {
			return r54_net_amt;
		}

		public void setR54_net_amt(BigDecimal r54_net_amt) {
			this.r54_net_amt = r54_net_amt;
		}

		public BigDecimal getR54_net_amt_bwp() {
			return r54_net_amt_bwp;
		}

		public void setR54_net_amt_bwp(BigDecimal r54_net_amt_bwp) {
			this.r54_net_amt_bwp = r54_net_amt_bwp;
		}

		public BigDecimal getR54_bal_sub() {
			return r54_bal_sub;
		}

		public void setR54_bal_sub(BigDecimal r54_bal_sub) {
			this.r54_bal_sub = r54_bal_sub;
		}

		public BigDecimal getR54_bal_sub_bwp() {
			return r54_bal_sub_bwp;
		}

		public void setR54_bal_sub_bwp(BigDecimal r54_bal_sub_bwp) {
			this.r54_bal_sub_bwp = r54_bal_sub_bwp;
		}

		public BigDecimal getR54_bal_sub_diaries_bwp() {
			return r54_bal_sub_diaries_bwp;
		}

		public void setR54_bal_sub_diaries_bwp(BigDecimal r54_bal_sub_diaries_bwp) {
			this.r54_bal_sub_diaries_bwp = r54_bal_sub_diaries_bwp;
		}

		public String getR55_operating_expenses() {
			return r55_operating_expenses;
		}

		public void setR55_operating_expenses(String r55_operating_expenses) {
			this.r55_operating_expenses = r55_operating_expenses;
		}

		public BigDecimal getR55_fig_bal_sheet() {
			return r55_fig_bal_sheet;
		}

		public void setR55_fig_bal_sheet(BigDecimal r55_fig_bal_sheet) {
			this.r55_fig_bal_sheet = r55_fig_bal_sheet;
		}

		public BigDecimal getR55_fig_bal_sheet_bwp() {
			return r55_fig_bal_sheet_bwp;
		}

		public void setR55_fig_bal_sheet_bwp(BigDecimal r55_fig_bal_sheet_bwp) {
			this.r55_fig_bal_sheet_bwp = r55_fig_bal_sheet_bwp;
		}

		public BigDecimal getR55_amt_statement_adj() {
			return r55_amt_statement_adj;
		}

		public void setR55_amt_statement_adj(BigDecimal r55_amt_statement_adj) {
			this.r55_amt_statement_adj = r55_amt_statement_adj;
		}

		public BigDecimal getR55_amt_statement_adj_bwp() {
			return r55_amt_statement_adj_bwp;
		}

		public void setR55_amt_statement_adj_bwp(BigDecimal r55_amt_statement_adj_bwp) {
			this.r55_amt_statement_adj_bwp = r55_amt_statement_adj_bwp;
		}

		public BigDecimal getR55_net_amt() {
			return r55_net_amt;
		}

		public void setR55_net_amt(BigDecimal r55_net_amt) {
			this.r55_net_amt = r55_net_amt;
		}

		public BigDecimal getR55_net_amt_bwp() {
			return r55_net_amt_bwp;
		}

		public void setR55_net_amt_bwp(BigDecimal r55_net_amt_bwp) {
			this.r55_net_amt_bwp = r55_net_amt_bwp;
		}

		public BigDecimal getR55_bal_sub() {
			return r55_bal_sub;
		}

		public void setR55_bal_sub(BigDecimal r55_bal_sub) {
			this.r55_bal_sub = r55_bal_sub;
		}

		public BigDecimal getR55_bal_sub_bwp() {
			return r55_bal_sub_bwp;
		}

		public void setR55_bal_sub_bwp(BigDecimal r55_bal_sub_bwp) {
			this.r55_bal_sub_bwp = r55_bal_sub_bwp;
		}

		public BigDecimal getR55_bal_sub_diaries_bwp() {
			return r55_bal_sub_diaries_bwp;
		}

		public void setR55_bal_sub_diaries_bwp(BigDecimal r55_bal_sub_diaries_bwp) {
			this.r55_bal_sub_diaries_bwp = r55_bal_sub_diaries_bwp;
		}

		public String getR56_operating_expenses() {
			return r56_operating_expenses;
		}

		public void setR56_operating_expenses(String r56_operating_expenses) {
			this.r56_operating_expenses = r56_operating_expenses;
		}

		public BigDecimal getR56_fig_bal_sheet() {
			return r56_fig_bal_sheet;
		}

		public void setR56_fig_bal_sheet(BigDecimal r56_fig_bal_sheet) {
			this.r56_fig_bal_sheet = r56_fig_bal_sheet;
		}

		public BigDecimal getR56_fig_bal_sheet_bwp() {
			return r56_fig_bal_sheet_bwp;
		}

		public void setR56_fig_bal_sheet_bwp(BigDecimal r56_fig_bal_sheet_bwp) {
			this.r56_fig_bal_sheet_bwp = r56_fig_bal_sheet_bwp;
		}

		public BigDecimal getR56_amt_statement_adj() {
			return r56_amt_statement_adj;
		}

		public void setR56_amt_statement_adj(BigDecimal r56_amt_statement_adj) {
			this.r56_amt_statement_adj = r56_amt_statement_adj;
		}

		public BigDecimal getR56_amt_statement_adj_bwp() {
			return r56_amt_statement_adj_bwp;
		}

		public void setR56_amt_statement_adj_bwp(BigDecimal r56_amt_statement_adj_bwp) {
			this.r56_amt_statement_adj_bwp = r56_amt_statement_adj_bwp;
		}

		public BigDecimal getR56_net_amt() {
			return r56_net_amt;
		}

		public void setR56_net_amt(BigDecimal r56_net_amt) {
			this.r56_net_amt = r56_net_amt;
		}

		public BigDecimal getR56_net_amt_bwp() {
			return r56_net_amt_bwp;
		}

		public void setR56_net_amt_bwp(BigDecimal r56_net_amt_bwp) {
			this.r56_net_amt_bwp = r56_net_amt_bwp;
		}

		public BigDecimal getR56_bal_sub() {
			return r56_bal_sub;
		}

		public void setR56_bal_sub(BigDecimal r56_bal_sub) {
			this.r56_bal_sub = r56_bal_sub;
		}

		public BigDecimal getR56_bal_sub_bwp() {
			return r56_bal_sub_bwp;
		}

		public void setR56_bal_sub_bwp(BigDecimal r56_bal_sub_bwp) {
			this.r56_bal_sub_bwp = r56_bal_sub_bwp;
		}

		public BigDecimal getR56_bal_sub_diaries_bwp() {
			return r56_bal_sub_diaries_bwp;
		}

		public void setR56_bal_sub_diaries_bwp(BigDecimal r56_bal_sub_diaries_bwp) {
			this.r56_bal_sub_diaries_bwp = r56_bal_sub_diaries_bwp;
		}

		public String getR57_operating_expenses() {
			return r57_operating_expenses;
		}

		public void setR57_operating_expenses(String r57_operating_expenses) {
			this.r57_operating_expenses = r57_operating_expenses;
		}

		public BigDecimal getR57_fig_bal_sheet() {
			return r57_fig_bal_sheet;
		}

		public void setR57_fig_bal_sheet(BigDecimal r57_fig_bal_sheet) {
			this.r57_fig_bal_sheet = r57_fig_bal_sheet;
		}

		public BigDecimal getR57_fig_bal_sheet_bwp() {
			return r57_fig_bal_sheet_bwp;
		}

		public void setR57_fig_bal_sheet_bwp(BigDecimal r57_fig_bal_sheet_bwp) {
			this.r57_fig_bal_sheet_bwp = r57_fig_bal_sheet_bwp;
		}

		public BigDecimal getR57_amt_statement_adj() {
			return r57_amt_statement_adj;
		}

		public void setR57_amt_statement_adj(BigDecimal r57_amt_statement_adj) {
			this.r57_amt_statement_adj = r57_amt_statement_adj;
		}

		public BigDecimal getR57_amt_statement_adj_bwp() {
			return r57_amt_statement_adj_bwp;
		}

		public void setR57_amt_statement_adj_bwp(BigDecimal r57_amt_statement_adj_bwp) {
			this.r57_amt_statement_adj_bwp = r57_amt_statement_adj_bwp;
		}

		public BigDecimal getR57_net_amt() {
			return r57_net_amt;
		}

		public void setR57_net_amt(BigDecimal r57_net_amt) {
			this.r57_net_amt = r57_net_amt;
		}

		public BigDecimal getR57_net_amt_bwp() {
			return r57_net_amt_bwp;
		}

		public void setR57_net_amt_bwp(BigDecimal r57_net_amt_bwp) {
			this.r57_net_amt_bwp = r57_net_amt_bwp;
		}

		public BigDecimal getR57_bal_sub() {
			return r57_bal_sub;
		}

		public void setR57_bal_sub(BigDecimal r57_bal_sub) {
			this.r57_bal_sub = r57_bal_sub;
		}

		public BigDecimal getR57_bal_sub_bwp() {
			return r57_bal_sub_bwp;
		}

		public void setR57_bal_sub_bwp(BigDecimal r57_bal_sub_bwp) {
			this.r57_bal_sub_bwp = r57_bal_sub_bwp;
		}

		public BigDecimal getR57_bal_sub_diaries_bwp() {
			return r57_bal_sub_diaries_bwp;
		}

		public void setR57_bal_sub_diaries_bwp(BigDecimal r57_bal_sub_diaries_bwp) {
			this.r57_bal_sub_diaries_bwp = r57_bal_sub_diaries_bwp;
		}

		public String getR58_operating_expenses() {
			return r58_operating_expenses;
		}

		public void setR58_operating_expenses(String r58_operating_expenses) {
			this.r58_operating_expenses = r58_operating_expenses;
		}

		public BigDecimal getR58_fig_bal_sheet() {
			return r58_fig_bal_sheet;
		}

		public void setR58_fig_bal_sheet(BigDecimal r58_fig_bal_sheet) {
			this.r58_fig_bal_sheet = r58_fig_bal_sheet;
		}

		public BigDecimal getR58_fig_bal_sheet_bwp() {
			return r58_fig_bal_sheet_bwp;
		}

		public void setR58_fig_bal_sheet_bwp(BigDecimal r58_fig_bal_sheet_bwp) {
			this.r58_fig_bal_sheet_bwp = r58_fig_bal_sheet_bwp;
		}

		public BigDecimal getR58_amt_statement_adj() {
			return r58_amt_statement_adj;
		}

		public void setR58_amt_statement_adj(BigDecimal r58_amt_statement_adj) {
			this.r58_amt_statement_adj = r58_amt_statement_adj;
		}

		public BigDecimal getR58_amt_statement_adj_bwp() {
			return r58_amt_statement_adj_bwp;
		}

		public void setR58_amt_statement_adj_bwp(BigDecimal r58_amt_statement_adj_bwp) {
			this.r58_amt_statement_adj_bwp = r58_amt_statement_adj_bwp;
		}

		public BigDecimal getR58_net_amt() {
			return r58_net_amt;
		}

		public void setR58_net_amt(BigDecimal r58_net_amt) {
			this.r58_net_amt = r58_net_amt;
		}

		public BigDecimal getR58_net_amt_bwp() {
			return r58_net_amt_bwp;
		}

		public void setR58_net_amt_bwp(BigDecimal r58_net_amt_bwp) {
			this.r58_net_amt_bwp = r58_net_amt_bwp;
		}

		public BigDecimal getR58_bal_sub() {
			return r58_bal_sub;
		}

		public void setR58_bal_sub(BigDecimal r58_bal_sub) {
			this.r58_bal_sub = r58_bal_sub;
		}

		public BigDecimal getR58_bal_sub_bwp() {
			return r58_bal_sub_bwp;
		}

		public void setR58_bal_sub_bwp(BigDecimal r58_bal_sub_bwp) {
			this.r58_bal_sub_bwp = r58_bal_sub_bwp;
		}

		public BigDecimal getR58_bal_sub_diaries_bwp() {
			return r58_bal_sub_diaries_bwp;
		}

		public void setR58_bal_sub_diaries_bwp(BigDecimal r58_bal_sub_diaries_bwp) {
			this.r58_bal_sub_diaries_bwp = r58_bal_sub_diaries_bwp;
		}

		public String getR59_operating_expenses() {
			return r59_operating_expenses;
		}

		public void setR59_operating_expenses(String r59_operating_expenses) {
			this.r59_operating_expenses = r59_operating_expenses;
		}

		public BigDecimal getR59_fig_bal_sheet() {
			return r59_fig_bal_sheet;
		}

		public void setR59_fig_bal_sheet(BigDecimal r59_fig_bal_sheet) {
			this.r59_fig_bal_sheet = r59_fig_bal_sheet;
		}

		public BigDecimal getR59_fig_bal_sheet_bwp() {
			return r59_fig_bal_sheet_bwp;
		}

		public void setR59_fig_bal_sheet_bwp(BigDecimal r59_fig_bal_sheet_bwp) {
			this.r59_fig_bal_sheet_bwp = r59_fig_bal_sheet_bwp;
		}

		public BigDecimal getR59_amt_statement_adj() {
			return r59_amt_statement_adj;
		}

		public void setR59_amt_statement_adj(BigDecimal r59_amt_statement_adj) {
			this.r59_amt_statement_adj = r59_amt_statement_adj;
		}

		public BigDecimal getR59_amt_statement_adj_bwp() {
			return r59_amt_statement_adj_bwp;
		}

		public void setR59_amt_statement_adj_bwp(BigDecimal r59_amt_statement_adj_bwp) {
			this.r59_amt_statement_adj_bwp = r59_amt_statement_adj_bwp;
		}

		public BigDecimal getR59_net_amt() {
			return r59_net_amt;
		}

		public void setR59_net_amt(BigDecimal r59_net_amt) {
			this.r59_net_amt = r59_net_amt;
		}

		public BigDecimal getR59_net_amt_bwp() {
			return r59_net_amt_bwp;
		}

		public void setR59_net_amt_bwp(BigDecimal r59_net_amt_bwp) {
			this.r59_net_amt_bwp = r59_net_amt_bwp;
		}

		public BigDecimal getR59_bal_sub() {
			return r59_bal_sub;
		}

		public void setR59_bal_sub(BigDecimal r59_bal_sub) {
			this.r59_bal_sub = r59_bal_sub;
		}

		public BigDecimal getR59_bal_sub_bwp() {
			return r59_bal_sub_bwp;
		}

		public void setR59_bal_sub_bwp(BigDecimal r59_bal_sub_bwp) {
			this.r59_bal_sub_bwp = r59_bal_sub_bwp;
		}

		public BigDecimal getR59_bal_sub_diaries_bwp() {
			return r59_bal_sub_diaries_bwp;
		}

		public void setR59_bal_sub_diaries_bwp(BigDecimal r59_bal_sub_diaries_bwp) {
			this.r59_bal_sub_diaries_bwp = r59_bal_sub_diaries_bwp;
		}

		public String getR60_operating_expenses() {
			return r60_operating_expenses;
		}

		public void setR60_operating_expenses(String r60_operating_expenses) {
			this.r60_operating_expenses = r60_operating_expenses;
		}

		public BigDecimal getR60_fig_bal_sheet() {
			return r60_fig_bal_sheet;
		}

		public void setR60_fig_bal_sheet(BigDecimal r60_fig_bal_sheet) {
			this.r60_fig_bal_sheet = r60_fig_bal_sheet;
		}

		public BigDecimal getR60_fig_bal_sheet_bwp() {
			return r60_fig_bal_sheet_bwp;
		}

		public void setR60_fig_bal_sheet_bwp(BigDecimal r60_fig_bal_sheet_bwp) {
			this.r60_fig_bal_sheet_bwp = r60_fig_bal_sheet_bwp;
		}

		public BigDecimal getR60_amt_statement_adj() {
			return r60_amt_statement_adj;
		}

		public void setR60_amt_statement_adj(BigDecimal r60_amt_statement_adj) {
			this.r60_amt_statement_adj = r60_amt_statement_adj;
		}

		public BigDecimal getR60_amt_statement_adj_bwp() {
			return r60_amt_statement_adj_bwp;
		}

		public void setR60_amt_statement_adj_bwp(BigDecimal r60_amt_statement_adj_bwp) {
			this.r60_amt_statement_adj_bwp = r60_amt_statement_adj_bwp;
		}

		public BigDecimal getR60_net_amt() {
			return r60_net_amt;
		}

		public void setR60_net_amt(BigDecimal r60_net_amt) {
			this.r60_net_amt = r60_net_amt;
		}

		public BigDecimal getR60_net_amt_bwp() {
			return r60_net_amt_bwp;
		}

		public void setR60_net_amt_bwp(BigDecimal r60_net_amt_bwp) {
			this.r60_net_amt_bwp = r60_net_amt_bwp;
		}

		public BigDecimal getR60_bal_sub() {
			return r60_bal_sub;
		}

		public void setR60_bal_sub(BigDecimal r60_bal_sub) {
			this.r60_bal_sub = r60_bal_sub;
		}

		public BigDecimal getR60_bal_sub_bwp() {
			return r60_bal_sub_bwp;
		}

		public void setR60_bal_sub_bwp(BigDecimal r60_bal_sub_bwp) {
			this.r60_bal_sub_bwp = r60_bal_sub_bwp;
		}

		public BigDecimal getR60_bal_sub_diaries_bwp() {
			return r60_bal_sub_diaries_bwp;
		}

		public void setR60_bal_sub_diaries_bwp(BigDecimal r60_bal_sub_diaries_bwp) {
			this.r60_bal_sub_diaries_bwp = r60_bal_sub_diaries_bwp;
		}

		public String getR61_operating_expenses() {
			return r61_operating_expenses;
		}

		public void setR61_operating_expenses(String r61_operating_expenses) {
			this.r61_operating_expenses = r61_operating_expenses;
		}

		public BigDecimal getR61_fig_bal_sheet() {
			return r61_fig_bal_sheet;
		}

		public void setR61_fig_bal_sheet(BigDecimal r61_fig_bal_sheet) {
			this.r61_fig_bal_sheet = r61_fig_bal_sheet;
		}

		public BigDecimal getR61_fig_bal_sheet_bwp() {
			return r61_fig_bal_sheet_bwp;
		}

		public void setR61_fig_bal_sheet_bwp(BigDecimal r61_fig_bal_sheet_bwp) {
			this.r61_fig_bal_sheet_bwp = r61_fig_bal_sheet_bwp;
		}

		public BigDecimal getR61_amt_statement_adj() {
			return r61_amt_statement_adj;
		}

		public void setR61_amt_statement_adj(BigDecimal r61_amt_statement_adj) {
			this.r61_amt_statement_adj = r61_amt_statement_adj;
		}

		public BigDecimal getR61_amt_statement_adj_bwp() {
			return r61_amt_statement_adj_bwp;
		}

		public void setR61_amt_statement_adj_bwp(BigDecimal r61_amt_statement_adj_bwp) {
			this.r61_amt_statement_adj_bwp = r61_amt_statement_adj_bwp;
		}

		public BigDecimal getR61_net_amt() {
			return r61_net_amt;
		}

		public void setR61_net_amt(BigDecimal r61_net_amt) {
			this.r61_net_amt = r61_net_amt;
		}

		public BigDecimal getR61_net_amt_bwp() {
			return r61_net_amt_bwp;
		}

		public void setR61_net_amt_bwp(BigDecimal r61_net_amt_bwp) {
			this.r61_net_amt_bwp = r61_net_amt_bwp;
		}

		public BigDecimal getR61_bal_sub() {
			return r61_bal_sub;
		}

		public void setR61_bal_sub(BigDecimal r61_bal_sub) {
			this.r61_bal_sub = r61_bal_sub;
		}

		public BigDecimal getR61_bal_sub_bwp() {
			return r61_bal_sub_bwp;
		}

		public void setR61_bal_sub_bwp(BigDecimal r61_bal_sub_bwp) {
			this.r61_bal_sub_bwp = r61_bal_sub_bwp;
		}

		public BigDecimal getR61_bal_sub_diaries_bwp() {
			return r61_bal_sub_diaries_bwp;
		}

		public void setR61_bal_sub_diaries_bwp(BigDecimal r61_bal_sub_diaries_bwp) {
			this.r61_bal_sub_diaries_bwp = r61_bal_sub_diaries_bwp;
		}

		public String getR62_operating_expenses() {
			return r62_operating_expenses;
		}

		public void setR62_operating_expenses(String r62_operating_expenses) {
			this.r62_operating_expenses = r62_operating_expenses;
		}

		public BigDecimal getR62_fig_bal_sheet() {
			return r62_fig_bal_sheet;
		}

		public void setR62_fig_bal_sheet(BigDecimal r62_fig_bal_sheet) {
			this.r62_fig_bal_sheet = r62_fig_bal_sheet;
		}

		public BigDecimal getR62_fig_bal_sheet_bwp() {
			return r62_fig_bal_sheet_bwp;
		}

		public void setR62_fig_bal_sheet_bwp(BigDecimal r62_fig_bal_sheet_bwp) {
			this.r62_fig_bal_sheet_bwp = r62_fig_bal_sheet_bwp;
		}

		public BigDecimal getR62_amt_statement_adj() {
			return r62_amt_statement_adj;
		}

		public void setR62_amt_statement_adj(BigDecimal r62_amt_statement_adj) {
			this.r62_amt_statement_adj = r62_amt_statement_adj;
		}

		public BigDecimal getR62_amt_statement_adj_bwp() {
			return r62_amt_statement_adj_bwp;
		}

		public void setR62_amt_statement_adj_bwp(BigDecimal r62_amt_statement_adj_bwp) {
			this.r62_amt_statement_adj_bwp = r62_amt_statement_adj_bwp;
		}

		public BigDecimal getR62_net_amt() {
			return r62_net_amt;
		}

		public void setR62_net_amt(BigDecimal r62_net_amt) {
			this.r62_net_amt = r62_net_amt;
		}

		public BigDecimal getR62_net_amt_bwp() {
			return r62_net_amt_bwp;
		}

		public void setR62_net_amt_bwp(BigDecimal r62_net_amt_bwp) {
			this.r62_net_amt_bwp = r62_net_amt_bwp;
		}

		public BigDecimal getR62_bal_sub() {
			return r62_bal_sub;
		}

		public void setR62_bal_sub(BigDecimal r62_bal_sub) {
			this.r62_bal_sub = r62_bal_sub;
		}

		public BigDecimal getR62_bal_sub_bwp() {
			return r62_bal_sub_bwp;
		}

		public void setR62_bal_sub_bwp(BigDecimal r62_bal_sub_bwp) {
			this.r62_bal_sub_bwp = r62_bal_sub_bwp;
		}

		public BigDecimal getR62_bal_sub_diaries_bwp() {
			return r62_bal_sub_diaries_bwp;
		}

		public void setR62_bal_sub_diaries_bwp(BigDecimal r62_bal_sub_diaries_bwp) {
			this.r62_bal_sub_diaries_bwp = r62_bal_sub_diaries_bwp;
		}

		public String getR63_operating_expenses() {
			return r63_operating_expenses;
		}

		public void setR63_operating_expenses(String r63_operating_expenses) {
			this.r63_operating_expenses = r63_operating_expenses;
		}

		public BigDecimal getR63_fig_bal_sheet() {
			return r63_fig_bal_sheet;
		}

		public void setR63_fig_bal_sheet(BigDecimal r63_fig_bal_sheet) {
			this.r63_fig_bal_sheet = r63_fig_bal_sheet;
		}

		public BigDecimal getR63_fig_bal_sheet_bwp() {
			return r63_fig_bal_sheet_bwp;
		}

		public void setR63_fig_bal_sheet_bwp(BigDecimal r63_fig_bal_sheet_bwp) {
			this.r63_fig_bal_sheet_bwp = r63_fig_bal_sheet_bwp;
		}

		public BigDecimal getR63_amt_statement_adj() {
			return r63_amt_statement_adj;
		}

		public void setR63_amt_statement_adj(BigDecimal r63_amt_statement_adj) {
			this.r63_amt_statement_adj = r63_amt_statement_adj;
		}

		public BigDecimal getR63_amt_statement_adj_bwp() {
			return r63_amt_statement_adj_bwp;
		}

		public void setR63_amt_statement_adj_bwp(BigDecimal r63_amt_statement_adj_bwp) {
			this.r63_amt_statement_adj_bwp = r63_amt_statement_adj_bwp;
		}

		public BigDecimal getR63_net_amt() {
			return r63_net_amt;
		}

		public void setR63_net_amt(BigDecimal r63_net_amt) {
			this.r63_net_amt = r63_net_amt;
		}

		public BigDecimal getR63_net_amt_bwp() {
			return r63_net_amt_bwp;
		}

		public void setR63_net_amt_bwp(BigDecimal r63_net_amt_bwp) {
			this.r63_net_amt_bwp = r63_net_amt_bwp;
		}

		public BigDecimal getR63_bal_sub() {
			return r63_bal_sub;
		}

		public void setR63_bal_sub(BigDecimal r63_bal_sub) {
			this.r63_bal_sub = r63_bal_sub;
		}

		public BigDecimal getR63_bal_sub_bwp() {
			return r63_bal_sub_bwp;
		}

		public void setR63_bal_sub_bwp(BigDecimal r63_bal_sub_bwp) {
			this.r63_bal_sub_bwp = r63_bal_sub_bwp;
		}

		public BigDecimal getR63_bal_sub_diaries_bwp() {
			return r63_bal_sub_diaries_bwp;
		}

		public void setR63_bal_sub_diaries_bwp(BigDecimal r63_bal_sub_diaries_bwp) {
			this.r63_bal_sub_diaries_bwp = r63_bal_sub_diaries_bwp;
		}

		public BigDecimal getR17_bal_sub_diaries() {
			return r17_bal_sub_diaries;
		}

		public void setR17_bal_sub_diaries(BigDecimal r17_bal_sub_diaries) {
			this.r17_bal_sub_diaries = r17_bal_sub_diaries;
		}

		public BigDecimal getR48_bal_sub_diaries() {
			return r48_bal_sub_diaries;
		}

		public void setR48_bal_sub_diaries(BigDecimal r48_bal_sub_diaries) {
			this.r48_bal_sub_diaries = r48_bal_sub_diaries;
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

	public static class PL_SCHS_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public PL_SCHS_PK() {
		}

		public PL_SCHS_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof PL_SCHS_PK))
				return false;
			PL_SCHS_PK that = (PL_SCHS_PK) o;
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

	public class PL_SCHS_Detail_Entity {
		private Long sno;
		@Column(name = "CUST_ID")
		private String custId;

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
		private String reportAddlCriteria1;

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

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

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

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
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

		public BigDecimal getAverage() {
			return average;
		}

		public void setAverage(BigDecimal average) {
			this.average = average;
		}
	}

	class PL_SCHSDetailRowMapper implements RowMapper<PL_SCHS_Detail_Entity> {

		@Override
		public PL_SCHS_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			PL_SCHS_Detail_Entity obj = new PL_SCHS_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
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
			obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');

			obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');

			obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

			return obj;
		}
	}

	class PL_SCHSArchivalDetailRowMapper implements RowMapper<PL_SCHS_Archival_Detail_Entity> {

		@Override
		public PL_SCHS_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			PL_SCHS_Archival_Detail_Entity obj = new PL_SCHS_Archival_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
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
			obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');

			obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');

			obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

			return obj;
		}
	}

	public class PL_SCHS_Archival_Detail_Entity {
		private Long sno;
		@Column(name = "CUST_ID")
		private String custId;

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
		private String reportAddlCriteria1;

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

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

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

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
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

		public BigDecimal getAverage() {
			return average;
		}

		public void setAverage(BigDecimal average) {
			this.average = average;
		}
	}

	// MODEL AND VIEW METHOD summary

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getPL_SCHSView(

			String reportId, String fromdate, String todate, String currency, String dtltype, Pageable pageable,
			String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("PL_SCHS View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL MODE

		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

			List<PL_SCHS_Archival_Summary_Entity> T1Master = new ArrayList<>();

			try {
				Date dt = dateformat.parse(todate);
				// SUMMARY ARCHIVAL
				T1Master = getdatabydateListarchival(dt, version);
				System.out.println("Archival Summary size = " + T1Master.size());

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
			List<PL_SCHS_Summary_Entity> T1Master = new ArrayList<>();
			try {
				Date dt = dateformat.parse(todate);

				// SUMMARY NORMAL
				T1Master = getDataByDate(dt);

				System.out.println("Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		}

		// VIEW SETTINGS

		mv.setViewName("BRRS/PL_SCHS");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getPL_SCHScurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

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

			// ARCHIVAL MODE

			// ARCHIVAL / RESUB MODE
			if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

				System.out.println(type + " DETAIL MODE");

				List<PL_SCHS_Archival_Detail_Entity> detailList;

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

				List<PL_SCHS_Detail_Entity> currentDetailList;

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

		mv.setViewName("BRRS/PL_SCHS");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getPL_SCHSArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<PL_SCHS_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (PL_SCHS_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				PL_SCHS_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  PL_SCHS  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		ModelAndView mv = new ModelAndView("BRRS/PL_SCHS");

		System.out.println("sno is : " + SNO);
		System.out.println("Type: " + type);
		if (SNO != null) {
			if (type == "RESUB" || type.equals("RESUB")) {
				System.out.println("Inside RESUB FETCH");
				PL_SCHS_Detail_Entity PL_SCHSEntity = findBySnoArch(SNO);
				if (PL_SCHSEntity != null && PL_SCHSEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(PL_SCHSEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("PL_SCHSData", PL_SCHSEntity);
			} else {
				PL_SCHS_Detail_Entity PL_SCHSEntity = findBySno(SNO);
				if (PL_SCHSEntity != null && PL_SCHSEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(PL_SCHSEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("PL_SCHSData", PL_SCHSEntity);
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

			String acctBalanceInpula = request.getParameter("acctBalanceInpula");

			String averageStr = request.getParameter("average");

			String acctName = request.getParameter("acctName");

			String reportDateStr = request.getParameter("reportDate");

			System.out.println("Sno is : " + Sno);
			String type = request.getParameter("type");
			String entry = (request.getParameter("entry") != null) ? request.getParameter("entry") : "YES";

			// Load Existing Record
			PL_SCHS_Detail_Entity existing = null;

			System.out.println("type is : " + type);
			if ((type == "RESUB") || (type.equals("RESUB"))) {
				existing = findBySnoArch(Sno);
			} else {
				existing = findBySno(Sno);
			}
			PL_SCHS_Detail_Entity oldcopy = new PL_SCHS_Detail_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

			if (existing == null) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			// Update Name
			if (acctName != null && !acctName.isEmpty()) {

				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {

					existing.setAcctName(acctName);

					isChanged = true;
				}
			}

			// Update Balance
			if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {

				BigDecimal newBalance = new BigDecimal(acctBalanceInpula);

				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newBalance) != 0) {

					existing.setAcctBalanceInpula(newBalance);

					isChanged = true;
				}
			}
// AVERAGE
			if (averageStr != null && !averageStr.isEmpty()) {

				BigDecimal newAverage = new BigDecimal(averageStr);

				if (existing.getAverage() == null || existing.getAverage().compareTo(newAverage) != 0) {

					existing.setAverage(newAverage);

					isChanged = true;
				}
			}
			// Save using JDBC
			if (isChanged) {
				String sql;
				System.out.println("Type in update block : " + type);
				if (type == "RESUB" || type.equals("RESUB")) {
					System.out.println("Inside RESUB UPDATE");
					sql = "UPDATE BRRS_PL_SCHS_ARCHIVALTABLE_DETAIL " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ?, " + // ✅ comma added
							"AVERAGE = ? " + // ✅ proper concatenation
							"WHERE SNO = ?";
				} else {
					sql = "UPDATE BRRS_PL_SCHS_DETAILTABLE " + "SET ACCT_NAME = ?, " + "ACCT_BALANCE_IN_PULA = ?, " + // ✅
																														// comma
																														// added
							"AVERAGE = ? " + // ✅ proper concatenation
							"WHERE SNO = ?";
				}
				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInpula(), existing.getAverage(),
						Sno);
				if ((type == "RESUB") || (type.equals("RESUB"))) {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "Common Disclosure Archival Screen",
							"BRRS_PL_SCHS_ARCHIVALTABLE_DETAIL");
				} else {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "Common Disclosure Screen",
							"BRRS_PL_SCHS_DETAILTABLE");
				}
				System.out.println("Record updated using JDBC");

				Run_PL_SCHS_Procudure(reportDateStr, type, entry);

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
			Run_PL_SCHS_Procudure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());

		}
	}

	private void Run_PL_SCHS_Procudure(String reportDateStr, String type, String entry) {

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
						// 1. Fixed date conversion here
						String bdsql = "DELETE FROM BRRS_PL_SCHS_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MM-YYYY')";
						int rowsDeleted = jdbcTemplate.update(bdsql, formattedDate);
						System.out.println("Successfully deleted before executing procedure " + rowsDeleted + " rows.");

						// 2. Fixed date conversion here
						String sqltransfer = "INSERT INTO BRRS_PL_SCHS_DETAILTABLE "
								+ " (SNO, ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA, AVERAGE, REPORT_LABEL, REPORT_ADDL_CRITERIA_1, REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION) "
								+ "SELECT SNO,  ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA, AVERAGE, REPORT_LABEL, REPORT_ADDL_CRITERIA_1, REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION "
								+ "FROM BRRS_PL_SCHS_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = TO_DATE(?, 'DD-MM-YYYY')";
						int rowsInserted = jdbcTemplate.update(sqltransfer, formattedDate);
						System.out.println("Successfully transferred " + rowsInserted + " rows.");
					}

					if (shouldExecuteProcedure) {
						jdbcTemplate.update("BEGIN BRRS_PL_SCHS_SUMMARY_PROCEDURE(?); END;", formattedDate);
						System.out.println("Procedure executed");
					}

					if (isResubNoEntry) {
						// 3. Fixed date conversion here
						String adsql = "DELETE FROM BRRS_PL_SCHS_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MM-YYYY')";
						int rowsDeleted = jdbcTemplate.update(adsql, formattedDate);
						System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

						// 4. Fixed date conversion here
						String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_PL_SCHS_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = TO_DATE(?, 'DD-MM-YYYY')";
						Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, formattedDate);
						int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

						String finalsql = "INSERT INTO BRRS_PL_SCHS_ARCHIVALTABLE_SUMMARY ("
								+ "R9_INTREST_DIV, R9_FIG_BAL_SHEET, R9_FIG_BAL_SHEET_BWP, R9_AMT_STATEMENT_ADJ, R9_AMT_STATEMENT_ADJ_BWP, R9_NET_AMT, R9_NET_AMT_BWP, R9_BAL_SUB, R9_BAL_SUB_BWP, R9_BAL_SUB_DIARIES, R9_BAL_SUB_DIARIES_BWP, "
								+ "R10_INTREST_DIV, R10_FIG_BAL_SHEET, R10_FIG_BAL_SHEET_BWP, R10_AMT_STATEMENT_ADJ, R10_AMT_STATEMENT_ADJ_BWP, R10_NET_AMT, R10_NET_AMT_BWP, R10_BAL_SUB, R10_BAL_SUB_BWP, R10_BAL_SUB_DIARIES, R10_BAL_SUB_DIARIES_BWP, "
								+ "R11_INTREST_DIV, R11_FIG_BAL_SHEET, R11_FIG_BAL_SHEET_BWP, R11_AMT_STATEMENT_ADJ, R11_AMT_STATEMENT_ADJ_BWP, R11_NET_AMT, R11_NET_AMT_BWP, R11_BAL_SUB, R11_BAL_SUB_BWP, R11_BAL_SUB_DIARIES, R11_BAL_SUB_DIARIES_BWP, "
								+ "R12_INTREST_DIV, R12_FIG_BAL_SHEET, R12_FIG_BAL_SHEET_BWP, R12_AMT_STATEMENT_ADJ, R12_AMT_STATEMENT_ADJ_BWP, R12_NET_AMT, R12_NET_AMT_BWP, R12_BAL_SUB, R12_BAL_SUB_BWP, R12_BAL_SUB_DIARIES, R12_BAL_SUB_DIARIES_BWP, "
								+ "R13_INTREST_DIV, R13_FIG_BAL_SHEET, R13_FIG_BAL_SHEET_BWP, R13_AMT_STATEMENT_ADJ, R13_AMT_STATEMENT_ADJ_BWP, R13_NET_AMT, R13_NET_AMT_BWP, R13_BAL_SUB, R13_BAL_SUB_BWP, R13_BAL_SUB_DIARIES, R13_BAL_SUB_DIARIES_BWP, "
								+ "R17_OTHER_INCOME, R17_FIG_BAL_SHEET, R17_FIG_BAL_SHEET_BWP, R17_AMT_STATEMENT_ADJ, R17_AMT_STATEMENT_ADJ_BWP, R17_NET_AMT, R17_NET_AMT_BWP, R17_BAL_SUB, R17_BAL_SUB_BWP, R17_BAL_SUB_DIARIES, R17_BAL_SUB_DIARIES_BWP, "
								+ "R18_OTHER_INCOME, R18_FIG_BAL_SHEET, R18_FIG_BAL_SHEET_BWP, R18_AMT_STATEMENT_ADJ, R18_AMT_STATEMENT_ADJ_BWP, R18_NET_AMT, R18_NET_AMT_BWP, R18_BAL_SUB, R18_BAL_SUB_BWP, R18_BAL_SUB_DIARIES, R18_BAL_SUB_DIARIES_BWP, "
								+ "R19_OTHER_INCOME, R19_FIG_BAL_SHEET, R19_FIG_BAL_SHEET_BWP, R19_AMT_STATEMENT_ADJ, R19_AMT_STATEMENT_ADJ_BWP, R19_NET_AMT, R19_NET_AMT_BWP, R19_BAL_SUB, R19_BAL_SUB_BWP, R19_BAL_SUB_DIARIES, R19_BAL_SUB_DIARIES_BWP, "
								+ "R20_OTHER_INCOME, R20_FIG_BAL_SHEET, R20_FIG_BAL_SHEET_BWP, R20_AMT_STATEMENT_ADJ, R20_AMT_STATEMENT_ADJ_BWP, R20_NET_AMT, R20_NET_AMT_BWP, R20_BAL_SUB, R20_BAL_SUB_BWP, R20_BAL_SUB_DIARIES, R20_BAL_SUB_DIARIES_BWP, "
								+ "R21_OTHER_INCOME, R21_FIG_BAL_SHEET, R21_FIG_BAL_SHEET_BWP, R21_AMT_STATEMENT_ADJ, R21_AMT_STATEMENT_ADJ_BWP, R21_NET_AMT, R21_NET_AMT_BWP, R21_BAL_SUB, R21_BAL_SUB_BWP, R21_BAL_SUB_DIARIES, R21_BAL_SUB_DIARIES_BWP, "
								+ "R22_OTHER_INCOME, R22_FIG_BAL_SHEET, R22_FIG_BAL_SHEET_BWP, R22_AMT_STATEMENT_ADJ, R22_AMT_STATEMENT_ADJ_BWP, R22_NET_AMT, R22_NET_AMT_BWP, R22_BAL_SUB, R22_BAL_SUB_BWP, R22_BAL_SUB_DIARIES, R22_BAL_SUB_DIARIES_BWP, "
								+ "R23_OTHER_INCOME, R23_FIG_BAL_SHEET, R23_FIG_BAL_SHEET_BWP, R23_AMT_STATEMENT_ADJ, R23_AMT_STATEMENT_ADJ_BWP, R23_NET_AMT, R23_NET_AMT_BWP, R23_BAL_SUB, R23_BAL_SUB_BWP, R23_BAL_SUB_DIARIES, R23_BAL_SUB_DIARIES_BWP, "
								+ "R24_OTHER_INCOME, R24_FIG_BAL_SHEET, R24_FIG_BAL_SHEET_BWP, R24_AMT_STATEMENT_ADJ, R24_AMT_STATEMENT_ADJ_BWP, R24_NET_AMT, R24_NET_AMT_BWP, R24_BAL_SUB, R24_BAL_SUB_BWP, R24_BAL_SUB_DIARIES, R24_BAL_SUB_DIARIES_BWP, "
								+ "R25_OTHER_INCOME, R25_FIG_BAL_SHEET, R25_FIG_BAL_SHEET_BWP, R25_AMT_STATEMENT_ADJ, R25_AMT_STATEMENT_ADJ_BWP, R25_NET_AMT, R25_NET_AMT_BWP, R25_BAL_SUB, R25_BAL_SUB_BWP, R25_BAL_SUB_DIARIES, R25_BAL_SUB_DIARIES_BWP, "
								+ "R26_OTHER_INCOME, R26_FIG_BAL_SHEET, R26_FIG_BAL_SHEET_BWP, R26_AMT_STATEMENT_ADJ, R26_AMT_STATEMENT_ADJ_BWP, R26_NET_AMT, R26_NET_AMT_BWP, R26_BAL_SUB, R26_BAL_SUB_BWP, R26_BAL_SUB_DIARIES, R26_BAL_SUB_DIARIES_BWP, "
								+ "R27_OTHER_INCOME, R27_FIG_BAL_SHEET, R27_FIG_BAL_SHEET_BWP, R27_AMT_STATEMENT_ADJ, R27_AMT_STATEMENT_ADJ_BWP, R27_NET_AMT, R27_NET_AMT_BWP, R27_BAL_SUB, R27_BAL_SUB_BWP, R27_BAL_SUB_DIARIES, R27_BAL_SUB_DIARIES_BWP, "
								+ "R28_OTHER_INCOME, R28_FIG_BAL_SHEET, R28_FIG_BAL_SHEET_BWP, R28_AMT_STATEMENT_ADJ, R28_AMT_STATEMENT_ADJ_BWP, R28_NET_AMT, R28_NET_AMT_BWP, R28_BAL_SUB, R28_BAL_SUB_BWP, R28_BAL_SUB_DIARIES, R28_BAL_SUB_DIARIES_BWP, "
								+ "R29_OTHER_INCOME, R29_FIG_BAL_SHEET, R29_FIG_BAL_SHEET_BWP, R29_AMT_STATEMENT_ADJ, R29_AMT_STATEMENT_ADJ_BWP, R29_NET_AMT, R29_NET_AMT_BWP, R29_BAL_SUB, R29_BAL_SUB_BWP, R29_BAL_SUB_DIARIES, R29_BAL_SUB_DIARIES_BWP, "
								+ "R30_OTHER_INCOME, R30_FIG_BAL_SHEET, R30_FIG_BAL_SHEET_BWP, R30_AMT_STATEMENT_ADJ, R30_AMT_STATEMENT_ADJ_BWP, R30_NET_AMT, R30_NET_AMT_BWP, R30_BAL_SUB, R30_BAL_SUB_BWP, R30_BAL_SUB_DIARIES, R30_BAL_SUB_DIARIES_BWP, "
								+ "R31_OTHER_INCOME, R31_FIG_BAL_SHEET, R31_FIG_BAL_SHEET_BWP, R31_AMT_STATEMENT_ADJ, R31_AMT_STATEMENT_ADJ_BWP, R31_NET_AMT, R31_NET_AMT_BWP, R31_BAL_SUB, R31_BAL_SUB_BWP, R31_BAL_SUB_DIARIES, R31_BAL_SUB_DIARIES_BWP, "
								+ "R40_INTREST_EXPENDED, R40_FIG_BAL_SHEET, R40_FIG_BAL_SHEET_BWP, R40_AMT_STATEMENT_ADJ, R40_AMT_STATEMENT_ADJ_BWP, R40_NET_AMT, R40_NET_AMT_BWP, R40_BAL_SUB, R40_BAL_SUB_BWP, R40_BAL_SUB_DIARIES, R40_BAL_SUB_DIARIES_BWP, "
								+ "R41_INTREST_EXPENDED, R41_FIG_BAL_SHEET, R41_FIG_BAL_SHEET_BWP, R41_AMT_STATEMENT_ADJ, R41_AMT_STATEMENT_ADJ_BWP, R41_NET_AMT, R41_NET_AMT_BWP, R41_BAL_SUB, R41_BAL_SUB_BWP, R41_BAL_SUB_DIARIES, R41_BAL_SUB_DIARIES_BWP, "
								+ "R42_INTREST_EXPENDED, R42_FIG_BAL_SHEET, R42_FIG_BAL_SHEET_BWP, R42_AMT_STATEMENT_ADJ, R42_AMT_STATEMENT_ADJ_BWP, R42_NET_AMT, R42_NET_AMT_BWP, R42_BAL_SUB, R42_BAL_SUB_BWP, R42_BAL_SUB_DIARIES, R42_BAL_SUB_DIARIES_BWP, "
								+ "R43_INTREST_EXPENDED, R43_FIG_BAL_SHEET, R43_FIG_BAL_SHEET_BWP, R43_AMT_STATEMENT_ADJ, R43_AMT_STATEMENT_ADJ_BWP, R43_NET_AMT, R43_NET_AMT_BWP, R43_BAL_SUB, R43_BAL_SUB_BWP, R43_BAL_SUB_DIARIES, R43_BAL_SUB_DIARIES_BWP, "
								+ "R48_OPERATING_EXPENSES, R48_FIG_BAL_SHEET, R48_FIG_BAL_SHEET_BWP, R48_AMT_STATEMENT_ADJ, R48_AMT_STATEMENT_ADJ_BWP, R48_NET_AMT, R48_NET_AMT_BWP, R48_BAL_SUB, R48_BAL_SUB_BWP, R48_BAL_SUB_DIARIES, R48_BAL_SUB_DIARIES_BWP, "
								+ "R49_OPERATING_EXPENSES, R49_FIG_BAL_SHEET, R49_FIG_BAL_SHEET_BWP, R49_AMT_STATEMENT_ADJ, R49_AMT_STATEMENT_ADJ_BWP, R49_NET_AMT, R49_NET_AMT_BWP, R49_BAL_SUB, R49_BAL_SUB_BWP, R49_BAL_SUB_DIARIES, R49_BAL_SUB_DIARIES_BWP, "
								+ "R50_OPERATING_EXPENSES, R50_FIG_BAL_SHEET, R50_FIG_BAL_SHEET_BWP, R50_AMT_STATEMENT_ADJ, R50_AMT_STATEMENT_ADJ_BWP, R50_NET_AMT, R50_NET_AMT_BWP, R50_BAL_SUB, R50_BAL_SUB_BWP, R50_BAL_SUB_DIARIES, R50_BAL_SUB_DIARIES_BWP, "
								+ "R51_OPERATING_EXPENSES, R51_FIG_BAL_SHEET, R51_FIG_BAL_SHEET_BWP, R51_AMT_STATEMENT_ADJ, R51_AMT_STATEMENT_ADJ_BWP, R51_NET_AMT, R51_NET_AMT_BWP, R51_BAL_SUB, R51_BAL_SUB_BWP, R51_BAL_SUB_DIARIES, R51_BAL_SUB_DIARIES_BWP, "
								+ "R52_OPERATING_EXPENSES, R52_FIG_BAL_SHEET, R52_FIG_BAL_SHEET_BWP, R52_AMT_STATEMENT_ADJ, R52_AMT_STATEMENT_ADJ_BWP, R52_NET_AMT, R52_NET_AMT_BWP, R52_BAL_SUB, R52_BAL_SUB_BWP, R52_BAL_SUB_DIARIES, R52_BAL_SUB_DIARIES_BWP, "
								+ "R53_OPERATING_EXPENSES, R53_FIG_BAL_SHEET, R53_FIG_BAL_SHEET_BWP, R53_AMT_STATEMENT_ADJ, R53_AMT_STATEMENT_ADJ_BWP, R53_NET_AMT, R53_NET_AMT_BWP, R53_BAL_SUB, R53_BAL_SUB_BWP, R53_BAL_SUB_DIARIES, R53_BAL_SUB_DIARIES_BWP, "
								+ "R54_OPERATING_EXPENSES, R54_FIG_BAL_SHEET, R54_FIG_BAL_SHEET_BWP, R54_AMT_STATEMENT_ADJ, R54_AMT_STATEMENT_ADJ_BWP, R54_NET_AMT, R54_NET_AMT_BWP, R54_BAL_SUB, R54_BAL_SUB_BWP, R54_BAL_SUB_DIARIES, R54_BAL_SUB_DIARIES_BWP, "
								+ "R55_OPERATING_EXPENSES, R55_FIG_BAL_SHEET, R55_FIG_BAL_SHEET_BWP, R55_AMT_STATEMENT_ADJ, R55_AMT_STATEMENT_ADJ_BWP, R55_NET_AMT, R55_NET_AMT_BWP, R55_BAL_SUB, R55_BAL_SUB_BWP, R55_BAL_SUB_DIARIES, R55_BAL_SUB_DIARIES_BWP, "
								+ "R56_OPERATING_EXPENSES, R56_FIG_BAL_SHEET, R56_FIG_BAL_SHEET_BWP, R56_AMT_STATEMENT_ADJ, R56_AMT_STATEMENT_ADJ_BWP, R56_NET_AMT, R56_NET_AMT_BWP, R56_BAL_SUB, R56_BAL_SUB_BWP, R56_BAL_SUB_DIARIES, R56_BAL_SUB_DIARIES_BWP, "
								+ "R57_OPERATING_EXPENSES, R57_FIG_BAL_SHEET, R57_FIG_BAL_SHEET_BWP, R57_AMT_STATEMENT_ADJ, R57_AMT_STATEMENT_ADJ_BWP, R57_NET_AMT, R57_NET_AMT_BWP, R57_BAL_SUB, R57_BAL_SUB_BWP, R57_BAL_SUB_DIARIES, R57_BAL_SUB_DIARIES_BWP, "
								+ "R58_OPERATING_EXPENSES, R58_FIG_BAL_SHEET, R58_FIG_BAL_SHEET_BWP, R58_AMT_STATEMENT_ADJ, R58_AMT_STATEMENT_ADJ_BWP, R58_NET_AMT, R58_NET_AMT_BWP, R58_BAL_SUB, R58_BAL_SUB_BWP, R58_BAL_SUB_DIARIES, R58_BAL_SUB_DIARIES_BWP, "
								+ "R59_OPERATING_EXPENSES, R59_FIG_BAL_SHEET, R59_FIG_BAL_SHEET_BWP, R59_AMT_STATEMENT_ADJ, R59_AMT_STATEMENT_ADJ_BWP, R59_NET_AMT, R59_NET_AMT_BWP, R59_BAL_SUB, R59_BAL_SUB_BWP, R59_BAL_SUB_DIARIES, R59_BAL_SUB_DIARIES_BWP, "
								+ "R60_OPERATING_EXPENSES, R60_FIG_BAL_SHEET, R60_FIG_BAL_SHEET_BWP, R60_AMT_STATEMENT_ADJ, R60_AMT_STATEMENT_ADJ_BWP, R60_NET_AMT, R60_NET_AMT_BWP, R60_BAL_SUB, R60_BAL_SUB_BWP, R60_BAL_SUB_DIARIES, R60_BAL_SUB_DIARIES_BWP, "
								+ "R61_OPERATING_EXPENSES, R61_FIG_BAL_SHEET, R61_FIG_BAL_SHEET_BWP, R61_AMT_STATEMENT_ADJ, R61_AMT_STATEMENT_ADJ_BWP, R61_NET_AMT, R61_NET_AMT_BWP, R61_BAL_SUB, R61_BAL_SUB_BWP, R61_BAL_SUB_DIARIES, R61_BAL_SUB_DIARIES_BWP, "
								+ "R62_OPERATING_EXPENSES, R62_FIG_BAL_SHEET, R62_FIG_BAL_SHEET_BWP, R62_AMT_STATEMENT_ADJ, R62_AMT_STATEMENT_ADJ_BWP, R62_NET_AMT, R62_NET_AMT_BWP, R62_BAL_SUB, R62_BAL_SUB_BWP, R62_BAL_SUB_DIARIES, R62_BAL_SUB_DIARIES_BWP, "
								+ "R63_OPERATING_EXPENSES, R63_FIG_BAL_SHEET, R63_FIG_BAL_SHEET_BWP, R63_AMT_STATEMENT_ADJ, R63_AMT_STATEMENT_ADJ_BWP, R63_NET_AMT, R63_NET_AMT_BWP, R63_BAL_SUB, R63_BAL_SUB_BWP, R63_BAL_SUB_DIARIES, R63_BAL_SUB_DIARIES_BWP, "
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
								+ "SELECT "
								+ "R9_INTREST_DIV, R9_FIG_BAL_SHEET, R9_FIG_BAL_SHEET_BWP, R9_AMT_STATEMENT_ADJ, R9_AMT_STATEMENT_ADJ_BWP, R9_NET_AMT, R9_NET_AMT_BWP, R9_BAL_SUB, R9_BAL_SUB_BWP, R9_BAL_SUB_DIARIES, R9_BAL_SUB_DIARIES_BWP, "
								+ "R10_INTREST_DIV, R10_FIG_BAL_SHEET, R10_FIG_BAL_SHEET_BWP, R10_AMT_STATEMENT_ADJ, R10_AMT_STATEMENT_ADJ_BWP, R10_NET_AMT, R10_NET_AMT_BWP, R10_BAL_SUB, R10_BAL_SUB_BWP, R10_BAL_SUB_DIARIES, R10_BAL_SUB_DIARIES_BWP, "
								+ "R11_INTREST_DIV, R11_FIG_BAL_SHEET, R11_FIG_BAL_SHEET_BWP, R11_AMT_STATEMENT_ADJ, R11_AMT_STATEMENT_ADJ_BWP, R11_NET_AMT, R11_NET_AMT_BWP, R11_BAL_SUB, R11_BAL_SUB_BWP, R11_BAL_SUB_DIARIES, R11_BAL_SUB_DIARIES_BWP, "
								+ "R12_INTREST_DIV, R12_FIG_BAL_SHEET, R12_FIG_BAL_SHEET_BWP, R12_AMT_STATEMENT_ADJ, R12_AMT_STATEMENT_ADJ_BWP, R12_NET_AMT, R12_NET_AMT_BWP, R12_BAL_SUB, R12_BAL_SUB_BWP, R12_BAL_SUB_DIARIES, R12_BAL_SUB_DIARIES_BWP, "
								+ "R13_INTREST_DIV, R13_FIG_BAL_SHEET, R13_FIG_BAL_SHEET_BWP, R13_AMT_STATEMENT_ADJ, R13_AMT_STATEMENT_ADJ_BWP, R13_NET_AMT, R13_NET_AMT_BWP, R13_BAL_SUB, R13_BAL_SUB_BWP, R13_BAL_SUB_DIARIES, R13_BAL_SUB_DIARIES_BWP, "
								+ "R17_OTHER_INCOME, R17_FIG_BAL_SHEET, R17_FIG_BAL_SHEET_BWP, R17_AMT_STATEMENT_ADJ, R17_AMT_STATEMENT_ADJ_BWP, R17_NET_AMT, R17_NET_AMT_BWP, R17_BAL_SUB, R17_BAL_SUB_BWP, R17_BAL_SUB_DIARIES, R17_BAL_SUB_DIARIES_BWP, "
								+ "R18_OTHER_INCOME, R18_FIG_BAL_SHEET, R18_FIG_BAL_SHEET_BWP, R18_AMT_STATEMENT_ADJ, R18_AMT_STATEMENT_ADJ_BWP, R18_NET_AMT, R18_NET_AMT_BWP, R18_BAL_SUB, R18_BAL_SUB_BWP, R18_BAL_SUB_DIARIES, R18_BAL_SUB_DIARIES_BWP, "
								+ "R19_OTHER_INCOME, R19_FIG_BAL_SHEET, R19_FIG_BAL_SHEET_BWP, R19_AMT_STATEMENT_ADJ, R19_AMT_STATEMENT_ADJ_BWP, R19_NET_AMT, R19_NET_AMT_BWP, R19_BAL_SUB, R19_BAL_SUB_BWP, R19_BAL_SUB_DIARIES, R19_BAL_SUB_DIARIES_BWP, "
								+ "R20_OTHER_INCOME, R20_FIG_BAL_SHEET, R20_FIG_BAL_SHEET_BWP, R20_AMT_STATEMENT_ADJ, R20_AMT_STATEMENT_ADJ_BWP, R20_NET_AMT, R20_NET_AMT_BWP, R20_BAL_SUB, R20_BAL_SUB_BWP, R20_BAL_SUB_DIARIES, R20_BAL_SUB_DIARIES_BWP, "
								+ "R21_OTHER_INCOME, R21_FIG_BAL_SHEET, R21_FIG_BAL_SHEET_BWP, R21_AMT_STATEMENT_ADJ, R21_AMT_STATEMENT_ADJ_BWP, R21_NET_AMT, R21_NET_AMT_BWP, R21_BAL_SUB, R21_BAL_SUB_BWP, R21_BAL_SUB_DIARIES, R21_BAL_SUB_DIARIES_BWP, "
								+ "R22_OTHER_INCOME, R22_FIG_BAL_SHEET, R22_FIG_BAL_SHEET_BWP, R22_AMT_STATEMENT_ADJ, R22_AMT_STATEMENT_ADJ_BWP, R22_NET_AMT, R22_NET_AMT_BWP, R22_BAL_SUB, R22_BAL_SUB_BWP, R22_BAL_SUB_DIARIES, R22_BAL_SUB_DIARIES_BWP, "
								+ "R23_OTHER_INCOME, R23_FIG_BAL_SHEET, R23_FIG_BAL_SHEET_BWP, R23_AMT_STATEMENT_ADJ, R23_AMT_STATEMENT_ADJ_BWP, R23_NET_AMT, R23_NET_AMT_BWP, R23_BAL_SUB, R23_BAL_SUB_BWP, R23_BAL_SUB_DIARIES, R23_BAL_SUB_DIARIES_BWP, "
								+ "R24_OTHER_INCOME, R24_FIG_BAL_SHEET, R24_FIG_BAL_SHEET_BWP, R24_AMT_STATEMENT_ADJ, R24_AMT_STATEMENT_ADJ_BWP, R24_NET_AMT, R24_NET_AMT_BWP, R24_BAL_SUB, R24_BAL_SUB_BWP, R24_BAL_SUB_DIARIES, R24_BAL_SUB_DIARIES_BWP, "
								+ "R25_OTHER_INCOME, R25_FIG_BAL_SHEET, R25_FIG_BAL_SHEET_BWP, R25_AMT_STATEMENT_ADJ, R25_AMT_STATEMENT_ADJ_BWP, R25_NET_AMT, R25_NET_AMT_BWP, R25_BAL_SUB, R25_BAL_SUB_BWP, R25_BAL_SUB_DIARIES, R25_BAL_SUB_DIARIES_BWP, "
								+ "R26_OTHER_INCOME, R26_FIG_BAL_SHEET, R26_FIG_BAL_SHEET_BWP, R26_AMT_STATEMENT_ADJ, R26_AMT_STATEMENT_ADJ_BWP, R26_NET_AMT, R26_NET_AMT_BWP, R26_BAL_SUB, R26_BAL_SUB_BWP, R26_BAL_SUB_DIARIES, R26_BAL_SUB_DIARIES_BWP, "
								+ "R27_OTHER_INCOME, R27_FIG_BAL_SHEET, R27_FIG_BAL_SHEET_BWP, R27_AMT_STATEMENT_ADJ, R27_AMT_STATEMENT_ADJ_BWP, R27_NET_AMT, R27_NET_AMT_BWP, R27_BAL_SUB, R27_BAL_SUB_BWP, R27_BAL_SUB_DIARIES, R27_BAL_SUB_DIARIES_BWP, "
								+ "R28_OTHER_INCOME, R28_FIG_BAL_SHEET, R28_FIG_BAL_SHEET_BWP, R28_AMT_STATEMENT_ADJ, R28_AMT_STATEMENT_ADJ_BWP, R28_NET_AMT, R28_NET_AMT_BWP, R28_BAL_SUB, R28_BAL_SUB_BWP, R28_BAL_SUB_DIARIES, R28_BAL_SUB_DIARIES_BWP, "
								+ "R29_OTHER_INCOME, R29_FIG_BAL_SHEET, R29_FIG_BAL_SHEET_BWP, R29_AMT_STATEMENT_ADJ, R29_AMT_STATEMENT_ADJ_BWP, R29_NET_AMT, R29_NET_AMT_BWP, R29_BAL_SUB, R29_BAL_SUB_BWP, R29_BAL_SUB_DIARIES, R29_BAL_SUB_DIARIES_BWP, "
								+ "R30_OTHER_INCOME, R30_FIG_BAL_SHEET, R30_FIG_BAL_SHEET_BWP, R30_AMT_STATEMENT_ADJ, R30_AMT_STATEMENT_ADJ_BWP, R30_NET_AMT, R30_NET_AMT_BWP, R30_BAL_SUB, R30_BAL_SUB_BWP, R30_BAL_SUB_DIARIES, R30_BAL_SUB_DIARIES_BWP, "
								+ "R31_OTHER_INCOME, R31_FIG_BAL_SHEET, R31_FIG_BAL_SHEET_BWP, R31_AMT_STATEMENT_ADJ, R31_AMT_STATEMENT_ADJ_BWP, R31_NET_AMT, R31_NET_AMT_BWP, R31_BAL_SUB, R31_BAL_SUB_BWP, R31_BAL_SUB_DIARIES, R31_BAL_SUB_DIARIES_BWP, "
								+ "R40_INTREST_EXPENDED, R40_FIG_BAL_SHEET, R40_FIG_BAL_SHEET_BWP, R40_AMT_STATEMENT_ADJ, R40_AMT_STATEMENT_ADJ_BWP, R40_NET_AMT, R40_NET_AMT_BWP, R40_BAL_SUB, R40_BAL_SUB_BWP, R40_BAL_SUB_DIARIES, R40_BAL_SUB_DIARIES_BWP, "
								+ "R41_INTREST_EXPENDED, R41_FIG_BAL_SHEET, R41_FIG_BAL_SHEET_BWP, R41_AMT_STATEMENT_ADJ, R41_AMT_STATEMENT_ADJ_BWP, R41_NET_AMT, R41_NET_AMT_BWP, R41_BAL_SUB, R41_BAL_SUB_BWP, R41_BAL_SUB_DIARIES, R41_BAL_SUB_DIARIES_BWP, "
								+ "R42_INTREST_EXPENDED, R42_FIG_BAL_SHEET, R42_FIG_BAL_SHEET_BWP, R42_AMT_STATEMENT_ADJ, R42_AMT_STATEMENT_ADJ_BWP, R42_NET_AMT, R42_NET_AMT_BWP, R42_BAL_SUB, R42_BAL_SUB_BWP, R42_BAL_SUB_DIARIES, R42_BAL_SUB_DIARIES_BWP, "
								+ "R43_INTREST_EXPENDED, R43_FIG_BAL_SHEET, R43_FIG_BAL_SHEET_BWP, R43_AMT_STATEMENT_ADJ, R43_AMT_STATEMENT_ADJ_BWP, R43_NET_AMT, R43_NET_AMT_BWP, R43_BAL_SUB, R43_BAL_SUB_BWP, R43_BAL_SUB_DIARIES, R43_BAL_SUB_DIARIES_BWP, "
								+ "R48_OPERATING_EXPENSES, R48_FIG_BAL_SHEET, R48_FIG_BAL_SHEET_BWP, R48_AMT_STATEMENT_ADJ, R48_AMT_STATEMENT_ADJ_BWP, R48_NET_AMT, R48_NET_AMT_BWP, R48_BAL_SUB, R48_BAL_SUB_BWP, R48_BAL_SUB_DIARIES, R48_BAL_SUB_DIARIES_BWP, "
								+ "R49_OPERATING_EXPENSES, R49_FIG_BAL_SHEET, R49_FIG_BAL_SHEET_BWP, R49_AMT_STATEMENT_ADJ, R49_AMT_STATEMENT_ADJ_BWP, R49_NET_AMT, R49_NET_AMT_BWP, R49_BAL_SUB, R49_BAL_SUB_BWP, R49_BAL_SUB_DIARIES, R49_BAL_SUB_DIARIES_BWP, "
								+ "R50_OPERATING_EXPENSES, R50_FIG_BAL_SHEET, R50_FIG_BAL_SHEET_BWP, R50_AMT_STATEMENT_ADJ, R50_AMT_STATEMENT_ADJ_BWP, R50_NET_AMT, R50_NET_AMT_BWP, R50_BAL_SUB, R50_BAL_SUB_BWP, R50_BAL_SUB_DIARIES, R50_BAL_SUB_DIARIES_BWP, "
								+ "R51_OPERATING_EXPENSES, R51_FIG_BAL_SHEET, R51_FIG_BAL_SHEET_BWP, R51_AMT_STATEMENT_ADJ, R51_AMT_STATEMENT_ADJ_BWP, R51_NET_AMT, R51_NET_AMT_BWP, R51_BAL_SUB, R51_BAL_SUB_BWP, R51_BAL_SUB_DIARIES, R51_BAL_SUB_DIARIES_BWP, "
								+ "R52_OPERATING_EXPENSES, R52_FIG_BAL_SHEET, R52_FIG_BAL_SHEET_BWP, R52_AMT_STATEMENT_ADJ, R52_AMT_STATEMENT_ADJ_BWP, R52_NET_AMT, R52_NET_AMT_BWP, R52_BAL_SUB, R52_BAL_SUB_BWP, R52_BAL_SUB_DIARIES, R52_BAL_SUB_DIARIES_BWP, "
								+ "R53_OPERATING_EXPENSES, R53_FIG_BAL_SHEET, R53_FIG_BAL_SHEET_BWP, R53_AMT_STATEMENT_ADJ, R53_AMT_STATEMENT_ADJ_BWP, R53_NET_AMT, R53_NET_AMT_BWP, R53_BAL_SUB, R53_BAL_SUB_BWP, R53_BAL_SUB_DIARIES, R53_BAL_SUB_DIARIES_BWP, "
								+ "R54_OPERATING_EXPENSES, R54_FIG_BAL_SHEET, R54_FIG_BAL_SHEET_BWP, R54_AMT_STATEMENT_ADJ, R54_AMT_STATEMENT_ADJ_BWP, R54_NET_AMT, R54_NET_AMT_BWP, R54_BAL_SUB, R54_BAL_SUB_BWP, R54_BAL_SUB_DIARIES, R54_BAL_SUB_DIARIES_BWP, "
								+ "R55_OPERATING_EXPENSES, R55_FIG_BAL_SHEET, R55_FIG_BAL_SHEET_BWP, R55_AMT_STATEMENT_ADJ, R55_AMT_STATEMENT_ADJ_BWP, R55_NET_AMT, R55_NET_AMT_BWP, R55_BAL_SUB, R55_BAL_SUB_BWP, R55_BAL_SUB_DIARIES, R55_BAL_SUB_DIARIES_BWP, "
								+ "R56_OPERATING_EXPENSES, R56_FIG_BAL_SHEET, R56_FIG_BAL_SHEET_BWP, R56_AMT_STATEMENT_ADJ, R56_AMT_STATEMENT_ADJ_BWP, R56_NET_AMT, R56_NET_AMT_BWP, R56_BAL_SUB, R56_BAL_SUB_BWP, R56_BAL_SUB_DIARIES, R56_BAL_SUB_DIARIES_BWP, "
								+ "R57_OPERATING_EXPENSES, R57_FIG_BAL_SHEET, R57_FIG_BAL_SHEET_BWP, R57_AMT_STATEMENT_ADJ, R57_AMT_STATEMENT_ADJ_BWP, R57_NET_AMT, R57_NET_AMT_BWP, R57_BAL_SUB, R57_BAL_SUB_BWP, R57_BAL_SUB_DIARIES, R57_BAL_SUB_DIARIES_BWP, "
								+ "R58_OPERATING_EXPENSES, R58_FIG_BAL_SHEET, R58_FIG_BAL_SHEET_BWP, R58_AMT_STATEMENT_ADJ, R58_AMT_STATEMENT_ADJ_BWP, R58_NET_AMT, R58_NET_AMT_BWP, R58_BAL_SUB, R58_BAL_SUB_BWP, R58_BAL_SUB_DIARIES, R58_BAL_SUB_DIARIES_BWP, "
								+ "R59_OPERATING_EXPENSES, R59_FIG_BAL_SHEET, R59_FIG_BAL_SHEET_BWP, R59_AMT_STATEMENT_ADJ, R59_AMT_STATEMENT_ADJ_BWP, R59_NET_AMT, R59_NET_AMT_BWP, R59_BAL_SUB, R59_BAL_SUB_BWP, R59_BAL_SUB_DIARIES, R59_BAL_SUB_DIARIES_BWP, "
								+ "R60_OPERATING_EXPENSES, R60_FIG_BAL_SHEET, R60_FIG_BAL_SHEET_BWP, R60_AMT_STATEMENT_ADJ, R60_AMT_STATEMENT_ADJ_BWP, R60_NET_AMT, R60_NET_AMT_BWP, R60_BAL_SUB, R60_BAL_SUB_BWP, R60_BAL_SUB_DIARIES, R60_BAL_SUB_DIARIES_BWP, "
								+ "R61_OPERATING_EXPENSES, R61_FIG_BAL_SHEET, R61_FIG_BAL_SHEET_BWP, R61_AMT_STATEMENT_ADJ, R61_AMT_STATEMENT_ADJ_BWP, R61_NET_AMT, R61_NET_AMT_BWP, R61_BAL_SUB, R61_BAL_SUB_BWP, R61_BAL_SUB_DIARIES, R61_BAL_SUB_DIARIES_BWP, "
								+ "R62_OPERATING_EXPENSES, R62_FIG_BAL_SHEET, R62_FIG_BAL_SHEET_BWP, R62_AMT_STATEMENT_ADJ, R62_AMT_STATEMENT_ADJ_BWP, R62_NET_AMT, R62_NET_AMT_BWP, R62_BAL_SUB, R62_BAL_SUB_BWP, R62_BAL_SUB_DIARIES, R62_BAL_SUB_DIARIES_BWP, "
								+ "R63_OPERATING_EXPENSES, R63_FIG_BAL_SHEET, R63_FIG_BAL_SHEET_BWP, R63_AMT_STATEMENT_ADJ, R63_AMT_STATEMENT_ADJ_BWP, R63_NET_AMT, R63_NET_AMT_BWP, R63_BAL_SUB, R63_BAL_SUB_BWP, R63_BAL_SUB_DIARIES, R63_BAL_SUB_DIARIES_BWP, "
								+ "REPORT_DATE, ? AS REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
								+ "FROM BRRS_PL_SCHS_SUMMARYTABLE " + "WHERE REPORT_DATE = TO_DATE(?, 'DD-MM-YYYY')";

						int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, formattedDate);
						System.out.println("Successfully transferred " + rowsInsertedSum + " rows.");

						// 6. Fixed date conversion here
						String adsumsql = "DELETE FROM BRRS_PL_SCHS_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MM-YYYY')";
						int rowsDeletedSum = jdbcTemplate.update(adsumsql, formattedDate);
						System.out.println("Deleted from summary " + rowsDeletedSum + " rows after transfering.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public byte[] getPL_SCHSDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  PL_SCHS Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getPL_SCHSDetailNewExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("PL_SCHSDetailsDetail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "AVERAGE", "REPORT LABEL",
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
			List<PL_SCHS_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (PL_SCHS_Detail_Entity item : reportData) {
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

					// AVERAGE (right aligned, 3 decimal places)
					Cell balanceCell1 = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell1.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell1.setCellValue(0);
					}
					balanceCell1.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for PL_SCHS — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating PL_SCHS Excel", e);
			return new byte[0];
		}
	}

	public byte[] getPL_SCHSDetailNewExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for PL_SCHS ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("PL_SCHS Detail NEW");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "AVERAGE", "REPORT LABEL",
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
			List<PL_SCHS_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (PL_SCHS_Archival_Detail_Entity item : reportData) {
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

					// AVERAGE (right aligned, 3 decimal places)
					Cell balanceCell1 = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell1.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell1.setCellValue(0);
					}
					balanceCell1.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for PL_SCHS — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating PL_SCHS NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getPL_SCHSExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.PL_SCHS");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelPL_SCHSARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<PL_SCHS_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

		System.out.println("DATA SIZE IS : " + dataList.size());
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  PL_SCHS report. Returning empty result.");
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

					PL_SCHS_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(9);

					if (record.getREPORT_DATE() != null) {

						R12Cell.setCellValue(record.getREPORT_DATE());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(8);
					Cell R9Cell1 = row.createCell(3);
					if (record.getR9_fig_bal_sheet() != null) {
						R9Cell1.setCellValue(record.getR9_fig_bal_sheet().doubleValue());
						R9Cell1.setCellStyle(numberStyle);
					} else {
						R9Cell1.setCellValue("");
						R9Cell1.setCellStyle(textStyle);
					}

					// R9 Col E
					Cell R9Cell2 = row.createCell(4);
					if (record.getR9_fig_bal_sheet_bwp() != null) {
						R9Cell2.setCellValue(record.getR9_fig_bal_sheet_bwp().doubleValue());
						R9Cell2.setCellStyle(numberStyle);
					} else {
						R9Cell2.setCellValue("");
						R9Cell2.setCellStyle(textStyle);
					}

					// R9 Col F
					Cell R9Cell3 = row.createCell(5);
					if (record.getR9_amt_statement_adj() != null) {
						R9Cell3.setCellValue(record.getR9_amt_statement_adj().doubleValue());
						R9Cell3.setCellStyle(numberStyle);
					} else {
						R9Cell3.setCellValue("");
						R9Cell3.setCellStyle(textStyle);
					}
					// R9 Col G
					Cell R9Cell4 = row.createCell(6);
					if (record.getR9_amt_statement_adj_bwp() != null) {
						R9Cell4.setCellValue(record.getR9_amt_statement_adj_bwp().doubleValue());
						R9Cell4.setCellStyle(numberStyle);
					} else {
						R9Cell4.setCellValue("");
						R9Cell4.setCellStyle(textStyle);
					}
					// // R9 Col H
					// Cell R9Cell5 = row.createCell(7);
					// if (record.getR9_net_amt() != null) {
					// R9Cell5.setCellValue(record.getR9_net_amt().doubleValue());
					// R9Cell5.setCellStyle(numberStyle);
					// } else {
					// R9Cell5.setCellValue("");
					// R9Cell5.setCellStyle(textStyle);
					// }
					// R9 Col I
					Cell R9Cell6 = row.createCell(8);
					if (record.getR9_net_amt_bwp() != null) {
						R9Cell6.setCellValue(record.getR9_net_amt_bwp().doubleValue());
						R9Cell6.setCellStyle(numberStyle);
					} else {
						R9Cell6.setCellValue("");
						R9Cell6.setCellStyle(textStyle);
					}
					// R9 Col J
					Cell R9Cell7 = row.createCell(9);
					if (record.getR9_bal_sub() != null) {
						R9Cell7.setCellValue(record.getR9_bal_sub().doubleValue());
						R9Cell7.setCellStyle(numberStyle);
					} else {
						R9Cell7.setCellValue("");
						R9Cell7.setCellStyle(textStyle);
					}
					// R9 Col K
					Cell R9Cell8 = row.createCell(10);
					if (record.getR9_bal_sub_bwp() != null) {
						R9Cell8.setCellValue(record.getR9_bal_sub_bwp().doubleValue());
						R9Cell8.setCellStyle(numberStyle);
					} else {
						R9Cell8.setCellValue("");
						R9Cell8.setCellStyle(textStyle);
					}
					// R9 Col L
					Cell R9Cell9 = row.createCell(11);
					if (record.getR9_bal_sub_diaries() != null) {
						R9Cell9.setCellValue(record.getR9_bal_sub_diaries().doubleValue());
						R9Cell9.setCellStyle(numberStyle);
					} else {
						R9Cell9.setCellValue("");
						R9Cell9.setCellStyle(textStyle);
					}
					// R9 Col M
					Cell R9Cell10 = row.createCell(12);
					if (record.getR9_bal_sub_diaries_bwp() != null) {
						R9Cell10.setCellValue(record.getR9_bal_sub_diaries_bwp().doubleValue());
						R9Cell10.setCellStyle(numberStyle);
					} else {
						R9Cell10.setCellValue("");
						R9Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(9);
					Cell R10Cell1 = row.createCell(3);
					if (record.getR10_fig_bal_sheet() != null) {
						R10Cell1.setCellValue(record.getR10_fig_bal_sheet().doubleValue());
						R10Cell1.setCellStyle(numberStyle);
					} else {
						R10Cell1.setCellValue("");
						R10Cell1.setCellStyle(textStyle);
					}

					// R10 Col E
					Cell R10Cell2 = row.createCell(4);
					if (record.getR10_fig_bal_sheet_bwp() != null) {
						R10Cell2.setCellValue(record.getR10_fig_bal_sheet_bwp().doubleValue());
						R10Cell2.setCellStyle(numberStyle);
					} else {
						R10Cell2.setCellValue("");
						R10Cell2.setCellStyle(textStyle);
					}

					// R10 Col F
					Cell R10Cell3 = row.createCell(5);
					if (record.getR10_amt_statement_adj() != null) {
						R10Cell3.setCellValue(record.getR10_amt_statement_adj().doubleValue());
						R10Cell3.setCellStyle(numberStyle);
					} else {
						R10Cell3.setCellValue("");
						R10Cell3.setCellStyle(textStyle);
					}
					// R10 Col G
					Cell R10Cell4 = row.createCell(6);
					if (record.getR10_amt_statement_adj_bwp() != null) {
						R10Cell4.setCellValue(record.getR10_amt_statement_adj_bwp().doubleValue());
						R10Cell4.setCellStyle(numberStyle);
					} else {
						R10Cell4.setCellValue("");
						R10Cell4.setCellStyle(textStyle);
					}
					// R10 Col H
					// Cell R10Cell5 = row.createCell(7);
					// if (record.getR10_net_amt() != null) {
					// R10Cell5.setCellValue(record.getR10_net_amt().doubleValue());
					// R10Cell5.setCellStyle(numberStyle);
					// } else {
					// R10Cell5.setCellValue("");
					// R10Cell5.setCellStyle(textStyle);
					// }
					// R10 Col I
					Cell R10Cell6 = row.createCell(8);
					if (record.getR10_net_amt_bwp() != null) {
						R10Cell6.setCellValue(record.getR10_net_amt_bwp().doubleValue());
						R10Cell6.setCellStyle(numberStyle);
					} else {
						R10Cell6.setCellValue("");
						R10Cell6.setCellStyle(textStyle);
					}
					// R10 Col J
					Cell R10Cell7 = row.createCell(9);
					if (record.getR10_bal_sub() != null) {
						R10Cell7.setCellValue(record.getR10_bal_sub().doubleValue());
						R10Cell7.setCellStyle(numberStyle);
					} else {
						R10Cell7.setCellValue("");
						R10Cell7.setCellStyle(textStyle);
					}
					// R10 Col K
					Cell R10Cell8 = row.createCell(10);
					if (record.getR10_bal_sub_bwp() != null) {
						R10Cell8.setCellValue(record.getR10_bal_sub_bwp().doubleValue());
						R10Cell8.setCellStyle(numberStyle);
					} else {
						R10Cell8.setCellValue("");
						R10Cell8.setCellStyle(textStyle);
					}
					// R10 Col L
					Cell R10Cell9 = row.createCell(11);
					if (record.getR10_bal_sub_diaries() != null) {
						R10Cell9.setCellValue(record.getR10_bal_sub_diaries().doubleValue());
						R10Cell9.setCellStyle(numberStyle);
					} else {
						R10Cell9.setCellValue("");
						R10Cell9.setCellStyle(textStyle);
					}
					// R10 Col M
					Cell R10Cell10 = row.createCell(12);
					if (record.getR10_bal_sub_diaries_bwp() != null) {
						R10Cell10.setCellValue(record.getR10_bal_sub_diaries_bwp().doubleValue());
						R10Cell10.setCellStyle(numberStyle);
					} else {
						R10Cell10.setCellValue("");
						R10Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(10);
					Cell R11Cell1 = row.createCell(3);
					if (record.getR11_fig_bal_sheet() != null) {
						R11Cell1.setCellValue(record.getR11_fig_bal_sheet().doubleValue());
						R11Cell1.setCellStyle(numberStyle);
					} else {
						R11Cell1.setCellValue("");
						R11Cell1.setCellStyle(textStyle);
					}

					// R11 Col E
					Cell R11Cell2 = row.createCell(4);
					if (record.getR11_fig_bal_sheet_bwp() != null) {
						R11Cell2.setCellValue(record.getR11_fig_bal_sheet_bwp().doubleValue());
						R11Cell2.setCellStyle(numberStyle);
					} else {
						R11Cell2.setCellValue("");
						R11Cell2.setCellStyle(textStyle);
					}

					// R11 Col F
					Cell R11Cell3 = row.createCell(5);
					if (record.getR11_amt_statement_adj() != null) {
						R11Cell3.setCellValue(record.getR11_amt_statement_adj().doubleValue());
						R11Cell3.setCellStyle(numberStyle);
					} else {
						R11Cell3.setCellValue("");
						R11Cell3.setCellStyle(textStyle);
					}
					// R11 Col G
					Cell R11Cell4 = row.createCell(6);
					if (record.getR11_amt_statement_adj_bwp() != null) {
						R11Cell4.setCellValue(record.getR11_amt_statement_adj_bwp().doubleValue());
						R11Cell4.setCellStyle(numberStyle);
					} else {
						R11Cell4.setCellValue("");
						R11Cell4.setCellStyle(textStyle);
					}
					// // R11 Col H
					// Cell R11Cell5 = row.createCell(7);
					// if (record.getR11_net_amt() != null) {
					// R11Cell5.setCellValue(record.getR11_net_amt().doubleValue());
					// R11Cell5.setCellStyle(numberStyle);
					// } else {
					// R11Cell5.setCellValue("");
					// R11Cell5.setCellStyle(textStyle);
					// }
					// R11 Col I
					Cell R11Cell6 = row.createCell(8);
					if (record.getR11_net_amt_bwp() != null) {
						R11Cell6.setCellValue(record.getR11_net_amt_bwp().doubleValue());
						R11Cell6.setCellStyle(numberStyle);
					} else {
						R11Cell6.setCellValue("");
						R11Cell6.setCellStyle(textStyle);
					}
					// R11 Col J
					Cell R11Cell7 = row.createCell(9);
					if (record.getR11_bal_sub() != null) {
						R11Cell7.setCellValue(record.getR11_bal_sub().doubleValue());
						R11Cell7.setCellStyle(numberStyle);
					} else {
						R11Cell7.setCellValue("");
						R11Cell7.setCellStyle(textStyle);
					}
					// R11 Col K
					Cell R11Cell8 = row.createCell(10);
					if (record.getR11_bal_sub_bwp() != null) {
						R11Cell8.setCellValue(record.getR11_bal_sub_bwp().doubleValue());
						R11Cell8.setCellStyle(numberStyle);
					} else {
						R11Cell8.setCellValue("");
						R11Cell8.setCellStyle(textStyle);
					}
					// R11 Col L
					Cell R11Cell9 = row.createCell(11);
					if (record.getR11_bal_sub_diaries() != null) {
						R11Cell9.setCellValue(record.getR11_bal_sub_diaries().doubleValue());
						R11Cell9.setCellStyle(numberStyle);
					} else {
						R11Cell9.setCellValue("");
						R11Cell9.setCellStyle(textStyle);
					}
					// R11 Col M
					Cell R11Cell10 = row.createCell(12);
					if (record.getR11_bal_sub_diaries_bwp() != null) {
						R11Cell10.setCellValue(record.getR11_bal_sub_diaries_bwp().doubleValue());
						R11Cell10.setCellStyle(numberStyle);
					} else {
						R11Cell10.setCellValue("");
						R11Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(11);
					Cell R12Cell1 = row.createCell(3);
					if (record.getR12_fig_bal_sheet() != null) {
						R12Cell1.setCellValue(record.getR12_fig_bal_sheet().doubleValue());
						R12Cell1.setCellStyle(numberStyle);
					} else {
						R12Cell1.setCellValue("");
						R12Cell1.setCellStyle(textStyle);
					}

					// R12 Col E
					Cell R12Cell2 = row.createCell(4);
					if (record.getR12_fig_bal_sheet_bwp() != null) {
						R12Cell2.setCellValue(record.getR12_fig_bal_sheet_bwp().doubleValue());
						R12Cell2.setCellStyle(numberStyle);
					} else {
						R12Cell2.setCellValue("");
						R12Cell2.setCellStyle(textStyle);
					}

					// R12 Col F
					Cell R12Cell3 = row.createCell(5);
					if (record.getR12_amt_statement_adj() != null) {
						R12Cell3.setCellValue(record.getR12_amt_statement_adj().doubleValue());
						R12Cell3.setCellStyle(numberStyle);
					} else {
						R12Cell3.setCellValue("");
						R12Cell3.setCellStyle(textStyle);
					}
					// R12 Col G
					Cell R12Cell4 = row.createCell(6);
					if (record.getR12_amt_statement_adj_bwp() != null) {
						R12Cell4.setCellValue(record.getR12_amt_statement_adj_bwp().doubleValue());
						R12Cell4.setCellStyle(numberStyle);
					} else {
						R12Cell4.setCellValue("");
						R12Cell4.setCellStyle(textStyle);
					}
					// R12 Col H
					// Cell R12Cell5 = row.createCell(7);
					// if (record.getR12_net_amt() != null) {
					// R12Cell5.setCellValue(record.getR12_net_amt().doubleValue());
					// R12Cell5.setCellStyle(numberStyle);
					// } else {
					// R12Cell5.setCellValue("");
					// R12Cell5.setCellStyle(textStyle);
					// }
					// R12 Col I
					Cell R12Cell6 = row.createCell(8);
					if (record.getR12_net_amt_bwp() != null) {
						R12Cell6.setCellValue(record.getR12_net_amt_bwp().doubleValue());
						R12Cell6.setCellStyle(numberStyle);
					} else {
						R12Cell6.setCellValue("");
						R12Cell6.setCellStyle(textStyle);
					}
					// R12 Col J
					Cell R12Cell7 = row.createCell(9);
					if (record.getR12_bal_sub() != null) {
						R12Cell7.setCellValue(record.getR12_bal_sub().doubleValue());
						R12Cell7.setCellStyle(numberStyle);
					} else {
						R12Cell7.setCellValue("");
						R12Cell7.setCellStyle(textStyle);
					}
					// R12 Col K
					Cell R12Cell8 = row.createCell(10);
					if (record.getR12_bal_sub_bwp() != null) {
						R12Cell8.setCellValue(record.getR12_bal_sub_bwp().doubleValue());
						R12Cell8.setCellStyle(numberStyle);
					} else {
						R12Cell8.setCellValue("");
						R12Cell8.setCellStyle(textStyle);
					}
					// R12 Col L
					Cell R12Cell9 = row.createCell(11);
					if (record.getR12_bal_sub_diaries() != null) {
						R12Cell9.setCellValue(record.getR12_bal_sub_diaries().doubleValue());
						R12Cell9.setCellStyle(numberStyle);
					} else {
						R12Cell9.setCellValue("");
						R12Cell9.setCellStyle(textStyle);
					}

					// R12 Col M
					Cell R12Cell10 = row.createCell(12);
					if (record.getR12_bal_sub_diaries_bwp() != null) {
						R12Cell10.setCellValue(record.getR12_bal_sub_diaries_bwp().doubleValue());
						R12Cell10.setCellStyle(numberStyle);
					} else {
						R12Cell10.setCellValue("");
						R12Cell10.setCellStyle(textStyle);
					}
					// row = sheet.getRow(12);
					// Cell R13Cell1 = row.createCell(3);
					// if (record.getR13_fig_bal_sheet() != null) {
					// R13Cell1.setCellValue(record.getR13_fig_bal_sheet().doubleValue());
					// R13Cell1.setCellStyle(numberStyle);
					// } else {
					// R13Cell1.setCellValue("");
					// R13Cell1.setCellStyle(textStyle);
					// }

					// // R13 Col E
					// Cell R13Cell2 = row.createCell(4);
					// if (record.getR13_fig_bal_sheet_bwp() != null) {
					// R13Cell2.setCellValue(record.getR13_fig_bal_sheet_bwp().doubleValue());
					// R13Cell2.setCellStyle(numberStyle);
					// } else {
					// R13Cell2.setCellValue("");
					// R13Cell2.setCellStyle(textStyle);
					// }

					// // R13 Col F
					// Cell R13Cell3 = row.createCell(5);
					// if (record.getR13_amt_statement_adj() != null) {
					// R13Cell3.setCellValue(record.getR13_amt_statement_adj().doubleValue());
					// R13Cell3.setCellStyle(numberStyle);
					// } else {
					// R13Cell3.setCellValue("");
					// R13Cell3.setCellStyle(textStyle);
					// }
					// // R13 Col G
					// Cell R13Cell4 = row.createCell(6);
					// if (record.getR13_amt_statement_adj_bwp() != null) {
					// R13Cell4.setCellValue(record.getR13_amt_statement_adj_bwp().doubleValue());
					// R13Cell4.setCellStyle(numberStyle);
					// } else {
					// R13Cell4.setCellValue("");
					// R13Cell4.setCellStyle(textStyle);
					// }
					// // R13 Col H
					// Cell R13Cell5 = row.createCell(7);
					// if (record.getR13_net_amt() != null) {
					// R13Cell5.setCellValue(record.getR13_net_amt().doubleValue());
					// R13Cell5.setCellStyle(numberStyle);
					// } else {
					// R13Cell5.setCellValue("");
					// R13Cell5.setCellStyle(textStyle);
					// }
					// // R13 Col I
					// Cell R13Cell6 = row.createCell(8);
					// if (record.getR13_net_amt_bwp() != null) {
					// R13Cell6.setCellValue(record.getR13_net_amt_bwp().doubleValue());
					// R13Cell6.setCellStyle(numberStyle);
					// } else {
					// R13Cell6.setCellValue("");
					// R13Cell6.setCellStyle(textStyle);
					// }
					// // R13 Col J
					// Cell R13Cell7 = row.createCell(9);
					// if (record.getR13_bal_sub() != null) {
					// R13Cell7.setCellValue(record.getR13_bal_sub().doubleValue());
					// R13Cell7.setCellStyle(numberStyle);
					// } else {
					// R13Cell7.setCellValue("");
					// R13Cell7.setCellStyle(textStyle);
					// }
					// // R13 Col K
					// Cell R13Cell8 = row.createCell(10);
					// if (record.getR13_bal_sub_bwp() != null) {
					// R13Cell8.setCellValue(record.getR13_bal_sub_bwp().doubleValue());
					// R13Cell8.setCellStyle(numberStyle);
					// } else {
					// R13Cell8.setCellValue("");
					// R13Cell8.setCellStyle(textStyle);
					// }
					// // R13 Col L
					// Cell R13Cell9 = row.createCell(11);
					// if (record.getR13_bal_sub_diaries() != null) {
					// R13Cell9.setCellValue(record.getR13_bal_sub_diaries().doubleValue());
					// R13Cell9.setCellStyle(numberStyle);
					// } else {
					// R13Cell9.setCellValue("");
					// R13Cell9.setCellStyle(textStyle);
					// }
					// // R13 Col M
					// Cell R13Cell10 = row.createCell(12);
					// if (record.getR13_bal_sub_diaries_bwp() != null) {
					// R13Cell10.setCellValue(record.getR13_bal_sub_diaries_bwp().doubleValue());
					// R13Cell10.setCellStyle(numberStyle);
					// } else {
					// R13Cell10.setCellValue("");
					// R13Cell10.setCellStyle(textStyle);
					// }

					row = sheet.getRow(16);
					Cell R17Cell1 = row.createCell(3);
					if (record.getR17_fig_bal_sheet() != null) {
						R17Cell1.setCellValue(record.getR17_fig_bal_sheet().doubleValue());
						R17Cell1.setCellStyle(numberStyle);
					} else {
						R17Cell1.setCellValue("");
						R17Cell1.setCellStyle(textStyle);
					}

					// R17 Col E
					Cell R17Cell2 = row.createCell(4);
					if (record.getR17_fig_bal_sheet_bwp() != null) {
						R17Cell2.setCellValue(record.getR17_fig_bal_sheet_bwp().doubleValue());
						R17Cell2.setCellStyle(numberStyle);
					} else {
						R17Cell2.setCellValue("");
						R17Cell2.setCellStyle(textStyle);
					}

					// R17 Col F
					Cell R17Cell3 = row.createCell(5);
					if (record.getR17_amt_statement_adj() != null) {
						R17Cell3.setCellValue(record.getR17_amt_statement_adj().doubleValue());
						R17Cell3.setCellStyle(numberStyle);
					} else {
						R17Cell3.setCellValue("");
						R17Cell3.setCellStyle(textStyle);
					}
					// R17 Col G
					Cell R17Cell4 = row.createCell(6);
					if (record.getR17_amt_statement_adj_bwp() != null) {
						R17Cell4.setCellValue(record.getR17_amt_statement_adj_bwp().doubleValue());
						R17Cell4.setCellStyle(numberStyle);
					} else {
						R17Cell4.setCellValue("");
						R17Cell4.setCellStyle(textStyle);
					}
					// R17 Col H
					// Cell R17Cell5 = row.createCell(7);
					// if (record.getR17_net_amt() != null) {
					// R17Cell5.setCellValue(record.getR17_net_amt().doubleValue());
					// R17Cell5.setCellStyle(numberStyle);
					// } else {
					// R17Cell5.setCellValue("");
					// R17Cell5.setCellStyle(textStyle);
					// }
					// R17 Col I
					Cell R17Cell6 = row.createCell(8);
					if (record.getR17_net_amt_bwp() != null) {
						R17Cell6.setCellValue(record.getR17_net_amt_bwp().doubleValue());
						R17Cell6.setCellStyle(numberStyle);
					} else {
						R17Cell6.setCellValue("");
						R17Cell6.setCellStyle(textStyle);
					}
					// R17 Col J
					Cell R17Cell7 = row.createCell(9);
					if (record.getR17_bal_sub() != null) {
						R17Cell7.setCellValue(record.getR17_bal_sub().doubleValue());
						R17Cell7.setCellStyle(numberStyle);
					} else {
						R17Cell7.setCellValue("");
						R17Cell7.setCellStyle(textStyle);
					}
					// R17 Col K
					Cell R17Cell8 = row.createCell(10);
					if (record.getR17_bal_sub_bwp() != null) {
						R17Cell8.setCellValue(record.getR17_bal_sub_bwp().doubleValue());
						R17Cell8.setCellStyle(numberStyle);
					} else {
						R17Cell8.setCellValue("");
						R17Cell8.setCellStyle(textStyle);
					}
					// R17 Col L
					Cell R17Cell9 = row.createCell(11);
					if (record.getR17_bal_sub_diaries() != null) {
						R17Cell9.setCellValue(record.getR17_bal_sub_diaries().doubleValue());
						R17Cell9.setCellStyle(numberStyle);
					} else {
						R17Cell9.setCellValue("");
						R17Cell9.setCellStyle(textStyle);
					}
					// R17 Col M
					Cell R17Cell10 = row.createCell(12);
					if (record.getR17_bal_sub_diaries_bwp() != null) {
						R17Cell10.setCellValue(record.getR17_bal_sub_diaries_bwp().doubleValue());
						R17Cell10.setCellStyle(numberStyle);
					} else {
						R17Cell10.setCellValue("");
						R17Cell10.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					Cell R18Cell1 = row.createCell(3);
					if (record.getR18_fig_bal_sheet() != null) {
						R18Cell1.setCellValue(record.getR18_fig_bal_sheet().doubleValue());
						R18Cell1.setCellStyle(numberStyle);
					} else {
						R18Cell1.setCellValue("");
						R18Cell1.setCellStyle(textStyle);
					}

					// R18 Col E
					Cell R18Cell2 = row.createCell(4);
					if (record.getR18_fig_bal_sheet_bwp() != null) {
						R18Cell2.setCellValue(record.getR18_fig_bal_sheet_bwp().doubleValue());
						R18Cell2.setCellStyle(numberStyle);
					} else {
						R18Cell2.setCellValue("");
						R18Cell2.setCellStyle(textStyle);
					}

					// R18 Col F
					Cell R18Cell3 = row.createCell(5);
					if (record.getR18_amt_statement_adj() != null) {
						R18Cell3.setCellValue(record.getR18_amt_statement_adj().doubleValue());
						R18Cell3.setCellStyle(numberStyle);
					} else {
						R18Cell3.setCellValue("");
						R18Cell3.setCellStyle(textStyle);
					}
					// R18 Col G
					Cell R18Cell4 = row.createCell(6);
					if (record.getR18_amt_statement_adj_bwp() != null) {
						R18Cell4.setCellValue(record.getR18_amt_statement_adj_bwp().doubleValue());
						R18Cell4.setCellStyle(numberStyle);
					} else {
						R18Cell4.setCellValue("");
						R18Cell4.setCellStyle(textStyle);
					}
					// // R18 Col H
					// Cell R18Cell5 = row.createCell(7);
					// if (record.getR18_net_amt() != null) {
					// R18Cell5.setCellValue(record.getR18_net_amt().doubleValue());
					// R18Cell5.setCellStyle(numberStyle);
					// } else {
					// R18Cell5.setCellValue("");
					// R18Cell5.setCellStyle(textStyle);
					// }
					// R18 Col I
					Cell R18Cell6 = row.createCell(8);
					if (record.getR18_net_amt_bwp() != null) {
						R18Cell6.setCellValue(record.getR18_net_amt_bwp().doubleValue());
						R18Cell6.setCellStyle(numberStyle);
					} else {
						R18Cell6.setCellValue("");
						R18Cell6.setCellStyle(textStyle);
					}
					// R18 Col J
					Cell R18Cell7 = row.createCell(9);
					if (record.getR18_bal_sub() != null) {
						R18Cell7.setCellValue(record.getR18_bal_sub().doubleValue());
						R18Cell7.setCellStyle(numberStyle);
					} else {
						R18Cell7.setCellValue("");
						R18Cell7.setCellStyle(textStyle);
					}
					// R18 Col K
					Cell R18Cell8 = row.createCell(10);
					if (record.getR18_bal_sub_bwp() != null) {
						R18Cell8.setCellValue(record.getR18_bal_sub_bwp().doubleValue());
						R18Cell8.setCellStyle(numberStyle);
					} else {
						R18Cell8.setCellValue("");
						R18Cell8.setCellStyle(textStyle);
					}
					// R18 Col L
					Cell R18Cell9 = row.createCell(11);
					if (record.getR18_bal_sub_diaries() != null) {
						R18Cell9.setCellValue(record.getR18_bal_sub_diaries().doubleValue());
						R18Cell9.setCellStyle(numberStyle);
					} else {
						R18Cell9.setCellValue("");
						R18Cell9.setCellStyle(textStyle);
					}
					// R18 Col M
					Cell R18Cell10 = row.createCell(12);
					if (record.getR18_bal_sub_diaries_bwp() != null) {
						R18Cell10.setCellValue(record.getR18_bal_sub_diaries_bwp().doubleValue());
						R18Cell10.setCellStyle(numberStyle);
					} else {
						R18Cell10.setCellValue("");
						R18Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(18);
					Cell R19Cell1 = row.createCell(3);
					if (record.getR19_fig_bal_sheet() != null) {
						R19Cell1.setCellValue(record.getR19_fig_bal_sheet().doubleValue());
						R19Cell1.setCellStyle(numberStyle);
					} else {
						R19Cell1.setCellValue("");
						R19Cell1.setCellStyle(textStyle);
					}

					// R19 Col E
					Cell R19Cell2 = row.createCell(4);
					if (record.getR19_fig_bal_sheet_bwp() != null) {
						R19Cell2.setCellValue(record.getR19_fig_bal_sheet_bwp().doubleValue());
						R19Cell2.setCellStyle(numberStyle);
					} else {
						R19Cell2.setCellValue("");
						R19Cell2.setCellStyle(textStyle);
					}

					// R19 Col F
					Cell R19Cell3 = row.createCell(5);
					if (record.getR19_amt_statement_adj() != null) {
						R19Cell3.setCellValue(record.getR19_amt_statement_adj().doubleValue());
						R19Cell3.setCellStyle(numberStyle);
					} else {
						R19Cell3.setCellValue("");
						R19Cell3.setCellStyle(textStyle);
					}
					// R19 Col G
					Cell R19Cell4 = row.createCell(6);
					if (record.getR19_amt_statement_adj_bwp() != null) {
						R19Cell4.setCellValue(record.getR19_amt_statement_adj_bwp().doubleValue());
						R19Cell4.setCellStyle(numberStyle);
					} else {
						R19Cell4.setCellValue("");
						R19Cell4.setCellStyle(textStyle);
					}
					// R19 Col H
					// Cell R19Cell5 = row.createCell(7);
					// if (record.getR19_net_amt() != null) {
					// R19Cell5.setCellValue(record.getR19_net_amt().doubleValue());
					// R19Cell5.setCellStyle(numberStyle);
					// } else {
					// R19Cell5.setCellValue("");
					// R19Cell5.setCellStyle(textStyle);
					// }
					// R19 Col I
					Cell R19Cell6 = row.createCell(8);
					if (record.getR19_net_amt_bwp() != null) {
						R19Cell6.setCellValue(record.getR19_net_amt_bwp().doubleValue());
						R19Cell6.setCellStyle(numberStyle);
					} else {
						R19Cell6.setCellValue("");
						R19Cell6.setCellStyle(textStyle);
					}
					// R19 Col J
					Cell R19Cell7 = row.createCell(9);
					if (record.getR19_bal_sub() != null) {
						R19Cell7.setCellValue(record.getR19_bal_sub().doubleValue());
						R19Cell7.setCellStyle(numberStyle);
					} else {
						R19Cell7.setCellValue("");
						R19Cell7.setCellStyle(textStyle);
					}
					// R19 Col K
					Cell R19Cell8 = row.createCell(10);
					if (record.getR19_bal_sub_bwp() != null) {
						R19Cell8.setCellValue(record.getR19_bal_sub_bwp().doubleValue());
						R19Cell8.setCellStyle(numberStyle);
					} else {
						R19Cell8.setCellValue("");
						R19Cell8.setCellStyle(textStyle);
					}
					// R19 Col L
					Cell R19Cell9 = row.createCell(11);
					if (record.getR19_bal_sub_diaries() != null) {
						R19Cell9.setCellValue(record.getR19_bal_sub_diaries().doubleValue());
						R19Cell9.setCellStyle(numberStyle);
					} else {
						R19Cell9.setCellValue("");
						R19Cell9.setCellStyle(textStyle);
					}
					// R19 Col M
					Cell R19Cell10 = row.createCell(12);
					if (record.getR19_bal_sub_diaries_bwp() != null) {
						R19Cell10.setCellValue(record.getR19_bal_sub_diaries_bwp().doubleValue());
						R19Cell10.setCellStyle(numberStyle);
					} else {
						R19Cell10.setCellValue("");
						R19Cell10.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					Cell R20Cell1 = row.createCell(3);
					if (record.getR20_fig_bal_sheet() != null) {
						R20Cell1.setCellValue(record.getR20_fig_bal_sheet().doubleValue());
						R20Cell1.setCellStyle(numberStyle);
					} else {
						R20Cell1.setCellValue("");
						R20Cell1.setCellStyle(textStyle);
					}

					// R20 Col E
					Cell R20Cell2 = row.createCell(4);
					if (record.getR20_fig_bal_sheet_bwp() != null) {
						R20Cell2.setCellValue(record.getR20_fig_bal_sheet_bwp().doubleValue());
						R20Cell2.setCellStyle(numberStyle);
					} else {
						R20Cell2.setCellValue("");
						R20Cell2.setCellStyle(textStyle);
					}

					// R20 Col F
					Cell R20Cell3 = row.createCell(5);
					if (record.getR20_amt_statement_adj() != null) {
						R20Cell3.setCellValue(record.getR20_amt_statement_adj().doubleValue());
						R20Cell3.setCellStyle(numberStyle);
					} else {
						R20Cell3.setCellValue("");
						R20Cell3.setCellStyle(textStyle);
					}
					// R20 Col G
					Cell R20Cell4 = row.createCell(6);
					if (record.getR20_amt_statement_adj_bwp() != null) {
						R20Cell4.setCellValue(record.getR20_amt_statement_adj_bwp().doubleValue());
						R20Cell4.setCellStyle(numberStyle);
					} else {
						R20Cell4.setCellValue("");
						R20Cell4.setCellStyle(textStyle);
					}
					// // R20 Col H
					// Cell R20Cell5 = row.createCell(7);
					// if (record.getR20_net_amt() != null) {
					// R20Cell5.setCellValue(record.getR20_net_amt().doubleValue());
					// R20Cell5.setCellStyle(numberStyle);
					// } else {
					// R20Cell5.setCellValue("");
					// R20Cell5.setCellStyle(textStyle);
					// }
					// R20 Col I
					Cell R20Cell6 = row.createCell(8);
					if (record.getR20_net_amt_bwp() != null) {
						R20Cell6.setCellValue(record.getR20_net_amt_bwp().doubleValue());
						R20Cell6.setCellStyle(numberStyle);
					} else {
						R20Cell6.setCellValue("");
						R20Cell6.setCellStyle(textStyle);
					}
					// R20 Col J
					Cell R20Cell7 = row.createCell(9);
					if (record.getR20_bal_sub() != null) {
						R20Cell7.setCellValue(record.getR20_bal_sub().doubleValue());
						R20Cell7.setCellStyle(numberStyle);
					} else {
						R20Cell7.setCellValue("");
						R20Cell7.setCellStyle(textStyle);
					}
					// R20 Col K
					Cell R20Cell8 = row.createCell(10);
					if (record.getR20_bal_sub_bwp() != null) {
						R20Cell8.setCellValue(record.getR20_bal_sub_bwp().doubleValue());
						R20Cell8.setCellStyle(numberStyle);
					} else {
						R20Cell8.setCellValue("");
						R20Cell8.setCellStyle(textStyle);
					}
					// R20 Col L
					Cell R20Cell9 = row.createCell(11);
					if (record.getR20_bal_sub_diaries() != null) {
						R20Cell9.setCellValue(record.getR20_bal_sub_diaries().doubleValue());
						R20Cell9.setCellStyle(numberStyle);
					} else {
						R20Cell9.setCellValue("");
						R20Cell9.setCellStyle(textStyle);
					}
					// R20 Col M
					Cell R20Cell10 = row.createCell(12);
					if (record.getR20_bal_sub_diaries_bwp() != null) {
						R20Cell10.setCellValue(record.getR20_bal_sub_diaries_bwp().doubleValue());
						R20Cell10.setCellStyle(numberStyle);
					} else {
						R20Cell10.setCellValue("");
						R20Cell10.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					Cell R21Cell1 = row.createCell(3);
					if (record.getR21_fig_bal_sheet() != null) {
						R21Cell1.setCellValue(record.getR21_fig_bal_sheet().doubleValue());
						R21Cell1.setCellStyle(numberStyle);
					} else {
						R21Cell1.setCellValue("");
						R21Cell1.setCellStyle(textStyle);
					}

					// R21 Col E
					Cell R21Cell2 = row.createCell(4);
					if (record.getR21_fig_bal_sheet_bwp() != null) {
						R21Cell2.setCellValue(record.getR21_fig_bal_sheet_bwp().doubleValue());
						R21Cell2.setCellStyle(numberStyle);
					} else {
						R21Cell2.setCellValue("");
						R21Cell2.setCellStyle(textStyle);
					}

					// R21 Col F
					Cell R21Cell3 = row.createCell(5);
					if (record.getR21_amt_statement_adj() != null) {
						R21Cell3.setCellValue(record.getR21_amt_statement_adj().doubleValue());
						R21Cell3.setCellStyle(numberStyle);
					} else {
						R21Cell3.setCellValue("");
						R21Cell3.setCellStyle(textStyle);
					}
					// R21 Col G
					Cell R21Cell4 = row.createCell(6);
					if (record.getR21_amt_statement_adj_bwp() != null) {
						R21Cell4.setCellValue(record.getR21_amt_statement_adj_bwp().doubleValue());
						R21Cell4.setCellStyle(numberStyle);
					} else {
						R21Cell4.setCellValue("");
						R21Cell4.setCellStyle(textStyle);
					}
					// // R21 Col H
					// Cell R21Cell5 = row.createCell(7);
					// if (record.getR21_net_amt() != null) {
					// R21Cell5.setCellValue(record.getR21_net_amt().doubleValue());
					// R21Cell5.setCellStyle(numberStyle);
					// } else {
					// R21Cell5.setCellValue("");
					// R21Cell5.setCellStyle(textStyle);
					// }
					// R21 Col I
					Cell R21Cell6 = row.createCell(8);
					if (record.getR21_net_amt_bwp() != null) {
						R21Cell6.setCellValue(record.getR21_net_amt_bwp().doubleValue());
						R21Cell6.setCellStyle(numberStyle);
					} else {
						R21Cell6.setCellValue("");
						R21Cell6.setCellStyle(textStyle);
					}
					// R21 Col J
					Cell R21Cell7 = row.createCell(9);
					if (record.getR21_bal_sub() != null) {
						R21Cell7.setCellValue(record.getR21_bal_sub().doubleValue());
						R21Cell7.setCellStyle(numberStyle);
					} else {
						R21Cell7.setCellValue("");
						R21Cell7.setCellStyle(textStyle);
					}
					// R21 Col K
					Cell R21Cell8 = row.createCell(10);
					if (record.getR21_bal_sub_bwp() != null) {
						R21Cell8.setCellValue(record.getR21_bal_sub_bwp().doubleValue());
						R21Cell8.setCellStyle(numberStyle);
					} else {
						R21Cell8.setCellValue("");
						R21Cell8.setCellStyle(textStyle);
					}
					// R21 Col L
					Cell R21Cell9 = row.createCell(11);
					if (record.getR21_bal_sub_diaries() != null) {
						R21Cell9.setCellValue(record.getR21_bal_sub_diaries().doubleValue());
						R21Cell9.setCellStyle(numberStyle);
					} else {
						R21Cell9.setCellValue("");
						R21Cell9.setCellStyle(textStyle);
					}
					// R21 Col M
					Cell R21Cell10 = row.createCell(12);
					if (record.getR21_bal_sub_diaries_bwp() != null) {
						R21Cell10.setCellValue(record.getR21_bal_sub_diaries_bwp().doubleValue());
						R21Cell10.setCellStyle(numberStyle);
					} else {
						R21Cell10.setCellValue("");
						R21Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(21);
					Cell R22Cell1 = row.createCell(3);
					if (record.getR22_fig_bal_sheet() != null) {
						R22Cell1.setCellValue(record.getR22_fig_bal_sheet().doubleValue());
						R22Cell1.setCellStyle(numberStyle);
					} else {
						R22Cell1.setCellValue("");
						R22Cell1.setCellStyle(textStyle);
					}

					// R22 Col E
					Cell R22Cell2 = row.createCell(4);
					if (record.getR22_fig_bal_sheet_bwp() != null) {
						R22Cell2.setCellValue(record.getR22_fig_bal_sheet_bwp().doubleValue());
						R22Cell2.setCellStyle(numberStyle);
					} else {
						R22Cell2.setCellValue("");
						R22Cell2.setCellStyle(textStyle);
					}

					// R22 Col F
					Cell R22Cell3 = row.createCell(5);
					if (record.getR22_amt_statement_adj() != null) {
						R22Cell3.setCellValue(record.getR22_amt_statement_adj().doubleValue());
						R22Cell3.setCellStyle(numberStyle);
					} else {
						R22Cell3.setCellValue("");
						R22Cell3.setCellStyle(textStyle);
					}
					// R22 Col G
					Cell R22Cell4 = row.createCell(6);
					if (record.getR22_amt_statement_adj_bwp() != null) {
						R22Cell4.setCellValue(record.getR22_amt_statement_adj_bwp().doubleValue());
						R22Cell4.setCellStyle(numberStyle);
					} else {
						R22Cell4.setCellValue("");
						R22Cell4.setCellStyle(textStyle);
					}
					// // R22 Col H
					// Cell R22Cell5 = row.createCell(7);
					// if (record.getR22_net_amt() != null) {
					// R22Cell5.setCellValue(record.getR22_net_amt().doubleValue());
					// R22Cell5.setCellStyle(numberStyle);
					// } else {
					// R22Cell5.setCellValue("");
					// R22Cell5.setCellStyle(textStyle);
					// }
					// R22 Col I
					Cell R22Cell6 = row.createCell(8);
					if (record.getR22_net_amt_bwp() != null) {
						R22Cell6.setCellValue(record.getR22_net_amt_bwp().doubleValue());
						R22Cell6.setCellStyle(numberStyle);
					} else {
						R22Cell6.setCellValue("");
						R22Cell6.setCellStyle(textStyle);
					}
					// R22 Col J
					Cell R22Cell7 = row.createCell(9);
					if (record.getR22_bal_sub() != null) {
						R22Cell7.setCellValue(record.getR22_bal_sub().doubleValue());
						R22Cell7.setCellStyle(numberStyle);
					} else {
						R22Cell7.setCellValue("");
						R22Cell7.setCellStyle(textStyle);
					}
					// R22 Col K
					Cell R22Cell8 = row.createCell(10);
					if (record.getR22_bal_sub_bwp() != null) {
						R22Cell8.setCellValue(record.getR22_bal_sub_bwp().doubleValue());
						R22Cell8.setCellStyle(numberStyle);
					} else {
						R22Cell8.setCellValue("");
						R22Cell8.setCellStyle(textStyle);
					}
					// R22 Col L
					Cell R22Cell9 = row.createCell(11);
					if (record.getR22_bal_sub_diaries() != null) {
						R22Cell9.setCellValue(record.getR22_bal_sub_diaries().doubleValue());
						R22Cell9.setCellStyle(numberStyle);
					} else {
						R22Cell9.setCellValue("");
						R22Cell9.setCellStyle(textStyle);
					}
					// R22 Col M
					Cell R22Cell10 = row.createCell(12);
					if (record.getR22_bal_sub_diaries_bwp() != null) {
						R22Cell10.setCellValue(record.getR22_bal_sub_diaries_bwp().doubleValue());
						R22Cell10.setCellStyle(numberStyle);
					} else {
						R22Cell10.setCellValue("");
						R22Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(22);
					Cell R23Cell1 = row.createCell(3);
					if (record.getR23_fig_bal_sheet() != null) {
						R23Cell1.setCellValue(record.getR23_fig_bal_sheet().doubleValue());
						R23Cell1.setCellStyle(numberStyle);
					} else {
						R23Cell1.setCellValue("");
						R23Cell1.setCellStyle(textStyle);
					}

					// R23 Col E
					Cell R23Cell2 = row.createCell(4);
					if (record.getR23_fig_bal_sheet_bwp() != null) {
						R23Cell2.setCellValue(record.getR23_fig_bal_sheet_bwp().doubleValue());
						R23Cell2.setCellStyle(numberStyle);
					} else {
						R23Cell2.setCellValue("");
						R23Cell2.setCellStyle(textStyle);
					}

					// R23 Col F
					Cell R23Cell3 = row.createCell(5);
					if (record.getR23_amt_statement_adj() != null) {
						R23Cell3.setCellValue(record.getR23_amt_statement_adj().doubleValue());
						R23Cell3.setCellStyle(numberStyle);
					} else {
						R23Cell3.setCellValue("");
						R23Cell3.setCellStyle(textStyle);
					}
					// R23 Col G
					Cell R23Cell4 = row.createCell(6);
					if (record.getR23_amt_statement_adj_bwp() != null) {
						R23Cell4.setCellValue(record.getR23_amt_statement_adj_bwp().doubleValue());
						R23Cell4.setCellStyle(numberStyle);
					} else {
						R23Cell4.setCellValue("");
						R23Cell4.setCellStyle(textStyle);
					}
					// // R23 Col H
					// Cell R23Cell5 = row.createCell(7);
					// if (record.getR23_net_amt() != null) {
					// R23Cell5.setCellValue(record.getR23_net_amt().doubleValue());
					// R23Cell5.setCellStyle(numberStyle);
					// } else {
					// R23Cell5.setCellValue("");
					// R23Cell5.setCellStyle(textStyle);
					// }
					// R23 Col I
					Cell R23Cell6 = row.createCell(8);
					if (record.getR23_net_amt_bwp() != null) {
						R23Cell6.setCellValue(record.getR23_net_amt_bwp().doubleValue());
						R23Cell6.setCellStyle(numberStyle);
					} else {
						R23Cell6.setCellValue("");
						R23Cell6.setCellStyle(textStyle);
					}
					// R23 Col J
					Cell R23Cell7 = row.createCell(9);
					if (record.getR23_bal_sub() != null) {
						R23Cell7.setCellValue(record.getR23_bal_sub().doubleValue());
						R23Cell7.setCellStyle(numberStyle);
					} else {
						R23Cell7.setCellValue("");
						R23Cell7.setCellStyle(textStyle);
					}
					// R23 Col K
					Cell R23Cell8 = row.createCell(10);
					if (record.getR23_bal_sub_bwp() != null) {
						R23Cell8.setCellValue(record.getR23_bal_sub_bwp().doubleValue());
						R23Cell8.setCellStyle(numberStyle);
					} else {
						R23Cell8.setCellValue("");
						R23Cell8.setCellStyle(textStyle);
					}
					// R23 Col L
					Cell R23Cell9 = row.createCell(11);
					if (record.getR23_bal_sub_diaries() != null) {
						R23Cell9.setCellValue(record.getR23_bal_sub_diaries().doubleValue());
						R23Cell9.setCellStyle(numberStyle);
					} else {
						R23Cell9.setCellValue("");
						R23Cell9.setCellStyle(textStyle);
					}
					// R23 Col M
					Cell R23Cell10 = row.createCell(12);
					if (record.getR23_bal_sub_diaries_bwp() != null) {
						R23Cell10.setCellValue(record.getR23_bal_sub_diaries_bwp().doubleValue());
						R23Cell10.setCellStyle(numberStyle);
					} else {
						R23Cell10.setCellValue("");
						R23Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(23);
					Cell R24Cell1 = row.createCell(3);
					if (record.getR24_fig_bal_sheet() != null) {
						R24Cell1.setCellValue(record.getR24_fig_bal_sheet().doubleValue());
						R24Cell1.setCellStyle(numberStyle);
					} else {
						R24Cell1.setCellValue("");
						R24Cell1.setCellStyle(textStyle);
					}

					// R24 Col E
					Cell R24Cell2 = row.createCell(4);
					if (record.getR24_fig_bal_sheet_bwp() != null) {
						R24Cell2.setCellValue(record.getR24_fig_bal_sheet_bwp().doubleValue());
						R24Cell2.setCellStyle(numberStyle);
					} else {
						R24Cell2.setCellValue("");
						R24Cell2.setCellStyle(textStyle);
					}

					// R24 Col F
					Cell R24Cell3 = row.createCell(5);
					if (record.getR24_amt_statement_adj() != null) {
						R24Cell3.setCellValue(record.getR24_amt_statement_adj().doubleValue());
						R24Cell3.setCellStyle(numberStyle);
					} else {
						R24Cell3.setCellValue("");
						R24Cell3.setCellStyle(textStyle);
					}
					// R24 Col G
					Cell R24Cell4 = row.createCell(6);
					if (record.getR24_amt_statement_adj_bwp() != null) {
						R24Cell4.setCellValue(record.getR24_amt_statement_adj_bwp().doubleValue());
						R24Cell4.setCellStyle(numberStyle);
					} else {
						R24Cell4.setCellValue("");
						R24Cell4.setCellStyle(textStyle);
					}
					// // R24 Col H
					// Cell R24Cell5 = row.createCell(7);
					// if (record.getR24_net_amt() != null) {
					// R24Cell5.setCellValue(record.getR24_net_amt().doubleValue());
					// R24Cell5.setCellStyle(numberStyle);
					// } else {
					// R24Cell5.setCellValue("");
					// R24Cell5.setCellStyle(textStyle);
					// }
					// R24 Col I
					Cell R24Cell6 = row.createCell(8);
					if (record.getR24_net_amt_bwp() != null) {
						R24Cell6.setCellValue(record.getR24_net_amt_bwp().doubleValue());
						R24Cell6.setCellStyle(numberStyle);
					} else {
						R24Cell6.setCellValue("");
						R24Cell6.setCellStyle(textStyle);
					}
					// R24 Col J
					Cell R24Cell7 = row.createCell(9);
					if (record.getR24_bal_sub() != null) {
						R24Cell7.setCellValue(record.getR24_bal_sub().doubleValue());
						R24Cell7.setCellStyle(numberStyle);
					} else {
						R24Cell7.setCellValue("");
						R24Cell7.setCellStyle(textStyle);
					}
					// R24 Col K
					Cell R24Cell8 = row.createCell(10);
					if (record.getR24_bal_sub_bwp() != null) {
						R24Cell8.setCellValue(record.getR24_bal_sub_bwp().doubleValue());
						R24Cell8.setCellStyle(numberStyle);
					} else {
						R24Cell8.setCellValue("");
						R24Cell8.setCellStyle(textStyle);
					}
					// R24 Col L
					Cell R24Cell9 = row.createCell(11);
					if (record.getR24_bal_sub_diaries() != null) {
						R24Cell9.setCellValue(record.getR24_bal_sub_diaries().doubleValue());
						R24Cell9.setCellStyle(numberStyle);
					} else {
						R24Cell9.setCellValue("");
						R24Cell9.setCellStyle(textStyle);
					}
					// R24 Col M
					Cell R24Cell10 = row.createCell(12);
					if (record.getR24_bal_sub_diaries_bwp() != null) {
						R24Cell10.setCellValue(record.getR24_bal_sub_diaries_bwp().doubleValue());
						R24Cell10.setCellStyle(numberStyle);
					} else {
						R24Cell10.setCellValue("");
						R24Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(24);
					Cell R25Cell1 = row.createCell(3);
					if (record.getR25_fig_bal_sheet() != null) {
						R25Cell1.setCellValue(record.getR25_fig_bal_sheet().doubleValue());
						R25Cell1.setCellStyle(numberStyle);
					} else {
						R25Cell1.setCellValue("");
						R25Cell1.setCellStyle(textStyle);
					}

					// R25 Col E
					Cell R25Cell2 = row.createCell(4);
					if (record.getR25_fig_bal_sheet_bwp() != null) {
						R25Cell2.setCellValue(record.getR25_fig_bal_sheet_bwp().doubleValue());
						R25Cell2.setCellStyle(numberStyle);
					} else {
						R25Cell2.setCellValue("");
						R25Cell2.setCellStyle(textStyle);
					}

					// R25 Col F
					Cell R25Cell3 = row.createCell(5);
					if (record.getR25_amt_statement_adj() != null) {
						R25Cell3.setCellValue(record.getR25_amt_statement_adj().doubleValue());
						R25Cell3.setCellStyle(numberStyle);
					} else {
						R25Cell3.setCellValue("");
						R25Cell3.setCellStyle(textStyle);
					}
					// R25 Col G
					Cell R25Cell4 = row.createCell(6);
					if (record.getR25_amt_statement_adj_bwp() != null) {
						R25Cell4.setCellValue(record.getR25_amt_statement_adj_bwp().doubleValue());
						R25Cell4.setCellStyle(numberStyle);
					} else {
						R25Cell4.setCellValue("");
						R25Cell4.setCellStyle(textStyle);
					}
					// // R25 Col H
					// Cell R25Cell5 = row.createCell(7);
					// if (record.getR25_net_amt() != null) {
					// R25Cell5.setCellValue(record.getR25_net_amt().doubleValue());
					// R25Cell5.setCellStyle(numberStyle);
					// } else {
					// R25Cell5.setCellValue("");
					// R25Cell5.setCellStyle(textStyle);
					// }
					// R25 Col I
					Cell R25Cell6 = row.createCell(8);
					if (record.getR25_net_amt_bwp() != null) {
						R25Cell6.setCellValue(record.getR25_net_amt_bwp().doubleValue());
						R25Cell6.setCellStyle(numberStyle);
					} else {
						R25Cell6.setCellValue("");
						R25Cell6.setCellStyle(textStyle);
					}
					// R25 Col J
					Cell R25Cell7 = row.createCell(9);
					if (record.getR25_bal_sub() != null) {
						R25Cell7.setCellValue(record.getR25_bal_sub().doubleValue());
						R25Cell7.setCellStyle(numberStyle);
					} else {
						R25Cell7.setCellValue("");
						R25Cell7.setCellStyle(textStyle);
					}
					// R25 Col K
					Cell R25Cell8 = row.createCell(10);
					if (record.getR25_bal_sub_bwp() != null) {
						R25Cell8.setCellValue(record.getR25_bal_sub_bwp().doubleValue());
						R25Cell8.setCellStyle(numberStyle);
					} else {
						R25Cell8.setCellValue("");
						R25Cell8.setCellStyle(textStyle);
					}
					// R25 Col L
					Cell R25Cell9 = row.createCell(11);
					if (record.getR25_bal_sub_diaries() != null) {
						R25Cell9.setCellValue(record.getR25_bal_sub_diaries().doubleValue());
						R25Cell9.setCellStyle(numberStyle);
					} else {
						R25Cell9.setCellValue("");
						R25Cell9.setCellStyle(textStyle);
					}
					// R25 Col M
					Cell R25Cell10 = row.createCell(12);
					if (record.getR25_bal_sub_diaries_bwp() != null) {
						R25Cell10.setCellValue(record.getR25_bal_sub_diaries_bwp().doubleValue());
						R25Cell10.setCellStyle(numberStyle);
					} else {
						R25Cell10.setCellValue("");
						R25Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(25);
					Cell R26Cell1 = row.createCell(3);
					if (record.getR26_fig_bal_sheet() != null) {
						R26Cell1.setCellValue(record.getR26_fig_bal_sheet().doubleValue());
						R26Cell1.setCellStyle(numberStyle);
					} else {
						R26Cell1.setCellValue("");
						R26Cell1.setCellStyle(textStyle);
					}

					// R26 Col E
					Cell R26Cell2 = row.createCell(4);
					if (record.getR26_fig_bal_sheet_bwp() != null) {
						R26Cell2.setCellValue(record.getR26_fig_bal_sheet_bwp().doubleValue());
						R26Cell2.setCellStyle(numberStyle);
					} else {
						R26Cell2.setCellValue("");
						R26Cell2.setCellStyle(textStyle);
					}

					// R26 Col F
					Cell R26Cell3 = row.createCell(5);
					if (record.getR26_amt_statement_adj() != null) {
						R26Cell3.setCellValue(record.getR26_amt_statement_adj().doubleValue());
						R26Cell3.setCellStyle(numberStyle);
					} else {
						R26Cell3.setCellValue("");
						R26Cell3.setCellStyle(textStyle);
					}
					// R26 Col G
					Cell R26Cell4 = row.createCell(6);
					if (record.getR26_amt_statement_adj_bwp() != null) {
						R26Cell4.setCellValue(record.getR26_amt_statement_adj_bwp().doubleValue());
						R26Cell4.setCellStyle(numberStyle);
					} else {
						R26Cell4.setCellValue("");
						R26Cell4.setCellStyle(textStyle);
					}
					// // R26 Col H
					// Cell R26Cell5 = row.createCell(7);
					// if (record.getR26_net_amt() != null) {
					// R26Cell5.setCellValue(record.getR26_net_amt().doubleValue());
					// R26Cell5.setCellStyle(numberStyle);
					// } else {
					// R26Cell5.setCellValue("");
					// R26Cell5.setCellStyle(textStyle);
					// }
					// R26 Col I
					Cell R26Cell6 = row.createCell(8);
					if (record.getR26_net_amt_bwp() != null) {
						R26Cell6.setCellValue(record.getR26_net_amt_bwp().doubleValue());
						R26Cell6.setCellStyle(numberStyle);
					} else {
						R26Cell6.setCellValue("");
						R26Cell6.setCellStyle(textStyle);
					}
					// R26 Col J
					Cell R26Cell7 = row.createCell(9);
					if (record.getR26_bal_sub() != null) {
						R26Cell7.setCellValue(record.getR26_bal_sub().doubleValue());
						R26Cell7.setCellStyle(numberStyle);
					} else {
						R26Cell7.setCellValue("");
						R26Cell7.setCellStyle(textStyle);
					}
					// R26 Col K
					Cell R26Cell8 = row.createCell(10);
					if (record.getR26_bal_sub_bwp() != null) {
						R26Cell8.setCellValue(record.getR26_bal_sub_bwp().doubleValue());
						R26Cell8.setCellStyle(numberStyle);
					} else {
						R26Cell8.setCellValue("");
						R26Cell8.setCellStyle(textStyle);
					}
					// R26 Col L
					Cell R26Cell9 = row.createCell(11);
					if (record.getR26_bal_sub_diaries() != null) {
						R26Cell9.setCellValue(record.getR26_bal_sub_diaries().doubleValue());
						R26Cell9.setCellStyle(numberStyle);
					} else {
						R26Cell9.setCellValue("");
						R26Cell9.setCellStyle(textStyle);
					}
					// R26 Col M
					Cell R26Cell10 = row.createCell(12);
					if (record.getR26_bal_sub_diaries_bwp() != null) {
						R26Cell10.setCellValue(record.getR26_bal_sub_diaries_bwp().doubleValue());
						R26Cell10.setCellStyle(numberStyle);
					} else {
						R26Cell10.setCellValue("");
						R26Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(26);
					Cell R27Cell1 = row.createCell(3);
					if (record.getR27_fig_bal_sheet() != null) {
						R27Cell1.setCellValue(record.getR27_fig_bal_sheet().doubleValue());
						R27Cell1.setCellStyle(numberStyle);
					} else {
						R27Cell1.setCellValue("");
						R27Cell1.setCellStyle(textStyle);
					}

					// R27 Col E
					Cell R27Cell2 = row.createCell(4);
					if (record.getR27_fig_bal_sheet_bwp() != null) {
						R27Cell2.setCellValue(record.getR27_fig_bal_sheet_bwp().doubleValue());
						R27Cell2.setCellStyle(numberStyle);
					} else {
						R27Cell2.setCellValue("");
						R27Cell2.setCellStyle(textStyle);
					}

					// R27 Col F
					Cell R27Cell3 = row.createCell(5);
					if (record.getR27_amt_statement_adj() != null) {
						R27Cell3.setCellValue(record.getR27_amt_statement_adj().doubleValue());
						R27Cell3.setCellStyle(numberStyle);
					} else {
						R27Cell3.setCellValue("");
						R27Cell3.setCellStyle(textStyle);
					}
					// R27 Col G
					Cell R27Cell4 = row.createCell(6);
					if (record.getR27_amt_statement_adj_bwp() != null) {
						R27Cell4.setCellValue(record.getR27_amt_statement_adj_bwp().doubleValue());
						R27Cell4.setCellStyle(numberStyle);
					} else {
						R27Cell4.setCellValue("");
						R27Cell4.setCellStyle(textStyle);
					}
					// // R27 Col H
					// Cell R27Cell5 = row.createCell(7);
					// if (record.getR27_net_amt() != null) {
					// R27Cell5.setCellValue(record.getR27_net_amt().doubleValue());
					// R27Cell5.setCellStyle(numberStyle);
					// } else {
					// R27Cell5.setCellValue("");
					// R27Cell5.setCellStyle(textStyle);
					// }
					// R27 Col I
					Cell R27Cell6 = row.createCell(8);
					if (record.getR27_net_amt_bwp() != null) {
						R27Cell6.setCellValue(record.getR27_net_amt_bwp().doubleValue());
						R27Cell6.setCellStyle(numberStyle);
					} else {
						R27Cell6.setCellValue("");
						R27Cell6.setCellStyle(textStyle);
					}
					// R27 Col J
					Cell R27Cell7 = row.createCell(9);
					if (record.getR27_bal_sub() != null) {
						R27Cell7.setCellValue(record.getR27_bal_sub().doubleValue());
						R27Cell7.setCellStyle(numberStyle);
					} else {
						R27Cell7.setCellValue("");
						R27Cell7.setCellStyle(textStyle);
					}
					// R27 Col K
					Cell R27Cell8 = row.createCell(10);
					if (record.getR27_bal_sub_bwp() != null) {
						R27Cell8.setCellValue(record.getR27_bal_sub_bwp().doubleValue());
						R27Cell8.setCellStyle(numberStyle);
					} else {
						R27Cell8.setCellValue("");
						R27Cell8.setCellStyle(textStyle);
					}
					// R27 Col L
					Cell R27Cell9 = row.createCell(11);
					if (record.getR27_bal_sub_diaries() != null) {
						R27Cell9.setCellValue(record.getR27_bal_sub_diaries().doubleValue());
						R27Cell9.setCellStyle(numberStyle);
					} else {
						R27Cell9.setCellValue("");
						R27Cell9.setCellStyle(textStyle);
					}
					// R27 Col M
					Cell R27Cell10 = row.createCell(12);
					if (record.getR27_bal_sub_diaries_bwp() != null) {
						R27Cell10.setCellValue(record.getR27_bal_sub_diaries_bwp().doubleValue());
						R27Cell10.setCellStyle(numberStyle);
					} else {
						R27Cell10.setCellValue("");
						R27Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(27);
					Cell R28Cell1 = row.createCell(3);
					if (record.getR28_fig_bal_sheet() != null) {
						R28Cell1.setCellValue(record.getR28_fig_bal_sheet().doubleValue());
						R28Cell1.setCellStyle(numberStyle);
					} else {
						R28Cell1.setCellValue("");
						R28Cell1.setCellStyle(textStyle);
					}

					// R28 Col E
					Cell R28Cell2 = row.createCell(4);
					if (record.getR28_fig_bal_sheet_bwp() != null) {
						R28Cell2.setCellValue(record.getR28_fig_bal_sheet_bwp().doubleValue());
						R28Cell2.setCellStyle(numberStyle);
					} else {
						R28Cell2.setCellValue("");
						R28Cell2.setCellStyle(textStyle);
					}

					// R28 Col F
					Cell R28Cell3 = row.createCell(5);
					if (record.getR28_amt_statement_adj() != null) {
						R28Cell3.setCellValue(record.getR28_amt_statement_adj().doubleValue());
						R28Cell3.setCellStyle(numberStyle);
					} else {
						R28Cell3.setCellValue("");
						R28Cell3.setCellStyle(textStyle);
					}
					// R28 Col G
					Cell R28Cell4 = row.createCell(6);
					if (record.getR28_amt_statement_adj_bwp() != null) {
						R28Cell4.setCellValue(record.getR28_amt_statement_adj_bwp().doubleValue());
						R28Cell4.setCellStyle(numberStyle);
					} else {
						R28Cell4.setCellValue("");
						R28Cell4.setCellStyle(textStyle);
					}
					// // R28 Col H
					// Cell R28Cell5 = row.createCell(7);
					// if (record.getR28_net_amt() != null) {
					// R28Cell5.setCellValue(record.getR28_net_amt().doubleValue());
					// R28Cell5.setCellStyle(numberStyle);
					// } else {
					// R28Cell5.setCellValue("");
					// R28Cell5.setCellStyle(textStyle);
					// }
					// R28 Col I
					Cell R28Cell6 = row.createCell(8);
					if (record.getR28_net_amt_bwp() != null) {
						R28Cell6.setCellValue(record.getR28_net_amt_bwp().doubleValue());
						R28Cell6.setCellStyle(numberStyle);
					} else {
						R28Cell6.setCellValue("");
						R28Cell6.setCellStyle(textStyle);
					}
					// R28 Col J
					Cell R28Cell7 = row.createCell(9);
					if (record.getR28_bal_sub() != null) {
						R28Cell7.setCellValue(record.getR28_bal_sub().doubleValue());
						R28Cell7.setCellStyle(numberStyle);
					} else {
						R28Cell7.setCellValue("");
						R28Cell7.setCellStyle(textStyle);
					}
					// R28 Col K
					Cell R28Cell8 = row.createCell(10);
					if (record.getR28_bal_sub_bwp() != null) {
						R28Cell8.setCellValue(record.getR28_bal_sub_bwp().doubleValue());
						R28Cell8.setCellStyle(numberStyle);
					} else {
						R28Cell8.setCellValue("");
						R28Cell8.setCellStyle(textStyle);
					}
					// R28 Col L
					Cell R28Cell9 = row.createCell(11);
					if (record.getR28_bal_sub_diaries() != null) {
						R28Cell9.setCellValue(record.getR28_bal_sub_diaries().doubleValue());
						R28Cell9.setCellStyle(numberStyle);
					} else {
						R28Cell9.setCellValue("");
						R28Cell9.setCellStyle(textStyle);
					}
					// R28 Col M
					Cell R28Cell10 = row.createCell(12);
					if (record.getR28_bal_sub_diaries_bwp() != null) {
						R28Cell10.setCellValue(record.getR28_bal_sub_diaries_bwp().doubleValue());
						R28Cell10.setCellStyle(numberStyle);
					} else {
						R28Cell10.setCellValue("");
						R28Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(28);
					Cell R29Cell1 = row.createCell(3);
					if (record.getR29_fig_bal_sheet() != null) {
						R29Cell1.setCellValue(record.getR29_fig_bal_sheet().doubleValue());
						R29Cell1.setCellStyle(numberStyle);
					} else {
						R29Cell1.setCellValue("");
						R29Cell1.setCellStyle(textStyle);
					}

					// R29 Col E
					Cell R29Cell2 = row.createCell(4);
					if (record.getR29_fig_bal_sheet_bwp() != null) {
						R29Cell2.setCellValue(record.getR29_fig_bal_sheet_bwp().doubleValue());
						R29Cell2.setCellStyle(numberStyle);
					} else {
						R29Cell2.setCellValue("");
						R29Cell2.setCellStyle(textStyle);
					}

					// R29 Col F
					Cell R29Cell3 = row.createCell(5);
					if (record.getR29_amt_statement_adj() != null) {
						R29Cell3.setCellValue(record.getR29_amt_statement_adj().doubleValue());
						R29Cell3.setCellStyle(numberStyle);
					} else {
						R29Cell3.setCellValue("");
						R29Cell3.setCellStyle(textStyle);
					}
					// R29 Col G
					Cell R29Cell4 = row.createCell(6);
					if (record.getR29_amt_statement_adj_bwp() != null) {
						R29Cell4.setCellValue(record.getR29_amt_statement_adj_bwp().doubleValue());
						R29Cell4.setCellStyle(numberStyle);
					} else {
						R29Cell4.setCellValue("");
						R29Cell4.setCellStyle(textStyle);
					}
					// // R29 Col H
					// Cell R29Cell5 = row.createCell(7);
					// if (record.getR29_net_amt() != null) {
					// R29Cell5.setCellValue(record.getR29_net_amt().doubleValue());
					// R29Cell5.setCellStyle(numberStyle);
					// } else {
					// R29Cell5.setCellValue("");
					// R29Cell5.setCellStyle(textStyle);
					// }
					// R29 Col I
					Cell R29Cell6 = row.createCell(8);
					if (record.getR29_net_amt_bwp() != null) {
						R29Cell6.setCellValue(record.getR29_net_amt_bwp().doubleValue());
						R29Cell6.setCellStyle(numberStyle);
					} else {
						R29Cell6.setCellValue("");
						R29Cell6.setCellStyle(textStyle);
					}
					// R29 Col J
					Cell R29Cell7 = row.createCell(9);
					if (record.getR29_bal_sub() != null) {
						R29Cell7.setCellValue(record.getR29_bal_sub().doubleValue());
						R29Cell7.setCellStyle(numberStyle);
					} else {
						R29Cell7.setCellValue("");
						R29Cell7.setCellStyle(textStyle);
					}
					// R29 Col K
					Cell R29Cell8 = row.createCell(10);
					if (record.getR29_bal_sub_bwp() != null) {
						R29Cell8.setCellValue(record.getR29_bal_sub_bwp().doubleValue());
						R29Cell8.setCellStyle(numberStyle);
					} else {
						R29Cell8.setCellValue("");
						R29Cell8.setCellStyle(textStyle);
					}
					// R29 Col L
					Cell R29Cell9 = row.createCell(11);
					if (record.getR29_bal_sub_diaries() != null) {
						R29Cell9.setCellValue(record.getR29_bal_sub_diaries().doubleValue());
						R29Cell9.setCellStyle(numberStyle);
					} else {
						R29Cell9.setCellValue("");
						R29Cell9.setCellStyle(textStyle);
					}
					// R29 Col M
					Cell R29Cell10 = row.createCell(12);
					if (record.getR29_bal_sub_diaries_bwp() != null) {
						R29Cell10.setCellValue(record.getR29_bal_sub_diaries_bwp().doubleValue());
						R29Cell10.setCellStyle(numberStyle);
					} else {
						R29Cell10.setCellValue("");
						R29Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(29);
					Cell R30Cell1 = row.createCell(3);
					if (record.getR30_fig_bal_sheet() != null) {
						R30Cell1.setCellValue(record.getR30_fig_bal_sheet().doubleValue());
						R30Cell1.setCellStyle(numberStyle);
					} else {
						R30Cell1.setCellValue("");
						R30Cell1.setCellStyle(textStyle);
					}

					// R30 Col E
					Cell R30Cell2 = row.createCell(4);
					if (record.getR30_fig_bal_sheet_bwp() != null) {
						R30Cell2.setCellValue(record.getR30_fig_bal_sheet_bwp().doubleValue());
						R30Cell2.setCellStyle(numberStyle);
					} else {
						R30Cell2.setCellValue("");
						R30Cell2.setCellStyle(textStyle);
					}

					// R30 Col F
					Cell R30Cell3 = row.createCell(5);
					if (record.getR30_amt_statement_adj() != null) {
						R30Cell3.setCellValue(record.getR30_amt_statement_adj().doubleValue());
						R30Cell3.setCellStyle(numberStyle);
					} else {
						R30Cell3.setCellValue("");
						R30Cell3.setCellStyle(textStyle);
					}
					// R30 Col G
					Cell R30Cell4 = row.createCell(6);
					if (record.getR30_amt_statement_adj_bwp() != null) {
						R30Cell4.setCellValue(record.getR30_amt_statement_adj_bwp().doubleValue());
						R30Cell4.setCellStyle(numberStyle);
					} else {
						R30Cell4.setCellValue("");
						R30Cell4.setCellStyle(textStyle);
					}
					// // R30 Col H
					// Cell R30Cell5 = row.createCell(7);
					// if (record.getR30_net_amt() != null) {
					// R30Cell5.setCellValue(record.getR30_net_amt().doubleValue());
					// R30Cell5.setCellStyle(numberStyle);
					// } else {
					// R30Cell5.setCellValue("");
					// R30Cell5.setCellStyle(textStyle);
					// }
					// R30 Col I
					Cell R30Cell6 = row.createCell(8);
					if (record.getR30_net_amt_bwp() != null) {
						R30Cell6.setCellValue(record.getR30_net_amt_bwp().doubleValue());
						R30Cell6.setCellStyle(numberStyle);
					} else {
						R30Cell6.setCellValue("");
						R30Cell6.setCellStyle(textStyle);
					}
					// R30 Col J
					Cell R30Cell7 = row.createCell(9);
					if (record.getR30_bal_sub() != null) {
						R30Cell7.setCellValue(record.getR30_bal_sub().doubleValue());
						R30Cell7.setCellStyle(numberStyle);
					} else {
						R30Cell7.setCellValue("");
						R30Cell7.setCellStyle(textStyle);
					}
					// R30 Col K
					Cell R30Cell8 = row.createCell(10);
					if (record.getR30_bal_sub_bwp() != null) {
						R30Cell8.setCellValue(record.getR30_bal_sub_bwp().doubleValue());
						R30Cell8.setCellStyle(numberStyle);
					} else {
						R30Cell8.setCellValue("");
						R30Cell8.setCellStyle(textStyle);
					}
					// R30 Col L
					Cell R30Cell9 = row.createCell(11);
					if (record.getR30_bal_sub_diaries() != null) {
						R30Cell9.setCellValue(record.getR30_bal_sub_diaries().doubleValue());
						R30Cell9.setCellStyle(numberStyle);
					} else {
						R30Cell9.setCellValue("");
						R30Cell9.setCellStyle(textStyle);
					}
					// R30 Col M
					Cell R30Cell10 = row.createCell(12);
					if (record.getR30_bal_sub_diaries_bwp() != null) {
						R30Cell10.setCellValue(record.getR30_bal_sub_diaries_bwp().doubleValue());
						R30Cell10.setCellStyle(numberStyle);
					} else {
						R30Cell10.setCellValue("");
						R30Cell10.setCellStyle(textStyle);
					}
					// row = sheet.getRow(30);
					// Cell R31Cell1 = row.createCell(3);
					// if (record.getR31_fig_bal_sheet() != null) {
					// R31Cell1.setCellValue(record.getR31_fig_bal_sheet().doubleValue());
					// R31Cell1.setCellStyle(numberStyle);
					// } else {
					// R31Cell1.setCellValue("");
					// R31Cell1.setCellStyle(textStyle);
					// }

					// // R31 Col E
					// Cell R31Cell2 = row.createCell(4);
					// if (record.getR31_fig_bal_sheet_bwp() != null) {
					// R31Cell2.setCellValue(record.getR31_fig_bal_sheet_bwp().doubleValue());
					// R31Cell2.setCellStyle(numberStyle);
					// } else {
					// R31Cell2.setCellValue("");
					// R31Cell2.setCellStyle(textStyle);
					// }

					// // R31 Col F
					// Cell R31Cell3 = row.createCell(5);
					// if (record.getR31_amt_statement_adj() != null) {
					// R31Cell3.setCellValue(record.getR31_amt_statement_adj().doubleValue());
					// R31Cell3.setCellStyle(numberStyle);
					// } else {
					// R31Cell3.setCellValue("");
					// R31Cell3.setCellStyle(textStyle);
					// }
					// // R31 Col G
					// Cell R31Cell4 = row.createCell(6);
					// if (record.getR31_amt_statement_adj_bwp() != null) {
					// R31Cell4.setCellValue(record.getR31_amt_statement_adj_bwp().doubleValue());
					// R31Cell4.setCellStyle(numberStyle);
					// } else {
					// R31Cell4.setCellValue("");
					// R31Cell4.setCellStyle(textStyle);
					// }
					// // R31 Col H
					// Cell R31Cell5 = row.createCell(7);
					// if (record.getR31_net_amt() != null) {
					// R31Cell5.setCellValue(record.getR31_net_amt().doubleValue());
					// R31Cell5.setCellStyle(numberStyle);
					// } else {
					// R31Cell5.setCellValue("");
					// R31Cell5.setCellStyle(textStyle);
					// }
					// // R31 Col I
					// Cell R31Cell6 = row.createCell(8);
					// if (record.getR31_net_amt_bwp() != null) {
					// R31Cell6.setCellValue(record.getR31_net_amt_bwp().doubleValue());
					// R31Cell6.setCellStyle(numberStyle);
					// } else {
					// R31Cell6.setCellValue("");
					// R31Cell6.setCellStyle(textStyle);
					// }
					// // R31 Col J
					// Cell R31Cell7 = row.createCell(9);
					// if (record.getR31_bal_sub() != null) {
					// R31Cell7.setCellValue(record.getR31_bal_sub().doubleValue());
					// R31Cell7.setCellStyle(numberStyle);
					// } else {
					// R31Cell7.setCellValue("");
					// R31Cell7.setCellStyle(textStyle);
					// }
					// // R31 Col K
					// Cell R31Cell8 = row.createCell(10);
					// if (record.getR31_bal_sub_bwp() != null) {
					// R31Cell8.setCellValue(record.getR31_bal_sub_bwp().doubleValue());
					// R31Cell8.setCellStyle(numberStyle);
					// } else {
					// R31Cell8.setCellValue("");
					// R31Cell8.setCellStyle(textStyle);
					// }
					// // R31 Col L
					// Cell R31Cell9 = row.createCell(11);
					// if (record.getR31_bal_sub_diaries() != null) {
					// R31Cell9.setCellValue(record.getR31_bal_sub_diaries().doubleValue());
					// R31Cell9.setCellStyle(numberStyle);
					// } else {
					// R31Cell9.setCellValue("");
					// R31Cell9.setCellStyle(textStyle);
					// }
					// // R31 Col M
					// Cell R31Cell10 = row.createCell(12);
					// if (record.getR31_bal_sub_diaries_bwp() != null) {
					// R31Cell10.setCellValue(record.getR31_bal_sub_diaries_bwp().doubleValue());
					// R31Cell10.setCellStyle(numberStyle);
					// } else {
					// R31Cell10.setCellValue("");
					// R31Cell10.setCellStyle(textStyle);
					// }
					row = sheet.getRow(39);
					Cell R40Cell1 = row.createCell(3);
					if (record.getR40_fig_bal_sheet() != null) {
						R40Cell1.setCellValue(record.getR40_fig_bal_sheet().doubleValue());
						R40Cell1.setCellStyle(numberStyle);
					} else {
						R40Cell1.setCellValue("");
						R40Cell1.setCellStyle(textStyle);
					}

					// R40 Col E
					Cell R40Cell2 = row.createCell(4);
					if (record.getR40_fig_bal_sheet_bwp() != null) {
						R40Cell2.setCellValue(record.getR40_fig_bal_sheet_bwp().doubleValue());
						R40Cell2.setCellStyle(numberStyle);
					} else {
						R40Cell2.setCellValue("");
						R40Cell2.setCellStyle(textStyle);
					}

					// R40 Col F
					Cell R40Cell3 = row.createCell(5);
					if (record.getR40_amt_statement_adj() != null) {
						R40Cell3.setCellValue(record.getR40_amt_statement_adj().doubleValue());
						R40Cell3.setCellStyle(numberStyle);
					} else {
						R40Cell3.setCellValue("");
						R40Cell3.setCellStyle(textStyle);
					}
					// R40 Col G
					Cell R40Cell4 = row.createCell(6);
					if (record.getR40_amt_statement_adj_bwp() != null) {
						R40Cell4.setCellValue(record.getR40_amt_statement_adj_bwp().doubleValue());
						R40Cell4.setCellStyle(numberStyle);
					} else {
						R40Cell4.setCellValue("");
						R40Cell4.setCellStyle(textStyle);
					}
					// // R40 Col H
					// Cell R40Cell5 = row.createCell(7);
					// if (record.getR40_net_amt() != null) {
					// R40Cell5.setCellValue(record.getR40_net_amt().doubleValue());
					// R40Cell5.setCellStyle(numberStyle);
					// } else {
					// R40Cell5.setCellValue("");
					// R40Cell5.setCellStyle(textStyle);
					// }
					// R40 Col I
					Cell R40Cell6 = row.createCell(8);
					if (record.getR40_net_amt_bwp() != null) {
						R40Cell6.setCellValue(record.getR40_net_amt_bwp().doubleValue());
						R40Cell6.setCellStyle(numberStyle);
					} else {
						R40Cell6.setCellValue("");
						R40Cell6.setCellStyle(textStyle);
					}
					// R40 Col J
					Cell R40Cell7 = row.createCell(9);
					if (record.getR40_bal_sub() != null) {
						R40Cell7.setCellValue(record.getR40_bal_sub().doubleValue());
						R40Cell7.setCellStyle(numberStyle);
					} else {
						R40Cell7.setCellValue("");
						R40Cell7.setCellStyle(textStyle);
					}
					// R40 Col K
					Cell R40Cell8 = row.createCell(10);
					if (record.getR40_bal_sub_bwp() != null) {
						R40Cell8.setCellValue(record.getR40_bal_sub_bwp().doubleValue());
						R40Cell8.setCellStyle(numberStyle);
					} else {
						R40Cell8.setCellValue("");
						R40Cell8.setCellStyle(textStyle);
					}
					// R40 Col L
					Cell R40Cell9 = row.createCell(11);
					if (record.getR40_bal_sub_diaries() != null) {
						R40Cell9.setCellValue(record.getR40_bal_sub_diaries().doubleValue());
						R40Cell9.setCellStyle(numberStyle);
					} else {
						R40Cell9.setCellValue("");
						R40Cell9.setCellStyle(textStyle);
					}
					// R40 Col M
					Cell R40Cell10 = row.createCell(12);
					if (record.getR40_bal_sub_diaries_bwp() != null) {
						R40Cell10.setCellValue(record.getR40_bal_sub_diaries_bwp().doubleValue());
						R40Cell10.setCellStyle(numberStyle);
					} else {
						R40Cell10.setCellValue("");
						R40Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(40);
					Cell R41Cell1 = row.createCell(3);
					if (record.getR41_fig_bal_sheet() != null) {
						R41Cell1.setCellValue(record.getR41_fig_bal_sheet().doubleValue());
						R41Cell1.setCellStyle(numberStyle);
					} else {
						R41Cell1.setCellValue("");
						R41Cell1.setCellStyle(textStyle);
					}

					// R41 Col E
					Cell R41Cell2 = row.createCell(4);
					if (record.getR41_fig_bal_sheet_bwp() != null) {
						R41Cell2.setCellValue(record.getR41_fig_bal_sheet_bwp().doubleValue());
						R41Cell2.setCellStyle(numberStyle);
					} else {
						R41Cell2.setCellValue("");
						R41Cell2.setCellStyle(textStyle);
					}

					// R41 Col F
					Cell R41Cell3 = row.createCell(5);
					if (record.getR41_amt_statement_adj() != null) {
						R41Cell3.setCellValue(record.getR41_amt_statement_adj().doubleValue());
						R41Cell3.setCellStyle(numberStyle);
					} else {
						R41Cell3.setCellValue("");
						R41Cell3.setCellStyle(textStyle);
					}
					// R41 Col G
					Cell R41Cell4 = row.createCell(6);
					if (record.getR41_amt_statement_adj_bwp() != null) {
						R41Cell4.setCellValue(record.getR41_amt_statement_adj_bwp().doubleValue());
						R41Cell4.setCellStyle(numberStyle);
					} else {
						R41Cell4.setCellValue("");
						R41Cell4.setCellStyle(textStyle);
					}
					// // R41 Col H
					// Cell R41Cell5 = row.createCell(7);
					// if (record.getR41_net_amt() != null) {
					// R41Cell5.setCellValue(record.getR41_net_amt().doubleValue());
					// R41Cell5.setCellStyle(numberStyle);
					// } else {
					// R41Cell5.setCellValue("");
					// R41Cell5.setCellStyle(textStyle);
					// }
					// R41 Col I
					Cell R41Cell6 = row.createCell(8);
					if (record.getR41_net_amt_bwp() != null) {
						R41Cell6.setCellValue(record.getR41_net_amt_bwp().doubleValue());
						R41Cell6.setCellStyle(numberStyle);
					} else {
						R41Cell6.setCellValue("");
						R41Cell6.setCellStyle(textStyle);
					}
					// R41 Col J
					Cell R41Cell7 = row.createCell(9);
					if (record.getR41_bal_sub() != null) {
						R41Cell7.setCellValue(record.getR41_bal_sub().doubleValue());
						R41Cell7.setCellStyle(numberStyle);
					} else {
						R41Cell7.setCellValue("");
						R41Cell7.setCellStyle(textStyle);
					}
					// R41 Col K
					Cell R41Cell8 = row.createCell(10);
					if (record.getR41_bal_sub_bwp() != null) {
						R41Cell8.setCellValue(record.getR41_bal_sub_bwp().doubleValue());
						R41Cell8.setCellStyle(numberStyle);
					} else {
						R41Cell8.setCellValue("");
						R41Cell8.setCellStyle(textStyle);
					}
					// R41 Col L
					Cell R41Cell9 = row.createCell(11);
					if (record.getR41_bal_sub_diaries() != null) {
						R41Cell9.setCellValue(record.getR41_bal_sub_diaries().doubleValue());
						R41Cell9.setCellStyle(numberStyle);
					} else {
						R41Cell9.setCellValue("");
						R41Cell9.setCellStyle(textStyle);
					}
					// R41 Col M
					Cell R41Cell10 = row.createCell(12);
					if (record.getR41_bal_sub_diaries_bwp() != null) {
						R41Cell10.setCellValue(record.getR41_bal_sub_diaries_bwp().doubleValue());
						R41Cell10.setCellStyle(numberStyle);
					} else {
						R41Cell10.setCellValue("");
						R41Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(41);
					Cell R42Cell1 = row.createCell(3);
					if (record.getR42_fig_bal_sheet() != null) {
						R42Cell1.setCellValue(record.getR42_fig_bal_sheet().doubleValue());
						R42Cell1.setCellStyle(numberStyle);
					} else {
						R42Cell1.setCellValue("");
						R42Cell1.setCellStyle(textStyle);
					}

					// R42 Col E
					Cell R42Cell2 = row.createCell(4);
					if (record.getR42_fig_bal_sheet_bwp() != null) {
						R42Cell2.setCellValue(record.getR42_fig_bal_sheet_bwp().doubleValue());
						R42Cell2.setCellStyle(numberStyle);
					} else {
						R42Cell2.setCellValue("");
						R42Cell2.setCellStyle(textStyle);
					}

					// R42 Col F
					Cell R42Cell3 = row.createCell(5);
					if (record.getR42_amt_statement_adj() != null) {
						R42Cell3.setCellValue(record.getR42_amt_statement_adj().doubleValue());
						R42Cell3.setCellStyle(numberStyle);
					} else {
						R42Cell3.setCellValue("");
						R42Cell3.setCellStyle(textStyle);
					}
					// R42 Col G
					Cell R42Cell4 = row.createCell(6);
					if (record.getR42_amt_statement_adj_bwp() != null) {
						R42Cell4.setCellValue(record.getR42_amt_statement_adj_bwp().doubleValue());
						R42Cell4.setCellStyle(numberStyle);
					} else {
						R42Cell4.setCellValue("");
						R42Cell4.setCellStyle(textStyle);
					}
					// // R42 Col H
					// Cell R42Cell5 = row.createCell(7);
					// if (record.getR42_net_amt() != null) {
					// R42Cell5.setCellValue(record.getR42_net_amt().doubleValue());
					// R42Cell5.setCellStyle(numberStyle);
					// } else {
					// R42Cell5.setCellValue("");
					// R42Cell5.setCellStyle(textStyle);
					// }
					// R42 Col I
					Cell R42Cell6 = row.createCell(8);
					if (record.getR42_net_amt_bwp() != null) {
						R42Cell6.setCellValue(record.getR42_net_amt_bwp().doubleValue());
						R42Cell6.setCellStyle(numberStyle);
					} else {
						R42Cell6.setCellValue("");
						R42Cell6.setCellStyle(textStyle);
					}
					// R42 Col J
					Cell R42Cell7 = row.createCell(9);
					if (record.getR42_bal_sub() != null) {
						R42Cell7.setCellValue(record.getR42_bal_sub().doubleValue());
						R42Cell7.setCellStyle(numberStyle);
					} else {
						R42Cell7.setCellValue("");
						R42Cell7.setCellStyle(textStyle);
					}
					// R42 Col K
					Cell R42Cell8 = row.createCell(10);
					if (record.getR42_bal_sub_bwp() != null) {
						R42Cell8.setCellValue(record.getR42_bal_sub_bwp().doubleValue());
						R42Cell8.setCellStyle(numberStyle);
					} else {
						R42Cell8.setCellValue("");
						R42Cell8.setCellStyle(textStyle);
					}
					// R42 Col L
					Cell R42Cell9 = row.createCell(11);
					if (record.getR42_bal_sub_diaries() != null) {
						R42Cell9.setCellValue(record.getR42_bal_sub_diaries().doubleValue());
						R42Cell9.setCellStyle(numberStyle);
					} else {
						R42Cell9.setCellValue("");
						R42Cell9.setCellStyle(textStyle);
					}
					// R42 Col M
					Cell R42Cell10 = row.createCell(12);
					if (record.getR42_bal_sub_diaries_bwp() != null) {
						R42Cell10.setCellValue(record.getR42_bal_sub_diaries_bwp().doubleValue());
						R42Cell10.setCellStyle(numberStyle);
					} else {
						R42Cell10.setCellValue("");
						R42Cell10.setCellStyle(textStyle);
					}
					// row = sheet.getRow(42);
					// Cell R43Cell1 = row.createCell(3);
					// if (record.getR43_fig_bal_sheet() != null) {
					// R43Cell1.setCellValue(record.getR43_fig_bal_sheet().doubleValue());
					// R43Cell1.setCellStyle(numberStyle);
					// } else {
					// R43Cell1.setCellValue("");
					// R43Cell1.setCellStyle(textStyle);
					// }

					// // R43 Col E
					// Cell R43Cell2 = row.createCell(4);
					// if (record.getR43_fig_bal_sheet_bwp() != null) {
					// R43Cell2.setCellValue(record.getR43_fig_bal_sheet_bwp().doubleValue());
					// R43Cell2.setCellStyle(numberStyle);
					// } else {
					// R43Cell2.setCellValue("");
					// R43Cell2.setCellStyle(textStyle);
					// }

					// // R43 Col F
					// Cell R43Cell3 = row.createCell(5);
					// if (record.getR43_amt_statement_adj() != null) {
					// R43Cell3.setCellValue(record.getR43_amt_statement_adj().doubleValue());
					// R43Cell3.setCellStyle(numberStyle);
					// } else {
					// R43Cell3.setCellValue("");
					// R43Cell3.setCellStyle(textStyle);
					// }
					// // R43 Col G
					// Cell R43Cell4 = row.createCell(6);
					// if (record.getR43_amt_statement_adj_bwp() != null) {
					// R43Cell4.setCellValue(record.getR43_amt_statement_adj_bwp().doubleValue());
					// R43Cell4.setCellStyle(numberStyle);
					// } else {
					// R43Cell4.setCellValue("");
					// R43Cell4.setCellStyle(textStyle);
					// }
					// // R43 Col H
					// Cell R43Cell5 = row.createCell(7);
					// if (record.getR43_net_amt() != null) {
					// R43Cell5.setCellValue(record.getR43_net_amt().doubleValue());
					// R43Cell5.setCellStyle(numberStyle);
					// } else {
					// R43Cell5.setCellValue("");
					// R43Cell5.setCellStyle(textStyle);
					// }
					// // R43 Col I
					// Cell R43Cell6 = row.createCell(8);
					// if (record.getR43_net_amt_bwp() != null) {
					// R43Cell6.setCellValue(record.getR43_net_amt_bwp().doubleValue());
					// R43Cell6.setCellStyle(numberStyle);
					// } else {
					// R43Cell6.setCellValue("");
					// R43Cell6.setCellStyle(textStyle);
					// }
					// // R43 Col J
					// Cell R43Cell7 = row.createCell(9);
					// if (record.getR43_bal_sub() != null) {
					// R43Cell7.setCellValue(record.getR43_bal_sub().doubleValue());
					// R43Cell7.setCellStyle(numberStyle);
					// } else {
					// R43Cell7.setCellValue("");
					// R43Cell7.setCellStyle(textStyle);
					// }
					// // R43 Col K
					// Cell R43Cell8 = row.createCell(10);
					// if (record.getR43_bal_sub_bwp() != null) {
					// R43Cell8.setCellValue(record.getR43_bal_sub_bwp().doubleValue());
					// R43Cell8.setCellStyle(numberStyle);
					// } else {
					// R43Cell8.setCellValue("");
					// R43Cell8.setCellStyle(textStyle);
					// }
					// // R43 Col L
					// Cell R43Cell9 = row.createCell(11);
					// if (record.getR43_bal_sub_diaries() != null) {
					// R43Cell9.setCellValue(record.getR43_bal_sub_diaries().doubleValue());
					// R43Cell9.setCellStyle(numberStyle);
					// } else {
					// R43Cell9.setCellValue("");
					// R43Cell9.setCellStyle(textStyle);
					// }
					// // R43 Col M
					// Cell R43Cell10 = row.createCell(12);
					// if (record.getR43_bal_sub_diaries_bwp() != null) {
					// R43Cell10.setCellValue(record.getR43_bal_sub_diaries_bwp().doubleValue());
					// R43Cell10.setCellStyle(numberStyle);
					// } else {
					// R43Cell10.setCellValue("");
					// R43Cell10.setCellStyle(textStyle);
					// }

					row = sheet.getRow(47);
					Cell R48Cell1 = row.createCell(3);
					if (record.getR48_fig_bal_sheet() != null) {
						R48Cell1.setCellValue(record.getR48_fig_bal_sheet().doubleValue());
						R48Cell1.setCellStyle(numberStyle);
					} else {
						R48Cell1.setCellValue("");
						R48Cell1.setCellStyle(textStyle);
					}

					// R48 Col E
					Cell R48Cell2 = row.createCell(4);
					if (record.getR48_fig_bal_sheet_bwp() != null) {
						R48Cell2.setCellValue(record.getR48_fig_bal_sheet_bwp().doubleValue());
						R48Cell2.setCellStyle(numberStyle);
					} else {
						R48Cell2.setCellValue("");
						R48Cell2.setCellStyle(textStyle);
					}

					// R48 Col F
					Cell R48Cell3 = row.createCell(5);
					if (record.getR48_amt_statement_adj() != null) {
						R48Cell3.setCellValue(record.getR48_amt_statement_adj().doubleValue());
						R48Cell3.setCellStyle(numberStyle);
					} else {
						R48Cell3.setCellValue("");
						R48Cell3.setCellStyle(textStyle);
					}
					// R48 Col G
					Cell R48Cell4 = row.createCell(6);
					if (record.getR48_amt_statement_adj_bwp() != null) {
						R48Cell4.setCellValue(record.getR48_amt_statement_adj_bwp().doubleValue());
						R48Cell4.setCellStyle(numberStyle);
					} else {
						R48Cell4.setCellValue("");
						R48Cell4.setCellStyle(textStyle);
					}
					// // R48 Col H
					// Cell R48Cell5 = row.createCell(7);
					// if (record.getR48_net_amt() != null) {
					// R48Cell5.setCellValue(record.getR48_net_amt().doubleValue());
					// R48Cell5.setCellStyle(numberStyle);
					// } else {
					// R48Cell5.setCellValue("");
					// R48Cell5.setCellStyle(textStyle);
					// }
					// R48 Col I
					Cell R48Cell6 = row.createCell(8);
					if (record.getR48_net_amt_bwp() != null) {
						R48Cell6.setCellValue(record.getR48_net_amt_bwp().doubleValue());
						R48Cell6.setCellStyle(numberStyle);
					} else {
						R48Cell6.setCellValue("");
						R48Cell6.setCellStyle(textStyle);
					}
					// R48 Col J
					Cell R48Cell7 = row.createCell(9);
					if (record.getR48_bal_sub() != null) {
						R48Cell7.setCellValue(record.getR48_bal_sub().doubleValue());
						R48Cell7.setCellStyle(numberStyle);
					} else {
						R48Cell7.setCellValue("");
						R48Cell7.setCellStyle(textStyle);
					}
					// R48 Col K
					Cell R48Cell8 = row.createCell(10);
					if (record.getR48_bal_sub_bwp() != null) {
						R48Cell8.setCellValue(record.getR48_bal_sub_bwp().doubleValue());
						R48Cell8.setCellStyle(numberStyle);
					} else {
						R48Cell8.setCellValue("");
						R48Cell8.setCellStyle(textStyle);
					}
					// R48 Col L
					Cell R48Cell9 = row.createCell(11);
					if (record.getR48_bal_sub_diaries() != null) {
						R48Cell9.setCellValue(record.getR48_bal_sub_diaries().doubleValue());
						R48Cell9.setCellStyle(numberStyle);
					} else {
						R48Cell9.setCellValue("");
						R48Cell9.setCellStyle(textStyle);
					}
					// R48 Col M
					Cell R48Cell10 = row.createCell(12);
					if (record.getR48_bal_sub_diaries_bwp() != null) {
						R48Cell10.setCellValue(record.getR48_bal_sub_diaries_bwp().doubleValue());
						R48Cell10.setCellStyle(numberStyle);
					} else {
						R48Cell10.setCellValue("");
						R48Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(48);
					Cell R49Cell1 = row.createCell(3);
					if (record.getR49_fig_bal_sheet() != null) {
						R49Cell1.setCellValue(record.getR49_fig_bal_sheet().doubleValue());
						R49Cell1.setCellStyle(numberStyle);
					} else {
						R49Cell1.setCellValue("");
						R49Cell1.setCellStyle(textStyle);
					}

					// R49 Col E
					Cell R49Cell2 = row.createCell(4);
					if (record.getR49_fig_bal_sheet_bwp() != null) {
						R49Cell2.setCellValue(record.getR49_fig_bal_sheet_bwp().doubleValue());
						R49Cell2.setCellStyle(numberStyle);
					} else {
						R49Cell2.setCellValue("");
						R49Cell2.setCellStyle(textStyle);
					}

					// R49 Col F
					Cell R49Cell3 = row.createCell(5);
					if (record.getR49_amt_statement_adj() != null) {
						R49Cell3.setCellValue(record.getR49_amt_statement_adj().doubleValue());
						R49Cell3.setCellStyle(numberStyle);
					} else {
						R49Cell3.setCellValue("");
						R49Cell3.setCellStyle(textStyle);
					}
					// R49 Col G
					Cell R49Cell4 = row.createCell(6);
					if (record.getR49_amt_statement_adj_bwp() != null) {
						R49Cell4.setCellValue(record.getR49_amt_statement_adj_bwp().doubleValue());
						R49Cell4.setCellStyle(numberStyle);
					} else {
						R49Cell4.setCellValue("");
						R49Cell4.setCellStyle(textStyle);
					}
					// // R49 Col H
					// Cell R49Cell5 = row.createCell(7);
					// if (record.getR49_net_amt() != null) {
					// R49Cell5.setCellValue(record.getR49_net_amt().doubleValue());
					// R49Cell5.setCellStyle(numberStyle);
					// } else {
					// R49Cell5.setCellValue("");
					// R49Cell5.setCellStyle(textStyle);
					// }
					// R49 Col I
					Cell R49Cell6 = row.createCell(8);
					if (record.getR49_net_amt_bwp() != null) {
						R49Cell6.setCellValue(record.getR49_net_amt_bwp().doubleValue());
						R49Cell6.setCellStyle(numberStyle);
					} else {
						R49Cell6.setCellValue("");
						R49Cell6.setCellStyle(textStyle);
					}
					// R49 Col J
					Cell R49Cell7 = row.createCell(9);
					if (record.getR49_bal_sub() != null) {
						R49Cell7.setCellValue(record.getR49_bal_sub().doubleValue());
						R49Cell7.setCellStyle(numberStyle);
					} else {
						R49Cell7.setCellValue("");
						R49Cell7.setCellStyle(textStyle);
					}
					// R49 Col K
					Cell R49Cell8 = row.createCell(10);
					if (record.getR49_bal_sub_bwp() != null) {
						R49Cell8.setCellValue(record.getR49_bal_sub_bwp().doubleValue());
						R49Cell8.setCellStyle(numberStyle);
					} else {
						R49Cell8.setCellValue("");
						R49Cell8.setCellStyle(textStyle);
					}
					// R49 Col L
					Cell R49Cell9 = row.createCell(11);
					if (record.getR49_bal_sub_diaries() != null) {
						R49Cell9.setCellValue(record.getR49_bal_sub_diaries().doubleValue());
						R49Cell9.setCellStyle(numberStyle);
					} else {
						R49Cell9.setCellValue("");
						R49Cell9.setCellStyle(textStyle);
					}
					// R49 Col M
					Cell R49Cell10 = row.createCell(12);
					if (record.getR49_bal_sub_diaries_bwp() != null) {
						R49Cell10.setCellValue(record.getR49_bal_sub_diaries_bwp().doubleValue());
						R49Cell10.setCellStyle(numberStyle);
					} else {
						R49Cell10.setCellValue("");
						R49Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(49);
					Cell R50Cell1 = row.createCell(3);
					if (record.getR50_fig_bal_sheet() != null) {
						R50Cell1.setCellValue(record.getR50_fig_bal_sheet().doubleValue());
						R50Cell1.setCellStyle(numberStyle);
					} else {
						R50Cell1.setCellValue("");
						R50Cell1.setCellStyle(textStyle);
					}

					// R50 Col E
					Cell R50Cell2 = row.createCell(4);
					if (record.getR50_fig_bal_sheet_bwp() != null) {
						R50Cell2.setCellValue(record.getR50_fig_bal_sheet_bwp().doubleValue());
						R50Cell2.setCellStyle(numberStyle);
					} else {
						R50Cell2.setCellValue("");
						R50Cell2.setCellStyle(textStyle);
					}

					// R50 Col F
					Cell R50Cell3 = row.createCell(5);
					if (record.getR50_amt_statement_adj() != null) {
						R50Cell3.setCellValue(record.getR50_amt_statement_adj().doubleValue());
						R50Cell3.setCellStyle(numberStyle);
					} else {
						R50Cell3.setCellValue("");
						R50Cell3.setCellStyle(textStyle);
					}
					// R50 Col G
					Cell R50Cell4 = row.createCell(6);
					if (record.getR50_amt_statement_adj_bwp() != null) {
						R50Cell4.setCellValue(record.getR50_amt_statement_adj_bwp().doubleValue());
						R50Cell4.setCellStyle(numberStyle);
					} else {
						R50Cell4.setCellValue("");
						R50Cell4.setCellStyle(textStyle);
					}
					// // R50 Col H
					// Cell R50Cell5 = row.createCell(7);
					// if (record.getR50_net_amt() != null) {
					// R50Cell5.setCellValue(record.getR50_net_amt().doubleValue());
					// R50Cell5.setCellStyle(numberStyle);
					// } else {
					// R50Cell5.setCellValue("");
					// R50Cell5.setCellStyle(textStyle);
					// }
					// R50 Col I
					Cell R50Cell6 = row.createCell(8);
					if (record.getR50_net_amt_bwp() != null) {
						R50Cell6.setCellValue(record.getR50_net_amt_bwp().doubleValue());
						R50Cell6.setCellStyle(numberStyle);
					} else {
						R50Cell6.setCellValue("");
						R50Cell6.setCellStyle(textStyle);
					}
					// R50 Col J
					Cell R50Cell7 = row.createCell(9);
					if (record.getR50_bal_sub() != null) {
						R50Cell7.setCellValue(record.getR50_bal_sub().doubleValue());
						R50Cell7.setCellStyle(numberStyle);
					} else {
						R50Cell7.setCellValue("");
						R50Cell7.setCellStyle(textStyle);
					}
					// R50 Col K
					Cell R50Cell8 = row.createCell(10);
					if (record.getR50_bal_sub_bwp() != null) {
						R50Cell8.setCellValue(record.getR50_bal_sub_bwp().doubleValue());
						R50Cell8.setCellStyle(numberStyle);
					} else {
						R50Cell8.setCellValue("");
						R50Cell8.setCellStyle(textStyle);
					}
					// R50 Col L
					Cell R50Cell9 = row.createCell(11);
					if (record.getR50_bal_sub_diaries() != null) {
						R50Cell9.setCellValue(record.getR50_bal_sub_diaries().doubleValue());
						R50Cell9.setCellStyle(numberStyle);
					} else {
						R50Cell9.setCellValue("");
						R50Cell9.setCellStyle(textStyle);
					}
					// R50 Col M
					Cell R50Cell10 = row.createCell(12);
					if (record.getR50_bal_sub_diaries_bwp() != null) {
						R50Cell10.setCellValue(record.getR50_bal_sub_diaries_bwp().doubleValue());
						R50Cell10.setCellStyle(numberStyle);
					} else {
						R50Cell10.setCellValue("");
						R50Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(50);
					Cell R51Cell1 = row.createCell(3);
					if (record.getR51_fig_bal_sheet() != null) {
						R51Cell1.setCellValue(record.getR51_fig_bal_sheet().doubleValue());
						R51Cell1.setCellStyle(numberStyle);
					} else {
						R51Cell1.setCellValue("");
						R51Cell1.setCellStyle(textStyle);
					}

					// R51 Col E
					Cell R51Cell2 = row.createCell(4);
					if (record.getR51_fig_bal_sheet_bwp() != null) {
						R51Cell2.setCellValue(record.getR51_fig_bal_sheet_bwp().doubleValue());
						R51Cell2.setCellStyle(numberStyle);
					} else {
						R51Cell2.setCellValue("");
						R51Cell2.setCellStyle(textStyle);
					}

					// R51 Col F
					Cell R51Cell3 = row.createCell(5);
					if (record.getR51_amt_statement_adj() != null) {
						R51Cell3.setCellValue(record.getR51_amt_statement_adj().doubleValue());
						R51Cell3.setCellStyle(numberStyle);
					} else {
						R51Cell3.setCellValue("");
						R51Cell3.setCellStyle(textStyle);
					}
					// R51 Col G
					Cell R51Cell4 = row.createCell(6);
					if (record.getR51_amt_statement_adj_bwp() != null) {
						R51Cell4.setCellValue(record.getR51_amt_statement_adj_bwp().doubleValue());
						R51Cell4.setCellStyle(numberStyle);
					} else {
						R51Cell4.setCellValue("");
						R51Cell4.setCellStyle(textStyle);
					}
					// // R51 Col H
					// Cell R51Cell5 = row.createCell(7);
					// if (record.getR51_net_amt() != null) {
					// R51Cell5.setCellValue(record.getR51_net_amt().doubleValue());
					// R51Cell5.setCellStyle(numberStyle);
					// } else {
					// R51Cell5.setCellValue("");
					// R51Cell5.setCellStyle(textStyle);
					// }
					// R51 Col I
					Cell R51Cell6 = row.createCell(8);
					if (record.getR51_net_amt_bwp() != null) {
						R51Cell6.setCellValue(record.getR51_net_amt_bwp().doubleValue());
						R51Cell6.setCellStyle(numberStyle);
					} else {
						R51Cell6.setCellValue("");
						R51Cell6.setCellStyle(textStyle);
					}
					// R51 Col J
					Cell R51Cell7 = row.createCell(9);
					if (record.getR51_bal_sub() != null) {
						R51Cell7.setCellValue(record.getR51_bal_sub().doubleValue());
						R51Cell7.setCellStyle(numberStyle);
					} else {
						R51Cell7.setCellValue("");
						R51Cell7.setCellStyle(textStyle);
					}
					// R51 Col K
					Cell R51Cell8 = row.createCell(10);
					if (record.getR51_bal_sub_bwp() != null) {
						R51Cell8.setCellValue(record.getR51_bal_sub_bwp().doubleValue());
						R51Cell8.setCellStyle(numberStyle);
					} else {
						R51Cell8.setCellValue("");
						R51Cell8.setCellStyle(textStyle);
					}
					// R51 Col L
					Cell R51Cell9 = row.createCell(11);
					if (record.getR51_bal_sub_diaries() != null) {
						R51Cell9.setCellValue(record.getR51_bal_sub_diaries().doubleValue());
						R51Cell9.setCellStyle(numberStyle);
					} else {
						R51Cell9.setCellValue("");
						R51Cell9.setCellStyle(textStyle);
					}
					// R51 Col M
					Cell R51Cell10 = row.createCell(12);
					if (record.getR51_bal_sub_diaries_bwp() != null) {
						R51Cell10.setCellValue(record.getR51_bal_sub_diaries_bwp().doubleValue());
						R51Cell10.setCellStyle(numberStyle);
					} else {
						R51Cell10.setCellValue("");
						R51Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(51);
					Cell R52Cell1 = row.createCell(3);
					if (record.getR52_fig_bal_sheet() != null) {
						R52Cell1.setCellValue(record.getR52_fig_bal_sheet().doubleValue());
						R52Cell1.setCellStyle(numberStyle);
					} else {
						R52Cell1.setCellValue("");
						R52Cell1.setCellStyle(textStyle);
					}

					// R52 Col E
					Cell R52Cell2 = row.createCell(4);
					if (record.getR52_fig_bal_sheet_bwp() != null) {
						R52Cell2.setCellValue(record.getR52_fig_bal_sheet_bwp().doubleValue());
						R52Cell2.setCellStyle(numberStyle);
					} else {
						R52Cell2.setCellValue("");
						R52Cell2.setCellStyle(textStyle);
					}

					// R52 Col F
					Cell R52Cell3 = row.createCell(5);
					if (record.getR52_amt_statement_adj() != null) {
						R52Cell3.setCellValue(record.getR52_amt_statement_adj().doubleValue());
						R52Cell3.setCellStyle(numberStyle);
					} else {
						R52Cell3.setCellValue("");
						R52Cell3.setCellStyle(textStyle);
					}
					// R52 Col G
					Cell R52Cell4 = row.createCell(6);
					if (record.getR52_amt_statement_adj_bwp() != null) {
						R52Cell4.setCellValue(record.getR52_amt_statement_adj_bwp().doubleValue());
						R52Cell4.setCellStyle(numberStyle);
					} else {
						R52Cell4.setCellValue("");
						R52Cell4.setCellStyle(textStyle);
					}
					// // R52 Col H
					// Cell R52Cell5 = row.createCell(7);
					// if (record.getR52_net_amt() != null) {
					// R52Cell5.setCellValue(record.getR52_net_amt().doubleValue());
					// R52Cell5.setCellStyle(numberStyle);
					// } else {
					// R52Cell5.setCellValue("");
					// R52Cell5.setCellStyle(textStyle);
					// }
					// R52 Col I
					Cell R52Cell6 = row.createCell(8);
					if (record.getR52_net_amt_bwp() != null) {
						R52Cell6.setCellValue(record.getR52_net_amt_bwp().doubleValue());
						R52Cell6.setCellStyle(numberStyle);
					} else {
						R52Cell6.setCellValue("");
						R52Cell6.setCellStyle(textStyle);
					}
					// R52 Col J
					Cell R52Cell7 = row.createCell(9);
					if (record.getR52_bal_sub() != null) {
						R52Cell7.setCellValue(record.getR52_bal_sub().doubleValue());
						R52Cell7.setCellStyle(numberStyle);
					} else {
						R52Cell7.setCellValue("");
						R52Cell7.setCellStyle(textStyle);
					}
					// R52 Col K
					Cell R52Cell8 = row.createCell(10);
					if (record.getR52_bal_sub_bwp() != null) {
						R52Cell8.setCellValue(record.getR52_bal_sub_bwp().doubleValue());
						R52Cell8.setCellStyle(numberStyle);
					} else {
						R52Cell8.setCellValue("");
						R52Cell8.setCellStyle(textStyle);
					}
					// R52 Col L
					Cell R52Cell9 = row.createCell(11);
					if (record.getR52_bal_sub_diaries() != null) {
						R52Cell9.setCellValue(record.getR52_bal_sub_diaries().doubleValue());
						R52Cell9.setCellStyle(numberStyle);
					} else {
						R52Cell9.setCellValue("");
						R52Cell9.setCellStyle(textStyle);
					}
					// R52 Col M
					Cell R52Cell10 = row.createCell(12);
					if (record.getR52_bal_sub_diaries_bwp() != null) {
						R52Cell10.setCellValue(record.getR52_bal_sub_diaries_bwp().doubleValue());
						R52Cell10.setCellStyle(numberStyle);
					} else {
						R52Cell10.setCellValue("");
						R52Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(52);
					Cell R53Cell1 = row.createCell(3);
					if (record.getR53_fig_bal_sheet() != null) {
						R53Cell1.setCellValue(record.getR53_fig_bal_sheet().doubleValue());
						R53Cell1.setCellStyle(numberStyle);
					} else {
						R53Cell1.setCellValue("");
						R53Cell1.setCellStyle(textStyle);
					}

					// R53 Col E
					Cell R53Cell2 = row.createCell(4);
					if (record.getR53_fig_bal_sheet_bwp() != null) {
						R53Cell2.setCellValue(record.getR53_fig_bal_sheet_bwp().doubleValue());
						R53Cell2.setCellStyle(numberStyle);
					} else {
						R53Cell2.setCellValue("");
						R53Cell2.setCellStyle(textStyle);
					}

					// R53 Col F
					Cell R53Cell3 = row.createCell(5);
					if (record.getR53_amt_statement_adj() != null) {
						R53Cell3.setCellValue(record.getR53_amt_statement_adj().doubleValue());
						R53Cell3.setCellStyle(numberStyle);
					} else {
						R53Cell3.setCellValue("");
						R53Cell3.setCellStyle(textStyle);
					}
					// R53 Col G
					Cell R53Cell4 = row.createCell(6);
					if (record.getR53_amt_statement_adj_bwp() != null) {
						R53Cell4.setCellValue(record.getR53_amt_statement_adj_bwp().doubleValue());
						R53Cell4.setCellStyle(numberStyle);
					} else {
						R53Cell4.setCellValue("");
						R53Cell4.setCellStyle(textStyle);
					}
					// // R53 Col H
					// Cell R53Cell5 = row.createCell(7);
					// if (record.getR53_net_amt() != null) {
					// R53Cell5.setCellValue(record.getR53_net_amt().doubleValue());
					// R53Cell5.setCellStyle(numberStyle);
					// } else {
					// R53Cell5.setCellValue("");
					// R53Cell5.setCellStyle(textStyle);
					// }
					// R53 Col I
					Cell R53Cell6 = row.createCell(8);
					if (record.getR53_net_amt_bwp() != null) {
						R53Cell6.setCellValue(record.getR53_net_amt_bwp().doubleValue());
						R53Cell6.setCellStyle(numberStyle);
					} else {
						R53Cell6.setCellValue("");
						R53Cell6.setCellStyle(textStyle);
					}
					// R53 Col J
					Cell R53Cell7 = row.createCell(9);
					if (record.getR53_bal_sub() != null) {
						R53Cell7.setCellValue(record.getR53_bal_sub().doubleValue());
						R53Cell7.setCellStyle(numberStyle);
					} else {
						R53Cell7.setCellValue("");
						R53Cell7.setCellStyle(textStyle);
					}
					// R53 Col K
					Cell R53Cell8 = row.createCell(10);
					if (record.getR53_bal_sub_bwp() != null) {
						R53Cell8.setCellValue(record.getR53_bal_sub_bwp().doubleValue());
						R53Cell8.setCellStyle(numberStyle);
					} else {
						R53Cell8.setCellValue("");
						R53Cell8.setCellStyle(textStyle);
					}
					// R53 Col L
					Cell R53Cell9 = row.createCell(11);
					if (record.getR53_bal_sub_diaries() != null) {
						R53Cell9.setCellValue(record.getR53_bal_sub_diaries().doubleValue());
						R53Cell9.setCellStyle(numberStyle);
					} else {
						R53Cell9.setCellValue("");
						R53Cell9.setCellStyle(textStyle);
					}
					// R53 Col M
					Cell R53Cell10 = row.createCell(12);
					if (record.getR53_bal_sub_diaries_bwp() != null) {
						R53Cell10.setCellValue(record.getR53_bal_sub_diaries_bwp().doubleValue());
						R53Cell10.setCellStyle(numberStyle);
					} else {
						R53Cell10.setCellValue("");
						R53Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(53);
					Cell R54Cell1 = row.createCell(3);
					if (record.getR54_fig_bal_sheet() != null) {
						R54Cell1.setCellValue(record.getR54_fig_bal_sheet().doubleValue());
						R54Cell1.setCellStyle(numberStyle);
					} else {
						R54Cell1.setCellValue("");
						R54Cell1.setCellStyle(textStyle);
					}

					// R54 Col E
					Cell R54Cell2 = row.createCell(4);
					if (record.getR54_fig_bal_sheet_bwp() != null) {
						R54Cell2.setCellValue(record.getR54_fig_bal_sheet_bwp().doubleValue());
						R54Cell2.setCellStyle(numberStyle);
					} else {
						R54Cell2.setCellValue("");
						R54Cell2.setCellStyle(textStyle);
					}

					// R54 Col F
					Cell R54Cell3 = row.createCell(5);
					if (record.getR54_amt_statement_adj() != null) {
						R54Cell3.setCellValue(record.getR54_amt_statement_adj().doubleValue());
						R54Cell3.setCellStyle(numberStyle);
					} else {
						R54Cell3.setCellValue("");
						R54Cell3.setCellStyle(textStyle);
					}
					// R54 Col G
					Cell R54Cell4 = row.createCell(6);
					if (record.getR54_amt_statement_adj_bwp() != null) {
						R54Cell4.setCellValue(record.getR54_amt_statement_adj_bwp().doubleValue());
						R54Cell4.setCellStyle(numberStyle);
					} else {
						R54Cell4.setCellValue("");
						R54Cell4.setCellStyle(textStyle);
					}
					// // R54 Col H
					// Cell R54Cell5 = row.createCell(7);
					// if (record.getR54_net_amt() != null) {
					// R54Cell5.setCellValue(record.getR54_net_amt().doubleValue());
					// R54Cell5.setCellStyle(numberStyle);
					// } else {
					// R54Cell5.setCellValue("");
					// R54Cell5.setCellStyle(textStyle);
					// }
					// R54 Col I
					Cell R54Cell6 = row.createCell(8);
					if (record.getR54_net_amt_bwp() != null) {
						R54Cell6.setCellValue(record.getR54_net_amt_bwp().doubleValue());
						R54Cell6.setCellStyle(numberStyle);
					} else {
						R54Cell6.setCellValue("");
						R54Cell6.setCellStyle(textStyle);
					}
					// R54 Col J
					Cell R54Cell7 = row.createCell(9);
					if (record.getR54_bal_sub() != null) {
						R54Cell7.setCellValue(record.getR54_bal_sub().doubleValue());
						R54Cell7.setCellStyle(numberStyle);
					} else {
						R54Cell7.setCellValue("");
						R54Cell7.setCellStyle(textStyle);
					}
					// R54 Col K
					Cell R54Cell8 = row.createCell(10);
					if (record.getR54_bal_sub_bwp() != null) {
						R54Cell8.setCellValue(record.getR54_bal_sub_bwp().doubleValue());
						R54Cell8.setCellStyle(numberStyle);
					} else {
						R54Cell8.setCellValue("");
						R54Cell8.setCellStyle(textStyle);
					}
					// R54 Col L
					Cell R54Cell9 = row.createCell(11);
					if (record.getR54_bal_sub_diaries() != null) {
						R54Cell9.setCellValue(record.getR54_bal_sub_diaries().doubleValue());
						R54Cell9.setCellStyle(numberStyle);
					} else {
						R54Cell9.setCellValue("");
						R54Cell9.setCellStyle(textStyle);
					}
					// R54 Col M
					Cell R54Cell10 = row.createCell(12);
					if (record.getR54_bal_sub_diaries_bwp() != null) {
						R54Cell10.setCellValue(record.getR54_bal_sub_diaries_bwp().doubleValue());
						R54Cell10.setCellStyle(numberStyle);
					} else {
						R54Cell10.setCellValue("");
						R54Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(54);
					Cell R55Cell1 = row.createCell(3);
					if (record.getR55_fig_bal_sheet() != null) {
						R55Cell1.setCellValue(record.getR55_fig_bal_sheet().doubleValue());
						R55Cell1.setCellStyle(numberStyle);
					} else {
						R55Cell1.setCellValue("");
						R55Cell1.setCellStyle(textStyle);
					}

					// R55 Col E
					Cell R55Cell2 = row.createCell(4);
					if (record.getR55_fig_bal_sheet_bwp() != null) {
						R55Cell2.setCellValue(record.getR55_fig_bal_sheet_bwp().doubleValue());
						R55Cell2.setCellStyle(numberStyle);
					} else {
						R55Cell2.setCellValue("");
						R55Cell2.setCellStyle(textStyle);
					}

					// R55 Col F
					Cell R55Cell3 = row.createCell(5);
					if (record.getR55_amt_statement_adj() != null) {
						R55Cell3.setCellValue(record.getR55_amt_statement_adj().doubleValue());
						R55Cell3.setCellStyle(numberStyle);
					} else {
						R55Cell3.setCellValue("");
						R55Cell3.setCellStyle(textStyle);
					}
					// R55 Col G
					Cell R55Cell4 = row.createCell(6);
					if (record.getR55_amt_statement_adj_bwp() != null) {
						R55Cell4.setCellValue(record.getR55_amt_statement_adj_bwp().doubleValue());
						R55Cell4.setCellStyle(numberStyle);
					} else {
						R55Cell4.setCellValue("");
						R55Cell4.setCellStyle(textStyle);
					}
					// // R55 Col H
					// Cell R55Cell5 = row.createCell(7);
					// if (record.getR55_net_amt() != null) {
					// R55Cell5.setCellValue(record.getR55_net_amt().doubleValue());
					// R55Cell5.setCellStyle(numberStyle);
					// } else {
					// R55Cell5.setCellValue("");
					// R55Cell5.setCellStyle(textStyle);
					// }
					// R55 Col I
					Cell R55Cell6 = row.createCell(8);
					if (record.getR55_net_amt_bwp() != null) {
						R55Cell6.setCellValue(record.getR55_net_amt_bwp().doubleValue());
						R55Cell6.setCellStyle(numberStyle);
					} else {
						R55Cell6.setCellValue("");
						R55Cell6.setCellStyle(textStyle);
					}
					// R55 Col J
					Cell R55Cell7 = row.createCell(9);
					if (record.getR55_bal_sub() != null) {
						R55Cell7.setCellValue(record.getR55_bal_sub().doubleValue());
						R55Cell7.setCellStyle(numberStyle);
					} else {
						R55Cell7.setCellValue("");
						R55Cell7.setCellStyle(textStyle);
					}
					// R55 Col K
					Cell R55Cell8 = row.createCell(10);
					if (record.getR55_bal_sub_bwp() != null) {
						R55Cell8.setCellValue(record.getR55_bal_sub_bwp().doubleValue());
						R55Cell8.setCellStyle(numberStyle);
					} else {
						R55Cell8.setCellValue("");
						R55Cell8.setCellStyle(textStyle);
					}
					// R55 Col L
					Cell R55Cell9 = row.createCell(11);
					if (record.getR55_bal_sub_diaries() != null) {
						R55Cell9.setCellValue(record.getR55_bal_sub_diaries().doubleValue());
						R55Cell9.setCellStyle(numberStyle);
					} else {
						R55Cell9.setCellValue("");
						R55Cell9.setCellStyle(textStyle);
					}
					// R55 Col M
					Cell R55Cell10 = row.createCell(12);
					if (record.getR55_bal_sub_diaries_bwp() != null) {
						R55Cell10.setCellValue(record.getR55_bal_sub_diaries_bwp().doubleValue());
						R55Cell10.setCellStyle(numberStyle);
					} else {
						R55Cell10.setCellValue("");
						R55Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(55);
					Cell R56Cell1 = row.createCell(3);
					if (record.getR56_fig_bal_sheet() != null) {
						R56Cell1.setCellValue(record.getR56_fig_bal_sheet().doubleValue());
						R56Cell1.setCellStyle(numberStyle);
					} else {
						R56Cell1.setCellValue("");
						R56Cell1.setCellStyle(textStyle);
					}

					// R56 Col E
					Cell R56Cell2 = row.createCell(4);
					if (record.getR56_fig_bal_sheet_bwp() != null) {
						R56Cell2.setCellValue(record.getR56_fig_bal_sheet_bwp().doubleValue());
						R56Cell2.setCellStyle(numberStyle);
					} else {
						R56Cell2.setCellValue("");
						R56Cell2.setCellStyle(textStyle);
					}

					// R56 Col F
					Cell R56Cell3 = row.createCell(5);
					if (record.getR56_amt_statement_adj() != null) {
						R56Cell3.setCellValue(record.getR56_amt_statement_adj().doubleValue());
						R56Cell3.setCellStyle(numberStyle);
					} else {
						R56Cell3.setCellValue("");
						R56Cell3.setCellStyle(textStyle);
					}
					// R56 Col G
					Cell R56Cell4 = row.createCell(6);
					if (record.getR56_amt_statement_adj_bwp() != null) {
						R56Cell4.setCellValue(record.getR56_amt_statement_adj_bwp().doubleValue());
						R56Cell4.setCellStyle(numberStyle);
					} else {
						R56Cell4.setCellValue("");
						R56Cell4.setCellStyle(textStyle);
					}
					// // R56 Col H
					// Cell R56Cell5 = row.createCell(7);
					// if (record.getR56_net_amt() != null) {
					// R56Cell5.setCellValue(record.getR56_net_amt().doubleValue());
					// R56Cell5.setCellStyle(numberStyle);
					// } else {
					// R56Cell5.setCellValue("");
					// R56Cell5.setCellStyle(textStyle);
					// }
					// R56 Col I
					Cell R56Cell6 = row.createCell(8);
					if (record.getR56_net_amt_bwp() != null) {
						R56Cell6.setCellValue(record.getR56_net_amt_bwp().doubleValue());
						R56Cell6.setCellStyle(numberStyle);
					} else {
						R56Cell6.setCellValue("");
						R56Cell6.setCellStyle(textStyle);
					}
					// R56 Col J
					Cell R56Cell7 = row.createCell(9);
					if (record.getR56_bal_sub() != null) {
						R56Cell7.setCellValue(record.getR56_bal_sub().doubleValue());
						R56Cell7.setCellStyle(numberStyle);
					} else {
						R56Cell7.setCellValue("");
						R56Cell7.setCellStyle(textStyle);
					}
					// R56 Col K
					Cell R56Cell8 = row.createCell(10);
					if (record.getR56_bal_sub_bwp() != null) {
						R56Cell8.setCellValue(record.getR56_bal_sub_bwp().doubleValue());
						R56Cell8.setCellStyle(numberStyle);
					} else {
						R56Cell8.setCellValue("");
						R56Cell8.setCellStyle(textStyle);
					}
					// R56 Col L
					Cell R56Cell9 = row.createCell(11);
					if (record.getR56_bal_sub_diaries() != null) {
						R56Cell9.setCellValue(record.getR56_bal_sub_diaries().doubleValue());
						R56Cell9.setCellStyle(numberStyle);
					} else {
						R56Cell9.setCellValue("");
						R56Cell9.setCellStyle(textStyle);
					}
					// R56 Col M
					Cell R56Cell10 = row.createCell(12);
					if (record.getR56_bal_sub_diaries_bwp() != null) {
						R56Cell10.setCellValue(record.getR56_bal_sub_diaries_bwp().doubleValue());
						R56Cell10.setCellStyle(numberStyle);
					} else {
						R56Cell10.setCellValue("");
						R56Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(56);
					Cell R57Cell1 = row.createCell(3);
					if (record.getR57_fig_bal_sheet() != null) {
						R57Cell1.setCellValue(record.getR57_fig_bal_sheet().doubleValue());
						R57Cell1.setCellStyle(numberStyle);
					} else {
						R57Cell1.setCellValue("");
						R57Cell1.setCellStyle(textStyle);
					}

					// R57 Col E
					Cell R57Cell2 = row.createCell(4);
					if (record.getR57_fig_bal_sheet_bwp() != null) {
						R57Cell2.setCellValue(record.getR57_fig_bal_sheet_bwp().doubleValue());
						R57Cell2.setCellStyle(numberStyle);
					} else {
						R57Cell2.setCellValue("");
						R57Cell2.setCellStyle(textStyle);
					}

					// R57 Col F
					Cell R57Cell3 = row.createCell(5);
					if (record.getR57_amt_statement_adj() != null) {
						R57Cell3.setCellValue(record.getR57_amt_statement_adj().doubleValue());
						R57Cell3.setCellStyle(numberStyle);
					} else {
						R57Cell3.setCellValue("");
						R57Cell3.setCellStyle(textStyle);
					}
					// R57 Col G
					Cell R57Cell4 = row.createCell(6);
					if (record.getR57_amt_statement_adj_bwp() != null) {
						R57Cell4.setCellValue(record.getR57_amt_statement_adj_bwp().doubleValue());
						R57Cell4.setCellStyle(numberStyle);
					} else {
						R57Cell4.setCellValue("");
						R57Cell4.setCellStyle(textStyle);
					}
					// // R57 Col H
					// Cell R57Cell5 = row.createCell(7);
					// if (record.getR57_net_amt() != null) {
					// R57Cell5.setCellValue(record.getR57_net_amt().doubleValue());
					// R57Cell5.setCellStyle(numberStyle);
					// } else {
					// R57Cell5.setCellValue("");
					// R57Cell5.setCellStyle(textStyle);
					// }
					// R57 Col I
					Cell R57Cell6 = row.createCell(8);
					if (record.getR57_net_amt_bwp() != null) {
						R57Cell6.setCellValue(record.getR57_net_amt_bwp().doubleValue());
						R57Cell6.setCellStyle(numberStyle);
					} else {
						R57Cell6.setCellValue("");
						R57Cell6.setCellStyle(textStyle);
					}
					// R57 Col J
					Cell R57Cell7 = row.createCell(9);
					if (record.getR57_bal_sub() != null) {
						R57Cell7.setCellValue(record.getR57_bal_sub().doubleValue());
						R57Cell7.setCellStyle(numberStyle);
					} else {
						R57Cell7.setCellValue("");
						R57Cell7.setCellStyle(textStyle);
					}
					// R57 Col K
					Cell R57Cell8 = row.createCell(10);
					if (record.getR57_bal_sub_bwp() != null) {
						R57Cell8.setCellValue(record.getR57_bal_sub_bwp().doubleValue());
						R57Cell8.setCellStyle(numberStyle);
					} else {
						R57Cell8.setCellValue("");
						R57Cell8.setCellStyle(textStyle);
					}
					// R57 Col L
					Cell R57Cell9 = row.createCell(11);
					if (record.getR57_bal_sub_diaries() != null) {
						R57Cell9.setCellValue(record.getR57_bal_sub_diaries().doubleValue());
						R57Cell9.setCellStyle(numberStyle);
					} else {
						R57Cell9.setCellValue("");
						R57Cell9.setCellStyle(textStyle);
					}
					// R57 Col M
					Cell R57Cell10 = row.createCell(12);
					if (record.getR57_bal_sub_diaries_bwp() != null) {
						R57Cell10.setCellValue(record.getR57_bal_sub_diaries_bwp().doubleValue());
						R57Cell10.setCellStyle(numberStyle);
					} else {
						R57Cell10.setCellValue("");
						R57Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(57);
					Cell R58Cell1 = row.createCell(3);
					if (record.getR58_fig_bal_sheet() != null) {
						R58Cell1.setCellValue(record.getR58_fig_bal_sheet().doubleValue());
						R58Cell1.setCellStyle(numberStyle);
					} else {
						R58Cell1.setCellValue("");
						R58Cell1.setCellStyle(textStyle);
					}

					// R58 Col E
					Cell R58Cell2 = row.createCell(4);
					if (record.getR58_fig_bal_sheet_bwp() != null) {
						R58Cell2.setCellValue(record.getR58_fig_bal_sheet_bwp().doubleValue());
						R58Cell2.setCellStyle(numberStyle);
					} else {
						R58Cell2.setCellValue("");
						R58Cell2.setCellStyle(textStyle);
					}

					// R58 Col F
					Cell R58Cell3 = row.createCell(5);
					if (record.getR58_amt_statement_adj() != null) {
						R58Cell3.setCellValue(record.getR58_amt_statement_adj().doubleValue());
						R58Cell3.setCellStyle(numberStyle);
					} else {
						R58Cell3.setCellValue("");
						R58Cell3.setCellStyle(textStyle);
					}
					// R58 Col G
					Cell R58Cell4 = row.createCell(6);
					if (record.getR58_amt_statement_adj_bwp() != null) {
						R58Cell4.setCellValue(record.getR58_amt_statement_adj_bwp().doubleValue());
						R58Cell4.setCellStyle(numberStyle);
					} else {
						R58Cell4.setCellValue("");
						R58Cell4.setCellStyle(textStyle);
					}
					// // R58 Col H
					// Cell R58Cell5 = row.createCell(7);
					// if (record.getR58_net_amt() != null) {
					// R58Cell5.setCellValue(record.getR58_net_amt().doubleValue());
					// R58Cell5.setCellStyle(numberStyle);
					// } else {
					// R58Cell5.setCellValue("");
					// R58Cell5.setCellStyle(textStyle);
					// }
					// R58 Col I
					Cell R58Cell6 = row.createCell(8);
					if (record.getR58_net_amt_bwp() != null) {
						R58Cell6.setCellValue(record.getR58_net_amt_bwp().doubleValue());
						R58Cell6.setCellStyle(numberStyle);
					} else {
						R58Cell6.setCellValue("");
						R58Cell6.setCellStyle(textStyle);
					}
					// R58 Col J
					Cell R58Cell7 = row.createCell(9);
					if (record.getR58_bal_sub() != null) {
						R58Cell7.setCellValue(record.getR58_bal_sub().doubleValue());
						R58Cell7.setCellStyle(numberStyle);
					} else {
						R58Cell7.setCellValue("");
						R58Cell7.setCellStyle(textStyle);
					}
					// R58 Col K
					Cell R58Cell8 = row.createCell(10);
					if (record.getR58_bal_sub_bwp() != null) {
						R58Cell8.setCellValue(record.getR58_bal_sub_bwp().doubleValue());
						R58Cell8.setCellStyle(numberStyle);
					} else {
						R58Cell8.setCellValue("");
						R58Cell8.setCellStyle(textStyle);
					}
					// R58 Col L
					Cell R58Cell9 = row.createCell(11);
					if (record.getR58_bal_sub_diaries() != null) {
						R58Cell9.setCellValue(record.getR58_bal_sub_diaries().doubleValue());
						R58Cell9.setCellStyle(numberStyle);
					} else {
						R58Cell9.setCellValue("");
						R58Cell9.setCellStyle(textStyle);
					}
					// R58 Col M
					Cell R58Cell10 = row.createCell(12);
					if (record.getR58_bal_sub_diaries_bwp() != null) {
						R58Cell10.setCellValue(record.getR58_bal_sub_diaries_bwp().doubleValue());
						R58Cell10.setCellStyle(numberStyle);
					} else {
						R58Cell10.setCellValue("");
						R58Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(58);
					Cell R59Cell1 = row.createCell(3);
					if (record.getR59_fig_bal_sheet() != null) {
						R59Cell1.setCellValue(record.getR59_fig_bal_sheet().doubleValue());
						R59Cell1.setCellStyle(numberStyle);
					} else {
						R59Cell1.setCellValue("");
						R59Cell1.setCellStyle(textStyle);
					}

					// R59 Col E
					Cell R59Cell2 = row.createCell(4);
					if (record.getR59_fig_bal_sheet_bwp() != null) {
						R59Cell2.setCellValue(record.getR59_fig_bal_sheet_bwp().doubleValue());
						R59Cell2.setCellStyle(numberStyle);
					} else {
						R59Cell2.setCellValue("");
						R59Cell2.setCellStyle(textStyle);
					}

					// R59 Col F
					Cell R59Cell3 = row.createCell(5);
					if (record.getR59_amt_statement_adj() != null) {
						R59Cell3.setCellValue(record.getR59_amt_statement_adj().doubleValue());
						R59Cell3.setCellStyle(numberStyle);
					} else {
						R59Cell3.setCellValue("");
						R59Cell3.setCellStyle(textStyle);
					}
					// R59 Col G
					Cell R59Cell4 = row.createCell(6);
					if (record.getR59_amt_statement_adj_bwp() != null) {
						R59Cell4.setCellValue(record.getR59_amt_statement_adj_bwp().doubleValue());
						R59Cell4.setCellStyle(numberStyle);
					} else {
						R59Cell4.setCellValue("");
						R59Cell4.setCellStyle(textStyle);
					}
					// // R59 Col H
					// Cell R59Cell5 = row.createCell(7);
					// if (record.getR59_net_amt() != null) {
					// R59Cell5.setCellValue(record.getR59_net_amt().doubleValue());
					// R59Cell5.setCellStyle(numberStyle);
					// } else {
					// R59Cell5.setCellValue("");
					// R59Cell5.setCellStyle(textStyle);
					// }
					// R59 Col I
					Cell R59Cell6 = row.createCell(8);
					if (record.getR59_net_amt_bwp() != null) {
						R59Cell6.setCellValue(record.getR59_net_amt_bwp().doubleValue());
						R59Cell6.setCellStyle(numberStyle);
					} else {
						R59Cell6.setCellValue("");
						R59Cell6.setCellStyle(textStyle);
					}
					// R59 Col J
					Cell R59Cell7 = row.createCell(9);
					if (record.getR59_bal_sub() != null) {
						R59Cell7.setCellValue(record.getR59_bal_sub().doubleValue());
						R59Cell7.setCellStyle(numberStyle);
					} else {
						R59Cell7.setCellValue("");
						R59Cell7.setCellStyle(textStyle);
					}
					// R59 Col K
					Cell R59Cell8 = row.createCell(10);
					if (record.getR59_bal_sub_bwp() != null) {
						R59Cell8.setCellValue(record.getR59_bal_sub_bwp().doubleValue());
						R59Cell8.setCellStyle(numberStyle);
					} else {
						R59Cell8.setCellValue("");
						R59Cell8.setCellStyle(textStyle);
					}
					// R59 Col L
					Cell R59Cell9 = row.createCell(11);
					if (record.getR59_bal_sub_diaries() != null) {
						R59Cell9.setCellValue(record.getR59_bal_sub_diaries().doubleValue());
						R59Cell9.setCellStyle(numberStyle);
					} else {
						R59Cell9.setCellValue("");
						R59Cell9.setCellStyle(textStyle);
					}
					// R59 Col M
					Cell R59Cell10 = row.createCell(12);
					if (record.getR59_bal_sub_diaries_bwp() != null) {
						R59Cell10.setCellValue(record.getR59_bal_sub_diaries_bwp().doubleValue());
						R59Cell10.setCellStyle(numberStyle);
					} else {
						R59Cell10.setCellValue("");
						R59Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(59);
					Cell R60Cell1 = row.createCell(3);
					if (record.getR60_fig_bal_sheet() != null) {
						R60Cell1.setCellValue(record.getR60_fig_bal_sheet().doubleValue());
						R60Cell1.setCellStyle(numberStyle);
					} else {
						R60Cell1.setCellValue("");
						R60Cell1.setCellStyle(textStyle);
					}

					// R60 Col E
					Cell R60Cell2 = row.createCell(4);
					if (record.getR60_fig_bal_sheet_bwp() != null) {
						R60Cell2.setCellValue(record.getR60_fig_bal_sheet_bwp().doubleValue());
						R60Cell2.setCellStyle(numberStyle);
					} else {
						R60Cell2.setCellValue("");
						R60Cell2.setCellStyle(textStyle);
					}

					// R60 Col F
					Cell R60Cell3 = row.createCell(5);
					if (record.getR60_amt_statement_adj() != null) {
						R60Cell3.setCellValue(record.getR60_amt_statement_adj().doubleValue());
						R60Cell3.setCellStyle(numberStyle);
					} else {
						R60Cell3.setCellValue("");
						R60Cell3.setCellStyle(textStyle);
					}
					// R60 Col G
					Cell R60Cell4 = row.createCell(6);
					if (record.getR60_amt_statement_adj_bwp() != null) {
						R60Cell4.setCellValue(record.getR60_amt_statement_adj_bwp().doubleValue());
						R60Cell4.setCellStyle(numberStyle);
					} else {
						R60Cell4.setCellValue("");
						R60Cell4.setCellStyle(textStyle);
					}
					// // R60 Col H
					// Cell R60Cell5 = row.createCell(7);
					// if (record.getR60_net_amt() != null) {
					// R60Cell5.setCellValue(record.getR60_net_amt().doubleValue());
					// R60Cell5.setCellStyle(numberStyle);
					// } else {
					// R60Cell5.setCellValue("");
					// R60Cell5.setCellStyle(textStyle);
					// }
					// R60 Col I
					Cell R60Cell6 = row.createCell(8);
					if (record.getR60_net_amt_bwp() != null) {
						R60Cell6.setCellValue(record.getR60_net_amt_bwp().doubleValue());
						R60Cell6.setCellStyle(numberStyle);
					} else {
						R60Cell6.setCellValue("");
						R60Cell6.setCellStyle(textStyle);
					}
					// R60 Col J
					Cell R60Cell7 = row.createCell(9);
					if (record.getR60_bal_sub() != null) {
						R60Cell7.setCellValue(record.getR60_bal_sub().doubleValue());
						R60Cell7.setCellStyle(numberStyle);
					} else {
						R60Cell7.setCellValue("");
						R60Cell7.setCellStyle(textStyle);
					}
					// R60 Col K
					Cell R60Cell8 = row.createCell(10);
					if (record.getR60_bal_sub_bwp() != null) {
						R60Cell8.setCellValue(record.getR60_bal_sub_bwp().doubleValue());
						R60Cell8.setCellStyle(numberStyle);
					} else {
						R60Cell8.setCellValue("");
						R60Cell8.setCellStyle(textStyle);
					}
					// R60 Col L
					Cell R60Cell9 = row.createCell(11);
					if (record.getR60_bal_sub_diaries() != null) {
						R60Cell9.setCellValue(record.getR60_bal_sub_diaries().doubleValue());
						R60Cell9.setCellStyle(numberStyle);
					} else {
						R60Cell9.setCellValue("");
						R60Cell9.setCellStyle(textStyle);
					}
					// R60 Col M
					Cell R60Cell10 = row.createCell(12);
					if (record.getR60_bal_sub_diaries_bwp() != null) {
						R60Cell10.setCellValue(record.getR60_bal_sub_diaries_bwp().doubleValue());
						R60Cell10.setCellStyle(numberStyle);
					} else {
						R60Cell10.setCellValue("");
						R60Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(60);
					Cell R61Cell1 = row.createCell(3);
					if (record.getR61_fig_bal_sheet() != null) {
						R61Cell1.setCellValue(record.getR61_fig_bal_sheet().doubleValue());
						R61Cell1.setCellStyle(numberStyle);
					} else {
						R61Cell1.setCellValue("");
						R61Cell1.setCellStyle(textStyle);
					}

					// R61 Col E
					Cell R61Cell2 = row.createCell(4);
					if (record.getR61_fig_bal_sheet_bwp() != null) {
						R61Cell2.setCellValue(record.getR61_fig_bal_sheet_bwp().doubleValue());
						R61Cell2.setCellStyle(numberStyle);
					} else {
						R61Cell2.setCellValue("");
						R61Cell2.setCellStyle(textStyle);
					}

					// R61 Col F
					Cell R61Cell3 = row.createCell(5);
					if (record.getR61_amt_statement_adj() != null) {
						R61Cell3.setCellValue(record.getR61_amt_statement_adj().doubleValue());
						R61Cell3.setCellStyle(numberStyle);
					} else {
						R61Cell3.setCellValue("");
						R61Cell3.setCellStyle(textStyle);
					}
					// R61 Col G
					Cell R61Cell4 = row.createCell(6);
					if (record.getR61_amt_statement_adj_bwp() != null) {
						R61Cell4.setCellValue(record.getR61_amt_statement_adj_bwp().doubleValue());
						R61Cell4.setCellStyle(numberStyle);
					} else {
						R61Cell4.setCellValue("");
						R61Cell4.setCellStyle(textStyle);
					}
					// // R61 Col H
					// Cell R61Cell5 = row.createCell(7);
					// if (record.getR61_net_amt() != null) {
					// R61Cell5.setCellValue(record.getR61_net_amt().doubleValue());
					// R61Cell5.setCellStyle(numberStyle);
					// } else {
					// R61Cell5.setCellValue("");
					// R61Cell5.setCellStyle(textStyle);
					// }
					// R61 Col I
					Cell R61Cell6 = row.createCell(8);
					if (record.getR61_net_amt_bwp() != null) {
						R61Cell6.setCellValue(record.getR61_net_amt_bwp().doubleValue());
						R61Cell6.setCellStyle(numberStyle);
					} else {
						R61Cell6.setCellValue("");
						R61Cell6.setCellStyle(textStyle);
					}
					// R61 Col J
					Cell R61Cell7 = row.createCell(9);
					if (record.getR61_bal_sub() != null) {
						R61Cell7.setCellValue(record.getR61_bal_sub().doubleValue());
						R61Cell7.setCellStyle(numberStyle);
					} else {
						R61Cell7.setCellValue("");
						R61Cell7.setCellStyle(textStyle);
					}
					// R61 Col K
					Cell R61Cell8 = row.createCell(10);
					if (record.getR61_bal_sub_bwp() != null) {
						R61Cell8.setCellValue(record.getR61_bal_sub_bwp().doubleValue());
						R61Cell8.setCellStyle(numberStyle);
					} else {
						R61Cell8.setCellValue("");
						R61Cell8.setCellStyle(textStyle);
					}
					// R61 Col L
					Cell R61Cell9 = row.createCell(11);
					if (record.getR61_bal_sub_diaries() != null) {
						R61Cell9.setCellValue(record.getR61_bal_sub_diaries().doubleValue());
						R61Cell9.setCellStyle(numberStyle);
					} else {
						R61Cell9.setCellValue("");
						R61Cell9.setCellStyle(textStyle);
					}
					// R61 Col M
					Cell R61Cell10 = row.createCell(12);
					if (record.getR61_bal_sub_diaries_bwp() != null) {
						R61Cell10.setCellValue(record.getR61_bal_sub_diaries_bwp().doubleValue());
						R61Cell10.setCellStyle(numberStyle);
					} else {
						R61Cell10.setCellValue("");
						R61Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(61);
					Cell R62Cell1 = row.createCell(3);
					if (record.getR62_fig_bal_sheet() != null) {
						R62Cell1.setCellValue(record.getR62_fig_bal_sheet().doubleValue());
						R62Cell1.setCellStyle(numberStyle);
					} else {
						R62Cell1.setCellValue("");
						R62Cell1.setCellStyle(textStyle);
					}

					// R62 Col E
					Cell R62Cell2 = row.createCell(4);
					if (record.getR62_fig_bal_sheet_bwp() != null) {
						R62Cell2.setCellValue(record.getR62_fig_bal_sheet_bwp().doubleValue());
						R62Cell2.setCellStyle(numberStyle);
					} else {
						R62Cell2.setCellValue("");
						R62Cell2.setCellStyle(textStyle);
					}

					// R62 Col F
					Cell R62Cell3 = row.createCell(5);
					if (record.getR62_amt_statement_adj() != null) {
						R62Cell3.setCellValue(record.getR62_amt_statement_adj().doubleValue());
						R62Cell3.setCellStyle(numberStyle);
					} else {
						R62Cell3.setCellValue("");
						R62Cell3.setCellStyle(textStyle);
					}
					// R62 Col G
					Cell R62Cell4 = row.createCell(6);
					if (record.getR62_amt_statement_adj_bwp() != null) {
						R62Cell4.setCellValue(record.getR62_amt_statement_adj_bwp().doubleValue());
						R62Cell4.setCellStyle(numberStyle);
					} else {
						R62Cell4.setCellValue("");
						R62Cell4.setCellStyle(textStyle);
					}
					// // R62 Col H
					// Cell R62Cell5 = row.createCell(7);
					// if (record.getR62_net_amt() != null) {
					// R62Cell5.setCellValue(record.getR62_net_amt().doubleValue());
					// R62Cell5.setCellStyle(numberStyle);
					// } else {
					// R62Cell5.setCellValue("");
					// R62Cell5.setCellStyle(textStyle);
					// }
					// R62 Col I
					Cell R62Cell6 = row.createCell(8);
					if (record.getR62_net_amt_bwp() != null) {
						R62Cell6.setCellValue(record.getR62_net_amt_bwp().doubleValue());
						R62Cell6.setCellStyle(numberStyle);
					} else {
						R62Cell6.setCellValue("");
						R62Cell6.setCellStyle(textStyle);
					}
					// R62 Col J
					Cell R62Cell7 = row.createCell(9);
					if (record.getR62_bal_sub() != null) {
						R62Cell7.setCellValue(record.getR62_bal_sub().doubleValue());
						R62Cell7.setCellStyle(numberStyle);
					} else {
						R62Cell7.setCellValue("");
						R62Cell7.setCellStyle(textStyle);
					}
					// R62 Col K
					Cell R62Cell8 = row.createCell(10);
					if (record.getR62_bal_sub_bwp() != null) {
						R62Cell8.setCellValue(record.getR62_bal_sub_bwp().doubleValue());
						R62Cell8.setCellStyle(numberStyle);
					} else {
						R62Cell8.setCellValue("");
						R62Cell8.setCellStyle(textStyle);
					}
					// R62 Col L
					Cell R62Cell9 = row.createCell(11);
					if (record.getR62_bal_sub_diaries() != null) {
						R62Cell9.setCellValue(record.getR62_bal_sub_diaries().doubleValue());
						R62Cell9.setCellStyle(numberStyle);
					} else {
						R62Cell9.setCellValue("");
						R62Cell9.setCellStyle(textStyle);
					}
					// R62 Col M
					Cell R62Cell10 = row.createCell(12);
					if (record.getR62_bal_sub_diaries_bwp() != null) {
						R62Cell10.setCellValue(record.getR62_bal_sub_diaries_bwp().doubleValue());
						R62Cell10.setCellStyle(numberStyle);
					} else {
						R62Cell10.setCellValue("");
						R62Cell10.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "PL_SCHS SUMMARY", null,
						"BRRS_PL_SCHS_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	public byte[] getExcelPL_SCHSARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<PL_SCHS_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for PL_SCHS new report. Returning empty result.");
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

					PL_SCHS_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(9);

					if (record.getREPORT_DATE() != null) {

						R12Cell.setCellValue(record.getREPORT_DATE());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(8);
					Cell R9Cell1 = row.createCell(3);
					if (record.getR9_fig_bal_sheet() != null) {
						R9Cell1.setCellValue(record.getR9_fig_bal_sheet().doubleValue());
						R9Cell1.setCellStyle(numberStyle);
					} else {
						R9Cell1.setCellValue("");
						R9Cell1.setCellStyle(textStyle);
					}

					// R9 Col E
					Cell R9Cell2 = row.createCell(4);
					if (record.getR9_fig_bal_sheet_bwp() != null) {
						R9Cell2.setCellValue(record.getR9_fig_bal_sheet_bwp().doubleValue());
						R9Cell2.setCellStyle(numberStyle);
					} else {
						R9Cell2.setCellValue("");
						R9Cell2.setCellStyle(textStyle);
					}

					// R9 Col F
					Cell R9Cell3 = row.createCell(5);
					if (record.getR9_amt_statement_adj() != null) {
						R9Cell3.setCellValue(record.getR9_amt_statement_adj().doubleValue());
						R9Cell3.setCellStyle(numberStyle);
					} else {
						R9Cell3.setCellValue("");
						R9Cell3.setCellStyle(textStyle);
					}
					// R9 Col G
					Cell R9Cell4 = row.createCell(6);
					if (record.getR9_amt_statement_adj_bwp() != null) {
						R9Cell4.setCellValue(record.getR9_amt_statement_adj_bwp().doubleValue());
						R9Cell4.setCellStyle(numberStyle);
					} else {
						R9Cell4.setCellValue("");
						R9Cell4.setCellStyle(textStyle);
					}
					// // R9 Col H
					// Cell R9Cell5 = row.createCell(7);
					// if (record.getR9_net_amt() != null) {
					// R9Cell5.setCellValue(record.getR9_net_amt().doubleValue());
					// R9Cell5.setCellStyle(numberStyle);
					// } else {
					// R9Cell5.setCellValue("");
					// R9Cell5.setCellStyle(textStyle);
					// }
					// R9 Col I
					Cell R9Cell6 = row.createCell(8);
					if (record.getR9_net_amt_bwp() != null) {
						R9Cell6.setCellValue(record.getR9_net_amt_bwp().doubleValue());
						R9Cell6.setCellStyle(numberStyle);
					} else {
						R9Cell6.setCellValue("");
						R9Cell6.setCellStyle(textStyle);
					}
					// R9 Col J
					Cell R9Cell7 = row.createCell(9);
					if (record.getR9_bal_sub() != null) {
						R9Cell7.setCellValue(record.getR9_bal_sub().doubleValue());
						R9Cell7.setCellStyle(numberStyle);
					} else {
						R9Cell7.setCellValue("");
						R9Cell7.setCellStyle(textStyle);
					}
					// R9 Col K
					Cell R9Cell8 = row.createCell(10);
					if (record.getR9_bal_sub_bwp() != null) {
						R9Cell8.setCellValue(record.getR9_bal_sub_bwp().doubleValue());
						R9Cell8.setCellStyle(numberStyle);
					} else {
						R9Cell8.setCellValue("");
						R9Cell8.setCellStyle(textStyle);
					}
					// R9 Col L
					Cell R9Cell9 = row.createCell(11);
					if (record.getR9_bal_sub_diaries() != null) {
						R9Cell9.setCellValue(record.getR9_bal_sub_diaries().doubleValue());
						R9Cell9.setCellStyle(numberStyle);
					} else {
						R9Cell9.setCellValue("");
						R9Cell9.setCellStyle(textStyle);
					}
					// R9 Col M
					Cell R9Cell10 = row.createCell(12);
					if (record.getR9_bal_sub_diaries_bwp() != null) {
						R9Cell10.setCellValue(record.getR9_bal_sub_diaries_bwp().doubleValue());
						R9Cell10.setCellStyle(numberStyle);
					} else {
						R9Cell10.setCellValue("");
						R9Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(9);
					Cell R10Cell1 = row.createCell(3);
					if (record.getR10_fig_bal_sheet() != null) {
						R10Cell1.setCellValue(record.getR10_fig_bal_sheet().doubleValue());
						R10Cell1.setCellStyle(numberStyle);
					} else {
						R10Cell1.setCellValue("");
						R10Cell1.setCellStyle(textStyle);
					}

					// R10 Col E
					Cell R10Cell2 = row.createCell(4);
					if (record.getR10_fig_bal_sheet_bwp() != null) {
						R10Cell2.setCellValue(record.getR10_fig_bal_sheet_bwp().doubleValue());
						R10Cell2.setCellStyle(numberStyle);
					} else {
						R10Cell2.setCellValue("");
						R10Cell2.setCellStyle(textStyle);
					}

					// R10 Col F
					Cell R10Cell3 = row.createCell(5);
					if (record.getR10_amt_statement_adj() != null) {
						R10Cell3.setCellValue(record.getR10_amt_statement_adj().doubleValue());
						R10Cell3.setCellStyle(numberStyle);
					} else {
						R10Cell3.setCellValue("");
						R10Cell3.setCellStyle(textStyle);
					}
					// R10 Col G
					Cell R10Cell4 = row.createCell(6);
					if (record.getR10_amt_statement_adj_bwp() != null) {
						R10Cell4.setCellValue(record.getR10_amt_statement_adj_bwp().doubleValue());
						R10Cell4.setCellStyle(numberStyle);
					} else {
						R10Cell4.setCellValue("");
						R10Cell4.setCellStyle(textStyle);
					}
					// R10 Col H
					// Cell R10Cell5 = row.createCell(7);
					// if (record.getR10_net_amt() != null) {
					// R10Cell5.setCellValue(record.getR10_net_amt().doubleValue());
					// R10Cell5.setCellStyle(numberStyle);
					// } else {
					// R10Cell5.setCellValue("");
					// R10Cell5.setCellStyle(textStyle);
					// }
					// R10 Col I
					Cell R10Cell6 = row.createCell(8);
					if (record.getR10_net_amt_bwp() != null) {
						R10Cell6.setCellValue(record.getR10_net_amt_bwp().doubleValue());
						R10Cell6.setCellStyle(numberStyle);
					} else {
						R10Cell6.setCellValue("");
						R10Cell6.setCellStyle(textStyle);
					}
					// R10 Col J
					Cell R10Cell7 = row.createCell(9);
					if (record.getR10_bal_sub() != null) {
						R10Cell7.setCellValue(record.getR10_bal_sub().doubleValue());
						R10Cell7.setCellStyle(numberStyle);
					} else {
						R10Cell7.setCellValue("");
						R10Cell7.setCellStyle(textStyle);
					}
					// R10 Col K
					Cell R10Cell8 = row.createCell(10);
					if (record.getR10_bal_sub_bwp() != null) {
						R10Cell8.setCellValue(record.getR10_bal_sub_bwp().doubleValue());
						R10Cell8.setCellStyle(numberStyle);
					} else {
						R10Cell8.setCellValue("");
						R10Cell8.setCellStyle(textStyle);
					}
					// R10 Col L
					Cell R10Cell9 = row.createCell(11);
					if (record.getR10_bal_sub_diaries() != null) {
						R10Cell9.setCellValue(record.getR10_bal_sub_diaries().doubleValue());
						R10Cell9.setCellStyle(numberStyle);
					} else {
						R10Cell9.setCellValue("");
						R10Cell9.setCellStyle(textStyle);
					}
					// R10 Col M
					Cell R10Cell10 = row.createCell(12);
					if (record.getR10_bal_sub_diaries_bwp() != null) {
						R10Cell10.setCellValue(record.getR10_bal_sub_diaries_bwp().doubleValue());
						R10Cell10.setCellStyle(numberStyle);
					} else {
						R10Cell10.setCellValue("");
						R10Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(10);
					Cell R11Cell1 = row.createCell(3);
					if (record.getR11_fig_bal_sheet() != null) {
						R11Cell1.setCellValue(record.getR11_fig_bal_sheet().doubleValue());
						R11Cell1.setCellStyle(numberStyle);
					} else {
						R11Cell1.setCellValue("");
						R11Cell1.setCellStyle(textStyle);
					}

					// R11 Col E
					Cell R11Cell2 = row.createCell(4);
					if (record.getR11_fig_bal_sheet_bwp() != null) {
						R11Cell2.setCellValue(record.getR11_fig_bal_sheet_bwp().doubleValue());
						R11Cell2.setCellStyle(numberStyle);
					} else {
						R11Cell2.setCellValue("");
						R11Cell2.setCellStyle(textStyle);
					}

					// R11 Col F
					Cell R11Cell3 = row.createCell(5);
					if (record.getR11_amt_statement_adj() != null) {
						R11Cell3.setCellValue(record.getR11_amt_statement_adj().doubleValue());
						R11Cell3.setCellStyle(numberStyle);
					} else {
						R11Cell3.setCellValue("");
						R11Cell3.setCellStyle(textStyle);
					}
					// R11 Col G
					Cell R11Cell4 = row.createCell(6);
					if (record.getR11_amt_statement_adj_bwp() != null) {
						R11Cell4.setCellValue(record.getR11_amt_statement_adj_bwp().doubleValue());
						R11Cell4.setCellStyle(numberStyle);
					} else {
						R11Cell4.setCellValue("");
						R11Cell4.setCellStyle(textStyle);
					}
					// // R11 Col H
					// Cell R11Cell5 = row.createCell(7);
					// if (record.getR11_net_amt() != null) {
					// R11Cell5.setCellValue(record.getR11_net_amt().doubleValue());
					// R11Cell5.setCellStyle(numberStyle);
					// } else {
					// R11Cell5.setCellValue("");
					// R11Cell5.setCellStyle(textStyle);
					// }
					// R11 Col I
					Cell R11Cell6 = row.createCell(8);
					if (record.getR11_net_amt_bwp() != null) {
						R11Cell6.setCellValue(record.getR11_net_amt_bwp().doubleValue());
						R11Cell6.setCellStyle(numberStyle);
					} else {
						R11Cell6.setCellValue("");
						R11Cell6.setCellStyle(textStyle);
					}
					// R11 Col J
					Cell R11Cell7 = row.createCell(9);
					if (record.getR11_bal_sub() != null) {
						R11Cell7.setCellValue(record.getR11_bal_sub().doubleValue());
						R11Cell7.setCellStyle(numberStyle);
					} else {
						R11Cell7.setCellValue("");
						R11Cell7.setCellStyle(textStyle);
					}
					// R11 Col K
					Cell R11Cell8 = row.createCell(10);
					if (record.getR11_bal_sub_bwp() != null) {
						R11Cell8.setCellValue(record.getR11_bal_sub_bwp().doubleValue());
						R11Cell8.setCellStyle(numberStyle);
					} else {
						R11Cell8.setCellValue("");
						R11Cell8.setCellStyle(textStyle);
					}
					// R11 Col L
					Cell R11Cell9 = row.createCell(11);
					if (record.getR11_bal_sub_diaries() != null) {
						R11Cell9.setCellValue(record.getR11_bal_sub_diaries().doubleValue());
						R11Cell9.setCellStyle(numberStyle);
					} else {
						R11Cell9.setCellValue("");
						R11Cell9.setCellStyle(textStyle);
					}
					// R11 Col M
					Cell R11Cell10 = row.createCell(12);
					if (record.getR11_bal_sub_diaries_bwp() != null) {
						R11Cell10.setCellValue(record.getR11_bal_sub_diaries_bwp().doubleValue());
						R11Cell10.setCellStyle(numberStyle);
					} else {
						R11Cell10.setCellValue("");
						R11Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(11);
					Cell R12Cell1 = row.createCell(3);
					if (record.getR12_fig_bal_sheet() != null) {
						R12Cell1.setCellValue(record.getR12_fig_bal_sheet().doubleValue());
						R12Cell1.setCellStyle(numberStyle);
					} else {
						R12Cell1.setCellValue("");
						R12Cell1.setCellStyle(textStyle);
					}

					// R12 Col E
					Cell R12Cell2 = row.createCell(4);
					if (record.getR12_fig_bal_sheet_bwp() != null) {
						R12Cell2.setCellValue(record.getR12_fig_bal_sheet_bwp().doubleValue());
						R12Cell2.setCellStyle(numberStyle);
					} else {
						R12Cell2.setCellValue("");
						R12Cell2.setCellStyle(textStyle);
					}

					// R12 Col F
					Cell R12Cell3 = row.createCell(5);
					if (record.getR12_amt_statement_adj() != null) {
						R12Cell3.setCellValue(record.getR12_amt_statement_adj().doubleValue());
						R12Cell3.setCellStyle(numberStyle);
					} else {
						R12Cell3.setCellValue("");
						R12Cell3.setCellStyle(textStyle);
					}
					// R12 Col G
					Cell R12Cell4 = row.createCell(6);
					if (record.getR12_amt_statement_adj_bwp() != null) {
						R12Cell4.setCellValue(record.getR12_amt_statement_adj_bwp().doubleValue());
						R12Cell4.setCellStyle(numberStyle);
					} else {
						R12Cell4.setCellValue("");
						R12Cell4.setCellStyle(textStyle);
					}
					// R12 Col H
					// Cell R12Cell5 = row.createCell(7);
					// if (record.getR12_net_amt() != null) {
					// R12Cell5.setCellValue(record.getR12_net_amt().doubleValue());
					// R12Cell5.setCellStyle(numberStyle);
					// } else {
					// R12Cell5.setCellValue("");
					// R12Cell5.setCellStyle(textStyle);
					// }
					// R12 Col I
					Cell R12Cell6 = row.createCell(8);
					if (record.getR12_net_amt_bwp() != null) {
						R12Cell6.setCellValue(record.getR12_net_amt_bwp().doubleValue());
						R12Cell6.setCellStyle(numberStyle);
					} else {
						R12Cell6.setCellValue("");
						R12Cell6.setCellStyle(textStyle);
					}
					// R12 Col J
					Cell R12Cell7 = row.createCell(9);
					if (record.getR12_bal_sub() != null) {
						R12Cell7.setCellValue(record.getR12_bal_sub().doubleValue());
						R12Cell7.setCellStyle(numberStyle);
					} else {
						R12Cell7.setCellValue("");
						R12Cell7.setCellStyle(textStyle);
					}
					// R12 Col K
					Cell R12Cell8 = row.createCell(10);
					if (record.getR12_bal_sub_bwp() != null) {
						R12Cell8.setCellValue(record.getR12_bal_sub_bwp().doubleValue());
						R12Cell8.setCellStyle(numberStyle);
					} else {
						R12Cell8.setCellValue("");
						R12Cell8.setCellStyle(textStyle);
					}
					// R12 Col L
					Cell R12Cell9 = row.createCell(11);
					if (record.getR12_bal_sub_diaries() != null) {
						R12Cell9.setCellValue(record.getR12_bal_sub_diaries().doubleValue());
						R12Cell9.setCellStyle(numberStyle);
					} else {
						R12Cell9.setCellValue("");
						R12Cell9.setCellStyle(textStyle);
					}

					// R12 Col M
					Cell R12Cell10 = row.createCell(12);
					if (record.getR12_bal_sub_diaries_bwp() != null) {
						R12Cell10.setCellValue(record.getR12_bal_sub_diaries_bwp().doubleValue());
						R12Cell10.setCellStyle(numberStyle);
					} else {
						R12Cell10.setCellValue("");
						R12Cell10.setCellStyle(textStyle);
					}
					// row = sheet.getRow(12);
					// Cell R13Cell1 = row.createCell(3);
					// if (record.getR13_fig_bal_sheet() != null) {
					// R13Cell1.setCellValue(record.getR13_fig_bal_sheet().doubleValue());
					// R13Cell1.setCellStyle(numberStyle);
					// } else {
					// R13Cell1.setCellValue("");
					// R13Cell1.setCellStyle(textStyle);
					// }

					// // R13 Col E
					// Cell R13Cell2 = row.createCell(4);
					// if (record.getR13_fig_bal_sheet_bwp() != null) {
					// R13Cell2.setCellValue(record.getR13_fig_bal_sheet_bwp().doubleValue());
					// R13Cell2.setCellStyle(numberStyle);
					// } else {
					// R13Cell2.setCellValue("");
					// R13Cell2.setCellStyle(textStyle);
					// }

					// // R13 Col F
					// Cell R13Cell3 = row.createCell(5);
					// if (record.getR13_amt_statement_adj() != null) {
					// R13Cell3.setCellValue(record.getR13_amt_statement_adj().doubleValue());
					// R13Cell3.setCellStyle(numberStyle);
					// } else {
					// R13Cell3.setCellValue("");
					// R13Cell3.setCellStyle(textStyle);
					// }
					// // R13 Col G
					// Cell R13Cell4 = row.createCell(6);
					// if (record.getR13_amt_statement_adj_bwp() != null) {
					// R13Cell4.setCellValue(record.getR13_amt_statement_adj_bwp().doubleValue());
					// R13Cell4.setCellStyle(numberStyle);
					// } else {
					// R13Cell4.setCellValue("");
					// R13Cell4.setCellStyle(textStyle);
					// }
					// // R13 Col H
					// Cell R13Cell5 = row.createCell(7);
					// if (record.getR13_net_amt() != null) {
					// R13Cell5.setCellValue(record.getR13_net_amt().doubleValue());
					// R13Cell5.setCellStyle(numberStyle);
					// } else {
					// R13Cell5.setCellValue("");
					// R13Cell5.setCellStyle(textStyle);
					// }
					// // R13 Col I
					// Cell R13Cell6 = row.createCell(8);
					// if (record.getR13_net_amt_bwp() != null) {
					// R13Cell6.setCellValue(record.getR13_net_amt_bwp().doubleValue());
					// R13Cell6.setCellStyle(numberStyle);
					// } else {
					// R13Cell6.setCellValue("");
					// R13Cell6.setCellStyle(textStyle);
					// }
					// // R13 Col J
					// Cell R13Cell7 = row.createCell(9);
					// if (record.getR13_bal_sub() != null) {
					// R13Cell7.setCellValue(record.getR13_bal_sub().doubleValue());
					// R13Cell7.setCellStyle(numberStyle);
					// } else {
					// R13Cell7.setCellValue("");
					// R13Cell7.setCellStyle(textStyle);
					// }
					// // R13 Col K
					// Cell R13Cell8 = row.createCell(10);
					// if (record.getR13_bal_sub_bwp() != null) {
					// R13Cell8.setCellValue(record.getR13_bal_sub_bwp().doubleValue());
					// R13Cell8.setCellStyle(numberStyle);
					// } else {
					// R13Cell8.setCellValue("");
					// R13Cell8.setCellStyle(textStyle);
					// }
					// // R13 Col L
					// Cell R13Cell9 = row.createCell(11);
					// if (record.getR13_bal_sub_diaries() != null) {
					// R13Cell9.setCellValue(record.getR13_bal_sub_diaries().doubleValue());
					// R13Cell9.setCellStyle(numberStyle);
					// } else {
					// R13Cell9.setCellValue("");
					// R13Cell9.setCellStyle(textStyle);
					// }
					// // R13 Col M
					// Cell R13Cell10 = row.createCell(12);
					// if (record.getR13_bal_sub_diaries_bwp() != null) {
					// R13Cell10.setCellValue(record.getR13_bal_sub_diaries_bwp().doubleValue());
					// R13Cell10.setCellStyle(numberStyle);
					// } else {
					// R13Cell10.setCellValue("");
					// R13Cell10.setCellStyle(textStyle);
					// }

					row = sheet.getRow(16);
					Cell R17Cell1 = row.createCell(3);
					if (record.getR17_fig_bal_sheet() != null) {
						R17Cell1.setCellValue(record.getR17_fig_bal_sheet().doubleValue());
						R17Cell1.setCellStyle(numberStyle);
					} else {
						R17Cell1.setCellValue("");
						R17Cell1.setCellStyle(textStyle);
					}

					// R17 Col E
					Cell R17Cell2 = row.createCell(4);
					if (record.getR17_fig_bal_sheet_bwp() != null) {
						R17Cell2.setCellValue(record.getR17_fig_bal_sheet_bwp().doubleValue());
						R17Cell2.setCellStyle(numberStyle);
					} else {
						R17Cell2.setCellValue("");
						R17Cell2.setCellStyle(textStyle);
					}

					// R17 Col F
					Cell R17Cell3 = row.createCell(5);
					if (record.getR17_amt_statement_adj() != null) {
						R17Cell3.setCellValue(record.getR17_amt_statement_adj().doubleValue());
						R17Cell3.setCellStyle(numberStyle);
					} else {
						R17Cell3.setCellValue("");
						R17Cell3.setCellStyle(textStyle);
					}
					// R17 Col G
					Cell R17Cell4 = row.createCell(6);
					if (record.getR17_amt_statement_adj_bwp() != null) {
						R17Cell4.setCellValue(record.getR17_amt_statement_adj_bwp().doubleValue());
						R17Cell4.setCellStyle(numberStyle);
					} else {
						R17Cell4.setCellValue("");
						R17Cell4.setCellStyle(textStyle);
					}
					// R17 Col H
					// Cell R17Cell5 = row.createCell(7);
					// if (record.getR17_net_amt() != null) {
					// R17Cell5.setCellValue(record.getR17_net_amt().doubleValue());
					// R17Cell5.setCellStyle(numberStyle);
					// } else {
					// R17Cell5.setCellValue("");
					// R17Cell5.setCellStyle(textStyle);
					// }
					// R17 Col I
					Cell R17Cell6 = row.createCell(8);
					if (record.getR17_net_amt_bwp() != null) {
						R17Cell6.setCellValue(record.getR17_net_amt_bwp().doubleValue());
						R17Cell6.setCellStyle(numberStyle);
					} else {
						R17Cell6.setCellValue("");
						R17Cell6.setCellStyle(textStyle);
					}
					// R17 Col J
					Cell R17Cell7 = row.createCell(9);
					if (record.getR17_bal_sub() != null) {
						R17Cell7.setCellValue(record.getR17_bal_sub().doubleValue());
						R17Cell7.setCellStyle(numberStyle);
					} else {
						R17Cell7.setCellValue("");
						R17Cell7.setCellStyle(textStyle);
					}
					// R17 Col K
					Cell R17Cell8 = row.createCell(10);
					if (record.getR17_bal_sub_bwp() != null) {
						R17Cell8.setCellValue(record.getR17_bal_sub_bwp().doubleValue());
						R17Cell8.setCellStyle(numberStyle);
					} else {
						R17Cell8.setCellValue("");
						R17Cell8.setCellStyle(textStyle);
					}
					// R17 Col L
					Cell R17Cell9 = row.createCell(11);
					if (record.getR17_bal_sub_diaries() != null) {
						R17Cell9.setCellValue(record.getR17_bal_sub_diaries().doubleValue());
						R17Cell9.setCellStyle(numberStyle);
					} else {
						R17Cell9.setCellValue("");
						R17Cell9.setCellStyle(textStyle);
					}
					// R17 Col M
					Cell R17Cell10 = row.createCell(12);
					if (record.getR17_bal_sub_diaries_bwp() != null) {
						R17Cell10.setCellValue(record.getR17_bal_sub_diaries_bwp().doubleValue());
						R17Cell10.setCellStyle(numberStyle);
					} else {
						R17Cell10.setCellValue("");
						R17Cell10.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					Cell R18Cell1 = row.createCell(3);
					if (record.getR18_fig_bal_sheet() != null) {
						R18Cell1.setCellValue(record.getR18_fig_bal_sheet().doubleValue());
						R18Cell1.setCellStyle(numberStyle);
					} else {
						R18Cell1.setCellValue("");
						R18Cell1.setCellStyle(textStyle);
					}

					// R18 Col E
					Cell R18Cell2 = row.createCell(4);
					if (record.getR18_fig_bal_sheet_bwp() != null) {
						R18Cell2.setCellValue(record.getR18_fig_bal_sheet_bwp().doubleValue());
						R18Cell2.setCellStyle(numberStyle);
					} else {
						R18Cell2.setCellValue("");
						R18Cell2.setCellStyle(textStyle);
					}

					// R18 Col F
					Cell R18Cell3 = row.createCell(5);
					if (record.getR18_amt_statement_adj() != null) {
						R18Cell3.setCellValue(record.getR18_amt_statement_adj().doubleValue());
						R18Cell3.setCellStyle(numberStyle);
					} else {
						R18Cell3.setCellValue("");
						R18Cell3.setCellStyle(textStyle);
					}
					// R18 Col G
					Cell R18Cell4 = row.createCell(6);
					if (record.getR18_amt_statement_adj_bwp() != null) {
						R18Cell4.setCellValue(record.getR18_amt_statement_adj_bwp().doubleValue());
						R18Cell4.setCellStyle(numberStyle);
					} else {
						R18Cell4.setCellValue("");
						R18Cell4.setCellStyle(textStyle);
					}
					// // R18 Col H
					// Cell R18Cell5 = row.createCell(7);
					// if (record.getR18_net_amt() != null) {
					// R18Cell5.setCellValue(record.getR18_net_amt().doubleValue());
					// R18Cell5.setCellStyle(numberStyle);
					// } else {
					// R18Cell5.setCellValue("");
					// R18Cell5.setCellStyle(textStyle);
					// }
					// R18 Col I
					Cell R18Cell6 = row.createCell(8);
					if (record.getR18_net_amt_bwp() != null) {
						R18Cell6.setCellValue(record.getR18_net_amt_bwp().doubleValue());
						R18Cell6.setCellStyle(numberStyle);
					} else {
						R18Cell6.setCellValue("");
						R18Cell6.setCellStyle(textStyle);
					}
					// R18 Col J
					Cell R18Cell7 = row.createCell(9);
					if (record.getR18_bal_sub() != null) {
						R18Cell7.setCellValue(record.getR18_bal_sub().doubleValue());
						R18Cell7.setCellStyle(numberStyle);
					} else {
						R18Cell7.setCellValue("");
						R18Cell7.setCellStyle(textStyle);
					}
					// R18 Col K
					Cell R18Cell8 = row.createCell(10);
					if (record.getR18_bal_sub_bwp() != null) {
						R18Cell8.setCellValue(record.getR18_bal_sub_bwp().doubleValue());
						R18Cell8.setCellStyle(numberStyle);
					} else {
						R18Cell8.setCellValue("");
						R18Cell8.setCellStyle(textStyle);
					}
					// R18 Col L
					Cell R18Cell9 = row.createCell(11);
					if (record.getR18_bal_sub_diaries() != null) {
						R18Cell9.setCellValue(record.getR18_bal_sub_diaries().doubleValue());
						R18Cell9.setCellStyle(numberStyle);
					} else {
						R18Cell9.setCellValue("");
						R18Cell9.setCellStyle(textStyle);
					}
					// R18 Col M
					Cell R18Cell10 = row.createCell(12);
					if (record.getR18_bal_sub_diaries_bwp() != null) {
						R18Cell10.setCellValue(record.getR18_bal_sub_diaries_bwp().doubleValue());
						R18Cell10.setCellStyle(numberStyle);
					} else {
						R18Cell10.setCellValue("");
						R18Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(18);
					Cell R19Cell1 = row.createCell(3);
					if (record.getR19_fig_bal_sheet() != null) {
						R19Cell1.setCellValue(record.getR19_fig_bal_sheet().doubleValue());
						R19Cell1.setCellStyle(numberStyle);
					} else {
						R19Cell1.setCellValue("");
						R19Cell1.setCellStyle(textStyle);
					}

					// R19 Col E
					Cell R19Cell2 = row.createCell(4);
					if (record.getR19_fig_bal_sheet_bwp() != null) {
						R19Cell2.setCellValue(record.getR19_fig_bal_sheet_bwp().doubleValue());
						R19Cell2.setCellStyle(numberStyle);
					} else {
						R19Cell2.setCellValue("");
						R19Cell2.setCellStyle(textStyle);
					}

					// R19 Col F
					Cell R19Cell3 = row.createCell(5);
					if (record.getR19_amt_statement_adj() != null) {
						R19Cell3.setCellValue(record.getR19_amt_statement_adj().doubleValue());
						R19Cell3.setCellStyle(numberStyle);
					} else {
						R19Cell3.setCellValue("");
						R19Cell3.setCellStyle(textStyle);
					}
					// R19 Col G
					Cell R19Cell4 = row.createCell(6);
					if (record.getR19_amt_statement_adj_bwp() != null) {
						R19Cell4.setCellValue(record.getR19_amt_statement_adj_bwp().doubleValue());
						R19Cell4.setCellStyle(numberStyle);
					} else {
						R19Cell4.setCellValue("");
						R19Cell4.setCellStyle(textStyle);
					}
					// R19 Col H
					// Cell R19Cell5 = row.createCell(7);
					// if (record.getR19_net_amt() != null) {
					// R19Cell5.setCellValue(record.getR19_net_amt().doubleValue());
					// R19Cell5.setCellStyle(numberStyle);
					// } else {
					// R19Cell5.setCellValue("");
					// R19Cell5.setCellStyle(textStyle);
					// }
					// R19 Col I
					Cell R19Cell6 = row.createCell(8);
					if (record.getR19_net_amt_bwp() != null) {
						R19Cell6.setCellValue(record.getR19_net_amt_bwp().doubleValue());
						R19Cell6.setCellStyle(numberStyle);
					} else {
						R19Cell6.setCellValue("");
						R19Cell6.setCellStyle(textStyle);
					}
					// R19 Col J
					Cell R19Cell7 = row.createCell(9);
					if (record.getR19_bal_sub() != null) {
						R19Cell7.setCellValue(record.getR19_bal_sub().doubleValue());
						R19Cell7.setCellStyle(numberStyle);
					} else {
						R19Cell7.setCellValue("");
						R19Cell7.setCellStyle(textStyle);
					}
					// R19 Col K
					Cell R19Cell8 = row.createCell(10);
					if (record.getR19_bal_sub_bwp() != null) {
						R19Cell8.setCellValue(record.getR19_bal_sub_bwp().doubleValue());
						R19Cell8.setCellStyle(numberStyle);
					} else {
						R19Cell8.setCellValue("");
						R19Cell8.setCellStyle(textStyle);
					}
					// R19 Col L
					Cell R19Cell9 = row.createCell(11);
					if (record.getR19_bal_sub_diaries() != null) {
						R19Cell9.setCellValue(record.getR19_bal_sub_diaries().doubleValue());
						R19Cell9.setCellStyle(numberStyle);
					} else {
						R19Cell9.setCellValue("");
						R19Cell9.setCellStyle(textStyle);
					}
					// R19 Col M
					Cell R19Cell10 = row.createCell(12);
					if (record.getR19_bal_sub_diaries_bwp() != null) {
						R19Cell10.setCellValue(record.getR19_bal_sub_diaries_bwp().doubleValue());
						R19Cell10.setCellStyle(numberStyle);
					} else {
						R19Cell10.setCellValue("");
						R19Cell10.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					Cell R20Cell1 = row.createCell(3);
					if (record.getR20_fig_bal_sheet() != null) {
						R20Cell1.setCellValue(record.getR20_fig_bal_sheet().doubleValue());
						R20Cell1.setCellStyle(numberStyle);
					} else {
						R20Cell1.setCellValue("");
						R20Cell1.setCellStyle(textStyle);
					}

					// R20 Col E
					Cell R20Cell2 = row.createCell(4);
					if (record.getR20_fig_bal_sheet_bwp() != null) {
						R20Cell2.setCellValue(record.getR20_fig_bal_sheet_bwp().doubleValue());
						R20Cell2.setCellStyle(numberStyle);
					} else {
						R20Cell2.setCellValue("");
						R20Cell2.setCellStyle(textStyle);
					}

					// R20 Col F
					Cell R20Cell3 = row.createCell(5);
					if (record.getR20_amt_statement_adj() != null) {
						R20Cell3.setCellValue(record.getR20_amt_statement_adj().doubleValue());
						R20Cell3.setCellStyle(numberStyle);
					} else {
						R20Cell3.setCellValue("");
						R20Cell3.setCellStyle(textStyle);
					}
					// R20 Col G
					Cell R20Cell4 = row.createCell(6);
					if (record.getR20_amt_statement_adj_bwp() != null) {
						R20Cell4.setCellValue(record.getR20_amt_statement_adj_bwp().doubleValue());
						R20Cell4.setCellStyle(numberStyle);
					} else {
						R20Cell4.setCellValue("");
						R20Cell4.setCellStyle(textStyle);
					}
					// // R20 Col H
					// Cell R20Cell5 = row.createCell(7);
					// if (record.getR20_net_amt() != null) {
					// R20Cell5.setCellValue(record.getR20_net_amt().doubleValue());
					// R20Cell5.setCellStyle(numberStyle);
					// } else {
					// R20Cell5.setCellValue("");
					// R20Cell5.setCellStyle(textStyle);
					// }
					// R20 Col I
					Cell R20Cell6 = row.createCell(8);
					if (record.getR20_net_amt_bwp() != null) {
						R20Cell6.setCellValue(record.getR20_net_amt_bwp().doubleValue());
						R20Cell6.setCellStyle(numberStyle);
					} else {
						R20Cell6.setCellValue("");
						R20Cell6.setCellStyle(textStyle);
					}
					// R20 Col J
					Cell R20Cell7 = row.createCell(9);
					if (record.getR20_bal_sub() != null) {
						R20Cell7.setCellValue(record.getR20_bal_sub().doubleValue());
						R20Cell7.setCellStyle(numberStyle);
					} else {
						R20Cell7.setCellValue("");
						R20Cell7.setCellStyle(textStyle);
					}
					// R20 Col K
					Cell R20Cell8 = row.createCell(10);
					if (record.getR20_bal_sub_bwp() != null) {
						R20Cell8.setCellValue(record.getR20_bal_sub_bwp().doubleValue());
						R20Cell8.setCellStyle(numberStyle);
					} else {
						R20Cell8.setCellValue("");
						R20Cell8.setCellStyle(textStyle);
					}
					// R20 Col L
					Cell R20Cell9 = row.createCell(11);
					if (record.getR20_bal_sub_diaries() != null) {
						R20Cell9.setCellValue(record.getR20_bal_sub_diaries().doubleValue());
						R20Cell9.setCellStyle(numberStyle);
					} else {
						R20Cell9.setCellValue("");
						R20Cell9.setCellStyle(textStyle);
					}
					// R20 Col M
					Cell R20Cell10 = row.createCell(12);
					if (record.getR20_bal_sub_diaries_bwp() != null) {
						R20Cell10.setCellValue(record.getR20_bal_sub_diaries_bwp().doubleValue());
						R20Cell10.setCellStyle(numberStyle);
					} else {
						R20Cell10.setCellValue("");
						R20Cell10.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					Cell R21Cell1 = row.createCell(3);
					if (record.getR21_fig_bal_sheet() != null) {
						R21Cell1.setCellValue(record.getR21_fig_bal_sheet().doubleValue());
						R21Cell1.setCellStyle(numberStyle);
					} else {
						R21Cell1.setCellValue("");
						R21Cell1.setCellStyle(textStyle);
					}

					// R21 Col E
					Cell R21Cell2 = row.createCell(4);
					if (record.getR21_fig_bal_sheet_bwp() != null) {
						R21Cell2.setCellValue(record.getR21_fig_bal_sheet_bwp().doubleValue());
						R21Cell2.setCellStyle(numberStyle);
					} else {
						R21Cell2.setCellValue("");
						R21Cell2.setCellStyle(textStyle);
					}

					// R21 Col F
					Cell R21Cell3 = row.createCell(5);
					if (record.getR21_amt_statement_adj() != null) {
						R21Cell3.setCellValue(record.getR21_amt_statement_adj().doubleValue());
						R21Cell3.setCellStyle(numberStyle);
					} else {
						R21Cell3.setCellValue("");
						R21Cell3.setCellStyle(textStyle);
					}
					// R21 Col G
					Cell R21Cell4 = row.createCell(6);
					if (record.getR21_amt_statement_adj_bwp() != null) {
						R21Cell4.setCellValue(record.getR21_amt_statement_adj_bwp().doubleValue());
						R21Cell4.setCellStyle(numberStyle);
					} else {
						R21Cell4.setCellValue("");
						R21Cell4.setCellStyle(textStyle);
					}
					// // R21 Col H
					// Cell R21Cell5 = row.createCell(7);
					// if (record.getR21_net_amt() != null) {
					// R21Cell5.setCellValue(record.getR21_net_amt().doubleValue());
					// R21Cell5.setCellStyle(numberStyle);
					// } else {
					// R21Cell5.setCellValue("");
					// R21Cell5.setCellStyle(textStyle);
					// }
					// R21 Col I
					Cell R21Cell6 = row.createCell(8);
					if (record.getR21_net_amt_bwp() != null) {
						R21Cell6.setCellValue(record.getR21_net_amt_bwp().doubleValue());
						R21Cell6.setCellStyle(numberStyle);
					} else {
						R21Cell6.setCellValue("");
						R21Cell6.setCellStyle(textStyle);
					}
					// R21 Col J
					Cell R21Cell7 = row.createCell(9);
					if (record.getR21_bal_sub() != null) {
						R21Cell7.setCellValue(record.getR21_bal_sub().doubleValue());
						R21Cell7.setCellStyle(numberStyle);
					} else {
						R21Cell7.setCellValue("");
						R21Cell7.setCellStyle(textStyle);
					}
					// R21 Col K
					Cell R21Cell8 = row.createCell(10);
					if (record.getR21_bal_sub_bwp() != null) {
						R21Cell8.setCellValue(record.getR21_bal_sub_bwp().doubleValue());
						R21Cell8.setCellStyle(numberStyle);
					} else {
						R21Cell8.setCellValue("");
						R21Cell8.setCellStyle(textStyle);
					}
					// R21 Col L
					Cell R21Cell9 = row.createCell(11);
					if (record.getR21_bal_sub_diaries() != null) {
						R21Cell9.setCellValue(record.getR21_bal_sub_diaries().doubleValue());
						R21Cell9.setCellStyle(numberStyle);
					} else {
						R21Cell9.setCellValue("");
						R21Cell9.setCellStyle(textStyle);
					}
					// R21 Col M
					Cell R21Cell10 = row.createCell(12);
					if (record.getR21_bal_sub_diaries_bwp() != null) {
						R21Cell10.setCellValue(record.getR21_bal_sub_diaries_bwp().doubleValue());
						R21Cell10.setCellStyle(numberStyle);
					} else {
						R21Cell10.setCellValue("");
						R21Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(21);
					Cell R22Cell1 = row.createCell(3);
					if (record.getR22_fig_bal_sheet() != null) {
						R22Cell1.setCellValue(record.getR22_fig_bal_sheet().doubleValue());
						R22Cell1.setCellStyle(numberStyle);
					} else {
						R22Cell1.setCellValue("");
						R22Cell1.setCellStyle(textStyle);
					}

					// R22 Col E
					Cell R22Cell2 = row.createCell(4);
					if (record.getR22_fig_bal_sheet_bwp() != null) {
						R22Cell2.setCellValue(record.getR22_fig_bal_sheet_bwp().doubleValue());
						R22Cell2.setCellStyle(numberStyle);
					} else {
						R22Cell2.setCellValue("");
						R22Cell2.setCellStyle(textStyle);
					}

					// R22 Col F
					Cell R22Cell3 = row.createCell(5);
					if (record.getR22_amt_statement_adj() != null) {
						R22Cell3.setCellValue(record.getR22_amt_statement_adj().doubleValue());
						R22Cell3.setCellStyle(numberStyle);
					} else {
						R22Cell3.setCellValue("");
						R22Cell3.setCellStyle(textStyle);
					}
					// R22 Col G
					Cell R22Cell4 = row.createCell(6);
					if (record.getR22_amt_statement_adj_bwp() != null) {
						R22Cell4.setCellValue(record.getR22_amt_statement_adj_bwp().doubleValue());
						R22Cell4.setCellStyle(numberStyle);
					} else {
						R22Cell4.setCellValue("");
						R22Cell4.setCellStyle(textStyle);
					}
					// // R22 Col H
					// Cell R22Cell5 = row.createCell(7);
					// if (record.getR22_net_amt() != null) {
					// R22Cell5.setCellValue(record.getR22_net_amt().doubleValue());
					// R22Cell5.setCellStyle(numberStyle);
					// } else {
					// R22Cell5.setCellValue("");
					// R22Cell5.setCellStyle(textStyle);
					// }
					// R22 Col I
					Cell R22Cell6 = row.createCell(8);
					if (record.getR22_net_amt_bwp() != null) {
						R22Cell6.setCellValue(record.getR22_net_amt_bwp().doubleValue());
						R22Cell6.setCellStyle(numberStyle);
					} else {
						R22Cell6.setCellValue("");
						R22Cell6.setCellStyle(textStyle);
					}
					// R22 Col J
					Cell R22Cell7 = row.createCell(9);
					if (record.getR22_bal_sub() != null) {
						R22Cell7.setCellValue(record.getR22_bal_sub().doubleValue());
						R22Cell7.setCellStyle(numberStyle);
					} else {
						R22Cell7.setCellValue("");
						R22Cell7.setCellStyle(textStyle);
					}
					// R22 Col K
					Cell R22Cell8 = row.createCell(10);
					if (record.getR22_bal_sub_bwp() != null) {
						R22Cell8.setCellValue(record.getR22_bal_sub_bwp().doubleValue());
						R22Cell8.setCellStyle(numberStyle);
					} else {
						R22Cell8.setCellValue("");
						R22Cell8.setCellStyle(textStyle);
					}
					// R22 Col L
					Cell R22Cell9 = row.createCell(11);
					if (record.getR22_bal_sub_diaries() != null) {
						R22Cell9.setCellValue(record.getR22_bal_sub_diaries().doubleValue());
						R22Cell9.setCellStyle(numberStyle);
					} else {
						R22Cell9.setCellValue("");
						R22Cell9.setCellStyle(textStyle);
					}
					// R22 Col M
					Cell R22Cell10 = row.createCell(12);
					if (record.getR22_bal_sub_diaries_bwp() != null) {
						R22Cell10.setCellValue(record.getR22_bal_sub_diaries_bwp().doubleValue());
						R22Cell10.setCellStyle(numberStyle);
					} else {
						R22Cell10.setCellValue("");
						R22Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(22);
					Cell R23Cell1 = row.createCell(3);
					if (record.getR23_fig_bal_sheet() != null) {
						R23Cell1.setCellValue(record.getR23_fig_bal_sheet().doubleValue());
						R23Cell1.setCellStyle(numberStyle);
					} else {
						R23Cell1.setCellValue("");
						R23Cell1.setCellStyle(textStyle);
					}

					// R23 Col E
					Cell R23Cell2 = row.createCell(4);
					if (record.getR23_fig_bal_sheet_bwp() != null) {
						R23Cell2.setCellValue(record.getR23_fig_bal_sheet_bwp().doubleValue());
						R23Cell2.setCellStyle(numberStyle);
					} else {
						R23Cell2.setCellValue("");
						R23Cell2.setCellStyle(textStyle);
					}

					// R23 Col F
					Cell R23Cell3 = row.createCell(5);
					if (record.getR23_amt_statement_adj() != null) {
						R23Cell3.setCellValue(record.getR23_amt_statement_adj().doubleValue());
						R23Cell3.setCellStyle(numberStyle);
					} else {
						R23Cell3.setCellValue("");
						R23Cell3.setCellStyle(textStyle);
					}
					// R23 Col G
					Cell R23Cell4 = row.createCell(6);
					if (record.getR23_amt_statement_adj_bwp() != null) {
						R23Cell4.setCellValue(record.getR23_amt_statement_adj_bwp().doubleValue());
						R23Cell4.setCellStyle(numberStyle);
					} else {
						R23Cell4.setCellValue("");
						R23Cell4.setCellStyle(textStyle);
					}
					// // R23 Col H
					// Cell R23Cell5 = row.createCell(7);
					// if (record.getR23_net_amt() != null) {
					// R23Cell5.setCellValue(record.getR23_net_amt().doubleValue());
					// R23Cell5.setCellStyle(numberStyle);
					// } else {
					// R23Cell5.setCellValue("");
					// R23Cell5.setCellStyle(textStyle);
					// }
					// R23 Col I
					Cell R23Cell6 = row.createCell(8);
					if (record.getR23_net_amt_bwp() != null) {
						R23Cell6.setCellValue(record.getR23_net_amt_bwp().doubleValue());
						R23Cell6.setCellStyle(numberStyle);
					} else {
						R23Cell6.setCellValue("");
						R23Cell6.setCellStyle(textStyle);
					}
					// R23 Col J
					Cell R23Cell7 = row.createCell(9);
					if (record.getR23_bal_sub() != null) {
						R23Cell7.setCellValue(record.getR23_bal_sub().doubleValue());
						R23Cell7.setCellStyle(numberStyle);
					} else {
						R23Cell7.setCellValue("");
						R23Cell7.setCellStyle(textStyle);
					}
					// R23 Col K
					Cell R23Cell8 = row.createCell(10);
					if (record.getR23_bal_sub_bwp() != null) {
						R23Cell8.setCellValue(record.getR23_bal_sub_bwp().doubleValue());
						R23Cell8.setCellStyle(numberStyle);
					} else {
						R23Cell8.setCellValue("");
						R23Cell8.setCellStyle(textStyle);
					}
					// R23 Col L
					Cell R23Cell9 = row.createCell(11);
					if (record.getR23_bal_sub_diaries() != null) {
						R23Cell9.setCellValue(record.getR23_bal_sub_diaries().doubleValue());
						R23Cell9.setCellStyle(numberStyle);
					} else {
						R23Cell9.setCellValue("");
						R23Cell9.setCellStyle(textStyle);
					}
					// R23 Col M
					Cell R23Cell10 = row.createCell(12);
					if (record.getR23_bal_sub_diaries_bwp() != null) {
						R23Cell10.setCellValue(record.getR23_bal_sub_diaries_bwp().doubleValue());
						R23Cell10.setCellStyle(numberStyle);
					} else {
						R23Cell10.setCellValue("");
						R23Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(23);
					Cell R24Cell1 = row.createCell(3);
					if (record.getR24_fig_bal_sheet() != null) {
						R24Cell1.setCellValue(record.getR24_fig_bal_sheet().doubleValue());
						R24Cell1.setCellStyle(numberStyle);
					} else {
						R24Cell1.setCellValue("");
						R24Cell1.setCellStyle(textStyle);
					}

					// R24 Col E
					Cell R24Cell2 = row.createCell(4);
					if (record.getR24_fig_bal_sheet_bwp() != null) {
						R24Cell2.setCellValue(record.getR24_fig_bal_sheet_bwp().doubleValue());
						R24Cell2.setCellStyle(numberStyle);
					} else {
						R24Cell2.setCellValue("");
						R24Cell2.setCellStyle(textStyle);
					}

					// R24 Col F
					Cell R24Cell3 = row.createCell(5);
					if (record.getR24_amt_statement_adj() != null) {
						R24Cell3.setCellValue(record.getR24_amt_statement_adj().doubleValue());
						R24Cell3.setCellStyle(numberStyle);
					} else {
						R24Cell3.setCellValue("");
						R24Cell3.setCellStyle(textStyle);
					}
					// R24 Col G
					Cell R24Cell4 = row.createCell(6);
					if (record.getR24_amt_statement_adj_bwp() != null) {
						R24Cell4.setCellValue(record.getR24_amt_statement_adj_bwp().doubleValue());
						R24Cell4.setCellStyle(numberStyle);
					} else {
						R24Cell4.setCellValue("");
						R24Cell4.setCellStyle(textStyle);
					}
					// // R24 Col H
					// Cell R24Cell5 = row.createCell(7);
					// if (record.getR24_net_amt() != null) {
					// R24Cell5.setCellValue(record.getR24_net_amt().doubleValue());
					// R24Cell5.setCellStyle(numberStyle);
					// } else {
					// R24Cell5.setCellValue("");
					// R24Cell5.setCellStyle(textStyle);
					// }
					// R24 Col I
					Cell R24Cell6 = row.createCell(8);
					if (record.getR24_net_amt_bwp() != null) {
						R24Cell6.setCellValue(record.getR24_net_amt_bwp().doubleValue());
						R24Cell6.setCellStyle(numberStyle);
					} else {
						R24Cell6.setCellValue("");
						R24Cell6.setCellStyle(textStyle);
					}
					// R24 Col J
					Cell R24Cell7 = row.createCell(9);
					if (record.getR24_bal_sub() != null) {
						R24Cell7.setCellValue(record.getR24_bal_sub().doubleValue());
						R24Cell7.setCellStyle(numberStyle);
					} else {
						R24Cell7.setCellValue("");
						R24Cell7.setCellStyle(textStyle);
					}
					// R24 Col K
					Cell R24Cell8 = row.createCell(10);
					if (record.getR24_bal_sub_bwp() != null) {
						R24Cell8.setCellValue(record.getR24_bal_sub_bwp().doubleValue());
						R24Cell8.setCellStyle(numberStyle);
					} else {
						R24Cell8.setCellValue("");
						R24Cell8.setCellStyle(textStyle);
					}
					// R24 Col L
					Cell R24Cell9 = row.createCell(11);
					if (record.getR24_bal_sub_diaries() != null) {
						R24Cell9.setCellValue(record.getR24_bal_sub_diaries().doubleValue());
						R24Cell9.setCellStyle(numberStyle);
					} else {
						R24Cell9.setCellValue("");
						R24Cell9.setCellStyle(textStyle);
					}
					// R24 Col M
					Cell R24Cell10 = row.createCell(12);
					if (record.getR24_bal_sub_diaries_bwp() != null) {
						R24Cell10.setCellValue(record.getR24_bal_sub_diaries_bwp().doubleValue());
						R24Cell10.setCellStyle(numberStyle);
					} else {
						R24Cell10.setCellValue("");
						R24Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(24);
					Cell R25Cell1 = row.createCell(3);
					if (record.getR25_fig_bal_sheet() != null) {
						R25Cell1.setCellValue(record.getR25_fig_bal_sheet().doubleValue());
						R25Cell1.setCellStyle(numberStyle);
					} else {
						R25Cell1.setCellValue("");
						R25Cell1.setCellStyle(textStyle);
					}

					// R25 Col E
					Cell R25Cell2 = row.createCell(4);
					if (record.getR25_fig_bal_sheet_bwp() != null) {
						R25Cell2.setCellValue(record.getR25_fig_bal_sheet_bwp().doubleValue());
						R25Cell2.setCellStyle(numberStyle);
					} else {
						R25Cell2.setCellValue("");
						R25Cell2.setCellStyle(textStyle);
					}

					// R25 Col F
					Cell R25Cell3 = row.createCell(5);
					if (record.getR25_amt_statement_adj() != null) {
						R25Cell3.setCellValue(record.getR25_amt_statement_adj().doubleValue());
						R25Cell3.setCellStyle(numberStyle);
					} else {
						R25Cell3.setCellValue("");
						R25Cell3.setCellStyle(textStyle);
					}
					// R25 Col G
					Cell R25Cell4 = row.createCell(6);
					if (record.getR25_amt_statement_adj_bwp() != null) {
						R25Cell4.setCellValue(record.getR25_amt_statement_adj_bwp().doubleValue());
						R25Cell4.setCellStyle(numberStyle);
					} else {
						R25Cell4.setCellValue("");
						R25Cell4.setCellStyle(textStyle);
					}
					// // R25 Col H
					// Cell R25Cell5 = row.createCell(7);
					// if (record.getR25_net_amt() != null) {
					// R25Cell5.setCellValue(record.getR25_net_amt().doubleValue());
					// R25Cell5.setCellStyle(numberStyle);
					// } else {
					// R25Cell5.setCellValue("");
					// R25Cell5.setCellStyle(textStyle);
					// }
					// R25 Col I
					Cell R25Cell6 = row.createCell(8);
					if (record.getR25_net_amt_bwp() != null) {
						R25Cell6.setCellValue(record.getR25_net_amt_bwp().doubleValue());
						R25Cell6.setCellStyle(numberStyle);
					} else {
						R25Cell6.setCellValue("");
						R25Cell6.setCellStyle(textStyle);
					}
					// R25 Col J
					Cell R25Cell7 = row.createCell(9);
					if (record.getR25_bal_sub() != null) {
						R25Cell7.setCellValue(record.getR25_bal_sub().doubleValue());
						R25Cell7.setCellStyle(numberStyle);
					} else {
						R25Cell7.setCellValue("");
						R25Cell7.setCellStyle(textStyle);
					}
					// R25 Col K
					Cell R25Cell8 = row.createCell(10);
					if (record.getR25_bal_sub_bwp() != null) {
						R25Cell8.setCellValue(record.getR25_bal_sub_bwp().doubleValue());
						R25Cell8.setCellStyle(numberStyle);
					} else {
						R25Cell8.setCellValue("");
						R25Cell8.setCellStyle(textStyle);
					}
					// R25 Col L
					Cell R25Cell9 = row.createCell(11);
					if (record.getR25_bal_sub_diaries() != null) {
						R25Cell9.setCellValue(record.getR25_bal_sub_diaries().doubleValue());
						R25Cell9.setCellStyle(numberStyle);
					} else {
						R25Cell9.setCellValue("");
						R25Cell9.setCellStyle(textStyle);
					}
					// R25 Col M
					Cell R25Cell10 = row.createCell(12);
					if (record.getR25_bal_sub_diaries_bwp() != null) {
						R25Cell10.setCellValue(record.getR25_bal_sub_diaries_bwp().doubleValue());
						R25Cell10.setCellStyle(numberStyle);
					} else {
						R25Cell10.setCellValue("");
						R25Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(25);
					Cell R26Cell1 = row.createCell(3);
					if (record.getR26_fig_bal_sheet() != null) {
						R26Cell1.setCellValue(record.getR26_fig_bal_sheet().doubleValue());
						R26Cell1.setCellStyle(numberStyle);
					} else {
						R26Cell1.setCellValue("");
						R26Cell1.setCellStyle(textStyle);
					}

					// R26 Col E
					Cell R26Cell2 = row.createCell(4);
					if (record.getR26_fig_bal_sheet_bwp() != null) {
						R26Cell2.setCellValue(record.getR26_fig_bal_sheet_bwp().doubleValue());
						R26Cell2.setCellStyle(numberStyle);
					} else {
						R26Cell2.setCellValue("");
						R26Cell2.setCellStyle(textStyle);
					}

					// R26 Col F
					Cell R26Cell3 = row.createCell(5);
					if (record.getR26_amt_statement_adj() != null) {
						R26Cell3.setCellValue(record.getR26_amt_statement_adj().doubleValue());
						R26Cell3.setCellStyle(numberStyle);
					} else {
						R26Cell3.setCellValue("");
						R26Cell3.setCellStyle(textStyle);
					}
					// R26 Col G
					Cell R26Cell4 = row.createCell(6);
					if (record.getR26_amt_statement_adj_bwp() != null) {
						R26Cell4.setCellValue(record.getR26_amt_statement_adj_bwp().doubleValue());
						R26Cell4.setCellStyle(numberStyle);
					} else {
						R26Cell4.setCellValue("");
						R26Cell4.setCellStyle(textStyle);
					}
					// // R26 Col H
					// Cell R26Cell5 = row.createCell(7);
					// if (record.getR26_net_amt() != null) {
					// R26Cell5.setCellValue(record.getR26_net_amt().doubleValue());
					// R26Cell5.setCellStyle(numberStyle);
					// } else {
					// R26Cell5.setCellValue("");
					// R26Cell5.setCellStyle(textStyle);
					// }
					// R26 Col I
					Cell R26Cell6 = row.createCell(8);
					if (record.getR26_net_amt_bwp() != null) {
						R26Cell6.setCellValue(record.getR26_net_amt_bwp().doubleValue());
						R26Cell6.setCellStyle(numberStyle);
					} else {
						R26Cell6.setCellValue("");
						R26Cell6.setCellStyle(textStyle);
					}
					// R26 Col J
					Cell R26Cell7 = row.createCell(9);
					if (record.getR26_bal_sub() != null) {
						R26Cell7.setCellValue(record.getR26_bal_sub().doubleValue());
						R26Cell7.setCellStyle(numberStyle);
					} else {
						R26Cell7.setCellValue("");
						R26Cell7.setCellStyle(textStyle);
					}
					// R26 Col K
					Cell R26Cell8 = row.createCell(10);
					if (record.getR26_bal_sub_bwp() != null) {
						R26Cell8.setCellValue(record.getR26_bal_sub_bwp().doubleValue());
						R26Cell8.setCellStyle(numberStyle);
					} else {
						R26Cell8.setCellValue("");
						R26Cell8.setCellStyle(textStyle);
					}
					// R26 Col L
					Cell R26Cell9 = row.createCell(11);
					if (record.getR26_bal_sub_diaries() != null) {
						R26Cell9.setCellValue(record.getR26_bal_sub_diaries().doubleValue());
						R26Cell9.setCellStyle(numberStyle);
					} else {
						R26Cell9.setCellValue("");
						R26Cell9.setCellStyle(textStyle);
					}
					// R26 Col M
					Cell R26Cell10 = row.createCell(12);
					if (record.getR26_bal_sub_diaries_bwp() != null) {
						R26Cell10.setCellValue(record.getR26_bal_sub_diaries_bwp().doubleValue());
						R26Cell10.setCellStyle(numberStyle);
					} else {
						R26Cell10.setCellValue("");
						R26Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(26);
					Cell R27Cell1 = row.createCell(3);
					if (record.getR27_fig_bal_sheet() != null) {
						R27Cell1.setCellValue(record.getR27_fig_bal_sheet().doubleValue());
						R27Cell1.setCellStyle(numberStyle);
					} else {
						R27Cell1.setCellValue("");
						R27Cell1.setCellStyle(textStyle);
					}

					// R27 Col E
					Cell R27Cell2 = row.createCell(4);
					if (record.getR27_fig_bal_sheet_bwp() != null) {
						R27Cell2.setCellValue(record.getR27_fig_bal_sheet_bwp().doubleValue());
						R27Cell2.setCellStyle(numberStyle);
					} else {
						R27Cell2.setCellValue("");
						R27Cell2.setCellStyle(textStyle);
					}

					// R27 Col F
					Cell R27Cell3 = row.createCell(5);
					if (record.getR27_amt_statement_adj() != null) {
						R27Cell3.setCellValue(record.getR27_amt_statement_adj().doubleValue());
						R27Cell3.setCellStyle(numberStyle);
					} else {
						R27Cell3.setCellValue("");
						R27Cell3.setCellStyle(textStyle);
					}
					// R27 Col G
					Cell R27Cell4 = row.createCell(6);
					if (record.getR27_amt_statement_adj_bwp() != null) {
						R27Cell4.setCellValue(record.getR27_amt_statement_adj_bwp().doubleValue());
						R27Cell4.setCellStyle(numberStyle);
					} else {
						R27Cell4.setCellValue("");
						R27Cell4.setCellStyle(textStyle);
					}
					// // R27 Col H
					// Cell R27Cell5 = row.createCell(7);
					// if (record.getR27_net_amt() != null) {
					// R27Cell5.setCellValue(record.getR27_net_amt().doubleValue());
					// R27Cell5.setCellStyle(numberStyle);
					// } else {
					// R27Cell5.setCellValue("");
					// R27Cell5.setCellStyle(textStyle);
					// }
					// R27 Col I
					Cell R27Cell6 = row.createCell(8);
					if (record.getR27_net_amt_bwp() != null) {
						R27Cell6.setCellValue(record.getR27_net_amt_bwp().doubleValue());
						R27Cell6.setCellStyle(numberStyle);
					} else {
						R27Cell6.setCellValue("");
						R27Cell6.setCellStyle(textStyle);
					}
					// R27 Col J
					Cell R27Cell7 = row.createCell(9);
					if (record.getR27_bal_sub() != null) {
						R27Cell7.setCellValue(record.getR27_bal_sub().doubleValue());
						R27Cell7.setCellStyle(numberStyle);
					} else {
						R27Cell7.setCellValue("");
						R27Cell7.setCellStyle(textStyle);
					}
					// R27 Col K
					Cell R27Cell8 = row.createCell(10);
					if (record.getR27_bal_sub_bwp() != null) {
						R27Cell8.setCellValue(record.getR27_bal_sub_bwp().doubleValue());
						R27Cell8.setCellStyle(numberStyle);
					} else {
						R27Cell8.setCellValue("");
						R27Cell8.setCellStyle(textStyle);
					}
					// R27 Col L
					Cell R27Cell9 = row.createCell(11);
					if (record.getR27_bal_sub_diaries() != null) {
						R27Cell9.setCellValue(record.getR27_bal_sub_diaries().doubleValue());
						R27Cell9.setCellStyle(numberStyle);
					} else {
						R27Cell9.setCellValue("");
						R27Cell9.setCellStyle(textStyle);
					}
					// R27 Col M
					Cell R27Cell10 = row.createCell(12);
					if (record.getR27_bal_sub_diaries_bwp() != null) {
						R27Cell10.setCellValue(record.getR27_bal_sub_diaries_bwp().doubleValue());
						R27Cell10.setCellStyle(numberStyle);
					} else {
						R27Cell10.setCellValue("");
						R27Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(27);
					Cell R28Cell1 = row.createCell(3);
					if (record.getR28_fig_bal_sheet() != null) {
						R28Cell1.setCellValue(record.getR28_fig_bal_sheet().doubleValue());
						R28Cell1.setCellStyle(numberStyle);
					} else {
						R28Cell1.setCellValue("");
						R28Cell1.setCellStyle(textStyle);
					}

					// R28 Col E
					Cell R28Cell2 = row.createCell(4);
					if (record.getR28_fig_bal_sheet_bwp() != null) {
						R28Cell2.setCellValue(record.getR28_fig_bal_sheet_bwp().doubleValue());
						R28Cell2.setCellStyle(numberStyle);
					} else {
						R28Cell2.setCellValue("");
						R28Cell2.setCellStyle(textStyle);
					}

					// R28 Col F
					Cell R28Cell3 = row.createCell(5);
					if (record.getR28_amt_statement_adj() != null) {
						R28Cell3.setCellValue(record.getR28_amt_statement_adj().doubleValue());
						R28Cell3.setCellStyle(numberStyle);
					} else {
						R28Cell3.setCellValue("");
						R28Cell3.setCellStyle(textStyle);
					}
					// R28 Col G
					Cell R28Cell4 = row.createCell(6);
					if (record.getR28_amt_statement_adj_bwp() != null) {
						R28Cell4.setCellValue(record.getR28_amt_statement_adj_bwp().doubleValue());
						R28Cell4.setCellStyle(numberStyle);
					} else {
						R28Cell4.setCellValue("");
						R28Cell4.setCellStyle(textStyle);
					}
					// // R28 Col H
					// Cell R28Cell5 = row.createCell(7);
					// if (record.getR28_net_amt() != null) {
					// R28Cell5.setCellValue(record.getR28_net_amt().doubleValue());
					// R28Cell5.setCellStyle(numberStyle);
					// } else {
					// R28Cell5.setCellValue("");
					// R28Cell5.setCellStyle(textStyle);
					// }
					// R28 Col I
					Cell R28Cell6 = row.createCell(8);
					if (record.getR28_net_amt_bwp() != null) {
						R28Cell6.setCellValue(record.getR28_net_amt_bwp().doubleValue());
						R28Cell6.setCellStyle(numberStyle);
					} else {
						R28Cell6.setCellValue("");
						R28Cell6.setCellStyle(textStyle);
					}
					// R28 Col J
					Cell R28Cell7 = row.createCell(9);
					if (record.getR28_bal_sub() != null) {
						R28Cell7.setCellValue(record.getR28_bal_sub().doubleValue());
						R28Cell7.setCellStyle(numberStyle);
					} else {
						R28Cell7.setCellValue("");
						R28Cell7.setCellStyle(textStyle);
					}
					// R28 Col K
					Cell R28Cell8 = row.createCell(10);
					if (record.getR28_bal_sub_bwp() != null) {
						R28Cell8.setCellValue(record.getR28_bal_sub_bwp().doubleValue());
						R28Cell8.setCellStyle(numberStyle);
					} else {
						R28Cell8.setCellValue("");
						R28Cell8.setCellStyle(textStyle);
					}
					// R28 Col L
					Cell R28Cell9 = row.createCell(11);
					if (record.getR28_bal_sub_diaries() != null) {
						R28Cell9.setCellValue(record.getR28_bal_sub_diaries().doubleValue());
						R28Cell9.setCellStyle(numberStyle);
					} else {
						R28Cell9.setCellValue("");
						R28Cell9.setCellStyle(textStyle);
					}
					// R28 Col M
					Cell R28Cell10 = row.createCell(12);
					if (record.getR28_bal_sub_diaries_bwp() != null) {
						R28Cell10.setCellValue(record.getR28_bal_sub_diaries_bwp().doubleValue());
						R28Cell10.setCellStyle(numberStyle);
					} else {
						R28Cell10.setCellValue("");
						R28Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(28);
					Cell R29Cell1 = row.createCell(3);
					if (record.getR29_fig_bal_sheet() != null) {
						R29Cell1.setCellValue(record.getR29_fig_bal_sheet().doubleValue());
						R29Cell1.setCellStyle(numberStyle);
					} else {
						R29Cell1.setCellValue("");
						R29Cell1.setCellStyle(textStyle);
					}

					// R29 Col E
					Cell R29Cell2 = row.createCell(4);
					if (record.getR29_fig_bal_sheet_bwp() != null) {
						R29Cell2.setCellValue(record.getR29_fig_bal_sheet_bwp().doubleValue());
						R29Cell2.setCellStyle(numberStyle);
					} else {
						R29Cell2.setCellValue("");
						R29Cell2.setCellStyle(textStyle);
					}

					// R29 Col F
					Cell R29Cell3 = row.createCell(5);
					if (record.getR29_amt_statement_adj() != null) {
						R29Cell3.setCellValue(record.getR29_amt_statement_adj().doubleValue());
						R29Cell3.setCellStyle(numberStyle);
					} else {
						R29Cell3.setCellValue("");
						R29Cell3.setCellStyle(textStyle);
					}
					// R29 Col G
					Cell R29Cell4 = row.createCell(6);
					if (record.getR29_amt_statement_adj_bwp() != null) {
						R29Cell4.setCellValue(record.getR29_amt_statement_adj_bwp().doubleValue());
						R29Cell4.setCellStyle(numberStyle);
					} else {
						R29Cell4.setCellValue("");
						R29Cell4.setCellStyle(textStyle);
					}
					// // R29 Col H
					// Cell R29Cell5 = row.createCell(7);
					// if (record.getR29_net_amt() != null) {
					// R29Cell5.setCellValue(record.getR29_net_amt().doubleValue());
					// R29Cell5.setCellStyle(numberStyle);
					// } else {
					// R29Cell5.setCellValue("");
					// R29Cell5.setCellStyle(textStyle);
					// }
					// R29 Col I
					Cell R29Cell6 = row.createCell(8);
					if (record.getR29_net_amt_bwp() != null) {
						R29Cell6.setCellValue(record.getR29_net_amt_bwp().doubleValue());
						R29Cell6.setCellStyle(numberStyle);
					} else {
						R29Cell6.setCellValue("");
						R29Cell6.setCellStyle(textStyle);
					}
					// R29 Col J
					Cell R29Cell7 = row.createCell(9);
					if (record.getR29_bal_sub() != null) {
						R29Cell7.setCellValue(record.getR29_bal_sub().doubleValue());
						R29Cell7.setCellStyle(numberStyle);
					} else {
						R29Cell7.setCellValue("");
						R29Cell7.setCellStyle(textStyle);
					}
					// R29 Col K
					Cell R29Cell8 = row.createCell(10);
					if (record.getR29_bal_sub_bwp() != null) {
						R29Cell8.setCellValue(record.getR29_bal_sub_bwp().doubleValue());
						R29Cell8.setCellStyle(numberStyle);
					} else {
						R29Cell8.setCellValue("");
						R29Cell8.setCellStyle(textStyle);
					}
					// R29 Col L
					Cell R29Cell9 = row.createCell(11);
					if (record.getR29_bal_sub_diaries() != null) {
						R29Cell9.setCellValue(record.getR29_bal_sub_diaries().doubleValue());
						R29Cell9.setCellStyle(numberStyle);
					} else {
						R29Cell9.setCellValue("");
						R29Cell9.setCellStyle(textStyle);
					}
					// R29 Col M
					Cell R29Cell10 = row.createCell(12);
					if (record.getR29_bal_sub_diaries_bwp() != null) {
						R29Cell10.setCellValue(record.getR29_bal_sub_diaries_bwp().doubleValue());
						R29Cell10.setCellStyle(numberStyle);
					} else {
						R29Cell10.setCellValue("");
						R29Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(29);
					Cell R30Cell1 = row.createCell(3);
					if (record.getR30_fig_bal_sheet() != null) {
						R30Cell1.setCellValue(record.getR30_fig_bal_sheet().doubleValue());
						R30Cell1.setCellStyle(numberStyle);
					} else {
						R30Cell1.setCellValue("");
						R30Cell1.setCellStyle(textStyle);
					}

					// R30 Col E
					Cell R30Cell2 = row.createCell(4);
					if (record.getR30_fig_bal_sheet_bwp() != null) {
						R30Cell2.setCellValue(record.getR30_fig_bal_sheet_bwp().doubleValue());
						R30Cell2.setCellStyle(numberStyle);
					} else {
						R30Cell2.setCellValue("");
						R30Cell2.setCellStyle(textStyle);
					}

					// R30 Col F
					Cell R30Cell3 = row.createCell(5);
					if (record.getR30_amt_statement_adj() != null) {
						R30Cell3.setCellValue(record.getR30_amt_statement_adj().doubleValue());
						R30Cell3.setCellStyle(numberStyle);
					} else {
						R30Cell3.setCellValue("");
						R30Cell3.setCellStyle(textStyle);
					}
					// R30 Col G
					Cell R30Cell4 = row.createCell(6);
					if (record.getR30_amt_statement_adj_bwp() != null) {
						R30Cell4.setCellValue(record.getR30_amt_statement_adj_bwp().doubleValue());
						R30Cell4.setCellStyle(numberStyle);
					} else {
						R30Cell4.setCellValue("");
						R30Cell4.setCellStyle(textStyle);
					}
					// // R30 Col H
					// Cell R30Cell5 = row.createCell(7);
					// if (record.getR30_net_amt() != null) {
					// R30Cell5.setCellValue(record.getR30_net_amt().doubleValue());
					// R30Cell5.setCellStyle(numberStyle);
					// } else {
					// R30Cell5.setCellValue("");
					// R30Cell5.setCellStyle(textStyle);
					// }
					// R30 Col I
					Cell R30Cell6 = row.createCell(8);
					if (record.getR30_net_amt_bwp() != null) {
						R30Cell6.setCellValue(record.getR30_net_amt_bwp().doubleValue());
						R30Cell6.setCellStyle(numberStyle);
					} else {
						R30Cell6.setCellValue("");
						R30Cell6.setCellStyle(textStyle);
					}
					// R30 Col J
					Cell R30Cell7 = row.createCell(9);
					if (record.getR30_bal_sub() != null) {
						R30Cell7.setCellValue(record.getR30_bal_sub().doubleValue());
						R30Cell7.setCellStyle(numberStyle);
					} else {
						R30Cell7.setCellValue("");
						R30Cell7.setCellStyle(textStyle);
					}
					// R30 Col K
					Cell R30Cell8 = row.createCell(10);
					if (record.getR30_bal_sub_bwp() != null) {
						R30Cell8.setCellValue(record.getR30_bal_sub_bwp().doubleValue());
						R30Cell8.setCellStyle(numberStyle);
					} else {
						R30Cell8.setCellValue("");
						R30Cell8.setCellStyle(textStyle);
					}
					// R30 Col L
					Cell R30Cell9 = row.createCell(11);
					if (record.getR30_bal_sub_diaries() != null) {
						R30Cell9.setCellValue(record.getR30_bal_sub_diaries().doubleValue());
						R30Cell9.setCellStyle(numberStyle);
					} else {
						R30Cell9.setCellValue("");
						R30Cell9.setCellStyle(textStyle);
					}
					// R30 Col M
					Cell R30Cell10 = row.createCell(12);
					if (record.getR30_bal_sub_diaries_bwp() != null) {
						R30Cell10.setCellValue(record.getR30_bal_sub_diaries_bwp().doubleValue());
						R30Cell10.setCellStyle(numberStyle);
					} else {
						R30Cell10.setCellValue("");
						R30Cell10.setCellStyle(textStyle);
					}
					// row = sheet.getRow(30);
					// Cell R31Cell1 = row.createCell(3);
					// if (record.getR31_fig_bal_sheet() != null) {
					// R31Cell1.setCellValue(record.getR31_fig_bal_sheet().doubleValue());
					// R31Cell1.setCellStyle(numberStyle);
					// } else {
					// R31Cell1.setCellValue("");
					// R31Cell1.setCellStyle(textStyle);
					// }

					// // R31 Col E
					// Cell R31Cell2 = row.createCell(4);
					// if (record.getR31_fig_bal_sheet_bwp() != null) {
					// R31Cell2.setCellValue(record.getR31_fig_bal_sheet_bwp().doubleValue());
					// R31Cell2.setCellStyle(numberStyle);
					// } else {
					// R31Cell2.setCellValue("");
					// R31Cell2.setCellStyle(textStyle);
					// }

					// // R31 Col F
					// Cell R31Cell3 = row.createCell(5);
					// if (record.getR31_amt_statement_adj() != null) {
					// R31Cell3.setCellValue(record.getR31_amt_statement_adj().doubleValue());
					// R31Cell3.setCellStyle(numberStyle);
					// } else {
					// R31Cell3.setCellValue("");
					// R31Cell3.setCellStyle(textStyle);
					// }
					// // R31 Col G
					// Cell R31Cell4 = row.createCell(6);
					// if (record.getR31_amt_statement_adj_bwp() != null) {
					// R31Cell4.setCellValue(record.getR31_amt_statement_adj_bwp().doubleValue());
					// R31Cell4.setCellStyle(numberStyle);
					// } else {
					// R31Cell4.setCellValue("");
					// R31Cell4.setCellStyle(textStyle);
					// }
					// // R31 Col H
					// Cell R31Cell5 = row.createCell(7);
					// if (record.getR31_net_amt() != null) {
					// R31Cell5.setCellValue(record.getR31_net_amt().doubleValue());
					// R31Cell5.setCellStyle(numberStyle);
					// } else {
					// R31Cell5.setCellValue("");
					// R31Cell5.setCellStyle(textStyle);
					// }
					// // R31 Col I
					// Cell R31Cell6 = row.createCell(8);
					// if (record.getR31_net_amt_bwp() != null) {
					// R31Cell6.setCellValue(record.getR31_net_amt_bwp().doubleValue());
					// R31Cell6.setCellStyle(numberStyle);
					// } else {
					// R31Cell6.setCellValue("");
					// R31Cell6.setCellStyle(textStyle);
					// }
					// // R31 Col J
					// Cell R31Cell7 = row.createCell(9);
					// if (record.getR31_bal_sub() != null) {
					// R31Cell7.setCellValue(record.getR31_bal_sub().doubleValue());
					// R31Cell7.setCellStyle(numberStyle);
					// } else {
					// R31Cell7.setCellValue("");
					// R31Cell7.setCellStyle(textStyle);
					// }
					// // R31 Col K
					// Cell R31Cell8 = row.createCell(10);
					// if (record.getR31_bal_sub_bwp() != null) {
					// R31Cell8.setCellValue(record.getR31_bal_sub_bwp().doubleValue());
					// R31Cell8.setCellStyle(numberStyle);
					// } else {
					// R31Cell8.setCellValue("");
					// R31Cell8.setCellStyle(textStyle);
					// }
					// // R31 Col L
					// Cell R31Cell9 = row.createCell(11);
					// if (record.getR31_bal_sub_diaries() != null) {
					// R31Cell9.setCellValue(record.getR31_bal_sub_diaries().doubleValue());
					// R31Cell9.setCellStyle(numberStyle);
					// } else {
					// R31Cell9.setCellValue("");
					// R31Cell9.setCellStyle(textStyle);
					// }
					// // R31 Col M
					// Cell R31Cell10 = row.createCell(12);
					// if (record.getR31_bal_sub_diaries_bwp() != null) {
					// R31Cell10.setCellValue(record.getR31_bal_sub_diaries_bwp().doubleValue());
					// R31Cell10.setCellStyle(numberStyle);
					// } else {
					// R31Cell10.setCellValue("");
					// R31Cell10.setCellStyle(textStyle);
					// }
					row = sheet.getRow(39);
					Cell R40Cell1 = row.createCell(3);
					if (record.getR40_fig_bal_sheet() != null) {
						R40Cell1.setCellValue(record.getR40_fig_bal_sheet().doubleValue());
						R40Cell1.setCellStyle(numberStyle);
					} else {
						R40Cell1.setCellValue("");
						R40Cell1.setCellStyle(textStyle);
					}

					// R40 Col E
					Cell R40Cell2 = row.createCell(4);
					if (record.getR40_fig_bal_sheet_bwp() != null) {
						R40Cell2.setCellValue(record.getR40_fig_bal_sheet_bwp().doubleValue());
						R40Cell2.setCellStyle(numberStyle);
					} else {
						R40Cell2.setCellValue("");
						R40Cell2.setCellStyle(textStyle);
					}

					// R40 Col F
					Cell R40Cell3 = row.createCell(5);
					if (record.getR40_amt_statement_adj() != null) {
						R40Cell3.setCellValue(record.getR40_amt_statement_adj().doubleValue());
						R40Cell3.setCellStyle(numberStyle);
					} else {
						R40Cell3.setCellValue("");
						R40Cell3.setCellStyle(textStyle);
					}
					// R40 Col G
					Cell R40Cell4 = row.createCell(6);
					if (record.getR40_amt_statement_adj_bwp() != null) {
						R40Cell4.setCellValue(record.getR40_amt_statement_adj_bwp().doubleValue());
						R40Cell4.setCellStyle(numberStyle);
					} else {
						R40Cell4.setCellValue("");
						R40Cell4.setCellStyle(textStyle);
					}
					// // R40 Col H
					// Cell R40Cell5 = row.createCell(7);
					// if (record.getR40_net_amt() != null) {
					// R40Cell5.setCellValue(record.getR40_net_amt().doubleValue());
					// R40Cell5.setCellStyle(numberStyle);
					// } else {
					// R40Cell5.setCellValue("");
					// R40Cell5.setCellStyle(textStyle);
					// }
					// R40 Col I
					Cell R40Cell6 = row.createCell(8);
					if (record.getR40_net_amt_bwp() != null) {
						R40Cell6.setCellValue(record.getR40_net_amt_bwp().doubleValue());
						R40Cell6.setCellStyle(numberStyle);
					} else {
						R40Cell6.setCellValue("");
						R40Cell6.setCellStyle(textStyle);
					}
					// R40 Col J
					Cell R40Cell7 = row.createCell(9);
					if (record.getR40_bal_sub() != null) {
						R40Cell7.setCellValue(record.getR40_bal_sub().doubleValue());
						R40Cell7.setCellStyle(numberStyle);
					} else {
						R40Cell7.setCellValue("");
						R40Cell7.setCellStyle(textStyle);
					}
					// R40 Col K
					Cell R40Cell8 = row.createCell(10);
					if (record.getR40_bal_sub_bwp() != null) {
						R40Cell8.setCellValue(record.getR40_bal_sub_bwp().doubleValue());
						R40Cell8.setCellStyle(numberStyle);
					} else {
						R40Cell8.setCellValue("");
						R40Cell8.setCellStyle(textStyle);
					}
					// R40 Col L
					Cell R40Cell9 = row.createCell(11);
					if (record.getR40_bal_sub_diaries() != null) {
						R40Cell9.setCellValue(record.getR40_bal_sub_diaries().doubleValue());
						R40Cell9.setCellStyle(numberStyle);
					} else {
						R40Cell9.setCellValue("");
						R40Cell9.setCellStyle(textStyle);
					}
					// R40 Col M
					Cell R40Cell10 = row.createCell(12);
					if (record.getR40_bal_sub_diaries_bwp() != null) {
						R40Cell10.setCellValue(record.getR40_bal_sub_diaries_bwp().doubleValue());
						R40Cell10.setCellStyle(numberStyle);
					} else {
						R40Cell10.setCellValue("");
						R40Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(40);
					Cell R41Cell1 = row.createCell(3);
					if (record.getR41_fig_bal_sheet() != null) {
						R41Cell1.setCellValue(record.getR41_fig_bal_sheet().doubleValue());
						R41Cell1.setCellStyle(numberStyle);
					} else {
						R41Cell1.setCellValue("");
						R41Cell1.setCellStyle(textStyle);
					}

					// R41 Col E
					Cell R41Cell2 = row.createCell(4);
					if (record.getR41_fig_bal_sheet_bwp() != null) {
						R41Cell2.setCellValue(record.getR41_fig_bal_sheet_bwp().doubleValue());
						R41Cell2.setCellStyle(numberStyle);
					} else {
						R41Cell2.setCellValue("");
						R41Cell2.setCellStyle(textStyle);
					}

					// R41 Col F
					Cell R41Cell3 = row.createCell(5);
					if (record.getR41_amt_statement_adj() != null) {
						R41Cell3.setCellValue(record.getR41_amt_statement_adj().doubleValue());
						R41Cell3.setCellStyle(numberStyle);
					} else {
						R41Cell3.setCellValue("");
						R41Cell3.setCellStyle(textStyle);
					}
					// R41 Col G
					Cell R41Cell4 = row.createCell(6);
					if (record.getR41_amt_statement_adj_bwp() != null) {
						R41Cell4.setCellValue(record.getR41_amt_statement_adj_bwp().doubleValue());
						R41Cell4.setCellStyle(numberStyle);
					} else {
						R41Cell4.setCellValue("");
						R41Cell4.setCellStyle(textStyle);
					}
					// // R41 Col H
					// Cell R41Cell5 = row.createCell(7);
					// if (record.getR41_net_amt() != null) {
					// R41Cell5.setCellValue(record.getR41_net_amt().doubleValue());
					// R41Cell5.setCellStyle(numberStyle);
					// } else {
					// R41Cell5.setCellValue("");
					// R41Cell5.setCellStyle(textStyle);
					// }
					// R41 Col I
					Cell R41Cell6 = row.createCell(8);
					if (record.getR41_net_amt_bwp() != null) {
						R41Cell6.setCellValue(record.getR41_net_amt_bwp().doubleValue());
						R41Cell6.setCellStyle(numberStyle);
					} else {
						R41Cell6.setCellValue("");
						R41Cell6.setCellStyle(textStyle);
					}
					// R41 Col J
					Cell R41Cell7 = row.createCell(9);
					if (record.getR41_bal_sub() != null) {
						R41Cell7.setCellValue(record.getR41_bal_sub().doubleValue());
						R41Cell7.setCellStyle(numberStyle);
					} else {
						R41Cell7.setCellValue("");
						R41Cell7.setCellStyle(textStyle);
					}
					// R41 Col K
					Cell R41Cell8 = row.createCell(10);
					if (record.getR41_bal_sub_bwp() != null) {
						R41Cell8.setCellValue(record.getR41_bal_sub_bwp().doubleValue());
						R41Cell8.setCellStyle(numberStyle);
					} else {
						R41Cell8.setCellValue("");
						R41Cell8.setCellStyle(textStyle);
					}
					// R41 Col L
					Cell R41Cell9 = row.createCell(11);
					if (record.getR41_bal_sub_diaries() != null) {
						R41Cell9.setCellValue(record.getR41_bal_sub_diaries().doubleValue());
						R41Cell9.setCellStyle(numberStyle);
					} else {
						R41Cell9.setCellValue("");
						R41Cell9.setCellStyle(textStyle);
					}
					// R41 Col M
					Cell R41Cell10 = row.createCell(12);
					if (record.getR41_bal_sub_diaries_bwp() != null) {
						R41Cell10.setCellValue(record.getR41_bal_sub_diaries_bwp().doubleValue());
						R41Cell10.setCellStyle(numberStyle);
					} else {
						R41Cell10.setCellValue("");
						R41Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(41);
					Cell R42Cell1 = row.createCell(3);
					if (record.getR42_fig_bal_sheet() != null) {
						R42Cell1.setCellValue(record.getR42_fig_bal_sheet().doubleValue());
						R42Cell1.setCellStyle(numberStyle);
					} else {
						R42Cell1.setCellValue("");
						R42Cell1.setCellStyle(textStyle);
					}

					// R42 Col E
					Cell R42Cell2 = row.createCell(4);
					if (record.getR42_fig_bal_sheet_bwp() != null) {
						R42Cell2.setCellValue(record.getR42_fig_bal_sheet_bwp().doubleValue());
						R42Cell2.setCellStyle(numberStyle);
					} else {
						R42Cell2.setCellValue("");
						R42Cell2.setCellStyle(textStyle);
					}

					// R42 Col F
					Cell R42Cell3 = row.createCell(5);
					if (record.getR42_amt_statement_adj() != null) {
						R42Cell3.setCellValue(record.getR42_amt_statement_adj().doubleValue());
						R42Cell3.setCellStyle(numberStyle);
					} else {
						R42Cell3.setCellValue("");
						R42Cell3.setCellStyle(textStyle);
					}
					// R42 Col G
					Cell R42Cell4 = row.createCell(6);
					if (record.getR42_amt_statement_adj_bwp() != null) {
						R42Cell4.setCellValue(record.getR42_amt_statement_adj_bwp().doubleValue());
						R42Cell4.setCellStyle(numberStyle);
					} else {
						R42Cell4.setCellValue("");
						R42Cell4.setCellStyle(textStyle);
					}
					// // R42 Col H
					// Cell R42Cell5 = row.createCell(7);
					// if (record.getR42_net_amt() != null) {
					// R42Cell5.setCellValue(record.getR42_net_amt().doubleValue());
					// R42Cell5.setCellStyle(numberStyle);
					// } else {
					// R42Cell5.setCellValue("");
					// R42Cell5.setCellStyle(textStyle);
					// }
					// R42 Col I
					Cell R42Cell6 = row.createCell(8);
					if (record.getR42_net_amt_bwp() != null) {
						R42Cell6.setCellValue(record.getR42_net_amt_bwp().doubleValue());
						R42Cell6.setCellStyle(numberStyle);
					} else {
						R42Cell6.setCellValue("");
						R42Cell6.setCellStyle(textStyle);
					}
					// R42 Col J
					Cell R42Cell7 = row.createCell(9);
					if (record.getR42_bal_sub() != null) {
						R42Cell7.setCellValue(record.getR42_bal_sub().doubleValue());
						R42Cell7.setCellStyle(numberStyle);
					} else {
						R42Cell7.setCellValue("");
						R42Cell7.setCellStyle(textStyle);
					}
					// R42 Col K
					Cell R42Cell8 = row.createCell(10);
					if (record.getR42_bal_sub_bwp() != null) {
						R42Cell8.setCellValue(record.getR42_bal_sub_bwp().doubleValue());
						R42Cell8.setCellStyle(numberStyle);
					} else {
						R42Cell8.setCellValue("");
						R42Cell8.setCellStyle(textStyle);
					}
					// R42 Col L
					Cell R42Cell9 = row.createCell(11);
					if (record.getR42_bal_sub_diaries() != null) {
						R42Cell9.setCellValue(record.getR42_bal_sub_diaries().doubleValue());
						R42Cell9.setCellStyle(numberStyle);
					} else {
						R42Cell9.setCellValue("");
						R42Cell9.setCellStyle(textStyle);
					}
					// R42 Col M
					Cell R42Cell10 = row.createCell(12);
					if (record.getR42_bal_sub_diaries_bwp() != null) {
						R42Cell10.setCellValue(record.getR42_bal_sub_diaries_bwp().doubleValue());
						R42Cell10.setCellStyle(numberStyle);
					} else {
						R42Cell10.setCellValue("");
						R42Cell10.setCellStyle(textStyle);
					}
					// row = sheet.getRow(42);
					// Cell R43Cell1 = row.createCell(3);
					// if (record.getR43_fig_bal_sheet() != null) {
					// R43Cell1.setCellValue(record.getR43_fig_bal_sheet().doubleValue());
					// R43Cell1.setCellStyle(numberStyle);
					// } else {
					// R43Cell1.setCellValue("");
					// R43Cell1.setCellStyle(textStyle);
					// }

					// // R43 Col E
					// Cell R43Cell2 = row.createCell(4);
					// if (record.getR43_fig_bal_sheet_bwp() != null) {
					// R43Cell2.setCellValue(record.getR43_fig_bal_sheet_bwp().doubleValue());
					// R43Cell2.setCellStyle(numberStyle);
					// } else {
					// R43Cell2.setCellValue("");
					// R43Cell2.setCellStyle(textStyle);
					// }

					// // R43 Col F
					// Cell R43Cell3 = row.createCell(5);
					// if (record.getR43_amt_statement_adj() != null) {
					// R43Cell3.setCellValue(record.getR43_amt_statement_adj().doubleValue());
					// R43Cell3.setCellStyle(numberStyle);
					// } else {
					// R43Cell3.setCellValue("");
					// R43Cell3.setCellStyle(textStyle);
					// }
					// // R43 Col G
					// Cell R43Cell4 = row.createCell(6);
					// if (record.getR43_amt_statement_adj_bwp() != null) {
					// R43Cell4.setCellValue(record.getR43_amt_statement_adj_bwp().doubleValue());
					// R43Cell4.setCellStyle(numberStyle);
					// } else {
					// R43Cell4.setCellValue("");
					// R43Cell4.setCellStyle(textStyle);
					// }
					// // R43 Col H
					// Cell R43Cell5 = row.createCell(7);
					// if (record.getR43_net_amt() != null) {
					// R43Cell5.setCellValue(record.getR43_net_amt().doubleValue());
					// R43Cell5.setCellStyle(numberStyle);
					// } else {
					// R43Cell5.setCellValue("");
					// R43Cell5.setCellStyle(textStyle);
					// }
					// // R43 Col I
					// Cell R43Cell6 = row.createCell(8);
					// if (record.getR43_net_amt_bwp() != null) {
					// R43Cell6.setCellValue(record.getR43_net_amt_bwp().doubleValue());
					// R43Cell6.setCellStyle(numberStyle);
					// } else {
					// R43Cell6.setCellValue("");
					// R43Cell6.setCellStyle(textStyle);
					// }
					// // R43 Col J
					// Cell R43Cell7 = row.createCell(9);
					// if (record.getR43_bal_sub() != null) {
					// R43Cell7.setCellValue(record.getR43_bal_sub().doubleValue());
					// R43Cell7.setCellStyle(numberStyle);
					// } else {
					// R43Cell7.setCellValue("");
					// R43Cell7.setCellStyle(textStyle);
					// }
					// // R43 Col K
					// Cell R43Cell8 = row.createCell(10);
					// if (record.getR43_bal_sub_bwp() != null) {
					// R43Cell8.setCellValue(record.getR43_bal_sub_bwp().doubleValue());
					// R43Cell8.setCellStyle(numberStyle);
					// } else {
					// R43Cell8.setCellValue("");
					// R43Cell8.setCellStyle(textStyle);
					// }
					// // R43 Col L
					// Cell R43Cell9 = row.createCell(11);
					// if (record.getR43_bal_sub_diaries() != null) {
					// R43Cell9.setCellValue(record.getR43_bal_sub_diaries().doubleValue());
					// R43Cell9.setCellStyle(numberStyle);
					// } else {
					// R43Cell9.setCellValue("");
					// R43Cell9.setCellStyle(textStyle);
					// }
					// // R43 Col M
					// Cell R43Cell10 = row.createCell(12);
					// if (record.getR43_bal_sub_diaries_bwp() != null) {
					// R43Cell10.setCellValue(record.getR43_bal_sub_diaries_bwp().doubleValue());
					// R43Cell10.setCellStyle(numberStyle);
					// } else {
					// R43Cell10.setCellValue("");
					// R43Cell10.setCellStyle(textStyle);
					// }

					row = sheet.getRow(47);
					Cell R48Cell1 = row.createCell(3);
					if (record.getR48_fig_bal_sheet() != null) {
						R48Cell1.setCellValue(record.getR48_fig_bal_sheet().doubleValue());
						R48Cell1.setCellStyle(numberStyle);
					} else {
						R48Cell1.setCellValue("");
						R48Cell1.setCellStyle(textStyle);
					}

					// R48 Col E
					Cell R48Cell2 = row.createCell(4);
					if (record.getR48_fig_bal_sheet_bwp() != null) {
						R48Cell2.setCellValue(record.getR48_fig_bal_sheet_bwp().doubleValue());
						R48Cell2.setCellStyle(numberStyle);
					} else {
						R48Cell2.setCellValue("");
						R48Cell2.setCellStyle(textStyle);
					}

					// R48 Col F
					Cell R48Cell3 = row.createCell(5);
					if (record.getR48_amt_statement_adj() != null) {
						R48Cell3.setCellValue(record.getR48_amt_statement_adj().doubleValue());
						R48Cell3.setCellStyle(numberStyle);
					} else {
						R48Cell3.setCellValue("");
						R48Cell3.setCellStyle(textStyle);
					}
					// R48 Col G
					Cell R48Cell4 = row.createCell(6);
					if (record.getR48_amt_statement_adj_bwp() != null) {
						R48Cell4.setCellValue(record.getR48_amt_statement_adj_bwp().doubleValue());
						R48Cell4.setCellStyle(numberStyle);
					} else {
						R48Cell4.setCellValue("");
						R48Cell4.setCellStyle(textStyle);
					}
					// // R48 Col H
					// Cell R48Cell5 = row.createCell(7);
					// if (record.getR48_net_amt() != null) {
					// R48Cell5.setCellValue(record.getR48_net_amt().doubleValue());
					// R48Cell5.setCellStyle(numberStyle);
					// } else {
					// R48Cell5.setCellValue("");
					// R48Cell5.setCellStyle(textStyle);
					// }
					// R48 Col I
					Cell R48Cell6 = row.createCell(8);
					if (record.getR48_net_amt_bwp() != null) {
						R48Cell6.setCellValue(record.getR48_net_amt_bwp().doubleValue());
						R48Cell6.setCellStyle(numberStyle);
					} else {
						R48Cell6.setCellValue("");
						R48Cell6.setCellStyle(textStyle);
					}
					// R48 Col J
					Cell R48Cell7 = row.createCell(9);
					if (record.getR48_bal_sub() != null) {
						R48Cell7.setCellValue(record.getR48_bal_sub().doubleValue());
						R48Cell7.setCellStyle(numberStyle);
					} else {
						R48Cell7.setCellValue("");
						R48Cell7.setCellStyle(textStyle);
					}
					// R48 Col K
					Cell R48Cell8 = row.createCell(10);
					if (record.getR48_bal_sub_bwp() != null) {
						R48Cell8.setCellValue(record.getR48_bal_sub_bwp().doubleValue());
						R48Cell8.setCellStyle(numberStyle);
					} else {
						R48Cell8.setCellValue("");
						R48Cell8.setCellStyle(textStyle);
					}
					// R48 Col L
					Cell R48Cell9 = row.createCell(11);
					if (record.getR48_bal_sub_diaries() != null) {
						R48Cell9.setCellValue(record.getR48_bal_sub_diaries().doubleValue());
						R48Cell9.setCellStyle(numberStyle);
					} else {
						R48Cell9.setCellValue("");
						R48Cell9.setCellStyle(textStyle);
					}
					// R48 Col M
					Cell R48Cell10 = row.createCell(12);
					if (record.getR48_bal_sub_diaries_bwp() != null) {
						R48Cell10.setCellValue(record.getR48_bal_sub_diaries_bwp().doubleValue());
						R48Cell10.setCellStyle(numberStyle);
					} else {
						R48Cell10.setCellValue("");
						R48Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(48);
					Cell R49Cell1 = row.createCell(3);
					if (record.getR49_fig_bal_sheet() != null) {
						R49Cell1.setCellValue(record.getR49_fig_bal_sheet().doubleValue());
						R49Cell1.setCellStyle(numberStyle);
					} else {
						R49Cell1.setCellValue("");
						R49Cell1.setCellStyle(textStyle);
					}

					// R49 Col E
					Cell R49Cell2 = row.createCell(4);
					if (record.getR49_fig_bal_sheet_bwp() != null) {
						R49Cell2.setCellValue(record.getR49_fig_bal_sheet_bwp().doubleValue());
						R49Cell2.setCellStyle(numberStyle);
					} else {
						R49Cell2.setCellValue("");
						R49Cell2.setCellStyle(textStyle);
					}

					// R49 Col F
					Cell R49Cell3 = row.createCell(5);
					if (record.getR49_amt_statement_adj() != null) {
						R49Cell3.setCellValue(record.getR49_amt_statement_adj().doubleValue());
						R49Cell3.setCellStyle(numberStyle);
					} else {
						R49Cell3.setCellValue("");
						R49Cell3.setCellStyle(textStyle);
					}
					// R49 Col G
					Cell R49Cell4 = row.createCell(6);
					if (record.getR49_amt_statement_adj_bwp() != null) {
						R49Cell4.setCellValue(record.getR49_amt_statement_adj_bwp().doubleValue());
						R49Cell4.setCellStyle(numberStyle);
					} else {
						R49Cell4.setCellValue("");
						R49Cell4.setCellStyle(textStyle);
					}
					// // R49 Col H
					// Cell R49Cell5 = row.createCell(7);
					// if (record.getR49_net_amt() != null) {
					// R49Cell5.setCellValue(record.getR49_net_amt().doubleValue());
					// R49Cell5.setCellStyle(numberStyle);
					// } else {
					// R49Cell5.setCellValue("");
					// R49Cell5.setCellStyle(textStyle);
					// }
					// R49 Col I
					Cell R49Cell6 = row.createCell(8);
					if (record.getR49_net_amt_bwp() != null) {
						R49Cell6.setCellValue(record.getR49_net_amt_bwp().doubleValue());
						R49Cell6.setCellStyle(numberStyle);
					} else {
						R49Cell6.setCellValue("");
						R49Cell6.setCellStyle(textStyle);
					}
					// R49 Col J
					Cell R49Cell7 = row.createCell(9);
					if (record.getR49_bal_sub() != null) {
						R49Cell7.setCellValue(record.getR49_bal_sub().doubleValue());
						R49Cell7.setCellStyle(numberStyle);
					} else {
						R49Cell7.setCellValue("");
						R49Cell7.setCellStyle(textStyle);
					}
					// R49 Col K
					Cell R49Cell8 = row.createCell(10);
					if (record.getR49_bal_sub_bwp() != null) {
						R49Cell8.setCellValue(record.getR49_bal_sub_bwp().doubleValue());
						R49Cell8.setCellStyle(numberStyle);
					} else {
						R49Cell8.setCellValue("");
						R49Cell8.setCellStyle(textStyle);
					}
					// R49 Col L
					Cell R49Cell9 = row.createCell(11);
					if (record.getR49_bal_sub_diaries() != null) {
						R49Cell9.setCellValue(record.getR49_bal_sub_diaries().doubleValue());
						R49Cell9.setCellStyle(numberStyle);
					} else {
						R49Cell9.setCellValue("");
						R49Cell9.setCellStyle(textStyle);
					}
					// R49 Col M
					Cell R49Cell10 = row.createCell(12);
					if (record.getR49_bal_sub_diaries_bwp() != null) {
						R49Cell10.setCellValue(record.getR49_bal_sub_diaries_bwp().doubleValue());
						R49Cell10.setCellStyle(numberStyle);
					} else {
						R49Cell10.setCellValue("");
						R49Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(49);
					Cell R50Cell1 = row.createCell(3);
					if (record.getR50_fig_bal_sheet() != null) {
						R50Cell1.setCellValue(record.getR50_fig_bal_sheet().doubleValue());
						R50Cell1.setCellStyle(numberStyle);
					} else {
						R50Cell1.setCellValue("");
						R50Cell1.setCellStyle(textStyle);
					}

					// R50 Col E
					Cell R50Cell2 = row.createCell(4);
					if (record.getR50_fig_bal_sheet_bwp() != null) {
						R50Cell2.setCellValue(record.getR50_fig_bal_sheet_bwp().doubleValue());
						R50Cell2.setCellStyle(numberStyle);
					} else {
						R50Cell2.setCellValue("");
						R50Cell2.setCellStyle(textStyle);
					}

					// R50 Col F
					Cell R50Cell3 = row.createCell(5);
					if (record.getR50_amt_statement_adj() != null) {
						R50Cell3.setCellValue(record.getR50_amt_statement_adj().doubleValue());
						R50Cell3.setCellStyle(numberStyle);
					} else {
						R50Cell3.setCellValue("");
						R50Cell3.setCellStyle(textStyle);
					}
					// R50 Col G
					Cell R50Cell4 = row.createCell(6);
					if (record.getR50_amt_statement_adj_bwp() != null) {
						R50Cell4.setCellValue(record.getR50_amt_statement_adj_bwp().doubleValue());
						R50Cell4.setCellStyle(numberStyle);
					} else {
						R50Cell4.setCellValue("");
						R50Cell4.setCellStyle(textStyle);
					}
					// // R50 Col H
					// Cell R50Cell5 = row.createCell(7);
					// if (record.getR50_net_amt() != null) {
					// R50Cell5.setCellValue(record.getR50_net_amt().doubleValue());
					// R50Cell5.setCellStyle(numberStyle);
					// } else {
					// R50Cell5.setCellValue("");
					// R50Cell5.setCellStyle(textStyle);
					// }
					// R50 Col I
					Cell R50Cell6 = row.createCell(8);
					if (record.getR50_net_amt_bwp() != null) {
						R50Cell6.setCellValue(record.getR50_net_amt_bwp().doubleValue());
						R50Cell6.setCellStyle(numberStyle);
					} else {
						R50Cell6.setCellValue("");
						R50Cell6.setCellStyle(textStyle);
					}
					// R50 Col J
					Cell R50Cell7 = row.createCell(9);
					if (record.getR50_bal_sub() != null) {
						R50Cell7.setCellValue(record.getR50_bal_sub().doubleValue());
						R50Cell7.setCellStyle(numberStyle);
					} else {
						R50Cell7.setCellValue("");
						R50Cell7.setCellStyle(textStyle);
					}
					// R50 Col K
					Cell R50Cell8 = row.createCell(10);
					if (record.getR50_bal_sub_bwp() != null) {
						R50Cell8.setCellValue(record.getR50_bal_sub_bwp().doubleValue());
						R50Cell8.setCellStyle(numberStyle);
					} else {
						R50Cell8.setCellValue("");
						R50Cell8.setCellStyle(textStyle);
					}
					// R50 Col L
					Cell R50Cell9 = row.createCell(11);
					if (record.getR50_bal_sub_diaries() != null) {
						R50Cell9.setCellValue(record.getR50_bal_sub_diaries().doubleValue());
						R50Cell9.setCellStyle(numberStyle);
					} else {
						R50Cell9.setCellValue("");
						R50Cell9.setCellStyle(textStyle);
					}
					// R50 Col M
					Cell R50Cell10 = row.createCell(12);
					if (record.getR50_bal_sub_diaries_bwp() != null) {
						R50Cell10.setCellValue(record.getR50_bal_sub_diaries_bwp().doubleValue());
						R50Cell10.setCellStyle(numberStyle);
					} else {
						R50Cell10.setCellValue("");
						R50Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(50);
					Cell R51Cell1 = row.createCell(3);
					if (record.getR51_fig_bal_sheet() != null) {
						R51Cell1.setCellValue(record.getR51_fig_bal_sheet().doubleValue());
						R51Cell1.setCellStyle(numberStyle);
					} else {
						R51Cell1.setCellValue("");
						R51Cell1.setCellStyle(textStyle);
					}

					// R51 Col E
					Cell R51Cell2 = row.createCell(4);
					if (record.getR51_fig_bal_sheet_bwp() != null) {
						R51Cell2.setCellValue(record.getR51_fig_bal_sheet_bwp().doubleValue());
						R51Cell2.setCellStyle(numberStyle);
					} else {
						R51Cell2.setCellValue("");
						R51Cell2.setCellStyle(textStyle);
					}

					// R51 Col F
					Cell R51Cell3 = row.createCell(5);
					if (record.getR51_amt_statement_adj() != null) {
						R51Cell3.setCellValue(record.getR51_amt_statement_adj().doubleValue());
						R51Cell3.setCellStyle(numberStyle);
					} else {
						R51Cell3.setCellValue("");
						R51Cell3.setCellStyle(textStyle);
					}
					// R51 Col G
					Cell R51Cell4 = row.createCell(6);
					if (record.getR51_amt_statement_adj_bwp() != null) {
						R51Cell4.setCellValue(record.getR51_amt_statement_adj_bwp().doubleValue());
						R51Cell4.setCellStyle(numberStyle);
					} else {
						R51Cell4.setCellValue("");
						R51Cell4.setCellStyle(textStyle);
					}
					// // R51 Col H
					// Cell R51Cell5 = row.createCell(7);
					// if (record.getR51_net_amt() != null) {
					// R51Cell5.setCellValue(record.getR51_net_amt().doubleValue());
					// R51Cell5.setCellStyle(numberStyle);
					// } else {
					// R51Cell5.setCellValue("");
					// R51Cell5.setCellStyle(textStyle);
					// }
					// R51 Col I
					Cell R51Cell6 = row.createCell(8);
					if (record.getR51_net_amt_bwp() != null) {
						R51Cell6.setCellValue(record.getR51_net_amt_bwp().doubleValue());
						R51Cell6.setCellStyle(numberStyle);
					} else {
						R51Cell6.setCellValue("");
						R51Cell6.setCellStyle(textStyle);
					}
					// R51 Col J
					Cell R51Cell7 = row.createCell(9);
					if (record.getR51_bal_sub() != null) {
						R51Cell7.setCellValue(record.getR51_bal_sub().doubleValue());
						R51Cell7.setCellStyle(numberStyle);
					} else {
						R51Cell7.setCellValue("");
						R51Cell7.setCellStyle(textStyle);
					}
					// R51 Col K
					Cell R51Cell8 = row.createCell(10);
					if (record.getR51_bal_sub_bwp() != null) {
						R51Cell8.setCellValue(record.getR51_bal_sub_bwp().doubleValue());
						R51Cell8.setCellStyle(numberStyle);
					} else {
						R51Cell8.setCellValue("");
						R51Cell8.setCellStyle(textStyle);
					}
					// R51 Col L
					Cell R51Cell9 = row.createCell(11);
					if (record.getR51_bal_sub_diaries() != null) {
						R51Cell9.setCellValue(record.getR51_bal_sub_diaries().doubleValue());
						R51Cell9.setCellStyle(numberStyle);
					} else {
						R51Cell9.setCellValue("");
						R51Cell9.setCellStyle(textStyle);
					}
					// R51 Col M
					Cell R51Cell10 = row.createCell(12);
					if (record.getR51_bal_sub_diaries_bwp() != null) {
						R51Cell10.setCellValue(record.getR51_bal_sub_diaries_bwp().doubleValue());
						R51Cell10.setCellStyle(numberStyle);
					} else {
						R51Cell10.setCellValue("");
						R51Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(51);
					Cell R52Cell1 = row.createCell(3);
					if (record.getR52_fig_bal_sheet() != null) {
						R52Cell1.setCellValue(record.getR52_fig_bal_sheet().doubleValue());
						R52Cell1.setCellStyle(numberStyle);
					} else {
						R52Cell1.setCellValue("");
						R52Cell1.setCellStyle(textStyle);
					}

					// R52 Col E
					Cell R52Cell2 = row.createCell(4);
					if (record.getR52_fig_bal_sheet_bwp() != null) {
						R52Cell2.setCellValue(record.getR52_fig_bal_sheet_bwp().doubleValue());
						R52Cell2.setCellStyle(numberStyle);
					} else {
						R52Cell2.setCellValue("");
						R52Cell2.setCellStyle(textStyle);
					}

					// R52 Col F
					Cell R52Cell3 = row.createCell(5);
					if (record.getR52_amt_statement_adj() != null) {
						R52Cell3.setCellValue(record.getR52_amt_statement_adj().doubleValue());
						R52Cell3.setCellStyle(numberStyle);
					} else {
						R52Cell3.setCellValue("");
						R52Cell3.setCellStyle(textStyle);
					}
					// R52 Col G
					Cell R52Cell4 = row.createCell(6);
					if (record.getR52_amt_statement_adj_bwp() != null) {
						R52Cell4.setCellValue(record.getR52_amt_statement_adj_bwp().doubleValue());
						R52Cell4.setCellStyle(numberStyle);
					} else {
						R52Cell4.setCellValue("");
						R52Cell4.setCellStyle(textStyle);
					}
					// // R52 Col H
					// Cell R52Cell5 = row.createCell(7);
					// if (record.getR52_net_amt() != null) {
					// R52Cell5.setCellValue(record.getR52_net_amt().doubleValue());
					// R52Cell5.setCellStyle(numberStyle);
					// } else {
					// R52Cell5.setCellValue("");
					// R52Cell5.setCellStyle(textStyle);
					// }
					// R52 Col I
					Cell R52Cell6 = row.createCell(8);
					if (record.getR52_net_amt_bwp() != null) {
						R52Cell6.setCellValue(record.getR52_net_amt_bwp().doubleValue());
						R52Cell6.setCellStyle(numberStyle);
					} else {
						R52Cell6.setCellValue("");
						R52Cell6.setCellStyle(textStyle);
					}
					// R52 Col J
					Cell R52Cell7 = row.createCell(9);
					if (record.getR52_bal_sub() != null) {
						R52Cell7.setCellValue(record.getR52_bal_sub().doubleValue());
						R52Cell7.setCellStyle(numberStyle);
					} else {
						R52Cell7.setCellValue("");
						R52Cell7.setCellStyle(textStyle);
					}
					// R52 Col K
					Cell R52Cell8 = row.createCell(10);
					if (record.getR52_bal_sub_bwp() != null) {
						R52Cell8.setCellValue(record.getR52_bal_sub_bwp().doubleValue());
						R52Cell8.setCellStyle(numberStyle);
					} else {
						R52Cell8.setCellValue("");
						R52Cell8.setCellStyle(textStyle);
					}
					// R52 Col L
					Cell R52Cell9 = row.createCell(11);
					if (record.getR52_bal_sub_diaries() != null) {
						R52Cell9.setCellValue(record.getR52_bal_sub_diaries().doubleValue());
						R52Cell9.setCellStyle(numberStyle);
					} else {
						R52Cell9.setCellValue("");
						R52Cell9.setCellStyle(textStyle);
					}
					// R52 Col M
					Cell R52Cell10 = row.createCell(12);
					if (record.getR52_bal_sub_diaries_bwp() != null) {
						R52Cell10.setCellValue(record.getR52_bal_sub_diaries_bwp().doubleValue());
						R52Cell10.setCellStyle(numberStyle);
					} else {
						R52Cell10.setCellValue("");
						R52Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(52);
					Cell R53Cell1 = row.createCell(3);
					if (record.getR53_fig_bal_sheet() != null) {
						R53Cell1.setCellValue(record.getR53_fig_bal_sheet().doubleValue());
						R53Cell1.setCellStyle(numberStyle);
					} else {
						R53Cell1.setCellValue("");
						R53Cell1.setCellStyle(textStyle);
					}

					// R53 Col E
					Cell R53Cell2 = row.createCell(4);
					if (record.getR53_fig_bal_sheet_bwp() != null) {
						R53Cell2.setCellValue(record.getR53_fig_bal_sheet_bwp().doubleValue());
						R53Cell2.setCellStyle(numberStyle);
					} else {
						R53Cell2.setCellValue("");
						R53Cell2.setCellStyle(textStyle);
					}

					// R53 Col F
					Cell R53Cell3 = row.createCell(5);
					if (record.getR53_amt_statement_adj() != null) {
						R53Cell3.setCellValue(record.getR53_amt_statement_adj().doubleValue());
						R53Cell3.setCellStyle(numberStyle);
					} else {
						R53Cell3.setCellValue("");
						R53Cell3.setCellStyle(textStyle);
					}
					// R53 Col G
					Cell R53Cell4 = row.createCell(6);
					if (record.getR53_amt_statement_adj_bwp() != null) {
						R53Cell4.setCellValue(record.getR53_amt_statement_adj_bwp().doubleValue());
						R53Cell4.setCellStyle(numberStyle);
					} else {
						R53Cell4.setCellValue("");
						R53Cell4.setCellStyle(textStyle);
					}
					// // R53 Col H
					// Cell R53Cell5 = row.createCell(7);
					// if (record.getR53_net_amt() != null) {
					// R53Cell5.setCellValue(record.getR53_net_amt().doubleValue());
					// R53Cell5.setCellStyle(numberStyle);
					// } else {
					// R53Cell5.setCellValue("");
					// R53Cell5.setCellStyle(textStyle);
					// }
					// R53 Col I
					Cell R53Cell6 = row.createCell(8);
					if (record.getR53_net_amt_bwp() != null) {
						R53Cell6.setCellValue(record.getR53_net_amt_bwp().doubleValue());
						R53Cell6.setCellStyle(numberStyle);
					} else {
						R53Cell6.setCellValue("");
						R53Cell6.setCellStyle(textStyle);
					}
					// R53 Col J
					Cell R53Cell7 = row.createCell(9);
					if (record.getR53_bal_sub() != null) {
						R53Cell7.setCellValue(record.getR53_bal_sub().doubleValue());
						R53Cell7.setCellStyle(numberStyle);
					} else {
						R53Cell7.setCellValue("");
						R53Cell7.setCellStyle(textStyle);
					}
					// R53 Col K
					Cell R53Cell8 = row.createCell(10);
					if (record.getR53_bal_sub_bwp() != null) {
						R53Cell8.setCellValue(record.getR53_bal_sub_bwp().doubleValue());
						R53Cell8.setCellStyle(numberStyle);
					} else {
						R53Cell8.setCellValue("");
						R53Cell8.setCellStyle(textStyle);
					}
					// R53 Col L
					Cell R53Cell9 = row.createCell(11);
					if (record.getR53_bal_sub_diaries() != null) {
						R53Cell9.setCellValue(record.getR53_bal_sub_diaries().doubleValue());
						R53Cell9.setCellStyle(numberStyle);
					} else {
						R53Cell9.setCellValue("");
						R53Cell9.setCellStyle(textStyle);
					}
					// R53 Col M
					Cell R53Cell10 = row.createCell(12);
					if (record.getR53_bal_sub_diaries_bwp() != null) {
						R53Cell10.setCellValue(record.getR53_bal_sub_diaries_bwp().doubleValue());
						R53Cell10.setCellStyle(numberStyle);
					} else {
						R53Cell10.setCellValue("");
						R53Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(53);
					Cell R54Cell1 = row.createCell(3);
					if (record.getR54_fig_bal_sheet() != null) {
						R54Cell1.setCellValue(record.getR54_fig_bal_sheet().doubleValue());
						R54Cell1.setCellStyle(numberStyle);
					} else {
						R54Cell1.setCellValue("");
						R54Cell1.setCellStyle(textStyle);
					}

					// R54 Col E
					Cell R54Cell2 = row.createCell(4);
					if (record.getR54_fig_bal_sheet_bwp() != null) {
						R54Cell2.setCellValue(record.getR54_fig_bal_sheet_bwp().doubleValue());
						R54Cell2.setCellStyle(numberStyle);
					} else {
						R54Cell2.setCellValue("");
						R54Cell2.setCellStyle(textStyle);
					}

					// R54 Col F
					Cell R54Cell3 = row.createCell(5);
					if (record.getR54_amt_statement_adj() != null) {
						R54Cell3.setCellValue(record.getR54_amt_statement_adj().doubleValue());
						R54Cell3.setCellStyle(numberStyle);
					} else {
						R54Cell3.setCellValue("");
						R54Cell3.setCellStyle(textStyle);
					}
					// R54 Col G
					Cell R54Cell4 = row.createCell(6);
					if (record.getR54_amt_statement_adj_bwp() != null) {
						R54Cell4.setCellValue(record.getR54_amt_statement_adj_bwp().doubleValue());
						R54Cell4.setCellStyle(numberStyle);
					} else {
						R54Cell4.setCellValue("");
						R54Cell4.setCellStyle(textStyle);
					}
					// // R54 Col H
					// Cell R54Cell5 = row.createCell(7);
					// if (record.getR54_net_amt() != null) {
					// R54Cell5.setCellValue(record.getR54_net_amt().doubleValue());
					// R54Cell5.setCellStyle(numberStyle);
					// } else {
					// R54Cell5.setCellValue("");
					// R54Cell5.setCellStyle(textStyle);
					// }
					// R54 Col I
					Cell R54Cell6 = row.createCell(8);
					if (record.getR54_net_amt_bwp() != null) {
						R54Cell6.setCellValue(record.getR54_net_amt_bwp().doubleValue());
						R54Cell6.setCellStyle(numberStyle);
					} else {
						R54Cell6.setCellValue("");
						R54Cell6.setCellStyle(textStyle);
					}
					// R54 Col J
					Cell R54Cell7 = row.createCell(9);
					if (record.getR54_bal_sub() != null) {
						R54Cell7.setCellValue(record.getR54_bal_sub().doubleValue());
						R54Cell7.setCellStyle(numberStyle);
					} else {
						R54Cell7.setCellValue("");
						R54Cell7.setCellStyle(textStyle);
					}
					// R54 Col K
					Cell R54Cell8 = row.createCell(10);
					if (record.getR54_bal_sub_bwp() != null) {
						R54Cell8.setCellValue(record.getR54_bal_sub_bwp().doubleValue());
						R54Cell8.setCellStyle(numberStyle);
					} else {
						R54Cell8.setCellValue("");
						R54Cell8.setCellStyle(textStyle);
					}
					// R54 Col L
					Cell R54Cell9 = row.createCell(11);
					if (record.getR54_bal_sub_diaries() != null) {
						R54Cell9.setCellValue(record.getR54_bal_sub_diaries().doubleValue());
						R54Cell9.setCellStyle(numberStyle);
					} else {
						R54Cell9.setCellValue("");
						R54Cell9.setCellStyle(textStyle);
					}
					// R54 Col M
					Cell R54Cell10 = row.createCell(12);
					if (record.getR54_bal_sub_diaries_bwp() != null) {
						R54Cell10.setCellValue(record.getR54_bal_sub_diaries_bwp().doubleValue());
						R54Cell10.setCellStyle(numberStyle);
					} else {
						R54Cell10.setCellValue("");
						R54Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(54);
					Cell R55Cell1 = row.createCell(3);
					if (record.getR55_fig_bal_sheet() != null) {
						R55Cell1.setCellValue(record.getR55_fig_bal_sheet().doubleValue());
						R55Cell1.setCellStyle(numberStyle);
					} else {
						R55Cell1.setCellValue("");
						R55Cell1.setCellStyle(textStyle);
					}

					// R55 Col E
					Cell R55Cell2 = row.createCell(4);
					if (record.getR55_fig_bal_sheet_bwp() != null) {
						R55Cell2.setCellValue(record.getR55_fig_bal_sheet_bwp().doubleValue());
						R55Cell2.setCellStyle(numberStyle);
					} else {
						R55Cell2.setCellValue("");
						R55Cell2.setCellStyle(textStyle);
					}

					// R55 Col F
					Cell R55Cell3 = row.createCell(5);
					if (record.getR55_amt_statement_adj() != null) {
						R55Cell3.setCellValue(record.getR55_amt_statement_adj().doubleValue());
						R55Cell3.setCellStyle(numberStyle);
					} else {
						R55Cell3.setCellValue("");
						R55Cell3.setCellStyle(textStyle);
					}
					// R55 Col G
					Cell R55Cell4 = row.createCell(6);
					if (record.getR55_amt_statement_adj_bwp() != null) {
						R55Cell4.setCellValue(record.getR55_amt_statement_adj_bwp().doubleValue());
						R55Cell4.setCellStyle(numberStyle);
					} else {
						R55Cell4.setCellValue("");
						R55Cell4.setCellStyle(textStyle);
					}
					// // R55 Col H
					// Cell R55Cell5 = row.createCell(7);
					// if (record.getR55_net_amt() != null) {
					// R55Cell5.setCellValue(record.getR55_net_amt().doubleValue());
					// R55Cell5.setCellStyle(numberStyle);
					// } else {
					// R55Cell5.setCellValue("");
					// R55Cell5.setCellStyle(textStyle);
					// }
					// R55 Col I
					Cell R55Cell6 = row.createCell(8);
					if (record.getR55_net_amt_bwp() != null) {
						R55Cell6.setCellValue(record.getR55_net_amt_bwp().doubleValue());
						R55Cell6.setCellStyle(numberStyle);
					} else {
						R55Cell6.setCellValue("");
						R55Cell6.setCellStyle(textStyle);
					}
					// R55 Col J
					Cell R55Cell7 = row.createCell(9);
					if (record.getR55_bal_sub() != null) {
						R55Cell7.setCellValue(record.getR55_bal_sub().doubleValue());
						R55Cell7.setCellStyle(numberStyle);
					} else {
						R55Cell7.setCellValue("");
						R55Cell7.setCellStyle(textStyle);
					}
					// R55 Col K
					Cell R55Cell8 = row.createCell(10);
					if (record.getR55_bal_sub_bwp() != null) {
						R55Cell8.setCellValue(record.getR55_bal_sub_bwp().doubleValue());
						R55Cell8.setCellStyle(numberStyle);
					} else {
						R55Cell8.setCellValue("");
						R55Cell8.setCellStyle(textStyle);
					}
					// R55 Col L
					Cell R55Cell9 = row.createCell(11);
					if (record.getR55_bal_sub_diaries() != null) {
						R55Cell9.setCellValue(record.getR55_bal_sub_diaries().doubleValue());
						R55Cell9.setCellStyle(numberStyle);
					} else {
						R55Cell9.setCellValue("");
						R55Cell9.setCellStyle(textStyle);
					}
					// R55 Col M
					Cell R55Cell10 = row.createCell(12);
					if (record.getR55_bal_sub_diaries_bwp() != null) {
						R55Cell10.setCellValue(record.getR55_bal_sub_diaries_bwp().doubleValue());
						R55Cell10.setCellStyle(numberStyle);
					} else {
						R55Cell10.setCellValue("");
						R55Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(55);
					Cell R56Cell1 = row.createCell(3);
					if (record.getR56_fig_bal_sheet() != null) {
						R56Cell1.setCellValue(record.getR56_fig_bal_sheet().doubleValue());
						R56Cell1.setCellStyle(numberStyle);
					} else {
						R56Cell1.setCellValue("");
						R56Cell1.setCellStyle(textStyle);
					}

					// R56 Col E
					Cell R56Cell2 = row.createCell(4);
					if (record.getR56_fig_bal_sheet_bwp() != null) {
						R56Cell2.setCellValue(record.getR56_fig_bal_sheet_bwp().doubleValue());
						R56Cell2.setCellStyle(numberStyle);
					} else {
						R56Cell2.setCellValue("");
						R56Cell2.setCellStyle(textStyle);
					}

					// R56 Col F
					Cell R56Cell3 = row.createCell(5);
					if (record.getR56_amt_statement_adj() != null) {
						R56Cell3.setCellValue(record.getR56_amt_statement_adj().doubleValue());
						R56Cell3.setCellStyle(numberStyle);
					} else {
						R56Cell3.setCellValue("");
						R56Cell3.setCellStyle(textStyle);
					}
					// R56 Col G
					Cell R56Cell4 = row.createCell(6);
					if (record.getR56_amt_statement_adj_bwp() != null) {
						R56Cell4.setCellValue(record.getR56_amt_statement_adj_bwp().doubleValue());
						R56Cell4.setCellStyle(numberStyle);
					} else {
						R56Cell4.setCellValue("");
						R56Cell4.setCellStyle(textStyle);
					}
					// // R56 Col H
					// Cell R56Cell5 = row.createCell(7);
					// if (record.getR56_net_amt() != null) {
					// R56Cell5.setCellValue(record.getR56_net_amt().doubleValue());
					// R56Cell5.setCellStyle(numberStyle);
					// } else {
					// R56Cell5.setCellValue("");
					// R56Cell5.setCellStyle(textStyle);
					// }
					// R56 Col I
					Cell R56Cell6 = row.createCell(8);
					if (record.getR56_net_amt_bwp() != null) {
						R56Cell6.setCellValue(record.getR56_net_amt_bwp().doubleValue());
						R56Cell6.setCellStyle(numberStyle);
					} else {
						R56Cell6.setCellValue("");
						R56Cell6.setCellStyle(textStyle);
					}
					// R56 Col J
					Cell R56Cell7 = row.createCell(9);
					if (record.getR56_bal_sub() != null) {
						R56Cell7.setCellValue(record.getR56_bal_sub().doubleValue());
						R56Cell7.setCellStyle(numberStyle);
					} else {
						R56Cell7.setCellValue("");
						R56Cell7.setCellStyle(textStyle);
					}
					// R56 Col K
					Cell R56Cell8 = row.createCell(10);
					if (record.getR56_bal_sub_bwp() != null) {
						R56Cell8.setCellValue(record.getR56_bal_sub_bwp().doubleValue());
						R56Cell8.setCellStyle(numberStyle);
					} else {
						R56Cell8.setCellValue("");
						R56Cell8.setCellStyle(textStyle);
					}
					// R56 Col L
					Cell R56Cell9 = row.createCell(11);
					if (record.getR56_bal_sub_diaries() != null) {
						R56Cell9.setCellValue(record.getR56_bal_sub_diaries().doubleValue());
						R56Cell9.setCellStyle(numberStyle);
					} else {
						R56Cell9.setCellValue("");
						R56Cell9.setCellStyle(textStyle);
					}
					// R56 Col M
					Cell R56Cell10 = row.createCell(12);
					if (record.getR56_bal_sub_diaries_bwp() != null) {
						R56Cell10.setCellValue(record.getR56_bal_sub_diaries_bwp().doubleValue());
						R56Cell10.setCellStyle(numberStyle);
					} else {
						R56Cell10.setCellValue("");
						R56Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(56);
					Cell R57Cell1 = row.createCell(3);
					if (record.getR57_fig_bal_sheet() != null) {
						R57Cell1.setCellValue(record.getR57_fig_bal_sheet().doubleValue());
						R57Cell1.setCellStyle(numberStyle);
					} else {
						R57Cell1.setCellValue("");
						R57Cell1.setCellStyle(textStyle);
					}

					// R57 Col E
					Cell R57Cell2 = row.createCell(4);
					if (record.getR57_fig_bal_sheet_bwp() != null) {
						R57Cell2.setCellValue(record.getR57_fig_bal_sheet_bwp().doubleValue());
						R57Cell2.setCellStyle(numberStyle);
					} else {
						R57Cell2.setCellValue("");
						R57Cell2.setCellStyle(textStyle);
					}

					// R57 Col F
					Cell R57Cell3 = row.createCell(5);
					if (record.getR57_amt_statement_adj() != null) {
						R57Cell3.setCellValue(record.getR57_amt_statement_adj().doubleValue());
						R57Cell3.setCellStyle(numberStyle);
					} else {
						R57Cell3.setCellValue("");
						R57Cell3.setCellStyle(textStyle);
					}
					// R57 Col G
					Cell R57Cell4 = row.createCell(6);
					if (record.getR57_amt_statement_adj_bwp() != null) {
						R57Cell4.setCellValue(record.getR57_amt_statement_adj_bwp().doubleValue());
						R57Cell4.setCellStyle(numberStyle);
					} else {
						R57Cell4.setCellValue("");
						R57Cell4.setCellStyle(textStyle);
					}
					// // R57 Col H
					// Cell R57Cell5 = row.createCell(7);
					// if (record.getR57_net_amt() != null) {
					// R57Cell5.setCellValue(record.getR57_net_amt().doubleValue());
					// R57Cell5.setCellStyle(numberStyle);
					// } else {
					// R57Cell5.setCellValue("");
					// R57Cell5.setCellStyle(textStyle);
					// }
					// R57 Col I
					Cell R57Cell6 = row.createCell(8);
					if (record.getR57_net_amt_bwp() != null) {
						R57Cell6.setCellValue(record.getR57_net_amt_bwp().doubleValue());
						R57Cell6.setCellStyle(numberStyle);
					} else {
						R57Cell6.setCellValue("");
						R57Cell6.setCellStyle(textStyle);
					}
					// R57 Col J
					Cell R57Cell7 = row.createCell(9);
					if (record.getR57_bal_sub() != null) {
						R57Cell7.setCellValue(record.getR57_bal_sub().doubleValue());
						R57Cell7.setCellStyle(numberStyle);
					} else {
						R57Cell7.setCellValue("");
						R57Cell7.setCellStyle(textStyle);
					}
					// R57 Col K
					Cell R57Cell8 = row.createCell(10);
					if (record.getR57_bal_sub_bwp() != null) {
						R57Cell8.setCellValue(record.getR57_bal_sub_bwp().doubleValue());
						R57Cell8.setCellStyle(numberStyle);
					} else {
						R57Cell8.setCellValue("");
						R57Cell8.setCellStyle(textStyle);
					}
					// R57 Col L
					Cell R57Cell9 = row.createCell(11);
					if (record.getR57_bal_sub_diaries() != null) {
						R57Cell9.setCellValue(record.getR57_bal_sub_diaries().doubleValue());
						R57Cell9.setCellStyle(numberStyle);
					} else {
						R57Cell9.setCellValue("");
						R57Cell9.setCellStyle(textStyle);
					}
					// R57 Col M
					Cell R57Cell10 = row.createCell(12);
					if (record.getR57_bal_sub_diaries_bwp() != null) {
						R57Cell10.setCellValue(record.getR57_bal_sub_diaries_bwp().doubleValue());
						R57Cell10.setCellStyle(numberStyle);
					} else {
						R57Cell10.setCellValue("");
						R57Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(57);
					Cell R58Cell1 = row.createCell(3);
					if (record.getR58_fig_bal_sheet() != null) {
						R58Cell1.setCellValue(record.getR58_fig_bal_sheet().doubleValue());
						R58Cell1.setCellStyle(numberStyle);
					} else {
						R58Cell1.setCellValue("");
						R58Cell1.setCellStyle(textStyle);
					}

					// R58 Col E
					Cell R58Cell2 = row.createCell(4);
					if (record.getR58_fig_bal_sheet_bwp() != null) {
						R58Cell2.setCellValue(record.getR58_fig_bal_sheet_bwp().doubleValue());
						R58Cell2.setCellStyle(numberStyle);
					} else {
						R58Cell2.setCellValue("");
						R58Cell2.setCellStyle(textStyle);
					}

					// R58 Col F
					Cell R58Cell3 = row.createCell(5);
					if (record.getR58_amt_statement_adj() != null) {
						R58Cell3.setCellValue(record.getR58_amt_statement_adj().doubleValue());
						R58Cell3.setCellStyle(numberStyle);
					} else {
						R58Cell3.setCellValue("");
						R58Cell3.setCellStyle(textStyle);
					}
					// R58 Col G
					Cell R58Cell4 = row.createCell(6);
					if (record.getR58_amt_statement_adj_bwp() != null) {
						R58Cell4.setCellValue(record.getR58_amt_statement_adj_bwp().doubleValue());
						R58Cell4.setCellStyle(numberStyle);
					} else {
						R58Cell4.setCellValue("");
						R58Cell4.setCellStyle(textStyle);
					}
					// // R58 Col H
					// Cell R58Cell5 = row.createCell(7);
					// if (record.getR58_net_amt() != null) {
					// R58Cell5.setCellValue(record.getR58_net_amt().doubleValue());
					// R58Cell5.setCellStyle(numberStyle);
					// } else {
					// R58Cell5.setCellValue("");
					// R58Cell5.setCellStyle(textStyle);
					// }
					// R58 Col I
					Cell R58Cell6 = row.createCell(8);
					if (record.getR58_net_amt_bwp() != null) {
						R58Cell6.setCellValue(record.getR58_net_amt_bwp().doubleValue());
						R58Cell6.setCellStyle(numberStyle);
					} else {
						R58Cell6.setCellValue("");
						R58Cell6.setCellStyle(textStyle);
					}
					// R58 Col J
					Cell R58Cell7 = row.createCell(9);
					if (record.getR58_bal_sub() != null) {
						R58Cell7.setCellValue(record.getR58_bal_sub().doubleValue());
						R58Cell7.setCellStyle(numberStyle);
					} else {
						R58Cell7.setCellValue("");
						R58Cell7.setCellStyle(textStyle);
					}
					// R58 Col K
					Cell R58Cell8 = row.createCell(10);
					if (record.getR58_bal_sub_bwp() != null) {
						R58Cell8.setCellValue(record.getR58_bal_sub_bwp().doubleValue());
						R58Cell8.setCellStyle(numberStyle);
					} else {
						R58Cell8.setCellValue("");
						R58Cell8.setCellStyle(textStyle);
					}
					// R58 Col L
					Cell R58Cell9 = row.createCell(11);
					if (record.getR58_bal_sub_diaries() != null) {
						R58Cell9.setCellValue(record.getR58_bal_sub_diaries().doubleValue());
						R58Cell9.setCellStyle(numberStyle);
					} else {
						R58Cell9.setCellValue("");
						R58Cell9.setCellStyle(textStyle);
					}
					// R58 Col M
					Cell R58Cell10 = row.createCell(12);
					if (record.getR58_bal_sub_diaries_bwp() != null) {
						R58Cell10.setCellValue(record.getR58_bal_sub_diaries_bwp().doubleValue());
						R58Cell10.setCellStyle(numberStyle);
					} else {
						R58Cell10.setCellValue("");
						R58Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(58);
					Cell R59Cell1 = row.createCell(3);
					if (record.getR59_fig_bal_sheet() != null) {
						R59Cell1.setCellValue(record.getR59_fig_bal_sheet().doubleValue());
						R59Cell1.setCellStyle(numberStyle);
					} else {
						R59Cell1.setCellValue("");
						R59Cell1.setCellStyle(textStyle);
					}

					// R59 Col E
					Cell R59Cell2 = row.createCell(4);
					if (record.getR59_fig_bal_sheet_bwp() != null) {
						R59Cell2.setCellValue(record.getR59_fig_bal_sheet_bwp().doubleValue());
						R59Cell2.setCellStyle(numberStyle);
					} else {
						R59Cell2.setCellValue("");
						R59Cell2.setCellStyle(textStyle);
					}

					// R59 Col F
					Cell R59Cell3 = row.createCell(5);
					if (record.getR59_amt_statement_adj() != null) {
						R59Cell3.setCellValue(record.getR59_amt_statement_adj().doubleValue());
						R59Cell3.setCellStyle(numberStyle);
					} else {
						R59Cell3.setCellValue("");
						R59Cell3.setCellStyle(textStyle);
					}
					// R59 Col G
					Cell R59Cell4 = row.createCell(6);
					if (record.getR59_amt_statement_adj_bwp() != null) {
						R59Cell4.setCellValue(record.getR59_amt_statement_adj_bwp().doubleValue());
						R59Cell4.setCellStyle(numberStyle);
					} else {
						R59Cell4.setCellValue("");
						R59Cell4.setCellStyle(textStyle);
					}
					// // R59 Col H
					// Cell R59Cell5 = row.createCell(7);
					// if (record.getR59_net_amt() != null) {
					// R59Cell5.setCellValue(record.getR59_net_amt().doubleValue());
					// R59Cell5.setCellStyle(numberStyle);
					// } else {
					// R59Cell5.setCellValue("");
					// R59Cell5.setCellStyle(textStyle);
					// }
					// R59 Col I
					Cell R59Cell6 = row.createCell(8);
					if (record.getR59_net_amt_bwp() != null) {
						R59Cell6.setCellValue(record.getR59_net_amt_bwp().doubleValue());
						R59Cell6.setCellStyle(numberStyle);
					} else {
						R59Cell6.setCellValue("");
						R59Cell6.setCellStyle(textStyle);
					}
					// R59 Col J
					Cell R59Cell7 = row.createCell(9);
					if (record.getR59_bal_sub() != null) {
						R59Cell7.setCellValue(record.getR59_bal_sub().doubleValue());
						R59Cell7.setCellStyle(numberStyle);
					} else {
						R59Cell7.setCellValue("");
						R59Cell7.setCellStyle(textStyle);
					}
					// R59 Col K
					Cell R59Cell8 = row.createCell(10);
					if (record.getR59_bal_sub_bwp() != null) {
						R59Cell8.setCellValue(record.getR59_bal_sub_bwp().doubleValue());
						R59Cell8.setCellStyle(numberStyle);
					} else {
						R59Cell8.setCellValue("");
						R59Cell8.setCellStyle(textStyle);
					}
					// R59 Col L
					Cell R59Cell9 = row.createCell(11);
					if (record.getR59_bal_sub_diaries() != null) {
						R59Cell9.setCellValue(record.getR59_bal_sub_diaries().doubleValue());
						R59Cell9.setCellStyle(numberStyle);
					} else {
						R59Cell9.setCellValue("");
						R59Cell9.setCellStyle(textStyle);
					}
					// R59 Col M
					Cell R59Cell10 = row.createCell(12);
					if (record.getR59_bal_sub_diaries_bwp() != null) {
						R59Cell10.setCellValue(record.getR59_bal_sub_diaries_bwp().doubleValue());
						R59Cell10.setCellStyle(numberStyle);
					} else {
						R59Cell10.setCellValue("");
						R59Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(59);
					Cell R60Cell1 = row.createCell(3);
					if (record.getR60_fig_bal_sheet() != null) {
						R60Cell1.setCellValue(record.getR60_fig_bal_sheet().doubleValue());
						R60Cell1.setCellStyle(numberStyle);
					} else {
						R60Cell1.setCellValue("");
						R60Cell1.setCellStyle(textStyle);
					}

					// R60 Col E
					Cell R60Cell2 = row.createCell(4);
					if (record.getR60_fig_bal_sheet_bwp() != null) {
						R60Cell2.setCellValue(record.getR60_fig_bal_sheet_bwp().doubleValue());
						R60Cell2.setCellStyle(numberStyle);
					} else {
						R60Cell2.setCellValue("");
						R60Cell2.setCellStyle(textStyle);
					}

					// R60 Col F
					Cell R60Cell3 = row.createCell(5);
					if (record.getR60_amt_statement_adj() != null) {
						R60Cell3.setCellValue(record.getR60_amt_statement_adj().doubleValue());
						R60Cell3.setCellStyle(numberStyle);
					} else {
						R60Cell3.setCellValue("");
						R60Cell3.setCellStyle(textStyle);
					}
					// R60 Col G
					Cell R60Cell4 = row.createCell(6);
					if (record.getR60_amt_statement_adj_bwp() != null) {
						R60Cell4.setCellValue(record.getR60_amt_statement_adj_bwp().doubleValue());
						R60Cell4.setCellStyle(numberStyle);
					} else {
						R60Cell4.setCellValue("");
						R60Cell4.setCellStyle(textStyle);
					}
					// // R60 Col H
					// Cell R60Cell5 = row.createCell(7);
					// if (record.getR60_net_amt() != null) {
					// R60Cell5.setCellValue(record.getR60_net_amt().doubleValue());
					// R60Cell5.setCellStyle(numberStyle);
					// } else {
					// R60Cell5.setCellValue("");
					// R60Cell5.setCellStyle(textStyle);
					// }
					// R60 Col I
					Cell R60Cell6 = row.createCell(8);
					if (record.getR60_net_amt_bwp() != null) {
						R60Cell6.setCellValue(record.getR60_net_amt_bwp().doubleValue());
						R60Cell6.setCellStyle(numberStyle);
					} else {
						R60Cell6.setCellValue("");
						R60Cell6.setCellStyle(textStyle);
					}
					// R60 Col J
					Cell R60Cell7 = row.createCell(9);
					if (record.getR60_bal_sub() != null) {
						R60Cell7.setCellValue(record.getR60_bal_sub().doubleValue());
						R60Cell7.setCellStyle(numberStyle);
					} else {
						R60Cell7.setCellValue("");
						R60Cell7.setCellStyle(textStyle);
					}
					// R60 Col K
					Cell R60Cell8 = row.createCell(10);
					if (record.getR60_bal_sub_bwp() != null) {
						R60Cell8.setCellValue(record.getR60_bal_sub_bwp().doubleValue());
						R60Cell8.setCellStyle(numberStyle);
					} else {
						R60Cell8.setCellValue("");
						R60Cell8.setCellStyle(textStyle);
					}
					// R60 Col L
					Cell R60Cell9 = row.createCell(11);
					if (record.getR60_bal_sub_diaries() != null) {
						R60Cell9.setCellValue(record.getR60_bal_sub_diaries().doubleValue());
						R60Cell9.setCellStyle(numberStyle);
					} else {
						R60Cell9.setCellValue("");
						R60Cell9.setCellStyle(textStyle);
					}
					// R60 Col M
					Cell R60Cell10 = row.createCell(12);
					if (record.getR60_bal_sub_diaries_bwp() != null) {
						R60Cell10.setCellValue(record.getR60_bal_sub_diaries_bwp().doubleValue());
						R60Cell10.setCellStyle(numberStyle);
					} else {
						R60Cell10.setCellValue("");
						R60Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(60);
					Cell R61Cell1 = row.createCell(3);
					if (record.getR61_fig_bal_sheet() != null) {
						R61Cell1.setCellValue(record.getR61_fig_bal_sheet().doubleValue());
						R61Cell1.setCellStyle(numberStyle);
					} else {
						R61Cell1.setCellValue("");
						R61Cell1.setCellStyle(textStyle);
					}

					// R61 Col E
					Cell R61Cell2 = row.createCell(4);
					if (record.getR61_fig_bal_sheet_bwp() != null) {
						R61Cell2.setCellValue(record.getR61_fig_bal_sheet_bwp().doubleValue());
						R61Cell2.setCellStyle(numberStyle);
					} else {
						R61Cell2.setCellValue("");
						R61Cell2.setCellStyle(textStyle);
					}

					// R61 Col F
					Cell R61Cell3 = row.createCell(5);
					if (record.getR61_amt_statement_adj() != null) {
						R61Cell3.setCellValue(record.getR61_amt_statement_adj().doubleValue());
						R61Cell3.setCellStyle(numberStyle);
					} else {
						R61Cell3.setCellValue("");
						R61Cell3.setCellStyle(textStyle);
					}
					// R61 Col G
					Cell R61Cell4 = row.createCell(6);
					if (record.getR61_amt_statement_adj_bwp() != null) {
						R61Cell4.setCellValue(record.getR61_amt_statement_adj_bwp().doubleValue());
						R61Cell4.setCellStyle(numberStyle);
					} else {
						R61Cell4.setCellValue("");
						R61Cell4.setCellStyle(textStyle);
					}
					// // R61 Col H
					// Cell R61Cell5 = row.createCell(7);
					// if (record.getR61_net_amt() != null) {
					// R61Cell5.setCellValue(record.getR61_net_amt().doubleValue());
					// R61Cell5.setCellStyle(numberStyle);
					// } else {
					// R61Cell5.setCellValue("");
					// R61Cell5.setCellStyle(textStyle);
					// }
					// R61 Col I
					Cell R61Cell6 = row.createCell(8);
					if (record.getR61_net_amt_bwp() != null) {
						R61Cell6.setCellValue(record.getR61_net_amt_bwp().doubleValue());
						R61Cell6.setCellStyle(numberStyle);
					} else {
						R61Cell6.setCellValue("");
						R61Cell6.setCellStyle(textStyle);
					}
					// R61 Col J
					Cell R61Cell7 = row.createCell(9);
					if (record.getR61_bal_sub() != null) {
						R61Cell7.setCellValue(record.getR61_bal_sub().doubleValue());
						R61Cell7.setCellStyle(numberStyle);
					} else {
						R61Cell7.setCellValue("");
						R61Cell7.setCellStyle(textStyle);
					}
					// R61 Col K
					Cell R61Cell8 = row.createCell(10);
					if (record.getR61_bal_sub_bwp() != null) {
						R61Cell8.setCellValue(record.getR61_bal_sub_bwp().doubleValue());
						R61Cell8.setCellStyle(numberStyle);
					} else {
						R61Cell8.setCellValue("");
						R61Cell8.setCellStyle(textStyle);
					}
					// R61 Col L
					Cell R61Cell9 = row.createCell(11);
					if (record.getR61_bal_sub_diaries() != null) {
						R61Cell9.setCellValue(record.getR61_bal_sub_diaries().doubleValue());
						R61Cell9.setCellStyle(numberStyle);
					} else {
						R61Cell9.setCellValue("");
						R61Cell9.setCellStyle(textStyle);
					}
					// R61 Col M
					Cell R61Cell10 = row.createCell(12);
					if (record.getR61_bal_sub_diaries_bwp() != null) {
						R61Cell10.setCellValue(record.getR61_bal_sub_diaries_bwp().doubleValue());
						R61Cell10.setCellStyle(numberStyle);
					} else {
						R61Cell10.setCellValue("");
						R61Cell10.setCellStyle(textStyle);
					}
					row = sheet.getRow(61);
					Cell R62Cell1 = row.createCell(3);
					if (record.getR62_fig_bal_sheet() != null) {
						R62Cell1.setCellValue(record.getR62_fig_bal_sheet().doubleValue());
						R62Cell1.setCellStyle(numberStyle);
					} else {
						R62Cell1.setCellValue("");
						R62Cell1.setCellStyle(textStyle);
					}

					// R62 Col E
					Cell R62Cell2 = row.createCell(4);
					if (record.getR62_fig_bal_sheet_bwp() != null) {
						R62Cell2.setCellValue(record.getR62_fig_bal_sheet_bwp().doubleValue());
						R62Cell2.setCellStyle(numberStyle);
					} else {
						R62Cell2.setCellValue("");
						R62Cell2.setCellStyle(textStyle);
					}

					// R62 Col F
					Cell R62Cell3 = row.createCell(5);
					if (record.getR62_amt_statement_adj() != null) {
						R62Cell3.setCellValue(record.getR62_amt_statement_adj().doubleValue());
						R62Cell3.setCellStyle(numberStyle);
					} else {
						R62Cell3.setCellValue("");
						R62Cell3.setCellStyle(textStyle);
					}
					// R62 Col G
					Cell R62Cell4 = row.createCell(6);
					if (record.getR62_amt_statement_adj_bwp() != null) {
						R62Cell4.setCellValue(record.getR62_amt_statement_adj_bwp().doubleValue());
						R62Cell4.setCellStyle(numberStyle);
					} else {
						R62Cell4.setCellValue("");
						R62Cell4.setCellStyle(textStyle);
					}
					// // R62 Col H
					// Cell R62Cell5 = row.createCell(7);
					// if (record.getR62_net_amt() != null) {
					// R62Cell5.setCellValue(record.getR62_net_amt().doubleValue());
					// R62Cell5.setCellStyle(numberStyle);
					// } else {
					// R62Cell5.setCellValue("");
					// R62Cell5.setCellStyle(textStyle);
					// }
					// R62 Col I
					Cell R62Cell6 = row.createCell(8);
					if (record.getR62_net_amt_bwp() != null) {
						R62Cell6.setCellValue(record.getR62_net_amt_bwp().doubleValue());
						R62Cell6.setCellStyle(numberStyle);
					} else {
						R62Cell6.setCellValue("");
						R62Cell6.setCellStyle(textStyle);
					}
					// R62 Col J
					Cell R62Cell7 = row.createCell(9);
					if (record.getR62_bal_sub() != null) {
						R62Cell7.setCellValue(record.getR62_bal_sub().doubleValue());
						R62Cell7.setCellStyle(numberStyle);
					} else {
						R62Cell7.setCellValue("");
						R62Cell7.setCellStyle(textStyle);
					}
					// R62 Col K
					Cell R62Cell8 = row.createCell(10);
					if (record.getR62_bal_sub_bwp() != null) {
						R62Cell8.setCellValue(record.getR62_bal_sub_bwp().doubleValue());
						R62Cell8.setCellStyle(numberStyle);
					} else {
						R62Cell8.setCellValue("");
						R62Cell8.setCellStyle(textStyle);
					}
					// R62 Col L
					Cell R62Cell9 = row.createCell(11);
					if (record.getR62_bal_sub_diaries() != null) {
						R62Cell9.setCellValue(record.getR62_bal_sub_diaries().doubleValue());
						R62Cell9.setCellStyle(numberStyle);
					} else {
						R62Cell9.setCellValue("");
						R62Cell9.setCellStyle(textStyle);
					}
					// R62 Col M
					Cell R62Cell10 = row.createCell(12);
					if (record.getR62_bal_sub_diaries_bwp() != null) {
						R62Cell10.setCellValue(record.getR62_bal_sub_diaries_bwp().doubleValue());
						R62Cell10.setCellStyle(numberStyle);
					} else {
						R62Cell10.setCellValue("");
						R62Cell10.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "PL_SCHS ARCHIVAL SUMMARY", null,
						"BRRS_PL_SCHS_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	@Transactional
	public void updateReport(PL_SCHS_Summary_Entity updatedEntity, String type) {

		boolean isResub = "RESUB".equalsIgnoreCase(type);

		String tableName = isResub ? "BRRS_PL_SCHS_ARCHIVALTABLE_SUMMARY" : "BRRS_PL_SCHS_SUMMARYTABLE";

		System.out.println("Came to PL SCHS Update");
		System.out.println("Type : " + (isResub ? "RESUB" : "NORMAL"));
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		// Allowed rows
		int[] rows = { 12, 13, 18, 19, 21, 22, 23, 24, 25, 26, 27, 28, 29, 31, 42, 43, 54, 61, 63 };

		try {

			// Allowed fields
			String[] fields = { "intrest_div", "other_income", "operating_expenses", "fig_bal_sheet",
					"fig_bal_sheet_bwp", "amt_statement_adj", "amt_statement_adj_bwp", "net_amt", "net_amt_bwp",
					"bal_sub", "bal_sub_bwp", "bal_sub_diaries", "bal_sub_diaries_bwp" };

			for (int r : rows) {

				for (String field : fields) {

					String getterName = "getR" + r + "_" + field;

					try {

						Method getter = PL_SCHS_Summary_Entity.class.getMethod(getterName);

						Object value = getter.invoke(updatedEntity);

						// Skip null values
						if (value == null) {
							continue;
						}

						String columnName = "R" + r + "_" + field.toUpperCase();

						String sql = "UPDATE " + tableName + " SET " + columnName + " = ? " + " WHERE REPORT_DATE = ?";

						int updatedRows = jdbcTemplate.update(sql, value, updatedEntity.getREPORT_DATE());

						System.out.println(columnName + " Updated -> " + value + " Rows : " + updatedRows);

					} catch (NoSuchMethodException e) {
						// Skip fields that don't exist
						continue;
					}
				}
			}

			System.out.println("PL SCHS Update Completed");

		} catch (Exception e) {

			System.err.println("===== PL SCHS UPDATE ERROR =====");
			e.printStackTrace();

			throw new RuntimeException("Error while updating PL SCHS fields", e);
		}
	}

	// Resubmission
	public List<Object[]> getPL_SCHSResub() {
		List<Object[]> resubList = new ArrayList<>();

		try {

			List<PL_SCHS_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (PL_SCHS_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " Resub records");
				PL_SCHS_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest Resub version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No Resub data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  PL_SCHS  Resub data: " + e.getMessage());
			e.printStackTrace();
		}

		return resubList;
	}
}