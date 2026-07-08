package com.bornfire.brrs.services;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
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

import com.bornfire.brrs.entities.M_OR1_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_OR1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_OR1_Detail_Entity;
import com.bornfire.brrs.entities.M_OR1_Summary_Entity;
import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional
public class BRRS_M_OR1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_OR1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// =========================================================
	// JDBC QUERY METHODS
	// =========================================================

	public List<M_OR1_Summary_Entity> getSummaryByDate(Date reportDate) {
		return jdbcTemplate.query(
			"select * from BRRS_M_OR1_SUMMARYTABLE where report_date = ?",
			new Object[]{reportDate}, new M_OR1SummaryRowMapper());
	}

	public List<M_OR1_Detail_Entity> getDetailByDate(Date reportDate) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_OR1_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
			new Object[]{reportDate}, new M_OR1DetailRowMapper());
	}

	public List<M_OR1_Detail_Entity> getDetailByDatePaged(Date reportDate, int startpage, int endpage) {
		return jdbcTemplate.query(
			"select * from BRRS_M_OR1_DETAILTABLE where REPORT_DATE=? offset ? rows fetch next ? rows only",
			new Object[]{reportDate, startpage, endpage}, new M_OR1DetailRowMapper());
	}

	public int getDetailCount(Date reportDate) {
		return jdbcTemplate.queryForObject(
			"select count(*) from BRRS_M_OR1_DETAILTABLE where REPORT_DATE = ?",
			new Object[]{reportDate}, Integer.class);
	}

	public List<M_OR1_Detail_Entity> getDetailByRowIdAndColumnId(String reportLable, String reportAddlCriteria1, Date reportdate) {
		return jdbcTemplate.query(
			"select * from BRRS_M_OR1_DETAILTABLE where REPORT_LABLE =? and REPORT_ADDL_CRITERIA_1=? AND REPORT_DATE=?",
			new Object[]{reportLable, reportAddlCriteria1, reportdate}, new M_OR1DetailRowMapper());
	}

	public M_OR1_Detail_Entity getDetailByAcctNumber(String acctNumber) {
		List<M_OR1_Detail_Entity> results = jdbcTemplate.query(
			"SELECT * FROM BRRS_M_OR1_DETAILTABLE WHERE ACCT_NUMBER = ?",
			new Object[]{acctNumber}, new M_OR1DetailRowMapper());
		return results.isEmpty() ? null : results.get(0);
	}

	public List<M_OR1_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportdate, String dataEntryVersion) {
		return jdbcTemplate.query(
			"select * from BRRS_M_OR1_ARCHIVALTABLE_DETAIL where REPORT_DATE=? AND DATA_ENTRY_VERSION=?",
			new Object[]{reportdate, dataEntryVersion}, new M_OR1ArchivalDetailRowMapper());
	}

	public List<M_OR1_Archival_Detail_Entity> getArchivalDetailByRowIdAndColumnId(String reportLable, String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {
		return jdbcTemplate.query(
			"select * from BRRS_M_OR1_ARCHIVALTABLE_DETAIL where REPORT_LABLE =? and REPORT_ADDL_CRITERIA_1=? AND REPORT_DATE=? AND DATA_ENTRY_VERSION=?",
			new Object[]{reportLable, reportAddlCriteria1, reportdate, dataEntryVersion}, new M_OR1ArchivalDetailRowMapper());
	}

	public List<M_OR1_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date report_date, BigDecimal report_version) {
		return jdbcTemplate.query(
			"select * from BRRS_M_OR1_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
			new Object[]{report_date, report_version}, new M_OR1ArchivalSummaryRowMapper());
	}

	public List<M_OR1_Archival_Summary_Entity> getArchivalSummaryWithVersion() {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_OR1_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC",
			new M_OR1ArchivalSummaryRowMapper());
	}

	// =========================================================
	// JDBC WRITE METHODS
	// =========================================================

	private static final String R_FIELDS_SET =
	"R10_PRODUCT=?,R10_MONTH=?,R10_GROSS_INCOME=?,R10_AGGREGATE_GROSS_INCOME=?,R10_RISK_WEIGHT_FACTOR=?," +
	"R11_PRODUCT=?,R11_MONTH=?,R11_GROSS_INCOME=?,R11_AGGREGATE_GROSS_INCOME=?,R11_RISK_WEIGHT_FACTOR=?," +
	"R12_PRODUCT=?,R12_MONTH=?,R12_GROSS_INCOME=?,R12_AGGREGATE_GROSS_INCOME=?,R12_RISK_WEIGHT_FACTOR=?," +
	"R13_PRODUCT=?,R13_MONTH=?,R13_GROSS_INCOME=?,R13_AGGREGATE_GROSS_INCOME=?,R13_RISK_WEIGHT_FACTOR=?," +
	"R14_PRODUCT=?,R14_MONTH=?,R14_GROSS_INCOME=?,R14_AGGREGATE_GROSS_INCOME=?,R14_RISK_WEIGHT_FACTOR=?," +
	"R15_PRODUCT=?,R15_MONTH=?,R15_GROSS_INCOME=?,R15_AGGREGATE_GROSS_INCOME=?,R15_RISK_WEIGHT_FACTOR=?," +
	"R16_PRODUCT=?,R16_MONTH=?,R16_GROSS_INCOME=?,R16_AGGREGATE_GROSS_INCOME=?,R16_RISK_WEIGHT_FACTOR=?," +
	"R17_PRODUCT=?,R17_MONTH=?,R17_GROSS_INCOME=?,R17_AGGREGATE_GROSS_INCOME=?,R17_RISK_WEIGHT_FACTOR=?," +
	"R18_PRODUCT=?,R18_MONTH=?,R18_GROSS_INCOME=?,R18_AGGREGATE_GROSS_INCOME=?,R18_RISK_WEIGHT_FACTOR=?," +
	"R19_PRODUCT=?,R19_MONTH=?,R19_GROSS_INCOME=?,R19_AGGREGATE_GROSS_INCOME=?,R19_RISK_WEIGHT_FACTOR=?," +
	"R20_PRODUCT=?,R20_MONTH=?,R20_GROSS_INCOME=?,R20_AGGREGATE_GROSS_INCOME=?,R20_RISK_WEIGHT_FACTOR=?," +
	"R21_PRODUCT=?,R21_MONTH=?,R21_GROSS_INCOME=?,R21_AGGREGATE_GROSS_INCOME=?,R21_RISK_WEIGHT_FACTOR=?," +
	"R22_PRODUCT=?,R22_MONTH=?,R22_GROSS_INCOME=?,R22_AGGREGATE_GROSS_INCOME=?,R22_RISK_WEIGHT_FACTOR=?," +
	"R23_PRODUCT=?,R23_MONTH=?,R23_GROSS_INCOME=?,R23_AGGREGATE_GROSS_INCOME=?,R23_RISK_WEIGHT_FACTOR=?," +
	"R24_PRODUCT=?,R24_MONTH=?,R24_GROSS_INCOME=?,R24_AGGREGATE_GROSS_INCOME=?,R24_RISK_WEIGHT_FACTOR=?," +
	"R25_PRODUCT=?,R25_MONTH=?,R25_GROSS_INCOME=?,R25_AGGREGATE_GROSS_INCOME=?,R25_RISK_WEIGHT_FACTOR=?," +
	"R26_PRODUCT=?,R26_MONTH=?,R26_GROSS_INCOME=?,R26_AGGREGATE_GROSS_INCOME=?,R26_RISK_WEIGHT_FACTOR=?," +
	"R27_PRODUCT=?,R27_MONTH=?,R27_GROSS_INCOME=?,R27_AGGREGATE_GROSS_INCOME=?,R27_RISK_WEIGHT_FACTOR=?," +
	"R28_PRODUCT=?,R28_MONTH=?,R28_GROSS_INCOME=?,R28_AGGREGATE_GROSS_INCOME=?,R28_RISK_WEIGHT_FACTOR=?," +
	"R29_PRODUCT=?,R29_MONTH=?,R29_GROSS_INCOME=?,R29_AGGREGATE_GROSS_INCOME=?,R29_RISK_WEIGHT_FACTOR=?," +
	"R30_PRODUCT=?,R30_MONTH=?,R30_GROSS_INCOME=?,R30_AGGREGATE_GROSS_INCOME=?,R30_RISK_WEIGHT_FACTOR=?," +
	"R31_PRODUCT=?,R31_MONTH=?,R31_GROSS_INCOME=?,R31_AGGREGATE_GROSS_INCOME=?,R31_RISK_WEIGHT_FACTOR=?," +
	"R32_PRODUCT=?,R32_MONTH=?,R32_GROSS_INCOME=?,R32_AGGREGATE_GROSS_INCOME=?,R32_RISK_WEIGHT_FACTOR=?," +
	"R33_PRODUCT=?,R33_MONTH=?,R33_GROSS_INCOME=?,R33_AGGREGATE_GROSS_INCOME=?,R33_RISK_WEIGHT_FACTOR=?," +
	"R34_PRODUCT=?,R34_MONTH=?,R34_GROSS_INCOME=?,R34_AGGREGATE_GROSS_INCOME=?,R34_RISK_WEIGHT_FACTOR=?," +
	"R35_PRODUCT=?,R35_MONTH=?,R35_GROSS_INCOME=?,R35_AGGREGATE_GROSS_INCOME=?,R35_RISK_WEIGHT_FACTOR=?," +
	"R36_PRODUCT=?,R36_MONTH=?,R36_GROSS_INCOME=?,R36_AGGREGATE_GROSS_INCOME=?,R36_RISK_WEIGHT_FACTOR=?," +
	"R37_PRODUCT=?,R37_MONTH=?,R37_GROSS_INCOME=?,R37_AGGREGATE_GROSS_INCOME=?,R37_RISK_WEIGHT_FACTOR=?," +
	"R38_PRODUCT=?,R38_MONTH=?,R38_GROSS_INCOME=?,R38_AGGREGATE_GROSS_INCOME=?,R38_RISK_WEIGHT_FACTOR=?," +
	"R39_PRODUCT=?,R39_MONTH=?,R39_GROSS_INCOME=?,R39_AGGREGATE_GROSS_INCOME=?,R39_RISK_WEIGHT_FACTOR=?," +
	"R40_PRODUCT=?,R40_MONTH=?,R40_GROSS_INCOME=?,R40_AGGREGATE_GROSS_INCOME=?,R40_RISK_WEIGHT_FACTOR=?," +
	"R41_PRODUCT=?,R41_MONTH=?,R41_GROSS_INCOME=?,R41_AGGREGATE_GROSS_INCOME=?,R41_RISK_WEIGHT_FACTOR=?," +
	"R42_PRODUCT=?,R42_MONTH=?,R42_GROSS_INCOME=?,R42_AGGREGATE_GROSS_INCOME=?,R42_RISK_WEIGHT_FACTOR=?," +
	"R43_PRODUCT=?,R43_MONTH=?,R43_GROSS_INCOME=?,R43_AGGREGATE_GROSS_INCOME=?,R43_RISK_WEIGHT_FACTOR=?," +
	"R44_PRODUCT=?,R44_MONTH=?,R44_GROSS_INCOME=?,R44_AGGREGATE_GROSS_INCOME=?,R44_RISK_WEIGHT_FACTOR=?," +
	"R45_PRODUCT=?,R45_MONTH=?,R45_GROSS_INCOME=?,R45_AGGREGATE_GROSS_INCOME=?,R45_RISK_WEIGHT_FACTOR=?," +
	"R46_PRODUCT=?,R46_MONTH=?,R46_GROSS_INCOME=?,R46_AGGREGATE_GROSS_INCOME=?,R46_RISK_WEIGHT_FACTOR=?," +
	"R47_PRODUCT=?,R47_MONTH=?,R47_GROSS_INCOME=?,R47_AGGREGATE_GROSS_INCOME=?,R47_RISK_WEIGHT_FACTOR=?," +
	"R48_PRODUCT=?,R48_MONTH=?,R48_GROSS_INCOME=?,R48_AGGREGATE_GROSS_INCOME=?,R48_RISK_WEIGHT_FACTOR=?," +
	"R49_PRODUCT=?,R49_MONTH=?,R49_GROSS_INCOME=?,R49_AGGREGATE_GROSS_INCOME=?,R49_RISK_WEIGHT_FACTOR=?," +
	"R50_PRODUCT=?,R50_MONTH=?,R50_GROSS_INCOME=?,R50_AGGREGATE_GROSS_INCOME=?,R50_RISK_WEIGHT_FACTOR=?," +
	"R51_PRODUCT=?,R51_MONTH=?,R51_GROSS_INCOME=?,R51_AGGREGATE_GROSS_INCOME=?,R51_RISK_WEIGHT_FACTOR=?," +
	"R52_PRODUCT=?,R52_MONTH=?,R52_GROSS_INCOME=?,R52_AGGREGATE_GROSS_INCOME=?,R52_RISK_WEIGHT_FACTOR=?," +
	"R53_PRODUCT=?,R53_MONTH=?,R53_GROSS_INCOME=?,R53_AGGREGATE_GROSS_INCOME=?,R53_RISK_WEIGHT_FACTOR=?," +
	"R54_PRODUCT=?,R54_MONTH=?,R54_GROSS_INCOME=?,R54_AGGREGATE_GROSS_INCOME=?,R54_RISK_WEIGHT_FACTOR=?," +
	"R55_PRODUCT=?,R55_MONTH=?,R55_GROSS_INCOME=?,R55_AGGREGATE_GROSS_INCOME=?,R55_RISK_WEIGHT_FACTOR=?," +
	"R56_PRODUCT=?,R56_MONTH=?,R56_GROSS_INCOME=?,R56_AGGREGATE_GROSS_INCOME=?,R56_RISK_WEIGHT_FACTOR=?";

	private Object[] summaryFieldValues(M_OR1_Summary_Entity e) {
		return new Object[] {
			e.getR10_product(),e.getR10_month(),e.getR10_gross_income(),e.getR10_aggregate_gross_income(),e.getR10_risk_weight_factor(),
			e.getR11_product(),e.getR11_month(),e.getR11_gross_income(),e.getR11_aggregate_gross_income(),e.getR11_risk_weight_factor(),
			e.getR12_product(),e.getR12_month(),e.getR12_gross_income(),e.getR12_aggregate_gross_income(),e.getR12_risk_weight_factor(),
			e.getR13_product(),e.getR13_month(),e.getR13_gross_income(),e.getR13_aggregate_gross_income(),e.getR13_risk_weight_factor(),
			e.getR14_product(),e.getR14_month(),e.getR14_gross_income(),e.getR14_aggregate_gross_income(),e.getR14_risk_weight_factor(),
			e.getR15_product(),e.getR15_month(),e.getR15_gross_income(),e.getR15_aggregate_gross_income(),e.getR15_risk_weight_factor(),
			e.getR16_product(),e.getR16_month(),e.getR16_gross_income(),e.getR16_aggregate_gross_income(),e.getR16_risk_weight_factor(),
			e.getR17_product(),e.getR17_month(),e.getR17_gross_income(),e.getR17_aggregate_gross_income(),e.getR17_risk_weight_factor(),
			e.getR18_product(),e.getR18_month(),e.getR18_gross_income(),e.getR18_aggregate_gross_income(),e.getR18_risk_weight_factor(),
			e.getR19_product(),e.getR19_month(),e.getR19_gross_income(),e.getR19_aggregate_gross_income(),e.getR19_risk_weight_factor(),
			e.getR20_product(),e.getR20_month(),e.getR20_gross_income(),e.getR20_aggregate_gross_income(),e.getR20_risk_weight_factor(),
			e.getR21_product(),e.getR21_month(),e.getR21_gross_income(),e.getR21_aggregate_gross_income(),e.getR21_risk_weight_factor(),
			e.getR22_product(),e.getR22_month(),e.getR22_gross_income(),e.getR22_aggregate_gross_income(),e.getR22_risk_weight_factor(),
			e.getR23_product(),e.getR23_month(),e.getR23_gross_income(),e.getR23_aggregate_gross_income(),e.getR23_risk_weight_factor(),
			e.getR24_product(),e.getR24_month(),e.getR24_gross_income(),e.getR24_aggregate_gross_income(),e.getR24_risk_weight_factor(),
			e.getR25_product(),e.getR25_month(),e.getR25_gross_income(),e.getR25_aggregate_gross_income(),e.getR25_risk_weight_factor(),
			e.getR26_product(),e.getR26_month(),e.getR26_gross_income(),e.getR26_aggregate_gross_income(),e.getR26_risk_weight_factor(),
			e.getR27_product(),e.getR27_month(),e.getR27_gross_income(),e.getR27_aggregate_gross_income(),e.getR27_risk_weight_factor(),
			e.getR28_product(),e.getR28_month(),e.getR28_gross_income(),e.getR28_aggregate_gross_income(),e.getR28_risk_weight_factor(),
			e.getR29_product(),e.getR29_month(),e.getR29_gross_income(),e.getR29_aggregate_gross_income(),e.getR29_risk_weight_factor(),
			e.getR30_product(),e.getR30_month(),e.getR30_gross_income(),e.getR30_aggregate_gross_income(),e.getR30_risk_weight_factor(),
			e.getR31_product(),e.getR31_month(),e.getR31_gross_income(),e.getR31_aggregate_gross_income(),e.getR31_risk_weight_factor(),
			e.getR32_product(),e.getR32_month(),e.getR32_gross_income(),e.getR32_aggregate_gross_income(),e.getR32_risk_weight_factor(),
			e.getR33_product(),e.getR33_month(),e.getR33_gross_income(),e.getR33_aggregate_gross_income(),e.getR33_risk_weight_factor(),
			e.getR34_product(),e.getR34_month(),e.getR34_gross_income(),e.getR34_aggregate_gross_income(),e.getR34_risk_weight_factor(),
			e.getR35_product(),e.getR35_month(),e.getR35_gross_income(),e.getR35_aggregate_gross_income(),e.getR35_risk_weight_factor(),
			e.getR36_product(),e.getR36_month(),e.getR36_gross_income(),e.getR36_aggregate_gross_income(),e.getR36_risk_weight_factor(),
			e.getR37_product(),e.getR37_month(),e.getR37_gross_income(),e.getR37_aggregate_gross_income(),e.getR37_risk_weight_factor(),
			e.getR38_product(),e.getR38_month(),e.getR38_gross_income(),e.getR38_aggregate_gross_income(),e.getR38_risk_weight_factor(),
			e.getR39_product(),e.getR39_month(),e.getR39_gross_income(),e.getR39_aggregate_gross_income(),e.getR39_risk_weight_factor(),
			e.getR40_product(),e.getR40_month(),e.getR40_gross_income(),e.getR40_aggregate_gross_income(),e.getR40_risk_weight_factor(),
			e.getR41_product(),e.getR41_month(),e.getR41_gross_income(),e.getR41_aggregate_gross_income(),e.getR41_risk_weight_factor(),
			e.getR42_product(),e.getR42_month(),e.getR42_gross_income(),e.getR42_aggregate_gross_income(),e.getR42_risk_weight_factor(),
			e.getR43_product(),e.getR43_month(),e.getR43_gross_income(),e.getR43_aggregate_gross_income(),e.getR43_risk_weight_factor(),
			e.getR44_product(),e.getR44_month(),e.getR44_gross_income(),e.getR44_aggregate_gross_income(),e.getR44_risk_weight_factor(),
			e.getR45_product(),e.getR45_month(),e.getR45_gross_income(),e.getR45_aggregate_gross_income(),e.getR45_risk_weight_factor(),
			e.getR46_product(),e.getR46_month(),e.getR46_gross_income(),e.getR46_aggregate_gross_income(),e.getR46_risk_weight_factor(),
			e.getR47_product(),e.getR47_month(),e.getR47_gross_income(),e.getR47_aggregate_gross_income(),e.getR47_risk_weight_factor(),
			e.getR48_product(),e.getR48_month(),e.getR48_gross_income(),e.getR48_aggregate_gross_income(),e.getR48_risk_weight_factor(),
			e.getR49_product(),e.getR49_month(),e.getR49_gross_income(),e.getR49_aggregate_gross_income(),e.getR49_risk_weight_factor(),
			e.getR50_product(),e.getR50_month(),e.getR50_gross_income(),e.getR50_aggregate_gross_income(),e.getR50_risk_weight_factor(),
			e.getR51_product(),e.getR51_month(),e.getR51_gross_income(),e.getR51_aggregate_gross_income(),e.getR51_risk_weight_factor(),
			e.getR52_product(),e.getR52_month(),e.getR52_gross_income(),e.getR52_aggregate_gross_income(),e.getR52_risk_weight_factor(),
			e.getR53_product(),e.getR53_month(),e.getR53_gross_income(),e.getR53_aggregate_gross_income(),e.getR53_risk_weight_factor(),
			e.getR54_product(),e.getR54_month(),e.getR54_gross_income(),e.getR54_aggregate_gross_income(),e.getR54_risk_weight_factor(),
			e.getR55_product(),e.getR55_month(),e.getR55_gross_income(),e.getR55_aggregate_gross_income(),e.getR55_risk_weight_factor(),
			e.getR56_product(),e.getR56_month(),e.getR56_gross_income(),e.getR56_aggregate_gross_income(),e.getR56_risk_weight_factor()
		};
	}

	private void updateSummaryRecord(M_OR1_Summary_Entity e) {
		Object[] rVals = summaryFieldValues(e);
		Object[] params = new Object[rVals.length + 8];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReport_version();
		params[rVals.length + 1] = e.getReport_frequency();
		params[rVals.length + 2] = e.getReport_code();
		params[rVals.length + 3] = e.getReport_desc();
		params[rVals.length + 4] = e.getEntity_flg();
		params[rVals.length + 5] = e.getModify_flg();
		params[rVals.length + 6] = e.getDel_flg();
		params[rVals.length + 7] = e.getReport_date();
		jdbcTemplate.update(
			"UPDATE BRRS_M_OR1_SUMMARYTABLE SET " + R_FIELDS_SET +
			",REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?,REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=?" +
			" WHERE REPORT_DATE=?", params);
	}

	private void updateDetailRecord(M_OR1_Detail_Entity e) {
		Object[] params = new Object[] {
			e.getCustId(), e.getAcctNumber(), e.getAcctName(), e.getDataType(),
			e.getReportAddlCriteria1(), e.getReportLabel(), e.getReportRemarks(), e.getModificationRemarks(),
			e.getDataEntryVersion(), e.getAcctBalanceInpula(), e.getReportDate(), e.getReportName(),
			e.getCreateUser(), e.getCreateTime(), e.getModifyUser(), e.getModifyTime(),
			e.getVerifyUser(), e.getVerifyTime(), e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(),
			e.getSno()
		};
		jdbcTemplate.update(
			"UPDATE BRRS_M_OR1_DETAILTABLE SET CUST_ID=?,ACCT_NUMBER=?,ACCT_NAME=?,DATA_TYPE=?," +
			"REPORT_ADDL_CRITERIA_1=?,REPORT_LABEL=?,REPORT_REMARKS=?,MODIFICATION_REMARKS=?," +
			"DATA_ENTRY_VERSION=?,ACCT_BALANCE_IN_PULA=?,REPORT_DATE=?,REPORT_NAME=?," +
			"CREATE_USER=?,CREATE_TIME=?,MODIFY_USER=?,MODIFY_TIME=?," +
			"VERIFY_USER=?,VERIFY_TIME=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=?" +
			" WHERE SNO=?", params);
	}

	public ModelAndView getM_OR1View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version,HttpServletRequest req1,Model md) {

		ModelAndView mv = new ModelAndView();

	    String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);
		/*
		 * Session hs = sessionFactory.getCurrentSession(); int pageSize =
		 * pageable.getPageSize(); int currentPage = pageable.getPageNumber(); int
		 * startItem = currentPage * pageSize;
		 */

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_OR1_Archival_Summary_Entity> T1Master = new ArrayList<M_OR1_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);

		} else {

			List<M_OR1_Summary_Entity> T1Master = new ArrayList<M_OR1_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				T1Master = getSummaryByDate(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_OR1");

		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_OR1currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version,HttpServletRequest req1,Model md) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();	
		
	    String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);
//		Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLable = null;
			String reportAddlCriteria1 = null;

			// ✅ Split the filter string here
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLable = parts[0];
					reportAddlCriteria1 = parts[1];
				}
			}

			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				System.out.println(version);
				// 🔹 Archival branch
				List<M_OR1_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria1 != null) {
					T1Dt1 = getArchivalDetailByRowIdAndColumnId(reportLable, reportAddlCriteria1,
							parsedDate, version);
				} else {
					T1Dt1 = getArchivalDetailByDateAndVersion(parsedDate, version);
					totalPages = getDetailCount(parsedDate);
					System.out.println(T1Dt1.size());
					mv.addObject("pagination", "YES");

				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				System.out.println("came to detail method");
				// 🔹 Current branch
				List<M_OR1_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria1 != null) {
					T1Dt1 = getDetailByRowIdAndColumnId(reportLable, reportAddlCriteria1, parsedDate);
				} else {
					T1Dt1 = getDetailByDatePaged(parsedDate, currentPage, pageSize);
					totalPages = getDetailCount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("LISTCOUNT for Detail page is : " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		// ✅ Common attributes
		mv.setViewName("BRRS/M_OR1");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	public void updateReport(M_OR1_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("report_date: " + updatedEntity.getReport_date());

		List<M_OR1_Summary_Entity> existingList = getSummaryByDate(updatedEntity.getReport_date());
		if (existingList.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}
		M_OR1_Summary_Entity existing = existingList.get(0);

		try {
			// Loop from R11 to R50 and copy fields
			int[] rows = new int[56];
			for (int k = 0, r = 10; r <= 22; r++, k++) {
				rows[k] = r;
			}

			for (int i : rows) {
				String prefix = "R" + i + "_"; // Use capital R (same as your working code)
				String[] fields = { "gross_income" };

				for (String field : fields) {
					try {
						String getterName = "get" + prefix + field;
						String setterName = "set" + prefix + field;

						Method getter = M_OR1_Summary_Entity.class.getMethod(getterName);
						Method setter = M_OR1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing getter/setter gracefully
						continue;
					}
				}
			}

			// Loop from R17 to R30 and copy fields
			// Loop from R23 to R34 and copy fields
			int[] rows2 = new int[12];
			for (int k = 0, r = 23; r <= 34; r++, k++) {
				rows2[k] = r;
			}

			for (int i : rows2) {
				String prefix = "R" + i + "_"; // FIX: Capital R (same as your working model)
				String[] fields = { "gross_income" };

				for (String field : fields) {
					try {
						String getterName = "get" + prefix + field;
						String setterName = "set" + prefix + field;

						Method getter = M_OR1_Summary_Entity.class.getMethod(getterName);
						Method setter = M_OR1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing getter/setter gracefully
						continue;
					}
				}
			}

			// Loop from R36 to R46 and copy fields
			int[] rows3 = new int[11];
			for (int k = 0, r = 36; r <= 46; r++, k++) {
				rows3[k] = r;
			}

			for (int i : rows3) {
				String prefix = "R" + i + "_"; // FIXED: Capital 'R'
				String[] fields = { "gross_income" };

				for (String field : fields) {
					try {
						String getterName = "get" + prefix + field;
						String setterName = "set" + prefix + field;

						Method getter = M_OR1_Summary_Entity.class.getMethod(getterName);
						Method setter = M_OR1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing getter/setter gracefully
						continue;
					}
				}
			}

			int[] Rows = { 22, 35, 48 };
			for (int i : Rows) {
				String prefix = "R" + i + "_";
				String[] fields = { "gross_income" };

				for (String field : fields) {
					try {
						String getterName = "get" + prefix + field;
						String setterName = "set" + prefix + field;

						Method getter = M_OR1_Summary_Entity.class.getMethod(getterName);
						Method setter = M_OR1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing getter/setter gracefully
						continue;
					}
				}
			}

			int[] Rows1 = { 50, 51, 52, 53, 54 };
			for (int i : Rows1) {
				String prefix = "R" + i + "_";
				String[] fields = { "aggregate_gross_income" };

				for (String field : fields) {
					try {
						String getterName = "get" + prefix + field;
						String setterName = "set" + prefix + field;

						Method getter = M_OR1_Summary_Entity.class.getMethod(getterName);
						Method setter = M_OR1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing getter/setter gracefully
						continue;
					}
				}
			}

			int[] Rows2 = { 55, 56 };
			for (int i : Rows2) {
				String prefix = "R" + i + "_";
				String[] fields = { "risk_weight_factor" };

				for (String field : fields) {
					try {
						String getterName = "get" + prefix + field;
						String setterName = "set" + prefix + field;

						Method getter = M_OR1_Summary_Entity.class.getMethod(getterName);
						Method setter = M_OR1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing getter/setter gracefully
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Save updated entity
		System.out.println("abc");
		updateSummaryRecord(existing);
	}

	public byte[] BRRS_M_OR1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println("======= DOWNLOAD DETAILS =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");
		
		
		if (type.equals("ARCHIVAL") & version != null) {
			
			if ("email".equalsIgnoreCase(format)) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_OR1ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} 
			
			else {
			byte[] ARCHIVALreport = getExcelM_OR1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
					version);
			return ARCHIVALreport;
			}
		}
		else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_OR1EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

		List<M_OR1_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M-OR1 report. Returning empty result.");
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
			 
			int startRow = 9;
			
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_OR1_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					//row11
					// Column C
					Cell cell3 = row.createCell(3);
					if (record.getR10_gross_income() != null) {
						cell3.setCellValue(record.getR10_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(10);
					cell3 = row.createCell(3);
					if (record.getR11_gross_income() != null) {
					    cell3.setCellValue(record.getR11_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell3 = row.createCell(3);
					if (record.getR12_gross_income() != null) {
					    cell3.setCellValue(record.getR12_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell3 = row.createCell(3);
					if (record.getR13_gross_income() != null) {
					    cell3.setCellValue(record.getR13_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell3 = row.createCell(3);
					if (record.getR14_gross_income() != null) {
					    cell3.setCellValue(record.getR14_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell3 = row.createCell(3);
					if (record.getR15_gross_income() != null) {
					    cell3.setCellValue(record.getR15_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell3 = row.createCell(3);
					if (record.getR16_gross_income() != null) {
					    cell3.setCellValue(record.getR16_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell3 = row.createCell(3);
					if (record.getR17_gross_income() != null) {
					    cell3.setCellValue(record.getR17_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell3 = row.createCell(3);
					if (record.getR18_gross_income() != null) {
					    cell3.setCellValue(record.getR18_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell3 = row.createCell(3);
					if (record.getR19_gross_income() != null) {
					    cell3.setCellValue(record.getR19_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell3 = row.createCell(3);
					if (record.getR20_gross_income() != null) {
					    cell3.setCellValue(record.getR20_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell3 = row.createCell(3);
					if (record.getR21_gross_income() != null) {
					    cell3.setCellValue(record.getR21_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell3 = row.createCell(3);
					if (record.getR22_gross_income() != null) {
					    cell3.setCellValue(record.getR22_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell3 = row.createCell(3);
					if (record.getR23_gross_income() != null) {
					    cell3.setCellValue(record.getR23_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell3 = row.createCell(3);
					if (record.getR24_gross_income() != null) {
					    cell3.setCellValue(record.getR24_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell3 = row.createCell(3);
					if (record.getR25_gross_income() != null) {
					    cell3.setCellValue(record.getR25_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell3 = row.createCell(3);
					if (record.getR26_gross_income() != null) {
					    cell3.setCellValue(record.getR26_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell3 = row.createCell(3);
					if (record.getR27_gross_income() != null) {
					    cell3.setCellValue(record.getR27_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell3 = row.createCell(3);
					if (record.getR28_gross_income() != null) {
					    cell3.setCellValue(record.getR28_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell3 = row.createCell(3);
					if (record.getR29_gross_income() != null) {
					    cell3.setCellValue(record.getR29_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell3 = row.createCell(3);
					if (record.getR30_gross_income() != null) {
					    cell3.setCellValue(record.getR30_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell3 = row.createCell(3);
					if (record.getR31_gross_income() != null) {
					    cell3.setCellValue(record.getR31_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell3 = row.createCell(3);
					if (record.getR32_gross_income() != null) {
					    cell3.setCellValue(record.getR32_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell3 = row.createCell(3);
					if (record.getR33_gross_income() != null) {
					    cell3.setCellValue(record.getR33_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell3 = row.createCell(3);
					if (record.getR34_gross_income() != null) {
					    cell3.setCellValue(record.getR34_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell3 = row.createCell(3);
					if (record.getR35_gross_income() != null) {
					    cell3.setCellValue(record.getR35_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell3 = row.createCell(3);
					if (record.getR36_gross_income() != null) {
					    cell3.setCellValue(record.getR36_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell3 = row.createCell(3);
					if (record.getR37_gross_income() != null) {
					    cell3.setCellValue(record.getR37_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell3 = row.createCell(3);
					if (record.getR38_gross_income() != null) {
					    cell3.setCellValue(record.getR38_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell3 = row.createCell(3);
					if (record.getR39_gross_income() != null) {
					    cell3.setCellValue(record.getR39_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell3 = row.createCell(3);
					if (record.getR40_gross_income() != null) {
					    cell3.setCellValue(record.getR40_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell3 = row.createCell(3);
					if (record.getR41_gross_income() != null) {
					    cell3.setCellValue(record.getR41_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell3 = row.createCell(3);
					if (record.getR42_gross_income() != null) {
					    cell3.setCellValue(record.getR42_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell3 = row.createCell(3);
					if (record.getR43_gross_income() != null) {
					    cell3.setCellValue(record.getR43_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell3 = row.createCell(3);
					if (record.getR44_gross_income() != null) {
					    cell3.setCellValue(record.getR44_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell3 = row.createCell(3);
					if (record.getR45_gross_income() != null) {
					    cell3.setCellValue(record.getR45_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell3 = row.createCell(3);
					if (record.getR46_gross_income() != null) {
					    cell3.setCellValue(record.getR46_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell3 = row.createCell(3);
					if (record.getR47_gross_income() != null) {
					    cell3.setCellValue(record.getR47_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell3 = row.createCell(3);
					if (record.getR48_gross_income() != null) {
					    cell3.setCellValue(record.getR48_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(48);
					cell3 = row.createCell(3);
					if (record.getR49_gross_income() != null) {
					    cell3.setCellValue(record.getR49_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(49);
					cell3 = row.createCell(4);
					if (record.getR50_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR50_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(50);
					cell3 = row.createCell(4);
					if (record.getR51_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR51_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(51);
					cell3 = row.createCell(4);
					if (record.getR52_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR52_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(52);
					cell3 = row.createCell(4);
					if (record.getR53_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR53_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(53);
					cell3 = row.createCell(4);
					if (record.getR54_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR54_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(54);
					cell3 = row.createCell(5);
					if (record.getR55_risk_weight_factor() != null) {
					    cell3.setCellValue(record.getR55_risk_weight_factor().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(55);
					cell3 = row.createCell(5);
					if (record.getR56_risk_weight_factor() != null) {
					    cell3.setCellValue(record.getR56_risk_weight_factor().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OR1 SUMMARY", null, "BRRS_M_OR1_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
			}
			}
	}

	public byte[] BRRS_M_OR1EmailExcel(String filename, String reportId, String fromdate, String todate, String currency,
					String dtltype, String type, BigDecimal version) throws Exception {
				
				logger.info("Service: Starting Excel generation process in memory.");
				System.out.println("======= DOWNLOAD DETAILS =======");
				System.out.println("TYPE      : " + type);
				System.out.println("DTLTYPE   : " + dtltype);
				System.out.println("DATE      : " + dateformat.parse(todate));
				System.out.println("VERSION   : " + version);
				System.out.println("==========================");
				
				
			
				List<M_OR1_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M-OR1 report. Returning empty result.");
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
					 
					int startRow = 9;
					
					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_OR1_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber="+startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}


							//row11
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR10_gross_income() != null) {
								cell3.setCellValue(record.getR10_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							
							row = sheet.getRow(10);
							cell3 = row.createCell(2);
							if (record.getR11_gross_income() != null) {
							    cell3.setCellValue(record.getR11_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(11);
							cell3 = row.createCell(2);
							if (record.getR12_gross_income() != null) {
							    cell3.setCellValue(record.getR12_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(12);
							cell3 = row.createCell(2);
							if (record.getR13_gross_income() != null) {
							    cell3.setCellValue(record.getR13_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(13);
							cell3 = row.createCell(2);
							if (record.getR14_gross_income() != null) {
							    cell3.setCellValue(record.getR14_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(14);
							cell3 = row.createCell(2);
							if (record.getR15_gross_income() != null) {
							    cell3.setCellValue(record.getR15_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(15);
							cell3 = row.createCell(2);
							if (record.getR16_gross_income() != null) {
							    cell3.setCellValue(record.getR16_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(16);
							cell3 = row.createCell(2);
							if (record.getR17_gross_income() != null) {
							    cell3.setCellValue(record.getR17_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(17);
							cell3 = row.createCell(2);
							if (record.getR18_gross_income() != null) {
							    cell3.setCellValue(record.getR18_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(18);
							cell3 = row.createCell(2);
							if (record.getR19_gross_income() != null) {
							    cell3.setCellValue(record.getR19_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(19);
							cell3 = row.createCell(2);
							if (record.getR20_gross_income() != null) {
							    cell3.setCellValue(record.getR20_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(20);
							cell3 = row.createCell(2);
							if (record.getR21_gross_income() != null) {
							    cell3.setCellValue(record.getR21_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(21);
							cell3 = row.createCell(2);
							if (record.getR22_gross_income() != null) {
							    cell3.setCellValue(record.getR22_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(22);
							cell3 = row.createCell(2);
							if (record.getR23_gross_income() != null) {
							    cell3.setCellValue(record.getR23_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(23);
							cell3 = row.createCell(2);
							if (record.getR24_gross_income() != null) {
							    cell3.setCellValue(record.getR24_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(24);
							cell3 = row.createCell(2);
							if (record.getR25_gross_income() != null) {
							    cell3.setCellValue(record.getR25_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(25);
							cell3 = row.createCell(2);
							if (record.getR26_gross_income() != null) {
							    cell3.setCellValue(record.getR26_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(26);
							cell3 = row.createCell(2);
							if (record.getR27_gross_income() != null) {
							    cell3.setCellValue(record.getR27_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(27);
							cell3 = row.createCell(2);
							if (record.getR28_gross_income() != null) {
							    cell3.setCellValue(record.getR28_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(28);
							cell3 = row.createCell(2);
							if (record.getR29_gross_income() != null) {
							    cell3.setCellValue(record.getR29_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(29);
							cell3 = row.createCell(2);
							if (record.getR30_gross_income() != null) {
							    cell3.setCellValue(record.getR30_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(30);
							cell3 = row.createCell(2);
							if (record.getR31_gross_income() != null) {
							    cell3.setCellValue(record.getR31_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(31);
							cell3 = row.createCell(2);
							if (record.getR32_gross_income() != null) {
							    cell3.setCellValue(record.getR32_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(32);
							cell3 = row.createCell(2);
							if (record.getR33_gross_income() != null) {
							    cell3.setCellValue(record.getR33_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(33);
							cell3 = row.createCell(2);
							if (record.getR34_gross_income() != null) {
							    cell3.setCellValue(record.getR34_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(34);
							cell3 = row.createCell(2);
							if (record.getR35_gross_income() != null) {
							    cell3.setCellValue(record.getR35_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(35);
							cell3 = row.createCell(2);
							if (record.getR36_gross_income() != null) {
							    cell3.setCellValue(record.getR36_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(36);
							cell3 = row.createCell(2);
							if (record.getR37_gross_income() != null) {
							    cell3.setCellValue(record.getR37_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(37);
							cell3 = row.createCell(2);
							if (record.getR38_gross_income() != null) {
							    cell3.setCellValue(record.getR38_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(38);
							cell3 = row.createCell(2);
							if (record.getR39_gross_income() != null) {
							    cell3.setCellValue(record.getR39_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(39);
							cell3 = row.createCell(2);
							if (record.getR40_gross_income() != null) {
							    cell3.setCellValue(record.getR40_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(40);
							cell3 = row.createCell(2);
							if (record.getR41_gross_income() != null) {
							    cell3.setCellValue(record.getR41_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(41);
							cell3 = row.createCell(2);
							if (record.getR42_gross_income() != null) {
							    cell3.setCellValue(record.getR42_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(42);
							cell3 = row.createCell(2);
							if (record.getR43_gross_income() != null) {
							    cell3.setCellValue(record.getR43_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(43);
							cell3 = row.createCell(2);
							if (record.getR44_gross_income() != null) {
							    cell3.setCellValue(record.getR44_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(44);
							cell3 = row.createCell(2);
							if (record.getR45_gross_income() != null) {
							    cell3.setCellValue(record.getR45_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(45);
							cell3 = row.createCell(2);
							if (record.getR46_gross_income() != null) {
							    cell3.setCellValue(record.getR46_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(46);
							cell3 = row.createCell(2);
							if (record.getR47_gross_income() != null) {
							    cell3.setCellValue(record.getR47_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(47);
							cell3 = row.createCell(2);
							if (record.getR48_gross_income() != null) {
							    cell3.setCellValue(record.getR48_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}
							
							/*
							 * row = sheet.getRow(48); cell3 = row.createCell(2); if
							 * (record.getR49_gross_income() != null) {
							 * cell3.setCellValue(record.getR49_gross_income().doubleValue());
							 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
							 * cell3.setCellStyle(textStyle); }
							 */
							row = sheet.getRow(48);
							cell3 = row.createCell(3);
							if (record.getR50_aggregate_gross_income() != null) {
							    cell3.setCellValue(record.getR50_aggregate_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}
							
							row = sheet.getRow(49);
							cell3 = row.createCell(3);
							if (record.getR51_aggregate_gross_income() != null) {
							    cell3.setCellValue(record.getR51_aggregate_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}
							
							
							row = sheet.getRow(50);
							cell3 = row.createCell(3);
							if (record.getR52_aggregate_gross_income() != null) {
							    cell3.setCellValue(record.getR52_aggregate_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}
							
							
							row = sheet.getRow(51);
							cell3 = row.createCell(3);
							if (record.getR53_aggregate_gross_income() != null) {
							    cell3.setCellValue(record.getR53_aggregate_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}
							
							
							row = sheet.getRow(52);
							cell3 = row.createCell(3);
							if (record.getR54_aggregate_gross_income() != null) {
							    cell3.setCellValue(record.getR54_aggregate_gross_income().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}
							
							row = sheet.getRow(53);
							cell3 = row.createCell(4);
							if (record.getR55_risk_weight_factor() != null) {
							    cell3.setCellValue(record.getR55_risk_weight_factor().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}
							
							row = sheet.getRow(54);
							cell3 = row.createCell(4);
							if (record.getR56_risk_weight_factor() != null) {
							    cell3.setCellValue(record.getR56_risk_weight_factor().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OR1 EMAIL SUMMARY", null, "BRRS_M_OR1_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}

	public byte[] BRRS_M_OR1ArchivalEmailExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		
		logger.info("Service: Starting Excel generation process in memory for EMAIL ARCHIVAL.");
		
		System.out.println("======= DOWNLOAD DETAILS =======");
		System.out.println("TYPE      : " + type);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");
		
		
	
		List<M_OR1_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M-OR1 report. Returning empty result.");
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
			 
			int startRow = 9;
			
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_OR1_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					//row11
					// Column C
					Cell cell3 = row.createCell(2);
					if (record.getR10_gross_income() != null) {
						cell3.setCellValue(record.getR10_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(10);
					cell3 = row.createCell(2);
					if (record.getR11_gross_income() != null) {
					    cell3.setCellValue(record.getR11_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell3 = row.createCell(2);
					if (record.getR12_gross_income() != null) {
					    cell3.setCellValue(record.getR12_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell3 = row.createCell(2);
					if (record.getR13_gross_income() != null) {
					    cell3.setCellValue(record.getR13_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell3 = row.createCell(2);
					if (record.getR14_gross_income() != null) {
					    cell3.setCellValue(record.getR14_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell3 = row.createCell(2);
					if (record.getR15_gross_income() != null) {
					    cell3.setCellValue(record.getR15_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell3 = row.createCell(2);
					if (record.getR16_gross_income() != null) {
					    cell3.setCellValue(record.getR16_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell3 = row.createCell(2);
					if (record.getR17_gross_income() != null) {
					    cell3.setCellValue(record.getR17_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell3 = row.createCell(2);
					if (record.getR18_gross_income() != null) {
					    cell3.setCellValue(record.getR18_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell3 = row.createCell(2);
					if (record.getR19_gross_income() != null) {
					    cell3.setCellValue(record.getR19_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell3 = row.createCell(2);
					if (record.getR20_gross_income() != null) {
					    cell3.setCellValue(record.getR20_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell3 = row.createCell(2);
					if (record.getR21_gross_income() != null) {
					    cell3.setCellValue(record.getR21_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell3 = row.createCell(2);
					if (record.getR22_gross_income() != null) {
					    cell3.setCellValue(record.getR22_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell3 = row.createCell(2);
					if (record.getR23_gross_income() != null) {
					    cell3.setCellValue(record.getR23_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell3 = row.createCell(2);
					if (record.getR24_gross_income() != null) {
					    cell3.setCellValue(record.getR24_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell3 = row.createCell(2);
					if (record.getR25_gross_income() != null) {
					    cell3.setCellValue(record.getR25_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell3 = row.createCell(2);
					if (record.getR26_gross_income() != null) {
					    cell3.setCellValue(record.getR26_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell3 = row.createCell(2);
					if (record.getR27_gross_income() != null) {
					    cell3.setCellValue(record.getR27_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell3 = row.createCell(2);
					if (record.getR28_gross_income() != null) {
					    cell3.setCellValue(record.getR28_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell3 = row.createCell(2);
					if (record.getR29_gross_income() != null) {
					    cell3.setCellValue(record.getR29_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell3 = row.createCell(2);
					if (record.getR30_gross_income() != null) {
					    cell3.setCellValue(record.getR30_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell3 = row.createCell(2);
					if (record.getR31_gross_income() != null) {
					    cell3.setCellValue(record.getR31_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell3 = row.createCell(2);
					if (record.getR32_gross_income() != null) {
					    cell3.setCellValue(record.getR32_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell3 = row.createCell(2);
					if (record.getR33_gross_income() != null) {
					    cell3.setCellValue(record.getR33_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell3 = row.createCell(2);
					if (record.getR34_gross_income() != null) {
					    cell3.setCellValue(record.getR34_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell3 = row.createCell(2);
					if (record.getR35_gross_income() != null) {
					    cell3.setCellValue(record.getR35_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell3 = row.createCell(2);
					if (record.getR36_gross_income() != null) {
					    cell3.setCellValue(record.getR36_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell3 = row.createCell(2);
					if (record.getR37_gross_income() != null) {
					    cell3.setCellValue(record.getR37_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell3 = row.createCell(2);
					if (record.getR38_gross_income() != null) {
					    cell3.setCellValue(record.getR38_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell3 = row.createCell(2);
					if (record.getR39_gross_income() != null) {
					    cell3.setCellValue(record.getR39_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell3 = row.createCell(2);
					if (record.getR40_gross_income() != null) {
					    cell3.setCellValue(record.getR40_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell3 = row.createCell(2);
					if (record.getR41_gross_income() != null) {
					    cell3.setCellValue(record.getR41_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell3 = row.createCell(2);
					if (record.getR42_gross_income() != null) {
					    cell3.setCellValue(record.getR42_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell3 = row.createCell(2);
					if (record.getR43_gross_income() != null) {
					    cell3.setCellValue(record.getR43_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell3 = row.createCell(2);
					if (record.getR44_gross_income() != null) {
					    cell3.setCellValue(record.getR44_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell3 = row.createCell(2);
					if (record.getR45_gross_income() != null) {
					    cell3.setCellValue(record.getR45_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell3 = row.createCell(2);
					if (record.getR46_gross_income() != null) {
					    cell3.setCellValue(record.getR46_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell3 = row.createCell(2);
					if (record.getR47_gross_income() != null) {
					    cell3.setCellValue(record.getR47_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell3 = row.createCell(2);
					if (record.getR48_gross_income() != null) {
					    cell3.setCellValue(record.getR48_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					/*
					 * row = sheet.getRow(48); cell3 = row.createCell(2); if
					 * (record.getR49_gross_income() != null) {
					 * cell3.setCellValue(record.getR49_gross_income().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); }
					 */
					row = sheet.getRow(48);
					cell3 = row.createCell(3);
					if (record.getR50_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR50_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(49);
					cell3 = row.createCell(3);
					if (record.getR51_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR51_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(50);
					cell3 = row.createCell(3);
					if (record.getR52_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR52_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(51);
					cell3 = row.createCell(3);
					if (record.getR53_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR53_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(52);
					cell3 = row.createCell(3);
					if (record.getR54_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR54_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(53);
					cell3 = row.createCell(4);
					if (record.getR55_risk_weight_factor() != null) {
					    cell3.setCellValue(record.getR55_risk_weight_factor().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(54);
					cell3 = row.createCell(4);
					if (record.getR56_risk_weight_factor() != null) {
					    cell3.setCellValue(record.getR56_risk_weight_factor().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OR1 EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_OR1_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	public byte[] BRRS_M_OR1DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_OR1 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_OR1Details");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);
// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT_LABLE",
					"REPORT_ADDL_CRITERIA_1", "REPORT_DATE" };
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
			List<M_OR1_Detail_Entity> reportData = getDetailByDate(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_OR1_Detail_Entity item : reportData) {
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
					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
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
				logger.info("No data found for BRRS_M_OR1 — only header will be written.");
			}
// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_OR1 Excel", e);
			return new byte[0];
		}
	}

	public List<Object[]> getM_OR1Archival() {
		
		
		List<Object[]> archivalList = new ArrayList<>();
		try {
			List<M_OR1_Archival_Summary_Entity> latestArchivalList = getArchivalSummaryWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_OR1_Archival_Summary_Entity entity : latestArchivalList) {
					archivalList.add(new Object[] { 
							entity.getReport_date(), 
							entity.getReport_version(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + archivalList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_OR1 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return archivalList;
	}

	public byte[] getExcelM_OR1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
		
		
		System.out.println("======= DOWNLOAD DETAILS in ARCHIVAL =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");
		
		
		
			
		
			logger.info("Service: Starting Excel generation process in memory fOR FORMAT ARCHIVAL.");
			
		List<M_OR1_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M-OR1 report. Returning empty result.");
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
			 
			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_OR1_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column C
					Cell cell3 = row.createCell(3);
					if (record.getR10_gross_income() != null) {
						cell3.setCellValue(record.getR10_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);
					cell3 = row.createCell(3);
					if (record.getR11_gross_income() != null) {
						cell3.setCellValue(record.getR11_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell3 = row.createCell(3);
					if (record.getR12_gross_income() != null) {
						cell3.setCellValue(record.getR12_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell3 = row.createCell(3);
					if (record.getR13_gross_income() != null) {
						cell3.setCellValue(record.getR13_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell3 = row.createCell(3);
					if (record.getR14_gross_income() != null) {
						cell3.setCellValue(record.getR14_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell3 = row.createCell(3);
					if (record.getR15_gross_income() != null) {
						cell3.setCellValue(record.getR15_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell3 = row.createCell(3);
					if (record.getR16_gross_income() != null) {
						cell3.setCellValue(record.getR16_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell3 = row.createCell(3);
					if (record.getR17_gross_income() != null) {
						cell3.setCellValue(record.getR17_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell3 = row.createCell(3);
					if (record.getR18_gross_income() != null) {
						cell3.setCellValue(record.getR18_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell3 = row.createCell(3);
					if (record.getR19_gross_income() != null) {
						cell3.setCellValue(record.getR19_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell3 = row.createCell(3);
					if (record.getR20_gross_income() != null) {
						cell3.setCellValue(record.getR20_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell3 = row.createCell(3);
					if (record.getR21_gross_income() != null) {
						cell3.setCellValue(record.getR21_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell3 = row.createCell(3);
					if (record.getR22_gross_income() != null) {
						cell3.setCellValue(record.getR22_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell3 = row.createCell(3);
					if (record.getR23_gross_income() != null) {
						cell3.setCellValue(record.getR23_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell3 = row.createCell(3);
					if (record.getR24_gross_income() != null) {
						cell3.setCellValue(record.getR24_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell3 = row.createCell(3);
					if (record.getR25_gross_income() != null) {
						cell3.setCellValue(record.getR25_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell3 = row.createCell(3);
					if (record.getR26_gross_income() != null) {
						cell3.setCellValue(record.getR26_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell3 = row.createCell(3);
					if (record.getR27_gross_income() != null) {
						cell3.setCellValue(record.getR27_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell3 = row.createCell(3);
					if (record.getR28_gross_income() != null) {
						cell3.setCellValue(record.getR28_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell3 = row.createCell(3);
					if (record.getR29_gross_income() != null) {
						cell3.setCellValue(record.getR29_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell3 = row.createCell(3);
					if (record.getR30_gross_income() != null) {
						cell3.setCellValue(record.getR30_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell3 = row.createCell(3);
					if (record.getR31_gross_income() != null) {
						cell3.setCellValue(record.getR31_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell3 = row.createCell(3);
					if (record.getR32_gross_income() != null) {
						cell3.setCellValue(record.getR32_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell3 = row.createCell(3);
					if (record.getR33_gross_income() != null) {
						cell3.setCellValue(record.getR33_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell3 = row.createCell(3);
					if (record.getR34_gross_income() != null) {
						cell3.setCellValue(record.getR34_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell3 = row.createCell(3);
					if (record.getR35_gross_income() != null) {
						cell3.setCellValue(record.getR35_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell3 = row.createCell(3);
					if (record.getR36_gross_income() != null) {
						cell3.setCellValue(record.getR36_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell3 = row.createCell(3);
					if (record.getR37_gross_income() != null) {
						cell3.setCellValue(record.getR37_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell3 = row.createCell(3);
					if (record.getR38_gross_income() != null) {
						cell3.setCellValue(record.getR38_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell3 = row.createCell(3);
					if (record.getR39_gross_income() != null) {
						cell3.setCellValue(record.getR39_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell3 = row.createCell(3);
					if (record.getR40_gross_income() != null) {
						cell3.setCellValue(record.getR40_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell3 = row.createCell(3);
					if (record.getR41_gross_income() != null) {
						cell3.setCellValue(record.getR41_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell3 = row.createCell(3);
					if (record.getR42_gross_income() != null) {
						cell3.setCellValue(record.getR42_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell3 = row.createCell(3);
					if (record.getR43_gross_income() != null) {
						cell3.setCellValue(record.getR43_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell3 = row.createCell(3);
					if (record.getR44_gross_income() != null) {
						cell3.setCellValue(record.getR44_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell3 = row.createCell(3);
					if (record.getR45_gross_income() != null) {
						cell3.setCellValue(record.getR45_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell3 = row.createCell(3);
					if (record.getR46_gross_income() != null) {
						cell3.setCellValue(record.getR46_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell3 = row.createCell(3);
					if (record.getR47_gross_income() != null) {
						cell3.setCellValue(record.getR47_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell3 = row.createCell(3);
					if (record.getR48_gross_income() != null) {
						cell3.setCellValue(record.getR48_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(48);
					cell3 = row.createCell(3);
					if (record.getR49_gross_income() != null) {
						cell3.setCellValue(record.getR49_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(49);
					cell3 = row.createCell(4);
					if (record.getR50_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR50_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell3 = row.createCell(4);
					if (record.getR51_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR51_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(51);
					cell3 = row.createCell(4);
					if (record.getR52_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR52_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(52);
					cell3 = row.createCell(4);
					if (record.getR53_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR53_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell3 = row.createCell(4);
					if (record.getR54_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR54_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell3 = row.createCell(5);
					if (record.getR55_risk_weight_factor() != null) {
						cell3.setCellValue(record.getR55_risk_weight_factor().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell3 = row.createCell(5);
					if (record.getR56_risk_weight_factor() != null) {
						cell3.setCellValue(record.getR56_risk_weight_factor().doubleValue());
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OR1 ARCHIVAL SUMMARY", null, "BRRS_M_OR1_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_OR1 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MOR2Detail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT_LABLE",
					"REPORT_ADDL_CRITERIA_1", "REPORT_DATE" };

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
			List<M_OR1_Archival_Detail_Entity> reportData = getArchivalDetailByDateAndVersion(parsedToDate,
					version);
			System.out.println("Size");
			System.out.println(reportData.size());
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_OR1_Archival_Detail_Entity item : reportData) {
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

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
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
				logger.info("No data found for BRRS_M-OR1 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_OR1Excel", e);
			return new byte[0];
		}
	}

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_OR1"); // ✅ match the report name
		System.out.println("Hello");
		if (acctNo != null) {
			M_OR1_Detail_Entity la1Entity = getDetailByAcctNumber(acctNo);
			if (la1Entity != null && la1Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", la1Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_OR1"); // ✅ match the report name

		if (acctNo != null) {
			M_OR1_Detail_Entity la1Entity = getDetailByAcctNumber(acctNo);
			if (la1Entity != null && la1Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
				System.out.println(formattedDate);
			}
			mv.addObject("Data", la1Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInpula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_OR1_Detail_Entity existing = getDetailByAcctNumber(acctNo);
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

			if (provisionStr != null && !provisionStr.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr);
				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newProvision) != 0) {
					existing.setAcctBalanceInpula(newProvision);
					isChanged = true;
					logger.info("Balance updated to {}", newProvision);
				}
			}

			if (isChanged) {
				updateDetailRecord(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_OR1_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_OR1_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating M_OR1 record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	class M_OR1SummaryRowMapper implements RowMapper<M_OR1_Summary_Entity> {
		@Override
		public M_OR1_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_OR1_Summary_Entity obj = new M_OR1_Summary_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getString("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR10_product(rs.getString("R10_PRODUCT")); obj.setR10_month(rs.getString("R10_MONTH")); obj.setR10_gross_income(rs.getBigDecimal("R10_GROSS_INCOME"));
			obj.setR10_aggregate_gross_income(rs.getBigDecimal("R10_AGGREGATE_GROSS_INCOME")); obj.setR10_risk_weight_factor(rs.getBigDecimal("R10_RISK_WEIGHT_FACTOR"));
			obj.setR11_product(rs.getString("R11_PRODUCT")); obj.setR11_month(rs.getString("R11_MONTH")); obj.setR11_gross_income(rs.getBigDecimal("R11_GROSS_INCOME"));
			obj.setR11_aggregate_gross_income(rs.getBigDecimal("R11_AGGREGATE_GROSS_INCOME")); obj.setR11_risk_weight_factor(rs.getBigDecimal("R11_RISK_WEIGHT_FACTOR"));
			obj.setR12_product(rs.getString("R12_PRODUCT")); obj.setR12_month(rs.getString("R12_MONTH")); obj.setR12_gross_income(rs.getBigDecimal("R12_GROSS_INCOME"));
			obj.setR12_aggregate_gross_income(rs.getBigDecimal("R12_AGGREGATE_GROSS_INCOME")); obj.setR12_risk_weight_factor(rs.getBigDecimal("R12_RISK_WEIGHT_FACTOR"));
			obj.setR13_product(rs.getString("R13_PRODUCT")); obj.setR13_month(rs.getString("R13_MONTH")); obj.setR13_gross_income(rs.getBigDecimal("R13_GROSS_INCOME"));
			obj.setR13_aggregate_gross_income(rs.getBigDecimal("R13_AGGREGATE_GROSS_INCOME")); obj.setR13_risk_weight_factor(rs.getBigDecimal("R13_RISK_WEIGHT_FACTOR"));
			obj.setR14_product(rs.getString("R14_PRODUCT")); obj.setR14_month(rs.getString("R14_MONTH")); obj.setR14_gross_income(rs.getBigDecimal("R14_GROSS_INCOME"));
			obj.setR14_aggregate_gross_income(rs.getBigDecimal("R14_AGGREGATE_GROSS_INCOME")); obj.setR14_risk_weight_factor(rs.getBigDecimal("R14_RISK_WEIGHT_FACTOR"));
			obj.setR15_product(rs.getString("R15_PRODUCT")); obj.setR15_month(rs.getString("R15_MONTH")); obj.setR15_gross_income(rs.getBigDecimal("R15_GROSS_INCOME"));
			obj.setR15_aggregate_gross_income(rs.getBigDecimal("R15_AGGREGATE_GROSS_INCOME")); obj.setR15_risk_weight_factor(rs.getBigDecimal("R15_RISK_WEIGHT_FACTOR"));
			obj.setR16_product(rs.getString("R16_PRODUCT")); obj.setR16_month(rs.getString("R16_MONTH")); obj.setR16_gross_income(rs.getBigDecimal("R16_GROSS_INCOME"));
			obj.setR16_aggregate_gross_income(rs.getBigDecimal("R16_AGGREGATE_GROSS_INCOME")); obj.setR16_risk_weight_factor(rs.getBigDecimal("R16_RISK_WEIGHT_FACTOR"));
			obj.setR17_product(rs.getString("R17_PRODUCT")); obj.setR17_month(rs.getString("R17_MONTH")); obj.setR17_gross_income(rs.getBigDecimal("R17_GROSS_INCOME"));
			obj.setR17_aggregate_gross_income(rs.getBigDecimal("R17_AGGREGATE_GROSS_INCOME")); obj.setR17_risk_weight_factor(rs.getBigDecimal("R17_RISK_WEIGHT_FACTOR"));
			obj.setR18_product(rs.getString("R18_PRODUCT")); obj.setR18_month(rs.getString("R18_MONTH")); obj.setR18_gross_income(rs.getBigDecimal("R18_GROSS_INCOME"));
			obj.setR18_aggregate_gross_income(rs.getBigDecimal("R18_AGGREGATE_GROSS_INCOME")); obj.setR18_risk_weight_factor(rs.getBigDecimal("R18_RISK_WEIGHT_FACTOR"));
			obj.setR19_product(rs.getString("R19_PRODUCT")); obj.setR19_month(rs.getString("R19_MONTH")); obj.setR19_gross_income(rs.getBigDecimal("R19_GROSS_INCOME"));
			obj.setR19_aggregate_gross_income(rs.getBigDecimal("R19_AGGREGATE_GROSS_INCOME")); obj.setR19_risk_weight_factor(rs.getBigDecimal("R19_RISK_WEIGHT_FACTOR"));
			obj.setR20_product(rs.getString("R20_PRODUCT")); obj.setR20_month(rs.getString("R20_MONTH")); obj.setR20_gross_income(rs.getBigDecimal("R20_GROSS_INCOME"));
			obj.setR20_aggregate_gross_income(rs.getBigDecimal("R20_AGGREGATE_GROSS_INCOME")); obj.setR20_risk_weight_factor(rs.getBigDecimal("R20_RISK_WEIGHT_FACTOR"));
			obj.setR21_product(rs.getString("R21_PRODUCT")); obj.setR21_month(rs.getString("R21_MONTH")); obj.setR21_gross_income(rs.getBigDecimal("R21_GROSS_INCOME"));
			obj.setR21_aggregate_gross_income(rs.getBigDecimal("R21_AGGREGATE_GROSS_INCOME")); obj.setR21_risk_weight_factor(rs.getBigDecimal("R21_RISK_WEIGHT_FACTOR"));
			obj.setR22_product(rs.getString("R22_PRODUCT")); obj.setR22_month(rs.getString("R22_MONTH")); obj.setR22_gross_income(rs.getBigDecimal("R22_GROSS_INCOME"));
			obj.setR22_aggregate_gross_income(rs.getBigDecimal("R22_AGGREGATE_GROSS_INCOME")); obj.setR22_risk_weight_factor(rs.getBigDecimal("R22_RISK_WEIGHT_FACTOR"));
			obj.setR23_product(rs.getString("R23_PRODUCT")); obj.setR23_month(rs.getString("R23_MONTH")); obj.setR23_gross_income(rs.getBigDecimal("R23_GROSS_INCOME"));
			obj.setR23_aggregate_gross_income(rs.getBigDecimal("R23_AGGREGATE_GROSS_INCOME")); obj.setR23_risk_weight_factor(rs.getBigDecimal("R23_RISK_WEIGHT_FACTOR"));
			obj.setR24_product(rs.getString("R24_PRODUCT")); obj.setR24_month(rs.getString("R24_MONTH")); obj.setR24_gross_income(rs.getBigDecimal("R24_GROSS_INCOME"));
			obj.setR24_aggregate_gross_income(rs.getBigDecimal("R24_AGGREGATE_GROSS_INCOME")); obj.setR24_risk_weight_factor(rs.getBigDecimal("R24_RISK_WEIGHT_FACTOR"));
			obj.setR25_product(rs.getString("R25_PRODUCT")); obj.setR25_month(rs.getString("R25_MONTH")); obj.setR25_gross_income(rs.getBigDecimal("R25_GROSS_INCOME"));
			obj.setR25_aggregate_gross_income(rs.getBigDecimal("R25_AGGREGATE_GROSS_INCOME")); obj.setR25_risk_weight_factor(rs.getBigDecimal("R25_RISK_WEIGHT_FACTOR"));
			obj.setR26_product(rs.getString("R26_PRODUCT")); obj.setR26_month(rs.getString("R26_MONTH")); obj.setR26_gross_income(rs.getBigDecimal("R26_GROSS_INCOME"));
			obj.setR26_aggregate_gross_income(rs.getBigDecimal("R26_AGGREGATE_GROSS_INCOME")); obj.setR26_risk_weight_factor(rs.getBigDecimal("R26_RISK_WEIGHT_FACTOR"));
			obj.setR27_product(rs.getString("R27_PRODUCT")); obj.setR27_month(rs.getString("R27_MONTH")); obj.setR27_gross_income(rs.getBigDecimal("R27_GROSS_INCOME"));
			obj.setR27_aggregate_gross_income(rs.getBigDecimal("R27_AGGREGATE_GROSS_INCOME")); obj.setR27_risk_weight_factor(rs.getBigDecimal("R27_RISK_WEIGHT_FACTOR"));
			obj.setR28_product(rs.getString("R28_PRODUCT")); obj.setR28_month(rs.getString("R28_MONTH")); obj.setR28_gross_income(rs.getBigDecimal("R28_GROSS_INCOME"));
			obj.setR28_aggregate_gross_income(rs.getBigDecimal("R28_AGGREGATE_GROSS_INCOME")); obj.setR28_risk_weight_factor(rs.getBigDecimal("R28_RISK_WEIGHT_FACTOR"));
			obj.setR29_product(rs.getString("R29_PRODUCT")); obj.setR29_month(rs.getString("R29_MONTH")); obj.setR29_gross_income(rs.getBigDecimal("R29_GROSS_INCOME"));
			obj.setR29_aggregate_gross_income(rs.getBigDecimal("R29_AGGREGATE_GROSS_INCOME")); obj.setR29_risk_weight_factor(rs.getBigDecimal("R29_RISK_WEIGHT_FACTOR"));
			obj.setR30_product(rs.getString("R30_PRODUCT")); obj.setR30_month(rs.getString("R30_MONTH")); obj.setR30_gross_income(rs.getBigDecimal("R30_GROSS_INCOME"));
			obj.setR30_aggregate_gross_income(rs.getBigDecimal("R30_AGGREGATE_GROSS_INCOME")); obj.setR30_risk_weight_factor(rs.getBigDecimal("R30_RISK_WEIGHT_FACTOR"));
			obj.setR31_product(rs.getString("R31_PRODUCT")); obj.setR31_month(rs.getString("R31_MONTH")); obj.setR31_gross_income(rs.getBigDecimal("R31_GROSS_INCOME"));
			obj.setR31_aggregate_gross_income(rs.getBigDecimal("R31_AGGREGATE_GROSS_INCOME")); obj.setR31_risk_weight_factor(rs.getBigDecimal("R31_RISK_WEIGHT_FACTOR"));
			obj.setR32_product(rs.getString("R32_PRODUCT")); obj.setR32_month(rs.getString("R32_MONTH")); obj.setR32_gross_income(rs.getBigDecimal("R32_GROSS_INCOME"));
			obj.setR32_aggregate_gross_income(rs.getBigDecimal("R32_AGGREGATE_GROSS_INCOME")); obj.setR32_risk_weight_factor(rs.getBigDecimal("R32_RISK_WEIGHT_FACTOR"));
			obj.setR33_product(rs.getString("R33_PRODUCT")); obj.setR33_month(rs.getString("R33_MONTH")); obj.setR33_gross_income(rs.getBigDecimal("R33_GROSS_INCOME"));
			obj.setR33_aggregate_gross_income(rs.getBigDecimal("R33_AGGREGATE_GROSS_INCOME")); obj.setR33_risk_weight_factor(rs.getBigDecimal("R33_RISK_WEIGHT_FACTOR"));
			obj.setR34_product(rs.getString("R34_PRODUCT")); obj.setR34_month(rs.getString("R34_MONTH")); obj.setR34_gross_income(rs.getBigDecimal("R34_GROSS_INCOME"));
			obj.setR34_aggregate_gross_income(rs.getBigDecimal("R34_AGGREGATE_GROSS_INCOME")); obj.setR34_risk_weight_factor(rs.getBigDecimal("R34_RISK_WEIGHT_FACTOR"));
			obj.setR35_product(rs.getString("R35_PRODUCT")); obj.setR35_month(rs.getString("R35_MONTH")); obj.setR35_gross_income(rs.getBigDecimal("R35_GROSS_INCOME"));
			obj.setR35_aggregate_gross_income(rs.getBigDecimal("R35_AGGREGATE_GROSS_INCOME")); obj.setR35_risk_weight_factor(rs.getBigDecimal("R35_RISK_WEIGHT_FACTOR"));
			obj.setR36_product(rs.getString("R36_PRODUCT")); obj.setR36_month(rs.getString("R36_MONTH")); obj.setR36_gross_income(rs.getBigDecimal("R36_GROSS_INCOME"));
			obj.setR36_aggregate_gross_income(rs.getBigDecimal("R36_AGGREGATE_GROSS_INCOME")); obj.setR36_risk_weight_factor(rs.getBigDecimal("R36_RISK_WEIGHT_FACTOR"));
			obj.setR37_product(rs.getString("R37_PRODUCT")); obj.setR37_month(rs.getString("R37_MONTH")); obj.setR37_gross_income(rs.getBigDecimal("R37_GROSS_INCOME"));
			obj.setR37_aggregate_gross_income(rs.getBigDecimal("R37_AGGREGATE_GROSS_INCOME")); obj.setR37_risk_weight_factor(rs.getBigDecimal("R37_RISK_WEIGHT_FACTOR"));
			obj.setR38_product(rs.getString("R38_PRODUCT")); obj.setR38_month(rs.getString("R38_MONTH")); obj.setR38_gross_income(rs.getBigDecimal("R38_GROSS_INCOME"));
			obj.setR38_aggregate_gross_income(rs.getBigDecimal("R38_AGGREGATE_GROSS_INCOME")); obj.setR38_risk_weight_factor(rs.getBigDecimal("R38_RISK_WEIGHT_FACTOR"));
			obj.setR39_product(rs.getString("R39_PRODUCT")); obj.setR39_month(rs.getString("R39_MONTH")); obj.setR39_gross_income(rs.getBigDecimal("R39_GROSS_INCOME"));
			obj.setR39_aggregate_gross_income(rs.getBigDecimal("R39_AGGREGATE_GROSS_INCOME")); obj.setR39_risk_weight_factor(rs.getBigDecimal("R39_RISK_WEIGHT_FACTOR"));
			obj.setR40_product(rs.getString("R40_PRODUCT")); obj.setR40_month(rs.getString("R40_MONTH")); obj.setR40_gross_income(rs.getBigDecimal("R40_GROSS_INCOME"));
			obj.setR40_aggregate_gross_income(rs.getBigDecimal("R40_AGGREGATE_GROSS_INCOME")); obj.setR40_risk_weight_factor(rs.getBigDecimal("R40_RISK_WEIGHT_FACTOR"));
			obj.setR41_product(rs.getString("R41_PRODUCT")); obj.setR41_month(rs.getString("R41_MONTH")); obj.setR41_gross_income(rs.getBigDecimal("R41_GROSS_INCOME"));
			obj.setR41_aggregate_gross_income(rs.getBigDecimal("R41_AGGREGATE_GROSS_INCOME")); obj.setR41_risk_weight_factor(rs.getBigDecimal("R41_RISK_WEIGHT_FACTOR"));
			obj.setR42_product(rs.getString("R42_PRODUCT")); obj.setR42_month(rs.getString("R42_MONTH")); obj.setR42_gross_income(rs.getBigDecimal("R42_GROSS_INCOME"));
			obj.setR42_aggregate_gross_income(rs.getBigDecimal("R42_AGGREGATE_GROSS_INCOME")); obj.setR42_risk_weight_factor(rs.getBigDecimal("R42_RISK_WEIGHT_FACTOR"));
			obj.setR43_product(rs.getString("R43_PRODUCT")); obj.setR43_month(rs.getString("R43_MONTH")); obj.setR43_gross_income(rs.getBigDecimal("R43_GROSS_INCOME"));
			obj.setR43_aggregate_gross_income(rs.getBigDecimal("R43_AGGREGATE_GROSS_INCOME")); obj.setR43_risk_weight_factor(rs.getBigDecimal("R43_RISK_WEIGHT_FACTOR"));
			obj.setR44_product(rs.getString("R44_PRODUCT")); obj.setR44_month(rs.getString("R44_MONTH")); obj.setR44_gross_income(rs.getBigDecimal("R44_GROSS_INCOME"));
			obj.setR44_aggregate_gross_income(rs.getBigDecimal("R44_AGGREGATE_GROSS_INCOME")); obj.setR44_risk_weight_factor(rs.getBigDecimal("R44_RISK_WEIGHT_FACTOR"));
			obj.setR45_product(rs.getString("R45_PRODUCT")); obj.setR45_month(rs.getString("R45_MONTH")); obj.setR45_gross_income(rs.getBigDecimal("R45_GROSS_INCOME"));
			obj.setR45_aggregate_gross_income(rs.getBigDecimal("R45_AGGREGATE_GROSS_INCOME")); obj.setR45_risk_weight_factor(rs.getBigDecimal("R45_RISK_WEIGHT_FACTOR"));
			obj.setR46_product(rs.getString("R46_PRODUCT")); obj.setR46_month(rs.getString("R46_MONTH")); obj.setR46_gross_income(rs.getBigDecimal("R46_GROSS_INCOME"));
			obj.setR46_aggregate_gross_income(rs.getBigDecimal("R46_AGGREGATE_GROSS_INCOME")); obj.setR46_risk_weight_factor(rs.getBigDecimal("R46_RISK_WEIGHT_FACTOR"));
			obj.setR47_product(rs.getString("R47_PRODUCT")); obj.setR47_month(rs.getString("R47_MONTH")); obj.setR47_gross_income(rs.getBigDecimal("R47_GROSS_INCOME"));
			obj.setR47_aggregate_gross_income(rs.getBigDecimal("R47_AGGREGATE_GROSS_INCOME")); obj.setR47_risk_weight_factor(rs.getBigDecimal("R47_RISK_WEIGHT_FACTOR"));
			obj.setR48_product(rs.getString("R48_PRODUCT")); obj.setR48_month(rs.getString("R48_MONTH")); obj.setR48_gross_income(rs.getBigDecimal("R48_GROSS_INCOME"));
			obj.setR48_aggregate_gross_income(rs.getBigDecimal("R48_AGGREGATE_GROSS_INCOME")); obj.setR48_risk_weight_factor(rs.getBigDecimal("R48_RISK_WEIGHT_FACTOR"));
			obj.setR49_product(rs.getString("R49_PRODUCT")); obj.setR49_month(rs.getString("R49_MONTH")); obj.setR49_gross_income(rs.getBigDecimal("R49_GROSS_INCOME"));
			obj.setR49_aggregate_gross_income(rs.getBigDecimal("R49_AGGREGATE_GROSS_INCOME")); obj.setR49_risk_weight_factor(rs.getBigDecimal("R49_RISK_WEIGHT_FACTOR"));
			obj.setR50_product(rs.getString("R50_PRODUCT")); obj.setR50_month(rs.getString("R50_MONTH")); obj.setR50_gross_income(rs.getBigDecimal("R50_GROSS_INCOME"));
			obj.setR50_aggregate_gross_income(rs.getBigDecimal("R50_AGGREGATE_GROSS_INCOME")); obj.setR50_risk_weight_factor(rs.getBigDecimal("R50_RISK_WEIGHT_FACTOR"));
			obj.setR51_product(rs.getString("R51_PRODUCT")); obj.setR51_month(rs.getString("R51_MONTH")); obj.setR51_gross_income(rs.getBigDecimal("R51_GROSS_INCOME"));
			obj.setR51_aggregate_gross_income(rs.getBigDecimal("R51_AGGREGATE_GROSS_INCOME")); obj.setR51_risk_weight_factor(rs.getBigDecimal("R51_RISK_WEIGHT_FACTOR"));
			obj.setR52_product(rs.getString("R52_PRODUCT")); obj.setR52_month(rs.getString("R52_MONTH")); obj.setR52_gross_income(rs.getBigDecimal("R52_GROSS_INCOME"));
			obj.setR52_aggregate_gross_income(rs.getBigDecimal("R52_AGGREGATE_GROSS_INCOME")); obj.setR52_risk_weight_factor(rs.getBigDecimal("R52_RISK_WEIGHT_FACTOR"));
			obj.setR53_product(rs.getString("R53_PRODUCT")); obj.setR53_month(rs.getString("R53_MONTH")); obj.setR53_gross_income(rs.getBigDecimal("R53_GROSS_INCOME"));
			obj.setR53_aggregate_gross_income(rs.getBigDecimal("R53_AGGREGATE_GROSS_INCOME")); obj.setR53_risk_weight_factor(rs.getBigDecimal("R53_RISK_WEIGHT_FACTOR"));
			obj.setR54_product(rs.getString("R54_PRODUCT")); obj.setR54_month(rs.getString("R54_MONTH")); obj.setR54_gross_income(rs.getBigDecimal("R54_GROSS_INCOME"));
			obj.setR54_aggregate_gross_income(rs.getBigDecimal("R54_AGGREGATE_GROSS_INCOME")); obj.setR54_risk_weight_factor(rs.getBigDecimal("R54_RISK_WEIGHT_FACTOR"));
			obj.setR55_product(rs.getString("R55_PRODUCT")); obj.setR55_month(rs.getString("R55_MONTH")); obj.setR55_gross_income(rs.getBigDecimal("R55_GROSS_INCOME"));
			obj.setR55_aggregate_gross_income(rs.getBigDecimal("R55_AGGREGATE_GROSS_INCOME")); obj.setR55_risk_weight_factor(rs.getBigDecimal("R55_RISK_WEIGHT_FACTOR"));
			obj.setR56_product(rs.getString("R56_PRODUCT")); obj.setR56_month(rs.getString("R56_MONTH")); obj.setR56_gross_income(rs.getBigDecimal("R56_GROSS_INCOME"));
			obj.setR56_aggregate_gross_income(rs.getBigDecimal("R56_AGGREGATE_GROSS_INCOME")); obj.setR56_risk_weight_factor(rs.getBigDecimal("R56_RISK_WEIGHT_FACTOR"));
			return obj;
		}
	}

	class M_OR1DetailRowMapper implements RowMapper<M_OR1_Detail_Entity> {
		@Override
		public M_OR1_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_OR1_Detail_Entity obj = new M_OR1_Detail_Entity();
			obj.setSno(rs.getString("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getTimestamp("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getTimestamp("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getTimestamp("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	class M_OR1ArchivalSummaryRowMapper implements RowMapper<M_OR1_Archival_Summary_Entity> {
		@Override
		public M_OR1_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_OR1_Archival_Summary_Entity obj = new M_OR1_Archival_Summary_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setReportResubDate(rs.getTimestamp("REPORT_RESUBDATE"));
			obj.setR10_product(rs.getString("R10_PRODUCT")); obj.setR10_month(rs.getString("R10_MONTH")); obj.setR10_gross_income(rs.getBigDecimal("R10_GROSS_INCOME"));
			obj.setR10_aggregate_gross_income(rs.getBigDecimal("R10_AGGREGATE_GROSS_INCOME")); obj.setR10_risk_weight_factor(rs.getBigDecimal("R10_RISK_WEIGHT_FACTOR"));
			obj.setR11_product(rs.getString("R11_PRODUCT")); obj.setR11_month(rs.getString("R11_MONTH")); obj.setR11_gross_income(rs.getBigDecimal("R11_GROSS_INCOME"));
			obj.setR11_aggregate_gross_income(rs.getBigDecimal("R11_AGGREGATE_GROSS_INCOME")); obj.setR11_risk_weight_factor(rs.getBigDecimal("R11_RISK_WEIGHT_FACTOR"));
			obj.setR12_product(rs.getString("R12_PRODUCT")); obj.setR12_month(rs.getString("R12_MONTH")); obj.setR12_gross_income(rs.getBigDecimal("R12_GROSS_INCOME"));
			obj.setR12_aggregate_gross_income(rs.getBigDecimal("R12_AGGREGATE_GROSS_INCOME")); obj.setR12_risk_weight_factor(rs.getBigDecimal("R12_RISK_WEIGHT_FACTOR"));
			obj.setR13_product(rs.getString("R13_PRODUCT")); obj.setR13_month(rs.getString("R13_MONTH")); obj.setR13_gross_income(rs.getBigDecimal("R13_GROSS_INCOME"));
			obj.setR13_aggregate_gross_income(rs.getBigDecimal("R13_AGGREGATE_GROSS_INCOME")); obj.setR13_risk_weight_factor(rs.getBigDecimal("R13_RISK_WEIGHT_FACTOR"));
			obj.setR14_product(rs.getString("R14_PRODUCT")); obj.setR14_month(rs.getString("R14_MONTH")); obj.setR14_gross_income(rs.getBigDecimal("R14_GROSS_INCOME"));
			obj.setR14_aggregate_gross_income(rs.getBigDecimal("R14_AGGREGATE_GROSS_INCOME")); obj.setR14_risk_weight_factor(rs.getBigDecimal("R14_RISK_WEIGHT_FACTOR"));
			obj.setR15_product(rs.getString("R15_PRODUCT")); obj.setR15_month(rs.getString("R15_MONTH")); obj.setR15_gross_income(rs.getBigDecimal("R15_GROSS_INCOME"));
			obj.setR15_aggregate_gross_income(rs.getBigDecimal("R15_AGGREGATE_GROSS_INCOME")); obj.setR15_risk_weight_factor(rs.getBigDecimal("R15_RISK_WEIGHT_FACTOR"));
			obj.setR16_product(rs.getString("R16_PRODUCT")); obj.setR16_month(rs.getString("R16_MONTH")); obj.setR16_gross_income(rs.getBigDecimal("R16_GROSS_INCOME"));
			obj.setR16_aggregate_gross_income(rs.getBigDecimal("R16_AGGREGATE_GROSS_INCOME")); obj.setR16_risk_weight_factor(rs.getBigDecimal("R16_RISK_WEIGHT_FACTOR"));
			obj.setR17_product(rs.getString("R17_PRODUCT")); obj.setR17_month(rs.getString("R17_MONTH")); obj.setR17_gross_income(rs.getBigDecimal("R17_GROSS_INCOME"));
			obj.setR17_aggregate_gross_income(rs.getBigDecimal("R17_AGGREGATE_GROSS_INCOME")); obj.setR17_risk_weight_factor(rs.getBigDecimal("R17_RISK_WEIGHT_FACTOR"));
			obj.setR18_product(rs.getString("R18_PRODUCT")); obj.setR18_month(rs.getString("R18_MONTH")); obj.setR18_gross_income(rs.getBigDecimal("R18_GROSS_INCOME"));
			obj.setR18_aggregate_gross_income(rs.getBigDecimal("R18_AGGREGATE_GROSS_INCOME")); obj.setR18_risk_weight_factor(rs.getBigDecimal("R18_RISK_WEIGHT_FACTOR"));
			obj.setR19_product(rs.getString("R19_PRODUCT")); obj.setR19_month(rs.getString("R19_MONTH")); obj.setR19_gross_income(rs.getBigDecimal("R19_GROSS_INCOME"));
			obj.setR19_aggregate_gross_income(rs.getBigDecimal("R19_AGGREGATE_GROSS_INCOME")); obj.setR19_risk_weight_factor(rs.getBigDecimal("R19_RISK_WEIGHT_FACTOR"));
			obj.setR20_product(rs.getString("R20_PRODUCT")); obj.setR20_month(rs.getString("R20_MONTH")); obj.setR20_gross_income(rs.getBigDecimal("R20_GROSS_INCOME"));
			obj.setR20_aggregate_gross_income(rs.getBigDecimal("R20_AGGREGATE_GROSS_INCOME")); obj.setR20_risk_weight_factor(rs.getBigDecimal("R20_RISK_WEIGHT_FACTOR"));
			obj.setR21_product(rs.getString("R21_PRODUCT")); obj.setR21_month(rs.getString("R21_MONTH")); obj.setR21_gross_income(rs.getBigDecimal("R21_GROSS_INCOME"));
			obj.setR21_aggregate_gross_income(rs.getBigDecimal("R21_AGGREGATE_GROSS_INCOME")); obj.setR21_risk_weight_factor(rs.getBigDecimal("R21_RISK_WEIGHT_FACTOR"));
			obj.setR22_product(rs.getString("R22_PRODUCT")); obj.setR22_month(rs.getString("R22_MONTH")); obj.setR22_gross_income(rs.getBigDecimal("R22_GROSS_INCOME"));
			obj.setR22_aggregate_gross_income(rs.getBigDecimal("R22_AGGREGATE_GROSS_INCOME")); obj.setR22_risk_weight_factor(rs.getBigDecimal("R22_RISK_WEIGHT_FACTOR"));
			obj.setR23_product(rs.getString("R23_PRODUCT")); obj.setR23_month(rs.getString("R23_MONTH")); obj.setR23_gross_income(rs.getBigDecimal("R23_GROSS_INCOME"));
			obj.setR23_aggregate_gross_income(rs.getBigDecimal("R23_AGGREGATE_GROSS_INCOME")); obj.setR23_risk_weight_factor(rs.getBigDecimal("R23_RISK_WEIGHT_FACTOR"));
			obj.setR24_product(rs.getString("R24_PRODUCT")); obj.setR24_month(rs.getString("R24_MONTH")); obj.setR24_gross_income(rs.getBigDecimal("R24_GROSS_INCOME"));
			obj.setR24_aggregate_gross_income(rs.getBigDecimal("R24_AGGREGATE_GROSS_INCOME")); obj.setR24_risk_weight_factor(rs.getBigDecimal("R24_RISK_WEIGHT_FACTOR"));
			obj.setR25_product(rs.getString("R25_PRODUCT")); obj.setR25_month(rs.getString("R25_MONTH")); obj.setR25_gross_income(rs.getBigDecimal("R25_GROSS_INCOME"));
			obj.setR25_aggregate_gross_income(rs.getBigDecimal("R25_AGGREGATE_GROSS_INCOME")); obj.setR25_risk_weight_factor(rs.getBigDecimal("R25_RISK_WEIGHT_FACTOR"));
			obj.setR26_product(rs.getString("R26_PRODUCT")); obj.setR26_month(rs.getString("R26_MONTH")); obj.setR26_gross_income(rs.getBigDecimal("R26_GROSS_INCOME"));
			obj.setR26_aggregate_gross_income(rs.getBigDecimal("R26_AGGREGATE_GROSS_INCOME")); obj.setR26_risk_weight_factor(rs.getBigDecimal("R26_RISK_WEIGHT_FACTOR"));
			obj.setR27_product(rs.getString("R27_PRODUCT")); obj.setR27_month(rs.getString("R27_MONTH")); obj.setR27_gross_income(rs.getBigDecimal("R27_GROSS_INCOME"));
			obj.setR27_aggregate_gross_income(rs.getBigDecimal("R27_AGGREGATE_GROSS_INCOME")); obj.setR27_risk_weight_factor(rs.getBigDecimal("R27_RISK_WEIGHT_FACTOR"));
			obj.setR28_product(rs.getString("R28_PRODUCT")); obj.setR28_month(rs.getString("R28_MONTH")); obj.setR28_gross_income(rs.getBigDecimal("R28_GROSS_INCOME"));
			obj.setR28_aggregate_gross_income(rs.getBigDecimal("R28_AGGREGATE_GROSS_INCOME")); obj.setR28_risk_weight_factor(rs.getBigDecimal("R28_RISK_WEIGHT_FACTOR"));
			obj.setR29_product(rs.getString("R29_PRODUCT")); obj.setR29_month(rs.getString("R29_MONTH")); obj.setR29_gross_income(rs.getBigDecimal("R29_GROSS_INCOME"));
			obj.setR29_aggregate_gross_income(rs.getBigDecimal("R29_AGGREGATE_GROSS_INCOME")); obj.setR29_risk_weight_factor(rs.getBigDecimal("R29_RISK_WEIGHT_FACTOR"));
			obj.setR30_product(rs.getString("R30_PRODUCT")); obj.setR30_month(rs.getString("R30_MONTH")); obj.setR30_gross_income(rs.getBigDecimal("R30_GROSS_INCOME"));
			obj.setR30_aggregate_gross_income(rs.getBigDecimal("R30_AGGREGATE_GROSS_INCOME")); obj.setR30_risk_weight_factor(rs.getBigDecimal("R30_RISK_WEIGHT_FACTOR"));
			obj.setR31_product(rs.getString("R31_PRODUCT")); obj.setR31_month(rs.getString("R31_MONTH")); obj.setR31_gross_income(rs.getBigDecimal("R31_GROSS_INCOME"));
			obj.setR31_aggregate_gross_income(rs.getBigDecimal("R31_AGGREGATE_GROSS_INCOME")); obj.setR31_risk_weight_factor(rs.getBigDecimal("R31_RISK_WEIGHT_FACTOR"));
			obj.setR32_product(rs.getString("R32_PRODUCT")); obj.setR32_month(rs.getString("R32_MONTH")); obj.setR32_gross_income(rs.getBigDecimal("R32_GROSS_INCOME"));
			obj.setR32_aggregate_gross_income(rs.getBigDecimal("R32_AGGREGATE_GROSS_INCOME")); obj.setR32_risk_weight_factor(rs.getBigDecimal("R32_RISK_WEIGHT_FACTOR"));
			obj.setR33_product(rs.getString("R33_PRODUCT")); obj.setR33_month(rs.getString("R33_MONTH")); obj.setR33_gross_income(rs.getBigDecimal("R33_GROSS_INCOME"));
			obj.setR33_aggregate_gross_income(rs.getBigDecimal("R33_AGGREGATE_GROSS_INCOME")); obj.setR33_risk_weight_factor(rs.getBigDecimal("R33_RISK_WEIGHT_FACTOR"));
			obj.setR34_product(rs.getString("R34_PRODUCT")); obj.setR34_month(rs.getString("R34_MONTH")); obj.setR34_gross_income(rs.getBigDecimal("R34_GROSS_INCOME"));
			obj.setR34_aggregate_gross_income(rs.getBigDecimal("R34_AGGREGATE_GROSS_INCOME")); obj.setR34_risk_weight_factor(rs.getBigDecimal("R34_RISK_WEIGHT_FACTOR"));
			obj.setR35_product(rs.getString("R35_PRODUCT")); obj.setR35_month(rs.getString("R35_MONTH")); obj.setR35_gross_income(rs.getBigDecimal("R35_GROSS_INCOME"));
			obj.setR35_aggregate_gross_income(rs.getBigDecimal("R35_AGGREGATE_GROSS_INCOME")); obj.setR35_risk_weight_factor(rs.getBigDecimal("R35_RISK_WEIGHT_FACTOR"));
			obj.setR36_product(rs.getString("R36_PRODUCT")); obj.setR36_month(rs.getString("R36_MONTH")); obj.setR36_gross_income(rs.getBigDecimal("R36_GROSS_INCOME"));
			obj.setR36_aggregate_gross_income(rs.getBigDecimal("R36_AGGREGATE_GROSS_INCOME")); obj.setR36_risk_weight_factor(rs.getBigDecimal("R36_RISK_WEIGHT_FACTOR"));
			obj.setR37_product(rs.getString("R37_PRODUCT")); obj.setR37_month(rs.getString("R37_MONTH")); obj.setR37_gross_income(rs.getBigDecimal("R37_GROSS_INCOME"));
			obj.setR37_aggregate_gross_income(rs.getBigDecimal("R37_AGGREGATE_GROSS_INCOME")); obj.setR37_risk_weight_factor(rs.getBigDecimal("R37_RISK_WEIGHT_FACTOR"));
			obj.setR38_product(rs.getString("R38_PRODUCT")); obj.setR38_month(rs.getString("R38_MONTH")); obj.setR38_gross_income(rs.getBigDecimal("R38_GROSS_INCOME"));
			obj.setR38_aggregate_gross_income(rs.getBigDecimal("R38_AGGREGATE_GROSS_INCOME")); obj.setR38_risk_weight_factor(rs.getBigDecimal("R38_RISK_WEIGHT_FACTOR"));
			obj.setR39_product(rs.getString("R39_PRODUCT")); obj.setR39_month(rs.getString("R39_MONTH")); obj.setR39_gross_income(rs.getBigDecimal("R39_GROSS_INCOME"));
			obj.setR39_aggregate_gross_income(rs.getBigDecimal("R39_AGGREGATE_GROSS_INCOME")); obj.setR39_risk_weight_factor(rs.getBigDecimal("R39_RISK_WEIGHT_FACTOR"));
			obj.setR40_product(rs.getString("R40_PRODUCT")); obj.setR40_month(rs.getString("R40_MONTH")); obj.setR40_gross_income(rs.getBigDecimal("R40_GROSS_INCOME"));
			obj.setR40_aggregate_gross_income(rs.getBigDecimal("R40_AGGREGATE_GROSS_INCOME")); obj.setR40_risk_weight_factor(rs.getBigDecimal("R40_RISK_WEIGHT_FACTOR"));
			obj.setR41_product(rs.getString("R41_PRODUCT")); obj.setR41_month(rs.getString("R41_MONTH")); obj.setR41_gross_income(rs.getBigDecimal("R41_GROSS_INCOME"));
			obj.setR41_aggregate_gross_income(rs.getBigDecimal("R41_AGGREGATE_GROSS_INCOME")); obj.setR41_risk_weight_factor(rs.getBigDecimal("R41_RISK_WEIGHT_FACTOR"));
			obj.setR42_product(rs.getString("R42_PRODUCT")); obj.setR42_month(rs.getString("R42_MONTH")); obj.setR42_gross_income(rs.getBigDecimal("R42_GROSS_INCOME"));
			obj.setR42_aggregate_gross_income(rs.getBigDecimal("R42_AGGREGATE_GROSS_INCOME")); obj.setR42_risk_weight_factor(rs.getBigDecimal("R42_RISK_WEIGHT_FACTOR"));
			obj.setR43_product(rs.getString("R43_PRODUCT")); obj.setR43_month(rs.getString("R43_MONTH")); obj.setR43_gross_income(rs.getBigDecimal("R43_GROSS_INCOME"));
			obj.setR43_aggregate_gross_income(rs.getBigDecimal("R43_AGGREGATE_GROSS_INCOME")); obj.setR43_risk_weight_factor(rs.getBigDecimal("R43_RISK_WEIGHT_FACTOR"));
			obj.setR44_product(rs.getString("R44_PRODUCT")); obj.setR44_month(rs.getString("R44_MONTH")); obj.setR44_gross_income(rs.getBigDecimal("R44_GROSS_INCOME"));
			obj.setR44_aggregate_gross_income(rs.getBigDecimal("R44_AGGREGATE_GROSS_INCOME")); obj.setR44_risk_weight_factor(rs.getBigDecimal("R44_RISK_WEIGHT_FACTOR"));
			obj.setR45_product(rs.getString("R45_PRODUCT")); obj.setR45_month(rs.getString("R45_MONTH")); obj.setR45_gross_income(rs.getBigDecimal("R45_GROSS_INCOME"));
			obj.setR45_aggregate_gross_income(rs.getBigDecimal("R45_AGGREGATE_GROSS_INCOME")); obj.setR45_risk_weight_factor(rs.getBigDecimal("R45_RISK_WEIGHT_FACTOR"));
			obj.setR46_product(rs.getString("R46_PRODUCT")); obj.setR46_month(rs.getString("R46_MONTH")); obj.setR46_gross_income(rs.getBigDecimal("R46_GROSS_INCOME"));
			obj.setR46_aggregate_gross_income(rs.getBigDecimal("R46_AGGREGATE_GROSS_INCOME")); obj.setR46_risk_weight_factor(rs.getBigDecimal("R46_RISK_WEIGHT_FACTOR"));
			obj.setR47_product(rs.getString("R47_PRODUCT")); obj.setR47_month(rs.getString("R47_MONTH")); obj.setR47_gross_income(rs.getBigDecimal("R47_GROSS_INCOME"));
			obj.setR47_aggregate_gross_income(rs.getBigDecimal("R47_AGGREGATE_GROSS_INCOME")); obj.setR47_risk_weight_factor(rs.getBigDecimal("R47_RISK_WEIGHT_FACTOR"));
			obj.setR48_product(rs.getString("R48_PRODUCT")); obj.setR48_month(rs.getString("R48_MONTH")); obj.setR48_gross_income(rs.getBigDecimal("R48_GROSS_INCOME"));
			obj.setR48_aggregate_gross_income(rs.getBigDecimal("R48_AGGREGATE_GROSS_INCOME")); obj.setR48_risk_weight_factor(rs.getBigDecimal("R48_RISK_WEIGHT_FACTOR"));
			obj.setR49_product(rs.getString("R49_PRODUCT")); obj.setR49_month(rs.getString("R49_MONTH")); obj.setR49_gross_income(rs.getBigDecimal("R49_GROSS_INCOME"));
			obj.setR49_aggregate_gross_income(rs.getBigDecimal("R49_AGGREGATE_GROSS_INCOME")); obj.setR49_risk_weight_factor(rs.getBigDecimal("R49_RISK_WEIGHT_FACTOR"));
			obj.setR50_product(rs.getString("R50_PRODUCT")); obj.setR50_month(rs.getString("R50_MONTH")); obj.setR50_gross_income(rs.getBigDecimal("R50_GROSS_INCOME"));
			obj.setR50_aggregate_gross_income(rs.getBigDecimal("R50_AGGREGATE_GROSS_INCOME")); obj.setR50_risk_weight_factor(rs.getBigDecimal("R50_RISK_WEIGHT_FACTOR"));
			obj.setR51_product(rs.getString("R51_PRODUCT")); obj.setR51_month(rs.getString("R51_MONTH")); obj.setR51_gross_income(rs.getBigDecimal("R51_GROSS_INCOME"));
			obj.setR51_aggregate_gross_income(rs.getBigDecimal("R51_AGGREGATE_GROSS_INCOME")); obj.setR51_risk_weight_factor(rs.getBigDecimal("R51_RISK_WEIGHT_FACTOR"));
			obj.setR52_product(rs.getString("R52_PRODUCT")); obj.setR52_month(rs.getString("R52_MONTH")); obj.setR52_gross_income(rs.getBigDecimal("R52_GROSS_INCOME"));
			obj.setR52_aggregate_gross_income(rs.getBigDecimal("R52_AGGREGATE_GROSS_INCOME")); obj.setR52_risk_weight_factor(rs.getBigDecimal("R52_RISK_WEIGHT_FACTOR"));
			obj.setR53_product(rs.getString("R53_PRODUCT")); obj.setR53_month(rs.getString("R53_MONTH")); obj.setR53_gross_income(rs.getBigDecimal("R53_GROSS_INCOME"));
			obj.setR53_aggregate_gross_income(rs.getBigDecimal("R53_AGGREGATE_GROSS_INCOME")); obj.setR53_risk_weight_factor(rs.getBigDecimal("R53_RISK_WEIGHT_FACTOR"));
			obj.setR54_product(rs.getString("R54_PRODUCT")); obj.setR54_month(rs.getString("R54_MONTH")); obj.setR54_gross_income(rs.getBigDecimal("R54_GROSS_INCOME"));
			obj.setR54_aggregate_gross_income(rs.getBigDecimal("R54_AGGREGATE_GROSS_INCOME")); obj.setR54_risk_weight_factor(rs.getBigDecimal("R54_RISK_WEIGHT_FACTOR"));
			obj.setR55_product(rs.getString("R55_PRODUCT")); obj.setR55_month(rs.getString("R55_MONTH")); obj.setR55_gross_income(rs.getBigDecimal("R55_GROSS_INCOME"));
			obj.setR55_aggregate_gross_income(rs.getBigDecimal("R55_AGGREGATE_GROSS_INCOME")); obj.setR55_risk_weight_factor(rs.getBigDecimal("R55_RISK_WEIGHT_FACTOR"));
			obj.setR56_product(rs.getString("R56_PRODUCT")); obj.setR56_month(rs.getString("R56_MONTH")); obj.setR56_gross_income(rs.getBigDecimal("R56_GROSS_INCOME"));
			obj.setR56_aggregate_gross_income(rs.getBigDecimal("R56_AGGREGATE_GROSS_INCOME")); obj.setR56_risk_weight_factor(rs.getBigDecimal("R56_RISK_WEIGHT_FACTOR"));
			return obj;
		}
	}

	class M_OR1ArchivalDetailRowMapper implements RowMapper<M_OR1_Archival_Detail_Entity> {
		@Override
		public M_OR1_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_OR1_Archival_Detail_Entity obj = new M_OR1_Archival_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getTimestamp("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getTimestamp("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getTimestamp("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

}

