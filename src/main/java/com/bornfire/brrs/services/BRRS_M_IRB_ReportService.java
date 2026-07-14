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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.M_IRB_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_IRB_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_IRB_Detail_Entity;
import com.bornfire.brrs.entities.M_IRB_Summary_Entity;
import com.bornfire.brrs.entities.UserProfileRep;


@Service
@Transactional

public class BRRS_M_IRB_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LIQ_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ===================== JDBC Query Methods (M_IRB) =====================

	public List<M_IRB_Summary_Entity> getSummaryByDate(Date reportDate) {
		return jdbcTemplate.query("select * from BRRS_M_IRB_SUMMARYTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, new M_IRBSummaryRowMapper());
	}

	public List<M_IRB_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
				"select * from BRRS_M_IRB_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_IRBArchivalSummaryRowMapper());
	}

	public List<M_IRB_Archival_Summary_Entity> getArchivalSummaryWithVersion() {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_IRB_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC",
				new M_IRBArchivalSummaryRowMapper());
	}

	public List<M_IRB_Detail_Entity> getDetailByDate(Date reportDate) {
		return jdbcTemplate.query("select * from BRRS_M_IRB_DETAILTABLE where REPORT_DATE = ?",
				new Object[] { reportDate }, new M_IRBDetailRowMapper());
	}

	public List<M_IRB_Detail_Entity> getDetailByDatePaged(Date reportDate, int startpage, int endpage) {
		return jdbcTemplate.query(
				"select * from BRRS_M_IRB_DETAILTABLE where REPORT_DATE=? offset ? rows fetch next ? rows only",
				new Object[] { reportDate, startpage, endpage }, new M_IRBDetailRowMapper());
	}

	public int getDetailCount(Date reportDate) {
		return jdbcTemplate.queryForObject("select count(*) from BRRS_M_IRB_DETAILTABLE where REPORT_DATE=?",
				new Object[] { reportDate }, Integer.class);
	}

	public List<M_IRB_Detail_Entity> getDetailByRowIdAndColumnId(String rowId, String columnId, Date reportDate) {
		return jdbcTemplate.query(
				"select * from BRRS_M_IRB_DETAILTABLE where ROW_ID =? and COLUMN_ID=? AND REPORT_DATE=?",
				new Object[] { rowId, columnId, reportDate }, new M_IRBDetailRowMapper());
	}

	public List<M_IRB_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, String version) {
		return jdbcTemplate.query(
				"select * from BRRS_M_IRB_ARCHIVALTABLE_DETAIL where REPORT_DATE=? AND DATA_ENTRY_VERSION=?",
				new Object[] { reportDate, version }, new M_IRBArchivalDetailRowMapper());
	}

	public List<M_IRB_Archival_Detail_Entity> getArchivalDetailByRowIdAndColumnId(String rowId, String columnId,
			Date reportDate, String version) {
		return jdbcTemplate.query(
				"select * from BRRS_M_IRB_ARCHIVALTABLE_DETAIL where ROW_ID =? and COLUMN_ID=? AND REPORT_DATE=? AND DATA_ENTRY_VERSION=?",
				new Object[] { rowId, columnId, reportDate, version }, new M_IRBArchivalDetailRowMapper());
	}

	private static final String UPDATE_M_IRB_SUMMARY_SQL = "UPDATE BRRS_M_IRB_SUMMARYTABLE SET " +
			"R10_PRODUCT=?,R10_UP_TO_1_MONTH=?,R10_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R10_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R10_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R10_MORE_THAN_12_MONTH_TO_3_YEARS=?,R10_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R10_MORE_THAN_5_YEARS_TO_10_YEARS=?,R10_MORE_THAN_10_YEARS=?,R10_NON_RATIO_SENSATIVE_ITEMS=?," +
			"R10_TOTAL=?,R11_PRODUCT=?,R11_UP_TO_1_MONTH=?,R11_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R11_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R11_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R11_MORE_THAN_12_MONTH_TO_3_YEARS=?,R11_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R11_MORE_THAN_5_YEARS_TO_10_YEARS=?,R11_MORE_THAN_10_YEARS=?,R11_TOTAL=?,R12_PRODUCT=?," +
			"R12_UP_TO_1_MONTH=?,R12_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R12_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R12_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R12_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R12_MORE_THAN_3_YEARS_TO_5_YEARS=?,R12_MORE_THAN_5_YEARS_TO_10_YEARS=?,R12_MORE_THAN_10_YEARS=?," +
			"R12_TOTAL=?,R13_PRODUCT=?,R13_UP_TO_1_MONTH=?,R13_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R13_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R13_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R13_MORE_THAN_12_MONTH_TO_3_YEARS=?,R13_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R13_MORE_THAN_5_YEARS_TO_10_YEARS=?,R13_MORE_THAN_10_YEARS=?,R13_TOTAL=?,R14_PRODUCT=?," +
			"R14_UP_TO_1_MONTH=?,R14_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R14_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R14_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R14_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R14_MORE_THAN_3_YEARS_TO_5_YEARS=?,R14_MORE_THAN_5_YEARS_TO_10_YEARS=?,R14_MORE_THAN_10_YEARS=?," +
			"R14_TOTAL=?,R15_PRODUCT=?,R15_UP_TO_1_MONTH=?,R15_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R15_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R15_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R15_MORE_THAN_12_MONTH_TO_3_YEARS=?,R15_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R15_MORE_THAN_5_YEARS_TO_10_YEARS=?,R15_MORE_THAN_10_YEARS=?,R15_TOTAL=?,R16_PRODUCT=?," +
			"R16_UP_TO_1_MONTH=?,R16_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R16_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R16_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R16_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R16_MORE_THAN_3_YEARS_TO_5_YEARS=?,R16_MORE_THAN_5_YEARS_TO_10_YEARS=?,R16_MORE_THAN_10_YEARS=?," +
			"R16_TOTAL=?,R17_PRODUCT=?,R17_UP_TO_1_MONTH=?,R17_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R17_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R17_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R17_MORE_THAN_12_MONTH_TO_3_YEARS=?,R17_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R17_MORE_THAN_5_YEARS_TO_10_YEARS=?,R17_MORE_THAN_10_YEARS=?,R17_TOTAL=?,R18_PRODUCT=?," +
			"R18_UP_TO_1_MONTH=?,R18_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R18_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R18_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R18_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R18_MORE_THAN_3_YEARS_TO_5_YEARS=?,R18_MORE_THAN_5_YEARS_TO_10_YEARS=?,R18_MORE_THAN_10_YEARS=?," +
			"R18_TOTAL=?,R19_PRODUCT=?,R19_UP_TO_1_MONTH=?,R19_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R19_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R19_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R19_MORE_THAN_12_MONTH_TO_3_YEARS=?,R19_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R19_MORE_THAN_5_YEARS_TO_10_YEARS=?,R19_MORE_THAN_10_YEARS=?,R19_TOTAL=?,R20_PRODUCT=?," +
			"R20_UP_TO_1_MONTH=?,R20_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R20_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R20_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R20_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R20_MORE_THAN_3_YEARS_TO_5_YEARS=?,R20_MORE_THAN_5_YEARS_TO_10_YEARS=?,R20_MORE_THAN_10_YEARS=?," +
			"R20_TOTAL=?,R21_PRODUCT=?,R21_UP_TO_1_MONTH=?,R21_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R21_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R21_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R21_MORE_THAN_12_MONTH_TO_3_YEARS=?,R21_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R21_MORE_THAN_5_YEARS_TO_10_YEARS=?,R21_MORE_THAN_10_YEARS=?,R21_TOTAL=?,R22_PRODUCT=?," +
			"R22_UP_TO_1_MONTH=?,R22_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R22_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R22_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R22_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R22_MORE_THAN_3_YEARS_TO_5_YEARS=?,R22_MORE_THAN_5_YEARS_TO_10_YEARS=?,R22_MORE_THAN_10_YEARS=?," +
			"R22_TOTAL=?,R23_PRODUCT=?,R23_UP_TO_1_MONTH=?,R23_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R23_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R23_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R23_MORE_THAN_12_MONTH_TO_3_YEARS=?,R23_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R23_MORE_THAN_5_YEARS_TO_10_YEARS=?,R23_MORE_THAN_10_YEARS=?,R23_TOTAL=?,R24_PRODUCT=?," +
			"R24_UP_TO_1_MONTH=?,R24_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R24_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R24_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R24_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R24_MORE_THAN_3_YEARS_TO_5_YEARS=?,R24_MORE_THAN_5_YEARS_TO_10_YEARS=?,R24_MORE_THAN_10_YEARS=?," +
			"R24_TOTAL=?,R25_PRODUCT=?,R25_UP_TO_1_MONTH=?,R25_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R25_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R25_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R25_MORE_THAN_12_MONTH_TO_3_YEARS=?,R25_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R25_MORE_THAN_5_YEARS_TO_10_YEARS=?,R25_MORE_THAN_10_YEARS=?,R25_TOTAL=?,R26_PRODUCT=?," +
			"R26_UP_TO_1_MONTH=?,R26_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R26_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R26_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R26_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R26_MORE_THAN_3_YEARS_TO_5_YEARS=?,R26_MORE_THAN_5_YEARS_TO_10_YEARS=?,R26_MORE_THAN_10_YEARS=?," +
			"R26_TOTAL=?,R27_PRODUCT=?,R27_UP_TO_1_MONTH=?,R27_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R27_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R27_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R27_MORE_THAN_12_MONTH_TO_3_YEARS=?,R27_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R27_MORE_THAN_5_YEARS_TO_10_YEARS=?,R27_MORE_THAN_10_YEARS=?,R27_TOTAL=?,R28_PRODUCT=?," +
			"R28_UP_TO_1_MONTH=?,R28_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R28_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R28_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R28_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R28_MORE_THAN_3_YEARS_TO_5_YEARS=?,R28_MORE_THAN_5_YEARS_TO_10_YEARS=?,R28_MORE_THAN_10_YEARS=?," +
			"R28_TOTAL=?,R29_PRODUCT=?,R29_UP_TO_1_MONTH=?,R29_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R29_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R29_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R29_MORE_THAN_12_MONTH_TO_3_YEARS=?,R29_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R29_MORE_THAN_5_YEARS_TO_10_YEARS=?,R29_MORE_THAN_10_YEARS=?,R29_TOTAL=?,R30_PRODUCT=?," +
			"R30_UP_TO_1_MONTH=?,R30_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R30_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R30_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R30_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R30_MORE_THAN_3_YEARS_TO_5_YEARS=?,R30_MORE_THAN_5_YEARS_TO_10_YEARS=?,R30_MORE_THAN_10_YEARS=?," +
			"R30_TOTAL=?,R31_PRODUCT=?,R31_UP_TO_1_MONTH=?,R31_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R31_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R31_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R31_MORE_THAN_12_MONTH_TO_3_YEARS=?,R31_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R31_MORE_THAN_5_YEARS_TO_10_YEARS=?,R31_MORE_THAN_10_YEARS=?,R31_TOTAL=?,R32_PRODUCT=?," +
			"R32_UP_TO_1_MONTH=?,R32_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R32_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R32_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R32_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R32_MORE_THAN_3_YEARS_TO_5_YEARS=?,R32_MORE_THAN_5_YEARS_TO_10_YEARS=?,R32_MORE_THAN_10_YEARS=?," +
			"R32_TOTAL=?,R33_PRODUCT=?,R33_UP_TO_1_MONTH=?,R33_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R33_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R33_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R33_MORE_THAN_12_MONTH_TO_3_YEARS=?,R33_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R33_MORE_THAN_5_YEARS_TO_10_YEARS=?,R33_MORE_THAN_10_YEARS=?,R33_TOTAL=?,R34_PRODUCT=?," +
			"R34_UP_TO_1_MONTH=?,R34_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R34_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R34_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R34_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R34_MORE_THAN_3_YEARS_TO_5_YEARS=?,R34_MORE_THAN_5_YEARS_TO_10_YEARS=?,R34_MORE_THAN_10_YEARS=?," +
			"R34_TOTAL=?,R35_PRODUCT=?,R35_UP_TO_1_MONTH=?,R35_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R35_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R35_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R35_MORE_THAN_12_MONTH_TO_3_YEARS=?,R35_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R35_MORE_THAN_5_YEARS_TO_10_YEARS=?,R35_MORE_THAN_10_YEARS=?,R35_TOTAL=?,R36_PRODUCT=?," +
			"R36_UP_TO_1_MONTH=?,R36_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R36_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R36_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R36_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R36_MORE_THAN_3_YEARS_TO_5_YEARS=?,R36_MORE_THAN_5_YEARS_TO_10_YEARS=?,R36_MORE_THAN_10_YEARS=?," +
			"R36_TOTAL=?,R37_PRODUCT=?,R37_UP_TO_1_MONTH=?,R37_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R37_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R37_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R37_MORE_THAN_12_MONTH_TO_3_YEARS=?,R37_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R37_MORE_THAN_5_YEARS_TO_10_YEARS=?,R37_MORE_THAN_10_YEARS=?,R37_TOTAL=?,R38_PRODUCT=?," +
			"R38_UP_TO_1_MONTH=?,R38_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R38_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R38_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R38_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R38_MORE_THAN_3_YEARS_TO_5_YEARS=?,R38_MORE_THAN_5_YEARS_TO_10_YEARS=?,R38_MORE_THAN_10_YEARS=?," +
			"R38_TOTAL=?,R39_PRODUCT=?,R39_UP_TO_1_MONTH=?,R39_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R39_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R39_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R39_MORE_THAN_12_MONTH_TO_3_YEARS=?,R39_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R39_MORE_THAN_5_YEARS_TO_10_YEARS=?,R39_MORE_THAN_10_YEARS=?,R39_TOTAL=?,R40_PRODUCT=?," +
			"R40_UP_TO_1_MONTH=?,R40_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R40_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R40_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R40_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R40_MORE_THAN_3_YEARS_TO_5_YEARS=?,R40_MORE_THAN_5_YEARS_TO_10_YEARS=?,R40_MORE_THAN_10_YEARS=?," +
			"R40_TOTAL=?,R41_PRODUCT=?,R41_UP_TO_1_MONTH=?,R41_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R41_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R41_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R41_MORE_THAN_12_MONTH_TO_3_YEARS=?,R41_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R41_MORE_THAN_5_YEARS_TO_10_YEARS=?,R41_MORE_THAN_10_YEARS=?,R41_TOTAL=?,R42_PRODUCT=?," +
			"R42_UP_TO_1_MONTH=?,R42_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R42_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R42_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R42_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R42_MORE_THAN_3_YEARS_TO_5_YEARS=?,R42_MORE_THAN_5_YEARS_TO_10_YEARS=?,R42_MORE_THAN_10_YEARS=?," +
			"R42_TOTAL=?,R43_PRODUCT=?,R43_UP_TO_1_MONTH=?,R43_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R43_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R43_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R43_MORE_THAN_12_MONTH_TO_3_YEARS=?,R43_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R43_MORE_THAN_5_YEARS_TO_10_YEARS=?,R43_MORE_THAN_10_YEARS=?,R43_TOTAL=?,R44_PRODUCT=?," +
			"R44_UP_TO_1_MONTH=?,R44_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R44_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R44_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R44_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R44_MORE_THAN_3_YEARS_TO_5_YEARS=?,R44_MORE_THAN_5_YEARS_TO_10_YEARS=?,R44_MORE_THAN_10_YEARS=?," +
			"R44_TOTAL=?,R45_PRODUCT=?,R45_UP_TO_1_MONTH=?,R45_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R45_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R45_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R45_MORE_THAN_12_MONTH_TO_3_YEARS=?,R45_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R45_MORE_THAN_5_YEARS_TO_10_YEARS=?,R45_MORE_THAN_10_YEARS=?,R45_TOTAL=?,R46_PRODUCT=?," +
			"R46_UP_TO_1_MONTH=?,R46_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R46_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R46_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R46_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R46_MORE_THAN_3_YEARS_TO_5_YEARS=?,R46_MORE_THAN_5_YEARS_TO_10_YEARS=?,R46_MORE_THAN_10_YEARS=?," +
			"R46_TOTAL=?,R47_NON_RATIO_SENSATIVE_ITEMS=?,R47_TOTAL=?,R48_NON_RATIO_SENSATIVE_ITEMS=?,R48_TOTAL=?," +
			"R49_NON_RATIO_SENSATIVE_ITEMS=?,R49_TOTAL=?,R50_NON_RATIO_SENSATIVE_ITEMS=?,R50_TOTAL=?," +
			"R51_NON_RATIO_SENSATIVE_ITEMS=?,R51_TOTAL=?,R52_NON_RATIO_SENSATIVE_ITEMS=?,R52_TOTAL=?," +
			"R53_NON_RATIO_SENSATIVE_ITEMS=?,R53_TOTAL=?,R54_NON_RATIO_SENSATIVE_ITEMS=?,R54_TOTAL=?," +
			"R55_NON_RATIO_SENSATIVE_ITEMS=?,R55_TOTAL=?,R56_NON_RATIO_SENSATIVE_ITEMS=?,R56_TOTAL=?," +
			"R57_NON_RATIO_SENSATIVE_ITEMS=?,R57_TOTAL=?,R58_NON_RATIO_SENSATIVE_ITEMS=?,R58_TOTAL=?," +
			"R59_PRODUCT=?,R59_UP_TO_1_MONTH=?,R59_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R59_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R59_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R59_MORE_THAN_12_MONTH_TO_3_YEARS=?,R59_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R59_MORE_THAN_5_YEARS_TO_10_YEARS=?,R59_MORE_THAN_10_YEARS=?,R59_NON_RATIO_SENSATIVE_ITEMS=?," +
			"R59_TOTAL=?,R60_PRODUCT=?,R60_UP_TO_1_MONTH=?,R60_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R60_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R60_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R60_MORE_THAN_12_MONTH_TO_3_YEARS=?,R60_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R60_MORE_THAN_5_YEARS_TO_10_YEARS=?,R60_MORE_THAN_10_YEARS=?,R60_TOTAL=?,R61_PRODUCT=?," +
			"R61_UP_TO_1_MONTH=?,R61_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R61_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R61_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R61_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R61_MORE_THAN_3_YEARS_TO_5_YEARS=?,R61_MORE_THAN_5_YEARS_TO_10_YEARS=?,R61_MORE_THAN_10_YEARS=?," +
			"R61_TOTAL=?,R62_PRODUCT=?,R62_UP_TO_1_MONTH=?,R62_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R62_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R62_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R62_MORE_THAN_12_MONTH_TO_3_YEARS=?,R62_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R62_MORE_THAN_5_YEARS_TO_10_YEARS=?,R62_MORE_THAN_10_YEARS=?,R62_TOTAL=?,R63_PRODUCT=?," +
			"R63_UP_TO_1_MONTH=?,R63_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R63_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R63_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R63_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R63_MORE_THAN_3_YEARS_TO_5_YEARS=?,R63_MORE_THAN_5_YEARS_TO_10_YEARS=?,R63_MORE_THAN_10_YEARS=?," +
			"R63_TOTAL=?,R64_PRODUCT=?,R64_UP_TO_1_MONTH=?,R64_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R64_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R64_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R64_MORE_THAN_12_MONTH_TO_3_YEARS=?,R64_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R64_MORE_THAN_5_YEARS_TO_10_YEARS=?,R64_MORE_THAN_10_YEARS=?,R64_TOTAL=?,R65_PRODUCT=?," +
			"R65_UP_TO_1_MONTH=?,R65_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R65_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R65_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R65_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R65_MORE_THAN_3_YEARS_TO_5_YEARS=?,R65_MORE_THAN_5_YEARS_TO_10_YEARS=?,R65_MORE_THAN_10_YEARS=?," +
			"R65_TOTAL=?,R66_PRODUCT=?,R66_UP_TO_1_MONTH=?,R66_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R66_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R66_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R66_MORE_THAN_12_MONTH_TO_3_YEARS=?,R66_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R66_MORE_THAN_5_YEARS_TO_10_YEARS=?,R66_MORE_THAN_10_YEARS=?,R66_TOTAL=?,R67_PRODUCT=?," +
			"R67_UP_TO_1_MONTH=?,R67_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R67_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R67_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R67_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R67_MORE_THAN_3_YEARS_TO_5_YEARS=?,R67_MORE_THAN_5_YEARS_TO_10_YEARS=?,R67_MORE_THAN_10_YEARS=?," +
			"R67_TOTAL=?,R68_PRODUCT=?,R68_UP_TO_1_MONTH=?,R68_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R68_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R68_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R68_MORE_THAN_12_MONTH_TO_3_YEARS=?,R68_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R68_MORE_THAN_5_YEARS_TO_10_YEARS=?,R68_MORE_THAN_10_YEARS=?,R68_TOTAL=?,R69_PRODUCT=?," +
			"R69_UP_TO_1_MONTH=?,R69_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R69_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R69_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R69_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R69_MORE_THAN_3_YEARS_TO_5_YEARS=?,R69_MORE_THAN_5_YEARS_TO_10_YEARS=?,R69_MORE_THAN_10_YEARS=?," +
			"R69_TOTAL=?,R70_PRODUCT=?,R70_UP_TO_1_MONTH=?,R70_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R70_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R70_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R70_MORE_THAN_12_MONTH_TO_3_YEARS=?,R70_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R70_MORE_THAN_5_YEARS_TO_10_YEARS=?,R70_MORE_THAN_10_YEARS=?,R70_TOTAL=?,R71_PRODUCT=?," +
			"R71_UP_TO_1_MONTH=?,R71_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R71_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R71_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R71_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R71_MORE_THAN_3_YEARS_TO_5_YEARS=?,R71_MORE_THAN_5_YEARS_TO_10_YEARS=?,R71_MORE_THAN_10_YEARS=?," +
			"R71_TOTAL=?,R72_PRODUCT=?,R72_UP_TO_1_MONTH=?,R72_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R72_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R72_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R72_MORE_THAN_12_MONTH_TO_3_YEARS=?,R72_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R72_MORE_THAN_5_YEARS_TO_10_YEARS=?,R72_MORE_THAN_10_YEARS=?,R72_TOTAL=?,R73_PRODUCT=?," +
			"R73_UP_TO_1_MONTH=?,R73_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R73_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R73_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R73_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R73_MORE_THAN_3_YEARS_TO_5_YEARS=?,R73_MORE_THAN_5_YEARS_TO_10_YEARS=?,R73_MORE_THAN_10_YEARS=?," +
			"R73_TOTAL=?,R74_PRODUCT=?,R74_UP_TO_1_MONTH=?,R74_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R74_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R74_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R74_MORE_THAN_12_MONTH_TO_3_YEARS=?,R74_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R74_MORE_THAN_5_YEARS_TO_10_YEARS=?,R74_MORE_THAN_10_YEARS=?,R74_TOTAL=?,R75_PRODUCT=?," +
			"R75_UP_TO_1_MONTH=?,R75_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R75_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R75_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R75_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R75_MORE_THAN_3_YEARS_TO_5_YEARS=?,R75_MORE_THAN_5_YEARS_TO_10_YEARS=?,R75_MORE_THAN_10_YEARS=?," +
			"R75_TOTAL=?,R76_PRODUCT=?,R76_UP_TO_1_MONTH=?,R76_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R76_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R76_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R76_MORE_THAN_12_MONTH_TO_3_YEARS=?,R76_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R76_MORE_THAN_5_YEARS_TO_10_YEARS=?,R76_MORE_THAN_10_YEARS=?,R76_TOTAL=?,R77_PRODUCT=?," +
			"R77_UP_TO_1_MONTH=?,R77_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R77_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R77_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R77_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R77_MORE_THAN_3_YEARS_TO_5_YEARS=?,R77_MORE_THAN_5_YEARS_TO_10_YEARS=?,R77_MORE_THAN_10_YEARS=?," +
			"R77_TOTAL=?,R78_PRODUCT=?,R78_UP_TO_1_MONTH=?,R78_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R78_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R78_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R78_MORE_THAN_12_MONTH_TO_3_YEARS=?,R78_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R78_MORE_THAN_5_YEARS_TO_10_YEARS=?,R78_MORE_THAN_10_YEARS=?,R78_TOTAL=?,R79_PRODUCT=?," +
			"R79_UP_TO_1_MONTH=?,R79_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R79_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R79_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R79_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R79_MORE_THAN_3_YEARS_TO_5_YEARS=?,R79_MORE_THAN_5_YEARS_TO_10_YEARS=?,R79_MORE_THAN_10_YEARS=?," +
			"R79_TOTAL=?,R80_PRODUCT=?,R80_UP_TO_1_MONTH=?,R80_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R80_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R80_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R80_MORE_THAN_12_MONTH_TO_3_YEARS=?,R80_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R80_MORE_THAN_5_YEARS_TO_10_YEARS=?,R80_MORE_THAN_10_YEARS=?,R80_TOTAL=?,R81_PRODUCT=?," +
			"R81_UP_TO_1_MONTH=?,R81_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R81_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R81_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R81_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R81_MORE_THAN_3_YEARS_TO_5_YEARS=?,R81_MORE_THAN_5_YEARS_TO_10_YEARS=?,R81_MORE_THAN_10_YEARS=?," +
			"R81_TOTAL=?,R82_PRODUCT=?,R82_UP_TO_1_MONTH=?,R82_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R82_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R82_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R82_MORE_THAN_12_MONTH_TO_3_YEARS=?,R82_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R82_MORE_THAN_5_YEARS_TO_10_YEARS=?,R82_MORE_THAN_10_YEARS=?,R82_TOTAL=?,R83_PRODUCT=?," +
			"R83_UP_TO_1_MONTH=?,R83_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R83_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R83_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R83_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R83_MORE_THAN_3_YEARS_TO_5_YEARS=?,R83_MORE_THAN_5_YEARS_TO_10_YEARS=?,R83_MORE_THAN_10_YEARS=?," +
			"R83_TOTAL=?,R84_NON_RATIO_SENSATIVE_ITEMS=?,R84_TOTAL=?,R85_NON_RATIO_SENSATIVE_ITEMS=?,R85_TOTAL=?," +
			"R86_NON_RATIO_SENSATIVE_ITEMS=?,R86_TOTAL=?,R87_NON_RATIO_SENSATIVE_ITEMS=?,R87_TOTAL=?," +
			"R88_NON_RATIO_SENSATIVE_ITEMS=?,R88_TOTAL=?,R89_NON_RATIO_SENSATIVE_ITEMS=?,R89_TOTAL=?," +
			"R90_NON_RATIO_SENSATIVE_ITEMS=?,R90_TOTAL=?,R91_NON_RATIO_SENSATIVE_ITEMS=?,R91_TOTAL=?," +
			"R92_PRODUCT=?,R92_UP_TO_1_MONTH=?,R92_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R92_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R92_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R92_MORE_THAN_12_MONTH_TO_3_YEARS=?,R92_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R92_MORE_THAN_5_YEARS_TO_10_YEARS=?,R92_MORE_THAN_10_YEARS=?,R92_TOTAL=?,R93_PRODUCT=?," +
			"R93_UP_TO_1_MONTH=?,R93_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R93_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R93_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R93_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R93_MORE_THAN_3_YEARS_TO_5_YEARS=?,R93_MORE_THAN_5_YEARS_TO_10_YEARS=?,R93_MORE_THAN_10_YEARS=?," +
			"R93_TOTAL=?,R94_PRODUCT=?,R94_UP_TO_1_MONTH=?,R94_MORE_THAN_1_MONTH_TO_3_MONTHS=?," +
			"R94_MORE_THAN_3_MONTH_TO_6_MONTHS=?,R94_MORE_THAN_6_MONTH_TO_12_MONTHS=?," +
			"R94_MORE_THAN_12_MONTH_TO_3_YEARS=?,R94_MORE_THAN_3_YEARS_TO_5_YEARS=?," +
			"R94_MORE_THAN_5_YEARS_TO_10_YEARS=?,R94_MORE_THAN_10_YEARS=?,R94_TOTAL=?," +
			"R95_NON_RATIO_SENSATIVE_ITEMS=?,R95_TOTAL=?,R96_PRODUCT=?,R96_UP_TO_1_MONTH=?," +
			"R96_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R96_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R96_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R96_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R96_MORE_THAN_3_YEARS_TO_5_YEARS=?,R96_MORE_THAN_5_YEARS_TO_10_YEARS=?,R96_MORE_THAN_10_YEARS=?," +
			"R96_NON_RATIO_SENSATIVE_ITEMS=?,R96_TOTAL=?,R97_PRODUCT=?,R97_UP_TO_1_MONTH=?," +
			"R97_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R97_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R97_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R97_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R97_MORE_THAN_3_YEARS_TO_5_YEARS=?,R97_MORE_THAN_5_YEARS_TO_10_YEARS=?,R97_MORE_THAN_10_YEARS=?," +
			"R97_NON_RATIO_SENSATIVE_ITEMS=?,R97_TOTAL=?,R98_PRODUCT=?,R98_UP_TO_1_MONTH=?," +
			"R98_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R98_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R98_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R98_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R98_MORE_THAN_3_YEARS_TO_5_YEARS=?,R98_MORE_THAN_5_YEARS_TO_10_YEARS=?,R98_MORE_THAN_10_YEARS=?," +
			"R98_NON_RATIO_SENSATIVE_ITEMS=?,R98_TOTAL=?,R99_PRODUCT=?,R99_UP_TO_1_MONTH=?," +
			"R99_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R99_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R99_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R99_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R99_MORE_THAN_3_YEARS_TO_5_YEARS=?,R99_MORE_THAN_5_YEARS_TO_10_YEARS=?,R99_MORE_THAN_10_YEARS=?," +
			"R99_NON_RATIO_SENSATIVE_ITEMS=?,R99_TOTAL=?,R100_PRODUCT=?,R100_UP_TO_1_MONTH=?," +
			"R100_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R100_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R100_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R100_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R100_MORE_THAN_3_YEARS_TO_5_YEARS=?,R100_MORE_THAN_5_YEARS_TO_10_YEARS=?,R100_MORE_THAN_10_YEARS=?," +
			"R100_NON_RATIO_SENSATIVE_ITEMS=?,R100_TOTAL=?,R101_PRODUCT=?,R101_UP_TO_1_MONTH=?," +
			"R101_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R101_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R101_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R101_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R101_MORE_THAN_3_YEARS_TO_5_YEARS=?,R101_MORE_THAN_5_YEARS_TO_10_YEARS=?,R101_MORE_THAN_10_YEARS=?," +
			"R101_NON_RATIO_SENSATIVE_ITEMS=?,R101_TOTAL=?,R102_PRODUCT=?,R102_UP_TO_1_MONTH=?," +
			"R102_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R102_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R102_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R102_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R102_MORE_THAN_3_YEARS_TO_5_YEARS=?,R102_MORE_THAN_5_YEARS_TO_10_YEARS=?,R102_MORE_THAN_10_YEARS=?," +
			"R102_NON_RATIO_SENSATIVE_ITEMS=?,R102_TOTAL=?,R103_PRODUCT=?,R103_UP_TO_1_MONTH=?," +
			"R103_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R103_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R103_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R103_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R103_MORE_THAN_3_YEARS_TO_5_YEARS=?,R103_MORE_THAN_5_YEARS_TO_10_YEARS=?,R103_MORE_THAN_10_YEARS=?," +
			"R103_NON_RATIO_SENSATIVE_ITEMS=?,R103_TOTAL=?,R104_PRODUCT=?,R104_UP_TO_1_MONTH=?," +
			"R104_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R104_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R104_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R104_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R104_MORE_THAN_3_YEARS_TO_5_YEARS=?,R104_MORE_THAN_5_YEARS_TO_10_YEARS=?,R104_MORE_THAN_10_YEARS=?," +
			"R104_NON_RATIO_SENSATIVE_ITEMS=?,R104_TOTAL=?,R105_PRODUCT=?,R105_UP_TO_1_MONTH=?," +
			"R105_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R105_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R105_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R105_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R105_MORE_THAN_3_YEARS_TO_5_YEARS=?,R105_MORE_THAN_5_YEARS_TO_10_YEARS=?,R105_MORE_THAN_10_YEARS=?," +
			"R105_NON_RATIO_SENSATIVE_ITEMS=?,R105_TOTAL=?,R106_PRODUCT=?,R106_UP_TO_1_MONTH=?," +
			"R106_MORE_THAN_1_MONTH_TO_3_MONTHS=?,R106_MORE_THAN_3_MONTH_TO_6_MONTHS=?," +
			"R106_MORE_THAN_6_MONTH_TO_12_MONTHS=?,R106_MORE_THAN_12_MONTH_TO_3_YEARS=?," +
			"R106_MORE_THAN_3_YEARS_TO_5_YEARS=?,R106_MORE_THAN_5_YEARS_TO_10_YEARS=?,R106_MORE_THAN_10_YEARS=?," +
			"R106_NON_RATIO_SENSATIVE_ITEMS=?,R106_TOTAL=?,REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?," +
			"REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=?" +
			" WHERE REPORT_DATE=?";

	public void updateSummary(M_IRB_Summary_Entity e) {
		Object[] params = new Object[] {
			e.getR10product(), e.getR10_upTo1Month(), e.getR10_moreThan1MonthTo3Months(), e.getR10_moreThan3MonthTo6Months(), 
			e.getR10_moreThan6MonthTo12Months(), e.getR10_moreThan12MonthTo3Years(), e.getR10_moreThan3YearsTo5Years(), e.getR10_moreThan5YearsTo10Years(), 
			e.getR10_moreThan10Years(), e.getR10_nonRatioSensativeItems(), e.getR10_total(), e.getR11product(), 
			e.getR11_upTo1Month(), e.getR11_moreThan1MonthTo3Months(), e.getR11_moreThan3MonthTo6Months(), e.getR11_moreThan6MonthTo12Months(), 
			e.getR11_moreThan12MonthTo3Years(), e.getR11_moreThan3YearsTo5Years(), e.getR11_moreThan5YearsTo10Years(), e.getR11_moreThan10Years(), 
			e.getR11_total(), e.getR12product(), e.getR12_upTo1Month(), e.getR12_moreThan1MonthTo3Months(), 
			e.getR12_moreThan3MonthTo6Months(), e.getR12_moreThan6MonthTo12Months(), e.getR12_moreThan12MonthTo3Years(), e.getR12_moreThan3YearsTo5Years(), 
			e.getR12_moreThan5YearsTo10Years(), e.getR12_moreThan10Years(), e.getR12_total(), e.getR13product(), 
			e.getR13_upTo1Month(), e.getR13_moreThan1MonthTo3Months(), e.getR13_moreThan3MonthTo6Months(), e.getR13_moreThan6MonthTo12Months(), 
			e.getR13_moreThan12MonthTo3Years(), e.getR13_moreThan3YearsTo5Years(), e.getR13_moreThan5YearsTo10Years(), e.getR13_moreThan10Years(), 
			e.getR13_total(), e.getR14product(), e.getR14_upTo1Month(), e.getR14_moreThan1MonthTo3Months(), 
			e.getR14_moreThan3MonthTo6Months(), e.getR14_moreThan6MonthTo12Months(), e.getR14_moreThan12MonthTo3Years(), e.getR14_moreThan3YearsTo5Years(), 
			e.getR14_moreThan5YearsTo10Years(), e.getR14_moreThan10Years(), e.getR14_total(), e.getR15product(), 
			e.getR15_upTo1Month(), e.getR15_moreThan1MonthTo3Months(), e.getR15_moreThan3MonthTo6Months(), e.getR15_moreThan6MonthTo12Months(), 
			e.getR15_moreThan12MonthTo3Years(), e.getR15_moreThan3YearsTo5Years(), e.getR15_moreThan5YearsTo10Years(), e.getR15_moreThan10Years(), 
			e.getR15_total(), e.getR16product(), e.getR16_upTo1Month(), e.getR16_moreThan1MonthTo3Months(), 
			e.getR16_moreThan3MonthTo6Months(), e.getR16_moreThan6MonthTo12Months(), e.getR16_moreThan12MonthTo3Years(), e.getR16_moreThan3YearsTo5Years(), 
			e.getR16_moreThan5YearsTo10Years(), e.getR16_moreThan10Years(), e.getR16_total(), e.getR17product(), 
			e.getR17_upTo1Month(), e.getR17_moreThan1MonthTo3Months(), e.getR17_moreThan3MonthTo6Months(), e.getR17_moreThan6MonthTo12Months(), 
			e.getR17_moreThan12MonthTo3Years(), e.getR17_moreThan3YearsTo5Years(), e.getR17_moreThan5YearsTo10Years(), e.getR17_moreThan10Years(), 
			e.getR17_total(), e.getR18product(), e.getR18_upTo1Month(), e.getR18_moreThan1MonthTo3Months(), 
			e.getR18_moreThan3MonthTo6Months(), e.getR18_moreThan6MonthTo12Months(), e.getR18_moreThan12MonthTo3Years(), e.getR18_moreThan3YearsTo5Years(), 
			e.getR18_moreThan5YearsTo10Years(), e.getR18_moreThan10Years(), e.getR18_total(), e.getR19product(), 
			e.getR19_upTo1Month(), e.getR19_moreThan1MonthTo3Months(), e.getR19_moreThan3MonthTo6Months(), e.getR19_moreThan6MonthTo12Months(), 
			e.getR19_moreThan12MonthTo3Years(), e.getR19_moreThan3YearsTo5Years(), e.getR19_moreThan5YearsTo10Years(), e.getR19_moreThan10Years(), 
			e.getR19_total(), e.getR20product(), e.getR20_upTo1Month(), e.getR20_moreThan1MonthTo3Months(), 
			e.getR20_moreThan3MonthTo6Months(), e.getR20_moreThan6MonthTo12Months(), e.getR20_moreThan12MonthTo3Years(), e.getR20_moreThan3YearsTo5Years(), 
			e.getR20_moreThan5YearsTo10Years(), e.getR20_moreThan10Years(), e.getR20_total(), e.getR21product(), 
			e.getR21_upTo1Month(), e.getR21_moreThan1MonthTo3Months(), e.getR21_moreThan3MonthTo6Months(), e.getR21_moreThan6MonthTo12Months(), 
			e.getR21_moreThan12MonthTo3Years(), e.getR21_moreThan3YearsTo5Years(), e.getR21_moreThan5YearsTo10Years(), e.getR21_moreThan10Years(), 
			e.getR21_total(), e.getR22product(), e.getR22_upTo1Month(), e.getR22_moreThan1MonthTo3Months(), 
			e.getR22_moreThan3MonthTo6Months(), e.getR22_moreThan6MonthTo12Months(), e.getR22_moreThan12MonthTo3Years(), e.getR22_moreThan3YearsTo5Years(), 
			e.getR22_moreThan5YearsTo10Years(), e.getR22_moreThan10Years(), e.getR22_total(), e.getR23product(), 
			e.getR23_upTo1Month(), e.getR23_moreThan1MonthTo3Months(), e.getR23_moreThan3MonthTo6Months(), e.getR23_moreThan6MonthTo12Months(), 
			e.getR23_moreThan12MonthTo3Years(), e.getR23_moreThan3YearsTo5Years(), e.getR23_moreThan5YearsTo10Years(), e.getR23_moreThan10Years(), 
			e.getR23_total(), e.getR24product(), e.getR24_upTo1Month(), e.getR24_moreThan1MonthTo3Months(), 
			e.getR24_moreThan3MonthTo6Months(), e.getR24_moreThan6MonthTo12Months(), e.getR24_moreThan12MonthTo3Years(), e.getR24_moreThan3YearsTo5Years(), 
			e.getR24_moreThan5YearsTo10Years(), e.getR24_moreThan10Years(), e.getR24_total(), e.getR25product(), 
			e.getR25_upTo1Month(), e.getR25_moreThan1MonthTo3Months(), e.getR25_moreThan3MonthTo6Months(), e.getR25_moreThan6MonthTo12Months(), 
			e.getR25_moreThan12MonthTo3Years(), e.getR25_moreThan3YearsTo5Years(), e.getR25_moreThan5YearsTo10Years(), e.getR25_moreThan10Years(), 
			e.getR25_total(), e.getR26product(), e.getR26_upTo1Month(), e.getR26_moreThan1MonthTo3Months(), 
			e.getR26_moreThan3MonthTo6Months(), e.getR26_moreThan6MonthTo12Months(), e.getR26_moreThan12MonthTo3Years(), e.getR26_moreThan3YearsTo5Years(), 
			e.getR26_moreThan5YearsTo10Years(), e.getR26_moreThan10Years(), e.getR26_total(), e.getR27product(), 
			e.getR27_upTo1Month(), e.getR27_moreThan1MonthTo3Months(), e.getR27_moreThan3MonthTo6Months(), e.getR27_moreThan6MonthTo12Months(), 
			e.getR27_moreThan12MonthTo3Years(), e.getR27_moreThan3YearsTo5Years(), e.getR27_moreThan5YearsTo10Years(), e.getR27_moreThan10Years(), 
			e.getR27_total(), e.getR28product(), e.getR28_upTo1Month(), e.getR28_moreThan1MonthTo3Months(), 
			e.getR28_moreThan3MonthTo6Months(), e.getR28_moreThan6MonthTo12Months(), e.getR28_moreThan12MonthTo3Years(), e.getR28_moreThan3YearsTo5Years(), 
			e.getR28_moreThan5YearsTo10Years(), e.getR28_moreThan10Years(), e.getR28_total(), e.getR29product(), 
			e.getR29_upTo1Month(), e.getR29_moreThan1MonthTo3Months(), e.getR29_moreThan3MonthTo6Months(), e.getR29_moreThan6MonthTo12Months(), 
			e.getR29_moreThan12MonthTo3Years(), e.getR29_moreThan3YearsTo5Years(), e.getR29_moreThan5YearsTo10Years(), e.getR29_moreThan10Years(), 
			e.getR29_total(), e.getR30product(), e.getR30_upTo1Month(), e.getR30_moreThan1MonthTo3Months(), 
			e.getR30_moreThan3MonthTo6Months(), e.getR30_moreThan6MonthTo12Months(), e.getR30_moreThan12MonthTo3Years(), e.getR30_moreThan3YearsTo5Years(), 
			e.getR30_moreThan5YearsTo10Years(), e.getR30_moreThan10Years(), e.getR30_total(), e.getR31product(), 
			e.getR31_upTo1Month(), e.getR31_moreThan1MonthTo3Months(), e.getR31_moreThan3MonthTo6Months(), e.getR31_moreThan6MonthTo12Months(), 
			e.getR31_moreThan12MonthTo3Years(), e.getR31_moreThan3YearsTo5Years(), e.getR31_moreThan5YearsTo10Years(), e.getR31_moreThan10Years(), 
			e.getR31_total(), e.getR32product(), e.getR32_upTo1Month(), e.getR32_moreThan1MonthTo3Months(), 
			e.getR32_moreThan3MonthTo6Months(), e.getR32_moreThan6MonthTo12Months(), e.getR32_moreThan12MonthTo3Years(), e.getR32_moreThan3YearsTo5Years(), 
			e.getR32_moreThan5YearsTo10Years(), e.getR32_moreThan10Years(), e.getR32_total(), e.getR33product(), 
			e.getR33_upTo1Month(), e.getR33_moreThan1MonthTo3Months(), e.getR33_moreThan3MonthTo6Months(), e.getR33_moreThan6MonthTo12Months(), 
			e.getR33_moreThan12MonthTo3Years(), e.getR33_moreThan3YearsTo5Years(), e.getR33_moreThan5YearsTo10Years(), e.getR33_moreThan10Years(), 
			e.getR33_total(), e.getR34product(), e.getR34_upTo1Month(), e.getR34_moreThan1MonthTo3Months(), 
			e.getR34_moreThan3MonthTo6Months(), e.getR34_moreThan6MonthTo12Months(), e.getR34_moreThan12MonthTo3Years(), e.getR34_moreThan3YearsTo5Years(), 
			e.getR34_moreThan5YearsTo10Years(), e.getR34_moreThan10Years(), e.getR34_total(), e.getR35product(), 
			e.getR35_upTo1Month(), e.getR35_moreThan1MonthTo3Months(), e.getR35_moreThan3MonthTo6Months(), e.getR35_moreThan6MonthTo12Months(), 
			e.getR35_moreThan12MonthTo3Years(), e.getR35_moreThan3YearsTo5Years(), e.getR35_moreThan5YearsTo10Years(), e.getR35_moreThan10Years(), 
			e.getR35_total(), e.getR36product(), e.getR36_upTo1Month(), e.getR36_moreThan1MonthTo3Months(), 
			e.getR36_moreThan3MonthTo6Months(), e.getR36_moreThan6MonthTo12Months(), e.getR36_moreThan12MonthTo3Years(), e.getR36_moreThan3YearsTo5Years(), 
			e.getR36_moreThan5YearsTo10Years(), e.getR36_moreThan10Years(), e.getR36_total(), e.getR37product(), 
			e.getR37_upTo1Month(), e.getR37_moreThan1MonthTo3Months(), e.getR37_moreThan3MonthTo6Months(), e.getR37_moreThan6MonthTo12Months(), 
			e.getR37_moreThan12MonthTo3Years(), e.getR37_moreThan3YearsTo5Years(), e.getR37_moreThan5YearsTo10Years(), e.getR37_moreThan10Years(), 
			e.getR37_total(), e.getR38product(), e.getR38_upTo1Month(), e.getR38_moreThan1MonthTo3Months(), 
			e.getR38_moreThan3MonthTo6Months(), e.getR38_moreThan6MonthTo12Months(), e.getR38_moreThan12MonthTo3Years(), e.getR38_moreThan3YearsTo5Years(), 
			e.getR38_moreThan5YearsTo10Years(), e.getR38_moreThan10Years(), e.getR38_total(), e.getR39product(), 
			e.getR39_upTo1Month(), e.getR39_moreThan1MonthTo3Months(), e.getR39_moreThan3MonthTo6Months(), e.getR39_moreThan6MonthTo12Months(), 
			e.getR39_moreThan12MonthTo3Years(), e.getR39_moreThan3YearsTo5Years(), e.getR39_moreThan5YearsTo10Years(), e.getR39_moreThan10Years(), 
			e.getR39_total(), e.getR40product(), e.getR40_upTo1Month(), e.getR40_moreThan1MonthTo3Months(), 
			e.getR40_moreThan3MonthTo6Months(), e.getR40_moreThan6MonthTo12Months(), e.getR40_moreThan12MonthTo3Years(), e.getR40_moreThan3YearsTo5Years(), 
			e.getR40_moreThan5YearsTo10Years(), e.getR40_moreThan10Years(), e.getR40_total(), e.getR41product(), 
			e.getR41_upTo1Month(), e.getR41_moreThan1MonthTo3Months(), e.getR41_moreThan3MonthTo6Months(), e.getR41_moreThan6MonthTo12Months(), 
			e.getR41_moreThan12MonthTo3Years(), e.getR41_moreThan3YearsTo5Years(), e.getR41_moreThan5YearsTo10Years(), e.getR41_moreThan10Years(), 
			e.getR41_total(), e.getR42product(), e.getR42_upTo1Month(), e.getR42_moreThan1MonthTo3Months(), 
			e.getR42_moreThan3MonthTo6Months(), e.getR42_moreThan6MonthTo12Months(), e.getR42_moreThan12MonthTo3Years(), e.getR42_moreThan3YearsTo5Years(), 
			e.getR42_moreThan5YearsTo10Years(), e.getR42_moreThan10Years(), e.getR42_total(), e.getR43product(), 
			e.getR43_upTo1Month(), e.getR43_moreThan1MonthTo3Months(), e.getR43_moreThan3MonthTo6Months(), e.getR43_moreThan6MonthTo12Months(), 
			e.getR43_moreThan12MonthTo3Years(), e.getR43_moreThan3YearsTo5Years(), e.getR43_moreThan5YearsTo10Years(), e.getR43_moreThan10Years(), 
			e.getR43_total(), e.getR44product(), e.getR44_upTo1Month(), e.getR44_moreThan1MonthTo3Months(), 
			e.getR44_moreThan3MonthTo6Months(), e.getR44_moreThan6MonthTo12Months(), e.getR44_moreThan12MonthTo3Years(), e.getR44_moreThan3YearsTo5Years(), 
			e.getR44_moreThan5YearsTo10Years(), e.getR44_moreThan10Years(), e.getR44_total(), e.getR45product(), 
			e.getR45_upTo1Month(), e.getR45_moreThan1MonthTo3Months(), e.getR45_moreThan3MonthTo6Months(), e.getR45_moreThan6MonthTo12Months(), 
			e.getR45_moreThan12MonthTo3Years(), e.getR45_moreThan3YearsTo5Years(), e.getR45_moreThan5YearsTo10Years(), e.getR45_moreThan10Years(), 
			e.getR45_total(), e.getR46product(), e.getR46_upTo1Month(), e.getR46_moreThan1MonthTo3Months(), 
			e.getR46_moreThan3MonthTo6Months(), e.getR46_moreThan6MonthTo12Months(), e.getR46_moreThan12MonthTo3Years(), e.getR46_moreThan3YearsTo5Years(), 
			e.getR46_moreThan5YearsTo10Years(), e.getR46_moreThan10Years(), e.getR46_total(), e.getR47_nonRatioSensativeItems(), 
			e.getR47_total(), e.getR48_nonRatioSensativeItems(), e.getR48_total(), e.getR49_nonRatioSensativeItems(), 
			e.getR49_total(), e.getR50_nonRatioSensativeItems(), e.getR50_total(), e.getR51_nonRatioSensativeItems(), 
			e.getR51_total(), e.getR52_nonRatioSensativeItems(), e.getR52_total(), e.getR53_nonRatioSensativeItems(), 
			e.getR53_total(), e.getR54_nonRatioSensativeItems(), e.getR54_total(), e.getR55_nonRatioSensativeItems(), 
			e.getR55_total(), e.getR56_nonRatioSensativeItems(), e.getR56_total(), e.getR57_nonRatioSensativeItems(), 
			e.getR57_total(), e.getR58_nonRatioSensativeItems(), e.getR58_total(), e.getR59product(), 
			e.getR59_upTo1Month(), e.getR59_moreThan1MonthTo3Months(), e.getR59_moreThan3MonthTo6Months(), e.getR59_moreThan6MonthTo12Months(), 
			e.getR59_moreThan12MonthTo3Years(), e.getR59_moreThan3YearsTo5Years(), e.getR59_moreThan5YearsTo10Years(), e.getR59_moreThan10Years(), 
			e.getR59_nonRatioSensativeItems(), e.getR59_total(), e.getR60product(), e.getR60_upTo1Month(), 
			e.getR60_moreThan1MonthTo3Months(), e.getR60_moreThan3MonthTo6Months(), e.getR60_moreThan6MonthTo12Months(), e.getR60_moreThan12MonthTo3Years(), 
			e.getR60_moreThan3YearsTo5Years(), e.getR60_moreThan5YearsTo10Years(), e.getR60_moreThan10Years(), e.getR60_total(), 
			e.getR61product(), e.getR61_upTo1Month(), e.getR61_moreThan1MonthTo3Months(), e.getR61_moreThan3MonthTo6Months(), 
			e.getR61_moreThan6MonthTo12Months(), e.getR61_moreThan12MonthTo3Years(), e.getR61_moreThan3YearsTo5Years(), e.getR61_moreThan5YearsTo10Years(), 
			e.getR61_moreThan10Years(), e.getR61_total(), e.getR62product(), e.getR62_upTo1Month(), 
			e.getR62_moreThan1MonthTo3Months(), e.getR62_moreThan3MonthTo6Months(), e.getR62_moreThan6MonthTo12Months(), e.getR62_moreThan12MonthTo3Years(), 
			e.getR62_moreThan3YearsTo5Years(), e.getR62_moreThan5YearsTo10Years(), e.getR62_moreThan10Years(), e.getR62_total(), 
			e.getR63product(), e.getR63_upTo1Month(), e.getR63_moreThan1MonthTo3Months(), e.getR63_moreThan3MonthTo6Months(), 
			e.getR63_moreThan6MonthTo12Months(), e.getR63_moreThan12MonthTo3Years(), e.getR63_moreThan3YearsTo5Years(), e.getR63_moreThan5YearsTo10Years(), 
			e.getR63_moreThan10Years(), e.getR63_total(), e.getR64product(), e.getR64_upTo1Month(), 
			e.getR64_moreThan1MonthTo3Months(), e.getR64_moreThan3MonthTo6Months(), e.getR64_moreThan6MonthTo12Months(), e.getR64_moreThan12MonthTo3Years(), 
			e.getR64_moreThan3YearsTo5Years(), e.getR64_moreThan5YearsTo10Years(), e.getR64_moreThan10Years(), e.getR64_total(), 
			e.getR65product(), e.getR65_upTo1Month(), e.getR65_moreThan1MonthTo3Months(), e.getR65_moreThan3MonthTo6Months(), 
			e.getR65_moreThan6MonthTo12Months(), e.getR65_moreThan12MonthTo3Years(), e.getR65_moreThan3YearsTo5Years(), e.getR65_moreThan5YearsTo10Years(), 
			e.getR65_moreThan10Years(), e.getR65_total(), e.getR66product(), e.getR66_upTo1Month(), 
			e.getR66_moreThan1MonthTo3Months(), e.getR66_moreThan3MonthTo6Months(), e.getR66_moreThan6MonthTo12Months(), e.getR66_moreThan12MonthTo3Years(), 
			e.getR66_moreThan3YearsTo5Years(), e.getR66_moreThan5YearsTo10Years(), e.getR66_moreThan10Years(), e.getR66_total(), 
			e.getR67product(), e.getR67_upTo1Month(), e.getR67_moreThan1MonthTo3Months(), e.getR67_moreThan3MonthTo6Months(), 
			e.getR67_moreThan6MonthTo12Months(), e.getR67_moreThan12MonthTo3Years(), e.getR67_moreThan3YearsTo5Years(), e.getR67_moreThan5YearsTo10Years(), 
			e.getR67_moreThan10Years(), e.getR67_total(), e.getR68product(), e.getR68_upTo1Month(), 
			e.getR68_moreThan1MonthTo3Months(), e.getR68_moreThan3MonthTo6Months(), e.getR68_moreThan6MonthTo12Months(), e.getR68_moreThan12MonthTo3Years(), 
			e.getR68_moreThan3YearsTo5Years(), e.getR68_moreThan5YearsTo10Years(), e.getR68_moreThan10Years(), e.getR68_total(), 
			e.getR69product(), e.getR69_upTo1Month(), e.getR69_moreThan1MonthTo3Months(), e.getR69_moreThan3MonthTo6Months(), 
			e.getR69_moreThan6MonthTo12Months(), e.getR69_moreThan12MonthTo3Years(), e.getR69_moreThan3YearsTo5Years(), e.getR69_moreThan5YearsTo10Years(), 
			e.getR69_moreThan10Years(), e.getR69_total(), e.getR70product(), e.getR70_upTo1Month(), 
			e.getR70_moreThan1MonthTo3Months(), e.getR70_moreThan3MonthTo6Months(), e.getR70_moreThan6MonthTo12Months(), e.getR70_moreThan12MonthTo3Years(), 
			e.getR70_moreThan3YearsTo5Years(), e.getR70_moreThan5YearsTo10Years(), e.getR70_moreThan10Years(), e.getR70_total(), 
			e.getR71product(), e.getR71_upTo1Month(), e.getR71_moreThan1MonthTo3Months(), e.getR71_moreThan3MonthTo6Months(), 
			e.getR71_moreThan6MonthTo12Months(), e.getR71_moreThan12MonthTo3Years(), e.getR71_moreThan3YearsTo5Years(), e.getR71_moreThan5YearsTo10Years(), 
			e.getR71_moreThan10Years(), e.getR71_total(), e.getR72product(), e.getR72_upTo1Month(), 
			e.getR72_moreThan1MonthTo3Months(), e.getR72_moreThan3MonthTo6Months(), e.getR72_moreThan6MonthTo12Months(), e.getR72_moreThan12MonthTo3Years(), 
			e.getR72_moreThan3YearsTo5Years(), e.getR72_moreThan5YearsTo10Years(), e.getR72_moreThan10Years(), e.getR72_total(), 
			e.getR73product(), e.getR73_upTo1Month(), e.getR73_moreThan1MonthTo3Months(), e.getR73_moreThan3MonthTo6Months(), 
			e.getR73_moreThan6MonthTo12Months(), e.getR73_moreThan12MonthTo3Years(), e.getR73_moreThan3YearsTo5Years(), e.getR73_moreThan5YearsTo10Years(), 
			e.getR73_moreThan10Years(), e.getR73_total(), e.getR74product(), e.getR74_upTo1Month(), 
			e.getR74_moreThan1MonthTo3Months(), e.getR74_moreThan3MonthTo6Months(), e.getR74_moreThan6MonthTo12Months(), e.getR74_moreThan12MonthTo3Years(), 
			e.getR74_moreThan3YearsTo5Years(), e.getR74_moreThan5YearsTo10Years(), e.getR74_moreThan10Years(), e.getR74_total(), 
			e.getR75product(), e.getR75_upTo1Month(), e.getR75_moreThan1MonthTo3Months(), e.getR75_moreThan3MonthTo6Months(), 
			e.getR75_moreThan6MonthTo12Months(), e.getR75_moreThan12MonthTo3Years(), e.getR75_moreThan3YearsTo5Years(), e.getR75_moreThan5YearsTo10Years(), 
			e.getR75_moreThan10Years(), e.getR75_total(), e.getR76product(), e.getR76_upTo1Month(), 
			e.getR76_moreThan1MonthTo3Months(), e.getR76_moreThan3MonthTo6Months(), e.getR76_moreThan6MonthTo12Months(), e.getR76_moreThan12MonthTo3Years(), 
			e.getR76_moreThan3YearsTo5Years(), e.getR76_moreThan5YearsTo10Years(), e.getR76_moreThan10Years(), e.getR76_total(), 
			e.getR77product(), e.getR77_upTo1Month(), e.getR77_moreThan1MonthTo3Months(), e.getR77_moreThan3MonthTo6Months(), 
			e.getR77_moreThan6MonthTo12Months(), e.getR77_moreThan12MonthTo3Years(), e.getR77_moreThan3YearsTo5Years(), e.getR77_moreThan5YearsTo10Years(), 
			e.getR77_moreThan10Years(), e.getR77_total(), e.getR78product(), e.getR78_upTo1Month(), 
			e.getR78_moreThan1MonthTo3Months(), e.getR78_moreThan3MonthTo6Months(), e.getR78_moreThan6MonthTo12Months(), e.getR78_moreThan12MonthTo3Years(), 
			e.getR78_moreThan3YearsTo5Years(), e.getR78_moreThan5YearsTo10Years(), e.getR78_moreThan10Years(), e.getR78_total(), 
			e.getR79product(), e.getR79_upTo1Month(), e.getR79_moreThan1MonthTo3Months(), e.getR79_moreThan3MonthTo6Months(), 
			e.getR79_moreThan6MonthTo12Months(), e.getR79_moreThan12MonthTo3Years(), e.getR79_moreThan3YearsTo5Years(), e.getR79_moreThan5YearsTo10Years(), 
			e.getR79_moreThan10Years(), e.getR79_total(), e.getR80product(), e.getR80_upTo1Month(), 
			e.getR80_moreThan1MonthTo3Months(), e.getR80_moreThan3MonthTo6Months(), e.getR80_moreThan6MonthTo12Months(), e.getR80_moreThan12MonthTo3Years(), 
			e.getR80_moreThan3YearsTo5Years(), e.getR80_moreThan5YearsTo10Years(), e.getR80_moreThan10Years(), e.getR80_total(), 
			e.getR81product(), e.getR81_upTo1Month(), e.getR81_moreThan1MonthTo3Months(), e.getR81_moreThan3MonthTo6Months(), 
			e.getR81_moreThan6MonthTo12Months(), e.getR81_moreThan12MonthTo3Years(), e.getR81_moreThan3YearsTo5Years(), e.getR81_moreThan5YearsTo10Years(), 
			e.getR81_moreThan10Years(), e.getR81_total(), e.getR82product(), e.getR82_upTo1Month(), 
			e.getR82_moreThan1MonthTo3Months(), e.getR82_moreThan3MonthTo6Months(), e.getR82_moreThan6MonthTo12Months(), e.getR82_moreThan12MonthTo3Years(), 
			e.getR82_moreThan3YearsTo5Years(), e.getR82_moreThan5YearsTo10Years(), e.getR82_moreThan10Years(), e.getR82_total(), 
			e.getR83product(), e.getR83_upTo1Month(), e.getR83_moreThan1MonthTo3Months(), e.getR83_moreThan3MonthTo6Months(), 
			e.getR83_moreThan6MonthTo12Months(), e.getR83_moreThan12MonthTo3Years(), e.getR83_moreThan3YearsTo5Years(), e.getR83_moreThan5YearsTo10Years(), 
			e.getR83_moreThan10Years(), e.getR83_total(), e.getR84_nonRatioSensativeItems(), e.getR84_total(), 
			e.getR85_nonRatioSensativeItems(), e.getR85_total(), e.getR86_nonRatioSensativeItems(), e.getR86_total(), 
			e.getR87_nonRatioSensativeItems(), e.getR87_total(), e.getR88_nonRatioSensativeItems(), e.getR88_total(), 
			e.getR89_nonRatioSensativeItems(), e.getR89_total(), e.getR90_nonRatioSensativeItems(), e.getR90_total(), 
			e.getR91_nonRatioSensativeItems(), e.getR91_total(), e.getR92product(), e.getR92_upTo1Month(), 
			e.getR92_moreThan1MonthTo3Months(), e.getR92_moreThan3MonthTo6Months(), e.getR92_moreThan6MonthTo12Months(), e.getR92_moreThan12MonthTo3Years(), 
			e.getR92_moreThan3YearsTo5Years(), e.getR92_moreThan5YearsTo10Years(), e.getR92_moreThan10Years(), e.getR92_total(), 
			e.getR93product(), e.getR93_upTo1Month(), e.getR93_moreThan1MonthTo3Months(), e.getR93_moreThan3MonthTo6Months(), 
			e.getR93_moreThan6MonthTo12Months(), e.getR93_moreThan12MonthTo3Years(), e.getR93_moreThan3YearsTo5Years(), e.getR93_moreThan5YearsTo10Years(), 
			e.getR93_moreThan10Years(), e.getR93_total(), e.getR94product(), e.getR94_upTo1Month(), 
			e.getR94_moreThan1MonthTo3Months(), e.getR94_moreThan3MonthTo6Months(), e.getR94_moreThan6MonthTo12Months(), e.getR94_moreThan12MonthTo3Years(), 
			e.getR94_moreThan3YearsTo5Years(), e.getR94_moreThan5YearsTo10Years(), e.getR94_moreThan10Years(), e.getR94_total(), 
			e.getR95_nonRatioSensativeItems(), e.getR95_total(), e.getR96product(), e.getR96_upTo1Month(), 
			e.getR96_moreThan1MonthTo3Months(), e.getR96_moreThan3MonthTo6Months(), e.getR96_moreThan6MonthTo12Months(), e.getR96_moreThan12MonthTo3Years(), 
			e.getR96_moreThan3YearsTo5Years(), e.getR96_moreThan5YearsTo10Years(), e.getR96_moreThan10Years(), e.getR96_nonRatioSensativeItems(), 
			e.getR96_total(), e.getR97product(), e.getR97_upTo1Month(), e.getR97_moreThan1MonthTo3Months(), 
			e.getR97_moreThan3MonthTo6Months(), e.getR97_moreThan6MonthTo12Months(), e.getR97_moreThan12MonthTo3Years(), e.getR97_moreThan3YearsTo5Years(), 
			e.getR97_moreThan5YearsTo10Years(), e.getR97_moreThan10Years(), e.getR97_nonRatioSensativeItems(), e.getR97_total(), 
			e.getR98product(), e.getR98_upTo1Month(), e.getR98_moreThan1MonthTo3Months(), e.getR98_moreThan3MonthTo6Months(), 
			e.getR98_moreThan6MonthTo12Months(), e.getR98_moreThan12MonthTo3Years(), e.getR98_moreThan3YearsTo5Years(), e.getR98_moreThan5YearsTo10Years(), 
			e.getR98_moreThan10Years(), e.getR98_nonRatioSensativeItems(), e.getR98_total(), e.getR99product(), 
			e.getR99_upTo1Month(), e.getR99_moreThan1MonthTo3Months(), e.getR99_moreThan3MonthTo6Months(), e.getR99_moreThan6MonthTo12Months(), 
			e.getR99_moreThan12MonthTo3Years(), e.getR99_moreThan3YearsTo5Years(), e.getR99_moreThan5YearsTo10Years(), e.getR99_moreThan10Years(), 
			e.getR99_nonRatioSensativeItems(), e.getR99_total(), e.getR100product(), e.getR100_upTo1Month(), 
			e.getR100_moreThan1MonthTo3Months(), e.getR100_moreThan3MonthTo6Months(), e.getR100_moreThan6MonthTo12Months(), e.getR100_moreThan12MonthTo3Years(), 
			e.getR100_moreThan3YearsTo5Years(), e.getR100_moreThan5YearsTo10Years(), e.getR100_moreThan10Years(), e.getR100_nonRatioSensativeItems(), 
			e.getR100_total(), e.getR101product(), e.getR101_upTo1Month(), e.getR101_moreThan1MonthTo3Months(), 
			e.getR101_moreThan3MonthTo6Months(), e.getR101_moreThan6MonthTo12Months(), e.getR101_moreThan12MonthTo3Years(), e.getR101_moreThan3YearsTo5Years(), 
			e.getR101_moreThan5YearsTo10Years(), e.getR101_moreThan10Years(), e.getR101_nonRatioSensativeItems(), e.getR101_total(), 
			e.getR102product(), e.getR102_upTo1Month(), e.getR102_moreThan1MonthTo3Months(), e.getR102_moreThan3MonthTo6Months(), 
			e.getR102_moreThan6MonthTo12Months(), e.getR102_moreThan12MonthTo3Years(), e.getR102_moreThan3YearsTo5Years(), e.getR102_moreThan5YearsTo10Years(), 
			e.getR102_moreThan10Years(), e.getR102_nonRatioSensativeItems(), e.getR102_total(), e.getR103product(), 
			e.getR103_upTo1Month(), e.getR103_moreThan1MonthTo3Months(), e.getR103_moreThan3MonthTo6Months(), e.getR103_moreThan6MonthTo12Months(), 
			e.getR103_moreThan12MonthTo3Years(), e.getR103_moreThan3YearsTo5Years(), e.getR103_moreThan5YearsTo10Years(), e.getR103_moreThan10Years(), 
			e.getR103_nonRatioSensativeItems(), e.getR103_total(), e.getR104product(), e.getR104_upTo1Month(), 
			e.getR104_moreThan1MonthTo3Months(), e.getR104_moreThan3MonthTo6Months(), e.getR104_moreThan6MonthTo12Months(), e.getR104_moreThan12MonthTo3Years(), 
			e.getR104_moreThan3YearsTo5Years(), e.getR104_moreThan5YearsTo10Years(), e.getR104_moreThan10Years(), e.getR104_nonRatioSensativeItems(), 
			e.getR104_total(), e.getR105product(), e.getR105_upTo1Month(), e.getR105_moreThan1MonthTo3Months(), 
			e.getR105_moreThan3MonthTo6Months(), e.getR105_moreThan6MonthTo12Months(), e.getR105_moreThan12MonthTo3Years(), e.getR105_moreThan3YearsTo5Years(), 
			e.getR105_moreThan5YearsTo10Years(), e.getR105_moreThan10Years(), e.getR105_nonRatioSensativeItems(), e.getR105_total(), 
			e.getR106product(), e.getR106_upTo1Month(), e.getR106_moreThan1MonthTo3Months(), e.getR106_moreThan3MonthTo6Months(), 
			e.getR106_moreThan6MonthTo12Months(), e.getR106_moreThan12MonthTo3Years(), e.getR106_moreThan3YearsTo5Years(), e.getR106_moreThan5YearsTo10Years(), 
			e.getR106_moreThan10Years(), e.getR106_nonRatioSensativeItems(), e.getR106_total(), e.getReportVersion(), 
			e.getReportFrequency(), e.getReportCode(), e.getReportDesc(), e.getEntityFlg(), 
			e.getModifyFlg(), e.getDelFlg(), e.getReportDate(), 
		};
		jdbcTemplate.update(UPDATE_M_IRB_SUMMARY_SQL, params);
	}
	// ===================== End JDBC Query/Update Methods =====================

	public ModelAndView getM_IRBView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version,HttpServletRequest req1,Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);
		

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_IRB_Archival_Summary_Entity> T1Master = new ArrayList<M_IRB_Archival_Summary_Entity>();
			System.out.println(version);
			try {

				T1Master = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {

			List<M_IRB_Summary_Entity> T1Master = new ArrayList<M_IRB_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = getSummaryByDate(dateformat.parse(todate));

				System.out.println("t1 master for IRB is :" + T1Master.size());
				mv.addObject("report_date", dateformat.format(d1));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/M_IRB");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_IRBcurrentDtl(String reportId, String fromdate, String todate, String currency,
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

		// Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;

			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLable = null;
			String reportAddlCriteria_1 = null;
			// ✅ Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLable = parts[0];
					reportAddlCriteria_1 = parts[1];
				}
			}

			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<M_IRB_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = getArchivalDetailByRowIdAndColumnId(reportLable, reportAddlCriteria_1,
							parsedDate, version);
				} else {
					T1Dt1 = getArchivalDetailByDateAndVersion(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_IRB_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = getDetailByRowIdAndColumnId(reportLable, reportAddlCriteria_1,
							parsedDate);
				} else {
					T1Dt1 = getDetailByDatePaged(parsedDate, currentPage, pageSize);
					totalPages = getDetailCount(parsedDate);
					mv.addObject("pagination", "YES");

				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);

				System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		// Page<Object> T1Dt1Page = new PageImpl<Object>(pagedlist,
		// PageRequest.of(currentPage, pageSize), T1Dt1.size());
		// mv.addObject("reportdetails", T1Dt1Page.getContent());
		// mv.addObject("reportmaster1", qr);
		// mv.addObject("singledetail", new T1CurProdDetail());

		// ✅ Common attributes
		mv.setViewName("BRRS/M_IRB");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	
	public List<Object[]> getM_IRBArchival() {
		List<Object[]> archivalList = new ArrayList<>();
		try {
			List<M_IRB_Archival_Summary_Entity> latestArchivalList = getArchivalSummaryWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_IRB_Archival_Summary_Entity entity : latestArchivalList) {
					archivalList.add(new Object[] {
							entity.getReportDate(),
							entity.getReportVersion(),
							entity.getReportResubDate()
					});
				}
				System.out.println("Fetched " + archivalList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_LIQ Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return archivalList;
	}

	public byte[] BRRS_M_IRBDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_IRB Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_IRBDetails");

//Common border style
			BorderStyle border = BorderStyle.THIN;
//Header style (left aligned)
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

//Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

//Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

//ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);
//Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
					"REPORT_DATE" };
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
//Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_IRB_Detail_Entity> reportData = getDetailByDate(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_IRB_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());
//ACCT BALANCE (right aligned, 3 decimal places)
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
//Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_PI — only header will be written.");
			}
//Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_PI Excel", e);
			return new byte[0];
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_PI ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MSFinP2Detail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
					"REPORT_DATE" };

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
			List<M_IRB_Archival_Detail_Entity> reportData = getArchivalDetailByDateAndVersion(parsedToDate, version);
			System.out.println("Size");
			System.out.println(reportData.size());
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_IRB_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for BRRS_M_PI — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_PIExcel", e);
			return new byte[0];
		}
	}

	public void MIRBUpdate(M_IRB_Summary_Entity updatedEntity) {
		System.out.println("Came to MIS UPDATE services");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		List<M_IRB_Summary_Entity> existingList = getSummaryByDate(updatedEntity.getReportDate());
		if (existingList == null || existingList.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate());
		}
		M_IRB_Summary_Entity existing = existingList.get(0);

		try {
			// 1️⃣ Loop from R10 to R16 and copy fields
			for (int i = 96; i <= 106; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "upTo1Month", "moreThan1MonthTo3Months", "moreThan3MonthTo6Months",
						"moreThan6MonthTo12Months", "moreThan12MonthTo3Years", "moreThan13YearsTo5Years",
						"moreThan5YearsTo10Years", "moreThan10Years", "nonRatioSensativeItems", "total" };

				for (String field : fields) {
					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;

					try {
						Method getter = M_IRB_Summary_Entity.class.getMethod(getterName);
						Method setter = M_IRB_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		updateSummary(existing);
	}

	// SUMMARY FORMAT EXCEL
	public byte[] BRRS_M_IRBExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type,String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		if ("ARCHIVAL".equals(type) && version != null) {
			byte[] ARCHIVALreport = getExcelM_IRBARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
			return ARCHIVALreport;
		}

		List<M_IRB_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_IRB report. Returning empty result.");
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

			if (!dataList.isEmpty()) {
				populateEntity1Data(sheet, todate, dataList.get(0), textStyle, numberStyle);
			}


			int startRow = 7;


			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_IRB SUMMARY", null, "BRRS_M_IRB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	//SUMMARY FORMAT VALUES
   private void populateEntity1Data(Sheet sheet, String todate, M_IRB_Summary_Entity record, CellStyle textStyle, CellStyle numberStyle) {

	   /* ==========================================================
	    * REPORT DATE
	    * Set todate in C6
	    * ========================================================== */

	   try {

	       // Row 5 = Excel row 6
	       Row dateRow = sheet.getRow(5);

	       if (dateRow == null) {
	           dateRow = sheet.createRow(5);
	       }

	       // Column 2 = Excel column C
	       Cell dateCell = dateRow.getCell(2);

	       if (dateCell == null) {
	           dateCell = dateRow.createCell(2);
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
	        Row row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
	        Cell R12cell1 = row.createCell(2);
	        if (record.getR12_upTo1Month() != null) {
	            R12cell1.setCellValue(record.getR12_upTo1Month().doubleValue());
	            R12cell1.setCellStyle(numberStyle);
	        } else {
	            R12cell1.setCellValue("");
	            R12cell1.setCellStyle(textStyle);
	        }

	        Cell R12cell2 = row.createCell(3);
	        if (record.getR12_moreThan1MonthTo3Months() != null) {
	            R12cell2.setCellValue(record.getR12_moreThan1MonthTo3Months().doubleValue());
	            R12cell2.setCellStyle(numberStyle);
	        } else {
	            R12cell2.setCellValue("");
	            R12cell2.setCellStyle(textStyle);
	        }

	        Cell R12cell3 = row.createCell(4);
	        if (record.getR12_moreThan3MonthTo6Months() != null) {
	            R12cell3.setCellValue(record.getR12_moreThan3MonthTo6Months().doubleValue());
	            R12cell3.setCellStyle(numberStyle);
	        } else {
	            R12cell3.setCellValue("");
	            R12cell3.setCellStyle(textStyle);
	        }

	        Cell R12cell4 = row.createCell(5);
	        if (record.getR12_moreThan6MonthTo12Months() != null) {
	            R12cell4.setCellValue(record.getR12_moreThan6MonthTo12Months().doubleValue());
	            R12cell4.setCellStyle(numberStyle);
	        } else {
	            R12cell4.setCellValue("");
	            R12cell4.setCellStyle(textStyle);
	        }

	        Cell R12cell5 = row.createCell(6);
	        if (record.getR12_moreThan12MonthTo3Years() != null) {
	            R12cell5.setCellValue(record.getR12_moreThan12MonthTo3Years().doubleValue());
	            R12cell5.setCellStyle(numberStyle);
	        } else {
	            R12cell5.setCellValue("");
	            R12cell5.setCellStyle(textStyle);
	        }

	        Cell R12cell6 = row.createCell(7);
	        if (record.getR12_moreThan3YearsTo5Years() != null) {
	            R12cell6.setCellValue(record.getR12_moreThan3YearsTo5Years().doubleValue());
	            R12cell6.setCellStyle(numberStyle);
	        } else {
	            R12cell6.setCellValue("");
	            R12cell6.setCellStyle(textStyle);
	        }

	        Cell R12cell7 = row.createCell(8);
	        if (record.getR12_moreThan5YearsTo10Years() != null) {
	            R12cell7.setCellValue(record.getR12_moreThan5YearsTo10Years().doubleValue());
	            R12cell7.setCellStyle(numberStyle);
	        } else {
	            R12cell7.setCellValue("");
	            R12cell7.setCellStyle(textStyle);
	        }

	        Cell R12cell8 = row.createCell(9);
	        if (record.getR12_moreThan10Years() != null) {
	            R12cell8.setCellValue(record.getR12_moreThan10Years().doubleValue());
	            R12cell8.setCellStyle(numberStyle);
	        } else {
	            R12cell8.setCellValue("");
	            R12cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(12);
	         Cell R13cell1 = row.createCell(2);
	        if (record.getR13_upTo1Month() != null) {
	            R13cell1.setCellValue(record.getR13_upTo1Month().doubleValue());
	            R13cell1.setCellStyle(numberStyle);
	        } else {
	            R13cell1.setCellValue("");
	            R13cell1.setCellStyle(textStyle);
	        }

	        Cell R13cell2 = row.createCell(3);
	        if (record.getR13_moreThan1MonthTo3Months() != null) {
	            R13cell2.setCellValue(record.getR13_moreThan1MonthTo3Months().doubleValue());
	            R13cell2.setCellStyle(numberStyle);
	        } else {
	            R13cell2.setCellValue("");
	            R13cell2.setCellStyle(textStyle);
	        }

	        Cell R13cell3 = row.createCell(4);
	        if (record.getR13_moreThan3MonthTo6Months() != null) {
	            R13cell3.setCellValue(record.getR13_moreThan3MonthTo6Months().doubleValue());
	            R13cell3.setCellStyle(numberStyle);
	        } else {
	            R13cell3.setCellValue("");
	            R13cell3.setCellStyle(textStyle);
	        }

	        Cell R13cell4 = row.createCell(5);
	        if (record.getR13_moreThan6MonthTo12Months() != null) {
	            R13cell4.setCellValue(record.getR13_moreThan6MonthTo12Months().doubleValue());
	            R13cell4.setCellStyle(numberStyle);
	        } else {
	            R13cell4.setCellValue("");
	            R13cell4.setCellStyle(textStyle);
	        }

	        Cell R13cell5 = row.createCell(6);
	        if (record.getR13_moreThan12MonthTo3Years() != null) {
	            R13cell5.setCellValue(record.getR13_moreThan12MonthTo3Years().doubleValue());
	            R13cell5.setCellStyle(numberStyle);
	        } else {
	            R13cell5.setCellValue("");
	            R13cell5.setCellStyle(textStyle);
	        }

	        Cell R13cell6 = row.createCell(7);
	        if (record.getR13_moreThan3YearsTo5Years() != null) {
	            R13cell6.setCellValue(record.getR13_moreThan3YearsTo5Years().doubleValue());
	            R13cell6.setCellStyle(numberStyle);
	        } else {
	            R13cell6.setCellValue("");
	            R13cell6.setCellStyle(textStyle);
	        }

	        Cell R13cell7 = row.createCell(8);
	        if (record.getR13_moreThan5YearsTo10Years() != null) {
	            R13cell7.setCellValue(record.getR13_moreThan5YearsTo10Years().doubleValue());
	            R13cell7.setCellStyle(numberStyle);
	        } else {
	            R13cell7.setCellValue("");
	            R13cell7.setCellStyle(textStyle);
	        }

	        Cell R13cell8 = row.createCell(9);
	        if (record.getR13_moreThan10Years() != null) {
	            R13cell8.setCellValue(record.getR13_moreThan10Years().doubleValue());
	            R13cell8.setCellStyle(numberStyle);
	        } else {
	            R13cell8.setCellValue("");
	            R13cell8.setCellStyle(textStyle);
	        }


	        row = sheet.getRow(13);
	        Cell R14cell1 = row.createCell(2);
	        if (record.getR14_upTo1Month() != null) {
	            R14cell1.setCellValue(record.getR14_upTo1Month().doubleValue());
	            R14cell1.setCellStyle(numberStyle);
	        } else {
	            R14cell1.setCellValue("");
	            R14cell1.setCellStyle(textStyle);
	        }

	        Cell R14cell2 = row.createCell(3);
	        if (record.getR14_moreThan1MonthTo3Months() != null) {
	            R14cell2.setCellValue(record.getR14_moreThan1MonthTo3Months().doubleValue());
	            R14cell2.setCellStyle(numberStyle);
	        } else {
	            R14cell2.setCellValue("");
	            R14cell2.setCellStyle(textStyle);
	        }

	        Cell R14cell3 = row.createCell(4);
	        if (record.getR14_moreThan3MonthTo6Months() != null) {
	            R14cell3.setCellValue(record.getR14_moreThan3MonthTo6Months().doubleValue());
	            R14cell3.setCellStyle(numberStyle);
	        } else {
	            R14cell3.setCellValue("");
	            R14cell3.setCellStyle(textStyle);
	        }

	        Cell R14cell4 = row.createCell(5);
	        if (record.getR14_moreThan6MonthTo12Months() != null) {
	            R14cell4.setCellValue(record.getR14_moreThan6MonthTo12Months().doubleValue());
	            R14cell4.setCellStyle(numberStyle);
	        } else {
	            R14cell4.setCellValue("");
	            R14cell4.setCellStyle(textStyle);
	        }

	        Cell R14cell5 = row.createCell(6);
	        if (record.getR14_moreThan12MonthTo3Years() != null) {
	            R14cell5.setCellValue(record.getR14_moreThan12MonthTo3Years().doubleValue());
	            R14cell5.setCellStyle(numberStyle);
	        } else {
	            R14cell5.setCellValue("");
	            R14cell5.setCellStyle(textStyle);
	        }

	        Cell R14cell6 = row.createCell(7);
	        if (record.getR14_moreThan3YearsTo5Years() != null) {
	            R14cell6.setCellValue(record.getR14_moreThan3YearsTo5Years().doubleValue());
	            R14cell6.setCellStyle(numberStyle);
	        } else {
	            R14cell6.setCellValue("");
	            R14cell6.setCellStyle(textStyle);
	        }

	        Cell R14cell7 = row.createCell(8);
	        if (record.getR14_moreThan5YearsTo10Years() != null) {
	            R14cell7.setCellValue(record.getR14_moreThan5YearsTo10Years().doubleValue());
	            R14cell7.setCellStyle(numberStyle);
	        } else {
	            R14cell7.setCellValue("");
	            R14cell7.setCellStyle(textStyle);
	        }

	        Cell R14cell8 = row.createCell(9);
	        if (record.getR14_moreThan10Years() != null) {
	            R14cell8.setCellValue(record.getR14_moreThan10Years().doubleValue());
	            R14cell8.setCellStyle(numberStyle);
	        } else {
	            R14cell8.setCellValue("");
	            R14cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(14);
	        Cell R15cell1 = row.createCell(2);
	        if (record.getR15_upTo1Month() != null) {
	            R15cell1.setCellValue(record.getR15_upTo1Month().doubleValue());
	            R15cell1.setCellStyle(numberStyle);
	        } else {
	            R15cell1.setCellValue("");
	            R15cell1.setCellStyle(textStyle);
	        }

	        Cell R15cell2 = row.createCell(3);
	        if (record.getR15_moreThan1MonthTo3Months() != null) {
	            R15cell2.setCellValue(record.getR15_moreThan1MonthTo3Months().doubleValue());
	            R15cell2.setCellStyle(numberStyle);
	        } else {
	            R15cell2.setCellValue("");
	            R15cell2.setCellStyle(textStyle);
	        }

	        Cell R15cell3 = row.createCell(4);
	        if (record.getR15_moreThan3MonthTo6Months() != null) {
	            R15cell3.setCellValue(record.getR15_moreThan3MonthTo6Months().doubleValue());
	            R15cell3.setCellStyle(numberStyle);
	        } else {
	            R15cell3.setCellValue("");
	            R15cell3.setCellStyle(textStyle);
	        }

	        Cell R15cell4 = row.createCell(5);
	        if (record.getR15_moreThan6MonthTo12Months() != null) {
	            R15cell4.setCellValue(record.getR15_moreThan6MonthTo12Months().doubleValue());
	            R15cell4.setCellStyle(numberStyle);
	        } else {
	            R15cell4.setCellValue("");
	            R15cell4.setCellStyle(textStyle);
	        }

	        Cell R15cell5 = row.createCell(6);
	        if (record.getR15_moreThan12MonthTo3Years() != null) {
	            R15cell5.setCellValue(record.getR15_moreThan12MonthTo3Years().doubleValue());
	            R15cell5.setCellStyle(numberStyle);
	        } else {
	            R15cell5.setCellValue("");
	            R15cell5.setCellStyle(textStyle);
	        }

	        Cell R15cell6 = row.createCell(7);
	        if (record.getR15_moreThan3YearsTo5Years() != null) {
	            R15cell6.setCellValue(record.getR15_moreThan3YearsTo5Years().doubleValue());
	            R15cell6.setCellStyle(numberStyle);
	        } else {
	            R15cell6.setCellValue("");
	            R15cell6.setCellStyle(textStyle);
	        }

	        Cell R15cell7 = row.createCell(8);
	        if (record.getR15_moreThan5YearsTo10Years() != null) {
	            R15cell7.setCellValue(record.getR15_moreThan5YearsTo10Years().doubleValue());
	            R15cell7.setCellStyle(numberStyle);
	        } else {
	            R15cell7.setCellValue("");
	            R15cell7.setCellStyle(textStyle);
	        }

	        Cell R15cell8 = row.createCell(9);
	        if (record.getR15_moreThan10Years() != null) {
	            R15cell8.setCellValue(record.getR15_moreThan10Years().doubleValue());
	            R15cell8.setCellStyle(numberStyle);
	        } else {
	            R15cell8.setCellValue("");
	            R15cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(15);
	        Cell R16cell1 = row.createCell(2);
	        if (record.getR16_upTo1Month() != null) {
	            R16cell1.setCellValue(record.getR16_upTo1Month().doubleValue());
	            R16cell1.setCellStyle(numberStyle);
	        } else {
	            R16cell1.setCellValue("");
	            R16cell1.setCellStyle(textStyle);
	        }

	        Cell R16cell2 = row.createCell(3);
	        if (record.getR16_moreThan1MonthTo3Months() != null) {
	            R16cell2.setCellValue(record.getR16_moreThan1MonthTo3Months().doubleValue());
	            R16cell2.setCellStyle(numberStyle);
	        } else {
	            R16cell2.setCellValue("");
	            R16cell2.setCellStyle(textStyle);
	        }

	        Cell R16cell3 = row.createCell(4);
	        if (record.getR16_moreThan3MonthTo6Months() != null) {
	            R16cell3.setCellValue(record.getR16_moreThan3MonthTo6Months().doubleValue());
	            R16cell3.setCellStyle(numberStyle);
	        } else {
	            R16cell3.setCellValue("");
	            R16cell3.setCellStyle(textStyle);
	        }

	        Cell R16cell4 = row.createCell(5);
	        if (record.getR16_moreThan6MonthTo12Months() != null) {
	            R16cell4.setCellValue(record.getR16_moreThan6MonthTo12Months().doubleValue());
	            R16cell4.setCellStyle(numberStyle);
	        } else {
	            R16cell4.setCellValue("");
	            R16cell4.setCellStyle(textStyle);
	        }

	        Cell R16cell5 = row.createCell(6);
	        if (record.getR16_moreThan12MonthTo3Years() != null) {
	            R16cell5.setCellValue(record.getR16_moreThan12MonthTo3Years().doubleValue());
	            R16cell5.setCellStyle(numberStyle);
	        } else {
	            R16cell5.setCellValue("");
	            R16cell5.setCellStyle(textStyle);
	        }

	        Cell R16cell6 = row.createCell(7);
	        if (record.getR16_moreThan3YearsTo5Years() != null) {
	            R16cell6.setCellValue(record.getR16_moreThan3YearsTo5Years().doubleValue());
	            R16cell6.setCellStyle(numberStyle);
	        } else {
	            R16cell6.setCellValue("");
	            R16cell6.setCellStyle(textStyle);
	        }

	        Cell R16cell7 = row.createCell(8);
	        if (record.getR16_moreThan5YearsTo10Years() != null) {
	            R16cell7.setCellValue(record.getR16_moreThan5YearsTo10Years().doubleValue());
	            R16cell7.setCellStyle(numberStyle);
	        } else {
	            R16cell7.setCellValue("");
	            R16cell7.setCellStyle(textStyle);
	        }

	        Cell R16cell8 = row.createCell(9);
	        if (record.getR16_moreThan10Years() != null) {
	            R16cell8.setCellValue(record.getR16_moreThan10Years().doubleValue());
	            R16cell8.setCellStyle(numberStyle);
	        } else {
	            R16cell8.setCellValue("");
	            R16cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(16);
	        Cell R17cell1 = row.createCell(2);
	        if (record.getR17_upTo1Month() != null) {
	            R17cell1.setCellValue(record.getR17_upTo1Month().doubleValue());
	            R17cell1.setCellStyle(numberStyle);
	        } else {
	            R17cell1.setCellValue("");
	            R17cell1.setCellStyle(textStyle);
	        }

	        Cell R17cell2 = row.createCell(3);
	        if (record.getR17_moreThan1MonthTo3Months() != null) {
	            R17cell2.setCellValue(record.getR17_moreThan1MonthTo3Months().doubleValue());
	            R17cell2.setCellStyle(numberStyle);
	        } else {
	            R17cell2.setCellValue("");
	            R17cell2.setCellStyle(textStyle);
	        }

	        Cell R17cell3 = row.createCell(4);
	        if (record.getR17_moreThan3MonthTo6Months() != null) {
	            R17cell3.setCellValue(record.getR17_moreThan3MonthTo6Months().doubleValue());
	            R17cell3.setCellStyle(numberStyle);
	        } else {
	            R17cell3.setCellValue("");
	            R17cell3.setCellStyle(textStyle);
	        }

	        Cell R17cell4 = row.createCell(5);
	        if (record.getR17_moreThan6MonthTo12Months() != null) {
	            R17cell4.setCellValue(record.getR17_moreThan6MonthTo12Months().doubleValue());
	            R17cell4.setCellStyle(numberStyle);
	        } else {
	            R17cell4.setCellValue("");
	            R17cell4.setCellStyle(textStyle);
	        }

	        Cell R17cell5 = row.createCell(6);
	        if (record.getR17_moreThan12MonthTo3Years() != null) {
	            R17cell5.setCellValue(record.getR17_moreThan12MonthTo3Years().doubleValue());
	            R17cell5.setCellStyle(numberStyle);
	        } else {
	            R17cell5.setCellValue("");
	            R17cell5.setCellStyle(textStyle);
	        }

	        Cell R17cell6 = row.createCell(7);
	        if (record.getR17_moreThan3YearsTo5Years() != null) {
	            R17cell6.setCellValue(record.getR17_moreThan3YearsTo5Years().doubleValue());
	            R17cell6.setCellStyle(numberStyle);
	        } else {
	            R17cell6.setCellValue("");
	            R17cell6.setCellStyle(textStyle);
	        }

	        Cell R17cell7 = row.createCell(8);
	        if (record.getR17_moreThan5YearsTo10Years() != null) {
	            R17cell7.setCellValue(record.getR17_moreThan5YearsTo10Years().doubleValue());
	            R17cell7.setCellStyle(numberStyle);
	        } else {
	            R17cell7.setCellValue("");
	            R17cell7.setCellStyle(textStyle);
	        }

	        Cell R17cell8 = row.createCell(9);
	        if (record.getR17_moreThan10Years() != null) {
	            R17cell8.setCellValue(record.getR17_moreThan10Years().doubleValue());
	            R17cell8.setCellStyle(numberStyle);
	        } else {
	            R17cell8.setCellValue("");
	            R17cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(17);
	        Cell R18cell1 = row.createCell(2);
	        if (record.getR18_upTo1Month() != null) {
	            R18cell1.setCellValue(record.getR18_upTo1Month().doubleValue());
	            R18cell1.setCellStyle(numberStyle);
	        } else {
	            R18cell1.setCellValue("");
	            R18cell1.setCellStyle(textStyle);
	        }

	        Cell R18cell2 = row.createCell(3);
	        if (record.getR18_moreThan1MonthTo3Months() != null) {
	            R18cell2.setCellValue(record.getR18_moreThan1MonthTo3Months().doubleValue());
	            R18cell2.setCellStyle(numberStyle);
	        } else {
	            R18cell2.setCellValue("");
	            R18cell2.setCellStyle(textStyle);
	        }

	        Cell R18cell3 = row.createCell(4);
	        if (record.getR18_moreThan3MonthTo6Months() != null) {
	            R18cell3.setCellValue(record.getR18_moreThan3MonthTo6Months().doubleValue());
	            R18cell3.setCellStyle(numberStyle);
	        } else {
	            R18cell3.setCellValue("");
	            R18cell3.setCellStyle(textStyle);
	        }

	        Cell R18cell4 = row.createCell(5);
	        if (record.getR18_moreThan6MonthTo12Months() != null) {
	            R18cell4.setCellValue(record.getR18_moreThan6MonthTo12Months().doubleValue());
	            R18cell4.setCellStyle(numberStyle);
	        } else {
	            R18cell4.setCellValue("");
	            R18cell4.setCellStyle(textStyle);
	        }

	        Cell R18cell5 = row.createCell(6);
	        if (record.getR18_moreThan12MonthTo3Years() != null) {
	            R18cell5.setCellValue(record.getR18_moreThan12MonthTo3Years().doubleValue());
	            R18cell5.setCellStyle(numberStyle);
	        } else {
	            R18cell5.setCellValue("");
	            R18cell5.setCellStyle(textStyle);
	        }

	        Cell R18cell6 = row.createCell(7);
	        if (record.getR18_moreThan3YearsTo5Years() != null) {
	            R18cell6.setCellValue(record.getR18_moreThan3YearsTo5Years().doubleValue());
	            R18cell6.setCellStyle(numberStyle);
	        } else {
	            R18cell6.setCellValue("");
	            R18cell6.setCellStyle(textStyle);
	        }

	        Cell R18cell7 = row.createCell(8);
	        if (record.getR18_moreThan5YearsTo10Years() != null) {
	            R18cell7.setCellValue(record.getR18_moreThan5YearsTo10Years().doubleValue());
	            R18cell7.setCellStyle(numberStyle);
	        } else {
	            R18cell7.setCellValue("");
	            R18cell7.setCellStyle(textStyle);
	        }

	        Cell R18cell8 = row.createCell(9);
	        if (record.getR18_moreThan10Years() != null) {
	            R18cell8.setCellValue(record.getR18_moreThan10Years().doubleValue());
	            R18cell8.setCellStyle(numberStyle);
	        } else {
	            R18cell8.setCellValue("");
	            R18cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(18);
	        Cell R19cell1 = row.createCell(2);
	        if (record.getR19_upTo1Month() != null) {
	            R19cell1.setCellValue(record.getR19_upTo1Month().doubleValue());
	            R19cell1.setCellStyle(numberStyle);
	        } else {
	            R19cell1.setCellValue("");
	            R19cell1.setCellStyle(textStyle);
	        }

	        Cell R19cell2 = row.createCell(3);
	        if (record.getR19_moreThan1MonthTo3Months() != null) {
	            R19cell2.setCellValue(record.getR19_moreThan1MonthTo3Months().doubleValue());
	            R19cell2.setCellStyle(numberStyle);
	        } else {
	            R19cell2.setCellValue("");
	            R19cell2.setCellStyle(textStyle);
	        }

	        Cell R19cell3 = row.createCell(4);
	        if (record.getR19_moreThan3MonthTo6Months() != null) {
	            R19cell3.setCellValue(record.getR19_moreThan3MonthTo6Months().doubleValue());
	            R19cell3.setCellStyle(numberStyle);
	        } else {
	            R19cell3.setCellValue("");
	            R19cell3.setCellStyle(textStyle);
	        }

	        Cell R19cell4 = row.createCell(5);
	        if (record.getR19_moreThan6MonthTo12Months() != null) {
	            R19cell4.setCellValue(record.getR19_moreThan6MonthTo12Months().doubleValue());
	            R19cell4.setCellStyle(numberStyle);
	        } else {
	            R19cell4.setCellValue("");
	            R19cell4.setCellStyle(textStyle);
	        }

	        Cell R19cell5 = row.createCell(6);
	        if (record.getR19_moreThan12MonthTo3Years() != null) {
	            R19cell5.setCellValue(record.getR19_moreThan12MonthTo3Years().doubleValue());
	            R19cell5.setCellStyle(numberStyle);
	        } else {
	            R19cell5.setCellValue("");
	            R19cell5.setCellStyle(textStyle);
	        }

	        Cell R19cell6 = row.createCell(7);
	        if (record.getR19_moreThan3YearsTo5Years() != null) {
	            R19cell6.setCellValue(record.getR19_moreThan3YearsTo5Years().doubleValue());
	            R19cell6.setCellStyle(numberStyle);
	        } else {
	            R19cell6.setCellValue("");
	            R19cell6.setCellStyle(textStyle);
	        }

	        Cell R19cell7 = row.createCell(8);
	        if (record.getR19_moreThan5YearsTo10Years() != null) {
	            R19cell7.setCellValue(record.getR19_moreThan5YearsTo10Years().doubleValue());
	            R19cell7.setCellStyle(numberStyle);
	        } else {
	            R19cell7.setCellValue("");
	            R19cell7.setCellStyle(textStyle);
	        }

	        Cell R19cell8 = row.createCell(9);
	        if (record.getR19_moreThan10Years() != null) {
	            R19cell8.setCellValue(record.getR19_moreThan10Years().doubleValue());
	            R19cell8.setCellStyle(numberStyle);
	        } else {
	            R19cell8.setCellValue("");
	            R19cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(19);
	        Cell R20cell1 = row.createCell(2);
	        if (record.getR20_upTo1Month() != null) {
	            R20cell1.setCellValue(record.getR20_upTo1Month().doubleValue());
	            R20cell1.setCellStyle(numberStyle);
	        } else {
	            R20cell1.setCellValue("");
	            R20cell1.setCellStyle(textStyle);
	        }

	        Cell R20cell2 = row.createCell(3);
	        if (record.getR20_moreThan1MonthTo3Months() != null) {
	            R20cell2.setCellValue(record.getR20_moreThan1MonthTo3Months().doubleValue());
	            R20cell2.setCellStyle(numberStyle);
	        } else {
	            R20cell2.setCellValue("");
	            R20cell2.setCellStyle(textStyle);
	        }

	        Cell R20cell3 = row.createCell(4);
	        if (record.getR20_moreThan3MonthTo6Months() != null) {
	            R20cell3.setCellValue(record.getR20_moreThan3MonthTo6Months().doubleValue());
	            R20cell3.setCellStyle(numberStyle);
	        } else {
	            R20cell3.setCellValue("");
	            R20cell3.setCellStyle(textStyle);
	        }

	        Cell R20cell4 = row.createCell(5);
	        if (record.getR20_moreThan6MonthTo12Months() != null) {
	            R20cell4.setCellValue(record.getR20_moreThan6MonthTo12Months().doubleValue());
	            R20cell4.setCellStyle(numberStyle);
	        } else {
	            R20cell4.setCellValue("");
	            R20cell4.setCellStyle(textStyle);
	        }

	        Cell R20cell5 = row.createCell(6);
	        if (record.getR20_moreThan12MonthTo3Years() != null) {
	            R20cell5.setCellValue(record.getR20_moreThan12MonthTo3Years().doubleValue());
	            R20cell5.setCellStyle(numberStyle);
	        } else {
	            R20cell5.setCellValue("");
	            R20cell5.setCellStyle(textStyle);
	        }

	        Cell R20cell6 = row.createCell(7);
	        if (record.getR20_moreThan3YearsTo5Years() != null) {
	            R20cell6.setCellValue(record.getR20_moreThan3YearsTo5Years().doubleValue());
	            R20cell6.setCellStyle(numberStyle);
	        } else {
	            R20cell6.setCellValue("");
	            R20cell6.setCellStyle(textStyle);
	        }

	        Cell R20cell7 = row.createCell(8);
	        if (record.getR20_moreThan5YearsTo10Years() != null) {
	            R20cell7.setCellValue(record.getR20_moreThan5YearsTo10Years().doubleValue());
	            R20cell7.setCellStyle(numberStyle);
	        } else {
	            R20cell7.setCellValue("");
	            R20cell7.setCellStyle(textStyle);
	        }

	        Cell R20cell8 = row.createCell(9);
	        if (record.getR20_moreThan10Years() != null) {
	            R20cell8.setCellValue(record.getR20_moreThan10Years().doubleValue());
	            R20cell8.setCellStyle(numberStyle);
	        } else {
	            R20cell8.setCellValue("");
	            R20cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(20);
	        Cell R21cell1 = row.createCell(2);
	        if (record.getR21_upTo1Month() != null) {
	            R21cell1.setCellValue(record.getR21_upTo1Month().doubleValue());
	            R21cell1.setCellStyle(numberStyle);
	        } else {
	            R21cell1.setCellValue("");
	            R21cell1.setCellStyle(textStyle);
	        }

	        Cell R21cell2 = row.createCell(3);
	        if (record.getR21_moreThan1MonthTo3Months() != null) {
	            R21cell2.setCellValue(record.getR21_moreThan1MonthTo3Months().doubleValue());
	            R21cell2.setCellStyle(numberStyle);
	        } else {
	            R21cell2.setCellValue("");
	            R21cell2.setCellStyle(textStyle);
	        }

	        Cell R21cell3 = row.createCell(4);
	        if (record.getR21_moreThan3MonthTo6Months() != null) {
	            R21cell3.setCellValue(record.getR21_moreThan3MonthTo6Months().doubleValue());
	            R21cell3.setCellStyle(numberStyle);
	        } else {
	            R21cell3.setCellValue("");
	            R21cell3.setCellStyle(textStyle);
	        }

	        Cell R21cell4 = row.createCell(5);
	        if (record.getR21_moreThan6MonthTo12Months() != null) {
	            R21cell4.setCellValue(record.getR21_moreThan6MonthTo12Months().doubleValue());
	            R21cell4.setCellStyle(numberStyle);
	        } else {
	            R21cell4.setCellValue("");
	            R21cell4.setCellStyle(textStyle);
	        }

	        Cell R21cell5 = row.createCell(6);
	        if (record.getR21_moreThan12MonthTo3Years() != null) {
	            R21cell5.setCellValue(record.getR21_moreThan12MonthTo3Years().doubleValue());
	            R21cell5.setCellStyle(numberStyle);
	        } else {
	            R21cell5.setCellValue("");
	            R21cell5.setCellStyle(textStyle);
	        }

	        Cell R21cell6 = row.createCell(7);
	        if (record.getR21_moreThan3YearsTo5Years() != null) {
	            R21cell6.setCellValue(record.getR21_moreThan3YearsTo5Years().doubleValue());
	            R21cell6.setCellStyle(numberStyle);
	        } else {
	            R21cell6.setCellValue("");
	            R21cell6.setCellStyle(textStyle);
	        }

	        Cell R21cell7 = row.createCell(8);
	        if (record.getR21_moreThan5YearsTo10Years() != null) {
	            R21cell7.setCellValue(record.getR21_moreThan5YearsTo10Years().doubleValue());
	            R21cell7.setCellStyle(numberStyle);
	        } else {
	            R21cell7.setCellValue("");
	            R21cell7.setCellStyle(textStyle);
	        }

	        Cell R21cell8 = row.createCell(9);
	        if (record.getR21_moreThan10Years() != null) {
	            R21cell8.setCellValue(record.getR21_moreThan10Years().doubleValue());
	            R21cell8.setCellStyle(numberStyle);
	        } else {
	            R21cell8.setCellValue("");
	            R21cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(21);
	        Cell R22cell1 = row.createCell(2);
	        if (record.getR22_upTo1Month() != null) {
	            R22cell1.setCellValue(record.getR22_upTo1Month().doubleValue());
	            R22cell1.setCellStyle(numberStyle);
	        } else {
	            R22cell1.setCellValue("");
	            R22cell1.setCellStyle(textStyle);
	        }

	        Cell R22cell2 = row.createCell(3);
	        if (record.getR22_moreThan1MonthTo3Months() != null) {
	            R22cell2.setCellValue(record.getR22_moreThan1MonthTo3Months().doubleValue());
	            R22cell2.setCellStyle(numberStyle);
	        } else {
	            R22cell2.setCellValue("");
	            R22cell2.setCellStyle(textStyle);
	        }

	        Cell R22cell3 = row.createCell(4);
	        if (record.getR22_moreThan3MonthTo6Months() != null) {
	            R22cell3.setCellValue(record.getR22_moreThan3MonthTo6Months().doubleValue());
	            R22cell3.setCellStyle(numberStyle);
	        } else {
	            R22cell3.setCellValue("");
	            R22cell3.setCellStyle(textStyle);
	        }

	        Cell R22cell4 = row.createCell(5);
	        if (record.getR22_moreThan6MonthTo12Months() != null) {
	            R22cell4.setCellValue(record.getR22_moreThan6MonthTo12Months().doubleValue());
	            R22cell4.setCellStyle(numberStyle);
	        } else {
	            R22cell4.setCellValue("");
	            R22cell4.setCellStyle(textStyle);
	        }

	        Cell R22cell5 = row.createCell(6);
	        if (record.getR22_moreThan12MonthTo3Years() != null) {
	            R22cell5.setCellValue(record.getR22_moreThan12MonthTo3Years().doubleValue());
	            R22cell5.setCellStyle(numberStyle);
	        } else {
	            R22cell5.setCellValue("");
	            R22cell5.setCellStyle(textStyle);
	        }

	        Cell R22cell6 = row.createCell(7);
	        if (record.getR22_moreThan3YearsTo5Years() != null) {
	            R22cell6.setCellValue(record.getR22_moreThan3YearsTo5Years().doubleValue());
	            R22cell6.setCellStyle(numberStyle);
	        } else {
	            R22cell6.setCellValue("");
	            R22cell6.setCellStyle(textStyle);
	        }

	        Cell R22cell7 = row.createCell(8);
	        if (record.getR22_moreThan5YearsTo10Years() != null) {
	            R22cell7.setCellValue(record.getR22_moreThan5YearsTo10Years().doubleValue());
	            R22cell7.setCellStyle(numberStyle);
	        } else {
	            R22cell7.setCellValue("");
	            R22cell7.setCellStyle(textStyle);
	        }

	        Cell R22cell8 = row.createCell(9);
	        if (record.getR22_moreThan10Years() != null) {
	            R22cell8.setCellValue(record.getR22_moreThan10Years().doubleValue());
	            R22cell8.setCellStyle(numberStyle);
	        } else {
	            R22cell8.setCellValue("");
	            R22cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(23);
	        Cell R24cell1 = row.createCell(2);
	        if (record.getR24_upTo1Month() != null) {
	            R24cell1.setCellValue(record.getR24_upTo1Month().doubleValue());
	            R24cell1.setCellStyle(numberStyle);
	        } else {
	            R24cell1.setCellValue("");
	            R24cell1.setCellStyle(textStyle);
	        }

	        Cell R24cell2 = row.createCell(3);
	        if (record.getR24_moreThan1MonthTo3Months() != null) {
	            R24cell2.setCellValue(record.getR24_moreThan1MonthTo3Months().doubleValue());
	            R24cell2.setCellStyle(numberStyle);
	        } else {
	            R24cell2.setCellValue("");
	            R24cell2.setCellStyle(textStyle);
	        }

	        Cell R24cell3 = row.createCell(4);
	        if (record.getR24_moreThan3MonthTo6Months() != null) {
	            R24cell3.setCellValue(record.getR24_moreThan3MonthTo6Months().doubleValue());
	            R24cell3.setCellStyle(numberStyle);
	        } else {
	            R24cell3.setCellValue("");
	            R24cell3.setCellStyle(textStyle);
	        }

	        Cell R24cell4 = row.createCell(5);
	        if (record.getR24_moreThan6MonthTo12Months() != null) {
	            R24cell4.setCellValue(record.getR24_moreThan6MonthTo12Months().doubleValue());
	            R24cell4.setCellStyle(numberStyle);
	        } else {
	            R24cell4.setCellValue("");
	            R24cell4.setCellStyle(textStyle);
	        }

	        Cell R24cell5 = row.createCell(6);
	        if (record.getR24_moreThan12MonthTo3Years() != null) {
	            R24cell5.setCellValue(record.getR24_moreThan12MonthTo3Years().doubleValue());
	            R24cell5.setCellStyle(numberStyle);
	        } else {
	            R24cell5.setCellValue("");
	            R24cell5.setCellStyle(textStyle);
	        }

	        Cell R24cell6 = row.createCell(7);
	        if (record.getR24_moreThan3YearsTo5Years() != null) {
	            R24cell6.setCellValue(record.getR24_moreThan3YearsTo5Years().doubleValue());
	            R24cell6.setCellStyle(numberStyle);
	        } else {
	            R24cell6.setCellValue("");
	            R24cell6.setCellStyle(textStyle);
	        }

	        Cell R24cell7 = row.createCell(8);
	        if (record.getR24_moreThan5YearsTo10Years() != null) {
	            R24cell7.setCellValue(record.getR24_moreThan5YearsTo10Years().doubleValue());
	            R24cell7.setCellStyle(numberStyle);
	        } else {
	            R24cell7.setCellValue("");
	            R24cell7.setCellStyle(textStyle);
	        }

	        Cell R24cell8 = row.createCell(9);
	        if (record.getR24_moreThan10Years() != null) {
	            R24cell8.setCellValue(record.getR24_moreThan10Years().doubleValue());
	            R24cell8.setCellStyle(numberStyle);
	        } else {
	            R24cell8.setCellValue("");
	            R24cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(24);
	        Cell R25cell1 = row.createCell(2);
	        if (record.getR25_upTo1Month() != null) {
	            R25cell1.setCellValue(record.getR25_upTo1Month().doubleValue());
	            R25cell1.setCellStyle(numberStyle);
	        } else {
	            R25cell1.setCellValue("");
	            R25cell1.setCellStyle(textStyle);
	        }

	        Cell R25cell2 = row.createCell(3);
	        if (record.getR25_moreThan1MonthTo3Months() != null) {
	            R25cell2.setCellValue(record.getR25_moreThan1MonthTo3Months().doubleValue());
	            R25cell2.setCellStyle(numberStyle);
	        } else {
	            R25cell2.setCellValue("");
	            R25cell2.setCellStyle(textStyle);
	        }

	        Cell R25cell3 = row.createCell(4);
	        if (record.getR25_moreThan3MonthTo6Months() != null) {
	            R25cell3.setCellValue(record.getR25_moreThan3MonthTo6Months().doubleValue());
	            R25cell3.setCellStyle(numberStyle);
	        } else {
	            R25cell3.setCellValue("");
	            R25cell3.setCellStyle(textStyle);
	        }

	        Cell R25cell4 = row.createCell(5);
	        if (record.getR25_moreThan6MonthTo12Months() != null) {
	            R25cell4.setCellValue(record.getR25_moreThan6MonthTo12Months().doubleValue());
	            R25cell4.setCellStyle(numberStyle);
	        } else {
	            R25cell4.setCellValue("");
	            R25cell4.setCellStyle(textStyle);
	        }

	        Cell R25cell5 = row.createCell(6);
	        if (record.getR25_moreThan12MonthTo3Years() != null) {
	            R25cell5.setCellValue(record.getR25_moreThan12MonthTo3Years().doubleValue());
	            R25cell5.setCellStyle(numberStyle);
	        } else {
	            R25cell5.setCellValue("");
	            R25cell5.setCellStyle(textStyle);
	        }

	        Cell R25cell6 = row.createCell(7);
	        if (record.getR25_moreThan3YearsTo5Years() != null) {
	            R25cell6.setCellValue(record.getR25_moreThan3YearsTo5Years().doubleValue());
	            R25cell6.setCellStyle(numberStyle);
	        } else {
	            R25cell6.setCellValue("");
	            R25cell6.setCellStyle(textStyle);
	        }

	        Cell R25cell7 = row.createCell(8);
	        if (record.getR25_moreThan5YearsTo10Years() != null) {
	            R25cell7.setCellValue(record.getR25_moreThan5YearsTo10Years().doubleValue());
	            R25cell7.setCellStyle(numberStyle);
	        } else {
	            R25cell7.setCellValue("");
	            R25cell7.setCellStyle(textStyle);
	        }

	        Cell R25cell8 = row.createCell(9);
	        if (record.getR25_moreThan10Years() != null) {
	            R25cell8.setCellValue(record.getR25_moreThan10Years().doubleValue());
	            R25cell8.setCellStyle(numberStyle);
	        } else {
	            R25cell8.setCellValue("");
	            R25cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(25);
	        Cell R26cell1 = row.createCell(2);
	        if (record.getR26_upTo1Month() != null) {
	            R26cell1.setCellValue(record.getR26_upTo1Month().doubleValue());
	            R26cell1.setCellStyle(numberStyle);
	        } else {
	            R26cell1.setCellValue("");
	            R26cell1.setCellStyle(textStyle);
	        }

	        Cell R26cell2 = row.createCell(3);
	        if (record.getR26_moreThan1MonthTo3Months() != null) {
	            R26cell2.setCellValue(record.getR26_moreThan1MonthTo3Months().doubleValue());
	            R26cell2.setCellStyle(numberStyle);
	        } else {
	            R26cell2.setCellValue("");
	            R26cell2.setCellStyle(textStyle);
	        }

	        Cell R26cell3 = row.createCell(4);
	        if (record.getR26_moreThan3MonthTo6Months() != null) {
	            R26cell3.setCellValue(record.getR26_moreThan3MonthTo6Months().doubleValue());
	            R26cell3.setCellStyle(numberStyle);
	        } else {
	            R26cell3.setCellValue("");
	            R26cell3.setCellStyle(textStyle);
	        }

	        Cell R26cell4 = row.createCell(5);
	        if (record.getR26_moreThan6MonthTo12Months() != null) {
	            R26cell4.setCellValue(record.getR26_moreThan6MonthTo12Months().doubleValue());
	            R26cell4.setCellStyle(numberStyle);
	        } else {
	            R26cell4.setCellValue("");
	            R26cell4.setCellStyle(textStyle);
	        }

	        Cell R26cell5 = row.createCell(6);
	        if (record.getR26_moreThan12MonthTo3Years() != null) {
	            R26cell5.setCellValue(record.getR26_moreThan12MonthTo3Years().doubleValue());
	            R26cell5.setCellStyle(numberStyle);
	        } else {
	            R26cell5.setCellValue("");
	            R26cell5.setCellStyle(textStyle);
	        }

	        Cell R26cell6 = row.createCell(7);
	        if (record.getR26_moreThan3YearsTo5Years() != null) {
	            R26cell6.setCellValue(record.getR26_moreThan3YearsTo5Years().doubleValue());
	            R26cell6.setCellStyle(numberStyle);
	        } else {
	            R26cell6.setCellValue("");
	            R26cell6.setCellStyle(textStyle);
	        }

	        Cell R26cell7 = row.createCell(8);
	        if (record.getR26_moreThan5YearsTo10Years() != null) {
	            R26cell7.setCellValue(record.getR26_moreThan5YearsTo10Years().doubleValue());
	            R26cell7.setCellStyle(numberStyle);
	        } else {
	            R26cell7.setCellValue("");
	            R26cell7.setCellStyle(textStyle);
	        }

	        Cell R26cell8 = row.createCell(9);
	        if (record.getR26_moreThan10Years() != null) {
	            R26cell8.setCellValue(record.getR26_moreThan10Years().doubleValue());
	            R26cell8.setCellStyle(numberStyle);
	        } else {
	            R26cell8.setCellValue("");
	            R26cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(26);
	        Cell R27cell1 = row.createCell(2);
	        if (record.getR27_upTo1Month() != null) {
	            R27cell1.setCellValue(record.getR27_upTo1Month().doubleValue());
	            R27cell1.setCellStyle(numberStyle);
	        } else {
	            R27cell1.setCellValue("");
	            R27cell1.setCellStyle(textStyle);
	        }

	        Cell R27cell2 = row.createCell(3);
	        if (record.getR27_moreThan1MonthTo3Months() != null) {
	            R27cell2.setCellValue(record.getR27_moreThan1MonthTo3Months().doubleValue());
	            R27cell2.setCellStyle(numberStyle);
	        } else {
	            R27cell2.setCellValue("");
	            R27cell2.setCellStyle(textStyle);
	        }

	        Cell R27cell3 = row.createCell(4);
	        if (record.getR27_moreThan3MonthTo6Months() != null) {
	            R27cell3.setCellValue(record.getR27_moreThan3MonthTo6Months().doubleValue());
	            R27cell3.setCellStyle(numberStyle);
	        } else {
	            R27cell3.setCellValue("");
	            R27cell3.setCellStyle(textStyle);
	        }

	        Cell R27cell4 = row.createCell(5);
	        if (record.getR27_moreThan6MonthTo12Months() != null) {
	            R27cell4.setCellValue(record.getR27_moreThan6MonthTo12Months().doubleValue());
	            R27cell4.setCellStyle(numberStyle);
	        } else {
	            R27cell4.setCellValue("");
	            R27cell4.setCellStyle(textStyle);
	        }

	        Cell R27cell5 = row.createCell(6);
	        if (record.getR27_moreThan12MonthTo3Years() != null) {
	            R27cell5.setCellValue(record.getR27_moreThan12MonthTo3Years().doubleValue());
	            R27cell5.setCellStyle(numberStyle);
	        } else {
	            R27cell5.setCellValue("");
	            R27cell5.setCellStyle(textStyle);
	        }

	        Cell R27cell6 = row.createCell(7);
	        if (record.getR27_moreThan3YearsTo5Years() != null) {
	            R27cell6.setCellValue(record.getR27_moreThan3YearsTo5Years().doubleValue());
	            R27cell6.setCellStyle(numberStyle);
	        } else {
	            R27cell6.setCellValue("");
	            R27cell6.setCellStyle(textStyle);
	        }

	        Cell R27cell7 = row.createCell(8);
	        if (record.getR27_moreThan5YearsTo10Years() != null) {
	            R27cell7.setCellValue(record.getR27_moreThan5YearsTo10Years().doubleValue());
	            R27cell7.setCellStyle(numberStyle);
	        } else {
	            R27cell7.setCellValue("");
	            R27cell7.setCellStyle(textStyle);
	        }

	        Cell R27cell8 = row.createCell(9);
	        if (record.getR27_moreThan10Years() != null) {
	            R27cell8.setCellValue(record.getR27_moreThan10Years().doubleValue());
	            R27cell8.setCellStyle(numberStyle);
	        } else {
	            R27cell8.setCellValue("");
	            R27cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(27);
	        Cell R28cell1 = row.createCell(2);
	        if (record.getR28_upTo1Month() != null) {
	            R28cell1.setCellValue(record.getR28_upTo1Month().doubleValue());
	            R28cell1.setCellStyle(numberStyle);
	        } else {
	            R28cell1.setCellValue("");
	            R28cell1.setCellStyle(textStyle);
	        }

	        Cell R28cell2 = row.createCell(3);
	        if (record.getR28_moreThan1MonthTo3Months() != null) {
	            R28cell2.setCellValue(record.getR28_moreThan1MonthTo3Months().doubleValue());
	            R28cell2.setCellStyle(numberStyle);
	        } else {
	            R28cell2.setCellValue("");
	            R28cell2.setCellStyle(textStyle);
	        }

	        Cell R28cell3 = row.createCell(4);
	        if (record.getR28_moreThan3MonthTo6Months() != null) {
	            R28cell3.setCellValue(record.getR28_moreThan3MonthTo6Months().doubleValue());
	            R28cell3.setCellStyle(numberStyle);
	        } else {
	            R28cell3.setCellValue("");
	            R28cell3.setCellStyle(textStyle);
	        }

	        Cell R28cell4 = row.createCell(5);
	        if (record.getR28_moreThan6MonthTo12Months() != null) {
	            R28cell4.setCellValue(record.getR28_moreThan6MonthTo12Months().doubleValue());
	            R28cell4.setCellStyle(numberStyle);
	        } else {
	            R28cell4.setCellValue("");
	            R28cell4.setCellStyle(textStyle);
	        }

	        Cell R28cell5 = row.createCell(6);
	        if (record.getR28_moreThan12MonthTo3Years() != null) {
	            R28cell5.setCellValue(record.getR28_moreThan12MonthTo3Years().doubleValue());
	            R28cell5.setCellStyle(numberStyle);
	        } else {
	            R28cell5.setCellValue("");
	            R28cell5.setCellStyle(textStyle);
	        }

	        Cell R28cell6 = row.createCell(7);
	        if (record.getR28_moreThan3YearsTo5Years() != null) {
	            R28cell6.setCellValue(record.getR28_moreThan3YearsTo5Years().doubleValue());
	            R28cell6.setCellStyle(numberStyle);
	        } else {
	            R28cell6.setCellValue("");
	            R28cell6.setCellStyle(textStyle);
	        }

	        Cell R28cell7 = row.createCell(8);
	        if (record.getR28_moreThan5YearsTo10Years() != null) {
	            R28cell7.setCellValue(record.getR28_moreThan5YearsTo10Years().doubleValue());
	            R28cell7.setCellStyle(numberStyle);
	        } else {
	            R28cell7.setCellValue("");
	            R28cell7.setCellStyle(textStyle);
	        }

	        Cell R28cell8 = row.createCell(9);
	        if (record.getR28_moreThan10Years() != null) {
	            R28cell8.setCellValue(record.getR28_moreThan10Years().doubleValue());
	            R28cell8.setCellStyle(numberStyle);
	        } else {
	            R28cell8.setCellValue("");
	            R28cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(28);
	        Cell R29cell1 = row.createCell(2);
	        if (record.getR29_upTo1Month() != null) {
	            R29cell1.setCellValue(record.getR29_upTo1Month().doubleValue());
	            R29cell1.setCellStyle(numberStyle);
	        } else {
	            R29cell1.setCellValue("");
	            R29cell1.setCellStyle(textStyle);
	        }

	        Cell R29cell2 = row.createCell(3);
	        if (record.getR29_moreThan1MonthTo3Months() != null) {
	            R29cell2.setCellValue(record.getR29_moreThan1MonthTo3Months().doubleValue());
	            R29cell2.setCellStyle(numberStyle);
	        } else {
	            R29cell2.setCellValue("");
	            R29cell2.setCellStyle(textStyle);
	        }

	        Cell R29cell3 = row.createCell(4);
	        if (record.getR29_moreThan3MonthTo6Months() != null) {
	            R29cell3.setCellValue(record.getR29_moreThan3MonthTo6Months().doubleValue());
	            R29cell3.setCellStyle(numberStyle);
	        } else {
	            R29cell3.setCellValue("");
	            R29cell3.setCellStyle(textStyle);
	        }

	        Cell R29cell4 = row.createCell(5);
	        if (record.getR29_moreThan6MonthTo12Months() != null) {
	            R29cell4.setCellValue(record.getR29_moreThan6MonthTo12Months().doubleValue());
	            R29cell4.setCellStyle(numberStyle);
	        } else {
	            R29cell4.setCellValue("");
	            R29cell4.setCellStyle(textStyle);
	        }

	        Cell R29cell5 = row.createCell(6);
	        if (record.getR29_moreThan12MonthTo3Years() != null) {
	            R29cell5.setCellValue(record.getR29_moreThan12MonthTo3Years().doubleValue());
	            R29cell5.setCellStyle(numberStyle);
	        } else {
	            R29cell5.setCellValue("");
	            R29cell5.setCellStyle(textStyle);
	        }

	        Cell R29cell6 = row.createCell(7);
	        if (record.getR29_moreThan3YearsTo5Years() != null) {
	            R29cell6.setCellValue(record.getR29_moreThan3YearsTo5Years().doubleValue());
	            R29cell6.setCellStyle(numberStyle);
	        } else {
	            R29cell6.setCellValue("");
	            R29cell6.setCellStyle(textStyle);
	        }

	        Cell R29cell7 = row.createCell(8);
	        if (record.getR29_moreThan5YearsTo10Years() != null) {
	            R29cell7.setCellValue(record.getR29_moreThan5YearsTo10Years().doubleValue());
	            R29cell7.setCellStyle(numberStyle);
	        } else {
	            R29cell7.setCellValue("");
	            R29cell7.setCellStyle(textStyle);
	        }

	        Cell R29cell8 = row.createCell(9);
	        if (record.getR29_moreThan10Years() != null) {
	            R29cell8.setCellValue(record.getR29_moreThan10Years().doubleValue());
	            R29cell8.setCellStyle(numberStyle);
	        } else {
	            R29cell8.setCellValue("");
	            R29cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(29);
	        Cell R30cell1 = row.createCell(2);
	        if (record.getR30_upTo1Month() != null) {
	            R30cell1.setCellValue(record.getR30_upTo1Month().doubleValue());
	            R30cell1.setCellStyle(numberStyle);
	        } else {
	            R30cell1.setCellValue("");
	            R30cell1.setCellStyle(textStyle);
	        }

	        Cell R30cell2 = row.createCell(3);
	        if (record.getR30_moreThan1MonthTo3Months() != null) {
	            R30cell2.setCellValue(record.getR30_moreThan1MonthTo3Months().doubleValue());
	            R30cell2.setCellStyle(numberStyle);
	        } else {
	            R30cell2.setCellValue("");
	            R30cell2.setCellStyle(textStyle);
	        }

	        Cell R30cell3 = row.createCell(4);
	        if (record.getR30_moreThan3MonthTo6Months() != null) {
	            R30cell3.setCellValue(record.getR30_moreThan3MonthTo6Months().doubleValue());
	            R30cell3.setCellStyle(numberStyle);
	        } else {
	            R30cell3.setCellValue("");
	            R30cell3.setCellStyle(textStyle);
	        }

	        Cell R30cell4 = row.createCell(5);
	        if (record.getR30_moreThan6MonthTo12Months() != null) {
	            R30cell4.setCellValue(record.getR30_moreThan6MonthTo12Months().doubleValue());
	            R30cell4.setCellStyle(numberStyle);
	        } else {
	            R30cell4.setCellValue("");
	            R30cell4.setCellStyle(textStyle);
	        }

	        Cell R30cell5 = row.createCell(6);
	        if (record.getR30_moreThan12MonthTo3Years() != null) {
	            R30cell5.setCellValue(record.getR30_moreThan12MonthTo3Years().doubleValue());
	            R30cell5.setCellStyle(numberStyle);
	        } else {
	            R30cell5.setCellValue("");
	            R30cell5.setCellStyle(textStyle);
	        }

	        Cell R30cell6 = row.createCell(7);
	        if (record.getR30_moreThan3YearsTo5Years() != null) {
	            R30cell6.setCellValue(record.getR30_moreThan3YearsTo5Years().doubleValue());
	            R30cell6.setCellStyle(numberStyle);
	        } else {
	            R30cell6.setCellValue("");
	            R30cell6.setCellStyle(textStyle);
	        }

	        Cell R30cell7 = row.createCell(8);
	        if (record.getR30_moreThan5YearsTo10Years() != null) {
	            R30cell7.setCellValue(record.getR30_moreThan5YearsTo10Years().doubleValue());
	            R30cell7.setCellStyle(numberStyle);
	        } else {
	            R30cell7.setCellValue("");
	            R30cell7.setCellStyle(textStyle);
	        }

	        Cell R30cell8 = row.createCell(9);
	        if (record.getR30_moreThan10Years() != null) {
	            R30cell8.setCellValue(record.getR30_moreThan10Years().doubleValue());
	            R30cell8.setCellStyle(numberStyle);
	        } else {
	            R30cell8.setCellValue("");
	            R30cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(30);
	        Cell R31cell1 = row.createCell(2);
	        if (record.getR31_upTo1Month() != null) {
	            R31cell1.setCellValue(record.getR31_upTo1Month().doubleValue());
	            R31cell1.setCellStyle(numberStyle);
	        } else {
	            R31cell1.setCellValue("");
	            R31cell1.setCellStyle(textStyle);
	        }

	        Cell R31cell2 = row.createCell(3);
	        if (record.getR31_moreThan1MonthTo3Months() != null) {
	            R31cell2.setCellValue(record.getR31_moreThan1MonthTo3Months().doubleValue());
	            R31cell2.setCellStyle(numberStyle);
	        } else {
	            R31cell2.setCellValue("");
	            R31cell2.setCellStyle(textStyle);
	        }

	        Cell R31cell3 = row.createCell(4);
	        if (record.getR31_moreThan3MonthTo6Months() != null) {
	            R31cell3.setCellValue(record.getR31_moreThan3MonthTo6Months().doubleValue());
	            R31cell3.setCellStyle(numberStyle);
	        } else {
	            R31cell3.setCellValue("");
	            R31cell3.setCellStyle(textStyle);
	        }

	        Cell R31cell4 = row.createCell(5);
	        if (record.getR31_moreThan6MonthTo12Months() != null) {
	            R31cell4.setCellValue(record.getR31_moreThan6MonthTo12Months().doubleValue());
	            R31cell4.setCellStyle(numberStyle);
	        } else {
	            R31cell4.setCellValue("");
	            R31cell4.setCellStyle(textStyle);
	        }

	        Cell R31cell5 = row.createCell(6);
	        if (record.getR31_moreThan12MonthTo3Years() != null) {
	            R31cell5.setCellValue(record.getR31_moreThan12MonthTo3Years().doubleValue());
	            R31cell5.setCellStyle(numberStyle);
	        } else {
	            R31cell5.setCellValue("");
	            R31cell5.setCellStyle(textStyle);
	        }

	        Cell R31cell6 = row.createCell(7);
	        if (record.getR31_moreThan3YearsTo5Years() != null) {
	            R31cell6.setCellValue(record.getR31_moreThan3YearsTo5Years().doubleValue());
	            R31cell6.setCellStyle(numberStyle);
	        } else {
	            R31cell6.setCellValue("");
	            R31cell6.setCellStyle(textStyle);
	        }

	        Cell R31cell7 = row.createCell(8);
	        if (record.getR31_moreThan5YearsTo10Years() != null) {
	            R31cell7.setCellValue(record.getR31_moreThan5YearsTo10Years().doubleValue());
	            R31cell7.setCellStyle(numberStyle);
	        } else {
	            R31cell7.setCellValue("");
	            R31cell7.setCellStyle(textStyle);
	        }

	        Cell R31cell8 = row.createCell(9);
	        if (record.getR31_moreThan10Years() != null) {
	            R31cell8.setCellValue(record.getR31_moreThan10Years().doubleValue());
	            R31cell8.setCellStyle(numberStyle);
	        } else {
	            R31cell8.setCellValue("");
	            R31cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(31);
	        Cell R32cell1 = row.createCell(2);
	        if (record.getR32_upTo1Month() != null) {
	            R32cell1.setCellValue(record.getR32_upTo1Month().doubleValue());
	            R32cell1.setCellStyle(numberStyle);
	        } else {
	            R32cell1.setCellValue("");
	            R32cell1.setCellStyle(textStyle);
	        }

	        Cell R32cell2 = row.createCell(3);
	        if (record.getR32_moreThan1MonthTo3Months() != null) {
	            R32cell2.setCellValue(record.getR32_moreThan1MonthTo3Months().doubleValue());
	            R32cell2.setCellStyle(numberStyle);
	        } else {
	            R32cell2.setCellValue("");
	            R32cell2.setCellStyle(textStyle);
	        }

	        Cell R32cell3 = row.createCell(4);
	        if (record.getR32_moreThan3MonthTo6Months() != null) {
	            R32cell3.setCellValue(record.getR32_moreThan3MonthTo6Months().doubleValue());
	            R32cell3.setCellStyle(numberStyle);
	        } else {
	            R32cell3.setCellValue("");
	            R32cell3.setCellStyle(textStyle);
	        }

	        Cell R32cell4 = row.createCell(5);
	        if (record.getR32_moreThan6MonthTo12Months() != null) {
	            R32cell4.setCellValue(record.getR32_moreThan6MonthTo12Months().doubleValue());
	            R32cell4.setCellStyle(numberStyle);
	        } else {
	            R32cell4.setCellValue("");
	            R32cell4.setCellStyle(textStyle);
	        }

	        Cell R32cell5 = row.createCell(6);
	        if (record.getR32_moreThan12MonthTo3Years() != null) {
	            R32cell5.setCellValue(record.getR32_moreThan12MonthTo3Years().doubleValue());
	            R32cell5.setCellStyle(numberStyle);
	        } else {
	            R32cell5.setCellValue("");
	            R32cell5.setCellStyle(textStyle);
	        }

	        Cell R32cell6 = row.createCell(7);
	        if (record.getR32_moreThan3YearsTo5Years() != null) {
	            R32cell6.setCellValue(record.getR32_moreThan3YearsTo5Years().doubleValue());
	            R32cell6.setCellStyle(numberStyle);
	        } else {
	            R32cell6.setCellValue("");
	            R32cell6.setCellStyle(textStyle);
	        }

	        Cell R32cell7 = row.createCell(8);
	        if (record.getR32_moreThan5YearsTo10Years() != null) {
	            R32cell7.setCellValue(record.getR32_moreThan5YearsTo10Years().doubleValue());
	            R32cell7.setCellStyle(numberStyle);
	        } else {
	            R32cell7.setCellValue("");
	            R32cell7.setCellStyle(textStyle);
	        }

	        Cell R32cell8 = row.createCell(9);
	        if (record.getR32_moreThan10Years() != null) {
	            R32cell8.setCellValue(record.getR32_moreThan10Years().doubleValue());
	            R32cell8.setCellStyle(numberStyle);
	        } else {
	            R32cell8.setCellValue("");
	            R32cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(32);
	        Cell R33cell1 = row.createCell(2);
	        if (record.getR33_upTo1Month() != null) {
	            R33cell1.setCellValue(record.getR33_upTo1Month().doubleValue());
	            R33cell1.setCellStyle(numberStyle);
	        } else {
	            R33cell1.setCellValue("");
	            R33cell1.setCellStyle(textStyle);
	        }

	        Cell R33cell2 = row.createCell(3);
	        if (record.getR33_moreThan1MonthTo3Months() != null) {
	            R33cell2.setCellValue(record.getR33_moreThan1MonthTo3Months().doubleValue());
	            R33cell2.setCellStyle(numberStyle);
	        } else {
	            R33cell2.setCellValue("");
	            R33cell2.setCellStyle(textStyle);
	        }

	        Cell R33cell3 = row.createCell(4);
	        if (record.getR33_moreThan3MonthTo6Months() != null) {
	            R33cell3.setCellValue(record.getR33_moreThan3MonthTo6Months().doubleValue());
	            R33cell3.setCellStyle(numberStyle);
	        } else {
	            R33cell3.setCellValue("");
	            R33cell3.setCellStyle(textStyle);
	        }

	        Cell R33cell4 = row.createCell(5);
	        if (record.getR33_moreThan6MonthTo12Months() != null) {
	            R33cell4.setCellValue(record.getR33_moreThan6MonthTo12Months().doubleValue());
	            R33cell4.setCellStyle(numberStyle);
	        } else {
	            R33cell4.setCellValue("");
	            R33cell4.setCellStyle(textStyle);
	        }

	        Cell R33cell5 = row.createCell(6);
	        if (record.getR33_moreThan12MonthTo3Years() != null) {
	            R33cell5.setCellValue(record.getR33_moreThan12MonthTo3Years().doubleValue());
	            R33cell5.setCellStyle(numberStyle);
	        } else {
	            R33cell5.setCellValue("");
	            R33cell5.setCellStyle(textStyle);
	        }

	        Cell R33cell6 = row.createCell(7);
	        if (record.getR33_moreThan3YearsTo5Years() != null) {
	            R33cell6.setCellValue(record.getR33_moreThan3YearsTo5Years().doubleValue());
	            R33cell6.setCellStyle(numberStyle);
	        } else {
	            R33cell6.setCellValue("");
	            R33cell6.setCellStyle(textStyle);
	        }

	        Cell R33cell7 = row.createCell(8);
	        if (record.getR33_moreThan5YearsTo10Years() != null) {
	            R33cell7.setCellValue(record.getR33_moreThan5YearsTo10Years().doubleValue());
	            R33cell7.setCellStyle(numberStyle);
	        } else {
	            R33cell7.setCellValue("");
	            R33cell7.setCellStyle(textStyle);
	        }

	        Cell R33cell8 = row.createCell(9);
	        if (record.getR33_moreThan10Years() != null) {
	            R33cell8.setCellValue(record.getR33_moreThan10Years().doubleValue());
	            R33cell8.setCellStyle(numberStyle);
	        } else {
	            R33cell8.setCellValue("");
	            R33cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(33);
	        Cell R34cell1 = row.createCell(2);
	        if (record.getR34_upTo1Month() != null) {
	            R34cell1.setCellValue(record.getR34_upTo1Month().doubleValue());
	            R34cell1.setCellStyle(numberStyle);
	        } else {
	            R34cell1.setCellValue("");
	            R34cell1.setCellStyle(textStyle);
	        }

	        Cell R34cell2 = row.createCell(3);
	        if (record.getR34_moreThan1MonthTo3Months() != null) {
	            R34cell2.setCellValue(record.getR34_moreThan1MonthTo3Months().doubleValue());
	            R34cell2.setCellStyle(numberStyle);
	        } else {
	            R34cell2.setCellValue("");
	            R34cell2.setCellStyle(textStyle);
	        }

	        Cell R34cell3 = row.createCell(4);
	        if (record.getR34_moreThan3MonthTo6Months() != null) {
	            R34cell3.setCellValue(record.getR34_moreThan3MonthTo6Months().doubleValue());
	            R34cell3.setCellStyle(numberStyle);
	        } else {
	            R34cell3.setCellValue("");
	            R34cell3.setCellStyle(textStyle);
	        }

	        Cell R34cell4 = row.createCell(5);
	        if (record.getR34_moreThan6MonthTo12Months() != null) {
	            R34cell4.setCellValue(record.getR34_moreThan6MonthTo12Months().doubleValue());
	            R34cell4.setCellStyle(numberStyle);
	        } else {
	            R34cell4.setCellValue("");
	            R34cell4.setCellStyle(textStyle);
	        }

	        Cell R34cell5 = row.createCell(6);
	        if (record.getR34_moreThan12MonthTo3Years() != null) {
	            R34cell5.setCellValue(record.getR34_moreThan12MonthTo3Years().doubleValue());
	            R34cell5.setCellStyle(numberStyle);
	        } else {
	            R34cell5.setCellValue("");
	            R34cell5.setCellStyle(textStyle);
	        }

	        Cell R34cell6 = row.createCell(7);
	        if (record.getR34_moreThan3YearsTo5Years() != null) {
	            R34cell6.setCellValue(record.getR34_moreThan3YearsTo5Years().doubleValue());
	            R34cell6.setCellStyle(numberStyle);
	        } else {
	            R34cell6.setCellValue("");
	            R34cell6.setCellStyle(textStyle);
	        }

	        Cell R34cell7 = row.createCell(8);
	        if (record.getR34_moreThan5YearsTo10Years() != null) {
	            R34cell7.setCellValue(record.getR34_moreThan5YearsTo10Years().doubleValue());
	            R34cell7.setCellStyle(numberStyle);
	        } else {
	            R34cell7.setCellValue("");
	            R34cell7.setCellStyle(textStyle);
	        }

	        Cell R34cell8 = row.createCell(9);
	        if (record.getR34_moreThan10Years() != null) {
	            R34cell8.setCellValue(record.getR34_moreThan10Years().doubleValue());
	            R34cell8.setCellStyle(numberStyle);
	        } else {
	            R34cell8.setCellValue("");
	            R34cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(35);
	        Cell R36cell1 = row.createCell(2);
	        if (record.getR36_upTo1Month() != null) {
	            R36cell1.setCellValue(record.getR36_upTo1Month().doubleValue());
	            R36cell1.setCellStyle(numberStyle);
	        } else {
	            R36cell1.setCellValue("");
	            R36cell1.setCellStyle(textStyle);
	        }

	        Cell R36cell2 = row.createCell(3);
	        if (record.getR36_moreThan1MonthTo3Months() != null) {
	            R36cell2.setCellValue(record.getR36_moreThan1MonthTo3Months().doubleValue());
	            R36cell2.setCellStyle(numberStyle);
	        } else {
	            R36cell2.setCellValue("");
	            R36cell2.setCellStyle(textStyle);
	        }

	        Cell R36cell3 = row.createCell(4);
	        if (record.getR36_moreThan3MonthTo6Months() != null) {
	            R36cell3.setCellValue(record.getR36_moreThan3MonthTo6Months().doubleValue());
	            R36cell3.setCellStyle(numberStyle);
	        } else {
	            R36cell3.setCellValue("");
	            R36cell3.setCellStyle(textStyle);
	        }

	        Cell R36cell4 = row.createCell(5);
	        if (record.getR36_moreThan6MonthTo12Months() != null) {
	            R36cell4.setCellValue(record.getR36_moreThan6MonthTo12Months().doubleValue());
	            R36cell4.setCellStyle(numberStyle);
	        } else {
	            R36cell4.setCellValue("");
	            R36cell4.setCellStyle(textStyle);
	        }

	        Cell R36cell5 = row.createCell(6);
	        if (record.getR36_moreThan12MonthTo3Years() != null) {
	            R36cell5.setCellValue(record.getR36_moreThan12MonthTo3Years().doubleValue());
	            R36cell5.setCellStyle(numberStyle);
	        } else {
	            R36cell5.setCellValue("");
	            R36cell5.setCellStyle(textStyle);
	        }

	        Cell R36cell6 = row.createCell(7);
	        if (record.getR36_moreThan3YearsTo5Years() != null) {
	            R36cell6.setCellValue(record.getR36_moreThan3YearsTo5Years().doubleValue());
	            R36cell6.setCellStyle(numberStyle);
	        } else {
	            R36cell6.setCellValue("");
	            R36cell6.setCellStyle(textStyle);
	        }

	        Cell R36cell7 = row.createCell(8);
	        if (record.getR36_moreThan5YearsTo10Years() != null) {
	            R36cell7.setCellValue(record.getR36_moreThan5YearsTo10Years().doubleValue());
	            R36cell7.setCellStyle(numberStyle);
	        } else {
	            R36cell7.setCellValue("");
	            R36cell7.setCellStyle(textStyle);
	        }

	        Cell R36cell8 = row.createCell(9);
	        if (record.getR36_moreThan10Years() != null) {
	            R36cell8.setCellValue(record.getR36_moreThan10Years().doubleValue());
	            R36cell8.setCellStyle(numberStyle);
	        } else {
	            R36cell8.setCellValue("");
	            R36cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(36);
	        Cell R37cell1 = row.createCell(2);
	        if (record.getR37_upTo1Month() != null) {
	            R37cell1.setCellValue(record.getR37_upTo1Month().doubleValue());
	            R37cell1.setCellStyle(numberStyle);
	        } else {
	            R37cell1.setCellValue("");
	            R37cell1.setCellStyle(textStyle);
	        }

	        Cell R37cell2 = row.createCell(3);
	        if (record.getR37_moreThan1MonthTo3Months() != null) {
	            R37cell2.setCellValue(record.getR37_moreThan1MonthTo3Months().doubleValue());
	            R37cell2.setCellStyle(numberStyle);
	        } else {
	            R37cell2.setCellValue("");
	            R37cell2.setCellStyle(textStyle);
	        }

	        Cell R37cell3 = row.createCell(4);
	        if (record.getR37_moreThan3MonthTo6Months() != null) {
	            R37cell3.setCellValue(record.getR37_moreThan3MonthTo6Months().doubleValue());
	            R37cell3.setCellStyle(numberStyle);
	        } else {
	            R37cell3.setCellValue("");
	            R37cell3.setCellStyle(textStyle);
	        }

	        Cell R37cell4 = row.createCell(5);
	        if (record.getR37_moreThan6MonthTo12Months() != null) {
	            R37cell4.setCellValue(record.getR37_moreThan6MonthTo12Months().doubleValue());
	            R37cell4.setCellStyle(numberStyle);
	        } else {
	            R37cell4.setCellValue("");
	            R37cell4.setCellStyle(textStyle);
	        }

	        Cell R37cell5 = row.createCell(6);
	        if (record.getR37_moreThan12MonthTo3Years() != null) {
	            R37cell5.setCellValue(record.getR37_moreThan12MonthTo3Years().doubleValue());
	            R37cell5.setCellStyle(numberStyle);
	        } else {
	            R37cell5.setCellValue("");
	            R37cell5.setCellStyle(textStyle);
	        }

	        Cell R37cell6 = row.createCell(7);
	        if (record.getR37_moreThan3YearsTo5Years() != null) {
	            R37cell6.setCellValue(record.getR37_moreThan3YearsTo5Years().doubleValue());
	            R37cell6.setCellStyle(numberStyle);
	        } else {
	            R37cell6.setCellValue("");
	            R37cell6.setCellStyle(textStyle);
	        }

	        Cell R37cell7 = row.createCell(8);
	        if (record.getR37_moreThan5YearsTo10Years() != null) {
	            R37cell7.setCellValue(record.getR37_moreThan5YearsTo10Years().doubleValue());
	            R37cell7.setCellStyle(numberStyle);
	        } else {
	            R37cell7.setCellValue("");
	            R37cell7.setCellStyle(textStyle);
	        }

	        Cell R37cell8 = row.createCell(9);
	        if (record.getR37_moreThan10Years() != null) {
	            R37cell8.setCellValue(record.getR37_moreThan10Years().doubleValue());
	            R37cell8.setCellStyle(numberStyle);
	        } else {
	            R37cell8.setCellValue("");
	            R37cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(37);
	        Cell R38cell1 = row.createCell(2);
	        if (record.getR38_upTo1Month() != null) {
	            R38cell1.setCellValue(record.getR38_upTo1Month().doubleValue());
	            R38cell1.setCellStyle(numberStyle);
	        } else {
	            R38cell1.setCellValue("");
	            R38cell1.setCellStyle(textStyle);
	        }

	        Cell R38cell2 = row.createCell(3);
	        if (record.getR38_moreThan1MonthTo3Months() != null) {
	            R38cell2.setCellValue(record.getR38_moreThan1MonthTo3Months().doubleValue());
	            R38cell2.setCellStyle(numberStyle);
	        } else {
	            R38cell2.setCellValue("");
	            R38cell2.setCellStyle(textStyle);
	        }

	        Cell R38cell3 = row.createCell(4);
	        if (record.getR38_moreThan3MonthTo6Months() != null) {
	            R38cell3.setCellValue(record.getR38_moreThan3MonthTo6Months().doubleValue());
	            R38cell3.setCellStyle(numberStyle);
	        } else {
	            R38cell3.setCellValue("");
	            R38cell3.setCellStyle(textStyle);
	        }

	        Cell R38cell4 = row.createCell(5);
	        if (record.getR38_moreThan6MonthTo12Months() != null) {
	            R38cell4.setCellValue(record.getR38_moreThan6MonthTo12Months().doubleValue());
	            R38cell4.setCellStyle(numberStyle);
	        } else {
	            R38cell4.setCellValue("");
	            R38cell4.setCellStyle(textStyle);
	        }

	        Cell R38cell5 = row.createCell(6);
	        if (record.getR38_moreThan12MonthTo3Years() != null) {
	            R38cell5.setCellValue(record.getR38_moreThan12MonthTo3Years().doubleValue());
	            R38cell5.setCellStyle(numberStyle);
	        } else {
	            R38cell5.setCellValue("");
	            R38cell5.setCellStyle(textStyle);
	        }

	        Cell R38cell6 = row.createCell(7);
	        if (record.getR38_moreThan3YearsTo5Years() != null) {
	            R38cell6.setCellValue(record.getR38_moreThan3YearsTo5Years().doubleValue());
	            R38cell6.setCellStyle(numberStyle);
	        } else {
	            R38cell6.setCellValue("");
	            R38cell6.setCellStyle(textStyle);
	        }

	        Cell R38cell7 = row.createCell(8);
	        if (record.getR38_moreThan5YearsTo10Years() != null) {
	            R38cell7.setCellValue(record.getR38_moreThan5YearsTo10Years().doubleValue());
	            R38cell7.setCellStyle(numberStyle);
	        } else {
	            R38cell7.setCellValue("");
	            R38cell7.setCellStyle(textStyle);
	        }

	        Cell R38cell8 = row.createCell(9);
	        if (record.getR38_moreThan10Years() != null) {
	            R38cell8.setCellValue(record.getR38_moreThan10Years().doubleValue());
	            R38cell8.setCellStyle(numberStyle);
	        } else {
	            R38cell8.setCellValue("");
	            R38cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(38);
	        Cell R39cell1 = row.createCell(2);
	        if (record.getR39_upTo1Month() != null) {
	            R39cell1.setCellValue(record.getR39_upTo1Month().doubleValue());
	            R39cell1.setCellStyle(numberStyle);
	        } else {
	            R39cell1.setCellValue("");
	            R39cell1.setCellStyle(textStyle);
	        }

	        Cell R39cell2 = row.createCell(3);
	        if (record.getR39_moreThan1MonthTo3Months() != null) {
	            R39cell2.setCellValue(record.getR39_moreThan1MonthTo3Months().doubleValue());
	            R39cell2.setCellStyle(numberStyle);
	        } else {
	            R39cell2.setCellValue("");
	            R39cell2.setCellStyle(textStyle);
	        }

	        Cell R39cell3 = row.createCell(4);
	        if (record.getR39_moreThan3MonthTo6Months() != null) {
	            R39cell3.setCellValue(record.getR39_moreThan3MonthTo6Months().doubleValue());
	            R39cell3.setCellStyle(numberStyle);
	        } else {
	            R39cell3.setCellValue("");
	            R39cell3.setCellStyle(textStyle);
	        }

	        Cell R39cell4 = row.createCell(5);
	        if (record.getR39_moreThan6MonthTo12Months() != null) {
	            R39cell4.setCellValue(record.getR39_moreThan6MonthTo12Months().doubleValue());
	            R39cell4.setCellStyle(numberStyle);
	        } else {
	            R39cell4.setCellValue("");
	            R39cell4.setCellStyle(textStyle);
	        }

	        Cell R39cell5 = row.createCell(6);
	        if (record.getR39_moreThan12MonthTo3Years() != null) {
	            R39cell5.setCellValue(record.getR39_moreThan12MonthTo3Years().doubleValue());
	            R39cell5.setCellStyle(numberStyle);
	        } else {
	            R39cell5.setCellValue("");
	            R39cell5.setCellStyle(textStyle);
	        }

	        Cell R39cell6 = row.createCell(7);
	        if (record.getR39_moreThan3YearsTo5Years() != null) {
	            R39cell6.setCellValue(record.getR39_moreThan3YearsTo5Years().doubleValue());
	            R39cell6.setCellStyle(numberStyle);
	        } else {
	            R39cell6.setCellValue("");
	            R39cell6.setCellStyle(textStyle);
	        }

	        Cell R39cell7 = row.createCell(8);
	        if (record.getR39_moreThan5YearsTo10Years() != null) {
	            R39cell7.setCellValue(record.getR39_moreThan5YearsTo10Years().doubleValue());
	            R39cell7.setCellStyle(numberStyle);
	        } else {
	            R39cell7.setCellValue("");
	            R39cell7.setCellStyle(textStyle);
	        }

	        Cell R39cell8 = row.createCell(9);
	        if (record.getR39_moreThan10Years() != null) {
	            R39cell8.setCellValue(record.getR39_moreThan10Years().doubleValue());
	            R39cell8.setCellStyle(numberStyle);
	        } else {
	            R39cell8.setCellValue("");
	            R39cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(39);
	        Cell R40cell1 = row.createCell(2);
	        if (record.getR40_upTo1Month() != null) {
	            R40cell1.setCellValue(record.getR40_upTo1Month().doubleValue());
	            R40cell1.setCellStyle(numberStyle);
	        } else {
	            R40cell1.setCellValue("");
	            R40cell1.setCellStyle(textStyle);
	        }

	        Cell R40cell2 = row.createCell(3);
	        if (record.getR40_moreThan1MonthTo3Months() != null) {
	            R40cell2.setCellValue(record.getR40_moreThan1MonthTo3Months().doubleValue());
	            R40cell2.setCellStyle(numberStyle);
	        } else {
	            R40cell2.setCellValue("");
	            R40cell2.setCellStyle(textStyle);
	        }

	        Cell R40cell3 = row.createCell(4);
	        if (record.getR40_moreThan3MonthTo6Months() != null) {
	            R40cell3.setCellValue(record.getR40_moreThan3MonthTo6Months().doubleValue());
	            R40cell3.setCellStyle(numberStyle);
	        } else {
	            R40cell3.setCellValue("");
	            R40cell3.setCellStyle(textStyle);
	        }

	        Cell R40cell4 = row.createCell(5);
	        if (record.getR40_moreThan6MonthTo12Months() != null) {
	            R40cell4.setCellValue(record.getR40_moreThan6MonthTo12Months().doubleValue());
	            R40cell4.setCellStyle(numberStyle);
	        } else {
	            R40cell4.setCellValue("");
	            R40cell4.setCellStyle(textStyle);
	        }

	        Cell R40cell5 = row.createCell(6);
	        if (record.getR40_moreThan12MonthTo3Years() != null) {
	            R40cell5.setCellValue(record.getR40_moreThan12MonthTo3Years().doubleValue());
	            R40cell5.setCellStyle(numberStyle);
	        } else {
	            R40cell5.setCellValue("");
	            R40cell5.setCellStyle(textStyle);
	        }

	        Cell R40cell6 = row.createCell(7);
	        if (record.getR40_moreThan3YearsTo5Years() != null) {
	            R40cell6.setCellValue(record.getR40_moreThan3YearsTo5Years().doubleValue());
	            R40cell6.setCellStyle(numberStyle);
	        } else {
	            R40cell6.setCellValue("");
	            R40cell6.setCellStyle(textStyle);
	        }

	        Cell R40cell7 = row.createCell(8);
	        if (record.getR40_moreThan5YearsTo10Years() != null) {
	            R40cell7.setCellValue(record.getR40_moreThan5YearsTo10Years().doubleValue());
	            R40cell7.setCellStyle(numberStyle);
	        } else {
	            R40cell7.setCellValue("");
	            R40cell7.setCellStyle(textStyle);
	        }

	        Cell R40cell8 = row.createCell(9);
	        if (record.getR40_moreThan10Years() != null) {
	            R40cell8.setCellValue(record.getR40_moreThan10Years().doubleValue());
	            R40cell8.setCellStyle(numberStyle);
	        } else {
	            R40cell8.setCellValue("");
	            R40cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(40);
	        Cell R41cell1 = row.createCell(2);
	        if (record.getR41_upTo1Month() != null) {
	            R41cell1.setCellValue(record.getR41_upTo1Month().doubleValue());
	            R41cell1.setCellStyle(numberStyle);
	        } else {
	            R41cell1.setCellValue("");
	            R41cell1.setCellStyle(textStyle);
	        }

	        Cell R41cell2 = row.createCell(3);
	        if (record.getR41_moreThan1MonthTo3Months() != null) {
	            R41cell2.setCellValue(record.getR41_moreThan1MonthTo3Months().doubleValue());
	            R41cell2.setCellStyle(numberStyle);
	        } else {
	            R41cell2.setCellValue("");
	            R41cell2.setCellStyle(textStyle);
	        }

	        Cell R41cell3 = row.createCell(4);
	        if (record.getR41_moreThan3MonthTo6Months() != null) {
	            R41cell3.setCellValue(record.getR41_moreThan3MonthTo6Months().doubleValue());
	            R41cell3.setCellStyle(numberStyle);
	        } else {
	            R41cell3.setCellValue("");
	            R41cell3.setCellStyle(textStyle);
	        }

	        Cell R41cell4 = row.createCell(5);
	        if (record.getR41_moreThan6MonthTo12Months() != null) {
	            R41cell4.setCellValue(record.getR41_moreThan6MonthTo12Months().doubleValue());
	            R41cell4.setCellStyle(numberStyle);
	        } else {
	            R41cell4.setCellValue("");
	            R41cell4.setCellStyle(textStyle);
	        }

	        Cell R41cell5 = row.createCell(6);
	        if (record.getR41_moreThan12MonthTo3Years() != null) {
	            R41cell5.setCellValue(record.getR41_moreThan12MonthTo3Years().doubleValue());
	            R41cell5.setCellStyle(numberStyle);
	        } else {
	            R41cell5.setCellValue("");
	            R41cell5.setCellStyle(textStyle);
	        }

	        Cell R41cell6 = row.createCell(7);
	        if (record.getR41_moreThan3YearsTo5Years() != null) {
	            R41cell6.setCellValue(record.getR41_moreThan3YearsTo5Years().doubleValue());
	            R41cell6.setCellStyle(numberStyle);
	        } else {
	            R41cell6.setCellValue("");
	            R41cell6.setCellStyle(textStyle);
	        }

	        Cell R41cell7 = row.createCell(8);
	        if (record.getR41_moreThan5YearsTo10Years() != null) {
	            R41cell7.setCellValue(record.getR41_moreThan5YearsTo10Years().doubleValue());
	            R41cell7.setCellStyle(numberStyle);
	        } else {
	            R41cell7.setCellValue("");
	            R41cell7.setCellStyle(textStyle);
	        }

	        Cell R41cell8 = row.createCell(9);
	        if (record.getR41_moreThan10Years() != null) {
	            R41cell8.setCellValue(record.getR41_moreThan10Years().doubleValue());
	            R41cell8.setCellStyle(numberStyle);
	        } else {
	            R41cell8.setCellValue("");
	            R41cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(41);
	        Cell R42cell1 = row.createCell(2);
	        if (record.getR42_upTo1Month() != null) {
	            R42cell1.setCellValue(record.getR42_upTo1Month().doubleValue());
	            R42cell1.setCellStyle(numberStyle);
	        } else {
	            R42cell1.setCellValue("");
	            R42cell1.setCellStyle(textStyle);
	        }

	        Cell R42cell2 = row.createCell(3);
	        if (record.getR42_moreThan1MonthTo3Months() != null) {
	            R42cell2.setCellValue(record.getR42_moreThan1MonthTo3Months().doubleValue());
	            R42cell2.setCellStyle(numberStyle);
	        } else {
	            R42cell2.setCellValue("");
	            R42cell2.setCellStyle(textStyle);
	        }

	        Cell R42cell3 = row.createCell(4);
	        if (record.getR42_moreThan3MonthTo6Months() != null) {
	            R42cell3.setCellValue(record.getR42_moreThan3MonthTo6Months().doubleValue());
	            R42cell3.setCellStyle(numberStyle);
	        } else {
	            R42cell3.setCellValue("");
	            R42cell3.setCellStyle(textStyle);
	        }

	        Cell R42cell4 = row.createCell(5);
	        if (record.getR42_moreThan6MonthTo12Months() != null) {
	            R42cell4.setCellValue(record.getR42_moreThan6MonthTo12Months().doubleValue());
	            R42cell4.setCellStyle(numberStyle);
	        } else {
	            R42cell4.setCellValue("");
	            R42cell4.setCellStyle(textStyle);
	        }

	        Cell R42cell5 = row.createCell(6);
	        if (record.getR42_moreThan12MonthTo3Years() != null) {
	            R42cell5.setCellValue(record.getR42_moreThan12MonthTo3Years().doubleValue());
	            R42cell5.setCellStyle(numberStyle);
	        } else {
	            R42cell5.setCellValue("");
	            R42cell5.setCellStyle(textStyle);
	        }

	        Cell R42cell6 = row.createCell(7);
	        if (record.getR42_moreThan3YearsTo5Years() != null) {
	            R42cell6.setCellValue(record.getR42_moreThan3YearsTo5Years().doubleValue());
	            R42cell6.setCellStyle(numberStyle);
	        } else {
	            R42cell6.setCellValue("");
	            R42cell6.setCellStyle(textStyle);
	        }

	        Cell R42cell7 = row.createCell(8);
	        if (record.getR42_moreThan5YearsTo10Years() != null) {
	            R42cell7.setCellValue(record.getR42_moreThan5YearsTo10Years().doubleValue());
	            R42cell7.setCellStyle(numberStyle);
	        } else {
	            R42cell7.setCellValue("");
	            R42cell7.setCellStyle(textStyle);
	        }

	        Cell R42cell8 = row.createCell(9);
	        if (record.getR42_moreThan10Years() != null) {
	            R42cell8.setCellValue(record.getR42_moreThan10Years().doubleValue());
	            R42cell8.setCellStyle(numberStyle);
	        } else {
	            R42cell8.setCellValue("");
	            R42cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(42);
	        Cell R43cell1 = row.createCell(2);
	        if (record.getR43_upTo1Month() != null) {
	            R43cell1.setCellValue(record.getR43_upTo1Month().doubleValue());
	            R43cell1.setCellStyle(numberStyle);
	        } else {
	            R43cell1.setCellValue("");
	            R43cell1.setCellStyle(textStyle);
	        }

	        Cell R43cell2 = row.createCell(3);
	        if (record.getR43_moreThan1MonthTo3Months() != null) {
	            R43cell2.setCellValue(record.getR43_moreThan1MonthTo3Months().doubleValue());
	            R43cell2.setCellStyle(numberStyle);
	        } else {
	            R43cell2.setCellValue("");
	            R43cell2.setCellStyle(textStyle);
	        }

	        Cell R43cell3 = row.createCell(4);
	        if (record.getR43_moreThan3MonthTo6Months() != null) {
	            R43cell3.setCellValue(record.getR43_moreThan3MonthTo6Months().doubleValue());
	            R43cell3.setCellStyle(numberStyle);
	        } else {
	            R43cell3.setCellValue("");
	            R43cell3.setCellStyle(textStyle);
	        }

	        Cell R43cell4 = row.createCell(5);
	        if (record.getR43_moreThan6MonthTo12Months() != null) {
	            R43cell4.setCellValue(record.getR43_moreThan6MonthTo12Months().doubleValue());
	            R43cell4.setCellStyle(numberStyle);
	        } else {
	            R43cell4.setCellValue("");
	            R43cell4.setCellStyle(textStyle);
	        }

	        Cell R43cell5 = row.createCell(6);
	        if (record.getR43_moreThan12MonthTo3Years() != null) {
	            R43cell5.setCellValue(record.getR43_moreThan12MonthTo3Years().doubleValue());
	            R43cell5.setCellStyle(numberStyle);
	        } else {
	            R43cell5.setCellValue("");
	            R43cell5.setCellStyle(textStyle);
	        }

	        Cell R43cell6 = row.createCell(7);
	        if (record.getR43_moreThan3YearsTo5Years() != null) {
	            R43cell6.setCellValue(record.getR43_moreThan3YearsTo5Years().doubleValue());
	            R43cell6.setCellStyle(numberStyle);
	        } else {
	            R43cell6.setCellValue("");
	            R43cell6.setCellStyle(textStyle);
	        }

	        Cell R43cell7 = row.createCell(8);
	        if (record.getR43_moreThan5YearsTo10Years() != null) {
	            R43cell7.setCellValue(record.getR43_moreThan5YearsTo10Years().doubleValue());
	            R43cell7.setCellStyle(numberStyle);
	        } else {
	            R43cell7.setCellValue("");
	            R43cell7.setCellStyle(textStyle);
	        }

	        Cell R43cell8 = row.createCell(9);
	        if (record.getR43_moreThan10Years() != null) {
	            R43cell8.setCellValue(record.getR43_moreThan10Years().doubleValue());
	            R43cell8.setCellStyle(numberStyle);
	        } else {
	            R43cell8.setCellValue("");
	            R43cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(43);
	        Cell R44cell1 = row.createCell(2);
	        if (record.getR44_upTo1Month() != null) {
	            R44cell1.setCellValue(record.getR44_upTo1Month().doubleValue());
	            R44cell1.setCellStyle(numberStyle);
	        } else {
	            R44cell1.setCellValue("");
	            R44cell1.setCellStyle(textStyle);
	        }

	        Cell R44cell2 = row.createCell(3);
	        if (record.getR44_moreThan1MonthTo3Months() != null) {
	            R44cell2.setCellValue(record.getR44_moreThan1MonthTo3Months().doubleValue());
	            R44cell2.setCellStyle(numberStyle);
	        } else {
	            R44cell2.setCellValue("");
	            R44cell2.setCellStyle(textStyle);
	        }

	        Cell R44cell3 = row.createCell(4);
	        if (record.getR44_moreThan3MonthTo6Months() != null) {
	            R44cell3.setCellValue(record.getR44_moreThan3MonthTo6Months().doubleValue());
	            R44cell3.setCellStyle(numberStyle);
	        } else {
	            R44cell3.setCellValue("");
	            R44cell3.setCellStyle(textStyle);
	        }

	        Cell R44cell4 = row.createCell(5);
	        if (record.getR44_moreThan6MonthTo12Months() != null) {
	            R44cell4.setCellValue(record.getR44_moreThan6MonthTo12Months().doubleValue());
	            R44cell4.setCellStyle(numberStyle);
	        } else {
	            R44cell4.setCellValue("");
	            R44cell4.setCellStyle(textStyle);
	        }

	        Cell R44cell5 = row.createCell(6);
	        if (record.getR44_moreThan12MonthTo3Years() != null) {
	            R44cell5.setCellValue(record.getR44_moreThan12MonthTo3Years().doubleValue());
	            R44cell5.setCellStyle(numberStyle);
	        } else {
	            R44cell5.setCellValue("");
	            R44cell5.setCellStyle(textStyle);
	        }

	        Cell R44cell6 = row.createCell(7);
	        if (record.getR44_moreThan3YearsTo5Years() != null) {
	            R44cell6.setCellValue(record.getR44_moreThan3YearsTo5Years().doubleValue());
	            R44cell6.setCellStyle(numberStyle);
	        } else {
	            R44cell6.setCellValue("");
	            R44cell6.setCellStyle(textStyle);
	        }

	        Cell R44cell7 = row.createCell(8);
	        if (record.getR44_moreThan5YearsTo10Years() != null) {
	            R44cell7.setCellValue(record.getR44_moreThan5YearsTo10Years().doubleValue());
	            R44cell7.setCellStyle(numberStyle);
	        } else {
	            R44cell7.setCellValue("");
	            R44cell7.setCellStyle(textStyle);
	        }

	        Cell R44cell8 = row.createCell(9);
	        if (record.getR44_moreThan10Years() != null) {
	            R44cell8.setCellValue(record.getR44_moreThan10Years().doubleValue());
	            R44cell8.setCellStyle(numberStyle);
	        } else {
	            R44cell8.setCellValue("");
	            R44cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(44);
	        Cell R45cell1 = row.createCell(2);
	        if (record.getR45_upTo1Month() != null) {
	            R45cell1.setCellValue(record.getR45_upTo1Month().doubleValue());
	            R45cell1.setCellStyle(numberStyle);
	        } else {
	            R45cell1.setCellValue("");
	            R45cell1.setCellStyle(textStyle);
	        }

	        Cell R45cell2 = row.createCell(3);
	        if (record.getR45_moreThan1MonthTo3Months() != null) {
	            R45cell2.setCellValue(record.getR45_moreThan1MonthTo3Months().doubleValue());
	            R45cell2.setCellStyle(numberStyle);
	        } else {
	            R45cell2.setCellValue("");
	            R45cell2.setCellStyle(textStyle);
	        }

	        Cell R45cell3 = row.createCell(4);
	        if (record.getR45_moreThan3MonthTo6Months() != null) {
	            R45cell3.setCellValue(record.getR45_moreThan3MonthTo6Months().doubleValue());
	            R45cell3.setCellStyle(numberStyle);
	        } else {
	            R45cell3.setCellValue("");
	            R45cell3.setCellStyle(textStyle);
	        }

	        Cell R45cell4 = row.createCell(5);
	        if (record.getR45_moreThan6MonthTo12Months() != null) {
	            R45cell4.setCellValue(record.getR45_moreThan6MonthTo12Months().doubleValue());
	            R45cell4.setCellStyle(numberStyle);
	        } else {
	            R45cell4.setCellValue("");
	            R45cell4.setCellStyle(textStyle);
	        }

	        Cell R45cell5 = row.createCell(6);
	        if (record.getR45_moreThan12MonthTo3Years() != null) {
	            R45cell5.setCellValue(record.getR45_moreThan12MonthTo3Years().doubleValue());
	            R45cell5.setCellStyle(numberStyle);
	        } else {
	            R45cell5.setCellValue("");
	            R45cell5.setCellStyle(textStyle);
	        }

	        Cell R45cell6 = row.createCell(7);
	        if (record.getR45_moreThan3YearsTo5Years() != null) {
	            R45cell6.setCellValue(record.getR45_moreThan3YearsTo5Years().doubleValue());
	            R45cell6.setCellStyle(numberStyle);
	        } else {
	            R45cell6.setCellValue("");
	            R45cell6.setCellStyle(textStyle);
	        }

	        Cell R45cell7 = row.createCell(8);
	        if (record.getR45_moreThan5YearsTo10Years() != null) {
	            R45cell7.setCellValue(record.getR45_moreThan5YearsTo10Years().doubleValue());
	            R45cell7.setCellStyle(numberStyle);
	        } else {
	            R45cell7.setCellValue("");
	            R45cell7.setCellStyle(textStyle);
	        }

	        Cell R45cell8 = row.createCell(9);
	        if (record.getR45_moreThan10Years() != null) {
	            R45cell8.setCellValue(record.getR45_moreThan10Years().doubleValue());
	            R45cell8.setCellStyle(numberStyle);
	        } else {
	            R45cell8.setCellValue("");
	            R45cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(45);
	        Cell R46cell1 = row.createCell(2);
	        if (record.getR46_upTo1Month() != null) {
	            R46cell1.setCellValue(record.getR46_upTo1Month().doubleValue());
	            R46cell1.setCellStyle(numberStyle);
	        } else {
	            R46cell1.setCellValue("");
	            R46cell1.setCellStyle(textStyle);
	        }

	        Cell R46cell2 = row.createCell(3);
	        if (record.getR46_moreThan1MonthTo3Months() != null) {
	            R46cell2.setCellValue(record.getR46_moreThan1MonthTo3Months().doubleValue());
	            R46cell2.setCellStyle(numberStyle);
	        } else {
	            R46cell2.setCellValue("");
	            R46cell2.setCellStyle(textStyle);
	        }

	        Cell R46cell3 = row.createCell(4);
	        if (record.getR46_moreThan3MonthTo6Months() != null) {
	            R46cell3.setCellValue(record.getR46_moreThan3MonthTo6Months().doubleValue());
	            R46cell3.setCellStyle(numberStyle);
	        } else {
	            R46cell3.setCellValue("");
	            R46cell3.setCellStyle(textStyle);
	        }

	        Cell R46cell4 = row.createCell(5);
	        if (record.getR46_moreThan6MonthTo12Months() != null) {
	            R46cell4.setCellValue(record.getR46_moreThan6MonthTo12Months().doubleValue());
	            R46cell4.setCellStyle(numberStyle);
	        } else {
	            R46cell4.setCellValue("");
	            R46cell4.setCellStyle(textStyle);
	        }

	        Cell R46cell5 = row.createCell(6);
	        if (record.getR46_moreThan12MonthTo3Years() != null) {
	            R46cell5.setCellValue(record.getR46_moreThan12MonthTo3Years().doubleValue());
	            R46cell5.setCellStyle(numberStyle);
	        } else {
	            R46cell5.setCellValue("");
	            R46cell5.setCellStyle(textStyle);
	        }

	        Cell R46cell6 = row.createCell(7);
	        if (record.getR46_moreThan3YearsTo5Years() != null) {
	            R46cell6.setCellValue(record.getR46_moreThan3YearsTo5Years().doubleValue());
	            R46cell6.setCellStyle(numberStyle);
	        } else {
	            R46cell6.setCellValue("");
	            R46cell6.setCellStyle(textStyle);
	        }

	        Cell R46cell7 = row.createCell(8);
	        if (record.getR46_moreThan5YearsTo10Years() != null) {
	            R46cell7.setCellValue(record.getR46_moreThan5YearsTo10Years().doubleValue());
	            R46cell7.setCellStyle(numberStyle);
	        } else {
	            R46cell7.setCellValue("");
	            R46cell7.setCellStyle(textStyle);
	        }

	        Cell R46cell8 = row.createCell(9);
	        if (record.getR46_moreThan10Years() != null) {
	            R46cell8.setCellValue(record.getR46_moreThan10Years().doubleValue());
	            R46cell8.setCellStyle(numberStyle);
	        } else {
	            R46cell8.setCellValue("");
	            R46cell8.setCellStyle(textStyle);
	        }
	        
	        
		    
	        row = sheet.getRow(47);
	        Cell R48cell9 = row.createCell(10);
	        if (record.getR48_nonRatioSensativeItems() != null) {
	            R48cell9.setCellValue(record.getR48_nonRatioSensativeItems().doubleValue());
	            R48cell9.setCellStyle(numberStyle);
	        } else {
	            R48cell9.setCellValue("");
	            R48cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(48);
	        Cell R49cell9 = row.createCell(10);
	        if (record.getR49_nonRatioSensativeItems() != null) {
	            R49cell9.setCellValue(record.getR49_nonRatioSensativeItems().doubleValue());
	            R49cell9.setCellStyle(numberStyle);
	        } else {
	            R49cell9.setCellValue("");
	            R49cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(49);
	        Cell R50cell9 = row.createCell(10);
	        if (record.getR50_nonRatioSensativeItems() != null) {
	            R50cell9.setCellValue(record.getR50_nonRatioSensativeItems().doubleValue());
	            R50cell9.setCellStyle(numberStyle);
	        } else {
	            R50cell9.setCellValue("");
	            R50cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(50);
	        Cell R51cell9 = row.createCell(10);
	        if (record.getR51_nonRatioSensativeItems() != null) {
	            R51cell9.setCellValue(record.getR51_nonRatioSensativeItems().doubleValue());
	            R51cell9.setCellStyle(numberStyle);
	        } else {
	            R51cell9.setCellValue("");
	            R51cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(51);
	        Cell R52cell9 = row.createCell(10);
	        if (record.getR52_nonRatioSensativeItems() != null) {
	            R52cell9.setCellValue(record.getR52_nonRatioSensativeItems().doubleValue());
	            R52cell9.setCellStyle(numberStyle);
	        } else {
	            R52cell9.setCellValue("");
	            R52cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(52);
	        Cell R53cell9 = row.createCell(10);
	        if (record.getR53_nonRatioSensativeItems() != null) {
	            R53cell9.setCellValue(record.getR53_nonRatioSensativeItems().doubleValue());
	            R53cell9.setCellStyle(numberStyle);
	        } else {
	            R53cell9.setCellValue("");
	            R53cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(53);
	        Cell R54cell9 = row.createCell(10);
	        if (record.getR54_nonRatioSensativeItems() != null) {
	            R54cell9.setCellValue(record.getR54_nonRatioSensativeItems().doubleValue());
	            R54cell9.setCellStyle(numberStyle);
	        } else {
	            R54cell9.setCellValue("");
	            R54cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(54);
	        Cell R55cell9 = row.createCell(10);
	        if (record.getR55_nonRatioSensativeItems() != null) {
	            R55cell9.setCellValue(record.getR55_nonRatioSensativeItems().doubleValue());
	            R55cell9.setCellStyle(numberStyle);
	        } else {
	            R55cell9.setCellValue("");
	            R55cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(55);
	        Cell R56cell9 = row.createCell(10);
	        if (record.getR56_nonRatioSensativeItems() != null) {
	            R56cell9.setCellValue(record.getR56_nonRatioSensativeItems().doubleValue());
	            R56cell9.setCellStyle(numberStyle);
	        } else {
	            R56cell9.setCellValue("");
	            R56cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(56);
	        Cell R57cell9 = row.createCell(10);
	        if (record.getR57_nonRatioSensativeItems() != null) {
	            R57cell9.setCellValue(record.getR57_nonRatioSensativeItems().doubleValue());
	            R57cell9.setCellStyle(numberStyle);
	        } else {
	            R57cell9.setCellValue("");
	            R57cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(57);
	        Cell R58cell9 = row.createCell(10);
	        if (record.getR58_nonRatioSensativeItems() != null) {
	            R58cell9.setCellValue(record.getR58_nonRatioSensativeItems().doubleValue());
	            R58cell9.setCellStyle(numberStyle);
	        } else {
	            R58cell9.setCellValue("");
	            R58cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(60);
	        Cell R61cell1 = row.createCell(2);
	        if (record.getR61_upTo1Month() != null) {
	            R61cell1.setCellValue(record.getR61_upTo1Month().doubleValue());
	            R61cell1.setCellStyle(numberStyle);
	        } else {
	            R61cell1.setCellValue("");
	            R61cell1.setCellStyle(textStyle);
	        }

	        Cell R61cell2 = row.createCell(3);
	        if (record.getR61_moreThan1MonthTo3Months() != null) {
	            R61cell2.setCellValue(record.getR61_moreThan1MonthTo3Months().doubleValue());
	            R61cell2.setCellStyle(numberStyle);
	        } else {
	            R61cell2.setCellValue("");
	            R61cell2.setCellStyle(textStyle);
	        }

	        Cell R61cell3 = row.createCell(4);
	        if (record.getR61_moreThan3MonthTo6Months() != null) {
	            R61cell3.setCellValue(record.getR61_moreThan3MonthTo6Months().doubleValue());
	            R61cell3.setCellStyle(numberStyle);
	        } else {
	            R61cell3.setCellValue("");
	            R61cell3.setCellStyle(textStyle);
	        }

	        Cell R61cell4 = row.createCell(5);
	        if (record.getR61_moreThan6MonthTo12Months() != null) {
	            R61cell4.setCellValue(record.getR61_moreThan6MonthTo12Months().doubleValue());
	            R61cell4.setCellStyle(numberStyle);
	        } else {
	            R61cell4.setCellValue("");
	            R61cell4.setCellStyle(textStyle);
	        }

	        Cell R61cell5 = row.createCell(6);
	        if (record.getR61_moreThan12MonthTo3Years() != null) {
	            R61cell5.setCellValue(record.getR61_moreThan12MonthTo3Years().doubleValue());
	            R61cell5.setCellStyle(numberStyle);
	        } else {
	            R61cell5.setCellValue("");
	            R61cell5.setCellStyle(textStyle);
	        }

	        Cell R61cell6 = row.createCell(7);
	        if (record.getR61_moreThan3YearsTo5Years() != null) {
	            R61cell6.setCellValue(record.getR61_moreThan3YearsTo5Years().doubleValue());
	            R61cell6.setCellStyle(numberStyle);
	        } else {
	            R61cell6.setCellValue("");
	            R61cell6.setCellStyle(textStyle);
	        }

	        Cell R61cell7 = row.createCell(8);
	        if (record.getR61_moreThan5YearsTo10Years() != null) {
	            R61cell7.setCellValue(record.getR61_moreThan5YearsTo10Years().doubleValue());
	            R61cell7.setCellStyle(numberStyle);
	        } else {
	            R61cell7.setCellValue("");
	            R61cell7.setCellStyle(textStyle);
	        }

	        Cell R61cell8 = row.createCell(9);
	        if (record.getR61_moreThan10Years() != null) {
	            R61cell8.setCellValue(record.getR61_moreThan10Years().doubleValue());
	            R61cell8.setCellStyle(numberStyle);
	        } else {
	            R61cell8.setCellValue("");
	            R61cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(61);
	        Cell R62cell1 = row.createCell(2);
	        if (record.getR62_upTo1Month() != null) {
	            R62cell1.setCellValue(record.getR62_upTo1Month().doubleValue());
	            R62cell1.setCellStyle(numberStyle);
	        } else {
	            R62cell1.setCellValue("");
	            R62cell1.setCellStyle(textStyle);
	        }

	        Cell R62cell2 = row.createCell(3);
	        if (record.getR62_moreThan1MonthTo3Months() != null) {
	            R62cell2.setCellValue(record.getR62_moreThan1MonthTo3Months().doubleValue());
	            R62cell2.setCellStyle(numberStyle);
	        } else {
	            R62cell2.setCellValue("");
	            R62cell2.setCellStyle(textStyle);
	        }

	        Cell R62cell3 = row.createCell(4);
	        if (record.getR62_moreThan3MonthTo6Months() != null) {
	            R62cell3.setCellValue(record.getR62_moreThan3MonthTo6Months().doubleValue());
	            R62cell3.setCellStyle(numberStyle);
	        } else {
	            R62cell3.setCellValue("");
	            R62cell3.setCellStyle(textStyle);
	        }

	        Cell R62cell4 = row.createCell(5);
	        if (record.getR62_moreThan6MonthTo12Months() != null) {
	            R62cell4.setCellValue(record.getR62_moreThan6MonthTo12Months().doubleValue());
	            R62cell4.setCellStyle(numberStyle);
	        } else {
	            R62cell4.setCellValue("");
	            R62cell4.setCellStyle(textStyle);
	        }

	        Cell R62cell5 = row.createCell(6);
	        if (record.getR62_moreThan12MonthTo3Years() != null) {
	            R62cell5.setCellValue(record.getR62_moreThan12MonthTo3Years().doubleValue());
	            R62cell5.setCellStyle(numberStyle);
	        } else {
	            R62cell5.setCellValue("");
	            R62cell5.setCellStyle(textStyle);
	        }

	        Cell R62cell6 = row.createCell(7);
	        if (record.getR62_moreThan3YearsTo5Years() != null) {
	            R62cell6.setCellValue(record.getR62_moreThan3YearsTo5Years().doubleValue());
	            R62cell6.setCellStyle(numberStyle);
	        } else {
	            R62cell6.setCellValue("");
	            R62cell6.setCellStyle(textStyle);
	        }

	        Cell R62cell7 = row.createCell(8);
	        if (record.getR62_moreThan5YearsTo10Years() != null) {
	            R62cell7.setCellValue(record.getR62_moreThan5YearsTo10Years().doubleValue());
	            R62cell7.setCellStyle(numberStyle);
	        } else {
	            R62cell7.setCellValue("");
	            R62cell7.setCellStyle(textStyle);
	        }

	        Cell R62cell8 = row.createCell(9);
	        if (record.getR62_moreThan10Years() != null) {
	            R62cell8.setCellValue(record.getR62_moreThan10Years().doubleValue());
	            R62cell8.setCellStyle(numberStyle);
	        } else {
	            R62cell8.setCellValue("");
	            R62cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(62);
	        Cell R63cell1 = row.createCell(2);
	        if (record.getR63_upTo1Month() != null) {
	            R63cell1.setCellValue(record.getR63_upTo1Month().doubleValue());
	            R63cell1.setCellStyle(numberStyle);
	        } else {
	            R63cell1.setCellValue("");
	            R63cell1.setCellStyle(textStyle);
	        }

	        Cell R63cell2 = row.createCell(3);
	        if (record.getR63_moreThan1MonthTo3Months() != null) {
	            R63cell2.setCellValue(record.getR63_moreThan1MonthTo3Months().doubleValue());
	            R63cell2.setCellStyle(numberStyle);
	        } else {
	            R63cell2.setCellValue("");
	            R63cell2.setCellStyle(textStyle);
	        }

	        Cell R63cell3 = row.createCell(4);
	        if (record.getR63_moreThan3MonthTo6Months() != null) {
	            R63cell3.setCellValue(record.getR63_moreThan3MonthTo6Months().doubleValue());
	            R63cell3.setCellStyle(numberStyle);
	        } else {
	            R63cell3.setCellValue("");
	            R63cell3.setCellStyle(textStyle);
	        }

	        Cell R63cell4 = row.createCell(5);
	        if (record.getR63_moreThan6MonthTo12Months() != null) {
	            R63cell4.setCellValue(record.getR63_moreThan6MonthTo12Months().doubleValue());
	            R63cell4.setCellStyle(numberStyle);
	        } else {
	            R63cell4.setCellValue("");
	            R63cell4.setCellStyle(textStyle);
	        }

	        Cell R63cell5 = row.createCell(6);
	        if (record.getR63_moreThan12MonthTo3Years() != null) {
	            R63cell5.setCellValue(record.getR63_moreThan12MonthTo3Years().doubleValue());
	            R63cell5.setCellStyle(numberStyle);
	        } else {
	            R63cell5.setCellValue("");
	            R63cell5.setCellStyle(textStyle);
	        }

	        Cell R63cell6 = row.createCell(7);
	        if (record.getR63_moreThan3YearsTo5Years() != null) {
	            R63cell6.setCellValue(record.getR63_moreThan3YearsTo5Years().doubleValue());
	            R63cell6.setCellStyle(numberStyle);
	        } else {
	            R63cell6.setCellValue("");
	            R63cell6.setCellStyle(textStyle);
	        }

	        Cell R63cell7 = row.createCell(8);
	        if (record.getR63_moreThan5YearsTo10Years() != null) {
	            R63cell7.setCellValue(record.getR63_moreThan5YearsTo10Years().doubleValue());
	            R63cell7.setCellStyle(numberStyle);
	        } else {
	            R63cell7.setCellValue("");
	            R63cell7.setCellStyle(textStyle);
	        }

	        Cell R63cell8 = row.createCell(9);
	        if (record.getR63_moreThan10Years() != null) {
	            R63cell8.setCellValue(record.getR63_moreThan10Years().doubleValue());
	            R63cell8.setCellStyle(numberStyle);
	        } else {
	            R63cell8.setCellValue("");
	            R63cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(63);
	        Cell R64cell1 = row.createCell(2);
	        if (record.getR64_upTo1Month() != null) {
	            R64cell1.setCellValue(record.getR64_upTo1Month().doubleValue());
	            R64cell1.setCellStyle(numberStyle);
	        } else {
	            R64cell1.setCellValue("");
	            R64cell1.setCellStyle(textStyle);
	        }

	        Cell R64cell2 = row.createCell(3);
	        if (record.getR64_moreThan1MonthTo3Months() != null) {
	            R64cell2.setCellValue(record.getR64_moreThan1MonthTo3Months().doubleValue());
	            R64cell2.setCellStyle(numberStyle);
	        } else {
	            R64cell2.setCellValue("");
	            R64cell2.setCellStyle(textStyle);
	        }

	        Cell R64cell3 = row.createCell(4);
	        if (record.getR64_moreThan3MonthTo6Months() != null) {
	            R64cell3.setCellValue(record.getR64_moreThan3MonthTo6Months().doubleValue());
	            R64cell3.setCellStyle(numberStyle);
	        } else {
	            R64cell3.setCellValue("");
	            R64cell3.setCellStyle(textStyle);
	        }

	        Cell R64cell4 = row.createCell(5);
	        if (record.getR64_moreThan6MonthTo12Months() != null) {
	            R64cell4.setCellValue(record.getR64_moreThan6MonthTo12Months().doubleValue());
	            R64cell4.setCellStyle(numberStyle);
	        } else {
	            R64cell4.setCellValue("");
	            R64cell4.setCellStyle(textStyle);
	        }

	        Cell R64cell5 = row.createCell(6);
	        if (record.getR64_moreThan12MonthTo3Years() != null) {
	            R64cell5.setCellValue(record.getR64_moreThan12MonthTo3Years().doubleValue());
	            R64cell5.setCellStyle(numberStyle);
	        } else {
	            R64cell5.setCellValue("");
	            R64cell5.setCellStyle(textStyle);
	        }

	        Cell R64cell6 = row.createCell(7);
	        if (record.getR64_moreThan3YearsTo5Years() != null) {
	            R64cell6.setCellValue(record.getR64_moreThan3YearsTo5Years().doubleValue());
	            R64cell6.setCellStyle(numberStyle);
	        } else {
	            R64cell6.setCellValue("");
	            R64cell6.setCellStyle(textStyle);
	        }

	        Cell R64cell7 = row.createCell(8);
	        if (record.getR64_moreThan5YearsTo10Years() != null) {
	            R64cell7.setCellValue(record.getR64_moreThan5YearsTo10Years().doubleValue());
	            R64cell7.setCellStyle(numberStyle);
	        } else {
	            R64cell7.setCellValue("");
	            R64cell7.setCellStyle(textStyle);
	        }

	        Cell R64cell8 = row.createCell(9);
	        if (record.getR64_moreThan10Years() != null) {
	            R64cell8.setCellValue(record.getR64_moreThan10Years().doubleValue());
	            R64cell8.setCellStyle(numberStyle);
	        } else {
	            R64cell8.setCellValue("");
	            R64cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(64);
	        Cell R65cell1 = row.createCell(2);
	        if (record.getR65_upTo1Month() != null) {
	            R65cell1.setCellValue(record.getR65_upTo1Month().doubleValue());
	            R65cell1.setCellStyle(numberStyle);
	        } else {
	            R65cell1.setCellValue("");
	            R65cell1.setCellStyle(textStyle);
	        }

	        Cell R65cell2 = row.createCell(3);
	        if (record.getR65_moreThan1MonthTo3Months() != null) {
	            R65cell2.setCellValue(record.getR65_moreThan1MonthTo3Months().doubleValue());
	            R65cell2.setCellStyle(numberStyle);
	        } else {
	            R65cell2.setCellValue("");
	            R65cell2.setCellStyle(textStyle);
	        }

	        Cell R65cell3 = row.createCell(4);
	        if (record.getR65_moreThan3MonthTo6Months() != null) {
	            R65cell3.setCellValue(record.getR65_moreThan3MonthTo6Months().doubleValue());
	            R65cell3.setCellStyle(numberStyle);
	        } else {
	            R65cell3.setCellValue("");
	            R65cell3.setCellStyle(textStyle);
	        }

	        Cell R65cell4 = row.createCell(5);
	        if (record.getR65_moreThan6MonthTo12Months() != null) {
	            R65cell4.setCellValue(record.getR65_moreThan6MonthTo12Months().doubleValue());
	            R65cell4.setCellStyle(numberStyle);
	        } else {
	            R65cell4.setCellValue("");
	            R65cell4.setCellStyle(textStyle);
	        }

	        Cell R65cell5 = row.createCell(6);
	        if (record.getR65_moreThan12MonthTo3Years() != null) {
	            R65cell5.setCellValue(record.getR65_moreThan12MonthTo3Years().doubleValue());
	            R65cell5.setCellStyle(numberStyle);
	        } else {
	            R65cell5.setCellValue("");
	            R65cell5.setCellStyle(textStyle);
	        }

	        Cell R65cell6 = row.createCell(7);
	        if (record.getR65_moreThan3YearsTo5Years() != null) {
	            R65cell6.setCellValue(record.getR65_moreThan3YearsTo5Years().doubleValue());
	            R65cell6.setCellStyle(numberStyle);
	        } else {
	            R65cell6.setCellValue("");
	            R65cell6.setCellStyle(textStyle);
	        }

	        Cell R65cell7 = row.createCell(8);
	        if (record.getR65_moreThan5YearsTo10Years() != null) {
	            R65cell7.setCellValue(record.getR65_moreThan5YearsTo10Years().doubleValue());
	            R65cell7.setCellStyle(numberStyle);
	        } else {
	            R65cell7.setCellValue("");
	            R65cell7.setCellStyle(textStyle);
	        }

	        Cell R65cell8 = row.createCell(9);
	        if (record.getR65_moreThan10Years() != null) {
	            R65cell8.setCellValue(record.getR65_moreThan10Years().doubleValue());
	            R65cell8.setCellStyle(numberStyle);
	        } else {
	            R65cell8.setCellValue("");
	            R65cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(65);
	        Cell R66cell1 = row.createCell(2);
	        if (record.getR66_upTo1Month() != null) {
	            R66cell1.setCellValue(record.getR66_upTo1Month().doubleValue());
	            R66cell1.setCellStyle(numberStyle);
	        } else {
	            R66cell1.setCellValue("");
	            R66cell1.setCellStyle(textStyle);
	        }

	        Cell R66cell2 = row.createCell(3);
	        if (record.getR66_moreThan1MonthTo3Months() != null) {
	            R66cell2.setCellValue(record.getR66_moreThan1MonthTo3Months().doubleValue());
	            R66cell2.setCellStyle(numberStyle);
	        } else {
	            R66cell2.setCellValue("");
	            R66cell2.setCellStyle(textStyle);
	        }

	        Cell R66cell3 = row.createCell(4);
	        if (record.getR66_moreThan3MonthTo6Months() != null) {
	            R66cell3.setCellValue(record.getR66_moreThan3MonthTo6Months().doubleValue());
	            R66cell3.setCellStyle(numberStyle);
	        } else {
	            R66cell3.setCellValue("");
	            R66cell3.setCellStyle(textStyle);
	        }

	        Cell R66cell4 = row.createCell(5);
	        if (record.getR66_moreThan6MonthTo12Months() != null) {
	            R66cell4.setCellValue(record.getR66_moreThan6MonthTo12Months().doubleValue());
	            R66cell4.setCellStyle(numberStyle);
	        } else {
	            R66cell4.setCellValue("");
	            R66cell4.setCellStyle(textStyle);
	        }

	        Cell R66cell5 = row.createCell(6);
	        if (record.getR66_moreThan12MonthTo3Years() != null) {
	            R66cell5.setCellValue(record.getR66_moreThan12MonthTo3Years().doubleValue());
	            R66cell5.setCellStyle(numberStyle);
	        } else {
	            R66cell5.setCellValue("");
	            R66cell5.setCellStyle(textStyle);
	        }

	        Cell R66cell6 = row.createCell(7);
	        if (record.getR66_moreThan3YearsTo5Years() != null) {
	            R66cell6.setCellValue(record.getR66_moreThan3YearsTo5Years().doubleValue());
	            R66cell6.setCellStyle(numberStyle);
	        } else {
	            R66cell6.setCellValue("");
	            R66cell6.setCellStyle(textStyle);
	        }

	        Cell R66cell7 = row.createCell(8);
	        if (record.getR66_moreThan5YearsTo10Years() != null) {
	            R66cell7.setCellValue(record.getR66_moreThan5YearsTo10Years().doubleValue());
	            R66cell7.setCellStyle(numberStyle);
	        } else {
	            R66cell7.setCellValue("");
	            R66cell7.setCellStyle(textStyle);
	        }

	        Cell R66cell8 = row.createCell(9);
	        if (record.getR66_moreThan10Years() != null) {
	            R66cell8.setCellValue(record.getR66_moreThan10Years().doubleValue());
	            R66cell8.setCellStyle(numberStyle);
	        } else {
	            R66cell8.setCellValue("");
	            R66cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(66);
	        Cell R67cell1 = row.createCell(2);
	        if (record.getR67_upTo1Month() != null) {
	            R67cell1.setCellValue(record.getR67_upTo1Month().doubleValue());
	            R67cell1.setCellStyle(numberStyle);
	        } else {
	            R67cell1.setCellValue("");
	            R67cell1.setCellStyle(textStyle);
	        }

	        Cell R67cell2 = row.createCell(3);
	        if (record.getR67_moreThan1MonthTo3Months() != null) {
	            R67cell2.setCellValue(record.getR67_moreThan1MonthTo3Months().doubleValue());
	            R67cell2.setCellStyle(numberStyle);
	        } else {
	            R67cell2.setCellValue("");
	            R67cell2.setCellStyle(textStyle);
	        }

	        Cell R67cell3 = row.createCell(4);
	        if (record.getR67_moreThan3MonthTo6Months() != null) {
	            R67cell3.setCellValue(record.getR67_moreThan3MonthTo6Months().doubleValue());
	            R67cell3.setCellStyle(numberStyle);
	        } else {
	            R67cell3.setCellValue("");
	            R67cell3.setCellStyle(textStyle);
	        }

	        Cell R67cell4 = row.createCell(5);
	        if (record.getR67_moreThan6MonthTo12Months() != null) {
	            R67cell4.setCellValue(record.getR67_moreThan6MonthTo12Months().doubleValue());
	            R67cell4.setCellStyle(numberStyle);
	        } else {
	            R67cell4.setCellValue("");
	            R67cell4.setCellStyle(textStyle);
	        }

	        Cell R67cell5 = row.createCell(6);
	        if (record.getR67_moreThan12MonthTo3Years() != null) {
	            R67cell5.setCellValue(record.getR67_moreThan12MonthTo3Years().doubleValue());
	            R67cell5.setCellStyle(numberStyle);
	        } else {
	            R67cell5.setCellValue("");
	            R67cell5.setCellStyle(textStyle);
	        }

	        Cell R67cell6 = row.createCell(7);
	        if (record.getR67_moreThan3YearsTo5Years() != null) {
	            R67cell6.setCellValue(record.getR67_moreThan3YearsTo5Years().doubleValue());
	            R67cell6.setCellStyle(numberStyle);
	        } else {
	            R67cell6.setCellValue("");
	            R67cell6.setCellStyle(textStyle);
	        }

	        Cell R67cell7 = row.createCell(8);
	        if (record.getR67_moreThan5YearsTo10Years() != null) {
	            R67cell7.setCellValue(record.getR67_moreThan5YearsTo10Years().doubleValue());
	            R67cell7.setCellStyle(numberStyle);
	        } else {
	            R67cell7.setCellValue("");
	            R67cell7.setCellStyle(textStyle);
	        }

	        Cell R67cell8 = row.createCell(9);
	        if (record.getR67_moreThan10Years() != null) {
	            R67cell8.setCellValue(record.getR67_moreThan10Years().doubleValue());
	            R67cell8.setCellStyle(numberStyle);
	        } else {
	            R67cell8.setCellValue("");
	            R67cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(68);
	        Cell R69cell1 = row.createCell(2);
	        if (record.getR69_upTo1Month() != null) {
	            R69cell1.setCellValue(record.getR69_upTo1Month().doubleValue());
	            R69cell1.setCellStyle(numberStyle);
	        } else {
	            R69cell1.setCellValue("");
	            R69cell1.setCellStyle(textStyle);
	        }

	        Cell R69cell2 = row.createCell(3);
	        if (record.getR69_moreThan1MonthTo3Months() != null) {
	            R69cell2.setCellValue(record.getR69_moreThan1MonthTo3Months().doubleValue());
	            R69cell2.setCellStyle(numberStyle);
	        } else {
	            R69cell2.setCellValue("");
	            R69cell2.setCellStyle(textStyle);
	        }

	        Cell R69cell3 = row.createCell(4);
	        if (record.getR69_moreThan3MonthTo6Months() != null) {
	            R69cell3.setCellValue(record.getR69_moreThan3MonthTo6Months().doubleValue());
	            R69cell3.setCellStyle(numberStyle);
	        } else {
	            R69cell3.setCellValue("");
	            R69cell3.setCellStyle(textStyle);
	        }

	        Cell R69cell4 = row.createCell(5);
	        if (record.getR69_moreThan6MonthTo12Months() != null) {
	            R69cell4.setCellValue(record.getR69_moreThan6MonthTo12Months().doubleValue());
	            R69cell4.setCellStyle(numberStyle);
	        } else {
	            R69cell4.setCellValue("");
	            R69cell4.setCellStyle(textStyle);
	        }

	        Cell R69cell5 = row.createCell(6);
	        if (record.getR69_moreThan12MonthTo3Years() != null) {
	            R69cell5.setCellValue(record.getR69_moreThan12MonthTo3Years().doubleValue());
	            R69cell5.setCellStyle(numberStyle);
	        } else {
	            R69cell5.setCellValue("");
	            R69cell5.setCellStyle(textStyle);
	        }

	        Cell R69cell6 = row.createCell(7);
	        if (record.getR69_moreThan3YearsTo5Years() != null) {
	            R69cell6.setCellValue(record.getR69_moreThan3YearsTo5Years().doubleValue());
	            R69cell6.setCellStyle(numberStyle);
	        } else {
	            R69cell6.setCellValue("");
	            R69cell6.setCellStyle(textStyle);
	        }

	        Cell R69cell7 = row.createCell(8);
	        if (record.getR69_moreThan5YearsTo10Years() != null) {
	            R69cell7.setCellValue(record.getR69_moreThan5YearsTo10Years().doubleValue());
	            R69cell7.setCellStyle(numberStyle);
	        } else {
	            R69cell7.setCellValue("");
	            R69cell7.setCellStyle(textStyle);
	        }

	        Cell R69cell8 = row.createCell(9);
	        if (record.getR69_moreThan10Years() != null) {
	            R69cell8.setCellValue(record.getR69_moreThan10Years().doubleValue());
	            R69cell8.setCellStyle(numberStyle);
	        } else {
	            R69cell8.setCellValue("");
	            R69cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(69);
	        Cell R70cell1 = row.createCell(2);
	        if (record.getR70_upTo1Month() != null) {
	            R70cell1.setCellValue(record.getR70_upTo1Month().doubleValue());
	            R70cell1.setCellStyle(numberStyle);
	        } else {
	            R70cell1.setCellValue("");
	            R70cell1.setCellStyle(textStyle);
	        }

	        Cell R70cell2 = row.createCell(3);
	        if (record.getR70_moreThan1MonthTo3Months() != null) {
	            R70cell2.setCellValue(record.getR70_moreThan1MonthTo3Months().doubleValue());
	            R70cell2.setCellStyle(numberStyle);
	        } else {
	            R70cell2.setCellValue("");
	            R70cell2.setCellStyle(textStyle);
	        }

	        Cell R70cell3 = row.createCell(4);
	        if (record.getR70_moreThan3MonthTo6Months() != null) {
	            R70cell3.setCellValue(record.getR70_moreThan3MonthTo6Months().doubleValue());
	            R70cell3.setCellStyle(numberStyle);
	        } else {
	            R70cell3.setCellValue("");
	            R70cell3.setCellStyle(textStyle);
	        }

	        Cell R70cell4 = row.createCell(5);
	        if (record.getR70_moreThan6MonthTo12Months() != null) {
	            R70cell4.setCellValue(record.getR70_moreThan6MonthTo12Months().doubleValue());
	            R70cell4.setCellStyle(numberStyle);
	        } else {
	            R70cell4.setCellValue("");
	            R70cell4.setCellStyle(textStyle);
	        }

	        Cell R70cell5 = row.createCell(6);
	        if (record.getR70_moreThan12MonthTo3Years() != null) {
	            R70cell5.setCellValue(record.getR70_moreThan12MonthTo3Years().doubleValue());
	            R70cell5.setCellStyle(numberStyle);
	        } else {
	            R70cell5.setCellValue("");
	            R70cell5.setCellStyle(textStyle);
	        }

	        Cell R70cell6 = row.createCell(7);
	        if (record.getR70_moreThan3YearsTo5Years() != null) {
	            R70cell6.setCellValue(record.getR70_moreThan3YearsTo5Years().doubleValue());
	            R70cell6.setCellStyle(numberStyle);
	        } else {
	            R70cell6.setCellValue("");
	            R70cell6.setCellStyle(textStyle);
	        }

	        Cell R70cell7 = row.createCell(8);
	        if (record.getR70_moreThan5YearsTo10Years() != null) {
	            R70cell7.setCellValue(record.getR70_moreThan5YearsTo10Years().doubleValue());
	            R70cell7.setCellStyle(numberStyle);
	        } else {
	            R70cell7.setCellValue("");
	            R70cell7.setCellStyle(textStyle);
	        }

	        Cell R70cell8 = row.createCell(9);
	        if (record.getR70_moreThan10Years() != null) {
	            R70cell8.setCellValue(record.getR70_moreThan10Years().doubleValue());
	            R70cell8.setCellStyle(numberStyle);
	        } else {
	            R70cell8.setCellValue("");
	            R70cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(70);
	        Cell R71cell1 = row.createCell(2);
	        if (record.getR71_upTo1Month() != null) {
	            R71cell1.setCellValue(record.getR71_upTo1Month().doubleValue());
	            R71cell1.setCellStyle(numberStyle);
	        } else {
	            R71cell1.setCellValue("");
	            R71cell1.setCellStyle(textStyle);
	        }

	        Cell R71cell2 = row.createCell(3);
	        if (record.getR71_moreThan1MonthTo3Months() != null) {
	            R71cell2.setCellValue(record.getR71_moreThan1MonthTo3Months().doubleValue());
	            R71cell2.setCellStyle(numberStyle);
	        } else {
	            R71cell2.setCellValue("");
	            R71cell2.setCellStyle(textStyle);
	        }

	        Cell R71cell3 = row.createCell(4);
	        if (record.getR71_moreThan3MonthTo6Months() != null) {
	            R71cell3.setCellValue(record.getR71_moreThan3MonthTo6Months().doubleValue());
	            R71cell3.setCellStyle(numberStyle);
	        } else {
	            R71cell3.setCellValue("");
	            R71cell3.setCellStyle(textStyle);
	        }

	        Cell R71cell4 = row.createCell(5);
	        if (record.getR71_moreThan6MonthTo12Months() != null) {
	            R71cell4.setCellValue(record.getR71_moreThan6MonthTo12Months().doubleValue());
	            R71cell4.setCellStyle(numberStyle);
	        } else {
	            R71cell4.setCellValue("");
	            R71cell4.setCellStyle(textStyle);
	        }

	        Cell R71cell5 = row.createCell(6);
	        if (record.getR71_moreThan12MonthTo3Years() != null) {
	            R71cell5.setCellValue(record.getR71_moreThan12MonthTo3Years().doubleValue());
	            R71cell5.setCellStyle(numberStyle);
	        } else {
	            R71cell5.setCellValue("");
	            R71cell5.setCellStyle(textStyle);
	        }

	        Cell R71cell6 = row.createCell(7);
	        if (record.getR71_moreThan3YearsTo5Years() != null) {
	            R71cell6.setCellValue(record.getR71_moreThan3YearsTo5Years().doubleValue());
	            R71cell6.setCellStyle(numberStyle);
	        } else {
	            R71cell6.setCellValue("");
	            R71cell6.setCellStyle(textStyle);
	        }

	        Cell R71cell7 = row.createCell(8);
	        if (record.getR71_moreThan5YearsTo10Years() != null) {
	            R71cell7.setCellValue(record.getR71_moreThan5YearsTo10Years().doubleValue());
	            R71cell7.setCellStyle(numberStyle);
	        } else {
	            R71cell7.setCellValue("");
	            R71cell7.setCellStyle(textStyle);
	        }

	        Cell R71cell8 = row.createCell(9);
	        if (record.getR71_moreThan10Years() != null) {
	            R71cell8.setCellValue(record.getR71_moreThan10Years().doubleValue());
	            R71cell8.setCellStyle(numberStyle);
	        } else {
	            R71cell8.setCellValue("");
	            R71cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(71);
	        Cell R72cell1 = row.createCell(2);
	        if (record.getR72_upTo1Month() != null) {
	            R72cell1.setCellValue(record.getR72_upTo1Month().doubleValue());
	            R72cell1.setCellStyle(numberStyle);
	        } else {
	            R72cell1.setCellValue("");
	            R72cell1.setCellStyle(textStyle);
	        }

	        Cell R72cell2 = row.createCell(3);
	        if (record.getR72_moreThan1MonthTo3Months() != null) {
	            R72cell2.setCellValue(record.getR72_moreThan1MonthTo3Months().doubleValue());
	            R72cell2.setCellStyle(numberStyle);
	        } else {
	            R72cell2.setCellValue("");
	            R72cell2.setCellStyle(textStyle);
	        }

	        Cell R72cell3 = row.createCell(4);
	        if (record.getR72_moreThan3MonthTo6Months() != null) {
	            R72cell3.setCellValue(record.getR72_moreThan3MonthTo6Months().doubleValue());
	            R72cell3.setCellStyle(numberStyle);
	        } else {
	            R72cell3.setCellValue("");
	            R72cell3.setCellStyle(textStyle);
	        }

	        Cell R72cell4 = row.createCell(5);
	        if (record.getR72_moreThan6MonthTo12Months() != null) {
	            R72cell4.setCellValue(record.getR72_moreThan6MonthTo12Months().doubleValue());
	            R72cell4.setCellStyle(numberStyle);
	        } else {
	            R72cell4.setCellValue("");
	            R72cell4.setCellStyle(textStyle);
	        }

	        Cell R72cell5 = row.createCell(6);
	        if (record.getR72_moreThan12MonthTo3Years() != null) {
	            R72cell5.setCellValue(record.getR72_moreThan12MonthTo3Years().doubleValue());
	            R72cell5.setCellStyle(numberStyle);
	        } else {
	            R72cell5.setCellValue("");
	            R72cell5.setCellStyle(textStyle);
	        }

	        Cell R72cell6 = row.createCell(7);
	        if (record.getR72_moreThan3YearsTo5Years() != null) {
	            R72cell6.setCellValue(record.getR72_moreThan3YearsTo5Years().doubleValue());
	            R72cell6.setCellStyle(numberStyle);
	        } else {
	            R72cell6.setCellValue("");
	            R72cell6.setCellStyle(textStyle);
	        }

	        Cell R72cell7 = row.createCell(8);
	        if (record.getR72_moreThan5YearsTo10Years() != null) {
	            R72cell7.setCellValue(record.getR72_moreThan5YearsTo10Years().doubleValue());
	            R72cell7.setCellStyle(numberStyle);
	        } else {
	            R72cell7.setCellValue("");
	            R72cell7.setCellStyle(textStyle);
	        }

	        Cell R72cell8 = row.createCell(9);
	        if (record.getR72_moreThan10Years() != null) {
	            R72cell8.setCellValue(record.getR72_moreThan10Years().doubleValue());
	            R72cell8.setCellStyle(numberStyle);
	        } else {
	            R72cell8.setCellValue("");
	            R72cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(72);
	        Cell R73cell1 = row.createCell(2);
	        if (record.getR73_upTo1Month() != null) {
	            R73cell1.setCellValue(record.getR73_upTo1Month().doubleValue());
	            R73cell1.setCellStyle(numberStyle);
	        } else {
	            R73cell1.setCellValue("");
	            R73cell1.setCellStyle(textStyle);
	        }

	        Cell R73cell2 = row.createCell(3);
	        if (record.getR73_moreThan1MonthTo3Months() != null) {
	            R73cell2.setCellValue(record.getR73_moreThan1MonthTo3Months().doubleValue());
	            R73cell2.setCellStyle(numberStyle);
	        } else {
	            R73cell2.setCellValue("");
	            R73cell2.setCellStyle(textStyle);
	        }

	        Cell R73cell3 = row.createCell(4);
	        if (record.getR73_moreThan3MonthTo6Months() != null) {
	            R73cell3.setCellValue(record.getR73_moreThan3MonthTo6Months().doubleValue());
	            R73cell3.setCellStyle(numberStyle);
	        } else {
	            R73cell3.setCellValue("");
	            R73cell3.setCellStyle(textStyle);
	        }

	        Cell R73cell4 = row.createCell(5);
	        if (record.getR73_moreThan6MonthTo12Months() != null) {
	            R73cell4.setCellValue(record.getR73_moreThan6MonthTo12Months().doubleValue());
	            R73cell4.setCellStyle(numberStyle);
	        } else {
	            R73cell4.setCellValue("");
	            R73cell4.setCellStyle(textStyle);
	        }

	        Cell R73cell5 = row.createCell(6);
	        if (record.getR73_moreThan12MonthTo3Years() != null) {
	            R73cell5.setCellValue(record.getR73_moreThan12MonthTo3Years().doubleValue());
	            R73cell5.setCellStyle(numberStyle);
	        } else {
	            R73cell5.setCellValue("");
	            R73cell5.setCellStyle(textStyle);
	        }

	        Cell R73cell6 = row.createCell(7);
	        if (record.getR73_moreThan3YearsTo5Years() != null) {
	            R73cell6.setCellValue(record.getR73_moreThan3YearsTo5Years().doubleValue());
	            R73cell6.setCellStyle(numberStyle);
	        } else {
	            R73cell6.setCellValue("");
	            R73cell6.setCellStyle(textStyle);
	        }

	        Cell R73cell7 = row.createCell(8);
	        if (record.getR73_moreThan5YearsTo10Years() != null) {
	            R73cell7.setCellValue(record.getR73_moreThan5YearsTo10Years().doubleValue());
	            R73cell7.setCellStyle(numberStyle);
	        } else {
	            R73cell7.setCellValue("");
	            R73cell7.setCellStyle(textStyle);
	        }

	        Cell R73cell8 = row.createCell(9);
	        if (record.getR73_moreThan10Years() != null) {
	            R73cell8.setCellValue(record.getR73_moreThan10Years().doubleValue());
	            R73cell8.setCellStyle(numberStyle);
	        } else {
	            R73cell8.setCellValue("");
	            R73cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(73);
	        Cell R74cell1 = row.createCell(2);
	        if (record.getR74_upTo1Month() != null) {
	            R74cell1.setCellValue(record.getR74_upTo1Month().doubleValue());
	            R74cell1.setCellStyle(numberStyle);
	        } else {
	            R74cell1.setCellValue("");
	            R74cell1.setCellStyle(textStyle);
	        }

	        Cell R74cell2 = row.createCell(3);
	        if (record.getR74_moreThan1MonthTo3Months() != null) {
	            R74cell2.setCellValue(record.getR74_moreThan1MonthTo3Months().doubleValue());
	            R74cell2.setCellStyle(numberStyle);
	        } else {
	            R74cell2.setCellValue("");
	            R74cell2.setCellStyle(textStyle);
	        }

	        Cell R74cell3 = row.createCell(4);
	        if (record.getR74_moreThan3MonthTo6Months() != null) {
	            R74cell3.setCellValue(record.getR74_moreThan3MonthTo6Months().doubleValue());
	            R74cell3.setCellStyle(numberStyle);
	        } else {
	            R74cell3.setCellValue("");
	            R74cell3.setCellStyle(textStyle);
	        }

	        Cell R74cell4 = row.createCell(5);
	        if (record.getR74_moreThan6MonthTo12Months() != null) {
	            R74cell4.setCellValue(record.getR74_moreThan6MonthTo12Months().doubleValue());
	            R74cell4.setCellStyle(numberStyle);
	        } else {
	            R74cell4.setCellValue("");
	            R74cell4.setCellStyle(textStyle);
	        }

	        Cell R74cell5 = row.createCell(6);
	        if (record.getR74_moreThan12MonthTo3Years() != null) {
	            R74cell5.setCellValue(record.getR74_moreThan12MonthTo3Years().doubleValue());
	            R74cell5.setCellStyle(numberStyle);
	        } else {
	            R74cell5.setCellValue("");
	            R74cell5.setCellStyle(textStyle);
	        }

	        Cell R74cell6 = row.createCell(7);
	        if (record.getR74_moreThan3YearsTo5Years() != null) {
	            R74cell6.setCellValue(record.getR74_moreThan3YearsTo5Years().doubleValue());
	            R74cell6.setCellStyle(numberStyle);
	        } else {
	            R74cell6.setCellValue("");
	            R74cell6.setCellStyle(textStyle);
	        }

	        Cell R74cell7 = row.createCell(8);
	        if (record.getR74_moreThan5YearsTo10Years() != null) {
	            R74cell7.setCellValue(record.getR74_moreThan5YearsTo10Years().doubleValue());
	            R74cell7.setCellStyle(numberStyle);
	        } else {
	            R74cell7.setCellValue("");
	            R74cell7.setCellStyle(textStyle);
	        }

	        Cell R74cell8 = row.createCell(9);
	        if (record.getR74_moreThan10Years() != null) {
	            R74cell8.setCellValue(record.getR74_moreThan10Years().doubleValue());
	            R74cell8.setCellStyle(numberStyle);
	        } else {
	            R74cell8.setCellValue("");
	            R74cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(74);
	        Cell R75cell1 = row.createCell(2);
	        if (record.getR75_upTo1Month() != null) {
	            R75cell1.setCellValue(record.getR75_upTo1Month().doubleValue());
	            R75cell1.setCellStyle(numberStyle);
	        } else {
	            R75cell1.setCellValue("");
	            R75cell1.setCellStyle(textStyle);
	        }

	        Cell R75cell2 = row.createCell(3);
	        if (record.getR75_moreThan1MonthTo3Months() != null) {
	            R75cell2.setCellValue(record.getR75_moreThan1MonthTo3Months().doubleValue());
	            R75cell2.setCellStyle(numberStyle);
	        } else {
	            R75cell2.setCellValue("");
	            R75cell2.setCellStyle(textStyle);
	        }

	        Cell R75cell3 = row.createCell(4);
	        if (record.getR75_moreThan3MonthTo6Months() != null) {
	            R75cell3.setCellValue(record.getR75_moreThan3MonthTo6Months().doubleValue());
	            R75cell3.setCellStyle(numberStyle);
	        } else {
	            R75cell3.setCellValue("");
	            R75cell3.setCellStyle(textStyle);
	        }

	        Cell R75cell4 = row.createCell(5);
	        if (record.getR75_moreThan6MonthTo12Months() != null) {
	            R75cell4.setCellValue(record.getR75_moreThan6MonthTo12Months().doubleValue());
	            R75cell4.setCellStyle(numberStyle);
	        } else {
	            R75cell4.setCellValue("");
	            R75cell4.setCellStyle(textStyle);
	        }

	        Cell R75cell5 = row.createCell(6);
	        if (record.getR75_moreThan12MonthTo3Years() != null) {
	            R75cell5.setCellValue(record.getR75_moreThan12MonthTo3Years().doubleValue());
	            R75cell5.setCellStyle(numberStyle);
	        } else {
	            R75cell5.setCellValue("");
	            R75cell5.setCellStyle(textStyle);
	        }

	        Cell R75cell6 = row.createCell(7);
	        if (record.getR75_moreThan3YearsTo5Years() != null) {
	            R75cell6.setCellValue(record.getR75_moreThan3YearsTo5Years().doubleValue());
	            R75cell6.setCellStyle(numberStyle);
	        } else {
	            R75cell6.setCellValue("");
	            R75cell6.setCellStyle(textStyle);
	        }

	        Cell R75cell7 = row.createCell(8);
	        if (record.getR75_moreThan5YearsTo10Years() != null) {
	            R75cell7.setCellValue(record.getR75_moreThan5YearsTo10Years().doubleValue());
	            R75cell7.setCellStyle(numberStyle);
	        } else {
	            R75cell7.setCellValue("");
	            R75cell7.setCellStyle(textStyle);
	        }

	        Cell R75cell8 = row.createCell(9);
	        if (record.getR75_moreThan10Years() != null) {
	            R75cell8.setCellValue(record.getR75_moreThan10Years().doubleValue());
	            R75cell8.setCellStyle(numberStyle);
	        } else {
	            R75cell8.setCellValue("");
	            R75cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(76);
	        Cell R77cell1 = row.createCell(2);
	        if (record.getR77_upTo1Month() != null) {
	            R77cell1.setCellValue(record.getR77_upTo1Month().doubleValue());
	            R77cell1.setCellStyle(numberStyle);
	        } else {
	            R77cell1.setCellValue("");
	            R77cell1.setCellStyle(textStyle);
	        }

	        Cell R77cell2 = row.createCell(3);
	        if (record.getR77_moreThan1MonthTo3Months() != null) {
	            R77cell2.setCellValue(record.getR77_moreThan1MonthTo3Months().doubleValue());
	            R77cell2.setCellStyle(numberStyle);
	        } else {
	            R77cell2.setCellValue("");
	            R77cell2.setCellStyle(textStyle);
	        }

	        Cell R77cell3 = row.createCell(4);
	        if (record.getR77_moreThan3MonthTo6Months() != null) {
	            R77cell3.setCellValue(record.getR77_moreThan3MonthTo6Months().doubleValue());
	            R77cell3.setCellStyle(numberStyle);
	        } else {
	            R77cell3.setCellValue("");
	            R77cell3.setCellStyle(textStyle);
	        }

	        Cell R77cell4 = row.createCell(5);
	        if (record.getR77_moreThan6MonthTo12Months() != null) {
	            R77cell4.setCellValue(record.getR77_moreThan6MonthTo12Months().doubleValue());
	            R77cell4.setCellStyle(numberStyle);
	        } else {
	            R77cell4.setCellValue("");
	            R77cell4.setCellStyle(textStyle);
	        }

	        Cell R77cell5 = row.createCell(6);
	        if (record.getR77_moreThan12MonthTo3Years() != null) {
	            R77cell5.setCellValue(record.getR77_moreThan12MonthTo3Years().doubleValue());
	            R77cell5.setCellStyle(numberStyle);
	        } else {
	            R77cell5.setCellValue("");
	            R77cell5.setCellStyle(textStyle);
	        }

	        Cell R77cell6 = row.createCell(7);
	        if (record.getR77_moreThan3YearsTo5Years() != null) {
	            R77cell6.setCellValue(record.getR77_moreThan3YearsTo5Years().doubleValue());
	            R77cell6.setCellStyle(numberStyle);
	        } else {
	            R77cell6.setCellValue("");
	            R77cell6.setCellStyle(textStyle);
	        }

	        Cell R77cell7 = row.createCell(8);
	        if (record.getR77_moreThan5YearsTo10Years() != null) {
	            R77cell7.setCellValue(record.getR77_moreThan5YearsTo10Years().doubleValue());
	            R77cell7.setCellStyle(numberStyle);
	        } else {
	            R77cell7.setCellValue("");
	            R77cell7.setCellStyle(textStyle);
	        }

	        Cell R77cell8 = row.createCell(9);
	        if (record.getR77_moreThan10Years() != null) {
	            R77cell8.setCellValue(record.getR77_moreThan10Years().doubleValue());
	            R77cell8.setCellStyle(numberStyle);
	        } else {
	            R77cell8.setCellValue("");
	            R77cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(77);
	        Cell R78cell1 = row.createCell(2);
	        if (record.getR78_upTo1Month() != null) {
	            R78cell1.setCellValue(record.getR78_upTo1Month().doubleValue());
	            R78cell1.setCellStyle(numberStyle);
	        } else {
	            R78cell1.setCellValue("");
	            R78cell1.setCellStyle(textStyle);
	        }

	        Cell R78cell2 = row.createCell(3);
	        if (record.getR78_moreThan1MonthTo3Months() != null) {
	            R78cell2.setCellValue(record.getR78_moreThan1MonthTo3Months().doubleValue());
	            R78cell2.setCellStyle(numberStyle);
	        } else {
	            R78cell2.setCellValue("");
	            R78cell2.setCellStyle(textStyle);
	        }

	        Cell R78cell3 = row.createCell(4);
	        if (record.getR78_moreThan3MonthTo6Months() != null) {
	            R78cell3.setCellValue(record.getR78_moreThan3MonthTo6Months().doubleValue());
	            R78cell3.setCellStyle(numberStyle);
	        } else {
	            R78cell3.setCellValue("");
	            R78cell3.setCellStyle(textStyle);
	        }

	        Cell R78cell4 = row.createCell(5);
	        if (record.getR78_moreThan6MonthTo12Months() != null) {
	            R78cell4.setCellValue(record.getR78_moreThan6MonthTo12Months().doubleValue());
	            R78cell4.setCellStyle(numberStyle);
	        } else {
	            R78cell4.setCellValue("");
	            R78cell4.setCellStyle(textStyle);
	        }

	        Cell R78cell5 = row.createCell(6);
	        if (record.getR78_moreThan12MonthTo3Years() != null) {
	            R78cell5.setCellValue(record.getR78_moreThan12MonthTo3Years().doubleValue());
	            R78cell5.setCellStyle(numberStyle);
	        } else {
	            R78cell5.setCellValue("");
	            R78cell5.setCellStyle(textStyle);
	        }

	        Cell R78cell6 = row.createCell(7);
	        if (record.getR78_moreThan3YearsTo5Years() != null) {
	            R78cell6.setCellValue(record.getR78_moreThan3YearsTo5Years().doubleValue());
	            R78cell6.setCellStyle(numberStyle);
	        } else {
	            R78cell6.setCellValue("");
	            R78cell6.setCellStyle(textStyle);
	        }

	        Cell R78cell7 = row.createCell(8);
	        if (record.getR78_moreThan5YearsTo10Years() != null) {
	            R78cell7.setCellValue(record.getR78_moreThan5YearsTo10Years().doubleValue());
	            R78cell7.setCellStyle(numberStyle);
	        } else {
	            R78cell7.setCellValue("");
	            R78cell7.setCellStyle(textStyle);
	        }

	        Cell R78cell8 = row.createCell(9);
	        if (record.getR78_moreThan10Years() != null) {
	            R78cell8.setCellValue(record.getR78_moreThan10Years().doubleValue());
	            R78cell8.setCellStyle(numberStyle);
	        } else {
	            R78cell8.setCellValue("");
	            R78cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(78);
	        Cell R79cell1 = row.createCell(2);
	        if (record.getR79_upTo1Month() != null) {
	            R79cell1.setCellValue(record.getR79_upTo1Month().doubleValue());
	            R79cell1.setCellStyle(numberStyle);
	        } else {
	            R79cell1.setCellValue("");
	            R79cell1.setCellStyle(textStyle);
	        }

	        Cell R79cell2 = row.createCell(3);
	        if (record.getR79_moreThan1MonthTo3Months() != null) {
	            R79cell2.setCellValue(record.getR79_moreThan1MonthTo3Months().doubleValue());
	            R79cell2.setCellStyle(numberStyle);
	        } else {
	            R79cell2.setCellValue("");
	            R79cell2.setCellStyle(textStyle);
	        }

	        Cell R79cell3 = row.createCell(4);
	        if (record.getR79_moreThan3MonthTo6Months() != null) {
	            R79cell3.setCellValue(record.getR79_moreThan3MonthTo6Months().doubleValue());
	            R79cell3.setCellStyle(numberStyle);
	        } else {
	            R79cell3.setCellValue("");
	            R79cell3.setCellStyle(textStyle);
	        }

	        Cell R79cell4 = row.createCell(5);
	        if (record.getR79_moreThan6MonthTo12Months() != null) {
	            R79cell4.setCellValue(record.getR79_moreThan6MonthTo12Months().doubleValue());
	            R79cell4.setCellStyle(numberStyle);
	        } else {
	            R79cell4.setCellValue("");
	            R79cell4.setCellStyle(textStyle);
	        }

	        Cell R79cell5 = row.createCell(6);
	        if (record.getR79_moreThan12MonthTo3Years() != null) {
	            R79cell5.setCellValue(record.getR79_moreThan12MonthTo3Years().doubleValue());
	            R79cell5.setCellStyle(numberStyle);
	        } else {
	            R79cell5.setCellValue("");
	            R79cell5.setCellStyle(textStyle);
	        }

	        Cell R79cell6 = row.createCell(7);
	        if (record.getR79_moreThan3YearsTo5Years() != null) {
	            R79cell6.setCellValue(record.getR79_moreThan3YearsTo5Years().doubleValue());
	            R79cell6.setCellStyle(numberStyle);
	        } else {
	            R79cell6.setCellValue("");
	            R79cell6.setCellStyle(textStyle);
	        }

	        Cell R79cell7 = row.createCell(8);
	        if (record.getR79_moreThan5YearsTo10Years() != null) {
	            R79cell7.setCellValue(record.getR79_moreThan5YearsTo10Years().doubleValue());
	            R79cell7.setCellStyle(numberStyle);
	        } else {
	            R79cell7.setCellValue("");
	            R79cell7.setCellStyle(textStyle);
	        }

	        Cell R79cell8 = row.createCell(9);
	        if (record.getR79_moreThan10Years() != null) {
	            R79cell8.setCellValue(record.getR79_moreThan10Years().doubleValue());
	            R79cell8.setCellStyle(numberStyle);
	        } else {
	            R79cell8.setCellValue("");
	            R79cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(79);
	        Cell R80cell1 = row.createCell(2);
	        if (record.getR80_upTo1Month() != null) {
	            R80cell1.setCellValue(record.getR80_upTo1Month().doubleValue());
	            R80cell1.setCellStyle(numberStyle);
	        } else {
	            R80cell1.setCellValue("");
	            R80cell1.setCellStyle(textStyle);
	        }

	        Cell R80cell2 = row.createCell(3);
	        if (record.getR80_moreThan1MonthTo3Months() != null) {
	            R80cell2.setCellValue(record.getR80_moreThan1MonthTo3Months().doubleValue());
	            R80cell2.setCellStyle(numberStyle);
	        } else {
	            R80cell2.setCellValue("");
	            R80cell2.setCellStyle(textStyle);
	        }

	        Cell R80cell3 = row.createCell(4);
	        if (record.getR80_moreThan3MonthTo6Months() != null) {
	            R80cell3.setCellValue(record.getR80_moreThan3MonthTo6Months().doubleValue());
	            R80cell3.setCellStyle(numberStyle);
	        } else {
	            R80cell3.setCellValue("");
	            R80cell3.setCellStyle(textStyle);
	        }

	        Cell R80cell4 = row.createCell(5);
	        if (record.getR80_moreThan6MonthTo12Months() != null) {
	            R80cell4.setCellValue(record.getR80_moreThan6MonthTo12Months().doubleValue());
	            R80cell4.setCellStyle(numberStyle);
	        } else {
	            R80cell4.setCellValue("");
	            R80cell4.setCellStyle(textStyle);
	        }

	        Cell R80cell5 = row.createCell(6);
	        if (record.getR80_moreThan12MonthTo3Years() != null) {
	            R80cell5.setCellValue(record.getR80_moreThan12MonthTo3Years().doubleValue());
	            R80cell5.setCellStyle(numberStyle);
	        } else {
	            R80cell5.setCellValue("");
	            R80cell5.setCellStyle(textStyle);
	        }

	        Cell R80cell6 = row.createCell(7);
	        if (record.getR80_moreThan3YearsTo5Years() != null) {
	            R80cell6.setCellValue(record.getR80_moreThan3YearsTo5Years().doubleValue());
	            R80cell6.setCellStyle(numberStyle);
	        } else {
	            R80cell6.setCellValue("");
	            R80cell6.setCellStyle(textStyle);
	        }

	        Cell R80cell7 = row.createCell(8);
	        if (record.getR80_moreThan5YearsTo10Years() != null) {
	            R80cell7.setCellValue(record.getR80_moreThan5YearsTo10Years().doubleValue());
	            R80cell7.setCellStyle(numberStyle);
	        } else {
	            R80cell7.setCellValue("");
	            R80cell7.setCellStyle(textStyle);
	        }

	        Cell R80cell8 = row.createCell(9);
	        if (record.getR80_moreThan10Years() != null) {
	            R80cell8.setCellValue(record.getR80_moreThan10Years().doubleValue());
	            R80cell8.setCellStyle(numberStyle);
	        } else {
	            R80cell8.setCellValue("");
	            R80cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(80);
	        Cell R81cell1 = row.createCell(2);
	        if (record.getR81_upTo1Month() != null) {
	            R81cell1.setCellValue(record.getR81_upTo1Month().doubleValue());
	            R81cell1.setCellStyle(numberStyle);
	        } else {
	            R81cell1.setCellValue("");
	            R81cell1.setCellStyle(textStyle);
	        }

	        Cell R81cell2 = row.createCell(3);
	        if (record.getR81_moreThan1MonthTo3Months() != null) {
	            R81cell2.setCellValue(record.getR81_moreThan1MonthTo3Months().doubleValue());
	            R81cell2.setCellStyle(numberStyle);
	        } else {
	            R81cell2.setCellValue("");
	            R81cell2.setCellStyle(textStyle);
	        }

	        Cell R81cell3 = row.createCell(4);
	        if (record.getR81_moreThan3MonthTo6Months() != null) {
	            R81cell3.setCellValue(record.getR81_moreThan3MonthTo6Months().doubleValue());
	            R81cell3.setCellStyle(numberStyle);
	        } else {
	            R81cell3.setCellValue("");
	            R81cell3.setCellStyle(textStyle);
	        }

	        Cell R81cell4 = row.createCell(5);
	        if (record.getR81_moreThan6MonthTo12Months() != null) {
	            R81cell4.setCellValue(record.getR81_moreThan6MonthTo12Months().doubleValue());
	            R81cell4.setCellStyle(numberStyle);
	        } else {
	            R81cell4.setCellValue("");
	            R81cell4.setCellStyle(textStyle);
	        }

	        Cell R81cell5 = row.createCell(6);
	        if (record.getR81_moreThan12MonthTo3Years() != null) {
	            R81cell5.setCellValue(record.getR81_moreThan12MonthTo3Years().doubleValue());
	            R81cell5.setCellStyle(numberStyle);
	        } else {
	            R81cell5.setCellValue("");
	            R81cell5.setCellStyle(textStyle);
	        }

	        Cell R81cell6 = row.createCell(7);
	        if (record.getR81_moreThan3YearsTo5Years() != null) {
	            R81cell6.setCellValue(record.getR81_moreThan3YearsTo5Years().doubleValue());
	            R81cell6.setCellStyle(numberStyle);
	        } else {
	            R81cell6.setCellValue("");
	            R81cell6.setCellStyle(textStyle);
	        }

	        Cell R81cell7 = row.createCell(8);
	        if (record.getR81_moreThan5YearsTo10Years() != null) {
	            R81cell7.setCellValue(record.getR81_moreThan5YearsTo10Years().doubleValue());
	            R81cell7.setCellStyle(numberStyle);
	        } else {
	            R81cell7.setCellValue("");
	            R81cell7.setCellStyle(textStyle);
	        }

	        Cell R81cell8 = row.createCell(9);
	        if (record.getR81_moreThan10Years() != null) {
	            R81cell8.setCellValue(record.getR81_moreThan10Years().doubleValue());
	            R81cell8.setCellStyle(numberStyle);
	        } else {
	            R81cell8.setCellValue("");
	            R81cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(81);
	        Cell R82cell1 = row.createCell(2);
	        if (record.getR82_upTo1Month() != null) {
	            R82cell1.setCellValue(record.getR82_upTo1Month().doubleValue());
	            R82cell1.setCellStyle(numberStyle);
	        } else {
	            R82cell1.setCellValue("");
	            R82cell1.setCellStyle(textStyle);
	        }

	        Cell R82cell2 = row.createCell(3);
	        if (record.getR82_moreThan1MonthTo3Months() != null) {
	            R82cell2.setCellValue(record.getR82_moreThan1MonthTo3Months().doubleValue());
	            R82cell2.setCellStyle(numberStyle);
	        } else {
	            R82cell2.setCellValue("");
	            R82cell2.setCellStyle(textStyle);
	        }

	        Cell R82cell3 = row.createCell(4);
	        if (record.getR82_moreThan3MonthTo6Months() != null) {
	            R82cell3.setCellValue(record.getR82_moreThan3MonthTo6Months().doubleValue());
	            R82cell3.setCellStyle(numberStyle);
	        } else {
	            R82cell3.setCellValue("");
	            R82cell3.setCellStyle(textStyle);
	        }

	        Cell R82cell4 = row.createCell(5);
	        if (record.getR82_moreThan6MonthTo12Months() != null) {
	            R82cell4.setCellValue(record.getR82_moreThan6MonthTo12Months().doubleValue());
	            R82cell4.setCellStyle(numberStyle);
	        } else {
	            R82cell4.setCellValue("");
	            R82cell4.setCellStyle(textStyle);
	        }

	        Cell R82cell5 = row.createCell(6);
	        if (record.getR82_moreThan12MonthTo3Years() != null) {
	            R82cell5.setCellValue(record.getR82_moreThan12MonthTo3Years().doubleValue());
	            R82cell5.setCellStyle(numberStyle);
	        } else {
	            R82cell5.setCellValue("");
	            R82cell5.setCellStyle(textStyle);
	        }

	        Cell R82cell6 = row.createCell(7);
	        if (record.getR82_moreThan3YearsTo5Years() != null) {
	            R82cell6.setCellValue(record.getR82_moreThan3YearsTo5Years().doubleValue());
	            R82cell6.setCellStyle(numberStyle);
	        } else {
	            R82cell6.setCellValue("");
	            R82cell6.setCellStyle(textStyle);
	        }

	        Cell R82cell7 = row.createCell(8);
	        if (record.getR82_moreThan5YearsTo10Years() != null) {
	            R82cell7.setCellValue(record.getR82_moreThan5YearsTo10Years().doubleValue());
	            R82cell7.setCellStyle(numberStyle);
	        } else {
	            R82cell7.setCellValue("");
	            R82cell7.setCellStyle(textStyle);
	        }

	        Cell R82cell8 = row.createCell(9);
	        if (record.getR82_moreThan10Years() != null) {
	            R82cell8.setCellValue(record.getR82_moreThan10Years().doubleValue());
	            R82cell8.setCellStyle(numberStyle);
	        } else {
	            R82cell8.setCellValue("");
	            R82cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(82);
	        Cell R83cell1 = row.createCell(2);
	        if (record.getR83_upTo1Month() != null) {
	            R83cell1.setCellValue(record.getR83_upTo1Month().doubleValue());
	            R83cell1.setCellStyle(numberStyle);
	        } else {
	            R83cell1.setCellValue("");
	            R83cell1.setCellStyle(textStyle);
	        }

	        Cell R83cell2 = row.createCell(3);
	        if (record.getR83_moreThan1MonthTo3Months() != null) {
	            R83cell2.setCellValue(record.getR83_moreThan1MonthTo3Months().doubleValue());
	            R83cell2.setCellStyle(numberStyle);
	        } else {
	            R83cell2.setCellValue("");
	            R83cell2.setCellStyle(textStyle);
	        }

	        Cell R83cell3 = row.createCell(4);
	        if (record.getR83_moreThan3MonthTo6Months() != null) {
	            R83cell3.setCellValue(record.getR83_moreThan3MonthTo6Months().doubleValue());
	            R83cell3.setCellStyle(numberStyle);
	        } else {
	            R83cell3.setCellValue("");
	            R83cell3.setCellStyle(textStyle);
	        }

	        Cell R83cell4 = row.createCell(5);
	        if (record.getR83_moreThan6MonthTo12Months() != null) {
	            R83cell4.setCellValue(record.getR83_moreThan6MonthTo12Months().doubleValue());
	            R83cell4.setCellStyle(numberStyle);
	        } else {
	            R83cell4.setCellValue("");
	            R83cell4.setCellStyle(textStyle);
	        }

	        Cell R83cell5 = row.createCell(6);
	        if (record.getR83_moreThan12MonthTo3Years() != null) {
	            R83cell5.setCellValue(record.getR83_moreThan12MonthTo3Years().doubleValue());
	            R83cell5.setCellStyle(numberStyle);
	        } else {
	            R83cell5.setCellValue("");
	            R83cell5.setCellStyle(textStyle);
	        }

	        Cell R83cell6 = row.createCell(7);
	        if (record.getR83_moreThan3YearsTo5Years() != null) {
	            R83cell6.setCellValue(record.getR83_moreThan3YearsTo5Years().doubleValue());
	            R83cell6.setCellStyle(numberStyle);
	        } else {
	            R83cell6.setCellValue("");
	            R83cell6.setCellStyle(textStyle);
	        }

	        Cell R83cell7 = row.createCell(8);
	        if (record.getR83_moreThan5YearsTo10Years() != null) {
	            R83cell7.setCellValue(record.getR83_moreThan5YearsTo10Years().doubleValue());
	            R83cell7.setCellStyle(numberStyle);
	        } else {
	            R83cell7.setCellValue("");
	            R83cell7.setCellStyle(textStyle);
	        }

	        Cell R83cell8 = row.createCell(9);
	        if (record.getR83_moreThan10Years() != null) {
	            R83cell8.setCellValue(record.getR83_moreThan10Years().doubleValue());
	            R83cell8.setCellStyle(numberStyle);
	        } else {
	            R83cell8.setCellValue("");
	            R83cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(84);
	        Cell R85cell9 = row.createCell(10);
	        if (record.getR85_nonRatioSensativeItems() != null) {
	            R85cell9.setCellValue(record.getR85_nonRatioSensativeItems().doubleValue());
	            R85cell9.setCellStyle(numberStyle);
	        } else {
	            R85cell9.setCellValue("");
	            R85cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(85);
	        Cell R86cell9 = row.createCell(10);
	        if (record.getR86_nonRatioSensativeItems() != null) {
	            R86cell9.setCellValue(record.getR86_nonRatioSensativeItems().doubleValue());
	            R86cell9.setCellStyle(numberStyle);
	        } else {
	            R86cell9.setCellValue("");
	            R86cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(86);
	        Cell R87cell9 = row.createCell(10);
	        if (record.getR87_nonRatioSensativeItems() != null) {
	            R87cell9.setCellValue(record.getR87_nonRatioSensativeItems().doubleValue());
	            R87cell9.setCellStyle(numberStyle);
	        } else {
	            R87cell9.setCellValue("");
	            R87cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(87);
	        Cell R88cell9 = row.createCell(10);
	        if (record.getR88_nonRatioSensativeItems() != null) {
	            R88cell9.setCellValue(record.getR88_nonRatioSensativeItems().doubleValue());
	            R88cell9.setCellStyle(numberStyle);
	        } else {
	            R88cell9.setCellValue("");
	            R88cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(88);
	        Cell R89cell9 = row.createCell(10);
	        if (record.getR89_nonRatioSensativeItems() != null) {
	            R89cell9.setCellValue(record.getR89_nonRatioSensativeItems().doubleValue());
	            R89cell9.setCellStyle(numberStyle);
	        } else {
	            R89cell9.setCellValue("");
	            R89cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(89);
	        Cell R90cell9 = row.createCell(10);
	        if (record.getR90_nonRatioSensativeItems() != null) {
	            R90cell9.setCellValue(record.getR90_nonRatioSensativeItems().doubleValue());
	            R90cell9.setCellStyle(numberStyle);
	        } else {
	            R90cell9.setCellValue("");
	            R90cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(90);
	        Cell R91cell9 = row.createCell(10);
	        if (record.getR91_nonRatioSensativeItems() != null) {
	            R91cell9.setCellValue(record.getR91_nonRatioSensativeItems().doubleValue());
	            R91cell9.setCellStyle(numberStyle);
	        } else {
	            R91cell9.setCellValue("");
	            R91cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(95);
	        Cell R96cell1 = row.createCell(2);
	        if (record.getR96_upTo1Month() != null) {
	            R96cell1.setCellValue(record.getR96_upTo1Month().doubleValue());
	            R96cell1.setCellStyle(numberStyle);
	        } else {
	            R96cell1.setCellValue("");
	            R96cell1.setCellStyle(textStyle);
	        }

	        Cell R96cell2 = row.createCell(3);
	        if (record.getR96_moreThan1MonthTo3Months() != null) {
	            R96cell2.setCellValue(record.getR96_moreThan1MonthTo3Months().doubleValue());
	            R96cell2.setCellStyle(numberStyle);
	        } else {
	            R96cell2.setCellValue("");
	            R96cell2.setCellStyle(textStyle);
	        }

	        Cell R96cell3 = row.createCell(4);
	        if (record.getR96_moreThan3MonthTo6Months() != null) {
	            R96cell3.setCellValue(record.getR96_moreThan3MonthTo6Months().doubleValue());
	            R96cell3.setCellStyle(numberStyle);
	        } else {
	            R96cell3.setCellValue("");
	            R96cell3.setCellStyle(textStyle);
	        }

	        Cell R96cell4 = row.createCell(5);
	        if (record.getR96_moreThan6MonthTo12Months() != null) {
	            R96cell4.setCellValue(record.getR96_moreThan6MonthTo12Months().doubleValue());
	            R96cell4.setCellStyle(numberStyle);
	        } else {
	            R96cell4.setCellValue("");
	            R96cell4.setCellStyle(textStyle);
	        }

	        Cell R96cell5 = row.createCell(6);
	        if (record.getR96_moreThan12MonthTo3Years() != null) {
	            R96cell5.setCellValue(record.getR96_moreThan12MonthTo3Years().doubleValue());
	            R96cell5.setCellStyle(numberStyle);
	        } else {
	            R96cell5.setCellValue("");
	            R96cell5.setCellStyle(textStyle);
	        }

	        Cell R96cell6 = row.createCell(7);
	        if (record.getR96_moreThan3YearsTo5Years() != null) {
	            R96cell6.setCellValue(record.getR96_moreThan3YearsTo5Years().doubleValue());
	            R96cell6.setCellStyle(numberStyle);
	        } else {
	            R96cell6.setCellValue("");
	            R96cell6.setCellStyle(textStyle);
	        }

	        Cell R96cell7 = row.createCell(8);
	        if (record.getR96_moreThan5YearsTo10Years() != null) {
	            R96cell7.setCellValue(record.getR96_moreThan5YearsTo10Years().doubleValue());
	            R96cell7.setCellStyle(numberStyle);
	        } else {
	            R96cell7.setCellValue("");
	            R96cell7.setCellStyle(textStyle);
	        }

	        Cell R96cell8 = row.createCell(9);
	        if (record.getR96_moreThan10Years() != null) {
	            R96cell8.setCellValue(record.getR96_moreThan10Years().doubleValue());
	            R96cell8.setCellStyle(numberStyle);
	        } else {
	            R96cell8.setCellValue("");
	            R96cell8.setCellStyle(textStyle);
	        }

	        Cell R96cell9 = row.createCell(10);
	        if (record.getR96_nonRatioSensativeItems() != null) {
	            R96cell9.setCellValue(record.getR96_nonRatioSensativeItems().doubleValue());
	            R96cell9.setCellStyle(numberStyle);
	        } else {
	            R96cell9.setCellValue("");
	            R96cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(99);
	        Cell R100cell1 = row.createCell(2);
	        if (record.getR100_upTo1Month() != null) {
	            R100cell1.setCellValue(record.getR100_upTo1Month().doubleValue());
	            R100cell1.setCellStyle(numberStyle);
	        } else {
	            R100cell1.setCellValue("");
	            R100cell1.setCellStyle(textStyle);
	        }

	        Cell R100cell2 = row.createCell(3);
	        if (record.getR100_moreThan1MonthTo3Months() != null) {
	            R100cell2.setCellValue(record.getR100_moreThan1MonthTo3Months().doubleValue());
	            R100cell2.setCellStyle(numberStyle);
	        } else {
	            R100cell2.setCellValue("");
	            R100cell2.setCellStyle(textStyle);
	        }

	        Cell R100cell3 = row.createCell(4);
	        if (record.getR100_moreThan3MonthTo6Months() != null) {
	            R100cell3.setCellValue(record.getR100_moreThan3MonthTo6Months().doubleValue());
	            R100cell3.setCellStyle(numberStyle);
	        } else {
	            R100cell3.setCellValue("");
	            R100cell3.setCellStyle(textStyle);
	        }

	        Cell R100cell4 = row.createCell(5);
	        if (record.getR100_moreThan6MonthTo12Months() != null) {
	            R100cell4.setCellValue(record.getR100_moreThan6MonthTo12Months().doubleValue());
	            R100cell4.setCellStyle(numberStyle);
	        } else {
	            R100cell4.setCellValue("");
	            R100cell4.setCellStyle(textStyle);
	        }

	        Cell R100cell5 = row.createCell(6);
	        if (record.getR100_moreThan12MonthTo3Years() != null) {
	            R100cell5.setCellValue(record.getR100_moreThan12MonthTo3Years().doubleValue());
	            R100cell5.setCellStyle(numberStyle);
	        } else {
	            R100cell5.setCellValue("");
	            R100cell5.setCellStyle(textStyle);
	        }

	        Cell R100cell6 = row.createCell(7);
	        if (record.getR100_moreThan3YearsTo5Years() != null) {
	            R100cell6.setCellValue(record.getR100_moreThan3YearsTo5Years().doubleValue());
	            R100cell6.setCellStyle(numberStyle);
	        } else {
	            R100cell6.setCellValue("");
	            R100cell6.setCellStyle(textStyle);
	        }

	        Cell R100cell7 = row.createCell(8);
	        if (record.getR100_moreThan5YearsTo10Years() != null) {
	            R100cell7.setCellValue(record.getR100_moreThan5YearsTo10Years().doubleValue());
	            R100cell7.setCellStyle(numberStyle);
	        } else {
	            R100cell7.setCellValue("");
	            R100cell7.setCellStyle(textStyle);
	        }

	        Cell R100cell8 = row.createCell(9);
	        if (record.getR100_moreThan10Years() != null) {
	            R100cell8.setCellValue(record.getR100_moreThan10Years().doubleValue());
	            R100cell8.setCellStyle(numberStyle);
	        } else {
	            R100cell8.setCellValue("");
	            R100cell8.setCellStyle(textStyle);
	        }

	        Cell R100cell9 = row.createCell(10);
	        if (record.getR100_nonRatioSensativeItems() != null) {
	            R100cell9.setCellValue(record.getR100_nonRatioSensativeItems().doubleValue());
	            R100cell9.setCellStyle(numberStyle);
	        } else {
	            R100cell9.setCellValue("");
	            R100cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(100);
	        Cell R101cell1 = row.createCell(2);
	        if (record.getR101_upTo1Month() != null) {
	            R101cell1.setCellValue(record.getR101_upTo1Month().doubleValue());
	            R101cell1.setCellStyle(numberStyle);
	        } else {
	            R101cell1.setCellValue("");
	            R101cell1.setCellStyle(textStyle);
	        }

	        Cell R101cell2 = row.createCell(3);
	        if (record.getR101_moreThan1MonthTo3Months() != null) {
	            R101cell2.setCellValue(record.getR101_moreThan1MonthTo3Months().doubleValue());
	            R101cell2.setCellStyle(numberStyle);
	        } else {
	            R101cell2.setCellValue("");
	            R101cell2.setCellStyle(textStyle);
	        }

	        Cell R101cell3 = row.createCell(4);
	        if (record.getR101_moreThan3MonthTo6Months() != null) {
	            R101cell3.setCellValue(record.getR101_moreThan3MonthTo6Months().doubleValue());
	            R101cell3.setCellStyle(numberStyle);
	        } else {
	            R101cell3.setCellValue("");
	            R101cell3.setCellStyle(textStyle);
	        }

	        Cell R101cell4 = row.createCell(5);
	        if (record.getR101_moreThan6MonthTo12Months() != null) {
	            R101cell4.setCellValue(record.getR101_moreThan6MonthTo12Months().doubleValue());
	            R101cell4.setCellStyle(numberStyle);
	        } else {
	            R101cell4.setCellValue("");
	            R101cell4.setCellStyle(textStyle);
	        }

	        Cell R101cell5 = row.createCell(6);
	        if (record.getR101_moreThan12MonthTo3Years() != null) {
	            R101cell5.setCellValue(record.getR101_moreThan12MonthTo3Years().doubleValue());
	            R101cell5.setCellStyle(numberStyle);
	        } else {
	            R101cell5.setCellValue("");
	            R101cell5.setCellStyle(textStyle);
	        }

	        Cell R101cell6 = row.createCell(7);
	        if (record.getR101_moreThan3YearsTo5Years() != null) {
	            R101cell6.setCellValue(record.getR101_moreThan3YearsTo5Years().doubleValue());
	            R101cell6.setCellStyle(numberStyle);
	        } else {
	            R101cell6.setCellValue("");
	            R101cell6.setCellStyle(textStyle);
	        }

	        Cell R101cell7 = row.createCell(8);
	        if (record.getR101_moreThan5YearsTo10Years() != null) {
	            R101cell7.setCellValue(record.getR101_moreThan5YearsTo10Years().doubleValue());
	            R101cell7.setCellStyle(numberStyle);
	        } else {
	            R101cell7.setCellValue("");
	            R101cell7.setCellStyle(textStyle);
	        }

	        Cell R101cell8 = row.createCell(9);
	        if (record.getR101_moreThan10Years() != null) {
	            R101cell8.setCellValue(record.getR101_moreThan10Years().doubleValue());
	            R101cell8.setCellStyle(numberStyle);
	        } else {
	            R101cell8.setCellValue("");
	            R101cell8.setCellStyle(textStyle);
	        }

	        Cell R101cell9 = row.createCell(10);
	        if (record.getR101_nonRatioSensativeItems() != null) {
	            R101cell9.setCellValue(record.getR101_nonRatioSensativeItems().doubleValue());
	            R101cell9.setCellStyle(numberStyle);
	        } else {
	            R101cell9.setCellValue("");
	            R101cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(101);
	        Cell R102cell1 = row.createCell(2);
	        if (record.getR102_upTo1Month() != null) {
	            R102cell1.setCellValue(record.getR102_upTo1Month().doubleValue());
	            R102cell1.setCellStyle(numberStyle);
	        } else {
	            R102cell1.setCellValue("");
	            R102cell1.setCellStyle(textStyle);
	        }

	        Cell R102cell2 = row.createCell(3);
	        if (record.getR102_moreThan1MonthTo3Months() != null) {
	            R102cell2.setCellValue(record.getR102_moreThan1MonthTo3Months().doubleValue());
	            R102cell2.setCellStyle(numberStyle);
	        } else {
	            R102cell2.setCellValue("");
	            R102cell2.setCellStyle(textStyle);
	        }

	        Cell R102cell3 = row.createCell(4);
	        if (record.getR102_moreThan3MonthTo6Months() != null) {
	            R102cell3.setCellValue(record.getR102_moreThan3MonthTo6Months().doubleValue());
	            R102cell3.setCellStyle(numberStyle);
	        } else {
	            R102cell3.setCellValue("");
	            R102cell3.setCellStyle(textStyle);
	        }

	        Cell R102cell4 = row.createCell(5);
	        if (record.getR102_moreThan6MonthTo12Months() != null) {
	            R102cell4.setCellValue(record.getR102_moreThan6MonthTo12Months().doubleValue());
	            R102cell4.setCellStyle(numberStyle);
	        } else {
	            R102cell4.setCellValue("");
	            R102cell4.setCellStyle(textStyle);
	        }

	        Cell R102cell5 = row.createCell(6);
	        if (record.getR102_moreThan12MonthTo3Years() != null) {
	            R102cell5.setCellValue(record.getR102_moreThan12MonthTo3Years().doubleValue());
	            R102cell5.setCellStyle(numberStyle);
	        } else {
	            R102cell5.setCellValue("");
	            R102cell5.setCellStyle(textStyle);
	        }

	        Cell R102cell6 = row.createCell(7);
	        if (record.getR102_moreThan3YearsTo5Years() != null) {
	            R102cell6.setCellValue(record.getR102_moreThan3YearsTo5Years().doubleValue());
	            R102cell6.setCellStyle(numberStyle);
	        } else {
	            R102cell6.setCellValue("");
	            R102cell6.setCellStyle(textStyle);
	        }

	        Cell R102cell7 = row.createCell(8);
	        if (record.getR102_moreThan5YearsTo10Years() != null) {
	            R102cell7.setCellValue(record.getR102_moreThan5YearsTo10Years().doubleValue());
	            R102cell7.setCellStyle(numberStyle);
	        } else {
	            R102cell7.setCellValue("");
	            R102cell7.setCellStyle(textStyle);
	        }

	        Cell R102cell8 = row.createCell(9);
	        if (record.getR102_moreThan10Years() != null) {
	            R102cell8.setCellValue(record.getR102_moreThan10Years().doubleValue());
	            R102cell8.setCellStyle(numberStyle);
	        } else {
	            R102cell8.setCellValue("");
	            R102cell8.setCellStyle(textStyle);
	        }

	        Cell R102cell9 = row.createCell(10);
	        if (record.getR102_nonRatioSensativeItems() != null) {
	            R102cell9.setCellValue(record.getR102_nonRatioSensativeItems().doubleValue());
	            R102cell9.setCellStyle(numberStyle);
	        } else {
	            R102cell9.setCellValue("");
	            R102cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(102);
	        Cell R103cell1 = row.createCell(2);
	        if (record.getR103_upTo1Month() != null) {
	            R103cell1.setCellValue(record.getR103_upTo1Month().doubleValue());
	            R103cell1.setCellStyle(numberStyle);
	        } else {
	            R103cell1.setCellValue("");
	            R103cell1.setCellStyle(textStyle);
	        }

	        Cell R103cell2 = row.createCell(3);
	        if (record.getR103_moreThan1MonthTo3Months() != null) {
	            R103cell2.setCellValue(record.getR103_moreThan1MonthTo3Months().doubleValue());
	            R103cell2.setCellStyle(numberStyle);
	        } else {
	            R103cell2.setCellValue("");
	            R103cell2.setCellStyle(textStyle);
	        }

	        Cell R103cell3 = row.createCell(4);
	        if (record.getR103_moreThan3MonthTo6Months() != null) {
	            R103cell3.setCellValue(record.getR103_moreThan3MonthTo6Months().doubleValue());
	            R103cell3.setCellStyle(numberStyle);
	        } else {
	            R103cell3.setCellValue("");
	            R103cell3.setCellStyle(textStyle);
	        }

	        Cell R103cell4 = row.createCell(5);
	        if (record.getR103_moreThan6MonthTo12Months() != null) {
	            R103cell4.setCellValue(record.getR103_moreThan6MonthTo12Months().doubleValue());
	            R103cell4.setCellStyle(numberStyle);
	        } else {
	            R103cell4.setCellValue("");
	            R103cell4.setCellStyle(textStyle);
	        }

	        Cell R103cell5 = row.createCell(6);
	        if (record.getR103_moreThan12MonthTo3Years() != null) {
	            R103cell5.setCellValue(record.getR103_moreThan12MonthTo3Years().doubleValue());
	            R103cell5.setCellStyle(numberStyle);
	        } else {
	            R103cell5.setCellValue("");
	            R103cell5.setCellStyle(textStyle);
	        }

	        Cell R103cell6 = row.createCell(7);
	        if (record.getR103_moreThan3YearsTo5Years() != null) {
	            R103cell6.setCellValue(record.getR103_moreThan3YearsTo5Years().doubleValue());
	            R103cell6.setCellStyle(numberStyle);
	        } else {
	            R103cell6.setCellValue("");
	            R103cell6.setCellStyle(textStyle);
	        }

	        Cell R103cell7 = row.createCell(8);
	        if (record.getR103_moreThan5YearsTo10Years() != null) {
	            R103cell7.setCellValue(record.getR103_moreThan5YearsTo10Years().doubleValue());
	            R103cell7.setCellStyle(numberStyle);
	        } else {
	            R103cell7.setCellValue("");
	            R103cell7.setCellStyle(textStyle);
	        }

	        Cell R103cell8 = row.createCell(9);
	        if (record.getR103_moreThan10Years() != null) {
	            R103cell8.setCellValue(record.getR103_moreThan10Years().doubleValue());
	            R103cell8.setCellStyle(numberStyle);
	        } else {
	            R103cell8.setCellValue("");
	            R103cell8.setCellStyle(textStyle);
	        }

	        Cell R103cell9 = row.createCell(10);
	        if (record.getR103_nonRatioSensativeItems() != null) {
	            R103cell9.setCellValue(record.getR103_nonRatioSensativeItems().doubleValue());
	            R103cell9.setCellStyle(numberStyle);
	        } else {
	            R103cell9.setCellValue("");
	            R103cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(103);
	        Cell R104cell1 = row.createCell(2);
	        if (record.getR104_upTo1Month() != null) {
	            R104cell1.setCellValue(record.getR104_upTo1Month().doubleValue());
	            R104cell1.setCellStyle(numberStyle);
	        } else {
	            R104cell1.setCellValue("");
	            R104cell1.setCellStyle(textStyle);
	        }

	        Cell R104cell2 = row.createCell(3);
	        if (record.getR104_moreThan1MonthTo3Months() != null) {
	            R104cell2.setCellValue(record.getR104_moreThan1MonthTo3Months().doubleValue());
	            R104cell2.setCellStyle(numberStyle);
	        } else {
	            R104cell2.setCellValue("");
	            R104cell2.setCellStyle(textStyle);
	        }

	        Cell R104cell3 = row.createCell(4);
	        if (record.getR104_moreThan3MonthTo6Months() != null) {
	            R104cell3.setCellValue(record.getR104_moreThan3MonthTo6Months().doubleValue());
	            R104cell3.setCellStyle(numberStyle);
	        } else {
	            R104cell3.setCellValue("");
	            R104cell3.setCellStyle(textStyle);
	        }

	        Cell R104cell4 = row.createCell(5);
	        if (record.getR104_moreThan6MonthTo12Months() != null) {
	            R104cell4.setCellValue(record.getR104_moreThan6MonthTo12Months().doubleValue());
	            R104cell4.setCellStyle(numberStyle);
	        } else {
	            R104cell4.setCellValue("");
	            R104cell4.setCellStyle(textStyle);
	        }

	        Cell R104cell5 = row.createCell(6);
	        if (record.getR104_moreThan12MonthTo3Years() != null) {
	            R104cell5.setCellValue(record.getR104_moreThan12MonthTo3Years().doubleValue());
	            R104cell5.setCellStyle(numberStyle);
	        } else {
	            R104cell5.setCellValue("");
	            R104cell5.setCellStyle(textStyle);
	        }

	        Cell R104cell6 = row.createCell(7);
	        if (record.getR104_moreThan3YearsTo5Years() != null) {
	            R104cell6.setCellValue(record.getR104_moreThan3YearsTo5Years().doubleValue());
	            R104cell6.setCellStyle(numberStyle);
	        } else {
	            R104cell6.setCellValue("");
	            R104cell6.setCellStyle(textStyle);
	        }

	        Cell R104cell7 = row.createCell(8);
	        if (record.getR104_moreThan5YearsTo10Years() != null) {
	            R104cell7.setCellValue(record.getR104_moreThan5YearsTo10Years().doubleValue());
	            R104cell7.setCellStyle(numberStyle);
	        } else {
	            R104cell7.setCellValue("");
	            R104cell7.setCellStyle(textStyle);
	        }

	        Cell R104cell8 = row.createCell(9);
	        if (record.getR104_moreThan10Years() != null) {
	            R104cell8.setCellValue(record.getR104_moreThan10Years().doubleValue());
	            R104cell8.setCellStyle(numberStyle);
	        } else {
	            R104cell8.setCellValue("");
	            R104cell8.setCellStyle(textStyle);
	        }

	        Cell R104cell9 = row.createCell(10);
	        if (record.getR104_nonRatioSensativeItems() != null) {
	            R104cell9.setCellValue(record.getR104_nonRatioSensativeItems().doubleValue());
	            R104cell9.setCellStyle(numberStyle);
	        } else {
	            R104cell9.setCellValue("");
	            R104cell9.setCellStyle(textStyle);
	        }

	   
	   }

   //ARCHIVAL FROMAT EXCEL
	public byte[] getExcelM_IRBARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {

		}
		List<M_IRB_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_PI report. Returning empty result.");
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

//Create the font
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8); // size 8
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
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
			int startRow = 7;

			if (!dataList.isEmpty()) {
				populateArchivalEntity1Data(sheet, todate, dataList.get(0), textStyle, numberStyle);
			}else {

			}

//Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_IRB ARCHIVAL SUMMARY", null, "BRRS_M_IRB_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	//ARCHIVAL FORMAT VALUES
	private void populateArchivalEntity1Data(Sheet sheet, String todate, M_IRB_Archival_Summary_Entity record, CellStyle textStyle, CellStyle numberStyle) {


		   /* ==========================================================
		    * REPORT DATE
		    * Set todate in C6
		    * ========================================================== */

		   try {

		       // Row 5 = Excel row 6
		       Row dateRow = sheet.getRow(5);

		       if (dateRow == null) {
		           dateRow = sheet.createRow(5);
		       }

		       // Column 2 = Excel column C
		       Cell dateCell = dateRow.getCell(2);

		       if (dateCell == null) {
		           dateCell = dateRow.createCell(2);
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
	       Row row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
	       Cell R12cell1 = row.createCell(2);
	       if (record.getR12_upTo1Month() != null) {
	           R12cell1.setCellValue(record.getR12_upTo1Month().doubleValue());
	           R12cell1.setCellStyle(numberStyle);
	       } else {
	           R12cell1.setCellValue("");
	           R12cell1.setCellStyle(textStyle);
	       }

	       Cell R12cell2 = row.createCell(3);
	       if (record.getR12_moreThan1MonthTo3Months() != null) {
	           R12cell2.setCellValue(record.getR12_moreThan1MonthTo3Months().doubleValue());
	           R12cell2.setCellStyle(numberStyle);
	       } else {
	           R12cell2.setCellValue("");
	           R12cell2.setCellStyle(textStyle);
	       }

	       Cell R12cell3 = row.createCell(4);
	       if (record.getR12_moreThan3MonthTo6Months() != null) {
	           R12cell3.setCellValue(record.getR12_moreThan3MonthTo6Months().doubleValue());
	           R12cell3.setCellStyle(numberStyle);
	       } else {
	           R12cell3.setCellValue("");
	           R12cell3.setCellStyle(textStyle);
	       }

	       Cell R12cell4 = row.createCell(5);
	       if (record.getR12_moreThan6MonthTo12Months() != null) {
	           R12cell4.setCellValue(record.getR12_moreThan6MonthTo12Months().doubleValue());
	           R12cell4.setCellStyle(numberStyle);
	       } else {
	           R12cell4.setCellValue("");
	           R12cell4.setCellStyle(textStyle);
	       }

	       Cell R12cell5 = row.createCell(6);
	       if (record.getR12_moreThan12MonthTo3Years() != null) {
	           R12cell5.setCellValue(record.getR12_moreThan12MonthTo3Years().doubleValue());
	           R12cell5.setCellStyle(numberStyle);
	       } else {
	           R12cell5.setCellValue("");
	           R12cell5.setCellStyle(textStyle);
	       }

	       Cell R12cell6 = row.createCell(7);
	       if (record.getR12_moreThan3YearsTo5Years() != null) {
	           R12cell6.setCellValue(record.getR12_moreThan3YearsTo5Years().doubleValue());
	           R12cell6.setCellStyle(numberStyle);
	       } else {
	           R12cell6.setCellValue("");
	           R12cell6.setCellStyle(textStyle);
	       }

	       Cell R12cell7 = row.createCell(8);
	       if (record.getR12_moreThan5YearsTo10Years() != null) {
	           R12cell7.setCellValue(record.getR12_moreThan5YearsTo10Years().doubleValue());
	           R12cell7.setCellStyle(numberStyle);
	       } else {
	           R12cell7.setCellValue("");
	           R12cell7.setCellStyle(textStyle);
	       }

	       Cell R12cell8 = row.createCell(9);
	       if (record.getR12_moreThan10Years() != null) {
	           R12cell8.setCellValue(record.getR12_moreThan10Years().doubleValue());
	           R12cell8.setCellStyle(numberStyle);
	       } else {
	           R12cell8.setCellValue("");
	           R12cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(12);
	        Cell R13cell1 = row.createCell(2);
	       if (record.getR13_upTo1Month() != null) {
	           R13cell1.setCellValue(record.getR13_upTo1Month().doubleValue());
	           R13cell1.setCellStyle(numberStyle);
	       } else {
	           R13cell1.setCellValue("");
	           R13cell1.setCellStyle(textStyle);
	       }

	       Cell R13cell2 = row.createCell(3);
	       if (record.getR13_moreThan1MonthTo3Months() != null) {
	           R13cell2.setCellValue(record.getR13_moreThan1MonthTo3Months().doubleValue());
	           R13cell2.setCellStyle(numberStyle);
	       } else {
	           R13cell2.setCellValue("");
	           R13cell2.setCellStyle(textStyle);
	       }

	       Cell R13cell3 = row.createCell(4);
	       if (record.getR13_moreThan3MonthTo6Months() != null) {
	           R13cell3.setCellValue(record.getR13_moreThan3MonthTo6Months().doubleValue());
	           R13cell3.setCellStyle(numberStyle);
	       } else {
	           R13cell3.setCellValue("");
	           R13cell3.setCellStyle(textStyle);
	       }

	       Cell R13cell4 = row.createCell(5);
	       if (record.getR13_moreThan6MonthTo12Months() != null) {
	           R13cell4.setCellValue(record.getR13_moreThan6MonthTo12Months().doubleValue());
	           R13cell4.setCellStyle(numberStyle);
	       } else {
	           R13cell4.setCellValue("");
	           R13cell4.setCellStyle(textStyle);
	       }

	       Cell R13cell5 = row.createCell(6);
	       if (record.getR13_moreThan12MonthTo3Years() != null) {
	           R13cell5.setCellValue(record.getR13_moreThan12MonthTo3Years().doubleValue());
	           R13cell5.setCellStyle(numberStyle);
	       } else {
	           R13cell5.setCellValue("");
	           R13cell5.setCellStyle(textStyle);
	       }

	       Cell R13cell6 = row.createCell(7);
	       if (record.getR13_moreThan3YearsTo5Years() != null) {
	           R13cell6.setCellValue(record.getR13_moreThan3YearsTo5Years().doubleValue());
	           R13cell6.setCellStyle(numberStyle);
	       } else {
	           R13cell6.setCellValue("");
	           R13cell6.setCellStyle(textStyle);
	       }

	       Cell R13cell7 = row.createCell(8);
	       if (record.getR13_moreThan5YearsTo10Years() != null) {
	           R13cell7.setCellValue(record.getR13_moreThan5YearsTo10Years().doubleValue());
	           R13cell7.setCellStyle(numberStyle);
	       } else {
	           R13cell7.setCellValue("");
	           R13cell7.setCellStyle(textStyle);
	       }

	       Cell R13cell8 = row.createCell(9);
	       if (record.getR13_moreThan10Years() != null) {
	           R13cell8.setCellValue(record.getR13_moreThan10Years().doubleValue());
	           R13cell8.setCellStyle(numberStyle);
	       } else {
	           R13cell8.setCellValue("");
	           R13cell8.setCellStyle(textStyle);
	       }


	       row = sheet.getRow(13);
	       Cell R14cell1 = row.createCell(2);
	       if (record.getR14_upTo1Month() != null) {
	           R14cell1.setCellValue(record.getR14_upTo1Month().doubleValue());
	           R14cell1.setCellStyle(numberStyle);
	       } else {
	           R14cell1.setCellValue("");
	           R14cell1.setCellStyle(textStyle);
	       }

	       Cell R14cell2 = row.createCell(3);
	       if (record.getR14_moreThan1MonthTo3Months() != null) {
	           R14cell2.setCellValue(record.getR14_moreThan1MonthTo3Months().doubleValue());
	           R14cell2.setCellStyle(numberStyle);
	       } else {
	           R14cell2.setCellValue("");
	           R14cell2.setCellStyle(textStyle);
	       }

	       Cell R14cell3 = row.createCell(4);
	       if (record.getR14_moreThan3MonthTo6Months() != null) {
	           R14cell3.setCellValue(record.getR14_moreThan3MonthTo6Months().doubleValue());
	           R14cell3.setCellStyle(numberStyle);
	       } else {
	           R14cell3.setCellValue("");
	           R14cell3.setCellStyle(textStyle);
	       }

	       Cell R14cell4 = row.createCell(5);
	       if (record.getR14_moreThan6MonthTo12Months() != null) {
	           R14cell4.setCellValue(record.getR14_moreThan6MonthTo12Months().doubleValue());
	           R14cell4.setCellStyle(numberStyle);
	       } else {
	           R14cell4.setCellValue("");
	           R14cell4.setCellStyle(textStyle);
	       }

	       Cell R14cell5 = row.createCell(6);
	       if (record.getR14_moreThan12MonthTo3Years() != null) {
	           R14cell5.setCellValue(record.getR14_moreThan12MonthTo3Years().doubleValue());
	           R14cell5.setCellStyle(numberStyle);
	       } else {
	           R14cell5.setCellValue("");
	           R14cell5.setCellStyle(textStyle);
	       }

	       Cell R14cell6 = row.createCell(7);
	       if (record.getR14_moreThan3YearsTo5Years() != null) {
	           R14cell6.setCellValue(record.getR14_moreThan3YearsTo5Years().doubleValue());
	           R14cell6.setCellStyle(numberStyle);
	       } else {
	           R14cell6.setCellValue("");
	           R14cell6.setCellStyle(textStyle);
	       }

	       Cell R14cell7 = row.createCell(8);
	       if (record.getR14_moreThan5YearsTo10Years() != null) {
	           R14cell7.setCellValue(record.getR14_moreThan5YearsTo10Years().doubleValue());
	           R14cell7.setCellStyle(numberStyle);
	       } else {
	           R14cell7.setCellValue("");
	           R14cell7.setCellStyle(textStyle);
	       }

	       Cell R14cell8 = row.createCell(9);
	       if (record.getR14_moreThan10Years() != null) {
	           R14cell8.setCellValue(record.getR14_moreThan10Years().doubleValue());
	           R14cell8.setCellStyle(numberStyle);
	       } else {
	           R14cell8.setCellValue("");
	           R14cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(14);
	       Cell R15cell1 = row.createCell(2);
	       if (record.getR15_upTo1Month() != null) {
	           R15cell1.setCellValue(record.getR15_upTo1Month().doubleValue());
	           R15cell1.setCellStyle(numberStyle);
	       } else {
	           R15cell1.setCellValue("");
	           R15cell1.setCellStyle(textStyle);
	       }

	       Cell R15cell2 = row.createCell(3);
	       if (record.getR15_moreThan1MonthTo3Months() != null) {
	           R15cell2.setCellValue(record.getR15_moreThan1MonthTo3Months().doubleValue());
	           R15cell2.setCellStyle(numberStyle);
	       } else {
	           R15cell2.setCellValue("");
	           R15cell2.setCellStyle(textStyle);
	       }

	       Cell R15cell3 = row.createCell(4);
	       if (record.getR15_moreThan3MonthTo6Months() != null) {
	           R15cell3.setCellValue(record.getR15_moreThan3MonthTo6Months().doubleValue());
	           R15cell3.setCellStyle(numberStyle);
	       } else {
	           R15cell3.setCellValue("");
	           R15cell3.setCellStyle(textStyle);
	       }

	       Cell R15cell4 = row.createCell(5);
	       if (record.getR15_moreThan6MonthTo12Months() != null) {
	           R15cell4.setCellValue(record.getR15_moreThan6MonthTo12Months().doubleValue());
	           R15cell4.setCellStyle(numberStyle);
	       } else {
	           R15cell4.setCellValue("");
	           R15cell4.setCellStyle(textStyle);
	       }

	       Cell R15cell5 = row.createCell(6);
	       if (record.getR15_moreThan12MonthTo3Years() != null) {
	           R15cell5.setCellValue(record.getR15_moreThan12MonthTo3Years().doubleValue());
	           R15cell5.setCellStyle(numberStyle);
	       } else {
	           R15cell5.setCellValue("");
	           R15cell5.setCellStyle(textStyle);
	       }

	       Cell R15cell6 = row.createCell(7);
	       if (record.getR15_moreThan3YearsTo5Years() != null) {
	           R15cell6.setCellValue(record.getR15_moreThan3YearsTo5Years().doubleValue());
	           R15cell6.setCellStyle(numberStyle);
	       } else {
	           R15cell6.setCellValue("");
	           R15cell6.setCellStyle(textStyle);
	       }

	       Cell R15cell7 = row.createCell(8);
	       if (record.getR15_moreThan5YearsTo10Years() != null) {
	           R15cell7.setCellValue(record.getR15_moreThan5YearsTo10Years().doubleValue());
	           R15cell7.setCellStyle(numberStyle);
	       } else {
	           R15cell7.setCellValue("");
	           R15cell7.setCellStyle(textStyle);
	       }

	       Cell R15cell8 = row.createCell(9);
	       if (record.getR15_moreThan10Years() != null) {
	           R15cell8.setCellValue(record.getR15_moreThan10Years().doubleValue());
	           R15cell8.setCellStyle(numberStyle);
	       } else {
	           R15cell8.setCellValue("");
	           R15cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(15);
	       Cell R16cell1 = row.createCell(2);
	       if (record.getR16_upTo1Month() != null) {
	           R16cell1.setCellValue(record.getR16_upTo1Month().doubleValue());
	           R16cell1.setCellStyle(numberStyle);
	       } else {
	           R16cell1.setCellValue("");
	           R16cell1.setCellStyle(textStyle);
	       }

	       Cell R16cell2 = row.createCell(3);
	       if (record.getR16_moreThan1MonthTo3Months() != null) {
	           R16cell2.setCellValue(record.getR16_moreThan1MonthTo3Months().doubleValue());
	           R16cell2.setCellStyle(numberStyle);
	       } else {
	           R16cell2.setCellValue("");
	           R16cell2.setCellStyle(textStyle);
	       }

	       Cell R16cell3 = row.createCell(4);
	       if (record.getR16_moreThan3MonthTo6Months() != null) {
	           R16cell3.setCellValue(record.getR16_moreThan3MonthTo6Months().doubleValue());
	           R16cell3.setCellStyle(numberStyle);
	       } else {
	           R16cell3.setCellValue("");
	           R16cell3.setCellStyle(textStyle);
	       }

	       Cell R16cell4 = row.createCell(5);
	       if (record.getR16_moreThan6MonthTo12Months() != null) {
	           R16cell4.setCellValue(record.getR16_moreThan6MonthTo12Months().doubleValue());
	           R16cell4.setCellStyle(numberStyle);
	       } else {
	           R16cell4.setCellValue("");
	           R16cell4.setCellStyle(textStyle);
	       }

	       Cell R16cell5 = row.createCell(6);
	       if (record.getR16_moreThan12MonthTo3Years() != null) {
	           R16cell5.setCellValue(record.getR16_moreThan12MonthTo3Years().doubleValue());
	           R16cell5.setCellStyle(numberStyle);
	       } else {
	           R16cell5.setCellValue("");
	           R16cell5.setCellStyle(textStyle);
	       }

	       Cell R16cell6 = row.createCell(7);
	       if (record.getR16_moreThan3YearsTo5Years() != null) {
	           R16cell6.setCellValue(record.getR16_moreThan3YearsTo5Years().doubleValue());
	           R16cell6.setCellStyle(numberStyle);
	       } else {
	           R16cell6.setCellValue("");
	           R16cell6.setCellStyle(textStyle);
	       }

	       Cell R16cell7 = row.createCell(8);
	       if (record.getR16_moreThan5YearsTo10Years() != null) {
	           R16cell7.setCellValue(record.getR16_moreThan5YearsTo10Years().doubleValue());
	           R16cell7.setCellStyle(numberStyle);
	       } else {
	           R16cell7.setCellValue("");
	           R16cell7.setCellStyle(textStyle);
	       }

	       Cell R16cell8 = row.createCell(9);
	       if (record.getR16_moreThan10Years() != null) {
	           R16cell8.setCellValue(record.getR16_moreThan10Years().doubleValue());
	           R16cell8.setCellStyle(numberStyle);
	       } else {
	           R16cell8.setCellValue("");
	           R16cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(16);
	       Cell R17cell1 = row.createCell(2);
	       if (record.getR17_upTo1Month() != null) {
	           R17cell1.setCellValue(record.getR17_upTo1Month().doubleValue());
	           R17cell1.setCellStyle(numberStyle);
	       } else {
	           R17cell1.setCellValue("");
	           R17cell1.setCellStyle(textStyle);
	       }

	       Cell R17cell2 = row.createCell(3);
	       if (record.getR17_moreThan1MonthTo3Months() != null) {
	           R17cell2.setCellValue(record.getR17_moreThan1MonthTo3Months().doubleValue());
	           R17cell2.setCellStyle(numberStyle);
	       } else {
	           R17cell2.setCellValue("");
	           R17cell2.setCellStyle(textStyle);
	       }

	       Cell R17cell3 = row.createCell(4);
	       if (record.getR17_moreThan3MonthTo6Months() != null) {
	           R17cell3.setCellValue(record.getR17_moreThan3MonthTo6Months().doubleValue());
	           R17cell3.setCellStyle(numberStyle);
	       } else {
	           R17cell3.setCellValue("");
	           R17cell3.setCellStyle(textStyle);
	       }

	       Cell R17cell4 = row.createCell(5);
	       if (record.getR17_moreThan6MonthTo12Months() != null) {
	           R17cell4.setCellValue(record.getR17_moreThan6MonthTo12Months().doubleValue());
	           R17cell4.setCellStyle(numberStyle);
	       } else {
	           R17cell4.setCellValue("");
	           R17cell4.setCellStyle(textStyle);
	       }

	       Cell R17cell5 = row.createCell(6);
	       if (record.getR17_moreThan12MonthTo3Years() != null) {
	           R17cell5.setCellValue(record.getR17_moreThan12MonthTo3Years().doubleValue());
	           R17cell5.setCellStyle(numberStyle);
	       } else {
	           R17cell5.setCellValue("");
	           R17cell5.setCellStyle(textStyle);
	       }

	       Cell R17cell6 = row.createCell(7);
	       if (record.getR17_moreThan3YearsTo5Years() != null) {
	           R17cell6.setCellValue(record.getR17_moreThan3YearsTo5Years().doubleValue());
	           R17cell6.setCellStyle(numberStyle);
	       } else {
	           R17cell6.setCellValue("");
	           R17cell6.setCellStyle(textStyle);
	       }

	       Cell R17cell7 = row.createCell(8);
	       if (record.getR17_moreThan5YearsTo10Years() != null) {
	           R17cell7.setCellValue(record.getR17_moreThan5YearsTo10Years().doubleValue());
	           R17cell7.setCellStyle(numberStyle);
	       } else {
	           R17cell7.setCellValue("");
	           R17cell7.setCellStyle(textStyle);
	       }

	       Cell R17cell8 = row.createCell(9);
	       if (record.getR17_moreThan10Years() != null) {
	           R17cell8.setCellValue(record.getR17_moreThan10Years().doubleValue());
	           R17cell8.setCellStyle(numberStyle);
	       } else {
	           R17cell8.setCellValue("");
	           R17cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(17);
	       Cell R18cell1 = row.createCell(2);
	       if (record.getR18_upTo1Month() != null) {
	           R18cell1.setCellValue(record.getR18_upTo1Month().doubleValue());
	           R18cell1.setCellStyle(numberStyle);
	       } else {
	           R18cell1.setCellValue("");
	           R18cell1.setCellStyle(textStyle);
	       }

	       Cell R18cell2 = row.createCell(3);
	       if (record.getR18_moreThan1MonthTo3Months() != null) {
	           R18cell2.setCellValue(record.getR18_moreThan1MonthTo3Months().doubleValue());
	           R18cell2.setCellStyle(numberStyle);
	       } else {
	           R18cell2.setCellValue("");
	           R18cell2.setCellStyle(textStyle);
	       }

	       Cell R18cell3 = row.createCell(4);
	       if (record.getR18_moreThan3MonthTo6Months() != null) {
	           R18cell3.setCellValue(record.getR18_moreThan3MonthTo6Months().doubleValue());
	           R18cell3.setCellStyle(numberStyle);
	       } else {
	           R18cell3.setCellValue("");
	           R18cell3.setCellStyle(textStyle);
	       }

	       Cell R18cell4 = row.createCell(5);
	       if (record.getR18_moreThan6MonthTo12Months() != null) {
	           R18cell4.setCellValue(record.getR18_moreThan6MonthTo12Months().doubleValue());
	           R18cell4.setCellStyle(numberStyle);
	       } else {
	           R18cell4.setCellValue("");
	           R18cell4.setCellStyle(textStyle);
	       }

	       Cell R18cell5 = row.createCell(6);
	       if (record.getR18_moreThan12MonthTo3Years() != null) {
	           R18cell5.setCellValue(record.getR18_moreThan12MonthTo3Years().doubleValue());
	           R18cell5.setCellStyle(numberStyle);
	       } else {
	           R18cell5.setCellValue("");
	           R18cell5.setCellStyle(textStyle);
	       }

	       Cell R18cell6 = row.createCell(7);
	       if (record.getR18_moreThan3YearsTo5Years() != null) {
	           R18cell6.setCellValue(record.getR18_moreThan3YearsTo5Years().doubleValue());
	           R18cell6.setCellStyle(numberStyle);
	       } else {
	           R18cell6.setCellValue("");
	           R18cell6.setCellStyle(textStyle);
	       }

	       Cell R18cell7 = row.createCell(8);
	       if (record.getR18_moreThan5YearsTo10Years() != null) {
	           R18cell7.setCellValue(record.getR18_moreThan5YearsTo10Years().doubleValue());
	           R18cell7.setCellStyle(numberStyle);
	       } else {
	           R18cell7.setCellValue("");
	           R18cell7.setCellStyle(textStyle);
	       }

	       Cell R18cell8 = row.createCell(9);
	       if (record.getR18_moreThan10Years() != null) {
	           R18cell8.setCellValue(record.getR18_moreThan10Years().doubleValue());
	           R18cell8.setCellStyle(numberStyle);
	       } else {
	           R18cell8.setCellValue("");
	           R18cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(18);
	       Cell R19cell1 = row.createCell(2);
	       if (record.getR19_upTo1Month() != null) {
	           R19cell1.setCellValue(record.getR19_upTo1Month().doubleValue());
	           R19cell1.setCellStyle(numberStyle);
	       } else {
	           R19cell1.setCellValue("");
	           R19cell1.setCellStyle(textStyle);
	       }

	       Cell R19cell2 = row.createCell(3);
	       if (record.getR19_moreThan1MonthTo3Months() != null) {
	           R19cell2.setCellValue(record.getR19_moreThan1MonthTo3Months().doubleValue());
	           R19cell2.setCellStyle(numberStyle);
	       } else {
	           R19cell2.setCellValue("");
	           R19cell2.setCellStyle(textStyle);
	       }

	       Cell R19cell3 = row.createCell(4);
	       if (record.getR19_moreThan3MonthTo6Months() != null) {
	           R19cell3.setCellValue(record.getR19_moreThan3MonthTo6Months().doubleValue());
	           R19cell3.setCellStyle(numberStyle);
	       } else {
	           R19cell3.setCellValue("");
	           R19cell3.setCellStyle(textStyle);
	       }

	       Cell R19cell4 = row.createCell(5);
	       if (record.getR19_moreThan6MonthTo12Months() != null) {
	           R19cell4.setCellValue(record.getR19_moreThan6MonthTo12Months().doubleValue());
	           R19cell4.setCellStyle(numberStyle);
	       } else {
	           R19cell4.setCellValue("");
	           R19cell4.setCellStyle(textStyle);
	       }

	       Cell R19cell5 = row.createCell(6);
	       if (record.getR19_moreThan12MonthTo3Years() != null) {
	           R19cell5.setCellValue(record.getR19_moreThan12MonthTo3Years().doubleValue());
	           R19cell5.setCellStyle(numberStyle);
	       } else {
	           R19cell5.setCellValue("");
	           R19cell5.setCellStyle(textStyle);
	       }

	       Cell R19cell6 = row.createCell(7);
	       if (record.getR19_moreThan3YearsTo5Years() != null) {
	           R19cell6.setCellValue(record.getR19_moreThan3YearsTo5Years().doubleValue());
	           R19cell6.setCellStyle(numberStyle);
	       } else {
	           R19cell6.setCellValue("");
	           R19cell6.setCellStyle(textStyle);
	       }

	       Cell R19cell7 = row.createCell(8);
	       if (record.getR19_moreThan5YearsTo10Years() != null) {
	           R19cell7.setCellValue(record.getR19_moreThan5YearsTo10Years().doubleValue());
	           R19cell7.setCellStyle(numberStyle);
	       } else {
	           R19cell7.setCellValue("");
	           R19cell7.setCellStyle(textStyle);
	       }

	       Cell R19cell8 = row.createCell(9);
	       if (record.getR19_moreThan10Years() != null) {
	           R19cell8.setCellValue(record.getR19_moreThan10Years().doubleValue());
	           R19cell8.setCellStyle(numberStyle);
	       } else {
	           R19cell8.setCellValue("");
	           R19cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(19);
	       Cell R20cell1 = row.createCell(2);
	       if (record.getR20_upTo1Month() != null) {
	           R20cell1.setCellValue(record.getR20_upTo1Month().doubleValue());
	           R20cell1.setCellStyle(numberStyle);
	       } else {
	           R20cell1.setCellValue("");
	           R20cell1.setCellStyle(textStyle);
	       }

	       Cell R20cell2 = row.createCell(3);
	       if (record.getR20_moreThan1MonthTo3Months() != null) {
	           R20cell2.setCellValue(record.getR20_moreThan1MonthTo3Months().doubleValue());
	           R20cell2.setCellStyle(numberStyle);
	       } else {
	           R20cell2.setCellValue("");
	           R20cell2.setCellStyle(textStyle);
	       }

	       Cell R20cell3 = row.createCell(4);
	       if (record.getR20_moreThan3MonthTo6Months() != null) {
	           R20cell3.setCellValue(record.getR20_moreThan3MonthTo6Months().doubleValue());
	           R20cell3.setCellStyle(numberStyle);
	       } else {
	           R20cell3.setCellValue("");
	           R20cell3.setCellStyle(textStyle);
	       }

	       Cell R20cell4 = row.createCell(5);
	       if (record.getR20_moreThan6MonthTo12Months() != null) {
	           R20cell4.setCellValue(record.getR20_moreThan6MonthTo12Months().doubleValue());
	           R20cell4.setCellStyle(numberStyle);
	       } else {
	           R20cell4.setCellValue("");
	           R20cell4.setCellStyle(textStyle);
	       }

	       Cell R20cell5 = row.createCell(6);
	       if (record.getR20_moreThan12MonthTo3Years() != null) {
	           R20cell5.setCellValue(record.getR20_moreThan12MonthTo3Years().doubleValue());
	           R20cell5.setCellStyle(numberStyle);
	       } else {
	           R20cell5.setCellValue("");
	           R20cell5.setCellStyle(textStyle);
	       }

	       Cell R20cell6 = row.createCell(7);
	       if (record.getR20_moreThan3YearsTo5Years() != null) {
	           R20cell6.setCellValue(record.getR20_moreThan3YearsTo5Years().doubleValue());
	           R20cell6.setCellStyle(numberStyle);
	       } else {
	           R20cell6.setCellValue("");
	           R20cell6.setCellStyle(textStyle);
	       }

	       Cell R20cell7 = row.createCell(8);
	       if (record.getR20_moreThan5YearsTo10Years() != null) {
	           R20cell7.setCellValue(record.getR20_moreThan5YearsTo10Years().doubleValue());
	           R20cell7.setCellStyle(numberStyle);
	       } else {
	           R20cell7.setCellValue("");
	           R20cell7.setCellStyle(textStyle);
	       }

	       Cell R20cell8 = row.createCell(9);
	       if (record.getR20_moreThan10Years() != null) {
	           R20cell8.setCellValue(record.getR20_moreThan10Years().doubleValue());
	           R20cell8.setCellStyle(numberStyle);
	       } else {
	           R20cell8.setCellValue("");
	           R20cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(20);
	       Cell R21cell1 = row.createCell(2);
	       if (record.getR21_upTo1Month() != null) {
	           R21cell1.setCellValue(record.getR21_upTo1Month().doubleValue());
	           R21cell1.setCellStyle(numberStyle);
	       } else {
	           R21cell1.setCellValue("");
	           R21cell1.setCellStyle(textStyle);
	       }

	       Cell R21cell2 = row.createCell(3);
	       if (record.getR21_moreThan1MonthTo3Months() != null) {
	           R21cell2.setCellValue(record.getR21_moreThan1MonthTo3Months().doubleValue());
	           R21cell2.setCellStyle(numberStyle);
	       } else {
	           R21cell2.setCellValue("");
	           R21cell2.setCellStyle(textStyle);
	       }

	       Cell R21cell3 = row.createCell(4);
	       if (record.getR21_moreThan3MonthTo6Months() != null) {
	           R21cell3.setCellValue(record.getR21_moreThan3MonthTo6Months().doubleValue());
	           R21cell3.setCellStyle(numberStyle);
	       } else {
	           R21cell3.setCellValue("");
	           R21cell3.setCellStyle(textStyle);
	       }

	       Cell R21cell4 = row.createCell(5);
	       if (record.getR21_moreThan6MonthTo12Months() != null) {
	           R21cell4.setCellValue(record.getR21_moreThan6MonthTo12Months().doubleValue());
	           R21cell4.setCellStyle(numberStyle);
	       } else {
	           R21cell4.setCellValue("");
	           R21cell4.setCellStyle(textStyle);
	       }

	       Cell R21cell5 = row.createCell(6);
	       if (record.getR21_moreThan12MonthTo3Years() != null) {
	           R21cell5.setCellValue(record.getR21_moreThan12MonthTo3Years().doubleValue());
	           R21cell5.setCellStyle(numberStyle);
	       } else {
	           R21cell5.setCellValue("");
	           R21cell5.setCellStyle(textStyle);
	       }

	       Cell R21cell6 = row.createCell(7);
	       if (record.getR21_moreThan3YearsTo5Years() != null) {
	           R21cell6.setCellValue(record.getR21_moreThan3YearsTo5Years().doubleValue());
	           R21cell6.setCellStyle(numberStyle);
	       } else {
	           R21cell6.setCellValue("");
	           R21cell6.setCellStyle(textStyle);
	       }

	       Cell R21cell7 = row.createCell(8);
	       if (record.getR21_moreThan5YearsTo10Years() != null) {
	           R21cell7.setCellValue(record.getR21_moreThan5YearsTo10Years().doubleValue());
	           R21cell7.setCellStyle(numberStyle);
	       } else {
	           R21cell7.setCellValue("");
	           R21cell7.setCellStyle(textStyle);
	       }

	       Cell R21cell8 = row.createCell(9);
	       if (record.getR21_moreThan10Years() != null) {
	           R21cell8.setCellValue(record.getR21_moreThan10Years().doubleValue());
	           R21cell8.setCellStyle(numberStyle);
	       } else {
	           R21cell8.setCellValue("");
	           R21cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(21);
	       Cell R22cell1 = row.createCell(2);
	       if (record.getR22_upTo1Month() != null) {
	           R22cell1.setCellValue(record.getR22_upTo1Month().doubleValue());
	           R22cell1.setCellStyle(numberStyle);
	       } else {
	           R22cell1.setCellValue("");
	           R22cell1.setCellStyle(textStyle);
	       }

	       Cell R22cell2 = row.createCell(3);
	       if (record.getR22_moreThan1MonthTo3Months() != null) {
	           R22cell2.setCellValue(record.getR22_moreThan1MonthTo3Months().doubleValue());
	           R22cell2.setCellStyle(numberStyle);
	       } else {
	           R22cell2.setCellValue("");
	           R22cell2.setCellStyle(textStyle);
	       }

	       Cell R22cell3 = row.createCell(4);
	       if (record.getR22_moreThan3MonthTo6Months() != null) {
	           R22cell3.setCellValue(record.getR22_moreThan3MonthTo6Months().doubleValue());
	           R22cell3.setCellStyle(numberStyle);
	       } else {
	           R22cell3.setCellValue("");
	           R22cell3.setCellStyle(textStyle);
	       }

	       Cell R22cell4 = row.createCell(5);
	       if (record.getR22_moreThan6MonthTo12Months() != null) {
	           R22cell4.setCellValue(record.getR22_moreThan6MonthTo12Months().doubleValue());
	           R22cell4.setCellStyle(numberStyle);
	       } else {
	           R22cell4.setCellValue("");
	           R22cell4.setCellStyle(textStyle);
	       }

	       Cell R22cell5 = row.createCell(6);
	       if (record.getR22_moreThan12MonthTo3Years() != null) {
	           R22cell5.setCellValue(record.getR22_moreThan12MonthTo3Years().doubleValue());
	           R22cell5.setCellStyle(numberStyle);
	       } else {
	           R22cell5.setCellValue("");
	           R22cell5.setCellStyle(textStyle);
	       }

	       Cell R22cell6 = row.createCell(7);
	       if (record.getR22_moreThan3YearsTo5Years() != null) {
	           R22cell6.setCellValue(record.getR22_moreThan3YearsTo5Years().doubleValue());
	           R22cell6.setCellStyle(numberStyle);
	       } else {
	           R22cell6.setCellValue("");
	           R22cell6.setCellStyle(textStyle);
	       }

	       Cell R22cell7 = row.createCell(8);
	       if (record.getR22_moreThan5YearsTo10Years() != null) {
	           R22cell7.setCellValue(record.getR22_moreThan5YearsTo10Years().doubleValue());
	           R22cell7.setCellStyle(numberStyle);
	       } else {
	           R22cell7.setCellValue("");
	           R22cell7.setCellStyle(textStyle);
	       }

	       Cell R22cell8 = row.createCell(9);
	       if (record.getR22_moreThan10Years() != null) {
	           R22cell8.setCellValue(record.getR22_moreThan10Years().doubleValue());
	           R22cell8.setCellStyle(numberStyle);
	       } else {
	           R22cell8.setCellValue("");
	           R22cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(23);
	       Cell R24cell1 = row.createCell(2);
	       if (record.getR24_upTo1Month() != null) {
	           R24cell1.setCellValue(record.getR24_upTo1Month().doubleValue());
	           R24cell1.setCellStyle(numberStyle);
	       } else {
	           R24cell1.setCellValue("");
	           R24cell1.setCellStyle(textStyle);
	       }

	       Cell R24cell2 = row.createCell(3);
	       if (record.getR24_moreThan1MonthTo3Months() != null) {
	           R24cell2.setCellValue(record.getR24_moreThan1MonthTo3Months().doubleValue());
	           R24cell2.setCellStyle(numberStyle);
	       } else {
	           R24cell2.setCellValue("");
	           R24cell2.setCellStyle(textStyle);
	       }

	       Cell R24cell3 = row.createCell(4);
	       if (record.getR24_moreThan3MonthTo6Months() != null) {
	           R24cell3.setCellValue(record.getR24_moreThan3MonthTo6Months().doubleValue());
	           R24cell3.setCellStyle(numberStyle);
	       } else {
	           R24cell3.setCellValue("");
	           R24cell3.setCellStyle(textStyle);
	       }

	       Cell R24cell4 = row.createCell(5);
	       if (record.getR24_moreThan6MonthTo12Months() != null) {
	           R24cell4.setCellValue(record.getR24_moreThan6MonthTo12Months().doubleValue());
	           R24cell4.setCellStyle(numberStyle);
	       } else {
	           R24cell4.setCellValue("");
	           R24cell4.setCellStyle(textStyle);
	       }

	       Cell R24cell5 = row.createCell(6);
	       if (record.getR24_moreThan12MonthTo3Years() != null) {
	           R24cell5.setCellValue(record.getR24_moreThan12MonthTo3Years().doubleValue());
	           R24cell5.setCellStyle(numberStyle);
	       } else {
	           R24cell5.setCellValue("");
	           R24cell5.setCellStyle(textStyle);
	       }

	       Cell R24cell6 = row.createCell(7);
	       if (record.getR24_moreThan3YearsTo5Years() != null) {
	           R24cell6.setCellValue(record.getR24_moreThan3YearsTo5Years().doubleValue());
	           R24cell6.setCellStyle(numberStyle);
	       } else {
	           R24cell6.setCellValue("");
	           R24cell6.setCellStyle(textStyle);
	       }

	       Cell R24cell7 = row.createCell(8);
	       if (record.getR24_moreThan5YearsTo10Years() != null) {
	           R24cell7.setCellValue(record.getR24_moreThan5YearsTo10Years().doubleValue());
	           R24cell7.setCellStyle(numberStyle);
	       } else {
	           R24cell7.setCellValue("");
	           R24cell7.setCellStyle(textStyle);
	       }

	       Cell R24cell8 = row.createCell(9);
	       if (record.getR24_moreThan10Years() != null) {
	           R24cell8.setCellValue(record.getR24_moreThan10Years().doubleValue());
	           R24cell8.setCellStyle(numberStyle);
	       } else {
	           R24cell8.setCellValue("");
	           R24cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(24);
	       Cell R25cell1 = row.createCell(2);
	       if (record.getR25_upTo1Month() != null) {
	           R25cell1.setCellValue(record.getR25_upTo1Month().doubleValue());
	           R25cell1.setCellStyle(numberStyle);
	       } else {
	           R25cell1.setCellValue("");
	           R25cell1.setCellStyle(textStyle);
	       }

	       Cell R25cell2 = row.createCell(3);
	       if (record.getR25_moreThan1MonthTo3Months() != null) {
	           R25cell2.setCellValue(record.getR25_moreThan1MonthTo3Months().doubleValue());
	           R25cell2.setCellStyle(numberStyle);
	       } else {
	           R25cell2.setCellValue("");
	           R25cell2.setCellStyle(textStyle);
	       }

	       Cell R25cell3 = row.createCell(4);
	       if (record.getR25_moreThan3MonthTo6Months() != null) {
	           R25cell3.setCellValue(record.getR25_moreThan3MonthTo6Months().doubleValue());
	           R25cell3.setCellStyle(numberStyle);
	       } else {
	           R25cell3.setCellValue("");
	           R25cell3.setCellStyle(textStyle);
	       }

	       Cell R25cell4 = row.createCell(5);
	       if (record.getR25_moreThan6MonthTo12Months() != null) {
	           R25cell4.setCellValue(record.getR25_moreThan6MonthTo12Months().doubleValue());
	           R25cell4.setCellStyle(numberStyle);
	       } else {
	           R25cell4.setCellValue("");
	           R25cell4.setCellStyle(textStyle);
	       }

	       Cell R25cell5 = row.createCell(6);
	       if (record.getR25_moreThan12MonthTo3Years() != null) {
	           R25cell5.setCellValue(record.getR25_moreThan12MonthTo3Years().doubleValue());
	           R25cell5.setCellStyle(numberStyle);
	       } else {
	           R25cell5.setCellValue("");
	           R25cell5.setCellStyle(textStyle);
	       }

	       Cell R25cell6 = row.createCell(7);
	       if (record.getR25_moreThan3YearsTo5Years() != null) {
	           R25cell6.setCellValue(record.getR25_moreThan3YearsTo5Years().doubleValue());
	           R25cell6.setCellStyle(numberStyle);
	       } else {
	           R25cell6.setCellValue("");
	           R25cell6.setCellStyle(textStyle);
	       }

	       Cell R25cell7 = row.createCell(8);
	       if (record.getR25_moreThan5YearsTo10Years() != null) {
	           R25cell7.setCellValue(record.getR25_moreThan5YearsTo10Years().doubleValue());
	           R25cell7.setCellStyle(numberStyle);
	       } else {
	           R25cell7.setCellValue("");
	           R25cell7.setCellStyle(textStyle);
	       }

	       Cell R25cell8 = row.createCell(9);
	       if (record.getR25_moreThan10Years() != null) {
	           R25cell8.setCellValue(record.getR25_moreThan10Years().doubleValue());
	           R25cell8.setCellStyle(numberStyle);
	       } else {
	           R25cell8.setCellValue("");
	           R25cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(25);
	       Cell R26cell1 = row.createCell(2);
	       if (record.getR26_upTo1Month() != null) {
	           R26cell1.setCellValue(record.getR26_upTo1Month().doubleValue());
	           R26cell1.setCellStyle(numberStyle);
	       } else {
	           R26cell1.setCellValue("");
	           R26cell1.setCellStyle(textStyle);
	       }

	       Cell R26cell2 = row.createCell(3);
	       if (record.getR26_moreThan1MonthTo3Months() != null) {
	           R26cell2.setCellValue(record.getR26_moreThan1MonthTo3Months().doubleValue());
	           R26cell2.setCellStyle(numberStyle);
	       } else {
	           R26cell2.setCellValue("");
	           R26cell2.setCellStyle(textStyle);
	       }

	       Cell R26cell3 = row.createCell(4);
	       if (record.getR26_moreThan3MonthTo6Months() != null) {
	           R26cell3.setCellValue(record.getR26_moreThan3MonthTo6Months().doubleValue());
	           R26cell3.setCellStyle(numberStyle);
	       } else {
	           R26cell3.setCellValue("");
	           R26cell3.setCellStyle(textStyle);
	       }

	       Cell R26cell4 = row.createCell(5);
	       if (record.getR26_moreThan6MonthTo12Months() != null) {
	           R26cell4.setCellValue(record.getR26_moreThan6MonthTo12Months().doubleValue());
	           R26cell4.setCellStyle(numberStyle);
	       } else {
	           R26cell4.setCellValue("");
	           R26cell4.setCellStyle(textStyle);
	       }

	       Cell R26cell5 = row.createCell(6);
	       if (record.getR26_moreThan12MonthTo3Years() != null) {
	           R26cell5.setCellValue(record.getR26_moreThan12MonthTo3Years().doubleValue());
	           R26cell5.setCellStyle(numberStyle);
	       } else {
	           R26cell5.setCellValue("");
	           R26cell5.setCellStyle(textStyle);
	       }

	       Cell R26cell6 = row.createCell(7);
	       if (record.getR26_moreThan3YearsTo5Years() != null) {
	           R26cell6.setCellValue(record.getR26_moreThan3YearsTo5Years().doubleValue());
	           R26cell6.setCellStyle(numberStyle);
	       } else {
	           R26cell6.setCellValue("");
	           R26cell6.setCellStyle(textStyle);
	       }

	       Cell R26cell7 = row.createCell(8);
	       if (record.getR26_moreThan5YearsTo10Years() != null) {
	           R26cell7.setCellValue(record.getR26_moreThan5YearsTo10Years().doubleValue());
	           R26cell7.setCellStyle(numberStyle);
	       } else {
	           R26cell7.setCellValue("");
	           R26cell7.setCellStyle(textStyle);
	       }

	       Cell R26cell8 = row.createCell(9);
	       if (record.getR26_moreThan10Years() != null) {
	           R26cell8.setCellValue(record.getR26_moreThan10Years().doubleValue());
	           R26cell8.setCellStyle(numberStyle);
	       } else {
	           R26cell8.setCellValue("");
	           R26cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(26);
	       Cell R27cell1 = row.createCell(2);
	       if (record.getR27_upTo1Month() != null) {
	           R27cell1.setCellValue(record.getR27_upTo1Month().doubleValue());
	           R27cell1.setCellStyle(numberStyle);
	       } else {
	           R27cell1.setCellValue("");
	           R27cell1.setCellStyle(textStyle);
	       }

	       Cell R27cell2 = row.createCell(3);
	       if (record.getR27_moreThan1MonthTo3Months() != null) {
	           R27cell2.setCellValue(record.getR27_moreThan1MonthTo3Months().doubleValue());
	           R27cell2.setCellStyle(numberStyle);
	       } else {
	           R27cell2.setCellValue("");
	           R27cell2.setCellStyle(textStyle);
	       }

	       Cell R27cell3 = row.createCell(4);
	       if (record.getR27_moreThan3MonthTo6Months() != null) {
	           R27cell3.setCellValue(record.getR27_moreThan3MonthTo6Months().doubleValue());
	           R27cell3.setCellStyle(numberStyle);
	       } else {
	           R27cell3.setCellValue("");
	           R27cell3.setCellStyle(textStyle);
	       }

	       Cell R27cell4 = row.createCell(5);
	       if (record.getR27_moreThan6MonthTo12Months() != null) {
	           R27cell4.setCellValue(record.getR27_moreThan6MonthTo12Months().doubleValue());
	           R27cell4.setCellStyle(numberStyle);
	       } else {
	           R27cell4.setCellValue("");
	           R27cell4.setCellStyle(textStyle);
	       }

	       Cell R27cell5 = row.createCell(6);
	       if (record.getR27_moreThan12MonthTo3Years() != null) {
	           R27cell5.setCellValue(record.getR27_moreThan12MonthTo3Years().doubleValue());
	           R27cell5.setCellStyle(numberStyle);
	       } else {
	           R27cell5.setCellValue("");
	           R27cell5.setCellStyle(textStyle);
	       }

	       Cell R27cell6 = row.createCell(7);
	       if (record.getR27_moreThan3YearsTo5Years() != null) {
	           R27cell6.setCellValue(record.getR27_moreThan3YearsTo5Years().doubleValue());
	           R27cell6.setCellStyle(numberStyle);
	       } else {
	           R27cell6.setCellValue("");
	           R27cell6.setCellStyle(textStyle);
	       }

	       Cell R27cell7 = row.createCell(8);
	       if (record.getR27_moreThan5YearsTo10Years() != null) {
	           R27cell7.setCellValue(record.getR27_moreThan5YearsTo10Years().doubleValue());
	           R27cell7.setCellStyle(numberStyle);
	       } else {
	           R27cell7.setCellValue("");
	           R27cell7.setCellStyle(textStyle);
	       }

	       Cell R27cell8 = row.createCell(9);
	       if (record.getR27_moreThan10Years() != null) {
	           R27cell8.setCellValue(record.getR27_moreThan10Years().doubleValue());
	           R27cell8.setCellStyle(numberStyle);
	       } else {
	           R27cell8.setCellValue("");
	           R27cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(27);
	       Cell R28cell1 = row.createCell(2);
	       if (record.getR28_upTo1Month() != null) {
	           R28cell1.setCellValue(record.getR28_upTo1Month().doubleValue());
	           R28cell1.setCellStyle(numberStyle);
	       } else {
	           R28cell1.setCellValue("");
	           R28cell1.setCellStyle(textStyle);
	       }

	       Cell R28cell2 = row.createCell(3);
	       if (record.getR28_moreThan1MonthTo3Months() != null) {
	           R28cell2.setCellValue(record.getR28_moreThan1MonthTo3Months().doubleValue());
	           R28cell2.setCellStyle(numberStyle);
	       } else {
	           R28cell2.setCellValue("");
	           R28cell2.setCellStyle(textStyle);
	       }

	       Cell R28cell3 = row.createCell(4);
	       if (record.getR28_moreThan3MonthTo6Months() != null) {
	           R28cell3.setCellValue(record.getR28_moreThan3MonthTo6Months().doubleValue());
	           R28cell3.setCellStyle(numberStyle);
	       } else {
	           R28cell3.setCellValue("");
	           R28cell3.setCellStyle(textStyle);
	       }

	       Cell R28cell4 = row.createCell(5);
	       if (record.getR28_moreThan6MonthTo12Months() != null) {
	           R28cell4.setCellValue(record.getR28_moreThan6MonthTo12Months().doubleValue());
	           R28cell4.setCellStyle(numberStyle);
	       } else {
	           R28cell4.setCellValue("");
	           R28cell4.setCellStyle(textStyle);
	       }

	       Cell R28cell5 = row.createCell(6);
	       if (record.getR28_moreThan12MonthTo3Years() != null) {
	           R28cell5.setCellValue(record.getR28_moreThan12MonthTo3Years().doubleValue());
	           R28cell5.setCellStyle(numberStyle);
	       } else {
	           R28cell5.setCellValue("");
	           R28cell5.setCellStyle(textStyle);
	       }

	       Cell R28cell6 = row.createCell(7);
	       if (record.getR28_moreThan3YearsTo5Years() != null) {
	           R28cell6.setCellValue(record.getR28_moreThan3YearsTo5Years().doubleValue());
	           R28cell6.setCellStyle(numberStyle);
	       } else {
	           R28cell6.setCellValue("");
	           R28cell6.setCellStyle(textStyle);
	       }

	       Cell R28cell7 = row.createCell(8);
	       if (record.getR28_moreThan5YearsTo10Years() != null) {
	           R28cell7.setCellValue(record.getR28_moreThan5YearsTo10Years().doubleValue());
	           R28cell7.setCellStyle(numberStyle);
	       } else {
	           R28cell7.setCellValue("");
	           R28cell7.setCellStyle(textStyle);
	       }

	       Cell R28cell8 = row.createCell(9);
	       if (record.getR28_moreThan10Years() != null) {
	           R28cell8.setCellValue(record.getR28_moreThan10Years().doubleValue());
	           R28cell8.setCellStyle(numberStyle);
	       } else {
	           R28cell8.setCellValue("");
	           R28cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(28);
	       Cell R29cell1 = row.createCell(2);
	       if (record.getR29_upTo1Month() != null) {
	           R29cell1.setCellValue(record.getR29_upTo1Month().doubleValue());
	           R29cell1.setCellStyle(numberStyle);
	       } else {
	           R29cell1.setCellValue("");
	           R29cell1.setCellStyle(textStyle);
	       }

	       Cell R29cell2 = row.createCell(3);
	       if (record.getR29_moreThan1MonthTo3Months() != null) {
	           R29cell2.setCellValue(record.getR29_moreThan1MonthTo3Months().doubleValue());
	           R29cell2.setCellStyle(numberStyle);
	       } else {
	           R29cell2.setCellValue("");
	           R29cell2.setCellStyle(textStyle);
	       }

	       Cell R29cell3 = row.createCell(4);
	       if (record.getR29_moreThan3MonthTo6Months() != null) {
	           R29cell3.setCellValue(record.getR29_moreThan3MonthTo6Months().doubleValue());
	           R29cell3.setCellStyle(numberStyle);
	       } else {
	           R29cell3.setCellValue("");
	           R29cell3.setCellStyle(textStyle);
	       }

	       Cell R29cell4 = row.createCell(5);
	       if (record.getR29_moreThan6MonthTo12Months() != null) {
	           R29cell4.setCellValue(record.getR29_moreThan6MonthTo12Months().doubleValue());
	           R29cell4.setCellStyle(numberStyle);
	       } else {
	           R29cell4.setCellValue("");
	           R29cell4.setCellStyle(textStyle);
	       }

	       Cell R29cell5 = row.createCell(6);
	       if (record.getR29_moreThan12MonthTo3Years() != null) {
	           R29cell5.setCellValue(record.getR29_moreThan12MonthTo3Years().doubleValue());
	           R29cell5.setCellStyle(numberStyle);
	       } else {
	           R29cell5.setCellValue("");
	           R29cell5.setCellStyle(textStyle);
	       }

	       Cell R29cell6 = row.createCell(7);
	       if (record.getR29_moreThan3YearsTo5Years() != null) {
	           R29cell6.setCellValue(record.getR29_moreThan3YearsTo5Years().doubleValue());
	           R29cell6.setCellStyle(numberStyle);
	       } else {
	           R29cell6.setCellValue("");
	           R29cell6.setCellStyle(textStyle);
	       }

	       Cell R29cell7 = row.createCell(8);
	       if (record.getR29_moreThan5YearsTo10Years() != null) {
	           R29cell7.setCellValue(record.getR29_moreThan5YearsTo10Years().doubleValue());
	           R29cell7.setCellStyle(numberStyle);
	       } else {
	           R29cell7.setCellValue("");
	           R29cell7.setCellStyle(textStyle);
	       }

	       Cell R29cell8 = row.createCell(9);
	       if (record.getR29_moreThan10Years() != null) {
	           R29cell8.setCellValue(record.getR29_moreThan10Years().doubleValue());
	           R29cell8.setCellStyle(numberStyle);
	       } else {
	           R29cell8.setCellValue("");
	           R29cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(29);
	       Cell R30cell1 = row.createCell(2);
	       if (record.getR30_upTo1Month() != null) {
	           R30cell1.setCellValue(record.getR30_upTo1Month().doubleValue());
	           R30cell1.setCellStyle(numberStyle);
	       } else {
	           R30cell1.setCellValue("");
	           R30cell1.setCellStyle(textStyle);
	       }

	       Cell R30cell2 = row.createCell(3);
	       if (record.getR30_moreThan1MonthTo3Months() != null) {
	           R30cell2.setCellValue(record.getR30_moreThan1MonthTo3Months().doubleValue());
	           R30cell2.setCellStyle(numberStyle);
	       } else {
	           R30cell2.setCellValue("");
	           R30cell2.setCellStyle(textStyle);
	       }

	       Cell R30cell3 = row.createCell(4);
	       if (record.getR30_moreThan3MonthTo6Months() != null) {
	           R30cell3.setCellValue(record.getR30_moreThan3MonthTo6Months().doubleValue());
	           R30cell3.setCellStyle(numberStyle);
	       } else {
	           R30cell3.setCellValue("");
	           R30cell3.setCellStyle(textStyle);
	       }

	       Cell R30cell4 = row.createCell(5);
	       if (record.getR30_moreThan6MonthTo12Months() != null) {
	           R30cell4.setCellValue(record.getR30_moreThan6MonthTo12Months().doubleValue());
	           R30cell4.setCellStyle(numberStyle);
	       } else {
	           R30cell4.setCellValue("");
	           R30cell4.setCellStyle(textStyle);
	       }

	       Cell R30cell5 = row.createCell(6);
	       if (record.getR30_moreThan12MonthTo3Years() != null) {
	           R30cell5.setCellValue(record.getR30_moreThan12MonthTo3Years().doubleValue());
	           R30cell5.setCellStyle(numberStyle);
	       } else {
	           R30cell5.setCellValue("");
	           R30cell5.setCellStyle(textStyle);
	       }

	       Cell R30cell6 = row.createCell(7);
	       if (record.getR30_moreThan3YearsTo5Years() != null) {
	           R30cell6.setCellValue(record.getR30_moreThan3YearsTo5Years().doubleValue());
	           R30cell6.setCellStyle(numberStyle);
	       } else {
	           R30cell6.setCellValue("");
	           R30cell6.setCellStyle(textStyle);
	       }

	       Cell R30cell7 = row.createCell(8);
	       if (record.getR30_moreThan5YearsTo10Years() != null) {
	           R30cell7.setCellValue(record.getR30_moreThan5YearsTo10Years().doubleValue());
	           R30cell7.setCellStyle(numberStyle);
	       } else {
	           R30cell7.setCellValue("");
	           R30cell7.setCellStyle(textStyle);
	       }

	       Cell R30cell8 = row.createCell(9);
	       if (record.getR30_moreThan10Years() != null) {
	           R30cell8.setCellValue(record.getR30_moreThan10Years().doubleValue());
	           R30cell8.setCellStyle(numberStyle);
	       } else {
	           R30cell8.setCellValue("");
	           R30cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(30);
	       Cell R31cell1 = row.createCell(2);
	       if (record.getR31_upTo1Month() != null) {
	           R31cell1.setCellValue(record.getR31_upTo1Month().doubleValue());
	           R31cell1.setCellStyle(numberStyle);
	       } else {
	           R31cell1.setCellValue("");
	           R31cell1.setCellStyle(textStyle);
	       }

	       Cell R31cell2 = row.createCell(3);
	       if (record.getR31_moreThan1MonthTo3Months() != null) {
	           R31cell2.setCellValue(record.getR31_moreThan1MonthTo3Months().doubleValue());
	           R31cell2.setCellStyle(numberStyle);
	       } else {
	           R31cell2.setCellValue("");
	           R31cell2.setCellStyle(textStyle);
	       }

	       Cell R31cell3 = row.createCell(4);
	       if (record.getR31_moreThan3MonthTo6Months() != null) {
	           R31cell3.setCellValue(record.getR31_moreThan3MonthTo6Months().doubleValue());
	           R31cell3.setCellStyle(numberStyle);
	       } else {
	           R31cell3.setCellValue("");
	           R31cell3.setCellStyle(textStyle);
	       }

	       Cell R31cell4 = row.createCell(5);
	       if (record.getR31_moreThan6MonthTo12Months() != null) {
	           R31cell4.setCellValue(record.getR31_moreThan6MonthTo12Months().doubleValue());
	           R31cell4.setCellStyle(numberStyle);
	       } else {
	           R31cell4.setCellValue("");
	           R31cell4.setCellStyle(textStyle);
	       }

	       Cell R31cell5 = row.createCell(6);
	       if (record.getR31_moreThan12MonthTo3Years() != null) {
	           R31cell5.setCellValue(record.getR31_moreThan12MonthTo3Years().doubleValue());
	           R31cell5.setCellStyle(numberStyle);
	       } else {
	           R31cell5.setCellValue("");
	           R31cell5.setCellStyle(textStyle);
	       }

	       Cell R31cell6 = row.createCell(7);
	       if (record.getR31_moreThan3YearsTo5Years() != null) {
	           R31cell6.setCellValue(record.getR31_moreThan3YearsTo5Years().doubleValue());
	           R31cell6.setCellStyle(numberStyle);
	       } else {
	           R31cell6.setCellValue("");
	           R31cell6.setCellStyle(textStyle);
	       }

	       Cell R31cell7 = row.createCell(8);
	       if (record.getR31_moreThan5YearsTo10Years() != null) {
	           R31cell7.setCellValue(record.getR31_moreThan5YearsTo10Years().doubleValue());
	           R31cell7.setCellStyle(numberStyle);
	       } else {
	           R31cell7.setCellValue("");
	           R31cell7.setCellStyle(textStyle);
	       }

	       Cell R31cell8 = row.createCell(9);
	       if (record.getR31_moreThan10Years() != null) {
	           R31cell8.setCellValue(record.getR31_moreThan10Years().doubleValue());
	           R31cell8.setCellStyle(numberStyle);
	       } else {
	           R31cell8.setCellValue("");
	           R31cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(31);
	       Cell R32cell1 = row.createCell(2);
	       if (record.getR32_upTo1Month() != null) {
	           R32cell1.setCellValue(record.getR32_upTo1Month().doubleValue());
	           R32cell1.setCellStyle(numberStyle);
	       } else {
	           R32cell1.setCellValue("");
	           R32cell1.setCellStyle(textStyle);
	       }

	       Cell R32cell2 = row.createCell(3);
	       if (record.getR32_moreThan1MonthTo3Months() != null) {
	           R32cell2.setCellValue(record.getR32_moreThan1MonthTo3Months().doubleValue());
	           R32cell2.setCellStyle(numberStyle);
	       } else {
	           R32cell2.setCellValue("");
	           R32cell2.setCellStyle(textStyle);
	       }

	       Cell R32cell3 = row.createCell(4);
	       if (record.getR32_moreThan3MonthTo6Months() != null) {
	           R32cell3.setCellValue(record.getR32_moreThan3MonthTo6Months().doubleValue());
	           R32cell3.setCellStyle(numberStyle);
	       } else {
	           R32cell3.setCellValue("");
	           R32cell3.setCellStyle(textStyle);
	       }

	       Cell R32cell4 = row.createCell(5);
	       if (record.getR32_moreThan6MonthTo12Months() != null) {
	           R32cell4.setCellValue(record.getR32_moreThan6MonthTo12Months().doubleValue());
	           R32cell4.setCellStyle(numberStyle);
	       } else {
	           R32cell4.setCellValue("");
	           R32cell4.setCellStyle(textStyle);
	       }

	       Cell R32cell5 = row.createCell(6);
	       if (record.getR32_moreThan12MonthTo3Years() != null) {
	           R32cell5.setCellValue(record.getR32_moreThan12MonthTo3Years().doubleValue());
	           R32cell5.setCellStyle(numberStyle);
	       } else {
	           R32cell5.setCellValue("");
	           R32cell5.setCellStyle(textStyle);
	       }

	       Cell R32cell6 = row.createCell(7);
	       if (record.getR32_moreThan3YearsTo5Years() != null) {
	           R32cell6.setCellValue(record.getR32_moreThan3YearsTo5Years().doubleValue());
	           R32cell6.setCellStyle(numberStyle);
	       } else {
	           R32cell6.setCellValue("");
	           R32cell6.setCellStyle(textStyle);
	       }

	       Cell R32cell7 = row.createCell(8);
	       if (record.getR32_moreThan5YearsTo10Years() != null) {
	           R32cell7.setCellValue(record.getR32_moreThan5YearsTo10Years().doubleValue());
	           R32cell7.setCellStyle(numberStyle);
	       } else {
	           R32cell7.setCellValue("");
	           R32cell7.setCellStyle(textStyle);
	       }

	       Cell R32cell8 = row.createCell(9);
	       if (record.getR32_moreThan10Years() != null) {
	           R32cell8.setCellValue(record.getR32_moreThan10Years().doubleValue());
	           R32cell8.setCellStyle(numberStyle);
	       } else {
	           R32cell8.setCellValue("");
	           R32cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(32);
	       Cell R33cell1 = row.createCell(2);
	       if (record.getR33_upTo1Month() != null) {
	           R33cell1.setCellValue(record.getR33_upTo1Month().doubleValue());
	           R33cell1.setCellStyle(numberStyle);
	       } else {
	           R33cell1.setCellValue("");
	           R33cell1.setCellStyle(textStyle);
	       }

	       Cell R33cell2 = row.createCell(3);
	       if (record.getR33_moreThan1MonthTo3Months() != null) {
	           R33cell2.setCellValue(record.getR33_moreThan1MonthTo3Months().doubleValue());
	           R33cell2.setCellStyle(numberStyle);
	       } else {
	           R33cell2.setCellValue("");
	           R33cell2.setCellStyle(textStyle);
	       }

	       Cell R33cell3 = row.createCell(4);
	       if (record.getR33_moreThan3MonthTo6Months() != null) {
	           R33cell3.setCellValue(record.getR33_moreThan3MonthTo6Months().doubleValue());
	           R33cell3.setCellStyle(numberStyle);
	       } else {
	           R33cell3.setCellValue("");
	           R33cell3.setCellStyle(textStyle);
	       }

	       Cell R33cell4 = row.createCell(5);
	       if (record.getR33_moreThan6MonthTo12Months() != null) {
	           R33cell4.setCellValue(record.getR33_moreThan6MonthTo12Months().doubleValue());
	           R33cell4.setCellStyle(numberStyle);
	       } else {
	           R33cell4.setCellValue("");
	           R33cell4.setCellStyle(textStyle);
	       }

	       Cell R33cell5 = row.createCell(6);
	       if (record.getR33_moreThan12MonthTo3Years() != null) {
	           R33cell5.setCellValue(record.getR33_moreThan12MonthTo3Years().doubleValue());
	           R33cell5.setCellStyle(numberStyle);
	       } else {
	           R33cell5.setCellValue("");
	           R33cell5.setCellStyle(textStyle);
	       }

	       Cell R33cell6 = row.createCell(7);
	       if (record.getR33_moreThan3YearsTo5Years() != null) {
	           R33cell6.setCellValue(record.getR33_moreThan3YearsTo5Years().doubleValue());
	           R33cell6.setCellStyle(numberStyle);
	       } else {
	           R33cell6.setCellValue("");
	           R33cell6.setCellStyle(textStyle);
	       }

	       Cell R33cell7 = row.createCell(8);
	       if (record.getR33_moreThan5YearsTo10Years() != null) {
	           R33cell7.setCellValue(record.getR33_moreThan5YearsTo10Years().doubleValue());
	           R33cell7.setCellStyle(numberStyle);
	       } else {
	           R33cell7.setCellValue("");
	           R33cell7.setCellStyle(textStyle);
	       }

	       Cell R33cell8 = row.createCell(9);
	       if (record.getR33_moreThan10Years() != null) {
	           R33cell8.setCellValue(record.getR33_moreThan10Years().doubleValue());
	           R33cell8.setCellStyle(numberStyle);
	       } else {
	           R33cell8.setCellValue("");
	           R33cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(33);
	       Cell R34cell1 = row.createCell(2);
	       if (record.getR34_upTo1Month() != null) {
	           R34cell1.setCellValue(record.getR34_upTo1Month().doubleValue());
	           R34cell1.setCellStyle(numberStyle);
	       } else {
	           R34cell1.setCellValue("");
	           R34cell1.setCellStyle(textStyle);
	       }

	       Cell R34cell2 = row.createCell(3);
	       if (record.getR34_moreThan1MonthTo3Months() != null) {
	           R34cell2.setCellValue(record.getR34_moreThan1MonthTo3Months().doubleValue());
	           R34cell2.setCellStyle(numberStyle);
	       } else {
	           R34cell2.setCellValue("");
	           R34cell2.setCellStyle(textStyle);
	       }

	       Cell R34cell3 = row.createCell(4);
	       if (record.getR34_moreThan3MonthTo6Months() != null) {
	           R34cell3.setCellValue(record.getR34_moreThan3MonthTo6Months().doubleValue());
	           R34cell3.setCellStyle(numberStyle);
	       } else {
	           R34cell3.setCellValue("");
	           R34cell3.setCellStyle(textStyle);
	       }

	       Cell R34cell4 = row.createCell(5);
	       if (record.getR34_moreThan6MonthTo12Months() != null) {
	           R34cell4.setCellValue(record.getR34_moreThan6MonthTo12Months().doubleValue());
	           R34cell4.setCellStyle(numberStyle);
	       } else {
	           R34cell4.setCellValue("");
	           R34cell4.setCellStyle(textStyle);
	       }

	       Cell R34cell5 = row.createCell(6);
	       if (record.getR34_moreThan12MonthTo3Years() != null) {
	           R34cell5.setCellValue(record.getR34_moreThan12MonthTo3Years().doubleValue());
	           R34cell5.setCellStyle(numberStyle);
	       } else {
	           R34cell5.setCellValue("");
	           R34cell5.setCellStyle(textStyle);
	       }

	       Cell R34cell6 = row.createCell(7);
	       if (record.getR34_moreThan3YearsTo5Years() != null) {
	           R34cell6.setCellValue(record.getR34_moreThan3YearsTo5Years().doubleValue());
	           R34cell6.setCellStyle(numberStyle);
	       } else {
	           R34cell6.setCellValue("");
	           R34cell6.setCellStyle(textStyle);
	       }

	       Cell R34cell7 = row.createCell(8);
	       if (record.getR34_moreThan5YearsTo10Years() != null) {
	           R34cell7.setCellValue(record.getR34_moreThan5YearsTo10Years().doubleValue());
	           R34cell7.setCellStyle(numberStyle);
	       } else {
	           R34cell7.setCellValue("");
	           R34cell7.setCellStyle(textStyle);
	       }

	       Cell R34cell8 = row.createCell(9);
	       if (record.getR34_moreThan10Years() != null) {
	           R34cell8.setCellValue(record.getR34_moreThan10Years().doubleValue());
	           R34cell8.setCellStyle(numberStyle);
	       } else {
	           R34cell8.setCellValue("");
	           R34cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(35);
	       Cell R36cell1 = row.createCell(2);
	       if (record.getR36_upTo1Month() != null) {
	           R36cell1.setCellValue(record.getR36_upTo1Month().doubleValue());
	           R36cell1.setCellStyle(numberStyle);
	       } else {
	           R36cell1.setCellValue("");
	           R36cell1.setCellStyle(textStyle);
	       }

	       Cell R36cell2 = row.createCell(3);
	       if (record.getR36_moreThan1MonthTo3Months() != null) {
	           R36cell2.setCellValue(record.getR36_moreThan1MonthTo3Months().doubleValue());
	           R36cell2.setCellStyle(numberStyle);
	       } else {
	           R36cell2.setCellValue("");
	           R36cell2.setCellStyle(textStyle);
	       }

	       Cell R36cell3 = row.createCell(4);
	       if (record.getR36_moreThan3MonthTo6Months() != null) {
	           R36cell3.setCellValue(record.getR36_moreThan3MonthTo6Months().doubleValue());
	           R36cell3.setCellStyle(numberStyle);
	       } else {
	           R36cell3.setCellValue("");
	           R36cell3.setCellStyle(textStyle);
	       }

	       Cell R36cell4 = row.createCell(5);
	       if (record.getR36_moreThan6MonthTo12Months() != null) {
	           R36cell4.setCellValue(record.getR36_moreThan6MonthTo12Months().doubleValue());
	           R36cell4.setCellStyle(numberStyle);
	       } else {
	           R36cell4.setCellValue("");
	           R36cell4.setCellStyle(textStyle);
	       }

	       Cell R36cell5 = row.createCell(6);
	       if (record.getR36_moreThan12MonthTo3Years() != null) {
	           R36cell5.setCellValue(record.getR36_moreThan12MonthTo3Years().doubleValue());
	           R36cell5.setCellStyle(numberStyle);
	       } else {
	           R36cell5.setCellValue("");
	           R36cell5.setCellStyle(textStyle);
	       }

	       Cell R36cell6 = row.createCell(7);
	       if (record.getR36_moreThan3YearsTo5Years() != null) {
	           R36cell6.setCellValue(record.getR36_moreThan3YearsTo5Years().doubleValue());
	           R36cell6.setCellStyle(numberStyle);
	       } else {
	           R36cell6.setCellValue("");
	           R36cell6.setCellStyle(textStyle);
	       }

	       Cell R36cell7 = row.createCell(8);
	       if (record.getR36_moreThan5YearsTo10Years() != null) {
	           R36cell7.setCellValue(record.getR36_moreThan5YearsTo10Years().doubleValue());
	           R36cell7.setCellStyle(numberStyle);
	       } else {
	           R36cell7.setCellValue("");
	           R36cell7.setCellStyle(textStyle);
	       }

	       Cell R36cell8 = row.createCell(9);
	       if (record.getR36_moreThan10Years() != null) {
	           R36cell8.setCellValue(record.getR36_moreThan10Years().doubleValue());
	           R36cell8.setCellStyle(numberStyle);
	       } else {
	           R36cell8.setCellValue("");
	           R36cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(36);
	       Cell R37cell1 = row.createCell(2);
	       if (record.getR37_upTo1Month() != null) {
	           R37cell1.setCellValue(record.getR37_upTo1Month().doubleValue());
	           R37cell1.setCellStyle(numberStyle);
	       } else {
	           R37cell1.setCellValue("");
	           R37cell1.setCellStyle(textStyle);
	       }

	       Cell R37cell2 = row.createCell(3);
	       if (record.getR37_moreThan1MonthTo3Months() != null) {
	           R37cell2.setCellValue(record.getR37_moreThan1MonthTo3Months().doubleValue());
	           R37cell2.setCellStyle(numberStyle);
	       } else {
	           R37cell2.setCellValue("");
	           R37cell2.setCellStyle(textStyle);
	       }

	       Cell R37cell3 = row.createCell(4);
	       if (record.getR37_moreThan3MonthTo6Months() != null) {
	           R37cell3.setCellValue(record.getR37_moreThan3MonthTo6Months().doubleValue());
	           R37cell3.setCellStyle(numberStyle);
	       } else {
	           R37cell3.setCellValue("");
	           R37cell3.setCellStyle(textStyle);
	       }

	       Cell R37cell4 = row.createCell(5);
	       if (record.getR37_moreThan6MonthTo12Months() != null) {
	           R37cell4.setCellValue(record.getR37_moreThan6MonthTo12Months().doubleValue());
	           R37cell4.setCellStyle(numberStyle);
	       } else {
	           R37cell4.setCellValue("");
	           R37cell4.setCellStyle(textStyle);
	       }

	       Cell R37cell5 = row.createCell(6);
	       if (record.getR37_moreThan12MonthTo3Years() != null) {
	           R37cell5.setCellValue(record.getR37_moreThan12MonthTo3Years().doubleValue());
	           R37cell5.setCellStyle(numberStyle);
	       } else {
	           R37cell5.setCellValue("");
	           R37cell5.setCellStyle(textStyle);
	       }

	       Cell R37cell6 = row.createCell(7);
	       if (record.getR37_moreThan3YearsTo5Years() != null) {
	           R37cell6.setCellValue(record.getR37_moreThan3YearsTo5Years().doubleValue());
	           R37cell6.setCellStyle(numberStyle);
	       } else {
	           R37cell6.setCellValue("");
	           R37cell6.setCellStyle(textStyle);
	       }

	       Cell R37cell7 = row.createCell(8);
	       if (record.getR37_moreThan5YearsTo10Years() != null) {
	           R37cell7.setCellValue(record.getR37_moreThan5YearsTo10Years().doubleValue());
	           R37cell7.setCellStyle(numberStyle);
	       } else {
	           R37cell7.setCellValue("");
	           R37cell7.setCellStyle(textStyle);
	       }

	       Cell R37cell8 = row.createCell(9);
	       if (record.getR37_moreThan10Years() != null) {
	           R37cell8.setCellValue(record.getR37_moreThan10Years().doubleValue());
	           R37cell8.setCellStyle(numberStyle);
	       } else {
	           R37cell8.setCellValue("");
	           R37cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(37);
	       Cell R38cell1 = row.createCell(2);
	       if (record.getR38_upTo1Month() != null) {
	           R38cell1.setCellValue(record.getR38_upTo1Month().doubleValue());
	           R38cell1.setCellStyle(numberStyle);
	       } else {
	           R38cell1.setCellValue("");
	           R38cell1.setCellStyle(textStyle);
	       }

	       Cell R38cell2 = row.createCell(3);
	       if (record.getR38_moreThan1MonthTo3Months() != null) {
	           R38cell2.setCellValue(record.getR38_moreThan1MonthTo3Months().doubleValue());
	           R38cell2.setCellStyle(numberStyle);
	       } else {
	           R38cell2.setCellValue("");
	           R38cell2.setCellStyle(textStyle);
	       }

	       Cell R38cell3 = row.createCell(4);
	       if (record.getR38_moreThan3MonthTo6Months() != null) {
	           R38cell3.setCellValue(record.getR38_moreThan3MonthTo6Months().doubleValue());
	           R38cell3.setCellStyle(numberStyle);
	       } else {
	           R38cell3.setCellValue("");
	           R38cell3.setCellStyle(textStyle);
	       }

	       Cell R38cell4 = row.createCell(5);
	       if (record.getR38_moreThan6MonthTo12Months() != null) {
	           R38cell4.setCellValue(record.getR38_moreThan6MonthTo12Months().doubleValue());
	           R38cell4.setCellStyle(numberStyle);
	       } else {
	           R38cell4.setCellValue("");
	           R38cell4.setCellStyle(textStyle);
	       }

	       Cell R38cell5 = row.createCell(6);
	       if (record.getR38_moreThan12MonthTo3Years() != null) {
	           R38cell5.setCellValue(record.getR38_moreThan12MonthTo3Years().doubleValue());
	           R38cell5.setCellStyle(numberStyle);
	       } else {
	           R38cell5.setCellValue("");
	           R38cell5.setCellStyle(textStyle);
	       }

	       Cell R38cell6 = row.createCell(7);
	       if (record.getR38_moreThan3YearsTo5Years() != null) {
	           R38cell6.setCellValue(record.getR38_moreThan3YearsTo5Years().doubleValue());
	           R38cell6.setCellStyle(numberStyle);
	       } else {
	           R38cell6.setCellValue("");
	           R38cell6.setCellStyle(textStyle);
	       }

	       Cell R38cell7 = row.createCell(8);
	       if (record.getR38_moreThan5YearsTo10Years() != null) {
	           R38cell7.setCellValue(record.getR38_moreThan5YearsTo10Years().doubleValue());
	           R38cell7.setCellStyle(numberStyle);
	       } else {
	           R38cell7.setCellValue("");
	           R38cell7.setCellStyle(textStyle);
	       }

	       Cell R38cell8 = row.createCell(9);
	       if (record.getR38_moreThan10Years() != null) {
	           R38cell8.setCellValue(record.getR38_moreThan10Years().doubleValue());
	           R38cell8.setCellStyle(numberStyle);
	       } else {
	           R38cell8.setCellValue("");
	           R38cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(38);
	       Cell R39cell1 = row.createCell(2);
	       if (record.getR39_upTo1Month() != null) {
	           R39cell1.setCellValue(record.getR39_upTo1Month().doubleValue());
	           R39cell1.setCellStyle(numberStyle);
	       } else {
	           R39cell1.setCellValue("");
	           R39cell1.setCellStyle(textStyle);
	       }

	       Cell R39cell2 = row.createCell(3);
	       if (record.getR39_moreThan1MonthTo3Months() != null) {
	           R39cell2.setCellValue(record.getR39_moreThan1MonthTo3Months().doubleValue());
	           R39cell2.setCellStyle(numberStyle);
	       } else {
	           R39cell2.setCellValue("");
	           R39cell2.setCellStyle(textStyle);
	       }

	       Cell R39cell3 = row.createCell(4);
	       if (record.getR39_moreThan3MonthTo6Months() != null) {
	           R39cell3.setCellValue(record.getR39_moreThan3MonthTo6Months().doubleValue());
	           R39cell3.setCellStyle(numberStyle);
	       } else {
	           R39cell3.setCellValue("");
	           R39cell3.setCellStyle(textStyle);
	       }

	       Cell R39cell4 = row.createCell(5);
	       if (record.getR39_moreThan6MonthTo12Months() != null) {
	           R39cell4.setCellValue(record.getR39_moreThan6MonthTo12Months().doubleValue());
	           R39cell4.setCellStyle(numberStyle);
	       } else {
	           R39cell4.setCellValue("");
	           R39cell4.setCellStyle(textStyle);
	       }

	       Cell R39cell5 = row.createCell(6);
	       if (record.getR39_moreThan12MonthTo3Years() != null) {
	           R39cell5.setCellValue(record.getR39_moreThan12MonthTo3Years().doubleValue());
	           R39cell5.setCellStyle(numberStyle);
	       } else {
	           R39cell5.setCellValue("");
	           R39cell5.setCellStyle(textStyle);
	       }

	       Cell R39cell6 = row.createCell(7);
	       if (record.getR39_moreThan3YearsTo5Years() != null) {
	           R39cell6.setCellValue(record.getR39_moreThan3YearsTo5Years().doubleValue());
	           R39cell6.setCellStyle(numberStyle);
	       } else {
	           R39cell6.setCellValue("");
	           R39cell6.setCellStyle(textStyle);
	       }

	       Cell R39cell7 = row.createCell(8);
	       if (record.getR39_moreThan5YearsTo10Years() != null) {
	           R39cell7.setCellValue(record.getR39_moreThan5YearsTo10Years().doubleValue());
	           R39cell7.setCellStyle(numberStyle);
	       } else {
	           R39cell7.setCellValue("");
	           R39cell7.setCellStyle(textStyle);
	       }

	       Cell R39cell8 = row.createCell(9);
	       if (record.getR39_moreThan10Years() != null) {
	           R39cell8.setCellValue(record.getR39_moreThan10Years().doubleValue());
	           R39cell8.setCellStyle(numberStyle);
	       } else {
	           R39cell8.setCellValue("");
	           R39cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(39);
	       Cell R40cell1 = row.createCell(2);
	       if (record.getR40_upTo1Month() != null) {
	           R40cell1.setCellValue(record.getR40_upTo1Month().doubleValue());
	           R40cell1.setCellStyle(numberStyle);
	       } else {
	           R40cell1.setCellValue("");
	           R40cell1.setCellStyle(textStyle);
	       }

	       Cell R40cell2 = row.createCell(3);
	       if (record.getR40_moreThan1MonthTo3Months() != null) {
	           R40cell2.setCellValue(record.getR40_moreThan1MonthTo3Months().doubleValue());
	           R40cell2.setCellStyle(numberStyle);
	       } else {
	           R40cell2.setCellValue("");
	           R40cell2.setCellStyle(textStyle);
	       }

	       Cell R40cell3 = row.createCell(4);
	       if (record.getR40_moreThan3MonthTo6Months() != null) {
	           R40cell3.setCellValue(record.getR40_moreThan3MonthTo6Months().doubleValue());
	           R40cell3.setCellStyle(numberStyle);
	       } else {
	           R40cell3.setCellValue("");
	           R40cell3.setCellStyle(textStyle);
	       }

	       Cell R40cell4 = row.createCell(5);
	       if (record.getR40_moreThan6MonthTo12Months() != null) {
	           R40cell4.setCellValue(record.getR40_moreThan6MonthTo12Months().doubleValue());
	           R40cell4.setCellStyle(numberStyle);
	       } else {
	           R40cell4.setCellValue("");
	           R40cell4.setCellStyle(textStyle);
	       }

	       Cell R40cell5 = row.createCell(6);
	       if (record.getR40_moreThan12MonthTo3Years() != null) {
	           R40cell5.setCellValue(record.getR40_moreThan12MonthTo3Years().doubleValue());
	           R40cell5.setCellStyle(numberStyle);
	       } else {
	           R40cell5.setCellValue("");
	           R40cell5.setCellStyle(textStyle);
	       }

	       Cell R40cell6 = row.createCell(7);
	       if (record.getR40_moreThan3YearsTo5Years() != null) {
	           R40cell6.setCellValue(record.getR40_moreThan3YearsTo5Years().doubleValue());
	           R40cell6.setCellStyle(numberStyle);
	       } else {
	           R40cell6.setCellValue("");
	           R40cell6.setCellStyle(textStyle);
	       }

	       Cell R40cell7 = row.createCell(8);
	       if (record.getR40_moreThan5YearsTo10Years() != null) {
	           R40cell7.setCellValue(record.getR40_moreThan5YearsTo10Years().doubleValue());
	           R40cell7.setCellStyle(numberStyle);
	       } else {
	           R40cell7.setCellValue("");
	           R40cell7.setCellStyle(textStyle);
	       }

	       Cell R40cell8 = row.createCell(9);
	       if (record.getR40_moreThan10Years() != null) {
	           R40cell8.setCellValue(record.getR40_moreThan10Years().doubleValue());
	           R40cell8.setCellStyle(numberStyle);
	       } else {
	           R40cell8.setCellValue("");
	           R40cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(40);
	       Cell R41cell1 = row.createCell(2);
	       if (record.getR41_upTo1Month() != null) {
	           R41cell1.setCellValue(record.getR41_upTo1Month().doubleValue());
	           R41cell1.setCellStyle(numberStyle);
	       } else {
	           R41cell1.setCellValue("");
	           R41cell1.setCellStyle(textStyle);
	       }

	       Cell R41cell2 = row.createCell(3);
	       if (record.getR41_moreThan1MonthTo3Months() != null) {
	           R41cell2.setCellValue(record.getR41_moreThan1MonthTo3Months().doubleValue());
	           R41cell2.setCellStyle(numberStyle);
	       } else {
	           R41cell2.setCellValue("");
	           R41cell2.setCellStyle(textStyle);
	       }

	       Cell R41cell3 = row.createCell(4);
	       if (record.getR41_moreThan3MonthTo6Months() != null) {
	           R41cell3.setCellValue(record.getR41_moreThan3MonthTo6Months().doubleValue());
	           R41cell3.setCellStyle(numberStyle);
	       } else {
	           R41cell3.setCellValue("");
	           R41cell3.setCellStyle(textStyle);
	       }

	       Cell R41cell4 = row.createCell(5);
	       if (record.getR41_moreThan6MonthTo12Months() != null) {
	           R41cell4.setCellValue(record.getR41_moreThan6MonthTo12Months().doubleValue());
	           R41cell4.setCellStyle(numberStyle);
	       } else {
	           R41cell4.setCellValue("");
	           R41cell4.setCellStyle(textStyle);
	       }

	       Cell R41cell5 = row.createCell(6);
	       if (record.getR41_moreThan12MonthTo3Years() != null) {
	           R41cell5.setCellValue(record.getR41_moreThan12MonthTo3Years().doubleValue());
	           R41cell5.setCellStyle(numberStyle);
	       } else {
	           R41cell5.setCellValue("");
	           R41cell5.setCellStyle(textStyle);
	       }

	       Cell R41cell6 = row.createCell(7);
	       if (record.getR41_moreThan3YearsTo5Years() != null) {
	           R41cell6.setCellValue(record.getR41_moreThan3YearsTo5Years().doubleValue());
	           R41cell6.setCellStyle(numberStyle);
	       } else {
	           R41cell6.setCellValue("");
	           R41cell6.setCellStyle(textStyle);
	       }

	       Cell R41cell7 = row.createCell(8);
	       if (record.getR41_moreThan5YearsTo10Years() != null) {
	           R41cell7.setCellValue(record.getR41_moreThan5YearsTo10Years().doubleValue());
	           R41cell7.setCellStyle(numberStyle);
	       } else {
	           R41cell7.setCellValue("");
	           R41cell7.setCellStyle(textStyle);
	       }

	       Cell R41cell8 = row.createCell(9);
	       if (record.getR41_moreThan10Years() != null) {
	           R41cell8.setCellValue(record.getR41_moreThan10Years().doubleValue());
	           R41cell8.setCellStyle(numberStyle);
	       } else {
	           R41cell8.setCellValue("");
	           R41cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(41);
	       Cell R42cell1 = row.createCell(2);
	       if (record.getR42_upTo1Month() != null) {
	           R42cell1.setCellValue(record.getR42_upTo1Month().doubleValue());
	           R42cell1.setCellStyle(numberStyle);
	       } else {
	           R42cell1.setCellValue("");
	           R42cell1.setCellStyle(textStyle);
	       }

	       Cell R42cell2 = row.createCell(3);
	       if (record.getR42_moreThan1MonthTo3Months() != null) {
	           R42cell2.setCellValue(record.getR42_moreThan1MonthTo3Months().doubleValue());
	           R42cell2.setCellStyle(numberStyle);
	       } else {
	           R42cell2.setCellValue("");
	           R42cell2.setCellStyle(textStyle);
	       }

	       Cell R42cell3 = row.createCell(4);
	       if (record.getR42_moreThan3MonthTo6Months() != null) {
	           R42cell3.setCellValue(record.getR42_moreThan3MonthTo6Months().doubleValue());
	           R42cell3.setCellStyle(numberStyle);
	       } else {
	           R42cell3.setCellValue("");
	           R42cell3.setCellStyle(textStyle);
	       }

	       Cell R42cell4 = row.createCell(5);
	       if (record.getR42_moreThan6MonthTo12Months() != null) {
	           R42cell4.setCellValue(record.getR42_moreThan6MonthTo12Months().doubleValue());
	           R42cell4.setCellStyle(numberStyle);
	       } else {
	           R42cell4.setCellValue("");
	           R42cell4.setCellStyle(textStyle);
	       }

	       Cell R42cell5 = row.createCell(6);
	       if (record.getR42_moreThan12MonthTo3Years() != null) {
	           R42cell5.setCellValue(record.getR42_moreThan12MonthTo3Years().doubleValue());
	           R42cell5.setCellStyle(numberStyle);
	       } else {
	           R42cell5.setCellValue("");
	           R42cell5.setCellStyle(textStyle);
	       }

	       Cell R42cell6 = row.createCell(7);
	       if (record.getR42_moreThan3YearsTo5Years() != null) {
	           R42cell6.setCellValue(record.getR42_moreThan3YearsTo5Years().doubleValue());
	           R42cell6.setCellStyle(numberStyle);
	       } else {
	           R42cell6.setCellValue("");
	           R42cell6.setCellStyle(textStyle);
	       }

	       Cell R42cell7 = row.createCell(8);
	       if (record.getR42_moreThan5YearsTo10Years() != null) {
	           R42cell7.setCellValue(record.getR42_moreThan5YearsTo10Years().doubleValue());
	           R42cell7.setCellStyle(numberStyle);
	       } else {
	           R42cell7.setCellValue("");
	           R42cell7.setCellStyle(textStyle);
	       }

	       Cell R42cell8 = row.createCell(9);
	       if (record.getR42_moreThan10Years() != null) {
	           R42cell8.setCellValue(record.getR42_moreThan10Years().doubleValue());
	           R42cell8.setCellStyle(numberStyle);
	       } else {
	           R42cell8.setCellValue("");
	           R42cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(42);
	       Cell R43cell1 = row.createCell(2);
	       if (record.getR43_upTo1Month() != null) {
	           R43cell1.setCellValue(record.getR43_upTo1Month().doubleValue());
	           R43cell1.setCellStyle(numberStyle);
	       } else {
	           R43cell1.setCellValue("");
	           R43cell1.setCellStyle(textStyle);
	       }

	       Cell R43cell2 = row.createCell(3);
	       if (record.getR43_moreThan1MonthTo3Months() != null) {
	           R43cell2.setCellValue(record.getR43_moreThan1MonthTo3Months().doubleValue());
	           R43cell2.setCellStyle(numberStyle);
	       } else {
	           R43cell2.setCellValue("");
	           R43cell2.setCellStyle(textStyle);
	       }

	       Cell R43cell3 = row.createCell(4);
	       if (record.getR43_moreThan3MonthTo6Months() != null) {
	           R43cell3.setCellValue(record.getR43_moreThan3MonthTo6Months().doubleValue());
	           R43cell3.setCellStyle(numberStyle);
	       } else {
	           R43cell3.setCellValue("");
	           R43cell3.setCellStyle(textStyle);
	       }

	       Cell R43cell4 = row.createCell(5);
	       if (record.getR43_moreThan6MonthTo12Months() != null) {
	           R43cell4.setCellValue(record.getR43_moreThan6MonthTo12Months().doubleValue());
	           R43cell4.setCellStyle(numberStyle);
	       } else {
	           R43cell4.setCellValue("");
	           R43cell4.setCellStyle(textStyle);
	       }

	       Cell R43cell5 = row.createCell(6);
	       if (record.getR43_moreThan12MonthTo3Years() != null) {
	           R43cell5.setCellValue(record.getR43_moreThan12MonthTo3Years().doubleValue());
	           R43cell5.setCellStyle(numberStyle);
	       } else {
	           R43cell5.setCellValue("");
	           R43cell5.setCellStyle(textStyle);
	       }

	       Cell R43cell6 = row.createCell(7);
	       if (record.getR43_moreThan3YearsTo5Years() != null) {
	           R43cell6.setCellValue(record.getR43_moreThan3YearsTo5Years().doubleValue());
	           R43cell6.setCellStyle(numberStyle);
	       } else {
	           R43cell6.setCellValue("");
	           R43cell6.setCellStyle(textStyle);
	       }

	       Cell R43cell7 = row.createCell(8);
	       if (record.getR43_moreThan5YearsTo10Years() != null) {
	           R43cell7.setCellValue(record.getR43_moreThan5YearsTo10Years().doubleValue());
	           R43cell7.setCellStyle(numberStyle);
	       } else {
	           R43cell7.setCellValue("");
	           R43cell7.setCellStyle(textStyle);
	       }

	       Cell R43cell8 = row.createCell(9);
	       if (record.getR43_moreThan10Years() != null) {
	           R43cell8.setCellValue(record.getR43_moreThan10Years().doubleValue());
	           R43cell8.setCellStyle(numberStyle);
	       } else {
	           R43cell8.setCellValue("");
	           R43cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(43);
	       Cell R44cell1 = row.createCell(2);
	       if (record.getR44_upTo1Month() != null) {
	           R44cell1.setCellValue(record.getR44_upTo1Month().doubleValue());
	           R44cell1.setCellStyle(numberStyle);
	       } else {
	           R44cell1.setCellValue("");
	           R44cell1.setCellStyle(textStyle);
	       }

	       Cell R44cell2 = row.createCell(3);
	       if (record.getR44_moreThan1MonthTo3Months() != null) {
	           R44cell2.setCellValue(record.getR44_moreThan1MonthTo3Months().doubleValue());
	           R44cell2.setCellStyle(numberStyle);
	       } else {
	           R44cell2.setCellValue("");
	           R44cell2.setCellStyle(textStyle);
	       }

	       Cell R44cell3 = row.createCell(4);
	       if (record.getR44_moreThan3MonthTo6Months() != null) {
	           R44cell3.setCellValue(record.getR44_moreThan3MonthTo6Months().doubleValue());
	           R44cell3.setCellStyle(numberStyle);
	       } else {
	           R44cell3.setCellValue("");
	           R44cell3.setCellStyle(textStyle);
	       }

	       Cell R44cell4 = row.createCell(5);
	       if (record.getR44_moreThan6MonthTo12Months() != null) {
	           R44cell4.setCellValue(record.getR44_moreThan6MonthTo12Months().doubleValue());
	           R44cell4.setCellStyle(numberStyle);
	       } else {
	           R44cell4.setCellValue("");
	           R44cell4.setCellStyle(textStyle);
	       }

	       Cell R44cell5 = row.createCell(6);
	       if (record.getR44_moreThan12MonthTo3Years() != null) {
	           R44cell5.setCellValue(record.getR44_moreThan12MonthTo3Years().doubleValue());
	           R44cell5.setCellStyle(numberStyle);
	       } else {
	           R44cell5.setCellValue("");
	           R44cell5.setCellStyle(textStyle);
	       }

	       Cell R44cell6 = row.createCell(7);
	       if (record.getR44_moreThan3YearsTo5Years() != null) {
	           R44cell6.setCellValue(record.getR44_moreThan3YearsTo5Years().doubleValue());
	           R44cell6.setCellStyle(numberStyle);
	       } else {
	           R44cell6.setCellValue("");
	           R44cell6.setCellStyle(textStyle);
	       }

	       Cell R44cell7 = row.createCell(8);
	       if (record.getR44_moreThan5YearsTo10Years() != null) {
	           R44cell7.setCellValue(record.getR44_moreThan5YearsTo10Years().doubleValue());
	           R44cell7.setCellStyle(numberStyle);
	       } else {
	           R44cell7.setCellValue("");
	           R44cell7.setCellStyle(textStyle);
	       }

	       Cell R44cell8 = row.createCell(9);
	       if (record.getR44_moreThan10Years() != null) {
	           R44cell8.setCellValue(record.getR44_moreThan10Years().doubleValue());
	           R44cell8.setCellStyle(numberStyle);
	       } else {
	           R44cell8.setCellValue("");
	           R44cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(44);
	       Cell R45cell1 = row.createCell(2);
	       if (record.getR45_upTo1Month() != null) {
	           R45cell1.setCellValue(record.getR45_upTo1Month().doubleValue());
	           R45cell1.setCellStyle(numberStyle);
	       } else {
	           R45cell1.setCellValue("");
	           R45cell1.setCellStyle(textStyle);
	       }

	       Cell R45cell2 = row.createCell(3);
	       if (record.getR45_moreThan1MonthTo3Months() != null) {
	           R45cell2.setCellValue(record.getR45_moreThan1MonthTo3Months().doubleValue());
	           R45cell2.setCellStyle(numberStyle);
	       } else {
	           R45cell2.setCellValue("");
	           R45cell2.setCellStyle(textStyle);
	       }

	       Cell R45cell3 = row.createCell(4);
	       if (record.getR45_moreThan3MonthTo6Months() != null) {
	           R45cell3.setCellValue(record.getR45_moreThan3MonthTo6Months().doubleValue());
	           R45cell3.setCellStyle(numberStyle);
	       } else {
	           R45cell3.setCellValue("");
	           R45cell3.setCellStyle(textStyle);
	       }

	       Cell R45cell4 = row.createCell(5);
	       if (record.getR45_moreThan6MonthTo12Months() != null) {
	           R45cell4.setCellValue(record.getR45_moreThan6MonthTo12Months().doubleValue());
	           R45cell4.setCellStyle(numberStyle);
	       } else {
	           R45cell4.setCellValue("");
	           R45cell4.setCellStyle(textStyle);
	       }

	       Cell R45cell5 = row.createCell(6);
	       if (record.getR45_moreThan12MonthTo3Years() != null) {
	           R45cell5.setCellValue(record.getR45_moreThan12MonthTo3Years().doubleValue());
	           R45cell5.setCellStyle(numberStyle);
	       } else {
	           R45cell5.setCellValue("");
	           R45cell5.setCellStyle(textStyle);
	       }

	       Cell R45cell6 = row.createCell(7);
	       if (record.getR45_moreThan3YearsTo5Years() != null) {
	           R45cell6.setCellValue(record.getR45_moreThan3YearsTo5Years().doubleValue());
	           R45cell6.setCellStyle(numberStyle);
	       } else {
	           R45cell6.setCellValue("");
	           R45cell6.setCellStyle(textStyle);
	       }

	       Cell R45cell7 = row.createCell(8);
	       if (record.getR45_moreThan5YearsTo10Years() != null) {
	           R45cell7.setCellValue(record.getR45_moreThan5YearsTo10Years().doubleValue());
	           R45cell7.setCellStyle(numberStyle);
	       } else {
	           R45cell7.setCellValue("");
	           R45cell7.setCellStyle(textStyle);
	       }

	       Cell R45cell8 = row.createCell(9);
	       if (record.getR45_moreThan10Years() != null) {
	           R45cell8.setCellValue(record.getR45_moreThan10Years().doubleValue());
	           R45cell8.setCellStyle(numberStyle);
	       } else {
	           R45cell8.setCellValue("");
	           R45cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(45);
	       Cell R46cell1 = row.createCell(2);
	       if (record.getR46_upTo1Month() != null) {
	           R46cell1.setCellValue(record.getR46_upTo1Month().doubleValue());
	           R46cell1.setCellStyle(numberStyle);
	       } else {
	           R46cell1.setCellValue("");
	           R46cell1.setCellStyle(textStyle);
	       }

	       Cell R46cell2 = row.createCell(3);
	       if (record.getR46_moreThan1MonthTo3Months() != null) {
	           R46cell2.setCellValue(record.getR46_moreThan1MonthTo3Months().doubleValue());
	           R46cell2.setCellStyle(numberStyle);
	       } else {
	           R46cell2.setCellValue("");
	           R46cell2.setCellStyle(textStyle);
	       }

	       Cell R46cell3 = row.createCell(4);
	       if (record.getR46_moreThan3MonthTo6Months() != null) {
	           R46cell3.setCellValue(record.getR46_moreThan3MonthTo6Months().doubleValue());
	           R46cell3.setCellStyle(numberStyle);
	       } else {
	           R46cell3.setCellValue("");
	           R46cell3.setCellStyle(textStyle);
	       }

	       Cell R46cell4 = row.createCell(5);
	       if (record.getR46_moreThan6MonthTo12Months() != null) {
	           R46cell4.setCellValue(record.getR46_moreThan6MonthTo12Months().doubleValue());
	           R46cell4.setCellStyle(numberStyle);
	       } else {
	           R46cell4.setCellValue("");
	           R46cell4.setCellStyle(textStyle);
	       }

	       Cell R46cell5 = row.createCell(6);
	       if (record.getR46_moreThan12MonthTo3Years() != null) {
	           R46cell5.setCellValue(record.getR46_moreThan12MonthTo3Years().doubleValue());
	           R46cell5.setCellStyle(numberStyle);
	       } else {
	           R46cell5.setCellValue("");
	           R46cell5.setCellStyle(textStyle);
	       }

	       Cell R46cell6 = row.createCell(7);
	       if (record.getR46_moreThan3YearsTo5Years() != null) {
	           R46cell6.setCellValue(record.getR46_moreThan3YearsTo5Years().doubleValue());
	           R46cell6.setCellStyle(numberStyle);
	       } else {
	           R46cell6.setCellValue("");
	           R46cell6.setCellStyle(textStyle);
	       }

	       Cell R46cell7 = row.createCell(8);
	       if (record.getR46_moreThan5YearsTo10Years() != null) {
	           R46cell7.setCellValue(record.getR46_moreThan5YearsTo10Years().doubleValue());
	           R46cell7.setCellStyle(numberStyle);
	       } else {
	           R46cell7.setCellValue("");
	           R46cell7.setCellStyle(textStyle);
	       }

	       Cell R46cell8 = row.createCell(9);
	       if (record.getR46_moreThan10Years() != null) {
	           R46cell8.setCellValue(record.getR46_moreThan10Years().doubleValue());
	           R46cell8.setCellStyle(numberStyle);
	       } else {
	           R46cell8.setCellValue("");
	           R46cell8.setCellStyle(textStyle);
	       }
	       
	       
		    
	       row = sheet.getRow(47);
	       Cell R48cell9 = row.createCell(10);
	       if (record.getR48_nonRatioSensativeItems() != null) {
	           R48cell9.setCellValue(record.getR48_nonRatioSensativeItems().doubleValue());
	           R48cell9.setCellStyle(numberStyle);
	       } else {
	           R48cell9.setCellValue("");
	           R48cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(48);
	       Cell R49cell9 = row.createCell(10);
	       if (record.getR49_nonRatioSensativeItems() != null) {
	           R49cell9.setCellValue(record.getR49_nonRatioSensativeItems().doubleValue());
	           R49cell9.setCellStyle(numberStyle);
	       } else {
	           R49cell9.setCellValue("");
	           R49cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(49);
	       Cell R50cell9 = row.createCell(10);
	       if (record.getR50_nonRatioSensativeItems() != null) {
	           R50cell9.setCellValue(record.getR50_nonRatioSensativeItems().doubleValue());
	           R50cell9.setCellStyle(numberStyle);
	       } else {
	           R50cell9.setCellValue("");
	           R50cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(50);
	       Cell R51cell9 = row.createCell(10);
	       if (record.getR51_nonRatioSensativeItems() != null) {
	           R51cell9.setCellValue(record.getR51_nonRatioSensativeItems().doubleValue());
	           R51cell9.setCellStyle(numberStyle);
	       } else {
	           R51cell9.setCellValue("");
	           R51cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(51);
	       Cell R52cell9 = row.createCell(10);
	       if (record.getR52_nonRatioSensativeItems() != null) {
	           R52cell9.setCellValue(record.getR52_nonRatioSensativeItems().doubleValue());
	           R52cell9.setCellStyle(numberStyle);
	       } else {
	           R52cell9.setCellValue("");
	           R52cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(52);
	       Cell R53cell9 = row.createCell(10);
	       if (record.getR53_nonRatioSensativeItems() != null) {
	           R53cell9.setCellValue(record.getR53_nonRatioSensativeItems().doubleValue());
	           R53cell9.setCellStyle(numberStyle);
	       } else {
	           R53cell9.setCellValue("");
	           R53cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(53);
	       Cell R54cell9 = row.createCell(10);
	       if (record.getR54_nonRatioSensativeItems() != null) {
	           R54cell9.setCellValue(record.getR54_nonRatioSensativeItems().doubleValue());
	           R54cell9.setCellStyle(numberStyle);
	       } else {
	           R54cell9.setCellValue("");
	           R54cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(54);
	       Cell R55cell9 = row.createCell(10);
	       if (record.getR55_nonRatioSensativeItems() != null) {
	           R55cell9.setCellValue(record.getR55_nonRatioSensativeItems().doubleValue());
	           R55cell9.setCellStyle(numberStyle);
	       } else {
	           R55cell9.setCellValue("");
	           R55cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(55);
	       Cell R56cell9 = row.createCell(10);
	       if (record.getR56_nonRatioSensativeItems() != null) {
	           R56cell9.setCellValue(record.getR56_nonRatioSensativeItems().doubleValue());
	           R56cell9.setCellStyle(numberStyle);
	       } else {
	           R56cell9.setCellValue("");
	           R56cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(56);
	       Cell R57cell9 = row.createCell(10);
	       if (record.getR57_nonRatioSensativeItems() != null) {
	           R57cell9.setCellValue(record.getR57_nonRatioSensativeItems().doubleValue());
	           R57cell9.setCellStyle(numberStyle);
	       } else {
	           R57cell9.setCellValue("");
	           R57cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(57);
	       Cell R58cell9 = row.createCell(10);
	       if (record.getR58_nonRatioSensativeItems() != null) {
	           R58cell9.setCellValue(record.getR58_nonRatioSensativeItems().doubleValue());
	           R58cell9.setCellStyle(numberStyle);
	       } else {
	           R58cell9.setCellValue("");
	           R58cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(60);
	       Cell R61cell1 = row.createCell(2);
	       if (record.getR61_upTo1Month() != null) {
	           R61cell1.setCellValue(record.getR61_upTo1Month().doubleValue());
	           R61cell1.setCellStyle(numberStyle);
	       } else {
	           R61cell1.setCellValue("");
	           R61cell1.setCellStyle(textStyle);
	       }

	       Cell R61cell2 = row.createCell(3);
	       if (record.getR61_moreThan1MonthTo3Months() != null) {
	           R61cell2.setCellValue(record.getR61_moreThan1MonthTo3Months().doubleValue());
	           R61cell2.setCellStyle(numberStyle);
	       } else {
	           R61cell2.setCellValue("");
	           R61cell2.setCellStyle(textStyle);
	       }

	       Cell R61cell3 = row.createCell(4);
	       if (record.getR61_moreThan3MonthTo6Months() != null) {
	           R61cell3.setCellValue(record.getR61_moreThan3MonthTo6Months().doubleValue());
	           R61cell3.setCellStyle(numberStyle);
	       } else {
	           R61cell3.setCellValue("");
	           R61cell3.setCellStyle(textStyle);
	       }

	       Cell R61cell4 = row.createCell(5);
	       if (record.getR61_moreThan6MonthTo12Months() != null) {
	           R61cell4.setCellValue(record.getR61_moreThan6MonthTo12Months().doubleValue());
	           R61cell4.setCellStyle(numberStyle);
	       } else {
	           R61cell4.setCellValue("");
	           R61cell4.setCellStyle(textStyle);
	       }

	       Cell R61cell5 = row.createCell(6);
	       if (record.getR61_moreThan12MonthTo3Years() != null) {
	           R61cell5.setCellValue(record.getR61_moreThan12MonthTo3Years().doubleValue());
	           R61cell5.setCellStyle(numberStyle);
	       } else {
	           R61cell5.setCellValue("");
	           R61cell5.setCellStyle(textStyle);
	       }

	       Cell R61cell6 = row.createCell(7);
	       if (record.getR61_moreThan3YearsTo5Years() != null) {
	           R61cell6.setCellValue(record.getR61_moreThan3YearsTo5Years().doubleValue());
	           R61cell6.setCellStyle(numberStyle);
	       } else {
	           R61cell6.setCellValue("");
	           R61cell6.setCellStyle(textStyle);
	       }

	       Cell R61cell7 = row.createCell(8);
	       if (record.getR61_moreThan5YearsTo10Years() != null) {
	           R61cell7.setCellValue(record.getR61_moreThan5YearsTo10Years().doubleValue());
	           R61cell7.setCellStyle(numberStyle);
	       } else {
	           R61cell7.setCellValue("");
	           R61cell7.setCellStyle(textStyle);
	       }

	       Cell R61cell8 = row.createCell(9);
	       if (record.getR61_moreThan10Years() != null) {
	           R61cell8.setCellValue(record.getR61_moreThan10Years().doubleValue());
	           R61cell8.setCellStyle(numberStyle);
	       } else {
	           R61cell8.setCellValue("");
	           R61cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(61);
	       Cell R62cell1 = row.createCell(2);
	       if (record.getR62_upTo1Month() != null) {
	           R62cell1.setCellValue(record.getR62_upTo1Month().doubleValue());
	           R62cell1.setCellStyle(numberStyle);
	       } else {
	           R62cell1.setCellValue("");
	           R62cell1.setCellStyle(textStyle);
	       }

	       Cell R62cell2 = row.createCell(3);
	       if (record.getR62_moreThan1MonthTo3Months() != null) {
	           R62cell2.setCellValue(record.getR62_moreThan1MonthTo3Months().doubleValue());
	           R62cell2.setCellStyle(numberStyle);
	       } else {
	           R62cell2.setCellValue("");
	           R62cell2.setCellStyle(textStyle);
	       }

	       Cell R62cell3 = row.createCell(4);
	       if (record.getR62_moreThan3MonthTo6Months() != null) {
	           R62cell3.setCellValue(record.getR62_moreThan3MonthTo6Months().doubleValue());
	           R62cell3.setCellStyle(numberStyle);
	       } else {
	           R62cell3.setCellValue("");
	           R62cell3.setCellStyle(textStyle);
	       }

	       Cell R62cell4 = row.createCell(5);
	       if (record.getR62_moreThan6MonthTo12Months() != null) {
	           R62cell4.setCellValue(record.getR62_moreThan6MonthTo12Months().doubleValue());
	           R62cell4.setCellStyle(numberStyle);
	       } else {
	           R62cell4.setCellValue("");
	           R62cell4.setCellStyle(textStyle);
	       }

	       Cell R62cell5 = row.createCell(6);
	       if (record.getR62_moreThan12MonthTo3Years() != null) {
	           R62cell5.setCellValue(record.getR62_moreThan12MonthTo3Years().doubleValue());
	           R62cell5.setCellStyle(numberStyle);
	       } else {
	           R62cell5.setCellValue("");
	           R62cell5.setCellStyle(textStyle);
	       }

	       Cell R62cell6 = row.createCell(7);
	       if (record.getR62_moreThan3YearsTo5Years() != null) {
	           R62cell6.setCellValue(record.getR62_moreThan3YearsTo5Years().doubleValue());
	           R62cell6.setCellStyle(numberStyle);
	       } else {
	           R62cell6.setCellValue("");
	           R62cell6.setCellStyle(textStyle);
	       }

	       Cell R62cell7 = row.createCell(8);
	       if (record.getR62_moreThan5YearsTo10Years() != null) {
	           R62cell7.setCellValue(record.getR62_moreThan5YearsTo10Years().doubleValue());
	           R62cell7.setCellStyle(numberStyle);
	       } else {
	           R62cell7.setCellValue("");
	           R62cell7.setCellStyle(textStyle);
	       }

	       Cell R62cell8 = row.createCell(9);
	       if (record.getR62_moreThan10Years() != null) {
	           R62cell8.setCellValue(record.getR62_moreThan10Years().doubleValue());
	           R62cell8.setCellStyle(numberStyle);
	       } else {
	           R62cell8.setCellValue("");
	           R62cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(62);
	       Cell R63cell1 = row.createCell(2);
	       if (record.getR63_upTo1Month() != null) {
	           R63cell1.setCellValue(record.getR63_upTo1Month().doubleValue());
	           R63cell1.setCellStyle(numberStyle);
	       } else {
	           R63cell1.setCellValue("");
	           R63cell1.setCellStyle(textStyle);
	       }

	       Cell R63cell2 = row.createCell(3);
	       if (record.getR63_moreThan1MonthTo3Months() != null) {
	           R63cell2.setCellValue(record.getR63_moreThan1MonthTo3Months().doubleValue());
	           R63cell2.setCellStyle(numberStyle);
	       } else {
	           R63cell2.setCellValue("");
	           R63cell2.setCellStyle(textStyle);
	       }

	       Cell R63cell3 = row.createCell(4);
	       if (record.getR63_moreThan3MonthTo6Months() != null) {
	           R63cell3.setCellValue(record.getR63_moreThan3MonthTo6Months().doubleValue());
	           R63cell3.setCellStyle(numberStyle);
	       } else {
	           R63cell3.setCellValue("");
	           R63cell3.setCellStyle(textStyle);
	       }

	       Cell R63cell4 = row.createCell(5);
	       if (record.getR63_moreThan6MonthTo12Months() != null) {
	           R63cell4.setCellValue(record.getR63_moreThan6MonthTo12Months().doubleValue());
	           R63cell4.setCellStyle(numberStyle);
	       } else {
	           R63cell4.setCellValue("");
	           R63cell4.setCellStyle(textStyle);
	       }

	       Cell R63cell5 = row.createCell(6);
	       if (record.getR63_moreThan12MonthTo3Years() != null) {
	           R63cell5.setCellValue(record.getR63_moreThan12MonthTo3Years().doubleValue());
	           R63cell5.setCellStyle(numberStyle);
	       } else {
	           R63cell5.setCellValue("");
	           R63cell5.setCellStyle(textStyle);
	       }

	       Cell R63cell6 = row.createCell(7);
	       if (record.getR63_moreThan3YearsTo5Years() != null) {
	           R63cell6.setCellValue(record.getR63_moreThan3YearsTo5Years().doubleValue());
	           R63cell6.setCellStyle(numberStyle);
	       } else {
	           R63cell6.setCellValue("");
	           R63cell6.setCellStyle(textStyle);
	       }

	       Cell R63cell7 = row.createCell(8);
	       if (record.getR63_moreThan5YearsTo10Years() != null) {
	           R63cell7.setCellValue(record.getR63_moreThan5YearsTo10Years().doubleValue());
	           R63cell7.setCellStyle(numberStyle);
	       } else {
	           R63cell7.setCellValue("");
	           R63cell7.setCellStyle(textStyle);
	       }

	       Cell R63cell8 = row.createCell(9);
	       if (record.getR63_moreThan10Years() != null) {
	           R63cell8.setCellValue(record.getR63_moreThan10Years().doubleValue());
	           R63cell8.setCellStyle(numberStyle);
	       } else {
	           R63cell8.setCellValue("");
	           R63cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(63);
	       Cell R64cell1 = row.createCell(2);
	       if (record.getR64_upTo1Month() != null) {
	           R64cell1.setCellValue(record.getR64_upTo1Month().doubleValue());
	           R64cell1.setCellStyle(numberStyle);
	       } else {
	           R64cell1.setCellValue("");
	           R64cell1.setCellStyle(textStyle);
	       }

	       Cell R64cell2 = row.createCell(3);
	       if (record.getR64_moreThan1MonthTo3Months() != null) {
	           R64cell2.setCellValue(record.getR64_moreThan1MonthTo3Months().doubleValue());
	           R64cell2.setCellStyle(numberStyle);
	       } else {
	           R64cell2.setCellValue("");
	           R64cell2.setCellStyle(textStyle);
	       }

	       Cell R64cell3 = row.createCell(4);
	       if (record.getR64_moreThan3MonthTo6Months() != null) {
	           R64cell3.setCellValue(record.getR64_moreThan3MonthTo6Months().doubleValue());
	           R64cell3.setCellStyle(numberStyle);
	       } else {
	           R64cell3.setCellValue("");
	           R64cell3.setCellStyle(textStyle);
	       }

	       Cell R64cell4 = row.createCell(5);
	       if (record.getR64_moreThan6MonthTo12Months() != null) {
	           R64cell4.setCellValue(record.getR64_moreThan6MonthTo12Months().doubleValue());
	           R64cell4.setCellStyle(numberStyle);
	       } else {
	           R64cell4.setCellValue("");
	           R64cell4.setCellStyle(textStyle);
	       }

	       Cell R64cell5 = row.createCell(6);
	       if (record.getR64_moreThan12MonthTo3Years() != null) {
	           R64cell5.setCellValue(record.getR64_moreThan12MonthTo3Years().doubleValue());
	           R64cell5.setCellStyle(numberStyle);
	       } else {
	           R64cell5.setCellValue("");
	           R64cell5.setCellStyle(textStyle);
	       }

	       Cell R64cell6 = row.createCell(7);
	       if (record.getR64_moreThan3YearsTo5Years() != null) {
	           R64cell6.setCellValue(record.getR64_moreThan3YearsTo5Years().doubleValue());
	           R64cell6.setCellStyle(numberStyle);
	       } else {
	           R64cell6.setCellValue("");
	           R64cell6.setCellStyle(textStyle);
	       }

	       Cell R64cell7 = row.createCell(8);
	       if (record.getR64_moreThan5YearsTo10Years() != null) {
	           R64cell7.setCellValue(record.getR64_moreThan5YearsTo10Years().doubleValue());
	           R64cell7.setCellStyle(numberStyle);
	       } else {
	           R64cell7.setCellValue("");
	           R64cell7.setCellStyle(textStyle);
	       }

	       Cell R64cell8 = row.createCell(9);
	       if (record.getR64_moreThan10Years() != null) {
	           R64cell8.setCellValue(record.getR64_moreThan10Years().doubleValue());
	           R64cell8.setCellStyle(numberStyle);
	       } else {
	           R64cell8.setCellValue("");
	           R64cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(64);
	       Cell R65cell1 = row.createCell(2);
	       if (record.getR65_upTo1Month() != null) {
	           R65cell1.setCellValue(record.getR65_upTo1Month().doubleValue());
	           R65cell1.setCellStyle(numberStyle);
	       } else {
	           R65cell1.setCellValue("");
	           R65cell1.setCellStyle(textStyle);
	       }

	       Cell R65cell2 = row.createCell(3);
	       if (record.getR65_moreThan1MonthTo3Months() != null) {
	           R65cell2.setCellValue(record.getR65_moreThan1MonthTo3Months().doubleValue());
	           R65cell2.setCellStyle(numberStyle);
	       } else {
	           R65cell2.setCellValue("");
	           R65cell2.setCellStyle(textStyle);
	       }

	       Cell R65cell3 = row.createCell(4);
	       if (record.getR65_moreThan3MonthTo6Months() != null) {
	           R65cell3.setCellValue(record.getR65_moreThan3MonthTo6Months().doubleValue());
	           R65cell3.setCellStyle(numberStyle);
	       } else {
	           R65cell3.setCellValue("");
	           R65cell3.setCellStyle(textStyle);
	       }

	       Cell R65cell4 = row.createCell(5);
	       if (record.getR65_moreThan6MonthTo12Months() != null) {
	           R65cell4.setCellValue(record.getR65_moreThan6MonthTo12Months().doubleValue());
	           R65cell4.setCellStyle(numberStyle);
	       } else {
	           R65cell4.setCellValue("");
	           R65cell4.setCellStyle(textStyle);
	       }

	       Cell R65cell5 = row.createCell(6);
	       if (record.getR65_moreThan12MonthTo3Years() != null) {
	           R65cell5.setCellValue(record.getR65_moreThan12MonthTo3Years().doubleValue());
	           R65cell5.setCellStyle(numberStyle);
	       } else {
	           R65cell5.setCellValue("");
	           R65cell5.setCellStyle(textStyle);
	       }

	       Cell R65cell6 = row.createCell(7);
	       if (record.getR65_moreThan3YearsTo5Years() != null) {
	           R65cell6.setCellValue(record.getR65_moreThan3YearsTo5Years().doubleValue());
	           R65cell6.setCellStyle(numberStyle);
	       } else {
	           R65cell6.setCellValue("");
	           R65cell6.setCellStyle(textStyle);
	       }

	       Cell R65cell7 = row.createCell(8);
	       if (record.getR65_moreThan5YearsTo10Years() != null) {
	           R65cell7.setCellValue(record.getR65_moreThan5YearsTo10Years().doubleValue());
	           R65cell7.setCellStyle(numberStyle);
	       } else {
	           R65cell7.setCellValue("");
	           R65cell7.setCellStyle(textStyle);
	       }

	       Cell R65cell8 = row.createCell(9);
	       if (record.getR65_moreThan10Years() != null) {
	           R65cell8.setCellValue(record.getR65_moreThan10Years().doubleValue());
	           R65cell8.setCellStyle(numberStyle);
	       } else {
	           R65cell8.setCellValue("");
	           R65cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(65);
	       Cell R66cell1 = row.createCell(2);
	       if (record.getR66_upTo1Month() != null) {
	           R66cell1.setCellValue(record.getR66_upTo1Month().doubleValue());
	           R66cell1.setCellStyle(numberStyle);
	       } else {
	           R66cell1.setCellValue("");
	           R66cell1.setCellStyle(textStyle);
	       }

	       Cell R66cell2 = row.createCell(3);
	       if (record.getR66_moreThan1MonthTo3Months() != null) {
	           R66cell2.setCellValue(record.getR66_moreThan1MonthTo3Months().doubleValue());
	           R66cell2.setCellStyle(numberStyle);
	       } else {
	           R66cell2.setCellValue("");
	           R66cell2.setCellStyle(textStyle);
	       }

	       Cell R66cell3 = row.createCell(4);
	       if (record.getR66_moreThan3MonthTo6Months() != null) {
	           R66cell3.setCellValue(record.getR66_moreThan3MonthTo6Months().doubleValue());
	           R66cell3.setCellStyle(numberStyle);
	       } else {
	           R66cell3.setCellValue("");
	           R66cell3.setCellStyle(textStyle);
	       }

	       Cell R66cell4 = row.createCell(5);
	       if (record.getR66_moreThan6MonthTo12Months() != null) {
	           R66cell4.setCellValue(record.getR66_moreThan6MonthTo12Months().doubleValue());
	           R66cell4.setCellStyle(numberStyle);
	       } else {
	           R66cell4.setCellValue("");
	           R66cell4.setCellStyle(textStyle);
	       }

	       Cell R66cell5 = row.createCell(6);
	       if (record.getR66_moreThan12MonthTo3Years() != null) {
	           R66cell5.setCellValue(record.getR66_moreThan12MonthTo3Years().doubleValue());
	           R66cell5.setCellStyle(numberStyle);
	       } else {
	           R66cell5.setCellValue("");
	           R66cell5.setCellStyle(textStyle);
	       }

	       Cell R66cell6 = row.createCell(7);
	       if (record.getR66_moreThan3YearsTo5Years() != null) {
	           R66cell6.setCellValue(record.getR66_moreThan3YearsTo5Years().doubleValue());
	           R66cell6.setCellStyle(numberStyle);
	       } else {
	           R66cell6.setCellValue("");
	           R66cell6.setCellStyle(textStyle);
	       }

	       Cell R66cell7 = row.createCell(8);
	       if (record.getR66_moreThan5YearsTo10Years() != null) {
	           R66cell7.setCellValue(record.getR66_moreThan5YearsTo10Years().doubleValue());
	           R66cell7.setCellStyle(numberStyle);
	       } else {
	           R66cell7.setCellValue("");
	           R66cell7.setCellStyle(textStyle);
	       }

	       Cell R66cell8 = row.createCell(9);
	       if (record.getR66_moreThan10Years() != null) {
	           R66cell8.setCellValue(record.getR66_moreThan10Years().doubleValue());
	           R66cell8.setCellStyle(numberStyle);
	       } else {
	           R66cell8.setCellValue("");
	           R66cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(66);
	       Cell R67cell1 = row.createCell(2);
	       if (record.getR67_upTo1Month() != null) {
	           R67cell1.setCellValue(record.getR67_upTo1Month().doubleValue());
	           R67cell1.setCellStyle(numberStyle);
	       } else {
	           R67cell1.setCellValue("");
	           R67cell1.setCellStyle(textStyle);
	       }

	       Cell R67cell2 = row.createCell(3);
	       if (record.getR67_moreThan1MonthTo3Months() != null) {
	           R67cell2.setCellValue(record.getR67_moreThan1MonthTo3Months().doubleValue());
	           R67cell2.setCellStyle(numberStyle);
	       } else {
	           R67cell2.setCellValue("");
	           R67cell2.setCellStyle(textStyle);
	       }

	       Cell R67cell3 = row.createCell(4);
	       if (record.getR67_moreThan3MonthTo6Months() != null) {
	           R67cell3.setCellValue(record.getR67_moreThan3MonthTo6Months().doubleValue());
	           R67cell3.setCellStyle(numberStyle);
	       } else {
	           R67cell3.setCellValue("");
	           R67cell3.setCellStyle(textStyle);
	       }

	       Cell R67cell4 = row.createCell(5);
	       if (record.getR67_moreThan6MonthTo12Months() != null) {
	           R67cell4.setCellValue(record.getR67_moreThan6MonthTo12Months().doubleValue());
	           R67cell4.setCellStyle(numberStyle);
	       } else {
	           R67cell4.setCellValue("");
	           R67cell4.setCellStyle(textStyle);
	       }

	       Cell R67cell5 = row.createCell(6);
	       if (record.getR67_moreThan12MonthTo3Years() != null) {
	           R67cell5.setCellValue(record.getR67_moreThan12MonthTo3Years().doubleValue());
	           R67cell5.setCellStyle(numberStyle);
	       } else {
	           R67cell5.setCellValue("");
	           R67cell5.setCellStyle(textStyle);
	       }

	       Cell R67cell6 = row.createCell(7);
	       if (record.getR67_moreThan3YearsTo5Years() != null) {
	           R67cell6.setCellValue(record.getR67_moreThan3YearsTo5Years().doubleValue());
	           R67cell6.setCellStyle(numberStyle);
	       } else {
	           R67cell6.setCellValue("");
	           R67cell6.setCellStyle(textStyle);
	       }

	       Cell R67cell7 = row.createCell(8);
	       if (record.getR67_moreThan5YearsTo10Years() != null) {
	           R67cell7.setCellValue(record.getR67_moreThan5YearsTo10Years().doubleValue());
	           R67cell7.setCellStyle(numberStyle);
	       } else {
	           R67cell7.setCellValue("");
	           R67cell7.setCellStyle(textStyle);
	       }

	       Cell R67cell8 = row.createCell(9);
	       if (record.getR67_moreThan10Years() != null) {
	           R67cell8.setCellValue(record.getR67_moreThan10Years().doubleValue());
	           R67cell8.setCellStyle(numberStyle);
	       } else {
	           R67cell8.setCellValue("");
	           R67cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(68);
	       Cell R69cell1 = row.createCell(2);
	       if (record.getR69_upTo1Month() != null) {
	           R69cell1.setCellValue(record.getR69_upTo1Month().doubleValue());
	           R69cell1.setCellStyle(numberStyle);
	       } else {
	           R69cell1.setCellValue("");
	           R69cell1.setCellStyle(textStyle);
	       }

	       Cell R69cell2 = row.createCell(3);
	       if (record.getR69_moreThan1MonthTo3Months() != null) {
	           R69cell2.setCellValue(record.getR69_moreThan1MonthTo3Months().doubleValue());
	           R69cell2.setCellStyle(numberStyle);
	       } else {
	           R69cell2.setCellValue("");
	           R69cell2.setCellStyle(textStyle);
	       }

	       Cell R69cell3 = row.createCell(4);
	       if (record.getR69_moreThan3MonthTo6Months() != null) {
	           R69cell3.setCellValue(record.getR69_moreThan3MonthTo6Months().doubleValue());
	           R69cell3.setCellStyle(numberStyle);
	       } else {
	           R69cell3.setCellValue("");
	           R69cell3.setCellStyle(textStyle);
	       }

	       Cell R69cell4 = row.createCell(5);
	       if (record.getR69_moreThan6MonthTo12Months() != null) {
	           R69cell4.setCellValue(record.getR69_moreThan6MonthTo12Months().doubleValue());
	           R69cell4.setCellStyle(numberStyle);
	       } else {
	           R69cell4.setCellValue("");
	           R69cell4.setCellStyle(textStyle);
	       }

	       Cell R69cell5 = row.createCell(6);
	       if (record.getR69_moreThan12MonthTo3Years() != null) {
	           R69cell5.setCellValue(record.getR69_moreThan12MonthTo3Years().doubleValue());
	           R69cell5.setCellStyle(numberStyle);
	       } else {
	           R69cell5.setCellValue("");
	           R69cell5.setCellStyle(textStyle);
	       }

	       Cell R69cell6 = row.createCell(7);
	       if (record.getR69_moreThan3YearsTo5Years() != null) {
	           R69cell6.setCellValue(record.getR69_moreThan3YearsTo5Years().doubleValue());
	           R69cell6.setCellStyle(numberStyle);
	       } else {
	           R69cell6.setCellValue("");
	           R69cell6.setCellStyle(textStyle);
	       }

	       Cell R69cell7 = row.createCell(8);
	       if (record.getR69_moreThan5YearsTo10Years() != null) {
	           R69cell7.setCellValue(record.getR69_moreThan5YearsTo10Years().doubleValue());
	           R69cell7.setCellStyle(numberStyle);
	       } else {
	           R69cell7.setCellValue("");
	           R69cell7.setCellStyle(textStyle);
	       }

	       Cell R69cell8 = row.createCell(9);
	       if (record.getR69_moreThan10Years() != null) {
	           R69cell8.setCellValue(record.getR69_moreThan10Years().doubleValue());
	           R69cell8.setCellStyle(numberStyle);
	       } else {
	           R69cell8.setCellValue("");
	           R69cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(69);
	       Cell R70cell1 = row.createCell(2);
	       if (record.getR70_upTo1Month() != null) {
	           R70cell1.setCellValue(record.getR70_upTo1Month().doubleValue());
	           R70cell1.setCellStyle(numberStyle);
	       } else {
	           R70cell1.setCellValue("");
	           R70cell1.setCellStyle(textStyle);
	       }

	       Cell R70cell2 = row.createCell(3);
	       if (record.getR70_moreThan1MonthTo3Months() != null) {
	           R70cell2.setCellValue(record.getR70_moreThan1MonthTo3Months().doubleValue());
	           R70cell2.setCellStyle(numberStyle);
	       } else {
	           R70cell2.setCellValue("");
	           R70cell2.setCellStyle(textStyle);
	       }

	       Cell R70cell3 = row.createCell(4);
	       if (record.getR70_moreThan3MonthTo6Months() != null) {
	           R70cell3.setCellValue(record.getR70_moreThan3MonthTo6Months().doubleValue());
	           R70cell3.setCellStyle(numberStyle);
	       } else {
	           R70cell3.setCellValue("");
	           R70cell3.setCellStyle(textStyle);
	       }

	       Cell R70cell4 = row.createCell(5);
	       if (record.getR70_moreThan6MonthTo12Months() != null) {
	           R70cell4.setCellValue(record.getR70_moreThan6MonthTo12Months().doubleValue());
	           R70cell4.setCellStyle(numberStyle);
	       } else {
	           R70cell4.setCellValue("");
	           R70cell4.setCellStyle(textStyle);
	       }

	       Cell R70cell5 = row.createCell(6);
	       if (record.getR70_moreThan12MonthTo3Years() != null) {
	           R70cell5.setCellValue(record.getR70_moreThan12MonthTo3Years().doubleValue());
	           R70cell5.setCellStyle(numberStyle);
	       } else {
	           R70cell5.setCellValue("");
	           R70cell5.setCellStyle(textStyle);
	       }

	       Cell R70cell6 = row.createCell(7);
	       if (record.getR70_moreThan3YearsTo5Years() != null) {
	           R70cell6.setCellValue(record.getR70_moreThan3YearsTo5Years().doubleValue());
	           R70cell6.setCellStyle(numberStyle);
	       } else {
	           R70cell6.setCellValue("");
	           R70cell6.setCellStyle(textStyle);
	       }

	       Cell R70cell7 = row.createCell(8);
	       if (record.getR70_moreThan5YearsTo10Years() != null) {
	           R70cell7.setCellValue(record.getR70_moreThan5YearsTo10Years().doubleValue());
	           R70cell7.setCellStyle(numberStyle);
	       } else {
	           R70cell7.setCellValue("");
	           R70cell7.setCellStyle(textStyle);
	       }

	       Cell R70cell8 = row.createCell(9);
	       if (record.getR70_moreThan10Years() != null) {
	           R70cell8.setCellValue(record.getR70_moreThan10Years().doubleValue());
	           R70cell8.setCellStyle(numberStyle);
	       } else {
	           R70cell8.setCellValue("");
	           R70cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(70);
	       Cell R71cell1 = row.createCell(2);
	       if (record.getR71_upTo1Month() != null) {
	           R71cell1.setCellValue(record.getR71_upTo1Month().doubleValue());
	           R71cell1.setCellStyle(numberStyle);
	       } else {
	           R71cell1.setCellValue("");
	           R71cell1.setCellStyle(textStyle);
	       }

	       Cell R71cell2 = row.createCell(3);
	       if (record.getR71_moreThan1MonthTo3Months() != null) {
	           R71cell2.setCellValue(record.getR71_moreThan1MonthTo3Months().doubleValue());
	           R71cell2.setCellStyle(numberStyle);
	       } else {
	           R71cell2.setCellValue("");
	           R71cell2.setCellStyle(textStyle);
	       }

	       Cell R71cell3 = row.createCell(4);
	       if (record.getR71_moreThan3MonthTo6Months() != null) {
	           R71cell3.setCellValue(record.getR71_moreThan3MonthTo6Months().doubleValue());
	           R71cell3.setCellStyle(numberStyle);
	       } else {
	           R71cell3.setCellValue("");
	           R71cell3.setCellStyle(textStyle);
	       }

	       Cell R71cell4 = row.createCell(5);
	       if (record.getR71_moreThan6MonthTo12Months() != null) {
	           R71cell4.setCellValue(record.getR71_moreThan6MonthTo12Months().doubleValue());
	           R71cell4.setCellStyle(numberStyle);
	       } else {
	           R71cell4.setCellValue("");
	           R71cell4.setCellStyle(textStyle);
	       }

	       Cell R71cell5 = row.createCell(6);
	       if (record.getR71_moreThan12MonthTo3Years() != null) {
	           R71cell5.setCellValue(record.getR71_moreThan12MonthTo3Years().doubleValue());
	           R71cell5.setCellStyle(numberStyle);
	       } else {
	           R71cell5.setCellValue("");
	           R71cell5.setCellStyle(textStyle);
	       }

	       Cell R71cell6 = row.createCell(7);
	       if (record.getR71_moreThan3YearsTo5Years() != null) {
	           R71cell6.setCellValue(record.getR71_moreThan3YearsTo5Years().doubleValue());
	           R71cell6.setCellStyle(numberStyle);
	       } else {
	           R71cell6.setCellValue("");
	           R71cell6.setCellStyle(textStyle);
	       }

	       Cell R71cell7 = row.createCell(8);
	       if (record.getR71_moreThan5YearsTo10Years() != null) {
	           R71cell7.setCellValue(record.getR71_moreThan5YearsTo10Years().doubleValue());
	           R71cell7.setCellStyle(numberStyle);
	       } else {
	           R71cell7.setCellValue("");
	           R71cell7.setCellStyle(textStyle);
	       }

	       Cell R71cell8 = row.createCell(9);
	       if (record.getR71_moreThan10Years() != null) {
	           R71cell8.setCellValue(record.getR71_moreThan10Years().doubleValue());
	           R71cell8.setCellStyle(numberStyle);
	       } else {
	           R71cell8.setCellValue("");
	           R71cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(71);
	       Cell R72cell1 = row.createCell(2);
	       if (record.getR72_upTo1Month() != null) {
	           R72cell1.setCellValue(record.getR72_upTo1Month().doubleValue());
	           R72cell1.setCellStyle(numberStyle);
	       } else {
	           R72cell1.setCellValue("");
	           R72cell1.setCellStyle(textStyle);
	       }

	       Cell R72cell2 = row.createCell(3);
	       if (record.getR72_moreThan1MonthTo3Months() != null) {
	           R72cell2.setCellValue(record.getR72_moreThan1MonthTo3Months().doubleValue());
	           R72cell2.setCellStyle(numberStyle);
	       } else {
	           R72cell2.setCellValue("");
	           R72cell2.setCellStyle(textStyle);
	       }

	       Cell R72cell3 = row.createCell(4);
	       if (record.getR72_moreThan3MonthTo6Months() != null) {
	           R72cell3.setCellValue(record.getR72_moreThan3MonthTo6Months().doubleValue());
	           R72cell3.setCellStyle(numberStyle);
	       } else {
	           R72cell3.setCellValue("");
	           R72cell3.setCellStyle(textStyle);
	       }

	       Cell R72cell4 = row.createCell(5);
	       if (record.getR72_moreThan6MonthTo12Months() != null) {
	           R72cell4.setCellValue(record.getR72_moreThan6MonthTo12Months().doubleValue());
	           R72cell4.setCellStyle(numberStyle);
	       } else {
	           R72cell4.setCellValue("");
	           R72cell4.setCellStyle(textStyle);
	       }

	       Cell R72cell5 = row.createCell(6);
	       if (record.getR72_moreThan12MonthTo3Years() != null) {
	           R72cell5.setCellValue(record.getR72_moreThan12MonthTo3Years().doubleValue());
	           R72cell5.setCellStyle(numberStyle);
	       } else {
	           R72cell5.setCellValue("");
	           R72cell5.setCellStyle(textStyle);
	       }

	       Cell R72cell6 = row.createCell(7);
	       if (record.getR72_moreThan3YearsTo5Years() != null) {
	           R72cell6.setCellValue(record.getR72_moreThan3YearsTo5Years().doubleValue());
	           R72cell6.setCellStyle(numberStyle);
	       } else {
	           R72cell6.setCellValue("");
	           R72cell6.setCellStyle(textStyle);
	       }

	       Cell R72cell7 = row.createCell(8);
	       if (record.getR72_moreThan5YearsTo10Years() != null) {
	           R72cell7.setCellValue(record.getR72_moreThan5YearsTo10Years().doubleValue());
	           R72cell7.setCellStyle(numberStyle);
	       } else {
	           R72cell7.setCellValue("");
	           R72cell7.setCellStyle(textStyle);
	       }

	       Cell R72cell8 = row.createCell(9);
	       if (record.getR72_moreThan10Years() != null) {
	           R72cell8.setCellValue(record.getR72_moreThan10Years().doubleValue());
	           R72cell8.setCellStyle(numberStyle);
	       } else {
	           R72cell8.setCellValue("");
	           R72cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(72);
	       Cell R73cell1 = row.createCell(2);
	       if (record.getR73_upTo1Month() != null) {
	           R73cell1.setCellValue(record.getR73_upTo1Month().doubleValue());
	           R73cell1.setCellStyle(numberStyle);
	       } else {
	           R73cell1.setCellValue("");
	           R73cell1.setCellStyle(textStyle);
	       }

	       Cell R73cell2 = row.createCell(3);
	       if (record.getR73_moreThan1MonthTo3Months() != null) {
	           R73cell2.setCellValue(record.getR73_moreThan1MonthTo3Months().doubleValue());
	           R73cell2.setCellStyle(numberStyle);
	       } else {
	           R73cell2.setCellValue("");
	           R73cell2.setCellStyle(textStyle);
	       }

	       Cell R73cell3 = row.createCell(4);
	       if (record.getR73_moreThan3MonthTo6Months() != null) {
	           R73cell3.setCellValue(record.getR73_moreThan3MonthTo6Months().doubleValue());
	           R73cell3.setCellStyle(numberStyle);
	       } else {
	           R73cell3.setCellValue("");
	           R73cell3.setCellStyle(textStyle);
	       }

	       Cell R73cell4 = row.createCell(5);
	       if (record.getR73_moreThan6MonthTo12Months() != null) {
	           R73cell4.setCellValue(record.getR73_moreThan6MonthTo12Months().doubleValue());
	           R73cell4.setCellStyle(numberStyle);
	       } else {
	           R73cell4.setCellValue("");
	           R73cell4.setCellStyle(textStyle);
	       }

	       Cell R73cell5 = row.createCell(6);
	       if (record.getR73_moreThan12MonthTo3Years() != null) {
	           R73cell5.setCellValue(record.getR73_moreThan12MonthTo3Years().doubleValue());
	           R73cell5.setCellStyle(numberStyle);
	       } else {
	           R73cell5.setCellValue("");
	           R73cell5.setCellStyle(textStyle);
	       }

	       Cell R73cell6 = row.createCell(7);
	       if (record.getR73_moreThan3YearsTo5Years() != null) {
	           R73cell6.setCellValue(record.getR73_moreThan3YearsTo5Years().doubleValue());
	           R73cell6.setCellStyle(numberStyle);
	       } else {
	           R73cell6.setCellValue("");
	           R73cell6.setCellStyle(textStyle);
	       }

	       Cell R73cell7 = row.createCell(8);
	       if (record.getR73_moreThan5YearsTo10Years() != null) {
	           R73cell7.setCellValue(record.getR73_moreThan5YearsTo10Years().doubleValue());
	           R73cell7.setCellStyle(numberStyle);
	       } else {
	           R73cell7.setCellValue("");
	           R73cell7.setCellStyle(textStyle);
	       }

	       Cell R73cell8 = row.createCell(9);
	       if (record.getR73_moreThan10Years() != null) {
	           R73cell8.setCellValue(record.getR73_moreThan10Years().doubleValue());
	           R73cell8.setCellStyle(numberStyle);
	       } else {
	           R73cell8.setCellValue("");
	           R73cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(73);
	       Cell R74cell1 = row.createCell(2);
	       if (record.getR74_upTo1Month() != null) {
	           R74cell1.setCellValue(record.getR74_upTo1Month().doubleValue());
	           R74cell1.setCellStyle(numberStyle);
	       } else {
	           R74cell1.setCellValue("");
	           R74cell1.setCellStyle(textStyle);
	       }

	       Cell R74cell2 = row.createCell(3);
	       if (record.getR74_moreThan1MonthTo3Months() != null) {
	           R74cell2.setCellValue(record.getR74_moreThan1MonthTo3Months().doubleValue());
	           R74cell2.setCellStyle(numberStyle);
	       } else {
	           R74cell2.setCellValue("");
	           R74cell2.setCellStyle(textStyle);
	       }

	       Cell R74cell3 = row.createCell(4);
	       if (record.getR74_moreThan3MonthTo6Months() != null) {
	           R74cell3.setCellValue(record.getR74_moreThan3MonthTo6Months().doubleValue());
	           R74cell3.setCellStyle(numberStyle);
	       } else {
	           R74cell3.setCellValue("");
	           R74cell3.setCellStyle(textStyle);
	       }

	       Cell R74cell4 = row.createCell(5);
	       if (record.getR74_moreThan6MonthTo12Months() != null) {
	           R74cell4.setCellValue(record.getR74_moreThan6MonthTo12Months().doubleValue());
	           R74cell4.setCellStyle(numberStyle);
	       } else {
	           R74cell4.setCellValue("");
	           R74cell4.setCellStyle(textStyle);
	       }

	       Cell R74cell5 = row.createCell(6);
	       if (record.getR74_moreThan12MonthTo3Years() != null) {
	           R74cell5.setCellValue(record.getR74_moreThan12MonthTo3Years().doubleValue());
	           R74cell5.setCellStyle(numberStyle);
	       } else {
	           R74cell5.setCellValue("");
	           R74cell5.setCellStyle(textStyle);
	       }

	       Cell R74cell6 = row.createCell(7);
	       if (record.getR74_moreThan3YearsTo5Years() != null) {
	           R74cell6.setCellValue(record.getR74_moreThan3YearsTo5Years().doubleValue());
	           R74cell6.setCellStyle(numberStyle);
	       } else {
	           R74cell6.setCellValue("");
	           R74cell6.setCellStyle(textStyle);
	       }

	       Cell R74cell7 = row.createCell(8);
	       if (record.getR74_moreThan5YearsTo10Years() != null) {
	           R74cell7.setCellValue(record.getR74_moreThan5YearsTo10Years().doubleValue());
	           R74cell7.setCellStyle(numberStyle);
	       } else {
	           R74cell7.setCellValue("");
	           R74cell7.setCellStyle(textStyle);
	       }

	       Cell R74cell8 = row.createCell(9);
	       if (record.getR74_moreThan10Years() != null) {
	           R74cell8.setCellValue(record.getR74_moreThan10Years().doubleValue());
	           R74cell8.setCellStyle(numberStyle);
	       } else {
	           R74cell8.setCellValue("");
	           R74cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(74);
	       Cell R75cell1 = row.createCell(2);
	       if (record.getR75_upTo1Month() != null) {
	           R75cell1.setCellValue(record.getR75_upTo1Month().doubleValue());
	           R75cell1.setCellStyle(numberStyle);
	       } else {
	           R75cell1.setCellValue("");
	           R75cell1.setCellStyle(textStyle);
	       }

	       Cell R75cell2 = row.createCell(3);
	       if (record.getR75_moreThan1MonthTo3Months() != null) {
	           R75cell2.setCellValue(record.getR75_moreThan1MonthTo3Months().doubleValue());
	           R75cell2.setCellStyle(numberStyle);
	       } else {
	           R75cell2.setCellValue("");
	           R75cell2.setCellStyle(textStyle);
	       }

	       Cell R75cell3 = row.createCell(4);
	       if (record.getR75_moreThan3MonthTo6Months() != null) {
	           R75cell3.setCellValue(record.getR75_moreThan3MonthTo6Months().doubleValue());
	           R75cell3.setCellStyle(numberStyle);
	       } else {
	           R75cell3.setCellValue("");
	           R75cell3.setCellStyle(textStyle);
	       }

	       Cell R75cell4 = row.createCell(5);
	       if (record.getR75_moreThan6MonthTo12Months() != null) {
	           R75cell4.setCellValue(record.getR75_moreThan6MonthTo12Months().doubleValue());
	           R75cell4.setCellStyle(numberStyle);
	       } else {
	           R75cell4.setCellValue("");
	           R75cell4.setCellStyle(textStyle);
	       }

	       Cell R75cell5 = row.createCell(6);
	       if (record.getR75_moreThan12MonthTo3Years() != null) {
	           R75cell5.setCellValue(record.getR75_moreThan12MonthTo3Years().doubleValue());
	           R75cell5.setCellStyle(numberStyle);
	       } else {
	           R75cell5.setCellValue("");
	           R75cell5.setCellStyle(textStyle);
	       }

	       Cell R75cell6 = row.createCell(7);
	       if (record.getR75_moreThan3YearsTo5Years() != null) {
	           R75cell6.setCellValue(record.getR75_moreThan3YearsTo5Years().doubleValue());
	           R75cell6.setCellStyle(numberStyle);
	       } else {
	           R75cell6.setCellValue("");
	           R75cell6.setCellStyle(textStyle);
	       }

	       Cell R75cell7 = row.createCell(8);
	       if (record.getR75_moreThan5YearsTo10Years() != null) {
	           R75cell7.setCellValue(record.getR75_moreThan5YearsTo10Years().doubleValue());
	           R75cell7.setCellStyle(numberStyle);
	       } else {
	           R75cell7.setCellValue("");
	           R75cell7.setCellStyle(textStyle);
	       }

	       Cell R75cell8 = row.createCell(9);
	       if (record.getR75_moreThan10Years() != null) {
	           R75cell8.setCellValue(record.getR75_moreThan10Years().doubleValue());
	           R75cell8.setCellStyle(numberStyle);
	       } else {
	           R75cell8.setCellValue("");
	           R75cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(76);
	       Cell R77cell1 = row.createCell(2);
	       if (record.getR77_upTo1Month() != null) {
	           R77cell1.setCellValue(record.getR77_upTo1Month().doubleValue());
	           R77cell1.setCellStyle(numberStyle);
	       } else {
	           R77cell1.setCellValue("");
	           R77cell1.setCellStyle(textStyle);
	       }

	       Cell R77cell2 = row.createCell(3);
	       if (record.getR77_moreThan1MonthTo3Months() != null) {
	           R77cell2.setCellValue(record.getR77_moreThan1MonthTo3Months().doubleValue());
	           R77cell2.setCellStyle(numberStyle);
	       } else {
	           R77cell2.setCellValue("");
	           R77cell2.setCellStyle(textStyle);
	       }

	       Cell R77cell3 = row.createCell(4);
	       if (record.getR77_moreThan3MonthTo6Months() != null) {
	           R77cell3.setCellValue(record.getR77_moreThan3MonthTo6Months().doubleValue());
	           R77cell3.setCellStyle(numberStyle);
	       } else {
	           R77cell3.setCellValue("");
	           R77cell3.setCellStyle(textStyle);
	       }

	       Cell R77cell4 = row.createCell(5);
	       if (record.getR77_moreThan6MonthTo12Months() != null) {
	           R77cell4.setCellValue(record.getR77_moreThan6MonthTo12Months().doubleValue());
	           R77cell4.setCellStyle(numberStyle);
	       } else {
	           R77cell4.setCellValue("");
	           R77cell4.setCellStyle(textStyle);
	       }

	       Cell R77cell5 = row.createCell(6);
	       if (record.getR77_moreThan12MonthTo3Years() != null) {
	           R77cell5.setCellValue(record.getR77_moreThan12MonthTo3Years().doubleValue());
	           R77cell5.setCellStyle(numberStyle);
	       } else {
	           R77cell5.setCellValue("");
	           R77cell5.setCellStyle(textStyle);
	       }

	       Cell R77cell6 = row.createCell(7);
	       if (record.getR77_moreThan3YearsTo5Years() != null) {
	           R77cell6.setCellValue(record.getR77_moreThan3YearsTo5Years().doubleValue());
	           R77cell6.setCellStyle(numberStyle);
	       } else {
	           R77cell6.setCellValue("");
	           R77cell6.setCellStyle(textStyle);
	       }

	       Cell R77cell7 = row.createCell(8);
	       if (record.getR77_moreThan5YearsTo10Years() != null) {
	           R77cell7.setCellValue(record.getR77_moreThan5YearsTo10Years().doubleValue());
	           R77cell7.setCellStyle(numberStyle);
	       } else {
	           R77cell7.setCellValue("");
	           R77cell7.setCellStyle(textStyle);
	       }

	       Cell R77cell8 = row.createCell(9);
	       if (record.getR77_moreThan10Years() != null) {
	           R77cell8.setCellValue(record.getR77_moreThan10Years().doubleValue());
	           R77cell8.setCellStyle(numberStyle);
	       } else {
	           R77cell8.setCellValue("");
	           R77cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(77);
	       Cell R78cell1 = row.createCell(2);
	       if (record.getR78_upTo1Month() != null) {
	           R78cell1.setCellValue(record.getR78_upTo1Month().doubleValue());
	           R78cell1.setCellStyle(numberStyle);
	       } else {
	           R78cell1.setCellValue("");
	           R78cell1.setCellStyle(textStyle);
	       }

	       Cell R78cell2 = row.createCell(3);
	       if (record.getR78_moreThan1MonthTo3Months() != null) {
	           R78cell2.setCellValue(record.getR78_moreThan1MonthTo3Months().doubleValue());
	           R78cell2.setCellStyle(numberStyle);
	       } else {
	           R78cell2.setCellValue("");
	           R78cell2.setCellStyle(textStyle);
	       }

	       Cell R78cell3 = row.createCell(4);
	       if (record.getR78_moreThan3MonthTo6Months() != null) {
	           R78cell3.setCellValue(record.getR78_moreThan3MonthTo6Months().doubleValue());
	           R78cell3.setCellStyle(numberStyle);
	       } else {
	           R78cell3.setCellValue("");
	           R78cell3.setCellStyle(textStyle);
	       }

	       Cell R78cell4 = row.createCell(5);
	       if (record.getR78_moreThan6MonthTo12Months() != null) {
	           R78cell4.setCellValue(record.getR78_moreThan6MonthTo12Months().doubleValue());
	           R78cell4.setCellStyle(numberStyle);
	       } else {
	           R78cell4.setCellValue("");
	           R78cell4.setCellStyle(textStyle);
	       }

	       Cell R78cell5 = row.createCell(6);
	       if (record.getR78_moreThan12MonthTo3Years() != null) {
	           R78cell5.setCellValue(record.getR78_moreThan12MonthTo3Years().doubleValue());
	           R78cell5.setCellStyle(numberStyle);
	       } else {
	           R78cell5.setCellValue("");
	           R78cell5.setCellStyle(textStyle);
	       }

	       Cell R78cell6 = row.createCell(7);
	       if (record.getR78_moreThan3YearsTo5Years() != null) {
	           R78cell6.setCellValue(record.getR78_moreThan3YearsTo5Years().doubleValue());
	           R78cell6.setCellStyle(numberStyle);
	       } else {
	           R78cell6.setCellValue("");
	           R78cell6.setCellStyle(textStyle);
	       }

	       Cell R78cell7 = row.createCell(8);
	       if (record.getR78_moreThan5YearsTo10Years() != null) {
	           R78cell7.setCellValue(record.getR78_moreThan5YearsTo10Years().doubleValue());
	           R78cell7.setCellStyle(numberStyle);
	       } else {
	           R78cell7.setCellValue("");
	           R78cell7.setCellStyle(textStyle);
	       }

	       Cell R78cell8 = row.createCell(9);
	       if (record.getR78_moreThan10Years() != null) {
	           R78cell8.setCellValue(record.getR78_moreThan10Years().doubleValue());
	           R78cell8.setCellStyle(numberStyle);
	       } else {
	           R78cell8.setCellValue("");
	           R78cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(78);
	       Cell R79cell1 = row.createCell(2);
	       if (record.getR79_upTo1Month() != null) {
	           R79cell1.setCellValue(record.getR79_upTo1Month().doubleValue());
	           R79cell1.setCellStyle(numberStyle);
	       } else {
	           R79cell1.setCellValue("");
	           R79cell1.setCellStyle(textStyle);
	       }

	       Cell R79cell2 = row.createCell(3);
	       if (record.getR79_moreThan1MonthTo3Months() != null) {
	           R79cell2.setCellValue(record.getR79_moreThan1MonthTo3Months().doubleValue());
	           R79cell2.setCellStyle(numberStyle);
	       } else {
	           R79cell2.setCellValue("");
	           R79cell2.setCellStyle(textStyle);
	       }

	       Cell R79cell3 = row.createCell(4);
	       if (record.getR79_moreThan3MonthTo6Months() != null) {
	           R79cell3.setCellValue(record.getR79_moreThan3MonthTo6Months().doubleValue());
	           R79cell3.setCellStyle(numberStyle);
	       } else {
	           R79cell3.setCellValue("");
	           R79cell3.setCellStyle(textStyle);
	       }

	       Cell R79cell4 = row.createCell(5);
	       if (record.getR79_moreThan6MonthTo12Months() != null) {
	           R79cell4.setCellValue(record.getR79_moreThan6MonthTo12Months().doubleValue());
	           R79cell4.setCellStyle(numberStyle);
	       } else {
	           R79cell4.setCellValue("");
	           R79cell4.setCellStyle(textStyle);
	       }

	       Cell R79cell5 = row.createCell(6);
	       if (record.getR79_moreThan12MonthTo3Years() != null) {
	           R79cell5.setCellValue(record.getR79_moreThan12MonthTo3Years().doubleValue());
	           R79cell5.setCellStyle(numberStyle);
	       } else {
	           R79cell5.setCellValue("");
	           R79cell5.setCellStyle(textStyle);
	       }

	       Cell R79cell6 = row.createCell(7);
	       if (record.getR79_moreThan3YearsTo5Years() != null) {
	           R79cell6.setCellValue(record.getR79_moreThan3YearsTo5Years().doubleValue());
	           R79cell6.setCellStyle(numberStyle);
	       } else {
	           R79cell6.setCellValue("");
	           R79cell6.setCellStyle(textStyle);
	       }

	       Cell R79cell7 = row.createCell(8);
	       if (record.getR79_moreThan5YearsTo10Years() != null) {
	           R79cell7.setCellValue(record.getR79_moreThan5YearsTo10Years().doubleValue());
	           R79cell7.setCellStyle(numberStyle);
	       } else {
	           R79cell7.setCellValue("");
	           R79cell7.setCellStyle(textStyle);
	       }

	       Cell R79cell8 = row.createCell(9);
	       if (record.getR79_moreThan10Years() != null) {
	           R79cell8.setCellValue(record.getR79_moreThan10Years().doubleValue());
	           R79cell8.setCellStyle(numberStyle);
	       } else {
	           R79cell8.setCellValue("");
	           R79cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(79);
	       Cell R80cell1 = row.createCell(2);
	       if (record.getR80_upTo1Month() != null) {
	           R80cell1.setCellValue(record.getR80_upTo1Month().doubleValue());
	           R80cell1.setCellStyle(numberStyle);
	       } else {
	           R80cell1.setCellValue("");
	           R80cell1.setCellStyle(textStyle);
	       }

	       Cell R80cell2 = row.createCell(3);
	       if (record.getR80_moreThan1MonthTo3Months() != null) {
	           R80cell2.setCellValue(record.getR80_moreThan1MonthTo3Months().doubleValue());
	           R80cell2.setCellStyle(numberStyle);
	       } else {
	           R80cell2.setCellValue("");
	           R80cell2.setCellStyle(textStyle);
	       }

	       Cell R80cell3 = row.createCell(4);
	       if (record.getR80_moreThan3MonthTo6Months() != null) {
	           R80cell3.setCellValue(record.getR80_moreThan3MonthTo6Months().doubleValue());
	           R80cell3.setCellStyle(numberStyle);
	       } else {
	           R80cell3.setCellValue("");
	           R80cell3.setCellStyle(textStyle);
	       }

	       Cell R80cell4 = row.createCell(5);
	       if (record.getR80_moreThan6MonthTo12Months() != null) {
	           R80cell4.setCellValue(record.getR80_moreThan6MonthTo12Months().doubleValue());
	           R80cell4.setCellStyle(numberStyle);
	       } else {
	           R80cell4.setCellValue("");
	           R80cell4.setCellStyle(textStyle);
	       }

	       Cell R80cell5 = row.createCell(6);
	       if (record.getR80_moreThan12MonthTo3Years() != null) {
	           R80cell5.setCellValue(record.getR80_moreThan12MonthTo3Years().doubleValue());
	           R80cell5.setCellStyle(numberStyle);
	       } else {
	           R80cell5.setCellValue("");
	           R80cell5.setCellStyle(textStyle);
	       }

	       Cell R80cell6 = row.createCell(7);
	       if (record.getR80_moreThan3YearsTo5Years() != null) {
	           R80cell6.setCellValue(record.getR80_moreThan3YearsTo5Years().doubleValue());
	           R80cell6.setCellStyle(numberStyle);
	       } else {
	           R80cell6.setCellValue("");
	           R80cell6.setCellStyle(textStyle);
	       }

	       Cell R80cell7 = row.createCell(8);
	       if (record.getR80_moreThan5YearsTo10Years() != null) {
	           R80cell7.setCellValue(record.getR80_moreThan5YearsTo10Years().doubleValue());
	           R80cell7.setCellStyle(numberStyle);
	       } else {
	           R80cell7.setCellValue("");
	           R80cell7.setCellStyle(textStyle);
	       }

	       Cell R80cell8 = row.createCell(9);
	       if (record.getR80_moreThan10Years() != null) {
	           R80cell8.setCellValue(record.getR80_moreThan10Years().doubleValue());
	           R80cell8.setCellStyle(numberStyle);
	       } else {
	           R80cell8.setCellValue("");
	           R80cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(80);
	       Cell R81cell1 = row.createCell(2);
	       if (record.getR81_upTo1Month() != null) {
	           R81cell1.setCellValue(record.getR81_upTo1Month().doubleValue());
	           R81cell1.setCellStyle(numberStyle);
	       } else {
	           R81cell1.setCellValue("");
	           R81cell1.setCellStyle(textStyle);
	       }

	       Cell R81cell2 = row.createCell(3);
	       if (record.getR81_moreThan1MonthTo3Months() != null) {
	           R81cell2.setCellValue(record.getR81_moreThan1MonthTo3Months().doubleValue());
	           R81cell2.setCellStyle(numberStyle);
	       } else {
	           R81cell2.setCellValue("");
	           R81cell2.setCellStyle(textStyle);
	       }

	       Cell R81cell3 = row.createCell(4);
	       if (record.getR81_moreThan3MonthTo6Months() != null) {
	           R81cell3.setCellValue(record.getR81_moreThan3MonthTo6Months().doubleValue());
	           R81cell3.setCellStyle(numberStyle);
	       } else {
	           R81cell3.setCellValue("");
	           R81cell3.setCellStyle(textStyle);
	       }

	       Cell R81cell4 = row.createCell(5);
	       if (record.getR81_moreThan6MonthTo12Months() != null) {
	           R81cell4.setCellValue(record.getR81_moreThan6MonthTo12Months().doubleValue());
	           R81cell4.setCellStyle(numberStyle);
	       } else {
	           R81cell4.setCellValue("");
	           R81cell4.setCellStyle(textStyle);
	       }

	       Cell R81cell5 = row.createCell(6);
	       if (record.getR81_moreThan12MonthTo3Years() != null) {
	           R81cell5.setCellValue(record.getR81_moreThan12MonthTo3Years().doubleValue());
	           R81cell5.setCellStyle(numberStyle);
	       } else {
	           R81cell5.setCellValue("");
	           R81cell5.setCellStyle(textStyle);
	       }

	       Cell R81cell6 = row.createCell(7);
	       if (record.getR81_moreThan3YearsTo5Years() != null) {
	           R81cell6.setCellValue(record.getR81_moreThan3YearsTo5Years().doubleValue());
	           R81cell6.setCellStyle(numberStyle);
	       } else {
	           R81cell6.setCellValue("");
	           R81cell6.setCellStyle(textStyle);
	       }

	       Cell R81cell7 = row.createCell(8);
	       if (record.getR81_moreThan5YearsTo10Years() != null) {
	           R81cell7.setCellValue(record.getR81_moreThan5YearsTo10Years().doubleValue());
	           R81cell7.setCellStyle(numberStyle);
	       } else {
	           R81cell7.setCellValue("");
	           R81cell7.setCellStyle(textStyle);
	       }

	       Cell R81cell8 = row.createCell(9);
	       if (record.getR81_moreThan10Years() != null) {
	           R81cell8.setCellValue(record.getR81_moreThan10Years().doubleValue());
	           R81cell8.setCellStyle(numberStyle);
	       } else {
	           R81cell8.setCellValue("");
	           R81cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(81);
	       Cell R82cell1 = row.createCell(2);
	       if (record.getR82_upTo1Month() != null) {
	           R82cell1.setCellValue(record.getR82_upTo1Month().doubleValue());
	           R82cell1.setCellStyle(numberStyle);
	       } else {
	           R82cell1.setCellValue("");
	           R82cell1.setCellStyle(textStyle);
	       }

	       Cell R82cell2 = row.createCell(3);
	       if (record.getR82_moreThan1MonthTo3Months() != null) {
	           R82cell2.setCellValue(record.getR82_moreThan1MonthTo3Months().doubleValue());
	           R82cell2.setCellStyle(numberStyle);
	       } else {
	           R82cell2.setCellValue("");
	           R82cell2.setCellStyle(textStyle);
	       }

	       Cell R82cell3 = row.createCell(4);
	       if (record.getR82_moreThan3MonthTo6Months() != null) {
	           R82cell3.setCellValue(record.getR82_moreThan3MonthTo6Months().doubleValue());
	           R82cell3.setCellStyle(numberStyle);
	       } else {
	           R82cell3.setCellValue("");
	           R82cell3.setCellStyle(textStyle);
	       }

	       Cell R82cell4 = row.createCell(5);
	       if (record.getR82_moreThan6MonthTo12Months() != null) {
	           R82cell4.setCellValue(record.getR82_moreThan6MonthTo12Months().doubleValue());
	           R82cell4.setCellStyle(numberStyle);
	       } else {
	           R82cell4.setCellValue("");
	           R82cell4.setCellStyle(textStyle);
	       }

	       Cell R82cell5 = row.createCell(6);
	       if (record.getR82_moreThan12MonthTo3Years() != null) {
	           R82cell5.setCellValue(record.getR82_moreThan12MonthTo3Years().doubleValue());
	           R82cell5.setCellStyle(numberStyle);
	       } else {
	           R82cell5.setCellValue("");
	           R82cell5.setCellStyle(textStyle);
	       }

	       Cell R82cell6 = row.createCell(7);
	       if (record.getR82_moreThan3YearsTo5Years() != null) {
	           R82cell6.setCellValue(record.getR82_moreThan3YearsTo5Years().doubleValue());
	           R82cell6.setCellStyle(numberStyle);
	       } else {
	           R82cell6.setCellValue("");
	           R82cell6.setCellStyle(textStyle);
	       }

	       Cell R82cell7 = row.createCell(8);
	       if (record.getR82_moreThan5YearsTo10Years() != null) {
	           R82cell7.setCellValue(record.getR82_moreThan5YearsTo10Years().doubleValue());
	           R82cell7.setCellStyle(numberStyle);
	       } else {
	           R82cell7.setCellValue("");
	           R82cell7.setCellStyle(textStyle);
	       }

	       Cell R82cell8 = row.createCell(9);
	       if (record.getR82_moreThan10Years() != null) {
	           R82cell8.setCellValue(record.getR82_moreThan10Years().doubleValue());
	           R82cell8.setCellStyle(numberStyle);
	       } else {
	           R82cell8.setCellValue("");
	           R82cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(82);
	       Cell R83cell1 = row.createCell(2);
	       if (record.getR83_upTo1Month() != null) {
	           R83cell1.setCellValue(record.getR83_upTo1Month().doubleValue());
	           R83cell1.setCellStyle(numberStyle);
	       } else {
	           R83cell1.setCellValue("");
	           R83cell1.setCellStyle(textStyle);
	       }

	       Cell R83cell2 = row.createCell(3);
	       if (record.getR83_moreThan1MonthTo3Months() != null) {
	           R83cell2.setCellValue(record.getR83_moreThan1MonthTo3Months().doubleValue());
	           R83cell2.setCellStyle(numberStyle);
	       } else {
	           R83cell2.setCellValue("");
	           R83cell2.setCellStyle(textStyle);
	       }

	       Cell R83cell3 = row.createCell(4);
	       if (record.getR83_moreThan3MonthTo6Months() != null) {
	           R83cell3.setCellValue(record.getR83_moreThan3MonthTo6Months().doubleValue());
	           R83cell3.setCellStyle(numberStyle);
	       } else {
	           R83cell3.setCellValue("");
	           R83cell3.setCellStyle(textStyle);
	       }

	       Cell R83cell4 = row.createCell(5);
	       if (record.getR83_moreThan6MonthTo12Months() != null) {
	           R83cell4.setCellValue(record.getR83_moreThan6MonthTo12Months().doubleValue());
	           R83cell4.setCellStyle(numberStyle);
	       } else {
	           R83cell4.setCellValue("");
	           R83cell4.setCellStyle(textStyle);
	       }

	       Cell R83cell5 = row.createCell(6);
	       if (record.getR83_moreThan12MonthTo3Years() != null) {
	           R83cell5.setCellValue(record.getR83_moreThan12MonthTo3Years().doubleValue());
	           R83cell5.setCellStyle(numberStyle);
	       } else {
	           R83cell5.setCellValue("");
	           R83cell5.setCellStyle(textStyle);
	       }

	       Cell R83cell6 = row.createCell(7);
	       if (record.getR83_moreThan3YearsTo5Years() != null) {
	           R83cell6.setCellValue(record.getR83_moreThan3YearsTo5Years().doubleValue());
	           R83cell6.setCellStyle(numberStyle);
	       } else {
	           R83cell6.setCellValue("");
	           R83cell6.setCellStyle(textStyle);
	       }

	       Cell R83cell7 = row.createCell(8);
	       if (record.getR83_moreThan5YearsTo10Years() != null) {
	           R83cell7.setCellValue(record.getR83_moreThan5YearsTo10Years().doubleValue());
	           R83cell7.setCellStyle(numberStyle);
	       } else {
	           R83cell7.setCellValue("");
	           R83cell7.setCellStyle(textStyle);
	       }

	       Cell R83cell8 = row.createCell(9);
	       if (record.getR83_moreThan10Years() != null) {
	           R83cell8.setCellValue(record.getR83_moreThan10Years().doubleValue());
	           R83cell8.setCellStyle(numberStyle);
	       } else {
	           R83cell8.setCellValue("");
	           R83cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(84);
	       Cell R85cell9 = row.createCell(10);
	       if (record.getR85_nonRatioSensativeItems() != null) {
	           R85cell9.setCellValue(record.getR85_nonRatioSensativeItems().doubleValue());
	           R85cell9.setCellStyle(numberStyle);
	       } else {
	           R85cell9.setCellValue("");
	           R85cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(85);
	       Cell R86cell9 = row.createCell(10);
	       if (record.getR86_nonRatioSensativeItems() != null) {
	           R86cell9.setCellValue(record.getR86_nonRatioSensativeItems().doubleValue());
	           R86cell9.setCellStyle(numberStyle);
	       } else {
	           R86cell9.setCellValue("");
	           R86cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(86);
	       Cell R87cell9 = row.createCell(10);
	       if (record.getR87_nonRatioSensativeItems() != null) {
	           R87cell9.setCellValue(record.getR87_nonRatioSensativeItems().doubleValue());
	           R87cell9.setCellStyle(numberStyle);
	       } else {
	           R87cell9.setCellValue("");
	           R87cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(87);
	       Cell R88cell9 = row.createCell(10);
	       if (record.getR88_nonRatioSensativeItems() != null) {
	           R88cell9.setCellValue(record.getR88_nonRatioSensativeItems().doubleValue());
	           R88cell9.setCellStyle(numberStyle);
	       } else {
	           R88cell9.setCellValue("");
	           R88cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(88);
	       Cell R89cell9 = row.createCell(10);
	       if (record.getR89_nonRatioSensativeItems() != null) {
	           R89cell9.setCellValue(record.getR89_nonRatioSensativeItems().doubleValue());
	           R89cell9.setCellStyle(numberStyle);
	       } else {
	           R89cell9.setCellValue("");
	           R89cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(89);
	       Cell R90cell9 = row.createCell(10);
	       if (record.getR90_nonRatioSensativeItems() != null) {
	           R90cell9.setCellValue(record.getR90_nonRatioSensativeItems().doubleValue());
	           R90cell9.setCellStyle(numberStyle);
	       } else {
	           R90cell9.setCellValue("");
	           R90cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(90);
	       Cell R91cell9 = row.createCell(10);
	       if (record.getR91_nonRatioSensativeItems() != null) {
	           R91cell9.setCellValue(record.getR91_nonRatioSensativeItems().doubleValue());
	           R91cell9.setCellStyle(numberStyle);
	       } else {
	           R91cell9.setCellValue("");
	           R91cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(95);
	       Cell R96cell1 = row.createCell(2);
	       if (record.getR96_upTo1Month() != null) {
	           R96cell1.setCellValue(record.getR96_upTo1Month().doubleValue());
	           R96cell1.setCellStyle(numberStyle);
	       } else {
	           R96cell1.setCellValue("");
	           R96cell1.setCellStyle(textStyle);
	       }

	       Cell R96cell2 = row.createCell(3);
	       if (record.getR96_moreThan1MonthTo3Months() != null) {
	           R96cell2.setCellValue(record.getR96_moreThan1MonthTo3Months().doubleValue());
	           R96cell2.setCellStyle(numberStyle);
	       } else {
	           R96cell2.setCellValue("");
	           R96cell2.setCellStyle(textStyle);
	       }

	       Cell R96cell3 = row.createCell(4);
	       if (record.getR96_moreThan3MonthTo6Months() != null) {
	           R96cell3.setCellValue(record.getR96_moreThan3MonthTo6Months().doubleValue());
	           R96cell3.setCellStyle(numberStyle);
	       } else {
	           R96cell3.setCellValue("");
	           R96cell3.setCellStyle(textStyle);
	       }

	       Cell R96cell4 = row.createCell(5);
	       if (record.getR96_moreThan6MonthTo12Months() != null) {
	           R96cell4.setCellValue(record.getR96_moreThan6MonthTo12Months().doubleValue());
	           R96cell4.setCellStyle(numberStyle);
	       } else {
	           R96cell4.setCellValue("");
	           R96cell4.setCellStyle(textStyle);
	       }

	       Cell R96cell5 = row.createCell(6);
	       if (record.getR96_moreThan12MonthTo3Years() != null) {
	           R96cell5.setCellValue(record.getR96_moreThan12MonthTo3Years().doubleValue());
	           R96cell5.setCellStyle(numberStyle);
	       } else {
	           R96cell5.setCellValue("");
	           R96cell5.setCellStyle(textStyle);
	       }

	       Cell R96cell6 = row.createCell(7);
	       if (record.getR96_moreThan3YearsTo5Years() != null) {
	           R96cell6.setCellValue(record.getR96_moreThan3YearsTo5Years().doubleValue());
	           R96cell6.setCellStyle(numberStyle);
	       } else {
	           R96cell6.setCellValue("");
	           R96cell6.setCellStyle(textStyle);
	       }

	       Cell R96cell7 = row.createCell(8);
	       if (record.getR96_moreThan5YearsTo10Years() != null) {
	           R96cell7.setCellValue(record.getR96_moreThan5YearsTo10Years().doubleValue());
	           R96cell7.setCellStyle(numberStyle);
	       } else {
	           R96cell7.setCellValue("");
	           R96cell7.setCellStyle(textStyle);
	       }

	       Cell R96cell8 = row.createCell(9);
	       if (record.getR96_moreThan10Years() != null) {
	           R96cell8.setCellValue(record.getR96_moreThan10Years().doubleValue());
	           R96cell8.setCellStyle(numberStyle);
	       } else {
	           R96cell8.setCellValue("");
	           R96cell8.setCellStyle(textStyle);
	       }

	       Cell R96cell9 = row.createCell(10);
	       if (record.getR96_nonRatioSensativeItems() != null) {
	           R96cell9.setCellValue(record.getR96_nonRatioSensativeItems().doubleValue());
	           R96cell9.setCellStyle(numberStyle);
	       } else {
	           R96cell9.setCellValue("");
	           R96cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(99);
	       Cell R100cell1 = row.createCell(2);
	       if (record.getR100_upTo1Month() != null) {
	           R100cell1.setCellValue(record.getR100_upTo1Month().doubleValue());
	           R100cell1.setCellStyle(numberStyle);
	       } else {
	           R100cell1.setCellValue("");
	           R100cell1.setCellStyle(textStyle);
	       }

	       Cell R100cell2 = row.createCell(3);
	       if (record.getR100_moreThan1MonthTo3Months() != null) {
	           R100cell2.setCellValue(record.getR100_moreThan1MonthTo3Months().doubleValue());
	           R100cell2.setCellStyle(numberStyle);
	       } else {
	           R100cell2.setCellValue("");
	           R100cell2.setCellStyle(textStyle);
	       }

	       Cell R100cell3 = row.createCell(4);
	       if (record.getR100_moreThan3MonthTo6Months() != null) {
	           R100cell3.setCellValue(record.getR100_moreThan3MonthTo6Months().doubleValue());
	           R100cell3.setCellStyle(numberStyle);
	       } else {
	           R100cell3.setCellValue("");
	           R100cell3.setCellStyle(textStyle);
	       }

	       Cell R100cell4 = row.createCell(5);
	       if (record.getR100_moreThan6MonthTo12Months() != null) {
	           R100cell4.setCellValue(record.getR100_moreThan6MonthTo12Months().doubleValue());
	           R100cell4.setCellStyle(numberStyle);
	       } else {
	           R100cell4.setCellValue("");
	           R100cell4.setCellStyle(textStyle);
	       }

	       Cell R100cell5 = row.createCell(6);
	       if (record.getR100_moreThan12MonthTo3Years() != null) {
	           R100cell5.setCellValue(record.getR100_moreThan12MonthTo3Years().doubleValue());
	           R100cell5.setCellStyle(numberStyle);
	       } else {
	           R100cell5.setCellValue("");
	           R100cell5.setCellStyle(textStyle);
	       }

	       Cell R100cell6 = row.createCell(7);
	       if (record.getR100_moreThan3YearsTo5Years() != null) {
	           R100cell6.setCellValue(record.getR100_moreThan3YearsTo5Years().doubleValue());
	           R100cell6.setCellStyle(numberStyle);
	       } else {
	           R100cell6.setCellValue("");
	           R100cell6.setCellStyle(textStyle);
	       }

	       Cell R100cell7 = row.createCell(8);
	       if (record.getR100_moreThan5YearsTo10Years() != null) {
	           R100cell7.setCellValue(record.getR100_moreThan5YearsTo10Years().doubleValue());
	           R100cell7.setCellStyle(numberStyle);
	       } else {
	           R100cell7.setCellValue("");
	           R100cell7.setCellStyle(textStyle);
	       }

	       Cell R100cell8 = row.createCell(9);
	       if (record.getR100_moreThan10Years() != null) {
	           R100cell8.setCellValue(record.getR100_moreThan10Years().doubleValue());
	           R100cell8.setCellStyle(numberStyle);
	       } else {
	           R100cell8.setCellValue("");
	           R100cell8.setCellStyle(textStyle);
	       }

	       Cell R100cell9 = row.createCell(10);
	       if (record.getR100_nonRatioSensativeItems() != null) {
	           R100cell9.setCellValue(record.getR100_nonRatioSensativeItems().doubleValue());
	           R100cell9.setCellStyle(numberStyle);
	       } else {
	           R100cell9.setCellValue("");
	           R100cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(100);
	       Cell R101cell1 = row.createCell(2);
	       if (record.getR101_upTo1Month() != null) {
	           R101cell1.setCellValue(record.getR101_upTo1Month().doubleValue());
	           R101cell1.setCellStyle(numberStyle);
	       } else {
	           R101cell1.setCellValue("");
	           R101cell1.setCellStyle(textStyle);
	       }

	       Cell R101cell2 = row.createCell(3);
	       if (record.getR101_moreThan1MonthTo3Months() != null) {
	           R101cell2.setCellValue(record.getR101_moreThan1MonthTo3Months().doubleValue());
	           R101cell2.setCellStyle(numberStyle);
	       } else {
	           R101cell2.setCellValue("");
	           R101cell2.setCellStyle(textStyle);
	       }

	       Cell R101cell3 = row.createCell(4);
	       if (record.getR101_moreThan3MonthTo6Months() != null) {
	           R101cell3.setCellValue(record.getR101_moreThan3MonthTo6Months().doubleValue());
	           R101cell3.setCellStyle(numberStyle);
	       } else {
	           R101cell3.setCellValue("");
	           R101cell3.setCellStyle(textStyle);
	       }

	       Cell R101cell4 = row.createCell(5);
	       if (record.getR101_moreThan6MonthTo12Months() != null) {
	           R101cell4.setCellValue(record.getR101_moreThan6MonthTo12Months().doubleValue());
	           R101cell4.setCellStyle(numberStyle);
	       } else {
	           R101cell4.setCellValue("");
	           R101cell4.setCellStyle(textStyle);
	       }

	       Cell R101cell5 = row.createCell(6);
	       if (record.getR101_moreThan12MonthTo3Years() != null) {
	           R101cell5.setCellValue(record.getR101_moreThan12MonthTo3Years().doubleValue());
	           R101cell5.setCellStyle(numberStyle);
	       } else {
	           R101cell5.setCellValue("");
	           R101cell5.setCellStyle(textStyle);
	       }

	       Cell R101cell6 = row.createCell(7);
	       if (record.getR101_moreThan3YearsTo5Years() != null) {
	           R101cell6.setCellValue(record.getR101_moreThan3YearsTo5Years().doubleValue());
	           R101cell6.setCellStyle(numberStyle);
	       } else {
	           R101cell6.setCellValue("");
	           R101cell6.setCellStyle(textStyle);
	       }

	       Cell R101cell7 = row.createCell(8);
	       if (record.getR101_moreThan5YearsTo10Years() != null) {
	           R101cell7.setCellValue(record.getR101_moreThan5YearsTo10Years().doubleValue());
	           R101cell7.setCellStyle(numberStyle);
	       } else {
	           R101cell7.setCellValue("");
	           R101cell7.setCellStyle(textStyle);
	       }

	       Cell R101cell8 = row.createCell(9);
	       if (record.getR101_moreThan10Years() != null) {
	           R101cell8.setCellValue(record.getR101_moreThan10Years().doubleValue());
	           R101cell8.setCellStyle(numberStyle);
	       } else {
	           R101cell8.setCellValue("");
	           R101cell8.setCellStyle(textStyle);
	       }

	       Cell R101cell9 = row.createCell(10);
	       if (record.getR101_nonRatioSensativeItems() != null) {
	           R101cell9.setCellValue(record.getR101_nonRatioSensativeItems().doubleValue());
	           R101cell9.setCellStyle(numberStyle);
	       } else {
	           R101cell9.setCellValue("");
	           R101cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(101);
	       Cell R102cell1 = row.createCell(2);
	       if (record.getR102_upTo1Month() != null) {
	           R102cell1.setCellValue(record.getR102_upTo1Month().doubleValue());
	           R102cell1.setCellStyle(numberStyle);
	       } else {
	           R102cell1.setCellValue("");
	           R102cell1.setCellStyle(textStyle);
	       }

	       Cell R102cell2 = row.createCell(3);
	       if (record.getR102_moreThan1MonthTo3Months() != null) {
	           R102cell2.setCellValue(record.getR102_moreThan1MonthTo3Months().doubleValue());
	           R102cell2.setCellStyle(numberStyle);
	       } else {
	           R102cell2.setCellValue("");
	           R102cell2.setCellStyle(textStyle);
	       }

	       Cell R102cell3 = row.createCell(4);
	       if (record.getR102_moreThan3MonthTo6Months() != null) {
	           R102cell3.setCellValue(record.getR102_moreThan3MonthTo6Months().doubleValue());
	           R102cell3.setCellStyle(numberStyle);
	       } else {
	           R102cell3.setCellValue("");
	           R102cell3.setCellStyle(textStyle);
	       }

	       Cell R102cell4 = row.createCell(5);
	       if (record.getR102_moreThan6MonthTo12Months() != null) {
	           R102cell4.setCellValue(record.getR102_moreThan6MonthTo12Months().doubleValue());
	           R102cell4.setCellStyle(numberStyle);
	       } else {
	           R102cell4.setCellValue("");
	           R102cell4.setCellStyle(textStyle);
	       }

	       Cell R102cell5 = row.createCell(6);
	       if (record.getR102_moreThan12MonthTo3Years() != null) {
	           R102cell5.setCellValue(record.getR102_moreThan12MonthTo3Years().doubleValue());
	           R102cell5.setCellStyle(numberStyle);
	       } else {
	           R102cell5.setCellValue("");
	           R102cell5.setCellStyle(textStyle);
	       }

	       Cell R102cell6 = row.createCell(7);
	       if (record.getR102_moreThan3YearsTo5Years() != null) {
	           R102cell6.setCellValue(record.getR102_moreThan3YearsTo5Years().doubleValue());
	           R102cell6.setCellStyle(numberStyle);
	       } else {
	           R102cell6.setCellValue("");
	           R102cell6.setCellStyle(textStyle);
	       }

	       Cell R102cell7 = row.createCell(8);
	       if (record.getR102_moreThan5YearsTo10Years() != null) {
	           R102cell7.setCellValue(record.getR102_moreThan5YearsTo10Years().doubleValue());
	           R102cell7.setCellStyle(numberStyle);
	       } else {
	           R102cell7.setCellValue("");
	           R102cell7.setCellStyle(textStyle);
	       }

	       Cell R102cell8 = row.createCell(9);
	       if (record.getR102_moreThan10Years() != null) {
	           R102cell8.setCellValue(record.getR102_moreThan10Years().doubleValue());
	           R102cell8.setCellStyle(numberStyle);
	       } else {
	           R102cell8.setCellValue("");
	           R102cell8.setCellStyle(textStyle);
	       }

	       Cell R102cell9 = row.createCell(10);
	       if (record.getR102_nonRatioSensativeItems() != null) {
	           R102cell9.setCellValue(record.getR102_nonRatioSensativeItems().doubleValue());
	           R102cell9.setCellStyle(numberStyle);
	       } else {
	           R102cell9.setCellValue("");
	           R102cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(102);
	       Cell R103cell1 = row.createCell(2);
	       if (record.getR103_upTo1Month() != null) {
	           R103cell1.setCellValue(record.getR103_upTo1Month().doubleValue());
	           R103cell1.setCellStyle(numberStyle);
	       } else {
	           R103cell1.setCellValue("");
	           R103cell1.setCellStyle(textStyle);
	       }

	       Cell R103cell2 = row.createCell(3);
	       if (record.getR103_moreThan1MonthTo3Months() != null) {
	           R103cell2.setCellValue(record.getR103_moreThan1MonthTo3Months().doubleValue());
	           R103cell2.setCellStyle(numberStyle);
	       } else {
	           R103cell2.setCellValue("");
	           R103cell2.setCellStyle(textStyle);
	       }

	       Cell R103cell3 = row.createCell(4);
	       if (record.getR103_moreThan3MonthTo6Months() != null) {
	           R103cell3.setCellValue(record.getR103_moreThan3MonthTo6Months().doubleValue());
	           R103cell3.setCellStyle(numberStyle);
	       } else {
	           R103cell3.setCellValue("");
	           R103cell3.setCellStyle(textStyle);
	       }

	       Cell R103cell4 = row.createCell(5);
	       if (record.getR103_moreThan6MonthTo12Months() != null) {
	           R103cell4.setCellValue(record.getR103_moreThan6MonthTo12Months().doubleValue());
	           R103cell4.setCellStyle(numberStyle);
	       } else {
	           R103cell4.setCellValue("");
	           R103cell4.setCellStyle(textStyle);
	       }

	       Cell R103cell5 = row.createCell(6);
	       if (record.getR103_moreThan12MonthTo3Years() != null) {
	           R103cell5.setCellValue(record.getR103_moreThan12MonthTo3Years().doubleValue());
	           R103cell5.setCellStyle(numberStyle);
	       } else {
	           R103cell5.setCellValue("");
	           R103cell5.setCellStyle(textStyle);
	       }

	       Cell R103cell6 = row.createCell(7);
	       if (record.getR103_moreThan3YearsTo5Years() != null) {
	           R103cell6.setCellValue(record.getR103_moreThan3YearsTo5Years().doubleValue());
	           R103cell6.setCellStyle(numberStyle);
	       } else {
	           R103cell6.setCellValue("");
	           R103cell6.setCellStyle(textStyle);
	       }

	       Cell R103cell7 = row.createCell(8);
	       if (record.getR103_moreThan5YearsTo10Years() != null) {
	           R103cell7.setCellValue(record.getR103_moreThan5YearsTo10Years().doubleValue());
	           R103cell7.setCellStyle(numberStyle);
	       } else {
	           R103cell7.setCellValue("");
	           R103cell7.setCellStyle(textStyle);
	       }

	       Cell R103cell8 = row.createCell(9);
	       if (record.getR103_moreThan10Years() != null) {
	           R103cell8.setCellValue(record.getR103_moreThan10Years().doubleValue());
	           R103cell8.setCellStyle(numberStyle);
	       } else {
	           R103cell8.setCellValue("");
	           R103cell8.setCellStyle(textStyle);
	       }

	       Cell R103cell9 = row.createCell(10);
	       if (record.getR103_nonRatioSensativeItems() != null) {
	           R103cell9.setCellValue(record.getR103_nonRatioSensativeItems().doubleValue());
	           R103cell9.setCellStyle(numberStyle);
	       } else {
	           R103cell9.setCellValue("");
	           R103cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(103);
	       Cell R104cell1 = row.createCell(2);
	       if (record.getR104_upTo1Month() != null) {
	           R104cell1.setCellValue(record.getR104_upTo1Month().doubleValue());
	           R104cell1.setCellStyle(numberStyle);
	       } else {
	           R104cell1.setCellValue("");
	           R104cell1.setCellStyle(textStyle);
	       }

	       Cell R104cell2 = row.createCell(3);
	       if (record.getR104_moreThan1MonthTo3Months() != null) {
	           R104cell2.setCellValue(record.getR104_moreThan1MonthTo3Months().doubleValue());
	           R104cell2.setCellStyle(numberStyle);
	       } else {
	           R104cell2.setCellValue("");
	           R104cell2.setCellStyle(textStyle);
	       }

	       Cell R104cell3 = row.createCell(4);
	       if (record.getR104_moreThan3MonthTo6Months() != null) {
	           R104cell3.setCellValue(record.getR104_moreThan3MonthTo6Months().doubleValue());
	           R104cell3.setCellStyle(numberStyle);
	       } else {
	           R104cell3.setCellValue("");
	           R104cell3.setCellStyle(textStyle);
	       }

	       Cell R104cell4 = row.createCell(5);
	       if (record.getR104_moreThan6MonthTo12Months() != null) {
	           R104cell4.setCellValue(record.getR104_moreThan6MonthTo12Months().doubleValue());
	           R104cell4.setCellStyle(numberStyle);
	       } else {
	           R104cell4.setCellValue("");
	           R104cell4.setCellStyle(textStyle);
	       }

	       Cell R104cell5 = row.createCell(6);
	       if (record.getR104_moreThan12MonthTo3Years() != null) {
	           R104cell5.setCellValue(record.getR104_moreThan12MonthTo3Years().doubleValue());
	           R104cell5.setCellStyle(numberStyle);
	       } else {
	           R104cell5.setCellValue("");
	           R104cell5.setCellStyle(textStyle);
	       }

	       Cell R104cell6 = row.createCell(7);
	       if (record.getR104_moreThan3YearsTo5Years() != null) {
	           R104cell6.setCellValue(record.getR104_moreThan3YearsTo5Years().doubleValue());
	           R104cell6.setCellStyle(numberStyle);
	       } else {
	           R104cell6.setCellValue("");
	           R104cell6.setCellStyle(textStyle);
	       }

	       Cell R104cell7 = row.createCell(8);
	       if (record.getR104_moreThan5YearsTo10Years() != null) {
	           R104cell7.setCellValue(record.getR104_moreThan5YearsTo10Years().doubleValue());
	           R104cell7.setCellStyle(numberStyle);
	       } else {
	           R104cell7.setCellValue("");
	           R104cell7.setCellStyle(textStyle);
	       }

	       Cell R104cell8 = row.createCell(9);
	       if (record.getR104_moreThan10Years() != null) {
	           R104cell8.setCellValue(record.getR104_moreThan10Years().doubleValue());
	           R104cell8.setCellStyle(numberStyle);
	       } else {
	           R104cell8.setCellValue("");
	           R104cell8.setCellStyle(textStyle);
	       }

	       Cell R104cell9 = row.createCell(10);
	       if (record.getR104_nonRatioSensativeItems() != null) {
	           R104cell9.setCellValue(record.getR104_nonRatioSensativeItems().doubleValue());
	           R104cell9.setCellStyle(numberStyle);
	       } else {
	           R104cell9.setCellValue("");
	           R104cell9.setCellStyle(textStyle);
	       }

	  
	  }



class M_IRBSummaryRowMapper implements RowMapper<M_IRB_Summary_Entity> {
	@Override
	public M_IRB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
		M_IRB_Summary_Entity obj = new M_IRB_Summary_Entity();
		obj.setR10product(rs.getString("R10_PRODUCT"));
		obj.setR10_upTo1Month(rs.getBigDecimal("R10_UP_TO_1_MONTH"));
		obj.setR10_moreThan1MonthTo3Months(rs.getBigDecimal("R10_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR10_moreThan3MonthTo6Months(rs.getBigDecimal("R10_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR10_moreThan6MonthTo12Months(rs.getBigDecimal("R10_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR10_moreThan12MonthTo3Years(rs.getBigDecimal("R10_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR10_moreThan3YearsTo5Years(rs.getBigDecimal("R10_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR10_moreThan5YearsTo10Years(rs.getBigDecimal("R10_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR10_moreThan10Years(rs.getBigDecimal("R10_MORE_THAN_10_YEARS"));
		obj.setR10_nonRatioSensativeItems(rs.getBigDecimal("R10_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR10_total(rs.getBigDecimal("R10_TOTAL"));
		obj.setR11product(rs.getString("R11_PRODUCT"));
		obj.setR11_upTo1Month(rs.getBigDecimal("R11_UP_TO_1_MONTH"));
		obj.setR11_moreThan1MonthTo3Months(rs.getBigDecimal("R11_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR11_moreThan3MonthTo6Months(rs.getBigDecimal("R11_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR11_moreThan6MonthTo12Months(rs.getBigDecimal("R11_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR11_moreThan12MonthTo3Years(rs.getBigDecimal("R11_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR11_moreThan3YearsTo5Years(rs.getBigDecimal("R11_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR11_moreThan5YearsTo10Years(rs.getBigDecimal("R11_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR11_moreThan10Years(rs.getBigDecimal("R11_MORE_THAN_10_YEARS"));
		obj.setR11_total(rs.getBigDecimal("R11_TOTAL"));
		obj.setR12product(rs.getString("R12_PRODUCT"));
		obj.setR12_upTo1Month(rs.getBigDecimal("R12_UP_TO_1_MONTH"));
		obj.setR12_moreThan1MonthTo3Months(rs.getBigDecimal("R12_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR12_moreThan3MonthTo6Months(rs.getBigDecimal("R12_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR12_moreThan6MonthTo12Months(rs.getBigDecimal("R12_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR12_moreThan12MonthTo3Years(rs.getBigDecimal("R12_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR12_moreThan3YearsTo5Years(rs.getBigDecimal("R12_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR12_moreThan5YearsTo10Years(rs.getBigDecimal("R12_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR12_moreThan10Years(rs.getBigDecimal("R12_MORE_THAN_10_YEARS"));
		obj.setR12_total(rs.getBigDecimal("R12_TOTAL"));
		obj.setR13product(rs.getString("R13_PRODUCT"));
		obj.setR13_upTo1Month(rs.getBigDecimal("R13_UP_TO_1_MONTH"));
		obj.setR13_moreThan1MonthTo3Months(rs.getBigDecimal("R13_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR13_moreThan3MonthTo6Months(rs.getBigDecimal("R13_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR13_moreThan6MonthTo12Months(rs.getBigDecimal("R13_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR13_moreThan12MonthTo3Years(rs.getBigDecimal("R13_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR13_moreThan3YearsTo5Years(rs.getBigDecimal("R13_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR13_moreThan5YearsTo10Years(rs.getBigDecimal("R13_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR13_moreThan10Years(rs.getBigDecimal("R13_MORE_THAN_10_YEARS"));
		obj.setR13_total(rs.getBigDecimal("R13_TOTAL"));
		obj.setR14product(rs.getString("R14_PRODUCT"));
		obj.setR14_upTo1Month(rs.getBigDecimal("R14_UP_TO_1_MONTH"));
		obj.setR14_moreThan1MonthTo3Months(rs.getBigDecimal("R14_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR14_moreThan3MonthTo6Months(rs.getBigDecimal("R14_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR14_moreThan6MonthTo12Months(rs.getBigDecimal("R14_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR14_moreThan12MonthTo3Years(rs.getBigDecimal("R14_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR14_moreThan3YearsTo5Years(rs.getBigDecimal("R14_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR14_moreThan5YearsTo10Years(rs.getBigDecimal("R14_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR14_moreThan10Years(rs.getBigDecimal("R14_MORE_THAN_10_YEARS"));
		obj.setR14_total(rs.getBigDecimal("R14_TOTAL"));
		obj.setR15product(rs.getString("R15_PRODUCT"));
		obj.setR15_upTo1Month(rs.getBigDecimal("R15_UP_TO_1_MONTH"));
		obj.setR15_moreThan1MonthTo3Months(rs.getBigDecimal("R15_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR15_moreThan3MonthTo6Months(rs.getBigDecimal("R15_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR15_moreThan6MonthTo12Months(rs.getBigDecimal("R15_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR15_moreThan12MonthTo3Years(rs.getBigDecimal("R15_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR15_moreThan3YearsTo5Years(rs.getBigDecimal("R15_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR15_moreThan5YearsTo10Years(rs.getBigDecimal("R15_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR15_moreThan10Years(rs.getBigDecimal("R15_MORE_THAN_10_YEARS"));
		obj.setR15_total(rs.getBigDecimal("R15_TOTAL"));
		obj.setR16product(rs.getString("R16_PRODUCT"));
		obj.setR16_upTo1Month(rs.getBigDecimal("R16_UP_TO_1_MONTH"));
		obj.setR16_moreThan1MonthTo3Months(rs.getBigDecimal("R16_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR16_moreThan3MonthTo6Months(rs.getBigDecimal("R16_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR16_moreThan6MonthTo12Months(rs.getBigDecimal("R16_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR16_moreThan12MonthTo3Years(rs.getBigDecimal("R16_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR16_moreThan3YearsTo5Years(rs.getBigDecimal("R16_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR16_moreThan5YearsTo10Years(rs.getBigDecimal("R16_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR16_moreThan10Years(rs.getBigDecimal("R16_MORE_THAN_10_YEARS"));
		obj.setR16_total(rs.getBigDecimal("R16_TOTAL"));
		obj.setR17product(rs.getString("R17_PRODUCT"));
		obj.setR17_upTo1Month(rs.getBigDecimal("R17_UP_TO_1_MONTH"));
		obj.setR17_moreThan1MonthTo3Months(rs.getBigDecimal("R17_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR17_moreThan3MonthTo6Months(rs.getBigDecimal("R17_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR17_moreThan6MonthTo12Months(rs.getBigDecimal("R17_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR17_moreThan12MonthTo3Years(rs.getBigDecimal("R17_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR17_moreThan3YearsTo5Years(rs.getBigDecimal("R17_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR17_moreThan5YearsTo10Years(rs.getBigDecimal("R17_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR17_moreThan10Years(rs.getBigDecimal("R17_MORE_THAN_10_YEARS"));
		obj.setR17_total(rs.getBigDecimal("R17_TOTAL"));
		obj.setR18product(rs.getString("R18_PRODUCT"));
		obj.setR18_upTo1Month(rs.getBigDecimal("R18_UP_TO_1_MONTH"));
		obj.setR18_moreThan1MonthTo3Months(rs.getBigDecimal("R18_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR18_moreThan3MonthTo6Months(rs.getBigDecimal("R18_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR18_moreThan6MonthTo12Months(rs.getBigDecimal("R18_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR18_moreThan12MonthTo3Years(rs.getBigDecimal("R18_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR18_moreThan3YearsTo5Years(rs.getBigDecimal("R18_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR18_moreThan5YearsTo10Years(rs.getBigDecimal("R18_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR18_moreThan10Years(rs.getBigDecimal("R18_MORE_THAN_10_YEARS"));
		obj.setR18_total(rs.getBigDecimal("R18_TOTAL"));
		obj.setR19product(rs.getString("R19_PRODUCT"));
		obj.setR19_upTo1Month(rs.getBigDecimal("R19_UP_TO_1_MONTH"));
		obj.setR19_moreThan1MonthTo3Months(rs.getBigDecimal("R19_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR19_moreThan3MonthTo6Months(rs.getBigDecimal("R19_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR19_moreThan6MonthTo12Months(rs.getBigDecimal("R19_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR19_moreThan12MonthTo3Years(rs.getBigDecimal("R19_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR19_moreThan3YearsTo5Years(rs.getBigDecimal("R19_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR19_moreThan5YearsTo10Years(rs.getBigDecimal("R19_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR19_moreThan10Years(rs.getBigDecimal("R19_MORE_THAN_10_YEARS"));
		obj.setR19_total(rs.getBigDecimal("R19_TOTAL"));
		obj.setR20product(rs.getString("R20_PRODUCT"));
		obj.setR20_upTo1Month(rs.getBigDecimal("R20_UP_TO_1_MONTH"));
		obj.setR20_moreThan1MonthTo3Months(rs.getBigDecimal("R20_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR20_moreThan3MonthTo6Months(rs.getBigDecimal("R20_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR20_moreThan6MonthTo12Months(rs.getBigDecimal("R20_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR20_moreThan12MonthTo3Years(rs.getBigDecimal("R20_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR20_moreThan3YearsTo5Years(rs.getBigDecimal("R20_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR20_moreThan5YearsTo10Years(rs.getBigDecimal("R20_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR20_moreThan10Years(rs.getBigDecimal("R20_MORE_THAN_10_YEARS"));
		obj.setR20_total(rs.getBigDecimal("R20_TOTAL"));
		obj.setR21product(rs.getString("R21_PRODUCT"));
		obj.setR21_upTo1Month(rs.getBigDecimal("R21_UP_TO_1_MONTH"));
		obj.setR21_moreThan1MonthTo3Months(rs.getBigDecimal("R21_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR21_moreThan3MonthTo6Months(rs.getBigDecimal("R21_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR21_moreThan6MonthTo12Months(rs.getBigDecimal("R21_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR21_moreThan12MonthTo3Years(rs.getBigDecimal("R21_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR21_moreThan3YearsTo5Years(rs.getBigDecimal("R21_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR21_moreThan5YearsTo10Years(rs.getBigDecimal("R21_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR21_moreThan10Years(rs.getBigDecimal("R21_MORE_THAN_10_YEARS"));
		obj.setR21_total(rs.getBigDecimal("R21_TOTAL"));
		obj.setR22product(rs.getString("R22_PRODUCT"));
		obj.setR22_upTo1Month(rs.getBigDecimal("R22_UP_TO_1_MONTH"));
		obj.setR22_moreThan1MonthTo3Months(rs.getBigDecimal("R22_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR22_moreThan3MonthTo6Months(rs.getBigDecimal("R22_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR22_moreThan6MonthTo12Months(rs.getBigDecimal("R22_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR22_moreThan12MonthTo3Years(rs.getBigDecimal("R22_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR22_moreThan3YearsTo5Years(rs.getBigDecimal("R22_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR22_moreThan5YearsTo10Years(rs.getBigDecimal("R22_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR22_moreThan10Years(rs.getBigDecimal("R22_MORE_THAN_10_YEARS"));
		obj.setR22_total(rs.getBigDecimal("R22_TOTAL"));
		obj.setR23product(rs.getString("R23_PRODUCT"));
		obj.setR23_upTo1Month(rs.getBigDecimal("R23_UP_TO_1_MONTH"));
		obj.setR23_moreThan1MonthTo3Months(rs.getBigDecimal("R23_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR23_moreThan3MonthTo6Months(rs.getBigDecimal("R23_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR23_moreThan6MonthTo12Months(rs.getBigDecimal("R23_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR23_moreThan12MonthTo3Years(rs.getBigDecimal("R23_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR23_moreThan3YearsTo5Years(rs.getBigDecimal("R23_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR23_moreThan5YearsTo10Years(rs.getBigDecimal("R23_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR23_moreThan10Years(rs.getBigDecimal("R23_MORE_THAN_10_YEARS"));
		obj.setR23_total(rs.getBigDecimal("R23_TOTAL"));
		obj.setR24product(rs.getString("R24_PRODUCT"));
		obj.setR24_upTo1Month(rs.getBigDecimal("R24_UP_TO_1_MONTH"));
		obj.setR24_moreThan1MonthTo3Months(rs.getBigDecimal("R24_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR24_moreThan3MonthTo6Months(rs.getBigDecimal("R24_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR24_moreThan6MonthTo12Months(rs.getBigDecimal("R24_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR24_moreThan12MonthTo3Years(rs.getBigDecimal("R24_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR24_moreThan3YearsTo5Years(rs.getBigDecimal("R24_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR24_moreThan5YearsTo10Years(rs.getBigDecimal("R24_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR24_moreThan10Years(rs.getBigDecimal("R24_MORE_THAN_10_YEARS"));
		obj.setR24_total(rs.getBigDecimal("R24_TOTAL"));
		obj.setR25product(rs.getString("R25_PRODUCT"));
		obj.setR25_upTo1Month(rs.getBigDecimal("R25_UP_TO_1_MONTH"));
		obj.setR25_moreThan1MonthTo3Months(rs.getBigDecimal("R25_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR25_moreThan3MonthTo6Months(rs.getBigDecimal("R25_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR25_moreThan6MonthTo12Months(rs.getBigDecimal("R25_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR25_moreThan12MonthTo3Years(rs.getBigDecimal("R25_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR25_moreThan3YearsTo5Years(rs.getBigDecimal("R25_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR25_moreThan5YearsTo10Years(rs.getBigDecimal("R25_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR25_moreThan10Years(rs.getBigDecimal("R25_MORE_THAN_10_YEARS"));
		obj.setR25_total(rs.getBigDecimal("R25_TOTAL"));
		obj.setR26product(rs.getString("R26_PRODUCT"));
		obj.setR26_upTo1Month(rs.getBigDecimal("R26_UP_TO_1_MONTH"));
		obj.setR26_moreThan1MonthTo3Months(rs.getBigDecimal("R26_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR26_moreThan3MonthTo6Months(rs.getBigDecimal("R26_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR26_moreThan6MonthTo12Months(rs.getBigDecimal("R26_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR26_moreThan12MonthTo3Years(rs.getBigDecimal("R26_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR26_moreThan3YearsTo5Years(rs.getBigDecimal("R26_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR26_moreThan5YearsTo10Years(rs.getBigDecimal("R26_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR26_moreThan10Years(rs.getBigDecimal("R26_MORE_THAN_10_YEARS"));
		obj.setR26_total(rs.getBigDecimal("R26_TOTAL"));
		obj.setR27product(rs.getString("R27_PRODUCT"));
		obj.setR27_upTo1Month(rs.getBigDecimal("R27_UP_TO_1_MONTH"));
		obj.setR27_moreThan1MonthTo3Months(rs.getBigDecimal("R27_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR27_moreThan3MonthTo6Months(rs.getBigDecimal("R27_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR27_moreThan6MonthTo12Months(rs.getBigDecimal("R27_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR27_moreThan12MonthTo3Years(rs.getBigDecimal("R27_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR27_moreThan3YearsTo5Years(rs.getBigDecimal("R27_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR27_moreThan5YearsTo10Years(rs.getBigDecimal("R27_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR27_moreThan10Years(rs.getBigDecimal("R27_MORE_THAN_10_YEARS"));
		obj.setR27_total(rs.getBigDecimal("R27_TOTAL"));
		obj.setR28product(rs.getString("R28_PRODUCT"));
		obj.setR28_upTo1Month(rs.getBigDecimal("R28_UP_TO_1_MONTH"));
		obj.setR28_moreThan1MonthTo3Months(rs.getBigDecimal("R28_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR28_moreThan3MonthTo6Months(rs.getBigDecimal("R28_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR28_moreThan6MonthTo12Months(rs.getBigDecimal("R28_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR28_moreThan12MonthTo3Years(rs.getBigDecimal("R28_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR28_moreThan3YearsTo5Years(rs.getBigDecimal("R28_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR28_moreThan5YearsTo10Years(rs.getBigDecimal("R28_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR28_moreThan10Years(rs.getBigDecimal("R28_MORE_THAN_10_YEARS"));
		obj.setR28_total(rs.getBigDecimal("R28_TOTAL"));
		obj.setR29product(rs.getString("R29_PRODUCT"));
		obj.setR29_upTo1Month(rs.getBigDecimal("R29_UP_TO_1_MONTH"));
		obj.setR29_moreThan1MonthTo3Months(rs.getBigDecimal("R29_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR29_moreThan3MonthTo6Months(rs.getBigDecimal("R29_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR29_moreThan6MonthTo12Months(rs.getBigDecimal("R29_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR29_moreThan12MonthTo3Years(rs.getBigDecimal("R29_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR29_moreThan3YearsTo5Years(rs.getBigDecimal("R29_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR29_moreThan5YearsTo10Years(rs.getBigDecimal("R29_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR29_moreThan10Years(rs.getBigDecimal("R29_MORE_THAN_10_YEARS"));
		obj.setR29_total(rs.getBigDecimal("R29_TOTAL"));
		obj.setR30product(rs.getString("R30_PRODUCT"));
		obj.setR30_upTo1Month(rs.getBigDecimal("R30_UP_TO_1_MONTH"));
		obj.setR30_moreThan1MonthTo3Months(rs.getBigDecimal("R30_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR30_moreThan3MonthTo6Months(rs.getBigDecimal("R30_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR30_moreThan6MonthTo12Months(rs.getBigDecimal("R30_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR30_moreThan12MonthTo3Years(rs.getBigDecimal("R30_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR30_moreThan3YearsTo5Years(rs.getBigDecimal("R30_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR30_moreThan5YearsTo10Years(rs.getBigDecimal("R30_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR30_moreThan10Years(rs.getBigDecimal("R30_MORE_THAN_10_YEARS"));
		obj.setR30_total(rs.getBigDecimal("R30_TOTAL"));
		obj.setR31product(rs.getString("R31_PRODUCT"));
		obj.setR31_upTo1Month(rs.getBigDecimal("R31_UP_TO_1_MONTH"));
		obj.setR31_moreThan1MonthTo3Months(rs.getBigDecimal("R31_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR31_moreThan3MonthTo6Months(rs.getBigDecimal("R31_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR31_moreThan6MonthTo12Months(rs.getBigDecimal("R31_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR31_moreThan12MonthTo3Years(rs.getBigDecimal("R31_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR31_moreThan3YearsTo5Years(rs.getBigDecimal("R31_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR31_moreThan5YearsTo10Years(rs.getBigDecimal("R31_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR31_moreThan10Years(rs.getBigDecimal("R31_MORE_THAN_10_YEARS"));
		obj.setR31_total(rs.getBigDecimal("R31_TOTAL"));
		obj.setR32product(rs.getString("R32_PRODUCT"));
		obj.setR32_upTo1Month(rs.getBigDecimal("R32_UP_TO_1_MONTH"));
		obj.setR32_moreThan1MonthTo3Months(rs.getBigDecimal("R32_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR32_moreThan3MonthTo6Months(rs.getBigDecimal("R32_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR32_moreThan6MonthTo12Months(rs.getBigDecimal("R32_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR32_moreThan12MonthTo3Years(rs.getBigDecimal("R32_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR32_moreThan3YearsTo5Years(rs.getBigDecimal("R32_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR32_moreThan5YearsTo10Years(rs.getBigDecimal("R32_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR32_moreThan10Years(rs.getBigDecimal("R32_MORE_THAN_10_YEARS"));
		obj.setR32_total(rs.getBigDecimal("R32_TOTAL"));
		obj.setR33product(rs.getString("R33_PRODUCT"));
		obj.setR33_upTo1Month(rs.getBigDecimal("R33_UP_TO_1_MONTH"));
		obj.setR33_moreThan1MonthTo3Months(rs.getBigDecimal("R33_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR33_moreThan3MonthTo6Months(rs.getBigDecimal("R33_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR33_moreThan6MonthTo12Months(rs.getBigDecimal("R33_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR33_moreThan12MonthTo3Years(rs.getBigDecimal("R33_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR33_moreThan3YearsTo5Years(rs.getBigDecimal("R33_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR33_moreThan5YearsTo10Years(rs.getBigDecimal("R33_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR33_moreThan10Years(rs.getBigDecimal("R33_MORE_THAN_10_YEARS"));
		obj.setR33_total(rs.getBigDecimal("R33_TOTAL"));
		obj.setR34product(rs.getString("R34_PRODUCT"));
		obj.setR34_upTo1Month(rs.getBigDecimal("R34_UP_TO_1_MONTH"));
		obj.setR34_moreThan1MonthTo3Months(rs.getBigDecimal("R34_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR34_moreThan3MonthTo6Months(rs.getBigDecimal("R34_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR34_moreThan6MonthTo12Months(rs.getBigDecimal("R34_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR34_moreThan12MonthTo3Years(rs.getBigDecimal("R34_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR34_moreThan3YearsTo5Years(rs.getBigDecimal("R34_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR34_moreThan5YearsTo10Years(rs.getBigDecimal("R34_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR34_moreThan10Years(rs.getBigDecimal("R34_MORE_THAN_10_YEARS"));
		obj.setR34_total(rs.getBigDecimal("R34_TOTAL"));
		obj.setR35product(rs.getString("R35_PRODUCT"));
		obj.setR35_upTo1Month(rs.getBigDecimal("R35_UP_TO_1_MONTH"));
		obj.setR35_moreThan1MonthTo3Months(rs.getBigDecimal("R35_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR35_moreThan3MonthTo6Months(rs.getBigDecimal("R35_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR35_moreThan6MonthTo12Months(rs.getBigDecimal("R35_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR35_moreThan12MonthTo3Years(rs.getBigDecimal("R35_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR35_moreThan3YearsTo5Years(rs.getBigDecimal("R35_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR35_moreThan5YearsTo10Years(rs.getBigDecimal("R35_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR35_moreThan10Years(rs.getBigDecimal("R35_MORE_THAN_10_YEARS"));
		obj.setR35_total(rs.getBigDecimal("R35_TOTAL"));
		obj.setR36product(rs.getString("R36_PRODUCT"));
		obj.setR36_upTo1Month(rs.getBigDecimal("R36_UP_TO_1_MONTH"));
		obj.setR36_moreThan1MonthTo3Months(rs.getBigDecimal("R36_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR36_moreThan3MonthTo6Months(rs.getBigDecimal("R36_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR36_moreThan6MonthTo12Months(rs.getBigDecimal("R36_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR36_moreThan12MonthTo3Years(rs.getBigDecimal("R36_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR36_moreThan3YearsTo5Years(rs.getBigDecimal("R36_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR36_moreThan5YearsTo10Years(rs.getBigDecimal("R36_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR36_moreThan10Years(rs.getBigDecimal("R36_MORE_THAN_10_YEARS"));
		obj.setR36_total(rs.getBigDecimal("R36_TOTAL"));
		obj.setR37product(rs.getString("R37_PRODUCT"));
		obj.setR37_upTo1Month(rs.getBigDecimal("R37_UP_TO_1_MONTH"));
		obj.setR37_moreThan1MonthTo3Months(rs.getBigDecimal("R37_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR37_moreThan3MonthTo6Months(rs.getBigDecimal("R37_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR37_moreThan6MonthTo12Months(rs.getBigDecimal("R37_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR37_moreThan12MonthTo3Years(rs.getBigDecimal("R37_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR37_moreThan3YearsTo5Years(rs.getBigDecimal("R37_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR37_moreThan5YearsTo10Years(rs.getBigDecimal("R37_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR37_moreThan10Years(rs.getBigDecimal("R37_MORE_THAN_10_YEARS"));
		obj.setR37_total(rs.getBigDecimal("R37_TOTAL"));
		obj.setR38product(rs.getString("R38_PRODUCT"));
		obj.setR38_upTo1Month(rs.getBigDecimal("R38_UP_TO_1_MONTH"));
		obj.setR38_moreThan1MonthTo3Months(rs.getBigDecimal("R38_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR38_moreThan3MonthTo6Months(rs.getBigDecimal("R38_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR38_moreThan6MonthTo12Months(rs.getBigDecimal("R38_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR38_moreThan12MonthTo3Years(rs.getBigDecimal("R38_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR38_moreThan3YearsTo5Years(rs.getBigDecimal("R38_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR38_moreThan5YearsTo10Years(rs.getBigDecimal("R38_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR38_moreThan10Years(rs.getBigDecimal("R38_MORE_THAN_10_YEARS"));
		obj.setR38_total(rs.getBigDecimal("R38_TOTAL"));
		obj.setR39product(rs.getString("R39_PRODUCT"));
		obj.setR39_upTo1Month(rs.getBigDecimal("R39_UP_TO_1_MONTH"));
		obj.setR39_moreThan1MonthTo3Months(rs.getBigDecimal("R39_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR39_moreThan3MonthTo6Months(rs.getBigDecimal("R39_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR39_moreThan6MonthTo12Months(rs.getBigDecimal("R39_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR39_moreThan12MonthTo3Years(rs.getBigDecimal("R39_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR39_moreThan3YearsTo5Years(rs.getBigDecimal("R39_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR39_moreThan5YearsTo10Years(rs.getBigDecimal("R39_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR39_moreThan10Years(rs.getBigDecimal("R39_MORE_THAN_10_YEARS"));
		obj.setR39_total(rs.getBigDecimal("R39_TOTAL"));
		obj.setR40product(rs.getString("R40_PRODUCT"));
		obj.setR40_upTo1Month(rs.getBigDecimal("R40_UP_TO_1_MONTH"));
		obj.setR40_moreThan1MonthTo3Months(rs.getBigDecimal("R40_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR40_moreThan3MonthTo6Months(rs.getBigDecimal("R40_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR40_moreThan6MonthTo12Months(rs.getBigDecimal("R40_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR40_moreThan12MonthTo3Years(rs.getBigDecimal("R40_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR40_moreThan3YearsTo5Years(rs.getBigDecimal("R40_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR40_moreThan5YearsTo10Years(rs.getBigDecimal("R40_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR40_moreThan10Years(rs.getBigDecimal("R40_MORE_THAN_10_YEARS"));
		obj.setR40_total(rs.getBigDecimal("R40_TOTAL"));
		obj.setR41product(rs.getString("R41_PRODUCT"));
		obj.setR41_upTo1Month(rs.getBigDecimal("R41_UP_TO_1_MONTH"));
		obj.setR41_moreThan1MonthTo3Months(rs.getBigDecimal("R41_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR41_moreThan3MonthTo6Months(rs.getBigDecimal("R41_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR41_moreThan6MonthTo12Months(rs.getBigDecimal("R41_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR41_moreThan12MonthTo3Years(rs.getBigDecimal("R41_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR41_moreThan3YearsTo5Years(rs.getBigDecimal("R41_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR41_moreThan5YearsTo10Years(rs.getBigDecimal("R41_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR41_moreThan10Years(rs.getBigDecimal("R41_MORE_THAN_10_YEARS"));
		obj.setR41_total(rs.getBigDecimal("R41_TOTAL"));
		obj.setR42product(rs.getString("R42_PRODUCT"));
		obj.setR42_upTo1Month(rs.getBigDecimal("R42_UP_TO_1_MONTH"));
		obj.setR42_moreThan1MonthTo3Months(rs.getBigDecimal("R42_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR42_moreThan3MonthTo6Months(rs.getBigDecimal("R42_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR42_moreThan6MonthTo12Months(rs.getBigDecimal("R42_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR42_moreThan12MonthTo3Years(rs.getBigDecimal("R42_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR42_moreThan3YearsTo5Years(rs.getBigDecimal("R42_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR42_moreThan5YearsTo10Years(rs.getBigDecimal("R42_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR42_moreThan10Years(rs.getBigDecimal("R42_MORE_THAN_10_YEARS"));
		obj.setR42_total(rs.getBigDecimal("R42_TOTAL"));
		obj.setR43product(rs.getString("R43_PRODUCT"));
		obj.setR43_upTo1Month(rs.getBigDecimal("R43_UP_TO_1_MONTH"));
		obj.setR43_moreThan1MonthTo3Months(rs.getBigDecimal("R43_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR43_moreThan3MonthTo6Months(rs.getBigDecimal("R43_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR43_moreThan6MonthTo12Months(rs.getBigDecimal("R43_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR43_moreThan12MonthTo3Years(rs.getBigDecimal("R43_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR43_moreThan3YearsTo5Years(rs.getBigDecimal("R43_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR43_moreThan5YearsTo10Years(rs.getBigDecimal("R43_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR43_moreThan10Years(rs.getBigDecimal("R43_MORE_THAN_10_YEARS"));
		obj.setR43_total(rs.getBigDecimal("R43_TOTAL"));
		obj.setR44product(rs.getString("R44_PRODUCT"));
		obj.setR44_upTo1Month(rs.getBigDecimal("R44_UP_TO_1_MONTH"));
		obj.setR44_moreThan1MonthTo3Months(rs.getBigDecimal("R44_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR44_moreThan3MonthTo6Months(rs.getBigDecimal("R44_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR44_moreThan6MonthTo12Months(rs.getBigDecimal("R44_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR44_moreThan12MonthTo3Years(rs.getBigDecimal("R44_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR44_moreThan3YearsTo5Years(rs.getBigDecimal("R44_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR44_moreThan5YearsTo10Years(rs.getBigDecimal("R44_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR44_moreThan10Years(rs.getBigDecimal("R44_MORE_THAN_10_YEARS"));
		obj.setR44_total(rs.getBigDecimal("R44_TOTAL"));
		obj.setR45product(rs.getString("R45_PRODUCT"));
		obj.setR45_upTo1Month(rs.getBigDecimal("R45_UP_TO_1_MONTH"));
		obj.setR45_moreThan1MonthTo3Months(rs.getBigDecimal("R45_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR45_moreThan3MonthTo6Months(rs.getBigDecimal("R45_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR45_moreThan6MonthTo12Months(rs.getBigDecimal("R45_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR45_moreThan12MonthTo3Years(rs.getBigDecimal("R45_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR45_moreThan3YearsTo5Years(rs.getBigDecimal("R45_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR45_moreThan5YearsTo10Years(rs.getBigDecimal("R45_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR45_moreThan10Years(rs.getBigDecimal("R45_MORE_THAN_10_YEARS"));
		obj.setR45_total(rs.getBigDecimal("R45_TOTAL"));
		obj.setR46product(rs.getString("R46_PRODUCT"));
		obj.setR46_upTo1Month(rs.getBigDecimal("R46_UP_TO_1_MONTH"));
		obj.setR46_moreThan1MonthTo3Months(rs.getBigDecimal("R46_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR46_moreThan3MonthTo6Months(rs.getBigDecimal("R46_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR46_moreThan6MonthTo12Months(rs.getBigDecimal("R46_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR46_moreThan12MonthTo3Years(rs.getBigDecimal("R46_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR46_moreThan3YearsTo5Years(rs.getBigDecimal("R46_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR46_moreThan5YearsTo10Years(rs.getBigDecimal("R46_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR46_moreThan10Years(rs.getBigDecimal("R46_MORE_THAN_10_YEARS"));
		obj.setR46_total(rs.getBigDecimal("R46_TOTAL"));
		obj.setR47_nonRatioSensativeItems(rs.getBigDecimal("R47_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR47_total(rs.getBigDecimal("R47_TOTAL"));
		obj.setR48_nonRatioSensativeItems(rs.getBigDecimal("R48_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR48_total(rs.getBigDecimal("R48_TOTAL"));
		obj.setR49_nonRatioSensativeItems(rs.getBigDecimal("R49_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR49_total(rs.getBigDecimal("R49_TOTAL"));
		obj.setR50_nonRatioSensativeItems(rs.getBigDecimal("R50_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR50_total(rs.getBigDecimal("R50_TOTAL"));
		obj.setR51_nonRatioSensativeItems(rs.getBigDecimal("R51_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR51_total(rs.getBigDecimal("R51_TOTAL"));
		obj.setR52_nonRatioSensativeItems(rs.getBigDecimal("R52_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR52_total(rs.getBigDecimal("R52_TOTAL"));
		obj.setR53_nonRatioSensativeItems(rs.getBigDecimal("R53_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR53_total(rs.getBigDecimal("R53_TOTAL"));
		obj.setR54_nonRatioSensativeItems(rs.getBigDecimal("R54_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR54_total(rs.getBigDecimal("R54_TOTAL"));
		obj.setR55_nonRatioSensativeItems(rs.getBigDecimal("R55_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR55_total(rs.getBigDecimal("R55_TOTAL"));
		obj.setR56_nonRatioSensativeItems(rs.getBigDecimal("R56_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR56_total(rs.getBigDecimal("R56_TOTAL"));
		obj.setR57_nonRatioSensativeItems(rs.getBigDecimal("R57_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR57_total(rs.getBigDecimal("R57_TOTAL"));
		obj.setR58_nonRatioSensativeItems(rs.getBigDecimal("R58_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR58_total(rs.getBigDecimal("R58_TOTAL"));
		obj.setR59product(rs.getString("R59_PRODUCT"));
		obj.setR59_upTo1Month(rs.getBigDecimal("R59_UP_TO_1_MONTH"));
		obj.setR59_moreThan1MonthTo3Months(rs.getBigDecimal("R59_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR59_moreThan3MonthTo6Months(rs.getBigDecimal("R59_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR59_moreThan6MonthTo12Months(rs.getBigDecimal("R59_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR59_moreThan12MonthTo3Years(rs.getBigDecimal("R59_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR59_moreThan3YearsTo5Years(rs.getBigDecimal("R59_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR59_moreThan5YearsTo10Years(rs.getBigDecimal("R59_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR59_moreThan10Years(rs.getBigDecimal("R59_MORE_THAN_10_YEARS"));
		obj.setR59_nonRatioSensativeItems(rs.getBigDecimal("R59_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR59_total(rs.getBigDecimal("R59_TOTAL"));
		obj.setR60product(rs.getString("R60_PRODUCT"));
		obj.setR60_upTo1Month(rs.getBigDecimal("R60_UP_TO_1_MONTH"));
		obj.setR60_moreThan1MonthTo3Months(rs.getBigDecimal("R60_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR60_moreThan3MonthTo6Months(rs.getBigDecimal("R60_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR60_moreThan6MonthTo12Months(rs.getBigDecimal("R60_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR60_moreThan12MonthTo3Years(rs.getBigDecimal("R60_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR60_moreThan3YearsTo5Years(rs.getBigDecimal("R60_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR60_moreThan5YearsTo10Years(rs.getBigDecimal("R60_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR60_moreThan10Years(rs.getBigDecimal("R60_MORE_THAN_10_YEARS"));
		obj.setR60_total(rs.getBigDecimal("R60_TOTAL"));
		obj.setR61product(rs.getString("R61_PRODUCT"));
		obj.setR61_upTo1Month(rs.getBigDecimal("R61_UP_TO_1_MONTH"));
		obj.setR61_moreThan1MonthTo3Months(rs.getBigDecimal("R61_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR61_moreThan3MonthTo6Months(rs.getBigDecimal("R61_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR61_moreThan6MonthTo12Months(rs.getBigDecimal("R61_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR61_moreThan12MonthTo3Years(rs.getBigDecimal("R61_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR61_moreThan3YearsTo5Years(rs.getBigDecimal("R61_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR61_moreThan5YearsTo10Years(rs.getBigDecimal("R61_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR61_moreThan10Years(rs.getBigDecimal("R61_MORE_THAN_10_YEARS"));
		obj.setR61_total(rs.getBigDecimal("R61_TOTAL"));
		obj.setR62product(rs.getString("R62_PRODUCT"));
		obj.setR62_upTo1Month(rs.getBigDecimal("R62_UP_TO_1_MONTH"));
		obj.setR62_moreThan1MonthTo3Months(rs.getBigDecimal("R62_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR62_moreThan3MonthTo6Months(rs.getBigDecimal("R62_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR62_moreThan6MonthTo12Months(rs.getBigDecimal("R62_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR62_moreThan12MonthTo3Years(rs.getBigDecimal("R62_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR62_moreThan3YearsTo5Years(rs.getBigDecimal("R62_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR62_moreThan5YearsTo10Years(rs.getBigDecimal("R62_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR62_moreThan10Years(rs.getBigDecimal("R62_MORE_THAN_10_YEARS"));
		obj.setR62_total(rs.getBigDecimal("R62_TOTAL"));
		obj.setR63product(rs.getString("R63_PRODUCT"));
		obj.setR63_upTo1Month(rs.getBigDecimal("R63_UP_TO_1_MONTH"));
		obj.setR63_moreThan1MonthTo3Months(rs.getBigDecimal("R63_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR63_moreThan3MonthTo6Months(rs.getBigDecimal("R63_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR63_moreThan6MonthTo12Months(rs.getBigDecimal("R63_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR63_moreThan12MonthTo3Years(rs.getBigDecimal("R63_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR63_moreThan3YearsTo5Years(rs.getBigDecimal("R63_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR63_moreThan5YearsTo10Years(rs.getBigDecimal("R63_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR63_moreThan10Years(rs.getBigDecimal("R63_MORE_THAN_10_YEARS"));
		obj.setR63_total(rs.getBigDecimal("R63_TOTAL"));
		obj.setR64product(rs.getString("R64_PRODUCT"));
		obj.setR64_upTo1Month(rs.getBigDecimal("R64_UP_TO_1_MONTH"));
		obj.setR64_moreThan1MonthTo3Months(rs.getBigDecimal("R64_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR64_moreThan3MonthTo6Months(rs.getBigDecimal("R64_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR64_moreThan6MonthTo12Months(rs.getBigDecimal("R64_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR64_moreThan12MonthTo3Years(rs.getBigDecimal("R64_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR64_moreThan3YearsTo5Years(rs.getBigDecimal("R64_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR64_moreThan5YearsTo10Years(rs.getBigDecimal("R64_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR64_moreThan10Years(rs.getBigDecimal("R64_MORE_THAN_10_YEARS"));
		obj.setR64_total(rs.getBigDecimal("R64_TOTAL"));
		obj.setR65product(rs.getString("R65_PRODUCT"));
		obj.setR65_upTo1Month(rs.getBigDecimal("R65_UP_TO_1_MONTH"));
		obj.setR65_moreThan1MonthTo3Months(rs.getBigDecimal("R65_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR65_moreThan3MonthTo6Months(rs.getBigDecimal("R65_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR65_moreThan6MonthTo12Months(rs.getBigDecimal("R65_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR65_moreThan12MonthTo3Years(rs.getBigDecimal("R65_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR65_moreThan3YearsTo5Years(rs.getBigDecimal("R65_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR65_moreThan5YearsTo10Years(rs.getBigDecimal("R65_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR65_moreThan10Years(rs.getBigDecimal("R65_MORE_THAN_10_YEARS"));
		obj.setR65_total(rs.getBigDecimal("R65_TOTAL"));
		obj.setR66product(rs.getString("R66_PRODUCT"));
		obj.setR66_upTo1Month(rs.getBigDecimal("R66_UP_TO_1_MONTH"));
		obj.setR66_moreThan1MonthTo3Months(rs.getBigDecimal("R66_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR66_moreThan3MonthTo6Months(rs.getBigDecimal("R66_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR66_moreThan6MonthTo12Months(rs.getBigDecimal("R66_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR66_moreThan12MonthTo3Years(rs.getBigDecimal("R66_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR66_moreThan3YearsTo5Years(rs.getBigDecimal("R66_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR66_moreThan5YearsTo10Years(rs.getBigDecimal("R66_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR66_moreThan10Years(rs.getBigDecimal("R66_MORE_THAN_10_YEARS"));
		obj.setR66_total(rs.getBigDecimal("R66_TOTAL"));
		obj.setR67product(rs.getString("R67_PRODUCT"));
		obj.setR67_upTo1Month(rs.getBigDecimal("R67_UP_TO_1_MONTH"));
		obj.setR67_moreThan1MonthTo3Months(rs.getBigDecimal("R67_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR67_moreThan3MonthTo6Months(rs.getBigDecimal("R67_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR67_moreThan6MonthTo12Months(rs.getBigDecimal("R67_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR67_moreThan12MonthTo3Years(rs.getBigDecimal("R67_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR67_moreThan3YearsTo5Years(rs.getBigDecimal("R67_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR67_moreThan5YearsTo10Years(rs.getBigDecimal("R67_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR67_moreThan10Years(rs.getBigDecimal("R67_MORE_THAN_10_YEARS"));
		obj.setR67_total(rs.getBigDecimal("R67_TOTAL"));
		obj.setR68product(rs.getString("R68_PRODUCT"));
		obj.setR68_upTo1Month(rs.getBigDecimal("R68_UP_TO_1_MONTH"));
		obj.setR68_moreThan1MonthTo3Months(rs.getBigDecimal("R68_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR68_moreThan3MonthTo6Months(rs.getBigDecimal("R68_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR68_moreThan6MonthTo12Months(rs.getBigDecimal("R68_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR68_moreThan12MonthTo3Years(rs.getBigDecimal("R68_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR68_moreThan3YearsTo5Years(rs.getBigDecimal("R68_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR68_moreThan5YearsTo10Years(rs.getBigDecimal("R68_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR68_moreThan10Years(rs.getBigDecimal("R68_MORE_THAN_10_YEARS"));
		obj.setR68_total(rs.getBigDecimal("R68_TOTAL"));
		obj.setR69product(rs.getString("R69_PRODUCT"));
		obj.setR69_upTo1Month(rs.getBigDecimal("R69_UP_TO_1_MONTH"));
		obj.setR69_moreThan1MonthTo3Months(rs.getBigDecimal("R69_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR69_moreThan3MonthTo6Months(rs.getBigDecimal("R69_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR69_moreThan6MonthTo12Months(rs.getBigDecimal("R69_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR69_moreThan12MonthTo3Years(rs.getBigDecimal("R69_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR69_moreThan3YearsTo5Years(rs.getBigDecimal("R69_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR69_moreThan5YearsTo10Years(rs.getBigDecimal("R69_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR69_moreThan10Years(rs.getBigDecimal("R69_MORE_THAN_10_YEARS"));
		obj.setR69_total(rs.getBigDecimal("R69_TOTAL"));
		obj.setR70product(rs.getString("R70_PRODUCT"));
		obj.setR70_upTo1Month(rs.getBigDecimal("R70_UP_TO_1_MONTH"));
		obj.setR70_moreThan1MonthTo3Months(rs.getBigDecimal("R70_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR70_moreThan3MonthTo6Months(rs.getBigDecimal("R70_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR70_moreThan6MonthTo12Months(rs.getBigDecimal("R70_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR70_moreThan12MonthTo3Years(rs.getBigDecimal("R70_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR70_moreThan3YearsTo5Years(rs.getBigDecimal("R70_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR70_moreThan5YearsTo10Years(rs.getBigDecimal("R70_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR70_moreThan10Years(rs.getBigDecimal("R70_MORE_THAN_10_YEARS"));
		obj.setR70_total(rs.getBigDecimal("R70_TOTAL"));
		obj.setR71product(rs.getString("R71_PRODUCT"));
		obj.setR71_upTo1Month(rs.getBigDecimal("R71_UP_TO_1_MONTH"));
		obj.setR71_moreThan1MonthTo3Months(rs.getBigDecimal("R71_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR71_moreThan3MonthTo6Months(rs.getBigDecimal("R71_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR71_moreThan6MonthTo12Months(rs.getBigDecimal("R71_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR71_moreThan12MonthTo3Years(rs.getBigDecimal("R71_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR71_moreThan3YearsTo5Years(rs.getBigDecimal("R71_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR71_moreThan5YearsTo10Years(rs.getBigDecimal("R71_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR71_moreThan10Years(rs.getBigDecimal("R71_MORE_THAN_10_YEARS"));
		obj.setR71_total(rs.getBigDecimal("R71_TOTAL"));
		obj.setR72product(rs.getString("R72_PRODUCT"));
		obj.setR72_upTo1Month(rs.getBigDecimal("R72_UP_TO_1_MONTH"));
		obj.setR72_moreThan1MonthTo3Months(rs.getBigDecimal("R72_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR72_moreThan3MonthTo6Months(rs.getBigDecimal("R72_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR72_moreThan6MonthTo12Months(rs.getBigDecimal("R72_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR72_moreThan12MonthTo3Years(rs.getBigDecimal("R72_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR72_moreThan3YearsTo5Years(rs.getBigDecimal("R72_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR72_moreThan5YearsTo10Years(rs.getBigDecimal("R72_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR72_moreThan10Years(rs.getBigDecimal("R72_MORE_THAN_10_YEARS"));
		obj.setR72_total(rs.getBigDecimal("R72_TOTAL"));
		obj.setR73product(rs.getString("R73_PRODUCT"));
		obj.setR73_upTo1Month(rs.getBigDecimal("R73_UP_TO_1_MONTH"));
		obj.setR73_moreThan1MonthTo3Months(rs.getBigDecimal("R73_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR73_moreThan3MonthTo6Months(rs.getBigDecimal("R73_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR73_moreThan6MonthTo12Months(rs.getBigDecimal("R73_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR73_moreThan12MonthTo3Years(rs.getBigDecimal("R73_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR73_moreThan3YearsTo5Years(rs.getBigDecimal("R73_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR73_moreThan5YearsTo10Years(rs.getBigDecimal("R73_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR73_moreThan10Years(rs.getBigDecimal("R73_MORE_THAN_10_YEARS"));
		obj.setR73_total(rs.getBigDecimal("R73_TOTAL"));
		obj.setR74product(rs.getString("R74_PRODUCT"));
		obj.setR74_upTo1Month(rs.getBigDecimal("R74_UP_TO_1_MONTH"));
		obj.setR74_moreThan1MonthTo3Months(rs.getBigDecimal("R74_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR74_moreThan3MonthTo6Months(rs.getBigDecimal("R74_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR74_moreThan6MonthTo12Months(rs.getBigDecimal("R74_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR74_moreThan12MonthTo3Years(rs.getBigDecimal("R74_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR74_moreThan3YearsTo5Years(rs.getBigDecimal("R74_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR74_moreThan5YearsTo10Years(rs.getBigDecimal("R74_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR74_moreThan10Years(rs.getBigDecimal("R74_MORE_THAN_10_YEARS"));
		obj.setR74_total(rs.getBigDecimal("R74_TOTAL"));
		obj.setR75product(rs.getString("R75_PRODUCT"));
		obj.setR75_upTo1Month(rs.getBigDecimal("R75_UP_TO_1_MONTH"));
		obj.setR75_moreThan1MonthTo3Months(rs.getBigDecimal("R75_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR75_moreThan3MonthTo6Months(rs.getBigDecimal("R75_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR75_moreThan6MonthTo12Months(rs.getBigDecimal("R75_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR75_moreThan12MonthTo3Years(rs.getBigDecimal("R75_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR75_moreThan3YearsTo5Years(rs.getBigDecimal("R75_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR75_moreThan5YearsTo10Years(rs.getBigDecimal("R75_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR75_moreThan10Years(rs.getBigDecimal("R75_MORE_THAN_10_YEARS"));
		obj.setR75_total(rs.getBigDecimal("R75_TOTAL"));
		obj.setR76product(rs.getString("R76_PRODUCT"));
		obj.setR76_upTo1Month(rs.getBigDecimal("R76_UP_TO_1_MONTH"));
		obj.setR76_moreThan1MonthTo3Months(rs.getBigDecimal("R76_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR76_moreThan3MonthTo6Months(rs.getBigDecimal("R76_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR76_moreThan6MonthTo12Months(rs.getBigDecimal("R76_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR76_moreThan12MonthTo3Years(rs.getBigDecimal("R76_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR76_moreThan3YearsTo5Years(rs.getBigDecimal("R76_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR76_moreThan5YearsTo10Years(rs.getBigDecimal("R76_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR76_moreThan10Years(rs.getBigDecimal("R76_MORE_THAN_10_YEARS"));
		obj.setR76_total(rs.getBigDecimal("R76_TOTAL"));
		obj.setR77product(rs.getString("R77_PRODUCT"));
		obj.setR77_upTo1Month(rs.getBigDecimal("R77_UP_TO_1_MONTH"));
		obj.setR77_moreThan1MonthTo3Months(rs.getBigDecimal("R77_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR77_moreThan3MonthTo6Months(rs.getBigDecimal("R77_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR77_moreThan6MonthTo12Months(rs.getBigDecimal("R77_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR77_moreThan12MonthTo3Years(rs.getBigDecimal("R77_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR77_moreThan3YearsTo5Years(rs.getBigDecimal("R77_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR77_moreThan5YearsTo10Years(rs.getBigDecimal("R77_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR77_moreThan10Years(rs.getBigDecimal("R77_MORE_THAN_10_YEARS"));
		obj.setR77_total(rs.getBigDecimal("R77_TOTAL"));
		obj.setR78product(rs.getString("R78_PRODUCT"));
		obj.setR78_upTo1Month(rs.getBigDecimal("R78_UP_TO_1_MONTH"));
		obj.setR78_moreThan1MonthTo3Months(rs.getBigDecimal("R78_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR78_moreThan3MonthTo6Months(rs.getBigDecimal("R78_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR78_moreThan6MonthTo12Months(rs.getBigDecimal("R78_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR78_moreThan12MonthTo3Years(rs.getBigDecimal("R78_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR78_moreThan3YearsTo5Years(rs.getBigDecimal("R78_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR78_moreThan5YearsTo10Years(rs.getBigDecimal("R78_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR78_moreThan10Years(rs.getBigDecimal("R78_MORE_THAN_10_YEARS"));
		obj.setR78_total(rs.getBigDecimal("R78_TOTAL"));
		obj.setR79product(rs.getString("R79_PRODUCT"));
		obj.setR79_upTo1Month(rs.getBigDecimal("R79_UP_TO_1_MONTH"));
		obj.setR79_moreThan1MonthTo3Months(rs.getBigDecimal("R79_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR79_moreThan3MonthTo6Months(rs.getBigDecimal("R79_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR79_moreThan6MonthTo12Months(rs.getBigDecimal("R79_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR79_moreThan12MonthTo3Years(rs.getBigDecimal("R79_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR79_moreThan3YearsTo5Years(rs.getBigDecimal("R79_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR79_moreThan5YearsTo10Years(rs.getBigDecimal("R79_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR79_moreThan10Years(rs.getBigDecimal("R79_MORE_THAN_10_YEARS"));
		obj.setR79_total(rs.getBigDecimal("R79_TOTAL"));
		obj.setR80product(rs.getString("R80_PRODUCT"));
		obj.setR80_upTo1Month(rs.getBigDecimal("R80_UP_TO_1_MONTH"));
		obj.setR80_moreThan1MonthTo3Months(rs.getBigDecimal("R80_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR80_moreThan3MonthTo6Months(rs.getBigDecimal("R80_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR80_moreThan6MonthTo12Months(rs.getBigDecimal("R80_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR80_moreThan12MonthTo3Years(rs.getBigDecimal("R80_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR80_moreThan3YearsTo5Years(rs.getBigDecimal("R80_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR80_moreThan5YearsTo10Years(rs.getBigDecimal("R80_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR80_moreThan10Years(rs.getBigDecimal("R80_MORE_THAN_10_YEARS"));
		obj.setR80_total(rs.getBigDecimal("R80_TOTAL"));
		obj.setR81product(rs.getString("R81_PRODUCT"));
		obj.setR81_upTo1Month(rs.getBigDecimal("R81_UP_TO_1_MONTH"));
		obj.setR81_moreThan1MonthTo3Months(rs.getBigDecimal("R81_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR81_moreThan3MonthTo6Months(rs.getBigDecimal("R81_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR81_moreThan6MonthTo12Months(rs.getBigDecimal("R81_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR81_moreThan12MonthTo3Years(rs.getBigDecimal("R81_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR81_moreThan3YearsTo5Years(rs.getBigDecimal("R81_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR81_moreThan5YearsTo10Years(rs.getBigDecimal("R81_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR81_moreThan10Years(rs.getBigDecimal("R81_MORE_THAN_10_YEARS"));
		obj.setR81_total(rs.getBigDecimal("R81_TOTAL"));
		obj.setR82product(rs.getString("R82_PRODUCT"));
		obj.setR82_upTo1Month(rs.getBigDecimal("R82_UP_TO_1_MONTH"));
		obj.setR82_moreThan1MonthTo3Months(rs.getBigDecimal("R82_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR82_moreThan3MonthTo6Months(rs.getBigDecimal("R82_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR82_moreThan6MonthTo12Months(rs.getBigDecimal("R82_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR82_moreThan12MonthTo3Years(rs.getBigDecimal("R82_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR82_moreThan3YearsTo5Years(rs.getBigDecimal("R82_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR82_moreThan5YearsTo10Years(rs.getBigDecimal("R82_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR82_moreThan10Years(rs.getBigDecimal("R82_MORE_THAN_10_YEARS"));
		obj.setR82_total(rs.getBigDecimal("R82_TOTAL"));
		obj.setR83product(rs.getString("R83_PRODUCT"));
		obj.setR83_upTo1Month(rs.getBigDecimal("R83_UP_TO_1_MONTH"));
		obj.setR83_moreThan1MonthTo3Months(rs.getBigDecimal("R83_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR83_moreThan3MonthTo6Months(rs.getBigDecimal("R83_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR83_moreThan6MonthTo12Months(rs.getBigDecimal("R83_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR83_moreThan12MonthTo3Years(rs.getBigDecimal("R83_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR83_moreThan3YearsTo5Years(rs.getBigDecimal("R83_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR83_moreThan5YearsTo10Years(rs.getBigDecimal("R83_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR83_moreThan10Years(rs.getBigDecimal("R83_MORE_THAN_10_YEARS"));
		obj.setR83_total(rs.getBigDecimal("R83_TOTAL"));
		obj.setR84_nonRatioSensativeItems(rs.getBigDecimal("R84_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR84_total(rs.getBigDecimal("R84_TOTAL"));
		obj.setR85_nonRatioSensativeItems(rs.getBigDecimal("R85_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR85_total(rs.getBigDecimal("R85_TOTAL"));
		obj.setR86_nonRatioSensativeItems(rs.getBigDecimal("R86_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR86_total(rs.getBigDecimal("R86_TOTAL"));
		obj.setR87_nonRatioSensativeItems(rs.getBigDecimal("R87_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR87_total(rs.getBigDecimal("R87_TOTAL"));
		obj.setR88_nonRatioSensativeItems(rs.getBigDecimal("R88_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR88_total(rs.getBigDecimal("R88_TOTAL"));
		obj.setR89_nonRatioSensativeItems(rs.getBigDecimal("R89_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR89_total(rs.getBigDecimal("R89_TOTAL"));
		obj.setR90_nonRatioSensativeItems(rs.getBigDecimal("R90_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR90_total(rs.getBigDecimal("R90_TOTAL"));
		obj.setR91_nonRatioSensativeItems(rs.getBigDecimal("R91_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR91_total(rs.getBigDecimal("R91_TOTAL"));
		obj.setR92product(rs.getString("R92_PRODUCT"));
		obj.setR92_upTo1Month(rs.getBigDecimal("R92_UP_TO_1_MONTH"));
		obj.setR92_moreThan1MonthTo3Months(rs.getBigDecimal("R92_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR92_moreThan3MonthTo6Months(rs.getBigDecimal("R92_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR92_moreThan6MonthTo12Months(rs.getBigDecimal("R92_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR92_moreThan12MonthTo3Years(rs.getBigDecimal("R92_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR92_moreThan3YearsTo5Years(rs.getBigDecimal("R92_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR92_moreThan5YearsTo10Years(rs.getBigDecimal("R92_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR92_moreThan10Years(rs.getBigDecimal("R92_MORE_THAN_10_YEARS"));
		obj.setR92_total(rs.getBigDecimal("R92_TOTAL"));
		obj.setR93product(rs.getString("R93_PRODUCT"));
		obj.setR93_upTo1Month(rs.getBigDecimal("R93_UP_TO_1_MONTH"));
		obj.setR93_moreThan1MonthTo3Months(rs.getBigDecimal("R93_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR93_moreThan3MonthTo6Months(rs.getBigDecimal("R93_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR93_moreThan6MonthTo12Months(rs.getBigDecimal("R93_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR93_moreThan12MonthTo3Years(rs.getBigDecimal("R93_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR93_moreThan3YearsTo5Years(rs.getBigDecimal("R93_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR93_moreThan5YearsTo10Years(rs.getBigDecimal("R93_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR93_moreThan10Years(rs.getBigDecimal("R93_MORE_THAN_10_YEARS"));
		obj.setR93_total(rs.getBigDecimal("R93_TOTAL"));
		obj.setR94product(rs.getString("R94_PRODUCT"));
		obj.setR94_upTo1Month(rs.getBigDecimal("R94_UP_TO_1_MONTH"));
		obj.setR94_moreThan1MonthTo3Months(rs.getBigDecimal("R94_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR94_moreThan3MonthTo6Months(rs.getBigDecimal("R94_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR94_moreThan6MonthTo12Months(rs.getBigDecimal("R94_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR94_moreThan12MonthTo3Years(rs.getBigDecimal("R94_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR94_moreThan3YearsTo5Years(rs.getBigDecimal("R94_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR94_moreThan5YearsTo10Years(rs.getBigDecimal("R94_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR94_moreThan10Years(rs.getBigDecimal("R94_MORE_THAN_10_YEARS"));
		obj.setR94_total(rs.getBigDecimal("R94_TOTAL"));
		obj.setR95_nonRatioSensativeItems(rs.getBigDecimal("R95_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR95_total(rs.getBigDecimal("R95_TOTAL"));
		obj.setR96product(rs.getString("R96_PRODUCT"));
		obj.setR96_upTo1Month(rs.getBigDecimal("R96_UP_TO_1_MONTH"));
		obj.setR96_moreThan1MonthTo3Months(rs.getBigDecimal("R96_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR96_moreThan3MonthTo6Months(rs.getBigDecimal("R96_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR96_moreThan6MonthTo12Months(rs.getBigDecimal("R96_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR96_moreThan12MonthTo3Years(rs.getBigDecimal("R96_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR96_moreThan3YearsTo5Years(rs.getBigDecimal("R96_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR96_moreThan5YearsTo10Years(rs.getBigDecimal("R96_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR96_moreThan10Years(rs.getBigDecimal("R96_MORE_THAN_10_YEARS"));
		obj.setR96_nonRatioSensativeItems(rs.getBigDecimal("R96_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR96_total(rs.getBigDecimal("R96_TOTAL"));
		obj.setR97product(rs.getString("R97_PRODUCT"));
		obj.setR97_upTo1Month(rs.getBigDecimal("R97_UP_TO_1_MONTH"));
		obj.setR97_moreThan1MonthTo3Months(rs.getBigDecimal("R97_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR97_moreThan3MonthTo6Months(rs.getBigDecimal("R97_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR97_moreThan6MonthTo12Months(rs.getBigDecimal("R97_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR97_moreThan12MonthTo3Years(rs.getBigDecimal("R97_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR97_moreThan3YearsTo5Years(rs.getBigDecimal("R97_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR97_moreThan5YearsTo10Years(rs.getBigDecimal("R97_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR97_moreThan10Years(rs.getBigDecimal("R97_MORE_THAN_10_YEARS"));
		obj.setR97_nonRatioSensativeItems(rs.getBigDecimal("R97_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR97_total(rs.getBigDecimal("R97_TOTAL"));
		obj.setR98product(rs.getString("R98_PRODUCT"));
		obj.setR98_upTo1Month(rs.getBigDecimal("R98_UP_TO_1_MONTH"));
		obj.setR98_moreThan1MonthTo3Months(rs.getBigDecimal("R98_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR98_moreThan3MonthTo6Months(rs.getBigDecimal("R98_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR98_moreThan6MonthTo12Months(rs.getBigDecimal("R98_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR98_moreThan12MonthTo3Years(rs.getBigDecimal("R98_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR98_moreThan3YearsTo5Years(rs.getBigDecimal("R98_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR98_moreThan5YearsTo10Years(rs.getBigDecimal("R98_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR98_moreThan10Years(rs.getBigDecimal("R98_MORE_THAN_10_YEARS"));
		obj.setR98_nonRatioSensativeItems(rs.getBigDecimal("R98_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR98_total(rs.getBigDecimal("R98_TOTAL"));
		obj.setR99product(rs.getString("R99_PRODUCT"));
		obj.setR99_upTo1Month(rs.getBigDecimal("R99_UP_TO_1_MONTH"));
		obj.setR99_moreThan1MonthTo3Months(rs.getBigDecimal("R99_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR99_moreThan3MonthTo6Months(rs.getBigDecimal("R99_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR99_moreThan6MonthTo12Months(rs.getBigDecimal("R99_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR99_moreThan12MonthTo3Years(rs.getBigDecimal("R99_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR99_moreThan3YearsTo5Years(rs.getBigDecimal("R99_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR99_moreThan5YearsTo10Years(rs.getBigDecimal("R99_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR99_moreThan10Years(rs.getBigDecimal("R99_MORE_THAN_10_YEARS"));
		obj.setR99_nonRatioSensativeItems(rs.getBigDecimal("R99_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR99_total(rs.getBigDecimal("R99_TOTAL"));
		obj.setR100product(rs.getString("R100_PRODUCT"));
		obj.setR100_upTo1Month(rs.getBigDecimal("R100_UP_TO_1_MONTH"));
		obj.setR100_moreThan1MonthTo3Months(rs.getBigDecimal("R100_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR100_moreThan3MonthTo6Months(rs.getBigDecimal("R100_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR100_moreThan6MonthTo12Months(rs.getBigDecimal("R100_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR100_moreThan12MonthTo3Years(rs.getBigDecimal("R100_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR100_moreThan3YearsTo5Years(rs.getBigDecimal("R100_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR100_moreThan5YearsTo10Years(rs.getBigDecimal("R100_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR100_moreThan10Years(rs.getBigDecimal("R100_MORE_THAN_10_YEARS"));
		obj.setR100_nonRatioSensativeItems(rs.getBigDecimal("R100_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR100_total(rs.getBigDecimal("R100_TOTAL"));
		obj.setR101product(rs.getString("R101_PRODUCT"));
		obj.setR101_upTo1Month(rs.getBigDecimal("R101_UP_TO_1_MONTH"));
		obj.setR101_moreThan1MonthTo3Months(rs.getBigDecimal("R101_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR101_moreThan3MonthTo6Months(rs.getBigDecimal("R101_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR101_moreThan6MonthTo12Months(rs.getBigDecimal("R101_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR101_moreThan12MonthTo3Years(rs.getBigDecimal("R101_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR101_moreThan3YearsTo5Years(rs.getBigDecimal("R101_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR101_moreThan5YearsTo10Years(rs.getBigDecimal("R101_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR101_moreThan10Years(rs.getBigDecimal("R101_MORE_THAN_10_YEARS"));
		obj.setR101_nonRatioSensativeItems(rs.getBigDecimal("R101_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR101_total(rs.getBigDecimal("R101_TOTAL"));
		obj.setR102product(rs.getString("R102_PRODUCT"));
		obj.setR102_upTo1Month(rs.getBigDecimal("R102_UP_TO_1_MONTH"));
		obj.setR102_moreThan1MonthTo3Months(rs.getBigDecimal("R102_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR102_moreThan3MonthTo6Months(rs.getBigDecimal("R102_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR102_moreThan6MonthTo12Months(rs.getBigDecimal("R102_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR102_moreThan12MonthTo3Years(rs.getBigDecimal("R102_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR102_moreThan3YearsTo5Years(rs.getBigDecimal("R102_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR102_moreThan5YearsTo10Years(rs.getBigDecimal("R102_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR102_moreThan10Years(rs.getBigDecimal("R102_MORE_THAN_10_YEARS"));
		obj.setR102_nonRatioSensativeItems(rs.getBigDecimal("R102_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR102_total(rs.getBigDecimal("R102_TOTAL"));
		obj.setR103product(rs.getString("R103_PRODUCT"));
		obj.setR103_upTo1Month(rs.getBigDecimal("R103_UP_TO_1_MONTH"));
		obj.setR103_moreThan1MonthTo3Months(rs.getBigDecimal("R103_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR103_moreThan3MonthTo6Months(rs.getBigDecimal("R103_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR103_moreThan6MonthTo12Months(rs.getBigDecimal("R103_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR103_moreThan12MonthTo3Years(rs.getBigDecimal("R103_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR103_moreThan3YearsTo5Years(rs.getBigDecimal("R103_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR103_moreThan5YearsTo10Years(rs.getBigDecimal("R103_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR103_moreThan10Years(rs.getBigDecimal("R103_MORE_THAN_10_YEARS"));
		obj.setR103_nonRatioSensativeItems(rs.getBigDecimal("R103_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR103_total(rs.getBigDecimal("R103_TOTAL"));
		obj.setR104product(rs.getString("R104_PRODUCT"));
		obj.setR104_upTo1Month(rs.getBigDecimal("R104_UP_TO_1_MONTH"));
		obj.setR104_moreThan1MonthTo3Months(rs.getBigDecimal("R104_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR104_moreThan3MonthTo6Months(rs.getBigDecimal("R104_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR104_moreThan6MonthTo12Months(rs.getBigDecimal("R104_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR104_moreThan12MonthTo3Years(rs.getBigDecimal("R104_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR104_moreThan3YearsTo5Years(rs.getBigDecimal("R104_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR104_moreThan5YearsTo10Years(rs.getBigDecimal("R104_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR104_moreThan10Years(rs.getBigDecimal("R104_MORE_THAN_10_YEARS"));
		obj.setR104_nonRatioSensativeItems(rs.getBigDecimal("R104_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR104_total(rs.getBigDecimal("R104_TOTAL"));
		obj.setR105product(rs.getString("R105_PRODUCT"));
		obj.setR105_upTo1Month(rs.getBigDecimal("R105_UP_TO_1_MONTH"));
		obj.setR105_moreThan1MonthTo3Months(rs.getBigDecimal("R105_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR105_moreThan3MonthTo6Months(rs.getBigDecimal("R105_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR105_moreThan6MonthTo12Months(rs.getBigDecimal("R105_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR105_moreThan12MonthTo3Years(rs.getBigDecimal("R105_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR105_moreThan3YearsTo5Years(rs.getBigDecimal("R105_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR105_moreThan5YearsTo10Years(rs.getBigDecimal("R105_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR105_moreThan10Years(rs.getBigDecimal("R105_MORE_THAN_10_YEARS"));
		obj.setR105_nonRatioSensativeItems(rs.getBigDecimal("R105_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR105_total(rs.getBigDecimal("R105_TOTAL"));
		obj.setR106product(rs.getString("R106_PRODUCT"));
		obj.setR106_upTo1Month(rs.getBigDecimal("R106_UP_TO_1_MONTH"));
		obj.setR106_moreThan1MonthTo3Months(rs.getBigDecimal("R106_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR106_moreThan3MonthTo6Months(rs.getBigDecimal("R106_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR106_moreThan6MonthTo12Months(rs.getBigDecimal("R106_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR106_moreThan12MonthTo3Years(rs.getBigDecimal("R106_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR106_moreThan3YearsTo5Years(rs.getBigDecimal("R106_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR106_moreThan5YearsTo10Years(rs.getBigDecimal("R106_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR106_moreThan10Years(rs.getBigDecimal("R106_MORE_THAN_10_YEARS"));
		obj.setR106_nonRatioSensativeItems(rs.getBigDecimal("R106_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR106_total(rs.getBigDecimal("R106_TOTAL"));
		obj.setReportDate(rs.getDate("REPORT_DATE"));
		obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
		obj.setReportCode(rs.getString("REPORT_CODE"));
		obj.setReportDesc(rs.getString("REPORT_DESC"));
		obj.setEntityFlg(rs.getString("ENTITY_FLG"));
		obj.setModifyFlg(rs.getString("MODIFY_FLG"));
		obj.setDelFlg(rs.getString("DEL_FLG"));
		return obj;
	}
}
class M_IRBArchivalSummaryRowMapper implements RowMapper<M_IRB_Archival_Summary_Entity> {
	@Override
	public M_IRB_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
		M_IRB_Archival_Summary_Entity obj = new M_IRB_Archival_Summary_Entity();
		obj.setR10product(rs.getString("R10_PRODUCT"));
		obj.setR10_upTo1Month(rs.getBigDecimal("R10_UP_TO_1_MONTH"));
		obj.setR10_moreThan1MonthTo3Months(rs.getBigDecimal("R10_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR10_moreThan3MonthTo6Months(rs.getBigDecimal("R10_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR10_moreThan6MonthTo12Months(rs.getBigDecimal("R10_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR10_moreThan12MonthTo3Years(rs.getBigDecimal("R10_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR10_moreThan3YearsTo5Years(rs.getBigDecimal("R10_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR10_moreThan5YearsTo10Years(rs.getBigDecimal("R10_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR10_moreThan10Years(rs.getBigDecimal("R10_MORE_THAN_10_YEARS"));
		obj.setR10_nonRatioSensativeItems(rs.getBigDecimal("R10_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR10_total(rs.getBigDecimal("R10_TOTAL"));
		obj.setR11product(rs.getString("R11_PRODUCT"));
		obj.setR11_upTo1Month(rs.getBigDecimal("R11_UP_TO_1_MONTH"));
		obj.setR11_moreThan1MonthTo3Months(rs.getBigDecimal("R11_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR11_moreThan3MonthTo6Months(rs.getBigDecimal("R11_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR11_moreThan6MonthTo12Months(rs.getBigDecimal("R11_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR11_moreThan12MonthTo3Years(rs.getBigDecimal("R11_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR11_moreThan3YearsTo5Years(rs.getBigDecimal("R11_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR11_moreThan5YearsTo10Years(rs.getBigDecimal("R11_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR11_moreThan10Years(rs.getBigDecimal("R11_MORE_THAN_10_YEARS"));
		obj.setR11_total(rs.getBigDecimal("R11_TOTAL"));
		obj.setR12product(rs.getString("R12_PRODUCT"));
		obj.setR12_upTo1Month(rs.getBigDecimal("R12_UP_TO_1_MONTH"));
		obj.setR12_moreThan1MonthTo3Months(rs.getBigDecimal("R12_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR12_moreThan3MonthTo6Months(rs.getBigDecimal("R12_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR12_moreThan6MonthTo12Months(rs.getBigDecimal("R12_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR12_moreThan12MonthTo3Years(rs.getBigDecimal("R12_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR12_moreThan3YearsTo5Years(rs.getBigDecimal("R12_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR12_moreThan5YearsTo10Years(rs.getBigDecimal("R12_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR12_moreThan10Years(rs.getBigDecimal("R12_MORE_THAN_10_YEARS"));
		obj.setR12_total(rs.getBigDecimal("R12_TOTAL"));
		obj.setR13product(rs.getString("R13_PRODUCT"));
		obj.setR13_upTo1Month(rs.getBigDecimal("R13_UP_TO_1_MONTH"));
		obj.setR13_moreThan1MonthTo3Months(rs.getBigDecimal("R13_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR13_moreThan3MonthTo6Months(rs.getBigDecimal("R13_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR13_moreThan6MonthTo12Months(rs.getBigDecimal("R13_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR13_moreThan12MonthTo3Years(rs.getBigDecimal("R13_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR13_moreThan3YearsTo5Years(rs.getBigDecimal("R13_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR13_moreThan5YearsTo10Years(rs.getBigDecimal("R13_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR13_moreThan10Years(rs.getBigDecimal("R13_MORE_THAN_10_YEARS"));
		obj.setR13_total(rs.getBigDecimal("R13_TOTAL"));
		obj.setR14product(rs.getString("R14_PRODUCT"));
		obj.setR14_upTo1Month(rs.getBigDecimal("R14_UP_TO_1_MONTH"));
		obj.setR14_moreThan1MonthTo3Months(rs.getBigDecimal("R14_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR14_moreThan3MonthTo6Months(rs.getBigDecimal("R14_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR14_moreThan6MonthTo12Months(rs.getBigDecimal("R14_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR14_moreThan12MonthTo3Years(rs.getBigDecimal("R14_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR14_moreThan3YearsTo5Years(rs.getBigDecimal("R14_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR14_moreThan5YearsTo10Years(rs.getBigDecimal("R14_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR14_moreThan10Years(rs.getBigDecimal("R14_MORE_THAN_10_YEARS"));
		obj.setR14_total(rs.getBigDecimal("R14_TOTAL"));
		obj.setR15product(rs.getString("R15_PRODUCT"));
		obj.setR15_upTo1Month(rs.getBigDecimal("R15_UP_TO_1_MONTH"));
		obj.setR15_moreThan1MonthTo3Months(rs.getBigDecimal("R15_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR15_moreThan3MonthTo6Months(rs.getBigDecimal("R15_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR15_moreThan6MonthTo12Months(rs.getBigDecimal("R15_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR15_moreThan12MonthTo3Years(rs.getBigDecimal("R15_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR15_moreThan3YearsTo5Years(rs.getBigDecimal("R15_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR15_moreThan5YearsTo10Years(rs.getBigDecimal("R15_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR15_moreThan10Years(rs.getBigDecimal("R15_MORE_THAN_10_YEARS"));
		obj.setR15_total(rs.getBigDecimal("R15_TOTAL"));
		obj.setR16product(rs.getString("R16_PRODUCT"));
		obj.setR16_upTo1Month(rs.getBigDecimal("R16_UP_TO_1_MONTH"));
		obj.setR16_moreThan1MonthTo3Months(rs.getBigDecimal("R16_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR16_moreThan3MonthTo6Months(rs.getBigDecimal("R16_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR16_moreThan6MonthTo12Months(rs.getBigDecimal("R16_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR16_moreThan12MonthTo3Years(rs.getBigDecimal("R16_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR16_moreThan3YearsTo5Years(rs.getBigDecimal("R16_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR16_moreThan5YearsTo10Years(rs.getBigDecimal("R16_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR16_moreThan10Years(rs.getBigDecimal("R16_MORE_THAN_10_YEARS"));
		obj.setR16_total(rs.getBigDecimal("R16_TOTAL"));
		obj.setR17product(rs.getString("R17_PRODUCT"));
		obj.setR17_upTo1Month(rs.getBigDecimal("R17_UP_TO_1_MONTH"));
		obj.setR17_moreThan1MonthTo3Months(rs.getBigDecimal("R17_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR17_moreThan3MonthTo6Months(rs.getBigDecimal("R17_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR17_moreThan6MonthTo12Months(rs.getBigDecimal("R17_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR17_moreThan12MonthTo3Years(rs.getBigDecimal("R17_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR17_moreThan3YearsTo5Years(rs.getBigDecimal("R17_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR17_moreThan5YearsTo10Years(rs.getBigDecimal("R17_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR17_moreThan10Years(rs.getBigDecimal("R17_MORE_THAN_10_YEARS"));
		obj.setR17_total(rs.getBigDecimal("R17_TOTAL"));
		obj.setR18product(rs.getString("R18_PRODUCT"));
		obj.setR18_upTo1Month(rs.getBigDecimal("R18_UP_TO_1_MONTH"));
		obj.setR18_moreThan1MonthTo3Months(rs.getBigDecimal("R18_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR18_moreThan3MonthTo6Months(rs.getBigDecimal("R18_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR18_moreThan6MonthTo12Months(rs.getBigDecimal("R18_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR18_moreThan12MonthTo3Years(rs.getBigDecimal("R18_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR18_moreThan3YearsTo5Years(rs.getBigDecimal("R18_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR18_moreThan5YearsTo10Years(rs.getBigDecimal("R18_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR18_moreThan10Years(rs.getBigDecimal("R18_MORE_THAN_10_YEARS"));
		obj.setR18_total(rs.getBigDecimal("R18_TOTAL"));
		obj.setR19product(rs.getString("R19_PRODUCT"));
		obj.setR19_upTo1Month(rs.getBigDecimal("R19_UP_TO_1_MONTH"));
		obj.setR19_moreThan1MonthTo3Months(rs.getBigDecimal("R19_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR19_moreThan3MonthTo6Months(rs.getBigDecimal("R19_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR19_moreThan6MonthTo12Months(rs.getBigDecimal("R19_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR19_moreThan12MonthTo3Years(rs.getBigDecimal("R19_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR19_moreThan3YearsTo5Years(rs.getBigDecimal("R19_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR19_moreThan5YearsTo10Years(rs.getBigDecimal("R19_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR19_moreThan10Years(rs.getBigDecimal("R19_MORE_THAN_10_YEARS"));
		obj.setR19_total(rs.getBigDecimal("R19_TOTAL"));
		obj.setR20product(rs.getString("R20_PRODUCT"));
		obj.setR20_upTo1Month(rs.getBigDecimal("R20_UP_TO_1_MONTH"));
		obj.setR20_moreThan1MonthTo3Months(rs.getBigDecimal("R20_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR20_moreThan3MonthTo6Months(rs.getBigDecimal("R20_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR20_moreThan6MonthTo12Months(rs.getBigDecimal("R20_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR20_moreThan12MonthTo3Years(rs.getBigDecimal("R20_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR20_moreThan3YearsTo5Years(rs.getBigDecimal("R20_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR20_moreThan5YearsTo10Years(rs.getBigDecimal("R20_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR20_moreThan10Years(rs.getBigDecimal("R20_MORE_THAN_10_YEARS"));
		obj.setR20_total(rs.getBigDecimal("R20_TOTAL"));
		obj.setR21product(rs.getString("R21_PRODUCT"));
		obj.setR21_upTo1Month(rs.getBigDecimal("R21_UP_TO_1_MONTH"));
		obj.setR21_moreThan1MonthTo3Months(rs.getBigDecimal("R21_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR21_moreThan3MonthTo6Months(rs.getBigDecimal("R21_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR21_moreThan6MonthTo12Months(rs.getBigDecimal("R21_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR21_moreThan12MonthTo3Years(rs.getBigDecimal("R21_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR21_moreThan3YearsTo5Years(rs.getBigDecimal("R21_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR21_moreThan5YearsTo10Years(rs.getBigDecimal("R21_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR21_moreThan10Years(rs.getBigDecimal("R21_MORE_THAN_10_YEARS"));
		obj.setR21_total(rs.getBigDecimal("R21_TOTAL"));
		obj.setR22product(rs.getString("R22_PRODUCT"));
		obj.setR22_upTo1Month(rs.getBigDecimal("R22_UP_TO_1_MONTH"));
		obj.setR22_moreThan1MonthTo3Months(rs.getBigDecimal("R22_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR22_moreThan3MonthTo6Months(rs.getBigDecimal("R22_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR22_moreThan6MonthTo12Months(rs.getBigDecimal("R22_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR22_moreThan12MonthTo3Years(rs.getBigDecimal("R22_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR22_moreThan3YearsTo5Years(rs.getBigDecimal("R22_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR22_moreThan5YearsTo10Years(rs.getBigDecimal("R22_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR22_moreThan10Years(rs.getBigDecimal("R22_MORE_THAN_10_YEARS"));
		obj.setR22_total(rs.getBigDecimal("R22_TOTAL"));
		obj.setR23product(rs.getString("R23_PRODUCT"));
		obj.setR23_upTo1Month(rs.getBigDecimal("R23_UP_TO_1_MONTH"));
		obj.setR23_moreThan1MonthTo3Months(rs.getBigDecimal("R23_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR23_moreThan3MonthTo6Months(rs.getBigDecimal("R23_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR23_moreThan6MonthTo12Months(rs.getBigDecimal("R23_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR23_moreThan12MonthTo3Years(rs.getBigDecimal("R23_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR23_moreThan3YearsTo5Years(rs.getBigDecimal("R23_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR23_moreThan5YearsTo10Years(rs.getBigDecimal("R23_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR23_moreThan10Years(rs.getBigDecimal("R23_MORE_THAN_10_YEARS"));
		obj.setR23_total(rs.getBigDecimal("R23_TOTAL"));
		obj.setR24product(rs.getString("R24_PRODUCT"));
		obj.setR24_upTo1Month(rs.getBigDecimal("R24_UP_TO_1_MONTH"));
		obj.setR24_moreThan1MonthTo3Months(rs.getBigDecimal("R24_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR24_moreThan3MonthTo6Months(rs.getBigDecimal("R24_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR24_moreThan6MonthTo12Months(rs.getBigDecimal("R24_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR24_moreThan12MonthTo3Years(rs.getBigDecimal("R24_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR24_moreThan3YearsTo5Years(rs.getBigDecimal("R24_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR24_moreThan5YearsTo10Years(rs.getBigDecimal("R24_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR24_moreThan10Years(rs.getBigDecimal("R24_MORE_THAN_10_YEARS"));
		obj.setR24_total(rs.getBigDecimal("R24_TOTAL"));
		obj.setR25product(rs.getString("R25_PRODUCT"));
		obj.setR25_upTo1Month(rs.getBigDecimal("R25_UP_TO_1_MONTH"));
		obj.setR25_moreThan1MonthTo3Months(rs.getBigDecimal("R25_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR25_moreThan3MonthTo6Months(rs.getBigDecimal("R25_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR25_moreThan6MonthTo12Months(rs.getBigDecimal("R25_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR25_moreThan12MonthTo3Years(rs.getBigDecimal("R25_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR25_moreThan3YearsTo5Years(rs.getBigDecimal("R25_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR25_moreThan5YearsTo10Years(rs.getBigDecimal("R25_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR25_moreThan10Years(rs.getBigDecimal("R25_MORE_THAN_10_YEARS"));
		obj.setR25_total(rs.getBigDecimal("R25_TOTAL"));
		obj.setR26product(rs.getString("R26_PRODUCT"));
		obj.setR26_upTo1Month(rs.getBigDecimal("R26_UP_TO_1_MONTH"));
		obj.setR26_moreThan1MonthTo3Months(rs.getBigDecimal("R26_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR26_moreThan3MonthTo6Months(rs.getBigDecimal("R26_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR26_moreThan6MonthTo12Months(rs.getBigDecimal("R26_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR26_moreThan12MonthTo3Years(rs.getBigDecimal("R26_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR26_moreThan3YearsTo5Years(rs.getBigDecimal("R26_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR26_moreThan5YearsTo10Years(rs.getBigDecimal("R26_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR26_moreThan10Years(rs.getBigDecimal("R26_MORE_THAN_10_YEARS"));
		obj.setR26_total(rs.getBigDecimal("R26_TOTAL"));
		obj.setR27product(rs.getString("R27_PRODUCT"));
		obj.setR27_upTo1Month(rs.getBigDecimal("R27_UP_TO_1_MONTH"));
		obj.setR27_moreThan1MonthTo3Months(rs.getBigDecimal("R27_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR27_moreThan3MonthTo6Months(rs.getBigDecimal("R27_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR27_moreThan6MonthTo12Months(rs.getBigDecimal("R27_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR27_moreThan12MonthTo3Years(rs.getBigDecimal("R27_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR27_moreThan3YearsTo5Years(rs.getBigDecimal("R27_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR27_moreThan5YearsTo10Years(rs.getBigDecimal("R27_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR27_moreThan10Years(rs.getBigDecimal("R27_MORE_THAN_10_YEARS"));
		obj.setR27_total(rs.getBigDecimal("R27_TOTAL"));
		obj.setR28product(rs.getString("R28_PRODUCT"));
		obj.setR28_upTo1Month(rs.getBigDecimal("R28_UP_TO_1_MONTH"));
		obj.setR28_moreThan1MonthTo3Months(rs.getBigDecimal("R28_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR28_moreThan3MonthTo6Months(rs.getBigDecimal("R28_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR28_moreThan6MonthTo12Months(rs.getBigDecimal("R28_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR28_moreThan12MonthTo3Years(rs.getBigDecimal("R28_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR28_moreThan3YearsTo5Years(rs.getBigDecimal("R28_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR28_moreThan5YearsTo10Years(rs.getBigDecimal("R28_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR28_moreThan10Years(rs.getBigDecimal("R28_MORE_THAN_10_YEARS"));
		obj.setR28_total(rs.getBigDecimal("R28_TOTAL"));
		obj.setR29product(rs.getString("R29_PRODUCT"));
		obj.setR29_upTo1Month(rs.getBigDecimal("R29_UP_TO_1_MONTH"));
		obj.setR29_moreThan1MonthTo3Months(rs.getBigDecimal("R29_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR29_moreThan3MonthTo6Months(rs.getBigDecimal("R29_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR29_moreThan6MonthTo12Months(rs.getBigDecimal("R29_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR29_moreThan12MonthTo3Years(rs.getBigDecimal("R29_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR29_moreThan3YearsTo5Years(rs.getBigDecimal("R29_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR29_moreThan5YearsTo10Years(rs.getBigDecimal("R29_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR29_moreThan10Years(rs.getBigDecimal("R29_MORE_THAN_10_YEARS"));
		obj.setR29_total(rs.getBigDecimal("R29_TOTAL"));
		obj.setR30product(rs.getString("R30_PRODUCT"));
		obj.setR30_upTo1Month(rs.getBigDecimal("R30_UP_TO_1_MONTH"));
		obj.setR30_moreThan1MonthTo3Months(rs.getBigDecimal("R30_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR30_moreThan3MonthTo6Months(rs.getBigDecimal("R30_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR30_moreThan6MonthTo12Months(rs.getBigDecimal("R30_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR30_moreThan12MonthTo3Years(rs.getBigDecimal("R30_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR30_moreThan3YearsTo5Years(rs.getBigDecimal("R30_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR30_moreThan5YearsTo10Years(rs.getBigDecimal("R30_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR30_moreThan10Years(rs.getBigDecimal("R30_MORE_THAN_10_YEARS"));
		obj.setR30_total(rs.getBigDecimal("R30_TOTAL"));
		obj.setR31product(rs.getString("R31_PRODUCT"));
		obj.setR31_upTo1Month(rs.getBigDecimal("R31_UP_TO_1_MONTH"));
		obj.setR31_moreThan1MonthTo3Months(rs.getBigDecimal("R31_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR31_moreThan3MonthTo6Months(rs.getBigDecimal("R31_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR31_moreThan6MonthTo12Months(rs.getBigDecimal("R31_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR31_moreThan12MonthTo3Years(rs.getBigDecimal("R31_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR31_moreThan3YearsTo5Years(rs.getBigDecimal("R31_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR31_moreThan5YearsTo10Years(rs.getBigDecimal("R31_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR31_moreThan10Years(rs.getBigDecimal("R31_MORE_THAN_10_YEARS"));
		obj.setR31_total(rs.getBigDecimal("R31_TOTAL"));
		obj.setR32product(rs.getString("R32_PRODUCT"));
		obj.setR32_upTo1Month(rs.getBigDecimal("R32_UP_TO_1_MONTH"));
		obj.setR32_moreThan1MonthTo3Months(rs.getBigDecimal("R32_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR32_moreThan3MonthTo6Months(rs.getBigDecimal("R32_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR32_moreThan6MonthTo12Months(rs.getBigDecimal("R32_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR32_moreThan12MonthTo3Years(rs.getBigDecimal("R32_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR32_moreThan3YearsTo5Years(rs.getBigDecimal("R32_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR32_moreThan5YearsTo10Years(rs.getBigDecimal("R32_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR32_moreThan10Years(rs.getBigDecimal("R32_MORE_THAN_10_YEARS"));
		obj.setR32_total(rs.getBigDecimal("R32_TOTAL"));
		obj.setR33product(rs.getString("R33_PRODUCT"));
		obj.setR33_upTo1Month(rs.getBigDecimal("R33_UP_TO_1_MONTH"));
		obj.setR33_moreThan1MonthTo3Months(rs.getBigDecimal("R33_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR33_moreThan3MonthTo6Months(rs.getBigDecimal("R33_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR33_moreThan6MonthTo12Months(rs.getBigDecimal("R33_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR33_moreThan12MonthTo3Years(rs.getBigDecimal("R33_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR33_moreThan3YearsTo5Years(rs.getBigDecimal("R33_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR33_moreThan5YearsTo10Years(rs.getBigDecimal("R33_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR33_moreThan10Years(rs.getBigDecimal("R33_MORE_THAN_10_YEARS"));
		obj.setR33_total(rs.getBigDecimal("R33_TOTAL"));
		obj.setR34product(rs.getString("R34_PRODUCT"));
		obj.setR34_upTo1Month(rs.getBigDecimal("R34_UP_TO_1_MONTH"));
		obj.setR34_moreThan1MonthTo3Months(rs.getBigDecimal("R34_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR34_moreThan3MonthTo6Months(rs.getBigDecimal("R34_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR34_moreThan6MonthTo12Months(rs.getBigDecimal("R34_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR34_moreThan12MonthTo3Years(rs.getBigDecimal("R34_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR34_moreThan3YearsTo5Years(rs.getBigDecimal("R34_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR34_moreThan5YearsTo10Years(rs.getBigDecimal("R34_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR34_moreThan10Years(rs.getBigDecimal("R34_MORE_THAN_10_YEARS"));
		obj.setR34_total(rs.getBigDecimal("R34_TOTAL"));
		obj.setR35product(rs.getString("R35_PRODUCT"));
		obj.setR35_upTo1Month(rs.getBigDecimal("R35_UP_TO_1_MONTH"));
		obj.setR35_moreThan1MonthTo3Months(rs.getBigDecimal("R35_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR35_moreThan3MonthTo6Months(rs.getBigDecimal("R35_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR35_moreThan6MonthTo12Months(rs.getBigDecimal("R35_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR35_moreThan12MonthTo3Years(rs.getBigDecimal("R35_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR35_moreThan3YearsTo5Years(rs.getBigDecimal("R35_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR35_moreThan5YearsTo10Years(rs.getBigDecimal("R35_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR35_moreThan10Years(rs.getBigDecimal("R35_MORE_THAN_10_YEARS"));
		obj.setR35_total(rs.getBigDecimal("R35_TOTAL"));
		obj.setR36product(rs.getString("R36_PRODUCT"));
		obj.setR36_upTo1Month(rs.getBigDecimal("R36_UP_TO_1_MONTH"));
		obj.setR36_moreThan1MonthTo3Months(rs.getBigDecimal("R36_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR36_moreThan3MonthTo6Months(rs.getBigDecimal("R36_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR36_moreThan6MonthTo12Months(rs.getBigDecimal("R36_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR36_moreThan12MonthTo3Years(rs.getBigDecimal("R36_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR36_moreThan3YearsTo5Years(rs.getBigDecimal("R36_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR36_moreThan5YearsTo10Years(rs.getBigDecimal("R36_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR36_moreThan10Years(rs.getBigDecimal("R36_MORE_THAN_10_YEARS"));
		obj.setR36_total(rs.getBigDecimal("R36_TOTAL"));
		obj.setR37product(rs.getString("R37_PRODUCT"));
		obj.setR37_upTo1Month(rs.getBigDecimal("R37_UP_TO_1_MONTH"));
		obj.setR37_moreThan1MonthTo3Months(rs.getBigDecimal("R37_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR37_moreThan3MonthTo6Months(rs.getBigDecimal("R37_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR37_moreThan6MonthTo12Months(rs.getBigDecimal("R37_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR37_moreThan12MonthTo3Years(rs.getBigDecimal("R37_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR37_moreThan3YearsTo5Years(rs.getBigDecimal("R37_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR37_moreThan5YearsTo10Years(rs.getBigDecimal("R37_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR37_moreThan10Years(rs.getBigDecimal("R37_MORE_THAN_10_YEARS"));
		obj.setR37_total(rs.getBigDecimal("R37_TOTAL"));
		obj.setR38product(rs.getString("R38_PRODUCT"));
		obj.setR38_upTo1Month(rs.getBigDecimal("R38_UP_TO_1_MONTH"));
		obj.setR38_moreThan1MonthTo3Months(rs.getBigDecimal("R38_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR38_moreThan3MonthTo6Months(rs.getBigDecimal("R38_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR38_moreThan6MonthTo12Months(rs.getBigDecimal("R38_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR38_moreThan12MonthTo3Years(rs.getBigDecimal("R38_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR38_moreThan3YearsTo5Years(rs.getBigDecimal("R38_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR38_moreThan5YearsTo10Years(rs.getBigDecimal("R38_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR38_moreThan10Years(rs.getBigDecimal("R38_MORE_THAN_10_YEARS"));
		obj.setR38_total(rs.getBigDecimal("R38_TOTAL"));
		obj.setR39product(rs.getString("R39_PRODUCT"));
		obj.setR39_upTo1Month(rs.getBigDecimal("R39_UP_TO_1_MONTH"));
		obj.setR39_moreThan1MonthTo3Months(rs.getBigDecimal("R39_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR39_moreThan3MonthTo6Months(rs.getBigDecimal("R39_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR39_moreThan6MonthTo12Months(rs.getBigDecimal("R39_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR39_moreThan12MonthTo3Years(rs.getBigDecimal("R39_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR39_moreThan3YearsTo5Years(rs.getBigDecimal("R39_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR39_moreThan5YearsTo10Years(rs.getBigDecimal("R39_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR39_moreThan10Years(rs.getBigDecimal("R39_MORE_THAN_10_YEARS"));
		obj.setR39_total(rs.getBigDecimal("R39_TOTAL"));
		obj.setR40product(rs.getString("R40_PRODUCT"));
		obj.setR40_upTo1Month(rs.getBigDecimal("R40_UP_TO_1_MONTH"));
		obj.setR40_moreThan1MonthTo3Months(rs.getBigDecimal("R40_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR40_moreThan3MonthTo6Months(rs.getBigDecimal("R40_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR40_moreThan6MonthTo12Months(rs.getBigDecimal("R40_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR40_moreThan12MonthTo3Years(rs.getBigDecimal("R40_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR40_moreThan3YearsTo5Years(rs.getBigDecimal("R40_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR40_moreThan5YearsTo10Years(rs.getBigDecimal("R40_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR40_moreThan10Years(rs.getBigDecimal("R40_MORE_THAN_10_YEARS"));
		obj.setR40_total(rs.getBigDecimal("R40_TOTAL"));
		obj.setR41product(rs.getString("R41_PRODUCT"));
		obj.setR41_upTo1Month(rs.getBigDecimal("R41_UP_TO_1_MONTH"));
		obj.setR41_moreThan1MonthTo3Months(rs.getBigDecimal("R41_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR41_moreThan3MonthTo6Months(rs.getBigDecimal("R41_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR41_moreThan6MonthTo12Months(rs.getBigDecimal("R41_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR41_moreThan12MonthTo3Years(rs.getBigDecimal("R41_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR41_moreThan3YearsTo5Years(rs.getBigDecimal("R41_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR41_moreThan5YearsTo10Years(rs.getBigDecimal("R41_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR41_moreThan10Years(rs.getBigDecimal("R41_MORE_THAN_10_YEARS"));
		obj.setR41_total(rs.getBigDecimal("R41_TOTAL"));
		obj.setR42product(rs.getString("R42_PRODUCT"));
		obj.setR42_upTo1Month(rs.getBigDecimal("R42_UP_TO_1_MONTH"));
		obj.setR42_moreThan1MonthTo3Months(rs.getBigDecimal("R42_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR42_moreThan3MonthTo6Months(rs.getBigDecimal("R42_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR42_moreThan6MonthTo12Months(rs.getBigDecimal("R42_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR42_moreThan12MonthTo3Years(rs.getBigDecimal("R42_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR42_moreThan3YearsTo5Years(rs.getBigDecimal("R42_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR42_moreThan5YearsTo10Years(rs.getBigDecimal("R42_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR42_moreThan10Years(rs.getBigDecimal("R42_MORE_THAN_10_YEARS"));
		obj.setR42_total(rs.getBigDecimal("R42_TOTAL"));
		obj.setR43product(rs.getString("R43_PRODUCT"));
		obj.setR43_upTo1Month(rs.getBigDecimal("R43_UP_TO_1_MONTH"));
		obj.setR43_moreThan1MonthTo3Months(rs.getBigDecimal("R43_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR43_moreThan3MonthTo6Months(rs.getBigDecimal("R43_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR43_moreThan6MonthTo12Months(rs.getBigDecimal("R43_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR43_moreThan12MonthTo3Years(rs.getBigDecimal("R43_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR43_moreThan3YearsTo5Years(rs.getBigDecimal("R43_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR43_moreThan5YearsTo10Years(rs.getBigDecimal("R43_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR43_moreThan10Years(rs.getBigDecimal("R43_MORE_THAN_10_YEARS"));
		obj.setR43_total(rs.getBigDecimal("R43_TOTAL"));
		obj.setR44product(rs.getString("R44_PRODUCT"));
		obj.setR44_upTo1Month(rs.getBigDecimal("R44_UP_TO_1_MONTH"));
		obj.setR44_moreThan1MonthTo3Months(rs.getBigDecimal("R44_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR44_moreThan3MonthTo6Months(rs.getBigDecimal("R44_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR44_moreThan6MonthTo12Months(rs.getBigDecimal("R44_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR44_moreThan12MonthTo3Years(rs.getBigDecimal("R44_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR44_moreThan3YearsTo5Years(rs.getBigDecimal("R44_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR44_moreThan5YearsTo10Years(rs.getBigDecimal("R44_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR44_moreThan10Years(rs.getBigDecimal("R44_MORE_THAN_10_YEARS"));
		obj.setR44_total(rs.getBigDecimal("R44_TOTAL"));
		obj.setR45product(rs.getString("R45_PRODUCT"));
		obj.setR45_upTo1Month(rs.getBigDecimal("R45_UP_TO_1_MONTH"));
		obj.setR45_moreThan1MonthTo3Months(rs.getBigDecimal("R45_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR45_moreThan3MonthTo6Months(rs.getBigDecimal("R45_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR45_moreThan6MonthTo12Months(rs.getBigDecimal("R45_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR45_moreThan12MonthTo3Years(rs.getBigDecimal("R45_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR45_moreThan3YearsTo5Years(rs.getBigDecimal("R45_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR45_moreThan5YearsTo10Years(rs.getBigDecimal("R45_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR45_moreThan10Years(rs.getBigDecimal("R45_MORE_THAN_10_YEARS"));
		obj.setR45_total(rs.getBigDecimal("R45_TOTAL"));
		obj.setR46product(rs.getString("R46_PRODUCT"));
		obj.setR46_upTo1Month(rs.getBigDecimal("R46_UP_TO_1_MONTH"));
		obj.setR46_moreThan1MonthTo3Months(rs.getBigDecimal("R46_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR46_moreThan3MonthTo6Months(rs.getBigDecimal("R46_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR46_moreThan6MonthTo12Months(rs.getBigDecimal("R46_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR46_moreThan12MonthTo3Years(rs.getBigDecimal("R46_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR46_moreThan3YearsTo5Years(rs.getBigDecimal("R46_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR46_moreThan5YearsTo10Years(rs.getBigDecimal("R46_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR46_moreThan10Years(rs.getBigDecimal("R46_MORE_THAN_10_YEARS"));
		obj.setR46_total(rs.getBigDecimal("R46_TOTAL"));
		obj.setR47_nonRatioSensativeItems(rs.getBigDecimal("R47_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR47_total(rs.getBigDecimal("R47_TOTAL"));
		obj.setR48_nonRatioSensativeItems(rs.getBigDecimal("R48_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR48_total(rs.getBigDecimal("R48_TOTAL"));
		obj.setR49_nonRatioSensativeItems(rs.getBigDecimal("R49_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR49_total(rs.getBigDecimal("R49_TOTAL"));
		obj.setR50_nonRatioSensativeItems(rs.getBigDecimal("R50_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR50_total(rs.getBigDecimal("R50_TOTAL"));
		obj.setR51_nonRatioSensativeItems(rs.getBigDecimal("R51_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR51_total(rs.getBigDecimal("R51_TOTAL"));
		obj.setR52_nonRatioSensativeItems(rs.getBigDecimal("R52_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR52_total(rs.getBigDecimal("R52_TOTAL"));
		obj.setR53_nonRatioSensativeItems(rs.getBigDecimal("R53_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR53_total(rs.getBigDecimal("R53_TOTAL"));
		obj.setR54_nonRatioSensativeItems(rs.getBigDecimal("R54_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR54_total(rs.getBigDecimal("R54_TOTAL"));
		obj.setR55_nonRatioSensativeItems(rs.getBigDecimal("R55_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR55_total(rs.getBigDecimal("R55_TOTAL"));
		obj.setR56_nonRatioSensativeItems(rs.getBigDecimal("R56_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR56_total(rs.getBigDecimal("R56_TOTAL"));
		obj.setR57_nonRatioSensativeItems(rs.getBigDecimal("R57_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR57_total(rs.getBigDecimal("R57_TOTAL"));
		obj.setR58_nonRatioSensativeItems(rs.getBigDecimal("R58_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR58_total(rs.getBigDecimal("R58_TOTAL"));
		obj.setR59product(rs.getString("R59_PRODUCT"));
		obj.setR59_upTo1Month(rs.getBigDecimal("R59_UP_TO_1_MONTH"));
		obj.setR59_moreThan1MonthTo3Months(rs.getBigDecimal("R59_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR59_moreThan3MonthTo6Months(rs.getBigDecimal("R59_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR59_moreThan6MonthTo12Months(rs.getBigDecimal("R59_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR59_moreThan12MonthTo3Years(rs.getBigDecimal("R59_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR59_moreThan3YearsTo5Years(rs.getBigDecimal("R59_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR59_moreThan5YearsTo10Years(rs.getBigDecimal("R59_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR59_moreThan10Years(rs.getBigDecimal("R59_MORE_THAN_10_YEARS"));
		obj.setR59_nonRatioSensativeItems(rs.getBigDecimal("R59_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR59_total(rs.getBigDecimal("R59_TOTAL"));
		obj.setR60product(rs.getString("R60_PRODUCT"));
		obj.setR60_upTo1Month(rs.getBigDecimal("R60_UP_TO_1_MONTH"));
		obj.setR60_moreThan1MonthTo3Months(rs.getBigDecimal("R60_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR60_moreThan3MonthTo6Months(rs.getBigDecimal("R60_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR60_moreThan6MonthTo12Months(rs.getBigDecimal("R60_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR60_moreThan12MonthTo3Years(rs.getBigDecimal("R60_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR60_moreThan3YearsTo5Years(rs.getBigDecimal("R60_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR60_moreThan5YearsTo10Years(rs.getBigDecimal("R60_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR60_moreThan10Years(rs.getBigDecimal("R60_MORE_THAN_10_YEARS"));
		obj.setR60_total(rs.getBigDecimal("R60_TOTAL"));
		obj.setR61product(rs.getString("R61_PRODUCT"));
		obj.setR61_upTo1Month(rs.getBigDecimal("R61_UP_TO_1_MONTH"));
		obj.setR61_moreThan1MonthTo3Months(rs.getBigDecimal("R61_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR61_moreThan3MonthTo6Months(rs.getBigDecimal("R61_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR61_moreThan6MonthTo12Months(rs.getBigDecimal("R61_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR61_moreThan12MonthTo3Years(rs.getBigDecimal("R61_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR61_moreThan3YearsTo5Years(rs.getBigDecimal("R61_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR61_moreThan5YearsTo10Years(rs.getBigDecimal("R61_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR61_moreThan10Years(rs.getBigDecimal("R61_MORE_THAN_10_YEARS"));
		obj.setR61_total(rs.getBigDecimal("R61_TOTAL"));
		obj.setR62product(rs.getString("R62_PRODUCT"));
		obj.setR62_upTo1Month(rs.getBigDecimal("R62_UP_TO_1_MONTH"));
		obj.setR62_moreThan1MonthTo3Months(rs.getBigDecimal("R62_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR62_moreThan3MonthTo6Months(rs.getBigDecimal("R62_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR62_moreThan6MonthTo12Months(rs.getBigDecimal("R62_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR62_moreThan12MonthTo3Years(rs.getBigDecimal("R62_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR62_moreThan3YearsTo5Years(rs.getBigDecimal("R62_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR62_moreThan5YearsTo10Years(rs.getBigDecimal("R62_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR62_moreThan10Years(rs.getBigDecimal("R62_MORE_THAN_10_YEARS"));
		obj.setR62_total(rs.getBigDecimal("R62_TOTAL"));
		obj.setR63product(rs.getString("R63_PRODUCT"));
		obj.setR63_upTo1Month(rs.getBigDecimal("R63_UP_TO_1_MONTH"));
		obj.setR63_moreThan1MonthTo3Months(rs.getBigDecimal("R63_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR63_moreThan3MonthTo6Months(rs.getBigDecimal("R63_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR63_moreThan6MonthTo12Months(rs.getBigDecimal("R63_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR63_moreThan12MonthTo3Years(rs.getBigDecimal("R63_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR63_moreThan3YearsTo5Years(rs.getBigDecimal("R63_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR63_moreThan5YearsTo10Years(rs.getBigDecimal("R63_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR63_moreThan10Years(rs.getBigDecimal("R63_MORE_THAN_10_YEARS"));
		obj.setR63_total(rs.getBigDecimal("R63_TOTAL"));
		obj.setR64product(rs.getString("R64_PRODUCT"));
		obj.setR64_upTo1Month(rs.getBigDecimal("R64_UP_TO_1_MONTH"));
		obj.setR64_moreThan1MonthTo3Months(rs.getBigDecimal("R64_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR64_moreThan3MonthTo6Months(rs.getBigDecimal("R64_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR64_moreThan6MonthTo12Months(rs.getBigDecimal("R64_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR64_moreThan12MonthTo3Years(rs.getBigDecimal("R64_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR64_moreThan3YearsTo5Years(rs.getBigDecimal("R64_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR64_moreThan5YearsTo10Years(rs.getBigDecimal("R64_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR64_moreThan10Years(rs.getBigDecimal("R64_MORE_THAN_10_YEARS"));
		obj.setR64_total(rs.getBigDecimal("R64_TOTAL"));
		obj.setR65product(rs.getString("R65_PRODUCT"));
		obj.setR65_upTo1Month(rs.getBigDecimal("R65_UP_TO_1_MONTH"));
		obj.setR65_moreThan1MonthTo3Months(rs.getBigDecimal("R65_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR65_moreThan3MonthTo6Months(rs.getBigDecimal("R65_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR65_moreThan6MonthTo12Months(rs.getBigDecimal("R65_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR65_moreThan12MonthTo3Years(rs.getBigDecimal("R65_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR65_moreThan3YearsTo5Years(rs.getBigDecimal("R65_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR65_moreThan5YearsTo10Years(rs.getBigDecimal("R65_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR65_moreThan10Years(rs.getBigDecimal("R65_MORE_THAN_10_YEARS"));
		obj.setR65_total(rs.getBigDecimal("R65_TOTAL"));
		obj.setR66product(rs.getString("R66_PRODUCT"));
		obj.setR66_upTo1Month(rs.getBigDecimal("R66_UP_TO_1_MONTH"));
		obj.setR66_moreThan1MonthTo3Months(rs.getBigDecimal("R66_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR66_moreThan3MonthTo6Months(rs.getBigDecimal("R66_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR66_moreThan6MonthTo12Months(rs.getBigDecimal("R66_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR66_moreThan12MonthTo3Years(rs.getBigDecimal("R66_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR66_moreThan3YearsTo5Years(rs.getBigDecimal("R66_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR66_moreThan5YearsTo10Years(rs.getBigDecimal("R66_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR66_moreThan10Years(rs.getBigDecimal("R66_MORE_THAN_10_YEARS"));
		obj.setR66_total(rs.getBigDecimal("R66_TOTAL"));
		obj.setR67product(rs.getString("R67_PRODUCT"));
		obj.setR67_upTo1Month(rs.getBigDecimal("R67_UP_TO_1_MONTH"));
		obj.setR67_moreThan1MonthTo3Months(rs.getBigDecimal("R67_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR67_moreThan3MonthTo6Months(rs.getBigDecimal("R67_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR67_moreThan6MonthTo12Months(rs.getBigDecimal("R67_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR67_moreThan12MonthTo3Years(rs.getBigDecimal("R67_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR67_moreThan3YearsTo5Years(rs.getBigDecimal("R67_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR67_moreThan5YearsTo10Years(rs.getBigDecimal("R67_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR67_moreThan10Years(rs.getBigDecimal("R67_MORE_THAN_10_YEARS"));
		obj.setR67_total(rs.getBigDecimal("R67_TOTAL"));
		obj.setR68product(rs.getString("R68_PRODUCT"));
		obj.setR68_upTo1Month(rs.getBigDecimal("R68_UP_TO_1_MONTH"));
		obj.setR68_moreThan1MonthTo3Months(rs.getBigDecimal("R68_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR68_moreThan3MonthTo6Months(rs.getBigDecimal("R68_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR68_moreThan6MonthTo12Months(rs.getBigDecimal("R68_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR68_moreThan12MonthTo3Years(rs.getBigDecimal("R68_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR68_moreThan3YearsTo5Years(rs.getBigDecimal("R68_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR68_moreThan5YearsTo10Years(rs.getBigDecimal("R68_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR68_moreThan10Years(rs.getBigDecimal("R68_MORE_THAN_10_YEARS"));
		obj.setR68_total(rs.getBigDecimal("R68_TOTAL"));
		obj.setR69product(rs.getString("R69_PRODUCT"));
		obj.setR69_upTo1Month(rs.getBigDecimal("R69_UP_TO_1_MONTH"));
		obj.setR69_moreThan1MonthTo3Months(rs.getBigDecimal("R69_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR69_moreThan3MonthTo6Months(rs.getBigDecimal("R69_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR69_moreThan6MonthTo12Months(rs.getBigDecimal("R69_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR69_moreThan12MonthTo3Years(rs.getBigDecimal("R69_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR69_moreThan3YearsTo5Years(rs.getBigDecimal("R69_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR69_moreThan5YearsTo10Years(rs.getBigDecimal("R69_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR69_moreThan10Years(rs.getBigDecimal("R69_MORE_THAN_10_YEARS"));
		obj.setR69_total(rs.getBigDecimal("R69_TOTAL"));
		obj.setR70product(rs.getString("R70_PRODUCT"));
		obj.setR70_upTo1Month(rs.getBigDecimal("R70_UP_TO_1_MONTH"));
		obj.setR70_moreThan1MonthTo3Months(rs.getBigDecimal("R70_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR70_moreThan3MonthTo6Months(rs.getBigDecimal("R70_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR70_moreThan6MonthTo12Months(rs.getBigDecimal("R70_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR70_moreThan12MonthTo3Years(rs.getBigDecimal("R70_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR70_moreThan3YearsTo5Years(rs.getBigDecimal("R70_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR70_moreThan5YearsTo10Years(rs.getBigDecimal("R70_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR70_moreThan10Years(rs.getBigDecimal("R70_MORE_THAN_10_YEARS"));
		obj.setR70_total(rs.getBigDecimal("R70_TOTAL"));
		obj.setR71product(rs.getString("R71_PRODUCT"));
		obj.setR71_upTo1Month(rs.getBigDecimal("R71_UP_TO_1_MONTH"));
		obj.setR71_moreThan1MonthTo3Months(rs.getBigDecimal("R71_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR71_moreThan3MonthTo6Months(rs.getBigDecimal("R71_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR71_moreThan6MonthTo12Months(rs.getBigDecimal("R71_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR71_moreThan12MonthTo3Years(rs.getBigDecimal("R71_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR71_moreThan3YearsTo5Years(rs.getBigDecimal("R71_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR71_moreThan5YearsTo10Years(rs.getBigDecimal("R71_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR71_moreThan10Years(rs.getBigDecimal("R71_MORE_THAN_10_YEARS"));
		obj.setR71_total(rs.getBigDecimal("R71_TOTAL"));
		obj.setR72product(rs.getString("R72_PRODUCT"));
		obj.setR72_upTo1Month(rs.getBigDecimal("R72_UP_TO_1_MONTH"));
		obj.setR72_moreThan1MonthTo3Months(rs.getBigDecimal("R72_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR72_moreThan3MonthTo6Months(rs.getBigDecimal("R72_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR72_moreThan6MonthTo12Months(rs.getBigDecimal("R72_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR72_moreThan12MonthTo3Years(rs.getBigDecimal("R72_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR72_moreThan3YearsTo5Years(rs.getBigDecimal("R72_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR72_moreThan5YearsTo10Years(rs.getBigDecimal("R72_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR72_moreThan10Years(rs.getBigDecimal("R72_MORE_THAN_10_YEARS"));
		obj.setR72_total(rs.getBigDecimal("R72_TOTAL"));
		obj.setR73product(rs.getString("R73_PRODUCT"));
		obj.setR73_upTo1Month(rs.getBigDecimal("R73_UP_TO_1_MONTH"));
		obj.setR73_moreThan1MonthTo3Months(rs.getBigDecimal("R73_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR73_moreThan3MonthTo6Months(rs.getBigDecimal("R73_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR73_moreThan6MonthTo12Months(rs.getBigDecimal("R73_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR73_moreThan12MonthTo3Years(rs.getBigDecimal("R73_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR73_moreThan3YearsTo5Years(rs.getBigDecimal("R73_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR73_moreThan5YearsTo10Years(rs.getBigDecimal("R73_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR73_moreThan10Years(rs.getBigDecimal("R73_MORE_THAN_10_YEARS"));
		obj.setR73_total(rs.getBigDecimal("R73_TOTAL"));
		obj.setR74product(rs.getString("R74_PRODUCT"));
		obj.setR74_upTo1Month(rs.getBigDecimal("R74_UP_TO_1_MONTH"));
		obj.setR74_moreThan1MonthTo3Months(rs.getBigDecimal("R74_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR74_moreThan3MonthTo6Months(rs.getBigDecimal("R74_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR74_moreThan6MonthTo12Months(rs.getBigDecimal("R74_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR74_moreThan12MonthTo3Years(rs.getBigDecimal("R74_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR74_moreThan3YearsTo5Years(rs.getBigDecimal("R74_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR74_moreThan5YearsTo10Years(rs.getBigDecimal("R74_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR74_moreThan10Years(rs.getBigDecimal("R74_MORE_THAN_10_YEARS"));
		obj.setR74_total(rs.getBigDecimal("R74_TOTAL"));
		obj.setR75product(rs.getString("R75_PRODUCT"));
		obj.setR75_upTo1Month(rs.getBigDecimal("R75_UP_TO_1_MONTH"));
		obj.setR75_moreThan1MonthTo3Months(rs.getBigDecimal("R75_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR75_moreThan3MonthTo6Months(rs.getBigDecimal("R75_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR75_moreThan6MonthTo12Months(rs.getBigDecimal("R75_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR75_moreThan12MonthTo3Years(rs.getBigDecimal("R75_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR75_moreThan3YearsTo5Years(rs.getBigDecimal("R75_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR75_moreThan5YearsTo10Years(rs.getBigDecimal("R75_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR75_moreThan10Years(rs.getBigDecimal("R75_MORE_THAN_10_YEARS"));
		obj.setR75_total(rs.getBigDecimal("R75_TOTAL"));
		obj.setR76product(rs.getString("R76_PRODUCT"));
		obj.setR76_upTo1Month(rs.getBigDecimal("R76_UP_TO_1_MONTH"));
		obj.setR76_moreThan1MonthTo3Months(rs.getBigDecimal("R76_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR76_moreThan3MonthTo6Months(rs.getBigDecimal("R76_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR76_moreThan6MonthTo12Months(rs.getBigDecimal("R76_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR76_moreThan12MonthTo3Years(rs.getBigDecimal("R76_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR76_moreThan3YearsTo5Years(rs.getBigDecimal("R76_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR76_moreThan5YearsTo10Years(rs.getBigDecimal("R76_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR76_moreThan10Years(rs.getBigDecimal("R76_MORE_THAN_10_YEARS"));
		obj.setR76_total(rs.getBigDecimal("R76_TOTAL"));
		obj.setR77product(rs.getString("R77_PRODUCT"));
		obj.setR77_upTo1Month(rs.getBigDecimal("R77_UP_TO_1_MONTH"));
		obj.setR77_moreThan1MonthTo3Months(rs.getBigDecimal("R77_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR77_moreThan3MonthTo6Months(rs.getBigDecimal("R77_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR77_moreThan6MonthTo12Months(rs.getBigDecimal("R77_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR77_moreThan12MonthTo3Years(rs.getBigDecimal("R77_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR77_moreThan3YearsTo5Years(rs.getBigDecimal("R77_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR77_moreThan5YearsTo10Years(rs.getBigDecimal("R77_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR77_moreThan10Years(rs.getBigDecimal("R77_MORE_THAN_10_YEARS"));
		obj.setR77_total(rs.getBigDecimal("R77_TOTAL"));
		obj.setR78product(rs.getString("R78_PRODUCT"));
		obj.setR78_upTo1Month(rs.getBigDecimal("R78_UP_TO_1_MONTH"));
		obj.setR78_moreThan1MonthTo3Months(rs.getBigDecimal("R78_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR78_moreThan3MonthTo6Months(rs.getBigDecimal("R78_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR78_moreThan6MonthTo12Months(rs.getBigDecimal("R78_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR78_moreThan12MonthTo3Years(rs.getBigDecimal("R78_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR78_moreThan3YearsTo5Years(rs.getBigDecimal("R78_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR78_moreThan5YearsTo10Years(rs.getBigDecimal("R78_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR78_moreThan10Years(rs.getBigDecimal("R78_MORE_THAN_10_YEARS"));
		obj.setR78_total(rs.getBigDecimal("R78_TOTAL"));
		obj.setR79product(rs.getString("R79_PRODUCT"));
		obj.setR79_upTo1Month(rs.getBigDecimal("R79_UP_TO_1_MONTH"));
		obj.setR79_moreThan1MonthTo3Months(rs.getBigDecimal("R79_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR79_moreThan3MonthTo6Months(rs.getBigDecimal("R79_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR79_moreThan6MonthTo12Months(rs.getBigDecimal("R79_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR79_moreThan12MonthTo3Years(rs.getBigDecimal("R79_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR79_moreThan3YearsTo5Years(rs.getBigDecimal("R79_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR79_moreThan5YearsTo10Years(rs.getBigDecimal("R79_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR79_moreThan10Years(rs.getBigDecimal("R79_MORE_THAN_10_YEARS"));
		obj.setR79_total(rs.getBigDecimal("R79_TOTAL"));
		obj.setR80product(rs.getString("R80_PRODUCT"));
		obj.setR80_upTo1Month(rs.getBigDecimal("R80_UP_TO_1_MONTH"));
		obj.setR80_moreThan1MonthTo3Months(rs.getBigDecimal("R80_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR80_moreThan3MonthTo6Months(rs.getBigDecimal("R80_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR80_moreThan6MonthTo12Months(rs.getBigDecimal("R80_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR80_moreThan12MonthTo3Years(rs.getBigDecimal("R80_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR80_moreThan3YearsTo5Years(rs.getBigDecimal("R80_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR80_moreThan5YearsTo10Years(rs.getBigDecimal("R80_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR80_moreThan10Years(rs.getBigDecimal("R80_MORE_THAN_10_YEARS"));
		obj.setR80_total(rs.getBigDecimal("R80_TOTAL"));
		obj.setR81product(rs.getString("R81_PRODUCT"));
		obj.setR81_upTo1Month(rs.getBigDecimal("R81_UP_TO_1_MONTH"));
		obj.setR81_moreThan1MonthTo3Months(rs.getBigDecimal("R81_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR81_moreThan3MonthTo6Months(rs.getBigDecimal("R81_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR81_moreThan6MonthTo12Months(rs.getBigDecimal("R81_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR81_moreThan12MonthTo3Years(rs.getBigDecimal("R81_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR81_moreThan3YearsTo5Years(rs.getBigDecimal("R81_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR81_moreThan5YearsTo10Years(rs.getBigDecimal("R81_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR81_moreThan10Years(rs.getBigDecimal("R81_MORE_THAN_10_YEARS"));
		obj.setR81_total(rs.getBigDecimal("R81_TOTAL"));
		obj.setR82product(rs.getString("R82_PRODUCT"));
		obj.setR82_upTo1Month(rs.getBigDecimal("R82_UP_TO_1_MONTH"));
		obj.setR82_moreThan1MonthTo3Months(rs.getBigDecimal("R82_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR82_moreThan3MonthTo6Months(rs.getBigDecimal("R82_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR82_moreThan6MonthTo12Months(rs.getBigDecimal("R82_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR82_moreThan12MonthTo3Years(rs.getBigDecimal("R82_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR82_moreThan3YearsTo5Years(rs.getBigDecimal("R82_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR82_moreThan5YearsTo10Years(rs.getBigDecimal("R82_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR82_moreThan10Years(rs.getBigDecimal("R82_MORE_THAN_10_YEARS"));
		obj.setR82_total(rs.getBigDecimal("R82_TOTAL"));
		obj.setR83product(rs.getString("R83_PRODUCT"));
		obj.setR83_upTo1Month(rs.getBigDecimal("R83_UP_TO_1_MONTH"));
		obj.setR83_moreThan1MonthTo3Months(rs.getBigDecimal("R83_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR83_moreThan3MonthTo6Months(rs.getBigDecimal("R83_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR83_moreThan6MonthTo12Months(rs.getBigDecimal("R83_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR83_moreThan12MonthTo3Years(rs.getBigDecimal("R83_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR83_moreThan3YearsTo5Years(rs.getBigDecimal("R83_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR83_moreThan5YearsTo10Years(rs.getBigDecimal("R83_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR83_moreThan10Years(rs.getBigDecimal("R83_MORE_THAN_10_YEARS"));
		obj.setR83_total(rs.getBigDecimal("R83_TOTAL"));
		obj.setR84_nonRatioSensativeItems(rs.getBigDecimal("R84_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR84_total(rs.getBigDecimal("R84_TOTAL"));
		obj.setR85_nonRatioSensativeItems(rs.getBigDecimal("R85_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR85_total(rs.getBigDecimal("R85_TOTAL"));
		obj.setR86_nonRatioSensativeItems(rs.getBigDecimal("R86_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR86_total(rs.getBigDecimal("R86_TOTAL"));
		obj.setR87_nonRatioSensativeItems(rs.getBigDecimal("R87_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR87_total(rs.getBigDecimal("R87_TOTAL"));
		obj.setR88_nonRatioSensativeItems(rs.getBigDecimal("R88_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR88_total(rs.getBigDecimal("R88_TOTAL"));
		obj.setR89_nonRatioSensativeItems(rs.getBigDecimal("R89_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR89_total(rs.getBigDecimal("R89_TOTAL"));
		obj.setR90_nonRatioSensativeItems(rs.getBigDecimal("R90_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR90_total(rs.getBigDecimal("R90_TOTAL"));
		obj.setR91_nonRatioSensativeItems(rs.getBigDecimal("R91_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR91_total(rs.getBigDecimal("R91_TOTAL"));
		obj.setR92product(rs.getString("R92_PRODUCT"));
		obj.setR92_upTo1Month(rs.getBigDecimal("R92_UP_TO_1_MONTH"));
		obj.setR92_moreThan1MonthTo3Months(rs.getBigDecimal("R92_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR92_moreThan3MonthTo6Months(rs.getBigDecimal("R92_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR92_moreThan6MonthTo12Months(rs.getBigDecimal("R92_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR92_moreThan12MonthTo3Years(rs.getBigDecimal("R92_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR92_moreThan3YearsTo5Years(rs.getBigDecimal("R92_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR92_moreThan5YearsTo10Years(rs.getBigDecimal("R92_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR92_moreThan10Years(rs.getBigDecimal("R92_MORE_THAN_10_YEARS"));
		obj.setR92_total(rs.getBigDecimal("R92_TOTAL"));
		obj.setR93product(rs.getString("R93_PRODUCT"));
		obj.setR93_upTo1Month(rs.getBigDecimal("R93_UP_TO_1_MONTH"));
		obj.setR93_moreThan1MonthTo3Months(rs.getBigDecimal("R93_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR93_moreThan3MonthTo6Months(rs.getBigDecimal("R93_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR93_moreThan6MonthTo12Months(rs.getBigDecimal("R93_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR93_moreThan12MonthTo3Years(rs.getBigDecimal("R93_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR93_moreThan3YearsTo5Years(rs.getBigDecimal("R93_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR93_moreThan5YearsTo10Years(rs.getBigDecimal("R93_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR93_moreThan10Years(rs.getBigDecimal("R93_MORE_THAN_10_YEARS"));
		obj.setR93_total(rs.getBigDecimal("R93_TOTAL"));
		obj.setR94product(rs.getString("R94_PRODUCT"));
		obj.setR94_upTo1Month(rs.getBigDecimal("R94_UP_TO_1_MONTH"));
		obj.setR94_moreThan1MonthTo3Months(rs.getBigDecimal("R94_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR94_moreThan3MonthTo6Months(rs.getBigDecimal("R94_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR94_moreThan6MonthTo12Months(rs.getBigDecimal("R94_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR94_moreThan12MonthTo3Years(rs.getBigDecimal("R94_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR94_moreThan3YearsTo5Years(rs.getBigDecimal("R94_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR94_moreThan5YearsTo10Years(rs.getBigDecimal("R94_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR94_moreThan10Years(rs.getBigDecimal("R94_MORE_THAN_10_YEARS"));
		obj.setR94_total(rs.getBigDecimal("R94_TOTAL"));
		obj.setR95_nonRatioSensativeItems(rs.getBigDecimal("R95_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR95_total(rs.getBigDecimal("R95_TOTAL"));
		obj.setR96product(rs.getString("R96_PRODUCT"));
		obj.setR96_upTo1Month(rs.getBigDecimal("R96_UP_TO_1_MONTH"));
		obj.setR96_moreThan1MonthTo3Months(rs.getBigDecimal("R96_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR96_moreThan3MonthTo6Months(rs.getBigDecimal("R96_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR96_moreThan6MonthTo12Months(rs.getBigDecimal("R96_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR96_moreThan12MonthTo3Years(rs.getBigDecimal("R96_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR96_moreThan3YearsTo5Years(rs.getBigDecimal("R96_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR96_moreThan5YearsTo10Years(rs.getBigDecimal("R96_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR96_moreThan10Years(rs.getBigDecimal("R96_MORE_THAN_10_YEARS"));
		obj.setR96_nonRatioSensativeItems(rs.getBigDecimal("R96_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR96_total(rs.getBigDecimal("R96_TOTAL"));
		obj.setR97product(rs.getString("R97_PRODUCT"));
		obj.setR97_upTo1Month(rs.getBigDecimal("R97_UP_TO_1_MONTH"));
		obj.setR97_moreThan1MonthTo3Months(rs.getBigDecimal("R97_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR97_moreThan3MonthTo6Months(rs.getBigDecimal("R97_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR97_moreThan6MonthTo12Months(rs.getBigDecimal("R97_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR97_moreThan12MonthTo3Years(rs.getBigDecimal("R97_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR97_moreThan3YearsTo5Years(rs.getBigDecimal("R97_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR97_moreThan5YearsTo10Years(rs.getBigDecimal("R97_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR97_moreThan10Years(rs.getBigDecimal("R97_MORE_THAN_10_YEARS"));
		obj.setR97_nonRatioSensativeItems(rs.getBigDecimal("R97_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR97_total(rs.getBigDecimal("R97_TOTAL"));
		obj.setR98product(rs.getString("R98_PRODUCT"));
		obj.setR98_upTo1Month(rs.getBigDecimal("R98_UP_TO_1_MONTH"));
		obj.setR98_moreThan1MonthTo3Months(rs.getBigDecimal("R98_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR98_moreThan3MonthTo6Months(rs.getBigDecimal("R98_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR98_moreThan6MonthTo12Months(rs.getBigDecimal("R98_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR98_moreThan12MonthTo3Years(rs.getBigDecimal("R98_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR98_moreThan3YearsTo5Years(rs.getBigDecimal("R98_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR98_moreThan5YearsTo10Years(rs.getBigDecimal("R98_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR98_moreThan10Years(rs.getBigDecimal("R98_MORE_THAN_10_YEARS"));
		obj.setR98_nonRatioSensativeItems(rs.getBigDecimal("R98_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR98_total(rs.getBigDecimal("R98_TOTAL"));
		obj.setR99product(rs.getString("R99_PRODUCT"));
		obj.setR99_upTo1Month(rs.getBigDecimal("R99_UP_TO_1_MONTH"));
		obj.setR99_moreThan1MonthTo3Months(rs.getBigDecimal("R99_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR99_moreThan3MonthTo6Months(rs.getBigDecimal("R99_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR99_moreThan6MonthTo12Months(rs.getBigDecimal("R99_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR99_moreThan12MonthTo3Years(rs.getBigDecimal("R99_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR99_moreThan3YearsTo5Years(rs.getBigDecimal("R99_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR99_moreThan5YearsTo10Years(rs.getBigDecimal("R99_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR99_moreThan10Years(rs.getBigDecimal("R99_MORE_THAN_10_YEARS"));
		obj.setR99_nonRatioSensativeItems(rs.getBigDecimal("R99_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR99_total(rs.getBigDecimal("R99_TOTAL"));
		obj.setR100product(rs.getString("R100_PRODUCT"));
		obj.setR100_upTo1Month(rs.getBigDecimal("R100_UP_TO_1_MONTH"));
		obj.setR100_moreThan1MonthTo3Months(rs.getBigDecimal("R100_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR100_moreThan3MonthTo6Months(rs.getBigDecimal("R100_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR100_moreThan6MonthTo12Months(rs.getBigDecimal("R100_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR100_moreThan12MonthTo3Years(rs.getBigDecimal("R100_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR100_moreThan3YearsTo5Years(rs.getBigDecimal("R100_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR100_moreThan5YearsTo10Years(rs.getBigDecimal("R100_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR100_moreThan10Years(rs.getBigDecimal("R100_MORE_THAN_10_YEARS"));
		obj.setR100_nonRatioSensativeItems(rs.getBigDecimal("R100_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR100_total(rs.getBigDecimal("R100_TOTAL"));
		obj.setR101product(rs.getString("R101_PRODUCT"));
		obj.setR101_upTo1Month(rs.getBigDecimal("R101_UP_TO_1_MONTH"));
		obj.setR101_moreThan1MonthTo3Months(rs.getBigDecimal("R101_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR101_moreThan3MonthTo6Months(rs.getBigDecimal("R101_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR101_moreThan6MonthTo12Months(rs.getBigDecimal("R101_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR101_moreThan12MonthTo3Years(rs.getBigDecimal("R101_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR101_moreThan3YearsTo5Years(rs.getBigDecimal("R101_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR101_moreThan5YearsTo10Years(rs.getBigDecimal("R101_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR101_moreThan10Years(rs.getBigDecimal("R101_MORE_THAN_10_YEARS"));
		obj.setR101_nonRatioSensativeItems(rs.getBigDecimal("R101_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR101_total(rs.getBigDecimal("R101_TOTAL"));
		obj.setR102product(rs.getString("R102_PRODUCT"));
		obj.setR102_upTo1Month(rs.getBigDecimal("R102_UP_TO_1_MONTH"));
		obj.setR102_moreThan1MonthTo3Months(rs.getBigDecimal("R102_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR102_moreThan3MonthTo6Months(rs.getBigDecimal("R102_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR102_moreThan6MonthTo12Months(rs.getBigDecimal("R102_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR102_moreThan12MonthTo3Years(rs.getBigDecimal("R102_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR102_moreThan3YearsTo5Years(rs.getBigDecimal("R102_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR102_moreThan5YearsTo10Years(rs.getBigDecimal("R102_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR102_moreThan10Years(rs.getBigDecimal("R102_MORE_THAN_10_YEARS"));
		obj.setR102_nonRatioSensativeItems(rs.getBigDecimal("R102_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR102_total(rs.getBigDecimal("R102_TOTAL"));
		obj.setR103product(rs.getString("R103_PRODUCT"));
		obj.setR103_upTo1Month(rs.getBigDecimal("R103_UP_TO_1_MONTH"));
		obj.setR103_moreThan1MonthTo3Months(rs.getBigDecimal("R103_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR103_moreThan3MonthTo6Months(rs.getBigDecimal("R103_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR103_moreThan6MonthTo12Months(rs.getBigDecimal("R103_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR103_moreThan12MonthTo3Years(rs.getBigDecimal("R103_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR103_moreThan3YearsTo5Years(rs.getBigDecimal("R103_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR103_moreThan5YearsTo10Years(rs.getBigDecimal("R103_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR103_moreThan10Years(rs.getBigDecimal("R103_MORE_THAN_10_YEARS"));
		obj.setR103_nonRatioSensativeItems(rs.getBigDecimal("R103_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR103_total(rs.getBigDecimal("R103_TOTAL"));
		obj.setR104product(rs.getString("R104_PRODUCT"));
		obj.setR104_upTo1Month(rs.getBigDecimal("R104_UP_TO_1_MONTH"));
		obj.setR104_moreThan1MonthTo3Months(rs.getBigDecimal("R104_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR104_moreThan3MonthTo6Months(rs.getBigDecimal("R104_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR104_moreThan6MonthTo12Months(rs.getBigDecimal("R104_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR104_moreThan12MonthTo3Years(rs.getBigDecimal("R104_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR104_moreThan3YearsTo5Years(rs.getBigDecimal("R104_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR104_moreThan5YearsTo10Years(rs.getBigDecimal("R104_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR104_moreThan10Years(rs.getBigDecimal("R104_MORE_THAN_10_YEARS"));
		obj.setR104_nonRatioSensativeItems(rs.getBigDecimal("R104_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR104_total(rs.getBigDecimal("R104_TOTAL"));
		obj.setR105product(rs.getString("R105_PRODUCT"));
		obj.setR105_upTo1Month(rs.getBigDecimal("R105_UP_TO_1_MONTH"));
		obj.setR105_moreThan1MonthTo3Months(rs.getBigDecimal("R105_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR105_moreThan3MonthTo6Months(rs.getBigDecimal("R105_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR105_moreThan6MonthTo12Months(rs.getBigDecimal("R105_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR105_moreThan12MonthTo3Years(rs.getBigDecimal("R105_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR105_moreThan3YearsTo5Years(rs.getBigDecimal("R105_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR105_moreThan5YearsTo10Years(rs.getBigDecimal("R105_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR105_moreThan10Years(rs.getBigDecimal("R105_MORE_THAN_10_YEARS"));
		obj.setR105_nonRatioSensativeItems(rs.getBigDecimal("R105_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR105_total(rs.getBigDecimal("R105_TOTAL"));
		obj.setR106product(rs.getString("R106_PRODUCT"));
		obj.setR106_upTo1Month(rs.getBigDecimal("R106_UP_TO_1_MONTH"));
		obj.setR106_moreThan1MonthTo3Months(rs.getBigDecimal("R106_MORE_THAN_1_MONTH_TO_3_MONTHS"));
		obj.setR106_moreThan3MonthTo6Months(rs.getBigDecimal("R106_MORE_THAN_3_MONTH_TO_6_MONTHS"));
		obj.setR106_moreThan6MonthTo12Months(rs.getBigDecimal("R106_MORE_THAN_6_MONTH_TO_12_MONTHS"));
		obj.setR106_moreThan12MonthTo3Years(rs.getBigDecimal("R106_MORE_THAN_12_MONTH_TO_3_YEARS"));
		obj.setR106_moreThan3YearsTo5Years(rs.getBigDecimal("R106_MORE_THAN_3_YEARS_TO_5_YEARS"));
		obj.setR106_moreThan5YearsTo10Years(rs.getBigDecimal("R106_MORE_THAN_5_YEARS_TO_10_YEARS"));
		obj.setR106_moreThan10Years(rs.getBigDecimal("R106_MORE_THAN_10_YEARS"));
		obj.setR106_nonRatioSensativeItems(rs.getBigDecimal("R106_NON_RATIO_SENSATIVE_ITEMS"));
		obj.setR106_total(rs.getBigDecimal("R106_TOTAL"));
		obj.setReportDate(rs.getDate("REPORT_DATE"));
		obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
		obj.setReportCode(rs.getString("REPORT_CODE"));
		obj.setReportDesc(rs.getString("REPORT_DESC"));
		obj.setEntityFlg(rs.getString("ENTITY_FLG"));
		obj.setModifyFlg(rs.getString("MODIFY_FLG"));
		obj.setDelFlg(rs.getString("DEL_FLG"));
		obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
		return obj;
	}
}
class M_IRBDetailRowMapper implements RowMapper<M_IRB_Detail_Entity> {
	@Override
	public M_IRB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
		M_IRB_Detail_Entity obj = new M_IRB_Detail_Entity();
		obj.setSno(rs.getString("SNO"));
		obj.setCust_id(rs.getString("CUST_ID"));
		obj.setAcct_number(rs.getString("ACCT_NUMBER"));
		obj.setAcct_name(rs.getString("ACCT_NAME"));
		obj.setData_type(rs.getString("DATA_TYPE"));
		obj.setReport_name(rs.getString("REPORT_NAME"));
		obj.setReport_label(rs.getString("REPORT_LABEL"));
		obj.setReport_addl_criteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
		obj.setReport_addl_criteria_2(rs.getString("REPORT_ADDL_CRITERIA_2"));
		obj.setReport_addl_criteria_3(rs.getString("REPORT_ADDL_CRITERIA_3"));
		obj.setReport_remarks(rs.getString("REPORT_REMARKS"));
		obj.setSanction_limit(rs.getBigDecimal("SANCTION_LIMIT"));
		obj.setModification_remarks(rs.getString("MODIFICATION_REMARKS"));
		obj.setData_entry_version(rs.getString("DATA_ENTRY_VERSION"));
		obj.setAcct_balance_in_pula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
		obj.setReport_date(rs.getDate("REPORT_DATE"));
		obj.setCreate_user(rs.getString("CREATE_USER"));
		obj.setCreate_time(rs.getDate("CREATE_TIME"));
		obj.setModify_user(rs.getString("MODIFY_USER"));
		obj.setModify_time(rs.getDate("MODIFY_TIME"));
		obj.setVerify_user(rs.getString("VERIFY_USER"));
		obj.setVerify_time(rs.getDate("VERIFY_TIME"));
		obj.setEntity_flg(rs.getString("ENTITY_FLG"));
		obj.setModify_flg(rs.getString("MODIFY_FLG"));
		obj.setDel_flg(rs.getString("DEL_FLG"));
		obj.setGl_code(rs.getString("GL_CODE"));
		obj.setGlsh_code(rs.getString("GLSH_CODE"));
		obj.setCurrency(rs.getString("CURRENCY"));
		return obj;
	}
}
class M_IRBArchivalDetailRowMapper implements RowMapper<M_IRB_Archival_Detail_Entity> {
	@Override
	public M_IRB_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
		M_IRB_Archival_Detail_Entity obj = new M_IRB_Archival_Detail_Entity();
		obj.setSno(rs.getString("SNO"));
		obj.setCust_id(rs.getString("CUST_ID"));
		obj.setAcct_number(rs.getString("ACCT_NUMBER"));
		obj.setAcct_name(rs.getString("ACCT_NAME"));
		obj.setData_type(rs.getString("DATA_TYPE"));
		obj.setReport_name(rs.getString("REPORT_NAME"));
		obj.setReport_label(rs.getString("REPORT_LABEL"));
		obj.setReport_addl_criteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
		obj.setReport_addl_criteria_2(rs.getString("REPORT_ADDL_CRITERIA_2"));
		obj.setReport_addl_criteria_3(rs.getString("REPORT_ADDL_CRITERIA_3"));
		obj.setReport_remarks(rs.getString("REPORT_REMARKS"));
		obj.setSanction_limit(rs.getBigDecimal("SANCTION_LIMIT"));
		obj.setModification_remarks(rs.getString("MODIFICATION_REMARKS"));
		obj.setData_entry_version(rs.getString("DATA_ENTRY_VERSION"));
		obj.setAcct_balance_in_pula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
		obj.setReport_date(rs.getDate("REPORT_DATE"));
		obj.setCreate_user(rs.getString("CREATE_USER"));
		obj.setCreate_time(rs.getDate("CREATE_TIME"));
		obj.setModify_user(rs.getString("MODIFY_USER"));
		obj.setModify_time(rs.getDate("MODIFY_TIME"));
		obj.setVerify_user(rs.getString("VERIFY_USER"));
		obj.setVerify_time(rs.getDate("VERIFY_TIME"));
		obj.setEntity_flg(rs.getString("ENTITY_FLG"));
		obj.setModify_flg(rs.getString("MODIFY_FLG"));
		obj.setDel_flg(rs.getString("DEL_FLG"));
		obj.setGl_code(rs.getString("GL_CODE"));
		obj.setGlsh_code(rs.getString("GLSH_CODE"));
		obj.setCurrency(rs.getString("CURRENCY"));
		return obj;
	}
}
}
