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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.M_UNCONS_INVEST_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Detail_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity;
import com.bornfire.brrs.entities.UserProfileRep;

@Component
@Service

public class BRRS_M_UNCONS_INVEST_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_UNCONS_INVEST_ReportService.class);

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

	public List<M_UNCONS_INVEST_Summary_Entity> getSummaryByDate(Date reportDate) {
		return jdbcTemplate.query("SELECT * FROM BRRS_M_UNCONS_INVEST_SUMMARYTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, new M_UNCONS_INVESTSummaryRowMapper());
	}

	public List<M_UNCONS_INVEST_Detail_Entity> getDetailByDate(Date reportDate) {
		return jdbcTemplate.query("SELECT * FROM BRRS_M_UNCONS_INVEST_DETAILTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, new M_UNCONS_INVESTDetailRowMapper());
	}

	public List<M_UNCONS_INVEST_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate,
			BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_UNCONS_INVEST_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_UNCONS_INVESTArchivalSummaryRowMapper());
	}

	public List<M_UNCONS_INVEST_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate,
			BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_UNCONS_INVEST_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_UNCONS_INVESTArchivalDetailRowMapper());
	}

	public List<M_UNCONS_INVEST_Archival_Summary_Entity> getArchivalSummaryWithVersionAll() {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_UNCONS_INVEST_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC",
				new M_UNCONS_INVESTArchivalSummaryRowMapper());
	}

	public List<M_UNCONS_INVEST_Resub_Summary_Entity> getResubSummaryByDateAndVersion(Date reportDate,
			BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_UNCONS_INVEST_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_UNCONS_INVESTResubSummaryRowMapper());
	}

	public List<M_UNCONS_INVEST_Resub_Detail_Entity> getResubDetailByDateAndVersion(Date reportDate,
			BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_UNCONS_INVEST_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_UNCONS_INVESTResubDetailRowMapper());
	}

	public BigDecimal findMaxResubVersion(Date reportDate) {
		return jdbcTemplate.queryForObject(
				"SELECT MAX(REPORT_VERSION) FROM BRRS_M_UNCONS_INVEST_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, BigDecimal.class);
	}

	// =========================================================
	// JDBC WRITE METHODS
	// =========================================================

	private static final String R_COLS = "R11_PRODUCT,R11_AMOUNT,R11_PERCENT_OF_CET1_HOLDING,R11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING,R11_PERCENT_OF_TIER_2_HOLDING,"
			+ "R12_PRODUCT,R12_AMOUNT,R12_PERCENT_OF_CET1_HOLDING,R12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING,R12_PERCENT_OF_TIER_2_HOLDING,"
			+ "R13_PRODUCT,R13_AMOUNT,R13_PERCENT_OF_CET1_HOLDING,R13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING,R13_PERCENT_OF_TIER_2_HOLDING,"
			+ "R14_PRODUCT,R14_AMOUNT,R14_PERCENT_OF_CET1_HOLDING,R14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING,R14_PERCENT_OF_TIER_2_HOLDING,"
			+ "R15_PRODUCT,R15_AMOUNT,R15_PERCENT_OF_CET1_HOLDING,R15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING,R15_PERCENT_OF_TIER_2_HOLDING,"
			+ "R22_PRODUCT,R22_ACCUULATED_EQUITY_INTEREST_5,R22_ASSETS,R22_LIABILITIES,R22_REVENUE,R22_PROFIT_OR_LOSS,R22_UNREG_SHARE_OF_LOSS,R22_CUMULATIVE_UNREG_SHARE_OF_LOSS,"
			+ "R23_PRODUCT,R23_ACCUULATED_EQUITY_INTEREST_5,R23_ASSETS,R23_LIABILITIES,R23_REVENUE,R23_PROFIT_OR_LOSS,R23_UNREG_SHARE_OF_LOSS,R23_CUMULATIVE_UNREG_SHARE_OF_LOSS,"
			+ "R24_PRODUCT,R24_ACCUULATED_EQUITY_INTEREST_5,R24_ASSETS,R24_LIABILITIES,R24_REVENUE,R24_PROFIT_OR_LOSS,R24_UNREG_SHARE_OF_LOSS,R24_CUMULATIVE_UNREG_SHARE_OF_LOSS,"
			+ "R29_PRODUCT,R29_FAIR_VALUE,"
			+ "R35_PRODUCT,R35_COMPANY,R35_JURISDICTION_OF_INCORP_1,R35_JURISDICTION_OF_INCORP_2,R35_LINE_OF_BUSINESS,R35_CURRENCY,R35_SHARE_CAPITAL,R35_ACCUMULATED_EQUITY_INTEREST,"
			+ "R36_PRODUCT,R36_COMPANY,R36_JURISDICTION_OF_INCORP_1,R36_JURISDICTION_OF_INCORP_2,R36_LINE_OF_BUSINESS,R36_CURRENCY,R36_SHARE_CAPITAL,R36_ACCUMULATED_EQUITY_INTEREST,"
			+ "R37_PRODUCT,R37_COMPANY,R37_JURISDICTION_OF_INCORP_1,R37_JURISDICTION_OF_INCORP_2,R37_LINE_OF_BUSINESS,R37_CURRENCY,R37_SHARE_CAPITAL,R37_ACCUMULATED_EQUITY_INTEREST,"
			+ "R38_PRODUCT,R38_COMPANY,R38_JURISDICTION_OF_INCORP_1,R38_JURISDICTION_OF_INCORP_2,R38_LINE_OF_BUSINESS,R38_CURRENCY,R38_SHARE_CAPITAL,R38_ACCUMULATED_EQUITY_INTEREST";

	private static final String R_PLACEHOLDERS = "?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, "
			+ "?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?, " + "?,?, "
			+ "?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?";

	private static String rFieldsSet() {
		StringBuilder sb = new StringBuilder();
		for (String col : R_COLS.split(",")) {
			sb.append(col).append("=?,");
		}
		return sb.substring(0, sb.length() - 1);
	}

	private static final String R_FIELDS_SET = rFieldsSet();

	private Object[] rFieldValues(M_UNCONS_INVEST_Summary_Entity e) {
		return new Object[] { e.getR11_product(), e.getR11_amount(), e.getR11_percent_of_cet1_holding(),
				e.getR11_percent_of_additional_tier_1_holding(), e.getR11_percent_of_tier_2_holding(),
				e.getR12_product(), e.getR12_amount(), e.getR12_percent_of_cet1_holding(),
				e.getR12_percent_of_additional_tier_1_holding(), e.getR12_percent_of_tier_2_holding(),
				e.getR13_product(), e.getR13_amount(), e.getR13_percent_of_cet1_holding(),
				e.getR13_percent_of_additional_tier_1_holding(), e.getR13_percent_of_tier_2_holding(),
				e.getR14_product(), e.getR14_amount(), e.getR14_percent_of_cet1_holding(),
				e.getR14_percent_of_additional_tier_1_holding(), e.getR14_percent_of_tier_2_holding(),
				e.getR15_product(), e.getR15_amount(), e.getR15_percent_of_cet1_holding(),
				e.getR15_percent_of_additional_tier_1_holding(), e.getR15_percent_of_tier_2_holding(),
				e.getR22_product(), e.getR22_accuulated_equity_interest_5(), e.getR22_assets(), e.getR22_liabilities(),
				e.getR22_revenue(), e.getR22_profit_or_loss(), e.getR22_unreg_share_of_loss(),
				e.getR22_cumulative_unreg_share_of_loss(),
				e.getR23_product(), e.getR23_accuulated_equity_interest_5(), e.getR23_assets(), e.getR23_liabilities(),
				e.getR23_revenue(), e.getR23_profit_or_loss(), e.getR23_unreg_share_of_loss(),
				e.getR23_cumulative_unreg_share_of_loss(),
				e.getR24_product(), e.getR24_accuulated_equity_interest_5(), e.getR24_assets(), e.getR24_liabilities(),
				e.getR24_revenue(), e.getR24_profit_or_loss(), e.getR24_unreg_share_of_loss(),
				e.getR24_cumulative_unreg_share_of_loss(),
				e.getR29_product(), e.getR29_fair_value(),
				e.getR35_product(), e.getR35_company(), e.getR35_jurisdiction_of_incorp_1(),
				e.getR35_jurisdiction_of_incorp_2(), e.getR35_line_of_business(), e.getR35_currency(),
				e.getR35_share_capital(), e.getR35_accumulated_equity_interest(),
				e.getR36_product(), e.getR36_company(), e.getR36_jurisdiction_of_incorp_1(),
				e.getR36_jurisdiction_of_incorp_2(), e.getR36_line_of_business(), e.getR36_currency(),
				e.getR36_share_capital(), e.getR36_accumulated_equity_interest(),
				e.getR37_product(), e.getR37_company(), e.getR37_jurisdiction_of_incorp_1(),
				e.getR37_jurisdiction_of_incorp_2(), e.getR37_line_of_business(), e.getR37_currency(),
				e.getR37_share_capital(), e.getR37_accumulated_equity_interest(),
				e.getR38_product(), e.getR38_company(), e.getR38_jurisdiction_of_incorp_1(),
				e.getR38_jurisdiction_of_incorp_2(), e.getR38_line_of_business(), e.getR38_currency(),
				e.getR38_share_capital(), e.getR38_accumulated_equity_interest() };
	}

	private Object[] rFieldValues(M_UNCONS_INVEST_Detail_Entity e) {
		return new Object[] { e.getR11_product(), e.getR11_amount(), e.getR11_percent_of_cet1_holding(),
				e.getR11_percent_of_additional_tier_1_holding(), e.getR11_percent_of_tier_2_holding(),
				e.getR12_product(), e.getR12_amount(), e.getR12_percent_of_cet1_holding(),
				e.getR12_percent_of_additional_tier_1_holding(), e.getR12_percent_of_tier_2_holding(),
				e.getR13_product(), e.getR13_amount(), e.getR13_percent_of_cet1_holding(),
				e.getR13_percent_of_additional_tier_1_holding(), e.getR13_percent_of_tier_2_holding(),
				e.getR14_product(), e.getR14_amount(), e.getR14_percent_of_cet1_holding(),
				e.getR14_percent_of_additional_tier_1_holding(), e.getR14_percent_of_tier_2_holding(),
				e.getR15_product(), e.getR15_amount(), e.getR15_percent_of_cet1_holding(),
				e.getR15_percent_of_additional_tier_1_holding(), e.getR15_percent_of_tier_2_holding(),
				e.getR22_product(), e.getR22_accuulated_equity_interest_5(), e.getR22_assets(), e.getR22_liabilities(),
				e.getR22_revenue(), e.getR22_profit_or_loss(), e.getR22_unreg_share_of_loss(),
				e.getR22_cumulative_unreg_share_of_loss(),
				e.getR23_product(), e.getR23_accuulated_equity_interest_5(), e.getR23_assets(), e.getR23_liabilities(),
				e.getR23_revenue(), e.getR23_profit_or_loss(), e.getR23_unreg_share_of_loss(),
				e.getR23_cumulative_unreg_share_of_loss(),
				e.getR24_product(), e.getR24_accuulated_equity_interest_5(), e.getR24_assets(), e.getR24_liabilities(),
				e.getR24_revenue(), e.getR24_profit_or_loss(), e.getR24_unreg_share_of_loss(),
				e.getR24_cumulative_unreg_share_of_loss(),
				e.getR29_product(), e.getR29_fair_value(),
				e.getR35_product(), e.getR35_company(), e.getR35_jurisdiction_of_incorp_1(),
				e.getR35_jurisdiction_of_incorp_2(), e.getR35_line_of_business(), e.getR35_currency(),
				e.getR35_share_capital(), e.getR35_accumulated_equity_interest(),
				e.getR36_product(), e.getR36_company(), e.getR36_jurisdiction_of_incorp_1(),
				e.getR36_jurisdiction_of_incorp_2(), e.getR36_line_of_business(), e.getR36_currency(),
				e.getR36_share_capital(), e.getR36_accumulated_equity_interest(),
				e.getR37_product(), e.getR37_company(), e.getR37_jurisdiction_of_incorp_1(),
				e.getR37_jurisdiction_of_incorp_2(), e.getR37_line_of_business(), e.getR37_currency(),
				e.getR37_share_capital(), e.getR37_accumulated_equity_interest(),
				e.getR38_product(), e.getR38_company(), e.getR38_jurisdiction_of_incorp_1(),
				e.getR38_jurisdiction_of_incorp_2(), e.getR38_line_of_business(), e.getR38_currency(),
				e.getR38_share_capital(), e.getR38_accumulated_equity_interest() };
	}

	private Object[] rFieldValues(M_UNCONS_INVEST_Archival_Summary_Entity e) {
		return new Object[] { e.getR11_product(), e.getR11_amount(), e.getR11_percent_of_cet1_holding(),
				e.getR11_percent_of_additional_tier_1_holding(), e.getR11_percent_of_tier_2_holding(),
				e.getR12_product(), e.getR12_amount(), e.getR12_percent_of_cet1_holding(),
				e.getR12_percent_of_additional_tier_1_holding(), e.getR12_percent_of_tier_2_holding(),
				e.getR13_product(), e.getR13_amount(), e.getR13_percent_of_cet1_holding(),
				e.getR13_percent_of_additional_tier_1_holding(), e.getR13_percent_of_tier_2_holding(),
				e.getR14_product(), e.getR14_amount(), e.getR14_percent_of_cet1_holding(),
				e.getR14_percent_of_additional_tier_1_holding(), e.getR14_percent_of_tier_2_holding(),
				e.getR15_product(), e.getR15_amount(), e.getR15_percent_of_cet1_holding(),
				e.getR15_percent_of_additional_tier_1_holding(), e.getR15_percent_of_tier_2_holding(),
				e.getR22_product(), e.getR22_accuulated_equity_interest_5(), e.getR22_assets(), e.getR22_liabilities(),
				e.getR22_revenue(), e.getR22_profit_or_loss(), e.getR22_unreg_share_of_loss(),
				e.getR22_cumulative_unreg_share_of_loss(),
				e.getR23_product(), e.getR23_accuulated_equity_interest_5(), e.getR23_assets(), e.getR23_liabilities(),
				e.getR23_revenue(), e.getR23_profit_or_loss(), e.getR23_unreg_share_of_loss(),
				e.getR23_cumulative_unreg_share_of_loss(),
				e.getR24_product(), e.getR24_accuulated_equity_interest_5(), e.getR24_assets(), e.getR24_liabilities(),
				e.getR24_revenue(), e.getR24_profit_or_loss(), e.getR24_unreg_share_of_loss(),
				e.getR24_cumulative_unreg_share_of_loss(),
				e.getR29_product(), e.getR29_fair_value(),
				e.getR35_product(), e.getR35_company(), e.getR35_jurisdiction_of_incorp_1(),
				e.getR35_jurisdiction_of_incorp_2(), e.getR35_line_of_business(), e.getR35_currency(),
				e.getR35_share_capital(), e.getR35_accumulated_equity_interest(),
				e.getR36_product(), e.getR36_company(), e.getR36_jurisdiction_of_incorp_1(),
				e.getR36_jurisdiction_of_incorp_2(), e.getR36_line_of_business(), e.getR36_currency(),
				e.getR36_share_capital(), e.getR36_accumulated_equity_interest(),
				e.getR37_product(), e.getR37_company(), e.getR37_jurisdiction_of_incorp_1(),
				e.getR37_jurisdiction_of_incorp_2(), e.getR37_line_of_business(), e.getR37_currency(),
				e.getR37_share_capital(), e.getR37_accumulated_equity_interest(),
				e.getR38_product(), e.getR38_company(), e.getR38_jurisdiction_of_incorp_1(),
				e.getR38_jurisdiction_of_incorp_2(), e.getR38_line_of_business(), e.getR38_currency(),
				e.getR38_share_capital(), e.getR38_accumulated_equity_interest() };
	}

	private Object[] rFieldValues(M_UNCONS_INVEST_Archival_Detail_Entity e) {
		return new Object[] { e.getR11_product(), e.getR11_amount(), e.getR11_percent_of_cet1_holding(),
				e.getR11_percent_of_additional_tier_1_holding(), e.getR11_percent_of_tier_2_holding(),
				e.getR12_product(), e.getR12_amount(), e.getR12_percent_of_cet1_holding(),
				e.getR12_percent_of_additional_tier_1_holding(), e.getR12_percent_of_tier_2_holding(),
				e.getR13_product(), e.getR13_amount(), e.getR13_percent_of_cet1_holding(),
				e.getR13_percent_of_additional_tier_1_holding(), e.getR13_percent_of_tier_2_holding(),
				e.getR14_product(), e.getR14_amount(), e.getR14_percent_of_cet1_holding(),
				e.getR14_percent_of_additional_tier_1_holding(), e.getR14_percent_of_tier_2_holding(),
				e.getR15_product(), e.getR15_amount(), e.getR15_percent_of_cet1_holding(),
				e.getR15_percent_of_additional_tier_1_holding(), e.getR15_percent_of_tier_2_holding(),
				e.getR22_product(), e.getR22_accuulated_equity_interest_5(), e.getR22_assets(), e.getR22_liabilities(),
				e.getR22_revenue(), e.getR22_profit_or_loss(), e.getR22_unreg_share_of_loss(),
				e.getR22_cumulative_unreg_share_of_loss(),
				e.getR23_product(), e.getR23_accuulated_equity_interest_5(), e.getR23_assets(), e.getR23_liabilities(),
				e.getR23_revenue(), e.getR23_profit_or_loss(), e.getR23_unreg_share_of_loss(),
				e.getR23_cumulative_unreg_share_of_loss(),
				e.getR24_product(), e.getR24_accuulated_equity_interest_5(), e.getR24_assets(), e.getR24_liabilities(),
				e.getR24_revenue(), e.getR24_profit_or_loss(), e.getR24_unreg_share_of_loss(),
				e.getR24_cumulative_unreg_share_of_loss(),
				e.getR29_product(), e.getR29_fair_value(),
				e.getR35_product(), e.getR35_company(), e.getR35_jurisdiction_of_incorp_1(),
				e.getR35_jurisdiction_of_incorp_2(), e.getR35_line_of_business(), e.getR35_currency(),
				e.getR35_share_capital(), e.getR35_accumulated_equity_interest(),
				e.getR36_product(), e.getR36_company(), e.getR36_jurisdiction_of_incorp_1(),
				e.getR36_jurisdiction_of_incorp_2(), e.getR36_line_of_business(), e.getR36_currency(),
				e.getR36_share_capital(), e.getR36_accumulated_equity_interest(),
				e.getR37_product(), e.getR37_company(), e.getR37_jurisdiction_of_incorp_1(),
				e.getR37_jurisdiction_of_incorp_2(), e.getR37_line_of_business(), e.getR37_currency(),
				e.getR37_share_capital(), e.getR37_accumulated_equity_interest(),
				e.getR38_product(), e.getR38_company(), e.getR38_jurisdiction_of_incorp_1(),
				e.getR38_jurisdiction_of_incorp_2(), e.getR38_line_of_business(), e.getR38_currency(),
				e.getR38_share_capital(), e.getR38_accumulated_equity_interest() };
	}

	private Object[] rFieldValues(M_UNCONS_INVEST_Resub_Summary_Entity e) {
		return new Object[] { e.getR11_product(), e.getR11_amount(), e.getR11_percent_of_cet1_holding(),
				e.getR11_percent_of_additional_tier_1_holding(), e.getR11_percent_of_tier_2_holding(),
				e.getR12_product(), e.getR12_amount(), e.getR12_percent_of_cet1_holding(),
				e.getR12_percent_of_additional_tier_1_holding(), e.getR12_percent_of_tier_2_holding(),
				e.getR13_product(), e.getR13_amount(), e.getR13_percent_of_cet1_holding(),
				e.getR13_percent_of_additional_tier_1_holding(), e.getR13_percent_of_tier_2_holding(),
				e.getR14_product(), e.getR14_amount(), e.getR14_percent_of_cet1_holding(),
				e.getR14_percent_of_additional_tier_1_holding(), e.getR14_percent_of_tier_2_holding(),
				e.getR15_product(), e.getR15_amount(), e.getR15_percent_of_cet1_holding(),
				e.getR15_percent_of_additional_tier_1_holding(), e.getR15_percent_of_tier_2_holding(),
				e.getR22_product(), e.getR22_accuulated_equity_interest_5(), e.getR22_assets(), e.getR22_liabilities(),
				e.getR22_revenue(), e.getR22_profit_or_loss(), e.getR22_unreg_share_of_loss(),
				e.getR22_cumulative_unreg_share_of_loss(),
				e.getR23_product(), e.getR23_accuulated_equity_interest_5(), e.getR23_assets(), e.getR23_liabilities(),
				e.getR23_revenue(), e.getR23_profit_or_loss(), e.getR23_unreg_share_of_loss(),
				e.getR23_cumulative_unreg_share_of_loss(),
				e.getR24_product(), e.getR24_accuulated_equity_interest_5(), e.getR24_assets(), e.getR24_liabilities(),
				e.getR24_revenue(), e.getR24_profit_or_loss(), e.getR24_unreg_share_of_loss(),
				e.getR24_cumulative_unreg_share_of_loss(),
				e.getR29_product(), e.getR29_fair_value(),
				e.getR35_product(), e.getR35_company(), e.getR35_jurisdiction_of_incorp_1(),
				e.getR35_jurisdiction_of_incorp_2(), e.getR35_line_of_business(), e.getR35_currency(),
				e.getR35_share_capital(), e.getR35_accumulated_equity_interest(),
				e.getR36_product(), e.getR36_company(), e.getR36_jurisdiction_of_incorp_1(),
				e.getR36_jurisdiction_of_incorp_2(), e.getR36_line_of_business(), e.getR36_currency(),
				e.getR36_share_capital(), e.getR36_accumulated_equity_interest(),
				e.getR37_product(), e.getR37_company(), e.getR37_jurisdiction_of_incorp_1(),
				e.getR37_jurisdiction_of_incorp_2(), e.getR37_line_of_business(), e.getR37_currency(),
				e.getR37_share_capital(), e.getR37_accumulated_equity_interest(),
				e.getR38_product(), e.getR38_company(), e.getR38_jurisdiction_of_incorp_1(),
				e.getR38_jurisdiction_of_incorp_2(), e.getR38_line_of_business(), e.getR38_currency(),
				e.getR38_share_capital(), e.getR38_accumulated_equity_interest() };
	}

	private Object[] rFieldValues(M_UNCONS_INVEST_Resub_Detail_Entity e) {
		return new Object[] { e.getR11_product(), e.getR11_amount(), e.getR11_percent_of_cet1_holding(),
				e.getR11_percent_of_additional_tier_1_holding(), e.getR11_percent_of_tier_2_holding(),
				e.getR12_product(), e.getR12_amount(), e.getR12_percent_of_cet1_holding(),
				e.getR12_percent_of_additional_tier_1_holding(), e.getR12_percent_of_tier_2_holding(),
				e.getR13_product(), e.getR13_amount(), e.getR13_percent_of_cet1_holding(),
				e.getR13_percent_of_additional_tier_1_holding(), e.getR13_percent_of_tier_2_holding(),
				e.getR14_product(), e.getR14_amount(), e.getR14_percent_of_cet1_holding(),
				e.getR14_percent_of_additional_tier_1_holding(), e.getR14_percent_of_tier_2_holding(),
				e.getR15_product(), e.getR15_amount(), e.getR15_percent_of_cet1_holding(),
				e.getR15_percent_of_additional_tier_1_holding(), e.getR15_percent_of_tier_2_holding(),
				e.getR22_product(), e.getR22_accuulated_equity_interest_5(), e.getR22_assets(), e.getR22_liabilities(),
				e.getR22_revenue(), e.getR22_profit_or_loss(), e.getR22_unreg_share_of_loss(),
				e.getR22_cumulative_unreg_share_of_loss(),
				e.getR23_product(), e.getR23_accuulated_equity_interest_5(), e.getR23_assets(), e.getR23_liabilities(),
				e.getR23_revenue(), e.getR23_profit_or_loss(), e.getR23_unreg_share_of_loss(),
				e.getR23_cumulative_unreg_share_of_loss(),
				e.getR24_product(), e.getR24_accuulated_equity_interest_5(), e.getR24_assets(), e.getR24_liabilities(),
				e.getR24_revenue(), e.getR24_profit_or_loss(), e.getR24_unreg_share_of_loss(),
				e.getR24_cumulative_unreg_share_of_loss(),
				e.getR29_product(), e.getR29_fair_value(),
				e.getR35_product(), e.getR35_company(), e.getR35_jurisdiction_of_incorp_1(),
				e.getR35_jurisdiction_of_incorp_2(), e.getR35_line_of_business(), e.getR35_currency(),
				e.getR35_share_capital(), e.getR35_accumulated_equity_interest(),
				e.getR36_product(), e.getR36_company(), e.getR36_jurisdiction_of_incorp_1(),
				e.getR36_jurisdiction_of_incorp_2(), e.getR36_line_of_business(), e.getR36_currency(),
				e.getR36_share_capital(), e.getR36_accumulated_equity_interest(),
				e.getR37_product(), e.getR37_company(), e.getR37_jurisdiction_of_incorp_1(),
				e.getR37_jurisdiction_of_incorp_2(), e.getR37_line_of_business(), e.getR37_currency(),
				e.getR37_share_capital(), e.getR37_accumulated_equity_interest(),
				e.getR38_product(), e.getR38_company(), e.getR38_jurisdiction_of_incorp_1(),
				e.getR38_jurisdiction_of_incorp_2(), e.getR38_line_of_business(), e.getR38_currency(),
				e.getR38_share_capital(), e.getR38_accumulated_equity_interest() };
	}

	private void saveSummary(M_UNCONS_INVEST_Summary_Entity e) {
		Integer cnt = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM BRRS_M_UNCONS_INVEST_SUMMARYTABLE WHERE REPORT_DATE=?",
				new Object[] { e.getReport_date() }, Integer.class);
		Object[] rVals = rFieldValues(e);
		if (cnt != null && cnt > 0) {
			Object[] params = new Object[rVals.length + 8];
			System.arraycopy(rVals, 0, params, 0, rVals.length);
			params[rVals.length] = e.getReport_version();
			params[rVals.length + 1] = e.getReport_frequency();
			params[rVals.length + 2] = e.getReport_code();
			params[rVals.length + 3] = e.getReport_desc();
			params[rVals.length + 4] = e.getEntity_flg();
			params[rVals.length + 5] = e.getModify_flg();
			params[rVals.length + 6] = e.getDel_flg();
			params[rVals.length + 7] = e.getReport_date();
			jdbcTemplate.update("UPDATE BRRS_M_UNCONS_INVEST_SUMMARYTABLE SET " + R_FIELDS_SET
					+ ",REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?,REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=? WHERE REPORT_DATE=?",
					params);
		} else {
			Object[] params = new Object[rVals.length + 8];
			params[0] = e.getReport_date();
			System.arraycopy(rVals, 0, params, 1, rVals.length);
			params[rVals.length + 1] = e.getReport_version();
			params[rVals.length + 2] = e.getReport_frequency();
			params[rVals.length + 3] = e.getReport_code();
			params[rVals.length + 4] = e.getReport_desc();
			params[rVals.length + 5] = e.getEntity_flg();
			params[rVals.length + 6] = e.getModify_flg();
			params[rVals.length + 7] = e.getDel_flg();
			jdbcTemplate.update("INSERT INTO BRRS_M_UNCONS_INVEST_SUMMARYTABLE (REPORT_DATE," + R_COLS
					+ ",REPORT_VERSION,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (?,"
					+ R_PLACEHOLDERS + ",?,?,?,?,?,?,?)", params);
		}
	}

	private void saveDetail(M_UNCONS_INVEST_Detail_Entity e) {
		Integer cnt = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM BRRS_M_UNCONS_INVEST_DETAILTABLE WHERE REPORT_DATE=?",
				new Object[] { e.getReport_date() }, Integer.class);
		Object[] rVals = rFieldValues(e);
		if (cnt != null && cnt > 0) {
			Object[] params = new Object[rVals.length + 8];
			System.arraycopy(rVals, 0, params, 0, rVals.length);
			params[rVals.length] = e.getReport_version();
			params[rVals.length + 1] = e.getReport_frequency();
			params[rVals.length + 2] = e.getReport_code();
			params[rVals.length + 3] = e.getReport_desc();
			params[rVals.length + 4] = e.getEntity_flg();
			params[rVals.length + 5] = e.getModify_flg();
			params[rVals.length + 6] = e.getDel_flg();
			params[rVals.length + 7] = e.getReport_date();
			jdbcTemplate.update("UPDATE BRRS_M_UNCONS_INVEST_DETAILTABLE SET " + R_FIELDS_SET
					+ ",REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?,REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=? WHERE REPORT_DATE=?",
					params);
		} else {
			Object[] params = new Object[rVals.length + 8];
			params[0] = e.getReport_date();
			System.arraycopy(rVals, 0, params, 1, rVals.length);
			params[rVals.length + 1] = e.getReport_version();
			params[rVals.length + 2] = e.getReport_frequency();
			params[rVals.length + 3] = e.getReport_code();
			params[rVals.length + 4] = e.getReport_desc();
			params[rVals.length + 5] = e.getEntity_flg();
			params[rVals.length + 6] = e.getModify_flg();
			params[rVals.length + 7] = e.getDel_flg();
			jdbcTemplate.update("INSERT INTO BRRS_M_UNCONS_INVEST_DETAILTABLE (REPORT_DATE," + R_COLS
					+ ",REPORT_VERSION,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (?,"
					+ R_PLACEHOLDERS + ",?,?,?,?,?,?,?)", params);
		}
	}

	private void insertResubSummary(M_UNCONS_INVEST_Resub_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 9];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length] = e.getReportDate();
		params[rVals.length + 1] = e.getReportVersion();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		params[rVals.length + 6] = e.getEntity_flg();
		params[rVals.length + 7] = e.getModify_flg();
		params[rVals.length + 8] = e.getDel_flg();
		jdbcTemplate.update("INSERT INTO BRRS_M_UNCONS_INVEST_RESUB_SUMMARYTABLE (" + R_COLS
				+ ",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES ("
				+ R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	private void insertResubDetail(M_UNCONS_INVEST_Resub_Detail_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 9];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length] = e.getReportDate();
		params[rVals.length + 1] = e.getReportVersion();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		params[rVals.length + 6] = e.getEntity_flg();
		params[rVals.length + 7] = e.getModify_flg();
		params[rVals.length + 8] = e.getDel_flg();
		jdbcTemplate.update("INSERT INTO BRRS_M_UNCONS_INVEST_RESUB_DETAILTABLE (" + R_COLS
				+ ",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES ("
				+ R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	private void insertArchivalSummary(M_UNCONS_INVEST_Archival_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 9];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length] = e.getReportDate();
		params[rVals.length + 1] = e.getReportVersion();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		params[rVals.length + 6] = e.getEntity_flg();
		params[rVals.length + 7] = e.getModify_flg();
		params[rVals.length + 8] = e.getDel_flg();
		jdbcTemplate.update("INSERT INTO BRRS_M_UNCONS_INVEST_ARCHIVALTABLE_SUMMARY (" + R_COLS
				+ ",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES ("
				+ R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	private void insertArchivalDetail(M_UNCONS_INVEST_Archival_Detail_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 9];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length] = e.getReportDate();
		params[rVals.length + 1] = e.getReportVersion();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		params[rVals.length + 6] = e.getEntity_flg();
		params[rVals.length + 7] = e.getModify_flg();
		params[rVals.length + 8] = e.getDel_flg();
		jdbcTemplate.update("INSERT INTO BRRS_M_UNCONS_INVEST_ARCHIVALTABLE_DETAIL (" + R_COLS
				+ ",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES ("
				+ R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	public ModelAndView getM_UNCONS_INVESTView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version,HttpServletRequest req1,Model md) {

		ModelAndView mv = new ModelAndView();
		
		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		System.out.println("dtltype...." + dtltype);
		System.out.println("type...." + type);

		try {

			// Parse only once
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW SCREEN =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + d1);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ===========================================================
			// SUMMARY SECTION
			// ===========================================================

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<M_UNCONS_INVEST_Archival_Summary_Entity> T1Master = getArchivalSummaryByDateAndVersion(d1,
						version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_UNCONS_INVEST_Resub_Summary_Entity> T1Master = getResubSummaryByDateAndVersion(d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_UNCONS_INVEST_Summary_Entity> T1Master = getSummaryByDate(d1);
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_UNCONS_INVEST_Archival_Detail_Entity> T1Master = getArchivalDetailByDateAndVersion(d1,
							version);
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_UNCONS_INVEST_Resub_Detail_Entity> T1Master = getResubDetailByDateAndVersion(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_UNCONS_INVEST_Detail_Entity> T1Master = getDetailByDate(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_UNCONS_INVEST");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	@Transactional
	public void updateReport(M_UNCONS_INVEST_Summary_Entity updatedEntity) {
		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getReport_date());
		// 🔹 Fetch existing SUMMARY
		List<M_UNCONS_INVEST_Summary_Entity> existingSummaryList = getSummaryByDate(updatedEntity.getReport_date());
		if (existingSummaryList.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}
		M_UNCONS_INVEST_Summary_Entity existingSummary = existingSummaryList.get(0);
		// 🔹 Create Audit Copy before editing
		M_UNCONS_INVEST_Summary_Entity oldcopy = new M_UNCONS_INVEST_Summary_Entity();
		BeanUtils.copyProperties(existingSummary, oldcopy);
		// 🔹 Fetch or create DETAIL
		List<M_UNCONS_INVEST_Detail_Entity> existingDetailList = getDetailByDate(updatedEntity.getReport_date());
		M_UNCONS_INVEST_Detail_Entity detailEntity;
		if (existingDetailList.isEmpty()) {
			detailEntity = new M_UNCONS_INVEST_Detail_Entity();
			detailEntity.setReport_date(updatedEntity.getReport_date());
		} else {
			detailEntity = existingDetailList.get(0);
		}

		try {
			// 1️⃣ Loop from R11 to R15 and copy fields
			for (int i = 11; i <= 15; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "product", "amount", "percent_of_cet1_holding",
						"percent_of_additional_tier_1_holding", "percent_of_tier_2_holding" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(getterName);
						Object newValue = getter.invoke(updatedEntity);
						Object existingValue = getter.invoke(existingSummary);

						// --- FIX: Normalize nulls vs empty strings to prevent audit bloat ---
						String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
						String newValStr = (newValue == null) ? "" : newValue.toString().trim();

						if (currentValStr.equals(newValStr)) {
							continue;
						}

						Method summarySetter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());
						summarySetter.invoke(existingSummary, newValue);

						Method detailSetter = M_UNCONS_INVEST_Detail_Entity.class.getMethod(setterName,
								getter.getReturnType());
						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// 2️⃣ Handle R15 totals
			String[] totalFields = { "product", "amount", "percent_of_cet1_holding",
					"percent_of_additional_tier_1_holding", "percent_of_tier_2_holding" };

			for (String field : totalFields) {
				String getterName = "getR15_" + field;
				String setterName = "setR15_" + field;

				try {
					Method getter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(getterName);
					Object newValue = getter.invoke(updatedEntity);
					Object existingValue = getter.invoke(existingSummary);

					// --- FIX: Normalize nulls vs empty strings ---
					String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
					String newValStr = (newValue == null) ? "" : newValue.toString().trim();

					if (currentValStr.equals(newValStr)) {
						continue;
					}

					Method summarySetter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(setterName,
							getter.getReturnType());
					summarySetter.invoke(existingSummary, newValue);

					Method detailSetter = M_UNCONS_INVEST_Detail_Entity.class.getMethod(setterName,
							getter.getReturnType());
					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Evaluate the actual changes calculated post-normalization
		String changes = auditService.getChanges(oldcopy, existingSummary);
		System.out.println("M_UNCONS_INVEST Changes Length = " + changes.length());

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables
		saveSummary(existingSummary);
		saveDetail(detailEntity);

		// Only invoke audit logger if actual physical modifications exist
		if (changes != null && !changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
					"M_UNCONS_INVEST Summary Screen", "BRRS_M_UNCONS_INVEST_SUMMARY");
		}

		System.out.println("Update completed successfully");
	}

	@Transactional
	public void updateReport2(M_UNCONS_INVEST_Summary_Entity updatedEntity) {
		System.out.println("Came to services 2");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// 🔹 Fetch existing SUMMARY
		List<M_UNCONS_INVEST_Summary_Entity> existingSummaryList = getSummaryByDate(updatedEntity.getReport_date());
		if (existingSummaryList.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}
		M_UNCONS_INVEST_Summary_Entity existingSummary = existingSummaryList.get(0);
		// 🔹 Create Audit Copy before editing
		M_UNCONS_INVEST_Summary_Entity oldcopy = new M_UNCONS_INVEST_Summary_Entity();
		BeanUtils.copyProperties(existingSummary, oldcopy);
		// 🔹 Fetch or create DETAIL
		List<M_UNCONS_INVEST_Detail_Entity> existingDetailList = getDetailByDate(updatedEntity.getReport_date());
		M_UNCONS_INVEST_Detail_Entity detailEntity;
		if (existingDetailList.isEmpty()) {
			detailEntity = new M_UNCONS_INVEST_Detail_Entity();
			detailEntity.setReport_date(updatedEntity.getReport_date());
		} else {
			detailEntity = existingDetailList.get(0);
		}

		try {
			// 1️⃣ Loop from R22 to R24 and copy fields
			for (int i = 22; i <= 24; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "product", "accuulated_equity_interest_5", "assets", "liabilities", "revenue",
						"profit_or_loss", "unreg_share_of_loss", "cumulative_unreg_share_of_loss" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(getterName);
						Object newValue = getter.invoke(updatedEntity);
						Object existingValue = getter.invoke(existingSummary);

						// --- FIX: Normalize nulls vs empty strings to prevent audit bloat ---
						String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
						String newValStr = (newValue == null) ? "" : newValue.toString().trim();

						if (currentValStr.equals(newValStr)) {
							continue;
						}

						Method summarySetter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());
						summarySetter.invoke(existingSummary, newValue);

						Method detailSetter = M_UNCONS_INVEST_Detail_Entity.class.getMethod(setterName,
								getter.getReturnType());
						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Evaluate the actual changes calculated post-normalization
		String changes = auditService.getChanges(oldcopy, existingSummary);
		System.out.println("M_UNCONS_INVEST Changes Length = " + changes.length());

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables
		saveSummary(existingSummary);
		saveDetail(detailEntity);

		// Only invoke audit logger if actual physical modifications exist
		if (changes != null && !changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
					"M_UNCONS_INVEST Summary Screen", "BRRS_M_UNCONS_INVEST_SUMMARY");
		}

		System.out.println("Update completed successfully");
	}

	@Transactional
	public void updateReport3(M_UNCONS_INVEST_Summary_Entity updatedEntity) {

		System.out.println("Came to services 3");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// 🔹 Fetch existing SUMMARY
		List<M_UNCONS_INVEST_Summary_Entity> existingSummaryList = getSummaryByDate(updatedEntity.getReport_date());
		if (existingSummaryList.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}
		M_UNCONS_INVEST_Summary_Entity existingSummary = existingSummaryList.get(0);
		// 🔹 Create Audit Copy before editing
		M_UNCONS_INVEST_Summary_Entity oldcopy = new M_UNCONS_INVEST_Summary_Entity();
		BeanUtils.copyProperties(existingSummary, oldcopy);
		// 🔹 Fetch or create DETAIL
		List<M_UNCONS_INVEST_Detail_Entity> existingDetailList = getDetailByDate(updatedEntity.getReport_date());
		M_UNCONS_INVEST_Detail_Entity detailEntity;
		if (existingDetailList.isEmpty()) {
			detailEntity = new M_UNCONS_INVEST_Detail_Entity();
			detailEntity.setReport_date(updatedEntity.getReport_date());
		} else {
			detailEntity = existingDetailList.get(0);
		}

		try {
			// 🔁 LOOP FOR R29 ONLY
			int i = 29;
			String prefix = "R" + i + "_";

			String[] fields = { "product", "fair_value" };

			for (String field : fields) {

				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					Method getter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(getterName);
					Object newValue = getter.invoke(updatedEntity);
					Object existingValue = getter.invoke(existingSummary);

					// --- FIX: Normalize nulls vs empty strings to prevent audit bloat ---
					String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
					String newValStr = (newValue == null) ? "" : newValue.toString().trim();

					if (currentValStr.equals(newValStr)) {
						continue;
					}

					Method summarySetter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(setterName,
							getter.getReturnType());
					summarySetter.invoke(existingSummary, newValue);

					Method detailSetter = M_UNCONS_INVEST_Detail_Entity.class.getMethod(setterName,
							getter.getReturnType());
					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Evaluate the actual changes calculated post-normalization
		String changes = auditService.getChanges(oldcopy, existingSummary);
		System.out.println("M_UNCONS_INVEST Changes Length = " + changes.length());

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables
		saveSummary(existingSummary);
		saveDetail(detailEntity);

		// Only invoke audit logger if actual physical modifications exist
		if (changes != null && !changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
					"M_UNCONS_INVEST Summary Screen", "BRRS_M_UNCONS_INVEST_SUMMARY");
		}

		System.out.println("Update completed successfully");
	}

	@Transactional
	public void updateReport4(M_UNCONS_INVEST_Summary_Entity updatedEntity) {
		System.out.println("Came to services 4");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// 🔹 Fetch existing SUMMARY
		List<M_UNCONS_INVEST_Summary_Entity> existingSummaryList = getSummaryByDate(updatedEntity.getReport_date());
		if (existingSummaryList.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}
		M_UNCONS_INVEST_Summary_Entity existingSummary = existingSummaryList.get(0);
		// 🔹 Create Audit Copy before editing
		M_UNCONS_INVEST_Summary_Entity oldcopy = new M_UNCONS_INVEST_Summary_Entity();
		BeanUtils.copyProperties(existingSummary, oldcopy);
		// 🔹 Fetch or create DETAIL
		List<M_UNCONS_INVEST_Detail_Entity> existingDetailList = getDetailByDate(updatedEntity.getReport_date());
		M_UNCONS_INVEST_Detail_Entity detailEntity;
		if (existingDetailList.isEmpty()) {
			detailEntity = new M_UNCONS_INVEST_Detail_Entity();
			detailEntity.setReport_date(updatedEntity.getReport_date());
		} else {
			detailEntity = existingDetailList.get(0);
		}

		try {
			// 1️⃣ Loop from R35 to R38 and copy fields
			for (int i = 35; i <= 38; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "product", "company", "jurisdiction_of_incorp_1", "jurisdiction_of_incorp_2",
						"line_of_business", "currency", "share_capital", "accumulated_equity_interest" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(getterName);
						Object newValue = getter.invoke(updatedEntity);
						Object existingValue = getter.invoke(existingSummary);

						// --- FIX: Normalize nulls vs empty strings to prevent audit bloat ---
						String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
						String newValStr = (newValue == null) ? "" : newValue.toString().trim();

						if (currentValStr.equals(newValStr)) {
							continue;
						}

						Method summarySetter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());
						summarySetter.invoke(existingSummary, newValue);

						Method detailSetter = M_UNCONS_INVEST_Detail_Entity.class.getMethod(setterName,
								getter.getReturnType());
						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Evaluate the actual changes calculated post-normalization
		String changes = auditService.getChanges(oldcopy, existingSummary);
		System.out.println("M_UNCONS_INVEST Changes Length = " + changes.length());

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables
		saveSummary(existingSummary);
		saveDetail(detailEntity);

		// Only invoke audit logger if actual physical modifications exist
		if (changes != null && !changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
					"M_UNCONS_INVEST Summary Screen", "BRRS_M_UNCONS_INVEST_SUMMARY");
		}

		System.out.println("Update completed successfully");
	}

	public void updateResubReport(M_UNCONS_INVEST_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = findMaxResubVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_UNCONS_INVEST_Resub_Summary_Entity resubSummary = new M_UNCONS_INVEST_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_UNCONS_INVEST_Resub_Detail_Entity resubDetail = new M_UNCONS_INVEST_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_UNCONS_INVEST_Archival_Summary_Entity archSummary = new M_UNCONS_INVEST_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_UNCONS_INVEST_Archival_Detail_Entity archDetail = new M_UNCONS_INVEST_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		insertResubSummary(resubSummary);
		insertResubDetail(resubDetail);

		insertArchivalSummary(archSummary);
		insertArchivalDetail(archDetail);
	}

//////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////	
/// Report Date | Report Version | Domain
/// RESUB VIEW
	public List<Object[]> getM_UNCONS_INVESTResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_UNCONS_INVEST_Archival_Summary_Entity> latestArchivalList = getArchivalSummaryWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_UNCONS_INVEST_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_UNCONS_INVEST Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

// Archival View
	public List<Object[]> getM_UNCONS_INVESTArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_UNCONS_INVEST_Archival_Summary_Entity> repoData = getArchivalSummaryWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_UNCONS_INVEST_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_UNCONS_INVEST_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_UNCONS_INVEST Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] BRRS_M_UNCONS_INVESTExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
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
				return getExcelM_UNCONS_INVESTARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
						format, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_UNCONS_INVESTResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						format, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_UNCONS_INVESTEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} else {

				// Fetch data

				List<M_UNCONS_INVEST_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_UNCONS_INVEST report. Returning empty result.");
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

					int startRow = 6;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_UNCONS_INVEST_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							Cell R12Cell = row.createCell(2);

							if (record.getReport_date() != null) {

								R12Cell.setCellValue(record.getReport_date());

								R12Cell.setCellStyle(dateStyle);

							} else {

								R12Cell.setCellValue("");

								R12Cell.setCellStyle(textStyle);
							}
							row = sheet.getRow(10);
//NORMAL

							// row11
							// Column D
							Cell cell3 = row.getCell(3);
							if (record.getR11_amount() != null) {
								cell3.setCellValue(record.getR11_amount().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);

							}

							// row11
							// Column E
							Cell cell4 = row.createCell(4);
							if (record.getR11_percent_of_cet1_holding() != null) {
								cell4.setCellValue(record.getR11_percent_of_cet1_holding().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row11
							// Column F
							Cell cell5 = row.createCell(5);
							if (record.getR11_percent_of_additional_tier_1_holding() != null) {
								cell5.setCellValue(record.getR11_percent_of_additional_tier_1_holding().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row11
							// Column G
							Cell cell6 = row.createCell(6);
							if (record.getR11_percent_of_tier_2_holding() != null) {
								cell6.setCellValue(record.getR11_percent_of_tier_2_holding().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(11);
							// Column D
							cell3 = row.getCell(3);
							if (record.getR12_amount() != null) {
								cell3.setCellValue(record.getR12_amount().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);

							}

							// row12
							// Column E
							cell4 = row.createCell(4);
							if (record.getR12_percent_of_cet1_holding() != null) {
								cell4.setCellValue(record.getR12_percent_of_cet1_holding().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row12
							// Column F
							cell5 = row.createCell(5);
							if (record.getR12_percent_of_additional_tier_1_holding() != null) {
								cell5.setCellValue(record.getR12_percent_of_additional_tier_1_holding().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row12
							// Column G
							cell6 = row.createCell(6);
							if (record.getR12_percent_of_tier_2_holding() != null) {
								cell6.setCellValue(record.getR12_percent_of_tier_2_holding().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(12);
							// Column D
							cell3 = row.getCell(3);
							if (record.getR13_amount() != null) {
								cell3.setCellValue(record.getR13_amount().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);

							}

							// row13
							// Column E
							cell4 = row.createCell(4);
							if (record.getR13_percent_of_cet1_holding() != null) {
								cell4.setCellValue(record.getR13_percent_of_cet1_holding().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row13
							// Column F
							cell5 = row.createCell(5);
							if (record.getR13_percent_of_additional_tier_1_holding() != null) {
								cell5.setCellValue(record.getR13_percent_of_additional_tier_1_holding().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row13
							// Column G
							cell6 = row.createCell(6);
							if (record.getR13_percent_of_tier_2_holding() != null) {
								cell6.setCellValue(record.getR13_percent_of_tier_2_holding().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row14
							row = sheet.getRow(13);
							// Column D
							cell3 = row.getCell(3);
							if (record.getR14_amount() != null) {
								cell3.setCellValue(record.getR14_amount().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);

							}

							// row14
							// Column E
							cell4 = row.createCell(4);
							if (record.getR14_percent_of_cet1_holding() != null) {
								cell4.setCellValue(record.getR14_percent_of_cet1_holding().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row14
							// Column F
							cell5 = row.createCell(5);
							if (record.getR14_percent_of_additional_tier_1_holding() != null) {
								cell5.setCellValue(record.getR14_percent_of_additional_tier_1_holding().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row14
							// Column G
							cell6 = row.createCell(6);
							if (record.getR14_percent_of_tier_2_holding() != null) {
								cell6.setCellValue(record.getR14_percent_of_tier_2_holding().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row15
							row = sheet.getRow(14);

							// row15
							// Column E
							cell4 = row.createCell(4);
							if (record.getR15_percent_of_cet1_holding() != null) {
								cell4.setCellValue(record.getR15_percent_of_cet1_holding().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row15
							// Column F
							cell5 = row.createCell(5);
							if (record.getR15_percent_of_additional_tier_1_holding() != null) {
								cell5.setCellValue(record.getR15_percent_of_additional_tier_1_holding().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row15
							// Column G
							cell6 = row.createCell(6);
							if (record.getR15_percent_of_tier_2_holding() != null) {
								cell6.setCellValue(record.getR15_percent_of_tier_2_holding().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
							// row22
							row = sheet.getRow(21);
							// Column C
							Cell cell2 = row.getCell(2);
							if (record.getR22_accuulated_equity_interest_5() != null) {
								cell2.setCellValue(record.getR22_accuulated_equity_interest_5().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row22
							// Column D
							cell3 = row.createCell(3);
							if (record.getR22_assets() != null) {
								cell3.setCellValue(record.getR22_assets().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// row22
							// Column E
							cell4 = row.createCell(4);
							if (record.getR22_liabilities() != null) {
								cell4.setCellValue(record.getR22_liabilities().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row22
							// Column F
							cell5 = row.createCell(5);
							if (record.getR22_revenue() != null) {
								cell5.setCellValue(record.getR22_revenue().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row22
							// Column G
							cell6 = row.createCell(6);
							if (record.getR22_profit_or_loss() != null) {
								cell6.setCellValue(record.getR22_profit_or_loss().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row22
							// Column H
							Cell cell7 = row.createCell(7);
							if (record.getR22_unreg_share_of_loss() != null) {
								cell7.setCellValue(record.getR22_unreg_share_of_loss().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// row22
							// Column I
							Cell cell8 = row.createCell(8);
							if (record.getR22_cumulative_unreg_share_of_loss() != null) {
								cell8.setCellValue(record.getR22_cumulative_unreg_share_of_loss().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// row23
							row = sheet.getRow(22);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR23_accuulated_equity_interest_5() != null) {
								cell2.setCellValue(record.getR23_accuulated_equity_interest_5().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row23
							// Column D
							cell3 = row.createCell(3);
							if (record.getR23_assets() != null) {
								cell3.setCellValue(record.getR23_assets().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// row23
							// Column E
							cell4 = row.createCell(4);
							if (record.getR23_liabilities() != null) {
								cell4.setCellValue(record.getR23_liabilities().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row23
							// Column F
							cell5 = row.createCell(5);
							if (record.getR23_revenue() != null) {
								cell5.setCellValue(record.getR23_revenue().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row23
							// Column G
							cell6 = row.createCell(6);
							if (record.getR23_profit_or_loss() != null) {
								cell6.setCellValue(record.getR23_profit_or_loss().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row23
							// Column H
							cell7 = row.createCell(7);
							if (record.getR23_unreg_share_of_loss() != null) {
								cell7.setCellValue(record.getR23_unreg_share_of_loss().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// row23
							// Column I
							cell8 = row.createCell(8);
							if (record.getR23_cumulative_unreg_share_of_loss() != null) {
								cell8.setCellValue(record.getR23_cumulative_unreg_share_of_loss().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(23);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR24_accuulated_equity_interest_5() != null) {
								cell2.setCellValue(record.getR24_accuulated_equity_interest_5().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row24
							// Column D
							cell3 = row.createCell(3);
							if (record.getR24_assets() != null) {
								cell3.setCellValue(record.getR24_assets().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// row24
							// Column E
							cell4 = row.createCell(4);
							if (record.getR24_liabilities() != null) {
								cell4.setCellValue(record.getR24_liabilities().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row24
							// Column F
							cell5 = row.createCell(5);
							if (record.getR24_revenue() != null) {
								cell5.setCellValue(record.getR24_revenue().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row24
							// Column G
							cell6 = row.createCell(6);
							if (record.getR24_profit_or_loss() != null) {
								cell6.setCellValue(record.getR24_profit_or_loss().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row24
							// Column H
							cell7 = row.createCell(7);
							if (record.getR24_unreg_share_of_loss() != null) {
								cell7.setCellValue(record.getR24_unreg_share_of_loss().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// row24
							// Column I
							cell8 = row.createCell(8);
							if (record.getR24_cumulative_unreg_share_of_loss() != null) {
								cell8.setCellValue(record.getR24_cumulative_unreg_share_of_loss().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// row29
							row = sheet.getRow(28);
							// Column G
							cell6 = row.getCell(6);
							if (record.getR29_fair_value() != null) {
								cell6.setCellValue(record.getR29_fair_value().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);

							}

							// row35
							row = sheet.getRow(34);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR35_company() != null) {
								cell2.setCellValue(record.getR35_company().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row35
							// Column D
							cell3 = row.createCell(3);
							if (record.getR35_jurisdiction_of_incorp_1() != null) {
								cell3.setCellValue(record.getR35_jurisdiction_of_incorp_1().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// row35
							// Column E
							cell4 = row.createCell(4);
							if (record.getR35_jurisdiction_of_incorp_2() != null) {
								cell4.setCellValue(record.getR35_jurisdiction_of_incorp_2().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row35
							// Column F
							cell5 = row.createCell(5);
							if (record.getR35_line_of_business() != null) {
								cell5.setCellValue(record.getR35_line_of_business().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row35
							// Column G
							cell6 = row.createCell(6);
							if (record.getR35_currency() != null) {
								cell6.setCellValue(record.getR35_currency().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row35
							// Column H
							cell7 = row.createCell(7);
							if (record.getR35_share_capital() != null) {
								cell7.setCellValue(record.getR35_share_capital().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// row35
							// Column I
							cell8 = row.createCell(8);
							if (record.getR35_accumulated_equity_interest() != null) {
								cell8.setCellValue(record.getR35_accumulated_equity_interest().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// row36
							row = sheet.getRow(35);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR36_company() != null) {
								cell2.setCellValue(record.getR36_company().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row36
							// Column D
							cell3 = row.createCell(3);
							if (record.getR36_jurisdiction_of_incorp_1() != null) {
								cell3.setCellValue(record.getR36_jurisdiction_of_incorp_1().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// row36
							// Column E
							cell4 = row.createCell(4);
							if (record.getR36_jurisdiction_of_incorp_2() != null) {
								cell4.setCellValue(record.getR36_jurisdiction_of_incorp_2().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row36
							// Column F
							cell5 = row.createCell(5);
							if (record.getR36_line_of_business() != null) {
								cell5.setCellValue(record.getR36_line_of_business().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row36
							// Column G
							cell6 = row.createCell(6);
							if (record.getR36_currency() != null) {
								cell6.setCellValue(record.getR36_currency().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row36
							// Column H
							cell7 = row.createCell(7);
							if (record.getR36_share_capital() != null) {
								cell7.setCellValue(record.getR36_share_capital().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// row36
							// Column I
							cell8 = row.createCell(8);
							if (record.getR36_accumulated_equity_interest() != null) {
								cell8.setCellValue(record.getR36_accumulated_equity_interest().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// row37
							row = sheet.getRow(36);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR37_company() != null) {
								cell2.setCellValue(record.getR37_company().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row37
							// Column D
							cell3 = row.createCell(3);
							if (record.getR37_jurisdiction_of_incorp_1() != null) {
								cell3.setCellValue(record.getR37_jurisdiction_of_incorp_1().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// row37
							// Column E
							cell4 = row.createCell(4);
							if (record.getR37_jurisdiction_of_incorp_2() != null) {
								cell4.setCellValue(record.getR37_jurisdiction_of_incorp_2().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row37
							// Column F
							cell5 = row.createCell(5);
							if (record.getR37_line_of_business() != null) {
								cell5.setCellValue(record.getR37_line_of_business().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row37
							// Column G
							cell6 = row.createCell(6);
							if (record.getR37_currency() != null) {
								cell6.setCellValue(record.getR37_currency().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row37
							// Column H
							cell7 = row.createCell(7);
							if (record.getR37_share_capital() != null) {
								cell7.setCellValue(record.getR37_share_capital().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// row37
							// Column I
							cell8 = row.createCell(8);
							if (record.getR37_accumulated_equity_interest() != null) {
								cell8.setCellValue(record.getR37_accumulated_equity_interest().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// row38
							row = sheet.getRow(37);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR38_company() != null) {
								cell2.setCellValue(record.getR38_company().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row38
							// Column D
							cell3 = row.createCell(3);
							if (record.getR38_jurisdiction_of_incorp_1() != null) {
								cell3.setCellValue(record.getR38_jurisdiction_of_incorp_1().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// row38
							// Column E
							cell4 = row.createCell(4);
							if (record.getR38_jurisdiction_of_incorp_2() != null) {
								cell4.setCellValue(record.getR38_jurisdiction_of_incorp_2().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row38
							// Column F
							cell5 = row.createCell(5);
							if (record.getR38_line_of_business() != null) {
								cell5.setCellValue(record.getR38_line_of_business().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row38
							// Column G
							cell6 = row.createCell(6);
							if (record.getR38_currency() != null) {
								cell6.setCellValue(record.getR38_currency().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row38
							// Column H
							cell7 = row.createCell(7);
							if (record.getR38_share_capital() != null) {
								cell7.setCellValue(record.getR38_share_capital().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// row38
							// Column I
							cell8 = row.createCell(8);
							if (record.getR38_accumulated_equity_interest() != null) {
								cell8.setCellValue(record.getR38_accumulated_equity_interest().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}
							// Normal

						}
						workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_UNCONS_INVEST SUMMARY", null,
								"BRRS_M_UNCONS_INVEST_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_UNCONS_INVESTEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_UNCONS_INVESTEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype,
						type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_UNCONS_INVESTResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype,
						type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_UNCONS_INVEST_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_UNCONS_INVEST report. Returning empty result.");
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

				int startRow = 6;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_UNCONS_INVEST_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
//EMAIL
						Cell R12Cell = row.createCell(3);

						if (record.getReport_date() != null) {

							R12Cell.setCellValue(record.getReport_date());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
						}
						row = sheet.getRow(9);
						// row11
						// Column D
						Cell cell3 = row.getCell(4);
						if (record.getR11_amount() != null) {
							cell3.setCellValue(record.getR11_amount().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);

						}

						// row14
						row = sheet.getRow(12);
						// Column D
						cell3 = row.getCell(4);
						if (record.getR14_amount() != null) {
							cell3.setCellValue(record.getR14_amount().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);

						}

						// row15
						row = sheet.getRow(13);
						// Column D
						cell3 = row.getCell(4);
						if (record.getR15_amount() != null) {
							cell3.setCellValue(record.getR15_amount().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);

						}

						// row22
						row = sheet.getRow(20);
						// Column C
						Cell cell2 = row.getCell(3);
						if (record.getR22_accuulated_equity_interest_5() != null) {
							cell2.setCellValue(record.getR22_accuulated_equity_interest_5().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);

						}

						// row22
						// Column D
						cell3 = row.createCell(4);
						if (record.getR22_assets() != null) {
							cell3.setCellValue(record.getR22_assets().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// row22
						// Column E
						Cell cell4 = row.createCell(5);
						if (record.getR22_liabilities() != null) {
							cell4.setCellValue(record.getR22_liabilities().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// row22
						// Column F
						Cell cell5 = row.createCell(6);
						if (record.getR22_revenue() != null) {
							cell5.setCellValue(record.getR22_revenue().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// row22
						// Column G
						Cell cell6 = row.createCell(7);
						if (record.getR22_profit_or_loss() != null) {
							cell6.setCellValue(record.getR22_profit_or_loss().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row22
						// Column H
						Cell cell7 = row.createCell(8);
						if (record.getR22_unreg_share_of_loss() != null) {
							cell7.setCellValue(record.getR22_unreg_share_of_loss().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// row22
						// Column I
						Cell cell8 = row.createCell(9);
						if (record.getR22_cumulative_unreg_share_of_loss() != null) {
							cell8.setCellValue(record.getR22_cumulative_unreg_share_of_loss().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// row23
						row = sheet.getRow(21);
						// Column C
						cell2 = row.getCell(3);
						if (record.getR23_accuulated_equity_interest_5() != null) {
							cell2.setCellValue(record.getR23_accuulated_equity_interest_5().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);

						}

						// row23
						// Column H
						cell7 = row.createCell(8);
						if (record.getR23_unreg_share_of_loss() != null) {
							cell7.setCellValue(record.getR23_unreg_share_of_loss().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// row23
						// Column I
						cell8 = row.createCell(9);
						if (record.getR23_cumulative_unreg_share_of_loss() != null) {
							cell8.setCellValue(record.getR23_cumulative_unreg_share_of_loss().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// row24
						row = sheet.getRow(22);
						// Column C
						cell2 = row.getCell(3);
						if (record.getR24_accuulated_equity_interest_5() != null) {
							cell2.setCellValue(record.getR24_accuulated_equity_interest_5().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);

						}

						// row24
						// Column D
						cell3 = row.createCell(4);
						if (record.getR24_assets() != null) {
							cell3.setCellValue(record.getR24_assets().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// row24
						// Column E
						cell4 = row.createCell(5);
						if (record.getR24_liabilities() != null) {
							cell4.setCellValue(record.getR24_liabilities().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// row24
						// Column F
						cell5 = row.createCell(6);
						if (record.getR24_revenue() != null) {
							cell5.setCellValue(record.getR24_revenue().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// row24
						// Column G
						cell6 = row.createCell(7);
						if (record.getR24_profit_or_loss() != null) {
							cell6.setCellValue(record.getR24_profit_or_loss().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row24
						// Column H
						cell7 = row.createCell(8);
						if (record.getR24_unreg_share_of_loss() != null) {
							cell7.setCellValue(record.getR24_unreg_share_of_loss().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// row24
						// Column I
						cell8 = row.createCell(9);
						if (record.getR24_cumulative_unreg_share_of_loss() != null) {
							cell8.setCellValue(record.getR24_cumulative_unreg_share_of_loss().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// row29
						row = sheet.getRow(27);
						// Column G
						cell6 = row.getCell(7);
						if (record.getR29_fair_value() != null) {
							cell6.setCellValue(record.getR29_fair_value().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);

						}

						// row35
						row = sheet.getRow(33);
						// Column C
						cell2 = row.getCell(3);
						if (record.getR35_company() != null) {
							cell2.setCellValue(record.getR35_company().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);

						}

						// row35
						// Column D
						cell3 = row.createCell(4);
						if (record.getR35_jurisdiction_of_incorp_1() != null) {
							cell3.setCellValue(record.getR35_jurisdiction_of_incorp_1().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// row35
						// Column E
						cell4 = row.createCell(5);
						if (record.getR35_jurisdiction_of_incorp_2() != null) {
							cell4.setCellValue(record.getR35_jurisdiction_of_incorp_2().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// row35
						// Column F
						cell5 = row.createCell(6);
						if (record.getR35_line_of_business() != null) {
							cell5.setCellValue(record.getR35_line_of_business().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// row35
						// Column G
						cell6 = row.createCell(7);
						if (record.getR35_currency() != null) {
							cell6.setCellValue(record.getR35_currency().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row35
						// Column H
						cell7 = row.createCell(8);
						if (record.getR35_share_capital() != null) {
							cell7.setCellValue(record.getR35_share_capital().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// row35
						// Column I
						cell8 = row.createCell(9);
						if (record.getR35_accumulated_equity_interest() != null) {
							cell8.setCellValue(record.getR35_accumulated_equity_interest().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// row36
						row = sheet.getRow(34);
						// Column C
						cell2 = row.getCell(3);
						if (record.getR36_company() != null) {
							cell2.setCellValue(record.getR36_company().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);

						}

						// row36
						// Column H
						cell7 = row.createCell(8);
						if (record.getR36_share_capital() != null) {
							cell7.setCellValue(record.getR36_share_capital().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// row36
						// Column I
						cell8 = row.createCell(9);
						if (record.getR36_accumulated_equity_interest() != null) {
							cell8.setCellValue(record.getR36_accumulated_equity_interest().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// row37
						row = sheet.getRow(35);
						// Column C
						cell2 = row.getCell(3);
						if (record.getR37_company() != null) {
							cell2.setCellValue(record.getR37_company().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);

						}

						// row37
						// Column D
						cell3 = row.createCell(4);
						if (record.getR37_jurisdiction_of_incorp_1() != null) {
							cell3.setCellValue(record.getR37_jurisdiction_of_incorp_1().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// row37
						// Column E
						cell4 = row.createCell(5);
						if (record.getR37_jurisdiction_of_incorp_2() != null) {
							cell4.setCellValue(record.getR37_jurisdiction_of_incorp_2().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// row37
						// Column F
						cell5 = row.createCell(6);
						if (record.getR37_line_of_business() != null) {
							cell5.setCellValue(record.getR37_line_of_business().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// row37
						// Column G
						cell6 = row.createCell(7);
						if (record.getR37_currency() != null) {
							cell6.setCellValue(record.getR37_currency().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row37
						// Column H
						cell7 = row.createCell(8);
						if (record.getR37_share_capital() != null) {
							cell7.setCellValue(record.getR37_share_capital().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// row37
						// Column I
						cell8 = row.createCell(9);
						if (record.getR37_accumulated_equity_interest() != null) {
							cell8.setCellValue(record.getR37_accumulated_equity_interest().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// row38
						row = sheet.getRow(36);
						// Column C
						cell2 = row.getCell(3);
						if (record.getR38_company() != null) {
							cell2.setCellValue(record.getR38_company().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);

						}

						// row38
						// Column D
						cell3 = row.createCell(4);
						if (record.getR38_jurisdiction_of_incorp_1() != null) {
							cell3.setCellValue(record.getR38_jurisdiction_of_incorp_1().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// row38
						// Column E
						cell4 = row.createCell(5);
						if (record.getR38_jurisdiction_of_incorp_2() != null) {
							cell4.setCellValue(record.getR38_jurisdiction_of_incorp_2().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// row38
						// Column F
						cell5 = row.createCell(6);
						if (record.getR38_line_of_business() != null) {
							cell5.setCellValue(record.getR38_line_of_business().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// row38
						// Column G
						cell6 = row.createCell(7);
						if (record.getR38_currency() != null) {
							cell6.setCellValue(record.getR38_currency().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row38
						// Column H
						cell7 = row.createCell(8);
						if (record.getR38_share_capital() != null) {
							cell7.setCellValue(record.getR38_share_capital().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// row38
						// Column I
						cell8 = row.createCell(9);
						if (record.getR38_accumulated_equity_interest() != null) {
							cell8.setCellValue(record.getR38_accumulated_equity_interest().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}
						// Email

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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_UNCONS_INVEST EMAIL SUMMARY", null,
							"BRRS_M_UNCONS_INVEST_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	public byte[] getExcelM_UNCONS_INVESTARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_UNCONS_INVESTEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype,
						type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_UNCONS_INVEST_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_UNCONS_INVEST report. Returning empty result.");
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
					M_UNCONS_INVEST_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//NORMAL
					Cell R12Cell = row.createCell(2);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(10);

					// row11
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR11_amount() != null) {
						cell3.setCellValue(record.getR11_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR11_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR11_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row11
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR11_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR11_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR12_amount() != null) {
						cell3.setCellValue(record.getR12_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row12
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR12_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR12_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR12_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR13_amount() != null) {
						cell3.setCellValue(record.getR13_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR13_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR13_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR13_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR14_amount() != null) {
						cell3.setCellValue(record.getR14_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR14_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR14_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR14_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);

					// row15
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR15_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR15_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					// Column G
					cell6 = row.createCell(6);
					if (record.getR15_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR15_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// row22
					row = sheet.getRow(21);
					// Column C
					Cell cell2 = row.getCell(2);
					if (record.getR22_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR22_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_assets() != null) {
						cell3.setCellValue(record.getR22_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_liabilities() != null) {
						cell4.setCellValue(record.getR22_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_revenue() != null) {
						cell5.setCellValue(record.getR22_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_profit_or_loss() != null) {
						cell6.setCellValue(record.getR22_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row22
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR22_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR22_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row22
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR22_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR22_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR23_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR23_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_assets() != null) {
						cell3.setCellValue(record.getR23_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_liabilities() != null) {
						cell4.setCellValue(record.getR23_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_revenue() != null) {
						cell5.setCellValue(record.getR23_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_profit_or_loss() != null) {
						cell6.setCellValue(record.getR23_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row23
					// Column H
					cell7 = row.createCell(7);
					if (record.getR23_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR23_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row23
					// Column I
					cell8 = row.createCell(8);
					if (record.getR23_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR23_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR24_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR24_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_assets() != null) {
						cell3.setCellValue(record.getR24_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_liabilities() != null) {
						cell4.setCellValue(record.getR24_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_revenue() != null) {
						cell5.setCellValue(record.getR24_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_profit_or_loss() != null) {
						cell6.setCellValue(record.getR24_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row24
					// Column H
					cell7 = row.createCell(7);
					if (record.getR24_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR24_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row24
					// Column I
					cell8 = row.createCell(8);
					if (record.getR24_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR24_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column G
					cell6 = row.getCell(6);
					if (record.getR29_fair_value() != null) {
						cell6.setCellValue(record.getR29_fair_value().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);

					}

					// row35
					row = sheet.getRow(34);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR35_company() != null) {
						cell2.setCellValue(record.getR35_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR35_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row35
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR35_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row35
					// Column F
					cell5 = row.createCell(5);
					if (record.getR35_line_of_business() != null) {
						cell5.setCellValue(record.getR35_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					// Column G
					cell6 = row.createCell(6);
					if (record.getR35_currency() != null) {
						cell6.setCellValue(record.getR35_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row35
					// Column H
					cell7 = row.createCell(7);
					if (record.getR35_share_capital() != null) {
						cell7.setCellValue(record.getR35_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row35
					// Column I
					cell8 = row.createCell(8);
					if (record.getR35_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR35_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR36_company() != null) {
						cell2.setCellValue(record.getR36_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row36
					// Column D
					cell3 = row.createCell(3);
					if (record.getR36_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR36_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row36
					// Column E
					cell4 = row.createCell(4);
					if (record.getR36_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR36_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record.getR36_line_of_business() != null) {
						cell5.setCellValue(record.getR36_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row36
					// Column G
					cell6 = row.createCell(6);
					if (record.getR36_currency() != null) {
						cell6.setCellValue(record.getR36_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row36
					// Column H
					cell7 = row.createCell(7);
					if (record.getR36_share_capital() != null) {
						cell7.setCellValue(record.getR36_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row36
					// Column I
					cell8 = row.createCell(8);
					if (record.getR36_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR36_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR37_company() != null) {
						cell2.setCellValue(record.getR37_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR37_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row37
					// Column E
					cell4 = row.createCell(4);
					if (record.getR37_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR37_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row37
					// Column F
					cell5 = row.createCell(5);
					if (record.getR37_line_of_business() != null) {
						cell5.setCellValue(record.getR37_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row37
					// Column G
					cell6 = row.createCell(6);
					if (record.getR37_currency() != null) {
						cell6.setCellValue(record.getR37_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row37
					// Column H
					cell7 = row.createCell(7);
					if (record.getR37_share_capital() != null) {
						cell7.setCellValue(record.getR37_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row37
					// Column I
					cell8 = row.createCell(8);
					if (record.getR37_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR37_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR38_company() != null) {
						cell2.setCellValue(record.getR38_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row38
					// Column D
					cell3 = row.createCell(3);
					if (record.getR38_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR38_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row38
					// Column E
					cell4 = row.createCell(4);
					if (record.getR38_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR38_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row38
					// Column F
					cell5 = row.createCell(5);
					if (record.getR38_line_of_business() != null) {
						cell5.setCellValue(record.getR38_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row38
					// Column G
					cell6 = row.createCell(6);
					if (record.getR38_currency() != null) {
						cell6.setCellValue(record.getR38_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row38
					// Column H
					cell7 = row.createCell(7);
					if (record.getR38_share_capital() != null) {
						cell7.setCellValue(record.getR38_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row38
					// Column I
					cell8 = row.createCell(8);
					if (record.getR38_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR38_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Normal

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_UNCONS_INVEST ARCHIVAL SUMMARY", null,
						"BRRS_M_UNCONS_INVEST_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_UNCONS_INVESTEmailArchivalExcel(String filename, String reportId, String fromdate,
			String todate, String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_UNCONS_INVEST_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_UNCONS_INVEST report. Returning empty result.");
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
					M_UNCONS_INVEST_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//EMAIL
					Cell R12Cell = row.createCell(3);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(9);

					// row11
					// Column D
					Cell cell3 = row.getCell(4);
					if (record.getR11_amount() != null) {
						cell3.setCellValue(record.getR11_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row14
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(4);
					if (record.getR14_amount() != null) {
						cell3.setCellValue(record.getR14_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row15
					row = sheet.getRow(13);
					// Column D
					cell3 = row.getCell(4);
					if (record.getR15_amount() != null) {
						cell3.setCellValue(record.getR15_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row22
					row = sheet.getRow(20);
					// Column C
					Cell cell2 = row.getCell(3);
					if (record.getR22_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR22_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row22
					// Column D
					cell3 = row.createCell(4);
					if (record.getR22_assets() != null) {
						cell3.setCellValue(record.getR22_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row22
					// Column E
					Cell cell4 = row.createCell(5);
					if (record.getR22_liabilities() != null) {
						cell4.setCellValue(record.getR22_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					Cell cell5 = row.createCell(6);
					if (record.getR22_revenue() != null) {
						cell5.setCellValue(record.getR22_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					// Column G
					Cell cell6 = row.createCell(7);
					if (record.getR22_profit_or_loss() != null) {
						cell6.setCellValue(record.getR22_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row22
					// Column H
					Cell cell7 = row.createCell(8);
					if (record.getR22_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR22_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row22
					// Column I
					Cell cell8 = row.createCell(9);
					if (record.getR22_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR22_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(21);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR23_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR23_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row23
					// Column H
					cell7 = row.createCell(8);
					if (record.getR23_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR23_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row23
					// Column I
					cell8 = row.createCell(9);
					if (record.getR23_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR23_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(22);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR24_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR24_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row24
					// Column D
					cell3 = row.createCell(4);
					if (record.getR24_assets() != null) {
						cell3.setCellValue(record.getR24_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					// Column E
					cell4 = row.createCell(5);
					if (record.getR24_liabilities() != null) {
						cell4.setCellValue(record.getR24_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(6);
					if (record.getR24_revenue() != null) {
						cell5.setCellValue(record.getR24_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					// Column G
					cell6 = row.createCell(7);
					if (record.getR24_profit_or_loss() != null) {
						cell6.setCellValue(record.getR24_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row24
					// Column H
					cell7 = row.createCell(8);
					if (record.getR24_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR24_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row24
					// Column I
					cell8 = row.createCell(9);
					if (record.getR24_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR24_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(27);
					// Column G
					cell6 = row.getCell(7);
					if (record.getR29_fair_value() != null) {
						cell6.setCellValue(record.getR29_fair_value().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);

					}

					// row35
					row = sheet.getRow(33);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR35_company() != null) {
						cell2.setCellValue(record.getR35_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row35
					// Column D
					cell3 = row.createCell(4);
					if (record.getR35_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR35_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row35
					// Column E
					cell4 = row.createCell(5);
					if (record.getR35_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR35_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row35
					// Column F
					cell5 = row.createCell(6);
					if (record.getR35_line_of_business() != null) {
						cell5.setCellValue(record.getR35_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					// Column G
					cell6 = row.createCell(7);
					if (record.getR35_currency() != null) {
						cell6.setCellValue(record.getR35_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row35
					// Column H
					cell7 = row.createCell(8);
					if (record.getR35_share_capital() != null) {
						cell7.setCellValue(record.getR35_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row35
					// Column I
					cell8 = row.createCell(9);
					if (record.getR35_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR35_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(34);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR36_company() != null) {
						cell2.setCellValue(record.getR36_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row36
					// Column H
					cell7 = row.createCell(8);
					if (record.getR36_share_capital() != null) {
						cell7.setCellValue(record.getR36_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row36
					// Column I
					cell8 = row.createCell(9);
					if (record.getR36_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR36_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(35);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR37_company() != null) {
						cell2.setCellValue(record.getR37_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row37
					// Column D
					cell3 = row.createCell(4);
					if (record.getR37_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR37_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row37
					// Column E
					cell4 = row.createCell(5);
					if (record.getR37_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR37_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row37
					// Column F
					cell5 = row.createCell(6);
					if (record.getR37_line_of_business() != null) {
						cell5.setCellValue(record.getR37_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row37
					// Column G
					cell6 = row.createCell(7);
					if (record.getR37_currency() != null) {
						cell6.setCellValue(record.getR37_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row37
					// Column H
					cell7 = row.createCell(8);
					if (record.getR37_share_capital() != null) {
						cell7.setCellValue(record.getR37_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row37
					// Column I
					cell8 = row.createCell(9);
					if (record.getR37_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR37_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(36);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR38_company() != null) {
						cell2.setCellValue(record.getR38_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row38
					// Column D
					cell3 = row.createCell(4);
					if (record.getR38_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR38_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row38
					// Column E
					cell4 = row.createCell(5);
					if (record.getR38_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR38_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row38
					// Column F
					cell5 = row.createCell(6);
					if (record.getR38_line_of_business() != null) {
						cell5.setCellValue(record.getR38_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row38
					// Column G
					cell6 = row.createCell(7);
					if (record.getR38_currency() != null) {
						cell6.setCellValue(record.getR38_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row38
					// Column H
					cell7 = row.createCell(8);
					if (record.getR38_share_capital() != null) {
						cell7.setCellValue(record.getR38_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row38
					// Column I
					cell8 = row.createCell(9);
					if (record.getR38_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR38_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Email

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_UNCONS_INVEST EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_UNCONS_INVEST_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// Resub Format excel
	public byte[] BRRS_M_UNCONS_INVESTResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_UNCONS_INVESTResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype,
						type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_UNCONS_INVEST_Resub_Summary_Entity> dataList = getResubSummaryByDateAndVersion(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_UNCONS_INVEST report. Returning empty result.");
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

					M_UNCONS_INVEST_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//NORMAL

					// row11
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR11_amount() != null) {
						cell3.setCellValue(record.getR11_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR11_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR11_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row11
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR11_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR11_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR12_amount() != null) {
						cell3.setCellValue(record.getR12_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row12
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR12_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR12_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR12_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR13_amount() != null) {
						cell3.setCellValue(record.getR13_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR13_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR13_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR13_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR14_amount() != null) {
						cell3.setCellValue(record.getR14_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR14_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR14_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR14_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);

					// row15
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR15_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR15_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					// Column G
					cell6 = row.createCell(6);
					if (record.getR15_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR15_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// row22
					row = sheet.getRow(21);
					// Column C
					Cell cell2 = row.getCell(2);
					if (record.getR22_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR22_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_assets() != null) {
						cell3.setCellValue(record.getR22_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_liabilities() != null) {
						cell4.setCellValue(record.getR22_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_revenue() != null) {
						cell5.setCellValue(record.getR22_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_profit_or_loss() != null) {
						cell6.setCellValue(record.getR22_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row22
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR22_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR22_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row22
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR22_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR22_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR23_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR23_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_assets() != null) {
						cell3.setCellValue(record.getR23_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_liabilities() != null) {
						cell4.setCellValue(record.getR23_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_revenue() != null) {
						cell5.setCellValue(record.getR23_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_profit_or_loss() != null) {
						cell6.setCellValue(record.getR23_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row23
					// Column H
					cell7 = row.createCell(7);
					if (record.getR23_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR23_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row23
					// Column I
					cell8 = row.createCell(8);
					if (record.getR23_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR23_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR24_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR24_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_assets() != null) {
						cell3.setCellValue(record.getR24_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_liabilities() != null) {
						cell4.setCellValue(record.getR24_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_revenue() != null) {
						cell5.setCellValue(record.getR24_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_profit_or_loss() != null) {
						cell6.setCellValue(record.getR24_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row24
					// Column H
					cell7 = row.createCell(7);
					if (record.getR24_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR24_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row24
					// Column I
					cell8 = row.createCell(8);
					if (record.getR24_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR24_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column G
					cell6 = row.getCell(6);
					if (record.getR29_fair_value() != null) {
						cell6.setCellValue(record.getR29_fair_value().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);

					}

					// row35
					row = sheet.getRow(34);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR35_company() != null) {
						cell2.setCellValue(record.getR35_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR35_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row35
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR35_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row35
					// Column F
					cell5 = row.createCell(5);
					if (record.getR35_line_of_business() != null) {
						cell5.setCellValue(record.getR35_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					// Column G
					cell6 = row.createCell(6);
					if (record.getR35_currency() != null) {
						cell6.setCellValue(record.getR35_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row35
					// Column H
					cell7 = row.createCell(7);
					if (record.getR35_share_capital() != null) {
						cell7.setCellValue(record.getR35_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row35
					// Column I
					cell8 = row.createCell(8);
					if (record.getR35_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR35_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR36_company() != null) {
						cell2.setCellValue(record.getR36_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row36
					// Column D
					cell3 = row.createCell(3);
					if (record.getR36_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR36_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row36
					// Column E
					cell4 = row.createCell(4);
					if (record.getR36_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR36_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record.getR36_line_of_business() != null) {
						cell5.setCellValue(record.getR36_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row36
					// Column G
					cell6 = row.createCell(6);
					if (record.getR36_currency() != null) {
						cell6.setCellValue(record.getR36_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row36
					// Column H
					cell7 = row.createCell(7);
					if (record.getR36_share_capital() != null) {
						cell7.setCellValue(record.getR36_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row36
					// Column I
					cell8 = row.createCell(8);
					if (record.getR36_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR36_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR37_company() != null) {
						cell2.setCellValue(record.getR37_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR37_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row37
					// Column E
					cell4 = row.createCell(4);
					if (record.getR37_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR37_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row37
					// Column F
					cell5 = row.createCell(5);
					if (record.getR37_line_of_business() != null) {
						cell5.setCellValue(record.getR37_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row37
					// Column G
					cell6 = row.createCell(6);
					if (record.getR37_currency() != null) {
						cell6.setCellValue(record.getR37_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row37
					// Column H
					cell7 = row.createCell(7);
					if (record.getR37_share_capital() != null) {
						cell7.setCellValue(record.getR37_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row37
					// Column I
					cell8 = row.createCell(8);
					if (record.getR37_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR37_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR38_company() != null) {
						cell2.setCellValue(record.getR38_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row38
					// Column D
					cell3 = row.createCell(3);
					if (record.getR38_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR38_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row38
					// Column E
					cell4 = row.createCell(4);
					if (record.getR38_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR38_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row38
					// Column F
					cell5 = row.createCell(5);
					if (record.getR38_line_of_business() != null) {
						cell5.setCellValue(record.getR38_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row38
					// Column G
					cell6 = row.createCell(6);
					if (record.getR38_currency() != null) {
						cell6.setCellValue(record.getR38_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row38
					// Column H
					cell7 = row.createCell(7);
					if (record.getR38_share_capital() != null) {
						cell7.setCellValue(record.getR38_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row38
					// Column I
					cell8 = row.createCell(8);
					if (record.getR38_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR38_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Normal

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_UNCONS_INVEST RESUB SUMMARY", null,
						"BRRS_M_UNCONS_INVEST_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	// Resub Email Excel
	public byte[] BRRS_M_UNCONS_INVESTResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_UNCONS_INVEST_Resub_Summary_Entity> dataList = getResubSummaryByDateAndVersion(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_UNCONS_INVEST report. Returning empty result.");
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
					M_UNCONS_INVEST_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//EMAIL
					Cell R12Cell = row.createCell(3);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(9);
					// row11
					// Column D
					Cell cell3 = row.getCell(4);
					if (record.getR11_amount() != null) {
						cell3.setCellValue(record.getR11_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row14
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(4);
					if (record.getR14_amount() != null) {
						cell3.setCellValue(record.getR14_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row15
					row = sheet.getRow(13);
					// Column D
					cell3 = row.getCell(4);
					if (record.getR15_amount() != null) {
						cell3.setCellValue(record.getR15_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row22
					row = sheet.getRow(20);
					// Column C
					Cell cell2 = row.getCell(3);
					if (record.getR22_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR22_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row22
					// Column D
					cell3 = row.createCell(4);
					if (record.getR22_assets() != null) {
						cell3.setCellValue(record.getR22_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row22
					// Column E
					Cell cell4 = row.createCell(5);
					if (record.getR22_liabilities() != null) {
						cell4.setCellValue(record.getR22_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					Cell cell5 = row.createCell(6);
					if (record.getR22_revenue() != null) {
						cell5.setCellValue(record.getR22_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					// Column G
					Cell cell6 = row.createCell(7);
					if (record.getR22_profit_or_loss() != null) {
						cell6.setCellValue(record.getR22_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row22
					// Column H
					Cell cell7 = row.createCell(8);
					if (record.getR22_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR22_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row22
					// Column I
					Cell cell8 = row.createCell(9);
					if (record.getR22_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR22_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(21);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR23_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR23_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row23
					// Column H
					cell7 = row.createCell(8);
					if (record.getR23_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR23_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row23
					// Column I
					cell8 = row.createCell(9);
					if (record.getR23_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR23_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(22);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR24_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR24_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row24
					// Column D
					cell3 = row.createCell(4);
					if (record.getR24_assets() != null) {
						cell3.setCellValue(record.getR24_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					// Column E
					cell4 = row.createCell(5);
					if (record.getR24_liabilities() != null) {
						cell4.setCellValue(record.getR24_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(6);
					if (record.getR24_revenue() != null) {
						cell5.setCellValue(record.getR24_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					// Column G
					cell6 = row.createCell(7);
					if (record.getR24_profit_or_loss() != null) {
						cell6.setCellValue(record.getR24_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row24
					// Column H
					cell7 = row.createCell(8);
					if (record.getR24_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR24_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row24
					// Column I
					cell8 = row.createCell(9);
					if (record.getR24_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR24_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(27);
					// Column G
					cell6 = row.getCell(7);
					if (record.getR29_fair_value() != null) {
						cell6.setCellValue(record.getR29_fair_value().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);

					}

					// row35
					row = sheet.getRow(33);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR35_company() != null) {
						cell2.setCellValue(record.getR35_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row35
					// Column D
					cell3 = row.createCell(4);
					if (record.getR35_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR35_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row35
					// Column E
					cell4 = row.createCell(5);
					if (record.getR35_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR35_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row35
					// Column F
					cell5 = row.createCell(6);
					if (record.getR35_line_of_business() != null) {
						cell5.setCellValue(record.getR35_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					// Column G
					cell6 = row.createCell(7);
					if (record.getR35_currency() != null) {
						cell6.setCellValue(record.getR35_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row35
					// Column H
					cell7 = row.createCell(8);
					if (record.getR35_share_capital() != null) {
						cell7.setCellValue(record.getR35_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row35
					// Column I
					cell8 = row.createCell(9);
					if (record.getR35_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR35_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(34);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR36_company() != null) {
						cell2.setCellValue(record.getR36_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row36
					// Column H
					cell7 = row.createCell(8);
					if (record.getR36_share_capital() != null) {
						cell7.setCellValue(record.getR36_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row36
					// Column I
					cell8 = row.createCell(9);
					if (record.getR36_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR36_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(35);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR37_company() != null) {
						cell2.setCellValue(record.getR37_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row37
					// Column D
					cell3 = row.createCell(4);
					if (record.getR37_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR37_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row37
					// Column E
					cell4 = row.createCell(5);
					if (record.getR37_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR37_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row37
					// Column F
					cell5 = row.createCell(6);
					if (record.getR37_line_of_business() != null) {
						cell5.setCellValue(record.getR37_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row37
					// Column G
					cell6 = row.createCell(7);
					if (record.getR37_currency() != null) {
						cell6.setCellValue(record.getR37_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row37
					// Column H
					cell7 = row.createCell(8);
					if (record.getR37_share_capital() != null) {
						cell7.setCellValue(record.getR37_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row37
					// Column I
					cell8 = row.createCell(9);
					if (record.getR37_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR37_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(36);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR38_company() != null) {
						cell2.setCellValue(record.getR38_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row38
					// Column D
					cell3 = row.createCell(4);
					if (record.getR38_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR38_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row38
					// Column E
					cell4 = row.createCell(5);
					if (record.getR38_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR38_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row38
					// Column F
					cell5 = row.createCell(6);
					if (record.getR38_line_of_business() != null) {
						cell5.setCellValue(record.getR38_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row38
					// Column G
					cell6 = row.createCell(7);
					if (record.getR38_currency() != null) {
						cell6.setCellValue(record.getR38_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row38
					// Column H
					cell7 = row.createCell(8);
					if (record.getR38_share_capital() != null) {
						cell7.setCellValue(record.getR38_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row38
					// Column I
					cell8 = row.createCell(9);
					if (record.getR38_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR38_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Email

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_UNCONS_INVEST EMAIL RESUB SUMMARY", null,
						"BRRS_M_UNCONS_INVEST_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	// =========================================================
	// ROW MAPPERS
	// =========================================================

	class M_UNCONS_INVESTSummaryRowMapper implements RowMapper<M_UNCONS_INVEST_Summary_Entity> {
		@Override
		public M_UNCONS_INVEST_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_UNCONS_INVEST_Summary_Entity obj = new M_UNCONS_INVEST_Summary_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR11_percent_of_cet1_holding(rs.getBigDecimal("R11_PERCENT_OF_CET1_HOLDING"));
			obj.setR11_percent_of_additional_tier_1_holding(rs.getBigDecimal("R11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR11_percent_of_tier_2_holding(rs.getBigDecimal("R11_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR12_percent_of_cet1_holding(rs.getBigDecimal("R12_PERCENT_OF_CET1_HOLDING"));
			obj.setR12_percent_of_additional_tier_1_holding(rs.getBigDecimal("R12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR12_percent_of_tier_2_holding(rs.getBigDecimal("R12_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR13_percent_of_cet1_holding(rs.getBigDecimal("R13_PERCENT_OF_CET1_HOLDING"));
			obj.setR13_percent_of_additional_tier_1_holding(rs.getBigDecimal("R13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR13_percent_of_tier_2_holding(rs.getBigDecimal("R13_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR14_percent_of_cet1_holding(rs.getBigDecimal("R14_PERCENT_OF_CET1_HOLDING"));
			obj.setR14_percent_of_additional_tier_1_holding(rs.getBigDecimal("R14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR14_percent_of_tier_2_holding(rs.getBigDecimal("R14_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR15_percent_of_cet1_holding(rs.getBigDecimal("R15_PERCENT_OF_CET1_HOLDING"));
			obj.setR15_percent_of_additional_tier_1_holding(rs.getBigDecimal("R15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR15_percent_of_tier_2_holding(rs.getBigDecimal("R15_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_accuulated_equity_interest_5(rs.getBigDecimal("R22_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR22_assets(rs.getBigDecimal("R22_ASSETS"));
			obj.setR22_liabilities(rs.getBigDecimal("R22_LIABILITIES"));
			obj.setR22_revenue(rs.getBigDecimal("R22_REVENUE"));
			obj.setR22_profit_or_loss(rs.getBigDecimal("R22_PROFIT_OR_LOSS"));
			obj.setR22_unreg_share_of_loss(rs.getBigDecimal("R22_UNREG_SHARE_OF_LOSS"));
			obj.setR22_cumulative_unreg_share_of_loss(rs.getBigDecimal("R22_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_accuulated_equity_interest_5(rs.getBigDecimal("R23_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR23_assets(rs.getBigDecimal("R23_ASSETS"));
			obj.setR23_liabilities(rs.getBigDecimal("R23_LIABILITIES"));
			obj.setR23_revenue(rs.getBigDecimal("R23_REVENUE"));
			obj.setR23_profit_or_loss(rs.getBigDecimal("R23_PROFIT_OR_LOSS"));
			obj.setR23_unreg_share_of_loss(rs.getBigDecimal("R23_UNREG_SHARE_OF_LOSS"));
			obj.setR23_cumulative_unreg_share_of_loss(rs.getBigDecimal("R23_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_accuulated_equity_interest_5(rs.getBigDecimal("R24_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR24_assets(rs.getBigDecimal("R24_ASSETS"));
			obj.setR24_liabilities(rs.getBigDecimal("R24_LIABILITIES"));
			obj.setR24_revenue(rs.getBigDecimal("R24_REVENUE"));
			obj.setR24_profit_or_loss(rs.getBigDecimal("R24_PROFIT_OR_LOSS"));
			obj.setR24_unreg_share_of_loss(rs.getBigDecimal("R24_UNREG_SHARE_OF_LOSS"));
			obj.setR24_cumulative_unreg_share_of_loss(rs.getBigDecimal("R24_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_fair_value(rs.getBigDecimal("R29_FAIR_VALUE"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_company(rs.getBigDecimal("R35_COMPANY"));
			obj.setR35_jurisdiction_of_incorp_1(rs.getBigDecimal("R35_JURISDICTION_OF_INCORP_1"));
			obj.setR35_jurisdiction_of_incorp_2(rs.getBigDecimal("R35_JURISDICTION_OF_INCORP_2"));
			obj.setR35_line_of_business(rs.getBigDecimal("R35_LINE_OF_BUSINESS"));
			obj.setR35_currency(rs.getBigDecimal("R35_CURRENCY"));
			obj.setR35_share_capital(rs.getBigDecimal("R35_SHARE_CAPITAL"));
			obj.setR35_accumulated_equity_interest(rs.getBigDecimal("R35_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_company(rs.getBigDecimal("R36_COMPANY"));
			obj.setR36_jurisdiction_of_incorp_1(rs.getBigDecimal("R36_JURISDICTION_OF_INCORP_1"));
			obj.setR36_jurisdiction_of_incorp_2(rs.getBigDecimal("R36_JURISDICTION_OF_INCORP_2"));
			obj.setR36_line_of_business(rs.getBigDecimal("R36_LINE_OF_BUSINESS"));
			obj.setR36_currency(rs.getBigDecimal("R36_CURRENCY"));
			obj.setR36_share_capital(rs.getBigDecimal("R36_SHARE_CAPITAL"));
			obj.setR36_accumulated_equity_interest(rs.getBigDecimal("R36_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_company(rs.getBigDecimal("R37_COMPANY"));
			obj.setR37_jurisdiction_of_incorp_1(rs.getBigDecimal("R37_JURISDICTION_OF_INCORP_1"));
			obj.setR37_jurisdiction_of_incorp_2(rs.getBigDecimal("R37_JURISDICTION_OF_INCORP_2"));
			obj.setR37_line_of_business(rs.getBigDecimal("R37_LINE_OF_BUSINESS"));
			obj.setR37_currency(rs.getBigDecimal("R37_CURRENCY"));
			obj.setR37_share_capital(rs.getBigDecimal("R37_SHARE_CAPITAL"));
			obj.setR37_accumulated_equity_interest(rs.getBigDecimal("R37_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_company(rs.getBigDecimal("R38_COMPANY"));
			obj.setR38_jurisdiction_of_incorp_1(rs.getBigDecimal("R38_JURISDICTION_OF_INCORP_1"));
			obj.setR38_jurisdiction_of_incorp_2(rs.getBigDecimal("R38_JURISDICTION_OF_INCORP_2"));
			obj.setR38_line_of_business(rs.getBigDecimal("R38_LINE_OF_BUSINESS"));
			obj.setR38_currency(rs.getBigDecimal("R38_CURRENCY"));
			obj.setR38_share_capital(rs.getBigDecimal("R38_SHARE_CAPITAL"));
			obj.setR38_accumulated_equity_interest(rs.getBigDecimal("R38_ACCUMULATED_EQUITY_INTEREST"));
			return obj;
		}
	}

	class M_UNCONS_INVESTDetailRowMapper implements RowMapper<M_UNCONS_INVEST_Detail_Entity> {
		@Override
		public M_UNCONS_INVEST_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_UNCONS_INVEST_Detail_Entity obj = new M_UNCONS_INVEST_Detail_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR11_percent_of_cet1_holding(rs.getBigDecimal("R11_PERCENT_OF_CET1_HOLDING"));
			obj.setR11_percent_of_additional_tier_1_holding(rs.getBigDecimal("R11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR11_percent_of_tier_2_holding(rs.getBigDecimal("R11_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR12_percent_of_cet1_holding(rs.getBigDecimal("R12_PERCENT_OF_CET1_HOLDING"));
			obj.setR12_percent_of_additional_tier_1_holding(rs.getBigDecimal("R12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR12_percent_of_tier_2_holding(rs.getBigDecimal("R12_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR13_percent_of_cet1_holding(rs.getBigDecimal("R13_PERCENT_OF_CET1_HOLDING"));
			obj.setR13_percent_of_additional_tier_1_holding(rs.getBigDecimal("R13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR13_percent_of_tier_2_holding(rs.getBigDecimal("R13_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR14_percent_of_cet1_holding(rs.getBigDecimal("R14_PERCENT_OF_CET1_HOLDING"));
			obj.setR14_percent_of_additional_tier_1_holding(rs.getBigDecimal("R14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR14_percent_of_tier_2_holding(rs.getBigDecimal("R14_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR15_percent_of_cet1_holding(rs.getBigDecimal("R15_PERCENT_OF_CET1_HOLDING"));
			obj.setR15_percent_of_additional_tier_1_holding(rs.getBigDecimal("R15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR15_percent_of_tier_2_holding(rs.getBigDecimal("R15_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_accuulated_equity_interest_5(rs.getBigDecimal("R22_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR22_assets(rs.getBigDecimal("R22_ASSETS"));
			obj.setR22_liabilities(rs.getBigDecimal("R22_LIABILITIES"));
			obj.setR22_revenue(rs.getBigDecimal("R22_REVENUE"));
			obj.setR22_profit_or_loss(rs.getBigDecimal("R22_PROFIT_OR_LOSS"));
			obj.setR22_unreg_share_of_loss(rs.getBigDecimal("R22_UNREG_SHARE_OF_LOSS"));
			obj.setR22_cumulative_unreg_share_of_loss(rs.getBigDecimal("R22_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_accuulated_equity_interest_5(rs.getBigDecimal("R23_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR23_assets(rs.getBigDecimal("R23_ASSETS"));
			obj.setR23_liabilities(rs.getBigDecimal("R23_LIABILITIES"));
			obj.setR23_revenue(rs.getBigDecimal("R23_REVENUE"));
			obj.setR23_profit_or_loss(rs.getBigDecimal("R23_PROFIT_OR_LOSS"));
			obj.setR23_unreg_share_of_loss(rs.getBigDecimal("R23_UNREG_SHARE_OF_LOSS"));
			obj.setR23_cumulative_unreg_share_of_loss(rs.getBigDecimal("R23_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_accuulated_equity_interest_5(rs.getBigDecimal("R24_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR24_assets(rs.getBigDecimal("R24_ASSETS"));
			obj.setR24_liabilities(rs.getBigDecimal("R24_LIABILITIES"));
			obj.setR24_revenue(rs.getBigDecimal("R24_REVENUE"));
			obj.setR24_profit_or_loss(rs.getBigDecimal("R24_PROFIT_OR_LOSS"));
			obj.setR24_unreg_share_of_loss(rs.getBigDecimal("R24_UNREG_SHARE_OF_LOSS"));
			obj.setR24_cumulative_unreg_share_of_loss(rs.getBigDecimal("R24_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_fair_value(rs.getBigDecimal("R29_FAIR_VALUE"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_company(rs.getBigDecimal("R35_COMPANY"));
			obj.setR35_jurisdiction_of_incorp_1(rs.getBigDecimal("R35_JURISDICTION_OF_INCORP_1"));
			obj.setR35_jurisdiction_of_incorp_2(rs.getBigDecimal("R35_JURISDICTION_OF_INCORP_2"));
			obj.setR35_line_of_business(rs.getBigDecimal("R35_LINE_OF_BUSINESS"));
			obj.setR35_currency(rs.getBigDecimal("R35_CURRENCY"));
			obj.setR35_share_capital(rs.getBigDecimal("R35_SHARE_CAPITAL"));
			obj.setR35_accumulated_equity_interest(rs.getBigDecimal("R35_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_company(rs.getBigDecimal("R36_COMPANY"));
			obj.setR36_jurisdiction_of_incorp_1(rs.getBigDecimal("R36_JURISDICTION_OF_INCORP_1"));
			obj.setR36_jurisdiction_of_incorp_2(rs.getBigDecimal("R36_JURISDICTION_OF_INCORP_2"));
			obj.setR36_line_of_business(rs.getBigDecimal("R36_LINE_OF_BUSINESS"));
			obj.setR36_currency(rs.getBigDecimal("R36_CURRENCY"));
			obj.setR36_share_capital(rs.getBigDecimal("R36_SHARE_CAPITAL"));
			obj.setR36_accumulated_equity_interest(rs.getBigDecimal("R36_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_company(rs.getBigDecimal("R37_COMPANY"));
			obj.setR37_jurisdiction_of_incorp_1(rs.getBigDecimal("R37_JURISDICTION_OF_INCORP_1"));
			obj.setR37_jurisdiction_of_incorp_2(rs.getBigDecimal("R37_JURISDICTION_OF_INCORP_2"));
			obj.setR37_line_of_business(rs.getBigDecimal("R37_LINE_OF_BUSINESS"));
			obj.setR37_currency(rs.getBigDecimal("R37_CURRENCY"));
			obj.setR37_share_capital(rs.getBigDecimal("R37_SHARE_CAPITAL"));
			obj.setR37_accumulated_equity_interest(rs.getBigDecimal("R37_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_company(rs.getBigDecimal("R38_COMPANY"));
			obj.setR38_jurisdiction_of_incorp_1(rs.getBigDecimal("R38_JURISDICTION_OF_INCORP_1"));
			obj.setR38_jurisdiction_of_incorp_2(rs.getBigDecimal("R38_JURISDICTION_OF_INCORP_2"));
			obj.setR38_line_of_business(rs.getBigDecimal("R38_LINE_OF_BUSINESS"));
			obj.setR38_currency(rs.getBigDecimal("R38_CURRENCY"));
			obj.setR38_share_capital(rs.getBigDecimal("R38_SHARE_CAPITAL"));
			obj.setR38_accumulated_equity_interest(rs.getBigDecimal("R38_ACCUMULATED_EQUITY_INTEREST"));
			return obj;
		}
	}

	class M_UNCONS_INVESTArchivalSummaryRowMapper implements RowMapper<M_UNCONS_INVEST_Archival_Summary_Entity> {
		@Override
		public M_UNCONS_INVEST_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_UNCONS_INVEST_Archival_Summary_Entity obj = new M_UNCONS_INVEST_Archival_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR11_percent_of_cet1_holding(rs.getBigDecimal("R11_PERCENT_OF_CET1_HOLDING"));
			obj.setR11_percent_of_additional_tier_1_holding(rs.getBigDecimal("R11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR11_percent_of_tier_2_holding(rs.getBigDecimal("R11_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR12_percent_of_cet1_holding(rs.getBigDecimal("R12_PERCENT_OF_CET1_HOLDING"));
			obj.setR12_percent_of_additional_tier_1_holding(rs.getBigDecimal("R12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR12_percent_of_tier_2_holding(rs.getBigDecimal("R12_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR13_percent_of_cet1_holding(rs.getBigDecimal("R13_PERCENT_OF_CET1_HOLDING"));
			obj.setR13_percent_of_additional_tier_1_holding(rs.getBigDecimal("R13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR13_percent_of_tier_2_holding(rs.getBigDecimal("R13_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR14_percent_of_cet1_holding(rs.getBigDecimal("R14_PERCENT_OF_CET1_HOLDING"));
			obj.setR14_percent_of_additional_tier_1_holding(rs.getBigDecimal("R14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR14_percent_of_tier_2_holding(rs.getBigDecimal("R14_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR15_percent_of_cet1_holding(rs.getBigDecimal("R15_PERCENT_OF_CET1_HOLDING"));
			obj.setR15_percent_of_additional_tier_1_holding(rs.getBigDecimal("R15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR15_percent_of_tier_2_holding(rs.getBigDecimal("R15_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_accuulated_equity_interest_5(rs.getBigDecimal("R22_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR22_assets(rs.getBigDecimal("R22_ASSETS"));
			obj.setR22_liabilities(rs.getBigDecimal("R22_LIABILITIES"));
			obj.setR22_revenue(rs.getBigDecimal("R22_REVENUE"));
			obj.setR22_profit_or_loss(rs.getBigDecimal("R22_PROFIT_OR_LOSS"));
			obj.setR22_unreg_share_of_loss(rs.getBigDecimal("R22_UNREG_SHARE_OF_LOSS"));
			obj.setR22_cumulative_unreg_share_of_loss(rs.getBigDecimal("R22_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_accuulated_equity_interest_5(rs.getBigDecimal("R23_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR23_assets(rs.getBigDecimal("R23_ASSETS"));
			obj.setR23_liabilities(rs.getBigDecimal("R23_LIABILITIES"));
			obj.setR23_revenue(rs.getBigDecimal("R23_REVENUE"));
			obj.setR23_profit_or_loss(rs.getBigDecimal("R23_PROFIT_OR_LOSS"));
			obj.setR23_unreg_share_of_loss(rs.getBigDecimal("R23_UNREG_SHARE_OF_LOSS"));
			obj.setR23_cumulative_unreg_share_of_loss(rs.getBigDecimal("R23_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_accuulated_equity_interest_5(rs.getBigDecimal("R24_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR24_assets(rs.getBigDecimal("R24_ASSETS"));
			obj.setR24_liabilities(rs.getBigDecimal("R24_LIABILITIES"));
			obj.setR24_revenue(rs.getBigDecimal("R24_REVENUE"));
			obj.setR24_profit_or_loss(rs.getBigDecimal("R24_PROFIT_OR_LOSS"));
			obj.setR24_unreg_share_of_loss(rs.getBigDecimal("R24_UNREG_SHARE_OF_LOSS"));
			obj.setR24_cumulative_unreg_share_of_loss(rs.getBigDecimal("R24_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_fair_value(rs.getBigDecimal("R29_FAIR_VALUE"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_company(rs.getBigDecimal("R35_COMPANY"));
			obj.setR35_jurisdiction_of_incorp_1(rs.getBigDecimal("R35_JURISDICTION_OF_INCORP_1"));
			obj.setR35_jurisdiction_of_incorp_2(rs.getBigDecimal("R35_JURISDICTION_OF_INCORP_2"));
			obj.setR35_line_of_business(rs.getBigDecimal("R35_LINE_OF_BUSINESS"));
			obj.setR35_currency(rs.getBigDecimal("R35_CURRENCY"));
			obj.setR35_share_capital(rs.getBigDecimal("R35_SHARE_CAPITAL"));
			obj.setR35_accumulated_equity_interest(rs.getBigDecimal("R35_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_company(rs.getBigDecimal("R36_COMPANY"));
			obj.setR36_jurisdiction_of_incorp_1(rs.getBigDecimal("R36_JURISDICTION_OF_INCORP_1"));
			obj.setR36_jurisdiction_of_incorp_2(rs.getBigDecimal("R36_JURISDICTION_OF_INCORP_2"));
			obj.setR36_line_of_business(rs.getBigDecimal("R36_LINE_OF_BUSINESS"));
			obj.setR36_currency(rs.getBigDecimal("R36_CURRENCY"));
			obj.setR36_share_capital(rs.getBigDecimal("R36_SHARE_CAPITAL"));
			obj.setR36_accumulated_equity_interest(rs.getBigDecimal("R36_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_company(rs.getBigDecimal("R37_COMPANY"));
			obj.setR37_jurisdiction_of_incorp_1(rs.getBigDecimal("R37_JURISDICTION_OF_INCORP_1"));
			obj.setR37_jurisdiction_of_incorp_2(rs.getBigDecimal("R37_JURISDICTION_OF_INCORP_2"));
			obj.setR37_line_of_business(rs.getBigDecimal("R37_LINE_OF_BUSINESS"));
			obj.setR37_currency(rs.getBigDecimal("R37_CURRENCY"));
			obj.setR37_share_capital(rs.getBigDecimal("R37_SHARE_CAPITAL"));
			obj.setR37_accumulated_equity_interest(rs.getBigDecimal("R37_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_company(rs.getBigDecimal("R38_COMPANY"));
			obj.setR38_jurisdiction_of_incorp_1(rs.getBigDecimal("R38_JURISDICTION_OF_INCORP_1"));
			obj.setR38_jurisdiction_of_incorp_2(rs.getBigDecimal("R38_JURISDICTION_OF_INCORP_2"));
			obj.setR38_line_of_business(rs.getBigDecimal("R38_LINE_OF_BUSINESS"));
			obj.setR38_currency(rs.getBigDecimal("R38_CURRENCY"));
			obj.setR38_share_capital(rs.getBigDecimal("R38_SHARE_CAPITAL"));
			obj.setR38_accumulated_equity_interest(rs.getBigDecimal("R38_ACCUMULATED_EQUITY_INTEREST"));
			return obj;
		}
	}

	class M_UNCONS_INVESTArchivalDetailRowMapper implements RowMapper<M_UNCONS_INVEST_Archival_Detail_Entity> {
		@Override
		public M_UNCONS_INVEST_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_UNCONS_INVEST_Archival_Detail_Entity obj = new M_UNCONS_INVEST_Archival_Detail_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR11_percent_of_cet1_holding(rs.getBigDecimal("R11_PERCENT_OF_CET1_HOLDING"));
			obj.setR11_percent_of_additional_tier_1_holding(rs.getBigDecimal("R11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR11_percent_of_tier_2_holding(rs.getBigDecimal("R11_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR12_percent_of_cet1_holding(rs.getBigDecimal("R12_PERCENT_OF_CET1_HOLDING"));
			obj.setR12_percent_of_additional_tier_1_holding(rs.getBigDecimal("R12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR12_percent_of_tier_2_holding(rs.getBigDecimal("R12_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR13_percent_of_cet1_holding(rs.getBigDecimal("R13_PERCENT_OF_CET1_HOLDING"));
			obj.setR13_percent_of_additional_tier_1_holding(rs.getBigDecimal("R13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR13_percent_of_tier_2_holding(rs.getBigDecimal("R13_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR14_percent_of_cet1_holding(rs.getBigDecimal("R14_PERCENT_OF_CET1_HOLDING"));
			obj.setR14_percent_of_additional_tier_1_holding(rs.getBigDecimal("R14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR14_percent_of_tier_2_holding(rs.getBigDecimal("R14_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR15_percent_of_cet1_holding(rs.getBigDecimal("R15_PERCENT_OF_CET1_HOLDING"));
			obj.setR15_percent_of_additional_tier_1_holding(rs.getBigDecimal("R15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR15_percent_of_tier_2_holding(rs.getBigDecimal("R15_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_accuulated_equity_interest_5(rs.getBigDecimal("R22_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR22_assets(rs.getBigDecimal("R22_ASSETS"));
			obj.setR22_liabilities(rs.getBigDecimal("R22_LIABILITIES"));
			obj.setR22_revenue(rs.getBigDecimal("R22_REVENUE"));
			obj.setR22_profit_or_loss(rs.getBigDecimal("R22_PROFIT_OR_LOSS"));
			obj.setR22_unreg_share_of_loss(rs.getBigDecimal("R22_UNREG_SHARE_OF_LOSS"));
			obj.setR22_cumulative_unreg_share_of_loss(rs.getBigDecimal("R22_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_accuulated_equity_interest_5(rs.getBigDecimal("R23_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR23_assets(rs.getBigDecimal("R23_ASSETS"));
			obj.setR23_liabilities(rs.getBigDecimal("R23_LIABILITIES"));
			obj.setR23_revenue(rs.getBigDecimal("R23_REVENUE"));
			obj.setR23_profit_or_loss(rs.getBigDecimal("R23_PROFIT_OR_LOSS"));
			obj.setR23_unreg_share_of_loss(rs.getBigDecimal("R23_UNREG_SHARE_OF_LOSS"));
			obj.setR23_cumulative_unreg_share_of_loss(rs.getBigDecimal("R23_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_accuulated_equity_interest_5(rs.getBigDecimal("R24_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR24_assets(rs.getBigDecimal("R24_ASSETS"));
			obj.setR24_liabilities(rs.getBigDecimal("R24_LIABILITIES"));
			obj.setR24_revenue(rs.getBigDecimal("R24_REVENUE"));
			obj.setR24_profit_or_loss(rs.getBigDecimal("R24_PROFIT_OR_LOSS"));
			obj.setR24_unreg_share_of_loss(rs.getBigDecimal("R24_UNREG_SHARE_OF_LOSS"));
			obj.setR24_cumulative_unreg_share_of_loss(rs.getBigDecimal("R24_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_fair_value(rs.getBigDecimal("R29_FAIR_VALUE"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_company(rs.getBigDecimal("R35_COMPANY"));
			obj.setR35_jurisdiction_of_incorp_1(rs.getBigDecimal("R35_JURISDICTION_OF_INCORP_1"));
			obj.setR35_jurisdiction_of_incorp_2(rs.getBigDecimal("R35_JURISDICTION_OF_INCORP_2"));
			obj.setR35_line_of_business(rs.getBigDecimal("R35_LINE_OF_BUSINESS"));
			obj.setR35_currency(rs.getBigDecimal("R35_CURRENCY"));
			obj.setR35_share_capital(rs.getBigDecimal("R35_SHARE_CAPITAL"));
			obj.setR35_accumulated_equity_interest(rs.getBigDecimal("R35_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_company(rs.getBigDecimal("R36_COMPANY"));
			obj.setR36_jurisdiction_of_incorp_1(rs.getBigDecimal("R36_JURISDICTION_OF_INCORP_1"));
			obj.setR36_jurisdiction_of_incorp_2(rs.getBigDecimal("R36_JURISDICTION_OF_INCORP_2"));
			obj.setR36_line_of_business(rs.getBigDecimal("R36_LINE_OF_BUSINESS"));
			obj.setR36_currency(rs.getBigDecimal("R36_CURRENCY"));
			obj.setR36_share_capital(rs.getBigDecimal("R36_SHARE_CAPITAL"));
			obj.setR36_accumulated_equity_interest(rs.getBigDecimal("R36_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_company(rs.getBigDecimal("R37_COMPANY"));
			obj.setR37_jurisdiction_of_incorp_1(rs.getBigDecimal("R37_JURISDICTION_OF_INCORP_1"));
			obj.setR37_jurisdiction_of_incorp_2(rs.getBigDecimal("R37_JURISDICTION_OF_INCORP_2"));
			obj.setR37_line_of_business(rs.getBigDecimal("R37_LINE_OF_BUSINESS"));
			obj.setR37_currency(rs.getBigDecimal("R37_CURRENCY"));
			obj.setR37_share_capital(rs.getBigDecimal("R37_SHARE_CAPITAL"));
			obj.setR37_accumulated_equity_interest(rs.getBigDecimal("R37_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_company(rs.getBigDecimal("R38_COMPANY"));
			obj.setR38_jurisdiction_of_incorp_1(rs.getBigDecimal("R38_JURISDICTION_OF_INCORP_1"));
			obj.setR38_jurisdiction_of_incorp_2(rs.getBigDecimal("R38_JURISDICTION_OF_INCORP_2"));
			obj.setR38_line_of_business(rs.getBigDecimal("R38_LINE_OF_BUSINESS"));
			obj.setR38_currency(rs.getBigDecimal("R38_CURRENCY"));
			obj.setR38_share_capital(rs.getBigDecimal("R38_SHARE_CAPITAL"));
			obj.setR38_accumulated_equity_interest(rs.getBigDecimal("R38_ACCUMULATED_EQUITY_INTEREST"));
			return obj;
		}
	}

	class M_UNCONS_INVESTResubSummaryRowMapper implements RowMapper<M_UNCONS_INVEST_Resub_Summary_Entity> {
		@Override
		public M_UNCONS_INVEST_Resub_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_UNCONS_INVEST_Resub_Summary_Entity obj = new M_UNCONS_INVEST_Resub_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR11_percent_of_cet1_holding(rs.getBigDecimal("R11_PERCENT_OF_CET1_HOLDING"));
			obj.setR11_percent_of_additional_tier_1_holding(rs.getBigDecimal("R11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR11_percent_of_tier_2_holding(rs.getBigDecimal("R11_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR12_percent_of_cet1_holding(rs.getBigDecimal("R12_PERCENT_OF_CET1_HOLDING"));
			obj.setR12_percent_of_additional_tier_1_holding(rs.getBigDecimal("R12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR12_percent_of_tier_2_holding(rs.getBigDecimal("R12_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR13_percent_of_cet1_holding(rs.getBigDecimal("R13_PERCENT_OF_CET1_HOLDING"));
			obj.setR13_percent_of_additional_tier_1_holding(rs.getBigDecimal("R13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR13_percent_of_tier_2_holding(rs.getBigDecimal("R13_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR14_percent_of_cet1_holding(rs.getBigDecimal("R14_PERCENT_OF_CET1_HOLDING"));
			obj.setR14_percent_of_additional_tier_1_holding(rs.getBigDecimal("R14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR14_percent_of_tier_2_holding(rs.getBigDecimal("R14_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR15_percent_of_cet1_holding(rs.getBigDecimal("R15_PERCENT_OF_CET1_HOLDING"));
			obj.setR15_percent_of_additional_tier_1_holding(rs.getBigDecimal("R15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR15_percent_of_tier_2_holding(rs.getBigDecimal("R15_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_accuulated_equity_interest_5(rs.getBigDecimal("R22_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR22_assets(rs.getBigDecimal("R22_ASSETS"));
			obj.setR22_liabilities(rs.getBigDecimal("R22_LIABILITIES"));
			obj.setR22_revenue(rs.getBigDecimal("R22_REVENUE"));
			obj.setR22_profit_or_loss(rs.getBigDecimal("R22_PROFIT_OR_LOSS"));
			obj.setR22_unreg_share_of_loss(rs.getBigDecimal("R22_UNREG_SHARE_OF_LOSS"));
			obj.setR22_cumulative_unreg_share_of_loss(rs.getBigDecimal("R22_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_accuulated_equity_interest_5(rs.getBigDecimal("R23_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR23_assets(rs.getBigDecimal("R23_ASSETS"));
			obj.setR23_liabilities(rs.getBigDecimal("R23_LIABILITIES"));
			obj.setR23_revenue(rs.getBigDecimal("R23_REVENUE"));
			obj.setR23_profit_or_loss(rs.getBigDecimal("R23_PROFIT_OR_LOSS"));
			obj.setR23_unreg_share_of_loss(rs.getBigDecimal("R23_UNREG_SHARE_OF_LOSS"));
			obj.setR23_cumulative_unreg_share_of_loss(rs.getBigDecimal("R23_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_accuulated_equity_interest_5(rs.getBigDecimal("R24_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR24_assets(rs.getBigDecimal("R24_ASSETS"));
			obj.setR24_liabilities(rs.getBigDecimal("R24_LIABILITIES"));
			obj.setR24_revenue(rs.getBigDecimal("R24_REVENUE"));
			obj.setR24_profit_or_loss(rs.getBigDecimal("R24_PROFIT_OR_LOSS"));
			obj.setR24_unreg_share_of_loss(rs.getBigDecimal("R24_UNREG_SHARE_OF_LOSS"));
			obj.setR24_cumulative_unreg_share_of_loss(rs.getBigDecimal("R24_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_fair_value(rs.getBigDecimal("R29_FAIR_VALUE"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_company(rs.getBigDecimal("R35_COMPANY"));
			obj.setR35_jurisdiction_of_incorp_1(rs.getBigDecimal("R35_JURISDICTION_OF_INCORP_1"));
			obj.setR35_jurisdiction_of_incorp_2(rs.getBigDecimal("R35_JURISDICTION_OF_INCORP_2"));
			obj.setR35_line_of_business(rs.getBigDecimal("R35_LINE_OF_BUSINESS"));
			obj.setR35_currency(rs.getBigDecimal("R35_CURRENCY"));
			obj.setR35_share_capital(rs.getBigDecimal("R35_SHARE_CAPITAL"));
			obj.setR35_accumulated_equity_interest(rs.getBigDecimal("R35_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_company(rs.getBigDecimal("R36_COMPANY"));
			obj.setR36_jurisdiction_of_incorp_1(rs.getBigDecimal("R36_JURISDICTION_OF_INCORP_1"));
			obj.setR36_jurisdiction_of_incorp_2(rs.getBigDecimal("R36_JURISDICTION_OF_INCORP_2"));
			obj.setR36_line_of_business(rs.getBigDecimal("R36_LINE_OF_BUSINESS"));
			obj.setR36_currency(rs.getBigDecimal("R36_CURRENCY"));
			obj.setR36_share_capital(rs.getBigDecimal("R36_SHARE_CAPITAL"));
			obj.setR36_accumulated_equity_interest(rs.getBigDecimal("R36_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_company(rs.getBigDecimal("R37_COMPANY"));
			obj.setR37_jurisdiction_of_incorp_1(rs.getBigDecimal("R37_JURISDICTION_OF_INCORP_1"));
			obj.setR37_jurisdiction_of_incorp_2(rs.getBigDecimal("R37_JURISDICTION_OF_INCORP_2"));
			obj.setR37_line_of_business(rs.getBigDecimal("R37_LINE_OF_BUSINESS"));
			obj.setR37_currency(rs.getBigDecimal("R37_CURRENCY"));
			obj.setR37_share_capital(rs.getBigDecimal("R37_SHARE_CAPITAL"));
			obj.setR37_accumulated_equity_interest(rs.getBigDecimal("R37_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_company(rs.getBigDecimal("R38_COMPANY"));
			obj.setR38_jurisdiction_of_incorp_1(rs.getBigDecimal("R38_JURISDICTION_OF_INCORP_1"));
			obj.setR38_jurisdiction_of_incorp_2(rs.getBigDecimal("R38_JURISDICTION_OF_INCORP_2"));
			obj.setR38_line_of_business(rs.getBigDecimal("R38_LINE_OF_BUSINESS"));
			obj.setR38_currency(rs.getBigDecimal("R38_CURRENCY"));
			obj.setR38_share_capital(rs.getBigDecimal("R38_SHARE_CAPITAL"));
			obj.setR38_accumulated_equity_interest(rs.getBigDecimal("R38_ACCUMULATED_EQUITY_INTEREST"));
			return obj;
		}
	}

	class M_UNCONS_INVESTResubDetailRowMapper implements RowMapper<M_UNCONS_INVEST_Resub_Detail_Entity> {
		@Override
		public M_UNCONS_INVEST_Resub_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_UNCONS_INVEST_Resub_Detail_Entity obj = new M_UNCONS_INVEST_Resub_Detail_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR11_percent_of_cet1_holding(rs.getBigDecimal("R11_PERCENT_OF_CET1_HOLDING"));
			obj.setR11_percent_of_additional_tier_1_holding(rs.getBigDecimal("R11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR11_percent_of_tier_2_holding(rs.getBigDecimal("R11_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR12_percent_of_cet1_holding(rs.getBigDecimal("R12_PERCENT_OF_CET1_HOLDING"));
			obj.setR12_percent_of_additional_tier_1_holding(rs.getBigDecimal("R12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR12_percent_of_tier_2_holding(rs.getBigDecimal("R12_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR13_percent_of_cet1_holding(rs.getBigDecimal("R13_PERCENT_OF_CET1_HOLDING"));
			obj.setR13_percent_of_additional_tier_1_holding(rs.getBigDecimal("R13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR13_percent_of_tier_2_holding(rs.getBigDecimal("R13_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR14_percent_of_cet1_holding(rs.getBigDecimal("R14_PERCENT_OF_CET1_HOLDING"));
			obj.setR14_percent_of_additional_tier_1_holding(rs.getBigDecimal("R14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR14_percent_of_tier_2_holding(rs.getBigDecimal("R14_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR15_percent_of_cet1_holding(rs.getBigDecimal("R15_PERCENT_OF_CET1_HOLDING"));
			obj.setR15_percent_of_additional_tier_1_holding(rs.getBigDecimal("R15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING"));
			obj.setR15_percent_of_tier_2_holding(rs.getBigDecimal("R15_PERCENT_OF_TIER_2_HOLDING"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_accuulated_equity_interest_5(rs.getBigDecimal("R22_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR22_assets(rs.getBigDecimal("R22_ASSETS"));
			obj.setR22_liabilities(rs.getBigDecimal("R22_LIABILITIES"));
			obj.setR22_revenue(rs.getBigDecimal("R22_REVENUE"));
			obj.setR22_profit_or_loss(rs.getBigDecimal("R22_PROFIT_OR_LOSS"));
			obj.setR22_unreg_share_of_loss(rs.getBigDecimal("R22_UNREG_SHARE_OF_LOSS"));
			obj.setR22_cumulative_unreg_share_of_loss(rs.getBigDecimal("R22_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_accuulated_equity_interest_5(rs.getBigDecimal("R23_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR23_assets(rs.getBigDecimal("R23_ASSETS"));
			obj.setR23_liabilities(rs.getBigDecimal("R23_LIABILITIES"));
			obj.setR23_revenue(rs.getBigDecimal("R23_REVENUE"));
			obj.setR23_profit_or_loss(rs.getBigDecimal("R23_PROFIT_OR_LOSS"));
			obj.setR23_unreg_share_of_loss(rs.getBigDecimal("R23_UNREG_SHARE_OF_LOSS"));
			obj.setR23_cumulative_unreg_share_of_loss(rs.getBigDecimal("R23_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_accuulated_equity_interest_5(rs.getBigDecimal("R24_ACCUULATED_EQUITY_INTEREST_5"));
			obj.setR24_assets(rs.getBigDecimal("R24_ASSETS"));
			obj.setR24_liabilities(rs.getBigDecimal("R24_LIABILITIES"));
			obj.setR24_revenue(rs.getBigDecimal("R24_REVENUE"));
			obj.setR24_profit_or_loss(rs.getBigDecimal("R24_PROFIT_OR_LOSS"));
			obj.setR24_unreg_share_of_loss(rs.getBigDecimal("R24_UNREG_SHARE_OF_LOSS"));
			obj.setR24_cumulative_unreg_share_of_loss(rs.getBigDecimal("R24_CUMULATIVE_UNREG_SHARE_OF_LOSS"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_fair_value(rs.getBigDecimal("R29_FAIR_VALUE"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_company(rs.getBigDecimal("R35_COMPANY"));
			obj.setR35_jurisdiction_of_incorp_1(rs.getBigDecimal("R35_JURISDICTION_OF_INCORP_1"));
			obj.setR35_jurisdiction_of_incorp_2(rs.getBigDecimal("R35_JURISDICTION_OF_INCORP_2"));
			obj.setR35_line_of_business(rs.getBigDecimal("R35_LINE_OF_BUSINESS"));
			obj.setR35_currency(rs.getBigDecimal("R35_CURRENCY"));
			obj.setR35_share_capital(rs.getBigDecimal("R35_SHARE_CAPITAL"));
			obj.setR35_accumulated_equity_interest(rs.getBigDecimal("R35_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_company(rs.getBigDecimal("R36_COMPANY"));
			obj.setR36_jurisdiction_of_incorp_1(rs.getBigDecimal("R36_JURISDICTION_OF_INCORP_1"));
			obj.setR36_jurisdiction_of_incorp_2(rs.getBigDecimal("R36_JURISDICTION_OF_INCORP_2"));
			obj.setR36_line_of_business(rs.getBigDecimal("R36_LINE_OF_BUSINESS"));
			obj.setR36_currency(rs.getBigDecimal("R36_CURRENCY"));
			obj.setR36_share_capital(rs.getBigDecimal("R36_SHARE_CAPITAL"));
			obj.setR36_accumulated_equity_interest(rs.getBigDecimal("R36_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_company(rs.getBigDecimal("R37_COMPANY"));
			obj.setR37_jurisdiction_of_incorp_1(rs.getBigDecimal("R37_JURISDICTION_OF_INCORP_1"));
			obj.setR37_jurisdiction_of_incorp_2(rs.getBigDecimal("R37_JURISDICTION_OF_INCORP_2"));
			obj.setR37_line_of_business(rs.getBigDecimal("R37_LINE_OF_BUSINESS"));
			obj.setR37_currency(rs.getBigDecimal("R37_CURRENCY"));
			obj.setR37_share_capital(rs.getBigDecimal("R37_SHARE_CAPITAL"));
			obj.setR37_accumulated_equity_interest(rs.getBigDecimal("R37_ACCUMULATED_EQUITY_INTEREST"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_company(rs.getBigDecimal("R38_COMPANY"));
			obj.setR38_jurisdiction_of_incorp_1(rs.getBigDecimal("R38_JURISDICTION_OF_INCORP_1"));
			obj.setR38_jurisdiction_of_incorp_2(rs.getBigDecimal("R38_JURISDICTION_OF_INCORP_2"));
			obj.setR38_line_of_business(rs.getBigDecimal("R38_LINE_OF_BUSINESS"));
			obj.setR38_currency(rs.getBigDecimal("R38_CURRENCY"));
			obj.setR38_share_capital(rs.getBigDecimal("R38_SHARE_CAPITAL"));
			obj.setR38_accumulated_equity_interest(rs.getBigDecimal("R38_ACCUMULATED_EQUITY_INTEREST"));
			return obj;
		}
	}
}