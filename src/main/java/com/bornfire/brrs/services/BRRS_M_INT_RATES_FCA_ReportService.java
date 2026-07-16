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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Service

public class BRRS_M_INT_RATES_FCA_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_INT_RATES_FCA_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// =========================================================
	// JDBC QUERY METHODS
	// =========================================================

	public List<M_INT_RATES_FCA_Summary_Entity> getSummaryByDate(Date reportDate) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_FCA_SUMMARYTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, new M_INT_RATES_FCASummaryRowMapper());
	}

	public List<M_INT_RATES_FCA_Detail_Entity> getDetailByDate(Date reportDate) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_FCA_DETAILTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, new M_INT_RATES_FCADetailRowMapper());
	}

	public List<M_INT_RATES_FCA_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_FCA_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_INT_RATES_FCAArchivalSummaryRowMapper());
	}

	public List<M_INT_RATES_FCA_Archival_Summary_Entity> getArchivalSummaryWithVersionAll() {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_FCA_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC",
			new M_INT_RATES_FCAArchivalSummaryRowMapper());
	}

	public List<M_INT_RATES_FCA_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_FCA_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_INT_RATES_FCAArchivalDetailRowMapper());
	}

	public List<M_INT_RATES_FCA_RESUB_Summary_Entity> getResubSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_FCA_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_INT_RATES_FCAResubSummaryRowMapper());
	}

	public List<M_INT_RATES_FCA_RESUB_Detail_Entity> getResubDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_FCA_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_INT_RATES_FCAResubDetailRowMapper());
	}

	public BigDecimal findMaxResubVersion(Date reportDate) {
		return jdbcTemplate.queryForObject(
			"SELECT MAX(REPORT_VERSION) FROM BRRS_M_INT_RATES_FCA_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, BigDecimal.class);
	}

	// =========================================================
	// JDBC WRITE METHODS
	// =========================================================

	private static final String R_COLS =
		"R10_CURRENCY,R10_CURRENT,R10_CALL,R10_SAVINGS,R10_NOTICE_0_31_DAYS,R10_NOTICE_32_88_DAYS,R10_91_DEPOSIT_DAY," +
		"R10_FD_1_6_MONTHS,R10_FD_7_12_MONTHS,R10_FD_13_18_MONTHS,R10_FD_19_24_MONTHS,R10_FD_OVER_24_MONTHS,R10_TOTAL," +
		"R11_CURRENCY,R11_CURRENT,R11_CALL,R11_SAVINGS,R11_NOTICE_0_31_DAYS,R11_NOTICE_32_88_DAYS,R11_91_DEPOSIT_DAY," +
		"R11_FD_1_6_MONTHS,R11_FD_7_12_MONTHS,R11_FD_13_18_MONTHS,R11_FD_19_24_MONTHS,R11_FD_OVER_24_MONTHS,R11_TOTAL," +
		"R12_CURRENCY,R12_CURRENT,R12_CALL,R12_SAVINGS,R12_NOTICE_0_31_DAYS,R12_NOTICE_32_88_DAYS,R12_91_DEPOSIT_DAY," +
		"R12_FD_1_6_MONTHS,R12_FD_7_12_MONTHS,R12_FD_13_18_MONTHS,R12_FD_19_24_MONTHS,R12_FD_OVER_24_MONTHS,R12_TOTAL," +
		"R13_CURRENCY,R13_CURRENT,R13_CALL,R13_SAVINGS,R13_NOTICE_0_31_DAYS,R13_NOTICE_32_88_DAYS,R13_91_DEPOSIT_DAY," +
		"R13_FD_1_6_MONTHS,R13_FD_7_12_MONTHS,R13_FD_13_18_MONTHS,R13_FD_19_24_MONTHS,R13_FD_OVER_24_MONTHS,R13_TOTAL," +
		"R14_CURRENCY,R14_CURRENT,R14_CALL,R14_SAVINGS,R14_NOTICE_0_31_DAYS,R14_NOTICE_32_88_DAYS,R14_91_DEPOSIT_DAY," +
		"R14_FD_1_6_MONTHS,R14_FD_7_12_MONTHS,R14_FD_13_18_MONTHS,R14_FD_19_24_MONTHS,R14_FD_OVER_24_MONTHS,R14_TOTAL," +
		"R15_CURRENCY,R15_CURRENT,R15_CALL,R15_SAVINGS,R15_NOTICE_0_31_DAYS,R15_NOTICE_32_88_DAYS,R15_91_DEPOSIT_DAY," +
		"R15_FD_1_6_MONTHS,R15_FD_7_12_MONTHS,R15_FD_13_18_MONTHS,R15_FD_19_24_MONTHS,R15_FD_OVER_24_MONTHS,R15_TOTAL";

	private static final String R_FIELDS_SET =
		"R10_CURRENCY=?,R10_CURRENT=?,R10_CALL=?,R10_SAVINGS=?,R10_NOTICE_0_31_DAYS=?,R10_NOTICE_32_88_DAYS=?,R10_91_DEPOSIT_DAY=?," +
		"R10_FD_1_6_MONTHS=?,R10_FD_7_12_MONTHS=?,R10_FD_13_18_MONTHS=?,R10_FD_19_24_MONTHS=?,R10_FD_OVER_24_MONTHS=?,R10_TOTAL=?," +
		"R11_CURRENCY=?,R11_CURRENT=?,R11_CALL=?,R11_SAVINGS=?,R11_NOTICE_0_31_DAYS=?,R11_NOTICE_32_88_DAYS=?,R11_91_DEPOSIT_DAY=?," +
		"R11_FD_1_6_MONTHS=?,R11_FD_7_12_MONTHS=?,R11_FD_13_18_MONTHS=?,R11_FD_19_24_MONTHS=?,R11_FD_OVER_24_MONTHS=?,R11_TOTAL=?," +
		"R12_CURRENCY=?,R12_CURRENT=?,R12_CALL=?,R12_SAVINGS=?,R12_NOTICE_0_31_DAYS=?,R12_NOTICE_32_88_DAYS=?,R12_91_DEPOSIT_DAY=?," +
		"R12_FD_1_6_MONTHS=?,R12_FD_7_12_MONTHS=?,R12_FD_13_18_MONTHS=?,R12_FD_19_24_MONTHS=?,R12_FD_OVER_24_MONTHS=?,R12_TOTAL=?," +
		"R13_CURRENCY=?,R13_CURRENT=?,R13_CALL=?,R13_SAVINGS=?,R13_NOTICE_0_31_DAYS=?,R13_NOTICE_32_88_DAYS=?,R13_91_DEPOSIT_DAY=?," +
		"R13_FD_1_6_MONTHS=?,R13_FD_7_12_MONTHS=?,R13_FD_13_18_MONTHS=?,R13_FD_19_24_MONTHS=?,R13_FD_OVER_24_MONTHS=?,R13_TOTAL=?," +
		"R14_CURRENCY=?,R14_CURRENT=?,R14_CALL=?,R14_SAVINGS=?,R14_NOTICE_0_31_DAYS=?,R14_NOTICE_32_88_DAYS=?,R14_91_DEPOSIT_DAY=?," +
		"R14_FD_1_6_MONTHS=?,R14_FD_7_12_MONTHS=?,R14_FD_13_18_MONTHS=?,R14_FD_19_24_MONTHS=?,R14_FD_OVER_24_MONTHS=?,R14_TOTAL=?," +
		"R15_CURRENCY=?,R15_CURRENT=?,R15_CALL=?,R15_SAVINGS=?,R15_NOTICE_0_31_DAYS=?,R15_NOTICE_32_88_DAYS=?,R15_91_DEPOSIT_DAY=?," +
		"R15_FD_1_6_MONTHS=?,R15_FD_7_12_MONTHS=?,R15_FD_13_18_MONTHS=?,R15_FD_19_24_MONTHS=?,R15_FD_OVER_24_MONTHS=?,R15_TOTAL=?";

	private static final String R_PLACEHOLDERS =
		"?,?,?,?,?,?,?,?,?,?,?,?,?," +
		"?,?,?,?,?,?,?,?,?,?,?,?,?," +
		"?,?,?,?,?,?,?,?,?,?,?,?,?," +
		"?,?,?,?,?,?,?,?,?,?,?,?,?," +
		"?,?,?,?,?,?,?,?,?,?,?,?,?," +
		"?,?,?,?,?,?,?,?,?,?,?,?,?";

	private Object[] rFieldValues(M_INT_RATES_FCA_Summary_Entity e) {
		return new Object[]{
			e.getR10_CURRENCY(), e.getR10_CURRENT(), e.getR10_CALL(), e.getR10_SAVINGS(),
			e.getR10_NOTICE_0_31_DAYS(), e.getR10_NOTICE_32_88_DAYS(), e.getR10_91_DEPOSIT_DAY(),
			e.getR10_FD_1_6_MONTHS(), e.getR10_FD_7_12_MONTHS(), e.getR10_FD_13_18_MONTHS(),
			e.getR10_FD_19_24_MONTHS(), e.getR10_FD_OVER_24_MONTHS(), e.getR10_TOTAL(),

			e.getR11_CURRENCY(), e.getR11_CURRENT(), e.getR11_CALL(), e.getR11_SAVINGS(),
			e.getR11_NOTICE_0_31_DAYS(), e.getR11_NOTICE_32_88_DAYS(), e.getR11_91_DEPOSIT_DAY(),
			e.getR11_FD_1_6_MONTHS(), e.getR11_FD_7_12_MONTHS(), e.getR11_FD_13_18_MONTHS(),
			e.getR11_FD_19_24_MONTHS(), e.getR11_FD_OVER_24_MONTHS(), e.getR11_TOTAL(),

			e.getR12_CURRENCY(), e.getR12_CURRENT(), e.getR12_CALL(), e.getR12_SAVINGS(),
			e.getR12_NOTICE_0_31_DAYS(), e.getR12_NOTICE_32_88_DAYS(), e.getR12_91_DEPOSIT_DAY(),
			e.getR12_FD_1_6_MONTHS(), e.getR12_FD_7_12_MONTHS(), e.getR12_FD_13_18_MONTHS(),
			e.getR12_FD_19_24_MONTHS(), e.getR12_FD_OVER_24_MONTHS(), e.getR12_TOTAL(),

			e.getR13_CURRENCY(), e.getR13_CURRENT(), e.getR13_CALL(), e.getR13_SAVINGS(),
			e.getR13_NOTICE_0_31_DAYS(), e.getR13_NOTICE_32_88_DAYS(), e.getR13_91_DEPOSIT_DAY(),
			e.getR13_FD_1_6_MONTHS(), e.getR13_FD_7_12_MONTHS(), e.getR13_FD_13_18_MONTHS(),
			e.getR13_FD_19_24_MONTHS(), e.getR13_FD_OVER_24_MONTHS(), e.getR13_TOTAL(),

			e.getR14_CURRENCY(), e.getR14_CURRENT(), e.getR14_CALL(), e.getR14_SAVINGS(),
			e.getR14_NOTICE_0_31_DAYS(), e.getR14_NOTICE_32_88_DAYS(), e.getR14_91_DEPOSIT_DAY(),
			e.getR14_FD_1_6_MONTHS(), e.getR14_FD_7_12_MONTHS(), e.getR14_FD_13_18_MONTHS(),
			e.getR14_FD_19_24_MONTHS(), e.getR14_FD_OVER_24_MONTHS(), e.getR14_TOTAL(),

			e.getR15_CURRENCY(), e.getR15_CURRENT(), e.getR15_CALL(), e.getR15_SAVINGS(),
			e.getR15_NOTICE_0_31_DAYS(), e.getR15_NOTICE_32_88_DAYS(), e.getR15_91_DEPOSIT_DAY(),
			e.getR15_FD_1_6_MONTHS(), e.getR15_FD_7_12_MONTHS(), e.getR15_FD_13_18_MONTHS(),
			e.getR15_FD_19_24_MONTHS(), e.getR15_FD_OVER_24_MONTHS(), e.getR15_TOTAL()
		};
	}

	private Object[] rFieldValues(M_INT_RATES_FCA_Detail_Entity e) {
		return new Object[]{
			e.getR10_CURRENCY(), e.getR10_CURRENT(), e.getR10_CALL(), e.getR10_SAVINGS(),
			e.getR10_NOTICE_0_31_DAYS(), e.getR10_NOTICE_32_88_DAYS(), e.getR10_91_DEPOSIT_DAY(),
			e.getR10_FD_1_6_MONTHS(), e.getR10_FD_7_12_MONTHS(), e.getR10_FD_13_18_MONTHS(),
			e.getR10_FD_19_24_MONTHS(), e.getR10_FD_OVER_24_MONTHS(), e.getR10_TOTAL(),

			e.getR11_CURRENCY(), e.getR11_CURRENT(), e.getR11_CALL(), e.getR11_SAVINGS(),
			e.getR11_NOTICE_0_31_DAYS(), e.getR11_NOTICE_32_88_DAYS(), e.getR11_91_DEPOSIT_DAY(),
			e.getR11_FD_1_6_MONTHS(), e.getR11_FD_7_12_MONTHS(), e.getR11_FD_13_18_MONTHS(),
			e.getR11_FD_19_24_MONTHS(), e.getR11_FD_OVER_24_MONTHS(), e.getR11_TOTAL(),

			e.getR12_CURRENCY(), e.getR12_CURRENT(), e.getR12_CALL(), e.getR12_SAVINGS(),
			e.getR12_NOTICE_0_31_DAYS(), e.getR12_NOTICE_32_88_DAYS(), e.getR12_91_DEPOSIT_DAY(),
			e.getR12_FD_1_6_MONTHS(), e.getR12_FD_7_12_MONTHS(), e.getR12_FD_13_18_MONTHS(),
			e.getR12_FD_19_24_MONTHS(), e.getR12_FD_OVER_24_MONTHS(), e.getR12_TOTAL(),

			e.getR13_CURRENCY(), e.getR13_CURRENT(), e.getR13_CALL(), e.getR13_SAVINGS(),
			e.getR13_NOTICE_0_31_DAYS(), e.getR13_NOTICE_32_88_DAYS(), e.getR13_91_DEPOSIT_DAY(),
			e.getR13_FD_1_6_MONTHS(), e.getR13_FD_7_12_MONTHS(), e.getR13_FD_13_18_MONTHS(),
			e.getR13_FD_19_24_MONTHS(), e.getR13_FD_OVER_24_MONTHS(), e.getR13_TOTAL(),

			e.getR14_CURRENCY(), e.getR14_CURRENT(), e.getR14_CALL(), e.getR14_SAVINGS(),
			e.getR14_NOTICE_0_31_DAYS(), e.getR14_NOTICE_32_88_DAYS(), e.getR14_91_DEPOSIT_DAY(),
			e.getR14_FD_1_6_MONTHS(), e.getR14_FD_7_12_MONTHS(), e.getR14_FD_13_18_MONTHS(),
			e.getR14_FD_19_24_MONTHS(), e.getR14_FD_OVER_24_MONTHS(), e.getR14_TOTAL(),

			e.getR15_CURRENCY(), e.getR15_CURRENT(), e.getR15_CALL(), e.getR15_SAVINGS(),
			e.getR15_NOTICE_0_31_DAYS(), e.getR15_NOTICE_32_88_DAYS(), e.getR15_91_DEPOSIT_DAY(),
			e.getR15_FD_1_6_MONTHS(), e.getR15_FD_7_12_MONTHS(), e.getR15_FD_13_18_MONTHS(),
			e.getR15_FD_19_24_MONTHS(), e.getR15_FD_OVER_24_MONTHS(), e.getR15_TOTAL()
		};
	}

	private Object[] rFieldValues(M_INT_RATES_FCA_Archival_Summary_Entity e) {
		return new Object[]{
			e.getR10_CURRENCY(), e.getR10_CURRENT(), e.getR10_CALL(), e.getR10_SAVINGS(),
			e.getR10_NOTICE_0_31_DAYS(), e.getR10_NOTICE_32_88_DAYS(), e.getR10_91_DEPOSIT_DAY(),
			e.getR10_FD_1_6_MONTHS(), e.getR10_FD_7_12_MONTHS(), e.getR10_FD_13_18_MONTHS(),
			e.getR10_FD_19_24_MONTHS(), e.getR10_FD_OVER_24_MONTHS(), e.getR10_TOTAL(),

			e.getR11_CURRENCY(), e.getR11_CURRENT(), e.getR11_CALL(), e.getR11_SAVINGS(),
			e.getR11_NOTICE_0_31_DAYS(), e.getR11_NOTICE_32_88_DAYS(), e.getR11_91_DEPOSIT_DAY(),
			e.getR11_FD_1_6_MONTHS(), e.getR11_FD_7_12_MONTHS(), e.getR11_FD_13_18_MONTHS(),
			e.getR11_FD_19_24_MONTHS(), e.getR11_FD_OVER_24_MONTHS(), e.getR11_TOTAL(),

			e.getR12_CURRENCY(), e.getR12_CURRENT(), e.getR12_CALL(), e.getR12_SAVINGS(),
			e.getR12_NOTICE_0_31_DAYS(), e.getR12_NOTICE_32_88_DAYS(), e.getR12_91_DEPOSIT_DAY(),
			e.getR12_FD_1_6_MONTHS(), e.getR12_FD_7_12_MONTHS(), e.getR12_FD_13_18_MONTHS(),
			e.getR12_FD_19_24_MONTHS(), e.getR12_FD_OVER_24_MONTHS(), e.getR12_TOTAL(),

			e.getR13_CURRENCY(), e.getR13_CURRENT(), e.getR13_CALL(), e.getR13_SAVINGS(),
			e.getR13_NOTICE_0_31_DAYS(), e.getR13_NOTICE_32_88_DAYS(), e.getR13_91_DEPOSIT_DAY(),
			e.getR13_FD_1_6_MONTHS(), e.getR13_FD_7_12_MONTHS(), e.getR13_FD_13_18_MONTHS(),
			e.getR13_FD_19_24_MONTHS(), e.getR13_FD_OVER_24_MONTHS(), e.getR13_TOTAL(),

			e.getR14_CURRENCY(), e.getR14_CURRENT(), e.getR14_CALL(), e.getR14_SAVINGS(),
			e.getR14_NOTICE_0_31_DAYS(), e.getR14_NOTICE_32_88_DAYS(), e.getR14_91_DEPOSIT_DAY(),
			e.getR14_FD_1_6_MONTHS(), e.getR14_FD_7_12_MONTHS(), e.getR14_FD_13_18_MONTHS(),
			e.getR14_FD_19_24_MONTHS(), e.getR14_FD_OVER_24_MONTHS(), e.getR14_TOTAL(),

			e.getR15_CURRENCY(), e.getR15_CURRENT(), e.getR15_CALL(), e.getR15_SAVINGS(),
			e.getR15_NOTICE_0_31_DAYS(), e.getR15_NOTICE_32_88_DAYS(), e.getR15_91_DEPOSIT_DAY(),
			e.getR15_FD_1_6_MONTHS(), e.getR15_FD_7_12_MONTHS(), e.getR15_FD_13_18_MONTHS(),
			e.getR15_FD_19_24_MONTHS(), e.getR15_FD_OVER_24_MONTHS(), e.getR15_TOTAL()
		};
	}

	private Object[] rFieldValues(M_INT_RATES_FCA_Archival_Detail_Entity e) {
		return new Object[]{
			e.getR10_CURRENCY(), e.getR10_CURRENT(), e.getR10_CALL(), e.getR10_SAVINGS(),
			e.getR10_NOTICE_0_31_DAYS(), e.getR10_NOTICE_32_88_DAYS(), e.getR10_91_DEPOSIT_DAY(),
			e.getR10_FD_1_6_MONTHS(), e.getR10_FD_7_12_MONTHS(), e.getR10_FD_13_18_MONTHS(),
			e.getR10_FD_19_24_MONTHS(), e.getR10_FD_OVER_24_MONTHS(), e.getR10_TOTAL(),

			e.getR11_CURRENCY(), e.getR11_CURRENT(), e.getR11_CALL(), e.getR11_SAVINGS(),
			e.getR11_NOTICE_0_31_DAYS(), e.getR11_NOTICE_32_88_DAYS(), e.getR11_91_DEPOSIT_DAY(),
			e.getR11_FD_1_6_MONTHS(), e.getR11_FD_7_12_MONTHS(), e.getR11_FD_13_18_MONTHS(),
			e.getR11_FD_19_24_MONTHS(), e.getR11_FD_OVER_24_MONTHS(), e.getR11_TOTAL(),

			e.getR12_CURRENCY(), e.getR12_CURRENT(), e.getR12_CALL(), e.getR12_SAVINGS(),
			e.getR12_NOTICE_0_31_DAYS(), e.getR12_NOTICE_32_88_DAYS(), e.getR12_91_DEPOSIT_DAY(),
			e.getR12_FD_1_6_MONTHS(), e.getR12_FD_7_12_MONTHS(), e.getR12_FD_13_18_MONTHS(),
			e.getR12_FD_19_24_MONTHS(), e.getR12_FD_OVER_24_MONTHS(), e.getR12_TOTAL(),

			e.getR13_CURRENCY(), e.getR13_CURRENT(), e.getR13_CALL(), e.getR13_SAVINGS(),
			e.getR13_NOTICE_0_31_DAYS(), e.getR13_NOTICE_32_88_DAYS(), e.getR13_91_DEPOSIT_DAY(),
			e.getR13_FD_1_6_MONTHS(), e.getR13_FD_7_12_MONTHS(), e.getR13_FD_13_18_MONTHS(),
			e.getR13_FD_19_24_MONTHS(), e.getR13_FD_OVER_24_MONTHS(), e.getR13_TOTAL(),

			e.getR14_CURRENCY(), e.getR14_CURRENT(), e.getR14_CALL(), e.getR14_SAVINGS(),
			e.getR14_NOTICE_0_31_DAYS(), e.getR14_NOTICE_32_88_DAYS(), e.getR14_91_DEPOSIT_DAY(),
			e.getR14_FD_1_6_MONTHS(), e.getR14_FD_7_12_MONTHS(), e.getR14_FD_13_18_MONTHS(),
			e.getR14_FD_19_24_MONTHS(), e.getR14_FD_OVER_24_MONTHS(), e.getR14_TOTAL(),

			e.getR15_CURRENCY(), e.getR15_CURRENT(), e.getR15_CALL(), e.getR15_SAVINGS(),
			e.getR15_NOTICE_0_31_DAYS(), e.getR15_NOTICE_32_88_DAYS(), e.getR15_91_DEPOSIT_DAY(),
			e.getR15_FD_1_6_MONTHS(), e.getR15_FD_7_12_MONTHS(), e.getR15_FD_13_18_MONTHS(),
			e.getR15_FD_19_24_MONTHS(), e.getR15_FD_OVER_24_MONTHS(), e.getR15_TOTAL()
		};
	}

	private Object[] rFieldValues(M_INT_RATES_FCA_RESUB_Summary_Entity e) {
		return new Object[]{
			e.getR10_CURRENCY(), e.getR10_CURRENT(), e.getR10_CALL(), e.getR10_SAVINGS(),
			e.getR10_NOTICE_0_31_DAYS(), e.getR10_NOTICE_32_88_DAYS(), e.getR10_91_DEPOSIT_DAY(),
			e.getR10_FD_1_6_MONTHS(), e.getR10_FD_7_12_MONTHS(), e.getR10_FD_13_18_MONTHS(),
			e.getR10_FD_19_24_MONTHS(), e.getR10_FD_OVER_24_MONTHS(), e.getR10_TOTAL(),

			e.getR11_CURRENCY(), e.getR11_CURRENT(), e.getR11_CALL(), e.getR11_SAVINGS(),
			e.getR11_NOTICE_0_31_DAYS(), e.getR11_NOTICE_32_88_DAYS(), e.getR11_91_DEPOSIT_DAY(),
			e.getR11_FD_1_6_MONTHS(), e.getR11_FD_7_12_MONTHS(), e.getR11_FD_13_18_MONTHS(),
			e.getR11_FD_19_24_MONTHS(), e.getR11_FD_OVER_24_MONTHS(), e.getR11_TOTAL(),

			e.getR12_CURRENCY(), e.getR12_CURRENT(), e.getR12_CALL(), e.getR12_SAVINGS(),
			e.getR12_NOTICE_0_31_DAYS(), e.getR12_NOTICE_32_88_DAYS(), e.getR12_91_DEPOSIT_DAY(),
			e.getR12_FD_1_6_MONTHS(), e.getR12_FD_7_12_MONTHS(), e.getR12_FD_13_18_MONTHS(),
			e.getR12_FD_19_24_MONTHS(), e.getR12_FD_OVER_24_MONTHS(), e.getR12_TOTAL(),

			e.getR13_CURRENCY(), e.getR13_CURRENT(), e.getR13_CALL(), e.getR13_SAVINGS(),
			e.getR13_NOTICE_0_31_DAYS(), e.getR13_NOTICE_32_88_DAYS(), e.getR13_91_DEPOSIT_DAY(),
			e.getR13_FD_1_6_MONTHS(), e.getR13_FD_7_12_MONTHS(), e.getR13_FD_13_18_MONTHS(),
			e.getR13_FD_19_24_MONTHS(), e.getR13_FD_OVER_24_MONTHS(), e.getR13_TOTAL(),

			e.getR14_CURRENCY(), e.getR14_CURRENT(), e.getR14_CALL(), e.getR14_SAVINGS(),
			e.getR14_NOTICE_0_31_DAYS(), e.getR14_NOTICE_32_88_DAYS(), e.getR14_91_DEPOSIT_DAY(),
			e.getR14_FD_1_6_MONTHS(), e.getR14_FD_7_12_MONTHS(), e.getR14_FD_13_18_MONTHS(),
			e.getR14_FD_19_24_MONTHS(), e.getR14_FD_OVER_24_MONTHS(), e.getR14_TOTAL(),

			e.getR15_CURRENCY(), e.getR15_CURRENT(), e.getR15_CALL(), e.getR15_SAVINGS(),
			e.getR15_NOTICE_0_31_DAYS(), e.getR15_NOTICE_32_88_DAYS(), e.getR15_91_DEPOSIT_DAY(),
			e.getR15_FD_1_6_MONTHS(), e.getR15_FD_7_12_MONTHS(), e.getR15_FD_13_18_MONTHS(),
			e.getR15_FD_19_24_MONTHS(), e.getR15_FD_OVER_24_MONTHS(), e.getR15_TOTAL()
		};
	}

	private Object[] rFieldValues(M_INT_RATES_FCA_RESUB_Detail_Entity e) {
		return new Object[]{
			e.getR10_CURRENCY(), e.getR10_CURRENT(), e.getR10_CALL(), e.getR10_SAVINGS(),
			e.getR10_NOTICE_0_31_DAYS(), e.getR10_NOTICE_32_88_DAYS(), e.getR10_91_DEPOSIT_DAY(),
			e.getR10_FD_1_6_MONTHS(), e.getR10_FD_7_12_MONTHS(), e.getR10_FD_13_18_MONTHS(),
			e.getR10_FD_19_24_MONTHS(), e.getR10_FD_OVER_24_MONTHS(), e.getR10_TOTAL(),

			e.getR11_CURRENCY(), e.getR11_CURRENT(), e.getR11_CALL(), e.getR11_SAVINGS(),
			e.getR11_NOTICE_0_31_DAYS(), e.getR11_NOTICE_32_88_DAYS(), e.getR11_91_DEPOSIT_DAY(),
			e.getR11_FD_1_6_MONTHS(), e.getR11_FD_7_12_MONTHS(), e.getR11_FD_13_18_MONTHS(),
			e.getR11_FD_19_24_MONTHS(), e.getR11_FD_OVER_24_MONTHS(), e.getR11_TOTAL(),

			e.getR12_CURRENCY(), e.getR12_CURRENT(), e.getR12_CALL(), e.getR12_SAVINGS(),
			e.getR12_NOTICE_0_31_DAYS(), e.getR12_NOTICE_32_88_DAYS(), e.getR12_91_DEPOSIT_DAY(),
			e.getR12_FD_1_6_MONTHS(), e.getR12_FD_7_12_MONTHS(), e.getR12_FD_13_18_MONTHS(),
			e.getR12_FD_19_24_MONTHS(), e.getR12_FD_OVER_24_MONTHS(), e.getR12_TOTAL(),

			e.getR13_CURRENCY(), e.getR13_CURRENT(), e.getR13_CALL(), e.getR13_SAVINGS(),
			e.getR13_NOTICE_0_31_DAYS(), e.getR13_NOTICE_32_88_DAYS(), e.getR13_91_DEPOSIT_DAY(),
			e.getR13_FD_1_6_MONTHS(), e.getR13_FD_7_12_MONTHS(), e.getR13_FD_13_18_MONTHS(),
			e.getR13_FD_19_24_MONTHS(), e.getR13_FD_OVER_24_MONTHS(), e.getR13_TOTAL(),

			e.getR14_CURRENCY(), e.getR14_CURRENT(), e.getR14_CALL(), e.getR14_SAVINGS(),
			e.getR14_NOTICE_0_31_DAYS(), e.getR14_NOTICE_32_88_DAYS(), e.getR14_91_DEPOSIT_DAY(),
			e.getR14_FD_1_6_MONTHS(), e.getR14_FD_7_12_MONTHS(), e.getR14_FD_13_18_MONTHS(),
			e.getR14_FD_19_24_MONTHS(), e.getR14_FD_OVER_24_MONTHS(), e.getR14_TOTAL(),

			e.getR15_CURRENCY(), e.getR15_CURRENT(), e.getR15_CALL(), e.getR15_SAVINGS(),
			e.getR15_NOTICE_0_31_DAYS(), e.getR15_NOTICE_32_88_DAYS(), e.getR15_91_DEPOSIT_DAY(),
			e.getR15_FD_1_6_MONTHS(), e.getR15_FD_7_12_MONTHS(), e.getR15_FD_13_18_MONTHS(),
			e.getR15_FD_19_24_MONTHS(), e.getR15_FD_OVER_24_MONTHS(), e.getR15_TOTAL()
		};
	}

	private void saveSummary(M_INT_RATES_FCA_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 8];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReportVersion();
		params[rVals.length + 1] = e.getReport_frequency();
		params[rVals.length + 2] = e.getReport_code();
		params[rVals.length + 3] = e.getReport_desc();
		params[rVals.length + 4] = e.getEntity_flg();
		params[rVals.length + 5] = e.getModify_flg();
		params[rVals.length + 6] = e.getDel_flg();
		params[rVals.length + 7] = e.getReportDate();
		jdbcTemplate.update(
			"UPDATE BRRS_M_INT_RATES_FCA_SUMMARYTABLE SET " + R_FIELDS_SET +
			",REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?,REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=?" +
			" WHERE REPORT_DATE=?", params);
	}

	private void saveDetail(M_INT_RATES_FCA_Detail_Entity e) {
		int cnt = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM BRRS_M_INT_RATES_FCA_DETAILTABLE WHERE REPORT_DATE=?",
			new Object[]{e.getReport_date()}, Integer.class);
		Object[] rVals = rFieldValues(e);
		if (cnt > 0) {
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
				"UPDATE BRRS_M_INT_RATES_FCA_DETAILTABLE SET " + R_FIELDS_SET +
				",REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?,REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=?" +
				" WHERE REPORT_DATE=?", params);
		} else {
			Object[] params = new Object[rVals.length + 8];
			System.arraycopy(rVals, 0, params, 0, rVals.length);
			params[rVals.length]     = e.getReport_date();
			params[rVals.length + 1] = e.getReport_version();
			params[rVals.length + 2] = e.getReport_frequency();
			params[rVals.length + 3] = e.getReport_code();
			params[rVals.length + 4] = e.getReport_desc();
			params[rVals.length + 5] = e.getEntity_flg();
			params[rVals.length + 6] = e.getModify_flg();
			params[rVals.length + 7] = e.getDel_flg();
			jdbcTemplate.update(
				"INSERT INTO BRRS_M_INT_RATES_FCA_DETAILTABLE (REPORT_DATE," + R_COLS +
				",REPORT_VERSION,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (?," +
				R_PLACEHOLDERS + ",?,?,?,?,?,?,?)", params);
		}
	}

	private void insertResubSummary(M_INT_RATES_FCA_RESUB_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 9];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReport_date();
		params[rVals.length + 1] = e.getReport_version();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		params[rVals.length + 6] = e.getEntity_flg();
		params[rVals.length + 7] = e.getModify_flg();
		params[rVals.length + 8] = e.getDel_flg();
		jdbcTemplate.update(
			"INSERT INTO BRRS_M_INT_RATES_FCA_RESUB_SUMMARYTABLE (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	private void insertResubDetail(M_INT_RATES_FCA_RESUB_Detail_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 9];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReport_date();
		params[rVals.length + 1] = e.getReport_version();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		params[rVals.length + 6] = e.getEntity_flg();
		params[rVals.length + 7] = e.getModify_flg();
		params[rVals.length + 8] = e.getDel_flg();
		jdbcTemplate.update(
			"INSERT INTO BRRS_M_INT_RATES_FCA_RESUB_DETAILTABLE (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	private void insertArchivalSummary(M_INT_RATES_FCA_Archival_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 9];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReport_date();
		params[rVals.length + 1] = e.getReport_version();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		params[rVals.length + 6] = e.getEntity_flg();
		params[rVals.length + 7] = e.getModify_flg();
		params[rVals.length + 8] = e.getDel_flg();
		jdbcTemplate.update(
			"INSERT INTO BRRS_M_INT_RATES_FCA_ARCHIVALTABLE_SUMMARY (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	private void insertArchivalDetail(M_INT_RATES_FCA_Archival_Detail_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 9];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReport_date();
		params[rVals.length + 1] = e.getReport_version();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		params[rVals.length + 6] = e.getEntity_flg();
		params[rVals.length + 7] = e.getModify_flg();
		params[rVals.length + 8] = e.getDel_flg();
		jdbcTemplate.update(
			"INSERT INTO BRRS_M_INT_RATES_FCA_ARCHIVALTABLE_DETAIL (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	public ModelAndView getINT_RATES_FCAView(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version,HttpServletRequest req1,Model md) {

	    ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

	    int pageSize = pageable.getPageSize();
	    int currentPage = pageable.getPageNumber();
	    int startItem = currentPage * pageSize;

	    try {

	        // Parse only once
	        Date d1 = dateformat.parse(todate);

	        System.out.println("======= VIEW DEBUG =======");
	        System.out.println("TYPE      : " + type);
	        System.out.println("DTLTYPE   : " + dtltype);
	        System.out.println("DATE      : " + d1);
	        System.out.println("VERSION   : " + version);
	        System.out.println("==========================");

	        // ===========================================================
	        //SUMMARY SECTION
	        // ===========================================================

	        // ---------- ARCHIVAL SUMMARY ----------
	        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

	            List<M_INT_RATES_FCA_Archival_Summary_Entity> T1Master =
	                    getArchivalSummaryByDateAndVersion(d1, version);

	            System.out.println("Archival Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- RESUB SUMMARY ----------
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_INT_RATES_FCA_RESUB_Summary_Entity> T1Master =
	                    getResubSummaryByDateAndVersion(d1, version);

	            System.out.println("Resub Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "resub");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- NORMAL SUMMARY ----------
	        else {

	            List<M_INT_RATES_FCA_Summary_Entity> T1Master =
	                    getSummaryByDate(d1);

	            System.out.println("Normal Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ===========================================================
	        // DETAIL SECTION
	        // ===========================================================

	        if ("detail".equalsIgnoreCase(dtltype)) {

	            // ---------- ARCHIVAL DETAIL ----------
	            if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

	                List<M_INT_RATES_FCA_Archival_Detail_Entity> T1Master =
	                        getArchivalDetailByDateAndVersion(d1, version);

	                System.out.println("Archival Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- RESUB DETAIL ---------------------------------------------------------------------------------------------------------------------------------
	            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	                List<M_INT_RATES_FCA_RESUB_Detail_Entity> T1Master =
	                        getResubDetailByDateAndVersion(d1, version);

	                System.out.println("Resub Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "resub");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- NORMAL DETAIL ----------
	            else {

	            	 List<M_INT_RATES_FCA_Detail_Entity> T1Master =
		                        getDetailByDate(d1);

	                System.out.println("Normal Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_INT_RATES_FCA");

	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}

	@Transactional
	public void updateReport(M_INT_RATES_FCA_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	    List<M_INT_RATES_FCA_Summary_Entity> summaryList =
	            getSummaryByDate(updatedEntity.getReportDate());
	    if (summaryList.isEmpty()) {
	        throw new RuntimeException(
	                "Summary record not found for REPORT_DATE: "
	                        + updatedEntity.getReportDate());
	    }
	    M_INT_RATES_FCA_Summary_Entity existingSummary = summaryList.get(0);

	    // =========================================
	    // AUDIT OLD COPY
	    // =========================================

	    M_INT_RATES_FCA_Summary_Entity oldcopy =
	            new M_INT_RATES_FCA_Summary_Entity();

	    BeanUtils.copyProperties(existingSummary, oldcopy);

	    // 2️⃣ Fetch or create DETAIL
	    List<M_INT_RATES_FCA_Detail_Entity> detailList =
	            getDetailByDate(updatedEntity.getReportDate());
	    M_INT_RATES_FCA_Detail_Entity existingDetail;
	    if (detailList.isEmpty()) {
	        existingDetail = new M_INT_RATES_FCA_Detail_Entity();
	        existingDetail.setReport_date(updatedEntity.getReportDate());
	    } else {
	        existingDetail = detailList.get(0);
	    }

	    try {

	        // 3️⃣ Loop through rows

	        for (int i = 10; i <= 15; i++) {

	            String prefix = "R" + i + "_";

	            String[] fields = {
	                    "CURRENT",
	                    "CALL",
	                    "SAVINGS",
	                    "NOTICE_0_31_DAYS",
	                    "NOTICE_32_88_DAYS",
	                    "91_DEPOSIT_DAY",
	                    "FD_1_6_MONTHS",
	                    "FD_7_12_MONTHS",
	                    "FD_13_18_MONTHS",
	                    "FD_19_24_MONTHS",
	                    "FD_OVER_24_MONTHS",
	                    "TOTAL"
	            };

	            for (String field : fields) {

	                String getterName =
	                        "get" + prefix + field;

	                String setterName =
	                        "set" + prefix + field;

	                try {

	                    Method getter =
	                            M_INT_RATES_FCA_Summary_Entity.class
	                                    .getMethod(getterName);

	                    Method summarySetter =
	                            M_INT_RATES_FCA_Summary_Entity.class
	                                    .getMethod(
	                                            setterName,
	                                            getter.getReturnType());

	                    Method detailSetter =
	                            M_INT_RATES_FCA_Detail_Entity.class
	                                    .getMethod(
	                                            setterName,
	                                            getter.getReturnType());

	                    Object newValue =
	                            getter.invoke(updatedEntity);

	                    // Update SUMMARY
	                    summarySetter.invoke(
	                            existingSummary,
	                            newValue);

	                    // Update DETAIL
	                    detailSetter.invoke(
	                            existingDetail,
	                            newValue);

	                } catch (NoSuchMethodException e) {

	                    // Skip missing fields safely
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {

	        throw new RuntimeException(
	                "Error while updating report fields",
	                e);
	    }

	    // =========================================
	    // CHECK CHANGES
	    // =========================================

	    String changes =
	            auditService.getChanges(
	                    oldcopy,
	                    existingSummary);

	    // =========================================
	    // SAVE BOTH TABLES
	    // =========================================

	    saveSummary(existingSummary);
	    saveDetail(existingDetail);

	    // =========================================
	    // AUDIT ONLY IF CHANGES FOUND
	    // =========================================

	    if (!changes.isEmpty()) {

	        auditService.compareEntitiesmanual(
	                oldcopy,
	                existingSummary,
	                updatedEntity.getReportDate().toString(),
	                "M INT RATES FCA Summary Screen",
	                "BRRS_M_INT_RATES_FCA_SUMMARY"
	        );
	    }

	    System.out.println(
	            "M_INT_RATES_FCA Summary & Detail Updated Successfully");
	}
	
	
	// RESUB VIEW

	
		public List<Object[]> getM_INT_RATES_FCAResub() {
		    List<Object[]> resubList = new ArrayList<>();
		    try {
		        List<M_INT_RATES_FCA_Archival_Summary_Entity> latestArchivalList =
		        		getArchivalSummaryWithVersionAll();

		        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
		            for (M_INT_RATES_FCA_Archival_Summary_Entity entity : latestArchivalList) {
		                resubList.add(new Object[] {
		                    entity.getReport_date(),
		                    entity.getReport_version(),
		                    entity.getReportResubDate()
		                });
		            }
		            System.out.println("Fetched " + resubList.size() + " record(s)");
		        } else {
		            System.out.println("No archival data found.");
		        }

		    } catch (Exception e) {
		        System.err.println("Error fetching M_INT_RATES_FCA Resub data: " + e.getMessage());
		        e.printStackTrace();
		    }
		    return resubList;
		}


		@Transactional
		public void updateResubReport(
		        M_INT_RATES_FCA_RESUB_Summary_Entity updatedEntity) {

		    Date reportDate = updatedEntity.getReport_date();

		    // ----------------------------------------------------
		    // GET CURRENT VERSION FROM RESUB TABLE
		    // ----------------------------------------------------

		    BigDecimal maxResubVer =
		            findMaxResubVersion(reportDate);

		    if (maxResubVer == null) {
		        throw new RuntimeException(
		                "No record for: " + reportDate);
		    }

		    BigDecimal newVersion =
		            maxResubVer.add(BigDecimal.ONE);

		    Date now = new Date();

		    // ====================================================
		    // RESUB SUMMARY
		    // ====================================================

		    M_INT_RATES_FCA_RESUB_Summary_Entity resubSummary =
		            new M_INT_RATES_FCA_RESUB_Summary_Entity();

		    BeanUtils.copyProperties(
		            updatedEntity,
		            resubSummary,
		            "reportDate",
		            "reportVersion",
		            "reportResubDate");

		    resubSummary.setReport_date(reportDate);
		    resubSummary.setReport_version(newVersion);
		    resubSummary.setReportResubDate(now);

		    // ====================================================
		    // RESUB DETAIL
		    // ====================================================

		    M_INT_RATES_FCA_RESUB_Detail_Entity resubDetail =
		            new M_INT_RATES_FCA_RESUB_Detail_Entity();

		    BeanUtils.copyProperties(
		            updatedEntity,
		            resubDetail,
		            "reportDate",
		            "reportVersion",
		            "reportResubDate");

		    resubDetail.setReport_date(reportDate);
		    resubDetail.setReport_version(newVersion);
		    resubDetail.setReportResubDate(now);

		    // ====================================================
		    // ARCHIVAL SUMMARY
		    // ====================================================

		    M_INT_RATES_FCA_Archival_Summary_Entity archSummary =
		            new M_INT_RATES_FCA_Archival_Summary_Entity();

		    BeanUtils.copyProperties(
		            updatedEntity,
		            archSummary,
		            "reportDate",
		            "reportVersion",
		            "reportResubDate");

		    archSummary.setReport_date(reportDate);
		    archSummary.setReport_version(newVersion);
		    archSummary.setReportResubDate(now);

		    // ====================================================
		    // ARCHIVAL DETAIL
		    // ====================================================

		    M_INT_RATES_FCA_Archival_Detail_Entity archDetail =
		            new M_INT_RATES_FCA_Archival_Detail_Entity();

		    BeanUtils.copyProperties(
		            updatedEntity,
		            archDetail,
		            "reportDate",
		            "reportVersion",
		            "reportResubDate");

		    archDetail.setReport_date(reportDate);
		    archDetail.setReport_version(newVersion);
		    archDetail.setReportResubDate(now);

		    // ====================================================
		    // SAVE ALL
		    // ====================================================

		    insertResubSummary(resubSummary);
		    insertResubDetail(resubDetail);

		    insertArchivalSummary(archSummary);
		    insertArchivalDetail(archDetail);

		    // ====================================================
		    // AUDIT
		    // ====================================================

		    ServletRequestAttributes attrs =
		            (ServletRequestAttributes)
		                    RequestContextHolder.getRequestAttributes();

		    if (attrs != null) {

		        HttpServletRequest request =
		                attrs.getRequest();

		        String userid =
		                (String) request.getSession()
		                        .getAttribute("USERID");

		        auditService.createBusinessAudit(
		                userid,
		                "RESUBMIT",
		                "M INT RATES FCA Resub Summary",
		                null,
		                "BRRS_M_INT_RATES_FCA_RESUB_SUMMARYTABLE"
		        );
		    }

		    System.out.println(
		            "M_INT_RATES_FCA Resub Version Created Successfully : "
		                    + newVersion);
		}	
		
		
		//Archival View
			public List<Object[]> getM_INT_RATES_FCAArchival() {
				List<Object[]> archivalList = new ArrayList<>();

				try {
					List<M_INT_RATES_FCA_Archival_Summary_Entity> repoData = getArchivalSummaryWithVersionAll();

					if (repoData != null && !repoData.isEmpty()) {
						for (M_INT_RATES_FCA_Archival_Summary_Entity entity : repoData) {
							Object[] row = new Object[] {
									entity.getReport_date(), 
									entity.getReport_version(), 
									 entity.getReportResubDate() 
							};
							archivalList.add(row);
						}

						System.out.println("Fetched " + archivalList.size() + " archival records");
						M_INT_RATES_FCA_Archival_Summary_Entity first = repoData.get(0);
						System.out.println("Latest archival version: " + first.getReport_version());
					} else {
						System.out.println("No archival data found.");
					}

				} catch (Exception e) {
					System.err.println("Error fetching  M_INT_RATES_FCA  Archival data: " + e.getMessage());
					e.printStackTrace();
				}

				return archivalList;
			}
		
	

	

			// Normal format Excel

			public byte[] getM_INTRATESFCAExcel(String filename, String reportId, String fromdate, String todate, String currency,
					String dtltype, String type, String format, BigDecimal version) throws Exception {
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
						return getExcelM_INTRATESFCAARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_INT_RATES_FCAResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else {

					if ("email".equalsIgnoreCase(format) && version == null) {
						logger.info("Got format as Email");
						logger.info("Service: Generating Email report for version {}", version);
						return BRRS_M_INT_RATES_FCAEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} else {

						// Fetch data

						List<M_INT_RATES_FCA_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

						if (dataList.isEmpty()) {
							logger.warn("Service: No data found for BRRS_M_INT_RATES_FCA report. Returning empty result.");
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

							int startRow = 5;

							if (!dataList.isEmpty()) {
								for (int i = 0; i < dataList.size(); i++) {
									M_INT_RATES_FCA_Summary_Entity record = dataList.get(i);
									System.out.println("rownumber=" + startRow + i);
									Row row = sheet.getRow(startRow + i);
									if (row == null) {
										row = sheet.createRow(startRow + i);
									}
									
									
									//row6
									// Column B
									Cell cellBdate = row.createCell(3);
									if (record.getReportDate() != null) {
										cellBdate.setCellValue(record.getReportDate());
										cellBdate.setCellStyle(dateStyle);
									} else {
										cellBdate.setCellValue("");
										cellBdate.setCellStyle(textStyle);
									}
									
									
									
									//ROW 10
									row = sheet.getRow(9);

			Cell cell1 = row.createCell(1);
							if (record.getR10_CURRENT() != null) {
								cell1.setCellValue(record.getR10_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							Cell cell2 = row.createCell(2);
							if (record.getR10_CALL() != null) {
								cell2.setCellValue(record.getR10_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(3);
							if (record.getR10_SAVINGS() != null) {
								cell3.setCellValue(record.getR10_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(4);
							if (record.getR10_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							Cell cell5 = row.createCell(5);
							if (record.getR10_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							Cell cell6 = row.createCell(6);
							if (record.getR10_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR10_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(7);
							if (record.getR10_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR10_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							Cell cell8 = row.createCell(8);
							if (record.getR10_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR10_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							Cell cell9 = row.createCell(9);
							if (record.getR10_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR10_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							Cell cell10 = row.createCell(10);
							if (record.getR10_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR10_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							Cell cell11 = row.createCell(11);
							if (record.getR10_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							Cell cell12 = row.createCell(12);
							if (record.getR10_TOTAL() != null) {
								cell12.setCellValue(record.getR10_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R11 ----------
							row = sheet.getRow(10);
							if (row == null) {
								row = sheet.createRow(10);
							}

							cell1 = row.createCell(1);
							if (record.getR11_CURRENT() != null) {
								cell1.setCellValue(record.getR11_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR11_CALL() != null) {
								cell2.setCellValue(record.getR11_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR11_SAVINGS() != null) {
								cell3.setCellValue(record.getR11_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR11_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR11_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR11_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR11_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR11_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR11_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR11_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR11_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR11_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR11_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR11_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR11_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR11_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR11_TOTAL() != null) {
								cell12.setCellValue(record.getR11_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}


							// ---------- R12 ----------
							row = sheet.getRow(11);
							if (row == null) {
								row = sheet.createRow(11);
							}

							cell1 = row.createCell(1);
							if (record.getR12_CURRENT() != null) {
								cell1.setCellValue(record.getR12_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR12_CALL() != null) {
								cell2.setCellValue(record.getR12_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR12_SAVINGS() != null) {
								cell3.setCellValue(record.getR12_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR12_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR12_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR12_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR12_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR12_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR12_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR12_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR12_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR12_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR12_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR12_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR12_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR12_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR12_TOTAL() != null) {
								cell12.setCellValue(record.getR12_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}


							// ---------- R13 ----------
							row = sheet.getRow(12);
							if (row == null) {
								row = sheet.createRow(12);
							}

							cell1 = row.createCell(1);
							if (record.getR13_CURRENT() != null) {
								cell1.setCellValue(record.getR13_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR13_CALL() != null) {
								cell2.setCellValue(record.getR13_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR13_SAVINGS() != null) {
								cell3.setCellValue(record.getR13_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR13_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR13_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR13_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR13_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR13_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR13_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR13_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR13_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR13_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR13_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR13_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR13_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR13_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR13_TOTAL() != null) {
								cell12.setCellValue(record.getR13_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}


							// ---------- R14 ----------
							row = sheet.getRow(13);
							if (row == null) {
								row = sheet.createRow(13);
							}

							cell1 = row.createCell(1);
							if (record.getR14_CURRENT() != null) {
								cell1.setCellValue(record.getR14_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR14_CALL() != null) {
								cell2.setCellValue(record.getR14_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR14_SAVINGS() != null) {
								cell3.setCellValue(record.getR14_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR14_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR14_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR14_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR14_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR14_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR14_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR14_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR14_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR14_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR14_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR14_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR14_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR14_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR14_TOTAL() != null) {
								cell12.setCellValue(record.getR14_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}
							
							// ---------- R15 ----------
							row = sheet.getRow(14);
							if (row == null) {
							    row = sheet.createRow(14);
							}

							cell1 = row.createCell(1);
							if (record.getR15_CURRENT() != null) {
							    cell1.setCellValue(record.getR15_CURRENT());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR15_CALL() != null) {
							    cell2.setCellValue(record.getR15_CALL());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR15_SAVINGS() != null) {
							    cell3.setCellValue(record.getR15_SAVINGS());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR15_NOTICE_0_31_DAYS() != null) {
							    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR15_NOTICE_32_88_DAYS() != null) {
							    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS());
							    cell5.setCellStyle(textStyle);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR15_91_DEPOSIT_DAY() != null) {
							    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY());
							    cell6.setCellStyle(textStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR15_FD_1_6_MONTHS() != null) {
							    cell7.setCellValue(record.getR15_FD_1_6_MONTHS());
							    cell7.setCellStyle(textStyle);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR15_FD_7_12_MONTHS() != null) {
							    cell8.setCellValue(record.getR15_FD_7_12_MONTHS());
							    cell8.setCellStyle(textStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR15_FD_13_18_MONTHS() != null) {
							    cell9.setCellValue(record.getR15_FD_13_18_MONTHS());
							    cell9.setCellStyle(textStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR15_FD_19_24_MONTHS() != null) {
							    cell10.setCellValue(record.getR15_FD_19_24_MONTHS());
							    cell10.setCellStyle(textStyle);
							} else {
							    cell10.setCellValue("");
							    cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR15_FD_OVER_24_MONTHS() != null) {
							    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS());
							    cell11.setCellStyle(textStyle);
							} else {
							    cell11.setCellValue("");
							    cell11.setCellStyle(textStyle);
							}

							cell12 = row.createCell(12);
							if (record.getR15_TOTAL() != null) {
							    cell12.setCellValue(record.getR15_TOTAL());
							    cell12.setCellStyle(textStyle);
							} else {
							    cell12.setCellValue("");
							    cell12.setCellStyle(textStyle);
							}


								}
								workbook.setForceFormulaRecalculation(true);
							} else {

							}

							// Write the final workbook content to the in-memory stream.
							workbook.write(out);

							logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
							
							// audit service summary format

							ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
														if (attrs != null) {
															HttpServletRequest request = attrs.getRequest();
															String userid = (String) request.getSession().getAttribute("USERID");
															auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES_FCA SUMMARY", null, "BRRS_M_INT_RATES_FCA_SUMMARYTABLE");
														}

							return out.toByteArray();
						}	
					}
				}
			}

			// Normal Email Excel
			public byte[] BRRS_M_INT_RATES_FCAEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Email Excel generation process in memory.");
				
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_M_INT_RATES_FCAARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_INT_RATES_FCAEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 
				else {
				List<M_INT_RATES_FCA_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_INT_RATES_FCA report. Returning empty result.");
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

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_INT_RATES_FCA_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
							
							//row6
							// Column B
							Cell cellBdate = row.createCell(3);
							if (record.getReportDate() != null) {
								cellBdate.setCellValue(record.getReportDate());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}
							
							
							
							//ROW 10
							row = sheet.getRow(9);

		Cell cell1 = row.createCell(1);
							if (record.getR10_CURRENT() != null) {
								cell1.setCellValue(record.getR10_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							Cell cell2 = row.createCell(2);
							if (record.getR10_CALL() != null) {
								cell2.setCellValue(record.getR10_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(3);
							if (record.getR10_SAVINGS() != null) {
								cell3.setCellValue(record.getR10_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(4);
							if (record.getR10_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							Cell cell5 = row.createCell(5);
							if (record.getR10_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							Cell cell6 = row.createCell(6);
							if (record.getR10_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR10_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(7);
							if (record.getR10_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR10_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							Cell cell8 = row.createCell(8);
							if (record.getR10_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR10_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							Cell cell9 = row.createCell(9);
							if (record.getR10_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR10_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							Cell cell10 = row.createCell(10);
							if (record.getR10_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR10_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							Cell cell11 = row.createCell(11);
							if (record.getR10_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							Cell cell12 = row.createCell(12);
							if (record.getR10_TOTAL() != null) {
								cell12.setCellValue(record.getR10_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R11 ----------
							row = sheet.getRow(10);
							if (row == null) {
								row = sheet.createRow(10);
							}

							cell1 = row.createCell(1);
							if (record.getR11_CURRENT() != null) {
								cell1.setCellValue(record.getR11_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR11_CALL() != null) {
								cell2.setCellValue(record.getR11_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR11_SAVINGS() != null) {
								cell3.setCellValue(record.getR11_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR11_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR11_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR11_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR11_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR11_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR11_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR11_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR11_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR11_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR11_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR11_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR11_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR11_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							cell12 = row.createCell(12);
							if (record.getR11_TOTAL() != null) {
								cell12.setCellValue(record.getR11_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R12 ----------
							row = sheet.getRow(11);
							if (row == null) {
								row = sheet.createRow(11);
							}

							cell1 = row.createCell(1);
							if (record.getR12_CURRENT() != null) {
								cell1.setCellValue(record.getR12_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR12_CALL() != null) {
								cell2.setCellValue(record.getR12_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR12_SAVINGS() != null) {
								cell3.setCellValue(record.getR12_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR12_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR12_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR12_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR12_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR12_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR12_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR12_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR12_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR12_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR12_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR12_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR12_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR12_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							cell12 = row.createCell(12);
							if (record.getR12_TOTAL() != null) {
								cell12.setCellValue(record.getR12_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R13 ----------
							row = sheet.getRow(12);
							if (row == null) {
								row = sheet.createRow(12);
							}

							cell1 = row.createCell(1);
							if (record.getR13_CURRENT() != null) {
								cell1.setCellValue(record.getR13_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR13_CALL() != null) {
								cell2.setCellValue(record.getR13_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR13_SAVINGS() != null) {
								cell3.setCellValue(record.getR13_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR13_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR13_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR13_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR13_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR13_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR13_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR13_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR13_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR13_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR13_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR13_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR13_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR13_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
						 cell12 = row.createCell(12);
							if (record.getR13_TOTAL() != null) {
								cell12.setCellValue(record.getR13_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R14 ----------
							row = sheet.getRow(13);
							if (row == null) {
								row = sheet.createRow(13);
							}

							cell1 = row.createCell(1);
							if (record.getR14_CURRENT() != null) {
								cell1.setCellValue(record.getR14_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR14_CALL() != null) {
								cell2.setCellValue(record.getR14_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR14_SAVINGS() != null) {
								cell3.setCellValue(record.getR14_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR14_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR14_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR14_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR14_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR14_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR14_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR14_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR14_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR14_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR14_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR14_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR14_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR14_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
						 cell12 = row.createCell(12);
							if (record.getR14_TOTAL() != null) {
								cell12.setCellValue(record.getR14_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}
							
							
							// ---------- R15 ----------
							row = sheet.getRow(14);
							if (row == null) {
							    row = sheet.createRow(14);
							}

							cell1 = row.createCell(1);
							if (record.getR15_CURRENT() != null) {
							    cell1.setCellValue(record.getR15_CURRENT());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR15_CALL() != null) {
							    cell2.setCellValue(record.getR15_CALL());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR15_SAVINGS() != null) {
							    cell3.setCellValue(record.getR15_SAVINGS());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR15_NOTICE_0_31_DAYS() != null) {
							    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR15_NOTICE_32_88_DAYS() != null) {
							    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS());
							    cell5.setCellStyle(textStyle);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR15_91_DEPOSIT_DAY() != null) {
							    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY());
							    cell6.setCellStyle(textStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR15_FD_1_6_MONTHS() != null) {
							    cell7.setCellValue(record.getR15_FD_1_6_MONTHS());
							    cell7.setCellStyle(textStyle);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR15_FD_7_12_MONTHS() != null) {
							    cell8.setCellValue(record.getR15_FD_7_12_MONTHS());
							    cell8.setCellStyle(textStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR15_FD_13_18_MONTHS() != null) {
							    cell9.setCellValue(record.getR15_FD_13_18_MONTHS());
							    cell9.setCellStyle(textStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR15_FD_19_24_MONTHS() != null) {
							    cell10.setCellValue(record.getR15_FD_19_24_MONTHS());
							    cell10.setCellStyle(textStyle);
							} else {
							    cell10.setCellValue("");
							    cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR15_FD_OVER_24_MONTHS() != null) {
							    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS());
							    cell11.setCellStyle(textStyle);
							} else {
							    cell11.setCellValue("");
							    cell11.setCellStyle(textStyle);
							}

							cell12 = row.createCell(12);
							if (record.getR15_TOTAL() != null) {
							    cell12.setCellValue(record.getR15_TOTAL());
							    cell12.setCellStyle(textStyle);
							} else {
							    cell12.setCellValue("");
							    cell12.setCellStyle(textStyle);
							}
							

						}
						workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					
					// audit service summary email

					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
										if (attrs != null) {
											HttpServletRequest request = attrs.getRequest();
											String userid = (String) request.getSession().getAttribute("USERID");
											auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES_FCA EMAIL SUMMARY", null, "BRRS_M_INT_RATES_FCA_SUMMARYTABLE");
										}


					return out.toByteArray();
				}
				}
			}
			
			
			
			// Archival format excel
			public byte[] getExcelM_INTRATESFCAARCHIVAL(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory in Archival.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_M_INT_RATES_FCAARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 

				List<M_INT_RATES_FCA_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_INT_RATES_FCA report. Returning empty result.");
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

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_INT_RATES_FCA_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
							//row6
							// Column B
							Cell cellBdate = row.createCell(3);
							if (record.getReport_date() != null) {
								cellBdate.setCellValue(record.getReport_date());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}
							
							
							
							//ROW 10
							row = sheet.getRow(9);

			Cell cell1 = row.createCell(1);
							if (record.getR10_CURRENT() != null) {
								cell1.setCellValue(record.getR10_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							Cell cell2 = row.createCell(2);
							if (record.getR10_CALL() != null) {
								cell2.setCellValue(record.getR10_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(3);
							if (record.getR10_SAVINGS() != null) {
								cell3.setCellValue(record.getR10_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(4);
							if (record.getR10_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							Cell cell5 = row.createCell(5);
							if (record.getR10_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							Cell cell6 = row.createCell(6);
							if (record.getR10_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR10_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(7);
							if (record.getR10_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR10_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							Cell cell8 = row.createCell(8);
							if (record.getR10_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR10_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							Cell cell9 = row.createCell(9);
							if (record.getR10_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR10_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							Cell cell10 = row.createCell(10);
							if (record.getR10_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR10_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							Cell cell11 = row.createCell(11);
							if (record.getR10_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							Cell cell12 = row.createCell(12);
							if (record.getR10_TOTAL() != null) {
								cell12.setCellValue(record.getR10_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R11 ----------
							row = sheet.getRow(10);
							if (row == null) {
								row = sheet.createRow(10);
							}

							cell1 = row.createCell(1);
							if (record.getR11_CURRENT() != null) {
								cell1.setCellValue(record.getR11_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR11_CALL() != null) {
								cell2.setCellValue(record.getR11_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR11_SAVINGS() != null) {
								cell3.setCellValue(record.getR11_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR11_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR11_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR11_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR11_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR11_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR11_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR11_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR11_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR11_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR11_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR11_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR11_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR11_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR11_TOTAL() != null) {
								cell12.setCellValue(record.getR11_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}


							// ---------- R12 ----------
							row = sheet.getRow(11);
							if (row == null) {
								row = sheet.createRow(11);
							}

							cell1 = row.createCell(1);
							if (record.getR12_CURRENT() != null) {
								cell1.setCellValue(record.getR12_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR12_CALL() != null) {
								cell2.setCellValue(record.getR12_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR12_SAVINGS() != null) {
								cell3.setCellValue(record.getR12_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR12_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR12_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR12_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR12_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR12_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR12_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR12_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR12_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR12_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR12_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR12_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR12_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR12_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR12_TOTAL() != null) {
								cell12.setCellValue(record.getR12_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}


							// ---------- R13 ----------
							row = sheet.getRow(12);
							if (row == null) {
								row = sheet.createRow(12);
							}

							cell1 = row.createCell(1);
							if (record.getR13_CURRENT() != null) {
								cell1.setCellValue(record.getR13_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR13_CALL() != null) {
								cell2.setCellValue(record.getR13_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR13_SAVINGS() != null) {
								cell3.setCellValue(record.getR13_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR13_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR13_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR13_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR13_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR13_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR13_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR13_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR13_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR13_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR13_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR13_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR13_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR13_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR13_TOTAL() != null) {
								cell12.setCellValue(record.getR13_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}


							// ---------- R14 ----------
							row = sheet.getRow(13);
							if (row == null) {
								row = sheet.createRow(13);
							}

							cell1 = row.createCell(1);
							if (record.getR14_CURRENT() != null) {
								cell1.setCellValue(record.getR14_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR14_CALL() != null) {
								cell2.setCellValue(record.getR14_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR14_SAVINGS() != null) {
								cell3.setCellValue(record.getR14_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR14_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR14_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR14_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR14_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR14_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR14_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR14_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR14_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR14_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR14_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR14_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR14_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR14_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR14_TOTAL() != null) {
								cell12.setCellValue(record.getR14_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}
							
							// ---------- R15 ----------
							row = sheet.getRow(14);
							if (row == null) {
							    row = sheet.createRow(14);
							}

							cell1 = row.createCell(1);
							if (record.getR15_CURRENT() != null) {
							    cell1.setCellValue(record.getR15_CURRENT());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR15_CALL() != null) {
							    cell2.setCellValue(record.getR15_CALL());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR15_SAVINGS() != null) {
							    cell3.setCellValue(record.getR15_SAVINGS());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR15_NOTICE_0_31_DAYS() != null) {
							    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR15_NOTICE_32_88_DAYS() != null) {
							    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS());
							    cell5.setCellStyle(textStyle);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR15_91_DEPOSIT_DAY() != null) {
							    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY());
							    cell6.setCellStyle(textStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR15_FD_1_6_MONTHS() != null) {
							    cell7.setCellValue(record.getR15_FD_1_6_MONTHS());
							    cell7.setCellStyle(textStyle);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR15_FD_7_12_MONTHS() != null) {
							    cell8.setCellValue(record.getR15_FD_7_12_MONTHS());
							    cell8.setCellStyle(textStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR15_FD_13_18_MONTHS() != null) {
							    cell9.setCellValue(record.getR15_FD_13_18_MONTHS());
							    cell9.setCellStyle(textStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR15_FD_19_24_MONTHS() != null) {
							    cell10.setCellValue(record.getR15_FD_19_24_MONTHS());
							    cell10.setCellStyle(textStyle);
							} else {
							    cell10.setCellValue("");
							    cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR15_FD_OVER_24_MONTHS() != null) {
							    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS());
							    cell11.setCellStyle(textStyle);
							} else {
							    cell11.setCellValue("");
							    cell11.setCellStyle(textStyle);
							}

							cell12 = row.createCell(12);
							if (record.getR15_TOTAL() != null) {
							    cell12.setCellValue(record.getR15_TOTAL());
							    cell12.setCellStyle(textStyle);
							} else {
							    cell12.setCellValue("");
							    cell12.setCellStyle(textStyle);
							}


						}
						workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					
					// audit service archival summary format

					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
										if (attrs != null) {
											HttpServletRequest request = attrs.getRequest();
											String userid = (String) request.getSession().getAttribute("USERID");
											auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES_FCA ARCHIVAL SUMMARY", null, "BRRS_M_INT_RATES_FCA_ARCHIVALTABLE_SUMMARY");
										}

					return out.toByteArray();
				}
			}



//		/// Downloaded for Archival & Resub
//			public byte[] BRRS_M_INT_RATES_FCAResubExcel(String filename, String reportId, String fromdate, String todate,
//					String currency, String dtltype, String type, BigDecimal version) throws Exception {
//
//				logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");
//
//				if (type.equals("RESUB") & version != null) {
//
//				}
//
//				List<M_INT_RATES_FCA_RESUB_Summary_Entity> dataList = M_INT_RATES_FCA_resub_summary_repo
//						.getdatabydateListarchival(dateformat.parse(todate), version);
//
//				if (dataList.isEmpty()) {
//					logger.warn("Service: No data found for M_INT_RATES_FCA report. Returning empty result.");
//					return new byte[0];
//				}
//
//				String templateDir = env.getProperty("output.exportpathtemp");
//				String templateFileName = filename;
//				System.out.println(filename);
//				Path templatePath = Paths.get(templateDir, templateFileName);
//				System.out.println(templatePath);
//
//				logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());
//
//				if (!Files.exists(templatePath)) {
//		//This specific exception will be caught by the controller.
//					throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
//				}
//				if (!Files.isReadable(templatePath)) {
//		//A specific exception for permission errors.
//					throw new SecurityException(
//							"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
//				}
//
//		//This try-with-resources block is perfect. It guarantees all resources are
//		//closed automatically.
//				try (InputStream templateInputStream = Files.newInputStream(templatePath);
//						Workbook workbook = WorkbookFactory.create(templateInputStream);
//						ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//
//					Sheet sheet = workbook.getSheetAt(0);
//
//		//--- Style Definitions ---
//					CreationHelper createHelper = workbook.getCreationHelper();
//
//					CellStyle dateStyle = workbook.createCellStyle();
//					dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
//					dateStyle.setBorderBottom(BorderStyle.THIN);
//					dateStyle.setBorderTop(BorderStyle.THIN);
//					dateStyle.setBorderLeft(BorderStyle.THIN);
//					dateStyle.setBorderRight(BorderStyle.THIN);
//
//					CellStyle textStyle = workbook.createCellStyle();
//					textStyle.setBorderBottom(BorderStyle.THIN);
//					textStyle.setBorderTop(BorderStyle.THIN);
//					textStyle.setBorderLeft(BorderStyle.THIN);
//					textStyle.setBorderRight(BorderStyle.THIN);
//
//		// Create the font
//					Font font = workbook.createFont();
//					font.setFontHeightInPoints((short) 8); // size 8
//					font.setFontName("Arial");
//					CellStyle numberStyle = workbook.createCellStyle();
//		// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
//					numberStyle.setBorderBottom(BorderStyle.THIN);
//					numberStyle.setBorderTop(BorderStyle.THIN);
//					numberStyle.setBorderLeft(BorderStyle.THIN);
//					numberStyle.setBorderRight(BorderStyle.THIN);
//					numberStyle.setFont(font);
//		// --- End of Style Definitions ---	
//
//					int startRow = 5;
//
//					if (!dataList.isEmpty()) {
//						for (int i = 0; i < dataList.size(); i++) {
//
//							M_INT_RATES_FCA_RESUB_Summary_Entity  record = dataList.get(i);
//							System.out.println("rownumber=" + startRow + i);
//							System.out.println("rownumber=" + startRow + i);
//							Row row = sheet.getRow(startRow + i);
//							if (row == null) {
//								row = sheet.createRow(startRow + i);
//							}
//							
//							//row6
//							// Column B
//							Cell cellBdate = row.createCell(3);
//							if (record.getReport_date() != null) {
//								cellBdate.setCellValue(record.getReport_date());
//								cellBdate.setCellStyle(dateStyle);
//							} else {
//								cellBdate.setCellValue("");
//								cellBdate.setCellStyle(textStyle);
//							}
//							
//							
//							
//							//ROW 10
//							row = sheet.getRow(9);
//
//									Cell cell1 = row.createCell(1);
//						if (record.getR10_CURRENT() != null) {
//							cell1.setCellValue(record.getR10_CURRENT());
//							cell1.setCellStyle(textStyle);
//						} else {
//							cell1.setCellValue("");
//							cell1.setCellStyle(textStyle);
//						}
//
//						Cell cell2 = row.createCell(2);
//						if (record.getR10_CALL() != null) {
//							cell2.setCellValue(record.getR10_CALL());
//							cell2.setCellStyle(textStyle);
//						} else {
//							cell2.setCellValue("");
//							cell2.setCellStyle(textStyle);
//						}
//
//						Cell cell3 = row.createCell(3);
//						if (record.getR10_SAVINGS() != null) {
//							cell3.setCellValue(record.getR10_SAVINGS());
//							cell3.setCellStyle(textStyle);
//						} else {
//							cell3.setCellValue("");
//							cell3.setCellStyle(textStyle);
//						}
//
//						Cell cell4 = row.createCell(4);
//						if (record.getR10_NOTICE_0_31_DAYS() != null) {
//							cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS());
//							cell4.setCellStyle(textStyle);
//						} else {
//							cell4.setCellValue("");
//							cell4.setCellStyle(textStyle);
//						}
//
//						Cell cell5 = row.createCell(5);
//						if (record.getR10_NOTICE_32_88_DAYS() != null) {
//							cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS());
//							cell5.setCellStyle(textStyle);
//						} else {
//							cell5.setCellValue("");
//							cell5.setCellStyle(textStyle);
//						}
//
//						Cell cell6 = row.createCell(6);
//						if (record.getR10_91_DEPOSIT_DAY() != null) {
//							cell6.setCellValue(record.getR10_91_DEPOSIT_DAY());
//							cell6.setCellStyle(textStyle);
//						} else {
//							cell6.setCellValue("");
//							cell6.setCellStyle(textStyle);
//						}
//
//						Cell cell7 = row.createCell(7);
//						if (record.getR10_FD_1_6_MONTHS() != null) {
//							cell7.setCellValue(record.getR10_FD_1_6_MONTHS());
//							cell7.setCellStyle(textStyle);
//						} else {
//							cell7.setCellValue("");
//							cell7.setCellStyle(textStyle);
//						}
//
//						Cell cell8 = row.createCell(8);
//						if (record.getR10_FD_7_12_MONTHS() != null) {
//							cell8.setCellValue(record.getR10_FD_7_12_MONTHS());
//							cell8.setCellStyle(textStyle);
//						} else {
//							cell8.setCellValue("");
//							cell8.setCellStyle(textStyle);
//						}
//
//						Cell cell9 = row.createCell(9);
//						if (record.getR10_FD_13_18_MONTHS() != null) {
//							cell9.setCellValue(record.getR10_FD_13_18_MONTHS());
//							cell9.setCellStyle(textStyle);
//						} else {
//							cell9.setCellValue("");
//							cell9.setCellStyle(textStyle);
//						}
//
//						Cell cell10 = row.createCell(10);
//						if (record.getR10_FD_19_24_MONTHS() != null) {
//							cell10.setCellValue(record.getR10_FD_19_24_MONTHS());
//							cell10.setCellStyle(textStyle);
//						} else {
//							cell10.setCellValue("");
//							cell10.setCellStyle(textStyle);
//						}
//
//						Cell cell11 = row.createCell(11);
//						if (record.getR10_FD_OVER_24_MONTHS() != null) {
//							cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS());
//							cell11.setCellStyle(textStyle);
//						} else {
//							cell11.setCellValue("");
//							cell11.setCellStyle(textStyle);
//						}
//						
//						Cell cell12 = row.createCell(12);
//						if (record.getR10_TOTAL() != null) {
//							cell12.setCellValue(record.getR10_TOTAL());
//							cell12.setCellStyle(textStyle);
//						} else {
//							cell12.setCellValue("");
//							cell12.setCellStyle(textStyle);
//						}
//
//						// ---------- R11 ----------
//						row = sheet.getRow(10);
//						if (row == null) {
//							row = sheet.createRow(10);
//						}
//
//						cell1 = row.createCell(1);
//						if (record.getR11_CURRENT() != null) {
//							cell1.setCellValue(record.getR11_CURRENT());
//							cell1.setCellStyle(textStyle);
//						} else {
//							cell1.setCellValue("");
//							cell1.setCellStyle(textStyle);
//						}
//
//						cell2 = row.createCell(2);
//						if (record.getR11_CALL() != null) {
//							cell2.setCellValue(record.getR11_CALL());
//							cell2.setCellStyle(textStyle);
//						} else {
//							cell2.setCellValue("");
//							cell2.setCellStyle(textStyle);
//						}
//
//						cell3 = row.createCell(3);
//						if (record.getR11_SAVINGS() != null) {
//							cell3.setCellValue(record.getR11_SAVINGS());
//							cell3.setCellStyle(textStyle);
//						} else {
//							cell3.setCellValue("");
//							cell3.setCellStyle(textStyle);
//						}
//
//						cell4 = row.createCell(4);
//						if (record.getR11_NOTICE_0_31_DAYS() != null) {
//							cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS());
//							cell4.setCellStyle(textStyle);
//						} else {
//							cell4.setCellValue("");
//							cell4.setCellStyle(textStyle);
//						}
//
//						cell5 = row.createCell(5);
//						if (record.getR11_NOTICE_32_88_DAYS() != null) {
//							cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS());
//							cell5.setCellStyle(textStyle);
//						} else {
//							cell5.setCellValue("");
//							cell5.setCellStyle(textStyle);
//						}
//
//						cell6 = row.createCell(6);
//						if (record.getR11_91_DEPOSIT_DAY() != null) {
//							cell6.setCellValue(record.getR11_91_DEPOSIT_DAY());
//							cell6.setCellStyle(textStyle);
//						} else {
//							cell6.setCellValue("");
//							cell6.setCellStyle(textStyle);
//						}
//
//						cell7 = row.createCell(7);
//						if (record.getR11_FD_1_6_MONTHS() != null) {
//							cell7.setCellValue(record.getR11_FD_1_6_MONTHS());
//							cell7.setCellStyle(textStyle);
//						} else {
//							cell7.setCellValue("");
//							cell7.setCellStyle(textStyle);
//						}
//
//						cell8 = row.createCell(8);
//						if (record.getR11_FD_7_12_MONTHS() != null) {
//							cell8.setCellValue(record.getR11_FD_7_12_MONTHS());
//							cell8.setCellStyle(textStyle);
//						} else {
//							cell8.setCellValue("");
//							cell8.setCellStyle(textStyle);
//						}
//
//						cell9 = row.createCell(9);
//						if (record.getR11_FD_13_18_MONTHS() != null) {
//							cell9.setCellValue(record.getR11_FD_13_18_MONTHS());
//							cell9.setCellStyle(textStyle);
//						} else {
//							cell9.setCellValue("");
//							cell9.setCellStyle(textStyle);
//						}
//
//						cell10 = row.createCell(10);
//						if (record.getR11_FD_19_24_MONTHS() != null) {
//							cell10.setCellValue(record.getR11_FD_19_24_MONTHS());
//							cell10.setCellStyle(textStyle);
//						} else {
//							cell10.setCellValue("");
//							cell10.setCellStyle(textStyle);
//						}
//
//						cell11 = row.createCell(11);
//						if (record.getR11_FD_OVER_24_MONTHS() != null) {
//							cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS());
//							cell11.setCellStyle(textStyle);
//						} else {
//							cell11.setCellValue("");
//							cell11.setCellStyle(textStyle);
//						}
//						cell12 = row.createCell(12);
//						if (record.getR11_TOTAL() != null) {
//							cell12.setCellValue(record.getR11_TOTAL());
//							cell12.setCellStyle(textStyle);
//						} else {
//							cell12.setCellValue("");
//							cell12.setCellStyle(textStyle);
//						}
//
//
//						// ---------- R12 ----------
//						row = sheet.getRow(11);
//						if (row == null) {
//							row = sheet.createRow(11);
//						}
//
//						cell1 = row.createCell(1);
//						if (record.getR12_CURRENT() != null) {
//							cell1.setCellValue(record.getR12_CURRENT());
//							cell1.setCellStyle(textStyle);
//						} else {
//							cell1.setCellValue("");
//							cell1.setCellStyle(textStyle);
//						}
//
//						cell2 = row.createCell(2);
//						if (record.getR12_CALL() != null) {
//							cell2.setCellValue(record.getR12_CALL());
//							cell2.setCellStyle(textStyle);
//						} else {
//							cell2.setCellValue("");
//							cell2.setCellStyle(textStyle);
//						}
//
//						cell3 = row.createCell(3);
//						if (record.getR12_SAVINGS() != null) {
//							cell3.setCellValue(record.getR12_SAVINGS());
//							cell3.setCellStyle(textStyle);
//						} else {
//							cell3.setCellValue("");
//							cell3.setCellStyle(textStyle);
//						}
//
//						cell4 = row.createCell(4);
//						if (record.getR12_NOTICE_0_31_DAYS() != null) {
//							cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS());
//							cell4.setCellStyle(textStyle);
//						} else {
//							cell4.setCellValue("");
//							cell4.setCellStyle(textStyle);
//						}
//
//						cell5 = row.createCell(5);
//						if (record.getR12_NOTICE_32_88_DAYS() != null) {
//							cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS());
//							cell5.setCellStyle(textStyle);
//						} else {
//							cell5.setCellValue("");
//							cell5.setCellStyle(textStyle);
//						}
//
//						cell6 = row.createCell(6);
//						if (record.getR12_91_DEPOSIT_DAY() != null) {
//							cell6.setCellValue(record.getR12_91_DEPOSIT_DAY());
//							cell6.setCellStyle(textStyle);
//						} else {
//							cell6.setCellValue("");
//							cell6.setCellStyle(textStyle);
//						}
//
//						cell7 = row.createCell(7);
//						if (record.getR12_FD_1_6_MONTHS() != null) {
//							cell7.setCellValue(record.getR12_FD_1_6_MONTHS());
//							cell7.setCellStyle(textStyle);
//						} else {
//							cell7.setCellValue("");
//							cell7.setCellStyle(textStyle);
//						}
//
//						cell8 = row.createCell(8);
//						if (record.getR12_FD_7_12_MONTHS() != null) {
//							cell8.setCellValue(record.getR12_FD_7_12_MONTHS());
//							cell8.setCellStyle(textStyle);
//						} else {
//							cell8.setCellValue("");
//							cell8.setCellStyle(textStyle);
//						}
//
//						cell9 = row.createCell(9);
//						if (record.getR12_FD_13_18_MONTHS() != null) {
//							cell9.setCellValue(record.getR12_FD_13_18_MONTHS());
//							cell9.setCellStyle(textStyle);
//						} else {
//							cell9.setCellValue("");
//							cell9.setCellStyle(textStyle);
//						}
//
//						cell10 = row.createCell(10);
//						if (record.getR12_FD_19_24_MONTHS() != null) {
//							cell10.setCellValue(record.getR12_FD_19_24_MONTHS());
//							cell10.setCellStyle(textStyle);
//						} else {
//							cell10.setCellValue("");
//							cell10.setCellStyle(textStyle);
//						}
//
//						cell11 = row.createCell(11);
//						if (record.getR12_FD_OVER_24_MONTHS() != null) {
//							cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS());
//							cell11.setCellStyle(textStyle);
//						} else {
//							cell11.setCellValue("");
//							cell11.setCellStyle(textStyle);
//						}
//						cell12 = row.createCell(12);
//						if (record.getR12_TOTAL() != null) {
//							cell12.setCellValue(record.getR12_TOTAL());
//							cell12.setCellStyle(textStyle);
//						} else {
//							cell12.setCellValue("");
//							cell12.setCellStyle(textStyle);
//						}
//
//
//						// ---------- R13 ----------
//						row = sheet.getRow(12);
//						if (row == null) {
//							row = sheet.createRow(12);
//						}
//
//						cell1 = row.createCell(1);
//						if (record.getR13_CURRENT() != null) {
//							cell1.setCellValue(record.getR13_CURRENT());
//							cell1.setCellStyle(textStyle);
//						} else {
//							cell1.setCellValue("");
//							cell1.setCellStyle(textStyle);
//						}
//
//						cell2 = row.createCell(2);
//						if (record.getR13_CALL() != null) {
//							cell2.setCellValue(record.getR13_CALL());
//							cell2.setCellStyle(textStyle);
//						} else {
//							cell2.setCellValue("");
//							cell2.setCellStyle(textStyle);
//						}
//
//						cell3 = row.createCell(3);
//						if (record.getR13_SAVINGS() != null) {
//							cell3.setCellValue(record.getR13_SAVINGS());
//							cell3.setCellStyle(textStyle);
//						} else {
//							cell3.setCellValue("");
//							cell3.setCellStyle(textStyle);
//						}
//
//						cell4 = row.createCell(4);
//						if (record.getR13_NOTICE_0_31_DAYS() != null) {
//							cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS());
//							cell4.setCellStyle(textStyle);
//						} else {
//							cell4.setCellValue("");
//							cell4.setCellStyle(textStyle);
//						}
//
//						cell5 = row.createCell(5);
//						if (record.getR13_NOTICE_32_88_DAYS() != null) {
//							cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS());
//							cell5.setCellStyle(textStyle);
//						} else {
//							cell5.setCellValue("");
//							cell5.setCellStyle(textStyle);
//						}
//
//						cell6 = row.createCell(6);
//						if (record.getR13_91_DEPOSIT_DAY() != null) {
//							cell6.setCellValue(record.getR13_91_DEPOSIT_DAY());
//							cell6.setCellStyle(textStyle);
//						} else {
//							cell6.setCellValue("");
//							cell6.setCellStyle(textStyle);
//						}
//
//						cell7 = row.createCell(7);
//						if (record.getR13_FD_1_6_MONTHS() != null) {
//							cell7.setCellValue(record.getR13_FD_1_6_MONTHS());
//							cell7.setCellStyle(textStyle);
//						} else {
//							cell7.setCellValue("");
//							cell7.setCellStyle(textStyle);
//						}
//
//						cell8 = row.createCell(8);
//						if (record.getR13_FD_7_12_MONTHS() != null) {
//							cell8.setCellValue(record.getR13_FD_7_12_MONTHS());
//							cell8.setCellStyle(textStyle);
//						} else {
//							cell8.setCellValue("");
//							cell8.setCellStyle(textStyle);
//						}
//
//						cell9 = row.createCell(9);
//						if (record.getR13_FD_13_18_MONTHS() != null) {
//							cell9.setCellValue(record.getR13_FD_13_18_MONTHS());
//							cell9.setCellStyle(textStyle);
//						} else {
//							cell9.setCellValue("");
//							cell9.setCellStyle(textStyle);
//						}
//
//						cell10 = row.createCell(10);
//						if (record.getR13_FD_19_24_MONTHS() != null) {
//							cell10.setCellValue(record.getR13_FD_19_24_MONTHS());
//							cell10.setCellStyle(textStyle);
//						} else {
//							cell10.setCellValue("");
//							cell10.setCellStyle(textStyle);
//						}
//
//						cell11 = row.createCell(11);
//						if (record.getR13_FD_OVER_24_MONTHS() != null) {
//							cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS());
//							cell11.setCellStyle(textStyle);
//						} else {
//							cell11.setCellValue("");
//							cell11.setCellStyle(textStyle);
//						}
//						cell12 = row.createCell(12);
//						if (record.getR13_TOTAL() != null) {
//							cell12.setCellValue(record.getR13_TOTAL());
//							cell12.setCellStyle(textStyle);
//						} else {
//							cell12.setCellValue("");
//							cell12.setCellStyle(textStyle);
//						}
//
//
//						// ---------- R14 ----------
//						row = sheet.getRow(13);
//						if (row == null) {
//							row = sheet.createRow(13);
//						}
//
//						cell1 = row.createCell(1);
//						if (record.getR14_CURRENT() != null) {
//							cell1.setCellValue(record.getR14_CURRENT());
//							cell1.setCellStyle(textStyle);
//						} else {
//							cell1.setCellValue("");
//							cell1.setCellStyle(textStyle);
//						}
//
//						cell2 = row.createCell(2);
//						if (record.getR14_CALL() != null) {
//							cell2.setCellValue(record.getR14_CALL());
//							cell2.setCellStyle(textStyle);
//						} else {
//							cell2.setCellValue("");
//							cell2.setCellStyle(textStyle);
//						}
//
//						cell3 = row.createCell(3);
//						if (record.getR14_SAVINGS() != null) {
//							cell3.setCellValue(record.getR14_SAVINGS());
//							cell3.setCellStyle(textStyle);
//						} else {
//							cell3.setCellValue("");
//							cell3.setCellStyle(textStyle);
//						}
//
//						cell4 = row.createCell(4);
//						if (record.getR14_NOTICE_0_31_DAYS() != null) {
//							cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS());
//							cell4.setCellStyle(textStyle);
//						} else {
//							cell4.setCellValue("");
//							cell4.setCellStyle(textStyle);
//						}
//
//						cell5 = row.createCell(5);
//						if (record.getR14_NOTICE_32_88_DAYS() != null) {
//							cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS());
//							cell5.setCellStyle(textStyle);
//						} else {
//							cell5.setCellValue("");
//							cell5.setCellStyle(textStyle);
//						}
//
//						cell6 = row.createCell(6);
//						if (record.getR14_91_DEPOSIT_DAY() != null) {
//							cell6.setCellValue(record.getR14_91_DEPOSIT_DAY());
//							cell6.setCellStyle(textStyle);
//						} else {
//							cell6.setCellValue("");
//							cell6.setCellStyle(textStyle);
//						}
//
//						cell7 = row.createCell(7);
//						if (record.getR14_FD_1_6_MONTHS() != null) {
//							cell7.setCellValue(record.getR14_FD_1_6_MONTHS());
//							cell7.setCellStyle(textStyle);
//						} else {
//							cell7.setCellValue("");
//							cell7.setCellStyle(textStyle);
//						}
//
//						cell8 = row.createCell(8);
//						if (record.getR14_FD_7_12_MONTHS() != null) {
//							cell8.setCellValue(record.getR14_FD_7_12_MONTHS());
//							cell8.setCellStyle(textStyle);
//						} else {
//							cell8.setCellValue("");
//							cell8.setCellStyle(textStyle);
//						}
//
//						cell9 = row.createCell(9);
//						if (record.getR14_FD_13_18_MONTHS() != null) {
//							cell9.setCellValue(record.getR14_FD_13_18_MONTHS());
//							cell9.setCellStyle(textStyle);
//						} else {
//							cell9.setCellValue("");
//							cell9.setCellStyle(textStyle);
//						}
//
//						cell10 = row.createCell(10);
//						if (record.getR14_FD_19_24_MONTHS() != null) {
//							cell10.setCellValue(record.getR14_FD_19_24_MONTHS());
//							cell10.setCellStyle(textStyle);
//						} else {
//							cell10.setCellValue("");
//							cell10.setCellStyle(textStyle);
//						}
//
//						cell11 = row.createCell(11);
//						if (record.getR14_FD_OVER_24_MONTHS() != null) {
//							cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS());
//							cell11.setCellStyle(textStyle);
//						} else {
//							cell11.setCellValue("");
//							cell11.setCellStyle(textStyle);
//						}
//						cell12 = row.createCell(12);
//						if (record.getR14_TOTAL() != null) {
//							cell12.setCellValue(record.getR14_TOTAL());
//							cell12.setCellStyle(textStyle);
//						} else {
//							cell12.setCellValue("");
//							cell12.setCellStyle(textStyle);
//						}
//						
//						// ---------- R15 ----------
//						row = sheet.getRow(14);
//						if (row == null) {
//						    row = sheet.createRow(14);
//						}
//
//						cell1 = row.createCell(1);
//						if (record.getR15_CURRENT() != null) {
//						    cell1.setCellValue(record.getR15_CURRENT());
//						    cell1.setCellStyle(textStyle);
//						} else {
//						    cell1.setCellValue("");
//						    cell1.setCellStyle(textStyle);
//						}
//
//						cell2 = row.createCell(2);
//						if (record.getR15_CALL() != null) {
//						    cell2.setCellValue(record.getR15_CALL());
//						    cell2.setCellStyle(textStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}
//
//						cell3 = row.createCell(3);
//						if (record.getR15_SAVINGS() != null) {
//						    cell3.setCellValue(record.getR15_SAVINGS());
//						    cell3.setCellStyle(textStyle);
//						} else {
//						    cell3.setCellValue("");
//						    cell3.setCellStyle(textStyle);
//						}
//
//						cell4 = row.createCell(4);
//						if (record.getR15_NOTICE_0_31_DAYS() != null) {
//						    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS());
//						    cell4.setCellStyle(textStyle);
//						} else {
//						    cell4.setCellValue("");
//						    cell4.setCellStyle(textStyle);
//						}
//
//						cell5 = row.createCell(5);
//						if (record.getR15_NOTICE_32_88_DAYS() != null) {
//						    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS());
//						    cell5.setCellStyle(textStyle);
//						} else {
//						    cell5.setCellValue("");
//						    cell5.setCellStyle(textStyle);
//						}
//
//						cell6 = row.createCell(6);
//						if (record.getR15_91_DEPOSIT_DAY() != null) {
//						    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY());
//						    cell6.setCellStyle(textStyle);
//						} else {
//						    cell6.setCellValue("");
//						    cell6.setCellStyle(textStyle);
//						}
//
//						cell7 = row.createCell(7);
//						if (record.getR15_FD_1_6_MONTHS() != null) {
//						    cell7.setCellValue(record.getR15_FD_1_6_MONTHS());
//						    cell7.setCellStyle(textStyle);
//						} else {
//						    cell7.setCellValue("");
//						    cell7.setCellStyle(textStyle);
//						}
//
//						cell8 = row.createCell(8);
//						if (record.getR15_FD_7_12_MONTHS() != null) {
//						    cell8.setCellValue(record.getR15_FD_7_12_MONTHS());
//						    cell8.setCellStyle(textStyle);
//						} else {
//						    cell8.setCellValue("");
//						    cell8.setCellStyle(textStyle);
//						}
//
//						cell9 = row.createCell(9);
//						if (record.getR15_FD_13_18_MONTHS() != null) {
//						    cell9.setCellValue(record.getR15_FD_13_18_MONTHS());
//						    cell9.setCellStyle(textStyle);
//						} else {
//						    cell9.setCellValue("");
//						    cell9.setCellStyle(textStyle);
//						}
//
//						cell10 = row.createCell(10);
//						if (record.getR15_FD_19_24_MONTHS() != null) {
//						    cell10.setCellValue(record.getR15_FD_19_24_MONTHS());
//						    cell10.setCellStyle(textStyle);
//						} else {
//						    cell10.setCellValue("");
//						    cell10.setCellStyle(textStyle);
//						}
//
//						cell11 = row.createCell(11);
//						if (record.getR15_FD_OVER_24_MONTHS() != null) {
//						    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS());
//						    cell11.setCellStyle(textStyle);
//						} else {
//						    cell11.setCellValue("");
//						    cell11.setCellStyle(textStyle);
//						}
//
//						cell12 = row.createCell(12);
//						if (record.getR15_TOTAL() != null) {
//						    cell12.setCellValue(record.getR15_TOTAL());
//						    cell12.setCellStyle(textStyle);
//						} else {
//						    cell12.setCellValue("");
//						    cell12.setCellStyle(textStyle);
//						}
//
//						}
//
//						workbook.setForceFormulaRecalculation(true);
//					} else {
//
//					}
//
//		// Write the final workbook content to the in-memory stream.
//					workbook.write(out);
//
//					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
//					
//					// audit service summary resub format
//
//
//					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//										if (attrs != null) {
//											HttpServletRequest request = attrs.getRequest();
//											String userid = (String) request.getSession().getAttribute("USERID");
//											auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES_FCA RESUB SUMMARY", null, "BRRS_M_INT_RATES_FCA_RESUB_SUMMARYTABLE");
//										}
//
//					return out.toByteArray();
//				}
//
//			}

			// Archival Email Excel
			public byte[] BRRS_M_INT_RATES_FCAARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Archival Email Excel generation process in memory.");

				List<M_INT_RATES_FCA_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_INT_RATES_FCA report. Returning empty result.");
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

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_INT_RATES_FCA_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
							//row6
							// Column B
							Cell cellBdate = row.createCell(3);
							if (record.getReport_date() != null) {
								cellBdate.setCellValue(record.getReport_date());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}
							
							
							
							//ROW 10
							row = sheet.getRow(9);

			
								Cell cell1 = row.createCell(1);
							if (record.getR10_CURRENT() != null) {
								cell1.setCellValue(record.getR10_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							Cell cell2 = row.createCell(2);
							if (record.getR10_CALL() != null) {
								cell2.setCellValue(record.getR10_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(3);
							if (record.getR10_SAVINGS() != null) {
								cell3.setCellValue(record.getR10_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(4);
							if (record.getR10_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							Cell cell5 = row.createCell(5);
							if (record.getR10_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							Cell cell6 = row.createCell(6);
							if (record.getR10_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR10_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(7);
							if (record.getR10_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR10_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							Cell cell8 = row.createCell(8);
							if (record.getR10_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR10_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							Cell cell9 = row.createCell(9);
							if (record.getR10_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR10_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							Cell cell10 = row.createCell(10);
							if (record.getR10_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR10_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							Cell cell11 = row.createCell(11);
							if (record.getR10_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							Cell cell12 = row.createCell(12);
							if (record.getR10_TOTAL() != null) {
								cell12.setCellValue(record.getR10_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R11 ----------
							row = sheet.getRow(10);
							if (row == null) {
								row = sheet.createRow(10);
							}

							cell1 = row.createCell(1);
							if (record.getR11_CURRENT() != null) {
								cell1.setCellValue(record.getR11_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR11_CALL() != null) {
								cell2.setCellValue(record.getR11_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR11_SAVINGS() != null) {
								cell3.setCellValue(record.getR11_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR11_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR11_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR11_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR11_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR11_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR11_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR11_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR11_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR11_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR11_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR11_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR11_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR11_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							cell12 = row.createCell(12);
							if (record.getR11_TOTAL() != null) {
								cell12.setCellValue(record.getR11_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R12 ----------
							row = sheet.getRow(11);
							if (row == null) {
								row = sheet.createRow(11);
							}

							cell1 = row.createCell(1);
							if (record.getR12_CURRENT() != null) {
								cell1.setCellValue(record.getR12_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR12_CALL() != null) {
								cell2.setCellValue(record.getR12_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR12_SAVINGS() != null) {
								cell3.setCellValue(record.getR12_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR12_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR12_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR12_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR12_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR12_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR12_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR12_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR12_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR12_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR12_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR12_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR12_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR12_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							cell12 = row.createCell(12);
							if (record.getR12_TOTAL() != null) {
								cell12.setCellValue(record.getR12_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R13 ----------
							row = sheet.getRow(12);
							if (row == null) {
								row = sheet.createRow(12);
							}

							cell1 = row.createCell(1);
							if (record.getR13_CURRENT() != null) {
								cell1.setCellValue(record.getR13_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR13_CALL() != null) {
								cell2.setCellValue(record.getR13_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR13_SAVINGS() != null) {
								cell3.setCellValue(record.getR13_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR13_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR13_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR13_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR13_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR13_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR13_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR13_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR13_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR13_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR13_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR13_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR13_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR13_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
						 cell12 = row.createCell(12);
							if (record.getR13_TOTAL() != null) {
								cell12.setCellValue(record.getR13_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R14 ----------
							row = sheet.getRow(13);
							if (row == null) {
								row = sheet.createRow(13);
							}

							cell1 = row.createCell(1);
							if (record.getR14_CURRENT() != null) {
								cell1.setCellValue(record.getR14_CURRENT());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR14_CALL() != null) {
								cell2.setCellValue(record.getR14_CALL());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR14_SAVINGS() != null) {
								cell3.setCellValue(record.getR14_SAVINGS());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR14_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS());
								cell4.setCellStyle(textStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR14_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS());
								cell5.setCellStyle(textStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR14_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR14_91_DEPOSIT_DAY());
								cell6.setCellStyle(textStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR14_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR14_FD_1_6_MONTHS());
								cell7.setCellStyle(textStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR14_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR14_FD_7_12_MONTHS());
								cell8.setCellStyle(textStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR14_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR14_FD_13_18_MONTHS());
								cell9.setCellStyle(textStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR14_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR14_FD_19_24_MONTHS());
								cell10.setCellStyle(textStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR14_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS());
								cell11.setCellStyle(textStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
						 cell12 = row.createCell(12);
							if (record.getR14_TOTAL() != null) {
								cell12.setCellValue(record.getR14_TOTAL());
								cell12.setCellStyle(textStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}
							
							
							// ---------- R15 ----------
							row = sheet.getRow(14);
							if (row == null) {
							    row = sheet.createRow(14);
							}

							cell1 = row.createCell(1);
							if (record.getR15_CURRENT() != null) {
							    cell1.setCellValue(record.getR15_CURRENT());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR15_CALL() != null) {
							    cell2.setCellValue(record.getR15_CALL());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR15_SAVINGS() != null) {
							    cell3.setCellValue(record.getR15_SAVINGS());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR15_NOTICE_0_31_DAYS() != null) {
							    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR15_NOTICE_32_88_DAYS() != null) {
							    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS());
							    cell5.setCellStyle(textStyle);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR15_91_DEPOSIT_DAY() != null) {
							    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY());
							    cell6.setCellStyle(textStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR15_FD_1_6_MONTHS() != null) {
							    cell7.setCellValue(record.getR15_FD_1_6_MONTHS());
							    cell7.setCellStyle(textStyle);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR15_FD_7_12_MONTHS() != null) {
							    cell8.setCellValue(record.getR15_FD_7_12_MONTHS());
							    cell8.setCellStyle(textStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR15_FD_13_18_MONTHS() != null) {
							    cell9.setCellValue(record.getR15_FD_13_18_MONTHS());
							    cell9.setCellStyle(textStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR15_FD_19_24_MONTHS() != null) {
							    cell10.setCellValue(record.getR15_FD_19_24_MONTHS());
							    cell10.setCellStyle(textStyle);
							} else {
							    cell10.setCellValue("");
							    cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR15_FD_OVER_24_MONTHS() != null) {
							    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS());
							    cell11.setCellStyle(textStyle);
							} else {
							    cell11.setCellValue("");
							    cell11.setCellStyle(textStyle);
							}

							cell12 = row.createCell(12);
							if (record.getR15_TOTAL() != null) {
							    cell12.setCellValue(record.getR15_TOTAL());
							    cell12.setCellStyle(textStyle);
							} else {
							    cell12.setCellValue("");
							    cell12.setCellStyle(textStyle);
							}

						}
						workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					

					// audit service archival summary email


						ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
										if (attrs != null) {
											HttpServletRequest request = attrs.getRequest();
											String userid = (String) request.getSession().getAttribute("USERID");
											auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES_FCA EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_INT_RATES_FCA_ARCHIVALTABLE_SUMMARY");
										}

					return out.toByteArray();
				}
			}

			// Resub Format excel
			public byte[] BRRS_M_INT_RATES_FCAResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_INT_RATES_FCAEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			}

				List<M_INT_RATES_FCA_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_INT_RATES_FCA report. Returning empty result.");
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

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {

							M_INT_RATES_FCA_RESUB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							
							//row6
							// Column B
							Cell cellBdate = row.createCell(3);
							if (record.getReport_date() != null) {
								cellBdate.setCellValue(record.getReport_date());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}
							
							
							
							//ROW 10
							row = sheet.getRow(9);
										Cell cell1 = row.createCell(1);
						if (record.getR10_CURRENT() != null) {
							cell1.setCellValue(record.getR10_CURRENT());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						Cell cell2 = row.createCell(2);
						if (record.getR10_CALL() != null) {
							cell2.setCellValue(record.getR10_CALL());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(3);
						if (record.getR10_SAVINGS() != null) {
							cell3.setCellValue(record.getR10_SAVINGS());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(4);
						if (record.getR10_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS());
							cell4.setCellStyle(textStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						Cell cell5 = row.createCell(5);
						if (record.getR10_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS());
							cell5.setCellStyle(textStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						Cell cell6 = row.createCell(6);
						if (record.getR10_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR10_91_DEPOSIT_DAY());
							cell6.setCellStyle(textStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						Cell cell7 = row.createCell(7);
						if (record.getR10_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR10_FD_1_6_MONTHS());
							cell7.setCellStyle(textStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						Cell cell8 = row.createCell(8);
						if (record.getR10_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR10_FD_7_12_MONTHS());
							cell8.setCellStyle(textStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						Cell cell9 = row.createCell(9);
						if (record.getR10_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR10_FD_13_18_MONTHS());
							cell9.setCellStyle(textStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						Cell cell10 = row.createCell(10);
						if (record.getR10_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR10_FD_19_24_MONTHS());
							cell10.setCellStyle(textStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						Cell cell11 = row.createCell(11);
						if (record.getR10_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS());
							cell11.setCellStyle(textStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
						Cell cell12 = row.createCell(12);
						if (record.getR10_TOTAL() != null) {
							cell12.setCellValue(record.getR10_TOTAL());
							cell12.setCellStyle(textStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// ---------- R11 ----------
						row = sheet.getRow(10);
						if (row == null) {
							row = sheet.createRow(10);
						}

						cell1 = row.createCell(1);
						if (record.getR11_CURRENT() != null) {
							cell1.setCellValue(record.getR11_CURRENT());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR11_CALL() != null) {
							cell2.setCellValue(record.getR11_CALL());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR11_SAVINGS() != null) {
							cell3.setCellValue(record.getR11_SAVINGS());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR11_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS());
							cell4.setCellStyle(textStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR11_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS());
							cell5.setCellStyle(textStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR11_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR11_91_DEPOSIT_DAY());
							cell6.setCellStyle(textStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR11_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR11_FD_1_6_MONTHS());
							cell7.setCellStyle(textStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR11_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR11_FD_7_12_MONTHS());
							cell8.setCellStyle(textStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR11_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR11_FD_13_18_MONTHS());
							cell9.setCellStyle(textStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR11_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR11_FD_19_24_MONTHS());
							cell10.setCellStyle(textStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR11_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS());
							cell11.setCellStyle(textStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						cell12 = row.createCell(12);
						if (record.getR11_TOTAL() != null) {
							cell12.setCellValue(record.getR11_TOTAL());
							cell12.setCellStyle(textStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}


						// ---------- R12 ----------
						row = sheet.getRow(11);
						if (row == null) {
							row = sheet.createRow(11);
						}

						cell1 = row.createCell(1);
						if (record.getR12_CURRENT() != null) {
							cell1.setCellValue(record.getR12_CURRENT());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR12_CALL() != null) {
							cell2.setCellValue(record.getR12_CALL());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR12_SAVINGS() != null) {
							cell3.setCellValue(record.getR12_SAVINGS());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR12_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS());
							cell4.setCellStyle(textStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR12_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS());
							cell5.setCellStyle(textStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR12_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR12_91_DEPOSIT_DAY());
							cell6.setCellStyle(textStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR12_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR12_FD_1_6_MONTHS());
							cell7.setCellStyle(textStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR12_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR12_FD_7_12_MONTHS());
							cell8.setCellStyle(textStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR12_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR12_FD_13_18_MONTHS());
							cell9.setCellStyle(textStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR12_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR12_FD_19_24_MONTHS());
							cell10.setCellStyle(textStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR12_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS());
							cell11.setCellStyle(textStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						cell12 = row.createCell(12);
						if (record.getR12_TOTAL() != null) {
							cell12.setCellValue(record.getR12_TOTAL());
							cell12.setCellStyle(textStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}


						// ---------- R13 ----------
						row = sheet.getRow(12);
						if (row == null) {
							row = sheet.createRow(12);
						}

						cell1 = row.createCell(1);
						if (record.getR13_CURRENT() != null) {
							cell1.setCellValue(record.getR13_CURRENT());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR13_CALL() != null) {
							cell2.setCellValue(record.getR13_CALL());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR13_SAVINGS() != null) {
							cell3.setCellValue(record.getR13_SAVINGS());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR13_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS());
							cell4.setCellStyle(textStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR13_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS());
							cell5.setCellStyle(textStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR13_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR13_91_DEPOSIT_DAY());
							cell6.setCellStyle(textStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR13_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR13_FD_1_6_MONTHS());
							cell7.setCellStyle(textStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR13_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR13_FD_7_12_MONTHS());
							cell8.setCellStyle(textStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR13_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR13_FD_13_18_MONTHS());
							cell9.setCellStyle(textStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR13_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR13_FD_19_24_MONTHS());
							cell10.setCellStyle(textStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR13_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS());
							cell11.setCellStyle(textStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						cell12 = row.createCell(12);
						if (record.getR13_TOTAL() != null) {
							cell12.setCellValue(record.getR13_TOTAL());
							cell12.setCellStyle(textStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}


						// ---------- R14 ----------
						row = sheet.getRow(13);
						if (row == null) {
							row = sheet.createRow(13);
						}

						cell1 = row.createCell(1);
						if (record.getR14_CURRENT() != null) {
							cell1.setCellValue(record.getR14_CURRENT());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR14_CALL() != null) {
							cell2.setCellValue(record.getR14_CALL());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_SAVINGS() != null) {
							cell3.setCellValue(record.getR14_SAVINGS());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR14_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS());
							cell4.setCellStyle(textStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR14_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS());
							cell5.setCellStyle(textStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR14_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR14_91_DEPOSIT_DAY());
							cell6.setCellStyle(textStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR14_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR14_FD_1_6_MONTHS());
							cell7.setCellStyle(textStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR14_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR14_FD_7_12_MONTHS());
							cell8.setCellStyle(textStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR14_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR14_FD_13_18_MONTHS());
							cell9.setCellStyle(textStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR14_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR14_FD_19_24_MONTHS());
							cell10.setCellStyle(textStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR14_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS());
							cell11.setCellStyle(textStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						cell12 = row.createCell(12);
						if (record.getR14_TOTAL() != null) {
							cell12.setCellValue(record.getR14_TOTAL());
							cell12.setCellStyle(textStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}
						
						// ---------- R15 ----------
						row = sheet.getRow(14);
						if (row == null) {
						    row = sheet.createRow(14);
						}

						cell1 = row.createCell(1);
						if (record.getR15_CURRENT() != null) {
						    cell1.setCellValue(record.getR15_CURRENT());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR15_CALL() != null) {
						    cell2.setCellValue(record.getR15_CALL());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR15_SAVINGS() != null) {
						    cell3.setCellValue(record.getR15_SAVINGS());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR15_NOTICE_0_31_DAYS() != null) {
						    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR15_NOTICE_32_88_DAYS() != null) {
						    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS());
						    cell5.setCellStyle(textStyle);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR15_91_DEPOSIT_DAY() != null) {
						    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY());
						    cell6.setCellStyle(textStyle);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR15_FD_1_6_MONTHS() != null) {
						    cell7.setCellValue(record.getR15_FD_1_6_MONTHS());
						    cell7.setCellStyle(textStyle);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR15_FD_7_12_MONTHS() != null) {
						    cell8.setCellValue(record.getR15_FD_7_12_MONTHS());
						    cell8.setCellStyle(textStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR15_FD_13_18_MONTHS() != null) {
						    cell9.setCellValue(record.getR15_FD_13_18_MONTHS());
						    cell9.setCellStyle(textStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR15_FD_19_24_MONTHS() != null) {
						    cell10.setCellValue(record.getR15_FD_19_24_MONTHS());
						    cell10.setCellStyle(textStyle);
						} else {
						    cell10.setCellValue("");
						    cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR15_FD_OVER_24_MONTHS() != null) {
						    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS());
						    cell11.setCellStyle(textStyle);
						} else {
						    cell11.setCellValue("");
						    cell11.setCellStyle(textStyle);
						}

						cell12 = row.createCell(12);
						if (record.getR15_TOTAL() != null) {
						    cell12.setCellValue(record.getR15_TOTAL());
						    cell12.setCellStyle(textStyle);
						} else {
						    cell12.setCellValue("");
						    cell12.setCellStyle(textStyle);
						}

						}
						workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					
					// audit service summary resub format


					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
										if (attrs != null) {
											HttpServletRequest request = attrs.getRequest();
											String userid = (String) request.getSession().getAttribute("USERID");
											auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES_FCA RESUB SUMMARY", null, "BRRS_M_INT_RATES_FCA_RESUB_SUMMARYTABLE");
										}
					

					return out.toByteArray();
				}

			}

			// Resub Email Excel
			public byte[] BRRS_M_INT_RATES_FCAEmailResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Archival Email Excel generation process in memory.");

				List<M_INT_RATES_FCA_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_INT_RATES_FCA report. Returning empty result.");
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

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_INT_RATES_FCA_RESUB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							
							//row6
							// Column B
							Cell cellBdate = row.createCell(3);
							if (record.getReport_date() != null) {
								cellBdate.setCellValue(record.getReport_date());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}
							
							
							
							//ROW 10
							row = sheet.getRow(9);

			Cell cell1 = row.createCell(1);
						if (record.getR10_CURRENT() != null) {
							cell1.setCellValue(record.getR10_CURRENT());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						Cell cell2 = row.createCell(2);
						if (record.getR10_CALL() != null) {
							cell2.setCellValue(record.getR10_CALL());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(3);
						if (record.getR10_SAVINGS() != null) {
							cell3.setCellValue(record.getR10_SAVINGS());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(4);
						if (record.getR10_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS());
							cell4.setCellStyle(textStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						Cell cell5 = row.createCell(5);
						if (record.getR10_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS());
							cell5.setCellStyle(textStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						Cell cell6 = row.createCell(6);
						if (record.getR10_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR10_91_DEPOSIT_DAY());
							cell6.setCellStyle(textStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						Cell cell7 = row.createCell(7);
						if (record.getR10_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR10_FD_1_6_MONTHS());
							cell7.setCellStyle(textStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						Cell cell8 = row.createCell(8);
						if (record.getR10_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR10_FD_7_12_MONTHS());
							cell8.setCellStyle(textStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						Cell cell9 = row.createCell(9);
						if (record.getR10_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR10_FD_13_18_MONTHS());
							cell9.setCellStyle(textStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						Cell cell10 = row.createCell(10);
						if (record.getR10_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR10_FD_19_24_MONTHS());
							cell10.setCellStyle(textStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						Cell cell11 = row.createCell(11);
						if (record.getR10_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS());
							cell11.setCellStyle(textStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
						Cell cell12 = row.createCell(12);
						if (record.getR10_TOTAL() != null) {
							cell12.setCellValue(record.getR10_TOTAL());
							cell12.setCellStyle(textStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// ---------- R11 ----------
						row = sheet.getRow(10);
						if (row == null) {
							row = sheet.createRow(10);
						}

						cell1 = row.createCell(1);
						if (record.getR11_CURRENT() != null) {
							cell1.setCellValue(record.getR11_CURRENT());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR11_CALL() != null) {
							cell2.setCellValue(record.getR11_CALL());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR11_SAVINGS() != null) {
							cell3.setCellValue(record.getR11_SAVINGS());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR11_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS());
							cell4.setCellStyle(textStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR11_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS());
							cell5.setCellStyle(textStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR11_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR11_91_DEPOSIT_DAY());
							cell6.setCellStyle(textStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR11_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR11_FD_1_6_MONTHS());
							cell7.setCellStyle(textStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR11_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR11_FD_7_12_MONTHS());
							cell8.setCellStyle(textStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR11_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR11_FD_13_18_MONTHS());
							cell9.setCellStyle(textStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR11_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR11_FD_19_24_MONTHS());
							cell10.setCellStyle(textStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR11_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS());
							cell11.setCellStyle(textStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
						cell12 = row.createCell(12);
						if (record.getR11_TOTAL() != null) {
							cell12.setCellValue(record.getR11_TOTAL());
							cell12.setCellStyle(textStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// ---------- R12 ----------
						row = sheet.getRow(11);
						if (row == null) {
							row = sheet.createRow(11);
						}

						cell1 = row.createCell(1);
						if (record.getR12_CURRENT() != null) {
							cell1.setCellValue(record.getR12_CURRENT());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR12_CALL() != null) {
							cell2.setCellValue(record.getR12_CALL());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR12_SAVINGS() != null) {
							cell3.setCellValue(record.getR12_SAVINGS());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR12_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS());
							cell4.setCellStyle(textStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR12_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS());
							cell5.setCellStyle(textStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR12_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR12_91_DEPOSIT_DAY());
							cell6.setCellStyle(textStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR12_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR12_FD_1_6_MONTHS());
							cell7.setCellStyle(textStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR12_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR12_FD_7_12_MONTHS());
							cell8.setCellStyle(textStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR12_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR12_FD_13_18_MONTHS());
							cell9.setCellStyle(textStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR12_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR12_FD_19_24_MONTHS());
							cell10.setCellStyle(textStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR12_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS());
							cell11.setCellStyle(textStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
						cell12 = row.createCell(12);
						if (record.getR12_TOTAL() != null) {
							cell12.setCellValue(record.getR12_TOTAL());
							cell12.setCellStyle(textStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// ---------- R13 ----------
						row = sheet.getRow(12);
						if (row == null) {
							row = sheet.createRow(12);
						}

						cell1 = row.createCell(1);
						if (record.getR13_CURRENT() != null) {
							cell1.setCellValue(record.getR13_CURRENT());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR13_CALL() != null) {
							cell2.setCellValue(record.getR13_CALL());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR13_SAVINGS() != null) {
							cell3.setCellValue(record.getR13_SAVINGS());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR13_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS());
							cell4.setCellStyle(textStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR13_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS());
							cell5.setCellStyle(textStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR13_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR13_91_DEPOSIT_DAY());
							cell6.setCellStyle(textStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR13_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR13_FD_1_6_MONTHS());
							cell7.setCellStyle(textStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR13_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR13_FD_7_12_MONTHS());
							cell8.setCellStyle(textStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR13_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR13_FD_13_18_MONTHS());
							cell9.setCellStyle(textStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR13_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR13_FD_19_24_MONTHS());
							cell10.setCellStyle(textStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR13_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS());
							cell11.setCellStyle(textStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
					 cell12 = row.createCell(12);
						if (record.getR13_TOTAL() != null) {
							cell12.setCellValue(record.getR13_TOTAL());
							cell12.setCellStyle(textStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// ---------- R14 ----------
						row = sheet.getRow(13);
						if (row == null) {
							row = sheet.createRow(13);
						}

						cell1 = row.createCell(1);
						if (record.getR14_CURRENT() != null) {
							cell1.setCellValue(record.getR14_CURRENT());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR14_CALL() != null) {
							cell2.setCellValue(record.getR14_CALL());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_SAVINGS() != null) {
							cell3.setCellValue(record.getR14_SAVINGS());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR14_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS());
							cell4.setCellStyle(textStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR14_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS());
							cell5.setCellStyle(textStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR14_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR14_91_DEPOSIT_DAY());
							cell6.setCellStyle(textStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR14_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR14_FD_1_6_MONTHS());
							cell7.setCellStyle(textStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR14_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR14_FD_7_12_MONTHS());
							cell8.setCellStyle(textStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR14_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR14_FD_13_18_MONTHS());
							cell9.setCellStyle(textStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR14_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR14_FD_19_24_MONTHS());
							cell10.setCellStyle(textStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR14_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS());
							cell11.setCellStyle(textStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
					 cell12 = row.createCell(12);
						if (record.getR14_TOTAL() != null) {
							cell12.setCellValue(record.getR14_TOTAL());
							cell12.setCellStyle(textStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}
						
						
						// ---------- R15 ----------
						row = sheet.getRow(14);
						if (row == null) {
						    row = sheet.createRow(14);
						}

						cell1 = row.createCell(1);
						if (record.getR15_CURRENT() != null) {
						    cell1.setCellValue(record.getR15_CURRENT());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR15_CALL() != null) {
						    cell2.setCellValue(record.getR15_CALL());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR15_SAVINGS() != null) {
						    cell3.setCellValue(record.getR15_SAVINGS());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR15_NOTICE_0_31_DAYS() != null) {
						    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR15_NOTICE_32_88_DAYS() != null) {
						    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS());
						    cell5.setCellStyle(textStyle);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR15_91_DEPOSIT_DAY() != null) {
						    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY());
						    cell6.setCellStyle(textStyle);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR15_FD_1_6_MONTHS() != null) {
						    cell7.setCellValue(record.getR15_FD_1_6_MONTHS());
						    cell7.setCellStyle(textStyle);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR15_FD_7_12_MONTHS() != null) {
						    cell8.setCellValue(record.getR15_FD_7_12_MONTHS());
						    cell8.setCellStyle(textStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR15_FD_13_18_MONTHS() != null) {
						    cell9.setCellValue(record.getR15_FD_13_18_MONTHS());
						    cell9.setCellStyle(textStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR15_FD_19_24_MONTHS() != null) {
						    cell10.setCellValue(record.getR15_FD_19_24_MONTHS());
						    cell10.setCellStyle(textStyle);
						} else {
						    cell10.setCellValue("");
						    cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR15_FD_OVER_24_MONTHS() != null) {
						    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS());
						    cell11.setCellStyle(textStyle);
						} else {
						    cell11.setCellValue("");
						    cell11.setCellStyle(textStyle);
						}

						cell12 = row.createCell(12);
						if (record.getR15_TOTAL() != null) {
						    cell12.setCellValue(record.getR15_TOTAL());
						    cell12.setCellStyle(textStyle);
						} else {
						    cell12.setCellValue("");
						    cell12.setCellStyle(textStyle);
						}

						}
						 workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					
					// audit service summary resub email

					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
									if (attrs != null) {
										HttpServletRequest request = attrs.getRequest();
										String userid = (String) request.getSession().getAttribute("USERID");
										auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES_FCA EMAIL RESUB SUMMARY", null, "BRRS_M_INT_RATES_FCA_RESUB_SUMMARYTABLE");
									}


					return out.toByteArray();
				}
			}

	// =========================================================
	// ROW MAPPERS
	// =========================================================

	// =========================================================
	// Inner entity: M_INT_RATES_FCA_Summary_Entity
	// =========================================================
	public static class M_INT_RATES_FCA_Summary_Entity {
		private String R10_CURRENCY;
		private String R10_CURRENT;
		private String R10_CALL;
		private String R10_SAVINGS;
		private String R10_NOTICE_0_31_DAYS;
		private String R10_NOTICE_32_88_DAYS;
		private String R10_91_DEPOSIT_DAY;
		private String R10_FD_1_6_MONTHS;
		private String R10_FD_7_12_MONTHS;
		private String R10_FD_13_18_MONTHS;
		private String R10_FD_19_24_MONTHS;
		private String R10_FD_OVER_24_MONTHS;
		private String R10_TOTAL;
		private String R11_CURRENCY;
		private String R11_CURRENT;
		private String R11_CALL;
		private String R11_SAVINGS;
		private String R11_NOTICE_0_31_DAYS;
		private String R11_NOTICE_32_88_DAYS;
		private String R11_91_DEPOSIT_DAY;
		private String R11_FD_1_6_MONTHS;
		private String R11_FD_7_12_MONTHS;
		private String R11_FD_13_18_MONTHS;
		private String R11_FD_19_24_MONTHS;
		private String R11_FD_OVER_24_MONTHS;
		private String R11_TOTAL;
		private String R12_CURRENCY;
		private String R12_CURRENT;
		private String R12_CALL;
		private String R12_SAVINGS;
		private String R12_NOTICE_0_31_DAYS;
		private String R12_NOTICE_32_88_DAYS;
		private String R12_91_DEPOSIT_DAY;
		private String R12_FD_1_6_MONTHS;
		private String R12_FD_7_12_MONTHS;
		private String R12_FD_13_18_MONTHS;
		private String R12_FD_19_24_MONTHS;
		private String R12_FD_OVER_24_MONTHS;
		private String R12_TOTAL;
		private String R13_CURRENCY;
		private String R13_CURRENT;
		private String R13_CALL;
		private String R13_SAVINGS;
		private String R13_NOTICE_0_31_DAYS;
		private String R13_NOTICE_32_88_DAYS;
		private String R13_91_DEPOSIT_DAY;
		private String R13_FD_1_6_MONTHS;
		private String R13_FD_7_12_MONTHS;
		private String R13_FD_13_18_MONTHS;
		private String R13_FD_19_24_MONTHS;
		private String R13_FD_OVER_24_MONTHS;
		private String R13_TOTAL;
		private String R14_CURRENCY;
		private String R14_CURRENT;
		private String R14_CALL;
		private String R14_SAVINGS;
		private String R14_NOTICE_0_31_DAYS;
		private String R14_NOTICE_32_88_DAYS;
		private String R14_91_DEPOSIT_DAY;
		private String R14_FD_1_6_MONTHS;
		private String R14_FD_7_12_MONTHS;
		private String R14_FD_13_18_MONTHS;
		private String R14_FD_19_24_MONTHS;
		private String R14_FD_OVER_24_MONTHS;
		private String R14_TOTAL;
		private String R15_CURRENCY;
		private String R15_CURRENT;
		private String R15_CALL;
		private String R15_SAVINGS;
		private String R15_NOTICE_0_31_DAYS;
		private String R15_NOTICE_32_88_DAYS;
		private String R15_91_DEPOSIT_DAY;
		private String R15_FD_1_6_MONTHS;
		private String R15_FD_7_12_MONTHS;
		private String R15_FD_13_18_MONTHS;
		private String R15_FD_19_24_MONTHS;
		private String R15_FD_OVER_24_MONTHS;
		private String R15_TOTAL;

		private Date reportDate;
		private BigDecimal reportVersion;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_CURRENCY() { return R10_CURRENCY; }
		public void setR10_CURRENCY(String r10_CURRENCY) { R10_CURRENCY = r10_CURRENCY; }
		public String getR10_CURRENT() { return R10_CURRENT; }
		public void setR10_CURRENT(String r10_CURRENT) { R10_CURRENT = r10_CURRENT; }
		public String getR10_CALL() { return R10_CALL; }
		public void setR10_CALL(String r10_CALL) { R10_CALL = r10_CALL; }
		public String getR10_SAVINGS() { return R10_SAVINGS; }
		public void setR10_SAVINGS(String r10_SAVINGS) { R10_SAVINGS = r10_SAVINGS; }
		public String getR10_NOTICE_0_31_DAYS() { return R10_NOTICE_0_31_DAYS; }
		public void setR10_NOTICE_0_31_DAYS(String r10_NOTICE_0_31_DAYS) { R10_NOTICE_0_31_DAYS = r10_NOTICE_0_31_DAYS; }
		public String getR10_NOTICE_32_88_DAYS() { return R10_NOTICE_32_88_DAYS; }
		public void setR10_NOTICE_32_88_DAYS(String r10_NOTICE_32_88_DAYS) { R10_NOTICE_32_88_DAYS = r10_NOTICE_32_88_DAYS; }
		public String getR10_91_DEPOSIT_DAY() { return R10_91_DEPOSIT_DAY; }
		public void setR10_91_DEPOSIT_DAY(String r10_91_DEPOSIT_DAY) { R10_91_DEPOSIT_DAY = r10_91_DEPOSIT_DAY; }
		public String getR10_FD_1_6_MONTHS() { return R10_FD_1_6_MONTHS; }
		public void setR10_FD_1_6_MONTHS(String r10_FD_1_6_MONTHS) { R10_FD_1_6_MONTHS = r10_FD_1_6_MONTHS; }
		public String getR10_FD_7_12_MONTHS() { return R10_FD_7_12_MONTHS; }
		public void setR10_FD_7_12_MONTHS(String r10_FD_7_12_MONTHS) { R10_FD_7_12_MONTHS = r10_FD_7_12_MONTHS; }
		public String getR10_FD_13_18_MONTHS() { return R10_FD_13_18_MONTHS; }
		public void setR10_FD_13_18_MONTHS(String r10_FD_13_18_MONTHS) { R10_FD_13_18_MONTHS = r10_FD_13_18_MONTHS; }
		public String getR10_FD_19_24_MONTHS() { return R10_FD_19_24_MONTHS; }
		public void setR10_FD_19_24_MONTHS(String r10_FD_19_24_MONTHS) { R10_FD_19_24_MONTHS = r10_FD_19_24_MONTHS; }
		public String getR10_FD_OVER_24_MONTHS() { return R10_FD_OVER_24_MONTHS; }
		public void setR10_FD_OVER_24_MONTHS(String r10_FD_OVER_24_MONTHS) { R10_FD_OVER_24_MONTHS = r10_FD_OVER_24_MONTHS; }
		public String getR10_TOTAL() { return R10_TOTAL; }
		public void setR10_TOTAL(String r10_TOTAL) { R10_TOTAL = r10_TOTAL; }
		public String getR11_CURRENCY() { return R11_CURRENCY; }
		public void setR11_CURRENCY(String r11_CURRENCY) { R11_CURRENCY = r11_CURRENCY; }
		public String getR11_CURRENT() { return R11_CURRENT; }
		public void setR11_CURRENT(String r11_CURRENT) { R11_CURRENT = r11_CURRENT; }
		public String getR11_CALL() { return R11_CALL; }
		public void setR11_CALL(String r11_CALL) { R11_CALL = r11_CALL; }
		public String getR11_SAVINGS() { return R11_SAVINGS; }
		public void setR11_SAVINGS(String r11_SAVINGS) { R11_SAVINGS = r11_SAVINGS; }
		public String getR11_NOTICE_0_31_DAYS() { return R11_NOTICE_0_31_DAYS; }
		public void setR11_NOTICE_0_31_DAYS(String r11_NOTICE_0_31_DAYS) { R11_NOTICE_0_31_DAYS = r11_NOTICE_0_31_DAYS; }
		public String getR11_NOTICE_32_88_DAYS() { return R11_NOTICE_32_88_DAYS; }
		public void setR11_NOTICE_32_88_DAYS(String r11_NOTICE_32_88_DAYS) { R11_NOTICE_32_88_DAYS = r11_NOTICE_32_88_DAYS; }
		public String getR11_91_DEPOSIT_DAY() { return R11_91_DEPOSIT_DAY; }
		public void setR11_91_DEPOSIT_DAY(String r11_91_DEPOSIT_DAY) { R11_91_DEPOSIT_DAY = r11_91_DEPOSIT_DAY; }
		public String getR11_FD_1_6_MONTHS() { return R11_FD_1_6_MONTHS; }
		public void setR11_FD_1_6_MONTHS(String r11_FD_1_6_MONTHS) { R11_FD_1_6_MONTHS = r11_FD_1_6_MONTHS; }
		public String getR11_FD_7_12_MONTHS() { return R11_FD_7_12_MONTHS; }
		public void setR11_FD_7_12_MONTHS(String r11_FD_7_12_MONTHS) { R11_FD_7_12_MONTHS = r11_FD_7_12_MONTHS; }
		public String getR11_FD_13_18_MONTHS() { return R11_FD_13_18_MONTHS; }
		public void setR11_FD_13_18_MONTHS(String r11_FD_13_18_MONTHS) { R11_FD_13_18_MONTHS = r11_FD_13_18_MONTHS; }
		public String getR11_FD_19_24_MONTHS() { return R11_FD_19_24_MONTHS; }
		public void setR11_FD_19_24_MONTHS(String r11_FD_19_24_MONTHS) { R11_FD_19_24_MONTHS = r11_FD_19_24_MONTHS; }
		public String getR11_FD_OVER_24_MONTHS() { return R11_FD_OVER_24_MONTHS; }
		public void setR11_FD_OVER_24_MONTHS(String r11_FD_OVER_24_MONTHS) { R11_FD_OVER_24_MONTHS = r11_FD_OVER_24_MONTHS; }
		public String getR11_TOTAL() { return R11_TOTAL; }
		public void setR11_TOTAL(String r11_TOTAL) { R11_TOTAL = r11_TOTAL; }
		public String getR12_CURRENCY() { return R12_CURRENCY; }
		public void setR12_CURRENCY(String r12_CURRENCY) { R12_CURRENCY = r12_CURRENCY; }
		public String getR12_CURRENT() { return R12_CURRENT; }
		public void setR12_CURRENT(String r12_CURRENT) { R12_CURRENT = r12_CURRENT; }
		public String getR12_CALL() { return R12_CALL; }
		public void setR12_CALL(String r12_CALL) { R12_CALL = r12_CALL; }
		public String getR12_SAVINGS() { return R12_SAVINGS; }
		public void setR12_SAVINGS(String r12_SAVINGS) { R12_SAVINGS = r12_SAVINGS; }
		public String getR12_NOTICE_0_31_DAYS() { return R12_NOTICE_0_31_DAYS; }
		public void setR12_NOTICE_0_31_DAYS(String r12_NOTICE_0_31_DAYS) { R12_NOTICE_0_31_DAYS = r12_NOTICE_0_31_DAYS; }
		public String getR12_NOTICE_32_88_DAYS() { return R12_NOTICE_32_88_DAYS; }
		public void setR12_NOTICE_32_88_DAYS(String r12_NOTICE_32_88_DAYS) { R12_NOTICE_32_88_DAYS = r12_NOTICE_32_88_DAYS; }
		public String getR12_91_DEPOSIT_DAY() { return R12_91_DEPOSIT_DAY; }
		public void setR12_91_DEPOSIT_DAY(String r12_91_DEPOSIT_DAY) { R12_91_DEPOSIT_DAY = r12_91_DEPOSIT_DAY; }
		public String getR12_FD_1_6_MONTHS() { return R12_FD_1_6_MONTHS; }
		public void setR12_FD_1_6_MONTHS(String r12_FD_1_6_MONTHS) { R12_FD_1_6_MONTHS = r12_FD_1_6_MONTHS; }
		public String getR12_FD_7_12_MONTHS() { return R12_FD_7_12_MONTHS; }
		public void setR12_FD_7_12_MONTHS(String r12_FD_7_12_MONTHS) { R12_FD_7_12_MONTHS = r12_FD_7_12_MONTHS; }
		public String getR12_FD_13_18_MONTHS() { return R12_FD_13_18_MONTHS; }
		public void setR12_FD_13_18_MONTHS(String r12_FD_13_18_MONTHS) { R12_FD_13_18_MONTHS = r12_FD_13_18_MONTHS; }
		public String getR12_FD_19_24_MONTHS() { return R12_FD_19_24_MONTHS; }
		public void setR12_FD_19_24_MONTHS(String r12_FD_19_24_MONTHS) { R12_FD_19_24_MONTHS = r12_FD_19_24_MONTHS; }
		public String getR12_FD_OVER_24_MONTHS() { return R12_FD_OVER_24_MONTHS; }
		public void setR12_FD_OVER_24_MONTHS(String r12_FD_OVER_24_MONTHS) { R12_FD_OVER_24_MONTHS = r12_FD_OVER_24_MONTHS; }
		public String getR12_TOTAL() { return R12_TOTAL; }
		public void setR12_TOTAL(String r12_TOTAL) { R12_TOTAL = r12_TOTAL; }
		public String getR13_CURRENCY() { return R13_CURRENCY; }
		public void setR13_CURRENCY(String r13_CURRENCY) { R13_CURRENCY = r13_CURRENCY; }
		public String getR13_CURRENT() { return R13_CURRENT; }
		public void setR13_CURRENT(String r13_CURRENT) { R13_CURRENT = r13_CURRENT; }
		public String getR13_CALL() { return R13_CALL; }
		public void setR13_CALL(String r13_CALL) { R13_CALL = r13_CALL; }
		public String getR13_SAVINGS() { return R13_SAVINGS; }
		public void setR13_SAVINGS(String r13_SAVINGS) { R13_SAVINGS = r13_SAVINGS; }
		public String getR13_NOTICE_0_31_DAYS() { return R13_NOTICE_0_31_DAYS; }
		public void setR13_NOTICE_0_31_DAYS(String r13_NOTICE_0_31_DAYS) { R13_NOTICE_0_31_DAYS = r13_NOTICE_0_31_DAYS; }
		public String getR13_NOTICE_32_88_DAYS() { return R13_NOTICE_32_88_DAYS; }
		public void setR13_NOTICE_32_88_DAYS(String r13_NOTICE_32_88_DAYS) { R13_NOTICE_32_88_DAYS = r13_NOTICE_32_88_DAYS; }
		public String getR13_91_DEPOSIT_DAY() { return R13_91_DEPOSIT_DAY; }
		public void setR13_91_DEPOSIT_DAY(String r13_91_DEPOSIT_DAY) { R13_91_DEPOSIT_DAY = r13_91_DEPOSIT_DAY; }
		public String getR13_FD_1_6_MONTHS() { return R13_FD_1_6_MONTHS; }
		public void setR13_FD_1_6_MONTHS(String r13_FD_1_6_MONTHS) { R13_FD_1_6_MONTHS = r13_FD_1_6_MONTHS; }
		public String getR13_FD_7_12_MONTHS() { return R13_FD_7_12_MONTHS; }
		public void setR13_FD_7_12_MONTHS(String r13_FD_7_12_MONTHS) { R13_FD_7_12_MONTHS = r13_FD_7_12_MONTHS; }
		public String getR13_FD_13_18_MONTHS() { return R13_FD_13_18_MONTHS; }
		public void setR13_FD_13_18_MONTHS(String r13_FD_13_18_MONTHS) { R13_FD_13_18_MONTHS = r13_FD_13_18_MONTHS; }
		public String getR13_FD_19_24_MONTHS() { return R13_FD_19_24_MONTHS; }
		public void setR13_FD_19_24_MONTHS(String r13_FD_19_24_MONTHS) { R13_FD_19_24_MONTHS = r13_FD_19_24_MONTHS; }
		public String getR13_FD_OVER_24_MONTHS() { return R13_FD_OVER_24_MONTHS; }
		public void setR13_FD_OVER_24_MONTHS(String r13_FD_OVER_24_MONTHS) { R13_FD_OVER_24_MONTHS = r13_FD_OVER_24_MONTHS; }
		public String getR13_TOTAL() { return R13_TOTAL; }
		public void setR13_TOTAL(String r13_TOTAL) { R13_TOTAL = r13_TOTAL; }
		public String getR14_CURRENCY() { return R14_CURRENCY; }
		public void setR14_CURRENCY(String r14_CURRENCY) { R14_CURRENCY = r14_CURRENCY; }
		public String getR14_CURRENT() { return R14_CURRENT; }
		public void setR14_CURRENT(String r14_CURRENT) { R14_CURRENT = r14_CURRENT; }
		public String getR14_CALL() { return R14_CALL; }
		public void setR14_CALL(String r14_CALL) { R14_CALL = r14_CALL; }
		public String getR14_SAVINGS() { return R14_SAVINGS; }
		public void setR14_SAVINGS(String r14_SAVINGS) { R14_SAVINGS = r14_SAVINGS; }
		public String getR14_NOTICE_0_31_DAYS() { return R14_NOTICE_0_31_DAYS; }
		public void setR14_NOTICE_0_31_DAYS(String r14_NOTICE_0_31_DAYS) { R14_NOTICE_0_31_DAYS = r14_NOTICE_0_31_DAYS; }
		public String getR14_NOTICE_32_88_DAYS() { return R14_NOTICE_32_88_DAYS; }
		public void setR14_NOTICE_32_88_DAYS(String r14_NOTICE_32_88_DAYS) { R14_NOTICE_32_88_DAYS = r14_NOTICE_32_88_DAYS; }
		public String getR14_91_DEPOSIT_DAY() { return R14_91_DEPOSIT_DAY; }
		public void setR14_91_DEPOSIT_DAY(String r14_91_DEPOSIT_DAY) { R14_91_DEPOSIT_DAY = r14_91_DEPOSIT_DAY; }
		public String getR14_FD_1_6_MONTHS() { return R14_FD_1_6_MONTHS; }
		public void setR14_FD_1_6_MONTHS(String r14_FD_1_6_MONTHS) { R14_FD_1_6_MONTHS = r14_FD_1_6_MONTHS; }
		public String getR14_FD_7_12_MONTHS() { return R14_FD_7_12_MONTHS; }
		public void setR14_FD_7_12_MONTHS(String r14_FD_7_12_MONTHS) { R14_FD_7_12_MONTHS = r14_FD_7_12_MONTHS; }
		public String getR14_FD_13_18_MONTHS() { return R14_FD_13_18_MONTHS; }
		public void setR14_FD_13_18_MONTHS(String r14_FD_13_18_MONTHS) { R14_FD_13_18_MONTHS = r14_FD_13_18_MONTHS; }
		public String getR14_FD_19_24_MONTHS() { return R14_FD_19_24_MONTHS; }
		public void setR14_FD_19_24_MONTHS(String r14_FD_19_24_MONTHS) { R14_FD_19_24_MONTHS = r14_FD_19_24_MONTHS; }
		public String getR14_FD_OVER_24_MONTHS() { return R14_FD_OVER_24_MONTHS; }
		public void setR14_FD_OVER_24_MONTHS(String r14_FD_OVER_24_MONTHS) { R14_FD_OVER_24_MONTHS = r14_FD_OVER_24_MONTHS; }
		public String getR14_TOTAL() { return R14_TOTAL; }
		public void setR14_TOTAL(String r14_TOTAL) { R14_TOTAL = r14_TOTAL; }
		public String getR15_CURRENCY() { return R15_CURRENCY; }
		public void setR15_CURRENCY(String r15_CURRENCY) { R15_CURRENCY = r15_CURRENCY; }
		public String getR15_CURRENT() { return R15_CURRENT; }
		public void setR15_CURRENT(String r15_CURRENT) { R15_CURRENT = r15_CURRENT; }
		public String getR15_CALL() { return R15_CALL; }
		public void setR15_CALL(String r15_CALL) { R15_CALL = r15_CALL; }
		public String getR15_SAVINGS() { return R15_SAVINGS; }
		public void setR15_SAVINGS(String r15_SAVINGS) { R15_SAVINGS = r15_SAVINGS; }
		public String getR15_NOTICE_0_31_DAYS() { return R15_NOTICE_0_31_DAYS; }
		public void setR15_NOTICE_0_31_DAYS(String r15_NOTICE_0_31_DAYS) { R15_NOTICE_0_31_DAYS = r15_NOTICE_0_31_DAYS; }
		public String getR15_NOTICE_32_88_DAYS() { return R15_NOTICE_32_88_DAYS; }
		public void setR15_NOTICE_32_88_DAYS(String r15_NOTICE_32_88_DAYS) { R15_NOTICE_32_88_DAYS = r15_NOTICE_32_88_DAYS; }
		public String getR15_91_DEPOSIT_DAY() { return R15_91_DEPOSIT_DAY; }
		public void setR15_91_DEPOSIT_DAY(String r15_91_DEPOSIT_DAY) { R15_91_DEPOSIT_DAY = r15_91_DEPOSIT_DAY; }
		public String getR15_FD_1_6_MONTHS() { return R15_FD_1_6_MONTHS; }
		public void setR15_FD_1_6_MONTHS(String r15_FD_1_6_MONTHS) { R15_FD_1_6_MONTHS = r15_FD_1_6_MONTHS; }
		public String getR15_FD_7_12_MONTHS() { return R15_FD_7_12_MONTHS; }
		public void setR15_FD_7_12_MONTHS(String r15_FD_7_12_MONTHS) { R15_FD_7_12_MONTHS = r15_FD_7_12_MONTHS; }
		public String getR15_FD_13_18_MONTHS() { return R15_FD_13_18_MONTHS; }
		public void setR15_FD_13_18_MONTHS(String r15_FD_13_18_MONTHS) { R15_FD_13_18_MONTHS = r15_FD_13_18_MONTHS; }
		public String getR15_FD_19_24_MONTHS() { return R15_FD_19_24_MONTHS; }
		public void setR15_FD_19_24_MONTHS(String r15_FD_19_24_MONTHS) { R15_FD_19_24_MONTHS = r15_FD_19_24_MONTHS; }
		public String getR15_FD_OVER_24_MONTHS() { return R15_FD_OVER_24_MONTHS; }
		public void setR15_FD_OVER_24_MONTHS(String r15_FD_OVER_24_MONTHS) { R15_FD_OVER_24_MONTHS = r15_FD_OVER_24_MONTHS; }
		public String getR15_TOTAL() { return R15_TOTAL; }
		public void setR15_TOTAL(String r15_TOTAL) { R15_TOTAL = r15_TOTAL; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getReport_frequency() { return report_frequency; }
		public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
		public String getReport_code() { return report_code; }
		public void setReport_code(String report_code) { this.report_code = report_code; }
		public String getReport_desc() { return report_desc; }
		public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
		public String getEntity_flg() { return entity_flg; }
		public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
		public String getModify_flg() { return modify_flg; }
		public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
		public String getDel_flg() { return del_flg; }
		public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	}

	// =========================================================
	// Inner entity: M_INT_RATES_FCA_Detail_Entity
	// =========================================================
	public static class M_INT_RATES_FCA_Detail_Entity {
		private String R10_CURRENCY;
		private String R10_CURRENT;
		private String R10_CALL;
		private String R10_SAVINGS;
		private String R10_NOTICE_0_31_DAYS;
		private String R10_NOTICE_32_88_DAYS;
		private String R10_91_DEPOSIT_DAY;
		private String R10_FD_1_6_MONTHS;
		private String R10_FD_7_12_MONTHS;
		private String R10_FD_13_18_MONTHS;
		private String R10_FD_19_24_MONTHS;
		private String R10_FD_OVER_24_MONTHS;
		private String R10_TOTAL;
		private String R11_CURRENCY;
		private String R11_CURRENT;
		private String R11_CALL;
		private String R11_SAVINGS;
		private String R11_NOTICE_0_31_DAYS;
		private String R11_NOTICE_32_88_DAYS;
		private String R11_91_DEPOSIT_DAY;
		private String R11_FD_1_6_MONTHS;
		private String R11_FD_7_12_MONTHS;
		private String R11_FD_13_18_MONTHS;
		private String R11_FD_19_24_MONTHS;
		private String R11_FD_OVER_24_MONTHS;
		private String R11_TOTAL;
		private String R12_CURRENCY;
		private String R12_CURRENT;
		private String R12_CALL;
		private String R12_SAVINGS;
		private String R12_NOTICE_0_31_DAYS;
		private String R12_NOTICE_32_88_DAYS;
		private String R12_91_DEPOSIT_DAY;
		private String R12_FD_1_6_MONTHS;
		private String R12_FD_7_12_MONTHS;
		private String R12_FD_13_18_MONTHS;
		private String R12_FD_19_24_MONTHS;
		private String R12_FD_OVER_24_MONTHS;
		private String R12_TOTAL;
		private String R13_CURRENCY;
		private String R13_CURRENT;
		private String R13_CALL;
		private String R13_SAVINGS;
		private String R13_NOTICE_0_31_DAYS;
		private String R13_NOTICE_32_88_DAYS;
		private String R13_91_DEPOSIT_DAY;
		private String R13_FD_1_6_MONTHS;
		private String R13_FD_7_12_MONTHS;
		private String R13_FD_13_18_MONTHS;
		private String R13_FD_19_24_MONTHS;
		private String R13_FD_OVER_24_MONTHS;
		private String R13_TOTAL;
		private String R14_CURRENCY;
		private String R14_CURRENT;
		private String R14_CALL;
		private String R14_SAVINGS;
		private String R14_NOTICE_0_31_DAYS;
		private String R14_NOTICE_32_88_DAYS;
		private String R14_91_DEPOSIT_DAY;
		private String R14_FD_1_6_MONTHS;
		private String R14_FD_7_12_MONTHS;
		private String R14_FD_13_18_MONTHS;
		private String R14_FD_19_24_MONTHS;
		private String R14_FD_OVER_24_MONTHS;
		private String R14_TOTAL;
		private String R15_CURRENCY;
		private String R15_CURRENT;
		private String R15_CALL;
		private String R15_SAVINGS;
		private String R15_NOTICE_0_31_DAYS;
		private String R15_NOTICE_32_88_DAYS;
		private String R15_91_DEPOSIT_DAY;
		private String R15_FD_1_6_MONTHS;
		private String R15_FD_7_12_MONTHS;
		private String R15_FD_13_18_MONTHS;
		private String R15_FD_19_24_MONTHS;
		private String R15_FD_OVER_24_MONTHS;
		private String R15_TOTAL;

		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_CURRENCY() { return R10_CURRENCY; }
		public void setR10_CURRENCY(String r10_CURRENCY) { R10_CURRENCY = r10_CURRENCY; }
		public String getR10_CURRENT() { return R10_CURRENT; }
		public void setR10_CURRENT(String r10_CURRENT) { R10_CURRENT = r10_CURRENT; }
		public String getR10_CALL() { return R10_CALL; }
		public void setR10_CALL(String r10_CALL) { R10_CALL = r10_CALL; }
		public String getR10_SAVINGS() { return R10_SAVINGS; }
		public void setR10_SAVINGS(String r10_SAVINGS) { R10_SAVINGS = r10_SAVINGS; }
		public String getR10_NOTICE_0_31_DAYS() { return R10_NOTICE_0_31_DAYS; }
		public void setR10_NOTICE_0_31_DAYS(String r10_NOTICE_0_31_DAYS) { R10_NOTICE_0_31_DAYS = r10_NOTICE_0_31_DAYS; }
		public String getR10_NOTICE_32_88_DAYS() { return R10_NOTICE_32_88_DAYS; }
		public void setR10_NOTICE_32_88_DAYS(String r10_NOTICE_32_88_DAYS) { R10_NOTICE_32_88_DAYS = r10_NOTICE_32_88_DAYS; }
		public String getR10_91_DEPOSIT_DAY() { return R10_91_DEPOSIT_DAY; }
		public void setR10_91_DEPOSIT_DAY(String r10_91_DEPOSIT_DAY) { R10_91_DEPOSIT_DAY = r10_91_DEPOSIT_DAY; }
		public String getR10_FD_1_6_MONTHS() { return R10_FD_1_6_MONTHS; }
		public void setR10_FD_1_6_MONTHS(String r10_FD_1_6_MONTHS) { R10_FD_1_6_MONTHS = r10_FD_1_6_MONTHS; }
		public String getR10_FD_7_12_MONTHS() { return R10_FD_7_12_MONTHS; }
		public void setR10_FD_7_12_MONTHS(String r10_FD_7_12_MONTHS) { R10_FD_7_12_MONTHS = r10_FD_7_12_MONTHS; }
		public String getR10_FD_13_18_MONTHS() { return R10_FD_13_18_MONTHS; }
		public void setR10_FD_13_18_MONTHS(String r10_FD_13_18_MONTHS) { R10_FD_13_18_MONTHS = r10_FD_13_18_MONTHS; }
		public String getR10_FD_19_24_MONTHS() { return R10_FD_19_24_MONTHS; }
		public void setR10_FD_19_24_MONTHS(String r10_FD_19_24_MONTHS) { R10_FD_19_24_MONTHS = r10_FD_19_24_MONTHS; }
		public String getR10_FD_OVER_24_MONTHS() { return R10_FD_OVER_24_MONTHS; }
		public void setR10_FD_OVER_24_MONTHS(String r10_FD_OVER_24_MONTHS) { R10_FD_OVER_24_MONTHS = r10_FD_OVER_24_MONTHS; }
		public String getR10_TOTAL() { return R10_TOTAL; }
		public void setR10_TOTAL(String r10_TOTAL) { R10_TOTAL = r10_TOTAL; }
		public String getR11_CURRENCY() { return R11_CURRENCY; }
		public void setR11_CURRENCY(String r11_CURRENCY) { R11_CURRENCY = r11_CURRENCY; }
		public String getR11_CURRENT() { return R11_CURRENT; }
		public void setR11_CURRENT(String r11_CURRENT) { R11_CURRENT = r11_CURRENT; }
		public String getR11_CALL() { return R11_CALL; }
		public void setR11_CALL(String r11_CALL) { R11_CALL = r11_CALL; }
		public String getR11_SAVINGS() { return R11_SAVINGS; }
		public void setR11_SAVINGS(String r11_SAVINGS) { R11_SAVINGS = r11_SAVINGS; }
		public String getR11_NOTICE_0_31_DAYS() { return R11_NOTICE_0_31_DAYS; }
		public void setR11_NOTICE_0_31_DAYS(String r11_NOTICE_0_31_DAYS) { R11_NOTICE_0_31_DAYS = r11_NOTICE_0_31_DAYS; }
		public String getR11_NOTICE_32_88_DAYS() { return R11_NOTICE_32_88_DAYS; }
		public void setR11_NOTICE_32_88_DAYS(String r11_NOTICE_32_88_DAYS) { R11_NOTICE_32_88_DAYS = r11_NOTICE_32_88_DAYS; }
		public String getR11_91_DEPOSIT_DAY() { return R11_91_DEPOSIT_DAY; }
		public void setR11_91_DEPOSIT_DAY(String r11_91_DEPOSIT_DAY) { R11_91_DEPOSIT_DAY = r11_91_DEPOSIT_DAY; }
		public String getR11_FD_1_6_MONTHS() { return R11_FD_1_6_MONTHS; }
		public void setR11_FD_1_6_MONTHS(String r11_FD_1_6_MONTHS) { R11_FD_1_6_MONTHS = r11_FD_1_6_MONTHS; }
		public String getR11_FD_7_12_MONTHS() { return R11_FD_7_12_MONTHS; }
		public void setR11_FD_7_12_MONTHS(String r11_FD_7_12_MONTHS) { R11_FD_7_12_MONTHS = r11_FD_7_12_MONTHS; }
		public String getR11_FD_13_18_MONTHS() { return R11_FD_13_18_MONTHS; }
		public void setR11_FD_13_18_MONTHS(String r11_FD_13_18_MONTHS) { R11_FD_13_18_MONTHS = r11_FD_13_18_MONTHS; }
		public String getR11_FD_19_24_MONTHS() { return R11_FD_19_24_MONTHS; }
		public void setR11_FD_19_24_MONTHS(String r11_FD_19_24_MONTHS) { R11_FD_19_24_MONTHS = r11_FD_19_24_MONTHS; }
		public String getR11_FD_OVER_24_MONTHS() { return R11_FD_OVER_24_MONTHS; }
		public void setR11_FD_OVER_24_MONTHS(String r11_FD_OVER_24_MONTHS) { R11_FD_OVER_24_MONTHS = r11_FD_OVER_24_MONTHS; }
		public String getR11_TOTAL() { return R11_TOTAL; }
		public void setR11_TOTAL(String r11_TOTAL) { R11_TOTAL = r11_TOTAL; }
		public String getR12_CURRENCY() { return R12_CURRENCY; }
		public void setR12_CURRENCY(String r12_CURRENCY) { R12_CURRENCY = r12_CURRENCY; }
		public String getR12_CURRENT() { return R12_CURRENT; }
		public void setR12_CURRENT(String r12_CURRENT) { R12_CURRENT = r12_CURRENT; }
		public String getR12_CALL() { return R12_CALL; }
		public void setR12_CALL(String r12_CALL) { R12_CALL = r12_CALL; }
		public String getR12_SAVINGS() { return R12_SAVINGS; }
		public void setR12_SAVINGS(String r12_SAVINGS) { R12_SAVINGS = r12_SAVINGS; }
		public String getR12_NOTICE_0_31_DAYS() { return R12_NOTICE_0_31_DAYS; }
		public void setR12_NOTICE_0_31_DAYS(String r12_NOTICE_0_31_DAYS) { R12_NOTICE_0_31_DAYS = r12_NOTICE_0_31_DAYS; }
		public String getR12_NOTICE_32_88_DAYS() { return R12_NOTICE_32_88_DAYS; }
		public void setR12_NOTICE_32_88_DAYS(String r12_NOTICE_32_88_DAYS) { R12_NOTICE_32_88_DAYS = r12_NOTICE_32_88_DAYS; }
		public String getR12_91_DEPOSIT_DAY() { return R12_91_DEPOSIT_DAY; }
		public void setR12_91_DEPOSIT_DAY(String r12_91_DEPOSIT_DAY) { R12_91_DEPOSIT_DAY = r12_91_DEPOSIT_DAY; }
		public String getR12_FD_1_6_MONTHS() { return R12_FD_1_6_MONTHS; }
		public void setR12_FD_1_6_MONTHS(String r12_FD_1_6_MONTHS) { R12_FD_1_6_MONTHS = r12_FD_1_6_MONTHS; }
		public String getR12_FD_7_12_MONTHS() { return R12_FD_7_12_MONTHS; }
		public void setR12_FD_7_12_MONTHS(String r12_FD_7_12_MONTHS) { R12_FD_7_12_MONTHS = r12_FD_7_12_MONTHS; }
		public String getR12_FD_13_18_MONTHS() { return R12_FD_13_18_MONTHS; }
		public void setR12_FD_13_18_MONTHS(String r12_FD_13_18_MONTHS) { R12_FD_13_18_MONTHS = r12_FD_13_18_MONTHS; }
		public String getR12_FD_19_24_MONTHS() { return R12_FD_19_24_MONTHS; }
		public void setR12_FD_19_24_MONTHS(String r12_FD_19_24_MONTHS) { R12_FD_19_24_MONTHS = r12_FD_19_24_MONTHS; }
		public String getR12_FD_OVER_24_MONTHS() { return R12_FD_OVER_24_MONTHS; }
		public void setR12_FD_OVER_24_MONTHS(String r12_FD_OVER_24_MONTHS) { R12_FD_OVER_24_MONTHS = r12_FD_OVER_24_MONTHS; }
		public String getR12_TOTAL() { return R12_TOTAL; }
		public void setR12_TOTAL(String r12_TOTAL) { R12_TOTAL = r12_TOTAL; }
		public String getR13_CURRENCY() { return R13_CURRENCY; }
		public void setR13_CURRENCY(String r13_CURRENCY) { R13_CURRENCY = r13_CURRENCY; }
		public String getR13_CURRENT() { return R13_CURRENT; }
		public void setR13_CURRENT(String r13_CURRENT) { R13_CURRENT = r13_CURRENT; }
		public String getR13_CALL() { return R13_CALL; }
		public void setR13_CALL(String r13_CALL) { R13_CALL = r13_CALL; }
		public String getR13_SAVINGS() { return R13_SAVINGS; }
		public void setR13_SAVINGS(String r13_SAVINGS) { R13_SAVINGS = r13_SAVINGS; }
		public String getR13_NOTICE_0_31_DAYS() { return R13_NOTICE_0_31_DAYS; }
		public void setR13_NOTICE_0_31_DAYS(String r13_NOTICE_0_31_DAYS) { R13_NOTICE_0_31_DAYS = r13_NOTICE_0_31_DAYS; }
		public String getR13_NOTICE_32_88_DAYS() { return R13_NOTICE_32_88_DAYS; }
		public void setR13_NOTICE_32_88_DAYS(String r13_NOTICE_32_88_DAYS) { R13_NOTICE_32_88_DAYS = r13_NOTICE_32_88_DAYS; }
		public String getR13_91_DEPOSIT_DAY() { return R13_91_DEPOSIT_DAY; }
		public void setR13_91_DEPOSIT_DAY(String r13_91_DEPOSIT_DAY) { R13_91_DEPOSIT_DAY = r13_91_DEPOSIT_DAY; }
		public String getR13_FD_1_6_MONTHS() { return R13_FD_1_6_MONTHS; }
		public void setR13_FD_1_6_MONTHS(String r13_FD_1_6_MONTHS) { R13_FD_1_6_MONTHS = r13_FD_1_6_MONTHS; }
		public String getR13_FD_7_12_MONTHS() { return R13_FD_7_12_MONTHS; }
		public void setR13_FD_7_12_MONTHS(String r13_FD_7_12_MONTHS) { R13_FD_7_12_MONTHS = r13_FD_7_12_MONTHS; }
		public String getR13_FD_13_18_MONTHS() { return R13_FD_13_18_MONTHS; }
		public void setR13_FD_13_18_MONTHS(String r13_FD_13_18_MONTHS) { R13_FD_13_18_MONTHS = r13_FD_13_18_MONTHS; }
		public String getR13_FD_19_24_MONTHS() { return R13_FD_19_24_MONTHS; }
		public void setR13_FD_19_24_MONTHS(String r13_FD_19_24_MONTHS) { R13_FD_19_24_MONTHS = r13_FD_19_24_MONTHS; }
		public String getR13_FD_OVER_24_MONTHS() { return R13_FD_OVER_24_MONTHS; }
		public void setR13_FD_OVER_24_MONTHS(String r13_FD_OVER_24_MONTHS) { R13_FD_OVER_24_MONTHS = r13_FD_OVER_24_MONTHS; }
		public String getR13_TOTAL() { return R13_TOTAL; }
		public void setR13_TOTAL(String r13_TOTAL) { R13_TOTAL = r13_TOTAL; }
		public String getR14_CURRENCY() { return R14_CURRENCY; }
		public void setR14_CURRENCY(String r14_CURRENCY) { R14_CURRENCY = r14_CURRENCY; }
		public String getR14_CURRENT() { return R14_CURRENT; }
		public void setR14_CURRENT(String r14_CURRENT) { R14_CURRENT = r14_CURRENT; }
		public String getR14_CALL() { return R14_CALL; }
		public void setR14_CALL(String r14_CALL) { R14_CALL = r14_CALL; }
		public String getR14_SAVINGS() { return R14_SAVINGS; }
		public void setR14_SAVINGS(String r14_SAVINGS) { R14_SAVINGS = r14_SAVINGS; }
		public String getR14_NOTICE_0_31_DAYS() { return R14_NOTICE_0_31_DAYS; }
		public void setR14_NOTICE_0_31_DAYS(String r14_NOTICE_0_31_DAYS) { R14_NOTICE_0_31_DAYS = r14_NOTICE_0_31_DAYS; }
		public String getR14_NOTICE_32_88_DAYS() { return R14_NOTICE_32_88_DAYS; }
		public void setR14_NOTICE_32_88_DAYS(String r14_NOTICE_32_88_DAYS) { R14_NOTICE_32_88_DAYS = r14_NOTICE_32_88_DAYS; }
		public String getR14_91_DEPOSIT_DAY() { return R14_91_DEPOSIT_DAY; }
		public void setR14_91_DEPOSIT_DAY(String r14_91_DEPOSIT_DAY) { R14_91_DEPOSIT_DAY = r14_91_DEPOSIT_DAY; }
		public String getR14_FD_1_6_MONTHS() { return R14_FD_1_6_MONTHS; }
		public void setR14_FD_1_6_MONTHS(String r14_FD_1_6_MONTHS) { R14_FD_1_6_MONTHS = r14_FD_1_6_MONTHS; }
		public String getR14_FD_7_12_MONTHS() { return R14_FD_7_12_MONTHS; }
		public void setR14_FD_7_12_MONTHS(String r14_FD_7_12_MONTHS) { R14_FD_7_12_MONTHS = r14_FD_7_12_MONTHS; }
		public String getR14_FD_13_18_MONTHS() { return R14_FD_13_18_MONTHS; }
		public void setR14_FD_13_18_MONTHS(String r14_FD_13_18_MONTHS) { R14_FD_13_18_MONTHS = r14_FD_13_18_MONTHS; }
		public String getR14_FD_19_24_MONTHS() { return R14_FD_19_24_MONTHS; }
		public void setR14_FD_19_24_MONTHS(String r14_FD_19_24_MONTHS) { R14_FD_19_24_MONTHS = r14_FD_19_24_MONTHS; }
		public String getR14_FD_OVER_24_MONTHS() { return R14_FD_OVER_24_MONTHS; }
		public void setR14_FD_OVER_24_MONTHS(String r14_FD_OVER_24_MONTHS) { R14_FD_OVER_24_MONTHS = r14_FD_OVER_24_MONTHS; }
		public String getR14_TOTAL() { return R14_TOTAL; }
		public void setR14_TOTAL(String r14_TOTAL) { R14_TOTAL = r14_TOTAL; }
		public String getR15_CURRENCY() { return R15_CURRENCY; }
		public void setR15_CURRENCY(String r15_CURRENCY) { R15_CURRENCY = r15_CURRENCY; }
		public String getR15_CURRENT() { return R15_CURRENT; }
		public void setR15_CURRENT(String r15_CURRENT) { R15_CURRENT = r15_CURRENT; }
		public String getR15_CALL() { return R15_CALL; }
		public void setR15_CALL(String r15_CALL) { R15_CALL = r15_CALL; }
		public String getR15_SAVINGS() { return R15_SAVINGS; }
		public void setR15_SAVINGS(String r15_SAVINGS) { R15_SAVINGS = r15_SAVINGS; }
		public String getR15_NOTICE_0_31_DAYS() { return R15_NOTICE_0_31_DAYS; }
		public void setR15_NOTICE_0_31_DAYS(String r15_NOTICE_0_31_DAYS) { R15_NOTICE_0_31_DAYS = r15_NOTICE_0_31_DAYS; }
		public String getR15_NOTICE_32_88_DAYS() { return R15_NOTICE_32_88_DAYS; }
		public void setR15_NOTICE_32_88_DAYS(String r15_NOTICE_32_88_DAYS) { R15_NOTICE_32_88_DAYS = r15_NOTICE_32_88_DAYS; }
		public String getR15_91_DEPOSIT_DAY() { return R15_91_DEPOSIT_DAY; }
		public void setR15_91_DEPOSIT_DAY(String r15_91_DEPOSIT_DAY) { R15_91_DEPOSIT_DAY = r15_91_DEPOSIT_DAY; }
		public String getR15_FD_1_6_MONTHS() { return R15_FD_1_6_MONTHS; }
		public void setR15_FD_1_6_MONTHS(String r15_FD_1_6_MONTHS) { R15_FD_1_6_MONTHS = r15_FD_1_6_MONTHS; }
		public String getR15_FD_7_12_MONTHS() { return R15_FD_7_12_MONTHS; }
		public void setR15_FD_7_12_MONTHS(String r15_FD_7_12_MONTHS) { R15_FD_7_12_MONTHS = r15_FD_7_12_MONTHS; }
		public String getR15_FD_13_18_MONTHS() { return R15_FD_13_18_MONTHS; }
		public void setR15_FD_13_18_MONTHS(String r15_FD_13_18_MONTHS) { R15_FD_13_18_MONTHS = r15_FD_13_18_MONTHS; }
		public String getR15_FD_19_24_MONTHS() { return R15_FD_19_24_MONTHS; }
		public void setR15_FD_19_24_MONTHS(String r15_FD_19_24_MONTHS) { R15_FD_19_24_MONTHS = r15_FD_19_24_MONTHS; }
		public String getR15_FD_OVER_24_MONTHS() { return R15_FD_OVER_24_MONTHS; }
		public void setR15_FD_OVER_24_MONTHS(String r15_FD_OVER_24_MONTHS) { R15_FD_OVER_24_MONTHS = r15_FD_OVER_24_MONTHS; }
		public String getR15_TOTAL() { return R15_TOTAL; }
		public void setR15_TOTAL(String r15_TOTAL) { R15_TOTAL = r15_TOTAL; }
		public Date getReport_date() { return report_date; }
		public void setReport_date(Date report_date) { this.report_date = report_date; }
		public BigDecimal getReport_version() { return report_version; }
		public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
		public String getReport_frequency() { return report_frequency; }
		public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
		public String getReport_code() { return report_code; }
		public void setReport_code(String report_code) { this.report_code = report_code; }
		public String getReport_desc() { return report_desc; }
		public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
		public String getEntity_flg() { return entity_flg; }
		public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
		public String getModify_flg() { return modify_flg; }
		public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
		public String getDel_flg() { return del_flg; }
		public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	}

	// =========================================================
	// Inner entity: M_INT_RATES_FCA_Archival_Summary_Entity
	// =========================================================
	public static class M_INT_RATES_FCA_Archival_Summary_Entity {
		private String R10_CURRENCY;
		private String R10_CURRENT;
		private String R10_CALL;
		private String R10_SAVINGS;
		private String R10_NOTICE_0_31_DAYS;
		private String R10_NOTICE_32_88_DAYS;
		private String R10_91_DEPOSIT_DAY;
		private String R10_FD_1_6_MONTHS;
		private String R10_FD_7_12_MONTHS;
		private String R10_FD_13_18_MONTHS;
		private String R10_FD_19_24_MONTHS;
		private String R10_FD_OVER_24_MONTHS;
		private String R10_TOTAL;
		private String R11_CURRENCY;
		private String R11_CURRENT;
		private String R11_CALL;
		private String R11_SAVINGS;
		private String R11_NOTICE_0_31_DAYS;
		private String R11_NOTICE_32_88_DAYS;
		private String R11_91_DEPOSIT_DAY;
		private String R11_FD_1_6_MONTHS;
		private String R11_FD_7_12_MONTHS;
		private String R11_FD_13_18_MONTHS;
		private String R11_FD_19_24_MONTHS;
		private String R11_FD_OVER_24_MONTHS;
		private String R11_TOTAL;
		private String R12_CURRENCY;
		private String R12_CURRENT;
		private String R12_CALL;
		private String R12_SAVINGS;
		private String R12_NOTICE_0_31_DAYS;
		private String R12_NOTICE_32_88_DAYS;
		private String R12_91_DEPOSIT_DAY;
		private String R12_FD_1_6_MONTHS;
		private String R12_FD_7_12_MONTHS;
		private String R12_FD_13_18_MONTHS;
		private String R12_FD_19_24_MONTHS;
		private String R12_FD_OVER_24_MONTHS;
		private String R12_TOTAL;
		private String R13_CURRENCY;
		private String R13_CURRENT;
		private String R13_CALL;
		private String R13_SAVINGS;
		private String R13_NOTICE_0_31_DAYS;
		private String R13_NOTICE_32_88_DAYS;
		private String R13_91_DEPOSIT_DAY;
		private String R13_FD_1_6_MONTHS;
		private String R13_FD_7_12_MONTHS;
		private String R13_FD_13_18_MONTHS;
		private String R13_FD_19_24_MONTHS;
		private String R13_FD_OVER_24_MONTHS;
		private String R13_TOTAL;
		private String R14_CURRENCY;
		private String R14_CURRENT;
		private String R14_CALL;
		private String R14_SAVINGS;
		private String R14_NOTICE_0_31_DAYS;
		private String R14_NOTICE_32_88_DAYS;
		private String R14_91_DEPOSIT_DAY;
		private String R14_FD_1_6_MONTHS;
		private String R14_FD_7_12_MONTHS;
		private String R14_FD_13_18_MONTHS;
		private String R14_FD_19_24_MONTHS;
		private String R14_FD_OVER_24_MONTHS;
		private String R14_TOTAL;
		private String R15_CURRENCY;
		private String R15_CURRENT;
		private String R15_CALL;
		private String R15_SAVINGS;
		private String R15_NOTICE_0_31_DAYS;
		private String R15_NOTICE_32_88_DAYS;
		private String R15_91_DEPOSIT_DAY;
		private String R15_FD_1_6_MONTHS;
		private String R15_FD_7_12_MONTHS;
		private String R15_FD_13_18_MONTHS;
		private String R15_FD_19_24_MONTHS;
		private String R15_FD_OVER_24_MONTHS;
		private String R15_TOTAL;

		private Date report_date;
		private BigDecimal report_version;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_CURRENCY() { return R10_CURRENCY; }
		public void setR10_CURRENCY(String r10_CURRENCY) { R10_CURRENCY = r10_CURRENCY; }
		public String getR10_CURRENT() { return R10_CURRENT; }
		public void setR10_CURRENT(String r10_CURRENT) { R10_CURRENT = r10_CURRENT; }
		public String getR10_CALL() { return R10_CALL; }
		public void setR10_CALL(String r10_CALL) { R10_CALL = r10_CALL; }
		public String getR10_SAVINGS() { return R10_SAVINGS; }
		public void setR10_SAVINGS(String r10_SAVINGS) { R10_SAVINGS = r10_SAVINGS; }
		public String getR10_NOTICE_0_31_DAYS() { return R10_NOTICE_0_31_DAYS; }
		public void setR10_NOTICE_0_31_DAYS(String r10_NOTICE_0_31_DAYS) { R10_NOTICE_0_31_DAYS = r10_NOTICE_0_31_DAYS; }
		public String getR10_NOTICE_32_88_DAYS() { return R10_NOTICE_32_88_DAYS; }
		public void setR10_NOTICE_32_88_DAYS(String r10_NOTICE_32_88_DAYS) { R10_NOTICE_32_88_DAYS = r10_NOTICE_32_88_DAYS; }
		public String getR10_91_DEPOSIT_DAY() { return R10_91_DEPOSIT_DAY; }
		public void setR10_91_DEPOSIT_DAY(String r10_91_DEPOSIT_DAY) { R10_91_DEPOSIT_DAY = r10_91_DEPOSIT_DAY; }
		public String getR10_FD_1_6_MONTHS() { return R10_FD_1_6_MONTHS; }
		public void setR10_FD_1_6_MONTHS(String r10_FD_1_6_MONTHS) { R10_FD_1_6_MONTHS = r10_FD_1_6_MONTHS; }
		public String getR10_FD_7_12_MONTHS() { return R10_FD_7_12_MONTHS; }
		public void setR10_FD_7_12_MONTHS(String r10_FD_7_12_MONTHS) { R10_FD_7_12_MONTHS = r10_FD_7_12_MONTHS; }
		public String getR10_FD_13_18_MONTHS() { return R10_FD_13_18_MONTHS; }
		public void setR10_FD_13_18_MONTHS(String r10_FD_13_18_MONTHS) { R10_FD_13_18_MONTHS = r10_FD_13_18_MONTHS; }
		public String getR10_FD_19_24_MONTHS() { return R10_FD_19_24_MONTHS; }
		public void setR10_FD_19_24_MONTHS(String r10_FD_19_24_MONTHS) { R10_FD_19_24_MONTHS = r10_FD_19_24_MONTHS; }
		public String getR10_FD_OVER_24_MONTHS() { return R10_FD_OVER_24_MONTHS; }
		public void setR10_FD_OVER_24_MONTHS(String r10_FD_OVER_24_MONTHS) { R10_FD_OVER_24_MONTHS = r10_FD_OVER_24_MONTHS; }
		public String getR10_TOTAL() { return R10_TOTAL; }
		public void setR10_TOTAL(String r10_TOTAL) { R10_TOTAL = r10_TOTAL; }
		public String getR11_CURRENCY() { return R11_CURRENCY; }
		public void setR11_CURRENCY(String r11_CURRENCY) { R11_CURRENCY = r11_CURRENCY; }
		public String getR11_CURRENT() { return R11_CURRENT; }
		public void setR11_CURRENT(String r11_CURRENT) { R11_CURRENT = r11_CURRENT; }
		public String getR11_CALL() { return R11_CALL; }
		public void setR11_CALL(String r11_CALL) { R11_CALL = r11_CALL; }
		public String getR11_SAVINGS() { return R11_SAVINGS; }
		public void setR11_SAVINGS(String r11_SAVINGS) { R11_SAVINGS = r11_SAVINGS; }
		public String getR11_NOTICE_0_31_DAYS() { return R11_NOTICE_0_31_DAYS; }
		public void setR11_NOTICE_0_31_DAYS(String r11_NOTICE_0_31_DAYS) { R11_NOTICE_0_31_DAYS = r11_NOTICE_0_31_DAYS; }
		public String getR11_NOTICE_32_88_DAYS() { return R11_NOTICE_32_88_DAYS; }
		public void setR11_NOTICE_32_88_DAYS(String r11_NOTICE_32_88_DAYS) { R11_NOTICE_32_88_DAYS = r11_NOTICE_32_88_DAYS; }
		public String getR11_91_DEPOSIT_DAY() { return R11_91_DEPOSIT_DAY; }
		public void setR11_91_DEPOSIT_DAY(String r11_91_DEPOSIT_DAY) { R11_91_DEPOSIT_DAY = r11_91_DEPOSIT_DAY; }
		public String getR11_FD_1_6_MONTHS() { return R11_FD_1_6_MONTHS; }
		public void setR11_FD_1_6_MONTHS(String r11_FD_1_6_MONTHS) { R11_FD_1_6_MONTHS = r11_FD_1_6_MONTHS; }
		public String getR11_FD_7_12_MONTHS() { return R11_FD_7_12_MONTHS; }
		public void setR11_FD_7_12_MONTHS(String r11_FD_7_12_MONTHS) { R11_FD_7_12_MONTHS = r11_FD_7_12_MONTHS; }
		public String getR11_FD_13_18_MONTHS() { return R11_FD_13_18_MONTHS; }
		public void setR11_FD_13_18_MONTHS(String r11_FD_13_18_MONTHS) { R11_FD_13_18_MONTHS = r11_FD_13_18_MONTHS; }
		public String getR11_FD_19_24_MONTHS() { return R11_FD_19_24_MONTHS; }
		public void setR11_FD_19_24_MONTHS(String r11_FD_19_24_MONTHS) { R11_FD_19_24_MONTHS = r11_FD_19_24_MONTHS; }
		public String getR11_FD_OVER_24_MONTHS() { return R11_FD_OVER_24_MONTHS; }
		public void setR11_FD_OVER_24_MONTHS(String r11_FD_OVER_24_MONTHS) { R11_FD_OVER_24_MONTHS = r11_FD_OVER_24_MONTHS; }
		public String getR11_TOTAL() { return R11_TOTAL; }
		public void setR11_TOTAL(String r11_TOTAL) { R11_TOTAL = r11_TOTAL; }
		public String getR12_CURRENCY() { return R12_CURRENCY; }
		public void setR12_CURRENCY(String r12_CURRENCY) { R12_CURRENCY = r12_CURRENCY; }
		public String getR12_CURRENT() { return R12_CURRENT; }
		public void setR12_CURRENT(String r12_CURRENT) { R12_CURRENT = r12_CURRENT; }
		public String getR12_CALL() { return R12_CALL; }
		public void setR12_CALL(String r12_CALL) { R12_CALL = r12_CALL; }
		public String getR12_SAVINGS() { return R12_SAVINGS; }
		public void setR12_SAVINGS(String r12_SAVINGS) { R12_SAVINGS = r12_SAVINGS; }
		public String getR12_NOTICE_0_31_DAYS() { return R12_NOTICE_0_31_DAYS; }
		public void setR12_NOTICE_0_31_DAYS(String r12_NOTICE_0_31_DAYS) { R12_NOTICE_0_31_DAYS = r12_NOTICE_0_31_DAYS; }
		public String getR12_NOTICE_32_88_DAYS() { return R12_NOTICE_32_88_DAYS; }
		public void setR12_NOTICE_32_88_DAYS(String r12_NOTICE_32_88_DAYS) { R12_NOTICE_32_88_DAYS = r12_NOTICE_32_88_DAYS; }
		public String getR12_91_DEPOSIT_DAY() { return R12_91_DEPOSIT_DAY; }
		public void setR12_91_DEPOSIT_DAY(String r12_91_DEPOSIT_DAY) { R12_91_DEPOSIT_DAY = r12_91_DEPOSIT_DAY; }
		public String getR12_FD_1_6_MONTHS() { return R12_FD_1_6_MONTHS; }
		public void setR12_FD_1_6_MONTHS(String r12_FD_1_6_MONTHS) { R12_FD_1_6_MONTHS = r12_FD_1_6_MONTHS; }
		public String getR12_FD_7_12_MONTHS() { return R12_FD_7_12_MONTHS; }
		public void setR12_FD_7_12_MONTHS(String r12_FD_7_12_MONTHS) { R12_FD_7_12_MONTHS = r12_FD_7_12_MONTHS; }
		public String getR12_FD_13_18_MONTHS() { return R12_FD_13_18_MONTHS; }
		public void setR12_FD_13_18_MONTHS(String r12_FD_13_18_MONTHS) { R12_FD_13_18_MONTHS = r12_FD_13_18_MONTHS; }
		public String getR12_FD_19_24_MONTHS() { return R12_FD_19_24_MONTHS; }
		public void setR12_FD_19_24_MONTHS(String r12_FD_19_24_MONTHS) { R12_FD_19_24_MONTHS = r12_FD_19_24_MONTHS; }
		public String getR12_FD_OVER_24_MONTHS() { return R12_FD_OVER_24_MONTHS; }
		public void setR12_FD_OVER_24_MONTHS(String r12_FD_OVER_24_MONTHS) { R12_FD_OVER_24_MONTHS = r12_FD_OVER_24_MONTHS; }
		public String getR12_TOTAL() { return R12_TOTAL; }
		public void setR12_TOTAL(String r12_TOTAL) { R12_TOTAL = r12_TOTAL; }
		public String getR13_CURRENCY() { return R13_CURRENCY; }
		public void setR13_CURRENCY(String r13_CURRENCY) { R13_CURRENCY = r13_CURRENCY; }
		public String getR13_CURRENT() { return R13_CURRENT; }
		public void setR13_CURRENT(String r13_CURRENT) { R13_CURRENT = r13_CURRENT; }
		public String getR13_CALL() { return R13_CALL; }
		public void setR13_CALL(String r13_CALL) { R13_CALL = r13_CALL; }
		public String getR13_SAVINGS() { return R13_SAVINGS; }
		public void setR13_SAVINGS(String r13_SAVINGS) { R13_SAVINGS = r13_SAVINGS; }
		public String getR13_NOTICE_0_31_DAYS() { return R13_NOTICE_0_31_DAYS; }
		public void setR13_NOTICE_0_31_DAYS(String r13_NOTICE_0_31_DAYS) { R13_NOTICE_0_31_DAYS = r13_NOTICE_0_31_DAYS; }
		public String getR13_NOTICE_32_88_DAYS() { return R13_NOTICE_32_88_DAYS; }
		public void setR13_NOTICE_32_88_DAYS(String r13_NOTICE_32_88_DAYS) { R13_NOTICE_32_88_DAYS = r13_NOTICE_32_88_DAYS; }
		public String getR13_91_DEPOSIT_DAY() { return R13_91_DEPOSIT_DAY; }
		public void setR13_91_DEPOSIT_DAY(String r13_91_DEPOSIT_DAY) { R13_91_DEPOSIT_DAY = r13_91_DEPOSIT_DAY; }
		public String getR13_FD_1_6_MONTHS() { return R13_FD_1_6_MONTHS; }
		public void setR13_FD_1_6_MONTHS(String r13_FD_1_6_MONTHS) { R13_FD_1_6_MONTHS = r13_FD_1_6_MONTHS; }
		public String getR13_FD_7_12_MONTHS() { return R13_FD_7_12_MONTHS; }
		public void setR13_FD_7_12_MONTHS(String r13_FD_7_12_MONTHS) { R13_FD_7_12_MONTHS = r13_FD_7_12_MONTHS; }
		public String getR13_FD_13_18_MONTHS() { return R13_FD_13_18_MONTHS; }
		public void setR13_FD_13_18_MONTHS(String r13_FD_13_18_MONTHS) { R13_FD_13_18_MONTHS = r13_FD_13_18_MONTHS; }
		public String getR13_FD_19_24_MONTHS() { return R13_FD_19_24_MONTHS; }
		public void setR13_FD_19_24_MONTHS(String r13_FD_19_24_MONTHS) { R13_FD_19_24_MONTHS = r13_FD_19_24_MONTHS; }
		public String getR13_FD_OVER_24_MONTHS() { return R13_FD_OVER_24_MONTHS; }
		public void setR13_FD_OVER_24_MONTHS(String r13_FD_OVER_24_MONTHS) { R13_FD_OVER_24_MONTHS = r13_FD_OVER_24_MONTHS; }
		public String getR13_TOTAL() { return R13_TOTAL; }
		public void setR13_TOTAL(String r13_TOTAL) { R13_TOTAL = r13_TOTAL; }
		public String getR14_CURRENCY() { return R14_CURRENCY; }
		public void setR14_CURRENCY(String r14_CURRENCY) { R14_CURRENCY = r14_CURRENCY; }
		public String getR14_CURRENT() { return R14_CURRENT; }
		public void setR14_CURRENT(String r14_CURRENT) { R14_CURRENT = r14_CURRENT; }
		public String getR14_CALL() { return R14_CALL; }
		public void setR14_CALL(String r14_CALL) { R14_CALL = r14_CALL; }
		public String getR14_SAVINGS() { return R14_SAVINGS; }
		public void setR14_SAVINGS(String r14_SAVINGS) { R14_SAVINGS = r14_SAVINGS; }
		public String getR14_NOTICE_0_31_DAYS() { return R14_NOTICE_0_31_DAYS; }
		public void setR14_NOTICE_0_31_DAYS(String r14_NOTICE_0_31_DAYS) { R14_NOTICE_0_31_DAYS = r14_NOTICE_0_31_DAYS; }
		public String getR14_NOTICE_32_88_DAYS() { return R14_NOTICE_32_88_DAYS; }
		public void setR14_NOTICE_32_88_DAYS(String r14_NOTICE_32_88_DAYS) { R14_NOTICE_32_88_DAYS = r14_NOTICE_32_88_DAYS; }
		public String getR14_91_DEPOSIT_DAY() { return R14_91_DEPOSIT_DAY; }
		public void setR14_91_DEPOSIT_DAY(String r14_91_DEPOSIT_DAY) { R14_91_DEPOSIT_DAY = r14_91_DEPOSIT_DAY; }
		public String getR14_FD_1_6_MONTHS() { return R14_FD_1_6_MONTHS; }
		public void setR14_FD_1_6_MONTHS(String r14_FD_1_6_MONTHS) { R14_FD_1_6_MONTHS = r14_FD_1_6_MONTHS; }
		public String getR14_FD_7_12_MONTHS() { return R14_FD_7_12_MONTHS; }
		public void setR14_FD_7_12_MONTHS(String r14_FD_7_12_MONTHS) { R14_FD_7_12_MONTHS = r14_FD_7_12_MONTHS; }
		public String getR14_FD_13_18_MONTHS() { return R14_FD_13_18_MONTHS; }
		public void setR14_FD_13_18_MONTHS(String r14_FD_13_18_MONTHS) { R14_FD_13_18_MONTHS = r14_FD_13_18_MONTHS; }
		public String getR14_FD_19_24_MONTHS() { return R14_FD_19_24_MONTHS; }
		public void setR14_FD_19_24_MONTHS(String r14_FD_19_24_MONTHS) { R14_FD_19_24_MONTHS = r14_FD_19_24_MONTHS; }
		public String getR14_FD_OVER_24_MONTHS() { return R14_FD_OVER_24_MONTHS; }
		public void setR14_FD_OVER_24_MONTHS(String r14_FD_OVER_24_MONTHS) { R14_FD_OVER_24_MONTHS = r14_FD_OVER_24_MONTHS; }
		public String getR14_TOTAL() { return R14_TOTAL; }
		public void setR14_TOTAL(String r14_TOTAL) { R14_TOTAL = r14_TOTAL; }
		public String getR15_CURRENCY() { return R15_CURRENCY; }
		public void setR15_CURRENCY(String r15_CURRENCY) { R15_CURRENCY = r15_CURRENCY; }
		public String getR15_CURRENT() { return R15_CURRENT; }
		public void setR15_CURRENT(String r15_CURRENT) { R15_CURRENT = r15_CURRENT; }
		public String getR15_CALL() { return R15_CALL; }
		public void setR15_CALL(String r15_CALL) { R15_CALL = r15_CALL; }
		public String getR15_SAVINGS() { return R15_SAVINGS; }
		public void setR15_SAVINGS(String r15_SAVINGS) { R15_SAVINGS = r15_SAVINGS; }
		public String getR15_NOTICE_0_31_DAYS() { return R15_NOTICE_0_31_DAYS; }
		public void setR15_NOTICE_0_31_DAYS(String r15_NOTICE_0_31_DAYS) { R15_NOTICE_0_31_DAYS = r15_NOTICE_0_31_DAYS; }
		public String getR15_NOTICE_32_88_DAYS() { return R15_NOTICE_32_88_DAYS; }
		public void setR15_NOTICE_32_88_DAYS(String r15_NOTICE_32_88_DAYS) { R15_NOTICE_32_88_DAYS = r15_NOTICE_32_88_DAYS; }
		public String getR15_91_DEPOSIT_DAY() { return R15_91_DEPOSIT_DAY; }
		public void setR15_91_DEPOSIT_DAY(String r15_91_DEPOSIT_DAY) { R15_91_DEPOSIT_DAY = r15_91_DEPOSIT_DAY; }
		public String getR15_FD_1_6_MONTHS() { return R15_FD_1_6_MONTHS; }
		public void setR15_FD_1_6_MONTHS(String r15_FD_1_6_MONTHS) { R15_FD_1_6_MONTHS = r15_FD_1_6_MONTHS; }
		public String getR15_FD_7_12_MONTHS() { return R15_FD_7_12_MONTHS; }
		public void setR15_FD_7_12_MONTHS(String r15_FD_7_12_MONTHS) { R15_FD_7_12_MONTHS = r15_FD_7_12_MONTHS; }
		public String getR15_FD_13_18_MONTHS() { return R15_FD_13_18_MONTHS; }
		public void setR15_FD_13_18_MONTHS(String r15_FD_13_18_MONTHS) { R15_FD_13_18_MONTHS = r15_FD_13_18_MONTHS; }
		public String getR15_FD_19_24_MONTHS() { return R15_FD_19_24_MONTHS; }
		public void setR15_FD_19_24_MONTHS(String r15_FD_19_24_MONTHS) { R15_FD_19_24_MONTHS = r15_FD_19_24_MONTHS; }
		public String getR15_FD_OVER_24_MONTHS() { return R15_FD_OVER_24_MONTHS; }
		public void setR15_FD_OVER_24_MONTHS(String r15_FD_OVER_24_MONTHS) { R15_FD_OVER_24_MONTHS = r15_FD_OVER_24_MONTHS; }
		public String getR15_TOTAL() { return R15_TOTAL; }
		public void setR15_TOTAL(String r15_TOTAL) { R15_TOTAL = r15_TOTAL; }
		public Date getReport_date() { return report_date; }
		public void setReport_date(Date report_date) { this.report_date = report_date; }
		public BigDecimal getReport_version() { return report_version; }
		public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
		public Date getReportResubDate() { return reportResubDate; }
		public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
		public String getReport_frequency() { return report_frequency; }
		public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
		public String getReport_code() { return report_code; }
		public void setReport_code(String report_code) { this.report_code = report_code; }
		public String getReport_desc() { return report_desc; }
		public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
		public String getEntity_flg() { return entity_flg; }
		public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
		public String getModify_flg() { return modify_flg; }
		public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
		public String getDel_flg() { return del_flg; }
		public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	}

	// =========================================================
	// Inner entity: M_INT_RATES_FCA_Archival_Detail_Entity
	// =========================================================
	public static class M_INT_RATES_FCA_Archival_Detail_Entity {
		private String R10_CURRENCY;
		private String R10_CURRENT;
		private String R10_CALL;
		private String R10_SAVINGS;
		private String R10_NOTICE_0_31_DAYS;
		private String R10_NOTICE_32_88_DAYS;
		private String R10_91_DEPOSIT_DAY;
		private String R10_FD_1_6_MONTHS;
		private String R10_FD_7_12_MONTHS;
		private String R10_FD_13_18_MONTHS;
		private String R10_FD_19_24_MONTHS;
		private String R10_FD_OVER_24_MONTHS;
		private String R10_TOTAL;
		private String R11_CURRENCY;
		private String R11_CURRENT;
		private String R11_CALL;
		private String R11_SAVINGS;
		private String R11_NOTICE_0_31_DAYS;
		private String R11_NOTICE_32_88_DAYS;
		private String R11_91_DEPOSIT_DAY;
		private String R11_FD_1_6_MONTHS;
		private String R11_FD_7_12_MONTHS;
		private String R11_FD_13_18_MONTHS;
		private String R11_FD_19_24_MONTHS;
		private String R11_FD_OVER_24_MONTHS;
		private String R11_TOTAL;
		private String R12_CURRENCY;
		private String R12_CURRENT;
		private String R12_CALL;
		private String R12_SAVINGS;
		private String R12_NOTICE_0_31_DAYS;
		private String R12_NOTICE_32_88_DAYS;
		private String R12_91_DEPOSIT_DAY;
		private String R12_FD_1_6_MONTHS;
		private String R12_FD_7_12_MONTHS;
		private String R12_FD_13_18_MONTHS;
		private String R12_FD_19_24_MONTHS;
		private String R12_FD_OVER_24_MONTHS;
		private String R12_TOTAL;
		private String R13_CURRENCY;
		private String R13_CURRENT;
		private String R13_CALL;
		private String R13_SAVINGS;
		private String R13_NOTICE_0_31_DAYS;
		private String R13_NOTICE_32_88_DAYS;
		private String R13_91_DEPOSIT_DAY;
		private String R13_FD_1_6_MONTHS;
		private String R13_FD_7_12_MONTHS;
		private String R13_FD_13_18_MONTHS;
		private String R13_FD_19_24_MONTHS;
		private String R13_FD_OVER_24_MONTHS;
		private String R13_TOTAL;
		private String R14_CURRENCY;
		private String R14_CURRENT;
		private String R14_CALL;
		private String R14_SAVINGS;
		private String R14_NOTICE_0_31_DAYS;
		private String R14_NOTICE_32_88_DAYS;
		private String R14_91_DEPOSIT_DAY;
		private String R14_FD_1_6_MONTHS;
		private String R14_FD_7_12_MONTHS;
		private String R14_FD_13_18_MONTHS;
		private String R14_FD_19_24_MONTHS;
		private String R14_FD_OVER_24_MONTHS;
		private String R14_TOTAL;
		private String R15_CURRENCY;
		private String R15_CURRENT;
		private String R15_CALL;
		private String R15_SAVINGS;
		private String R15_NOTICE_0_31_DAYS;
		private String R15_NOTICE_32_88_DAYS;
		private String R15_91_DEPOSIT_DAY;
		private String R15_FD_1_6_MONTHS;
		private String R15_FD_7_12_MONTHS;
		private String R15_FD_13_18_MONTHS;
		private String R15_FD_19_24_MONTHS;
		private String R15_FD_OVER_24_MONTHS;
		private String R15_TOTAL;

		private Date report_date;
		private BigDecimal report_version;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_CURRENCY() { return R10_CURRENCY; }
		public void setR10_CURRENCY(String r10_CURRENCY) { R10_CURRENCY = r10_CURRENCY; }
		public String getR10_CURRENT() { return R10_CURRENT; }
		public void setR10_CURRENT(String r10_CURRENT) { R10_CURRENT = r10_CURRENT; }
		public String getR10_CALL() { return R10_CALL; }
		public void setR10_CALL(String r10_CALL) { R10_CALL = r10_CALL; }
		public String getR10_SAVINGS() { return R10_SAVINGS; }
		public void setR10_SAVINGS(String r10_SAVINGS) { R10_SAVINGS = r10_SAVINGS; }
		public String getR10_NOTICE_0_31_DAYS() { return R10_NOTICE_0_31_DAYS; }
		public void setR10_NOTICE_0_31_DAYS(String r10_NOTICE_0_31_DAYS) { R10_NOTICE_0_31_DAYS = r10_NOTICE_0_31_DAYS; }
		public String getR10_NOTICE_32_88_DAYS() { return R10_NOTICE_32_88_DAYS; }
		public void setR10_NOTICE_32_88_DAYS(String r10_NOTICE_32_88_DAYS) { R10_NOTICE_32_88_DAYS = r10_NOTICE_32_88_DAYS; }
		public String getR10_91_DEPOSIT_DAY() { return R10_91_DEPOSIT_DAY; }
		public void setR10_91_DEPOSIT_DAY(String r10_91_DEPOSIT_DAY) { R10_91_DEPOSIT_DAY = r10_91_DEPOSIT_DAY; }
		public String getR10_FD_1_6_MONTHS() { return R10_FD_1_6_MONTHS; }
		public void setR10_FD_1_6_MONTHS(String r10_FD_1_6_MONTHS) { R10_FD_1_6_MONTHS = r10_FD_1_6_MONTHS; }
		public String getR10_FD_7_12_MONTHS() { return R10_FD_7_12_MONTHS; }
		public void setR10_FD_7_12_MONTHS(String r10_FD_7_12_MONTHS) { R10_FD_7_12_MONTHS = r10_FD_7_12_MONTHS; }
		public String getR10_FD_13_18_MONTHS() { return R10_FD_13_18_MONTHS; }
		public void setR10_FD_13_18_MONTHS(String r10_FD_13_18_MONTHS) { R10_FD_13_18_MONTHS = r10_FD_13_18_MONTHS; }
		public String getR10_FD_19_24_MONTHS() { return R10_FD_19_24_MONTHS; }
		public void setR10_FD_19_24_MONTHS(String r10_FD_19_24_MONTHS) { R10_FD_19_24_MONTHS = r10_FD_19_24_MONTHS; }
		public String getR10_FD_OVER_24_MONTHS() { return R10_FD_OVER_24_MONTHS; }
		public void setR10_FD_OVER_24_MONTHS(String r10_FD_OVER_24_MONTHS) { R10_FD_OVER_24_MONTHS = r10_FD_OVER_24_MONTHS; }
		public String getR10_TOTAL() { return R10_TOTAL; }
		public void setR10_TOTAL(String r10_TOTAL) { R10_TOTAL = r10_TOTAL; }
		public String getR11_CURRENCY() { return R11_CURRENCY; }
		public void setR11_CURRENCY(String r11_CURRENCY) { R11_CURRENCY = r11_CURRENCY; }
		public String getR11_CURRENT() { return R11_CURRENT; }
		public void setR11_CURRENT(String r11_CURRENT) { R11_CURRENT = r11_CURRENT; }
		public String getR11_CALL() { return R11_CALL; }
		public void setR11_CALL(String r11_CALL) { R11_CALL = r11_CALL; }
		public String getR11_SAVINGS() { return R11_SAVINGS; }
		public void setR11_SAVINGS(String r11_SAVINGS) { R11_SAVINGS = r11_SAVINGS; }
		public String getR11_NOTICE_0_31_DAYS() { return R11_NOTICE_0_31_DAYS; }
		public void setR11_NOTICE_0_31_DAYS(String r11_NOTICE_0_31_DAYS) { R11_NOTICE_0_31_DAYS = r11_NOTICE_0_31_DAYS; }
		public String getR11_NOTICE_32_88_DAYS() { return R11_NOTICE_32_88_DAYS; }
		public void setR11_NOTICE_32_88_DAYS(String r11_NOTICE_32_88_DAYS) { R11_NOTICE_32_88_DAYS = r11_NOTICE_32_88_DAYS; }
		public String getR11_91_DEPOSIT_DAY() { return R11_91_DEPOSIT_DAY; }
		public void setR11_91_DEPOSIT_DAY(String r11_91_DEPOSIT_DAY) { R11_91_DEPOSIT_DAY = r11_91_DEPOSIT_DAY; }
		public String getR11_FD_1_6_MONTHS() { return R11_FD_1_6_MONTHS; }
		public void setR11_FD_1_6_MONTHS(String r11_FD_1_6_MONTHS) { R11_FD_1_6_MONTHS = r11_FD_1_6_MONTHS; }
		public String getR11_FD_7_12_MONTHS() { return R11_FD_7_12_MONTHS; }
		public void setR11_FD_7_12_MONTHS(String r11_FD_7_12_MONTHS) { R11_FD_7_12_MONTHS = r11_FD_7_12_MONTHS; }
		public String getR11_FD_13_18_MONTHS() { return R11_FD_13_18_MONTHS; }
		public void setR11_FD_13_18_MONTHS(String r11_FD_13_18_MONTHS) { R11_FD_13_18_MONTHS = r11_FD_13_18_MONTHS; }
		public String getR11_FD_19_24_MONTHS() { return R11_FD_19_24_MONTHS; }
		public void setR11_FD_19_24_MONTHS(String r11_FD_19_24_MONTHS) { R11_FD_19_24_MONTHS = r11_FD_19_24_MONTHS; }
		public String getR11_FD_OVER_24_MONTHS() { return R11_FD_OVER_24_MONTHS; }
		public void setR11_FD_OVER_24_MONTHS(String r11_FD_OVER_24_MONTHS) { R11_FD_OVER_24_MONTHS = r11_FD_OVER_24_MONTHS; }
		public String getR11_TOTAL() { return R11_TOTAL; }
		public void setR11_TOTAL(String r11_TOTAL) { R11_TOTAL = r11_TOTAL; }
		public String getR12_CURRENCY() { return R12_CURRENCY; }
		public void setR12_CURRENCY(String r12_CURRENCY) { R12_CURRENCY = r12_CURRENCY; }
		public String getR12_CURRENT() { return R12_CURRENT; }
		public void setR12_CURRENT(String r12_CURRENT) { R12_CURRENT = r12_CURRENT; }
		public String getR12_CALL() { return R12_CALL; }
		public void setR12_CALL(String r12_CALL) { R12_CALL = r12_CALL; }
		public String getR12_SAVINGS() { return R12_SAVINGS; }
		public void setR12_SAVINGS(String r12_SAVINGS) { R12_SAVINGS = r12_SAVINGS; }
		public String getR12_NOTICE_0_31_DAYS() { return R12_NOTICE_0_31_DAYS; }
		public void setR12_NOTICE_0_31_DAYS(String r12_NOTICE_0_31_DAYS) { R12_NOTICE_0_31_DAYS = r12_NOTICE_0_31_DAYS; }
		public String getR12_NOTICE_32_88_DAYS() { return R12_NOTICE_32_88_DAYS; }
		public void setR12_NOTICE_32_88_DAYS(String r12_NOTICE_32_88_DAYS) { R12_NOTICE_32_88_DAYS = r12_NOTICE_32_88_DAYS; }
		public String getR12_91_DEPOSIT_DAY() { return R12_91_DEPOSIT_DAY; }
		public void setR12_91_DEPOSIT_DAY(String r12_91_DEPOSIT_DAY) { R12_91_DEPOSIT_DAY = r12_91_DEPOSIT_DAY; }
		public String getR12_FD_1_6_MONTHS() { return R12_FD_1_6_MONTHS; }
		public void setR12_FD_1_6_MONTHS(String r12_FD_1_6_MONTHS) { R12_FD_1_6_MONTHS = r12_FD_1_6_MONTHS; }
		public String getR12_FD_7_12_MONTHS() { return R12_FD_7_12_MONTHS; }
		public void setR12_FD_7_12_MONTHS(String r12_FD_7_12_MONTHS) { R12_FD_7_12_MONTHS = r12_FD_7_12_MONTHS; }
		public String getR12_FD_13_18_MONTHS() { return R12_FD_13_18_MONTHS; }
		public void setR12_FD_13_18_MONTHS(String r12_FD_13_18_MONTHS) { R12_FD_13_18_MONTHS = r12_FD_13_18_MONTHS; }
		public String getR12_FD_19_24_MONTHS() { return R12_FD_19_24_MONTHS; }
		public void setR12_FD_19_24_MONTHS(String r12_FD_19_24_MONTHS) { R12_FD_19_24_MONTHS = r12_FD_19_24_MONTHS; }
		public String getR12_FD_OVER_24_MONTHS() { return R12_FD_OVER_24_MONTHS; }
		public void setR12_FD_OVER_24_MONTHS(String r12_FD_OVER_24_MONTHS) { R12_FD_OVER_24_MONTHS = r12_FD_OVER_24_MONTHS; }
		public String getR12_TOTAL() { return R12_TOTAL; }
		public void setR12_TOTAL(String r12_TOTAL) { R12_TOTAL = r12_TOTAL; }
		public String getR13_CURRENCY() { return R13_CURRENCY; }
		public void setR13_CURRENCY(String r13_CURRENCY) { R13_CURRENCY = r13_CURRENCY; }
		public String getR13_CURRENT() { return R13_CURRENT; }
		public void setR13_CURRENT(String r13_CURRENT) { R13_CURRENT = r13_CURRENT; }
		public String getR13_CALL() { return R13_CALL; }
		public void setR13_CALL(String r13_CALL) { R13_CALL = r13_CALL; }
		public String getR13_SAVINGS() { return R13_SAVINGS; }
		public void setR13_SAVINGS(String r13_SAVINGS) { R13_SAVINGS = r13_SAVINGS; }
		public String getR13_NOTICE_0_31_DAYS() { return R13_NOTICE_0_31_DAYS; }
		public void setR13_NOTICE_0_31_DAYS(String r13_NOTICE_0_31_DAYS) { R13_NOTICE_0_31_DAYS = r13_NOTICE_0_31_DAYS; }
		public String getR13_NOTICE_32_88_DAYS() { return R13_NOTICE_32_88_DAYS; }
		public void setR13_NOTICE_32_88_DAYS(String r13_NOTICE_32_88_DAYS) { R13_NOTICE_32_88_DAYS = r13_NOTICE_32_88_DAYS; }
		public String getR13_91_DEPOSIT_DAY() { return R13_91_DEPOSIT_DAY; }
		public void setR13_91_DEPOSIT_DAY(String r13_91_DEPOSIT_DAY) { R13_91_DEPOSIT_DAY = r13_91_DEPOSIT_DAY; }
		public String getR13_FD_1_6_MONTHS() { return R13_FD_1_6_MONTHS; }
		public void setR13_FD_1_6_MONTHS(String r13_FD_1_6_MONTHS) { R13_FD_1_6_MONTHS = r13_FD_1_6_MONTHS; }
		public String getR13_FD_7_12_MONTHS() { return R13_FD_7_12_MONTHS; }
		public void setR13_FD_7_12_MONTHS(String r13_FD_7_12_MONTHS) { R13_FD_7_12_MONTHS = r13_FD_7_12_MONTHS; }
		public String getR13_FD_13_18_MONTHS() { return R13_FD_13_18_MONTHS; }
		public void setR13_FD_13_18_MONTHS(String r13_FD_13_18_MONTHS) { R13_FD_13_18_MONTHS = r13_FD_13_18_MONTHS; }
		public String getR13_FD_19_24_MONTHS() { return R13_FD_19_24_MONTHS; }
		public void setR13_FD_19_24_MONTHS(String r13_FD_19_24_MONTHS) { R13_FD_19_24_MONTHS = r13_FD_19_24_MONTHS; }
		public String getR13_FD_OVER_24_MONTHS() { return R13_FD_OVER_24_MONTHS; }
		public void setR13_FD_OVER_24_MONTHS(String r13_FD_OVER_24_MONTHS) { R13_FD_OVER_24_MONTHS = r13_FD_OVER_24_MONTHS; }
		public String getR13_TOTAL() { return R13_TOTAL; }
		public void setR13_TOTAL(String r13_TOTAL) { R13_TOTAL = r13_TOTAL; }
		public String getR14_CURRENCY() { return R14_CURRENCY; }
		public void setR14_CURRENCY(String r14_CURRENCY) { R14_CURRENCY = r14_CURRENCY; }
		public String getR14_CURRENT() { return R14_CURRENT; }
		public void setR14_CURRENT(String r14_CURRENT) { R14_CURRENT = r14_CURRENT; }
		public String getR14_CALL() { return R14_CALL; }
		public void setR14_CALL(String r14_CALL) { R14_CALL = r14_CALL; }
		public String getR14_SAVINGS() { return R14_SAVINGS; }
		public void setR14_SAVINGS(String r14_SAVINGS) { R14_SAVINGS = r14_SAVINGS; }
		public String getR14_NOTICE_0_31_DAYS() { return R14_NOTICE_0_31_DAYS; }
		public void setR14_NOTICE_0_31_DAYS(String r14_NOTICE_0_31_DAYS) { R14_NOTICE_0_31_DAYS = r14_NOTICE_0_31_DAYS; }
		public String getR14_NOTICE_32_88_DAYS() { return R14_NOTICE_32_88_DAYS; }
		public void setR14_NOTICE_32_88_DAYS(String r14_NOTICE_32_88_DAYS) { R14_NOTICE_32_88_DAYS = r14_NOTICE_32_88_DAYS; }
		public String getR14_91_DEPOSIT_DAY() { return R14_91_DEPOSIT_DAY; }
		public void setR14_91_DEPOSIT_DAY(String r14_91_DEPOSIT_DAY) { R14_91_DEPOSIT_DAY = r14_91_DEPOSIT_DAY; }
		public String getR14_FD_1_6_MONTHS() { return R14_FD_1_6_MONTHS; }
		public void setR14_FD_1_6_MONTHS(String r14_FD_1_6_MONTHS) { R14_FD_1_6_MONTHS = r14_FD_1_6_MONTHS; }
		public String getR14_FD_7_12_MONTHS() { return R14_FD_7_12_MONTHS; }
		public void setR14_FD_7_12_MONTHS(String r14_FD_7_12_MONTHS) { R14_FD_7_12_MONTHS = r14_FD_7_12_MONTHS; }
		public String getR14_FD_13_18_MONTHS() { return R14_FD_13_18_MONTHS; }
		public void setR14_FD_13_18_MONTHS(String r14_FD_13_18_MONTHS) { R14_FD_13_18_MONTHS = r14_FD_13_18_MONTHS; }
		public String getR14_FD_19_24_MONTHS() { return R14_FD_19_24_MONTHS; }
		public void setR14_FD_19_24_MONTHS(String r14_FD_19_24_MONTHS) { R14_FD_19_24_MONTHS = r14_FD_19_24_MONTHS; }
		public String getR14_FD_OVER_24_MONTHS() { return R14_FD_OVER_24_MONTHS; }
		public void setR14_FD_OVER_24_MONTHS(String r14_FD_OVER_24_MONTHS) { R14_FD_OVER_24_MONTHS = r14_FD_OVER_24_MONTHS; }
		public String getR14_TOTAL() { return R14_TOTAL; }
		public void setR14_TOTAL(String r14_TOTAL) { R14_TOTAL = r14_TOTAL; }
		public String getR15_CURRENCY() { return R15_CURRENCY; }
		public void setR15_CURRENCY(String r15_CURRENCY) { R15_CURRENCY = r15_CURRENCY; }
		public String getR15_CURRENT() { return R15_CURRENT; }
		public void setR15_CURRENT(String r15_CURRENT) { R15_CURRENT = r15_CURRENT; }
		public String getR15_CALL() { return R15_CALL; }
		public void setR15_CALL(String r15_CALL) { R15_CALL = r15_CALL; }
		public String getR15_SAVINGS() { return R15_SAVINGS; }
		public void setR15_SAVINGS(String r15_SAVINGS) { R15_SAVINGS = r15_SAVINGS; }
		public String getR15_NOTICE_0_31_DAYS() { return R15_NOTICE_0_31_DAYS; }
		public void setR15_NOTICE_0_31_DAYS(String r15_NOTICE_0_31_DAYS) { R15_NOTICE_0_31_DAYS = r15_NOTICE_0_31_DAYS; }
		public String getR15_NOTICE_32_88_DAYS() { return R15_NOTICE_32_88_DAYS; }
		public void setR15_NOTICE_32_88_DAYS(String r15_NOTICE_32_88_DAYS) { R15_NOTICE_32_88_DAYS = r15_NOTICE_32_88_DAYS; }
		public String getR15_91_DEPOSIT_DAY() { return R15_91_DEPOSIT_DAY; }
		public void setR15_91_DEPOSIT_DAY(String r15_91_DEPOSIT_DAY) { R15_91_DEPOSIT_DAY = r15_91_DEPOSIT_DAY; }
		public String getR15_FD_1_6_MONTHS() { return R15_FD_1_6_MONTHS; }
		public void setR15_FD_1_6_MONTHS(String r15_FD_1_6_MONTHS) { R15_FD_1_6_MONTHS = r15_FD_1_6_MONTHS; }
		public String getR15_FD_7_12_MONTHS() { return R15_FD_7_12_MONTHS; }
		public void setR15_FD_7_12_MONTHS(String r15_FD_7_12_MONTHS) { R15_FD_7_12_MONTHS = r15_FD_7_12_MONTHS; }
		public String getR15_FD_13_18_MONTHS() { return R15_FD_13_18_MONTHS; }
		public void setR15_FD_13_18_MONTHS(String r15_FD_13_18_MONTHS) { R15_FD_13_18_MONTHS = r15_FD_13_18_MONTHS; }
		public String getR15_FD_19_24_MONTHS() { return R15_FD_19_24_MONTHS; }
		public void setR15_FD_19_24_MONTHS(String r15_FD_19_24_MONTHS) { R15_FD_19_24_MONTHS = r15_FD_19_24_MONTHS; }
		public String getR15_FD_OVER_24_MONTHS() { return R15_FD_OVER_24_MONTHS; }
		public void setR15_FD_OVER_24_MONTHS(String r15_FD_OVER_24_MONTHS) { R15_FD_OVER_24_MONTHS = r15_FD_OVER_24_MONTHS; }
		public String getR15_TOTAL() { return R15_TOTAL; }
		public void setR15_TOTAL(String r15_TOTAL) { R15_TOTAL = r15_TOTAL; }
		public Date getReport_date() { return report_date; }
		public void setReport_date(Date report_date) { this.report_date = report_date; }
		public BigDecimal getReport_version() { return report_version; }
		public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
		public Date getReportResubDate() { return reportResubDate; }
		public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
		public String getReport_frequency() { return report_frequency; }
		public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
		public String getReport_code() { return report_code; }
		public void setReport_code(String report_code) { this.report_code = report_code; }
		public String getReport_desc() { return report_desc; }
		public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
		public String getEntity_flg() { return entity_flg; }
		public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
		public String getModify_flg() { return modify_flg; }
		public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
		public String getDel_flg() { return del_flg; }
		public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	}

	// =========================================================
	// Inner entity: M_INT_RATES_FCA_RESUB_Summary_Entity
	// =========================================================
	public static class M_INT_RATES_FCA_RESUB_Summary_Entity {
		private String R10_CURRENCY;
		private String R10_CURRENT;
		private String R10_CALL;
		private String R10_SAVINGS;
		private String R10_NOTICE_0_31_DAYS;
		private String R10_NOTICE_32_88_DAYS;
		private String R10_91_DEPOSIT_DAY;
		private String R10_FD_1_6_MONTHS;
		private String R10_FD_7_12_MONTHS;
		private String R10_FD_13_18_MONTHS;
		private String R10_FD_19_24_MONTHS;
		private String R10_FD_OVER_24_MONTHS;
		private String R10_TOTAL;
		private String R11_CURRENCY;
		private String R11_CURRENT;
		private String R11_CALL;
		private String R11_SAVINGS;
		private String R11_NOTICE_0_31_DAYS;
		private String R11_NOTICE_32_88_DAYS;
		private String R11_91_DEPOSIT_DAY;
		private String R11_FD_1_6_MONTHS;
		private String R11_FD_7_12_MONTHS;
		private String R11_FD_13_18_MONTHS;
		private String R11_FD_19_24_MONTHS;
		private String R11_FD_OVER_24_MONTHS;
		private String R11_TOTAL;
		private String R12_CURRENCY;
		private String R12_CURRENT;
		private String R12_CALL;
		private String R12_SAVINGS;
		private String R12_NOTICE_0_31_DAYS;
		private String R12_NOTICE_32_88_DAYS;
		private String R12_91_DEPOSIT_DAY;
		private String R12_FD_1_6_MONTHS;
		private String R12_FD_7_12_MONTHS;
		private String R12_FD_13_18_MONTHS;
		private String R12_FD_19_24_MONTHS;
		private String R12_FD_OVER_24_MONTHS;
		private String R12_TOTAL;
		private String R13_CURRENCY;
		private String R13_CURRENT;
		private String R13_CALL;
		private String R13_SAVINGS;
		private String R13_NOTICE_0_31_DAYS;
		private String R13_NOTICE_32_88_DAYS;
		private String R13_91_DEPOSIT_DAY;
		private String R13_FD_1_6_MONTHS;
		private String R13_FD_7_12_MONTHS;
		private String R13_FD_13_18_MONTHS;
		private String R13_FD_19_24_MONTHS;
		private String R13_FD_OVER_24_MONTHS;
		private String R13_TOTAL;
		private String R14_CURRENCY;
		private String R14_CURRENT;
		private String R14_CALL;
		private String R14_SAVINGS;
		private String R14_NOTICE_0_31_DAYS;
		private String R14_NOTICE_32_88_DAYS;
		private String R14_91_DEPOSIT_DAY;
		private String R14_FD_1_6_MONTHS;
		private String R14_FD_7_12_MONTHS;
		private String R14_FD_13_18_MONTHS;
		private String R14_FD_19_24_MONTHS;
		private String R14_FD_OVER_24_MONTHS;
		private String R14_TOTAL;
		private String R15_CURRENCY;
		private String R15_CURRENT;
		private String R15_CALL;
		private String R15_SAVINGS;
		private String R15_NOTICE_0_31_DAYS;
		private String R15_NOTICE_32_88_DAYS;
		private String R15_91_DEPOSIT_DAY;
		private String R15_FD_1_6_MONTHS;
		private String R15_FD_7_12_MONTHS;
		private String R15_FD_13_18_MONTHS;
		private String R15_FD_19_24_MONTHS;
		private String R15_FD_OVER_24_MONTHS;
		private String R15_TOTAL;

		private Date report_date;
		private BigDecimal report_version;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_CURRENCY() { return R10_CURRENCY; }
		public void setR10_CURRENCY(String r10_CURRENCY) { R10_CURRENCY = r10_CURRENCY; }
		public String getR10_CURRENT() { return R10_CURRENT; }
		public void setR10_CURRENT(String r10_CURRENT) { R10_CURRENT = r10_CURRENT; }
		public String getR10_CALL() { return R10_CALL; }
		public void setR10_CALL(String r10_CALL) { R10_CALL = r10_CALL; }
		public String getR10_SAVINGS() { return R10_SAVINGS; }
		public void setR10_SAVINGS(String r10_SAVINGS) { R10_SAVINGS = r10_SAVINGS; }
		public String getR10_NOTICE_0_31_DAYS() { return R10_NOTICE_0_31_DAYS; }
		public void setR10_NOTICE_0_31_DAYS(String r10_NOTICE_0_31_DAYS) { R10_NOTICE_0_31_DAYS = r10_NOTICE_0_31_DAYS; }
		public String getR10_NOTICE_32_88_DAYS() { return R10_NOTICE_32_88_DAYS; }
		public void setR10_NOTICE_32_88_DAYS(String r10_NOTICE_32_88_DAYS) { R10_NOTICE_32_88_DAYS = r10_NOTICE_32_88_DAYS; }
		public String getR10_91_DEPOSIT_DAY() { return R10_91_DEPOSIT_DAY; }
		public void setR10_91_DEPOSIT_DAY(String r10_91_DEPOSIT_DAY) { R10_91_DEPOSIT_DAY = r10_91_DEPOSIT_DAY; }
		public String getR10_FD_1_6_MONTHS() { return R10_FD_1_6_MONTHS; }
		public void setR10_FD_1_6_MONTHS(String r10_FD_1_6_MONTHS) { R10_FD_1_6_MONTHS = r10_FD_1_6_MONTHS; }
		public String getR10_FD_7_12_MONTHS() { return R10_FD_7_12_MONTHS; }
		public void setR10_FD_7_12_MONTHS(String r10_FD_7_12_MONTHS) { R10_FD_7_12_MONTHS = r10_FD_7_12_MONTHS; }
		public String getR10_FD_13_18_MONTHS() { return R10_FD_13_18_MONTHS; }
		public void setR10_FD_13_18_MONTHS(String r10_FD_13_18_MONTHS) { R10_FD_13_18_MONTHS = r10_FD_13_18_MONTHS; }
		public String getR10_FD_19_24_MONTHS() { return R10_FD_19_24_MONTHS; }
		public void setR10_FD_19_24_MONTHS(String r10_FD_19_24_MONTHS) { R10_FD_19_24_MONTHS = r10_FD_19_24_MONTHS; }
		public String getR10_FD_OVER_24_MONTHS() { return R10_FD_OVER_24_MONTHS; }
		public void setR10_FD_OVER_24_MONTHS(String r10_FD_OVER_24_MONTHS) { R10_FD_OVER_24_MONTHS = r10_FD_OVER_24_MONTHS; }
		public String getR10_TOTAL() { return R10_TOTAL; }
		public void setR10_TOTAL(String r10_TOTAL) { R10_TOTAL = r10_TOTAL; }
		public String getR11_CURRENCY() { return R11_CURRENCY; }
		public void setR11_CURRENCY(String r11_CURRENCY) { R11_CURRENCY = r11_CURRENCY; }
		public String getR11_CURRENT() { return R11_CURRENT; }
		public void setR11_CURRENT(String r11_CURRENT) { R11_CURRENT = r11_CURRENT; }
		public String getR11_CALL() { return R11_CALL; }
		public void setR11_CALL(String r11_CALL) { R11_CALL = r11_CALL; }
		public String getR11_SAVINGS() { return R11_SAVINGS; }
		public void setR11_SAVINGS(String r11_SAVINGS) { R11_SAVINGS = r11_SAVINGS; }
		public String getR11_NOTICE_0_31_DAYS() { return R11_NOTICE_0_31_DAYS; }
		public void setR11_NOTICE_0_31_DAYS(String r11_NOTICE_0_31_DAYS) { R11_NOTICE_0_31_DAYS = r11_NOTICE_0_31_DAYS; }
		public String getR11_NOTICE_32_88_DAYS() { return R11_NOTICE_32_88_DAYS; }
		public void setR11_NOTICE_32_88_DAYS(String r11_NOTICE_32_88_DAYS) { R11_NOTICE_32_88_DAYS = r11_NOTICE_32_88_DAYS; }
		public String getR11_91_DEPOSIT_DAY() { return R11_91_DEPOSIT_DAY; }
		public void setR11_91_DEPOSIT_DAY(String r11_91_DEPOSIT_DAY) { R11_91_DEPOSIT_DAY = r11_91_DEPOSIT_DAY; }
		public String getR11_FD_1_6_MONTHS() { return R11_FD_1_6_MONTHS; }
		public void setR11_FD_1_6_MONTHS(String r11_FD_1_6_MONTHS) { R11_FD_1_6_MONTHS = r11_FD_1_6_MONTHS; }
		public String getR11_FD_7_12_MONTHS() { return R11_FD_7_12_MONTHS; }
		public void setR11_FD_7_12_MONTHS(String r11_FD_7_12_MONTHS) { R11_FD_7_12_MONTHS = r11_FD_7_12_MONTHS; }
		public String getR11_FD_13_18_MONTHS() { return R11_FD_13_18_MONTHS; }
		public void setR11_FD_13_18_MONTHS(String r11_FD_13_18_MONTHS) { R11_FD_13_18_MONTHS = r11_FD_13_18_MONTHS; }
		public String getR11_FD_19_24_MONTHS() { return R11_FD_19_24_MONTHS; }
		public void setR11_FD_19_24_MONTHS(String r11_FD_19_24_MONTHS) { R11_FD_19_24_MONTHS = r11_FD_19_24_MONTHS; }
		public String getR11_FD_OVER_24_MONTHS() { return R11_FD_OVER_24_MONTHS; }
		public void setR11_FD_OVER_24_MONTHS(String r11_FD_OVER_24_MONTHS) { R11_FD_OVER_24_MONTHS = r11_FD_OVER_24_MONTHS; }
		public String getR11_TOTAL() { return R11_TOTAL; }
		public void setR11_TOTAL(String r11_TOTAL) { R11_TOTAL = r11_TOTAL; }
		public String getR12_CURRENCY() { return R12_CURRENCY; }
		public void setR12_CURRENCY(String r12_CURRENCY) { R12_CURRENCY = r12_CURRENCY; }
		public String getR12_CURRENT() { return R12_CURRENT; }
		public void setR12_CURRENT(String r12_CURRENT) { R12_CURRENT = r12_CURRENT; }
		public String getR12_CALL() { return R12_CALL; }
		public void setR12_CALL(String r12_CALL) { R12_CALL = r12_CALL; }
		public String getR12_SAVINGS() { return R12_SAVINGS; }
		public void setR12_SAVINGS(String r12_SAVINGS) { R12_SAVINGS = r12_SAVINGS; }
		public String getR12_NOTICE_0_31_DAYS() { return R12_NOTICE_0_31_DAYS; }
		public void setR12_NOTICE_0_31_DAYS(String r12_NOTICE_0_31_DAYS) { R12_NOTICE_0_31_DAYS = r12_NOTICE_0_31_DAYS; }
		public String getR12_NOTICE_32_88_DAYS() { return R12_NOTICE_32_88_DAYS; }
		public void setR12_NOTICE_32_88_DAYS(String r12_NOTICE_32_88_DAYS) { R12_NOTICE_32_88_DAYS = r12_NOTICE_32_88_DAYS; }
		public String getR12_91_DEPOSIT_DAY() { return R12_91_DEPOSIT_DAY; }
		public void setR12_91_DEPOSIT_DAY(String r12_91_DEPOSIT_DAY) { R12_91_DEPOSIT_DAY = r12_91_DEPOSIT_DAY; }
		public String getR12_FD_1_6_MONTHS() { return R12_FD_1_6_MONTHS; }
		public void setR12_FD_1_6_MONTHS(String r12_FD_1_6_MONTHS) { R12_FD_1_6_MONTHS = r12_FD_1_6_MONTHS; }
		public String getR12_FD_7_12_MONTHS() { return R12_FD_7_12_MONTHS; }
		public void setR12_FD_7_12_MONTHS(String r12_FD_7_12_MONTHS) { R12_FD_7_12_MONTHS = r12_FD_7_12_MONTHS; }
		public String getR12_FD_13_18_MONTHS() { return R12_FD_13_18_MONTHS; }
		public void setR12_FD_13_18_MONTHS(String r12_FD_13_18_MONTHS) { R12_FD_13_18_MONTHS = r12_FD_13_18_MONTHS; }
		public String getR12_FD_19_24_MONTHS() { return R12_FD_19_24_MONTHS; }
		public void setR12_FD_19_24_MONTHS(String r12_FD_19_24_MONTHS) { R12_FD_19_24_MONTHS = r12_FD_19_24_MONTHS; }
		public String getR12_FD_OVER_24_MONTHS() { return R12_FD_OVER_24_MONTHS; }
		public void setR12_FD_OVER_24_MONTHS(String r12_FD_OVER_24_MONTHS) { R12_FD_OVER_24_MONTHS = r12_FD_OVER_24_MONTHS; }
		public String getR12_TOTAL() { return R12_TOTAL; }
		public void setR12_TOTAL(String r12_TOTAL) { R12_TOTAL = r12_TOTAL; }
		public String getR13_CURRENCY() { return R13_CURRENCY; }
		public void setR13_CURRENCY(String r13_CURRENCY) { R13_CURRENCY = r13_CURRENCY; }
		public String getR13_CURRENT() { return R13_CURRENT; }
		public void setR13_CURRENT(String r13_CURRENT) { R13_CURRENT = r13_CURRENT; }
		public String getR13_CALL() { return R13_CALL; }
		public void setR13_CALL(String r13_CALL) { R13_CALL = r13_CALL; }
		public String getR13_SAVINGS() { return R13_SAVINGS; }
		public void setR13_SAVINGS(String r13_SAVINGS) { R13_SAVINGS = r13_SAVINGS; }
		public String getR13_NOTICE_0_31_DAYS() { return R13_NOTICE_0_31_DAYS; }
		public void setR13_NOTICE_0_31_DAYS(String r13_NOTICE_0_31_DAYS) { R13_NOTICE_0_31_DAYS = r13_NOTICE_0_31_DAYS; }
		public String getR13_NOTICE_32_88_DAYS() { return R13_NOTICE_32_88_DAYS; }
		public void setR13_NOTICE_32_88_DAYS(String r13_NOTICE_32_88_DAYS) { R13_NOTICE_32_88_DAYS = r13_NOTICE_32_88_DAYS; }
		public String getR13_91_DEPOSIT_DAY() { return R13_91_DEPOSIT_DAY; }
		public void setR13_91_DEPOSIT_DAY(String r13_91_DEPOSIT_DAY) { R13_91_DEPOSIT_DAY = r13_91_DEPOSIT_DAY; }
		public String getR13_FD_1_6_MONTHS() { return R13_FD_1_6_MONTHS; }
		public void setR13_FD_1_6_MONTHS(String r13_FD_1_6_MONTHS) { R13_FD_1_6_MONTHS = r13_FD_1_6_MONTHS; }
		public String getR13_FD_7_12_MONTHS() { return R13_FD_7_12_MONTHS; }
		public void setR13_FD_7_12_MONTHS(String r13_FD_7_12_MONTHS) { R13_FD_7_12_MONTHS = r13_FD_7_12_MONTHS; }
		public String getR13_FD_13_18_MONTHS() { return R13_FD_13_18_MONTHS; }
		public void setR13_FD_13_18_MONTHS(String r13_FD_13_18_MONTHS) { R13_FD_13_18_MONTHS = r13_FD_13_18_MONTHS; }
		public String getR13_FD_19_24_MONTHS() { return R13_FD_19_24_MONTHS; }
		public void setR13_FD_19_24_MONTHS(String r13_FD_19_24_MONTHS) { R13_FD_19_24_MONTHS = r13_FD_19_24_MONTHS; }
		public String getR13_FD_OVER_24_MONTHS() { return R13_FD_OVER_24_MONTHS; }
		public void setR13_FD_OVER_24_MONTHS(String r13_FD_OVER_24_MONTHS) { R13_FD_OVER_24_MONTHS = r13_FD_OVER_24_MONTHS; }
		public String getR13_TOTAL() { return R13_TOTAL; }
		public void setR13_TOTAL(String r13_TOTAL) { R13_TOTAL = r13_TOTAL; }
		public String getR14_CURRENCY() { return R14_CURRENCY; }
		public void setR14_CURRENCY(String r14_CURRENCY) { R14_CURRENCY = r14_CURRENCY; }
		public String getR14_CURRENT() { return R14_CURRENT; }
		public void setR14_CURRENT(String r14_CURRENT) { R14_CURRENT = r14_CURRENT; }
		public String getR14_CALL() { return R14_CALL; }
		public void setR14_CALL(String r14_CALL) { R14_CALL = r14_CALL; }
		public String getR14_SAVINGS() { return R14_SAVINGS; }
		public void setR14_SAVINGS(String r14_SAVINGS) { R14_SAVINGS = r14_SAVINGS; }
		public String getR14_NOTICE_0_31_DAYS() { return R14_NOTICE_0_31_DAYS; }
		public void setR14_NOTICE_0_31_DAYS(String r14_NOTICE_0_31_DAYS) { R14_NOTICE_0_31_DAYS = r14_NOTICE_0_31_DAYS; }
		public String getR14_NOTICE_32_88_DAYS() { return R14_NOTICE_32_88_DAYS; }
		public void setR14_NOTICE_32_88_DAYS(String r14_NOTICE_32_88_DAYS) { R14_NOTICE_32_88_DAYS = r14_NOTICE_32_88_DAYS; }
		public String getR14_91_DEPOSIT_DAY() { return R14_91_DEPOSIT_DAY; }
		public void setR14_91_DEPOSIT_DAY(String r14_91_DEPOSIT_DAY) { R14_91_DEPOSIT_DAY = r14_91_DEPOSIT_DAY; }
		public String getR14_FD_1_6_MONTHS() { return R14_FD_1_6_MONTHS; }
		public void setR14_FD_1_6_MONTHS(String r14_FD_1_6_MONTHS) { R14_FD_1_6_MONTHS = r14_FD_1_6_MONTHS; }
		public String getR14_FD_7_12_MONTHS() { return R14_FD_7_12_MONTHS; }
		public void setR14_FD_7_12_MONTHS(String r14_FD_7_12_MONTHS) { R14_FD_7_12_MONTHS = r14_FD_7_12_MONTHS; }
		public String getR14_FD_13_18_MONTHS() { return R14_FD_13_18_MONTHS; }
		public void setR14_FD_13_18_MONTHS(String r14_FD_13_18_MONTHS) { R14_FD_13_18_MONTHS = r14_FD_13_18_MONTHS; }
		public String getR14_FD_19_24_MONTHS() { return R14_FD_19_24_MONTHS; }
		public void setR14_FD_19_24_MONTHS(String r14_FD_19_24_MONTHS) { R14_FD_19_24_MONTHS = r14_FD_19_24_MONTHS; }
		public String getR14_FD_OVER_24_MONTHS() { return R14_FD_OVER_24_MONTHS; }
		public void setR14_FD_OVER_24_MONTHS(String r14_FD_OVER_24_MONTHS) { R14_FD_OVER_24_MONTHS = r14_FD_OVER_24_MONTHS; }
		public String getR14_TOTAL() { return R14_TOTAL; }
		public void setR14_TOTAL(String r14_TOTAL) { R14_TOTAL = r14_TOTAL; }
		public String getR15_CURRENCY() { return R15_CURRENCY; }
		public void setR15_CURRENCY(String r15_CURRENCY) { R15_CURRENCY = r15_CURRENCY; }
		public String getR15_CURRENT() { return R15_CURRENT; }
		public void setR15_CURRENT(String r15_CURRENT) { R15_CURRENT = r15_CURRENT; }
		public String getR15_CALL() { return R15_CALL; }
		public void setR15_CALL(String r15_CALL) { R15_CALL = r15_CALL; }
		public String getR15_SAVINGS() { return R15_SAVINGS; }
		public void setR15_SAVINGS(String r15_SAVINGS) { R15_SAVINGS = r15_SAVINGS; }
		public String getR15_NOTICE_0_31_DAYS() { return R15_NOTICE_0_31_DAYS; }
		public void setR15_NOTICE_0_31_DAYS(String r15_NOTICE_0_31_DAYS) { R15_NOTICE_0_31_DAYS = r15_NOTICE_0_31_DAYS; }
		public String getR15_NOTICE_32_88_DAYS() { return R15_NOTICE_32_88_DAYS; }
		public void setR15_NOTICE_32_88_DAYS(String r15_NOTICE_32_88_DAYS) { R15_NOTICE_32_88_DAYS = r15_NOTICE_32_88_DAYS; }
		public String getR15_91_DEPOSIT_DAY() { return R15_91_DEPOSIT_DAY; }
		public void setR15_91_DEPOSIT_DAY(String r15_91_DEPOSIT_DAY) { R15_91_DEPOSIT_DAY = r15_91_DEPOSIT_DAY; }
		public String getR15_FD_1_6_MONTHS() { return R15_FD_1_6_MONTHS; }
		public void setR15_FD_1_6_MONTHS(String r15_FD_1_6_MONTHS) { R15_FD_1_6_MONTHS = r15_FD_1_6_MONTHS; }
		public String getR15_FD_7_12_MONTHS() { return R15_FD_7_12_MONTHS; }
		public void setR15_FD_7_12_MONTHS(String r15_FD_7_12_MONTHS) { R15_FD_7_12_MONTHS = r15_FD_7_12_MONTHS; }
		public String getR15_FD_13_18_MONTHS() { return R15_FD_13_18_MONTHS; }
		public void setR15_FD_13_18_MONTHS(String r15_FD_13_18_MONTHS) { R15_FD_13_18_MONTHS = r15_FD_13_18_MONTHS; }
		public String getR15_FD_19_24_MONTHS() { return R15_FD_19_24_MONTHS; }
		public void setR15_FD_19_24_MONTHS(String r15_FD_19_24_MONTHS) { R15_FD_19_24_MONTHS = r15_FD_19_24_MONTHS; }
		public String getR15_FD_OVER_24_MONTHS() { return R15_FD_OVER_24_MONTHS; }
		public void setR15_FD_OVER_24_MONTHS(String r15_FD_OVER_24_MONTHS) { R15_FD_OVER_24_MONTHS = r15_FD_OVER_24_MONTHS; }
		public String getR15_TOTAL() { return R15_TOTAL; }
		public void setR15_TOTAL(String r15_TOTAL) { R15_TOTAL = r15_TOTAL; }
		public Date getReport_date() { return report_date; }
		public void setReport_date(Date report_date) { this.report_date = report_date; }
		public BigDecimal getReport_version() { return report_version; }
		public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
		public Date getReportResubDate() { return reportResubDate; }
		public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
		public String getReport_frequency() { return report_frequency; }
		public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
		public String getReport_code() { return report_code; }
		public void setReport_code(String report_code) { this.report_code = report_code; }
		public String getReport_desc() { return report_desc; }
		public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
		public String getEntity_flg() { return entity_flg; }
		public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
		public String getModify_flg() { return modify_flg; }
		public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
		public String getDel_flg() { return del_flg; }
		public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	}

	// =========================================================
	// Inner entity: M_INT_RATES_FCA_RESUB_Detail_Entity
	// =========================================================
	public static class M_INT_RATES_FCA_RESUB_Detail_Entity {
		private String R10_CURRENCY;
		private String R10_CURRENT;
		private String R10_CALL;
		private String R10_SAVINGS;
		private String R10_NOTICE_0_31_DAYS;
		private String R10_NOTICE_32_88_DAYS;
		private String R10_91_DEPOSIT_DAY;
		private String R10_FD_1_6_MONTHS;
		private String R10_FD_7_12_MONTHS;
		private String R10_FD_13_18_MONTHS;
		private String R10_FD_19_24_MONTHS;
		private String R10_FD_OVER_24_MONTHS;
		private String R10_TOTAL;
		private String R11_CURRENCY;
		private String R11_CURRENT;
		private String R11_CALL;
		private String R11_SAVINGS;
		private String R11_NOTICE_0_31_DAYS;
		private String R11_NOTICE_32_88_DAYS;
		private String R11_91_DEPOSIT_DAY;
		private String R11_FD_1_6_MONTHS;
		private String R11_FD_7_12_MONTHS;
		private String R11_FD_13_18_MONTHS;
		private String R11_FD_19_24_MONTHS;
		private String R11_FD_OVER_24_MONTHS;
		private String R11_TOTAL;
		private String R12_CURRENCY;
		private String R12_CURRENT;
		private String R12_CALL;
		private String R12_SAVINGS;
		private String R12_NOTICE_0_31_DAYS;
		private String R12_NOTICE_32_88_DAYS;
		private String R12_91_DEPOSIT_DAY;
		private String R12_FD_1_6_MONTHS;
		private String R12_FD_7_12_MONTHS;
		private String R12_FD_13_18_MONTHS;
		private String R12_FD_19_24_MONTHS;
		private String R12_FD_OVER_24_MONTHS;
		private String R12_TOTAL;
		private String R13_CURRENCY;
		private String R13_CURRENT;
		private String R13_CALL;
		private String R13_SAVINGS;
		private String R13_NOTICE_0_31_DAYS;
		private String R13_NOTICE_32_88_DAYS;
		private String R13_91_DEPOSIT_DAY;
		private String R13_FD_1_6_MONTHS;
		private String R13_FD_7_12_MONTHS;
		private String R13_FD_13_18_MONTHS;
		private String R13_FD_19_24_MONTHS;
		private String R13_FD_OVER_24_MONTHS;
		private String R13_TOTAL;
		private String R14_CURRENCY;
		private String R14_CURRENT;
		private String R14_CALL;
		private String R14_SAVINGS;
		private String R14_NOTICE_0_31_DAYS;
		private String R14_NOTICE_32_88_DAYS;
		private String R14_91_DEPOSIT_DAY;
		private String R14_FD_1_6_MONTHS;
		private String R14_FD_7_12_MONTHS;
		private String R14_FD_13_18_MONTHS;
		private String R14_FD_19_24_MONTHS;
		private String R14_FD_OVER_24_MONTHS;
		private String R14_TOTAL;
		private String R15_CURRENCY;
		private String R15_CURRENT;
		private String R15_CALL;
		private String R15_SAVINGS;
		private String R15_NOTICE_0_31_DAYS;
		private String R15_NOTICE_32_88_DAYS;
		private String R15_91_DEPOSIT_DAY;
		private String R15_FD_1_6_MONTHS;
		private String R15_FD_7_12_MONTHS;
		private String R15_FD_13_18_MONTHS;
		private String R15_FD_19_24_MONTHS;
		private String R15_FD_OVER_24_MONTHS;
		private String R15_TOTAL;

		private Date report_date;
		private BigDecimal report_version;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_CURRENCY() { return R10_CURRENCY; }
		public void setR10_CURRENCY(String r10_CURRENCY) { R10_CURRENCY = r10_CURRENCY; }
		public String getR10_CURRENT() { return R10_CURRENT; }
		public void setR10_CURRENT(String r10_CURRENT) { R10_CURRENT = r10_CURRENT; }
		public String getR10_CALL() { return R10_CALL; }
		public void setR10_CALL(String r10_CALL) { R10_CALL = r10_CALL; }
		public String getR10_SAVINGS() { return R10_SAVINGS; }
		public void setR10_SAVINGS(String r10_SAVINGS) { R10_SAVINGS = r10_SAVINGS; }
		public String getR10_NOTICE_0_31_DAYS() { return R10_NOTICE_0_31_DAYS; }
		public void setR10_NOTICE_0_31_DAYS(String r10_NOTICE_0_31_DAYS) { R10_NOTICE_0_31_DAYS = r10_NOTICE_0_31_DAYS; }
		public String getR10_NOTICE_32_88_DAYS() { return R10_NOTICE_32_88_DAYS; }
		public void setR10_NOTICE_32_88_DAYS(String r10_NOTICE_32_88_DAYS) { R10_NOTICE_32_88_DAYS = r10_NOTICE_32_88_DAYS; }
		public String getR10_91_DEPOSIT_DAY() { return R10_91_DEPOSIT_DAY; }
		public void setR10_91_DEPOSIT_DAY(String r10_91_DEPOSIT_DAY) { R10_91_DEPOSIT_DAY = r10_91_DEPOSIT_DAY; }
		public String getR10_FD_1_6_MONTHS() { return R10_FD_1_6_MONTHS; }
		public void setR10_FD_1_6_MONTHS(String r10_FD_1_6_MONTHS) { R10_FD_1_6_MONTHS = r10_FD_1_6_MONTHS; }
		public String getR10_FD_7_12_MONTHS() { return R10_FD_7_12_MONTHS; }
		public void setR10_FD_7_12_MONTHS(String r10_FD_7_12_MONTHS) { R10_FD_7_12_MONTHS = r10_FD_7_12_MONTHS; }
		public String getR10_FD_13_18_MONTHS() { return R10_FD_13_18_MONTHS; }
		public void setR10_FD_13_18_MONTHS(String r10_FD_13_18_MONTHS) { R10_FD_13_18_MONTHS = r10_FD_13_18_MONTHS; }
		public String getR10_FD_19_24_MONTHS() { return R10_FD_19_24_MONTHS; }
		public void setR10_FD_19_24_MONTHS(String r10_FD_19_24_MONTHS) { R10_FD_19_24_MONTHS = r10_FD_19_24_MONTHS; }
		public String getR10_FD_OVER_24_MONTHS() { return R10_FD_OVER_24_MONTHS; }
		public void setR10_FD_OVER_24_MONTHS(String r10_FD_OVER_24_MONTHS) { R10_FD_OVER_24_MONTHS = r10_FD_OVER_24_MONTHS; }
		public String getR10_TOTAL() { return R10_TOTAL; }
		public void setR10_TOTAL(String r10_TOTAL) { R10_TOTAL = r10_TOTAL; }
		public String getR11_CURRENCY() { return R11_CURRENCY; }
		public void setR11_CURRENCY(String r11_CURRENCY) { R11_CURRENCY = r11_CURRENCY; }
		public String getR11_CURRENT() { return R11_CURRENT; }
		public void setR11_CURRENT(String r11_CURRENT) { R11_CURRENT = r11_CURRENT; }
		public String getR11_CALL() { return R11_CALL; }
		public void setR11_CALL(String r11_CALL) { R11_CALL = r11_CALL; }
		public String getR11_SAVINGS() { return R11_SAVINGS; }
		public void setR11_SAVINGS(String r11_SAVINGS) { R11_SAVINGS = r11_SAVINGS; }
		public String getR11_NOTICE_0_31_DAYS() { return R11_NOTICE_0_31_DAYS; }
		public void setR11_NOTICE_0_31_DAYS(String r11_NOTICE_0_31_DAYS) { R11_NOTICE_0_31_DAYS = r11_NOTICE_0_31_DAYS; }
		public String getR11_NOTICE_32_88_DAYS() { return R11_NOTICE_32_88_DAYS; }
		public void setR11_NOTICE_32_88_DAYS(String r11_NOTICE_32_88_DAYS) { R11_NOTICE_32_88_DAYS = r11_NOTICE_32_88_DAYS; }
		public String getR11_91_DEPOSIT_DAY() { return R11_91_DEPOSIT_DAY; }
		public void setR11_91_DEPOSIT_DAY(String r11_91_DEPOSIT_DAY) { R11_91_DEPOSIT_DAY = r11_91_DEPOSIT_DAY; }
		public String getR11_FD_1_6_MONTHS() { return R11_FD_1_6_MONTHS; }
		public void setR11_FD_1_6_MONTHS(String r11_FD_1_6_MONTHS) { R11_FD_1_6_MONTHS = r11_FD_1_6_MONTHS; }
		public String getR11_FD_7_12_MONTHS() { return R11_FD_7_12_MONTHS; }
		public void setR11_FD_7_12_MONTHS(String r11_FD_7_12_MONTHS) { R11_FD_7_12_MONTHS = r11_FD_7_12_MONTHS; }
		public String getR11_FD_13_18_MONTHS() { return R11_FD_13_18_MONTHS; }
		public void setR11_FD_13_18_MONTHS(String r11_FD_13_18_MONTHS) { R11_FD_13_18_MONTHS = r11_FD_13_18_MONTHS; }
		public String getR11_FD_19_24_MONTHS() { return R11_FD_19_24_MONTHS; }
		public void setR11_FD_19_24_MONTHS(String r11_FD_19_24_MONTHS) { R11_FD_19_24_MONTHS = r11_FD_19_24_MONTHS; }
		public String getR11_FD_OVER_24_MONTHS() { return R11_FD_OVER_24_MONTHS; }
		public void setR11_FD_OVER_24_MONTHS(String r11_FD_OVER_24_MONTHS) { R11_FD_OVER_24_MONTHS = r11_FD_OVER_24_MONTHS; }
		public String getR11_TOTAL() { return R11_TOTAL; }
		public void setR11_TOTAL(String r11_TOTAL) { R11_TOTAL = r11_TOTAL; }
		public String getR12_CURRENCY() { return R12_CURRENCY; }
		public void setR12_CURRENCY(String r12_CURRENCY) { R12_CURRENCY = r12_CURRENCY; }
		public String getR12_CURRENT() { return R12_CURRENT; }
		public void setR12_CURRENT(String r12_CURRENT) { R12_CURRENT = r12_CURRENT; }
		public String getR12_CALL() { return R12_CALL; }
		public void setR12_CALL(String r12_CALL) { R12_CALL = r12_CALL; }
		public String getR12_SAVINGS() { return R12_SAVINGS; }
		public void setR12_SAVINGS(String r12_SAVINGS) { R12_SAVINGS = r12_SAVINGS; }
		public String getR12_NOTICE_0_31_DAYS() { return R12_NOTICE_0_31_DAYS; }
		public void setR12_NOTICE_0_31_DAYS(String r12_NOTICE_0_31_DAYS) { R12_NOTICE_0_31_DAYS = r12_NOTICE_0_31_DAYS; }
		public String getR12_NOTICE_32_88_DAYS() { return R12_NOTICE_32_88_DAYS; }
		public void setR12_NOTICE_32_88_DAYS(String r12_NOTICE_32_88_DAYS) { R12_NOTICE_32_88_DAYS = r12_NOTICE_32_88_DAYS; }
		public String getR12_91_DEPOSIT_DAY() { return R12_91_DEPOSIT_DAY; }
		public void setR12_91_DEPOSIT_DAY(String r12_91_DEPOSIT_DAY) { R12_91_DEPOSIT_DAY = r12_91_DEPOSIT_DAY; }
		public String getR12_FD_1_6_MONTHS() { return R12_FD_1_6_MONTHS; }
		public void setR12_FD_1_6_MONTHS(String r12_FD_1_6_MONTHS) { R12_FD_1_6_MONTHS = r12_FD_1_6_MONTHS; }
		public String getR12_FD_7_12_MONTHS() { return R12_FD_7_12_MONTHS; }
		public void setR12_FD_7_12_MONTHS(String r12_FD_7_12_MONTHS) { R12_FD_7_12_MONTHS = r12_FD_7_12_MONTHS; }
		public String getR12_FD_13_18_MONTHS() { return R12_FD_13_18_MONTHS; }
		public void setR12_FD_13_18_MONTHS(String r12_FD_13_18_MONTHS) { R12_FD_13_18_MONTHS = r12_FD_13_18_MONTHS; }
		public String getR12_FD_19_24_MONTHS() { return R12_FD_19_24_MONTHS; }
		public void setR12_FD_19_24_MONTHS(String r12_FD_19_24_MONTHS) { R12_FD_19_24_MONTHS = r12_FD_19_24_MONTHS; }
		public String getR12_FD_OVER_24_MONTHS() { return R12_FD_OVER_24_MONTHS; }
		public void setR12_FD_OVER_24_MONTHS(String r12_FD_OVER_24_MONTHS) { R12_FD_OVER_24_MONTHS = r12_FD_OVER_24_MONTHS; }
		public String getR12_TOTAL() { return R12_TOTAL; }
		public void setR12_TOTAL(String r12_TOTAL) { R12_TOTAL = r12_TOTAL; }
		public String getR13_CURRENCY() { return R13_CURRENCY; }
		public void setR13_CURRENCY(String r13_CURRENCY) { R13_CURRENCY = r13_CURRENCY; }
		public String getR13_CURRENT() { return R13_CURRENT; }
		public void setR13_CURRENT(String r13_CURRENT) { R13_CURRENT = r13_CURRENT; }
		public String getR13_CALL() { return R13_CALL; }
		public void setR13_CALL(String r13_CALL) { R13_CALL = r13_CALL; }
		public String getR13_SAVINGS() { return R13_SAVINGS; }
		public void setR13_SAVINGS(String r13_SAVINGS) { R13_SAVINGS = r13_SAVINGS; }
		public String getR13_NOTICE_0_31_DAYS() { return R13_NOTICE_0_31_DAYS; }
		public void setR13_NOTICE_0_31_DAYS(String r13_NOTICE_0_31_DAYS) { R13_NOTICE_0_31_DAYS = r13_NOTICE_0_31_DAYS; }
		public String getR13_NOTICE_32_88_DAYS() { return R13_NOTICE_32_88_DAYS; }
		public void setR13_NOTICE_32_88_DAYS(String r13_NOTICE_32_88_DAYS) { R13_NOTICE_32_88_DAYS = r13_NOTICE_32_88_DAYS; }
		public String getR13_91_DEPOSIT_DAY() { return R13_91_DEPOSIT_DAY; }
		public void setR13_91_DEPOSIT_DAY(String r13_91_DEPOSIT_DAY) { R13_91_DEPOSIT_DAY = r13_91_DEPOSIT_DAY; }
		public String getR13_FD_1_6_MONTHS() { return R13_FD_1_6_MONTHS; }
		public void setR13_FD_1_6_MONTHS(String r13_FD_1_6_MONTHS) { R13_FD_1_6_MONTHS = r13_FD_1_6_MONTHS; }
		public String getR13_FD_7_12_MONTHS() { return R13_FD_7_12_MONTHS; }
		public void setR13_FD_7_12_MONTHS(String r13_FD_7_12_MONTHS) { R13_FD_7_12_MONTHS = r13_FD_7_12_MONTHS; }
		public String getR13_FD_13_18_MONTHS() { return R13_FD_13_18_MONTHS; }
		public void setR13_FD_13_18_MONTHS(String r13_FD_13_18_MONTHS) { R13_FD_13_18_MONTHS = r13_FD_13_18_MONTHS; }
		public String getR13_FD_19_24_MONTHS() { return R13_FD_19_24_MONTHS; }
		public void setR13_FD_19_24_MONTHS(String r13_FD_19_24_MONTHS) { R13_FD_19_24_MONTHS = r13_FD_19_24_MONTHS; }
		public String getR13_FD_OVER_24_MONTHS() { return R13_FD_OVER_24_MONTHS; }
		public void setR13_FD_OVER_24_MONTHS(String r13_FD_OVER_24_MONTHS) { R13_FD_OVER_24_MONTHS = r13_FD_OVER_24_MONTHS; }
		public String getR13_TOTAL() { return R13_TOTAL; }
		public void setR13_TOTAL(String r13_TOTAL) { R13_TOTAL = r13_TOTAL; }
		public String getR14_CURRENCY() { return R14_CURRENCY; }
		public void setR14_CURRENCY(String r14_CURRENCY) { R14_CURRENCY = r14_CURRENCY; }
		public String getR14_CURRENT() { return R14_CURRENT; }
		public void setR14_CURRENT(String r14_CURRENT) { R14_CURRENT = r14_CURRENT; }
		public String getR14_CALL() { return R14_CALL; }
		public void setR14_CALL(String r14_CALL) { R14_CALL = r14_CALL; }
		public String getR14_SAVINGS() { return R14_SAVINGS; }
		public void setR14_SAVINGS(String r14_SAVINGS) { R14_SAVINGS = r14_SAVINGS; }
		public String getR14_NOTICE_0_31_DAYS() { return R14_NOTICE_0_31_DAYS; }
		public void setR14_NOTICE_0_31_DAYS(String r14_NOTICE_0_31_DAYS) { R14_NOTICE_0_31_DAYS = r14_NOTICE_0_31_DAYS; }
		public String getR14_NOTICE_32_88_DAYS() { return R14_NOTICE_32_88_DAYS; }
		public void setR14_NOTICE_32_88_DAYS(String r14_NOTICE_32_88_DAYS) { R14_NOTICE_32_88_DAYS = r14_NOTICE_32_88_DAYS; }
		public String getR14_91_DEPOSIT_DAY() { return R14_91_DEPOSIT_DAY; }
		public void setR14_91_DEPOSIT_DAY(String r14_91_DEPOSIT_DAY) { R14_91_DEPOSIT_DAY = r14_91_DEPOSIT_DAY; }
		public String getR14_FD_1_6_MONTHS() { return R14_FD_1_6_MONTHS; }
		public void setR14_FD_1_6_MONTHS(String r14_FD_1_6_MONTHS) { R14_FD_1_6_MONTHS = r14_FD_1_6_MONTHS; }
		public String getR14_FD_7_12_MONTHS() { return R14_FD_7_12_MONTHS; }
		public void setR14_FD_7_12_MONTHS(String r14_FD_7_12_MONTHS) { R14_FD_7_12_MONTHS = r14_FD_7_12_MONTHS; }
		public String getR14_FD_13_18_MONTHS() { return R14_FD_13_18_MONTHS; }
		public void setR14_FD_13_18_MONTHS(String r14_FD_13_18_MONTHS) { R14_FD_13_18_MONTHS = r14_FD_13_18_MONTHS; }
		public String getR14_FD_19_24_MONTHS() { return R14_FD_19_24_MONTHS; }
		public void setR14_FD_19_24_MONTHS(String r14_FD_19_24_MONTHS) { R14_FD_19_24_MONTHS = r14_FD_19_24_MONTHS; }
		public String getR14_FD_OVER_24_MONTHS() { return R14_FD_OVER_24_MONTHS; }
		public void setR14_FD_OVER_24_MONTHS(String r14_FD_OVER_24_MONTHS) { R14_FD_OVER_24_MONTHS = r14_FD_OVER_24_MONTHS; }
		public String getR14_TOTAL() { return R14_TOTAL; }
		public void setR14_TOTAL(String r14_TOTAL) { R14_TOTAL = r14_TOTAL; }
		public String getR15_CURRENCY() { return R15_CURRENCY; }
		public void setR15_CURRENCY(String r15_CURRENCY) { R15_CURRENCY = r15_CURRENCY; }
		public String getR15_CURRENT() { return R15_CURRENT; }
		public void setR15_CURRENT(String r15_CURRENT) { R15_CURRENT = r15_CURRENT; }
		public String getR15_CALL() { return R15_CALL; }
		public void setR15_CALL(String r15_CALL) { R15_CALL = r15_CALL; }
		public String getR15_SAVINGS() { return R15_SAVINGS; }
		public void setR15_SAVINGS(String r15_SAVINGS) { R15_SAVINGS = r15_SAVINGS; }
		public String getR15_NOTICE_0_31_DAYS() { return R15_NOTICE_0_31_DAYS; }
		public void setR15_NOTICE_0_31_DAYS(String r15_NOTICE_0_31_DAYS) { R15_NOTICE_0_31_DAYS = r15_NOTICE_0_31_DAYS; }
		public String getR15_NOTICE_32_88_DAYS() { return R15_NOTICE_32_88_DAYS; }
		public void setR15_NOTICE_32_88_DAYS(String r15_NOTICE_32_88_DAYS) { R15_NOTICE_32_88_DAYS = r15_NOTICE_32_88_DAYS; }
		public String getR15_91_DEPOSIT_DAY() { return R15_91_DEPOSIT_DAY; }
		public void setR15_91_DEPOSIT_DAY(String r15_91_DEPOSIT_DAY) { R15_91_DEPOSIT_DAY = r15_91_DEPOSIT_DAY; }
		public String getR15_FD_1_6_MONTHS() { return R15_FD_1_6_MONTHS; }
		public void setR15_FD_1_6_MONTHS(String r15_FD_1_6_MONTHS) { R15_FD_1_6_MONTHS = r15_FD_1_6_MONTHS; }
		public String getR15_FD_7_12_MONTHS() { return R15_FD_7_12_MONTHS; }
		public void setR15_FD_7_12_MONTHS(String r15_FD_7_12_MONTHS) { R15_FD_7_12_MONTHS = r15_FD_7_12_MONTHS; }
		public String getR15_FD_13_18_MONTHS() { return R15_FD_13_18_MONTHS; }
		public void setR15_FD_13_18_MONTHS(String r15_FD_13_18_MONTHS) { R15_FD_13_18_MONTHS = r15_FD_13_18_MONTHS; }
		public String getR15_FD_19_24_MONTHS() { return R15_FD_19_24_MONTHS; }
		public void setR15_FD_19_24_MONTHS(String r15_FD_19_24_MONTHS) { R15_FD_19_24_MONTHS = r15_FD_19_24_MONTHS; }
		public String getR15_FD_OVER_24_MONTHS() { return R15_FD_OVER_24_MONTHS; }
		public void setR15_FD_OVER_24_MONTHS(String r15_FD_OVER_24_MONTHS) { R15_FD_OVER_24_MONTHS = r15_FD_OVER_24_MONTHS; }
		public String getR15_TOTAL() { return R15_TOTAL; }
		public void setR15_TOTAL(String r15_TOTAL) { R15_TOTAL = r15_TOTAL; }
		public Date getReport_date() { return report_date; }
		public void setReport_date(Date report_date) { this.report_date = report_date; }
		public BigDecimal getReport_version() { return report_version; }
		public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
		public Date getReportResubDate() { return reportResubDate; }
		public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
		public String getReport_frequency() { return report_frequency; }
		public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
		public String getReport_code() { return report_code; }
		public void setReport_code(String report_code) { this.report_code = report_code; }
		public String getReport_desc() { return report_desc; }
		public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
		public String getEntity_flg() { return entity_flg; }
		public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
		public String getModify_flg() { return modify_flg; }
		public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
		public String getDel_flg() { return del_flg; }
		public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	}

	class M_INT_RATES_FCASummaryRowMapper implements RowMapper<M_INT_RATES_FCA_Summary_Entity> {
		@Override
		public M_INT_RATES_FCA_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_INT_RATES_FCA_Summary_Entity obj = new M_INT_RATES_FCA_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			obj.setR10_CURRENCY(rs.getString("R10_CURRENCY")); obj.setR10_CURRENT(rs.getString("R10_CURRENT"));
			obj.setR10_CALL(rs.getString("R10_CALL")); obj.setR10_SAVINGS(rs.getString("R10_SAVINGS"));
			obj.setR10_NOTICE_0_31_DAYS(rs.getString("R10_NOTICE_0_31_DAYS")); obj.setR10_NOTICE_32_88_DAYS(rs.getString("R10_NOTICE_32_88_DAYS"));
			obj.setR10_91_DEPOSIT_DAY(rs.getString("R10_91_DEPOSIT_DAY")); obj.setR10_FD_1_6_MONTHS(rs.getString("R10_FD_1_6_MONTHS"));
			obj.setR10_FD_7_12_MONTHS(rs.getString("R10_FD_7_12_MONTHS")); obj.setR10_FD_13_18_MONTHS(rs.getString("R10_FD_13_18_MONTHS"));
			obj.setR10_FD_19_24_MONTHS(rs.getString("R10_FD_19_24_MONTHS")); obj.setR10_FD_OVER_24_MONTHS(rs.getString("R10_FD_OVER_24_MONTHS"));
			obj.setR10_TOTAL(rs.getString("R10_TOTAL"));

			obj.setR11_CURRENCY(rs.getString("R11_CURRENCY")); obj.setR11_CURRENT(rs.getString("R11_CURRENT"));
			obj.setR11_CALL(rs.getString("R11_CALL")); obj.setR11_SAVINGS(rs.getString("R11_SAVINGS"));
			obj.setR11_NOTICE_0_31_DAYS(rs.getString("R11_NOTICE_0_31_DAYS")); obj.setR11_NOTICE_32_88_DAYS(rs.getString("R11_NOTICE_32_88_DAYS"));
			obj.setR11_91_DEPOSIT_DAY(rs.getString("R11_91_DEPOSIT_DAY")); obj.setR11_FD_1_6_MONTHS(rs.getString("R11_FD_1_6_MONTHS"));
			obj.setR11_FD_7_12_MONTHS(rs.getString("R11_FD_7_12_MONTHS")); obj.setR11_FD_13_18_MONTHS(rs.getString("R11_FD_13_18_MONTHS"));
			obj.setR11_FD_19_24_MONTHS(rs.getString("R11_FD_19_24_MONTHS")); obj.setR11_FD_OVER_24_MONTHS(rs.getString("R11_FD_OVER_24_MONTHS"));
			obj.setR11_TOTAL(rs.getString("R11_TOTAL"));

			obj.setR12_CURRENCY(rs.getString("R12_CURRENCY")); obj.setR12_CURRENT(rs.getString("R12_CURRENT"));
			obj.setR12_CALL(rs.getString("R12_CALL")); obj.setR12_SAVINGS(rs.getString("R12_SAVINGS"));
			obj.setR12_NOTICE_0_31_DAYS(rs.getString("R12_NOTICE_0_31_DAYS")); obj.setR12_NOTICE_32_88_DAYS(rs.getString("R12_NOTICE_32_88_DAYS"));
			obj.setR12_91_DEPOSIT_DAY(rs.getString("R12_91_DEPOSIT_DAY")); obj.setR12_FD_1_6_MONTHS(rs.getString("R12_FD_1_6_MONTHS"));
			obj.setR12_FD_7_12_MONTHS(rs.getString("R12_FD_7_12_MONTHS")); obj.setR12_FD_13_18_MONTHS(rs.getString("R12_FD_13_18_MONTHS"));
			obj.setR12_FD_19_24_MONTHS(rs.getString("R12_FD_19_24_MONTHS")); obj.setR12_FD_OVER_24_MONTHS(rs.getString("R12_FD_OVER_24_MONTHS"));
			obj.setR12_TOTAL(rs.getString("R12_TOTAL"));

			obj.setR13_CURRENCY(rs.getString("R13_CURRENCY")); obj.setR13_CURRENT(rs.getString("R13_CURRENT"));
			obj.setR13_CALL(rs.getString("R13_CALL")); obj.setR13_SAVINGS(rs.getString("R13_SAVINGS"));
			obj.setR13_NOTICE_0_31_DAYS(rs.getString("R13_NOTICE_0_31_DAYS")); obj.setR13_NOTICE_32_88_DAYS(rs.getString("R13_NOTICE_32_88_DAYS"));
			obj.setR13_91_DEPOSIT_DAY(rs.getString("R13_91_DEPOSIT_DAY")); obj.setR13_FD_1_6_MONTHS(rs.getString("R13_FD_1_6_MONTHS"));
			obj.setR13_FD_7_12_MONTHS(rs.getString("R13_FD_7_12_MONTHS")); obj.setR13_FD_13_18_MONTHS(rs.getString("R13_FD_13_18_MONTHS"));
			obj.setR13_FD_19_24_MONTHS(rs.getString("R13_FD_19_24_MONTHS")); obj.setR13_FD_OVER_24_MONTHS(rs.getString("R13_FD_OVER_24_MONTHS"));
			obj.setR13_TOTAL(rs.getString("R13_TOTAL"));

			obj.setR14_CURRENCY(rs.getString("R14_CURRENCY")); obj.setR14_CURRENT(rs.getString("R14_CURRENT"));
			obj.setR14_CALL(rs.getString("R14_CALL")); obj.setR14_SAVINGS(rs.getString("R14_SAVINGS"));
			obj.setR14_NOTICE_0_31_DAYS(rs.getString("R14_NOTICE_0_31_DAYS")); obj.setR14_NOTICE_32_88_DAYS(rs.getString("R14_NOTICE_32_88_DAYS"));
			obj.setR14_91_DEPOSIT_DAY(rs.getString("R14_91_DEPOSIT_DAY")); obj.setR14_FD_1_6_MONTHS(rs.getString("R14_FD_1_6_MONTHS"));
			obj.setR14_FD_7_12_MONTHS(rs.getString("R14_FD_7_12_MONTHS")); obj.setR14_FD_13_18_MONTHS(rs.getString("R14_FD_13_18_MONTHS"));
			obj.setR14_FD_19_24_MONTHS(rs.getString("R14_FD_19_24_MONTHS")); obj.setR14_FD_OVER_24_MONTHS(rs.getString("R14_FD_OVER_24_MONTHS"));
			obj.setR14_TOTAL(rs.getString("R14_TOTAL"));

			obj.setR15_CURRENCY(rs.getString("R15_CURRENCY")); obj.setR15_CURRENT(rs.getString("R15_CURRENT"));
			obj.setR15_CALL(rs.getString("R15_CALL")); obj.setR15_SAVINGS(rs.getString("R15_SAVINGS"));
			obj.setR15_NOTICE_0_31_DAYS(rs.getString("R15_NOTICE_0_31_DAYS")); obj.setR15_NOTICE_32_88_DAYS(rs.getString("R15_NOTICE_32_88_DAYS"));
			obj.setR15_91_DEPOSIT_DAY(rs.getString("R15_91_DEPOSIT_DAY")); obj.setR15_FD_1_6_MONTHS(rs.getString("R15_FD_1_6_MONTHS"));
			obj.setR15_FD_7_12_MONTHS(rs.getString("R15_FD_7_12_MONTHS")); obj.setR15_FD_13_18_MONTHS(rs.getString("R15_FD_13_18_MONTHS"));
			obj.setR15_FD_19_24_MONTHS(rs.getString("R15_FD_19_24_MONTHS")); obj.setR15_FD_OVER_24_MONTHS(rs.getString("R15_FD_OVER_24_MONTHS"));
			obj.setR15_TOTAL(rs.getString("R15_TOTAL"));

			return obj;
		}
	}

	class M_INT_RATES_FCADetailRowMapper implements RowMapper<M_INT_RATES_FCA_Detail_Entity> {
		@Override
		public M_INT_RATES_FCA_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_INT_RATES_FCA_Detail_Entity obj = new M_INT_RATES_FCA_Detail_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			obj.setR10_CURRENCY(rs.getString("R10_CURRENCY")); obj.setR10_CURRENT(rs.getString("R10_CURRENT"));
			obj.setR10_CALL(rs.getString("R10_CALL")); obj.setR10_SAVINGS(rs.getString("R10_SAVINGS"));
			obj.setR10_NOTICE_0_31_DAYS(rs.getString("R10_NOTICE_0_31_DAYS")); obj.setR10_NOTICE_32_88_DAYS(rs.getString("R10_NOTICE_32_88_DAYS"));
			obj.setR10_91_DEPOSIT_DAY(rs.getString("R10_91_DEPOSIT_DAY")); obj.setR10_FD_1_6_MONTHS(rs.getString("R10_FD_1_6_MONTHS"));
			obj.setR10_FD_7_12_MONTHS(rs.getString("R10_FD_7_12_MONTHS")); obj.setR10_FD_13_18_MONTHS(rs.getString("R10_FD_13_18_MONTHS"));
			obj.setR10_FD_19_24_MONTHS(rs.getString("R10_FD_19_24_MONTHS")); obj.setR10_FD_OVER_24_MONTHS(rs.getString("R10_FD_OVER_24_MONTHS"));
			obj.setR10_TOTAL(rs.getString("R10_TOTAL"));

			obj.setR11_CURRENCY(rs.getString("R11_CURRENCY")); obj.setR11_CURRENT(rs.getString("R11_CURRENT"));
			obj.setR11_CALL(rs.getString("R11_CALL")); obj.setR11_SAVINGS(rs.getString("R11_SAVINGS"));
			obj.setR11_NOTICE_0_31_DAYS(rs.getString("R11_NOTICE_0_31_DAYS")); obj.setR11_NOTICE_32_88_DAYS(rs.getString("R11_NOTICE_32_88_DAYS"));
			obj.setR11_91_DEPOSIT_DAY(rs.getString("R11_91_DEPOSIT_DAY")); obj.setR11_FD_1_6_MONTHS(rs.getString("R11_FD_1_6_MONTHS"));
			obj.setR11_FD_7_12_MONTHS(rs.getString("R11_FD_7_12_MONTHS")); obj.setR11_FD_13_18_MONTHS(rs.getString("R11_FD_13_18_MONTHS"));
			obj.setR11_FD_19_24_MONTHS(rs.getString("R11_FD_19_24_MONTHS")); obj.setR11_FD_OVER_24_MONTHS(rs.getString("R11_FD_OVER_24_MONTHS"));
			obj.setR11_TOTAL(rs.getString("R11_TOTAL"));

			obj.setR12_CURRENCY(rs.getString("R12_CURRENCY")); obj.setR12_CURRENT(rs.getString("R12_CURRENT"));
			obj.setR12_CALL(rs.getString("R12_CALL")); obj.setR12_SAVINGS(rs.getString("R12_SAVINGS"));
			obj.setR12_NOTICE_0_31_DAYS(rs.getString("R12_NOTICE_0_31_DAYS")); obj.setR12_NOTICE_32_88_DAYS(rs.getString("R12_NOTICE_32_88_DAYS"));
			obj.setR12_91_DEPOSIT_DAY(rs.getString("R12_91_DEPOSIT_DAY")); obj.setR12_FD_1_6_MONTHS(rs.getString("R12_FD_1_6_MONTHS"));
			obj.setR12_FD_7_12_MONTHS(rs.getString("R12_FD_7_12_MONTHS")); obj.setR12_FD_13_18_MONTHS(rs.getString("R12_FD_13_18_MONTHS"));
			obj.setR12_FD_19_24_MONTHS(rs.getString("R12_FD_19_24_MONTHS")); obj.setR12_FD_OVER_24_MONTHS(rs.getString("R12_FD_OVER_24_MONTHS"));
			obj.setR12_TOTAL(rs.getString("R12_TOTAL"));

			obj.setR13_CURRENCY(rs.getString("R13_CURRENCY")); obj.setR13_CURRENT(rs.getString("R13_CURRENT"));
			obj.setR13_CALL(rs.getString("R13_CALL")); obj.setR13_SAVINGS(rs.getString("R13_SAVINGS"));
			obj.setR13_NOTICE_0_31_DAYS(rs.getString("R13_NOTICE_0_31_DAYS")); obj.setR13_NOTICE_32_88_DAYS(rs.getString("R13_NOTICE_32_88_DAYS"));
			obj.setR13_91_DEPOSIT_DAY(rs.getString("R13_91_DEPOSIT_DAY")); obj.setR13_FD_1_6_MONTHS(rs.getString("R13_FD_1_6_MONTHS"));
			obj.setR13_FD_7_12_MONTHS(rs.getString("R13_FD_7_12_MONTHS")); obj.setR13_FD_13_18_MONTHS(rs.getString("R13_FD_13_18_MONTHS"));
			obj.setR13_FD_19_24_MONTHS(rs.getString("R13_FD_19_24_MONTHS")); obj.setR13_FD_OVER_24_MONTHS(rs.getString("R13_FD_OVER_24_MONTHS"));
			obj.setR13_TOTAL(rs.getString("R13_TOTAL"));

			obj.setR14_CURRENCY(rs.getString("R14_CURRENCY")); obj.setR14_CURRENT(rs.getString("R14_CURRENT"));
			obj.setR14_CALL(rs.getString("R14_CALL")); obj.setR14_SAVINGS(rs.getString("R14_SAVINGS"));
			obj.setR14_NOTICE_0_31_DAYS(rs.getString("R14_NOTICE_0_31_DAYS")); obj.setR14_NOTICE_32_88_DAYS(rs.getString("R14_NOTICE_32_88_DAYS"));
			obj.setR14_91_DEPOSIT_DAY(rs.getString("R14_91_DEPOSIT_DAY")); obj.setR14_FD_1_6_MONTHS(rs.getString("R14_FD_1_6_MONTHS"));
			obj.setR14_FD_7_12_MONTHS(rs.getString("R14_FD_7_12_MONTHS")); obj.setR14_FD_13_18_MONTHS(rs.getString("R14_FD_13_18_MONTHS"));
			obj.setR14_FD_19_24_MONTHS(rs.getString("R14_FD_19_24_MONTHS")); obj.setR14_FD_OVER_24_MONTHS(rs.getString("R14_FD_OVER_24_MONTHS"));
			obj.setR14_TOTAL(rs.getString("R14_TOTAL"));

			obj.setR15_CURRENCY(rs.getString("R15_CURRENCY")); obj.setR15_CURRENT(rs.getString("R15_CURRENT"));
			obj.setR15_CALL(rs.getString("R15_CALL")); obj.setR15_SAVINGS(rs.getString("R15_SAVINGS"));
			obj.setR15_NOTICE_0_31_DAYS(rs.getString("R15_NOTICE_0_31_DAYS")); obj.setR15_NOTICE_32_88_DAYS(rs.getString("R15_NOTICE_32_88_DAYS"));
			obj.setR15_91_DEPOSIT_DAY(rs.getString("R15_91_DEPOSIT_DAY")); obj.setR15_FD_1_6_MONTHS(rs.getString("R15_FD_1_6_MONTHS"));
			obj.setR15_FD_7_12_MONTHS(rs.getString("R15_FD_7_12_MONTHS")); obj.setR15_FD_13_18_MONTHS(rs.getString("R15_FD_13_18_MONTHS"));
			obj.setR15_FD_19_24_MONTHS(rs.getString("R15_FD_19_24_MONTHS")); obj.setR15_FD_OVER_24_MONTHS(rs.getString("R15_FD_OVER_24_MONTHS"));
			obj.setR15_TOTAL(rs.getString("R15_TOTAL"));

			return obj;
		}
	}

	class M_INT_RATES_FCAArchivalSummaryRowMapper implements RowMapper<M_INT_RATES_FCA_Archival_Summary_Entity> {
		@Override
		public M_INT_RATES_FCA_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_INT_RATES_FCA_Archival_Summary_Entity obj = new M_INT_RATES_FCA_Archival_Summary_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			obj.setR10_CURRENCY(rs.getString("R10_CURRENCY")); obj.setR10_CURRENT(rs.getString("R10_CURRENT"));
			obj.setR10_CALL(rs.getString("R10_CALL")); obj.setR10_SAVINGS(rs.getString("R10_SAVINGS"));
			obj.setR10_NOTICE_0_31_DAYS(rs.getString("R10_NOTICE_0_31_DAYS")); obj.setR10_NOTICE_32_88_DAYS(rs.getString("R10_NOTICE_32_88_DAYS"));
			obj.setR10_91_DEPOSIT_DAY(rs.getString("R10_91_DEPOSIT_DAY")); obj.setR10_FD_1_6_MONTHS(rs.getString("R10_FD_1_6_MONTHS"));
			obj.setR10_FD_7_12_MONTHS(rs.getString("R10_FD_7_12_MONTHS")); obj.setR10_FD_13_18_MONTHS(rs.getString("R10_FD_13_18_MONTHS"));
			obj.setR10_FD_19_24_MONTHS(rs.getString("R10_FD_19_24_MONTHS")); obj.setR10_FD_OVER_24_MONTHS(rs.getString("R10_FD_OVER_24_MONTHS"));
			obj.setR10_TOTAL(rs.getString("R10_TOTAL"));

			obj.setR11_CURRENCY(rs.getString("R11_CURRENCY")); obj.setR11_CURRENT(rs.getString("R11_CURRENT"));
			obj.setR11_CALL(rs.getString("R11_CALL")); obj.setR11_SAVINGS(rs.getString("R11_SAVINGS"));
			obj.setR11_NOTICE_0_31_DAYS(rs.getString("R11_NOTICE_0_31_DAYS")); obj.setR11_NOTICE_32_88_DAYS(rs.getString("R11_NOTICE_32_88_DAYS"));
			obj.setR11_91_DEPOSIT_DAY(rs.getString("R11_91_DEPOSIT_DAY")); obj.setR11_FD_1_6_MONTHS(rs.getString("R11_FD_1_6_MONTHS"));
			obj.setR11_FD_7_12_MONTHS(rs.getString("R11_FD_7_12_MONTHS")); obj.setR11_FD_13_18_MONTHS(rs.getString("R11_FD_13_18_MONTHS"));
			obj.setR11_FD_19_24_MONTHS(rs.getString("R11_FD_19_24_MONTHS")); obj.setR11_FD_OVER_24_MONTHS(rs.getString("R11_FD_OVER_24_MONTHS"));
			obj.setR11_TOTAL(rs.getString("R11_TOTAL"));

			obj.setR12_CURRENCY(rs.getString("R12_CURRENCY")); obj.setR12_CURRENT(rs.getString("R12_CURRENT"));
			obj.setR12_CALL(rs.getString("R12_CALL")); obj.setR12_SAVINGS(rs.getString("R12_SAVINGS"));
			obj.setR12_NOTICE_0_31_DAYS(rs.getString("R12_NOTICE_0_31_DAYS")); obj.setR12_NOTICE_32_88_DAYS(rs.getString("R12_NOTICE_32_88_DAYS"));
			obj.setR12_91_DEPOSIT_DAY(rs.getString("R12_91_DEPOSIT_DAY")); obj.setR12_FD_1_6_MONTHS(rs.getString("R12_FD_1_6_MONTHS"));
			obj.setR12_FD_7_12_MONTHS(rs.getString("R12_FD_7_12_MONTHS")); obj.setR12_FD_13_18_MONTHS(rs.getString("R12_FD_13_18_MONTHS"));
			obj.setR12_FD_19_24_MONTHS(rs.getString("R12_FD_19_24_MONTHS")); obj.setR12_FD_OVER_24_MONTHS(rs.getString("R12_FD_OVER_24_MONTHS"));
			obj.setR12_TOTAL(rs.getString("R12_TOTAL"));

			obj.setR13_CURRENCY(rs.getString("R13_CURRENCY")); obj.setR13_CURRENT(rs.getString("R13_CURRENT"));
			obj.setR13_CALL(rs.getString("R13_CALL")); obj.setR13_SAVINGS(rs.getString("R13_SAVINGS"));
			obj.setR13_NOTICE_0_31_DAYS(rs.getString("R13_NOTICE_0_31_DAYS")); obj.setR13_NOTICE_32_88_DAYS(rs.getString("R13_NOTICE_32_88_DAYS"));
			obj.setR13_91_DEPOSIT_DAY(rs.getString("R13_91_DEPOSIT_DAY")); obj.setR13_FD_1_6_MONTHS(rs.getString("R13_FD_1_6_MONTHS"));
			obj.setR13_FD_7_12_MONTHS(rs.getString("R13_FD_7_12_MONTHS")); obj.setR13_FD_13_18_MONTHS(rs.getString("R13_FD_13_18_MONTHS"));
			obj.setR13_FD_19_24_MONTHS(rs.getString("R13_FD_19_24_MONTHS")); obj.setR13_FD_OVER_24_MONTHS(rs.getString("R13_FD_OVER_24_MONTHS"));
			obj.setR13_TOTAL(rs.getString("R13_TOTAL"));

			obj.setR14_CURRENCY(rs.getString("R14_CURRENCY")); obj.setR14_CURRENT(rs.getString("R14_CURRENT"));
			obj.setR14_CALL(rs.getString("R14_CALL")); obj.setR14_SAVINGS(rs.getString("R14_SAVINGS"));
			obj.setR14_NOTICE_0_31_DAYS(rs.getString("R14_NOTICE_0_31_DAYS")); obj.setR14_NOTICE_32_88_DAYS(rs.getString("R14_NOTICE_32_88_DAYS"));
			obj.setR14_91_DEPOSIT_DAY(rs.getString("R14_91_DEPOSIT_DAY")); obj.setR14_FD_1_6_MONTHS(rs.getString("R14_FD_1_6_MONTHS"));
			obj.setR14_FD_7_12_MONTHS(rs.getString("R14_FD_7_12_MONTHS")); obj.setR14_FD_13_18_MONTHS(rs.getString("R14_FD_13_18_MONTHS"));
			obj.setR14_FD_19_24_MONTHS(rs.getString("R14_FD_19_24_MONTHS")); obj.setR14_FD_OVER_24_MONTHS(rs.getString("R14_FD_OVER_24_MONTHS"));
			obj.setR14_TOTAL(rs.getString("R14_TOTAL"));

			obj.setR15_CURRENCY(rs.getString("R15_CURRENCY")); obj.setR15_CURRENT(rs.getString("R15_CURRENT"));
			obj.setR15_CALL(rs.getString("R15_CALL")); obj.setR15_SAVINGS(rs.getString("R15_SAVINGS"));
			obj.setR15_NOTICE_0_31_DAYS(rs.getString("R15_NOTICE_0_31_DAYS")); obj.setR15_NOTICE_32_88_DAYS(rs.getString("R15_NOTICE_32_88_DAYS"));
			obj.setR15_91_DEPOSIT_DAY(rs.getString("R15_91_DEPOSIT_DAY")); obj.setR15_FD_1_6_MONTHS(rs.getString("R15_FD_1_6_MONTHS"));
			obj.setR15_FD_7_12_MONTHS(rs.getString("R15_FD_7_12_MONTHS")); obj.setR15_FD_13_18_MONTHS(rs.getString("R15_FD_13_18_MONTHS"));
			obj.setR15_FD_19_24_MONTHS(rs.getString("R15_FD_19_24_MONTHS")); obj.setR15_FD_OVER_24_MONTHS(rs.getString("R15_FD_OVER_24_MONTHS"));
			obj.setR15_TOTAL(rs.getString("R15_TOTAL"));

			return obj;
		}
	}

	class M_INT_RATES_FCAArchivalDetailRowMapper implements RowMapper<M_INT_RATES_FCA_Archival_Detail_Entity> {
		@Override
		public M_INT_RATES_FCA_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_INT_RATES_FCA_Archival_Detail_Entity obj = new M_INT_RATES_FCA_Archival_Detail_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			obj.setR10_CURRENCY(rs.getString("R10_CURRENCY")); obj.setR10_CURRENT(rs.getString("R10_CURRENT"));
			obj.setR10_CALL(rs.getString("R10_CALL")); obj.setR10_SAVINGS(rs.getString("R10_SAVINGS"));
			obj.setR10_NOTICE_0_31_DAYS(rs.getString("R10_NOTICE_0_31_DAYS")); obj.setR10_NOTICE_32_88_DAYS(rs.getString("R10_NOTICE_32_88_DAYS"));
			obj.setR10_91_DEPOSIT_DAY(rs.getString("R10_91_DEPOSIT_DAY")); obj.setR10_FD_1_6_MONTHS(rs.getString("R10_FD_1_6_MONTHS"));
			obj.setR10_FD_7_12_MONTHS(rs.getString("R10_FD_7_12_MONTHS")); obj.setR10_FD_13_18_MONTHS(rs.getString("R10_FD_13_18_MONTHS"));
			obj.setR10_FD_19_24_MONTHS(rs.getString("R10_FD_19_24_MONTHS")); obj.setR10_FD_OVER_24_MONTHS(rs.getString("R10_FD_OVER_24_MONTHS"));
			obj.setR10_TOTAL(rs.getString("R10_TOTAL"));

			obj.setR11_CURRENCY(rs.getString("R11_CURRENCY")); obj.setR11_CURRENT(rs.getString("R11_CURRENT"));
			obj.setR11_CALL(rs.getString("R11_CALL")); obj.setR11_SAVINGS(rs.getString("R11_SAVINGS"));
			obj.setR11_NOTICE_0_31_DAYS(rs.getString("R11_NOTICE_0_31_DAYS")); obj.setR11_NOTICE_32_88_DAYS(rs.getString("R11_NOTICE_32_88_DAYS"));
			obj.setR11_91_DEPOSIT_DAY(rs.getString("R11_91_DEPOSIT_DAY")); obj.setR11_FD_1_6_MONTHS(rs.getString("R11_FD_1_6_MONTHS"));
			obj.setR11_FD_7_12_MONTHS(rs.getString("R11_FD_7_12_MONTHS")); obj.setR11_FD_13_18_MONTHS(rs.getString("R11_FD_13_18_MONTHS"));
			obj.setR11_FD_19_24_MONTHS(rs.getString("R11_FD_19_24_MONTHS")); obj.setR11_FD_OVER_24_MONTHS(rs.getString("R11_FD_OVER_24_MONTHS"));
			obj.setR11_TOTAL(rs.getString("R11_TOTAL"));

			obj.setR12_CURRENCY(rs.getString("R12_CURRENCY")); obj.setR12_CURRENT(rs.getString("R12_CURRENT"));
			obj.setR12_CALL(rs.getString("R12_CALL")); obj.setR12_SAVINGS(rs.getString("R12_SAVINGS"));
			obj.setR12_NOTICE_0_31_DAYS(rs.getString("R12_NOTICE_0_31_DAYS")); obj.setR12_NOTICE_32_88_DAYS(rs.getString("R12_NOTICE_32_88_DAYS"));
			obj.setR12_91_DEPOSIT_DAY(rs.getString("R12_91_DEPOSIT_DAY")); obj.setR12_FD_1_6_MONTHS(rs.getString("R12_FD_1_6_MONTHS"));
			obj.setR12_FD_7_12_MONTHS(rs.getString("R12_FD_7_12_MONTHS")); obj.setR12_FD_13_18_MONTHS(rs.getString("R12_FD_13_18_MONTHS"));
			obj.setR12_FD_19_24_MONTHS(rs.getString("R12_FD_19_24_MONTHS")); obj.setR12_FD_OVER_24_MONTHS(rs.getString("R12_FD_OVER_24_MONTHS"));
			obj.setR12_TOTAL(rs.getString("R12_TOTAL"));

			obj.setR13_CURRENCY(rs.getString("R13_CURRENCY")); obj.setR13_CURRENT(rs.getString("R13_CURRENT"));
			obj.setR13_CALL(rs.getString("R13_CALL")); obj.setR13_SAVINGS(rs.getString("R13_SAVINGS"));
			obj.setR13_NOTICE_0_31_DAYS(rs.getString("R13_NOTICE_0_31_DAYS")); obj.setR13_NOTICE_32_88_DAYS(rs.getString("R13_NOTICE_32_88_DAYS"));
			obj.setR13_91_DEPOSIT_DAY(rs.getString("R13_91_DEPOSIT_DAY")); obj.setR13_FD_1_6_MONTHS(rs.getString("R13_FD_1_6_MONTHS"));
			obj.setR13_FD_7_12_MONTHS(rs.getString("R13_FD_7_12_MONTHS")); obj.setR13_FD_13_18_MONTHS(rs.getString("R13_FD_13_18_MONTHS"));
			obj.setR13_FD_19_24_MONTHS(rs.getString("R13_FD_19_24_MONTHS")); obj.setR13_FD_OVER_24_MONTHS(rs.getString("R13_FD_OVER_24_MONTHS"));
			obj.setR13_TOTAL(rs.getString("R13_TOTAL"));

			obj.setR14_CURRENCY(rs.getString("R14_CURRENCY")); obj.setR14_CURRENT(rs.getString("R14_CURRENT"));
			obj.setR14_CALL(rs.getString("R14_CALL")); obj.setR14_SAVINGS(rs.getString("R14_SAVINGS"));
			obj.setR14_NOTICE_0_31_DAYS(rs.getString("R14_NOTICE_0_31_DAYS")); obj.setR14_NOTICE_32_88_DAYS(rs.getString("R14_NOTICE_32_88_DAYS"));
			obj.setR14_91_DEPOSIT_DAY(rs.getString("R14_91_DEPOSIT_DAY")); obj.setR14_FD_1_6_MONTHS(rs.getString("R14_FD_1_6_MONTHS"));
			obj.setR14_FD_7_12_MONTHS(rs.getString("R14_FD_7_12_MONTHS")); obj.setR14_FD_13_18_MONTHS(rs.getString("R14_FD_13_18_MONTHS"));
			obj.setR14_FD_19_24_MONTHS(rs.getString("R14_FD_19_24_MONTHS")); obj.setR14_FD_OVER_24_MONTHS(rs.getString("R14_FD_OVER_24_MONTHS"));
			obj.setR14_TOTAL(rs.getString("R14_TOTAL"));

			obj.setR15_CURRENCY(rs.getString("R15_CURRENCY")); obj.setR15_CURRENT(rs.getString("R15_CURRENT"));
			obj.setR15_CALL(rs.getString("R15_CALL")); obj.setR15_SAVINGS(rs.getString("R15_SAVINGS"));
			obj.setR15_NOTICE_0_31_DAYS(rs.getString("R15_NOTICE_0_31_DAYS")); obj.setR15_NOTICE_32_88_DAYS(rs.getString("R15_NOTICE_32_88_DAYS"));
			obj.setR15_91_DEPOSIT_DAY(rs.getString("R15_91_DEPOSIT_DAY")); obj.setR15_FD_1_6_MONTHS(rs.getString("R15_FD_1_6_MONTHS"));
			obj.setR15_FD_7_12_MONTHS(rs.getString("R15_FD_7_12_MONTHS")); obj.setR15_FD_13_18_MONTHS(rs.getString("R15_FD_13_18_MONTHS"));
			obj.setR15_FD_19_24_MONTHS(rs.getString("R15_FD_19_24_MONTHS")); obj.setR15_FD_OVER_24_MONTHS(rs.getString("R15_FD_OVER_24_MONTHS"));
			obj.setR15_TOTAL(rs.getString("R15_TOTAL"));

			return obj;
		}
	}

	class M_INT_RATES_FCAResubSummaryRowMapper implements RowMapper<M_INT_RATES_FCA_RESUB_Summary_Entity> {
		@Override
		public M_INT_RATES_FCA_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_INT_RATES_FCA_RESUB_Summary_Entity obj = new M_INT_RATES_FCA_RESUB_Summary_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			obj.setR10_CURRENCY(rs.getString("R10_CURRENCY")); obj.setR10_CURRENT(rs.getString("R10_CURRENT"));
			obj.setR10_CALL(rs.getString("R10_CALL")); obj.setR10_SAVINGS(rs.getString("R10_SAVINGS"));
			obj.setR10_NOTICE_0_31_DAYS(rs.getString("R10_NOTICE_0_31_DAYS")); obj.setR10_NOTICE_32_88_DAYS(rs.getString("R10_NOTICE_32_88_DAYS"));
			obj.setR10_91_DEPOSIT_DAY(rs.getString("R10_91_DEPOSIT_DAY")); obj.setR10_FD_1_6_MONTHS(rs.getString("R10_FD_1_6_MONTHS"));
			obj.setR10_FD_7_12_MONTHS(rs.getString("R10_FD_7_12_MONTHS")); obj.setR10_FD_13_18_MONTHS(rs.getString("R10_FD_13_18_MONTHS"));
			obj.setR10_FD_19_24_MONTHS(rs.getString("R10_FD_19_24_MONTHS")); obj.setR10_FD_OVER_24_MONTHS(rs.getString("R10_FD_OVER_24_MONTHS"));
			obj.setR10_TOTAL(rs.getString("R10_TOTAL"));

			obj.setR11_CURRENCY(rs.getString("R11_CURRENCY")); obj.setR11_CURRENT(rs.getString("R11_CURRENT"));
			obj.setR11_CALL(rs.getString("R11_CALL")); obj.setR11_SAVINGS(rs.getString("R11_SAVINGS"));
			obj.setR11_NOTICE_0_31_DAYS(rs.getString("R11_NOTICE_0_31_DAYS")); obj.setR11_NOTICE_32_88_DAYS(rs.getString("R11_NOTICE_32_88_DAYS"));
			obj.setR11_91_DEPOSIT_DAY(rs.getString("R11_91_DEPOSIT_DAY")); obj.setR11_FD_1_6_MONTHS(rs.getString("R11_FD_1_6_MONTHS"));
			obj.setR11_FD_7_12_MONTHS(rs.getString("R11_FD_7_12_MONTHS")); obj.setR11_FD_13_18_MONTHS(rs.getString("R11_FD_13_18_MONTHS"));
			obj.setR11_FD_19_24_MONTHS(rs.getString("R11_FD_19_24_MONTHS")); obj.setR11_FD_OVER_24_MONTHS(rs.getString("R11_FD_OVER_24_MONTHS"));
			obj.setR11_TOTAL(rs.getString("R11_TOTAL"));

			obj.setR12_CURRENCY(rs.getString("R12_CURRENCY")); obj.setR12_CURRENT(rs.getString("R12_CURRENT"));
			obj.setR12_CALL(rs.getString("R12_CALL")); obj.setR12_SAVINGS(rs.getString("R12_SAVINGS"));
			obj.setR12_NOTICE_0_31_DAYS(rs.getString("R12_NOTICE_0_31_DAYS")); obj.setR12_NOTICE_32_88_DAYS(rs.getString("R12_NOTICE_32_88_DAYS"));
			obj.setR12_91_DEPOSIT_DAY(rs.getString("R12_91_DEPOSIT_DAY")); obj.setR12_FD_1_6_MONTHS(rs.getString("R12_FD_1_6_MONTHS"));
			obj.setR12_FD_7_12_MONTHS(rs.getString("R12_FD_7_12_MONTHS")); obj.setR12_FD_13_18_MONTHS(rs.getString("R12_FD_13_18_MONTHS"));
			obj.setR12_FD_19_24_MONTHS(rs.getString("R12_FD_19_24_MONTHS")); obj.setR12_FD_OVER_24_MONTHS(rs.getString("R12_FD_OVER_24_MONTHS"));
			obj.setR12_TOTAL(rs.getString("R12_TOTAL"));

			obj.setR13_CURRENCY(rs.getString("R13_CURRENCY")); obj.setR13_CURRENT(rs.getString("R13_CURRENT"));
			obj.setR13_CALL(rs.getString("R13_CALL")); obj.setR13_SAVINGS(rs.getString("R13_SAVINGS"));
			obj.setR13_NOTICE_0_31_DAYS(rs.getString("R13_NOTICE_0_31_DAYS")); obj.setR13_NOTICE_32_88_DAYS(rs.getString("R13_NOTICE_32_88_DAYS"));
			obj.setR13_91_DEPOSIT_DAY(rs.getString("R13_91_DEPOSIT_DAY")); obj.setR13_FD_1_6_MONTHS(rs.getString("R13_FD_1_6_MONTHS"));
			obj.setR13_FD_7_12_MONTHS(rs.getString("R13_FD_7_12_MONTHS")); obj.setR13_FD_13_18_MONTHS(rs.getString("R13_FD_13_18_MONTHS"));
			obj.setR13_FD_19_24_MONTHS(rs.getString("R13_FD_19_24_MONTHS")); obj.setR13_FD_OVER_24_MONTHS(rs.getString("R13_FD_OVER_24_MONTHS"));
			obj.setR13_TOTAL(rs.getString("R13_TOTAL"));

			obj.setR14_CURRENCY(rs.getString("R14_CURRENCY")); obj.setR14_CURRENT(rs.getString("R14_CURRENT"));
			obj.setR14_CALL(rs.getString("R14_CALL")); obj.setR14_SAVINGS(rs.getString("R14_SAVINGS"));
			obj.setR14_NOTICE_0_31_DAYS(rs.getString("R14_NOTICE_0_31_DAYS")); obj.setR14_NOTICE_32_88_DAYS(rs.getString("R14_NOTICE_32_88_DAYS"));
			obj.setR14_91_DEPOSIT_DAY(rs.getString("R14_91_DEPOSIT_DAY")); obj.setR14_FD_1_6_MONTHS(rs.getString("R14_FD_1_6_MONTHS"));
			obj.setR14_FD_7_12_MONTHS(rs.getString("R14_FD_7_12_MONTHS")); obj.setR14_FD_13_18_MONTHS(rs.getString("R14_FD_13_18_MONTHS"));
			obj.setR14_FD_19_24_MONTHS(rs.getString("R14_FD_19_24_MONTHS")); obj.setR14_FD_OVER_24_MONTHS(rs.getString("R14_FD_OVER_24_MONTHS"));
			obj.setR14_TOTAL(rs.getString("R14_TOTAL"));

			obj.setR15_CURRENCY(rs.getString("R15_CURRENCY")); obj.setR15_CURRENT(rs.getString("R15_CURRENT"));
			obj.setR15_CALL(rs.getString("R15_CALL")); obj.setR15_SAVINGS(rs.getString("R15_SAVINGS"));
			obj.setR15_NOTICE_0_31_DAYS(rs.getString("R15_NOTICE_0_31_DAYS")); obj.setR15_NOTICE_32_88_DAYS(rs.getString("R15_NOTICE_32_88_DAYS"));
			obj.setR15_91_DEPOSIT_DAY(rs.getString("R15_91_DEPOSIT_DAY")); obj.setR15_FD_1_6_MONTHS(rs.getString("R15_FD_1_6_MONTHS"));
			obj.setR15_FD_7_12_MONTHS(rs.getString("R15_FD_7_12_MONTHS")); obj.setR15_FD_13_18_MONTHS(rs.getString("R15_FD_13_18_MONTHS"));
			obj.setR15_FD_19_24_MONTHS(rs.getString("R15_FD_19_24_MONTHS")); obj.setR15_FD_OVER_24_MONTHS(rs.getString("R15_FD_OVER_24_MONTHS"));
			obj.setR15_TOTAL(rs.getString("R15_TOTAL"));

			return obj;
		}
	}

	class M_INT_RATES_FCAResubDetailRowMapper implements RowMapper<M_INT_RATES_FCA_RESUB_Detail_Entity> {
		@Override
		public M_INT_RATES_FCA_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_INT_RATES_FCA_RESUB_Detail_Entity obj = new M_INT_RATES_FCA_RESUB_Detail_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			obj.setR10_CURRENCY(rs.getString("R10_CURRENCY")); obj.setR10_CURRENT(rs.getString("R10_CURRENT"));
			obj.setR10_CALL(rs.getString("R10_CALL")); obj.setR10_SAVINGS(rs.getString("R10_SAVINGS"));
			obj.setR10_NOTICE_0_31_DAYS(rs.getString("R10_NOTICE_0_31_DAYS")); obj.setR10_NOTICE_32_88_DAYS(rs.getString("R10_NOTICE_32_88_DAYS"));
			obj.setR10_91_DEPOSIT_DAY(rs.getString("R10_91_DEPOSIT_DAY")); obj.setR10_FD_1_6_MONTHS(rs.getString("R10_FD_1_6_MONTHS"));
			obj.setR10_FD_7_12_MONTHS(rs.getString("R10_FD_7_12_MONTHS")); obj.setR10_FD_13_18_MONTHS(rs.getString("R10_FD_13_18_MONTHS"));
			obj.setR10_FD_19_24_MONTHS(rs.getString("R10_FD_19_24_MONTHS")); obj.setR10_FD_OVER_24_MONTHS(rs.getString("R10_FD_OVER_24_MONTHS"));
			obj.setR10_TOTAL(rs.getString("R10_TOTAL"));

			obj.setR11_CURRENCY(rs.getString("R11_CURRENCY")); obj.setR11_CURRENT(rs.getString("R11_CURRENT"));
			obj.setR11_CALL(rs.getString("R11_CALL")); obj.setR11_SAVINGS(rs.getString("R11_SAVINGS"));
			obj.setR11_NOTICE_0_31_DAYS(rs.getString("R11_NOTICE_0_31_DAYS")); obj.setR11_NOTICE_32_88_DAYS(rs.getString("R11_NOTICE_32_88_DAYS"));
			obj.setR11_91_DEPOSIT_DAY(rs.getString("R11_91_DEPOSIT_DAY")); obj.setR11_FD_1_6_MONTHS(rs.getString("R11_FD_1_6_MONTHS"));
			obj.setR11_FD_7_12_MONTHS(rs.getString("R11_FD_7_12_MONTHS")); obj.setR11_FD_13_18_MONTHS(rs.getString("R11_FD_13_18_MONTHS"));
			obj.setR11_FD_19_24_MONTHS(rs.getString("R11_FD_19_24_MONTHS")); obj.setR11_FD_OVER_24_MONTHS(rs.getString("R11_FD_OVER_24_MONTHS"));
			obj.setR11_TOTAL(rs.getString("R11_TOTAL"));

			obj.setR12_CURRENCY(rs.getString("R12_CURRENCY")); obj.setR12_CURRENT(rs.getString("R12_CURRENT"));
			obj.setR12_CALL(rs.getString("R12_CALL")); obj.setR12_SAVINGS(rs.getString("R12_SAVINGS"));
			obj.setR12_NOTICE_0_31_DAYS(rs.getString("R12_NOTICE_0_31_DAYS")); obj.setR12_NOTICE_32_88_DAYS(rs.getString("R12_NOTICE_32_88_DAYS"));
			obj.setR12_91_DEPOSIT_DAY(rs.getString("R12_91_DEPOSIT_DAY")); obj.setR12_FD_1_6_MONTHS(rs.getString("R12_FD_1_6_MONTHS"));
			obj.setR12_FD_7_12_MONTHS(rs.getString("R12_FD_7_12_MONTHS")); obj.setR12_FD_13_18_MONTHS(rs.getString("R12_FD_13_18_MONTHS"));
			obj.setR12_FD_19_24_MONTHS(rs.getString("R12_FD_19_24_MONTHS")); obj.setR12_FD_OVER_24_MONTHS(rs.getString("R12_FD_OVER_24_MONTHS"));
			obj.setR12_TOTAL(rs.getString("R12_TOTAL"));

			obj.setR13_CURRENCY(rs.getString("R13_CURRENCY")); obj.setR13_CURRENT(rs.getString("R13_CURRENT"));
			obj.setR13_CALL(rs.getString("R13_CALL")); obj.setR13_SAVINGS(rs.getString("R13_SAVINGS"));
			obj.setR13_NOTICE_0_31_DAYS(rs.getString("R13_NOTICE_0_31_DAYS")); obj.setR13_NOTICE_32_88_DAYS(rs.getString("R13_NOTICE_32_88_DAYS"));
			obj.setR13_91_DEPOSIT_DAY(rs.getString("R13_91_DEPOSIT_DAY")); obj.setR13_FD_1_6_MONTHS(rs.getString("R13_FD_1_6_MONTHS"));
			obj.setR13_FD_7_12_MONTHS(rs.getString("R13_FD_7_12_MONTHS")); obj.setR13_FD_13_18_MONTHS(rs.getString("R13_FD_13_18_MONTHS"));
			obj.setR13_FD_19_24_MONTHS(rs.getString("R13_FD_19_24_MONTHS")); obj.setR13_FD_OVER_24_MONTHS(rs.getString("R13_FD_OVER_24_MONTHS"));
			obj.setR13_TOTAL(rs.getString("R13_TOTAL"));

			obj.setR14_CURRENCY(rs.getString("R14_CURRENCY")); obj.setR14_CURRENT(rs.getString("R14_CURRENT"));
			obj.setR14_CALL(rs.getString("R14_CALL")); obj.setR14_SAVINGS(rs.getString("R14_SAVINGS"));
			obj.setR14_NOTICE_0_31_DAYS(rs.getString("R14_NOTICE_0_31_DAYS")); obj.setR14_NOTICE_32_88_DAYS(rs.getString("R14_NOTICE_32_88_DAYS"));
			obj.setR14_91_DEPOSIT_DAY(rs.getString("R14_91_DEPOSIT_DAY")); obj.setR14_FD_1_6_MONTHS(rs.getString("R14_FD_1_6_MONTHS"));
			obj.setR14_FD_7_12_MONTHS(rs.getString("R14_FD_7_12_MONTHS")); obj.setR14_FD_13_18_MONTHS(rs.getString("R14_FD_13_18_MONTHS"));
			obj.setR14_FD_19_24_MONTHS(rs.getString("R14_FD_19_24_MONTHS")); obj.setR14_FD_OVER_24_MONTHS(rs.getString("R14_FD_OVER_24_MONTHS"));
			obj.setR14_TOTAL(rs.getString("R14_TOTAL"));

			obj.setR15_CURRENCY(rs.getString("R15_CURRENCY")); obj.setR15_CURRENT(rs.getString("R15_CURRENT"));
			obj.setR15_CALL(rs.getString("R15_CALL")); obj.setR15_SAVINGS(rs.getString("R15_SAVINGS"));
			obj.setR15_NOTICE_0_31_DAYS(rs.getString("R15_NOTICE_0_31_DAYS")); obj.setR15_NOTICE_32_88_DAYS(rs.getString("R15_NOTICE_32_88_DAYS"));
			obj.setR15_91_DEPOSIT_DAY(rs.getString("R15_91_DEPOSIT_DAY")); obj.setR15_FD_1_6_MONTHS(rs.getString("R15_FD_1_6_MONTHS"));
			obj.setR15_FD_7_12_MONTHS(rs.getString("R15_FD_7_12_MONTHS")); obj.setR15_FD_13_18_MONTHS(rs.getString("R15_FD_13_18_MONTHS"));
			obj.setR15_FD_19_24_MONTHS(rs.getString("R15_FD_19_24_MONTHS")); obj.setR15_FD_OVER_24_MONTHS(rs.getString("R15_FD_OVER_24_MONTHS"));
			obj.setR15_TOTAL(rs.getString("R15_TOTAL"));

			return obj;
		}
	}

}
